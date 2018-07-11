package   com  .  enterprisedt  .  net  .  ftp  ; 

import   java  .  io  .  IOException  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  util  .  Date  ; 
import   com  .  enterprisedt  .  net  .  ftp  .  internal  .  ConnectionContext  ; 
import   com  .  enterprisedt  .  net  .  ftp  .  internal  .  EventAggregator  ; 
import   com  .  enterprisedt  .  util  .  debug  .  Logger  ; 









public   class   FileTransferClient   implements   FileTransferClientInterface  { 

private   Logger   log  =  Logger  .  getLogger  (  "FileTransferClient"  )  ; 







protected   ConnectionContext   masterContext  =  new   ConnectionContext  (  )  ; 

protected   EventAggregator   eventAggregator  =  null  ; 




protected   EventListener   listener  ; 




private   FTPClient   ftpClient  ; 




private   AdvancedFTPSettings   advancedFTPSettings  ; 

private   AdvancedGeneralSettings   advancedSettings  ; 

private   FileStatistics   statistics  ; 




public   FileTransferClient  (  )  { 
ftpClient  =  new   FTPClient  (  )  ; 
advancedFTPSettings  =  new   AdvancedFTPSettings  (  masterContext  )  ; 
advancedSettings  =  new   AdvancedGeneralSettings  (  masterContext  )  ; 
statistics  =  new   FileStatistics  (  )  ; 
statistics  .  addClient  (  ftpClient  )  ; 
} 







protected   void   checkConnection  (  boolean   shouldBeConnected  )  throws   FTPException  { 
if  (  shouldBeConnected  &&  !  isConnected  (  )  )  throw   new   FTPException  (  "The file transfer client has not yet connected to the server.  "  +  "The requested action cannot be performed until after a connection has been established."  )  ;  else   if  (  !  shouldBeConnected  &&  isConnected  (  )  )  throw   new   FTPException  (  "The file transfer client has already been connected to the server.  "  +  "The requested action must be performed before a connection is established."  )  ; 
} 






public   synchronized   boolean   isConnected  (  )  { 
return   ftpClient  .  connected  (  )  ; 
} 






public   synchronized   String   getRemoteHost  (  )  { 
return   masterContext  .  getRemoteHost  (  )  ; 
} 









public   synchronized   void   setRemoteHost  (  String   remoteHost  )  throws   FTPException  { 
checkConnection  (  false  )  ; 
masterContext  .  setRemoteHost  (  remoteHost  )  ; 
} 






public   synchronized   int   getTimeout  (  )  { 
return   masterContext  .  getTimeout  (  )  ; 
} 








public   synchronized   void   setTimeout  (  int   timeout  )  throws   FTPException  { 
checkConnection  (  false  )  ; 
masterContext  .  setTimeout  (  timeout  )  ; 
} 






public   synchronized   int   getRemotePort  (  )  { 
return   masterContext  .  getRemotePort  (  )  ; 
} 








public   synchronized   void   setRemotePort  (  int   remotePort  )  throws   FTPException  { 
checkConnection  (  false  )  ; 
masterContext  .  setRemotePort  (  remotePort  )  ; 
} 










public   synchronized   void   setContentType  (  FTPTransferType   type  )  throws   IOException  ,  FTPException  { 
masterContext  .  setContentType  (  type  )  ; 
if  (  ftpClient  !=  null  &&  ftpClient  .  connected  (  )  )  ftpClient  .  setType  (  type  )  ; 
} 






public   synchronized   FTPTransferType   getContentType  (  )  { 
return   masterContext  .  getContentType  (  )  ; 
} 











public   void   setDetectContentType  (  boolean   detectContentType  )  { 
masterContext  .  setDetectContentType  (  detectContentType  )  ; 
if  (  ftpClient  !=  null  )  ftpClient  .  setDetectTransferMode  (  detectContentType  )  ; 
} 






public   boolean   isDetectContentType  (  )  { 
return   masterContext  .  getDetectContentType  (  )  ; 
} 








public   synchronized   void   setUserName  (  String   userName  )  throws   FTPException  { 
checkConnection  (  false  )  ; 
masterContext  .  setUserName  (  userName  )  ; 
} 






public   synchronized   String   getPassword  (  )  { 
return   masterContext  .  getPassword  (  )  ; 
} 








public   synchronized   void   setPassword  (  String   password  )  throws   FTPException  { 
checkConnection  (  false  )  ; 
masterContext  .  setPassword  (  password  )  ; 
} 






public   synchronized   String   getUserName  (  )  { 
return   masterContext  .  getUserName  (  )  ; 
} 






public   synchronized   AdvancedFTPSettings   getAdvancedFTPSettings  (  )  { 
return   advancedFTPSettings  ; 
} 







public   synchronized   AdvancedGeneralSettings   getAdvancedSettings  (  )  { 
return   advancedSettings  ; 
} 






public   synchronized   void   setEventListener  (  EventListener   listener  )  { 
this  .  listener  =  listener  ; 
eventAggregator  =  new   EventAggregator  (  listener  )  ; 
if  (  ftpClient  !=  null  )  { 
eventAggregator  .  setConnId  (  ftpClient  .  getId  (  )  )  ; 
ftpClient  .  setMessageListener  (  eventAggregator  )  ; 
ftpClient  .  setProgressMonitor  (  eventAggregator  )  ; 
ftpClient  .  setProgressMonitorEx  (  eventAggregator  )  ; 
} 
} 







public   synchronized   void   connect  (  )  throws   FTPException  ,  IOException  { 
if  (  eventAggregator  !=  null  )  { 
eventAggregator  .  setConnId  (  ftpClient  .  getId  (  )  )  ; 
ftpClient  .  setMessageListener  (  eventAggregator  )  ; 
ftpClient  .  setProgressMonitor  (  eventAggregator  )  ; 
ftpClient  .  setProgressMonitorEx  (  eventAggregator  )  ; 
} 
statistics  .  clear  (  )  ; 
configureClient  (  )  ; 
log  .  debug  (  "Configured client"  )  ; 
ftpClient  .  connect  (  )  ; 
log  .  debug  (  "Client connected"  )  ; 
if  (  masterContext  .  isAutoLogin  (  )  )  { 
log  .  debug  (  "Logging in"  )  ; 
ftpClient  .  login  (  masterContext  .  getUserName  (  )  ,  masterContext  .  getPassword  (  )  )  ; 
log  .  debug  (  "Logged in"  )  ; 
configureTransferType  (  masterContext  .  getContentType  (  )  )  ; 
}  else  { 
log  .  debug  (  "Manual login enabled"  )  ; 
} 
} 









public   void   manualLogin  (  )  throws   FTPException  ,  IOException  { 
checkConnection  (  true  )  ; 
log  .  debug  (  "Logging in"  )  ; 
ftpClient  .  login  (  masterContext  .  getUserName  (  )  ,  masterContext  .  getPassword  (  )  )  ; 
log  .  debug  (  "Logged in"  )  ; 
configureTransferType  (  masterContext  .  getContentType  (  )  )  ; 
} 







private   void   configureClient  (  )  throws   IOException  ,  FTPException  { 
ftpClient  .  setRemoteHost  (  masterContext  .  getRemoteHost  (  )  )  ; 
ftpClient  .  setRemotePort  (  masterContext  .  getRemotePort  (  )  )  ; 
ftpClient  .  setTimeout  (  masterContext  .  getTimeout  (  )  )  ; 
ftpClient  .  setControlEncoding  (  masterContext  .  getControlEncoding  (  )  )  ; 
ftpClient  .  setStrictReturnCodes  (  masterContext  .  isStrictReturnCodes  (  )  )  ; 
ftpClient  .  setDetectTransferMode  (  masterContext  .  getDetectContentType  (  )  )  ; 
ftpClient  .  setConnectMode  (  masterContext  .  getConnectMode  (  )  )  ; 
ftpClient  .  setParserLocales  (  masterContext  .  getParserLocales  (  )  )  ; 
ftpClient  .  setAutoPassiveIPSubstitution  (  masterContext  .  isAutoPassiveIPSubstitution  (  )  )  ; 
ftpClient  .  setDeleteOnFailure  (  masterContext  .  isDeleteOnFailure  (  )  )  ; 
ftpClient  .  setActiveIPAddress  (  masterContext  .  getActiveIPAddress  (  )  )  ; 
ftpClient  .  setMonitorInterval  (  masterContext  .  getTransferNotifyInterval  (  )  )  ; 
ftpClient  .  setTransferBufferSize  (  masterContext  .  getTransferBufferSize  (  )  )  ; 
ftpClient  .  setFileNotFoundMessages  (  masterContext  .  getFileNotFoundMessages  (  )  )  ; 
ftpClient  .  setDirectoryEmptyMessages  (  masterContext  .  getDirectoryEmptyMessages  (  )  )  ; 
ftpClient  .  setTransferCompleteMessages  (  masterContext  .  getTransferCompleteMessages  (  )  )  ; 
if  (  masterContext  .  getActiveHighPort  (  )  >=  0  &&  masterContext  .  getActiveLowPort  (  )  >=  0  )  ftpClient  .  setActivePortRange  (  masterContext  .  getActiveLowPort  (  )  ,  masterContext  .  getActiveHighPort  (  )  )  ; 
} 

private   void   configureTransferType  (  FTPTransferType   type  )  throws   IOException  ,  FTPException  { 
ftpClient  .  setDetectTransferMode  (  masterContext  .  getDetectContentType  (  )  )  ; 
ftpClient  .  setType  (  type  )  ; 
} 

private   void   checkTransferSettings  (  )  throws   FTPException  { 
if  (  ftpClient  .  getDetectTransferMode  (  )  !=  masterContext  .  getDetectContentType  (  )  )  ftpClient  .  setDetectTransferMode  (  masterContext  .  getDetectContentType  (  )  )  ; 
if  (  ftpClient  .  isStrictReturnCodes  (  )  !=  masterContext  .  isStrictReturnCodes  (  )  )  ftpClient  .  setStrictReturnCodes  (  masterContext  .  isStrictReturnCodes  (  )  )  ; 
if  (  !  ftpClient  .  getConnectMode  (  )  .  equals  (  masterContext  .  getConnectMode  (  )  )  )  ftpClient  .  setConnectMode  (  masterContext  .  getConnectMode  (  )  )  ; 
if  (  ftpClient  .  isAutoPassiveIPSubstitution  (  )  !=  masterContext  .  isAutoPassiveIPSubstitution  (  )  )  ftpClient  .  setAutoPassiveIPSubstitution  (  masterContext  .  isAutoPassiveIPSubstitution  (  )  )  ; 
if  (  ftpClient  .  isDeleteOnFailure  (  )  !=  masterContext  .  isDeleteOnFailure  (  )  )  ftpClient  .  setDeleteOnFailure  (  masterContext  .  isDeleteOnFailure  (  )  )  ; 
if  (  ftpClient  .  getActiveIPAddress  (  )  !=  masterContext  .  getActiveIPAddress  (  )  )  ftpClient  .  setActiveIPAddress  (  masterContext  .  getActiveIPAddress  (  )  )  ; 
if  (  ftpClient  .  getTransferBufferSize  (  )  !=  masterContext  .  getTransferBufferSize  (  )  )  ftpClient  .  setTransferBufferSize  (  masterContext  .  getTransferBufferSize  (  )  )  ; 
if  (  ftpClient  .  getMonitorInterval  (  )  !=  masterContext  .  getTransferNotifyInterval  (  )  )  ftpClient  .  setMonitorInterval  (  masterContext  .  getTransferNotifyInterval  (  )  )  ; 
if  (  masterContext  .  getActiveHighPort  (  )  !=  ftpClient  .  getActiveHighPort  (  )  ||  masterContext  .  getActiveLowPort  (  )  !=  ftpClient  .  getActiveLowPort  (  )  )  ftpClient  .  setActivePortRange  (  masterContext  .  getActiveLowPort  (  )  ,  masterContext  .  getActiveHighPort  (  )  )  ; 
} 

private   void   checkListingSettings  (  )  throws   FTPException  { 
ftpClient  .  setParserLocales  (  masterContext  .  getParserLocales  (  )  )  ; 
checkTransferSettings  (  )  ; 
} 






public   synchronized   FileStatistics   getStatistics  (  )  { 
return   statistics  ; 
} 











public   synchronized   String   executeCommand  (  String   command  )  throws   FTPException  ,  IOException  { 
return   ftpClient  .  quote  (  command  )  ; 
} 




public   void   cancelAllTransfers  (  )  { 
log  .  debug  (  "cancelAllTransfers() called"  )  ; 
ftpClient  .  cancelTransfer  (  )  ; 
} 









public   synchronized   String   getSystemType  (  )  throws   FTPException  ,  IOException  { 
return   ftpClient  .  system  (  )  ; 
} 







public   synchronized   String  [  ]  directoryNameList  (  )  throws   FTPException  ,  IOException  { 
return   directoryNameList  (  ""  ,  false  )  ; 
} 










public   synchronized   String  [  ]  directoryNameList  (  String   directoryName  ,  boolean   isLongListing  )  throws   FTPException  ,  IOException  { 
checkListingSettings  (  )  ; 
return   ftpClient  .  dir  (  directoryName  ,  isLongListing  )  ; 
} 







public   synchronized   FTPFile  [  ]  directoryList  (  )  throws   FTPException  ,  IOException  ,  ParseException  { 
return   directoryList  (  ""  )  ; 
} 









public   synchronized   FTPFile  [  ]  directoryList  (  String   directoryName  )  throws   FTPException  ,  IOException  ,  ParseException  { 
checkListingSettings  (  )  ; 
return   ftpClient  .  dirDetails  (  directoryName  )  ; 
} 







public   synchronized   byte  [  ]  downloadByteArray  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
checkTransferSettings  (  )  ; 
return   ftpClient  .  get  (  remoteFileName  )  ; 
} 








