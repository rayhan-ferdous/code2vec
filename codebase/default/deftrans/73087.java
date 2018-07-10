import   java  .  io  .  *  ; 

abstract   class   OsProcess   implements   jdpConstants  ,  VM_BaselineConstants  { 








public   abstract   boolean   isBreakpointTrap  (  int   status  )  ; 









public   abstract   boolean   isIgnoredTrap  (  int   status  )  ; 

public   abstract   boolean   isIgnoredOtherBreakpointTrap  (  int   status  )  ; 

public   abstract   boolean   isLibraryLoadedTrap  (  int   status  )  ; 








public   abstract   boolean   isKilled  (  int   status  )  ; 








public   abstract   boolean   isExit  (  int   status  )  ; 









public   abstract   String   statusMessage  (  int   status  )  ; 








public   abstract   void   mkill  (  )  ; 








public   abstract   void   mdetach  (  )  ; 








public   abstract   int   mwait  (  )  ; 








public   abstract   void   mcontinue  (  int   signal  )  ; 








public   abstract   void   mcontinueThread  (  int   signal  )  ; 









public   abstract   int   createDebugProcess  (  String   ProgramName  ,  String   args  [  ]  )  ; 









public   abstract   int   attachDebugProcess  (  int   processID  )  ; 







public   abstract   int  [  ]  getSystemThreadId  (  )  ; 







public   abstract   String   listSystemThreads  (  )  ; 







public   abstract   int  [  ]  getVMThreadIDInSystemThread  (  )  ; 




memory   mem  ; 




register   reg  ; 




BootMap   bmap  ; 




breakpointList   bpset  ; 




breakpointList   threadstep  ; 




breakpointList   threadstepLine  ; 








boolean   ignoreOtherBreakpointTrap  =  false  ; 




boolean   verbose  ; 




int   lastSignal  [  ]  ; 





















public   OsProcess  (  String   ProgramName  ,  String   args  [  ]  ,  String   mainClass  ,  String   classesNeededFilename  ,  String   classpath  )  { 
createDebugProcess  (  ProgramName  ,  args  )  ; 
lastSignal  =  new   int  [  1  ]  ; 
lastSignal  [  0  ]  =  0  ; 
if  (  args  .  length  >  1  )  { 
bmap  =  loadMap  (  mainClass  ,  classesNeededFilename  ,  classpath  )  ; 
} 
verbose  =  false  ; 
} 







private   BootMap   loadMap  (  String   mainClass  ,  String   classesNeededFilename  ,  String   classpath  )  { 
try  { 
return   new   BootMapExternal  (  this  ,  mainClass  ,  classesNeededFilename  ,  classpath  )  ; 
}  catch  (  BootMapCorruptException   bme  )  { 
System  .  out  .  println  (  bme  )  ; 
bme  .  printStackTrace  (  )  ; 
System  .  out  .  println  (  "ERROR: corrupted method map"  )  ; 
return   new   BootMapExternal  (  this  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  out  .  println  (  "ERROR: something wrong with method map :) "  )  ; 
return   new   BootMapExternal  (  this  )  ; 
} 
} 


















public   OsProcess  (  int   processID  ,  String   mainClass  ,  String   classesNeededFilename  ,  String   classpath  )  throws   OsProcessException  { 
int   pid  =  attachDebugProcess  (  processID  )  ; 
if  (  pid  ==  -  1  )  throw   new   OsProcessException  (  "Fail to attach to process"  )  ; 
lastSignal  =  new   int  [  1  ]  ; 
lastSignal  [  0  ]  =  0  ; 
bmap  =  loadMap  (  mainClass  ,  classesNeededFilename  ,  classpath  )  ; 
verbose  =  false  ; 
} 













public   boolean   pstep  (  int   thread  ,  int   printMode  ,  boolean   skip_prolog  )  { 
int   addr  =  reg  .  hardwareIP  (  )  ; 
boolean   stillrunning  ; 
boolean   over_branch  =  false  ; 
breakpoint   bpSaved  =  bpset  .  lookup  (  addr  )  ; 
threadstep  .  setStepBreakpoint  (  thread  ,  over_branch  ,  skip_prolog  )  ; 
if  (  bpSaved  !=  null  )  bpset  .  clearBreakpoint  (  bpSaved  )  ; 
stillrunning  =  continueCheckingForSignal  (  thread  ,  printMode  ,  false  )  ; 
threadstep  .  clearStepBreakpoint  (  thread  )  ; 
if  (  stillrunning  &&  bpSaved  !=  null  )  bpset  .  setBreakpoint  (  bpSaved  )  ; 
return   stillrunning  ; 
} 














public   boolean   pstepLine  (  int   thread  ,  int   printMode  )  { 
boolean   stillrunning  =  true  ; 
boolean   lookingForNextLine  =  true  ; 
breakpoint   bpSaved  =  bpset  .  lookup  (  reg  .  hardwareIP  (  )  )  ; 
int   orig_thread  =  reg  .  registerToTPIndex  (  reg  .  hardwareTP  (  )  )  ; 
int   curr_thread  =  -  1  ; 
boolean   success  =  threadstepLine  .  setStepLineBreakpoint  (  thread  )  ; 
if  (  !  success  )  { 
return   stillrunning  ; 
} 
if  (  bpSaved  !=  null  )  bpset  .  clearBreakpoint  (  bpSaved  )  ; 
while  (  lookingForNextLine  )  { 
stillrunning  =  continueCheckingForSignal  (  thread  ,  printMode  ,  false  )  ; 
if  (  stillrunning  )  { 
int   curr_addr  =  reg  .  hardwareIP  (  )  ; 
curr_thread  =  reg  .  registerToTPIndex  (  reg  .  hardwareTP  (  )  )  ; 
if  (  threadstep  .  isAtStepLineBreakpoint  (  thread  ,  curr_addr  )  &&  (  curr_thread  !=  orig_thread  )  )  { 
threadstep  .  setStepBreakpoint  (  thread  ,  false  ,  false  )  ; 
threadstepLine  .  temporaryClearStepBreakpoint  (  thread  )  ; 
stillrunning  =  continueCheckingForSignal  (  thread  ,  printMode  ,  false  )  ; 
threadstep  .  clearStepBreakpoint  (  thread  )  ; 
threadstepLine  .  restoreStepBreakpoint  (  thread  )  ; 
}  else  { 
lookingForNextLine  =  false  ; 
} 
}  else  { 
return   stillrunning  ; 
} 
} 
if  (  stillrunning  )  { 
threadstepLine  .  clearStepBreakpoint  (  thread  )  ; 
if  (  bpSaved  !=  null  )  bpset  .  setBreakpoint  (  bpSaved  )  ; 
} 
return   stillrunning  ; 
} 










public   boolean   pstepOverBranch  (  int   thread  )  { 
int   addr  =  reg  .  hardwareIP  (  )  ; 
boolean   over_branch  =  true  ; 
boolean   stillrunning  ; 
boolean   skip_prolog  =  false  ; 
breakpoint   bpSaved  =  bpset  .  lookup  (  addr  )  ; 
threadstep  .  setStepBreakpoint  (  thread  ,  over_branch  ,  skip_prolog  )  ; 
if  (  bpSaved  !=  null  )  bpset  .  clearBreakpoint  (  bpSaved  )  ; 
stillrunning  =  continueCheckingForSignal  (  thread  ,  PRINTASSEMBLY  ,  false  )  ; 
threadstep  .  clearStepBreakpoint  (  thread  )  ; 
if  (  stillrunning  &&  bpSaved  !=  null  )  bpset  .  setBreakpoint  (  bpSaved  )  ; 
return   stillrunning  ; 
} 










private   boolean   continueCheckingForSignal  (  int   thread  ,  int   printMode  ,  boolean   allThreads  )  { 
if  (  allThreads  )  { 
if  (  lastSignal  [  thread  ]  ==  0  )  mcontinue  (  0  )  ;  else  { 
int   sig  =  lastSignal  [  thread  ]  ; 
lastSignal  [  thread  ]  =  0  ; 
mcontinue  (  sig  )  ; 
} 
}  else  { 
if  (  lastSignal  [  thread  ]  ==  0  )  mcontinueThread  (  0  )  ;  else  { 
int   sig  =  lastSignal  [  thread  ]  ; 
lastSignal  [  thread  ]  =  0  ; 
mcontinueThread  (  sig  )  ; 
} 
} 
try  { 
pwait  (  allThreads  )  ; 
printCurrentStatus  (  printMode  )  ; 
}  catch  (  OsProcessException   e  )  { 
lastSignal  [  thread  ]  =  e  .  status  ; 
if  (  isExit  (  e  .  status  )  )  { 
System  .  out  .  println  (  "Program has exited."  )  ; 
return   false  ; 
}  else  { 
System  .  out  .  println  (  "Unexpected signal: "  +  statusMessage  (  e  .  status  )  +  ", "  +  (  e  .  status  )  )  ; 
System  .  out  .  println  (  "...type cont/step to continue with this signal"  )  ; 
if  (  printMode  !=  PRINTNONE  )  { 
printCurrentStatus  (  PRINTASSEMBLY  )  ; 
} 
} 
} 
return   true  ; 
} 










private   void   printCurrentStatus  (  int   printMode  )  { 
switch  (  printMode  )  { 
case   PRINTNONE  : 
break  ; 
case   PRINTASSEMBLY  : 
if  (  verbose  )  { 
try  { 
mem  .  printJVMstack  (  reg  .  read  (  "FP"  )  ,  4  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  .  getMessage  (  )  )  ; 
} 
} 
System  .  out  .  println  (  mem  .  printCurrentInstr  (  )  )  ; 
break  ; 
case   PRINTSOURCE  : 
bmap  .  printCurrentSourceLine  (  )  ; 
break  ; 
} 
} 











public   boolean   pstepLineOverMethod  (  int   thread  )  { 
boolean   stillrunning  =  true  ; 
boolean   skip_prolog  ; 
int   addr  ,  orig_frame  ,  curr_frame  ; 
String   orig_line  ; 
String   curr_line  ; 
orig_frame  =  mem  .  findFrameCount  (  )  ; 
stillrunning  =  pstepLine  (  thread  ,  PRINTNONE  )  ; 
if  (  !  stillrunning  )  return   stillrunning  ; 
curr_frame  =  mem  .  findFrameCount  (  )  ; 
if  (  orig_frame  <  curr_frame  )  { 
stillrunning  =  pcontinueToReturn  (  thread  ,  PRINTNONE  )  ; 
}  else  { 
stillrunning  =  true  ; 
} 
if  (  stillrunning  )  printCurrentStatus  (  PRINTSOURCE  )  ; 
return   stillrunning  ; 
} 














public   void   pwait  (  boolean   allThread  )  throws   OsProcessException  { 
int   addr  ,  inst  ; 
boolean   skipBP  =  false  ; 
int   status  =  mwait  (  )  ; 
while  (  isIgnoredTrap  (  status  )  ||  (  skipBP  =  isLibraryLoadedTrap  (  status  )  )  ||  isIgnoredOtherBreakpointTrap  (  status  )  )  { 
if  (  skipBP  )  status  =  0  ; 
if  (  allThread  )  mcontinue  (  status  )  ;  else   mcontinueThread  (  status  )  ; 
status  =  mwait  (  )  ; 
} 
if  (  isBreakpointTrap  (  status  )  )  { 
bpset  .  relocateBreakpoint  (  )  ; 
addr  =  reg  .  hardwareIP  (  )  ; 
if  (  bpset  .  doesBreakpointExist  (  addr  )  ||  threadstep  .  doesBreakpointExist  (  addr  )  ||  threadstepLine  .  doesBreakpointExist  (  addr  )  )  { 
return  ; 
}  else  { 
throw   new   OsProcessException  (  status  )  ; 
} 
}  else  { 
if  (  !  ignoreOtherBreakpointTrap  )  throw   new   OsProcessException  (  status  )  ; 
} 
} 





















public   boolean   pcontinue  (  int   thread  ,  int   printMode  ,  boolean   allThreads  )  { 
int   addr  =  reg  .  hardwareIP  (  )  ; 
boolean   over_branch  =  false  ; 
boolean   stillrunning  ; 
boolean   skip_prolog  =  false  ; 
breakpoint   bpSaved  =  bpset  .  lookup  (  addr  )  ; 
if  (  bpSaved  !=  null  )  { 
threadstep  .  setStepBreakpoint  (  thread  ,  over_branch  ,  skip_prolog  )  ; 
bpset  .  clearBreakpoint  (  bpSaved  )  ; 
stillrunning  =  continueCheckingForSignal  (  thread  ,  PRINTNONE  ,  false  )  ; 
threadstep  .  clearStepBreakpoint  (  thread  )  ; 
bpset  .  setBreakpoint  (  bpSaved  )  ; 
} 
stillrunning  =  continueCheckingForSignal  (  thread  ,  printMode  ,  allThreads  )  ; 
return   stillrunning  ; 
} 











public   boolean   pcontinueToReturn  (  int   thread  ,  int   printMode  )  { 
int   addr  =  reg  .  hardwareIP  (  )  ; 
boolean   stillrunning  ; 
breakpoint   bpSaved  =  bpset  .  lookup  (  addr  )  ; 
threadstep  .  setLinkBreakpoint  (  thread  )  ; 
bpset  .  clearBreakpoint  (  bpSaved  )  ; 
stillrunning  =  continueCheckingForSignal  (  thread  ,  printMode  ,  false  )  ; 
threadstep  .  clearStepBreakpoint  (  thread  )  ; 
if  (  stillrunning  &&  bpSaved  !=  null  )  bpset  .  setBreakpoint  (  bpSaved  )  ; 
return   stillrunning  ; 
} 








public   void   pkill  (  )  { 
mkill  (  )  ; 
try  { 
pwait  (  true  )  ; 
}  catch  (  OsProcessException   e  )  { 
if  (  isExit  (  e  .  status  )  )  System  .  out  .  println  (  "Program has exited."  )  ;  else   if  (  isKilled  (  e  .  status  )  )  System  .  out  .  println  (  "Program terminated by user."  )  ; 
} 
} 








public   BootMap   bootmap  (  )  { 
return   bmap  ; 
} 

private   String   getJ2NThreadCounts  (  int   threadPointer  )  throws   Exception  { 
String   result  =  new   String  (  )  ; 
String   temp  ; 
String   blanks  =  "            "  ; 
if  (  threadPointer  ==  0  )  return   null  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NYieldCount"  )  ; 
temp  =  Integer  .  toString  (  mem  .  read  (  threadPointer  +  field  .  getOffset  (  )  )  )  ; 
result  +=  blanks  .  substring  (  1  ,  blanks  .  length  (  )  -  temp  .  length  (  )  )  +  temp  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NLockFailureCount"  )  ; 
temp  =  Integer  .  toString  (  mem  .  read  (  threadPointer  +  field  .  getOffset  (  )  )  )  ; 
result  +=  blanks  .  substring  (  1  ,  blanks  .  length  (  )  -  temp  .  length  (  )  )  +  temp  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NTotalYieldDuration"  )  ; 
temp  =  Integer  .  toString  (  mem  .  read  (  threadPointer  +  field  .  getOffset  (  )  )  )  ; 
result  +=  blanks  .  substring  (  1  ,  blanks  .  length  (  )  -  temp  .  length  (  )  )  +  temp  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NTotalLockDuration"  )  ; 
temp  =  Integer  .  toString  (  mem  .  read  (  threadPointer  +  field  .  getOffset  (  )  )  )  ; 
result  +=  blanks  .  substring  (  1  ,  blanks  .  length  (  )  -  temp  .  length  (  )  )  +  temp  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
return   result  ; 
} 

private   void   zeroJ2NThreadCounts  (  int   threadPointer  )  throws   Exception  { 
if  (  threadPointer  ==  0  )  return  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NYieldCount"  )  ; 
mem  .  write  (  threadPointer  +  field  .  getOffset  (  )  ,  0  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NLockFailureCount"  )  ; 
mem  .  write  (  threadPointer  +  field  .  getOffset  (  )  ,  0  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NTotalYieldDuration"  )  ; 
mem  .  write  (  threadPointer  +  field  .  getOffset  (  )  ,  0  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "J2NTotalLockDuration"  )  ; 
mem  .  write  (  threadPointer  +  field  .  getOffset  (  )  ,  0  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
} 








public   int  [  ]  getAllThreads  (  )  throws   Exception  { 
int   count  =  0  ; 
int   threadPointer  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  "threads"  )  ; 
int   address  =  mem  .  readTOC  (  field  .  getOffset  (  )  )  ; 
int   arraySize  =  mem  .  read  (  address  +  VM_ObjectModel  .  getArrayLengthOffset  (  )  )  ; 
VM_Field   numThreadsField  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  "numActiveThreads"  )  ; 
int   numThreadsAddress  =  mem  .  addressTOC  (  numThreadsField  .  getOffset  (  )  )  ; 
int   numThreads  =  mem  .  readsafe  (  numThreadsAddress  )  +  1  ; 
int   threadArray  [  ]  =  new   int  [  numThreads  ]  ; 
int   j  =  0  ; 
for  (  int   i  =  1  ;  i  <  arraySize  ;  i  ++  )  { 
threadPointer  =  mem  .  read  (  address  +  i  *  4  )  ; 
if  (  threadPointer  !=  0  )  { 
threadArray  [  j  ++  ]  =  threadPointer  ; 
if  (  j  ==  numThreads  )  break  ; 
} 
} 
return   threadArray  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Scheduler.threads, has VM_Scheduler.java been changed?"  )  ; 
} 
} 

public   int   getVMThreadForIndex  (  int   index  )  throws   Exception  { 
if  (  index  ==  0  )  return   0  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  "threads"  )  ; 
int   address  =  mem  .  readTOC  (  field  .  getOffset  (  )  )  ; 
int   arraySize  =  mem  .  read  (  address  +  VM_ObjectModel  .  getArrayLengthOffset  (  )  )  ; 
return   mem  .  read  (  address  +  index  *  4  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Scheduler.threads, has VM_Scheduler.java been changed?"  )  ; 
} 
} 

public   int   getIndexForVMThread  (  int   threadPointer  )  throws   Exception  { 
if  (  threadPointer  ==  0  )  return   0  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "threadSlot"  )  ; 
int   address  =  mem  .  read  (  threadPointer  +  field  .  getOffset  (  )  )  ; 
return   address  ; 
}  catch  (  BmapNotFoundException   e  )  { 
throw   new   Exception  (  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  )  ; 
} 
} 

