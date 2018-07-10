package   com  .  ibm  .  JikesRVM  ; 

import   com  .  ibm  .  JikesRVM  .  classloader  .  *  ; 
import   com  .  ibm  .  JikesRVM  .  memoryManagers  .  vmInterface  .  MM_Interface  ; 









public   class   VM   extends   VM_Properties   implements   VM_Constants  ,  VM_Uninterruptible  { 






public   static   void   initForBootImageWriter  (  String   classPath  ,  String  [  ]  bootCompilerArgs  )  throws   VM_PragmaInterruptible  ,  ClassNotFoundException  { 
writingBootImage  =  true  ; 
init  (  classPath  ,  bootCompilerArgs  )  ; 
} 




public   static   void   initForTool  (  )  throws   VM_PragmaInterruptible  ,  ClassNotFoundException  { 
runningTool  =  true  ; 
init  (  System  .  getProperty  (  "java.class.path"  )  ,  null  )  ; 
} 





public   static   void   initForTool  (  String   classpath  )  throws   VM_PragmaInterruptible  ,  ClassNotFoundException  { 
runningTool  =  true  ; 
init  (  classpath  ,  null  )  ; 
} 

static   int   verbose  =  0  ; 










public   static   void   boot  (  )  throws   Exception  ,  VM_PragmaLogicallyUninterruptible  { 
VM  .  writingBootImage  =  false  ; 
VM  .  runningVM  =  true  ; 
VM  .  runningAsSubsystem  =  false  ; 
sysWriteLockOffset  =  VM_Entrypoints  .  sysWriteLockField  .  getOffset  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Booting"  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Setting up current VM_Processor"  )  ; 
VM_ProcessorLocalState  .  boot  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Doing thread initialization"  )  ; 
VM_Thread   currentThread  =  VM_Scheduler  .  threads  [  VM_Magic  .  getThreadId  (  )  >  >  >  VM_ThinLockConstants  .  TL_THREAD_ID_SHIFT  ]  ; 
currentThread  .  stackLimit  =  VM_Magic  .  objectAsAddress  (  currentThread  .  stack  )  .  add  (  STACK_SIZE_GUARD  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  activeThreadStackLimit  =  currentThread  .  stackLimit  ; 
if  (  !  BuildForSingleVirtualProcessor  )  VM_Processor  .  getCurrentProcessor  (  )  .  pthread_id  =  VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysPthreadSelfIP  )  ; 
VM  .  TraceClassLoading  =  (  VM_BootRecord  .  the_boot_record  .  traceClassLoading  ==  1  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Setting up write barrier"  )  ; 
MM_Interface  .  setupProcessor  (  VM_Processor  .  getCurrentProcessor  (  )  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Setting up memory manager: bootrecord = "  ,  VM_Magic  .  objectAsAddress  (  VM_BootRecord  .  the_boot_record  )  )  ; 
MM_Interface  .  boot  (  VM_BootRecord  .  the_boot_record  )  ; 
VM_Time  .  boot  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Setting up baseline compiler options"  )  ; 
VM_BaselineCompiler  .  initOptions  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Creating class objects for static synchronized methods"  )  ; 
createClassObjects  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Fetching command-line arguments"  )  ; 
VM_CommandLineArgs  .  fetchCommandLineArguments  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Initializing class loader"  )  ; 
String   vmClasses  =  VM_CommandLineArgs  .  getVMClasses  (  )  ; 
VM_ClassLoader  .  boot  (  vmClasses  )  ; 
VM_SystemClassLoader  .  boot  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Running various class initializers"  )  ; 
java  .  lang  .  ref  .  JikesRVMSupport  .  setReferenceLock  (  new   VM_Synchronizer  (  )  )  ; 
runClassInitializer  (  "java.lang.Runtime"  )  ; 
runClassInitializer  (  "java.lang.System"  )  ; 
runClassInitializer  (  "java.io.File"  )  ; 
runClassInitializer  (  "java.lang.Boolean"  )  ; 
runClassInitializer  (  "java.lang.Byte"  )  ; 
runClassInitializer  (  "java.lang.Short"  )  ; 
runClassInitializer  (  "java.lang.Number"  )  ; 
runClassInitializer  (  "java.lang.Integer"  )  ; 
runClassInitializer  (  "java.lang.Long"  )  ; 
runClassInitializer  (  "java.lang.Float"  )  ; 
runClassInitializer  (  "java.lang.Character"  )  ; 
runClassInitializer  (  "gnu.java.io.EncodingManager"  )  ; 
runClassInitializer  (  "java.lang.Thread"  )  ; 
runClassInitializer  (  "java.lang.ThreadGroup"  )  ; 
runClassInitializer  (  "java.io.PrintWriter"  )  ; 
runClassInitializer  (  "gnu.java.lang.SystemClassLoader"  )  ; 
runClassInitializer  (  "java.lang.String"  )  ; 
runClassInitializer  (  "gnu.java.security.provider.DefaultPolicy"  )  ; 
runClassInitializer  (  "java.security.Policy"  )  ; 
runClassInitializer  (  "java.util.WeakHashMap"  )  ; 
runClassInitializer  (  "java.lang.ClassLoader"  )  ; 
runClassInitializer  (  "java.lang.Math"  )  ; 
runClassInitializer  (  "java.util.TimeZone"  )  ; 
runClassInitializer  (  "java.util.Locale"  )  ; 
runClassInitializer  (  "java.util.Calendar"  )  ; 
runClassInitializer  (  "java.util.GregorianCalendar"  )  ; 
runClassInitializer  (  "java.util.ResourceBundle"  )  ; 
runClassInitializer  (  "java.util.zip.ZipEntry"  )  ; 
runClassInitializer  (  "java.util.zip.Inflater"  )  ; 
runClassInitializer  (  "java.util.zip.DeflaterHuffman"  )  ; 
runClassInitializer  (  "java.util.zip.InflaterDynHeader"  )  ; 
runClassInitializer  (  "java.util.zip.InflaterHuffmanTree"  )  ; 
runClassInitializer  (  "gnu.java.locale.Calendar"  )  ; 
runClassInitializer  (  "java.util.Date"  )  ; 
runClassInitializer  (  "java.util.jar.Attributes$Name"  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Early stage processing of VM directives"  )  ; 
VM_CommandLineArgs  .  earlyProcessCommandLineArguments  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Collector processing rest of boot options"  )  ; 
MM_Interface  .  postBoot  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Booting VM_Lock"  )  ; 
VM_Lock  .  boot  (  )  ; 
if  (  BuildForHPM  )  { 
if  (  VM_HardwarePerformanceMonitors  .  verbose  >=  1  )  VM  .  sysWriteln  (  "VM.boot() call VM_HardwarePerformanceMonitors.boot()"  )  ; 
VM_HardwarePerformanceMonitors  .  boot  (  )  ; 
if  (  VM_HardwarePerformanceMonitors  .  enabled  (  )  )  { 
if  (  !  VM_HardwarePerformanceMonitors  .  hpm_thread_group  )  { 
if  (  VM_HardwarePerformanceMonitors  .  verbose  >=  1  )  VM  .  sysWriteln  (  "VM.boot()"  ,  "call to sysHPMsetSettings() and sysHPMstartMyThread()\n"  )  ; 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysHPMsetProgramMyThreadIP  )  ; 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysHPMstartMyThreadIP  )  ; 
} 
} 
} 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Booting scheduler"  )  ; 
VM_Scheduler  .  boot  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Initializing JNI for boot thread"  )  ; 
VM_Thread  .  getCurrentThread  (  )  .  initializeJNIEnv  (  )  ; 
runClassInitializer  (  "com.ibm.JikesRVM.Java2HPM"  )  ; 
VM_HardwarePerformanceMonitors  .  setUpHPMinfo  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Running late class initializers"  )  ; 
runClassInitializer  (  "java.io.FileDescriptor"  )  ; 
runClassInitializer  (  "java.lang.Double"  )  ; 
runClassInitializer  (  "java.util.PropertyPermission"  )  ; 
runClassInitializer  (  "com.ibm.JikesRVM.VM_Process"  )  ; 
VM_FileSystem  .  initializeStandardStreams  (  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Late stage processing of VM directives"  )  ; 
String  [  ]  applicationArguments  =  VM_CommandLineArgs  .  lateProcessCommandLineArguments  (  )  ; 
if  (  applicationArguments  .  length  ==  0  )  { 
VM  .  sysWrite  (  "vm: please specify a class to execute\n"  )  ; 
VM  .  sysExit  (  1  )  ; 
} 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Compiler processing rest of boot options"  )  ; 
VM_BaselineCompiler  .  postBootOptions  (  )  ; 
VM_EdgeCounts  .  boot  (  )  ; 
if  (  VM  .  verbose  >=  1  )  VM  .  sysWriteln  (  "Initializing runtime compiler"  )  ; 
VM_RuntimeCompiler  .  boot  (  )  ; 
if  (  VM  .  verboseClassLoading  )  VM  .  sysWrite  (  "[VM booted]\n"  )  ; 
if  (  VM  .  verbose  >=  1  )  VM  .  sysWriteln  (  "Constructing mainThread"  )  ; 
Thread   xx  =  new   MainThread  (  applicationArguments  )  ; 
VM_Address   yy  =  VM_Magic  .  objectAsAddress  (  xx  )  ; 
VM_Thread   mainThread  =  (  VM_Thread  )  VM_Magic  .  addressAsObject  (  yy  )  ; 
VM_Callbacks  .  notifyAppRunStart  (  "VM"  ,  0  )  ; 
if  (  verbose  >=  1  )  VM  .  sysWriteln  (  "Starting main thread"  )  ; 
mainThread  .  start  (  )  ; 
VM_Thread   t  =  new   DebuggerThread  (  )  ; 
t  .  start  (  VM_Scheduler  .  debuggerQueue  )  ; 
if  (  VM_HardwarePerformanceMonitors  .  enabled  (  )  )  { 
if  (  !  VM_HardwarePerformanceMonitors  .  hpm_thread_group  )  { 
if  (  VM_HardwarePerformanceMonitors  .  verbose  >=  1  )  VM  .  sysWrite  (  " VM.boot() call sysHPMresetMyThread()\n"  )  ; 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysHPMresetMyThreadIP  )  ; 
} 
} 
if  (  VM  .  TraceThreads  )  VM_Scheduler  .  trace  (  "VM.boot"  ,  "completed - terminating"  )  ; 
VM_Thread  .  terminate  (  )  ; 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  NOT_REACHED  )  ; 
} 

