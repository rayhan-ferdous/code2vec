package   com  .  xiledsystems  .  AlternateJavaBridgelib  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   org  .  json  .  JSONException  ; 
import   android  .  app  .  Service  ; 
import   android  .  text  .  TextUtils  ; 
import   android  .  util  .  Log  ; 
import   com  .  google  .  devtools  .  simple  .  runtime  .  components  .  Component  ; 
import   com  .  google  .  devtools  .  simple  .  runtime  .  components  .  android  .  util  .  AsynchUtil  ; 
import   com  .  google  .  devtools  .  simple  .  runtime  .  components  .  android  .  util  .  FileUtil  ; 
import   com  .  google  .  devtools  .  simple  .  runtime  .  components  .  util  .  ErrorMessages  ; 
import   com  .  google  .  devtools  .  simple  .  runtime  .  events  .  EventDispatcher  ; 
import   com  .  xiledsystems  .  AlternateJavaBridgelib  .  AndroidServiceComponent  ; 
import   com  .  xiledsystems  .  AlternateJavaBridgelib  .  JsonUtil2  ; 
import   com  .  xiledsystems  .  AlternateJavaBridgelib  .  MediaUtilsvc  ; 
import   com  .  xiledsystems  .  AlternateJavaBridgelib  .  SvcComponentContainer  ; 

public   class   Websvc   extends   AndroidServiceComponent   implements   Component  { 

private   static   final   String   LOG_TAG  =  "Websvc"  ; 

private   static   final   Map  <  String  ,  Character  >  htmlCharacterEntities  ; 

static  { 
htmlCharacterEntities  =  new   HashMap  <  String  ,  Character  >  (  )  ; 
htmlCharacterEntities  .  put  (  "quot"  ,  '"'  )  ; 
htmlCharacterEntities  .  put  (  "amp"  ,  '&'  )  ; 
htmlCharacterEntities  .  put  (  "apos"  ,  '\''  )  ; 
htmlCharacterEntities  .  put  (  "lt"  ,  '<'  )  ; 
htmlCharacterEntities  .  put  (  "gt"  ,  '>'  )  ; 
} 

private   static   final   Map  <  String  ,  String  >  mimeTypeToExtension  ; 

static  { 
mimeTypeToExtension  =  new   HashMap  <  String  ,  String  >  (  )  ; 
mimeTypeToExtension  .  put  (  "application/pdf"  ,  "pdf"  )  ; 
mimeTypeToExtension  .  put  (  "application/zip"  ,  "zip"  )  ; 
mimeTypeToExtension  .  put  (  "audio/mpeg"  ,  "mpeg"  )  ; 
mimeTypeToExtension  .  put  (  "audio/mp3"  ,  "mp3"  )  ; 
mimeTypeToExtension  .  put  (  "audio/mp4"  ,  "mp4"  )  ; 
mimeTypeToExtension  .  put  (  "image/gif"  ,  "gif"  )  ; 
mimeTypeToExtension  .  put  (  "image/jpeg"  ,  "jpg"  )  ; 
mimeTypeToExtension  .  put  (  "image/png"  ,  "png"  )  ; 
mimeTypeToExtension  .  put  (  "image/tiff"  ,  "tiff"  )  ; 
mimeTypeToExtension  .  put  (  "text/plain"  ,  "txt"  )  ; 
mimeTypeToExtension  .  put  (  "text/html"  ,  "html"  )  ; 
mimeTypeToExtension  .  put  (  "text/xml"  ,  "xml"  )  ; 
} 

private   final   Service   service  ; 

private   String   urlString  =  ""  ; 

private   boolean   saveResponse  ; 

private   String   responseFileName  =  ""  ; 






public   Websvc  (  SvcComponentContainer   container  )  { 
super  (  container  .  $formService  (  )  )  ; 
service  =  container  .  $context  (  )  ; 
} 




protected   Websvc  (  )  { 
super  (  null  )  ; 
service  =  null  ; 
} 




public   String   Url  (  )  { 
return   urlString  ; 
} 




public   void   Url  (  String   url  )  { 
urlString  =  url  ; 
} 




public   boolean   SaveResponse  (  )  { 
return   saveResponse  ; 
} 




public   void   SaveResponse  (  boolean   saveResponse  )  { 
this  .  saveResponse  =  saveResponse  ; 
} 






public   String   ResponseFileName  (  )  { 
return   responseFileName  ; 
} 






public   void   ResponseFileName  (  String   responseFileName  )  { 
this  .  responseFileName  =  responseFileName  ; 
} 










public   void   Get  (  )  { 
final   String   urlString  =  this  .  urlString  ; 
final   boolean   saveResponse  =  this  .  saveResponse  ; 
final   String   responseFileName  =  this  .  responseFileName  ; 
AsynchUtil  .  runAsynchronously  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
performRequest  (  urlString  ,  null  ,  null  ,  saveResponse  ,  responseFileName  )  ; 
}  catch  (  FileUtil  .  FileException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "Get"  ,  e  .  getErrorMessageNumber  (  )  )  ; 
}  catch  (  Exception   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "Get"  ,  ErrorMessages  .  ERROR_WEB_UNABLE_TO_GET  ,  urlString  )  ; 
} 
} 
}  )  ; 
} 














