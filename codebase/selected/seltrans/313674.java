package   test  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  Socket  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  transform  .  TransformerConfigurationException  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   org  .  xml  .  sax  .  SAXException  ; 






public   abstract   class   AbstractAgent  { 

@  SuppressWarnings  (  "serial"  ) 
private   class   SocketClosedException   extends   Exception  { 
} 

private   int   networkport  ; 

private   String   networkhost  ; 

private   InetSocketAddress   socketaddress  ; 

private   Socket   socket  ; 

private   InputStream   inputstream  ; 

private   OutputStream   outputstream  ; 

protected   String   username  ; 

private   String   password  ; 

protected   DocumentBuilderFactory   documentbuilderfactory  ; 

private   TransformerFactory   transformerfactory  ; 

protected   static   Logger   logger  =  Logger  .  getLogger  (  "agentLog.log"  )  ; 

public   static   String   getDate  (  )  { 
Date   dt  =  new   Date  (  )  ; 
SimpleDateFormat   df  =  new   SimpleDateFormat  (  "HH-mm-ss_dd-MM-yyyy"  )  ; 
return   df  .  format  (  dt  )  ; 
} 

public   AbstractAgent  (  )  { 
networkhost  =  "localhost"  ; 
networkport  =  0  ; 
socket  =  new   Socket  (  )  ; 
documentbuilderfactory  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
transformerfactory  =  TransformerFactory  .  newInstance  (  )  ; 
} 

public   String   getHost  (  )  { 
return   networkhost  ; 
} 

public   void   setHost  (  String   host  )  { 
this  .  networkhost  =  host  ; 
} 

public   int   getPort  (  )  { 
return   networkport  ; 
} 

public   void   setPort  (  int   port  )  { 
this  .  networkport  =  port  ; 
} 

public   String   getUsername  (  )  { 
return   username  ; 
} 

public   void   setUsername  (  String   username  )  { 
this  .  username  =  username  ; 
} 

public   String   getPassword  (  )  { 
return   password  ; 
} 

public   void   setPassword  (  String   password  )  { 
this  .  password  =  password  ; 
} 





public   void   start  (  )  { 
new   Thread  (  )  { 

public   void   run  (  )  { 
agentThread  (  )  ; 
} 
}  .  start  (  )  ; 
} 










public   void   sendAuthentication  (  String   username  ,  String   password  )  throws   IOException  { 
try  { 
Document   doc  =  documentbuilderfactory  .  newDocumentBuilder  (  )  .  newDocument  (  )  ; 
Element   root  =  doc  .  createElement  (  "message"  )  ; 
root  .  setAttribute  (  "type"  ,  "auth-request"  )  ; 
doc  .  appendChild  (  root  )  ; 
Element   auth  =  doc  .  createElement  (  "authentication"  )  ; 
auth  .  setAttribute  (  "username"  ,  username  )  ; 
auth  .  setAttribute  (  "password"  ,  password  )  ; 
root  .  appendChild  (  auth  )  ; 
this  .  sendDocument  (  doc  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
System  .  err  .  println  (  "unable to create new document for authentication."  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 







public   boolean   receiveAuthenticationResult  (  )  throws   IOException  { 
try  { 
Document   doc  =  receiveDocument  (  )  ; 
Element   root  =  doc  .  getDocumentElement  (  )  ; 
if  (  root  ==  null  )  return   false  ; 
if  (  !  root  .  getAttribute  (  "type"  )  .  equalsIgnoreCase  (  "auth-response"  )  )  return   false  ; 
NodeList   nl  =  root  .  getChildNodes  (  )  ; 
Element   authresult  =  null  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   n  =  nl  .  item  (  i  )  ; 
if  (  n  .  getNodeType  (  )  ==  Element  .  ELEMENT_NODE  &&  n  .  getNodeName  (  )  .  equalsIgnoreCase  (  "authentication"  )  )  { 
authresult  =  (  Element  )  n  ; 
break  ; 
} 
} 
if  (  !  authresult  .  getAttribute  (  "result"  )  .  equalsIgnoreCase  (  "ok"  )  )  return   false  ; 
}  catch  (  SAXException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
}  catch  (  ParserConfigurationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
}  catch  (  SocketClosedException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
return   true  ; 
} 











public   boolean   doAuthentication  (  String   username  ,  String   password  )  throws   IOException  { 
sendAuthentication  (  username  ,  password  )  ; 
return   receiveAuthenticationResult  (  )  ; 
} 










public   byte  [  ]  receivePacket  (  )  throws   IOException  ,  SocketClosedException  { 
ByteArrayOutputStream   buffer  =  new   ByteArrayOutputStream  (  )  ; 
int   read  =  inputstream  .  read  (  )  ; 
while  (  read  !=  0  )  { 
if  (  read  ==  -  1  )  { 
throw   new   SocketClosedException  (  )  ; 
} 
buffer  .  write  (  read  )  ; 
read  =  inputstream  .  read  (  )  ; 
} 
String   s  =  "Server -> Agent: AgentName "  +  this  .  username  +  "\n"  +  buffer  .  toString  (  )  ; 
synchronized  (  logger  )  { 
logger  .  log  (  Level  .  ALL  ,  s  )  ; 
} 
return   buffer  .  toByteArray  (  )  ; 
} 












public   Document   receiveDocument  (  )  throws   SAXException  ,  IOException  ,  ParserConfigurationException  ,  SocketClosedException  { 
byte  [  ]  raw  =  receivePacket  (  )  ; 
Document   doc  =  documentbuilderfactory  .  newDocumentBuilder  (  )  .  parse  (  new   ByteArrayInputStream  (  raw  )  )  ; 
return   doc  ; 
} 















public   void   agentThread  (  )  { 
try  { 
socketaddress  =  new   InetSocketAddress  (  networkhost  ,  networkport  )  ; 
socket  .  connect  (  socketaddress  )  ; 
inputstream  =  socket  .  getInputStream  (  )  ; 
outputstream  =  socket  .  getOutputStream  (  )  ; 
boolean   auth  =  doAuthentication  (  username  ,  password  )  ; 
if  (  !  auth  )  { 
System  .  err  .  println  (  "Authentication failed"  )  ; 
return  ; 
} 
processLogIn  (  )  ; 
while  (  true  )  { 
Document   doc  =  null  ; 
try  { 
doc  =  receiveDocument  (  )  ; 
}  catch  (  SAXException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
Element   el_root  =  doc  .  getDocumentElement  (  )  ; 
if  (  el_root  ==  null  )  { 
System  .  err  .  println  (  "No document element found"  )  ; 
continue  ; 
} 
if  (  el_root  .  getNodeName  (  )  .  equals  (  "message"  )  )  { 
if  (  !  processMessage  (  el_root  )  )  break  ; 
}  else  { 
System  .  err  .  println  (  "Unknown document received"  )  ; 
} 
} 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "IOException"  )  ; 
e  .  printStackTrace  (  )  ; 
return  ; 
}  catch  (  SocketClosedException   e  )  { 
System  .  err  .  println  (  "Socket was closed"  )  ; 
} 
} 



























@  SuppressWarnings  (  "static-access"  ) 
public   boolean   processMessage  (  Element   el_message  )  { 
String   type  =  el_message  .  getAttribute  (  "type"  )  ; 
if  (  type  .  equals  (  "request-action"  )  ||  type  .  equals  (  "sim-start"  )  ||  type  .  equals  (  "sim-end"  )  )  { 
Element   el_perception  =  null  ; 
NodeList   nl  =  el_message  .  getChildNodes  (  )  ; 
String   infoelementname  =  "perception"  ; 
if  (  type  .  equals  (  "request-action"  )  )  { 
infoelementname  =  "perception"  ; 
}  else   if  (  type  .  equals  (  "sim-start"  )  )  { 
infoelementname  =  "simulation"  ; 
}  else   if  (  type  .  equals  (  "sim-end"  )  )  { 
infoelementname  =  "sim-result"  ; 
} 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   n  =  nl  .  item  (  i  )  ; 
if  (  n  .  getNodeType  (  )  ==  Element  .  ELEMENT_NODE  &&  n  .  getNodeName  (  )  .  equalsIgnoreCase  (  infoelementname  )  )  { 
if  (  el_perception  ==  null  )  el_perception  =  (  Element  )  n  ;  else  { 
System  .  err  .  println  (  "perception message doesn't contain right number of perception elements"  )  ; 
return   true  ; 
} 
} 
} 
Document   doc  =  null  ; 
try  { 
doc  =  documentbuilderfactory  .  newDocumentBuilder  (  )  .  newDocument  (  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
System  .  err  .  println  (  "parser config error"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
Element   el_response  =  doc  .  createElement  (  "message"  )  ; 
doc  .  appendChild  (  el_response  )  ; 
Element   el_action  =  doc  .  createElement  (  "action"  )  ; 
el_response  .  setAttribute  (  "type"  ,  "action"  )  ; 
el_response  .  appendChild  (  el_action  )  ; 
long   currenttime  =  0  ; 
try  { 
currenttime  =  Long  .  parseLong  (  el_message  .  getAttribute  (  "timestamp"  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
System  .  err  .  println  (  "number format invalid"  )  ; 
e  .  printStackTrace  (  )  ; 
return   true  ; 
} 
long   deadline  =  0  ; 
if  (  type  .  equals  (  "request-action"  )  )  { 
try  { 
deadline  =  Long  .  parseLong  (  el_perception  .  getAttribute  (  "deadline"  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
System  .  err  .  println  (  "number format invalid"  )  ; 
e  .  printStackTrace  (  )  ; 
return   true  ; 
} 
processRequestAction  (  el_perception  ,  el_action  ,  currenttime  ,  deadline  )  ; 
}  else   if  (  type  .  equals  (  "sim-start"  )  )  { 
processSimulationStart  (  el_perception  ,  currenttime  )  ; 
}  else   if  (  type  .  equals  (  "sim-end"  )  )  { 
processSimulationEnd  (  el_perception  ,  currenttime  )  ; 
} 
el_action  .  setAttribute  (  "id"  ,  el_perception  .  getAttribute  (  "id"  )  )  ; 
try  { 
if  (  type  .  equals  (  "request-action"  )  )  sendDocument  (  doc  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "IO Exception while trying to send action"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
}  else   if  (  type  .  equals  (  "pong"  )  )  { 
NodeList   nl  =  el_message  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   n  =  nl  .  item  (  i  )  ; 
if  (  n  .  getNodeType  (  )  ==  Element  .  ELEMENT_NODE  &&  n  .  getNodeName  (  )  .  equalsIgnoreCase  (  "payload"  )  )  { 
processPong  (  (  (  Element  )  n  )  .  getAttribute  (  "value"  )  )  ; 
return   true  ; 
} 
} 
} 
return   true  ; 
} 

public   void   processRequestAction  (  Element   perception  ,  Element   target  ,  long   currenttime  ,  long   deadline  )  { 
System  .  err  .  println  (  "---#-#-#-#-#-#-- processPerception --#-#-#-#-#-#---"  )  ; 
} 

public   void   processSimulationEnd  (  Element   perception  ,  long   currenttime  )  { 
System  .  err  .  println  (  "---#-#-#-#-#-#-- processSimEnd --#-#-#-#-#-#---"  )  ; 
} 

public   void   processSimulationStart  (  Element   perception  ,  long   currenttime  )  { 
System  .  err  .  println  (  "---#-#-#-#-#-#-- processSimStart --#-#-#-#-#-#---"  )  ; 
} 

public   void   processPong  (  String   pong  )  { 
System  .  err  .  println  (  "---#-#-#-#-#-#-- processPong("  +  pong  +  ") --#-#-#-#-#-#---"  )  ; 
} 

public   void   processLogIn  (  )  { 
System  .  err  .  println  (  "---#-#-#-#-#-#-- login --#-#-#-#-#-#---"  )  ; 
} 






public   void   sendDocument  (  Document   doc  )  throws   IOException  { 
try  { 
transformerfactory  .  newTransformer  (  )  .  transform  (  new   DOMSource  (  doc  )  ,  new   StreamResult  (  outputstream  )  )  ; 
ByteArrayOutputStream   temp  =  new   ByteArrayOutputStream  (  )  ; 
transformerfactory  .  newTransformer  (  )  .  transform  (  new   DOMSource  (  doc  )  ,  new   StreamResult  (  temp  )  )  ; 
String   s  =  "Agent -> Server:\n"  +  temp  .  toString  (  )  ; 
logger  .  log  (  Level  .  ALL  ,  s  )  ; 
outputstream  .  write  (  0  )  ; 
outputstream  .  flush  (  )  ; 
}  catch  (  TransformerConfigurationException   e  )  { 
System  .  err  .  println  (  "transformer config error"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
}  catch  (  TransformerException   e  )  { 
System  .  err  .  println  (  "transformer error error"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
} 






public   void   sendPing  (  String   ping  )  throws   IOException  { 
Document   doc  =  null  ; 
try  { 
doc  =  documentbuilderfactory  .  newDocumentBuilder  (  )  .  newDocument  (  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
System  .  err  .  println  (  "parser config error"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
Element   root  =  doc  .  createElement  (  "message"  )  ; 
doc  .  appendChild  (  root  )  ; 
root  .  setAttribute  (  "type"  ,  "ping"  )  ; 
Element   payload  =  doc  .  createElement  (  "payload"  )  ; 
payload  .  setAttribute  (  "value"  ,  ping  )  ; 
root  .  appendChild  (  payload  )  ; 
sendDocument  (  doc  )  ; 
} 
} 

