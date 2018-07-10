package   com  .  speed  .  irc  .  connection  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   java  .  util  .  concurrent  .  ScheduledExecutorService  ; 
import   java  .  util  .  concurrent  .  ScheduledThreadPoolExecutor  ; 
import   java  .  util  .  concurrent  .  TimeUnit  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   com  .  speed  .  irc  .  event  .  ApiEvent  ; 
import   com  .  speed  .  irc  .  event  .  EventManager  ; 
import   com  .  speed  .  irc  .  types  .  CTCPReply  ; 
import   com  .  speed  .  irc  .  types  .  Channel  ; 
import   com  .  speed  .  irc  .  types  .  NOTICE  ; 






















public   class   Server   implements   ConnectionHandler  ,  Runnable  { 

private   volatile   BufferedWriter   write  ; 

private   volatile   BufferedReader   read  ; 

protected   volatile   Socket   socket  ; 

protected   EventManager   eventManager  =  new   EventManager  (  )  ; 

protected   Map  <  String  ,  Channel  >  channels  =  new   HashMap  <  String  ,  Channel  >  (  )  ; 

private   char  [  ]  modeSymbols  ; 

private   char  [  ]  modeLetters  ; 

private   String   serverName  ; 

private   String   nick  ; 

private   ServerMessageParser   parser  ; 

protected   HashSet  <  CTCPReply  >  ctcpReplies  =  new   HashSet  <  CTCPReply  >  (  )  ; 

protected   boolean   autoConnect  ; 

private   int   port  ; 

private   ScheduledThreadPoolExecutor   chanExec  ; 

private   ScheduledExecutorService   serverExecutor  ,  eventExecutor  ; 

public   Server  (  final   Socket   sock  )  throws   IOException  { 
socket  =  sock  ; 
port  =  sock  .  getPort  (  )  ; 
setServerName  (  socket  .  getInetAddress  (  )  .  getHostAddress  (  )  )  ; 
write  =  new   BufferedWriter  (  new   OutputStreamWriter  (  sock  .  getOutputStream  (  )  )  )  ; 
read  =  new   BufferedReader  (  new   InputStreamReader  (  sock  .  getInputStream  (  )  )  )  ; 
chanExec  =  new   ScheduledThreadPoolExecutor  (  10  )  ; 
serverExecutor  =  Executors  .  newSingleThreadScheduledExecutor  (  )  ; 
eventExecutor  =  Executors  .  newSingleThreadScheduledExecutor  (  )  ; 
serverExecutor  .  scheduleWithFixedDelay  (  this  ,  1000  ,  200  ,  TimeUnit  .  MILLISECONDS  )  ; 
eventExecutor  .  scheduleWithFixedDelay  (  eventManager  ,  1000  ,  100  ,  TimeUnit  .  MILLISECONDS  )  ; 
parser  =  new   ServerMessageParser  (  this  )  ; 
ctcpReplies  .  add  (  ServerMessageParser  .  CTCP_REPLY_VERSION  )  ; 
ctcpReplies  .  add  (  ServerMessageParser  .  CTCP_REPLY_TIME  )  ; 
ctcpReplies  .  add  (  ServerMessageParser  .  CTCP_REPLY_PING  )  ; 
} 

public   ScheduledThreadPoolExecutor   getChanExec  (  )  { 
return   chanExec  ; 
} 





public   void   quit  (  )  { 
quit  (  null  )  ; 
} 








public   void   quit  (  final   String   message  )  { 
eventManager  .  fireEvent  (  new   ApiEvent  (  ApiEvent  .  SERVER_QUIT  ,  this  ,  this  )  )  ; 
try  { 
if  (  !  socket  .  isClosed  (  )  )  { 
getWriter  (  )  .  write  (  "QUIT"  +  (  message  ==  null  ||  message  .  trim  (  )  .  isEmpty  (  )  ?  "\n"  :  (  "Quit :"  +  message  +  "\n"  )  )  )  ; 
getWriter  (  )  .  flush  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
try  { 
Thread  .  sleep  (  100  )  ; 
}  catch  (  InterruptedException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
for  (  Channel   c  :  channels  .  values  (  )  )  { 
if  (  c  .  getFuture  (  )  !=  null  &&  !  c  .  getFuture  (  )  .  isDone  (  )  )  c  .  getFuture  (  )  .  cancel  (  true  )  ; 
} 
parser  .  reader  .  running  =  false  ; 
try  { 
socket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
eventExecutor  .  shutdownNow  (  )  ; 
parser  .  execServ  .  shutdownNow  (  )  ; 
chanExec  .  shutdownNow  (  )  ; 
serverExecutor  .  shutdownNow  (  )  ; 
} 

public   final   void   setReadDebug  (  final   Logger   logger  )  { 
parser  .  reader  .  logger  =  logger  ; 
parser  .  reader  .  logging  =  true  ; 
} 

public   final   void   setReadDebug  (  boolean   on  )  { 
parser  .  reader  .  logging  =  on  ; 
} 

protected   final   void   connect  (  )  { 
try  { 
socket  =  new   Socket  (  serverName  ,  port  )  ; 
write  =  new   BufferedWriter  (  new   OutputStreamWriter  (  socket  .  getOutputStream  (  )  )  )  ; 
read  =  new   BufferedReader  (  new   InputStreamReader  (  socket  .  getInputStream  (  )  )  )  ; 
Logger   logger  =  null  ; 
boolean   log  =  false  ; 
if  (  parser  .  reader  .  logging  )  { 
logger  =  parser  .  reader  .  logger  ; 
log  =  parser  .  reader  .  logging  ; 
} 
parser  =  new   ServerMessageParser  (  this  )  ; 
if  (  logger  !=  null  &&  log  )  { 
setReadDebug  (  logger  )  ; 
} 
}  catch  (  UnknownHostException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   ServerMessageParser   getParser  (  )  { 
return   parser  ; 
} 







public   void   setAutoReconnect  (  final   boolean   on  )  { 
this  .  autoConnect  =  on  ; 
} 






public   String   getNick  (  )  { 
return   nick  ; 
} 









public   void   setCtcpReply  (  final   String   request  ,  final   String   reply  )  { 
synchronized  (  ctcpReplies  )  { 
ctcpReplies  .  add  (  new   CTCPReply  (  )  { 

public   String   getReply  (  )  { 
return   reply  ; 
} 

public   String   getRequest  (  )  { 
return   request  ; 
} 
}  )  ; 
} 
} 

public   void   addCtcpReply  (  final   CTCPReply   reply  )  { 
synchronized  (  ctcpReplies  )  { 
ctcpReplies  .  add  (  reply  )  ; 
} 
} 

public   String   getCtcpReply  (  final   String   request  )  { 
synchronized  (  ctcpReplies  )  { 
for  (  CTCPReply   reply  :  ctcpReplies  )  { 
Matcher   matcher  =  Pattern  .  compile  (  reply  .  getRequest  (  )  ,  Pattern  .  CASE_INSENSITIVE  )  .  matcher  (  request  )  ; 
if  (  matcher  .  matches  (  )  )  { 
if  (  matcher  .  groupCount  (  )  ==  0  )  return   reply  .  getReply  (  )  ;  else  { 
String   resp  =  reply  .  getReply  (  )  ; 
StringBuffer   response  =  new   StringBuffer  (  )  ; 
boolean   flag  =  false  ; 
for  (  int   i  =  0  ;  i  <  resp  .  length  (  )  ;  i  ++  )  { 
char   c  =  resp  .  charAt  (  i  )  ; 
if  (  c  ==  '$'  &&  (  i  ==  0  ||  resp  .  charAt  (  i  -  1  )  !=  '\\'  )  )  { 
flag  =  true  ; 
continue  ; 
}  else   if  (  resp  .  charAt  (  i  -  1  )  ==  '\\'  )  { 
response  .  deleteCharAt  (  i  -  1  )  ; 
}  else   if  (  Character  .  isDigit  (  c  )  &&  flag  )  { 
int   group  =  Character  .  getNumericValue  (  c  )  ; 
flag  =  false  ; 
try  { 
String   str  =  matcher  .  group  (  group  )  ; 
response  .  append  (  str  )  ; 
}  catch  (  IndexOutOfBoundsException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
continue  ; 
} 
response  .  append  (  c  )  ; 
} 
return   response  .  toString  (  )  ; 
} 
} 
} 
} 
return   null  ; 
} 







public   void   sendRaw  (  String   raw  )  { 
if  (  raw  .  startsWith  (  "NICK"  )  )  { 
nick  =  raw  .  replace  (  "NICK"  ,  ""  )  .  replace  (  ":"  ,  ""  )  .  trim  (  )  ; 
} 
if  (  (  raw  .  contains  (  "\n"  )  ||  raw  .  contains  (  "\r"  )  )  &&  !  raw  .  endsWith  (  "\r\n"  )  )  raw  =  raw  .  replace  (  "\n"  ,  ""  )  .  replace  (  "\r"  ,  ""  )  ; 
if  (  !  raw  .  endsWith  (  "\r\n"  )  )  raw  +=  "\r\n"  ; 
try  { 
write  .  write  (  raw  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 






public   Map  <  String  ,  Channel  >  getChannels  (  )  { 
return   channels  ; 
} 






public   BufferedWriter   getWriter  (  )  { 
return   write  ; 
} 







public   void   setWrite  (  final   BufferedWriter   write  )  { 
this  .  write  =  write  ; 
} 






public   BufferedReader   getReader  (  )  { 
return   read  ; 
} 







public   void   setRead  (  final   BufferedReader   read  )  { 
this  .  read  =  read  ; 
} 







public   boolean   isConnected  (  )  { 
return  !  socket  .  isClosed  (  )  ; 
} 






public   char  [  ]  getModeSymbols  (  )  { 
return   modeSymbols  ; 
} 

protected   void   setModeSymbols  (  final   char  [  ]  modeSymbols  )  { 
this  .  modeSymbols  =  modeSymbols  ; 
} 






public   char  [  ]  getModeLetters  (  )  { 
return   modeLetters  ; 
} 

protected   void   setModeLetters  (  final   char  [  ]  modeLetters  )  { 
this  .  modeLetters  =  modeLetters  ; 
} 







public   void   sendNotice  (  final   NOTICE   notice  )  { 
sendRaw  (  "NOTICE "  +  notice  .  getChannel  (  )  +  " :"  +  notice  .  getMessage  (  )  +  "\n"  )  ; 
} 










public   void   sendAction  (  final   String   channel  ,  final   String   action  )  { 
sendRaw  (  "PRIVMSG "  +  channel  +  ": ACTION "  +  action  +  "\n"  )  ; 
} 

public   EventManager   getEventManager  (  )  { 
return   eventManager  ; 
} 

public   void   run  (  )  { 
try  { 
if  (  write  !=  null  )  { 
write  .  flush  (  )  ; 
} 
}  catch  (  SocketException   e  )  { 
if  (  autoConnect  )  { 
try  { 
Thread  .  sleep  (  5000  )  ; 
}  catch  (  InterruptedException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
try  { 
socket  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
connect  (  )  ; 
eventManager  .  fireEvent  (  new   ApiEvent  (  ApiEvent  .  SERVER_DISCONNECTED  ,  this  ,  this  )  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   setServerName  (  String   serverName  )  { 
this  .  serverName  =  serverName  ; 
} 






public   String   getServerName  (  )  { 
return   serverName  ; 
} 








public   Channel   joinChannel  (  final   String   channelName  )  { 
if  (  channels  .  containsKey  (  channelName  .  trim  (  )  )  )  { 
final   Channel   channel  =  channels  .  get  (  channelName  )  ; 
if  (  !  channel  .  isRunning  )  { 
channel  .  join  (  )  ; 
} 
return   channel  ; 
} 
final   Channel   channel  =  new   Channel  (  channelName  ,  this  )  ; 
channel  .  join  (  )  ; 
return   channel  ; 
} 
} 