public   boolean   isValidVMThread  (  int   threadPointer  )  throws   Exception  { 
int   vmthreads  [  ]  =  getAllThreads  (  )  ; 
for  (  int   i  =  0  ;  i  <  vmthreads  .  length  ;  i  ++  )  { 
if  (  threadPointer  ==  vmthreads  [  i  ]  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 







public   String   listAllThreads  (  boolean   byClassName  )  { 
try  { 
String   result  =  ""  ; 
int   allThreads  [  ]  =  getAllThreads  (  )  ; 
int   runThreads  [  ]  =  getVMThreadIDInSystemThread  (  )  ; 
for  (  int   j  =  0  ;  j  <  runThreads  .  length  ;  j  ++  )  { 
runThreads  [  j  ]  =  getVMThreadForIndex  (  runThreads  [  j  ]  )  ; 
} 
result  =  "All threads: "  +  allThreads  .  length  +  "\n"  ; 
if  (  byClassName  )  { 
result  +=  "  ID  VM_Thread   Thread Class\n"  ; 
result  +=  "  -- -----------  ------------\n"  ; 
}  else  { 
result  +=  "  ID  VM_Thread   top stack frame\n"  ; 
result  +=  "  -- -----------  -----------------\n"  ; 
} 
int   activeThread  =  reg  .  registerToTPIndex  (  reg  .  hardwareTP  (  )  )  ; 
activeThread  =  getVMThreadForIndex  (  activeThread  )  ; 
for  (  int   i  =  0  ;  i  <  allThreads  .  length  ;  i  ++  )  { 
if  (  allThreads  [  i  ]  ==  activeThread  )  result  +=  " -"  ;  else   result  +=  "  "  ; 
boolean   running  =  false  ; 
for  (  int   j  =  0  ;  j  <  runThreads  .  length  ;  j  ++  )  { 
if  (  allThreads  [  i  ]  ==  runThreads  [  j  ]  )  running  =  true  ; 
} 
if  (  running  )  result  +=  ">"  ;  else   result  +=  " "  ; 
if  (  byClassName  )  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "threadSlot"  )  ; 
int   fieldOffset  =  field  .  getOffset  (  )  ; 
int   id  =  mem  .  read  (  allThreads  [  i  ]  +  fieldOffset  )  ; 
result  +=  id  +  " @"  +  VM  .  intAsHexString  (  allThreads  [  i  ]  )  +  "  "  ; 
result  +=  bmap  .  addressToClassString  (  allThreads  [  i  ]  )  .  substring  (  1  )  +  "\n"  ; 
}  else  { 
result  +=  threadToString  (  allThreads  [  i  ]  )  +  "\n"  ; 
} 
} 
return   result  ; 
}  catch  (  Exception   e  )  { 
return   e  .  getMessage  (  )  ; 
} 
} 

public   String   listThreadsCounts  (  )  { 
try  { 
String   result  =  ""  ; 
int   allThreads  [  ]  =  getAllThreads  (  )  ; 
result  =  "All threads: "  +  allThreads  .  length  +  "\n"  ; 
result  +=  "  ID VM_Thread  yield  failure   total yield   total lck \n"  ; 
result  +=  "  ------------  -----  -------   -----------   --------- \n"  ; 
for  (  int   i  =  0  ;  i  <  allThreads  .  length  ;  i  ++  )  { 
result  +=  "  "  +  Integer  .  toHexString  (  allThreads  [  i  ]  )  +  "  "  ; 
result  +=  getJ2NThreadCounts  (  allThreads  [  i  ]  )  +  "\n"  ; 
} 
return   result  ; 
}  catch  (  Exception   e  )  { 
return   e  .  getMessage  (  )  ; 
} 
} 

public   void   zeroThreadsCounts  (  )  { 
try  { 
String   result  =  ""  ; 
int   allThreads  [  ]  =  getAllThreads  (  )  ; 
for  (  int   i  =  0  ;  i  <  allThreads  .  length  ;  i  ++  )  { 
zeroJ2NThreadCounts  (  allThreads  [  i  ]  )  ; 
} 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "zeroThreadsCounts: "  +  e  .  getMessage  (  )  )  ; 
} 
} 








