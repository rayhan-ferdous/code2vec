package   net  .  jini  .  jeri  .  kerberos  ; 

import   com  .  sun  .  jini  .  action  .  GetIntegerAction  ; 
import   com  .  sun  .  jini  .  discovery  .  internal  .  EndpointInternals  ; 
import   com  .  sun  .  jini  .  discovery  .  internal  .  KerberosEndpointInternalsAccess  ; 
import   com  .  sun  .  jini  .  jeri  .  internal  .  connection  .  BasicConnManagerFactory  ; 
import   com  .  sun  .  jini  .  jeri  .  internal  .  connection  .  ConnManager  ; 
import   com  .  sun  .  jini  .  jeri  .  internal  .  connection  .  ConnManagerFactory  ; 
import   com  .  sun  .  jini  .  jeri  .  internal  .  connection  .  ServerConnManager  ; 
import   com  .  sun  .  jini  .  jeri  .  internal  .  runtime  .  Util  ; 
import   com  .  sun  .  jini  .  logging  .  Levels  ; 
import   com  .  sun  .  jini  .  logging  .  LogUtil  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InvalidObjectException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  lang  .  ref  .  Reference  ; 
import   java  .  lang  .  ref  .  WeakReference  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketAddress  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  SocketTimeoutException  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  nio  .  channels  .  SocketChannel  ; 
import   java  .  security  .  AccessController  ; 
import   java  .  security  .  PrivilegedAction  ; 
import   java  .  security  .  PrivilegedActionException  ; 
import   java  .  security  .  PrivilegedExceptionAction  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  WeakHashMap  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  net  .  SocketFactory  ; 
import   javax  .  security  .  auth  .  Subject  ; 
import   javax  .  security  .  auth  .  kerberos  .  KerberosPrincipal  ; 
import   javax  .  security  .  auth  .  kerberos  .  KerberosTicket  ; 
import   net  .  jini  .  core  .  constraint  .  ConnectionAbsoluteTime  ; 
import   net  .  jini  .  core  .  constraint  .  ConstraintAlternatives  ; 
import   net  .  jini  .  core  .  constraint  .  Delegation  ; 
import   net  .  jini  .  core  .  constraint  .  Integrity  ; 
import   net  .  jini  .  core  .  constraint  .  InvocationConstraint  ; 
import   net  .  jini  .  core  .  constraint  .  InvocationConstraints  ; 
import   net  .  jini  .  io  .  UnsupportedConstraintException  ; 
import   net  .  jini  .  jeri  .  Endpoint  ; 
import   net  .  jini  .  jeri  .  OutboundRequestIterator  ; 
import   net  .  jini  .  jeri  .  ServerEndpoint  ; 
import   net  .  jini  .  jeri  .  connection  .  Connection  ; 
import   net  .  jini  .  jeri  .  connection  .  ConnectionEndpoint  ; 
import   net  .  jini  .  jeri  .  connection  .  OutboundRequestHandle  ; 
import   net  .  jini  .  jeri  .  kerberos  .  KerberosUtil  .  Config  ; 
import   net  .  jini  .  jeri  .  kerberos  .  KerberosUtil  .  ConfigIter  ; 
import   net  .  jini  .  security  .  AuthenticationPermission  ; 
import   net  .  jini  .  security  .  Security  ; 
import   net  .  jini  .  security  .  proxytrust  .  TrustEquivalence  ; 
import   org  .  ietf  .  jgss  .  GSSContext  ; 
import   org  .  ietf  .  jgss  .  GSSCredential  ; 
import   org  .  ietf  .  jgss  .  GSSException  ; 
import   org  .  ietf  .  jgss  .  GSSManager  ; 
import   org  .  ietf  .  jgss  .  GSSName  ; 























































































































































































public   final   class   KerberosEndpoint   implements   Endpoint  ,  TrustEquivalence  ,  Serializable  { 

private   static   final   long   serialVersionUID  =  -  880347439811805543L  ; 


private   static   final   Logger   logger  =  Logger  .  getLogger  (  "net.jini.jeri.kerberos.client"  )  ; 






private   final   String   serverHost  ; 






private   final   int   serverPort  ; 






private   final   KerberosPrincipal   serverPrincipal  ; 







private   final   SocketFactory   csf  ; 


private   static   final   Object   classLock  =  new   Object  (  )  ; 


private   static   GSSManager   gssManager  ; 





private   static   final   int   maxCacheSize  =  (  (  Integer  )  AccessController  .  doPrivileged  (  new   GetIntegerAction  (  "com.sun.jini.jeri.kerberos.KerberosEndpoint.maxCacheSize"  ,  64  )  )  )  .  intValue  (  )  ; 






private   static   final   int   minGssContextLifetime  =  (  (  Integer  )  AccessController  .  doPrivileged  (  new   GetIntegerAction  (  "com.sun.jini.jeri.kerberos.KerberosEndpoint."  +  "minGssContextLifetime"  ,  30  )  )  )  .  intValue  (  )  ; 


private   static   final   int   maxGssContextRetries  =  (  (  Integer  )  AccessController  .  doPrivileged  (  new   GetIntegerAction  (  "com.sun.jini.jeri.kerberos.KerberosEndpoint."  +  "maxGssContextRetries"  ,  3  )  )  )  .  intValue  (  )  ; 








private   transient   KerberosUtil  .  SoftCache   softCache  ; 





private   transient   ConnectionEndpointImpl   connectionEndpoint  ; 




private   transient   ConnManager   connManager  ; 





private   transient   boolean   disableSocketConnect  ; 





private   static   final   Map   internTable  =  new   WeakHashMap  (  5  )  ; 

static  { 
KerberosEndpointInternals  .  registerDiscoveryBackDoor  (  )  ; 
} 





















private   KerberosEndpoint  (  String   serverHost  ,  int   serverPort  ,  KerberosPrincipal   serverPrincipal  ,  SocketFactory   csf  )  { 
if  (  serverHost  ==  null  )  throw   new   NullPointerException  (  "serverHost is null"  )  ; 
if  (  serverPort  <=  0  ||  serverPort  >  0xFFFF  )  { 
throw   new   IllegalArgumentException  (  "server port number out of range 1-65535: serverPort = "  +  serverPort  )  ; 
} 
if  (  serverPrincipal  ==  null  )  throw   new   NullPointerException  (  "serverPrincipal is null"  )  ; 
this  .  serverHost  =  serverHost  ; 
this  .  serverPort  =  serverPort  ; 
this  .  serverPrincipal  =  serverPrincipal  ; 
this  .  csf  =  csf  ; 
logger  .  log  (  Level  .  FINE  ,  "created {0}"  ,  this  )  ; 
} 




















public   static   KerberosEndpoint   getInstance  (  String   serverHost  ,  int   serverPort  ,  KerberosPrincipal   serverPrincipal  )  { 
return   intern  (  new   KerberosEndpoint  (  serverHost  ,  serverPort  ,  serverPrincipal  ,  null  )  )  ; 
} 





























public   static   KerberosEndpoint   getInstance  (  String   serverHost  ,  int   serverPort  ,  KerberosPrincipal   serverPrincipal  ,  SocketFactory   csf  )  { 
return   intern  (  new   KerberosEndpoint  (  serverHost  ,  serverPort  ,  serverPrincipal  ,  csf  )  )  ; 
} 






public   String   getHost  (  )  { 
return   serverHost  ; 
} 






public   int   getPort  (  )  { 
return   serverPort  ; 
} 







public   KerberosPrincipal   getPrincipal  (  )  { 
return   serverPrincipal  ; 
} 








public   SocketFactory   getSocketFactory  (  )  { 
return   csf  ; 
} 





































































































public   OutboundRequestIterator   newRequest  (  InvocationConstraints   constraints  )  { 
if  (  constraints  ==  null  )  throw   new   NullPointerException  (  "constraints cannot be null"  )  ; 
logger  .  log  (  Level  .  FINE  ,  "newRequest requested with constraints:\n"  +  "{0}"  ,  constraints  )  ; 
Subject   clientSubject  =  (  Subject  )  Security  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   Subject  .  getSubject  (  AccessController  .  getContext  (  )  )  ; 
} 
}  )  ; 
CacheKey   key  =  new   CacheKey  (  clientSubject  ,  constraints  )  ; 
RequestHandleImpl   handle  =  (  RequestHandleImpl  )  softCache  .  get  (  key  )  ; 
if  (  handle  ==  null  ||  !  handle  .  reusable  (  clientSubject  )  )  { 
handle  =  new   RequestHandleImpl  (  clientSubject  ,  constraints  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "new request handle has been "  +  "constructed:\n{0}"  ,  new   Object  [  ]  {  handle  }  )  ; 
} 
softCache  .  put  (  key  ,  handle  )  ; 
} 
return   connManager  .  newRequest  (  handle  )  ; 
} 








