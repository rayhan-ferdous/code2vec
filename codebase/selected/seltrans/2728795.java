package   org  .  eclipse  .  emf  .  test  .  build  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 




public   class   FileTool  { 







public   interface   IZipFilter  { 

















public   boolean   shouldExtract  (  String   fullEntryName  ,  String   entryName  ,  int   depth  )  ; 



















public   boolean   shouldUnzip  (  String   fullEntryName  ,  String   entryName  ,  int   depth  )  ; 
} 




private   static   byte  [  ]  buffer  =  new   byte  [  8192  ]  ; 














public   static   String   changeSeparator  (  String   path  ,  char   oldSeparator  ,  char   newSeparator  )  { 
return   path  .  replace  (  oldSeparator  ,  newSeparator  )  ; 
} 












public   static   boolean   compare  (  File   file1  ,  File   file2  )  throws   IOException  { 
if  (  file1  .  length  (  )  !=  file2  .  length  (  )  )  { 
return   false  ; 
} 
InputStream   is1  =  null  ; 
InputStream   is2  =  null  ; 
try  { 
is1  =  new   BufferedInputStream  (  new   FileInputStream  (  file1  )  )  ; 
is2  =  new   BufferedInputStream  (  new   FileInputStream  (  file2  )  )  ; 
int   a  =  0  ; 
int   b  =  0  ; 
boolean   same  =  true  ; 
while  (  same  &&  a  !=  -  1  &&  b  !=  -  1  )  { 
a  =  is1  .  read  (  )  ; 
b  =  is2  .  read  (  )  ; 
same  =  a  ==  b  ; 
} 
return   same  ; 
}  finally  { 
if  (  is2  !=  null  )  { 
try  { 
is2  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
if  (  is1  !=  null  )  { 
try  { 
is1  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 









public   static   void   copy  (  File   src  ,  File   dst  )  throws   IOException  { 
copy  (  src  .  getParentFile  (  )  ,  src  ,  dst  )  ; 
} 










public   static   void   copy  (  File   root  ,  File   src  ,  File   dst  )  throws   IOException  { 
if  (  src  .  isDirectory  (  )  )  { 
String  [  ]  children  =  src  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  ++  i  )  { 
File   child  =  new   File  (  src  ,  children  [  i  ]  )  ; 
copy  (  root  ,  child  ,  dst  )  ; 
} 
}  else  { 
String   rootString  =  root  .  toString  (  )  ; 
String   srcString  =  src  .  toString  (  )  ; 
File   dstFile  =  new   File  (  dst  ,  srcString  .  substring  (  rootString  .  length  (  )  +  1  )  )  ; 
transferData  (  src  ,  dstFile  )  ; 
} 
} 









public   static   void   delete  (  File   file  )  { 
if  (  file  .  exists  (  )  )  { 
if  (  file  .  isDirectory  (  )  )  { 
String  [  ]  children  =  file  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  ++  i  )  { 
File   child  =  new   File  (  file  ,  children  [  i  ]  )  ; 
delete  (  child  )  ; 
} 
} 
if  (  !  file  .  delete  (  )  )  { 
System  .  out  .  println  (  "WARNING: could not delete "  +  file  )  ; 
} 
} 
} 








public   static   File   getFile  (  String  [  ]  segments  )  { 
File   result  =  new   File  (  segments  [  0  ]  )  ; 
for  (  int   i  =  1  ;  i  <  segments  .  length  ;  ++  i  )  { 
result  =  new   File  (  result  ,  segments  [  i  ]  )  ; 
} 
return   result  ; 
} 

















public   static   File  [  ]  getFiles  (  File   dir  ,  String  [  ]  include  ,  String  [  ]  exclude  )  { 
List  <  File  >  list  =  new   ArrayList  <  File  >  (  )  ; 
String  [  ]  children  =  dir  .  list  (  )  ; 
if  (  children  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  ++  i  )  { 
File   child  =  new   File  (  dir  ,  children  [  i  ]  )  ; 
String   name  =  child  .  getName  (  )  ; 
if  (  child  .  isDirectory  (  )  )  { 
File  [  ]  result  =  getFiles  (  child  ,  include  ,  exclude  )  ; 
for  (  int   j  =  0  ;  j  <  result  .  length  ;  ++  j  )  { 
list  .  add  (  result  [  j  ]  )  ; 
} 
}  else  { 
boolean   includeFile  =  include  ==  null  ; 
if  (  include  !=  null  )  { 
for  (  int   j  =  0  ;  j  <  include  .  length  ;  ++  j  )  { 
if  (  name  .  endsWith  (  include  [  j  ]  )  )  { 
includeFile  =  true  ; 
break  ; 
} 
} 
} 
boolean   excludeFile  =  exclude  !=  null  ; 
if  (  exclude  !=  null  )  { 
for  (  int   j  =  0  ;  j  <  exclude  .  length  ;  ++  j  )  { 
if  (  name  .  endsWith  (  exclude  [  j  ]  )  )  { 
excludeFile  =  true  ; 
break  ; 
} 
} 
} 
if  (  includeFile  &&  !  excludeFile  )  { 
list  .  add  (  child  )  ; 
} 
} 
} 
return   list  .  toArray  (  new   File  [  0  ]  )  ; 
} 









public   static   String  [  ]  getSegments  (  File   file  )  { 
return   getSegments  (  file  .  toString  (  )  ,  File  .  separatorChar  )  ; 
} 










public   static   String  [  ]  getSegments  (  String   s  ,  char   separator  )  { 
List  <  String  >  result  =  new   ArrayList  <  String  >  (  )  ; 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  s  ,  ""  +  separator  )  ; 
while  (  tokenizer  .  hasMoreTokens  (  )  )  { 
result  .  add  (  tokenizer  .  nextToken  (  )  )  ; 
} 
return   result  .  toArray  (  new   String  [  0  ]  )  ; 
} 










public   static   File  [  ]  parsePaths  (  String   paths  )  { 
List  <  File  >  result  =  new   ArrayList  <  File  >  (  )  ; 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  paths  ,  ";"  )  ; 
while  (  tokenizer  .  hasMoreTokens  (  )  )  { 
result  .  add  (  new   File  (  tokenizer  .  nextToken  (  )  )  )  ; 
} 
return   result  .  toArray  (  new   File  [  0  ]  )  ; 
} 









public   static   void   transferData  (  File   source  ,  File   destination  )  throws   IOException  { 
destination  .  getParentFile  (  )  .  mkdirs  (  )  ; 
InputStream   is  =  null  ; 
OutputStream   os  =  null  ; 
try  { 
is  =  new   FileInputStream  (  source  )  ; 
os  =  new   FileOutputStream  (  destination  )  ; 
transferData  (  is  ,  os  )  ; 
}  finally  { 
if  (  os  !=  null  )  { 
try  { 
os  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 










public   static   void   transferData  (  InputStream   source  ,  OutputStream   destination  )  throws   IOException  { 
int   bytesRead  =  0  ; 
while  (  bytesRead  !=  -  1  )  { 
bytesRead  =  source  .  read  (  buffer  ,  0  ,  buffer  .  length  )  ; 
if  (  bytesRead  !=  -  1  )  { 
destination  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
} 
} 
} 












public   static   void   unzip  (  IZipFilter   filter  ,  ZipFile   zipFile  ,  File   dstDir  )  throws   IOException  { 
unzip  (  filter  ,  zipFile  ,  dstDir  ,  dstDir  ,  0  )  ; 
} 

private   static   void   unzip  (  IZipFilter   filter  ,  ZipFile   zipFile  ,  File   rootDstDir  ,  File   dstDir  ,  int   depth  )  throws   IOException  { 
Enumeration  <  ?  extends   ZipEntry  >  entries  =  zipFile  .  entries  (  )  ; 
try  { 
while  (  entries  .  hasMoreElements  (  )  )  { 
ZipEntry   entry  =  entries  .  nextElement  (  )  ; 
if  (  entry  .  isDirectory  (  )  )  { 
continue  ; 
} 
String   entryName  =  entry  .  getName  (  )  ; 
File   file  =  new   File  (  dstDir  ,  FileTool  .  changeSeparator  (  entryName  ,  '/'  ,  File  .  separatorChar  )  )  ; 
String   fullEntryName  =  FileTool  .  changeSeparator  (  file  .  toString  (  )  .  substring  (  rootDstDir  .  toString  (  )  .  length  (  )  +  1  )  ,  File  .  separatorChar  ,  '/'  )  ; 
if  (  !  (  filter  ==  null  ||  filter  .  shouldExtract  (  fullEntryName  ,  entryName  ,  depth  )  )  )  { 
continue  ; 
} 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
InputStream   src  =  null  ; 
OutputStream   dst  =  null  ; 
try  { 
src  =  zipFile  .  getInputStream  (  entry  )  ; 
dst  =  new   FileOutputStream  (  file  )  ; 
transferData  (  src  ,  dst  )  ; 
}  finally  { 
if  (  dst  !=  null  )  { 
try  { 
dst  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
if  (  src  !=  null  )  { 
try  { 
src  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
if  (  (  entryName  .  endsWith  (  ".zip"  )  ||  entryName  .  endsWith  (  ".jar"  )  )  &&  (  filter  ==  null  ||  filter  .  shouldUnzip  (  fullEntryName  ,  entryName  ,  depth  )  )  )  { 
String   fileName  =  file  .  getName  (  )  ; 
String   dirName  =  fileName  .  substring  (  0  ,  fileName  .  length  (  )  -  4  )  +  "_"  +  fileName  .  substring  (  fileName  .  length  (  )  -  3  )  ; 
ZipFile   innerZipFile  =  null  ; 
try  { 
innerZipFile  =  new   ZipFile  (  file  )  ; 
File   innerDstDir  =  new   File  (  file  .  getParentFile  (  )  ,  dirName  )  ; 
unzip  (  filter  ,  innerZipFile  ,  rootDstDir  ,  innerDstDir  ,  depth  +  1  )  ; 
file  .  delete  (  )  ; 
}  catch  (  IOException   e  )  { 
if  (  innerZipFile  !=  null  )  { 
try  { 
innerZipFile  .  close  (  )  ; 
}  catch  (  IOException   e2  )  { 
} 
System  .  out  .  println  (  "Could not unzip: "  +  fileName  +  ". InnerZip = "  +  innerZipFile  .  getName  (  )  +  ". Length: "  +  innerZipFile  .  getName  (  )  .  length  (  )  )  ; 
} 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
}  finally  { 
try  { 
zipFile  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 









public   static   void   zip  (  File   dir  ,  File   zipFile  )  throws   IOException  { 
BufferedOutputStream   bos  =  null  ; 
ZipOutputStream   zos  =  null  ; 
try  { 
bos  =  new   BufferedOutputStream  (  new   FileOutputStream  (  zipFile  )  )  ; 
zos  =  new   ZipOutputStream  (  bos  )  ; 
zip  (  dir  ,  dir  ,  zos  )  ; 
}  finally  { 
if  (  zos  ==  null  )  { 
if  (  bos  !=  null  )  { 
try  { 
bos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
}  else  { 
try  { 
zos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 

private   static   void   zip  (  File   root  ,  File   file  ,  ZipOutputStream   zos  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
String   name  =  file  .  getName  (  )  ; 
if  (  name  .  endsWith  (  "_zip"  )  ||  name  .  endsWith  (  "_jar"  )  )  { 
String   rootString  =  root  .  toString  (  )  ; 
String   fileString  =  file  .  toString  (  )  ; 
String   zipEntryName  =  fileString  .  substring  (  rootString  .  length  (  )  +  1  )  ; 
int   underscoreIndex  =  zipEntryName  .  lastIndexOf  (  "_"  )  ; 
zipEntryName  =  zipEntryName  .  substring  (  0  ,  underscoreIndex  )  +  "."  +  zipEntryName  .  substring  (  underscoreIndex  +  1  )  ; 
ZipEntry   zipEntry  =  new   ZipEntry  (  changeSeparator  (  zipEntryName  ,  File  .  separatorChar  ,  '/'  )  )  ; 
zos  .  putNextEntry  (  zipEntry  )  ; 
ZipOutputStream   zos2  =  new   ZipOutputStream  (  zos  )  ; 
String  [  ]  list  =  file  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  list  .  length  ;  ++  i  )  { 
File   item  =  new   File  (  file  ,  list  [  i  ]  )  ; 
zip  (  file  ,  item  ,  zos2  )  ; 
} 
zos2  .  finish  (  )  ; 
zos  .  closeEntry  (  )  ; 
}  else  { 
String  [  ]  list  =  file  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  list  .  length  ;  ++  i  )  { 
File   item  =  new   File  (  file  ,  list  [  i  ]  )  ; 
zip  (  root  ,  item  ,  zos  )  ; 
} 
} 
}  else  { 
String   rootString  =  root  .  toString  (  )  ; 
String   fileString  =  file  .  toString  (  )  ; 
String   zipEntryName  =  fileString  .  substring  (  rootString  .  length  (  )  +  1  )  ; 
ZipEntry   zipEntry  =  new   ZipEntry  (  changeSeparator  (  zipEntryName  ,  File  .  separatorChar  ,  '/'  )  )  ; 
zos  .  putNextEntry  (  zipEntry  )  ; 
FileInputStream   fos  =  null  ; 
try  { 
fos  =  new   FileInputStream  (  file  )  ; 
transferData  (  fos  ,  zos  )  ; 
}  finally  { 
if  (  fos  !=  null  )  { 
try  { 
fos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
zos  .  closeEntry  (  )  ; 
} 
} 
} 

