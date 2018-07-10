package   org  .  gvsig  .  xmlschema  .  utils  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  ConnectException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  rmi  .  NoSuchObjectException  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  Vector  ; 






public   class   DownloadUtilities  { 

private   static   String   characters  ; 

static   boolean   canceled  ; 

static   final   long   latency  =  500  ; 




private   static   Hashtable   downloadedFiles  ; 

static   Exception   downloadException  ; 

private   static   final   String   tempDirectoryPath  =  System  .  getProperty  (  "java.io.tmpdir"  )  +  "/tmp-andami"  ; 

static  { 
characters  =  ""  ; 
for  (  int   j  =  32  ;  j  <=  127  ;  j  ++  )  { 
characters  +=  (  char  )  j  ; 
} 
characters  +=  "�������������������������������������������ǡ�����\n\r\f\t��"  ; 
} 










public   static   boolean   isTextFile  (  File   file  )  { 
return   isTextFile  (  file  ,  1024  )  ; 
} 











public   static   boolean   isTextFile  (  File   file  ,  int   byteAmount  )  { 
int   umbral  =  byteAmount  ; 
try  { 
FileReader   fr  =  new   FileReader  (  file  )  ; 
for  (  int   i  =  0  ;  i  <  umbral  ;  i  ++  )  { 
int   c  =  fr  .  read  (  )  ; 
if  (  c  ==  -  1  )  { 
return   true  ; 
} 
char   ch  =  (  char  )  c  ; 
if  (  characters  .  indexOf  (  ch  )  ==  -  1  )  { 
return   false  ; 
} 
} 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   true  ; 
} 









public   static   boolean   isTextData  (  byte  [  ]  data  )  { 
char  [  ]  charData  =  new   char  [  data  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
charData  [  i  ]  =  (  char  )  data  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   c  =  charData  [  i  ]  ; 
if  (  c  ==  -  1  )  { 
return   true  ; 
} 
char   ch  =  (  char  )  c  ; 
if  (  characters  .  indexOf  (  ch  )  ==  -  1  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 







public   static   void   serializar  (  InputStream   in  ,  OutputStream   out  )  { 
byte  [  ]  buffer  =  new   byte  [  102400  ]  ; 
int   n  ; 
try  { 
while  (  (  n  =  in  .  read  (  buffer  )  )  !=  -  1  )  { 
out  .  write  (  buffer  ,  0  ,  n  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 









public   static   byte  [  ]  eliminarDTD  (  byte  [  ]  bytes  ,  String   startTag  )  { 
String   text  =  new   String  (  bytes  )  ; 
int   index1  =  text  .  indexOf  (  "?>"  )  +  2  ; 
int   index2  ; 
try  { 
index2  =  findBeginIndex  (  bytes  ,  startTag  )  ; 
}  catch  (  NoSuchObjectException   e  )  { 
return   bytes  ; 
} 
byte  [  ]  buffer  =  new   byte  [  bytes  .  length  -  (  index2  -  index1  )  ]  ; 
System  .  arraycopy  (  bytes  ,  0  ,  buffer  ,  0  ,  index1  )  ; 
System  .  arraycopy  (  bytes  ,  index2  ,  buffer  ,  index1  ,  bytes  .  length  -  index2  )  ; 
return   buffer  ; 
} 











private   static   int   findBeginIndex  (  byte  [  ]  bytes  ,  String   tagRaiz  )  throws   NoSuchObjectException  { 
try  { 
int   nodo  =  0  ; 
int   ret  =  -  1  ; 
int   i  =  0  ; 
while  (  true  )  { 
switch  (  nodo  )  { 
case   0  : 
if  (  bytes  [  i  ]  ==  '<'  )  { 
ret  =  i  ; 
nodo  =  1  ; 
} 
break  ; 
case   1  : 
if  (  bytes  [  i  ]  ==  ' '  )  { 
}  else   if  (  bytes  [  i  ]  ==  tagRaiz  .  charAt  (  0  )  )  { 
nodo  =  2  ; 
}  else  { 
nodo  =  0  ; 
} 
break  ; 
case   2  : 
String   aux  =  new   String  (  bytes  ,  i  ,  18  )  ; 
if  (  aux  .  equalsIgnoreCase  (  tagRaiz  .  substring  (  1  )  )  )  { 
return   ret  ; 
} 
nodo  =  0  ; 
break  ; 
} 
i  ++  ; 
} 
}  catch  (  Exception   e  )  { 
throw   new   NoSuchObjectException  (  "No se pudo parsear el xml"  )  ; 
} 
} 





public   static   String   Vector2CS  (  Vector   v  )  { 
String   str  =  new   String  (  )  ; 
if  (  v  !=  null  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  v  .  size  (  )  ;  i  ++  )  { 
str  =  str  +  v  .  elementAt  (  i  )  ; 
if  (  i  <  v  .  size  (  )  -  1  )  str  =  str  +  ","  ; 
} 
} 
return   str  ; 
} 

public   static   boolean   isValidVersion  (  String   version  )  { 
if  (  version  .  trim  (  )  .  length  (  )  ==  5  )  { 
if  (  (  version  .  charAt  (  1  )  ==  '.'  )  &&  (  version  .  charAt  (  3  )  ==  '.'  )  )  { 
char   x  =  version  .  charAt  (  0  )  ; 
char   y  =  version  .  charAt  (  2  )  ; 
char   z  =  version  .  charAt  (  4  )  ; 
if  (  (  Character  .  isDigit  (  x  )  )  &&  (  Character  .  isDigit  (  y  )  )  &&  (  Character  .  isDigit  (  z  )  )  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  else  { 
return   false  ; 
} 
}  else  { 
return   false  ; 
} 
} 







public   static   void   createTemp  (  String   fileName  ,  String   data  )  throws   IOException  { 
File   f  =  new   File  (  fileName  )  ; 
DataOutputStream   dos  =  new   DataOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  f  )  )  )  ; 
dos  .  writeBytes  (  data  )  ; 
dos  .  close  (  )  ; 
f  .  deleteOnExit  (  )  ; 
} 







public   static   boolean   isNumber  (  String   s  )  { 
try  { 
return   true  ; 
}  catch  (  NumberFormatException   e  )  { 
return   false  ; 
} 
} 








public   static   Vector   createVector  (  String   str  ,  String   c  )  { 
StringTokenizer   tokens  =  new   StringTokenizer  (  str  ,  c  )  ; 
Vector   v  =  new   Vector  (  )  ; 
try  { 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
v  .  addElement  (  tokens  .  nextToken  (  )  )  ; 
} 
return   v  ; 
}  catch  (  Exception   e  )  { 
return   new   Vector  (  )  ; 
} 
} 





public   static   String   Vector2URLParamString  (  Vector   v  )  { 
if  (  v  ==  null  )  return  ""  ; 
String   s  =  ""  ; 
for  (  int   i  =  0  ;  i  <  v  .  size  (  )  ;  i  ++  )  { 
s  +=  v  .  get  (  i  )  ; 
if  (  i  <  v  .  size  (  )  -  1  )  s  +=  "&"  ; 
} 
return   s  ; 
} 












private   static   File   getPreviousDownloadedURL  (  URL   url  )  { 
File   f  =  null  ; 
if  (  downloadedFiles  !=  null  &&  downloadedFiles  .  containsKey  (  url  )  )  { 
String   filePath  =  (  String  )  downloadedFiles  .  get  (  url  )  ; 
f  =  new   File  (  filePath  )  ; 
if  (  !  f  .  exists  (  )  )  return   null  ; 
} 
return   f  ; 
} 









static   void   addDownloadedURL  (  URL   url  ,  String   filePath  )  { 
if  (  downloadedFiles  ==  null  )  downloadedFiles  =  new   Hashtable  (  )  ; 
String   fileName  =  (  String  )  downloadedFiles  .  put  (  url  ,  filePath  )  ; 
} 













public   static   synchronized   File   downloadFile  (  URL   url  ,  String   name  )  throws   IOException  ,  ConnectException  ,  UnknownHostException  { 
File   f  =  null  ; 
if  (  (  f  =  getPreviousDownloadedURL  (  url  )  )  ==  null  )  { 
File   tempDirectory  =  new   File  (  tempDirectoryPath  )  ; 
if  (  !  tempDirectory  .  exists  (  )  )  tempDirectory  .  mkdir  (  )  ; 
f  =  new   File  (  tempDirectoryPath  +  "/"  +  name  +  System  .  currentTimeMillis  (  )  )  ; 
Thread   downloader  =  new   Thread  (  new   Downloader  (  url  ,  f  )  )  ; 
downloader  .  start  (  )  ; 
while  (  !  canceled  &&  downloader  .  isAlive  (  )  )  { 
try  { 
Thread  .  sleep  (  latency  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
if  (  canceled  )  return   null  ; 
downloader  =  null  ; 
if  (  DownloadUtilities  .  downloadException  !=  null  )  { 
Exception   e  =  DownloadUtilities  .  downloadException  ; 
if  (  e   instanceof   FileNotFoundException  )  throw  (  IOException  )  e  ;  else   if  (  e   instanceof   IOException  )  throw  (  IOException  )  e  ;  else   if  (  e   instanceof   ConnectException  )  throw  (  ConnectException  )  e  ;  else   if  (  e   instanceof   UnknownHostException  )  throw  (  UnknownHostException  )  e  ; 
} 
}  else  { 
System  .  out  .  println  (  url  .  toString  (  )  +  " cached at '"  +  f  .  getAbsolutePath  (  )  +  "'"  )  ; 
} 
return   f  ; 
} 




public   static   void   cleanUpTempFiles  (  )  { 
try  { 
File   tempDirectory  =  new   File  (  tempDirectoryPath  )  ; 
File  [  ]  files  =  tempDirectory  .  listFiles  (  )  ; 
if  (  files  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  deleteDirectory  (  files  [  i  ]  )  ; 
files  [  i  ]  .  delete  (  )  ; 
} 
} 
tempDirectory  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 





private   static   void   deleteDirectory  (  File   f  )  { 
File  [  ]  files  =  f  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  deleteDirectory  (  files  [  i  ]  )  ; 
files  [  i  ]  .  delete  (  )  ; 
} 
} 






public   static   void   removeURL  (  URL   url  )  { 
if  (  downloadedFiles  !=  null  &&  downloadedFiles  .  containsKey  (  url  )  )  downloadedFiles  .  remove  (  url  )  ; 
} 
} 

final   class   Monitor   implements   Runnable  { 

public   void   run  (  )  { 
try  { 
Thread  .  sleep  (  DownloadUtilities  .  latency  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
DownloadUtilities  .  canceled  =  true  ; 
} 
} 

final   class   Downloader   implements   Runnable  { 

private   URL   url  ; 

private   File   dstFile  ; 

public   Downloader  (  URL   url  ,  File   dstFile  )  { 
this  .  url  =  url  ; 
this  .  dstFile  =  dstFile  ; 
DownloadUtilities  .  downloadException  =  null  ; 
} 

public   void   run  (  )  { 
System  .  out  .  println  (  "downloading '"  +  url  .  toString  (  )  +  "' to: "  +  dstFile  .  getAbsolutePath  (  )  )  ; 
DataOutputStream   dos  ; 
try  { 
dos  =  new   DataOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  dstFile  )  )  )  ; 
byte  [  ]  buffer  =  new   byte  [  1024  *  4  ]  ; 
DataInputStream   is  =  new   DataInputStream  (  url  .  openStream  (  )  )  ; 
long   readed  =  0  ; 
for  (  int   i  =  is  .  read  (  buffer  )  ;  !  DownloadUtilities  .  canceled  &&  i  >  0  ;  i  =  is  .  read  (  buffer  )  )  { 
dos  .  write  (  buffer  ,  0  ,  i  )  ; 
readed  +=  i  ; 
} 
dos  .  close  (  )  ; 
is  .  close  (  )  ; 
is  =  null  ; 
dos  =  null  ; 
if  (  DownloadUtilities  .  canceled  )  { 
System  .  err  .  println  (  "[RemoteClients] '"  +  url  +  "' CANCELED."  )  ; 
dstFile  .  delete  (  )  ; 
dstFile  =  null  ; 
}  else  { 
DownloadUtilities  .  addDownloadedURL  (  url  ,  dstFile  .  getAbsolutePath  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
DownloadUtilities  .  downloadException  =  e  ; 
} 
} 
} 

