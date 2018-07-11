package   com  .  myjavatools  .  lib  ; 

import   static   com  .  myjavatools  .  lib  .  Bytes  .  toBytes  ; 
import   static   com  .  myjavatools  .  lib  .  Strings  .  join  ; 
import   static   com  .  myjavatools  .  lib  .  Strings  .  write  ; 
import   static   com  .  myjavatools  .  lib  .  foundation  .  Objects  .  isEmpty  ; 
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
import   com  .  myjavatools  .  lib  .  foundation  .  Iterators  .  EmptyIterator  ; 







public   abstract   class   Files  { 

private   static   final   boolean   DEBUG  =  true  ; 

private   static   final   char   altSeparatorChar  =  File  .  separatorChar  ==  '/'  ?  '\\'  :  '/'  ; 






public   static   final   String   getFullPath  (  final   File   file  )  { 
try  { 
return   file  .  getCanonicalPath  (  )  ; 
}  catch  (  final   IOException   ex  )  { 
} 
return   file  .  getAbsolutePath  (  )  ; 
} 






public   static   final   String   getFullPath  (  final   String   path  )  { 
return   getFullPath  (  new   File  (  path  )  )  ; 
} 













public   static   String   relPath  (  final   String   dir  ,  final   String   path  )  { 
final   String   fullpath  =  getFullPath  (  path  )  ; 
final   String   fulldir  =  getFullPath  (  dir  )  ; 
if  (  !  fullpath  .  startsWith  (  fulldir  +  File  .  separatorChar  )  )  { 
return   path  ; 
} 
final   String   result  =  fullpath  .  substring  (  fulldir  .  length  (  )  +  1  )  ; 
if  (  (  dir  .  indexOf  (  File  .  separatorChar  )  <  0  )  &&  (  path  .  indexOf  (  File  .  separatorChar  )  <  0  )  &&  (  (  dir  .  indexOf  (  altSeparatorChar  )  >=  0  )  ||  (  path  .  indexOf  (  altSeparatorChar  )  >=  0  )  )  )  { 
return   result  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   result  ; 
} 













public   static   String   path  (  final   String   currentDir  ,  final   String   filepath  )  { 
return  (  (  filepath  .  charAt  (  0  )  ==  File  .  separatorChar  )  ||  (  filepath  .  charAt  (  0  )  ==  altSeparatorChar  )  ||  (  filepath  .  indexOf  (  ':'  )  >  0  )  ||  isEmpty  (  currentDir  )  )  ?  filepath  :  (  currentDir  +  (  currentDir  .  endsWith  (  File  .  separator  )  ?  ""  :  File  .  separator  )  +  filepath  )  ; 
} 












public   static   String  [  ]  splitPath  (  final   String   path  )  { 
return   new   String  [  ]  {  dirname  (  path  )  ,  new   File  (  path  )  .  getName  (  )  }  ; 
} 














public   static   String   dirname  (  final   File   file  )  { 
String   parent  =  file  .  getParent  (  )  ; 
if  (  parent  ==  null  )  { 
parent  =  "."  ; 
} 
if  (  (  file  .  getPath  (  )  .  indexOf  (  File  .  separatorChar  )  <  0  )  &&  (  file  .  getPath  (  )  .  indexOf  (  altSeparatorChar  )  >=  0  )  &&  (  parent  .  indexOf  (  File  .  separatorChar  )  >=  0  )  )  { 
parent  =  parent  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   parent  ; 
} 














public   static   String   dirname  (  final   String   path  )  { 
final   String   dirname  =  dirname  (  new   File  (  path  )  )  ; 
if  (  (  path  .  indexOf  (  altSeparatorChar  )  >=  0  )  &&  (  path  .  indexOf  (  File  .  separatorChar  )  <  0  )  )  { 
return   dirname  .  replace  (  File  .  separatorChar  ,  altSeparatorChar  )  ; 
} 
return   dirname  ; 
} 










public   static   String   filename  (  final   String   path  )  { 
return   new   File  (  path  )  .  getName  (  )  ; 
} 












public   static   List  <  String  >  find  (  final   File   subdir  ,  final   Pattern   pattern  )  { 
final   List  <  String  >  resultSet  =  new   ArrayList  <  String  >  (  )  ; 
final   File   contents  [  ]  =  subdir  .  listFiles  (  )  ; 
for  (  final   File   file  :  contents  )  { 
String   path  =  getFullPath  (  file  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
resultSet  .  addAll  (  find  (  file  ,  pattern  )  )  ; 
}  else   if  (  pattern  .  matcher  (  path  )  .  find  (  )  )  { 
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












public   static   List  <  String  >  find  (  final   String   subdir  ,  final   Pattern   pattern  )  { 
return   find  (  new   File  (  subdir  )  ,  pattern  )  ; 
} 












public   static   List  <  String  >  find  (  final   String   subdir  ,  final   String   pattern  )  { 
try  { 
return   find  (  subdir  ,  Pattern  .  compile  (  pattern  ,  Pattern  .  CASE_INSENSITIVE  )  )  ; 
}  catch  (  final   Exception   e  )  { 
return   new   ArrayList  <  String  >  (  )  ; 
} 
} 

public   static   final   int   FIND_FILE  =  1  ; 

public   static   final   int   FIND_DIRECTORY  =  2  ; 

public   static   final   int   FIND_ALL  =  3  ; 









public   static   String   findLatest  (  final   String   subdir  ,  final   String   pattern  ,  final   int   whatExactly  )  { 
String   currentFile  =  null  ; 
long   currentTime  =  0  ; 
for  (  final   String   path  :  find  (  subdir  ,  pattern  )  )  { 
final   File   candidate  =  new   File  (  path  )  ; 
final   boolean   isGood  =  (  (  candidate  .  isDirectory  (  )  ?  FIND_DIRECTORY  :  candidate  .  isFile  (  )  ?  FIND_FILE  :  0  )  &  whatExactly  )  !=  0  ; 
if  (  (  currentTime  <  candidate  .  lastModified  (  )  )  &&  isGood  )  { 
try  { 
currentTime  =  candidate  .  lastModified  (  )  ; 
currentFile  =  candidate  .  getCanonicalPath  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
} 
return   currentFile  ; 
} 








public   static   String   findLatest  (  final   String   subdir  ,  final   String   pattern  )  { 
return   findLatest  (  subdir  ,  pattern  ,  FIND_ALL  )  ; 
} 








public   static   String   findLatestFile  (  final   String   subdir  ,  final   String   pattern  )  { 
return   findLatest  (  subdir  ,  pattern  ,  FIND_FILE  )  ; 
} 








public   static   String   findLatestDirectory  (  final   String   subdir  ,  final   String   pattern  )  { 
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






public   static   final   File  [  ]  listSubdirectories  (  final   File   dir  )  { 
return   dir  .  isDirectory  (  )  ?  dir  .  listFiles  (  DIRECTORY_FILTER  )  :  null  ; 
} 






public   static   final   File  [  ]  listFiles  (  final   File   dir  )  { 
return   dir  .  isDirectory  (  )  ?  dir  .  listFiles  (  fileFilter  )  :  null  ; 
} 










public   static   final   String   lastModified  (  final   File   file  )  { 
return  (  new   Date  (  file  .  lastModified  (  )  )  )  .  toString  (  )  ; 
} 






public   static   String   getcwd  (  )  { 
final   File   here  =  new   File  (  "."  )  ; 
try  { 
return   here  .  getCanonicalPath  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
; 
return   here  .  getAbsolutePath  (  )  ; 
} 









public   static   boolean   deleteFile  (  final   String   filename  )  { 
return   deleteFile  (  new   File  (  filename  )  )  ; 
} 







public   static   boolean   deleteFile  (  final   File   file  )  { 
try  { 
if  (  file  .  isDirectory  (  )  )  { 
final   String   fullpath  =  file  .  getCanonicalPath  (  )  ; 
for  (  final   String   filename  :  file  .  list  (  )  )  { 
deleteFile  (  new   File  (  fullpath  ,  filename  )  )  ; 
} 
} 
return  !  file  .  exists  (  )  ||  file  .  delete  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   false  ; 
} 













public   static   FileOutputStream   makeFile  (  final   String   dirname  ,  final   String   filename  ,  final   boolean   append  )  throws   IOException  { 
if  (  isEmpty  (  dirname  )  )  { 
return   new   FileOutputStream  (  new   File  (  filename  )  ,  append  )  ; 
}  else  { 
final   File   dir  =  new   File  (  dirname  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
if  (  dir  .  exists  (  )  )  { 
dir  .  delete  (  )  ; 
} 
dir  .  mkdirs  (  )  ; 
} 
return   new   FileOutputStream  (  new   File  (  dirname  ,  filename  )  ,  append  )  ; 
} 
} 











public   static   FileOutputStream   makeFile  (  final   String   dir  ,  final   String   filename  )  throws   IOException  { 
return   makeFile  (  dir  ,  filename  ,  false  )  ; 
} 












public   static   FileOutputStream   makeFile  (  final   String  [  ]  path  ,  final   boolean   append  )  throws   IOException  { 
return   makeFile  (  path  [  0  ]  ,  path  [  1  ]  ,  append  )  ; 
} 










public   static   FileOutputStream   makeFile  (  final   String  ...  path  )  throws   IOException  { 
return   path  .  length  <  1  ?  null  :  path  .  length  <  2  ?  makeFile  (  path  [  0  ]  )  :  path  .  length  <  3  ?  makeFile  (  path  [  0  ]  ,  path  [  1  ]  )  :  makeFile  (  join  (  File  .  separator  ,  path  )  )  ; 
} 












public   static   FileOutputStream   makeFile  (  final   String   path  ,  final   boolean   append  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  ,  append  )  ; 
} 










public   static   FileOutputStream   makeFile  (  final   String   path  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  )  ; 
} 












public   static   FileOutputStream   makeFile  (  final   File   file  ,  final   boolean   append  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  ,  append  )  ; 
} 










public   static   FileOutputStream   makeFile  (  final   File   file  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  )  ; 
} 











public   static   final   OutputStreamWriter   makeFileWriter  (  final   String   path  ,  final   String   encoding  )  throws   IOException  { 
return   new   OutputStreamWriter  (  makeFile  (  path  )  ,  encoding  )  ; 
} 







public   static   final   int   adjustSizeByMooreLaw  (  final   int   size  ,  final   int   thisYear  )  { 
final   double   milli  =  System  .  currentTimeMillis  (  )  ; 
final   double   aYear  =  (  double  )  1000  *  60  *  60  *  24  *  (  365  *  4  +  1  )  /  4  ; 
final   double   q  =  Math  .  exp  (  (  milli  /  aYear  +  1970  -  thisYear  )  /  3  *  Math  .  log  (  2  )  )  ; 
return  (  int  )  (  size  *  q  )  ; 
} 








private   static   final   int   MAX_BUFFER_SIZE  =  Files  .  adjustSizeByMooreLaw  (  65536  ,  2004  )  ; 

public   static   final   String   readString  (  final   Reader   reader  )  { 
try  { 
final   StringBuffer   buf  =  new   StringBuffer  (  )  ; 
final   char  [  ]  chars  =  new   char  [  MAX_BUFFER_SIZE  ]  ; 
int   l  ; 
while  (  (  l  =  reader  .  read  (  chars  )  )  >  0  )  { 
buf  .  append  (  chars  ,  0  ,  l  )  ; 
} 
return   buf  .  toString  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 











public   static   final   String   readStringFromFile  (  final   File   file  )  { 
try  { 
return   readString  (  new   FileReader  (  file  )  )  ; 
}  catch  (  final   Exception   e  )  { 
if  (  DEBUG  )  { 
System  .  out  .  println  (  e  )  ; 
} 
} 
return   null  ; 
} 









public   static   final   String   readStringFromFile  (  final   File   file  ,  final   String   encoding  )  { 
try  { 
return   readString  (  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  encoding  )  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 











public   static   final   String   readStringFromFile  (  final   String   filename  )  { 
return   readStringFromFile  (  new   File  (  filename  )  )  ; 
} 











public   static   final   byte  [  ]  readBytesFromStream  (  final   InputStream   is  )  { 
try  { 
final   ArrayList  <  byte  [  ]  >  chunkList  =  new   ArrayList  <  byte  [  ]  >  (  )  ; 
int   total  =  0  ; 
int   l  ; 
while  (  (  l  =  is  .  available  (  )  )  >  0  )  { 
final   byte  [  ]  chunk  =  new   byte  [  l  ]  ; 
is  .  read  (  chunk  )  ; 
chunkList  .  add  (  chunk  )  ; 
total  +=  l  ; 
} 
final   byte  [  ]  buffer  =  new   byte  [  total  ]  ; 
final   int   pos  =  0  ; 
for  (  final   byte  [  ]  chunk  :  chunkList  )  { 
java  .  lang  .  System  .  arraycopy  (  chunk  ,  0  ,  buffer  ,  pos  ,  chunk  .  length  )  ; 
} 
return   buffer  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 











public   static   final   byte  [  ]  readBytesFromFile  (  final   String   filename  )  { 
try  { 
final   File   file  =  new   File  (  filename  )  ; 
final   long   fullsize  =  file  .  length  (  )  ; 
if  (  fullsize  >  Integer  .  MAX_VALUE  )  { 
throw   new   IOException  (  "File too large"  )  ; 
} 
final   FileChannel   channel  =  new   FileInputStream  (  file  )  .  getChannel  (  )  ; 
final   MappedByteBuffer   buffer  =  channel  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  file  .  length  (  )  )  ; 
final   byte  [  ]  result  =  new   byte  [  (  int  )  fullsize  ]  ; 
buffer  .  get  (  result  )  ; 
return   result  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  final   CharSequence   data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  )  )  ; 
write  (  sw  ,  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  final   char  [  ]  data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  )  )  ; 
sw  .  write  (  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   writeToFile  (  final   byte  [  ]  data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStream   os  =  makeFile  (  file  )  ; 
os  .  write  (  data  )  ; 
os  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 







public   static   final   File   writeBytesToFile  (  final   byte  [  ]  data  ,  final   String   fileTo  )  { 
return   writeToFile  (  data  ,  fileTo  )  ; 
} 









public   static   final   File   writeToFile  (  final   InputStream   is  ,  final   String   fileTo  )  throws   IOException  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStream   os  =  makeFile  (  file  )  ; 
pipe  (  is  ,  os  ,  false  )  ; 
os  .  close  (  )  ; 
return   file  ; 
} 








public   static   final   File   appendToFile  (  final   CharSequence   data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  ,  true  )  )  ; 
write  (  sw  ,  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendToFile  (  final   char  [  ]  data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStreamWriter   sw  =  new   OutputStreamWriter  (  makeFile  (  file  ,  true  )  )  ; 
sw  .  write  (  data  )  ; 
sw  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendToFile  (  final   byte  [  ]  data  ,  final   String   fileTo  )  { 
try  { 
final   File   file  =  new   File  (  fileTo  )  ; 
final   OutputStream   os  =  makeFile  (  file  ,  true  )  ; 
os  .  write  (  data  )  ; 
os  .  close  (  )  ; 
return   file  ; 
}  catch  (  final   Exception   e  )  { 
} 
return   null  ; 
} 








public   static   final   File   appendBytesToFile  (  final   char  [  ]  data  ,  final   String   fileTo  )  { 
return   appendBytesToFile  (  toBytes  (  data  )  ,  fileTo  )  ; 
} 





public   static   final   File   appendBytesToFile  (  final   byte  [  ]  data  ,  final   String   fileTo  )  { 
return   appendToFile  (  data  ,  fileTo  )  ; 
} 















public   static   final   String   getPackageName  (  final   String   basePath  ,  final   String   currentPath  )  { 
final   String   path  =  relPath  (  basePath  ,  currentPath  )  ; 
return   path  ==  null  ?  null  :  path  .  equals  (  currentPath  )  ?  null  :  path  .  replace  (  File  .  separatorChar  ,  '.'  )  ; 
} 







public   interface   ByteFilter  { 







byte  [  ]  filter  (  byte  [  ]  input  ,  int   length  )  ; 
} 






public   interface   BufferingFilter   extends   ByteFilter  { 





byte  [  ]  getBuffer  (  )  ; 




void   clear  (  )  ; 
} 












public   static   void   pipe  (  final   InputStream   in  ,  final   OutputStream   out  ,  final   boolean   isBlocking  ,  final   ByteFilter   filter  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  MAX_BUFFER_SIZE  ]  ; 
int   nread  ; 
int   navailable  ; 
int   total  =  0  ; 
synchronized  (  in  )  { 
while  (  (  (  navailable  =  isBlocking  ?  buf  .  length  :  in  .  available  (  )  )  >  0  )  &&  (  (  nread  =  in  .  read  (  buf  ,  0  ,  Math  .  min  (  buf  .  length  ,  navailable  )  )  )  >=  0  )  )  { 
if  (  filter  ==  null  )  { 
out  .  write  (  buf  ,  0  ,  nread  )  ; 
}  else  { 
final   byte  [  ]  filtered  =  filter  .  filter  (  buf  ,  nread  )  ; 
out  .  write  (  filtered  )  ; 
} 
total  +=  nread  ; 
} 
} 
out  .  flush  (  )  ; 
buf  =  null  ; 
} 











public   static   void   pipe  (  final   InputStream   in  ,  final   OutputStream   out  ,  final   boolean   isBlocking  )  throws   IOException  { 
pipe  (  in  ,  out  ,  isBlocking  ,  null  )  ; 
} 










public   static   boolean   pipe  (  final   Reader   in  ,  final   Writer   out  )  { 
if  (  in  ==  null  )  { 
return   false  ; 
} 
if  (  out  ==  null  )  { 
return   false  ; 
} 
try  { 
int   c  ; 
synchronized  (  in  )  { 
while  (  in  .  ready  (  )  &&  (  (  c  =  in  .  read  (  )  )  >  0  )  )  { 
out  .  write  (  c  )  ; 
} 
} 
out  .  flush  (  )  ; 
}  catch  (  final   Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 

private   static   final   boolean   COPY_DEBUG  =  false  ; 











public   static   boolean   copy  (  final   String   from  ,  final   String   to  ,  final   String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 











public   static   boolean   copy  (  final   File   from  ,  final   File   to  ,  final   String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 










public   static   boolean   copy  (  final   String   from  ,  final   String   to  )  { 
return   copy  (  new   File  (  from  )  ,  new   File  (  to  )  )  ; 
} 










public   static   boolean   USE_NIO  =  true  ; 

public   static   boolean   copy  (  final   File   from  ,  final   File   to  )  { 
if  (  from  .  isDirectory  (  )  )  { 
to  .  mkdirs  (  )  ; 
for  (  final   String   name  :  Arrays  .  asList  (  from  .  list  (  )  )  )  { 
if  (  !  copy  (  from  ,  to  ,  name  )  )  { 
if  (  COPY_DEBUG  )  { 
System  .  out  .  println  (  "Failed to copy "  +  name  +  " from "  +  from  +  " to "  +  to  )  ; 
} 
return   false  ; 
} 
} 
}  else  { 
try  { 
final   FileInputStream   is  =  new   FileInputStream  (  from  )  ; 
final   FileChannel   ifc  =  is  .  getChannel  (  )  ; 
final   FileOutputStream   os  =  makeFile  (  to  )  ; 
if  (  USE_NIO  )  { 
final   FileChannel   ofc  =  os  .  getChannel  (  )  ; 
ofc  .  transferFrom  (  ifc  ,  0  ,  from  .  length  (  )  )  ; 
}  else  { 
pipe  (  is  ,  os  ,  false  )  ; 
} 
is  .  close  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  final   IOException   ex  )  { 
if  (  COPY_DEBUG  )  { 
System  .  out  .  println  (  "Failed to copy "  +  from  +  " to "  +  to  +  ": "  +  ex  )  ; 
} 
return   false  ; 
} 
} 
final   long   time  =  from  .  lastModified  (  )  ; 
setLastModified  (  to  ,  time  )  ; 
final   long   newtime  =  to  .  lastModified  (  )  ; 
if  (  COPY_DEBUG  )  { 
if  (  newtime  !=  time  )  { 
System  .  out  .  println  (  "Failed to set timestamp for file "  +  to  +  ": tried "  +  new   Date  (  time  )  +  ", have "  +  new   Date  (  newtime  )  )  ; 
to  .  setLastModified  (  time  )  ; 
final   long   morenewtime  =  to  .  lastModified  (  )  ; 
return   false  ; 
}  else  { 
System  .  out  .  println  (  "Timestamp for "  +  to  +  " set successfully."  )  ; 
} 
} 
return   time  ==  newtime  ; 
} 


























public   static   boolean   setLastModified  (  final   File   file  ,  final   long   time  )  { 
if  (  file  .  setLastModified  (  time  )  )  { 
return   true  ; 
} 
System  .  gc  (  )  ; 
return   file  .  setLastModified  (  time  )  ; 
} 

private   static   final   boolean   EQUAL_DEBUG  =  DEBUG  ; 







public   static   boolean   equal  (  final   File   left  ,  final   File   right  )  { 
if  (  left  .  isDirectory  (  )  &&  right  .  isDirectory  (  )  )  { 
final   Set  <  String  >  leftSet  =  new   HashSet  <  String  >  (  Arrays  .  asList  (  left  .  list  (  )  )  )  ; 
final   Set  <  String  >  rightSet  =  new   HashSet  <  String  >  (  Arrays  .  asList  (  right  .  list  (  )  )  )  ; 
if  (  leftSet  .  size  (  )  !=  rightSet  .  size  (  )  )  { 
if  (  EQUAL_DEBUG  )  { 
System  .  out  .  println  (  left  .  getPath  (  )  +  " has "  +  leftSet  .  size  (  )  +  " while "  +  right  .  getPath  (  )  +  " has "  +  rightSet  .  size  (  )  )  ; 
} 
return   false  ; 
} 
for  (  final   String   name  :  leftSet  )  { 
if  (  rightSet  .  contains  (  name  )  )  { 
if  (  !  equal  (  new   File  (  left  ,  name  )  ,  new   File  (  right  ,  name  )  )  )  { 
if  (  EQUAL_DEBUG  )  { 
System  .  out  .  println  (  left  .  getPath  (  )  +  File  .  separator  +  name  +  " is different from "  +  right  .  getPath  (  )  +  File  .  separator  +  name  )  ; 
} 
return   false  ; 
} 
}  else  { 
if  (  EQUAL_DEBUG  )  { 
System  .  out  .  println  (  right  .  getPath  (  )  +  " does not contain "  +  name  )  ; 
} 
return   false  ; 
} 
} 
return   true  ; 
}  else   if  (  left  .  isFile  (  )  &&  right  .  isFile  (  )  )  { 
try  { 
return   compare  (  left  ,  right  )  ==  0  ; 
}  catch  (  final   IOException   e  )  { 
if  (  EQUAL_DEBUG  )  { 
System  .  out  .  println  (  e  .  getMessage  (  )  +  " while comparing "  +  left  .  getPath  (  )  +  " and "  +  right  .  getPath  (  )  )  ; 
} 
return   false  ; 
} 
}  else  { 
return   false  ; 
} 
} 















public   static   int   compare  (  final   File   left  ,  final   File   right  )  throws   IOException  { 
final   long   lm  =  left  .  lastModified  (  )  /  1000  ; 
final   long   rm  =  right  .  lastModified  (  )  /  1000  ; 
if  (  lm  <  rm  )  { 
return  -  1  ; 
} 
if  (  lm  >  rm  )  { 
return   1  ; 
} 
final   long   ll  =  left  .  length  (  )  ; 
final   long   rl  =  right  .  length  (  )  ; 
if  (  ll  <  rl  )  { 
return  -  1  ; 
} 
if  (  ll  >  rl  )  { 
return   1  ; 
} 
final   InputStream   is1  =  new   BufferedInputStream  (  new   FileInputStream  (  left  )  )  ; 
final   InputStream   is2  =  new   BufferedInputStream  (  new   FileInputStream  (  right  )  )  ; 
for  (  long   i  =  0  ;  i  <  ll  ;  i  ++  )  { 
final   int   b1  =  is1  .  read  (  )  ; 
final   int   b2  =  is2  .  read  (  )  ; 
if  (  b1  <  0  )  { 
return  -  1  ; 
} 
if  (  b2  <  0  )  { 
return   1  ; 
} 
if  (  b1  !=  b2  )  { 
return   b1  <  b2  ?  -  1  :  1  ; 
} 
} 
return   0  ; 
} 












public   static   boolean   synchronize  (  final   File   left  ,  final   File   right  ,  final   String   what  )  { 
return   synchronize  (  new   File  (  left  ,  what  )  ,  new   File  (  right  ,  what  )  )  ; 
} 











public   static   boolean   synchronize  (  final   File   left  ,  final   File   right  )  { 
if  (  left  .  isDirectory  (  )  ||  right  .  isDirectory  (  )  )  { 
final   String  [  ]  leftContents  =  left  .  list  (  )  ; 
final   Set  <  String  >  contents  =  leftContents  ==  null  ?  new   LinkedHashSet  <  String  >  (  )  :  new   LinkedHashSet  <  String  >  (  Arrays  .  asList  (  leftContents  )  )  ; 
final   String  [  ]  rightContents  =  right  .  list  (  )  ; 
if  (  rightContents  !=  null  )  { 
contents  .  addAll  (  Arrays  .  asList  (  rightContents  )  )  ; 
} 
for  (  final   String   name  :  contents  )  { 
if  (  !  synchronize  (  left  ,  right  ,  name  )  )  { 
return   false  ; 
} 
} 
}  else  { 
final   long   leftTime  =  left  .  lastModified  (  )  ; 
final   long   rightTime  =  right  .  lastModified  (  )  ; 
if  (  left  .  exists  (  )  &&  (  !  right  .  exists  (  )  ||  (  leftTime  <  rightTime  )  )  )  { 
return   copy  (  left  ,  right  )  ; 
}  else   if  (  right  .  exists  (  )  &&  (  !  left  .  exists  (  )  ||  (  leftTime  >  rightTime  )  )  )  { 
return   copy  (  right  ,  left  )  ; 
} 
} 
return   true  ; 
} 













public   static   boolean   unzip  (  final   ZipInputStream   zis  ,  final   File   location  )  throws   IOException  { 
if  (  !  location  .  exists  (  )  )  { 
location  .  mkdirs  (  )  ; 
} 
ZipEntry   ze  ; 
while  (  (  ze  =  zis  .  getNextEntry  (  )  )  !=  null  )  { 
final   File   output  =  new   File  (  location  ,  ze  .  getName  (  )  )  ; 
if  (  ze  .  isDirectory  (  )  )  { 
output  .  mkdirs  (  )  ; 
}  else  { 
final   File   dir  =  output  .  getParentFile  (  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
dir  .  delete  (  )  ; 
} 
dir  .  mkdirs  (  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
System  .  err  .  println  (  "Could not create directory "  +  dir  .  getCanonicalPath  (  )  )  ; 
return   false  ; 
} 
final   OutputStream   os  =  new   FileOutputStream  (  output  )  ; 
pipe  (  zis  ,  os  ,  true  )  ; 
os  .  close  (  )  ; 
} 
} 
zis  .  close  (  )  ; 
return   true  ; 
} 










public   static   boolean   install  (  final   Class   clazz  ,  final   String   resourceArchiveName  ,  final   File   location  )  throws   IOException  { 
final   ZipInputStream   zis  =  new   ZipInputStream  (  clazz  .  getResourceAsStream  (  resourceArchiveName  )  )  ; 
return   unzip  (  zis  ,  location  )  ; 
} 










public   static   boolean   install  (  final   Class   clazz  ,  final   String   resourceArchiveName  ,  final   String   folderName  )  throws   IOException  { 
return   install  (  clazz  ,  resourceArchiveName  ,  new   File  (  folderName  )  )  ; 
} 

private   static   class   ByteIterator   implements   Iterator  <  Byte  >  { 

IOException   exception  =  null  ; 

byte   next  ; 

boolean   have  =  false  ; 

InputStream   is  =  null  ; 

private   ByteIterator  (  final   InputStream   is  )  { 
this  .  is  =  is  ; 
} 

public   boolean   hasNext  (  )  { 
if  (  have  )  { 
return   true  ; 
}  else   if  (  is  ==  null  )  { 
return   false  ; 
}  else  { 
try  { 
final   int   input  =  is  .  read  (  )  ; 
if  (  input  <  0  )  { 
close  (  )  ; 
}  else  { 
have  =  true  ; 
next  =  (  byte  )  input  ; 
} 
}  catch  (  final   IOException   ex  )  { 
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
}  catch  (  final   Exception   e  )  { 
} 
} 
is  =  null  ; 
} 

@  Override 
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
}  catch  (  final   IOException   e  )  { 
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

private   CharIterator  (  final   Reader   reader  )  { 
this  .  reader  =  reader  ; 
} 

public   boolean   hasNext  (  )  { 
if  (  have  )  { 
return   true  ; 
}  else   if  (  reader  ==  null  )  { 
return   false  ; 
}  else  { 
try  { 
final   int   input  =  reader  .  read  (  )  ; 
if  (  input  <  0  )  { 
close  (  )  ; 
}  else  { 
have  =  true  ; 
next  =  (  char  )  input  ; 
return   true  ; 
} 
}  catch  (  final   IOException   ex  )  { 
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
}  catch  (  final   Exception   e  )  { 
} 
} 
reader  =  null  ; 
} 

@  Override 
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
}  catch  (  final   IOException   e  )  { 
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

private   LineIterator  (  final   Reader   reader  )  { 
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
}  catch  (  final   IOException   ex  )  { 
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
}  catch  (  final   Exception   e  )  { 
} 
} 
lr  =  null  ; 
} 

@  Override 
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
}  catch  (  final   IOException   e  )  { 
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

