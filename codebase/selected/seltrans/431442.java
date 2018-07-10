package   org  .  mobicents  .  servlet  .  sip  .  startup  .  jboss  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Set  ; 
import   javax  .  management  .  Attribute  ; 
import   javax  .  management  .  JMException  ; 
import   javax  .  management  .  MBeanServer  ; 
import   javax  .  management  .  Notification  ; 
import   javax  .  management  .  NotificationListener  ; 
import   javax  .  management  .  ObjectInstance  ; 
import   javax  .  management  .  ObjectName  ; 
import   javax  .  security  .  jacc  .  PolicyContext  ; 
import   org  .  apache  .  catalina  .  Lifecycle  ; 
import   org  .  apache  .  catalina  .  LifecycleException  ; 
import   org  .  apache  .  catalina  .  connector  .  Connector  ; 
import   org  .  apache  .  tomcat  .  util  .  modeler  .  Registry  ; 
import   org  .  jboss  .  deployment  .  DeploymentInfo  ; 
import   org  .  jboss  .  deployment  .  SubDeployerExt  ; 
import   org  .  jboss  .  metadata  .  WebMetaData  ; 
import   org  .  jboss  .  mx  .  util  .  MBeanProxyExt  ; 
import   org  .  jboss  .  security  .  plugins  .  JaasSecurityManagerServiceMBean  ; 
import   org  .  jboss  .  system  .  ServiceControllerMBean  ; 
import   org  .  jboss  .  system  .  server  .  Server  ; 
import   org  .  jboss  .  system  .  server  .  ServerImplMBean  ; 
import   org  .  jboss  .  web  .  AbstractWebDeployer  ; 
import   org  .  jboss  .  web  .  tomcat  .  security  .  HttpServletRequestPolicyContextHandler  ; 
import   org  .  jboss  .  web  .  tomcat  .  service  .  DeployerConfig  ; 
import   org  .  jboss  .  web  .  tomcat  .  service  .  JBossWebMBean  ; 
import   org  .  jboss  .  web  .  tomcat  .  service  .  session  .  SessionIDGenerator  ; 
import   org  .  mobicents  .  servlet  .  sip  .  catalina  .  SipHostConfig  ; 
import   org  .  mobicents  .  servlet  .  sip  .  catalina  .  annotations  .  SipApplicationAnnotationUtils  ; 
import   org  .  mobicents  .  servlet  .  sip  .  core  .  SipContext  ; 
















