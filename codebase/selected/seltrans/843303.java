package   EDU  .  oswego  .  cs  .  dl  .  util  .  concurrent  ; 

import   java  .  util  .  *  ; 








public   class   SyncSortedMap   extends   SyncMap   implements   SortedMap  { 







public   SyncSortedMap  (  SortedMap   map  ,  Sync   sync  )  { 
this  (  map  ,  sync  ,  sync  )  ; 
} 





public   SyncSortedMap  (  SortedMap   map  ,  ReadWriteLock   rwl  )  { 
super  (  map  ,  rwl  .  readLock  (  )  ,  rwl  .  writeLock  (  )  )  ; 
} 





public   SyncSortedMap  (  SortedMap   map  ,  Sync   readLock  ,  Sync   writeLock  )  { 
super  (  map  ,  readLock  ,  writeLock  )  ; 
} 

protected   SortedMap   baseSortedMap  (  )  { 
return  (  SortedMap  )  c_  ; 
} 

public   Comparator   comparator  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedMap  (  )  .  comparator  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object   firstKey  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedMap  (  )  .  firstKey  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object   lastKey  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedMap  (  )  .  lastKey  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedMap   subMap  (  Object   fromElement  ,  Object   toElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedMap  (  baseSortedMap  (  )  .  subMap  (  fromElement  ,  toElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedMap   headMap  (  Object   toElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedMap  (  baseSortedMap  (  )  .  headMap  (  toElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedMap   tailMap  (  Object   fromElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedMap  (  baseSortedMap  (  )  .  tailMap  (  fromElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 
} 

