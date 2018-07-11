package   org  .  hsqldb  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  StringTokenizer  ; 
import   org  .  hsqldb  .  lib  .  ArrayUtil  ; 
import   org  .  hsqldb  .  lib  .  FileUtil  ; 
import   org  .  hsqldb  .  lib  .  HashSet  ; 
import   org  .  hsqldb  .  lib  .  Iterator  ; 
import   org  .  hsqldb  .  lib  .  StopWatch  ; 
import   org  .  hsqldb  .  lib  .  StringUtil  ; 
import   org  .  hsqldb  .  lib  .  WrapperIterator  ; 
import   org  .  hsqldb  .  lib  .  java  .  JavaSystem  ; 
import   org  .  hsqldb  .  persist  .  HsqlDatabaseProperties  ; 
import   org  .  hsqldb  .  persist  .  HsqlProperties  ; 
import   org  .  hsqldb  .  resources  .  BundleHandler  ; 








































































































































































public   class   Server   implements   HsqlSocketRequestHandler  { 

protected   static   final   int   serverBundleHandle  =  BundleHandler  .  getBundleHandle  (  "org_hsqldb_Server_messages"  ,  null  )  ; 

HsqlProperties   serverProperties  ; 

HashSet   serverConnSet  ; 

private   String  [  ]  dbAlias  ; 

private   String  [  ]  dbType  ; 

private   String  [  ]  dbPath  ; 

private   HsqlProperties  [  ]  dbProps  ; 

private   int  [  ]  dbID  ; 

private   int   maxConnections  ; 

protected   String   serverId  ; 

protected   int   serverProtocol  ; 

protected   ThreadGroup   serverConnectionThreadGroup  ; 

protected   HsqlSocketFactory   socketFactory  ; 

protected   ServerSocket   socket  ; 

private   Thread   serverThread  ; 

private   Throwable   serverError  ; 

private   volatile   int   serverState  ; 

private   volatile   boolean   isSilent  ; 

private   volatile   boolean   isRemoteOpen  ; 

private   PrintWriter   logWriter  ; 

private   PrintWriter   errWriter  ; 





private   class   ServerThread   extends   Thread  { 







ServerThread  (  String   name  )  { 
super  (  name  )  ; 
setName  (  name  +  '@'  +  Integer  .  toString  (  Server  .  this  .  hashCode  (  )  ,  16  )  )  ; 
} 




public   void   run  (  )  { 
Server  .  this  .  run  (  )  ; 
printWithThread  (  "ServerThread.run() exited"  )  ; 
} 
} 




public   Server  (  )  { 
this  (  ServerConstants  .  SC_PROTOCOL_HSQL  )  ; 
} 













protected   Server  (  int   protocol  )  { 
init  (  protocol  )  ; 
} 








public   static   void   main  (  String  [  ]  args  )  { 
String   propsPath  =  FileUtil  .  getDefaultInstance  (  )  .  canonicalOrAbsolutePath  (  "server"  )  ; 
HsqlProperties   fileProps  =  ServerConfiguration  .  getPropertiesFromFile  (  propsPath  )  ; 
HsqlProperties   props  =  fileProps  ==  null  ?  new   HsqlProperties  (  )  :  fileProps  ; 
HsqlProperties   stringProps  =  null  ; 
try  { 
stringProps  =  HsqlProperties  .  argArrayToProps  (  args  ,  ServerConstants  .  SC_KEY_PREFIX  )  ; 
}  catch  (  ArrayIndexOutOfBoundsException   aioob  )  { 
printHelp  (  "server.help"  )  ; 
return  ; 
} 
if  (  stringProps  !=  null  )  { 
if  (  stringProps  .  getErrorKeys  (  )  .  length  !=  0  )  { 
printHelp  (  "server.help"  )  ; 
return  ; 
} 
props  .  addProperties  (  stringProps  )  ; 
} 
ServerConfiguration  .  translateDefaultDatabaseProperty  (  props  )  ; 
ServerConfiguration  .  translateDefaultNoSystemExitProperty  (  props  )  ; 
Server   server  =  new   Server  (  )  ; 
try  { 
server  .  setProperties  (  props  )  ; 
}  catch  (  Exception   e  )  { 
server  .  printError  (  "Failed to set properties"  )  ; 
server  .  printStackTrace  (  e  )  ; 
return  ; 
} 
server  .  print  (  "Startup sequence initiated from main() method"  )  ; 
if  (  fileProps  !=  null  )  { 
server  .  print  (  "Loaded properties from ["  +  propsPath  +  ".properties]"  )  ; 
}  else  { 
server  .  print  (  "Could not load properties from file"  )  ; 
server  .  print  (  "Using cli/default properties only"  )  ; 
} 
server  .  start  (  )  ; 
} 










public   void   checkRunning  (  boolean   running  )  throws   RuntimeException  { 
int   state  ; 
boolean   error  ; 
printWithThread  (  "checkRunning("  +  running  +  ") entered"  )  ; 
state  =  getState  (  )  ; 
error  =  (  running  &&  state  !=  ServerConstants  .  SERVER_STATE_ONLINE  )  ||  (  !  running  &&  state  !=  ServerConstants  .  SERVER_STATE_SHUTDOWN  )  ; 
if  (  error  )  { 
String   msg  =  "server is "  +  (  running  ?  "not "  :  ""  )  +  "running"  ; 
throw   new   RuntimeException  (  msg  )  ; 
} 
printWithThread  (  "checkRunning("  +  running  +  ") exited"  )  ; 
} 








public   synchronized   void   signalCloseAllServerConnections  (  )  { 
Iterator   it  ; 
printWithThread  (  "signalCloseAllServerConnections() entered"  )  ; 
synchronized  (  serverConnSet  )  { 
it  =  new   WrapperIterator  (  serverConnSet  .  toArray  (  null  )  )  ; 
} 
for  (  ;  it  .  hasNext  (  )  ;  )  { 
ServerConnection   sc  =  (  ServerConnection  )  it  .  next  (  )  ; 
printWithThread  (  "Closing "  +  sc  )  ; 
sc  .  signalClose  (  )  ; 
} 
printWithThread  (  "signalCloseAllServerConnections() exited"  )  ; 
} 

protected   void   finalize  (  )  throws   Throwable  { 
if  (  serverThread  !=  null  )  { 
releaseServerSocket  (  )  ; 
} 
} 










public   String   getAddress  (  )  { 
return   socket  ==  null  ?  serverProperties  .  getProperty  (  ServerConstants  .  SC_KEY_ADDRESS  )  :  socket  .  getInetAddress  (  )  .  getHostAddress  (  )  ; 
} 



























public   String   getDatabaseName  (  int   index  ,  boolean   asconfigured  )  { 
if  (  asconfigured  )  { 
return   serverProperties  .  getProperty  (  ServerConstants  .  SC_KEY_DBNAME  +  "."  +  index  )  ; 
}  else   if  (  getState  (  )  ==  ServerConstants  .  SERVER_STATE_ONLINE  )  { 
return  (  dbAlias  ==  null  ||  index  <  0  ||  index  >=  dbAlias  .  length  )  ?  null  :  dbAlias  [  index  ]  ; 
}  else  { 
return   null  ; 
} 
} 




























public   String   getDatabasePath  (  int   index  ,  boolean   asconfigured  )  { 
if  (  asconfigured  )  { 
return   serverProperties  .  getProperty  (  ServerConstants  .  SC_KEY_DATABASE  +  "."  +  index  )  ; 
}  else   if  (  getState  (  )  ==  ServerConstants  .  SERVER_STATE_ONLINE  )  { 
return  (  dbPath  ==  null  ||  index  <  0  ||  index  >=  dbPath  .  length  )  ?  null  :  dbPath  [  index  ]  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getDatabaseType  (  int   index  )  { 
return  (  dbType  ==  null  ||  index  <  0  ||  index  >=  dbType  .  length  )  ?  null  :  dbType  [  index  ]  ; 
} 











public   String   getDefaultWebPage  (  )  { 
return  "[IGNORED]"  ; 
} 







public   String   getHelpString  (  )  { 
return   BundleHandler  .  getString  (  serverBundleHandle  ,  "server.help"  )  ; 
} 






public   PrintWriter   getErrWriter  (  )  { 
return   errWriter  ; 
} 






public   PrintWriter   getLogWriter  (  )  { 
return   logWriter  ; 
} 










public   int   getPort  (  )  { 
return   serverProperties  .  getIntegerProperty  (  ServerConstants  .  SC_KEY_PORT  ,  ServerConfiguration  .  getDefaultPort  (  serverProtocol  ,  isTls  (  )  )  )  ; 
} 












public   String   getProductName  (  )  { 
return  "HSQLDB server"  ; 
} 












public   String   getProductVersion  (  )  { 
return   HsqlDatabaseProperties  .  THIS_VERSION  ; 
} 











public   String   getProtocol  (  )  { 
return   isTls  (  )  ?  "HSQLS"  :  "HSQL"  ; 
} 










public   Throwable   getServerError  (  )  { 
return   serverError  ; 
} 










public   String   getServerId  (  )  { 
return   serverId  ; 
} 



















public   synchronized   int   getState  (  )  { 
return   serverState  ; 
} 












public   String   getStateDescriptor  (  )  { 
String   state  ; 
Throwable   t  =  getServerError  (  )  ; 
switch  (  serverState  )  { 
case   ServerConstants  .  SERVER_STATE_SHUTDOWN  : 
state  =  "SHUTDOWN"  ; 
break  ; 
case   ServerConstants  .  SERVER_STATE_OPENING  : 
state  =  "OPENING"  ; 
break  ; 
case   ServerConstants  .  SERVER_STATE_CLOSING  : 
state  =  "CLOSING"  ; 
break  ; 
case   ServerConstants  .  SERVER_STATE_ONLINE  : 
state  =  "ONLINE"  ; 
break  ; 
default  : 
state  =  "UNKNOWN"  ; 
break  ; 
} 
return   state  ; 
} 














public   String   getWebRoot  (  )  { 
return  "[IGNORED]"  ; 
} 







public   void   handleConnection  (  Socket   s  )  { 
Thread   t  ; 
Runnable   r  ; 
String   ctn  ; 
printWithThread  (  "handleConnection("  +  s  +  ") entered"  )  ; 
if  (  !  allowConnection  (  s  )  )  { 
try  { 
s  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
printWithThread  (  "allowConnection(): connection refused"  )  ; 
printWithThread  (  "handleConnection() exited"  )  ; 
return  ; 
} 
if  (  socketFactory  !=  null  )  { 
socketFactory  .  configureSocket  (  s  )  ; 
} 
if  (  serverProtocol  ==  ServerConstants  .  SC_PROTOCOL_HSQL  )  { 
r  =  new   ServerConnection  (  s  ,  this  )  ; 
ctn  =  (  (  ServerConnection  )  r  )  .  getConnectionThreadName  (  )  ; 
synchronized  (  serverConnSet  )  { 
serverConnSet  .  add  (  r  )  ; 
} 
}  else  { 
r  =  new   WebServerConnection  (  s  ,  (  WebServer  )  this  )  ; 
ctn  =  (  (  WebServerConnection  )  r  )  .  getConnectionThreadName  (  )  ; 
} 
t  =  new   Thread  (  serverConnectionThreadGroup  ,  r  ,  ctn  )  ; 
t  .  start  (  )  ; 
printWithThread  (  "handleConnection() exited"  )  ; 
} 










public   boolean   isNoSystemExit  (  )  { 
return   serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_NO_SYSTEM_EXIT  )  ; 
} 










public   boolean   isRestartOnShutdown  (  )  { 
return   serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_AUTORESTART_SERVER  )  ; 
} 












public   boolean   isSilent  (  )  { 
return   isSilent  ; 
} 











public   boolean   isTls  (  )  { 
return   serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_TLS  )  ; 
} 











public   boolean   isTrace  (  )  { 
return   serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_TRACE  )  ; 
} 






















