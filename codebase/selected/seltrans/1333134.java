package   org  .  meshcms  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  CharArrayWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 






public   final   class   Utils  { 

private   Utils  (  )  { 
} 




public   static   final   String   VALID_CHARS  =  "abcdefghijkmnpqrstuvwxyz23456789"  ; 




public   static   final   String   INVALID_FILENAME_CHARS  =  "\"\\/:*?<>| ,\t\n\r"  ; 




public   static   final   String   VALID_FILENAME_CHARS  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'._-"  ; 




public   static   final   int   BUFFER_SIZE  =  2048  ; 




public   static   final   int   KBYTE  =  1024  ; 




public   static   final   int   MBYTE  =  KBYTE  *  KBYTE  ; 




public   static   final   int   GBYTE  =  MBYTE  *  KBYTE  ; 

public   static   final   String   SYSTEM_CHARSET  ; 

public   static   final   boolean   IS_MULTIBYTE_SYSTEM_CHARSET  ; 

static  { 
String   s  =  System  .  getProperty  (  "file.encoding"  ,  "ISO-8859-1"  )  ; 
boolean   multibyte  =  true  ; 
try  { 
Charset   c  =  Charset  .  forName  (  s  )  ; 
s  =  c  .  toString  (  )  ; 
multibyte  =  c  .  newEncoder  (  )  .  maxBytesPerChar  (  )  >  1.0F  ; 
}  catch  (  Exception   ex  )  { 
} 
SYSTEM_CHARSET  =  s  ; 
IS_MULTIBYTE_SYSTEM_CHARSET  =  multibyte  ; 
} 





public   static   String   addDigits  (  int   num  ,  char   space  ,  int   len  )  { 
return   addDigits  (  Integer  .  toString  (  num  )  ,  space  ,  len  )  ; 
} 





public   static   String   addDigits  (  String   s  ,  char   space  ,  int   len  )  { 
if  (  s  ==  null  )  { 
s  =  ""  ; 
} 
while  (  s  .  length  (  )  <  len  )  { 
s  =  space  +  s  ; 
} 
return   s  ; 
} 







public   static   boolean   isNullOrEmpty  (  String   s  )  { 
return   s  ==  null  ||  s  .  length  (  )  ==  0  ; 
} 







public   static   boolean   isNullOrWhitespace  (  String   s  )  { 
if  (  s  ==  null  )  { 
return   true  ; 
} 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  !  Character  .  isWhitespace  (  s  .  charAt  (  i  )  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 










public   static   String   trim  (  String   s  )  { 
return  (  s  ==  null  )  ?  null  :  s  .  trim  (  )  ; 
} 








public   static   String   noNull  (  String   s  )  { 
return   s  ==  null  ?  ""  :  s  ; 
} 









public   static   String   noNull  (  String   s  ,  String   def  )  { 
return   s  ==  null  ?  def  :  s  ; 
} 












public   static   boolean   compareStrings  (  String   s1  ,  String   s2  ,  boolean   ignoreCase  )  { 
if  (  s1  ==  null  &&  s2  ==  null  )  { 
return   true  ; 
} 
if  (  s1  !=  null  &&  s2  !=  null  )  { 
return   ignoreCase  ?  s1  .  equalsIgnoreCase  (  s2  )  :  s1  .  equals  (  s2  )  ; 
} 
return   false  ; 
} 











public   static   boolean   isTrue  (  String   s  )  { 
return   isTrue  (  s  ,  false  )  ; 
} 

public   static   boolean   isTrue  (  String   s  ,  boolean   checkFalse  )  { 
boolean   result  =  false  ; 
if  (  s  ==  null  )  { 
if  (  checkFalse  )  { 
throw   new   IllegalArgumentException  (  )  ; 
} 
}  else  { 
s  =  s  .  trim  (  )  .  toLowerCase  (  )  ; 
if  (  s  .  equals  (  "true"  )  ||  s  .  equals  (  "1"  )  ||  s  .  equals  (  "yes"  )  ||  s  .  equals  (  "ok"  )  ||  s  .  equals  (  "checked"  )  ||  s  .  equals  (  "selected"  )  ||  s  .  equals  (  "on"  )  )  { 
result  =  true  ; 
}  else   if  (  checkFalse  )  { 
if  (  !  (  s  .  equals  (  "false"  )  ||  s  .  equals  (  "0"  )  ||  s  .  equals  (  "no"  )  ||  s  .  equals  (  "off"  )  )  )  { 
throw   new   IllegalArgumentException  (  )  ; 
} 
} 
} 
return   result  ; 
} 










public   static   String   limitedLength  (  String   s  ,  int   len  )  { 
String   s1  ; 
if  (  isNullOrEmpty  (  s  )  )  { 
s1  =  ""  ; 
}  else   if  (  s  .  length  (  )  <=  len  )  { 
s1  =  s  ; 
}  else   if  (  len  <  5  )  { 
s1  =  "..."  ; 
}  else  { 
s1  =  s  .  substring  (  0  ,  len  -  4  )  +  " ..."  ; 
} 
return   s1  ; 
} 








public   static   String   escapeSingleQuotes  (  String   s  )  { 
return   replace  (  s  ,  '\''  ,  "\\'"  )  ; 
} 










public   static   String   replace  (  String   s  ,  char   c  ,  String   n  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  s  .  charAt  (  i  )  ==  c  )  { 
sb  .  append  (  n  )  ; 
}  else  { 
sb  .  append  (  s  .  charAt  (  i  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 











public   static   String   encodeHTML  (  String   s  )  { 
return   encodeHTML  (  s  ,  false  )  ; 
} 
















public   static   String   encodeHTML  (  String   s  ,  boolean   encodeAmpersands  )  { 
if  (  isNullOrEmpty  (  s  )  )  { 
return  ""  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  s  .  length  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
char   c  =  s  .  charAt  (  i  )  ; 
switch  (  c  )  { 
case  '\"'  : 
sb  .  append  (  "&quot;"  )  ; 
break  ; 
case  '&'  : 
if  (  encodeAmpersands  )  { 
sb  .  append  (  "&amp;"  )  ; 
}  else  { 
sb  .  append  (  c  )  ; 
} 
break  ; 
case  '\''  : 
sb  .  append  (  "&#39;"  )  ; 
break  ; 
case  '<'  : 
sb  .  append  (  "&lt;"  )  ; 
break  ; 
case  '>'  : 
sb  .  append  (  "&gt;"  )  ; 
break  ; 
default  : 
sb  .  append  (  c  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 









public   static   String   decodeHTML  (  String   s  )  { 
if  (  isNullOrEmpty  (  s  )  )  { 
return  ""  ; 
} 
int   sl  =  s  .  length  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  sl  )  ; 
int   i  =  0  ; 
String   s0  ; 
while  (  i  <  sl  -  3  )  { 
s0  =  s  .  substring  (  i  ,  i  +  4  )  ; 
if  (  s0  .  equals  (  "&gt;"  )  )  { 
sb  .  append  (  '>'  )  ; 
i  +=  4  ; 
}  else   if  (  s0  .  equals  (  "&lt;"  )  )  { 
sb  .  append  (  '<'  )  ; 
i  +=  4  ; 
}  else   if  (  s0  .  equals  (  "&amp"  )  )  { 
if  (  i  <  sl  -  4  &&  s  .  charAt  (  i  +  4  )  ==  ';'  )  { 
sb  .  append  (  '&'  )  ; 
i  +=  5  ; 
} 
}  else   if  (  s0  .  equals  (  "&#39"  )  )  { 
if  (  i  <  sl  -  4  &&  s  .  charAt  (  i  +  4  )  ==  ';'  )  { 
sb  .  append  (  '\''  )  ; 
i  +=  5  ; 
} 
}  else   if  (  s0  .  equals  (  "&quo"  )  )  { 
if  (  i  <  sl  -  5  &&  s  .  charAt  (  i  +  4  )  ==  't'  &&  s  .  charAt  (  i  +  5  )  ==  ';'  )  { 
sb  .  append  (  '\"'  )  ; 
i  +=  6  ; 
} 
}  else  { 
sb  .  append  (  s  .  charAt  (  i  ++  )  )  ; 
} 
} 
return   sb  .  append  (  s  .  substring  (  i  )  )  .  toString  (  )  ; 
} 








public   static   String   stripHTMLTags  (  String   s  )  { 
return  (  s  !=  null  )  ?  s  .  replaceAll  (  "</?\\S+?[\\s\\S+]*?>"  ,  " "  )  :  null  ; 
} 












public   static   boolean   copyFile  (  File   file  ,  File   newFile  ,  boolean   overwrite  ,  boolean   setLastModified  )  throws   IOException  { 
if  (  newFile  .  exists  (  )  &&  !  overwrite  )  { 
return   false  ; 
} 
FileInputStream   fis  =  null  ; 
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





public   static   boolean   copyDirectory  (  File   dir  ,  File   newDir  ,  boolean   overwriteDir  ,  boolean   overwriteFiles  ,  boolean   setLastModified  )  { 
DirectoryCopier   dc  =  new   DirectoryCopier  (  dir  ,  newDir  ,  overwriteDir  ,  overwriteFiles  ,  setLastModified  )  ; 
dc  .  process  (  )  ; 
return   dc  .  getResult  (  )  ; 
} 











public   static   void   copyStream  (  InputStream   in  ,  OutputStream   out  ,  boolean   closeOut  )  throws   IOException  { 
byte   b  [  ]  =  new   byte  [  BUFFER_SIZE  ]  ; 
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
char   c  [  ]  =  new   char  [  BUFFER_SIZE  ]  ; 
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









public   static   void   writeFully  (  File   file  ,  String   s  )  throws   IOException  { 
Writer   writer  =  new   BufferedWriter  (  new   OutputStreamWriter  (  new   FileOutputStream  (  file  )  ,  SYSTEM_CHARSET  )  )  ; 
writer  .  write  (  s  )  ; 
writer  .  close  (  )  ; 
} 









public   static   void   writeFully  (  File   file  ,  byte  [  ]  b  )  throws   IOException  { 
FileOutputStream   fos  =  new   FileOutputStream  (  file  )  ; 
fos  .  write  (  b  )  ; 
fos  .  close  (  )  ; 
} 










public   static   String   readFully  (  File   file  )  throws   IOException  { 
Reader   reader  =  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  SYSTEM_CHARSET  )  ; 
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










public   static   char  [  ]  readAllChars  (  Reader   reader  )  throws   IOException  { 
CharArrayWriter   caw  =  new   CharArrayWriter  (  )  ; 
char  [  ]  cbuf  =  new   char  [  BUFFER_SIZE  ]  ; 
int   n  ; 
while  (  (  n  =  reader  .  read  (  cbuf  )  )  !=  -  1  )  { 
caw  .  write  (  cbuf  ,  0  ,  n  )  ; 
} 
return   caw  .  toCharArray  (  )  ; 
} 











public   static   byte  [  ]  readFully  (  InputStream   in  )  throws   IOException  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
byte  [  ]  buf  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   n  ; 
while  (  (  n  =  in  .  read  (  buf  )  )  !=  -  1  )  { 
baos  .  write  (  buf  ,  0  ,  n  )  ; 
} 
return   baos  .  toByteArray  (  )  ; 
} 










public   static   String  [  ]  readAllLines  (  File   file  )  throws   FileNotFoundException  ,  IOException  { 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  SYSTEM_CHARSET  )  )  ; 
List   list  =  new   ArrayList  (  )  ; 
String   line  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
list  .  add  (  line  )  ; 
} 
reader  .  close  (  )  ; 
return  (  String  [  ]  )  list  .  toArray  (  new   String  [  list  .  size  (  )  ]  )  ; 
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
FileOutputStream   out  =  new   FileOutputStream  (  f  )  ; 
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





public   static   String   generateRandomString  (  int   len  )  { 
StringBuffer   sb  =  new   StringBuffer  (  len  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
sb  .  append  (  Utils  .  VALID_CHARS  .  charAt  (  (  int  )  (  Math  .  random  (  )  *  Utils  .  VALID_CHARS  .  length  (  )  )  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 






public   static   String   generateList  (  Object  [  ]  list  ,  String   sep  )  { 
if  (  list  ==  null  )  { 
return   null  ; 
} 
if  (  list  .  length  ==  0  )  { 
return  ""  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  list  [  0  ]  )  ; 
for  (  int   i  =  1  ;  i  <  list  .  length  ;  i  ++  )  { 
sb  .  append  (  sep  )  .  append  (  list  [  i  ]  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   String   listProperties  (  Properties   p  ,  String   sep  )  { 
if  (  p  ==  null  )  { 
return   null  ; 
} 
Enumeration   names  =  p  .  propertyNames  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
if  (  names  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  names  .  nextElement  (  )  ; 
sb  .  append  (  name  )  .  append  (  '='  )  .  append  (  p  .  getProperty  (  name  )  )  ; 
} 
while  (  names  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  names  .  nextElement  (  )  ; 
sb  .  append  (  sep  )  .  append  (  name  )  .  append  (  '='  )  .  append  (  p  .  getProperty  (  name  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 






public   static   String   generateList  (  Collection   c  ,  String   sep  )  { 
if  (  c  ==  null  )  { 
return   null  ; 
} 
if  (  c  .  size  (  )  ==  0  )  { 
return  ""  ; 
} 
Iterator   iter  =  c  .  iterator  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
if  (  iter  .  hasNext  (  )  )  { 
sb  .  append  (  iter  .  next  (  )  .  toString  (  )  )  ; 
} 
while  (  iter  .  hasNext  (  )  )  { 
sb  .  append  (  sep  )  .  append  (  iter  .  next  (  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 






public   static   String   generateList  (  Enumeration   e  ,  String   sep  )  { 
if  (  e  ==  null  )  { 
return   null  ; 
} 
if  (  !  e  .  hasMoreElements  (  )  )  { 
return  ""  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
if  (  e  .  hasMoreElements  (  )  )  { 
sb  .  append  (  e  .  nextElement  (  )  .  toString  (  )  )  ; 
} 
while  (  e  .  hasMoreElements  (  )  )  { 
sb  .  append  (  sep  )  .  append  (  e  .  nextElement  (  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 






public   static   String   generateList  (  int  [  ]  list  ,  String   sep  )  { 
if  (  list  ==  null  )  { 
return   null  ; 
} 
if  (  list  .  length  ==  0  )  { 
return  ""  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  Integer  .  toString  (  list  [  0  ]  )  )  ; 
for  (  int   i  =  1  ;  i  <  list  .  length  ;  i  ++  )  { 
sb  .  append  (  sep  )  .  append  (  list  [  i  ]  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   int   sum  (  int  [  ]  ints  )  { 
int   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  ints  .  length  ;  i  ++  )  { 
s  +=  ints  [  i  ]  ; 
} 
return   s  ; 
} 





public   static   String   generateUniqueName  (  String   fileName  ,  File   directory  )  { 
if  (  directory  .  isDirectory  (  )  )  { 
return   generateUniqueName  (  fileName  ,  directory  .  list  (  )  )  ; 
} 
return   null  ; 
} 







public   static   String   generateUniqueDosName  (  String   fileName  ,  String  [  ]  files  )  { 
fileName  =  fileName  .  toLowerCase  (  )  ; 
String   ext  =  ""  ; 
int   idx  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  idx  !=  -  1  )  { 
ext  =  fileName  .  substring  (  idx  )  ; 
if  (  ext  .  length  (  )  >  4  )  { 
ext  =  ext  .  substring  (  0  ,  4  )  ; 
} 
fileName  =  fileName  .  substring  (  0  ,  idx  )  ; 
} 
String   name  =  ""  ; 
for  (  int   i  =  0  ;  i  <  fileName  .  length  (  )  ;  i  ++  )  { 
char   c  =  fileName  .  charAt  (  i  )  ; 
if  (  Character  .  isLetterOrDigit  (  c  )  )  { 
name  +=  c  ; 
if  (  name  .  length  (  )  ==  8  )  { 
break  ; 
} 
} 
} 
if  (  name  .  length  (  )  ==  0  )  { 
name  =  "file"  ; 
} 
if  (  searchString  (  files  ,  name  +  ext  ,  true  )  <  0  )  { 
return   name  +  ext  ; 
} 
int   limit  =  1  ; 
for  (  int   i  =  1  ;  i  <=  8  ;  i  ++  )  { 
int   first  =  limit  ; 
limit  *=  10  ; 
String   base  =  (  name  .  length  (  )  <=  7  -  i  )  ?  name  :  name  .  substring  (  0  ,  7  -  i  )  ; 
base  +=  "_"  ; 
for  (  int   j  =  first  ;  j  <  limit  ;  j  ++  )  { 
String   temp  =  base  +  j  +  ext  ; 
if  (  searchString  (  files  ,  temp  ,  true  )  <  0  )  { 
return   temp  ; 
} 
} 
} 
return   null  ; 
} 












public   static   String   generateUniqueName  (  String   fileName  ,  String  [  ]  files  )  { 
if  (  searchString  (  files  ,  fileName  ,  true  )  ==  -  1  )  { 
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
int   number  =  parseInt  (  fileName  .  substring  (  d  )  ,  1  )  ; 
fileName  =  fileName  .  substring  (  0  ,  d  )  ; 
String   temp  ; 
do  { 
temp  =  fileName  +  ++  number  +  ext  ; 
}  while  (  searchString  (  files  ,  temp  ,  true  )  !=  -  1  )  ; 
return   temp  ; 
} 












public   static   int   parseInt  (  String   s  ,  int   def  )  { 
try  { 
def  =  Integer  .  parseInt  (  s  )  ; 
}  catch  (  Exception   ex  )  { 
} 
return   def  ; 
} 












public   static   long   parseLong  (  String   s  ,  long   def  )  { 
try  { 
def  =  Long  .  parseLong  (  s  )  ; 
}  catch  (  Exception   ex  )  { 
} 
return   def  ; 
} 










public   static   String  [  ]  tokenize  (  String   s  )  { 
return   tokenize  (  s  ,  null  )  ; 
} 









public   static   String  [  ]  tokenize  (  String   s  ,  String   delim  )  { 
if  (  s  ==  null  )  { 
return   null  ; 
} 
StringTokenizer   st  ; 
if  (  isNullOrEmpty  (  delim  )  )  { 
st  =  new   StringTokenizer  (  s  )  ; 
}  else  { 
st  =  new   StringTokenizer  (  s  ,  delim  )  ; 
} 
String  [  ]  res  =  new   String  [  st  .  countTokens  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  res  .  length  ;  i  ++  )  { 
res  [  i  ]  =  st  .  nextToken  (  )  ; 
} 
return   res  ; 
} 










public   static   int   searchString  (  String  [  ]  array  ,  String   s  ,  boolean   ignoreCase  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  ||  s  ==  null  )  { 
return  -  1  ; 
} 
if  (  ignoreCase  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  s  .  equalsIgnoreCase  (  array  [  i  ]  )  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  s  .  equals  (  array  [  i  ]  )  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 

public   static   int   searchInt  (  int  [  ]  array  ,  int   n  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return  -  1  ; 
} 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  n  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return  -  1  ; 
} 








public   static   boolean   checkAddress  (  String   email  )  { 
if  (  isNullOrEmpty  (  email  )  ||  email  .  indexOf  (  ' '  )  >=  0  )  { 
return   false  ; 
} 
int   dot  =  email  .  lastIndexOf  (  '.'  )  ; 
int   at  =  email  .  lastIndexOf  (  '@'  )  ; 
return  !  (  dot  <  0  ||  at  <  0  ||  at  >  dot  )  ; 
} 








public   static   int   getRandomInt  (  int   max  )  { 
return  (  int  )  (  Math  .  random  (  )  *  max  )  ; 
} 








public   static   Object   getRandomElement  (  Object  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   null  ; 
} 
return   array  [  getRandomInt  (  array  .  length  )  ]  ; 
} 








public   static   float   decimalPart  (  float   f  )  { 
return   f  -  (  int  )  f  ; 
} 








public   static   double   decimalPart  (  double   d  )  { 
return   d  -  (  long  )  d  ; 
} 








public   static   int   sign  (  int   n  )  { 
if  (  n  <  0  )  { 
return  -  1  ; 
} 
if  (  n  >  0  )  { 
return   1  ; 
} 
return   0  ; 
} 











public   static   int   constrain  (  int   min  ,  int   max  ,  int   n  )  { 
if  (  n  <  min  )  { 
return   min  ; 
} 
if  (  n  >  max  )  { 
return   max  ; 
} 
return   n  ; 
} 










public   static   String   addAtEnd  (  String   base  ,  String   what  )  { 
if  (  base  ==  null  )  { 
base  =  what  ; 
}  else   if  (  !  base  .  endsWith  (  what  )  )  { 
base  +=  what  ; 
} 
return   base  ; 
} 










public   static   String   removeAtEnd  (  String   base  ,  String   what  )  { 
if  (  base  !=  null  &&  base  .  endsWith  (  what  )  )  { 
base  =  base  .  substring  (  0  ,  base  .  length  (  )  -  what  .  length  (  )  )  ; 
} 
return   base  ; 
} 










public   static   String   addAtBeginning  (  String   base  ,  String   what  )  { 
if  (  base  ==  null  )  { 
base  =  what  ; 
}  else   if  (  !  base  .  startsWith  (  what  )  )  { 
base  =  what  +  base  ; 
} 
return   base  ; 
} 










public   static   String   removeAtBeginning  (  String   base  ,  String   what  )  { 
if  (  base  !=  null  &&  base  .  startsWith  (  what  )  )  { 
base  =  base  .  substring  (  what  .  length  (  )  )  ; 
} 
return   base  ; 
} 












public   static   String   getRelativePath  (  File   folder  ,  File   file  ,  String   separator  )  { 
return   getRelativePath  (  getFilePath  (  folder  )  ,  getFilePath  (  file  )  ,  separator  )  ; 
} 












public   static   String   getRelativePath  (  String   folder  ,  String   file  ,  String   separator  )  { 
String  [  ]  a0  =  Utils  .  tokenize  (  folder  ,  "/\\"  )  ; 
String  [  ]  a1  =  Utils  .  tokenize  (  file  ,  "/\\"  )  ; 
int   i0  =  0  ; 
int   i1  =  0  ; 
String   result  =  ""  ; 
while  (  i0  <  a0  .  length  &&  i1  <  a1  .  length  &&  a0  [  i0  ]  .  equals  (  a1  [  i1  ]  )  )  { 
i0  ++  ; 
i1  ++  ; 
} 
while  (  i0  ++  <  a0  .  length  )  { 
result  +=  ".."  +  separator  ; 
} 
while  (  i1  <  a1  .  length  -  1  )  { 
result  +=  a1  [  i1  ++  ]  +  separator  ; 
} 
if  (  i1  ==  a1  .  length  -  1  )  { 
result  +=  a1  [  i1  ]  ; 
} 
return   result  ; 
} 













public   static   String   getCombinedPath  (  String   folder  ,  String   file  ,  String   separator  )  { 
String  [  ]  a0  =  Utils  .  tokenize  (  folder  ,  "/\\"  )  ; 
String  [  ]  a1  =  Utils  .  tokenize  (  file  ,  "/\\"  )  ; 
int   i0  =  a0  .  length  ; 
int   i1  =  0  ; 
while  (  i1  <  a1  .  length  &&  a1  [  i1  ]  .  equals  (  ".."  )  )  { 
i0  --  ; 
i1  ++  ; 
} 
if  (  i0  <  0  )  { 
throw   new   IllegalArgumentException  (  "Not enough levels"  )  ; 
} 
String   result  =  null  ; 
for  (  int   i  =  0  ;  i  <  i0  ;  i  ++  )  { 
if  (  !  a0  [  i  ]  .  equals  (  "."  )  )  { 
result  =  (  result  ==  null  )  ?  a0  [  i  ]  :  result  +  separator  +  a0  [  i  ]  ; 
} 
} 
for  (  int   i  =  i1  ;  i  <  a1  .  length  ;  i  ++  )  { 
if  (  !  a1  [  i  ]  .  equals  (  "."  )  )  { 
result  =  (  result  ==  null  )  ?  a1  [  i  ]  :  result  +  separator  +  a1  [  i  ]  ; 
} 
} 
return   noNull  (  result  )  ; 
} 










public   static   String   getFilePath  (  File   f  )  { 
try  { 
return   f  .  getCanonicalPath  (  )  ; 
}  catch  (  IOException   ex  )  { 
} 
return   f  .  getAbsolutePath  (  )  ; 
} 

public   static   String   getExtension  (  File   file  ,  boolean   includeDot  )  { 
return   getExtension  (  file  .  getName  (  )  ,  includeDot  )  ; 
} 

public   static   String   getExtension  (  Path   path  ,  boolean   includeDot  )  { 
return   getExtension  (  path  .  getLastElement  (  )  ,  includeDot  )  ; 
} 









public   static   String   getExtension  (  String   fileName  ,  boolean   includeDot  )  { 
if  (  fileName  ==  null  )  { 
return   null  ; 
} 
int   dot  =  fileName  .  lastIndexOf  (  '.'  )  ; 
return  (  dot  ==  -  1  )  ?  ""  :  fileName  .  substring  (  includeDot  ?  dot  :  dot  +  1  )  .  toLowerCase  (  )  ; 
} 

public   static   File   replaceExtension  (  File   file  ,  String   ext  )  { 
return   new   File  (  replaceExtension  (  getFilePath  (  file  )  ,  ext  )  )  ; 
} 

public   static   Path   replaceExtension  (  Path   path  ,  String   ext  )  { 
return   new   Path  (  replaceExtension  (  path  .  toString  (  )  ,  ext  )  )  ; 
} 

public   static   String   replaceExtension  (  String   filePath  ,  String   ext  )  { 
int   dot  =  filePath  .  lastIndexOf  (  '.'  )  ; 
int   slash  =  filePath  .  lastIndexOf  (  '/'  )  ; 
ext  =  addAtBeginning  (  ext  ,  "."  )  ; 
if  (  dot  <  0  )  { 
filePath  +=  ext  ; 
}  else   if  (  dot  >  slash  )  { 
filePath  =  filePath  .  substring  (  0  ,  dot  )  +  ext  ; 
} 
return   filePath  ; 
} 








public   static   String   removeExtension  (  Object   o  )  { 
String   fileName  =  null  ; 
if  (  o   instanceof   File  )  { 
fileName  =  (  (  File  )  o  )  .  getName  (  )  ; 
}  else   if  (  o   instanceof   Path  )  { 
fileName  =  (  (  Path  )  o  )  .  getLastElement  (  )  ; 
}  else   if  (  o  !=  null  )  { 
fileName  =  o  .  toString  (  )  ; 
} 
if  (  fileName  ==  null  )  { 
return   null  ; 
} 
int   dot  =  fileName  .  lastIndexOf  (  '.'  )  ; 
return  (  dot  ==  -  1  )  ?  fileName  :  fileName  .  substring  (  0  ,  dot  )  ; 
} 










public   static   String   getCommonPart  (  String   s1  ,  String   s2  )  { 
if  (  s1  ==  null  ||  s2  ==  null  )  { 
return   null  ; 
} 
int   len  =  Math  .  min  (  s1  .  length  (  )  ,  s2  .  length  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
if  (  s1  .  charAt  (  i  )  !=  s2  .  charAt  (  i  )  )  { 
return   s1  .  substring  (  0  ,  i  )  ; 
} 
} 
return   s1  .  length  (  )  <  s2  .  length  (  )  ?  s1  :  s2  ; 
} 










public   static   String   beautify  (  String   s  ,  boolean   titleCase  )  { 
StringBuffer   sb  =  new   StringBuffer  (  s  .  length  (  )  )  ; 
boolean   nextUpper  =  true  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
char   c  =  s  .  charAt  (  i  )  ; 
if  (  c  ==  '_'  )  { 
c  =  ' '  ; 
} 
if  (  c  ==  ' '  )  { 
nextUpper  =  true  ; 
}  else  { 
if  (  titleCase  &&  nextUpper  )  { 
c  =  Character  .  toTitleCase  (  c  )  ; 
nextUpper  =  false  ; 
} 
} 
sb  .  append  (  c  )  ; 
} 
return   sb  .  toString  (  )  ; 
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










public   static   String   formatFileLength  (  File   file  )  { 
return   formatFileLength  (  file  .  length  (  )  )  ; 
} 









public   static   String   formatFileLength  (  long   length  )  { 
DecimalFormat   format  =  new   DecimalFormat  (  "###0.##"  )  ; 
double   num  =  length  ; 
String   unit  ; 
if  (  length  <  KBYTE  )  { 
unit  =  "B"  ; 
}  else   if  (  length  <  MBYTE  )  { 
num  /=  KBYTE  ; 
unit  =  "KB"  ; 
}  else   if  (  length  <  GBYTE  )  { 
num  /=  MBYTE  ; 
unit  =  "MB"  ; 
}  else  { 
num  /=  GBYTE  ; 
unit  =  "GB"  ; 
} 
return   format  .  format  (  num  )  +  unit  ; 
} 









public   static   String   encodeURL  (  Path   path  )  { 
return   encodeURL  (  path  .  toString  (  )  )  ; 
} 









public   static   String   encodeURL  (  String   url  )  { 
try  { 
url  =  URLEncoder  .  encode  (  url  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
return   url  ; 
} 









public   static   String   decodeURL  (  String   url  )  { 
try  { 
url  =  URLDecoder  .  decode  (  url  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
return   url  ; 
} 









public   static   Locale   getLocale  (  String   localeName  )  { 
if  (  !  isNullOrEmpty  (  localeName  )  )  { 
Locale  [  ]  locales  =  Locale  .  getAvailableLocales  (  )  ; 
for  (  int   i  =  0  ;  i  <  locales  .  length  ;  i  ++  )  { 
if  (  localeName  .  equals  (  locales  [  i  ]  .  toString  (  )  )  )  { 
return   locales  [  i  ]  ; 
} 
} 
} 
return   null  ; 
} 






public   static   Locale  [  ]  getLanguageLocales  (  )  { 
Locale  [  ]  all  =  Locale  .  getAvailableLocales  (  )  ; 
List   list  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  all  .  length  ;  i  ++  )  { 
if  (  all  [  i  ]  .  toString  (  )  .  length  (  )  ==  2  )  { 
list  .  add  (  all  [  i  ]  )  ; 
} 
} 
return  (  Locale  [  ]  )  list  .  toArray  (  new   Locale  [  list  .  size  (  )  ]  )  ; 
} 

public   static   String   toTitleCase  (  String   s  )  { 
char  [  ]  chars  =  s  .  trim  (  )  .  toLowerCase  (  )  .  toCharArray  (  )  ; 
boolean   found  =  false  ; 
for  (  int   i  =  0  ;  i  <  chars  .  length  ;  i  ++  )  { 
if  (  !  found  &&  Character  .  isLetter  (  chars  [  i  ]  )  )  { 
chars  [  i  ]  =  Character  .  toUpperCase  (  chars  [  i  ]  )  ; 
found  =  true  ; 
}  else   if  (  Character  .  isWhitespace  (  chars  [  i  ]  )  )  { 
found  =  false  ; 
} 
} 
return   String  .  valueOf  (  chars  )  ; 
} 

public   static   String  [  ]  commonPart  (  String  [  ]  sa1  ,  String  [  ]  sa2  ,  boolean   fromEnd  )  { 
int   len1  =  sa1  .  length  ; 
int   len2  =  sa2  .  length  ; 
int   cnt  =  Math  .  min  (  len1  ,  len2  )  ; 
if  (  fromEnd  )  { 
for  (  int   i  =  1  ;  i  <=  cnt  ;  i  ++  )  { 
if  (  !  sa1  [  len1  -  i  ]  .  equals  (  sa2  [  len2  -  i  ]  )  )  { 
cnt  =  i  -  1  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  cnt  ;  i  ++  )  { 
if  (  !  sa1  [  i  ]  .  equals  (  sa2  [  i  ]  )  )  { 
cnt  =  i  ; 
} 
} 
} 
String  [  ]  result  =  new   String  [  cnt  ]  ; 
System  .  arraycopy  (  sa1  ,  fromEnd  ?  len1  -  cnt  :  0  ,  result  ,  0  ,  cnt  )  ; 
return   result  ; 
} 
} 

