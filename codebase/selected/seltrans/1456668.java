package   org  .  apache  .  http  .  examples  .  nio  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InterruptedIOException  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   org  .  apache  .  http  .  HttpEntityEnclosingRequest  ; 
import   org  .  apache  .  http  .  HttpException  ; 
import   org  .  apache  .  http  .  HttpRequest  ; 
import   org  .  apache  .  http  .  HttpResponse  ; 
import   org  .  apache  .  http  .  HttpStatus  ; 
import   org  .  apache  .  http  .  impl  .  DefaultConnectionReuseStrategy  ; 
import   org  .  apache  .  http  .  impl  .  DefaultHttpResponseFactory  ; 
import   org  .  apache  .  http  .  impl  .  nio  .  DefaultServerIOEventDispatch  ; 
import   org  .  apache  .  http  .  impl  .  nio  .  reactor  .  DefaultListeningIOReactor  ; 
import   org  .  apache  .  http  .  nio  .  ContentDecoder  ; 
import   org  .  apache  .  http  .  nio  .  ContentDecoderChannel  ; 
import   org  .  apache  .  http  .  nio  .  FileContentDecoder  ; 
import   org  .  apache  .  http  .  nio  .  IOControl  ; 
import   org  .  apache  .  http  .  nio  .  NHttpConnection  ; 
import   org  .  apache  .  http  .  nio  .  entity  .  ConsumingNHttpEntity  ; 
import   org  .  apache  .  http  .  nio  .  entity  .  ConsumingNHttpEntityTemplate  ; 
import   org  .  apache  .  http  .  nio  .  entity  .  ContentListener  ; 
import   org  .  apache  .  http  .  nio  .  entity  .  NFileEntity  ; 
import   org  .  apache  .  http  .  nio  .  entity  .  NStringEntity  ; 
import   org  .  apache  .  http  .  nio  .  protocol  .  AsyncNHttpServiceHandler  ; 
import   org  .  apache  .  http  .  nio  .  protocol  .  EventListener  ; 
import   org  .  apache  .  http  .  nio  .  protocol  .  NHttpRequestHandler  ; 
import   org  .  apache  .  http  .  nio  .  protocol  .  NHttpRequestHandlerResolver  ; 
import   org  .  apache  .  http  .  nio  .  protocol  .  SimpleNHttpRequestHandler  ; 
import   org  .  apache  .  http  .  nio  .  reactor  .  IOEventDispatch  ; 
import   org  .  apache  .  http  .  nio  .  reactor  .  ListeningIOReactor  ; 
import   org  .  apache  .  http  .  params  .  BasicHttpParams  ; 
import   org  .  apache  .  http  .  params  .  CoreConnectionPNames  ; 
import   org  .  apache  .  http  .  params  .  CoreProtocolPNames  ; 
import   org  .  apache  .  http  .  params  .  HttpParams  ; 
import   org  .  apache  .  http  .  protocol  .  BasicHttpProcessor  ; 
import   org  .  apache  .  http  .  protocol  .  HttpContext  ; 
import   org  .  apache  .  http  .  protocol  .  ResponseConnControl  ; 
import   org  .  apache  .  http  .  protocol  .  ResponseContent  ; 
import   org  .  apache  .  http  .  protocol  .  ResponseDate  ; 
import   org  .  apache  .  http  .  protocol  .  ResponseServer  ; 











