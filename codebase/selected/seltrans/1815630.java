package   jaxlib  .  jdbc  .  tds  ; 

import   java  .  io  .  Closeable  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  sql  .  SQLDataException  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  sql  .  SQLNonTransientException  ; 
import   java  .  util  .  Arrays  ; 
import   jaxlib  .  io  .  IO  ; 
import   jaxlib  .  io  .  stream  .  IOStreams  ; 
import   jaxlib  .  io  .  stream  .  embedded  .  FixedLengthInputStream  ; 
import   jaxlib  .  lang  .  Bytes  ; 





































final   class   BlobBuffer   extends   Object  { 




private   static   final   int   PAGE_SIZE  =  1024  ; 




private   static   final   int   PAGE_MASK  =  0xFFFFFC00  ; 




private   static   final   int   BYTE_MASK  =  0x000003FF  ; 




private   static   final   int   MAX_BUF_INC  =  16384  ; 




private   static   final   int   INVALID_PAGE  =  -  1  ; 




private   byte  [  ]  buffer  ; 




private   int   length  ; 




private   int   currentPage  ; 




private   boolean   bufferDirty  ; 




private   int   openCount  ; 




private   boolean   isMemOnly  ; 




private   final   int   maxMemSize  ; 

private   final   BlobBuffer  .  FileHolder   fileHolder  ; 






BlobBuffer  (  final   TdsConnection   connection  )  throws   SQLException  { 
super  (  )  ; 
this  .  fileHolder  =  new   BlobBuffer  .  FileHolder  (  connection  .  getCloser  (  )  )  ; 
this  .  maxMemSize  =  (  int  )  connection  .  lobBuffer  ; 
this  .  buffer  =  Bytes  .  EMPTY_ARRAY  ; 
} 








private   void   createBlobFileIfNecessary  (  )  throws   SQLException  { 
synchronized  (  this  .  fileHolder  )  { 
ensureOpen  (  )  ; 
if  (  !  this  .  isMemOnly  &&  (  this  .  fileHolder  .  file  ==  null  )  )  { 
try  { 
this  .  fileHolder  .  closer  .  purge  (  )  ; 
if  (  this  .  fileHolder  .  path  ==  null  )  { 
this  .  fileHolder  .  path  =  File  .  createTempFile  (  "tdsblob"  ,  ".tmp"  )  ; 
this  .  fileHolder  .  closer  .  info  .  incLobFilesCreated  (  )  ; 
} 
this  .  fileHolder  .  file  =  new   RandomAccessFile  (  this  .  fileHolder  .  path  ,  "rw"  )  ; 
if  (  this  .  length  >  0  )  this  .  fileHolder  .  file  .  write  (  this  .  buffer  ,  0  ,  this  .  length  )  ; 
this  .  buffer  =  new   byte  [  PAGE_SIZE  ]  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
this  .  openCount  =  0  ; 
}  catch  (  final   SecurityException   ex  )  { 
this  .  fileHolder  .  tryClose  (  )  ; 
this  .  isMemOnly  =  true  ; 
}  catch  (  final   IOException   ex  )  { 
this  .  fileHolder  .  tryClose  (  )  ; 
this  .  isMemOnly  =  true  ; 
} 
if  (  this  .  fileHolder  .  close  ==  null  )  this  .  fileHolder  .  close  =  this  .  fileHolder  .  closer  .  add  (  this  ,  this  .  fileHolder  )  ; 
} 
} 
} 

private   void   ensureOpen  (  )  throws   SQLException  { 
if  (  this  .  fileHolder  .  closed  )  throw   new   SQLNonTransientException  (  Messages  .  get  (  "error.generic.closed"  ,  "Blob"  )  ,  "57014"  )  ; 
} 

final   void   close  (  )  throws   IOException  { 
this  .  fileHolder  .  closed  =  true  ; 
this  .  buffer  =  Bytes  .  EMPTY_ARRAY  ; 
this  .  fileHolder  .  close  (  )  ; 
} 










final   void   closeOne  (  )  throws   IOException  { 
synchronized  (  this  .  fileHolder  )  { 
if  (  (  this  .  openCount  >  0  )  &&  (  --  this  .  openCount  ==  0  )  &&  (  this  .  fileHolder  .  file  !=  null  )  &&  !  this  .  fileHolder  .  closed  )  { 
if  (  this  .  bufferDirty  )  writePage  (  this  .  currentPage  )  ; 
this  .  fileHolder  .  closeFile  (  )  ; 
this  .  buffer  =  Bytes  .  EMPTY_ARRAY  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
} 
} 
} 

final   boolean   isOpen  (  )  { 
return  !  this  .  fileHolder  .  closed  ; 
} 










final   void   open  (  )  throws   IOException  { 
synchronized  (  this  .  fileHolder  )  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  (  this  .  fileHolder  .  file  ==  null  )  &&  (  this  .  fileHolder  .  path  !=  null  )  )  { 
this  .  fileHolder  .  file  =  new   RandomAccessFile  (  this  .  fileHolder  .  path  ,  "rw"  )  ; 
this  .  openCount  =  1  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
this  .  buffer  =  new   byte  [  PAGE_SIZE  ]  ; 
}  else   if  (  this  .  fileHolder  .  file  !=  null  )  this  .  openCount  ++  ; 
} 
} 












