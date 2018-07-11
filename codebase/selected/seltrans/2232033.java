package   allensoft  .  javacvs  .  client  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  prefs  .  *  ; 
import   java  .  util  .  zip  .  *  ; 
import   java  .  text  .  *  ; 
import   allensoft  .  util  .  *  ; 
import   allensoft  .  io  .  *  ; 
import   allensoft  .  javacvs  .  client  .  event  .  *  ; 





















































public   class   CVSClient  { 

private   static   Preferences   prefs  =  Preferences  .  userNodeForPackage  (  CVSClient  .  class  )  ; 

private   static   ResourceLoader   res  =  ResourceLoader  .  getLoader  (  CVSClient  .  class  )  ; 


public   static   final   String   VERSION  =  "0.2"  ; 



public   CVSClient  (  CVSConnectionManager   connectionManager  ,  LoginManager   loginManager  )  { 
m_ConnectionManager  =  connectionManager  ; 
m_LoginManager  =  loginManager  ; 
} 



public   CVSClient  (  LoginManager   loginManager  )  { 
this  (  new   DefaultConnectionManager  (  )  ,  loginManager  )  ; 
} 

public   CVSRequest   getCurrentRequest  (  )  { 
synchronized  (  m_Lock  )  { 
return   m_CurrentRequest  ; 
} 
} 

public   void   addCVSClientListener  (  CVSClientListener   listener  )  { 
if  (  listener  ==  null  )  throw   new   NullPointerException  (  "listener was null"  )  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  )  m_Listeners  =  new   ArrayList  (  4  )  ; 
m_Listeners  .  add  (  listener  )  ; 
} 
} 

public   void   removeCVSClientListener  (  CVSClientListener   listener  )  { 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  !=  null  )  m_Listeners  .  remove  (  listener  )  ; 
} 
} 

public   static   int   getDefaultRemoteZipCompressionLevel  (  )  { 
return   prefs  .  getInt  (  "defaultRemoteZipLevel"  ,  9  )  ; 
} 

public   static   void   setDefaultRemoteZipCompressionLevel  (  int   n  )  { 
prefs  .  putInt  (  "defaultRemoteZipLevel"  ,  n  )  ; 
} 

public   boolean   isPerformingRequests  (  )  { 
return   m_bPerformingRequests  ; 
} 


public   CVSConnectionManager   getConnectionManager  (  )  { 
return   m_ConnectionManager  ; 
} 


public   void   setConnectionManager  (  CVSConnectionManager   manager  )  { 
m_ConnectionManager  =  manager  ; 
} 


public   LoginManager   getLoginManager  (  )  { 
return   m_LoginManager  ; 
} 


public   void   setLoginManager  (  LoginManager   manager  )  { 
m_LoginManager  =  manager  ; 
} 

public   String   getValidResponses  (  )  { 
return   m_sValidResponses  ; 
} 

public   void   setValidResponses  (  String   sResponses  )  { 
m_sValidResponses  =  sResponses  ; 
} 

public   boolean   getUseUnchanged  (  )  { 
return   m_bUseUnchanged  ; 
} 

public   void   setUseUnchanged  (  boolean   b  )  { 
m_bUseUnchanged  =  b  ; 
} 





public   FileFilter   getIgnoreFileFilter  (  )  { 
return   m_IgnoreFileFilter  ; 
} 





public   void   setIgnoreFileFilter  (  FileFilter   filter  )  { 
m_IgnoreFileFilter  =  filter  ; 
} 

public   GlobalOptions   getGlobalOptions  (  )  { 
return   m_GlobalOptions  ; 
} 

public   void   setGlobalOptions  (  GlobalOptions   options  )  { 
m_GlobalOptions  =  options  ; 
} 

public   boolean   isRequestValid  (  String   sRequest  )  { 
return  (  m_sValidRequests  ==  null  )  ?  true  :  m_sValidRequests  .  indexOf  (  sRequest  )  !=  -  1  ; 
} 















public   CVSResponse   performRequest  (  CVSRequest   request  ,  boolean   bSendImmediately  )  throws   IOException  ,  CVSException  { 
performRequestBatch  (  new   CVSRequestBatch  (  request  )  ,  bSendImmediately  )  ; 
return   request  .  getResponse  (  )  ; 
} 

public   CVSResponse   performRequest  (  CVSRequest   request  )  throws   IOException  ,  CVSException  { 
return   performRequest  (  request  ,  false  )  ; 
} 






public   void   performRequestBatch  (  CVSRequestBatch   batch  ,  boolean   bSendImmediately  )  throws   IOException  ,  CVSException  { 
List   requests  =  new   LinkedList  (  )  ; 
synchronized  (  batch  )  { 
for  (  int   i  =  0  ;  i  <  batch  .  getRequestCount  (  )  ;  i  ++  )  { 
CVSRequest   request  =  batch  .  getRequest  (  i  )  ; 
request  .  m_Client  =  this  ; 
if  (  request  .  validateRequest  (  )  )  requests  .  add  (  request  )  ;  else   request  .  m_Client  =  null  ; 
} 
} 
if  (  !  bSendImmediately  )  { 
CVSRequestBatch   requestBatch  =  null  ; 
synchronized  (  m_BatchLock  )  { 
if  (  m_RequestBatch  !=  null  &&  batch  !=  m_RequestBatch  )  { 
requestBatch  =  m_RequestBatch  ; 
Iterator   i  =  requests  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  m_RequestBatch  .  addRequest  (  (  CVSRequest  )  i  .  next  (  )  )  ; 
} 
} 
if  (  requestBatch  !=  null  )  { 
Iterator   i  =  requests  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  (  (  CVSRequest  )  i  .  next  (  )  )  .  waitForSuccessfulCompletion  (  )  ; 
return  ; 
} 
} 
synchronized  (  this  )  { 
Map   requestsMap  =  new   HashMap  (  10  )  ; 
Iterator   i  =  requests  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSRequest   request  =  (  CVSRequest  )  i  .  next  (  )  ; 
if  (  !  request  .  hasCompletedSuccessfully  (  )  )  { 
RepositoryLocation   location  =  request  .  getRepositoryLocation  (  )  ; 
List   requestsList  =  (  List  )  requestsMap  .  get  (  location  )  ; 
if  (  requestsList  ==  null  )  { 
requestsList  =  new   LinkedList  (  )  ; 
requestsMap  .  put  (  location  ,  requestsList  )  ; 
} 
requestsList  .  add  (  request  )  ; 
} 
} 
WorkingDirectory  .  synchWithFileSystem  (  )  ; 
m_AbortException  =  null  ; 
Set   entries  =  requestsMap  .  entrySet  (  )  ; 
i  =  entries  .  iterator  (  )  ; 
fireStartingRequests  (  )  ; 
try  { 
while  (  i  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  i  .  next  (  )  ; 
List   requestsList  =  (  List  )  entry  .  getValue  (  )  ; 
RepositoryLocation   location  =  (  RepositoryLocation  )  entry  .  getKey  (  )  ; 
try  { 
openConnection  (  location  )  ; 
}  catch  (  IOException   e  )  { 
notifyRequestsOfUnsuccessfulConnection  (  requestsList  )  ; 
throw   e  ; 
}  catch  (  CVSException   e  )  { 
notifyRequestsOfUnsuccessfulConnection  (  requestsList  )  ; 
throw   e  ; 
} 
try  { 
Iterator   j  =  requestsList  .  iterator  (  )  ; 
while  (  j  .  hasNext  (  )  )  { 
CVSRequest   request  =  (  CVSRequest  )  j  .  next  (  )  ; 
m_CurrentRequest  =  request  ; 
if  (  m_AbortException  !=  null  )  throw   m_AbortException  ; 
request  .  clientPerformRequest  (  )  ; 
WorkingDirectory  .  writeEntriesFiles  (  )  ; 
} 
}  finally  { 
closeConnection  (  )  ; 
} 
} 
}  finally  { 
fireFinishedRequests  (  )  ; 
} 
} 
} 