public   boolean   checkTrustEquivalence  (  Object   obj  )  { 
return   equals  (  obj  )  ; 
} 


public   int   hashCode  (  )  { 
return   getClass  (  )  .  getName  (  )  .  hashCode  (  )  ^  serverPrincipal  .  hashCode  (  )  ^  serverHost  .  hashCode  (  )  ^  serverPort  ^  (  csf  !=  null  ?  csf  .  hashCode  (  )  :  0  )  ; 
} 






public   boolean   equals  (  Object   obj  )  { 
if  (  obj  ==  this  )  { 
return   true  ; 
}  else   if  (  !  (  obj   instanceof   KerberosEndpoint  )  )  { 
return   false  ; 
} 
KerberosEndpoint   oep  =  (  KerberosEndpoint  )  obj  ; 
return   serverPrincipal  .  equals  (  oep  .  serverPrincipal  )  &&  serverHost  .  equals  (  oep  .  serverHost  )  &&  serverPort  ==  oep  .  serverPort  &&  Util  .  sameClassAndEquals  (  csf  ,  oep  .  csf  )  ; 
} 


public   String   toString  (  )  { 
return  "KerberosEndpoint[serverHost="  +  serverHost  +  " serverPort="  +  serverPort  +  " serverPrincipal="  +  serverPrincipal  +  (  csf  ==  null  ?  ""  :  "csf = "  +  csf  .  toString  (  )  )  +  "]"  ; 
} 


private   Object   readResolve  (  )  { 
return   intern  (  this  )  ; 
} 





private   void   readObject  (  ObjectInputStream   ois  )  throws   IOException  ,  ClassNotFoundException  { 
ois  .  defaultReadObject  (  )  ; 
if  (  serverHost  ==  null  )  { 
throw   new   InvalidObjectException  (  "serverHost is null"  )  ; 
}  else   if  (  serverPort  <=  0  ||  serverPort  >  0xFFFF  )  { 
throw   new   InvalidObjectException  (  "server port number out of range 1-65535: "  +  "serverPort = : "  +  serverPort  )  ; 
}  else   if  (  serverPrincipal  ==  null  )  { 
throw   new   InvalidObjectException  (  "serverPrincipal is null"  )  ; 
} 
} 


private   static   KerberosEndpoint   intern  (  KerberosEndpoint   endpoint  )  { 
synchronized  (  internTable  )  { 
Reference   ref  =  (  WeakReference  )  internTable  .  get  (  endpoint  )  ; 
if  (  ref  !=  null  )  { 
KerberosEndpoint   canonical  =  (  KerberosEndpoint  )  ref  .  get  (  )  ; 
if  (  canonical  !=  null  )  { 
return   canonical  ; 
} 
} 
endpoint  .  softCache  =  new   KerberosUtil  .  SoftCache  (  maxCacheSize  )  ; 
endpoint  .  connectionEndpoint  =  endpoint  .  new   ConnectionEndpointImpl  (  )  ; 
endpoint  .  connManager  =  new   BasicConnManagerFactory  (  )  .  create  (  endpoint  .  connectionEndpoint  )  ; 
internTable  .  put  (  endpoint  ,  new   WeakReference  (  endpoint  )  )  ; 
return   endpoint  ; 
} 
} 





private   void   checkEndpoint  (  KerberosEndpoint   ep  )  { 
if  (  !  this  .  equals  (  ep  )  )  { 
throw   new   IllegalArgumentException  (  "endpoint mismatch, this endpoint is: "  +  this  +  ", passed in endpoint is: "  +  ep  )  ; 
} 
} 





private   RequestHandleImpl   checkRequestHandleImpl  (  Object   h  )  { 
if  (  h  ==  null  )  { 
throw   new   NullPointerException  (  "Handle cannot be null"  )  ; 
}  else   if  (  !  (  h   instanceof   RequestHandleImpl  )  )  { 
throw   new   IllegalArgumentException  (  "Unexpected handle type: "  +  h  )  ; 
} 
RequestHandleImpl   rh  =  (  RequestHandleImpl  )  h  ; 
checkEndpoint  (  rh  .  getEndpoint  (  )  )  ; 
return   rh  ; 
} 





