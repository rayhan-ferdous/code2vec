package   org  .  jpublish  .  repository  .  web  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  File  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  util  .  Iterator  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   com  .  anthonyeden  .  lib  .  util  .  IOUtilities  ; 
import   com  .  anthonyeden  .  lib  .  config  .  Configuration  ; 
import   org  .  jpublish  .  JPublishContext  ; 
import   org  .  jpublish  .  view  .  ViewRenderer  ; 
import   org  .  jpublish  .  util  .  PathUtilities  ; 
import   org  .  jpublish  .  util  .  vfs  .  VFSFile  ; 
import   org  .  jpublish  .  repository  .  AbstractRepository  ; 


public   class   WebRepository   extends   AbstractRepository  { 

private   static   final   Log   log  =  LogFactory  .  getLog  (  WebRepository  .  class  )  ; 








public   String   get  (  String   path  )  throws   Exception  { 
InputStream   in  =  null  ; 
StringWriter   out  =  null  ; 
try  { 
URL   url  =  new   URL  (  path  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
in  =  conn  .  getInputStream  (  )  ; 
out  =  new   StringWriter  (  )  ; 
int   c  =  -  1  ; 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  { 
out  .  write  (  (  char  )  c  )  ; 
} 
return   out  .  toString  (  )  ; 
}  finally  { 
IOUtilities  .  close  (  in  )  ; 
IOUtilities  .  close  (  out  )  ; 
} 
} 









public   String   get  (  String   path  ,  JPublishContext   context  )  throws   Exception  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Getting dynamic content element for path "  +  path  )  ; 
StringWriter   writer  =  null  ; 
StringReader   reader  =  null  ; 
try  { 
writer  =  new   StringWriter  (  )  ; 
reader  =  new   StringReader  (  get  (  path  )  )  ; 
String   name  =  PathUtilities  .  makeRepositoryURI  (  getName  (  )  ,  path  )  ; 
ViewRenderer   renderer  =  siteContext  .  getViewRenderer  (  )  ; 
renderer  .  render  (  context  ,  name  ,  reader  ,  writer  )  ; 
return   writer  .  toString  (  )  ; 
}  finally  { 
IOUtilities  .  close  (  writer  )  ; 
IOUtilities  .  close  (  reader  )  ; 
} 
} 





public   void   remove  (  String   path  )  throws   Exception  { 
throw   new   UnsupportedOperationException  (  "Cannot remove web content"  )  ; 
} 






public   void   makeDirectory  (  String   path  )  { 
throw   new   UnsupportedOperationException  (  "Make directory not supported"  )  ; 
} 







public   void   removeDirectory  (  String   path  )  throws   Exception  { 
throw   new   UnsupportedOperationException  (  "Remove directory not supported"  )  ; 
} 







public   long   getLastModified  (  String   path  )  throws   Exception  { 
URL   url  =  new   URL  (  path  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
return   conn  .  getLastModified  (  )  ; 
} 






public   Iterator   getPaths  (  )  throws   Exception  { 
throw   new   UnsupportedOperationException  (  "getPaths() not supported"  )  ; 
} 








public   Iterator   getPaths  (  String   path  )  throws   Exception  { 
throw   new   UnsupportedOperationException  (  "getPaths() not supported"  )  ; 
} 








public   VFSFile   getVFSRoot  (  )  throws   Exception  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

public   File   pathToFile  (  String   path  )  { 
return   null  ; 
} 







public   void   loadConfiguration  (  Configuration   configuration  )  throws   Exception  { 
this  .  name  =  configuration  .  getAttribute  (  "name"  )  ; 
} 
} 

