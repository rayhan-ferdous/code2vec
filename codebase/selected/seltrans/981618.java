package   com  .  xmultra  .  processor  .  zap  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   com  .  ice  .  tar  .  InvalidHeaderException  ; 
import   com  .  ice  .  tar  .  TarEntry  ; 
import   com  .  ice  .  tar  .  TarOutputStream  ; 
import   com  .  xmultra  .  XmultraConfig  ; 
import   com  .  xmultra  .  log  .  Console  ; 
import   com  .  xmultra  .  log  .  ErrorLogEntry  ; 
import   com  .  xmultra  .  log  .  MessageLogEntry  ; 
import   com  .  xmultra  .  processor  .  DestinationWriter  ; 
import   com  .  xmultra  .  processor  .  Processor  ; 
import   com  .  xmultra  .  util  .  CallbackRegistry  ; 
import   com  .  xmultra  .  util  .  DateUtils  ; 
import   com  .  xmultra  .  util  .  FileHolder  ; 
import   com  .  xmultra  .  util  .  FileUtils  ; 
import   com  .  xmultra  .  util  .  InitMapHolder  ; 
import   com  .  xmultra  .  util  .  ListHolder  ; 
import   com  .  xmultra  .  util  .  NameSelector  ; 
import   com  .  xmultra  .  util  .  SyncFlag  ; 
import   com  .  xmultra  .  watcher  .  WakeAble  ; 