public   synchronized   void   downloadFile  (  String   localFileName  ,  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
downloadFile  (  localFileName  ,  remoteFileName  ,  WriteMode  .  OVERWRITE  )  ; 
} 









public   synchronized   void   downloadFile  (  String   localFileName  ,  String   remoteFileName  ,  WriteMode   writeMode  )  throws   FTPException  ,  IOException  { 
checkTransferSettings  (  )  ; 
if  (  writeMode  .  equals  (  WriteMode  .  RESUME  )  )  { 
ftpClient  .  resume  (  )  ; 
}  else   if  (  writeMode  .  equals  (  WriteMode  .  APPEND  )  )  { 
throw   new   FTPException  (  "Append not permitted for downloads"  )  ; 
} 
ftpClient  .  get  (  localFileName  ,  remoteFileName  )  ; 
} 









public   synchronized   FileTransferInputStream   downloadStream  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
checkTransferSettings  (  )  ; 
return   new   FTPInputStream  (  ftpClient  ,  remoteFileName  )  ; 
} 













public   synchronized   String   uploadFile  (  String   localFileName  ,  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
return   uploadFile  (  localFileName  ,  remoteFileName  ,  WriteMode  .  OVERWRITE  )  ; 
} 














public   synchronized   String   uploadFile  (  String   localFileName  ,  String   remoteFileName  ,  WriteMode   writeMode  )  throws   FTPException  ,  IOException  { 
checkTransferSettings  (  )  ; 
boolean   append  =  false  ; 
if  (  writeMode  .  equals  (  WriteMode  .  RESUME  )  )  { 
ftpClient  .  resume  (  )  ; 
}  else   if  (  writeMode  .  equals  (  WriteMode  .  APPEND  )  )  { 
append  =  true  ; 
} 
return   ftpClient  .  put  (  localFileName  ,  remoteFileName  ,  append  )  ; 
} 














