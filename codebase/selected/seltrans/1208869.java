package   org  .  lightcommons  .  io  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  CRC32  ; 
import   java  .  util  .  zip  .  CheckedInputStream  ; 
import   java  .  util  .  zip  .  Checksum  ; 




































public   class   FileUtils  { 




public   FileUtils  (  )  { 
super  (  )  ; 
} 




public   static   final   long   ONE_KB  =  1024  ; 




public   static   final   long   ONE_MB  =  ONE_KB  *  ONE_KB  ; 




public   static   final   long   ONE_GB  =  ONE_KB  *  ONE_MB  ; 




public   static   final   File  [  ]  EMPTY_FILE_ARRAY  =  new   File  [  0  ]  ; 



















public   static   FileInputStream   openInputStream  (  File   file  )  throws   IOException  { 
if  (  file  .  exists  (  )  )  { 
if  (  file  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "File '"  +  file  +  "' exists but is a directory"  )  ; 
} 
if  (  file  .  canRead  (  )  ==  false  )  { 
throw   new   IOException  (  "File '"  +  file  +  "' cannot be read"  )  ; 
} 
}  else  { 
throw   new   FileNotFoundException  (  "File '"  +  file  +  "' does not exist"  )  ; 
} 
return   new   FileInputStream  (  file  )  ; 
} 





















public   static   FileOutputStream   openOutputStream  (  File   file  )  throws   IOException  { 
if  (  file  .  exists  (  )  )  { 
if  (  file  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "File '"  +  file  +  "' exists but is a directory"  )  ; 
} 
if  (  file  .  canWrite  (  )  ==  false  )  { 
throw   new   IOException  (  "File '"  +  file  +  "' cannot be written to"  )  ; 
} 
}  else  { 
File   parent  =  file  .  getParentFile  (  )  ; 
if  (  parent  !=  null  &&  parent  .  exists  (  )  ==  false  )  { 
if  (  parent  .  mkdirs  (  )  ==  false  )  { 
throw   new   IOException  (  "File '"  +  file  +  "' could not be created"  )  ; 
} 
} 
} 
return   new   FileOutputStream  (  file  )  ; 
} 








public   static   String   byteCountToDisplaySize  (  long   size  )  { 
String   displaySize  ; 
if  (  size  /  ONE_GB  >  0  )  { 
displaySize  =  String  .  valueOf  (  size  /  ONE_GB  )  +  " GB"  ; 
}  else   if  (  size  /  ONE_MB  >  0  )  { 
displaySize  =  String  .  valueOf  (  size  /  ONE_MB  )  +  " MB"  ; 
}  else   if  (  size  /  ONE_KB  >  0  )  { 
displaySize  =  String  .  valueOf  (  size  /  ONE_KB  )  +  " KB"  ; 
}  else  { 
displaySize  =  String  .  valueOf  (  size  )  +  " bytes"  ; 
} 
return   displaySize  ; 
} 













public   static   void   touch  (  File   file  )  throws   IOException  { 
if  (  !  file  .  exists  (  )  )  { 
OutputStream   out  =  openOutputStream  (  file  )  ; 
IOUtils  .  closeQuietly  (  out  )  ; 
} 
boolean   success  =  file  .  setLastModified  (  System  .  currentTimeMillis  (  )  )  ; 
if  (  !  success  )  { 
throw   new   IOException  (  "Unable to set the last modification time for "  +  file  )  ; 
} 
} 









public   static   File  [  ]  convertFileCollectionToFileArray  (  Collection  <  File  >  files  )  { 
return   files  .  toArray  (  new   File  [  files  .  size  (  )  ]  )  ; 
} 
















public   static   boolean   contentEquals  (  File   file1  ,  File   file2  )  throws   IOException  { 
boolean   file1Exists  =  file1  .  exists  (  )  ; 
if  (  file1Exists  !=  file2  .  exists  (  )  )  { 
return   false  ; 
} 
if  (  !  file1Exists  )  { 
return   true  ; 
} 
if  (  file1  .  isDirectory  (  )  ||  file2  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Can't compare directories, only files"  )  ; 
} 
if  (  file1  .  length  (  )  !=  file2  .  length  (  )  )  { 
return   false  ; 
} 
if  (  file1  .  getCanonicalFile  (  )  .  equals  (  file2  .  getCanonicalFile  (  )  )  )  { 
return   true  ; 
} 
InputStream   input1  =  null  ; 
InputStream   input2  =  null  ; 
try  { 
input1  =  new   FileInputStream  (  file1  )  ; 
input2  =  new   FileInputStream  (  file2  )  ; 
return   IOUtils  .  contentEquals  (  input1  ,  input2  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  input1  )  ; 
IOUtils  .  closeQuietly  (  input2  )  ; 
} 
} 













