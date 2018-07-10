package   org  .  apache  .  axis  ; 

import   org  .  apache  .  axis  .  components  .  logger  .  LogFactory  ; 
import   org  .  apache  .  axis  .  encoding  .  DeserializationContext  ; 
import   org  .  apache  .  axis  .  encoding  .  SerializationContext  ; 
import   org  .  apache  .  axis  .  message  .  InputStreamBody  ; 
import   org  .  apache  .  axis  .  message  .  MimeHeaders  ; 
import   org  .  apache  .  axis  .  message  .  SOAPDocumentImpl  ; 
import   org  .  apache  .  axis  .  message  .  SOAPEnvelope  ; 
import   org  .  apache  .  axis  .  message  .  SOAPHeaderElement  ; 
import   org  .  apache  .  axis  .  transport  .  http  .  HTTPConstants  ; 
import   org  .  apache  .  axis  .  utils  .  ByteArray  ; 
import   org  .  apache  .  axis  .  utils  .  Messages  ; 
import   org  .  apache  .  axis  .  utils  .  SessionUtils  ; 
import   org  .  apache  .  axis  .  utils  .  XMLUtils  ; 
import   org  .  apache  .  axis  .  handlers  .  HandlerChainImpl  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  w3c  .  dom  .  Attr  ; 
import   org  .  w3c  .  dom  .  CDATASection  ; 
import   org  .  w3c  .  dom  .  Comment  ; 
import   org  .  w3c  .  dom  .  DOMException  ; 
import   org  .  w3c  .  dom  .  DOMImplementation  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  DocumentFragment  ; 
import   org  .  w3c  .  dom  .  DocumentType  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  EntityReference  ; 
import   org  .  w3c  .  dom  .  NamedNodeMap  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   org  .  w3c  .  dom  .  ProcessingInstruction  ; 
import   org  .  w3c  .  dom  .  Text  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   javax  .  xml  .  soap  .  SOAPException  ; 
import   javax  .  xml  .  soap  .  SOAPMessage  ; 
import   javax  .  xml  .  transform  .  Source  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamSource  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Vector  ; 

















public   class   SOAPPart   extends   javax  .  xml  .  soap  .  SOAPPart   implements   Part  { 

protected   static   Log   log  =  LogFactory  .  getLog  (  SOAPPart  .  class  .  getName  (  )  )  ; 

public   static   final   int   FORM_STRING  =  1  ; 

public   static   final   int   FORM_INPUTSTREAM  =  2  ; 

public   static   final   int   FORM_SOAPENVELOPE  =  3  ; 

public   static   final   int   FORM_BYTES  =  4  ; 

public   static   final   int   FORM_BODYINSTREAM  =  5  ; 

public   static   final   int   FORM_FAULT  =  6  ; 

public   static   final   int   FORM_OPTIMIZED  =  7  ; 

private   int   currentForm  ; 




public   static   final   String   ALLOW_FORM_OPTIMIZATION  =  "axis.form.optimization"  ; 

private   MimeHeaders   mimeHeaders  =  new   MimeHeaders  (  )  ; 

private   static   final   String  [  ]  formNames  =  {  ""  ,  "FORM_STRING"  ,  "FORM_INPUTSTREAM"  ,  "FORM_SOAPENVELOPE"  ,  "FORM_BYTES"  ,  "FORM_BODYINSTREAM"  ,  "FORM_FAULT"  ,  "FORM_OPTIMIZED"  }  ; 











private   Object   currentMessage  ; 




private   String   currentEncoding  =  "UTF-8"  ; 

private   String   currentMessageAsString  =  null  ; 

private   byte  [  ]  currentMessageAsBytes  =  null  ; 

private   org  .  apache  .  axis  .  message  .  SOAPEnvelope   currentMessageAsEnvelope  =  null  ; 




private   Message   msgObject  ; 


private   Source   contentSource  =  null  ; 










public   SOAPPart  (  Message   parent  ,  Object   initialContents  ,  boolean   isBodyStream  )  { 
setMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_ID  ,  SessionUtils  .  generateSessionId  (  )  )  ; 
setMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_TYPE  ,  "text/xml"  )  ; 
msgObject  =  parent  ; 
int   form  =  FORM_STRING  ; 
if  (  initialContents   instanceof   SOAPEnvelope  )  { 
form  =  FORM_SOAPENVELOPE  ; 
(  (  SOAPEnvelope  )  initialContents  )  .  setOwnerDocument  (  this  )  ; 
}  else   if  (  initialContents   instanceof   InputStream  )  { 
form  =  isBodyStream  ?  FORM_BODYINSTREAM  :  FORM_INPUTSTREAM  ; 
}  else   if  (  initialContents   instanceof   byte  [  ]  )  { 
form  =  FORM_BYTES  ; 
}  else   if  (  initialContents   instanceof   AxisFault  )  { 
form  =  FORM_FAULT  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Enter: SOAPPart ctor("  +  formNames  [  form  ]  +  ")"  )  ; 
} 
setCurrentMessage  (  initialContents  ,  form  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart ctor()"  )  ; 
} 
} 






