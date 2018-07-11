package   fr  .  x9c  .  cadmium  .  kernel  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   fr  .  x9c  .  cadmium  .  primitives  .  systhreads  .  ThreadStatus  ; 










public   abstract   class   AbstractNativeRunner   extends   AbstractCodeRunner  { 


private   final   Map  <  Class  ,  Value  >  constants  ; 


private   final   Map  <  String  ,  Value  >  globals  ; 


private   int   globalsInited  ; 


private   List  <  Method  >  closures  ; 


protected   Value   result  ; 


protected   Throwable   exception  ; 


private   Throwable   backtraceInfo  ; 





public   AbstractNativeRunner  (  final   NativeParameters   np  )  { 
super  (  new   Context  (  np  ,  true  ,  new   File  (  "."  )  )  )  ; 
this  .  constants  =  new   HashMap  <  Class  ,  Value  >  (  )  ; 
this  .  globals  =  new   HashMap  <  String  ,  Value  >  (  )  ; 
this  .  globalsInited  =  0  ; 
this  .  closures  =  new   ArrayList  <  Method  >  (  )  ; 
} 





public   AbstractNativeRunner  (  final   AbstractNativeRunner   that  )  { 
super  (  that  .  context  )  ; 
this  .  constants  =  that  .  constants  ; 
this  .  globals  =  that  .  globals  ; 
this  .  globalsInited  =  that  .  globalsInited  ; 
this  .  closures  =  that  .  closures  ; 
} 





public   final   Value   getResult  (  )  { 
return   this  .  result  ; 
} 







public   final   void   checkSignals  (  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  ,  CadmiumException  { 
Signals  .  processSignal  (  this  )  ; 
} 







public   final   Value   getAtom  (  final   int   atm  )  { 
return   this  .  context  .  getAtom  (  atm  )  ; 
} 






public   final   void   loadConstant  (  final   Class   id  )  throws   CadmiumException  { 
try  { 
final   InputStream   is  =  id  .  getResourceAsStream  (  id  .  getSimpleName  (  )  +  ".consts"  )  ; 
if  (  is  ==  null  )  { 
throw   new   CadmiumException  (  "unable to load constants for "  +  id  )  ; 
} 
final   DataInputStream   dis  =  new   DataInputStream  (  is  )  ; 
final   Value   consts  =  Intern  .  inputVal  (  this  .  context  ,  dis  ,  true  )  ; 
dis  .  close  (  )  ; 
is  .  close  (  )  ; 
this  .  constants  .  put  (  id  ,  consts  )  ; 
}  catch  (  final   IOException   ioe  )  { 
throw   new   CadmiumException  (  "unable to load constants for "  +  id  )  ; 
}  catch  (  final   Fail  .  Exception   fe  )  { 
throw   new   CadmiumException  (  "unable to load constants for "  +  id  )  ; 
}  catch  (  final   Fatal  .  Exception   fe  )  { 
throw   new   CadmiumException  (  "unable to load constants for "  +  id  )  ; 
} 
} 







public   final   Value   getConstant  (  final   Class   id  ,  final   int   idx  )  { 
assert   id  !=  null  :  "null id"  ; 
return   this  .  constants  .  get  (  id  )  .  asBlock  (  )  .  get  (  idx  )  ; 
} 






public   final   void   createGlobal  (  final   String   id  ,  final   int   sz  )  { 
assert   id  !=  null  :  "null id"  ; 
assert   sz  >=  0  :  "sz should be >= 0"  ; 
final   Block   bl  =  Block  .  createBlock  (  sz  ,  0  )  ; 
this  .  globals  .  put  (  id  ,  Value  .  createFromBlock  (  bl  )  )  ; 
} 





public   final   void   registerPredefinedException  (  final   String   name  )  { 
assert   name  !=  null  :  "null name"  ; 
final   String   bucketName  =  "caml_bucket_"  +  name  ; 
final   String   symName  =  "caml_exn_"  +  name  ; 
final   Value   symValue  =  Value  .  createFromBlock  (  Block  .  createBlock  (  0  ,  Value  .  createFromBlock  (  Block  .  createString  (  name  )  )  )  )  ; 
this  .  globals  .  put  (  symName  ,  symValue  )  ; 
this  .  globals  .  put  (  bucketName  ,  Value  .  createFromBlock  (  Block  .  createBlock  (  0  ,  symValue  )  )  )  ; 
} 






public   final   Value   getGlobal  (  final   String   id  )  { 
assert   id  !=  null  :  "null id"  ; 
return   this  .  globals  .  get  (  id  )  ; 
} 






public   final   void   setGlobal  (  final   String   id  ,  final   Value   val  )  { 
assert   id  !=  null  :  "null id"  ; 
this  .  globals  .  put  (  id  ,  val  )  ; 
} 





public   final   int   getGlobalsInited  (  )  { 
return   this  .  globalsInited  ; 
} 




public   final   void   incrGlobalsInited  (  )  { 
this  .  globalsInited  ++  ; 
} 








public   static   boolean   equalValues  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
if  (  v1  .  isBlock  (  )  )  { 
return   v2  .  isBlock  (  )  ?  v1  .  asBlock  (  )  ==  v2  .  asBlock  (  )  :  false  ; 
}  else  { 
return   v1  .  getRawValue  (  )  ==  v2  .  getRawValue  (  )  ; 
} 
} 








public   static   boolean   notEqualValues  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return  !  equalValues  (  v1  ,  v2  )  ; 
} 