private   void   notifyRequestsOfUnsuccessfulConnection  (  List   requests  )  { 
Iterator   i  =  requests  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSRequest   request  =  (  CVSRequest  )  i  .  next  (  )  ; 
request  .  couldNotConnect  (  )  ; 
} 
} 

public   void   performRequestBatch  (  CVSRequestBatch   batch  )  throws   IOException  ,  CVSException  { 
performRequestBatch  (  batch  ,  false  )  ; 
} 




public   void   enterBatchMode  (  )  { 
synchronized  (  m_BatchLock  )  { 
if  (  m_RequestBatch  ==  null  )  { 
m_RequestBatch  =  new   CVSRequestBatch  (  )  ; 
fireEnteredBatchMode  (  )  ; 
} 
} 
} 



public   void   exitBatchMode  (  )  { 
synchronized  (  m_BatchLock  )  { 
if  (  m_RequestBatch  !=  null  )  { 
m_RequestBatch  =  null  ; 
fireExitedBatchMode  (  )  ; 
} 
} 
} 


public   boolean   isInBatchMode  (  )  { 
synchronized  (  m_BatchLock  )  { 
return   m_RequestBatch  !=  null  ; 
} 
} 



public   CVSRequestBatch   getBatch  (  )  { 
synchronized  (  m_BatchLock  )  { 
return   m_RequestBatch  ; 
} 
} 




public   void   sendBatch  (  )  throws   CVSException  ,  IOException  { 
synchronized  (  m_BatchLock  )  { 
if  (  m_RequestBatch  !=  null  )  { 
performRequestBatch  (  m_RequestBatch  ,  true  )  ; 
m_RequestBatch  =  null  ; 
fireExitedBatchMode  (  )  ; 
} 
} 
} 


public   void   abortRequest  (  RequestAbortedException   e  )  { 
m_AbortException  =  e  ; 
} 

public   void   abortRequest  (  )  { 
abortRequest  (  new   RequestAbortedException  (  )  )  ; 
} 

CVSException   getAbortException  (  )  { 
return   m_AbortException  ; 
} 

private   String   getResourceString  (  String   key  )  { 
return   ResourceLoader  .  getString  (  this  ,  CVSClient  .  class  ,  key  )  ; 
} 

File   getCurrentDirectory  (  )  { 
return   m_CurrentRequest  .  getCurrentDirectory  (  )  ; 
} 

RepositoryLocation   getRepositoryLocation  (  )  { 
return   m_RepositoryLocation  ; 
} 


String   getRelativePath  (  File   file  )  { 
return   CVSUtilities  .  getRelativePath  (  getCurrentDirectory  (  )  ,  file  )  ; 
} 


String   getRelativePath  (  FileEntry   file  )  { 
return   getRelativePath  (  file  .  getFile  (  )  )  ; 
} 


String   getRelativePath  (  WorkingDirectory   workingDirectory  )  { 
return   getRelativePath  (  workingDirectory  .  getDirectory  (  )  )  ; 
} 



File   getFileFromRelativePath  (  String   sRelativePath  )  { 
return   CVSUtilities  .  getFileFromRelativePath  (  getCurrentDirectory  (  )  ,  sRelativePath  )  ; 
} 




