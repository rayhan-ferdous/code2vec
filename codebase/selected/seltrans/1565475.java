package   com  .  aptana  .  ide  .  core  ; 

import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  security  .  AccessController  ; 
import   java  .  security  .  PrivilegedAction  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Map  ; 
import   org  .  eclipse  .  core  .  runtime  .  Path  ; 
import   org  .  eclipse  .  core  .  runtime  .  Platform  ; 
import   com  .  aptana  .  ide  .  internal  .  core  .  CoreNatives  ; 




public   class   FileUtils  { 




public   static   String   NEW_LINE  =  System  .  getProperty  (  "line.separator"  )  ; 




private   static   final   char   ALT_SEPARATOR_CHAR  =  File  .  separatorChar  ==  '/'  ?  '\\'  :  '/'  ; 




private   static   Hashtable   fileDirectoryStatus  =  new   Hashtable  (  )  ; 




public   static   String   systemTempDir  =  getTempDir  (  )  ; 




protected   FileUtils  (  )  { 
} 















public   static   boolean   copy  (  String   from  ,  String   to  ,  String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 















public   static   boolean   copy  (  File   from  ,  File   to  ,  String   what  )  { 
return   copy  (  new   File  (  from  ,  what  )  ,  new   File  (  to  ,  what  )  )  ; 
} 














public   static   final   boolean   isEmpty  (  Object   data  )  { 
if  (  data  ==  null  )  { 
return   true  ; 
} 
if  (  data   instanceof   Collection  )  { 
return  (  (  Collection  )  data  )  .  isEmpty  (  )  ; 
} 
if  (  data   instanceof   Map  )  { 
return  (  (  Map  )  data  )  .  isEmpty  (  )  ; 
} 
if  (  data   instanceof   Object  [  ]  )  { 
return  (  (  Object  [  ]  )  data  )  .  length  ==  0  ; 
} 
return  (  data  .  toString  (  )  .  length  (  )  ==  0  )  ||  "null"  .  equals  (  data  .  toString  (  )  )  ; 
} 













public   static   boolean   copy  (  String   from  ,  String   to  )  { 
return   copy  (  new   File  (  from  )  ,  new   File  (  to  )  )  ; 
} 













public   static   boolean   copy  (  File   from  ,  File   to  )  { 
if  (  from  .  isDirectory  (  )  )  { 
String  [  ]  contents  =  from  .  list  (  )  ; 
for  (  int   i  =  0  ;  contents  !=  null  &&  i  <  contents  .  length  ;  i  ++  )  { 
copy  (  from  ,  to  ,  contents  [  i  ]  )  ; 
} 
}  else  { 
try  { 
OutputStream   os  =  makeFile  (  to  )  ; 
InputStream   is  =  new   FileInputStream  (  from  )  ; 
pipe  (  is  ,  os  ,  false  )  ; 
is  .  close  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
return   false  ; 
} 
} 
long   time  =  from  .  lastModified  (  )  ; 
if  (  !  to  .  setLastModified  (  time  )  )  { 
return   false  ; 
} 
long   newtime  =  to  .  lastModified  (  )  ; 
return   time  ==  newtime  ; 
} 


















public   static   void   pipe  (  InputStream   in  ,  OutputStream   out  ,  boolean   isBlocking  ,  ByteFilter   filter  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  50000  ]  ; 
int   nread  ; 
int   navailable  ; 
int   total  =  0  ; 
synchronized  (  in  )  { 
navailable  =  isBlocking  ?  buf  .  length  :  in  .  available  (  )  ; 
nread  =  in  .  read  (  buf  ,  0  ,  Math  .  min  (  buf  .  length  ,  navailable  )  )  ; 
while  (  navailable  >  0  &&  nread  >=  0  )  { 
if  (  filter  ==  null  )  { 
out  .  write  (  buf  ,  0  ,  nread  )  ; 
}  else  { 
byte  [  ]  filtered  =  filter  .  filter  (  buf  ,  nread  )  ; 
out  .  write  (  filtered  )  ; 
} 
total  +=  nread  ; 
navailable  =  isBlocking  ?  buf  .  length  :  in  .  available  (  )  ; 
nread  =  in  .  read  (  buf  ,  0  ,  Math  .  min  (  buf  .  length  ,  navailable  )  )  ; 
} 
} 
out  .  flush  (  )  ; 
buf  =  null  ; 
} 








public   interface   ByteFilter  { 










byte  [  ]  filter  (  byte  [  ]  input  ,  int   length  )  ; 
} 















public   static   void   pipe  (  InputStream   in  ,  OutputStream   out  ,  boolean   isBlocking  )  throws   IOException  { 
pipe  (  in  ,  out  ,  isBlocking  ,  null  )  ; 
} 











public   static   boolean   pipe  (  Reader   in  ,  Writer   out  )  { 
if  (  in  ==  null  )  { 
return   false  ; 
} 
if  (  out  ==  null  )  { 
return   false  ; 
} 
try  { 
int   c  ; 
synchronized  (  in  )  { 
c  =  in  .  read  (  )  ; 
while  (  in  .  ready  (  )  &&  c  >  0  )  { 
out  .  write  (  c  )  ; 
c  =  in  .  read  (  )  ; 
} 
} 
out  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 
















public   static   FileOutputStream   makeFile  (  String   dirname  ,  String   filename  ,  boolean   append  )  throws   IOException  { 
if  (  !  isEmpty  (  dirname  )  )  { 
File   dir  =  new   File  (  dirname  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
if  (  dir  .  exists  (  )  )  { 
dir  .  delete  (  )  ; 
} 
dir  .  mkdirs  (  )  ; 
} 
} 
return   new   FileOutputStream  (  new   File  (  dirname  ,  filename  )  ,  append  )  ; 
} 













public   static   FileOutputStream   makeFile  (  String   dir  ,  String   filename  )  throws   IOException  { 
return   makeFile  (  dir  ,  filename  ,  false  )  ; 
} 














public   static   FileOutputStream   makeFile  (  String  [  ]  path  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  path  [  0  ]  ,  path  [  1  ]  ,  append  )  ; 
} 











public   static   FileOutputStream   makeFile  (  String  [  ]  path  )  throws   IOException  { 
return   makeFile  (  path  [  0  ]  ,  path  [  1  ]  )  ; 
} 














public   static   FileOutputStream   makeFile  (  String   path  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  ,  append  )  ; 
} 











public   static   FileOutputStream   makeFile  (  String   path  )  throws   IOException  { 
return   makeFile  (  splitPath  (  path  )  )  ; 
} 














public   static   String  [  ]  splitPath  (  String   path  )  { 
return   new   String  [  ]  {  dirname  (  path  )  ,  new   File  (  path  )  .  getName  (  )  }  ; 
} 














public   static   FileOutputStream   makeFile  (  File   file  ,  boolean   append  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  ,  append  )  ; 
} 