public   static   boolean   lowerThanValue  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return   v1  .  universalLong  (  )  <  v2  .  universalLong  (  )  ; 
} 








public   static   boolean   lowerEqualValue  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return   v1  .  universalLong  (  )  <=  v2  .  universalLong  (  )  ; 
} 








public   static   boolean   greaterThanValue  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return   v1  .  universalLong  (  )  >  v2  .  universalLong  (  )  ; 
} 








public   static   boolean   greaterEqualValue  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return   v1  .  universalLong  (  )  >=  v2  .  universalLong  (  )  ; 
} 








public   static   boolean   isOutValue  (  final   Value   v1  ,  final   Value   v2  )  { 
assert   v1  !=  null  :  "null v1"  ; 
assert   v2  !=  null  :  "null v2"  ; 
return   v1  .  universalUnsignedLong  (  )  <  v2  .  universalUnsignedLong  (  )  ; 
} 






private   int   addClosure  (  final   Method   meth  )  { 
assert   meth  !=  null  :  "null meth"  ; 
synchronized  (  this  .  closures  )  { 
final   int   res  =  this  .  closures  .  size  (  )  ; 
this  .  closures  .  add  (  meth  )  ; 
return   res  ; 
} 
} 






private   Method   getClosure  (  final   int   id  )  { 
synchronized  (  this  .  closures  )  { 
return   this  .  closures  .  get  (  id  )  ; 
} 
} 








public   final   int   registerClosure  (  final   Class   cls  ,  final   String   name  )  throws   Fatal  .  Exception  { 
assert   cls  !=  null  :  "null cls"  ; 
assert   name  !=  null  :  "null name"  ; 
for  (  Method   m  :  cls  .  getMethods  (  )  )  { 
if  (  m  .  getName  (  )  .  equals  (  name  )  )  { 
return   addClosure  (  m  )  ; 
} 
} 
Fatal  .  raise  (  "unknown function:"  +  name  +  " "  +  cls  .  getSimpleName  (  )  )  ; 
return   0  ; 
} 








public   final   Value   registerClosure1  (  final   Class   cls  ,  final   String   name  )  throws   Fatal  .  Exception  { 
assert   cls  !=  null  :  "null cls"  ; 
assert   name  !=  null  :  "null name"  ; 
for  (  Method   m  :  cls  .  getMethods  (  )  )  { 
if  (  m  .  getName  (  )  .  equals  (  name  )  )  { 
final   Block   res  =  Block  .  createClosure  (  2  )  ; 
res  .  setCustom  (  res  )  ; 
res  .  setCode  (  addClosure  (  m  )  )  ; 
res  .  set  (  1  ,  Value  .  ONE  )  ; 
return   Value  .  createFromBlock  (  res  )  ; 
} 
} 
Fatal  .  raise  (  "unknown function:"  +  name  +  " "  +  cls  .  getSimpleName  (  )  )  ; 
return   null  ; 
} 









public   final   Value   registerClosureN  (  final   Class   cls  ,  final   String   name  ,  final   int   arity  )  throws   Fatal  .  Exception  { 
assert   cls  !=  null  :  "null cls"  ; 
assert   name  !=  null  :  "null name"  ; 
assert   arity  >  1  :  "arity should be > 1"  ; 
for  (  Method   m  :  cls  .  getMethods  (  )  )  { 
if  (  m  .  getName  (  )  .  equals  (  name  )  )  { 
final   int   code  =  arity  <  0  ?  -  1  :  -  arity  -  1  ; 
final   Block   res  =  Block  .  createClosure  (  3  )  ; 
res  .  setCustom  (  res  )  ; 
res  .  setCode  (  code  )  ; 
res  .  set  (  1  ,  Value  .  createFromLong  (  arity  )  )  ; 
res  .  set  (  2  ,  Value  .  createFromLong  (  addClosure  (  m  )  )  )  ; 
return   Value  .  createFromBlock  (  res  )  ; 
} 
} 
Fatal  .  raise  (  "unknown function:"  +  name  +  " "  +  cls  .  getSimpleName  (  )  )  ; 
return   null  ; 
} 









public   final   void   registerInfix1  (  final   Block   parent  ,  final   int   idx  ,  final   Class   cls  ,  final   String   name  )  throws   Fatal  .  Exception  { 
assert   parent  !=  null  :  "null parent"  ; 
assert   cls  !=  null  :  "null cls"  ; 
assert   name  !=  null  :  "null name"  ; 
for  (  Method   m  :  cls  .  getMethods  (  )  )  { 
if  (  m  .  getName  (  )  .  equals  (  name  )  )  { 
final   Block   infix  =  Block  .  createInfix  (  idx  +  1  )  ; 
infix  .  setParent  (  parent  )  ; 
infix  .  setCustom  (  infix  )  ; 
infix  .  setCode  (  addClosure  (  m  )  )  ; 
parent  .  set  (  idx  ,  Value  .  createFromRawValue  (  infix  .  getTag  (  )  )  )  ; 
parent  .  set  (  idx  +  1  ,  Value  .  createFromBlock  (  infix  )  )  ; 
parent  .  set  (  idx  +  2  ,  Value  .  ONE  )  ; 
return  ; 
} 
} 
Fatal  .  raise  (  "unknown function:"  +  name  +  " "  +  cls  .  getSimpleName  (  )  )  ; 
} 










