package   be  .  ac  .  fundp  .  infonet  .  econf  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 






public   class   Utilities  { 




private   static   org  .  apache  .  log4j  .  Category   m_logCat  =  org  .  apache  .  log4j  .  Category  .  getInstance  (  Utilities  .  class  .  getName  (  )  )  ; 






public   static   String   convertPathname  (  String   filename  )  { 
if  (  filename  ==  null  )  return   null  ; 
String   sep  =  File  .  separator  ; 
if  (  sep  .  equals  (  "/"  )  )  return   filename  .  replace  (  '\\'  ,  '/'  )  ; 
return   filename  .  replace  (  '/'  ,  '\\'  )  ; 
} 







public   static   File   getFile  (  byte  [  ]  content  ,  String   path  )  throws   IOException  { 
File   f  =  new   File  (  path  )  ; 
if  (  f  .  exists  (  )  )  return   f  ;  else  { 
f  .  createNewFile  (  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  f  )  ; 
out  .  write  (  content  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
return   f  ; 
} 
} 





public   static   String   checkDir  (  String   dir  )  { 
dir  =  convertPathname  (  dir  )  ; 
if  (  !  dir  .  endsWith  (  File  .  separator  )  )  dir  =  dir  +  File  .  separator  ; 
return   dir  ; 
} 






public   static   boolean   createDirectory  (  String   path  )  { 
if  (  path  ==  null  )  return   false  ; 
try  { 
path  =  checkDir  (  path  )  ; 
File   f  =  new   File  (  path  )  ; 
if  (  f  .  isDirectory  (  )  )  return   true  ;  else  { 
f  .  mkdirs  (  )  ; 
m_logCat  .  info  (  "eConf created "  +  f  )  ; 
} 
return   true  ; 
}  catch  (  Exception   e  )  { 
m_logCat  .  error  (  "Error while creating directory for: "  +  path  ,  e  )  ; 
return   false  ; 
} 
} 






public   static   String   getWebSite  (  URL   url  )  { 
String   anURL  =  url  .  toString  (  )  ; 
if  (  anURL  .  startsWith  (  "http://"  )  )  anURL  =  anURL  .  substring  (  7  ,  anURL  .  length  (  )  )  ; 
int   i  =  anURL  .  indexOf  (  "/"  )  ; 
return  (  anURL  .  substring  (  0  ,  i  )  )  ; 
} 






public   static   URL   rebuildURL  (  String   relativePath  ,  URL   originURL  )  { 
URL   u  =  null  ; 
if  (  (  relativePath  ==  null  )  ||  (  originURL  ==  null  )  )  return   null  ; 
try  { 
u  =  new   URL  (  relativePath  )  ; 
return   u  ; 
}  catch  (  MalformedURLException   ue  )  { 
} 
if  (  relativePath  .  startsWith  (  "/"  )  )  { 
try  { 
u  =  new   URL  (  "http"  ,  originURL  .  getAuthority  (  )  ,  relativePath  )  ; 
return   u  ; 
}  catch  (  MalformedURLException   ue2  )  { 
m_logCat  .  warn  (  "Trying to construct : "  +  relativePath  +  " from: "  +  originURL  )  ; 
} 
}  else  { 
String   tmp  =  originURL  .  toString  (  )  ; 
int   i  =  tmp  .  lastIndexOf  (  "/"  )  ; 
if  (  i  !=  -  1  )  { 
tmp  =  tmp  .  substring  (  0  ,  i  +  1  )  ; 
tmp  =  tmp  +  relativePath  ; 
try  { 
u  =  new   URL  (  tmp  )  ; 
return   u  ; 
}  catch  (  MalformedURLException   ue3  )  { 
m_logCat  .  warn  (  "Trying to construct : "  +  relativePath  +  " from: "  +  originURL  )  ; 
} 
} 
} 
return   null  ; 
} 






public   static   boolean   isFullURL  (  String   uri  )  { 
try  { 
URL   u  =  new   URL  (  uri  )  ; 
return   true  ; 
}  catch  (  MalformedURLException   e  )  { 
if  (  uri  .  indexOf  (  "www."  )  !=  -  1  )  return   true  ; 
} 
return   false  ; 
} 






public   static   String   getFileName  (  URL   u  )  { 
try  { 
String   anURL  =  u  .  toString  (  )  ; 
int   i  =  anURL  .  lastIndexOf  (  "/"  )  ; 
return  (  anURL  .  substring  (  i  +  1  )  )  ; 
}  catch  (  Exception   e  )  { 
return  (  ""  )  ; 
} 
} 





public   static   String   replace  (  String   s  ,  String   oldToken  ,  String   newToken  ,  boolean   fAll  )  { 
if  (  (  s  ==  null  )  ||  (  oldToken  ==  null  )  ||  (  newToken  ==  null  )  )  { 
throw   new   IllegalArgumentException  (  "Null argument(s) seen"  )  ; 
} 
int   oldTokenLen  =  oldToken  .  length  (  )  ; 
StringBuffer   sb  =  null  ; 
int   oldPos  =  0  ; 
int   pos  =  s  .  indexOf  (  oldToken  ,  oldPos  )  ; 
if  (  oldPos  >  -  1  )  { 
sb  =  new   StringBuffer  (  s  .  length  (  )  )  ; 
} 
for  (  ;  pos  >  -  1  ;  pos  =  s  .  indexOf  (  oldToken  ,  oldPos  )  )  { 
sb  .  append  (  s  .  substring  (  oldPos  ,  pos  )  )  ; 
sb  .  append  (  newToken  )  ; 
oldPos  =  pos  +  oldTokenLen  ; 
if  (  !  fAll  )  { 
break  ; 
} 
} 
return  (  (  oldPos  >  0  )  ?  sb  .  append  (  s  .  substring  (  oldPos  )  )  .  toString  (  )  :  s  )  ; 
} 









public   static   boolean   copyFile  (  File   src  ,  File   des  )  { 
try  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   FileInputStream  (  src  )  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  des  )  )  ; 
int   b  ; 
while  (  (  b  =  in  .  read  (  )  )  !=  -  1  )  out  .  write  (  b  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
return   true  ; 
}  catch  (  IOException   ie  )  { 
m_logCat  .  error  (  "Copy file + "  +  src  +  " to "  +  des  +  " failed!"  ,  ie  )  ; 
return   false  ; 
} 
} 