void   sendText  (  String   sText  )  throws   IOException  { 
byte  [  ]  bytes  =  null  ; 
try  { 
bytes  =  sText  .  getBytes  (  "ASCII"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   RuntimeException  (  "ASCII is not supported"  )  ; 
} 
m_Out  .  write  (  bytes  )  ; 
fireSentText  (  sText  )  ; 
Thread  .  yield  (  )  ; 
} 


void   sendLine  (  String   sText  )  throws   IOException  ,  CVSException  { 
if  (  m_AbortException  !=  null  )  throw   m_AbortException  ; 
StringBuffer   buffer  =  new   StringBuffer  (  sText  )  ; 
buffer  .  append  (  '\n'  )  ; 
sendText  (  buffer  .  toString  (  )  )  ; 
} 


void   sendDirectory  (  File   directory  ,  String   sRepositoryPath  )  throws   IOException  ,  CVSException  { 
if  (  !  directory  .  equals  (  getCurrentDirectory  (  )  )  )  { 
DirectoryEntry   entry  =  null  ; 
try  { 
entry  =  DirectoryEntry  .  getDirectoryEntry  (  directory  )  ; 
}  catch  (  IOException   e  )  { 
}  catch  (  CVSException   e  )  { 
} 
if  (  entry  !=  null  &&  !  m_DirectoriesSent  .  contains  (  entry  .  getWorkingDirectory  (  )  .  getDirectory  (  )  )  )  sendDirectory  (  entry  .  getWorkingDirectory  (  )  )  ; 
} 
if  (  !  directory  .  equals  (  m_LastDirectory  )  )  { 
sendLine  (  "Directory "  +  getRelativePath  (  directory  )  )  ; 
sendLine  (  sRepositoryPath  )  ; 
m_LastDirectory  =  directory  ; 
m_DirectoriesSent  .  add  (  directory  )  ; 
} 
} 


void   sendDirectory  (  WorkingDirectory   workingDirectory  )  throws   IOException  ,  CVSException  { 
sendDirectory  (  workingDirectory  .  getDirectory  (  )  ,  workingDirectory  .  getAbsoluteRepositoryPath  (  )  )  ; 
if  (  !  m_WorkingDirectoriesStateHasBeenSentFor  .  contains  (  workingDirectory  )  )  { 
if  (  m_CurrentRequest  .  getSendQuestionableCommands  (  )  )  { 
File  [  ]  files  =  workingDirectory  .  getDirectory  (  )  .  listFiles  (  m_IgnoreFileFilter  )  ; 
Arrays  .  sort  (  files  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
if  (  workingDirectory  .  getEntry  (  file  )  ==  null  )  sendQuestionable  (  file  .  getName  (  )  )  ; 
} 
} 
if  (  workingDirectory  .  isStaticDirectory  (  )  )  sendStaticDirectory  (  )  ; 
String   sStickyTag  =  workingDirectory  .  getStickyTagSpec  (  )  ; 
if  (  sStickyTag  !=  null  )  sendSticky  (  sStickyTag  )  ; 
String   sCheckinProg  =  workingDirectory  .  getCheckinProgram  (  )  ; 
if  (  sCheckinProg  !=  null  )  sendCheckinProg  (  sCheckinProg  )  ; 
String   sUpdateProg  =  workingDirectory  .  getUpdateProgram  (  )  ; 
if  (  sUpdateProg  !=  null  )  sendUpdateProg  (  sUpdateProg  )  ; 
m_WorkingDirectoriesStateHasBeenSentFor  .  add  (  workingDirectory  )  ; 
} 
} 

void   sendCurrentDirectory  (  )  throws   CVSException  ,  IOException  { 
sendDirectory  (  WorkingDirectory  .  getWorkingDirectory  (  getCurrentDirectory  (  )  )  )  ; 
} 

void   sendStaticDirectory  (  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Static-directory"  )  ; 
} 

void   sendSticky  (  String   sTagSpec  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Sticky "  +  sTagSpec  )  ; 
} 

void   sendCheckinProg  (  String   sProg  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Checkin-prog "  +  sProg  )  ; 
} 

void   sendUpdateProg  (  String   sProg  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Update-prog "  +  sProg  )  ; 
} 


void   sendEntry  (  FileEntry   entry  )  throws   IOException  ,  CVSException  { 
StringBuffer   buffer  =  new   StringBuffer  (  50  )  ; 
buffer  .  append  (  "Entry /"  )  ; 
buffer  .  append  (  entry  .  getName  (  )  )  ; 
buffer  .  append  (  '/'  )  ; 
if  (  entry  .  isLocallyRemoved  (  )  )  { 
buffer  .  append  (  '-'  )  ; 
buffer  .  append  (  entry  .  getRevision  (  )  )  ; 
}  else   if  (  entry  .  isLocallyAdded  (  )  )  buffer  .  append  (  '0'  )  ;  else   buffer  .  append  (  entry  .  getRevision  (  )  )  ; 
buffer  .  append  (  '/'  )  ; 
if  (  entry  .  getHasConflicts  (  )  )  { 
buffer  .  append  (  '+'  )  ; 
if  (  !  entry  .  isModified  (  )  )  buffer  .  append  (  '='  )  ; 
}  else   if  (  entry  .  isLocallyRemoved  (  )  )  { 
if  (  entry  .  getFile  (  )  .  exists  (  )  )  buffer  .  append  (  '='  )  ; 
}  else   if  (  entry  .  isLocallyAdded  (  )  )  ; 
buffer  .  append  (  '/'  )  ; 
buffer  .  append  (  entry  .  getKeywordSubstitutionMode  (  )  .  toString  (  )  )  ; 
buffer  .  append  (  '/'  )  ; 
sendLine  (  buffer  .  toString  (  )  )  ; 
} 



void   sendModified  (  File   file  ,  boolean   isBinary  )  throws   IOException  ,  CVSException  { 
if  (  !  file  .  exists  (  )  )  return  ; 
CVSClient   client  =  this  ; 
sendLine  (  "Modified "  +  file  .  getName  (  )  )  ; 
if  (  UnixFileSupport  .  isEnabled  (  )  )  { 
UnixFilePermissions   permissions  =  UnixFileSupport  .  getPermissions  (  file  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  20  )  ; 
buffer  .  append  (  "u="  )  ; 
if  (  permissions  .  isOwnerReadable  (  )  )  buffer  .  append  (  'r'  )  ; 
if  (  permissions  .  isOwnerWritable  (  )  )  buffer  .  append  (  'w'  )  ; 
if  (  permissions  .  isOwnerExecutable  (  )  )  buffer  .  append  (  'x'  )  ; 
buffer  .  append  (  ",g="  )  ; 
if  (  permissions  .  isGroupReadable  (  )  )  buffer  .  append  (  'r'  )  ; 
if  (  permissions  .  isGroupWritable  (  )  )  buffer  .  append  (  'w'  )  ; 
if  (  permissions  .  isGroupExecutable  (  )  )  buffer  .  append  (  'x'  )  ; 
buffer  .  append  (  ",o="  )  ; 
if  (  permissions  .  isOtherReadable  (  )  )  buffer  .  append  (  'r'  )  ; 
if  (  permissions  .  isOtherWritable  (  )  )  buffer  .  append  (  'w'  )  ; 
if  (  permissions  .  isOtherExecutable  (  )  )  buffer  .  append  (  'x'  )  ; 
sendLine  (  buffer  .  toString  (  )  )  ; 
}  else   sendLine  (  "u=rw,g=r,o=r"  )  ; 
if  (  isBinary  )  sendBinaryFile  (  file  )  ;  else  { 
TextFileFormatter   formatter  =  TextFileFormatter  .  getFormatterForSendingText  (  file  )  ; 
if  (  formatter  ==  null  )  sendTextFile  (  file  )  ;  else  { 
File   tempFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
try  { 
formatter  .  formatTextFile  (  file  ,  tempFile  )  ; 
sendTextFile  (  tempFile  )  ; 
}  finally  { 
tempFile  .  delete  (  )  ; 
} 
} 
} 
} 



private   void   sendBinaryFile  (  File   file  )  throws   IOException  ,  CVSException  { 
BufferedInputStream   in  =  null  ; 
try  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
if  (  m_bCompressFiles  )  { 
GZIPOutputStream   gzipOut  =  null  ; 
InputStream   gzipIn  =  null  ; 
File   gzipFile  =  null  ; 
try  { 
gzipFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
gzipOut  =  new   GZIPOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  gzipFile  )  )  )  ; 
int   b  ; 
while  (  (  b  =  in  .  read  (  )  )  !=  -  1  )  gzipOut  .  write  (  (  byte  )  b  )  ; 
gzipOut  .  close  (  )  ; 
long   gzipLength  =  gzipFile  .  length  (  )  ; 
sendLine  (  "z"  +  Long  .  toString  (  gzipLength  )  )  ; 
gzipIn  =  new   BufferedInputStream  (  new   FileInputStream  (  gzipFile  )  )  ; 
for  (  long   i  =  0  ;  i  <  gzipLength  ;  i  ++  )  { 
b  =  gzipIn  .  read  (  )  ; 
if  (  b  ==  -  1  )  throw   new   EOFException  (  )  ; 
m_Out  .  write  (  (  byte  )  b  )  ; 
} 
}  finally  { 
if  (  gzipOut  !=  null  )  gzipOut  .  close  (  )  ; 
if  (  gzipIn  !=  null  )  gzipIn  .  close  (  )  ; 
if  (  gzipFile  !=  null  )  gzipFile  .  delete  (  )  ; 
} 
}  else  { 
long   nLength  =  file  .  length  (  )  ; 
sendLine  (  Long  .  toString  (  nLength  )  )  ; 
for  (  long   i  =  0  ;  i  <  nLength  ;  i  ++  )  { 
int   b  =  in  .  read  (  )  ; 
if  (  b  ==  -  1  )  throw   new   EOFException  (  )  ; 
m_Out  .  write  (  (  byte  )  b  )  ; 
} 
} 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 