public   final   void   registerInfixN  (  final   Block   parent  ,  final   int   idx  ,  final   Class   cls  ,  final   String   name  ,  final   int   arity  )  throws   Fatal  .  Exception  { 
assert   parent  !=  null  :  "null parent"  ; 
assert   cls  !=  null  :  "null cls"  ; 
assert   name  !=  null  :  "null name"  ; 
assert   arity  >  1  :  "arity should be > 1"  ; 
for  (  Method   m  :  cls  .  getMethods  (  )  )  { 
if  (  m  .  getName  (  )  .  equals  (  name  )  )  { 
final   int   code  =  arity  <  0  ?  -  1  :  -  arity  -  1  ; 
final   Block   infix  =  Block  .  createInfix  (  idx  +  1  )  ; 
infix  .  setParent  (  parent  )  ; 
infix  .  setCustom  (  infix  )  ; 
infix  .  setCode  (  code  )  ; 
parent  .  set  (  idx  ,  Value  .  createFromRawValue  (  infix  .  getTag  (  )  )  )  ; 
parent  .  set  (  idx  +  1  ,  Value  .  createFromBlock  (  infix  )  )  ; 
parent  .  set  (  idx  +  2  ,  Value  .  createFromLong  (  arity  )  )  ; 
parent  .  set  (  idx  +  3  ,  Value  .  createFromLong  (  addClosure  (  m  )  )  )  ; 
return  ; 
} 
} 
Fatal  .  raise  (  "unknown function:"  +  name  +  " "  +  cls  .  getSimpleName  (  )  )  ; 
} 




