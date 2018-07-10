package   com  .  ibm  .  JikesRVM  ; 


















public   final   class   VM_ThreadIOQueue   extends   VM_ThreadEventWaitQueue   implements   VM_Uninterruptible  ,  VM_ThreadEventConstants  ,  VM_ThreadIOConstants  { 








private   static   class   WaitDataDowncaster   extends   VM_ThreadEventWaitDataVisitor   implements   VM_Uninterruptible  { 

public   VM_ThreadIOWaitData   waitData  ; 

public   void   visitThreadIOWaitData  (  VM_ThreadIOWaitData   waitData  )  { 
this  .  waitData  =  waitData  ; 
} 

public   void   visitThreadProcessWaitData  (  VM_ThreadProcessWaitData   waitData  )  { 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  false  )  ; 
} 
} 





private   WaitDataDowncaster   myDowncaster  =  new   WaitDataDowncaster  (  )  ; 




private   int   id  ; 

private   static   final   int   FD_SETSIZE  =  2048  ; 





private   int  [  ]  allFds  =  new   int  [  3  *  FD_SETSIZE  ]  ; 


public   static   final   int   READ_OFFSET  =  0  *  FD_SETSIZE  ; 


public   static   final   int   WRITE_OFFSET  =  1  *  FD_SETSIZE  ; 


public   static   final   int   EXCEPT_OFFSET  =  2  *  FD_SETSIZE  ; 





private   int   numKilledInJava  ; 


public   static   VM_ProcessorLock   selectInProgressMutex  =  new   VM_ProcessorLock  (  )  ; 










private   static   int   addFileDescriptors  (  int   dest  [  ]  ,  int   offset  ,  int  [  ]  src  )  { 
for  (  int   i  =  0  ;  i  <  src  .  length  ;  ++  i  )  { 
dest  [  offset  ++  ]  =  src  [  i  ]  ; 
} 
return   src  .  length  ; 
} 







private   static   boolean   isKilled  (  VM_Thread   thread  )  { 
return  (  thread  .  waitData  .  waitFlags  &  WAIT_NATIVE  )  !=  0  &&  thread  .  externalInterrupt  !=  null  &&  thread  .  throwInterruptWhenScheduled  ; 
} 














private   int   updateStatus  (  int  [  ]  waitDataFds  ,  int   waitDataOffset  ,  int  [  ]  selectFds  ,  int   setOffset  )  { 
if  (  waitDataFds  ==  null  )  return   0  ; 
int   numReady  =  0  ; 
int   selectIndex  =  setOffset  +  waitDataOffset  ; 
for  (  int   i  =  0  ;  i  <  waitDataFds  .  length  ;  ++  i  )  { 
int   fd  =  selectFds  [  selectIndex  ++  ]  ; 
switch  (  fd  )  { 
case   FD_READY  : 
waitDataFds  [  i  ]  |=  FD_READY_BIT  ; 
++  numReady  ; 
break  ; 
case   FD_INVALID  : 
waitDataFds  [  i  ]  |=  FD_INVALID_BIT  ; 
++  numReady  ; 
break  ; 
default  : 
waitDataFds  [  i  ]  &=  FD_MASK  ; 
} 
} 
return   numReady  ; 
} 

VM_ThreadIOQueue  (  int   id  )  { 
this  .  id  =  id  ; 
} 








public   boolean   pollForEvents  (  )  { 
numKilledInJava  =  0  ; 
VM_Thread   thread  =  head  ; 
int   readCount  =  0  ,  writeCount  =  0  ,  exceptCount  =  0  ; 
while  (  thread  !=  null  )  { 
if  (  isKilled  (  thread  )  )  { 
thread  .  throwInterruptWhenScheduled  =  true  ; 
++  numKilledInJava  ; 
} 
if  (  numKilledInJava  ==  0  )  { 
thread  .  waitData  .  accept  (  myDowncaster  )  ; 
VM_ThreadIOWaitData   waitData  =  myDowncaster  .  waitData  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  waitData  ==  thread  .  waitData  )  ; 
if  (  waitData  .  readFds  !=  null  )  { 
waitData  .  readOffset  =  readCount  ; 
readCount  +=  addFileDescriptors  (  allFds  ,  READ_OFFSET  +  readCount  ,  waitData  .  readFds  )  ; 
} 
if  (  waitData  .  writeFds  !=  null  )  { 
waitData  .  writeOffset  =  writeCount  ; 
writeCount  +=  addFileDescriptors  (  allFds  ,  WRITE_OFFSET  +  writeCount  ,  waitData  .  writeFds  )  ; 
} 
if  (  waitData  .  exceptFds  !=  null  )  { 
waitData  .  exceptOffset  =  exceptCount  ; 
exceptCount  +=  addFileDescriptors  (  allFds  ,  EXCEPT_OFFSET  +  exceptCount  ,  waitData  .  exceptFds  )  ; 
} 
} 
thread  =  thread  .  next  ; 
} 
if  (  numKilledInJava  >  0  )  return   true  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  isInSelect  =  true  ; 
VM_BootRecord   bootRecord  =  VM_BootRecord  .  the_boot_record  ; 
selectInProgressMutex  .  lock  (  )  ; 
int   ret  =  VM_SysCall  .  call_I_A_I_I_I  (  bootRecord  .  sysNetSelectIP  ,  VM_Magic  .  objectAsAddress  (  allFds  )  ,  readCount  ,  writeCount  ,  exceptCount  )  ; 
selectInProgressMutex  .  unlock  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  isInSelect  =  false  ; 
if  (  ret  ==  -  1  )  { 
return   false  ; 
} 
return   true  ; 
} 