private   void   sendTextFile  (  File   file  )  throws   IOException  ,  CVSException  { 
BufferedReader   in  =  null  ; 
boolean   warnedAboutBinary  =  false  ; 
RepositoryDetails  .  EndOfLineType   eolType  =  null  ; 
RepositoryDetails   repositoryDetails  =  RepositoryDetails  .  get  (  m_RepositoryLocation  )  ; 
if  (  repositoryDetails  !=  null  &&  repositoryDetails  .  shouldConvertEndOfLinesForFile  (  file  )  )  eolType  =  repositoryDetails  .  getRepositoryEndOfLineType  (  )  ; 
byte  [  ]  endOfLine  =  null  ; 
if  (  eolType  !=  null  )  endOfLine  =  eolType  .  getEndOfLine  (  )  .  getBytes  (  "ASCII"  )  ; 
boolean   sentConversionUpdate  =  false  ; 
try  { 
in  =  new   BufferedReader  (  new   FileReader  (  file  )  )  ; 
OutputStream   tempOut  =  null  ; 
InputStream   tempIn  =  null  ; 
File   tempFile  =  null  ; 
try  { 
tempFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
tempOut  =  new   BufferedOutputStream  (  new   FileOutputStream  (  tempFile  )  )  ; 
if  (  m_bCompressFiles  )  tempOut  =  new   GZIPOutputStream  (  tempOut  )  ; 
int   n  ; 
while  (  (  n  =  in  .  read  (  )  )  !=  -  1  )  { 
if  (  eolType  !=  null  )  { 
if  (  n  ==  '\r'  )  { 
tempOut  .  write  (  endOfLine  )  ; 
if  (  !  sentConversionUpdate  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingRepositoryEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentConversionUpdate  =  true  ; 
} 
n  =  in  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  n  ==  '\n'  )  continue  ; 
}  else   if  (  n  ==  '\n'  )  { 
tempOut  .  write  (  endOfLine  )  ; 
if  (  !  sentConversionUpdate  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingRepositoryEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentConversionUpdate  =  true  ; 
} 
continue  ; 
} 
} 
if  (  !  warnedAboutBinary  &&  CVSUtilities  .  isProbablyBinary  (  n  )  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "textFileContainsBinary"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  }  )  )  ; 
warnedAboutBinary  =  true  ; 
} 
tempOut  .  write  (  n  )  ; 
} 
tempOut  .  close  (  )  ; 
long   tempLength  =  tempFile  .  length  (  )  ; 
tempIn  =  new   BufferedInputStream  (  new   FileInputStream  (  tempFile  )  )  ; 
sendLine  (  (  m_bCompressFiles  ?  "z"  :  ""  )  +  Long  .  toString  (  tempLength  )  )  ; 
for  (  long   i  =  0  ;  i  <  tempLength  ;  i  ++  )  { 
n  =  tempIn  .  read  (  )  ; 
if  (  n  ==  -  1  )  throw   new   EOFException  (  )  ; 
m_Out  .  write  (  (  byte  )  n  )  ; 
} 
}  finally  { 
if  (  tempOut  !=  null  )  tempOut  .  close  (  )  ; 
if  (  tempIn  !=  null  )  tempIn  .  close  (  )  ; 
if  (  tempFile  !=  null  )  tempFile  .  delete  (  )  ; 
} 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 

private   boolean   isBinaryCharacter  (  int   n  )  { 
return   n  ==  0  ; 
} 


void   sendIsModified  (  String   sName  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Is-modified "  +  sName  )  ; 
} 

void   sendArgument  (  String   sArgument  )  throws   IOException  ,  CVSException  { 
int   nIndex  =  0  ; 
boolean   bFinished  =  false  ; 
while  (  !  bFinished  )  { 
int   nEndOfLine  =  sArgument  .  indexOf  (  '\n'  ,  nIndex  )  ; 
String   sLine  =  null  ; 
if  (  nEndOfLine  ==  -  1  )  { 
sLine  =  sArgument  .  substring  (  nIndex  )  ; 
bFinished  =  true  ; 
}  else   sLine  =  sArgument  .  substring  (  nIndex  ,  nEndOfLine  )  ; 
sendLine  (  (  nIndex  ==  0  ?  "Argument "  :  "Argumentx "  )  +  sLine  )  ; 
if  (  !  bFinished  )  nIndex  =  nEndOfLine  +  1  ; 
} 
} 

void   sendUnchanged  (  String   sName  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Unchanged "  +  sName  )  ; 
} 

void   sendQuestionable  (  String   sName  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Questionable "  +  sName  )  ; 
} 

void   sendArgument  (  File   file  )  throws   IOException  ,  CVSException  { 
sendArgument  (  getRelativePath  (  file  )  )  ; 
} 

void   sendArgument  (  FileEntry   file  )  throws   IOException  ,  CVSException  { 
sendArgument  (  getRelativePath  (  file  )  )  ; 
} 

void   sendArgument  (  WorkingDirectory   workingDirectory  )  throws   IOException  ,  CVSException  { 
sendArgument  (  getRelativePath  (  workingDirectory  )  )  ; 
} 

void   sendKopt  (  KeywordSubstitutionMode   mode  )  throws   IOException  ,  CVSException  { 
if  (  mode  !=  KeywordSubstitutionMode  .  NOT_DEFINED  )  sendLine  (  "Kopt "  +  mode  .  toString  (  )  )  ; 
} 

void   sendWatchActions  (  WatchActions   actions  )  throws   IOException  ,  CVSException  { 
if  (  actions  .  watchEdit  (  )  )  { 
sendArgument  (  "-a"  )  ; 
sendArgument  (  "edit"  )  ; 
} 
if  (  actions  .  watchUnedit  (  )  )  { 
sendArgument  (  "-a"  )  ; 
sendArgument  (  "unedit"  )  ; 
} 
if  (  actions  .  watchCommit  (  )  )  { 
sendArgument  (  "-a"  )  ; 
sendArgument  (  "commit"  )  ; 
} 
} 

void   sendDateOption  (  Date   date  )  throws   IOException  ,  CVSException  { 
sendArgument  (  "-D "  +  g_DateFormatter  .  format  (  date  )  )  ; 
} 

void   sendGlobalOption  (  String   sOption  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Global_option "  +  sOption  )  ; 
} 