@  Override 
public   final   Value   callback  (  final   Value   closure  ,  final   Value  ...  params  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  { 
assert   closure  !=  null  :  "null closure"  ; 
assert   params  !=  null  :  "null params"  ; 
assert   params  .  length  +  4  <=  256  :  "params is too long"  ; 
final   Thread   currentThread  =  Thread  .  currentThread  (  )  ; 
final   boolean   isCadmiumThread  =  currentThread   instanceof   CadmiumThread  ; 
if  (  !  isCadmiumThread  )  { 
this  .  context  .  addAdditionalThread  (  currentThread  )  ; 
} 
final   AbstractNativeRunner   runner  =  copy  (  )  ; 
runner  .  threadStatus  =  null  ; 
runner  .  setup  (  closure  ,  params  )  ; 
try  { 
runner  .  run  (  )  ; 
}  finally  { 
if  (  !  isCadmiumThread  )  { 
this  .  context  .  removeAdditionalThread  (  currentThread  )  ; 
} 
} 
if  (  runner  .  exception  !=  null  )  { 
if  (  runner  .  exception   instanceof   Fail  .  Exception  )  { 
throw  (  Fail  .  Exception  )  runner  .  exception  ; 
}  else   if  (  runner  .  exception   instanceof   Fatal  .  Exception  )  { 
throw  (  Fatal  .  Exception  )  runner  .  exception  ; 
}  else   if  (  runner  .  exception   instanceof   FalseExit  )  { 
final   boolean   backtrace  =  this  .  context  .  isBacktraceActive  (  )  ; 
this  .  context  .  setBacktraceActive  (  false  )  ; 
final   Value   atExit  =  this  .  context  .  getCallback  (  "Pervasives.do_at_exit"  )  ; 
if  (  atExit  !=  null  )  { 
try  { 
callback  (  atExit  ,  Value  .  UNIT  )  ; 
}  catch  (  final   Throwable   t  )  { 
} 
} 
this  .  context  .  setBacktraceActive  (  backtrace  )  ; 
throw  (  FalseExit  )  runner  .  exception  ; 
}  else  { 
Fatal  .  raise  (  "error in callback: "  +  runner  .  exception  )  ; 
} 
} 
return   runner  .  result  ; 
} 





public   final   void   run  (  )  { 
this  .  result  =  null  ; 
this  .  exception  =  null  ; 
try  { 
context  .  leaveBlockingSection  (  )  ; 
}  catch  (  final   Fail  .  Exception   fe  )  { 
return  ; 
}  catch  (  final   FalseExit   fe  )  { 
return  ; 
} 
try  { 
if  (  this  .  closure  ==  null  )  { 
try  { 
moduleMain  (  )  ; 
}  catch  (  final   Throwable   t  )  { 
setBacktraceInfo  (  t  )  ; 
this  .  exception  =  t  ; 
} 
}  else  { 
final   Application   app  =  new   Application  (  this  ,  this  .  closure  ,  this  .  args  .  length  )  ; 
for  (  int   i  =  this  .  args  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
app  .  params  [  app  .  next  --  ]  =  this  .  args  [  i  ]  ; 
} 
try  { 
this  .  result  =  returnApplication  (  app  )  ; 
}  catch  (  final   Throwable   t  )  { 
setBacktraceInfo  (  t  )  ; 
this  .  exception  =  t  ; 
} 
} 
}  finally  { 
if  (  this  .  threadStatus  !=  null  )  { 
(  (  ThreadStatus  )  this  .  threadStatus  .  asBlock  (  )  .  get  (  TERMINATED  )  .  asBlock  (  )  .  asCustom  (  )  )  .  terminate  (  )  ; 
} 
context  .  enterBlockingSection  (  )  ; 
} 
} 




public   final   void   execute  (  )  { 
this  .  context  .  setMainCodeRunner  (  this  )  ; 
setup  (  null  )  ; 
final   CadmiumThread   thread  =  new   CadmiumThread  (  this  .  context  .  getThreadGroup  (  )  ,  this  )  ; 
this  .  context  .  setMainThread  (  thread  )  ; 
thread  .  start  (  )  ; 
while  (  thread  .  isAlive  (  )  )  { 
try  { 
thread  .  join  (  )  ; 
}  catch  (  final   InterruptedException   ie  )  { 
return  ; 
} 
} 
Signals  .  unregisterContext  (  this  .  context  )  ; 
this  .  context  .  clearSignals  (  )  ; 
if  (  (  this  .  exception  !=  null  )  &&  !  (  this  .  exception   instanceof   FalseExit  )  )  { 
final   Channel   ch  =  this  .  context  .  getChannel  (  Channel  .  STDERR  )  ; 
final   PrintStream   err  ; 
final   ByteArrayOutputStream   altErr  ; 
if  (  (  ch  !=  null  )  &&  (  ch  .  asOutputStream  (  )  !=  null  )  )  { 
altErr  =  null  ; 
err  =  new   PrintStream  (  ch  .  asOutputStream  (  )  ,  true  )  ; 
}  else  { 
altErr  =  new   ByteArrayOutputStream  (  )  ; 
err  =  new   PrintStream  (  altErr  ,  true  )  ; 
} 
final   boolean   backtrace  =  this  .  context  .  isBacktraceActive  (  )  ; 
this  .  context  .  setBacktraceActive  (  false  )  ; 
final   Value   atExit  =  this  .  context  .  getCallback  (  "Pervasives.do_at_exit"  )  ; 
if  (  atExit  !=  null  )  { 
try  { 
callback  (  atExit  ,  Value  .  UNIT  )  ; 
}  catch  (  final   Throwable   t  )  { 
} 
} 
this  .  context  .  setBacktraceActive  (  backtrace  )  ; 
if  (  this  .  exception   instanceof   Fail  .  Exception  )  { 
final   String   msg  =  Misc  .  convertException  (  (  (  Fail  .  Exception  )  this  .  exception  )  .  asValue  (  this  )  )  ; 
err  .  println  (  "Fatal error: exception "  +  msg  )  ; 
if  (  this  .  context  .  isBacktraceActive  (  )  )  { 
printExceptionBacktrace  (  err  )  ; 
} 
err  .  close  (  )  ; 
return  ; 
}  else   if  (  this  .  exception   instanceof   Fatal  .  Exception  )  { 
err  .  println  (  (  (  Fatal  .  Exception  )  this  .  exception  )  .  getMessage  (  )  )  ; 
err  .  close  (  )  ; 
return  ; 
}  else  { 
err  .  println  (  this  .  exception  .  toString  (  )  )  ; 
err  .  close  (  )  ; 
return  ; 
} 
}  else  { 
final   boolean   backtrace  =  this  .  context  .  isBacktraceActive  (  )  ; 
this  .  context  .  setBacktraceActive  (  false  )  ; 
final   Value   atExit  =  this  .  context  .  getCallback  (  "Pervasives.do_at_exit"  )  ; 
if  (  atExit  !=  null  )  { 
try  { 
callback  (  atExit  ,  Value  .  UNIT  )  ; 
}  catch  (  final   Throwable   t  )  { 
} 
} 
this  .  context  .  setBacktraceActive  (  backtrace  )  ; 
} 
} 





public   final   void   executeWithBindings  (  final   Map  <  String  ,  Value  >  bindings  )  { 
this  .  bindings  =  bindings  ; 
execute  (  )  ; 
} 







private   StackTraceElement  [  ]  filterBackTrace  (  final   StackTraceElement  [  ]  elems  )  { 
assert   elems  !=  null  :  "null elems"  ; 
final   boolean   simplified  =  (  (  NativeParameters  )  this  .  context  .  getParameters  (  )  )  .  isSimplifiedBacktrace  (  )  ; 
final   List  <  StackTraceElement  >  l  =  new   ArrayList  <  StackTraceElement  >  (  Arrays  .  asList  (  elems  )  )  ; 
final   Iterator  <  StackTraceElement  >  it  =  l  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
final   StackTraceElement   e  =  it  .  next  (  )  ; 
final   String   className  =  e  .  getClassName  (  )  ; 
final   boolean   filtered  =  className  .  startsWith  (  "java.lang."  )  ||  className  .  startsWith  (  "java.lang.reflect."  )  ||  className  .  startsWith  (  "fr.x9c.cadmium.kernel."  )  ; 
if  (  (  simplified  &&  filtered  )  ||  (  e  .  getFileName  (  )  ==  null  )  ||  (  e  .  getLineNumber  (  )  <  0  )  )  { 
it  .  remove  (  )  ; 
} 
} 
return   l  .  toArray  (  new   StackTraceElement  [  l  .  size  (  )  ]  )  ; 
} 




