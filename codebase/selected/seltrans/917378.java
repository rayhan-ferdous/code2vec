package   com  .  volantis  .  mcs  .  servlet  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  lang  .  IllegalStateException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  TimeZone  ; 
import   javax  .  servlet  .  ServletOutputStream  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   com  .  volantis  .  synergetics  .  log  .  LogDispatcher  ; 
import   com  .  volantis  .  mcs  .  localization  .  LocalizationFactory  ; 

public   class   HttpStringResponse   implements   HttpServletResponse  { 


private   static   String   mark  =  "(c) Volantis Systems Ltd 2000."  ; 

private   static   final   String   RFC1123_DATE_SPEC  =  "EEE, dd MMM yyyy HH:mm:ss z"  ; 




private   static   final   LogDispatcher   logger  =  LocalizationFactory  .  createLogger  (  HttpStringResponse  .  class  )  ; 

private   List   cookies  ; 

private   Map   headers  ; 

private   String   contentType  ; 

private   String   encoding  ; 

private   PrintWriter   writer  ; 

private   StringWriter   stringWriter  ; 

private   ByteArrayOutputStream   outputStream  ; 

private   ServletOutputStream   servletStream  ; 

private   int   status  ; 

private   String   statusMessage  ; 


public   HttpStringResponse  (  )  { 
cookies  =  new   ArrayList  (  )  ; 
headers  =  new   HashMap  (  )  ; 
encoding  =  "us-ascii"  ; 
} 





public   void   addCookie  (  javax  .  servlet  .  http  .  Cookie   cookie  )  { 
cookies  .  add  (  cookie  )  ; 
} 




public   void   addDateHeader  (  String   str  ,  long   param  )  { 
addHeader  (  str  ,  asDateHeaderValue  (  param  )  )  ; 
} 





public   void   addHeader  (  String   name  ,  String   value  )  { 
synchronized  (  headers  )  { 
String   key  =  name  .  toUpperCase  (  )  ; 
ArrayList   values  =  (  ArrayList  )  headers  .  get  (  key  )  ; 
if  (  values  ==  null  )  { 
values  =  new   ArrayList  (  )  ; 
headers  .  put  (  key  ,  values  )  ; 
} 
values  .  add  (  value  )  ; 
} 
} 




public   void   addIntHeader  (  String   str  ,  int   param  )  { 
addHeader  (  str  ,  Integer  .  toString  (  param  )  )  ; 
} 





public   boolean   containsHeader  (  String   str  )  { 
return   headers  .  containsKey  (  str  )  ; 
} 











public   String   encodeRedirectURL  (  String   url  )  { 
return   url  ; 
} 




public   String   encodeRedirectUrl  (  String   str  )  { 
return   encodeRedirectURL  (  str  )  ; 
} 









public   String   encodeURL  (  String   url  )  { 
return   url  ; 
} 




public   String   encodeUrl  (  String   str  )  { 
return   encodeURL  (  str  )  ; 
} 




public   void   flushBuffer  (  )  throws   java  .  io  .  IOException  { 
} 




public   int   getBufferSize  (  )  { 
return   0  ; 
} 




public   String   getCharacterEncoding  (  )  { 
return   encoding  ; 
} 

public   void   setCharacterEncoding  (  String   encoding  )  { 
this  .  encoding  =  encoding  ; 
if  (  contentType  ==  null  ||  contentType  .  length  (  )  ==  0  )  { 
contentType  =  "text/plain"  ; 
} 
setHeader  (  "Content-type"  ,  contentType  +  "; charset="  +  encoding  )  ; 
} 




public   java  .  util  .  Locale   getLocale  (  )  { 
return   null  ; 
} 









public   ServletOutputStream   getOutputStream  (  )  throws   IOException  { 
if  (  writer  !=  null  )  { 
throw   new   IllegalStateException  (  "Tried to create output stream; writer already exists"  )  ; 
} 
if  (  servletStream  ==  null  )  { 
outputStream  =  new   ByteArrayOutputStream  (  )  ; 
servletStream  =  new   ServletStringOutputStream  (  outputStream  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Create ServletOutputStream for outputStream "  +  outputStream  )  ; 
} 
} 
return   servletStream  ; 
} 



























public   java  .  io  .  PrintWriter   getWriter  (  )  throws   java  .  io  .  IOException  { 
if  (  servletStream  !=  null  )  { 
throw   new   IllegalStateException  (  "Tried to create writer; output stream already exists"  )  ; 
} 
if  (  writer  ==  null  )  { 
stringWriter  =  new   StringWriter  (  )  ; 
writer  =  new   PrintWriter  (  stringWriter  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Created writer for output stream "  +  outputStream  )  ; 
} 
} 
return   writer  ; 
} 





public   boolean   isCommitted  (  )  { 
return   false  ; 
} 





public   void   reset  (  )  { 
} 







public   void   resetBuffer  (  )  { 
} 










public   void   sendError  (  int   sc  )  throws   IOException  { 
sendError  (  sc  ,  ""  )  ; 
} 










public   void   sendError  (  int   sc  ,  String   msg  )  throws   IOException  { 
setStatus  (  sc  )  ; 
statusMessage  =  msg  ; 
writer  =  null  ; 
servletStream  =  null  ; 
setContentType  (  "text/html"  )  ; 
getWriter  (  )  .  println  (  "<html><head><title>"  +  msg  +  "</title></head>"  +  "<body>"  +  msg  +  "</body></html>"  )  ; 
} 







public   void   sendRedirect  (  String   location  )  throws   IOException  { 
setStatus  (  HttpServletResponse  .  SC_MOVED_PERMANENTLY  )  ; 
setHeader  (  "Location"  ,  location  )  ; 
} 






public   void   setBufferSize  (  int   size  )  { 
} 

public   void   setContentLength  (  int   param  )  { 
} 










public   void   setContentType  (  String   type  )  { 
String  [  ]  typeAndEncoding  =  parseContentTypeHeader  (  type  )  ; 
contentType  =  typeAndEncoding  [  0  ]  ; 
if  (  typeAndEncoding  [  1  ]  !=  null  )  { 
encoding  =  typeAndEncoding  [  1  ]  ; 
}  else  { 
encoding  =  "ISO-8859-4"  ; 
} 
setHeader  (  "Content-type"  ,  contentType  +  "; charset="  +  encoding  )  ; 
} 

public   void   setDateHeader  (  String   str  ,  long   param  )  { 
addDateHeader  (  str  ,  param  )  ; 
} 

public   void   setHeader  (  String   str  ,  String   str1  )  { 
addHeader  (  str  ,  str1  )  ; 
} 

public   void   setIntHeader  (  String   str  ,  int   param  )  { 
addIntHeader  (  str  ,  param  )  ; 
} 

public   void   setLocale  (  java  .  util  .  Locale   locale  )  { 
} 

public   void   setStatus  (  int   param  )  { 
status  =  param  ; 
} 

public   void   setStatus  (  int   param  ,  String   str  )  { 
status  =  param  ; 
statusMessage  =  str  ; 
} 






private   String   asDateHeaderValue  (  long   date  )  { 
Date   value  =  new   Date  (  date  )  ; 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  RFC1123_DATE_SPEC  )  ; 
formatter  .  setTimeZone  (  TimeZone  .  getTimeZone  (  "Greenwich Mean Time"  )  )  ; 
return   formatter  .  format  (  value  )  ; 
} 





