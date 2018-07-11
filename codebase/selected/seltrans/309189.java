package   java  .  net  ; 

import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FilePermission  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  URLStreamHandlerFactory  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  NoSuchElementException  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  jar  .  Manifest  ; 
import   java  .  util  .  jar  .  Attributes  ; 
import   java  .  util  .  jar  .  Attributes  .  Name  ; 
import   java  .  security  .  PrivilegedAction  ; 
import   java  .  security  .  PrivilegedExceptionAction  ; 
import   java  .  security  .  AccessController  ; 
import   java  .  security  .  AccessControlContext  ; 
import   java  .  security  .  SecureClassLoader  ; 
import   java  .  security  .  CodeSource  ; 
import   java  .  security  .  Permission  ; 
import   java  .  security  .  PermissionCollection  ; 
import   sun  .  misc  .  Resource  ; 
import   sun  .  misc  .  URLClassPath  ; 
import   sun  .  net  .  www  .  ParseUtil  ; 
import   sun  .  security  .  util  .  SecurityConstants  ; 


















public   class   URLClassLoader   extends   SecureClassLoader  { 

private   URLClassPath   ucp  ; 

private   AccessControlContext   acc  ; 



















public   URLClassLoader  (  URL  [  ]  urls  ,  ClassLoader   parent  )  { 
super  (  parent  )  ; 
SecurityManager   security  =  System  .  getSecurityManager  (  )  ; 
if  (  security  !=  null  )  { 
security  .  checkCreateClassLoader  (  )  ; 
} 
ucp  =  new   URLClassPath  (  urls  )  ; 
acc  =  AccessController  .  getContext  (  )  ; 
} 





















public   URLClassLoader  (  URL  [  ]  urls  )  { 
super  (  )  ; 
SecurityManager   security  =  System  .  getSecurityManager  (  )  ; 
if  (  security  !=  null  )  { 
security  .  checkCreateClassLoader  (  )  ; 
} 
ucp  =  new   URLClassPath  (  urls  )  ; 
acc  =  AccessController  .  getContext  (  )  ; 
} 





















public   URLClassLoader  (  URL  [  ]  urls  ,  ClassLoader   parent  ,  URLStreamHandlerFactory   factory  )  { 
super  (  parent  )  ; 
SecurityManager   security  =  System  .  getSecurityManager  (  )  ; 
if  (  security  !=  null  )  { 
security  .  checkCreateClassLoader  (  )  ; 
} 
ucp  =  new   URLClassPath  (  urls  ,  factory  )  ; 
acc  =  AccessController  .  getContext  (  )  ; 
} 







protected   void   addURL  (  URL   url  )  { 
ucp  .  addURL  (  url  )  ; 
} 







public   URL  [  ]  getURLs  (  )  { 
return   ucp  .  getURLs  (  )  ; 
} 










protected   Class   findClass  (  final   String   name  )  throws   ClassNotFoundException  { 
Class   retValue  =  null  ; 
try  { 
retValue  =  (  Class  )  AccessController  .  doPrivileged  (  new   PrivilegedExceptionAction  (  )  { 

public   Object   run  (  )  throws   ClassNotFoundException  { 
String   path  =  name  .  replace  (  '.'  ,  '/'  )  .  concat  (  ".class"  )  ; 
Resource   res  =  ucp  .  getResource  (  path  ,  false  )  ; 
if  (  res  !=  null  )  { 
try  { 
return   defineClass  (  name  ,  res  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ClassNotFoundException  (  name  ,  e  )  ; 
} 
} 
return   null  ; 
} 
}  ,  acc  )  ; 
}  catch  (  java  .  security  .  PrivilegedActionException   pae  )  { 
throw  (  ClassNotFoundException  )  pae  .  getException  (  )  ; 
} 
if  (  retValue  ==  null  )  { 
throw   new   ClassNotFoundException  (  name  )  ; 
} 
return   retValue  ; 
} 

private   Class   defineClass  (  String   name  ,  Resource   res  )  throws   IOException  { 
int   i  =  name  .  lastIndexOf  (  '.'  )  ; 
URL   url  =  res  .  getCodeSourceURL  (  )  ; 
if  (  i  !=  -  1  )  { 
String   pkgname  =  name  .  substring  (  0  ,  i  )  ; 
Package   pkg  =  getPackage  (  pkgname  )  ; 
Manifest   man  =  res  .  getManifest  (  )  ; 
if  (  pkg  !=  null  )  { 
if  (  pkg  .  isSealed  (  )  )  { 
if  (  !  pkg  .  isSealed  (  url  )  )  { 
throw   new   SecurityException  (  "sealing violation: package "  +  pkgname  +  " is sealed"  )  ; 
} 
}  else  { 
if  (  (  man  !=  null  )  &&  isSealed  (  pkgname  ,  man  )  )  { 
throw   new   SecurityException  (  "sealing violation: can't seal package "  +  pkgname  +  ": already loaded"  )  ; 
} 
} 
}  else  { 
if  (  man  !=  null  )  { 
definePackage  (  pkgname  ,  man  ,  url  )  ; 
}  else  { 
definePackage  (  pkgname  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  )  ; 
} 
} 
} 
byte  [  ]  b  =  res  .  getBytes  (  )  ; 
java  .  security  .  cert  .  Certificate  [  ]  certs  =  res  .  getCertificates  (  )  ; 
CodeSource   cs  =  new   CodeSource  (  url  ,  certs  )  ; 
return   defineClass  (  name  ,  b  ,  0  ,  b  .  length  ,  cs  )  ; 
} 
















protected   Package   definePackage  (  String   name  ,  Manifest   man  ,  URL   url  )  throws   IllegalArgumentException  { 
String   path  =  name  .  replace  (  '.'  ,  '/'  )  .  concat  (  "/"  )  ; 
String   specTitle  =  null  ,  specVersion  =  null  ,  specVendor  =  null  ; 
String   implTitle  =  null  ,  implVersion  =  null  ,  implVendor  =  null  ; 
String   sealed  =  null  ; 
URL   sealBase  =  null  ; 
Attributes   attr  =  man  .  getAttributes  (  path  )  ; 
if  (  attr  !=  null  )  { 
specTitle  =  attr  .  getValue  (  Name  .  SPECIFICATION_TITLE  )  ; 
specVersion  =  attr  .  getValue  (  Name  .  SPECIFICATION_VERSION  )  ; 
specVendor  =  attr  .  getValue  (  Name  .  SPECIFICATION_VENDOR  )  ; 
implTitle  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_TITLE  )  ; 
implVersion  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_VERSION  )  ; 
implVendor  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_VENDOR  )  ; 
sealed  =  attr  .  getValue  (  Name  .  SEALED  )  ; 
} 
attr  =  man  .  getMainAttributes  (  )  ; 
if  (  attr  !=  null  )  { 
if  (  specTitle  ==  null  )  { 
specTitle  =  attr  .  getValue  (  Name  .  SPECIFICATION_TITLE  )  ; 
} 
if  (  specVersion  ==  null  )  { 
specVersion  =  attr  .  getValue  (  Name  .  SPECIFICATION_VERSION  )  ; 
} 
if  (  specVendor  ==  null  )  { 
specVendor  =  attr  .  getValue  (  Name  .  SPECIFICATION_VENDOR  )  ; 
} 
if  (  implTitle  ==  null  )  { 
implTitle  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_TITLE  )  ; 
} 
if  (  implVersion  ==  null  )  { 
implVersion  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_VERSION  )  ; 
} 
if  (  implVendor  ==  null  )  { 
implVendor  =  attr  .  getValue  (  Name  .  IMPLEMENTATION_VENDOR  )  ; 
} 
if  (  sealed  ==  null  )  { 
sealed  =  attr  .  getValue  (  Name  .  SEALED  )  ; 
} 
} 
if  (  "true"  .  equalsIgnoreCase  (  sealed  )  )  { 
sealBase  =  url  ; 
} 
return   definePackage  (  name  ,  specTitle  ,  specVersion  ,  specVendor  ,  implTitle  ,  implVersion  ,  implVendor  ,  sealBase  )  ; 
} 