@  Override 
public   final   void   printExceptionBacktrace  (  final   PrintStream   out  )  { 
assert   out  !=  null  :  "null out"  ; 
for  (  StackTraceElement   elem  :  filterBackTrace  (  this  .  exception  .  getStackTrace  (  )  )  )  { 
out  .  println  (  "\tat "  +  elem  )  ; 
} 
} 





public   void   setBacktraceInfo  (  final   Throwable   t  )  { 
if  (  this  .  context  .  isBacktraceActive  (  )  )  { 
this  .  backtraceInfo  =  t  ; 
} 
} 




@  Override 
public   void   clearBacktraceInfo  (  )  { 
this  .  backtraceInfo  =  null  ; 
} 




@  Override 
public   Value   getExceptionBacktrace  (  )  { 
if  (  this  .  backtraceInfo  !=  null  )  { 
final   StackTraceElement  [  ]  l  =  filterBackTrace  (  this  .  backtraceInfo  .  getStackTrace  (  )  )  ; 
final   int   len  =  l  .  length  ; 
final   Block   arr  =  Block  .  createBlock  (  len  ,  0  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
final   StackTraceElement   e  =  l  [  i  ]  ; 
final   Block   p  =  Block  .  createBlock  (  5  ,  0  )  ; 
p  .  set  (  0  ,  i  ==  0  ?  Value  .  TRUE  :  Value  .  FALSE  )  ; 
p  .  set  (  1  ,  Value  .  createFromBlock  (  Block  .  createString  (  e  .  getFileName  (  )  )  )  )  ; 
p  .  set  (  2  ,  Value  .  createFromLong  (  e  .  getLineNumber  (  )  )  )  ; 
p  .  set  (  3  ,  Value  .  ZERO  )  ; 
p  .  set  (  4  ,  Value  .  ZERO  )  ; 
arr  .  set  (  i  ,  Value  .  createFromBlock  (  p  )  )  ; 
} 
final   Block   res  =  Block  .  createBlock  (  0  ,  Value  .  createFromBlock  (  arr  )  )  ; 
return   Value  .  createFromBlock  (  res  )  ; 
}  else  { 
final   Block   arr  =  Block  .  createBlock  (  0  ,  0  )  ; 
final   Block   res  =  Block  .  createBlock  (  0  ,  Value  .  createFromBlock  (  arr  )  )  ; 
return   Value  .  createFromBlock  (  res  )  ; 
} 
} 






public   final   CodeRunner   createNewThread  (  final   Value   status  )  { 
final   AbstractCodeRunner   res  =  copy  (  )  ; 
if  (  status  !=  null  )  { 
res  .  setThreadStatus  (  status  )  ; 
} 
return   res  ; 
} 




protected   abstract   AbstractNativeRunner   copy  (  )  ; 




protected   abstract   void   moduleMain  (  )  ; 






public   static   Value   createBlock  (  final   Value   p1  ,  final   int   tag  )  { 
return   Value  .  createFromBlock  (  Block  .  createBlock  (  tag  ,  p1  )  )  ; 
} 







public   static   Value   createBlock  (  final   Value   p1  ,  final   Value   p2  ,  final   int   tag  )  { 
return   Value  .  createFromBlock  (  Block  .  createBlock  (  tag  ,  p1  ,  p2  )  )  ; 
} 








public   static   Value   createBlock  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   int   tag  )  { 
return   Value  .  createFromBlock  (  Block  .  createBlock  (  tag  ,  p1  ,  p2  ,  p3  )  )  ; 
} 









public   static   Value   createBlock  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   p4  ,  final   int   tag  )  { 
return   Value  .  createFromBlock  (  Block  .  createBlock  (  tag  ,  p1  ,  p2  ,  p3  ,  p4  )  )  ; 
} 






public   static   Values   createValues  (  final   int   tag  ,  final   int   n  )  { 
return   new   Values  (  tag  ,  n  )  ; 
} 





public   static   Values   createDoubles  (  final   int   n  )  { 
return   new   Values  (  0  ,  n  )  ; 
} 







public   static   Values   foldValues1  (  final   Value   p1  ,  final   Values   v  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   v  !=  null  :  "null v"  ; 
v  .  elements  [  v  .  next  --  ]  =  p1  ; 
return   v  ; 
} 








public   static   Values   foldValues2  (  final   Value   p1  ,  final   Value   p2  ,  final   Values   v  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   v  !=  null  :  "null v"  ; 
v  .  elements  [  v  .  next  --  ]  =  p2  ; 
v  .  elements  [  v  .  next  --  ]  =  p1  ; 
return   v  ; 
} 









public   static   Values   foldValues3  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Values   v  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   v  !=  null  :  "null v"  ; 
v  .  elements  [  v  .  next  --  ]  =  p3  ; 
v  .  elements  [  v  .  next  --  ]  =  p2  ; 
v  .  elements  [  v  .  next  --  ]  =  p1  ; 
return   v  ; 
} 