private   ConnectionImpl   checkConnection  (  Object   c  )  { 
if  (  !  (  c   instanceof   ConnectionImpl  )  )  { 
throw   new   IllegalArgumentException  (  "Expected connection type is "  +  ConnectionImpl  .  class  +  ", while "  +  c  +  " is passed in."  )  ; 
} 
ConnectionImpl   conn  =  (  ConnectionImpl  )  c  ; 
checkEndpoint  (  conn  .  getEndpoint  (  )  )  ; 
return   conn  ; 
} 


private   static   final   class   KerberosEndpointInternals   implements   EndpointInternals  { 


static   void   registerDiscoveryBackDoor  (  )  { 
final   KerberosEndpointInternals   backDoor  =  new   KerberosEndpointInternals  (  )  ; 
try  { 
Security  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
KerberosEndpointInternalsAccess  .  set  (  backDoor  )  ; 
return   null  ; 
} 
}  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Problem registering with discovery provider"  ,  t  )  ; 
} 
} 

public   void   disableSocketConnect  (  Endpoint   endpoint  )  { 
(  (  KerberosEndpoint  )  endpoint  )  .  disableSocketConnect  =  true  ; 
} 

public   void   setConnManagerFactory  (  Endpoint   endpoint  ,  ConnManagerFactory   factory  )  { 
KerberosEndpoint   kep  =  (  KerberosEndpoint  )  endpoint  ; 
kep  .  connManager  =  factory  .  create  (  kep  .  connectionEndpoint  )  ; 
} 

public   void   setServerConnManager  (  ServerEndpoint   endpoint  ,  ServerConnManager   manager  )  { 
KerberosServerEndpoint   ksep  =  (  KerberosServerEndpoint  )  endpoint  ; 
ksep  .  serverConnManager  =  manager  ; 
} 

public   InvocationConstraints   getUnfulfilledConstraints  (  OutboundRequestHandle   handle  )  { 
return  (  (  RequestHandleImpl  )  handle  )  .  unfulfilledConstraints  ; 
} 
} 

private   static   final   int   NO_ERROR  =  -  1  ; 

private   static   final   int   UNSUPPORTABLE_CONSTRAINT_REQUIRED  =  0  ; 

private   static   final   int   NULL_SUBJECT  =  1  ; 

private   static   final   int   NO_CLIENT_PRINCIPAL  =  2  ; 

private   static   final   int   UNSATISFIABLE_CONSTRAINT_REQUIRED  =  3  ; 

private   static   final   String  [  ]  ERROR_STRINGS  =  {  "UNSUPPORTABLE_CONSTRAINT_REQUIRED"  ,  "NULL_SUBJECT"  ,  "NO_CLIENT_PRINCIPAL"  ,  "UNSATISFIABLE_CONSTRAINT_REQUIRED"  }  ; 


private   final   class   RequestHandleImpl   implements   OutboundRequestHandle  { 


private   Subject   clientSubject  ; 


private   InvocationConstraints   constraints  ; 


private   boolean   subjectReadOnly  ; 




private   Set   subjectClientPrincipals  ; 





private   Set   clientPrincipals  ; 





private   int   errorCode  =  NO_ERROR  ; 








private   String   detailedExceptionMsg  ; 







private   Config  [  ]  configs  ; 





private   InvocationConstraints   unfulfilledConstraints  ; 


long   connectionAbsoluteTime  ; 






































RequestHandleImpl  (  Subject   clientSubject  ,  InvocationConstraints   constraints  )  { 
for  (  Iterator   iter  =  constraints  .  requirements  (  )  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
InvocationConstraint   c  =  (  InvocationConstraint  )  iter  .  next  (  )  ; 
if  (  !  KerberosUtil  .  isSupportableConstraint  (  c  )  )  { 
errorCode  =  UNSUPPORTABLE_CONSTRAINT_REQUIRED  ; 
detailedExceptionMsg  =  "A constraint unsupportable by "  +  "this endpoint has been required: "  +  c  ; 
return  ; 
} 
} 
clientPrincipals  =  new   HashSet  (  )  ; 
for  (  Iterator   iter  =  constraints  .  requirements  (  )  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
if  (  !  KerberosUtil  .  collectCpCandidates  (  (  InvocationConstraint  )  iter  .  next  (  )  ,  clientPrincipals  )  )  { 
errorCode  =  UNSUPPORTABLE_CONSTRAINT_REQUIRED  ; 
detailedExceptionMsg  =  "Client principal constraint "  +  "related conflicts found in the given set of "  +  "constraints: "  +  constraints  ; 
return  ; 
} 
} 
if  (  clientSubject  ==  null  )  { 
errorCode  =  NULL_SUBJECT  ; 
detailedExceptionMsg  =  "JAAS login has not been done "  +  "properly, the subject associated with the current "  +  "AccessControlContext is null."  ; 
return  ; 
} 
this  .  clientSubject  =  clientSubject  ; 
this  .  constraints  =  constraints  ; 
subjectReadOnly  =  clientSubject  .  isReadOnly  (  )  ; 
subjectClientPrincipals  =  getClientPrincipals  (  clientSubject  )  ; 
if  (  subjectClientPrincipals  .  size  (  )  ==  0  )  { 
errorCode  =  NO_CLIENT_PRINCIPAL  ; 
detailedExceptionMsg  =  "JAAS login has not been done "  +  "properly, the subject associated with the current "  +  "AccessControlContext contains no KerberosPrincipal."  ; 
return  ; 
} 
if  (  clientPrincipals  .  size  (  )  >  0  )  { 
clientPrincipals  .  retainAll  (  subjectClientPrincipals  )  ; 
}  else  { 
clientPrincipals  =  subjectClientPrincipals  ; 
} 
boolean   canDeleg  =  false  ; 
if  (  KerberosUtil  .  containsConstraint  (  constraints  .  requirements  (  )  ,  Delegation  .  YES  )  ||  KerberosUtil  .  containsConstraint  (  constraints  .  preferences  (  )  ,  Delegation  .  YES  )  )  { 
canDeleg  =  true  ; 
} 
ArrayList   configArr  =  new   ArrayList  (  )  ; 
outer  :  for  (  ConfigIter   citer  =  new   ConfigIter  (  clientPrincipals  ,  serverPrincipal  ,  canDeleg  )  ;  citer  .  hasNext  (  )  ;  )  { 
Config   config  =  citer  .  next  (  )  ; 
for  (  Iterator   jter  =  constraints  .  requirements  (  )  .  iterator  (  )  ;  jter  .  hasNext  (  )  ;  )  { 
InvocationConstraint   c  =  (  InvocationConstraint  )  jter  .  next  (  )  ; 
if  (  !  KerberosUtil  .  isSatisfiable  (  config  ,  c  )  )  continue   outer  ; 
} 
configArr  .  add  (  config  )  ; 
} 
if  (  configArr  .  size  (  )  ==  0  )  { 
errorCode  =  UNSATISFIABLE_CONSTRAINT_REQUIRED  ; 
detailedExceptionMsg  =  "Constraints unsatisfiable by this "  +  "endpoint with the current subject have been required: "  +  constraints  +  ", while the KerberosPrincipal set of "  +  "the subject is: "  +  subjectClientPrincipals  ; 
return  ; 
} 
configs  =  (  Config  [  ]  )  configArr  .  toArray  (  new   Config  [  configArr  .  size  (  )  ]  )  ; 
for  (  int   i  =  0  ;  i  <  configs  .  length  ;  i  ++  )  { 
for  (  Iterator   iter  =  constraints  .  preferences  (  )  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
InvocationConstraint   c  =  (  InvocationConstraint  )  iter  .  next  (  )  ; 
if  (  KerberosUtil  .  isSatisfiable  (  configs  [  i  ]  ,  c  )  )  configs  [  i  ]  .  prefCount  ++  ; 
} 
} 
Arrays  .  sort  (  configs  ,  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
Config   config1  =  (  Config  )  o1  ; 
Config   config2  =  (  Config  )  o2  ; 
return   config2  .  prefCount  -  config1  .  prefCount  ; 
} 
}  )  ; 
if  (  KerberosUtil  .  containsConstraint  (  constraints  .  requirements  (  )  ,  Integrity  .  YES  )  )  { 
unfulfilledConstraints  =  KerberosUtil  .  INTEGRITY_REQUIRED_CONSTRAINTS  ; 
}  else   if  (  KerberosUtil  .  containsConstraint  (  constraints  .  preferences  (  )  ,  Integrity  .  YES  )  )  { 
unfulfilledConstraints  =  KerberosUtil  .  INTEGRITY_PREFERRED_CONSTRAINTS  ; 
}  else  { 
unfulfilledConstraints  =  InvocationConstraints  .  EMPTY  ; 
} 
connectionAbsoluteTime  =  Math  .  min  (  computeConnectionTimeLimit  (  constraints  .  requirements  (  )  )  ,  computeConnectionTimeLimit  (  constraints  .  preferences  (  )  )  )  ; 
} 