public   String   listReadyThreads  (  )  { 
String   result  =  ""  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  "processors"  )  ; 
int   address  =  mem  .  readTOC  (  field  .  getOffset  (  )  )  ; 
int   processorCount  =  mem  .  read  (  address  +  VM_ObjectModel  .  getArrayLengthOffset  (  )  )  ; 
field  =  bmap  .  findVMField  (  "VM_Processor"  ,  "readyQueue"  )  ; 
int   fieldOffset  =  field  .  getOffset  (  )  ; 
for  (  int   proc  =  1  ;  proc  <  processorCount  ;  proc  ++  )  { 
result  +=  "Virtual Processor "  +  proc  +  "\n"  ; 
int   procAddress  =  mem  .  read  (  address  +  proc  *  4  )  ; 
int   queuePointer  =  mem  .  read  (  procAddress  +  fieldOffset  )  ; 
result  +=  threadQueueToString  (  queuePointer  )  ; 
} 
return   result  ; 
}  catch  (  BmapNotFoundException   e  )  { 
return  "ERROR: cannot find VM_Scheduler.processors, has VM_Scheduler been changed?"  ; 
} 
} 







public   String   listWakeupThreads  (  )  { 
String   queueName  =  "wakeupQueue"  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  queueName  )  ; 
int   queuePointer  =  mem  .  readTOC  (  field  .  getOffset  (  )  )  ; 
return   wakeupQueueToString  (  queuePointer  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
return  "ERROR: cannot find VM_Scheduler."  +  queueName  +  ", has VM_Scheduler been changed?"  ; 
} 
} 








