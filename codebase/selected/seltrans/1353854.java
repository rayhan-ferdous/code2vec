package   org  .  loon  .  framework  .  game  .  simple  .  utils  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  CharArrayReader  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   org  .  loon  .  framework  .  game  .  simple  .  core  .  LSystem  ; 
import   org  .  loon  .  framework  .  game  .  simple  .  core  .  resource  .  LRAFile  ; 
import   org  .  loon  .  framework  .  game  .  simple  .  core  .  resource  .  Resources  ; 





















public   final   class   FileUtils   extends   CompressionUtils  { 








public   static   void   write  (  String   fileName  ,  String   context  )  throws   IOException  { 
write  (  fileName  ,  context  ,  false  )  ; 
} 








public   static   void   write  (  File   file  ,  String   context  ,  String   coding  )  throws   IOException  { 
write  (  file  ,  context  .  getBytes  (  coding  )  ,  false  )  ; 
} 








public   static   void   write  (  String   fileName  ,  String   context  ,  boolean   append  )  throws   IOException  { 
write  (  new   File  (  fileName  )  ,  context  .  getBytes  (  LSystem  .  encoding  )  ,  append  )  ; 
} 








public   static   void   write  (  File   file  ,  byte  [  ]  bytes  )  throws   IOException  { 
write  (  file  ,  new   ByteArrayInputStream  (  bytes  )  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  byte  [  ]  bytes  ,  boolean   append  )  throws   IOException  { 
write  (  file  ,  new   ByteArrayInputStream  (  bytes  )  ,  append  )  ; 
} 








public   static   void   write  (  File   file  ,  InputStream   input  )  throws   IOException  { 
write  (  file  ,  input  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  InputStream   input  ,  boolean   append  )  throws   IOException  { 
makedirs  (  file  )  ; 
BufferedOutputStream   output  =  null  ; 
try  { 
int   contentLength  =  input  .  available  (  )  ; 
output  =  new   BufferedOutputStream  (  new   FileOutputStream  (  file  ,  append  )  )  ; 
while  (  contentLength  --  >  0  )  { 
output  .  write  (  input  .  read  (  )  )  ; 
} 
}  finally  { 
close  (  input  )  ; 
close  (  output  )  ; 
} 
} 








public   static   void   write  (  File   file  ,  char  [  ]  chars  )  throws   IOException  { 
write  (  file  ,  new   CharArrayReader  (  chars  )  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  char  [  ]  chars  ,  boolean   append  )  throws   IOException  { 
write  (  file  ,  new   CharArrayReader  (  chars  )  ,  append  )  ; 
} 








public   static   void   write  (  File   file  ,  String   string  )  throws   IOException  { 
write  (  file  ,  new   CharArrayReader  (  string  .  toCharArray  (  )  )  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  String   string  ,  boolean   append  )  throws   IOException  { 
write  (  file  ,  new   CharArrayReader  (  string  .  toCharArray  (  )  )  ,  append  )  ; 
} 








public   static   void   write  (  File   file  ,  Reader   reader  )  throws   IOException  { 
write  (  file  ,  reader  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  Reader   reader  ,  boolean   append  )  throws   IOException  { 
makedirs  (  file  )  ; 
BufferedWriter   writer  =  null  ; 
try  { 
writer  =  new   BufferedWriter  (  new   FileWriter  (  file  ,  append  )  )  ; 
int   i  =  -  1  ; 
while  (  (  i  =  reader  .  read  (  )  )  !=  -  1  )  { 
writer  .  write  (  i  )  ; 
} 
}  finally  { 
close  (  reader  )  ; 
close  (  writer  )  ; 
} 
} 








public   static   void   write  (  File   file  ,  List   records  )  throws   IOException  { 
write  (  file  ,  records  ,  false  )  ; 
} 









public   static   void   write  (  File   file  ,  List   records  ,  boolean   append  )  throws   IOException  { 
makedirs  (  file  )  ; 
BufferedWriter   writer  =  null  ; 
try  { 
writer  =  new   BufferedWriter  (  new   FileWriter  (  file  ,  append  )  )  ; 
for  (  Iterator   it  =  records  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
writer  .  write  (  (  String  )  it  .  next  (  )  )  ; 
writer  .  write  (  LSystem  .  LS  )  ; 
} 
}  finally  { 
close  (  writer  )  ; 
} 
} 







public   static   void   makedirs  (  String   fileName  )  throws   IOException  { 
makedirs  (  new   File  (  fileName  )  )  ; 
} 







public   static   void   makedirs  (  File   file  )  throws   IOException  { 
checkFile  (  file  )  ; 
File   parentFile  =  file  .  getParentFile  (  )  ; 
if  (  parentFile  !=  null  )  { 
if  (  !  parentFile  .  exists  (  )  &&  !  parentFile  .  mkdirs  (  )  )  { 
throw   new   IOException  (  "Creating directories "  +  parentFile  .  getPath  (  )  +  " failed."  )  ; 
} 
} 
} 







public   static   String   cutFileName  (  String   str  )  { 
if  (  str  ==  null  )  return   null  ; 
str  =  cutDC  (  str  )  ; 
int   idx  ; 
idx  =  str  .  lastIndexOf  (  "\\"  )  ; 
if  (  (  idx  +  1  )  <  str  .  length  (  )  )  { 
str  =  str  .  substring  (  idx  +  1  )  ; 
} 
idx  =  str  .  lastIndexOf  (  "/"  )  ; 
if  (  (  idx  +  1  )  <  str  .  length  (  )  )  { 
str  =  str  .  substring  (  idx  +  1  )  ; 
} 
return   str  ; 
} 







private   static   String   cutDC  (  String   str  )  { 
if  (  str  ==  null  )  return   null  ; 
if  (  str  .  equals  (  "\"\""  )  )  return  ""  ; 
if  (  str  .  length  (  )  >  1  )  { 
if  (  str  .  substring  (  0  ,  1  )  .  equals  (  "\""  )  )  { 
str  =  str  .  substring  (  1  )  ; 
} 
} 
if  (  str  .  length  (  )  >  1  )  { 
if  (  str  .  substring  (  str  .  length  (  )  -  1  )  .  equals  (  "\""  )  )  { 
str  =  str  .  substring  (  0  ,  str  .  length  (  )  -  1  )  ; 
} 
} 
return   str  ; 
} 







private   static   void   checkFile  (  File   file  )  throws   IOException  { 
boolean   exists  =  file  .  exists  (  )  ; 
if  (  exists  &&  !  file  .  isFile  (  )  )  { 
throw   new   IOException  (  "File "  +  file  .  getPath  (  )  +  " is actually not a file."  )  ; 
} 
} 







public   static   void   reName  (  String   oldName  ,  String   newName  )  { 
FileUtils  .  reName  (  new   File  (  oldName  )  ,  new   File  (  newName  )  )  ; 
} 







public   static   void   reName  (  File   oldFile  ,  File   newFile  )  { 
try  { 
checkFile  (  oldFile  )  ; 
checkFile  (  newFile  )  ; 
oldFile  .  renameTo  (  newFile  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Change the file name fails!"  ,  e  )  ; 
} 
} 







public   static   void   close  (  InputStream   in  )  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
closingFailed  (  e  )  ; 
} 
} 
} 







public   static   void   close  (  OutputStream   output  )  { 
if  (  output  !=  null  )  { 
try  { 
output  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
closingFailed  (  e  )  ; 
} 
} 
} 







public   static   void   close  (  Reader   reader  )  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
closingFailed  (  e  )  ; 
} 
} 
} 







public   static   void   close  (  Writer   writer  )  { 
if  (  writer  !=  null  )  { 
try  { 
writer  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
closingFailed  (  e  )  ; 
} 
} 
} 







public   static   void   closingFailed  (  IOException   e  )  { 
throw   new   RuntimeException  (  e  .  getMessage  (  )  )  ; 
} 










public   static   long   copy  (  InputStream   is  ,  OutputStream   os  ,  long   len  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
long   copied  =  0  ; 
int   read  ; 
while  (  (  read  =  is  .  read  (  buf  )  )  !=  0  &&  copied  <  len  )  { 
long   leftToCopy  =  len  -  copied  ; 
int   toWrite  =  read  <  leftToCopy  ?  read  :  (  int  )  leftToCopy  ; 
os  .  write  (  buf  ,  0  ,  toWrite  )  ; 
copied  +=  toWrite  ; 
} 
return   copied  ; 
} 









public   static   long   copy  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
long   written  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
while  (  true  )  { 
int   len  =  in  .  read  (  buffer  )  ; 
if  (  len  <  0  )  { 
break  ; 
} 
out  .  write  (  buffer  ,  0  ,  len  )  ; 
written  +=  len  ; 
} 
return   written  ; 
} 








