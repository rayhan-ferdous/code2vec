package   org  .  hibernate  .  cache  ; 

import   java  .  io  .  Serializable  ; 
import   java  .  util  .  Comparator  ; 
import   org  .  slf4j  .  Logger  ; 
import   org  .  slf4j  .  LoggerFactory  ; 
import   org  .  hibernate  .  cache  .  access  .  SoftLock  ; 

















public   class   ReadWriteCache   implements   CacheConcurrencyStrategy  { 

private   static   final   Logger   log  =  LoggerFactory  .  getLogger  (  ReadWriteCache  .  class  )  ; 

private   Cache   cache  ; 

private   int   nextLockId  ; 

public   ReadWriteCache  (  )  { 
} 

public   void   setCache  (  Cache   cache  )  { 
this  .  cache  =  cache  ; 
} 

public   Cache   getCache  (  )  { 
return   cache  ; 
} 

public   String   getRegionName  (  )  { 
return   cache  .  getRegionName  (  )  ; 
} 






private   int   nextLockId  (  )  { 
if  (  nextLockId  ==  Integer  .  MAX_VALUE  )  nextLockId  =  Integer  .  MIN_VALUE  ; 
return   nextLockId  ++  ; 
} 

















public   synchronized   Object   get  (  Object   key  ,  long   txTimestamp  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Cache lookup: "  +  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
boolean   gettable  =  lockable  !=  null  &&  lockable  .  isGettable  (  txTimestamp  )  ; 
if  (  gettable  )  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Cache hit: "  +  key  )  ; 
return  (  (  Item  )  lockable  )  .  getValue  (  )  ; 
}  else  { 
if  (  log  .  isTraceEnabled  (  )  )  { 
if  (  lockable  ==  null  )  { 
log  .  trace  (  "Cache miss: "  +  key  )  ; 
}  else  { 
log  .  trace  (  "Cached item was locked: "  +  key  )  ; 
} 
} 
return   null  ; 
} 
} 








