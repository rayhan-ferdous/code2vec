package   net  .  yapbam  .  gui  ; 

import   java  .  awt  .  Component  ; 
import   java  .  awt  .  Window  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  concurrent  .  BlockingDeque  ; 
import   java  .  util  .  concurrent  .  LinkedBlockingDeque  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   net  .  yapbam  .  gui  .  dialogs  .  ErrorDialog  ; 
import   net  .  yapbam  .  update  .  VersionManager  ; 



public   class   ErrorManager  { 


public   static   final   ErrorManager   INSTANCE  =  new   ErrorManager  (  )  ; 

private   static   final   String   ENC  =  "UTF-8"  ; 

private   BlockingDeque  <  Message  >  errorsQueue  ; 

private   HashSet  <  String  >  encounteredErrors  ; 

private   ErrorManager  (  )  { 
this  .  encounteredErrors  =  new   HashSet  <  String  >  (  )  ; 
this  .  errorsQueue  =  new   LinkedBlockingDeque  <  Message  >  (  )  ; 
final   Thread   thread  =  new   Thread  (  new   LogSender  (  )  ,  "LogSender"  )  ; 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   Thread  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
thread  .  interrupt  (  )  ; 
} 
}  )  )  ; 
thread  .  setDaemon  (  true  )  ; 
thread  .  start  (  )  ; 
} 






public   void   display  (  Component   parent  ,  Throwable   t  )  { 
display  (  parent  ,  t  ,  LocalizationData  .  get  (  "ErrorManager.message"  )  )  ; 
} 







public   void   display  (  Component   parent  ,  Throwable   t  ,  String   message  )  { 
if  (  t  !=  null  )  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
t  .  printStackTrace  (  new   PrintWriter  (  writer  )  )  ; 
String   trace  =  writer  .  getBuffer  (  )  .  toString  (  )  ; 
t  .  printStackTrace  (  )  ; 
message  =  message  +  "\n\n"  +  trace  ; 
} 
JOptionPane  .  showMessageDialog  (  parent  ,  message  ,  LocalizationData  .  get  (  "ErrorManager.title"  )  ,  JOptionPane  .  WARNING_MESSAGE  )  ; 
} 






public   void   log  (  Window   parent  ,  Throwable   t  )  { 
try  { 
String   trace  =  getTraceKey  (  t  )  ; 
if  (  !  encounteredErrors  .  add  (  trace  )  )  { 
return  ; 
} 
int   action  =  Preferences  .  safeGetCrashReportAction  (  )  ; 
if  (  action  ==  0  )  { 
ErrorDialog   errorDialog  =  new   ErrorDialog  (  parent  ,  t  )  ; 
errorDialog  .  setVisible  (  true  )  ; 
Object   result  =  errorDialog  .  getResult  (  )  ; 
errorDialog  .  dispose  (  )  ; 
if  (  result  !=  null  )  { 
action  =  1  ; 
} 
} 
if  (  action  ==  1  )  { 
errorsQueue  .  add  (  new   Message  (  t  )  )  ; 
}  else  { 
System  .  err  .  println  (  "Exception was catched by "  +  this  .  getClass  (  )  .  getName  (  )  )  ; 
t  .  printStackTrace  (  )  ; 
} 
}  catch  (  Throwable   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 









private   String   getTraceKey  (  Throwable   t  )  { 
StackTraceElement  [  ]  elements  =  t  .  getStackTrace  (  )  ; 
StringBuilder   buffer  =  new   StringBuilder  (  )  ; 
buffer  .  append  (  t  .  toString  (  )  )  ; 
int   i  =  0  ; 
for  (  StackTraceElement   element  :  elements  )  { 
if  (  i  ==  2  )  break  ; 
buffer  .  append  (  element  .  toString  (  )  )  ; 
i  ++  ; 
} 
return   buffer  .  toString  (  )  ; 
} 

private   class   LogSender   implements   Runnable  { 

@  Override 
public   void   run  (  )  { 
while  (  true  )  { 
try  { 
postToYapbam  (  errorsQueue  .  take  (  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
}  catch  (  Throwable   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 

private   void   addToBuffer  (  StringBuilder   buffer  ,  String   variable  ,  String   value  )  throws   UnsupportedEncodingException  { 
buffer  .  append  (  "&"  )  .  append  (  URLEncoder  .  encode  (  variable  ,  ENC  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  value  ,  ENC  )  )  ; 
} 

private   void   postToYapbam  (  Message   message  )  throws   IOException  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
message  .  error  .  printStackTrace  (  new   PrintWriter  (  writer  )  )  ; 
String   trace  =  writer  .  getBuffer  (  )  .  toString  (  )  ; 
StringBuilder   data  =  new   StringBuilder  (  )  ; 
data  .  append  (  URLEncoder  .  encode  (  "error"  ,  ENC  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  trace  ,  ENC  )  )  ; 
addToBuffer  (  data  ,  "country"  ,  message  .  country  )  ; 
addToBuffer  (  data  ,  "javaVendor"  ,  message  .  javaVendor  )  ; 
addToBuffer  (  data  ,  "javaVersion"  ,  message  .  javaVersion  )  ; 
addToBuffer  (  data  ,  "lang"  ,  message  .  lang  )  ; 
addToBuffer  (  data  ,  "osName"  ,  message  .  osName  )  ; 
addToBuffer  (  data  ,  "osVersion"  ,  message  .  osVersion  )  ; 
addToBuffer  (  data  ,  "version"  ,  message  .  version  )  ; 
URL   url  =  new   URL  (  "http://www.yapbam.net/crashReport.php"  )  ; 
URLConnection   conn  =  url  .  openConnection  (  )  ; 
conn  .  setDoOutput  (  true  )  ; 
OutputStreamWriter   wr  =  new   OutputStreamWriter  (  conn  .  getOutputStream  (  )  ,  ENC  )  ; 
try  { 
wr  .  write  (  data  .  toString  (  )  )  ; 
wr  .  flush  (  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  ,  ENC  )  )  ; 
try  { 
for  (  String   line  =  rd  .  readLine  (  )  ;  line  !=  null  ;  line  =  rd  .  readLine  (  )  )  { 
System  .  out  .  println  (  line  )  ; 
} 
}  finally  { 
rd  .  close  (  )  ; 
} 
}  finally  { 
wr  .  close  (  )  ; 
} 
} 
} 

private   static   class   Message  { 

private   String   version  ; 

private   String   country  ; 

private   String   lang  ; 

private   String   osName  ; 

private   String   osVersion  ; 

private   String   javaVendor  ; 

private   String   javaVersion  ; 

private   Throwable   error  ; 

Message  (  Throwable   t  )  { 
this  .  error  =  t  ; 
this  .  version  =  VersionManager  .  getVersion  (  )  .  toString  (  )  ; 
this  .  country  =  LocalizationData  .  getLocale  (  )  .  getCountry  (  )  ; 
this  .  lang  =  LocalizationData  .  getLocale  (  )  .  getLanguage  (  )  ; 
this  .  osName  =  System  .  getProperty  (  "os.name"  ,  "?"  )  ; 
this  .  osVersion  =  System  .  getProperty  (  "os.version"  ,  "?"  )  ; 
this  .  javaVendor  =  System  .  getProperty  (  "java.vendor"  ,  "?"  )  ; 
this  .  javaVersion  =  System  .  getProperty  (  "java.version"  ,  "?"  )  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
INSTANCE  .  log  (  null  ,  new   RuntimeException  (  "just a test"  )  )  ; 
INSTANCE  .  log  (  null  ,  new   RuntimeException  (  "just a second test"  )  )  ; 
} 
} 

