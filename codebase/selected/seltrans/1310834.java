package   com  .  zhiyun  .  admin  .  common  .  utils  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  apache  .  tools  .  ant  .  Project  ; 
import   org  .  apache  .  tools  .  ant  .  taskdefs  .  Zip  ; 
import   org  .  apache  .  tools  .  ant  .  types  .  FileSet  ; 











public   class   ExFileUtils  { 















public   static   void   copyStream  (  OutputStream   outputStream  ,  InputStream   inputStream  )  { 
int   c  ; 
try  { 
while  (  (  c  =  inputStream  .  read  (  )  )  !=  -  1  )  outputStream  .  write  (  c  )  ; 
outputStream  .  flush  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 










public   static   String   createFolder  (  String   rootPath  ,  String   folderName  )  { 
try  { 
folderName  =  folderName  .  replace  (  "/"  ,  ""  )  ; 
rootPath  =  getRealFolderPath  (  rootPath  )  ; 
File   file  =  new   File  (  rootPath  )  ; 
if  (  !  file  .  exists  (  )  )  { 
String   newRootPath  =  rootPath  .  substring  (  0  ,  rootPath  .  length  (  )  -  1  )  ; 
createFolder  (  rootPath  .  substring  (  0  ,  newRootPath  .  lastIndexOf  (  "/"  )  )  ,  rootPath  .  substring  (  newRootPath  .  lastIndexOf  (  "/"  )  )  )  ; 
} 
file  =  new   File  (  rootPath  +  folderName  )  ; 
if  (  !  file  .  exists  (  )  )  file  .  mkdir  (  )  ; 
return   rootPath  +  folderName  +  (  folderName  .  equals  (  ""  )  ?  ""  :  "/"  )  ; 
}  catch  (  RuntimeException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return  ""  ; 
} 








public   static   String   createFolder  (  String   folderPath  )  { 
return   createFolder  (  folderPath  ,  ""  )  ; 
} 







public   static   String   getParentDirectory  (  String   folderPath  )  { 
File   file  =  new   File  (  getRealFolderPath  (  folderPath  )  )  ; 
file  =  file  .  getParentFile  (  )  ==  null  ?  file  :  file  .  getParentFile  (  )  ; 
return   getRealFolderPath  (  file  .  getPath  (  )  )  ; 
} 







public   static   String   getParentDirectory  (  String   folderPath  ,  int   n  )  { 
File   file  =  new   File  (  getRealFolderPath  (  folderPath  )  )  ; 
while  (  n  --  >  0  )  { 
file  =  file  .  getParentFile  (  )  ==  null  ?  file  :  file  .  getParentFile  (  )  ; 
} 
return   getRealFolderPath  (  file  .  getPath  (  )  )  ; 
} 







public   static   void   deleteFile  (  String   filePath  )  { 
try  { 
File   f  =  new   File  (  filePath  )  ; 
if  (  f  .  exists  (  )  &&  f  .  isDirectory  (  )  )  { 
if  (  f  .  listFiles  (  )  .  length  ==  0  )  { 
f  .  delete  (  )  ; 
}  else  { 
File   delFile  [  ]  =  f  .  listFiles  (  )  ; 
int   i  =  f  .  listFiles  (  )  .  length  ; 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
if  (  delFile  [  j  ]  .  isDirectory  (  )  )  { 
deleteFile  (  delFile  [  j  ]  .  getAbsolutePath  (  )  )  ; 
} 
delFile  [  j  ]  .  delete  (  )  ; 
} 
} 
deleteFile  (  filePath  )  ; 
}  else   if  (  f  .  exists  (  )  )  { 
f  .  delete  (  )  ; 
} 
}  catch  (  RuntimeException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 









public   static   File  [  ]  browseFolder  (  String   folderPath  ,  FileFilter   fileFilter  )  { 
try  { 
File   f  =  new   File  (  folderPath  )  ; 
if  (  f  .  exists  (  )  &&  f  .  isDirectory  (  )  )  { 
if  (  fileFilter  !=  null  )  return   f  .  listFiles  (  fileFilter  )  ; 
} 
}  catch  (  RuntimeException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   new   File  [  ]  {  }  ; 
} 









public   static   File  [  ]  browseFolder  (  File   f  ,  FileFilter   fileFilter  )  { 
try  { 
if  (  f  .  exists  (  )  &&  f  .  isDirectory  (  )  )  { 
if  (  fileFilter  !=  null  )  return   f  .  listFiles  (  fileFilter  )  ; 
} 
}  catch  (  RuntimeException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   new   File  [  ]  {  }  ; 
} 








private   static   String   getRealFolderPath  (  String   path  )  { 
path  =  path  .  replace  (  "\\"  ,  "/"  )  ; 
while  (  path  .  contains  (  "//"  )  )  { 
path  =  path  .  replace  (  "//"  ,  "/"  )  ; 
} 
if  (  !  path  .  endsWith  (  "/"  )  )  path  +=  "/"  ; 
return   path  ; 
} 








public   static   String   getRealFileName  (  String   path  )  { 
path  =  path  .  replace  (  "\\"  ,  "/"  )  ; 
while  (  path  .  contains  (  "//"  )  )  { 
path  =  path  .  replace  (  "//"  ,  "/"  )  ; 
} 
return   path  ; 
} 






public   static   String   getExtendName  (  String   fileName  )  { 
return   fileName  .  substring  (  fileName  .  lastIndexOf  (  "."  )  +  1  ,  fileName  .  length  (  )  )  ; 
} 






public   static   String   getExtendName  (  File   file  )  { 
if  (  file  .  isDirectory  (  )  )  return  "folder"  ; 
return   getExtendName  (  file  .  getName  (  )  )  ; 
} 












public   static   void   appendContentToFile  (  String   content  ,  String   rootPath  ,  String   fileName  )  { 
String   str  =  content  ; 
try  { 
appendContentToFile  (  str  .  getBytes  (  "UTF-8"  )  ,  rootPath  ,  fileName  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 












public   static   void   createFile  (  String   content  ,  String   rootPath  ,  String   fileName  )  { 
try  { 
createFile  (  content  .  getBytes  (  "UTF-8"  )  ,  rootPath  ,  fileName  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 











public   static   void   createFile  (  String   content  ,  String   fileFullName  )  { 
try  { 
createFile  (  content  .  getBytes  (  "UTF-8"  )  ,  fileFullName  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 










public   static   String   readCharsFile  (  String   rootPath  ,  String   fileName  )  { 
try  { 
return   new   String  (  readBytesFile  (  rootPath  ,  fileName  )  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return  ""  ; 
} 








public   static   String   readCharsFile  (  String   fileFullName  )  { 
try  { 
return   new   String  (  readBytesFile  (  fileFullName  )  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return  ""  ; 
} 












public   static   void   appendContentToFile  (  byte  [  ]  content  ,  String   rootPath  ,  String   fileName  )  { 
File   file  =  null  ; 
RandomAccessFile   randomAccessFile  =  null  ; 
try  { 
fileName  =  fileName  .  replace  (  "/"  ,  ""  )  ; 
rootPath  =  getRealFolderPath  (  rootPath  )  ; 
createFolder  (  rootPath  )  ; 
String   filePath  =  rootPath  +  fileName  ; 
file  =  new   File  (  filePath  )  ; 
randomAccessFile  =  new   RandomAccessFile  (  filePath  ,  "rw"  )  ; 
if  (  content  !=  null  )  { 
if  (  file  .  exists  (  )  )  { 
randomAccessFile  .  seek  (  randomAccessFile  .  length  (  )  )  ; 
}  else  { 
randomAccessFile  .  setLength  (  0  )  ; 
} 
randomAccessFile  .  write  (  content  )  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  randomAccessFile  !=  null  )  { 
try  { 
randomAccessFile  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
} 
} 
} 












public   static   void   createFile  (  byte  [  ]  content  ,  String   rootPath  ,  String   fileName  )  { 
RandomAccessFile   randomAccessFile  =  null  ; 
try  { 
fileName  =  fileName  .  replace  (  "/"  ,  ""  )  ; 
rootPath  =  getRealFolderPath  (  rootPath  )  ; 
createFolder  (  rootPath  )  ; 
String   filePath  =  rootPath  +  fileName  ; 
deleteFile  (  filePath  )  ; 
randomAccessFile  =  new   RandomAccessFile  (  filePath  ,  "rw"  )  ; 
randomAccessFile  .  setLength  (  0  )  ; 
if  (  content  !=  null  )  { 
randomAccessFile  .  write  (  content  )  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  randomAccessFile  !=  null  )  { 
try  { 
randomAccessFile  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
} 
} 
} 











public   static   void   createFile  (  byte  [  ]  content  ,  String   fileFullName  )  { 
fileFullName  =  getRealFileName  (  fileFullName  )  ; 
String   rootPath  =  fileFullName  .  substring  (  0  ,  fileFullName  .  lastIndexOf  (  '/'  )  )  ; 
fileFullName  =  fileFullName  .  substring  (  fileFullName  .  lastIndexOf  (  '/'  )  )  ; 
createFile  (  content  ,  rootPath  ,  fileFullName  )  ; 
} 










public   static   byte  [  ]  readBytesFile  (  String   rootPath  ,  String   fileName  )  { 
File   file  =  null  ; 
FileInputStream   fis  =  null  ; 
try  { 
fileName  =  fileName  .  replace  (  "/"  ,  ""  )  ; 
rootPath  =  getRealFolderPath  (  rootPath  )  ; 
String   filePath  =  rootPath  +  fileName  ; 
file  =  new   File  (  filePath  )  ; 
if  (  file  .  exists  (  )  )  { 
fis  =  new   FileInputStream  (  file  )  ; 
byte  [  ]  bytes  =  readInputStream  (  fis  )  ; 
return   bytes  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  fis  !=  null  )  { 
try  { 
fis  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
return   new   byte  [  0  ]  ; 
} 







public   static   byte  [  ]  readInputStream  (  InputStream   inputStream  )  { 
List  <  Byte  >  bytes  =  new   ArrayList  <  Byte  >  (  )  ; 
try  { 
int   c  ; 
while  (  (  c  =  inputStream  .  read  (  )  )  !=  -  1  )  { 
bytes  .  add  (  (  byte  )  c  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
byte  [  ]  bytesResult  =  new   byte  [  bytes  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  bytesResult  .  length  ;  i  ++  )  { 
bytesResult  [  i  ]  =  bytes  .  get  (  i  )  ; 
} 
return   bytesResult  ; 
} 









public   static   byte  [  ]  readBytesFile  (  String   fileFullName  )  { 
File   file  =  null  ; 
FileInputStream   fis  =  null  ; 
try  { 
fileFullName  =  getRealFileName  (  fileFullName  )  ; 
file  =  new   File  (  fileFullName  )  ; 
if  (  file  .  exists  (  )  )  { 
fis  =  new   FileInputStream  (  file  )  ; 
byte  [  ]  bytes  =  readInputStream  (  fis  )  ; 
return   bytes  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  fis  !=  null  )  { 
try  { 
fis  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
return   new   byte  [  0  ]  ; 
} 

public   static   void   createZipAnt  (  String   dirPath  ,  String   outPath  )  { 
File   infile  =  new   File  (  dirPath  )  ; 
File   outFile  =  new   File  (  outPath  )  ; 
Project   prj  =  new   Project  (  )  ; 
Zip   zip  =  new   Zip  (  )  ; 
zip  .  setProject  (  prj  )  ; 
zip  .  setDestFile  (  outFile  )  ; 
FileSet   fileSet  =  new   FileSet  (  )  ; 
fileSet  .  setProject  (  prj  )  ; 
fileSet  .  setDir  (  infile  )  ; 
zip  .  addFileset  (  fileSet  )  ; 
zip  .  execute  (  )  ; 
deleteFile  (  dirPath  )  ; 
} 

public   static   void   fileRead  (  File   src  ,  File   dest  )  { 
if  (  null  !=  src  &&  null  !=  dest  )  { 
InputStream   is  =  null  ; 
OutputStream   os  =  null  ; 
try  { 
int   length  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
is  =  new   FileInputStream  (  src  )  ; 
os  =  new   FileOutputStream  (  dest  )  ; 
while  (  (  length  =  is  .  read  (  buffer  )  )  >  0  )  { 
os  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
if  (  is  !=  null  )  { 
is  .  close  (  )  ; 
} 
if  (  os  !=  null  )  { 
os  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 

public   static   void   deleteFilesFromDir  (  String   name  ,  String   dirName  )  { 
File   dir  =  new   File  (  dirName  )  ; 
File  [  ]  files  =  dir  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
if  (  file  .  getName  (  )  .  contains  (  name  )  )  { 
file  .  delete  (  )  ; 
} 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
System  .  out  .  println  (  readCharsFile  (  "E:\\xvxv\\Function\\dataaccess\\dataaccess\\src\\main\\java\\cn\\rtdata\\dataaccess\\util\\file\\FileTools.java"  )  )  ; 
} 
} 

