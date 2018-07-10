package   testdeployment  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  BindException  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  TimeZone  ; 
import   org  .  ibex  .  js  .  JSTestUtil  ; 
import   org  .  ibex  .  util  .  DefaultLog  ; 











































public   class   NanoHTTPD  { 

public   final   int   PORT  ; 

private   final   int   MAX_BYTES_PER_SECOND  ; 

public   static   boolean   log  =  true  ; 

private   static   NanoHTTPD   singleton  ; 

public   static   void   start  (  )  throws   Exception  { 
start  (  7070  ,  Integer  .  MAX_VALUE  )  ; 
} 

public   static   void   start  (  int   port  ,  int   rate  )  throws   Exception  { 
start  (  port  ,  rate  ,  null  )  ; 
} 

public   static   void   start  (  int   port  ,  int   rate  ,  File   rootDir  )  throws   Exception  { 
if  (  singleton  ==  null  )  { 
singleton  =  new   NanoHTTPD  (  port  ,  rate  ,  rootDir  )  ; 
System  .  err  .  println  (  "Starting "  +  rootDir  +  " "  +  port  )  ; 
} 
} 

public   static   void   stop  (  )  throws   IOException  { 
if  (  singleton  !=  null  )  { 
singleton  .  myThread  .  interrupt  (  )  ; 
singleton  .  ss  .  close  (  )  ; 
} 
singleton  =  null  ; 
} 












public   Response   serve  (  String   uri  ,  String   method  ,  Properties   header  ,  Properties   parms  )  { 
DefaultLog  .  info  (  NanoHTTPD  .  class  ,  method  +  " '"  +  uri  +  "' "  )  ; 
Enumeration   e  =  header  .  propertyNames  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
String   value  =  (  String  )  e  .  nextElement  (  )  ; 
DefaultLog  .  info  (  NanoHTTPD  .  class  ,  "  HDR: '"  +  value  +  "' = '"  +  header  .  getProperty  (  value  )  +  "'"  )  ; 
} 
e  =  parms  .  propertyNames  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
String   value  =  (  String  )  e  .  nextElement  (  )  ; 
DefaultLog  .  info  (  NanoHTTPD  .  class  ,  "  PRM: '"  +  value  +  "' = '"  +  parms  .  getProperty  (  value  )  +  "'"  )  ; 
} 
return   serveFile  (  uri  ,  method  ,  header  ,  myFileDir  ,  true  )  ; 
} 





public   class   Response  { 




public   Response  (  )  { 
this  .  status  =  HTTP_OK  ; 
} 




public   Response  (  String   status  ,  String   mimeType  ,  InputStream   data  )  { 
this  .  status  =  status  ; 
this  .  mimeType  =  mimeType  ; 
this  .  data  =  data  ; 
} 





public   Response  (  String   status  ,  String   mimeType  ,  String   txt  )  { 
this  .  status  =  status  ; 
this  .  mimeType  =  mimeType  ; 
this  .  data  =  new   ByteArrayInputStream  (  txt  .  getBytes  (  )  )  ; 
} 




public   void   addHeader  (  String   name  ,  String   value  )  { 
header  .  put  (  name  ,  value  )  ; 
} 




public   String   status  ; 




public   String   mimeType  ; 




public   InputStream   data  ; 





public   Properties   header  =  new   Properties  (  )  ; 
} 




public   static   final   String   HTTP_OK  =  "200 OK"  ,  HTTP_REDIRECT  =  "301 Moved Permanently"  ,  HTTP_FORBIDDEN  =  "403 Forbidden"  ,  HTTP_NOTFOUND  =  "404 Not Found"  ,  HTTP_BADREQUEST  =  "400 Bad Request"  ,  HTTP_INTERNALERROR  =  "500 Internal Server Error"  ,  HTTP_NOTIMPLEMENTED  =  "501 Not Implemented"  ; 




public   static   final   String   MIME_PLAINTEXT  =  "text/plain"  ,  MIME_HTML  =  "text/html"  ,  MIME_DEFAULT_BINARY  =  "application/octet-stream"  ; 

ServerSocket   ss  ; 






