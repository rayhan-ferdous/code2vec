package   Utilities  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 





public   class   FileUtils  { 







public   static   void   zipFolder  (  String   srcFolder  ,  String   destZipFile  )  throws   Exception  { 
ZipOutputStream   zip  =  null  ; 
FileOutputStream   fileWriter  =  null  ; 
fileWriter  =  new   FileOutputStream  (  destZipFile  )  ; 
zip  =  new   ZipOutputStream  (  fileWriter  )  ; 
addFolderToZip  (  ""  ,  srcFolder  ,  zip  )  ; 
zip  .  flush  (  )  ; 
zip  .  close  (  )  ; 
} 








private   static   void   addFolderToZip  (  String   path  ,  String   srcFolder  ,  ZipOutputStream   zip  )  throws   Exception  { 
File   folder  =  new   File  (  srcFolder  )  ; 
for  (  String   fileName  :  folder  .  list  (  )  )  { 
if  (  path  .  equals  (  ""  )  )  { 
addFileToZip  (  folder  .  getName  (  )  ,  srcFolder  +  "/"  +  fileName  ,  zip  )  ; 
}  else  { 
addFileToZip  (  path  +  "/"  +  folder  .  getName  (  )  ,  srcFolder  +  "/"  +  fileName  ,  zip  )  ; 
} 
} 
} 









private   static   void   addFileToZip  (  String   path  ,  String   srcFile  ,  ZipOutputStream   zip  )  throws   Exception  { 
File   folder  =  new   File  (  srcFile  )  ; 
if  (  folder  .  isDirectory  (  )  )  { 
addFolderToZip  (  path  ,  srcFile  ,  zip  )  ; 
}  else  { 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
int   len  ; 
FileInputStream   in  =  new   FileInputStream  (  srcFile  )  ; 
zip  .  putNextEntry  (  new   ZipEntry  (  path  +  "/"  +  folder  .  getName  (  )  )  )  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
zip  .  write  (  buf  ,  0  ,  len  )  ; 
} 
in  .  close  (  )  ; 
} 
} 








public   static   void   deleteDir  (  File   file  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
if  (  file  .  list  (  )  .  length  ==  0  )  { 
file  .  delete  (  )  ; 
}  else  { 
String   files  [  ]  =  file  .  list  (  )  ; 
for  (  String   temp  :  files  )  { 
File   fileDelete  =  new   File  (  file  ,  temp  )  ; 
deleteDir  (  fileDelete  )  ; 
} 
if  (  file  .  list  (  )  .  length  ==  0  )  { 
file  .  delete  (  )  ; 
} 
} 
}  else  { 
file  .  delete  (  )  ; 
} 
} 






public   static   String   getFileNameWithoutExt  (  String   fileName  )  { 
File   file  =  new   File  (  fileName  )  ; 
int   index  =  file  .  getName  (  )  .  lastIndexOf  (  '.'  )  ; 
if  (  index  >  0  &&  index  <=  file  .  getName  (  )  .  length  (  )  -  2  )  { 
return   file  .  getName  (  )  .  substring  (  0  ,  index  )  ; 
} 
return  ""  ; 
} 






public   static   String   getFileExtension  (  File   file  )  { 
String   ext  =  null  ; 
String   s  =  file  .  getName  (  )  ; 
int   i  =  s  .  lastIndexOf  (  "."  )  ; 
if  (  i  >  0  &&  i  <  s  .  length  (  )  -  1  )  { 
ext  =  s  .  substring  (  i  +  1  )  .  toLowerCase  (  )  ; 
} 
return   ext  ; 
} 






public   static   String   getProjectNameFromPiqFile  (  String   fileName  )  { 
return   getFileNameWithoutExt  (  fileName  )  .  replaceAll  (  "all_"  ,  ""  )  ; 
} 







public   static   void   copyFile  (  File   sourceFile  ,  File   destFile  )  throws   IOException  { 
if  (  !  destFile  .  exists  (  )  )  { 
destFile  .  createNewFile  (  )  ; 
} 
FileChannel   source  =  null  ; 
FileChannel   destination  =  null  ; 
try  { 
source  =  new   FileInputStream  (  sourceFile  )  .  getChannel  (  )  ; 
destination  =  new   FileOutputStream  (  destFile  )  .  getChannel  (  )  ; 
destination  .  transferFrom  (  source  ,  0  ,  source  .  size  (  )  )  ; 
}  finally  { 
if  (  source  !=  null  )  { 
source  .  close  (  )  ; 
} 
if  (  destination  !=  null  )  { 
destination  .  close  (  )  ; 
} 
} 
} 

public   static   void   copyFolder  (  File   src  ,  File   dest  )  throws   IOException  { 
if  (  src  .  isDirectory  (  )  )  { 
if  (  !  dest  .  exists  (  )  )  { 
dest  .  mkdir  (  )  ; 
System  .  out  .  println  (  "Directory copied from "  +  src  +  "  to "  +  dest  )  ; 
} 
String   files  [  ]  =  src  .  list  (  )  ; 
for  (  String   file  :  files  )  { 
File   srcFile  =  new   File  (  src  ,  file  )  ; 
File   destFile  =  new   File  (  dest  ,  file  )  ; 
copyFolder  (  srcFile  ,  destFile  )  ; 
} 
}  else  { 
InputStream   in  =  new   FileInputStream  (  src  )  ; 
OutputStream   out  =  new   FileOutputStream  (  dest  )  ; 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   length  ; 
while  (  (  length  =  in  .  read  (  buffer  )  )  >  0  )  { 
out  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
System  .  out  .  println  (  "File copied from "  +  src  +  " to "  +  dest  )  ; 
} 
} 
} 

