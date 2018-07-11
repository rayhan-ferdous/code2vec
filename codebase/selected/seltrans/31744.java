package   com  .  cromoteca  .  meshcms  .  server  .  toolbox  ; 

import   com  .  cromoteca  .  meshcms  .  client  .  toolbox  .  Path  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  CharArrayWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 

public   class   IO  { 

public   static   final   Charset   ISO_8859_1  =  Charset  .  forName  (  "iso-8859-1"  )  ; 

public   static   final   Charset   UTF_8  =  Charset  .  forName  (  "utf-8"  )  ; 




public   static   final   int   BUFFER_SIZE  =  4096  ; 




public   static   final   int   KBYTE  =  1024  ; 




public   static   final   int   MBYTE  =  KBYTE  *  KBYTE  ; 




public   static   final   int   GBYTE  =  MBYTE  *  KBYTE  ; 




public   static   final   String   SYSTEM_CHARSET  ; 




public   static   final   boolean   IS_MULTIBYTE_SYSTEM_CHARSET  ; 




public   static   final   String   FN_CHARMAP  =  "________________________________"  +  "__'____'()..--._0123456789..(-)."  +  "_ABCDEFGHIJKLMNOPQRSTUVWXYZ(_)__"  +  "'abcdefghijklmnopqrstuvwxyz(_)-_"  +  "________________________________"  +  "__cL.Y_P_Ca(__R-o-23'mP._10)423_"  +  "AAAAAAACEEEEIIIIENOOOOOxOUUUUYTS"  +  "aaaaaaaceeeeiiiienooooo-ouuuuyty"  ; 




public   static   final   String   FN_SPACERS  =  "_!'()-"  ; 

static  { 
String   systemCharset  =  System  .  getProperty  (  "file.encoding"  ,  ISO_8859_1  .  name  (  )  )  ; 
boolean   multibyte  =  false  ; 
try  { 
Charset   c  =  Charset  .  forName  (  systemCharset  )  ; 
systemCharset  =  c  .  toString  (  )  ; 
multibyte  =  c  .  newEncoder  (  )  .  maxBytesPerChar  (  )  >  1.0F  ; 
}  catch  (  Exception   ex  )  { 
} 
SYSTEM_CHARSET  =  systemCharset  ; 
IS_MULTIBYTE_SYSTEM_CHARSET  =  multibyte  ; 
} 











public   static   void   copyStream  (  InputStream   in  ,  OutputStream   out  ,  boolean   closeOut  )  throws   IOException  { 
byte  [  ]  b  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   n  ; 
try  { 
while  (  (  n  =  in  .  read  (  b  )  )  !=  -  1  )  { 
out  .  write  (  b  ,  0  ,  n  )  ; 
} 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  finally  { 
if  (  closeOut  )  { 
out  .  close  (  )  ; 
} 
} 
} 
} 










public   static   void   copyReaderToWriter  (  Reader   reader  ,  Writer   writer  ,  boolean   closeWriter  )  throws   IOException  { 
char  [  ]  c  =  new   char  [  BUFFER_SIZE  ]  ; 
int   n  ; 
while  (  (  n  =  reader  .  read  (  c  )  )  !=  -  1  )  { 
writer  .  write  (  c  ,  0  ,  n  )  ; 
} 
reader  .  close  (  )  ; 
writer  .  flush  (  )  ; 
if  (  closeWriter  )  { 
writer  .  close  (  )  ; 
} 
} 





public   static   String   fixFileName  (  String   text  ,  boolean   spacers  )  { 
char  [  ]  chars  =  text  .  toCharArray  (  )  ; 
StringBuilder   sb  =  new   StringBuilder  (  chars  .  length  )  ; 
boolean   needSeparator  =  false  ; 
for  (  char   c  :  chars  )  { 
if  (  c  <  256  )  { 
c  =  FN_CHARMAP  .  charAt  (  c  )  ; 
} 
if  (  Character  .  isLetterOrDigit  (  c  )  )  { 
if  (  needSeparator  )  { 
if  (  spacers  &&  sb  .  length  (  )  >  0  )  { 
sb  .  append  (  '-'  )  ; 
} 
needSeparator  =  false  ; 
} 
sb  .  append  (  Character  .  toLowerCase  (  c  )  )  ; 
}  else  { 
needSeparator  =  true  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 









public   static   void   write  (  String   s  ,  File   file  )  throws   IOException  { 
write  (  s  ,  file  ,  SYSTEM_CHARSET  )  ; 
} 

public   static   void   write  (  String   s  ,  File   file  ,  String   charset  )  throws   IOException  { 
Writer   writer  =  new   BufferedWriter  (  new   OutputStreamWriter  (  new   FileOutputStream  (  file  )  ,  charset  )  )  ; 
writer  .  write  (  s  )  ; 
writer  .  close  (  )  ; 
} 













public   static   boolean   copyFile  (  File   file  ,  File   newFile  ,  boolean   overwrite  ,  boolean   setLastModified  )  throws   IOException  { 
if  (  newFile  .  exists  (  )  &&  !  overwrite  )  { 
return   false  ; 
} 
newFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
InputStream   fis  =  null  ; 
try  { 
fis  =  new   FileInputStream  (  file  )  ; 
copyStream  (  fis  ,  new   FileOutputStream  (  newFile  )  ,  true  )  ; 
if  (  setLastModified  )  { 
newFile  .  setLastModified  (  file  .  lastModified  (  )  )  ; 
} 
}  finally  { 
if  (  fis  !=  null  )  { 
fis  .  close  (  )  ; 
} 
} 
return   true  ; 
} 










public   static   String   readFully  (  File   file  )  throws   IOException  { 
Reader   reader  =  new   InputStreamReader  (  new   FileInputStream  (  file  )  )  ; 
String   s  =  readFully  (  reader  )  ; 
reader  .  close  (  )  ; 
return   s  ; 
} 

public   static   byte  [  ]  readAllBytes  (  File   file  )  throws   IOException  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
copyStream  (  new   FileInputStream  (  file  )  ,  baos  ,  true  )  ; 
return   baos  .  toByteArray  (  )  ; 
} 

public   static   void   writeAllBytes  (  byte  [  ]  b  ,  File   file  )  throws   IOException  { 
ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  b  )  ; 
copyStream  (  bais  ,  new   FileOutputStream  (  file  )  ,  true  )  ; 
} 

public   static   String   readFully  (  File   file  ,  Charset   charset  )  throws   IOException  { 
Reader   reader  =  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  charset  )  ; 
String   s  =  readFully  (  reader  )  ; 
reader  .  close  (  )  ; 
return   s  ; 
} 










public   static   String   readFully  (  Reader   reader  )  throws   IOException  { 
CharArrayWriter   caw  =  new   CharArrayWriter  (  )  ; 
char  [  ]  cbuf  =  new   char  [  BUFFER_SIZE  ]  ; 
int   n  ; 
while  (  (  n  =  reader  .  read  (  cbuf  )  )  !=  -  1  )  { 
caw  .  write  (  cbuf  ,  0  ,  n  )  ; 
} 
return   caw  .  toString  (  )  ; 
} 









public   static   void   unzip  (  File   zip  ,  File   dir  )  throws   IOException  { 
dir  .  mkdirs  (  )  ; 
InputStream   in  =  new   BufferedInputStream  (  new   FileInputStream  (  zip  )  )  ; 
ZipInputStream   zin  =  new   ZipInputStream  (  in  )  ; 
ZipEntry   e  ; 
while  (  (  e  =  zin  .  getNextEntry  (  )  )  !=  null  )  { 
File   f  =  new   File  (  dir  ,  e  .  getName  (  )  )  ; 
if  (  e  .  isDirectory  (  )  )  { 
f  .  mkdirs  (  )  ; 
}  else  { 
f  .  getParentFile  (  )  .  mkdirs  (  )  ; 
OutputStream   out  =  new   FileOutputStream  (  f  )  ; 
byte  [  ]  b  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   len  ; 
while  (  (  len  =  zin  .  read  (  b  )  )  !=  -  1  )  { 
out  .  write  (  b  ,  0  ,  len  )  ; 
} 
out  .  close  (  )  ; 
} 
} 
zin  .  close  (  )  ; 
} 





public   static   boolean   copyDirectory  (  File   dir  ,  File   newDir  ,  boolean   overwriteDir  ,  boolean   overwriteFiles  ,  boolean   setLastModified  )  { 
DirectoryCopier   dc  =  new   DirectoryCopier  (  dir  ,  newDir  ,  overwriteDir  ,  overwriteFiles  ,  setLastModified  )  ; 
dc  .  process  (  )  ; 
return   dc  .  getResult  (  )  ; 
} 











public   static   boolean   forceDelete  (  File   file  )  { 
if  (  !  file  .  exists  (  )  )  { 
return   true  ; 
} 
if  (  file  .  isDirectory  (  )  )  { 
return   file  .  delete  (  )  ; 
} 
for  (  int   i  =  1  ;  i  <  20  ;  i  ++  )  { 
if  (  file  .  delete  (  )  )  { 
return   true  ; 
} 
try  { 
Thread  .  sleep  (  i  *  100L  )  ; 
}  catch  (  InterruptedException   ex  )  { 
} 
} 
return   false  ; 
} 












public   static   boolean   forceRenameTo  (  File   oldFile  ,  File   newFile  ,  boolean   overwrite  )  { 
if  (  newFile  .  exists  (  )  )  { 
if  (  overwrite  )  { 
if  (  !  forceDelete  (  newFile  )  )  { 
return   false  ; 
} 
}  else  { 
return   false  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  20  ;  i  ++  )  { 
if  (  oldFile  .  renameTo  (  newFile  )  )  { 
return   true  ; 
} 
try  { 
Thread  .  sleep  (  i  *  100L  )  ; 
}  catch  (  InterruptedException   ex  )  { 
} 
} 
return   false  ; 
} 





public   static   String   generateUniqueName  (  String   fileName  ,  File   directory  )  { 
if  (  directory  .  isDirectory  (  )  )  { 
return   generateUniqueName  (  fileName  ,  directory  .  list  (  )  )  ; 
} 
return   null  ; 
} 













public   static   String   generateUniqueName  (  String   fileName  ,  String  [  ]  files  )  { 
if  (  Strings  .  searchString  (  files  ,  fileName  ,  true  )  ==  -  1  )  { 
return   fileName  ; 
} 
String   ext  =  ""  ; 
int   idx  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  idx  !=  -  1  )  { 
ext  =  fileName  .  substring  (  idx  )  ; 
fileName  =  fileName  .  substring  (  0  ,  idx  )  ; 
} 
int   d  =  0  ; 
for  (  int   i  =  fileName  .  length  (  )  -  1  ;  i  >=  0  ;  i  --  )  { 
if  (  !  Character  .  isDigit  (  fileName  .  charAt  (  i  )  )  )  { 
d  =  i  +  1  ; 
break  ; 
} 
} 
int   number  =  Strings  .  parseInt  (  fileName  .  substring  (  d  )  ,  1  )  ; 
fileName  =  fileName  .  substring  (  0  ,  d  )  ; 
String   temp  ; 
do  { 
temp  =  fileName  +  ++  number  +  ext  ; 
}  while  (  Strings  .  searchString  (  files  ,  temp  ,  true  )  !=  -  1  )  ; 
return   temp  ; 
} 

public   static   File   getFileFromPath  (  File   parent  ,  Path   path  )  { 
return   path  .  getElementCount  (  )  ==  0  ?  parent  :  new   File  (  parent  ,  path  .  toString  (  )  )  ; 
} 
} 