public   class   JBossSip   extends   AbstractConvergedContainer   implements   JBossWebMBean  ,  NotificationListener  { 

public   static   final   String   NAME  =  "JBossSip"  ; 





public   static   final   String   DEFAULT_CACHE_NAME  =  "jboss.cache:service=TomcatClusteringCache"  ; 

private   String   contextClassName  =  "org.apache.catalina.core.StandardContext"  ; 






private   Properties   authenticators  =  null  ; 




private   String   catalinaDomain  =  "Catalina"  ; 





private   String   cacheName  =  DEFAULT_CACHE_NAME  ; 





protected   String   managerClass  =  "org.jboss.web.tomcat.service.session.JBossCacheManager"  ; 





private   int   snapshotInterval  =  1000  ; 





private   String   snapshotMode  =  "instant"  ; 




private   boolean   useLocalCache  =  true  ; 





private   int   maxUnreplicatedInterval  =  WebMetaData  .  DEFAULT_MAX_UNREPLICATED_INTERVAL  ; 




private   boolean   useJK  =  false  ; 




private   boolean   useJBossWebLoader  =  true  ; 




private   boolean   deleteWorkDirOnContextDestroy  =  false  ; 




private   String   httpHeaderForSSOAuth  =  null  ; 

private   String   sessionCookieForSSOAuth  =  null  ; 




private   String   serverConfigFile  =  "server.xml"  ; 




private   String   subjectAttributeName  =  null  ; 




private   boolean   allowSelfPrivilegedWebApps  =  false  ; 


private   JaasSecurityManagerServiceMBean   secMgrService  ; 


private   String  [  ]  filteredPackages  ; 


private   SubDeployerExt   thisProxy  ; 

public   String   getName  (  )  { 
return   NAME  ; 
} 

public   String   getManagerClass  (  )  { 
return   managerClass  ; 
} 

public   void   setManagerClass  (  String   managerClass  )  { 
this  .  managerClass  =  managerClass  ; 
} 

public   String   getDomain  (  )  { 
return   this  .  catalinaDomain  ; 
} 

public   Properties   getAuthenticators  (  )  { 
return   this  .  authenticators  ; 
} 

public   void   setAuthenticators  (  Properties   prop  )  { 
this  .  authenticators  =  prop  ; 
log  .  debug  (  "Passed set of authenticators="  +  prop  )  ; 
} 








public   void   setDomain  (  String   catalinaDomain  )  { 
this  .  catalinaDomain  =  catalinaDomain  ; 
} 

public   void   setContextMBeanCode  (  String   className  )  { 
this  .  contextClassName  =  className  ; 
} 

public   String   getContextMBeanCode  (  )  { 
return   contextClassName  ; 
} 




public   void   setSnapshotInterval  (  int   interval  )  { 
this  .  snapshotInterval  =  interval  ; 
} 




public   int   getSnapshotInterval  (  )  { 
return   this  .  snapshotInterval  ; 
} 




public   void   setSnapshotMode  (  String   mode  )  { 
this  .  snapshotMode  =  mode  ; 
} 




public   String   getSnapshotMode  (  )  { 
return   this  .  snapshotMode  ; 
} 








public   String   getCacheName  (  )  { 
return   cacheName  ; 
} 










public   void   setCacheName  (  String   cacheName  )  { 
this  .  cacheName  =  cacheName  ; 
} 

public   boolean   isUseLocalCache  (  )  { 
return   useLocalCache  ; 
} 

public   void   setUseLocalCache  (  boolean   useLocalCache  )  { 
this  .  useLocalCache  =  useLocalCache  ; 
} 

public   boolean   isUseJK  (  )  { 
return   useJK  ; 
} 

public   void   setUseJK  (  boolean   useJK  )  { 
this  .  useJK  =  useJK  ; 
} 

public   int   getMaxUnreplicatedInterval  (  )  { 
return   maxUnreplicatedInterval  ; 
} 

public   void   setMaxUnreplicatedInterval  (  int   maxUnreplicatedInterval  )  { 
this  .  maxUnreplicatedInterval  =  maxUnreplicatedInterval  ; 
} 

public   boolean   getDeleteWorkDirOnContextDestroy  (  )  { 
return   deleteWorkDirOnContextDestroy  ; 
} 

public   void   setDeleteWorkDirOnContextDestroy  (  boolean   deleteFlag  )  { 
this  .  deleteWorkDirOnContextDestroy  =  deleteFlag  ; 
} 

public   String   getHttpHeaderForSSOAuth  (  )  { 
return   httpHeaderForSSOAuth  ; 
} 

public   void   setHttpHeaderForSSOAuth  (  String   httpHeader  )  { 
this  .  httpHeaderForSSOAuth  =  httpHeader  ; 
} 

public   String   getSessionCookieForSSOAuth  (  )  { 
return   sessionCookieForSSOAuth  ; 
} 

public   void   setSessionCookieForSSOAuth  (  String   sessionC  )  { 
this  .  sessionCookieForSSOAuth  =  sessionC  ; 
} 




public   void   setSessionIdAlphabet  (  String   sessionIdAlphabet  )  { 
SessionIDGenerator  .  getInstance  (  )  .  setSessionIdAlphabet  (  sessionIdAlphabet  )  ; 
} 




public   String   getSessionIdAlphabet  (  )  { 
return   SessionIDGenerator  .  getInstance  (  )  .  getSessionIdAlphabet  (  )  ; 
} 

public   boolean   getUseJBossWebLoader  (  )  { 
return   useJBossWebLoader  ; 
} 

public   void   setUseJBossWebLoader  (  boolean   flag  )  { 
this  .  useJBossWebLoader  =  flag  ; 
} 

public   String   getConfigFile  (  )  { 
return   serverConfigFile  ; 
} 

public   void   setConfigFile  (  String   configFile  )  { 
this  .  serverConfigFile  =  configFile  ; 
} 

public   String   getSubjectAttributeName  (  )  { 
return   this  .  subjectAttributeName  ; 
} 

public   void   setSubjectAttributeName  (  String   name  )  { 
this  .  subjectAttributeName  =  name  ; 
} 

public   boolean   isAllowSelfPrivilegedWebApps  (  )  { 
return   allowSelfPrivilegedWebApps  ; 
} 

public   void   setAllowSelfPrivilegedWebApps  (  boolean   allowSelfPrivilegedWebApps  )  { 
this  .  allowSelfPrivilegedWebApps  =  allowSelfPrivilegedWebApps  ; 
} 

public   void   setSecurityManagerService  (  JaasSecurityManagerServiceMBean   mgr  )  { 
this  .  secMgrService  =  mgr  ; 
} 

public   String  [  ]  getFilteredPackages  (  )  { 
return   filteredPackages  ; 
} 

public   void   setFilteredPackages  (  String  [  ]  pkgs  )  { 
this  .  filteredPackages  =  pkgs  ; 
} 

public   void   startService  (  )  throws   Exception  { 
System  .  setProperty  (  "catalina.ext.dirs"  ,  (  System  .  getProperty  (  "jboss.server.home.dir"  )  +  File  .  separator  +  "lib"  )  )  ; 
String   objectNameS  =  catalinaDomain  +  ":type=server"  ; 
ObjectName   objectName  =  new   ObjectName  (  objectNameS  )  ; 
Registry  .  getRegistry  (  )  .  setMBeanServer  (  server  )  ; 
Registry  .  getRegistry  (  )  .  registerComponent  (  Class  .  forName  (  "org.apache.catalina.startup.Catalina"  )  .  newInstance  (  )  ,  objectName  ,  "org.apache.catalina.startup.Catalina"  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "catalinaHome"  ,  System  .  getProperty  (  "jboss.server.home.dir"  )  )  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "configFile"  ,  serverConfigFile  )  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "useNaming"  ,  Boolean  .  valueOf  (  false  )  )  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "useShutdownHook"  ,  Boolean  .  valueOf  (  false  )  )  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "await"  ,  Boolean  .  valueOf  (  false  )  )  )  ; 
server  .  setAttribute  (  objectName  ,  new   Attribute  (  "redirectStreams"  ,  Boolean  .  valueOf  (  false  )  )  )  ; 
server  .  invoke  (  objectName  ,  "create"  ,  new   Object  [  ]  {  }  ,  new   String  [  ]  {  }  )  ; 
server  .  invoke  (  objectName  ,  "start"  ,  new   Object  [  ]  {  }  ,  new   String  [  ]  {  }  )  ; 
ObjectName   ssoQuery  =  new   ObjectName  (  "*:type=Valve,*"  )  ; 
Iterator   iterator  =  server  .  queryMBeans  (  ssoQuery  ,  null  )  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
ObjectName   ssoObjectName  =  (  (  ObjectInstance  )  iterator  .  next  (  )  )  .  getObjectName  (  )  ; 
String   name  =  ssoObjectName  .  getKeyProperty  (  "name"  )  ; 
if  (  cacheName  !=  null  &&  "ClusteredSingleSignOn"  .  equals  (  name  )  )  { 
String   tcName  =  (  String  )  server  .  getAttribute  (  ssoObjectName  ,  "treeCacheName"  )  ; 
tcName  =  (  tcName  !=  null  ?  tcName  :  DEFAULT_CACHE_NAME  )  ; 
ObjectName   ssoCacheName  =  new   ObjectName  (  tcName  )  ; 
if  (  ssoCacheName  .  equals  (  new   ObjectName  (  DEFAULT_CACHE_NAME  )  )  )  { 
log  .  info  (  "Setting the cache name to "  +  cacheName  +  " on "  +  ssoObjectName  )  ; 
server  .  setAttribute  (  ssoObjectName  ,  new   Attribute  (  "treeCacheName"  ,  cacheName  )  )  ; 
} 
} 
} 
HttpServletRequestPolicyContextHandler   handler  =  new   HttpServletRequestPolicyContextHandler  (  )  ; 
PolicyContext  .  registerHandler  (  HttpServletRequestPolicyContextHandler  .  WEB_REQUEST_KEY  ,  handler  ,  true  )  ; 
serviceController  =  (  ServiceControllerMBean  )  MBeanProxyExt  .  create  (  ServiceControllerMBean  .  class  ,  ServiceControllerMBean  .  OBJECT_NAME  ,  server  )  ; 
thisProxy  =  (  SubDeployerExt  )  MBeanProxyExt  .  create  (  SubDeployerExt  .  class  ,  super  .  getServiceName  (  )  ,  super  .  getServer  (  )  )  ; 
mainDeployer  .  addDeployer  (  thisProxy  )  ; 
Boolean   started  =  (  Boolean  )  server  .  getAttribute  (  ServerImplMBean  .  OBJECT_NAME  ,  "Started"  )  ; 
if  (  started  .  booleanValue  (  )  ==  true  )  { 
log  .  debug  (  "Server '"  +  ServerImplMBean  .  OBJECT_NAME  +  "' already started, starting connectors now"  )  ; 
startConnectors  (  )  ; 
}  else  { 
log  .  debug  (  "Server '"  +  ServerImplMBean  .  OBJECT_NAME  +  "' not started, registering for start-up notification"  )  ; 
server  .  addNotificationListener  (  ServerImplMBean  .  OBJECT_NAME  ,  this  ,  null  ,  null  )  ; 
} 
} 

