package   edu  .  jas  .  util  ; 

import   java  .  io  .  IOException  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ArrayList  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  log4j  .  BasicConfigurator  ; 





public   class   ExecutableServer   extends   Thread  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  ExecutableServer  .  class  )  ; 

private   final   boolean   debug  =  logger  .  isDebugEnabled  (  )  ; 




protected   final   ChannelFactory   cf  ; 




protected   List  <  Executor  >  servers  =  null  ; 




public   static   final   int   DEFAULT_PORT  =  7411  ; 




public   static   final   String   DONE  =  "Done"  ; 




public   static   final   String   STOP  =  "Stop"  ; 

private   volatile   boolean   goon  =  true  ; 

private   Thread   mythread  =  null  ; 




public   ExecutableServer  (  )  { 
this  (  DEFAULT_PORT  )  ; 
} 





public   ExecutableServer  (  int   port  )  { 
this  (  new   ChannelFactory  (  port  )  )  ; 
} 





public   ExecutableServer  (  ChannelFactory   cf  )  { 
this  .  cf  =  cf  ; 
cf  .  init  (  )  ; 
servers  =  new   ArrayList  <  Executor  >  (  )  ; 
} 





public   static   void   main  (  String  [  ]  args  )  { 
BasicConfigurator  .  configure  (  )  ; 
int   port  =  DEFAULT_PORT  ; 
if  (  args  .  length  <  1  )  { 
System  .  out  .  println  (  "Usage: ExecutableServer <port>"  )  ; 
}  else  { 
try  { 
port  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
logger  .  info  (  "ExecutableServer at port "  +  port  )  ; 
(  new   ExecutableServer  (  port  )  )  .  run  (  )  ; 
} 




public   void   init  (  )  { 
this  .  start  (  )  ; 
logger  .  info  (  "ExecutableServer at "  +  cf  )  ; 
} 




public   int   size  (  )  { 
return   servers  .  size  (  )  ; 
} 




@  Override 
public   void   run  (  )  { 
SocketChannel   channel  =  null  ; 
Executor   s  =  null  ; 
mythread  =  Thread  .  currentThread  (  )  ; 
while  (  goon  )  { 
if  (  debug  )  { 
logger  .  info  (  "execute server "  +  this  +  " go on"  )  ; 
} 
try  { 
channel  =  cf  .  getChannel  (  )  ; 
logger  .  debug  (  "execute channel = "  +  channel  )  ; 
if  (  mythread  .  isInterrupted  (  )  )  { 
goon  =  false  ; 
logger  .  debug  (  "execute server "  +  this  +  " interrupted"  )  ; 
channel  .  close  (  )  ; 
}  else  { 
s  =  new   Executor  (  channel  )  ; 
if  (  goon  )  { 
servers  .  add  (  s  )  ; 
s  .  start  (  )  ; 
logger  .  debug  (  "server "  +  s  +  " started"  )  ; 
}  else  { 
s  =  null  ; 
channel  .  close  (  )  ; 
} 
} 
}  catch  (  InterruptedException   e  )  { 
goon  =  false  ; 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  debug  )  { 
logger  .  info  (  "execute server "  +  this  +  " terminated"  )  ; 
} 
} 




public   void   terminate  (  )  { 
goon  =  false  ; 
logger  .  debug  (  "terminating ExecutableServer"  )  ; 
if  (  cf  !=  null  )  cf  .  terminate  (  )  ; 
if  (  servers  !=  null  )  { 
Iterator  <  Executor  >  it  =  servers  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Executor   x  =  it  .  next  (  )  ; 
if  (  x  .  channel  !=  null  )  { 
x  .  channel  .  close  (  )  ; 
} 
try  { 
while  (  x  .  isAlive  (  )  )  { 
x  .  interrupt  (  )  ; 
x  .  join  (  100  )  ; 
} 
logger  .  debug  (  "server "  +  x  +  " terminated"  )  ; 
}  catch  (  InterruptedException   e  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
} 
} 
servers  =  null  ; 
} 
logger  .  debug  (  "Executors terminated"  )  ; 
if  (  mythread  ==  null  )  return  ; 
try  { 
while  (  mythread  .  isAlive  (  )  )  { 
mythread  .  interrupt  (  )  ; 
mythread  .  join  (  100  )  ; 
} 
}  catch  (  InterruptedException   e  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
} 
mythread  =  null  ; 
logger  .  debug  (  "ExecuteServer terminated"  )  ; 
} 
} 




class   Executor   extends   Thread  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  Executor  .  class  )  ; 

private   final   boolean   debug  =  logger  .  isInfoEnabled  (  )  ; 

protected   final   SocketChannel   channel  ; 

Executor  (  SocketChannel   s  )  { 
channel  =  s  ; 
} 




@  Override 
public   void   run  (  )  { 
Object   o  ; 
RemoteExecutable   re  =  null  ; 
String   d  ; 
boolean   goon  =  true  ; 
logger  .  debug  (  "executor started "  +  this  )  ; 
while  (  goon  )  { 
try  { 
o  =  channel  .  receive  (  )  ; 
logger  .  info  (  "receive: "  +  o  +  " from "  +  channel  )  ; 
if  (  this  .  isInterrupted  (  )  )  { 
goon  =  false  ; 
}  else  { 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "receive: "  +  o  +  " from "  +  channel  )  ; 
} 
if  (  o   instanceof   String  )  { 
d  =  (  String  )  o  ; 
if  (  ExecutableServer  .  STOP  .  equals  (  d  )  )  { 
goon  =  false  ; 
channel  .  send  (  ExecutableServer  .  DONE  )  ; 
}  else  { 
goon  =  false  ; 
channel  .  send  (  ExecutableServer  .  DONE  )  ; 
} 
} 
if  (  o   instanceof   RemoteExecutable  )  { 
re  =  (  RemoteExecutable  )  o  ; 
if  (  debug  )  { 
logger  .  info  (  "running "  +  re  )  ; 
} 
try  { 
re  .  run  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
logger  .  info  (  "Exception on re.run()"  ,  e  )  ; 
}  finally  { 
logger  .  info  (  "finally re.run() "  +  re  )  ; 
} 
if  (  debug  )  { 
logger  .  info  (  "finished "  +  re  )  ; 
} 
if  (  this  .  isInterrupted  (  )  )  { 
goon  =  false  ; 
}  else  { 
channel  .  send  (  ExecutableServer  .  DONE  )  ; 
logger  .  info  (  "finished send "  +  ExecutableServer  .  DONE  )  ; 
} 
} 
} 
}  catch  (  IOException   e  )  { 
goon  =  false  ; 
logger  .  info  (  "IOException "  ,  e  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
goon  =  false  ; 
e  .  printStackTrace  (  )  ; 
logger  .  info  (  "ClassNotFoundException "  ,  e  )  ; 
}  finally  { 
logger  .  info  (  "finally "  +  this  )  ; 
} 
} 
logger  .  info  (  "executor terminated "  +  this  )  ; 
channel  .  close  (  )  ; 
} 
} 

