package   com  .  meetup  .  memcached  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  IdentityHashMap  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  SortedMap  ; 
import   java  .  util  .  TreeMap  ; 
import   java  .  util  .  zip  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   java  .  util  .  concurrent  .  locks  .  ReentrantLock  ; 
import   org  .  apache  .  log4j  .  Logger  ; 



















































































public   class   SockIOPool  { 

private   static   Logger   log  =  Logger  .  getLogger  (  SockIOPool  .  class  .  getName  (  )  )  ; 

private   static   Map  <  String  ,  SockIOPool  >  pools  =  new   HashMap  <  String  ,  SockIOPool  >  (  )  ; 

private   static   ThreadLocal  <  MessageDigest  >  MD5  =  new   ThreadLocal  <  MessageDigest  >  (  )  { 

@  Override 
protected   MessageDigest   initialValue  (  )  { 
try  { 
return   MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
log  .  error  (  "++++ no md5 algorithm found"  )  ; 
throw   new   IllegalStateException  (  "++++ no md5 algorythm found"  )  ; 
} 
} 
}  ; 

private   static   final   Integer   ZERO  =  new   Integer  (  0  )  ; 

public   static   final   int   NATIVE_HASH  =  0  ; 

public   static   final   int   OLD_COMPAT_HASH  =  1  ; 

public   static   final   int   NEW_COMPAT_HASH  =  2  ; 

public   static   final   int   CONSISTENT_HASH  =  3  ; 

public   static   final   long   MAX_RETRY_DELAY  =  10  *  60  *  1000  ; 

private   MaintThread   maintThread  ; 

private   boolean   initialized  =  false  ; 

private   int   maxCreate  =  1  ; 

private   int   poolMultiplier  =  3  ; 

private   int   initConn  =  1  ; 

private   int   minConn  =  1  ; 

private   int   maxConn  =  10  ; 

private   long   maxIdle  =  1000  *  60  *  5  ; 

private   long   maxBusyTime  =  1000  *  30  ; 

private   long   maintSleep  =  1000  *  30  ; 

private   int   socketTO  =  1000  *  30  ; 

private   int   socketConnectTO  =  1000  *  3  ; 

private   boolean   aliveCheck  =  false  ; 

private   boolean   failover  =  true  ; 

private   boolean   failback  =  true  ; 

private   boolean   nagle  =  true  ; 

private   int   hashingAlg  =  NATIVE_HASH  ; 

private   final   ReentrantLock   hostDeadLock  =  new   ReentrantLock  (  )  ; 

private   String  [  ]  servers  ; 

private   Integer  [  ]  weights  ; 

private   Integer   totalWeight  =  0  ; 

private   List  <  String  >  buckets  ; 

private   TreeMap  <  Long  ,  String  >  consistentBuckets  ; 

private   Map  <  String  ,  Date  >  hostDead  ; 

private   Map  <  String  ,  Long  >  hostDeadDur  ; 

private   Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  availPool  ; 

private   Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  busyPool  ; 

private   Map  <  SockIO  ,  Integer  >  deadPool  ; 

; 

protected   SockIOPool  (  )  { 
} 







public   static   synchronized   SockIOPool   getInstance  (  String   poolName  )  { 
if  (  pools  .  containsKey  (  poolName  )  )  return   pools  .  get  (  poolName  )  ; 
SockIOPool   pool  =  new   SockIOPool  (  )  ; 
pools  .  put  (  poolName  ,  pool  )  ; 
return   pool  ; 
} 







public   static   SockIOPool   getInstance  (  )  { 
return   getInstance  (  "default"  )  ; 
} 






public   void   setServers  (  String  [  ]  servers  )  { 
this  .  servers  =  servers  ; 
} 






public   String  [  ]  getServers  (  )  { 
return   this  .  servers  ; 
} 









public   void   setWeights  (  Integer  [  ]  weights  )  { 
this  .  weights  =  weights  ; 
} 






public   Integer  [  ]  getWeights  (  )  { 
return   this  .  weights  ; 
} 






public   void   setInitConn  (  int   initConn  )  { 
this  .  initConn  =  initConn  ; 
} 







public   int   getInitConn  (  )  { 
return   this  .  initConn  ; 
} 






public   void   setMinConn  (  int   minConn  )  { 
this  .  minConn  =  minConn  ; 
} 






public   int   getMinConn  (  )  { 
return   this  .  minConn  ; 
} 






public   void   setMaxConn  (  int   maxConn  )  { 
this  .  maxConn  =  maxConn  ; 
} 






public   int   getMaxConn  (  )  { 
return   this  .  maxConn  ; 
} 






public   void   setMaxIdle  (  long   maxIdle  )  { 
this  .  maxIdle  =  maxIdle  ; 
} 






public   long   getMaxIdle  (  )  { 
return   this  .  maxIdle  ; 
} 






public   void   setMaxBusyTime  (  long   maxBusyTime  )  { 
this  .  maxBusyTime  =  maxBusyTime  ; 
} 






public   long   getMaxBusy  (  )  { 
return   this  .  maxBusyTime  ; 
} 







public   void   setMaintSleep  (  long   maintSleep  )  { 
this  .  maintSleep  =  maintSleep  ; 
} 






public   long   getMaintSleep  (  )  { 
return   this  .  maintSleep  ; 
} 






public   void   setSocketTO  (  int   socketTO  )  { 
this  .  socketTO  =  socketTO  ; 
} 






public   int   getSocketTO  (  )  { 
return   this  .  socketTO  ; 
} 






public   void   setSocketConnectTO  (  int   socketConnectTO  )  { 
this  .  socketConnectTO  =  socketConnectTO  ; 
} 






public   int   getSocketConnectTO  (  )  { 
return   this  .  socketConnectTO  ; 
} 











public   void   setFailover  (  boolean   failover  )  { 
this  .  failover  =  failover  ; 
} 






public   boolean   getFailover  (  )  { 
return   this  .  failover  ; 
} 










public   void   setFailback  (  boolean   failback  )  { 
this  .  failback  =  failback  ; 
} 






public   boolean   getFailback  (  )  { 
return   this  .  failback  ; 
} 












public   void   setAliveCheck  (  boolean   aliveCheck  )  { 
this  .  aliveCheck  =  aliveCheck  ; 
} 






public   boolean   getAliveCheck  (  )  { 
return   this  .  aliveCheck  ; 
} 








public   void   setNagle  (  boolean   nagle  )  { 
this  .  nagle  =  nagle  ; 
} 






public   boolean   getNagle  (  )  { 
return   this  .  nagle  ; 
} 












public   void   setHashingAlg  (  int   alg  )  { 
this  .  hashingAlg  =  alg  ; 
} 






public   int   getHashingAlg  (  )  { 
return   this  .  hashingAlg  ; 
} 










private   static   long   origCompatHashingAlg  (  String   key  )  { 
long   hash  =  0  ; 
char  [  ]  cArr  =  key  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  cArr  .  length  ;  ++  i  )  { 
hash  =  (  hash  *  33  )  +  cArr  [  i  ]  ; 
} 
return   hash  ; 
} 












