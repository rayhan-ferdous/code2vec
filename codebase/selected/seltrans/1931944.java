package   tractor  .  client  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  Socket  ; 
import   org  .  newdawn  .  slick  .  AppGameContainer  ; 
import   org  .  newdawn  .  slick  .  SlickException  ; 
import   tractor  .  client  .  handlers  .  IOFactory  ; 
import   tractor  .  client  .  game  .  TractorGame  ; 
import   tractor  .  client  .  game  .  TractorGameContainer  ; 
import   tractor  .  lib  .  MessageFactory  ; 

public   class   Client  { 

public   static   final   int   BEGIN_CONNECT  =  3  ; 

public   static   final   int   CONNECTED  =  4  ; 

public   static   final   int   DISCONNECTED  =  1  ; 

public   static   final   int   DISCONNECTING  =  2  ; 

private   static   Client   instance  ; 

public   static   final   String   ip  =  "208.53.131.251"  ; 

public   static   final   int   NULL  =  0  ; 

public   static   final   int   port  =  9741  ; 

public   static   Client   getInstance  (  )  { 
return   Client  .  instance  ; 
} 

private   ClientView   clientview  ; 

private   int   connectionStatus  ; 

private   ClientError   errorCode  ; 

private   String   errorMsg  ; 

private   IOFactory   io  ; 

private   String   md5  ; 

private   String   username  ; 

private   TractorGame   game  ; 

private   TractorGameContainer   container  ; 

private   Thread   gamethread  ; 

public   static   void   main  (  String  ...  bobby  )  { 
new   Client  (  )  ; 
} 

Client  (  )  { 
Thread  .  setDefaultUncaughtExceptionHandler  (  new   Thread  .  UncaughtExceptionHandler  (  )  { 

@  Override 
public   void   uncaughtException  (  Thread   t  ,  Throwable   e  )  { 
e  .  printStackTrace  (  System  .  out  )  ; 
} 
}  )  ; 
Client  .  instance  =  this  ; 
this  .  io  =  new   IOFactory  (  15000  )  ; 
this  .  clientview  =  new   ClientView  (  )  ; 
this  .  clientview  .  setVisible  (  true  )  ; 
this  .  game  =  null  ; 
} 





public   void   connect  (  boolean   fork  )  { 
this  .  connectionStatus  =  BEGIN_CONNECT  ; 
if  (  fork  )  { 
Thread   connect  =  new   Thread  (  "connect"  )  { 

public   void   run  (  )  { 
Client  .  getInstance  (  )  .  connect  (  false  )  ; 
} 
}  ; 
connect  .  start  (  )  ; 
}  else  { 
System  .  out  .  println  (  "connecting..."  )  ; 
if  (  this  .  isConnected  (  )  )  { 
return  ; 
} 
io  .  reset  (  )  ; 
try  { 
Socket   s  =  new   Socket  (  )  ; 
s  .  setSoTimeout  (  1000  )  ; 
System  .  out  .  println  (  "Establishing Connection"  )  ; 
s  .  connect  (  new   InetSocketAddress  (  Client  .  ip  ,  Client  .  port  )  ,  2000  )  ; 
System  .  out  .  println  (  "Connection Established"  )  ; 
s  .  setSoTimeout  (  15000  )  ; 
s  .  setKeepAlive  (  true  )  ; 
io  .  initIO  (  s  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
this  .  connectionStatus  =  DISCONNECTED  ; 
this  .  setError  (  ClientError  .  CONNECT_SERVER_TIMEOUT  ,  "connect failure: server timeout"  )  ; 
this  .  clientview  .  updateStatusTS  (  )  ; 
} 
while  (  !  this  .  io  .  hasNextMessage  (  MessageFactory  .  LOGIN  )  )  { 
try  { 
Thread  .  sleep  (  250  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
this  .  md5  =  this  .  io  .  getNextMessage  (  MessageFactory  .  LOGIN  )  ; 
this  .  login  (  false  )  ; 
} 
} 





public   IOFactory   getIO  (  )  { 
return   this  .  io  ; 
} 




public   TractorGame   getGame  (  )  { 
return   this  .  game  ; 
} 




public   void   setGame  (  TractorGame   game  )  { 
this  .  game  =  game  ; 
} 




public   void   startGame  (  )  { 
this  .  gamethread  =  new   Thread  (  "game thread"  )  { 

public   void   run  (  )  { 
try  { 
container  =  new   TractorGameContainer  (  game  )  ; 
container  .  setDisplayMode  (  1024  ,  600  ,  false  )  ; 
container  .  setTargetFrameRate  (  30  )  ; 
container  .  setAlwaysRender  (  true  )  ; 
container  .  start  (  )  ; 
}  catch  (  SlickException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
}  ; 
this  .  gamethread  .  start  (  )  ; 
} 




public   void   stopGame  (  )  { 
this  .  container  .  exit  (  )  ; 
} 





public   String   getUsername  (  )  { 
return   username  ; 
} 





public   ClientError   getErrorCode  (  )  { 
return   this  .  errorCode  ; 
} 





public   String   getErrorMessage  (  )  { 
return   this  .  errorMsg  ; 
} 





public   int   getConnectionStatus  (  )  { 
return   this  .  connectionStatus  ; 
} 





public   boolean   isConnected  (  )  { 
return   this  .  io  .  isAlive  (  )  ; 
} 





public   void   login  (  boolean   fork  )  { 
this  .  connectionStatus  =  BEGIN_CONNECT  ; 
if  (  fork  )  { 
Thread   login  =  new   Thread  (  "login"  )  { 

public   void   run  (  )  { 
Client  .  getInstance  (  )  .  login  (  false  )  ; 
} 
}  ; 
login  .  start  (  )  ; 
}  else  { 
this  .  username  =  this  .  clientview  .  getUsername  (  )  ; 
this  .  io  .  write  (  this  .  username  ,  MessageFactory  .  LOGIN  )  ; 
this  .  io  .  write  (  this  .  md5  ,  MessageFactory  .  LOGIN  )  ; 
while  (  this  .  io  .  isAlive  (  )  )  { 
if  (  this  .  io  .  hasNextMessage  (  MessageFactory  .  LOGIN  )  )  { 
char   s  =  this  .  io  .  getNextMessage  (  MessageFactory  .  LOGIN  )  .  charAt  (  0  )  ; 
switch  (  s  )  { 
case  '1'  : 
this  .  connectionStatus  =  CONNECTED  ; 
this  .  clearError  (  )  ; 
this  .  io  .  write  (  "JOIN #lobby"  ,  MessageFactory  .  CHATCMD  )  ; 
this  .  clientview  .  updateStatusTS  (  )  ; 
break  ; 
case  '2'  : 
this  .  connectionStatus  =  DISCONNECTED  ; 
this  .  setError  (  ClientError  .  LOGIN_USERNAME_UNAVAILABLE  ,  "login failure: username unavailable"  )  ; 
this  .  clientview  .  updateStatusTS  (  )  ; 
break  ; 
case  '3'  : 
this  .  connectionStatus  =  DISCONNECTED  ; 
this  .  setError  (  ClientError  .  LOGIN_MD5_MISMATCH  ,  "login failure: md5 mismatch"  )  ; 
this  .  clientview  .  updateStatusTS  (  )  ; 
break  ; 
default  : 
this  .  connectionStatus  =  DISCONNECTED  ; 
this  .  setError  (  ClientError  .  LOGIN_ERRONEOUS_MESSAGE  ,  "login failure: unknown server response"  )  ; 
this  .  clientview  .  updateStatusTS  (  )  ; 
break  ; 
} 
}  else  { 
try  { 
Thread  .  sleep  (  250  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
} 




private   void   clearError  (  )  { 
this  .  errorCode  =  ClientError  .  NO_ERROR  ; 
this  .  errorMsg  =  null  ; 
} 






private   void   setError  (  ClientError   error  ,  String   message  )  { 
this  .  errorCode  =  error  ; 
this  .  errorMsg  =  message  ; 
} 





public   void   setUsername  (  String   username  )  { 
this  .  username  =  username  ; 
} 
} 

