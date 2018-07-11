package   org  .  bongolipi  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  zip  .  *  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  bongolipi  .  btrans  .  service  .  Configuration  ; 
import   static   org  .  bongolipi  .  btrans  .  util  .  BtransConstants  .  *  ; 




public   class   FileUtility   extends   File  { 

public   static   int   PERMISSION_NONE  =  000  ; 

public   static   int   PERMISSION_READ  =  111  ; 

public   static   int   PERMISSION_READ_WRITE  =  666  ; 

public   static   int   PERMISSION_READ_WRITE_EXECUTE  =  777  ; 

private   static   final   Logger   log  =  Logger  .  getLogger  (  FileUtility  .  class  )  ; 

private   String   pathName  =  ""  ; 

public   FileUtility  (  String   pathname  )  { 
super  (  pathname  )  ; 
pathName  =  pathname  ; 
} 









public   static   String   getAbsoluteParentPath  (  String   filename  )  throws   FileNotFoundException  ,  IOException  ,  URISyntaxException  { 
ClassLoader   loader  ; 
URL   url  =  null  ; 
String   path  ; 
loader  =  FileUtility  .  class  .  getClassLoader  (  )  ; 
url  =  loader  .  getResource  (  filename  )  ; 
path  =  new   File  (  url  .  toURI  (  )  )  .  getParent  (  )  ; 
if  (  null  ==  path  )  { 
path  =  new   File  (  url  .  toURI  (  )  )  .  getAbsolutePath  (  )  ; 
} 
return   path  ; 
} 




public   String   getContent  (  )  throws   IOException  { 
StringBuilder   content  =  new   StringBuilder  (  )  ; 
if  (  !  this  .  exists  (  )  )  { 
abort  (  "FileUtility: no such source file: "  +  pathName  )  ; 
} 
BufferedReader   reader  =  new   BufferedReader  (  new   FileReader  (  this  )  )  ; 
String   line  ; 
while  (  null  !=  (  line  =  reader  .  readLine  (  )  )  )  { 
content  .  append  (  line  )  .  append  (  LINE_SEPARATOR  )  ; 
} 
return   content  .  toString  (  )  ; 
} 





public   static   String   getContent  (  String   pathname  )  throws   IOException  { 
FileUtility   fu  =  new   FileUtility  (  pathname  )  ; 
return   fu  .  getContent  (  )  ; 
} 



public   boolean   remove  (  )  throws   SecurityException  ,  IOException  { 
if  (  !  this  .  isDirectory  (  )  )  { 
if  (  this  .  getParentFile  (  )  !=  null  &&  this  .  getParentFile  (  )  .  canWrite  (  )  )  { 
return   this  .  delete  (  )  ; 
}  else  { 
abort  (  "FileUtility: Cannot delete file: "  +  pathName  )  ; 
return   false  ; 
} 
}  else  { 
String  [  ]  contentList  =  this  .  list  (  )  ; 
if  (  contentList  !=  null  &&  contentList  .  length  >  0  )  { 
for  (  int   contentCount  =  0  ;  contentCount  <  contentList  .  length  ;  contentCount  ++  )  { 
(  new   FileUtility  (  this  .  getPath  (  )  +  File  .  separator  +  contentList  [  contentCount  ]  )  )  .  remove  (  )  ; 
} 
this  .  delete  (  )  ; 
}  else   return   this  .  delete  (  )  ; 
} 
return   true  ; 
} 





public   static   boolean   remove  (  String   path  )  throws   SecurityException  ,  IOException  { 
FileUtility   f  =  new   FileUtility  (  path  )  ; 
return   f  .  remove  (  )  ; 
} 







public   static   boolean   copy  (  String   fromFile  ,  String   toFile  )  throws   IOException  { 
FileUtility   fu  =  new   FileUtility  (  toFile  )  ; 
return   fu  .  copy  (  fromFile  )  ; 
} 






public   boolean   copy  (  String   fromFile  )  throws   IOException  { 
File   file  =  new   File  (  fromFile  )  ; 
return   copy  (  file  )  ; 
} 







public   boolean   copy  (  File   fromFile  )  throws   IOException  { 
FileUtility   toFile  =  this  ; 
if  (  !  fromFile  .  exists  (  )  )  { 
abort  (  "FileUtility: no such source file: "  +  fromFile  .  getAbsolutePath  (  )  )  ; 
return   false  ; 
} 
if  (  !  fromFile  .  isFile  (  )  )  { 
abort  (  "FileUtility: can't copy directory: "  +  fromFile  .  getAbsolutePath  (  )  )  ; 
return   false  ; 
} 
if  (  !  fromFile  .  canRead  (  )  )  { 
abort  (  "FileUtility: source file is unreadable: "  +  fromFile  .  getAbsolutePath  (  )  )  ; 
return   false  ; 
} 
if  (  this  .  isDirectory  (  )  )  toFile  =  (  FileUtility  )  (  new   File  (  this  ,  fromFile  .  getName  (  )  )  )  ; 
if  (  toFile  .  exists  (  )  )  { 
if  (  !  toFile  .  canWrite  (  )  )  { 
abort  (  "FileUtility: destination file is unwriteable: "  +  pathName  )  ; 
return   false  ; 
} 
}  else  { 
String   parent  =  toFile  .  getParent  (  )  ; 
File   dir  =  new   File  (  parent  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
abort  (  "FileUtility: destination directory doesn't exist: "  +  parent  )  ; 
return   false  ; 
} 
if  (  dir  .  isFile  (  )  )  { 
abort  (  "FileUtility: destination is not a directory: "  +  parent  )  ; 
return   false  ; 
} 
if  (  !  dir  .  canWrite  (  )  )  { 
abort  (  "FileUtility: destination directory is unwriteable: "  +  parent  )  ; 
return   false  ; 
} 
} 
FileInputStream   from  =  null  ; 
FileOutputStream   to  =  null  ; 
try  { 
from  =  new   FileInputStream  (  fromFile  )  ; 
to  =  new   FileOutputStream  (  toFile  )  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  from  .  read  (  buffer  )  )  !=  -  1  )  to  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
}  finally  { 
if  (  from  !=  null  )  try  { 
from  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
if  (  to  !=  null  )  try  { 
to  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
} 
return   true  ; 
} 






public   static   boolean   write  (  String   content  ,  String   toFile  )  throws   IOException  { 
FileUtility   fu  =  new   FileUtility  (  toFile  )  ; 
return   fu  .  save  (  content  )  ; 
} 





public   boolean   save  (  String   content  )  throws   IOException  { 
FileUtility   toFile  =  this  ; 
if  (  this  .  isDirectory  (  )  )  abort  (  "FileUtility: destination is a directory: "  +  pathName  )  ; 
if  (  toFile  .  exists  (  )  )  { 
if  (  !  toFile  .  canWrite  (  )  )  { 
abort  (  "FileUtility: destination file is unwriteable: "  +  pathName  )  ; 
return   false  ; 
} 
}  else  { 
String   parent  =  toFile  .  getParent  (  )  ; 
File   dir  =  new   File  (  parent  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
abort  (  "FileUtility: destination directory doesn't exist: "  +  parent  )  ; 
return   false  ; 
} 
if  (  dir  .  isFile  (  )  )  { 
abort  (  "FileUtility: destination is not a directory: "  +  parent  )  ; 
return   false  ; 
} 
if  (  !  dir  .  canWrite  (  )  )  { 
abort  (  "FileUtility: destination directory is unwriteable: "  +  parent  )  ; 
return   false  ; 
} 
} 
FileOutputStream   to  =  null  ; 
try  { 
to  =  new   FileOutputStream  (  toFile  )  ; 
byte  [  ]  buffer  =  content  .  getBytes  (  )  ; 
int   bytes_read  =  buffer  .  length  ; 
; 
to  .  write  (  buffer  )  ; 
}  finally  { 
if  (  to  !=  null  )  try  { 
to  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
} 
return   true  ; 
} 







public   static   boolean   save  (  String   content  ,  String   filePath  )  throws   IOException  { 
FileUtility   dest  =  new   FileUtility  (  filePath  )  ; 
return   dest  .  save  (  content  )  ; 
} 


public   static   boolean   createDirectory  (  String   name  )  throws   IOException  ,  InterruptedException  { 
File   directory  =  new   File  (  name  )  ; 
if  (  !  directory  .  exists  (  )  )  { 
directory  .  mkdirs  (  )  ; 
}  else  { 
abort  (  "Directory exists: "  +  name  )  ; 
} 
if  (  !  System  .  getProperty  (  "os.name"  )  .  startsWith  (  "Windows"  )  )  { 
Process   permission  =  Runtime  .  getRuntime  (  )  .  exec  (  "chmod "  +  PERMISSION_READ_WRITE_EXECUTE  +  " "  +  name  )  ; 
permission  .  waitFor  (  )  ; 
} 
return   true  ; 
} 











public   static   boolean   copyDirectory  (  String   fromPath  ,  String   toPath  )  throws   IOException  ,  InterruptedException  ,  SecurityException  { 
if  (  !  fromPath  .  endsWith  (  File  .  separator  )  )  fromPath  =  fromPath  +  File  .  separator  ; 
if  (  !  toPath  .  endsWith  (  File  .  separator  )  )  toPath  =  toPath  +  File  .  separator  ; 
File   from  =  new   File  (  fromPath  )  ; 
File   to  =  new   File  (  toPath  )  ; 
String  [  ]  files  ; 
if  (  !  from  .  exists  (  )  )  abort  (  "Source does not exist: "  +  fromPath  )  ; 
if  (  !  from  .  isDirectory  (  )  )  abort  (  "Source is not a directory: "  +  from  .  toString  (  )  )  ; 
if  (  !  from  .  canRead  (  )  )  abort  (  "Cannot read source: "  +  from  .  toString  (  )  )  ; 
if  (  !  to  .  exists  (  )  )  if  (  !  createDirectory  (  toPath  )  )  abort  (  "Failed creating destination directory: "  +  toPath  )  ; 
if  (  !  to  .  isDirectory  (  )  )  abort  (  "Destination is not a directory: "  +  to  .  toString  (  )  )  ; 
if  (  !  to  .  canWrite  (  )  )  abort  (  "Cannot write to destination: "  +  to  .  toString  (  )  )  ; 
files  =  from  .  list  (  )  ; 
if  (  files  !=  null  )  { 
for  (  int   fileCount  =  0  ;  fileCount  <  files  .  length  ;  fileCount  ++  )  { 
if  (  (  new   File  (  fromPath  +  files  [  fileCount  ]  )  )  .  isDirectory  (  )  )  copyDirectory  (  fromPath  +  files  [  fileCount  ]  ,  toPath  +  files  [  fileCount  ]  )  ;  else   copy  (  fromPath  +  files  [  fileCount  ]  ,  toPath  +  files  [  fileCount  ]  )  ; 
} 
} 
return   true  ; 
} 












public   static   boolean   copyDirectory  (  String   fromPath  ,  String   toPath  ,  boolean   makeRWX  )  throws   IOException  ,  InterruptedException  ,  SecurityException  { 
if  (  copyDirectory  (  fromPath  ,  toPath  )  &&  makeRWX  )  { 
if  (  !  toPath  .  endsWith  (  File  .  separator  )  )  toPath  =  toPath  +  File  .  separator  ; 
return   setDirectoryPermission  (  toPath  ,  PERMISSION_READ_WRITE_EXECUTE  ,  true  )  ; 
} 
return   true  ; 
} 

public   static   boolean   setDirectoryPermission  (  String   path  ,  int   permissionType  ,  boolean   isRecursive  )  throws   IOException  ,  InterruptedException  { 
StringBuilder   command  =  new   StringBuilder  (  ""  )  ; 
command  .  append  (  "chmod "  )  .  append  (  permissionType  )  .  append  (  " "  )  .  append  (  path  )  ; 
if  (  !  path  .  endsWith  (  File  .  separator  )  )  path  =  path  +  File  .  separator  ; 
File   dir  =  new   File  (  path  )  ; 
String  [  ]  files  ; 
if  (  !  dir  .  exists  (  )  )  abort  (  "Directory does not exist: "  +  dir  .  toString  (  )  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  abort  (  "Source is not a directory: "  +  dir  .  toString  (  )  )  ; 
files  =  dir  .  list  (  )  ; 
if  (  files  !=  null  )  { 
for  (  int   fileCount  =  0  ;  fileCount  <  files  .  length  ;  fileCount  ++  )  { 
if  (  (  new   File  (  path  +  files  [  fileCount  ]  )  )  .  isDirectory  (  )  )  { 
Process   permission  =  Runtime  .  getRuntime  (  )  .  exec  (  command  .  toString  (  )  )  ; 
permission  .  waitFor  (  )  ; 
setDirectoryPermission  (  path  +  files  [  fileCount  ]  ,  permissionType  ,  isRecursive  )  ; 
}  else  { 
setFilePermission  (  path  +  files  [  fileCount  ]  ,  permissionType  )  ; 
} 
} 
} 
return   true  ; 
} 

public   static   boolean   setFilePermission  (  String   path  ,  int   permissionType  )  throws   IOException  ,  InterruptedException  { 
File   f  =  new   File  (  path  )  ; 
if  (  !  f  .  isFile  (  )  )  { 
abort  (  "Not a file:"  +  f  .  toString  (  )  )  ; 
} 
if  (  !  System  .  getProperty  (  "os.name"  )  .  startsWith  (  "Windows"  )  )  { 
StringBuilder   command  =  new   StringBuilder  (  ""  )  ; 
command  .  append  (  "chmod "  )  .  append  (  permissionType  )  .  append  (  " "  )  .  append  (  path  )  ; 
Process   permission  =  Runtime  .  getRuntime  (  )  .  exec  (  command  .  toString  (  )  )  ; 
permission  .  waitFor  (  )  ; 
} 
return   true  ; 
} 
















public   static   boolean   makeZip  (  String   fromPath  ,  String   toFile  ,  boolean   isRecursive  )  throws   IOException  ,  FileNotFoundException  { 
File   dest  =  new   File  (  toFile  )  ; 
if  (  dest  .  isDirectory  (  )  )  { 
abort  (  "FileUtility: destination is a directory: "  +  toFile  )  ; 
return   false  ; 
} 
if  (  !  getFileNameExt  (  toFile  )  .  toLowerCase  (  )  .  equals  (  ".zip"  )  )  { 
toFile  =  toFile  .  substring  (  0  ,  toFile  .  lastIndexOf  (  "."  )  )  +  ".zip"  ; 
} 
FileUtility   fu  =  new   FileUtility  (  toFile  )  ; 
return   fu  .  makeZip  (  fromPath  ,  isRecursive  )  ; 
} 













public   boolean   makeZip  (  String   fromPath  ,  boolean   isRecursive  )  throws   IOException  ,  FileNotFoundException  { 
if  (  !  getFileNameExt  (  pathName  )  .  toLowerCase  (  )  .  equals  (  ".zip"  )  )  { 
abort  (  "FileUtility: the pathname extension has to be .zip"  +  pathName  )  ; 
} 
File   from  =  new   File  (  fromPath  )  ; 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   FileOutputStream  (  pathName  )  )  ; 
if  (  from  .  isFile  (  )  )  { 
addFileToZip  (  fromPath  ,  out  )  ; 
}  else  { 
if  (  !  fromPath  .  endsWith  (  File  .  separator  )  )  fromPath  =  fromPath  +  File  .  separator  ; 
String  [  ]  fileList  =  from  .  list  (  )  ; 
if  (  fileList  !=  null  )  { 
for  (  int   contentCount  =  0  ;  contentCount  <  fileList  .  length  ;  contentCount  ++  )  { 
if  (  (  new   File  (  fromPath  +  fileList  [  contentCount  ]  )  .  isDirectory  (  )  )  )  { 
if  (  isRecursive  )  { 
addDirectoryToZip  (  fromPath  +  fileList  [  contentCount  ]  ,  out  )  ; 
} 
}  else  { 
addFileToZip  (  fromPath  +  fileList  [  contentCount  ]  ,  out  )  ; 
} 
} 
} 
} 
out  .  finish  (  )  ; 
out  .  close  (  )  ; 
return   true  ; 
} 

private   void   addDirectoryToZip  (  String   fromPath  ,  ZipOutputStream   out  )  throws   IOException  { 
if  (  !  fromPath  .  endsWith  (  File  .  separator  )  )  fromPath  =  fromPath  +  File  .  separator  ; 
String  [  ]  fileList  =  (  new   File  (  fromPath  )  )  .  list  (  )  ; 
if  (  fileList  !=  null  )  { 
for  (  int   contentCount  =  0  ;  contentCount  <  fileList  .  length  ;  contentCount  ++  )  { 
if  (  (  new   File  (  fromPath  +  fileList  [  contentCount  ]  )  .  isDirectory  (  )  )  )  addDirectoryToZip  (  fromPath  +  fileList  [  contentCount  ]  ,  out  )  ;  else   addFileToZip  (  fromPath  +  fileList  [  contentCount  ]  ,  out  )  ; 
} 
} 
} 

private   void   addFileToZip  (  String   filePath  ,  ZipOutputStream   out  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   bytes_read  ; 
FileInputStream   in  =  new   FileInputStream  (  filePath  )  ; 
ZipEntry   zip  =  new   ZipEntry  (  (  new   File  (  filePath  )  )  .  getPath  (  )  )  ; 
out  .  putNextEntry  (  zip  )  ; 
while  (  (  bytes_read  =  in  .  read  (  buffer  )  )  !=  -  1  )  { 
out  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
in  .  close  (  )  ; 
} 









public   static   String   getName  (  String   path  )  { 
File   file  =  new   File  (  path  )  ; 
String   name  =  ""  ; 
if  (  file  .  isFile  (  )  )  { 
name  =  file  .  getName  (  )  ; 
name  =  name  .  substring  (  0  ,  name  .  lastIndexOf  (  "."  )  )  ; 
} 
return   name  ; 
} 








public   static   String   getExtension  (  String   path  )  { 
String   ext  =  ""  ; 
String   name  =  ""  ; 
File   file  =  new   File  (  path  )  ; 
if  (  file  .  isFile  (  )  )  { 
name  =  file  .  getName  (  )  ; 
ext  =  name  .  substring  (  name  .  lastIndexOf  (  "."  )  )  ; 
} 
return   ext  ; 
} 







public   static   String   getNameWithExt  (  String   path  )  { 
if  (  path  ==  null  )  { 
return   path  ; 
} 
int   pos  =  path  .  lastIndexOf  (  File  .  separator  )  ; 
if  (  pos  ==  -  1  ||  pos  ==  path  .  length  (  )  -  1  )  { 
return   path  ; 
} 
return   path  .  substring  (  pos  +  1  )  ; 
} 











public   static   void   appendText  (  String   content  ,  String   path  ,  boolean   append  )  throws   IOException  { 
FileOutputStream   fos  ; 
FileUtility   fu  =  new   FileUtility  (  path  )  ; 
byte  [  ]  buffer  ; 
int   bytes_read  ; 
if  (  fu  .  exists  (  )  &&  fu  .  isDirectory  (  )  )  { 
abort  (  "directory exists"  )  ; 
} 
if  (  !  fu  .  exists  (  )  ||  !  fu  .  isFile  (  )  )  { 
fos  =  new   FileOutputStream  (  path  )  ; 
buffer  =  content  .  getBytes  (  )  ; 
bytes_read  =  buffer  .  length  ; 
fos  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
return  ; 
} 
if  (  fu  .  exists  (  )  &&  fu  .  isFile  (  )  )  { 
String   originalContent  =  fu  .  getContent  (  )  ; 
StringBuilder   contentBuf  =  new   StringBuilder  (  )  ; 
if  (  append  )  { 
contentBuf  .  append  (  originalContent  )  .  append  (  "\n"  )  .  append  (  content  )  ; 
}  else  { 
contentBuf  .  append  (  content  )  .  append  (  "\n"  )  .  append  (  originalContent  )  ; 
} 
if  (  fu  .  remove  (  )  )  { 
fos  =  new   FileOutputStream  (  fu  )  ; 
buffer  =  contentBuf  .  toString  (  )  .  getBytes  (  )  ; 
bytes_read  =  buffer  .  length  ; 
fos  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
} 
} 


private   static   void   abort  (  String   msg  )  throws   IOException  { 
throw   new   IOException  (  msg  )  ; 
} 









public   static   String   getFileNameExt  (  String   path  )  { 
int   li  =  -  1  ; 
if  (  (  li  =  path  .  lastIndexOf  (  "."  )  )  !=  -  1  &&  path  .  length  (  )  >  li  )  { 
return   path  .  substring  (  li  +  1  )  ; 
}  else  { 
return  ""  ; 
} 
} 




private   static   long   lastModified  (  String   path  )  { 
FileUtility   fu  =  new   FileUtility  (  path  )  ; 
return   fu  .  lastModified  (  )  ; 
} 






public   static   URL   getFileUrl  (  String   fileName  )  { 
ClassLoader   loader  =  null  ; 
URL   url  =  null  ; 
loader  =  FileUtility  .  class  .  getClassLoader  (  )  ; 
url  =  loader  .  getResource  (  fileName  )  ; 
return   url  ; 
} 
} 

