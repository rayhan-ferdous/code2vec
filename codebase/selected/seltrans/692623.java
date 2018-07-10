package   jlib  .  log  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Vector  ; 
import   jlib  .  misc  .  StopWatch  ; 
import   jlib  .  xml  .  Tag  ; 











public   class   Log  { 

private   static   CallStackExclusion   ms_CallStackExclusion  =  null  ; 

private   static   StopWatch   ms_processStopWatch  =  new   StopWatch  (  )  ; 

Log  (  )  { 
} 

public   static   long   getRunningTime_ms  (  )  { 
return   ms_processStopWatch  .  getElapsedTime  (  )  ; 
} 


















public   static   synchronized   Tag   open  (  String   csChannel  ,  String   csConfigFile  ,  String   csRunId  ,  String   csProduct  )  { 
Tag   tagConfig  =  Tag  .  createFromFile  (  csConfigFile  )  ; 
setProduct  (  csChannel  ,  csProduct  )  ; 
return   open  (  csChannel  ,  csRunId  ,  tagConfig  )  ; 
} 










public   static   synchronized   Tag   open  (  String   csChannel  ,  String   csConfigFile  )  { 
Tag   tagConfig  =  Tag  .  createFromFile  (  csConfigFile  )  ; 
return   open  (  csChannel  ,  null  ,  tagConfig  )  ; 
} 










public   static   synchronized   Tag   open  (  String   csChannel  ,  InputStream   isConfigFile  )  { 
Tag   tagConfig  =  Tag  .  createFromStream  (  isConfigFile  )  ; 
return   open  (  csChannel  ,  null  ,  tagConfig  )  ; 
} 








public   static   synchronized   Tag   open  (  String   csConfigFile  )  { 
Tag   tagConfig  =  Tag  .  createFromFile  (  csConfigFile  )  ; 
Tag   tag  =  open  (  null  ,  null  ,  tagConfig  )  ; 
return   tag  ; 
} 








public   static   synchronized   Tag   open  (  InputStream   isConfigFile  )  { 
Tag   tagConfig  =  Tag  .  createFromStream  (  isConfigFile  )  ; 
Tag   tag  =  open  (  null  ,  null  ,  tagConfig  )  ; 
return   tag  ; 
} 











public   static   synchronized   void   setRunId  (  String   csChannel  ,  String   csRunId  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  ||  csChannel  ==  null  )  logCenter  .  setRunId  (  csRunId  )  ; 
} 
} 
} 











public   static   synchronized   void   setRuntimeId  (  String   csChannel  ,  String   csRuntimeId  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  ||  csChannel  ==  null  )  logCenter  .  setRuntimeId  (  csRuntimeId  )  ; 
} 
} 
} 






public   static   synchronized   String   getRunId  (  String   csChannel  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  )  return   logCenter  .  getRunId  (  )  ; 
} 
} 
return   null  ; 
} 






public   static   synchronized   String   getRuntimeId  (  String   csChannel  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  )  return   logCenter  .  getRuntimeId  (  )  ; 
} 
} 
return   null  ; 
} 







public   static   synchronized   void   setProduct  (  String   csChannel  ,  String   csProduct  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  ||  csChannel  ==  null  )  logCenter  .  setProduct  (  csProduct  )  ; 
} 
} 
} 






public   static   synchronized   String   getProduct  (  String   csChannel  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  )  return   logCenter  .  getProduct  (  )  ; 
} 
} 
return   null  ; 
} 







public   static   synchronized   void   setProcess  (  String   csChannel  ,  String   csProcess  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  ||  csChannel  ==  null  )  logCenter  .  setProcess  (  csProcess  )  ; 
} 
} 
} 






public   static   synchronized   String   getProcess  (  String   csChannel  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getChannel  (  )  .  equals  (  csChannel  )  )  return   logCenter  .  getProcess  (  )  ; 
} 
} 
return   null  ; 
} 


















private   static   synchronized   Tag   open  (  String   csChannel  ,  String   csRunId  ,  Tag   tagConfig  )  { 
LogFlowStd  .  declare  (  )  ; 
if  (  tagConfig  !=  null  )  { 
LogCenters   logCenters  =  new   LogCenters  (  )  ; 
boolean   b  =  logCenters  .  loadDefinition  (  csChannel  ,  tagConfig  ,  null  )  ; 
if  (  b  )  { 
Tag   tagSettings  =  tagConfig  .  getChild  (  "Settings"  )  ; 
fillCallStack  (  tagSettings  )  ; 
} 
if  (  csChannel  ==  null  )  { 
Vector  <  String  >  arrChannels  =  new   Vector  <  String  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  logCenters  .  getNbLogCenterloader  (  )  ;  i  ++  )  { 
LogCenterLoader   loader  =  logCenters  .  getLogCenterloader  (  i  )  ; 
String   ch  =  loader  .  getChannel  (  )  ; 
if  (  !  arrChannels  .  contains  (  ch  )  )  { 
arrChannels  .  add  (  ch  )  ; 
} 
} 
for  (  String   cs  :  arrChannels  )  { 
setRunId  (  cs  ,  csRunId  )  ; 
} 
}  else  { 
setRunId  (  csChannel  ,  csRunId  )  ; 
} 
} 
return   tagConfig  ; 
} 