public   synchronized   SoftLock   lock  (  Object   key  ,  Object   version  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Invalidating: "  +  key  )  ; 
try  { 
cache  .  lock  (  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
long   timeout  =  cache  .  nextTimestamp  (  )  +  cache  .  getTimeout  (  )  ; 
final   Lock   lock  =  (  lockable  ==  null  )  ?  new   Lock  (  timeout  ,  nextLockId  (  )  ,  version  )  :  lockable  .  lock  (  timeout  ,  nextLockId  (  )  )  ; 
cache  .  update  (  key  ,  lock  )  ; 
return   lock  ; 
}  finally  { 
cache  .  unlock  (  key  )  ; 
} 
} 









public   synchronized   boolean   put  (  Object   key  ,  Object   value  ,  long   txTimestamp  ,  Object   version  ,  Comparator   versionComparator  ,  boolean   minimalPut  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Caching: "  +  key  )  ; 
try  { 
cache  .  lock  (  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
boolean   puttable  =  lockable  ==  null  ||  lockable  .  isPuttable  (  txTimestamp  ,  version  ,  versionComparator  )  ; 
if  (  puttable  )  { 
cache  .  put  (  key  ,  new   Item  (  value  ,  version  ,  cache  .  nextTimestamp  (  )  )  )  ; 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Cached: "  +  key  )  ; 
return   true  ; 
}  else  { 
if  (  log  .  isTraceEnabled  (  )  )  { 
if  (  lockable  .  isLock  (  )  )  { 
log  .  trace  (  "Item was locked: "  +  key  )  ; 
}  else  { 
log  .  trace  (  "Item was already cached: "  +  key  )  ; 
} 
} 
return   false  ; 
} 
}  finally  { 
cache  .  unlock  (  key  )  ; 
} 
} 




private   void   decrementLock  (  Object   key  ,  Lock   lock  )  throws   CacheException  { 
lock  .  unlock  (  cache  .  nextTimestamp  (  )  )  ; 
cache  .  update  (  key  ,  lock  )  ; 
} 






public   synchronized   void   release  (  Object   key  ,  SoftLock   clientLock  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Releasing: "  +  key  )  ; 
try  { 
cache  .  lock  (  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
if  (  isUnlockable  (  clientLock  ,  lockable  )  )  { 
decrementLock  (  key  ,  (  Lock  )  lockable  )  ; 
}  else  { 
handleLockExpiry  (  key  )  ; 
} 
}  finally  { 
cache  .  unlock  (  key  )  ; 
} 
} 

void   handleLockExpiry  (  Object   key  )  throws   CacheException  { 
log  .  warn  (  "An item was expired by the cache while it was locked (increase your cache timeout): "  +  key  )  ; 
long   ts  =  cache  .  nextTimestamp  (  )  +  cache  .  getTimeout  (  )  ; 
Lock   lock  =  new   Lock  (  ts  ,  nextLockId  (  )  ,  null  )  ; 
lock  .  unlock  (  ts  )  ; 
cache  .  update  (  key  ,  lock  )  ; 
} 

public   void   clear  (  )  throws   CacheException  { 
cache  .  clear  (  )  ; 
} 

public   void   remove  (  Object   key  )  throws   CacheException  { 
cache  .  remove  (  key  )  ; 
} 

public   void   destroy  (  )  { 
try  { 
cache  .  destroy  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  warn  (  "could not destroy cache"  ,  e  )  ; 
} 
} 





public   synchronized   boolean   afterUpdate  (  Object   key  ,  Object   value  ,  Object   version  ,  SoftLock   clientLock  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Updating: "  +  key  )  ; 
try  { 
cache  .  lock  (  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
if  (  isUnlockable  (  clientLock  ,  lockable  )  )  { 
Lock   lock  =  (  Lock  )  lockable  ; 
if  (  lock  .  wasLockedConcurrently  (  )  )  { 
decrementLock  (  key  ,  lock  )  ; 
return   false  ; 
}  else  { 
cache  .  update  (  key  ,  new   Item  (  value  ,  version  ,  cache  .  nextTimestamp  (  )  )  )  ; 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Updated: "  +  key  )  ; 
return   true  ; 
} 
}  else  { 
handleLockExpiry  (  key  )  ; 
return   false  ; 
} 
}  finally  { 
cache  .  unlock  (  key  )  ; 
} 
} 





public   synchronized   boolean   afterInsert  (  Object   key  ,  Object   value  ,  Object   version  )  throws   CacheException  { 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Inserting: "  +  key  )  ; 
try  { 
cache  .  lock  (  key  )  ; 
Lockable   lockable  =  (  Lockable  )  cache  .  get  (  key  )  ; 
if  (  lockable  ==  null  )  { 
cache  .  update  (  key  ,  new   Item  (  value  ,  version  ,  cache  .  nextTimestamp  (  )  )  )  ; 
if  (  log  .  isTraceEnabled  (  )  )  log  .  trace  (  "Inserted: "  +  key  )  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  finally  { 
cache  .  unlock  (  key  )  ; 
} 
} 




public   void   evict  (  Object   key  )  throws   CacheException  { 
} 




public   boolean   insert  (  Object   key  ,  Object   value  ,  Object   currentVersion  )  { 
return   false  ; 
} 




public   boolean   update  (  Object   key  ,  Object   value  ,  Object   currentVersion  ,  Object   previousVersion  )  { 
return   false  ; 
} 






private   boolean   isUnlockable  (  SoftLock   clientLock  ,  Lockable   myLock  )  throws   CacheException  { 
return   myLock  !=  null  &&  myLock  .  isLock  (  )  &&  clientLock  !=  null  &&  (  (  Lock  )  clientLock  )  .  getId  (  )  ==  (  (  Lock  )  myLock  )  .  getId  (  )  ; 
} 

public   static   interface   Lockable  { 

public   Lock   lock  (  long   timeout  ,  int   id  )  ; 

public   boolean   isLock  (  )  ; 

public   boolean   isGettable  (  long   txTimestamp  )  ; 

public   boolean   isPuttable  (  long   txTimestamp  ,  Object   newVersion  ,  Comparator   comparator  )  ; 
} 





public   static   final   class   Item   implements   Serializable  ,  Lockable  { 

private   final   long   freshTimestamp  ; 

private   final   Object   value  ; 

private   final   Object   version  ; 

public   Item  (  Object   value  ,  Object   version  ,  long   currentTimestamp  )  { 
this  .  value  =  value  ; 
this  .  version  =  version  ; 
freshTimestamp  =  currentTimestamp  ; 
} 




public   long   getFreshTimestamp  (  )  { 
return   freshTimestamp  ; 
} 




public   Object   getValue  (  )  { 
return   value  ; 
} 




public   Lock   lock  (  long   timeout  ,  int   id  )  { 
return   new   Lock  (  timeout  ,  id  ,  version  )  ; 
} 




public   boolean   isLock  (  )  { 
return   false  ; 
} 





public   boolean   isGettable  (  long   txTimestamp  )  { 
return   freshTimestamp  <  txTimestamp  ; 
} 




public   boolean   isPuttable  (  long   txTimestamp  ,  Object   newVersion  ,  Comparator   comparator  )  { 
return   version  !=  null  &&  comparator  .  compare  (  version  ,  newVersion  )  <  0  ; 
} 

public   String   toString  (  )  { 
return  "Item{version="  +  version  +  ",freshTimestamp="  +  freshTimestamp  ; 
} 
} 






public   static   final   class   Lock   implements   Serializable  ,  Lockable  ,  SoftLock  { 

private   long   unlockTimestamp  =  -  1  ; 

private   int   multiplicity  =  1  ; 

private   boolean   concurrentLock  =  false  ; 

private   long   timeout  ; 

private   final   int   id  ; 

private   final   Object   version  ; 

public   Lock  (  long   timeout  ,  int   id  ,  Object   version  )  { 
this  .  timeout  =  timeout  ; 
this  .  id  =  id  ; 
this  .  version  =  version  ; 
} 

public   long   getUnlockTimestamp  (  )  { 
return   unlockTimestamp  ; 
} 





public   Lock   lock  (  long   timeout  ,  int   id  )  { 
concurrentLock  =  true  ; 
multiplicity  ++  ; 
this  .  timeout  =  timeout  ; 
return   this  ; 
} 






public   void   unlock  (  long   currentTimestamp  )  { 
if  (  --  multiplicity  ==  0  )  { 
unlockTimestamp  =  currentTimestamp  ; 
} 
} 





public   boolean   isPuttable  (  long   txTimestamp  ,  Object   newVersion  ,  Comparator   comparator  )  { 
if  (  timeout  <  txTimestamp  )  return   true  ; 
if  (  multiplicity  >  0  )  return   false  ; 
return   version  ==  null  ?  unlockTimestamp  <  txTimestamp  :  comparator  .  compare  (  version  ,  newVersion  )  <  0  ; 
} 





public   boolean   wasLockedConcurrently  (  )  { 
return   concurrentLock  ; 
} 




public   boolean   isLock  (  )  { 
return   true  ; 
} 




public   boolean   isGettable  (  long   txTimestamp  )  { 
return   false  ; 
} 

public   int   getId  (  )  { 
return   id  ; 
} 

public   String   toString  (  )  { 
return  "Lock{id="  +  id  +  ",version="  +  version  +  ",multiplicity="  +  multiplicity  +  ",unlockTimestamp="  +  unlockTimestamp  ; 
} 
} 

public   String   toString  (  )  { 
return   cache  +  "(read-write)"  ; 
} 
} 

