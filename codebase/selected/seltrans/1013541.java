package   EDU  .  oswego  .  cs  .  dl  .  util  .  concurrent  ; 

import   java  .  util  .  *  ; 










































































































































































public   class   SyncCollection   implements   Collection  { 

protected   final   Collection   c_  ; 

protected   final   Sync   rd_  ; 

protected   final   Sync   wr_  ; 

protected   final   SynchronizedLong   syncFailures_  =  new   SynchronizedLong  (  0  )  ; 












public   SyncCollection  (  Collection   collection  ,  Sync   sync  )  { 
this  (  collection  ,  sync  ,  sync  )  ; 
} 











public   SyncCollection  (  Collection   collection  ,  ReadWriteLock   rwl  )  { 
this  (  collection  ,  rwl  .  readLock  (  )  ,  rwl  .  writeLock  (  )  )  ; 
} 





public   SyncCollection  (  Collection   collection  ,  Sync   readLock  ,  Sync   writeLock  )  { 
c_  =  collection  ; 
rd_  =  readLock  ; 
wr_  =  writeLock  ; 
} 




public   Sync   readerSync  (  )  { 
return   rd_  ; 
} 




public   Sync   writerSync  (  )  { 
return   wr_  ; 
} 




public   long   syncFailures  (  )  { 
return   syncFailures_  .  get  (  )  ; 
} 


protected   boolean   beforeRead  (  )  { 
try  { 
rd_  .  acquire  (  )  ; 
return   false  ; 
}  catch  (  InterruptedException   ex  )  { 
syncFailures_  .  increment  (  )  ; 
return   true  ; 
} 
} 


protected   void   afterRead  (  boolean   wasInterrupted  )  { 
if  (  wasInterrupted  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
}  else   rd_  .  release  (  )  ; 
} 

public   int   size  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  size  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   boolean   isEmpty  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  isEmpty  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   boolean   contains  (  Object   o  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  contains  (  o  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object  [  ]  toArray  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  toArray  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object  [  ]  toArray  (  Object  [  ]  a  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  toArray  (  a  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   boolean   containsAll  (  Collection   coll  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  containsAll  (  coll  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   boolean   add  (  Object   o  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
return   c_  .  add  (  o  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

public   boolean   remove  (  Object   o  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
return   c_  .  remove  (  o  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

public   boolean   addAll  (  Collection   coll  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
return   c_  .  addAll  (  coll  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

public   boolean   removeAll  (  Collection   coll  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
return   c_  .  removeAll  (  coll  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

public   boolean   retainAll  (  Collection   coll  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
return   c_  .  retainAll  (  coll  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

public   void   clear  (  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
c_  .  clear  (  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 


public   Iterator   unprotectedIterator  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   c_  .  iterator  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Iterator   iterator  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncCollectionIterator  (  c_  .  iterator  (  )  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   class   SyncCollectionIterator   implements   Iterator  { 

protected   final   Iterator   baseIterator_  ; 

SyncCollectionIterator  (  Iterator   baseIterator  )  { 
baseIterator_  =  baseIterator  ; 
} 

public   boolean   hasNext  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseIterator_  .  hasNext  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object   next  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseIterator_  .  next  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   void   remove  (  )  { 
try  { 
wr_  .  acquire  (  )  ; 
try  { 
baseIterator_  .  remove  (  )  ; 
}  finally  { 
wr_  .  release  (  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
Thread  .  currentThread  (  )  .  interrupt  (  )  ; 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 
} 
} 