private   static   VM_Class  [  ]  classObjects  =  new   VM_Class  [  0  ]  ; 





public   static   void   deferClassObjectCreation  (  VM_Class   c  )  throws   VM_PragmaInterruptible  { 
for  (  int   i  =  0  ;  i  <  classObjects  .  length  ;  i  ++  )  { 
if  (  classObjects  [  i  ]  ==  c  )  return  ; 
} 
VM_Class  [  ]  tmp  =  new   VM_Class  [  classObjects  .  length  +  1  ]  ; 
System  .  arraycopy  (  classObjects  ,  0  ,  tmp  ,  0  ,  classObjects  .  length  )  ; 
tmp  [  classObjects  .  length  ]  =  c  ; 
classObjects  =  tmp  ; 
} 





private   static   void   createClassObjects  (  )  throws   VM_PragmaInterruptible  { 
for  (  int   i  =  0  ;  i  <  classObjects  .  length  ;  i  ++  )  { 
if  (  verbose  >=  2  )  { 
VM  .  sysWrite  (  classObjects  [  i  ]  .  toString  (  )  )  ; 
VM  .  sysWriteln  (  )  ; 
} 
classObjects  [  i  ]  .  getClassForType  (  )  ; 
} 
} 






static   void   runClassInitializer  (  String   className  )  throws   VM_PragmaInterruptible  { 
if  (  verbose  >=  2  )  { 
sysWrite  (  "running class intializer for "  )  ; 
sysWriteln  (  className  )  ; 
} 
VM_Atom   classDescriptor  =  VM_Atom  .  findOrCreateAsciiAtom  (  className  .  replace  (  '.'  ,  '/'  )  )  .  descriptorFromClassName  (  )  ; 
VM_TypeReference   tRef  =  VM_TypeReference  .  findOrCreate  (  VM_SystemClassLoader  .  getVMClassLoader  (  )  ,  classDescriptor  )  ; 
VM_Class   cls  =  (  VM_Class  )  tRef  .  peekResolvedType  (  )  ; 
if  (  cls  !=  null  &&  cls  .  isInBootImage  (  )  )  { 
VM_Method   clinit  =  cls  .  getClassInitializerMethod  (  )  ; 
clinit  .  compile  (  )  ; 
if  (  verbose  >=  10  )  VM  .  sysWriteln  (  "invoking method "  +  clinit  )  ; 
VM_Magic  .  invokeClassInitializer  (  clinit  .  getCurrentInstructions  (  )  )  ; 
cls  .  setAllFinalStaticJTOCEntries  (  )  ; 
} 
} 