public   void   stopService  (  )  throws   Exception  { 
String   objectNameS  =  catalinaDomain  +  ":type=server"  ; 
ObjectName   objectName  =  new   ObjectName  (  objectNameS  )  ; 
server  .  invoke  (  objectName  ,  "stop"  ,  new   Object  [  ]  {  }  ,  new   String  [  ]  {  }  )  ; 
server  .  invoke  (  objectName  ,  "destroy"  ,  new   Object  [  ]  {  }  ,  new   String  [  ]  {  }  )  ; 
server  .  unregisterMBean  (  objectName  )  ; 
MBeanServer   server2  =  server  ; 
mainDeployer  .  removeDeployer  (  thisProxy  )  ; 
ObjectName   queryObjectName  =  new   ObjectName  (  catalinaDomain  +  ":*"  )  ; 
Iterator   iterator  =  server2  .  queryMBeans  (  queryObjectName  ,  null  )  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
ObjectInstance   oi  =  (  ObjectInstance  )  iterator  .  next  (  )  ; 
ObjectName   toRemove  =  oi  .  getObjectName  (  )  ; 
if  (  !  "WebServer"  .  equals  (  toRemove  .  getKeyProperty  (  "service"  )  )  )  { 
if  (  server2  .  isRegistered  (  toRemove  )  )  { 
server2  .  unregisterMBean  (  toRemove  )  ; 
} 
} 
} 
queryObjectName  =  new   ObjectName  (  "Catalina:*"  )  ; 
iterator  =  server2  .  queryMBeans  (  queryObjectName  ,  null  )  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
ObjectInstance   oi  =  (  ObjectInstance  )  iterator  .  next  (  )  ; 
ObjectName   name  =  oi  .  getObjectName  (  )  ; 
server2  .  unregisterMBean  (  name  )  ; 
} 
} 