public   static   Values   foldValues4  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   p4  ,  final   Values   v  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   p4  !=  null  :  "null p4"  ; 
assert   v  !=  null  :  "null v"  ; 
v  .  elements  [  v  .  next  --  ]  =  p4  ; 
v  .  elements  [  v  .  next  --  ]  =  p3  ; 
v  .  elements  [  v  .  next  --  ]  =  p2  ; 
v  .  elements  [  v  .  next  --  ]  =  p1  ; 
return   v  ; 
} 






public   static   Value   returnValues  (  final   Values   v  )  { 
assert   v  !=  null  :  "null v"  ; 
final   Block   res  =  Block  .  createBlock  (  v  .  n  ,  v  .  tag  )  ; 
for  (  int   i  =  0  ;  i  <  v  .  n  ;  i  ++  )  { 
res  .  set  (  i  ,  v  .  elements  [  i  ]  )  ; 
} 
return   Value  .  createFromBlock  (  res  )  ; 
} 






public   static   Value   returnDoubles  (  final   Values   d  )  { 
assert   d  !=  null  :  "null d"  ; 
final   Block   res  =  Block  .  createDoubleArray  (  d  .  n  )  ; 
for  (  int   i  =  0  ;  i  <  d  .  n  ;  i  ++  )  { 
res  .  setDouble  (  i  ,  d  .  elements  [  i  ]  .  asBlock  (  )  .  asDouble  (  )  )  ; 
} 
return   Value  .  createFromBlock  (  res  )  ; 
} 






public   static   Closure   createClosureVars  (  final   int   index  ,  final   int   n  )  { 
return   new   Closure  (  index  ,  n  )  ; 
} 







public   static   Closure   foldClosure1  (  final   Value   p1  ,  final   Closure   c  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   c  !=  null  :  "null c"  ; 
c  .  elements  [  c  .  next  --  ]  =  p1  ; 
return   c  ; 
} 








public   static   Closure   foldClosure2  (  final   Value   p1  ,  final   Value   p2  ,  final   Closure   c  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   c  !=  null  :  "null c"  ; 
c  .  elements  [  c  .  next  --  ]  =  p2  ; 
c  .  elements  [  c  .  next  --  ]  =  p1  ; 
return   c  ; 
} 









public   static   Closure   foldClosure3  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Closure   c  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   c  !=  null  :  "null c"  ; 
c  .  elements  [  c  .  next  --  ]  =  p3  ; 
c  .  elements  [  c  .  next  --  ]  =  p2  ; 
c  .  elements  [  c  .  next  --  ]  =  p1  ; 
return   c  ; 
} 










public   static   Closure   foldClosure4  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   p4  ,  final   Closure   c  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   p4  !=  null  :  "null p4"  ; 
assert   c  !=  null  :  "null c"  ; 
c  .  elements  [  c  .  next  --  ]  =  p4  ; 
c  .  elements  [  c  .  next  --  ]  =  p3  ; 
c  .  elements  [  c  .  next  --  ]  =  p2  ; 
c  .  elements  [  c  .  next  --  ]  =  p1  ; 
return   c  ; 
} 







public   static   Value   returnClosure  (  final   Block   closure  ,  final   Closure   c  )  { 
assert   closure  !=  null  :  "null closure"  ; 
assert   c  !=  null  :  "null c"  ; 
final   Block   res  =  closure  ; 
for  (  int   i  =  0  ;  i  <  c  .  n  ;  i  ++  )  { 
res  .  set  (  c  .  index  +  i  ,  c  .  elements  [  i  ]  )  ; 
} 
return   Value  .  createFromBlock  (  res  )  ; 
} 









public   final   Value   apply1  (  final   Value   p1  ,  final   Value   closure  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   closure  !=  null  :  "null closure"  ; 
final   Application   app  =  new   Application  (  this  ,  closure  ,  1  )  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   returnApplication  (  app  )  ; 
} 










public   final   Value   apply2  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   closure  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   closure  !=  null  :  "null closure"  ; 
final   Application   app  =  new   Application  (  this  ,  closure  ,  2  )  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   returnApplication  (  app  )  ; 
} 











public   final   Value   apply3  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   closure  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   closure  !=  null  :  "null closure"  ; 
final   Application   app  =  new   Application  (  this  ,  closure  ,  3  )  ; 
app  .  params  [  app  .  next  --  ]  =  p3  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   returnApplication  (  app  )  ; 
} 












public   final   Value   apply4  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   p4  ,  final   Value   closure  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   p4  !=  null  :  "null p4"  ; 
assert   closure  !=  null  :  "null closure"  ; 
final   Application   app  =  new   Application  (  this  ,  closure  ,  4  )  ; 
app  .  params  [  app  .  next  --  ]  =  p4  ; 
app  .  params  [  app  .  next  --  ]  =  p3  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   returnApplication  (  app  )  ; 
} 






public   final   Application   createApplication  (  final   Value   closure  ,  final   int   n  )  { 
assert   closure  !=  null  :  "null closure"  ; 
return   new   Application  (  this  ,  closure  ,  n  )  ; 
} 







public   static   Application   foldApplication1  (  final   Value   p1  ,  final   Application   app  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   app  !=  null  :  "null app"  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   app  ; 
} 








public   static   Application   foldApplication2  (  final   Value   p1  ,  final   Value   p2  ,  final   Application   app  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   app  !=  null  :  "null app"  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   app  ; 
} 