public   static   Class   initClass  (  String   className  )  { 
if  (  className  ==  null  )  return   null  ; 
try  { 
return   Class  .  forName  (  className  )  ; 
}  catch  (  ClassNotFoundException   nfe  )  { 
m_logCat  .  error  (  "The specified class: "  +  className  +  " has not been found"  ,  nfe  )  ; 
} 
return   null  ; 
} 





public   static   Object   instantiateClass  (  Class   c  )  { 
if  (  c  ==  null  )  return   null  ; 
try  { 
return   c  .  newInstance  (  )  ; 
}  catch  (  InstantiationException   ie  )  { 
m_logCat  .  error  (  "Cannot instanciate class: "  +  c  ,  ie  )  ; 
}  catch  (  IllegalAccessException   iae  )  { 
m_logCat  .  error  (  "Cannot access class: "  +  c  ,  iae  )  ; 
} 
return   null  ; 
} 











public   static   String   appendName  (  String   s  ,  String   app  )  { 
if  (  s  ==  null  )  return   null  ; 
if  (  app  ==  null  )  app  =  ""  ; 
int   i  =  s  .  lastIndexOf  (  "."  )  ; 
if  (  i  !=  -  1  )  { 
String   first  =  s  .  substring  (  0  ,  i  )  ; 
String   second  =  s  .  substring  (  i  ,  s  .  length  (  )  )  ; 
return  (  first  +  app  +  second  )  ; 
}  else   return  (  s  +  app  )  ; 
} 




public   static   String   getFileFromURL  (  String   anURL  )  { 
try  { 
int   i  =  anURL  .  lastIndexOf  (  "/"  )  ; 
return  (  anURL  .  substring  (  i  +  1  )  )  ; 
}  catch  (  Exception   e  )  { 
return  (  ""  )  ; 
} 
} 







public   static   String   convertPropDir  (  String   dir  )  { 
if  (  File  .  separator  .  equals  (  "/"  )  )  return   dir  ; 
String   res  =  ""  ; 
int   i  =  dir  .  indexOf  (  "\\"  )  ; 
while  (  (  dir  !=  ""  )  ||  (  dir  ==  null  )  )  { 
if  (  i  ==  -  1  )  return   res  ;  else  { 
dir  =  dir  .  substring  (  0  ,  i  )  ; 
res  +=  dir  +  "\\\\"  ; 
} 
} 
return   res  ; 
} 
} 

