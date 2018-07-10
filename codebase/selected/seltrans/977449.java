package   org  .  mobicents  .  servlet  .  sip  .  annotations  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilePermission  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  lang  .  reflect  .  Field  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLClassLoader  ; 
import   java  .  security  .  AccessControlException  ; 
import   java  .  security  .  AccessController  ; 
import   java  .  security  .  CodeSource  ; 
import   java  .  security  .  Permission  ; 
import   java  .  security  .  PermissionCollection  ; 
import   java  .  security  .  Policy  ; 
import   java  .  security  .  PrivilegedAction  ; 
import   java  .  sql  .  Driver  ; 
import   java  .  sql  .  DriverManager  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  jar  .  Attributes  ; 
import   java  .  util  .  jar  .  JarEntry  ; 
import   java  .  util  .  jar  .  JarFile  ; 
import   java  .  util  .  jar  .  Manifest  ; 
import   java  .  util  .  jar  .  Attributes  .  Name  ; 
import   javax  .  naming  .  NameClassPair  ; 
import   javax  .  naming  .  NamingEnumeration  ; 
import   javax  .  naming  .  NamingException  ; 
import   javax  .  naming  .  directory  .  DirContext  ; 
import   org  .  apache  .  catalina  .  Lifecycle  ; 
import   org  .  apache  .  catalina  .  LifecycleException  ; 
import   org  .  apache  .  catalina  .  LifecycleListener  ; 
import   org  .  apache  .  catalina  .  LifecycleState  ; 
import   org  .  apache  .  catalina  .  loader  .  Constants  ; 
import   org  .  apache  .  catalina  .  loader  .  ResourceEntry  ; 
import   org  .  apache  .  tomcat  .  util  .  res  .  StringManager  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  naming  .  JndiPermission  ; 
import   org  .  apache  .  naming  .  resources  .  Resource  ; 
import   org  .  apache  .  naming  .  resources  .  ResourceAttributes  ; 
import   org  .  apache  .  tomcat  .  util  .  IntrospectionUtils  ; 








































public   class   AnnotationsClassLoader   extends   URLClassLoader   implements   Lifecycle  { 

private   static   final   transient   Logger   log  =  Logger  .  getLogger  (  AnnotationsClassLoader  .  class  )  ; 

public   static   final   boolean   ENABLE_CLEAR_REFERENCES  =  Boolean  .  valueOf  (  System  .  getProperty  (  "org.apache.catalina.loader.WebappClassLoader.ENABLE_CLEAR_REFERENCES"  ,  "true"  )  )  .  booleanValue  (  )  ; 

protected   class   PrivilegedFindResource   implements   PrivilegedAction  <  Object  >  { 

protected   File   file  ; 

protected   String   path  ; 

PrivilegedFindResource  (  File   file  ,  String   path  )  { 
this  .  file  =  file  ; 
this  .  path  =  path  ; 
} 

public   Object   run  (  )  { 
return   findResourceInternal  (  file  ,  path  )  ; 
} 
} 









static   final   String  [  ]  triggers  =  {  "javax.servlet.Servlet"  }  ; 





protected   static   final   String  [  ]  packageTriggers  =  {  }  ; 




protected   static   final   StringManager   sm  =  StringManager  .  getManager  (  Constants  .  Package  )  ; 





boolean   antiJARLocking  =  false  ; 





public   AnnotationsClassLoader  (  )  { 
super  (  new   URL  [  0  ]  )  ; 
this  .  parent  =  getParent  (  )  ; 
system  =  getSystemClassLoader  (  )  ; 
securityManager  =  System  .  getSecurityManager  (  )  ; 
if  (  securityManager  !=  null  )  { 
refreshPolicy  (  )  ; 
} 
} 





public   AnnotationsClassLoader  (  ClassLoader   parent  )  { 
super  (  new   URL  [  0  ]  ,  parent  )  ; 
this  .  parent  =  getParent  (  )  ; 
system  =  getSystemClassLoader  (  )  ; 
securityManager  =  System  .  getSecurityManager  (  )  ; 
if  (  securityManager  !=  null  )  { 
refreshPolicy  (  )  ; 
} 
} 





protected   DirContext   resources  =  null  ; 





protected   Map  <  String  ,  ResourceEntry  >  resourceEntries  =  new   HashMap  <  String  ,  ResourceEntry  >  (  )  ; 




protected   Map  <  String  ,  String  >  notFoundResources  =  new   HashMap  <  String  ,  String  >  (  )  ; 









protected   boolean   delegate  =  false  ; 




protected   long   lastJarAccessed  =  0L  ; 





protected   String  [  ]  repositories  =  new   String  [  0  ]  ; 




protected   URL  [  ]  repositoryURLs  =  null  ; 






protected   File  [  ]  files  =  new   File  [  0  ]  ; 





protected   JarFile  [  ]  jarFiles  =  new   JarFile  [  0  ]  ; 





protected   File  [  ]  jarRealFiles  =  new   File  [  0  ]  ; 




protected   String   jarPath  =  null  ; 





protected   String  [  ]  jarNames  =  new   String  [  0  ]  ; 





protected   long  [  ]  lastModifiedDates  =  new   long  [  0  ]  ; 





protected   String  [  ]  paths  =  new   String  [  0  ]  ; 





protected   ArrayList   permissionList  =  new   ArrayList  (  )  ; 




protected   File   loaderDir  =  null  ; 





protected   Map   loaderPC  =  new   HashMap  (  )  ; 




protected   SecurityManager   securityManager  =  null  ; 




protected   ClassLoader   parent  =  null  ; 




protected   ClassLoader   system  =  null  ; 





protected   boolean   started  =  true  ; 




protected   boolean   hasExternalRepositories  =  false  ; 




protected   boolean   needConvert  =  false  ; 




protected   Permission   allPermission  =  new   java  .  security  .  AllPermission  (  )  ; 




public   DirContext   getResources  (  )  { 
return   this  .  resources  ; 
} 




public   void   setResources  (  DirContext   resources  )  { 
this  .  resources  =  resources  ; 
} 




public   boolean   getDelegate  (  )  { 
return  (  this  .  delegate  )  ; 
} 






public   void   setDelegate  (  boolean   delegate  )  { 
this  .  delegate  =  delegate  ; 
} 




public   boolean   getAntiJARLocking  (  )  { 
return   antiJARLocking  ; 
} 




public   void   setAntiJARLocking  (  boolean   antiJARLocking  )  { 
this  .  antiJARLocking  =  antiJARLocking  ; 
} 







public   void   addPermission  (  String   path  )  { 
if  (  path  ==  null  )  { 
return  ; 
} 
if  (  securityManager  !=  null  )  { 
Permission   permission  =  null  ; 
if  (  path  .  startsWith  (  "jndi:"  )  ||  path  .  startsWith  (  "jar:jndi:"  )  )  { 
if  (  !  path  .  endsWith  (  "/"  )  )  { 
path  =  path  +  "/"  ; 
} 
permission  =  new   JndiPermission  (  path  +  "*"  )  ; 
addPermission  (  permission  )  ; 
}  else  { 
if  (  !  path  .  endsWith  (  File  .  separator  )  )  { 
permission  =  new   FilePermission  (  path  ,  "read"  )  ; 
addPermission  (  permission  )  ; 
path  =  path  +  File  .  separator  ; 
} 
permission  =  new   FilePermission  (  path  +  "-"  ,  "read"  )  ; 
addPermission  (  permission  )  ; 
} 
} 
} 







public   void   addPermission  (  URL   url  )  { 
if  (  url  !=  null  )  { 
addPermission  (  url  .  toString  (  )  )  ; 
} 
} 






public   void   addPermission  (  Permission   permission  )  { 
if  (  (  securityManager  !=  null  )  &&  (  permission  !=  null  )  )  { 
permissionList  .  add  (  permission  )  ; 
} 
} 




public   String   getJarPath  (  )  { 
return   this  .  jarPath  ; 
} 




public   void   setJarPath  (  String   jarPath  )  { 
this  .  jarPath  =  jarPath  ; 
} 




public   void   setWorkDir  (  File   workDir  )  { 
this  .  loaderDir  =  new   File  (  workDir  ,  "loader"  )  ; 
} 





protected   void   setParentClassLoader  (  ClassLoader   pcl  )  { 
parent  =  pcl  ; 
} 











public   void   addRepository  (  String   repository  )  { 
if  (  repository  .  startsWith  (  "/WEB-INF/lib"  )  ||  repository  .  startsWith  (  "/WEB-INF/classes"  )  )  return  ; 
try  { 
URL   url  =  new   URL  (  repository  )  ; 
super  .  addURL  (  url  )  ; 
hasExternalRepositories  =  true  ; 
repositoryURLs  =  null  ; 
}  catch  (  MalformedURLException   e  )  { 
IllegalArgumentException   iae  =  new   IllegalArgumentException  (  "Invalid repository: "  +  repository  )  ; 
iae  .  initCause  (  e  )  ; 
throw   iae  ; 
} 
} 











synchronized   void   addRepository  (  String   repository  ,  File   file  )  { 
if  (  repository  ==  null  )  return  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "addRepository("  +  repository  +  ")"  )  ; 
int   i  ; 
String  [  ]  result  =  new   String  [  repositories  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  repositories  .  length  ;  i  ++  )  { 
result  [  i  ]  =  repositories  [  i  ]  ; 
} 
result  [  repositories  .  length  ]  =  repository  ; 
repositories  =  result  ; 
File  [  ]  result2  =  new   File  [  files  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
result2  [  i  ]  =  files  [  i  ]  ; 
} 
result2  [  files  .  length  ]  =  file  ; 
files  =  result2  ; 
} 

