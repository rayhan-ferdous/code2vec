package   net  .  nutss  .  stunt  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   net  .  nutss  .  *  ; 
import   static   net  .  nutss  .  Control  .  log  ; 
import   static   net  .  nutss  .  stunt  .  STUNTMessage  .  Stage  .  *  ; 










































class   STUNTCont   implements   Continuation  { 




private   static   final   int   STUNT_MAGIC  =  0x5ca1ab1e  ; 





private   static   final   int   T_RETRY  =  4000  ; 




private   static   final   int   T_EXPIRE  =  16000  ; 




private   static   final   int   T_SHORT  =  2000  ; 





private   static   final   int   MAX_ATTEMPTS  =  1  ; 




private   Exception   reason  =  null  ; 




private   boolean   isActive  =  false  ; 





private   enum   State  { 


A_START  (  0  )  , 



A_ST1  (  T_EXPIRE  ,  3  )  , 



A_ST2  (  T_EXPIRE  ,  3  )  ,  A_INV_SENT  (  T_EXPIRE  ,  6  )  , 




A_INV_ACKD  (  T_SHORT  )  , 




A_CB_SENT  (  T_RETRY  ,  T_EXPIRE  /  T_RETRY  )  , 



A_ECHO  (  T_RETRY  )  , 



A_DONE  (  T_RETRY  )  , 



A_SWAP  (  T_RETRY  ,  2  *  T_EXPIRE  /  T_RETRY  )  , 
P_START  (  0  )  , 




P_ST1  (  T_EXPIRE  ,  3  )  , 



P_ST2  (  T_EXPIRE  ,  3  )  , 





P_INV_ACKD  (  T_EXPIRE  ,  6  )  , 



P_CB_RCVD  (  T_EXPIRE  )  , 



P_ECHO  (  T_RETRY  )  , 



P_DONE  (  T_RETRY  )  , 




P_SWAP  (  T_RETRY  ,  2  *  T_EXPIRE  /  T_RETRY  )  , 





STOP  (  -  1  )  ; 




private   final   int   timeout  ; 





private   final   int   retries  ; 









State  (  int   timeout  ,  int   retries  )  { 
this  .  timeout  =  timeout  ; 
this  .  retries  =  retries  ; 
} 








State  (  int   timeout  )  { 
this  (  timeout  ,  1  )  ; 
} 






int   getTimeout  (  )  { 
return   timeout  ; 
} 






int   getRetries  (  )  { 
return   retries  ; 
} 
} 

; 




private   State   mState  ; 

private   int   iTryed  =  0  ; 




private   int   tries  ; 





private   URI   id  ; 





URI   dst  ; 




private   SignalingContext   sig  ; 






private   SocketAddress  [  ]  responders  ; 




private   SignalingMessage   msg  ; 




private   STUNTSocket   asock  ; 




private   STUNTSocket   bsock  ; 




private   STUNTSocket   fsock  ; 





private   InetSocketAddress  [  ]  eaddrs  =  new   InetSocketAddress  [  2  ]  ; 




private   SocketAddress   iaddr  ; 




private   STUNTServerSocket   psock  ; 





private   STUNTMessage   stuntState  =  new   STUNTMessage  (  )  ; 





private   STUNTMessage   peerState  ; 





private   int   nonce  ; 






private   int   swapped  ; 




ByteBuffer   outbuf  =  ByteBuffer  .  allocate  (  4  )  ; 




ByteBuffer   inbuf  =  ByteBuffer  .  allocateDirect  (  4  )  ; 





boolean   established  ; 













STUNTCont  (  URI   dst  ,  URI   id  ,  SignalingContext   context  ,  SocketAddress  [  ]  responders  )  { 
this  .  sig  =  context  ; 
this  .  dst  =  dst  ; 
this  .  id  =  id  ; 
this  .  responders  =  responders  ; 
} 









public   SignalingMessage   init  (  SignalingMessage   msg  )  { 
if  (  msg  ==  null  )  { 
setState  (  State  .  A_START  )  ; 
this  .  msg  =  sig  .  createMessage  (  id  ,  dst  ,  stuntState  )  ; 
isActive  =  true  ; 
return   this  .  msg  ; 
}  else  { 
setState  (  State  .  P_START  )  ; 
peerState  =  (  STUNTMessage  )  msg  .  getPayload  (  )  ; 
this  .  msg  =  sig  .  createReply  (  msg  ,  stuntState  )  ; 
isActive  =  false  ; 
return   this  .  msg  ; 
} 
} 







private   boolean   resend  (  )  throws   IOException  { 
if  (  tries  >=  mState  .  getRetries  (  )  )  return   false  ; 
tries  ++  ; 
sig  .  send  (  msg  )  ; 
return   true  ; 
} 







private   void   send  (  STUNTMessage   payload  )  throws   IOException  { 
tries  =  0  ; 
msg  =  sig  .  setMessage  (  msg  ,  payload  )  ; 
resend  (  )  ; 
} 
















private   STUNTSocket   getNBConnSock  (  SocketAddress   local  ,  SocketAddress   remote  ,  STUNTSelector   sel  ,  boolean   bConnectAsEvent  )  { 
try  { 
Socket   sock  =  new   Socket  (  )  ; 
sock  .  setReuseAddress  (  true  )  ; 
if  (  local  !=  null  )  sock  .  bind  (  local  )  ; 
STUNTSocket   s  =  new   STUNTSocket  (  sock  ,  this  ,  sel  ,  remote  ,  bConnectAsEvent  )  ; 
s  .  start  (  )  ; 
return   s  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 













private   STUNTServerSocket   getNBLstnSock  (  SocketAddress   local  ,  STUNTSelector   sel  )  throws   Exception  { 
ServerSocket   sock  =  new   ServerSocket  (  )  ; 
sock  .  setReuseAddress  (  true  )  ; 
sock  .  bind  (  local  )  ; 
STUNTServerSocket   sv  =  new   STUNTServerSocket  (  sock  ,  this  ,  sel  )  ; 
sv  .  start  (  )  ; 
return   sv  ; 
} 









private   InetSocketAddress   predict  (  )  { 
return   new   InetSocketAddress  (  eaddrs  [  0  ]  .  getAddress  (  )  ,  2  *  eaddrs  [  1  ]  .  getPort  (  )  -  eaddrs  [  0  ]  .  getPort  (  )  )  ; 
} 










private   Object   readOne  (  STUNTSocket   sock  )  throws   Exception  { 
Object   o  =  new   ObjectInputStream  (  sock  .  is  )  .  readObject  (  )  ; 
sock  .  close  (  )  ; 
return   o  ; 
} 










private   int   readInt  (  STUNTSocket   sock  )  throws   Exception  { 
try  { 
return   sock  .  readInt  (  )  ; 
}  catch  (  Exception   e  )  { 
abort  (  e  )  ; 
return  -  1  ; 
} 
} 











private   void   writeInt  (  STUNTSocket   sock  ,  int   val  )  throws   Exception  { 
try  { 
sock  .  writeInt  (  val  )  ; 
}  catch  (  Exception   e  )  { 
abort  (  e  )  ; 
} 
} 











private   void   verifyConnected  (  STUNTSocket   fsock  )  throws   Exception  { 
nonce  =  (  int  )  (  Math  .  random  (  )  *  Integer  .  MAX_VALUE  )  ; 
writeInt  (  fsock  ,  nonce  )  ; 
} 







private   void   reset  (  )  { 
closeSocket  (  ASOCK  +  BSOCK  +  PSOCK  +  FSOCK  )  ; 
peerState  =  null  ; 
} 








private   void   abortFatal  (  Exception   e  )  throws   Exception  { 
reason  =  e  ; 
log  .  throwing  (  "STUNTCont"  ,  "abortFatal"  ,  e  )  ; 
throw   new   Exception  (  "STUNT attempt aborted."  )  ; 
} 










private   void   abort  (  Exception   e  )  throws   Exception  { 
reason  =  e  ; 
log  .  throwing  (  "STUNTCont"  ,  "abort"  ,  e  )  ; 
throw   new   IllegalStateException  (  "STUNT attempt aborted."  )  ; 
} 









private   void   setState  (  State   st  )  { 
if  (  st  !=  mState  )  { 
iTryed  =  0  ; 
mState  =  st  ; 
} 
iTryed  ++  ; 
} 

private   void   handleTimeout  (  STUNTSelector   sel  )  throws   Exception  { 
switch  (  mState  )  { 
case   A_ST1  : 
if  (  iTryed  >  mState  .  getRetries  (  )  )  { 
abort  (  new   SocketTimeoutException  (  "TCP connect to stunt signal server failed!"  )  )  ; 
break  ; 
} 
case   A_START  : 
closeSocket  (  ASOCK  )  ; 
asock  =  getNBConnSock  (  null  ,  responders  [  0  ]  ,  sel  ,  false  )  ; 
setState  (  State  .  A_ST1  )  ; 
log  .  finest  (  "Attempting first STUNT lookup."  )  ; 
break  ; 
case   P_ST2  : 
case   A_ST2  : 
if  (  iTryed  >  mState  .  getRetries  (  )  )  { 
abort  (  new   SocketTimeoutException  (  "TCP connect to stunt signal server failed!"  )  )  ; 
break  ; 
} 
closeSocket  (  ASOCK  )  ; 
asock  =  getNBConnSock  (  iaddr  ,  responders  [  1  ]  ,  sel  ,  false  )  ; 
setState  (  mState  )  ; 
break  ; 
case   P_ST1  : 
if  (  iTryed  >  mState  .  getRetries  (  )  )  { 
abort  (  new   SocketTimeoutException  (  "TCP connect to stunt signal server failed!"  )  )  ; 
break  ; 
} 
case   P_START  : 
closeSocket  (  ASOCK  +  BSOCK  )  ; 
bsock  =  getNBConnSock  (  null  ,  peerState  .  pred  ,  sel  ,  true  )  ; 
asock  =  getNBConnSock  (  null  ,  responders  [  0  ]  ,  sel  ,  false  )  ; 
setState  (  State  .  P_ST1  )  ; 
log  .  finest  (  "Passive client attemting first STUNT lookup and direct connect."  )  ; 
break  ; 
case   A_INV_SENT  : 
if  (  !  resend  (  )  )  abort  (  new   SocketTimeoutException  (  "Connect Timeout"  )  )  ; 
break  ; 
case   P_CB_RCVD  : 
abort  (  new   SocketTimeoutException  (  "Connect Timeout"  )  )  ; 
break  ; 
case   P_INV_ACKD  : 
case   A_CB_SENT  : 
case   A_SWAP  : 
case   P_SWAP  : 
if  (  !  resend  (  )  )  abort  (  new   SocketTimeoutException  (  "Listen Timeout"  )  )  ; 
break  ; 
case   A_INV_ACKD  : 
closeSocket  (  BSOCK  +  PSOCK  )  ; 
psock  =  getNBLstnSock  (  iaddr  ,  sel  )  ; 
setState  (  State  .  A_CB_SENT  )  ; 
send  (  stuntState  .  callback  (  )  )  ; 
log  .  finest  (  "Direct connect timed out. Sending callback request."  )  ; 
break  ; 
default  : 
log  .  fine  (  "Unhandled timeout in state: "  +  mState  )  ; 
break  ; 
} 
} 











void   handleSocketOp  (  Object   obj  ,  STUNTSelector   sel  )  throws   Exception  { 
STUNTSocket   so  =  null  ; 
STUNTServerSocket   ss  =  null  ; 
if  (  obj   instanceof   STUNTSocket  )  so  =  (  STUNTSocket  )  obj  ; 
if  (  obj   instanceof   STUNTServerSocket  )  ss  =  (  STUNTServerSocket  )  obj  ; 
if  (  so  !=  null  &&  so  ==  bsock  )  { 
if  (  bsock  .  isConnected  (  )  )  { 
fsock  =  bsock  ; 
bsock  =  null  ; 
closeSocket  (  ASOCK  )  ; 
verifyConnected  (  fsock  )  ; 
setState  (  isActive  ?  State  .  A_ECHO  :  State  .  P_ECHO  )  ; 
log  .  finest  (  "Direct connect succeeded. Verifying."  )  ; 
return  ; 
} 
} 
if  (  ss  !=  null  &&  ss  ==  psock  )  { 
fsock  =  psock  .  accept  (  )  ; 
if  (  fsock  !=  null  )  { 
closeSocket  (  PSOCK  )  ; 
verifyConnected  (  fsock  )  ; 
setState  (  isActive  ?  State  .  A_ECHO  :  State  .  P_ECHO  )  ; 
log  .  finest  (  "Accepted direct connect, verifying."  )  ; 
} 
} 
switch  (  mState  )  { 
case   P_ST1  : 
case   P_ST2  : 
case   A_INV_ACKD  : 
case   P_CB_RCVD  : 
if  (  so  !=  null  &&  so  ==  bsock  )  { 
if  (  !  bsock  .  isConnected  (  )  )  { 
closeSocket  (  BSOCK  )  ; 
log  .  finest  (  "Direct connect failed."  )  ; 
if  (  mState  ==  State  .  P_CB_RCVD  )  abort  (  new   SocketTimeoutException  (  "Connect Timeout"  )  )  ; 
} 
return  ; 
} 
} 
switch  (  mState  )  { 
case   P_ST1  : 
case   A_ST1  : 
if  (  so  !=  null  &&  so  ==  asock  )  { 
if  (  asock  .  isConnected  (  )  )  { 
iaddr  =  asock  .  s  .  getLocalSocketAddress  (  )  ; 
eaddrs  [  0  ]  =  (  InetSocketAddress  )  readOne  (  asock  )  ; 
asock  =  getNBConnSock  (  iaddr  ,  responders  [  1  ]  ,  sel  ,  false  )  ; 
setState  (  isActive  ?  State  .  A_ST2  :  State  .  P_ST2  )  ; 
log  .  finest  (  "First STUNT lookup successful("  +  iaddr  +  "->"  +  eaddrs  [  0  ]  +  "), attemting second STUNT lookup."  )  ; 
}  else  { 
abort  (  new   UnknownHostException  (  "Failed contacting STUNT responder #1"  )  )  ; 
} 
} 
break  ; 
case   P_ST2  : 
case   A_ST2  : 
if  (  so  !=  null  &&  so  ==  asock  )  { 
if  (  asock  .  isConnected  (  )  )  { 
eaddrs  [  1  ]  =  (  InetSocketAddress  )  readOne  (  asock  )  ; 
asock  =  null  ; 
psock  =  getNBLstnSock  (  iaddr  ,  sel  )  ; 
log  .  finest  (  "Second STUNT lookup successful("  +  iaddr  +  "->"  +  eaddrs  [  1  ]  +  ")."  )  ; 
if  (  isActive  )  { 
setState  (  State  .  A_INV_SENT  )  ; 
send  (  stuntState  .  invite  (  predict  (  )  )  )  ; 
log  .  finest  (  "Active client inviting passive, listening for direct connect."  )  ; 
}  else  { 
setState  (  State  .  P_INV_ACKD  )  ; 
send  (  stuntState  .  accept  (  predict  (  )  )  )  ; 
log  .  finest  (  "Passive client accepting inviting, listening for direct connect."  )  ; 
} 
}  else  { 
abort  (  new   UnknownHostException  (  "Failed contacting STUNT responder #2"  )  )  ; 
} 
} 
break  ; 
case   P_ECHO  : 
case   A_ECHO  : 
if  (  so  !=  null  &&  so  ==  fsock  )  { 
writeInt  (  fsock  ,  readInt  (  fsock  )  ^  STUNT_MAGIC  ^  id  .  hashCode  (  )  )  ; 
setState  (  isActive  ?  State  .  A_DONE  :  State  .  P_DONE  )  ; 
log  .  finest  (  "Echoed peer's half-pipe check, checking half-pipe."  )  ; 
} 
break  ; 
case   P_DONE  : 
case   A_DONE  : 
if  (  so  !=  null  &&  so  ==  fsock  )  { 
if  (  (  readInt  (  fsock  )  ^  STUNT_MAGIC  ^  dst  .  hashCode  (  )  )  ==  nonce  )  { 
setState  (  State  .  STOP  )  ; 
established  =  true  ; 
fsock  .  stopSelect  (  )  ; 
log  .  finest  (  "Connection is full-pipe and verified. Success!!!"  )  ; 
}  else  { 
abort  (  new   ProtocolException  (  "Connection verification failed."  )  )  ; 
} 
} 
break  ; 
default  : 
log  .  fine  (  "Unhandled STUNTSocket event: "  +  obj  .  toString  (  )  )  ; 
if  (  so  !=  null  )  { 
so  .  close  (  )  ; 
if  (  so  ==  fsock  )  fsock  =  null  ;  else   if  (  so  ==  asock  )  asock  =  null  ;  else   if  (  so  ==  bsock  )  bsock  =  null  ; 
} 
if  (  ss  !=  null  )  { 
ss  .  close  (  )  ; 
if  (  ss  ==  psock  )  psock  =  null  ; 
} 
break  ; 
} 
} 

static   final   int   BSOCK  =  1  ; 

static   final   int   PSOCK  =  2  ; 

static   final   int   FSOCK  =  4  ; 

static   final   int   ASOCK  =  8  ; 

void   closeSocket  (  int   iType  )  { 
try  { 
if  (  (  iType  &  BSOCK  )  >  0  &&  bsock  !=  null  )  { 
bsock  .  close  (  )  ; 
bsock  =  null  ; 
} 
if  (  (  iType  &  ASOCK  )  >  0  &&  asock  !=  null  )  { 
asock  .  close  (  )  ; 
asock  =  null  ; 
} 
if  (  (  iType  &  FSOCK  )  >  0  &&  fsock  !=  null  )  { 
fsock  .  close  (  )  ; 
fsock  =  null  ; 
} 
if  (  (  iType  &  PSOCK  )  >  0  &&  psock  !=  null  )  { 
psock  .  close  (  )  ; 
psock  =  null  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 











void   handleProxyMsg  (  SignalingMessage   msg  ,  STUNTSelector   sel  )  throws   Exception  { 
STUNTMessage   msgState  =  msg  .  hasPayload  (  )  ?  (  STUNTMessage  )  msg  .  getPayload  (  )  :  null  ; 
if  (  msgState  !=  null  &&  msgState  .  stage  ==  STUNT_ABORT  )  { 
reset  (  )  ; 
setState  (  State  .  STOP  )  ; 
abortFatal  (  new   SocketException  (  "Peer aborted connection attempt."  )  )  ; 
return  ; 
} 
if  (  msgState  !=  null  &&  msgState  .  stage  ==  STUNT_SWAP  )  { 
switch  (  mState  )  { 
case   A_SWAP  : 
case   P_SWAP  : 
break  ; 
default  : 
reset  (  )  ; 
if  (  ++  swapped  >  2  *  MAX_ATTEMPTS  -  1  )  { 
send  (  stuntState  .  abort  (  )  )  ; 
setState  (  State  .  STOP  )  ; 
}  else  { 
send  (  stuntState  .  swap  (  )  )  ; 
setState  (  isActive  ?  State  .  A_SWAP  :  State  .  P_SWAP  )  ; 
} 
break  ; 
} 
} 
switch  (  mState  )  { 
case   A_INV_SENT  : 
case   P_INV_ACKD  : 
if  (  msgState  !=  null  &&  msgState  .  stage  ==  (  isActive  ?  STUNT_ACCEPT  :  STUNT_CALLBACK  )  )  { 
closeSocket  (  BSOCK  +  PSOCK  )  ; 
if  (  msgState  .  pred  !=  null  )  peerState  =  msgState  ; 
bsock  =  getNBConnSock  (  iaddr  ,  peerState  .  pred  ,  sel  ,  true  )  ; 
setState  (  isActive  ?  State  .  A_INV_ACKD  :  State  .  P_CB_RCVD  )  ; 
log  .  finest  (  "Passive client accepted invite, trying direct connect."  )  ; 
}  else   if  (  msg  .  isError  (  )  )  { 
abortFatal  (  new   UnknownHostException  (  "Unknown ID"  )  )  ; 
} 
break  ; 
case   P_SWAP  : 
if  (  msgState  !=  null  &&  msgState  .  stage  ==  STUNT_SWAP  )  { 
asock  =  getNBConnSock  (  null  ,  responders  [  0  ]  ,  sel  ,  false  )  ; 
setState  (  State  .  A_ST1  )  ; 
isActive  =  true  ; 
log  .  finest  (  "Attempting connction as active client."  )  ; 
} 
break  ; 
case   A_SWAP  : 
if  (  msgState  !=  null  &&  msgState  .  stage  ==  STUNT_INVITE  )  { 
setState  (  State  .  P_START  )  ; 
peerState  =  msgState  ; 
isActive  =  false  ; 
log  .  finest  (  "Attempting connection as passive client."  )  ; 
} 
break  ; 
default  : 
log  .  fine  (  "Unhandled message: "  +  msg  )  ; 
} 
} 

public   int   step  (  Object   o  ,  Dispatch   d  ,  STUNTSelector   sel  )  { 
log  .  entering  (  "STUNTCont"  ,  "step"  ,  o  )  ; 
if  (  mState  ==  State  .  STOP  )  { 
new   Exception  (  )  .  printStackTrace  (  )  ; 
} 
try  { 
if  (  o   instanceof   SignalingMessage  )  handleProxyMsg  (  (  SignalingMessage  )  o  ,  sel  )  ;  else   if  (  o   instanceof   STUNTSocket  ||  o   instanceof   STUNTServerSocket  )  handleSocketOp  (  o  ,  sel  )  ;  else   handleTimeout  (  sel  )  ; 
}  catch  (  IllegalStateException   e  )  { 
log  .  throwing  (  "STUNTCont"  ,  "step"  ,  e  )  ; 
reset  (  )  ; 
if  (  ++  swapped  >  2  *  MAX_ATTEMPTS  -  1  )  { 
try  { 
send  (  stuntState  .  abort  (  )  )  ; 
}  catch  (  IOException   i  )  { 
} 
setState  (  State  .  STOP  )  ; 
}  else  { 
try  { 
send  (  stuntState  .  swap  (  )  )  ; 
}  catch  (  IOException   i  )  { 
} 
setState  (  isActive  ?  State  .  A_SWAP  :  State  .  P_SWAP  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  throwing  (  "STUNTCont"  ,  "step"  ,  e  )  ; 
setState  (  State  .  STOP  )  ; 
try  { 
send  (  stuntState  .  abort  (  )  )  ; 
}  catch  (  IOException   i  )  { 
} 
reset  (  )  ; 
} 
log  .  exiting  (  "STUNTCont"  ,  "step"  ,  mState  +  "("  +  mState  .  getTimeout  (  )  +  ":"  +  tries  +  "/"  +  mState  .  getRetries  (  )  +  ")"  )  ; 
return   mState  .  getTimeout  (  )  ; 
} 

public   void   cancel  (  Dispatch   d  )  { 
if  (  established  )  { 
try  { 
fsock  .  stopSelect  (  )  ; 
d  .  sel  .  remove  (  fsock  )  ; 
d  .  callback  (  this  ,  fsock  )  ; 
fsock  =  null  ; 
}  catch  (  Exception   e  )  { 
log  .  throwing  (  "STUNTCont"  ,  "cancel"  ,  e  )  ; 
} 
}  else  { 
d  .  callback  (  this  ,  reason  )  ; 
} 
reset  (  )  ; 
} 
} 

