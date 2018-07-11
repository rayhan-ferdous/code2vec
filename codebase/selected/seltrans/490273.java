package   org  .  jiopi  .  ibean  .  share  ; 

import   java  .  io  .  Closeable  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  nio  .  channels  .  FileLock  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  TreeSet  ; 
import   java  .  util  .  logging  .  Level  ; 
import   org  .  jiopi  .  framework  .  core  .  JiopiConfigConstants  ; 
import   org  .  jiopi  .  ibean  .  loader  .  log  .  LoaderLogUtil  ; 







public   class   ShareUtil  { 





public   static   class   StringUtil  { 








public   static   String   fixToLen  (  String   s  ,  int   maxLen  )  { 
int   len  =  s  .  length  (  )  ; 
if  (  len  <  maxLen  )  { 
int   fit  =  maxLen  -  len  ; 
StringBuilder   buffer  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  fit  ;  i  ++  )  { 
buffer  .  append  (  '0'  )  ; 
} 
buffer  .  append  (  s  )  ; 
return   buffer  .  toString  (  )  ; 
} 
return   s  ; 
} 









public   static   int   getInt  (  String   s  ,  String   defaultValue  )  { 
int   ret  ; 
try  { 
ret  =  Integer  .  parseInt  (  s  )  ; 
}  catch  (  Exception   e  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
ret  =  Integer  .  parseInt  (  defaultValue  )  ; 
} 
return   ret  ; 
} 

public   static   boolean   getBoolean  (  String   s  ,  boolean   defaultValue  )  { 
boolean   ret  =  defaultValue  ; 
try  { 
if  (  s  !=  null  )  { 
s  =  s  .  trim  (  )  ; 
ret  =  Boolean  .  parseBoolean  (  s  )  ; 
} 
}  catch  (  Exception   e  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
} 
return   ret  ; 
} 
} 