public   class   ZAProcessor   extends   Processor  { 




public   static   final   String   VERSION  =  "@version $Revision: #2 $"  ; 

private   static   final   int   BUFFER_SIZE  =  8192  ; 

private   DestinationWriter   destWriter  =  null  ; 

private   File   destLocationFile  =  null  ; 

private   File   srcDoneLocFile  =  null  ; 

private   FileHolder   fileHolder  =  new   FileHolder  (  )  ; 

private   FileInputStream   inStream  =  null  ; 

private   int   zipCompressionLevel  =  0  ; 

private   NameSelector   nameSelector  =  null  ; 

private   String   destinationFileNameType  =  null  ; 

private   String   destinationFixedFileName  =  null  ; 

private   String   fileNameExtension  =  null  ; 

private   String   zapMode  =  null  ; 

private   String   zapType  =  null  ; 

private   static   String   GZIP_EXTENSION  =  ".gz"  ; 

private   static   String   TAR_EXTENSION  =  ".tar"  ; 

private   static   String   TAR_GZIP_EXTENSION_1  =  ".tar.gz"  ; 

private   static   String   TAR_GZIP_EXTENSION_2  =  ".taz"  ; 

private   static   String   TAR_GZIP_EXTENSION_3  =  ".tgz"  ; 

private   static   String   ZIP_EXTENSION  =  ".zip"  ; 

private   boolean   deleteProcessedFiles  =  false  ; 




public   ZAProcessor  (  )  { 
} 

















public   boolean   init  (  Node   n  ,  InitMapHolder   imh  ,  SyncFlag   sf  ,  SyncFlag   stopFlag  )  { 
if  (  !  super  .  init  (  n  ,  imh  ,  sf  ,  stopFlag  )  )  return   false  ; 
Element   zapEl  =  (  Element  )  n  ; 
this  .  destWriter  =  new   DestinationWriter  (  )  ; 
if  (  !  this  .  destWriter  .  init  (  imh  ,  zapEl  ,  new   DateUtils  (  )  )  )  return   false  ; 
msgEntry  =  new   MessageLogEntry  (  this  ,  VERSION  )  ; 
errEntry  =  new   ErrorLogEntry  (  this  ,  VERSION  )  ; 
this  .  srcDoneLocFile  =  getLocationDirectory  (  XmultraConfig  .  SRC_DONE_LOCATION_ELEMENT  ,  false  )  ; 
if  (  this  .  srcDoneLocFile  ==  null  )  { 
deleteProcessedFiles  =  true  ; 
} 
this  .  destLocationFile  =  getLocationDirectory  (  XmultraConfig  .  DEST_LOCATION_ELEMENT  )  ; 
if  (  this  .  destLocationFile  ==  null  )  return   false  ; 
this  .  zapMode  =  zapEl  .  getAttribute  (  XmultraConfig  .  ZAP_MODE_ATTR  )  ; 
this  .  zapType  =  zapEl  .  getAttribute  (  XmultraConfig  .  ZAP_TYPE_ATTR  )  ; 
destinationFileNameType  =  zapEl  .  getAttribute  (  XmultraConfig  .  DEST_FILE_NAME_TYPE_ATTR  )  ; 
this  .  destinationFixedFileName  =  zapEl  .  getAttribute  (  XmultraConfig  .  DEST_FIXED_FILE_NAME_ATTR  )  ; 
if  (  this  .  destinationFileNameType  .  equals  (  XmultraConfig  .  FIXED_VALUE  )  &&  this  .  destinationFixedFileName  .  equals  (  ""  )  )  { 
msgEntry  .  setAppContext  (  "init()"  )  ; 
msgEntry  .  setMessageText  (  "Required attribute missing."  )  ; 
msgEntry  .  setError  (  "When the '"  +  XmultraConfig  .  DEST_FILE_NAME_TYPE_ATTR  +  "' attribute is set to '"  +  XmultraConfig  .  FIXED_VALUE  +  "' the '"  +  XmultraConfig  .  DEST_FIXED_FILE_NAME_ATTR  +  "' attribute needs a value."  )  ; 
logger  .  logWarning  (  msgEntry  )  ; 
return   false  ; 
} 
if  (  destinationFileNameType  .  equals  (  XmultraConfig  .  SOURCE_NAME_VALUE  )  &&  !  (  zapType  .  equals  (  XmultraConfig  .  GZIP_VALUE  )  ||  zapMode  .  equals  (  XmultraConfig  .  INDIVIDUAL_FILES_OVER_UNMOD_VALUE  )  )  )  { 
msgEntry  .  setAppContext  (  "init()"  )  ; 
msgEntry  .  setMessageText  (  "If the '"  +  XmultraConfig  .  ZAP_TYPE_ATTR  +  "' attribute is '"  +  zapType  +  "' and the '"  +  XmultraConfig  .  DEST_FILE_NAME_TYPE_ATTR  +  "' attribute is '"  +  XmultraConfig  .  SOURCE_NAME_VALUE  +  "' then the '"  +  XmultraConfig  .  ZAP_MODE_ATTR  +  "' must be '"  +  XmultraConfig  .  INDIVIDUAL_FILES_OVER_UNMOD_VALUE  +  "'."  )  ; 
logger  .  logWarning  (  msgEntry  )  ; 
return   false  ; 
} 
if  (  this  .  zapType  .  equals  (  XmultraConfig  .  GZIP_VALUE  )  )  { 
this  .  fileNameExtension  =  GZIP_EXTENSION  ; 
}  else   if  (  this  .  zapType  .  equals  (  XmultraConfig  .  ZIP_VALUE  )  )  { 
this  .  fileNameExtension  =  ZIP_EXTENSION  ; 
}  else   if  (  this  .  zapType  .  equals  (  XmultraConfig  .  TAR_VALUE  )  )  { 
this  .  fileNameExtension  =  TAR_EXTENSION  ; 
}  else  { 
this  .  fileNameExtension  =  zapEl  .  getAttribute  (  XmultraConfig  .  TAR_GZIP_EXTENSION_ATTR  )  ; 
} 
String   zipCompressionLevelStr  =  zapEl  .  getAttribute  (  XmultraConfig  .  ZIP_COMPRESSION_LEVEL  )  ; 
zipCompressionLevelStr  =  zipCompressionLevelStr  .  substring  (  0  ,  1  )  ; 
this  .  zipCompressionLevel  =  Integer  .  parseInt  (  zipCompressionLevelStr  )  ; 
Node   locationNode  =  xmlParseUtils  .  getChildNode  (  zapEl  ,  XmultraConfig  .  LOCATIONS_ELEMENT  )  ; 
Node   destinationLocationNode  =  xmlParseUtils  .  getChildNode  (  locationNode  ,  XmultraConfig  .  DEST_LOCATION_ELEMENT  )  ; 
this  .  nameSelector  =  new   NameSelector  (  destinationLocationNode  ,  initMapHolder  )  ; 
return   true  ; 
} 




public   void   cleanUp  (  )  { 
if  (  this  .  inStream  !=  null  )  { 
try  { 
this  .  inStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
listHolder  .  setIndex  (  listHolder  .  getList  (  )  .  size  (  )  )  ; 
File   srcFile  =  (  File  )  super  .  currentObjBeingProcessed  ; 
if  (  deleteProcessedFiles  )  { 
srcFile  .  delete  (  )  ; 
}  else  { 
fileUtils  .  moveFileToDoneLocation  (  srcFile  ,  srcDoneLocFile  .  toString  (  )  )  ; 
} 
} 






public   void   run  (  )  { 
if  (  !  processorStopSyncFlag  .  getFlag  (  )  )  super  .  notifyAndStartWaiting  (  )  ; 
while  (  !  processorStopSyncFlag  .  getFlag  (  )  )  { 
zap  (  )  ; 
if  (  processorStopSyncFlag  .  getFlag  (  )  )  break  ; 
super  .  notifyAndStartWaiting  (  )  ; 
} 
msgEntry  .  setAppContext  (  "run()"  )  ; 
msgEntry  .  setMessageText  (  "Exiting ZAProcessor"  )  ; 
logger  .  logProcess  (  msgEntry  )  ; 
processorSyncFlag  .  setFlag  (  false  )  ; 
processorStopSyncFlag  .  setFlag  (  false  )  ; 
} 




private   void   zap  (  )  { 
fileHolder  .  srcFileList  =  listHolder  .  getList  (  )  ; 
fileHolder  .  srcFirstFileIndex  =  listHolder  .  getIndex  (  )  ; 
if  (  !  setFileHolderIndex  (  fileHolder  )  )  return  ; 
selectInputFiles  (  fileHolder  )  ; 
setDestinationFileName  (  fileHolder  )  ; 
if  (  fileHolder  .  destFileName  !=  null  )  { 
fileHolder  .  destFiles  =  this  .  destWriter  .  generateDestFiles  (  new   File  (  fileHolder  .  destFileName  )  ,  null  ,  null  ,  false  )  ; 
} 
this  .  addFileNameExtension  (  fileHolder  )  ; 
if  (  this  .  zapType  .  equals  (  XmultraConfig  .  TAR_VALUE  )  )  { 
this  .  tar  (  fileHolder  ,  false  )  ; 
}  else   if  (  this  .  zapType  .  equals  (  XmultraConfig  .  TAR_GZIP_VALUE  )  )  { 
this  .  tar  (  fileHolder  ,  true  )  ; 
}  else   if  (  this  .  zapType  .  equals  (  XmultraConfig  .  GZIP_VALUE  )  )  { 
this  .  gzip  (  fileHolder  )  ; 
}  else   if  (  this  .  zapType  .  equals  (  XmultraConfig  .  ZIP_VALUE  )  )  { 
this  .  zip  (  fileHolder  ,  this  .  zipCompressionLevel  )  ; 
}  else   return  ; 
copyAdditionalDestLocations  (  fileHolder  )  ; 
if  (  !  Console  .  getConsoleMode  (  "9"  )  )  { 
for  (  int   i  =  fileHolder  .  srcFirstFileIndex  ;  i  <=  fileHolder  .  srcLastFileIndex  ;  i  ++  )  { 
File   srcFile  =  (  File  )  fileHolder  .  srcFileList  .  get  (  i  )  ; 
if  (  deleteProcessedFiles  )  { 
srcFile  .  delete  (  )  ; 
}  else  { 
fileUtils  .  moveFileToDoneLocation  (  srcFile  ,  srcDoneLocFile  .  toString  (  )  )  ; 
} 
} 
} 
listHolder  .  setIndex  (  fileHolder  .  srcLastFileIndex  )  ; 
if  (  fileHolder  .  selectedFileList  .  size  (  )  >  0  )  { 
logEntries  (  fileHolder  )  ; 
} 
for  (  int   j  =  0  ;  j  <  fileHolder  .  destFiles  .  length  ;  j  ++  )  { 
File   destFile  =  new   File  (  fileHolder  .  destFiles  [  j  ]  )  ; 
WakeAble   wakeAble  =  CallbackRegistry  .  getFromWakeAbleRegistry  (  destFile  .  getParent  (  )  .  toString  (  )  )  ; 
if  (  wakeAble  !=  null  )  { 
wakeAble  .  wakeUp  (  )  ; 
} 
} 
if  (  Console  .  getConsoleMode  (  "9"  )  &&  listHolder  .  getIndex  (  )  >=  listHolder  .  getList  (  )  .  size  (  )  -  1  )  { 
System  .  exit  (  0  )  ; 
} 
listHolder  .  setIndex  (  fileHolder  .  srcLastFileIndex  +  1  )  ; 
} 






private   void   logEntries  (  FileHolder   fileHolder  )  { 
StringBuffer   msg  =  new   StringBuffer  (  )  ; 
msg  .  append  (  "Successfully "  +  this  .  zapType  +  "'d the following file(s):\n"  )  ; 
for  (  int   i  =  0  ;  i  <  fileHolder  .  selectedFileList  .  size  (  )  ;  i  ++  )  { 
msg  .  append  (  "         "  )  ; 
msg  .  append  (  fileHolder  .  selectedFileList  .  get  (  i  )  .  toString  (  )  )  ; 
if  (  i  ==  fileHolder  .  selectedFileList  .  size  (  )  -  1  &&  Console  .  getConsoleMode  (  "a"  )  )  { 
continue  ; 
} 
msg  .  append  (  "\n"  )  ; 
} 
if  (  !  Console  .  getConsoleMode  (  "a"  )  )  msg  .  append  (  "    "  )  ; 
if  (  fileHolder  .  selectedFileList  .  size  (  )  ==  0  )  { 
return  ; 
} 
for  (  int   j  =  0  ;  j  <  fileHolder  .  destFiles  .  length  ;  j  ++  )  { 
File   destFile  =  new   File  (  fileHolder  .  destFiles  [  j  ]  )  ; 
msgEntry  .  setDocInfo  (  destFile  .  toString  (  )  )  ; 
msgEntry  .  setMessageText  (  msg  .  toString  (  )  )  ; 
logger  .  logProcess  (  msgEntry  )  ; 
} 
} 







private   void   copyAdditionalDestLocations  (  FileHolder   fileHolder  )  { 
if  (  fileHolder  .  destFileName  ==  null  ||  fileHolder  .  destFiles  .  length  <  2  )  { 
return  ; 
} 
if  (  fileHolder  .  destFiles  [  0  ]  ==  null  )  { 
return  ; 
} 
File   firstDestFile  =  new   File  (  fileHolder  .  destFiles  [  0  ]  )  ; 
if  (  !  firstDestFile  .  isFile  (  )  )  { 
return  ; 
} 
for  (  int   i  =  1  ;  i  <  fileHolder  .  destFiles  .  length  ;  i  ++  )  { 
File   otherDestFile  =  new   File  (  fileHolder  .  destFiles  [  i  ]  )  ; 
fileUtils  .  copyFile  (  firstDestFile  ,  otherDestFile  )  ; 
} 
} 









private   void   addFileNameExtension  (  FileHolder   fileHolder  )  { 
if  (  fileHolder  .  destFileName  ==  null  )  return  ; 
String  [  ]  newDestFiles  =  new   String  [  fileHolder  .  destFiles  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  fileHolder  .  destFiles  .  length  ;  i  ++  )  { 
String   newFileName  =  fileHolder  .  destFiles  [  i  ]  .  toLowerCase  (  )  ; 
if  (  this  .  zapType  .  equals  (  XmultraConfig  .  TAR_GZIP_VALUE  )  )  { 
if  (  newFileName  .  endsWith  (  TAR_GZIP_EXTENSION_1  )  ||  newFileName  .  endsWith  (  TAR_GZIP_EXTENSION_2  )  ||  newFileName  .  endsWith  (  TAR_GZIP_EXTENSION_3  )  )  { 
newDestFiles  [  i  ]  =  fileHolder  .  destFiles  [  i  ]  ; 
continue  ; 
} 
} 
if  (  newFileName  .  endsWith  (  this  .  fileNameExtension  )  )  { 
newDestFiles  [  i  ]  =  fileHolder  .  destFiles  [  i  ]  ; 
}  else  { 
newDestFiles  [  i  ]  =  fileHolder  .  destFiles  [  i  ]  +  this  .  fileNameExtension  ; 
} 
} 
fileHolder  .  destFiles  =  newDestFiles  ; 
} 








private   void   selectInputFiles  (  FileHolder   fileHolder  )  { 
fileHolder  .  selectedFileList  =  new   ArrayList  <  File  >  (  )  ; 
for  (  int   i  =  fileHolder  .  srcFirstFileIndex  ;  i  <=  fileHolder  .  srcLastFileIndex  ;  i  ++  )  { 
File   inputFile  =  (  File  )  fileHolder  .  srcFileList  .  get  (  i  )  ; 
String   fileName  =  inputFile  .  getName  (  )  ; 
if  (  this  .  nameSelector  .  isIncluded  (  fileName  )  )  { 
fileHolder  .  selectedFileList  .  add  (  inputFile  )  ; 
} 
} 
} 






private   void   setDestinationFileName  (  FileHolder   fileHolder  )  { 
if  (  this  .  destinationFileNameType  .  equals  (  XmultraConfig  .  FIXED_VALUE  )  )  { 
fileHolder  .  destFileName  =  this  .  destinationFixedFileName  ; 
}  else   if  (  this  .  destinationFileNameType  .  equals  (  XmultraConfig  .  UNIQUE_ID_VALUE  )  )  { 
fileHolder  .  destFileName  =  FileUtils  .  getUniqueFileName  (  null  ,  null  )  ; 
}  else   if  (  this  .  destinationFileNameType  .  equals  (  XmultraConfig  .  SOURCE_NAME_VALUE  )  )  { 
if  (  fileHolder  .  selectedFileList  ==  null  ||  fileHolder  .  selectedFileList  .  size  (  )  ==  0  ||  fileHolder  .  selectedFileList  .  get  (  0  )  ==  null  )  { 
fileHolder  .  destFileName  =  null  ; 
}  else  { 
File   selectedFile  =  fileHolder  .  selectedFileList  .  get  (  0  )  ; 
fileHolder  .  destFileName  =  selectedFile  .  getName  (  )  ; 
} 
} 
} 









private   boolean   setFileHolderIndex  (  FileHolder   fileHolder  )  { 
if  (  this  .  zapMode  .  equals  (  XmultraConfig  .  INDIVIDUAL_FILES_OVER_UNMOD_VALUE  )  ||  this  .  zapType  .  equals  (  XmultraConfig  .  GZIP_VALUE  )  )  { 
fileHolder  .  srcLastFileIndex  =  fileHolder  .  srcFirstFileIndex  ; 
return   true  ; 
}  else   if  (  this  .  zapMode  .  equals  (  XmultraConfig  .  WHEN_ALL_FILES_OVER_UNMOD_VALUE  )  )  { 
if  (  areAllFilesOverUnmodifiedTime  (  listHolder  )  )  { 
fileHolder  .  srcLastFileIndex  =  fileHolder  .  srcFileList  .  size  (  )  -  1  ; 
return   true  ; 
}  else  { 
super  .  listHolder  .  setIndex  (  listHolder  .  getList  (  )  .  size  (  )  )  ; 
return   false  ; 
} 
}  else   if  (  this  .  zapMode  .  equals  (  XmultraConfig  .  ONLY_THOSE_FILES_OVER_UNMOD_VALUE  )  )  { 
fileHolder  .  srcLastFileIndex  =  fileHolder  .  srcFileList  .  size  (  )  -  1  ; 
return   true  ; 
} 
return   false  ; 
} 












private   boolean   areAllFilesOverUnmodifiedTime  (  ListHolder   listHolder  )  { 
File   firstFile  =  (  File  )  listHolder  .  getList  (  )  .  get  (  listHolder  .  getIndex  (  )  )  ; 
File   srcDir  =  firstFile  .  getParentFile  (  )  ; 
File  [  ]  files  =  srcDir  .  listFiles  (  new   FilesOnlyFilter  (  )  )  ; 
if  (  files  .  length  ==  listHolder  .  getList  (  )  .  size  (  )  )  { 
return   true  ; 
} 
return   false  ; 
} 






private   void   gzip  (  FileHolder   fileHolder  )  { 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
if  (  fileHolder  .  selectedFileList  .  size  (  )  ==  0  )  { 
return  ; 
} 
File   destFile  =  new   File  (  fileHolder  .  destFiles  [  0  ]  )  ; 
try  { 
OutputStream   outStream  =  new   FileOutputStream  (  destFile  )  ; 
outStream  =  new   GZIPOutputStream  (  outStream  )  ; 
File   selectedFile  =  fileHolder  .  selectedFileList  .  get  (  0  )  ; 
super  .  currentObjBeingProcessed  =  selectedFile  ; 
this  .  inStream  =  new   FileInputStream  (  selectedFile  )  ; 
while  (  (  bytes_read  =  this  .  inStream  .  read  (  buffer  )  )  !=  -  1  )  { 
outStream  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
outStream  .  close  (  )  ; 
this  .  inStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
errEntry  .  setThrowable  (  e  )  ; 
errEntry  .  setAppContext  (  "gzip()"  )  ; 
errEntry  .  setAppMessage  (  "Error gzip'ing: "  +  destFile  )  ; 
logger  .  logError  (  errEntry  )  ; 
} 
} 










private   void   zip  (  FileHolder   fileHolder  ,  int   zipCompressionLevel  )  { 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
if  (  fileHolder  .  selectedFileList  .  size  (  )  ==  0  )  { 
return  ; 
} 
File   zipDestFile  =  new   File  (  fileHolder  .  destFiles  [  0  ]  )  ; 
try  { 
ZipOutputStream   outStream  =  new   ZipOutputStream  (  new   FileOutputStream  (  zipDestFile  )  )  ; 
for  (  int   i  =  0  ;  i  <  fileHolder  .  selectedFileList  .  size  (  )  ;  i  ++  )  { 
File   selectedFile  =  fileHolder  .  selectedFileList  .  get  (  i  )  ; 
super  .  currentObjBeingProcessed  =  selectedFile  ; 
this  .  inStream  =  new   FileInputStream  (  selectedFile  )  ; 
ZipEntry   entry  =  new   ZipEntry  (  selectedFile  .  getName  (  )  )  ; 
outStream  .  setLevel  (  zipCompressionLevel  )  ; 
outStream  .  putNextEntry  (  entry  )  ; 
while  (  (  bytes_read  =  this  .  inStream  .  read  (  buffer  )  )  !=  -  1  )  { 
outStream  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
outStream  .  closeEntry  (  )  ; 
this  .  inStream  .  close  (  )  ; 
} 
outStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
errEntry  .  setThrowable  (  e  )  ; 
errEntry  .  setAppContext  (  "gzip()"  )  ; 
errEntry  .  setAppMessage  (  "Error zipping: "  +  zipDestFile  )  ; 
logger  .  logError  (  errEntry  )  ; 
} 
return  ; 
} 








private   void   tar  (  FileHolder   fileHolder  ,  boolean   gzipIt  )  { 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
if  (  fileHolder  .  selectedFileList  .  size  (  )  ==  0  )  { 
return  ; 
} 
File   tarDestFile  =  new   File  (  fileHolder  .  destFiles  [  0  ]  )  ; 
try  { 
OutputStream   outStream  =  new   FileOutputStream  (  tarDestFile  )  ; 
if  (  gzipIt  )  { 
outStream  =  new   GZIPOutputStream  (  outStream  )  ; 
} 
TarOutputStream   tarOutStream  =  new   TarOutputStream  (  outStream  )  ; 
for  (  int   i  =  0  ;  i  <  fileHolder  .  selectedFileList  .  size  (  )  ;  i  ++  )  { 
File   selectedFile  =  fileHolder  .  selectedFileList  .  get  (  i  )  ; 
super  .  currentObjBeingProcessed  =  selectedFile  ; 
this  .  inStream  =  new   FileInputStream  (  selectedFile  )  ; 
TarEntry   tarEntry  =  null  ; 
try  { 
tarEntry  =  new   TarEntry  (  selectedFile  ,  selectedFile  .  getName  (  )  )  ; 
}  catch  (  InvalidHeaderException   e  )  { 
errEntry  .  setThrowable  (  e  )  ; 
errEntry  .  setAppContext  (  "tar()"  )  ; 
errEntry  .  setAppMessage  (  "Error tar'ing: "  +  selectedFile  )  ; 
logger  .  logError  (  errEntry  )  ; 
} 
tarOutStream  .  putNextEntry  (  tarEntry  )  ; 
while  (  (  bytes_read  =  inStream  .  read  (  buffer  )  )  !=  -  1  )  { 
tarOutStream  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
tarOutStream  .  closeEntry  (  )  ; 
inStream  .  close  (  )  ; 
super  .  processorSyncFlag  .  restartWaitUntilFalse  (  )  ; 
} 
tarOutStream  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
errEntry  .  setThrowable  (  e  )  ; 
errEntry  .  setAppContext  (  "tar()"  )  ; 
errEntry  .  setAppMessage  (  "Error tar'ing: "  +  tarDestFile  )  ; 
logger  .  logError  (  errEntry  )  ; 
} 
} 




private   class   FilesOnlyFilter   implements   FileFilter  { 

public   boolean   accept  (  File   file  )  { 
return   file  .  isFile  (  )  ; 
} 
} 
} 

