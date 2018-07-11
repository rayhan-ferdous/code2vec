package   org  .  tcpfile  .  main  ; 

import   java  .  awt  .  BorderLayout  ; 
import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Component  ; 
import   java  .  awt  .  Container  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  Point  ; 
import   java  .  awt  .  Rectangle  ; 
import   java  .  awt  .  Robot  ; 
import   java  .  awt  .  event  .  MouseAdapter  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  beans  .  PropertyChangeEvent  ; 
import   java  .  beans  .  PropertyChangeListener  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  security  .  SecureRandom  ; 
import   java  .  sql  .  ResultSet  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  concurrent  .  atomic  .  AtomicBoolean  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JScrollPane  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  JTextArea  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   javax  .  swing  .  text  .  BadLocationException  ; 
import   javax  .  xml  .  transform  .  OutputKeys  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   org  .  apache  .  commons  .  io  .  FileUtils  ; 
import   org  .  h2  .  tools  .  Server  ; 
import   org  .  slf4j  .  Logger  ; 
import   org  .  slf4j  .  LoggerFactory  ; 
import   org  .  tcpfile  .  crypto  .  CryptServerAccessor  ; 
import   org  .  tcpfile  .  crypto  .  Encryption  ; 
import   org  .  tcpfile  .  crypto  .  RSA  ; 
import   org  .  tcpfile  .  fileio  .  CFile  ; 
import   org  .  tcpfile  .  fileio  .  FileHandling  ; 
import   org  .  tcpfile  .  fileio  .  Hasher  ; 
import   org  .  tcpfile  .  fileio  .  Share  ; 
import   org  .  tcpfile  .  gui  .  GUI  ; 
import   org  .  tcpfile  .  gui  .  GenericTableModel  ; 
import   org  .  tcpfile  .  gui  .  settingsmanager  .  HookToSetting  ; 
import   org  .  tcpfile  .  gui  .  settingsmanager  .  SettingHookGenerator  ; 
import   org  .  tcpfile  .  gui  .  settingsmanager  .  SettingsManager  ; 
import   org  .  tcpfile  .  gui  .  settingsmanager  .  settings  .  Setting  ; 
import   org  .  tcpfile  .  gui  .  wizards  .  FirstTimeWizard  ; 
import   org  .  tcpfile  .  net  .  BandwidthAnalyzer  ; 
import   org  .  tcpfile  .  net  .  ByteArray  ; 
import   org  .  tcpfile  .  net  .  Connection  ; 
import   org  .  tcpfile  .  net  .  ContactAddress  ; 
import   org  .  tcpfile  .  net  .  UDPBroadcasting  ; 
import   org  .  tcpfile  .  net  .  messages  .  AwayMessage  .  Status  ; 
import   org  .  tcpfile  .  net  .  p2p  .  SQLFileDescriptor  ; 
import   org  .  tcpfile  .  platforms  .  PlatformCaller  ; 
import   org  .  tcpfile  .  plugin  .  PluginHandler  ; 
import   org  .  tcpfile  .  sql  .  QueryWriter  ; 
import   org  .  tcpfile  .  sql  .  SQLConnection  ; 
import   org  .  tcpfile  .  sql  .  SQLPersistantObjects  ; 
import   org  .  tcpfile  .  update  .  Updater  ; 
import   org  .  tcpfile  .  update  .  updatescripts  .  VersionJump  ; 
import   org  .  tcpfile  .  utils  .  AwayManager  ; 
import   org  .  tcpfile  .  utils  .  BrowserControl  ; 
import   org  .  tcpfile  .  utils  .  PeriodChecker  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   com  .  sun  .  org  .  apache  .  xerces  .  internal  .  parsers  .  DOMParser  ; 