private   String  [  ]  parseContentTypeHeader  (  String   header  )  { 
String  [  ]  result  =  new   String  [  ]  {  "text/plain"  ,  null  }  ; 
StringTokenizer   st  =  new   StringTokenizer  (  header  ,  ";="  )  ; 
result  [  0  ]  =  st  .  nextToken  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   parameter  =  st  .  nextToken  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  { 
String   value  =  stripQuotes  (  st  .  nextToken  (  )  )  ; 
if  (  parameter  .  trim  (  )  .  equalsIgnoreCase  (  "charset"  )  )  { 
result  [  1  ]  =  value  ; 
} 
} 
} 
return   result  ; 
} 

private   String   stripQuotes  (  String   value  )  { 
if  (  value  .  startsWith  (  "'"  )  ||  value  .  startsWith  (  "\""  )  )  { 
value  =  value  .  substring  (  1  )  ; 
} 
if  (  value  .  endsWith  (  "'"  )  ||  value  .  endsWith  (  "\""  )  )  { 
value  =  value  .  substring  (  0  ,  value  .  length  (  )  -  1  )  ; 
} 
return   value  ; 
} 




public   String   getContents  (  )  throws   UnsupportedEncodingException  { 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Getting contents, encoding "  +  encoding  )  ; 
} 
if  (  servletStream  !=  null  )  { 
return   outputStream  .  toString  (  encoding  )  ; 
}  else  { 
return   stringWriter  .  toString  (  )  ; 
} 
} 




public   String   getContentType  (  )  { 
return   contentType  ; 
} 
} 

