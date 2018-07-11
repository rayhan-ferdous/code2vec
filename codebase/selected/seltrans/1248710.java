package   oracle  .  toplink  .  essentials  .  logging  ; 

import   java  .  util  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  io  .  *  ; 
import   oracle  .  toplink  .  essentials  .  sessions  .  Session  ; 
import   oracle  .  toplink  .  essentials  .  internal  .  databaseaccess  .  Accessor  ; 
import   oracle  .  toplink  .  essentials  .  internal  .  localization  .  *  ; 
import   oracle  .  toplink  .  essentials  .  exceptions  .  *  ; 
import   oracle  .  toplink  .  essentials  .  internal  .  sessions  .  AbstractSession  ; 













public   abstract   class   AbstractSessionLog   implements   SessionLog  ,  java  .  lang  .  Cloneable  { 




protected   int   level  ; 




protected   static   SessionLog   defaultLog  ; 




protected   Session   session  ; 




protected   String   sessionType  ; 




protected   String   sessionHashCode  ; 




protected   static   String   SEVERE_PREFIX  =  null  ; 




protected   static   String   WARNING_PREFIX  =  null  ; 




protected   static   String   INFO_PREFIX  =  null  ; 




protected   static   String   CONFIG_PREFIX  =  null  ; 




protected   static   String   FINE_PREFIX  =  null  ; 




protected   static   String   FINER_PREFIX  =  null  ; 




protected   static   String   FINEST_PREFIX  =  null  ; 




protected   static   String   TOPLINK_PREFIX  =  null  ; 




protected   static   final   String   CONNECTION_STRING  =  "Connection"  ; 




protected   static   final   String   THREAD_STRING  =  "Thread"  ; 




protected   Writer   writer  ; 




protected   DateFormat   dateFormat  ; 






protected   Boolean   shouldLogExceptionStackTrace  ; 






protected   Boolean   shouldPrintDate  ; 






protected   Boolean   shouldPrintThread  ; 






protected   Boolean   shouldPrintSession  ; 






protected   Boolean   shouldPrintConnection  ; 





public   AbstractSessionLog  (  )  { 
this  .  writer  =  new   PrintWriter  (  System  .  out  )  ; 
} 










public   int   getLevel  (  )  { 
return   getLevel  (  null  )  ; 
} 












public   int   getLevel  (  String   category  )  { 
return   level  ; 
} 










public   void   setLevel  (  int   level  )  { 
setLevel  (  level  ,  null  )  ; 
} 











public   void   setLevel  (  int   level  ,  String   category  )  { 
this  .  level  =  level  ; 
} 













public   boolean   shouldLog  (  int   level  )  { 
return   shouldLog  (  level  ,  null  )  ; 
} 















public   boolean   shouldLog  (  int   level  ,  String   category  )  { 
return  (  this  .  level  <=  level  )  &&  !  isOff  (  )  ; 
} 











public   static   SessionLog   getLog  (  )  { 
if  (  defaultLog  ==  null  )  { 
defaultLog  =  new   DefaultSessionLog  (  )  ; 
} 
return   defaultLog  ; 
} 










public   static   void   setLog  (  SessionLog   sessionLog  )  { 
defaultLog  =  sessionLog  ; 
defaultLog  .  setSession  (  null  )  ; 
} 










public   Session   getSession  (  )  { 
return   this  .  session  ; 
} 










public   void   setSession  (  Session   session  )  { 
if  (  this  .  session  ==  null  )  { 
this  .  session  =  session  ; 
buildSessionType  (  )  ; 
buildSessionHashCode  (  )  ; 
} 
} 













public   void   log  (  int   level  ,  String   message  )  { 
if  (  !  shouldLog  (  level  )  )  { 
return  ; 
} 
log  (  level  ,  message  ,  (  Object  [  ]  )  null  ,  false  )  ; 
} 














public   void   log  (  int   level  ,  String   message  ,  Object   param  )  { 
if  (  !  shouldLog  (  level  )  )  { 
return  ; 
} 
log  (  level  ,  message  ,  new   Object  [  ]  {  param  }  )  ; 
} 
















public   void   log  (  int   level  ,  String   message  ,  Object   param1  ,  Object   param2  )  { 
if  (  !  shouldLog  (  level  )  )  { 
return  ; 
} 
log  (  level  ,  message  ,  new   Object  [  ]  {  param1  ,  param2  }  )  ; 
} 


















public   void   log  (  int   level  ,  String   message  ,  Object   param1  ,  Object   param2  ,  Object   param3  )  { 
if  (  !  shouldLog  (  level  )  )  { 
return  ; 
} 
log  (  level  ,  message  ,  new   Object  [  ]  {  param1  ,  param2  ,  param3  }  )  ; 
} 














public   void   log  (  int   level  ,  String   message  ,  Object  [  ]  params  )  { 
log  (  level  ,  message  ,  params  ,  true  )  ; 
} 
















public   void   log  (  int   level  ,  String   message  ,  Object  [  ]  params  ,  boolean   shouldTranslate  )  { 
if  (  !  shouldLog  (  level  )  )  { 
return  ; 
} 
log  (  new   SessionLogEntry  (  level  ,  null  ,  message  ,  params  ,  null  ,  shouldTranslate  )  )  ; 
} 










public   abstract   void   log  (  SessionLogEntry   sessionLogEntry  )  ; 





public   boolean   shouldPrintSession  (  )  { 
return  (  shouldPrintSession  ==  null  )  ||  shouldPrintSession  .  booleanValue  (  )  ; 
} 





public   void   setShouldPrintSession  (  boolean   shouldPrintSession  )  { 
if  (  shouldPrintSession  )  { 
this  .  shouldPrintSession  =  Boolean  .  TRUE  ; 
}  else  { 
this  .  shouldPrintSession  =  Boolean  .  FALSE  ; 
} 
} 




public   boolean   shouldPrintConnection  (  )  { 
return  (  shouldPrintConnection  ==  null  )  ||  shouldPrintConnection  .  booleanValue  (  )  ; 
} 




public   void   setShouldPrintConnection  (  boolean   shouldPrintConnection  )  { 
if  (  shouldPrintConnection  )  { 
this  .  shouldPrintConnection  =  Boolean  .  TRUE  ; 
}  else  { 
this  .  shouldPrintConnection  =  Boolean  .  FALSE  ; 
} 
} 





public   boolean   shouldLogExceptionStackTrace  (  )  { 
if  (  shouldLogExceptionStackTrace  ==  null  )  { 
return   getLevel  (  )  <=  FINER  ; 
}  else  { 
return   shouldLogExceptionStackTrace  .  booleanValue  (  )  ; 
} 
} 





public   void   setShouldLogExceptionStackTrace  (  boolean   shouldLogExceptionStackTrace  )  { 
if  (  shouldLogExceptionStackTrace  )  { 
this  .  shouldLogExceptionStackTrace  =  Boolean  .  TRUE  ; 
}  else  { 
this  .  shouldLogExceptionStackTrace  =  Boolean  .  FALSE  ; 
} 
} 




public   boolean   shouldPrintDate  (  )  { 
return  (  shouldPrintDate  ==  null  )  ||  (  shouldPrintDate  .  booleanValue  (  )  )  ; 
} 




public   void   setShouldPrintDate  (  boolean   shouldPrintDate  )  { 
if  (  shouldPrintDate  )  { 
this  .  shouldPrintDate  =  Boolean  .  TRUE  ; 
}  else  { 
this  .  shouldPrintDate  =  Boolean  .  FALSE  ; 
} 
} 





public   boolean   shouldPrintThread  (  )  { 
if  (  shouldPrintThread  ==  null  )  { 
return   getLevel  (  )  <=  FINE  ; 
}  else  { 
return   shouldPrintThread  .  booleanValue  (  )  ; 
} 
} 





public   void   setShouldPrintThread  (  boolean   shouldPrintThread  )  { 
if  (  shouldPrintThread  )  { 
this  .  shouldPrintThread  =  Boolean  .  TRUE  ; 
}  else  { 
this  .  shouldPrintThread  =  Boolean  .  FALSE  ; 
} 
} 










public   Writer   getWriter  (  )  { 
return   writer  ; 
} 










public   void   setWriter  (  Writer   writer  )  { 
this  .  writer  =  writer  ; 
} 






protected   DateFormat   buildDefaultDateFormat  (  )  { 
return   new   SimpleDateFormat  (  "yyyy.MM.dd hh:mm:ss.SSS"  )  ; 
} 






public   DateFormat   getDateFormat  (  )  { 
if  (  dateFormat  ==  null  )  { 
dateFormat  =  this  .  buildDefaultDateFormat  (  )  ; 
} 
return   dateFormat  ; 
} 





protected   String   getDateString  (  Date   date  )  { 
return   this  .  getDateFormat  (  )  .  format  (  date  )  ; 
} 




protected   String   getSupplementDetailString  (  SessionLogEntry   entry  )  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
if  (  shouldPrintDate  (  )  )  { 
writer  .  write  (  getDateString  (  entry  .  getDate  (  )  )  )  ; 
writer  .  write  (  "--"  )  ; 
} 
if  (  shouldPrintSession  (  )  &&  (  entry  .  getSession  (  )  !=  null  )  )  { 
writer  .  write  (  this  .  getSessionString  (  entry  .  getSession  (  )  )  )  ; 
writer  .  write  (  "--"  )  ; 
} 
if  (  shouldPrintConnection  (  )  &&  (  entry  .  getConnection  (  )  !=  null  )  )  { 
writer  .  write  (  this  .  getConnectionString  (  entry  .  getConnection  (  )  )  )  ; 
writer  .  write  (  "--"  )  ; 
} 
if  (  shouldPrintThread  (  )  )  { 
writer  .  write  (  this  .  getThreadString  (  entry  .  getThread  (  )  )  )  ; 
writer  .  write  (  "--"  )  ; 
} 
return   writer  .  toString  (  )  ; 
} 




