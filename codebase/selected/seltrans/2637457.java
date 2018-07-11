package   org  .  apache  .  zookeeper  .  server  .  persistence  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  EOFException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilterInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  concurrent  .  TimeUnit  ; 
import   java  .  util  .  zip  .  Adler32  ; 
import   java  .  util  .  zip  .  Checksum  ; 
import   org  .  apache  .  jute  .  BinaryInputArchive  ; 
import   org  .  apache  .  jute  .  BinaryOutputArchive  ; 
import   org  .  apache  .  jute  .  InputArchive  ; 
import   org  .  apache  .  jute  .  OutputArchive  ; 
import   org  .  apache  .  jute  .  Record  ; 
import   org  .  slf4j  .  Logger  ; 
import   org  .  slf4j  .  LoggerFactory  ; 
import   org  .  apache  .  zookeeper  .  server  .  util  .  SerializeUtils  ; 
import   org  .  apache  .  zookeeper  .  txn  .  TxnHeader  ; 











































public   class   FileTxnLog   implements   TxnLog  { 

private   static   final   Logger   LOG  ; 

static   long   preAllocSize  =  65536  *  1024  ; 

public   static   final   int   TXNLOG_MAGIC  =  ByteBuffer  .  wrap  (  "ZKLG"  .  getBytes  (  )  )  .  getInt  (  )  ; 

public   static   final   int   VERSION  =  2  ; 


private   static   final   long   fsyncWarningThresholdMS  ; 

static  { 
LOG  =  LoggerFactory  .  getLogger  (  FileTxnLog  .  class  )  ; 
String   size  =  System  .  getProperty  (  "zookeeper.preAllocSize"  )  ; 
if  (  size  !=  null  )  { 
try  { 
preAllocSize  =  Long  .  parseLong  (  size  )  *  1024  ; 
}  catch  (  NumberFormatException   e  )  { 
LOG  .  warn  (  size  +  " is not a valid value for preAllocSize"  )  ; 
} 
} 
fsyncWarningThresholdMS  =  Long  .  getLong  (  "fsync.warningthresholdms"  ,  1000  )  ; 
} 

long   lastZxidSeen  ; 

volatile   BufferedOutputStream   logStream  =  null  ; 

volatile   OutputArchive   oa  ; 

volatile   FileOutputStream   fos  =  null  ; 

File   logDir  ; 

private   final   boolean   forceSync  =  !  System  .  getProperty  (  "zookeeper.forceSync"  ,  "yes"  )  .  equals  (  "no"  )  ; 

; 

long   dbId  ; 

private   LinkedList  <  FileOutputStream  >  streamsToFlush  =  new   LinkedList  <  FileOutputStream  >  (  )  ; 

long   currentSize  ; 

File   logFileWrite  =  null  ; 






public   FileTxnLog  (  File   logDir  )  { 
this  .  logDir  =  logDir  ; 
} 






public   static   void   setPreallocSize  (  long   size  )  { 
preAllocSize  =  size  ; 
} 





protected   Checksum   makeChecksumAlgorithm  (  )  { 
return   new   Adler32  (  )  ; 
} 





public   synchronized   void   rollLog  (  )  throws   IOException  { 
if  (  logStream  !=  null  )  { 
this  .  logStream  .  flush  (  )  ; 
this  .  logStream  =  null  ; 
oa  =  null  ; 
} 
} 





public   synchronized   void   close  (  )  throws   IOException  { 
if  (  logStream  !=  null  )  { 
logStream  .  close  (  )  ; 
} 
for  (  FileOutputStream   log  :  streamsToFlush  )  { 
log  .  close  (  )  ; 
} 
} 







public   synchronized   boolean   append  (  TxnHeader   hdr  ,  Record   txn  )  throws   IOException  { 
if  (  hdr  !=  null  )  { 
if  (  hdr  .  getZxid  (  )  <=  lastZxidSeen  )  { 
LOG  .  warn  (  "Current zxid "  +  hdr  .  getZxid  (  )  +  " is <= "  +  lastZxidSeen  +  " for "  +  hdr  .  getType  (  )  )  ; 
} 
if  (  logStream  ==  null  )  { 
if  (  LOG  .  isInfoEnabled  (  )  )  { 
LOG  .  info  (  "Creating new log file: log."  +  Long  .  toHexString  (  hdr  .  getZxid  (  )  )  )  ; 
} 
logFileWrite  =  new   File  (  logDir  ,  (  "log."  +  Long  .  toHexString  (  hdr  .  getZxid  (  )  )  )  )  ; 
fos  =  new   FileOutputStream  (  logFileWrite  )  ; 
logStream  =  new   BufferedOutputStream  (  fos  )  ; 
oa  =  BinaryOutputArchive  .  getArchive  (  logStream  )  ; 
FileHeader   fhdr  =  new   FileHeader  (  TXNLOG_MAGIC  ,  VERSION  ,  dbId  )  ; 
fhdr  .  serialize  (  oa  ,  "fileheader"  )  ; 
logStream  .  flush  (  )  ; 
currentSize  =  fos  .  getChannel  (  )  .  position  (  )  ; 
streamsToFlush  .  add  (  fos  )  ; 
} 
padFile  (  fos  )  ; 
byte  [  ]  buf  =  Util  .  marshallTxnEntry  (  hdr  ,  txn  )  ; 
if  (  buf  ==  null  ||  buf  .  length  ==  0  )  { 
throw   new   IOException  (  "Faulty serialization for header "  +  "and txn"  )  ; 
} 
Checksum   crc  =  makeChecksumAlgorithm  (  )  ; 
crc  .  update  (  buf  ,  0  ,  buf  .  length  )  ; 
oa  .  writeLong  (  crc  .  getValue  (  )  ,  "txnEntryCRC"  )  ; 
Util  .  writeTxnBytes  (  oa  ,  buf  )  ; 
return   true  ; 
} 
return   false  ; 
} 






private   void   padFile  (  FileOutputStream   out  )  throws   IOException  { 
currentSize  =  Util  .  padLogFile  (  out  ,  currentSize  ,  preAllocSize  )  ; 
} 









public   static   File  [  ]  getLogFiles  (  File  [  ]  logDirList  ,  long   snapshotZxid  )  { 
List  <  File  >  files  =  Util  .  sortDataDir  (  logDirList  ,  "log"  ,  true  )  ; 
long   logZxid  =  0  ; 
for  (  File   f  :  files  )  { 
long   fzxid  =  Util  .  getZxidFromName  (  f  .  getName  (  )  ,  "log"  )  ; 
if  (  fzxid  >  snapshotZxid  )  { 
continue  ; 
} 
if  (  fzxid  >  logZxid  )  { 
logZxid  =  fzxid  ; 
} 
} 
List  <  File  >  v  =  new   ArrayList  <  File  >  (  5  )  ; 
for  (  File   f  :  files  )  { 
long   fzxid  =  Util  .  getZxidFromName  (  f  .  getName  (  )  ,  "log"  )  ; 
if  (  fzxid  <  logZxid  )  { 
continue  ; 
} 
v  .  add  (  f  )  ; 
} 
return   v  .  toArray  (  new   File  [  0  ]  )  ; 
} 





public   long   getLastLoggedZxid  (  )  { 
File  [  ]  files  =  getLogFiles  (  logDir  .  listFiles  (  )  ,  0  )  ; 
long   maxLog  =  files  .  length  >  0  ?  Util  .  getZxidFromName  (  files  [  files  .  length  -  1  ]  .  getName  (  )  ,  "log"  )  :  -  1  ; 
long   zxid  =  maxLog  ; 
try  { 
FileTxnLog   txn  =  new   FileTxnLog  (  logDir  )  ; 
TxnIterator   itr  =  txn  .  read  (  maxLog  )  ; 
while  (  true  )  { 
if  (  !  itr  .  next  (  )  )  break  ; 
TxnHeader   hdr  =  itr  .  getHeader  (  )  ; 
zxid  =  hdr  .  getZxid  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
LOG  .  warn  (  "Unexpected exception"  ,  e  )  ; 
} 
return   zxid  ; 
} 





public   synchronized   void   commit  (  )  throws   IOException  { 
if  (  logStream  !=  null  )  { 
logStream  .  flush  (  )  ; 
} 
for  (  FileOutputStream   log  :  streamsToFlush  )  { 
log  .  flush  (  )  ; 
if  (  forceSync  )  { 
long   startSyncNS  =  System  .  nanoTime  (  )  ; 
log  .  getChannel  (  )  .  force  (  false  )  ; 
long   syncElapsedMS  =  TimeUnit  .  NANOSECONDS  .  toMillis  (  System  .  nanoTime  (  )  -  startSyncNS  )  ; 
if  (  syncElapsedMS  >  fsyncWarningThresholdMS  )  { 
LOG  .  warn  (  "fsync-ing the write ahead log in "  +  Thread  .  currentThread  (  )  .  getName  (  )  +  " took "  +  syncElapsedMS  +  "ms which will adversely effect operation latency. "  +  "See the ZooKeeper troubleshooting guide"  )  ; 
} 
} 
} 
while  (  streamsToFlush  .  size  (  )  >  1  )  { 
streamsToFlush  .  removeFirst  (  )  .  close  (  )  ; 
} 
} 







public   TxnIterator   read  (  long   zxid  )  throws   IOException  { 
return   new   FileTxnIterator  (  logDir  ,  zxid  )  ; 
} 






public   boolean   truncate  (  long   zxid  )  throws   IOException  { 
FileTxnIterator   itr  =  new   FileTxnIterator  (  this  .  logDir  ,  zxid  )  ; 
PositionInputStream   input  =  itr  .  inputStream  ; 
long   pos  =  input  .  getPosition  (  )  ; 
RandomAccessFile   raf  =  new   RandomAccessFile  (  itr  .  logFile  ,  "rw"  )  ; 
raf  .  setLength  (  pos  )  ; 
raf  .  close  (  )  ; 
while  (  itr  .  goToNextLog  (  )  )  { 
if  (  !  itr  .  logFile  .  delete  (  )  )  { 
LOG  .  warn  (  "Unable to truncate "  +  itr  .  logFile  )  ; 
} 
} 
return   true  ; 
} 







private   static   FileHeader   readHeader  (  File   file  )  throws   IOException  { 
InputStream   is  =  null  ; 
try  { 
is  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
InputArchive   ia  =  BinaryInputArchive  .  getArchive  (  is  )  ; 
FileHeader   hdr  =  new   FileHeader  (  )  ; 
hdr  .  deserialize  (  ia  ,  "fileheader"  )  ; 
return   hdr  ; 
}  finally  { 
try  { 
if  (  is  !=  null  )  is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
LOG  .  warn  (  "Ignoring exception during close"  ,  e  )  ; 
} 
} 
} 





public   long   getDbId  (  )  throws   IOException  { 
FileTxnIterator   itr  =  new   FileTxnIterator  (  logDir  ,  0  )  ; 
FileHeader   fh  =  readHeader  (  itr  .  logFile  )  ; 
itr  .  close  (  )  ; 
if  (  fh  ==  null  )  throw   new   IOException  (  "Unsupported Format."  )  ; 
return   fh  .  getDbid  (  )  ; 
} 





public   boolean   isForceSync  (  )  { 
return   forceSync  ; 
} 








static   class   PositionInputStream   extends   FilterInputStream  { 

long   position  ; 

protected   PositionInputStream  (  InputStream   in  )  { 
super  (  in  )  ; 
position  =  0  ; 
} 

@  Override 
public   int   read  (  )  throws   IOException  { 
int   rc  =  super  .  read  (  )  ; 
if  (  rc  >  -  1  )  { 
position  ++  ; 
} 
return   rc  ; 
} 

public   int   read  (  byte  [  ]  b  )  throws   IOException  { 
int   rc  =  super  .  read  (  b  )  ; 
if  (  rc  >  0  )  { 
position  +=  rc  ; 
} 
return   rc  ; 
} 

@  Override 
public   int   read  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
int   rc  =  super  .  read  (  b  ,  off  ,  len  )  ; 
if  (  rc  >  0  )  { 
position  +=  rc  ; 
} 
return   rc  ; 
} 

@  Override 
public   long   skip  (  long   n  )  throws   IOException  { 
long   rc  =  super  .  skip  (  n  )  ; 
if  (  rc  >  0  )  { 
position  +=  rc  ; 
} 
return   rc  ; 
} 

public   long   getPosition  (  )  { 
return   position  ; 
} 

@  Override 
public   boolean   markSupported  (  )  { 
return   false  ; 
} 

@  Override 
public   void   mark  (  int   readLimit  )  { 
throw   new   UnsupportedOperationException  (  "mark"  )  ; 
} 

@  Override 
public   void   reset  (  )  { 
throw   new   UnsupportedOperationException  (  "reset"  )  ; 
} 
} 





public   static   class   FileTxnIterator   implements   TxnLog  .  TxnIterator  { 

File   logDir  ; 

long   zxid  ; 

TxnHeader   hdr  ; 

Record   record  ; 

File   logFile  ; 

InputArchive   ia  ; 

static   final   String   CRC_ERROR  =  "CRC check failed"  ; 

PositionInputStream   inputStream  =  null  ; 

private   ArrayList  <  File  >  storedFiles  ; 







public   FileTxnIterator  (  File   logDir  ,  long   zxid  )  throws   IOException  { 
this  .  logDir  =  logDir  ; 
this  .  zxid  =  zxid  ; 
init  (  )  ; 
} 






void   init  (  )  throws   IOException  { 
storedFiles  =  new   ArrayList  <  File  >  (  )  ; 
List  <  File  >  files  =  Util  .  sortDataDir  (  FileTxnLog  .  getLogFiles  (  logDir  .  listFiles  (  )  ,  0  )  ,  "log"  ,  false  )  ; 
for  (  File   f  :  files  )  { 
if  (  Util  .  getZxidFromName  (  f  .  getName  (  )  ,  "log"  )  >=  zxid  )  { 
storedFiles  .  add  (  f  )  ; 
}  else   if  (  Util  .  getZxidFromName  (  f  .  getName  (  )  ,  "log"  )  <  zxid  )  { 
storedFiles  .  add  (  f  )  ; 
break  ; 
} 
} 
goToNextLog  (  )  ; 
if  (  !  next  (  )  )  return  ; 
while  (  hdr  .  getZxid  (  )  <  zxid  )  { 
if  (  !  next  (  )  )  return  ; 
} 
} 







private   boolean   goToNextLog  (  )  throws   IOException  { 
if  (  storedFiles  .  size  (  )  >  0  )  { 
this  .  logFile  =  storedFiles  .  remove  (  storedFiles  .  size  (  )  -  1  )  ; 
ia  =  createInputArchive  (  this  .  logFile  )  ; 
return   true  ; 
} 
return   false  ; 
} 







protected   void   inStreamCreated  (  InputArchive   ia  ,  InputStream   is  )  throws   IOException  { 
FileHeader   header  =  new   FileHeader  (  )  ; 
header  .  deserialize  (  ia  ,  "fileheader"  )  ; 
if  (  header  .  getMagic  (  )  !=  FileTxnLog  .  TXNLOG_MAGIC  )  { 
throw   new   IOException  (  "Transaction log: "  +  this  .  logFile  +  " has invalid magic number "  +  header  .  getMagic  (  )  +  " != "  +  FileTxnLog  .  TXNLOG_MAGIC  )  ; 
} 
} 







protected   InputArchive   createInputArchive  (  File   logFile  )  throws   IOException  { 
if  (  inputStream  ==  null  )  { 
inputStream  =  new   PositionInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  logFile  )  )  )  ; 
LOG  .  debug  (  "Created new input stream "  +  logFile  )  ; 
ia  =  BinaryInputArchive  .  getArchive  (  inputStream  )  ; 
inStreamCreated  (  ia  ,  inputStream  )  ; 
LOG  .  debug  (  "Created new input archive "  +  logFile  )  ; 
} 
return   ia  ; 
} 