public   synchronized   FileTransferOutputStream   uploadStream  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
return   uploadStream  (  remoteFileName  ,  WriteMode  .  OVERWRITE  )  ; 
} 















public   synchronized   FileTransferOutputStream   uploadStream  (  String   remoteFileName  ,  WriteMode   writeMode  )  throws   FTPException  ,  IOException  { 
checkTransferSettings  (  )  ; 
if  (  WriteMode  .  RESUME  .  equals  (  writeMode  )  )  throw   new   FTPException  (  "Resume not supported for stream uploads"  )  ; 
boolean   append  =  WriteMode  .  APPEND  .  equals  (  writeMode  )  ; 
return   new   FTPOutputStream  (  ftpClient  ,  remoteFileName  ,  append  )  ; 
} 








public   synchronized   long   getSize  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
return   ftpClient  .  size  (  remoteFileName  )  ; 
} 








public   synchronized   Date   getModifiedTime  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
return   ftpClient  .  modtime  (  remoteFileName  )  ; 
} 









public   synchronized   void   setModifiedTime  (  String   remoteFileName  ,  Date   modifiedTime  )  throws   FTPException  ,  IOException  { 
ftpClient  .  setModTime  (  remoteFileName  ,  modifiedTime  )  ; 
} 







public   synchronized   boolean   exists  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
return   ftpClient  .  exists  (  remoteFileName  )  ; 
} 








