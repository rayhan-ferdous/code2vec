package   com  .  rhythm  .  commons  .  io  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  Deflater  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   static   com  .  rhythm  .  base  .  Preconditions  .  assertNotNull  ; 
import   static   com  .  rhythm  .  base  .  Preconditions  .  assertArgument  ; 

public   class   Files  { 

public   static   final   String   ZIP_FILE_EXTENSION  =  ".zip"  ; 

public   static   final   String   FILE_SEPARATOR  =  File  .  separator  ; 

private   Files  (  )  { 
} 










public   static   boolean   exists  (  String   path  )  { 
return   new   File  (  path  )  .  exists  (  )  ; 
} 










public   static   boolean   exists  (  File   dir  ,  String   fileName  )  { 
assertNotNull  (  dir  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
return   false  ; 
} 
File  [  ]  files  =  dir  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
if  (  file  .  getName  (  )  .  equals  (  fileName  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 

public   static   synchronized   File   makeZip  (  File  [  ]  files  ,  String   fileName  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  18024  ]  ; 
if  (  Files  .  exists  (  fileName  )  )  { 
throw   new   IOException  (  "The file ["  +  fileName  +  "] already exists."  )  ; 
} 
if  (  !  fileName  .  endsWith  (  ".zip"  )  )  { 
fileName  +=  ".zip"  ; 
} 
FileOutputStream   fileOutputStream  =  null  ; 
BufferedOutputStream   bufferedOutputStream  =  null  ; 
ZipOutputStream   zipOutputStream  =  null  ; 
InputStream   inputStream  =  null  ; 
try  { 
fileOutputStream  =  new   FileOutputStream  (  fileName  )  ; 
bufferedOutputStream  =  new   BufferedOutputStream  (  fileOutputStream  )  ; 
zipOutputStream  =  new   ZipOutputStream  (  bufferedOutputStream  )  ; 
zipOutputStream  .  setLevel  (  Deflater  .  DEFAULT_COMPRESSION  )  ; 
for  (  File   file  :  files  )  { 
inputStream  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
zipOutputStream  .  putNextEntry  (  new   ZipEntry  (  file  .  getName  (  )  )  )  ; 
int   len  ; 
while  (  (  len  =  inputStream  .  read  (  buffer  )  )  >  0  )  { 
zipOutputStream  .  write  (  buffer  ,  0  ,  len  )  ; 
} 
zipOutputStream  .  closeEntry  (  )  ; 
inputStream  .  close  (  )  ; 
} 
}  finally  { 
Streams  .  close  (  zipOutputStream  )  ; 
Streams  .  close  (  bufferedOutputStream  )  ; 
Streams  .  close  (  fileOutputStream  )  ; 
Streams  .  close  (  inputStream  )  ; 
} 
return   new   File  (  fileName  )  ; 
} 










public   static   synchronized   String  [  ]  listZipContents  (  File   file  )  throws   IOException  { 
assertNotNull  (  file  ,  "Cannot list the contents of a null zip file. "  )  ; 
ZipFile   zipFile  =  new   ZipFile  (  file  )  ; 
String  [  ]  files  =  new   String  [  zipFile  .  size  (  )  ]  ; 
int   entry  =  0  ; 
for  (  Enumeration   entries  =  zipFile  .  entries  (  )  ;  entries  .  hasMoreElements  (  )  ;  )  { 
files  [  entry  ]  =  (  (  ZipEntry  )  entries  .  nextElement  (  )  )  .  getName  (  )  ; 
entry  ++  ; 
} 
zipFile  .  close  (  )  ; 
return   files  ; 
} 


















public   static   synchronized   File   newDirectory  (  String   path  )  throws   IOException  { 
assertNotNull  (  path  ,  "Cannot create a new directory with a null path"  )  ; 
path  =  (  (  path  .  endsWith  (  FILE_SEPARATOR  )  ?  path  :  path  +  FILE_SEPARATOR  )  )  ; 
File   file  =  new   File  (  path  )  ; 
assertArgument  (  file  .  mkdirs  (  )  ,  new   IOException  (  "Failed to make the directory ["  +  path  +  "] check that the path name is valid."  )  )  ; 
assertArgument  (  file  .  exists  (  )  ,  new   FileNotFoundException  (  "Directorty was created but reported as non-existing when checking the path of ["  +  path  +  "]."  )  )  ; 
assertArgument  (  file  .  isDirectory  (  )  ,  new   IOException  (  "Directorty was created and found but was reported as a non-directory when checking the path of ["  +  path  +  "]."  )  )  ; 
return   file  ; 
} 

















public   static   synchronized   File   newFile  (  File   dir  ,  String   name  )  throws   IOException  { 
assertNotNull  (  dir  )  ; 
assertNotNull  (  name  )  ; 
assertArgument  (  name  .  length  (  )  >  0  ,  "Invalid file name"  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
dir  =  newDirectory  (  dir  .  getCanonicalPath  (  )  )  ; 
} 
File   file  =  new   File  (  dir  .  getCanonicalPath  (  )  +  FILE_SEPARATOR  +  name  )  ; 
assertArgument  (  file  .  createNewFile  (  )  ,  new   IOException  (  "Failed to make the file ["  +  file  .  getCanonicalPath  (  )  +  "] check that the path name is valid."  )  )  ; 
assertArgument  (  file  .  exists  (  )  ,  new   FileNotFoundException  (  "Directorty was created but reported as non-existing when checking the path of ["  +  file  .  getCanonicalPath  (  )  +  "]."  )  )  ; 
assertArgument  (  file  .  isFile  (  )  ,  new   IOException  (  "Directorty was created and found but was reported as a non-directory when checking the path of ["  +  file  .  getCanonicalPath  (  )  +  "]."  )  )  ; 
return   file  ; 
} 















public   static   synchronized   boolean   copyFile  (  File   destFile  ,  File   srcFile  )  throws   IOException  { 
assertNotNull  (  srcFile  ,  "Source must not be null"  )  ; 
assertNotNull  (  destFile  ,  "Destination must not be null"  )  ; 
assertArgument  (  srcFile  .  exists  (  )  ,  "Source '"  +  srcFile  +  "' does not exist"  )  ; 
assertArgument  (  srcFile  .  isFile  (  )  ,  "Source '"  +  srcFile  +  "' exists but is a not a file"  )  ; 
assertArgument  (  !  srcFile  .  getCanonicalPath  (  )  .  equals  (  destFile  .  getCanonicalPath  (  )  )  ,  "Source '"  +  srcFile  +  "' and destination '"  +  destFile  +  "' are the same"  )  ; 
if  (  destFile  .  getParentFile  (  )  !=  null  &&  destFile  .  getParentFile  (  )  .  exists  (  )  ==  false  )  { 
if  (  destFile  .  getParentFile  (  )  .  mkdirs  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' directory cannot be created"  )  ; 
} 
} 
if  (  destFile  .  exists  (  )  &&  destFile  .  canWrite  (  )  ==  false  )  { 
throw   new   IOException  (  "Destination '"  +  destFile  +  "' exists but is read-only"  )  ; 
} 
return   doCopyFile  (  srcFile  ,  destFile  ,  false  )  ; 
} 









private   static   synchronized   boolean   doCopyFile  (  File   srcFile  ,  File   destFile  ,  boolean   preserveFileDate  )  throws   IOException  { 
if  (  destFile  .  exists  (  )  &&  destFile  .  isDirectory  (  )  )  { 
destFile  =  new   File  (  destFile  +  FILE_SEPARATOR  +  srcFile  .  getName  (  )  )  ; 
} 
FileInputStream   input  =  new   FileInputStream  (  srcFile  )  ; 
try  { 
FileOutputStream   output  =  new   FileOutputStream  (  destFile  )  ; 
try  { 
IOUtils  .  copy  (  input  ,  output  )  ; 
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
return   destFile  .  exists  (  )  ; 
} 







public   static   void   deleteDirectory  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  exists  (  )  )  { 
return  ; 
} 
emptyDirectory  (  directory  )  ; 
if  (  !  directory  .  delete  (  )  )  { 
String   message  =  "Unable to delete directory "  +  directory  +  "."  ; 
throw   new   IOException  (  message  )  ; 
} 
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








public   static   void   emptyDirectory  (  File   directory  )  throws   IOException  { 
assertNotNull  (  directory  ,  "Cannot empty the directory of a null argument"  )  ; 
assertArgument  (  directory  .  exists  (  )  ,  "The directory ["  +  directory  +  "] does not exist."  )  ; 
assertArgument  (  directory  .  isDirectory  (  )  ,  "The path ["  +  directory  +  "] is not a directory."  )  ; 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
throw   new   IOException  (  "Failed to list contents of "  +  directory  )  ; 
} 
IOException   exception  =  null  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
try  { 
forceDelete  (  file  )  ; 
}  catch  (  IOException   es  )  { 
exception  =  es  ; 
} 
} 
if  (  null  !=  exception  )  { 
throw   exception  ; 
} 
} 
















public   static   boolean   deleteQuietly  (  File   file  )  { 
if  (  file  ==  null  )  { 
return   false  ; 
} 
try  { 
if  (  file  .  isDirectory  (  )  )  { 
emptyDirectory  (  file  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
try  { 
return   file  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
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
copyFile  (  destFile  ,  srcFile  )  ; 
if  (  !  srcFile  .  delete  (  )  )  { 
deleteQuietly  (  destFile  )  ; 
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




















public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  )  throws   IOException  { 
copyDirectory  (  srcDir  ,  destDir  ,  true  )  ; 
} 





















public   static   void   copyDirectory  (  File   srcDir  ,  File   destDir  ,  boolean   preserveFileDate  )  throws   IOException  { 
copyDirectory  (  srcDir  ,  destDir  ,  null  ,  preserveFileDate  )  ; 
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
List   exclusionList  =  null  ; 
if  (  destDir  .  getCanonicalPath  (  )  .  startsWith  (  srcDir  .  getCanonicalPath  (  )  )  )  { 
File  [  ]  srcFiles  =  filter  ==  null  ?  srcDir  .  listFiles  (  )  :  srcDir  .  listFiles  (  filter  )  ; 
if  (  srcFiles  !=  null  &&  srcFiles  .  length  >  0  )  { 
exclusionList  =  new   ArrayList  (  srcFiles  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  srcFiles  .  length  ;  i  ++  )  { 
File   copiedFile  =  new   File  (  destDir  ,  srcFiles  [  i  ]  .  getName  (  )  )  ; 
exclusionList  .  add  (  copiedFile  .  getCanonicalPath  (  )  )  ; 
} 
} 
} 
doCopyDirectory  (  srcDir  ,  destDir  ,  filter  ,  preserveFileDate  ,  exclusionList  )  ; 
} 












private   static   void   doCopyDirectory  (  File   srcDir  ,  File   destDir  ,  FileFilter   filter  ,  boolean   preserveFileDate  ,  List   exclusionList  )  throws   IOException  { 
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

public   static   byte  [  ]  getBytesFromFile  (  File   file  )  throws   IOException  { 
InputStream   is  =  null  ; 
byte  [  ]  bytes  =  null  ; 
try  { 
is  =  new   FileInputStream  (  file  )  ; 
long   length  =  file  .  length  (  )  ; 
if  (  length  >  Integer  .  MAX_VALUE  )  { 
throw   new   IOException  (  "File is too large"  )  ; 
} 
bytes  =  new   byte  [  (  int  )  length  ]  ; 
int   offset  =  0  ; 
int   numRead  =  0  ; 
while  (  offset  <  bytes  .  length  &&  (  numRead  =  is  .  read  (  bytes  ,  offset  ,  bytes  .  length  -  offset  )  )  >=  0  )  { 
offset  +=  numRead  ; 
} 
if  (  offset  <  bytes  .  length  )  { 
throw   new   IOException  (  "Could not completely read file "  +  file  .  getName  (  )  )  ; 
} 
}  catch  (  Exception   ex  )  { 
throw   new   IOException  (  ex  )  ; 
}  finally  { 
if  (  is  !=  null  )  { 
is  .  close  (  )  ; 
} 
} 
return   bytes  ; 
} 
} 