public   Message   getMessage  (  )  { 
return   msgObject  ; 
} 







public   void   setMessage  (  Message   msg  )  { 
this  .  msgObject  =  msg  ; 
} 






public   String   getContentType  (  )  { 
return  "text/xml"  ; 
} 








public   long   getContentLength  (  )  throws   AxisFault  { 
saveChanges  (  )  ; 
if  (  currentForm  ==  FORM_OPTIMIZED  )  { 
return  (  (  ByteArray  )  currentMessage  )  .  size  (  )  ; 
}  else   if  (  currentForm  ==  FORM_BYTES  )  { 
return  (  (  byte  [  ]  )  currentMessage  )  .  length  ; 
} 
byte  [  ]  bytes  =  this  .  getAsBytes  (  )  ; 
return   bytes  .  length  ; 
} 













public   void   setSOAPEnvelope  (  org  .  apache  .  axis  .  message  .  SOAPEnvelope   env  )  { 
setCurrentMessage  (  env  ,  FORM_SOAPENVELOPE  )  ; 
} 






public   void   writeTo  (  java  .  io  .  OutputStream   os  )  throws   IOException  { 
if  (  currentForm  ==  FORM_BYTES  )  { 
os  .  write  (  (  byte  [  ]  )  currentMessage  )  ; 
}  else   if  (  currentForm  ==  FORM_OPTIMIZED  )  { 
(  (  ByteArray  )  currentMessage  )  .  writeTo  (  os  )  ; 
}  else  { 
Writer   writer  =  new   OutputStreamWriter  (  os  ,  currentEncoding  )  ; 
writer  =  new   BufferedWriter  (  new   PrintWriter  (  writer  )  )  ; 
writeTo  (  writer  )  ; 
writer  .  flush  (  )  ; 
} 
} 