public   void   startConnectors  (  )  throws   Exception  { 
ObjectName   service  =  new   ObjectName  (  catalinaDomain  +  ":type=Service,serviceName=jboss.web"  )  ; 
Object  [  ]  args  =  {  }  ; 
String  [  ]  sig  =  {  }  ; 
Connector  [  ]  connectors  =  (  Connector  [  ]  )  server  .  invoke  (  service  ,  "findConnectors"  ,  args  ,  sig  )  ; 
for  (  int   n  =  0  ;  n  <  connectors  .  length  ;  n  ++  )  { 
Lifecycle   lc  =  (  Lifecycle  )  connectors  [  n  ]  ; 
lc  .  start  (  )  ; 
} 
startAllConnectors  (  )  ; 
ObjectName   sipApplicationDispatcher  =  new   ObjectName  (  catalinaDomain  +  ":type=SipApplicationDispatcher"  )  ; 
server  .  invoke  (  sipApplicationDispatcher  ,  "start"  ,  args  ,  sig  )  ; 
sendNotification  (  new   Notification  (  TOMCAT_CONNECTORS_STARTED  ,  this  ,  getNextNotificationSequenceNumber  (  )  )  )  ; 
} 

public   void   stopConnectors  (  )  throws   Exception  { 
ObjectName   service  =  new   ObjectName  (  catalinaDomain  +  ":type=Service,serviceName=jboss.web"  )  ; 
Object  [  ]  args  =  {  }  ; 
String  [  ]  sig  =  {  }  ; 
Connector  [  ]  connectors  =  (  Connector  [  ]  )  server  .  invoke  (  service  ,  "findConnectors"  ,  args  ,  sig  )  ; 
for  (  int   n  =  0  ;  n  <  connectors  .  length  ;  n  ++  )  { 
Lifecycle   lc  =  (  Lifecycle  )  connectors  [  n  ]  ; 
lc  .  stop  (  )  ; 
} 
stopAllConnectors  (  )  ; 
ObjectName   sipApplicationDispatcher  =  new   ObjectName  (  catalinaDomain  +  ":type=SipApplicationDispatcher"  )  ; 
server  .  invoke  (  sipApplicationDispatcher  ,  "stop"  ,  args  ,  sig  )  ; 
} 

