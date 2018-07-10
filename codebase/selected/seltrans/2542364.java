package   org  .  opcda2out  ; 

import   org  .  opcda2out  .  output  .  OutputWriter  ; 
import   org  .  opcda2out  .  output  .  OutputWriterQueue  ; 
import   org  .  opcda2out  .  util  .  OPCArrayManager  ; 
import   org  .  opcda2out  .  opciteminfo  .  AbstractOPCItemInfo  ; 
import   org  .  communications  .  CommunicationManager  .  STATUS  ; 
import   org  .  communications  .  I18NException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Map  .  Entry  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  script  .  ScriptException  ; 
import   org  .  opccom  .  common  .  JIVariantX  ; 
import   org  .  opccom  .  da  .  OPCDAManager  ; 
import   org  .  jinterop  .  dcom  .  common  .  JIException  ; 
import   org  .  jinterop  .  dcom  .  core  .  JIVariant  ; 
import   org  .  opcda2out  .  async  .  AsyncOPCDataProvider  ; 
import   org  .  opcda2out  .  composite  .  CompositeItem  ; 
import   org  .  opcda2out  .  composite  .  CompositeItemElement  ; 
import   org  .  opcda2out  .  exception  .  FatalInitializationException  ; 
import   org  .  opcda2out  .  exception  .  InitializationException  ; 
import   org  .  opcda2out  .  opciteminfo  .  ArrayOPCItemInfo  ; 
import   org  .  opcda2out  .  opciteminfo  .  OPCItemInfo  ; 
import   org  .  opcda2out  .  output  .  OutputData  ; 
import   org  .  opcda2out  .  scripting  .  ScriptDataProvider  ; 
import   org  .  opcda2out  .  scripting  .  ScriptDataResult  ; 
import   org  .  opcda2out  .  sync  .  SyncOPCDataProvider  ; 
import   org  .  opccom  .  convertion  .  jivariant2java  .  JIVariant2JavaConverter  ; 
import   org  .  openscada  .  opc  .  dcom  .  common  .  Result  ; 
import   org  .  openscada  .  opc  .  dcom  .  da  .  OPCITEMRESULT  ; 
import   org  .  openscada  .  opc  .  dcom  .  da  .  impl  .  OPCItemProperties  ; 
import   org  .  openscada  .  opc  .  lib  .  common  .  NotConnectedException  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  AddFailedException  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  Group  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  Item  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  ItemState  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  Server  ; 
import   org  .  openscada  .  opc  .  lib  .  da  .  browser  .  Access  ; 