public   void   addJarDir  (  String   dirPath  )  { 
File   dir  =  new   File  (  dirPath  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
log  .  info  (  "No libraries loaded from this directory: "  +  dir  .  getAbsolutePath  (  )  )  ; 
return  ; 
} 
File  [  ]  files  =  dir  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
if  (  !  file  .  isDirectory  (  )  &&  file  .  getName  (  )  .  indexOf  (  ".jar"  )  !=  -  1  )  { 
try  { 
addJar  (  file  .  getName  (  )  ,  new   JarFile  (  file  )  ,  file  )  ; 
}  catch  (  IOException   e  )  { 
log  .  error  (  "An exception occured when trying to add the following jar to the AnnotationsClassLoader : "  +  file  .  getAbsolutePath  (  )  ,  e  )  ; 
} 
}  else  { 
log  .  info  (  file  .  getAbsolutePath  (  )  +  " is a directory in "  +  dirPath  +  " and as such will be skipped."  )  ; 
} 
} 
} 

synchronized   void   addJar  (  String   jar  ,  JarFile   jarFile  ,  File   file  )  throws   IOException  { 
if  (  jar  ==  null  )  return  ; 
if  (  jarFile  ==  null  )  return  ; 
if  (  file  ==  null  )  return  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "addJar("  +  jar  +  ")"  )  ; 
int   i  ; 
if  (  (  jarPath  !=  null  )  &&  (  jar  .  startsWith  (  jarPath  )  )  )  { 
String   jarName  =  jar  .  substring  (  jarPath  .  length  (  )  )  ; 
while  (  jarName  .  startsWith  (  "/"  )  )  jarName  =  jarName  .  substring  (  1  )  ; 
String  [  ]  result  =  new   String  [  jarNames  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  jarNames  .  length  ;  i  ++  )  { 
result  [  i  ]  =  jarNames  [  i  ]  ; 
} 
result  [  jarNames  .  length  ]  =  jarName  ; 
jarNames  =  result  ; 
} 
try  { 
long   lastModified  =  (  (  ResourceAttributes  )  resources  .  getAttributes  (  jar  )  )  .  getLastModified  (  )  ; 
String  [  ]  result  =  new   String  [  paths  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  paths  .  length  ;  i  ++  )  { 
result  [  i  ]  =  paths  [  i  ]  ; 
} 
result  [  paths  .  length  ]  =  jar  ; 
paths  =  result  ; 
long  [  ]  result3  =  new   long  [  lastModifiedDates  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  lastModifiedDates  .  length  ;  i  ++  )  { 
result3  [  i  ]  =  lastModifiedDates  [  i  ]  ; 
} 
result3  [  lastModifiedDates  .  length  ]  =  lastModified  ; 
lastModifiedDates  =  result3  ; 
}  catch  (  NamingException   e  )  { 
} 
if  (  !  validateJarFile  (  file  )  )  return  ; 
JarFile  [  ]  result2  =  new   JarFile  [  jarFiles  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  jarFiles  .  length  ;  i  ++  )  { 
result2  [  i  ]  =  jarFiles  [  i  ]  ; 
} 
result2  [  jarFiles  .  length  ]  =  jarFile  ; 
jarFiles  =  result2  ; 
File  [  ]  result4  =  new   File  [  jarRealFiles  .  length  +  1  ]  ; 
for  (  i  =  0  ;  i  <  jarRealFiles  .  length  ;  i  ++  )  { 
result4  [  i  ]  =  jarRealFiles  [  i  ]  ; 
} 
result4  [  jarRealFiles  .  length  ]  =  file  ; 
jarRealFiles  =  result4  ; 
} 







public   String  [  ]  findRepositories  (  )  { 
return  (  (  String  [  ]  )  repositories  .  clone  (  )  )  ; 
} 