public   String   toString  (  )  { 
StringBuffer   b  =  new   StringBuffer  (  "KerberosEndpoint.RequestHandleImpl[\n"  )  ; 
if  (  errorCode  !=  NO_ERROR  )  { 
b  .  append  (  "errorCode="  +  ERROR_STRINGS  [  errorCode  ]  )  ; 
b  .  append  (  " errorExceptionMsg="  +  detailedExceptionMsg  )  ; 
}  else  { 
b  .  append  (  "constraints="  +  constraints  )  ; 
b  .  append  (  "\nprincipalsInSubject="  +  subjectClientPrincipals  )  ; 
b  .  append  (  "\nallowedConfigs=[\n"  )  ; 
if  (  configs  .  length  >  0  )  b  .  append  (  configs  [  0  ]  )  ; 
for  (  int   i  =  1  ;  i  <  configs  .  length  ;  i  ++  )  { 
b  .  append  (  ",\n"  +  configs  [  i  ]  )  ; 
} 
b  .  append  (  "],"  )  ; 
b  .  append  (  "\nunfulfilledConstraints="  +  unfulfilledConstraints  )  ; 
b  .  append  (  "\nconnectionAbsoluteTime="  )  ; 
if  (  connectionAbsoluteTime  ==  Long  .  MAX_VALUE  )  { 
b  .  append  (  "NO_LIMIT"  )  ; 
}  else  { 
b  .  append  (  new   Date  (  connectionAbsoluteTime  )  )  ; 
} 
} 
b  .  append  (  ']'  )  ; 
return   b  .  toString  (  )  ; 
} 






boolean   reusable  (  Subject   subject  )  { 
if  (  subject  ==  null  ||  subjectReadOnly  )  return   true  ; 
Set   cps  =  getClientPrincipals  (  subject  )  ; 
return   cps  .  equals  (  subjectClientPrincipals  )  ; 
} 



































List   getConfigs  (  )  throws   UnsupportedConstraintException  { 
if  (  errorCode  !=  NO_ERROR  )  { 
throw   new   UnsupportedConstraintException  (  detailedExceptionMsg  )  ; 
} 
KerberosTicket  [  ]  tickets  =  (  KerberosTicket  [  ]  )  AccessController  .  doPrivileged  (  new   PrivilegedAction  (  )  { 

public   Object   run  (  )  { 
return   getTickets  (  )  ; 
} 
}  )  ; 
ArrayList   configList  =  new   ArrayList  (  configs  .  length  )  ; 
int   delegYesStepsFromSuccess  =  3  ; 
KerberosPrincipal   delegYesCp  =  null  ; 
int   delegNoStepsFromSuccess  =  2  ; 
KerberosPrincipal   delegNoCp  =  null  ; 
HashMap   hasPermMap  =  new   HashMap  (  )  ; 
for  (  int   i  =  0  ;  i  <  configs  .  length  ;  i  ++  )  { 
AuthenticationPermission   perm  =  getAuthenticationPermission  (  configs  [  i  ]  .  clientPrincipal  ,  configs  [  i  ]  .  deleg  )  ; 
Boolean   hasPerm  =  (  Boolean  )  hasPermMap  .  get  (  perm  )  ; 
if  (  hasPerm  ==  null  )  { 
try  { 
KerberosUtil  .  checkAuthPermission  (  perm  )  ; 
hasPermMap  .  put  (  perm  ,  Boolean  .  TRUE  )  ; 
}  catch  (  SecurityException   e  )  { 
hasPermMap  .  put  (  perm  ,  Boolean  .  FALSE  )  ; 
continue  ; 
} 
}  else   if  (  hasPerm  ==  Boolean  .  FALSE  )  { 
continue  ; 
} 
if  (  configs  [  i  ]  .  deleg  )  { 
if  (  delegYesStepsFromSuccess  >  2  )  { 
delegYesStepsFromSuccess  =  2  ; 
delegYesCp  =  configs  [  i  ]  .  clientPrincipal  ; 
} 
KerberosTicket   t  =  findTicket  (  tickets  ,  configs  [  i  ]  .  clientPrincipal  )  ; 
if  (  t  !=  null  )  { 
if  (  delegYesStepsFromSuccess  >  1  )  { 
delegYesStepsFromSuccess  =  1  ; 
delegYesCp  =  configs  [  i  ]  .  clientPrincipal  ; 
} 
if  (  t  .  isForwardable  (  )  )  configList  .  add  (  configs  [  i  ]  )  ; 
} 
}  else  { 
if  (  delegNoStepsFromSuccess  >  1  )  { 
delegNoStepsFromSuccess  =  1  ; 
delegNoCp  =  configs  [  i  ]  .  clientPrincipal  ; 
} 
if  (  findTicket  (  tickets  ,  configs  [  i  ]  .  clientPrincipal  )  !=  null  )  { 
configList  .  add  (  configs  [  i  ]  )  ; 
} 
} 
} 
if  (  configList  .  size  (  )  ==  0  )  { 
if  (  delegNoStepsFromSuccess  <  delegYesStepsFromSuccess  )  { 
switch  (  delegNoStepsFromSuccess  )  { 
case   1  : 
throw   new   UnsupportedConstraintException  (  "JAAS login has not been done properly, the "  +  "subject associated with the current "  +  "AccessControlContext does not contain a valid "  +  "TGT for "  +  delegNoCp  .  getName  (  )  )  ; 
case   2  : 
throw   new   SecurityException  (  "Caller does not have any of the following "  +  "acceptable permissions: "  +  hasPermMap  .  keySet  (  )  )  ; 
default  : 
throw   new   AssertionError  (  "should not reach here"  )  ; 
} 
}  else  { 
switch  (  delegYesStepsFromSuccess  )  { 
case   1  : 
throw   new   UnsupportedConstraintException  (  "JAAS login has not been done properly, the "  +  "subject associated with the current "  +  "AccessControlContext contains a valid TGT for "  +  delegYesCp  .  getName  (  )  +  ", but the TGT is not "  +  "forwardable."  )  ; 
case   2  : 
throw   new   UnsupportedConstraintException  (  "JAAS login has not been done properly, the "  +  "subject associated with the current "  +  "AccessControlContext does not contain a valid "  +  "TGT for "  +  delegYesCp  .  getName  (  )  )  ; 
default  : 
throw   new   AssertionError  (  "should not reach here"  )  ; 
} 
} 
} 
return   configList  ; 
} 


