



















public   class   VM_Finalizer  { 

public   static   boolean   finalizeOnExit  =  false  ; 

static   VM_FinalizerListElement   live_head  ; 

static   VM_FinalizerListElement   finalize_head  ; 

static   int   live_count  ; 

static   int   finalize_count  ; 

private   static   Object   locker  ; 

static   boolean   foundFinalizableObject  ; 

static   int   foundFinalizableCount  =  0  ; 

private   static   final   boolean   TRACE  =  false  ; 

private   static   final   boolean   TRACE_DETAIL  =  false  ; 

private   static   final   boolean   PRINT_FINALIZABLE_COUNT  =  false  ; 

private   static   final   boolean   COUNT_BY_TYPES  =  false  ; 

public   static   void   setup  (  )  { 
locker  =  new   Object  (  )  ; 
} 

static   final   void   addElement  (  int   item  )  { 
VM_Magic  .  pragmaNoInline  (  )  ; 
synchronized  (  locker  )  { 
live_count  ++  ; 
if  (  TRACE_DETAIL  )  VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  " addElement	called, count = "  ,  live_count  )  ; 
VM_FinalizerListElement   le  =  new   VM_FinalizerListElement  (  item  )  ; 
VM_FinalizerListElement   old  =  live_head  ; 
live_head  =  le  ; 
le  .  next  =  old  ; 
} 
} 

static   final   void   addElement  (  Object   item  )  { 
VM_Magic  .  pragmaNoInline  (  )  ; 
synchronized  (  locker  )  { 
live_count  ++  ; 
if  (  TRACE_DETAIL  )  VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  " addElement   called, count = "  ,  live_count  )  ; 
VM_FinalizerListElement   le  =  new   VM_FinalizerListElement  (  item  )  ; 
VM_FinalizerListElement   old  =  live_head  ; 
live_head  =  le  ; 
le  .  next  =  old  ; 
} 
} 





static   final   Object   get  (  )  { 
VM_FinalizerListElement   temp  =  finalize_head  ; 
if  (  temp  ==  null  )  return   null  ; 
finalize_head  =  temp  .  next  ; 
finalize_count  --  ; 
if  (  TRACE_DETAIL  )  { 
VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  "get returning "  ,  VM_Magic  .  objectAsAddress  (  temp  .  pointer  )  )  ; 
VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  "finalize count is "  ,  finalize_count  )  ; 
} 
return   temp  .  pointer  ; 
} 







static   final   boolean   existObjectsWithFinalizers  (  )  { 
return  (  live_head  !=  null  )  ; 
} 





static   final   void   finalizeAll  (  )  { 
VM_FinalizerListElement   le  =  live_head  ; 
VM_FinalizerListElement   from  =  live_head  ; 
while  (  le  !=  null  )  { 
live_count  --  ; 
finalize_count  ++  ; 
if  (  TRACE  )  VM_Scheduler  .  traceHex  (  "\n in finalizeall:"  ,  "le.value ="  ,  VM_Magic  .  objectAsAddress  (  le  .  pointer  )  )  ; 
VM_FinalizerListElement   current  =  le  .  next  ; 
if  (  le  ==  live_head  )  live_head  =  current  ;  else   from  .  next  =  current  ; 
VM_FinalizerListElement   temp  =  finalize_head  ; 
finalize_head  =  le  ; 
le  .  next  =  temp  ; 
le  =  current  ; 
} 
if  (  !  VM_Scheduler  .  finalizerQueue  .  isEmpty  (  )  )  { 
VM_Thread   tt  =  VM_Scheduler  .  finalizerQueue  .  dequeue  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  tt  )  ; 
} 
} 





