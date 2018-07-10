package   net  .  sf  .  beenuts  .  net  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  EOFException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  net  .  ConnectException  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Queue  ; 














public   class   Connection   implements   Runnable  { 


public   static   final   int   OBJECT_BASED_CONNECTION  =  1  ; 


public   static   final   int   LINE_BASED_CONNECTION  =  2  ; 


protected   boolean   outerRun  =  true  ; 

private   Socket   mSocket  ; 

private   int   mType  ; 

private   Reader   reader  ; 

private   Writer   writer  ; 


private   Thread   connThread  ; 


private   Thread   readerThread  ; 


private   Thread   writerThread  ; 





private   String   lastError  =  ""  ; 


private   String   ip  ; 


private   short   port  ; 


private   List  <  ConnectionHandler  >  handlers  =  new   LinkedList  <  ConnectionHandler  >  (  )  ; 


private   Queue  <  Object  >  receivedQueue  =  new   LinkedList  <  Object  >  (  )  ; 


private   Queue  <  Object  >  sendQueue  =  new   LinkedList  <  Object  >  (  )  ; 






private   class   Reader   implements   Runnable  { 


private   InputStream   mStream  ; 

public   Reader  (  InputStream   stream  )  { 
mStream  =  stream  ; 
} 

@  Override 
public   void   run  (  )  { 
if  (  mType  ==  OBJECT_BASED_CONNECTION  )  { 
runObjectReader  (  )  ; 
}  else   if  (  mType  ==  LINE_BASED_CONNECTION  )  { 
runLineReader  (  )  ; 
} 
} 




private   void   runLineReader  (  )  { 
BufferedReader   in  ; 
String   line  ; 
try  { 
in  =  new   BufferedReader  (  new   InputStreamReader  (  mSocket  .  getInputStream  (  )  )  )  ; 
while  (  outerRun  )  { 
line  =  in  .  readLine  (  )  ; 
synchronized  (  receivedQueue  )  { 
receivedQueue  .  add  (  line  .  trim  (  )  )  ; 
} 
} 
}  catch  (  SocketException   e  )  { 
outerRun  =  false  ; 
}  catch  (  IOException   e  )  { 
setLastError  (  e  .  getMessage  (  )  )  ; 
outerRun  =  false  ; 
e  .  printStackTrace  (  )  ; 
} 
} 




private   void   runObjectReader  (  )  { 
try  { 
ObjectInputStream   ois  =  new   ObjectInputStream  (  mStream  )  ; 
while  (  outerRun  )  { 
Object   reval  =  ois  .  readObject  (  )  ; 
synchronized  (  receivedQueue  )  { 
receivedQueue  .  add  (  reval  )  ; 
} 
} 
ois  .  close  (  )  ; 
}  catch  (  EOFException   e  )  { 
outerRun  =  false  ; 
}  catch  (  SocketException   e  )  { 
outerRun  =  false  ; 
}  catch  (  IOException   e  )  { 
setLastError  (  e  .  getMessage  (  )  )  ; 
outerRun  =  false  ; 
}  catch  (  ClassNotFoundException   e  )  { 
setLastError  (  e  .  getMessage  (  )  )  ; 
} 
} 
} 






private   class   Writer   implements   Runnable  { 

private   OutputStream   mStream  ; 

public   Writer  (  OutputStream   stream  )  { 
mStream  =  stream  ; 
} 

@  Override 
public   void   run  (  )  { 
if  (  mType  ==  LINE_BASED_CONNECTION  )  { 
PrintWriter   out  ; 
out  =  new   PrintWriter  (  mStream  )  ; 
while  (  outerRun  )  { 
synchronized  (  sendQueue  )  { 
while  (  sendQueue  .  size  (  )  >  0  )  { 
try  { 
String   toSend  =  sendQueue  .  poll  (  )  +  "\n"  ; 
out  .  write  (  toSend  )  ; 
out  .  flush  (  )  ; 
}  catch  (  ClassCastException   ex  )  { 
setLastError  (  "Cant send not string class in LINE_BASE_CONNECTION Mode: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 
} 
try  { 
Thread  .  sleep  (  50  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
}  else   if  (  mType  ==  OBJECT_BASED_CONNECTION  )  { 
try  { 
ObjectOutputStream   oos  =  new   ObjectOutputStream  (  mStream  )  ; 
while  (  outerRun  )  { 
synchronized  (  sendQueue  )  { 
while  (  sendQueue  .  size  (  )  >  0  )  { 
try  { 
Serializable   obj  =  (  Serializable  )  sendQueue  .  poll  (  )  ; 
oos  .  writeObject  (  obj  )  ; 
}  catch  (  ClassCastException   ex  )  { 
setLastError  (  "Cant send object not dereived from Serializable: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 
oos  .  flush  (  )  ; 
try  { 
Thread  .  sleep  (  50  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
oos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
setLastError  (  e  .  getMessage  (  )  )  ; 
outerRun  =  false  ; 
} 
} 
} 
} 








public   Connection  (  Socket   sock  ,  int   type  )  { 
mSocket  =  sock  ; 
this  .  ip  =  sock  .  getInetAddress  (  )  .  getHostAddress  (  )  ; 
init  (  type  )  ; 
} 









public   Connection  (  String   ip  ,  short   port  ,  int   type  )  { 
this  .  ip  =  ip  ; 
this  .  port  =  port  ; 
init  (  type  )  ; 
} 






public   void   addHandler  (  ConnectionHandler   ch  )  { 
handlers  .  add  (  ch  )  ; 
} 






public   boolean   removeHandler  (  ConnectionHandler   ch  )  { 
return   handlers  .  remove  (  ch  )  ; 
} 

public   void   removeAllHandlers  (  )  { 
handlers  .  clear  (  )  ; 
} 

@  Override 
public   String   toString  (  )  { 
return   ip  +  ":"  +  String  .  valueOf  (  port  )  ; 
} 

private   void   init  (  int   type  )  { 
if  (  type  !=  LINE_BASED_CONNECTION  &&  type  !=  OBJECT_BASED_CONNECTION  )  throw   new   IllegalArgumentException  (  "type must be LINE_ or OBJECT_ BASE_CONNECTION"  )  ; 
mType  =  type  ; 
ConnectionController  .  getInstance  (  )  .  registerConnection  (  this  )  ; 
connThread  =  new   Thread  (  this  )  ; 
connThread  .  start  (  )  ; 
} 





protected   void   internalStop  (  )  { 
outerRun  =  false  ; 
try  { 
mSocket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

@  Override 
public   void   run  (  )  { 
while  (  mSocket  ==  null  )  { 
try  { 
if  (  mSocket  ==  null  )  { 
mSocket  =  new   Socket  (  ip  ,  port  )  ; 
} 
}  catch  (  ConnectException   ce  )  { 
try  { 
Thread  .  sleep  (  3000  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
t  .  printStackTrace  (  )  ; 
synchronized  (  lastError  )  { 
setLastError  (  t  .  getMessage  (  )  )  ; 
} 
return  ; 
} 
} 
try  { 
reader  =  new   Reader  (  mSocket  .  getInputStream  (  )  )  ; 
readerThread  =  new   Thread  (  reader  )  ; 
readerThread  .  start  (  )  ; 
writer  =  new   Writer  (  mSocket  .  getOutputStream  (  )  )  ; 
writerThread  =  new   Thread  (  writer  )  ; 
writerThread  .  start  (  )  ; 
}  catch  (  IOException   e1  )  { 
setLastError  (  e1  .  getMessage  (  )  )  ; 
e1  .  printStackTrace  (  )  ; 
} 
} 

void   update  (  )  { 
synchronized  (  receivedQueue  )  { 
while  (  receivedQueue  .  size  (  )  >  0  )  onHandle  (  receivedQueue  .  poll  (  )  )  ; 
} 
synchronized  (  lastError  )  { 
if  (  lastError  !=  ""  )  onError  (  lastError  )  ; 
lastError  =  ""  ; 
} 
if  (  !  outerRun  )  { 
ConnectionController  .  getInstance  (  )  .  unregisterConnection  (  this  )  ; 
onClose  (  )  ; 
} 
} 






protected   void   setLastError  (  String   error  )  { 
synchronized  (  lastError  )  { 
lastError  =  error  !=  null  ?  error  :  ""  ; 
} 
} 






public   void   sendLine  (  String   s  )  { 
synchronized  (  sendQueue  )  { 
sendQueue  .  add  (  s  )  ; 
} 
} 







public   void   sendObject  (  Serializable   object  )  { 
synchronized  (  sendQueue  )  { 
sendQueue  .  add  (  object  )  ; 
} 
} 







protected   void   onError  (  String   error  )  { 
for  (  ConnectionHandler   h  :  handlers  )  h  .  errorOccurred  (  this  ,  error  )  ; 
} 







protected   void   onHandle  (  Object   obj  )  { 
for  (  ConnectionHandler   h  :  handlers  )  h  .  dataReceived  (  this  ,  obj  )  ; 
} 






protected   void   onClose  (  )  { 
for  (  ConnectionHandler   h  :  handlers  )  h  .  connectionClosed  (  this  )  ; 
} 
} 

