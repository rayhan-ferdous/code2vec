package   com  .  ibm  .  JikesRVM  ; 

import   com  .  ibm  .  JikesRVM  .  memoryManagers  .  vmInterface  .  MM_Interface  ; 
import   com  .  ibm  .  JikesRVM  .  classloader  .  *  ; 
import   com  .  ibm  .  JikesRVM  .  adaptive  .  VM_RuntimeMeasurements  ; 
import   com  .  ibm  .  JikesRVM  .  adaptive  .  VM_Controller  ; 
import   com  .  ibm  .  JikesRVM  .  adaptive  .  VM_ControllerMemory  ; 
import   com  .  ibm  .  JikesRVM  .  adaptive  .  OSR_OnStackReplacementTrigger  ; 
import   com  .  ibm  .  JikesRVM  .  adaptive  .  OSR_OnStackReplacementEvent  ; 
import   com  .  ibm  .  JikesRVM  .  OSR  .  OSR_PostThreadSwitch  ; 
import   com  .  ibm  .  JikesRVM  .  OSR  .  OSR_ObjectHolder  ; 






public   class   VM_Thread   implements   VM_Constants  ,  VM_Uninterruptible  { 




private   static   final   boolean   trace  =  false  ; 




private   static   final   boolean   debugDeadVP  =  false  ; 




public   static   final   int   PROLOGUE  =  0  ; 

public   static   final   int   BACKEDGE  =  1  ; 

public   static   final   int   EPILOGUE  =  2  ; 

public   static   final   int   OSRBASE  =  98  ; 

public   static   final   int   OSROPT  =  99  ; 

public   HPM_counters   hpm_counters  =  null  ; 

public   long   startOfWallTime  =  -  1  ; 

private   static   int   GLOBAL_TID_INITIAL_VALUE  =  -  1  ; 

private   int   global_tid  =  GLOBAL_TID_INITIAL_VALUE  ; 

public   final   int   getGlobalIndex  (  )  { 
return   global_tid  ; 
} 

private   static   Integer   global_hpm_tid  =  new   Integer  (  1  )  ; 

private   final   void   assignGlobalTID  (  )  throws   VM_PragmaLogicallyUninterruptible  { 
synchronized  (  global_hpm_tid  )  { 
global_tid  =  global_hpm_tid  .  intValue  (  )  ; 
global_hpm_tid  =  new   Integer  (  global_hpm_tid  .  intValue  (  )  +  1  )  ; 
} 
if  (  VM_HardwarePerformanceMonitors  .  verbose  >=  2  )  { 
VM  .  sysWrite  (  " VM_Thread.assignGlobalTID ("  ,  threadSlot  ,  ") assigned "  )  ; 
VM  .  sysWriteln  (  global_tid  )  ; 
} 
} 




public   VM_Thread  (  )  { 
this  (  MM_Interface  .  newStack  (  STACK_SIZE_NORMAL  >  >  LOG_BYTES_IN_ADDRESS  )  )  ; 
if  (  hpm_counters  ==  null  )  hpm_counters  =  new   HPM_counters  (  )  ; 
if  (  global_tid  ==  GLOBAL_TID_INITIAL_VALUE  )  { 
assignGlobalTID  (  )  ; 
if  (  VM_HardwarePerformanceMonitors  .  hpm_trace  )  { 
VM_HardwarePerformanceMonitors  .  writeThreadToHeaderFile  (  global_tid  ,  threadSlot  ,  getClass  (  )  .  toString  (  )  )  ; 
} 
} 
} 




public   static   VM_Thread   getCurrentThread  (  )  { 
return   VM_Processor  .  getCurrentProcessor  (  )  .  activeThread  ; 
} 




public   final   VM_JNIEnvironment   getJNIEnv  (  )  { 
return   jniEnv  ; 
} 

public   final   void   initializeJNIEnv  (  )  throws   VM_PragmaInterruptible  { 
jniEnv  =  new   VM_JNIEnvironment  (  threadSlot  )  ; 
} 







public   final   boolean   hasNativeStackFrame  (  )  { 
return   jniEnv  !=  null  &&  jniEnv  .  hasNativeStackFrame  (  )  ; 
} 

public   String   toString  (  )  throws   VM_PragmaInterruptible  { 
return  "VM_Thread"  ; 
} 





public   void   run  (  )  throws   VM_PragmaInterruptible  { 
} 





public   void   exit  (  )  throws   VM_PragmaInterruptible  { 
} 





public   final   void   suspend  (  )  { 
suspendLock  .  lock  (  )  ; 
suspendPending  =  true  ; 
suspendLock  .  unlock  (  )  ; 
if  (  this  ==  getCurrentThread  (  )  )  yield  (  )  ; 
} 





public   void   resume  (  )  throws   VM_PragmaInterruptible  { 
suspendLock  .  lock  (  )  ; 
suspendPending  =  false  ; 
if  (  suspended  )  { 
suspended  =  false  ; 
suspendLock  .  unlock  (  )  ; 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "resume() scheduleThread "  ,  getIndex  (  )  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  this  )  ; 
}  else  { 
suspendLock  .  unlock  (  )  ; 
} 
} 





public   final   void   osrSuspend  (  )  { 
suspendLock  .  lock  (  )  ; 
suspendPending  =  true  ; 
suspendLock  .  unlock  (  )  ; 
} 