public   synchronized   void   deleteFile  (  String   remoteFileName  )  throws   FTPException  ,  IOException  { 
ftpClient  .  delete  (  remoteFileName  )  ; 
} 










public   synchronized   void   rename  (  String   renameFromName  ,  String   renameToName  )  throws   FTPException  ,  IOException  { 
ftpClient  .  rename  (  renameFromName  ,  renameToName  )  ; 
} 








public   synchronized   void   changeDirectory  (  String   directoryName  )  throws   FTPException  ,  IOException  { 
ftpClient  .  chdir  (  directoryName  )  ; 
} 






public   synchronized   void   changeToParentDirectory  (  )  throws   FTPException  ,  IOException  { 
ftpClient  .  cdup  (  )  ; 
} 








public   synchronized   String   getRemoteDirectory  (  )  throws   IOException  ,  FTPException  { 
return   ftpClient  .  pwd  (  )  ; 
} 








public   synchronized   void   createDirectory  (  String   directoryName  )  throws   FTPException  ,  IOException  { 
ftpClient  .  mkdir  (  directoryName  )  ; 
} 









public   synchronized   void   deleteDirectory  (  String   directoryName  )  throws   FTPException  ,  IOException  { 
ftpClient  .  rmdir  (  directoryName  )  ; 
} 






public   synchronized   void   disconnect  (  )  throws   FTPException  ,  IOException  { 
ftpClient  .  quit  (  )  ; 
} 






public   synchronized   void   disconnect  (  boolean   immediate  )  throws   FTPException  ,  IOException  { 
if  (  immediate  )  ftpClient  .  quitImmediately  (  )  ;  else   ftpClient  .  quit  (  )  ; 
} 
} 

