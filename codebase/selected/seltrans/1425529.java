package   com  .  jogamp  .  common  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  JarURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   com  .  jogamp  .  common  .  nio  .  Buffers  ; 
import   com  .  jogamp  .  common  .  os  .  MachineDescription  ; 
import   com  .  jogamp  .  common  .  os  .  Platform  ; 

public   class   IOUtil  { 

private   IOUtil  (  )  { 
} 





public   static   int   copyURL2File  (  URL   url  ,  File   outFile  )  throws   IOException  { 
URLConnection   conn  =  url  .  openConnection  (  )  ; 
conn  .  connect  (  )  ; 
int   totalNumBytes  =  0  ; 
InputStream   in  =  new   BufferedInputStream  (  conn  .  getInputStream  (  )  )  ; 
try  { 
OutputStream   out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  outFile  )  )  ; 
try  { 
totalNumBytes  =  copyStream2Stream  (  in  ,  out  ,  conn  .  getContentLength  (  )  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
}  finally  { 
in  .  close  (  )  ; 
} 
return   totalNumBytes  ; 
} 





public   static   int   copyStream2Stream  (  InputStream   in  ,  OutputStream   out  ,  int   totalNumBytes  )  throws   IOException  { 
final   byte  [  ]  buf  =  new   byte  [  Platform  .  getMachineDescription  (  )  .  pageSizeInBytes  (  )  ]  ; 
int   numBytes  =  0  ; 
while  (  true  )  { 
int   count  ; 
if  (  (  count  =  in  .  read  (  buf  )  )  ==  -  1  )  { 
break  ; 
} 
out  .  write  (  buf  ,  0  ,  count  )  ; 
numBytes  +=  count  ; 
} 
return   numBytes  ; 
} 




public   static   byte  [  ]  copyStream2ByteArray  (  InputStream   stream  )  throws   IOException  { 
if  (  !  (  stream   instanceof   BufferedInputStream  )  )  { 
stream  =  new   BufferedInputStream  (  stream  )  ; 
} 
int   totalRead  =  0  ; 
int   avail  =  stream  .  available  (  )  ; 
byte  [  ]  data  =  new   byte  [  avail  ]  ; 
int   numRead  =  0  ; 
do  { 
if  (  totalRead  +  avail  >  data  .  length  )  { 
final   byte  [  ]  newData  =  new   byte  [  totalRead  +  avail  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  newData  ,  0  ,  totalRead  )  ; 
data  =  newData  ; 
} 
numRead  =  stream  .  read  (  data  ,  totalRead  ,  avail  )  ; 
if  (  numRead  >=  0  )  { 
totalRead  +=  numRead  ; 
} 
avail  =  stream  .  available  (  )  ; 
}  while  (  avail  >  0  &&  numRead  >=  0  )  ; 
if  (  totalRead  !=  data  .  length  )  { 
final   byte  [  ]  newData  =  new   byte  [  totalRead  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  newData  ,  0  ,  totalRead  )  ; 
data  =  newData  ; 
} 
return   data  ; 
} 





public   static   ByteBuffer   copyStream2ByteBuffer  (  InputStream   stream  )  throws   IOException  { 
if  (  !  (  stream   instanceof   BufferedInputStream  )  )  { 
stream  =  new   BufferedInputStream  (  stream  )  ; 
} 
int   avail  =  stream  .  available  (  )  ; 
final   MachineDescription   machine  =  Platform  .  getMachineDescription  (  )  ; 
ByteBuffer   data  =  Buffers  .  newDirectByteBuffer  (  machine  .  pageAlignedSize  (  avail  )  )  ; 
byte  [  ]  chunk  =  new   byte  [  machine  .  pageSizeInBytes  (  )  ]  ; 
int   chunk2Read  =  Math  .  min  (  machine  .  pageSizeInBytes  (  )  ,  avail  )  ; 
int   numRead  =  0  ; 
do  { 
if  (  avail  >  data  .  remaining  (  )  )  { 
final   ByteBuffer   newData  =  Buffers  .  newDirectByteBuffer  (  machine  .  pageAlignedSize  (  data  .  position  (  )  +  avail  )  )  ; 
newData  .  put  (  data  )  ; 
data  =  newData  ; 
} 
numRead  =  stream  .  read  (  chunk  ,  0  ,  chunk2Read  )  ; 
if  (  numRead  >=  0  )  { 
data  .  put  (  chunk  ,  0  ,  numRead  )  ; 
} 
avail  =  stream  .  available  (  )  ; 
chunk2Read  =  Math  .  min  (  machine  .  pageSizeInBytes  (  )  ,  avail  )  ; 
}  while  (  avail  >  0  &&  numRead  >=  0  )  ; 
data  .  flip  (  )  ; 
return   data  ; 
} 











public   static   String   getFileSuffix  (  File   file  )  { 
return   getFileSuffix  (  file  .  getName  (  )  )  ; 
} 











public   static   String   getFileSuffix  (  String   filename  )  { 
int   lastDot  =  filename  .  lastIndexOf  (  '.'  )  ; 
if  (  lastDot  <  0  )  { 
return   null  ; 
} 
return   toLowerCase  (  filename  .  substring  (  lastDot  +  1  )  )  ; 
} 

private   static   String   toLowerCase  (  String   arg  )  { 
if  (  arg  ==  null  )  { 
return   null  ; 
} 
return   arg  .  toLowerCase  (  )  ; 
} 








public   static   URL   getResource  (  Class   context  ,  String   resourcePath  )  { 
if  (  null  ==  resourcePath  )  { 
return   null  ; 
} 
ClassLoader   contextCL  =  (  null  !=  context  )  ?  context  .  getClassLoader  (  )  :  null  ; 
URL   url  =  getResource  (  resourcePath  ,  contextCL  )  ; 
if  (  url  ==  null  &&  null  !=  context  )  { 
String   className  =  context  .  getName  (  )  .  replace  (  '.'  ,  '/'  )  ; 
int   lastSlash  =  className  .  lastIndexOf  (  '/'  )  ; 
if  (  lastSlash  >=  0  )  { 
String   tmpPath  =  className  .  substring  (  0  ,  lastSlash  +  1  )  +  resourcePath  ; 
url  =  getResource  (  tmpPath  ,  contextCL  )  ; 
} 
} 
return   url  ; 
} 










public   static   URL   getResource  (  String   resourcePath  ,  ClassLoader   cl  )  { 
if  (  null  ==  resourcePath  )  { 
return   null  ; 
} 
URL   url  =  null  ; 
if  (  cl  !=  null  )  { 
url  =  cl  .  getResource  (  resourcePath  )  ; 
if  (  !  urlExists  (  url  )  )  { 
url  =  null  ; 
} 
} 
if  (  null  ==  url  )  { 
url  =  ClassLoader  .  getSystemResource  (  resourcePath  )  ; 
if  (  !  urlExists  (  url  )  )  { 
url  =  null  ; 
} 
} 
if  (  null  ==  url  )  { 
try  { 
url  =  new   URL  (  resourcePath  )  ; 
if  (  !  urlExists  (  url  )  )  { 
url  =  null  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
} 
} 
if  (  null  ==  url  )  { 
try  { 
File   file  =  new   File  (  resourcePath  )  ; 
if  (  file  .  exists  (  )  )  { 
url  =  file  .  toURL  (  )  ; 
}  else  { 
} 
}  catch  (  MalformedURLException   e  )  { 
} 
} 
return   url  ; 
} 







public   static   String   getRelativeOf  (  File   baseLocation  ,  String   relativeFile  )  { 
if  (  null  ==  relativeFile  )  { 
return   null  ; 
} 
while  (  baseLocation  !=  null  &&  relativeFile  .  startsWith  (  "../"  )  )  { 
baseLocation  =  baseLocation  .  getParentFile  (  )  ; 
relativeFile  =  relativeFile  .  substring  (  3  )  ; 
} 
if  (  baseLocation  !=  null  )  { 
final   File   file  =  new   File  (  baseLocation  ,  relativeFile  )  ; 
return   file  .  getPath  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
} 
return   null  ; 
} 







public   static   String   getRelativeOf  (  URL   baseLocation  ,  String   relativeFile  )  { 
String   urlPath  =  baseLocation  .  getPath  (  )  ; 
if  (  baseLocation  .  toString  (  )  .  startsWith  (  "jar"  )  )  { 
JarURLConnection   jarConnection  ; 
try  { 
jarConnection  =  (  JarURLConnection  )  baseLocation  .  openConnection  (  )  ; 
urlPath  =  jarConnection  .  getEntryName  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 
return   getRelativeOf  (  new   File  (  urlPath  )  .  getParentFile  (  )  ,  relativeFile  )  ; 
} 




public   static   boolean   urlExists  (  URL   url  )  { 
boolean   v  =  false  ; 
if  (  null  !=  url  )  { 
try  { 
URLConnection   uc  =  url  .  openConnection  (  )  ; 
v  =  true  ; 
}  catch  (  IOException   ioe  )  { 
} 
} 
return   v  ; 
} 
} 