public   static   String   dirname  (  File   file  )  { 
String   parent  =  file  .  getParent  (  )  ; 
if  (  parent  ==  null  )  { 
parent  =  "."  ; 
} 
if  (  file  .  getPath  (  )  .  indexOf  (  File  .  separatorChar  )  <  0  &&  file  .  getPath  (  )  .  indexOf  (  ALT_SEPARATOR_CHAR  )  >=  0  &&  parent  .  indexOf  (  File  .  separatorChar  )  >=  0  )  { 
parent  =  parent  .  replace  (  File  .  separatorChar  ,  ALT_SEPARATOR_CHAR  )  ; 
} 
return   parent  ; 
} 















public   static   String   dirname  (  String   path  )  { 
String   dirname  =  dirname  (  new   File  (  path  )  )  ; 
if  (  path  .  indexOf  (  ALT_SEPARATOR_CHAR  )  >=  0  &&  path  .  indexOf  (  File  .  separatorChar  )  <  0  )  { 
return   dirname  .  replace  (  File  .  separatorChar  ,  ALT_SEPARATOR_CHAR  )  ; 
} 
return   dirname  ; 
} 











public   static   FileOutputStream   makeFile  (  File   file  )  throws   IOException  { 
return   makeFile  (  file  .  getCanonicalPath  (  )  )  ; 
} 













public   static   final   OutputStreamWriter   makeFileWriter  (  String   path  ,  String   encoding  )  throws   IOException  { 
return   new   OutputStreamWriter  (  makeFile  (  path  )  ,  encoding  )  ; 
} 