public   static   File   toFile  (  URL   url  )  { 
if  (  url  ==  null  ||  !  url  .  getProtocol  (  )  .  equals  (  "file"  )  )  { 
return   null  ; 
}  else  { 
String   filename  =  url  .  getFile  (  )  .  replace  (  '/'  ,  File  .  separatorChar  )  ; 
int   pos  =  0  ; 
while  (  (  pos  =  filename  .  indexOf  (  '%'  ,  pos  )  )  >=  0  )  { 
if  (  pos  +  2  <  filename  .  length  (  )  )  { 
String   hexStr  =  filename  .  substring  (  pos  +  1  ,  pos  +  3  )  ; 
char   ch  =  (  char  )  Integer  .  parseInt  (  hexStr  ,  16  )  ; 
filename  =  filename  .  substring  (  0  ,  pos  )  +  ch  +  filename  .  substring  (  pos  +  3  )  ; 
} 
} 
return   new   File  (  filename  )  ; 
} 
} 




















public   static   File  [  ]  toFiles  (  URL  [  ]  urls  )  { 
if  (  urls  ==  null  ||  urls  .  length  ==  0  )  { 
return   EMPTY_FILE_ARRAY  ; 
} 
File  [  ]  files  =  new   File  [  urls  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
URL   url  =  urls  [  i  ]  ; 
if  (  url  !=  null  )  { 
if  (  url  .  getProtocol  (  )  .  equals  (  "file"  )  ==  false  )  { 
throw   new   IllegalArgumentException  (  "URL could not be converted to a File: "  +  url  )  ; 
} 
files  [  i  ]  =  toFile  (  url  )  ; 
} 
} 
return   files  ; 
} 










public   static   URL  [  ]  toURLs  (  File  [  ]  files  )  throws   IOException  { 
URL  [  ]  urls  =  new   URL  [  files  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
urls  [  i  ]  =  files  [  i  ]  .  toURI  (  )  .  toURL  (  )  ; 
} 
return   urls  ; 
} 

















public   static   void   copyFileToDirectory  (  File   srcFile  ,  File   destDir  )  throws   IOException  { 
copyFileToDirectory  (  srcFile  ,  destDir  ,  true  )  ; 
} 




















public   static   void   copyFileToDirectory  (  File   srcFile  ,  File   destDir  ,  boolean   preserveFileDate  )  throws   IOException  { 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  destDir  .  exists  (  )  &&  destDir  .  isDirectory  (  )  ==  false  )  { 
throw   new   IllegalArgumentException  (  "Destination '"  +  destDir  +  "' is not a directory"  )  ; 
} 
copyFile  (  srcFile  ,  new   File  (  destDir  ,  srcFile  .  getName  (  )  )  ,  preserveFileDate  )  ; 
} 

















public   static   void   copyFile  (  File   srcFile  ,  File   destFile  )  throws   IOException  { 
copyFile  (  srcFile  ,  destFile  ,  true  )  ; 
} 



















public   static   void   copyFile  (  File   srcFile  ,  File   destFile  ,  boolean   preserveFileDate  )  throws   IOException  { 
if  (  srcFile  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destFile  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  srcFile  .  exists  (  )  ==  false  )  { 
throw   new   FileNotFoundException  (  "Source '"  +  srcFile  +  "' does not exist"  )  ; 
} 
if  (  srcFile  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Source '"  +  srcFile  +  "' exists but is a directory"  )  ; 
} 
if  (  srcFile  .  getCanonicalPath  (  )  .  equals  (  destFile  .  getCanonicalPath  (  )  )  )  { 
throw   new   IOException  (  "Source '"  +  srcFile  +  "' and destination '"  +  destFile  +  "' are the same"  )  ; 
} 
if  (  destFile  .  getParentFile  (  )  !=  null  &&  destFile  .  getParentFile  (  )  .  exists  (  )  ==  false  )  { 
if  (  destFile  .  getParentFile  (  )  .  mkdirs  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' directory cannot be created"  )  ; 
} 
} 
if  (  destFile  .  exists  (  )  &&  destFile  .  canWrite  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' exists but is read-only"  )  ; 
} 
doCopyFile  (  srcFile  ,  destFile  ,  preserveFileDate  )  ; 
} 