public   static   Application   foldApplication3  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Application   app  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   app  !=  null  :  "null app"  ; 
app  .  params  [  app  .  next  --  ]  =  p3  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   app  ; 
} 










public   static   Application   foldApplication4  (  final   Value   p1  ,  final   Value   p2  ,  final   Value   p3  ,  final   Value   p4  ,  final   Application   app  )  { 
assert   p1  !=  null  :  "null p1"  ; 
assert   p2  !=  null  :  "null p2"  ; 
assert   p3  !=  null  :  "null p3"  ; 
assert   p4  !=  null  :  "null p4"  ; 
assert   app  !=  null  :  "null app"  ; 
app  .  params  [  app  .  next  --  ]  =  p4  ; 
app  .  params  [  app  .  next  --  ]  =  p3  ; 
app  .  params  [  app  .  next  --  ]  =  p2  ; 
app  .  params  [  app  .  next  --  ]  =  p1  ; 
return   app  ; 
} 








public   static   Value   returnApplication  (  final   Application   app  )  throws   Fail  .  Exception  ,  Fatal  .  Exception  ,  FalseExit  ,  Error  { 
assert   app  !=  null  :  "null app"  ; 
final   AbstractNativeRunner   that  =  app  .  that  ; 
try  { 
final   int   code  =  app  .  closure  .  asBlock  (  )  .  getCode  (  )  ; 
if  (  code  >=  0  )  { 
final   Block   env  =  (  Block  )  app  .  closure  .  asBlock  (  )  .  asCustom  (  )  ; 
final   Method   meth  =  that  .  getClosure  (  code  )  ; 
final   Object  [  ]  objs  ; 
if  (  meth  .  getParameterTypes  (  )  .  length  ==  2  )  { 
objs  =  new   Object  [  ]  {  that  ,  app  .  params  [  0  ]  }  ; 
}  else  { 
objs  =  new   Object  [  ]  {  that  ,  app  .  params  [  0  ]  ,  Value  .  createFromBlock  (  env  )  }  ; 
} 
final   Value   tmp  =  (  Value  )  meth  .  invoke  (  null  ,  objs  )  ; 
if  (  app  .  n  ==  1  )  { 
return   tmp  ; 
}  else  { 
final   Application   newApp  =  new   Application  (  app  .  that  ,  tmp  ,  app  .  n  -  1  )  ; 
for  (  int   i  =  1  ;  i  <  app  .  n  ;  i  ++  )  { 
newApp  .  params  [  i  -  1  ]  =  app  .  params  [  i  ]  ; 
} 
return   returnApplication  (  newApp  )  ; 
} 
}  else   if  (  code  ==  -  1  )  { 
final   Block   env  =  (  Block  )  app  .  closure  .  asBlock  (  )  .  asCustom  (  )  ; 
final   Method   meth  =  that  .  getClosure  (  app  .  closure  .  asBlock  (  )  .  get  (  2  )  .  asLong  (  )  )  ; 
final   int   arity  =  meth  .  getParameterTypes  (  )  .  length  -  1  ; 
final   Block   bl  =  app  .  params  [  0  ]  .  asBlock  (  )  ; 
final   Object  [  ]  objs  =  new   Object  [  arity  +  1  ]  ; 
objs  [  0  ]  =  that  ; 
objs  [  arity  ]  =  env  ==  null  ?  null  :  Value  .  createFromBlock  (  env  )  ; 
final   int   len  =  bl  .  sizeValues  (  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
objs  [  i  +  1  ]  =  bl  .  get  (  i  )  ; 
} 
final   Value   tmp  =  (  Value  )  meth  .  invoke  (  null  ,  objs  )  ; 
if  (  app  .  n  ==  1  )  { 
return   tmp  ; 
}  else  { 
final   Application   newApp  =  new   Application  (  app  .  that  ,  tmp  ,  app  .  n  -  1  )  ; 
for  (  int   i  =  1  ;  i  <  app  .  n  ;  i  ++  )  { 
newApp  .  params  [  i  -  1  ]  =  app  .  params  [  i  ]  ; 
} 
return   returnApplication  (  newApp  )  ; 
} 
}  else  { 
final   Block   env  =  (  Block  )  app  .  closure  .  asBlock  (  )  .  asCustom  (  )  ; 
final   Method   meth  =  that  .  getClosure  (  app  .  closure  .  asBlock  (  )  .  get  (  2  )  .  asLong  (  )  )  ; 
final   int   funArity  =  meth  .  getParameterTypes  (  )  .  length  -  1  ; 
final   int   remArity  =  -  (  code  +  1  )  ; 
if  (  remArity  -  app  .  n  <=  0  )  { 
final   Object  [  ]  objs  =  new   Object  [  funArity  +  1  ]  ; 
objs  [  0  ]  =  that  ; 
objs  [  funArity  ]  =  env  ==  null  ?  null  :  Value  .  createFromBlock  (  env  )  ; 
if  (  env  !=  app  .  closure  .  asBlock  (  )  )  { 
final   int   len  =  app  .  closure  .  asBlock  (  )  .  sizeValues  (  )  ; 
for  (  int   i  =  3  ;  i  <  len  ;  i  ++  )  { 
objs  [  i  -  2  ]  =  app  .  closure  .  asBlock  (  )  .  get  (  i  )  ; 
} 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  app  .  n  ,  remArity  )  ;  i  ++  )  { 
objs  [  i  +  len  -  2  ]  =  app  .  params  [  i  ]  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  app  .  n  ,  remArity  )  ;  i  ++  )  { 
objs  [  i  +  1  ]  =  app  .  params  [  i  ]  ; 
} 
} 
final   Value   tmp  =  (  Value  )  meth  .  invoke  (  null  ,  objs  )  ; 
if  (  remArity  -  app  .  n  ==  0  )  { 
return   tmp  ; 
}  else  { 
final   int   newN  =  app  .  n  -  remArity  ; 
final   Application   newApp  =  new   Application  (  app  .  that  ,  tmp  ,  newN  )  ; 
for  (  int   i  =  0  ;  i  <  newN  ;  i  ++  )  { 
newApp  .  params  [  i  ]  =  app  .  params  [  remArity  +  i  ]  ; 
} 
return   returnApplication  (  newApp  )  ; 
} 
}  else  { 
final   int   newArity  =  remArity  -  app  .  n  ; 
final   int   blockSize  =  env  !=  app  .  closure  .  asBlock  (  )  ?  app  .  closure  .  asBlock  (  )  .  sizeValues  (  )  +  app  .  n  :  3  +  app  .  n  ; 
final   Block   newBlock  =  Block  .  createClosure  (  blockSize  )  ; 
final   int   newCode  =  -  newArity  -  1  ; 
newBlock  .  setCustom  (  env  )  ; 
newBlock  .  setCode  (  newCode  )  ; 
newBlock  .  set  (  1  ,  Value  .  createFromLong  (  newArity  )  )  ; 
newBlock  .  set  (  2  ,  app  .  closure  .  asBlock  (  )  .  get  (  2  )  )  ; 
if  (  env  !=  app  .  closure  .  asBlock  (  )  )  { 
final   int   len  =  app  .  closure  .  asBlock  (  )  .  sizeValues  (  )  ; 
for  (  int   i  =  3  ;  i  <  len  ;  i  ++  )  { 
newBlock  .  set  (  i  ,  app  .  closure  .  asBlock  (  )  .  get  (  i  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  app  .  n  ;  i  ++  )  { 
newBlock  .  set  (  i  +  len  ,  app  .  params  [  i  ]  )  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  app  .  n  ;  i  ++  )  { 
newBlock  .  set  (  3  +  i  ,  app  .  params  [  i  ]  )  ; 
} 
} 
return   Value  .  createFromBlock  (  newBlock  )  ; 
} 
} 
}  catch  (  final   java  .  lang  .  reflect  .  InvocationTargetException   ite  )  { 
final   Throwable   te  =  ite  .  getTargetException  (  )  ; 
if  (  te   instanceof   Fail  .  Exception  )  { 
throw  (  Fail  .  Exception  )  te  ; 
}  else   if  (  te   instanceof   Fatal  .  Exception  )  { 
throw  (  Fatal  .  Exception  )  te  ; 
}  else   if  (  te   instanceof   FalseExit  )  { 
throw  (  FalseExit  )  te  ; 
}  else  { 
Fatal  .  raise  (  "error in apply: "  +  te  .  toString  (  )  )  ; 
return   null  ; 
} 
}  catch  (  final   IllegalAccessException   iae  )  { 
Fatal  .  raise  (  "error in apply: illegal access exception"  )  ; 
return   null  ; 
} 
} 