private   static   long   newCompatHashingAlg  (  String   key  )  { 
CRC32   checksum  =  new   CRC32  (  )  ; 
checksum  .  update  (  key  .  getBytes  (  )  )  ; 
long   crc  =  checksum  .  getValue  (  )  ; 
return  (  crc  >  >  16  )  &  0x7fff  ; 
} 










private   static   long   md5HashingAlg  (  String   key  )  { 
MessageDigest   md5  =  MD5  .  get  (  )  ; 
md5  .  reset  (  )  ; 
md5  .  update  (  key  .  getBytes  (  )  )  ; 
byte  [  ]  bKey  =  md5  .  digest  (  )  ; 
long   res  =  (  (  long  )  (  bKey  [  3  ]  &  0xFF  )  <<  24  )  |  (  (  long  )  (  bKey  [  2  ]  &  0xFF  )  <<  16  )  |  (  (  long  )  (  bKey  [  1  ]  &  0xFF  )  <<  8  )  |  (  long  )  (  bKey  [  0  ]  &  0xFF  )  ; 
return   res  ; 
} 







private   long   getHash  (  String   key  ,  Integer   hashCode  )  { 
if  (  hashCode  !=  null  )  { 
if  (  hashingAlg  ==  CONSISTENT_HASH  )  return   hashCode  .  longValue  (  )  &  0xffffffffL  ;  else   return   hashCode  .  longValue  (  )  ; 
}  else  { 
switch  (  hashingAlg  )  { 
case   NATIVE_HASH  : 
return  (  long  )  key  .  hashCode  (  )  ; 
case   OLD_COMPAT_HASH  : 
return   origCompatHashingAlg  (  key  )  ; 
case   NEW_COMPAT_HASH  : 
return   newCompatHashingAlg  (  key  )  ; 
case   CONSISTENT_HASH  : 
return   md5HashingAlg  (  key  )  ; 
default  : 
hashingAlg  =  NATIVE_HASH  ; 
return  (  long  )  key  .  hashCode  (  )  ; 
} 
} 
} 

private   long   getBucket  (  String   key  ,  Integer   hashCode  )  { 
long   hc  =  getHash  (  key  ,  hashCode  )  ; 
if  (  this  .  hashingAlg  ==  CONSISTENT_HASH  )  { 
return   findPointFor  (  hc  )  ; 
}  else  { 
long   bucket  =  hc  %  buckets  .  size  (  )  ; 
if  (  bucket  <  0  )  bucket  *=  -  1  ; 
return   bucket  ; 
} 
} 







private   Long   findPointFor  (  Long   hv  )  { 
SortedMap  <  Long  ,  String  >  tmap  =  this  .  consistentBuckets  .  tailMap  (  hv  )  ; 
return  (  tmap  .  isEmpty  (  )  )  ?  this  .  consistentBuckets  .  firstKey  (  )  :  tmap  .  firstKey  (  )  ; 
} 




public   void   initialize  (  )  { 
synchronized  (  this  )  { 
if  (  initialized  &&  (  buckets  !=  null  ||  consistentBuckets  !=  null  )  &&  (  availPool  !=  null  )  &&  (  busyPool  !=  null  )  )  { 
log  .  error  (  "++++ trying to initialize an already initialized pool"  )  ; 
return  ; 
} 
availPool  =  new   HashMap  <  String  ,  Map  <  SockIO  ,  Long  >  >  (  servers  .  length  *  initConn  )  ; 
busyPool  =  new   HashMap  <  String  ,  Map  <  SockIO  ,  Long  >  >  (  servers  .  length  *  initConn  )  ; 
deadPool  =  new   IdentityHashMap  <  SockIO  ,  Integer  >  (  )  ; 
hostDeadDur  =  new   HashMap  <  String  ,  Long  >  (  )  ; 
hostDead  =  new   HashMap  <  String  ,  Date  >  (  )  ; 
maxCreate  =  (  poolMultiplier  >  minConn  )  ?  minConn  :  minConn  /  poolMultiplier  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "++++ initializing pool with following settings:"  )  ; 
log  .  debug  (  "++++ initial size: "  +  initConn  )  ; 
log  .  debug  (  "++++ min spare   : "  +  minConn  )  ; 
log  .  debug  (  "++++ max spare   : "  +  maxConn  )  ; 
} 
if  (  servers  ==  null  ||  servers  .  length  <=  0  )  { 
log  .  error  (  "++++ trying to initialize with no servers"  )  ; 
throw   new   IllegalStateException  (  "++++ trying to initialize with no servers"  )  ; 
} 
if  (  this  .  hashingAlg  ==  CONSISTENT_HASH  )  populateConsistentBuckets  (  )  ;  else   populateBuckets  (  )  ; 
this  .  initialized  =  true  ; 
if  (  this  .  maintSleep  >  0  )  this  .  startMaintThread  (  )  ; 
} 
} 