protected   Checksum   makeChecksumAlgorithm  (  )  { 
return   new   Adler32  (  )  ; 
} 






public   boolean   next  (  )  throws   IOException  { 
if  (  ia  ==  null  )  { 
return   false  ; 
} 
try  { 
long   crcValue  =  ia  .  readLong  (  "crcvalue"  )  ; 
byte  [  ]  bytes  =  Util  .  readTxnBytes  (  ia  )  ; 
if  (  bytes  ==  null  ||  bytes  .  length  ==  0  )  throw   new   EOFException  (  "Failed to read"  )  ; 
Checksum   crc  =  makeChecksumAlgorithm  (  )  ; 
crc  .  update  (  bytes  ,  0  ,  bytes  .  length  )  ; 
if  (  crcValue  !=  crc  .  getValue  (  )  )  throw   new   IOException  (  CRC_ERROR  )  ; 
if  (  bytes  ==  null  ||  bytes  .  length  ==  0  )  return   false  ; 
hdr  =  new   TxnHeader  (  )  ; 
record  =  SerializeUtils  .  deserializeTxn  (  bytes  ,  hdr  )  ; 
}  catch  (  EOFException   e  )  { 
LOG  .  debug  (  "EOF excepton "  +  e  )  ; 
inputStream  .  close  (  )  ; 
inputStream  =  null  ; 
ia  =  null  ; 
hdr  =  null  ; 
if  (  !  goToNextLog  (  )  )  { 
return   false  ; 
} 
return   next  (  )  ; 
} 
return   true  ; 
} 






public   TxnHeader   getHeader  (  )  { 
return   hdr  ; 
} 






public   Record   getTxn  (  )  { 
return   record  ; 
} 





public   void   close  (  )  throws   IOException  { 
inputStream  .  close  (  )  ; 
} 
} 
} 