public   static   synchronized   void   close  (  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   LogCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
LogCenter  .  close  (  )  ; 
} 
} 
} 







static   synchronized   void   registerLogCenter  (  LogCenter   logCenter  )  { 
boolean   b  =  logCenter  .  doOpen  (  )  ; 
if  (  b  )  { 
if  (  m_arrLogCenter  ==  null  )  m_arrLogCenter  =  new   ArrayList  <  LogCenter  >  (  )  ; 
m_arrLogCenter  .  add  (  logCenter  )  ; 
} 
} 

public   static   synchronized   LogCenterPluginConsole   getLogCenterPluginConsole  (  )  { 
if  (  m_arrLogCenter  !=  null  )  { 
for  (  int   n  =  0  ;  n  <  m_arrLogCenter  .  size  (  )  ;  n  ++  )  { 
LogCenter   logCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
if  (  logCenter  .  getType  (  )  .  equalsIgnoreCase  (  "LogCenterPluginConsole"  )  )  return  (  LogCenterPluginConsole  )  logCenter  ; 
} 
} 
return   null  ; 
} 





static   synchronized   void   unregisterLogCenter  (  LogCenter   logCenter  )  { 
logCenter  .  closeLogCenter  (  )  ; 
} 

private   static   ArrayList  <  LogCenter  >  m_arrLogCenter  =  null  ; 

















static   synchronized   void   sendLog  (  LogParams   logParams  )  { 
String   csOut  =  ""  ; 
if  (  m_arrLogCenter  !=  null  )  { 
int   nNbLogCenter  =  m_arrLogCenter  .  size  (  )  ; 
for  (  int   n  =  0  ;  n  <  nNbLogCenter  ;  n  ++  )  { 
LogCenter   LogCenter  =  m_arrLogCenter  .  get  (  n  )  ; 
LogCenter  .  output  (  logParams  )  ; 
} 
} 
} 






static   void   fillCallStack  (  Tag   tagSettings  )  { 
if  (  tagSettings  !=  null  )  { 
boolean   bFillCallStack  =  tagSettings  .  getValAsBoolean  (  "GetCallerLocation"  )  ; 
if  (  bFillCallStack  )  { 
ms_CallStackExclusion  =  new   CallStackExclusion  (  )  ; 
ms_CallStackExclusion  .  fillExcluded  (  tagSettings  )  ; 
} 
} 
} 













