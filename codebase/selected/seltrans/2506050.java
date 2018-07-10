package   org  .  indi  .  server  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  channels  .  ClosedChannelException  ; 
import   java  .  nio  .  channels  .  SelectableChannel  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  PropertyResourceBundle  ; 
import   java  .  util  .  Queue  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  concurrent  .  BlockingQueue  ; 
import   java  .  util  .  concurrent  .  LinkedBlockingQueue  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  indi  .  clientmessages  .  GetProperties  ; 
import   org  .  indi  .  objects  .  TransferType  ; 
import   org  .  indi  .  objects  .  Vector  ; 
import   org  .  indi  .  reactor  .  EventHandler  ; 
import   org  .  indi  .  reactor  .  EventHandlerFactory  ; 
import   org  .  indi  .  reactor  .  QueueHandler  ; 
import   org  .  indi  .  reactor  .  Reactor  ; 
import   org  .  indi  .  reactor  .  TimerHandler  ; 
import   org  .  xml  .  sax  .  SAXException  ; 







public   class   IndiServer   implements   QueueHandler  { 

private   final   Log   log  =  LogFactory  .  getLog  (  IndiServer  .  class  )  ; 




public   Collection  <  DeviceConnection  >  deviceconnections  ; 




public   Collection  <  ClientHandler  >  clientHandlers  ; 




public   Reactor   reactor  ; 




public   String   version  =  "1.0"  ; 





public   Set  <  Observer  >  observers  ; 




private   Acceptor   acceptor  ; 




private   Dispatcher   dispatcher  ; 




private   BlockingQueue  <  Object  >  deviceToServerQueue  ; 

public   IndiServer  (  )  throws   IOException  { 
init  (  new   Reactor  (  )  )  ; 
} 






public   IndiServer  (  Reactor   r  )  throws   IOException  { 
init  (  r  )  ; 
} 

public   void   init  (  Reactor   r  )  throws   IOException  { 
this  .  observers  =  new   HashSet  <  Observer  >  (  )  ; 
this  .  reactor  =  r  ; 
Queue  <  Object  >  toDriverQueue  =  new   LinkedBlockingQueue  <  Object  >  (  )  ; 
this  .  deviceconnections  =  new   ArrayList  <  DeviceConnection  >  (  )  ; 
this  .  dispatcher  =  new   Dispatcher  (  this  )  ; 
this  .  acceptor  =  new   Acceptor  (  this  .  reactor  ,  new   EventHandlerFactory  (  )  { 

public   EventHandler   produce  (  Reactor   r  ,  SelectableChannel   ch  )  throws   ClosedChannelException  ,  IOException  { 
try  { 
return   new   ClientHandler  (  reactor  ,  ch  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
log  .  error  (  "Could not start indi server"  ,  e  )  ; 
}  catch  (  SAXException   e  )  { 
log  .  error  (  "Could not start indi server"  ,  e  )  ; 
} 
return   null  ; 
} 
}  ,  7624  ,  this  )  ; 
deviceToServerQueue  =  new   LinkedBlockingQueue  <  Object  >  (  )  ; 
reactor  .  register  (  this  )  ; 
this  .  clientHandlers  =  acceptor  .  getClientHandlers  (  )  ; 
} 

public   BlockingQueue  <  Object  >  getQueue  (  )  { 
return   deviceToServerQueue  ; 
} 







public   void   addDevice  (  Device   device  )  { 
DeviceConnection   c  =  new   ThreadedDeviceConnection  (  device  ,  this  )  ; 
device  .  setConnection  (  c  )  ; 
this  .  deviceconnections  .  add  (  c  )  ; 
} 






public   void   bogusDevice  (  BogusDevice   d  )  { 
this  .  log  .  error  (  "Error in Driver "  +  d  .  connection  .  getDevice  (  )  .  getClass  (  )  .  getName  (  )  +  "\""  ,  d  .  exception  )  ; 
this  .  deviceconnections  .  remove  (  d  .  connection  )  ; 
} 











public   void   sendToClients  (  org  .  indi  .  objects  .  Object   object  ,  TransferType   type  ,  String   message  )  { 
for  (  ClientHandler   ch  :  this  .  clientHandlers  )  { 
ch  .  send  (  object  ,  type  ,  message  )  ; 
} 
if  (  object   instanceof   Vector  )  { 
Vector   vector  =  (  Vector  )  object  ; 
vector  .  setTransferType  (  type  )  ; 
for  (  Observer   o  :  this  .  observers  )  { 
if  (  vector  .  getDevice  (  )  .  equals  (  o  .  getDevice  (  )  )  )  { 
if  (  o  .  getName  (  )  .  equals  (  vector  .  getName  (  )  )  ||  vector  .  getTransferType  (  )  ==  TransferType  .  Del  )  { 
if  (  (  vector  .  getTransferType  (  )  ==  TransferType  .  Set  )  &&  o  .  getState  (  )  ==  ObserverState  .  State  )  { 
if  (  o  .  laststate  ==  vector  .  getState  (  )  )  { 
return  ; 
}  else  { 
o  .  laststate  =  vector  .  getState  (  )  ; 
} 
} 
o  .  onObserved  (  vector  )  ; 
} 
} 
; 
} 
} 
} 

public   void   shutDown  (  )  { 
for  (  ClientHandler   ch  :  this  .  clientHandlers  )  { 
ch  .  shutDown  (  )  ; 
} 
try  { 
this  .  acceptor  .  channel  (  )  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
log  .  error  (  "could not close connection on server shutdown."  ,  e  )  ; 
} 
} 








public   static   void   main  (  String  [  ]  args  )  { 
try  { 
new   IndiServer  (  )  .  startServer  (  args  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 








public   void   startServer  (  String  [  ]  args  )  { 
try  { 
ArrayList  <  String  >  deveiceToStart  =  new   ArrayList  <  String  >  (  )  ; 
nameScanForDevicesToStart  (  args  ,  deveiceToStart  )  ; 
classNameScanForDevicesToStart  (  args  ,  deveiceToStart  )  ; 
if  (  !  startAtLeastOneDevice  (  deveiceToStart  )  )  { 
this  .  log  .  error  (  "no drivers successfully started!"  )  ; 
}  else  { 
handleEvents  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
this  .  log  .  error  (  "could not start indi server due to exception"  ,  e  )  ; 
} 
} 

protected   void   handleEvents  (  )  throws   IOException  { 
while  (  true  )  { 
this  .  reactor  .  handleEvents  (  10  )  ; 
} 
} 

private   void   classNameScanForDevicesToStart  (  String  [  ]  args  ,  ArrayList  <  String  >  devicesToStart  )  { 
for  (  String   element  :  args  )  { 
if  (  element  .  length  (  )  >  0  )  { 
try  { 
Class   driverClass  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  .  loadClass  (  element  )  ; 
if  (  driverClass  .  getClass  (  )  .  getSuperclass  (  )  .  isAssignableFrom  (  BasicDevice  .  class  )  )  { 
devicesToStart  .  add  (  element  )  ; 
}  else  { 
this  .  log  .  error  (  "argument is not a driver or driver class ("  +  element  +  ")"  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
this  .  log  .  error  (  "argument is not a driver class ("  +  element  +  ")"  ,  e  )  ; 
} 
} 
} 
} 

private   void   nameScanForDevicesToStart  (  String  [  ]  args  ,  ArrayList  <  String  >  driversToStart  )  throws   IOException  { 
Enumeration  <  URL  >  driverresources  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  .  getResources  (  "META-INF/indi"  )  ; 
while  (  driverresources  .  hasMoreElements  (  )  )  { 
URL   url  =  driverresources  .  nextElement  (  )  ; 
PropertyResourceBundle   bundle  =  new   PropertyResourceBundle  (  url  .  openStream  (  )  )  ; 
Enumeration  <  String  >  keys  =  bundle  .  getKeys  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
String   driverClassName  =  keys  .  nextElement  (  )  ; 
String   driverName  =  bundle  .  getString  (  driverClassName  )  ; 
boolean   driverActivated  =  false  ; 
for  (  int   index  =  0  ;  index  <  args  .  length  ;  index  ++  )  { 
if  (  args  [  index  ]  .  equals  (  driverName  )  )  { 
driversToStart  .  add  (  driverClassName  )  ; 
driverActivated  =  true  ; 
args  [  index  ]  =  ""  ; 
} 
} 
if  (  !  driverActivated  )  { 
this  .  log  .  info  (  "detected deacivated driver for "  +  driverName  +  " ("  +  driverClassName  +  ")"  )  ; 
}  else  { 
this  .  log  .  info  (  "detected acivated driver for "  +  driverName  +  " ("  +  driverClassName  +  ")"  )  ; 
} 
} 
} 
} 






private   boolean   startAtLeastOneDevice  (  ArrayList  <  String  >  driversToStart  )  { 
boolean   atLeastOnDriverStarted  =  false  ; 
for  (  String   driverClassName  :  driversToStart  )  { 
try  { 
Class   driverClass  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  .  loadClass  (  driverClassName  )  ; 
addDevice  (  (  BasicDevice  )  driverClass  .  getConstructor  (  )  .  newInstance  (  )  )  ; 
this  .  log  .  info  (  "Successfuly started driver class "  +  driverClassName  )  ; 
atLeastOnDriverStarted  =  true  ; 
}  catch  (  Exception   e  )  { 
this  .  log  .  error  (  "could not instanciate Driver: "  +  driverClassName  +  " due to exception"  ,  e  )  ; 
} 
} 
return   atLeastOnDriverStarted  ; 
} 

public   Dispatcher   getDispatcher  (  )  { 
return   dispatcher  ; 
} 

public   void   bogusClient  (  ClientHandler   client  ,  Exception   e  )  { 
this  .  log  .  error  (  "Error while reading or processing data from client "  ,  e  )  ; 
reactor  .  unregister  (  client  )  ; 
this  .  clientHandlers  .  remove  (  client  )  ; 
} 

public   void   onRead  (  Object   input  )  { 
if  (  input   instanceof   BogusDevice  )  { 
bogusDevice  (  (  BogusDevice  )  input  )  ; 
} 
if  (  input   instanceof   IndiTransfer  )  { 
IndiTransfer   t  =  (  IndiTransfer  )  input  ; 
sendToClients  (  t  .  object  ,  t  .  type  ,  t  .  message  )  ; 
} 
} 
} 

