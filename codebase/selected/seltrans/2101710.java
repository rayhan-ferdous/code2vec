package   org  .  myrobotlab  .  net  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Map  .  Entry  ; 
import   org  .  apache  .  log4j  .  Level  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  myrobotlab  .  framework  .  Service  ; 
























public   class   HTTPRequest  { 

public   static   final   Logger   LOG  =  Logger  .  getLogger  (  HTTPRequest  .  class  .  getCanonicalName  (  )  )  ; 

URLConnection   connection  ; 

OutputStream   osstr  =  null  ; 

BufferedOutputStream   os  =  null  ; 

Map  <  String  ,  String  >  cookies  =  new   HashMap  <  String  ,  String  >  (  )  ; 

String   boundary  =  "---------------------------"  ; 

String   error  =  null  ; 

protected   void   connect  (  )  throws   IOException  { 
if  (  os  ==  null  )  os  =  new   BufferedOutputStream  (  connection  .  getOutputStream  (  )  )  ; 
} 

protected   void   write  (  char   c  )  throws   IOException  { 
connect  (  )  ; 
os  .  write  (  c  )  ; 
} 

protected   void   write  (  String   s  )  throws   IOException  { 
Service  .  logTime  (  "t1"  ,  "write-connect"  )  ; 
connect  (  )  ; 
Service  .  logTime  (  "t1"  ,  "write-post connect"  )  ; 
os  .  write  (  s  .  getBytes  (  )  )  ; 
Service  .  logTime  (  "t1"  ,  "post write s.getBytes"  )  ; 
} 

protected   void   newline  (  )  throws   IOException  { 
connect  (  )  ; 
write  (  "\r\n"  )  ; 
} 

protected   void   writeln  (  String   s  )  throws   IOException  { 
connect  (  )  ; 
write  (  s  )  ; 
newline  (  )  ; 
} 

private   void   boundary  (  )  throws   IOException  { 
write  (  "--"  )  ; 
write  (  boundary  )  ; 
} 









public   HTTPRequest  (  URLConnection   connection  )  throws   IOException  { 
LOG  .  info  (  "http request for "  +  connection  .  getURL  (  )  )  ; 
this  .  connection  =  connection  ; 
connection  .  setDoOutput  (  true  )  ; 
} 








public   HTTPRequest  (  URL   url  )  throws   IOException  { 
this  (  url  .  openConnection  (  )  )  ; 
} 








public   HTTPRequest  (  String   urlString  )  throws   IOException  { 
this  (  new   URL  (  urlString  )  )  ; 
} 

public   void   setRequestProperty  (  String   key  ,  String   value  )  { 
if  (  connection  !=  null  )  { 
connection  .  setRequestProperty  (  key  ,  value  )  ; 
} 
} 

public   void   postCookies  (  )  { 
StringBuffer   cookieList  =  new   StringBuffer  (  )  ; 
for  (  Iterator  <  Entry  <  String  ,  String  >  >  i  =  cookies  .  entrySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Entry  <  String  ,  String  >  entry  =  (  i  .  next  (  )  )  ; 
cookieList  .  append  (  entry  .  getKey  (  )  .  toString  (  )  +  "="  +  entry  .  getValue  (  )  )  ; 
if  (  i  .  hasNext  (  )  )  { 
cookieList  .  append  (  "; "  )  ; 
} 
} 
if  (  cookieList  .  length  (  )  >  0  )  { 
connection  .  setRequestProperty  (  "Cookie"  ,  cookieList  .  toString  (  )  )  ; 
} 
} 










public   void   setCookie  (  String   name  ,  String   value  )  throws   IOException  { 
cookies  .  put  (  name  ,  value  )  ; 
} 








public   void   setCookies  (  Map  <  String  ,  String  >  cookies  )  throws   IOException  { 
if  (  cookies  ==  null  )  return  ; 
this  .  cookies  .  putAll  (  cookies  )  ; 
} 









public   void   setCookies  (  String  [  ]  cookies  )  throws   IOException  { 
if  (  cookies  ==  null  )  return  ; 
for  (  int   i  =  0  ;  i  <  cookies  .  length  -  1  ;  i  +=  2  )  { 
setCookie  (  cookies  [  i  ]  ,  cookies  [  i  +  1  ]  )  ; 
} 
} 

private   void   writeName  (  String   name  )  throws   IOException  { 
newline  (  )  ; 
write  (  "Content-Disposition: form-data; name=\""  )  ; 
write  (  name  )  ; 
write  (  '"'  )  ; 
} 










public   void   setParameter  (  String   name  ,  String   value  )  throws   IOException  { 
boundary  (  )  ; 
writeName  (  name  )  ; 
newline  (  )  ; 
newline  (  )  ; 
writeln  (  value  )  ; 
} 

private   void   pipe  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  500000  ]  ; 
int   nread  ; 
synchronized  (  in  )  { 
while  (  (  nread  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  >=  0  )  { 
out  .  write  (  buf  ,  0  ,  nread  )  ; 
} 
} 
out  .  flush  (  )  ; 
in  .  close  (  )  ; 
buf  =  null  ; 
} 












