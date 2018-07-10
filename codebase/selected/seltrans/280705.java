package   fi  .  arcusys  .  acj  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  Closeable  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  Flushable  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  util  .  Vector  ; 
import   javax  .  xml  .  stream  .  XMLStreamReader  ; 
import   javax  .  xml  .  stream  .  XMLStreamWriter  ; 
import   org  .  slf4j  .  Logger  ; 








public   class   IOUtil  { 

private   static   final   Logger   LOG  =  LoggerFactoryUtil  .  getLoggerIfAvailable  (  IOUtil  .  class  )  ; 





static   class   TempDirDeleter   extends   Thread  { 

private   static   Vector  <  File  >  tempDirsToDelete  =  new   Vector  <  File  >  (  )  ; 

private   static   boolean   shutDownHookAdded  =  false  ; 


@  Override 
public   void   run  (  )  { 
synchronized  (  tempDirsToDelete  )  { 
for  (  File   dir  :  tempDirsToDelete  )  { 
try  { 
deleteRecursive  (  dir  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
} 


protected   IOUtil  (  )  { 
} 




















public   static   void   closeSilently  (  Object   obj  )  { 
LOG  .  trace  (  "Closing silently: {}"  ,  obj  )  ; 
if  (  null  ==  obj  )  { 
LOG  .  trace  (  "Object is null; ignoring"  )  ; 
}  else  { 
try  { 
if  (  obj   instanceof   Closeable  )  { 
(  (  Closeable  )  obj  )  .  close  (  )  ; 
}  else   if  (  obj   instanceof   Socket  )  { 
(  (  Socket  )  obj  )  .  close  (  )  ; 
}  else   if  (  obj   instanceof   XMLStreamReader  )  { 
(  (  XMLStreamReader  )  obj  )  .  close  (  )  ; 
}  else   if  (  obj   instanceof   XMLStreamWriter  )  { 
(  (  XMLStreamWriter  )  obj  )  .  close  (  )  ; 
}  else   if  (  !  reflectionClose  (  obj  )  )  { 
throw   new   IllegalArgumentException  (  "Don't know how to close an object of class "  +  obj  .  getClass  (  )  .  getName  (  )  )  ; 
} 
}  catch  (  Throwable   ex  )  { 
LOG  .  warn  (  "An exception occurred while closing object: "  +  obj  ,  ex  )  ; 
} 
} 
} 











static   boolean   reflectionClose  (  Object   obj  )  throws   Throwable  { 
boolean   closed  ; 
Class  <  ?  >  clazz  =  obj  .  getClass  (  )  ; 
Method   m  =  null  ; 
try  { 
m  =  clazz  .  getMethod  (  "close"  ,  new   Class  <  ?  >  [  0  ]  )  ; 
m  .  invoke  (  obj  )  ; 
closed  =  true  ; 
}  catch  (  NoSuchMethodException   ex  )  { 
LOG  .  error  (  "Class "  +  clazz  .  getName  (  )  +  " doesn't have method close()"  )  ; 
closed  =  false  ; 
}  catch  (  IllegalAccessException   ex  )  { 
LOG  .  error  (  "Method "  +  m  +  " is not accessible"  )  ; 
closed  =  false  ; 
}  catch  (  InvocationTargetException   ex  )  { 
throw   ex  .  getCause  (  )  ; 
} 
return   closed  ; 
} 










public   static   void   copy  (  Reader   reader  ,  Writer   writer  )  throws   IOException  { 
if  (  null  ==  reader  )  { 
throw   new   NullPointerException  (  "reader is null"  )  ; 
} 
if  (  null  ==  writer  )  { 
throw   new   NullPointerException  (  "writer is null"  )  ; 
} 
Reader   rdr  =  new   BufferedReader  (  reader  )  ; 
Writer   w  =  new   BufferedWriter  (  writer  )  ; 
final   int   bufSize  =  1024  ; 
final   char  [  ]  buf  =  new   char  [  bufSize  ]  ; 
int   r  ; 
do  { 
r  =  rdr  .  read  (  buf  )  ; 
if  (  r  >  0  )  { 
w  .  write  (  buf  ,  0  ,  r  )  ; 
} 
}  while  (  r  >=  0  )  ; 
w  .  flush  (  )  ; 
} 










public   static   void   copy  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
if  (  null  ==  in  )  { 
throw   new   NullPointerException  (  "in is null"  )  ; 
} 
if  (  null  ==  out  )  { 
throw   new   NullPointerException  (  "out is null"  )  ; 
} 
BufferedInputStream   bis  =  new   BufferedInputStream  (  in  )  ; 
BufferedOutputStream   bos  =  new   BufferedOutputStream  (  out  )  ; 
final   int   bufSize  =  1024  ; 
final   byte  [  ]  buf  =  new   byte  [  bufSize  ]  ; 
int   r  ; 
do  { 
r  =  bis  .  read  (  buf  )  ; 
if  (  r  >  0  )  { 
bos  .  write  (  buf  ,  0  ,  r  )  ; 
} 
}  while  (  r  >=  0  )  ; 
bos  .  flush  (  )  ; 
} 


















public   static   void   loadResourceContents  (  Class  <  ?  >  clazz  ,  String   name  ,  Writer   writer  ,  String   charset  )  throws   IOException  { 
if  (  null  ==  clazz  )  { 
throw   new   NullPointerException  (  "clazz is null"  )  ; 
} 
if  (  null  ==  name  )  { 
throw   new   NullPointerException  (  "name is null"  )  ; 
} 
if  (  null  ==  writer  )  { 
throw   new   NullPointerException  (  "writer is null"  )  ; 
} 
InputStream   in  =  clazz  .  getResourceAsStream  (  name  )  ; 
if  (  null  ==  in  )  { 
throw   new   IOException  (  "No such resource found: "  +  name  )  ; 
} 
InputStreamReader   isr  ; 
isr  =  (  null  ==  charset  )  ?  new   InputStreamReader  (  in  )  :  new   InputStreamReader  (  in  ,  charset  )  ; 
try  { 
copy  (  isr  ,  writer  )  ; 
}  finally  { 
closeSilently  (  isr  )  ; 
} 
} 















public   static   void   loadResourceContents  (  Class  <  ?  >  clazz  ,  String   name  ,  OutputStream   out  )  throws   IOException  { 
if  (  null  ==  clazz  )  { 
throw   new   NullPointerException  (  "clazz is null"  )  ; 
} 
if  (  null  ==  name  )  { 
throw   new   NullPointerException  (  "name is null"  )  ; 
} 
if  (  null  ==  out  )  { 
throw   new   NullPointerException  (  "out is null"  )  ; 
} 
InputStream   in  =  clazz  .  getResourceAsStream  (  name  )  ; 
if  (  null  ==  in  )  { 
throw   new   IOException  (  "No such resource found: "  +  name  )  ; 
} 
try  { 
copy  (  in  ,  out  )  ; 
}  finally  { 
closeSilently  (  in  )  ; 
} 
} 
















public   static   String   loadResourceContents  (  Class  <  ?  >  clazz  ,  String   name  ,  String   charset  )  throws   IOException  { 
if  (  null  ==  clazz  )  { 
throw   new   NullPointerException  (  "clazz is null"  )  ; 
} 
if  (  null  ==  name  )  { 
throw   new   NullPointerException  (  "name is null"  )  ; 
} 
StringWriter   writer  =  new   StringWriter  (  )  ; 
try  { 
loadResourceContents  (  clazz  ,  name  ,  writer  ,  charset  )  ; 
return   writer  .  toString  (  )  ; 
}  finally  { 
closeSilently  (  writer  )  ; 
} 
} 




















public   static   String   loadResourceContents  (  Class  <  ?  >  clazz  ,  String   name  )  throws   IOException  { 
return   loadResourceContents  (  clazz  ,  name  ,  (  String  )  null  )  ; 
} 








public   static   String   loadToString  (  InputStream   in  ,  String   charset  )  throws   IOException  { 
Writer   w  =  new   StringWriter  (  )  ; 
InputStreamReader   isr  =  (  null  ==  charset  )  ?  new   InputStreamReader  (  in  )  :  new   InputStreamReader  (  in  ,  charset  )  ; 
copy  (  isr  ,  w  )  ; 
return   w  .  toString  (  )  ; 
} 







public   static   String   loadToString  (  Reader   r  )  throws   IOException  { 
Writer   w  =  new   StringWriter  (  )  ; 
copy  (  r  ,  w  )  ; 
return   w  .  toString  (  )  ; 
} 








public   static   String   loadToString  (  File   f  ,  String   charset  )  throws   IOException  { 
InputStream   is  =  new   BufferedInputStream  (  new   FileInputStream  (  f  )  )  ; 
try  { 
return   loadToString  (  is  ,  charset  )  ; 
}  finally  { 
closeSilently  (  is  )  ; 
} 
} 










public   static   File   createTempDirectory  (  )  throws   IOException  { 
return   createTempDirectory  (  true  )  ; 
} 









public   static   File   createTempDirectory  (  boolean   deleteOnExit  )  throws   IOException  { 
return   doCreateTempDirectory  (  "acj"  ,  null  ,  null  ,  deleteOnExit  )  ; 
} 



























public   static   File   createTempDirectory  (  String   prefix  ,  String   suffix  )  throws   IOException  { 
return   createTempDirectory  (  prefix  ,  suffix  ,  true  )  ; 
} 


























public   static   File   createTempDirectory  (  String   prefix  ,  String   suffix  ,  boolean   deleteOnExit  )  throws   IOException  { 
return   doCreateTempDirectory  (  prefix  ,  suffix  ,  null  ,  deleteOnExit  )  ; 
} 






























public   static   File   createTempDirectory  (  String   prefix  ,  String   suffix  ,  File   directory  )  throws   IOException  { 
return   createTempDirectory  (  prefix  ,  suffix  ,  directory  ,  true  )  ; 
} 
































public   static   File   createTempDirectory  (  String   prefix  ,  String   suffix  ,  File   directory  ,  boolean   deleteOnExit  )  throws   IOException  { 
return   doCreateTempDirectory  (  prefix  ,  suffix  ,  directory  ,  deleteOnExit  )  ; 
} 












static   File   doCreateTempDirectory  (  String   prefix  ,  String   suffix  ,  File   directory  ,  boolean   deleteOnExit  )  throws   IOException  { 
File   f  ; 
if  (  null  ==  directory  )  { 
f  =  File  .  createTempFile  (  prefix  ,  suffix  )  ; 
}  else  { 
f  =  File  .  createTempFile  (  prefix  ,  suffix  ,  directory  )  ; 
} 
if  (  !  f  .  delete  (  )  )  { 
throw   new   IOException  (  "Could not delete the intermediate temporary file: "  +  f  )  ; 
} 
if  (  !  f  .  mkdir  (  )  )  { 
throw   new   IOException  (  "Could not create a directory: "  +  f  )  ; 
} 
if  (  deleteOnExit  )  { 
synchronized  (  TempDirDeleter  .  tempDirsToDelete  )  { 
if  (  !  TempDirDeleter  .  shutDownHookAdded  )  { 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   TempDirDeleter  (  )  )  ; 
TempDirDeleter  .  shutDownHookAdded  =  true  ; 
} 
TempDirDeleter  .  tempDirsToDelete  .  add  (  f  )  ; 
} 
} 
return   f  ; 
} 
















public   static   void   flush  (  Object   o  )  throws   IOException  { 
if  (  null  !=  o  )  { 
if  (  !  flushIfPossible  (  o  )  )  { 
throw   new   IllegalArgumentException  (  "Not Flushable: "  +  o  .  getClass  (  )  )  ; 
} 
} 
} 















public   static   boolean   flushIfPossible  (  Object   o  )  throws   IOException  { 
boolean   possible  ; 
if  (  null  ==  o  )  { 
possible  =  false  ; 
}  else   if  (  o   instanceof   Flushable  )  { 
(  (  Flushable  )  o  )  .  flush  (  )  ; 
possible  =  true  ; 
}  else  { 
try  { 
Method   m  =  o  .  getClass  (  )  .  getMethod  (  "flush"  )  ; 
m  .  invoke  (  o  )  ; 
possible  =  true  ; 
}  catch  (  NoSuchMethodException   e  )  { 
return   false  ; 
}  catch  (  InvocationTargetException   e  )  { 
Throwable   cause  =  e  .  getCause  (  )  ; 
if  (  cause   instanceof   RuntimeException  )  { 
throw  (  RuntimeException  )  cause  ; 
}  else  { 
throw   new   RuntimeException  (  cause  )  ; 
} 
}  catch  (  IllegalAccessException   e  )  { 
possible  =  false  ; 
} 
} 
return   possible  ; 
} 








public   static   int   findFreePort  (  )  throws   IOException  { 
ServerSocket   ss  =  new   ServerSocket  (  0  )  ; 
try  { 
return   ss  .  getLocalPort  (  )  ; 
}  finally  { 
ss  .  close  (  )  ; 
} 
} 












public   static   boolean   touch  (  File   f  )  throws   IOException  { 
boolean   created  =  !  f  .  exists  (  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  f  ,  true  )  ; 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
return   created  ; 
} 












public   static   boolean   deleteRecursive  (  File   f  )  throws   IOException  { 
boolean   deleted  ; 
if  (  !  f  .  exists  (  )  )  { 
deleted  =  false  ; 
}  else   if  (  f  .  isFile  (  )  )  { 
deleted  =  f  .  delete  (  )  ; 
}  else  { 
File  [  ]  children  =  f  .  listFiles  (  )  ; 
for  (  File   child  :  children  )  { 
deleteRecursive  (  child  )  ; 
} 
deleted  =  f  .  delete  (  )  ; 
} 
return   deleted  ; 
} 
} 