public   boolean   modified  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "modified()"  )  ; 
int   length  =  paths  .  length  ; 
int   length2  =  lastModifiedDates  .  length  ; 
if  (  length  >  length2  )  length  =  length2  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
try  { 
long   lastModified  =  (  (  ResourceAttributes  )  resources  .  getAttributes  (  paths  [  i  ]  )  )  .  getLastModified  (  )  ; 
if  (  lastModified  !=  lastModifiedDates  [  i  ]  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Resource '"  +  paths  [  i  ]  +  "' was modified; Date is now: "  +  new   java  .  util  .  Date  (  lastModified  )  +  " Was: "  +  new   java  .  util  .  Date  (  lastModifiedDates  [  i  ]  )  )  ; 
return  (  true  )  ; 
} 
}  catch  (  NamingException   e  )  { 
log  .  error  (  "    Resource '"  +  paths  [  i  ]  +  "' is missing"  )  ; 
return  (  true  )  ; 
} 
} 
length  =  jarNames  .  length  ; 
if  (  getJarPath  (  )  !=  null  )  { 
try  { 
NamingEnumeration   enumeration  =  resources  .  listBindings  (  getJarPath  (  )  )  ; 
int   i  =  0  ; 
while  (  enumeration  .  hasMoreElements  (  )  &&  (  i  <  length  )  )  { 
NameClassPair   ncPair  =  (  NameClassPair  )  enumeration  .  nextElement  (  )  ; 
String   name  =  ncPair  .  getName  (  )  ; 
if  (  !  name  .  endsWith  (  ".jar"  )  )  continue  ; 
if  (  !  name  .  equals  (  jarNames  [  i  ]  )  )  { 
log  .  info  (  "    Additional JARs have been added : '"  +  name  +  "'"  )  ; 
return  (  true  )  ; 
} 
i  ++  ; 
} 
if  (  enumeration  .  hasMoreElements  (  )  )  { 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
NameClassPair   ncPair  =  (  NameClassPair  )  enumeration  .  nextElement  (  )  ; 
String   name  =  ncPair  .  getName  (  )  ; 
if  (  name  .  endsWith  (  ".jar"  )  )  { 
log  .  info  (  "    Additional JARs have been added"  )  ; 
return  (  true  )  ; 
} 
} 
}  else   if  (  i  <  jarNames  .  length  )  { 
log  .  info  (  "    Additional JARs have been added"  )  ; 
return  (  true  )  ; 
} 
}  catch  (  NamingException   e  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "    Failed tracking modifications of '"  +  getJarPath  (  )  +  "'"  )  ; 
}  catch  (  ClassCastException   e  )  { 
log  .  error  (  "    Failed tracking modifications of '"  +  getJarPath  (  )  +  "' : "  +  e  .  getMessage  (  )  )  ; 
} 
} 
return  (  false  )  ; 
} 




public   String   toString  (  )  { 
StringBuffer   sb  =  new   StringBuffer  (  "WebappClassLoader\r\n"  )  ; 
sb  .  append  (  "  delegate: "  )  ; 
sb  .  append  (  delegate  )  ; 
sb  .  append  (  "\r\n"  )  ; 
sb  .  append  (  "  repositories:\r\n"  )  ; 
if  (  repositories  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  repositories  .  length  ;  i  ++  )  { 
sb  .  append  (  "    "  )  ; 
sb  .  append  (  repositories  [  i  ]  )  ; 
sb  .  append  (  "\r\n"  )  ; 
} 
} 
if  (  this  .  parent  !=  null  )  { 
sb  .  append  (  "----------> Parent Classloader:\r\n"  )  ; 
sb  .  append  (  this  .  parent  .  toString  (  )  )  ; 
sb  .  append  (  "\r\n"  )  ; 
} 
return  (  sb  .  toString  (  )  )  ; 
} 




protected   void   addURL  (  URL   url  )  { 
super  .  addURL  (  url  )  ; 
hasExternalRepositories  =  true  ; 
repositoryURLs  =  null  ; 
} 









public   Class   findClass  (  String   name  )  throws   ClassNotFoundException  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "    findClass("  +  name  +  ")"  )  ; 
if  (  !  started  )  { 
throw   new   ClassNotFoundException  (  name  )  ; 
} 
if  (  securityManager  !=  null  )  { 
int   i  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  i  >=  0  )  { 
try  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "      securityManager.checkPackageDefinition"  )  ; 
securityManager  .  checkPackageDefinition  (  name  .  substring  (  0  ,  i  )  )  ; 
}  catch  (  Exception   se  )  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "      -->Exception-->ClassNotFoundException"  ,  se  )  ; 
throw   new   ClassNotFoundException  (  name  ,  se  )  ; 
} 
} 
} 
Class   clazz  =  null  ; 
try  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "      findClassInternal("  +  name  +  ")"  )  ; 
try  { 
clazz  =  findClassInternal  (  name  )  ; 
}  catch  (  ClassNotFoundException   cnfe  )  { 
if  (  !  hasExternalRepositories  )  { 
throw   cnfe  ; 
} 
}  catch  (  AccessControlException   ace  )  { 
throw   new   ClassNotFoundException  (  name  ,  ace  )  ; 
}  catch  (  RuntimeException   e  )  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "      -->RuntimeException Rethrown"  ,  e  )  ; 
throw   e  ; 
} 
if  (  (  clazz  ==  null  )  &&  hasExternalRepositories  )  { 
try  { 
clazz  =  super  .  findClass  (  name  )  ; 
}  catch  (  AccessControlException   ace  )  { 
throw   new   ClassNotFoundException  (  name  ,  ace  )  ; 
}  catch  (  RuntimeException   e  )  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "      -->RuntimeException Rethrown"  ,  e  )  ; 
throw   e  ; 
} 
} 
if  (  clazz  ==  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "    --> Returning ClassNotFoundException"  )  ; 
throw   new   ClassNotFoundException  (  name  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "    --> Passing on ClassNotFoundException"  )  ; 
throw   e  ; 
} 
if  (  log  .  isTraceEnabled  (  )  )  log  .  debug  (  "      Returning class "  +  clazz  )  ; 
if  (  log  .  isTraceEnabled  (  )  )  log  .  debug  (  "      Loaded by "  +  clazz  .  getClassLoader  (  )  )  ; 
return  (  clazz  )  ; 
} 








public   URL   findResource  (  final   String   name  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "    findResource("  +  name  +  ")"  )  ; 
URL   url  =  null  ; 
ResourceEntry   entry  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
if  (  entry  ==  null  )  { 
entry  =  findResourceInternal  (  name  ,  name  )  ; 
} 
if  (  entry  !=  null  )  { 
url  =  entry  .  source  ; 
} 
if  (  (  url  ==  null  )  &&  hasExternalRepositories  )  url  =  super  .  findResource  (  name  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
if  (  url  !=  null  )  log  .  debug  (  "    --> Returning '"  +  url  .  toString  (  )  +  "'"  )  ;  else   log  .  debug  (  "    --> Resource not found, returning null"  )  ; 
} 
return  (  url  )  ; 
} 