private   String   threadQueueToString  (  int   queuePointer  )  { 
String   result  =  ""  ; 
int   count  =  0  ; 
int   fieldOffset  ; 
int   thisThreadPointer  ,  headThreadPointer  ,  tailThreadPointer  ; 
VM_Field   field  ; 
try  { 
field  =  bmap  .  findVMField  (  "VM_ThreadQueue"  ,  "head"  )  ; 
headThreadPointer  =  mem  .  read  (  queuePointer  +  field  .  getOffset  (  )  )  ; 
field  =  bmap  .  findVMField  (  "VM_ThreadQueue"  ,  "tail"  )  ; 
tailThreadPointer  =  mem  .  read  (  queuePointer  +  field  .  getOffset  (  )  )  ; 
thisThreadPointer  =  headThreadPointer  ; 
if  (  thisThreadPointer  !=  0  )  { 
result  +=  "   "  +  threadToString  (  thisThreadPointer  )  +  "\n"  ; 
count  ++  ; 
} 
while  (  thisThreadPointer  !=  tailThreadPointer  )  { 
field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "next"  )  ; 
thisThreadPointer  =  mem  .  read  (  thisThreadPointer  +  field  .  getOffset  (  )  )  ; 
if  (  thisThreadPointer  !=  0  )  { 
result  +=  "   "  +  threadToString  (  thisThreadPointer  )  +  "\n"  ; 
count  ++  ; 
}  else  { 
thisThreadPointer  =  tailThreadPointer  ; 
} 
} 
}  catch  (  BmapNotFoundException   e  )  { 
return  "ERROR: cannot find VM_ThreadQueue.head or tail, has VM_ThreadQueue been changed?"  ; 
} 
String   heading  =  ""  ; 
heading  +=  "  ID  VM_Thread   top stack frame\n"  ; 
heading  +=  "  -- -----------  -----------------\n"  ; 
return  "Threads in queue:  "  +  count  +  "\n"  +  heading  +  result  ; 
} 