public   static   void   sleepImpl  (  VM_Thread   thread  )  { 
VM_Scheduler  .  wakeupMutex  .  lock  (  )  ; 
yield  (  VM_Scheduler  .  wakeupQueue  ,  VM_Scheduler  .  wakeupMutex  )  ; 
} 






public   static   void   ioWaitImpl  (  VM_ThreadIOWaitData   waitData  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  waitData  =  waitData  ; 
yield  (  VM_Processor  .  getCurrentProcessor  (  )  .  ioQueue  )  ; 
} 







public   static   void   processWaitImpl  (  VM_ThreadProcessWaitData   waitData  ,  VM_Process   process  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  waitData  =  waitData  ; 
VM_Processor   creatingProcessor  =  process  .  getCreatingProcessor  (  )  ; 
VM_ProcessorLock   queueLock  =  creatingProcessor  .  processWaitQueueLock  ; 
queueLock  .  lock  (  )  ; 
yield  (  creatingProcessor  .  processWaitQueue  ,  queueLock  )  ; 
} 




public   final   void   kill  (  Throwable   externalInterrupt  ,  boolean   throwImmediately  )  { 
this  .  externalInterrupt  =  externalInterrupt  ; 
if  (  throwImmediately  )  { 
this  .  throwInterruptWhenScheduled  =  true  ; 
} 
VM_Proxy   p  =  proxy  ; 
if  (  p  !=  null  )  { 
this  .  throwInterruptWhenScheduled  =  true  ; 
VM_Thread   t  =  p  .  unproxy  (  )  ; 
if  (  t  !=  null  )  t  .  schedule  (  )  ; 
} 
} 





public   static   void   threadSwitchFromPrologue  (  )  { 
threadSwitch  (  PROLOGUE  )  ; 
} 





public   static   void   threadSwitchFromBackedge  (  )  { 
threadSwitch  (  BACKEDGE  )  ; 
} 





public   static   void   threadSwitchFromEpilogue  (  )  { 
threadSwitch  (  EPILOGUE  )  ; 
} 

public   static   void   threadSwitchFromOsrBase  (  )  { 
threadSwitch  (  OSRBASE  )  ; 
} 

public   static   void   threadSwitchFromOsrOpt  (  )  { 
threadSwitch  (  OSROPT  )  ; 
} 