public   class   Misc  { 

private   static   Logger   log  =  LoggerFactory  .  getLogger  (  Misc  .  class  )  ; 

public   static   final   String   NEWLINE  =  System  .  getProperty  (  "line.separator"  )  ; 

public   static   final   String   FOLDERSEPARATOR  =  System  .  getProperty  (  "file.separator"  )  ; 

public   static   final   String   OS  =  System  .  getProperty  (  "os.name"  )  ; 

public   static   final   boolean   isWindows  =  OS  .  toLowerCase  (  )  .  contains  (  "windows"  )  ; 




public   static   final   String   VERSIONNUMBER  =  "0.3.7"  ; 

public   static   final   SecureRandom   random  =  new   SecureRandom  (  )  ; 

public   static   Contact   myself  ; 

public   static   int   numberOfUsers  ; 

public   static   String   ipcache  ; 

public   static   RSA   authKey  =  null  ; 

public   static   Vector  <  String  >  runnables  =  new   Vector  <  String  >  (  )  ; 

public   static   Timer   timer  =  new   Timer  (  "TimerTasks"  ,  true  )  ; 

public   static   Hasher   hasher  =  new   Hasher  (  )  ; 

public   static   Map  <  String  ,  Share  >  shares  =  new   HashMap  <  String  ,  Share  >  (  )  ; 

public   static   boolean   isAlive  =  true  ; 

public   static   PluginHandler   plugins  ; 

public   static   volatile   SettingsManager   settings  =  SettingsManager  .  settingsManager  ; 

public   static   boolean   initLaterFinished  =  false  ; 

public   static   boolean   mayPoll  =  false  ; 

public   static   SQLConnection   sql  ; 

public   static   SQLConnection   othersql  ; 

private   static   PeriodChecker   gcPeriod  =  new   PeriodChecker  (  1000  )  ; 

private   static   final   File   RSAFILE  =  new   CFile  (  "RSA.key"  )  ; 

@  HookToSetting 
static   boolean   scrollConsole  ; 

@  HookToSetting  (  getName  =  "ConsoleLimit"  ) 
static   int   consoleLimit  ; 

public   static   AwayManager   awayManager  ; 

private   Misc  (  )  { 
} 








public   static   void   init  (  )  { 
settings  =  SettingsManager  .  settingsManager  ; 
Thread  .  currentThread  (  )  .  setPriority  (  Thread  .  MAX_PRIORITY  )  ; 
SettingHookGenerator  .  generateHooks  (  Misc  .  class  )  ; 
final   Thread   RSA  =  new   Thread  (  "RSALoad"  )  { 

public   void   run  (  )  { 
this  .  setPriority  (  Thread  .  MIN_PRIORITY  )  ; 
if  (  !  RSAFILE  .  exists  (  )  )  { 
log  .  info  (  "No Authentication Key found, generating one"  )  ; 
RSA   key  =  new   RSA  (  3000  )  ; 
if  (  authKey  ==  null  )  authKey  =  key  ; 
if  (  !  RSAFILE  .  exists  (  )  )  { 
saveObject  (  RSAFILE  .  getAbsolutePath  (  )  ,  authKey  )  ; 
log  .  info  (  "New Authentication Key generated and saved"  )  ; 
}  else   log  .  info  (  "Authentication Key not saved because already one here!"  )  ; 
}  else   authKey  =  (  RSA  )  Misc  .  loadObject  (  RSAFILE  .  getAbsolutePath  (  )  )  ; 
} 
}  ; 
RSA  .  start  (  )  ; 
Setting   namesetting  =  SettingsManager  .  settingsManager  .  findSetting  (  "Name"  )  ; 
String   n  =  namesetting  .  getValue  (  )  .  toString  (  )  ; 
final   AtomicBoolean   wizarddone  =  new   AtomicBoolean  (  false  )  ; 
final   PropertyChangeListener   pcl  =  new   PropertyChangeListener  (  )  { 

@  Override 
public   void   propertyChange  (  PropertyChangeEvent   evt  )  { 
wizarddone  .  set  (  true  )  ; 
} 
}  ; 
if  (  !  checkName  (  n  )  )  { 
if  (  n  .  equals  (  "Unknown"  )  )  { 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
FirstTimeWizard   ftw  =  new   FirstTimeWizard  (  )  ; 
ftw  .  addPropertyChangeListener  (  pcl  )  ; 
ftw  .  startWizard  (  )  ; 
} 
}  )  ; 
}  else  { 
BuddyList  .  askForNewName  (  "Your name is invalid"  )  ; 
wizarddone  .  set  (  true  )  ; 
} 
}  else   wizarddone  .  set  (  true  )  ; 
if  (  SettingsManager  .  settingsManager  .  findSetting  (  "defaultSaveFolder"  )  .  getValue  (  )  .  toString  (  )  .  equals  (  ""  )  )  Misc  .  askForDirectory  (  )  ; 
try  { 
RSA  .  join  (  )  ; 
while  (  !  wizarddone  .  get  (  )  )  sleeps  (  100  )  ; 
}  catch  (  InterruptedException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 
awayManager  =  new   AwayManager  (  )  ; 
myself  =  new   Contact  (  "127.0.0.1"  ,  namesetting  .  getValue  (  )  .  toString  (  )  ,  "9999"  ,  authKey  .  getPublic  (  )  )  ; 
Setting   s  =  settings  .  findSetting  (  "SettingsXMLVersion"  )  ; 
if  (  !  s  .  getValue  (  )  .  equals  (  Misc  .  VERSIONNUMBER  )  )  { 
log  .  info  (  "First start on new version {}"  ,  Misc  .  VERSIONNUMBER  )  ; 
VersionJump  .  jumpFrom  (  s  .  getValue  (  )  .  toString  (  )  ,  new   SQLConnection  (  )  )  ; 
s  .  setValue  (  Misc  .  VERSIONNUMBER  )  ; 
SettingsManager  .  settingsManager  .  saveToXML  (  null  )  ; 
} 
BuddyList  .  loadFromXML  (  )  ; 
settings  =  SettingsManager  .  settingsManager  ; 
if  (  settings  .  findSetting  (  "useDatabaseDebugMode"  )  .  getOriginalBoolean  (  )  )  { 
try  { 
Server  .  createTcpServer  (  new   String  [  ]  {  "-baseDir"  ,  new   CFile  (  "db"  )  .  getAbsolutePath  (  )  }  )  .  start  (  )  ; 
Server   ws  =  Server  .  createWebServer  (  null  )  ; 
ws  .  start  (  )  ; 
BrowserControl  .  displayURL  (  ws  .  getURL  (  )  )  ; 
}  catch  (  SQLException   e1  )  { 
log  .  warn  (  ""  ,  e1  )  ; 
} 
} 
sql  =  new   SQLConnection  (  )  ; 
othersql  =  new   SQLConnection  (  "shares"  )  ; 
loadShares  (  )  ; 
Thread  .  currentThread  (  )  .  setPriority  (  Thread  .  NORM_PRIORITY  )  ; 
} 





public   static   void   takeOverOldInstallation  (  File   folder  )  { 
takeOverFile  (  new   File  (  folder  ,  "RSA.key"  )  )  ; 
if  (  RSAFILE  .  exists  (  )  )  Misc  .  authKey  =  (  RSA  )  Misc  .  loadObject  (  RSAFILE  .  getAbsolutePath  (  )  )  ; 
takeOverFile  (  new   File  (  folder  ,  "buddies.xml"  )  )  ; 
takeOverFile  (  new   File  (  folder  ,  "db"  )  )  ; 
takeOverFile  (  new   File  (  folder  ,  "history"  )  )  ; 
takeOverFile  (  new   File  (  folder  ,  "workspace"  )  )  ; 
File   oldSettings  =  new   File  (  folder  .  getAbsolutePath  (  )  ,  "settings.xml"  )  ; 
if  (  oldSettings  .  exists  (  )  )  { 
log  .  info  (  "Found settings.xml "  )  ; 
SettingsManager   sm  =  SettingsManager  .  loadFromXML  (  oldSettings  .  getAbsolutePath  (  )  )  ; 
if  (  sm  !=  null  )  { 
SettingsManager  .  settingsManager  .  takeOverSettings  (  sm  )  ; 
}  else  { 
log  .  info  (  "Failed to load settings.xml"  )  ; 
} 
} 
SettingsManager  .  settingsManager  .  saveToXML  (  null  )  ; 
authKey  =  (  RSA  )  loadObject  (  "RSA.key"  )  ; 
} 