public   static   class   ResourceUtil  { 





private   static   volatile   String   programDir  =  null  ; 

public   static   String   getProgramDir  (  )  { 
if  (  programDir  ==  null  )  { 
synchronized  (  "programDir"  .  intern  (  )  )  { 
if  (  programDir  ==  null  )  { 
Properties  [  ]  jiopiProperties  =  ResourceUtil  .  getJIOPICascadingConfig  (  )  ; 
String   downloadDirPath  =  ResourceUtil  .  getPropertyValue  (  ShareConstants  .  IBEAN_DOWNLOAD_DIR  ,  jiopiProperties  ,  null  ,  false  )  ; 
if  (  downloadDirPath  ==  null  )  { 
downloadDirPath  =  ResourceUtil  .  getPropertyValue  (  ShareConstants  .  IBEAN_WORK_DIR  ,  jiopiProperties  ,  ShareConstants  .  IBEAN_WORK_DIR_DEFAULT  ,  false  )  ; 
} 
downloadDirPath  =  FileUtil  .  getPathWithSystemProperty  (  downloadDirPath  )  ; 
programDir  =  FileUtil  .  joinPath  (  downloadDirPath  ,  ShareConstants  .  PROGRAM_PATH  )  ; 
} 
} 
} 
return   programDir  ; 
} 





private   static   volatile   String   iBeanProgramDir  =  null  ; 

public   static   String   getIBeanProgramDir  (  )  { 
if  (  iBeanProgramDir  ==  null  )  { 
synchronized  (  "iBeanProgramDir"  .  intern  (  )  )  { 
if  (  iBeanProgramDir  ==  null  )  { 
iBeanProgramDir  =  FileUtil  .  joinPath  (  getProgramDir  (  )  ,  ShareConstants  .  IBEAN_PROGRAM_PATH  )  ; 
} 
} 
} 
return   iBeanProgramDir  ; 
} 

public   static   void   deleteUnfinishedVersionDir  (  File   baseDir  )  { 
if  (  !  baseDir  .  isDirectory  (  )  )  return  ; 
File  [  ]  subFiles  =  baseDir  .  listFiles  (  )  ; 
HashSet  <  String  >  checkedDir  =  new   HashSet  <  String  >  (  )  ; 
for  (  File   file  :  subFiles  )  { 
if  (  file  .  isFile  (  )  )  { 
String   name  =  file  .  getName  (  )  ; 
if  (  name  .  startsWith  (  ".ver"  )  )  { 
String   getDirName  =  ResourceUtil  .  getVersionDirName  (  name  .  substring  (  4  )  )  ; 
if  (  getDirName  !=  null  )  checkedDir  .  add  (  getDirName  )  ;  else   file  .  deleteOnExit  (  )  ; 
} 
} 
} 
for  (  File   file  :  subFiles  )  { 
if  (  file  .  isDirectory  (  )  )  { 
String   dirName  =  file  .  getName  (  )  ; 
if  (  !  checkedDir  .  contains  (  dirName  )  )  { 
FileUtil  .  deleteFile  (  file  )  ; 
} 
} 
} 
} 






public   static   boolean   isCorrectVersion  (  String   version  )  { 
if  (  version  ==  null  )  return   false  ; 
return   ShareConstants  .  VERSION_PATTERN  .  matcher  (  version  )  .  find  (  )  ; 
} 






public   static   String   getVersionDirName  (  String   versionString  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
int   sl  =  versionString  .  length  (  )  ; 
int   intPosMax  =  0  ; 
switch  (  sl  )  { 
case   8  : 
case   20  : 
intPosMax  =  2  ; 
break  ; 
case   16  : 
case   28  : 
intPosMax  =  4  ; 
break  ; 
} 
if  (  intPosMax  ==  0  )  { 
return   null  ; 
} 
int   beginPos  =  0  ; 
int   endPos  =  4  ; 
int   nowPos  =  0  ; 
do  { 
nowPos  ++  ; 
if  (  nowPos  >  1  )  { 
sb  .  append  (  '.'  )  ; 
} 
String   num  =  versionString  .  substring  (  beginPos  ,  endPos  )  ; 
if  (  nowPos  <=  intPosMax  )  { 
sb  .  append  (  Integer  .  parseInt  (  num  ,  16  )  )  ; 
}  else  { 
sb  .  append  (  Long  .  parseLong  (  num  ,  16  )  )  ; 
} 
beginPos  =  endPos  ; 
if  (  nowPos  <  intPosMax  )  endPos  +=  4  ;  else   endPos  +=  12  ; 
}  while  (  endPos  <=  sl  )  ; 
return   sb  .  toString  (  )  ; 
} 







public   static   String   getVersionString  (  String   version  ,  long   time  )  { 
try  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
String  [  ]  vers  =  version  .  split  (  "\\."  )  ; 
if  (  vers  .  length  ==  4  ||  vers  .  length  ==  2  )  { 
for  (  String   ver  :  vers  )  { 
int   verNum  =  Integer  .  parseInt  (  ver  )  ; 
sb  .  append  (  StringUtil  .  fixToLen  (  Integer  .  toHexString  (  verNum  )  ,  4  )  )  ; 
} 
} 
if  (  time  >  0  )  sb  .  append  (  StringUtil  .  fixToLen  (  Long  .  toHexString  (  time  /  1000  )  ,  12  )  )  ; 
return   sb  .  toString  (  )  .  toLowerCase  (  )  ; 
}  catch  (  Exception   e  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
} 
return   null  ; 
} 











public   static   void   copyContent  (  final   URL   url  ,  final   File   outputFile  ,  boolean   rewrite  )  throws   IOException  { 
if  (  outputFile  .  exists  (  )  )  { 
if  (  outputFile  .  isDirectory  (  )  )  return  ;  else   if  (  !  rewrite  )  { 
int   urlContentLength  =  FileUtil  .  getURLContentLength  (  url  )  ; 
long   fileLength  =  outputFile  .  length  (  )  ; 
if  (  fileLength  ==  urlContentLength  )  return  ; 
} 
} 
String   outputFilePath  =  outputFile  .  getAbsolutePath  (  )  ; 
String   outputFilePathTemp  =  outputFilePath  +  ".tmp"  ; 
File   tmpDownloadFile  =  FileUtil  .  createNewFile  (  outputFilePathTemp  ,  false  )  ; 
if  (  !  tmpDownloadFile  .  isFile  (  )  )  return  ; 
MyFileLock   fl  =  FileUtil  .  tryLockTempFile  (  tmpDownloadFile  ,  1000  ,  ShareConstants  .  connectTimeout  )  ; 
if  (  fl  !=  null  )  { 
try  { 
if  (  outputFile  .  isFile  (  )  )  outputFile  .  delete  (  )  ; 
if  (  !  outputFile  .  isFile  (  )  )  { 
OutputStream   out  =  null  ; 
InputStream   in  =  null  ; 
try  { 
in  =  FileUtil  .  getURLInputStream  (  url  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  tmpDownloadFile  )  )  ; 
IOUtil  .  copyStreams  (  in  ,  out  )  ; 
}  finally  { 
IOUtil  .  close  (  in  )  ; 
IOUtil  .  close  (  out  )  ; 
} 
if  (  tmpDownloadFile  .  length  (  )  >  0  )  IOUtil  .  copyFile  (  tmpDownloadFile  ,  outputFile  )  ; 
} 
}  finally  { 
tmpDownloadFile  .  delete  (  )  ; 
fl  .  release  (  )  ; 
} 
} 
} 