private   void   sendNotify  (  File   file  ,  boolean   edit  )  throws   IOException  ,  CVSException  { 
sendLine  (  "Notify "  +  file  .  getName  (  )  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  50  )  ; 
if  (  edit  )  buffer  .  append  (  'E'  )  ;  else   buffer  .  append  (  'U'  )  ; 
buffer  .  append  (  '\t'  )  ; 
buffer  .  append  (  DateFormat  .  getDateTimeInstance  (  )  .  format  (  new   Date  (  )  )  )  ; 
buffer  .  append  (  '\t'  )  ; 
buffer  .  append  (  java  .  net  .  InetAddress  .  getLocalHost  (  )  .  getHostName  (  )  )  ; 
buffer  .  append  (  '\t'  )  ; 
buffer  .  append  (  file  .  getParentFile  (  )  .  getAbsolutePath  (  )  )  ; 
buffer  .  append  (  "\tEUC"  )  ; 
sendLine  (  buffer  .  toString  (  )  )  ; 
} 

void   sendEditNotify  (  File   file  )  throws   IOException  ,  CVSException  { 
sendNotify  (  file  ,  true  )  ; 
} 

void   sendUneditNotify  (  File   file  )  throws   IOException  ,  CVSException  { 
sendNotify  (  file  ,  false  )  ; 
} 



void   sendFileDetails  (  FileEntry   entry  )  throws   IOException  ,  CVSException  { 
if  (  !  m_FileDetailsSent  .  contains  (  entry  .  getFile  (  )  )  )  { 
sendDirectory  (  entry  .  getWorkingDirectory  (  )  )  ; 
sendEntry  (  entry  )  ; 
File   file  =  entry  .  getFile  (  )  ; 
if  (  file  .  exists  (  )  )  { 
if  (  !  m_bUseUnchanged  ||  entry  .  isModified  (  )  ||  entry  .  isLocallyAdded  (  )  )  { 
if  (  m_CurrentRequest  .  canSendIsModified  (  )  )  sendIsModified  (  file  .  getName  (  )  )  ;  else   sendModified  (  file  ,  entry  .  isBinary  (  )  )  ; 
}  else   sendUnchanged  (  entry  .  getName  (  )  )  ; 
} 
m_FileDetailsSent  .  add  (  file  )  ; 
} 
} 

void   sendFileDetails  (  File   file  ,  KeywordSubstitutionMode   mode  )  throws   IOException  ,  CVSException  { 
if  (  file  .  isDirectory  (  )  )  throw   new   IllegalArgumentException  (  "Use sendDirectoryDetails to send directories"  )  ; 
if  (  !  m_FileDetailsSent  .  contains  (  file  )  )  { 
FileEntry   entry  =  null  ; 
try  { 
entry  =  FileEntry  .  getFileEntry  (  file  )  ; 
}  catch  (  IOException   e  )  { 
}  catch  (  CVSException   e  )  { 
} 
if  (  entry  !=  null  )  sendFileDetails  (  entry  )  ;  else  { 
sendDirectory  (  WorkingDirectory  .  getWorkingDirectory  (  file  .  getParentFile  (  )  )  )  ; 
sendKopt  (  mode  )  ; 
sendModified  (  file  ,  mode  ==  KeywordSubstitutionMode  .  KB  )  ; 
m_FileDetailsSent  .  add  (  file  )  ; 
} 
} 
} 

void   sendFileDetails  (  File   file  )  throws   IOException  ,  CVSException  { 
boolean   isBinary  =  false  ; 
if  (  !  CVSUtilities  .  isUnderCVSControl  (  file  )  )  isBinary  =  CVSUtilities  .  isProbablyBinary  (  file  )  ; 
sendFileDetails  (  file  ,  isBinary  ?  KeywordSubstitutionMode  .  KB  :  KeywordSubstitutionMode  .  NOT_DEFINED  )  ; 
} 


void   sendDirectoryDetails  (  WorkingDirectory   workingDir  ,  boolean   bRecursive  ,  FileEntryFilter   fileEntryFilter  )  throws   IOException  ,  CVSException  { 
sendDirectory  (  workingDir  )  ; 
Entry  [  ]  entries  =  workingDir  .  getEntries  (  )  ; 
for  (  int   i  =  0  ;  i  <  entries  .  length  ;  i  ++  )  { 
Entry   entry  =  entries  [  i  ]  ; 
if  (  entry   instanceof   FileEntry  )  { 
FileEntry   fileEntry  =  (  FileEntry  )  entry  ; 
if  (  fileEntryFilter  .  accept  (  fileEntry  )  )  sendFileDetails  (  fileEntry  )  ; 
}  else   if  (  bRecursive  )  sendDirectoryDetails  (  WorkingDirectory  .  getWorkingDirectory  (  entry  .  getFile  (  )  )  ,  true  ,  fileEntryFilter  )  ; 
} 
} 

void   sendDirectoryDetails  (  WorkingDirectory   workingDir  ,  boolean   bRecursive  )  throws   IOException  ,  CVSException  { 
sendDirectoryDetails  (  workingDir  ,  bRecursive  ,  FileEntryFilter  .  ALL_FILES  )  ; 
} 

void   sendDirectoryDetails  (  WorkingDirectory   workingDir  )  throws   IOException  ,  CVSException  { 
sendDirectoryDetails  (  workingDir  ,  true  ,  FileEntryFilter  .  ALL_FILES  )  ; 
} 

void   sendDirectoryDetails  (  File   directory  ,  boolean   bRecursive  ,  FileEntryFilter   fileEntryFilter  )  throws   IOException  ,  CVSException  { 
WorkingDirectory   w  =  null  ; 
try  { 
w  =  WorkingDirectory  .  getWorkingDirectory  (  directory  )  ; 
}  catch  (  IOException   e  )  { 
return  ; 
}  catch  (  CVSException   e  )  { 
return  ; 
} 
sendDirectoryDetails  (  w  ,  bRecursive  ,  fileEntryFilter  )  ; 
} 

void   sendDirectoryDetails  (  File   directory  ,  boolean   bRecursive  )  throws   IOException  ,  CVSException  { 
sendDirectoryDetails  (  directory  ,  bRecursive  ,  FileEntryFilter  .  ALL_FILES  )  ; 
} 

void   sendDirectoryDetails  (  File   directory  )  throws   IOException  ,  CVSException  { 
sendDirectoryDetails  (  directory  ,  true  ,  FileEntryFilter  .  ALL_FILES  )  ; 
} 


void   sendOptions  (  Options   options  )  throws   IOException  ,  CVSException  { 
if  (  options  ==  null  )  return  ; 
options  .  m_Client  =  this  ; 
options  .  sendOptions  (  )  ; 
} 



CVSResponse   performSubRequest  (  CVSRequest   request  )  throws   IOException  ,  CVSException  { 
CVSRequest   oldRequest  =  m_CurrentRequest  ; 
m_CurrentRequest  =  request  ; 
try  { 
request  .  m_Client  =  this  ; 
if  (  !  request  .  getRepositoryLocation  (  )  .  equals  (  m_RepositoryLocation  )  )  throw   new   CVSException  (  "Cannot "  +  request  .  getDescription  (  )  +  " because the request is for a different repository location"  )  ; 
if  (  !  request  .  validateRequest  (  )  )  return   null  ; 
request  .  clientPerformRequest  (  )  ; 
return   request  .  getResponse  (  )  ; 
}  finally  { 
request  .  m_Client  =  null  ; 
m_CurrentRequest  =  oldRequest  ; 
} 
} 

