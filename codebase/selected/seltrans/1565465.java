package   com  .  elitost  .  utils  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  LineNumberReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  nio  .  MappedByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashSet  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  NoSuchElementException  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 












public   final   class   FileUtils  { 

private   static   final   char   altSeparatorChar  =  File  .  separatorChar  ==  '/'  ?  '\\'  :  '/'  ; 

private   FileUtils  (  )  { 
} 







public   static   final   String   getFullPath  (  File   file  )  { 
try  { 
return   file  .  getCanonicalPath  (  )  ; 
}  catch  (  IOException   ex  )  { 
} 
return   file  .  getAbsolutePath  (  )  ; 
} 







public   static   final   String   getFullPath  (  String   path  )  { 
return   getFullPath  (  new   File  (  path  )  )  ; 
} 
















public   static   String   relPath  (  String   dir  ,  String   path  )  { 
String   fullpath  =  getFullPath  (  path  )  ; 
String   fulldir  =  getFullPath  (  dir  )  ; 
if  (  !  fullpath  .  startsWith  (  fulldir  +  File  .  separatorChar  )  )  { 
return   path  ; 
} 
String   result  =  fullpath  .  substring  (  fulldir  .  length  (  )  +  1  )  ; 
if  (  dir  .  indexOf  (  File  .  separatorChar  )  <  0  &&  path  .  indexOf  (  File  .  separatorChar  )  <  0  &&  (  dir  .  indexOf  (  altSeparatorChar  )  >=  0  ||  path  .  indexOf  (  altSeparatorChar  )  >=  0  )  )  { 
return   result  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   result  ; 
} 

















public   static   String   path  (  String   currentDir  ,  String   filepath  )  { 
return  (  filepath  .  charAt  (  0  )  ==  File  .  separatorChar  ||  filepath  .  charAt  (  0  )  ==  altSeparatorChar  ||  filepath  .  indexOf  (  ':'  )  >  0  ||  Utils  .  isEmpty  (  currentDir  )  )  ?  filepath  :  (  currentDir  +  (  currentDir  .  endsWith  (  File  .  separator  )  ?  ""  :  File  .  separator  )  +  filepath  )  ; 
} 















public   static   String  [  ]  splitPath  (  String   path  )  { 
return   new   String  [  ]  {  dirname  (  path  )  ,  new   File  (  path  )  .  getName  (  )  }  ; 
} 















public   static   String   dirname  (  File   file  )  { 
String   parent  =  file  .  getParent  (  )  ; 
if  (  parent  ==  null  )  parent  =  "."  ; 
if  (  file  .  getPath  (  )  .  indexOf  (  File  .  separatorChar  )  <  0  &&  file  .  getPath  (  )  .  indexOf  (  altSeparatorChar  )  >=  0  &&  parent  .  indexOf  (  File  .  separatorChar  )  >=  0  )  { 
parent  =  parent  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   parent  ; 
} 














public   static   String   dirname  (  String   path  )  { 
String   dirname  =  dirname  (  new   File  (  path  )  )  ; 
if  (  path  .  indexOf  (  altSeparatorChar  )  >=  0  &&  path  .  indexOf  (  File  .  separatorChar  )  <  0  )  { 
return   dirname  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   dirname  ; 
} 











public   static   String   filename  (  String   path  )  { 
return   new   File  (  path  )  .  getName  (  )  ; 
} 













public   static   List  <  String  >  find  (  File   subdir  ,  Pattern   pattern  )  { 
List  <  String  >  resultSet  =  new   ArrayList  <  String  >  (  )  ; 
File   contents  [  ]  =  subdir  .  listFiles  (  )  ; 
for  (  File   file  :  contents  )  { 
String   path  =  getFullPath  (  file  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
resultSet  .  addAll  (  find  (  file  ,  pattern  )  )  ; 
} 
if  (  pattern  .  matcher  (  path  )  .  find  (  )  )  { 
resultSet  .  add  (  path  )  ; 
}  else  { 
path  =  path  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
if  (  pattern  .  matcher  (  path  )  .  find  (  )  )  { 
resultSet  .  add  (  path  )  ; 
} 
} 
} 
return   resultSet  ; 
} 












public   static   List  <  String  >  find  (  String   subdir  ,  Pattern   pattern  )  { 
return   find  (  new   File  (  subdir  )  ,  pattern  )  ; 
} 












public   static   List  <  String  >  find  (  String   subdir  ,  String   pattern  )  { 
try  { 
return   find  (  subdir  ,  Pattern  .  compile  (  pattern  ,  Pattern  .  CASE_INSENSITIVE  )  )  ; 
}  catch  (  Exception   e  )  { 
return   new   ArrayList  <  String  >  (  )  ; 
} 
} 

public   static   final   int   FIND_FILE  =  1  ; 

public   static   final   int   FIND_DIRECTORY  =  2  ; 

public   static   final   int   FIND_ALL  =  3  ; 










public   static   String   findLatest  (  String   subdir  ,  String   pattern  ,  int   whatExactly  )  { 
String   currentFile  =  null  ; 
long   currentTime  =  0  ; 
for  (  String   path  :  find  (  subdir  ,  pattern  )  )  { 
File   candidate  =  new   File  (  path  )  ; 
boolean   isGood  =  (  (  candidate  .  isDirectory  (  )  ?  FIND_DIRECTORY  :  candidate  .  isFile  (  )  ?  FIND_FILE  :  0  )  &  whatExactly  )  !=  0  ; 
if  (  currentTime  <  candidate  .  lastModified  (  )  &&  isGood  )  { 
try  { 
currentTime  =  candidate  .  lastModified  (  )  ; 
currentFile  =  candidate  .  getCanonicalPath  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
return   currentFile  ; 
} 








public   static   String   findLatest  (  String   subdir  ,  String   pattern  )  { 
return   findLatest  (  subdir  ,  pattern  ,  FIND_ALL  )  ; 
} 








public   static   String   findLatestFile  (  String   subdir  ,  String   pattern  )  { 
return   findLatest  (  subdir  ,  pattern  ,  FIND_FILE  )  ; 
} 








public   static   String   findLatestDirectory  (  String   subdir  ,  String   pattern  )  { 
return   findLatest  (  subdir  ,  pattern  ,  FIND_DIRECTORY  )  ; 
} 




public   static   FileFilter   DIRECTORY_FILTER  =  new   FileFilter  (  )  { 

public   boolean   accept  (  File   file  )  { 
return   file  .  isDirectory  (  )  ; 
} 
}  ; 




public   static   FileFilter   fileFilter  =  new   FileFilter  (  )  { 

public   boolean   accept  (  File   file  )  { 
return   file  .  isFile  (  )  ; 
} 
}  ; 







public   static   final   File  [  ]  listSubdirectories  (  File   dir  )  { 
return   dir  .  isDirectory  (  )  ?  dir  .  listFiles  (  DIRECTORY_FILTER  )  :  null  ; 
} 







public   static   final   File  [  ]  listFiles  (  File   dir  )  { 
return   dir  .  isDirectory  (  )  ?  dir  .  listFiles  (  fileFilter  )  :  null  ; 
} 











public   static   final   String   lastModified  (  File   file  )  { 
return  (  new   Date  (  file  .  lastModified  (  )  )  )  .  toString  (  )  ; 
} 






public   static   String   getcwd  (  )  { 
File   here  =  new   File  (  "."  )  ; 
try  { 
return   here  .  getCanonicalPath  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
; 
return   here  .  getAbsolutePath  (  )  ; 
} 












public   static   boolean   deleteFile  (  String   filename  )  { 
return   deleteFile  (  new   File  (  filename  )  )  ; 
} 








public   static   boolean   deleteFile  (  File   file  )  { 
try  { 
if  (  file  .  isDirectory  (  )  )  { 
String   fullpath  =  file  .  getCanonicalPath  (  )  ; 
for  (  String   filename  :  file  .  list  (  )  )  { 
deleteFile  (  new   File  (  fullpath  ,  filename  )  )  ; 
} 
} 
return  !  file  .  exists  (  )  ||  file  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   false  ; 
} 













public   static   FileOutputStream   makeFile  (  String   dirname  ,  String   filename  ,  boolean   append  )  throws   IOException  { 
if  (  Utils  .  isEmpty  (  dirname  )  )  { 
return   new   FileOutputStream  (  new   File  (  filename  )  ,  append  )  ; 
}  else  { 
File   dir  =  new   File  (  dirname  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
if  (  dir  .  exists  (  )  )  dir  .  delete  (  )  ; 
dir  .  mkdirs  (  )  ; 
} 
return   new   FileOutputStream  (  new   File  (  dirname  ,  filename  )  ,  append  )  ; 
} 
} 










public   static   FileOutputStream   makeFile  (  String   dir  ,  String   filename  )  throws   IOException  { 
return   makeFile  (  dir  ,  filename  ,  false  )  ; 
} 












public   static   FileOutputStream   makeFile  (  String  [  ]  path  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  path  [  0  ]  ,  path  [  1  ]  ,  append  )  ; 
} 









public   static   FileOutputStream   makeFile  (  String  ...  path  )  throws   IOException  { 
return   path  .  length  <  1  ?  null  :  path  .  length  <  2  ?  makeFile  (  path  [  0  ]  )  :  path  .  length  <  3  ?  makeFile  (  path  [  0  ]  ,  path  [  1  ]  )  :  makeFile  (  StringUtils  .  join  (  File  .  separator  ,  path  )  )  ; 
} 












public   static   FileOutputStream   makeFile  (  String   path  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  ,  append  )  ; 
} 









public   static   FileOutputStream   makeFile  (  String   path  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  )  ; 
} 












public   static   FileOutputStream   makeFile  (  File   file  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  ,  append  )  ; 
} 









public   static   FileOutputStream   makeFile  (  File   file  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  )  ; 
} 










public   static   final   OutputStreamWriter   makeFileWriter  (  String   path  ,  String   encoding  )  throws   IOException  { 
return   new   OutputStreamWriter  (  makeFile  (  path  )  ,  encoding  )  ; 
} 









public   static   final   int   adjustSizeByMooreLaw  (  int   size  ,  int   thisYear  )  { 
double   milli  =  System  .  currentTimeMillis  (  )  ; 
double   aYear  =  (  double  )  1000  *  60  *  60  *  24  *  (  365  *  4  +  1  )  /  4  ; 
double   q  =  Math  .  exp  (  (  milli  /  aYear  +  1970  -  thisYear  )  /  3  *  Math  .  log  (  2  )  )  ; 
return  (  int  )  (  size  *  q  )  ; 
} 







private   static   final   int   MAX_BUFFER_SIZE  =  FileUtils  .  adjustSizeByMooreLaw  (  65536  ,  2004  )  ; 

public   static   final   String   readString  (  Reader   reader  )  { 
try  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
char  [  ]  chars  =  new   char  [  MAX_BUFFER_SIZE  ]  ; 
int   l  ; 
while  (  (  l  =  reader  .  read  (  chars  )  )  >  0  )  { 
buf  .  append  (  chars  ,  0  ,  l  )  ; 
} 
return   buf  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 












public   static   final   String   readStringFromFile  (  File   file  )  { 
try  { 
return   readString  (  new   FileReader  (  file  )  )  ; 
}  catch  (  Exception   e  )  { 
LogUtils  .  severe  (  null  ,  e  )  ; 
} 
return   null  ; 
} 








public   static   final   String   readStringFromFile  (  File   file  ,  String   encoding  )  { 
try  { 
return   readString  (  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  encoding  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 












public   static   final   String   readStringFromFile  (  String   filename  )  { 
return   readStringFromFile  (  new   File  (  filename  )  )  ; 
} 











public   static   final   byte  [  ]  readBytesFromStream  (  InputStream   is  )  { 
try  { 
ArrayList  <  byte  [  ]  >  chunkList  =  new   ArrayList  <  byte  [  ]  >  (  )  ; 
int   total  =  0  ; 
int   l  ; 
while  (  (  l  =  is  .  available  (  )  )  >  0  )  { 
byte  [  ]  chunk  =  new   byte  [  l  ]  ; 
is  .  read  (  chunk  )  ; 
chunkList  .  add  (  chunk  )  ; 
total  +=  l  ; 
} 
byte  [  ]  buffer  =  new   byte  [  total  ]  ; 
int   pos  =  0  ; 
for  (  byte  [  ]  chunk  :  chunkList  )  { 
java  .  lang  .  System  .  arraycopy  (  chunk  ,  0  ,  buffer  ,  pos  ,  chunk  .  length  )  ; 
} 
return   buffer  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 











public   static   final   byte  [  ]  readBytesFromFile  (  String   filename  )  { 
try  { 
File   file  =  new   File  (  filename  )  ; 
long   fullsize  =  file  .  length  (  )  ; 
if  (  fullsize  >  Integer  .  MAX_VALUE  )  { 
throw   new   IOException  (  "File too large"  )  ; 
} 
FileChannel   channel  =  new   FileInputStream  (  file  )  .  getChannel  (  )  ; 
MappedByteBuffer   buffer  =  channel  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  file  .  length  (  )  )  ; 
byte  [  ]  result  =  new   byte  [  (  int  )  fullsize  ]  ; 
buffer  .  get  (  result  )  ; 
return   result  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  CharSequence   data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  )  )  ; 
StringUtils  .  write  (  sw  ,  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  char  [  ]  data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  )  )  ; 
sw  .  write  (  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  byte  [  ]  data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStream   os  =  makeFile  (  file  )  ; 
os  .  write  (  data  )  ; 
os  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 







public   static   final   File   writeBytesToFile  (  byte  [  ]  data  ,  String   fileTo  )  { 
return   writeToFile  (  data  ,  fileTo  )  ; 
} 









public   static   final   File   writeToFile  (  InputStream   is  ,  String   fileTo  )  throws   IOException  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStream   os  =  makeFile  (  file  )  ; 
pipe  (  is  ,  os  ,  false  )  ; 
os  .  close  (  )  ; 
return   file  ; 
} 








public   static   final   File   appendToFile  (  CharSequence   data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  ,  true  )  )  ; 
StringUtils  .  write  (  sw  ,  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendToFile  (  char  [  ]  data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  ,  true  )  )  ; 
sw  .  write  (  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendToFile  (  byte  [  ]  data  ,  String   fileTo  )  { 
try  { 
File   file  =  new   File  (  fileTo  )  ; 
OutputStream   os  =  makeFile  (  file  ,  true  )  ; 
os  .  write  (  data  )  ; 
os  .  close  (  )  ; 
return   file  ; 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendBytesToFile  (  char  [  ]  data  ,  String   fileTo  )  { 
return   appendBytesToFile  (  Utils  .  toBytes  (  (  char  [  ]  )  data  )  ,  fileTo  )  ; 
} 






public   static   final   File   appendBytesToFile  (  byte  [  ]  data  ,  String   fileTo  )  { 
return   appendToFile  (  data  ,  fileTo  )  ; 
} 














public   static   final   String   getPackageName  (  String   basePath  ,  String   currentPath  )  { 
String   path  =  relPath  (  basePath  ,  currentPath  )  ; 
return   path  ==  null  ?  null  :  path  .  equals  (  currentPath  )  ?  null  :  path  .  replace  (  File  .  separatorChar  ,  '.'  )  ; 
} 









public   interface   ByteFilter  { 








byte  [  ]  filter  (  byte  [  ]  input  ,  int   length  )  ; 
} 








public   interface   BufferingFilter   extends   ByteFilter  { 







byte  [  ]  getBuffer  (  )  ; 




void   clear  (  )  ; 
} 














public   static   void   pipe  (  InputStream   in  ,  OutputStream   out  ,  boolean   isBlocking  ,  ByteFilter   filter  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  MAX_BUFFER_SIZE  ]  ; 
int   nread  ; 
int   navailable  ; 
int   total  =  0  ; 
synchronized  (  in  )  { 
while  (  (  navailable  =  isBlocking  ?  buf  .  length  :  in  .  available  (  )  )  >  0  &&  (  nread  =  in  .  read  (  buf  ,  0  ,  Math  .  min  (  buf  .  length  ,  navailable  )  )  )  >=  0  )  { 
if  (  filter  ==  null  )  { 
out  .  write  (  buf  ,  0  ,  nread  )  ; 
}  else  { 
byte  [  ]  filtered  =  filter  .  filter  (  buf  ,  nread  )  ; 
out  .  write  (  filtered  )  ; 
} 
total  +=  nread  ; 
} 
} 
out  .  flush  (  )  ; 
buf  =  null  ; 
} 












public   static   void   pipe  (  InputStream   in  ,  OutputStream   out  ,  boolean   isBlocking  )  throws   IOException  { 
pipe  (  in  ,  out  ,  isBlocking  ,  null  )  ; 
} 










public   static   boolean   pipe  (  Reader   in  ,  Writer   out  )  { 
if  (  in  ==  null  )  { 
return   false  ; 
} 
if  (  out  ==  null  )  { 
return   false  ; 
} 
try  { 
int   c  ; 
synchronized  (  in  )  { 
while  (  in  .  ready  (  )  &&  (  c  =  in  .  read  (  )  )  >  0  )  { 
out  .  write  (  c  )  ; 
} 
} 
out  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 












public   static   boolean   copy  (  String   from  ,  String   to  ,  String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 












public   static   boolean   copy  (  File   from  ,  File   to  ,  String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 











public   static   boolean   copy  (  String   from  ,  String   to  )  { 
return   copy  (  new   File  (  from  )  ,  new   File  (  to  )  )  ; 
} 











public   static   boolean   USE_NIO  =  true  ; 

public   static   boolean   copy  (  File   from  ,  File   to  )  { 
if  (  from  .  isDirectory  (  )  )  { 
for  (  String   name  :  Arrays  .  asList  (  from  .  list  (  )  )  )  { 
if  (  !  copy  (  from  ,  to  ,  name  )  )  { 
LogUtils  .  info  (  "Failed to copy "  +  name  +  " from "  +  from  +  " to "  +  to  ,  null  )  ; 
return   false  ; 
} 
} 
}  else  { 
try  { 
FileInputStream   is  =  new   FileInputStream  (  from  )  ; 
FileChannel   ifc  =  is  .  getChannel  (  )  ; 
FileOutputStream   os  =  makeFile  (  to  )  ; 
if  (  USE_NIO  )  { 
FileChannel   ofc  =  os  .  getChannel  (  )  ; 
ofc  .  transferFrom  (  ifc  ,  0  ,  from  .  length  (  )  )  ; 
}  else  { 
pipe  (  is  ,  os  ,  false  )  ; 
} 
is  .  close  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
LogUtils  .  warning  (  "Failed to copy "  +  from  +  " to "  +  to  ,  ex  )  ; 
return   false  ; 
} 
} 
long   time  =  from  .  lastModified  (  )  ; 
setLastModified  (  to  ,  time  )  ; 
long   newtime  =  to  .  lastModified  (  )  ; 
if  (  newtime  !=  time  )  { 
LogUtils  .  info  (  "Failed to set timestamp for file "  +  to  +  ": tried "  +  new   Date  (  time  )  +  ", have "  +  new   Date  (  newtime  )  ,  null  )  ; 
to  .  setLastModified  (  time  )  ; 
long   morenewtime  =  to  .  lastModified  (  )  ; 
return   false  ; 
} 
return   time  ==  newtime  ; 
} 



















public   static   boolean   setLastModified  (  File   file  ,  long   time  )  { 
if  (  file  .  setLastModified  (  time  )  )  { 
return   true  ; 
} 
System  .  gc  (  )  ; 
return   file  .  setLastModified  (  time  )  ; 
} 








public   static   boolean   equal  (  File   left  ,  File   right  )  { 
if  (  left  .  isDirectory  (  )  &&  right  .  isDirectory  (  )  )  { 
Set  <  String  >  leftSet  =  new   HashSet  <  String  >  (  Arrays  .  asList  (  left  .  list  (  )  )  )  ; 
Set  <  String  >  rightSet  =  new   HashSet  <  String  >  (  Arrays  .  asList  (  right  .  list  (  )  )  )  ; 
if  (  leftSet  .  size  (  )  !=  rightSet  .  size  (  )  )  { 
LogUtils  .  info  (  left  .  getPath  (  )  +  " has "  +  leftSet  .  size  (  )  +  " while "  +  right  .  getPath  (  )  +  " has "  +  rightSet  .  size  (  )  ,  null  )  ; 
return   false  ; 
} 
for  (  String   name  :  leftSet  )  { 
if  (  rightSet  .  contains  (  name  )  )  { 
if  (  !  equal  (  new   File  (  left  ,  name  )  ,  new   File  (  right  ,  name  )  )  )  { 
LogUtils  .  info  (  left  .  getPath  (  )  +  File  .  separator  +  name  +  " is different from "  +  right  .  getPath  (  )  +  File  .  separator  +  name  ,  null  )  ; 
return   false  ; 
} 
}  else  { 
LogUtils  .  info  (  right  .  getPath  (  )  +  " does not contain "  +  name  ,  null  )  ; 
return   false  ; 
} 
} 
return   true  ; 
}  else   if  (  left  .  isFile  (  )  &&  right  .  isFile  (  )  )  { 
try  { 
return   compare  (  left  ,  right  )  ==  0  ; 
}  catch  (  IOException   e  )  { 
LogUtils  .  severe  (  e  .  getMessage  (  )  +  " while comparing "  +  left  .  getPath  (  )  +  " and "  +  right  .  getPath  (  )  ,  e  )  ; 
return   false  ; 
} 
}  else  { 
return   false  ; 
} 
} 















public   static   int   compare  (  File   left  ,  File   right  )  throws   IOException  { 
long   lm  =  left  .  lastModified  (  )  /  1000  ; 
long   rm  =  right  .  lastModified  (  )  /  1000  ; 
if  (  lm  <  rm  )  return  -  1  ; 
if  (  lm  >  rm  )  return   1  ; 
long   ll  =  left  .  length  (  )  ; 
long   rl  =  right  .  length  (  )  ; 
if  (  ll  <  rl  )  return  -  1  ; 
if  (  ll  >  rl  )  return   1  ; 
InputStream   is1  =  new   BufferedInputStream  (  new   FileInputStream  (  left  )  )  ; 
InputStream   is2  =  new   BufferedInputStream  (  new   FileInputStream  (  right  )  )  ; 
for  (  long   i  =  0  ;  i  <  ll  ;  i  ++  )  { 
int   b1  =  is1  .  read  (  )  ; 
int   b2  =  is2  .  read  (  )  ; 
if  (  b1  <  0  )  return  -  1  ; 
if  (  b2  <  0  )  return   1  ; 
if  (  b1  !=  b2  )  return   b1  <  b2  ?  -  1  :  1  ; 
} 
return   0  ; 
} 














public   static   boolean   synchronize  (  File   left  ,  File   right  ,  String   what  )  { 
return   synchronize  (  new   File  (  left  ,  what  )  ,  new   File  (  right  ,  what  )  )  ; 
} 













public   static   boolean   synchronize  (  File   left  ,  File   right  )  { 
if  (  left  .  isDirectory  (  )  ||  right  .  isDirectory  (  )  )  { 
String  [  ]  leftContents  =  left  .  list  (  )  ; 
Set  <  String  >  contents  =  leftContents  ==  null  ?  new   LinkedHashSet  <  String  >  (  )  :  new   LinkedHashSet  <  String  >  (  Arrays  .  asList  (  leftContents  )  )  ; 
String  [  ]  rightContents  =  right  .  list  (  )  ; 
if  (  rightContents  !=  null  )  { 
contents  .  addAll  (  Arrays  .  asList  (  rightContents  )  )  ; 
} 
for  (  String   name  :  contents  )  { 
if  (  !  synchronize  (  left  ,  right  ,  name  )  )  return   false  ; 
} 
}  else  { 
long   leftTime  =  left  .  lastModified  (  )  ; 
long   rightTime  =  right  .  lastModified  (  )  ; 
if  (  left  .  exists  (  )  &&  (  !  right  .  exists  (  )  ||  leftTime  <  rightTime  )  )  { 
return   copy  (  left  ,  right  )  ; 
}  else   if  (  right  .  exists  (  )  &&  (  !  left  .  exists  (  )  ||  leftTime  >  rightTime  )  )  { 
return   copy  (  right  ,  left  )  ; 
} 
} 
return   true  ; 
} 














public   static   boolean   unzip  (  ZipInputStream   zis  ,  File   location  )  throws   IOException  { 
if  (  !  location  .  exists  (  )  )  { 
location  .  mkdirs  (  )  ; 
} 
ZipEntry   ze  ; 
while  (  (  ze  =  zis  .  getNextEntry  (  )  )  !=  null  )  { 
File   output  =  new   File  (  location  ,  ze  .  getName  (  )  )  ; 
if  (  ze  .  isDirectory  (  )  )  { 
output  .  mkdirs  (  )  ; 
}  else  { 
File   dir  =  output  .  getParentFile  (  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  dir  .  delete  (  )  ; 
dir  .  mkdirs  (  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
System  .  err  .  println  (  "Could not create directory "  +  dir  .  getCanonicalPath  (  )  )  ; 
return   false  ; 
} 
OutputStream   os  =  new   FileOutputStream  (  output  )  ; 
pipe  (  zis  ,  os  ,  true  )  ; 
os  .  close  (  )  ; 
} 
} 
zis  .  close  (  )  ; 
return   true  ; 
} 













public   static   boolean   install  (  Class   clazz  ,  String   resourceArchiveName  ,  File   location  )  throws   IOException  { 
ZipInputStream   zis  =  new   ZipInputStream  (  clazz  .  getResourceAsStream  (  resourceArchiveName  )  )  ; 
return   unzip  (  zis  ,  location  )  ; 
} 













public   static   boolean   install  (  Class   clazz  ,  String   resourceArchiveName  ,  String   folderName  )  throws   IOException  { 
return   install  (  clazz  ,  resourceArchiveName  ,  new   File  (  folderName  )  )  ; 
} 

private   static   class   ByteIterator   implements   Iterator  <  Byte  >  { 

IOException   exception  =  null  ; 

byte   next  ; 

boolean   have  =  false  ; 

InputStream   is  =  null  ; 

private   ByteIterator  (  InputStream   is  )  { 
this  .  is  =  is  ; 
} 

public   boolean   hasNext  (  )  { 
if  (  have  )  { 
return   true  ; 
}  else   if  (  is  ==  null  )  { 
return   false  ; 
}  else  { 
try  { 
int   input  =  is  .  read  (  )  ; 
if  (  input  <  0  )  { 
close  (  )  ; 
}  else  { 
have  =  true  ; 
next  =  (  byte  )  input  ; 
} 
}  catch  (  IOException   ex  )  { 
exception  =  ex  ; 
close  (  )  ; 
} 
} 
return   is  !=  null  ; 
} 

public   Byte   next  (  )  { 
if  (  !  hasNext  (  )  )  { 
throw   exception  ==  null  ?  new   NoSuchElementException  (  )  :  new   NoSuchElementException  (  exception  .  getMessage  (  )  )  ; 
} 
have  =  false  ; 
return   next  ; 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

private   void   close  (  )  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
is  =  null  ; 
} 

protected   void   finalize  (  )  { 
close  (  )  ; 
} 
} 


















public   static   Iterable  <  Byte  >  bytes  (  final   InputStream   is  )  { 
return   new   Iterable  <  Byte  >  (  )  { 

private   final   Iterator  <  Byte  >  iterator  =  new   ByteIterator  (  is  )  ; 

public   Iterator  <  Byte  >  iterator  (  )  { 
return   iterator  ; 
} 
}  ; 
} 















public   static   Iterable  <  Byte  >  bytes  (  final   File   file  )  { 
return   new   Iterable  <  Byte  >  (  )  { 

public   Iterator  <  Byte  >  iterator  (  )  { 
try  { 
return   new   ByteIterator  (  new   FileInputStream  (  file  )  )  ; 
}  catch  (  IOException   e  )  { 
return   new   EmptyIterator  <  Byte  >  (  e  .  getMessage  (  )  )  ; 
} 
} 
}  ; 
} 

private   static   class   CharIterator   implements   Iterator  <  Character  >  { 

IOException   exception  =  null  ; 

Reader   reader  ; 

char   next  ; 

boolean   have  =  false  ; 

private   CharIterator  (  Reader   reader  )  { 
this  .  reader  =  reader  ; 
} 

public   boolean   hasNext  (  )  { 
if  (  have  )  { 
return   true  ; 
}  else   if  (  reader  ==  null  )  { 
return   false  ; 
}  else  { 
try  { 
int   input  =  reader  .  read  (  )  ; 
if  (  input  <  0  )  { 
close  (  )  ; 
}  else  { 
have  =  true  ; 
next  =  (  char  )  input  ; 
return   true  ; 
} 
}  catch  (  IOException   ex  )  { 
exception  =  ex  ; 
close  (  )  ; 
} 
return   reader  !=  null  ; 
} 
} 

public   Character   next  (  )  { 
if  (  !  hasNext  (  )  )  { 
throw   exception  ==  null  ?  new   NoSuchElementException  (  )  :  new   NoSuchElementException  (  exception  .  getMessage  (  )  )  ; 
} 
have  =  false  ; 
return   next  ; 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

private   void   close  (  )  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
reader  =  null  ; 
} 

protected   void   finalize  (  )  { 
close  (  )  ; 
} 
} 

















public   static   Iterable  <  Character  >  chars  (  final   Reader   reader  )  { 
return   new   Iterable  <  Character  >  (  )  { 

private   final   Iterator  <  Character  >  iterator  =  new   CharIterator  (  reader  )  ; 

public   Iterator  <  Character  >  iterator  (  )  { 
return   iterator  ; 
} 
}  ; 
} 














public   static   Iterable  <  Character  >  chars  (  final   File   file  )  { 
return   new   Iterable  <  Character  >  (  )  { 

public   Iterator  <  Character  >  iterator  (  )  { 
try  { 
return   new   CharIterator  (  new   FileReader  (  file  )  )  ; 
}  catch  (  IOException   e  )  { 
return   new   EmptyIterator  <  Character  >  (  e  .  getMessage  (  )  )  ; 
} 
} 
}  ; 
} 

private   static   class   LineIterator   implements   Iterator  <  String  >  { 

LineNumberReader   lr  ; 

IOException   exception  =  null  ; 

String   next  ; 

boolean   have  =  false  ; 

private   LineIterator  (  Reader   reader  )  { 
lr  =  new   LineNumberReader  (  reader  )  ; 
} 

public   boolean   hasNext  (  )  { 
if  (  have  )  { 
return   true  ; 
}  else   if  (  lr  ==  null  )  { 
return   false  ; 
}  else  { 
try  { 
next  =  lr  .  readLine  (  )  ; 
if  (  next  ==  null  )  { 
close  (  )  ; 
}  else  { 
have  =  true  ; 
return   true  ; 
} 
}  catch  (  IOException   ex  )  { 
exception  =  ex  ; 
close  (  )  ; 
} 
return   false  ; 
} 
} 

public   String   next  (  )  { 
if  (  !  hasNext  (  )  )  { 
throw   exception  ==  null  ?  new   NoSuchElementException  (  )  :  new   NoSuchElementException  (  exception  .  getMessage  (  )  )  ; 
} 
have  =  false  ; 
return   next  ; 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

private   void   close  (  )  { 
if  (  lr  !=  null  )  { 
try  { 
lr  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
lr  =  null  ; 
} 

protected   void   finalize  (  )  { 
close  (  )  ; 
} 
} 

















public   static   Iterable  <  String  >  lines  (  final   Reader   reader  )  { 
return   new   Iterable  <  String  >  (  )  { 

private   final   Iterator  <  String  >  iterator  =  new   LineIterator  (  reader  )  ; 

public   Iterator  <  String  >  iterator  (  )  { 
return   iterator  ; 
} 
}  ; 
} 











public   static   Iterable  <  String  >  lines  (  final   File   file  )  { 
return   new   Iterable  <  String  >  (  )  { 

public   Iterator  <  String  >  iterator  (  )  { 
try  { 
return   new   LineIterator  (  new   FileReader  (  file  )  )  ; 
}  catch  (  IOException   e  )  { 
return   new   EmptyIterator  <  String  >  (  e  .  getMessage  (  )  )  ; 
} 
} 
}  ; 
} 




















public   static   Iterable  <  File  >  tree  (  final   File   folder  )  { 
return   new   Iterable  <  File  >  (  )  { 

public   Iterator  <  File  >  iterator  (  )  { 
return   FolderIterator  .  preorder  (  folder  )  ; 
} 
}  ; 
} 




















public   static   Iterable  <  File  >  tree  (  final   File   folder  ,  final   FileFilter   filter  )  { 
return   new   Iterable  <  File  >  (  )  { 

public   Iterator  <  File  >  iterator  (  )  { 
return   FolderIterator  .  preorder  (  folder  ,  filter  )  ; 
} 
}  ; 
} 




















public   static   Iterable  <  File  >  treePostorder  (  final   File   folder  )  { 
return   new   Iterable  <  File  >  (  )  { 

public   Iterator  <  File  >  iterator  (  )  { 
return   FolderIterator  .  postorder  (  folder  )  ; 
} 
}  ; 
} 














public   static   Iterable  <  File  >  files  (  final   File   folder  )  { 
return   Arrays  .  asList  (  listFiles  (  folder  )  )  ; 
} 
} 














final   class   FolderIterator   implements   Iterator  <  File  >  { 

private   File   self  ; 

private   final   Iterator  <  File  >  outerIterator  ; 

private   Iterator  <  File  >  current  =  null  ; 

private   FileFilter   filter  ; 

private   final   boolean   preorder  ; 

private   FolderIterator  (  final   File   folder  ,  boolean   preorder  )  { 
this  (  folder  ,  FileUtils  .  DIRECTORY_FILTER  ,  preorder  )  ; 
} 

private   FolderIterator  (  final   File   folder  ,  FileFilter   filter  ,  boolean   preorder  )  { 
this  .  self  =  folder  ; 
this  .  filter  =  filter  ; 
this  .  preorder  =  preorder  ; 
this  .  outerIterator  =  Arrays  .  asList  (  folder  .  listFiles  (  filter  )  )  .  iterator  (  )  ; 
} 

private   static   FileFilter   makeDirectoryFilter  (  final   FileFilter   filter  )  { 
return   new   FileFilter  (  )  { 

public   boolean   accept  (  File   file  )  { 
return   file  .  isDirectory  (  )  &&  filter  .  accept  (  file  )  ; 
} 
}  ; 
} 








public   static   FolderIterator   preorder  (  File   folder  )  { 
return   new   FolderIterator  (  folder  ,  true  )  ; 
} 









public   static   FolderIterator   preorder  (  File   folder  ,  FileFilter   filter  )  { 
return   new   FolderIterator  (  folder  ,  makeDirectoryFilter  (  filter  )  ,  true  )  ; 
} 








public   static   FolderIterator   postorder  (  File   folder  )  { 
return   new   FolderIterator  (  folder  ,  false  )  ; 
} 









public   static   FolderIterator   postorder  (  File   folder  ,  FileFilter   filter  )  { 
return   new   FolderIterator  (  folder  ,  makeDirectoryFilter  (  filter  )  ,  false  )  ; 
} 






public   boolean   hasNext  (  )  { 
if  (  preorder  &&  self  !=  null  )  { 
return   true  ; 
} 
return   haveSubtree  (  )  ||  self  !=  null  ; 
} 

private   boolean   haveSubtree  (  )  { 
while  (  current  ==  null  ||  !  current  .  hasNext  (  )  )  { 
if  (  outerIterator  .  hasNext  (  )  )  { 
current  =  new   FolderIterator  (  outerIterator  .  next  (  )  ,  filter  ,  preorder  )  ; 
}  else  { 
return   false  ; 
} 
} 
return   true  ; 
} 







public   File   next  (  )  { 
if  (  preorder  &&  self  !=  null  )  { 
return   self  (  )  ; 
}  else   if  (  haveSubtree  (  )  )  { 
return   current  .  next  (  )  ; 
}  else   if  (  !  preorder  )  { 
return   self  (  )  ; 
} 
throw   new   NoSuchElementException  (  )  ; 
} 

private   File   self  (  )  { 
File   result  =  self  ; 
self  =  null  ; 
return   result  ; 
} 





public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 







class   EmptyIterator  <  T  >  implements   Iterator  <  T  >  { 

NoSuchElementException   exception  ; 

public   EmptyIterator  (  String   message  )  { 
exception  =  new   NoSuchElementException  (  message  )  ; 
} 

public   EmptyIterator  (  )  { 
exception  =  new   NoSuchElementException  (  )  ; 
} 

public   boolean   hasNext  (  )  { 
return   false  ; 
} 

public   T   next  (  )  { 
throw   exception  ; 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

