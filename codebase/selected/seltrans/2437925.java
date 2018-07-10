package   org  .  python  .  debug  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   android  .  dalvik  .  DexFile  ; 
import   com  .  google  .  dex  .  cf  .  CfOptions  ; 
import   com  .  google  .  dex  .  cf  .  CfTranslator  ; 
import   com  .  google  .  dex  .  file  .  ClassDefItem  ; 







public   class   FixMe  { 

public   static   String   apkpath  =  "/data/app/"  ; 

public   static   String   apkname  =  "Jythonroid.apk"  ; 

public   static   String   apppath  =  apkpath  +  apkname  ; 

public   static   String   tmpdirpath  =  "/tmp/jythonroid/"  ; 

public   static   boolean   isinitialized  =  false  ; 

public   static   boolean   initialize  (  )  { 
File   tdp  =  new   File  (  tmpdirpath  )  ; 
if  (  !  tdp  .  exists  (  )  )  { 
tdp  .  mkdir  (  )  ; 
}  else  { 
if  (  !  tdp  .  isDirectory  (  )  )  { 
return   false  ; 
} 
} 
isinitialized  =  true  ; 
return   true  ; 
} 










public   static   Class  <  ?  >  getDeclaringClass  (  Class  <  ?  >  c  )  throws   ClassNotFoundException  { 
try  { 
Class  <  ?  >  result  =  c  .  getDeclaringClass  (  )  ; 
return   result  ; 
}  catch  (  Exception   e  )  { 
String  [  ]  elements  =  c  .  getName  (  )  .  replace  (  '.'  ,  '/'  )  .  split  (  "\\$"  )  ; 
String   name  =  elements  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  elements  .  length  -  1  ;  i  ++  )  { 
name  +=  "$"  +  elements  [  i  ]  ; 
} 
if  (  elements  .  length  ==  1  )  { 
return   null  ; 
}  else  { 
return   getClassByName  (  apkpath  +  apkname  ,  name  )  ; 
} 
} 
} 







public   static   Class  <  ?  >  getClassByName  (  String   classname  )  { 
return   getClassByName  (  apkpath  +  apkname  ,  classname  )  ; 
} 











public   static   Class  <  ?  >  getClassByName  (  String   filename  ,  String   classname  )  { 
try  { 
DexFile   f  =  new   DexFile  (  new   File  (  filename  )  )  ; 
Class  <  ?  >  s  =  f  .  loadClass  (  classname  ,  ClassLoader  .  getSystemClassLoader  (  )  )  ; 
return   s  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 









public   static   Class  <  ?  >  getInnerClassByName  (  Class  <  ?  >  c  ,  String   name  )  { 
Class  <  ?  >  [  ]  inners  =  c  .  getClasses  (  )  ; 
for  (  int   i  =  0  ;  i  <  inners  .  length  ;  i  ++  )  { 
if  (  inners  [  i  ]  .  getName  (  )  .  equals  (  name  )  )  { 
return   inners  [  i  ]  ; 
} 
} 
return   null  ; 
} 







public   static   boolean   isInnerClass  (  Class  <  ?  >  c  )  { 
String   name  =  c  .  getName  (  )  ; 
if  (  name  .  contains  (  "$"  )  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 







public   static   boolean   isOuterClass  (  Class  <  ?  >  c  )  { 
Class  <  ?  >  [  ]  inners  =  c  .  getClasses  (  )  ; 
if  (  inners  .  length  ==  0  )  { 
return   false  ; 
}  else  { 
return   true  ; 
} 
} 

public   static   Class  <  ?  >  getClass  (  byte  [  ]  bytecode  )  { 
return   null  ; 
} 

public   static   Class  <  ?  >  getClass  (  File   apkFile  )  { 
return   null  ; 
} 

private   static   String   fixPath  (  String   path  )  { 
if  (  File  .  separatorChar  ==  '\\'  )  path  =  path  .  replace  (  '\\'  ,  '/'  )  ; 
int   index  =  path  .  lastIndexOf  (  "/./"  )  ; 
if  (  index  !=  -  1  )  return   path  .  substring  (  index  +  3  )  ; 
if  (  path  .  startsWith  (  "./"  )  )  return   path  .  substring  (  2  )  ;  else   return   path  ; 
} 








public   static   Class  <  ?  >  getDexClass  (  String   name  ,  byte  [  ]  data  )  throws   IOException  { 
File   fff  =  new   File  (  "/tmp/jvm.class"  )  ; 
if  (  !  fff  .  exists  (  )  )  { 
fff  .  createNewFile  (  )  ; 
} 
FileOutputStream   fos  =  new   FileOutputStream  (  fff  )  ; 
fos  .  write  (  data  )  ; 
fos  .  close  (  )  ; 
com  .  google  .  dex  .  file  .  DexFile   outputDex  =  new   com  .  google  .  dex  .  file  .  DexFile  (  )  ; 
CfOptions   cf  =  new   CfOptions  (  )  ; 
ClassDefItem   clazz  =  CfTranslator  .  translate  (  fixPath  (  name  .  replace  (  '.'  ,  '/'  )  +  ".class"  )  ,  data  ,  cf  )  ; 
outputDex  .  add  (  clazz  )  ; 
File   tmpdir  =  new   File  (  tmpdirpath  +  name  )  ; 
if  (  !  tmpdir  .  exists  (  )  )  { 
tmpdir  .  mkdir  (  )  ; 
}  else  { 
if  (  !  tmpdir  .  isDirectory  (  )  )  { 
throw   new   IOException  (  )  ; 
} 
} 
File   apk  =  new   File  (  tmpdirpath  +  name  +  "/"  +  name  +  ".apk"  )  ; 
if  (  !  apk  .  exists  (  )  )  { 
apk  .  createNewFile  (  )  ; 
} 
ZipOutputStream   zos  =  new   ZipOutputStream  (  new   FileOutputStream  (  apk  )  )  ; 
ZipEntry   classeszip  =  new   ZipEntry  (  "classes.dex"  )  ; 
zos  .  putNextEntry  (  classeszip  )  ; 
outputDex  .  writeTo  (  zos  ,  null  ,  false  )  ; 
zos  .  closeEntry  (  )  ; 
zos  .  close  (  )  ; 
getClassByName  (  apppath  ,  "org/python/core/PyFunctionTable"  )  ; 
getClassByName  (  apppath  ,  "org/python/core/PyRunnable"  )  ; 
Class  <  ?  >  c  =  getClassByName  (  tmpdirpath  +  name  +  "/"  +  name  +  ".apk"  ,  name  .  replace  (  '.'  ,  '/'  )  )  ; 
getClassByName  (  apppath  ,  "org/python/core/PyFunctionTable"  )  ; 
return   c  ; 
} 

public   static   Object   newInstance  (  Constructor  <  ?  >  cst  ,  Object  [  ]  objects  )  { 
Thread  .  currentThread  (  )  .  setContextClassLoader  (  ClassLoader  .  getSystemClassLoader  (  )  )  ; 
Class  <  ?  >  a  =  null  ; 
try  { 
a  =  Class  .  forName  (  "org.python.core.PyObject"  )  ; 
Constructor   cs  =  a  .  getConstructor  (  new   Class  [  ]  {  }  )  ; 
cs  .  newInstance  (  new   Object  (  )  { 
}  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  SecurityException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  NoSuchMethodException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  InstantiationException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   a  ; 
} 

public   static   void   resolveClass  (  Class  <  ?  >  c  )  { 
} 
} 