public   static   String   getPropertyValue  (  String   key  ,  Properties  [  ]  properties  ,  String   defalut  ,  boolean   highBegin  )  { 
if  (  properties  ==  null  ||  properties  .  length  <  1  )  return   defalut  ; 
int   begin  =  0  ; 
int   add  =  1  ; 
if  (  !  highBegin  )  { 
begin  =  properties  .  length  -  1  ; 
add  =  -  1  ; 
} 
for  (  int   i  =  0  ;  i  <  properties  .  length  ;  i  ++  )  { 
String   value  =  properties  [  begin  ]  .  getProperty  (  key  )  ; 
if  (  value  !=  null  )  return   value  ; 
begin  +=  add  ; 
} 
return   defalut  ; 
} 














public   static   Properties  [  ]  getJIOPICascadingConfig  (  )  { 
ClassLoader   jiopiClassLoader  =  ClassUtil  .  getClassLoaderByClass  (  JiopiConfigConstants  .  class  )  ; 
ClassLoader   contextClassLoader  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  ; 
if  (  jiopiClassLoader  ==  null  &&  contextClassLoader  !=  null  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  contextClassLoader  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else   if  (  contextClassLoader  ==  null  &&  jiopiClassLoader  !=  null  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  jiopiClassLoader  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else   if  (  jiopiClassLoader  ==  contextClassLoader  &&  jiopiClassLoader  !=  null  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  jiopiClassLoader  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else   if  (  jiopiClassLoader  ==  contextClassLoader  &&  jiopiClassLoader  ==  null  )  { 
throw   new   RuntimeException  (  "cannot find configuration file: "  +  JiopiConfigConstants  .  CONFIG_FILE  +  " in class path."  )  ; 
} 
ClassLoader   high  =  null  ; 
ClassLoader   low  =  null  ; 
int   compare  =  ClassUtil  .  compareClassLoader  (  jiopiClassLoader  ,  contextClassLoader  )  ; 
if  (  compare  ==  0  )  { 
high  =  jiopiClassLoader  ; 
low  =  contextClassLoader  ; 
}  else  { 
high  =  contextClassLoader  ; 
low  =  jiopiClassLoader  ; 
} 
URL   highURL  =  high  .  getResource  (  JiopiConfigConstants  .  CONFIG_FILE  )  ; 
URL   lowURL  =  low  .  getResource  (  JiopiConfigConstants  .  CONFIG_FILE  )  ; 
if  (  highURL  ==  null  &&  lowURL  !=  null  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  low  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else   if  (  lowURL  ==  null  &&  highURL  !=  null  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  high  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else   if  (  lowURL  ==  highURL  &&  highURL  ==  null  )  { 
throw   new   RuntimeException  (  "cannot find configuration file: "  +  JiopiConfigConstants  .  CONFIG_FILE  +  " in class path."  )  ; 
}  else   if  (  highURL  .  equals  (  lowURL  )  )  { 
return   new   Properties  [  ]  {  getPropertiesFromClassPath  (  high  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  false  )  }  ; 
}  else  { 
Properties   highPro  =  getPropertiesFromClassPath  (  high  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  true  )  ; 
Properties   lowPro  =  getPropertiesFromClassPath  (  low  ,  JiopiConfigConstants  .  CONFIG_FILE  ,  true  )  ; 
if  (  highPro  ==  null  &&  lowPro  !=  null  )  return   new   Properties  [  ]  {  lowPro  }  ;  else   if  (  lowPro  ==  null  &&  highPro  !=  null  )  return   new   Properties  [  ]  {  highPro  }  ;  else   if  (  lowPro  !=  null  &&  highPro  !=  null  )  return   new   Properties  [  ]  {  highPro  ,  lowPro  }  ;  else   throw   new   RuntimeException  (  "cannot find configuration file: "  +  JiopiConfigConstants  .  CONFIG_FILE  +  " in class path."  )  ; 
} 
} 







public   static   Properties   getPropertiesFormURL  (  URL   url  )  throws   IOException  { 
Properties   props  =  new   Properties  (  )  ; 
InputStream   in  =  FileUtil  .  getURLInputStream  (  url  )  ; 
try  { 
props  .  load  (  in  )  ; 
}  finally  { 
IOUtil  .  close  (  in  )  ; 
} 
return   props  ; 
} 








public   static   Properties   getPropertiesFromClassPath  (  ClassLoader   classLoader  ,  String   name  ,  boolean   swallow  )  { 
Properties   props  =  new   Properties  (  )  ; 
InputStream   in  =  null  ; 
try  { 
in  =  classLoader  .  getResourceAsStream  (  name  )  ; 
if  (  in  !=  null  )  { 
props  .  load  (  in  )  ; 
}  else  { 
if  (  swallow  )  return   null  ;  else   throw   new   RuntimeException  (  "cannot find configuration file: "  +  name  +  " in class path."  )  ; 
} 
}  catch  (  Exception   e  )  { 
if  (  swallow  )  return   null  ;  else   throw   new   RuntimeException  (  e  )  ; 
}  finally  { 
IOUtil  .  close  (  in  )  ; 
} 
return   props  ; 
} 
} 






public   static   class   IOUtil  { 

public   static   void   copyFile  (  final   File   from  ,  final   File   to  )  throws   IOException  { 
InputStream   in  =  null  ; 
OutputStream   out  =  null  ; 
try  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  from  )  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  to  )  )  ; 
IOUtil  .  copyStreams  (  in  ,  out  )  ; 
}  finally  { 
IOUtil  .  close  (  in  )  ; 
IOUtil  .  close  (  out  )  ; 
} 
} 








public   static   void   copyStreams  (  final   InputStream   in  ,  final   OutputStream   out  )  throws   IOException  { 
copyStreams  (  in  ,  out  ,  4096  )  ; 
} 








private   static   void   copyStreams  (  final   InputStream   in  ,  final   OutputStream   out  ,  final   int   buffersize  )  throws   IOException  { 
final   byte  [  ]  bytes  =  new   byte  [  buffersize  ]  ; 
int   bytesRead  =  in  .  read  (  bytes  )  ; 
while  (  bytesRead  >  -  1  )  { 
out  .  write  (  bytes  ,  0  ,  bytesRead  )  ; 
bytesRead  =  in  .  read  (  bytes  )  ; 
} 
} 






public   static   void   close  (  Closeable   c  )  { 
if  (  c  !=  null  )  { 
try  { 
c  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
} 
} 
} 
} 