private   static   void   doCopyFile  (  File   srcFile  ,  File   destFile  ,  boolean   preserveFileDate  )  throws   IOException  { 
if  (  destFile  .  exists  (  )  &&  destFile  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' exists but is a directory"  )  ; 
} 
FileChannel   input  =  new   FileInputStream  (  srcFile  )  .  getChannel  (  )  ; 
try  { 
FileChannel   output  =  new   FileOutputStream  (  destFile  )  .  getChannel  (  )  ; 
try  { 
output  .  transferFrom  (  input  ,  0  ,  input  .  size  (  )  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  output  )  ; 
} 
}  finally  { 
IOUtils  .  closeQuietly  (  input  )  ; 
} 
if  (  srcFile  .  length  (  )  !=  destFile  .  length  (  )  )  { 
throw   new   IOException  (  "Failed to copy full contents from '"  +  srcFile  +  "' to '"  +  destFile  +  "'"  )  ; 
} 
if  (  preserveFileDate  )  { 
destFile  .  setLastModified  (  srcFile  .  lastModified  (  )  )  ; 
} 
} 



















public   static   void   copyDirectoryToDirectory  (  File   srcDir  ,  File   destDir  )  throws   IOException  { 
if  (  srcDir  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  srcDir  .  exists  (  )  &&  srcDir  .  isDirectory  (  )  ==  false  )  { 
throw   new   IllegalArgumentException  (  "Source '"  +  destDir  +  "' is not a directory"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  destDir  .  exists  (  )  &&  destDir  .  isDirectory  (  )  ==  false  )  { 
throw   new   IllegalArgumentException  (  "Destination '"  +  destDir  +  "' is not a directory"  )  ; 
} 
copyDirectory  (  srcDir  ,  new   File  (  destDir  ,  srcDir  .  getName  (  )  )  ,  true  )  ; 
} 




















public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  )  throws   IOException  { 
copyDirectory  (  srcDir  ,  destDir  ,  true  )  ; 
} 





















public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  ,  boolean   preserveFileDate  )  throws   IOException  { 
copyDirectory  (  srcDir  ,  destDir  ,  null  ,  preserveFileDate  )  ; 
} 








































public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  ,  FileFilter   filter  )  throws   IOException  { 
copyDirectory  (  srcDir  ,  destDir  ,  filter  ,  true  )  ; 
} 









































public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  ,  FileFilter   filter  ,  boolean   preserveFileDate  )  throws   IOException  { 
if  (  srcDir  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  srcDir  .  exists  (  )  ==  false  )  { 
throw   new   FileNotFoundException  (  "Source '"  +  srcDir  +  "' does not exist"  )  ; 
} 
if  (  srcDir  .  isDirectory  (  )  ==  false  )  { 
throw   new   IOException  (  "Source '"  +  srcDir  +  "' exists but is not a directory"  )  ; 
} 
if  (  srcDir  .  getCanonicalPath  (  )  .  equals  (  destDir  .  getCanonicalPath  (  )  )  )  { 
throw   new   IOException  (  "Source '"  +  srcDir  +  "' and destination '"  +  destDir  +  "' are the same"  )  ; 
} 
List  <  String  >  exclusionList  =  null  ; 
if  (  destDir  .  getCanonicalPath  (  )  .  startsWith  (  srcDir  .  getCanonicalPath  (  )  )  )  { 
File  [  ]  srcFiles  =  filter  ==  null  ?  srcDir  .  listFiles  (  )  :  srcDir  .  listFiles  (  filter  )  ; 
if  (  srcFiles  !=  null  &&  srcFiles  .  length  >  0  )  { 
exclusionList  =  new   ArrayList  <  String  >  (  srcFiles  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  srcFiles  .  length  ;  i  ++  )  { 
File   copiedFile  =  new   File  (  destDir  ,  srcFiles  [  i  ]  .  getName  (  )  )  ; 
exclusionList  .  add  (  copiedFile  .  getCanonicalPath  (  )  )  ; 
} 
} 
} 
doCopyDirectory  (  srcDir  ,  destDir  ,  filter  ,  preserveFileDate  ,  exclusionList  )  ; 
} 












