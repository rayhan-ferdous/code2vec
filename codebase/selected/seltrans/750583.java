package   org  .  psepr  .  jClient  ; 















































public   class   LeaseHandler  { 

private   PsEPRConnection   pConn  ; 

private   String   channel  ; 

private   PayloadParser   payloadParser  ; 

private   EventReceiver   eventReceiver  ; 

private   LeaseWatcher   leaseWatcher  ; 

private   PsEPRLease   pLease  ; 

private   boolean   keepChecking  ; 




public   LeaseHandler  (  )  { 
super  (  )  ; 
this  .  init  (  )  ; 
} 









public   LeaseHandler  (  PsEPRConnection   con  ,  String   chan  ,  EventReceiver   eq  )  throws   PsEPRException  { 
super  (  )  ; 
this  .  init  (  )  ; 
this  .  setConnection  (  con  )  ; 
this  .  setChannel  (  chan  )  ; 
this  .  setPayloadParser  (  new   PayloadGeneric  (  )  )  ; 
this  .  setEventReceiver  (  eq  )  ; 
connectLease  (  )  ; 
} 










public   LeaseHandler  (  PsEPRConnection   con  ,  String   chan  ,  EventReceiver   eq  ,  LeaseWatcher   lw  )  throws   PsEPRException  { 
super  (  )  ; 
this  .  init  (  )  ; 
this  .  setConnection  (  con  )  ; 
this  .  setChannel  (  chan  )  ; 
this  .  setPayloadParser  (  new   PayloadGeneric  (  )  )  ; 
this  .  setEventReceiver  (  eq  )  ; 
this  .  setLeaseWatcher  (  lw  )  ; 
connectLease  (  )  ; 
} 










public   LeaseHandler  (  PsEPRConnection   con  ,  String   chan  ,  PayloadParser   pp  ,  EventReceiver   eq  ,  LeaseWatcher   lw  )  throws   PsEPRException  { 
super  (  )  ; 
this  .  init  (  )  ; 
this  .  setConnection  (  con  )  ; 
this  .  setChannel  (  chan  )  ; 
this  .  setPayloadParser  (  pp  )  ; 
this  .  setEventReceiver  (  eq  )  ; 
this  .  setLeaseWatcher  (  lw  )  ; 
connectLease  (  )  ; 
} 

private   void   init  (  )  { 
this  .  pConn  =  null  ; 
this  .  channel  =  null  ; 
this  .  payloadParser  =  null  ; 
this  .  eventReceiver  =  null  ; 
this  .  leaseWatcher  =  null  ; 
this  .  pLease  =  null  ; 
this  .  keepChecking  =  false  ; 
} 

public   PsEPRConnection   getConnection  (  )  { 
return   this  .  pConn  ; 
} 

public   void   setConnection  (  PsEPRConnection   xx  )  { 
this  .  pConn  =  xx  ; 
return  ; 
} 

public   String   getChannel  (  )  { 
return   this  .  channel  ; 
} 

public   void   setChannel  (  String   xx  )  { 
this  .  channel  =  xx  ; 
return  ; 
} 

public   PayloadParser   getPayloadParser  (  )  { 
return   this  .  payloadParser  ; 
} 

public   void   setPayloadParser  (  PayloadParser   xx  )  { 
this  .  payloadParser  =  xx  ; 
return  ; 
} 

public   EventReceiver   getEventReceiver  (  )  { 
return   this  .  eventReceiver  ; 
} 

public   void   setEventReceiver  (  EventReceiver   xx  )  { 
this  .  eventReceiver  =  xx  ; 
return  ; 
} 

public   LeaseWatcher   getLeaseWatcher  (  )  { 
return   this  .  leaseWatcher  ; 
} 

public   void   setLeaseWatcher  (  LeaseWatcher   xx  )  { 
this  .  leaseWatcher  =  xx  ; 
return  ; 
} 

public   PsEPRLease   getLease  (  )  { 
return   this  .  pLease  ; 
} 




public   void   connect  (  )  throws   PsEPRException  { 
connectLease  (  )  ; 
} 





public   void   disconnect  (  )  { 
if  (  this  .  pConn  !=  null  &&  this  .  pLease  !=  null  )  { 
this  .  pConn  .  releaseLease  (  this  .  pLease  )  ; 
} 
this  .  keepChecking  =  false  ; 
this  .  pConn  =  null  ; 
this  .  pLease  =  null  ; 
} 




public   void   waitForLease  (  )  { 
if  (  this  .  pConn  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null connection handle"  )  ; 
} 
if  (  this  .  channel  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null channel"  )  ; 
} 
if  (  this  .  payloadParser  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null event parser"  )  ; 
} 
if  (  this  .  eventReceiver  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null event receiver"  )  ; 
} 
int   retryCount  =  10  ; 
while  (  retryCount  --  >  0  )  { 
if  (  this  .  pLease  .  getLeaseManager  (  )  .  getLeaseActive  (  )  )  { 
break  ; 
} 
try  { 
Thread  .  sleep  (  5  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: After 10 trys, could not get lease to wait for"  )  ; 
} 
} 
} 




private   void   connectLease  (  )  { 
if  (  this  .  pConn  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null connection handle"  )  ; 
} 
if  (  this  .  channel  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null channel"  )  ; 
} 
if  (  this  .  payloadParser  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null event parser"  )  ; 
} 
if  (  this  .  eventReceiver  ==  null  )  { 
throw   new   PsEPRException  (  "LeaseHandler.waitForLease: cannot wait because passed null event receiver"  )  ; 
} 
try  { 
this  .  pLease  =  this  .  pConn  .  getLease  (  this  .  channel  ,  this  .  payloadParser  ,  this  .  eventReceiver  )  ; 
}  catch  (  PsEPRException   e  )  { 
this  .  pConn  .  close  (  )  ; 
throw   e  ; 
} 
Thread   checker  =  new   Thread  (  new   CheckConnection  (  )  )  ; 
checker  .  setName  (  "LeaseHandler:"  +  this  .  pLease  .  getChannel  (  )  )  ; 
keepChecking  =  true  ; 
checker  .  start  (  )  ; 
} 





private   class   CheckConnection   implements   Runnable  { 

public   void   run  (  )  { 
doWait  (  60000  )  ; 
while  (  keepChecking  )  { 
if  (  LeaseHandler  .  this  .  pConn  !=  null  )  { 
if  (  LeaseHandler  .  this  .  pLease  !=  null  )  { 
if  (  LeaseHandler  .  this  .  pConn  .  isConnected  )  { 
if  (  !  LeaseHandler  .  this  .  pLease  .  getLeaseManager  (  )  .  getLeaseActive  (  )  )  { 
LeaseHandler  .  this  .  pConn  .  forceReconnection  (  )  ; 
} 
}  else  { 
callWatcher  (  "pConn.isConnected returned false"  )  ; 
} 
}  else  { 
callWatcher  (  "pLease is null"  )  ; 
} 
}  else  { 
callWatcher  (  "pConn is null"  )  ; 
} 
doWait  (  10000  )  ; 
} 
} 

private   void   doWait  (  long   ww  )  { 
try  { 
Thread  .  sleep  (  ww  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 

private   void   callWatcher  (  String   reason  )  { 
try  { 
if  (  LeaseHandler  .  this  .  leaseWatcher  !=  null  )  { 
LeaseHandler  .  this  .  leaseWatcher  .  LeaseStateChange  (  LeaseHandler  .  this  .  pLease  ,  reason  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 

