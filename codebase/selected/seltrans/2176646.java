package   gov  .  nasa  .  worldwind  .  util  ; 

import   com  .  sun  .  opengl  .  util  .  BufferUtil  ; 
import   gov  .  nasa  .  worldwind  .  Configuration  ; 
import   gov  .  nasa  .  worldwind  .  avlist  .  AVKey  ; 
import   gov  .  nasa  .  worldwind  .  exception  .  WWRuntimeException  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  zip  .  *  ; 

public   class   WWIO  { 

public   static   final   String   DELETE_ON_EXIT_PREFIX  =  "WWJDeleteOnExit"  ; 

public   static   final   String   ILLEGAL_FILE_PATH_PART_CHARACTERS  =  "["  +  "?/\\\\=+<>:;\\,\"\\|^\\[\\]"  +  "]"  ; 


protected   static   final   String   DEFAULT_CHARACTER_ENCODING  =  "UTF-8"  ; 


public   static   final   int   MAX_FILE_PATH_LENGTH  =  255  ; 

public   static   String   formPath  (  String  ...  pathParts  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  String   pathPart  :  pathParts  )  { 
if  (  pathPart  ==  null  )  continue  ; 
if  (  sb  .  length  (  )  >  0  )  sb  .  append  (  File  .  separator  )  ; 
sb  .  append  (  pathPart  .  replaceAll  (  ILLEGAL_FILE_PATH_PART_CHARACTERS  ,  "_"  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   String   appendPathPart  (  String   firstPart  ,  String   secondPart  )  { 
if  (  secondPart  ==  null  ||  secondPart  .  length  (  )  ==  0  )  return   firstPart  ; 
if  (  firstPart  ==  null  ||  firstPart  .  length  (  )  ==  0  )  return   secondPart  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
sb  .  append  (  WWIO  .  stripTrailingSeparator  (  firstPart  )  )  ; 
sb  .  append  (  File  .  separator  )  ; 
sb  .  append  (  WWIO  .  stripLeadingSeparator  (  secondPart  )  )  ; 
return   sb  .  toString  (  )  ; 
} 










public   static   String   replaceIllegalFileNameCharacters  (  String   s  )  { 
if  (  s  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   s  .  replaceAll  (  ILLEGAL_FILE_PATH_PART_CHARACTERS  ,  "_"  )  ; 
} 

public   static   String   stripTrailingSeparator  (  String   s  )  { 
if  (  s  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  s  .  endsWith  (  "/"  )  ||  s  .  endsWith  (  "\\"  )  )  return   s  .  substring  (  0  ,  s  .  length  (  )  -  1  )  ;  else   return   s  ; 
} 

public   static   String   stripLeadingSeparator  (  String   s  )  { 
if  (  s  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  s  .  startsWith  (  "/"  )  ||  s  .  startsWith  (  "\\"  )  )  return   s  .  substring  (  1  ,  s  .  length  (  )  )  ;  else   return   s  ; 
} 

public   static   String   stripLeadingZeros  (  String   s  )  { 
if  (  s  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
int   len  =  s  .  length  (  )  ; 
if  (  len  <  2  )  return   s  ; 
int   i  =  0  ; 
while  (  i  <  len  &&  s  .  charAt  (  i  )  ==  '0'  )  { 
i  ++  ; 
} 
if  (  i  ==  len  )  i  =  len  -  1  ; 
if  (  i  ==  0  )  return   s  ; 
return   s  .  substring  (  i  ,  len  )  ; 
} 













public   static   File   getFileForLocalAddress  (  Object   src  )  { 
if  (  WWUtil  .  isEmpty  (  src  )  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  src   instanceof   File  )  return  (  File  )  src  ;  else   if  (  src   instanceof   URL  )  return   convertURLToFile  (  (  URL  )  src  )  ;  else   if  (  src   instanceof   URI  )  return   convertURIToFile  (  (  URI  )  src  )  ;  else   if  (  !  (  src   instanceof   String  )  )  return   null  ; 
String   sourceName  =  (  String  )  src  ; 
File   file  =  new   File  (  sourceName  )  ; 
if  (  file  .  exists  (  )  )  return   file  ; 
URL   url  =  makeURL  (  sourceName  )  ; 
if  (  url  !=  null  )  return   convertURLToFile  (  url  )  ; 
URI   uri  =  makeURI  (  sourceName  )  ; 
if  (  uri  !=  null  )  return   convertURIToFile  (  uri  )  ; 
return   null  ; 
} 











public   static   File   convertURIToFile  (  URI   uri  )  { 
if  (  uri  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URIIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
return   new   File  (  uri  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   null  ; 
} 
} 











public   static   File   convertURLToFile  (  URL   url  )  { 
if  (  url  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URLIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
return   new   File  (  url  .  toURI  (  )  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   null  ; 
}  catch  (  URISyntaxException   e  )  { 
return   null  ; 
} 
} 

@  SuppressWarnings  (  {  "ResultOfMethodCallIgnored"  }  ) 
public   static   boolean   saveBuffer  (  ByteBuffer   buffer  ,  File   file  ,  boolean   forceFilesystemWrite  )  throws   IOException  { 
if  (  buffer  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.BufferNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
FileOutputStream   fos  =  null  ; 
FileChannel   channel  =  null  ; 
FileLock   lock  ; 
int   numBytesWritten  =  0  ; 
try  { 
fos  =  new   FileOutputStream  (  file  )  ; 
channel  =  fos  .  getChannel  (  )  ; 
lock  =  channel  .  tryLock  (  )  ; 
if  (  lock  ==  null  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  FINER  ,  "WWIO.UnableToAcquireLockFor"  ,  file  .  getPath  (  )  )  ; 
return   false  ; 
} 
for  (  buffer  .  rewind  (  )  ;  buffer  .  hasRemaining  (  )  ;  )  { 
numBytesWritten  +=  channel  .  write  (  buffer  )  ; 
} 
if  (  forceFilesystemWrite  )  channel  .  force  (  true  )  ; 
fos  .  flush  (  )  ; 
return   true  ; 
}  catch  (  ClosedByInterruptException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  FINE  ,  Logging  .  getMessage  (  "generic.interrupted"  ,  "WWIO.saveBuffer"  ,  file  .  getPath  (  )  )  ,  e  )  ; 
if  (  numBytesWritten  >  0  )  file  .  delete  (  )  ; 
throw   e  ; 
}  catch  (  IOException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  SEVERE  ,  Logging  .  getMessage  (  "WWIO.ErrorSavingBufferTo"  ,  file  .  getPath  (  )  )  ,  e  )  ; 
if  (  numBytesWritten  >  0  )  file  .  delete  (  )  ; 
throw   e  ; 
}  finally  { 
WWIO  .  closeStream  (  channel  ,  file  .  getPath  (  )  )  ; 
WWIO  .  closeStream  (  fos  ,  file  .  getPath  (  )  )  ; 
} 
} 

public   static   boolean   saveBuffer  (  ByteBuffer   buffer  ,  File   file  )  throws   IOException  { 
return   saveBuffer  (  buffer  ,  file  ,  true  )  ; 
} 

@  SuppressWarnings  (  {  "ResultOfMethodCallIgnored"  }  ) 
public   static   boolean   saveBufferToStream  (  ByteBuffer   buffer  ,  OutputStream   fos  )  throws   IOException  { 
if  (  buffer  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.BufferNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  fos  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
WritableByteChannel   channel  ; 
try  { 
channel  =  Channels  .  newChannel  (  fos  )  ; 
for  (  buffer  .  rewind  (  )  ;  buffer  .  hasRemaining  (  )  ;  )  { 
channel  .  write  (  buffer  )  ; 
} 
fos  .  flush  (  )  ; 
return   true  ; 
}  finally  { 
WWIO  .  closeStream  (  fos  ,  null  )  ; 
} 
} 
























public   static   MappedByteBuffer   mapFile  (  File   file  ,  FileChannel  .  MapMode   mode  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  mode  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ModelIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
String   accessMode  ; 
if  (  mode  ==  FileChannel  .  MapMode  .  READ_ONLY  )  accessMode  =  "r"  ;  else   accessMode  =  "rw"  ; 
RandomAccessFile   raf  =  null  ; 
try  { 
raf  =  new   RandomAccessFile  (  file  ,  accessMode  )  ; 
FileChannel   fc  =  raf  .  getChannel  (  )  ; 
return   fc  .  map  (  mode  ,  0  ,  fc  .  size  (  )  )  ; 
}  finally  { 
WWIO  .  closeStream  (  raf  ,  file  .  getPath  (  )  )  ; 
} 
} 












public   static   MappedByteBuffer   mapFile  (  File   file  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   mapFile  (  file  ,  FileChannel  .  MapMode  .  READ_ONLY  )  ; 
} 












public   static   ByteBuffer   readURLContentToBuffer  (  URL   url  )  throws   IOException  { 
if  (  url  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URLIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   readURLContentToBuffer  (  url  ,  false  )  ; 
} 
















public   static   ByteBuffer   readURLContentToBuffer  (  URL   url  ,  boolean   allocateDirect  )  throws   IOException  { 
if  (  url  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URLIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
InputStream   is  =  null  ; 
try  { 
is  =  url  .  openStream  (  )  ; 
return   readStreamToBuffer  (  is  ,  allocateDirect  )  ; 
}  finally  { 
WWIO  .  closeStream  (  is  ,  url  .  toString  (  )  )  ; 
} 
} 


















public   static   String   readURLContentToString  (  URL   url  ,  String   encoding  )  throws   IOException  { 
if  (  url  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URLIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
ByteBuffer   buffer  =  readURLContentToBuffer  (  url  )  ; 
return   byteBufferToString  (  buffer  ,  encoding  )  ; 
} 












public   static   ByteBuffer   readFileToBuffer  (  File   file  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   readFileToBuffer  (  file  ,  false  )  ; 
} 
















public   static   ByteBuffer   readFileToBuffer  (  File   file  ,  boolean   allocateDirect  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
FileInputStream   is  =  new   FileInputStream  (  file  )  ; 
try  { 
FileChannel   fc  =  is  .  getChannel  (  )  ; 
int   size  =  (  int  )  fc  .  size  (  )  ; 
ByteBuffer   buffer  =  allocateDirect  ?  ByteBuffer  .  allocateDirect  (  size  )  :  ByteBuffer  .  allocate  (  size  )  ; 
for  (  int   count  =  0  ;  count  >=  0  &&  buffer  .  hasRemaining  (  )  ;  )  { 
count  =  fc  .  read  (  buffer  )  ; 
} 
buffer  .  flip  (  )  ; 
return   buffer  ; 
}  finally  { 
WWIO  .  closeStream  (  is  ,  file  .  getPath  (  )  )  ; 
} 
} 

public   static   ByteBuffer   inflateFileToBuffer  (  File   file  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
FileInputStream   is  =  new   FileInputStream  (  file  )  ; 
try  { 
return   inflateStreamToBuffer  (  is  )  ; 
}  finally  { 
WWIO  .  closeStream  (  is  ,  file  .  getPath  (  )  )  ; 
} 
} 

public   static   boolean   saveBufferToGZipFile  (  ByteBuffer   buffer  ,  File   file  )  throws   IOException  { 
return   saveBufferToStream  (  buffer  ,  new   GZIPOutputStream  (  new   FileOutputStream  (  file  )  )  )  ; 
} 

public   static   boolean   deflateBufferToFile  (  ByteBuffer   buffer  ,  File   file  )  throws   IOException  { 
return   saveBufferToStream  (  buffer  ,  new   DeflaterOutputStream  (  new   FileOutputStream  (  file  )  )  )  ; 
} 

public   static   ByteBuffer   readGZipFileToBuffer  (  File   gzFile  )  throws   IllegalArgumentException  ,  IOException  { 
if  (  gzFile  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  gzFile  .  exists  (  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.FileNotFound"  ,  gzFile  .  getAbsolutePath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   FileNotFoundException  (  message  )  ; 
} 
if  (  !  gzFile  .  canRead  (  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.FileNoReadPermission"  ,  gzFile  .  getAbsolutePath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IOException  (  message  )  ; 
} 
int   inflatedLength  =  gzipGetInflatedLength  (  gzFile  )  ; 
if  (  0  ==  inflatedLength  )  { 
String   message  =  Logging  .  getMessage  (  "generic.LengthIsInvalid"  ,  gzFile  .  getAbsolutePath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IOException  (  message  )  ; 
} 
ByteBuffer   buffer  =  null  ; 
GZIPInputStream   is  =  null  ; 
try  { 
is  =  new   GZIPInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  gzFile  )  )  )  ; 
buffer  =  transferStreamToByteBuffer  (  is  ,  inflatedLength  )  ; 
buffer  .  rewind  (  )  ; 
}  finally  { 
WWIO  .  closeStream  (  is  ,  gzFile  .  getPath  (  )  )  ; 
} 
return   buffer  ; 
} 

private   static   int   gzipGetInflatedLength  (  File   gzFile  )  throws   IOException  { 
RandomAccessFile   raf  =  null  ; 
int   length  =  0  ; 
try  { 
raf  =  new   RandomAccessFile  (  gzFile  ,  "r"  )  ; 
raf  .  seek  (  raf  .  length  (  )  -  4  )  ; 
int   b4  =  0xFF  &  raf  .  read  (  )  ; 
int   b3  =  0xFF  &  raf  .  read  (  )  ; 
int   b2  =  0xFF  &  raf  .  read  (  )  ; 
int   b1  =  0xFF  &  raf  .  read  (  )  ; 
length  =  (  b1  <<  24  )  |  (  b2  <<  16  )  +  (  b3  <<  8  )  +  b4  ; 
}  finally  { 
if  (  null  !=  raf  )  raf  .  close  (  )  ; 
} 
return   length  ; 
} 

public   static   ByteBuffer   readZipEntryToBuffer  (  File   zipFile  ,  String   entryName  )  throws   IOException  { 
if  (  zipFile  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
InputStream   is  =  null  ; 
ZipEntry   ze  =  null  ; 
try  { 
ZipFile   zf  =  new   ZipFile  (  zipFile  )  ; 
if  (  zf  .  size  (  )  <  1  )  { 
String   message  =  Logging  .  getMessage  (  "WWIO.ZipFileIsEmpty"  ,  zipFile  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   java  .  io  .  IOException  (  message  )  ; 
} 
if  (  entryName  !=  null  )  { 
ze  =  zf  .  getEntry  (  entryName  )  ; 
if  (  ze  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "WWIO.ZipFileEntryNIF"  ,  entryName  ,  zipFile  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IOException  (  message  )  ; 
} 
}  else  { 
Enumeration   entries  =  zf  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
ZipEntry   entry  =  (  ZipEntry  )  entries  .  nextElement  (  )  ; 
if  (  null  !=  entry  &&  !  entry  .  isDirectory  (  )  )  { 
ze  =  entry  ; 
break  ; 
} 
} 
if  (  null  ==  ze  )  { 
String   message  =  Logging  .  getMessage  (  "WWIO.ZipFileIsEmpty"  ,  zipFile  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   java  .  io  .  IOException  (  message  )  ; 
} 
} 
is  =  zf  .  getInputStream  (  ze  )  ; 
ByteBuffer   buffer  =  null  ; 
if  (  ze  .  getSize  (  )  >  0  )  { 
buffer  =  transferStreamToByteBuffer  (  is  ,  (  int  )  ze  .  getSize  (  )  )  ; 
buffer  .  rewind  (  )  ; 
} 
return   buffer  ; 
}  finally  { 
WWIO  .  closeStream  (  is  ,  entryName  )  ; 
} 
} 

private   static   ByteBuffer   transferStreamToByteBuffer  (  InputStream   stream  ,  int   numBytes  )  throws   IOException  { 
if  (  stream  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  numBytes  <  1  )  { 
Logging  .  logger  (  )  .  severe  (  "WWIO.NumberBytesTransferLessThanOne"  )  ; 
throw   new   IllegalArgumentException  (  Logging  .  getMessage  (  "WWIO.NumberBytesTransferLessThanOne"  )  )  ; 
} 
int   bytesRead  =  0  ; 
int   count  =  0  ; 
byte  [  ]  bytes  =  new   byte  [  numBytes  ]  ; 
while  (  count  >=  0  &&  (  numBytes  -  bytesRead  )  >  0  )  { 
count  =  stream  .  read  (  bytes  ,  bytesRead  ,  numBytes  -  bytesRead  )  ; 
if  (  count  >  0  )  { 
bytesRead  +=  count  ; 
} 
} 
ByteBuffer   buffer  =  BufferUtil  .  newByteBuffer  (  bytes  .  length  )  ; 
return   buffer  .  put  (  bytes  )  ; 
} 












public   static   ByteBuffer   readStreamToBuffer  (  InputStream   inputStream  )  throws   IOException  { 
if  (  inputStream  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   readStreamToBuffer  (  inputStream  ,  false  )  ; 
} 
















public   static   ByteBuffer   readStreamToBuffer  (  InputStream   inputStream  ,  boolean   allocateDirect  )  throws   IOException  { 
if  (  inputStream  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
ReadableByteChannel   channel  =  Channels  .  newChannel  (  inputStream  )  ; 
return   readChannelToBuffer  (  channel  ,  allocateDirect  )  ; 
} 













public   static   String   readStreamToString  (  InputStream   stream  ,  String   encoding  )  throws   IOException  { 
if  (  stream  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   readCharacterStreamToString  (  new   InputStreamReader  (  stream  ,  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  )  ; 
} 
















public   static   ByteBuffer   readChannelToBuffer  (  ReadableByteChannel   channel  ,  boolean   allocateDirect  )  throws   IOException  { 
if  (  channel  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ChannelIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
final   int   PAGE_SIZE  =  (  int  )  Math  .  round  (  Math  .  pow  (  2  ,  16  )  )  ; 
ByteBuffer   buffer  =  WWBufferUtil  .  newByteBuffer  (  PAGE_SIZE  ,  allocateDirect  )  ; 
int   count  =  0  ; 
while  (  count  >=  0  )  { 
count  =  channel  .  read  (  buffer  )  ; 
if  (  count  >  0  &&  !  buffer  .  hasRemaining  (  )  )  { 
ByteBuffer   biggerBuffer  =  allocateDirect  ?  ByteBuffer  .  allocateDirect  (  buffer  .  limit  (  )  +  PAGE_SIZE  )  :  ByteBuffer  .  allocate  (  buffer  .  limit  (  )  +  PAGE_SIZE  )  ; 
biggerBuffer  .  put  (  (  ByteBuffer  )  buffer  .  rewind  (  )  )  ; 
buffer  =  biggerBuffer  ; 
} 
} 
if  (  buffer  !=  null  )  buffer  .  flip  (  )  ; 
return   buffer  ; 
} 















public   static   ByteBuffer   readChannelToBuffer  (  ReadableByteChannel   channel  ,  ByteBuffer   buffer  )  throws   IOException  { 
if  (  channel  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ChannelIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  buffer  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.BufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
int   count  =  0  ; 
while  (  count  >=  0  &&  buffer  .  hasRemaining  (  )  )  { 
count  =  channel  .  read  (  buffer  )  ; 
} 
buffer  .  flip  (  )  ; 
return   buffer  ; 
} 














public   static   String   readChannelToString  (  ReadableByteChannel   channel  ,  String   encoding  )  throws   IOException  { 
if  (  channel  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ChannelIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   readCharacterStreamToString  (  Channels  .  newReader  (  channel  ,  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  )  ; 
} 












public   static   String   readCharacterStreamToString  (  Reader   reader  )  throws   IOException  { 
if  (  reader  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ReaderIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
BufferedReader   br  =  new   BufferedReader  (  reader  )  ; 
String   line  ; 
while  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   ByteBuffer   inflateStreamToBuffer  (  InputStream   inputStream  )  throws   IOException  { 
return   readStreamToBuffer  (  new   InflaterInputStream  (  inputStream  )  )  ; 
} 

public   static   String   replaceSuffix  (  String   in  ,  String   newSuffix  )  { 
if  (  in  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
String   suffix  =  newSuffix  !=  null  ?  newSuffix  :  ""  ; 
int   p  =  in  .  lastIndexOf  (  "."  )  ; 
return   p  >=  0  ?  in  .  substring  (  0  ,  p  )  +  suffix  :  in  +  suffix  ; 
} 

public   static   String   getSuffix  (  String   filePath  )  { 
if  (  filePath  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
int   len  =  filePath  .  length  (  )  ; 
int   p  =  filePath  .  lastIndexOf  (  "."  )  ; 
String   suffix  =  (  p  >=  0  &&  p  +  1  <  len  )  ?  filePath  .  substring  (  p  +  1  ,  len  )  :  null  ; 
if  (  null  !=  suffix  &&  p  >  0  &&  "gz"  .  equals  (  suffix  )  )  { 
int   idx  =  filePath  .  lastIndexOf  (  "."  ,  p  -  1  )  ; 
suffix  =  (  idx  >=  0  &&  idx  +  1  <  len  )  ?  filePath  .  substring  (  idx  +  1  ,  len  )  :  suffix  ; 
} 
return   suffix  ; 
} 











public   static   String   getFilename  (  String   filePath  )  { 
if  (  filePath  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
filePath  =  stripTrailingSeparator  (  filePath  )  ; 
int   len  =  filePath  .  length  (  )  ; 
int   p  =  filePath  .  lastIndexOf  (  "/"  )  ; 
if  (  p  <  0  )  p  =  filePath  .  lastIndexOf  (  "\\"  )  ; 
return  (  p  >=  0  &&  p  +  1  <  len  )  ?  filePath  .  substring  (  p  +  1  ,  len  )  :  null  ; 
} 










public   static   String   getParentFilePath  (  String   filePath  )  { 
if  (  filePath  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
filePath  =  stripTrailingSeparator  (  filePath  )  ; 
int   len  =  filePath  .  length  (  )  ; 
int   p  =  filePath  .  lastIndexOf  (  "/"  )  ; 
if  (  p  <  0  )  p  =  filePath  .  lastIndexOf  (  "\\"  )  ; 
return  (  p  >  0  &&  p  <  len  )  ?  filePath  .  substring  (  0  ,  p  )  :  null  ; 
} 













public   static   boolean   makeParentDirs  (  String   path  )  { 
if  (  path  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
String   fs  =  File  .  separator  ; 
String  [  ]  pathParts  =  path  .  split  (  "[/"  +  (  fs  .  equals  (  "/"  )  ?  ""  :  (  fs  .  equals  (  "\\"  )  ?  "\\\\"  :  fs  )  )  +  "]"  )  ; 
if  (  pathParts  .  length  <=  1  )  return   true  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  pathParts  .  length  -  1  ;  i  ++  )  { 
if  (  pathParts  [  i  ]  .  length  (  )  ==  0  )  continue  ; 
sb  .  append  (  File  .  separator  )  ; 
sb  .  append  (  pathParts  [  i  ]  )  ; 
} 
return  (  new   File  (  sb  .  toString  (  )  )  )  .  mkdirs  (  )  ; 
} 









public   static   File   makeTempDir  (  )  throws   IOException  { 
File   temp  =  File  .  createTempFile  (  "wwj"  ,  null  )  ; 
if  (  !  temp  .  delete  (  )  )  return   null  ; 
if  (  !  temp  .  mkdir  (  )  )  return   null  ; 
return   temp  ; 
} 

public   static   File   saveBufferToTempFile  (  ByteBuffer   buffer  ,  String   suffix  )  throws   IOException  { 
if  (  buffer  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ByteBufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
File   outputFile  =  java  .  io  .  File  .  createTempFile  (  "WorldWind"  ,  suffix  !=  null  ?  suffix  :  ""  )  ; 
outputFile  .  deleteOnExit  (  )  ; 
buffer  .  rewind  (  )  ; 
WWIO  .  saveBuffer  (  buffer  ,  outputFile  )  ; 
return   outputFile  ; 
} 

public   static   boolean   isFileOutOfDate  (  URL   url  ,  long   expiryTime  )  { 
if  (  url  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.URLIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
URI   uri  =  url  .  toURI  (  )  ; 
if  (  uri  .  isOpaque  (  )  )  return   false  ; 
File   file  =  new   File  (  uri  )  ; 
return   file  .  exists  (  )  &&  file  .  lastModified  (  )  <  expiryTime  ; 
}  catch  (  URISyntaxException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  SEVERE  ,  "WWIO.ExceptionValidatingFileExpiration"  ,  url  )  ; 
return   false  ; 
} 
} 

public   static   Proxy   configureProxy  (  )  { 
String   proxyHost  =  Configuration  .  getStringValue  (  AVKey  .  URL_PROXY_HOST  )  ; 
if  (  proxyHost  ==  null  )  return   null  ; 
Proxy   proxy  =  null  ; 
try  { 
int   proxyPort  =  Configuration  .  getIntegerValue  (  AVKey  .  URL_PROXY_PORT  )  ; 
String   proxyType  =  Configuration  .  getStringValue  (  AVKey  .  URL_PROXY_TYPE  )  ; 
SocketAddress   addr  =  new   InetSocketAddress  (  proxyHost  ,  proxyPort  )  ; 
if  (  proxyType  .  equals  (  "Proxy.Type.Http"  )  )  proxy  =  new   Proxy  (  Proxy  .  Type  .  HTTP  ,  addr  )  ;  else   if  (  proxyType  .  equals  (  "Proxy.Type.SOCKS"  )  )  proxy  =  new   Proxy  (  Proxy  .  Type  .  SOCKS  ,  addr  )  ; 
}  catch  (  Exception   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  Logging  .  getMessage  (  "URLRetriever.ErrorConfiguringProxy"  ,  proxyHost  )  ,  e  )  ; 
} 
return   proxy  ; 
} 












public   static   boolean   isContentType  (  File   file  ,  String  ...  mimeTypes  )  { 
if  (  file  ==  null  ||  mimeTypes  ==  null  )  return   false  ; 
for  (  String   mimeType  :  mimeTypes  )  { 
if  (  mimeType  ==  null  )  continue  ; 
String   typeSuffix  =  WWIO  .  makeSuffixForMimeType  (  mimeType  )  ; 
String   fileSuffix  =  WWIO  .  getSuffix  (  file  .  getName  (  )  )  ; 
if  (  fileSuffix  ==  null  ||  typeSuffix  ==  null  )  continue  ; 
if  (  !  fileSuffix  .  startsWith  (  "."  )  )  fileSuffix  =  "."  +  fileSuffix  ; 
if  (  fileSuffix  .  equalsIgnoreCase  (  typeSuffix  )  )  return   true  ; 
} 
return   false  ; 
} 











public   static   String   makeSuffixForMimeType  (  String   mimeType  )  { 
if  (  mimeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ImageFomat"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
if  (  !  mimeType  .  contains  (  "/"  )  ||  mimeType  .  endsWith  (  "/"  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.InvalidImageFormat"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
int   paramIndex  =  mimeType  .  indexOf  (  ";"  )  ; 
if  (  paramIndex  !=  -  1  )  mimeType  =  mimeType  .  substring  (  0  ,  paramIndex  )  ; 
String   suffix  =  mimeTypeToSuffixMap  .  get  (  mimeType  )  ; 
if  (  suffix  ==  null  )  suffix  =  mimeType  .  substring  (  mimeType  .  lastIndexOf  (  "/"  )  +  1  )  ; 
suffix  =  suffix  .  replaceFirst  (  "bil32"  ,  "bil"  )  ; 
suffix  =  suffix  .  replaceFirst  (  "bil16"  ,  "bil"  )  ; 
return  "."  +  suffix  ; 
} 










public   static   String   makeMimeTypeForSuffix  (  String   suffix  )  { 
if  (  suffix  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FormatSuffixIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
if  (  suffix  .  startsWith  (  "."  )  )  suffix  =  suffix  .  substring  (  1  ,  suffix  .  length  (  )  )  ; 
return   suffixToMimeTypeMap  .  get  (  suffix  )  ; 
} 

protected   static   Map  <  String  ,  String  >  mimeTypeToSuffixMap  =  new   HashMap  <  String  ,  String  >  (  )  ; 

protected   static   Map  <  String  ,  String  >  suffixToMimeTypeMap  =  new   HashMap  <  String  ,  String  >  (  )  ; 

static  { 
mimeTypeToSuffixMap  .  put  (  "application/acad"  ,  "dwg"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/bil"  ,  "bil"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/bil16"  ,  "bil"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/bil32"  ,  "bil"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/dxf"  ,  "dxf"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/octet-stream"  ,  "bin"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/pdf"  ,  "pdf"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/rss+xml"  ,  "xml"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/rtf"  ,  "rtf"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/sla"  ,  "slt"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/vnd.google-earth.kml+xml"  ,  "kml"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/vnd.google-earth.kmz"  ,  "kmz"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/vnd.ogc.gml+xml"  ,  "gml"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/x-gzip"  ,  "gz"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/xml"  ,  "xml"  )  ; 
mimeTypeToSuffixMap  .  put  (  "application/zip"  ,  "zip"  )  ; 
mimeTypeToSuffixMap  .  put  (  "multipart/zip"  ,  "zip"  )  ; 
mimeTypeToSuffixMap  .  put  (  "multipart/x-gzip"  ,  "gzip"  )  ; 
mimeTypeToSuffixMap  .  put  (  "model/collada+xml"  ,  "dae"  )  ; 
mimeTypeToSuffixMap  .  put  (  "text/html"  ,  "html"  )  ; 
mimeTypeToSuffixMap  .  put  (  "text/plain"  ,  "txt"  )  ; 
mimeTypeToSuffixMap  .  put  (  "text/richtext"  ,  "rtx"  )  ; 
mimeTypeToSuffixMap  .  put  (  "text/tab-separated-values"  ,  "tsv"  )  ; 
mimeTypeToSuffixMap  .  put  (  "text/xml"  ,  "xml"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/bmp"  ,  "bmp"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/dds"  ,  "dds"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/geotiff"  ,  "gtif"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/gif"  ,  "gif"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/jp2"  ,  "jp2"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/jpeg"  ,  "jpg"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/jpg"  ,  "jpg"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/png"  ,  "png"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/svg+xml"  ,  "svg"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/tiff"  ,  "tif"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/x-imagewebserver-ecw"  ,  "ecw"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/x-mrsid"  ,  "sid"  )  ; 
mimeTypeToSuffixMap  .  put  (  "image/x-rgb"  ,  "rgb"  )  ; 
mimeTypeToSuffixMap  .  put  (  "video/mpeg"  ,  "mpg"  )  ; 
mimeTypeToSuffixMap  .  put  (  "video/quicktime"  ,  "mov"  )  ; 
mimeTypeToSuffixMap  .  put  (  "audio/x-aiff"  ,  "aif"  )  ; 
mimeTypeToSuffixMap  .  put  (  "audio/x-midi"  ,  "mid"  )  ; 
mimeTypeToSuffixMap  .  put  (  "audio/x-wav"  ,  "wav"  )  ; 
mimeTypeToSuffixMap  .  put  (  "world/x-vrml"  ,  "wrl"  )  ; 
suffixToMimeTypeMap  .  put  (  "aif"  ,  "audio/x-aiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "aifc"  ,  "audio/x-aiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "aiff"  ,  "audio/x-aiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "bil"  ,  "application/bil"  )  ; 
suffixToMimeTypeMap  .  put  (  "bil16"  ,  "application/bil16"  )  ; 
suffixToMimeTypeMap  .  put  (  "bil32"  ,  "application/bil32"  )  ; 
suffixToMimeTypeMap  .  put  (  "bin"  ,  "application/octet-stream"  )  ; 
suffixToMimeTypeMap  .  put  (  "bmp"  ,  "image/bmp"  )  ; 
suffixToMimeTypeMap  .  put  (  "dds"  ,  "image/dds"  )  ; 
suffixToMimeTypeMap  .  put  (  "dwg"  ,  "application/acad"  )  ; 
suffixToMimeTypeMap  .  put  (  "dxf"  ,  "application/dxf"  )  ; 
suffixToMimeTypeMap  .  put  (  "ecw"  ,  "image/x-imagewebserver-ecw"  )  ; 
suffixToMimeTypeMap  .  put  (  "gif"  ,  "image/gif"  )  ; 
suffixToMimeTypeMap  .  put  (  "gml"  ,  "application/vnd.ogc.gml+xml"  )  ; 
suffixToMimeTypeMap  .  put  (  "gtif"  ,  "image/geotiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "gz"  ,  "application/x-gzip"  )  ; 
suffixToMimeTypeMap  .  put  (  "gzip"  ,  "multipart/x-gzip"  )  ; 
suffixToMimeTypeMap  .  put  (  "htm"  ,  "text/html"  )  ; 
suffixToMimeTypeMap  .  put  (  "html"  ,  "text/html"  )  ; 
suffixToMimeTypeMap  .  put  (  "jp2"  ,  "image/jp2"  )  ; 
suffixToMimeTypeMap  .  put  (  "jpeg"  ,  "image/jpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "jpg"  ,  "image/jpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "kml"  ,  "application/vnd.google-earth.kml+xml"  )  ; 
suffixToMimeTypeMap  .  put  (  "kmz"  ,  "application/vnd.google-earth.kmz"  )  ; 
suffixToMimeTypeMap  .  put  (  "mid"  ,  "audio/x-midi"  )  ; 
suffixToMimeTypeMap  .  put  (  "midi"  ,  "audio/x-midi"  )  ; 
suffixToMimeTypeMap  .  put  (  "mov"  ,  "video/quicktime"  )  ; 
suffixToMimeTypeMap  .  put  (  "mp3"  ,  "audio/x-mpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "mpe"  ,  "video/mpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "mpeg"  ,  "video/mpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "mpg"  ,  "video/mpeg"  )  ; 
suffixToMimeTypeMap  .  put  (  "pdf"  ,  "application/pdf"  )  ; 
suffixToMimeTypeMap  .  put  (  "png"  ,  "image/png"  )  ; 
suffixToMimeTypeMap  .  put  (  "rgb"  ,  "image/x-rgb"  )  ; 
suffixToMimeTypeMap  .  put  (  "rtf"  ,  "application/rtf"  )  ; 
suffixToMimeTypeMap  .  put  (  "rtx"  ,  "text/richtext"  )  ; 
suffixToMimeTypeMap  .  put  (  "sid"  ,  "image/x-mrsid"  )  ; 
suffixToMimeTypeMap  .  put  (  "slt"  ,  "application/sla"  )  ; 
suffixToMimeTypeMap  .  put  (  "svg"  ,  "image/svg+xml"  )  ; 
suffixToMimeTypeMap  .  put  (  "tif"  ,  "image/tiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "tiff"  ,  "image/tiff"  )  ; 
suffixToMimeTypeMap  .  put  (  "tsv"  ,  "text/tab-separated-values"  )  ; 
suffixToMimeTypeMap  .  put  (  "txt"  ,  "text/plain"  )  ; 
suffixToMimeTypeMap  .  put  (  "wav"  ,  "audio/x-wav"  )  ; 
suffixToMimeTypeMap  .  put  (  "wbmp"  ,  "image/vnd.wap.wbmp"  )  ; 
suffixToMimeTypeMap  .  put  (  "wrl"  ,  "world/x-vrml"  )  ; 
suffixToMimeTypeMap  .  put  (  "xml"  ,  "application/xml"  )  ; 
suffixToMimeTypeMap  .  put  (  "zip"  ,  "application/zip"  )  ; 
} 















public   static   String   makeDataTypeForMimeType  (  String   mimeType  )  { 
if  (  mimeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.MimeTypeIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  mimeType  .  contains  (  "/"  )  ||  mimeType  .  endsWith  (  "/"  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.InvalidImageFormat"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  mimeType  .  equals  (  "application/bil32"  )  )  return   AVKey  .  FLOAT32  ;  else   if  (  mimeType  .  equals  (  "application/bil16"  )  )  return   AVKey  .  INT16  ;  else   if  (  mimeType  .  equals  (  "application/bil"  )  )  return   AVKey  .  INT16  ;  else   if  (  mimeType  .  equals  (  "image/bil"  )  )  return   AVKey  .  INT16  ; 
return   null  ; 
} 

public   static   Object   getFileOrResourceAsStream  (  String   path  ,  Class   c  )  { 
if  (  path  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FilePathIsNull"  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
File   file  =  new   File  (  path  )  ; 
if  (  file  .  exists  (  )  )  { 
try  { 
return   new   FileInputStream  (  file  )  ; 
}  catch  (  Exception   e  )  { 
return   e  ; 
} 
} 
if  (  c  ==  null  )  c  =  WWIO  .  class  ; 
try  { 
return   c  .  getResourceAsStream  (  "/"  +  path  )  ; 
}  catch  (  Exception   e  )  { 
return   e  ; 
} 
} 











public   static   InputStream   getInputStreamFromString  (  String   string  )  { 
return   getInputStreamFromString  (  string  ,  DEFAULT_CHARACTER_ENCODING  )  ; 
} 












public   static   InputStream   getInputStreamFromString  (  String   string  ,  String   encoding  )  { 
if  (  string  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
try  { 
return   new   ByteArrayInputStream  (  string  .  getBytes  (  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   WWRuntimeException  (  e  )  ; 
} 
} 











public   static   InputStream   getInputStreamFromByteBuffer  (  ByteBuffer   buffer  )  { 
if  (  buffer  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ByteBufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  buffer  .  hasArray  (  )  &&  buffer  .  limit  (  )  ==  buffer  .  capacity  (  )  )  return   new   ByteArrayInputStream  (  buffer  .  array  (  )  )  ; 
byte  [  ]  byteArray  =  new   byte  [  buffer  .  limit  (  )  ]  ; 
buffer  .  get  (  byteArray  )  ; 
return   new   ByteArrayInputStream  (  byteArray  )  ; 
} 










public   static   BufferedInputStream   getBufferedInputStream  (  InputStream   is  )  { 
if  (  is  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return  (  is   instanceof   BufferedInputStream  &&  BufferedInputStream  .  class  .  equals  (  is  .  getClass  (  )  )  )  ?  (  BufferedInputStream  )  is  :  new   BufferedInputStream  (  is  )  ; 
} 

public   static   boolean   isAncestorOf  (  File   file  ,  File   ancestor  )  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  ancestor  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.AncestorIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
File   cur  =  file  ; 
while  (  cur  !=  null  &&  !  cur  .  equals  (  ancestor  )  )  { 
cur  =  cur  .  getParentFile  (  )  ; 
} 
return   cur  !=  null  ; 
} 

public   static   void   copyFile  (  File   source  ,  File   destination  )  throws   IOException  { 
if  (  source  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  destination  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.DestinationIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
FileInputStream   fis  =  null  ; 
FileOutputStream   fos  =  null  ; 
FileChannel   fic  ,  foc  ; 
try  { 
fis  =  new   FileInputStream  (  source  )  ; 
fic  =  fis  .  getChannel  (  )  ; 
fos  =  new   FileOutputStream  (  destination  )  ; 
foc  =  fos  .  getChannel  (  )  ; 
foc  .  transferFrom  (  fic  ,  0  ,  fic  .  size  (  )  )  ; 
fos  .  flush  (  )  ; 
fis  .  close  (  )  ; 
fos  .  close  (  )  ; 
}  finally  { 
WWIO  .  closeStream  (  fis  ,  source  .  getPath  (  )  )  ; 
WWIO  .  closeStream  (  fos  ,  destination  .  getPath  (  )  )  ; 
} 
} 

public   static   void   copyDirectory  (  File   source  ,  File   destination  ,  boolean   copySubDirectories  )  throws   IOException  { 
if  (  source  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  destination  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.DestinationIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  destination  .  exists  (  )  )  destination  .  mkdirs  (  )  ; 
if  (  !  destination  .  exists  (  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.CannotCreateFile"  ,  destination  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IOException  (  message  )  ; 
} 
File  [  ]  fileList  =  source  .  listFiles  (  )  ; 
if  (  fileList  ==  null  )  return  ; 
List  <  File  >  childFiles  =  new   ArrayList  <  File  >  (  )  ; 
List  <  File  >  childDirs  =  new   ArrayList  <  File  >  (  )  ; 
for  (  File   child  :  fileList  )  { 
if  (  child  ==  null  )  continue  ; 
if  (  child  .  isDirectory  (  )  )  childDirs  .  add  (  child  )  ;  else   childFiles  .  add  (  child  )  ; 
} 
for  (  File   childFile  :  childFiles  )  { 
File   destFile  =  new   File  (  destination  ,  childFile  .  getName  (  )  )  ; 
copyFile  (  childFile  ,  destFile  )  ; 
} 
if  (  copySubDirectories  )  { 
for  (  File   childDir  :  childDirs  )  { 
File   destDir  =  new   File  (  destination  ,  childDir  .  getName  (  )  )  ; 
copyDirectory  (  childDir  ,  destDir  ,  copySubDirectories  )  ; 
} 
} 
} 

@  SuppressWarnings  (  {  "ResultOfMethodCallIgnored"  }  ) 
public   static   void   deleteDirectory  (  File   file  )  throws   IOException  { 
if  (  file  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
File  [  ]  fileList  =  file  .  listFiles  (  )  ; 
if  (  fileList  !=  null  )  { 
List  <  File  >  childFiles  =  new   ArrayList  <  File  >  (  )  ; 
List  <  File  >  childDirs  =  new   ArrayList  <  File  >  (  )  ; 
for  (  File   child  :  fileList  )  { 
if  (  child  ==  null  )  continue  ; 
if  (  child  .  isDirectory  (  )  )  childDirs  .  add  (  child  )  ;  else   childFiles  .  add  (  child  )  ; 
} 
for  (  File   childFile  :  childFiles  )  { 
childFile  .  delete  (  )  ; 
} 
for  (  File   childDir  :  childDirs  )  { 
deleteDirectory  (  childDir  )  ; 
} 
} 
} 








public   static   void   closeStream  (  Object   stream  ,  String   name  )  { 
if  (  stream  ==  null  )  return  ; 
try  { 
if  (  stream   instanceof   Closeable  )  { 
(  (  Closeable  )  stream  )  .  close  (  )  ; 
}  else  { 
String   message  =  Logging  .  getMessage  (  "WWIO.StreamTypeNotSupported"  ,  name  !=  null  ?  name  :  "Unknown"  )  ; 
Logging  .  logger  (  )  .  warning  (  message  )  ; 
} 
}  catch  (  IOException   e  )  { 
String   message  =  Logging  .  getMessage  (  "generic.ExceptionClosingStream"  ,  e  ,  name  !=  null  ?  name  :  "Unknown"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
} 
} 










public   static   String   readTextFile  (  File   file  )  { 
if  (  file  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
BufferedReader   reader  =  null  ; 
try  { 
reader  =  new   BufferedReader  (  new   FileReader  (  file  )  )  ; 
String   line  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  )  ; 
} 
}  catch  (  IOException   e  )  { 
String   msg  =  Logging  .  getMessage  (  "generic.ExceptionAttemptingToReadFile"  ,  file  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  msg  )  ; 
return   null  ; 
}  finally  { 
WWIO  .  closeStream  (  reader  ,  file  .  getPath  (  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 









public   static   void   writeTextFile  (  String   text  ,  File   file  )  { 
if  (  file  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
if  (  text  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
BufferedWriter   writer  =  null  ; 
try  { 
writer  =  new   BufferedWriter  (  new   FileWriter  (  file  )  )  ; 
writer  .  write  (  text  )  ; 
}  catch  (  IOException   e  )  { 
String   msg  =  Logging  .  getMessage  (  "generic.ExceptionAttemptingToWriteTo"  ,  file  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  msg  )  ; 
}  finally  { 
WWIO  .  closeStream  (  writer  ,  file  .  getPath  (  )  )  ; 
} 
} 













public   static   InputStream   openFileOrResourceStream  (  String   fileName  ,  Class   c  )  { 
if  (  fileName  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
Object   streamOrException  =  WWIO  .  getFileOrResourceAsStream  (  fileName  ,  c  )  ; 
if  (  streamOrException   instanceof   Exception  )  { 
String   msg  =  Logging  .  getMessage  (  "generic.CannotOpenFile"  ,  fileName  )  ; 
throw   new   WWRuntimeException  (  msg  ,  (  Exception  )  streamOrException  )  ; 
} 
return  (  InputStream  )  streamOrException  ; 
} 















public   static   String   byteBufferToString  (  ByteBuffer   buffer  ,  String   encoding  )  { 
if  (  buffer  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.BufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
return   Charset  .  forName  (  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  .  decode  (  buffer  )  .  toString  (  )  ; 
} 
















public   static   String   byteBufferToString  (  ByteBuffer   buffer  ,  int   length  ,  String   encoding  )  { 
if  (  buffer  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.BufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
if  (  length  <  1  )  { 
String   msg  =  Logging  .  getMessage  (  "generic.LengthIsInvalid"  ,  length  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
CharBuffer   charBuffer  =  Charset  .  forName  (  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  .  decode  (  buffer  )  ; 
if  (  charBuffer  .  remaining  (  )  >  length  )  { 
charBuffer  =  charBuffer  .  slice  (  )  ; 
charBuffer  .  limit  (  length  )  ; 
} 
return   charBuffer  .  toString  (  )  ; 
} 











public   static   ByteBuffer   stringToByteBuffer  (  String   string  ,  String   encoding  )  throws   UnsupportedEncodingException  { 
if  (  string  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.StringIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
return   ByteBuffer  .  wrap  (  string  .  getBytes  (  encoding  !=  null  ?  encoding  :  DEFAULT_CHARACTER_ENCODING  )  )  ; 
} 













public   static   java  .  io  .  Reader   openReader  (  Object   src  )  throws   java  .  io  .  IOException  { 
java  .  io  .  Reader   r  =  null  ; 
if  (  src   instanceof   java  .  io  .  Reader  )  r  =  (  java  .  io  .  Reader  )  src  ;  else   if  (  src   instanceof   java  .  io  .  InputStream  )  r  =  new   java  .  io  .  InputStreamReader  (  (  java  .  io  .  InputStream  )  src  )  ;  else   if  (  src   instanceof   java  .  io  .  File  )  r  =  new   java  .  io  .  FileReader  (  (  java  .  io  .  File  )  src  )  ;  else   if  (  src   instanceof   java  .  net  .  URL  )  r  =  new   java  .  io  .  InputStreamReader  (  (  (  java  .  net  .  URL  )  src  )  .  openStream  (  )  )  ;  else   if  (  src   instanceof   String  )  r  =  new   java  .  io  .  StringReader  (  (  String  )  src  )  ; 
return   r  ; 
} 














public   static   InputStream   openStream  (  Object   src  )  throws   Exception  { 
if  (  src  ==  null  ||  WWUtil  .  isEmpty  (  src  )  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  src   instanceof   InputStream  )  { 
return  (  InputStream  )  src  ; 
}  else   if  (  src   instanceof   URL  )  { 
return  (  (  URL  )  src  )  .  openStream  (  )  ; 
}  else   if  (  src   instanceof   URI  )  { 
return  (  (  URI  )  src  )  .  toURL  (  )  .  openStream  (  )  ; 
}  else   if  (  src   instanceof   File  )  { 
Object   streamOrException  =  getFileOrResourceAsStream  (  (  (  File  )  src  )  .  getPath  (  )  ,  null  )  ; 
if  (  streamOrException   instanceof   Exception  )  { 
throw  (  Exception  )  streamOrException  ; 
} 
return  (  InputStream  )  streamOrException  ; 
}  else   if  (  !  (  src   instanceof   String  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.UnrecognizedSourceType"  ,  src  .  toString  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
String   sourceName  =  (  String  )  src  ; 
URL   url  =  WWIO  .  makeURL  (  sourceName  )  ; 
if  (  url  !=  null  )  return   url  .  openStream  (  )  ; 
Object   streamOrException  =  getFileOrResourceAsStream  (  sourceName  ,  null  )  ; 
if  (  streamOrException   instanceof   Exception  )  { 
throw  (  Exception  )  streamOrException  ; 
} 
return  (  InputStream  )  streamOrException  ; 
} 












public   static   String   getSourcePath  (  Object   src  )  { 
if  (  src  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
String   s  =  null  ; 
if  (  src   instanceof   java  .  io  .  File  )  s  =  (  (  java  .  io  .  File  )  src  )  .  getAbsolutePath  (  )  ;  else   if  (  src   instanceof   java  .  net  .  URL  )  s  =  (  (  java  .  net  .  URL  )  src  )  .  toExternalForm  (  )  ;  else   if  (  src   instanceof   java  .  net  .  URI  )  s  =  src  .  toString  (  )  ;  else   if  (  src   instanceof   String  )  s  =  (  String  )  src  ; 
return   s  ; 
} 











public   static   URL   makeURL  (  String   path  )  { 
try  { 
return   new   URL  (  path  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 











public   static   URL   makeURL  (  Object   path  )  { 
try  { 
URI   uri  =  makeURI  (  path  )  ; 
return   uri  !=  null  ?  uri  .  toURL  (  )  :  null  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 













public   static   URL   makeURL  (  Object   path  ,  String   defaultProtocol  )  { 
try  { 
URL   url  =  makeURL  (  path  )  ; 
if  (  url  ==  null  &&  !  WWUtil  .  isEmpty  (  path  .  toString  (  )  )  &&  !  WWUtil  .  isEmpty  (  defaultProtocol  )  )  url  =  new   URL  (  defaultProtocol  ,  null  ,  path  .  toString  (  )  )  ; 
return   url  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 












public   static   URI   makeURI  (  Object   path  )  { 
try  { 
if  (  path   instanceof   String  )  return   new   URI  (  (  String  )  path  )  ;  else   if  (  path   instanceof   File  )  return  (  (  File  )  path  )  .  toURI  (  )  ;  else   if  (  path   instanceof   URL  )  return  (  (  URL  )  path  )  .  toURI  (  )  ;  else   return   null  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 














public   static   String  [  ]  listChildFilenames  (  File   file  ,  FileFilter   filter  )  { 
if  (  file  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
String  [  ]  names  =  file  .  list  (  )  ; 
if  (  names  ==  null  )  return   null  ; 
ArrayList  <  String  >  matches  =  new   ArrayList  <  String  >  (  )  ; 
for  (  String   filename  :  names  )  { 
if  (  filename  ==  null  ||  filename  .  length  (  )  ==  0  )  continue  ; 
if  (  filter  !=  null  &&  !  filter  .  accept  (  new   File  (  file  ,  filename  )  )  )  continue  ; 
matches  .  add  (  filename  )  ; 
} 
return   matches  .  toArray  (  new   String  [  matches  .  size  (  )  ]  )  ; 
} 















public   static   String  [  ]  listDescendantFilenames  (  File   file  ,  FileFilter   filter  )  { 
if  (  file  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
return   listDescendantFilenames  (  file  ,  filter  ,  true  )  ; 
} 


















public   static   String  [  ]  listDescendantFilenames  (  File   file  ,  FileFilter   filter  ,  boolean   recurseAfterMatch  )  { 
if  (  file  ==  null  )  { 
String   msg  =  Logging  .  getMessage  (  "nullValue.FileIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  msg  )  ; 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 
if  (  file  .  list  (  )  ==  null  )  return   null  ; 
ArrayList  <  String  >  matches  =  new   ArrayList  <  String  >  (  )  ; 
listDescendantFilenames  (  file  ,  null  ,  filter  ,  recurseAfterMatch  ,  matches  )  ; 
return   matches  .  toArray  (  new   String  [  matches  .  size  (  )  ]  )  ; 
} 

protected   static   void   listDescendantFilenames  (  File   parent  ,  String   pathname  ,  FileFilter   filter  ,  boolean   recurseAfterMatch  ,  Collection  <  String  >  matches  )  { 
File   file  =  (  pathname  !=  null  )  ?  new   File  (  parent  ,  pathname  )  :  parent  ; 
String  [  ]  names  =  file  .  list  (  )  ; 
if  (  names  ==  null  )  return  ; 
boolean   haveMatch  =  false  ; 
for  (  String   filename  :  names  )  { 
if  (  filename  ==  null  ||  filename  .  length  (  )  ==  0  )  continue  ; 
if  (  filter  !=  null  &&  !  filter  .  accept  (  new   File  (  file  ,  filename  )  )  )  continue  ; 
matches  .  add  (  appendPathPart  (  pathname  ,  filename  )  )  ; 
haveMatch  =  true  ; 
} 
if  (  haveMatch  &&  !  recurseAfterMatch  )  return  ; 
for  (  String   filename  :  names  )  { 
listDescendantFilenames  (  parent  ,  appendPathPart  (  pathname  ,  filename  )  ,  filter  ,  recurseAfterMatch  ,  matches  )  ; 
} 
} 










public   static   void   skipBytes  (  InputStream   is  ,  int   numBytes  )  throws   IOException  { 
if  (  is  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
int   byteSkipped  =  0  ; 
while  (  byteSkipped  <  numBytes  )  { 
byteSkipped  +=  is  .  skip  (  numBytes  -  byteSkipped  )  ; 
} 
} 

public   static   String  [  ]  makeCachePathForURL  (  URL   url  )  { 
String   cacheDir  =  WWIO  .  replaceIllegalFileNameCharacters  (  url  .  getHost  (  )  )  ; 
String   fileName  =  WWIO  .  replaceIllegalFileNameCharacters  (  url  .  getPath  (  )  )  ; 
return   new   String  [  ]  {  cacheDir  ,  fileName  }  ; 
} 

public   static   void   reverseFloatArray  (  int   pos  ,  int   count  ,  float  [  ]  array  )  { 
if  (  pos  <  0  )  { 
String   message  =  Logging  .  getMessage  (  "generic.ArgumentOutOfRange"  ,  "pos="  +  pos  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  count  <  0  )  { 
String   message  =  Logging  .  getMessage  (  "generic.ArgumentOutOfRange"  ,  "count="  +  count  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  array  ==  null  )  { 
String   message  =  "nullValue.ArrayIsNull"  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  array  .  length  <  (  pos  +  count  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.ArrayInvalidLength"  ,  "points.length < "  +  (  pos  +  count  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
float   tmp  ; 
int   i  ,  j  ,  mid  ; 
for  (  i  =  0  ,  mid  =  count  >  >  1  ,  j  =  count  -  1  ;  i  <  mid  ;  i  ++  ,  j  --  )  { 
tmp  =  array  [  pos  +  i  ]  ; 
array  [  pos  +  i  ]  =  array  [  pos  +  j  ]  ; 
array  [  pos  +  j  ]  =  tmp  ; 
} 
} 









public   static   boolean   isLocalJarAddress  (  URL   jarUrl  )  { 
return   jarUrl  !=  null  &&  jarUrl  .  getFile  (  )  .  startsWith  (  "file:"  )  ; 
} 
} 

