package   net  .  jini  .  jeri  .  ssl  ; 

import   com  .  sun  .  jini  .  action  .  GetLongAction  ; 
import   com  .  sun  .  jini  .  logging  .  Levels  ; 
import   com  .  sun  .  jini  .  logging  .  LogUtil  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketAddress  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  SocketTimeoutException  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  nio  .  channels  .  SocketChannel  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  net  .  SocketFactory  ; 
import   javax  .  net  .  ssl  .  SSLContext  ; 
import   javax  .  net  .  ssl  .  SSLException  ; 
import   javax  .  net  .  ssl  .  SSLProtocolException  ; 
import   javax  .  net  .  ssl  .  SSLSession  ; 
import   javax  .  net  .  ssl  .  SSLSocket  ; 
import   javax  .  net  .  ssl  .  SSLSocketFactory  ; 
import   javax  .  security  .  auth  .  x500  .  X500Principal  ; 
import   net  .  jini  .  core  .  constraint  .  InvocationConstraints  ; 
import   net  .  jini  .  io  .  UnsupportedConstraintException  ; 
import   net  .  jini  .  jeri  .  connection  .  Connection  ; 
import   net  .  jini  .  jeri  .  connection  .  OutboundRequestHandle  ; 
import   net  .  jini  .  security  .  Security  ; 






class   SslConnection   extends   Utilities   implements   Connection  { 







private   static   long   maxClientSessionDuration  =  (  (  Long  )  Security  .  doPrivileged  (  new   GetLongAction  (  "com.sun.jini.jeri.ssl.maxClientSessionDuration"  ,  (  long  )  (  23.5  *  60  *  60  *  1000  )  )  )  )  .  longValue  (  )  ; 


private   static   final   Logger   logger  =  clientLogger  ; 


final   String   serverHost  ; 


final   int   port  ; 





final   SocketFactory   socketFactory  ; 


final   CallContext   callContext  ; 





private   final   SSLContext   sslContext  ; 


final   SSLSocketFactory   sslSocketFactory  ; 


private   final   ClientAuthManager   authManager  ; 


SSLSocket   sslSocket  ; 


private   String   activeCipherSuite  ; 


private   SSLSession   session  ; 


boolean   closed  ; 













SslConnection  (  CallContext   callContext  ,  String   serverHost  ,  int   port  ,  SocketFactory   socketFactory  )  { 
this  .  serverHost  =  serverHost  ; 
this  .  port  =  port  ; 
this  .  socketFactory  =  socketFactory  ; 
if  (  callContext  ==  null  )  { 
throw   new   NullPointerException  (  "Call context cannot be null"  )  ; 
} 
this  .  callContext  =  callContext  ; 
SSLContextInfo   info  =  getClientSSLContextInfo  (  callContext  )  ; 
sslContext  =  info  .  sslContext  ; 
sslSocketFactory  =  sslContext  .  getSocketFactory  (  )  ; 
authManager  =  (  ClientAuthManager  )  info  .  authManager  ; 
} 













final   void   establishCallContext  (  )  throws   IOException  { 
Exception   exception  ; 
try  { 
establishNewSocket  (  )  ; 
if  (  callContext  .  clientAuthRequired  &&  !  authManager  .  getClientAuthenticated  (  )  )  { 
Exception   credExcept  =  authManager  .  getClientCredentialException  (  )  ; 
SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
try  { 
sm  .  checkPermission  (  getSubjectPermission  )  ; 
}  catch  (  SecurityException   e  )  { 
credExcept  =  null  ; 
} 
} 
if  (  credExcept   instanceof   SecurityException  )  { 
exception  =  (  SecurityException  )  credExcept  ; 
}  else  { 
exception  =  new   UnsupportedConstraintException  (  "Client not authenticated"  ,  credExcept  )  ; 
} 
}  else  { 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "new connection for {0}\ncreates {1}"  ,  new   Object  [  ]  {  callContext  ,  this  }  )  ; 
} 
return  ; 
} 
}  catch  (  SSLProtocolException   e  )  { 
exception  =  e  ; 
}  catch  (  SSLException   e  )  { 
exception  =  new   UnsupportedConstraintException  (  e  .  getMessage  (  )  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
exception  =  e  ; 
}  catch  (  SecurityException   e  )  { 
exception  =  e  ; 
} 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "establishCallContext"  ,  "new connection for {0}\nthrows"  ,  new   Object  [  ]  {  callContext  }  ,  exception  )  ; 
} 
closeSocket  (  )  ; 
if  (  exception   instanceof   IOException  )  { 
throw  (  IOException  )  exception  ; 
}  else  { 
throw  (  SecurityException  )  exception  ; 
} 
} 