public   static   void   log  (  String   csChannel  ,  LogEvent   logEvent  ,  String   csMessage  )  { 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 


















public   static   void   log  (  String   csChannel  ,  LogEvent   logEvent  ,  String   csMessage  ,  String   csRunId  ,  String   csRuntimeId  )  { 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  ,  csRunId  ,  csRuntimeId  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   log  (  String   csChannel  ,  LogFlow   logFlow  ,  LogEvent   logEvent  ,  String   csMessage  )  { 
logEvent  .  setLogFlow  (  logFlow  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 

public   static   void   log  (  String   csChannel  ,  LogFlow   logFlow  ,  LogEvent   logEvent  ,  LogLevel   logLevel  ,  String   csMessage  )  { 
logEvent  .  setLogFlow  (  logFlow  )  ; 
logEvent  .  setLogLevel  (  logLevel  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 









public   static   void   logSystem  (  String   csChannel  ,  LogEvent   logEvent  ,  String   csMessage  )  { 
logEvent  .  setLogFlow  (  LogFlowStd  .  System  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 









public   static   void   logMonitoring  (  String   csChannel  ,  LogEvent   logEvent  ,  String   csMessage  )  { 
logEvent  .  setLogFlow  (  LogFlowStd  .  Monitoring  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 










public   static   void   logMonitoring  (  String   csChannel  ,  LogEvent   logEvent  ,  LogLevel   logLevel  ,  String   csMessage  )  { 
logEvent  .  setLogLevel  (  logLevel  )  ; 
logEvent  .  setLogFlow  (  LogFlowStd  .  Monitoring  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 









public   static   void   logTrace  (  String   csChannel  ,  LogEvent   logEvent  ,  String   csMessage  )  { 
logEvent  .  setLogFlow  (  LogFlowStd  .  Trace  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 










public   static   void   logTrace  (  String   csChannel  ,  LogEvent   logEvent  ,  LogLevel   logLevel  ,  String   csMessage  )  { 
logEvent  .  setLogLevel  (  logLevel  )  ; 
logEvent  .  setLogFlow  (  LogFlowStd  .  Trace  )  ; 
LogParams   logParams  =  new   LogParams  (  csChannel  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   logCritical  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  Critical  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "CriticalEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 

public   static   void   logWholeCallStack  (  String   csMessage  ,  LogLevel   level  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  level  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "CallStackEvent"  )  ; 
StackTraceElement  [  ]  tStack  =  fillWholeCallStack  (  )  ; 
String   cs  =  formatCallStack  (  tStack  ,  null  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  +  cs  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 

public   static   String   logCallStack  (  String   csMessage  ,  LogLevel   level  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  level  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "CallStackEvent"  )  ; 
StackTraceElement  [  ]  tStack  =  fillWholeCallStack  (  )  ; 
String   cs  =  formatCallStack  (  tStack  ,  ms_CallStackExclusion  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  +  cs  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
return   cs  ; 
} 

private   static   String   formatCallStack  (  StackTraceElement  [  ]  tStack  ,  CallStackExclusion   callStackExclusion  )  { 
String   csText  =  ""  ; 
if  (  tStack  ==  null  )  return   csText  ; 
if  (  callStackExclusion  ==  null  )  { 
for  (  int   n  =  0  ;  n  <  tStack  .  length  ;  n  ++  )  { 
StackTraceElement   stackElem  =  tStack  [  n  ]  ; 
String   csFile  =  stackElem  .  getFileName  (  )  ; 
if  (  csFile  !=  null  )  { 
String   cs  =  csFile  +  "("  +  stackElem  .  getLineNumber  (  )  +  ")"  ; 
csText  +=  cs  +  " / "  ; 
} 
} 
}  else  { 
for  (  int   n  =  0  ;  n  <  tStack  .  length  ;  n  ++  )  { 
StackTraceElement   stackElem  =  tStack  [  n  ]  ; 
String   csClassName  =  stackElem  .  getClassName  (  )  ; 
if  (  callStackExclusion  .  doNotContains  (  csClassName  )  )  { 
String   csFile  =  stackElem  .  getFileName  (  )  ; 
if  (  csFile  !=  null  )  { 
String   cs  =  csFile  +  "("  +  stackElem  .  getLineNumber  (  )  +  ")"  ; 
csText  +=  cs  +  " / "  ; 
} 
} 
} 
} 
return   csText  ; 
} 

private   static   String   formatFilteredWholeCallStack  (  StackTraceElement  [  ]  tStack  )  { 
String   csText  =  ""  ; 
if  (  tStack  ==  null  )  return   csText  ; 
for  (  int   n  =  0  ;  n  <  tStack  .  length  ;  n  ++  )  { 
StackTraceElement   stackElem  =  tStack  [  n  ]  ; 
if  (  stackElem  .  getFileName  (  )  !=  null  )  { 
String   cs  =  stackElem  .  getFileName  (  )  +  "("  +  stackElem  .  getLineNumber  (  )  +  ")"  ; 
csText  +=  cs  +  " / "  ; 
} 
} 
return   csText  ; 
} 

private   static   StackTraceElement  [  ]  fillWholeCallStack  (  )  { 
Throwable   th  =  new   Throwable  (  )  ; 
StackTraceElement   tStack  [  ]  =  th  .  getStackTrace  (  )  ; 
return   tStack  ; 
} 















public   static   void   logImportant  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  Important  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "ImportantEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   logNormal  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  Normal  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "NormalEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   logVerbose  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  Verbose  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "VerboseEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   logDebug  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  Debug  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "DebugEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 















public   static   void   logFineDebug  (  String   csMessage  )  { 
LogEvent   logEvent  =  new   LogEvent  (  LogEventType  .  Remark  ,  LogFlowStd  .  Any  ,  LogLevel  .  FineDebug  ,  null  ,  null  )  ; 
logEvent  .  setName  (  "FineDebugEvent"  )  ; 
LogParams   logParams  =  new   LogParams  (  null  ,  logEvent  ,  csMessage  )  ; 
if  (  ms_CallStackExclusion  !=  null  )  logParams  .  fillAppCallerLocation  (  ms_CallStackExclusion  )  ; 
sendLog  (  logParams  )  ; 
} 

public   static   int   incCounter  (  String   csName  )  { 
return   0  ; 
} 

public   static   int   decCounter  (  String   csName  )  { 
return   0  ; 
} 

public   static   void   resetCounter  (  String   csName  )  { 
} 
} 