ExpandModulesResponse   expandModules  (  String  [  ]  modules  )  throws   IOException  ,  CVSException  { 
return  (  ExpandModulesResponse  )  performSubRequest  (  new   ExpandModulesRequest  (  getRepositoryLocation  (  )  ,  getCurrentDirectory  (  )  ,  modules  )  )  ; 
} 

ValidRequestsResponse   validRequests  (  )  throws   IOException  ,  CVSException  { 
return  (  ValidRequestsResponse  )  performSubRequest  (  new   ValidRequestsRequest  (  getRepositoryLocation  (  )  )  )  ; 
} 

GetWrapperRCSOptionsResponse   getWrapperRCSOptions  (  )  throws   IOException  ,  CVSException  { 
return  (  GetWrapperRCSOptionsResponse  )  performSubRequest  (  new   GetWrapperRCSOptionsRequest  (  getRepositoryLocation  (  )  )  )  ; 
} 


void   flush  (  )  throws   IOException  { 
m_Out  .  flush  (  )  ; 
} 


String   receiveLine  (  )  throws   IOException  { 
flush  (  )  ; 
m_ReceiveBuffer  .  setLength  (  0  )  ; 
int   n  ; 
while  (  true  )  { 
n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  throw   new   EOFException  (  "Connection closed"  )  ; 
if  (  n  ==  '\n'  )  break  ; 
m_ReceiveBuffer  .  append  (  (  char  )  n  )  ; 
} 
String   s  =  m_ReceiveBuffer  .  toString  (  )  ; 
fireReceivedText  (  s  )  ; 
Thread  .  yield  (  )  ; 
return   s  ; 
} 



private   void   receiveBinaryFile  (  OutputStream   out  )  throws   IOException  ,  CVSException  { 
flush  (  )  ; 
long   nLength  =  0  ; 
String   sLength  =  receiveLine  (  )  ; 
if  (  sLength  .  charAt  (  0  )  ==  'z'  )  { 
try  { 
nLength  =  Long  .  parseLong  (  sLength  .  substring  (  1  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   CVSProtocolException  (  "Invalid response from server: \""  +  sLength  +  "\"\n"  +  "Could not parse the length of the file to be received."  ,  e  )  ; 
} 
File   gzipFile  =  null  ; 
OutputStream   gzipOut  =  null  ; 
InputStream   gzipIn  =  null  ; 
try  { 
gzipFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
gzipOut  =  new   BufferedOutputStream  (  new   FileOutputStream  (  gzipFile  )  )  ; 
for  (  int   i  =  0  ;  i  <  nLength  ;  i  ++  )  { 
int   n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  throw   new   EOFException  (  )  ; 
gzipOut  .  write  (  (  byte  )  n  )  ; 
Thread  .  yield  (  )  ; 
} 
gzipOut  .  close  (  )  ; 
gzipIn  =  new   GZIPInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  gzipFile  )  )  )  ; 
while  (  true  )  { 
int   n  =  gzipIn  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  out  !=  null  )  out  .  write  (  (  byte  )  n  )  ; 
} 
}  finally  { 
if  (  gzipIn  !=  null  )  gzipIn  .  close  (  )  ; 
if  (  gzipOut  !=  null  )  gzipOut  .  close  (  )  ; 
if  (  gzipFile  !=  null  )  gzipFile  .  delete  (  )  ; 
} 
}  else  { 
try  { 
nLength  =  Long  .  parseLong  (  sLength  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   CVSProtocolException  (  "Invalid response from server: \""  +  sLength  +  "\"\n"  +  "Could not parse the length of the file to be received."  ,  e  )  ; 
} 
for  (  int   i  =  0  ;  i  <  nLength  ;  i  ++  )  { 
int   n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  throw   new   EOFException  (  )  ; 
if  (  out  !=  null  )  out  .  write  (  (  byte  )  n  )  ; 
} 
} 
} 

void   receiveBinaryFile  (  File   fileToSaveAs  )  throws   IOException  ,  CVSException  { 
File   backup  =  null  ; 
if  (  fileToSaveAs  .  exists  (  )  )  { 
backup  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
if  (  !  fileToSaveAs  .  renameTo  (  backup  )  )  throw   new   IOException  (  "Could not backup existing file "  +  fileToSaveAs  .  getAbsolutePath  (  )  )  ; 
} 
OutputStream   out  =  null  ; 
try  { 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  fileToSaveAs  )  )  ; 
receiveBinaryFile  (  out  )  ; 
}  catch  (  Exception   e  )  { 
if  (  fileToSaveAs  .  exists  (  )  )  fileToSaveAs  .  delete  (  )  ; 
if  (  backup  !=  null  &&  !  backup  .  renameTo  (  fileToSaveAs  )  )  throw   new   IOException  (  "Could not restore file "  +  fileToSaveAs  .  getAbsolutePath  (  )  +  " from backup file "  +  backup  .  getAbsolutePath  (  )  )  ; 
if  (  e   instanceof   CVSException  )  throw  (  CVSException  )  e  ;  else   if  (  e   instanceof   IOException  )  throw  (  IOException  )  e  ;  else   throw  (  RuntimeException  )  e  ; 
}  finally  { 
if  (  out  !=  null  )  out  .  close  (  )  ; 
} 
} 