private   void   populateBuckets  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ initializing internal hashing structure for consistent hashing"  )  ; 
this  .  buckets  =  new   ArrayList  <  String  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  servers  .  length  ;  i  ++  )  { 
if  (  this  .  weights  !=  null  &&  this  .  weights  .  length  >  i  )  { 
for  (  int   k  =  0  ;  k  <  this  .  weights  [  i  ]  .  intValue  (  )  ;  k  ++  )  { 
this  .  buckets  .  add  (  servers  [  i  ]  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ added "  +  servers  [  i  ]  +  " to server bucket"  )  ; 
} 
}  else  { 
this  .  buckets  .  add  (  servers  [  i  ]  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ added "  +  servers  [  i  ]  +  " to server bucket"  )  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "+++ creating initial connections ("  +  initConn  +  ") for host: "  +  servers  [  i  ]  )  ; 
for  (  int   j  =  0  ;  j  <  initConn  ;  j  ++  )  { 
SockIO   socket  =  createSocket  (  servers  [  i  ]  )  ; 
if  (  socket  ==  null  )  { 
log  .  error  (  "++++ failed to create connection to: "  +  servers  [  i  ]  +  " -- only "  +  j  +  " created."  )  ; 
break  ; 
} 
addSocketToPool  (  availPool  ,  servers  [  i  ]  ,  socket  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ created and added socket: "  +  socket  .  toString  (  )  +  " for host "  +  servers  [  i  ]  )  ; 
} 
} 
} 

private   void   populateConsistentBuckets  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ initializing internal hashing structure for consistent hashing"  )  ; 
this  .  consistentBuckets  =  new   TreeMap  <  Long  ,  String  >  (  )  ; 
MessageDigest   md5  =  MD5  .  get  (  )  ; 
if  (  this  .  totalWeight  <=  0  &&  this  .  weights  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  this  .  weights  .  length  ;  i  ++  )  this  .  totalWeight  +=  (  this  .  weights  [  i  ]  ==  null  )  ?  1  :  this  .  weights  [  i  ]  ; 
}  else   if  (  this  .  weights  ==  null  )  { 
this  .  totalWeight  =  this  .  servers  .  length  ; 
} 
for  (  int   i  =  0  ;  i  <  servers  .  length  ;  i  ++  )  { 
int   thisWeight  =  1  ; 
if  (  this  .  weights  !=  null  &&  this  .  weights  [  i  ]  !=  null  )  thisWeight  =  this  .  weights  [  i  ]  ; 
double   factor  =  Math  .  floor  (  (  (  double  )  (  40  *  this  .  servers  .  length  *  thisWeight  )  )  /  (  double  )  this  .  totalWeight  )  ; 
for  (  long   j  =  0  ;  j  <  factor  ;  j  ++  )  { 
byte  [  ]  d  =  md5  .  digest  (  (  servers  [  i  ]  +  "-"  +  j  )  .  getBytes  (  )  )  ; 
for  (  int   h  =  0  ;  h  <  4  ;  h  ++  )  { 
Long   k  =  (  (  long  )  (  d  [  3  +  h  *  4  ]  &  0xFF  )  <<  24  )  |  (  (  long  )  (  d  [  2  +  h  *  4  ]  &  0xFF  )  <<  16  )  |  (  (  long  )  (  d  [  1  +  h  *  4  ]  &  0xFF  )  <<  8  )  |  (  (  long  )  (  d  [  0  +  h  *  4  ]  &  0xFF  )  )  ; 
consistentBuckets  .  put  (  k  ,  servers  [  i  ]  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ added "  +  servers  [  i  ]  +  " to server bucket"  )  ; 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "+++ creating initial connections ("  +  initConn  +  ") for host: "  +  servers  [  i  ]  )  ; 
for  (  int   j  =  0  ;  j  <  initConn  ;  j  ++  )  { 
SockIO   socket  =  createSocket  (  servers  [  i  ]  )  ; 
if  (  socket  ==  null  )  { 
log  .  error  (  "++++ failed to create connection to: "  +  servers  [  i  ]  +  " -- only "  +  j  +  " created."  )  ; 
break  ; 
} 
addSocketToPool  (  availPool  ,  servers  [  i  ]  ,  socket  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ created and added socket: "  +  socket  .  toString  (  )  +  " for host "  +  servers  [  i  ]  )  ; 
} 
} 
} 






public   boolean   isInitialized  (  )  { 
return   initialized  ; 
} 











protected   SockIO   createSocket  (  String   host  )  { 
SockIO   socket  =  null  ; 
hostDeadLock  .  lock  (  )  ; 
try  { 
if  (  failover  &&  failback  &&  hostDead  .  containsKey  (  host  )  &&  hostDeadDur  .  containsKey  (  host  )  )  { 
Date   store  =  hostDead  .  get  (  host  )  ; 
long   expire  =  hostDeadDur  .  get  (  host  )  .  longValue  (  )  ; 
if  (  (  store  .  getTime  (  )  +  expire  )  >  System  .  currentTimeMillis  (  )  )  return   null  ; 
} 
}  finally  { 
hostDeadLock  .  unlock  (  )  ; 
} 
try  { 
socket  =  new   SockIO  (  this  ,  host  ,  this  .  socketTO  ,  this  .  socketConnectTO  ,  this  .  nagle  )  ; 
if  (  !  socket  .  isConnected  (  )  )  { 
log  .  error  (  "++++ failed to get SockIO obj for: "  +  host  +  " -- new socket is not connected"  )  ; 
deadPool  .  put  (  socket  ,  ZERO  )  ; 
socket  =  null  ; 
} 
}  catch  (  Exception   ex  )  { 
log  .  error  (  "++++ failed to get SockIO obj for: "  +  host  )  ; 
log  .  error  (  ex  .  getMessage  (  )  ,  ex  )  ; 
socket  =  null  ; 
} 
hostDeadLock  .  lock  (  )  ; 
try  { 
if  (  socket  ==  null  )  { 
Date   now  =  new   Date  (  )  ; 
hostDead  .  put  (  host  ,  now  )  ; 
long   expire  =  (  hostDeadDur  .  containsKey  (  host  )  )  ?  (  (  (  Long  )  hostDeadDur  .  get  (  host  )  )  .  longValue  (  )  *  2  )  :  1000  ; 
if  (  expire  >  MAX_RETRY_DELAY  )  expire  =  MAX_RETRY_DELAY  ; 
hostDeadDur  .  put  (  host  ,  new   Long  (  expire  )  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ ignoring dead host: "  +  host  +  " for "  +  expire  +  " ms"  )  ; 
clearHostFromPool  (  availPool  ,  host  )  ; 
}  else  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ created socket ("  +  socket  .  toString  (  )  +  ") for host: "  +  host  )  ; 
if  (  hostDead  .  containsKey  (  host  )  ||  hostDeadDur  .  containsKey  (  host  )  )  { 
hostDead  .  remove  (  host  )  ; 
hostDeadDur  .  remove  (  host  )  ; 
} 
} 
}  finally  { 
hostDeadLock  .  unlock  (  )  ; 
} 
return   socket  ; 
} 





public   String   getHost  (  String   key  )  { 
return   getHost  (  key  ,  null  )  ; 
} 








public   String   getHost  (  String   key  ,  Integer   hashcode  )  { 
SockIO   socket  =  getSock  (  key  ,  hashcode  )  ; 
String   host  =  socket  .  getHost  (  )  ; 
socket  .  close  (  )  ; 
return   host  ; 
} 








public   SockIO   getSock  (  String   key  )  { 
return   getSock  (  key  ,  null  )  ; 
} 












public   SockIO   getSock  (  String   key  ,  Integer   hashCode  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "cache socket pick "  +  key  +  " "  +  hashCode  )  ; 
if  (  !  this  .  initialized  )  { 
log  .  error  (  "attempting to get SockIO from uninitialized pool!"  )  ; 
return   null  ; 
} 
if  (  (  this  .  hashingAlg  ==  CONSISTENT_HASH  &&  consistentBuckets  .  size  (  )  ==  0  )  ||  (  buckets  !=  null  &&  buckets  .  size  (  )  ==  0  )  )  return   null  ; 
if  (  (  this  .  hashingAlg  ==  CONSISTENT_HASH  &&  consistentBuckets  .  size  (  )  ==  1  )  ||  (  buckets  !=  null  &&  buckets  .  size  (  )  ==  1  )  )  { 
SockIO   sock  =  (  this  .  hashingAlg  ==  CONSISTENT_HASH  )  ?  getConnection  (  consistentBuckets  .  get  (  consistentBuckets  .  firstKey  (  )  )  )  :  getConnection  (  buckets  .  get  (  0  )  )  ; 
if  (  sock  !=  null  &&  sock  .  isConnected  (  )  )  { 
if  (  aliveCheck  )  { 
if  (  !  sock  .  isAlive  (  )  )  { 
sock  .  close  (  )  ; 
try  { 
sock  .  trueClose  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "failed to close dead socket"  )  ; 
} 
sock  =  null  ; 
} 
} 
}  else  { 
if  (  sock  !=  null  )  { 
deadPool  .  put  (  sock  ,  ZERO  )  ; 
sock  =  null  ; 
} 
} 
return   sock  ; 
} 
Set  <  String  >  tryServers  =  new   HashSet  <  String  >  (  Arrays  .  asList  (  servers  )  )  ; 
long   bucket  =  getBucket  (  key  ,  hashCode  )  ; 
String   server  =  (  this  .  hashingAlg  ==  CONSISTENT_HASH  )  ?  consistentBuckets  .  get  (  bucket  )  :  buckets  .  get  (  (  int  )  bucket  )  ; 
while  (  !  tryServers  .  isEmpty  (  )  )  { 
SockIO   sock  =  getConnection  (  server  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "cache choose "  +  server  +  " for "  +  key  )  ; 
if  (  sock  !=  null  &&  sock  .  isConnected  (  )  )  { 
if  (  aliveCheck  )  { 
if  (  sock  .  isAlive  (  )  )  { 
return   sock  ; 
}  else  { 
sock  .  close  (  )  ; 
try  { 
sock  .  trueClose  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "failed to close dead socket"  )  ; 
} 
sock  =  null  ; 
} 
}  else  { 
return   sock  ; 
} 
}  else  { 
if  (  sock  !=  null  )  { 
deadPool  .  put  (  sock  ,  ZERO  )  ; 
sock  =  null  ; 
} 
} 
if  (  !  failover  )  return   null  ; 
tryServers  .  remove  (  server  )  ; 
if  (  tryServers  .  isEmpty  (  )  )  break  ; 
int   rehashTries  =  0  ; 
while  (  !  tryServers  .  contains  (  server  )  )  { 
String   newKey  =  String  .  format  (  "%s%s"  ,  rehashTries  ,  key  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "rehashing with: "  +  newKey  )  ; 
bucket  =  getBucket  (  newKey  ,  null  )  ; 
server  =  (  this  .  hashingAlg  ==  CONSISTENT_HASH  )  ?  consistentBuckets  .  get  (  bucket  )  :  buckets  .  get  (  (  int  )  bucket  )  ; 
rehashTries  ++  ; 
} 
} 
return   null  ; 
} 











public   SockIO   getConnection  (  String   host  )  { 
if  (  !  this  .  initialized  )  { 
log  .  error  (  "attempting to get SockIO from uninitialized pool!"  )  ; 
return   null  ; 
} 
if  (  host  ==  null  )  return   null  ; 
synchronized  (  this  )  { 
if  (  availPool  !=  null  &&  !  availPool  .  isEmpty  (  )  )  { 
Map  <  SockIO  ,  Long  >  aSockets  =  availPool  .  get  (  host  )  ; 
if  (  aSockets  !=  null  &&  !  aSockets  .  isEmpty  (  )  )  { 
for  (  Iterator  <  SockIO  >  i  =  aSockets  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
SockIO   socket  =  i  .  next  (  )  ; 
if  (  socket  .  isConnected  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ moving socket for host ("  +  host  +  ") to busy pool ... socket: "  +  socket  )  ; 
i  .  remove  (  )  ; 
addSocketToPool  (  busyPool  ,  host  ,  socket  )  ; 
return   socket  ; 
}  else  { 
deadPool  .  put  (  socket  ,  ZERO  )  ; 
i  .  remove  (  )  ; 
} 
} 
} 
} 
} 
SockIO   socket  =  createSocket  (  host  )  ; 
if  (  socket  !=  null  )  { 
synchronized  (  this  )  { 
addSocketToPool  (  busyPool  ,  host  ,  socket  )  ; 
} 
} 
return   socket  ; 
} 











protected   void   addSocketToPool  (  Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  pool  ,  String   host  ,  SockIO   socket  )  { 
if  (  pool  .  containsKey  (  host  )  )  { 
Map  <  SockIO  ,  Long  >  sockets  =  pool  .  get  (  host  )  ; 
if  (  sockets  !=  null  )  { 
sockets  .  put  (  socket  ,  new   Long  (  System  .  currentTimeMillis  (  )  )  )  ; 
return  ; 
} 
} 
Map  <  SockIO  ,  Long  >  sockets  =  new   IdentityHashMap  <  SockIO  ,  Long  >  (  )  ; 
sockets  .  put  (  socket  ,  new   Long  (  System  .  currentTimeMillis  (  )  )  )  ; 
pool  .  put  (  host  ,  sockets  )  ; 
} 











protected   void   removeSocketFromPool  (  Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  pool  ,  String   host  ,  SockIO   socket  )  { 
if  (  pool  .  containsKey  (  host  )  )  { 
Map  <  SockIO  ,  Long  >  sockets  =  pool  .  get  (  host  )  ; 
if  (  sockets  !=  null  )  sockets  .  remove  (  socket  )  ; 
} 
} 










protected   void   clearHostFromPool  (  Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  pool  ,  String   host  )  { 
if  (  pool  .  containsKey  (  host  )  )  { 
Map  <  SockIO  ,  Long  >  sockets  =  pool  .  get  (  host  )  ; 
if  (  sockets  !=  null  &&  sockets  .  size  (  )  >  0  )  { 
for  (  Iterator  <  SockIO  >  i  =  sockets  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
SockIO   socket  =  i  .  next  (  )  ; 
try  { 
socket  .  trueClose  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "++++ failed to close socket: "  +  ioe  .  getMessage  (  )  )  ; 
} 
i  .  remove  (  )  ; 
socket  =  null  ; 
} 
} 
} 
} 










private   void   checkIn  (  SockIO   socket  ,  boolean   addToAvail  )  { 
String   host  =  socket  .  getHost  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ calling check-in on socket: "  +  socket  .  toString  (  )  +  " for host: "  +  host  )  ; 
synchronized  (  this  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ removing socket ("  +  socket  .  toString  (  )  +  ") from busy pool for host: "  +  host  )  ; 
removeSocketFromPool  (  busyPool  ,  host  ,  socket  )  ; 
if  (  socket  .  isConnected  (  )  &&  addToAvail  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ returning socket ("  +  socket  .  toString  (  )  +  " to avail pool for host: "  +  host  )  ; 
addSocketToPool  (  availPool  ,  host  ,  socket  )  ; 
}  else  { 
deadPool  .  put  (  socket  ,  ZERO  )  ; 
socket  =  null  ; 
} 
} 
} 










private   void   checkIn  (  SockIO   socket  )  { 
checkIn  (  socket  ,  true  )  ; 
} 








protected   void   closePool  (  Map  <  String  ,  Map  <  SockIO  ,  Long  >  >  pool  )  { 
for  (  Iterator  <  String  >  i  =  pool  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   host  =  i  .  next  (  )  ; 
Map  <  SockIO  ,  Long  >  sockets  =  pool  .  get  (  host  )  ; 
for  (  Iterator  <  SockIO  >  j  =  sockets  .  keySet  (  )  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
SockIO   socket  =  j  .  next  (  )  ; 
try  { 
socket  .  trueClose  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "++++ failed to trueClose socket: "  +  socket  .  toString  (  )  +  " for host: "  +  host  )  ; 
} 
j  .  remove  (  )  ; 
socket  =  null  ; 
} 
} 
} 








public   void   shutDown  (  )  { 
synchronized  (  this  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ SockIOPool shutting down..."  )  ; 
if  (  maintThread  !=  null  &&  maintThread  .  isRunning  (  )  )  { 
stopMaintThread  (  )  ; 
while  (  maintThread  .  isRunning  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ waiting for main thread to finish run +++"  )  ; 
try  { 
Thread  .  sleep  (  500  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ closing all internal pools."  )  ; 
closePool  (  availPool  )  ; 
closePool  (  busyPool  )  ; 
availPool  =  null  ; 
busyPool  =  null  ; 
buckets  =  null  ; 
consistentBuckets  =  null  ; 
hostDeadDur  =  null  ; 
hostDead  =  null  ; 
maintThread  =  null  ; 
initialized  =  false  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ SockIOPool finished shutting down."  )  ; 
} 
} 








protected   void   startMaintThread  (  )  { 
if  (  maintThread  !=  null  )  { 
if  (  maintThread  .  isRunning  (  )  )  { 
log  .  error  (  "main thread already running"  )  ; 
}  else  { 
maintThread  .  start  (  )  ; 
} 
}  else  { 
maintThread  =  new   MaintThread  (  this  )  ; 
maintThread  .  setInterval  (  this  .  maintSleep  )  ; 
maintThread  .  start  (  )  ; 
} 
} 




protected   void   stopMaintThread  (  )  { 
if  (  maintThread  !=  null  &&  maintThread  .  isRunning  (  )  )  maintThread  .  stopThread  (  )  ; 
} 






protected   void   selfMaint  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Starting self maintenance...."  )  ; 
Map  <  String  ,  Integer  >  needSockets  =  new   HashMap  <  String  ,  Integer  >  (  )  ; 
synchronized  (  this  )  { 
for  (  Iterator  <  String  >  i  =  availPool  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   host  =  i  .  next  (  )  ; 
Map  <  SockIO  ,  Long  >  sockets  =  availPool  .  get  (  host  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Size of avail pool for host ("  +  host  +  ") = "  +  sockets  .  size  (  )  )  ; 
if  (  sockets  .  size  (  )  <  minConn  )  { 
int   need  =  minConn  -  sockets  .  size  (  )  ; 
needSockets  .  put  (  host  ,  need  )  ; 
} 
} 
} 
Map  <  String  ,  Set  <  SockIO  >  >  newSockets  =  new   HashMap  <  String  ,  Set  <  SockIO  >  >  (  )  ; 
for  (  String   host  :  needSockets  .  keySet  (  )  )  { 
Integer   need  =  needSockets  .  get  (  host  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Need to create "  +  need  +  " new sockets for pool for host: "  +  host  )  ; 
Set  <  SockIO  >  newSock  =  new   HashSet  <  SockIO  >  (  need  )  ; 
for  (  int   j  =  0  ;  j  <  need  ;  j  ++  )  { 
SockIO   socket  =  createSocket  (  host  )  ; 
if  (  socket  ==  null  )  break  ; 
newSock  .  add  (  socket  )  ; 
} 
newSockets  .  put  (  host  ,  newSock  )  ; 
} 
synchronized  (  this  )  { 
for  (  String   host  :  newSockets  .  keySet  (  )  )  { 
Set  <  SockIO  >  sockets  =  newSockets  .  get  (  host  )  ; 
for  (  SockIO   socket  :  sockets  )  addSocketToPool  (  availPool  ,  host  ,  socket  )  ; 
} 
for  (  Iterator  <  String  >  i  =  availPool  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   host  =  i  .  next  (  )  ; 
Map  <  SockIO  ,  Long  >  sockets  =  availPool  .  get  (  host  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Size of avail pool for host ("  +  host  +  ") = "  +  sockets  .  size  (  )  )  ; 
if  (  sockets  .  size  (  )  >  maxConn  )  { 
int   diff  =  sockets  .  size  (  )  -  maxConn  ; 
int   needToClose  =  (  diff  <=  poolMultiplier  )  ?  diff  :  (  diff  )  /  poolMultiplier  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ need to remove "  +  needToClose  +  " spare sockets for pool for host: "  +  host  )  ; 
for  (  Iterator  <  SockIO  >  j  =  sockets  .  keySet  (  )  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
if  (  needToClose  <=  0  )  break  ; 
SockIO   socket  =  j  .  next  (  )  ; 
long   expire  =  sockets  .  get  (  socket  )  .  longValue  (  )  ; 
if  (  (  expire  +  maxIdle  )  <  System  .  currentTimeMillis  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "+++ removing stale entry from pool as it is past its idle timeout and pool is over max spare"  )  ; 
deadPool  .  put  (  socket  ,  ZERO  )  ; 
j  .  remove  (  )  ; 
needToClose  --  ; 
} 
} 
} 
} 
for  (  Iterator  <  String  >  i  =  busyPool  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   host  =  i  .  next  (  )  ; 
Map  <  SockIO  ,  Long  >  sockets  =  busyPool  .  get  (  host  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Size of busy pool for host ("  +  host  +  ")  = "  +  sockets  .  size  (  )  )  ; 
for  (  Iterator  <  SockIO  >  j  =  sockets  .  keySet  (  )  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
SockIO   socket  =  j  .  next  (  )  ; 
long   hungTime  =  sockets  .  get  (  socket  )  .  longValue  (  )  ; 
if  (  (  hungTime  +  maxBusyTime  )  <  System  .  currentTimeMillis  (  )  )  { 
log  .  error  (  "+++ removing potentially hung connection from busy pool ... socket in pool for "  +  (  System  .  currentTimeMillis  (  )  -  hungTime  )  +  "ms"  )  ; 
deadPool  .  put  (  socket  ,  ZERO  )  ; 
j  .  remove  (  )  ; 
} 
} 
} 
} 
Set  <  SockIO  >  toClose  ; 
synchronized  (  deadPool  )  { 
toClose  =  deadPool  .  keySet  (  )  ; 
deadPool  =  new   IdentityHashMap  <  SockIO  ,  Integer  >  (  )  ; 
} 
for  (  SockIO   socket  :  toClose  )  { 
try  { 
socket  .  trueClose  (  false  )  ; 
}  catch  (  Exception   ex  )  { 
log  .  error  (  "++++ failed to close SockIO obj from deadPool"  )  ; 
log  .  error  (  ex  .  getMessage  (  )  ,  ex  )  ; 
} 
socket  =  null  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "+++ ending self maintenance."  )  ; 
} 







protected   static   class   MaintThread   extends   Thread  { 

private   static   Logger   log  =  Logger  .  getLogger  (  MaintThread  .  class  .  getName  (  )  )  ; 

private   SockIOPool   pool  ; 

private   long   interval  =  1000  *  3  ; 

private   boolean   stopThread  =  false  ; 

private   boolean   running  ; 

protected   MaintThread  (  SockIOPool   pool  )  { 
this  .  pool  =  pool  ; 
this  .  setDaemon  (  true  )  ; 
this  .  setName  (  "MaintThread"  )  ; 
} 

public   void   setInterval  (  long   interval  )  { 
this  .  interval  =  interval  ; 
} 

public   boolean   isRunning  (  )  { 
return   this  .  running  ; 
} 





public   void   stopThread  (  )  { 
this  .  stopThread  =  true  ; 
this  .  interrupt  (  )  ; 
} 




public   void   run  (  )  { 
this  .  running  =  true  ; 
while  (  !  this  .  stopThread  )  { 
try  { 
Thread  .  sleep  (  interval  )  ; 
if  (  pool  .  isInitialized  (  )  )  pool  .  selfMaint  (  )  ; 
}  catch  (  Exception   e  )  { 
break  ; 
} 
} 
this  .  running  =  false  ; 
} 
} 










public   static   class   SockIO   implements   LineInputStream  { 

private   static   Logger   log  =  Logger  .  getLogger  (  SockIO  .  class  .  getName  (  )  )  ; 

private   SockIOPool   pool  ; 

private   String   host  ; 

private   Socket   sock  ; 

private   DataInputStream   in  ; 

private   BufferedOutputStream   out  ; 














public   SockIO  (  SockIOPool   pool  ,  String   host  ,  int   port  ,  int   timeout  ,  int   connectTimeout  ,  boolean   noDelay  )  throws   IOException  ,  UnknownHostException  { 
this  .  pool  =  pool  ; 
sock  =  getSocket  (  host  ,  port  ,  connectTimeout  )  ; 
if  (  timeout  >=  0  )  sock  .  setSoTimeout  (  timeout  )  ; 
sock  .  setTcpNoDelay  (  noDelay  )  ; 
in  =  new   DataInputStream  (  sock  .  getInputStream  (  )  )  ; 
out  =  new   BufferedOutputStream  (  sock  .  getOutputStream  (  )  )  ; 
this  .  host  =  host  +  ":"  +  port  ; 
} 












public   SockIO  (  SockIOPool   pool  ,  String   host  ,  int   timeout  ,  int   connectTimeout  ,  boolean   noDelay  )  throws   IOException  ,  UnknownHostException  { 
this  .  pool  =  pool  ; 
String  [  ]  ip  =  host  .  split  (  ":"  )  ; 
sock  =  getSocket  (  ip  [  0  ]  ,  Integer  .  parseInt  (  ip  [  1  ]  )  ,  connectTimeout  )  ; 
if  (  timeout  >=  0  )  this  .  sock  .  setSoTimeout  (  timeout  )  ; 
sock  .  setTcpNoDelay  (  noDelay  )  ; 
in  =  new   DataInputStream  (  sock  .  getInputStream  (  )  )  ; 
out  =  new   BufferedOutputStream  (  sock  .  getOutputStream  (  )  )  ; 
this  .  host  =  host  ; 
} 











protected   static   Socket   getSocket  (  String   host  ,  int   port  ,  int   timeout  )  throws   IOException  { 
SocketChannel   sock  =  SocketChannel  .  open  (  )  ; 
sock  .  socket  (  )  .  connect  (  new   InetSocketAddress  (  host  ,  port  )  ,  timeout  )  ; 
return   sock  .  socket  (  )  ; 
} 






public   SocketChannel   getChannel  (  )  { 
return   sock  .  getChannel  (  )  ; 
} 






public   String   getHost  (  )  { 
return   this  .  host  ; 
} 






public   void   trueClose  (  )  throws   IOException  { 
trueClose  (  true  )  ; 
} 






public   void   trueClose  (  boolean   addToDeadPool  )  throws   IOException  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ Closing socket for real: "  +  toString  (  )  )  ; 
boolean   err  =  false  ; 
StringBuilder   errMsg  =  new   StringBuilder  (  )  ; 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "++++ error closing input stream for socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  )  ; 
log  .  error  (  ioe  .  getMessage  (  )  ,  ioe  )  ; 
errMsg  .  append  (  "++++ error closing input stream for socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  +  "\n"  )  ; 
errMsg  .  append  (  ioe  .  getMessage  (  )  )  ; 
err  =  true  ; 
} 
} 
if  (  out  !=  null  )  { 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "++++ error closing output stream for socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  )  ; 
log  .  error  (  ioe  .  getMessage  (  )  ,  ioe  )  ; 
errMsg  .  append  (  "++++ error closing output stream for socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  +  "\n"  )  ; 
errMsg  .  append  (  ioe  .  getMessage  (  )  )  ; 
err  =  true  ; 
} 
} 
if  (  sock  !=  null  )  { 
try  { 
sock  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "++++ error closing socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  )  ; 
log  .  error  (  ioe  .  getMessage  (  )  ,  ioe  )  ; 
errMsg  .  append  (  "++++ error closing socket: "  +  toString  (  )  +  " for host: "  +  getHost  (  )  +  "\n"  )  ; 
errMsg  .  append  (  ioe  .  getMessage  (  )  )  ; 
err  =  true  ; 
} 
} 
if  (  addToDeadPool  &&  sock  !=  null  )  pool  .  checkIn  (  this  ,  false  )  ; 
in  =  null  ; 
out  =  null  ; 
sock  =  null  ; 
if  (  err  )  throw   new   IOException  (  errMsg  .  toString  (  )  )  ; 
} 





void   close  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "++++ marking socket ("  +  this  .  toString  (  )  +  ") as closed and available to return to avail pool"  )  ; 
pool  .  checkIn  (  this  )  ; 
} 






boolean   isConnected  (  )  { 
return  (  sock  !=  null  &&  sock  .  isConnected  (  )  )  ; 
} 

boolean   isAlive  (  )  { 
if  (  !  isConnected  (  )  )  return   false  ; 
try  { 
this  .  write  (  "version\r\n"  .  getBytes  (  )  )  ; 
this  .  flush  (  )  ; 
String   response  =  this  .  readLine  (  )  ; 
}  catch  (  IOException   ex  )  { 
return   false  ; 
} 
return   true  ; 
} 








public   String   readLine  (  )  throws   IOException  { 
if  (  sock  ==  null  ||  !  sock  .  isConnected  (  )  )  { 
log  .  error  (  "++++ attempting to read from closed socket"  )  ; 
throw   new   IOException  (  "++++ attempting to read from closed socket"  )  ; 
} 
byte  [  ]  b  =  new   byte  [  1  ]  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
boolean   eol  =  false  ; 
while  (  in  .  read  (  b  ,  0  ,  1  )  !=  -  1  )  { 
if  (  b  [  0  ]  ==  13  )  { 
eol  =  true  ; 
}  else  { 
if  (  eol  )  { 
if  (  b  [  0  ]  ==  10  )  break  ; 
eol  =  false  ; 
} 
} 
bos  .  write  (  b  ,  0  ,  1  )  ; 
} 
if  (  bos  ==  null  ||  bos  .  size  (  )  <=  0  )  { 
throw   new   IOException  (  "++++ Stream appears to be dead, so closing it down"  )  ; 
} 
return   bos  .  toString  (  )  .  trim  (  )  ; 
} 






public   void   clearEOL  (  )  throws   IOException  { 
if  (  sock  ==  null  ||  !  sock  .  isConnected  (  )  )  { 
log  .  error  (  "++++ attempting to read from closed socket"  )  ; 
throw   new   IOException  (  "++++ attempting to read from closed socket"  )  ; 
} 
byte  [  ]  b  =  new   byte  [  1  ]  ; 
boolean   eol  =  false  ; 
while  (  in  .  read  (  b  ,  0  ,  1  )  !=  -  1  )  { 
if  (  b  [  0  ]  ==  13  )  { 
eol  =  true  ; 
continue  ; 
} 
if  (  eol  )  { 
if  (  b  [  0  ]  ==  10  )  break  ; 
eol  =  false  ; 
} 
} 
} 







public   int   read  (  byte  [  ]  b  )  throws   IOException  { 
if  (  sock  ==  null  ||  !  sock  .  isConnected  (  )  )  { 
log  .  error  (  "++++ attempting to read from closed socket"  )  ; 
throw   new   IOException  (  "++++ attempting to read from closed socket"  )  ; 
} 
int   count  =  0  ; 
while  (  count  <  b  .  length  )  { 
int   cnt  =  in  .  read  (  b  ,  count  ,  (  b  .  length  -  count  )  )  ; 
count  +=  cnt  ; 
} 
return   count  ; 
} 






void   flush  (  )  throws   IOException  { 
if  (  sock  ==  null  ||  !  sock  .  isConnected  (  )  )  { 
log  .  error  (  "++++ attempting to write to closed socket"  )  ; 
throw   new   IOException  (  "++++ attempting to write to closed socket"  )  ; 
} 
out  .  flush  (  )  ; 
} 







void   write  (  byte  [  ]  b  )  throws   IOException  { 
if  (  sock  ==  null  ||  !  sock  .  isConnected  (  )  )  { 
log  .  error  (  "++++ attempting to write to closed socket"  )  ; 
throw   new   IOException  (  "++++ attempting to write to closed socket"  )  ; 
} 
out  .  write  (  b  )  ; 
} 







public   int   hashCode  (  )  { 
return  (  sock  ==  null  )  ?  0  :  sock  .  hashCode  (  )  ; 
} 






public   String   toString  (  )  { 
return  (  sock  ==  null  )  ?  ""  :  sock  .  toString  (  )  ; 
} 




protected   void   finalize  (  )  throws   Throwable  { 
try  { 
if  (  sock  !=  null  )  { 
log  .  error  (  "++++ closing potentially leaked socket in finalize"  )  ; 
sock  .  close  (  )  ; 
sock  =  null  ; 
} 
}  catch  (  Throwable   t  )  { 
log  .  error  (  t  .  getMessage  (  )  ,  t  )  ; 
}  finally  { 
super  .  finalize  (  )  ; 
} 
} 
} 
} 

