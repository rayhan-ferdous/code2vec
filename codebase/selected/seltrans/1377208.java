package   es  .  unav  .  informesgoogleanalytics  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  Proxy  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  ProxySelector  ; 
import   java  .  net  .  URI  ; 
import   java  .  util  .  *  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  *  ; 






public   class   Utils  { 




public   static   void   initProxies  (  )  throws   Exception  { 
try  { 
System  .  setProperty  (  "java.net.useSystemProxies"  ,  "true"  )  ; 
String   hostname  =  new   String  (  )  ; 
String   port  =  new   String  (  )  ; 
String   url  =  "http://www.yahoo.com/"  ; 
boolean   proxyFound  =  false  ; 
List  <  Proxy  >  l  =  ProxySelector  .  getDefault  (  )  .  select  (  new   URI  (  url  )  )  ; 
for  (  Iterator  <  Proxy  >  iter  =  l  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Proxy   proxy  =  iter  .  next  (  )  ; 
InetSocketAddress   addr  =  (  InetSocketAddress  )  proxy  .  address  (  )  ; 
if  (  addr  ==  null  )  { 
}  else  { 
hostname  =  addr  .  getHostName  (  )  ; 
port  =  String  .  valueOf  (  addr  .  getPort  (  )  )  ; 
proxyFound  =  true  ; 
} 
} 
if  (  proxyFound  )  { 
Properties   systemProperties  =  System  .  getProperties  (  )  ; 
systemProperties  .  setProperty  (  "http.proxyHost"  ,  hostname  )  ; 
systemProperties  .  setProperty  (  "http.proxyPort"  ,  port  )  ; 
systemProperties  .  setProperty  (  "https.proxyHost"  ,  hostname  )  ; 
systemProperties  .  setProperty  (  "https.proxyPort"  ,  port  )  ; 
} 
}  catch  (  Exception   e  )  { 
throw   new   Exception  (  "Couldn't set proxy"  ,  e  )  ; 
} 
} 








public   static   int   getPosInIterator  (  Iterator  <  ?  >  i  ,  Object   o  )  { 
boolean   found  =  false  ; 
int   pos  =  0  ; 
while  (  !  found  &&  i  .  hasNext  (  )  )  { 
Object   oc  =  i  .  next  (  )  ; 
if  (  oc  ==  o  )  { 
found  =  true  ; 
}  else  { 
pos  ++  ; 
} 
} 
if  (  !  found  )  { 
return  -  1  ; 
} 
return   pos  ; 
} 








public   static   int   getPosInEnumeration  (  Enumeration  <  ?  >  e  ,  Object   o  )  { 
boolean   found  =  false  ; 
int   pos  =  0  ; 
while  (  !  found  &&  e  .  hasMoreElements  (  )  )  { 
Object   oc  =  e  .  nextElement  (  )  ; 
if  (  oc  ==  o  )  { 
found  =  true  ; 
}  else  { 
pos  ++  ; 
} 
} 
if  (  !  found  )  { 
return  -  1  ; 
} 
return   pos  ; 
} 









public   static   Object   getObjectAtIteratorPos  (  Iterator  <  ?  >  i  ,  int   pos  )  throws   ArrayIndexOutOfBoundsException  { 
int   currPos  =  0  ; 
Object   oc  ; 
while  (  i  .  hasNext  (  )  )  { 
oc  =  i  .  next  (  )  ; 
if  (  currPos  ==  pos  )  { 
return   oc  ; 
}  else  { 
currPos  ++  ; 
} 
} 
throw   new   ArrayIndexOutOfBoundsException  (  )  ; 
} 






public   static   String   getExtension  (  java  .  io  .  File   f  )  { 
if  (  f  ==  null  )  { 
return   null  ; 
} 
String   s  =  f  .  getName  (  )  ; 
return   getExtension  (  s  )  ; 
} 






public   static   String   getExtension  (  String   s  )  { 
String   ext  =  null  ; 
int   i  =  s  .  lastIndexOf  (  '.'  )  ; 
if  (  i  >  0  &&  i  <  s  .  length  (  )  -  1  )  { 
ext  =  s  .  substring  (  i  +  1  )  .  toLowerCase  (  )  ; 
} 
return   ext  ; 
} 






public   static   String   getDirectoryFromFilepath  (  String   s  )  { 
String   path  =  null  ; 
int   i  =  s  .  lastIndexOf  (  File  .  separator  )  ; 
if  (  i  >  0  &&  i  <  s  .  length  (  )  -  1  )  { 
path  =  s  .  substring  (  0  ,  i  +  1  )  ; 
} 
return   path  ; 
} 





public   static   String   noNullString  (  String   s  )  { 
if  (  s  ==  null  )  { 
return  ""  ; 
} 
return   s  ; 
} 






public   static   String   getStacktraceAsString  (  Exception   e  )  { 
final   Writer   result  =  new   StringWriter  (  )  ; 
final   PrintWriter   printWriter  =  new   PrintWriter  (  result  )  ; 
e  .  printStackTrace  (  printWriter  )  ; 
return   result  .  toString  (  )  ; 
} 




public   static   String   getDateTimeString  (  )  { 
DateFormat   fDateFormat  =  DateFormat  .  getDateTimeInstance  (  DateFormat  .  LONG  ,  DateFormat  .  LONG  )  ; 
return   fDateFormat  .  format  (  new   Date  (  )  )  ; 
} 




public   static   String   getMD5  (  String   s  )  throws   NoSuchAlgorithmException  { 
MessageDigest   digester  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte  [  ]  bytes  =  s  .  getBytes  (  )  ; 
digester  .  update  (  bytes  )  ; 
return   digester  .  digest  (  )  .  toString  (  )  ; 
} 




public   static   String   getMensajeLocalizado  (  String   clave  )  { 
return   java  .  util  .  ResourceBundle  .  getBundle  (  "es/unav/informesgoogleanalytics/vista/Bundle"  )  .  getString  (  clave  )  ; 
} 
} 