public   NanoHTTPD  (  int   port  ,  int   rate  ,  File   rootDir  )  throws   IOException  ,  InterruptedException  { 
PORT  =  port  ; 
MAX_BYTES_PER_SECOND  =  rate  ; 
myFileDir  =  rootDir  ==  null  ?  JSTestUtil  .  getResourceFile  (  NanoHTTPD  .  class  ,  ".txt"  )  :  rootDir  ; 
myTcpPort  =  port  ; 
for  (  int   i  =  0  ;  i  <  10  ;  i  ++  )  { 
try  { 
ss  =  new   ServerSocket  (  myTcpPort  )  ; 
break  ; 
}  catch  (  BindException   e  )  { 
Thread  .  sleep  (  100  *  i  )  ; 
} 
} 
myThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
while  (  !  Thread  .  interrupted  (  )  )  new   HTTPSession  (  ss  .  accept  (  )  )  ; 
}  catch  (  SocketException   e  )  { 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
DefaultLog  .  warn  (  NanoHTTPD  .  class  ,  "finished serving"  )  ; 
} 
}  )  ; 
myThread  .  setDaemon  (  true  )  ; 
myThread  .  start  (  )  ; 
} 





private   class   HTTPSession   implements   Runnable  { 

public   HTTPSession  (  Socket   s  )  { 
mySocket  =  s  ; 
Thread   t  =  new   Thread  (  this  )  ; 
t  .  setDaemon  (  true  )  ; 
t  .  start  (  )  ; 
} 

public   void   run  (  )  { 
try  { 
InputStream   is  =  mySocket  .  getInputStream  (  )  ; 
if  (  is  ==  null  )  return  ; 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  is  )  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  in  .  readLine  (  )  )  ; 
if  (  !  st  .  hasMoreTokens  (  )  )  sendError  (  HTTP_BADREQUEST  ,  "BAD REQUEST: Syntax error. Usage: GET /example/file.html"  )  ; 
String   method  =  st  .  nextToken  (  )  ; 
if  (  !  st  .  hasMoreTokens  (  )  )  sendError  (  HTTP_BADREQUEST  ,  "BAD REQUEST: Missing URI. Usage: GET /example/file.html"  )  ; 
String   uri  =  decodePercent  (  st  .  nextToken  (  )  )  ; 
Properties   parms  =  new   Properties  (  )  ; 
int   qmi  =  uri  .  indexOf  (  '?'  )  ; 
if  (  qmi  >=  0  )  { 
decodeParms  (  uri  .  substring  (  qmi  +  1  )  ,  parms  )  ; 
uri  =  decodePercent  (  uri  .  substring  (  0  ,  qmi  )  )  ; 
} 
Properties   header  =  new   Properties  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  { 
String   line  =  in  .  readLine  (  )  ; 
while  (  line  .  trim  (  )  .  length  (  )  >  0  )  { 
int   p  =  line  .  indexOf  (  ':'  )  ; 
header  .  put  (  line  .  substring  (  0  ,  p  )  .  trim  (  )  ,  line  .  substring  (  p  +  1  )  .  trim  (  )  )  ; 
line  =  in  .  readLine  (  )  ; 
} 
} 
if  (  method  .  equalsIgnoreCase  (  "POST"  )  )  { 
long   size  =  0x7FFFFFFFFFFFFFFFl  ; 
String   contentLength  =  header  .  getProperty  (  "Content-Length"  )  ; 
if  (  contentLength  !=  null  )  { 
try  { 
size  =  Integer  .  parseInt  (  contentLength  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
} 
} 
String   postLine  =  ""  ; 
char   buf  [  ]  =  new   char  [  512  ]  ; 
int   read  =  in  .  read  (  buf  )  ; 
while  (  read  >=  0  &&  size  >  0  &&  !  postLine  .  endsWith  (  "\r\n"  )  )  { 
size  -=  read  ; 
postLine  +=  String  .  valueOf  (  buf  )  ; 
if  (  size  >  0  )  read  =  in  .  read  (  buf  )  ; 
} 
postLine  =  postLine  .  trim  (  )  ; 
decodeParms  (  postLine  ,  parms  )  ; 
} 
Response   r  =  serve  (  uri  ,  method  ,  header  ,  parms  )  ; 
if  (  r  ==  null  )  sendError  (  HTTP_INTERNALERROR  ,  "SERVER INTERNAL ERROR: Serve() returned a null response."  )  ;  else   sendResponse  (  r  .  status  ,  r  .  mimeType  ,  r  .  header  ,  r  .  data  )  ; 
in  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
try  { 
sendError  (  HTTP_INTERNALERROR  ,  "SERVER INTERNAL ERROR: IOException: "  +  ioe  .  getMessage  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
} 
}  catch  (  InterruptedException   ie  )  { 
} 
} 





private   String   decodePercent  (  String   str  )  throws   InterruptedException  { 
try  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
char   c  =  str  .  charAt  (  i  )  ; 
switch  (  c  )  { 
case  '+'  : 
sb  .  append  (  ' '  )  ; 
break  ; 
case  '%'  : 
sb  .  append  (  (  char  )  Integer  .  parseInt  (  str  .  substring  (  i  +  1  ,  i  +  3  )  ,  16  )  )  ; 
i  +=  2  ; 
break  ; 
default  : 
sb  .  append  (  c  )  ; 
break  ; 
} 
} 
return   new   String  (  sb  .  toString  (  )  .  getBytes  (  )  )  ; 
}  catch  (  Exception   e  )  { 
sendError  (  HTTP_BADREQUEST  ,  "BAD REQUEST: Bad percent-encoding."  )  ; 
return   null  ; 
} 
} 






