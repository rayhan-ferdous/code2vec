package   org  .  xaware  .  shared  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 

public   class   ZipFileUtils  { 




private   ZipFileUtils  (  )  { 
} 

public   static   File   extractZipEntry  (  final   String   zipFilename  ,  final   String   locationWithinZip  ,  final   File   destinationDir  )  throws   IOException  { 
ZipFile   zipFile  =  null  ; 
try  { 
zipFile  =  new   ZipFile  (  zipFilename  )  ; 
ZipEntry   zipEntry  =  getZipEntry  (  zipFile  ,  locationWithinZip  )  ; 
InputStream   is  =  zipFile  .  getInputStream  (  zipEntry  )  ; 
File   destinationFile  =  new   File  (  destinationDir  ,  zipEntry  .  getName  (  )  )  ; 
if  (  destinationFile  .  exists  (  )  )  { 
destinationFile  .  delete  (  )  ; 
}  else  { 
File   parentDir  =  destinationFile  .  getParentFile  (  )  ; 
if  (  (  parentDir  !=  null  )  &&  (  !  parentDir  .  exists  (  )  )  )  { 
destinationFile  .  mkdirs  (  )  ; 
} 
} 
FileUtils  .  copyFile  (  is  ,  destinationFile  )  ; 
return   destinationFile  ; 
}  finally  { 
try  { 
if  (  zipFile  !=  null  )  { 
zipFile  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
} 

public   static   boolean   zipContainsEntry  (  final   String   zipFilename  ,  final   String   locationWithinZip  )  { 
ZipFile   zipFile  =  null  ; 
try  { 
zipFile  =  new   ZipFile  (  zipFilename  )  ; 
getZipEntry  (  zipFile  ,  locationWithinZip  )  ; 
return   true  ; 
}  catch  (  IOException   e  )  { 
return   false  ; 
}  finally  { 
try  { 
if  (  zipFile  !=  null  )  { 
zipFile  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
} 








private   static   ZipEntry   getZipEntry  (  ZipFile   zipFile  ,  final   String   locationWithinZip  )  throws   FileNotFoundException  { 
String   entryName  ; 
if  (  File  .  separatorChar  ==  '/'  )  { 
entryName  =  locationWithinZip  .  replace  (  '\\'  ,  File  .  separatorChar  )  ; 
}  else  { 
entryName  =  locationWithinZip  .  replace  (  '/'  ,  File  .  separatorChar  )  ; 
} 
ZipEntry   zipEntry  =  zipFile  .  getEntry  (  entryName  )  ; 
if  (  zipEntry  ==  null  )  { 
throw   new   FileNotFoundException  (  "Failed to find entry "  +  entryName  +  " within "  +  zipFile  .  getName  (  )  )  ; 
} 
return   zipEntry  ; 
} 















public   static   void   addFileToZipFile  (  final   String   zipFileName  ,  final   String   nameOfFileToAdd  ,  final   String   pathToFileInZipFile  )  throws   IOException  { 
final   File   zipFile  =  new   File  (  zipFileName  )  ; 
final   String   oldZipFileName  =  zipFileName  +  ".old"  ; 
final   File   oldZipFile  =  new   File  (  oldZipFileName  )  ; 
renameFile  (  zipFile  ,  oldZipFile  )  ; 
final   File   newZipFile  =  new   File  (  zipFileName  )  ; 
boolean   copyCompleted  =  false  ; 
boolean   fileClosingCompleted  =  false  ; 
try  { 
ZipFile   fromZip  =  null  ; 
ZipOutputStream   toZipOutputStream  =  null  ; 
try  { 
fromZip  =  new   ZipFile  (  oldZipFile  )  ; 
toZipOutputStream  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  newZipFile  )  )  )  ; 
final   byte  [  ]  copyBuffer  =  new   byte  [  10240  ]  ; 
addFileToZip  (  nameOfFileToAdd  ,  pathToFileInZipFile  ,  toZipOutputStream  ,  copyBuffer  )  ; 
copyExistingZipFiles  (  fromZip  ,  toZipOutputStream  ,  pathToFileInZipFile  ,  copyBuffer  )  ; 
copyCompleted  =  true  ; 
}  finally  { 
if  (  fromZip  !=  null  )  { 
fromZip  .  close  (  )  ; 
fromZip  =  null  ; 
} 
if  (  toZipOutputStream  !=  null  )  { 
toZipOutputStream  .  close  (  )  ; 
toZipOutputStream  =  null  ; 
} 
fileClosingCompleted  =  true  ; 
} 
}  finally  { 
if  (  copyCompleted  &&  fileClosingCompleted  )  { 
deleteFile  (  oldZipFile  )  ; 
}  else  { 
try  { 
deleteFile  (  newZipFile  )  ; 
renameFile  (  oldZipFile  ,  zipFile  )  ; 
}  catch  (  final   IOException   e  )  { 
; 
} 
} 
} 
} 











private   static   void   renameFile  (  final   File   fromFile  ,  final   File   toFile  )  throws   IOException  { 
for  (  int   i  =  0  ;  i  <  5  ;  i  ++  )  { 
final   boolean   renameSuccessful  =  fromFile  .  renameTo  (  toFile  )  ; 
if  (  renameSuccessful  )  { 
return  ; 
} 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  final   InterruptedException   e  )  { 
; 
} 
} 
throw   new   IOException  (  "Failed to rename archive file.  "  +  "Please make sure that the file is not open "  +  "in some other application."  )  ; 
} 









private   static   void   deleteFile  (  final   File   file  )  throws   IOException  { 
for  (  int   i  =  0  ;  i  <  5  ;  i  ++  )  { 
final   boolean   deleteSuccessful  =  file  .  delete  (  )  ; 
if  (  deleteSuccessful  )  { 
return  ; 
} 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  final   InterruptedException   e  )  { 
; 
} 
} 
throw   new   IOException  (  "Failed to delete file: "  +  file  .  getName  (  )  )  ; 
} 















private   static   void   addFileToZip  (  final   String   fileName  ,  final   String   pathToFileInZipFile  ,  final   ZipOutputStream   zipOutStream  ,  final   byte  [  ]  copyBuffer  )  throws   IOException  { 
BufferedInputStream   inStream  =  null  ; 
try  { 
inStream  =  new   BufferedInputStream  (  new   FileInputStream  (  fileName  )  )  ; 
final   ZipEntry   entry  =  new   ZipEntry  (  pathToFileInZipFile  )  ; 
zipOutStream  .  putNextEntry  (  entry  )  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  inStream  .  read  (  copyBuffer  )  )  !=  -  1  )  { 
zipOutStream  .  write  (  copyBuffer  ,  0  ,  bytesRead  )  ; 
} 
}  finally  { 
if  (  inStream  !=  null  )  { 
try  { 
inStream  .  close  (  )  ; 
}  catch  (  final   IOException   e  )  { 
; 
} 
} 
} 
} 














private   static   void   copyExistingZipFiles  (  final   ZipFile   fromZip  ,  final   ZipOutputStream   toZip  ,  final   String   fileToOverwrite  ,  final   byte  [  ]  copyBuffer  )  throws   IOException  { 
final   Enumeration   entries  =  fromZip  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
final   ZipEntry   entry  =  (  ZipEntry  )  entries  .  nextElement  (  )  ; 
if  (  !  entry  .  getName  (  )  .  equals  (  fileToOverwrite  )  )  { 
BufferedInputStream   entryStream  =  null  ; 
try  { 
entryStream  =  new   BufferedInputStream  (  fromZip  .  getInputStream  (  entry  )  )  ; 
toZip  .  putNextEntry  (  entry  )  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  entryStream  .  read  (  copyBuffer  )  )  !=  -  1  )  { 
toZip  .  write  (  copyBuffer  ,  0  ,  bytesRead  )  ; 
} 
}  finally  { 
if  (  entryStream  !=  null  )  { 
try  { 
entryStream  .  close  (  )  ; 
}  catch  (  final   IOException   e  )  { 
; 
} 
} 
} 
} 
} 
} 
} 