public   class   OPCBackupManager   implements   Runnable  { 




private   static   final   Logger   logger  =  Logger  .  getLogger  (  OPCBackupManager  .  class  .  getName  (  )  )  ; 

private   static   final   ResourceBundle   bundle  =  ResourceBundle  .  getBundle  (  "org/opcda2out/logging"  )  ; 

private   final   ThreadGroup   thGroup  =  new   ThreadGroup  (  "OPC Backup"  )  ; 

private   final   ScriptDataProvider   scriptDataProvider  =  new   ScriptDataProvider  (  )  ; 

private   final   Object   errorLock  =  new   Object  (  )  ; 

private   final   Object   sleepMutex  =  new   Object  (  )  ; 

private   final   Object   statusMutex  =  new   Object  (  )  ; 

private   final   Object   opcDataProviderLock  =  new   Object  (  )  ; 

private   final   OutputWriterQueue   writerQueue  ; 

private   final   ItemSelection   selection  ; 

private   final   Map  <  String  ,  AbstractOPCItemInfo  >  item2infoMap  ; 

private   boolean   initFailFast  =  true  ; 

private   DecideOnItemError   decisionMaker  =  null  ; 

private   boolean   readProperties  ; 

private   Throwable   error  ; 

private   boolean   asynchronous  ; 

private   OPCDataProvider   opcDataProvider  ; 




private   final   OPCDAManager   opc  ; 




private   int   samplingRate  ; 




private   final   OutputWriter   writer  ; 




private   final   List  <  OPC2OutProgressListener  >  progressListeners  =  new   ArrayList  <  OPC2OutProgressListener  >  (  4  )  ; 




private   final   List  <  OPCBkManagerStatusListener  >  statusListeners  =  new   ArrayList  <  OPCBkManagerStatusListener  >  (  4  )  ; 




private   volatile   MANAGERSTATUS   status  =  MANAGERSTATUS  .  STOPPED  ; 




public   enum   MANAGERSTATUS  { 




STOPPED  (  "OPC2Out_Stopped"  )  , 


INITIALIZING  (  "OPC2Out_Initializing"  )  , 


RUNNING  (  "OPC2Out_Started"  )  , 


STOPPING  (  "OPC2Out_Stopping"  )  ; 




private   final   String   description  ; 

private   static   final   ResourceBundle   bundle  =  ResourceBundle  .  getBundle  (  "org/opcda2out/logging"  )  ; 

MANAGERSTATUS  (  String   myInfo  )  { 
this  .  description  =  myInfo  ; 
} 






public   String   getDescription  (  )  { 
return   description  ; 
} 







public   String   getI18nDescription  (  )  { 
return   bundle  .  getString  (  description  )  ; 
} 
} 

; 












public   OPCBackupManager  (  OPCDAManager   opc  ,  int   samplingRate  ,  OutputWriter   writer  ,  Map  <  String  ,  String  [  ]  >  opcItemsId  )  { 
this  (  opc  ,  samplingRate  ,  writer  ,  opcItemsId  ,  null  ,  null  ,  false  ,  1  )  ; 
} 

















public   OPCBackupManager  (  OPCDAManager   opc  ,  int   samplingRate  ,  OutputWriter   writer  ,  Map  <  String  ,  String  [  ]  >  opcItemsId  ,  Map  <  String  ,  List  <  int  [  ]  >  >  arrayIndexes  ,  CompositeItem  [  ]  composite  ,  boolean   async  ,  int   bufferSize  )  { 
this  (  opc  ,  samplingRate  ,  writer  ,  new   ItemSelection  (  opcItemsId  ,  arrayIndexes  ,  composite  !=  null  ?  new   ArrayList  <  CompositeItem  >  (  Arrays  .  asList  (  composite  )  )  :  null  )  ,  async  ,  bufferSize  )  ; 
} 

public   OPCBackupManager  (  OPCDAManager   opc  ,  int   samplingRate  ,  OutputWriter   writer  ,  ItemSelection   selection  ,  boolean   async  ,  int   bufferSize  )  { 
if  (  samplingRate  <  100  )  { 
throw   new   IllegalArgumentException  (  "The sampling rate must be higher than 100 milliseconds"  )  ; 
}  else   if  (  opc  ==  null  )  { 
throw   new   NullPointerException  (  "The OPC manager cannot be null"  )  ; 
}  else   if  (  writer  ==  null  )  { 
throw   new   NullPointerException  (  "The Output writer cannot be null"  )  ; 
}  else   if  (  selection  ==  null  )  { 
throw   new   NullPointerException  (  "The selection cannot be null"  )  ; 
} 
this  .  samplingRate  =  samplingRate  ; 
this  .  opc  =  opc  ; 
this  .  writer  =  writer  ; 
this  .  selection  =  selection  ; 
this  .  asynchronous  =  async  ; 
this  .  writerQueue  =  new   OutputWriterQueue  (  this  ,  bufferSize  )  ; 
this  .  thGroup  .  setMaxPriority  (  Thread  .  MAX_PRIORITY  )  ; 
this  .  item2infoMap  =  new   HashMap  <  String  ,  AbstractOPCItemInfo  >  (  selection  .  opcItemsId  .  size  (  )  )  ; 
} 









public   synchronized   boolean   isReadProperties  (  )  { 
return   readProperties  ; 
} 









public   synchronized   void   setReadProperties  (  boolean   readProperties  )  { 
this  .  readProperties  =  readProperties  ; 
} 







public   DecideOnItemError   getDecisionMaker  (  )  { 
return   decisionMaker  ; 
} 







public   void   setDecisionMaker  (  DecideOnItemError   decisionMaker  )  { 
this  .  decisionMaker  =  decisionMaker  ; 
} 







public   OutputWriter   getOutputWriter  (  )  { 
return   writer  ; 
} 

public   OPCDataProvider   getOpcDataProvider  (  )  { 
synchronized  (  opcDataProviderLock  )  { 
return   opcDataProvider  ; 
} 
} 







public   synchronized   int   getSamplingRate  (  )  { 
return   samplingRate  ; 
} 

public   synchronized   void   setSamplingRate  (  int   samplingRate  )  throws   JIException  { 
if  (  samplingRate  <  100  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Sampling rate must be higher that 100 milliseconds."  )  ; 
samplingRate  =  100  ; 
} 
if  (  this  .  samplingRate  ==  samplingRate  )  { 
return  ; 
} 
this  .  samplingRate  =  samplingRate  ; 
final   Server   server  =  opc  .  getServer  (  )  ; 
if  (  server  !=  null  )  { 
server  .  setDefaultUpdateRate  (  samplingRate  )  ; 
synchronized  (  opcDataProviderLock  )  { 
if  (  opcDataProvider  !=  null  )  { 
final   Group   group  =  opcDataProvider  .  getGroup  (  )  ; 
if  (  group  !=  null  )  { 
int   actualSampling  =  group  .  setUpdateRate  (  samplingRate  )  ; 
if  (  actualSampling  !=  samplingRate  )  { 
logger  .  log  (  Level  .  WARNING  ,  "The OPC server update rate was changed to {0} milliseconds by the server. Logging/sampling will continue at the requested rate but the server will have a different update rate."  ,  actualSampling  )  ; 
} 
} 
} 
} 
} 
logger  .  log  (  Level  .  INFO  ,  "Sampling rate changed to {0} milliseconds."  ,  samplingRate  )  ; 
} 

public   boolean   isAsynchronous  (  )  { 
synchronized  (  opcDataProviderLock  )  { 
return   asynchronous  ; 
} 
} 

public   void   setAsynchronous  (  boolean   asynchronous  )  throws   Exception  { 
synchronized  (  opcDataProviderLock  )  { 
if  (  this  .  asynchronous  ==  asynchronous  )  { 
return  ; 
} 
this  .  asynchronous  =  asynchronous  ; 
if  (  opcDataProvider  !=  null  )  { 
opcDataProvider  .  stop  (  )  ; 
final   Group   group  =  opcDataProvider  .  getGroup  (  )  ; 
final   Item  [  ]  items  =  opcDataProvider  .  getItems  (  )  ; 
if  (  asynchronous  )  { 
opcDataProvider  =  new   AsyncOPCDataProvider  (  group  ,  items  )  ; 
}  else  { 
opcDataProvider  =  new   SyncOPCDataProvider  (  group  ,  items  )  ; 
} 
opcDataProvider  .  initialize  (  )  ; 
} 
} 
logger  .  log  (  Level  .  INFO  ,  "Data collection in {0} mode."  ,  asynchronous  ?  "asynchronous"  :  "synchronous"  )  ; 
} 






public   MANAGERSTATUS   getStatus  (  )  { 
synchronized  (  statusMutex  )  { 
return   status  ; 
} 
} 







public   Throwable   getError  (  )  { 
synchronized  (  errorLock  )  { 
return   error  ; 
} 
} 

private   void   setError  (  Throwable   error  )  { 
synchronized  (  errorLock  )  { 
this  .  error  =  error  ; 
} 
} 

@  Override 
public   void   run  (  )  { 
try  { 
setStatus  (  MANAGERSTATUS  .  INITIALIZING  )  ; 
initialize  (  )  ; 
System  .  gc  (  )  ; 
if  (  getStatus  (  )  !=  MANAGERSTATUS  .  INITIALIZING  )  { 
return  ; 
} 
setStatus  (  MANAGERSTATUS  .  RUNNING  )  ; 
Thread   th  =  new   Thread  (  thGroup  ,  writerQueue  ,  "Output writer queue"  )  ; 
th  .  setPriority  (  Thread  .  MAX_PRIORITY  )  ; 
th  .  setDaemon  (  true  )  ; 
th  .  start  (  )  ; 
doLoop  (  )  ; 
}  catch  (  Throwable   th  )  { 
setError  (  th  )  ; 
String   m  =  th  .  getLocalizedMessage  (  )  ; 
if  (  m  !=  null  )  { 
logger  .  log  (  Level  .  SEVERE  ,  m  ,  th  )  ; 
}  else  { 
logger  .  log  (  Level  .  SEVERE  ,  "An error occurred: {0}"  ,  th  .  getClass  (  )  .  getSimpleName  (  )  )  ; 
} 
}  finally  { 
setStatus  (  MANAGERSTATUS  .  STOPPING  )  ; 
writerQueue  .  stop  (  )  ; 
writer  .  stop  (  )  ; 
disposeOpcDataProvider  (  )  ; 
setStatus  (  MANAGERSTATUS  .  STOPPED  )  ; 
} 
} 

private   void   disposeOpcDataProvider  (  )  { 
synchronized  (  opcDataProviderLock  )  { 
if  (  opcDataProvider  !=  null  )  { 
opcDataProvider  .  stop  (  )  ; 
final   Group   group  =  opcDataProvider  .  getGroup  (  )  ; 
try  { 
group  .  clear  (  )  ; 
}  catch  (  JIException   ex  )  { 
logger  .  log  (  Level  .  INFO  ,  ex  .  getMessage  (  )  ,  ex  )  ; 
} 
try  { 
group  .  remove  (  )  ; 
}  catch  (  JIException   ex  )  { 
logger  .  log  (  Level  .  INFO  ,  "Failed to remove Group: "  +  ex  .  getMessage  (  )  ,  ex  )  ; 
} 
} 
} 
} 










private   synchronized   OPCItemInformation   prepareOPCItems  (  final   Group   group  )  throws   InitializationException  ,  Exception  { 
logger  .  info  (  "Initializing OPC Backup: Validating OPC items..."  )  ; 
if  (  !  group  .  isActive  (  )  )  { 
group  .  setActive  (  true  )  ; 
} 
final   Map  <  String  ,  Exception  >  blackList  =  new   HashMap  <  String  ,  Exception  >  (  )  ; 
final   Map  <  CompositeItem  ,  Exception  >  blackListComp  =  new   HashMap  <  CompositeItem  ,  Exception  >  (  )  ; 
final   List  <  AbstractOPCItemInfo  >  itemInfos  =  validateAdd  (  group  ,  selection  .  opcItemsId  ,  selection  .  arrayIndexes  ,  blackList  )  ; 
final   String  [  ]  emptyPropPath  =  new   String  [  0  ]  ; 
final   Map  <  String  ,  String  [  ]  >  itemIdsComp  =  new   HashMap  <  String  ,  String  [  ]  >  (  selection  .  composite  .  size  (  )  )  ; 
for  (  CompositeItem   comp  :  selection  .  composite  )  { 
Collection  <  String  >  ids  =  comp  .  getElementIds  (  )  ; 
for  (  String   id  :  ids  )  { 
if  (  !  selection  .  opcItemsId  .  containsKey  (  id  )  )  { 
itemIdsComp  .  put  (  id  ,  emptyPropPath  )  ; 
} 
} 
} 
final   List  <  AbstractOPCItemInfo  >  itemInfo4Comp  =  validateAdd  (  group  ,  itemIdsComp  ,  null  ,  blackList  )  ; 
final   List  <  CompositeItem  >  compList  =  prepareComposite  (  selection  .  composite  ,  blackListComp  )  ; 
if  (  !  blackList  .  isEmpty  (  )  ||  !  blackListComp  .  isEmpty  (  )  )  { 
if  (  decisionMaker  !=  null  &&  !  decisionMaker  .  continueOnOPCItemError  (  blackList  ,  blackListComp  )  )  { 
throw   new   FatalInitializationException  (  new   I18NException  (  bundle  ,  "OPC2Out_aborted"  )  )  ; 
} 
Set  <  String  >  used  =  new   HashSet  <  String  >  (  itemIdsComp  .  size  (  )  )  ; 
for  (  CompositeItem   comp  :  compList  )  { 
used  .  addAll  (  comp  .  getElementIds  (  )  )  ; 
} 
for  (  String   id  :  itemIdsComp  .  keySet  (  )  )  { 
if  (  !  used  .  contains  (  id  )  )  { 
AbstractOPCItemInfo   i  =  item2infoMap  .  remove  (  id  )  ; 
i  .  getOPCItem  (  )  .  getGroup  (  )  .  removeItem  (  id  )  ; 
} 
} 
selection  .  composite  .  clear  (  )  ; 
selection  .  composite  .  addAll  (  compList  )  ; 
} 
if  (  readProperties  )  { 
readItemProperties  (  itemInfos  )  ; 
} 
return   new   OPCItemInformation  (  itemInfos  ,  itemInfo4Comp  )  ; 
} 

private  <  T  >  void   handleError  (  Map  <  T  ,  Exception  >  blackList  ,  T   id  ,  I18NException   ex  )  throws   FatalInitializationException  { 
if  (  initFailFast  )  { 
throw   new   FatalInitializationException  (  ex  .  getLocalizedMessage  (  )  ,  ex  )  ; 
} 
logger  .  log  (  Level  .  WARNING  ,  ex  .  getMessage  (  )  ,  ex  .  getArguments  (  )  )  ; 
blackList  .  put  (  id  ,  ex  )  ; 
} 







public   synchronized   void   readItemProperties  (  )  throws   NotConnectedException  { 
final   List  <  AbstractOPCItemInfo  >  updtrs  =  new   ArrayList  <  AbstractOPCItemInfo  >  (  selection  .  opcItemsId  .  size  (  )  )  ; 
for  (  String   id  :  selection  .  opcItemsId  .  keySet  (  )  )  { 
final   AbstractOPCItemInfo   info  =  item2infoMap  .  get  (  id  )  ; 
if  (  info  !=  null  )  { 
updtrs  .  add  (  info  )  ; 
} 
} 
readItemProperties  (  updtrs  )  ; 
} 








private   void   readItemProperties  (  final   List  <  AbstractOPCItemInfo  >  updtrs  )  throws   NotConnectedException  { 
logger  .  info  (  "Retrieving OPC items' EU Unit and description properties..."  )  ; 
final   Server   server  =  opc  .  getServer  (  )  ; 
final   OPCItemProperties   service  =  server  .  getItemPropertiesService  (  )  ; 
for  (  AbstractOPCItemInfo   up  :  updtrs  )  { 
try  { 
up  .  loadItemProperties  (  service  )  ; 
}  catch  (  JIException   ex  )  { 
logger  .  log  (  Level  .  SEVERE  ,  ex  .  getMessage  (  )  ,  ex  )  ; 
} 
} 
} 

private   synchronized   void   initialize  (  )  throws   InitializationException  ,  Exception  { 
final   Server   server  =  opc  .  getServer  (  )  ; 
server  .  setDefaultUpdateRate  (  samplingRate  )  ; 
final   Group   group  =  server  .  addGroup  (  )  ; 
final   int   actualSampling  =  group  .  getUpdateRate  (  )  ; 
if  (  actualSampling  !=  samplingRate  )  { 
logger  .  log  (  Level  .  WARNING  ,  "The OPC server update rate was changed to {0} milliseconds by the server. Logging/sampling will continue at the requested rate but the server will have a different update rate."  ,  actualSampling  )  ; 
} 
final   OPCItemInformation   preparedInfo  =  prepareOPCItems  (  group  )  ; 
prepareOPCDataProvider  (  group  )  ; 
writer  .  initialize  (  preparedInfo  .  itemInfo  ,  selection  .  composite  .  toArray  (  new   CompositeItem  [  selection  .  composite  .  size  (  )  ]  )  ,  readProperties  )  ; 
} 

private   void   prepareOPCDataProvider  (  final   Group   group  )  throws   Exception  { 
synchronized  (  opcDataProviderLock  )  { 
if  (  opcDataProvider  !=  null  )  { 
opcDataProvider  .  stop  (  )  ; 
} 
final   List  <  AbstractOPCItemInfo  >  info  =  new   ArrayList  <  AbstractOPCItemInfo  >  (  item2infoMap  .  values  (  )  )  ; 
final   int   count  =  info  .  size  (  )  ; 
final   Item  [  ]  items  =  new   Item  [  count  ]  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
items  [  i  ]  =  info  .  get  (  i  )  .  getOPCItem  (  )  ; 
} 
if  (  asynchronous  )  { 
opcDataProvider  =  new   AsyncOPCDataProvider  (  group  ,  items  )  ; 
}  else  { 
opcDataProvider  =  new   SyncOPCDataProvider  (  group  ,  items  )  ; 
} 
opcDataProvider  .  initialize  (  )  ; 
} 
} 




