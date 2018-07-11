package   com  .  sleepycatje  .  je  .  log  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  ClosedChannelException  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  channels  .  FileLock  ; 
import   java  .  nio  .  channels  .  OverlappingFileLockException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  Set  ; 
import   com  .  sleepycatje  .  je  .  DatabaseException  ; 
import   com  .  sleepycatje  .  je  .  EnvironmentStats  ; 
import   com  .  sleepycatje  .  je  .  RunRecoveryException  ; 
import   com  .  sleepycatje  .  je  .  StatsConfig  ; 
import   com  .  sleepycatje  .  je  .  config  .  EnvironmentParams  ; 
import   com  .  sleepycatje  .  je  .  dbi  .  DbConfigManager  ; 
import   com  .  sleepycatje  .  je  .  dbi  .  EnvironmentImpl  ; 
import   com  .  sleepycatje  .  je  .  latch  .  Latch  ; 
import   com  .  sleepycatje  .  je  .  latch  .  LatchSupport  ; 
import   com  .  sleepycatje  .  je  .  log  .  entry  .  LogEntry  ; 
import   com  .  sleepycatje  .  je  .  log  .  entry  .  SingleItemEntry  ; 
import   com  .  sleepycatje  .  je  .  utilint  .  DbLsn  ; 
import   com  .  sleepycatje  .  je  .  utilint  .  HexFormatter  ; 