public   static   class   ClassUtil  { 






public   static   ClassLoader   getJIOPIContextClassLoader  (  ClassLoader   jiopiClassLoader  )  { 
ClassLoader   contextClassLoader  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  ; 
if  (  jiopiClassLoader  ==  contextClassLoader  &&  contextClassLoader  !=  null  )  { 
return   contextClassLoader  ; 
}  else   if  (  jiopiClassLoader  ==  null  &&  contextClassLoader  ==  null  )  { 
return   null  ; 
}  else   if  (  jiopiClassLoader  ==  null  )  { 
return   contextClassLoader  ; 
}  else   if  (  contextClassLoader  ==  null  )  { 
return   jiopiClassLoader  ; 
}  else  { 
ClassLoader   high  =  null  ; 
int   compare  =  ClassUtil  .  compareClassLoader  (  jiopiClassLoader  ,  contextClassLoader  )  ; 
if  (  compare  ==  0  )  { 
high  =  jiopiClassLoader  ; 
}  else  { 
high  =  contextClassLoader  ; 
} 
return   high  ; 
} 
} 










public   static   int   compareClassLoader  (  ClassLoader   a  ,  ClassLoader   b  )  { 
if  (  isLoaderClass  (  a  ,  b  )  )  return   1  ;  else   if  (  isLoaderClass  (  b  ,  a  )  )  return   0  ;  else   return  -  1  ; 
} 







public   static   boolean   isLoaderClass  (  ClassLoader   father  ,  ClassLoader   son  )  { 
if  (  son  ==  null  ||  father  ==  null  )  return   false  ; 
boolean   find  =  false  ; 
do  { 
son  =  son  .  getClass  (  )  .  getClassLoader  (  )  ; 
if  (  son  ==  father  )  { 
find  =  true  ; 
break  ; 
} 
}  while  (  son  !=  null  )  ; 
return   find  ; 
} 






public   static   ClassLoader   getClassLoaderByClass  (  Class  <  ?  >  c  )  { 
ClassLoader   cl  =  c  .  getClassLoader  (  )  ; 
if  (  cl  ==  null  )  cl  =  ClassLoader  .  getSystemClassLoader  (  )  ; 
return   cl  ; 
} 
} 





