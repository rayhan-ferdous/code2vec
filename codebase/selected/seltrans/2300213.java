package   jeeobserver  ; 

import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  MissingResourceException  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  logging  .  ConsoleHandler  ; 
import   java  .  util  .  logging  .  Handler  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  servlet  .  ServletContext  ; 
import   jeeobserver  .  logger  .  LoggerFormatter  ; 













public   class   JeeObserverServerContext  { 


public   static   final   int  [  ]  VERSION  =  {  3  ,  1  ,  1  }  ; 


public   static   final   String   DATABASE_HANDLER_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_HANDLER"  ; 


public   static   final   String   DATABASE_DRIVER_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_DRIVER"  ; 


public   static   final   String   DATABASE_URL_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_URL"  ; 


public   static   final   String   DATABASE_USER_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_USER"  ; 


public   static   final   String   DATABASE_PASSWORD_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_PASSWORD"  ; 


public   static   final   String   DATABASE_SCHEMA_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_SCHEMA"  ; 


public   static   final   String   DATABASE_CONNECTION_POOL_SIZE_PARAMETER  =  "JEEOBSERVER_SERVER_DATABASE_CONNECTION_POOL_SIZE"  ; 


public   static   final   String   SERVER_PORT_PARAMETER  =  "JEEOBSERVER_SERVER_SERVER_PORT"  ; 


public   static   final   String   LOGGER_LEVEL_PARAMETER  =  "JEEOBSERVER_SERVER_LOGGER_LEVEL"  ; 


public   static   final   String   NOTIFICATOR_TASK_NAME  =  "jeeobserverNotificatorTask"  ; 


public   static   final   String   DATABASE_HANDLER_TASK_NAME  =  "jeeobserverDatabaseHandlerTask"  ; 


public   static   final   String   DEFAULT_DATABASE_HANDLER  =  "jeeobserver.DerbyDatabaseHandler"  ; 


public   static   final   String   DEFAULT_DATABASE_DRIVER  =  "org.apache.derby.jdbc.EmbeddedDriver"  ; 


public   static   final   String   DEFAULT_DATABASE_SCHEMA  =  "JEEOBSERVER_"  +  JeeObserverServerContext  .  VERSION  [  0  ]  +  "_"  +  JeeObserverServerContext  .  VERSION  [  1  ]  ; 


public   static   final   String   DEFAULT_DATABASE_URL  =  "jdbc:derby:./jeeobserver_db/"  +  JeeObserverServerContext  .  DEFAULT_DATABASE_SCHEMA  ; 


public   static   final   String   DEFAULT_DATABASE_USER  =  "SA"  ; 


public   static   final   String   DEFAULT_DATABASE_PASSWORD  =  ""  ; 


public   static   final   int   DEFAULT_SERVER_PORT  =  5688  ; 


public   static   final   Level   DEFAULT_LOGGER_LEVEL  =  Level  .  INFO  ; 


public   static   final   int   DEFAULT_DATABASE_CONNECTION_POOL_SIZE  =  10  ; 


private   static   JeeObserverServerContext   instance  ; 


private   String   sessionId  ; 


private   Date   startTimestamp  ; 


private   String   ip  ; 


private   final   String   operatingSystemName  ; 


private   final   String   operatingSystemVersion  ; 


private   final   String   operatingSystemArchitecture  ; 


private   final   String   javaVersion  ; 


private   final   String   javaVendor  ; 


private   long   notificationsSent  =  0  ; 


private   boolean   enabled  =  false  ; 


private   final   Timer   databaseHandlerTimer  ; 


private   DatabaseHandler   databaseHandler  ; 


private   final   JeeObserverServer   server  ; 


private   final   JeeObserverServerContextProperties   properties  ; 


public   static   final   Logger   logger  =  JeeObserverServerContext  .  createLogger  (  JeeObserverServerContext  .  DEFAULT_LOGGER_LEVEL  )  ; 








private   JeeObserverServerContext  (  JeeObserverServerContextProperties   properties  )  throws   DatabaseException  ,  ServerException  { 
super  (  )  ; 
try  { 
final   MessageDigest   md5  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md5  .  update  (  (  "JE"  +  System  .  currentTimeMillis  (  )  )  .  getBytes  (  )  )  ; 
final   BigInteger   hash  =  new   BigInteger  (  1  ,  md5  .  digest  (  )  )  ; 
this  .  sessionId  =  hash  .  toString  (  16  )  .  toUpperCase  (  )  ; 
}  catch  (  final   Exception   e  )  { 
this  .  sessionId  =  "JE"  +  System  .  currentTimeMillis  (  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  WARNING  ,  "JeeObserver Server session ID MD5 error: {0}"  ,  this  .  sessionId  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINEST  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
try  { 
@  SuppressWarnings  (  "unchecked"  )  final   Class  <  DatabaseHandler  >  databaseHandlerClass  =  (  Class  <  DatabaseHandler  >  )  Class  .  forName  (  properties  .  getDatabaseHandler  (  )  )  ; 
final   Constructor  <  DatabaseHandler  >  handlerConstructor  =  databaseHandlerClass  .  getConstructor  (  new   Class  <  ?  >  [  ]  {  String  .  class  ,  String  .  class  ,  String  .  class  ,  String  .  class  ,  String  .  class  ,  Integer  .  class  }  )  ; 
this  .  databaseHandler  =  handlerConstructor  .  newInstance  (  new   Object  [  ]  {  properties  .  getDatabaseDriver  (  )  ,  properties  .  getDatabaseUrl  (  )  ,  properties  .  getDatabaseUser  (  )  ,  properties  .  getDatabasePassword  (  )  ,  properties  .  getDatabaseSchema  (  )  ,  new   Integer  (  properties  .  getDatabaseConnectionPoolSize  (  )  )  }  )  ; 
}  catch  (  final   Exception   e  )  { 
throw   new   ServerException  (  "Database handler loading exception."  ,  e  )  ; 
} 
this  .  databaseHandlerTimer  =  new   Timer  (  JeeObserverServerContext  .  DATABASE_HANDLER_TASK_NAME  ,  true  )  ; 
this  .  server  =  new   JeeObserverServer  (  properties  .  getServerPort  (  )  )  ; 
this  .  enabled  =  true  ; 
this  .  properties  =  properties  ; 
this  .  startTimestamp  =  new   Date  (  )  ; 
try  { 
this  .  ip  =  InetAddress  .  getLocalHost  (  )  .  getHostAddress  (  )  ; 
}  catch  (  final   UnknownHostException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
this  .  operatingSystemName  =  System  .  getProperty  (  "os.name"  )  ; 
this  .  operatingSystemVersion  =  System  .  getProperty  (  "os.version"  )  ; 
this  .  operatingSystemArchitecture  =  System  .  getProperty  (  "os.arch"  )  ; 
this  .  javaVersion  =  System  .  getProperty  (  "java.version"  )  ; 
this  .  javaVendor  =  System  .  getProperty  (  "java.vendor"  )  ; 
} 








public   static   void   main  (  String  [  ]  arguments  )  throws   DatabaseException  ,  ServerException  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "JeeObserver Server v {0}.{1}.{2}"  ,  new   String  [  ]  {  String  .  valueOf  (  JeeObserverServerContext  .  VERSION  [  0  ]  )  ,  String  .  valueOf  (  JeeObserverServerContext  .  VERSION  [  1  ]  )  ,  String  .  valueOf  (  JeeObserverServerContext  .  VERSION  [  2  ]  )  }  )  ; 
if  (  (  arguments  .  length  >  0  )  &&  (  arguments  [  0  ]  .  equals  (  "-h"  )  ||  arguments  [  0  ]  .  equals  (  "--help"  )  )  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "Usage: JeeObserverServerContext [OPTIONS]..."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "Start jeeObserver server on the specified port and using database located in the specified path."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "List of available options:"  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -dh, --dbhandler:   Database handler class."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: "  +  JeeObserverServerContext  .  DEFAULT_DATABASE_HANDLER  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -dd, --dbdriver:    JDBC Driver class of database."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: "  +  JeeObserverServerContext  .  DEFAULT_DATABASE_DRIVER  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -dr, --dburl:       Database url. Using embedded database like Derby you can specify the directory where save data."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: {0}"  ,  JeeObserverServerContext  .  DEFAULT_DATABASE_URL  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -du, --dbuser:      Database user."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: "  +  JeeObserverServerContext  .  DEFAULT_DATABASE_USER  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -dp, --dbpassword:  Database password."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: (?)"  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -ds, --dbschema:    Database schema."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: {0}"  ,  JeeObserverServerContext  .  DEFAULT_DATABASE_SCHEMA  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -dc, --dbpoolsize:  Database connection pool maximum size."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: {0}"  ,  String  .  valueOf  (  JeeObserverServerContext  .  DEFAULT_DATABASE_CONNECTION_POOL_SIZE  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -p, --port:         Listening port of server."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: {0}"  ,  String  .  valueOf  (  JeeObserverServerContext  .  DEFAULT_SERVER_PORT  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "   -l, --loggerlevel:  Level of logger information displayed."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Possible values: OFF | SEVERE | WARNING | INFO | FINE | FINER | FINEST | ALL"  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "                       Default value: {0}"  ,  JeeObserverServerContext  .  DEFAULT_LOGGER_LEVEL  .  getName  (  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "Visit http:\\\\www.jeeobserver.com\\"  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
final   SimpleDateFormat   simpleDateFormat  =  new   SimpleDateFormat  (  "yyyy"  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "Copyright 2009 - {0} Luca Mingardi."  ,  simpleDateFormat  .  format  (  new   Date  (  )  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  ""  )  ; 
}  else  { 
JeeObserverServerContext  .  createInstance  (  arguments  )  ; 
} 
} 






public   static   JeeObserverServerContext   getInstance  (  )  { 
if  (  JeeObserverServerContext  .  instance  ==  null  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  WARNING  ,  "JeeObserver server context not yet created."  )  ; 
} 
return   JeeObserverServerContext  .  instance  ; 
} 








private   static   JeeObserverServerContextProperties   calculateProperties  (  ServletContext   servletContext  ,  String  [  ]  arguments  )  { 
final   Map  <  String  ,  String  >  parameters  =  new   HashMap  <  String  ,  String  >  (  )  ; 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  ; 
} 
if  (  System  .  getenv  (  )  .  containsKey  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  System  .  getenv  (  )  .  get  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  ; 
} 
ResourceBundle   resourceBundle  =  null  ; 
try  { 
resourceBundle  =  ResourceBundle  .  getBundle  (  "jeeobserver-server"  )  ; 
Set  <  String  >  keysSet  =  new   HashSet  <  String  >  (  )  ; 
Enumeration  <  String  >  keys  =  resourceBundle  .  getKeys  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
keysSet  .  add  (  keys  .  nextElement  (  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.port"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  resourceBundle  .  getString  (  "server.port"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.handler"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.handler"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.driver"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.driver"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.url"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.url"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.user"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.user"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.password"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.password"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.schema"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.schema"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.database.connectionPoolSize"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  resourceBundle  .  getString  (  "server.database.connectionPoolSize"  )  )  ; 
} 
if  (  keysSet  .  contains  (  "server.logger.level"  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  resourceBundle  .  getString  (  "server.logger.level"  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  ; 
} 
if  (  keysSet  .  contains  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  { 
parameters  .  put  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  resourceBundle  .  getString  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  ; 
} 
}  catch  (  final   MissingResourceException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "Properties file \"jeeobserver-server.properties\" not found."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINEST  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
if  (  servletContext  !=  null  )  { 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  ; 
} 
if  (  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  !=  null  )  { 
parameters  .  put  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  servletContext  .  getInitParameter  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  ; 
} 
} 
if  (  arguments  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  arguments  .  length  ;  i  ++  )  { 
if  (  (  arguments  [  i  ]  !=  null  )  &&  !  arguments  [  i  ]  .  trim  (  )  .  equals  (  ""  )  )  { 
if  (  arguments  [  i  ]  .  equals  (  "-p"  )  ||  arguments  [  i  ]  .  equals  (  "--port"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-l"  )  ||  arguments  [  i  ]  .  equals  (  "--loggerlevel"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-dd"  )  ||  arguments  [  i  ]  .  equals  (  "--dbdriver"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-dr"  )  ||  arguments  [  i  ]  .  equals  (  "--dburl"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-du"  )  ||  arguments  [  i  ]  .  equals  (  "--dbuser"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-dp"  )  ||  arguments  [  i  ]  .  equals  (  "--dbpassword"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-dh"  )  ||  arguments  [  i  ]  .  equals  (  "--dbhandler"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-ds"  )  ||  arguments  [  i  ]  .  equals  (  "--dbschema"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  arguments  [  i  ]  )  ; 
}  else   if  (  arguments  [  i  ]  .  equals  (  "-dc"  )  ||  arguments  [  i  ]  .  equals  (  "--dbpoolsize"  )  )  { 
i  =  i  +  1  ; 
parameters  .  put  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  arguments  [  i  ]  )  ; 
} 
} 
} 
} 
for  (  final   Map  .  Entry  <  String  ,  String  >  entry  :  parameters  .  entrySet  (  )  )  { 
entry  .  setValue  (  entry  .  getValue  (  )  .  trim  (  )  )  ; 
} 
final   JeeObserverServerContextProperties   properties  =  new   JeeObserverServerContextProperties  (  )  ; 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  { 
try  { 
properties  .  setServerPort  (  Integer  .  parseInt  (  parameters  .  get  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  )  ; 
}  catch  (  final   NumberFormatException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  "Parameter "  +  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  +  " = {0} is not a number."  ,  parameters  .  get  (  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINEST  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  { 
properties  .  setDatabaseHandler  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  { 
properties  .  setDatabaseDriver  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  { 
properties  .  setDatabaseUrl  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  { 
properties  .  setDatabaseUser  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  { 
properties  .  setDatabasePassword  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  { 
properties  .  setDatabaseSchema  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  )  )  ; 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  { 
try  { 
properties  .  setDatabaseConnectionPoolSize  (  Integer  .  parseInt  (  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  )  ; 
}  catch  (  final   NumberFormatException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  +  " = {0} is not a number."  ,  parameters  .  get  (  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  )  )  ; 
} 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  +  " not found."  )  ; 
} 
if  (  parameters  .  containsKey  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  { 
try  { 
properties  .  setLoggerLevel  (  Level  .  parse  (  parameters  .  get  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  )  ; 
}  catch  (  final   Exception   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  "Parameter "  +  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  +  " = {0} is not a valid Level. (Available values: OFF | SEVERE | WARNING | INFO | FINE | FINER | FINEST | ALL)"  ,  parameters  .  get  (  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  )  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINEST  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
}  else  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "Parameter "  +  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  +  " not found."  )  ; 
} 
return   properties  ; 
} 









public   static   JeeObserverServerContext   createInstance  (  JeeObserverServerContextProperties   properties  )  throws   DatabaseException  ,  ServerException  { 
JeeObserverServerContext  .  logger  .  setLevel  (  properties  .  getLoggerLevel  (  )  )  ; 
final   JeeObserverServerContext   newInstance  =  new   JeeObserverServerContext  (  properties  )  ; 
newInstance  .  getDatabaseHandlerTimer  (  )  .  schedule  (  new   DatabaseHandlerTimerTask  (  )  ,  DatabaseHandlerTimerTask  .  TIMER_INTERVAL  ,  DatabaseHandlerTimerTask  .  TIMER_INTERVAL  )  ; 
newInstance  .  getServer  (  )  .  start  (  )  ; 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   Thread  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
if  (  JeeObserverServerContext  .  getInstance  (  )  !=  null  )  { 
JeeObserverServerContext  .  getInstance  (  )  .  close  (  )  ; 
} 
}  catch  (  final   DatabaseException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  e  .  getMessage  (  )  ,  e  )  ; 
}  catch  (  final   ServerException   e  )  { 
JeeObserverServerContext  .  logger  .  log  (  Level  .  SEVERE  ,  e  .  getMessage  (  )  ,  e  )  ; 
} 
} 
}  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "JeeObserver server context instance created."  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server operating system: {0} {1} - {2}"  ,  new   Object  [  ]  {  newInstance  .  getOperatingSystemName  (  )  ,  newInstance  .  getOperatingSystemVersion  (  )  ,  newInstance  .  getOperatingSystemArchitecture  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server java: {0} {1}"  ,  new   Object  [  ]  {  newInstance  .  getJavaVendor  (  )  ,  newInstance  .  getJavaVersion  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  LOGGER_LEVEL_PARAMETER  ,  properties  .  getLoggerLevel  (  )  .  getName  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  SERVER_PORT_PARAMETER  ,  String  .  valueOf  (  properties  .  getServerPort  (  )  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_HANDLER_PARAMETER  ,  properties  .  getLoggerLevel  (  )  .  getName  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_DRIVER_PARAMETER  ,  properties  .  getDatabaseDriver  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_URL_PARAMETER  ,  properties  .  getDatabaseUrl  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_USER_PARAMETER  ,  properties  .  getDatabaseUser  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_PASSWORD_PARAMETER  ,  properties  .  getDatabasePassword  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_SCHEMA_PARAMETER  ,  properties  .  getDatabaseSchema  (  )  }  )  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  FINE  ,  "JeeObserver server parameter: {0} = {1}"  ,  new   Object  [  ]  {  JeeObserverServerContext  .  DATABASE_CONNECTION_POOL_SIZE_PARAMETER  ,  String  .  valueOf  (  properties  .  getDatabaseConnectionPoolSize  (  )  )  }  )  ; 
JeeObserverServerContext  .  instance  =  newInstance  ; 
return   newInstance  ; 
} 








public   static   JeeObserverServerContext   createInstance  (  )  throws   DatabaseException  ,  ServerException  { 
return   JeeObserverServerContext  .  createInstance  (  JeeObserverServerContext  .  calculateProperties  (  null  ,  null  )  )  ; 
} 









public   static   JeeObserverServerContext   createInstance  (  ServletContext   servletContext  )  throws   DatabaseException  ,  ServerException  { 
return   JeeObserverServerContext  .  createInstance  (  JeeObserverServerContext  .  calculateProperties  (  servletContext  ,  null  )  )  ; 
} 









public   static   JeeObserverServerContext   createInstance  (  String  [  ]  arguments  )  throws   DatabaseException  ,  ServerException  { 
return   JeeObserverServerContext  .  createInstance  (  JeeObserverServerContext  .  calculateProperties  (  null  ,  arguments  )  )  ; 
} 







public   void   close  (  )  throws   DatabaseException  ,  ServerException  { 
if  (  this  .  enabled  ==  true  )  { 
this  .  enabled  =  false  ; 
this  .  databaseHandlerTimer  .  cancel  (  )  ; 
this  .  server  .  setEnabled  (  false  )  ; 
this  .  server  .  close  (  )  ; 
this  .  getDatabaseHandler  (  )  .  stopDatabase  (  )  ; 
this  .  startTimestamp  =  null  ; 
JeeObserverServerContext  .  logger  .  log  (  Level  .  INFO  ,  "JeeObserver server context instance destroyed."  )  ; 
} 
} 







private   static   Logger   createLogger  (  Level   loggerLevel  )  { 
final   Logger   newLogger  =  Logger  .  getLogger  (  "jeeobserver_server"  )  ; 
newLogger  .  setUseParentHandlers  (  false  )  ; 
newLogger  .  setLevel  (  loggerLevel  )  ; 
final   Handler   handlerArray  [  ]  =  newLogger  .  getHandlers  (  )  ; 
for  (  final   Handler   element  :  handlerArray  )  { 
newLogger  .  removeHandler  (  element  )  ; 
} 
final   Handler   handler  =  new   ConsoleHandler  (  )  ; 
handler  .  setLevel  (  Level  .  ALL  )  ; 
handler  .  setFormatter  (  new   LoggerFormatter  (  )  )  ; 
newLogger  .  addHandler  (  handler  )  ; 
return   newLogger  ; 
} 






public   boolean   isEnabled  (  )  { 
return   this  .  enabled  ; 
} 






long   incrementNotification  (  )  { 
this  .  notificationsSent  =  this  .  notificationsSent  +  1  ; 
return   this  .  notificationsSent  ; 
} 






public   long   getActiveRequests  (  )  { 
return   this  .  server  .  getActiveRequests  (  )  ; 
} 






public   long   getTotalRequests  (  )  { 
return   this  .  server  .  getTotalRequests  (  )  ; 
} 






public   long   getExecutedRequests  (  )  { 
return   this  .  server  .  getTotalRequests  (  )  -  this  .  server  .  getActiveRequests  (  )  ; 
} 






public   long   getTotalDatabaseRequests  (  )  { 
return   this  .  getDatabaseHandler  (  )  .  getTotalInserts  (  )  ; 
} 






public   long   getDatabasePoolSize  (  )  { 
return   this  .  getDatabaseHandler  (  )  .  getPoolSize  (  )  ; 
} 






public   String   getDatabaseHandlerName  (  )  { 
return   this  .  getDatabaseHandler  (  )  .  getHandler  (  )  ; 
} 






public   String   getDatabaseUrl  (  )  { 
return   this  .  getDatabaseHandler  (  )  .  getUrl  (  )  ; 
} 






public   String   getDatabaseDriver  (  )  { 
return   this  .  getDatabaseHandler  (  )  .  getDriver  (  )  ; 
} 






public   long   getNotificationsSent  (  )  { 
return   this  .  notificationsSent  ; 
} 






public   Date   getStartTimestamp  (  )  { 
return   this  .  startTimestamp  ; 
} 






public   String   getJavaVendor  (  )  { 
return   this  .  javaVendor  ; 
} 






public   String   getSessionId  (  )  { 
return   this  .  sessionId  ; 
} 






public   String   getJavaVersion  (  )  { 
return   this  .  javaVersion  ; 
} 






public   String   getOperatingSystemName  (  )  { 
return   this  .  operatingSystemName  ; 
} 






public   String   getOperatingSystemArchitecture  (  )  { 
return   this  .  operatingSystemArchitecture  ; 
} 






public   String   getOperatingSystemVersion  (  )  { 
return   this  .  operatingSystemVersion  ; 
} 






public   JeeObserverServerContextProperties   getProperties  (  )  { 
return   this  .  properties  ; 
} 






public   String   getIp  (  )  { 
return   this  .  ip  ; 
} 






public   DatabaseHandler   getDatabaseHandler  (  )  { 
return   this  .  databaseHandler  ; 
} 






private   Timer   getDatabaseHandlerTimer  (  )  { 
return   this  .  databaseHandlerTimer  ; 
} 






private   JeeObserverServer   getServer  (  )  { 
return   this  .  server  ; 
} 
} 