public   void   writeTo  (  Writer   writer  )  throws   IOException  { 
boolean   inclXmlDecl  =  false  ; 
if  (  msgObject  .  getMessageContext  (  )  !=  null  )  { 
inclXmlDecl  =  true  ; 
}  else  { 
try  { 
String   xmlDecl  =  (  String  )  msgObject  .  getProperty  (  SOAPMessage  .  WRITE_XML_DECLARATION  )  ; 
if  (  xmlDecl  !=  null  &&  xmlDecl  .  equals  (  "true"  )  )  { 
inclXmlDecl  =  true  ; 
} 
}  catch  (  SOAPException   e  )  { 
throw   new   IOException  (  e  .  getMessage  (  )  )  ; 
} 
} 
if  (  currentForm  ==  FORM_FAULT  )  { 
AxisFault   env  =  (  AxisFault  )  currentMessage  ; 
try  { 
SerializationContext   serContext  =  new   SerializationContext  (  writer  ,  getMessage  (  )  .  getMessageContext  (  )  )  ; 
serContext  .  setSendDecl  (  inclXmlDecl  )  ; 
serContext  .  setEncoding  (  currentEncoding  )  ; 
env  .  output  (  serContext  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
throw   env  ; 
} 
return  ; 
} 
if  (  currentForm  ==  FORM_SOAPENVELOPE  )  { 
SOAPEnvelope   env  =  (  SOAPEnvelope  )  currentMessage  ; 
try  { 
SerializationContext   serContext  =  new   SerializationContext  (  writer  ,  getMessage  (  )  .  getMessageContext  (  )  )  ; 
serContext  .  setSendDecl  (  inclXmlDecl  )  ; 
serContext  .  setEncoding  (  currentEncoding  )  ; 
env  .  output  (  serContext  )  ; 
}  catch  (  Exception   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
return  ; 
} 
String   xml  =  this  .  getAsString  (  )  ; 
if  (  inclXmlDecl  )  { 
if  (  !  xml  .  startsWith  (  "<?xml"  )  )  { 
writer  .  write  (  "<?xml version=\"1.0\" encoding=\""  )  ; 
writer  .  write  (  currentEncoding  )  ; 
writer  .  write  (  "\"?>"  )  ; 
} 
} 
writer  .  write  (  xml  )  ; 
} 











public   Object   getCurrentMessage  (  )  { 
return   currentMessage  ; 
} 






public   void   setCurrentMessage  (  Object   currMsg  ,  int   form  )  { 
currentMessageAsString  =  null  ; 
currentMessageAsBytes  =  null  ; 
currentMessageAsEnvelope  =  null  ; 
setCurrentForm  (  currMsg  ,  form  )  ; 
} 









private   void   setCurrentForm  (  Object   currMsg  ,  int   form  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
String   msgStr  ; 
if  (  currMsg   instanceof   String  )  { 
msgStr  =  (  String  )  currMsg  ; 
}  else  { 
msgStr  =  currMsg  .  getClass  (  )  .  getName  (  )  ; 
} 
log  .  debug  (  Messages  .  getMessage  (  "setMsgForm"  ,  formNames  [  form  ]  ,  ""  +  msgStr  )  )  ; 
} 
if  (  isFormOptimizationAllowed  (  )  )  { 
currentMessage  =  currMsg  ; 
currentForm  =  form  ; 
if  (  currentForm  ==  FORM_SOAPENVELOPE  )  { 
currentMessageAsEnvelope  =  (  org  .  apache  .  axis  .  message  .  SOAPEnvelope  )  currMsg  ; 
} 
} 
} 





private   boolean   isFormOptimizationAllowed  (  )  { 
boolean   allowFormOptimization  =  true  ; 
Message   msg  =  getMessage  (  )  ; 
if  (  msg  !=  null  )  { 
MessageContext   ctx  =  msg  .  getMessageContext  (  )  ; 
if  (  ctx  !=  null  )  { 
Boolean   propFormOptimization  =  (  Boolean  )  ctx  .  getProperty  (  ALLOW_FORM_OPTIMIZATION  )  ; 
if  (  propFormOptimization  !=  null  )  { 
allowFormOptimization  =  propFormOptimization  .  booleanValue  (  )  ; 
} 
} 
} 
return   allowFormOptimization  ; 
} 

public   int   getCurrentForm  (  )  { 
return   currentForm  ; 
} 








public   byte  [  ]  getAsBytes  (  )  throws   AxisFault  { 
log  .  debug  (  "Enter: SOAPPart::getAsBytes"  )  ; 
if  (  currentForm  ==  FORM_OPTIMIZED  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
try  { 
return  (  (  ByteArray  )  currentMessage  )  .  toByteArray  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
} 
if  (  currentForm  ==  FORM_BYTES  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return  (  byte  [  ]  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_BODYINSTREAM  )  { 
try  { 
getAsSOAPEnvelope  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  fatal  (  Messages  .  getMessage  (  "makeEnvFail00"  )  ,  e  )  ; 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return   null  ; 
} 
} 
if  (  currentForm  ==  FORM_INPUTSTREAM  )  { 
try  { 
InputStream   inp  =  null  ; 
byte  [  ]  buf  =  null  ; 
try  { 
inp  =  (  InputStream  )  currentMessage  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
buf  =  new   byte  [  4096  ]  ; 
int   len  ; 
while  (  (  len  =  inp  .  read  (  buf  ,  0  ,  4096  )  )  !=  -  1  )  baos  .  write  (  buf  ,  0  ,  len  )  ; 
buf  =  baos  .  toByteArray  (  )  ; 
}  finally  { 
if  (  inp  !=  null  &&  currentMessage   instanceof   org  .  apache  .  axis  .  transport  .  http  .  SocketInputStream  )  inp  .  close  (  )  ; 
} 
setCurrentForm  (  buf  ,  FORM_BYTES  )  ; 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return  (  byte  [  ]  )  currentMessage  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
} 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return   null  ; 
} 
if  (  currentForm  ==  FORM_SOAPENVELOPE  ||  currentForm  ==  FORM_FAULT  )  { 
currentEncoding  =  XMLUtils  .  getEncoding  (  msgObject  ,  null  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
BufferedOutputStream   os  =  new   BufferedOutputStream  (  baos  )  ; 
try  { 
this  .  writeTo  (  os  )  ; 
os  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
setCurrentForm  (  baos  .  toByteArray  (  )  ,  FORM_BYTES  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsBytes(): "  +  currentMessage  )  ; 
} 
return  (  byte  [  ]  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_STRING  )  { 
if  (  currentMessage  ==  currentMessageAsString  &&  currentMessageAsBytes  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsBytes()"  )  ; 
} 
return   currentMessageAsBytes  ; 
} 
currentMessageAsString  =  (  String  )  currentMessage  ; 
try  { 
currentEncoding  =  XMLUtils  .  getEncoding  (  msgObject  ,  null  )  ; 
setCurrentForm  (  (  (  String  )  currentMessage  )  .  getBytes  (  currentEncoding  )  ,  FORM_BYTES  )  ; 
}  catch  (  UnsupportedEncodingException   ue  )  { 
setCurrentForm  (  (  (  String  )  currentMessage  )  .  getBytes  (  )  ,  FORM_BYTES  )  ; 
} 
currentMessageAsBytes  =  (  byte  [  ]  )  currentMessage  ; 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return  (  byte  [  ]  )  currentMessage  ; 
} 
log  .  error  (  Messages  .  getMessage  (  "cantConvert00"  ,  ""  +  currentForm  )  )  ; 
log  .  debug  (  "Exit: SOAPPart::getAsBytes"  )  ; 
return   null  ; 
} 

public   void   saveChanges  (  )  throws   AxisFault  { 
log  .  debug  (  "Enter: SOAPPart::saveChanges"  )  ; 
if  (  currentForm  ==  FORM_SOAPENVELOPE  ||  currentForm  ==  FORM_FAULT  )  { 
currentEncoding  =  XMLUtils  .  getEncoding  (  msgObject  ,  null  )  ; 
ByteArray   array  =  new   ByteArray  (  )  ; 
try  { 
writeTo  (  array  )  ; 
array  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
setCurrentForm  (  array  ,  FORM_OPTIMIZED  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::saveChanges(): "  +  currentMessage  )  ; 
} 
} 
} 








public   String   getAsString  (  )  throws   AxisFault  { 
log  .  debug  (  "Enter: SOAPPart::getAsString"  )  ; 
if  (  currentForm  ==  FORM_STRING  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessage  )  ; 
} 
return  (  String  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_INPUTSTREAM  ||  currentForm  ==  FORM_BODYINSTREAM  )  { 
getAsBytes  (  )  ; 
} 
if  (  currentForm  ==  FORM_OPTIMIZED  )  { 
try  { 
currentMessageAsBytes  =  (  (  ByteArray  )  currentMessage  )  .  toByteArray  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
try  { 
setCurrentForm  (  new   String  (  (  byte  [  ]  )  currentMessageAsBytes  ,  currentEncoding  )  ,  FORM_STRING  )  ; 
}  catch  (  UnsupportedEncodingException   ue  )  { 
setCurrentForm  (  new   String  (  (  byte  [  ]  )  currentMessageAsBytes  )  ,  FORM_STRING  )  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessage  )  ; 
} 
return  (  String  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_BYTES  )  { 
if  (  currentMessage  ==  currentMessageAsBytes  &&  currentMessageAsString  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessageAsString  )  ; 
} 
return   currentMessageAsString  ; 
} 
currentMessageAsBytes  =  (  byte  [  ]  )  currentMessage  ; 
try  { 
setCurrentForm  (  new   String  (  (  byte  [  ]  )  currentMessage  ,  currentEncoding  )  ,  FORM_STRING  )  ; 
}  catch  (  UnsupportedEncodingException   ue  )  { 
setCurrentForm  (  new   String  (  (  byte  [  ]  )  currentMessage  )  ,  FORM_STRING  )  ; 
} 
currentMessageAsString  =  (  String  )  currentMessage  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessage  )  ; 
} 
return  (  String  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_FAULT  )  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
try  { 
this  .  writeTo  (  writer  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
return   null  ; 
} 
setCurrentForm  (  writer  .  getBuffer  (  )  .  toString  (  )  ,  FORM_STRING  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessage  )  ; 
} 
return  (  String  )  currentMessage  ; 
} 
if  (  currentForm  ==  FORM_SOAPENVELOPE  )  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
try  { 
this  .  writeTo  (  writer  )  ; 
}  catch  (  Exception   e  )  { 
throw   AxisFault  .  makeFault  (  e  )  ; 
} 
setCurrentForm  (  writer  .  getBuffer  (  )  .  toString  (  )  ,  FORM_STRING  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Exit: SOAPPart::getAsString(): "  +  currentMessage  )  ; 
} 
return  (  String  )  currentMessage  ; 
} 
log  .  error  (  Messages  .  getMessage  (  "cantConvert01"  ,  ""  +  currentForm  )  )  ; 
log  .  debug  (  "Exit: SOAPPart::getAsString()"  )  ; 
return   null  ; 
} 









public   SOAPEnvelope   getAsSOAPEnvelope  (  )  throws   AxisFault  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Enter: SOAPPart::getAsSOAPEnvelope()"  )  ; 
log  .  debug  (  Messages  .  getMessage  (  "currForm"  ,  formNames  [  currentForm  ]  )  )  ; 
} 
if  (  currentForm  ==  FORM_SOAPENVELOPE  )  return  (  SOAPEnvelope  )  currentMessage  ; 
if  (  currentForm  ==  FORM_BODYINSTREAM  )  { 
InputStreamBody   bodyEl  =  new   InputStreamBody  (  (  InputStream  )  currentMessage  )  ; 
SOAPEnvelope   env  =  new   SOAPEnvelope  (  )  ; 
env  .  setOwnerDocument  (  this  )  ; 
env  .  addBodyElement  (  bodyEl  )  ; 
setCurrentForm  (  env  ,  FORM_SOAPENVELOPE  )  ; 
return   env  ; 
} 
InputSource   is  ; 
if  (  currentForm  ==  FORM_INPUTSTREAM  )  { 
is  =  new   InputSource  (  (  InputStream  )  currentMessage  )  ; 
String   encoding  =  XMLUtils  .  getEncoding  (  msgObject  ,  null  ,  null  )  ; 
if  (  encoding  !=  null  )  { 
currentEncoding  =  encoding  ; 
is  .  setEncoding  (  currentEncoding  )  ; 
} 
}  else  { 
is  =  new   InputSource  (  new   StringReader  (  getAsString  (  )  )  )  ; 
} 
DeserializationContext   dser  =  new   DeserializationContext  (  is  ,  getMessage  (  )  .  getMessageContext  (  )  ,  getMessage  (  )  .  getMessageType  (  )  )  ; 
dser  .  getEnvelope  (  )  .  setOwnerDocument  (  this  )  ; 
try  { 
dser  .  parse  (  )  ; 
}  catch  (  SAXException   e  )  { 
Exception   real  =  e  .  getException  (  )  ; 
if  (  real  ==  null  )  real  =  e  ; 
throw   AxisFault  .  makeFault  (  real  )  ; 
} 
SOAPEnvelope   nse  =  dser  .  getEnvelope  (  )  ; 
if  (  currentMessageAsEnvelope  !=  null  )  { 
Vector   newHeaders  =  nse  .  getHeaders  (  )  ; 
Vector   oldHeaders  =  currentMessageAsEnvelope  .  getHeaders  (  )  ; 
if  (  null  !=  newHeaders  &&  null  !=  oldHeaders  )  { 
Iterator   ohi  =  oldHeaders  .  iterator  (  )  ; 
Iterator   nhi  =  newHeaders  .  iterator  (  )  ; 
while  (  ohi  .  hasNext  (  )  &&  nhi  .  hasNext  (  )  )  { 
SOAPHeaderElement   nhe  =  (  SOAPHeaderElement  )  nhi  .  next  (  )  ; 
SOAPHeaderElement   ohe  =  (  SOAPHeaderElement  )  ohi  .  next  (  )  ; 
if  (  ohe  .  isProcessed  (  )  )  nhe  .  setProcessed  (  true  )  ; 
} 
} 
} 
setCurrentForm  (  nse  ,  FORM_SOAPENVELOPE  )  ; 
log  .  debug  (  "Exit: SOAPPart::getAsSOAPEnvelope"  )  ; 
SOAPEnvelope   env  =  (  SOAPEnvelope  )  currentMessage  ; 
env  .  setOwnerDocument  (  this  )  ; 
return   env  ; 
} 







