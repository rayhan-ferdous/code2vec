package   net  .  sourceforge  .  pebble  .  util  ; 

import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  FileNameMap  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  Properties  ; 






public   final   class   FileUtils  { 


private   static   final   Log   log  =  LogFactory  .  getLog  (  FileUtils  .  class  )  ; 


private   static   Properties   localFileNameMap  ; 

static  { 
try  { 
localFileNameMap  =  new   Properties  (  )  ; 
InputStream   in  =  FileUtils  .  class  .  getClassLoader  (  )  .  getResourceAsStream  (  "content-types.properties"  )  ; 
if  (  in  !=  null  )  { 
localFileNameMap  .  load  (  in  )  ; 
in  .  close  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "Could not load content types."  ,  ioe  )  ; 
} 
} 










public   static   boolean   underneathRoot  (  File   root  ,  File   file  )  { 
try  { 
root  =  root  .  getCanonicalFile  (  )  ; 
file  =  file  .  getCanonicalFile  (  )  ; 
while  (  file  !=  null  )  { 
if  (  file  .  equals  (  root  )  )  { 
return   true  ; 
}  else  { 
file  =  file  .  getParentFile  (  )  ; 
} 
} 
}  catch  (  IOException   ioe  )  { 
return   false  ; 
} 
return   false  ; 
} 







public   static   void   deleteFile  (  File   directory  )  { 
File   files  [  ]  =  directory  .  listFiles  (  )  ; 
if  (  files  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  { 
deleteFile  (  files  [  i  ]  )  ; 
}  else  { 
files  [  i  ]  .  delete  (  )  ; 
} 
} 
} 
directory  .  delete  (  )  ; 
} 







public   static   void   copyFile  (  File   source  ,  File   destination  )  throws   IOException  { 
FileChannel   srcChannel  =  new   FileInputStream  (  source  )  .  getChannel  (  )  ; 
FileChannel   dstChannel  =  new   FileOutputStream  (  destination  )  .  getChannel  (  )  ; 
dstChannel  .  transferFrom  (  srcChannel  ,  0  ,  srcChannel  .  size  (  )  )  ; 
srcChannel  .  close  (  )  ; 
dstChannel  .  close  (  )  ; 
} 







public   static   String   getContentType  (  String   name  )  { 
String   contentType  ; 
FileNameMap   fileNameMap  =  URLConnection  .  getFileNameMap  (  )  ; 
contentType  =  fileNameMap  .  getContentTypeFor  (  name  )  ; 
if  (  contentType  ==  null  )  { 
int   index  =  name  .  lastIndexOf  (  "."  )  ; 
if  (  index  >  -  1  )  { 
contentType  =  localFileNameMap  .  getProperty  (  name  .  substring  (  index  )  )  ; 
} 
} 
return   contentType  ; 
} 
} 