private   boolean   isSealed  (  String   name  ,  Manifest   man  )  { 
String   path  =  name  .  replace  (  '.'  ,  '/'  )  .  concat  (  "/"  )  ; 
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








public   URL   findResource  (  final   String   name  )  { 
URL   url  =  (  URL  )  AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   ucp  .  findResource  (  name  ,  true  )  ; 
} 
}  ,  acc  )  ; 
return   url  !=  null  ?  ucp  .  checkURL  (  url  )  :  null  ; 
} 









public   Enumeration   findResources  (  final   String   name  )  throws   IOException  { 
final   Enumeration   e  =  ucp  .  findResources  (  name  ,  true  )  ; 
return   new   Enumeration  (  )  { 

private   URL   url  =  null  ; 

private   boolean   next  (  )  { 
if  (  url  !=  null  )  { 
return   true  ; 
} 
do  { 
URL   u  =  (  URL  )  AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
if  (  !  e  .  hasMoreElements  (  )  )  return   null  ; 
return   e  .  nextElement  (  )  ; 
} 
}  ,  acc  )  ; 
if  (  u  ==  null  )  break  ; 
url  =  ucp  .  checkURL  (  u  )  ; 
}  while  (  url  ==  null  )  ; 
return   url  !=  null  ; 
} 