public   void   handleNotification  (  Notification   msg  ,  Object   handback  )  { 
String   type  =  msg  .  getType  (  )  ; 
if  (  type  .  equals  (  Server  .  START_NOTIFICATION_TYPE  )  )  { 
log  .  debug  (  "Saw "  +  type  +  " notification, starting connectors"  )  ; 
try  { 
startConnectors  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  warn  (  "Failed to startConnectors"  ,  e  )  ; 
} 
} 
} 

public   AbstractWebDeployer   getDeployer  (  DeploymentInfo   di  )  throws   Exception  { 
ClassLoader   loader  =  di  .  ucl  ; 
Class   deployerClass  =  loader  .  loadClass  (  "org.mobicents.servlet.sip.startup.jboss.TomcatConvergedDeployer"  )  ; 
AbstractWebDeployer   deployer  =  (  AbstractWebDeployer  )  deployerClass  .  newInstance  (  )  ; 
DeployerConfig   config  =  new   DeployerConfig  (  )  ; 
config  .  setDefaultSecurityDomain  (  this  .  defaultSecurityDomain  )  ; 
config  .  setSubjectAttributeName  (  this  .  subjectAttributeName  )  ; 
config  .  setServiceClassLoader  (  getClass  (  )  .  getClassLoader  (  )  )  ; 
config  .  setManagerClass  (  this  .  managerClass  )  ; 
config  .  setJava2ClassLoadingCompliance  (  this  .  java2ClassLoadingCompliance  )  ; 
config  .  setUnpackWars  (  this  .  unpackWars  )  ; 
config  .  setLenientEjbLink  (  this  .  lenientEjbLink  )  ; 
config  .  setCatalinaDomain  (  catalinaDomain  )  ; 
if  (  isSipServletApplication  (  di  )  )  { 
config  .  setContextClassName  (  SipHostConfig  .  SIP_CONTEXT_CLASS  )  ; 
}  else  { 
config  .  setContextClassName  (  contextClassName  )  ; 
} 
config  .  setServiceName  (  serviceName  )  ; 
config  .  setSnapshotInterval  (  this  .  snapshotInterval  )  ; 
config  .  setSnapshotMode  (  this  .  snapshotMode  )  ; 
config  .  setUseLocalCache  (  this  .  useLocalCache  )  ; 
config  .  setUseJK  (  this  .  useJK  )  ; 
config  .  setMaxUnreplicatedInterval  (  this  .  maxUnreplicatedInterval  )  ; 
config  .  setSubjectAttributeName  (  this  .  subjectAttributeName  )  ; 
config  .  setUseJBossWebLoader  (  this  .  useJBossWebLoader  )  ; 
config  .  setAllowSelfPrivilegedWebApps  (  this  .  allowSelfPrivilegedWebApps  )  ; 
config  .  setSecurityManagerService  (  this  .  secMgrService  )  ; 
config  .  setFilteredPackages  (  filteredPackages  )  ; 
deployer  .  setServer  (  server  )  ; 
deployer  .  init  (  config  )  ; 
return   deployer  ; 
} 







private   void   startAllConnectors  (  )  throws   JMException  ,  LifecycleException  { 
ObjectName   oname  =  new   ObjectName  (  "*:type=Service,*"  )  ; 
Set   services  =  server  .  queryMBeans  (  oname  ,  null  )  ; 
Iterator   iter  =  services  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
ObjectInstance   oi  =  (  ObjectInstance  )  iter  .  next  (  )  ; 
ObjectName   on  =  oi  .  getObjectName  (  )  ; 
if  (  this  .  catalinaDomain  .  equals  (  on  .  getDomain  (  )  )  )  continue  ; 
String   key  =  on  .  getKeyProperty  (  "serviceName"  )  ; 
if  (  key  !=  null  )  { 
Connector  [  ]  connectors  =  (  Connector  [  ]  )  server  .  invoke  (  on  ,  "findConnectors"  ,  new   Object  [  0  ]  ,  new   String  [  0  ]  )  ; 
for  (  int   n  =  0  ;  n  <  connectors  .  length  ;  n  ++  )  { 
Lifecycle   lc  =  (  Lifecycle  )  connectors  [  n  ]  ; 
lc  .  start  (  )  ; 
} 
} 
} 
} 







