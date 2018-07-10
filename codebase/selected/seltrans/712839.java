package   org  .  nightlabs  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  lang  .  reflect  .  Field  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 







public   abstract   class   Utils  { 




public   static   final   String   CHARSET_NAME_UTF_8  =  "UTF-8"  ; 




public   static   final   Charset   CHARSET_UTF_8  =  Charset  .  forName  (  CHARSET_NAME_UTF_8  )  ; 





public   static   final   long   GIGABYTE  =  1  *  1024  *  1024  *  1024  ; 









public   static   String   addFinalSlash  (  String   directory  )  { 
if  (  directory  .  endsWith  (  File  .  separator  )  )  return   directory  ;  else   return   directory  +  File  .  separator  ; 
} 




public   static   String   int2StringMinDigits  (  int   i  ,  int   minDigits  )  { 
String   s  =  Integer  .  toString  (  i  )  ; 
if  (  s  .  length  (  )  <  minDigits  )  { 
StringBuffer   sBuf  =  new   StringBuffer  (  s  )  ; 
while  (  sBuf  .  length  (  )  <  minDigits  )  sBuf  .  insert  (  0  ,  '0'  )  ; 
s  =  sBuf  .  toString  (  )  ; 
} 
return   s  ; 
} 

















public   static   String   getRelativePath  (  String   baseDir  ,  String   file  )  throws   IOException  { 
return   getRelativePath  (  new   File  (  baseDir  )  ,  file  )  ; 
} 

















public   static   String   getRelativePath  (  File   baseDir  ,  File   file  )  throws   IOException  { 
return   getRelativePath  (  baseDir  ,  file  .  getPath  (  )  )  ; 
} 








































public   static   String   getRelativePath  (  File   baseDir  ,  String   file  )  throws   IOException  { 
File   absFile  ; 
File   tmpF  =  new   File  (  file  )  ; 
if  (  tmpF  .  isAbsolute  (  )  )  absFile  =  tmpF  ;  else   absFile  =  new   File  (  baseDir  ,  file  )  ; 
File   dest  =  absFile  ; 
File   b  =  baseDir  ; 
String   up  =  ""  ; 
while  (  b  .  getParentFile  (  )  !=  null  )  { 
String   res  =  _getRelativePath  (  b  ,  dest  .  getAbsolutePath  (  )  )  ; 
if  (  res  !=  null  )  return   up  +  res  ; 
up  =  "../"  +  up  ; 
b  =  b  .  getParentFile  (  )  ; 
} 
return   absFile  .  getAbsolutePath  (  )  ; 
} 









private   static   String   _getRelativePath  (  File   baseDir  ,  String   file  )  throws   IOException  { 
if  (  !  baseDir  .  isAbsolute  (  )  )  throw   new   IllegalArgumentException  (  "baseDir \""  +  baseDir  .  getPath  (  )  +  "\" is not absolute!"  )  ; 
File   absFile  ; 
File   tmpF  =  new   File  (  file  )  ; 
if  (  tmpF  .  isAbsolute  (  )  )  absFile  =  tmpF  ;  else   absFile  =  new   File  (  baseDir  ,  file  )  ; 
String   absFileStr  =  null  ; 
String   baseDirStr  =  null  ; 
for  (  int   mode_base  =  0  ;  mode_base  <  2  ;  ++  mode_base  )  { 
switch  (  mode_base  )  { 
case   0  : 
baseDirStr  =  simplifyPath  (  baseDir  )  ; 
break  ; 
case   1  : 
baseDirStr  =  baseDir  .  getCanonicalPath  (  )  ; 
break  ; 
default  : 
throw   new   IllegalStateException  (  "this should never happen!"  )  ; 
} 
for  (  int   mode_abs  =  0  ;  mode_abs  <  2  ;  ++  mode_abs  )  { 
baseDirStr  =  addFinalSlash  (  baseDirStr  )  ; 
switch  (  mode_abs  )  { 
case   0  : 
absFileStr  =  simplifyPath  (  absFile  )  ; 
break  ; 
case   1  : 
absFileStr  =  absFile  .  getCanonicalPath  (  )  ; 
break  ; 
default  : 
throw   new   IllegalStateException  (  "this should never happen!"  )  ; 
} 
if  (  !  absFileStr  .  startsWith  (  baseDirStr  )  )  { 
if  (  mode_base  >=  1  &&  mode_abs  >=  1  )  return   null  ; 
}  else   break  ; 
} 
} 
if  (  baseDirStr  ==  null  )  throw   new   NullPointerException  (  "baseDirStr == null"  )  ; 
if  (  absFileStr  ==  null  )  throw   new   NullPointerException  (  "absFileStr == null"  )  ; 
return   absFileStr  .  substring  (  baseDirStr  .  length  (  )  ,  absFileStr  .  length  (  )  )  ; 
} 













public   static   String   simplifyPath  (  File   path  )  { 
LinkedList  <  String  >  dirs  =  new   LinkedList  <  String  >  (  )  ; 
String   pathStr  =  path  .  getAbsolutePath  (  )  ; 
boolean   startWithSeparator  =  pathStr  .  startsWith  (  File  .  separator  )  ; 
StringTokenizer   tk  =  new   StringTokenizer  (  pathStr  ,  File  .  separator  ,  false  )  ; 
while  (  tk  .  hasMoreTokens  (  )  )  { 
String   dir  =  tk  .  nextToken  (  )  ; 
if  (  "."  .  equals  (  dir  )  )  ;  else   if  (  ".."  .  equals  (  dir  )  )  { 
if  (  !  dirs  .  isEmpty  (  )  )  dirs  .  removeLast  (  )  ; 
}  else   dirs  .  addLast  (  dir  )  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  String   dir  :  dirs  )  { 
if  (  startWithSeparator  ||  sb  .  length  (  )  >  0  )  sb  .  append  (  File  .  separator  )  ; 
sb  .  append  (  dir  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 










public   static   long   transferStreamData  (  java  .  io  .  InputStream   in  ,  java  .  io  .  OutputStream   out  )  throws   java  .  io  .  IOException  { 
return   transferStreamData  (  in  ,  out  ,  0  ,  -  1  )  ; 
} 





















public   static   boolean   equals  (  Object   obj0  ,  Object   obj1  )  { 
if  (  obj0   instanceof   Object  [  ]  &&  obj1   instanceof   Object  [  ]  )  return   obj0  ==  obj1  ||  Arrays  .  equals  (  (  Object  [  ]  )  obj0  ,  (  Object  [  ]  )  obj1  )  ; 
return   obj0  ==  obj1  ||  (  obj0  !=  null  &&  obj0  .  equals  (  obj1  )  )  ; 
} 






public   static   int   hashCode  (  long   l  )  { 
return  (  int  )  (  l  ^  (  l  >  >  >  32  )  )  ; 
} 








public   static   int   hashCode  (  Object   obj  )  { 
return   obj  ==  null  ?  0  :  obj  .  hashCode  (  )  ; 
} 












public   static   boolean   deleteDirectoryRecursively  (  String   dir  )  { 
File   dirF  =  new   File  (  dir  )  ; 
return   deleteDirectoryRecursively  (  dirF  )  ; 
} 













public   static   boolean   deleteDirectoryRecursively  (  File   dir  )  { 
if  (  !  dir  .  exists  (  )  )  return   true  ; 
if  (  dir  .  isDirectory  (  )  )  { 
File  [  ]  content  =  dir  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  ++  i  )  { 
File   f  =  content  [  i  ]  ; 
if  (  f  .  isDirectory  (  )  )  deleteDirectoryRecursively  (  f  )  ;  else   try  { 
f  .  delete  (  )  ; 
}  catch  (  SecurityException   e  )  { 
} 
} 
} 
try  { 
return   dir  .  delete  (  )  ; 
}  catch  (  SecurityException   e  )  { 
return   false  ; 
} 
} 



















public   static   File   createUniqueRandomFolder  (  File   rootFolder  ,  final   String   prefix  )  throws   IOException  { 
return   createUniqueRandomFolder  (  rootFolder  ,  prefix  ,  10000  ,  10000  )  ; 
} 





















public   static   synchronized   File   createUniqueRandomFolder  (  File   rootFolder  ,  final   String   prefix  ,  long   maxIterations  ,  long   uniqueOutOf  )  throws   IOException  { 
long   count  =  0  ; 
while  (  ++  count  <=  maxIterations  )  { 
File   f  =  new   File  (  rootFolder  ,  String  .  format  (  "%s%x"  ,  prefix  ,  (  long  )  (  Math  .  random  (  )  *  uniqueOutOf  )  )  )  ; 
if  (  !  f  .  exists  (  )  )  { 
if  (  !  f  .  mkdirs  (  )  )  throw   new   IOException  (  "The directory "  +  f  .  getAbsolutePath  (  )  +  " could not be created"  )  ; 
return   f  ; 
} 
} 
throw   new   IOException  (  "Reached end of maxIteration("  +  maxIterations  +  "), but could not acquire a unique fileName for folder "  +  rootFolder  )  ; 
} 

















public   static   synchronized   File   createUniqueIncrementalFolder  (  File   rootFolder  ,  final   String   prefix  )  throws   IOException  { 
for  (  int   n  =  0  ;  n  <=  Integer  .  MAX_VALUE  ;  n  ++  )  { 
File   f  =  new   File  (  rootFolder  ,  String  .  format  (  "%s%x"  ,  prefix  ,  n  )  )  ; 
if  (  !  f  .  exists  (  )  )  { 
if  (  !  f  .  mkdirs  (  )  )  throw   new   IOException  (  "The directory "  +  f  .  getAbsolutePath  (  )  +  " could not be created"  )  ; 
return   f  ; 
} 
} 
throw   new   IOException  (  "Iterated to Integer.MAX_VALUE and could not find a unique folder!"  )  ; 
} 










public   static   long   transferStreamData  (  java  .  io  .  InputStream   in  ,  java  .  io  .  OutputStream   out  ,  long   inputOffset  ,  long   inputLen  )  throws   java  .  io  .  IOException  { 
int   bytesRead  ; 
int   transferred  =  0  ; 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
if  (  inputOffset  >  0  )  if  (  in  .  skip  (  inputOffset  )  !=  inputOffset  )  throw   new   IOException  (  "Input skip failed (offset "  +  inputOffset  +  ")"  )  ; 
while  (  true  )  { 
if  (  inputLen  >=  0  )  bytesRead  =  in  .  read  (  buf  ,  0  ,  (  int  )  Math  .  min  (  buf  .  length  ,  inputLen  -  transferred  )  )  ;  else   bytesRead  =  in  .  read  (  buf  )  ; 
if  (  bytesRead  <=  0  )  break  ; 
out  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
transferred  +=  bytesRead  ; 
if  (  inputLen  >=  0  &&  transferred  >=  inputLen  )  break  ; 
} 
out  .  flush  (  )  ; 
return   transferred  ; 
} 












public   static   void   copyResource  (  Class  <  ?  >  sourceResClass  ,  String   sourceResName  ,  String   destinationFilename  )  throws   IOException  { 
copyResource  (  sourceResClass  ,  sourceResName  ,  new   File  (  destinationFilename  )  )  ; 
} 










public   static   void   copyResource  (  Class  <  ?  >  sourceResClass  ,  String   sourceResName  ,  File   destinationFile  )  throws   IOException  { 
InputStream   source  =  null  ; 
FileOutputStream   destination  =  null  ; 
try  { 
source  =  sourceResClass  .  getResourceAsStream  (  sourceResName  )  ; 
if  (  source  ==  null  )  throw   new   FileNotFoundException  (  "Class "  +  sourceResClass  .  getName  (  )  +  " could not find resource "  +  sourceResName  )  ; 
if  (  destinationFile  .  exists  (  )  )  { 
if  (  destinationFile  .  isFile  (  )  )  { 
if  (  !  destinationFile  .  canWrite  (  )  )  throw   new   IOException  (  "FileCopy: destination file is unwriteable: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
}  else   throw   new   IOException  (  "FileCopy: destination is not a file: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
}  else  { 
File   parentdir  =  destinationFile  .  getParentFile  (  )  ; 
if  (  parentdir  ==  null  ||  !  parentdir  .  exists  (  )  )  throw   new   IOException  (  "FileCopy: destination directory doesn't exist: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
if  (  !  parentdir  .  canWrite  (  )  )  throw   new   IOException  (  "FileCopy: destination directory is unwriteable: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
} 
destination  =  new   FileOutputStream  (  destinationFile  )  ; 
transferStreamData  (  source  ,  destination  )  ; 
}  finally  { 
if  (  source  !=  null  )  try  { 
source  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
if  (  destination  !=  null  )  try  { 
destination  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
} 
} 









public   static   void   copyFile  (  String   sourceFilename  ,  String   destinationFilename  )  throws   IOException  { 
copyFile  (  new   File  (  sourceFilename  )  ,  new   File  (  destinationFilename  )  )  ; 
} 







public   static   void   copyFile  (  File   sourceFile  ,  File   destinationFile  )  throws   IOException  { 
FileInputStream   source  =  null  ; 
FileOutputStream   destination  =  null  ; 
try  { 
if  (  !  sourceFile  .  exists  (  )  ||  !  sourceFile  .  isFile  (  )  )  throw   new   IOException  (  "FileCopy: no such source file: "  +  sourceFile  .  getCanonicalPath  (  )  )  ; 
if  (  !  sourceFile  .  canRead  (  )  )  throw   new   IOException  (  "FileCopy: source file is unreadable: "  +  sourceFile  .  getCanonicalPath  (  )  )  ; 
if  (  destinationFile  .  exists  (  )  )  { 
if  (  destinationFile  .  isFile  (  )  )  { 
if  (  !  destinationFile  .  canWrite  (  )  )  throw   new   IOException  (  "FileCopy: destination file is unwriteable: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
}  else   throw   new   IOException  (  "FileCopy: destination is not a file: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
}  else  { 
File   parentdir  =  destinationFile  .  getParentFile  (  )  ; 
if  (  parentdir  ==  null  ||  !  parentdir  .  exists  (  )  )  throw   new   IOException  (  "FileCopy: destination directory doesn't exist: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
if  (  !  parentdir  .  canWrite  (  )  )  throw   new   IOException  (  "FileCopy: destination directory is unwriteable: "  +  destinationFile  .  getCanonicalPath  (  )  )  ; 
} 
source  =  new   FileInputStream  (  sourceFile  )  ; 
destination  =  new   FileOutputStream  (  destinationFile  )  ; 
transferStreamData  (  source  ,  destination  )  ; 
}  finally  { 
if  (  source  !=  null  )  try  { 
source  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
if  (  destination  !=  null  )  try  { 
destination  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
} 
} 







public   static   void   copyDirectory  (  File   sourceDirectory  ,  File   destinationDirectory  )  throws   IOException  { 
if  (  !  sourceDirectory  .  exists  (  )  ||  !  sourceDirectory  .  isDirectory  (  )  )  throw   new   IOException  (  "No such source directory: "  +  sourceDirectory  .  getAbsolutePath  (  )  )  ; 
if  (  destinationDirectory  .  exists  (  )  )  { 
if  (  !  destinationDirectory  .  isDirectory  (  )  )  throw   new   IOException  (  "Destination exists but is not a directory: "  +  sourceDirectory  .  getAbsolutePath  (  )  )  ; 
}  else   destinationDirectory  .  mkdirs  (  )  ; 
File  [  ]  files  =  sourceDirectory  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
File   destinationFile  =  new   File  (  destinationDirectory  ,  file  .  getName  (  )  )  ; 
if  (  file  .  isDirectory  (  )  )  copyDirectory  (  file  ,  destinationFile  )  ;  else   copyFile  (  file  ,  destinationFile  )  ; 
} 
} 







public   static   File   getFile  (  File   file  ,  String  ...  subDirs  )  { 
File   f  =  file  ; 
for  (  String   subDir  :  subDirs  )  f  =  new   File  (  f  ,  subDir  )  ; 
return   f  ; 
} 














public   static   String   addLeadingZeros  (  String   base  ,  int   strLength  )  { 
return   addLeadingChars  (  base  ,  strLength  ,  '0'  )  ; 
} 













public   static   String   addLeadingChars  (  String   s  ,  int   length  ,  char   fillChar  )  { 
if  (  s  !=  null  &&  s  .  length  (  )  >=  length  )  return   s  ; 
StringBuffer   sb  =  new   StringBuffer  (  length  )  ; 
int   l  =  s  ==  null  ?  length  :  length  -  s  .  length  (  )  ; 
while  (  sb  .  length  (  )  <  l  )  sb  .  append  (  fillChar  )  ; 
if  (  s  !=  null  )  sb  .  append  (  s  )  ; 
return   sb  .  toString  (  )  ; 
} 













public   static   String   addTrailingChars  (  String   s  ,  int   length  ,  char   fillChar  )  { 
if  (  s  !=  null  &&  s  .  length  (  )  >=  length  )  return   s  ; 
StringBuffer   sb  =  new   StringBuffer  (  length  )  ; 
if  (  s  !=  null  )  sb  .  append  (  s  )  ; 
while  (  sb  .  length  (  )  <  length  )  sb  .  append  (  fillChar  )  ; 
return   sb  .  toString  (  )  ; 
} 














public   static   String   addLeadingSpaces  (  String   base  ,  int   strLength  )  { 
return   addLeadingChars  (  base  ,  strLength  ,  ' '  )  ; 
} 










public   static   String   encodeHexStr  (  byte  [  ]  buf  )  { 
return   encodeHexStr  (  buf  ,  0  ,  buf  .  length  )  ; 
} 











public   static   String   encodeHexStr  (  byte  [  ]  buf  ,  int   pos  ,  int   len  )  { 
StringBuffer   hex  =  new   StringBuffer  (  )  ; 
while  (  len  --  >  0  )  { 
byte   ch  =  buf  [  pos  ++  ]  ; 
int   d  =  (  ch  >  >  4  )  &  0xf  ; 
hex  .  append  (  (  char  )  (  d  >=  10  ?  'a'  -  10  +  d  :  '0'  +  d  )  )  ; 
d  =  ch  &  0xf  ; 
hex  .  append  (  (  char  )  (  d  >=  10  ?  'a'  -  10  +  d  :  '0'  +  d  )  )  ; 
} 
return   hex  .  toString  (  )  ; 
} 





public   static   String   decodeHexStr  (  byte  [  ]  buf  ,  int   pos  ,  int   len  )  { 
return   encodeHexStr  (  buf  ,  pos  ,  len  )  ; 
} 






public   static   byte  [  ]  decodeHexStr  (  String   hex  )  { 
if  (  hex  .  length  (  )  %  2  !=  0  )  throw   new   IllegalArgumentException  (  "The hex string must have an even number of characters!"  )  ; 
byte  [  ]  res  =  new   byte  [  hex  .  length  (  )  /  2  ]  ; 
int   m  =  0  ; 
for  (  int   i  =  0  ;  i  <  hex  .  length  (  )  ;  i  +=  2  )  { 
res  [  m  ++  ]  =  (  byte  )  Integer  .  parseInt  (  hex  .  substring  (  i  ,  i  +  2  )  ,  16  )  ; 
} 
return   res  ; 
} 







public   static   String   getMD5HexString  (  String   clear  )  throws   NoSuchAlgorithmException  { 
byte  [  ]  enc  =  MessageDigest  .  getInstance  (  "MD5"  )  .  digest  (  clear  .  getBytes  (  )  )  ; 
System  .  out  .  println  (  new   String  (  enc  )  )  ; 
return   encodeHexStr  (  enc  ,  0  ,  enc  .  length  )  ; 
} 







public   static   String   htmlEncode  (  String   s  )  { 
if  (  s  ==  null  )  return  ""  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
char   ch  =  s  .  charAt  (  i  )  ; 
if  (  ch  ==  '\n'  )  sb  .  append  (  "<br>"  )  ;  else   if  (  ch  ==  '<'  )  sb  .  append  (  "&lt;"  )  ;  else   if  (  ch  ==  '>'  )  sb  .  append  (  "&gt;"  )  ;  else   if  (  ch  ==  '\t'  )  sb  .  append  (  "&nbsp;&nbsp;&nbsp;&nbsp;"  )  ;  else   sb  .  append  (  ch  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 









public   static   String   readTextFile  (  File   f  )  throws   FileNotFoundException  ,  IOException  { 
return   readTextFile  (  f  ,  CHARSET_NAME_UTF_8  )  ; 
} 










public   static   String   readTextFile  (  File   f  ,  String   encoding  )  throws   FileNotFoundException  ,  IOException  { 
if  (  f  .  length  (  )  >  GIGABYTE  )  throw   new   IllegalArgumentException  (  "File exceeds "  +  GIGABYTE  +  " bytes: "  +  f  .  getAbsolutePath  (  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
FileInputStream   fin  =  new   FileInputStream  (  f  )  ; 
try  { 
InputStreamReader   reader  =  new   InputStreamReader  (  fin  ,  encoding  )  ; 
try  { 
char  [  ]  cbuf  =  new   char  [  1024  ]  ; 
int   bytesRead  ; 
while  (  true  )  { 
bytesRead  =  reader  .  read  (  cbuf  )  ; 
if  (  bytesRead  <=  0  )  break  ;  else   sb  .  append  (  cbuf  ,  0  ,  bytesRead  )  ; 
} 
}  finally  { 
reader  .  close  (  )  ; 
} 
}  finally  { 
fin  .  close  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 









public   static   String   readTextFile  (  InputStream   in  )  throws   FileNotFoundException  ,  IOException  { 
return   readTextFile  (  in  ,  CHARSET_NAME_UTF_8  )  ; 
} 










public   static   String   readTextFile  (  InputStream   in  ,  String   encoding  )  throws   FileNotFoundException  ,  IOException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
InputStreamReader   reader  =  new   InputStreamReader  (  in  ,  encoding  )  ; 
char  [  ]  cbuf  =  new   char  [  1024  ]  ; 
int   bytesRead  ; 
while  (  true  )  { 
bytesRead  =  reader  .  read  (  cbuf  )  ; 
if  (  bytesRead  <=  0  )  break  ;  else   sb  .  append  (  cbuf  ,  0  ,  bytesRead  )  ; 
if  (  sb  .  length  (  )  >  GIGABYTE  )  throw   new   IllegalArgumentException  (  "Text exceeds "  +  GIGABYTE  +  " bytes!"  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 








public   static   String   getFileExtension  (  String   fileName  )  { 
if  (  fileName  ==  null  )  return   null  ; 
int   lastIndex  =  fileName  .  lastIndexOf  (  "."  )  ; 
if  (  lastIndex  <  0  )  return   null  ; 
return   fileName  .  substring  (  lastIndex  +  1  )  ; 
} 







public   static   String   getFileNameWithoutExtension  (  String   fileName  )  { 
if  (  fileName  ==  null  )  return   null  ; 
int   lastIndex  =  fileName  .  lastIndexOf  (  "."  )  ; 
if  (  lastIndex  <  0  )  return   fileName  ; 
return   fileName  .  substring  (  0  ,  lastIndex  )  ; 
} 

private   static   File   tempDir  =  null  ; 













public   static   File   getTempDir  (  )  throws   IOException  { 
if  (  tempDir  ==  null  )  { 
File   tmp  =  File  .  createTempFile  (  "tmp."  ,  ".tmp"  )  ; 
File   res  =  tmp  .  getParentFile  (  )  ; 
if  (  !  res  .  isAbsolute  (  )  )  res  =  res  .  getAbsoluteFile  (  )  ; 
tempDir  =  res  ; 
tmp  .  delete  (  )  ; 
} 
return   tempDir  ; 
} 












public   static   byte  [  ]  generateUniqueKey  (  byte  [  ]  data  ,  String   algorithm  )  { 
try  { 
return   hash  (  data  ,  algorithm  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 




public   static   final   String   HASH_ALGORITHM_MD5  =  "MD5"  ; 




public   static   final   String   HASH_ALGORITHM_SHA  =  "SHA"  ; 













public   static   byte  [  ]  hash  (  byte  [  ]  data  ,  String   algorithm  )  throws   NoSuchAlgorithmException  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
md  .  update  (  data  )  ; 
return   md  .  digest  (  )  ; 
} 
















public   static   byte  [  ]  hash  (  InputStream   in  ,  long   bytesToRead  ,  String   algorithm  )  throws   NoSuchAlgorithmException  ,  IOException  { 
if  (  bytesToRead  <  0  &&  bytesToRead  !=  -  1  )  throw   new   IllegalArgumentException  (  "bytesToRead < 0 && bytesToRead != -1"  )  ; 
long   bytesReadTotal  =  0  ; 
MessageDigest   md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
byte  [  ]  data  =  new   byte  [  10240  ]  ; 
while  (  true  )  { 
int   len  ; 
if  (  bytesToRead  <  0  )  len  =  data  .  length  ;  else  { 
len  =  (  int  )  Math  .  min  (  (  long  )  data  .  length  ,  bytesToRead  -  bytesReadTotal  )  ; 
if  (  len  <  1  )  break  ; 
} 
int   bytesRead  =  in  .  read  (  data  ,  0  ,  len  )  ; 
if  (  bytesRead  <  0  )  { 
if  (  bytesToRead  >=  0  )  throw   new   IOException  (  "Unexpected EndOfStream! bytesToRead=="  +  bytesToRead  +  " but only "  +  bytesReadTotal  +  " bytes could be read from InputStream!"  )  ; 
break  ; 
} 
bytesReadTotal  +=  bytesRead  ; 
if  (  bytesRead  >  0  )  md  .  update  (  data  ,  0  ,  bytesRead  )  ; 
} 
return   md  .  digest  (  )  ; 
} 












public   static   byte  [  ]  hash  (  File   file  ,  String   algorithm  )  throws   NoSuchAlgorithmException  ,  IOException  { 
FileInputStream   in  =  new   FileInputStream  (  file  )  ; 
try  { 
return   hash  (  in  ,  -  1  ,  algorithm  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 










public   static   boolean   compareByteArrays  (  byte  [  ]  b1  ,  byte  [  ]  b2  )  { 
return   Arrays  .  equals  (  b1  ,  b2  )  ; 
} 











public   static   boolean   compareInputStreams  (  InputStream   in1  ,  InputStream   in2  ,  int   length  )  throws   IOException  { 
boolean   identical  =  true  ; 
int   read  =  0  ; 
while  (  read  <  length  )  { 
int   int1  =  in1  .  read  (  )  ; 
int   int2  =  in2  .  read  (  )  ; 
read  ++  ; 
if  (  int1  !=  int2  )  { 
identical  =  false  ; 
break  ; 
} 
} 
if  (  read  <  length  )  { 
in1  .  skip  (  length  -  read  )  ; 
in2  .  skip  (  length  -  read  )  ; 
} 
return   identical  ; 
} 








public   static   void   zipFolder  (  File   zipOutputFile  ,  File   zipInputFolder  )  throws   IOException  { 
FileOutputStream   fout  =  new   FileOutputStream  (  zipOutputFile  )  ; 
ZipOutputStream   out  =  new   ZipOutputStream  (  fout  )  ; 
try  { 
File  [  ]  files  =  zipInputFolder  .  listFiles  (  )  ; 
zipFilesRecursively  (  out  ,  zipOutputFile  ,  files  ,  zipInputFolder  .  getAbsoluteFile  (  )  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
} 
















public   static   void   zipFilesRecursively  (  ZipOutputStream   out  ,  File   zipOutputFile  ,  File  [  ]  files  ,  File   entryRoot  )  throws   IOException  { 
if  (  entryRoot  ==  null  &&  files  ==  null  )  throw   new   IllegalArgumentException  (  "entryRoot and files must not both be null!"  )  ; 
if  (  entryRoot  !=  null  &&  !  entryRoot  .  isDirectory  (  )  )  throw   new   IllegalArgumentException  (  "entryRoot is not a directory: "  +  entryRoot  .  getAbsolutePath  (  )  )  ; 
if  (  files  ==  null  )  { 
files  =  new   File  [  ]  {  entryRoot  }  ; 
} 
byte  [  ]  buf  =  new   byte  [  1024  *  5  ]  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
if  (  zipOutputFile  !=  null  )  if  (  file  .  equals  (  zipOutputFile  )  )  continue  ; 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  dirFiles  =  file  .  listFiles  (  )  ; 
zipFilesRecursively  (  out  ,  zipOutputFile  ,  dirFiles  ,  entryRoot  )  ; 
}  else  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
String   relativePath  =  entryRoot  ==  null  ?  file  .  getName  (  )  :  getRelativePath  (  entryRoot  ,  file  .  getAbsoluteFile  (  )  )  ; 
ZipEntry   entry  =  new   ZipEntry  (  relativePath  )  ; 
entry  .  setTime  (  file  .  lastModified  (  )  )  ; 
out  .  putNextEntry  (  entry  )  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
out  .  closeEntry  (  )  ; 
in  .  close  (  )  ; 
} 
} 
} 








public   static   void   unzipArchive  (  File   zipArchive  ,  File   unzipRootFolder  )  throws   IOException  { 
ZipFile   zipFile  =  new   ZipFile  (  zipArchive  )  ; 
Enumeration  <  ?  extends   ZipEntry  >  entries  =  zipFile  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
ZipEntry   entry  =  (  ZipEntry  )  entries  .  nextElement  (  )  ; 
if  (  entry  .  isDirectory  (  )  )  { 
File   dir  =  new   File  (  unzipRootFolder  ,  entry  .  getName  (  )  )  ; 
if  (  !  dir  .  mkdirs  (  )  )  throw   new   IllegalStateException  (  "Could not create directory entry, possibly permission issues."  )  ; 
}  else  { 
InputStream   in  =  zipFile  .  getInputStream  (  entry  )  ; 
File   file  =  new   File  (  unzipRootFolder  ,  entry  .  getName  (  )  )  ; 
File   dir  =  new   File  (  file  .  getParent  (  )  )  ; 
if  (  dir  .  exists  (  )  )  { 
assert  (  dir  .  isDirectory  (  )  )  ; 
}  else  { 
dir  .  mkdirs  (  )  ; 
} 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  file  )  )  ; 
int   len  ; 
byte  [  ]  buf  =  new   byte  [  1024  *  5  ]  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 
} 
zipFile  .  close  (  )  ; 
} 










public   static   double   truncateDouble  (  double   d  ,  int   numDigits  )  { 
double   multiplier  =  Math  .  pow  (  10  ,  numDigits  )  ; 
return  (  (  int  )  (  d  *  multiplier  )  )  /  multiplier  ; 
} 












public   static   double   getDouble  (  int   value  ,  int   numDigits  )  { 
double   multiplier  =  Math  .  pow  (  10  ,  numDigits  )  ; 
return  (  (  double  )  value  )  /  multiplier  ; 
} 




public   static   String   getStacktraceAsString  (  Throwable   t  )  { 
return   getStackTraceAsString  (  t  )  ; 
} 







public   static   String   getStackTraceAsString  (  Throwable   t  )  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
t  .  printStackTrace  (  new   PrintWriter  (  sw  )  )  ; 
return   sw  .  toString  (  )  ; 
} 







public   static   void   toFieldString  (  Object   o  ,  StringBuffer   s  )  { 
final   Field   fields  [  ]  =  o  .  getClass  (  )  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  ++  i  )  { 
s  .  append  (  ","  )  ; 
s  .  append  (  fields  [  i  ]  .  getName  (  )  )  ; 
s  .  append  (  "="  )  ; 
try  { 
fields  [  i  ]  .  setAccessible  (  true  )  ; 
s  .  append  (  fields  [  i  ]  .  get  (  o  )  )  ; 
}  catch  (  IllegalAccessException   ex  )  { 
s  .  append  (  "*private*"  )  ; 
} 
} 
} 






public   static   String   toFieldString  (  Object   o  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
toFieldString  (  o  ,  s  )  ; 
return   s  .  toString  (  )  ; 
} 






public   static   String   toString  (  Object   o  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
s  .  append  (  o  .  getClass  (  )  .  getName  (  )  )  ; 
s  .  append  (  "@"  )  ; 
s  .  append  (  Integer  .  toHexString  (  o  .  hashCode  (  )  )  )  ; 
s  .  append  (  "["  )  ; 
toFieldString  (  o  ,  s  )  ; 
s  .  append  (  "]"  )  ; 
return   s  .  toString  (  )  ; 
} 











public   static   String   byteArrayToHexString  (  byte   in  [  ]  )  { 
return   encodeHexStr  (  in  )  ; 
} 







public   static   URI   urlToUri  (  URL   url  )  throws   MalformedURLException  { 
if  (  url  ==  null  )  return   null  ; 
try  { 
return   new   URI  (  url  .  getProtocol  (  )  ,  url  .  getAuthority  (  )  ,  url  .  getPath  (  )  ,  url  .  getQuery  (  )  ,  url  .  getRef  (  )  )  ; 
}  catch  (  URISyntaxException   e  )  { 
MalformedURLException   newEx  =  new   MalformedURLException  (  "URL "  +  url  +  " was malformed"  )  ; 
newEx  .  initCause  (  e  )  ; 
throw   newEx  ; 
} 
} 
} 