public   void   PostText  (  final   String   text  ,  final   String   encoding  )  { 
final   String   urlString  =  this  .  urlString  ; 
final   boolean   saveResponse  =  this  .  saveResponse  ; 
final   String   responseFileName  =  this  .  responseFileName  ; 
AsynchUtil  .  runAsynchronously  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
byte  [  ]  postData  ; 
try  { 
if  (  encoding  ==  null  ||  encoding  .  length  (  )  ==  0  )  { 
postData  =  text  .  getBytes  (  "UTF-8"  )  ; 
}  else  { 
postData  =  text  .  getBytes  (  encoding  )  ; 
} 
}  catch  (  UnsupportedEncodingException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "PostText"  ,  ErrorMessages  .  ERROR_WEB_UNSUPPORTED_ENCODING  ,  encoding  )  ; 
return  ; 
} 
try  { 
performRequest  (  urlString  ,  postData  ,  null  ,  saveResponse  ,  responseFileName  )  ; 
}  catch  (  FileUtil  .  FileException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "PostText"  ,  e  .  getErrorMessageNumber  (  )  )  ; 
}  catch  (  Exception   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "PostText"  ,  ErrorMessages  .  ERROR_WEB_UNABLE_TO_POST  ,  text  ,  urlString  )  ; 
} 
} 
}  )  ; 
} 












public   void   PostFile  (  final   String   path  )  { 
final   String   urlString  =  this  .  urlString  ; 
final   boolean   saveResponse  =  this  .  saveResponse  ; 
final   String   responseFileName  =  this  .  responseFileName  ; 
AsynchUtil  .  runAsynchronously  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
performRequest  (  urlString  ,  null  ,  path  ,  saveResponse  ,  responseFileName  )  ; 
}  catch  (  FileUtil  .  FileException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "PostFile"  ,  e  .  getErrorMessageNumber  (  )  )  ; 
}  catch  (  Exception   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  Websvc  .  this  ,  "PostFile"  ,  ErrorMessages  .  ERROR_WEB_UNABLE_TO_POST_FILE  ,  path  ,  urlString  )  ; 
} 
} 
}  )  ; 
} 











public   void   GotText  (  String   url  ,  int   responseCode  ,  String   responseType  ,  String   responseContent  )  { 
EventDispatcher  .  dispatchEvent  (  this  ,  "GotText"  ,  url  ,  responseCode  ,  responseType  ,  responseContent  )  ; 
} 











public   void   GotFile  (  String   url  ,  int   responseCode  ,  String   responseType  ,  String   fileName  )  { 
EventDispatcher  .  dispatchEvent  (  this  ,  "GotFile"  ,  url  ,  responseCode  ,  responseType  ,  fileName  )  ; 
} 







