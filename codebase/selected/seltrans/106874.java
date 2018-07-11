package   oracle  .  toplink  .  essentials  .  internal  .  identitymaps  ; 

import   java  .  util  .  *  ; 











public   class   WeakIdentityMap   extends   FullIdentityMap  { 


protected   int   cleanupCount  ; 


protected   int   cleanupSize  ; 

public   WeakIdentityMap  (  int   size  )  { 
super  (  size  )  ; 
this  .  cleanupCount  =  0  ; 
this  .  cleanupSize  =  size  ; 
} 








protected   void   cleanupDeadCacheKeys  (  )  { 
for  (  Enumeration   keysEnum  =  getCacheKeys  (  )  .  elements  (  )  ;  keysEnum  .  hasMoreElements  (  )  ;  )  { 
CacheKey   key  =  (  CacheKey  )  keysEnum  .  nextElement  (  )  ; 
if  (  key  .  getObject  (  )  ==  null  )  { 
if  (  key  .  acquireNoWait  (  )  )  { 
try  { 
if  (  key  .  getObject  (  )  ==  null  )  { 
getCacheKeys  (  )  .  remove  (  key  )  ; 
} 
}  finally  { 
key  .  release  (  )  ; 
} 
} 
} 
} 
} 

public   CacheKey   createCacheKey  (  Vector   primaryKey  ,  Object   object  ,  Object   writeLockValue  ,  long   readTime  )  { 
return   new   WeakCacheKey  (  primaryKey  ,  object  ,  writeLockValue  ,  readTime  )  ; 
} 




protected   int   getCleanupCount  (  )  { 
return   cleanupCount  ; 
} 

protected   void   setCleanupCount  (  int   cleanupCount  )  { 
this  .  cleanupCount  =  cleanupCount  ; 
} 




protected   int   getCleanupSize  (  )  { 
return   cleanupSize  ; 
} 

protected   void   setCleanupSize  (  int   cleanupSize  )  { 
this  .  cleanupSize  =  cleanupSize  ; 
} 




protected   void   put  (  CacheKey   cacheKey  )  { 
synchronized  (  this  )  { 
if  (  getCleanupCount  (  )  >  getCleanupSize  (  )  )  { 
cleanupDeadCacheKeys  (  )  ; 
setCleanupCount  (  0  )  ; 
if  (  getSize  (  )  >  getCleanupSize  (  )  )  { 
setCleanupSize  (  getSize  (  )  )  ; 
} 
} 
setCleanupCount  (  getCleanupCount  (  )  +  1  )  ; 
} 
super  .  put  (  cacheKey  )  ; 
} 
} 

