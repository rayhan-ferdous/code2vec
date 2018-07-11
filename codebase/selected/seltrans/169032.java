package   org  .  ourgrid  .  common  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilePermission  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Map  ; 
import   org  .  apache  .  commons  .  io  .  FileSystemUtils  ; 
import   org  .  ourgrid  .  common  .  exception  .  UnableToDigestFileException  ; 
import   org  .  ourgrid  .  common  .  executor  .  Win32Executor  ; 
import   org  .  ourgrid  .  worker  .  WorkerConfiguration  ; 
import   sun  .  misc  .  BASE64Encoder  ; 




public   class   JavaFileUtil  { 


private   static   final   String   CLASS_SUFFIX  =  ".class"  ; 


private   static   final   String   JAVA_SUFFIX  =  ".java"  ; 

public   static   void   writeToFile  (  String   string  ,  String   path  )  throws   IOException  { 
File   f  =  new   File  (  path  )  ; 
FileWriter   writer  =  new   FileWriter  (  f  ,  true  )  ; 
writer  .  write  (  string  +  System  .  getProperty  (  "newLine"  )  )  ; 
writer  .  close  (  )  ; 
} 








public   static   String   extractJavaSuffix  (  String   namePlusSufix  ,  String   suffix  )  { 
int   sufixo_inicio  =  namePlusSufix  .  indexOf  (  suffix  )  ; 
if  (  sufixo_inicio  !=  -  1  )  { 
namePlusSufix  =  namePlusSufix  .  substring  (  0  ,  sufixo_inicio  )  ; 
} 
return   namePlusSufix  ; 
} 








public   static   String   getFullClassName  (  File   file  ,  String   root  )  { 
String   path  =  file  .  getAbsolutePath  (  )  ; 
int   inicial  =  path  .  indexOf  (  root  )  ; 
inicial  +=  root  .  length  (  )  ; 
inicial  +=  1  ; 
String   pathPlusClassName  =  path  .  substring  (  inicial  ,  path  .  length  (  )  )  ; 
String   classFullName  =  JavaFileUtil  .  extractJavaSuffix  (  pathPlusClassName  ,  JavaFileUtil  .  CLASS_SUFFIX  )  ; 
classFullName  =  JavaFileUtil  .  extractJavaSuffix  (  classFullName  ,  JavaFileUtil  .  JAVA_SUFFIX  )  ; 
classFullName  =  classFullName  .  replace  (  File  .  separatorChar  ,  '.'  )  ; 
return   classFullName  ; 
} 













public   static   String   getDigestRepresentation  (  File   fileToDigest  )  throws   UnableToDigestFileException  { 
MessageDigest   messageDigest  ; 
FileInputStream   inputStream  =  null  ; 
byte  [  ]  buffer  =  new   byte  [  8129  ]  ; 
int   numberOfBytes  ; 
byte  [  ]  digestValue  ; 
BASE64Encoder   encoder  ; 
String   fileHash  =  new   String  (  )  ; 
try  { 
messageDigest  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
inputStream  =  new   FileInputStream  (  fileToDigest  .  getAbsoluteFile  (  )  )  ; 
numberOfBytes  =  inputStream  .  read  (  buffer  )  ; 
while  (  numberOfBytes  !=  -  1  )  { 
messageDigest  .  update  (  buffer  ,  0  ,  numberOfBytes  )  ; 
numberOfBytes  =  inputStream  .  read  (  buffer  )  ; 
} 
digestValue  =  messageDigest  .  digest  (  )  ; 
encoder  =  new   BASE64Encoder  (  )  ; 
fileHash  =  encoder  .  encode  (  digestValue  )  ; 
}  catch  (  IOException   exception  )  { 
throw   new   UnableToDigestFileException  (  fileToDigest  .  getAbsolutePath  (  )  ,  exception  )  ; 
}  catch  (  NoSuchAlgorithmException   exception  )  { 
throw   new   UnableToDigestFileException  (  fileToDigest  .  getAbsolutePath  (  )  ,  exception  )  ; 
}  finally  { 
if  (  inputStream  !=  null  )  { 
try  { 
inputStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   fileHash  ; 
} 








public   static   boolean   isAbsolutePath  (  String   filepath  )  { 
if  (  (  filepath  .  indexOf  (  ":\\"  )  !=  -  1  )  ||  filepath  .  charAt  (  0  )  ==  '\\'  )  { 
return   true  ; 
} 
return  (  new   File  (  filepath  )  )  .  isAbsolute  (  )  ; 
} 











public   static   String   getTranslatedFilePath  (  String   filepath  ,  Map   gumAttributesMap  )  { 
String   os  =  (  String  )  gumAttributesMap  .  get  (  WorkerConfiguration  .  ATT_OS  )  ; 
if  (  os  !=  null  &&  os  .  equals  (  WorkerConfiguration  .  OS_WINDOWS  )  )  { 
return  (  Win32Executor  .  convert2WinStyle  (  filepath  )  )  ; 
} 
return   filepath  ; 
} 







public   static   boolean   deleteDir  (  File   dir  )  { 
if  (  dir  .  isDirectory  (  )  &&  dir  .  exists  (  )  )  { 
String  [  ]  children  =  dir  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
boolean   success  =  deleteDir  (  new   File  (  dir  ,  children  [  i  ]  )  )  ; 
if  (  !  success  )  { 
return   false  ; 
} 
} 
} 
return   dir  .  delete  (  )  ; 
} 







public   static   boolean   deleteDir  (  String   string  )  { 
return   deleteDir  (  new   File  (  string  )  )  ; 
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






public   static   void   copyDirectory  (  String   srcPath  ,  String   dstPath  )  throws   IOException  { 
copyDirectory  (  new   File  (  srcPath  )  ,  new   File  (  dstPath  )  )  ; 
} 







public   static   void   copyDirectory  (  File   srcPath  ,  File   dstPath  )  throws   IOException  { 
if  (  srcPath  .  isDirectory  (  )  )  { 
if  (  !  dstPath  .  exists  (  )  )  { 
dstPath  .  mkdir  (  )  ; 
} 
String   files  [  ]  =  srcPath  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
copyDirectory  (  new   File  (  srcPath  ,  files  [  i  ]  )  ,  new   File  (  dstPath  ,  files  [  i  ]  )  )  ; 
} 
}  else  { 
if  (  !  srcPath  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "File or directory does not exist."  )  ; 
}  else  { 
InputStream   in  =  new   FileInputStream  (  srcPath  )  ; 
OutputStream   out  =  new   FileOutputStream  (  dstPath  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 
} 
} 

public   static   boolean   setWritable  (  File   file  )  { 
try  { 
new   FilePermission  (  file  .  getAbsolutePath  (  )  ,  "write"  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 

public   static   boolean   setReadAndWrite  (  File   file  )  { 
return   file  .  setReadable  (  true  ,  false  )  &&  file  .  setWritable  (  true  ,  false  )  ; 
} 

public   static   boolean   setNonReadable  (  File   file  )  { 
return   file  .  setReadable  (  false  ,  false  )  ; 
} 

public   static   boolean   setReadable  (  File   file  )  { 
try  { 
new   FilePermission  (  file  .  getAbsolutePath  (  )  ,  "read"  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 

public   static   boolean   setExecutable  (  File   file  )  { 
try  { 
new   FilePermission  (  file  .  getAbsolutePath  (  )  ,  "execute"  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 








public   static   void   copyFile  (  String   sourceFile  ,  String   destFile  )  throws   IOException  { 
copyFile  (  new   File  (  sourceFile  )  ,  new   File  (  destFile  )  )  ; 
} 
} 