public   class   NHttpFileServer  { 

public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
if  (  args  .  length  <  1  )  { 
System  .  err  .  println  (  "Please specify document root directory"  )  ; 
System  .  exit  (  1  )  ; 
} 
boolean   useFileChannels  =  true  ; 
if  (  args  .  length  >=  2  )  { 
String   s  =  args  [  1  ]  ; 
if  (  s  .  equalsIgnoreCase  (  "disableFileChannels"  )  )  { 
useFileChannels  =  false  ; 
} 
} 
HttpParams   params  =  new   BasicHttpParams  (  )  ; 
params  .  setIntParameter  (  CoreConnectionPNames  .  SO_TIMEOUT  ,  20000  )  .  setIntParameter  (  CoreConnectionPNames  .  SOCKET_BUFFER_SIZE  ,  8  *  1024  )  .  setBooleanParameter  (  CoreConnectionPNames  .  STALE_CONNECTION_CHECK  ,  false  )  .  setBooleanParameter  (  CoreConnectionPNames  .  TCP_NODELAY  ,  true  )  .  setParameter  (  CoreProtocolPNames  .  ORIGIN_SERVER  ,  "HttpComponents/1.1"  )  ; 
BasicHttpProcessor   httpproc  =  new   BasicHttpProcessor  (  )  ; 
httpproc  .  addInterceptor  (  new   ResponseDate  (  )  )  ; 
httpproc  .  addInterceptor  (  new   ResponseServer  (  )  )  ; 
httpproc  .  addInterceptor  (  new   ResponseContent  (  )  )  ; 
httpproc  .  addInterceptor  (  new   ResponseConnControl  (  )  )  ; 
AsyncNHttpServiceHandler   handler  =  new   AsyncNHttpServiceHandler  (  httpproc  ,  new   DefaultHttpResponseFactory  (  )  ,  new   DefaultConnectionReuseStrategy  (  )  ,  params  )  ; 
final   HttpFileHandler   filehandler  =  new   HttpFileHandler  (  args  [  0  ]  ,  useFileChannels  )  ; 
NHttpRequestHandlerResolver   resolver  =  new   NHttpRequestHandlerResolver  (  )  { 

public   NHttpRequestHandler   lookup  (  String   requestURI  )  { 
return   filehandler  ; 
} 
}  ; 
handler  .  setHandlerResolver  (  resolver  )  ; 
handler  .  setEventListener  (  new   EventLogger  (  )  )  ; 
IOEventDispatch   ioEventDispatch  =  new   DefaultServerIOEventDispatch  (  handler  ,  params  )  ; 
ListeningIOReactor   ioReactor  =  new   DefaultListeningIOReactor  (  2  ,  params  )  ; 
try  { 
ioReactor  .  listen  (  new   InetSocketAddress  (  8080  )  )  ; 
ioReactor  .  execute  (  ioEventDispatch  )  ; 
}  catch  (  InterruptedIOException   ex  )  { 
System  .  err  .  println  (  "Interrupted"  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "I/O error: "  +  e  .  getMessage  (  )  )  ; 
} 
System  .  out  .  println  (  "Shutdown"  )  ; 
} 

static   class   HttpFileHandler   extends   SimpleNHttpRequestHandler  { 

private   final   String   docRoot  ; 

private   final   boolean   useFileChannels  ; 

public   HttpFileHandler  (  final   String   docRoot  ,  boolean   useFileChannels  )  { 
this  .  docRoot  =  docRoot  ; 
this  .  useFileChannels  =  useFileChannels  ; 
} 

public   ConsumingNHttpEntity   entityRequest  (  final   HttpEntityEnclosingRequest   request  ,  final   HttpContext   context  )  throws   HttpException  ,  IOException  { 
return   new   ConsumingNHttpEntityTemplate  (  request  .  getEntity  (  )  ,  new   FileWriteListener  (  useFileChannels  )  )  ; 
} 

@  Override 
public   void   handle  (  final   HttpRequest   request  ,  final   HttpResponse   response  ,  final   HttpContext   context  )  throws   HttpException  ,  IOException  { 
String   target  =  request  .  getRequestLine  (  )  .  getUri  (  )  ; 
final   File   file  =  new   File  (  this  .  docRoot  ,  URLDecoder  .  decode  (  target  ,  "UTF-8"  )  )  ; 
if  (  !  file  .  exists  (  )  )  { 
response  .  setStatusCode  (  HttpStatus  .  SC_NOT_FOUND  )  ; 
NStringEntity   entity  =  new   NStringEntity  (  "<html><body><h1>File"  +  file  .  getPath  (  )  +  " not found</h1></body></html>"  ,  "UTF-8"  )  ; 
entity  .  setContentType  (  "text/html; charset=UTF-8"  )  ; 
response  .  setEntity  (  entity  )  ; 
}  else   if  (  !  file  .  canRead  (  )  ||  file  .  isDirectory  (  )  )  { 
response  .  setStatusCode  (  HttpStatus  .  SC_FORBIDDEN  )  ; 
NStringEntity   entity  =  new   NStringEntity  (  "<html><body><h1>Access denied</h1></body></html>"  ,  "UTF-8"  )  ; 
entity  .  setContentType  (  "text/html; charset=UTF-8"  )  ; 
response  .  setEntity  (  entity  )  ; 
}  else  { 
response  .  setStatusCode  (  HttpStatus  .  SC_OK  )  ; 
NFileEntity   entity  =  new   NFileEntity  (  file  ,  "text/html"  ,  useFileChannels  )  ; 
response  .  setEntity  (  entity  )  ; 
} 
} 
} 

