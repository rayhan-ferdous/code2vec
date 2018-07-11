package   de  .  laidback  .  racoon  .  common  ; 

import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  CharBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  nio  .  charset  .  CharsetDecoder  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  Format  ; 
import   java  .  util  .  Random  ; 
import   org  .  apache  .  commons  .  lang  .  time  .  FastDateFormat  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   de  .  laidback  .  racoon  .  exceptions  .  RacoonFileAccessException  ; 







public   class   Tools  { 




private   static   final   Logger   logger  =  Logger  .  getLogger  (  Tools  .  class  )  ; 

private   static   Charset   charset  =  Charset  .  forName  (  "ISO-8859-15"  )  ; 

private   static   CharsetDecoder   decoder  =  charset  .  newDecoder  (  )  ; 




private   static   Tools   instance  ; 

private   Random   rnd  ; 






public   static   Tools   getInstance  (  )  { 
if  (  instance  ==  null  )  { 
instance  =  new   Tools  (  )  ; 
} 
return   instance  ; 
} 





private   Tools  (  )  { 
rnd  =  new   Random  (  )  ; 
} 









public   int   parseInt  (  String   value  )  { 
if  (  isEmpty  (  value  )  )  return  -  1  ; 
try  { 
return   Integer  .  parseInt  (  value  )  ; 
}  catch  (  NumberFormatException   e  )  { 
logger  .  error  (  "parseInt()"  ,  e  )  ; 
return  -  1  ; 
} 
} 









public   long   parselong  (  String   value  )  { 
if  (  isEmpty  (  value  )  )  return  -  1  ; 
try  { 
return   Long  .  parseLong  (  value  )  ; 
}  catch  (  NumberFormatException   e  )  { 
logger  .  error  (  "parseInt()"  ,  e  )  ; 
return  -  1  ; 
} 
} 









public   Long   parseLong  (  String   value  )  { 
if  (  isEmpty  (  value  )  )  return   null  ; 
try  { 
return   Long  .  parseLong  (  value  )  ; 
}  catch  (  NumberFormatException   e  )  { 
logger  .  error  (  "parseInt()"  ,  e  )  ; 
return   null  ; 
} 
} 







public   String   getResourceFilePath  (  String   resource  )  { 
ClassLoader   loader  =  Tools  .  class  .  getClassLoader  (  )  ; 
URL   url  =  loader  .  getResource  (  resource  )  ; 
if  (  url  !=  null  )  { 
return   url  .  getFile  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   void   sleep  (  long   millis  )  { 
try  { 
Thread  .  sleep  (  millis  )  ; 
}  catch  (  InterruptedException   e  )  { 
logger  .  error  (  "delay method was interrupted !"  )  ; 
} 
} 







public   boolean   deleteFile  (  String   filename  )  { 
File   f  =  new   File  (  filename  )  ; 
return   f  .  delete  (  )  ; 
} 








public   String   md5  (  String   input  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
String   s  =  null  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte   digest  [  ]  =  md  .  digest  (  input  .  getBytes  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  digest  .  length  ;  i  ++  )  { 
s  =  Integer  .  toHexString  (  digest  [  i  ]  &  0xFF  )  ; 
if  (  s  .  length  (  )  ==  1  )  { 
sb  .  append  (  "0"  )  ; 
sb  .  append  (  s  )  ; 
}  else   sb  .  append  (  s  )  ; 
} 
}  catch  (  NoSuchAlgorithmException   e  )  { 
logger  .  error  (  "md5(String)"  ,  e  )  ; 
return   null  ; 
} 
return   sb  .  toString  (  )  ; 
} 









public   String   readFile  (  String   filename  )  { 
if  (  logger  .  isTraceEnabled  (  )  )  { 
logger  .  trace  (  "readFile("  +  filename  +  ") - start"  )  ; 
} 
FileInputStream   fstream  ; 
try  { 
fstream  =  new   FileInputStream  (  filename  )  ; 
FileChannel   fChannel  =  fstream  .  getChannel  (  )  ; 
int   size  =  (  int  )  fChannel  .  size  (  )  ; 
CharBuffer   cb  =  decoder  .  decode  (  fChannel  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  size  )  )  ; 
String   returnString  =  cb  .  toString  (  )  ; 
if  (  logger  .  isTraceEnabled  (  )  )  { 
logger  .  trace  (  "readFile("  +  filename  +  ") - end"  )  ; 
} 
return   returnString  ; 
}  catch  (  FileNotFoundException   e  )  { 
logger  .  error  (  e  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  e  )  ; 
} 
if  (  logger  .  isTraceEnabled  (  )  )  { 
logger  .  trace  (  "readFile("  +  filename  +  ") - end"  )  ; 
} 
return  ""  ; 
} 








public   boolean   writeFile  (  String   fileName  ,  String   content  )  { 
BufferedWriter   out  =  null  ; 
try  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  fileName  )  )  ; 
out  .  write  (  content  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "writeFile(String, String)"  ,  e  )  ; 
return   false  ; 
} 
closeStream  (  out  )  ; 
return   true  ; 
} 