public   void   addMimeHeader  (  String   header  ,  String   value  )  { 
mimeHeaders  .  addHeader  (  header  ,  value  )  ; 
} 







private   String   getFirstMimeHeader  (  String   header  )  { 
String  [  ]  values  =  mimeHeaders  .  getHeader  (  header  )  ; 
if  (  values  !=  null  &&  values  .  length  >  0  )  return   values  [  0  ]  ; 
return   null  ; 
} 






public   String   getContentLocation  (  )  { 
return   getFirstMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_LOCATION  )  ; 
} 






public   void   setContentLocation  (  String   loc  )  { 
setMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_LOCATION  ,  loc  )  ; 
} 






public   void   setContentId  (  String   newCid  )  { 
setMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_ID  ,  newCid  )  ; 
} 






public   String   getContentId  (  )  { 
return   getFirstMimeHeader  (  HTTPConstants  .  HEADER_CONTENT_ID  )  ; 
} 








public   String   getContentIdRef  (  )  { 
return   org  .  apache  .  axis  .  attachments  .  Attachments  .  CIDprefix  +  getContentId  (  )  ; 
} 







public   java  .  util  .  Iterator   getMatchingMimeHeaders  (  final   String  [  ]  match  )  { 
return   mimeHeaders  .  getMatchingHeaders  (  match  )  ; 
} 