static   class   FileWriteListener   implements   ContentListener  { 

private   final   File   file  ; 

private   final   FileInputStream   inputFile  ; 

private   final   FileChannel   fileChannel  ; 

private   final   boolean   useFileChannels  ; 

private   long   idx  =  0  ; 

public   FileWriteListener  (  boolean   useFileChannels  )  throws   IOException  { 
this  .  file  =  File  .  createTempFile  (  "tmp"  ,  ".tmp"  ,  null  )  ; 
this  .  inputFile  =  new   FileInputStream  (  file  )  ; 
this  .  fileChannel  =  inputFile  .  getChannel  (  )  ; 
this  .  useFileChannels  =  useFileChannels  ; 
} 

public   void   contentAvailable  (  ContentDecoder   decoder  ,  IOControl   ioctrl  )  throws   IOException  { 
long   transferred  ; 
if  (  useFileChannels  &&  decoder   instanceof   FileContentDecoder  )  { 
transferred  =  (  (  FileContentDecoder  )  decoder  )  .  transfer  (  fileChannel  ,  idx  ,  Long  .  MAX_VALUE  )  ; 
}  else  { 
transferred  =  fileChannel  .  transferFrom  (  new   ContentDecoderChannel  (  decoder  )  ,  idx  ,  Long  .  MAX_VALUE  )  ; 
} 
if  (  transferred  >  0  )  idx  +=  transferred  ; 
} 

public   void   finished  (  )  { 
try  { 
inputFile  .  close  (  )  ; 
}  catch  (  IOException   ignored  )  { 
} 
try  { 
fileChannel  .  close  (  )  ; 
}  catch  (  IOException   ignored  )  { 
} 
} 
} 

static   class   EventLogger   implements   EventListener  { 

public   void   connectionOpen  (  final   NHttpConnection   conn  )  { 
System  .  out  .  println  (  "Connection open: "  +  conn  )  ; 
} 

public   void   connectionTimeout  (  final   NHttpConnection   conn  )  { 
System  .  out  .  println  (  "Connection timed out: "  +  conn  )  ; 
} 

public   void   connectionClosed  (  final   NHttpConnection   conn  )  { 
System  .  out  .  println  (  "Connection closed: "  +  conn  )  ; 
} 

public   void   fatalIOException  (  final   IOException   ex  ,  final   NHttpConnection   conn  )  { 
System  .  err  .  println  (  "I/O error: "  +  ex  .  getMessage  (  )  )  ; 
} 

public   void   fatalProtocolException  (  final   HttpException   ex  ,  final   NHttpConnection   conn  )  { 
System  .  err  .  println  (  "HTTP error: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 
} 