public   Writer   getFileWriter  (  String   fileName  )  { 
try  { 
return   new   BufferedWriter  (  new   FileWriter  (  fileName  )  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "Error getting Filewriter for file "  +  fileName  ,  e  )  ; 
return   null  ; 
} 
} 










public   byte  [  ]  readFileIntoByteArray  (  String   filename  )  throws   RacoonFileAccessException  { 
FileInputStream   fstream  ; 
try  { 
fstream  =  new   FileInputStream  (  filename  )  ; 
FileChannel   fChannel  =  fstream  .  getChannel  (  )  ; 
int   size  =  (  int  )  fChannel  .  size  (  )  ; 
return   fChannel  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  size  )  .  array  (  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
logger  .  error  (  "readFileIntoByteArray(String)"  ,  e  )  ; 
throw   new   RacoonFileAccessException  (  "File not found"  ,  filename  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "readFileIntoByteArray(String)"  ,  e  )  ; 
throw   new   RacoonFileAccessException  (  "File not found"  ,  filename  )  ; 
} 
} 









public   File  [  ]  getFiles  (  String   path  ,  String   filterExtension  )  { 
if  (  !  dirExists  (  path  )  )  return   null  ; 
File   dir  =  new   File  (  path  )  ; 
return   dir  .  listFiles  (  new   MyFileFilter  (  filterExtension  )  )  ; 
} 









public   String  [  ]  getFileNames  (  String   path  ,  String   filterExtension  )  { 
File  [  ]  files  =  getFiles  (  path  ,  filterExtension  )  ; 
if  (  files  ==  null  )  { 
return   new   String  [  0  ]  ; 
} 
String  [  ]  s  =  new   String  [  files  .  length  ]  ; 
int   i  =  0  ; 
for  (  File   f  :  files  )  { 
if  (  f  .  isFile  (  )  )  s  [  i  ++  ]  =  f  .  getAbsolutePath  (  )  ; 
} 
return   trimStringArray  (  s  )  ; 
} 

public   Format   getDateFormat  (  String   format  )  { 
return   FastDateFormat  .  getInstance  (  format  )  ; 
} 

public   String   getDateFormatted  (  long   date  ,  String   format  )  { 
return   getDateFormat  (  format  )  .  format  (  date  )  ; 
} 








public   boolean   dirExists  (  String   path  )  { 
if  (  isEmpty  (  path  )  )  return   false  ; 
File   dir  =  new   File  (  path  )  ; 
return  (  dir  .  exists  (  )  &&  dir  .  isDirectory  (  )  )  ; 
} 







public   boolean   isEmpty  (  String   value  )  { 
return  (  value  ==  null  ||  value  .  length  (  )  ==  0  )  ; 
} 








public   void   closeStream  (  Writer   writer  )  { 
if  (  writer  ==  null  )  return  ; 
try  { 
writer  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "closeStream(Writer)"  ,  e  )  ; 
} 
writer  =  null  ; 
} 








public   void   closeStream  (  Reader   reader  )  { 
if  (  reader  ==  null  )  return  ; 
try  { 
reader  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "closeStream(Reader)"  ,  e  )  ; 
} 
reader  =  null  ; 
} 








public   void   closeStream  (  InputStream   is  )  { 
if  (  is  ==  null  )  return  ; 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "closeStream(InputStream)"  ,  e  )  ; 
} 
is  =  null  ; 
} 












public   Object  [  ]  trimArray  (  Object  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  return   array  ; 
Object  [  ]  newArray  =  null  ; 
int   c  =  0  ; 
try  { 
while  (  array  [  c  ]  !=  null  )  c  ++  ; 
}  catch  (  Exception   e  )  { 
} 
try  { 
newArray  =  new   Object  [  c  ]  ; 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  c  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  error  (  "Error at trimArray(Object[]): c="  +  c  +  ", source array length is "  +  array  .  length  ,  e  )  ; 
return   array  ; 
} 
return   newArray  ; 
} 






public   String   getWorkingDir  (  )  { 
return   new   File  (  "."  )  .  getAbsolutePath  (  )  ; 
} 








public   String  [  ]  trimStringArray  (  String  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  return   array  ; 
String  [  ]  newArray  =  null  ; 
int   c  =  0  ; 
try  { 
while  (  array  [  c  ]  !=  null  )  c  ++  ; 
}  catch  (  Exception   e  )  { 
} 
try  { 
newArray  =  new   String  [  c  ]  ; 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  c  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  error  (  "Error at trimArray(String[]): c="  +  c  +  ", source array length is "  +  array  .  length  ,  e  )  ; 
return   array  ; 
} 
return   newArray  ; 
} 




public   final   String   getStackTrace  (  Throwable   ex  )  { 
String   result  =  ""  ; 
if  (  ex  !=  null  )  { 
try  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
ex  .  printStackTrace  (  pw  )  ; 
pw  .  close  (  )  ; 
sw  .  close  (  )  ; 
result  =  sw  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
return   result  ; 
} 





public   int   generateRandomInt  (  )  { 
return   rnd  .  nextInt  (  )  ; 
} 
} 

