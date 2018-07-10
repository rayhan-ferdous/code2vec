package   com  .  volantis  .  synergetics  .  io  ; 

import   com  .  volantis  .  synergetics  .  localization  .  ExceptionLocalizer  ; 
import   com  .  volantis  .  synergetics  .  localization  .  LocalizationFactory  ; 
import   com  .  volantis  .  synergetics  .  log  .  LogDispatcher  ; 
import   org  .  jdom  .  Document  ; 
import   org  .  jdom  .  output  .  XMLOutputter  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  net  .  URL  ; 




public   abstract   class   IOUtils  { 




private   static   final   ExceptionLocalizer   EXCEPTION_LOCALIZER  =  LocalizationFactory  .  createExceptionLocalizer  (  IOUtils  .  class  )  ; 




private   static   final   LogDispatcher   logger  =  LocalizationFactory  .  createLogger  (  IOUtils  .  class  )  ; 




private   IOUtils  (  )  { 
} 




public   static   String   getExtension  (  String   filename  )  { 
String   ext  =  null  ; 
int   index  =  filename  .  lastIndexOf  (  '.'  )  ; 
if  (  index  >  0  &&  index  <  filename  .  length  (  )  -  1  )  { 
ext  =  filename  .  substring  (  index  +  1  )  .  toLowerCase  (  )  ; 
} 
return   ext  ; 
} 




public   static   String   getExtension  (  File   f  )  { 
return   getExtension  (  f  .  getName  (  )  )  ; 
} 






public   static   void   deleteDirectoryContents  (  File   directory  )  { 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  +=  1  )  { 
File   file  =  files  [  i  ]  ; 
if  (  file  .  isDirectory  (  )  )  { 
deleteDirectoryContents  (  file  )  ; 
} 
file  .  delete  (  )  ; 
} 
} 








public   static   void   copyFiles  (  File   source  ,  File   target  )  throws   IOException  { 
FileInputStream   input  =  new   FileInputStream  (  source  )  ; 
FileOutputStream   output  =  new   FileOutputStream  (  target  )  ; 
copyAndClose  (  input  ,  output  )  ; 
} 












public   static   boolean   copyDirectoryContent  (  File   srcDir  ,  File   dstDir  )  throws   IOException  ,  IllegalArgumentException  { 
return   copyDirectoryContent  (  srcDir  ,  dstDir  ,  false  )  ; 
} 















public   static   boolean   copyDirectoryContent  (  File   srcDir  ,  File   dstDir  ,  boolean   preserveModificationTime  )  throws   IOException  ,  IllegalArgumentException  { 
if  (  !  srcDir  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  EXCEPTION_LOCALIZER  .  format  (  "file-is-not-directory"  ,  srcDir  )  )  ; 
} 
if  (  !  dstDir  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  EXCEPTION_LOCALIZER  .  format  (  "file-is-not-directory"  ,  dstDir  )  )  ; 
} 
File  [  ]  children  =  srcDir  .  listFiles  (  )  ; 
boolean   result  =  true  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  &&  result  ;  i  ++  )  { 
File   child  =  new   File  (  dstDir  ,  children  [  i  ]  .  getName  (  )  )  ; 
if  (  children  [  i  ]  .  isDirectory  (  )  )  { 
result  =  child  .  mkdir  (  )  ; 
if  (  result  )  { 
result  =  copyDirectoryContent  (  children  [  i  ]  ,  child  ,  preserveModificationTime  )  ; 
} 
}  else  { 
result  =  child  .  createNewFile  (  )  ; 
if  (  result  )  { 
copyFileChannel  (  children  [  i  ]  ,  child  ,  preserveModificationTime  )  ; 
} 
} 
if  (  preserveModificationTime  &&  result  )  { 
child  .  setLastModified  (  children  [  i  ]  .  lastModified  (  )  )  ; 
} 
} 
return   result  ; 
} 









public   static   void   copyFileChannel  (  File   src  ,  File   dst  )  throws   IOException  { 
copyFileChannel  (  src  ,  dst  ,  false  )  ; 
} 











public   static   void   copyFileChannel  (  File   src  ,  File   dst  ,  boolean   preserveModificationTime  )  throws   IOException  { 
FileChannel   inputChannel  =  null  ; 
FileChannel   outputChannel  =  null  ; 
long   length  =  0  ; 
try  { 
inputChannel  =  new   FileInputStream  (  src  )  .  getChannel  (  )  ; 
length  =  inputChannel  .  size  (  )  ; 
outputChannel  =  new   FileOutputStream  (  dst  )  .  getChannel  (  )  ; 
long   total  =  0  ; 
while  (  total  <  length  )  { 
total  +=  inputChannel  .  transferTo  (  0  ,  length  ,  outputChannel  )  ; 
} 
if  (  preserveModificationTime  )  { 
dst  .  setLastModified  (  src  .  lastModified  (  )  )  ; 
} 
}  finally  { 
if  (  inputChannel  !=  null  )  { 
inputChannel  .  close  (  )  ; 
} 
if  (  outputChannel  !=  null  )  { 
outputChannel  .  close  (  )  ; 
} 
} 
} 










