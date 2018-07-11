package   gloodb  .  file  .  storage  .  block  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  EOFException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Iterator  ; 
import   javax  .  crypto  .  BadPaddingException  ; 
import   javax  .  crypto  .  IllegalBlockSizeException  ; 
import   gloodb  .  GlooException  ; 
import   gloodb  .  EncryptionException  ; 
import   gloodb  .  storage  .  Address  ; 
import   gloodb  .  storage  .  Storage  ; 
import   gloodb  .  storage  .  StorageFullException  ; 
import   gloodb  .  storage  .  StorageProxy  ; 
import   gloodb  .  utils  .  FileUtils  ; 
import   gloodb  .  utils  .  SafeReentrantReadWriteLock  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  math  .  BigInteger  ; 



















class   BlockStorage   implements   Storage  { 

private   static   final   int   delta  =  1024  *  8  ; 

private   final   BlockStorageConfiguration   configuration  ; 

private   RandomAccessFile   file  ; 

private   File   fileObj  ; 

private   byte  [  ]  allocationTable  ; 

private   final   SafeReentrantReadWriteLock   lock  ; 





public   BlockStorage  (  BlockStorageConfiguration   configuration  )  { 
this  .  configuration  =  configuration  ; 
this  .  lock  =  new   SafeReentrantReadWriteLock  (  )  ; 
try  { 
File   namespaceDir  =  new   File  (  configuration  .  getNameSpace  (  )  )  ; 
if  (  !  namespaceDir  .  isDirectory  (  )  )  { 
namespaceDir  .  mkdirs  (  )  ; 
} 
createStorageFile  (  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   GlooException  (  e  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
} 
} 

private   void   createStorageFile  (  )  throws   IOException  { 
this  .  fileObj  =  new   File  (  configuration  .  getNameSpace  (  )  +  "/"  +  configuration  .  getRepositoryName  (  )  +  configuration  .  getRepositoryExtension  (  )  )  ; 
if  (  !  this  .  fileObj  .  exists  (  )  )  { 
fileObj  .  createNewFile  (  )  ; 
} 
this  .  file  =  new   RandomAccessFile  (  fileObj  ,  "rws"  )  ; 
createStorageAllocationTable  (  )  ; 
} 








@  SuppressWarnings  (  "unchecked"  ) 
public  <  P   extends   Serializable  >  P   restore  (  Class  <  P  >  clazz  ,  Address   uncheckedAddress  )  { 
try  { 
lock  .  readLock  (  )  ; 
BlockAddress   address  =  (  BlockAddress  )  uncheckedAddress  ; 
if  (  address  .  size  (  )  ==  0  )  { 
return   null  ; 
} 
P   result  =  null  ; 
try  { 
byte  [  ]  buf  =  new   byte  [  address  .  getWrittenSize  (  )  ]  ; 
int   blockNo  =  0  ; 
for  (  Iterator  <  Integer  >  iter  =  address  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
readBlock  (  buf  ,  blockNo  ,  iter  .  next  (  )  .  intValue  (  )  )  ; 
blockNo  ++  ; 
} 
buf  =  decrypt  (  buf  )  ; 
ByteArrayInputStream   bis  =  new   ByteArrayInputStream  (  buf  )  ; 
ObjectInputStream   ois  =  new   ObjectInputStream  (  bis  )  ; 
result  =  (  P  )  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
bis  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   GlooException  (  e  )  ; 
} 
return   result  ; 
}  finally  { 
lock  .  readUnlock  (  )  ; 
} 
} 





public   void   remove  (  Address   uncheckedAddress  )  { 
try  { 
lock  .  writeLock  (  )  ; 
BlockAddress   address  =  (  BlockAddress  )  uncheckedAddress  ; 
for  (  Iterator  <  Integer  >  iter  =  address  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
try  { 
int   blockOffset  =  iter  .  next  (  )  .  intValue  (  )  ; 
file  .  seek  (  blockOffset  )  ; 
file  .  writeByte  (  0  )  ; 
int   block  =  blockOffset  /  configuration  .  getBlockSize  (  )  ; 
synchronized  (  allocationTable  )  { 
allocationTable  [  block  /  8  ]  &=  ~  (  0x1  <<  (  block  %  8  )  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
} 
} 
address  .  clear  (  )  ; 
}  finally  { 
lock  .  writeUnlock  (  )  ; 
} 
} 

private   void   createStorageAllocationTable  (  )  throws   IOException  { 
int   blockNo  =  (  int  )  (  this  .  file  .  length  (  )  /  8  /  configuration  .  getBlockSize  (  )  )  ; 
int   size  =  blockNo  /  delta  ; 
if  (  size  ==  0  )  { 
size  ++  ; 
} 
if  (  blockNo  %  delta  ==  0  )  { 
size  ++  ; 
} 
size  *=  delta  ; 
allocationTable  =  new   byte  [  size  ]  ; 
Arrays  .  fill  (  allocationTable  ,  (  byte  )  0  )  ; 
for  (  int   i  =  0  ;  i  <  8  *  allocationTable  .  length  ;  i  ++  )  { 
try  { 
file  .  seek  (  i  *  configuration  .  getBlockSize  (  )  )  ; 
if  (  file  .  readByte  (  )  !=  0  )  { 
allocationTable  [  i  /  8  ]  |=  0x1  <<  (  i  %  8  )  ; 
} 
}  catch  (  EOFException   e  )  { 
return  ; 
} 
} 
} 

private   void   adjustAddress  (  int   blockNo  ,  BlockAddress   address  )  { 
int   size  =  address  .  size  (  )  ; 
if  (  size  ==  blockNo  )  { 
return  ; 
}  else   if  (  size  <  blockNo  )  { 
int   searchFrom  =  0  ; 
int   block  ; 
for  (  int   i  =  size  ;  i  <  blockNo  ;  i  ++  )  { 
block  =  getFreeBlock  (  searchFrom  )  ; 
address  .  addItem  (  block  *  configuration  .  getBlockSize  (  )  )  ; 
searchFrom  =  block  +  1  ; 
} 
}  else  { 
remove  (  address  .  clear  (  blockNo  )  )  ; 
} 
} 

private   int   getFreeBlock  (  int   start  )  { 
synchronized  (  allocationTable  )  { 
for  (  int   i  =  start  ;  i  <  8  *  allocationTable  .  length  ;  i  ++  )  { 
if  (  (  allocationTable  [  i  /  8  ]  &  (  0x1  <<  (  i  %  8  )  )  )  ==  0  )  { 
allocationTable  [  i  /  8  ]  |=  0x1  <<  (  i  %  8  )  ; 
return   i  ; 
} 
} 
if  (  allocationTable  .  length  >=  configuration  .  getMaxBlocks  (  )  /  8  )  { 
throw   new   StorageFullException  (  )  ; 
} 
byte  [  ]  temp  =  allocationTable  ; 
allocationTable  =  new   byte  [  temp  .  length  +  delta  ]  ; 
System  .  arraycopy  (  temp  ,  0  ,  allocationTable  ,  0  ,  temp  .  length  )  ; 
Arrays  .  fill  (  allocationTable  ,  temp  .  length  ,  allocationTable  .  length  -  1  ,  (  byte  )  0  )  ; 
return   temp  .  length  *  8  ; 
} 
} 

private   int   writeBlock  (  byte  [  ]  buf  ,  int   blockNo  ,  int   pos  )  throws   IOException  { 
int   writableBlockSize  =  configuration  .  getWritableBlockSize  (  )  ; 
int   toWrite  =  buf  .  length  -  blockNo  *  writableBlockSize  ; 
int   size  =  toWrite  <  writableBlockSize  ?  toWrite  :  writableBlockSize  ; 
file  .  seek  (  pos  )  ; 
file  .  writeByte  (  1  )  ; 
file  .  seek  (  pos  +  1  )  ; 
file  .  write  (  buf  ,  blockNo  *  writableBlockSize  ,  size  )  ; 
return   pos  ; 
} 

private   void   readBlock  (  byte  [  ]  buf  ,  int   blockNo  ,  int   pos  )  { 
int   writableBlockSize  =  configuration  .  getWritableBlockSize  (  )  ; 
int   toRead  =  buf  .  length  -  blockNo  *  writableBlockSize  ; 
toRead  =  toRead  >  writableBlockSize  ?  writableBlockSize  :  toRead  ; 
try  { 
synchronized  (  file  )  { 
file  .  seek  (  pos  +  1  )  ; 
file  .  read  (  buf  ,  blockNo  *  writableBlockSize  ,  toRead  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
} 
} 





public   void   takeSnapshot  (  BigInteger   version  )  { 
try  { 
lock  .  writeLock  (  )  ; 
File   snapshot  =  getSnapshotFile  (  version  )  ; 
FileUtils  .  copyFile  (  fileObj  ,  snapshot  )  ; 
}  catch  (  FileNotFoundException   ffe  )  { 
throw   new   GlooException  (  ffe  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
}  finally  { 
lock  .  writeUnlock  (  )  ; 
} 
} 





public   void   restoreSnapshot  (  BigInteger   version  )  { 
try  { 
lock  .  writeLock  (  )  ; 
File   snapshot  =  getSnapshotFile  (  version  )  ; 
if  (  !  snapshot  .  exists  (  )  )  { 
file  .  close  (  )  ; 
if  (  !  fileObj  .  delete  (  )  )  throw   new   GlooException  (  String  .  format  (  "Cannot delete old %s file. Please remove manually and restart."  ,  fileObj  .  getName  (  )  )  )  ; 
createStorageFile  (  )  ; 
return  ; 
} 
FileUtils  .  copyFile  (  snapshot  ,  fileObj  )  ; 
}  catch  (  FileNotFoundException   ffe  )  { 
throw   new   GlooException  (  ffe  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
}  finally  { 
lock  .  writeUnlock  (  )  ; 
} 
} 







public   void   store  (  Serializable   persistentObject  ,  Address   uncheckedAddress  )  { 
try  { 
lock  .  writeLock  (  )  ; 
int   writableBlockSize  =  configuration  .  getWritableBlockSize  (  )  ; 
BlockAddress   address  =  (  BlockAddress  )  uncheckedAddress  ; 
if  (  persistentObject  ==  null  )  { 
return  ; 
} 
try  { 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  bos  )  ; 
out  .  writeObject  (  persistentObject  )  ; 
out  .  close  (  )  ; 
byte  [  ]  buf  =  bos  .  toByteArray  (  )  ; 
buf  =  encrypt  (  buf  )  ; 
address  .  setWrittenSize  (  buf  .  length  )  ; 
int   blockNo  =  buf  .  length  /  writableBlockSize  ; 
if  (  buf  .  length  %  writableBlockSize  !=  0  )  { 
blockNo  ++  ; 
} 
adjustAddress  (  blockNo  ,  address  )  ; 
for  (  int   i  =  0  ;  i  <  blockNo  ;  i  ++  )  { 
writeBlock  (  buf  ,  i  ,  address  .  getItem  (  i  )  )  ; 
} 
if  (  this  .  configuration  .  isVerificationOn  (  )  )  verify  (  buf  ,  persistentObject  ,  address  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  e  )  ; 
} 
}  finally  { 
lock  .  writeUnlock  (  )  ; 
} 
} 

private   void   verify  (  byte  [  ]  expected  ,  Serializable   writtenObject  ,  BlockAddress   address  )  { 
try  { 
Serializable   persistentObject  =  restore  (  writtenObject  .  getClass  (  )  ,  address  )  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  bos  )  ; 
out  .  writeObject  (  persistentObject  )  ; 
out  .  close  (  )  ; 
}  catch  (  GlooException   e  )  { 
throw   e  ; 
}  catch  (  Exception   e  )  { 
throw   new   GlooException  (  "Object store verification failed."  ,  e  )  ; 
} 
} 

private   byte  [  ]  encrypt  (  byte  [  ]  buf  )  { 
try  { 
return   configuration  .  getOutputCipher  (  )  .  doFinal  (  buf  ,  0  ,  buf  .  length  )  ; 
}  catch  (  IllegalBlockSizeException   e  )  { 
throw   new   EncryptionException  (  e  )  ; 
}  catch  (  BadPaddingException   e  )  { 
throw   new   EncryptionException  (  e  )  ; 
} 
} 

private   byte  [  ]  decrypt  (  byte  [  ]  buf  )  { 
try  { 
return   configuration  .  getInputCipher  (  )  .  doFinal  (  buf  ,  0  ,  buf  .  length  )  ; 
}  catch  (  IllegalBlockSizeException   e  )  { 
throw   new   EncryptionException  (  e  )  ; 
}  catch  (  BadPaddingException   e  )  { 
throw   new   EncryptionException  (  e  )  ; 
} 
} 

private   File   getSnapshotFile  (  BigInteger   version  )  { 
return   new   File  (  configuration  .  getNameSpace  (  )  +  "/"  +  String  .  format  (  configuration  .  getSnapshotName  (  )  ,  version  )  +  configuration  .  getRepositoryExtension  (  )  )  ; 
} 





public   Address   buildAddress  (  )  { 
return   new   BlockAddress  (  )  ; 
} 

@  Override 
public   void   start  (  BigInteger   startVersion  )  { 
this  .  restoreSnapshot  (  startVersion  )  ; 
try  { 
this  .  createStorageAllocationTable  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   GlooException  (  "Cannot create storage allocation table on restart."  )  ; 
} 
} 

@  Override 
public   StorageProxy   buildStorageProxy  (  Address   address  )  { 
return   new   BlockStorageProxy  (  address  )  ; 
} 

@  Override 
public   void   begin  (  long   txId  )  { 
} 

@  Override 
public   void   commit  (  long   txId  )  { 
} 

@  Override 
public   void   rollback  (  long   txId  )  { 
} 
} 