KerberosEndpoint   getEndpoint  (  )  { 
return   KerberosEndpoint  .  this  ; 
} 








private   Set   getClientPrincipals  (  Subject   subj  )  { 
Set   cpset  =  subj  .  getPrincipals  (  )  ; 
synchronized  (  cpset  )  { 
HashSet   set  =  new   HashSet  (  cpset  .  size  (  )  )  ; 
for  (  Iterator   iter  =  cpset  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Object   p  =  iter  .  next  (  )  ; 
if  (  p   instanceof   KerberosPrincipal  )  set  .  add  (  p  )  ; 
} 
return   set  ; 
} 
} 










private   long   computeConnectionTimeLimit  (  Set   constraints  )  { 
long   timeLimit  =  Long  .  MAX_VALUE  ; 
outer  :  for  (  Iterator   iter  =  constraints  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Object   c  =  iter  .  next  (  )  ; 
long   constraintTimeLimit  =  Long  .  MIN_VALUE  ; 
if  (  c   instanceof   ConstraintAlternatives  )  { 
Set   alts  =  (  (  ConstraintAlternatives  )  c  )  .  elements  (  )  ; 
for  (  Iterator   jter  =  alts  .  iterator  (  )  ;  jter  .  hasNext  (  )  ;  )  { 
Object   alt  =  jter  .  next  (  )  ; 
if  (  alt   instanceof   ConnectionAbsoluteTime  )  { 
long   t  =  (  (  ConnectionAbsoluteTime  )  alt  )  .  getTime  (  )  ; 
if  (  constraintTimeLimit  <  t  )  constraintTimeLimit  =  t  ; 
}  else  { 
continue   outer  ; 
} 
} 
}  else   if  (  c   instanceof   ConnectionAbsoluteTime  )  { 
constraintTimeLimit  =  (  (  ConnectionAbsoluteTime  )  c  )  .  getTime  (  )  ; 
}  else  { 
continue  ; 
} 
if  (  constraintTimeLimit  <  timeLimit  )  { 
timeLimit  =  constraintTimeLimit  ; 
} 
} 
return   timeLimit  ; 
} 







private   KerberosTicket  [  ]  getTickets  (  )  { 
ArrayList   tlist  =  new   ArrayList  (  )  ; 
Set   creds  =  clientSubject  .  getPrivateCredentials  (  )  ; 
synchronized  (  creds  )  { 
for  (  Iterator   iter  =  creds  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Object   cred  =  iter  .  next  (  )  ; 
if  (  cred   instanceof   KerberosTicket  )  { 
KerberosTicket   ticket  =  (  KerberosTicket  )  cred  ; 
if  (  ticket  .  getServer  (  )  .  getName  (  )  .  startsWith  (  "krbtgt/"  )  &&  !  ticket  .  isDestroyed  (  )  &&  ticket  .  isCurrent  (  )  )  { 
tlist  .  add  (  ticket  )  ; 
} 
} 
} 
} 
return  (  KerberosTicket  [  ]  )  tlist  .  toArray  (  new   KerberosTicket  [  tlist  .  size  (  )  ]  )  ; 
} 

private   KerberosTicket   findTicket  (  KerberosTicket  [  ]  tickets  ,  KerberosPrincipal   p  )  { 
String   crealm  =  p  .  getRealm  (  )  ; 
String   srealm  =  serverPrincipal  .  getRealm  (  )  ; 
String   tgtName  =  "krbtgt/"  +  srealm  +  "@"  +  crealm  ; 
for  (  int   i  =  0  ;  i  <  tickets  .  length  ;  i  ++  )  { 
if  (  tickets  [  i  ]  .  getClient  (  )  .  equals  (  p  )  &&  tickets  [  i  ]  .  getServer  (  )  .  getName  (  )  .  equals  (  tgtName  )  )  { 
return   tickets  [  i  ]  ; 
} 
} 
return   null  ; 
} 

private   AuthenticationPermission   getAuthenticationPermission  (  KerberosPrincipal   client  ,  boolean   deleg  )  { 
String   act  ; 
if  (  deleg  )  { 
act  =  "delegate"  ; 
}  else  { 
act  =  "connect"  ; 
} 
Set   locals  =  Collections  .  singleton  (  client  )  ; 
Set   peers  =  Collections  .  singleton  (  serverPrincipal  )  ; 
return   new   AuthenticationPermission  (  locals  ,  peers  ,  act  )  ; 
} 
} 


