package   org  .  asteriskjava  .  live  .  internal  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  TimerTask  ; 
import   org  .  asteriskjava  .  live  .  AsteriskQueue  ; 
import   org  .  asteriskjava  .  live  .  AsteriskQueueEntry  ; 
import   org  .  asteriskjava  .  live  .  AsteriskQueueListener  ; 
import   org  .  asteriskjava  .  live  .  AsteriskQueueMember  ; 
import   org  .  asteriskjava  .  util  .  Log  ; 
import   org  .  asteriskjava  .  util  .  LogFactory  ; 







class   AsteriskQueueImpl   extends   AbstractLiveObject   implements   AsteriskQueue  { 






private   class   ServiceLevelTimerTask   extends   TimerTask  { 

private   final   AsteriskQueueEntry   entry  ; 

ServiceLevelTimerTask  (  AsteriskQueueEntry   entry  )  { 
this  .  entry  =  entry  ; 
} 

@  Override 
public   void   run  (  )  { 
fireServiceLevelExceeded  (  entry  )  ; 
} 
} 

private   final   Log   logger  =  LogFactory  .  getLog  (  this  .  getClass  (  )  )  ; 

private   final   String   name  ; 

private   Integer   max  ; 

private   String   strategy  ; 

private   Integer   serviceLevel  ; 

private   Integer   weight  ; 

private   final   ArrayList  <  AsteriskQueueEntryImpl  >  entries  ; 

private   final   Timer   timer  ; 

private   final   HashMap  <  String  ,  AsteriskQueueMemberImpl  >  members  ; 

private   final   List  <  AsteriskQueueListener  >  listeners  ; 

private   final   HashMap  <  AsteriskQueueEntry  ,  ServiceLevelTimerTask  >  serviceLevelTimerTasks  ; 

AsteriskQueueImpl  (  AsteriskServerImpl   server  ,  String   name  ,  Integer   max  ,  String   strategy  ,  Integer   serviceLevel  ,  Integer   weight  )  { 
super  (  server  )  ; 
this  .  name  =  name  ; 
this  .  max  =  max  ; 
this  .  strategy  =  strategy  ; 
this  .  serviceLevel  =  serviceLevel  ; 
this  .  weight  =  weight  ; 
entries  =  new   ArrayList  <  AsteriskQueueEntryImpl  >  (  25  )  ; 
listeners  =  new   ArrayList  <  AsteriskQueueListener  >  (  )  ; 
members  =  new   HashMap  <  String  ,  AsteriskQueueMemberImpl  >  (  )  ; 
timer  =  new   Timer  (  "ServiceLevelTimer-"  +  name  ,  true  )  ; 
serviceLevelTimerTasks  =  new   HashMap  <  AsteriskQueueEntry  ,  ServiceLevelTimerTask  >  (  )  ; 
} 

void   cancelServiceLevelTimer  (  )  { 
timer  .  cancel  (  )  ; 
} 

public   String   getName  (  )  { 
return   name  ; 
} 

public   Integer   getMax  (  )  { 
return   max  ; 
} 

public   String   getStrategy  (  )  { 
return   strategy  ; 
} 

void   setMax  (  Integer   max  )  { 
this  .  max  =  max  ; 
} 

public   Integer   getServiceLevel  (  )  { 
return   serviceLevel  ; 
} 

void   setServiceLevel  (  Integer   serviceLevel  )  { 
this  .  serviceLevel  =  serviceLevel  ; 
} 

public   Integer   getWeight  (  )  { 
return   weight  ; 
} 

void   setWeight  (  Integer   weight  )  { 
this  .  weight  =  weight  ; 
} 

public   List  <  AsteriskQueueEntry  >  getEntries  (  )  { 
synchronized  (  entries  )  { 
return   new   ArrayList  <  AsteriskQueueEntry  >  (  entries  )  ; 
} 
} 





private   void   shift  (  )  { 
int   currentPos  =  1  ; 
synchronized  (  entries  )  { 
for  (  AsteriskQueueEntryImpl   qe  :  entries  )  { 
if  (  qe  .  getPosition  (  )  !=  currentPos  )  { 
qe  .  setPosition  (  currentPos  )  ; 
} 
currentPos  ++  ; 
} 
} 
} 















void   createNewEntry  (  AsteriskChannelImpl   channel  ,  int   reportedPosition  ,  Date   dateReceived  )  { 
AsteriskQueueEntryImpl   qe  =  new   AsteriskQueueEntryImpl  (  server  ,  this  ,  channel  ,  reportedPosition  ,  dateReceived  )  ; 
long   delay  =  serviceLevel  *  1000L  ; 
if  (  delay  >  0  )  { 
ServiceLevelTimerTask   timerTask  =  new   ServiceLevelTimerTask  (  qe  )  ; 
timer  .  schedule  (  timerTask  ,  delay  )  ; 
synchronized  (  serviceLevelTimerTasks  )  { 
serviceLevelTimerTasks  .  put  (  qe  ,  timerTask  )  ; 
} 
} 
synchronized  (  entries  )  { 
entries  .  add  (  qe  )  ; 
shift  (  )  ; 
} 
channel  .  setQueueEntry  (  qe  )  ; 
fireNewEntry  (  qe  )  ; 
server  .  fireNewQueueEntry  (  qe  )  ; 
} 













void   removeEntry  (  AsteriskQueueEntryImpl   entry  ,  Date   dateReceived  )  { 
synchronized  (  serviceLevelTimerTasks  )  { 
if  (  serviceLevelTimerTasks  .  containsKey  (  entry  )  )  { 
ServiceLevelTimerTask   timerTask  =  serviceLevelTimerTasks  .  get  (  entry  )  ; 
timerTask  .  cancel  (  )  ; 
serviceLevelTimerTasks  .  remove  (  entry  )  ; 
} 
} 
boolean   changed  ; 
synchronized  (  entries  )  { 
changed  =  entries  .  remove  (  entry  )  ; 
if  (  changed  )  { 
shift  (  )  ; 
} 
} 
if  (  changed  )  { 
entry  .  getChannel  (  )  .  setQueueEntry  (  null  )  ; 
entry  .  left  (  dateReceived  )  ; 
fireEntryLeave  (  entry  )  ; 
} 
} 

@  Override 
public   String   toString  (  )  { 
final   StringBuffer   sb  ; 
sb  =  new   StringBuffer  (  "AsteriskQueue["  )  ; 
sb  .  append  (  "name='"  )  .  append  (  getName  (  )  )  .  append  (  "',"  )  ; 
sb  .  append  (  "max='"  )  .  append  (  getMax  (  )  )  .  append  (  "',"  )  ; 
sb  .  append  (  "strategy='"  )  .  append  (  getStrategy  (  )  )  .  append  (  "',"  )  ; 
sb  .  append  (  "serviceLevel='"  )  .  append  (  getServiceLevel  (  )  )  .  append  (  "',"  )  ; 
sb  .  append  (  "weight='"  )  .  append  (  getWeight  (  )  )  .  append  (  "',"  )  ; 
synchronized  (  entries  )  { 
sb  .  append  (  "entries='"  )  .  append  (  entries  .  toString  (  )  )  .  append  (  "',"  )  ; 
} 
synchronized  (  members  )  { 
sb  .  append  (  "members='"  )  .  append  (  members  .  toString  (  )  )  .  append  (  "',"  )  ; 
} 
sb  .  append  (  "systemHashcode="  )  .  append  (  System  .  identityHashCode  (  this  )  )  ; 
sb  .  append  (  "]"  )  ; 
return   sb  .  toString  (  )  ; 
} 

public   void   addAsteriskQueueListener  (  AsteriskQueueListener   listener  )  { 
synchronized  (  listeners  )  { 
listeners  .  add  (  listener  )  ; 
} 
} 

public   void   removeAsteriskQueueListener  (  AsteriskQueueListener   listener  )  { 
synchronized  (  listeners  )  { 
listeners  .  remove  (  listener  )  ; 
} 
} 






void   fireNewEntry  (  AsteriskQueueEntryImpl   entry  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onNewEntry  (  entry  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in onNewEntry()"  ,  e  )  ; 
} 
} 
} 
} 






