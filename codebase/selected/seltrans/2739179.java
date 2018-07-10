package   de  .  powerstaff  .  business  .  dao  .  generic  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  StringBufferInputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Random  ; 













public   class   ClientHttpRequest  { 

private   HttpURLConnection   connection  ; 

private   OutputStream   os  =  null  ; 

private   Map   cookies  =  new   HashMap  (  )  ; 

protected   void   connect  (  )  throws   IOException  { 
if  (  os  ==  null  )  { 
os  =  connection  .  getOutputStream  (  )  ; 
} 
} 

protected   void   write  (  char   c  )  throws   IOException  { 
connect  (  )  ; 
os  .  write  (  c  )  ; 
} 

protected   void   write  (  String   s  )  throws   IOException  { 
connect  (  )  ; 
os  .  write  (  s  .  getBytes  (  )  )  ; 
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

private   static   Random   random  =  new   Random  (  )  ; 

protected   static   String   randomString  (  )  { 
return   Long  .  toString  (  random  .  nextLong  (  )  ,  36  )  ; 
} 

private   String   boundary  =  "---------------------------"  +  randomString  (  )  +  randomString  (  )  +  randomString  (  )  ; 

private   void   boundary  (  )  throws   IOException  { 
write  (  "--"  )  ; 
write  (  boundary  )  ; 
} 









public   ClientHttpRequest  (  HttpURLConnection   connection  )  throws   IOException  { 
this  .  connection  =  connection  ; 
connection  .  setDoOutput  (  true  )  ; 
connection  .  setRequestProperty  (  "Content-Type"  ,  "multipart/form-data; boundary="  +  boundary  )  ; 
} 








public   ClientHttpRequest  (  URL   url  )  throws   IOException  { 
this  (  (  HttpURLConnection  )  url  .  openConnection  (  )  )  ; 
} 








public   ClientHttpRequest  (  String   urlString  )  throws   IOException  { 
this  (  new   URL  (  urlString  )  )  ; 
} 










public   void   setCookie  (  String   name  ,  String   value  )  throws   IOException  { 
cookies  .  put  (  name  ,  value  )  ; 
} 








public   void   setCookies  (  Map   cookies  )  throws   IOException  { 
if  (  cookies  ==  null  )  { 
return  ; 
} 
this  .  cookies  .  putAll  (  cookies  )  ; 
} 









public   void   setCookies  (  String  [  ]  cookies  )  throws   IOException  { 
if  (  cookies  ==  null  )  { 
return  ; 
} 
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

private   static   void   pipe  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  500000  ]  ; 
int   nread  ; 
int   total  =  0  ; 
synchronized  (  in  )  { 
while  (  (  nread  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  >=  0  )  { 
out  .  write  (  buf  ,  0  ,  nread  )  ; 
total  +=  nread  ; 
} 
} 
out  .  flush  (  )  ; 
} 












public   void   setParameter  (  String   name  ,  String   filename  ,  InputStream   is  )  throws   IOException  { 
boundary  (  )  ; 
writeName  (  name  )  ; 
write  (  "; filename=\""  )  ; 
write  (  filename  )  ; 
write  (  '"'  )  ; 
newline  (  )  ; 
write  (  "Content-Type: "  )  ; 
String   type  =  URLConnection  .  guessContentTypeFromName  (  filename  )  ; 
if  (  type  ==  null  )  { 
type  =  "application/octet-stream"  ; 
} 
writeln  (  type  )  ; 
newline  (  )  ; 
pipe  (  is  ,  os  )  ; 
newline  (  )  ; 
} 










public   void   setParameter  (  String   name  ,  File   file  )  throws   IOException  { 
setParameter  (  name  ,  file  .  getPath  (  )  ,  new   FileInputStream  (  file  )  )  ; 
} 

public   void   setParameterAsFile  (  String   name  ,  String   value  )  throws   IOException  { 
setParameter  (  name  ,  name  ,  new   StringBufferInputStream  (  value  )  )  ; 
} 













public   void   setParameter  (  String   name  ,  Object   object  )  throws   IOException  { 
if  (  object   instanceof   File  )  { 
setParameter  (  name  ,  (  File  )  object  )  ; 
}  else  { 
setParameter  (  name  ,  object  .  toString  (  )  )  ; 
} 
} 










public   void   setParameters  (  Map   parameters  )  throws   IOException  { 
if  (  parameters  ==  null  )  { 
return  ; 
} 
for  (  Iterator   i  =  parameters  .  entrySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  i  .  next  (  )  ; 
setParameter  (  entry  .  getKey  (  )  .  toString  (  )  ,  entry  .  getValue  (  )  )  ; 
} 
} 











public   void   setParameters  (  Object  [  ]  parameters  )  throws   IOException  { 
if  (  parameters  ==  null  )  { 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  parameters  .  length  -  1  ;  i  +=  2  )  { 
setParameter  (  parameters  [  i  ]  .  toString  (  )  ,  parameters  [  i  +  1  ]  )  ; 
} 
} 








public   InputStream   post  (  )  throws   IOException  { 
boundary  (  )  ; 
writeln  (  "--"  )  ; 
os  .  close  (  )  ; 
return   connection  .  getInputStream  (  )  ; 
} 












public   InputStream   post  (  Map   parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 












public   InputStream   post  (  Object  [  ]  parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
return   post  (  )  ; 
} 















public   InputStream   post  (  Map   aCookies  ,  Map   aParameters  )  throws   IOException  { 
setCookies  (  aCookies  )  ; 
setParameters  (  aParameters  )  ; 
return   post  (  )  ; 
} 















public   InputStream   post  (  String  [  ]  aCookies  ,  Object  [  ]  aParameters  )  throws   IOException  { 
setCookies  (  aCookies  )  ; 
setParameters  (  aParameters  )  ; 
return   post  (  )  ; 
} 












public   InputStream   post  (  String   name  ,  Object   value  )  throws   IOException  { 
setParameter  (  name  ,  value  )  ; 
return   post  (  )  ; 
} 

public   InputStream   postAsFile  (  String   name  ,  String   value  )  throws   IOException  { 
setParameterAsFile  (  name  ,  value  )  ; 
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











public   static   InputStream   post  (  URL   url  ,  Map   parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  parameters  )  ; 
} 











public   static   InputStream   post  (  URL   url  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  parameters  )  ; 
} 














public   static   InputStream   post  (  URL   url  ,  Map   cookies  ,  Map   parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 














public   static   InputStream   post  (  URL   url  ,  String  [  ]  cookies  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 

public   static   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  name1  ,  value1  )  ; 
} 

public   static   InputStream   postAsFile  (  URL   url  ,  String   name1  ,  String   value1  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  postAsFile  (  name1  ,  value1  )  ; 
} 

public   static   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  )  ; 
} 




















public   static   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  ,  name3  ,  value3  )  ; 
} 
























public   static   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  ,  String   name4  ,  Object   value4  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  name1  ,  value1  ,  name2  ,  value2  ,  name3  ,  value3  ,  name4  ,  value4  )  ; 
} 
} 