public   boolean   putPropertiesFromFile  (  String   path  )  { 
if  (  getState  (  )  !=  ServerConstants  .  SERVER_STATE_SHUTDOWN  )  { 
throw   new   RuntimeException  (  )  ; 
} 
path  =  FileUtil  .  getDefaultInstance  (  )  .  canonicalOrAbsolutePath  (  path  )  ; 
HsqlProperties   p  =  ServerConfiguration  .  getPropertiesFromFile  (  path  )  ; 
if  (  p  ==  null  ||  p  .  isEmpty  (  )  )  { 
return   false  ; 
} 
printWithThread  (  "putPropertiesFromFile(): ["  +  path  +  ".properties]"  )  ; 
try  { 
setProperties  (  p  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Failed to set properties: "  +  e  )  ; 
} 
return   true  ; 
} 




















public   void   putPropertiesFromString  (  String   s  )  { 
if  (  getState  (  )  !=  ServerConstants  .  SERVER_STATE_SHUTDOWN  )  { 
throw   new   RuntimeException  (  )  ; 
} 
if  (  StringUtil  .  isEmpty  (  s  )  )  { 
return  ; 
} 
printWithThread  (  "putPropertiesFromString(): ["  +  s  +  "]"  )  ; 
HsqlProperties   p  =  HsqlProperties  .  delimitedArgPairsToProps  (  s  ,  "="  ,  ";"  ,  ServerConstants  .  SC_KEY_PREFIX  )  ; 
try  { 
setProperties  (  p  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Failed to set properties: "  +  e  )  ; 
} 
} 















public   void   setAddress  (  String   address  )  throws   RuntimeException  { 
checkRunning  (  false  )  ; 
if  (  org  .  hsqldb  .  lib  .  StringUtil  .  isEmpty  (  address  )  )  { 
address  =  ServerConstants  .  SC_DEFAULT_ADDRESS  ; 
} 
printWithThread  (  "setAddress("  +  address  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_ADDRESS  ,  address  )  ; 
} 
























public   void   setDatabaseName  (  int   index  ,  String   name  )  throws   RuntimeException  { 
checkRunning  (  false  )  ; 
printWithThread  (  "setDatabaseName("  +  index  +  ","  +  name  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_DBNAME  +  "."  +  index  ,  name  )  ; 
} 























public   void   setDatabasePath  (  int   index  ,  String   path  )  throws   RuntimeException  { 
checkRunning  (  false  )  ; 
printWithThread  (  "setDatabasePath("  +  index  +  ","  +  path  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_DATABASE  +  "."  +  index  ,  path  )  ; 
} 








public   void   setDefaultWebPage  (  String   file  )  { 
checkRunning  (  false  )  ; 
printWithThread  (  "setDefaultWebPage("  +  file  +  ")"  )  ; 
if  (  serverProtocol  !=  ServerConstants  .  SC_PROTOCOL_HTTP  )  { 
return  ; 
} 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_WEB_DEFAULT_PAGE  ,  file  )  ; 
} 








public   void   setPort  (  int   port  )  throws   RuntimeException  { 
checkRunning  (  false  )  ; 
printWithThread  (  "setPort("  +  port  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_PORT  ,  port  )  ; 
} 








public   void   setErrWriter  (  PrintWriter   pw  )  { 
errWriter  =  pw  ; 
} 








public   void   setLogWriter  (  PrintWriter   pw  )  { 
logWriter  =  pw  ; 
} 








public   void   setNoSystemExit  (  boolean   noExit  )  { 
printWithThread  (  "setNoSystemExit("  +  noExit  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_NO_SYSTEM_EXIT  ,  noExit  )  ; 
} 








public   void   setRestartOnShutdown  (  boolean   restart  )  { 
printWithThread  (  "setRestartOnShutdown("  +  restart  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_AUTORESTART_SERVER  ,  restart  )  ; 
} 









public   void   setSilent  (  boolean   silent  )  { 
printWithThread  (  "setSilent("  +  silent  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_SILENT  ,  silent  )  ; 
isSilent  =  silent  ; 
} 









public   void   setTls  (  boolean   tls  )  { 
checkRunning  (  false  )  ; 
printWithThread  (  "setTls("  +  tls  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_TLS  ,  tls  )  ; 
} 









public   void   setTrace  (  boolean   trace  )  { 
printWithThread  (  "setTrace("  +  trace  +  ")"  )  ; 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_TRACE  ,  trace  )  ; 
JavaSystem  .  setLogToSystem  (  trace  )  ; 
} 