public   static   File  [  ]  getFilesInDirectory  (  File   file  )  { 
Path   path  =  new   Path  (  file  .  toString  (  )  )  ; 
String   lastSegment  =  path  .  lastSegment  (  )  ; 
File  [  ]  files  =  new   File  [  0  ]  ; 
if  (  file  .  isDirectory  (  )  )  { 
files  =  file  .  listFiles  (  )  ; 
}  else  { 
File   parent  =  file  .  getParentFile  (  )  ; 
files  =  parent  .  listFiles  (  )  ; 
} 
if  (  lastSegment  !=  null  &&  lastSegment  .  indexOf  (  '*'  )  >=  0  )  { 
return   matchFiles  (  lastSegment  ,  files  )  ; 
}  else  { 
return   files  ; 
} 
} 











public   static   File  [  ]  matchFiles  (  String   pattern  ,  File  [  ]  files  )  { 
String   newPattern  =  StringUtils  .  replace  (  pattern  ,  "\\"  ,  "\\\\"  )  ; 
newPattern  =  StringUtils  .  replace  (  newPattern  ,  "."  ,  "\\."  )  ; 
newPattern  =  StringUtils  .  replace  (  newPattern  ,  "*"  ,  ".*"  )  ; 
ArrayList   al  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   fileTest  =  files  [  i  ]  ; 
if  (  fileTest  .  toString  (  )  .  matches  (  newPattern  )  )  { 
al  .  add  (  fileTest  )  ; 
} 
} 
return  (  File  [  ]  )  al  .  toArray  (  new   File  [  0  ]  )  ; 
} 










public   static   String   makeFilePathRelative  (  File   fileA  ,  File   fileB  )  { 
String   separator  =  System  .  getProperty  (  "file.separator"  )  ; 
String   a  =  fileA  .  toString  (  )  ; 
if  (  !  fileA  .  isDirectory  (  )  )  { 
a  =  fileA  .  getParent  (  )  .  toString  (  )  +  separator  ; 
} 
String   b  =  fileB  .  toString  (  )  ; 
if  (  fileB  .  isDirectory  (  )  )  { 
b  =  b  +  separator  ; 
} 
String   r  =  StringUtils  .  replace  (  b  ,  a  ,  StringUtils  .  EMPTY  )  ; 
if  (  r  .  endsWith  (  separator  )  )  { 
r  =  r  .  substring  (  0  ,  r  .  length  (  )  -  1  )  ; 
} 
return   r  ; 
} 








public   static   String   getExtension  (  String   fileName  )  { 
if  (  fileName  ==  null  ||  StringUtils  .  EMPTY  .  equals  (  fileName  )  )  { 
return   fileName  ; 
} 
int   index  =  fileName  .  indexOf  (  '.'  )  ; 
if  (  index  ==  -  1  )  { 
return   StringUtils  .  EMPTY  ; 
} 
if  (  index  ==  fileName  .  length  (  )  )  { 
return   StringUtils  .  EMPTY  ; 
} 
return   fileName  .  substring  (  index  +  1  ,  fileName  .  length  (  )  )  ; 
} 








public   static   String   stripExtension  (  String   fileName  )  { 
if  (  fileName  ==  null  ||  StringUtils  .  EMPTY  .  equals  (  fileName  )  )  { 
return   fileName  ; 
} 
int   index  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  index  ==  -  1  )  { 
return   fileName  ; 
} 
if  (  index  ==  fileName  .  length  (  )  )  { 
return   fileName  ; 
} 
return   fileName  .  substring  (  0  ,  index  )  ; 
} 








public   static   boolean   isDirectory  (  File   f  )  { 
if  (  System  .  getProperty  (  "os.name"  )  .  startsWith  (  "Mac OS"  )  )  { 
return   f  .  isDirectory  (  )  ; 
}  else  { 
String   filePath  =  f  .  getAbsolutePath  (  )  ; 
if  (  fileDirectoryStatus  .  containsKey  (  filePath  )  )  { 
return   fileDirectoryStatus  .  get  (  filePath  )  .  equals  (  Boolean  .  TRUE  )  ; 
}  else  { 
File   fShell  =  FileTricks  .  attemptReplaceWithShellFolder  (  f  )  ; 
boolean   isDirectory  =  fShell  .  isDirectory  (  )  ; 
if  (  isDirectory  )  { 
fileDirectoryStatus  .  put  (  filePath  ,  Boolean  .  TRUE  )  ; 
}  else  { 
fileDirectoryStatus  .  put  (  filePath  ,  Boolean  .  FALSE  )  ; 
} 
return   isDirectory  ; 
} 
} 
} 

