package   oscript  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  zip  .  *  ; 
import   oscript  .  exceptions  .  ProgrammingErrorException  ; 









public   class   JarFileSystem   extends   AbstractFileSystem  { 

private   File   file  ; 

private   ZipFile   zipFile  ; 

private   String   fileDescriptor  ; 




private   Hashtable   jarFileTable  =  new   Hashtable  (  )  ; 






public   JarFileSystem  (  File   root  )  throws   ZipException  ,  IOException  { 
file  =  root  ; 
fileDescriptor  =  file  .  getAbsolutePath  (  )  ; 
if  (  file  .  exists  (  )  )  zipFile  =  new   ZipFile  (  file  )  ; 
} 






public   JarFileSystem  (  String   root  )  throws   ZipException  ,  IOException  { 
this  (  new   File  (  root  )  )  ; 
} 

protected   synchronized   void   finalize  (  )  throws   Throwable  { 
flush  (  )  ; 
} 

private   boolean   flushing  =  false  ; 

private   boolean   needsFlush  =  false  ; 

private   Runnable   flusher  =  null  ; 

private   synchronized   void   scheduleFlush  (  )  { 
if  (  !  flushing  &&  (  flusher  ==  null  )  )  { 
flusher  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
synchronized  (  JarFileSystem  .  this  )  { 
flush  (  )  ; 
flusher  =  null  ; 
} 
}  catch  (  Throwable   t  )  { 
t  .  printStackTrace  (  )  ; 
} 
} 
}  ; 
OscriptBuiltins  .  atExit  (  flusher  )  ; 
needsFlush  =  true  ; 
} 
} 




protected   synchronized   void   flush  (  )  throws   IOException  { 
flushing  =  true  ; 
try  { 
if  (  needsFlush  )  { 
needsFlush  =  false  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
ZipOutputStream   zos  =  new   ZipOutputStream  (  bos  )  ; 
if  (  zipFile  !=  null  )  { 
for  (  Enumeration   e  =  zipFile  .  entries  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
ZipEntry   zipEntry  =  (  ZipEntry  )  (  e  .  nextElement  (  )  )  ; 
JarFile   jarFile  =  (  JarFile  )  (  jarFileTable  .  get  (  zipEntry  .  getName  (  )  )  )  ; 
if  (  jarFile  ==  null  )  writeZipEntry  (  zos  ,  zipEntry  ,  zipFile  .  getInputStream  (  zipEntry  )  )  ; 
} 
} 
for  (  Enumeration   e  =  jarFileTable  .  keys  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   name  =  (  String  )  (  e  .  nextElement  (  )  )  ; 
JarFile   jarFile  =  (  JarFile  )  (  jarFileTable  .  get  (  name  )  )  ; 
if  (  jarFile  .  exists  (  )  &&  !  jarFile  .  isDirectory  (  )  )  { 
jarFile  .  flush  (  )  ; 
writeZipEntry  (  zos  ,  jarFile  .  getZipEntry  (  )  ,  jarFile  .  getInputStream  (  )  )  ; 
} 
} 
if  (  zipFile  !=  null  )  zipFile  .  close  (  )  ; 
zos  .  flush  (  )  ; 
zos  .  close  (  )  ; 
file  .  delete  (  )  ; 
file  .  createNewFile  (  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  file  )  ; 
fos  .  write  (  bos  .  toByteArray  (  )  )  ; 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
zipFile  =  new   ZipFile  (  file  ,  ZipFile  .  OPEN_READ  )  ; 
} 
}  finally  { 
flushing  =  false  ; 
} 
} 




