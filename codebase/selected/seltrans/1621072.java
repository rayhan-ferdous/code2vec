package   org  .  mmtk  .  utility  .  deque  ; 

import   org  .  mmtk  .  policy  .  RawPageSpace  ; 
import   org  .  mmtk  .  policy  .  Space  ; 
import   org  .  mmtk  .  utility  .  Constants  ; 
import   org  .  mmtk  .  utility  .  Log  ; 
import   org  .  mmtk  .  vm  .  Lock  ; 
import   org  .  mmtk  .  vm  .  VM  ; 
import   org  .  vmmagic  .  pragma  .  Entrypoint  ; 
import   org  .  vmmagic  .  pragma  .  Inline  ; 
import   org  .  vmmagic  .  pragma  .  Uninterruptible  ; 
import   org  .  vmmagic  .  unboxed  .  Address  ; 
import   org  .  vmmagic  .  unboxed  .  Offset  ; 






@  Uninterruptible 
public   class   SharedDeque   extends   Deque   implements   Constants  { 

private   static   final   boolean   DISABLE_WAITING  =  true  ; 

private   static   final   Offset   NEXT_OFFSET  =  Offset  .  zero  (  )  ; 

private   static   final   Offset   PREV_OFFSET  =  Offset  .  fromIntSignExtend  (  BYTES_IN_ADDRESS  )  ; 

private   static   final   boolean   TRACE  =  false  ; 

private   static   final   boolean   TRACE_DETAIL  =  false  ; 

private   static   final   boolean   TRACE_BLOCKERS  =  false  ; 




public   SharedDeque  (  String   name  ,  RawPageSpace   rps  ,  int   arity  )  { 
this  .  rps  =  rps  ; 
this  .  arity  =  arity  ; 
this  .  name  =  name  ; 
lock  =  VM  .  newLock  (  "SharedDeque"  )  ; 
clearCompletionFlag  (  )  ; 
head  =  HEAD_INITIAL_VALUE  ; 
tail  =  TAIL_INITIAL_VALUE  ; 
} 


@  Inline 
final   int   getArity  (  )  { 
return   arity  ; 
} 








final   void   enqueue  (  Address   buf  ,  int   arity  ,  boolean   toTail  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  arity  ==  this  .  arity  )  ; 
lock  (  )  ; 
if  (  toTail  )  { 
setNext  (  buf  ,  Address  .  zero  (  )  )  ; 
if  (  tail  .  EQ  (  TAIL_INITIAL_VALUE  )  )  head  =  buf  ;  else   setNext  (  tail  ,  buf  )  ; 
setPrev  (  buf  ,  tail  )  ; 
tail  =  buf  ; 
}  else  { 
setPrev  (  buf  ,  Address  .  zero  (  )  )  ; 
if  (  head  .  EQ  (  HEAD_INITIAL_VALUE  )  )  tail  =  buf  ;  else   setPrev  (  head  ,  buf  )  ; 
setNext  (  buf  ,  head  )  ; 
head  =  buf  ; 
} 
bufsenqueued  ++  ; 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  checkDequeLength  (  bufsenqueued  )  )  ; 
unlock  (  )  ; 
} 

public   final   void   clearDeque  (  int   arity  )  { 
Address   buf  =  dequeue  (  arity  )  ; 
while  (  !  buf  .  isZero  (  )  )  { 
free  (  bufferStart  (  buf  )  )  ; 
buf  =  dequeue  (  arity  )  ; 
} 
setCompletionFlag  (  )  ; 
} 

@  Inline 
final   Address   dequeue  (  int   arity  )  { 
return   dequeue  (  arity  ,  false  )  ; 
} 

final   Address   dequeue  (  int   arity  ,  boolean   fromTail  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  arity  ==  this  .  arity  )  ; 
return   dequeue  (  false  ,  fromTail  )  ; 
} 