private   void   decodeParms  (  String   parms  ,  Properties   p  )  throws   InterruptedException  { 
if  (  parms  ==  null  )  return  ; 
StringTokenizer   st  =  new   StringTokenizer  (  parms  ,  "&"  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   e  =  st  .  nextToken  (  )  ; 
int   sep  =  e  .  indexOf  (  '='  )  ; 
if  (  sep  >=  0  )  p  .  put  (  decodePercent  (  e  .  substring  (  0  ,  sep  )  )  .  trim  (  )  ,  decodePercent  (  e  .  substring  (  sep  +  1  )  )  )  ; 
} 
} 





private   void   sendError  (  String   status  ,  String   msg  )  throws   InterruptedException  { 
sendResponse  (  status  ,  MIME_PLAINTEXT  ,  null  ,  new   ByteArrayInputStream  (  msg  .  getBytes  (  )  )  )  ; 
throw   new   InterruptedException  (  )  ; 
} 




private   void   sendResponse  (  String   status  ,  String   mime  ,  Properties   header  ,  InputStream   data  )  { 
try  { 
if  (  status  ==  null  )  throw   new   Error  (  "sendResponse(): Status can't be null."  )  ; 
OutputStream   out  =  mySocket  .  getOutputStream  (  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  out  )  ; 
pw  .  print  (  "HTTP/1.0 "  +  status  +  " \r\n"  )  ; 
if  (  mime  !=  null  )  pw  .  print  (  "Content-Type: "  +  mime  +  "\r\n"  )  ; 
if  (  header  ==  null  ||  header  .  getProperty  (  "Date"  )  ==  null  )  pw  .  print  (  "Date: "  +  gmtFrmt  .  format  (  new   Date  (  )  )  +  "\r\n"  )  ; 
if  (  header  !=  null  )  { 
Enumeration   e  =  header  .  keys  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
String   key  =  (  String  )  e  .  nextElement  (  )  ; 
String   value  =  header  .  getProperty  (  key  )  ; 
pw  .  print  (  key  +  ": "  +  value  +  "\r\n"  )  ; 
} 
} 
pw  .  print  (  "\r\n"  )  ; 
pw  .  flush  (  )  ; 
if  (  data  !=  null  )  { 
long   start  =  System  .  currentTimeMillis  (  )  ; 
long   total  =  0  ; 
Object   mutex  =  new   Object  (  )  ; 
synchronized  (  mutex  )  { 
byte  [  ]  buff  =  new   byte  [  2048  ]  ; 
while  (  true  )  { 
int   read  =  data  .  read  (  buff  ,  0  ,  2048  )  ; 
if  (  read  <=  0  )  break  ; 
total  +=  read  ; 
if  (  (  total  /  2048  )  %  40  ==  0  )  DefaultLog  .  debug  (  NanoHTTPD  .  class  ,  ""  +  total  )  ; 
out  .  write  (  buff  ,  0  ,  read  )  ; 
long   elapsed  =  System  .  currentTimeMillis  (  )  -  start  ; 
if  (  elapsed  <  total  /  MAX_BYTES_PER_SECOND  )  { 
try  { 
mutex  .  wait  (  (  total  /  MAX_BYTES_PER_SECOND  )  -  elapsed  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  (  total  /  2048  )  %  20  !=  0  )  DefaultLog  .  debug  (  NanoHTTPD  .  class  ,  ""  +  total  )  ; 
} 
} 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
if  (  data  !=  null  )  data  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
try  { 
mySocket  .  close  (  )  ; 
}  catch  (  Throwable   t  )  { 
} 
} 
} 

private   Socket   mySocket  ; 
} 

; 





private   String   encodeUri  (  String   uri  )  { 
String   newUri  =  ""  ; 
StringTokenizer   st  =  new   StringTokenizer  (  uri  ,  "/ "  ,  true  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   tok  =  st  .  nextToken  (  )  ; 
if  (  tok  .  equals  (  "/"  )  )  newUri  +=  "/"  ;  else   if  (  tok  .  equals  (  " "  )  )  newUri  +=  "%20"  ;  else   newUri  +=  URLEncoder  .  encode  (  tok  )  ; 
} 
return   newUri  ; 
} 

private   int   myTcpPort  ; 

File   myFileDir  ; 

Thread   myThread  ; 





public   Response   serveFile  (  String   uri  ,  String   method  ,  Properties   header  ,  File   homeDir  ,  boolean   allowDirectoryListing  )  { 
if  (  !  homeDir  .  isDirectory  (  )  )  return   new   Response  (  HTTP_INTERNALERROR  ,  MIME_PLAINTEXT  ,  "INTERNAL ERRROR: serveFile(): given homeDir is not a directory."  )  ; 
uri  =  uri  .  trim  (  )  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
if  (  uri  .  indexOf  (  '?'  )  >=  0  )  uri  =  uri  .  substring  (  0  ,  uri  .  indexOf  (  '?'  )  )  ; 
if  (  uri  .  startsWith  (  ".."  )  ||  uri  .  endsWith  (  ".."  )  ||  uri  .  indexOf  (  "../"  )  >=  0  )  return   new   Response  (  HTTP_FORBIDDEN  ,  MIME_PLAINTEXT  ,  "FORBIDDEN: Won't serve ../ for security reasons."  )  ; 
File   f  =  new   File  (  homeDir  ,  uri  )  ; 
if  (  !  f  .  exists  (  )  )  return   new   Response  (  HTTP_NOTFOUND  ,  MIME_PLAINTEXT  ,  "Error 404, file not found."  )  ; 
if  (  f  .  isDirectory  (  )  )  { 
if  (  !  uri  .  endsWith  (  "/"  )  )  { 
uri  +=  "/"  ; 
Response   r  =  new   Response  (  HTTP_REDIRECT  ,  MIME_HTML  ,  "<html><body>Redirected: <a href=\""  +  uri  +  "\">"  +  uri  +  "</a></body></html>"  )  ; 
r  .  addHeader  (  "Location"  ,  uri  )  ; 
return   r  ; 
} 
if  (  new   File  (  f  ,  "index.html"  )  .  exists  (  )  )  f  =  new   File  (  homeDir  ,  uri  +  "/index.html"  )  ;  else   if  (  new   File  (  f  ,  "index.htm"  )  .  exists  (  )  )  f  =  new   File  (  homeDir  ,  uri  +  "/index.htm"  )  ;  else   if  (  allowDirectoryListing  )  { 
String  [  ]  files  =  f  .  list  (  )  ; 
String   msg  =  "<html><body><h1>Directory "  +  uri  +  "</h1><br/>"  ; 
if  (  uri  .  length  (  )  >  1  )  { 
String   u  =  uri  .  substring  (  0  ,  uri  .  length  (  )  -  1  )  ; 
int   slash  =  u  .  lastIndexOf  (  '/'  )  ; 
if  (  slash  >=  0  &&  slash  <  u  .  length  (  )  )  msg  +=  "<b><a href=\""  +  uri  .  substring  (  0  ,  slash  +  1  )  +  "\">..</a></b><br/>"  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  ++  i  )  { 
File   curFile  =  new   File  (  f  ,  files  [  i  ]  )  ; 
boolean   dir  =  curFile  .  isDirectory  (  )  ; 
if  (  dir  )  { 
msg  +=  "<b>"  ; 
files  [  i  ]  +=  "/"  ; 
} 
msg  +=  "<a href=\""  +  encodeUri  (  uri  +  files  [  i  ]  )  +  "\">"  +  files  [  i  ]  +  "</a>"  ; 
if  (  curFile  .  isFile  (  )  )  { 
long   len  =  curFile  .  length  (  )  ; 
msg  +=  " &nbsp;<font size=2>("  ; 
if  (  len  <  1024  )  msg  +=  curFile  .  length  (  )  +  " bytes"  ;  else   if  (  len  <  1024  *  1024  )  msg  +=  curFile  .  length  (  )  /  1024  +  "."  +  (  curFile  .  length  (  )  %  1024  /  10  %  100  )  +  " KB"  ;  else   msg  +=  curFile  .  length  (  )  /  (  1024  *  1024  )  +  "."  +  curFile  .  length  (  )  %  (  1024  *  1024  )  /  10  %  100  +  " MB"  ; 
msg  +=  ")</font>"  ; 
} 
msg  +=  "<br/>"  ; 
if  (  dir  )  msg  +=  "</b>"  ; 
} 
return   new   Response  (  HTTP_OK  ,  MIME_HTML  ,  msg  )  ; 
}  else  { 
return   new   Response  (  HTTP_FORBIDDEN  ,  MIME_PLAINTEXT  ,  "FORBIDDEN: No directory listing."  )  ; 
} 
} 
try  { 
String   mime  =  null  ; 
int   dot  =  f  .  getCanonicalPath  (  )  .  lastIndexOf  (  '.'  )  ; 
if  (  dot  >=  0  )  mime  =  (  String  )  theMimeTypes  .  get  (  f  .  getCanonicalPath  (  )  .  substring  (  dot  +  1  )  .  toLowerCase  (  )  )  ; 
if  (  mime  ==  null  )  mime  =  MIME_DEFAULT_BINARY  ; 
long   startFrom  =  0  ; 
String   range  =  header  .  getProperty  (  "Range"  )  ; 
if  (  range  !=  null  )  { 
if  (  range  .  startsWith  (  "bytes="  )  )  { 
range  =  range  .  substring  (  "bytes="  .  length  (  )  )  ; 
int   minus  =  range  .  indexOf  (  '-'  )  ; 
if  (  minus  >  0  )  range  =  range  .  substring  (  0  ,  minus  )  ; 
try  { 
startFrom  =  Long  .  parseLong  (  range  )  ; 
}  catch  (  NumberFormatException   nfe  )  { 
} 
} 
} 
InputStream   is  =  null  ; 
if  (  "HEAD"  .  equals  (  method  )  )  { 
is  =  new   ByteArrayInputStream  (  new   byte  [  ]  {  }  )  ; 
}  else  { 
is  =  new   FileInputStream  (  f  )  ; 
is  .  skip  (  startFrom  )  ; 
} 
Response   r  =  new   Response  (  HTTP_OK  ,  mime  ,  is  )  ; 
r  .  addHeader  (  "Content-length"  ,  ""  +  (  f  .  length  (  )  -  startFrom  )  )  ; 
r  .  addHeader  (  "Content-range"  ,  ""  +  startFrom  +  "-"  +  (  f  .  length  (  )  -  1  )  +  "/"  +  f  .  length  (  )  )  ; 
r  .  addHeader  (  "last-modified"  ,  ""  +  formatDate  (  f  .  lastModified  (  )  )  )  ; 
return   r  ; 
}  catch  (  IOException   ioe  )  { 
return   new   Response  (  HTTP_FORBIDDEN  ,  MIME_PLAINTEXT  ,  "FORBIDDEN: Reading file failed."  )  ; 
} 
} 

private   static   final   SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "EEE, dd MMM yyyy HH:mm:ss zzz"  )  ; 