public   static   void   takeOverFile  (  File   file  )  { 
if  (  file  .  exists  (  )  )  { 
log  .  info  (  "Found "  +  file  .  getName  (  )  )  ; 
File   newFile  =  new   CFile  (  file  .  getName  (  )  )  ; 
try  { 
if  (  file  .  isDirectory  (  )  )  { 
FileUtils  .  copyDirectory  (  file  ,  newFile  )  ; 
}  else  { 
FileUtils  .  copyFile  (  file  ,  newFile  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  info  (  "Unable to copy "  +  file  .  getName  (  )  ,  e  )  ; 
} 
} 
} 

public   static   void   initLater  (  )  { 
initLaterSameThread  (  )  ; 
final   Thread   initLaterThread  =  new   Thread  (  "initLater"  )  { 

public   void   run  (  )  { 
Misc  .  initLaterThreaded  (  )  ; 
} 
}  ; 
initLaterThread  .  start  (  )  ; 
startPlugins  (  )  ; 
EntryPoint  .  gui  .  loadWorkspace  (  GUI  .  DOCKAUTOSAVENAME  )  ; 
UDPBroadcasting  .  start  (  )  ; 
initLaterFinished  =  true  ; 
} 






public   static   void   runRunnableInThread  (  Runnable   r  ,  String   name  )  { 
final   Thread   runnableThread  =  new   Thread  (  r  ,  name  )  { 

protected   void   finalize  (  )  throws   Throwable  { 
runnables  .  remove  (  this  .  getName  (  )  )  ; 
super  .  finalize  (  )  ; 
} 
}  ; 
runnableThread  .  setPriority  (  Thread  .  MIN_PRIORITY  )  ; 
runnableThread  .  setDaemon  (  true  )  ; 
runnableThread  .  start  (  )  ; 
runnables  .  add  (  name  )  ; 
} 







public   static   void   runRunnableInSingletonThread  (  Runnable   r  ,  String   name  )  { 
forceGarbageCollection  (  )  ; 
if  (  runnables  .  contains  (  name  )  )  { 
log  .  debug  (  "Thread of this type already running {}"  ,  name  )  ; 
return  ; 
} 
runnables  .  add  (  name  )  ; 
final   Thread   runnableThread  =  new   Thread  (  r  ,  name  )  { 

protected   void   finalize  (  )  throws   Throwable  { 
log  .  debug  (  "Thread {} has been collected"  ,  this  .  getName  (  )  )  ; 
runnables  .  remove  (  this  .  getName  (  )  )  ; 
super  .  finalize  (  )  ; 
} 
}  ; 
runnableThread  .  setPriority  (  Thread  .  MIN_PRIORITY  )  ; 
runnableThread  .  setDaemon  (  true  )  ; 
runnableThread  .  start  (  )  ; 
} 

public   static   void   forceGarbageCollection  (  )  { 
if  (  gcPeriod  .  check  (  )  )  { 
Runtime  .  getRuntime  (  )  .  gc  (  )  ; 
} 
} 





public   static   void   initLaterSameThread  (  )  { 
BuddyList  .  buildTree  (  )  ; 
EntryPoint  .  gui  .  treeRebuildThreaded  (  )  ; 
} 

public   static   void   initLaterThreaded  (  )  { 
CryptServerAccessor  .  addBackgroundWork  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
for  (  Share   s  :  shares  .  values  (  )  )  s  .  updateShare  (  false  )  ; 
} 
}  )  ; 
Connection  .  init  (  )  ; 
startTimerTasks  (  )  ; 
Runnable   extip  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
myself  .  address  =  new   ContactAddress  (  getExternalIP  (  )  ,  SettingsManager  .  settingsManager  .  findSetting  (  "ListenerPort"  )  .  getInteger  (  )  )  ; 
mayPoll  =  true  ; 
Updater  .  updateCheck  (  )  ; 
log  .  info  (  "Your Contact Info: \n"  +  myself  .  toCopyString  (  )  )  ; 
BuddyList  .  contactAdvertiser  (  )  ; 
BuddyList  .  pollAllContacts  (  )  ; 
} 
}  ; 
Misc  .  runRunnableInThread  (  extip  ,  "External IP"  )  ; 
Misc  .  updateNumberOfUsers  (  )  ; 
if  (  settings  .  findSetting  (  "AutocheckPort"  )  .  getBoolean  (  )  )  { 
Misc  .  checkPortStatus  (  )  ; 
} 
update  (  )  ; 
Encryption  .  testUnlimited  (  )  ; 
log  .  info  (  "Unlimited enabled: "  +  Encryption  .  unlimited  )  ; 
SQLPersistantObjects  .  loadSQLPersistantObjects  (  )  ; 
try  { 
ResultSet   rs  =  sql  .  getResultSet  (  "select sum(size),count(size) from files where isfolder='0'"  )  ; 
if  (  rs  .  next  (  )  )  { 
long   totalsize  =  rs  .  getLong  (  1  )  ; 
long   count  =  rs  .  getLong  (  2  )  ; 
log  .  info  (  "There are {} files in database, in total {}"  ,  count  ,  prettyPrintSize  (  totalsize  )  )  ; 
} 
rs  .  close  (  )  ; 
}  catch  (  SQLException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 
if  (  settings  .  findSetting  (  "useDatabaseDebugMode"  )  .  getOriginalBoolean  (  )  )  { 
log  .  info  (  "Use this Connection url: jdbc:h2:"  +  SQLFileDescriptor  .  convertPathToMultiPlatform  (  new   File  (  "db/files"  )  .  getAbsolutePath  (  )  )  )  ; 
} 
awayManager  .  addPropertyChangeListener  (  new   PropertyChangeListener  (  )  { 

@  Override 
public   void   propertyChange  (  PropertyChangeEvent   evt  )  { 
Status   s  =  myself  .  lastAwayMessage  .  status  ; 
if  (  s  ==  Status  .  Online  ||  s  ==  Status  .  Away  )  BuddyList  .  setAway  (  (  Status  )  evt  .  getNewValue  (  )  )  ; 
} 
}  )  ; 
} 

public   static   void   exit  (  )  { 
exit  (  true  )  ; 
} 






public   static   void   exit  (  boolean   waitforplugins  )  { 
try  { 
Thread   plugshutdown  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
plugins  .  shutdown  (  )  ; 
} 
}  )  ; 
if  (  SettingsManager  .  settingsManager  .  findSetting  (  "usePlugins"  )  .  getOriginalBoolean  (  )  )  { 
plugshutdown  .  start  (  )  ; 
} 
logSQLStatistics  (  )  ; 
FileHandling  .  printHitsMisses  (  )  ; 
isAlive  =  false  ; 
SQLPersistantObjects  .  saveSQLPersistantObjects  (  )  ; 
sql  .  sendUpdate  (  "delete from subhashes where lastused<='"  +  Misc  .  getTime  (  -  7  *  24  *  3600  ,  true  ,  true  ,  false  )  +  "'"  )  ; 
EntryPoint  .  gui  .  saveOptionsToOptions  (  )  ; 
EntryPoint  .  gui  .  saveWorkspace  (  GUI  .  DOCKAUTOSAVENAME  )  ; 
SettingsManager  .  settingsManager  .  saveToXML  (  null  )  ; 
BuddyList  .  toXML  (  )  ; 
if  (  SettingsManager  .  settingsManager  .  findSetting  (  "usePlugins"  )  .  getOriginalBoolean  (  )  )  { 
try  { 
if  (  waitforplugins  )  plugshutdown  .  join  (  10000  )  ; 
}  catch  (  RuntimeException   e  )  { 
log  .  warn  (  "Plugins were not shut down correctly"  ,  e  )  ; 
} 
} 
}  catch  (  Throwable   t  )  { 
log  .  warn  (  "Error while shutting down"  ,  t  )  ; 
} 
} 

public   static   String   fromSecondsToTime  (  long   sec  )  { 
String   out  =  ""  ; 
out  =  (  sec  %  60  )  +  out  ; 
if  (  sec  <  60  )  return   out  ; 
if  (  out  .  length  (  )  <  2  )  out  =  "0"  +  out  ; 
out  =  (  sec  %  3600  )  /  60  +  ":"  +  out  ; 
if  (  sec  <  3600  )  return   out  ; 
if  (  out  .  length  (  )  <  5  )  out  =  "0"  +  out  ; 
out  =  (  sec  %  86400  )  /  3600  +  ":"  +  out  ; 
if  (  out  .  length  (  )  <  8  )  out  =  "0"  +  out  ; 
if  (  sec  >  86400  )  { 
long   days  =  sec  /  86400  ; 
out  =  days  +  "d "  +  out  ; 
} 
return   out  ; 
} 