public   static   void   skipForSure  (  InputStream   is  ,  long   len  )  throws   IOException  { 
long   leftToSkip  =  len  ; 
while  (  leftToSkip  >  0  )  { 
long   skiped  =  is  .  skip  (  leftToSkip  )  ; 
leftToSkip  -=  skiped  ; 
} 
} 









public   static   byte  [  ]  read  (  InputStream   is  ,  long   len  )  throws   IOException  { 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
copy  (  is  ,  out  ,  len  )  ; 
return   out  .  toByteArray  (  )  ; 
} 








public   static   String   getName  (  String   name  ,  String   ext  )  { 
return  (  name  +  "."  +  ext  )  .  intern  (  )  ; 
} 







public   static   String   getNoExtensionName  (  String   name  )  { 
if  (  name  .  indexOf  (  "."  )  ==  -  1  )  return   name  ;  else   return   name  .  substring  (  0  ,  name  .  lastIndexOf  (  getExtension  (  name  )  )  -  1  )  ; 
} 







public   static   long   getKB  (  File   file  )  { 
return   getKB  (  file  .  length  (  )  )  ; 
} 







public   static   long   getKB  (  long   size  )  { 
size  /=  1000L  ; 
if  (  size  ==  0L  )  { 
size  =  1L  ; 
} 
return   size  ; 
} 