public   void   setParameter  (  String   name  ,  String   filename  ,  InputStream   is  )  throws   IOException  { 
Service  .  logTime  (  "t1"  ,  "setParameter begin (after new fileinput)"  )  ; 
boundary  (  )  ; 
writeName  (  name  )  ; 
write  (  "; filename=\""  )  ; 
write  (  filename  )  ; 
write  (  '"'  )  ; 
newline  (  )  ; 
write  (  "Content-Type: "  )  ; 
Service  .  logTime  (  "t1"  ,  "pre guessContentTypeFromName"  )  ; 
String   type  =  URLConnection  .  guessContentTypeFromName  (  filename  )  ; 
if  (  type  ==  null  )  type  =  "application/octet-stream"  ; 
writeln  (  type  )  ; 
Service  .  logTime  (  "t1"  ,  "post guessContentTypeFromName"  )  ; 
newline  (  )  ; 
pipe  (  is  ,  os  )  ; 
newline  (  )  ; 
} 










public   void   setParameter  (  String   name  ,  File   file  )  throws   IOException  { 
Service  .  logTime  (  "t1"  ,  "pre set file"  )  ; 
setParameter  (  name  ,  file  .  getPath  (  )  ,  new   FileInputStream  (  file  )  )  ; 
Service  .  logTime  (  "t1"  ,  "post set file"  )  ; 
} 













public   void   setParameter  (  String   name  ,  Object   object  )  throws   IOException  { 
if  (  object   instanceof   File  )  { 
setParameter  (  name  ,  (  File  )  object  )  ; 
}  else  { 
setParameter  (  name  ,  object  .  toString  (  )  )  ; 
} 
} 










public   void   setParameters  (  Map   parameters  )  throws   IOException  { 
if  (  parameters  ==  null  )  return  ; 
for  (  Iterator   i  =  parameters  .  entrySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  i  .  next  (  )  ; 
setParameter  (  entry  .  getKey  (  )  .  toString  (  )  ,  entry  .  getValue  (  )  )  ; 
} 
} 











public   void   setParameters  (  Object  [  ]  parameters  )  throws   IOException  { 
if  (  parameters  ==  null  )  return  ; 
for  (  int   i  =  0  ;  i  <  parameters  .  length  -  1  ;  i  +=  2  )  { 
setParameter  (  parameters  [  i  ]  .  toString  (  )  ,  parameters  [  i  +  1  ]  )  ; 
} 
} 








public   InputStream   post  (  )  { 
try  { 
boundary  (  )  ; 
writeln  (  "--"  )  ; 
os  .  close  (  )  ; 
return   connection  .  getInputStream  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   null  ; 
} 












public   InputStream   post  (  Map   parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 












public   InputStream   post  (  Object  [  ]  parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 















public   InputStream   post  (  Map  <  String  ,  String  >  cookies  ,  Map   parameters  )  throws   IOException  { 
setCookies  (  cookies  )  ; 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 















public   InputStream   post  (  String  [  ]  cookies  ,  Object  [  ]  parameters  )  throws   IOException  { 
setCookies  (  cookies  )  ; 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 












public   InputStream   post  (  String   name  ,  Object   value  )  throws   IOException  { 
setParameter  (  name  ,  value  )  ; 
return   post  (  )  ; 
} 
















public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  )  throws   IOException  { 
setParameter  (  name1  ,  value1  )  ; 
return   post  (  name2  ,  value2  )  ; 
} 




















public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  )  throws   IOException  { 
setParameter  (  name1  ,  value1  )  ; 
return   post  (  name2  ,  value2  ,  name3  ,  value3  )  ; 
} 
























public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  ,  String   name4  ,  Object   value4  )  throws   IOException  { 
setParameter  (  name1  ,  value1  )  ; 
return   post  (  name2  ,  value2  ,  name3  ,  value3  ,  name4  ,  value4  )  ; 
} 











public   InputStream   post  (  URL   url  ,  Map   parameters  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  parameters  )  ; 
} 











public   InputStream   post  (  URL   url  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  parameters  )  ; 
} 














