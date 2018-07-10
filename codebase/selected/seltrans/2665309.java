package   org  .  aitools  .  util  .  resource  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Stack  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipException  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   org  .  aitools  .  util  .  runtime  .  DeveloperError  ; 
import   org  .  aitools  .  util  .  runtime  .  UserError  ; 
import   org  .  aitools  .  util  .  xml  .  Characters  ; 
import   org  .  apache  .  log4j  .  Logger  ; 




public   class   Filesystem  { 


private   static   URL   root  ; 


private   static   Logger   LOGGER  =  Logger  .  getLogger  (  "programd"  )  ; 


private   static   Stack  <  URL  >  workingDirectory  =  new   Stack  <  URL  >  (  )  ; 


public   static   final   String   FILE  =  "file"  ; 


static  { 
try  { 
workingDirectory  .  add  (  URLTools  .  createValidURL  (  System  .  getProperty  (  "user.dir"  )  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   DeveloperError  (  "Current working directory (according to system properties) does not exist!"  ,  e  )  ; 
} 
} 

private   static   final   String   SLASH  =  "/"  ; 






public   static   void   setRootPath  (  URL   url  )  { 
root  =  url  ; 
workingDirectory  .  push  (  url  )  ; 
} 






public   static   URL   getRootPath  (  )  { 
return   root  ; 
} 








public   static   File   getBestFile  (  String   path  )  { 
File   file  =  new   File  (  URLTools  .  unescape  (  path  )  )  ; 
try  { 
return   file  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
return   file  .  getAbsoluteFile  (  )  ; 
} 
} 









public   static   File   getExistingFile  (  String   path  )  throws   FileNotFoundException  { 
File   file  =  getBestFile  (  path  )  ; 
if  (  !  file  .  exists  (  )  )  { 
file  =  getBestFile  (  workingDirectory  .  peek  (  )  .  getPath  (  )  +  path  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  String  .  format  (  "Couldn't find \"%s\"."  ,  path  )  )  ; 
} 
} 
try  { 
return   file  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   DeveloperError  (  String  .  format  (  "I/O Error creating the canonical form of file \"%s\"."  ,  path  )  ,  e  )  ; 
} 
} 







public   static   File   getExistingDirectory  (  String   path  )  { 
File   file  =  getBestFile  (  path  )  ; 
if  (  !  file  .  exists  (  )  )  { 
file  =  getBestFile  (  workingDirectory  .  peek  (  )  .  getPath  (  )  +  path  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   DeveloperError  (  String  .  format  (  "Couldn't find \"%s\"."  ,  path  )  ,  new   FileNotFoundException  (  path  )  )  ; 
} 
} 
try  { 
if  (  !  file  .  isDirectory  (  )  )  { 
throw   new   DeveloperError  (  String  .  format  (  "Could not find directory \"%s\"."  ,  path  )  ,  new   FileAlreadyExistsAsFileException  (  file  )  )  ; 
} 
return   file  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   DeveloperError  (  String  .  format  (  "I/O Error creating the canonical form of file \"%s\"."  ,  path  )  ,  e  )  ; 
} 
} 









public   static   FileInputStream   getFileInputStream  (  String   path  )  throws   FileNotFoundException  { 
File   file  =  getExistingFile  (  path  )  ; 
return   new   FileInputStream  (  file  )  ; 
} 









public   static   FileOutputStream   getFileOutputStream  (  String   path  )  throws   FileNotFoundException  { 
File   file  =  getExistingFile  (  path  )  ; 
return   new   FileOutputStream  (  file  )  ; 
} 










public   static   FileWriter   getFileWriter  (  String   path  ,  boolean   append  )  throws   IOException  { 
File   file  =  getBestFile  (  path  )  ; 
return   new   FileWriter  (  file  ,  append  )  ; 
} 









public   static   String   getAbsolutePath  (  String   path  )  throws   FileNotFoundException  { 
File   file  =  new   File  (  path  )  ; 
if  (  file  .  isAbsolute  (  )  )  { 
return   file  .  getAbsolutePath  (  )  ; 
} 
file  =  new   File  (  root  .  getPath  (  )  +  path  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  String  .  format  (  "Could not find \"%s\"."  ,  path  )  )  ; 
} 
return   file  .  getAbsolutePath  (  )  ; 
} 










public   static   File   checkOrCreate  (  String   path  ,  String   description  )  { 
File   file  =  getBestFile  (  path  )  ; 
if  (  file  .  exists  (  )  )  { 
return   file  ; 
} 
String   _description  =  description  ; 
if  (  _description  ==  null  )  { 
_description  =  "file"  ; 
} 
try  { 
file  .  createNewFile  (  )  ; 
}  catch  (  IOException   e  )  { 
File   directory  =  file  .  getParentFile  (  )  ; 
if  (  directory  !=  null  )  { 
if  (  directory  .  mkdirs  (  )  )  { 
try  { 
file  .  createNewFile  (  )  ; 
}  catch  (  IOException   ee  )  { 
throw   new   UserError  (  String  .  format  (  "Could not create %s \"%s\"."  ,  _description  ,  path  )  ,  new   CouldNotCreateFileException  (  file  .  getAbsolutePath  (  )  )  )  ; 
} 
}  else  { 
throw   new   UserError  (  String  .  format  (  "Could not create %s directory \"%s\"."  ,  _description  ,  directory  .  getAbsolutePath  (  )  )  ,  new   CouldNotCreateFileException  (  directory  .  getAbsolutePath  (  )  )  )  ; 
} 
}  else  { 
throw   new   UserError  (  String  .  format  (  "Could not create %s directory for \"%s\"."  ,  _description  ,  file  .  getAbsolutePath  (  )  )  ,  new   CouldNotCreateFileException  (  file  .  getAbsolutePath  (  )  )  )  ; 
} 
} 
assert   file  .  exists  (  )  ; 
LOGGER  .  info  (  String  .  format  (  "Created new %s \"%s\"."  ,  _description  ,  file  .  getAbsolutePath  (  )  )  )  ; 
return   file  ; 
} 











public   static   PrintWriter   checkOrCreatePrintWriter  (  String   path  ,  String   description  )  { 
PrintWriter   out  =  null  ; 
try  { 
out  =  new   PrintWriter  (  checkOrCreate  (  path  ,  description  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   UserError  (  String  .  format  (  "Could not find just-created %s \"%s\"."  ,  description  ,  path  )  ,  e  )  ; 
} 
return   out  ; 
} 











public   static   File   checkOrCreateDirectory  (  String   path  ,  String   description  )  { 
File   file  =  getBestFile  (  path  )  ; 
if  (  file  .  exists  (  )  )  { 
if  (  !  file  .  isDirectory  (  )  )  { 
throw   new   UserError  (  new   FileAlreadyExistsAsFileException  (  file  )  )  ; 
} 
return   file  ; 
} 
String   _description  =  description  ; 
if  (  _description  ==  null  )  { 
_description  =  "file"  ; 
} 
if  (  !  file  .  mkdirs  (  )  )  { 
throw   new   UserError  (  String  .  format  (  "Could not create %s directory at \"%s\"."  ,  _description  ,  path  )  ,  new   CouldNotCreateFileException  (  file  .  getAbsolutePath  (  )  )  )  ; 
} 
LOGGER  .  debug  (  String  .  format  (  "Created new %s \"%s\"."  ,  _description  ,  path  )  )  ; 
return   file  ; 
} 







public   static   String   getFileContents  (  String   path  )  { 
BufferedReader   buffReader  =  null  ; 
InputStream   stream  =  null  ; 
if  (  path  .  indexOf  (  "://"  )  !=  -  1  )  { 
URL   url  =  null  ; 
try  { 
url  =  new   URL  (  path  )  ; 
}  catch  (  MalformedURLException   e  )  { 
LOGGER  .  warn  (  String  .  format  (  "Malformed URL: \"%s\""  ,  path  )  )  ; 
} 
if  (  url  ==  null  )  { 
throw   new   DeveloperError  (  String  .  format  (  "Cannot create URL from path: \"%s\""  ,  path  )  ,  new   NullPointerException  (  )  )  ; 
} 
try  { 
String   encoding  =  Characters  .  getDeclaredXMLEncoding  (  url  )  ; 
stream  =  url  .  openStream  (  )  ; 
buffReader  =  new   BufferedReader  (  new   InputStreamReader  (  stream  ,  encoding  )  )  ; 
}  catch  (  IOException   e  )  { 
LOGGER  .  warn  (  String  .  format  (  "I/O error trying to read \"%s\""  ,  path  )  )  ; 
} 
}  else  { 
File   toRead  =  null  ; 
try  { 
toRead  =  getExistingFile  (  path  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   UserError  (  new   FileNotFoundException  (  path  )  )  ; 
} 
if  (  toRead  .  isAbsolute  (  )  )  { 
String   parent  =  toRead  .  getParent  (  )  ; 
try  { 
workingDirectory  .  push  (  URLTools  .  createValidURL  (  parent  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   DeveloperError  (  String  .  format  (  "Created an invalid parent file: \"%s\"."  ,  parent  )  ,  e  )  ; 
} 
} 
if  (  toRead  .  exists  (  )  &&  !  toRead  .  isDirectory  (  )  )  { 
String   _path  =  toRead  .  getAbsolutePath  (  )  ; 
try  { 
String   encoding  =  Characters  .  getDeclaredXMLEncoding  (  URLTools  .  createValidURL  (  _path  )  )  ; 
stream  =  new   FileInputStream  (  _path  )  ; 
buffReader  =  new   BufferedReader  (  new   InputStreamReader  (  stream  ,  encoding  )  )  ; 
}  catch  (  IOException   e  )  { 
LOGGER  .  warn  (  String  .  format  (  "I/O error trying to read \"%s\""  ,  _path  )  )  ; 
return   null  ; 
} 
}  else  { 
assert   toRead  .  exists  (  )  :  "getExistingFile() returned a non-existent file"  ; 
if  (  toRead  .  isDirectory  (  )  )  { 
throw   new   UserError  (  new   FileAlreadyExistsAsDirectoryException  (  toRead  )  )  ; 
} 
} 
} 
StringBuilder   result  =  new   StringBuilder  (  )  ; 
String   line  ; 
if  (  buffReader  !=  null  &&  stream  !=  null  )  { 
try  { 
while  (  (  line  =  buffReader  .  readLine  (  )  )  !=  null  )  { 
result  .  append  (  line  )  ; 
} 
buffReader  .  close  (  )  ; 
stream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
LOGGER  .  warn  (  String  .  format  (  "I/O error trying to read \"%s\""  ,  path  )  )  ; 
return   null  ; 
} 
} 
return   result  .  toString  (  )  ; 
} 










public   static   List  <  File  >  glob  (  String   path  )  throws   FileNotFoundException  { 
return   glob  (  path  ,  workingDirectory  .  peek  (  )  .  getPath  (  )  )  ; 
} 












public   static   List  <  File  >  glob  (  String   path  ,  String   workingDirectoryToUse  )  throws   FileNotFoundException  { 
int   wildCardIndex  =  path  .  indexOf  (  '*'  )  ; 
if  (  wildCardIndex  <  0  )  { 
List  <  File  >  list  =  new   ArrayList  <  File  >  (  1  )  ; 
list  .  add  (  new   File  (  path  )  )  ; 
return   list  ; 
} 
int   separatorIndex  =  path  .  lastIndexOf  (  File  .  separatorChar  )  ; 
if  (  separatorIndex  <  0  )  { 
separatorIndex  =  path  .  lastIndexOf  (  '\\'  )  ; 
if  (  separatorIndex  <  0  )  { 
separatorIndex  =  path  .  lastIndexOf  (  '/'  )  ; 
if  (  separatorIndex  <  0  )  { 
separatorIndex  =  path  .  lastIndexOf  (  ':'  )  ; 
} 
} 
} 
if  (  separatorIndex  >  wildCardIndex  )  { 
throw   new   FileNotFoundException  (  String  .  format  (  "Cannot expand %s"  ,  path  )  )  ; 
} 
String   pattern  ; 
String   dirName  ; 
File   dir  =  null  ; 
if  (  separatorIndex  >=  0  )  { 
pattern  =  path  .  substring  (  separatorIndex  +  1  )  ; 
dirName  =  URLTools  .  unescape  (  path  .  substring  (  0  ,  separatorIndex  +  1  )  )  ; 
if  (  !  dirName  .  startsWith  (  File  .  separator  )  &&  !  dirName  .  startsWith  (  SLASH  )  )  { 
dir  =  new   File  (  workingDirectory  .  peek  (  )  .  getPath  (  )  +  dirName  )  ; 
}  else  { 
dir  =  new   File  (  dirName  )  ; 
} 
}  else  { 
pattern  =  path  ; 
dirName  =  URLTools  .  unescape  (  workingDirectoryToUse  )  ; 
dir  =  new   File  (  dirName  )  ; 
try  { 
dir  =  dir  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   DeveloperError  (  String  .  format  (  "Could not get canonical file for \"%s\"."  ,  dir  .  getAbsolutePath  (  )  )  ,  e  )  ; 
} 
} 
if  (  !  dir  .  isDirectory  (  )  )  { 
throw   new   FileNotFoundException  (  String  .  format  (  "\"%s\" is not a valid directory path!"  ,  dirName  )  )  ; 
} 
String  [  ]  files  =  dir  .  list  (  new   WildCardFilter  (  pattern  ,  '*'  )  )  ; 
if  (  files  ==  null  )  { 
return   new   ArrayList  <  File  >  (  )  ; 
} 
List  <  File  >  list  =  new   ArrayList  <  File  >  (  files  .  length  )  ; 
for  (  int   i  =  files  .  length  ;  --  i  >=  0  ;  )  { 
list  .  add  (  new   File  (  dirName  +  files  [  i  ]  )  )  ; 
} 
return   list  ; 
} 






@  SuppressWarnings  (  "boxing"  ) 
public   static   void   pushWorkingDirectory  (  URL   path  )  { 
workingDirectory  .  push  (  path  )  ; 
if  (  LOGGER  .  isDebugEnabled  (  )  )  { 
LOGGER  .  debug  (  String  .  format  (  "Pushed working directory \"%s\".  Stack size now: %,d"  ,  path  ,  workingDirectory  .  size  (  )  )  )  ; 
} 
} 




@  SuppressWarnings  (  "boxing"  ) 
public   static   void   popWorkingDirectory  (  )  { 
URL   popped  ; 
if  (  workingDirectory  .  size  (  )  >  1  )  { 
popped  =  workingDirectory  .  pop  (  )  ; 
if  (  LOGGER  .  isDebugEnabled  (  )  )  { 
LOGGER  .  debug  (  String  .  format  (  "Popped working directory \"%s\".  Stack size now: %,d"  ,  popped  ,  workingDirectory  .  size  (  )  )  )  ; 
} 
}  else  { 
if  (  LOGGER  .  isDebugEnabled  (  )  )  { 
LOGGER  .  debug  (  "No more working directories to pop."  )  ; 
} 
} 
} 






public   static   URL   getWorkingDirectory  (  )  { 
return   workingDirectory  .  peek  (  )  ; 
} 








public   static   String   loadFileAsString  (  String   path  )  throws   FileNotFoundException  { 
return   loadFileAsString  (  getExistingFile  (  path  )  )  ; 
} 







public   static   String   loadFileAsString  (  File   file  )  { 
String   templateLine  ; 
StringBuilder   result  =  new   StringBuilder  (  1000  )  ; 
BufferedReader   reader  ; 
try  { 
String   encoding  =  Characters  .  getDeclaredXMLEncoding  (  file  .  toURI  (  )  .  toURL  (  )  )  ; 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  new   FileInputStream  (  file  )  ,  encoding  )  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
try  { 
while  (  (  templateLine  =  reader  .  readLine  (  )  )  !=  null  )  { 
result  .  append  (  templateLine  )  ; 
} 
reader  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   UserError  (  String  .  format  (  "I/O error reading \"%s\"."  ,  file  .  getAbsolutePath  (  )  )  ,  e  )  ; 
} 
return   result  .  toString  (  )  ; 
} 






public   static   void   deleteDirectoryContents  (  File   directory  )  { 
File  [  ]  contents  =  directory  .  listFiles  (  )  ; 
for  (  int   index  =  0  ;  index  <  contents  .  length  ;  index  ++  )  { 
File   file  =  contents  [  index  ]  ; 
if  (  file  .  isDirectory  (  )  )  { 
deleteDirectoryContents  (  file  )  ; 
file  .  delete  (  )  ; 
}  else  { 
file  .  delete  (  )  ; 
} 
} 
} 








public   static   void   addToZip  (  ZipOutputStream   out  ,  URL   path  ,  String   omitPrefix  )  { 
addToZip  (  out  ,  path  ,  omitPrefix  ,  null  )  ; 
} 









public   static   void   addToZip  (  ZipOutputStream   out  ,  URL   path  ,  String   omitPrefix  ,  Logger   logger  )  { 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
try  { 
InputStream   in  =  path  .  openStream  (  )  ; 
out  .  putNextEntry  (  new   ZipEntry  (  URLTools  .  unescape  (  path  .  getPath  (  )  .  replace  (  omitPrefix  ,  ""  )  )  )  )  ; 
for  (  int   length  =  0  ;  (  length  =  in  .  read  (  buffer  )  )  >  0  ;  )  { 
out  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
out  .  closeEntry  (  )  ; 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  !=  null  &&  e   instanceof   ZipException  )  { 
logger  .  warn  (  e  )  ; 
return  ; 
} 
throw   new   RuntimeException  (  "Error adding file to zip file."  ,  e  )  ; 
} 
} 








public   static   boolean   move  (  URL   source  ,  URL   destination  )  { 
File   src  =  new   File  (  source  .  getPath  (  )  )  ; 
if  (  !  src  .  exists  (  )  ||  !  src  .  canWrite  (  )  )  { 
return   false  ; 
} 
File   dest  =  new   File  (  destination  .  getPath  (  )  )  ; 
if  (  dest  .  exists  (  )  )  { 
return   false  ; 
} 
return   src  .  renameTo  (  dest  )  ; 
} 
} 