public   static   void   copyAndClose  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
IOException   copyException  =  null  ; 
RuntimeException   runtimeException  =  null  ; 
IOException   closeException  =  null  ; 
try  { 
copy  (  in  ,  out  )  ; 
}  catch  (  IOException   e  )  { 
copyException  =  e  ; 
}  catch  (  RuntimeException   e  )  { 
runtimeException  =  e  ; 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "unexpected-ioexception"  ,  e  )  ; 
if  (  closeException  ==  null  )  { 
closeException  =  e  ; 
} 
} 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "unexpected-ioexception"  ,  e  )  ; 
if  (  closeException  ==  null  )  { 
closeException  =  e  ; 
} 
} 
if  (  copyException  !=  null  )  { 
throw   copyException  ; 
} 
if  (  runtimeException  !=  null  )  { 
throw   runtimeException  ; 
} 
if  (  closeException  !=  null  )  { 
throw   closeException  ; 
} 
} 
} 










public   static   int   copy  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte   buf  [  ]  =  new   byte  [  1024  ]  ; 
int   len  ; 
int   totalBytesCopied  =  0  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
totalBytesCopied  +=  len  ; 
} 
out  .  flush  (  )  ; 
return   totalBytesCopied  ; 
} 











public   static   boolean   inputStreamsEqual  (  InputStream   is1  ,  InputStream   is2  )  throws   IOException  { 
byte   b1  [  ]  =  new   byte  [  1024  ]  ; 
byte   b2  [  ]  =  new   byte  [  1024  ]  ; 
int   count  ; 
boolean   equals  ; 
do  { 
count  =  is1  .  read  (  b1  ,  0  ,  1024  )  ; 
is2  .  read  (  b2  ,  0  ,  1024  )  ; 
equals  =  Arrays  .  equals  (  b1  ,  b2  )  ; 
}  while  (  equals  &&  count  !=  -  1  )  ; 
return   equals  ; 
} 







public   static   InputStream   createDocumentInputStream  (  Document   document  )  { 
XMLOutputter   out  =  new   XMLOutputter  (  )  ; 
String   docString  =  out  .  outputString  (  document  )  ; 
return   new   ByteArrayInputStream  (  docString  .  getBytes  (  )  )  ; 
} 







public   static   void   closeQuietly  (  InputStream   is  )  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  warn  (  "problem-closing-quietly"  ,  is  ,  e  )  ; 
} 
} 
} 







public   static   void   closeQuietly  (  OutputStream   os  )  { 
if  (  os  !=  null  )  { 
try  { 
os  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  warn  (  "problem-closing-quietly"  ,  os  ,  e  )  ; 
} 
} 
} 







public   static   void   closeQuietly  (  Reader   reader  )  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  warn  (  "problem-closing-quietly"  ,  reader  ,  e  )  ; 
} 
} 
} 







public   static   void   closeQuietly  (  Writer   writer  )  { 
if  (  writer  !=  null  )  { 
try  { 
writer  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  warn  (  "problem-closing-quietly"  ,  writer  ,  e  )  ; 
} 
} 
} 















public   static   File   extractTempZipFromJarFile  (  Class   clazz  ,  String   zipFilename  ,  String   suffix  )  throws   Exception  { 
URL   url  =  clazz  .  getResource  (  zipFilename  )  ; 
InputStream   in  =  url  .  openConnection  (  )  .  getInputStream  (  )  ; 
File   file  =  File  .  createTempFile  (  "testZipFile"  ,  "."  +  suffix  ,  new   File  (  System  .  getProperty  (  "java.io.tmpdir"  )  )  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  file  )  ; 
byte   buf  [  ]  =  new   byte  [  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
file  .  deleteOnExit  (  )  ; 
return   file  ; 
} 








public   static   void   deleteDir  (  File   dir  )  throws   IllegalStateException  { 
String   list  [  ]  =  dir  .  list  (  )  ; 
if  (  list  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  list  .  length  ;  i  ++  )  { 
File   file  =  new   File  (  dir  ,  list  [  i  ]  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
deleteDir  (  file  )  ; 
}  else   if  (  !  file  .  delete  (  )  )  { 
throw   new   IllegalStateException  (  "Could not delete test file: "  +  file  .  getAbsolutePath  (  )  )  ; 
} 
} 
if  (  !  dir  .  delete  (  )  )  { 
throw   new   IllegalStateException  (  "Could not delete test directory: "  +  dir  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 











public   static   File   createDirectory  (  File   tempDir  )  throws   IllegalStateException  { 
if  (  tempDir  .  exists  (  )  )  { 
if  (  !  tempDir  .  delete  (  )  )  { 
throw   new   IllegalStateException  (  "Could not delete temporary test file: "  +  tempDir  )  ; 
} 
} 
if  (  !  tempDir  .  mkdirs  (  )  )  { 
throw   new   IllegalStateException  (  "Could not create temporary test directory: "  +  tempDir  )  ; 
} 
return   tempDir  ; 
} 
} 