private   void   stopAllConnectors  (  )  throws   JMException  ,  LifecycleException  { 
ObjectName   oname  =  new   ObjectName  (  "*:type=Service,*"  )  ; 
Set   services  =  server  .  queryMBeans  (  oname  ,  null  )  ; 
Iterator   iter  =  services  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
ObjectInstance   oi  =  (  ObjectInstance  )  iter  .  next  (  )  ; 
ObjectName   on  =  oi  .  getObjectName  (  )  ; 
if  (  this  .  catalinaDomain  .  equals  (  on  .  getDomain  (  )  )  )  continue  ; 
String   key  =  on  .  getKeyProperty  (  "serviceName"  )  ; 
if  (  key  !=  null  )  { 
Connector  [  ]  connectors  =  (  Connector  [  ]  )  server  .  invoke  (  on  ,  "findConnectors"  ,  new   Object  [  0  ]  ,  new   String  [  0  ]  )  ; 
for  (  int   n  =  0  ;  n  <  connectors  .  length  ;  n  ++  )  { 
Lifecycle   lc  =  (  Lifecycle  )  connectors  [  n  ]  ; 
lc  .  stop  (  )  ; 
} 
} 
} 
} 


public   static   final   String   SAR_SUFFIX  =  ".sar2"  ; 





private   static   final   String  [  ]  DEFAULT_ENHANCED_SUFFIXES  =  new   String  [  ]  {  "600:"  +  SAR_SUFFIX  }  ; 




public   JBossSip  (  )  { 
super  (  )  ; 
String  [  ]  enhancedSuffixes  =  getEnhancedSuffixes  (  )  ; 
String  [  ]  convergedEnhancedSuffixes  =  new   String  [  enhancedSuffixes  .  length  +  DEFAULT_ENHANCED_SUFFIXES  .  length  ]  ; 
System  .  arraycopy  (  enhancedSuffixes  ,  0  ,  convergedEnhancedSuffixes  ,  0  ,  enhancedSuffixes  .  length  )  ; 
System  .  arraycopy  (  DEFAULT_ENHANCED_SUFFIXES  ,  0  ,  convergedEnhancedSuffixes  ,  enhancedSuffixes  .  length  ,  DEFAULT_ENHANCED_SUFFIXES  .  length  )  ; 
setEnhancedSuffixes  (  convergedEnhancedSuffixes  )  ; 
} 

@  Override 
public   boolean   accepts  (  DeploymentInfo   sdi  )  { 
boolean   accept  =  super  .  accepts  (  sdi  )  ; 
String   urlPath  =  sdi  .  url  .  getPath  (  )  ; 
String   shortName  =  sdi  .  shortName  ; 
boolean   checkDir  =  sdi  .  isDirectory  &&  !  (  sdi  .  isXML  ||  sdi  .  isScript  )  ; 
if  (  (  urlPath  .  endsWith  (  SAR_SUFFIX  )  ||  (  checkDir  &&  shortName  .  endsWith  (  SAR_SUFFIX  )  )  ||  urlPath  .  endsWith  (  "war"  )  ||  (  checkDir  &&  shortName  .  endsWith  (  "war"  )  )  )  &&  isSipServletApplication  (  sdi  )  )  { 
return   true  ; 
} 
return   accept  ; 
} 








public   static   boolean   isSipServletApplication  (  DeploymentInfo   di  )  { 
URL   url  =  di  .  localCl  .  findResource  (  SipContext  .  APPLICATION_SIP_XML  )  ; 
if  (  url  !=  null  )  { 
try  { 
url  .  openStream  (  )  ; 
return   true  ; 
}  catch  (  IOException   e  )  { 
return   false  ; 
} 
}  else  { 
File   deploymentPath  ; 
try  { 
deploymentPath  =  new   File  (  di  .  url  .  toURI  (  )  )  ; 
}  catch  (  URISyntaxException   e  )  { 
deploymentPath  =  new   File  (  di  .  url  .  getPath  (  )  )  ; 
} 
if  (  deploymentPath  .  isDirectory  (  )  )  return   SipApplicationAnnotationUtils  .  findPackageInfoinDirectory  (  deploymentPath  )  ;  else   return   SipApplicationAnnotationUtils  .  findPackageInfoInArchive  (  deploymentPath  )  ; 
} 
} 
} 

