package   easyJ  .  http  .  upload  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 






















public   class   MultipartIterator  { 




private   static   final   String   DEFAULT_ENCODING  =  "iso-8859-1"  ; 




private   static   final   int   TEXT_BUFFER_SIZE  =  1000  ; 




public   static   String   HEADER_CONTENT_TYPE  =  "Content-Type"  ; 




public   static   final   String   HEADER_CONTENT_DISPOSITION  =  "Content-Disposition"  ; 





public   static   final   String   MESSAGE_CANNOT_RETRIEVE_BOUNDARY  =  "MultipartIterator: cannot retrieve boundary for multipart request"  ; 

private   static   final   String   PARAMETER_BOUNDARY  =  "boundary="  ; 

private   static   final   String   FILE_PREFIX  =  "strts"  ; 




protected   HttpServletRequest   request  ; 




protected   MultipartBoundaryInputStream   inputStream  ; 




protected   String   boundary  ; 




protected   long   maxSize  =  -  1  ; 




protected   int   contentLength  ; 




protected   int   diskBufferSize  =  2  *  10240  ; 





protected   int   bufferSize  =  4096  ; 




protected   String   tempDir  ; 




protected   String   contentType  ; 




protected   boolean   maxLengthExceeded  ; 








public   MultipartIterator  (  HttpServletRequest   request  )  throws   IOException  { 
this  (  request  ,  -  1  )  ; 
} 











public   MultipartIterator  (  HttpServletRequest   request  ,  int   bufferSize  )  throws   IOException  { 
this  (  request  ,  bufferSize  ,  -  1  )  ; 
} 














public   MultipartIterator  (  HttpServletRequest   request  ,  int   bufferSize  ,  long   maxSize  )  throws   IOException  { 
this  (  request  ,  bufferSize  ,  maxSize  ,  null  )  ; 
} 

public   MultipartIterator  (  HttpServletRequest   request  ,  int   bufferSize  ,  long   maxSize  ,  String   tempDir  )  throws   IOException  { 
this  .  request  =  request  ; 
this  .  maxSize  =  maxSize  ; 
if  (  bufferSize  >  -  1  )  { 
this  .  bufferSize  =  bufferSize  ; 
} 
if  (  tempDir  !=  null  )  { 
this  .  tempDir  =  tempDir  ; 
}  else  { 
this  .  tempDir  =  System  .  getProperty  (  "java.io.tmpdir"  )  ; 
} 
this  .  maxLengthExceeded  =  false  ; 
this  .  inputStream  =  new   MultipartBoundaryInputStream  (  )  ; 
parseRequest  (  )  ; 
} 




protected   void   parseRequest  (  )  throws   IOException  { 
getContentTypeOfRequest  (  )  ; 
this  .  contentLength  =  this  .  request  .  getContentLength  (  )  ; 
getBoundaryFromContentType  (  )  ; 
this  .  inputStream  .  setMaxLength  (  this  .  contentLength  +  1  )  ; 
if  (  (  this  .  maxSize  >  -  1  )  &&  (  this  .  contentLength  >  this  .  maxSize  )  )  { 
this  .  maxLengthExceeded  =  true  ; 
}  else  { 
InputStream   requestInputStream  =  this  .  request  .  getInputStream  (  )  ; 
if  (  requestInputStream  .  markSupported  (  )  )  { 
requestInputStream  .  mark  (  contentLength  +  1  )  ; 
} 
this  .  inputStream  .  setBoundary  (  this  .  boundary  )  ; 
this  .  inputStream  .  setInputStream  (  requestInputStream  )  ; 
} 
} 











public   MultipartElement   getNextElement  (  )  throws   IOException  { 
MultipartElement   element  =  null  ; 
if  (  !  isMaxLengthExceeded  (  )  )  { 
if  (  !  this  .  inputStream  .  isFinalBoundaryEncountered  (  )  )  { 
if  (  this  .  inputStream  .  isElementFile  (  )  )  { 
element  =  createFileMultipartElement  (  )  ; 
}  else  { 
String   encoding  =  getElementEncoding  (  )  ; 
element  =  createTextMultipartElement  (  encoding  )  ; 
} 
this  .  inputStream  .  resetForNextBoundary  (  )  ; 
} 
} 
return   element  ; 
} 




protected   String   getElementEncoding  (  )  { 
String   encoding  =  this  .  inputStream  .  getElementCharset  (  )  ; 
if  (  encoding  ==  null  )  { 
encoding  =  this  .  request  .  getCharacterEncoding  (  )  ; 
if  (  encoding  ==  null  )  { 
encoding  =  DEFAULT_ENCODING  ; 
} 
} 
return   encoding  ; 
} 







protected   MultipartElement   createTextMultipartElement  (  String   encoding  )  throws   IOException  { 
MultipartElement   element  ; 
int   read  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  TEXT_BUFFER_SIZE  ]  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
while  (  (  read  =  this  .  inputStream  .  read  (  buffer  ,  0  ,  TEXT_BUFFER_SIZE  )  )  >  0  )  { 
baos  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
String   value  =  baos  .  toString  (  encoding  )  ; 
element  =  new   MultipartElement  (  this  .  inputStream  .  getElementName  (  )  ,  value  )  ; 
return   element  ; 
} 




protected   MultipartElement   createFileMultipartElement  (  )  throws   IOException  { 
MultipartElement   element  ; 
File   elementFile  =  createLocalFile  (  )  ; 
element  =  new   MultipartElement  (  this  .  inputStream  .  getElementName  (  )  ,  this  .  inputStream  .  getElementFileName  (  )  ,  this  .  inputStream  .  getElementContentType  (  )  ,  elementFile  )  ; 
return   element  ; 
} 






public   void   setBufferSize  (  int   bufferSize  )  { 
this  .  bufferSize  =  bufferSize  ; 
} 






public   int   getBufferSize  (  )  { 
return   bufferSize  ; 
} 








public   void   setMaxSize  (  long   maxSize  )  { 
this  .  maxSize  =  maxSize  ; 
} 






public   long   getMaxSize  (  )  { 
return   this  .  maxSize  ; 
} 




public   boolean   isMaxLengthExceeded  (  )  { 
return  (  this  .  maxLengthExceeded  ||  this  .  inputStream  .  isMaxLengthMet  (  )  )  ; 
} 




private   final   void   getBoundaryFromContentType  (  )  throws   IOException  { 
if  (  this  .  contentType  .  lastIndexOf  (  PARAMETER_BOUNDARY  )  !=  -  1  )  { 
String   _boundary  =  this  .  contentType  .  substring  (  this  .  contentType  .  lastIndexOf  (  PARAMETER_BOUNDARY  )  +  9  )  ; 
if  (  _boundary  .  endsWith  (  "\n"  )  )  { 
this  .  boundary  =  _boundary  .  substring  (  0  ,  _boundary  .  length  (  )  -  1  )  ; 
} 
this  .  boundary  =  _boundary  ; 
}  else  { 
this  .  boundary  =  null  ; 
} 
if  (  (  this  .  boundary  ==  null  )  ||  (  this  .  boundary  .  length  (  )  <  1  )  )  { 
throw   new   IOException  (  MESSAGE_CANNOT_RETRIEVE_BOUNDARY  )  ; 
} 
} 




private   final   void   getContentTypeOfRequest  (  )  { 
this  .  contentType  =  request  .  getContentType  (  )  ; 
if  (  this  .  contentType  ==  null  )  { 
this  .  contentType  =  this  .  request  .  getHeader  (  HEADER_CONTENT_TYPE  )  ; 
} 
} 




protected   File   createLocalFile  (  )  throws   IOException  { 
File   tempFile  =  File  .  createTempFile  (  FILE_PREFIX  ,  null  ,  new   File  (  this  .  tempDir  )  )  ; 
BufferedOutputStream   fos  =  new   BufferedOutputStream  (  new   FileOutputStream  (  tempFile  )  ,  this  .  diskBufferSize  )  ; 
int   read  =  0  ; 
byte   buffer  [  ]  =  new   byte  [  this  .  diskBufferSize  ]  ; 
while  (  (  read  =  this  .  inputStream  .  read  (  buffer  ,  0  ,  this  .  diskBufferSize  )  )  >  0  )  { 
fos  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
return   tempFile  ; 
} 
} 

