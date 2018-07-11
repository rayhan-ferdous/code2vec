package   org  .  labrad  ; 

import   java  .  awt  .  EventQueue  ; 
import   java  .  beans  .  PropertyChangeListener  ; 
import   java  .  beans  .  PropertyChangeSupport  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  concurrent  .  BlockingQueue  ; 
import   java  .  util  .  concurrent  .  Callable  ; 
import   java  .  util  .  concurrent  .  ExecutionException  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   java  .  util  .  concurrent  .  Future  ; 
import   java  .  util  .  concurrent  .  LinkedBlockingQueue  ; 
import   org  .  labrad  .  data  .  Context  ; 
import   org  .  labrad  .  data  .  Data  ; 
import   org  .  labrad  .  data  .  Packet  ; 
import   org  .  labrad  .  data  .  PacketInputStream  ; 
import   org  .  labrad  .  data  .  PacketOutputStream  ; 
import   org  .  labrad  .  data  .  Request  ; 
import   org  .  labrad  .  errors  .  IncorrectPasswordException  ; 
import   org  .  labrad  .  errors  .  LoginFailedException  ; 
import   org  .  labrad  .  events  .  ConnectionListener  ; 
import   org  .  labrad  .  events  .  ConnectionListenerSupport  ; 
import   org  .  labrad  .  events  .  MessageListener  ; 
import   org  .  labrad  .  events  .  MessageListenerSupport  ; 
import   org  .  labrad  .  util  .  LookupProvider  ; 
import   org  .  labrad  .  util  .  Util  ; 