private   final   class   ConnectionEndpointImpl   implements   ConnectionEndpoint  { 

public   Connection   connect  (  OutboundRequestHandle   handle  )  throws   IOException  { 
RequestHandleImpl   rh  =  checkRequestHandleImpl  (  handle  )  ; 
Config   config  =  null  ; 
Exception   exceptionCaught  =  null  ; 
try  { 
List   configs  =  rh  .  getConfigs  (  )  ; 
config  =  (  Config  )  configs  .  get  (  0  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "Passed in request handle "  +  "is:\n{0},\nconfiguration list returned by "  +  "getConfigs is:\n{1},\nin which the first "  +  "one will be used."  ,  new   Object  [  ]  {  rh  ,  configs  }  )  ; 
} 
}  catch  (  UnsupportedConstraintException   e  )  { 
exceptionCaught  =  e  ; 
}  catch  (  SecurityException   e  )  { 
exceptionCaught  =  e  ; 
} 
if  (  exceptionCaught  !=  null  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
KerberosUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  this  .  getClass  (  )  ,  "connect"  ,  "failed to find a supportable "  +  "connection configuration for the request"  ,  null  ,  exceptionCaught  )  ; 
} 
if  (  rh  .  errorCode  ==  UNSUPPORTABLE_CONSTRAINT_REQUIRED  )  throw  (  UnsupportedConstraintException  )  exceptionCaught  ; 
UnsupportedConstraintException   genericException  =  new   UnsupportedConstraintException  (  "Either there are conflicting or unsatisfiable "  +  "constraint requirements, "  +  "or the JAAS login has not been "  +  "done (Subject.getSubject(AccessController."  +  "getContext()) returns null), or no appropriate "  +  "Kerberos principal and corresponding TGT "  +  "allowed by the requirements can be found in "  +  "the current subject. "  +  rh  .  constraints  )  ; 
KerberosUtil  .  secureThrow  (  exceptionCaught  ,  genericException  )  ; 
} 
Socket   sock  ; 
if  (  !  disableSocketConnect  )  { 
sock  =  connectToHost  (  rh  )  ; 
}  else  { 
sock  =  newSocket  (  )  ; 
} 
Connection   c  =  new   ConnectionImpl  (  sock  ,  config  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "New connection established:\n{0}"  ,  new   Object  [  ]  {  c  }  )  ; 
} 
return   c  ; 
} 

private   Socket   connectToHost  (  RequestHandleImpl   rh  )  throws   IOException  { 
InetAddress  [  ]  addresses  ; 
try  { 
addresses  =  InetAddress  .  getAllByName  (  serverHost  )  ; 
}  catch  (  UnknownHostException   uhe  )  { 
try  { 
return   connectToSocketAddress  (  new   InetSocketAddress  (  serverHost  ,  serverPort  )  ,  rh  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to unresolved host {0}"  ,  new   Object  [  ]  {  serverHost  +  ":"  +  serverPort  }  ,  e  )  ; 
} 
throw   e  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to unresolved host {0}"  ,  new   Object  [  ]  {  serverHost  +  ":"  +  serverPort  }  ,  e  )  ; 
} 
throw   e  ; 
} 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception resolving host {0}"  ,  new   Object  [  ]  {  serverHost  }  ,  e  )  ; 
} 
throw   e  ; 
} 
IOException   lastIOException  =  null  ; 
SecurityException   lastSecurityException  =  null  ; 
for  (  int   i  =  0  ;  i  <  addresses  .  length  ;  i  ++  )  { 
SocketAddress   socketAddress  =  new   InetSocketAddress  (  addresses  [  i  ]  ,  serverPort  )  ; 
try  { 
return   connectToSocketAddress  (  socketAddress  ,  rh  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  socketAddress  }  ,  e  )  ; 
} 
lastIOException  =  e  ; 
if  (  e   instanceof   SocketTimeoutException  )  { 
break  ; 
} 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  socketAddress  }  ,  e  )  ; 
} 
lastSecurityException  =  e  ; 
} 
} 
if  (  lastIOException  !=  null  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  serverHost  +  ":"  +  serverPort  }  ,  lastIOException  )  ; 
} 
throw   lastIOException  ; 
} 
assert   lastSecurityException  !=  null  ; 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connectToHost"  ,  "exception connecting to {0}"  ,  new   Object  [  ]  {  serverHost  +  ":"  +  serverPort  }  ,  lastSecurityException  )  ; 
} 
throw   lastSecurityException  ; 
} 





private   Socket   connectToSocketAddress  (  SocketAddress   socketAddress  ,  RequestHandleImpl   rh  )  throws   IOException  { 
long   timeout  =  rh  .  connectionAbsoluteTime  -  System  .  currentTimeMillis  (  )  ; 
if  (  timeout  <=  0  )  { 
throw   new   SocketTimeoutException  (  "connection timeout passed before socket."  +  "connect is called"  )  ; 
} 
Socket   sock  =  newSocket  (  )  ; 
boolean   ok  =  false  ; 
try  { 
if  (  timeout  >  Integer  .  MAX_VALUE  )  { 
sock  .  connect  (  socketAddress  )  ; 
}  else  { 
sock  .  connect  (  socketAddress  ,  (  int  )  timeout  )  ; 
} 
ok  =  true  ; 
return   sock  ; 
}  finally  { 
if  (  !  ok  )  { 
try  { 
sock  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 





private   Socket   newSocket  (  )  throws   IOException  { 
Socket   sock  ; 
if  (  csf  !=  null  )  { 
try  { 
sock  =  csf  .  createSocket  (  )  ; 
}  catch  (  IOException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
KerberosUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  this  .  getClass  (  )  ,  "newSocket"  ,  "failed to create socket "  +  "using the given SocketFactory {0}"  ,  new   Object  [  ]  {  csf  }  ,  e  )  ; 
} 
throw   e  ; 
} 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "created socket {0} using "  +  "factory {1}"  ,  new   Object  [  ]  {  sock  ,  csf  }  )  ; 
} 
}  else  { 
sock  =  new   Socket  (  )  ; 
logger  .  log  (  Level  .  FINE  ,  "created socket {0}"  ,  sock  )  ; 
} 
setSocketOptions  (  sock  )  ; 
return   sock  ; 
} 

private   void   setSocketOptions  (  Socket   sock  )  { 
try  { 
sock  .  setTcpNoDelay  (  true  )  ; 
}  catch  (  SocketException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
KerberosUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  this  .  getClass  (  )  ,  "connect"  ,  "failed to setTcpNoDelay "  +  "option for {0}"  ,  new   Object  [  ]  {  sock  }  ,  e  )  ; 
} 
} 
try  { 
sock  .  setKeepAlive  (  true  )  ; 
}  catch  (  SocketException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
KerberosUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  this  .  getClass  (  )  ,  "connect"  ,  "failed to setKeepAlive "  +  "options for {0}"  ,  new   Object  [  ]  {  sock  }  ,  e  )  ; 
} 
} 
} 

