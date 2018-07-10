package   org  .  pprun  .  common  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 







public   abstract   class   FileUtils  { 







public   static   byte  [  ]  readFile  (  File   file  )  throws   IOException  { 
if  (  !  file  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "No such file '"  +  file  .  getAbsoluteFile  (  )  +  "'."  )  ; 
}  else   if  (  file  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  "File '"  +  file  .  getAbsoluteFile  (  )  +  "' is a directory.  Cannot read."  )  ; 
} 
InputStream   stream  =  new   FileInputStream  (  file  )  ; 
try  { 
return   StreamUtils  .  readStream  (  stream  )  ; 
}  finally  { 
stream  .  close  (  )  ; 
} 
} 







public   static   String   readFileAsString  (  File   file  )  throws   IOException  { 
if  (  !  file  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "No such file '"  +  file  .  getAbsoluteFile  (  )  +  "'."  )  ; 
}  else   if  (  file  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  "File '"  +  file  .  getAbsoluteFile  (  )  +  "' is a directory.  Cannot read."  )  ; 
} 
InputStream   stream  =  new   FileInputStream  (  file  )  ; 
try  { 
return   StreamUtils  .  readStreamAsString  (  stream  )  ; 
}  finally  { 
stream  .  close  (  )  ; 
} 
} 

public   static   void   writeFile  (  byte  [  ]  bytes  ,  File   file  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  "File '"  +  file  .  getAbsoluteFile  (  )  +  "' is an existing directory.  Cannot write."  )  ; 
} 
FileOutputStream   stream  =  new   FileOutputStream  (  file  )  ; 
try  { 
stream  .  write  (  bytes  )  ; 
stream  .  flush  (  )  ; 
}  finally  { 
stream  .  close  (  )  ; 
} 
} 

public   static   void   copyFile  (  String   from  ,  String   to  )  throws   IOException  { 
File   fromFile  =  new   File  (  from  )  ; 
File   toFile  =  new   File  (  to  )  ; 
writeFile  (  readFile  (  fromFile  )  ,  toFile  )  ; 
} 






public   static   String   getPath  (  String   dir  )  { 
String   path  =  dir  ; 
if  (  path  !=  null  &&  !  path  .  endsWith  (  File  .  separator  )  )  { 
path  +=  File  .  separator  ; 
} 
return   path  ; 
} 







public   static   String   getPath  (  String   dir  ,  String   separator  )  { 
String   path  =  dir  ; 
if  (  path  !=  null  &&  !  path  .  endsWith  (  separator  )  &&  !  path  .  endsWith  (  File  .  separator  )  )  { 
path  +=  separator  ; 
} 
return   path  ; 
} 








public   static   File   ensureDirExist  (  String   path  )  { 
File   dir  =  new   File  (  path  )  ; 
if  (  dir  .  exists  (  )  )  { 
if  (  !  dir  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  "Existing file with same name for the directory already exists"  )  ; 
} 
}  else  { 
if  (  !  dir  .  mkdirs  (  )  )  { 
throw   new   IllegalArgumentException  (  "Cannot create directory : "  +  path  )  ; 
} 
} 
return   dir  ; 
} 







public   static   File   getNewFile  (  String   path  ,  String   fileName  ,  String   extension  )  { 
if  (  extension  !=  null  &&  extension  .  isEmpty  (  )  ==  false  )  { 
extension  =  "."  +  extension  ; 
}  else  { 
extension  =  ""  ; 
} 
File   file  =  new   File  (  path  +  File  .  separator  +  fileName  +  extension  )  ; 
int   i  =  0  ; 
while  (  file  .  exists  (  )  )  { 
file  =  new   File  (  path  +  File  .  separator  +  fileName  +  "_"  +  ++  i  +  extension  )  ; 
} 
return   file  ; 
} 








public   static   void   zip  (  File   fromFile  ,  File   toFile  )  throws   IOException  { 
zip  (  new   FileInputStream  (  fromFile  )  ,  toFile  )  ; 
} 








public   static   void   zip  (  InputStream   is  ,  File   toFile  )  throws   IOException  { 
zip  (  is  ,  toFile  ,  true  )  ; 
} 








public   static   void   zip  (  InputStream   is  ,  File   toFile  ,  boolean   closeInputStream  )  throws   IOException  { 
final   int   BUFFER_SIZE  =  2048  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  is  ,  BUFFER_SIZE  )  ; 
try  { 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  toFile  )  )  )  ; 
try  { 
ZipEntry   entry  =  new   ZipEntry  (  toFile  .  getName  (  )  )  ; 
out  .  putNextEntry  (  entry  )  ; 
byte   data  [  ]  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   count  ; 
while  (  (  count  =  in  .  read  (  data  ,  0  ,  BUFFER_SIZE  )  )  !=  -  1  )  { 
out  .  write  (  data  ,  0  ,  count  )  ; 
} 
}  finally  { 
out  .  close  (  )  ; 
} 
}  finally  { 
if  (  closeInputStream  )  { 
in  .  close  (  )  ; 
} 
} 
} 







