package   org  .  jikesrvm  .  scheduler  .  greenthreads  ; 

import   org  .  jikesrvm  .  runtime  .  Time  ; 
import   org  .  jikesrvm  .  scheduler  .  Processor  ; 
import   org  .  jikesrvm  .  scheduler  .  Scheduler  ; 
import   org  .  vmmagic  .  pragma  .  Uninterruptible  ; 







class   Wait  { 

private   static   boolean   noIoWait  =  false  ; 







@  Uninterruptible 
public   static   void   disableIoWait  (  )  { 
noIoWait  =  true  ; 
} 






private   static   long   getMaxWaitNano  (  double   totalWaitTimeInSeconds  )  { 
long   maxWaitNano  =  (  long  )  (  totalWaitTimeInSeconds  *  1e9  )  ; 
if  (  maxWaitNano  >=  0  )  { 
maxWaitNano  +=  Time  .  nanoTime  (  )  ; 
} 
return   maxWaitNano  ; 
} 









public   static   ThreadIOWaitData   ioWaitRead  (  int   fd  ,  double   totalWaitTime  )  { 
long   maxWaitNano  =  getMaxWaitNano  (  totalWaitTime  )  ; 
ThreadIOWaitData   waitData  =  new   ThreadIOWaitData  (  maxWaitNano  )  ; 
waitData  .  readFds  =  new   int  [  ]  {  fd  }  ; 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
GreenThread  .  ioWaitImpl  (  waitData  )  ; 
} 
return   waitData  ; 
} 




public   static   ThreadIOWaitData   ioWaitRead  (  int   fd  )  { 
return   ioWaitRead  (  fd  ,  ThreadEventConstants  .  WAIT_INFINITE  )  ; 
} 









public   static   ThreadIOWaitData   ioWaitWrite  (  int   fd  ,  double   totalWaitTime  )  { 
long   maxWaitNano  =  getMaxWaitNano  (  totalWaitTime  )  ; 
ThreadIOWaitData   waitData  =  new   ThreadIOWaitData  (  maxWaitNano  )  ; 
waitData  .  writeFds  =  new   int  [  ]  {  fd  }  ; 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
GreenThread  .  ioWaitImpl  (  waitData  )  ; 
} 
return   waitData  ; 
} 




public   static   ThreadIOWaitData   ioWaitWrite  (  int   fd  )  { 
return   ioWaitWrite  (  fd  ,  ThreadEventConstants  .  WAIT_INFINITE  )  ; 
} 



















public   static   int   ioWaitSelect  (  int  [  ]  readFds  ,  int  [  ]  writeFds  ,  int  [  ]  exceptFds  ,  double   totalWaitTime  ,  boolean   fromNative  )  { 
if  (  fromNative  )  { 
if  (  !  Processor  .  getCurrentProcessor  (  )  .  threadSwitchingEnabled  (  )  ||  Scheduler  .  getCurrentThread  (  )  .  getDisallowAllocationsByThisThread  (  )  )  { 
return  -  1  ; 
} 
} 
long   maxWaitNano  =  getMaxWaitNano  (  totalWaitTime  )  ; 
ThreadIOWaitData   waitData  =  new   ThreadIOWaitData  (  maxWaitNano  )  ; 
waitData  .  readFds  =  readFds  ; 
waitData  .  writeFds  =  writeFds  ; 
waitData  .  exceptFds  =  exceptFds  ; 
if  (  fromNative  )  { 
waitData  .  setNative  (  )  ; 
} 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
GreenThread  .  ioWaitImpl  (  waitData  )  ; 
} 
return   0  ; 
} 










public   static   ThreadProcessWaitData   processWait  (  VMProcess   process  ,  double   totalWaitTime  )  throws   InterruptedException  { 
long   maxWaitNano  =  getMaxWaitNano  (  totalWaitTime  )  ; 
ThreadProcessWaitData   waitData  =  new   ThreadProcessWaitData  (  process  .  getPid  (  )  ,  maxWaitNano  )  ; 
GreenThread  .  processWaitImpl  (  waitData  ,  process  )  ; 
return   waitData  ; 
} 
} 

