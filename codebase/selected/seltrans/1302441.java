package   net  .  sf  .  bt747  .  j2se  .  system  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  Authenticator  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  PasswordAuthentication  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  URLEncoder  ; 
import   net  .  sf  .  bt747  .  loc  .  LocationSender  ; 
import   bt747  .  sys  .  Generic  ; 
import   bt747  .  sys  .  interfaces  .  BT747Exception  ; 
import   bt747  .  sys  .  interfaces  .  BT747Hashtable  ; 
import   bt747  .  sys  .  interfaces  .  BT747HttpSender  ; 

public   class   J2SEHttpSenderImpl   implements   BT747HttpSender  { 





private   static   final   String   HTTP_PROTOCOL  =  "http"  ; 





private   static   final   String   DEFAULT_ENCODING  =  "UTF-8"  ; 

public   void   doRequest  (  final   String   hostname  ,  final   int   port  ,  final   String   file  ,  final   String   user  ,  final   String   password  ,  final   BT747Hashtable   data  ,  final   String   encodingOrNull  ,  final   LocationSender   caller  )  { 
Thread   t  =  new   Thread  (  )  { 

public   void   run  (  )  { 
doRequestAsynchronously  (  hostname  ,  port  ,  file  ,  user  ,  password  ,  data  ,  encodingOrNull  ,  caller  )  ; 
super  .  run  (  )  ; 
} 
}  ; 
t  .  start  (  )  ; 
} 























private   void   doRequestAsynchronously  (  String   hostname  ,  int   port  ,  String   file  ,  String   user  ,  String   password  ,  BT747Hashtable   data  ,  String   encodingOrNull  ,  final   LocationSender   caller  )  { 
String   encodedData  =  null  ; 
try  { 
encodedData  =  encodeRequestData  (  data  ,  encodingOrNull  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
caller  .  notifyFatalFailure  (  "Unsupported encoding."  )  ; 
return  ; 
} 
URL   url  =  null  ; 
try  { 
url  =  createURL  (  hostname  ,  port  ,  file  ,  encodedData  )  ; 
}  catch  (  MalformedURLException   e  )  { 
caller  .  notifyFatalFailure  (  "Could not prepare URL from parameters."  )  ; 
return  ; 
} 
Generic  .  debug  (  "LocSrv - connect to "  +  hostname  +  ", sending "  +  encodedData  )  ; 
HttpURLConnection   conn  =  null  ; 
try  { 
conn  =  sendData  (  url  ,  user  ,  password  )  ; 
}  catch  (  IOException   e  )  { 
caller  .  notifyConnectionFailure  (  "Connect to target server failed."  )  ; 
return  ; 
}  catch  (  IllegalArgumentException   e  )  { 
Generic  .  debug  (  "Connection error"  ,  e  )  ; 
caller  .  notifyConnectionFailure  (  "Connect to target server failed."  )  ; 
return  ; 
} 
try  { 
readResult  (  conn  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
caller  .  notifyFatalFailure  (  "URL points to non existing location on target server."  )  ; 
return  ; 
}  catch  (  IOException   e  )  { 
caller  .  notifyConnectionFailure  (  "Problem reading the target server response"  )  ; 
return  ; 
} 
try  { 
if  (  conn  .  getResponseCode  (  )  ==  HttpURLConnection  .  HTTP_OK  )  { 
caller  .  notifySuccess  (  )  ; 
}  else  { 
caller  .  notifyConnectionFailure  (  "Did not receive Code 200"  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
caller  .  notifyConnectionFailure  (  "Problem reading response code from response"  )  ; 
} 
} 
















private   URL   createURL  (  String   hostname  ,  int   port  ,  String   file  ,  String   encodedData  )  throws   MalformedURLException  { 
if  (  (  file  .  length  (  )  >  0  )  &&  (  file  .  charAt  (  0  )  !=  '/'  )  )  { 
file  =  "/"  +  file  ; 
} 
file  +=  "?"  +  encodedData  ; 
return   new   URL  (  HTTP_PROTOCOL  ,  hostname  ,  port  ,  file  )  ; 
} 
















private   HttpURLConnection   sendData  (  URL   url  ,  String   user  ,  String   password  )  throws   IOException  ,  IllegalArgumentException  { 
String   tmpAuthUserName  =  ""  ; 
if  (  user  !=  null  )  { 
tmpAuthUserName  =  user  ; 
} 
final   String   anAuthUserName  =  tmpAuthUserName  ; 
String   tmpAuthPasswd  =  ""  ; 
if  (  password  !=  null  )  { 
tmpAuthPasswd  =  password  ; 
} 
final   String   anAuthPasswd  =  tmpAuthPasswd  ; 
Authenticator  .  setDefault  (  new   Authenticator  (  )  { 

protected   PasswordAuthentication   getPasswordAuthentication  (  )  { 
return   new   PasswordAuthentication  (  anAuthUserName  ,  anAuthPasswd  .  toCharArray  (  )  )  ; 
} 
}  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setRequestMethod  (  "GET"  )  ; 
conn  .  setReadTimeout  (  1000  )  ; 
conn  .  connect  (  )  ; 
return   conn  ; 
} 










private   String   readResult  (  URLConnection   conn  )  throws   IOException  { 
BufferedReader   rd  =  null  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
try  { 
rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  +  '\n'  )  ; 
} 
}  finally  { 
if  (  rd  !=  null  )  { 
try  { 
rd  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   sb  .  toString  (  )  ; 
} 
















private   String   encodeRequestData  (  BT747Hashtable   data  ,  String   encodingOrNull  )  throws   UnsupportedEncodingException  { 
String   encoding  =  DEFAULT_ENCODING  ; 
if  (  encodingOrNull  !=  null  )  { 
encoding  =  encodingOrNull  ; 
} 
StringBuffer   encodedData  =  new   StringBuffer  (  )  ; 
BT747Hashtable   it  =  data  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
String   key  =  (  String  )  it  .  nextKey  (  )  ; 
String   value  =  (  String  )  data  .  get  (  key  )  ; 
if  (  encodedData  .  length  (  )  >  0  )  { 
encodedData  .  append  (  '&'  )  ; 
} 
encodedData  .  append  (  URLEncoder  .  encode  (  key  ,  encoding  )  )  ; 
encodedData  .  append  (  '='  )  ; 
encodedData  .  append  (  URLEncoder  .  encode  (  value  ,  encoding  )  )  ; 
} 
return   encodedData  .  toString  (  )  ; 
} 
} 