public   static   class   FileUtil  { 





public   static   void   deleteFile  (  File   file  )  { 
if  (  file  .  exists  (  )  )  { 
if  (  !  file  .  isDirectory  (  )  )  { 
file  .  delete  (  )  ; 
}  else  { 
File  [  ]  children  =  file  .  listFiles  (  )  ; 
if  (  children  !=  null  &&  children  .  length  >  0  )  { 
for  (  File   child  :  children  )  { 
deleteFile  (  child  )  ; 
} 
} 
file  .  delete  (  )  ; 
} 
} 
} 














public   static   MyFileLock   tryLockTempFile  (  File   lockFile  ,  long   intervalTime  ,  long   timeOut  )  throws   IOException  { 
if  (  !  lockFile  .  isFile  (  )  )  return   null  ; 
FileLock   fl  =  null  ; 
long   fileSize  =  lockFile  .  length  (  )  ; 
long   checkTime  =  System  .  currentTimeMillis  (  )  ; 
String   fileLockPath  =  lockFile  .  getAbsolutePath  (  )  +  ".lock"  ; 
File   fileLockFile  =  FileUtil  .  createNewFile  (  fileLockPath  ,  false  )  ; 
if  (  !  fileLockFile  .  isFile  (  )  )  return   null  ; 
RandomAccessFile   raf  =  new   RandomAccessFile  (  fileLockPath  ,  "rw"  )  ; 
do  { 
fl  =  raf  .  getChannel  (  )  .  tryLock  (  )  ; 
if  (  fl  ==  null  )  { 
try  { 
Thread  .  sleep  (  intervalTime  )  ; 
}  catch  (  InterruptedException   e  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
return   null  ; 
} 
if  (  lockFile  .  isFile  (  )  )  { 
long   newFileSize  =  lockFile  .  length  (  )  ; 
if  (  newFileSize  ==  fileSize  )  { 
if  (  System  .  currentTimeMillis  (  )  -  checkTime  >  timeOut  )  { 
break  ; 
} 
}  else  { 
fileSize  =  newFileSize  ; 
checkTime  =  System  .  currentTimeMillis  (  )  ; 
} 
}  else  { 
break  ; 
} 
} 
}  while  (  fl  ==  null  )  ; 
if  (  fl  ==  null  )  { 
IOUtil  .  close  (  raf  )  ; 
return   null  ; 
}  else  { 
MyFileLock   mfl  =  new   MyFileLock  (  raf  ,  fl  ,  fileLockFile  )  ; 
return   mfl  ; 
} 
} 

public   static   int   getURLContentLength  (  URL   url  )  throws   IOException  { 
URLConnection   con  =  url  .  openConnection  (  )  ; 
con  .  setConnectTimeout  (  ShareConstants  .  connectTimeout  )  ; 
con  .  setReadTimeout  (  ShareConstants  .  connectTimeout  )  ; 
con  .  setUseCaches  (  false  )  ; 
con  .  connect  (  )  ; 
return   con  .  getContentLength  (  )  ; 
} 








public   static   InputStream   getURLInputStream  (  URL   url  )  throws   IOException  { 
URLConnection   con  =  url  .  openConnection  (  )  ; 
con  .  setConnectTimeout  (  ShareConstants  .  connectTimeout  )  ; 
con  .  setReadTimeout  (  ShareConstants  .  connectTimeout  )  ; 
con  .  setUseCaches  (  false  )  ; 
con  .  connect  (  )  ; 
return   con  .  getInputStream  (  )  ; 
} 









public   static   String  [  ]  getVersionsDirName  (  String   programDirPath  )  { 
File   programDir  =  confirmDir  (  programDirPath  ,  false  )  ; 
if  (  programDir  ==  null  )  { 
return   null  ; 
} 
File  [  ]  subFiles  =  programDir  .  listFiles  (  )  ; 
String  [  ]  rt  =  null  ; 
if  (  subFiles  .  length  >  0  )  { 
TreeSet  <  String  >  dirNames  =  new   TreeSet  <  String  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  subFiles  .  length  ;  i  ++  )  { 
if  (  subFiles  [  i  ]  .  isFile  (  )  )  { 
String   dirName  =  subFiles  [  i  ]  .  getName  (  )  ; 
if  (  dirName  .  startsWith  (  ".ver"  )  )  { 
String   getDirName  =  ResourceUtil  .  getVersionDirName  (  dirName  .  substring  (  4  )  )  ; 
if  (  getDirName  !=  null  )  dirNames  .  add  (  getDirName  )  ; 
} 
} 
} 
int   length  =  dirNames  .  size  (  )  ; 
if  (  length  >  0  )  { 
rt  =  new   String  [  length  ]  ; 
Iterator  <  String  >  it  =  dirNames  .  iterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
int   ri  =  length  -  1  -  i  ; 
if  (  it  .  hasNext  (  )  )  { 
rt  [  ri  ]  =  it  .  next  (  )  ; 
} 
} 
} 
} 
return   rt  ; 
} 












