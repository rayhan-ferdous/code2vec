package   fi  .  hip  .  gb  .  utils  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLClassLoader  ; 
import   java  .  rmi  .  RemoteException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  jar  .  JarEntry  ; 
import   java  .  util  .  jar  .  JarFile  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 







public   class   ClassLoaderUtils   extends   URLClassLoader  { 


private   URL  [  ]  jarFiles  ; 

private   static   Log   log  =  LogFactory  .  getLog  (  ClassLoaderUtils  .  class  )  ; 












public   ClassLoaderUtils  (  Object   parent  ,  URL  [  ]  files  )  throws   IOException  { 
super  (  files  ,  parent  .  getClass  (  )  .  getClassLoader  (  )  )  ; 
this  .  jarFiles  =  files  ; 
} 















public   static   URL  [  ]  prepareJars  (  URL  [  ]  files  ,  String   destinationDir  )  throws   IOException  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  getProtocol  (  )  .  toLowerCase  (  )  .  equals  (  "jar"  )  )  { 
files  [  i  ]  =  FileUtils  .  convertFromJarURL  (  files  [  i  ]  )  ; 
} 
String   filename  =  FileUtils  .  getFilename  (  files  [  i  ]  .  getFile  (  )  )  ; 
if  (  destinationDir  !=  null  )  { 
File   outputfile  =  File  .  createTempFile  (  filename  ,  null  ,  new   File  (  destinationDir  )  )  ; 
outputfile  .  deleteOnExit  (  )  ; 
log  .  debug  (  "JAR file "  +  files  [  i  ]  .  toString  (  )  +  " has temporary copy at "  +  outputfile  .  getPath  (  )  )  ; 
files  [  i  ]  =  FileUtils  .  copyFile  (  files  [  i  ]  ,  outputfile  )  ; 
} 
} 
return   files  ; 
} 









public   static   boolean   typeCheck  (  Class   c  ,  Object   o  )  { 
if  (  c  .  isInstance  (  o  )  )  { 
return   true  ; 
}  else   if  (  o  ==  null  )  { 
return   true  ; 
}  else   if  (  c  .  isPrimitive  (  )  )  { 
return  (  c  ==  Boolean  .  TYPE  &&  Boolean  .  class  .  isInstance  (  o  )  ||  c  ==  Character  .  TYPE  &&  Character  .  class  .  isInstance  (  o  )  ||  c  ==  Byte  .  TYPE  &&  Byte  .  class  .  isInstance  (  o  )  ||  c  ==  Short  .  TYPE  &&  Short  .  class  .  isInstance  (  o  )  ||  c  ==  Integer  .  TYPE  &&  Integer  .  class  .  isInstance  (  o  )  ||  c  ==  Long  .  TYPE  &&  Long  .  class  .  isInstance  (  o  )  ||  c  ==  Float  .  TYPE  &&  Float  .  class  .  isInstance  (  o  )  ||  c  ==  Double  .  TYPE  &&  Double  .  class  .  isInstance  (  o  )  )  ; 
} 
return   false  ; 
} 









public   Object   loadInstance  (  String   className  )  throws   RemoteException  { 
try  { 
Class   c  =  loadClass  (  className  )  ; 
return   c  .  newInstance  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RemoteException  (  "Could not load class "  +  className  +  " : "  +  e  .  getMessage  (  )  +  "\n"  +  TextUtils  .  getStackTrace  (  e  )  )  ; 
} 
} 