public   Connection   connect  (  OutboundRequestHandle   handle  ,  Collection   active  ,  Collection   idle  )  { 
RequestHandleImpl   rh  =  checkRequestHandleImpl  (  handle  )  ; 
if  (  active  ==  null  )  { 
throw   new   NullPointerException  (  "active collection cannot be null"  )  ; 
}  else   if  (  idle  ==  null  )  { 
throw   new   NullPointerException  (  "idle collection cannot be null"  )  ; 
} 
List   configList  ; 
try  { 
configList  =  rh  .  getConfigs  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
boolean   checkedResolvePermission  =  false  ; 
for  (  Iterator   i  =  configList  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Config   config  =  (  Config  )  i  .  next  (  )  ; 
for  (  Iterator   j  =  active  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
ConnectionImpl   c  =  checkConnection  (  j  .  next  (  )  )  ; 
if  (  c  .  satisfies  (  config  )  )  { 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "found an active "  +  "connection for reusing:\n{0}\n{1}"  ,  new   Object  [  ]  {  c  ,  config  }  )  ; 
} 
if  (  !  checkedResolvePermission  )  { 
try  { 
checkResolvePermission  (  )  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connect"  ,  "exception resolving host {0}"  ,  new   Object  [  ]  {  serverHost  }  ,  e  )  ; 
} 
throw   e  ; 
} 
checkedResolvePermission  =  true  ; 
} 
try  { 
c  .  checkConnectPermission  (  )  ; 
return   c  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  ConnectionEndpointImpl  .  class  ,  "connect"  ,  "access to reuse connection {0} denied"  ,  new   Object  [  ]  {  c  .  sock  }  ,  e  )  ; 
} 
} 
} 
} 
} 
for  (  Iterator   i  =  configList  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Config   config  =  (  Config  )  i  .  next  (  )  ; 
for  (  Iterator   j  =  idle  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
ConnectionImpl   c  =  checkConnection  (  j  .  next  (  )  )  ; 
if  (  c  .  switchTo  (  config  )  )  { 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "found an idle "  +  "connection for reusing:\n{0}\n{1}"  ,  new   Object  [  ]  {  c  ,  config  }  )  ; 
} 
if  (  !  checkedResolvePermission  )  { 
try  { 
checkResolvePermission  (  )  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  ConnectionEndpointImpl  .  class  ,  "connect"  ,  "exception resolving host {0}"  ,  new   Object  [  ]  {  serverHost  }  ,  e  )  ; 
} 
throw   e  ; 
} 
checkedResolvePermission  =  true  ; 
} 
try  { 
c  .  checkConnectPermission  (  )  ; 
return   c  ; 
}  catch  (  SecurityException   e  )  { 
if  (  logger  .  isLoggable  (  Levels  .  HANDLED  )  )  { 
LogUtil  .  logThrow  (  logger  ,  Levels  .  HANDLED  ,  ConnectionEndpointImpl  .  class  ,  "connect"  ,  "access to reuse connection {0} denied"  ,  new   Object  [  ]  {  c  .  sock  }  ,  e  )  ; 
} 
} 
} 
} 
} 
return   null  ; 
} 

private   void   checkResolvePermission  (  )  { 
SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
sm  .  checkConnect  (  serverHost  ,  -  1  )  ; 
} 
} 
} 


