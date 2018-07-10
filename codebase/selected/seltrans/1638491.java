package   de  .  fhg  .  igd  .  util  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLClassLoader  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  SortedSet  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  TreeSet  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   de  .  fhg  .  igd  .  io  .  Pipe  ; 































public   class   ClassSource  { 




protected   static   final   String   options_  =  "help:!,verbose:!,debug:!,hex:!,resource:!,search:s,"  +  "userclasspath:s,userclassresource:s,userclassurls:U["  ; 




public   static   final   String   ZIP_EXTENSION  =  ".zip"  ; 




public   static   final   String   JAR_EXTENSION  =  ".jar"  ; 




public   static   final   String   CLASS_EXTENSION  =  ".class"  ; 




public   static   final   String   RESOURCE_URL_PROTOCOL  =  "resource"  ; 




public   static   final   String   ARCHIVE_URL_PROTOCOL  =  "jar"  ; 




public   static   final   String   FILE_URL_PROTOCOL  =  "file"  ; 








public   static   int   SOURCE_NOT_FOUND  =  0x00000000  ; 








public   static   int   SOURCE_ALL  =  0x00ffffff  ; 
















public   static   int   SOURCE_SYSTEM  =  0x00010000  ; 
















public   static   int   SOURCE_USER  =  0x00020000  ; 














public   static   int   SOURCE_BOOT_CLASSPATH  =  0x00010001  ; 















public   static   int   SOURCE_SYSTEM_JARFILE  =  0x00010002  ; 














public   static   int   SOURCE_SYSTEM_CLASSPATH  =  0x00010004  ; 
















public   static   int   SOURCE_USER_LOCALCLASS  =  0x00020008  ; 














public   static   int   SOURCE_USER_CLASSRESOURCE  =  0x00020010  ; 














public   static   int   SOURCE_USER_REMOTECLASS  =  0x00020020  ; 





private   static   String  [  ]  bootClassPaths_  ; 





private   static   String  [  ]  systemJarFiles_  ; 





private   static   String  [  ]  systemClassPaths_  ; 






private   String  [  ]  userClassPaths_  ; 






private   URL  [  ]  userClassPathUrls_  ; 





private   Resource   userClassResource_  ; 





private   URL  [  ]  userClassUrls_  ; 






private   static   DummyClassLoader   dummyClassLoader_  ; 






static  { 
StringTokenizer   st  ; 
ArrayList   paths  ; 
String   systemClassPaths  ; 
String   bootClassPaths  ; 
String   systemExtDirs  ; 
String   path  ; 
File  [  ]  files  ; 
File   dir  ; 
int   n  ; 
bootClassPaths  =  System  .  getProperty  (  "sun.boot.class.path"  )  ; 
if  (  bootClassPaths  !=  null  )  { 
st  =  new   StringTokenizer  (  bootClassPaths  ,  File  .  pathSeparator  )  ; 
paths  =  new   ArrayList  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
path  =  canonicalClassPath  (  st  .  nextToken  (  )  )  ; 
paths  .  add  (  path  )  ; 
} 
bootClassPaths_  =  (  String  [  ]  )  paths  .  toArray  (  new   String  [  0  ]  )  ; 
}  else  { 
bootClassPaths_  =  new   String  [  0  ]  ; 
} 
systemExtDirs  =  System  .  getProperty  (  "java.ext.dirs"  )  ; 
if  (  systemExtDirs  !=  null  )  { 
st  =  new   StringTokenizer  (  systemExtDirs  ,  File  .  pathSeparator  )  ; 
paths  =  new   ArrayList  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
dir  =  new   File  (  st  .  nextToken  (  )  )  ; 
files  =  dir  .  listFiles  (  )  ; 
for  (  n  =  0  ;  files  !=  null  &&  n  <  files  .  length  ;  n  ++  )  { 
if  (  checkFileExtension  (  files  [  n  ]  .  getName  (  )  ,  ZIP_EXTENSION  )  ||  checkFileExtension  (  files  [  n  ]  .  getName  (  )  ,  JAR_EXTENSION  )  )  { 
path  =  canonicalClassPath  (  files  [  n  ]  .  getPath  (  )  )  ; 
paths  .  add  (  path  )  ; 
} 
} 
} 
systemJarFiles_  =  (  String  [  ]  )  paths  .  toArray  (  new   String  [  0  ]  )  ; 
}  else  { 
systemJarFiles_  =  new   String  [  0  ]  ; 
} 
systemClassPaths  =  System  .  getProperty  (  "java.class.path"  )  ; 
if  (  systemClassPaths  !=  null  )  { 
st  =  new   StringTokenizer  (  systemClassPaths  ,  File  .  pathSeparator  )  ; 
paths  =  new   ArrayList  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
path  =  canonicalClassPath  (  st  .  nextToken  (  )  )  ; 
paths  .  add  (  path  )  ; 
} 
systemClassPaths_  =  (  String  [  ]  )  paths  .  toArray  (  new   String  [  0  ]  )  ; 
}  else  { 
systemClassPaths_  =  new   String  [  0  ]  ; 
} 
dummyClassLoader_  =  new   DummyClassLoader  (  )  ; 
} 























public   static   String   canonicalClassPath  (  String   path  )  { 
String   canonicalPath  ; 
File   file  ; 
canonicalPath  =  path  ; 
try  { 
file  =  new   File  (  canonicalPath  )  ; 
canonicalPath  =  file  .  getCanonicalPath  (  )  ; 
}  catch  (  Throwable   t  )  { 
} 
canonicalPath  =  canonicalPath  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
if  (  !  canonicalPath  .  startsWith  (  "/"  )  )  { 
canonicalPath  =  "/"  +  canonicalPath  ; 
} 
return   canonicalPath  ; 
} 
















