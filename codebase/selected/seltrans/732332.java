package   br  .  com  .  sysmap  .  crux  .  scannotation  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  JarURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  jar  .  JarFile  ; 







public   class   URLStreamManager  { 

private   final   URL   url  ; 

private   InputStream   inputStream  ; 

private   URLConnection   con  =  null  ; 

private   JarFile   jarFile  =  null  ; 





public   URLStreamManager  (  URL   url  )  { 
this  .  url  =  url  ; 
} 






public   InputStream   open  (  )  { 
try  { 
if  (  "file"  .  equals  (  url  .  getProtocol  (  )  )  )  { 
if  (  new   File  (  url  .  toURI  (  )  )  .  exists  (  )  )  { 
inputStream  =  url  .  openStream  (  )  ; 
} 
}  else  { 
con  =  url  .  openConnection  (  )  ; 
if  (  con   instanceof   JarURLConnection  )  { 
JarURLConnection   jarCon  =  (  JarURLConnection  )  con  ; 
jarCon  .  setUseCaches  (  false  )  ; 
jarFile  =  jarCon  .  getJarFile  (  )  ; 
} 
inputStream  =  con  .  getInputStream  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
return   inputStream  ; 
} 





public   static   ByteArrayInputStream   bufferedRead  (  URL   url  )  { 
try  { 
URLStreamManager   manager  =  new   URLStreamManager  (  url  )  ; 
InputStream   stream  =  manager  .  open  (  )  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
int   read  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  1204  ]  ; 
while  (  (  read  =  stream  .  read  (  buffer  )  )  >  0  )  { 
out  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
manager  .  close  (  )  ; 
return   new   ByteArrayInputStream  (  out  .  toByteArray  (  )  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 





public   void   close  (  )  { 
try  { 
if  (  inputStream  !=  null  )  { 
inputStream  .  close  (  )  ; 
} 
if  (  jarFile  !=  null  )  { 
jarFile  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 
} 