private   String   wakeupQueueToString  (  int   queuePointer  )  { 
String   result  =  ""  ; 
int   count  =  0  ; 
int   fieldOffset  ; 
int   thisProxyPointer  ,  thisThreadPointer  ; 
VM_Field   field  ; 
try  { 
field  =  bmap  .  findVMField  (  "VM_ProxyWakeupQueue"  ,  "head"  )  ; 
thisProxyPointer  =  mem  .  read  (  queuePointer  +  field  .  getOffset  (  )  )  ; 
while  (  thisProxyPointer  !=  0  )  { 
field  =  bmap  .  findVMField  (  "VM_Proxy"  ,  "thread"  )  ; 
thisThreadPointer  =  mem  .  read  (  thisProxyPointer  +  field  .  getOffset  (  )  )  ; 
if  (  thisThreadPointer  !=  0  )  { 
result  +=  "   "  +  threadToString  (  thisThreadPointer  )  +  "\n"  ; 
count  ++  ; 
} 
field  =  bmap  .  findVMField  (  "VM_Proxy"  ,  "wakeupNext"  )  ; 
thisProxyPointer  =  mem  .  read  (  thisProxyPointer  +  field  .  getOffset  (  )  )  ; 
} 
}  catch  (  BmapNotFoundException   e  )  { 
return  "ERROR: cannot find VM_ThreadQueue.head or tail, has VM_ThreadQueue been changed?"  ; 
} 
String   heading  =  ""  ; 
heading  +=  "  ID  VM_Thread   top stack frame\n"  ; 
heading  +=  "  -- -----------  -----------------\n"  ; 
return  "Threads in queue:  "  +  count  +  "\n"  +  heading  +  result  ; 
} 