@  Inline 
final   Address   dequeueAndWait  (  int   arity  )  { 
return   dequeueAndWait  (  arity  ,  false  )  ; 
} 

final   Address   dequeueAndWait  (  int   arity  ,  boolean   fromTail  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  arity  ==  this  .  arity  )  ; 
Address   buf  =  dequeue  (  false  ,  fromTail  )  ; 
if  (  buf  .  isZero  (  )  &&  (  !  complete  (  )  )  )  { 
buf  =  dequeue  (  true  ,  fromTail  )  ; 
} 
return   buf  ; 
} 






public   final   void   prepare  (  )  { 
if  (  DISABLE_WAITING  )  { 
prepareNonBlocking  (  )  ; 
}  else  { 
prepare  (  VM  .  activePlan  .  collector  (  )  .  parallelWorkerCount  (  )  )  ; 
} 
} 





public   final   void   prepareNonBlocking  (  )  { 
prepare  (  1  )  ; 
} 







private   void   prepare  (  int   consumers  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  numConsumersWaiting  ==  0  )  ; 
setNumConsumers  (  consumers  )  ; 
clearCompletionFlag  (  )  ; 
} 

public   final   void   reset  (  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  numConsumersWaiting  ==  0  )  ; 
clearCompletionFlag  (  )  ; 
setNumConsumersWaiting  (  0  )  ; 
assertExhausted  (  )  ; 
} 

public   final   void   assertExhausted  (  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  head  .  isZero  (  )  &&  tail  .  isZero  (  )  )  ; 
} 

@  Inline 
final   Address   alloc  (  )  { 
Address   rtn  =  rps  .  acquire  (  PAGES_PER_BUFFER  )  ; 
if  (  rtn  .  isZero  (  )  )  { 
Space  .  printUsageMB  (  )  ; 
VM  .  assertions  .  fail  (  "Failed to allocate space for queue.  Is metadata virtual memory exhausted?"  )  ; 
} 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  rtn  .  EQ  (  bufferStart  (  rtn  )  )  )  ; 
return   rtn  ; 
} 

@  Inline 
final   void   free  (  Address   buf  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  buf  .  EQ  (  bufferStart  (  buf  )  )  &&  !  buf  .  isZero  (  )  )  ; 
rps  .  release  (  buf  )  ; 
} 

@  Inline 
public   final   int   enqueuedPages  (  )  { 
return  (  int  )  (  bufsenqueued  *  PAGES_PER_BUFFER  )  ; 
} 


private   final   String   name  ; 


private   RawPageSpace   rps  ; 


private   final   int   arity  ; 


@  Entrypoint 
private   volatile   int   completionFlag  ; 


@  Entrypoint 
private   volatile   int   numConsumers  ; 


@  Entrypoint 
private   volatile   int   numConsumersWaiting  ; 


@  Entrypoint 
protected   volatile   Address   head  ; 


@  Entrypoint 
protected   volatile   Address   tail  ; 

@  Entrypoint 
private   volatile   int   bufsenqueued  ; 

private   Lock   lock  ; 

private   static   final   long   WARN_PERIOD  =  (  long  )  (  2  *  1E9  )  ; 

private   static   final   long   TIMEOUT_PERIOD  =  10  *  WARN_PERIOD  ; 