public   Object   nextElement  (  )  { 
if  (  !  next  (  )  )  { 
throw   new   NoSuchElementException  (  )  ; 
} 
URL   u  =  url  ; 
url  =  null  ; 
return   u  ; 
} 

public   boolean   hasMoreElements  (  )  { 
return   next  (  )  ; 
} 
}  ; 
} 


















protected   PermissionCollection   getPermissions  (  CodeSource   codesource  )  { 
PermissionCollection   perms  =  super  .  getPermissions  (  codesource  )  ; 
URL   url  =  codesource  .  getLocation  (  )  ; 
Permission   p  ; 
URLConnection   urlConnection  ; 
try  { 
urlConnection  =  url  .  openConnection  (  )  ; 
p  =  urlConnection  .  getPermission  (  )  ; 
}  catch  (  java  .  io  .  IOException   ioe  )  { 
p  =  null  ; 
urlConnection  =  null  ; 
} 
if  (  p   instanceof   FilePermission  )  { 
String   path  =  p  .  getName  (  )  ; 
if  (  path  .  endsWith  (  File  .  separator  )  )  { 
path  +=  "-"  ; 
p  =  new   FilePermission  (  path  ,  SecurityConstants  .  FILE_READ_ACTION  )  ; 
} 
}  else   if  (  (  p  ==  null  )  &&  (  url  .  getProtocol  (  )  .  equals  (  "file"  )  )  )  { 
String   path  =  url  .  getFile  (  )  .  replace  (  '/'  ,  File  .  separatorChar  )  ; 
path  =  ParseUtil  .  decode  (  path  )  ; 
if  (  path  .  endsWith  (  File  .  separator  )  )  path  +=  "-"  ; 
p  =  new   FilePermission  (  path  ,  SecurityConstants  .  FILE_READ_ACTION  )  ; 
}  else  { 
URL   locUrl  =  url  ; 
if  (  urlConnection   instanceof   JarURLConnection  )  { 
locUrl  =  (  (  JarURLConnection  )  urlConnection  )  .  getJarFileURL  (  )  ; 
} 
String   host  =  locUrl  .  getHost  (  )  ; 
if  (  host  !=  null  &&  (  host  .  length  (  )  >  0  )  )  p  =  new   SocketPermission  (  host  ,  SecurityConstants  .  SOCKET_CONNECT_ACCEPT_ACTION  )  ; 
} 
if  (  p  !=  null  )  { 
final   SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
final   Permission   fp  =  p  ; 
AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  throws   SecurityException  { 
sm  .  checkPermission  (  fp  )  ; 
return   null  ; 
} 
}  ,  acc  )  ; 
} 
perms  .  add  (  p  )  ; 
} 
return   perms  ; 
} 













public   static   URLClassLoader   newInstance  (  final   URL  [  ]  urls  ,  final   ClassLoader   parent  )  { 
AccessControlContext   acc  =  AccessController  .  getContext  (  )  ; 
URLClassLoader   ucl  =  (  URLClassLoader  )  AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   new   FactoryURLClassLoader  (  urls  ,  parent  )  ; 
} 
}  )  ; 
ucl  .  acc  =  acc  ; 
return   ucl  ; 
} 












public   static   URLClassLoader   newInstance  (  final   URL  [  ]  urls  )  { 
AccessControlContext   acc  =  AccessController  .  getContext  (  )  ; 
URLClassLoader   ucl  =  (  URLClassLoader  )  AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   new   FactoryURLClassLoader  (  urls  )  ; 
} 
}  )  ; 
ucl  .  acc  =  acc  ; 
return   ucl  ; 
} 
} 

final   class   FactoryURLClassLoader   extends   URLClassLoader  { 

FactoryURLClassLoader  (  URL  [  ]  urls  ,  ClassLoader   parent  )  { 
super  (  urls  ,  parent  )  ; 
} 

FactoryURLClassLoader  (  URL  [  ]  urls  )  { 
super  (  urls  )  ; 
} 

public   final   synchronized   Class   loadClass  (  String   name  ,  boolean   resolve  )  throws   ClassNotFoundException  { 
SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
String   cname  =  name  .  replace  (  '/'  ,  '.'  )  ; 
if  (  cname  .  startsWith  (  "["  )  )  { 
int   b  =  cname  .  lastIndexOf  (  '['  )  +  2  ; 
if  (  b  >  1  &&  b  <  cname  .  length  (  )  )  { 
cname  =  cname  .  substring  (  b  )  ; 
} 
} 
int   i  =  cname  .  lastIndexOf  (  '.'  )  ; 
if  (  i  !=  -  1  )  { 
sm  .  checkPackageAccess  (  name  .  substring  (  0  ,  i  )  )  ; 
} 
} 
return   super  .  loadClass  (  name  ,  resolve  )  ; 
} 
} 