void   fireEntryLeave  (  AsteriskQueueEntryImpl   entry  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onEntryLeave  (  entry  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in onEntryLeave()"  ,  e  )  ; 
} 
} 
} 
} 






void   fireMemberAdded  (  AsteriskQueueMemberImpl   member  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onMemberAdded  (  member  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in onMemberAdded()"  ,  e  )  ; 
} 
} 
} 
} 






void   fireMemberRemoved  (  AsteriskQueueMemberImpl   member  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onMemberRemoved  (  member  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in onMemberRemoved()"  ,  e  )  ; 
} 
} 
} 
} 






public   Collection  <  AsteriskQueueMember  >  getMembers  (  )  { 
ArrayList  <  AsteriskQueueMember  >  listOfMembers  =  new   ArrayList  <  AsteriskQueueMember  >  (  members  .  size  (  )  )  ; 
synchronized  (  members  )  { 
for  (  AsteriskQueueMemberImpl   asteriskQueueMember  :  members  .  values  (  )  )  { 
listOfMembers  .  add  (  asteriskQueueMember  )  ; 
} 
} 
return   listOfMembers  ; 
} 







AsteriskQueueMemberImpl   getMember  (  String   location  )  { 
synchronized  (  members  )  { 
if  (  members  .  containsKey  (  location  )  )  { 
return   members  .  get  (  location  )  ; 
} 
} 
return   null  ; 
} 