public   void   setWebRoot  (  String   root  )  { 
checkRunning  (  false  )  ; 
root  =  (  new   File  (  root  )  )  .  getAbsolutePath  (  )  ; 
printWithThread  (  "setWebRoot("  +  root  +  ")"  )  ; 
if  (  serverProtocol  !=  ServerConstants  .  SC_PROTOCOL_HTTP  )  { 
return  ; 
} 
serverProperties  .  setProperty  (  ServerConstants  .  SC_KEY_WEB_ROOT  ,  root  )  ; 
} 






public   void   setProperties  (  HsqlProperties   p  )  { 
checkRunning  (  false  )  ; 
if  (  p  !=  null  )  { 
serverProperties  .  addProperties  (  p  )  ; 
ServerConfiguration  .  translateAddressProperty  (  serverProperties  )  ; 
} 
maxConnections  =  serverProperties  .  getIntegerProperty  (  ServerConstants  .  SC_KEY_MAX_CONNECTIONS  ,  16  )  ; 
JavaSystem  .  setLogToSystem  (  isTrace  (  )  )  ; 
isSilent  =  serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_SILENT  )  ; 
isRemoteOpen  =  serverProperties  .  isPropertyTrue  (  ServerConstants  .  SC_KEY_REMOTE_OPEN_DB  )  ; 
} 
