private   void   doLoop  (  )  throws   Exception  { 
long   sleep  ; 
long   tfinal  =  System  .  currentTimeMillis  (  )  ; 
while  (  getStatus  (  )  ==  MANAGERSTATUS  .  RUNNING  &&  opc  .  getConnectionStatus  (  )  ==  STATUS  .  CONNECTED  &&  writer  .  isReady  (  )  )  { 
synchronized  (  sleepMutex  )  { 
final   int   localSamplingRate  =  getSamplingRate  (  )  ; 
tfinal  +=  localSamplingRate  ; 
final   Map  <  String  ,  ItemState  >  newItemData  ; 
synchronized  (  opcDataProviderLock  )  { 
newItemData  =  opcDataProvider  .  getOPCData  (  )  ; 
} 
final   long   cTime  =  System  .  currentTimeMillis  (  )  ; 
final   Map  <  String  ,  ScriptDataResult  >  newCompData  =  scriptDataProvider  .  getScriptData  (  selection  .  composite  ,  newItemData  )  ; 
final   Map  <  String  ,  OPCItemData  >  newCastedItemData  =  new   HashMap  <  String  ,  OPCItemData  >  (  newItemData  .  size  (  )  )  ; 
for  (  Entry  <  String  ,  AbstractOPCItemInfo  >  e  :  item2infoMap  .  entrySet  (  )  )  { 
final   String   id  =  e  .  getKey  (  )  ; 
final   ItemState   state  =  newItemData  .  get  (  e  .  getKey  (  )  )  ; 
if  (  state  !=  null  )  { 
final   JIVariant   variant  =  state  .  getValue  (  )  ; 
Object   casted  =  null  ; 
if  (  variant  !=  null  &&  variant  .  getType  (  )  !=  JIVariant  .  VT_EMPTY  &&  !  variant  .  isNull  (  )  )  { 
final   AbstractOPCItemInfo   info  =  e  .  getValue  (  )  ; 
JIVariant2JavaConverter   converter  =  info  .  getCasting  (  )  ; 
if  (  converter  .  getJIVariantType  (  )  !=  variant  .  getType  (  )  )  { 
info  .  updateCasting  (  variant  .  getType  (  )  )  ; 
} 
try  { 
casted  =  converter  .  convert  (  variant  )  ; 
}  catch  (  JIException   ex  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Error while converting value for ''{0}'' to a {1}: {2}"  ,  new   String  [  ]  {  id  ,  info  .  getOutputDataType  (  )  .  getSimpleName  (  )  ,  ex  .  getMessage  (  )  }  )  ; 
} 
} 
newCastedItemData  .  put  (  id  ,  new   OPCItemData  (  casted  ,  state  )  )  ; 
} 
} 
final   OutputData   oData  =  new   OutputData  (  cTime  ,  newCastedItemData  ,  newCompData  )  ; 
sleep  =  tfinal  -  cTime  ; 
if  (  sleep  <=  0  )  { 
tfinal  =  System  .  currentTimeMillis  (  )  ; 
if  (  getStatus  (  )  !=  MANAGERSTATUS  .  RUNNING  ||  opc  .  getConnectionStatus  (  )  !=  STATUS  .  CONNECTED  )  { 
break  ; 
} 
logger  .  warning  (  "Currently unable to read data from the OPC Server at the selected update rate!\nThe data is being read as fast as possible.\nPerhaps the update rate is too low."  )  ; 
} 
if  (  !  writerQueue  .  addData  (  oData  )  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Currently unable to write data to the output {0} at the selected update rate!\nThe data is being read as fast as possible.\nPerhaps the update rate is too low."  ,  writer  .  getOutputTypeName  (  )  )  ; 
if  (  !  writerQueue  .  addData  (  oData  ,  localSamplingRate  )  )  { 
writerQueue  .  forceblyAddData  (  oData  )  ; 
} 
tfinal  =  System  .  currentTimeMillis  (  )  ; 
sleep  =  0  ; 
} 
} 
if  (  sleep  >  0  )  { 
try  { 
synchronized  (  sleepMutex  )  { 
sleepMutex  .  wait  (  sleep  )  ; 
} 
}  catch  (  InterruptedException   ex  )  { 
if  (  getStatus  (  )  !=  MANAGERSTATUS  .  RUNNING  )  { 
return  ; 
}  else  { 
throw   ex  ; 
} 
} 
} 
} 
} 