public   static   void   _assert  (  boolean   b  )  { 
_assert  (  b  ,  null  )  ; 
} 








public   static   void   _assert  (  boolean   b  ,  String   message  )  { 
if  (  !  VM  .  VerifyAssertions  )  { 
_assertionFailure  (  "vm internal error: assert called when !VM.VerifyAssertions"  )  ; 
} 
if  (  !  b  )  _assertionFailure  (  message  )  ; 
} 

private   static   void   _assertionFailure  (  String   message  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  message  ==  null  )  message  =  "vm internal error at:"  ; 
if  (  VM  .  runningVM  )  { 
sysFail  (  message  )  ; 
} 
throw   new   RuntimeException  (  message  )  ; 
} 









public   static   String   intAsHexString  (  int   number  )  throws   VM_PragmaInterruptible  { 
char  [  ]  buf  =  new   char  [  10  ]  ; 
int   index  =  10  ; 
while  (  --  index  >  1  )  { 
int   digit  =  number  &  0x0000000f  ; 
buf  [  index  ]  =  digit  <=  9  ?  (  char  )  (  '0'  +  digit  )  :  (  char  )  (  'a'  +  digit  -  10  )  ; 
number  >  >=  4  ; 
} 
buf  [  index  --  ]  =  'x'  ; 
buf  [  index  ]  =  '0'  ; 
return   new   String  (  buf  )  ; 
} 