private   static   String   formatDate  (  long   t  )  { 
return   sdf  .  format  (  new   Date  (  t  )  )  ; 
} 




private   static   Hashtable   theMimeTypes  =  new   Hashtable  (  )  ; 

static  { 
StringTokenizer   st  =  new   StringTokenizer  (  "htm		text/html "  +  "html		text/html "  +  "txt		text/plain "  +  "asc		text/plain "  +  "gif		image/gif "  +  "jpg		image/jpeg "  +  "jpeg		image/jpeg "  +  "png		image/png "  +  "mp3		audio/mpeg "  +  "m3u		audio/mpeg-url "  +  "pdf		application/pdf "  +  "doc		application/msword "  +  "ogg		application/x-ogg "  +  "zip		application/octet-stream "  +  "vexi		application/octet-stream "  +  "exe		application/octet-stream "  +  "class		application/octet-stream "  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  theMimeTypes  .  put  (  st  .  nextToken  (  )  ,  st  .  nextToken  (  )  )  ; 
} 




private   static   java  .  text  .  SimpleDateFormat   gmtFrmt  ; 

static  { 
gmtFrmt  =  new   java  .  text  .  SimpleDateFormat  (  "E, d MMM yyyy HH:mm:ss 'GMT'"  ,  Locale  .  US  )  ; 
gmtFrmt  .  setTimeZone  (  TimeZone  .  getTimeZone  (  "GMT"  )  )  ; 
} 
} 