public   InputStream   post  (  URL   url  ,  Map  <  String  ,  String  >  cookies  ,  Map   parameters  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 














public   InputStream   post  (  URL   url  ,  String  [  ]  cookies  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 












public   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  name1  ,  value1  )  ; 
} 
















public   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  )  ; 
} 




















public   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  ,  name3  ,  value3  )  ; 
} 
























public   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  ,  String   name4  ,  Object   value4  )  throws   IOException  { 
return   new   HTTPRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  ,  name3  ,  value3  ,  name4  ,  value4  )  ; 
} 

public   byte  [  ]  getBinary  (  )  { 
error  =  null  ; 
String   contentType  =  connection  .  getContentType  (  )  ; 
int   contentLength  =  connection  .  getContentLength  (  )  ; 
LOG  .  info  (  "contentType "  +  contentType  +  " contentLength "  +  contentLength  )  ; 
InputStream   raw  ; 
byte  [  ]  data  =  null  ; 
try  { 
raw  =  connection  .  getInputStream  (  )  ; 
InputStream   in  =  new   BufferedInputStream  (  raw  )  ; 
data  =  new   byte  [  contentLength  ]  ; 
int   bytesRead  =  0  ; 
int   offset  =  0  ; 
while  (  offset  <  contentLength  )  { 
bytesRead  =  in  .  read  (  data  ,  offset  ,  data  .  length  -  offset  )  ; 
if  (  bytesRead  ==  -  1  )  break  ; 
offset  +=  bytesRead  ; 
} 
in  .  close  (  )  ; 
if  (  offset  !=  contentLength  )  { 
throw   new   IOException  (  "Only read "  +  offset  +  " bytes; Expected "  +  contentLength  +  " bytes"  )  ; 
} 
}  catch  (  IOException   e1  )  { 
Service  .  logException  (  e1  )  ; 
error  =  e1  .  getMessage  (  )  ; 
} 
return   data  ; 
} 

public   String   getString  (  )  { 
byte  [  ]  b  =  getBinary  (  )  ; 
if  (  b  !=  null  )  return   new   String  (  b  )  ; 
return   null  ; 
} 

public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
org  .  apache  .  log4j  .  BasicConfigurator  .  configure  (  )  ; 
Logger  .  getRootLogger  (  )  .  setLevel  (  Level  .  DEBUG  )  ; 
HTTPRequest   http  =  new   HTTPRequest  (  "http://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/"  )  ; 
String   s  =  http  .  getString  (  )  ; 
LOG  .  info  (  s  )  ; 
String   language  =  "en"  ; 
String   toSpeak  =  "hello"  ; 
URI   uri  =  new   URI  (  "http"  ,  null  ,  "translate.google.com"  ,  80  ,  "/translate_tts"  ,  "tl="  +  language  +  "&q="  +  toSpeak  ,  null  )  ; 
URL   url  =  uri  .  toURL  (  )  ; 
HttpURLConnection  .  setFollowRedirects  (  true  )  ; 
HttpURLConnection   connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
System  .  out  .  println  (  "Response code = "  +  connection  .  getResponseCode  (  )  )  ; 
String   header  =  connection  .  getHeaderField  (  "location"  )  ; 
if  (  header  !=  null  )  System  .  out  .  println  (  "Redirected to "  +  header  )  ; 
HTTPRequest   request  =  new   HTTPRequest  (  uri  .  toURL  (  )  )  ; 
request  .  getBinary  (  )  ; 
} 

public   boolean   hasError  (  )  { 
return   error  !=  null  ; 
} 

public   String   getError  (  )  { 
return   error  ; 
} 
} 