public   String   listGCThreads  (  )  { 
String   queueName  =  "collectorQueue"  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Scheduler"  ,  queueName  )  ; 
int   queuePointer  =  mem  .  readTOC  (  field  .  getOffset  (  )  )  ; 
return  "GC threads: \n"  +  threadQueueToString  (  queuePointer  )  ; 
}  catch  (  BmapNotFoundException   e  )  { 
return  "ERROR: cannot find VM_Scheduler."  +  queueName  +  ", has VM_Scheduler been changed?"  ; 
} 
} 







public   String   listRunThreads  (  )  { 
String   result  =  ""  ; 
int   runThreads  [  ]  =  getVMThreadIDInSystemThread  (  )  ; 
try  { 
for  (  int   j  =  0  ;  j  <  runThreads  .  length  ;  j  ++  )  { 
runThreads  [  j  ]  =  getVMThreadForIndex  (  runThreads  [  j  ]  )  ; 
} 
result  +=  "Running in system threads: "  +  runThreads  .  length  +  "\n"  ; 
result  +=  "  ID  VM_Thread   top stack frame\n"  ; 
result  +=  "  -- -----------  -----------------\n"  ; 
for  (  int   i  =  0  ;  i  <  runThreads  .  length  ;  i  ++  )  { 
if  (  runThreads  [  i  ]  !=  0  )  { 
result  +=  "   "  +  threadToString  (  runThreads  [  i  ]  )  +  "\n"  ; 
}  else  { 
System  .  out  .  println  (  "CAUTION:  unexpected null thread in system thread\n"  )  ; 
} 
} 
return   result  ; 
}  catch  (  Exception   e  )  { 
return   e  .  getMessage  (  )  ; 
} 
} 