public   Object   loadInstance  (  String   className  ,  Object  [  ]  initArgs  )  throws   RemoteException  { 
try  { 
Class   typeClass  =  loadClass  (  className  )  ; 
constructors  :  for  (  Constructor   typeConstr  :  typeClass  .  getConstructors  (  )  )  { 
if  (  typeConstr  .  getParameterTypes  (  )  .  length  ==  initArgs  .  length  )  { 
for  (  int   i  =  0  ;  i  <  initArgs  .  length  ;  i  ++  )  { 
if  (  !  typeCheck  (  typeConstr  .  getParameterTypes  (  )  [  i  ]  ,  initArgs  [  i  ]  )  )  { 
continue   constructors  ; 
} 
} 
Object   o  =  (  initArgs  !=  null  )  ?  typeConstr  .  newInstance  (  initArgs  )  :  typeConstr  .  newInstance  (  new   Object  [  ]  {  }  )  ; 
return   o  ; 
} 
} 
throw   new   IllegalArgumentException  (  "Constructor not found"  )  ; 
}  catch  (  Exception   e  )  { 
String   args  =  ""  ; 
for  (  Object   m  :  initArgs  )  { 
if  (  args  .  length  (  )  >  0  )  args  +=  ", "  ; 
args  +=  m  .  getClass  (  )  .  getName  (  )  ; 
} 
throw   new   RemoteException  (  "Failed to call constructor "  +  className  +  "("  +  args  +  ") "  +  Arrays  .  toString  (  initArgs  )  +  " : "  +  e  .  getMessage  (  )  +  "\n"  +  TextUtils  .  getStackTrace  (  e  )  )  ; 
} 
} 









public   String  [  ]  getClassNames  (  Class   interfaceType  )  throws   IOException  { 
Class  [  ]  classes  =  getClasses  (  )  ; 
Vector  <  String  >  subclasses  =  new   Vector  <  String  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  classes  .  length  ;  i  ++  )  { 
if  (  interfaceType  ==  null  ||  interfaceType  .  isAssignableFrom  (  classes  [  i  ]  )  )  if  (  classes  [  i  ]  .  getName  (  )  .  indexOf  (  "$"  )  ==  -  1  )  subclasses  .  add  (  classes  [  i  ]  .  getName  (  )  )  ; 
} 
return   subclasses  .  toArray  (  new   String  [  0  ]  )  ; 
} 







public   Class  [  ]  getClasses  (  )  throws   IOException  { 
Vector  <  Class  >  classes  =  new   Vector  <  Class  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  jarFiles  .  length  ;  i  ++  )  { 
try  { 
JarFile   file  =  new   JarFile  (  FileUtils  .  convertFromJarURL  (  jarFiles  [  i  ]  )  .  getFile  (  )  )  ; 
for  (  Enumeration  <  JarEntry  >  e  =  file  .  entries  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   classname  =  e  .  nextElement  (  )  .  toString  (  )  ; 
if  (  classname  .  endsWith  (  ".class"  )  )  { 
classname  =  classname  .  substring  (  0  ,  classname  .  indexOf  (  "."  )  )  ; 
classname  =  classname  .  replaceAll  (  "/"  ,  "."  )  ; 
Class   typeClass  =  loadClass  (  classname  )  ; 
classes  .  add  (  typeClass  )  ; 
} 
} 
}  catch  (  ClassNotFoundException   e  )  { 
throw   new   RemoteException  (  "Class not found"  +  e  .  getMessage  (  )  )  ; 
} 
} 
return   classes  .  toArray  (  new   Class  [  0  ]  )  ; 
} 









public   static   Object   getNewInstanceOf  (  String   classname  )  { 
try  { 
Class   typeClass  =  Class  .  forName  (  classname  )  ; 
return   typeClass  .  newInstance  (  )  ; 
}  catch  (  java  .  lang  .  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
return   null  ; 
} 
} 










public   static   Object   getNewInstanceOf  (  String   classname  ,  String   constructor  )  { 
try  { 
Class   typeClass  =  Class  .  forName  (  classname  )  ; 
Class  [  ]  paramTypes  =  new   Class  [  ]  {  (  new   String  (  )  )  .  getClass  (  )  }  ; 
Object  [  ]  initArgs  =  new   Object  [  ]  {  constructor  }  ; 
Constructor   typeConstr  =  typeClass  .  getConstructor  (  paramTypes  )  ; 
return   typeConstr  .  newInstance  (  initArgs  )  ; 
}  catch  (  java  .  lang  .  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
return   null  ; 
} 
} 
} 