private   Address   dequeue  (  boolean   waiting  ,  boolean   fromTail  )  { 
lock  (  )  ; 
Address   rtn  =  (  (  fromTail  )  ?  tail  :  head  )  ; 
if  (  rtn  .  isZero  (  )  )  { 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  tail  .  isZero  (  )  &&  head  .  isZero  (  )  )  ; 
if  (  waiting  )  { 
int   ordinal  =  TRACE  ?  0  :  VM  .  activePlan  .  collector  (  )  .  getId  (  )  ; 
setNumConsumersWaiting  (  numConsumersWaiting  +  1  )  ; 
while  (  rtn  .  isZero  (  )  )  { 
if  (  numConsumersWaiting  ==  numConsumers  )  setCompletionFlag  (  )  ; 
if  (  TRACE  )  { 
Log  .  write  (  "-- ("  )  ; 
Log  .  write  (  ordinal  )  ; 
Log  .  write  (  ") joining wait queue of SharedDeque("  )  ; 
Log  .  write  (  name  )  ; 
Log  .  write  (  ") "  )  ; 
Log  .  write  (  numConsumersWaiting  )  ; 
Log  .  write  (  "/"  )  ; 
Log  .  write  (  numConsumers  )  ; 
Log  .  write  (  " consumers waiting"  )  ; 
if  (  complete  (  )  )  Log  .  write  (  " WAIT COMPLETE"  )  ; 
Log  .  writeln  (  )  ; 
if  (  TRACE_BLOCKERS  )  VM  .  assertions  .  dumpStack  (  )  ; 
} 
unlock  (  )  ; 
spinWait  (  fromTail  )  ; 
if  (  complete  (  )  )  { 
if  (  TRACE  )  { 
Log  .  write  (  "-- ("  )  ; 
Log  .  write  (  ordinal  )  ; 
Log  .  writeln  (  ") EXITING"  )  ; 
} 
lock  (  )  ; 
setNumConsumersWaiting  (  numConsumersWaiting  -  1  )  ; 
unlock  (  )  ; 
return   Address  .  zero  (  )  ; 
} 
lock  (  )  ; 
rtn  =  (  (  fromTail  )  ?  tail  :  head  )  ; 
} 
setNumConsumersWaiting  (  numConsumersWaiting  -  1  )  ; 
if  (  TRACE  )  { 
Log  .  write  (  "-- ("  )  ; 
Log  .  write  (  ordinal  )  ; 
Log  .  write  (  ") resuming work "  )  ; 
Log  .  write  (  " n="  )  ; 
Log  .  writeln  (  numConsumersWaiting  )  ; 
} 
}  else  { 
unlock  (  )  ; 
return   Address  .  zero  (  )  ; 
} 
} 
if  (  fromTail  )  { 
setTail  (  getPrev  (  tail  )  )  ; 
if  (  head  .  EQ  (  rtn  )  )  { 
setHead  (  Address  .  zero  (  )  )  ; 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  tail  .  isZero  (  )  )  ; 
}  else  { 
setNext  (  tail  ,  Address  .  zero  (  )  )  ; 
} 
}  else  { 
setHead  (  getNext  (  head  )  )  ; 
if  (  tail  .  EQ  (  rtn  )  )  { 
setTail  (  Address  .  zero  (  )  )  ; 
if  (  VM  .  VERIFY_ASSERTIONS  )  VM  .  assertions  .  _assert  (  head  .  isZero  (  )  )  ; 
}  else  { 
setPrev  (  head  ,  Address  .  zero  (  )  )  ; 
} 
} 
bufsenqueued  --  ; 
unlock  (  )  ; 
return   rtn  ; 
} 