public   static   String   longAsHexString  (  long   number  )  throws   VM_PragmaInterruptible  { 
char  [  ]  buf  =  new   char  [  18  ]  ; 
int   index  =  18  ; 
while  (  --  index  >  1  )  { 
int   digit  =  (  int  )  (  number  &  0x000000000000000f  )  ; 
buf  [  index  ]  =  digit  <=  9  ?  (  char  )  (  '0'  +  digit  )  :  (  char  )  (  'a'  +  digit  -  10  )  ; 
number  >  >=  4  ; 
} 
buf  [  index  --  ]  =  'x'  ; 
buf  [  index  ]  =  '0'  ; 
return   new   String  (  buf  )  ; 
} 

public   static   String   addressAsHexString  (  VM_Address   addr  )  throws   VM_PragmaInterruptible  { 
return   longAsHexString  (  addr  .  toLong  (  )  )  ; 
return   intAsHexString  (  addr  .  toInt  (  )  )  ; 
} 

private   static   int   sysWriteLock  =  0  ; 

private   static   int   sysWriteLockOffset  =  -  1  ; 

private   static   void   swLock  (  )  { 
if  (  sysWriteLockOffset  ==  -  1  )  return  ; 
while  (  !  VM_Synchronization  .  testAndSet  (  VM_Magic  .  getJTOC  (  )  ,  sysWriteLockOffset  ,  1  )  )  ; 
} 

private   static   void   swUnlock  (  )  { 
if  (  sysWriteLockOffset  ==  -  1  )  return  ; 
VM_Synchronization  .  fetchAndStore  (  VM_Magic  .  getJTOC  (  )  ,  sysWriteLockOffset  ,  0  )  ; 
} 





public   static   void   write  (  VM_Atom   value  )  throws   VM_PragmaNoInline  { 
value  .  sysWrite  (  )  ; 
} 





public   static   void   write  (  VM_Member   value  )  throws   VM_PragmaNoInline  { 
write  (  value  .  getMemberRef  (  )  )  ; 
} 





public   static   void   write  (  VM_MemberReference   value  )  throws   VM_PragmaNoInline  { 
write  (  value  .  getType  (  )  .  getName  (  )  )  ; 
write  (  "."  )  ; 
write  (  value  .  getName  (  )  )  ; 
write  (  " "  )  ; 
write  (  value  .  getDescriptor  (  )  )  ; 
} 