public   void   notifyOutputWorkerError  (  )  { 
} 






public   void   addOPC2OutStatusListener  (  OPCBkManagerStatusListener   l  )  { 
synchronized  (  statusListeners  )  { 
if  (  !  statusListeners  .  contains  (  l  )  )  { 
statusListeners  .  add  (  l  )  ; 
} 
} 
} 






public   void   removeOPC2OutStatusListener  (  OPCBkManagerStatusListener   l  )  { 
synchronized  (  statusListeners  )  { 
statusListeners  .  remove  (  l  )  ; 
} 
} 







protected   void   fireOPC2OutStatusChanged  (  MANAGERSTATUS   oldStatus  ,  MANAGERSTATUS   newStatus  )  { 
OPCBkManagerStatusListener  [  ]  listeners  ; 
synchronized  (  statusListeners  )  { 
listeners  =  statusListeners  .  toArray  (  new   OPCBkManagerStatusListener  [  statusListeners  .  size  (  )  ]  )  ; 
} 
if  (  listeners  .  length  >  0  )  { 
OPCBkManagerStatusChangeEvent   e  =  new   OPCBkManagerStatusChangeEvent  (  this  ,  oldStatus  ,  newStatus  )  ; 
for  (  OPCBkManagerStatusListener   l  :  listeners  )  { 
l  .  managerStatusChanged  (  e  )  ; 
} 
} 
} 