public   String   UriEncode  (  String   text  )  { 
try  { 
return   URLEncoder  .  encode  (  text  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return  ""  ; 
} 
} 








public   String   JsonTextDecode  (  String   jsonText  )  { 
try  { 
return   decodeJsonText  (  jsonText  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  this  ,  "JsonTextDecode"  ,  ErrorMessages  .  ERROR_WEB_JSON_TEXT_DECODE_FAILED  ,  jsonText  )  ; 
return  ""  ; 
} 
} 

String   decodeJsonText  (  String   jsonText  )  throws   IllegalArgumentException  { 
Object   o  ; 
try  { 
o  =  JsonUtil2  .  getObjectFromJson  (  jsonText  )  ; 
}  catch  (  JSONException   e  )  { 
throw   new   IllegalArgumentException  (  "jsonText is not a legal JSON value"  )  ; 
} 
if  (  o   instanceof   String  )  { 
return  (  String  )  o  ; 
} 
throw   new   IllegalArgumentException  (  "jsonText is not a legal JSON text value"  )  ; 
} 













public   String   HtmlTextDecode  (  String   htmlText  )  { 
try  { 
return   decodeHtmlText  (  htmlText  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
formService  .  dispatchErrorOccurredEvent  (  this  ,  "HtmlTextDecode"  ,  ErrorMessages  .  ERROR_WEB_HTML_TEXT_DECODE_FAILED  ,  htmlText  )  ; 
return  ""  ; 
} 
} 

String   decodeHtmlText  (  String   htmlText  )  throws   IllegalArgumentException  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
int   htmlLength  =  htmlText  .  length  (  )  ; 
for  (  int   i  =  0  ;  i  <  htmlLength  ;  i  ++  )  { 
char   c  =  htmlText  .  charAt  (  i  )  ; 
if  (  c  ==  '&'  )  { 
int   indexOfSemi  =  htmlText  .  indexOf  (  ';'  ,  i  +  1  )  ; 
if  (  indexOfSemi  ==  -  1  )  { 
throw   new   IllegalArgumentException  (  "htmlText contains a & without a following ;"  )  ; 
} 
if  (  htmlText  .  charAt  (  i  +  1  )  ==  '#'  )  { 
int   n  ; 
if  (  htmlText  .  charAt  (  i  +  2  )  ==  'x'  )  { 
String   hhhh  =  htmlText  .  substring  (  i  +  3  ,  indexOfSemi  )  ; 
try  { 
n  =  Integer  .  parseInt  (  hhhh  ,  16  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   IllegalArgumentException  (  "htmlText contains an illegal hex value: "  +  hhhh  )  ; 
} 
}  else  { 
String   nnnn  =  htmlText  .  substring  (  i  +  2  ,  indexOfSemi  )  ; 
try  { 
n  =  Integer  .  parseInt  (  nnnn  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   IllegalArgumentException  (  "htmlText contains an illegal decimal value: "  +  nnnn  )  ; 
} 
} 
sb  .  append  (  (  char  )  n  )  ; 
i  =  indexOfSemi  ; 
}  else  { 
String   entity  =  htmlText  .  substring  (  i  +  1  ,  indexOfSemi  )  ; 
Character   decoded  =  htmlCharacterEntities  .  get  (  entity  )  ; 
if  (  decoded  !=  null  )  { 
sb  .  append  (  decoded  )  ; 
i  =  indexOfSemi  ; 
}  else  { 
throw   new   IllegalArgumentException  (  "htmlText contains an unknown entity: &"  +  entity  +  ";"  )  ; 
} 
} 
}  else  { 
sb  .  append  (  c  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 

private   void   performRequest  (  final   String   urlString  ,  byte  [  ]  postData  ,  String   postFile  ,  boolean   saveResponse  ,  String   responseFileName  )  throws   IOException  { 
HttpURLConnection   connection  =  openConnection  (  urlString  )  ; 
Log  .  i  (  LOG_TAG  ,  "performRequest - connection is "  +  connection  )  ; 
if  (  connection  !=  null  )  { 
try  { 
if  (  postData  !=  null  )  { 
writePostData  (  connection  ,  postData  )  ; 
}  else   if  (  postFile  !=  null  )  { 
writePostFile  (  connection  ,  postFile  )  ; 
} 
final   int   responseCode  =  connection  .  getResponseCode  (  )  ; 
Log  .  i  (  LOG_TAG  ,  "performRequest - responseCode is "  +  responseCode  )  ; 
final   String   responseType  =  (  responseCode  ==  HttpURLConnection  .  HTTP_OK  )  ?  getResponseType  (  connection  )  :  ""  ; 
Log  .  i  (  LOG_TAG  ,  "performRequest - responseType is "  +  responseType  )  ; 
if  (  saveResponse  )  { 
final   String   path  =  (  responseCode  ==  HttpURLConnection  .  HTTP_OK  )  ?  saveResponseContent  (  connection  ,  responseFileName  ,  responseType  )  :  ""  ; 
formService  .  runOnSvcThread  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
GotFile  (  urlString  ,  responseCode  ,  responseType  ,  path  )  ; 
} 
}  )  ; 
}  else  { 
final   String   responseContent  =  (  responseCode  ==  HttpURLConnection  .  HTTP_OK  )  ?  getResponseContent  (  connection  )  :  ""  ; 
formService  .  runOnSvcThread  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
GotText  (  urlString  ,  responseCode  ,  responseType  ,  responseContent  )  ; 
} 
}  )  ; 
} 
}  finally  { 
connection  .  disconnect  (  )  ; 
} 
} 
} 

private   static   HttpURLConnection   openConnection  (  String   urlString  )  throws   MalformedURLException  ,  IOException  ,  ClassCastException  { 
return  (  HttpURLConnection  )  new   URL  (  urlString  )  .  openConnection  (  )  ; 
} 

private   static   void   writePostData  (  HttpURLConnection   connection  ,  byte  [  ]  postData  )  throws   IOException  { 
connection  .  setDoOutput  (  true  )  ; 
connection  .  setChunkedStreamingMode  (  0  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  connection  .  getOutputStream  (  )  )  ; 
try  { 
out  .  write  (  postData  ,  0  ,  postData  .  length  )  ; 
out  .  flush  (  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
} 

private   void   writePostFile  (  HttpURLConnection   connection  ,  String   path  )  throws   IOException  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  MediaUtilsvc  .  openMedia  (  formService  ,  path  )  )  ; 
try  { 
connection  .  setDoOutput  (  true  )  ; 
connection  .  setChunkedStreamingMode  (  0  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  connection  .  getOutputStream  (  )  )  ; 
try  { 
while  (  true  )  { 
int   b  =  in  .  read  (  )  ; 
if  (  b  ==  -  1  )  { 
break  ; 
} 
out  .  write  (  b  )  ; 
} 
out  .  flush  (  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
}  finally  { 
in  .  close  (  )  ; 
} 
} 

private   static   String   getResponseType  (  HttpURLConnection   connection  )  { 
String   responseType  =  connection  .  getContentType  (  )  ; 
return  (  responseType  !=  null  )  ?  responseType  :  ""  ; 
} 

private   static   String   getResponseContent  (  HttpURLConnection   connection  )  throws   IOException  { 
String   encoding  =  connection  .  getContentEncoding  (  )  ; 
if  (  encoding  ==  null  )  { 
encoding  =  "UTF-8"  ; 
} 
InputStreamReader   reader  =  new   InputStreamReader  (  connection  .  getInputStream  (  )  ,  encoding  )  ; 
try  { 
int   contentLength  =  connection  .  getContentLength  (  )  ; 
StringBuilder   sb  =  (  contentLength  !=  -  1  )  ?  new   StringBuilder  (  contentLength  )  :  new   StringBuilder  (  )  ; 
char  [  ]  buf  =  new   char  [  1024  ]  ; 
int   read  ; 
while  (  (  read  =  reader  .  read  (  buf  )  )  !=  -  1  )  { 
sb  .  append  (  buf  ,  0  ,  read  )  ; 
} 
return   sb  .  toString  (  )  ; 
}  finally  { 
reader  .  close  (  )  ; 
} 
} 

private   static   String   saveResponseContent  (  HttpURLConnection   connection  ,  String   responseFileName  ,  String   responseType  )  throws   IOException  { 
File   file  =  createFile  (  responseFileName  ,  responseType  )  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  connection  .  getInputStream  (  )  ,  0x1000  )  ; 
try  { 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  file  )  ,  0x1000  )  ; 
try  { 
while  (  true  )  { 
int   b  =  in  .  read  (  )  ; 
if  (  b  ==  -  1  )  { 
break  ; 
} 
out  .  write  (  b  )  ; 
} 
out  .  flush  (  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
}  finally  { 
in  .  close  (  )  ; 
} 
return   file  .  getAbsolutePath  (  )  ; 
} 

private   static   File   createFile  (  String   fileName  ,  String   responseType  )  throws   IOException  ,  FileUtil  .  FileException  { 
if  (  !  TextUtils  .  isEmpty  (  fileName  )  )  { 
return   FileUtil  .  getExternalFile  (  fileName  )  ; 
} 
int   indexOfSemicolon  =  responseType  .  indexOf  (  ';'  )  ; 
if  (  indexOfSemicolon  !=  -  1  )  { 
responseType  =  responseType  .  substring  (  0  ,  indexOfSemicolon  )  ; 
} 
String   extension  =  mimeTypeToExtension  .  get  (  responseType  )  ; 
if  (  extension  ==  null  )  { 
extension  =  "tmp"  ; 
} 
return   FileUtil  .  getDownloadFile  (  extension  )  ; 
} 
} 