private   void   closeSocket  (  )  { 
if  (  sslSocket  !=  null  )  { 
try  { 
sslSocket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
sslSocket  =  null  ; 
session  =  null  ; 
activeCipherSuite  =  null  ; 
} 
} 









void   establishNewSocket  (  )  throws   IOException  { 
Socket   socket  =  createPlainSocket  (  serverHost  ,  port  )  ; 
sslSocket  =  (  SSLSocket  )  sslSocketFactory  .  createSocket  (  socket  ,  serverHost  ,  port  ,  true  )  ; 
establishSuites  (  )  ; 
} 









final   void   establishSuites  (  )  throws   IOException  { 
sslSocket  .  setEnabledCipherSuites  (  callContext  .  cipherSuites  )  ; 
sslSocket  .  startHandshake  (  )  ; 
session  =  sslSocket  .  getSession  (  )  ; 
activeCipherSuite  =  session  .  getCipherSuite  (  )  ; 
sslSocket  .  setEnableSessionCreation  (  false  )  ; 
releaseClientSSLContextInfo  (  callContext  ,  sslContext  ,  authManager  )  ; 
} 





final   Socket   createPlainSocket  (  String   host  ,  int   port  )  throws   IOException  { 
Socket   socket  ; 
if  (  !  callContext  .  endpointImpl  .  disableSocketConnect  )  { 
socket  =  connectToHost  (  host  ,  port  ,  callContext  .  connectionTime  )  ; 
}  else  { 
socket  =  newSocket  (  )  ; 
} 
return   socket  ; 
} 

private   static   int   computeTimeout  (  long   connectionTime  )  throws   IOException  { 
int   timeout  ; 
long   current  =  System  .  currentTimeMillis  (  )  ; 
if  (  connectionTime  ==  -  1  )  { 
timeout  =  0  ; 
}  else   if  (  connectionTime  <  current  )  { 
throw   new   IOException  (  "Connection not made within specified time"  )  ; 
}  else   if  (  connectionTime  -  current  >  Integer  .  MAX_VALUE  )  { 
timeout  =  0  ; 
}  else  { 
timeout  =  (  int  )  (  connectionTime  -  current  )  ; 
} 
return   timeout  ; 
} 







private   Socket   connectToHost  (  String   host  ,  int   port  ,  long   connectionTime  )  throws   IOException  { 
InetAddress  [  ]  addresses  ; 
try  { 
addresses  =  InetAddress  .  getAllByName  (  host  )  ; 
}  catch  (  UnknownHostException   uhe  )  { 
try  { 
return   connectToSocketAddress  (  new   InetSocketAddress  (  host  ,  port  )  ,  connectionTime  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to unresolved host {0}"  ,  new   Object  [  ]  {  host  +  ":"  +  port  }  ,  e  )  ; 
} 
throw   e  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to unresolved host {0}"  ,  new   Object  [  ]  {  host  +  ":"  +  port  }  ,  e  )  ; 
} 
throw   e  ; 
} 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception resolving host {0}"  ,  new   Object  [  ]  {  host  }  ,  e  )  ; 
} 
throw   e  ; 
} 
IOException   lastIOException  =  null  ; 
SecurityException   lastSecurityException  =  null  ; 
for  (  int   i  =  0  ;  i  <  addresses  .  length  ;  i  ++  )  { 
SocketAddress   socketAddress  =  new   InetSocketAddress  (  addresses  [  i  ]  ,  port  )  ; 
try  { 
return   connectToSocketAddress  (  socketAddress  ,  connectionTime  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  socketAddress  }  ,  e  )  ; 
} 
lastIOException  =  e  ; 
if  (  e   instanceof   SocketTimeoutException  )  { 
break  ; 
} 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  socketAddress  }  ,  e  )  ; 
} 
lastSecurityException  =  e  ; 
} 
} 
if  (  lastIOException  !=  null  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  host  +  ":"  +  port  }  ,  lastIOException  )  ; 
} 
throw   lastIOException  ; 
} 
assert   lastSecurityException  !=  null  ; 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  SslConnection  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  host  +  ":"  +  port  }  ,  lastSecurityException  )  ; 
} 
throw   lastSecurityException  ; 
} 