public   void   addProgressListener  (  OPC2OutProgressListener   l  )  { 
synchronized  (  progressListeners  )  { 
if  (  !  progressListeners  .  contains  (  l  )  )  { 
progressListeners  .  add  (  l  )  ; 
} 
} 
} 






public   void   removeProgressListener  (  OPC2OutProgressListener   l  )  { 
synchronized  (  progressListeners  )  { 
progressListeners  .  remove  (  l  )  ; 
} 
} 







public   void   fireProgressEvent  (  OutputData   data  )  { 
OPC2OutProgressEvent   e  =  new   OPC2OutProgressEvent  (  this  ,  data  )  ; 
synchronized  (  progressListeners  )  { 
for  (  OPC2OutProgressListener   l  :  progressListeners  )  { 
l  .  dataWritten2Output  (  e  )  ; 
} 
} 
} 

private   List  <  AbstractOPCItemInfo  >  validateAdd  (  Group   group  ,  Map  <  String  ,  String  [  ]  >  itemIdsMap  ,  Map  <  String  ,  List  <  int  [  ]  >  >  arrayIndexes  ,  Map  <  String  ,  Exception  >  failed  )  throws   JIException  ,  FatalInitializationException  { 
final   List  <  AbstractOPCItemInfo  >  itemsInfo  =  new   ArrayList  <  AbstractOPCItemInfo  >  (  itemIdsMap  .  size  (  )  )  ; 
List  <  String  >  itemIds  =  new   ArrayList  <  String  >  (  itemIdsMap  .  keySet  (  )  )  ; 
final   String  [  ]  emptyPropPath  =  new   String  [  0  ]  ; 
for  (  int   x  =  0  ;  x  <  itemIds  .  size  (  )  ;  x  +=  20  )  { 
final   int   size  =  Math  .  min  (  itemIds  .  size  (  )  -  x  ,  20  )  ; 
final   String  [  ]  ids  =  new   String  [  size  ]  ; 
final   String  [  ]  [  ]  paths  =  new   String  [  size  ]  [  ]  ; 
for  (  int   k  =  0  ;  k  <  size  ;  k  ++  )  { 
ids  [  k  ]  =  itemIds  .  get  (  k  +  x  )  ; 
paths  [  k  ]  =  itemIdsMap  .  get  (  ids  [  k  ]  )  ; 
if  (  paths  [  k  ]  ==  null  )  { 
paths  [  k  ]  =  emptyPropPath  ; 
} 
} 
Map  <  String  ,  Result  <  OPCITEMRESULT  >  >  results  =  group  .  validateItems  (  ids  )  ; 
Map  <  String  ,  Short  >  dataTypes  =  new   HashMap  <  String  ,  Short  >  (  ids  .  length  )  ; 
for  (  int   k  =  0  ;  k  <  ids  .  length  ;  k  ++  )  { 
String   id  =  ids  [  k  ]  ; 
Result  <  OPCITEMRESULT  >  result  =  results  .  get  (  id  )  ; 
if  (  result  .  isFailed  (  )  )  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "Invalid_OPC_item_{0}!"  ,  id  )  ; 
handleError  (  failed  ,  id  ,  ex  )  ; 
continue  ; 
} 
short   dataType  =  result  .  getValue  (  )  .  getCanonicalDataType  (  )  ; 
dataTypes  .  put  (  id  ,  dataType  )  ; 
if  (  !  writer  .  isOPCTypeSupported  (  dataType  )  )  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "The_{0}_type_of_the_OPC_item_{1}_is_not_supported"  ,  JIVariantX  .  getJIVariantName  (  dataType  )  ,  id  )  ; 
handleError  (  failed  ,  id  ,  ex  )  ; 
continue  ; 
} 
if  (  !  OPCDAManager  .  isItemAccessRightAvailable  (  result  ,  Access  .  READ  )  )  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "OPC_item_{0}_is_not_readable!"  ,  id  )  ; 
handleError  (  failed  ,  id  ,  ex  )  ; 
continue  ; 
} 
} 
Map  <  String  ,  Item  >  items  =  null  ; 
try  { 
items  =  group  .  addItems  (  ids  )  ; 
}  catch  (  AddFailedException   ex  )  { 
for  (  String   id  :  ex  .  getItems  (  )  .  keySet  (  )  )  { 
final   I18NException   ex1  =  new   I18NException  (  ex  ,  bundle  ,  "Unable_to_use_OPC_item_{0}_{1}"  ,  id  ,  ex  .  getLocalizedMessage  (  )  )  ; 
handleError  (  failed  ,  id  ,  ex1  )  ; 
} 
if  (  items  .  isEmpty  (  )  )  { 
continue  ; 
} 
} 
for  (  int   k  =  0  ;  k  <  ids  .  length  ;  k  ++  )  { 
String   id  =  ids  [  k  ]  ; 
Item   item  =  items  .  get  (  id  )  ; 
AbstractOPCItemInfo   itemInfo  =  null  ; 
short   dataType  =  dataTypes  .  get  (  id  )  ; 
if  (  JIVariantX  .  isArray  (  dataType  )  )  { 
List  <  int  [  ]  >  indexes  =  null  ; 
if  (  arrayIndexes  !=  null  )  { 
indexes  =  arrayIndexes  .  get  (  id  )  ; 
} 
if  (  indexes  ==  null  ||  indexes  .  isEmpty  (  )  )  { 
indexes  =  OPCArrayManager  .  getAllArrayIndexes  (  item  )  ; 
} 
if  (  indexes  !=  null  &&  indexes  .  size  (  )  >  0  )  { 
itemInfo  =  new   ArrayOPCItemInfo  (  item  ,  dataType  ,  indexes  ,  paths  [  k  ]  ,  null  )  ; 
}  else  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "The_opc_array_item_{0}_must_have_at_least_one_selected_element"  ,  id  )  ; 
handleError  (  failed  ,  id  ,  ex  )  ; 
continue  ; 
} 
}  else  { 
itemInfo  =  new   OPCItemInfo  (  item  ,  dataType  ,  paths  [  k  ]  ,  null  )  ; 
} 
itemsInfo  .  add  (  itemInfo  )  ; 
item  .  setActive  (  true  )  ; 
item2infoMap  .  put  (  id  ,  itemInfo  )  ; 
} 
} 
return   itemsInfo  ; 
} 