public   static   URL   canonicalClassUrl  (  String   path  )  { 
String   canonicalPath  ; 
URL   canonicalUrl  ; 
canonicalPath  =  canonicalClassPath  (  path  )  ; 
if  (  !  canonicalPath  .  endsWith  (  "/"  )  )  { 
if  (  !  checkFileExtension  (  canonicalPath  ,  JAR_EXTENSION  )  &&  !  checkFileExtension  (  canonicalPath  ,  ZIP_EXTENSION  )  )  { 
canonicalPath  =  canonicalPath  .  concat  (  "/"  )  ; 
} 
} 
try  { 
canonicalUrl  =  new   URL  (  FILE_URL_PROTOCOL  ,  null  ,  canonicalPath  )  ; 
return   canonicalUrl  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
} 


















public   static   URL   canonicalClassUrl  (  URL   url  )  { 
String   path  ; 
URL   canonicalUrl  ; 
canonicalUrl  =  url  ; 
if  (  url  .  getProtocol  (  )  .  equalsIgnoreCase  (  FILE_URL_PROTOCOL  )  )  { 
canonicalUrl  =  canonicalClassUrl  (  url  .  getFile  (  )  )  ; 
}  else  { 
path  =  url  .  toString  (  )  ; 
path  =  path  .  toLowerCase  (  )  ; 
if  (  !  path  .  endsWith  (  "/"  )  )  { 
if  (  !  path  .  endsWith  (  JAR_EXTENSION  )  &&  !  path  .  endsWith  (  ZIP_EXTENSION  )  )  { 
try  { 
canonicalUrl  =  new   URL  (  url  .  toString  (  )  .  concat  (  "/"  )  )  ; 
}  catch  (  Throwable   t  )  { 
} 
} 
} 
} 
return   canonicalUrl  ; 
} 














public   static   boolean   checkFileExtension  (  File   file  ,  String   extension  )  { 
if  (  file  ==  null  )  { 
return   false  ; 
} 
return   checkFileExtension  (  file  .  getPath  (  )  ,  extension  )  ; 
} 














public   static   boolean   checkFileExtension  (  String   file  ,  String   extension  )  { 
if  (  file  ==  null  ||  extension  ==  null  )  { 
return   false  ; 
} 
return   file  .  toLowerCase  (  )  .  endsWith  (  extension  .  toLowerCase  (  )  )  ; 
} 
















public   static   String   transformClassToFile  (  String   className  )  { 
String   classFile  ; 
if  (  className  ==  null  )  { 
return   null  ; 
} 
classFile  =  className  .  trim  (  )  ; 
classFile  =  classFile  .  replace  (  '.'  ,  '/'  )  .  concat  (  CLASS_EXTENSION  )  ; 
return   classFile  ; 
} 















public   static   String   transformFileToClass  (  String   classFile  )  { 
String   className  ; 
if  (  checkFileExtension  (  classFile  ,  CLASS_EXTENSION  )  )  { 
className  =  classFile  .  substring  (  0  ,  classFile  .  length  (  )  -  CLASS_EXTENSION  .  length  (  )  )  ; 
className  =  className  .  replace  (  '/'  ,  '.'  )  ; 
return   className  ; 
}  else  { 
return   null  ; 
} 
} 
































protected   static   SortedSet   getResourceNames0  (  String   prefix  ,  File   path  ,  boolean   onlyClasses  ,  boolean   inspectJars  )  { 
ZipInputStream   zipIn  ; 
ZipEntry   zipEntry  ; 
SortedSet   resources  ; 
SortedSet   set  ; 
String   file  ; 
File  [  ]  files  ; 
int   pos  ; 
int   i  ; 
resources  =  new   TreeSet  (  )  ; 
zipIn  =  null  ; 
if  (  path  .  isDirectory  (  )  )  { 
try  { 
files  =  path  .  listFiles  (  )  ; 
if  (  prefix  !=  null  )  { 
prefix  =  prefix  +  path  .  getName  (  )  +  "/"  ; 
}  else  { 
prefix  =  ""  ; 
} 
for  (  i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
set  =  getResourceNames0  (  prefix  ,  files  [  i  ]  ,  onlyClasses  ,  false  )  ; 
resources  .  addAll  (  set  )  ; 
} 
}  catch  (  Throwable   t  )  { 
} 
return   resources  ; 
} 
file  =  path  .  getName  (  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  ""  ; 
} 
if  (  inspectJars  &&  (  checkFileExtension  (  file  ,  ZIP_EXTENSION  )  ||  checkFileExtension  (  file  ,  JAR_EXTENSION  )  )  )  { 
try  { 
zipIn  =  new   ZipInputStream  (  new   FileInputStream  (  path  )  )  ; 
while  (  (  zipEntry  =  zipIn  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  zipEntry  .  isDirectory  (  )  )  { 
continue  ; 
} 
file  =  zipEntry  .  getName  (  )  ; 
if  (  onlyClasses  )  { 
if  (  checkFileExtension  (  file  ,  CLASS_EXTENSION  )  )  { 
resources  .  add  (  file  )  ; 
} 
}  else  { 
resources  .  add  (  file  )  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
}  finally  { 
try  { 
zipIn  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
}  else  { 
if  (  onlyClasses  )  { 
if  (  checkFileExtension  (  file  ,  CLASS_EXTENSION  )  )  { 
resources  .  add  (  prefix  +  file  )  ; 
} 
}  else  { 
resources  .  add  (  prefix  +  file  )  ; 
} 
} 
return   resources  ; 
} 















public   static   SortedSet   getResourceNames  (  File   path  ,  boolean   onlyClasses  )  { 
return   getResourceNames0  (  null  ,  path  ,  onlyClasses  ,  true  )  ; 
} 











public   static   boolean   checkSource  (  int   source  ,  int   sourcemask  )  { 
if  (  sourcemask  ==  SOURCE_NOT_FOUND  )  { 
return  (  source  ==  SOURCE_NOT_FOUND  )  ; 
} 
if  (  sourcemask  ==  SOURCE_ALL  )  { 
return  (  source  !=  SOURCE_NOT_FOUND  )  ; 
} 
return  (  (  source  &  sourcemask  )  ==  sourcemask  )  ; 
} 





















public   static   URL   systemClassUrl  (  String   className  )  { 
return   systemResourceUrl  (  transformClassToFile  (  className  )  )  ; 
} 





















public   static   URL   systemResourceUrl  (  String   resourceFile  )  { 
if  (  resourceFile  ==  null  )  { 
return   null  ; 
} 
return   ClassLoader  .  getSystemResource  (  resourceFile  )  ; 
} 



























public   static   int   systemClassSource  (  String   className  )  { 
return   systemResourceSource  (  transformClassToFile  (  className  )  )  ; 
} 



























public   static   int   systemResourceSource  (  String   resourceFile  )  { 
String   url  ; 
URL   resourceUrl  ; 
int   resourceFileLen  ; 
int   pos  ; 
int   i  ; 
resourceUrl  =  systemResourceUrl  (  resourceFile  )  ; 
if  (  resourceUrl  ==  null  )  { 
return   SOURCE_NOT_FOUND  ; 
} 
url  =  resourceUrl  .  toString  (  )  ; 
resourceFileLen  =  resourceFile  .  length  (  )  ; 
if  (  url  .  startsWith  (  ARCHIVE_URL_PROTOCOL  +  ":"  )  )  { 
url  =  url  .  substring  (  ARCHIVE_URL_PROTOCOL  .  length  (  )  +  1  )  ; 
} 
if  (  url  .  startsWith  (  FILE_URL_PROTOCOL  +  ":"  )  )  { 
url  =  url  .  substring  (  FILE_URL_PROTOCOL  .  length  (  )  +  1  )  ; 
} 
if  (  url  .  startsWith  (  "//"  )  )  { 
url  =  url  .  substring  (  2  )  ; 
} 
pos  =  url  .  indexOf  (  "!"  )  ; 
if  (  pos  !=  -  1  )  { 
url  =  url  .  substring  (  0  ,  pos  )  ; 
}  else  { 
url  =  url  .  substring  (  0  ,  url  .  length  (  )  -  (  resourceFileLen  +  1  )  )  ; 
} 
for  (  i  =  0  ;  i  <  bootClassPaths_  .  length  ;  i  ++  )  { 
if  (  url  .  equals  (  bootClassPaths_  [  i  ]  )  )  { 
return   SOURCE_BOOT_CLASSPATH  ; 
} 
} 
for  (  i  =  0  ;  i  <  systemJarFiles_  .  length  ;  i  ++  )  { 
if  (  url  .  equals  (  systemJarFiles_  [  i  ]  )  )  { 
return   SOURCE_SYSTEM_JARFILE  ; 
} 
} 
for  (  i  =  0  ;  i  <  systemClassPaths_  .  length  ;  i  ++  )  { 
if  (  url  .  equals  (  systemClassPaths_  [  i  ]  )  )  { 
return   SOURCE_SYSTEM_CLASSPATH  ; 
} 
} 
return   SOURCE_NOT_FOUND  ; 
} 













public   static   InputStream   systemClassInputStream  (  String   className  )  { 
return   systemResourceInputStream  (  transformClassToFile  (  className  )  )  ; 
} 














public   static   InputStream   systemResourceInputStream  (  String   resourceFile  )  { 
ZipInputStream   zipIn  ; 
ZipEntry   zipEntry  ; 
String   zipFile  ; 
String   url  ; 
URL   resourceUrl  ; 
int   pos  ; 
resourceUrl  =  systemResourceUrl  (  resourceFile  )  ; 
if  (  resourceUrl  ==  null  )  { 
return   null  ; 
} 
if  (  !  resourceUrl  .  getProtocol  (  )  .  equalsIgnoreCase  (  ARCHIVE_URL_PROTOCOL  )  )  { 
try  { 
return   resourceUrl  .  openStream  (  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
} 
url  =  resourceUrl  .  toString  (  )  .  substring  (  4  )  ; 
pos  =  url  .  indexOf  (  "!"  )  ; 
if  (  pos  ==  -  1  )  { 
return   null  ; 
} 
zipFile  =  url  .  substring  (  pos  +  1  )  ; 
url  =  url  .  substring  (  0  ,  pos  )  ; 
if  (  zipFile  .  startsWith  (  "/"  )  )  { 
zipFile  =  zipFile  .  substring  (  1  )  ; 
} 
try  { 
resourceUrl  =  new   URL  (  url  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
try  { 
zipIn  =  new   ZipInputStream  (  resourceUrl  .  openStream  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
try  { 
while  (  (  zipEntry  =  zipIn  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  zipEntry  .  isDirectory  (  )  )  { 
continue  ; 
} 
if  (  zipEntry  .  getName  (  )  .  equals  (  zipFile  )  )  { 
return   zipIn  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
return   null  ; 
} 


















public   SortedSet   getSystemClassNames  (  int   classSource  )  { 
SortedSet   resources  ; 
SortedSet   classes  ; 
Iterator   it  ; 
String   resource  ; 
resources  =  getSystemResourceNames0  (  classSource  ,  true  )  ; 
if  (  resources  ==  null  )  { 
return   null  ; 
} 
classes  =  new   TreeSet  (  )  ; 
for  (  it  =  resources  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
resource  =  (  String  )  it  .  next  (  )  ; 
classes  .  add  (  transformFileToClass  (  resource  )  )  ; 
} 
return   classes  ; 
} 



















public   SortedSet   getSystemResourceNames  (  int   resourceSource  )  { 
return   getSystemResourceNames0  (  resourceSource  ,  false  )  ; 
} 






















protected   SortedSet   getSystemResourceNames0  (  int   resourceSource  ,  boolean   onlyClasses  )  { 
SortedSet   resources  ; 
SortedSet   set  ; 
String   file  ; 
String   name  ; 
int   i  ; 
resources  =  new   TreeSet  (  )  ; 
if  (  (  resourceSource  &  SOURCE_BOOT_CLASSPATH  )  ==  SOURCE_BOOT_CLASSPATH  )  { 
for  (  i  =  0  ;  i  <  bootClassPaths_  .  length  ;  i  ++  )  { 
set  =  getResourceNames  (  new   File  (  bootClassPaths_  [  i  ]  )  ,  onlyClasses  )  ; 
resources  .  addAll  (  set  )  ; 
} 
} 
if  (  (  resourceSource  &  SOURCE_SYSTEM_JARFILE  )  ==  SOURCE_SYSTEM_JARFILE  )  { 
for  (  i  =  0  ;  i  <  systemJarFiles_  .  length  ;  i  ++  )  { 
set  =  getResourceNames  (  new   File  (  systemJarFiles_  [  i  ]  )  ,  onlyClasses  )  ; 
resources  .  addAll  (  set  )  ; 
} 
} 
if  (  (  resourceSource  &  SOURCE_SYSTEM_CLASSPATH  )  ==  SOURCE_SYSTEM_CLASSPATH  )  { 
for  (  i  =  0  ;  i  <  systemClassPaths_  .  length  ;  i  ++  )  { 
set  =  getResourceNames  (  new   File  (  systemClassPaths_  [  i  ]  )  ,  onlyClasses  )  ; 
resources  .  addAll  (  set  )  ; 
} 
} 
return   resources  ; 
} 






public   ClassSource  (  )  { 
this  (  (  String  )  null  ,  null  ,  null  )  ; 
} 













public   ClassSource  (  String   classpath  ,  Resource   resource  ,  URL  [  ]  urls  )  { 
setUserClassPath  (  classpath  )  ; 
setUserClassResource  (  resource  )  ; 
setUserClassUrls  (  urls  )  ; 
} 












public   ClassSource  (  String  [  ]  classpaths  ,  Resource   resource  ,  URL  [  ]  urls  )  { 
setUserClassPaths  (  classpaths  )  ; 
setUserClassResource  (  resource  )  ; 
setUserClassUrls  (  urls  )  ; 
} 











public   void   setUserClassPath  (  String   classpath  )  { 
StringTokenizer   st  ; 
ArrayList   paths  ; 
if  (  classpath  ==  null  )  { 
userClassPaths_  =  null  ; 
userClassPathUrls_  =  null  ; 
return  ; 
} 
st  =  new   StringTokenizer  (  classpath  ,  File  .  pathSeparator  )  ; 
paths  =  new   ArrayList  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
paths  .  add  (  st  .  nextToken  (  )  )  ; 
} 
setUserClassPaths  (  (  String  [  ]  )  paths  .  toArray  (  new   String  [  0  ]  )  )  ; 
} 










public   void   setUserClassPaths  (  String  [  ]  classpaths  )  { 
ArrayList   paths  ; 
ArrayList   urls  ; 
String   path  ; 
URL   url  ; 
int   i  ; 
if  (  classpaths  ==  null  )  { 
userClassPaths_  =  null  ; 
userClassPathUrls_  =  null  ; 
return  ; 
} 
paths  =  new   ArrayList  (  )  ; 
urls  =  new   ArrayList  (  )  ; 
for  (  i  =  0  ;  i  <  classpaths  .  length  ;  i  ++  )  { 
path  =  canonicalClassPath  (  classpaths  [  i  ]  )  ; 
paths  .  add  (  path  )  ; 
url  =  canonicalClassUrl  (  path  )  ; 
if  (  url  !=  null  )  { 
urls  .  add  (  url  )  ; 
} 
} 
userClassPaths_  =  (  String  [  ]  )  paths  .  toArray  (  new   String  [  0  ]  )  ; 
userClassPathUrls_  =  (  URL  [  ]  )  urls  .  toArray  (  new   URL  [  0  ]  )  ; 
} 










public   void   setUserClassResource  (  Resource   resource  )  { 
userClassResource_  =  resource  ; 
} 










public   void   setUserClassUrls  (  URL  [  ]  urls  )  { 
ArrayList   ar  ; 
URL   url  ; 
int   i  ; 
if  (  urls  ==  null  )  { 
userClassUrls_  =  null  ; 
return  ; 
} 
ar  =  new   ArrayList  (  )  ; 
for  (  i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
url  =  canonicalClassUrl  (  urls  [  i  ]  )  ; 
if  (  url  !=  null  )  { 
ar  .  add  (  url  )  ; 
} 
} 
userClassUrls_  =  (  URL  [  ]  )  ar  .  toArray  (  new   URL  [  0  ]  )  ; 
} 
























public   String   userClassUrl  (  String   className  )  { 
return   userResourceUrl  (  transformClassToFile  (  className  )  )  ; 
} 

























public   String   userResourceUrl  (  String   resourceFile  )  { 
URLClassLoader   ucl  ; 
InputStream   rin  ; 
URL   url  ; 
if  (  resourceFile  ==  null  )  { 
return   null  ; 
} 
if  (  userClassPathUrls_  !=  null  )  { 
ucl  =  new   URLClassLoader  (  userClassPathUrls_  ,  dummyClassLoader_  )  ; 
try  { 
rin  =  ucl  .  getResourceAsStream  (  resourceFile  )  ; 
url  =  ucl  .  getResource  (  resourceFile  )  ; 
if  (  url  !=  null  )  { 
return   url  .  toString  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
} 
} 
if  (  userClassResource_  !=  null  )  { 
if  (  userClassResource_  .  exists  (  resourceFile  )  )  { 
return   RESOURCE_URL_PROTOCOL  +  ":"  +  resourceFile  ; 
} 
} 
if  (  userClassUrls_  !=  null  )  { 
ucl  =  new   URLClassLoader  (  userClassUrls_  ,  dummyClassLoader_  )  ; 
try  { 
rin  =  ucl  .  getResourceAsStream  (  resourceFile  )  ; 
url  =  ucl  .  getResource  (  resourceFile  )  ; 
if  (  url  !=  null  )  { 
return   url  .  toString  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
} 
} 
return   null  ; 
} 






























public   int   userClassSource  (  String   className  )  { 
return   userResourceSource  (  transformClassToFile  (  className  )  )  ; 
} 






























public   int   userResourceSource  (  String   resourceFile  )  { 
String   url  ; 
url  =  userResourceUrl  (  resourceFile  )  ; 
if  (  url  ==  null  )  { 
return   SOURCE_NOT_FOUND  ; 
} 
if  (  url  .  startsWith  (  ARCHIVE_URL_PROTOCOL  +  ":"  )  )  { 
url  =  url  .  substring  (  ARCHIVE_URL_PROTOCOL  .  length  (  )  +  1  )  ; 
} 
if  (  url  .  startsWith  (  FILE_URL_PROTOCOL  +  ":"  )  )  { 
return   SOURCE_USER_LOCALCLASS  ; 
} 
if  (  url  .  startsWith  (  RESOURCE_URL_PROTOCOL  +  ":"  )  )  { 
return   SOURCE_USER_CLASSRESOURCE  ; 
} 
return   SOURCE_USER_REMOTECLASS  ; 
} 













public   InputStream   userClassInputStream  (  String   className  )  { 
return   userResourceInputStream  (  transformClassToFile  (  className  )  )  ; 
} 













public   InputStream   userResourceInputStream  (  String   resourceFile  )  { 
ZipInputStream   zipIn  ; 
InputStream   in  ; 
ZipEntry   zipEntry  ; 
String   zipFile  ; 
String   url  ; 
URL   resourceUrl  ; 
int   pos  ; 
url  =  userResourceUrl  (  resourceFile  )  ; 
if  (  url  ==  null  )  { 
return   null  ; 
} 
if  (  url  .  startsWith  (  RESOURCE_URL_PROTOCOL  +  ":"  )  )  { 
try  { 
in  =  userClassResource_  .  getInputStream  (  resourceFile  )  ; 
return   in  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
} 
try  { 
resourceUrl  =  new   URL  (  url  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
if  (  !  resourceUrl  .  getProtocol  (  )  .  equalsIgnoreCase  (  ARCHIVE_URL_PROTOCOL  )  )  { 
try  { 
return   resourceUrl  .  openStream  (  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
} 
url  =  resourceUrl  .  toString  (  )  .  substring  (  4  )  ; 
pos  =  url  .  indexOf  (  "!"  )  ; 
if  (  pos  ==  -  1  )  { 
return   null  ; 
} 
zipFile  =  url  .  substring  (  pos  +  1  )  ; 
url  =  url  .  substring  (  0  ,  pos  )  ; 
if  (  zipFile  .  startsWith  (  "/"  )  )  { 
zipFile  =  zipFile  .  substring  (  1  )  ; 
} 
try  { 
resourceUrl  =  new   URL  (  url  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
try  { 
zipIn  =  new   ZipInputStream  (  resourceUrl  .  openStream  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
try  { 
while  (  (  zipEntry  =  zipIn  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  zipEntry  .  isDirectory  (  )  )  { 
continue  ; 
} 
if  (  zipEntry  .  getName  (  )  .  equals  (  zipFile  )  )  { 
return   zipIn  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
return   null  ; 
} 
return   null  ; 
} 





















public   SortedSet   getUserClassNames  (  int   classSource  )  { 
SortedSet   resources  ; 
SortedSet   classes  ; 
Iterator   it  ; 
String   resource  ; 
resources  =  getUserResourceNames0  (  classSource  ,  true  )  ; 
if  (  resources  ==  null  )  { 
return   null  ; 
} 
classes  =  new   TreeSet  (  )  ; 
for  (  it  =  resources  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
resource  =  (  String  )  it  .  next  (  )  ; 
classes  .  add  (  transformFileToClass  (  resource  )  )  ; 
} 
return   classes  ; 
} 






















public   SortedSet   getUserResourceNames  (  int   resourceSource  )  { 
return   getUserResourceNames0  (  resourceSource  ,  false  )  ; 
} 






















public   SortedSet   getUserResourceNames0  (  int   resourceSource  ,  boolean   onlyClasses  )  { 
ZipInputStream   zipIn  ; 
SortedSet   resources  ; 
SortedSet   set  ; 
ZipEntry   zipEntry  ; 
Iterator   it  ; 
String   name  ; 
URL   url  ; 
int   i  ; 
resources  =  new   TreeSet  (  )  ; 
zipIn  =  null  ; 
if  (  (  resourceSource  &  SOURCE_USER_LOCALCLASS  )  ==  SOURCE_USER_LOCALCLASS  )  { 
for  (  i  =  0  ;  userClassPaths_  !=  null  &&  i  <  userClassPaths_  .  length  ;  i  ++  )  { 
set  =  getResourceNames  (  new   File  (  userClassPaths_  [  i  ]  )  ,  onlyClasses  )  ; 
resources  .  addAll  (  set  )  ; 
} 
for  (  i  =  0  ;  userClassUrls_  !=  null  &&  i  <  userClassUrls_  .  length  ;  i  ++  )  { 
if  (  userClassUrls_  [  i  ]  .  getProtocol  (  )  .  equalsIgnoreCase  (  FILE_URL_PROTOCOL  )  )  { 
set  =  getResourceNames  (  new   File  (  userClassUrls_  [  i  ]  .  getFile  (  )  )  ,  onlyClasses  )  ; 
resources  .  addAll  (  set  )  ; 
} 
} 
} 
if  (  (  resourceSource  &  SOURCE_USER_CLASSRESOURCE  )  ==  SOURCE_USER_CLASSRESOURCE  )  { 
try  { 
for  (  it  =  userClassResource_  .  list  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
name  =  (  String  )  it  .  next  (  )  ; 
if  (  onlyClasses  )  { 
if  (  checkFileExtension  (  name  ,  CLASS_EXTENSION  )  )  { 
resources  .  add  (  name  )  ; 
} 
}  else  { 
resources  .  add  (  name  )  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
} 
} 
if  (  (  resourceSource  &  SOURCE_USER_REMOTECLASS  )  ==  SOURCE_USER_REMOTECLASS  )  { 
for  (  i  =  0  ;  userClassUrls_  !=  null  &&  i  <  userClassUrls_  .  length  ;  i  ++  )  { 
url  =  userClassUrls_  [  i  ]  ; 
if  (  !  url  .  getProtocol  (  )  .  equalsIgnoreCase  (  FILE_URL_PROTOCOL  )  &&  (  checkFileExtension  (  url  .  getFile  (  )  ,  JAR_EXTENSION  )  ||  checkFileExtension  (  url  .  getFile  (  )  ,  ZIP_EXTENSION  )  )  )  { 
try  { 
zipIn  =  new   ZipInputStream  (  url  .  openStream  (  )  )  ; 
while  (  (  zipEntry  =  zipIn  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  zipEntry  .  isDirectory  (  )  )  { 
continue  ; 
} 
name  =  zipEntry  .  getName  (  )  ; 
if  (  onlyClasses  )  { 
if  (  checkFileExtension  (  name  ,  CLASS_EXTENSION  )  )  { 
resources  .  add  (  name  )  ; 
} 
}  else  { 
resources  .  add  (  name  )  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
}  finally  { 
try  { 
zipIn  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 
} 
return   resources  ; 
} 











protected   static   String   indentHex  (  int   hex  ,  int   n  )  { 
String   result  ; 
String   str  ; 
int   mask  ; 
int   i  ; 
if  (  n  >  8  )  { 
n  =  8  ; 
} 
for  (  mask  =  1  ,  i  =  0  ;  i  <  n  ;  i  ++  )  { 
mask  *=  0x10  ; 
} 
mask  --  ; 
str  =  Integer  .  toHexString  (  hex  &  mask  )  ; 
for  (  result  =  ""  ,  i  =  str  .  length  (  )  ;  i  <  n  ;  i  ++  )  { 
result  +=  "0"  ; 
} 
result  +=  str  ; 
return   result  ; 
} 




















protected   static   String   hexDump  (  byte  [  ]  data  )  { 
StringBuffer   charbuf  ; 
StringBuffer   hexbuf  ; 
StringBuffer   strbuf  ; 
String   separator  ; 
String   addr  ; 
String   hex  ; 
int   i  ; 
strbuf  =  new   StringBuffer  (  )  ; 
separator  =  "."  ; 
charbuf  =  new   StringBuffer  (  )  ; 
hexbuf  =  new   StringBuffer  (  )  ; 
addr  =  "00000000"  ; 
for  (  i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
if  (  i  !=  0  &&  (  i  %  16  )  ==  0  )  { 
strbuf  .  append  (  addr  )  ; 
strbuf  .  append  (  "   "  )  ; 
strbuf  .  append  (  hexbuf  .  toString  (  )  )  ; 
strbuf  .  append  (  "  "  )  ; 
strbuf  .  append  (  charbuf  .  toString  (  )  )  ; 
strbuf  .  append  (  "\n"  )  ; 
charbuf  =  new   StringBuffer  (  )  ; 
hexbuf  =  new   StringBuffer  (  )  ; 
addr  =  indentHex  (  i  ,  8  )  ; 
} 
hexbuf  .  append  (  indentHex  (  (  int  )  data  [  i  ]  ,  2  )  +  " "  )  ; 
if  (  Character  .  isLetterOrDigit  (  (  char  )  data  [  i  ]  )  )  { 
charbuf  .  append  (  (  char  )  data  [  i  ]  )  ; 
}  else  { 
charbuf  .  append  (  separator  )  ; 
} 
} 
if  (  charbuf  .  length  (  )  >  0  )  { 
while  (  (  i  %  16  )  !=  0  )  { 
hexbuf  .  append  (  "   "  )  ; 
i  ++  ; 
} 
strbuf  .  append  (  addr  )  ; 
strbuf  .  append  (  "   "  )  ; 
strbuf  .  append  (  hexbuf  .  toString  (  )  )  ; 
strbuf  .  append  (  "  "  )  ; 
strbuf  .  append  (  charbuf  .  toString  (  )  )  ; 
strbuf  .  append  (  "\n"  )  ; 
} 
return   strbuf  .  toString  (  )  ; 
} 





protected   static   void   printHelp  (  )  { 
System  .  out  .  println  (  "\n  USAGE: java "  +  ClassSource  .  class  .  getName  (  )  +  " <options>\n"  )  ; 
System  .  out  .  println  (  "  Options:\n"  )  ; 
System  .  out  .  println  (  "   -help                               "  +  "Displays this help message"  )  ; 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "   -userclasspath <class_path>         "  +  "Set system dependent <user_class_path>\n"  +  "                                       "  +  "as user class path"  )  ; 
System  .  out  .  println  (  "   -userclassresource <file_resource>  "  +  "Set <file_resource> (type: ZIP or JAR)\n"  +  "                                       "  +  "as user class resource"  )  ; 
System  .  out  .  println  (  "   -userclassurls (<class_url> ' ')*   "  +  "Set the <user_url>s as user class URLs"  )  ; 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "   -resource                           "  +  "Flag to toggle between class names and\n"  +  "                                       "  +  "resource files for input and output"  )  ; 
System  .  out  .  println  (  "   -verbose                            "  +  "Displays all configured class paths/URLs"  )  ; 
System  .  out  .  println  (  "   -debug                              "  +  "Displays all available classes"  )  ; 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "   -search <name>                      "  +  "Search class <name> in the system and\n"  +  "                                       "  +  "given user sources"  )  ; 
System  .  out  .  println  (  "   -hex                                "  +  "Print hex dump of found class"  )  ; 
System  .  out  .  println  (  )  ; 
} 
























public   static   void   main  (  String  [  ]  argv  )  { 
ByteArrayOutputStream   bos  ; 
de  .  fhg  .  igd  .  util  .  URL   argUrl  ; 
StringTokenizer   st  ; 
InputStream   in  ; 
ClassSource   cs  ; 
ArgsParser   p  ; 
ArrayList   ucu  ; 
ArrayList   al  ; 
SortedSet   sortedSet  ; 
Iterator   it  ; 
Resource   userClassResource  ; 
boolean   resourceFlag  ; 
String   urlStr  ; 
String   sourceStr  ; 
String   name  ; 
String   ucp  ; 
String   str  ; 
URL  [  ]  userClassUrls  ; 
URL   url  ; 
int   source  ; 
int   i  ; 
try  { 
cs  =  new   ClassSource  (  )  ; 
p  =  new   ArgsParser  (  options_  )  ; 
p  .  parse  (  argv  )  ; 
if  (  p  .  isDefined  (  "help"  )  )  { 
printHelp  (  )  ; 
return  ; 
} 
if  (  p  .  isDefined  (  "resource"  )  )  { 
resourceFlag  =  true  ; 
}  else  { 
resourceFlag  =  false  ; 
} 
if  (  p  .  isDefined  (  "userclasspath"  )  )  { 
cs  .  setUserClassPath  (  p  .  stringValue  (  "userclasspath"  )  )  ; 
} 
userClassResource  =  null  ; 
if  (  p  .  isDefined  (  "userclassresource"  )  )  { 
try  { 
userClassResource  =  new   MemoryResource  (  )  ; 
Resources  .  unzip  (  new   FileInputStream  (  p  .  stringValue  (  "userclassresource"  )  )  ,  userClassResource  )  ; 
cs  .  setUserClassResource  (  userClassResource  )  ; 
}  catch  (  Throwable   t  )  { 
userClassResource  =  null  ; 
} 
} 
if  (  p  .  isDefined  (  "userclassurls"  )  )  { 
ucu  =  (  ArrayList  )  p  .  value  (  "userclassurls"  )  ; 
al  =  new   ArrayList  (  )  ; 
for  (  i  =  0  ;  i  <  ucu  .  size  (  )  ;  i  ++  )  { 
try  { 
argUrl  =  (  de  .  fhg  .  igd  .  util  .  URL  )  ucu  .  get  (  i  )  ; 
if  (  argUrl  .  getProtocol  (  )  .  equalsIgnoreCase  (  FILE_URL_PROTOCOL  )  )  { 
url  =  new   URL  (  FILE_URL_PROTOCOL  ,  null  ,  argUrl  .  getPath  (  )  )  ; 
}  else  { 
url  =  new   URL  (  argUrl  .  toString  (  )  )  ; 
} 
al  .  add  (  url  )  ; 
}  catch  (  Throwable   t  )  { 
} 
} 
userClassUrls  =  (  URL  [  ]  )  al  .  toArray  (  new   URL  [  0  ]  )  ; 
}  else  { 
userClassUrls  =  null  ; 
} 
cs  .  setUserClassUrls  (  userClassUrls  )  ; 
if  (  p  .  isDefined  (  "verbose"  )  ||  p  .  isDefined  (  "debug"  )  )  { 
System  .  out  .  println  (  )  ; 
for  (  i  =  0  ;  i  <  ClassSource  .  bootClassPaths_  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "[sun.boot.class.path] "  +  ClassSource  .  bootClassPaths_  [  i  ]  )  ; 
} 
System  .  out  .  println  (  )  ; 
for  (  i  =  0  ;  i  <  ClassSource  .  systemJarFiles_  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "[java.ext.dirs] "  +  ClassSource  .  systemJarFiles_  [  i  ]  )  ; 
} 
System  .  out  .  println  (  )  ; 
for  (  i  =  0  ;  i  <  ClassSource  .  systemClassPaths_  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "[java.class.path] "  +  ClassSource  .  systemClassPaths_  [  i  ]  )  ; 
} 
System  .  out  .  println  (  )  ; 
for  (  i  =  0  ;  cs  .  userClassPaths_  !=  null  &&  i  <  cs  .  userClassPaths_  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "[user.class.path] "  +  cs  .  userClassPaths_  [  i  ]  )  ; 
} 
System  .  out  .  println  (  )  ; 
if  (  userClassResource  !=  null  )  { 
System  .  out  .  println  (  "[user.class.resource] "  +  p  .  stringValue  (  "userclassresource"  )  )  ; 
} 
System  .  out  .  println  (  )  ; 
for  (  i  =  0  ;  cs  .  userClassUrls_  !=  null  &&  i  <  cs  .  userClassUrls_  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "[user.class.url] "  +  cs  .  userClassUrls_  [  i  ]  )  ; 
} 
System  .  out  .  println  (  )  ; 
} 
if  (  p  .  isDefined  (  "debug"  )  )  { 
System  .  out  .  println  (  )  ; 
if  (  resourceFlag  )  { 
System  .  out  .  println  (  "List of all system resources:"  )  ; 
sortedSet  =  cs  .  getSystemResourceNames  (  SOURCE_ALL  )  ; 
}  else  { 
System  .  out  .  println  (  "List of all system classes:"  )  ; 
sortedSet  =  cs  .  getSystemClassNames  (  SOURCE_ALL  )  ; 
} 
for  (  it  =  sortedSet  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
str  =  (  String  )  it  .  next  (  )  ; 
System  .  out  .  println  (  "> "  +  str  )  ; 
} 
System  .  out  .  println  (  )  ; 
if  (  resourceFlag  )  { 
System  .  out  .  println  (  "List of all user resources:"  )  ; 
sortedSet  =  cs  .  getUserResourceNames  (  SOURCE_ALL  )  ; 
}  else  { 
System  .  out  .  println  (  "List of all user classes:"  )  ; 
sortedSet  =  cs  .  getUserClassNames  (  SOURCE_ALL  )  ; 
} 
for  (  it  =  sortedSet  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
str  =  (  String  )  it  .  next  (  )  ; 
System  .  out  .  println  (  "> "  +  str  )  ; 
} 
System  .  out  .  println  (  )  ; 
} 
if  (  p  .  isDefined  (  "search"  )  )  { 
name  =  p  .  stringValue  (  "search"  )  ; 
if  (  resourceFlag  )  { 
System  .  out  .  println  (  "ResourceName   = "  +  name  )  ; 
url  =  ClassSource  .  systemResourceUrl  (  name  )  ; 
if  (  url  ==  null  )  { 
urlStr  =  cs  .  userResourceUrl  (  name  )  ; 
System  .  out  .  println  (  "ResourceUrl    = "  +  urlStr  )  ; 
}  else  { 
System  .  out  .  println  (  "ResourceUrl    = "  +  url  )  ; 
} 
source  =  ClassSource  .  systemResourceSource  (  name  )  ; 
source  |=  cs  .  userResourceSource  (  name  )  ; 
System  .  out  .  print  (  "ResourceSource = "  )  ; 
}  else  { 
System  .  out  .  println  (  "ClassName   = "  +  name  )  ; 
url  =  ClassSource  .  systemClassUrl  (  name  )  ; 
if  (  url  ==  null  )  { 
urlStr  =  cs  .  userClassUrl  (  name  )  ; 
System  .  out  .  println  (  "ClassUrl    = "  +  urlStr  )  ; 
}  else  { 
System  .  out  .  println  (  "ClassUrl    = "  +  url  )  ; 
} 
source  =  ClassSource  .  systemClassSource  (  name  )  ; 
source  |=  cs  .  userClassSource  (  name  )  ; 
System  .  out  .  print  (  "ClassSource = "  )  ; 
} 
sourceStr  =  ""  ; 
if  (  (  source  &  SOURCE_BOOT_CLASSPATH  )  ==  SOURCE_BOOT_CLASSPATH  )  { 
sourceStr  +=  " | <sun.boot.class.path>"  ; 
} 
if  (  (  source  &  SOURCE_SYSTEM_JARFILE  )  ==  SOURCE_SYSTEM_JARFILE  )  { 
sourceStr  +=  " | <java.ext.dirs>"  ; 
} 
if  (  (  source  &  SOURCE_SYSTEM_CLASSPATH  )  ==  SOURCE_SYSTEM_CLASSPATH  )  { 
sourceStr  +=  " | <java.class.path>"  ; 
} 
if  (  (  source  &  SOURCE_USER_LOCALCLASS  )  ==  SOURCE_USER_LOCALCLASS  )  { 
sourceStr  +=  " | <user.local.class>"  ; 
} 
if  (  (  source  &  SOURCE_USER_CLASSRESOURCE  )  ==  SOURCE_USER_CLASSRESOURCE  )  { 
sourceStr  +=  " | <user.class.resource>"  ; 
} 
if  (  (  source  &  SOURCE_USER_REMOTECLASS  )  ==  SOURCE_USER_REMOTECLASS  )  { 
sourceStr  +=  " | <user.remote.class>"  ; 
} 
sourceStr  =  sourceStr  .  trim  (  )  ; 
if  (  sourceStr  .  length  (  )  !=  0  )  { 
sourceStr  =  sourceStr  .  substring  (  2  )  ; 
}  else  { 
sourceStr  =  "<not_found>"  ; 
} 
System  .  out  .  println  (  sourceStr  )  ; 
if  (  p  .  isDefined  (  "hex"  )  )  { 
if  (  source  !=  SOURCE_NOT_FOUND  )  { 
if  (  resourceFlag  )  { 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "ResourceHexDump:\n"  )  ; 
in  =  ClassSource  .  systemResourceInputStream  (  name  )  ; 
if  (  in  ==  null  )  { 
in  =  cs  .  userResourceInputStream  (  name  )  ; 
} 
}  else  { 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "ClassHexDump:\n"  )  ; 
in  =  ClassSource  .  systemClassInputStream  (  name  )  ; 
if  (  in  ==  null  )  { 
in  =  cs  .  userClassInputStream  (  name  )  ; 
} 
} 
bos  =  new   ByteArrayOutputStream  (  )  ; 
Pipe  .  pipe  (  in  ,  bos  )  ; 
try  { 
in  .  close  (  )  ; 
}  catch  (  Throwable   t  )  { 
} 
System  .  out  .  println  (  hexDump  (  bos  .  toByteArray  (  )  )  )  ; 
}  else  { 
System  .  out  .  println  (  )  ; 
System  .  out  .  println  (  "HexDump: <none>\n"  )  ; 
System  .  out  .  println  (  )  ; 
} 
} 
} 
}  catch  (  Throwable   t  )  { 
printHelp  (  )  ; 
t  .  printStackTrace  (  )  ; 
} 
} 
} 









class   DummyClassLoader   extends   ClassLoader  { 

public   DummyClassLoader  (  )  { 
} 

public   DummyClassLoader  (  ClassLoader   parent  )  { 
} 

public   Class   loadClass  (  String   name  )  throws   ClassNotFoundException  { 
throw   new   ClassNotFoundException  (  name  )  ; 
} 

public   URL   getResource  (  String   name  )  { 
return   null  ; 
} 

public   InputStream   getResourceAsStream  (  String   name  )  { 
return   null  ; 
} 

public   static   URL   getSystemResource  (  String   name  )  { 
return   null  ; 
} 

public   static   Enumeration   getSystemResources  (  String   name  )  throws   IOException  { 
return   null  ; 
} 

public   static   InputStream   getSystemResourceAsStream  (  String   name  )  { 
return   null  ; 
} 

public   static   ClassLoader   getSystemClassLoader  (  )  { 
return   null  ; 
} 

public   synchronized   void   setDefaultAssertionStatus  (  boolean   enabled  )  { 
} 

public   synchronized   void   setPackageAssertionStatus  (  String   packageName  ,  boolean   enabled  )  { 
} 

public   synchronized   void   setClassAssertionStatus  (  String   className  ,  boolean   enabled  )  { 
} 

public   synchronized   void   clearAssertionStatus  (  )  { 
} 
} 