protected   String   getSessionString  (  Session   session  )  { 
if  (  session  !=  null  )  { 
return  (  (  AbstractSession  )  session  )  .  getLogSessionString  (  )  ; 
}  else  { 
return   getSessionString  (  )  ; 
} 
} 




protected   void   buildSessionType  (  )  { 
if  (  session  !=  null  )  { 
sessionType  =  (  (  AbstractSession  )  session  )  .  getSessionTypeString  (  )  ; 
}  else  { 
sessionType  =  null  ; 
} 
} 




protected   void   buildSessionHashCode  (  )  { 
if  (  session  !=  null  )  { 
sessionHashCode  =  String  .  valueOf  (  System  .  identityHashCode  (  session  )  )  ; 
}  else  { 
sessionHashCode  =  null  ; 
} 
} 




protected   String   getSessionString  (  )  { 
return   sessionType  +  "("  +  sessionHashCode  +  ")"  ; 
} 




protected   String   getConnectionString  (  Accessor   connection  )  { 
if  (  connection  .  getDatasourceConnection  (  )  ==  null  )  { 
return   CONNECTION_STRING  +  "("  +  String  .  valueOf  (  System  .  identityHashCode  (  connection  )  )  +  ")"  ; 
}  else  { 
return   CONNECTION_STRING  +  "("  +  String  .  valueOf  (  System  .  identityHashCode  (  connection  .  getDatasourceConnection  (  )  )  )  +  ")"  ; 
} 
} 