private   List  <  CompositeItem  >  prepareComposite  (  final   Collection  <  CompositeItem  >  composite  ,  final   Map  <  CompositeItem  ,  Exception  >  failed  )  throws   FatalInitializationException  { 
final   List  <  CompositeItem  >  compList  =  new   ArrayList  <  CompositeItem  >  (  composite  .  size  (  )  )  ; 
outer  :  for  (  CompositeItem   comp  :  composite  )  { 
Collection  <  String  >  ids  =  comp  .  getElementIds  (  )  ; 
CompositeItemElement  [  ]  elements  =  new   CompositeItemElement  [  ids  .  size  (  )  ]  ; 
int   j  =  0  ; 
for  (  String   id  :  ids  )  { 
AbstractOPCItemInfo   itemInfo  =  item2infoMap  .  get  (  id  )  ; 
if  (  itemInfo  !=  null  )  { 
String   alias  =  comp  .  getAliasForOPCId  (  id  )  ; 
elements  [  j  ++  ]  =  new   CompositeItemElement  (  itemInfo  .  getOPCItem  (  )  .  getId  (  )  ,  itemInfo  .  getOpcDataType  (  )  ,  alias  )  ; 
}  else  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "At_least_one_of_element_used_in_the_script_of_composite_item_{0}_is_invalid"  ,  comp  .  getName  (  )  )  ; 
handleError  (  failed  ,  comp  ,  ex  )  ; 
continue   outer  ; 
} 
} 
comp  .  setElements  (  elements  )  ; 
if  (  comp  .  getCompiledScript  (  )  ==  null  )  { 
try  { 
comp  .  checkAndCompileScript  (  )  ; 
}  catch  (  ScriptException   se  )  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "An_error_occurred_while_compiling_the_script_for_composite_item_{0}_{1}"  ,  comp  .  getName  (  )  ,  se  .  getMessage  (  )  )  ; 
handleError  (  failed  ,  comp  ,  ex  )  ; 
continue  ; 
} 
} 
compList  .  add  (  comp  )  ; 
} 
return   compList  ; 
} 