private   static   void   doCopyDirectory  (  File   srcDir  ,  File   destDir  ,  FileFilter   filter  ,  boolean   preserveFileDate  ,  List  <  String  >  exclusionList  )  throws   IOException  { 
if  (  destDir  .  exists  (  )  )  { 
if  (  destDir  .  isDirectory  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' exists but is not a directory"  )  ; 
} 
}  else  { 
if  (  destDir  .  mkdirs  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' directory cannot be created"  )  ; 
} 
if  (  preserveFileDate  )  { 
destDir  .  setLastModified  (  srcDir  .  lastModified  (  )  )  ; 
} 
} 
if  (  destDir  .  canWrite  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' cannot be written to"  )  ; 
} 
File  [  ]  files  =  filter  ==  null  ?  srcDir  .  listFiles  (  )  :  srcDir  .  listFiles  (  filter  )  ; 
if  (  files  ==  null  )  { 
throw   new   IOException  (  "Failed to list contents of "  +  srcDir  )  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   copiedFile  =  new   File  (  destDir  ,  files  [  i  ]  .  getName  (  )  )  ; 
if  (  exclusionList  ==  null  ||  !  exclusionList  .  contains  (  files  [  i  ]  .  getCanonicalPath  (  )  )  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  { 
doCopyDirectory  (  files  [  i  ]  ,  copiedFile  ,  filter  ,  preserveFileDate  ,  exclusionList  )  ; 
}  else  { 
doCopyFile  (  files  [  i  ]  ,  copiedFile  ,  preserveFileDate  )  ; 
} 
} 
} 
} 
















public   static   void   copyURLToFile  (  URL   source  ,  File   destination  )  throws   IOException  { 
InputStream   input  =  source  .  openStream  (  )  ; 
try  { 
FileOutputStream   output  =  openOutputStream  (  destination  )  ; 
try  { 
IOUtils  .  copy  (  input  ,  output  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  output  )  ; 
} 
}  finally  { 
IOUtils  .  closeQuietly  (  input  )  ; 
} 
} 







public   static   void   deleteDirectory  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  exists  (  )  )  { 
return  ; 
} 
cleanDirectory  (  directory  )  ; 
if  (  !  directory  .  delete  (  )  )  { 
String   message  =  "Unable to delete directory "  +  directory  +  "."  ; 
throw   new   IOException  (  message  )  ; 
} 
} 
















