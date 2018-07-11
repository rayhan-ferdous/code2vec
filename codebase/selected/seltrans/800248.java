package   org  .  jikesrvm  .  scheduler  ; 

import   org  .  jikesrvm  .  runtime  .  VM_Process  ; 
import   org  .  jikesrvm  .  runtime  .  VM_Time  ; 







public   class   VM_Wait  { 

private   static   boolean   noIoWait  =  false  ; 







public   static   void   disableIoWait  (  )  { 
noIoWait  =  true  ; 
} 





public   static   void   sleep  (  long   millis  )  throws   InterruptedException  { 
VM_Thread   myThread  =  VM_Thread  .  getCurrentThread  (  )  ; 
myThread  .  wakeupCycle  =  VM_Time  .  cycles  (  )  +  VM_Time  .  millisToCycles  (  millis  )  ; 
VM_Proxy   proxy  =  new   VM_Proxy  (  myThread  ,  myThread  .  wakeupCycle  )  ; 
myThread  .  proxy  =  proxy  ; 
VM_Thread  .  sleepImpl  (  myThread  )  ; 
} 






private   static   long   getMaxWaitCycle  (  double   totalWaitTimeInSeconds  )  { 
long   maxWaitCycle  =  VM_Time  .  secsToCycles  (  totalWaitTimeInSeconds  )  ; 
if  (  maxWaitCycle  >=  0  )  { 
maxWaitCycle  +=  VM_Time  .  cycles  (  )  ; 
} 
return   maxWaitCycle  ; 
} 









public   static   VM_ThreadIOWaitData   ioWaitRead  (  int   fd  ,  double   totalWaitTime  )  { 
long   maxWaitCycle  =  getMaxWaitCycle  (  totalWaitTime  )  ; 
VM_ThreadIOWaitData   waitData  =  new   VM_ThreadIOWaitData  (  maxWaitCycle  )  ; 
waitData  .  readFds  =  new   int  [  ]  {  fd  }  ; 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
VM_Thread  .  ioWaitImpl  (  waitData  )  ; 
} 
return   waitData  ; 
} 




public   static   VM_ThreadIOWaitData   ioWaitRead  (  int   fd  )  { 
return   ioWaitRead  (  fd  ,  VM_ThreadEventConstants  .  WAIT_INFINITE  )  ; 
} 









public   static   VM_ThreadIOWaitData   ioWaitWrite  (  int   fd  ,  double   totalWaitTime  )  { 
long   maxWaitCycle  =  getMaxWaitCycle  (  totalWaitTime  )  ; 
VM_ThreadIOWaitData   waitData  =  new   VM_ThreadIOWaitData  (  maxWaitCycle  )  ; 
waitData  .  writeFds  =  new   int  [  ]  {  fd  }  ; 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
VM_Thread  .  ioWaitImpl  (  waitData  )  ; 
} 
return   waitData  ; 
} 




public   static   VM_ThreadIOWaitData   ioWaitWrite  (  int   fd  )  { 
return   ioWaitWrite  (  fd  ,  VM_ThreadEventConstants  .  WAIT_INFINITE  )  ; 
} 


















public   static   void   ioWaitSelect  (  int  [  ]  readFds  ,  int  [  ]  writeFds  ,  int  [  ]  exceptFds  ,  double   totalWaitTime  ,  boolean   fromNative  )  { 
long   maxWaitCycle  =  getMaxWaitCycle  (  totalWaitTime  )  ; 
VM_ThreadIOWaitData   waitData  =  new   VM_ThreadIOWaitData  (  maxWaitCycle  )  ; 
waitData  .  readFds  =  readFds  ; 
waitData  .  writeFds  =  writeFds  ; 
waitData  .  exceptFds  =  exceptFds  ; 
if  (  fromNative  )  { 
waitData  .  waitFlags  |=  VM_ThreadEventConstants  .  WAIT_NATIVE  ; 
} 
if  (  noIoWait  )  { 
waitData  .  markAllAsReady  (  )  ; 
}  else  { 
VM_Thread  .  ioWaitImpl  (  waitData  )  ; 
} 
} 










public   static   VM_ThreadProcessWaitData   processWait  (  VM_Process   process  ,  double   totalWaitTime  )  throws   InterruptedException  { 
long   maxWaitCycle  =  getMaxWaitCycle  (  totalWaitTime  )  ; 
VM_ThreadProcessWaitData   waitData  =  new   VM_ThreadProcessWaitData  (  process  .  getPid  (  )  ,  maxWaitCycle  )  ; 
VM_Thread  .  processWaitImpl  (  waitData  ,  process  )  ; 
return   waitData  ; 
} 
} 