public   Enumeration   findResources  (  String   name  )  throws   IOException  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "    findResources("  +  name  +  ")"  )  ; 
Vector   result  =  new   Vector  (  )  ; 
int   jarFilesLength  =  jarFiles  .  length  ; 
int   repositoriesLength  =  repositories  .  length  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  repositoriesLength  ;  i  ++  )  { 
try  { 
String   fullPath  =  repositories  [  i  ]  +  name  ; 
resources  .  lookup  (  fullPath  )  ; 
try  { 
result  .  addElement  (  getURI  (  new   File  (  files  [  i  ]  ,  name  )  )  )  ; 
}  catch  (  MalformedURLException   e  )  { 
} 
}  catch  (  NamingException   e  )  { 
} 
} 
synchronized  (  jarFiles  )  { 
if  (  openJARs  (  )  )  { 
for  (  i  =  0  ;  i  <  jarFilesLength  ;  i  ++  )  { 
JarEntry   jarEntry  =  jarFiles  [  i  ]  .  getJarEntry  (  name  )  ; 
if  (  jarEntry  !=  null  )  { 
try  { 
String   jarFakeUrl  =  getURI  (  jarRealFiles  [  i  ]  )  .  toString  (  )  ; 
jarFakeUrl  =  "jar:"  +  jarFakeUrl  +  "!/"  +  name  ; 
result  .  addElement  (  new   URL  (  jarFakeUrl  )  )  ; 
}  catch  (  MalformedURLException   e  )  { 
} 
} 
} 
} 
} 
if  (  hasExternalRepositories  )  { 
Enumeration   otherResourcePaths  =  super  .  findResources  (  name  )  ; 
while  (  otherResourcePaths  .  hasMoreElements  (  )  )  { 
result  .  addElement  (  otherResourcePaths  .  nextElement  (  )  )  ; 
} 
} 
return   result  .  elements  (  )  ; 
} 