public   static   boolean   deleteQuietly  (  File   file  )  { 
if  (  file  ==  null  )  { 
return   false  ; 
} 
try  { 
if  (  file  .  isDirectory  (  )  )  { 
cleanDirectory  (  file  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
try  { 
return   file  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 







public   static   void   cleanDirectory  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  exists  (  )  )  { 
String   message  =  directory  +  " does not exist"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  directory  .  isDirectory  (  )  )  { 
String   message  =  directory  +  " is not a directory"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
throw   new   IOException  (  "Failed to list contents of "  +  directory  )  ; 
} 
IOException   exception  =  null  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
try  { 
forceDelete  (  file  )  ; 
}  catch  (  IOException   ioe  )  { 
exception  =  ioe  ; 
} 
} 
if  (  null  !=  exception  )  { 
throw   exception  ; 
} 
} 












public   static   boolean   waitFor  (  File   file  ,  int   seconds  )  { 
int   timeout  =  0  ; 
int   tick  =  0  ; 
while  (  !  file  .  exists  (  )  )  { 
if  (  tick  ++  >=  10  )  { 
tick  =  0  ; 
if  (  timeout  ++  >  seconds  )  { 
return   false  ; 
} 
} 
try  { 
Thread  .  sleep  (  100  )  ; 
}  catch  (  InterruptedException   ignore  )  { 
}  catch  (  Exception   ex  )  { 
break  ; 
} 
} 
return   true  ; 
} 











public   static   String   readFileToString  (  File   file  ,  String   encoding  )  throws   IOException  { 
InputStream   in  =  null  ; 
try  { 
in  =  openInputStream  (  file  )  ; 
return   IOUtils  .  toString  (  in  ,  encoding  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
} 
} 










public   static   String   readFileToString  (  File   file  )  throws   IOException  { 
return   readFileToString  (  file  ,  null  )  ; 
} 










public   static   byte  [  ]  readFileToByteArray  (  File   file  )  throws   IOException  { 
InputStream   in  =  null  ; 
try  { 
in  =  openInputStream  (  file  )  ; 
return   IOUtils  .  toByteArray  (  in  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
} 
} 












public   static   List  <  String  >  readLines  (  File   file  ,  String   encoding  )  throws   IOException  { 
InputStream   in  =  null  ; 
try  { 
in  =  openInputStream  (  file  )  ; 
return   IOUtils  .  readLines  (  in  ,  encoding  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
} 
} 










public   static   List  <  String  >  readLines  (  File   file  )  throws   IOException  { 
return   readLines  (  file  ,  null  )  ; 
} 













public   static   void   writeStringToFile  (  File   file  ,  String   data  ,  String   encoding  )  throws   IOException  { 
OutputStream   out  =  null  ; 
try  { 
out  =  openOutputStream  (  file  )  ; 
IOUtils  .  write  (  data  ,  out  ,  encoding  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  out  )  ; 
} 
} 








public   static   void   writeStringToFile  (  File   file  ,  String   data  )  throws   IOException  { 
writeStringToFile  (  file  ,  data  ,  null  )  ; 
} 









public   static   void   write  (  File   file  ,  CharSequence   data  )  throws   IOException  { 
String   str  =  data  ==  null  ?  null  :  data  .  toString  (  )  ; 
writeStringToFile  (  file  ,  str  )  ; 
} 











public   static   void   write  (  File   file  ,  CharSequence   data  ,  String   encoding  )  throws   IOException  { 
String   str  =  data  ==  null  ?  null  :  data  .  toString  (  )  ; 
writeStringToFile  (  file  ,  str  ,  encoding  )  ; 
} 












public   static   void   writeByteArrayToFile  (  File   file  ,  byte  [  ]  data  )  throws   IOException  { 
OutputStream   out  =  null  ; 
try  { 
out  =  openOutputStream  (  file  )  ; 
out  .  write  (  data  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  out  )  ; 
} 
} 
















public   static   void   writeLines  (  File   file  ,  String   encoding  ,  Collection  <  ?  >  lines  )  throws   IOException  { 
writeLines  (  file  ,  encoding  ,  lines  ,  null  )  ; 
} 











public   static   void   writeLines  (  File   file  ,  Collection  <  ?  >  lines  )  throws   IOException  { 
writeLines  (  file  ,  null  ,  lines  ,  null  )  ; 
} 

















public   static   void   writeLines  (  File   file  ,  String   encoding  ,  Collection  <  ?  >  lines  ,  String   lineEnding  )  throws   IOException  { 
OutputStream   out  =  null  ; 
try  { 
out  =  openOutputStream  (  file  )  ; 
IOUtils  .  writeLines  (  lines  ,  lineEnding  ,  out  ,  encoding  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  out  )  ; 
} 
} 












public   static   void   writeLines  (  File   file  ,  Collection  <  ?  >  lines  ,  String   lineEnding  )  throws   IOException  { 
writeLines  (  file  ,  null  ,  lines  ,  lineEnding  )  ; 
} 
















public   static   void   forceDelete  (  File   file  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
deleteDirectory  (  file  )  ; 
}  else  { 
boolean   filePresent  =  file  .  exists  (  )  ; 
if  (  !  file  .  delete  (  )  )  { 
if  (  !  filePresent  )  { 
throw   new   FileNotFoundException  (  "File does not exist: "  +  file  )  ; 
} 
String   message  =  "Unable to delete file: "  +  file  ; 
throw   new   IOException  (  message  )  ; 
} 
} 
} 









public   static   void   forceDeleteOnExit  (  File   file  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
deleteDirectoryOnExit  (  file  )  ; 
}  else  { 
file  .  deleteOnExit  (  )  ; 
} 
} 








private   static   void   deleteDirectoryOnExit  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  exists  (  )  )  { 
return  ; 
} 
cleanDirectoryOnExit  (  directory  )  ; 
directory  .  deleteOnExit  (  )  ; 
} 








private   static   void   cleanDirectoryOnExit  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  exists  (  )  )  { 
String   message  =  directory  +  " does not exist"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  directory  .  isDirectory  (  )  )  { 
String   message  =  directory  +  " is not a directory"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
throw   new   IOException  (  "Failed to list contents of "  +  directory  )  ; 
} 
IOException   exception  =  null  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
try  { 
forceDeleteOnExit  (  file  )  ; 
}  catch  (  IOException   ioe  )  { 
exception  =  ioe  ; 
} 
} 
if  (  null  !=  exception  )  { 
throw   exception  ; 
} 
} 