private   void   receiveTextFile  (  File   file  ,  Writer   out  )  throws   IOException  ,  CVSException  { 
flush  (  )  ; 
RepositoryDetails  .  EndOfLineType   eolType  =  null  ; 
RepositoryDetails   repositoryDetails  =  RepositoryDetails  .  get  (  m_RepositoryLocation  )  ; 
if  (  repositoryDetails  !=  null  &&  repositoryDetails  .  shouldConvertEndOfLinesForFile  (  file  )  )  eolType  =  repositoryDetails  .  getLocalEndOfLineType  (  )  ; 
String   sEndOfLine  =  null  ; 
if  (  eolType  !=  null  )  sEndOfLine  =  eolType  .  getEndOfLine  (  )  ; 
long   nLength  =  0  ; 
String   sLength  =  receiveLine  (  )  ; 
boolean   sentStatusUpdateAboutConvert  =  false  ; 
boolean   sentBinaryWarning  =  false  ; 
if  (  sLength  .  charAt  (  0  )  ==  'z'  )  { 
try  { 
nLength  =  Long  .  parseLong  (  sLength  .  substring  (  1  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   CVSProtocolException  (  "Invalid response from server: \""  +  sLength  +  "\"\n"  +  "Could not parse the length of the file to be received."  ,  e  )  ; 
} 
File   gzipFile  =  null  ; 
OutputStream   gzipOut  =  null  ; 
InputStream   gzipIn  =  null  ; 
try  { 
gzipFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
gzipOut  =  new   BufferedOutputStream  (  new   FileOutputStream  (  gzipFile  )  )  ; 
for  (  int   i  =  0  ;  i  <  nLength  ;  i  ++  )  { 
int   n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  throw   new   EOFException  (  )  ; 
gzipOut  .  write  (  (  byte  )  n  )  ; 
Thread  .  yield  (  )  ; 
} 
gzipOut  .  close  (  )  ; 
gzipIn  =  new   GZIPInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  gzipFile  )  )  )  ; 
while  (  true  )  { 
int   n  =  gzipIn  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  eolType  !=  null  )  { 
if  (  n  ==  '\r'  )  { 
if  (  out  !=  null  )  { 
out  .  write  (  sEndOfLine  )  ; 
if  (  !  sentStatusUpdateAboutConvert  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingLocalEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentStatusUpdateAboutConvert  =  true  ; 
} 
} 
n  =  gzipIn  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  n  ==  '\n'  )  continue  ; 
}  else   if  (  n  ==  '\n'  )  { 
if  (  out  !=  null  )  { 
out  .  write  (  sEndOfLine  )  ; 
if  (  !  sentStatusUpdateAboutConvert  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingLocalEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentStatusUpdateAboutConvert  =  true  ; 
} 
} 
continue  ; 
} 
} 
if  (  !  sentBinaryWarning  &&  CVSUtilities  .  isProbablyBinary  (  n  )  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "receivedTextFileIsProbablyBinary"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  }  )  )  ; 
sentBinaryWarning  =  true  ; 
} 
if  (  out  !=  null  )  out  .  write  (  n  )  ; 
} 
}  finally  { 
if  (  gzipIn  !=  null  )  gzipIn  .  close  (  )  ; 
if  (  gzipOut  !=  null  )  gzipOut  .  close  (  )  ; 
if  (  gzipFile  !=  null  )  gzipFile  .  delete  (  )  ; 
} 
}  else  { 
try  { 
nLength  =  Long  .  parseLong  (  sLength  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   CVSProtocolException  (  "Invalid response from server: \""  +  sLength  +  "\"\n"  +  "Could not parse the length of the file to be received."  ,  e  )  ; 
} 
for  (  int   i  =  0  ;  i  <  nLength  ;  i  ++  )  { 
int   n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  eolType  !=  null  )  { 
if  (  n  ==  '\r'  )  { 
if  (  out  !=  null  )  { 
out  .  write  (  sEndOfLine  )  ; 
if  (  !  sentStatusUpdateAboutConvert  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingLocalEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentStatusUpdateAboutConvert  =  true  ; 
} 
} 
n  =  m_In  .  read  (  )  ; 
if  (  n  ==  -  1  )  break  ; 
if  (  n  ==  '\n'  )  continue  ; 
}  else   if  (  n  ==  '\n'  )  { 
if  (  out  !=  null  )  { 
out  .  write  (  sEndOfLine  )  ; 
if  (  !  sentStatusUpdateAboutConvert  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "convertingLocalEndOfLine"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  ,  eolType  .  toString  (  )  }  )  )  ; 
sentStatusUpdateAboutConvert  =  true  ; 
} 
} 
continue  ; 
} 
} 
if  (  !  sentBinaryWarning  &&  CVSUtilities  .  isProbablyBinary  (  n  )  )  { 
fireStatusUpdate  (  MessageFormat  .  format  (  res  .  getString  (  "receivedTextFileIsProbablyBinary"  )  ,  new   Object  [  ]  {  file  .  getAbsolutePath  (  )  }  )  )  ; 
sentBinaryWarning  =  true  ; 
} 
if  (  out  !=  null  )  out  .  write  (  n  )  ; 
} 
} 
} 