public   java  .  util  .  Iterator   getNonMatchingMimeHeaders  (  final   String  [  ]  match  )  { 
return   mimeHeaders  .  getNonMatchingHeaders  (  match  )  ; 
} 










public   void   setContent  (  Source   source  )  throws   SOAPException  { 
if  (  source  ==  null  )  throw   new   SOAPException  (  Messages  .  getMessage  (  "illegalArgumentException00"  )  )  ; 
MessageContext   ctx  =  getMessage  (  )  .  getMessageContext  (  )  ; 
if  (  ctx  !=  null  )  { 
ctx  .  setProperty  (  org  .  apache  .  axis  .  SOAPPart  .  ALLOW_FORM_OPTIMIZATION  ,  Boolean  .  TRUE  )  ; 
} 
contentSource  =  source  ; 
InputSource   in  =  org  .  apache  .  axis  .  utils  .  XMLUtils  .  sourceToInputSource  (  contentSource  )  ; 
InputStream   is  =  in  .  getByteStream  (  )  ; 
if  (  is  !=  null  )  { 
setCurrentMessage  (  is  ,  FORM_INPUTSTREAM  )  ; 
}  else  { 
Reader   r  =  in  .  getCharacterStream  (  )  ; 
if  (  r  ==  null  )  { 
throw   new   SOAPException  (  Messages  .  getMessage  (  "noCharacterOrByteStream"  )  )  ; 
} 
BufferedReader   br  =  new   BufferedReader  (  r  )  ; 
String   line  =  null  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
try  { 
while  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   SOAPException  (  Messages  .  getMessage  (  "couldNotReadFromCharStream"  )  ,  e  )  ; 
} 
setCurrentMessage  (  sb  .  toString  (  )  ,  FORM_STRING  )  ; 
} 
} 