public   URL   getResource  (  String   name  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "getResource("  +  name  +  ")"  )  ; 
URL   url  =  null  ; 
if  (  delegate  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Delegating to parent classloader "  +  parent  )  ; 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
url  =  loader  .  getResource  (  name  )  ; 
if  (  url  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning '"  +  url  .  toString  (  )  +  "'"  )  ; 
return  (  url  )  ; 
} 
} 
url  =  findResource  (  name  )  ; 
if  (  url  !=  null  )  { 
if  (  antiJARLocking  )  { 
ResourceEntry   entry  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
try  { 
String   repository  =  entry  .  codeBase  .  toString  (  )  ; 
if  (  (  repository  .  endsWith  (  ".jar"  )  )  &&  (  !  (  name  .  endsWith  (  ".class"  )  )  )  )  { 
File   resourceFile  =  new   File  (  loaderDir  ,  name  )  ; 
url  =  getURI  (  resourceFile  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning '"  +  url  .  toString  (  )  +  "'"  )  ; 
return  (  url  )  ; 
} 
if  (  !  delegate  )  { 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
url  =  loader  .  getResource  (  name  )  ; 
if  (  url  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning '"  +  url  .  toString  (  )  +  "'"  )  ; 
return  (  url  )  ; 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Resource not found, returning null"  )  ; 
return  (  null  )  ; 
} 










public   InputStream   getResourceAsStream  (  String   name  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "getResourceAsStream("  +  name  +  ")"  )  ; 
InputStream   stream  =  null  ; 
stream  =  findLoadedResource  (  name  )  ; 
if  (  stream  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning stream from cache"  )  ; 
return  (  stream  )  ; 
} 
if  (  delegate  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Delegating to parent classloader "  +  parent  )  ; 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
stream  =  loader  .  getResourceAsStream  (  name  )  ; 
if  (  stream  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning stream from parent"  )  ; 
return  (  stream  )  ; 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Searching local repositories"  )  ; 
URL   url  =  findResource  (  name  )  ; 
if  (  url  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning stream from local"  )  ; 
stream  =  findLoadedResource  (  name  )  ; 
try  { 
if  (  hasExternalRepositories  &&  (  stream  ==  null  )  )  stream  =  url  .  openStream  (  )  ; 
}  catch  (  IOException   e  )  { 
; 
} 
if  (  stream  !=  null  )  return  (  stream  )  ; 
} 
if  (  !  delegate  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Delegating to parent classloader unconditionally "  +  parent  )  ; 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
stream  =  loader  .  getResourceAsStream  (  name  )  ; 
if  (  stream  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Returning stream from parent"  )  ; 
return  (  stream  )  ; 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  --> Resource not found, returning null"  )  ; 
return  (  null  )  ; 
} 










public   Class   loadClass  (  String   name  )  throws   ClassNotFoundException  { 
return  (  loadClass  (  name  ,  false  )  )  ; 
} 


























public   Class   loadClass  (  String   name  ,  boolean   resolve  )  throws   ClassNotFoundException  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "loadClass("  +  name  +  ", "  +  resolve  +  ")"  )  ; 
Class   clazz  =  null  ; 
clazz  =  findLoadedClass0  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Returning class from cache"  )  ; 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
clazz  =  findLoadedClass  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Returning class from cache"  )  ; 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
try  { 
clazz  =  system  .  loadClass  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
} 
if  (  securityManager  !=  null  )  { 
int   i  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  i  >=  0  )  { 
try  { 
securityManager  .  checkPackageAccess  (  name  .  substring  (  0  ,  i  )  )  ; 
}  catch  (  SecurityException   se  )  { 
String   error  =  "Security Violation, attempt to use "  +  "Restricted Class: "  +  name  ; 
log  .  info  (  error  ,  se  )  ; 
throw   new   ClassNotFoundException  (  error  ,  se  )  ; 
} 
} 
} 
boolean   delegateLoad  =  delegate  ||  filter  (  name  )  ; 
if  (  delegateLoad  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Delegating to parent classloader1 "  +  parent  )  ; 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
try  { 
clazz  =  loader  .  loadClass  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Loading class from parent"  )  ; 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
; 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Searching local repositories"  )  ; 
try  { 
clazz  =  findClass  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Loading class from local repository"  )  ; 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
; 
} 
if  (  !  delegateLoad  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Delegating to parent classloader at end: "  +  parent  )  ; 
ClassLoader   loader  =  parent  ; 
if  (  loader  ==  null  )  loader  =  system  ; 
try  { 
clazz  =  loader  .  loadClass  (  name  )  ; 
if  (  clazz  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "  Loading class from parent"  )  ; 
if  (  resolve  )  resolveClass  (  clazz  )  ; 
return  (  clazz  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
; 
} 
} 
throw   new   ClassNotFoundException  (  name  )  ; 
} 











protected   PermissionCollection   getPermissions  (  CodeSource   codeSource  )  { 
String   codeUrl  =  codeSource  .  getLocation  (  )  .  toString  (  )  ; 
PermissionCollection   pc  ; 
if  (  (  pc  =  (  PermissionCollection  )  loaderPC  .  get  (  codeUrl  )  )  ==  null  )  { 
pc  =  super  .  getPermissions  (  codeSource  )  ; 
if  (  pc  !=  null  )  { 
Iterator   perms  =  permissionList  .  iterator  (  )  ; 
while  (  perms  .  hasNext  (  )  )  { 
Permission   p  =  (  Permission  )  perms  .  next  (  )  ; 
pc  .  add  (  p  )  ; 
} 
loaderPC  .  put  (  codeUrl  ,  pc  )  ; 
} 
} 
return  (  pc  )  ; 
} 







public   URL  [  ]  getURLs  (  )  { 
if  (  repositoryURLs  !=  null  )  { 
return   repositoryURLs  ; 
} 
URL  [  ]  external  =  super  .  getURLs  (  )  ; 
int   filesLength  =  files  .  length  ; 
int   jarFilesLength  =  jarRealFiles  .  length  ; 
int   length  =  filesLength  +  jarFilesLength  +  external  .  length  ; 
int   i  ; 
try  { 
URL  [  ]  urls  =  new   URL  [  length  ]  ; 
for  (  i  =  0  ;  i  <  length  ;  i  ++  )  { 
if  (  i  <  filesLength  )  { 
urls  [  i  ]  =  getURL  (  files  [  i  ]  ,  true  )  ; 
}  else   if  (  i  <  filesLength  +  jarFilesLength  )  { 
urls  [  i  ]  =  getURL  (  jarRealFiles  [  i  -  filesLength  ]  ,  true  )  ; 
}  else  { 
urls  [  i  ]  =  external  [  i  -  filesLength  -  jarFilesLength  ]  ; 
} 
} 
repositoryURLs  =  urls  ; 
}  catch  (  MalformedURLException   e  )  { 
repositoryURLs  =  new   URL  [  0  ]  ; 
} 
return   repositoryURLs  ; 
} 






public   void   addLifecycleListener  (  LifecycleListener   listener  )  { 
} 





public   LifecycleListener  [  ]  findLifecycleListeners  (  )  { 
return   new   LifecycleListener  [  0  ]  ; 
} 






public   void   removeLifecycleListener  (  LifecycleListener   listener  )  { 
} 






public   void   start  (  )  throws   LifecycleException  { 
started  =  true  ; 
String   encoding  =  null  ; 
try  { 
encoding  =  System  .  getProperty  (  "file.encoding"  )  ; 
}  catch  (  Exception   e  )  { 
return  ; 
} 
if  (  encoding  .  indexOf  (  "EBCDIC"  )  !=  -  1  )  { 
needConvert  =  true  ; 
} 
} 






public   void   stop  (  )  throws   LifecycleException  { 
clearReferences  (  )  ; 
int   length  =  files  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
files  [  i  ]  =  null  ; 
} 
length  =  jarFiles  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
try  { 
if  (  jarFiles  [  i  ]  !=  null  )  { 
jarFiles  [  i  ]  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
jarFiles  [  i  ]  =  null  ; 
} 
notFoundResources  .  clear  (  )  ; 
resourceEntries  .  clear  (  )  ; 
resources  =  null  ; 
repositories  =  null  ; 
repositoryURLs  =  null  ; 
files  =  null  ; 
jarFiles  =  null  ; 
jarRealFiles  =  null  ; 
jarPath  =  null  ; 
jarNames  =  null  ; 
lastModifiedDates  =  null  ; 
paths  =  null  ; 
hasExternalRepositories  =  false  ; 
parent  =  null  ; 
permissionList  .  clear  (  )  ; 
loaderPC  .  clear  (  )  ; 
if  (  loaderDir  !=  null  )  { 
deleteDir  (  loaderDir  )  ; 
} 
} 





public   void   closeJARs  (  boolean   force  )  { 
if  (  jarFiles  .  length  >  0  )  { 
synchronized  (  jarFiles  )  { 
if  (  force  ||  (  System  .  currentTimeMillis  (  )  >  (  lastJarAccessed  +  90000  )  )  )  { 
for  (  int   i  =  0  ;  i  <  jarFiles  .  length  ;  i  ++  )  { 
try  { 
if  (  jarFiles  [  i  ]  !=  null  )  { 
jarFiles  [  i  ]  .  close  (  )  ; 
jarFiles  [  i  ]  =  null  ; 
} 
}  catch  (  IOException   e  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Failed to close JAR"  ,  e  )  ; 
} 
} 
} 
} 
} 
} 
} 




protected   void   clearReferences  (  )  { 
Enumeration   drivers  =  DriverManager  .  getDrivers  (  )  ; 
while  (  drivers  .  hasMoreElements  (  )  )  { 
Driver   driver  =  (  Driver  )  drivers  .  nextElement  (  )  ; 
if  (  driver  .  getClass  (  )  .  getClassLoader  (  )  ==  this  )  { 
try  { 
DriverManager  .  deregisterDriver  (  driver  )  ; 
}  catch  (  SQLException   e  )  { 
log  .  warn  (  "SQL driver deregistration failed"  ,  e  )  ; 
} 
} 
} 
if  (  ENABLE_CLEAR_REFERENCES  )  { 
Iterator   loadedClasses  =  (  (  HashMap  )  (  (  HashMap  )  resourceEntries  )  .  clone  (  )  )  .  values  (  )  .  iterator  (  )  ; 
while  (  loadedClasses  .  hasNext  (  )  )  { 
ResourceEntry   entry  =  (  ResourceEntry  )  loadedClasses  .  next  (  )  ; 
if  (  entry  .  loadedClass  !=  null  )  { 
Class   clazz  =  entry  .  loadedClass  ; 
try  { 
Field  [  ]  fields  =  clazz  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
Field   field  =  fields  [  i  ]  ; 
int   mods  =  field  .  getModifiers  (  )  ; 
if  (  field  .  getType  (  )  .  isPrimitive  (  )  ||  (  field  .  getName  (  )  .  indexOf  (  "$"  )  !=  -  1  )  )  { 
continue  ; 
} 
if  (  Modifier  .  isStatic  (  mods  )  )  { 
try  { 
field  .  setAccessible  (  true  )  ; 
if  (  Modifier  .  isFinal  (  mods  )  )  { 
if  (  !  (  (  field  .  getType  (  )  .  getName  (  )  .  startsWith  (  "java."  )  )  ||  (  field  .  getType  (  )  .  getName  (  )  .  startsWith  (  "javax."  )  )  )  )  { 
nullInstance  (  field  .  get  (  null  )  )  ; 
} 
}  else  { 
field  .  set  (  null  ,  null  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Set field "  +  field  .  getName  (  )  +  " to null in class "  +  clazz  .  getName  (  )  )  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Could not set field "  +  field  .  getName  (  )  +  " to null in class "  +  clazz  .  getName  (  )  ,  t  )  ; 
} 
} 
} 
} 
}  catch  (  Throwable   t  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Could not clean fields for class "  +  clazz  .  getName  (  )  ,  t  )  ; 
} 
} 
} 
} 
} 
IntrospectionUtils  .  clear  (  )  ; 
org  .  apache  .  juli  .  logging  .  LogFactory  .  release  (  this  )  ; 
java  .  beans  .  Introspector  .  flushCaches  (  )  ; 
} 

protected   void   nullInstance  (  Object   instance  )  { 
if  (  instance  ==  null  )  { 
return  ; 
} 
Field  [  ]  fields  =  instance  .  getClass  (  )  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
Field   field  =  fields  [  i  ]  ; 
int   mods  =  field  .  getModifiers  (  )  ; 
if  (  field  .  getType  (  )  .  isPrimitive  (  )  ||  (  field  .  getName  (  )  .  indexOf  (  "$"  )  !=  -  1  )  )  { 
continue  ; 
} 
try  { 
field  .  setAccessible  (  true  )  ; 
if  (  Modifier  .  isStatic  (  mods  )  &&  Modifier  .  isFinal  (  mods  )  )  { 
continue  ; 
}  else  { 
Object   value  =  field  .  get  (  instance  )  ; 
if  (  null  !=  value  )  { 
Class   valueClass  =  value  .  getClass  (  )  ; 
if  (  !  loadedByThisOrChild  (  valueClass  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Not setting field "  +  field  .  getName  (  )  +  " to null in object of class "  +  instance  .  getClass  (  )  .  getName  (  )  +  " because the referenced object was of type "  +  valueClass  .  getName  (  )  +  " which was not loaded by this WebappClassLoader."  )  ; 
} 
}  else  { 
field  .  set  (  instance  ,  null  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Set field "  +  field  .  getName  (  )  +  " to null in class "  +  instance  .  getClass  (  )  .  getName  (  )  )  ; 
} 
} 
} 
} 
}  catch  (  Throwable   t  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Could not set field "  +  field  .  getName  (  )  +  " to null in object instance of class "  +  instance  .  getClass  (  )  .  getName  (  )  ,  t  )  ; 
} 
} 
} 
} 





protected   boolean   loadedByThisOrChild  (  Class   clazz  )  { 
boolean   result  =  false  ; 
for  (  ClassLoader   classLoader  =  clazz  .  getClassLoader  (  )  ;  null  !=  classLoader  ;  classLoader  =  classLoader  .  getParent  (  )  )  { 
if  (  classLoader  .  equals  (  this  )  )  { 
result  =  true  ; 
break  ; 
} 
} 
return   result  ; 
} 




protected   boolean   openJARs  (  )  { 
if  (  started  &&  (  jarFiles  .  length  >  0  )  )  { 
lastJarAccessed  =  System  .  currentTimeMillis  (  )  ; 
if  (  jarFiles  [  0  ]  ==  null  )  { 
for  (  int   i  =  0  ;  i  <  jarFiles  .  length  ;  i  ++  )  { 
try  { 
jarFiles  [  i  ]  =  new   JarFile  (  jarRealFiles  [  i  ]  )  ; 
}  catch  (  IOException   e  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Failed to open JAR"  ,  e  )  ; 
} 
return   false  ; 
} 
} 
} 
} 
return   true  ; 
} 






protected   Class   findClassInternal  (  String   name  )  throws   ClassNotFoundException  { 
if  (  !  validate  (  name  )  )  throw   new   ClassNotFoundException  (  name  )  ; 
String   tempPath  =  name  .  replace  (  '.'  ,  '/'  )  ; 
String   classPath  =  tempPath  +  ".class"  ; 
ResourceEntry   entry  =  null  ; 
entry  =  findResourceInternal  (  name  ,  classPath  )  ; 
if  (  entry  ==  null  )  throw   new   ClassNotFoundException  (  name  )  ; 
Class   clazz  =  entry  .  loadedClass  ; 
if  (  clazz  !=  null  )  return   clazz  ; 
synchronized  (  this  )  { 
if  (  entry  .  binaryContent  ==  null  &&  entry  .  loadedClass  ==  null  )  throw   new   ClassNotFoundException  (  name  )  ; 
String   packageName  =  null  ; 
int   pos  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  pos  !=  -  1  )  packageName  =  name  .  substring  (  0  ,  pos  )  ; 
Package   pkg  =  null  ; 
if  (  packageName  !=  null  )  { 
pkg  =  getPackage  (  packageName  )  ; 
if  (  pkg  ==  null  )  { 
try  { 
if  (  entry  .  manifest  ==  null  )  { 
definePackage  (  packageName  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  )  ; 
}  else  { 
definePackage  (  packageName  ,  entry  .  manifest  ,  entry  .  codeBase  )  ; 
} 
}  catch  (  IllegalArgumentException   e  )  { 
} 
pkg  =  getPackage  (  packageName  )  ; 
} 
} 
if  (  securityManager  !=  null  )  { 
if  (  pkg  !=  null  )  { 
boolean   sealCheck  =  true  ; 
if  (  pkg  .  isSealed  (  )  )  { 
sealCheck  =  pkg  .  isSealed  (  entry  .  codeBase  )  ; 
}  else  { 
sealCheck  =  (  entry  .  manifest  ==  null  )  ||  !  isPackageSealed  (  packageName  ,  entry  .  manifest  )  ; 
} 
if  (  !  sealCheck  )  throw   new   SecurityException  (  "Sealing violation loading "  +  name  +  " : Package "  +  packageName  +  " is sealed."  )  ; 
} 
} 
if  (  entry  .  loadedClass  ==  null  )  { 
clazz  =  defineClass  (  name  ,  entry  .  binaryContent  ,  0  ,  entry  .  binaryContent  .  length  ,  new   CodeSource  (  entry  .  codeBase  ,  entry  .  certificates  )  )  ; 
entry  .  loadedClass  =  clazz  ; 
entry  .  binaryContent  =  null  ; 
entry  .  source  =  null  ; 
entry  .  codeBase  =  null  ; 
entry  .  manifest  =  null  ; 
entry  .  certificates  =  null  ; 
}  else  { 
clazz  =  entry  .  loadedClass  ; 
} 
} 
return   clazz  ; 
} 







protected   ResourceEntry   findResourceInternal  (  File   file  ,  String   path  )  { 
ResourceEntry   entry  =  new   ResourceEntry  (  )  ; 
try  { 
entry  .  source  =  getURI  (  new   File  (  file  ,  path  )  )  ; 
entry  .  codeBase  =  getURL  (  new   File  (  file  ,  path  )  ,  false  )  ; 
}  catch  (  MalformedURLException   e  )  { 
return   null  ; 
} 
return   entry  ; 
} 






protected   ResourceEntry   findResourceInternal  (  String   name  ,  String   path  )  { 
if  (  !  started  )  { 
log  .  info  (  sm  .  getString  (  "webappClassLoader.stopped"  ,  name  )  )  ; 
return   null  ; 
} 
if  (  (  name  ==  null  )  ||  (  path  ==  null  )  )  return   null  ; 
ResourceEntry   entry  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
if  (  entry  !=  null  )  return   entry  ; 
int   contentLength  =  -  1  ; 
InputStream   binaryStream  =  null  ; 
int   jarFilesLength  =  jarFiles  .  length  ; 
int   repositoriesLength  =  repositories  .  length  ; 
int   i  ; 
Resource   resource  =  null  ; 
boolean   fileNeedConvert  =  false  ; 
for  (  i  =  0  ;  (  entry  ==  null  )  &&  (  i  <  repositoriesLength  )  ;  i  ++  )  { 
try  { 
String   fullPath  =  repositories  [  i  ]  +  path  ; 
Object   lookupResult  =  resources  .  lookup  (  fullPath  )  ; 
if  (  lookupResult   instanceof   Resource  )  { 
resource  =  (  Resource  )  lookupResult  ; 
} 
if  (  securityManager  !=  null  )  { 
PrivilegedAction   dp  =  new   PrivilegedFindResource  (  files  [  i  ]  ,  path  )  ; 
entry  =  (  ResourceEntry  )  AccessController  .  doPrivileged  (  dp  )  ; 
}  else  { 
entry  =  findResourceInternal  (  files  [  i  ]  ,  path  )  ; 
} 
ResourceAttributes   attributes  =  (  ResourceAttributes  )  resources  .  getAttributes  (  fullPath  )  ; 
contentLength  =  (  int  )  attributes  .  getContentLength  (  )  ; 
entry  .  lastModified  =  attributes  .  getLastModified  (  )  ; 
if  (  resource  !=  null  )  { 
try  { 
binaryStream  =  resource  .  streamContent  (  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
if  (  needConvert  )  { 
if  (  path  .  endsWith  (  ".properties"  )  )  { 
fileNeedConvert  =  true  ; 
} 
} 
synchronized  (  allPermission  )  { 
int   j  ; 
long  [  ]  result2  =  new   long  [  lastModifiedDates  .  length  +  1  ]  ; 
for  (  j  =  0  ;  j  <  lastModifiedDates  .  length  ;  j  ++  )  { 
result2  [  j  ]  =  lastModifiedDates  [  j  ]  ; 
} 
result2  [  lastModifiedDates  .  length  ]  =  entry  .  lastModified  ; 
lastModifiedDates  =  result2  ; 
String  [  ]  result  =  new   String  [  paths  .  length  +  1  ]  ; 
for  (  j  =  0  ;  j  <  paths  .  length  ;  j  ++  )  { 
result  [  j  ]  =  paths  [  j  ]  ; 
} 
result  [  paths  .  length  ]  =  fullPath  ; 
paths  =  result  ; 
} 
} 
}  catch  (  NamingException   e  )  { 
} 
} 
if  (  (  entry  ==  null  )  &&  (  notFoundResources  .  containsKey  (  name  )  )  )  return   null  ; 
JarEntry   jarEntry  =  null  ; 
synchronized  (  jarFiles  )  { 
if  (  !  openJARs  (  )  )  { 
return   null  ; 
} 
for  (  i  =  0  ;  (  entry  ==  null  )  &&  (  i  <  jarFilesLength  )  ;  i  ++  )  { 
jarEntry  =  jarFiles  [  i  ]  .  getJarEntry  (  path  )  ; 
if  (  jarEntry  !=  null  )  { 
entry  =  new   ResourceEntry  (  )  ; 
try  { 
entry  .  codeBase  =  getURL  (  jarRealFiles  [  i  ]  ,  false  )  ; 
String   jarFakeUrl  =  getURI  (  jarRealFiles  [  i  ]  )  .  toString  (  )  ; 
jarFakeUrl  =  "jar:"  +  jarFakeUrl  +  "!/"  +  path  ; 
entry  .  source  =  new   URL  (  jarFakeUrl  )  ; 
entry  .  lastModified  =  jarRealFiles  [  i  ]  .  lastModified  (  )  ; 
}  catch  (  MalformedURLException   e  )  { 
return   null  ; 
} 
contentLength  =  (  int  )  jarEntry  .  getSize  (  )  ; 
try  { 
entry  .  manifest  =  jarFiles  [  i  ]  .  getManifest  (  )  ; 
binaryStream  =  jarFiles  [  i  ]  .  getInputStream  (  jarEntry  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
if  (  antiJARLocking  &&  !  (  path  .  endsWith  (  ".class"  )  )  )  { 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
File   resourceFile  =  new   File  (  loaderDir  ,  jarEntry  .  getName  (  )  )  ; 
if  (  !  resourceFile  .  exists  (  )  )  { 
Enumeration   entries  =  jarFiles  [  i  ]  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
JarEntry   jarEntry2  =  (  JarEntry  )  entries  .  nextElement  (  )  ; 
if  (  !  (  jarEntry2  .  isDirectory  (  )  )  &&  (  !  jarEntry2  .  getName  (  )  .  endsWith  (  ".class"  )  )  )  { 
resourceFile  =  new   File  (  loaderDir  ,  jarEntry2  .  getName  (  )  )  ; 
resourceFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
FileOutputStream   os  =  null  ; 
InputStream   is  =  null  ; 
try  { 
is  =  jarFiles  [  i  ]  .  getInputStream  (  jarEntry2  )  ; 
os  =  new   FileOutputStream  (  resourceFile  )  ; 
while  (  true  )  { 
int   n  =  is  .  read  (  buf  )  ; 
if  (  n  <=  0  )  { 
break  ; 
} 
os  .  write  (  buf  ,  0  ,  n  )  ; 
} 
}  catch  (  IOException   e  )  { 
}  finally  { 
try  { 
if  (  is  !=  null  )  { 
is  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
try  { 
if  (  os  !=  null  )  { 
os  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 
} 
} 
} 
} 
if  (  entry  ==  null  )  { 
synchronized  (  notFoundResources  )  { 
notFoundResources  .  put  (  name  ,  name  )  ; 
} 
return   null  ; 
} 
if  (  binaryStream  !=  null  )  { 
byte  [  ]  binaryContent  =  new   byte  [  contentLength  ]  ; 
int   pos  =  0  ; 
try  { 
while  (  true  )  { 
int   n  =  binaryStream  .  read  (  binaryContent  ,  pos  ,  binaryContent  .  length  -  pos  )  ; 
if  (  n  <=  0  )  break  ; 
pos  +=  n  ; 
} 
binaryStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
if  (  fileNeedConvert  )  { 
String   str  =  new   String  (  binaryContent  ,  0  ,  pos  )  ; 
try  { 
binaryContent  =  str  .  getBytes  (  "UTF-8"  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
entry  .  binaryContent  =  binaryContent  ; 
if  (  jarEntry  !=  null  )  { 
entry  .  certificates  =  jarEntry  .  getCertificates  (  )  ; 
} 
} 
} 
synchronized  (  resourceEntries  )  { 
ResourceEntry   entry2  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
if  (  entry2  ==  null  )  { 
resourceEntries  .  put  (  name  ,  entry  )  ; 
}  else  { 
entry  =  entry2  ; 
} 
} 
return   entry  ; 
} 





protected   boolean   isPackageSealed  (  String   name  ,  Manifest   man  )  { 
String   path  =  name  .  replace  (  '.'  ,  '/'  )  +  '/'  ; 
Attributes   attr  =  man  .  getAttributes  (  path  )  ; 
String   sealed  =  null  ; 
if  (  attr  !=  null  )  { 
sealed  =  attr  .  getValue  (  Name  .  SEALED  )  ; 
} 
if  (  sealed  ==  null  )  { 
if  (  (  attr  =  man  .  getMainAttributes  (  )  )  !=  null  )  { 
sealed  =  attr  .  getValue  (  Name  .  SEALED  )  ; 
} 
} 
return  "true"  .  equalsIgnoreCase  (  sealed  )  ; 
} 









protected   InputStream   findLoadedResource  (  String   name  )  { 
ResourceEntry   entry  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
if  (  entry  !=  null  )  { 
if  (  entry  .  binaryContent  !=  null  )  return   new   ByteArrayInputStream  (  entry  .  binaryContent  )  ; 
} 
return  (  null  )  ; 
} 








protected   Class   findLoadedClass0  (  String   name  )  { 
ResourceEntry   entry  =  (  ResourceEntry  )  resourceEntries  .  get  (  name  )  ; 
if  (  entry  !=  null  )  { 
return   entry  .  loadedClass  ; 
} 
return  (  null  )  ; 
} 




protected   void   refreshPolicy  (  )  { 
try  { 
Policy   policy  =  Policy  .  getPolicy  (  )  ; 
policy  .  refresh  (  )  ; 
}  catch  (  AccessControlException   e  )  { 
} 
} 







protected   boolean   filter  (  String   name  )  { 
if  (  name  ==  null  )  return   false  ; 
String   packageName  =  null  ; 
int   pos  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  pos  !=  -  1  )  packageName  =  name  .  substring  (  0  ,  pos  )  ;  else   return   false  ; 
for  (  int   i  =  0  ;  i  <  packageTriggers  .  length  ;  i  ++  )  { 
if  (  packageName  .  startsWith  (  packageTriggers  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 











protected   boolean   validate  (  String   name  )  { 
if  (  name  ==  null  )  return   false  ; 
if  (  name  .  startsWith  (  "java."  )  )  return   false  ; 
return   true  ; 
} 









protected   boolean   validateJarFile  (  File   jarfile  )  throws   IOException  { 
if  (  triggers  ==  null  )  return  (  true  )  ; 
JarFile   jarFile  =  new   JarFile  (  jarfile  )  ; 
for  (  int   i  =  0  ;  i  <  triggers  .  length  ;  i  ++  )  { 
Class   clazz  =  null  ; 
try  { 
if  (  parent  !=  null  )  { 
clazz  =  parent  .  loadClass  (  triggers  [  i  ]  )  ; 
}  else  { 
clazz  =  Class  .  forName  (  triggers  [  i  ]  )  ; 
} 
}  catch  (  Throwable   t  )  { 
clazz  =  null  ; 
} 
if  (  clazz  ==  null  )  continue  ; 
String   name  =  triggers  [  i  ]  .  replace  (  '.'  ,  '/'  )  +  ".class"  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  " Checking for "  +  name  )  ; 
JarEntry   jarEntry  =  jarFile  .  getJarEntry  (  name  )  ; 
if  (  jarEntry  !=  null  )  { 
log  .  info  (  "validateJarFile("  +  jarfile  +  ") - jar not loaded. See Servlet Spec 2.3, "  +  "section 9.7.2. Offending class: "  +  name  )  ; 
jarFile  .  close  (  )  ; 
return  (  false  )  ; 
} 
} 
jarFile  .  close  (  )  ; 
return  (  true  )  ; 
} 




protected   URL   getURL  (  File   file  ,  boolean   encoded  )  throws   MalformedURLException  { 
File   realFile  =  file  ; 
try  { 
realFile  =  realFile  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
if  (  encoded  )  { 
return   getURI  (  realFile  )  ; 
}  else  { 
return   realFile  .  toURL  (  )  ; 
} 
} 




protected   URL   getURI  (  File   file  )  throws   MalformedURLException  { 
File   realFile  =  file  ; 
try  { 
realFile  =  realFile  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   realFile  .  toURI  (  )  .  toURL  (  )  ; 
} 







protected   static   void   deleteDir  (  File   dir  )  { 
String   files  [  ]  =  dir  .  list  (  )  ; 
if  (  files  ==  null  )  { 
files  =  new   String  [  0  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  new   File  (  dir  ,  files  [  i  ]  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
deleteDir  (  file  )  ; 
}  else  { 
try  { 
file  .  delete  (  )  ; 
}  catch  (  SecurityException   se  )  { 
log  .  error  (  "The file "  +  file  .  getAbsolutePath  (  )  +  " couldn't be deleted"  )  ; 
} 
} 
} 
try  { 
dir  .  delete  (  )  ; 
}  catch  (  SecurityException   se  )  { 
log  .  error  (  "The directory "  +  dir  .  getAbsolutePath  (  )  +  " couldn't be deleted"  )  ; 
} 
} 

@  Override 
public   void   init  (  )  throws   LifecycleException  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

@  Override 
public   void   destroy  (  )  throws   LifecycleException  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

@  Override 
public   LifecycleState   getState  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

@  Override 
public   String   getStateName  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