public   static   void   threadSwitch  (  int   whereFrom  )  throws   VM_PragmaNoInline  { 
VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchRequested  =  0  ; 
if  (  VM_Processor  .  getCurrentProcessor  (  )  .  needsSync  )  { 
VM_Processor  .  getCurrentProcessor  (  )  .  needsSync  =  false  ; 
VM_Magic  .  isync  (  )  ; 
VM_Synchronization  .  fetchAndDecrement  (  VM_Magic  .  getJTOC  (  )  ,  VM_Entrypoints  .  toSyncProcessorsField  .  getOffset  (  )  ,  1  )  ; 
} 
if  (  !  VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchingEnabled  (  )  )  { 
VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchPending  =  true  ; 
return  ; 
} 
if  (  VM_Scheduler  .  debugRequested  &&  VM_Scheduler  .  allProcessorsInitialized  )  { 
VM_Scheduler  .  debuggerMutex  .  lock  (  )  ; 
if  (  VM_Scheduler  .  debuggerQueue  .  isEmpty  (  )  )  { 
VM_Scheduler  .  debuggerMutex  .  unlock  (  )  ; 
}  else  { 
VM_Thread   t  =  VM_Scheduler  .  debuggerQueue  .  dequeue  (  )  ; 
VM_Scheduler  .  debuggerMutex  .  unlock  (  )  ; 
t  .  schedule  (  )  ; 
} 
} 
if  (  !  VM_Scheduler  .  attachThreadRequested  .  isZero  (  )  )  { 
VM_Scheduler  .  attachThreadMutex  .  lock  (  )  ; 
if  (  VM_Scheduler  .  attachThreadQueue  .  isEmpty  (  )  )  { 
VM_Scheduler  .  attachThreadMutex  .  unlock  (  )  ; 
}  else  { 
VM_Thread   t  =  VM_Scheduler  .  attachThreadQueue  .  dequeue  (  )  ; 
VM_Scheduler  .  attachThreadMutex  .  unlock  (  )  ; 
t  .  schedule  (  )  ; 
} 
} 
if  (  VM_Scheduler  .  wakeupQueue  .  isReady  (  )  )  { 
VM_Scheduler  .  wakeupMutex  .  lock  (  )  ; 
VM_Thread   t  =  VM_Scheduler  .  wakeupQueue  .  dequeue  (  )  ; 
VM_Scheduler  .  wakeupMutex  .  unlock  (  )  ; 
if  (  t  !=  null  )  { 
t  .  schedule  (  )  ; 
} 
} 
if  (  VM  .  BuildForDeterministicThreadSwitching  )  VM_Processor  .  getCurrentProcessor  (  )  .  deterministicThreadSwitchCount  =  VM  .  deterministicThreadSwitchInterval  ; 
VM_Controller  .  controllerClock  ++  ; 
if  (  !  VM_Thread  .  getCurrentThread  (  )  .  isIdleThread  )  { 
VM_Address   fp  =  VM_Magic  .  getCallerFramePointer  (  VM_Magic  .  getFramePointer  (  )  )  ; 
fp  =  VM_Magic  .  getCallerFramePointer  (  fp  )  ; 
int   ypTakenInCMID  =  VM_Magic  .  getCompiledMethodID  (  fp  )  ; 
fp  =  VM_Magic  .  getCallerFramePointer  (  fp  )  ; 
int   ypTakenInCallerCMID  =  VM_Magic  .  getCompiledMethodID  (  fp  )  ; 
boolean   ypTakenInCallerCMIDValid  =  true  ; 
VM_CompiledMethod   ypTakenInCM  =  VM_CompiledMethods  .  getCompiledMethod  (  ypTakenInCMID  )  ; 
if  (  ypTakenInCallerCMID  ==  STACKFRAME_SENTINEL_FP  .  toInt  (  )  ||  ypTakenInCallerCMID  ==  INVISIBLE_METHOD_ID  ||  ypTakenInCM  .  getMethod  (  )  .  getDeclaringClass  (  )  .  isBridgeFromNative  (  )  )  { 
ypTakenInCallerCMIDValid  =  false  ; 
} 
if  (  (  VM_Controller  .  osrOrganizer  !=  null  )  &&  (  VM_Controller  .  osrOrganizer  .  osr_flag  )  )  { 
VM_Controller  .  osrOrganizer  .  activate  (  )  ; 
} 
if  (  !  VM_Thread  .  getCurrentThread  (  )  .  isSystemThread  )  { 
boolean   baseToOptOSR  =  false  ; 
if  (  whereFrom  ==  VM_Thread  .  BACKEDGE  )  { 
if  (  ypTakenInCM  .  isOutdated  (  )  )  { 
baseToOptOSR  =  true  ; 
} 
} 
if  (  baseToOptOSR  ||  (  whereFrom  ==  VM_Thread  .  OSROPT  )  )  { 
VM_Address   tsFP  =  VM_Magic  .  getFramePointer  (  )  ; 
VM_Address   tsFromFP  =  VM_Magic  .  getCallerFramePointer  (  tsFP  )  ; 
VM_Address   realFP  =  VM_Magic  .  getCallerFramePointer  (  tsFromFP  )  ; 
VM_Address   stackbeg  =  VM_Magic  .  objectAsAddress  (  VM_Thread  .  getCurrentThread  (  )  .  stack  )  ; 
VM_Offset   tsFromFPoff  =  tsFromFP  .  diff  (  stackbeg  )  ; 
VM_Offset   realFPoff  =  realFP  .  diff  (  stackbeg  )  ; 
OSR_OnStackReplacementTrigger  .  trigger  (  ypTakenInCMID  ,  tsFromFPoff  ,  realFPoff  ,  whereFrom  )  ; 
} 
} 
if  (  VM_RuntimeMeasurements  .  hasMethodListener  (  )  )  { 
if  (  !  ypTakenInCallerCMIDValid  )  ypTakenInCallerCMID  =  -  1  ; 
VM_RuntimeMeasurements  .  activateMethodListeners  (  ypTakenInCMID  ,  ypTakenInCallerCMID  ,  whereFrom  )  ; 
} 
if  (  ypTakenInCallerCMIDValid  &&  VM_RuntimeMeasurements  .  hasContextListener  (  )  )  { 
fp  =  VM_Magic  .  getCallerFramePointer  (  VM_Magic  .  getFramePointer  (  )  )  ; 
fp  =  VM_Magic  .  getCallerFramePointer  (  fp  )  ; 
VM_RuntimeMeasurements  .  activateContextListeners  (  fp  ,  whereFrom  )  ; 
} 
if  (  VM_RuntimeMeasurements  .  hasNullListener  (  )  )  { 
VM_RuntimeMeasurements  .  activateNullListeners  (  whereFrom  )  ; 
} 
} 
timerTickYield  (  )  ; 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
if  (  myThread  .  isWaitingForOsr  )  { 
OSR_PostThreadSwitch  .  postProcess  (  myThread  )  ; 
} 
} 






public   static   void   timerTickYield  (  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "timerTickYield() scheduleThread "  ,  myThread  .  getIndex  (  )  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  myThread  )  ; 
morph  (  true  )  ; 
} 




public   static   void   yield  (  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  readyQueue  .  enqueue  (  myThread  )  ; 
morph  (  false  )  ; 
} 






public   static   void   yield  (  VM_AbstractThreadQueue   q  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
q  .  enqueue  (  myThread  )  ; 
morph  (  false  )  ; 
} 






public   static   void   yield  (  VM_AbstractThreadQueue   q  ,  VM_ProcessorLock   l  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
q  .  enqueue  (  myThread  )  ; 
l  .  unlock  (  )  ; 
morph  (  false  )  ; 
} 












static   void   yield  (  VM_ProxyWaitingQueue   q1  ,  VM_ProcessorLock   l1  ,  VM_ProxyWakeupQueue   q2  ,  VM_ProcessorLock   l2  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
q1  .  enqueue  (  myThread  .  proxy  )  ; 
q2  .  enqueue  (  myThread  .  proxy  )  ; 
l1  .  unlock  (  )  ; 
l2  .  unlock  (  )  ; 
morph  (  false  )  ; 
} 

static   void   yield  (  VM_Processor   p  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
if  (  VM  .  VerifyAssertions  )  { 
VM  .  _assert  (  p  .  processorMode  ==  VM_Processor  .  NATIVE  )  ; 
VM  .  _assert  (  VM_Processor  .  vpStatus  [  p  .  vpStatusIndex  ]  ==  VM_Processor  .  BLOCKED_IN_NATIVE  )  ; 
VM  .  _assert  (  myThread  .  isNativeIdleThread  ==  true  )  ; 
} 
myThread  .  beingDispatched  =  true  ; 
p  .  transferMutex  .  lock  (  )  ; 
p  .  transferQueue  .  enqueue  (  myThread  )  ; 
VM_Processor  .  vpStatus  [  p  .  vpStatusIndex  ]  =  VM_Processor  .  IN_NATIVE  ; 
p  .  transferMutex  .  unlock  (  )  ; 
morph  (  false  )  ; 
} 

