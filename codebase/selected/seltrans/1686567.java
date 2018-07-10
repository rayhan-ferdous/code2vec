package   dk  .  i2m  .  converge  .  mobile  .  server  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 






public   class   FileUtils  { 




private   FileUtils  (  )  { 
} 

public   static   String   getFilename  (  final   String   filename  )  { 
int   pos  =  filename  .  lastIndexOf  (  File  .  separator  )  ; 
if  (  pos  !=  -  1  )  { 
return   filename  .  substring  (  pos  +  1  )  ; 
}  else  { 
return   filename  ; 
} 
} 

public   static   String   getFolder  (  final   String   filename  )  { 
int   pos  =  filename  .  lastIndexOf  (  File  .  separator  )  ; 
if  (  pos  !=  -  1  )  { 
return   getFilename  (  filename  .  substring  (  0  ,  pos  )  )  ; 
}  else  { 
return  ""  ; 
} 
} 











public   static   void   writeToFile  (  byte  [  ]  content  ,  String   fileName  )  throws   IOException  { 
FileOutputStream   out  =  null  ; 
out  =  new   FileOutputStream  (  fileName  )  ; 
try  { 
out  .  write  (  content  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
} 










public   static   byte  [  ]  getBytes  (  InputStream   is  )  throws   IOException  { 
byte   out  [  ]  =  new   byte  [  is  .  available  (  )  ]  ; 
int   bytesread  =  0  ; 
int   i  ; 
while  (  bytesread  <  out  .  length  )  { 
i  =  is  .  read  (  out  ,  bytesread  ,  out  .  length  -  bytesread  )  ; 
if  (  i  <  0  )  { 
throw   new   IOException  (  "Ran out of bytes to read!"  )  ; 
} 
bytesread  +=  i  ; 
} 
return   out  ; 
} 










public   static   byte  [  ]  getBytes  (  java  .  io  .  File   file  )  throws   IOException  { 
InputStream   is  =  new   FileInputStream  (  file  )  ; 
long   length  =  file  .  length  (  )  ; 
if  (  length  >  Integer  .  MAX_VALUE  )  { 
throw   new   IOException  (  "File size is too big"  )  ; 
} 
byte  [  ]  bytes  =  new   byte  [  (  int  )  length  ]  ; 
int   offset  =  0  ; 
int   numRead  =  0  ; 
while  (  offset  <  bytes  .  length  &&  (  numRead  =  is  .  read  (  bytes  ,  offset  ,  bytes  .  length  -  offset  )  )  >=  0  )  { 
offset  +=  numRead  ; 
} 
if  (  offset  <  bytes  .  length  )  { 
throw   new   IOException  (  "Could not completely read file "  +  file  .  getName  (  )  )  ; 
} 
is  .  close  (  )  ; 
return   bytes  ; 
} 










public   static   byte  [  ]  getBytes  (  URL   url  )  throws   IOException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
InputStream   in  =  connection  .  getInputStream  (  )  ; 
int   contentLength  =  connection  .  getContentLength  (  )  ; 
ByteArrayOutputStream   tmpOut  ; 
if  (  contentLength  !=  -  1  )  { 
tmpOut  =  new   ByteArrayOutputStream  (  contentLength  )  ; 
}  else  { 
tmpOut  =  new   ByteArrayOutputStream  (  16384  )  ; 
} 
byte  [  ]  buf  =  new   byte  [  512  ]  ; 
while  (  true  )  { 
int   len  =  in  .  read  (  buf  )  ; 
if  (  len  ==  -  1  )  { 
break  ; 
} 
tmpOut  .  write  (  buf  ,  0  ,  len  )  ; 
} 
in  .  close  (  )  ; 
tmpOut  .  close  (  )  ; 
byte  [  ]  array  =  tmpOut  .  toByteArray  (  )  ; 
return   array  ; 
} 

public   static   String   getString  (  InputStream   is  )  throws   IOException  { 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  is  )  )  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
String   line  =  null  ; 
try  { 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  )  .  append  (  StringUtils  .  LINE_BREAK  )  ; 
} 
}  finally  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   e  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 
} 