private   final   class   ConnectionImpl   extends   KerberosUtil  .  Connection   implements   Connection  { 


private   static   final   int   KRB_AP_ERR_REPEAT  =  34  ; 


private   InputStream   istream  ; 


private   OutputStream   ostream  ; 












ConnectionImpl  (  Socket   sock  ,  Config   config  )  throws   IOException  { 
super  (  sock  )  ; 
clientPrincipal  =  config  .  clientPrincipal  ; 
doEncryption  =  config  .  encry  ; 
doDelegation  =  config  .  deleg  ; 
connectionLogger  =  logger  ; 
boolean   done  =  false  ; 
try  { 
Security  .  doPrivileged  (  new   PrivilegedExceptionAction  (  )  { 

public   Object   run  (  )  throws   IOException  ,  GSSException  { 
establishContext  (  )  ; 
return   null  ; 
} 
}  )  ; 
ostream  =  new   KerberosUtil  .  ConnectionOutputStream  (  this  )  ; 
istream  =  new   KerberosUtil  .  ConnectionInputStream  (  this  )  ; 
done  =  true  ; 
}  catch  (  PrivilegedActionException   e  )  { 
Exception   ex  =  e  .  getException  (  )  ; 
if  (  logger  .  isLoggable  (  Levels  .  FAILED  )  )  { 
KerberosUtil  .  logThrow  (  logger  ,  Levels  .  FAILED  ,  this  .  getClass  (  )  ,  "constructor"  ,  "failed to establish GSSContext for this connection "  +  "with {0}."  ,  new   Object  [  ]  {  config  }  ,  ex  )  ; 
} 
if  (  ex   instanceof   GSSException  )  { 
IOException   ioe  =  new   IOException  (  "Failed to establish GSS "  +  "context for this connection."  )  ; 
ioe  .  initCause  (  ex  )  ; 
throw   ioe  ; 
}  else  { 
throw  (  IOException  )  ex  ; 
} 
}  finally  { 
if  (  !  done  )  close  (  )  ; 
} 
} 

public   OutputStream   getOutputStream  (  )  throws   IOException  { 
return   ostream  ; 
} 

public   InputStream   getInputStream  (  )  throws   IOException  { 
return   istream  ; 
} 

public   SocketChannel   getChannel  (  )  { 
return   null  ; 
} 

public   void   populateContext  (  OutboundRequestHandle   handle  ,  Collection   context  )  { 
if  (  handle  ==  null  )  { 
throw   new   NullPointerException  (  "handle is null"  )  ; 
}  else   if  (  context  ==  null  )  { 
throw   new   NullPointerException  (  "context is null"  )  ; 
} 
} 

public   InvocationConstraints   getUnfulfilledConstraints  (  OutboundRequestHandle   handle  )  { 
RequestHandleImpl   rh  =  checkRequestHandleImpl  (  handle  )  ; 
return   rh  .  unfulfilledConstraints  ; 
} 

public   void   writeRequestData  (  OutboundRequestHandle   handle  ,  OutputStream   out  )  { 
} 

public   IOException   readResponseData  (  OutboundRequestHandle   handle  ,  InputStream   in  )  { 
return   null  ; 
} 


public   String   toString  (  )  { 
StringBuffer   b  =  new   StringBuffer  (  "KerberosEndpoint.ConnectionImpl"  )  ; 
b  .  append  (  "[clientPrincipal="  +  clientPrincipal  )  ; 
b  .  append  (  " serverPrincipal="  +  serverPrincipal  )  ; 
b  .  append  (  " doEncryption="  +  doEncryption  )  ; 
b  .  append  (  " doDelegation="  +  doDelegation  )  ; 
b  .  append  (  " client="  +  sock  .  getLocalAddress  (  )  .  getHostName  (  )  )  ; 
b  .  append  (  ":"  +  sock  .  getLocalPort  (  )  )  ; 
b  .  append  (  " server="  +  sock  .  getInetAddress  (  )  .  getHostName  (  )  )  ; 
b  .  append  (  ":"  +  sock  .  getPort  (  )  )  ; 
b  .  append  (  ']'  )  ; 
return   b  .  toString  (  )  ; 
} 





boolean   satisfies  (  Config   config  )  { 
return   gssContext  .  getLifetime  (  )  >=  minGssContextLifetime  &&  clientPrincipal  .  equals  (  config  .  clientPrincipal  )  &&  doEncryption  ==  config  .  encry  &&  doDelegation  ==  config  .  deleg  ; 
} 





void   checkConnectPermission  (  )  { 
SecurityManager   sm  =  System  .  getSecurityManager  (  )  ; 
if  (  sm  !=  null  )  { 
InetSocketAddress   address  =  (  InetSocketAddress  )  sock  .  getRemoteSocketAddress  (  )  ; 
if  (  address  .  isUnresolved  (  )  )  { 
sm  .  checkConnect  (  address  .  getHostName  (  )  ,  sock  .  getPort  (  )  )  ; 
}  else  { 
sm  .  checkConnect  (  address  .  getAddress  (  )  .  getHostAddress  (  )  ,  sock  .  getPort  (  )  )  ; 
} 
} 
} 









boolean   switchTo  (  Config   config  )  { 
if  (  gssContext  .  getLifetime  (  )  <  minGssContextLifetime  )  return   false  ; 
if  (  clientPrincipal  .  equals  (  config  .  clientPrincipal  )  &&  doDelegation  ==  config  .  deleg  )  { 
doEncryption  =  config  .  encry  ; 
return   true  ; 
} 
return   false  ; 
} 


KerberosEndpoint   getEndpoint  (  )  { 
return   KerberosEndpoint  .  this  ; 
} 





private   void   establishContext  (  )  throws   IOException  ,  GSSException  { 
synchronized  (  classLock  )  { 
if  (  gssManager  ==  null  )  { 
gssManager  =  GSSManager  .  getInstance  (  )  ; 
} 
} 
GSSName   clientName  =  gssManager  .  createName  (  clientPrincipal  .  getName  (  )  ,  KerberosUtil  .  krb5NameType  )  ; 
GSSCredential   clientCred  =  gssManager  .  createCredential  (  clientName  ,  GSSCredential  .  INDEFINITE_LIFETIME  ,  KerberosUtil  .  krb5MechOid  ,  GSSCredential  .  INITIATE_ONLY  )  ; 
GSSName   serverName  =  gssManager  .  createName  (  serverPrincipal  .  getName  (  )  ,  KerberosUtil  .  krb5NameType  )  ; 
for  (  int   i  =  maxGssContextRetries  ;  i  >  0  ;  i  --  )  { 
gssContext  =  gssManager  .  createContext  (  serverName  ,  KerberosUtil  .  krb5MechOid  ,  clientCred  ,  GSSContext  .  DEFAULT_LIFETIME  )  ; 
gssContext  .  requestMutualAuth  (  true  )  ; 
gssContext  .  requestInteg  (  true  )  ; 
gssContext  .  requestConf  (  true  )  ; 
gssContext  .  requestCredDeleg  (  doDelegation  )  ; 
gssContext  .  requestReplayDet  (  true  )  ; 
gssContext  .  requestSequenceDet  (  true  )  ; 
byte  [  ]  token  =  new   byte  [  0  ]  ; 
try  { 
while  (  true  )  { 
token  =  gssContext  .  initSecContext  (  token  ,  0  ,  token  .  length  )  ; 
if  (  token  !=  null  )  { 
dos  .  writeInt  (  token  .  length  )  ; 
dos  .  write  (  token  )  ; 
dos  .  flush  (  )  ; 
} 
if  (  gssContext  .  isEstablished  (  )  )  { 
break  ; 
}  else  { 
token  =  new   byte  [  dis  .  readInt  (  )  ]  ; 
dis  .  readFully  (  token  )  ; 
} 
} 
break  ; 
}  catch  (  GSSException   ge  )  { 
if  (  (  ge  .  getMessage  (  )  .  indexOf  (  "34"  )  >=  0  ||  ge  .  getMajor  (  )  ==  GSSException  .  DUPLICATE_TOKEN  ||  ge  .  getMinor  (  )  ==  KRB_AP_ERR_REPEAT  ||  ge  .  getMessage  (  )  .  indexOf  (  "Request is a replay"  )  >=  0  )  &&  i  !=  1  )  { 
continue  ; 
} 
throw   ge  ; 
} 
} 
if  (  !  gssContext  .  getIntegState  (  )  ||  (  doEncryption  &&  !  gssContext  .  getConfState  (  )  )  ||  (  doDelegation  &&  !  gssContext  .  getCredDelegState  (  )  )  ||  !  gssContext  .  getTargName  (  )  .  toString  (  )  .  equals  (  serverPrincipal  .  getName  (  )  )  )  { 
throw   new   IOException  (  "Failed to establish gss context "  +  "for connection"  )  ; 
} 
logger  .  log  (  Level  .  FINE  ,  "GSSContext established for {0}"  ,  this  )  ; 
} 
} 








private   static   final   class   CacheKey  { 

private   final   Subject   subject  ; 

private   final   InvocationConstraints   constraints  ; 


CacheKey  (  Subject   subject  ,  InvocationConstraints   constraints  )  { 
this  .  subject  =  subject  ; 
this  .  constraints  =  constraints  ; 
} 

public   int   hashCode  (  )  { 
return   System  .  identityHashCode  (  subject  )  ^  System  .  identityHashCode  (  constraints  )  ; 
} 


public   boolean   equals  (  Object   o  )  { 
if  (  o  ==  this  )  { 
return   true  ; 
}  else   if  (  !  (  o   instanceof   CacheKey  )  )  { 
return   false  ; 
} 
CacheKey   okey  =  (  CacheKey  )  o  ; 
return   subject  ==  okey  .  subject  &&  constraints  ==  okey  .  constraints  ; 
} 
} 
} 