static   String   getExternalIP  (  )  { 
String   page  =  null  ; 
while  (  page  ==  null  )  { 
page  =  downloadpage  (  "http://checkip.dyndns.org/"  )  ; 
} 
page  =  page  .  replaceAll  (  "\\<.*?\\>"  ,  ""  )  ; 
String   regex  =  "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b"  ; 
Pattern   p  =  Pattern  .  compile  (  regex  )  ; 
Matcher   m  =  p  .  matcher  (  page  )  ; 
if  (  m  .  find  (  )  )  { 
page  =  m  .  group  (  0  )  ; 
} 
assert  (  page  .  matches  (  regex  )  )  ; 
return   page  ; 
} 

private   static   void   startPlugins  (  )  { 
if  (  SettingsManager  .  settingsManager  .  findSetting  (  "usePlugins"  )  .  getOriginalBoolean  (  )  )  { 
plugins  =  new   PluginHandler  (  )  ; 
plugins  .  loadAllPlugins  (  )  ; 
} 
} 

public   static   void   logSQLStatistics  (  )  { 
log  .  info  (  "SQL Connections opened: {}"  ,  SQLConnection  .  connectionsOpened  )  ; 
log  .  info  (  "SQL Queries sent: {}"  ,  SQLConnection  .  queriesSent  )  ; 
log  .  info  (  "SQL Updates sent: {}"  ,  SQLConnection  .  updatesSent  )  ; 
} 

public   static   boolean   openExplorer  (  File   file  )  { 
return   PlatformCaller  .  openExplorer  (  file  )  ; 
} 

public   static   void   takeScreenCap  (  Component   target  ,  String   filename  )  { 
Point   upperleft  =  target  .  getLocationOnScreen  (  )  ; 
Rectangle   r  =  target  .  getBounds  (  )  ; 
Rectangle   cap  =  new   Rectangle  (  upperleft  ,  new   Dimension  (  r  .  width  ,  r  .  height  )  )  ; 
takeScreenCap  (  cap  ,  filename  )  ; 
} 