public   int   start  (  )  { 
printWithThread  (  "start() entered"  )  ; 
int   previousState  =  getState  (  )  ; 
if  (  serverThread  !=  null  )  { 
printWithThread  (  "start(): serverThread != null; no action taken"  )  ; 
return   previousState  ; 
} 
setState  (  ServerConstants  .  SERVER_STATE_OPENING  )  ; 
serverThread  =  new   ServerThread  (  "HSQLDB Server "  )  ; 
serverThread  .  start  (  )  ; 
while  (  getState  (  )  ==  ServerConstants  .  SERVER_STATE_OPENING  )  { 
try  { 
Thread  .  sleep  (  100  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
printWithThread  (  "start() exiting"  )  ; 
return   previousState  ; 
} 















public   int   stop  (  )  { 
printWithThread  (  "stop() entered"  )  ; 
int   previousState  =  getState  (  )  ; 
if  (  serverThread  ==  null  )  { 
printWithThread  (  "stop() serverThread is null; no action taken"  )  ; 
return   previousState  ; 
} 
releaseServerSocket  (  )  ; 
printWithThread  (  "stop() exiting"  )  ; 
return   previousState  ; 
} 









protected   boolean   allowConnection  (  Socket   socket  )  { 
return   true  ; 
} 






protected   void   init  (  int   protocol  )  { 
serverState  =  ServerConstants  .  SERVER_STATE_SHUTDOWN  ; 
serverConnSet  =  new   HashSet  (  )  ; 
serverId  =  toString  (  )  ; 
serverId  =  serverId  .  substring  (  serverId  .  lastIndexOf  (  '.'  )  +  1  )  ; 
serverProtocol  =  protocol  ; 
serverProperties  =  ServerConfiguration  .  newDefaultProperties  (  protocol  )  ; 
logWriter  =  new   PrintWriter  (  System  .  out  )  ; 
errWriter  =  new   PrintWriter  (  System  .  err  )  ; 
JavaSystem  .  setLogToSystem  (  isTrace  (  )  )  ; 
} 






protected   synchronized   void   setState  (  int   state  )  { 
serverState  =  state  ; 
} 







final   void   notify  (  int   action  ,  int   id  )  { 
printWithThread  (  "notifiy("  +  action  +  ","  +  id  +  ") entered"  )  ; 
if  (  action  !=  ServerConstants  .  SC_DATABASE_SHUTDOWN  )  { 
return  ; 
} 
releaseDatabase  (  id  )  ; 
boolean   shutdown  =  true  ; 
for  (  int   i  =  0  ;  i  <  dbID  .  length  ;  i  ++  )  { 
if  (  dbAlias  [  i  ]  !=  null  )  { 
shutdown  =  false  ; 
} 
} 
if  (  !  isRemoteOpen  &&  shutdown  )  { 
stop  (  )  ; 
} 
} 





final   synchronized   void   releaseDatabase  (  int   id  )  { 
Iterator   it  ; 
boolean   found  =  false  ; 
printWithThread  (  "releaseDatabase("  +  id  +  ") entered"  )  ; 
for  (  int   i  =  0  ;  i  <  dbID  .  length  ;  i  ++  )  { 
if  (  dbID  [  i  ]  ==  id  &&  dbAlias  [  i  ]  !=  null  )  { 
dbID  [  i  ]  =  0  ; 
dbAlias  [  i  ]  =  null  ; 
dbPath  [  i  ]  =  null  ; 
dbType  [  i  ]  =  null  ; 
dbProps  [  i  ]  =  null  ; 
} 
} 
synchronized  (  serverConnSet  )  { 
it  =  new   WrapperIterator  (  serverConnSet  .  toArray  (  null  )  )  ; 
} 
while  (  it  .  hasNext  (  )  )  { 
ServerConnection   sc  =  (  ServerConnection  )  it  .  next  (  )  ; 
if  (  sc  .  dbID  ==  id  )  { 
sc  .  signalClose  (  )  ; 
serverConnSet  .  remove  (  sc  )  ; 
} 
} 
printWithThread  (  "releaseDatabase("  +  id  +  ") exiting"  )  ; 
} 







protected   synchronized   void   print  (  String   msg  )  { 
PrintWriter   writer  =  logWriter  ; 
if  (  writer  !=  null  )  { 
writer  .  println  (  "["  +  serverId  +  "]: "  +  msg  )  ; 
writer  .  flush  (  )  ; 
} 
} 








final   void   printResource  (  String   key  )  { 
String   resource  ; 
StringTokenizer   st  ; 
if  (  serverBundleHandle  <  0  )  { 
return  ; 
} 
resource  =  BundleHandler  .  getString  (  serverBundleHandle  ,  key  )  ; 
if  (  resource  ==  null  )  { 
return  ; 
} 
st  =  new   StringTokenizer  (  resource  ,  "\n\r"  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
print  (  st  .  nextToken  (  )  )  ; 
} 
} 







protected   synchronized   void   printStackTrace  (  Throwable   t  )  { 
if  (  errWriter  !=  null  )  { 
t  .  printStackTrace  (  errWriter  )  ; 
errWriter  .  flush  (  )  ; 
} 
} 








final   void   printWithTimestamp  (  String   msg  )  { 
print  (  HsqlDateTime  .  getSytemTimeString  (  )  +  " "  +  msg  )  ; 
} 








protected   void   printWithThread  (  String   msg  )  { 
if  (  !  isSilent  (  )  )  { 
print  (  "["  +  Thread  .  currentThread  (  )  +  "]: "  +  msg  )  ; 
} 
} 








protected   synchronized   void   printError  (  String   msg  )  { 
PrintWriter   writer  =  errWriter  ; 
if  (  writer  !=  null  )  { 
writer  .  print  (  "["  +  serverId  +  "]: "  )  ; 
writer  .  print  (  "["  +  Thread  .  currentThread  (  )  +  "]: "  )  ; 
writer  .  println  (  msg  )  ; 
writer  .  flush  (  )  ; 
} 
} 























final   void   printRequest  (  int   cid  ,  Result   r  )  { 
if  (  isSilent  (  )  )  { 
return  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  cid  )  ; 
sb  .  append  (  ':'  )  ; 
switch  (  r  .  mode  )  { 
case   ResultConstants  .  SQLPREPARE  : 
{ 
sb  .  append  (  "SQLCLI:SQLPREPARE "  )  ; 
sb  .  append  (  r  .  getMainString  (  )  )  ; 
break  ; 
} 
case   ResultConstants  .  SQLEXECDIRECT  : 
{ 
if  (  r  .  getSize  (  )  <  2  )  { 
sb  .  append  (  r  .  getMainString  (  )  )  ; 
}  else  { 
sb  .  append  (  "SQLCLI:SQLEXECDIRECT:BATCHMODE\n"  )  ; 
Iterator   it  =  r  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object  [  ]  data  =  (  Object  [  ]  )  it  .  next  (  )  ; 
sb  .  append  (  data  [  0  ]  )  .  append  (  '\n'  )  ; 
} 
} 
break  ; 
} 
case   ResultConstants  .  SQLEXECUTE  : 
{ 
sb  .  append  (  "SQLCLI:SQLEXECUTE:"  )  ; 
if  (  r  .  getSize  (  )  >  1  )  { 
sb  .  append  (  "BATCHMODE:"  )  ; 
} 
sb  .  append  (  r  .  getStatementID  (  )  )  ; 
break  ; 
} 
case   ResultConstants  .  SQLFREESTMT  : 
{ 
sb  .  append  (  "SQLCLI:SQLFREESTMT:"  )  ; 
sb  .  append  (  r  .  getStatementID  (  )  )  ; 
break  ; 
} 
case   ResultConstants  .  GETSESSIONATTR  : 
{ 
sb  .  append  (  "HSQLCLI:GETSESSIONATTR"  )  ; 
break  ; 
} 
case   ResultConstants  .  SETSESSIONATTR  : 
{ 
sb  .  append  (  "HSQLCLI:SETSESSIONATTR:"  )  ; 
sb  .  append  (  "AUTOCOMMIT "  )  ; 
sb  .  append  (  r  .  rRoot  .  data  [  Session  .  INFO_AUTOCOMMIT  ]  )  ; 
sb  .  append  (  " CONNECTION_READONLY "  )  ; 
sb  .  append  (  r  .  rRoot  .  data  [  Session  .  INFO_CONNECTION_READONLY  ]  )  ; 
break  ; 
} 
case   ResultConstants  .  SQLENDTRAN  : 
{ 
sb  .  append  (  "SQLCLI:SQLENDTRAN:"  )  ; 
switch  (  r  .  getEndTranType  (  )  )  { 
case   ResultConstants  .  COMMIT  : 
sb  .  append  (  "COMMIT"  )  ; 
break  ; 
case   ResultConstants  .  ROLLBACK  : 
sb  .  append  (  "ROLLBACK"  )  ; 
break  ; 
case   ResultConstants  .  SAVEPOINT_NAME_RELEASE  : 
sb  .  append  (  "SAVEPOINT_NAME_RELEASE "  )  ; 
sb  .  append  (  r  .  getMainString  (  )  )  ; 
break  ; 
case   ResultConstants  .  SAVEPOINT_NAME_ROLLBACK  : 
sb  .  append  (  "SAVEPOINT_NAME_ROLLBACK "  )  ; 
sb  .  append  (  r  .  getMainString  (  )  )  ; 
break  ; 
default  : 
sb  .  append  (  r  .  getEndTranType  (  )  )  ; 
} 
break  ; 
} 
case   ResultConstants  .  SQLSTARTTRAN  : 
{ 
sb  .  append  (  "SQLCLI:SQLSTARTTRAN"  )  ; 
break  ; 
} 
case   ResultConstants  .  SQLDISCONNECT  : 
{ 
sb  .  append  (  "SQLCLI:SQLDISCONNECT"  )  ; 
break  ; 
} 
case   ResultConstants  .  SQLSETCONNECTATTR  : 
{ 
sb  .  append  (  "SQLCLI:SQLSETCONNECTATTR:"  )  ; 
switch  (  r  .  getConnectionAttrType  (  )  )  { 
case   ResultConstants  .  SQL_ATTR_SAVEPOINT_NAME  : 
{ 
sb  .  append  (  "SQL_ATTR_SAVEPOINT_NAME "  )  ; 
sb  .  append  (  r  .  getMainString  (  )  )  ; 
break  ; 
} 
default  : 
{ 
sb  .  append  (  r  .  getConnectionAttrType  (  )  )  ; 
} 
} 
break  ; 
} 
default  : 
{ 
sb  .  append  (  "SQLCLI:MODE:"  )  ; 
sb  .  append  (  r  .  mode  )  ; 
break  ; 
} 
} 
print  (  sb  .  toString  (  )  )  ; 
} 




final   synchronized   int   getDBID  (  String   aliasPath  )  throws   HsqlException  { 
int   semipos  =  aliasPath  .  indexOf  (  ';'  )  ; 
String   alias  =  aliasPath  ; 
String   filepath  =  null  ; 
if  (  semipos  !=  -  1  )  { 
alias  =  aliasPath  .  substring  (  0  ,  semipos  )  ; 
filepath  =  aliasPath  .  substring  (  semipos  +  1  )  ; 
} 
int   dbIndex  =  ArrayUtil  .  find  (  dbAlias  ,  alias  )  ; 
if  (  dbIndex  ==  -  1  )  { 
if  (  filepath  ==  null  )  { 
RuntimeException   e  =  new   RuntimeException  (  "database alias does not exist"  )  ; 
printError  (  "database alias="  +  alias  +  " does not exist"  )  ; 
setServerError  (  e  )  ; 
throw   e  ; 
}  else  { 
return   openDatabase  (  alias  ,  filepath  )  ; 
} 
}  else  { 
return   dbID  [  dbIndex  ]  ; 
} 
} 




final   int   openDatabase  (  String   alias  ,  String   filepath  )  throws   HsqlException  { 
if  (  !  isRemoteOpen  )  { 
RuntimeException   e  =  new   RuntimeException  (  "remote open not allowed"  )  ; 
printError  (  "Remote database open not allowed"  )  ; 
setServerError  (  e  )  ; 
throw   e  ; 
} 
int   i  =  getFirstEmptyDatabaseIndex  (  )  ; 
if  (  i  <  -  1  )  { 
RuntimeException   e  =  new   RuntimeException  (  "limit of open databases reached"  )  ; 
printError  (  "limit of open databases reached"  )  ; 
setServerError  (  e  )  ; 
throw   e  ; 
} 
HsqlProperties   newprops  =  DatabaseURL  .  parseURL  (  filepath  ,  false  )  ; 
if  (  newprops  ==  null  )  { 
RuntimeException   e  =  new   RuntimeException  (  "invalid database path"  )  ; 
printError  (  "invalid database path"  )  ; 
setServerError  (  e  )  ; 
throw   e  ; 
} 
String   path  =  newprops  .  getProperty  (  "database"  )  ; 
String   type  =  newprops  .  getProperty  (  "connection_type"  )  ; 
try  { 
int   dbid  =  DatabaseManager  .  getDatabase  (  type  ,  path  ,  this  ,  newprops  )  ; 
dbID  [  i  ]  =  dbid  ; 
dbAlias  [  i  ]  =  alias  ; 
dbPath  [  i  ]  =  path  ; 
dbType  [  i  ]  =  type  ; 
dbProps  [  i  ]  =  newprops  ; 
return   dbid  ; 
}  catch  (  HsqlException   e  )  { 
printError  (  "Database [index="  +  i  +  "db="  +  dbType  [  i  ]  +  dbPath  [  i  ]  +  ", alias="  +  dbAlias  [  i  ]  +  "] did not open: "  +  e  .  toString  (  )  )  ; 
setServerError  (  e  )  ; 
throw   e  ; 
} 
} 

final   int   getFirstEmptyDatabaseIndex  (  )  { 
for  (  int   i  =  0  ;  i  <  dbAlias  .  length  ;  i  ++  )  { 
if  (  dbAlias  [  i  ]  ==  null  )  { 
return   i  ; 
} 
} 
return  -  1  ; 
} 










final   boolean   openDatabases  (  )  { 
printWithThread  (  "openDatabases() entered"  )  ; 
boolean   success  =  false  ; 
setDBInfoArrays  (  )  ; 
for  (  int   i  =  0  ;  i  <  dbAlias  .  length  ;  i  ++  )  { 
if  (  dbAlias  [  i  ]  ==  null  )  { 
continue  ; 
} 
printWithThread  (  "Opening database: ["  +  dbType  [  i  ]  +  dbPath  [  i  ]  +  "]"  )  ; 
StopWatch   sw  =  new   StopWatch  (  )  ; 
int   id  ; 
try  { 
id  =  DatabaseManager  .  getDatabase  (  dbType  [  i  ]  ,  dbPath  [  i  ]  ,  this  ,  dbProps  [  i  ]  )  ; 
dbID  [  i  ]  =  id  ; 
success  =  true  ; 
}  catch  (  HsqlException   e  )  { 
printError  (  "Database [index="  +  i  +  "db="  +  dbType  [  i  ]  +  dbPath  [  i  ]  +  ", alias="  +  dbAlias  [  i  ]  +  "] did not open: "  +  e  .  toString  (  )  )  ; 
setServerError  (  e  )  ; 
dbAlias  [  i  ]  =  null  ; 
dbPath  [  i  ]  =  null  ; 
dbType  [  i  ]  =  null  ; 
dbProps  [  i  ]  =  null  ; 
continue  ; 
} 
sw  .  stop  (  )  ; 
String   msg  =  "Database [index="  +  i  +  ", id="  +  id  +  ", "  +  "db="  +  dbType  [  i  ]  +  dbPath  [  i  ]  +  ", alias="  +  dbAlias  [  i  ]  +  "] opened sucessfully"  ; 
print  (  sw  .  elapsedTimeToMessage  (  msg  )  )  ; 
} 
printWithThread  (  "openDatabases() exiting"  )  ; 
if  (  isRemoteOpen  )  { 
success  =  true  ; 
} 
if  (  !  success  &&  getServerError  (  )  ==  null  )  { 
setServerError  (  Trace  .  error  (  Trace  .  SERVER_NO_DATABASE  )  )  ; 
} 
return   success  ; 
} 




private   void   setDBInfoArrays  (  )  { 
dbAlias  =  getDBNameArray  (  )  ; 
dbPath  =  new   String  [  dbAlias  .  length  ]  ; 
dbType  =  new   String  [  dbAlias  .  length  ]  ; 
dbID  =  new   int  [  dbAlias  .  length  ]  ; 
dbProps  =  new   HsqlProperties  [  dbAlias  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  dbAlias  .  length  ;  i  ++  )  { 
if  (  dbAlias  [  i  ]  ==  null  )  { 
continue  ; 
} 
String   path  =  getDatabasePath  (  i  ,  true  )  ; 
if  (  path  ==  null  )  { 
dbAlias  [  i  ]  =  null  ; 
continue  ; 
} 
HsqlProperties   dbURL  =  DatabaseURL  .  parseURL  (  path  ,  false  )  ; 
if  (  dbURL  ==  null  )  { 
dbAlias  [  i  ]  =  null  ; 
continue  ; 
} 
dbPath  [  i  ]  =  dbURL  .  getProperty  (  "database"  )  ; 
dbType  [  i  ]  =  dbURL  .  getProperty  (  "connection_type"  )  ; 
dbProps  [  i  ]  =  dbURL  ; 
} 
} 





private   String  [  ]  getDBNameArray  (  )  { 
final   String   prefix  =  ServerConstants  .  SC_KEY_DBNAME  +  "."  ; 
final   int   prefixLen  =  prefix  .  length  (  )  ; 
String  [  ]  dblist  =  new   String  [  10  ]  ; 
int   maxindex  =  0  ; 
try  { 
Enumeration   en  =  serverProperties  .  propertyNames  (  )  ; 
for  (  ;  en  .  hasMoreElements  (  )  ;  )  { 
String   key  =  (  String  )  en  .  nextElement  (  )  ; 
if  (  !  key  .  startsWith  (  prefix  )  )  { 
continue  ; 
} 
try  { 
int   dbnum  =  Integer  .  parseInt  (  key  .  substring  (  prefixLen  )  )  ; 
maxindex  =  dbnum  <  maxindex  ?  maxindex  :  dbnum  ; 
dblist  [  dbnum  ]  =  serverProperties  .  getProperty  (  key  )  .  toLowerCase  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
printWithThread  (  "dblist: "  +  e  .  toString  (  )  )  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
printWithThread  (  "dblist: "  +  e  .  toString  (  )  )  ; 
} 
return   dblist  ; 
} 







private   void   openServerSocket  (  )  throws   Exception  { 
String   address  ; 
int   port  ; 
String  [  ]  candidateAddrs  ; 
String   emsg  ; 
StopWatch   sw  ; 
printWithThread  (  "openServerSocket() entered"  )  ; 
if  (  isTls  (  )  )  { 
printWithThread  (  "Requesting TLS/SSL-encrypted JDBC"  )  ; 
} 
sw  =  new   StopWatch  (  )  ; 
socketFactory  =  HsqlSocketFactory  .  getInstance  (  isTls  (  )  )  ; 
address  =  getAddress  (  )  ; 
port  =  getPort  (  )  ; 
if  (  org  .  hsqldb  .  lib  .  StringUtil  .  isEmpty  (  address  )  ||  ServerConstants  .  SC_DEFAULT_ADDRESS  .  equalsIgnoreCase  (  address  .  trim  (  )  )  )  { 
socket  =  socketFactory  .  createServerSocket  (  port  )  ; 
}  else  { 
try  { 
socket  =  socketFactory  .  createServerSocket  (  port  ,  address  )  ; 
}  catch  (  UnknownHostException   e  )  { 
candidateAddrs  =  ServerConfiguration  .  listLocalInetAddressNames  (  )  ; 
int   messageID  ; 
Object  [  ]  messageParameters  ; 
if  (  candidateAddrs  .  length  >  0  )  { 
messageID  =  Trace  .  Server_openServerSocket  ; 
messageParameters  =  new   Object  [  ]  {  address  ,  candidateAddrs  }  ; 
}  else  { 
messageID  =  Trace  .  Server_openServerSocket2  ; 
messageParameters  =  new   Object  [  ]  {  address  }  ; 
} 
throw   new   UnknownHostException  (  Trace  .  getMessage  (  messageID  ,  true  ,  messageParameters  )  )  ; 
} 
} 
socket  .  setSoTimeout  (  1000  )  ; 
printWithThread  (  "Got server socket: "  +  socket  )  ; 
print  (  sw  .  elapsedTimeToMessage  (  "Server socket opened successfully"  )  )  ; 
if  (  socketFactory  .  isSecure  (  )  )  { 
print  (  "Using TLS/SSL-encrypted JDBC"  )  ; 
} 
printWithThread  (  "openServerSocket() exiting"  )  ; 
} 


private   void   printServerOnlineMessage  (  )  { 
String   s  =  getProductName  (  )  +  " "  +  getProductVersion  (  )  +  " is online"  ; 
printWithTimestamp  (  s  )  ; 
printResource  (  "online.help"  )  ; 
} 




protected   void   printProperties  (  )  { 
Enumeration   e  ; 
String   key  ; 
String   value  ; 
if  (  isSilent  (  )  )  { 
return  ; 
} 
e  =  serverProperties  .  propertyNames  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
key  =  (  String  )  e  .  nextElement  (  )  ; 
value  =  serverProperties  .  getProperty  (  key  )  ; 
printWithThread  (  key  +  "="  +  value  )  ; 
} 
} 







private   void   releaseServerSocket  (  )  { 
printWithThread  (  "releaseServerSocket() entered"  )  ; 
if  (  socket  !=  null  )  { 
printWithThread  (  "Releasing server socket: ["  +  socket  +  "]"  )  ; 
setState  (  ServerConstants  .  SERVER_STATE_CLOSING  )  ; 
try  { 
socket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
printError  (  "Exception closing server socket"  )  ; 
printError  (  "releaseServerSocket(): "  +  e  )  ; 
} 
socket  =  null  ; 
} 
printWithThread  (  "releaseServerSocket() exited"  )  ; 
} 









private   void   run  (  )  { 
StopWatch   sw  ; 
ThreadGroup   tg  ; 
String   tgName  ; 
printWithThread  (  "run() entered"  )  ; 
print  (  "Initiating startup sequence..."  )  ; 
printProperties  (  )  ; 
sw  =  new   StopWatch  (  )  ; 
setServerError  (  null  )  ; 
try  { 
openServerSocket  (  )  ; 
}  catch  (  Exception   e  )  { 
setServerError  (  e  )  ; 
printError  (  "run()/openServerSocket(): "  )  ; 
printStackTrace  (  e  )  ; 
shutdown  (  true  )  ; 
return  ; 
} 
tgName  =  "HSQLDB Connections @"  +  Integer  .  toString  (  this  .  hashCode  (  )  ,  16  )  ; 
tg  =  new   ThreadGroup  (  tgName  )  ; 
tg  .  setDaemon  (  false  )  ; 
serverConnectionThreadGroup  =  tg  ; 
if  (  openDatabases  (  )  ==  false  )  { 
setServerError  (  null  )  ; 
printError  (  "Shutting down because there are no open databases"  )  ; 
shutdown  (  true  )  ; 
return  ; 
} 
setState  (  ServerConstants  .  SERVER_STATE_ONLINE  )  ; 
print  (  sw  .  elapsedTimeToMessage  (  "Startup sequence completed"  )  )  ; 
printServerOnlineMessage  (  )  ; 
try  { 
while  (  true  )  { 
try  { 
handleConnection  (  socket  .  accept  (  )  )  ; 
}  catch  (  java  .  io  .  InterruptedIOException   e  )  { 
} 
} 
}  catch  (  IOException   e  )  { 
if  (  getState  (  )  ==  ServerConstants  .  SERVER_STATE_ONLINE  )  { 
setServerError  (  e  )  ; 
printError  (  this  +  ".run()/handleConnection(): "  )  ; 
printStackTrace  (  e  )  ; 
} 
}  catch  (  Throwable   t  )  { 
printWithThread  (  t  .  toString  (  )  )  ; 
}  finally  { 
shutdown  (  false  )  ; 
} 
} 






protected   void   setServerError  (  Throwable   t  )  { 
serverError  =  t  ; 
} 




public   void   shutdown  (  )  { 
shutdown  (  false  )  ; 
} 







protected   synchronized   void   shutdown  (  boolean   error  )  { 
if  (  serverState  ==  ServerConstants  .  SERVER_STATE_SHUTDOWN  )  { 
return  ; 
} 
StopWatch   sw  ; 
printWithThread  (  "shutdown() entered"  )  ; 
sw  =  new   StopWatch  (  )  ; 
print  (  "Initiating shutdown sequence..."  )  ; 
releaseServerSocket  (  )  ; 
DatabaseManager  .  deRegisterServer  (  this  )  ; 
if  (  dbPath  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  dbPath  .  length  ;  i  ++  )  { 
releaseDatabase  (  dbID  [  i  ]  )  ; 
} 
} 
if  (  serverConnectionThreadGroup  !=  null  )  { 
if  (  !  serverConnectionThreadGroup  .  isDestroyed  (  )  )  { 
for  (  int   i  =  0  ;  serverConnectionThreadGroup  .  activeCount  (  )  >  0  ;  i  ++  )  { 
int   count  ; 
try  { 
Thread  .  sleep  (  100  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
try  { 
serverConnectionThreadGroup  .  destroy  (  )  ; 
printWithThread  (  serverConnectionThreadGroup  .  getName  (  )  +  " destroyed"  )  ; 
}  catch  (  Throwable   t  )  { 
printWithThread  (  serverConnectionThreadGroup  .  getName  (  )  +  " not destroyed"  )  ; 
printWithThread  (  t  .  toString  (  )  )  ; 
} 
} 
serverConnectionThreadGroup  =  null  ; 
} 
serverThread  =  null  ; 
setState  (  ServerConstants  .  SERVER_STATE_SHUTDOWN  )  ; 
print  (  sw  .  elapsedTimeToMessage  (  "Shutdown sequence completed"  )  )  ; 
if  (  isNoSystemExit  (  )  )  { 
printWithTimestamp  (  "SHUTDOWN : System.exit() was not called"  )  ; 
printWithThread  (  "shutdown() exited"  )  ; 
}  else  { 
printWithTimestamp  (  "SHUTDOWN : System.exit() is called next"  )  ; 
printWithThread  (  "shutdown() exiting..."  )  ; 
try  { 
System  .  exit  (  0  )  ; 
}  catch  (  Throwable   t  )  { 
printWithThread  (  t  .  toString  (  )  )  ; 
} 
} 
} 












protected   static   void   printHelp  (  String   key  )  { 
System  .  out  .  print  (  BundleHandler  .  getString  (  serverBundleHandle  ,  key  )  )  ; 
} 
} 