private   String   threadToString  (  int   threadPointer  )  { 
String   result  =  ""  ; 
try  { 
VM_Field   field  =  bmap  .  findVMField  (  "VM_Thread"  ,  "threadSlot"  )  ; 
int   fieldOffset  =  field  .  getOffset  (  )  ; 
int   id  =  mem  .  read  (  threadPointer  +  fieldOffset  )  ; 
result  +=  id  +  " @"  +  VM  .  intAsHexString  (  threadPointer  )  ; 
int   ip  =  reg  .  getVMThreadIP  (  id  )  ; 
int  [  ]  savedRegs  =  reg  .  getVMThreadGPR  (  id  )  ; 
int   fp  =  savedRegs  [  reg  .  regGetNum  (  "FP"  )  ]  ; 
int   compiledMethodID  =  bmap  .  getCompiledMethodID  (  fp  ,  ip  )  ; 
if  (  compiledMethodID  ==  NATIVE_METHOD_ID  )  { 
result  +=  "  (native procedure)"  ; 
}  else  { 
VM_Method   mth  =  bmap  .  findVMMethod  (  compiledMethodID  ,  true  )  ; 
if  (  mth  !=  null  )  { 
String   method_name  =  mth  .  getName  (  )  .  toString  (  )  ; 
String   method_sig  =  mth  .  getDescriptor  (  )  .  toString  (  )  ; 
result  +=  "  "  +  method_name  +  "  "  +  method_sig  ; 
}  else  { 
result  +=  "  (method ID not set up yet)"  ; 
} 
} 
return   result  ; 
}  catch  (  BmapNotFoundException   e  )  { 
return  "cannot find VM_Thread.threadSlot, has VM_Thread.java been changed?"  ; 
}  catch  (  Exception   e  )  { 
return   e  .  getMessage  (  )  ; 
} 
} 

public   void   enableIgnoreOtherBreakpointTrap  (  )  { 
ignoreOtherBreakpointTrap  =  true  ; 
} 

public   void   disableIgnoreOtherBreakpointTrap  (  )  { 
ignoreOtherBreakpointTrap  =  false  ; 
} 
} 

