package   org  .  jhotdraw  .  net  ; 

import   edu  .  umd  .  cs  .  findbugs  .  annotations  .  Nullable  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  io  .  IOException  ; 
import   java  .  security  .  InvalidParameterException  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  Random  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  util  .  Iterator  ; 

public   class   ClientHttpRequest  { 

URLConnection   connection  ; 

@  Nullable 
OutputStream   os  =  null  ; 

Map  <  String  ,  String  >  cookies  =  new   HashMap  <  String  ,  String  >  (  )  ; 

String   rawCookies  =  ""  ; 

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
os  .  write  (  s  .  getBytes  (  "UTF-8"  )  )  ; 
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

String   boundary  =  "---------------------------"  +  randomString  (  )  +  randomString  (  )  +  randomString  (  )  ; 

private   void   boundary  (  )  throws   IOException  { 
write  (  "--"  )  ; 
write  (  boundary  )  ; 
} 







public   ClientHttpRequest  (  URLConnection   connection  )  throws   IOException  { 
this  .  connection  =  connection  ; 
connection  .  setDoOutput  (  true  )  ; 
connection  .  setDoInput  (  true  )  ; 
connection  .  setRequestProperty  (  "Content-Type"  ,  "multipart/form-data; boundary="  +  boundary  )  ; 
} 







public   ClientHttpRequest  (  URL   url  )  throws   IOException  { 
this  (  url  .  openConnection  (  )  )  ; 
} 







public   ClientHttpRequest  (  String   urlString  )  throws   IOException  { 
this  (  new   URL  (  urlString  )  )  ; 
} 

private   void   postCookies  (  )  { 
StringBuffer   cookieList  =  new   StringBuffer  (  rawCookies  )  ; 
for  (  Iterator   i  =  cookies  .  entrySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  (  i  .  next  (  )  )  ; 
cookieList  .  append  (  entry  .  getKey  (  )  .  toString  (  )  +  "="  +  entry  .  getValue  (  )  )  ; 
if  (  i  .  hasNext  (  )  )  { 
cookieList  .  append  (  "; "  )  ; 
} 
} 
if  (  cookieList  .  length  (  )  >  0  )  { 
connection  .  setRequestProperty  (  "Cookie"  ,  cookieList  .  toString  (  )  )  ; 
} 
} 






public   void   setCookies  (  String   rawCookies  )  throws   IOException  { 
this  .  rawCookies  =  (  rawCookies  ==  null  )  ?  ""  :  rawCookies  ; 
cookies  .  clear  (  )  ; 
} 







public   void   setCookie  (  String   name  ,  String   value  )  throws   IOException  { 
cookies  .  put  (  name  ,  value  )  ; 
} 






public   void   setCookies  (  Map  <  String  ,  String  >  cookies  )  throws   IOException  { 
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
if  (  name  ==  null  )  { 
throw   new   InvalidParameterException  (  "setParameter("  +  name  +  ","  +  value  +  ") name must not be null"  )  ; 
} 
if  (  value  ==  null  )  { 
throw   new   InvalidParameterException  (  "setParameter("  +  name  +  ","  +  value  +  ") value must not be null"  )  ; 
} 
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
buf  =  null  ; 
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
FileInputStream   in  =  new   FileInputStream  (  file  )  ; 
try  { 
setParameter  (  name  ,  file  .  getPath  (  )  ,  in  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 







public   void   setParameter  (  String   name  ,  Object   object  )  throws   IOException  { 
if  (  object   instanceof   File  )  { 
setParameter  (  name  ,  (  File  )  object  )  ; 
}  else  { 
setParameter  (  name  ,  object  .  toString  (  )  )  ; 
} 
} 






public   void   setParameters  (  Map   parameters  )  throws   IOException  { 
if  (  parameters  !=  null  )  { 
for  (  Iterator   i  =  parameters  .  entrySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  i  .  next  (  )  ; 
setParameter  (  entry  .  getKey  (  )  .  toString  (  )  ,  entry  .  getValue  (  )  )  ; 
} 
} 
} 






public   void   setParameters  (  Object  [  ]  parameters  )  throws   IOException  { 
if  (  parameters  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  parameters  .  length  -  1  ;  i  +=  2  )  { 
setParameter  (  parameters  [  i  ]  .  toString  (  )  ,  parameters  [  i  +  1  ]  )  ; 
} 
} 
} 






private   InputStream   doPost  (  )  throws   IOException  { 
boundary  (  )  ; 
writeln  (  "--"  )  ; 
os  .  close  (  )  ; 
return   connection  .  getInputStream  (  )  ; 
} 






public   InputStream   post  (  )  throws   IOException  { 
postCookies  (  )  ; 
return   doPost  (  )  ; 
} 








public   InputStream   post  (  Map   parameters  )  throws   IOException  { 
postCookies  (  )  ; 
setParameters  (  parameters  )  ; 
return   doPost  (  )  ; 
} 








public   InputStream   post  (  Object  [  ]  parameters  )  throws   IOException  { 
postCookies  (  )  ; 
setParameters  (  parameters  )  ; 
return   doPost  (  )  ; 
} 










public   InputStream   post  (  Map  <  String  ,  String  >  cookies  ,  Map   parameters  )  throws   IOException  { 
setCookies  (  cookies  )  ; 
postCookies  (  )  ; 
setParameters  (  parameters  )  ; 
return   doPost  (  )  ; 
} 










public   InputStream   post  (  String   raw_cookies  ,  Map   parameters  )  throws   IOException  { 
setCookies  (  raw_cookies  )  ; 
postCookies  (  )  ; 
setParameters  (  parameters  )  ; 
return   doPost  (  )  ; 
} 










public   InputStream   post  (  String  [  ]  cookies  ,  Object  [  ]  parameters  )  throws   IOException  { 
setCookies  (  cookies  )  ; 
postCookies  (  )  ; 
setParameters  (  parameters  )  ; 
return   doPost  (  )  ; 
} 









public   InputStream   post  (  String   name  ,  Object   value  )  throws   IOException  { 
postCookies  (  )  ; 
setParameter  (  name  ,  value  )  ; 
return   doPost  (  )  ; 
} 











public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  )  throws   IOException  { 
postCookies  (  )  ; 
setParameter  (  name1  ,  value1  )  ; 
setParameter  (  name2  ,  value2  )  ; 
return   doPost  (  )  ; 
} 













public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  )  throws   IOException  { 
postCookies  (  )  ; 
setParameter  (  name1  ,  value1  )  ; 
setParameter  (  name2  ,  value2  )  ; 
setParameter  (  name3  ,  value3  )  ; 
return   doPost  (  )  ; 
} 















public   InputStream   post  (  String   name1  ,  Object   value1  ,  String   name2  ,  Object   value2  ,  String   name3  ,  Object   value3  ,  String   name4  ,  Object   value4  )  throws   IOException  { 
postCookies  (  )  ; 
setParameter  (  name1  ,  value1  )  ; 
setParameter  (  name2  ,  value2  )  ; 
setParameter  (  name3  ,  value3  )  ; 
setParameter  (  name4  ,  value4  )  ; 
return   doPost  (  )  ; 
} 








public   static   InputStream   post  (  URL   url  ,  Map   parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  parameters  )  ; 
} 








public   static   InputStream   post  (  URL   url  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  parameters  )  ; 
} 










public   static   InputStream   post  (  URL   url  ,  Map  <  String  ,  String  >  cookies  ,  Map   parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 











public   static   InputStream   post  (  URL   url  ,  String  [  ]  cookies  ,  Object  [  ]  parameters  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  cookies  ,  parameters  )  ; 
} 










public   static   InputStream   post  (  URL   url  ,  String   name1  ,  Object   value1  )  throws   IOException  { 
return   new   ClientHttpRequest  (  url  )  .  post  (  name1  ,  value1  )  ; 
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