public   static   void   write  (  String   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
VM_Processor  .  getCurrentProcessor  (  )  .  disableThreadSwitching  (  )  ; 
for  (  int   i  =  0  ,  n  =  value  .  length  (  )  ;  i  <  n  ;  ++  i  )  write  (  value  .  charAt  (  i  )  )  ; 
VM_Processor  .  getCurrentProcessor  (  )  .  enableThreadSwitching  (  )  ; 
}  else  { 
System  .  err  .  print  (  value  )  ; 
} 
} 





public   static   void   write  (  char   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysWriteCharIP  ,  value  )  ;  else   System  .  err  .  print  (  value  )  ; 
} 







public   static   void   write  (  double   value  ,  int   postDecimalDigits  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
boolean   negative  =  (  value  <  0.0  )  ; 
value  =  (  value  <  0.0  )  ?  (  -  value  )  :  value  ; 
int   ones  =  (  int  )  value  ; 
int   multiplier  =  1  ; 
while  (  postDecimalDigits  --  >  0  )  multiplier  *=  10  ; 
int   remainder  =  (  int  )  (  multiplier  *  (  value  -  ones  )  )  ; 
if  (  negative  )  write  (  '-'  )  ; 
write  (  ones  ,  false  )  ; 
write  (  '.'  )  ; 
while  (  multiplier  >  1  )  { 
multiplier  /=  10  ; 
write  (  remainder  /  multiplier  )  ; 
remainder  %=  multiplier  ; 
} 
}  else   System  .  err  .  print  (  value  )  ; 
} 





public   static   void   write  (  int   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
int   mode  =  (  value  <  -  (  1  <<  20  )  ||  value  >  (  1  <<  20  )  )  ?  2  :  0  ; 
VM_SysCall  .  call2  (  VM_BootRecord  .  the_boot_record  .  sysWriteIP  ,  value  ,  mode  )  ; 
}  else  { 
System  .  err  .  print  (  value  )  ; 
} 
} 





public   static   void   writeHex  (  int   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  VM_SysCall  .  call2  (  VM_BootRecord  .  the_boot_record  .  sysWriteIP  ,  value  ,  2  )  ;  else  { 
System  .  err  .  print  (  Integer  .  toHexString  (  value  )  )  ; 
} 
} 





public   static   void   writeHex  (  long   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
int   val1  =  (  int  )  (  value  >  >  32  )  ; 
int   val2  =  (  int  )  (  value  &  0xFFFFFFFF  )  ; 
VM_SysCall  .  call3  (  VM_BootRecord  .  the_boot_record  .  sysWriteLongIP  ,  val1  ,  val2  ,  2  )  ; 
}  else  { 
System  .  err  .  print  (  Long  .  toHexString  (  value  )  )  ; 
} 
} 





public   static   void   writeHex  (  VM_Address   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
writeHex  (  value  .  toLong  (  )  )  ; 
writeHex  (  value  .  toInt  (  )  )  ; 
} 

public   static   void   writeHex  (  VM_Offset   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
writeHex  (  value  .  toLong  (  )  )  ; 
writeHex  (  value  .  toInt  (  )  )  ; 
} 





public   static   void   writeInt  (  int   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  VM_SysCall  .  call2  (  VM_BootRecord  .  the_boot_record  .  sysWriteIP  ,  value  ,  0  )  ;  else  { 
System  .  err  .  print  (  value  )  ; 
} 
} 







public   static   void   write  (  int   value  ,  boolean   hexToo  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  VM_SysCall  .  call2  (  VM_BootRecord  .  the_boot_record  .  sysWriteIP  ,  value  ,  hexToo  ?  1  :  0  )  ;  else   System  .  err  .  print  (  value  )  ; 
} 





public   static   void   write  (  long   value  )  throws   VM_PragmaNoInline  { 
write  (  value  ,  true  )  ; 
} 







public   static   void   write  (  long   value  ,  boolean   hexToo  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
int   val1  ,  val2  ; 
val1  =  (  int  )  (  value  >  >  32  )  ; 
val2  =  (  int  )  (  value  &  0xFFFFFFFF  )  ; 
VM_SysCall  .  call3  (  VM_BootRecord  .  the_boot_record  .  sysWriteLongIP  ,  val1  ,  val2  ,  hexToo  ?  1  :  0  )  ; 
}  else   System  .  err  .  print  (  value  )  ; 
} 