public   static   void   takeScreenCap  (  Rectangle   rv  ,  String   filename  )  { 
try  { 
Robot   robot  =  new   Robot  (  )  ; 
BufferedImage   bi  =  robot  .  createScreenCapture  (  rv  )  ; 
new   CFile  (  "caps"  )  .  mkdir  (  )  ; 
File   file  =  new   CFile  (  "caps/"  +  filename  +  ".jpg"  )  ; 
ImageIO  .  write  (  bi  ,  "jpg"  ,  file  )  ; 
}  catch  (  Throwable   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 
} 

public   static   void   createOneTimePad  (  String   file  ,  long   length  )  { 
log  .  info  (  "Starting "  +  file  +  " size "  +  length  )  ; 
long   current  =  0  ; 
int   onego  =  5012  ; 
try  { 
SecureRandom   sr  =  new   SecureRandom  (  )  ; 
SecureRandom   decider  =  new   SecureRandom  (  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  file  )  ; 
while  (  current  <  length  )  { 
log  .  trace  (  "Progress: "  +  Misc  .  prettyPrintSize  (  current  )  )  ; 
byte  [  ]  b  =  new   byte  [  onego  ]  ; 
if  (  decider  .  nextBoolean  (  )  )  random  .  nextBytes  (  b  )  ;  else   sr  .  nextBytes  (  b  )  ; 
fos  .  write  (  b  )  ; 
current  +=  onego  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 
log  .  info  (  "Stopping "  +  file  +  " size "  +  length  )  ; 
} 

public   static   byte  [  ]  xor  (  byte  [  ]  message  ,  byte  [  ]  key  )  { 
byte  [  ]  encrypted  =  new   byte  [  message  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  message  .  length  ;  i  ++  )  { 
encrypted  [  i  ]  =  (  (  byte  )  (  key  [  i  ]  ^  message  [  i  ]  )  )  ; 
} 
return   encrypted  ; 
} 

public   static   String   changeBase  (  String   base1  )  { 
String   out  =  ""  ; 
ArrayList  <  Character  >  base  =  get62Base  (  )  ; 
BigInteger   b  =  new   BigInteger  (  base1  )  ; 
BigInteger   length  =  new   BigInteger  (  ""  +  base  .  size  (  )  )  ; 
while  (  !  b  .  equals  (  BigInteger  .  ZERO  )  )  { 
BigInteger   cur  =  b  .  remainder  (  length  )  ; 
int   i  =  Integer  .  parseInt  (  ""  +  cur  )  ; 
out  =  base  .  get  (  i  )  +  out  ; 
b  =  b  .  divide  (  length  )  ; 
} 
return   out  ; 
} 

public   static   String   changeBaseBack  (  String   base1  )  { 
ArrayList  <  Character  >  base  =  get62Base  (  )  ; 
BigInteger   length  =  new   BigInteger  (  ""  +  base  .  size  (  )  )  ; 
BigInteger   out  =  new   BigInteger  (  "0"  )  ; 
BigInteger   mul  =  new   BigInteger  (  "1"  )  ; 
for  (  int   i  =  base1  .  length  (  )  -  1  ;  i  >=  0  ;  i  --  )  { 
char   cur  =  base1  .  charAt  (  i  )  ; 
BigInteger   current  =  new   BigInteger  (  ""  +  base  .  indexOf  (  cur  )  )  ; 
current  =  current  .  multiply  (  mul  )  ; 
out  =  out  .  add  (  current  )  ; 
mul  =  mul  .  multiply  (  length  )  ; 
} 
return   out  +  ""  ; 
} 

public   static   ArrayList  <  Character  >  get62Base  (  )  { 
ArrayList  <  Character  >  base  =  new   ArrayList  <  Character  >  (  )  ; 
char   c  =  '0'  ; 
for  (  int   i  =  0  ;  i  <  10  ;  i  ++  )  { 
base  .  add  (  c  ++  )  ; 
} 
c  =  'a'  ; 
char   d  =  'A'  ; 
for  (  int   i  =  0  ;  i  <  26  ;  i  ++  )  { 
base  .  add  (  c  ++  )  ; 
base  .  add  (  d  ++  )  ; 
} 
return   base  ; 
} 

public   static   void   loadShares  (  )  { 
ArrayList  <  Share  >  s  =  QueryWriter  .  selectObjects  (  Share  .  class  ,  "select * from shares"  ,  sql  )  ; 
for  (  Share   sh  :  s  )  { 
if  (  !  shares  .  containsKey  (  sh  .  shareid  )  )  { 
shares  .  put  (  sh  .  shareid  ,  sh  )  ; 
log  .  trace  (  "Loaded share: "  +  sh  .  sharename  )  ; 
} 
} 
} 






public   static   Color   parseColorFromString  (  String   rgbString  )  { 
if  (  rgbString  ==  null  )  { 
return   Color  .  black  ; 
} 
String  [  ]  splittedRGB  =  (  rgbString  )  .  replaceAll  (  "[^0-9,]"  ,  ""  )  .  split  (  ","  )  ; 
if  (  splittedRGB  .  length  !=  3  )  { 
String   colorName  =  rgbString  .  toUpperCase  (  )  .  replaceAll  (  "[^A-Z]"  ,  ""  )  ; 
Color   colorByName  ; 
try  { 
colorByName  =  (  Color  )  Class  .  forName  (  "java.awt.Color"  )  .  getField  (  colorName  )  .  get  (  null  )  ; 
}  catch  (  Exception   e  )  { 
return   Color  .  black  ; 
} 
return   colorByName  ; 
} 
Integer  [  ]  rgb  =  new   Integer  [  3  ]  ; 
for  (  int   i  =  0  ;  i  <  3  ;  i  ++  )  { 
try  { 
rgb  [  i  ]  =  Integer  .  parseInt  (  splittedRGB  [  i  ]  )  ; 
}  catch  (  NumberFormatException   e  )  { 
rgb  [  i  ]  =  null  ; 
} 
if  (  rgb  [  i  ]  ==  null  ||  rgb  [  i  ]  <  0  )  { 
rgb  [  i  ]  =  0  ; 
}  else   if  (  rgb  [  i  ]  >  255  )  { 
rgb  [  i  ]  =  255  ; 
} 
} 
return   new   Color  (  rgb  [  0  ]  ,  rgb  [  1  ]  ,  rgb  [  2  ]  )  ; 
} 





public   static   void   desktopLaunchFile  (  final   File   file  )  { 
PlatformCaller  .  desktopLaunchFile  (  file  )  ; 
} 






public   static   boolean   checkName  (  String   name  )  { 
if  (  name  ==  null  )  return   false  ; 
if  (  name  .  equals  (  "Unknown"  )  )  return   false  ; 
if  (  name  .  length  (  )  >  50  ||  name  .  length  (  )  ==  0  )  return   false  ; 
if  (  !  name  .  matches  (  "[A-Za-z0-9_-]+"  )  )  return   false  ; 
if  (  Misc  .  myself  !=  null  )  if  (  name  .  equals  (  Misc  .  myself  .  name  )  )  return   false  ; 
return   true  ; 
} 

public   static   boolean   checkIfValidPath  (  String   out  )  { 
while  (  out  .  contains  (  "//"  )  )  { 
out  =  out  .  replace  (  "//"  ,  "/"  )  ; 
} 
return   new   File  (  out  )  .  exists  (  )  ; 
} 






public   static   String   ensureValidPath  (  String   out  ,  boolean   check  )  { 
if  (  Misc  .  isWindows  )  out  =  out  .  replaceAll  (  "\\\\"  ,  "/"  )  ; 
while  (  out  .  contains  (  "//"  )  )  { 
out  =  out  .  replace  (  "//"  ,  "/"  )  ; 
} 
if  (  !  new   File  (  out  )  .  exists  (  )  &&  check  )  { 
log  .  info  (  "Fix this problem!"  )  ; 
printStack  (  )  ; 
} 
return   out  ; 
} 

public   static   String  [  ]  appendString  (  String   toAppend  ,  String  ...  sarray  )  { 
if  (  sarray  ==  null  )  return   new   String  [  ]  {  toAppend  }  ; 
String  [  ]  ss  =  new   String  [  sarray  .  length  +  1  ]  ; 
int   y  =  0  ; 
for  (  String   s  :  sarray  )  ss  [  y  ++  ]  =  s  ; 
ss  [  y  ]  =  toAppend  ; 
return   ss  ; 
} 

public   static   String   assembleSystemInfo  (  )  { 
java  .  util  .  Properties   p  =  System  .  getProperties  (  )  ; 
String   out  =  ""  ; 
for  (  Object   o  :  p  .  keySet  (  )  )  { 
out  +=  o  .  toString  (  )  ; 
out  +=  ": "  +  p  .  getProperty  (  o  .  toString  (  )  )  +  Misc  .  NEWLINE  ; 
} 
out  +=  Misc  .  NEWLINE  ; 
out  +=  "TCPFile Variables:"  ; 
out  +=  "Version "  +  Misc  .  VERSIONNUMBER  +  NEWLINE  ; 
out  +=  "Unlimited encryption? "  +  Encryption  .  unlimited  +  NEWLINE  ; 
return   out  ; 
} 










public   static   void   update  (  )  { 
} 




public   static   boolean   askUser  (  String   question  ,  String   title  )  { 
return   JOptionPane  .  showConfirmDialog  (  null  ,  question  ,  title  ,  JOptionPane  .  YES_NO_OPTION  )  ==  JOptionPane  .  YES_OPTION  ; 
} 






public   static   void   dieAndTell  (  int   exit  ,  String   reason  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  reason  )  ; 
System  .  exit  (  exit  )  ; 
} 





public   static   void   echo  (  int   bla  )  { 
echo  (  bla  +  ""  )  ; 
} 





public   static   void   debugecho  (  Object   bla  )  { 
if  (  EntryPoint  .  debug  )  echo  (  bla  )  ; 
} 





public   static   synchronized   void   echo  (  Object   object  )  { 
String   timestamp  =  getTime  (  false  ,  true  ,  true  )  ; 
String   out  =  timestamp  +  ": "  ; 
if  (  object   instanceof   byte  [  ]  )  out  +=  "Length: "  +  (  (  byte  [  ]  )  object  )  .  length  +  " "  +  ByteArray  .  dumpBytes  (  (  byte  [  ]  )  object  )  ;  else   if  (  object  !=  null  )  out  +=  object  .  toString  (  )  ;  else   out  +=  "NULL"  ; 
if  (  out  .  endsWith  (  "\\n"  )  )  out  .  replace  (  "\\n"  ,  ""  )  ; 
if  (  out  .  endsWith  (  Misc  .  NEWLINE  )  )  out  =  out  .  substring  (  0  ,  out  .  length  (  )  -  Misc  .  NEWLINE  .  length  (  )  )  ; 
System  .  out  .  println  (  out  )  ; 
if  (  EntryPoint  .  GUIReady  )  { 
appendText  (  EntryPoint  .  gui  .  jTextAreaConsole  ,  out  ,  consoleLimit  ,  false  )  ; 
} 
} 

public   static   void   logError  (  Exception   e  )  { 
logError  (  e  ,  Level  .  WARNING  )  ; 
} 

public   static   void   logError  (  Exception   e  ,  Level   level  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 

public   static   StackTraceElement  [  ]  getStackTrace  (  )  { 
try  { 
int   i  =  1  /  0  ; 
i  ++  ; 
}  catch  (  RuntimeException   e  )  { 
return   e  .  getStackTrace  (  )  ; 
} 
return   null  ; 
} 

public   static   String   getCaller  (  )  { 
StackTraceElement  [  ]  ste  =  getStackTrace  (  )  ; 
return   ste  [  ste  .  length  -  3  ]  .  getMethodName  (  )  ; 
} 






public   static   void   printStack  (  )  { 
printStack  (  null  )  ; 
} 






public   static   void   printStack  (  String   message  )  { 
try  { 
int   i  =  1  /  0  ; 
i  ++  ; 
}  catch  (  RuntimeException   e  )  { 
String   out  =  message  !=  null  ?  message  :  "Exception used to print stack"  ; 
ArrayList  <  StackTraceElement  >  list  =  new   ArrayList  <  StackTraceElement  >  (  )  ; 
for  (  StackTraceElement   ste  :  e  .  getStackTrace  (  )  )  { 
if  (  !  ste  .  getMethodName  (  )  .  contains  (  "printStack"  )  )  list  .  add  (  ste  )  ; 
} 
e  .  setStackTrace  (  list  .  toArray  (  new   StackTraceElement  [  0  ]  )  )  ; 
log  .  warn  (  out  ,  e  )  ; 
} 
} 









public   static   String   getTime  (  long   offset  ,  boolean   date  ,  boolean   time  ,  boolean   ms  )  { 
String   format  =  ""  ; 
Date   d  =  new   Date  (  System  .  currentTimeMillis  (  )  +  offset  *  1000  )  ; 
if  (  date  )  { 
format  =  "yyyy-MM-dd"  ; 
if  (  time  )  { 
format  +=  " "  ; 
} 
} 
if  (  time  )  { 
format  +=  "HH:mm:ss"  ; 
} 
if  (  ms  )  { 
format  +=  ".SSS"  ; 
} 
String   timestamp  =  (  new   SimpleDateFormat  (  format  )  )  .  format  (  d  )  ; 
return   timestamp  ; 
} 




public   static   String   getTime  (  boolean   date  ,  boolean   time  ,  boolean   ms  )  { 
return   getTime  (  0  ,  date  ,  time  ,  ms  )  ; 
} 








public   static   void   appendText  (  final   JTextArea   textArea  ,  final   String   newText2  ,  final   int   lineLimit  ,  boolean   logIt  )  { 
Runnable   r  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
String   newText  =  newText2  ; 
if  (  newText  .  length  (  )  >  0  )  { 
String   newLine  =  newText  .  substring  (  newText  .  length  (  )  -  1  )  ; 
if  (  newLine  .  equals  (  "\n"  )  )  { 
newText  =  newText  .  substring  (  0  ,  newText  .  length  (  )  -  1  )  ; 
} 
newLine  =  newText  .  substring  (  0  ,  1  )  ; 
if  (  newLine  .  equals  (  "\n"  )  )  { 
textArea  .  append  (  newText  )  ; 
}  else  { 
textArea  .  append  (  "\n"  +  newText  )  ; 
} 
}  else  { 
return  ; 
} 
if  (  textArea  .  getLineCount  (  )  >  lineLimit  )  { 
try  { 
int   offset  =  textArea  .  getLineStartOffset  (  textArea  .  getLineCount  (  )  -  lineLimit  )  ; 
textArea  .  setText  (  textArea  .  getText  (  offset  ,  textArea  .  getDocument  (  )  .  getLength  (  )  -  offset  )  )  ; 
}  catch  (  BadLocationException   e  )  { 
log  .  warn  (  "Bad offset calculation while appending text."  )  ; 
} 
} 
if  (  scrollConsole  &&  textArea  ==  EntryPoint  .  gui  .  jTextAreaConsole  )  { 
try  { 
textArea  .  setCaretPosition  (  textArea  .  getDocument  (  )  .  getLength  (  )  )  ; 
}  catch  (  RuntimeException   e  )  { 
log  .  trace  (  "Failed to set the caret at the end of the textArea"  )  ; 
} 
} 
} 
}  ; 
SwingUtilities  .  invokeLater  (  r  )  ; 
} 







public   static   Class  <  ?  >  getNonPrimitive  (  Class  <  ?  >  c  )  { 
if  (  c  .  getName  (  )  .  equals  (  "int"  )  )  return   Integer  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "boolean"  )  )  return   Boolean  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "short"  )  )  return   Short  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "double"  )  )  return   Double  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "float"  )  )  return   Float  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "char"  )  )  return   Character  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "byte"  )  )  return   Byte  .  class  ; 
if  (  c  .  getName  (  )  .  equals  (  "long"  )  )  return   Long  .  class  ; 
log  .  warn  (  "{} is not a primitive type "  ,  c  .  getName  (  )  )  ; 
Misc  .  printStack  (  )  ; 
return   c  ; 
} 