public   class   Client   implements   Connection  ,  Serializable  { 


private   static   final   long   serialVersionUID  =  1L  ; 


private   static   final   String   DEFAULT_NAME  =  "Java Client"  ; 







public   Client  (  )  { 
setName  (  DEFAULT_NAME  )  ; 
setHost  (  Util  .  getEnv  (  "LABRADHOST"  ,  Constants  .  DEFAULT_HOST  )  )  ; 
setPort  (  Util  .  getEnvInt  (  "LABRADPORT"  ,  Constants  .  DEFAULT_PORT  )  )  ; 
setPassword  (  Util  .  getEnv  (  "LABRADPASSWORD"  ,  Constants  .  DEFAULT_PASSWORD  )  )  ; 
connected  =  false  ; 
} 

private   String   name  ; 

private   String   host  ; 

private   int   port  ; 

private   String   password  ; 

private   long   ID  ; 

private   String   loginMessage  ; 

private   boolean   connected  =  false  ; 





public   String   getName  (  )  { 
return   name  ; 
} 





public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 




public   String   getHost  (  )  { 
return   host  ; 
} 





public   void   setHost  (  String   host  )  { 
this  .  host  =  host  ; 
} 




public   int   getPort  (  )  { 
return   port  ; 
} 





public   void   setPort  (  int   port  )  { 
this  .  port  =  port  ; 
} 





public   void   setPassword  (  String   password  )  { 
this  .  password  =  password  ; 
} 





public   long   getId  (  )  { 
return   ID  ; 
} 

private   void   setID  (  long   ID  )  { 
this  .  ID  =  ID  ; 
} 






public   String   getLoginMessage  (  )  { 
return   loginMessage  ; 
} 

private   void   setLoginMessage  (  String   message  )  { 
loginMessage  =  message  ; 
} 





public   boolean   isConnected  (  )  { 
return   connected  ; 
} 

private   void   setConnected  (  boolean   connected  )  { 
boolean   old  =  this  .  connected  ; 
this  .  connected  =  connected  ; 
propertyChangeListeners  .  firePropertyChange  (  "connected"  ,  old  ,  connected  )  ; 
if  (  connected  )  { 
connectionListeners  .  fireConnected  (  )  ; 
}  else  { 
connectionListeners  .  fireDisconnected  (  )  ; 
} 
} 

private   final   PropertyChangeSupport   propertyChangeListeners  =  new   PropertyChangeSupport  (  this  )  ; 

private   final   ConnectionListenerSupport   connectionListeners  =  new   ConnectionListenerSupport  (  this  )  ; 

private   final   MessageListenerSupport   messageListeners  =  new   MessageListenerSupport  (  this  )  ; 





public   void   addPropertyChangeListener  (  PropertyChangeListener   listener  )  { 
propertyChangeListeners  .  addPropertyChangeListener  (  listener  )  ; 
} 





public   void   removePropertyChangeListener  (  PropertyChangeListener   listener  )  { 
propertyChangeListeners  .  removePropertyChangeListener  (  listener  )  ; 
} 





public   void   addMessageListener  (  MessageListener   listener  )  { 
messageListeners  .  addListener  (  listener  )  ; 
} 





public   void   removeMessageListener  (  MessageListener   listener  )  { 
messageListeners  .  removeListener  (  listener  )  ; 
} 





public   void   addConnectionListener  (  ConnectionListener   listener  )  { 
connectionListeners  .  addListener  (  listener  )  ; 
} 





public   void   removeConnectionListener  (  ConnectionListener   listener  )  { 
connectionListeners  .  removeListener  (  listener  )  ; 
} 

private   Socket   socket  ; 

private   Thread   reader  ,  writer  ; 

private   PacketInputStream   inputStream  ; 

private   PacketOutputStream   outputStream  ; 

private   BlockingQueue  <  Packet  >  writeQueue  ; 


private   RequestDispatcher   requestDispatcher  ; 


private   transient   ExecutorService   executor  =  Executors  .  newCachedThreadPool  (  )  ; 


private   LookupProvider   lookupProvider  =  new   LookupProvider  (  this  )  ; 








public   void   connect  (  )  throws   UnknownHostException  ,  IOException  ,  LoginFailedException  ,  IncorrectPasswordException  { 
socket  =  new   Socket  (  host  ,  port  )  ; 
socket  .  setTcpNoDelay  (  true  )  ; 
socket  .  setKeepAlive  (  true  )  ; 
inputStream  =  new   PacketInputStream  (  socket  .  getInputStream  (  )  )  ; 
outputStream  =  new   PacketOutputStream  (  socket  .  getOutputStream  (  )  )  ; 
writeQueue  =  new   LinkedBlockingQueue  <  Packet  >  (  )  ; 
requestDispatcher  =  new   RequestDispatcher  (  writeQueue  )  ; 
reader  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
while  (  !  Thread  .  interrupted  (  )  )  handlePacket  (  inputStream  .  readPacket  (  )  )  ; 
}  catch  (  IOException   e  )  { 
if  (  !  Thread  .  interrupted  (  )  )  close  (  e  )  ; 
} 
} 
}  ,  "Packet Reader Thread"  )  ; 
writer  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
while  (  true  )  { 
Packet   p  =  writeQueue  .  take  (  )  ; 
outputStream  .  writePacket  (  p  )  ; 
} 
}  catch  (  InterruptedException   e  )  { 
}  catch  (  IOException   e  )  { 
close  (  e  )  ; 
} 
} 
}  ,  "Packet Writer Thread"  )  ; 
reader  .  start  (  )  ; 
writer  .  start  (  )  ; 
try  { 
connected  =  true  ; 
doLogin  (  password  )  ; 
}  finally  { 
connected  =  false  ; 
} 
setConnected  (  true  )  ; 
} 







private   void   doLogin  (  String   password  )  throws   LoginFailedException  ,  IncorrectPasswordException  { 
final   long   mgr  =  Constants  .  MANAGER  ; 
Data   data  ,  response  ; 
try  { 
response  =  sendAndWait  (  new   Request  (  mgr  )  )  .  get  (  0  )  ; 
MessageDigest   md  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   RuntimeException  (  "MD5 hash not supported."  )  ; 
} 
byte  [  ]  challenge  =  response  .  getBytes  (  )  ; 
md  .  update  (  challenge  )  ; 
md  .  update  (  password  .  getBytes  (  Data  .  STRING_ENCODING  )  )  ; 
data  =  Data  .  valueOf  (  md  .  digest  (  )  )  ; 
try  { 
response  =  sendAndWait  (  new   Request  (  mgr  )  .  add  (  0  ,  data  )  )  .  get  (  0  )  ; 
}  catch  (  ExecutionException   ex  )  { 
throw   new   IncorrectPasswordException  (  )  ; 
} 
setLoginMessage  (  response  .  getString  (  )  )  ; 
response  =  sendAndWait  (  new   Request  (  mgr  )  .  add  (  0  ,  getLoginData  (  )  )  )  .  get  (  0  )  ; 
setID  (  response  .  getWord  (  )  )  ; 
}  catch  (  InterruptedException   ex  )  { 
throw   new   LoginFailedException  (  ex  )  ; 
}  catch  (  ExecutionException   ex  )  { 
throw   new   LoginFailedException  (  ex  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   LoginFailedException  (  ex  )  ; 
} 
} 

