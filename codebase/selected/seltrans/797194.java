package   org  .  aha  .  mf4j  ; 

import   static   org  .  aha  .  mf4j  .  FlickrUrls  .  REST_URL  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  SortedMap  ; 
import   java  .  util  .  TreeMap  ; 
import   org  .  aha  .  mf4j  .  reflection  .  FlickrMethod  ; 
import   org  .  aha  .  mf4j  .  response  .  FlickrObject  ; 
import   org  .  aha  .  mf4j  .  response  .  FlickrResponseException  ; 
import   org  .  aha  .  mf4j  .  response  .  FlickrResponseParser  ; 
import   org  .  aha  .  mf4j  .  util  .  MD5  ; 







public   final   class   Request  { 

private   final   String   m_url  ; 

private   final   String   m_secret  ; 

private   final   SortedMap  <  String  ,  String  >  m_args  =  new   TreeMap  <  String  ,  String  >  (  )  ; 

private   FlickrMethod   m_meta  =  null  ; 

private   String   m_xml  =  null  ; 











Request  (  )  { 
m_url  =  REST_URL  ; 
m_secret  =  null  ; 
} 















Request  (  String   secret  ,  String   token  )  { 
this  (  REST_URL  ,  secret  ,  token  )  ; 
} 










Request  (  String   url  ,  String   secret  ,  String   token  )  { 
if  (  url  ==  null  )  { 
throw   new   NullPointerException  (  "url"  )  ; 
} 
if  (  secret  ==  null  )  { 
throw   new   NullPointerException  (  "secret"  )  ; 
} 
m_url  =  url  ; 
m_secret  =  secret  ; 
if  (  token  !=  null  )  m_args  .  put  (  "auth_token"  ,  token  )  ; 
} 







void   setMeta  (  FlickrMethod   meta  )  { 
if  (  meta  ==  null  )  { 
throw   new   NullPointerException  (  "meta"  )  ; 
} 
if  (  m_meta  !=  null  )  { 
throw   new   IllegalStateException  (  "meta sat"  )  ; 
} 
m_meta  =  meta  ; 
} 













public   Request   setArg  (  String   name  ,  int   value  )  { 
return   setArg  (  name  ,  Integer  .  toString  (  value  )  )  ; 
} 













public   Request   setArg  (  String   name  ,  String   value  )  { 
if  (  name  ==  null  )  { 
throw   new   NullPointerException  (  "name"  )  ; 
} 
if  (  value  ==  null  )  { 
throw   new   NullPointerException  (  "value"  )  ; 
} 
if  (  FlickrMethodArguments  .  isSessionArg  (  name  )  )  { 
throw   new   IllegalArgumentException  (  name  +  " parameter is set by FlickrSession"  )  ; 
} 
if  (  m_meta  !=  null  &&  !  m_meta  .  hasArgument  (  name  )  )  { 
throw   new   IllegalArgumentException  (  "method : "  +  m_meta  .  getName  (  )  +  " does not have argument : "  +  name  )  ; 
} 
m_args  .  put  (  name  ,  value  )  ; 
return   this  ; 
} 









Request   setApiKey  (  String   value  )  { 
return   setSessionArg  (  "api_key"  ,  value  )  ; 
} 









Request   setMethod  (  String   value  )  { 
return   setSessionArg  (  "method"  ,  value  )  ; 
} 

private   Request   setSessionArg  (  String   name  ,  String   value  )  { 
if  (  name  ==  null  )  { 
throw   new   NullPointerException  (  "name"  )  ; 
} 
if  (  value  ==  null  )  { 
throw   new   NullPointerException  (  "value"  )  ; 
} 
if  (  m_args  .  containsKey  (  name  )  )  { 
throw   new   IllegalArgumentException  (  "has "  +  name  +  " arg"  )  ; 
} 
m_args  .  put  (  name  ,  value  )  ; 
return   this  ; 
} 

private   String   m_sig  =  null  ; 







private   String   getSignature  (  )  { 
if  (  m_sig  ==  null  &&  m_secret  !=  null  )  { 
StringBuilder   sb  =  new   StringBuilder  (  m_secret  )  ; 
Iterator  <  Map  .  Entry  <  String  ,  String  >  >  i  =  m_args  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
Map  .  Entry  <  String  ,  String  >  curr  =  i  .  next  (  )  ; 
sb  .  append  (  curr  .  getKey  (  )  )  .  append  (  curr  .  getValue  (  )  )  ; 
} 
String   sigString  =  sb  .  toString  (  )  ; 
m_sig  =  MD5  .  compute  (  sigString  )  ; 
} 
return   m_sig  ; 
} 