final   int   read  (  final   int   readPtr  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  readPtr  >=  this  .  length  )  return  -  1  ; 
if  (  this  .  fileHolder  .  file  !=  null  )  { 
if  (  this  .  currentPage  !=  (  readPtr  &  PAGE_MASK  )  )  readPage  (  readPtr  )  ; 
return   this  .  buffer  [  readPtr  &  BYTE_MASK  ]  &  0xFF  ; 
}  else  { 
return   this  .  buffer  [  readPtr  ]  &  0xFF  ; 
} 
} 











final   int   read  (  int   readPtr  ,  final   byte  [  ]  bytes  ,  int   offset  ,  int   len  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  bytes  ==  null  )  throw   new   NullPointerException  (  )  ;  else   if  (  (  offset  <  0  )  ||  (  offset  >  bytes  .  length  )  ||  (  len  <  0  )  ||  (  (  offset  +  len  )  >  bytes  .  length  )  ||  (  (  offset  +  len  )  <  0  )  )  { 
throw   new   IndexOutOfBoundsException  (  )  ; 
}  else   if  (  len  ==  0  )  return   0  ;  else   if  (  readPtr  >=  length  )  return  -  1  ;  else  { 
len  =  Math  .  min  (  this  .  length  -  readPtr  ,  len  )  ; 
if  (  this  .  fileHolder  .  file  ==  null  )  System  .  arraycopy  (  this  .  buffer  ,  readPtr  ,  bytes  ,  offset  ,  len  )  ;  else   if  (  len  >=  PAGE_SIZE  )  { 
if  (  this  .  bufferDirty  )  writePage  (  this  .  currentPage  )  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
this  .  fileHolder  .  file  .  seek  (  readPtr  )  ; 
this  .  fileHolder  .  file  .  readFully  (  bytes  ,  offset  ,  len  )  ; 
}  else  { 
int   count  =  len  ; 
while  (  count  >  0  )  { 
if  (  this  .  currentPage  !=  (  readPtr  &  PAGE_MASK  )  )  readPage  (  readPtr  )  ; 
final   int   inBuffer  =  Math  .  min  (  PAGE_SIZE  -  (  readPtr  &  BYTE_MASK  )  ,  count  )  ; 
System  .  arraycopy  (  this  .  buffer  ,  readPtr  &  BYTE_MASK  ,  bytes  ,  offset  ,  inBuffer  )  ; 
offset  +=  inBuffer  ; 
readPtr  +=  inBuffer  ; 
count  -=  inBuffer  ; 
} 
} 
return   len  ; 
} 
} 












final   void   write  (  final   int   writePtr  ,  final   int   b  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  writePtr  >=  this  .  length  )  { 
if  (  writePtr  >  this  .  length  )  { 
throw   new   IOException  (  "BLOB buffer has been truncated"  )  ; 
} 
if  (  ++  this  .  length  <  0  )  { 
throw   new   IOException  (  "BLOB may not exceed 2GB in size"  )  ; 
} 
} 
if  (  this  .  fileHolder  .  file  !=  null  )  { 
if  (  this  .  currentPage  !=  (  writePtr  &  PAGE_MASK  )  )  readPage  (  writePtr  )  ; 
this  .  buffer  [  writePtr  &  BYTE_MASK  ]  =  (  byte  )  b  ; 
this  .  bufferDirty  =  true  ; 
}  else  { 
if  (  writePtr  >=  this  .  buffer  .  length  )  growBuffer  (  writePtr  +  1  )  ; 
this  .  buffer  [  writePtr  ]  =  (  byte  )  b  ; 
} 
} 