public   class   FileManager  { 

public   static   class   FileMode  { 

public   static   final   FileMode   READ_MODE  =  new   FileMode  (  "r"  )  ; 

public   static   final   FileMode   READWRITE_MODE  =  new   FileMode  (  "rw"  )  ; 

private   String   fileModeValue  ; 

private   FileMode  (  String   fileModeValue  )  { 
this  .  fileModeValue  =  fileModeValue  ; 
} 

public   String   getModeValue  (  )  { 
return   fileModeValue  ; 
} 
} 

static   boolean   IO_EXCEPTION_TESTING  =  false  ; 

private   static   final   String   DEBUG_NAME  =  FileManager  .  class  .  getName  (  )  ; 

private   static   long   writeCount  =  0  ; 

private   static   long   stopOnWriteCount  =  Long  .  MAX_VALUE  ; 

public   static   final   String   JE_SUFFIX  =  ".jdb"  ; 

public   static   final   String   DEL_SUFFIX  =  ".del"  ; 

public   static   final   String   BAD_SUFFIX  =  ".bad"  ; 

private   static   final   String   LOCK_FILE  =  "je.lck"  ; 

static   final   String  [  ]  DEL_SUFFIXES  =  {  DEL_SUFFIX  }  ; 

static   final   String  [  ]  JE_SUFFIXES  =  {  JE_SUFFIX  }  ; 

private   static   final   String  [  ]  JE_AND_DEL_SUFFIXES  =  {  JE_SUFFIX  ,  DEL_SUFFIX  }  ; 

private   boolean   syncAtFileEnd  =  true  ; 

private   EnvironmentImpl   envImpl  ; 

private   long   maxFileSize  ; 

private   File   dbEnvHome  ; 

private   boolean   includeDeletedFiles  =  false  ; 

private   FileCache   fileCache  ; 

private   Latch   fileCacheLatch  ; 

private   RandomAccessFile   lockFile  ; 

private   FileChannel   channel  ; 

private   FileLock   envLock  ; 

private   FileLock   exclLock  ; 

private   boolean   readOnly  ; 

private   long   currentFileNum  ; 

private   long   nextAvailableLsn  ; 

private   long   lastUsedLsn  ; 

private   long   prevOffset  ; 

private   boolean   forceNewFile  ; 

private   long   savedCurrentFileNum  ; 

private   long   savedNextAvailableLsn  ; 

private   long   savedLastUsedLsn  ; 

private   long   savedPrevOffset  ; 

private   boolean   savedForceNewFile  ; 

private   LogEndFileDescriptor   endOfLog  ; 

private   FSyncManager   syncManager  ; 

private   Map   perFileLastUsedLsn  ; 

private   boolean   useNIO  ; 

private   long   chunkedNIOSize  =  0  ; 








public   FileManager  (  EnvironmentImpl   envImpl  ,  File   dbEnvHome  ,  boolean   readOnly  )  throws   DatabaseException  { 
this  .  envImpl  =  envImpl  ; 
this  .  dbEnvHome  =  dbEnvHome  ; 
this  .  readOnly  =  readOnly  ; 
DbConfigManager   configManager  =  envImpl  .  getConfigManager  (  )  ; 
maxFileSize  =  configManager  .  getLong  (  EnvironmentParams  .  LOG_FILE_MAX  )  ; 
useNIO  =  configManager  .  getBoolean  (  EnvironmentParams  .  LOG_USE_NIO  )  ; 
chunkedNIOSize  =  configManager  .  getLong  (  EnvironmentParams  .  LOG_CHUNKED_NIO  )  ; 
boolean   directNIO  =  configManager  .  getBoolean  (  EnvironmentParams  .  LOG_DIRECT_NIO  )  ; 
if  (  !  useNIO  &&  (  chunkedNIOSize  >  0  ||  directNIO  )  )  { 
throw   new   IllegalArgumentException  (  EnvironmentParams  .  LOG_USE_NIO  .  getName  (  )  +  " is false and therefore "  +  EnvironmentParams  .  LOG_DIRECT_NIO  .  getName  (  )  +  " or "  +  EnvironmentParams  .  LOG_CHUNKED_NIO  .  getName  (  )  +  " may not be used."  )  ; 
} 
if  (  !  envImpl  .  isMemOnly  (  )  )  { 
if  (  !  dbEnvHome  .  exists  (  )  )  { 
throw   new   LogException  (  "Environment home "  +  dbEnvHome  +  " doesn't exist"  )  ; 
} 
lockEnvironment  (  readOnly  ,  false  )  ; 
} 
fileCache  =  new   FileCache  (  configManager  )  ; 
fileCacheLatch  =  LatchSupport  .  makeLatch  (  DEBUG_NAME  +  "_fileCache"  ,  envImpl  )  ; 
currentFileNum  =  0L  ; 
nextAvailableLsn  =  DbLsn  .  makeLsn  (  currentFileNum  ,  firstLogEntryOffset  (  )  )  ; 
lastUsedLsn  =  DbLsn  .  NULL_LSN  ; 
perFileLastUsedLsn  =  new   HashMap  (  )  ; 
prevOffset  =  0L  ; 
endOfLog  =  new   LogEndFileDescriptor  (  )  ; 
forceNewFile  =  false  ; 
saveLastPosition  (  )  ; 
String   stopOnWriteProp  =  System  .  getProperty  (  "je.debug.stopOnWrite"  )  ; 
if  (  stopOnWriteProp  !=  null  )  { 
stopOnWriteCount  =  Long  .  parseLong  (  stopOnWriteProp  )  ; 
} 
syncManager  =  new   FSyncManager  (  envImpl  )  ; 
} 









public   void   setLastPosition  (  long   nextAvailableLsn  ,  long   lastUsedLsn  ,  long   prevOffset  )  { 
this  .  lastUsedLsn  =  lastUsedLsn  ; 
perFileLastUsedLsn  .  put  (  new   Long  (  DbLsn  .  getFileNumber  (  lastUsedLsn  )  )  ,  new   Long  (  lastUsedLsn  )  )  ; 
this  .  nextAvailableLsn  =  nextAvailableLsn  ; 
currentFileNum  =  DbLsn  .  getFileNumber  (  this  .  nextAvailableLsn  )  ; 
this  .  prevOffset  =  prevOffset  ; 
saveLastPosition  (  )  ; 
} 

void   saveLastPosition  (  )  { 
savedNextAvailableLsn  =  nextAvailableLsn  ; 
savedLastUsedLsn  =  lastUsedLsn  ; 
savedPrevOffset  =  prevOffset  ; 
savedForceNewFile  =  forceNewFile  ; 
savedCurrentFileNum  =  currentFileNum  ; 
} 

void   restoreLastPosition  (  )  { 
nextAvailableLsn  =  savedNextAvailableLsn  ; 
lastUsedLsn  =  savedLastUsedLsn  ; 
prevOffset  =  savedPrevOffset  ; 
forceNewFile  =  savedForceNewFile  ; 
currentFileNum  =  savedCurrentFileNum  ; 
} 





public   void   setSyncAtFileEnd  (  boolean   sync  )  { 
syncAtFileEnd  =  sync  ; 
} 






public   Long   getFirstFileNum  (  )  { 
return   getFileNum  (  true  )  ; 
} 

public   boolean   getReadOnly  (  )  { 
return   readOnly  ; 
} 




public   Long   getLastFileNum  (  )  { 
return   getFileNum  (  false  )  ; 
} 

public   long   getCurrentFileNum  (  )  { 
return   currentFileNum  ; 
} 

public   void   setIncludeDeletedFiles  (  boolean   includeDeletedFiles  )  { 
this  .  includeDeletedFiles  =  includeDeletedFiles  ; 
} 





public   Long  [  ]  getAllFileNumbers  (  )  { 
String  [  ]  names  =  listFiles  (  JE_SUFFIXES  )  ; 
Long  [  ]  nums  =  new   Long  [  names  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  nums  .  length  ;  i  +=  1  )  { 
nums  [  i  ]  =  getNumFromName  (  names  [  i  ]  )  ; 
} 
return   nums  ; 
} 









public   Long   getFollowingFileNum  (  long   currentFileNum  ,  boolean   forward  )  { 
String  [  ]  names  =  listFiles  (  JE_SUFFIXES  )  ; 
String   searchName  =  getFileName  (  currentFileNum  ,  JE_SUFFIX  )  ; 
int   foundIdx  =  Arrays  .  binarySearch  (  names  ,  searchName  )  ; 
boolean   foundTarget  =  false  ; 
if  (  foundIdx  >=  0  )  { 
if  (  forward  )  { 
foundIdx  ++  ; 
}  else  { 
foundIdx  --  ; 
} 
}  else  { 
foundIdx  =  Math  .  abs  (  foundIdx  +  1  )  ; 
if  (  !  forward  )  { 
foundIdx  --  ; 
} 
} 
if  (  forward  &&  (  foundIdx  <  names  .  length  )  )  { 
foundTarget  =  true  ; 
}  else   if  (  !  forward  &&  (  foundIdx  >  -  1  )  )  { 
foundTarget  =  true  ; 
} 
if  (  foundTarget  )  { 
return   getNumFromName  (  names  [  foundIdx  ]  )  ; 
}  else  { 
return   null  ; 
} 
} 




public   boolean   filesExist  (  )  { 
String  [  ]  names  =  listFiles  (  JE_SUFFIXES  )  ; 
return  (  names  .  length  !=  0  )  ; 
} 







private   Long   getFileNum  (  boolean   first  )  { 
String  [  ]  names  =  listFiles  (  JE_SUFFIXES  )  ; 
if  (  names  .  length  ==  0  )  { 
return   null  ; 
}  else  { 
int   index  =  0  ; 
if  (  !  first  )  { 
index  =  names  .  length  -  1  ; 
} 
return   getNumFromName  (  names  [  index  ]  )  ; 
} 
} 







public   Long   getNumFromName  (  String   fileName  )  { 
String   fileNumber  =  fileName  .  substring  (  0  ,  fileName  .  indexOf  (  "."  )  )  ; 
return   new   Long  (  Long  .  parseLong  (  fileNumber  ,  16  )  )  ; 
} 






public   String  [  ]  listFiles  (  String  [  ]  suffixes  )  { 
String  [  ]  fileNames  =  dbEnvHome  .  list  (  new   JEFileFilter  (  suffixes  )  )  ; 
if  (  fileNames  !=  null  )  { 
Arrays  .  sort  (  fileNames  )  ; 
}  else  { 
fileNames  =  new   String  [  0  ]  ; 
} 
return   fileNames  ; 
} 








public   String  [  ]  listFiles  (  long   minFileNumber  ,  long   maxFileNumber  )  { 
String  [  ]  fileNames  =  dbEnvHome  .  list  (  new   JEFileFilter  (  JE_SUFFIXES  ,  minFileNumber  ,  maxFileNumber  )  )  ; 
Arrays  .  sort  (  fileNames  )  ; 
return   fileNames  ; 
} 







public   static   String  [  ]  listFiles  (  File   envDirFile  ,  String  [  ]  suffixes  )  { 
String  [  ]  fileNames  =  envDirFile  .  list  (  new   JEFileFilter  (  suffixes  )  )  ; 
if  (  fileNames  !=  null  )  { 
Arrays  .  sort  (  fileNames  )  ; 
}  else  { 
fileNames  =  new   String  [  0  ]  ; 
} 
return   fileNames  ; 
} 




String  [  ]  getFullFileNames  (  long   fileNum  )  { 
if  (  includeDeletedFiles  )  { 
int   nSuffixes  =  JE_AND_DEL_SUFFIXES  .  length  ; 
String  [  ]  ret  =  new   String  [  nSuffixes  ]  ; 
for  (  int   i  =  0  ;  i  <  nSuffixes  ;  i  ++  )  { 
ret  [  i  ]  =  getFullFileName  (  getFileName  (  fileNum  ,  JE_AND_DEL_SUFFIXES  [  i  ]  )  )  ; 
} 
return   ret  ; 
}  else  { 
return   new   String  [  ]  {  getFullFileName  (  getFileName  (  fileNum  ,  JE_SUFFIX  )  )  }  ; 
} 
} 





public   String   getFullFileName  (  long   fileNum  ,  String   suffix  )  { 
return   getFullFileName  (  getFileName  (  fileNum  ,  suffix  )  )  ; 
} 




private   String   getFullFileName  (  String   fileName  )  { 
return   dbEnvHome  +  File  .  separator  +  fileName  ; 
} 




public   static   String   getFileName  (  long   fileNum  ,  String   suffix  )  { 
return  (  HexFormatter  .  formatLong  (  fileNum  )  .  substring  (  10  )  +  suffix  )  ; 
} 









public   void   renameFile  (  long   fileNum  ,  String   newSuffix  )  throws   DatabaseException  ,  IOException  { 
int   repeatNum  =  0  ; 
boolean   renamed  =  false  ; 
while  (  !  renamed  )  { 
String   generation  =  ""  ; 
if  (  repeatNum  >  0  )  { 
generation  =  "."  +  repeatNum  ; 
} 
String   newName  =  getFullFileName  (  getFileName  (  fileNum  ,  newSuffix  )  +  generation  )  ; 
File   targetFile  =  new   File  (  newName  )  ; 
if  (  targetFile  .  exists  (  )  )  { 
repeatNum  ++  ; 
}  else  { 
String   oldFileName  =  getFullFileNames  (  fileNum  )  [  0  ]  ; 
clearFileCache  (  fileNum  )  ; 
File   oldFile  =  new   File  (  oldFileName  )  ; 
if  (  oldFile  .  renameTo  (  targetFile  )  )  { 
renamed  =  true  ; 
}  else  { 
throw   new   LogException  (  "Couldn't rename "  +  oldFileName  +  " to "  +  newName  )  ; 
} 
} 
} 
} 






public   void   deleteFile  (  long   fileNum  )  throws   DatabaseException  ,  IOException  { 
String   fileName  =  getFullFileNames  (  fileNum  )  [  0  ]  ; 
clearFileCache  (  fileNum  )  ; 
File   file  =  new   File  (  fileName  )  ; 
boolean   done  =  file  .  delete  (  )  ; 
if  (  !  done  )  { 
throw   new   LogException  (  "Couldn't delete "  +  file  )  ; 
} 
} 











FileHandle   getFileHandle  (  long   fileNum  )  throws   LogException  ,  DatabaseException  { 
Long   fileId  =  new   Long  (  fileNum  )  ; 
FileHandle   fileHandle  =  null  ; 
while  (  true  )  { 
fileHandle  =  fileCache  .  get  (  fileId  )  ; 
if  (  fileHandle  ==  null  )  { 
fileCacheLatch  .  acquire  (  )  ; 
try  { 
fileHandle  =  fileCache  .  get  (  fileId  )  ; 
if  (  fileHandle  ==  null  )  { 
fileHandle  =  makeFileHandle  (  fileNum  ,  FileMode  .  READ_MODE  )  ; 
fileCache  .  add  (  fileId  ,  fileHandle  )  ; 
} 
}  finally  { 
fileCacheLatch  .  release  (  )  ; 
} 
} 
fileHandle  .  latch  (  )  ; 
if  (  fileHandle  .  getFile  (  )  ==  null  )  { 
fileHandle  .  release  (  )  ; 
}  else  { 
break  ; 
} 
} 
return   fileHandle  ; 
} 

private   FileHandle   makeFileHandle  (  long   fileNum  ,  FileMode   mode  )  throws   DatabaseException  { 
String  [  ]  fileNames  =  getFullFileNames  (  fileNum  )  ; 
RandomAccessFile   newFile  =  null  ; 
String   fileName  =  null  ; 
try  { 
FileNotFoundException   FNFE  =  null  ; 
for  (  int   i  =  0  ;  i  <  fileNames  .  length  ;  i  ++  )  { 
fileName  =  fileNames  [  i  ]  ; 
try  { 
newFile  =  new   RandomAccessFile  (  fileName  ,  mode  .  getModeValue  (  )  )  ; 
break  ; 
}  catch  (  FileNotFoundException   e  )  { 
if  (  FNFE  ==  null  )  { 
FNFE  =  e  ; 
} 
} 
} 
if  (  newFile  ==  null  )  { 
throw   FNFE  ; 
} 
boolean   oldHeaderVersion  =  false  ; 
if  (  newFile  .  length  (  )  ==  0  )  { 
if  (  mode  ==  FileMode  .  READWRITE_MODE  )  { 
long   lastLsn  =  DbLsn  .  longToLsn  (  (  Long  )  perFileLastUsedLsn  .  remove  (  new   Long  (  fileNum  -  1  )  )  )  ; 
long   headerPrevOffset  =  0  ; 
if  (  lastLsn  !=  DbLsn  .  NULL_LSN  )  { 
headerPrevOffset  =  DbLsn  .  getFileOffset  (  lastLsn  )  ; 
} 
FileHeader   fileHeader  =  new   FileHeader  (  fileNum  ,  headerPrevOffset  )  ; 
writeFileHeader  (  newFile  ,  fileName  ,  fileHeader  )  ; 
} 
}  else  { 
oldHeaderVersion  =  readAndValidateFileHeader  (  newFile  ,  fileName  ,  fileNum  )  ; 
} 
return   new   FileHandle  (  newFile  ,  fileName  ,  envImpl  ,  oldHeaderVersion  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   LogFileNotFoundException  (  "Couldn't open file "  +  fileName  +  ": "  +  e  .  getMessage  (  )  )  ; 
}  catch  (  DbChecksumException   e  )  { 
closeFileInErrorCase  (  newFile  )  ; 
throw   new   DbChecksumException  (  envImpl  ,  "Couldn't open file "  +  fileName  ,  e  )  ; 
}  catch  (  Throwable   t  )  { 
closeFileInErrorCase  (  newFile  )  ; 
throw   new   DatabaseException  (  "Couldn't open file "  +  fileName  +  ": "  +  t  ,  t  )  ; 
} 
} 




private   void   closeFileInErrorCase  (  RandomAccessFile   file  )  { 
try  { 
if  (  file  !=  null  )  { 
file  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 








private   boolean   readAndValidateFileHeader  (  RandomAccessFile   file  ,  String   fileName  ,  long   fileNum  )  throws   DatabaseException  ,  IOException  { 
LogManager   logManager  =  envImpl  .  getLogManager  (  )  ; 
LogEntry   headerEntry  =  logManager  .  getLogEntry  (  DbLsn  .  makeLsn  (  fileNum  ,  0  )  ,  file  )  ; 
FileHeader   header  =  (  FileHeader  )  headerEntry  .  getMainItem  (  )  ; 
return   header  .  validate  (  fileName  ,  fileNum  )  ; 
} 




private   void   writeFileHeader  (  RandomAccessFile   file  ,  String   fileName  ,  FileHeader   header  )  throws   DatabaseException  ,  IOException  { 
envImpl  .  checkIfInvalid  (  )  ; 
if  (  envImpl  .  mayNotWrite  (  )  )  { 
return  ; 
} 
LogEntry   headerLogEntry  =  new   SingleItemEntry  (  LogEntryType  .  LOG_FILE_HEADER  ,  header  )  ; 
ByteBuffer   headerBuf  =  envImpl  .  getLogManager  (  )  .  putIntoBuffer  (  headerLogEntry  ,  0  )  ; 
if  (  ++  writeCount  >=  stopOnWriteCount  )  { 
Runtime  .  getRuntime  (  )  .  halt  (  0xff  )  ; 
} 
int   bytesWritten  ; 
try  { 
if  (  RUNRECOVERY_EXCEPTION_TESTING  )  { 
generateRunRecoveryException  (  file  ,  headerBuf  ,  0  )  ; 
} 
bytesWritten  =  writeToFile  (  file  ,  headerBuf  ,  0  )  ; 
}  catch  (  ClosedChannelException   e  )  { 
throw   new   RunRecoveryException  (  envImpl  ,  "Channel closed, may be due to thread interrupt"  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RunRecoveryException  (  envImpl  ,  "IOException caught: "  +  e  )  ; 
} 
if  (  bytesWritten  !=  headerLogEntry  .  getSize  (  )  +  LogEntryHeader  .  MIN_HEADER_SIZE  )  { 
throw   new   LogException  (  "File "  +  fileName  +  " was created with an incomplete header. Only "  +  bytesWritten  +  " bytes were written."  )  ; 
} 
} 




long   getFileHeaderPrevOffset  (  long   fileNum  )  throws   IOException  ,  DatabaseException  { 
LogEntry   headerEntry  =  envImpl  .  getLogManager  (  )  .  getLogEntry  (  DbLsn  .  makeLsn  (  fileNum  ,  0  )  )  ; 
FileHeader   header  =  (  FileHeader  )  headerEntry  .  getMainItem  (  )  ; 
return   header  .  getLastEntryInPrevFileOffset  (  )  ; 
} 







long   getPrevEntryOffset  (  )  { 
return   prevOffset  ; 
} 








boolean   bumpLsn  (  long   size  )  { 
saveLastPosition  (  )  ; 
boolean   flippedFiles  =  false  ; 
if  (  forceNewFile  ||  (  DbLsn  .  getFileOffset  (  nextAvailableLsn  )  +  size  )  >  maxFileSize  )  { 
forceNewFile  =  false  ; 
currentFileNum  ++  ; 
if  (  lastUsedLsn  !=  DbLsn  .  NULL_LSN  )  { 
perFileLastUsedLsn  .  put  (  new   Long  (  DbLsn  .  getFileNumber  (  lastUsedLsn  )  )  ,  new   Long  (  lastUsedLsn  )  )  ; 
} 
prevOffset  =  0  ; 
lastUsedLsn  =  DbLsn  .  makeLsn  (  currentFileNum  ,  firstLogEntryOffset  (  )  )  ; 
flippedFiles  =  true  ; 
}  else  { 
if  (  lastUsedLsn  ==  DbLsn  .  NULL_LSN  )  { 
prevOffset  =  0  ; 
}  else  { 
prevOffset  =  DbLsn  .  getFileOffset  (  lastUsedLsn  )  ; 
} 
lastUsedLsn  =  nextAvailableLsn  ; 
} 
nextAvailableLsn  =  DbLsn  .  makeLsn  (  DbLsn  .  getFileNumber  (  lastUsedLsn  )  ,  (  DbLsn  .  getFileOffset  (  lastUsedLsn  )  +  size  )  )  ; 
return   flippedFiles  ; 
} 





void   writeLogBuffer  (  LogBuffer   fullBuffer  )  throws   DatabaseException  { 
envImpl  .  checkIfInvalid  (  )  ; 
if  (  envImpl  .  mayNotWrite  (  )  )  { 
return  ; 
} 
long   firstLsn  =  fullBuffer  .  getFirstLsn  (  )  ; 
if  (  firstLsn  !=  DbLsn  .  NULL_LSN  )  { 
RandomAccessFile   file  =  endOfLog  .  getWritableFile  (  DbLsn  .  getFileNumber  (  firstLsn  )  )  ; 
ByteBuffer   data  =  fullBuffer  .  getDataBuffer  (  )  ; 
if  (  ++  writeCount  >=  stopOnWriteCount  )  { 
Runtime  .  getRuntime  (  )  .  halt  (  0xff  )  ; 
} 
try  { 
assert   fullBuffer  .  getRewriteAllowed  (  )  ||  (  DbLsn  .  getFileOffset  (  firstLsn  )  >=  file  .  length  (  )  ||  file  .  length  (  )  ==  firstLogEntryOffset  (  )  )  :  "FileManager would overwrite non-empty file 0x"  +  Long  .  toHexString  (  DbLsn  .  getFileNumber  (  firstLsn  )  )  +  " lsnOffset=0x"  +  Long  .  toHexString  (  DbLsn  .  getFileOffset  (  firstLsn  )  )  +  " fileLength=0x"  +  Long  .  toHexString  (  file  .  length  (  )  )  ; 
if  (  IO_EXCEPTION_TESTING  )  { 
throw   new   IOException  (  "generated for testing"  )  ; 
} 
if  (  RUNRECOVERY_EXCEPTION_TESTING  )  { 
generateRunRecoveryException  (  file  ,  data  ,  DbLsn  .  getFileOffset  (  firstLsn  )  )  ; 
} 
writeToFile  (  file  ,  data  ,  DbLsn  .  getFileOffset  (  firstLsn  )  )  ; 
}  catch  (  ClosedChannelException   e  )  { 
throw   new   RunRecoveryException  (  envImpl  ,  "File closed, may be due to thread interrupt"  ,  e  )  ; 
}  catch  (  IOException   IOE  )  { 
abortCommittedTxns  (  data  )  ; 
try  { 
if  (  IO_EXCEPTION_TESTING  )  { 
throw   new   IOException  (  "generated for testing"  )  ; 
} 
writeToFile  (  file  ,  data  ,  DbLsn  .  getFileOffset  (  firstLsn  )  )  ; 
}  catch  (  IOException   IOE2  )  { 
fullBuffer  .  setRewriteAllowed  (  )  ; 
throw   new   DatabaseException  (  IOE2  )  ; 
} 
} 
assert   EnvironmentImpl  .  maybeForceYield  (  )  ; 
} 
} 




private   int   writeToFile  (  RandomAccessFile   file  ,  ByteBuffer   data  ,  long   destOffset  )  throws   IOException  ,  DatabaseException  { 
int   totalBytesWritten  =  0  ; 
if  (  useNIO  )  { 
FileChannel   channel  =  file  .  getChannel  (  )  ; 
if  (  chunkedNIOSize  >  0  )  { 
ByteBuffer   useData  =  data  .  duplicate  (  )  ; 
int   originalLimit  =  useData  .  limit  (  )  ; 
useData  .  limit  (  useData  .  position  (  )  )  ; 
while  (  useData  .  limit  (  )  <  originalLimit  )  { 
useData  .  limit  (  (  int  )  (  Math  .  min  (  useData  .  limit  (  )  +  chunkedNIOSize  ,  originalLimit  )  )  )  ; 
int   bytesWritten  =  channel  .  write  (  useData  ,  destOffset  )  ; 
destOffset  +=  bytesWritten  ; 
totalBytesWritten  +=  bytesWritten  ; 
} 
}  else  { 
totalBytesWritten  =  channel  .  write  (  data  ,  destOffset  )  ; 
} 
}  else  { 
synchronized  (  file  )  { 
assert   data  .  hasArray  (  )  ; 
assert   data  .  arrayOffset  (  )  ==  0  ; 
int   pos  =  data  .  position  (  )  ; 
int   size  =  data  .  limit  (  )  -  pos  ; 
file  .  seek  (  destOffset  )  ; 
file  .  write  (  data  .  array  (  )  ,  pos  ,  size  )  ; 
data  .  position  (  pos  +  size  )  ; 
totalBytesWritten  =  size  ; 
} 
} 
return   totalBytesWritten  ; 
} 




void   readFromFile  (  RandomAccessFile   file  ,  ByteBuffer   readBuffer  ,  long   offset  )  throws   IOException  { 
if  (  useNIO  )  { 
FileChannel   channel  =  file  .  getChannel  (  )  ; 
if  (  chunkedNIOSize  >  0  )  { 
int   readLength  =  readBuffer  .  limit  (  )  ; 
long   currentPosition  =  offset  ; 
while  (  readBuffer  .  position  (  )  <  readLength  )  { 
readBuffer  .  limit  (  (  int  )  (  Math  .  min  (  readBuffer  .  limit  (  )  +  chunkedNIOSize  ,  readLength  )  )  )  ; 
int   bytesRead  =  channel  .  read  (  readBuffer  ,  currentPosition  )  ; 
if  (  bytesRead  <  1  )  break  ; 
currentPosition  +=  bytesRead  ; 
} 
}  else  { 
channel  .  read  (  readBuffer  ,  offset  )  ; 
} 
}  else  { 
synchronized  (  file  )  { 
assert   readBuffer  .  hasArray  (  )  ; 
assert   readBuffer  .  arrayOffset  (  )  ==  0  ; 
int   pos  =  readBuffer  .  position  (  )  ; 
int   size  =  readBuffer  .  limit  (  )  -  pos  ; 
file  .  seek  (  offset  )  ; 
int   bytesRead  =  file  .  read  (  readBuffer  .  array  (  )  ,  pos  ,  size  )  ; 
if  (  bytesRead  >  0  )  { 
readBuffer  .  position  (  pos  +  bytesRead  )  ; 
} 
} 
} 
} 

private   void   abortCommittedTxns  (  ByteBuffer   data  )  throws   DatabaseException  { 
final   byte   commitType  =  LogEntryType  .  LOG_TXN_COMMIT  .  getTypeNum  (  )  ; 
final   byte   abortType  =  LogEntryType  .  LOG_TXN_ABORT  .  getTypeNum  (  )  ; 
data  .  position  (  0  )  ; 
while  (  data  .  remaining  (  )  >  0  )  { 
int   recStartPos  =  data  .  position  (  )  ; 
LogEntryHeader   header  =  new   LogEntryHeader  (  envImpl  ,  data  ,  false  )  ; 
if  (  header  .  getType  (  )  ==  commitType  )  { 
header  .  convertCommitToAbort  (  data  )  ; 
} 
data  .  position  (  recStartPos  +  header  .  getSize  (  )  +  header  .  getItemSize  (  )  )  ; 
} 
data  .  position  (  0  )  ; 
} 




void   syncLogEnd  (  )  throws   DatabaseException  { 
try  { 
endOfLog  .  force  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   DatabaseException  (  e  )  ; 
} 
} 





void   syncLogEndAndFinishFile  (  )  throws   DatabaseException  ,  IOException  { 
if  (  syncAtFileEnd  )  { 
syncLogEnd  (  )  ; 
} 
endOfLog  .  close  (  )  ; 
} 





void   groupSync  (  )  throws   DatabaseException  { 
syncManager  .  fsync  (  )  ; 
} 




public   void   clear  (  )  throws   IOException  ,  DatabaseException  { 
fileCacheLatch  .  acquire  (  )  ; 
try  { 
fileCache  .  clear  (  )  ; 
}  finally  { 
fileCacheLatch  .  release  (  )  ; 
} 
endOfLog  .  close  (  )  ; 
} 




public   void   close  (  )  throws   IOException  ,  DatabaseException  { 
if  (  envLock  !=  null  )  { 
envLock  .  release  (  )  ; 
} 
if  (  exclLock  !=  null  )  { 
exclLock  .  release  (  )  ; 
} 
if  (  channel  !=  null  )  { 
channel  .  close  (  )  ; 
} 
if  (  lockFile  !=  null  )  { 
lockFile  .  close  (  )  ; 
lockFile  =  null  ; 
} 
} 




























public   boolean   lockEnvironment  (  boolean   readOnly  ,  boolean   exclusive  )  throws   DatabaseException  { 
try  { 
if  (  checkEnvHomePermissions  (  readOnly  )  )  { 
return   true  ; 
} 
if  (  lockFile  ==  null  )  { 
lockFile  =  new   RandomAccessFile  (  new   File  (  dbEnvHome  ,  LOCK_FILE  )  ,  FileMode  .  READWRITE_MODE  .  getModeValue  (  )  )  ; 
} 
channel  =  lockFile  .  getChannel  (  )  ; 
boolean   throwIt  =  false  ; 
try  { 
if  (  exclusive  )  { 
exclLock  =  channel  .  tryLock  (  1  ,  1  ,  false  )  ; 
if  (  exclLock  ==  null  )  { 
return   false  ; 
} 
return   true  ; 
}  else  { 
if  (  readOnly  )  { 
envLock  =  channel  .  tryLock  (  1  ,  1  ,  true  )  ; 
}  else  { 
envLock  =  channel  .  tryLock  (  0  ,  1  ,  false  )  ; 
} 
if  (  envLock  ==  null  )  { 
throwIt  =  true  ; 
} 
} 
}  catch  (  OverlappingFileLockException   e  )  { 
throwIt  =  true  ; 
} 
if  (  throwIt  )  { 
throw   new   LogException  (  "A "  +  LOCK_FILE  +  " file exists in "  +  dbEnvHome  .  getAbsolutePath  (  )  +  " The environment can not be locked for "  +  (  readOnly  ?  "shared"  :  "single writer"  )  +  " access."  )  ; 
} 
}  catch  (  IOException   IOE  )  { 
throw   new   LogException  (  IOE  .  toString  (  )  )  ; 
} 
return   true  ; 
} 

public   void   releaseExclusiveLock  (  )  throws   DatabaseException  { 
try  { 
if  (  exclLock  !=  null  )  { 
exclLock  .  release  (  )  ; 
} 
}  catch  (  IOException   IOE  )  { 
throw   new   DatabaseException  (  IOE  )  ; 
} 
} 








public   boolean   checkEnvHomePermissions  (  boolean   readOnly  )  throws   DatabaseException  { 
boolean   envDirIsReadOnly  =  !  dbEnvHome  .  canWrite  (  )  ; 
if  (  envDirIsReadOnly  &&  !  readOnly  )  { 
throw   new   DatabaseException  (  "The Environment directory "  +  dbEnvHome  .  getAbsolutePath  (  )  +  " is not writable, but the "  +  "Environment was opened for read-write access."  )  ; 
} 
return   envDirIsReadOnly  ; 
} 










public   void   truncateLog  (  long   fileNum  ,  long   offset  )  throws   IOException  ,  DatabaseException  { 
FileHandle   handle  =  makeFileHandle  (  fileNum  ,  FileMode  .  READWRITE_MODE  )  ; 
RandomAccessFile   file  =  handle  .  getFile  (  )  ; 
try  { 
file  .  getChannel  (  )  .  truncate  (  offset  )  ; 
}  finally  { 
file  .  close  (  )  ; 
} 
if  (  handle  .  isOldHeaderVersion  (  )  )  { 
forceNewFile  =  true  ; 
} 
} 




void   forceNewLogFile  (  )  { 
forceNewFile  =  true  ; 
} 




public   static   int   firstLogEntryOffset  (  )  { 
return   FileHeader  .  entrySize  (  )  +  LogEntryHeader  .  MIN_HEADER_SIZE  ; 
} 





public   long   getNextLsn  (  )  { 
return   nextAvailableLsn  ; 
} 






public   long   getLastUsedLsn  (  )  { 
return   lastUsedLsn  ; 
} 

public   long   getNFSyncs  (  )  { 
return   syncManager  .  getNFSyncs  (  )  ; 
} 

public   long   getNFSyncRequests  (  )  { 
return   syncManager  .  getNFSyncRequests  (  )  ; 
} 

public   long   getNFSyncTimeouts  (  )  { 
return   syncManager  .  getNTimeouts  (  )  ; 
} 

void   loadStats  (  StatsConfig   config  ,  EnvironmentStats   stats  )  throws   DatabaseException  { 
syncManager  .  loadStats  (  config  ,  stats  )  ; 
} 

Set   getCacheKeys  (  )  { 
return   fileCache  .  getCacheKeys  (  )  ; 
} 




private   void   clearFileCache  (  long   fileNum  )  throws   IOException  ,  DatabaseException  { 
fileCacheLatch  .  acquire  (  )  ; 
try  { 
fileCache  .  remove  (  fileNum  )  ; 
}  finally  { 
fileCacheLatch  .  release  (  )  ; 
} 
} 

private   static   class   FileCache  { 

private   Map   fileMap  ; 

private   LinkedList   fileList  ; 

private   int   fileCacheSize  ; 

FileCache  (  DbConfigManager   configManager  )  throws   DatabaseException  { 
fileMap  =  new   Hashtable  (  )  ; 
fileList  =  new   LinkedList  (  )  ; 
fileCacheSize  =  configManager  .  getInt  (  EnvironmentParams  .  LOG_FILE_CACHE_SIZE  )  ; 
} 

private   FileHandle   get  (  Long   fileId  )  { 
return  (  FileHandle  )  fileMap  .  get  (  fileId  )  ; 
} 

private   void   add  (  Long   fileId  ,  FileHandle   fileHandle  )  throws   DatabaseException  { 
if  (  fileList  .  size  (  )  >=  fileCacheSize  )  { 
Iterator   iter  =  fileList  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Long   evictId  =  (  Long  )  iter  .  next  (  )  ; 
FileHandle   evictTarget  =  (  FileHandle  )  fileMap  .  get  (  evictId  )  ; 
if  (  evictTarget  .  latchNoWait  (  )  )  { 
try  { 
fileMap  .  remove  (  evictId  )  ; 
iter  .  remove  (  )  ; 
evictTarget  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   DatabaseException  (  e  )  ; 
}  finally  { 
evictTarget  .  release  (  )  ; 
} 
break  ; 
} 
} 
} 
fileList  .  add  (  fileId  )  ; 
fileMap  .  put  (  fileId  ,  fileHandle  )  ; 
} 






private   void   remove  (  long   fileNum  )  throws   IOException  ,  DatabaseException  { 
Iterator   iter  =  fileList  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Long   evictId  =  (  Long  )  iter  .  next  (  )  ; 
if  (  evictId  .  longValue  (  )  ==  fileNum  )  { 
FileHandle   evictTarget  =  (  FileHandle  )  fileMap  .  get  (  evictId  )  ; 
try  { 
evictTarget  .  latch  (  )  ; 
fileMap  .  remove  (  evictId  )  ; 
iter  .  remove  (  )  ; 
evictTarget  .  close  (  )  ; 
}  finally  { 
evictTarget  .  release  (  )  ; 
} 
} 
} 
} 

private   void   clear  (  )  throws   IOException  ,  DatabaseException  { 
Iterator   iter  =  fileMap  .  values  (  )  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
FileHandle   fileHandle  =  (  FileHandle  )  iter  .  next  (  )  ; 
try  { 
fileHandle  .  latch  (  )  ; 
fileHandle  .  close  (  )  ; 
iter  .  remove  (  )  ; 
}  finally  { 
fileHandle  .  release  (  )  ; 
} 
} 
fileMap  .  clear  (  )  ; 
fileList  .  clear  (  )  ; 
} 

private   Set   getCacheKeys  (  )  { 
return   fileMap  .  keySet  (  )  ; 
} 
} 











































class   LogEndFileDescriptor  { 

private   RandomAccessFile   endOfLogRWFile  =  null  ; 

private   RandomAccessFile   endOfLogSyncFile  =  null  ; 

private   Object   fsyncFileSynchronizer  =  new   Object  (  )  ; 




RandomAccessFile   getWritableFile  (  long   fileNumber  )  throws   RunRecoveryException  { 
try  { 
if  (  endOfLogRWFile  ==  null  )  { 
endOfLogRWFile  =  makeFileHandle  (  fileNumber  ,  FileMode  .  READWRITE_MODE  )  .  getFile  (  )  ; 
synchronized  (  fsyncFileSynchronizer  )  { 
endOfLogSyncFile  =  makeFileHandle  (  fileNumber  ,  FileMode  .  READWRITE_MODE  )  .  getFile  (  )  ; 
} 
} 
return   endOfLogRWFile  ; 
}  catch  (  Exception   e  )  { 
throw   new   RunRecoveryException  (  envImpl  ,  e  )  ; 
} 
} 




void   force  (  )  throws   DatabaseException  ,  IOException  { 
synchronized  (  fsyncFileSynchronizer  )  { 
RandomAccessFile   file  =  endOfLogSyncFile  ; 
if  (  file  !=  null  )  { 
FileChannel   channel  =  file  .  getChannel  (  )  ; 
try  { 
channel  .  force  (  false  )  ; 
}  catch  (  ClosedChannelException   e  )  { 
throw   new   RunRecoveryException  (  envImpl  ,  "Channel closed, may be due to thread interrupt"  ,  e  )  ; 
} 
assert   EnvironmentImpl  .  maybeForceYield  (  )  ; 
} 
} 
} 





void   close  (  )  throws   IOException  { 
IOException   firstException  =  null  ; 
if  (  endOfLogRWFile  !=  null  )  { 
RandomAccessFile   file  =  endOfLogRWFile  ; 
endOfLogRWFile  =  null  ; 
try  { 
file  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
firstException  =  e  ; 
} 
} 
synchronized  (  fsyncFileSynchronizer  )  { 
if  (  endOfLogSyncFile  !=  null  )  { 
RandomAccessFile   file  =  endOfLogSyncFile  ; 
endOfLogSyncFile  =  null  ; 
file  .  close  (  )  ; 
} 
if  (  firstException  !=  null  )  { 
throw   firstException  ; 
} 
} 
} 
} 

static   boolean   RUNRECOVERY_EXCEPTION_TESTING  =  false  ; 

private   static   final   int   RUNRECOVERY_EXCEPTION_MAX  =  100  ; 

private   int   runRecoveryExceptionCounter  =  0  ; 

private   boolean   runRecoveryExceptionThrown  =  false  ; 

private   Random   runRecoveryExceptionRandom  =  null  ; 

private   void   generateRunRecoveryException  (  RandomAccessFile   file  ,  ByteBuffer   data  ,  long   destOffset  )  throws   DatabaseException  ,  IOException  { 
if  (  runRecoveryExceptionThrown  )  { 
try  { 
throw   new   Exception  (  "Write after RunRecoveryException"  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
runRecoveryExceptionCounter  +=  1  ; 
if  (  runRecoveryExceptionCounter  >=  RUNRECOVERY_EXCEPTION_MAX  )  { 
runRecoveryExceptionCounter  =  0  ; 
} 
if  (  runRecoveryExceptionRandom  ==  null  )  { 
runRecoveryExceptionRandom  =  new   Random  (  System  .  currentTimeMillis  (  )  )  ; 
} 
if  (  runRecoveryExceptionCounter  ==  runRecoveryExceptionRandom  .  nextInt  (  RUNRECOVERY_EXCEPTION_MAX  )  )  { 
int   len  =  runRecoveryExceptionRandom  .  nextInt  (  data  .  remaining  (  )  )  ; 
if  (  len  >  0  )  { 
byte  [  ]  a  =  new   byte  [  len  ]  ; 
data  .  get  (  a  ,  0  ,  len  )  ; 
ByteBuffer   buf  =  ByteBuffer  .  wrap  (  a  )  ; 
writeToFile  (  file  ,  buf  ,  destOffset  )  ; 
} 
runRecoveryExceptionThrown  =  true  ; 
throw   new   RunRecoveryException  (  envImpl  ,  "Randomly generated for testing"  )  ; 
} 
} 
} 