private   Socket   connectToSocketAddress  (  SocketAddress   socketAddress  ,  long   connectionTime  )  throws   IOException  { 
int   timeout  =  computeTimeout  (  connectionTime  )  ; 
Socket   socket  =  newSocket  (  )  ; 
boolean   ok  =  false  ; 
try  { 
socket  .  connect  (  socketAddress  ,  timeout  )  ; 
ok  =  true  ; 
return   socket  ; 
}  finally  { 
if  (  !  ok  )  { 
try  { 
socket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 





private   Socket   newSocket  (  )  throws   IOException  { 
Socket   socket  =  socketFactory  !=  null  ?  socketFactory  .  createSocket  (  )  :  new   Socket  (  )  ; 
try  { 
socket  .  setTcpNoDelay  (  true  )  ; 
}  catch  (  SocketException   e  )  { 
} 
try  { 
socket  .  setKeepAlive  (  true  )  ; 
}  catch  (  SocketException   e  )  { 
} 
return   socket  ; 
} 


public   String   toString  (  )  { 
String   sessionString  =  (  session  ==  null  )  ?  ""  :  session  +  ", "  ; 
return   getClassName  (  this  )  +  "["  +  sessionString  +  (  sslSocket  ==  null  ?  "???"  :  Integer  .  toString  (  sslSocket  .  getLocalPort  (  )  )  )  +  "=>"  +  serverHost  +  ":"  +  port  +  "]"  ; 
} 

public   InputStream   getInputStream  (  )  throws   IOException  { 
if  (  sslSocket  !=  null  )  { 
return   sslSocket  .  getInputStream  (  )  ; 
}  else  { 
throw   new   IOException  (  "No socket established"  )  ; 
} 
} 

public   OutputStream   getOutputStream  (  )  throws   IOException  { 
if  (  sslSocket  !=  null  )  { 
return   sslSocket  .  getOutputStream  (  )  ; 
}  else  { 
throw   new   IOException  (  "No socket established"  )  ; 
} 
} 

public   SocketChannel   getChannel  (  )  { 
return   null  ; 
} 

public   void   populateContext  (  OutboundRequestHandle   handle  ,  Collection   context  )  { 
CallContext  .  coerce  (  handle  ,  callContext  .  endpoint  )  ; 
if  (  context  ==  null  )  { 
throw   new   NullPointerException  (  "Context cannot be null"  )  ; 
} 
} 

public   InvocationConstraints   getUnfulfilledConstraints  (  OutboundRequestHandle   handle  )  { 
CallContext   callContext  =  CallContext  .  coerce  (  handle  ,  this  .  callContext  .  endpoint  )  ; 
return   callContext  .  getUnfulfilledConstraints  (  )  ; 
} 

public   void   writeRequestData  (  OutboundRequestHandle   handle  ,  OutputStream   stream  )  { 
CallContext  .  coerce  (  handle  ,  callContext  .  endpoint  )  ; 
if  (  stream  ==  null  )  { 
throw   new   NullPointerException  (  "Stream cannot be null"  )  ; 
} 
} 

public   IOException   readResponseData  (  OutboundRequestHandle   handle  ,  InputStream   stream  )  { 
CallContext  .  coerce  (  handle  ,  callContext  .  endpoint  )  ; 
if  (  stream  ==  null  )  { 
throw   new   NullPointerException  (  "Stream cannot be null"  )  ; 
} 
return   null  ; 
} 

public   synchronized   void   close  (  )  throws   IOException  { 
if  (  !  closed  )  { 
logger  .  log  (  Level  .  FINE  ,  "closing {0}"  ,  this  )  ; 
closed  =  true  ; 
closeSocket  (  )  ; 
} 
} 





final   boolean   useFor  (  CallContext   otherCallContext  )  { 
assert   callContext  .  endpoint  .  equals  (  otherCallContext  .  endpoint  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINEST  )  )  { 
logger  .  log  (  Level  .  FINEST  ,  "try {0}\nwith {1}\nfor {2}"  ,  new   Object  [  ]  {  this  ,  callContext  ,  otherCallContext  }  )  ; 
} 
if  (  session  ==  null  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection {0} is not established"  ,  this  )  ; 
return   false  ; 
} 
if  (  checkSessionExpired  (  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "connection {0} session is expired"  ,  this  )  ; 
return   false  ; 
} 
if  (  callContext  .  clientSubject  !=  otherCallContext  .  clientSubject  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection has wrong subject"  )  ; 
return   false  ; 
} 
X500Principal   clientPrincipal  =  authManager  .  getClientPrincipal  (  )  ; 
if  (  clientPrincipal  ==  null  )  { 
if  (  otherCallContext  .  clientAuthRequired  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection has no client authentication"  )  ; 
return   false  ; 
} 
}  else   if  (  otherCallContext  .  clientPrincipals  !=  null  &&  !  otherCallContext  .  clientPrincipals  .  contains  (  clientPrincipal  )  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection has wrong client principal"  )  ; 
return   false  ; 
} 
X500Principal   serverPrincipal  =  authManager  .  getServerPrincipal  (  )  ; 
if  (  serverPrincipal  !=  null  &&  otherCallContext  .  serverPrincipals  !=  null  &&  !  otherCallContext  .  serverPrincipals  .  contains  (  serverPrincipal  )  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection has wrong server principal"  )  ; 
return   false  ; 
} 
String  [  ]  requestedSuites  =  otherCallContext  .  cipherSuites  ; 
int   requestedPos  =  position  (  activeCipherSuite  ,  requestedSuites  )  ; 
if  (  requestedPos  <  0  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection has wrong suite"  )  ; 
return   false  ; 
} 
String  [  ]  connectionSuites  =  callContext  .  cipherSuites  ; 
int   connectionPos  =  position  (  activeCipherSuite  ,  connectionSuites  )  ; 
assert   connectionPos  >=  0  :  "Couldn't find connection suite"  ; 
for  (  int   i  =  requestedPos  ;  --  i  >=  0  ;  )  { 
String   suite  =  requestedSuites  [  i  ]  ; 
int   p  =  position  (  suite  ,  connectionSuites  )  ; 
if  (  p  <  0  ||  p  >=  connectionPos  )  { 
logger  .  log  (  Level  .  FINEST  ,  "connection did not try all better suites"  )  ; 
return   false  ; 
} 
} 
if  (  clientPrincipal  !=  null  )  { 
Exception   exception  ; 
try  { 
authManager  .  checkAuthentication  (  )  ; 
exception  =  null  ; 
}  catch  (  SecurityException   e  )  { 
exception  =  e  ; 
}  catch  (  UnsupportedConstraintException   e  )  { 
exception  =  e  ; 
} 
if  (  exception  !=  null  )  { 
if  (  logger  .  isLoggable  (  Level  .  FINEST  )  )  { 
logThrow  (  logger  ,  Level  .  FINEST  ,  SslConnection  .  class  ,  "useFor"  ,  "connection {0} has missing subject credentials"  ,  new   Object  [  ]  {  this  }  ,  exception  )  ; 
} 
return   false  ; 
} 
} 
logger  .  log  (  Level  .  FINEST  ,  "connection OK"  )  ; 
return   true  ; 
} 






private   boolean   checkSessionExpired  (  )  { 
long   create  =  session  .  getCreationTime  (  )  ; 
long   expiration  =  create  +  maxClientSessionDuration  ; 
if  (  expiration  <  create  )  { 
expiration  =  Long  .  MAX_VALUE  ; 
} 
if  (  expiration  <=  System  .  currentTimeMillis  (  )  )  { 
session  .  invalidate  (  )  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 




protected   String   getProxyHost  (  )  { 
return  ""  ; 
} 

void   checkConnectPermission  (  )  { 
SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
Socket   socket  =  sslSocket  ; 
InetSocketAddress   address  =  (  InetSocketAddress  )  socket  .  getRemoteSocketAddress  (  )  ; 
if  (  address  .  isUnresolved  (  )  )  { 
sm  .  checkConnect  (  address  .  getHostName  (  )  ,  socket  .  getPort  (  )  )  ; 
}  else  { 
sm  .  checkConnect  (  address  .  getAddress  (  )  .  getHostAddress  (  )  ,  socket  .  getPort  (  )  )  ; 
} 
} 
} 
} 