private   static   String   getTempDir  (  )  { 
if  (  systemTempDir  ==  null  )  { 
PrivilegedAction   pa  =  new   java  .  security  .  PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   System  .  getProperty  (  "java.io.tmpdir"  )  ; 
} 
}  ; 
systemTempDir  =  (  (  String  )  AccessController  .  doPrivileged  (  pa  )  )  ; 
} 
return   systemTempDir  ; 
} 







public   static   String   ensureExtension  (  String   extension  )  { 
if  (  extension  ==  null  ||  StringUtils  .  EMPTY  .  equals  (  extension  )  )  { 
return   extension  ; 
}  else  { 
if  (  extension  .  startsWith  (  "."  )  )  { 
return   extension  ; 
}  else  { 
return  "."  +  extension  ; 
} 
} 
} 







public   static   String   stripExtensionPeriod  (  String   extension  )  { 
if  (  extension  ==  null  ||  StringUtils  .  EMPTY  .  equals  (  extension  )  )  { 
return   extension  ; 
}  else  { 
if  (  extension  .  startsWith  (  "."  )  )  { 
return   extension  .  substring  (  1  )  ; 
}  else  { 
return   extension  ; 
} 
} 
} 












public   static   String   compressPath  (  String   path  ,  int   pathLength  )  { 
path  =  path  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  path  .  length  (  )  >  pathLength  )  { 
int   firstSlash  =  path  .  indexOf  (  '/'  ,  1  )  ; 
int   endSearch  =  path  .  length  (  )  -  pathLength  -  firstSlash  ; 
if  (  endSearch  <  0  )  { 
return   path  ; 
}  else  { 
int   lastSlash  =  path  .  indexOf  (  '/'  ,  endSearch  )  ; 
if  (  lastSlash  >  firstSlash  )  { 
return   path  .  substring  (  0  ,  firstSlash  )  +  "/..."  +  path  .  substring  (  lastSlash  )  ; 
}  else  { 
lastSlash  =  path  .  lastIndexOf  (  '/'  ,  path  .  length  (  )  -  2  )  ; 
return   path  .  substring  (  0  ,  firstSlash  )  +  "/..."  +  path  .  substring  (  lastSlash  )  ; 
} 
} 
}  else  { 
return   path  ; 
} 
} 







public   static   String   getRandomFileName  (  String   prefix  ,  String   suffix  )  { 
if  (  suffix  ==  null  )  { 
return   prefix  +  (  long  )  (  Integer  .  MAX_VALUE  *  Math  .  random  (  )  )  ; 
}  else  { 
return   prefix  +  (  long  )  (  Integer  .  MAX_VALUE  *  Math  .  random  (  )  )  +  suffix  ; 
} 
} 










public   static   boolean   deleteDirectory  (  String   parentDirectory  ,  String   directoryName  )  { 
File   newDirectory  =  new   File  (  parentDirectory  +  File  .  separator  +  directoryName  )  ; 
if  (  newDirectory  .  exists  (  )  )  { 
return   deleteDirectory  (  newDirectory  )  ; 
} 
return   false  ; 
} 








public   static   boolean   deleteDirectory  (  File   directory  )  { 
if  (  directory  .  isDirectory  (  )  )  { 
String  [  ]  children  =  directory  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
boolean   success  =  deleteDirectory  (  new   File  (  directory  ,  children  [  i  ]  )  )  ; 
if  (  !  success  )  { 
return   false  ; 
} 
} 
} 
return   directory  .  delete  (  )  ; 
} 







