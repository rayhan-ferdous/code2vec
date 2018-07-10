package   com  .  nhncorp  .  usf  .  core  .  util  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   org  .  apache  .  commons  .  io  .  FileUtils  ; 
import   org  .  apache  .  commons  .  io  .  FilenameUtils  ; 
import   org  .  apache  .  commons  .  io  .  IOUtils  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   com  .  nhncorp  .  lucy  .  common  .  config  .  model  .  ApplicationInfo  ; 






public   class   FileUtil  { 

private   static   Log   log  =  LogFactory  .  getLog  (  FileUtil  .  class  )  ; 

public   static   final   char   NON_EUC_KR_CHAR  =  65533  ; 

private   static   String   encoding  ; 








public   static   String   getCorrectedFileName  (  String   path  )  { 
if  (  path  ==  null  )  { 
return   null  ; 
} 
String   reserved  =  ";|/|\\?|:|@|&|=|\\+|\\s"  ; 
String   regEx  =  reserved  ; 
if  (  encoding  ==  null  )  { 
encoding  =  ApplicationInfo  .  getPageEncoding  (  )  ; 
} 
boolean   isMS949  =  "MS949"  .  equalsIgnoreCase  (  encoding  )  ; 
if  (  isMS949  )  { 
try  { 
path  =  new   String  (  path  .  getBytes  (  encoding  )  ,  "EUC-KR"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
path  =  "myfile"  ; 
} 
regEx  +=  "|"  +  Character  .  toString  (  NON_EUC_KR_CHAR  )  ; 
} 
return  "myfile"  .  equals  (  path  )  ?  path  :  path  .  replaceAll  (  regEx  ,  "_"  )  ; 
} 








public   static   String   changeExtension  (  String   filename  ,  String   extension  )  { 
return   FilenameUtils  .  removeExtension  (  filename  )  +  "."  +  extension  ; 
} 









public   static   void   overwrite  (  File   srcFile  ,  File   destFile  )  throws   IOException  { 
FileUtils  .  copyFile  (  srcFile  ,  destFile  )  ; 
} 








public   static   byte  [  ]  readFileToByteArray  (  String   path  )  throws   IOException  { 
return   FileUtils  .  readFileToByteArray  (  new   File  (  path  )  )  ; 
} 












public   static   String   getFileName  (  String   path  )  { 
return   FilenameUtils  .  getName  (  path  )  ; 
} 





public   static   String   getFileSeparator  (  )  { 
return   File  .  separator  ; 
} 












public   static   File   getUniqueFile  (  File   file  )  { 
String   parent  =  file  .  getParent  (  )  ; 
String   name  =  file  .  getName  (  )  ; 
String   baseName  =  FilenameUtils  .  getBaseName  (  name  )  ; 
String   extension  =  FilenameUtils  .  getExtension  (  name  )  ; 
File   uniqueFile  =  file  ; 
int   i  =  1  ; 
while  (  uniqueFile  .  exists  (  )  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
if  (  parent  !=  null  )  { 
sb  .  append  (  parent  )  .  append  (  File  .  separator  )  ; 
} 
sb  .  append  (  baseName  )  .  append  (  '('  )  .  append  (  i  ++  )  .  append  (  ')'  )  ; 
if  (  !  StringUtils  .  isEmpty  (  extension  )  )  { 
sb  .  append  (  '.'  )  .  append  (  extension  )  ; 
} 
uniqueFile  =  new   File  (  sb  .  toString  (  )  )  ; 
} 
return   uniqueFile  ; 
} 












public   static   int   save  (  byte  [  ]  bytes  ,  String   repository  ,  String   fileName  )  throws   IOException  { 
File   outFile  =  new   File  (  FilenameUtils  .  concat  (  repository  ,  fileName  )  )  ; 
return   save  (  bytes  ,  outFile  )  ; 
} 










public   static   int   save  (  byte  [  ]  bytes  ,  File   outputFile  )  throws   IOException  { 
InputStream   in  =  new   ByteArrayInputStream  (  bytes  )  ; 
outputFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
OutputStream   out  =  new   FileOutputStream  (  outputFile  )  ; 
try  { 
return   IOUtils  .  copy  (  in  ,  out  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
IOUtils  .  closeQuietly  (  out  )  ; 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
} 
} 










public   static   int   save  (  File   inputFile  ,  File   outputFile  )  throws   IOException  { 
InputStream   in  =  null  ; 
OutputStream   out  =  null  ; 
try  { 
in  =  new   FileInputStream  (  inputFile  )  ; 
outputFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
out  =  new   FileOutputStream  (  outputFile  )  ; 
}  catch  (  Exception   e  )  { 
e  .  getMessage  (  )  ; 
} 
try  { 
return   IOUtils  .  copy  (  in  ,  out  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
IOUtils  .  closeQuietly  (  out  )  ; 
try  { 
if  (  out  !=  null  )  { 
out  .  close  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
} 
} 











public   static   File   saveToUniqueFileName  (  byte  [  ]  bytes  ,  File   outputFile  )  throws   IOException  { 
outputFile  =  getUniqueFile  (  outputFile  )  ; 
save  (  bytes  ,  outputFile  )  ; 
return   outputFile  ; 
} 











public   static   File   saveToUniqueFileName  (  File   inputFile  ,  File   outputFile  )  throws   IOException  { 
outputFile  =  getUniqueFile  (  outputFile  )  ; 
save  (  inputFile  ,  outputFile  )  ; 
return   outputFile  ; 
} 











public   static   String   copy  (  File   srcFile  ,  File   destFile  )  throws   IOException  { 
destFile  =  getUniqueFile  (  destFile  )  ; 
FileUtils  .  copyFile  (  srcFile  ,  destFile  ,  true  )  ; 
return   destFile  .  getAbsolutePath  (  )  ; 
} 











public   static   String   copy  (  String   srcPath  ,  File   destDir  )  throws   IOException  { 
File   srcFile  =  new   File  (  srcPath  )  ; 
File   destFile  =  new   File  (  destDir  ,  FilenameUtils  .  getName  (  srcPath  )  )  ; 
return   copy  (  srcFile  ,  destFile  )  ; 
} 











public   static   String  [  ]  copy  (  String  [  ]  srcPaths  ,  String   destDirPath  )  throws   IOException  { 
String  [  ]  destPaths  =  new   String  [  srcPaths  .  length  ]  ; 
File   destDir  =  new   File  (  destDirPath  )  ; 
for  (  int   i  =  0  ;  i  <  srcPaths  .  length  ;  i  ++  )  { 
destPaths  [  i  ]  =  copy  (  srcPaths  [  i  ]  ,  destDir  )  ; 
} 
return   destPaths  ; 
} 












public   static   String   move  (  File   srcFile  ,  File   destFile  ,  boolean   overwrite  )  { 
if  (  overwrite  )  { 
destFile  .  delete  (  )  ; 
}  else  { 
destFile  =  getUniqueFile  (  destFile  )  ; 
} 
destFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
boolean   success  =  srcFile  .  renameTo  (  destFile  )  ; 
if  (  !  success  )  { 
try  { 
copy  (  srcFile  ,  destFile  )  ; 
deleteFile  (  srcFile  .  getAbsolutePath  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
} 
return   destFile  .  getAbsolutePath  (  )  ; 
} 












public   static   String   move  (  String   srcFile  ,  String   destFile  ,  boolean   overwrite  )  { 
return   move  (  new   File  (  srcFile  )  ,  new   File  (  destFile  )  ,  overwrite  )  ; 
} 









public   static   String   move  (  String   srcPath  ,  String   destPath  )  { 
return   move  (  srcPath  ,  destPath  ,  false  )  ; 
} 









public   static   String   move  (  File   srcFile  ,  File   destFile  )  { 
return   move  (  srcFile  ,  destFile  ,  false  )  ; 
} 












public   static   String   moveToDir  (  String   srcPath  ,  String   destDirPath  ,  boolean   overwrite  )  { 
File   srcFile  =  new   File  (  srcPath  )  ; 
File   destFile  =  new   File  (  destDirPath  ,  FilenameUtils  .  getName  (  srcPath  )  )  ; 
return   move  (  srcFile  ,  destFile  ,  overwrite  )  ; 
} 












public   static   String   moveToDir  (  String   srcPath  ,  File   destDir  ,  boolean   overwrite  )  { 
File   srcFile  =  new   File  (  srcPath  )  ; 
File   destFile  =  new   File  (  destDir  ,  FilenameUtils  .  getName  (  srcPath  )  )  ; 
return   move  (  srcFile  ,  destFile  ,  overwrite  )  ; 
} 










public   static   String   moveToDir  (  String   srcPath  ,  File   destDir  )  { 
return   moveToDir  (  srcPath  ,  destDir  ,  false  )  ; 
} 











public   static   String   moveToDir  (  String   srcPath  ,  String   destDirPath  )  { 
return   moveToDir  (  srcPath  ,  destDirPath  ,  false  )  ; 
} 












public   static   String  [  ]  moveToDir  (  String  [  ]  srcPaths  ,  String   destDirPath  ,  boolean   overwrite  )  { 
String  [  ]  destPaths  =  new   String  [  srcPaths  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  srcPaths  .  length  ;  i  ++  )  { 
destPaths  [  i  ]  =  moveToDir  (  srcPaths  [  i  ]  ,  destDirPath  ,  overwrite  )  ; 
} 
return   destPaths  ; 
} 











public   static   String  [  ]  moveToDir  (  String  [  ]  srcPaths  ,  String   destDirPath  )  { 
return   moveToDir  (  srcPaths  ,  destDirPath  ,  false  )  ; 
} 









public   static   long   deleteFile  (  String   path  )  throws   IOException  { 
long   size  =  0  ; 
File   deleteFile  =  new   File  (  path  )  ; 
if  (  deleteFile  .  isDirectory  (  )  )  { 
log  .  error  (  "File("  +  path  +  ") is directory. Use method named deleteDirectory."  )  ; 
}  else  { 
size  =  deleteFile  .  length  (  )  ; 
FileUtils  .  forceDelete  (  deleteFile  )  ; 
log  .  info  (  "File("  +  path  +  ") is deleted..."  )  ; 
} 
return   size  ; 
} 








public   static   long   deleteFiles  (  String  [  ]  paths  )  throws   IOException  { 
long   size  =  0  ; 
for  (  String   path  :  paths  )  { 
size  +=  deleteFile  (  path  )  ; 
} 
return   size  ; 
} 








public   static   long   deleteDirectory  (  String  [  ]  dirPaths  )  throws   IOException  { 
long   size  =  0  ; 
for  (  String   dirPath  :  dirPaths  )  { 
File   dir  =  new   File  (  dirPath  )  ; 
size  +=  FileUtils  .  sizeOfDirectory  (  dir  )  ; 
FileUtils  .  deleteDirectory  (  dir  )  ; 
log  .  info  (  "Directory("  +  dirPath  +  ") is deleted..."  )  ; 
} 
return   size  ; 
} 







public   static   boolean   fileExists  (  File   file  )  { 
return  (  file  .  exists  (  )  &&  file  .  isFile  (  )  )  ; 
} 







public   static   boolean   fileExists  (  String   path  )  { 
return   fileExists  (  new   File  (  path  )  )  ; 
} 







public   static   boolean   isFile  (  File   file  )  { 
return   file  .  isFile  (  )  ; 
} 






public   static   void   mkdirs  (  File   dir  )  { 
dir  .  mkdirs  (  )  ; 
} 






public   static   void   mkdirs  (  String   dirPath  )  { 
mkdirs  (  new   File  (  dirPath  )  )  ; 
} 







public   static   File   getParentFile  (  File   file  )  { 
return   file  .  getParentFile  (  )  ; 
} 







public   static   boolean   exists  (  File   file  )  { 
return   file  .  exists  (  )  ; 
} 







public   static   boolean   exists  (  String   path  )  { 
return   new   File  (  path  )  .  exists  (  )  ; 
} 








public   static   List  <  String  >  extract  (  String   zipFilePath  ,  String   destDirPath  )  throws   IOException  { 
List  <  String  >  list  =  null  ; 
ZipFile   zip  =  new   ZipFile  (  zipFilePath  )  ; 
try  { 
Enumeration  <  ?  extends   ZipEntry  >  entries  =  zip  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
ZipEntry   entry  =  entries  .  nextElement  (  )  ; 
File   destFile  =  new   File  (  destDirPath  ,  entry  .  getName  (  )  )  ; 
if  (  entry  .  isDirectory  (  )  )  { 
destFile  .  mkdirs  (  )  ; 
}  else  { 
InputStream   in  =  zip  .  getInputStream  (  entry  )  ; 
OutputStream   out  =  new   FileOutputStream  (  destFile  )  ; 
try  { 
IOUtils  .  copy  (  in  ,  out  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
IOUtils  .  closeQuietly  (  out  )  ; 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  getMessage  (  )  ; 
} 
} 
} 
if  (  list  ==  null  )  { 
list  =  new   ArrayList  <  String  >  (  )  ; 
} 
list  .  add  (  destFile  .  getAbsolutePath  (  )  )  ; 
} 
return   list  ; 
}  finally  { 
try  { 
zip  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  getMessage  (  )  ; 
} 
} 
} 
} 