public   static   long   max  (  long   i1  ,  long   i2  )  { 
if  (  i1  >  i2  )  return   i1  ; 
return   i2  ; 
} 







public   static   long   min  (  long   i1  ,  long   i2  )  { 
if  (  i1  <  i2  )  return   i1  ; 
return   i2  ; 
} 






public   static   String   prettyPrintSize  (  long   size  )  { 
return   prettyPrintSize  (  (  float  )  size  ,  0  ,  2  )  ; 
} 

static   String  [  ]  ext  =  new   String  [  ]  {  "B"  ,  "KB"  ,  "MB"  ,  "GB"  ,  "TB"  }  ; 








public   static   String   prettyPrintSize  (  float   size  ,  int   currentUnit  ,  int   commaDigits  )  { 
int   extCount  =  0  ; 
while  (  size  >=  1024  )  { 
size  /=  1024f  ; 
extCount  ++  ; 
} 
if  (  extCount  ==  0  )  return  (  (  int  )  size  )  +  " B"  ; 
if  (  extCount  +  currentUnit  >  ext  .  length  ||  extCount  +  currentUnit  <  0  )  return   size  +  ""  ; 
float   commaPower  =  (  float  )  java  .  lang  .  Math  .  pow  (  10  ,  commaDigits  )  ; 
return  (  Math  .  round  (  (  size  )  *  commaPower  )  /  commaPower  )  +  " "  +  ext  [  extCount  +  currentUnit  ]  ; 
} 








