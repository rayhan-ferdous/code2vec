package   org  .  jpxx  .  commons  .  util  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  apache  .  commons  .  io  .  IOUtils  ; 







public   class   FileUtils  { 







public   static   void   recursiveRm  (  File   f  )  { 
if  (  f  .  isDirectory  (  )  )  { 
String  [  ]  filenames  =  f  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  filenames  .  length  ;  i  ++  )  recursiveRm  (  new   File  (  f  ,  filenames  [  i  ]  )  )  ; 
f  .  delete  (  )  ; 
}  else  { 
f  .  delete  (  )  ; 
} 
} 







public   static   boolean   mkDirs  (  File   f  )  { 
boolean   ok  =  false  ; 
if  (  f  .  isDirectory  (  )  )  ok  =  true  ;  else   if  (  f  .  exists  (  )  )  ok  =  false  ;  else  { 
ok  =  f  .  mkdirs  (  )  ; 
} 
return   ok  ; 
} 










public   static   boolean   writeToFile  (  File   outputFile  ,  InputStream   in  )  { 
return   writeToFile  (  outputFile  ,  in  ,  true  )  ; 
} 












public   static   boolean   writeToFile  (  File   outputFile  ,  InputStream   in  ,  boolean   closeIn  )  { 
boolean   ok  =  false  ; 
OutputStream   out  =  null  ; 
try  { 
out  =  new   FileOutputStream  (  outputFile  )  ; 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   cnt  =  0  ; 
if  (  in  !=  null  )  cnt  =  in  .  read  (  buffer  )  ; 
while  (  cnt  >  0  )  { 
out  .  write  (  buffer  ,  0  ,  cnt  )  ; 
cnt  =  in  .  read  (  buffer  )  ; 
} 
ok  =  true  ; 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  closeIn  &&  (  in  !=  null  )  )  try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
if  (  out  !=  null  )  try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   ok  ; 
} 







public   static   boolean   mkParentDirs  (  String   fileName  )  { 
boolean   ok  =  false  ; 
File   f  =  new   File  (  fileName  )  ; 
File   parent  =  f  .  getParentFile  (  )  ; 
if  (  parent  ==  null  )  ok  =  true  ;  else   ok  =  parent  .  mkdirs  (  )  ; 
return   ok  ; 
} 





public   static   String   readContent  (  File   file  )  { 
String   result  =  null  ; 
try  { 
byte  [  ]  buffer  =  new   byte  [  (  int  )  file  .  length  (  )  ]  ; 
FileInputStream   in  =  new   FileInputStream  (  file  )  ; 
in  .  read  (  buffer  )  ; 
in  .  close  (  )  ; 
result  =  new   String  (  buffer  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
return   result  ; 
} 








public   static   String   readContent  (  InputStream   in  )  { 
String   result  =  null  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
try  { 
StringBuffer   sbuf  =  new   StringBuffer  (  )  ; 
int   cnt  =  in  .  read  (  buffer  )  ; 
while  (  cnt  >  0  )  { 
String   str  =  new   String  (  buffer  ,  0  ,  cnt  )  ; 
sbuf  .  append  (  str  )  ; 
cnt  =  in  .  read  (  buffer  )  ; 
} 
result  =  sbuf  .  toString  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
return   result  ; 
} 





public   static   void   writeToFile  (  File   outputFile  ,  String   content  )  { 
InputStream   stream  =  new   ByteArrayInputStream  (  content  .  getBytes  (  )  )  ; 
writeToFile  (  outputFile  ,  stream  )  ; 
} 







public   static   List  <  String  >  listAllFiles  (  String   path  )  { 
list  =  new   ArrayList  <  String  >  (  )  ; 
listAllFile  (  path  )  ; 
return   list  ; 
} 

private   static   List  <  String  >  list  =  null  ; 

private   static   synchronized   void   listAllFile  (  String   path  )  { 
if  (  list  ==  null  )  { 
list  =  new   ArrayList  <  String  >  (  )  ; 
} 
File   file  =  new   File  (  path  )  ; 
String  [  ]  array  =  null  ; 
String   sTemp  =  ""  ; 
if  (  !  file  .  isDirectory  (  )  )  { 
return  ; 
} 
array  =  file  .  list  (  )  ; 
if  (  array  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
sTemp  =  path  +  array  [  i  ]  ; 
sTemp  =  sTemp  .  replace  (  '\\'  ,  '/'  )  ; 
file  =  new   File  (  sTemp  )  ; 
if  (  file  .  getName  (  )  .  startsWith  (  "."  )  )  { 
continue  ; 
} 
if  (  file  .  isDirectory  (  )  )  { 
list  .  add  (  sTemp  +  "/"  )  ; 
listAllFile  (  sTemp  +  "/"  )  ; 
}  else  { 
list  .  add  (  sTemp  )  ; 
} 
} 
} 
} 

public   static   void   delete  (  String   file  )  throws   IOException  { 
File   f  =  new   File  (  file  )  ; 
if  (  f  .  isDirectory  (  )  )  { 
deleteDirectory  (  f  )  ; 
}  else  { 
f  .  delete  (  )  ; 
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

public   static   void   copy  (  String   resDir  ,  String   destDir  )  throws   IOException  { 
File   dir  =  new   File  (  resDir  )  ; 
File   resFiles  [  ]  =  dir  .  listFiles  (  )  ; 
if  (  resFiles  ==  null  )  { 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  resFiles  .  length  ;  i  ++  )  { 
File   f  =  resFiles  [  i  ]  ; 
if  (  f  .  isFile  (  )  )  { 
copyFile  (  f  .  getAbsolutePath  (  )  ,  destDir  )  ; 
}  else  { 
String   destF  =  destDir  +  File  .  separator  +  f  .  getName  (  )  ; 
if  (  f  .  getName  (  )  .  startsWith  (  "."  )  )  { 
continue  ; 
} 
File   temp  =  new   File  (  destF  )  ; 
if  (  !  temp  .  exists  (  )  )  { 
temp  .  mkdirs  (  )  ; 
} 
copy  (  f  .  getAbsolutePath  (  )  ,  destF  )  ; 
} 
} 
} 

public   static   void   copyFile  (  String   srcFile  ,  String   destDir  )  throws   IOException  { 
File   f  =  new   File  (  srcFile  )  ; 
if  (  !  f  .  exists  (  )  )  { 
return  ; 
} 
copyFileToDirectory  (  f  ,  new   File  (  destDir  )  )  ; 
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
} 
} 