private   Data   getLoginData  (  )  { 
Data   data  =  Data  .  ofType  (  "ws"  )  ; 
data  .  get  (  0  )  .  setWord  (  Constants  .  PROTOCOL  )  ; 
data  .  get  (  1  )  .  setString  (  getName  (  )  )  ; 
return   data  ; 
} 




public   void   close  (  )  { 
close  (  new   IOException  (  "Connection closed."  )  )  ; 
} 





private   synchronized   void   close  (  Throwable   cause  )  { 
if  (  isConnected  (  )  )  { 
setConnected  (  false  )  ; 
executor  .  shutdown  (  )  ; 
requestDispatcher  .  failAll  (  cause  )  ; 
writer  .  interrupt  (  )  ; 
try  { 
writer  .  join  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
reader  .  interrupt  (  )  ; 
try  { 
socket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
try  { 
reader  .  join  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 


private   long   nextContext  =  1L  ; 

private   final   Object   contextLock  =  new   Object  (  )  ; 





public   Context   newContext  (  )  { 
synchronized  (  contextLock  )  { 
return   new   Context  (  0  ,  nextContext  ++  )  ; 
} 
} 






public   Future  <  List  <  Data  >  >  send  (  final   Request   request  )  { 
return   send  (  request  ,  null  )  ; 
} 









public   Future  <  List  <  Data  >  >  send  (  final   Request   request  ,  final   RequestCallback   callback  )  { 
Future  <  List  <  Data  >  >  result  ; 
lookupProvider  .  doLookupsFromCache  (  request  )  ; 
if  (  request  .  needsLookup  (  )  )  { 
result  =  executor  .  submit  (  new   Callable  <  List  <  Data  >  >  (  )  { 

public   List  <  Data  >  call  (  )  throws   Exception  { 
try  { 
lookupProvider  .  doLookups  (  request  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  callback  !=  null  )  callback  .  onFailure  (  request  ,  ex  )  ; 
throw   ex  ; 
} 
return   sendWithoutLookups  (  request  ,  callback  )  .  get  (  )  ; 
} 
}  )  ; 
}  else  { 
result  =  sendWithoutLookups  (  request  ,  callback  )  ; 
} 
return   result  ; 
} 










public   List  <  Data  >  sendAndWait  (  Request   request  )  throws   InterruptedException  ,  ExecutionException  { 
return   send  (  request  )  .  get  (  )  ; 
} 








public   void   sendMessage  (  final   Request   request  )  throws   InterruptedException  ,  ExecutionException  { 
lookupProvider  .  doLookups  (  request  )  ; 
sendMessageWithoutLookups  (  request  )  ; 
} 









private   Future  <  List  <  Data  >  >  sendWithoutLookups  (  final   Request   request  ,  final   RequestCallback   callback  )  { 
if  (  !  isConnected  (  )  )  { 
throw   new   RuntimeException  (  "Not connected."  )  ; 
} 
if  (  request  .  needsLookup  (  )  )  { 
throw   new   RuntimeException  (  "Server and/or setting IDs not looked up!"  )  ; 
} 
return   requestDispatcher  .  startRequest  (  request  ,  callback  )  ; 
} 






private   void   sendMessageWithoutLookups  (  final   Request   request  )  { 
if  (  !  isConnected  (  )  )  { 
throw   new   RuntimeException  (  "Not connected."  )  ; 
} 
if  (  request  .  needsLookup  (  )  )  { 
throw   new   RuntimeException  (  "Server and/or setting IDs not looked up!"  )  ; 
} 
writeQueue  .  add  (  Packet  .  forMessage  (  request  )  )  ; 
} 





private   void   handlePacket  (  final   Packet   packet  )  { 
int   request  =  packet  .  getRequest  (  )  ; 
if  (  request  <  0  )  { 
requestDispatcher  .  finishRequest  (  packet  )  ; 
}  else   if  (  request  ==  0  )  { 
EventQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
messageListeners  .  fireMessage  (  packet  )  ; 
} 
}  )  ; 
}  else  { 
} 
} 












public   static   void   main  (  String  [  ]  args  )  throws   IncorrectPasswordException  ,  LoginFailedException  ,  IOException  ,  ExecutionException  ,  InterruptedException  { 
Data   response  ; 
long   start  ,  end  ; 
int   nEcho  =  5  ; 
int   nRandomData  =  1000  ; 
int   nPings  =  10000  ; 
String   server  =  "Python Test Server"  ; 
String   setting  =  "Get Random Data"  ; 
List  <  Future  <  List  <  Data  >  >  >  requests  =  new   ArrayList  <  Future  <  List  <  Data  >  >  >  (  )  ; 
Client   c  =  new   Client  (  )  ; 
c  .  connect  (  )  ; 
c  .  sendAndWait  (  new   Request  (  server  )  .  add  (  "Echo Delay"  ,  new   Data  (  "v[s]"  )  .  setValue  (  1.0  )  )  )  ; 
System  .  out  .  println  (  "echo with delays..."  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
for  (  int   i  =  0  ;  i  <  nEcho  ;  i  ++  )  { 
requests  .  add  (  c  .  send  (  new   Request  (  server  )  .  add  (  "Delayed Echo"  ,  Data  .  valueOf  (  4L  )  )  )  )  ; 
} 
for  (  Future  <  List  <  Data  >  >  request  :  requests  )  { 
request  .  get  (  )  ; 
System  .  out  .  println  (  "Got one!"  )  ; 
} 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
requests  .  clear  (  )  ; 
System  .  out  .  println  (  "getting random data, with printing..."  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
for  (  int   i  =  0  ;  i  <  nRandomData  ;  i  ++  )  { 
requests  .  add  (  c  .  send  (  new   Request  (  server  )  .  add  (  setting  )  )  )  ; 
} 
for  (  Future  <  List  <  Data  >  >  request  :  requests  )  { 
response  =  request  .  get  (  )  .  get  (  0  )  ; 
System  .  out  .  println  (  "got packet: "  +  response  .  pretty  (  )  )  ; 
} 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
requests  .  clear  (  )  ; 
System  .  out  .  println  (  "getting random data, make pretty, but don't print..."  )  ; 
requests  .  clear  (  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
for  (  int   i  =  0  ;  i  <  nRandomData  ;  i  ++  )  { 
requests  .  add  (  c  .  send  (  new   Request  (  server  )  .  add  (  setting  )  )  )  ; 
} 
for  (  Future  <  List  <  Data  >  >  request  :  requests  )  { 
request  .  get  (  )  .  get  (  0  )  .  pretty  (  )  ; 
} 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
System  .  out  .  println  (  "getting random data, no printing..."  )  ; 
requests  .  clear  (  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
for  (  int   i  =  0  ;  i  <  nRandomData  ;  i  ++  )  { 
requests  .  add  (  c  .  send  (  new   Request  (  server  )  .  add  (  setting  )  )  )  ; 
} 
for  (  Future  <  List  <  Data  >  >  request  :  requests  )  { 
request  .  get  (  )  ; 
} 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
response  =  c  .  sendAndWait  (  new   Request  (  server  )  .  add  (  "debug"  )  )  .  get  (  0  )  ; 
System  .  out  .  println  (  "Debug output: "  +  response  .  pretty  (  )  )  ; 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
System  .  out  .  println  (  "pinging manager "  +  nPings  +  " times..."  )  ; 
requests  .  clear  (  )  ; 
start  =  System  .  currentTimeMillis  (  )  ; 
for  (  int   i  =  0  ;  i  <  nPings  ;  i  ++  )  { 
requests  .  add  (  c  .  send  (  new   Request  (  "Manager"  )  )  )  ; 
} 
for  (  Future  <  List  <  Data  >  >  request  :  requests  )  { 
request  .  get  (  )  ; 
} 
end  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  "done.  elapsed: "  +  (  end  -  start  )  +  " ms."  )  ; 
c  .  close  (  )  ; 
} 
} 