public   static   void   startTimerTasks  (  )  { 
class   AdvertiserContact   extends   UnbreakableTimerTask  { 

public   void   runCaught  (  )  { 
BuddyList  .  contactAdvertiser  (  )  ; 
} 
} 
class   GarbageCollectorForcer   extends   UnbreakableTimerTask  { 

public   void   runCaught  (  )  { 
forceGarbageCollection  (  )  ; 
} 
} 
class   QueuePoller   extends   UnbreakableTimerTask  { 

public   void   runCaught  (  )  { 
BuddyList  .  tryToSend  (  )  ; 
EntryPoint  .  gui  .  tableModelConnections  .  fireTableDataChanged  (  )  ; 
EntryPoint  .  gui  .  speed  .  update  (  )  ; 
} 
} 
class   Poller   extends   UnbreakableTimerTask  { 

public   void   runCaught  (  )  { 
BuddyList  .  pollOne  (  )  ; 
} 
} 
class   MemoryWatcher   extends   UnbreakableTimerTask  { 

public   void   runCaught  (  )  { 
long   minimum  =  20  *  1024  *  1024  ; 
long   b  =  Runtime  .  getRuntime  (  )  .  maxMemory  (  )  -  Runtime  .  getRuntime  (  )  .  totalMemory  (  )  +  Runtime  .  getRuntime  (  )  .  freeMemory  (  )  ; 
if  (  b  <  minimum  )  { 
log  .  debug  (  "Low Memory: "  +  Misc  .  prettyPrintSize  (  (  int  )  b  )  )  ; 
minimum  =  b  ; 
} 
final   int   percentage  =  (  int  )  (  100  *  b  /  Runtime  .  getRuntime  (  )  .  maxMemory  (  )  )  ; 
Runnable   r  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
EntryPoint  .  gui  .  updateProgressBar  (  percentage  )  ; 
EntryPoint  .  gui  .  updateActualSpeedDisplay  (  BandwidthAnalyzer  .  totalDownload  (  5  )  /  5  ,  BandwidthAnalyzer  .  totalUpload  (  5  )  /  5  ,  0  )  ; 
} 
}  ; 
SwingUtilities  .  invokeLater  (  r  )  ; 
} 
} 
timer  .  schedule  (  new   AdvertiserContact  (  )  ,  0  ,  SettingsManager  .  settingsManager  .  findSetting  (  "RefreshEvery"  )  .  getInteger  (  )  *  1000  )  ; 
timer  .  schedule  (  new   GarbageCollectorForcer  (  )  ,  0  ,  600  *  1000  )  ; 
timer  .  schedule  (  new   QueuePoller  (  )  ,  0  ,  1000  )  ; 
timer  .  schedule  (  new   Poller  (  )  ,  0  ,  5  *  1000  )  ; 
timer  .  schedule  (  new   MemoryWatcher  (  )  ,  5  *  1000  ,  500  )  ; 
} 





public   static   void   sleeps  (  long   time  )  { 
try  { 
Thread  .  sleep  (  time  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 





public   static   void   askForDirectory  (  )  { 
JFrame   frame  =  new   JFrame  (  "JFileChooser Popup"  )  ; 
frame  .  setDefaultCloseOperation  (  JFrame  .  EXIT_ON_CLOSE  )  ; 
Container   contentPane  =  frame  .  getContentPane  (  )  ; 
JFileChooser   fc  =  new   JFileChooser  (  )  ; 
fc  .  setControlButtonsAreShown  (  true  )  ; 
contentPane  .  add  (  fc  ,  BorderLayout  .  CENTER  )  ; 
fc  .  setFileSelectionMode  (  JFileChooser  .  DIRECTORIES_ONLY  )  ; 
fc  .  setApproveButtonText  (  "Select"  )  ; 
fc  .  setDialogTitle  (  "Select Default Save Directory"  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  frame  )  ; 
if  (  returnVal  ==  0  )  { 
File   f  =  fc  .  getSelectedFile  (  )  ; 
if  (  f  !=  null  )  if  (  f  .  exists  (  )  )  { 
String   path  =  f  .  getAbsolutePath  (  )  ; 
if  (  !  path  .  endsWith  (  Misc  .  FOLDERSEPARATOR  )  )  path  =  path  +  Misc  .  FOLDERSEPARATOR  ; 
SettingsManager  .  settingsManager  .  findSetting  (  "defaultSaveFolder"  )  .  setValue  (  path  )  ; 
} 
} 
} 






public   static   String   getNameOfThread  (  int   ThreadID  )  { 
Thread  [  ]  bla  =  new   Thread  [  100  ]  ; 
Thread  .  enumerate  (  bla  )  ; 
for  (  Thread   t  :  bla  )  if  (  t  !=  null  )  { 
int   blu  =  (  int  )  t  .  getId  (  )  ; 
if  (  blu  ==  ThreadID  )  return   t  .  getName  (  )  ; 
} 
return  ""  ; 
} 

public   static   String   firstToUpperCase  (  String   s  )  { 
return   s  .  substring  (  0  ,  1  )  .  toUpperCase  (  )  +  s  .  substring  (  1  )  ; 
} 

public   static   String   cutEnd  (  String   in  ,  int   count  )  { 
return   in  .  substring  (  0  ,  in  .  length  (  )  -  count  )  ; 
} 









public   static   void   saveObject  (  String   filename  ,  Object   output_veld  )  { 
try  { 
FileOutputStream   fos  =  new   FileOutputStream  (  filename  )  ; 
GZIPOutputStream   gzos  =  new   GZIPOutputStream  (  fos  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  gzos  )  ; 
out  .  writeObject  (  output_veld  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  e  +  output_veld  .  toString  (  )  )  ; 
} 
} 






public   static   Object   loadObject  (  String   filename  )  { 
try  { 
FileInputStream   fis  =  new   FileInputStream  (  filename  )  ; 
GZIPInputStream   gzis  =  new   GZIPInputStream  (  fis  )  ; 
ObjectInputStream   in  =  new   ObjectInputStream  (  gzis  )  ; 
Object   gelezen_veld  =  in  .  readObject  (  )  ; 
in  .  close  (  )  ; 
return   gelezen_veld  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
} 
return   null  ; 
} 







public   static   Object  [  ]  concat  (  Object  [  ]  A  ,  Object  [  ]  B  )  { 
Object  [  ]  C  =  new   Object  [  A  .  length  +  B  .  length  ]  ; 
System  .  arraycopy  (  A  ,  0  ,  C  ,  0  ,  A  .  length  )  ; 
System  .  arraycopy  (  B  ,  0  ,  C  ,  A  .  length  ,  B  .  length  )  ; 
return   C  ; 
} 





public   static   synchronized   String   getLocalIP  (  )  { 
if  (  ipcache  !=  null  )  return   ipcache  ; 
String   returning  =  ""  ; 
try  { 
Socket   connection  =  new   Socket  (  "www.sourceforge.net"  ,  80  )  ; 
returning  =  connection  .  getLocalAddress  (  )  .  getHostAddress  (  )  ; 
connection  .  close  (  )  ; 
}  catch  (  UnknownHostException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
log  .  warn  (  ""  ,  e  )  ; 
} 
ipcache  =  returning  ; 
if  (  ipcache  .  equals  (  ""  )  ||  ipcache  .  contains  (  "0.0.0.0"  )  )  { 
log  .  debug  (  "Could not find local ip address out the standard way"  )  ; 
try  { 
ipcache  =  InetAddress  .  getLocalHost  (  )  .  getHostAddress  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
} 
return   ipcache  ; 
} 

public   static   String   getFirstJSPIP  (  )  { 
if  (  SettingsManager  .  settingsManager  .  findSetting  (  "UseJSP"  )  .  getBoolean  (  )  )  { 
String   server  =  SettingsManager  .  settingsManager  .  findSetting  (  "JSPAddress"  )  .  getValue  (  )  .  toString  (  )  .  split  (  "\n"  )  [  0  ]  .  trim  (  )  ; 
return   server  ; 
} 
return   null  ; 
} 





public   static   void   checkPortStatus  (  )  { 
Runnable   r  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
while  (  !  mayPoll  )  sleeps  (  1000  )  ; 
if  (  getFirstJSPIP  (  )  !=  null  )  { 
EntryPoint  .  gui  .  updatePortStatus  (  2  ,  null  )  ; 
String   portCheckAnswer  =  downloadpage  (  getFirstJSPIP  (  )  +  "portopen.jsp?port="  +  Misc  .  myself  .  address  .  getPort  (  )  +  "&plaintext=34"  )  ; 
if  (  portCheckAnswer  !=  null  )  { 
if  (  portCheckAnswer  .  contains  (  "not"  )  )  { 
EntryPoint  .  gui  .  updatePortStatus  (  1  ,  null  )  ; 
return  ; 
}  else   if  (  portCheckAnswer  .  contains  (  "open"  )  )  { 
EntryPoint  .  gui  .  updatePortStatus  (  3  ,  null  )  ; 
return  ; 
}  else  { 
EntryPoint  .  gui  .  updatePortStatus  (  0  ,  "server did not response properly"  )  ; 
} 
}  else  { 
EntryPoint  .  gui  .  updatePortStatus  (  0  ,  "server did not response properly"  )  ; 
} 
} 
} 
}  ; 
runRunnableInSingletonThread  (  r  ,  "checkPortStatus"  )  ; 
} 





public   static   void   updateNumberOfUsers  (  )  { 
Runnable   r  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
Misc  .  numberOfUsers  =  0  ; 
String   server  =  getFirstJSPIP  (  )  ; 
if  (  server  ==  null  )  return  ; 
String   downloaded  =  downloadpage  (  server  +  "index.jsp?justnumbers=true"  )  .  trim  (  )  ; 
if  (  downloaded  ==  null  )  return  ; 
EntryPoint  .  gui  .  jLabelStatusBarServerUsers  .  setText  (  downloaded  )  ; 
Misc  .  numberOfUsers  =  Integer  .  parseInt  (  downloaded  )  ; 
}  catch  (  Exception   e  )  { 
log  .  warn  (  "Unable to update users"  ,  e  )  ; 
} 
} 
}  ; 
runRunnableInSingletonThread  (  r  ,  "updateNumberOfUsers"  )  ; 
} 