public   static   final   boolean   copyFile  (  final   String   sSource  ,  final   String   sDest  ,  final   String   srcEncoding  ,  final   String   destEncoding  )  { 
try  { 
File   src  =  new   File  (  sSource  )  ; 
if  (  !  src  .  exists  (  )  )  { 
return   false  ; 
} 
File   dest  =  new   File  (  sDest  )  ; 
String   reuslt  =  FileUtils  .  readFile  (  src  ,  srcEncoding  )  ; 
FileUtils  .  write  (  dest  ,  reuslt  ,  destEncoding  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 








public   static   final   boolean   copyFile  (  final   String   sSource  ,  final   String   sDest  )  { 
try  { 
File   src  =  new   File  (  sSource  )  ; 
if  (  !  src  .  exists  (  )  )  { 
return   false  ; 
} 
File   dest  =  new   File  (  sDest  )  ; 
FileUtils  .  makedirs  (  dest  )  ; 
InputStream   in  =  new   BufferedInputStream  (  new   FileInputStream  (  src  )  )  ; 
OutputStream   os  =  new   BufferedOutputStream  (  new   FileOutputStream  (  dest  )  )  ; 
byte  [  ]  buffer  =  new   byte  [  8  *  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  >  0  )  { 
os  .  write  (  buffer  ,  0  ,  len  )  ; 
} 
in  .  close  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 







public   static   void   copyFiles  (  String   listFile  ,  String   targetFloder  )  { 
BufferedReader   reader  =  null  ; 
try  { 
reader  =  new   BufferedReader  (  new   FileReader  (  listFile  )  )  ; 
String   tempString  =  null  ; 
while  (  (  tempString  =  reader  .  readLine  (  )  )  !=  null  )  { 
copyFile  (  tempString  ,  targetFloder  )  ; 
} 
reader  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
}  finally  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
} 
} 
} 







public   static   boolean   deleteAll  (  File   dir  )  { 
String   fileNames  [  ]  =  dir  .  list  (  )  ; 
if  (  fileNames  ==  null  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  fileNames  .  length  ;  i  ++  )  { 
File   file  =  new   File  (  dir  ,  fileNames  [  i  ]  )  ; 
if  (  file  .  isFile  (  )  )  file  .  delete  (  )  ;  else   if  (  file  .  isDirectory  (  )  )  deleteAll  (  file  )  ; 
} 
return   dir  .  delete  (  )  ; 
} 









public   static   String   readFile  (  String   filePath  ,  String   encode  )  throws   IOException  { 
return   readFile  (  new   File  (  filePath  )  ,  encode  )  ; 
} 









public   static   String   readFile  (  File   file  ,  String   encode  )  throws   IOException  { 
BufferedReader   reader  =  null  ; 
InputStream   in  =  null  ; 
String   s  ; 
try  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  in  ,  encode  )  ; 
reader  =  new   BufferedReader  (  isr  )  ; 
s  =  read  (  reader  )  ; 
}  finally  { 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
in  =  null  ; 
} 
}  catch  (  IOException   ioexception  )  { 
} 
try  { 
if  (  reader  !=  null  )  { 
reader  .  close  (  )  ; 
reader  =  null  ; 
} 
}  catch  (  IOException   ioexception1  )  { 
} 
} 
return   s  ; 
} 