void   receiveTextFile  (  File   fileToSaveAs  )  throws   IOException  ,  CVSException  { 
File   backup  =  null  ; 
if  (  fileToSaveAs  .  exists  (  )  )  { 
backup  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
if  (  !  fileToSaveAs  .  renameTo  (  backup  )  )  throw   new   IOException  (  "Could not backup existing file "  +  fileToSaveAs  .  getAbsolutePath  (  )  )  ; 
} 
BufferedWriter   out  =  null  ; 
try  { 
TextFileFormatter   formatter  =  TextFileFormatter  .  getFormatterForReceivingText  (  fileToSaveAs  )  ; 
if  (  formatter  ==  null  )  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  fileToSaveAs  )  )  ; 
receiveTextFile  (  fileToSaveAs  ,  out  )  ; 
}  else  { 
File   tempFile  =  File  .  createTempFile  (  "javacvs"  ,  "tmp"  )  ; 
Writer   tempWriter  =  null  ; 
try  { 
tempWriter  =  new   BufferedWriter  (  new   FileWriter  (  tempFile  )  )  ; 
receiveTextFile  (  fileToSaveAs  ,  tempWriter  )  ; 
tempWriter  .  close  (  )  ; 
formatter  .  formatTextFile  (  tempFile  ,  fileToSaveAs  )  ; 
}  finally  { 
if  (  tempWriter  !=  null  )  tempWriter  .  close  (  )  ; 
tempFile  .  delete  (  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
if  (  fileToSaveAs  .  exists  (  )  )  fileToSaveAs  .  delete  (  )  ; 
if  (  backup  !=  null  &&  !  backup  .  renameTo  (  fileToSaveAs  )  )  throw   new   IOException  (  "Could not restore file "  +  fileToSaveAs  .  getAbsolutePath  (  )  +  " from backup file "  +  backup  .  getAbsolutePath  (  )  )  ; 
if  (  e   instanceof   CVSException  )  throw  (  CVSException  )  e  ;  else   if  (  e   instanceof   IOException  )  throw  (  IOException  )  e  ;  else   throw  (  RuntimeException  )  e  ; 
}  finally  { 
if  (  out  !=  null  )  out  .  close  (  )  ; 
} 
} 





private   void   openConnection  (  RepositoryLocation   location  )  throws   CVSException  ,  IOException  { 
if  (  location  .  getConnectionMethod  (  )  .  equals  (  CVSConnectionMethod  .  LOCAL  )  )  fireStatusUpdate  (  getResourceString  (  "connectingToLocalServer"  )  )  ;  else   fireStatusUpdate  (  MessageFormat  .  format  (  getResourceString  (  "connectingToServer"  )  ,  new   Object  [  ]  {  location  .  getHostName  (  )  }  )  )  ; 
closeConnection  (  )  ; 
m_Connection  =  m_ConnectionManager  .  createConnection  (  this  ,  location  ,  m_LoginManager  )  ; 
fireOpenedConnection  (  )  ; 
m_RepositoryLocation  =  location  ; 
m_In  =  new   BufferedInputStream  (  m_Connection  .  getInputStream  (  )  )  ; 
m_Out  =  new   BufferedOutputStream  (  m_Connection  .  getOutputStream  (  )  )  ; 
final   InputStream   errorStream  =  m_Connection  .  getErrorStream  (  )  ; 
if  (  errorStream  !=  null  )  { 
Thread   thread  =  new   Thread  (  )  { 

public   void   run  (  )  { 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  errorStream  )  )  ; 
String   sError  ; 
try  { 
while  (  (  sError  =  in  .  readLine  (  )  )  !=  null  )  { 
fireStatusUpdate  (  sError  )  ; 
} 
}  catch  (  IOException   e  )  { 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
}  ; 
thread  .  start  (  )  ; 
} 
clearOptimisationState  (  )  ; 
sendLine  (  "Root "  +  location  .  getRepositoryPath  (  )  )  ; 
ValidRequestsResponse   validRequestsResponse  =  validRequests  (  )  ; 
m_sValidRequests  =  null  ; 
if  (  validRequestsResponse  !=  null  &&  !  validRequestsResponse  .  hadError  (  )  )  m_sValidRequests  =  validRequestsResponse  .  getValidRequests  (  )  ; 
sendLine  (  "Valid-responses "  +  m_sValidResponses  )  ; 
if  (  m_bUseUnchanged  )  sendLine  (  "UseUnchanged"  )  ; 
sendOptions  (  m_GlobalOptions  )  ; 
int   nZipLevel  =  m_GlobalOptions  .  getZipCompressionLevel  (  )  ; 
if  (  nZipLevel  ==  -  1  )  { 
RepositoryDetails   details  =  RepositoryDetails  .  get  (  location  )  ; 
if  (  details  !=  null  )  nZipLevel  =  details  .  getZipCompressionLevel  (  )  ; 
if  (  nZipLevel  ==  -  1  )  { 
if  (  location  .  getConnectionMethod  (  )  ==  CVSConnectionMethod  .  LOCAL  )  nZipLevel  =  0  ;  else   nZipLevel  =  getDefaultRemoteZipCompressionLevel  (  )  ; 
} 
} 
m_bCompressFiles  =  false  ; 
if  (  nZipLevel  >  0  )  { 
if  (  isRequestValid  (  "gzip-file-contents"  )  )  { 
sendLine  (  "gzip-file-contents "  +  nZipLevel  )  ; 
m_bCompressFiles  =  true  ; 
}  else   nZipLevel  =  0  ; 
} 
m_nZipLevel  =  nZipLevel  ; 
} 

private   void   closeConnection  (  )  { 
if  (  m_Connection  !=  null  )  { 
m_Connection  .  close  (  )  ; 
m_Connection  =  null  ; 
} 
m_RepositoryLocation  =  null  ; 
} 

protected   void   fireSentText  (  String   sText  )  { 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientTextEvent   event  =  new   CVSClientTextEvent  (  this  ,  sText  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  sentText  (  event  )  ; 
} 
} 

protected   void   fireReceivedText  (  String   sText  )  { 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientTextEvent   event  =  new   CVSClientTextEvent  (  this  ,  sText  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  receivedText  (  event  )  ; 
} 
} 

protected   synchronized   void   fireStatusUpdate  (  String   sText  )  { 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientTextEvent   event  =  new   CVSClientTextEvent  (  this  ,  sText  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  statusUpdate  (  event  )  ; 
} 
} 

protected   void   fireReceivedResponse  (  CVSResponse   response  )  { 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientResponseEvent   event  =  new   CVSClientResponseEvent  (  this  ,  response  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  receivedResponse  (  event  )  ; 
} 
} 

protected   void   fireStartingRequests  (  )  { 
m_bPerformingRequests  =  true  ; 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientEvent   event  =  new   CVSClientEvent  (  this  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  startingRequests  (  event  )  ; 
} 
} 

protected   void   fireOpenedConnection  (  )  { 
m_bPerformingRequests  =  true  ; 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientEvent   event  =  new   CVSClientEvent  (  this  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  openedConnection  (  event  )  ; 
} 
} 

protected   void   fireFinishedRequests  (  )  { 
m_bPerformingRequests  =  false  ; 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientEvent   event  =  new   CVSClientEvent  (  this  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  finishedRequests  (  event  )  ; 
} 
} 

protected   void   fireEnteredBatchMode  (  )  { 
m_bPerformingRequests  =  false  ; 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientEvent   event  =  new   CVSClientEvent  (  this  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  enteredBatchMode  (  event  )  ; 
} 
} 

protected   void   fireExitedBatchMode  (  )  { 
m_bPerformingRequests  =  false  ; 
List   listeners  =  null  ; 
synchronized  (  m_Lock  )  { 
if  (  m_Listeners  ==  null  ||  m_Listeners  .  size  (  )  ==  0  )  return  ; 
listeners  =  (  List  )  m_Listeners  .  clone  (  )  ; 
} 
CVSClientEvent   event  =  new   CVSClientEvent  (  this  )  ; 
Iterator   i  =  listeners  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CVSClientListener   listener  =  (  CVSClientListener  )  i  .  next  (  )  ; 
listener  .  exitedBatchMode  (  event  )  ; 
} 
} 

private   void   clearOptimisationState  (  )  { 
m_LastDirectory  =  null  ; 
m_DirectoriesSent  .  clear  (  )  ; 
m_WorkingDirectoriesStateHasBeenSentFor  .  clear  (  )  ; 
m_FileDetailsSent  .  clear  (  )  ; 
} 

private   CVSConnectionManager   m_ConnectionManager  ; 

private   RepositoryLocation   m_RepositoryLocation  ; 

private   LoginManager   m_LoginManager  ; 

private   CVSConnection   m_Connection  ; 

private   InputStream   m_In  ; 

private   OutputStream   m_Out  ; 

private   int   m_nZipLevel  ; 

private   boolean   m_bCompressFiles  ; 

private   StringBuffer   m_ReceiveBuffer  =  new   StringBuffer  (  100  )  ; 

private   ArrayList   m_Listeners  ; 

private   String   m_sValidResponses  =  "E M ok error Valid-requests Created Merged Updated Update-existing Removed Remove-entry New-entry Checked-in Copy-file Notified Clear-sticky Set-sticky Clear-static-directory Set-static-directory Wrapper-rcsOption Mod-time"  ; 

private   boolean   m_bUseUnchanged  =  true  ; 

private   boolean   m_bPerformingRequests  =  false  ; 

private   CVSRequest   m_CurrentRequest  ; 

private   Object   m_Lock  =  new   Object  (  )  ; 

private   RequestAbortedException   m_AbortException  ; 

private   boolean   m_bAborted  ; 

private   String   m_sValidRequests  ; 

private   File   m_LastDirectory  ; 

private   static   final   DateFormat   g_DateFormatter  =  new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  ; 

private   Set   m_DirectoriesSent  =  new   HashSet  (  50  )  ; 

private   Set   m_WorkingDirectoriesStateHasBeenSentFor  =  new   HashSet  (  50  )  ; 

private   Set   m_FileDetailsSent  =  new   HashSet  (  200  )  ; 

private   FileFilter   m_IgnoreFileFilter  =  CVSIgnoreFileFilter  .  getInstance  (  )  ; 

private   GlobalOptions   m_GlobalOptions  =  new   GlobalOptions  (  )  ; 

private   CVSRequestBatch   m_RequestBatch  ; 

private   Object   m_BatchLock  =  new   Object  (  )  ; 
} 

