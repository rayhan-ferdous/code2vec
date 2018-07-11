package   edu  .  oswego  .  cs  .  dl  .  util  .  concurrent  ; 

import   java  .  util  .  *  ; 








public   class   SyncSortedSet   extends   SyncSet   implements   SortedSet  { 





public   SyncSortedSet  (  SortedSet   set  ,  ReadWriteLock   rwl  )  { 
super  (  set  ,  rwl  .  readLock  (  )  ,  rwl  .  writeLock  (  )  )  ; 
} 







public   SyncSortedSet  (  SortedSet   set  ,  Sync   sync  )  { 
super  (  set  ,  sync  )  ; 
} 





public   SyncSortedSet  (  SortedSet   set  ,  Sync   readLock  ,  Sync   writeLock  )  { 
super  (  set  ,  readLock  ,  writeLock  )  ; 
} 

protected   SortedSet   baseSortedSet  (  )  { 
return  (  SortedSet  )  c_  ; 
} 

public   Comparator   comparator  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedSet  (  )  .  comparator  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object   first  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedSet  (  )  .  first  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedSet   headSet  (  Object   toElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedSet  (  baseSortedSet  (  )  .  headSet  (  toElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   Object   last  (  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   baseSortedSet  (  )  .  last  (  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedSet   subSet  (  Object   fromElement  ,  Object   toElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedSet  (  baseSortedSet  (  )  .  subSet  (  fromElement  ,  toElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 

public   SortedSet   tailSet  (  Object   fromElement  )  { 
boolean   wasInterrupted  =  beforeRead  (  )  ; 
try  { 
return   new   SyncSortedSet  (  baseSortedSet  (  )  .  tailSet  (  fromElement  )  ,  rd_  ,  wr_  )  ; 
}  finally  { 
afterRead  (  wasInterrupted  )  ; 
} 
} 
} 