static   void   morph  (  )  { 
morph  (  false  )  ; 
} 





static   void   morph  (  boolean   timerTick  )  { 
VM_Magic  .  sync  (  )  ; 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "morph "  )  ; 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
if  (  VM  .  VerifyAssertions  )  { 
if  (  !  VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchingEnabled  (  )  )  { 
VM  .  sysWrite  (  "no threadswitching on proc "  ,  VM_Processor  .  getCurrentProcessor  (  )  .  id  )  ; 
VM  .  sysWriteln  (  " with addr "  ,  VM_Magic  .  objectAsAddress  (  VM_Processor  .  getCurrentProcessor  (  )  )  )  ; 
} 
VM  .  _assert  (  VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchingEnabled  (  )  ,  "thread switching not enabled"  )  ; 
VM  .  _assert  (  myThread  .  beingDispatched  ==  true  ,  "morph: not beingDispatched"  )  ; 
} 
VM_Processor  .  getCurrentProcessor  (  )  .  dispatch  (  timerTick  )  ; 
if  (  myThread  .  externalInterrupt  !=  null  &&  myThread  .  throwInterruptWhenScheduled  )  { 
postExternalInterrupt  (  myThread  )  ; 
} 
} 

private   static   void   postExternalInterrupt  (  VM_Thread   myThread  )  throws   VM_PragmaLogicallyUninterruptible  { 
Throwable   t  =  myThread  .  externalInterrupt  ; 
myThread  .  externalInterrupt  =  null  ; 
myThread  .  throwInterruptWhenScheduled  =  false  ; 
t  .  fillInStackTrace  (  )  ; 
VM_Runtime  .  athrow  (  t  )  ; 
} 









