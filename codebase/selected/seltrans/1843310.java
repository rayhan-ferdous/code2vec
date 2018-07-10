package   com  .  wangyu001  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  Authenticator  ; 
import   java  .  net  .  PasswordAuthentication  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 

public   final   class   HtmlReader  { 

private   static   final   Log   log  =  LogFactory  .  getLog  (  HtmlReader  .  class  )  ; 

private   static   final   boolean   DEBUG  =  log  .  isDebugEnabled  (  )  ; 

private   static   final   boolean   isProxyNeeded  =  false  ; 

public   static   final   String   DEFAULT_CHARSET  =  "UTF-8"  ; 

private   static   final   Pattern   P_HEAD  ; 

private   static   final   Pattern   P_CHARSET  ; 

private   static   final   Pattern   P_TITLE  ; 

private   static   final   Pattern   P_DESCRIPTION  ; 

private   static   final   Pattern   P_KEYWORDS  ; 

private   static   final   Pattern   P_CONTENT  ; 

private   static   final   Pattern   P_CHARSET_TAIL  ; 

private   static   final   Pattern   P_TAIL  ; 

static  { 
if  (  isProxyNeeded  )  { 
initProxy  (  )  ; 
} 
P_HEAD  =  Pattern  .  compile  (  "<head[\\s>]+.+</head>"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
String   s  =  "<meta [^<]*charset\\s*=\\s*([\\w\\-]+)\"?\\s*[^<]*/?>"  ; 
P_CHARSET  =  Pattern  .  compile  (  s  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
P_TITLE  =  Pattern  .  compile  (  "<title>.+</title>"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
s  =  "<meta\\s+[^<>]*name\\s*=\\s*((\"\\s*description\\s*\")|(\'\\s*description\\s*\'))[^<>]*/?>"  ; 
P_DESCRIPTION  =  Pattern  .  compile  (  s  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
s  =  "<meta\\s+[^<>]*name\\s*=\\s*((\"\\s*keywords\\s*\")|(\'\\s*keywords\\s*\'))[^<>]*/?>"  ; 
P_KEYWORDS  =  Pattern  .  compile  (  s  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
s  =  "\\s+content\\s*=\\s*((\"[^\"]+\")|(\'[^\']+\')|([^<>/]+))"  ; 
P_CONTENT  =  Pattern  .  compile  (  s  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
P_CHARSET_TAIL  =  Pattern  .  compile  (  "[=\"\'/]+"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
P_TAIL  =  Pattern  .  compile  (  "[=\"\']+"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
} 







public   static   String  [  ]  parse  (  String   url  )  { 
String   head  =  parseHead  (  url  )  ; 
return   new   String  [  ]  {  findTitle  (  head  )  ,  findDescription  (  head  )  ,  findKeywords  (  head  )  }  ; 
} 







public   static   String   parseHead  (  String   url  )  { 
byte  [  ]  head  =  findHead  (  url  )  ; 
String   charset  =  findCharset  (  head  )  ; 
String   result  =  null  ; 
try  { 
result  =  new   String  (  head  ,  charset  )  .  replaceAll  (  "\\s+"  ,  " "  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  url  ,  e  )  ; 
} 
if  (  DEBUG  )  { 
log  .  debug  (  "result="  +  result  +  ", charset="  +  charset  +  ", url="  +  url  )  ; 
} 
return   result  ; 
} 







public   static   String   parseTitle  (  String   url  )  { 
return   findTitle  (  parseHead  (  url  )  )  ; 
} 







public   static   String   parseDescription  (  String   url  )  { 
return   findDescription  (  parseHead  (  url  )  )  ; 
} 







public   static   String   parseKeywords  (  String   url  )  { 
return   findKeywords  (  parseHead  (  url  )  )  ; 
} 








private   static   String   findCharset  (  byte  [  ]  head  )  { 
String   headString  =  new   String  (  head  )  .  replaceAll  (  "\\s+"  ,  " "  )  ; 
Matcher   m  =  P_CHARSET  .  matcher  (  headString  )  ; 
if  (  m  .  find  (  )  )  { 
return   m  .  group  (  1  )  ; 
} 
return   DEFAULT_CHARSET  ; 
} 







private   static   String   findTitle  (  String   head  )  { 
String   title  =  null  ; 
try  { 
Matcher   m  =  P_TITLE  .  matcher  (  head  .  replaceAll  (  "\\s+"  ,  " "  )  )  ; 
if  (  m  .  find  (  )  )  { 
title  =  m  .  group  (  )  ; 
title  =  title  .  substring  (  7  ,  title  .  length  (  )  -  8  )  .  trim  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
return   title  ; 
} 







private   static   String   findDescription  (  String   head  )  { 
String   description  =  null  ; 
try  { 
Matcher   m  =  P_DESCRIPTION  .  matcher  (  head  )  ; 
if  (  m  .  find  (  )  )  { 
description  =  findContent  (  m  .  group  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
return   description  ; 
} 







private   static   String   findKeywords  (  String   head  )  { 
String   keywords  =  null  ; 
try  { 
Matcher   m  =  P_KEYWORDS  .  matcher  (  head  )  ; 
if  (  m  .  find  (  )  )  { 
keywords  =  findContent  (  m  .  group  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
return   keywords  ; 
} 







private   static   String   findContent  (  String   src  )  { 
String   content  =  null  ; 
try  { 
Matcher   m  =  P_CONTENT  .  matcher  (  src  )  ; 
if  (  m  .  find  (  )  )  { 
content  =  m  .  group  (  )  .  trim  (  )  .  substring  (  7  )  ; 
} 
m  =  P_TAIL  .  matcher  (  content  )  ; 
content  =  m  .  replaceAll  (  ""  )  .  trim  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
return   content  ; 
} 







private   static   String   appendSlash  (  String   url  )  { 
if  (  url  .  lastIndexOf  (  '/'  )  ==  (  url  .  indexOf  (  '/'  )  +  1  )  )  { 
url  =  url  +  "/"  ; 
} 
return   url  ; 
} 

public   static   boolean   exist  (  String   file  )  { 
boolean   result  =  true  ; 
InputStream   in  =  null  ; 
try  { 
in  =  new   URL  (  file  )  .  openStream  (  )  ; 
}  catch  (  Exception   e  )  { 
result  =  false  ; 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
}  finally  { 
try  { 
if  (  null  !=  in  )  in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   result  ; 
} 







public   static   void   readAsFile  (  String   fileName  ,  String   url  )  { 
BufferedInputStream   in  =  null  ; 
BufferedOutputStream   out  =  null  ; 
URLConnection   conn  =  null  ; 
try  { 
conn  =  new   URL  (  url  )  .  openConnection  (  )  ; 
conn  .  setDoInput  (  true  )  ; 
in  =  new   BufferedInputStream  (  conn  .  getInputStream  (  )  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  fileName  )  )  ; 
int   b  ; 
while  (  (  b  =  in  .  read  (  )  )  !=  -  1  )  { 
out  .  write  (  b  )  ; 
} 
}  catch  (  Exception   ex  )  { 
log  .  error  (  ex  .  getMessage  (  )  ,  ex  )  ; 
}  finally  { 
if  (  null  !=  in  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
if  (  null  !=  out  )  { 
try  { 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 







public   static   byte  [  ]  findHead  (  String   url  )  { 
byte  [  ]  result  =  new   byte  [  0  ]  ; 
InputStream   in  =  null  ; 
try  { 
in  =  new   URL  (  appendSlash  (  url  )  )  .  openStream  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   len  =  -  1  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  !=  -  1  )  { 
byte  [  ]  temp  =  new   byte  [  result  .  length  +  len  ]  ; 
System  .  arraycopy  (  result  ,  0  ,  temp  ,  0  ,  result  .  length  )  ; 
System  .  arraycopy  (  buffer  ,  0  ,  temp  ,  result  .  length  ,  len  )  ; 
result  =  temp  ; 
if  (  DEBUG  )  { 
log  .  debug  (  String  .  format  (  "len=%d, result.length=%d"  ,  len  ,  result  .  length  )  )  ; 
} 
if  (  result  .  length  >  4096  )  { 
break  ; 
} 
if  (  result  .  length  >  1024  )  { 
String   s  =  new   String  (  result  )  .  replaceAll  (  "\\s+"  ,  " "  )  ; 
Matcher   m  =  P_HEAD  .  matcher  (  s  )  ; 
if  (  m  .  find  (  )  )  { 
break  ; 
} 
} 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  e  .  getMessage  (  )  ,  e  )  ; 
}  finally  { 
try  { 
if  (  null  !=  in  )  in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   result  ; 
} 

private   static   void   initProxy  (  )  { 
Authenticator  .  setDefault  (  new   Authenticator  (  )  { 

protected   PasswordAuthentication   getPasswordAuthentication  (  )  { 
return   new   PasswordAuthentication  (  "guest"  ,  "nanjing"  .  toCharArray  (  )  )  ; 
} 
}  )  ; 
System  .  setProperty  (  "http.proxySet"  ,  "true"  )  ; 
System  .  setProperty  (  "http.proxyType"  ,  "4"  )  ; 
System  .  setProperty  (  "http.proxyHost"  ,  "10.170.253.153"  )  ; 
System  .  setProperty  (  "http.proxyPort"  ,  "808"  )  ; 
System  .  setProperty  (  "https.proxySet"  ,  "true"  )  ; 
System  .  setProperty  (  "https.proxyType"  ,  "4"  )  ; 
System  .  setProperty  (  "https.proxyHost"  ,  "10.170.253.153"  )  ; 
System  .  setProperty  (  "https.proxyPort"  ,  "808"  )  ; 
} 

public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
String   url  =  "http://club.it.sohu.com/l-it-0-0-900-0.html"  ; 
url  =  "http://ym.163.com"  ; 
url  =  "http://www.sogou.com"  ; 
System  .  out  .  println  (  findCharset  (  findHead  (  url  )  )  )  ; 
} 
} 

