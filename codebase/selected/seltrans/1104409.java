package   tool  .  dtf4j  .  client  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 





class   OutputTracer   extends   Thread  { 


private   static   Logger   logger_  =  Logger  .  getLogger  (  "DTFLogger"  )  ; 


private   static   String   VM_CRASH_TEXT  =  "# HotSpot Virtual Machine Error"  ; 


private   BufferedReader   stream_  ; 


private   PrintWriter   trace_  ; 


private   boolean   showOutput_  ; 


private   boolean   thread_running_  ; 


private   boolean   interrupt_called_  =  false  ; 


private   File   traceFile_  ; 


private   FileOutputStream   fileOut_  ; 


private   String   pattern_  ; 


private   ProcessExecutor   execProcess_  ; 


private   String   name_  ; 


private   boolean   fileLogging_  ; 


private   final   Object   syncObj_  =  new   Object  (  )  ; 


private   boolean   done_  =  false  ; 





OutputTracer  (  String   name  ,  ProcessExecutor   execProcess  ,  InputStream   s  ,  boolean   mode  ,  boolean   fileLogging  ,  File   traceFile  ,  String   pattern  )  throws   IOException  { 
fileLogging_  =  fileLogging  ; 
name_  =  name  ; 
execProcess_  =  execProcess  ; 
pattern_  =  pattern  ; 
traceFile_  =  traceFile  ; 
stream_  =  new   BufferedReader  (  new   InputStreamReader  (  s  )  )  ; 
fileOut_  =  new   FileOutputStream  (  traceFile  )  ; 
trace_  =  new   PrintWriter  (  fileOut_  )  ; 
showOutput_  =  mode  ; 
thread_running_  =  true  ; 
if  (  !  fileLogging_  )  { 
trace_  .  println  (  "Logging to file disabled on client."  )  ; 
trace_  .  flush  (  )  ; 
} 
} 




public   void   run  (  )  { 
try  { 
String   nextLine  =  null  ; 
while  (  true  )  { 
if  (  stream_  ==  null  )  return  ; 
if  (  stream_  .  ready  (  )  )  { 
nextLine  =  stream_  .  readLine  (  )  ; 
if  (  nextLine  .  startsWith  (  VM_CRASH_TEXT  )  )  execProcess_  .  vmcrash  (  )  ; 
if  (  nextLine  !=  null  )  { 
if  (  fileLogging_  )  { 
trace_  .  println  (  nextLine  )  ; 
trace_  .  flush  (  )  ; 
} 
if  (  !  (  pattern_  .  equals  (  ""  )  )  &&  (  nextLine  .  indexOf  (  pattern_  )  >=  0  )  )  { 
fileOut_  .  getFD  (  )  .  sync  (  )  ; 
logger_  .  log  (  Level  .  FINE  ,  "Pattern found: "  +  pattern_  )  ; 
execProcess_  .  patternMatchFound  (  traceFile_  )  ; 
} 
if  (  showOutput_  )  logger_  .  log  (  Level  .  FINE  ,  name_  +  ": "  +  nextLine  )  ; 
}  else  { 
return  ; 
} 
}  else  { 
synchronized  (  syncObj_  )  { 
if  (  !  thread_running_  )  return  ; 
} 
try  { 
Thread  .  sleep  (  500  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 
}  catch  (  IOException   ex  )  { 
}  finally  { 
synchronized  (  syncObj_  )  { 
done_  =  true  ; 
syncObj_  .  notifyAll  (  )  ; 
} 
} 
} 




public   void   interrupt  (  )  { 
synchronized  (  syncObj_  )  { 
if  (  interrupt_called_  )  { 
logger_  .  log  (  Level  .  FINE  ,  "OutputTracer already interrupted: "  +  this  )  ; 
return  ; 
} 
interrupt_called_  =  true  ; 
} 
try  { 
stream_  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
} 
synchronized  (  syncObj_  )  { 
thread_running_  =  false  ; 
while  (  !  done_  )  { 
try  { 
syncObj_  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 
trace_  .  flush  (  )  ; 
trace_  .  close  (  )  ; 
} 
} 