public   static   byte  [  ]  readBytesFromFile  (  File   file  )  throws   IOException  { 
InputStream   is  =  new   DataInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
long   length  =  file  .  length  (  )  ; 
byte  [  ]  bytes  =  new   byte  [  (  int  )  length  ]  ; 
int   offset  =  0  ; 
int   numRead  =  0  ; 
while  (  offset  <  bytes  .  length  &&  (  numRead  =  is  .  read  (  bytes  ,  offset  ,  bytes  .  length  -  offset  )  )  >=  0  )  { 
offset  +=  numRead  ; 
} 
if  (  offset  <  bytes  .  length  )  { 
throw   new   IOException  (  "Could not completely read file "  +  file  .  getName  (  )  )  ; 
} 
is  .  close  (  )  ; 
return   bytes  ; 
} 







public   static   byte  [  ]  readBytesFromFile  (  String   fileName  )  { 
try  { 
return   readBytesFromFile  (  new   File  (  fileName  )  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 








public   static   final   String   readToString  (  InputStream   inputStream  ,  String   encoding  )  { 
try  { 
byte  [  ]  buffer  =  Resources  .  getDataSource  (  inputStream  )  ; 
return   new   String  (  buffer  ,  encoding  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 







public   static   final   String   readToString  (  InputStream   inputStream  )  { 
return   readToString  (  inputStream  ,  LSystem  .  encoding  )  ; 
} 







public   static   Object  [  ]  readPath  (  String   fileName  )  { 
try  { 
int   len  =  0  ; 
File   file  =  new   File  (  fileName  )  ; 
InputStream   is  =  null  ; 
if  (  file  .  exists  (  )  )  { 
is  =  new   DataInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
len  =  (  int  )  file  .  length  (  )  ; 
}  else  { 
URL   url  =  null  ; 
URLConnection   uc  =  null  ; 
uc  =  url  .  openConnection  (  )  ; 
uc  .  setUseCaches  (  true  )  ; 
if  (  fileName  .  endsWith  (  ".zip"  )  )  { 
try  { 
is  =  new   ZipInputStream  (  new   BufferedInputStream  (  uc  .  getInputStream  (  )  ,  8192  )  )  ; 
ZipEntry   ze  =  (  (  ZipInputStream  )  is  )  .  getNextEntry  (  )  ; 
len  =  (  int  )  ze  .  getSize  (  )  ; 
}  catch  (  Exception   ex  )  { 
is  =  null  ; 
} 
} 
if  (  is  ==  null  )  { 
len  =  uc  .  getContentLength  (  )  ; 
is  =  new   DataInputStream  (  new   BufferedInputStream  (  uc  .  getInputStream  (  )  ,  8192  )  )  ; 
} 
} 
return   new   Object  [  ]  {  is  ,  new   Integer  (  len  )  }  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 








public   static   final   String   read  (  Reader   reader  )  throws   IOException  { 
StringWriter   writer  =  new   StringWriter  (  8192  )  ; 
copy  (  reader  ,  writer  )  ; 
return   writer  .  getBuffer  (  )  .  toString  (  )  ; 
} 








public   static   String   read  (  BufferedReader   buffer  )  throws   IOException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  String   line  =  ""  ;  (  line  =  buffer  .  readLine  (  )  )  !=  null  ;  )  { 
sb  .  append  (  line  +  LSystem  .  LS  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   final   InputStream   read  (  File   file  )  { 
try  { 
return   new   FileInputStream  (  file  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
return   null  ; 
} 
} 







public   static   final   InputStream   read  (  String   fileName  )  { 
return   read  (  new   File  (  fileName  )  )  ; 
} 

public   static   final   void   copy  (  Reader   from  ,  Writer   to  )  throws   IOException  { 
char   buffer  [  ]  =  new   char  [  8192  ]  ; 
int   charsRead  ; 
while  (  (  charsRead  =  from  .  read  (  buffer  )  )  !=  -  1  )  { 
to  .  write  (  buffer  ,  0  ,  charsRead  )  ; 
to  .  flush  (  )  ; 
} 
} 










public   static   URL   toURL  (  File   file  )  throws   MalformedURLException  { 
return   new   URL  (  "file:/"  +  file  .  getAbsolutePath  (  )  )  ; 
} 









public   static   String   toFileName  (  String   filePath  )  { 
return   new   File  (  filePath  )  .  getName  (  )  ; 
} 









public   static   String   toFilePath  (  String   fileName  )  { 
return   new   File  (  fileName  )  .  getAbsolutePath  (  )  ; 
} 









public   static   String   toUNIXpath  (  String   filePath  )  { 
return   filePath  .  replace  (  '\\'  ,  '/'  )  ; 
} 








public   static   String   toUNIXfilePath  (  String   fileName  )  { 
return   toUNIXpath  (  toformatPath  (  fileName  )  )  ; 
} 







public   static   final   String   toformatPath  (  String   fileName  )  { 
return   new   File  (  fileName  )  .  getAbsolutePath  (  )  ; 
} 








public   static   String   toTypePart  (  String   fileName  )  { 
int   point  =  fileName  .  lastIndexOf  (  '.'  )  ; 
int   length  =  fileName  .  length  (  )  ; 
return  (  point  ==  -  1  ||  point  ==  length  -  1  )  ?  ""  :  fileName  .  substring  (  point  +  1  ,  length  )  ; 
} 









public   static   String   toFileType  (  File   file  )  { 
return   toTypePart  (  file  .  getName  (  )  )  ; 
} 









public   static   String   toNamePart  (  String   fileName  )  { 
int   point  =  toPathLsatIndex  (  fileName  )  ; 
int   length  =  fileName  .  length  (  )  ; 
if  (  point  ==  -  1  )  { 
return   fileName  ; 
}  else   if  (  point  ==  length  -  1  )  { 
int   secondPoint  =  toPathLsatIndex  (  fileName  ,  point  -  1  )  ; 
if  (  secondPoint  ==  -  1  )  { 
if  (  length  ==  1  )  { 
return   fileName  ; 
}  else  { 
return   fileName  .  substring  (  0  ,  point  )  ; 
} 
}  else  { 
return   fileName  .  substring  (  secondPoint  +  1  ,  point  )  ; 
} 
}  else  { 
return   fileName  .  substring  (  point  +  1  )  ; 
} 
} 









public   static   String   toPathPart  (  String   fileName  )  { 
int   point  =  toPathLsatIndex  (  fileName  )  ; 
int   length  =  fileName  .  length  (  )  ; 
if  (  point  ==  -  1  )  { 
return  ""  ; 
}  else   if  (  point  ==  length  -  1  )  { 
int   secondPoint  =  toPathLsatIndex  (  fileName  ,  point  -  1  )  ; 
if  (  secondPoint  ==  -  1  )  { 
return  ""  ; 
}  else  { 
return   fileName  .  substring  (  0  ,  secondPoint  )  ; 
} 
}  else  { 
return   fileName  .  substring  (  0  ,  point  )  ; 
} 
} 









public   static   int   toPathIndex  (  String   fileName  )  { 
int   point  =  fileName  .  indexOf  (  '/'  )  ; 
if  (  point  ==  -  1  )  { 
point  =  fileName  .  indexOf  (  '\\'  )  ; 
} 
return   point  ; 
} 











public   static   int   toPathIndex  (  String   fileName  ,  int   fromIndex  )  { 
int   point  =  fileName  .  indexOf  (  '/'  ,  fromIndex  )  ; 
if  (  point  ==  -  1  )  { 
point  =  fileName  .  indexOf  (  '\\'  ,  fromIndex  )  ; 
} 
return   point  ; 
} 









public   static   int   toPathLsatIndex  (  String   fileName  )  { 
int   point  =  fileName  .  lastIndexOf  (  '/'  )  ; 
if  (  point  ==  -  1  )  { 
point  =  fileName  .  lastIndexOf  (  '\\'  )  ; 
} 
return   point  ; 
} 











public   static   int   toPathLsatIndex  (  String   fileName  ,  int   fromIndex  )  { 
int   point  =  fileName  .  lastIndexOf  (  '/'  ,  fromIndex  )  ; 
if  (  point  ==  -  1  )  { 
point  =  fileName  .  lastIndexOf  (  '\\'  ,  fromIndex  )  ; 
} 
return   point  ; 
} 











public   static   String   toSubpath  (  String   pathName  ,  String   fileName  )  { 
return  (  fileName  .  indexOf  (  pathName  )  !=  -  1  )  ?  fileName  .  substring  (  fileName  .  indexOf  (  pathName  )  +  pathName  .  length  (  )  +  1  )  :  fileName  ; 
} 






public   static   final   String   toFileRoot  (  )  { 
File  [  ]  dvs  =  File  .  listRoots  (  )  ; 
String  [  ]  rootname  =  new   String  [  dvs  .  length  ]  ; 
StringBuffer   path  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  rootname  .  length  ;  i  ++  )  { 
rootname  [  i  ]  =  dvs  [  i  ]  .  getPath  (  )  ; 
path  .  append  (  rootname  [  i  ]  .  toString  (  )  )  ; 
} 
return   path  .  toString  (  )  ; 
} 








public   static   String   toNonceDir  (  )  { 
return   new   File  (  ""  )  .  getAbsoluteFile  (  )  .  getAbsolutePath  (  )  ; 
} 








public   static   String  [  ]  toDirList  (  File   path  )  { 
String   pathName  =  path  .  getPath  (  )  ; 
String  [  ]  fileList  ; 
if  (  ""  .  equals  (  pathName  )  )  path  =  new   File  (  "."  )  ;  else   path  =  new   File  (  pathName  )  ; 
if  (  path  .  isDirectory  (  )  )  fileList  =  path  .  list  (  )  ;  else   return   null  ; 
return   fileList  ; 
} 








public   static   String  [  ]  toDirList  (  String   pathName  )  { 
return   toDirList  (  new   File  (  pathName  )  )  ; 
} 








public   static   void   append  (  String   file  ,  String   context  )  throws   IOException  { 
LRAFile   rf  =  new   LRAFile  (  file  ,  "rw"  )  ; 
rf  .  seek  (  rf  .  length  (  )  )  ; 
rf  .  writeBytes  (  LSystem  .  LS  +  context  )  ; 
rf  .  close  (  )  ; 
} 









public   static   ArrayList   getAllFiles  (  String   path  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   ret  =  new   ArrayList  (  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
ArrayList   arr  =  getAllFiles  (  tempfile  .  getPath  (  )  )  ; 
ret  .  addAll  (  arr  )  ; 
arr  .  clear  (  )  ; 
arr  =  null  ; 
}  else  { 
ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
return   ret  ; 
} 









public   static   ArrayList   getAllDir  (  String   path  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   ret  =  new   ArrayList  (  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
ArrayList   arr  =  getAllDir  (  tempfile  .  getPath  (  )  )  ; 
ret  .  addAll  (  arr  )  ; 
arr  .  clear  (  )  ; 
arr  =  null  ; 
} 
} 
} 
return   ret  ; 
} 











public   static   ArrayList   getAllFiles  (  String   path  ,  String   ext  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   ret  =  new   ArrayList  (  )  ; 
String  [  ]  exts  =  ext  .  split  (  ","  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
ArrayList   arr  =  getAllFiles  (  tempfile  .  getPath  (  )  ,  ext  )  ; 
ret  .  addAll  (  arr  )  ; 
arr  .  clear  (  )  ; 
arr  =  null  ; 
}  else  { 
for  (  int   j  =  0  ;  j  <  exts  .  length  ;  j  ++  )  { 
if  (  getExtension  (  tempfile  .  getAbsolutePath  (  )  )  .  equalsIgnoreCase  (  exts  [  j  ]  )  )  { 
ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
} 
} 
return   ret  ; 
} 









public   static   ArrayList   getFiles  (  String   path  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   Ret  =  new   ArrayList  (  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  !  tempfile  .  isDirectory  (  )  )  { 
Ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
return   Ret  ; 
} 









public   static   ArrayList   getDir  (  String   path  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   ret  =  new   ArrayList  (  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
return   ret  ; 
} 











public   static   ArrayList   getFiles  (  String   path  ,  String   ext  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
ArrayList   ret  =  new   ArrayList  (  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  !  tempfile  .  isDirectory  (  )  )  { 
if  (  getExtension  (  tempfile  .  getAbsolutePath  (  )  )  .  equalsIgnoreCase  (  ext  )  )  ret  .  add  (  tempfile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
return   ret  ; 
} 







public   static   String   getFileName  (  String   name  )  { 
if  (  name  ==  null  )  { 
return  ""  ; 
} 
int   length  =  name  .  length  (  )  ; 
int   size  =  name  .  lastIndexOf  (  LSystem  .  FS  )  +  1  ; 
if  (  size  <  length  )  { 
return   name  .  substring  (  size  ,  length  )  ; 
}  else  { 
return  ""  ; 
} 
} 







public   static   String   getExtension  (  String   name  )  { 
if  (  name  ==  null  )  { 
return  ""  ; 
} 
int   index  =  name  .  lastIndexOf  (  "."  )  ; 
if  (  index  ==  -  1  )  { 
return  ""  ; 
}  else  { 
return   name  .  substring  (  index  +  1  )  ; 
} 
} 








public   static   void   deleteFile  (  String   path  )  throws   Exception  { 
File   file  =  new   File  (  path  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
deleteFile  (  tempfile  .  getPath  (  )  )  ; 
}  else  { 
tempfile  .  delete  (  )  ; 
} 
} 
} 
} 








public   static   String   searchClassPath  (  String   searchDirectory  ,  String   className  )  { 
if  (  searchDirectory  ==  null  ||  searchDirectory  .  length  (  )  ==  0  )  { 
return   null  ; 
} 
if  (  className  ==  null  ||  className  .  length  (  )  ==  0  )  { 
return   null  ; 
} 
String   classPath  =  null  ; 
String   classFileName  =  className  +  ".class"  ; 
try  { 
File   dir  =  new   File  (  searchDirectory  )  ; 
String  [  ]  fileList  =  dir  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  fileList  .  length  ;  i  ++  )  { 
File   file  =  new   File  (  searchDirectory  +  "/"  +  fileList  [  i  ]  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
classPath  =  searchClassPath  (  file  .  getAbsolutePath  (  )  ,  className  )  ; 
if  (  classPath  !=  null  )  { 
break  ; 
} 
}  else  { 
if  (  fileList  [  i  ]  .  equals  (  classFileName  )  )  { 
return   file  .  getParent  (  )  ; 
} 
} 
} 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
return   classPath  ; 
} 








public   static   void   deleteDir  (  String   path  )  throws   Exception  { 
File   file  =  new   File  (  path  )  ; 
String  [  ]  listFile  =  file  .  list  (  )  ; 
if  (  listFile  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  listFile  .  length  ;  i  ++  )  { 
File   tempfile  =  new   File  (  path  +  LSystem  .  FS  +  listFile  [  i  ]  )  ; 
if  (  tempfile  .  isDirectory  (  )  )  { 
deleteDir  (  tempfile  .  getPath  (  )  )  ; 
tempfile  .  delete  (  )  ; 
} 
} 
} 
} 







public   static   String   getCanonicalPath  (  File   file  )  { 
try  { 
return   file  .  getCanonicalPath  (  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 







public   static   String   getClassPath  (  Class   clazz  )  { 
String   fileName  ; 
try  { 
File   jarFile  =  new   File  (  clazz  .  getProtectionDomain  (  )  .  getCodeSource  (  )  .  getLocation  (  )  .  getPath  (  )  )  ; 
fileName  =  jarFile  .  getAbsolutePath  (  )  ; 
}  catch  (  Exception   e  )  { 
fileName  =  null  ; 
} 
return   fileName  ; 
} 







public   static   long   getFileSize  (  String   filePath  )  { 
File   file  =  new   File  (  filePath  )  ; 
return   file  .  length  (  )  ; 
} 
} 