private   static   final   class   Application  { 


private   AbstractNativeRunner   that  ; 


private   final   int   n  ; 


private   int   next  ; 


private   final   Value  [  ]  params  ; 


private   final   Value   closure  ; 







private   Application  (  final   AbstractNativeRunner   that  ,  final   Value   closure  ,  final   int   n  )  { 
assert   that  !=  null  :  "null that"  ; 
assert   closure  !=  null  :  "null closure"  ; 
assert   n  >  0  :  "n should be > 0"  ; 
this  .  that  =  that  ; 
this  .  n  =  n  ; 
this  .  next  =  n  -  1  ; 
this  .  params  =  new   Value  [  n  ]  ; 
this  .  closure  =  closure  ; 
} 
} 




private   static   final   class   Values  { 


private   final   int   tag  ; 


private   final   int   n  ; 


private   int   next  ; 


private   final   Value  [  ]  elements  ; 






private   Values  (  final   int   tag  ,  final   int   n  )  { 
assert   n  >  0  :  "n should be > 0"  ; 
this  .  tag  =  tag  ; 
this  .  n  =  n  ; 
this  .  next  =  n  -  1  ; 
this  .  elements  =  new   Value  [  n  ]  ; 
} 
} 




private   static   final   class   Closure  { 


private   final   int   index  ; 


private   final   int   n  ; 


private   final   Value  [  ]  elements  ; 


private   int   next  ; 






private   Closure  (  final   int   index  ,  final   int   n  )  { 
assert   index  >  0  :  "index should be > 0"  ; 
assert   n  >=  0  :  "n should be >= 0"  ; 
this  .  index  =  index  ; 
this  .  n  =  n  ; 
this  .  elements  =  new   Value  [  n  ]  ; 
this  .  next  =  n  -  1  ; 
} 
} 
} 