public   synchronized   void   setItemSelection  (  final   ItemSelection   newSelection  )  throws   JIException  ,  FatalInitializationException  ,  Exception  { 
final   List  <  CompositeItem  >  addedComposite  =  new   ArrayList  <  CompositeItem  >  (  )  ; 
final   List  <  CompositeItem  >  removedComposite  =  new   ArrayList  <  CompositeItem  >  (  )  ; 
final   Map  <  String  ,  CompositeItem  >  remainingComposite  =  new   HashMap  <  String  ,  CompositeItem  >  (  selection  .  composite  .  size  (  )  )  ; 
for  (  CompositeItem   c  :  selection  .  composite  )  { 
remainingComposite  .  put  (  c  .  getName  (  )  ,  c  )  ; 
} 
for  (  CompositeItem   newComp  :  newSelection  .  composite  )  { 
final   String   name  =  newComp  .  getName  (  )  ; 
final   CompositeItem   oldComp  =  remainingComposite  .  remove  (  name  )  ; 
if  (  oldComp  ==  null  )  { 
addedComposite  .  add  (  newComp  )  ; 
}  else   if  (  !  oldComp  .  equals  (  newComp  )  )  { 
removedComposite  .  add  (  oldComp  )  ; 
addedComposite  .  add  (  newComp  )  ; 
} 
} 
removedComposite  .  addAll  (  remainingComposite  .  values  (  )  )  ; 
final   Map  <  String  ,  String  [  ]  >  addedOpcItemsId  =  new   HashMap  <  String  ,  String  [  ]  >  (  )  ; 
final   List  <  String  >  removedOpcItemsId  =  new   ArrayList  <  String  >  (  )  ; 
final   List  <  String  >  remainingOPCItems  =  new   ArrayList  <  String  >  (  selection  .  opcItemsId  .  keySet  (  )  )  ; 
for  (  Entry  <  String  ,  String  [  ]  >  e  :  newSelection  .  opcItemsId  .  entrySet  (  )  )  { 
final   String   id  =  e  .  getKey  (  )  ; 
final   int   pos  =  remainingOPCItems  .  indexOf  (  id  )  ; 
if  (  pos  <  0  )  { 
addedOpcItemsId  .  put  (  id  ,  e  .  getValue  (  )  )  ; 
}  else  { 
remainingOPCItems  .  remove  (  pos  )  ; 
} 
} 
removedOpcItemsId  .  addAll  (  remainingOPCItems  )  ; 
if  (  addedComposite  .  isEmpty  (  )  &&  removedComposite  .  isEmpty  (  )  &&  addedOpcItemsId  .  isEmpty  (  )  &&  removedOpcItemsId  .  isEmpty  (  )  )  { 
logger  .  info  (  "No selection change"  )  ; 
return  ; 
} 
synchronized  (  opcDataProviderLock  )  { 
final   Group   group  =  opcDataProvider  .  getGroup  (  )  ; 
final   Map  <  String  ,  Exception  >  blackList  =  new   HashMap  <  String  ,  Exception  >  (  )  ; 
final   Map  <  CompositeItem  ,  Exception  >  blackListComp  =  new   HashMap  <  CompositeItem  ,  Exception  >  (  )  ; 
synchronized  (  sleepMutex  )  { 
opcDataProvider  .  stop  (  )  ; 
writer  .  stop  (  )  ; 
for  (  CompositeItem   c2rm  :  removedComposite  )  { 
for  (  CompositeItem   c  :  selection  .  composite  )  { 
if  (  c  .  getName  (  )  .  equals  (  c2rm  .  getName  (  )  )  )  { 
selection  .  composite  .  remove  (  c  )  ; 
break  ; 
} 
} 
} 
final   String  [  ]  emptyPropPath  =  new   String  [  0  ]  ; 
final   Map  <  String  ,  String  [  ]  >  itemIdsComp  =  new   HashMap  <  String  ,  String  [  ]  >  (  addedComposite  .  size  (  )  )  ; 
for  (  CompositeItem   comp  :  addedComposite  )  { 
Collection  <  String  >  ids  =  comp  .  getElementIds  (  )  ; 
for  (  String   id  :  ids  )  { 
if  (  !  item2infoMap  .  containsKey  (  id  )  )  { 
itemIdsComp  .  put  (  id  ,  emptyPropPath  )  ; 
} 
} 
} 
final   List  <  AbstractOPCItemInfo  >  itemInfo4Comp  =  validateAdd  (  group  ,  itemIdsComp  ,  null  ,  blackList  )  ; 
final   List  <  CompositeItem  >  compList  =  prepareComposite  (  addedComposite  ,  blackListComp  )  ; 
selection  .  composite  .  addAll  (  compList  )  ; 
for  (  String   id  :  removedOpcItemsId  )  { 
selection  .  opcItemsId  .  remove  (  id  )  ; 
} 
selection  .  opcItemsId  .  putAll  (  addedOpcItemsId  )  ; 
Map  <  String  ,  String  [  ]  >  missingOPCItems  =  new   HashMap  <  String  ,  String  [  ]  >  (  )  ; 
for  (  Entry  <  String  ,  String  [  ]  >  e  :  addedOpcItemsId  .  entrySet  (  )  )  { 
final   String   id  =  e  .  getKey  (  )  ; 
AbstractOPCItemInfo   info  =  item2infoMap  .  get  (  id  )  ; 
if  (  info  !=  null  )  { 
if  (  info  .  isArray  (  )  )  { 
List  <  int  [  ]  >  indexes  =  newSelection  .  arrayIndexes  .  get  (  id  )  ; 
if  (  indexes  ==  null  ||  indexes  .  isEmpty  (  )  )  { 
List  <  int  [  ]  >  oldIndexes  =  selection  .  arrayIndexes  .  get  (  id  )  ; 
if  (  oldIndexes  ==  null  ||  oldIndexes  .  isEmpty  (  )  )  { 
indexes  =  (  (  ArrayOPCItemInfo  )  info  )  .  getElementIndexes  (  )  ; 
}  else  { 
indexes  =  OPCArrayManager  .  getAllArrayIndexes  (  info  .  getOPCItem  (  )  )  ; 
} 
} 
if  (  indexes  ==  null  ||  indexes  .  isEmpty  (  )  )  { 
I18NException   ex  =  new   I18NException  (  bundle  ,  "The_opc_array_item_{0}_must_have_at_least_one_selected_element"  ,  id  )  ; 
handleError  (  blackList  ,  id  ,  ex  )  ; 
continue  ; 
} 
(  (  ArrayOPCItemInfo  )  info  )  .  setIndexes  (  indexes  )  ; 
} 
}  else  { 
missingOPCItems  .  put  (  id  ,  e  .  getValue  (  )  )  ; 
} 
} 
List  <  AbstractOPCItemInfo  >  newItemsInfo  =  validateAdd  (  group  ,  missingOPCItems  ,  newSelection  .  arrayIndexes  ,  blackList  )  ; 
selection  .  arrayIndexes  .  clear  (  )  ; 
selection  .  arrayIndexes  .  putAll  (  newSelection  .  arrayIndexes  )  ; 
List  <  AbstractOPCItemInfo  >  opcItems  =  new   ArrayList  <  AbstractOPCItemInfo  >  (  selection  .  opcItemsId  .  size  (  )  )  ; 
for  (  String   id  :  selection  .  opcItemsId  .  keySet  (  )  )  { 
final   AbstractOPCItemInfo   i  =  item2infoMap  .  get  (  id  )  ; 
if  (  i  !=  null  )  { 
opcItems  .  add  (  i  )  ; 
} 
} 
final   Set  <  String  >  used  =  new   HashSet  <  String  >  (  itemIdsComp  .  size  (  )  )  ; 
for  (  AbstractOPCItemInfo   info  :  opcItems  )  { 
used  .  add  (  info  .  getOPCItem  (  )  .  getId  (  )  )  ; 
} 
for  (  CompositeItem   comp  :  selection  .  composite  )  { 
used  .  addAll  (  comp  .  getElementIds  (  )  )  ; 
} 
for  (  String   id  :  removedOpcItemsId  )  { 
if  (  !  used  .  contains  (  id  )  )  { 
AbstractOPCItemInfo   i  =  item2infoMap  .  remove  (  id  )  ; 
i  .  getOPCItem  (  )  .  getGroup  (  )  .  removeItem  (  id  )  ; 
} 
} 
for  (  CompositeItem   comp  :  removedComposite  )  { 
for  (  String   id  :  comp  .  getElementIds  (  )  )  { 
if  (  !  used  .  contains  (  id  )  )  { 
AbstractOPCItemInfo   i  =  item2infoMap  .  remove  (  id  )  ; 
if  (  i  !=  null  )  { 
i  .  getOPCItem  (  )  .  getGroup  (  )  .  removeItem  (  id  )  ; 
} 
} 
} 
} 
if  (  !  blackList  .  isEmpty  (  )  ||  !  blackListComp  .  isEmpty  (  )  )  { 
if  (  decisionMaker  !=  null  &&  !  decisionMaker  .  continueOnOPCItemError  (  blackList  ,  blackListComp  )  )  { 
throw   new   FatalInitializationException  (  new   I18NException  (  bundle  ,  "OPC2Out_aborted"  )  )  ; 
} 
for  (  String   id  :  blackList  .  keySet  (  )  )  { 
if  (  !  used  .  contains  (  id  )  )  { 
AbstractOPCItemInfo   i  =  item2infoMap  .  remove  (  id  )  ; 
i  .  getOPCItem  (  )  .  getGroup  (  )  .  removeItem  (  id  )  ; 
} 
} 
for  (  CompositeItem   comp  :  blackListComp  .  keySet  (  )  )  { 
for  (  String   id  :  comp  .  getElementIds  (  )  )  { 
if  (  !  used  .  contains  (  id  )  )  { 
AbstractOPCItemInfo   i  =  item2infoMap  .  remove  (  id  )  ; 
if  (  i  !=  null  )  { 
i  .  getOPCItem  (  )  .  getGroup  (  )  .  removeItem  (  id  )  ; 
} 
} 
} 
} 
} 
prepareOPCDataProvider  (  group  )  ; 
final   CompositeItem  [  ]  composite  =  selection  .  composite  .  toArray  (  new   CompositeItem  [  selection  .  composite  .  size  (  )  ]  )  ; 
writer  .  initialize  (  opcItems  .  toArray  (  new   AbstractOPCItemInfo  [  opcItems  .  size  (  )  ]  )  ,  composite  ,  readProperties  )  ; 
} 
} 
logger  .  log  (  Level  .  INFO  ,  "Selection changed: {0} OPC-DA items added, {1} OPC-DA items removed, {2} composite items added, {3} composite items removed"  ,  new   Integer  [  ]  {  addedOpcItemsId  .  size  (  )  ,  removedOpcItemsId  .  size  (  )  ,  addedComposite  .  size  (  )  ,  removedComposite  .  size  (  )  }  )  ; 
} 