final   void   write  (  int   writePtr  ,  final   byte  [  ]  bytes  ,  int   offset  ,  final   int   len  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  bytes  ==  null  )  throw   new   NullPointerException  (  )  ;  else   if  (  (  offset  <  0  )  ||  (  offset  >  bytes  .  length  )  ||  (  len  <  0  )  ||  (  (  offset  +  len  )  >  bytes  .  length  )  ||  (  (  offset  +  len  )  <  0  )  )  { 
throw   new   IndexOutOfBoundsException  (  )  ; 
}  else   if  (  len  ==  0  )  { 
return  ; 
} 
if  (  (  long  )  writePtr  +  len  >  (  long  )  Integer  .  MAX_VALUE  )  throw   new   IOException  (  "BLOB may not exceed 2GB in size"  )  ; 
if  (  writePtr  >  this  .  length  )  { 
throw   new   IOException  (  "BLOB buffer has been truncated"  )  ; 
} 
if  (  this  .  fileHolder  .  file  !=  null  )  { 
if  (  len  >=  PAGE_SIZE  )  { 
if  (  bufferDirty  )  writePage  (  this  .  currentPage  )  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
this  .  fileHolder  .  file  .  seek  (  writePtr  )  ; 
this  .  fileHolder  .  file  .  write  (  bytes  ,  offset  ,  len  )  ; 
writePtr  +=  len  ; 
}  else  { 
int   count  =  len  ; 
while  (  count  >  0  )  { 
if  (  this  .  currentPage  !=  (  writePtr  &  PAGE_MASK  )  )  readPage  (  writePtr  )  ; 
final   int   inBuffer  =  Math  .  min  (  PAGE_SIZE  -  (  writePtr  &  BYTE_MASK  )  ,  count  )  ; 
System  .  arraycopy  (  bytes  ,  offset  ,  this  .  buffer  ,  writePtr  &  BYTE_MASK  ,  inBuffer  )  ; 
this  .  bufferDirty  =  true  ; 
offset  +=  inBuffer  ; 
writePtr  +=  inBuffer  ; 
count  -=  inBuffer  ; 
} 
} 
}  else  { 
if  (  writePtr  +  len  >  this  .  buffer  .  length  )  growBuffer  (  writePtr  +  len  )  ; 
System  .  arraycopy  (  bytes  ,  offset  ,  this  .  buffer  ,  writePtr  ,  len  )  ; 
writePtr  +=  len  ; 
} 
if  (  writePtr  >  this  .  length  )  this  .  length  =  writePtr  ; 
} 

final   void   write  (  int   writePtr  ,  final   ByteBuffer   src  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  src  ==  null  )  throw   new   NullPointerException  (  )  ; 
final   int   remaining  =  src  .  remaining  (  )  ; 
if  (  remaining  ==  0  )  return  ; 
if  (  (  long  )  writePtr  +  remaining  >  Integer  .  MAX_VALUE  )  throw   new   IOException  (  "BLOB may not exceed 2GB in size"  )  ; 
if  (  writePtr  >  this  .  length  )  { 
throw   new   IOException  (  "BLOB buffer has been truncated"  )  ; 
} 
if  (  this  .  fileHolder  .  file  !=  null  )  { 
if  (  remaining  >=  PAGE_SIZE  )  { 
if  (  this  .  bufferDirty  )  writePage  (  this  .  currentPage  )  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
this  .  fileHolder  .  file  .  seek  (  writePtr  )  ; 
while  (  src  .  hasRemaining  (  )  )  this  .  fileHolder  .  file  .  getChannel  (  )  .  write  (  src  )  ; 
writePtr  +=  remaining  ; 
}  else  { 
while  (  src  .  hasRemaining  (  )  )  { 
if  (  this  .  currentPage  !=  (  writePtr  &  PAGE_MASK  )  )  readPage  (  writePtr  )  ; 
final   int   inBuffer  =  Math  .  min  (  PAGE_SIZE  -  (  writePtr  &  BYTE_MASK  )  ,  src  .  remaining  (  )  )  ; 
src  .  get  (  this  .  buffer  ,  writePtr  &  BYTE_MASK  ,  inBuffer  )  ; 
this  .  bufferDirty  =  true  ; 
writePtr  +=  inBuffer  ; 
} 
} 
}  else  { 
if  (  writePtr  +  remaining  >  this  .  buffer  .  length  )  growBuffer  (  writePtr  +  remaining  )  ; 
src  .  get  (  this  .  buffer  ,  writePtr  ,  remaining  )  ; 
writePtr  +=  remaining  ; 
} 
if  (  writePtr  >  this  .  length  )  this  .  length  =  writePtr  ; 
} 