public   static   String   downloadpage  (  String   pagehtml  )  { 
int   tries  =  0  ; 
String   type  =  ""  ; 
while  (  tries  ++  <  2  )  { 
try  { 
String   thisLine  ; 
URL   u  =  new   URL  (  pagehtml  )  ; 
BufferedReader   d  =  new   BufferedReader  (  new   InputStreamReader  (  u  .  openStream  (  )  )  )  ; 
while  (  (  thisLine  =  d  .  readLine  (  )  )  !=  null  )  { 
type  +=  thisLine  +  Misc  .  NEWLINE  ; 
} 
d  .  close  (  )  ; 
tries  =  6  ; 
return   type  .  trim  (  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
System  .  err  .  println  (  "not a URL I understand:"  +  pagehtml  )  ; 
tries  =  4  ; 
}  catch  (  IOException   e  )  { 
if  (  (  e  )  !=  null  )  { 
if  (  e  .  toString  (  )  .  contains  (  "UnknownHostException"  )  )  tries  =  4  ;  else   if  (  e  .  toString  (  )  .  contains  (  "refused"  )  )  tries  =  6  ;  else   log  .  info  (  "Connection to "  +  pagehtml  ,  e  )  ; 
} 
} 
} 
type  =  type  .  trim  (  )  ; 
return   null  ; 
} 







public   static   void   downloadToHD  (  String   url  ,  String   fileName  )  { 
url  =  url  .  replace  (  " "  ,  "%20"  )  ; 
try  { 
URL   u  =  new   URL  (  url  )  ; 
HttpURLConnection   huc  =  (  HttpURLConnection  )  u  .  openConnection  (  )  ; 
huc  .  setReadTimeout  (  20000  )  ; 
huc  .  setRequestMethod  (  "GET"  )  ; 
huc  .  connect  (  )  ; 
InputStream   is  =  huc  .  getInputStream  (  )  ; 
int   code  =  huc  .  getResponseCode  (  )  ; 
if  (  code  ==  HttpURLConnection  .  HTTP_OK  )  { 
int   totBytes  ,  bytes  ,  sumBytes  =  0  ; 
totBytes  =  huc  .  getContentLength  (  )  ; 
if  (  totBytes  >  0  )  { 
int   size  =  1024  *  1024  ; 
byte  [  ]  buffer  =  new   byte  [  size  ]  ; 
fileName  =  fileName  .  replace  (  "_"  ,  " "  )  ; 
ByteArrayOutputStream   outputStream  =  new   ByteArrayOutputStream  (  )  ; 
while  (  true  )  { 
bytes  =  is  .  read  (  buffer  )  ; 
if  (  bytes  <=  0  )  break  ; 
sumBytes  +=  bytes  ; 
outputStream  .  write  (  buffer  ,  0  ,  bytes  )  ; 
} 
byte  [  ]  Image  =  outputStream  .  toByteArray  (  )  ; 
outputStream  .  close  (  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  fileName  )  ; 
out  .  write  (  Image  )  ; 
out  .  close  (  )  ; 
}  else   log  .  debug  (  "File "  +  url  +  " not found! Request redirected!"  )  ; 
} 
huc  .  disconnect  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  e  +  " "  +  url  )  ; 
if  (  e  .  toString  (  )  .  contains  (  "timed"  )  )  { 
downloadToHD  (  url  ,  fileName  )  ; 
} 
} 
} 

public   static   String   indentXMLString  (  String   s  )  { 
DOMParser   parser  =  new   DOMParser  (  )  ; 
InputSource   in  =  new   InputSource  (  new   StringReader  (  s  )  )  ; 
try  { 
parser  .  parse  (  in  )  ; 
Document   tree  =  parser  .  getDocument  (  )  ; 
TransformerFactory   tf  =  TransformerFactory  .  newInstance  (  )  ; 
tf  .  setAttribute  (  "indent-number"  ,  new   Integer  (  4  )  )  ; 
Transformer   t  =  tf  .  newTransformer  (  )  ; 
t  .  setOutputProperty  (  OutputKeys  .  INDENT  ,  "yes"  )  ; 
StringWriter   blaa  =  new   StringWriter  (  )  ; 
t  .  transform  (  new   DOMSource  (  tree  )  ,  new   StreamResult  (  blaa  )  )  ; 
return   blaa  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  warn  (  "Some error while indenting XML"  ,  e  )  ; 
} 
return   s  ; 
} 







public   static   int   generateInt  (  int   start  ,  int   end  )  { 
if  (  start  ==  end  )  return   start  ; 
int   rand  =  random  .  nextInt  (  java  .  lang  .  Math  .  abs  (  (  end  -  start  )  )  )  ; 
if  (  end  >=  start  )  rand  =  (  rand  )  +  start  ;  else   rand  =  rand  +  end  ; 
return   rand  ; 
} 

@  SuppressWarnings  (  value  =  {  "unchecked"  }  ) 
public   static   String   collectionToString  (  Collection   c  )  { 
return   Arrays  .  deepToString  (  c  .  toArray  (  )  )  ; 
} 
} 