public   synchronized   void   stop  (  )  { 
if  (  getStatus  (  )  !=  MANAGERSTATUS  .  STOPPED  )  { 
setStatus  (  MANAGERSTATUS  .  STOPPING  )  ; 
synchronized  (  sleepMutex  )  { 
sleepMutex  .  notifyAll  (  )  ; 
} 
} 
} 

private   void   setStatus  (  MANAGERSTATUS   newStatus  )  { 
boolean   changed  =  false  ; 
MANAGERSTATUS   oldStatus  ; 
synchronized  (  statusMutex  )  { 
oldStatus  =  status  ; 
if  (  newStatus  !=  status  )  { 
status  =  newStatus  ; 
logger  .  info  (  status  .  getI18nDescription  (  )  )  ; 
changed  =  true  ; 
} 
} 
if  (  changed  )  { 
fireOPC2OutStatusChanged  (  oldStatus  ,  newStatus  )  ; 
} 
} 

private   static   class   OPCItemInformation  { 




final   AbstractOPCItemInfo  [  ]  itemInfo  ; 





final   AbstractOPCItemInfo  [  ]  itemForComposite  ; 

public   OPCItemInformation  (  final   Collection  <  AbstractOPCItemInfo  >  itemInfo  ,  final   Collection  <  AbstractOPCItemInfo  >  itemInfo4Comp  )  { 
this  .  itemInfo  =  itemInfo  .  toArray  (  new   AbstractOPCItemInfo  [  itemInfo  .  size  (  )  ]  )  ; 
this  .  itemForComposite  =  itemInfo4Comp  .  toArray  (  new   AbstractOPCItemInfo  [  itemInfo4Comp  .  size  (  )  ]  )  ; 
} 
} 

; 
} 