public   boolean   isReady  (  VM_Thread   thread  )  { 
thread  .  waitData  .  accept  (  myDowncaster  )  ; 
VM_ThreadIOWaitData   waitData  =  myDowncaster  .  waitData  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  waitData  ==  thread  .  waitData  )  ; 
if  (  isKilled  (  thread  )  )  { 
waitData  .  waitFlags  =  (  WAIT_FINISHED  |  WAIT_INTERRUPTED  )  ; 
return   true  ; 
} 
int   numReady  =  0  ; 
numReady  +=  updateStatus  (  waitData  .  readFds  ,  waitData  .  readOffset  ,  allFds  ,  READ_OFFSET  )  ; 
numReady  +=  updateStatus  (  waitData  .  writeFds  ,  waitData  .  writeOffset  ,  allFds  ,  WRITE_OFFSET  )  ; 
numReady  +=  updateStatus  (  waitData  .  exceptFds  ,  waitData  .  exceptOffset  ,  allFds  ,  EXCEPT_OFFSET  )  ; 
boolean   ready  =  (  numReady  >  0  )  ; 
if  (  ready  )  { 
waitData  .  waitFlags  =  WAIT_FINISHED  ; 
} 
return   ready  ; 
} 

private   void   dumpFds  (  int  [  ]  fds  )  { 
if  (  fds  ==  null  )  return  ; 
for  (  int   i  =  0  ;  i  <  fds  .  length  ;  ++  i  )  { 
VM  .  sysWrite  (  fds  [  i  ]  &  FD_MASK  )  ; 
if  (  (  fds  [  i  ]  &  FD_READY_BIT  )  !=  0  )  VM  .  sysWrite  (  '+'  )  ; 
if  (  (  fds  [  i  ]  &  FD_INVALID_BIT  )  !=  0  )  VM  .  sysWrite  (  'X'  )  ; 
if  (  i  !=  fds  .  length  -  1  )  VM  .  sysWrite  (  ','  )  ; 
} 
} 





void   dumpWaitDescription  (  VM_Thread   thread  )  throws   VM_PragmaInterruptible  { 
WaitDataDowncaster   downcaster  =  new   WaitDataDowncaster  (  )  ; 
thread  .  waitData  .  accept  (  downcaster  )  ; 
VM_ThreadIOWaitData   waitData  =  downcaster  .  waitData  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  waitData  ==  thread  .  waitData  )  ; 
VM  .  sysWrite  (  "(R"  )  ; 
dumpFds  (  waitData  .  readFds  )  ; 
VM  .  sysWrite  (  ";W"  )  ; 
dumpFds  (  waitData  .  writeFds  )  ; 
VM  .  sysWrite  (  ";E"  )  ; 
dumpFds  (  waitData  .  exceptFds  )  ; 
VM  .  sysWrite  (  ')'  )  ; 
} 

private   void   appendFds  (  StringBuffer   buffer  ,  int  [  ]  fds  )  throws   VM_PragmaInterruptible  { 
if  (  fds  ==  null  )  return  ; 
for  (  int   i  =  0  ;  i  <  fds  .  length  ;  ++  i  )  { 
buffer  .  append  (  fds  [  i  ]  &  FD_MASK  )  ; 
if  (  (  fds  [  i  ]  &  FD_READY_BIT  )  !=  0  )  buffer  .  append  (  '+'  )  ; 
if  (  (  fds  [  i  ]  &  FD_INVALID_BIT  )  !=  0  )  buffer  .  append  (  'X'  )  ; 
if  (  i  !=  fds  .  length  -  1  )  buffer  .  append  (  ','  )  ; 
} 
} 





String   getWaitDescription  (  VM_Thread   thread  )  throws   VM_PragmaInterruptible  { 
WaitDataDowncaster   downcaster  =  new   WaitDataDowncaster  (  )  ; 
thread  .  waitData  .  accept  (  downcaster  )  ; 
VM_ThreadIOWaitData   waitData  =  downcaster  .  waitData  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  waitData  ==  thread  .  waitData  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
buffer  .  append  (  "(R"  )  ; 
appendFds  (  buffer  ,  waitData  .  readFds  )  ; 
buffer  .  append  (  ";W"  )  ; 
appendFds  (  buffer  ,  waitData  .  writeFds  )  ; 
buffer  .  append  (  ";E"  )  ; 
appendFds  (  buffer  ,  waitData  .  exceptFds  )  ; 
buffer  .  append  (  ')'  )  ; 
return   buffer  .  toString  (  )  ; 
} 
} 