final   void   readPage  (  int   page  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
page  =  page  &  PAGE_MASK  ; 
if  (  bufferDirty  )  writePage  (  currentPage  )  ; 
if  (  page  >  this  .  fileHolder  .  file  .  length  (  )  )  throw   new   IOException  (  "readPage: Invalid page number "  +  page  )  ; 
this  .  currentPage  =  page  ; 
this  .  fileHolder  .  file  .  seek  (  currentPage  )  ; 
int   count  =  0  ,  res  ; 
do  { 
res  =  this  .  fileHolder  .  file  .  read  (  buffer  ,  count  ,  buffer  .  length  -  count  )  ; 
count  +=  (  res  ==  -  1  )  ?  0  :  res  ; 
}  while  (  count  <  PAGE_SIZE  &&  res  !=  -  1  )  ; 
} 







final   void   writePage  (  int   page  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
page  &=  PAGE_MASK  ; 
if  (  page  >  this  .  fileHolder  .  file  .  length  (  )  )  throw   new   IOException  (  "writePage: Invalid page number "  +  page  )  ; 
if  (  buffer  .  length  !=  PAGE_SIZE  )  throw   new   IllegalStateException  (  "writePage: buffer size invalid"  )  ; 
this  .  fileHolder  .  file  .  seek  (  page  )  ; 
this  .  fileHolder  .  file  .  write  (  buffer  )  ; 
this  .  bufferDirty  =  false  ; 
} 







final   void   growBuffer  (  final   int   minSize  )  throws   IOException  { 
this  .  fileHolder  .  ensureOpen  (  )  ; 
if  (  this  .  buffer  .  length  ==  0  )  this  .  buffer  =  new   byte  [  Math  .  max  (  PAGE_SIZE  ,  minSize  )  ]  ;  else  { 
final   byte  [  ]  tmp  ; 
if  (  (  this  .  buffer  .  length  *  2  >  minSize  )  &&  (  this  .  buffer  .  length  <=  MAX_BUF_INC  )  )  tmp  =  new   byte  [  this  .  buffer  .  length  *  2  ]  ;  else   tmp  =  new   byte  [  minSize  +  MAX_BUF_INC  ]  ; 
System  .  arraycopy  (  this  .  buffer  ,  0  ,  tmp  ,  0  ,  this  .  buffer  .  length  )  ; 
this  .  buffer  =  tmp  ; 
} 
} 







final   void   setBuffer  (  final   byte  [  ]  bytes  ,  final   boolean   copy  )  throws   SQLException  { 
ensureOpen  (  )  ; 
this  .  buffer  =  (  bytes  .  length  ==  0  )  ?  Bytes  .  EMPTY_ARRAY  :  copy  ?  bytes  .  clone  (  )  :  bytes  ; 
this  .  length  =  bytes  .  length  ; 
} 