protected   String   getThreadString  (  Thread   thread  )  { 
return   THREAD_STRING  +  "("  +  String  .  valueOf  (  thread  )  +  ")"  ; 
} 

protected   void   printPrefixString  (  int   level  )  { 
try  { 
switch  (  level  )  { 
case   SEVERE  : 
if  (  SEVERE_PREFIX  ==  null  )  { 
SEVERE_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_severe"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  SEVERE_PREFIX  )  ; 
break  ; 
case   WARNING  : 
if  (  WARNING_PREFIX  ==  null  )  { 
WARNING_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_warning"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  WARNING_PREFIX  )  ; 
break  ; 
case   INFO  : 
if  (  INFO_PREFIX  ==  null  )  { 
INFO_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_info"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  INFO_PREFIX  )  ; 
break  ; 
case   CONFIG  : 
if  (  CONFIG_PREFIX  ==  null  )  { 
CONFIG_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_config"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  CONFIG_PREFIX  )  ; 
break  ; 
case   FINE  : 
if  (  FINE_PREFIX  ==  null  )  { 
FINE_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_fine"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  FINE_PREFIX  )  ; 
break  ; 
case   FINER  : 
if  (  FINER_PREFIX  ==  null  )  { 
FINER_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_finer"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  FINER_PREFIX  )  ; 
break  ; 
case   FINEST  : 
if  (  FINEST_PREFIX  ==  null  )  { 
FINEST_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink_finest"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  FINEST_PREFIX  )  ; 
break  ; 
default  : 
if  (  TOPLINK_PREFIX  ==  null  )  { 
TOPLINK_PREFIX  =  LoggingLocalization  .  buildMessage  (  "toplink"  )  ; 
} 
this  .  getWriter  (  )  .  write  (  TOPLINK_PREFIX  )  ; 
} 
}  catch  (  IOException   exception  )  { 
throw   ValidationException  .  logIOError  (  exception  )  ; 
} 
} 







public   void   setDateFormat  (  DateFormat   dateFormat  )  { 
this  .  dateFormat  =  dateFormat  ; 
} 






protected   String   formatMessage  (  SessionLogEntry   entry  )  { 
String   message  =  entry  .  getMessage  (  )  ; 
if  (  entry  .  shouldTranslate  (  )  )  { 
if  (  entry  .  getLevel  (  )  >  FINE  )  { 
message  =  LoggingLocalization  .  buildMessage  (  message  ,  entry  .  getParameters  (  )  )  ; 
}  else  { 
message  =  TraceLocalization  .  buildMessage  (  message  ,  entry  .  getParameters  (  )  )  ; 
} 
}  else  { 
if  (  entry  .  getParameters  (  )  !=  null  &&  entry  .  getParameters  (  )  .  length  >  0  &&  message  .  indexOf  (  "{0"  )  >=  0  )  { 
message  =  java  .  text  .  MessageFormat  .  format  (  message  ,  entry  .  getParameters  (  )  )  ; 
} 
} 
return   message  ; 
} 










public   void   throwing  (  Throwable   throwable  )  { 
if  (  shouldLog  (  FINER  )  )  { 
SessionLogEntry   entry  =  new   SessionLogEntry  (  null  ,  throwable  )  ; 
entry  .  setLevel  (  FINER  )  ; 
log  (  entry  )  ; 
} 
} 






public   static   int   translateStringToLoggingLevel  (  String   loggingLevel  )  { 
if  (  loggingLevel  ==  null  )  { 
return   INFO  ; 
}  else   if  (  loggingLevel  .  equals  (  "OFF"  )  )  { 
return   OFF  ; 
}  else   if  (  loggingLevel  .  equals  (  "SEVERE"  )  )  { 
return   SEVERE  ; 
}  else   if  (  loggingLevel  .  equals  (  "WARNING"  )  )  { 
return   WARNING  ; 
}  else   if  (  loggingLevel  .  equals  (  "INFO"  )  )  { 
return   INFO  ; 
}  else   if  (  loggingLevel  .  equals  (  "CONFIG"  )  )  { 
return   CONFIG  ; 
}  else   if  (  loggingLevel  .  equals  (  "FINE"  )  )  { 
return   FINE  ; 
}  else   if  (  loggingLevel  .  equals  (  "FINER"  )  )  { 
return   FINER  ; 
}  else   if  (  loggingLevel  .  equals  (  "FINEST"  )  )  { 
return   FINEST  ; 
}  else   if  (  loggingLevel  .  equals  (  "ALL"  )  )  { 
return   ALL  ; 
} 
return   INFO  ; 
} 






public   static   String   translateLoggingLevelToString  (  int   loggingLevel  )  { 
if  (  loggingLevel  ==  OFF  )  { 
return  "OFF"  ; 
}  else   if  (  loggingLevel  ==  SEVERE  )  { 
return  "SEVERE"  ; 
}  else   if  (  loggingLevel  ==  WARNING  )  { 
return  "WARNING"  ; 
}  else   if  (  loggingLevel  ==  INFO  )  { 
return  "INFO"  ; 
}  else   if  (  loggingLevel  ==  CONFIG  )  { 
return  "CONFIG"  ; 
}  else   if  (  loggingLevel  ==  FINE  )  { 
return  "FINE"  ; 
}  else   if  (  loggingLevel  ==  FINER  )  { 
return  "FINER"  ; 
}  else   if  (  loggingLevel  ==  FINEST  )  { 
return  "FINEST"  ; 
}  else   if  (  loggingLevel  ==  ALL  )  { 
return  "ALL"  ; 
} 
return  "INFO"  ; 
} 











public   void   severe  (  String   message  )  { 
log  (  SEVERE  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   warning  (  String   message  )  { 
log  (  WARNING  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   info  (  String   message  )  { 
log  (  INFO  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   config  (  String   message  )  { 
log  (  CONFIG  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   fine  (  String   message  )  { 
log  (  FINE  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   finer  (  String   message  )  { 
log  (  FINER  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 











public   void   finest  (  String   message  )  { 
log  (  FINEST  ,  message  ,  (  Object  [  ]  )  null  )  ; 
} 












public   void   logThrowable  (  int   level  ,  Throwable   throwable  )  { 
if  (  shouldLog  (  level  )  )  { 
log  (  new   SessionLogEntry  (  null  ,  level  ,  null  ,  throwable  )  )  ; 
} 
} 





public   boolean   isOff  (  )  { 
return   this  .  level  ==  OFF  ; 
} 





public   Object   clone  (  )  { 
try  { 
return   super  .  clone  (  )  ; 
}  catch  (  Exception   exception  )  { 
return   null  ; 
} 
} 
} 