static   final   void   moveToFinalizable  (  )  { 
if  (  TRACE  )  VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  " move to finalizable "  )  ; 
boolean   added  =  false  ; 
boolean   is_live  =  false  ; 
foundFinalizableObject  =  false  ; 
foundFinalizableCount  =  0  ; 
int   initial_finalize_count  =  finalize_count  ; 
VM_FinalizerListElement   le  =  live_head  ; 
VM_FinalizerListElement   from  =  live_head  ; 
while  (  le  !=  null  )  { 
is_live  =  VM_Allocator  .  processFinalizerListElement  (  le  )  ; 
if  (  is_live  )  { 
from  =  le  ; 
le  =  le  .  next  ; 
continue  ; 
}  else  { 
added  =  true  ; 
live_count  --  ; 
finalize_count  ++  ; 
if  (  TRACE  )  VM_Scheduler  .  traceHex  (  "\n moving to finalizer:"  ,  "le.value ="  ,  VM_Magic  .  objectAsAddress  (  le  .  pointer  )  )  ; 
VM_FinalizerListElement   current  =  le  .  next  ; 
if  (  le  ==  live_head  )  live_head  =  current  ;  else   from  .  next  =  current  ; 
VM_FinalizerListElement   temp  =  finalize_head  ; 
finalize_head  =  le  ; 
le  .  next  =  temp  ; 
le  =  current  ; 
} 
} 
if  (  added  )  { 
if  (  TRACE  )  VM_Scheduler  .  trace  (  " VM_Finalizer: "  ,  " added was true"  )  ; 
foundFinalizableObject  =  true  ; 
foundFinalizableCount  =  finalize_count  -  initial_finalize_count  ; 
if  (  !  VM_Scheduler  .  finalizerQueue  .  isEmpty  (  )  )  { 
VM_Thread   tt  =  VM_Scheduler  .  finalizerQueue  .  dequeue  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  tt  )  ; 
} 
} 
if  (  PRINT_FINALIZABLE_COUNT  &&  VM  .  verboseGC  )  { 
VM  .  sysWrite  (  "<GC "  )  ; 
VM  .  sysWrite  (  VM_Collector  .  collectionCount  (  )  ,  false  )  ; 
VM  .  sysWrite  (  " moveToFinalizable: finalize_count: before = "  )  ; 
VM  .  sysWrite  (  initial_finalize_count  ,  false  )  ; 
VM  .  sysWrite  (  " after = "  )  ; 
VM  .  sysWrite  (  finalize_count  ,  false  )  ; 
VM  .  sysWrite  (  ">\n"  )  ; 
} 
} 

static   void   schedule  (  )  { 
if  (  (  finalize_head  !=  null  )  &&  !  VM_Scheduler  .  finalizerQueue  .  isEmpty  (  )  )  { 
VM_Thread   t  =  VM_Scheduler  .  finalizerQueue  .  dequeue  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  t  )  ; 
} 
} 

static   int   countHasFinalizer  (  )  { 
int   count  =  0  ; 
VM_FinalizerListElement   le  =  live_head  ; 
while  (  le  !=  null  )  { 
count  ++  ; 
if  (  COUNT_BY_TYPES  )  { 
VM_Type   type  =  VM_Magic  .  getObjectType  (  VM_Magic  .  addressAsObject  (  le  .  value  )  )  ; 
type  .  liveCount  ++  ; 
} 
le  =  le  .  next  ; 
} 
return   count  ; 
} 

static   int   countToBeFinalized  (  )  { 
int   count  =  0  ; 
VM_FinalizerListElement   le  =  finalize_head  ; 
while  (  le  !=  null  )  { 
count  ++  ; 
if  (  COUNT_BY_TYPES  )  { 
VM_Type   type  =  VM_Magic  .  getObjectType  (  le  .  pointer  )  ; 
type  .  liveCount  ++  ; 
} 
le  =  le  .  next  ; 
} 
return   count  ; 
} 




static   final   void   dump_live  (  )  { 
VM_Scheduler  .  trace  (  " VM_Finalizer.dump_live"  ,  "cnt is "  ,  live_count  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
VM_FinalizerListElement   le  =  live_head  ; 
while  (  le  !=  null  )  { 
VM  .  sysWrite  (  " In live_list: object type is "  )  ; 
VM_GCUtil  .  printclass  (  le  .  value  )  ; 
VM  .  sysWrite  (  " at "  )  ; 
VM  .  sysWrite  (  le  .  value  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
le  =  le  .  next  ; 
} 
} 




static   final   void   dump_finalize  (  )  { 
VM_Scheduler  .  trace  (  " VM_Finalizer.dump_finalize"  ,  "cnt is "  ,  finalize_count  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
VM_FinalizerListElement   le  =  finalize_head  ; 
while  (  le  !=  null  )  { 
VM  .  sysWrite  (  " In finalize_list: object type is "  )  ; 
VM_GCUtil  .  printclass  (  le  .  value  )  ; 
VM  .  sysWrite  (  " at "  )  ; 
VM  .  sysWrite  (  le  .  value  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
le  =  le  .  next  ; 
} 
} 
} 