final   byte  [  ]  getBytes  (  long   pos  ,  int   len  )  throws   SQLException  { 
ensureOpen  (  )  ; 
pos  --  ; 
if  (  pos  <  0  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badpos"  )  ,  "HY090"  )  ; 
if  (  pos  >  this  .  length  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badposlen"  )  ,  "HY090"  )  ; 
if  (  len  <  0  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badlen"  )  ,  "HY090"  )  ; 
if  (  pos  +  len  >  this  .  length  )  len  =  (  int  )  (  this  .  length  -  pos  )  ; 
try  { 
if  (  BlobBuffer  .  this  .  fileHolder  .  path  ==  null  )  { 
final   byte  [  ]  data  =  new   byte  [  len  ]  ; 
System  .  arraycopy  (  buffer  ,  (  int  )  (  pos  )  ,  data  ,  0  ,  len  )  ; 
return   data  ; 
}  else  { 
BlobInputStream   is  =  new   BlobInputStream  (  this  ,  pos  )  ; 
try  { 
final   byte  [  ]  data  =  new   byte  [  len  ]  ; 
IOStreams  .  readFully  (  is  ,  data  )  ; 
is  .  close  (  )  ; 
is  =  null  ; 
return   data  ; 
}  finally  { 
IO  .  tryClose  (  is  )  ; 
} 
} 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
} 

final   InputStream   getAsciiStream  (  )  throws   SQLException  { 
ensureOpen  (  )  ; 
try  { 
return   new   AsciiInputStream  (  this  ,  0  )  ; 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
} 








final   InputStream   getBinaryStream  (  )  throws   SQLException  { 
ensureOpen  (  )  ; 
try  { 
return   new   BlobInputStream  (  this  ,  0  )  ; 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
} 

final   InputStream   getBinaryStream  (  long   pos  ,  final   long   length  )  throws   SQLException  { 
ensureOpen  (  )  ; 
if  (  pos  <  1  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badoffset"  )  ,  "22003"  )  ; 
--  pos  ; 
final   long   totalLength  =  getLength  (  )  ; 
if  (  (  length  <  0  )  ||  (  pos  +  length  >  totalLength  )  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badlen"  )  ,  "22003"  )  ; 
InputStream   in  ; 
try  { 
in  =  new   BlobInputStream  (  this  ,  pos  )  ; 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
if  (  pos  +  length  <  totalLength  )  in  =  new   FixedLengthInputStream  (  in  ,  length  ,  true  )  ; 
return   in  ; 
} 








final   InputStream   getUnicodeStream  (  )  throws   SQLException  { 
ensureOpen  (  )  ; 
try  { 
return   new   UnicodeInputStream  (  this  ,  0  )  ; 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
} 













final   OutputStream   setBinaryStream  (  long   pos  ,  final   boolean   ascii  )  throws   SQLException  { 
ensureOpen  (  )  ; 
pos  --  ; 
if  (  pos  <  0  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badpos"  )  ,  "HY090"  )  ; 
if  (  pos  >  this  .  length  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badposlen"  )  ,  "HY090"  )  ; 
try  { 
createBlobFileIfNecessary  (  )  ; 
if  (  ascii  )  return   new   AsciiOutputStream  (  this  ,  pos  )  ;  else   return   new   BlobOutputStream  (  this  ,  pos  )  ; 
}  catch  (  final   IOException   ex  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  ex  .  getMessage  (  )  )  ,  "HY000"  ,  ex  )  ; 
} 
} 






















final   int   setBytes  (  long   pos  ,  final   byte  [  ]  bytes  ,  final   int   offset  ,  final   int   len  ,  final   boolean   copy  )  throws   SQLException  { 
ensureOpen  (  )  ; 
pos  --  ; 
if  (  pos  <  0  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badpos"  )  ,  "HY090"  )  ; 
if  (  pos  >  this  .  length  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badposlen"  )  ,  "HY090"  )  ; 
if  (  bytes  ==  null  )  throw   new   SQLException  (  Messages  .  get  (  "error.blob.bytesnull"  )  ,  "HY009"  )  ; 
if  (  offset  <  0  ||  offset  >  bytes  .  length  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badoffset"  )  ,  "HY090"  )  ; 
if  (  len  <  0  ||  pos  +  len  >  (  long  )  Integer  .  MAX_VALUE  ||  offset  +  len  >  bytes  .  length  )  throw   new   SQLException  (  Messages  .  get  (  "error.blobclob.badlen"  )  ,  "HY090"  )  ; 
if  (  (  this  .  fileHolder  .  path  ==  null  )  &&  (  pos  ==  0  )  &&  (  len  >=  this  .  length  )  &&  (  len  <=  maxMemSize  )  )  { 
if  (  !  copy  )  this  .  buffer  =  bytes  ;  else   this  .  buffer  =  Arrays  .  copyOfRange  (  bytes  ,  offset  ,  offset  +  len  )  ; 
this  .  length  =  len  ; 
return   len  ; 
}  else  { 
try  { 
createBlobFileIfNecessary  (  )  ; 
open  (  )  ; 
try  { 
write  (  (  int  )  pos  ,  bytes  ,  offset  ,  len  )  ; 
}  finally  { 
closeOne  (  )  ; 
} 
return   len  ; 
}  catch  (  final   IOException   e  )  { 
throw   new   SQLException  (  Messages  .  get  (  "error.generic.ioerror"  ,  e  .  getMessage  (  )  )  ,  "HY000"  ,  e  )  ; 
} 
} 
} 

final   int   setBytes  (  long   pos  ,  final   ByteBuffer   bytes  ,  final   boolean   copy  )  throws   SQLException  { 
ensureOpen  (  )  ; 
if  (  bytes  ==  null  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blob.bytesnull"  )  ,  "HY009"  )  ; 
final   int   remaining  =  bytes  .  remaining  (  )  ; 
if  (  remaining  ==  0  )  return   0  ;  else   if  (  copy  )  { 
final   byte  [  ]  a  =  new   byte  [  remaining  ]  ; 
bytes  .  get  (  a  )  ; 
return   setBytes  (  pos  ,  a  ,  0  ,  remaining  ,  false  )  ; 
}  else  { 
pos  --  ; 
if  (  pos  <  0  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badpos"  )  ,  "HY090"  )  ; 
if  (  pos  >  this  .  length  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badposlen"  )  ,  "HY090"  )  ; 
if  (  pos  +  remaining  >  Integer  .  MAX_VALUE  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badlen"  )  ,  "HY090"  )  ; 
try  { 
createBlobFileIfNecessary  (  )  ; 
open  (  )  ; 
try  { 
write  (  (  int  )  pos  ,  bytes  )  ; 
}  finally  { 
closeOne  (  )  ; 
} 
return   remaining  ; 
}  catch  (  final   IOException   ex  )  { 
throw   new   SQLNonTransientException  (  Messages  .  get  (  "error.generic.ioerror"  ,  ex  .  getMessage  (  )  )  ,  "HY000"  ,  ex  )  ; 
} 
} 
} 






final   long   getLength  (  )  { 
return   this  .  length  ; 
} 






final   void   setLength  (  final   long   length  )  { 
this  .  length  =  (  int  )  length  ; 
} 







final   void   truncate  (  final   long   len  )  throws   SQLException  { 
ensureOpen  (  )  ; 
if  (  len  <  0  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badlen"  )  ,  "HY090"  )  ; 
if  (  len  >  this  .  length  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.lentoolong"  )  ,  "HY090"  )  ; 
length  =  (  int  )  len  ; 
if  (  len  ==  0  )  { 
try  { 
this  .  fileHolder  .  close  (  )  ; 
}  catch  (  final   IOException   ex  )  { 
throw   new   SQLNonTransientException  (  Messages  .  get  (  "error.generic.ioerror"  ,  ex  .  getMessage  (  )  )  ,  "HY000"  ,  ex  )  ; 
}  finally  { 
this  .  buffer  =  Bytes  .  EMPTY_ARRAY  ; 
this  .  openCount  =  0  ; 
this  .  currentPage  =  INVALID_PAGE  ; 
} 
} 
} 










final   int   position  (  final   byte  [  ]  pattern  ,  long   start  )  throws   SQLException  { 
ensureOpen  (  )  ; 
try  { 
start  --  ; 
if  (  start  <  0  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badpos"  )  ,  "HY090"  )  ; 
if  (  start  >=  this  .  length  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blobclob.badposlen"  )  ,  "HY090"  )  ; 
if  (  pattern  ==  null  )  throw   new   SQLDataException  (  Messages  .  get  (  "error.blob.badpattern"  )  ,  "HY009"  )  ; 
if  (  (  pattern  .  length  ==  0  )  ||  (  length  ==  0  )  ||  (  pattern  .  length  >  length  )  )  return  -  1  ; 
final   int   limit  =  length  -  pattern  .  length  ; 
if  (  this  .  fileHolder  .  path  ==  null  )  { 
for  (  int   i  =  (  int  )  start  ;  i  <=  limit  ;  i  ++  )  { 
int   p  ; 
for  (  p  =  0  ;  p  <  pattern  .  length  &&  buffer  [  i  +  p  ]  ==  pattern  [  p  ]  ;  p  ++  )  continue  ; 
if  (  p  ==  pattern  .  length  )  return   i  +  1  ; 
} 
return  -  1  ; 
}  else  { 
int   result  =  -  1  ; 
open  (  )  ; 
try  { 
for  (  int   i  =  (  int  )  start  ;  i  <=  limit  ;  i  ++  )  { 
int   p  ; 
for  (  p  =  0  ;  p  <  pattern  .  length  &&  read  (  i  +  p  )  ==  (  pattern  [  p  ]  &  0xFF  )  ;  p  ++  )  continue  ; 
if  (  p  ==  pattern  .  length  )  { 
result  =  i  +  1  ; 
break  ; 
} 
} 
}  finally  { 
closeOne  (  )  ; 
} 
return   result  ; 
} 
}  catch  (  final   IOException   ex  )  { 
throw   new   SQLNonTransientException  (  Messages  .  get  (  "error.generic.ioerror"  ,  ex  .  getMessage  (  )  )  ,  "HY000"  ,  ex  )  ; 
} 
} 

private   static   final   class   FileHolder   extends   Object   implements   Closeable  { 

boolean   closed  ; 




RandomAccessFile   file  ; 




private   File   path  ; 

private   Closer  .  CloseableReference   close  ; 

private   final   Closer   closer  ; 

FileHolder  (  final   Closer   closer  )  { 
super  (  )  ; 
this  .  closer  =  closer  ; 
} 

@  Override 
public   final   void   close  (  )  throws   IOException  { 
IOException   ex  =  null  ; 
Closer  .  CloseableReference   close  ; 
RandomAccessFile   file  ; 
final   File   path  ; 
synchronized  (  this  )  { 
this  .  closed  =  true  ; 
close  =  this  .  close  ; 
file  =  this  .  file  ; 
path  =  this  .  path  ; 
this  .  file  =  null  ; 
this  .  path  =  null  ; 
this  .  close  =  null  ; 
} 
if  (  close  !=  null  )  { 
close  .  clear  (  )  ; 
close  =  null  ; 
} 
if  (  file  !=  null  )  { 
try  { 
file  .  close  (  )  ; 
}  catch  (  final   IOException   x  )  { 
ex  =  x  ; 
} 
file  =  null  ; 
} 
if  (  path  !=  null  )  { 
try  { 
path  .  delete  (  )  ; 
}  catch  (  final   RuntimeException   x  )  { 
if  (  ex  ==  null  )  throw   x  ; 
} 
} 
if  (  ex  !=  null  )  throw   ex  ; 
} 

final   void   closeFile  (  )  throws   IOException  { 
final   RandomAccessFile   f  ; 
synchronized  (  this  )  { 
f  =  this  .  file  ; 
this  .  file  =  null  ; 
} 
if  (  f  !=  null  )  f  .  close  (  )  ; 
} 

final   void   ensureOpen  (  )  throws   IOException  { 
if  (  this  .  closed  )  throw   new   IOException  (  "closed"  )  ; 
} 

final   void   tryClose  (  )  { 
try  { 
close  (  )  ; 
}  catch  (  final   IOException   ex  )  { 
}  catch  (  final   SecurityException   ex  )  { 
} 
} 
} 

private   abstract   static   class   AbstractInputStream   extends   InputStream  { 

int   readPtr  ; 

BlobBuffer   buffer  ; 

AbstractInputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  )  ; 
buffer  .  open  (  )  ; 
this  .  buffer  =  buffer  ; 
this  .  readPtr  =  (  int  )  pos  ; 
} 

final   BlobBuffer   ensureOpen  (  )  throws   IOException  { 
final   BlobBuffer   buffer  =  this  .  buffer  ; 
if  (  buffer  ==  null  )  throw   new   IOException  (  "closed"  )  ; 
return   buffer  ; 
} 

@  Override 
public   final   synchronized   void   close  (  )  throws   IOException  { 
final   BlobBuffer   buffer  =  this  .  buffer  ; 
if  (  buffer  !=  null  )  { 
this  .  buffer  =  null  ; 
buffer  .  closeOne  (  )  ; 
} 
} 
} 

private   abstract   static   class   AbstractOutputStream   extends   OutputStream  { 

int   writePtr  ; 

BlobBuffer   buffer  ; 

AbstractOutputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  )  ; 
buffer  .  open  (  )  ; 
this  .  buffer  =  buffer  ; 
this  .  writePtr  =  (  int  )  pos  ; 
} 

final   BlobBuffer   ensureOpen  (  )  throws   IOException  { 
final   BlobBuffer   buffer  =  this  .  buffer  ; 
if  (  buffer  ==  null  )  throw   new   IOException  (  "closed"  )  ; 
return   buffer  ; 
} 

@  Override 
public   final   synchronized   void   close  (  )  throws   IOException  { 
final   BlobBuffer   buffer  =  this  .  buffer  ; 
if  (  buffer  !=  null  )  { 
this  .  buffer  =  null  ; 
buffer  .  closeOne  (  )  ; 
} 
} 
} 




private   static   final   class   BlobInputStream   extends   AbstractInputStream  { 







BlobInputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  buffer  ,  pos  )  ; 
} 






@  Override 
public   final   int   available  (  )  throws   IOException  { 
return  (  int  )  ensureOpen  (  )  .  getLength  (  )  -  this  .  readPtr  ; 
} 







@  Override 
public   final   int   read  (  )  throws   IOException  { 
final   int   b  =  ensureOpen  (  )  .  read  (  this  .  readPtr  )  ; 
if  (  b  >=  0  )  this  .  readPtr  ++  ; 
return   b  ; 
} 










@  Override 
public   final   int   read  (  final   byte  [  ]  bytes  ,  final   int   offset  ,  final   int   len  )  throws   IOException  { 
final   int   count  =  ensureOpen  (  )  .  read  (  readPtr  ,  bytes  ,  offset  ,  len  )  ; 
if  (  count  >  0  )  this  .  readPtr  +=  count  ; 
return   count  ; 
} 

@  Override 
public   final   long   skip  (  long   n  )  throws   IOException  { 
final   BlobBuffer   buffer  =  ensureOpen  (  )  ; 
if  (  n  <=  0  )  return   0  ; 
final   int   pos  =  this  .  readPtr  ; 
n  =  Math  .  max  (  0  ,  Math  .  min  (  n  ,  buffer  .  getLength  (  )  -  pos  )  )  ; 
this  .  readPtr  =  (  int  )  (  pos  +  n  )  ; 
return   n  ; 
} 
} 




private   static   final   class   UnicodeInputStream   extends   AbstractInputStream  { 







UnicodeInputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  buffer  ,  pos  )  ; 
} 






@  Override 
public   final   int   available  (  )  throws   IOException  { 
return  (  int  )  ensureOpen  (  )  .  getLength  (  )  -  this  .  readPtr  ; 
} 







@  Override 
public   final   int   read  (  )  throws   IOException  { 
final   int   b  =  ensureOpen  (  )  .  read  (  readPtr  ^  1  )  ; 
if  (  b  >=  0  )  this  .  readPtr  ++  ; 
return   b  ; 
} 
} 










private   static   final   class   AsciiInputStream   extends   AbstractInputStream  { 







AsciiInputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  buffer  ,  pos  )  ; 
} 






@  Override 
public   final   int   available  (  )  throws   IOException  { 
return  (  (  int  )  ensureOpen  (  )  .  getLength  (  )  -  this  .  readPtr  )  >  >  1  ; 
} 







@  Override 
public   final   int   read  (  )  throws   IOException  { 
final   BlobBuffer   buffer  =  ensureOpen  (  )  ; 
int   b1  =  buffer  .  read  (  readPtr  )  ; 
if  (  b1  >=  0  )  { 
readPtr  ++  ; 
final   int   b2  =  buffer  .  read  (  readPtr  )  ; 
if  (  b2  >=  0  )  { 
readPtr  ++  ; 
if  (  b2  !=  0  ||  b1  >  0x7F  )  b1  =  '?'  ; 
return   b1  ; 
} 
} 
return  -  1  ; 
} 
} 




private   static   final   class   BlobOutputStream   extends   AbstractOutputStream  { 







BlobOutputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  buffer  ,  pos  )  ; 
} 







@  Override 
public   final   void   write  (  final   int   b  )  throws   IOException  { 
ensureOpen  (  )  .  write  (  writePtr  ++  ,  b  )  ; 
} 









@  Override 
public   final   void   write  (  final   byte  [  ]  bytes  ,  final   int   offset  ,  final   int   len  )  throws   IOException  { 
ensureOpen  (  )  .  write  (  this  .  writePtr  ,  bytes  ,  offset  ,  len  )  ; 
this  .  writePtr  +=  len  ; 
} 
} 




private   static   final   class   AsciiOutputStream   extends   AbstractOutputStream  { 








AsciiOutputStream  (  final   BlobBuffer   buffer  ,  final   long   pos  )  throws   IOException  { 
super  (  buffer  ,  pos  )  ; 
} 







@  Override 
public   final   void   write  (  final   int   b  )  throws   IOException  { 
final   BlobBuffer   buffer  =  ensureOpen  (  )  ; 
buffer  .  write  (  writePtr  ++  ,  b  )  ; 
buffer  .  write  (  writePtr  ++  ,  0  )  ; 
} 
} 
} 