public   static   void   copyFile  (  File   in  ,  File   out  )  throws   IOException  { 
FileChannel   sourceChannel  =  new   FileInputStream  (  in  )  .  getChannel  (  )  ; 
try  { 
FileChannel   destinationChannel  =  new   FileOutputStream  (  out  )  .  getChannel  (  )  ; 
try  { 
sourceChannel  .  transferTo  (  0  ,  sourceChannel  .  size  (  )  ,  destinationChannel  )  ; 
}  finally  { 
destinationChannel  .  close  (  )  ; 
} 
}  finally  { 
sourceChannel  .  close  (  )  ; 
} 
} 
} 






abstract   class   StreamUtils  { 










public   static   byte  [  ]  readStream  (  InputStream   stream  )  throws   IOException  { 
ByteArrayOutputStream   bytesOut  =  new   ByteArrayOutputStream  (  )  ; 
byte  [  ]  byteBuf  =  new   byte  [  1024  ]  ; 
int   readCount  =  0  ; 
while  (  (  readCount  =  stream  .  read  (  byteBuf  )  )  !=  -  1  )  { 
bytesOut  .  write  (  byteBuf  ,  0  ,  readCount  )  ; 
} 
return   bytesOut  .  toByteArray  (  )  ; 
} 










public   static   String   readStreamAsString  (  InputStream   stream  )  throws   IOException  { 
return   new   String  (  readStream  (  stream  )  ,  Charset  .  forName  (  CommonUtil  .  UTF8  )  )  ; 
} 

public   static   byte  [  ]  readFile  (  File   file  )  throws   IOException  { 
InputStream   stream  =  new   FileInputStream  (  file  )  ; 
try  { 
return   readStream  (  stream  )  ; 
}  finally  { 
stream  .  close  (  )  ; 
} 
} 

public   static   void   writeFile  (  File   file  ,  byte  [  ]  data  )  throws   IOException  { 
OutputStream   stream  =  new   FileOutputStream  (  file  )  ; 
try  { 
stream  .  write  (  data  )  ; 
}  finally  { 
try  { 
stream  .  flush  (  )  ; 
}  finally  { 
stream  .  close  (  )  ; 
} 
} 
} 

public   static   String   readStream  (  Reader   stream  )  throws   IOException  { 
StringBuilder   streamString  =  new   StringBuilder  (  )  ; 
char  [  ]  readBuffer  =  new   char  [  256  ]  ; 
int   readCount  =  0  ; 
while  (  (  readCount  =  stream  .  read  (  readBuffer  )  )  !=  -  1  )  { 
streamString  .  append  (  readBuffer  ,  0  ,  readCount  )  ; 
} 
return   streamString  .  toString  (  )  ; 
} 










public   static   boolean   compareCharStreams  (  InputStream   s1  ,  InputStream   s2  )  { 
StringBuffer   s1Buf  ,  s2Buf  ; 
try  { 
s1Buf  =  trimLines  (  s1  )  ; 
s2Buf  =  trimLines  (  s2  )  ; 
return   s1Buf  .  toString  (  )  .  equals  (  s2Buf  .  toString  (  )  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   false  ; 
} 










public   static   boolean   compareCharStreams  (  Reader   s1  ,  Reader   s2  )  { 
StringBuffer   s1Buf  ,  s2Buf  ; 
try  { 
s1Buf  =  trimLines  (  s1  )  ; 
s2Buf  =  trimLines  (  s2  )  ; 
return   s1Buf  .  toString  (  )  .  equals  (  s2Buf  .  toString  (  )  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   false  ; 
} 










public   static   boolean   compareCharStreams  (  String   s1  ,  String   s2  )  { 
return   compareCharStreams  (  new   StringReader  (  s1  )  ,  new   StringReader  (  s2  )  )  ; 
} 








public   static   StringBuffer   trimLines  (  Reader   charStream  )  throws   IOException  { 
StringBuffer   stringBuf  =  new   StringBuffer  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  charStream  )  ; 
String   line  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
stringBuf  .  append  (  line  .  trim  (  )  )  ; 
} 
return   stringBuf  ; 
} 








public   static   StringBuffer   trimLines  (  InputStream   charStream  )  throws   IOException  { 
return   trimLines  (  new   InputStreamReader  (  charStream  ,  CommonUtil  .  UTF8  )  )  ; 
} 
} 