public   static   boolean   setHidden  (  File   file  )  { 
if  (  Platform  .  OS_WIN32  .  equals  (  Platform  .  getOS  (  )  )  )  { 
try  { 
return   CoreNatives  .  SetFileAttributes  (  file  .  getAbsolutePath  (  )  ,  CoreNatives  .  FILE_ATTRIBUTE_HIDDEN  ,  0  )  ; 
}  catch  (  UnsatisfiedLinkError   e  )  { 
IdeLog  .  logError  (  AptanaCorePlugin  .  getDefault  (  )  ,  Messages  .  FileUtils_CoreLibraryNotFound  ,  e  )  ; 
} 
} 
return   false  ; 
} 









public   static   String   readContent  (  File   file  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  file  )  ; 
return   StreamUtils  .  readContent  (  fis  ,  null  )  ; 
} 






public   static   boolean   isFileURL  (  URL   url  )  { 
if  (  url  ==  null  )  { 
return   false  ; 
} 
String   surl  =  url  .  toString  (  )  ; 
return   surl  .  startsWith  (  "file:/"  )  ; 
} 






public   static   File   urlToFile  (  URL   url  )  { 
if  (  isFileURL  (  url  )  )  { 
String   surl  =  url  .  toString  (  )  ; 
surl  =  StringUtils  .  replace  (  surl  ,  "file://"  ,  ""  )  ; 
surl  =  StringUtils  .  replace  (  surl  ,  "file:/"  ,  ""  )  ; 
surl  =  StringUtils  .  urlDecodeFilename  (  surl  .  toCharArray  (  )  )  ; 
File   f  =  new   File  (  surl  )  ; 
return   f  ; 
}  else  { 
return   null  ; 
} 
} 






public   static   File   openURL  (  String   uri  )  { 
URL   fileURL  =  uriToURL  (  uri  )  ; 
File   f  =  urlToFile  (  fileURL  )  ; 
if  (  f  !=  null  )  { 
return   f  ; 
}  else  { 
String   text  =  readContent  (  fileURL  )  ; 
String  [  ]  path  =  fileURL  .  getFile  (  )  .  split  (  "/"  )  ; 
String   name  =  path  [  path  .  length  -  1  ]  ; 
File   temp  ; 
try  { 
temp  =  File  .  createTempFile  (  FileUtils  .  stripExtension  (  name  )  ,  FileUtils  .  ensureExtension  (  FileUtils  .  getExtension  (  name  )  )  )  ; 
BufferedWriter   out  =  new   BufferedWriter  (  new   FileWriter  (  temp  )  )  ; 
out  .  write  (  text  )  ; 
out  .  close  (  )  ; 
return   temp  ; 
}  catch  (  IOException   e  )  { 
IdeLog  .  logError  (  AptanaCorePlugin  .  getDefault  (  )  ,  StringUtils  .  format  (  "Unable to open URL {0} as file"  ,  uri  )  ,  e  )  ; 
return   null  ; 
} 
} 
} 

public   static   URL   uriToURL  (  String   uri  )  { 
URI   uri2  ; 
String   encodedUri  ; 
try  { 
encodedUri  =  URLEncoder  .  encode  (  uri  ,  null  ,  null  )  ; 
uri2  =  new   URI  (  encodedUri  )  .  normalize  (  )  ; 
return   uri2  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   e  )  { 
IdeLog  .  logError  (  AptanaCorePlugin  .  getDefault  (  )  ,  StringUtils  .  format  (  "Unable to convert uri {0} to URL. Malformed"  ,  uri  )  ,  e  )  ; 
return   null  ; 
}  catch  (  URISyntaxException   e  )  { 
IdeLog  .  logError  (  AptanaCorePlugin  .  getDefault  (  )  ,  StringUtils  .  format  (  "Unable to convert uri {0} to URL. Syntax is incorrect"  ,  uri  )  ,  e  )  ; 
return   null  ; 
} 
} 






public   static   String   readContent  (  URL   url  )  { 
String   text  =  null  ; 
try  { 
if  (  isFileURL  (  url  )  )  { 
File   file  =  urlToFile  (  url  )  ; 
return   readContent  (  file  )  ; 
}  else  { 
InputStream   is  =  url  .  openStream  (  )  ; 
text  =  StreamUtils  .  readContent  (  is  ,  null  )  ; 
} 
}  catch  (  IOException   e  )  { 
IdeLog  .  logError  (  AptanaCorePlugin  .  getDefault  (  )  ,  "Unable to read content"  ,  e  )  ; 
} 
return   text  ; 
} 
} 