private   void   writeZipEntry  (  ZipOutputStream   zos  ,  ZipEntry   zipEntry  ,  InputStream   is  )  throws   IOException  { 
zos  .  putNextEntry  (  zipEntry  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
int   in  ; 
while  (  (  in  =  is  .  read  (  buf  ,  0  ,  buf  .  length  )  )  >  0  )  zos  .  write  (  buf  ,  0  ,  in  )  ; 
zos  .  closeEntry  (  )  ; 
} 








protected   AbstractFile   resolveInFileSystem  (  String   mountPath  ,  String   path  )  { 
JarFile   jarFile  =  (  JarFile  )  (  jarFileTable  .  get  (  path  )  )  ; 
if  (  jarFile  ==  null  )  { 
jarFile  =  new   JarFile  (  mountPath  ,  path  )  ; 
jarFileTable  .  put  (  path  ,  jarFile  )  ; 
} 
return   jarFile  ; 
} 








protected   synchronized   Iterator   childrenInFileSystem  (  String   mountPath  ,  String   path  )  throws   IOException  { 
LinkedList   childList  =  new   LinkedList  (  )  ; 
Hashtable   childTable  =  new   Hashtable  (  )  ; 
flush  (  )  ; 
if  (  zipFile  !=  null  )  { 
for  (  Enumeration   e  =  zipFile  .  entries  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
ZipEntry   ze  =  (  ZipEntry  )  (  e  .  nextElement  (  )  )  ; 
String   name  =  ze  .  getName  (  )  ; 
if  (  !  name  .  startsWith  (  "META-INF"  )  &&  name  .  startsWith  (  path  )  )  { 
int   plen  =  path  .  length  (  )  ; 
if  (  (  plen  <  name  .  length  (  )  )  &&  (  name  .  charAt  (  plen  )  ==  '/'  )  )  plen  ++  ; 
int   idx  =  name  .  indexOf  (  '/'  ,  plen  )  ; 
String   childName  =  name  .  substring  (  plen  ,  (  idx  !=  -  1  )  ?  idx  :  name  .  length  (  )  )  ; 
if  (  (  childName  .  length  (  )  >  0  )  &&  (  childTable  .  get  (  childName  )  ==  null  )  )  { 
childTable  .  put  (  childName  ,  childName  )  ; 
AbstractFile   file  =  resolveInFileSystem  (  mountPath  ,  (  (  plen  ==  0  )  ?  ""  :  (  path  +  SEPERATOR_CHAR  )  )  +  childName  )  ; 
childList  .  add  (  file  )  ; 
} 
} 
} 
} 
return   childList  .  iterator  (  )  ; 
} 

private   static   final   byte  [  ]  EMPTY_BUF  =  new   byte  [  0  ]  ; 






class   JarFile   implements   AbstractFile  { 

private   String   mountPath  ; 

private   String   path  ; 

private   String   entryDescriptor  ; 

private   String   ext  ; 

private   ZipEntry   zipEntry  ; 

ZipEntry   getZipEntry  (  )  { 
synchronized  (  JarFileSystem  .  this  )  { 
if  (  (  zipEntry  ==  null  )  &&  (  zipFile  !=  null  )  )  zipEntry  =  zipFile  .  getEntry  (  path  +  "/"  )  ; 
if  (  (  zipEntry  ==  null  )  &&  (  zipFile  !=  null  )  )  zipEntry  =  zipFile  .  getEntry  (  path  )  ; 
return   zipEntry  ; 
} 
} 






byte  [  ]  buf  ; 




JarFile  (  String   mountPath  ,  String   path  )  { 
this  .  mountPath  =  mountPath  ; 
this  .  path  =  path  ; 
entryDescriptor  =  fileDescriptor  +  "@@/"  +  path  ; 
int   idx  =  path  .  lastIndexOf  (  '.'  )  ; 
if  (  idx  !=  -  1  )  ext  =  path  .  substring  (  idx  +  1  )  ;  else   ext  =  ""  ; 
} 








public   String   getExtension  (  )  { 
return   ext  ; 
} 




public   boolean   canWrite  (  )  { 
return   exists  (  )  &&  file  .  canWrite  (  )  ; 
} 




public   boolean   canRead  (  )  { 
return   exists  (  )  &&  file  .  canRead  (  )  ; 
} 






public   boolean   exists  (  )  { 
return   getZipEntry  (  )  !=  null  ; 
} 






public   boolean   isDirectory  (  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
if  (  zipEntry  !=  null  )  return   zipEntry  .  isDirectory  (  )  ;  else   return   false  ; 
} 







public   boolean   isFile  (  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
if  (  zipEntry  !=  null  )  return  !  zipEntry  .  isDirectory  (  )  ;  else   return   false  ; 
} 











public   long   lastModified  (  )  { 
synchronized  (  JarFileSystem  .  this  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
if  (  zipEntry  !=  null  )  return   zipEntry  .  getTime  (  )  ;  else   return  -  1  ; 
} 
} 




public   long   length  (  )  { 
if  (  buf  !=  null  )  return   buf  .  length  ;  else   return   getZipEntry  (  )  .  getSize  (  )  ; 
} 








public   boolean   createNewFile  (  )  throws   java  .  io  .  IOException  { 
synchronized  (  JarFileSystem  .  this  )  { 
boolean   rc  =  (  getZipEntry  (  )  ==  null  )  ; 
zipEntry  =  new   ZipEntry  (  path  )  ; 
buf  =  EMPTY_BUF  ; 
return   rc  ; 
} 
} 








public   boolean   delete  (  )  throws   IOException  { 
throw   new   ProgrammingErrorException  (  "unimplemented"  )  ; 
} 








public   InputStream   getInputStream  (  )  throws   java  .  io  .  IOException  { 
if  (  isDirectory  (  )  )  throw   new   java  .  io  .  IOException  (  "cannot read directory: "  +  this  )  ; 
synchronized  (  JarFileSystem  .  this  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
if  (  zipEntry  ==  null  )  throw   new   java  .  io  .  IOException  (  "does not exist"  )  ; 
flush  (  )  ; 
if  (  buf  ==  null  )  return   zipFile  .  getInputStream  (  zipEntry  )  ;  else   return   new   ByteArrayInputStream  (  buf  )  ; 
} 
} 








public   OutputStream   getOutputStream  (  boolean   append  )  throws   java  .  io  .  IOException  { 
if  (  isDirectory  (  )  )  throw   new   java  .  io  .  IOException  (  "cannot write directory: "  +  this  )  ; 
synchronized  (  JarFileSystem  .  this  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
if  (  zipEntry  ==  null  )  throw   new   java  .  io  .  IOException  (  "does not exist"  )  ; 
return   new   BufferOutputStream  (  append  )  ; 
} 
} 



public   int   hashCode  (  )  { 
return   entryDescriptor  .  hashCode  (  )  ; 
} 



public   void   flush  (  )  throws   IOException  { 
for  (  Iterator   itr  =  bosMap  .  keySet  (  )  .  iterator  (  )  ;  itr  .  hasNext  (  )  ;  )  (  (  BufferOutputStream  )  (  itr  .  next  (  )  )  )  .  flush  (  )  ; 
} 





private   WeakHashMap   bosMap  =  new   WeakHashMap  (  )  ; 



public   boolean   equals  (  Object   obj  )  { 
return  (  obj   instanceof   JarFile  )  &&  entryDescriptor  .  equals  (  (  (  JarFile  )  obj  )  .  entryDescriptor  )  ; 
} 







private   class   BufferOutputStream   extends   ByteArrayOutputStream  { 

private   boolean   append  ; 

BufferOutputStream  (  boolean   append  )  { 
this  .  append  =  append  ; 
bosMap  .  put  (  this  ,  null  )  ; 
} 

public   void   flush  (  )  throws   IOException  { 
if  (  append  )  throw   new   ProgrammingErrorException  (  "unimplemented"  )  ;  else   setBuf  (  toByteArray  (  )  )  ; 
} 

public   void   close  (  )  throws   IOException  { 
flush  (  )  ; 
} 
} 





private   void   setBuf  (  byte  [  ]  newBuf  )  { 
synchronized  (  JarFileSystem  .  this  )  { 
ZipEntry   zipEntry  =  getZipEntry  (  )  ; 
buf  =  newBuf  ; 
zipEntry  .  setSize  (  buf  .  length  )  ; 
zipEntry  .  setCompressedSize  (  -  1  )  ; 
zipEntry  .  setTime  (  System  .  currentTimeMillis  (  )  )  ; 
scheduleFlush  (  )  ; 
} 
} 







public   String   getPath  (  )  { 
return   mountPath  +  "/"  +  path  ; 
} 







public   String   getName  (  )  { 
return   basename  (  getPath  (  )  )  ; 
} 

public   String   toString  (  )  { 
return   getPath  (  )  ; 
} 
} 
} 

