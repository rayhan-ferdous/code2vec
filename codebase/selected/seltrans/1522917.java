package   org  .  psepr  .  jClient  ; 

import   java  .  util  .  Timer  ; 
import   java  .  util  .  TimerTask  ; 



















































public   class   SimpleLeaseManager   extends   LeaseManager  { 

private   long   leaseWindow  ; 

private   long   leaseGranted  ; 

private   int   leaseState  ; 

private   int   leaseMaxRetries  ; 

private   int   leaseRetriesRemaining  ; 

private   Timer   leaseTimer  ; 

private   long   leaseTimeoutTime  ; 

private   String   leaseChannel  ; 

private   String   leaseType  ; 

private   String   leaseID  ; 

private   static   final   int   LEASE_INACTIVE  =  0  ; 

private   static   final   int   LEASE_PENDING  =  1  ; 

private   static   final   int   LEASE_ACTIVE  =  2  ; 

private   static   final   int   LEASE_EXPIRED  =  3  ; 

private   static   final   int   LEASE_RENEGOTIATING  =  4  ; 

private   static   final   int   LEASE_RELEASE  =  5  ; 

private   int   shortTimeoutAbsolute  ; 

private   int   longTimeoutPercent  ; 

private   long   defaultInterval  ; 

private   static   final   String   PARAM_SLM_MAXRETRIES  =  "/jClient/SimpleLeaseManager/MaximumRetries"  ; 

private   static   final   String   PARAM_SLM_DEFAULTINTERVAL  =  "/jClient/SimpleLeaseManager/DefaultLeaseDuration"  ; 

private   static   final   String   PARAM_SLM_SHORTABSOLUTE  =  "/jClient/SimpleLeaseManager/ShortTimeoutAbsolute"  ; 

private   static   final   String   PARAM_SLM_LONGPERCENT  =  "/jClient/SimpleLeaseManager/LongTimeoutPercent"  ; 

private   static   final   String   leaseOpTimeout  =  "XXYY_TIMEOUT_AABB"  ; 

private   DebugLogger   log  ; 




public   SimpleLeaseManager  (  )  { 
super  (  )  ; 
this  .  init  (  )  ; 
} 




public   SimpleLeaseManager  (  PsEPRConnection   pConn  ,  String   chan  ,  String   type  )  { 
super  (  )  ; 
this  .  init  (  )  ; 
this  .  setConnection  (  pConn  )  ; 
this  .  setLeaseWindow  (  defaultInterval  )  ; 
this  .  setChannel  (  chan  )  ; 
this  .  setType  (  type  )  ; 
return  ; 
} 




public   SimpleLeaseManager  (  PsEPRConnection   pConn  ,  String   chan  ,  String   type  ,  long   interval  )  { 
super  (  )  ; 
this  .  init  (  )  ; 
this  .  setConnection  (  pConn  )  ; 
this  .  setLeaseWindow  (  interval  )  ; 
this  .  setChannel  (  chan  )  ; 
this  .  setType  (  type  )  ; 
return  ; 
} 

private   void   init  (  )  { 
leaseWindow  =  0L  ; 
leaseState  =  SimpleLeaseManager  .  LEASE_INACTIVE  ; 
leaseChannel  =  null  ; 
leaseType  =  null  ; 
leaseTimer  =  null  ; 
leaseTimeoutTime  =  System  .  currentTimeMillis  (  )  ; 
leaseID  =  PsEPRService  .  randomString  (  16  )  ; 
log  =  new   DebugLogger  (  PsEPRService  .  AppName  ,  "SimpleLeaseManager"  )  ; 
leaseMaxRetries  =  3  ; 
shortTimeoutAbsolute  =  5  ; 
longTimeoutPercent  =  80  ; 
leaseRetriesRemaining  =  0  ; 
defaultInterval  =  3600  ; 
try  { 
leaseMaxRetries  =  PsEPRService  .  getParam  (  PARAM_SLM_MAXRETRIES  ,  10  )  ; 
shortTimeoutAbsolute  =  PsEPRService  .  getParam  (  PARAM_SLM_SHORTABSOLUTE  ,  20  )  ; 
longTimeoutPercent  =  PsEPRService  .  getParam  (  PARAM_SLM_LONGPERCENT  ,  80  )  ; 
defaultInterval  =  PsEPRService  .  getParam  (  PARAM_SLM_DEFAULTINTERVAL  ,  3600  )  ; 
}  catch  (  Exception   e  )  { 
} 
return  ; 
} 

public   boolean   getLeaseActive  (  )  { 
if  (  leaseState  ==  LEASE_ACTIVE  ||  leaseState  ==  LEASE_RENEGOTIATING  )  { 
return   true  ; 
} 
return   false  ; 
} 

public   long   getLeaseWindow  (  )  { 
return   leaseWindow  ; 
} 

public   void   setLeaseWindow  (  long   lw  )  { 
leaseWindow  =  lw  ; 
return  ; 
} 

public   long   getLeaseGranted  (  )  { 
return   leaseGranted  ; 
} 

public   void   setLeaseGranted  (  long   lg  )  { 
leaseGranted  =  lg  ; 
return  ; 
} 

public   String   getChannel  (  )  { 
return   leaseChannel  ; 
} 

public   void   setChannel  (  String   chan  )  { 
leaseChannel  =  chan  ; 
return  ; 
} 

public   String   getType  (  )  { 
return   leaseType  ; 
} 

public   void   setType  (  String   tp  )  { 
leaseType  =  tp  ; 
return  ; 
} 

public   int   getMaxRetries  (  )  { 
return   leaseMaxRetries  ; 
} 

public   void   setMaxRetries  (  int   mr  )  { 
leaseMaxRetries  =  mr  ; 
return  ; 
} 

public   int   getRetriesRemaining  (  )  { 
return   leaseRetriesRemaining  ; 
} 

public   void   setRetriesRemaining  (  int   mr  )  { 
leaseRetriesRemaining  =  mr  ; 
return  ; 
} 

public   int   getShortTimeoutPeriod  (  )  { 
return   shortTimeoutAbsolute  ; 
} 

public   void   setShortTimeoutPeriod  (  int   xx  )  { 
shortTimeoutAbsolute  =  xx  ; 
return  ; 
} 

public   String   getLeaseID  (  )  { 
return   leaseID  ; 
} 

public   void   setLeaseID  (  String   xx  )  { 
leaseID  =  xx  ; 
return  ; 
} 

public   int   getLongTimeoutPercent  (  )  { 
return   longTimeoutPercent  ; 
} 

public   void   setLongTimeoutPercent  (  int   xx  )  { 
longTimeoutPercent  =  xx  ; 
return  ; 
} 

public   long   getLongTimeoutPeriod  (  )  { 
return   this  .  getLeaseGranted  (  )  *  this  .  getLongTimeoutPercent  (  )  /  100L  ; 
} 

public   int   getLeaseState  (  )  { 
return   leaseState  ; 
} 

public   String   getLeaseStateName  (  )  { 
String   ret  =  "LEASE_UNKNOWN"  ; 
if  (  leaseState  ==  LEASE_INACTIVE  )  { 
ret  =  "LEASE_INACTIVE"  ; 
} 
if  (  leaseState  ==  LEASE_PENDING  )  { 
ret  =  "LEASE_PENDING"  ; 
} 
if  (  leaseState  ==  LEASE_ACTIVE  )  { 
ret  =  "LEASE_ACTIVE"  ; 
} 
if  (  leaseState  ==  LEASE_EXPIRED  )  { 
ret  =  "LEASE_EXPIRED"  ; 
} 
if  (  leaseState  ==  LEASE_RENEGOTIATING  )  { 
ret  =  "LEASE_RENEGOTIATING"  ; 
} 
if  (  leaseState  ==  LEASE_RELEASE  )  { 
ret  =  "LEASE_RELEASE"  ; 
} 
return   ret  ; 
} 

public   synchronized   void   start  (  )  { 
if  (  leaseState  ==  LEASE_INACTIVE  )  { 
sendLeaseRequest  (  LEASE_PENDING  ,  this  .  getShortTimeoutPeriod  (  )  )  ; 
} 
} 




public   synchronized   void   release  (  )  { 
if  (  leaseState  ==  LEASE_ACTIVE  ||  leaseState  ==  LEASE_PENDING  ||  leaseState  ==  LEASE_RENEGOTIATING  )  { 
sendUnregisterRequest  (  LEASE_RELEASE  )  ; 
} 
} 





public   synchronized   void   forceRenegotiation  (  )  { 
if  (  leaseState  ==  LEASE_ACTIVE  ||  leaseState  ==  LEASE_PENDING  ||  leaseState  ==  LEASE_RENEGOTIATING  )  { 
log  .  log  (  log  .  LEASE  ,  "Forcing renegotiation"  )  ; 
sendLeaseRequest  (  LEASE_RENEGOTIATING  ,  this  .  getShortTimeoutPeriod  (  )  )  ; 
} 
return  ; 
} 







public   synchronized   boolean   receiveEvent  (  PsEPREvent   pE  )  { 
boolean   ret  =  false  ; 
if  (  pE  .  getPayload  (  )  .  getNamespace  (  )  !=  null  &&  pE  .  getPayload  (  )  .  getNamespace  (  )  .  equalsIgnoreCase  (  PayloadLease  .  leaseNamespace  )  )  { 
PayloadLease   pl  =  (  PayloadLease  )  pE  .  getPayload  (  )  ; 
if  (  pl  .  getLeaseIdentifier  (  )  .  equals  (  leaseID  )  )  { 
log  .  log  (  log  .  LEASEDETAIL  ,  "Received lease with my ID. state="  +  getLeaseStateName  (  )  +  ", op="  +  pl  .  getLeaseOp  (  )  )  ; 
String   lOp  =  pl  .  getLeaseOp  (  )  ; 
switch  (  leaseState  )  { 
case   LEASE_INACTIVE  : 
{ 
break  ; 
} 
case   LEASE_ACTIVE  : 
{ 
if  (  lOp  .  equals  (  PayloadLease  .  leaseOpLease  )  )  { 
leaseGranted  =  pl  .  getLeaseDuration  (  )  ; 
log  .  log  (  log  .  LEASEDETAIL  ,  "received lease while lease active. Dur="  +  leaseGranted  )  ; 
ResetTimeoutTimer  (  this  .  getLongTimeoutPeriod  (  )  ,  leaseGranted  )  ; 
ret  =  true  ; 
}  else   if  (  lOp  .  equals  (  leaseOpTimeout  )  )  { 
if  (  System  .  currentTimeMillis  (  )  >  leaseTimeoutTime  )  { 
log  .  log  (  log  .  LEASE  ,  "Timeout during lease. Starting renegotiation"  )  ; 
sendLeaseRequest  (  LEASE_RENEGOTIATING  ,  this  .  getShortTimeoutPeriod  (  )  )  ; 
ret  =  true  ; 
}  else  { 
log  .  log  (  log  .  LEASEDETAIL  ,  "Extra timeout.  Still waiting"  )  ; 
} 
} 
break  ; 
} 
case   LEASE_PENDING  : 
{ 
if  (  lOp  .  equals  (  PayloadLease  .  leaseOpLease  )  )  { 
leaseState  =  LEASE_ACTIVE  ; 
leaseGranted  =  pl  .  getLeaseDuration  (  )  ; 
leaseRetriesRemaining  =  leaseMaxRetries  ; 
log  .  log  (  log  .  LEASE  ,  "Lease now active. Dur="  +  leaseGranted  )  ; 
ResetTimeoutTimer  (  this  .  getLongTimeoutPeriod  (  )  ,  leaseGranted  )  ; 
ret  =  true  ; 
}  else   if  (  lOp  .  equals  (  leaseOpTimeout  )  )  { 
if  (  System  .  currentTimeMillis  (  )  >  leaseTimeoutTime  )  { 
if  (  leaseRetriesRemaining  --  >=  0  )  { 
sendLeaseRequest  (  LEASE_PENDING  ,  this  .  getShortTimeoutPeriod  (  )  )  ; 
}  else  { 
sendUnregisterRequest  (  LEASE_EXPIRED  )  ; 
} 
ret  =  true  ; 
}  else  { 
log  .  log  (  log  .  LEASEDETAIL  ,  "Extra timeout.  Still waiting"  )  ; 
} 
} 
break  ; 
} 
case   LEASE_EXPIRED  : 
{ 
break  ; 
} 
case   LEASE_RENEGOTIATING  : 
{ 
if  (  pl  .  getLeaseOp  (  )  .  equals  (  PayloadLease  .  leaseOpLease  )  )  { 
leaseState  =  LEASE_ACTIVE  ; 
leaseGranted  =  pl  .  getLeaseDuration  (  )  ; 
leaseRetriesRemaining  =  leaseMaxRetries  ; 
log  .  log  (  log  .  LEASE  ,  "Lease now active. Dur="  +  leaseGranted  )  ; 
ResetTimeoutTimer  (  this  .  getLongTimeoutPeriod  (  )  ,  leaseGranted  )  ; 
ret  =  true  ; 
} 
break  ; 
} 
case   LEASE_RELEASE  : 
{ 
break  ; 
} 
} 
} 
} 
return   ret  ; 
} 








private   void   sendLeaseRequest  (  int   nextState  ,  long   timeo  )  { 
try  { 
log  .  log  (  log  .  LEASE  ,  "Sending lease: chan="  +  leaseChannel  +  ", dur="  +  leaseWindow  +  ", ID="  +  leaseID  )  ; 
PayloadLease   pl  =  new   PayloadLease  (  leaseID  ,  this  .  getType  (  )  ,  PayloadLease  .  leaseOpRegister  ,  leaseWindow  )  ; 
PsEPREvent   ev  =  new   PsEPREvent  (  )  ; 
ev  .  setDistribution  (  PsEPREvent  .  distributionOne  )  ; 
ev  .  setToChannel  (  leaseChannel  )  ; 
ev  .  setToService  (  PsEPRService  .  getRegistryName  (  )  )  ; 
ev  .  setToInstance  (  PsEPRService  .  getRegistryInstance  (  )  )  ; 
ev  .  setPayload  (  pl  )  ; 
leaseState  =  nextState  ; 
ResetTimeoutTimer  (  timeo  ,  leaseWindow  )  ; 
this  .  getConnection  (  )  .  sendEvent  (  ev  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   PsEPRException  (  "SimpleLeaseManager.sendLeaseRequest caught:"  +  e  .  toString  (  )  )  ; 
} 
} 





private   void   sendUnregisterRequest  (  int   nextState  )  { 
try  { 
log  .  log  (  log  .  LEASE  ,  "Sending unregister: chan="  +  leaseChannel  +  ", ID="  +  leaseID  )  ; 
PayloadLease   pl  =  new   PayloadLease  (  leaseID  ,  null  ,  PayloadLease  .  leaseOpUnregister  ,  leaseWindow  )  ; 
PsEPREvent   ev  =  new   PsEPREvent  (  )  ; 
ev  .  setDistribution  (  PsEPREvent  .  distributionOne  )  ; 
ev  .  setToChannel  (  leaseChannel  )  ; 
ev  .  setToService  (  PsEPRService  .  getRegistryName  (  )  )  ; 
ev  .  setPayload  (  pl  )  ; 
leaseState  =  nextState  ; 
this  .  getConnection  (  )  .  sendEvent  (  ev  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   PsEPRException  (  "SimpleLeaseManager.sendUnregisterRequest caught:"  +  e  .  toString  (  )  )  ; 
} 
} 







private   void   ResetTimeoutTimer  (  long   timeoutPeriod  ,  long   leaseInterval  )  { 
class   InsertTimeout   extends   TimerTask  { 

private   String   identifier  ; 

private   PsEPRConnection   connection  ; 

private   String   toChannel  ; 

private   long   leaseLong  ; 

public   InsertTimeout  (  String   id  ,  PsEPRConnection   pConn  ,  String   chan  ,  long   leaseL  )  { 
identifier  =  id  ; 
connection  =  pConn  ; 
toChannel  =  chan  ; 
leaseLong  =  leaseL  ; 
} 

public   void   run  (  )  { 
PsEPREvent   myE  =  new   PsEPREvent  (  )  ; 
PayloadLease   myP  =  new   PayloadLease  (  identifier  ,  null  ,  leaseOpTimeout  ,  leaseLong  )  ; 
myE  .  setToChannel  (  toChannel  )  ; 
myE  .  setToService  (  connection  .  getServiceIdentity  (  )  .  getServiceName  (  )  )  ; 
myE  .  setPayload  (  myP  )  ; 
connection  .  insertEvent  (  myE  )  ; 
} 
} 
if  (  leaseTimer  ==  null  )  { 
leaseTimer  =  new   Timer  (  )  ; 
} 
long   period  =  timeoutPeriod  *  1000L  ; 
leaseTimeoutTime  =  System  .  currentTimeMillis  (  )  +  period  -  1000  ; 
log  .  log  (  log  .  LEASEDETAIL  ,  "Scheduling timeout in "  +  Long  .  toString  (  period  )  +  " milliseconds"  )  ; 
leaseTimer  .  schedule  (  new   InsertTimeout  (  leaseID  ,  this  .  getConnection  (  )  ,  this  .  getChannel  (  )  ,  leaseInterval  )  ,  period  )  ; 
} 
} 

