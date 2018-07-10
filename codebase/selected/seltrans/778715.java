package   com  .  koutra  .  dist  .  proc  .  sink  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  concurrent  .  BlockingDeque  ; 
import   java  .  util  .  concurrent  .  LinkedBlockingDeque  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  sax  .  SAXSource  ; 
import   javax  .  xml  .  transform  .  sax  .  SAXTransformerFactory  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  XMLReader  ; 
import   com  .  koutra  .  dist  .  proc  .  model  .  ContentType  ; 
import   com  .  koutra  .  dist  .  proc  .  model  .  IFaucet  ; 
import   com  .  koutra  .  dist  .  proc  .  model  .  IPipelineItem  ; 
import   com  .  koutra  .  dist  .  proc  .  model  .  XformationException  ; 









public   class   XMLZipMuxSink   extends   AbstractFileOrStreamMuxSink  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  XMLZipMuxSink  .  class  )  ; 

protected   static   class   DequePayload  { 

public   IFaucet   faucet  ; 
} 

protected   BlockingDeque  <  DequePayload  >  deque  ; 

protected   ZipOutputStream   outputStream  ; 




public   XMLZipMuxSink  (  )  { 
} 






public   XMLZipMuxSink  (  String   id  ,  ZipOutputStream   os  )  { 
super  (  id  )  ; 
this  .  deque  =  new   LinkedBlockingDeque  <  DequePayload  >  (  )  ; 
this  .  outputStream  =  os  ; 
} 






public   XMLZipMuxSink  (  String   id  ,  String   path  )  { 
super  (  id  ,  path  )  ; 
this  .  deque  =  new   LinkedBlockingDeque  <  DequePayload  >  (  )  ; 
this  .  outputStream  =  null  ; 
} 







@  Override 
public   boolean   supportsInput  (  ContentType   contentType  )  { 
switch  (  contentType  )  { 
case   XML  : 
return   true  ; 
case   ByteStream  : 
case   CharStream  : 
case   ResultSet  : 
default  : 
return   false  ; 
} 
} 





@  Override 
protected   void   checkFaucetValidity  (  IFaucet   faucet  )  { 
super  .  checkFaucetValidity  (  faucet  )  ; 
if  (  !  faucet  .  supportsOutput  (  ContentType  .  XML  )  )  throw   new   IllegalArgumentException  (  "Faucet '"  +  faucet  .  getId  (  )  +  "' must support the XML content type."  )  ; 
} 




@  Override 
public   void   registerSource  (  Object   source  )  { 
IFaucet   faucet  =  (  IFaucet  )  source  ; 
if  (  logger  .  isTraceEnabled  (  )  )  logger  .  trace  (  "Registering faucet: "  +  faucet  +  " with the mux deque"  )  ; 
DequePayload   payload  =  new   DequePayload  (  )  ; 
payload  .  faucet  =  faucet  ; 
while  (  true  )  { 
try  { 
deque  .  putLast  (  payload  )  ; 
break  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 




@  Override 
public   String   dumpPipeline  (  )  { 
DequePayload   payload  =  deque  .  peekFirst  (  )  ; 
return   getClass  (  )  .  getName  (  )  +  ": "  +  (  payload  ==  null  ?  "null"  :  payload  .  faucet  )  ; 
} 




@  Override 
public   void   dispose  (  )  { 
switch  (  type  )  { 
case   File  : 
try  { 
outputStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   XformationException  (  "Unable to close output stream"  ,  e  )  ; 
} 
break  ; 
case   Stream  : 
break  ; 
} 
faucet  .  dispose  (  )  ; 
} 




@  Override 
public   void   consume  (  )  { 
if  (  !  hookedUp  &&  faucetTemplate  ==  null  )  throw   new   XformationException  (  "Sink has not been set up correctly: "  +  "faucet has not been set"  )  ; 
switch  (  type  )  { 
case   File  : 
try  { 
outputStream  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  path  )  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   XformationException  (  "Unable to create output stream"  ,  e  )  ; 
} 
break  ; 
case   Stream  : 
break  ; 
} 
if  (  !  hookedUp  )  { 
if  (  faucetTemplate   instanceof   IPipelineItem  )  { 
(  (  IPipelineItem  )  faucetTemplate  )  .  consume  (  this  )  ; 
} 
} 
try  { 
int   counter  =  0  ; 
while  (  true  )  { 
DequePayload   payload  =  null  ; 
try  { 
payload  =  deque  .  takeFirst  (  )  ; 
}  catch  (  InterruptedException   ie  )  { 
} 
if  (  payload  ==  null  )  break  ; 
IFaucet   faucet  =  payload  .  faucet  ; 
if  (  logger  .  isTraceEnabled  (  )  )  logger  .  trace  (  "Retrieved faucet: "  +  faucet  +  " from the mux deque"  )  ; 
if  (  faucet  ==  null  )  break  ; 
ZipEntry   entry  =  new   ZipEntry  (  "entry"  +  counter  ++  )  ; 
outputStream  .  putNextEntry  (  entry  )  ; 
StreamResult   result  =  new   StreamResult  (  outputStream  )  ; 
InputSource   inputSource  =  null  ; 
if  (  faucet   instanceof   IPipelineItem  )  { 
inputSource  =  (  InputSource  )  (  (  IPipelineItem  )  faucet  )  .  consume  (  this  )  ; 
} 
try  { 
Object   faucetObject  =  faucet  .  getSource  (  ContentType  .  XML  )  ; 
if  (  logger  .  isTraceEnabled  (  )  )  logger  .  trace  (  "Sink is using reader: "  +  faucetObject  )  ; 
SAXTransformerFactory   stf  =  (  SAXTransformerFactory  )  TransformerFactory  .  newInstance  (  )  ; 
Transformer   transformer  =  stf  .  newTransformer  (  )  ; 
SAXSource   transformSource  =  new   SAXSource  (  (  XMLReader  )  faucetObject  ,  inputSource  )  ; 
transformer  .  transform  (  transformSource  ,  result  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   XformationException  (  "Unable to set up transform"  ,  e  )  ; 
} 
faucet  .  dispose  (  )  ; 
} 
outputStream  .  close  (  )  ; 
faucetTemplate  .  dispose  (  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Error while consuming input"  ,  ioe  )  ; 
throw   new   XformationException  (  "Unable to transform stream"  ,  ioe  )  ; 
} 
} 





@  Override 
public   void   readFrom  (  DataInputStream   in  )  throws   IOException  ,  IllegalAccessException  ,  InstantiationException  { 
super  .  readFrom  (  in  )  ; 
this  .  deque  =  new   LinkedBlockingDeque  <  DequePayload  >  (  )  ; 
this  .  outputStream  =  null  ; 
} 
} 