public   static   void   forceMkdir  (  File   directory  )  throws   IOException  { 
if  (  directory  .  exists  (  )  )  { 
if  (  directory  .  isFile  (  )  )  { 
String   message  =  "File "  +  directory  +  " exists and is "  +  "not a directory. Unable to create directory."  ; 
throw   new   IOException  (  message  )  ; 
} 
}  else  { 
if  (  !  directory  .  mkdirs  (  )  )  { 
String   message  =  "Unable to create directory "  +  directory  ; 
throw   new   IOException  (  message  )  ; 
} 
} 
} 








public   static   long   sizeOfDirectory  (  File   directory  )  { 
if  (  !  directory  .  exists  (  )  )  { 
String   message  =  directory  +  " does not exist"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  !  directory  .  isDirectory  (  )  )  { 
String   message  =  directory  +  " is not a directory"  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
long   size  =  0  ; 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
return   0L  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
if  (  file  .  isDirectory  (  )  )  { 
size  +=  sizeOfDirectory  (  file  )  ; 
}  else  { 
size  +=  file  .  length  (  )  ; 
} 
} 
return   size  ; 
} 














public   static   boolean   isFileNewer  (  File   file  ,  File   reference  )  { 
if  (  reference  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified reference file"  )  ; 
} 
if  (  !  reference  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "The reference file '"  +  file  +  "' doesn't exist"  )  ; 
} 
return   isFileNewer  (  file  ,  reference  .  lastModified  (  )  )  ; 
} 













public   static   boolean   isFileNewer  (  File   file  ,  Date   date  )  { 
if  (  date  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified date"  )  ; 
} 
return   isFileNewer  (  file  ,  date  .  getTime  (  )  )  ; 
} 













public   static   boolean   isFileNewer  (  File   file  ,  long   timeMillis  )  { 
if  (  file  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified file"  )  ; 
} 
if  (  !  file  .  exists  (  )  )  { 
return   false  ; 
} 
return   file  .  lastModified  (  )  >  timeMillis  ; 
} 














public   static   boolean   isFileOlder  (  File   file  ,  File   reference  )  { 
if  (  reference  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified reference file"  )  ; 
} 
if  (  !  reference  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "The reference file '"  +  file  +  "' doesn't exist"  )  ; 
} 
return   isFileOlder  (  file  ,  reference  .  lastModified  (  )  )  ; 
} 













public   static   boolean   isFileOlder  (  File   file  ,  Date   date  )  { 
if  (  date  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified date"  )  ; 
} 
return   isFileOlder  (  file  ,  date  .  getTime  (  )  )  ; 
} 













public   static   boolean   isFileOlder  (  File   file  ,  long   timeMillis  )  { 
if  (  file  ==  null  )  { 
throw   new   IllegalArgumentException  (  "No specified file"  )  ; 
} 
if  (  !  file  .  exists  (  )  )  { 
return   false  ; 
} 
return   file  .  lastModified  (  )  <  timeMillis  ; 
} 












public   static   long   checksumCRC32  (  File   file  )  throws   IOException  { 
CRC32   crc  =  new   CRC32  (  )  ; 
checksum  (  file  ,  crc  )  ; 
return   crc  .  getValue  (  )  ; 
} 


















public   static   Checksum   checksum  (  File   file  ,  Checksum   checksum  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
throw   new   IllegalArgumentException  (  "Checksums can't be computed on directories"  )  ; 
} 
InputStream   in  =  null  ; 
try  { 
in  =  new   CheckedInputStream  (  new   FileInputStream  (  file  )  ,  checksum  )  ; 
IOUtils  .  copy  (  in  ,  new   NullOutputStream  (  )  )  ; 
}  finally  { 
IOUtils  .  closeQuietly  (  in  )  ; 
} 
return   checksum  ; 
} 