public   static   File   createNewFile  (  String   filePath  ,  boolean   deleteExists  )  throws   IOException  { 
File   newFile  =  new   File  (  filePath  )  ; 
if  (  newFile  .  exists  (  )  )  { 
if  (  newFile  .  isFile  (  )  &&  deleteExists  )  { 
newFile  .  delete  (  )  ; 
if  (  newFile  .  exists  (  )  )  { 
return   null  ; 
} 
}  else   if  (  newFile  .  isDirectory  (  )  )  { 
return   null  ; 
}  else   if  (  newFile  .  isFile  (  )  )  { 
return   newFile  ; 
} 
} 
File   dir  =  confirmDir  (  newFile  .  getParent  (  )  ,  true  )  ; 
if  (  dir  !=  null  )  { 
newFile  .  createNewFile  (  )  ; 
if  (  newFile  .  isFile  (  )  )  return   newFile  ; 
} 
return   null  ; 
} 








public   static   String   getPathWithSystemProperty  (  String   path  )  { 
path  =  FileUtil  .  standardizeFileSeparator  (  path  )  ; 
if  (  path  .  charAt  (  0  )  ==  '$'  )  { 
int   begin  =  path  .  indexOf  (  '{'  )  ; 
if  (  begin  <  0  )  return   path  ;  else   begin  =  begin  +  1  ; 
int   end  =  path  .  indexOf  (  '}'  ,  begin  )  ; 
if  (  end  <  0  )  return   path  ; 
String   systemPropertyName  =  path  .  substring  (  begin  ,  end  )  ; 
String   systemProperty  =  System  .  getProperty  (  systemPropertyName  )  ; 
if  (  systemProperty  ==  null  )  systemProperty  =  ""  ; 
systemProperty  =  FileUtil  .  standardizeFileSeparator  (  systemProperty  )  ; 
systemProperty  =  FileUtil  .  correctDirPath  (  systemProperty  )  ; 
path  =  path  .  replaceFirst  (  "\\$\\{"  +  systemPropertyName  +  "\\}"  ,  systemProperty  )  ; 
} 
return   path  ; 
} 






public   static   URL  [  ]  toURL  (  File  [  ]  paths  )  { 
URL  [  ]  urls  =  new   URL  [  paths  .  length  ]  ; 
int   i  =  0  ; 
for  (  File   path  :  paths  )  { 
urls  [  i  ]  =  toURL  (  path  .  getAbsolutePath  (  )  )  ; 
i  ++  ; 
} 
return   urls  ; 
} 