public   static   void   writeField  (  int   fieldWidth  ,  String   s  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
write  (  s  )  ; 
int   len  =  s  .  length  (  )  ; 
while  (  fieldWidth  >  len  ++  )  write  (  " "  )  ; 
} 





public   static   void   writeField  (  int   fieldWidth  ,  int   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
int   len  =  1  ,  temp  =  value  ; 
if  (  temp  <  0  )  { 
len  ++  ; 
temp  =  -  temp  ; 
} 
while  (  temp  >=  10  )  { 
len  ++  ; 
temp  /=  10  ; 
} 
while  (  fieldWidth  >  len  ++  )  write  (  " "  )  ; 
if  (  runningVM  )  VM_SysCall  .  call2  (  VM_BootRecord  .  the_boot_record  .  sysWriteIP  ,  value  ,  0  )  ;  else   System  .  err  .  print  (  value  )  ; 
} 





public   static   void   writeField  (  int   fieldWidth  ,  VM_Atom   s  )  throws   VM_PragmaNoInline  { 
int   len  =  s  .  length  (  )  ; 
while  (  fieldWidth  >  len  ++  )  write  (  " "  )  ; 
write  (  s  )  ; 
} 

public   static   void   writeln  (  )  { 
write  (  '\n'  )  ; 
} 

public   static   void   write  (  double   d  )  { 
write  (  d  ,  2  )  ; 
} 

public   static   void   write  (  VM_Address   addr  )  { 
writeHex  (  addr  )  ; 
} 

public   static   void   write  (  VM_Offset   offset  )  { 
writeHex  (  offset  )  ; 
} 

public   static   void   write  (  VM_Word   w  )  { 
writeHex  (  w  .  toAddress  (  )  )  ; 
} 

public   static   void   write  (  boolean   b  )  { 
write  (  b  ?  "true"  :  "false"  )  ; 
} 




public   static   void   sysWrite  (  VM_Atom   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  a  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  VM_Atom   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  a  )  ; 
write  (  "\n"  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  VM_Member   m  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  m  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  VM_MemberReference   mr  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  mr  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  "\n"  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  char   c  )  throws   VM_PragmaNoInline  { 
write  (  c  )  ; 
} 