static   void   becomeNativeThread  (  )  { 
int   lockoutId  ; 
if  (  trace  )  { 
VM  .  sysWrite  (  "VM_Thread.becomeNativeThread entry -process = "  )  ; 
VM  .  sysWrite  (  VM_Magic  .  objectAsAddress  (  VM_Processor  .  getCurrentProcessor  (  )  )  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
VM  .  sysWrite  (  "Thread id "  )  ; 
VM  .  sysWrite  (  VM_Magic  .  getThreadId  (  )  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
} 
VM_Processor   p  =  VM_Thread  .  getCurrentThread  (  )  .  nativeAffinity  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  p  !=  null  ,  "null nativeAffinity, should have been recorded by C caller\n"  )  ; 
p  .  transferMutex  .  lock  (  )  ; 
VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysPthreadSignalIP  ,  p  .  pthread_id  )  ; 
yield  (  p  .  transferQueue  ,  p  .  transferMutex  )  ; 
if  (  trace  )  { 
VM  .  sysWrite  (  "VM_Thread.becomeNativeThread exit -process = "  )  ; 
VM  .  sysWrite  (  VM_Magic  .  objectAsAddress  (  VM_Processor  .  getCurrentProcessor  (  )  )  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
} 
} 




static   void   becomeRVMThread  (  )  { 
VM_Processor   currentProcessor  =  VM_ProcessorLocalState  .  getCurrentProcessor  (  )  ; 
currentProcessor  .  activeThread  .  returnAffinity  .  transferMutex  .  lock  (  )  ; 
yield  (  VM_Thread  .  getCurrentThread  (  )  .  returnAffinity  .  transferQueue  ,  VM_Thread  .  getCurrentThread  (  )  .  returnAffinity  .  transferMutex  )  ; 
if  (  trace  )  { 
VM  .  sysWrite  (  "VM_Thread.becomeRVMThread- exit process = "  )  ; 
VM  .  sysWrite  (  VM_Magic  .  objectAsAddress  (  VM_Processor  .  getCurrentProcessor  (  )  )  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
} 
} 










public   final   void   schedule  (  )  { 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "schedule"  ,  getIndex  (  )  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  this  )  ; 
} 








public   final   void   scheduleHighPriority  (  )  { 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "scheduleHighPriority"  ,  getIndex  (  )  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  scheduleThread  (  this  )  ; 
} 




private   static   void   startoff  (  )  throws   VM_PragmaInterruptible  { 
VM_Thread   currentThread  =  getCurrentThread  (  )  ; 
currentThread  .  run  (  )  ; 
terminate  (  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 





final   void   registerThread  (  )  { 
isAlive  =  true  ; 
VM_Scheduler  .  threadCreationMutex  .  lock  (  )  ; 
VM_Scheduler  .  numActiveThreads  +=  1  ; 
if  (  isDaemon  )  VM_Scheduler  .  numDaemons  +=  1  ; 
VM_Scheduler  .  threadCreationMutex  .  unlock  (  )  ; 
} 





public   synchronized   void   start  (  )  throws   VM_PragmaInterruptible  { 
registerThread  (  )  ; 
schedule  (  )  ; 
} 






final   void   start  (  VM_ThreadQueue   q  )  { 
registerThread  (  )  ; 
q  .  enqueue  (  this  )  ; 
} 






static   void   terminate  (  )  throws   VM_PragmaInterruptible  { 
boolean   terminateSystem  =  false  ; 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "terminate"  )  ; 
VM_RuntimeMeasurements  .  monitorThreadExit  (  )  ; 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
myThread  .  exit  (  )  ; 
synchronized  (  myThread  )  { 
VM_Scheduler  .  threadCreationMutex  .  lock  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  disableThreadSwitching  (  )  ; 
myThread  .  isAlive  =  false  ; 
myThread  .  notifyAll  (  )  ; 
} 
myThread  .  hardwareExceptionRegisters  .  inuse  =  false  ; 
VM_Scheduler  .  numActiveThreads  -=  1  ; 
if  (  myThread  .  isDaemon  )  VM_Scheduler  .  numDaemons  -=  1  ; 
if  (  VM_Scheduler  .  numDaemons  ==  VM_Scheduler  .  numActiveThreads  )  { 
terminateSystem  =  true  ; 
} 
VM_Processor  .  getCurrentProcessor  (  )  .  enableThreadSwitching  (  )  ; 
VM_Scheduler  .  threadCreationMutex  .  unlock  (  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM_Processor  .  getCurrentProcessor  (  )  .  threadSwitchingEnabled  (  )  )  ; 
if  (  terminateSystem  )  { 
VM  .  sysExit  (  0  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 
VM_Processor   p  =  myThread  .  nativeAffinity  ; 
if  (  p  !=  null  )  { 
VM_Scheduler  .  deadVPQueue  .  enqueue  (  p  )  ; 
myThread  .  nativeAffinity  =  null  ; 
myThread  .  processorAffinity  =  null  ; 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "terminate "  ,  myThread  .  getIndex  (  )  )  ; 
VM_Processor  .  vpStatus  [  p  .  vpStatusIndex  ]  =  VM_Processor  .  RVM_VP_GOING_TO_WAIT  ; 
} 
VM_Scheduler  .  threadCreationMutex  .  lock  (  )  ; 
myThread  .  releaseThreadSlot  (  )  ; 
myThread  .  beingDispatched  =  true  ; 
VM_Scheduler  .  threadCreationMutex  .  unlock  (  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  dispatch  (  false  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 




public   final   int   getIndex  (  )  { 
return   threadSlot  ; 
} 




public   final   int   getLockingId  (  )  { 
return   threadSlot  <<  VM_ThinLockConstants  .  TL_THREAD_ID_SHIFT  ; 
} 

private   static   final   boolean   traceAdjustments  =  false  ; 








public   static   void   resizeCurrentStack  (  int   newSize  ,  VM_Registers   exceptionRegisters  )  throws   VM_PragmaInterruptible  { 
if  (  traceAdjustments  )  VM  .  sysWrite  (  "VM_Thread: resizeCurrentStack\n"  )  ; 
if  (  MM_Interface  .  gcInProgress  (  )  )  VM  .  sysFail  (  "system error: resizing stack while GC is in progress"  )  ; 
int  [  ]  newStack  =  MM_Interface  .  newStack  (  newSize  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  disableThreadSwitching  (  )  ; 
transferExecutionToNewStack  (  newStack  ,  exceptionRegisters  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  enableThreadSwitching  (  )  ; 
if  (  traceAdjustments  )  { 
VM  .  sysWrite  (  "VM_Thread: resized stack "  )  ; 
VM  .  sysWrite  (  getCurrentThread  (  )  .  getIndex  (  )  )  ; 
VM  .  sysWrite  (  " to "  )  ; 
VM  .  sysWrite  (  (  (  getCurrentThread  (  )  .  stack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  /  1024  )  )  ; 
VM  .  sysWrite  (  "k\n"  )  ; 
} 
} 

private   static   void   transferExecutionToNewStack  (  int  [  ]  newStack  ,  VM_Registers   exceptionRegisters  )  throws   VM_PragmaNoInline  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
int  [  ]  myStack  =  myThread  .  stack  ; 
VM_Address   myTop  =  VM_Magic  .  objectAsAddress  (  myStack  )  .  add  (  myStack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  ; 
VM_Address   newTop  =  VM_Magic  .  objectAsAddress  (  newStack  )  .  add  (  newStack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  ; 
VM_Address   myFP  =  VM_Magic  .  getFramePointer  (  )  ; 
VM_Offset   myDepth  =  myTop  .  diff  (  myFP  )  ; 
VM_Address   newFP  =  newTop  .  sub  (  myDepth  )  ; 
VM_Offset   delta  =  copyStack  (  newStack  )  ; 
if  (  exceptionRegisters  !=  null  )  adjustRegisters  (  exceptionRegisters  ,  delta  )  ; 
adjustStack  (  newStack  ,  newFP  ,  delta  )  ; 
myThread  .  stack  =  newStack  ; 
myThread  .  stackLimit  =  VM_Magic  .  objectAsAddress  (  newStack  )  .  add  (  STACK_SIZE_GUARD  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  activeThreadStackLimit  =  myThread  .  stackLimit  ; 
VM_Magic  .  returnToNewStack  (  VM_Magic  .  getCallerFramePointer  (  newFP  )  )  ; 
VM_Magic  .  returnToNewStack  (  newFP  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 






public   final   void   fixupMovedStack  (  VM_Offset   delta  )  { 
if  (  traceAdjustments  )  VM  .  sysWrite  (  "VM_Thread: fixupMovedStack\n"  )  ; 
if  (  !  contextRegisters  .  getInnermostFramePointer  (  )  .  isZero  (  )  )  adjustRegisters  (  contextRegisters  ,  delta  )  ; 
if  (  (  hardwareExceptionRegisters  .  inuse  )  &&  (  hardwareExceptionRegisters  .  getInnermostFramePointer  (  )  .  NE  (  VM_Address  .  zero  (  )  )  )  )  { 
adjustRegisters  (  hardwareExceptionRegisters  ,  delta  )  ; 
} 
if  (  !  contextRegisters  .  getInnermostFramePointer  (  )  .  isZero  (  )  )  adjustStack  (  stack  ,  contextRegisters  .  getInnermostFramePointer  (  )  ,  delta  )  ; 
stackLimit  =  stackLimit  .  add  (  delta  )  ; 
} 








private   static   void   adjustRegisters  (  VM_Registers   registers  ,  VM_Offset   delta  )  { 
if  (  traceAdjustments  )  VM  .  sysWrite  (  "VM_Thread: adjustRegisters\n"  )  ; 
VM_Address   newFP  =  registers  .  getInnermostFramePointer  (  )  .  add  (  delta  )  ; 
VM_Address   ip  =  registers  .  getInnermostInstructionAddress  (  )  ; 
registers  .  setInnermost  (  ip  ,  newFP  )  ; 
if  (  traceAdjustments  )  { 
VM  .  sysWrite  (  " fp="  )  ; 
VM  .  sysWrite  (  registers  .  getInnermostFramePointer  (  )  )  ; 
} 
int   compiledMethodId  =  VM_Magic  .  getCompiledMethodID  (  registers  .  getInnermostFramePointer  (  )  )  ; 
if  (  compiledMethodId  !=  INVISIBLE_METHOD_ID  )  { 
VM_Word   old  =  registers  .  gprs  .  get  (  ESP  )  ; 
registers  .  gprs  .  set  (  ESP  ,  old  .  add  (  delta  )  )  ; 
if  (  traceAdjustments  )  { 
VM  .  sysWrite  (  " esp ="  )  ; 
VM  .  sysWrite  (  registers  .  gprs  .  get  (  ESP  )  )  ; 
} 
if  (  traceAdjustments  )  { 
VM_CompiledMethod   compiledMethod  =  VM_CompiledMethods  .  getCompiledMethod  (  compiledMethodId  )  ; 
VM  .  sysWrite  (  " method="  )  ; 
VM  .  sysWrite  (  compiledMethod  .  getMethod  (  )  )  ; 
VM  .  sysWrite  (  "\n"  )  ; 
} 
} 
} 









private   static   void   adjustStack  (  int  [  ]  stack  ,  VM_Address   fp  ,  VM_Offset   delta  )  { 
if  (  traceAdjustments  )  VM  .  sysWrite  (  "VM_Thread: adjustStack\n"  )  ; 
while  (  VM_Magic  .  getCallerFramePointer  (  fp  )  .  NE  (  STACKFRAME_SENTINEL_FP  )  )  { 
VM_Magic  .  setCallerFramePointer  (  fp  ,  VM_Magic  .  getCallerFramePointer  (  fp  )  .  add  (  delta  )  )  ; 
if  (  traceAdjustments  )  { 
VM  .  sysWrite  (  " fp="  ,  fp  .  toWord  (  )  )  ; 
} 
fp  =  VM_Magic  .  getCallerFramePointer  (  fp  )  ; 
} 
} 



















private   static   VM_Offset   copyStack  (  int  [  ]  newStack  )  { 
VM_Thread   myThread  =  getCurrentThread  (  )  ; 
int  [  ]  myStack  =  myThread  .  stack  ; 
VM_Address   myTop  =  VM_Magic  .  objectAsAddress  (  myStack  )  .  add  (  myStack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  ; 
VM_Address   newTop  =  VM_Magic  .  objectAsAddress  (  newStack  )  .  add  (  newStack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  ; 
VM_Address   myFP  =  VM_Magic  .  getFramePointer  (  )  ; 
VM_Offset   myDepth  =  myTop  .  diff  (  myFP  )  ; 
VM_Address   newFP  =  newTop  .  sub  (  myDepth  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  newFP  .  GE  (  VM_Magic  .  objectAsAddress  (  newStack  )  .  add  (  STACK_SIZE_GUARD  )  )  )  ; 
VM_Memory  .  aligned32Copy  (  newFP  ,  myFP  ,  myDepth  .  toInt  (  )  )  ; 
return   newFP  .  diff  (  myFP  )  ; 
} 










protected   final   void   makeDaemon  (  boolean   on  )  { 
if  (  isDaemon  ==  on  )  return  ; 
isDaemon  =  on  ; 
if  (  !  isAlive  )  return  ; 
VM_Scheduler  .  threadCreationMutex  .  lock  (  )  ; 
VM_Scheduler  .  numDaemons  +=  on  ?  1  :  -  1  ; 
VM_Scheduler  .  threadCreationMutex  .  unlock  (  )  ; 
if  (  VM_Scheduler  .  numDaemons  ==  VM_Scheduler  .  numActiveThreads  )  { 
if  (  VM  .  TraceThreads  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "last non Daemon demonized"  )  ; 
VM  .  sysExit  (  0  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 
} 





public   VM_Thread  (  int  [  ]  stack  )  { 
this  .  stack  =  stack  ; 
chosenProcessorId  =  (  VM  .  runningVM  ?  VM_Processor  .  getCurrentProcessorId  (  )  :  0  )  ; 
suspendLock  =  new   VM_ProcessorLock  (  )  ; 
contextRegisters  =  new   VM_Registers  (  )  ; 
hardwareExceptionRegisters  =  new   VM_Registers  (  )  ; 
if  (  !  VM  .  runningVM  )  { 
VM_Scheduler  .  threads  [  threadSlot  =  VM_Scheduler  .  PRIMORDIAL_THREAD_INDEX  ]  =  this  ; 
VM_Scheduler  .  numActiveThreads  +=  1  ; 
return  ; 
} 
if  (  trace  )  VM_Scheduler  .  trace  (  "VM_Thread"  ,  "create"  )  ; 
stackLimit  =  VM_Magic  .  objectAsAddress  (  stack  )  .  add  (  STACK_SIZE_GUARD  )  ; 
INSTRUCTION  [  ]  instructions  =  VM_Entrypoints  .  threadStartoffMethod  .  getCurrentInstructions  (  )  ; 
VM  .  disableGC  (  )  ; 
VM_Address   ip  =  VM_Magic  .  objectAsAddress  (  instructions  )  ; 
VM_Address   sp  =  VM_Magic  .  objectAsAddress  (  stack  )  .  add  (  stack  .  length  <<  LOG_BYTES_IN_ADDRESS  )  ; 
VM_Address   fp  =  STACKFRAME_SENTINEL_FP  ; 
sp  =  sp  .  sub  (  STACKFRAME_HEADER_SIZE  )  ; 
fp  =  sp  .  sub  (  BYTES_IN_ADDRESS  +  STACKFRAME_BODY_OFFSET  )  ; 
VM_Magic  .  setCallerFramePointer  (  fp  ,  STACKFRAME_SENTINEL_FP  )  ; 
VM_Magic  .  setCompiledMethodID  (  fp  ,  INVISIBLE_METHOD_ID  )  ; 
sp  =  sp  .  sub  (  BYTES_IN_ADDRESS  )  ; 
contextRegisters  .  gprs  .  set  (  ESP  ,  sp  )  ; 
contextRegisters  .  gprs  .  set  (  VM_BaselineConstants  .  JTOC  ,  VM_Magic  .  objectAsAddress  (  VM_Magic  .  getJTOC  (  )  )  )  ; 
contextRegisters  .  fp  =  fp  ; 
contextRegisters  .  ip  =  ip  ; 
int   INITIAL_FRAME_SIZE  =  STACKFRAME_HEADER_SIZE  ; 
fp  =  VM_Memory  .  alignDown  (  sp  .  sub  (  INITIAL_FRAME_SIZE  )  ,  STACKFRAME_ALIGNMENT  )  ; 
VM_Magic  .  setMemoryAddress  (  fp  .  add  (  STACKFRAME_FRAME_POINTER_OFFSET  )  ,  STACKFRAME_SENTINEL_FP  )  ; 
VM_Magic  .  setMemoryAddress  (  fp  .  add  (  STACKFRAME_NEXT_INSTRUCTION_OFFSET  )  ,  ip  )  ; 
VM_Magic  .  setMemoryInt  (  fp  .  add  (  STACKFRAME_METHOD_ID_OFFSET  )  ,  INVISIBLE_METHOD_ID  )  ; 
contextRegisters  .  gprs  .  set  (  FRAME_POINTER  ,  fp  )  ; 
contextRegisters  .  ip  =  ip  ; 
VM_Scheduler  .  threadCreationMutex  .  lock  (  )  ; 
assignThreadSlot  (  )  ; 
if  (  hpm_counters  ==  null  )  hpm_counters  =  new   HPM_counters  (  )  ; 
assignGlobalTID  (  )  ; 
if  (  VM_HardwarePerformanceMonitors  .  hpm_trace  )  { 
VM_HardwarePerformanceMonitors  .  writeThreadToHeaderFile  (  global_tid  ,  threadSlot  ,  getClass  (  )  .  toString  (  )  )  ; 
} 
VM_Scheduler  .  threadCreationMutex  .  unlock  (  )  ; 
contextRegisters  .  gprs  .  set  (  THREAD_ID_REGISTER  ,  VM_Word  .  fromInt  (  getLockingId  (  )  )  )  ; 
VM  .  enableGC  (  )  ; 
if  (  VM  .  runningVM  )  jniEnv  =  new   VM_JNIEnvironment  (  threadSlot  )  ; 
onStackReplacementEvent  =  new   OSR_OnStackReplacementEvent  (  )  ; 
} 





private   void   assignThreadSlot  (  )  { 
for  (  int   cnt  =  VM_Scheduler  .  threads  .  length  ;  --  cnt  >=  1  ;  )  { 
int   index  =  VM_Scheduler  .  threadAllocationIndex  ; 
if  (  ++  VM_Scheduler  .  threadAllocationIndex  ==  VM_Scheduler  .  threads  .  length  )  VM_Scheduler  .  threadAllocationIndex  =  1  ; 
if  (  VM_Scheduler  .  threads  [  index  ]  ==  null  )  { 
threadSlot  =  index  ; 
VM_Magic  .  setObjectAtOffset  (  VM_Scheduler  .  threads  ,  threadSlot  <<  LOG_BYTES_IN_ADDRESS  ,  this  )  ; 
return  ; 
} 
} 
VM  .  sysFail  (  "too many threads"  )  ; 
} 









final   void   releaseThreadSlot  (  )  { 
VM_Magic  .  setObjectAtOffset  (  VM_Scheduler  .  threads  ,  threadSlot  <<  LOG_BYTES_IN_ADDRESS  ,  null  )  ; 
VM_Scheduler  .  threadAllocationIndex  =  threadSlot  ; 
if  (  VM  .  VerifyAssertions  )  threadSlot  =  -  1  ; 
} 




public   void   dump  (  )  { 
dump  (  0  )  ; 
} 

public   void   dump  (  int   verbosity  )  { 
VM_Scheduler  .  writeDecimal  (  getIndex  (  )  )  ; 
if  (  isDaemon  )  VM_Scheduler  .  writeString  (  "-daemon"  )  ; 
if  (  isNativeIdleThread  )  VM_Scheduler  .  writeString  (  "-nativeidle"  )  ; 
if  (  isIdleThread  )  VM_Scheduler  .  writeString  (  "-idle"  )  ; 
if  (  isGCThread  )  VM_Scheduler  .  writeString  (  "-collector"  )  ; 
if  (  isNativeDaemonThread  )  VM_Scheduler  .  writeString  (  "-nativeDaemon"  )  ; 
if  (  beingDispatched  )  VM_Scheduler  .  writeString  (  "-being_dispatched"  )  ; 
} 

public   static   void   dumpAll  (  int   verbosity  )  { 
for  (  int   i  =  0  ;  i  <  VM_Scheduler  .  threads  .  length  ;  i  ++  )  { 
VM_Thread   t  =  VM_Scheduler  .  threads  [  i  ]  ; 
if  (  t  ==  null  )  continue  ; 
VM  .  sysWrite  (  "Thread "  ,  i  )  ; 
VM  .  sysWrite  (  ":  "  ,  VM_Magic  .  objectAsAddress  (  t  )  )  ; 
VM  .  sysWrite  (  "   "  )  ; 
t  .  dump  (  verbosity  )  ; 
VM  .  sysWriteln  (  )  ; 
} 
} 




public   boolean   is_suspended  (  )  { 
return   isSuspended  ; 
} 

VM_ProcessorLock   suspendLock  ; 

boolean   suspendPending  ; 

boolean   suspended  ; 






private   int   threadSlot  ; 




VM_Proxy   proxy  ; 




protected   volatile   boolean   isSuspended  ; 






Throwable   externalInterrupt  ; 





boolean   throwInterruptWhenScheduled  ; 






public   boolean   disallowAllocationsByThisThread  ; 




public   int  [  ]  stack  ; 

public   VM_Address   stackLimit  ; 




public   VM_Registers   contextRegisters  ; 





public   VM_Registers   hardwareExceptionRegisters  ; 





Object   waitObject  ; 

int   waitCount  ; 




double   wakeupTime  ; 





VM_ThreadEventWaitData   waitData  ; 





protected   int   priority  ; 





public   VM_Processor   processorAffinity  ; 












public   static   void   setProcessorAffinity  (  Object   t  ,  int   pid  )  { 
if  (  VM_HardwarePerformanceMonitors  .  verbose  >=  3  )  { 
VM  .  sysWrite  (  "VM_Thread.setProcessorAffinity("  ,  pid  ,  ")"  )  ; 
} 
VM_Thread   thread  =  (  VM_Thread  )  t  ; 
if  (  pid  <=  VM_Scheduler  .  numProcessors  &&  pid  >  0  )  { 
thread  .  processorAffinity  =  VM_Scheduler  .  processors  [  pid  ]  ; 
} 
} 




public   VM_Processor   nativeAffinity  ; 




public   VM_Processor   returnAffinity  ; 





public   boolean   beingDispatched  ; 




public   VM_Thread   next  ; 







protected   boolean   isAlive  ; 




public   boolean   isGCThread  ; 




boolean   isIdleThread  ; 





boolean   isNativeIdleThread  ; 





boolean   isNativeDaemonThread  ; 





protected   boolean   isDaemon  ; 




public   int   chosenProcessorId  ; 

public   VM_JNIEnvironment   jniEnv  ; 

double   cpuStartTime  =  -  1  ; 

double   cpuTotalTime  ; 

public   int   netReads  ; 

public   int   netWrites  ; 

public   double   getCPUStartTime  (  )  { 
return   cpuStartTime  ; 
} 

public   double   getCPUTotalTime  (  )  { 
return   cpuTotalTime  ; 
} 

public   void   setCPUStartTime  (  double   time  )  { 
cpuStartTime  =  time  ; 
} 

public   void   setCPUTotalTime  (  double   time  )  { 
cpuTotalTime  =  time  ; 
} 

public   boolean   isIdleThread  (  )  { 
return   isIdleThread  ; 
} 

public   boolean   isGCThread  (  )  { 
return   isGCThread  ; 
} 

public   boolean   isDaemonThread  (  )  throws   VM_PragmaInterruptible  { 
return   isDaemon  ; 
} 

public   boolean   isAlive  (  )  throws   VM_PragmaInterruptible  { 
return   isAlive  ; 
} 

public   boolean   isSystemThread  (  )  { 
return   isSystemThread  ; 
} 

protected   boolean   isSystemThread  =  true  ; 

public   OSR_OnStackReplacementEvent   onStackReplacementEvent  ; 

public   boolean   isWaitingForOsr  =  false  ; 

public   INSTRUCTION  [  ]  bridgeInstructions  =  null  ; 

public   int   fooFPOffset  =  0  ; 

public   int   tsFPOffset  =  0  ; 

public   boolean   requesting_osr  =  false  ; 
} 