public   static   URL   toURL  (  String   s  )  { 
URL   url  =  getURL  (  s  ,  null  ,  true  )  ; 
if  (  url  ==  null  )  { 
File   testFile  =  new   File  (  s  )  ; 
if  (  testFile  .  isDirectory  (  )  )  { 
if  (  !  s  .  endsWith  (  "/"  )  )  { 
s  =  s  +  "/"  ; 
} 
} 
if  (  !  s  .  startsWith  (  "/"  )  )  { 
s  =  "/"  +  s  ; 
} 
s  =  ShareConstants  .  FILE_PROTOCOL  +  "://"  +  s  ; 
url  =  getURL  (  s  ,  null  ,  false  )  ; 
} 
if  (  url  ==  null  )  { 
throw   new   RuntimeException  (  "cannot change "  +  s  +  " to url."  )  ; 
} 
return   url  ; 
} 








public   static   URL   getURL  (  String   url  ,  URL   defaultValue  ,  boolean   swallow  )  { 
URL   ret  =  defaultValue  ; 
if  (  url  !=  null  )  { 
try  { 
ret  =  new   URL  (  url  )  ; 
}  catch  (  Exception   e  )  { 
if  (  !  swallow  )  { 
LoaderLogUtil  .  logExceptionTrace  (  ShareConstants  .  shareLogger  ,  Level  .  WARNING  ,  e  )  ; 
throw   new   RuntimeException  (  e  )  ; 
} 
} 
} 
return   ret  ; 
} 











public   static   File   confirmDir  (  String   path  ,  boolean   makeDir  )  { 
File   dir  =  new   File  (  path  )  ; 
if  (  !  dir  .  exists  (  )  &&  makeDir  )  { 
dir  .  mkdirs  (  )  ; 
} 
if  (  dir  .  isDirectory  (  )  )  { 
return   dir  ; 
} 
return   null  ; 
} 






public   static   String   joinPath  (  String  ...  path  )  { 
if  (  path  ==  null  ||  path  .  length  <  1  )  return  ""  ; 
String   joined  =  path  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  path  .  length  ;  i  ++  )  { 
joined  =  correctDirPath  (  joined  )  +  correctFilePath  (  path  [  i  ]  )  ; 
} 
return   correctDirPath  (  joined  )  ; 
} 







public   static   String   correctDirPath  (  String   path  )  { 
path  =  standardizeFileSeparator  (  path  )  ; 
if  (  !  path  .  endsWith  (  "/"  )  )  { 
return   path  ; 
} 
return   path  .  substring  (  0  ,  path  .  length  (  )  -  1  )  ; 
} 







public   static   String   correctFilePath  (  String   path  )  { 
path  =  standardizeFileSeparator  (  path  )  ; 
if  (  path  .  charAt  (  0  )  ==  '/'  )  { 
return   path  ; 
} 
return  "/"  +  path  ; 
} 






public   static   String   standardizeFileSeparator  (  String   path  )  { 
String   fileSeparator  =  System  .  getProperty  (  "file.separator"  )  ; 
fileSeparator  =  fileSeparator  .  replaceAll  (  "\\\\"  ,  "\\\\\\\\"  )  ; 
path  =  path  .  replaceAll  (  fileSeparator  ,  "/"  )  ; 
return   path  ; 
} 
} 

public   static   class   MyFileLock  { 

private   final   RandomAccessFile   myRaf  ; 

private   final   FileLock   myFl  ; 

private   final   File   lockFile  ; 

public   MyFileLock  (  RandomAccessFile   raFile  ,  FileLock   l  ,  File   f  )  { 
if  (  raFile  ==  null  ||  l  ==  null  ||  f  ==  null  )  throw   new   IllegalArgumentException  (  )  ; 
this  .  myRaf  =  raFile  ; 
this  .  myFl  =  l  ; 
this  .  lockFile  =  f  ; 
} 

public   void   release  (  )  throws   IOException  { 
try  { 
myFl  .  release  (  )  ; 
lockFile  .  deleteOnExit  (  )  ; 
}  finally  { 
IOUtil  .  close  (  myRaf  )  ; 
} 
} 
} 
} 