void   addMember  (  AsteriskQueueMemberImpl   member  )  { 
synchronized  (  members  )  { 
if  (  members  .  containsValue  (  member  )  )  { 
return  ; 
} 
logger  .  info  (  "Adding new member to the queue "  +  getName  (  )  +  ": "  +  member  .  toString  (  )  )  ; 
members  .  put  (  member  .  getLocation  (  )  ,  member  )  ; 
} 
fireMemberAdded  (  member  )  ; 
} 







AsteriskQueueMemberImpl   getMemberByLocation  (  String   location  )  { 
AsteriskQueueMemberImpl   member  ; 
synchronized  (  members  )  { 
member  =  members  .  get  (  location  )  ; 
} 
if  (  member  ==  null  )  { 
logger  .  error  (  "Requested member at location "  +  location  +  " not found!"  )  ; 
} 
return   member  ; 
} 






void   fireMemberStateChanged  (  AsteriskQueueMemberImpl   member  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onMemberStateChange  (  member  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in onMemberStateChange()"  ,  e  )  ; 
} 
} 
} 
} 







AsteriskQueueEntryImpl   getEntry  (  String   channelName  )  { 
synchronized  (  entries  )  { 
for  (  AsteriskQueueEntryImpl   entry  :  entries  )  { 
if  (  entry  .  getChannel  (  )  .  getName  (  )  .  equals  (  channelName  )  )  { 
return   entry  ; 
} 
} 
} 
return   null  ; 
} 






public   void   removeMember  (  AsteriskQueueMemberImpl   member  )  { 
synchronized  (  members  )  { 
if  (  !  members  .  containsValue  (  member  )  )  { 
return  ; 
} 
logger  .  info  (  "Remove member from the queue "  +  getName  (  )  +  ": "  +  member  .  toString  (  )  )  ; 
members  .  remove  (  member  .  getLocation  (  )  )  ; 
} 
fireMemberRemoved  (  member  )  ; 
} 

void   fireServiceLevelExceeded  (  AsteriskQueueEntry   entry  )  { 
synchronized  (  listeners  )  { 
for  (  AsteriskQueueListener   listener  :  listeners  )  { 
try  { 
listener  .  onEntryServiceLevelExceeded  (  entry  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Exception in fireServiceLevelExceeded()"  ,  e  )  ; 
} 
} 
} 
} 







AsteriskQueueEntryImpl   getEntry  (  int   position  )  { 
position  --  ; 
AsteriskQueueEntryImpl   foundEntry  =  null  ; 
synchronized  (  entries  )  { 
try  { 
foundEntry  =  entries  .  get  (  position  )  ; 
}  catch  (  IndexOutOfBoundsException   e  )  { 
} 
} 
return   foundEntry  ; 
} 
} 

