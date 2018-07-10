package   org  .  mortbay  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  security  .  Permission  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  mortbay  .  log  .  LogFactory  ; 







public   class   URLResource   extends   Resource  { 

private   static   Log   log  =  LogFactory  .  getLog  (  URLResource  .  class  )  ; 

protected   URL   _url  ; 

protected   String   _urlString  ; 

protected   transient   URLConnection   _connection  ; 

protected   transient   InputStream   _in  =  null  ; 

protected   URLResource  (  URL   url  ,  URLConnection   connection  )  { 
_url  =  url  ; 
_urlString  =  _url  .  toString  (  )  ; 
_connection  =  connection  ; 
} 

protected   synchronized   boolean   checkConnection  (  )  { 
if  (  _connection  ==  null  )  { 
try  { 
_connection  =  _url  .  openConnection  (  )  ; 
}  catch  (  IOException   e  )  { 
LogSupport  .  ignore  (  log  ,  e  )  ; 
} 
} 
return   _connection  !=  null  ; 
} 



public   synchronized   void   release  (  )  { 
if  (  _in  !=  null  )  { 
try  { 
_in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
LogSupport  .  ignore  (  log  ,  e  )  ; 
} 
_in  =  null  ; 
} 
if  (  _connection  !=  null  )  _connection  =  null  ; 
} 




public   boolean   exists  (  )  { 
try  { 
synchronized  (  this  )  { 
if  (  checkConnection  (  )  &&  _in  ==  null  )  _in  =  _connection  .  getInputStream  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
LogSupport  .  ignore  (  log  ,  e  )  ; 
} 
return   _in  !=  null  ; 
} 






public   boolean   isDirectory  (  )  { 
return   exists  (  )  &&  _url  .  toString  (  )  .  endsWith  (  "/"  )  ; 
} 




public   long   lastModified  (  )  { 
if  (  checkConnection  (  )  )  return   _connection  .  getLastModified  (  )  ; 
return  -  1  ; 
} 




public   long   length  (  )  { 
if  (  checkConnection  (  )  )  return   _connection  .  getContentLength  (  )  ; 
return  -  1  ; 
} 




public   URL   getURL  (  )  { 
return   _url  ; 
} 





public   File   getFile  (  )  throws   IOException  { 
if  (  checkConnection  (  )  )  { 
Permission   perm  =  _connection  .  getPermission  (  )  ; 
if  (  perm   instanceof   java  .  io  .  FilePermission  )  return   new   File  (  perm  .  getName  (  )  )  ; 
} 
try  { 
return   new   File  (  _url  .  getFile  (  )  )  ; 
}  catch  (  Exception   e  )  { 
LogSupport  .  ignore  (  log  ,  e  )  ; 
} 
return   null  ; 
} 




public   String   getName  (  )  { 
return   _url  .  toExternalForm  (  )  ; 
} 




public   synchronized   InputStream   getInputStream  (  )  throws   java  .  io  .  IOException  { 
if  (  !  checkConnection  (  )  )  throw   new   IOException  (  "Invalid resource"  )  ; 
try  { 
if  (  _in  !=  null  )  { 
InputStream   in  =  _in  ; 
_in  =  null  ; 
return   in  ; 
} 
return   _connection  .  getInputStream  (  )  ; 
}  finally  { 
_connection  =  null  ; 
} 
} 




public   OutputStream   getOutputStream  (  )  throws   java  .  io  .  IOException  ,  SecurityException  { 
throw   new   IOException  (  "Output not supported"  )  ; 
} 




public   boolean   delete  (  )  throws   SecurityException  { 
throw   new   SecurityException  (  "Delete not supported"  )  ; 
} 




public   boolean   renameTo  (  Resource   dest  )  throws   SecurityException  { 
throw   new   SecurityException  (  "RenameTo not supported"  )  ; 
} 




public   String  [  ]  list  (  )  { 
return   null  ; 
} 





public   Resource   addPath  (  String   path  )  throws   IOException  ,  MalformedURLException  { 
if  (  path  ==  null  )  return   null  ; 
path  =  URI  .  canonicalPath  (  path  )  ; 
return   newResource  (  URI  .  addPaths  (  _url  .  toExternalForm  (  )  ,  path  )  )  ; 
} 

public   String   toString  (  )  { 
return   _urlString  ; 
} 

public   int   hashCode  (  )  { 
return   _url  .  hashCode  (  )  ; 
} 

public   boolean   equals  (  Object   o  )  { 
return   o   instanceof   URLResource  &&  _url  .  equals  (  (  (  URLResource  )  o  )  .  _url  )  ; 
} 
} 