public   Source   getContent  (  )  throws   SOAPException  { 
if  (  contentSource  ==  null  )  { 
switch  (  currentForm  )  { 
case   FORM_STRING  : 
String   s  =  (  String  )  currentMessage  ; 
contentSource  =  new   StreamSource  (  new   StringReader  (  s  )  )  ; 
break  ; 
case   FORM_INPUTSTREAM  : 
contentSource  =  new   StreamSource  (  (  InputStream  )  currentMessage  )  ; 
break  ; 
case   FORM_SOAPENVELOPE  : 
SOAPEnvelope   se  =  (  SOAPEnvelope  )  currentMessage  ; 
try  { 
contentSource  =  new   DOMSource  (  se  .  getAsDocument  (  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   SOAPException  (  Messages  .  getMessage  (  "errorGetDocFromSOAPEnvelope"  )  ,  e  )  ; 
} 
break  ; 
case   FORM_OPTIMIZED  : 
try  { 
ByteArrayInputStream   baos  =  new   ByteArrayInputStream  (  (  (  ByteArray  )  currentMessage  )  .  toByteArray  (  )  )  ; 
contentSource  =  new   StreamSource  (  baos  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   SOAPException  (  Messages  .  getMessage  (  "errorGetDocFromSOAPEnvelope"  )  ,  e  )  ; 
} 
break  ; 
case   FORM_BYTES  : 
byte  [  ]  bytes  =  (  byte  [  ]  )  currentMessage  ; 
contentSource  =  new   StreamSource  (  new   ByteArrayInputStream  (  bytes  )  )  ; 
break  ; 
case   FORM_BODYINSTREAM  : 
contentSource  =  new   StreamSource  (  (  InputStream  )  currentMessage  )  ; 
break  ; 
} 
} 
return   contentSource  ; 
} 








public   Iterator   getAllMimeHeaders  (  )  { 
return   mimeHeaders  .  getAllHeaders  (  )  ; 
} 

























public   void   setMimeHeader  (  String   name  ,  String   value  )  { 
mimeHeaders  .  setHeader  (  name  ,  value  )  ; 
} 











public   String  [  ]  getMimeHeader  (  String   name  )  { 
return   mimeHeaders  .  getHeader  (  name  )  ; 
} 





public   void   removeAllMimeHeaders  (  )  { 
mimeHeaders  .  removeAllHeaders  (  )  ; 
} 






public   void   removeMimeHeader  (  String   header  )  { 
mimeHeaders  .  removeHeader  (  header  )  ; 
} 









public   javax  .  xml  .  soap  .  SOAPEnvelope   getEnvelope  (  )  throws   SOAPException  { 
try  { 
return   getAsSOAPEnvelope  (  )  ; 
}  catch  (  AxisFault   af  )  { 
throw   new   SOAPException  (  af  )  ; 
} 
} 











private   Document   document  =  new   SOAPDocumentImpl  (  this  )  ; 




public   Document   getSOAPDocument  (  )  { 
if  (  document  ==  null  )  { 
document  =  new   SOAPDocumentImpl  (  this  )  ; 
} 
return   document  ; 
} 




public   DocumentType   getDoctype  (  )  { 
return   document  .  getDoctype  (  )  ; 
} 




public   DOMImplementation   getImplementation  (  )  { 
return   document  .  getImplementation  (  )  ; 
} 




protected   Document   mDocument  ; 

public   Element   getDocumentElement  (  )  { 
try  { 
return   getEnvelope  (  )  ; 
}  catch  (  SOAPException   se  )  { 
return   null  ; 
} 
} 







public   Element   createElement  (  String   tagName  )  throws   DOMException  { 
return   document  .  createElement  (  tagName  )  ; 
} 

public   DocumentFragment   createDocumentFragment  (  )  { 
return   document  .  createDocumentFragment  (  )  ; 
} 

public   Text   createTextNode  (  String   data  )  { 
return   document  .  createTextNode  (  data  )  ; 
} 

public   Comment   createComment  (  String   data  )  { 
return   document  .  createComment  (  data  )  ; 
} 

public   CDATASection   createCDATASection  (  String   data  )  throws   DOMException  { 
return   document  .  createCDATASection  (  data  )  ; 
} 

public   ProcessingInstruction   createProcessingInstruction  (  String   target  ,  String   data  )  throws   DOMException  { 
return   document  .  createProcessingInstruction  (  target  ,  data  )  ; 
} 

public   Attr   createAttribute  (  String   name  )  throws   DOMException  { 
return   document  .  createAttribute  (  name  )  ; 
} 

public   EntityReference   createEntityReference  (  String   name  )  throws   DOMException  { 
return   document  .  createEntityReference  (  name  )  ; 
} 

public   NodeList   getElementsByTagName  (  String   tagname  )  { 
return   document  .  getElementsByTagName  (  tagname  )  ; 
} 

public   Node   importNode  (  Node   importedNode  ,  boolean   deep  )  throws   DOMException  { 
return   document  .  importNode  (  importedNode  ,  deep  )  ; 
} 

public   Element   createElementNS  (  String   namespaceURI  ,  String   qualifiedName  )  throws   DOMException  { 
return   document  .  createElementNS  (  namespaceURI  ,  qualifiedName  )  ; 
} 

public   Attr   createAttributeNS  (  String   namespaceURI  ,  String   qualifiedName  )  throws   DOMException  { 
return   document  .  createAttributeNS  (  namespaceURI  ,  qualifiedName  )  ; 
} 

public   NodeList   getElementsByTagNameNS  (  String   namespaceURI  ,  String   localName  )  { 
return   document  .  getElementsByTagNameNS  (  namespaceURI  ,  localName  )  ; 
} 

public   Element   getElementById  (  String   elementId  )  { 
return   document  .  getElementById  (  elementId  )  ; 
} 

public   String   getEncoding  (  )  { 
return   currentEncoding  ; 
} 

public   void   setEncoding  (  String   s  )  { 
currentEncoding  =  s  ; 
} 

public   boolean   getStandalone  (  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented.71"  )  ; 
} 

public   void   setStandalone  (  boolean   flag  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented.72"  )  ; 
} 

public   boolean   getStrictErrorChecking  (  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented.73"  )  ; 
} 

public   void   setStrictErrorChecking  (  boolean   flag  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented. 74"  )  ; 
} 

public   String   getVersion  (  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented. 75"  )  ; 
} 

public   void   setVersion  (  String   s  )  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented.76"  )  ; 
} 

public   Node   adoptNode  (  Node   node  )  throws   DOMException  { 
throw   new   UnsupportedOperationException  (  "Not yet implemented.77"  )  ; 
} 




public   String   getNodeName  (  )  { 
return   document  .  getNodeName  (  )  ; 
} 

public   String   getNodeValue  (  )  throws   DOMException  { 
return   document  .  getNodeValue  (  )  ; 
} 

public   void   setNodeValue  (  String   nodeValue  )  throws   DOMException  { 
document  .  setNodeValue  (  nodeValue  )  ; 
} 

public   short   getNodeType  (  )  { 
return   document  .  getNodeType  (  )  ; 
} 

public   Node   getParentNode  (  )  { 
return   document  .  getParentNode  (  )  ; 
} 

public   NodeList   getChildNodes  (  )  { 
return   document  .  getChildNodes  (  )  ; 
} 

public   Node   getFirstChild  (  )  { 
return   document  .  getFirstChild  (  )  ; 
} 

public   Node   getLastChild  (  )  { 
return   document  .  getLastChild  (  )  ; 
} 

public   Node   getPreviousSibling  (  )  { 
return   document  .  getPreviousSibling  (  )  ; 
} 

public   Node   getNextSibling  (  )  { 
return   document  .  getNextSibling  (  )  ; 
} 

public   NamedNodeMap   getAttributes  (  )  { 
return   document  .  getAttributes  (  )  ; 
} 

public   Document   getOwnerDocument  (  )  { 
return   document  .  getOwnerDocument  (  )  ; 
} 

public   Node   insertBefore  (  Node   newChild  ,  Node   refChild  )  throws   DOMException  { 
return   document  .  insertBefore  (  newChild  ,  refChild  )  ; 
} 

public   Node   replaceChild  (  Node   newChild  ,  Node   oldChild  )  throws   DOMException  { 
return   document  .  replaceChild  (  newChild  ,  oldChild  )  ; 
} 

public   Node   removeChild  (  Node   oldChild  )  throws   DOMException  { 
return   document  .  removeChild  (  oldChild  )  ; 
} 

public   Node   appendChild  (  Node   newChild  )  throws   DOMException  { 
return   document  .  appendChild  (  newChild  )  ; 
} 

public   boolean   hasChildNodes  (  )  { 
return   document  .  hasChildNodes  (  )  ; 
} 

public   Node   cloneNode  (  boolean   deep  )  { 
return   document  .  cloneNode  (  deep  )  ; 
} 

public   void   normalize  (  )  { 
document  .  normalize  (  )  ; 
} 

public   boolean   isSupported  (  String   feature  ,  String   version  )  { 
return   document  .  isSupported  (  feature  ,  version  )  ; 
} 

public   String   getNamespaceURI  (  )  { 
return   document  .  getNamespaceURI  (  )  ; 
} 

public   String   getPrefix  (  )  { 
return   document  .  getPrefix  (  )  ; 
} 

public   void   setPrefix  (  String   prefix  )  throws   DOMException  { 
document  .  setPrefix  (  prefix  )  ; 
} 

public   String   getLocalName  (  )  { 
return   document  .  getLocalName  (  )  ; 
} 

public   boolean   hasAttributes  (  )  { 
return   document  .  hasAttributes  (  )  ; 
} 

public   boolean   isBodyStream  (  )  { 
return  (  currentForm  ==  SOAPPart  .  FORM_INPUTSTREAM  ||  currentForm  ==  SOAPPart  .  FORM_BODYINSTREAM  )  ; 
} 
} 