private   String   getRequestData  (  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
Iterator  <  Map  .  Entry  <  String  ,  String  >  >  args  =  m_args  .  entrySet  (  )  .  iterator  (  )  ; 
boolean   first  =  true  ; 
while  (  args  .  hasNext  (  )  )  { 
if  (  !  first  )  sb  .  append  (  '&'  )  ; 
first  =  false  ; 
Map  .  Entry  <  String  ,  String  >  curr  =  args  .  next  (  )  ; 
try  { 
sb  .  append  (  URLEncoder  .  encode  (  curr  .  getKey  (  )  ,  "UTF-8"  )  )  .  append  (  '='  )  .  append  (  URLEncoder  .  encode  (  curr  .  getValue  (  )  ,  "UTF-8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   uex  )  { 
IllegalStateException   isx  =  new   IllegalStateException  (  )  ; 
isx  .  initCause  (  uex  )  ; 
throw   isx  ; 
} 
} 
String   sig  =  getSignature  (  )  ; 
if  (  sig  !=  null  )  { 
if  (  first  )  sb  .  append  (  '?'  )  ;  else   sb  .  append  (  '&'  )  ; 
sb  .  append  (  "api_sig"  )  .  append  (  '='  )  .  append  (  sig  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 







private   void   validate  (  )  { 
if  (  m_meta  !=  null  )  { 
int   n  =  m_meta  .  getMandatoryArgCount  (  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
String   name  =  m_meta  .  getMandatoryArgName  (  i  )  ; 
if  (  !  m_args  .  containsKey  (  name  )  )  { 
throw   new   IllegalStateException  (  "missing mandatory attribute : "  +  name  )  ; 
} 
} 
} 
} 









public   FlickrObject   perform  (  )  throws   FlickrException  { 
return   perform  (  false  )  ; 
} 














public   FlickrObject   perform  (  boolean   chkResponse  )  throws   FlickrException  { 
validate  (  )  ; 
String   data  =  getRequestData  (  )  ; 
OutputStream   os  =  null  ; 
InputStream   is  =  null  ; 
try  { 
URL   url  =  null  ; 
try  { 
url  =  new   URL  (  m_url  )  ; 
}  catch  (  MalformedURLException   mux  )  { 
IllegalStateException   iax  =  new   IllegalStateException  (  )  ; 
iax  .  initCause  (  mux  )  ; 
throw   iax  ; 
} 
HttpURLConnection   con  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
con  .  setDoOutput  (  true  )  ; 
con  .  setRequestMethod  (  "POST"  )  ; 
os  =  con  .  getOutputStream  (  )  ; 
OutputStreamWriter   osw  =  new   OutputStreamWriter  (  os  )  ; 
osw  .  write  (  data  )  ; 
osw  .  flush  (  )  ; 
is  =  con  .  getInputStream  (  )  ; 
return   processRespons  (  is  ,  chkResponse  )  ; 
}  catch  (  FlickrException   fx  )  { 
throw   fx  ; 
}  catch  (  IOException   iox  )  { 
throw   new   FlickrException  (  iox  )  ; 
}  finally  { 
if  (  os  !=  null  )  try  { 
os  .  close  (  )  ; 
}  catch  (  IOException   _  )  { 
} 
if  (  is  !=  null  )  try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   _  )  { 
} 
} 
} 







public   String   getXml  (  )  { 
return   m_xml  ; 
} 










public   boolean   saveXml  (  File   f  )  throws   IOException  { 
if  (  f  ==  null  )  { 
throw   new   NullPointerException  (  "f"  )  ; 
} 
String   xml  =  getXml  (  )  ; 
if  (  xml  ==  null  )  return   false  ; 
FileOutputStream   fos  =  null  ; 
try  { 
fos  =  new   FileOutputStream  (  f  )  ; 
OutputStreamWriter   osw  =  new   OutputStreamWriter  (  fos  ,  "UTF-8"  )  ; 
osw  .  write  (  xml  )  ; 
osw  .  flush  (  )  ; 
return   true  ; 
}  finally  { 
if  (  fos  !=  null  )  try  { 
fos  .  close  (  )  ; 
}  catch  (  IOException   _  )  { 
} 
} 
} 















private   FlickrObject   processRespons  (  InputStream   is  ,  boolean   chkResponse  )  throws   FlickrException  { 
BufferedReader   br  =  null  ; 
try  { 
br  =  new   BufferedReader  (  new   InputStreamReader  (  is  ,  "UTF-8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   uex  )  { 
IllegalStateException   iax  =  new   IllegalStateException  (  )  ; 
iax  .  initCause  (  uex  )  ; 
throw   iax  ; 
} 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
try  { 
String   line  =  br  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
sb  .  append  (  line  )  ; 
line  =  br  .  readLine  (  )  ; 
} 
}  catch  (  IOException   iox  )  { 
throw   new   FlickrException  (  iox  )  ; 
} 
String   xml  =  sb  .  toString  (  )  ; 
FlickrObject   retVal  =  FlickrResponseParser  .  parse  (  xml  ,  chkResponse  )  ; 
m_xml  =  xml  ; 
return   retVal  ; 
} 
} 

