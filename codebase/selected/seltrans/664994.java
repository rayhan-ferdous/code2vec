package   org  .  fto  .  jthink  .  j2ee  .  web  .  fileload  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   javax  .  servlet  .  ServletContext  ; 
import   javax  .  servlet  .  ServletOutputStream  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   org  .  fto  .  jthink  .  exception  .  JThinkRuntimeException  ; 
import   org  .  fto  .  jthink  .  log  .  LogManager  ; 
import   org  .  fto  .  jthink  .  log  .  Logger  ; 














public   class   FileDownload  { 

private   static   Logger   logger  =  LogManager  .  getLogger  (  FileDownload  .  class  )  ; 

protected   HttpServletResponse   response  ; 

protected   ServletContext   application  ; 

private   boolean   denyPhysicalPath  ; 

private   String   contentDisposition  ; 







public   FileDownload  (  ServletContext   application  ,  HttpServletResponse   response  )  { 
this  .  response  =  response  ; 
this  .  application  =  application  ; 
this  .  denyPhysicalPath  =  false  ; 
} 






public   void   setDenyPhysicalPath  (  boolean   deny  )  { 
this  .  denyPhysicalPath  =  deny  ; 
} 






public   void   setContentDisposition  (  String   contentDisposition  )  { 
this  .  contentDisposition  =  contentDisposition  ; 
} 






public   void   downloadFile  (  String   sourceFilePathName  )  { 
downloadFile  (  sourceFilePathName  ,  null  ,  null  )  ; 
} 







public   void   downloadFile  (  String   sourceFilePathName  ,  String   contentType  )  { 
downloadFile  (  sourceFilePathName  ,  contentType  ,  null  )  ; 
} 








public   void   downloadFile  (  String   sourceFilePathName  ,  String   contentType  ,  String   destFileName  )  { 
downloadFile  (  sourceFilePathName  ,  contentType  ,  destFileName  ,  65000  )  ; 
} 









public   void   downloadFile  (  String   sourceFilePathName  ,  String   contentType  ,  String   destFileName  ,  int   blockSize  )  { 
if  (  sourceFilePathName  ==  null  ||  sourceFilePathName  .  trim  (  )  .  equals  (  ""  )  )  { 
logger  .  error  (  "文件 '"  +  sourceFilePathName  +  "' 没有找到."  )  ; 
throw   new   IllegalArgumentException  (  "文件 '"  +  sourceFilePathName  +  "' 没有找到."  )  ; 
} 
if  (  !  isVirtual  (  sourceFilePathName  )  &&  denyPhysicalPath  )  { 
logger  .  error  (  "物理路径被拒绝."  )  ; 
throw   new   SecurityException  (  "Physical path is denied."  )  ; 
} 
ServletOutputStream   servletoutputstream  =  null  ; 
BufferedOutputStream   bufferedoutputstream  =  null  ; 
FileInputStream   fileIn  =  null  ; 
try  { 
if  (  isVirtual  (  sourceFilePathName  )  )  { 
sourceFilePathName  =  application  .  getRealPath  (  sourceFilePathName  )  ; 
} 
File   file  =  new   File  (  sourceFilePathName  )  ; 
fileIn  =  new   FileInputStream  (  file  )  ; 
long   fileLen  =  file  .  length  (  )  ; 
int   readBytes  =  0  ; 
int   totalRead  =  0  ; 
byte   b  [  ]  =  new   byte  [  blockSize  ]  ; 
if  (  contentType  ==  null  ||  contentType  .  trim  (  )  .  length  (  )  ==  0  )  { 
response  .  setContentType  (  "application/x-msdownload"  )  ; 
}  else  { 
response  .  setContentType  (  contentType  )  ; 
} 
contentDisposition  =  contentDisposition  !=  null  ?  contentDisposition  :  "attachment;"  ; 
if  (  destFileName  ==  null  ||  destFileName  .  trim  (  )  .  length  (  )  ==  0  )  { 
response  .  setHeader  (  "Content-Disposition"  ,  contentDisposition  +  " filename="  +  toUtf8String  (  getFileName  (  sourceFilePathName  )  )  )  ; 
}  else  { 
response  .  setHeader  (  "Content-Disposition"  ,  String  .  valueOf  (  (  new   StringBuffer  (  String  .  valueOf  (  contentDisposition  )  )  )  .  append  (  " filename="  )  .  append  (  toUtf8String  (  destFileName  )  )  )  )  ; 
} 
servletoutputstream  =  response  .  getOutputStream  (  )  ; 
bufferedoutputstream  =  new   BufferedOutputStream  (  servletoutputstream  )  ; 
while  (  (  long  )  totalRead  <  fileLen  )  { 
readBytes  =  fileIn  .  read  (  b  ,  0  ,  blockSize  )  ; 
totalRead  +=  readBytes  ; 
bufferedoutputstream  .  write  (  b  ,  0  ,  readBytes  )  ; 
} 
fileIn  .  close  (  )  ; 
}  catch  (  JThinkRuntimeException   e  )  { 
throw   e  ; 
}  catch  (  Exception   e  )  { 
logger  .  error  (  "下载文件时发生异常."  ,  e  )  ; 
throw   new   JThinkRuntimeException  (  e  )  ; 
}  finally  { 
if  (  bufferedoutputstream  !=  null  )  { 
try  { 
bufferedoutputstream  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
logger  .  error  (  "关闭BufferedOutputStream时发生异常."  ,  e1  )  ; 
e1  .  printStackTrace  (  )  ; 
} 
} 
if  (  fileIn  !=  null  )  { 
try  { 
fileIn  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
logger  .  error  (  "关闭FileInputStream时发生异常."  ,  e1  )  ; 
e1  .  printStackTrace  (  )  ; 
} 
} 
} 
} 







private   boolean   isVirtual  (  String   pathName  )  { 
if  (  application  .  getRealPath  (  pathName  )  !=  null  )  { 
File   virtualFile  =  new   File  (  application  .  getRealPath  (  pathName  )  )  ; 
return   virtualFile  .  exists  (  )  ; 
}  else  { 
return   false  ; 
} 
} 







private   String   getFileName  (  String   filePathName  )  { 
int   pos  =  0  ; 
pos  =  filePathName  .  lastIndexOf  (  '/'  )  ; 
if  (  pos  !=  -  1  )  return   filePathName  .  substring  (  pos  +  1  ,  filePathName  .  length  (  )  )  ; 
pos  =  filePathName  .  lastIndexOf  (  '\\'  )  ; 
if  (  pos  !=  -  1  )  return   filePathName  .  substring  (  pos  +  1  ,  filePathName  .  length  (  )  )  ;  else   return   filePathName  ; 
} 







private   String   toUtf8String  (  String   s  )  throws   UnsupportedEncodingException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
char   c  =  s  .  charAt  (  i  )  ; 
if  (  c  >=  0  &&  c  <=  255  )  { 
sb  .  append  (  c  )  ; 
}  else  { 
byte  [  ]  b  ; 
b  =  String  .  valueOf  (  c  )  .  getBytes  (  "utf-8"  )  ; 
for  (  int   j  =  0  ;  j  <  b  .  length  ;  j  ++  )  { 
int   k  =  b  [  j  ]  ; 
if  (  k  <  0  )  k  +=  256  ; 
sb  .  append  (  "%"  +  Integer  .  toHexString  (  k  )  .  toUpperCase  (  )  )  ; 
} 
} 
} 
return   sb  .  toString  (  )  ; 
} 
} 