public   static   void   sysWriteField  (  int   w  ,  int   v  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
writeField  (  w  ,  v  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteField  (  int   w  ,  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
writeField  (  w  ,  s  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteHex  (  int   v  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
writeHex  (  v  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteHex  (  VM_Address   v  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
writeHex  (  v  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteInt  (  int   v  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
writeInt  (  v  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteLong  (  long   v  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  v  ,  false  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  double   d  ,  int   p  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  d  ,  p  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  double   d  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  d  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  a  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  a  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  VM_Offset   o  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  o  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  VM_Offset   o  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  o  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  VM_Word   w  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  w  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  boolean   b  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  b  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  double   d  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  d  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  long   l  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  l  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  boolean   b  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  b  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  i  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  boolean   b  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  b  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  boolean   b  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  b  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  double   d  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  d  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  double   d  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  d  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  double   d  ,  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  d  )  ; 
write  (  s  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  double   d  ,  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  d  )  ; 
write  (  s  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  long   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  i  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  long   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  int   i  ,  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
write  (  s  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  int   i  ,  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
write  (  s  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  a  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  a  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  VM_Offset   o  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  o  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  VM_Offset   o  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  o  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s  ,  VM_Word   w  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  w  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s  ,  VM_Word   w  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s  )  ; 
write  (  w  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  a  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  a  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  i  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  int   i  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i  )  ; 
write  (  s2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  int   i  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i  )  ; 
write  (  s2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  String   s3  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  String   s3  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  int   i1  ,  String   s  ,  int   i2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i1  )  ; 
write  (  s  )  ; 
write  (  i2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  int   i1  ,  String   s  ,  int   i2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i1  )  ; 
write  (  s  )  ; 
write  (  i2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  int   i1  ,  String   s1  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i1  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  int   i1  ,  String   s1  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i1  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  int   i1  ,  String   s2  ,  int   i2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i1  )  ; 
write  (  s2  )  ; 
write  (  i2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  int   i1  ,  String   s2  ,  int   i2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i1  )  ; 
write  (  s2  )  ; 
write  (  i2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  int   i1  ,  String   s2  ,  long   l1  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i1  )  ; 
write  (  s2  )  ; 
write  (  l1  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  int   i1  ,  String   s2  ,  long   l1  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  i1  )  ; 
write  (  s2  )  ; 
write  (  l1  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  double   d  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  d  )  ; 
write  (  s2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  double   d  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  d  )  ; 
write  (  s2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  int   i1  ,  String   s3  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  i1  )  ; 
write  (  s3  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  int   i1  ,  String   s3  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  i1  )  ; 
write  (  s3  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  String   s3  ,  int   i1  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
write  (  i1  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  String   s3  ,  int   i1  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
write  (  i1  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  int   i  ,  String   s1  ,  double   d  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
write  (  s1  )  ; 
write  (  d  )  ; 
write  (  s2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  int   i  ,  String   s1  ,  double   d  ,  String   s2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  i  )  ; 
write  (  s1  )  ; 
write  (  d  )  ; 
write  (  s2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  String   s2  ,  String   s3  ,  int   i1  ,  String   s4  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
write  (  i1  )  ; 
write  (  s4  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  String   s2  ,  String   s3  ,  int   i1  ,  String   s4  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  s2  )  ; 
write  (  s3  )  ; 
write  (  i1  )  ; 
write  (  s4  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWrite  (  String   s1  ,  VM_Address   a  ,  String   s2  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  a  )  ; 
write  (  s2  )  ; 
write  (  i  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   sysWriteln  (  String   s1  ,  VM_Address   a  ,  String   s2  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
write  (  s1  )  ; 
write  (  a  )  ; 
write  (  s2  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

private   static   void   showProc  (  )  { 
VM_Processor   p  =  VM_Processor  .  getCurrentProcessor  (  )  ; 
write  (  "Proc "  )  ; 
write  (  p  .  id  )  ; 
write  (  ": "  )  ; 
} 

private   static   void   showThread  (  )  { 
write  (  "Thread "  )  ; 
write  (  VM_Thread  .  getCurrentThread  (  )  .  getIndex  (  )  )  ; 
write  (  ": "  )  ; 
} 

public   static   void   ptsysWriteln  (  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
showThread  (  )  ; 
write  (  s  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  a  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s  ,  int   i  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s  )  ; 
write  (  i  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s  ,  VM_Address   a  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s  )  ; 
write  (  a  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  ,  String   s3  ,  VM_Address   a3  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
write  (  s3  )  ; 
write  (  a3  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  ,  String   s3  ,  VM_Address   a3  ,  String   s4  ,  VM_Address   a4  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
write  (  s3  )  ; 
write  (  a3  )  ; 
write  (  s4  )  ; 
write  (  a4  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 

public   static   void   psysWriteln  (  String   s1  ,  VM_Address   a1  ,  String   s2  ,  VM_Address   a2  ,  String   s3  ,  VM_Address   a3  ,  String   s4  ,  VM_Address   a4  ,  String   s5  ,  VM_Address   a5  )  throws   VM_PragmaNoInline  { 
swLock  (  )  ; 
showProc  (  )  ; 
write  (  s1  )  ; 
write  (  a1  )  ; 
write  (  s2  )  ; 
write  (  a2  )  ; 
write  (  s3  )  ; 
write  (  a3  )  ; 
write  (  s4  )  ; 
write  (  a4  )  ; 
write  (  s5  )  ; 
write  (  a5  )  ; 
writeln  (  )  ; 
swUnlock  (  )  ; 
} 





public   static   void   sysFail  (  String   message  )  throws   VM_PragmaNoInline  { 
VM_Scheduler  .  traceback  (  message  )  ; 
VM  .  shutdown  (  1  )  ; 
} 





public   static   void   sysExit  (  int   value  )  throws   VM_PragmaLogicallyUninterruptible  ,  VM_PragmaNoInline  { 
if  (  runningVM  )  { 
VM_Wait  .  disableIoWait  (  )  ; 
VM_Callbacks  .  notifyExit  (  value  )  ; 
VM  .  shutdown  (  value  )  ; 
}  else  { 
System  .  exit  (  value  )  ; 
} 
} 






public   static   void   shutdown  (  int   value  )  { 
if  (  VM  .  VerifyAssertions  )  VM  .  _assert  (  VM  .  runningVM  )  ; 
if  (  VM  .  runningAsSubsystem  )  { 
VM_Scheduler  .  processorExit  (  value  )  ; 
}  else  { 
VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysExitIP  ,  value  )  ; 
} 
} 









static   int   sysVirtualProcessorCreate  (  VM_Address   jtoc  ,  VM_Address   pr  ,  VM_Address   ti  ,  VM_Address   fp  )  { 
return   VM_SysCall  .  call_I_A_A_A_A  (  VM_BootRecord  .  the_boot_record  .  sysVirtualProcessorCreateIP  ,  jtoc  ,  pr  ,  ti  ,  fp  )  ; 
} 





static   void   sysVirtualProcessorBind  (  int   cpuId  )  { 
VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysVirtualProcessorBindIP  ,  cpuId  )  ; 
} 




public   static   void   sysVirtualProcessorYield  (  )  { 
return  ; 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysVirtualProcessorYieldIP  )  ; 
} 






static   void   sysVirtualProcessorEnableTimeSlicing  (  int   timeSlice  )  { 
VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysVirtualProcessorEnableTimeSlicingIP  ,  timeSlice  )  ; 
} 

static   void   sysCreateThreadSpecificDataKeys  (  )  { 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysCreateThreadSpecificDataKeysIP  )  ; 
} 

static   void   sysWaitForVirtualProcessorInitialization  (  )  { 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysWaitForVirtualProcessorInitializationIP  )  ; 
} 

static   void   sysWaitForMultithreadingStart  (  )  { 
VM_SysCall  .  call0  (  VM_BootRecord  .  the_boot_record  .  sysWaitForMultithreadingStartIP  )  ; 
} 

static   void   sysInitializeStartupLocks  (  int   howMany  )  { 
VM_SysCall  .  call1  (  VM_BootRecord  .  the_boot_record  .  sysInitializeStartupLocksIP  ,  howMany  )  ; 
} 








private   static   void   init  (  String   vmClassPath  ,  String  [  ]  bootCompilerArgs  )  throws   VM_PragmaInterruptible  ,  ClassNotFoundException  { 
VM_BootRecord  .  the_boot_record  =  new   VM_BootRecord  (  )  ; 
VM_ClassLoader  .  init  (  vmClassPath  )  ; 
VM_JNIEnvironment  .  init  (  )  ; 
VM_Entrypoints  .  init  (  )  ; 
VM_OutOfLineMachineCode  .  init  (  )  ; 
if  (  writingBootImage  )  VM_BootImageCompiler  .  init  (  bootCompilerArgs  )  ; 
VM_Runtime  .  init  (  )  ; 
VM_Scheduler  .  init  (  )  ; 
MM_Interface  .  init  (  )  ; 
} 






























public   static   void   disableGC  (  )  throws   VM_PragmaInline  ,  VM_PragmaInterruptible  { 
VM_Thread   myThread  =  VM_Thread  .  getCurrentThread  (  )  ; 
if  (  VM_Magic  .  getFramePointer  (  )  .  sub  (  STACK_SIZE_GCDISABLED  )  .  LT  (  myThread  .  stackLimit  )  &&  !  myThread  .  hasNativeStackFrame  (  )  )  { 
VM_Thread  .  resizeCurrentStack  (  myThread  .  stack  .  length  +  (  STACK_SIZE_GCDISABLED  >  >  2  )  ,  null  )  ; 
} 
VM_Processor  .  getCurrentProcessor  (  )  .  disableThreadSwitching  (  )  ; 
if  (  VM  .  VerifyAssertions  )  { 
VM  .  _assert  (  myThread  .  disallowAllocationsByThisThread  ==  false  )  ; 
myThread  .  disallowAllocationsByThisThread  =  true  ; 
} 
} 




public   static   void   enableGC  (  )  throws   VM_PragmaInline  { 
if  (  VM  .  VerifyAssertions  )  { 
VM_Thread   myThread  =  VM_Thread  .  getCurrentThread  (  )  ; 
VM  .  _assert  (  myThread  .  disallowAllocationsByThisThread  ==  true  )  ; 
myThread  .  disallowAllocationsByThisThread  =  false  ; 
} 
VM_Processor  .  getCurrentProcessor  (  )  .  enableThreadSwitching  (  )  ; 
} 




public   static   void   debugBreakpoint  (  )  throws   VM_PragmaNoInline  ,  VM_PragmaNoOptCompile  { 
} 
} 