public   static   void   moveDirectory  (  File   srcDir  ,  File   destDir  )  throws   IOException  { 
if  (  srcDir  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  !  srcDir  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "Source '"  +  srcDir  +  "' does not exist"  )  ; 
} 
if  (  !  srcDir  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Source '"  +  srcDir  +  "' is not a directory"  )  ; 
} 
if  (  destDir  .  exists  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' already exists"  )  ; 
} 
boolean   rename  =  srcDir  .  renameTo  (  destDir  )  ; 
if  (  !  rename  )  { 
copyDirectory  (  srcDir  ,  destDir  )  ; 
deleteDirectory  (  srcDir  )  ; 
if  (  srcDir  .  exists  (  )  )  { 
throw   new   IOException  (  "Failed to delete original directory '"  +  srcDir  +  "' after copy to '"  +  destDir  +  "'"  )  ; 
} 
} 
} 













public   static   void   moveDirectoryToDirectory  (  File   src  ,  File   destDir  ,  boolean   createDestDir  )  throws   IOException  { 
if  (  src  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination directory must not be null"  )  ; 
} 
if  (  !  destDir  .  exists  (  )  &&  createDestDir  )  { 
destDir  .  mkdirs  (  )  ; 
} 
if  (  !  destDir  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "Destination directory '"  +  destDir  +  "' does not exist [createDestDir="  +  createDestDir  +  "]"  )  ; 
} 
if  (  !  destDir  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' is not a directory"  )  ; 
} 
moveDirectory  (  src  ,  new   File  (  destDir  ,  src  .  getName  (  )  )  )  ; 
} 













public   static   void   moveFile  (  File   srcFile  ,  File   destFile  )  throws   IOException  { 
if  (  srcFile  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destFile  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  !  srcFile  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "Source '"  +  srcFile  +  "' does not exist"  )  ; 
} 
if  (  srcFile  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Source '"  +  srcFile  +  "' is a directory"  )  ; 
} 
if  (  destFile  .  exists  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' already exists"  )  ; 
} 
if  (  destFile  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' is a directory"  )  ; 
} 
boolean   rename  =  srcFile  .  renameTo  (  destFile  )  ; 
if  (  !  rename  )  { 
copyFile  (  srcFile  ,  destFile  )  ; 
if  (  !  srcFile  .  delete  (  )  )  { 
FileUtils  .  deleteQuietly  (  destFile  )  ; 
throw   new   IOException  (  "Failed to delete original file '"  +  srcFile  +  "' after copy to '"  +  destFile  +  "'"  )  ; 
} 
} 
} 













public   static   void   moveFileToDirectory  (  File   srcFile  ,  File   destDir  ,  boolean   createDestDir  )  throws   IOException  { 
if  (  srcFile  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination directory must not be null"  )  ; 
} 
if  (  !  destDir  .  exists  (  )  &&  createDestDir  )  { 
destDir  .  mkdirs  (  )  ; 
} 
if  (  !  destDir  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "Destination directory '"  +  destDir  +  "' does not exist [createDestDir="  +  createDestDir  +  "]"  )  ; 
} 
if  (  !  destDir  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Destination '"  +  destDir  +  "' is not a directory"  )  ; 
} 
moveFile  (  srcFile  ,  new   File  (  destDir  ,  srcFile  .  getName  (  )  )  )  ; 
} 















public   static   void   moveToDirectory  (  File   src  ,  File   destDir  ,  boolean   createDestDir  )  throws   IOException  { 
if  (  src  ==  null  )  { 
throw   new   NullPointerException  (  "Source must not be null"  )  ; 
} 
if  (  destDir  ==  null  )  { 
throw   new   NullPointerException  (  "Destination must not be null"  )  ; 
} 
if  (  !  src  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "Source '"  +  src  +  "' does not exist"  )  ; 
} 
if  (  src  .  isDirectory  (  )  )  { 
moveDirectoryToDirectory  (  src  ,  destDir  ,  createDestDir  )  ; 
}  else  { 
moveFileToDirectory  (  src  ,  destDir  ,  createDestDir  )  ; 
} 
} 
} 