private   void   spinWait  (  boolean   fromTail  )  { 
long   startNano  =  0  ; 
long   lastElapsedNano  =  0  ; 
while  (  true  )  { 
long   startCycles  =  VM  .  statistics  .  cycles  (  )  ; 
long   endCycles  =  startCycles  +  (  (  long  )  1e9  )  ; 
long   nowCycles  ; 
do  { 
VM  .  memory  .  isync  (  )  ; 
Address   rtn  =  (  (  fromTail  )  ?  tail  :  head  )  ; 
if  (  !  rtn  .  isZero  (  )  ||  complete  (  )  )  return  ; 
nowCycles  =  VM  .  statistics  .  cycles  (  )  ; 
}  while  (  startCycles  <  nowCycles  &&  nowCycles  <  endCycles  )  ; 
lock  (  )  ; 
if  (  startNano  ==  0  )  { 
startNano  =  VM  .  statistics  .  nanoTime  (  )  ; 
}  else  { 
long   nowNano  =  VM  .  statistics  .  nanoTime  (  )  ; 
long   elapsedNano  =  nowNano  -  startNano  ; 
if  (  elapsedNano  -  lastElapsedNano  >  WARN_PERIOD  )  { 
Log  .  write  (  "GC Warning: SharedDeque("  )  ; 
Log  .  write  (  name  )  ; 
Log  .  write  (  ") wait has reached "  )  ; 
Log  .  write  (  VM  .  statistics  .  nanosToSecs  (  elapsedNano  )  )  ; 
Log  .  write  (  ", "  )  ; 
Log  .  write  (  numConsumersWaiting  )  ; 
Log  .  write  (  "/"  )  ; 
Log  .  write  (  numConsumers  )  ; 
Log  .  writeln  (  " threads waiting"  )  ; 
lastElapsedNano  =  elapsedNano  ; 
} 
if  (  elapsedNano  >  TIMEOUT_PERIOD  )  { 
unlock  (  )  ; 
VM  .  assertions  .  fail  (  "GC Error: SharedDeque Timeout"  )  ; 
} 
} 
unlock  (  )  ; 
} 
} 







private   static   void   setNext  (  Address   buf  ,  Address   next  )  { 
buf  .  store  (  next  ,  NEXT_OFFSET  )  ; 
} 







protected   final   Address   getNext  (  Address   buf  )  { 
return   buf  .  loadAddress  (  NEXT_OFFSET  )  ; 
} 







private   void   setPrev  (  Address   buf  ,  Address   prev  )  { 
buf  .  store  (  prev  ,  PREV_OFFSET  )  ; 
} 







protected   final   Address   getPrev  (  Address   buf  )  { 
return   buf  .  loadAddress  (  PREV_OFFSET  )  ; 
} 








private   boolean   checkDequeLength  (  int   length  )  { 
Address   top  =  head  ; 
int   l  =  0  ; 
while  (  !  top  .  isZero  (  )  &&  l  <=  length  )  { 
top  =  getNext  (  top  )  ; 
l  ++  ; 
} 
return   l  ==  length  ; 
} 





private   void   lock  (  )  { 
lock  .  acquire  (  )  ; 
} 





private   void   unlock  (  )  { 
lock  .  release  (  )  ; 
} 




private   boolean   complete  (  )  { 
return   completionFlag  ==  1  ; 
} 




@  Inline 
private   void   setCompletionFlag  (  )  { 
if  (  TRACE_DETAIL  )  { 
Log  .  writeln  (  "# setCompletionFlag: "  )  ; 
} 
completionFlag  =  1  ; 
} 




@  Inline 
private   void   clearCompletionFlag  (  )  { 
if  (  TRACE_DETAIL  )  { 
Log  .  writeln  (  "# clearCompletionFlag: "  )  ; 
} 
completionFlag  =  0  ; 
} 

@  Inline 
private   void   setNumConsumers  (  int   newNumConsumers  )  { 
if  (  TRACE_DETAIL  )  { 
Log  .  write  (  "# Num consumers "  )  ; 
Log  .  writeln  (  newNumConsumers  )  ; 
} 
numConsumers  =  newNumConsumers  ; 
} 

@  Inline 
private   void   setNumConsumersWaiting  (  int   newNCW  )  { 
if  (  TRACE_DETAIL  )  { 
Log  .  write  (  "# Num consumers waiting "  )  ; 
Log  .  writeln  (  newNCW  )  ; 
} 
numConsumersWaiting  =  newNCW  ; 
} 

@  Inline 
private   void   setHead  (  Address   newHead  )  { 
head  =  newHead  ; 
VM  .  memory  .  sync  (  )  ; 
} 

@  Inline 
private   void   setTail  (  Address   newTail  )  { 
tail  =  newTail  ; 
VM  .  memory  .  sync  (  )  ; 
} 
} 

