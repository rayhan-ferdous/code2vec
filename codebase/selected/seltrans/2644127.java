package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   java  .  util  .  logging  .  Level  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ExtensionSpy  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 























public   class   ExtensionSpyImpl   extends   AsteriskActionStepImpl   implements   ExtensionSpy  { 








protected   SafiCall   call1  ; 








protected   DynamicValue   extension  ; 








protected   DynamicValue   context  ; 









protected   static   final   boolean   SPY_BRIDGED_ONLY_EDEFAULT  =  false  ; 









protected   boolean   spyBridgedOnly  =  SPY_BRIDGED_ONLY_EDEFAULT  ; 








protected   static   final   String   GROUP_EDEFAULT  =  null  ; 








protected   String   group  =  GROUP_EDEFAULT  ; 









protected   static   final   boolean   BEEP_EDEFAULT  =  true  ; 









protected   boolean   beep  =  BEEP_EDEFAULT  ; 









protected   static   final   String   RECORD_FILENAME_PREFIX_EDEFAULT  =  null  ; 









protected   String   recordFilenamePrefix  =  RECORD_FILENAME_PREFIX_EDEFAULT  ; 








protected   static   final   int   VOLUME_EDEFAULT  =  0  ; 








protected   int   volume  =  VOLUME_EDEFAULT  ; 









protected   static   final   boolean   WHISPER_ENABLED_EDEFAULT  =  false  ; 









protected   boolean   whisperEnabled  =  WHISPER_ENABLED_EDEFAULT  ; 









protected   static   final   boolean   PRIVATE_WHISPER_ENABLED_EDEFAULT  =  false  ; 









protected   boolean   privateWhisperEnabled  =  PRIVATE_WHISPER_ENABLED_EDEFAULT  ; 








protected   DynamicValue   channelName  ; 





protected   ExtensionSpyImpl  (  )  { 
super  (  )  ; 
} 

@  Override 
public   void   beginProcessing  (  SafletContext   context  )  throws   ActionStepException  { 
super  .  beginProcessing  (  context  )  ; 
Exception   exception  =  null  ; 
if  (  call1  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No current call found"  )  )  ; 
return  ; 
}  else   if  (  !  (  call1   instanceof   Call  )  )  { 
handleException  (  context  ,  new   ActionStepException  (  "Call isn't isn't an Asterisk call: "  +  call1  .  getClass  (  )  .  getName  (  )  )  )  ; 
return  ; 
} 
if  (  (  (  Call  )  call1  )  .  getChannel  (  )  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No channel found in current context"  )  )  ; 
return  ; 
} 
AgiChannel   channel  =  (  (  Call  )  call1  )  .  getChannel  (  )  ; 
try  { 
String   chanStr  =  null  ; 
if  (  channelName  !=  null  )  { 
Object   dynValue  =  resolveDynamicValue  (  channelName  ,  context  )  ; 
chanStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
} 
if  (  StringUtils  .  isNotBlank  (  chanStr  )  )  { 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "ExtenSpy trying to spy on "  +  chanStr  )  ; 
} 
Object   dynValue  =  resolveDynamicValue  (  extension  ,  context  )  ; 
String   extStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
String   ctxStr  =  null  ; 
if  (  context  !=  null  )  { 
dynValue  =  resolveDynamicValue  (  this  .  context  ,  context  )  ; 
ctxStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
} 
if  (  StringUtils  .  isBlank  (  chanStr  )  &&  StringUtils  .  isBlank  (  extStr  )  )  { 
exception  =  new   ActionStepException  (  "Channel name or extension must be specified"  )  ; 
}  else  { 
StringBuffer   args  =  new   StringBuffer  (  )  ; 
if  (  StringUtils  .  isNotBlank  (  chanStr  )  )  args  .  append  (  chanStr  )  ;  else  { 
args  .  append  (  extStr  )  ; 
if  (  StringUtils  .  isNotBlank  (  ctxStr  )  )  args  .  append  (  '@'  )  .  append  (  ctxStr  )  ; 
} 
args  .  append  (  "|v("  )  .  append  (  volume  )  .  append  (  ')'  )  ; 
if  (  spyBridgedOnly  )  args  .  append  (  'b'  )  ; 
if  (  StringUtils  .  isNotBlank  (  group  )  )  args  .  append  (  "g("  )  .  append  (  group  )  .  append  (  ')'  )  ; 
if  (  !  beep  )  args  .  append  (  'q'  )  ; 
if  (  StringUtils  .  isNotBlank  (  recordFilenamePrefix  )  )  args  .  append  (  "r("  )  .  append  (  recordFilenamePrefix  )  .  append  (  ')'  )  ; 
if  (  whisperEnabled  )  args  .  append  (  'w'  )  ; 
if  (  privateWhisperEnabled  )  args  .  append  (  'W'  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Executing ExtenSpy app with args "  +  args  )  ; 
int   result  =  channel  .  exec  (  "ExtenSpy"  ,  args  .  toString  (  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "ExtenSpy return value was "  +  translateAppReturnValue  (  result  )  )  ; 
if  (  result  ==  -  1  )  { 
exception  =  new   ActionStepException  (  "Channel was hung up"  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
exception  =  e  ; 
} 
if  (  exception  !=  null  )  { 
handleException  (  context  ,  exception  )  ; 
return  ; 
} 
handleSuccess  (  context  )  ; 
} 





@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  EXTENSION_SPY  ; 
} 





public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  EXTENSION_SPY__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 
} 
return   call1  ; 
} 





public   SafiCall   basicGetCall1  (  )  { 
return   call1  ; 
} 





public   void   setCall1  (  SafiCall   newCall1  )  { 
SafiCall   oldCall1  =  call1  ; 
call1  =  newCall1  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 





public   DynamicValue   getExtension  (  )  { 
return   extension  ; 
} 





public   NotificationChain   basicSetExtension  (  DynamicValue   newExtension  ,  NotificationChain   msgs  )  { 
DynamicValue   oldExtension  =  extension  ; 
extension  =  newExtension  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__EXTENSION  ,  oldExtension  ,  newExtension  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 





public   void   setExtension  (  DynamicValue   newExtension  )  { 
if  (  newExtension  !=  extension  )  { 
NotificationChain   msgs  =  null  ; 
if  (  extension  !=  null  )  msgs  =  (  (  InternalEObject  )  extension  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__EXTENSION  ,  null  ,  msgs  )  ; 
if  (  newExtension  !=  null  )  msgs  =  (  (  InternalEObject  )  newExtension  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__EXTENSION  ,  null  ,  msgs  )  ; 
msgs  =  basicSetExtension  (  newExtension  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__EXTENSION  ,  newExtension  ,  newExtension  )  )  ; 
} 





public   DynamicValue   getContext  (  )  { 
return   context  ; 
} 





public   NotificationChain   basicSetContext  (  DynamicValue   newContext  ,  NotificationChain   msgs  )  { 
DynamicValue   oldContext  =  context  ; 
context  =  newContext  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__CONTEXT  ,  oldContext  ,  newContext  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 





public   void   setContext  (  DynamicValue   newContext  )  { 
if  (  newContext  !=  context  )  { 
NotificationChain   msgs  =  null  ; 
if  (  context  !=  null  )  msgs  =  (  (  InternalEObject  )  context  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__CONTEXT  ,  null  ,  msgs  )  ; 
if  (  newContext  !=  null  )  msgs  =  (  (  InternalEObject  )  newContext  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__CONTEXT  ,  null  ,  msgs  )  ; 
msgs  =  basicSetContext  (  newContext  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__CONTEXT  ,  newContext  ,  newContext  )  )  ; 
} 





public   boolean   isSpyBridgedOnly  (  )  { 
return   spyBridgedOnly  ; 
} 





public   void   setSpyBridgedOnly  (  boolean   newSpyBridgedOnly  )  { 
boolean   oldSpyBridgedOnly  =  spyBridgedOnly  ; 
spyBridgedOnly  =  newSpyBridgedOnly  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__SPY_BRIDGED_ONLY  ,  oldSpyBridgedOnly  ,  spyBridgedOnly  )  )  ; 
} 





public   String   getGroup  (  )  { 
return   group  ; 
} 





public   void   setGroup  (  String   newGroup  )  { 
String   oldGroup  =  group  ; 
group  =  newGroup  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__GROUP  ,  oldGroup  ,  group  )  )  ; 
} 





public   boolean   isBeep  (  )  { 
return   beep  ; 
} 





public   void   setBeep  (  boolean   newBeep  )  { 
boolean   oldBeep  =  beep  ; 
beep  =  newBeep  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__BEEP  ,  oldBeep  ,  beep  )  )  ; 
} 





public   String   getRecordFilenamePrefix  (  )  { 
return   recordFilenamePrefix  ; 
} 





public   void   setRecordFilenamePrefix  (  String   newRecordFilenamePrefix  )  { 
String   oldRecordFilenamePrefix  =  recordFilenamePrefix  ; 
recordFilenamePrefix  =  newRecordFilenamePrefix  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__RECORD_FILENAME_PREFIX  ,  oldRecordFilenamePrefix  ,  recordFilenamePrefix  )  )  ; 
} 





public   int   getVolume  (  )  { 
return   volume  ; 
} 





public   void   setVolume  (  int   newVolume  )  { 
int   oldVolume  =  volume  ; 
volume  =  newVolume  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__VOLUME  ,  oldVolume  ,  volume  )  )  ; 
} 





public   boolean   isWhisperEnabled  (  )  { 
return   whisperEnabled  ; 
} 





public   void   setWhisperEnabled  (  boolean   newWhisperEnabled  )  { 
boolean   oldWhisperEnabled  =  whisperEnabled  ; 
whisperEnabled  =  newWhisperEnabled  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__WHISPER_ENABLED  ,  oldWhisperEnabled  ,  whisperEnabled  )  )  ; 
} 





public   boolean   isPrivateWhisperEnabled  (  )  { 
return   privateWhisperEnabled  ; 
} 





public   void   setPrivateWhisperEnabled  (  boolean   newPrivateWhisperEnabled  )  { 
boolean   oldPrivateWhisperEnabled  =  privateWhisperEnabled  ; 
privateWhisperEnabled  =  newPrivateWhisperEnabled  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__PRIVATE_WHISPER_ENABLED  ,  oldPrivateWhisperEnabled  ,  privateWhisperEnabled  )  )  ; 
} 





public   DynamicValue   getChannelName  (  )  { 
return   channelName  ; 
} 





public   NotificationChain   basicSetChannelName  (  DynamicValue   newChannelName  ,  NotificationChain   msgs  )  { 
DynamicValue   oldChannelName  =  channelName  ; 
channelName  =  newChannelName  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  ,  oldChannelName  ,  newChannelName  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 





public   void   setChannelName  (  DynamicValue   newChannelName  )  { 
if  (  newChannelName  !=  channelName  )  { 
NotificationChain   msgs  =  null  ; 
if  (  channelName  !=  null  )  msgs  =  (  (  InternalEObject  )  channelName  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  ,  null  ,  msgs  )  ; 
if  (  newChannelName  !=  null  )  msgs  =  (  (  InternalEObject  )  newChannelName  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  ,  null  ,  msgs  )  ; 
msgs  =  basicSetChannelName  (  newChannelName  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  ,  newChannelName  ,  newChannelName  )  )  ; 
} 





@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__EXTENSION  : 
return   basicSetExtension  (  null  ,  msgs  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CONTEXT  : 
return   basicSetContext  (  null  ,  msgs  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  : 
return   basicSetChannelName  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 





@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__EXTENSION  : 
return   getExtension  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CONTEXT  : 
return   getContext  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__SPY_BRIDGED_ONLY  : 
return   isSpyBridgedOnly  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__GROUP  : 
return   getGroup  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__BEEP  : 
return   isBeep  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__RECORD_FILENAME_PREFIX  : 
return   getRecordFilenamePrefix  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__VOLUME  : 
return   getVolume  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__WHISPER_ENABLED  : 
return   isWhisperEnabled  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__PRIVATE_WHISPER_ENABLED  : 
return   isPrivateWhisperEnabled  (  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  : 
return   getChannelName  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 





@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__EXTENSION  : 
setExtension  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CONTEXT  : 
setContext  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__SPY_BRIDGED_ONLY  : 
setSpyBridgedOnly  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__GROUP  : 
setGroup  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__BEEP  : 
setBeep  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__RECORD_FILENAME_PREFIX  : 
setRecordFilenamePrefix  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__VOLUME  : 
setVolume  (  (  Integer  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__WHISPER_ENABLED  : 
setWhisperEnabled  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__PRIVATE_WHISPER_ENABLED  : 
setPrivateWhisperEnabled  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  : 
setChannelName  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 





@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__EXTENSION  : 
setExtension  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CONTEXT  : 
setContext  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__SPY_BRIDGED_ONLY  : 
setSpyBridgedOnly  (  SPY_BRIDGED_ONLY_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__GROUP  : 
setGroup  (  GROUP_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__BEEP  : 
setBeep  (  BEEP_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__RECORD_FILENAME_PREFIX  : 
setRecordFilenamePrefix  (  RECORD_FILENAME_PREFIX_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__VOLUME  : 
setVolume  (  VOLUME_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__WHISPER_ENABLED  : 
setWhisperEnabled  (  WHISPER_ENABLED_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__PRIVATE_WHISPER_ENABLED  : 
setPrivateWhisperEnabled  (  PRIVATE_WHISPER_ENABLED_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  : 
setChannelName  (  (  DynamicValue  )  null  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 





@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  EXTENSION_SPY__EXTENSION  : 
return   extension  !=  null  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CONTEXT  : 
return   context  !=  null  ; 
case   ActionstepPackage  .  EXTENSION_SPY__SPY_BRIDGED_ONLY  : 
return   spyBridgedOnly  !=  SPY_BRIDGED_ONLY_EDEFAULT  ; 
case   ActionstepPackage  .  EXTENSION_SPY__GROUP  : 
return   GROUP_EDEFAULT  ==  null  ?  group  !=  null  :  !  GROUP_EDEFAULT  .  equals  (  group  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__BEEP  : 
return   beep  !=  BEEP_EDEFAULT  ; 
case   ActionstepPackage  .  EXTENSION_SPY__RECORD_FILENAME_PREFIX  : 
return   RECORD_FILENAME_PREFIX_EDEFAULT  ==  null  ?  recordFilenamePrefix  !=  null  :  !  RECORD_FILENAME_PREFIX_EDEFAULT  .  equals  (  recordFilenamePrefix  )  ; 
case   ActionstepPackage  .  EXTENSION_SPY__VOLUME  : 
return   volume  !=  VOLUME_EDEFAULT  ; 
case   ActionstepPackage  .  EXTENSION_SPY__WHISPER_ENABLED  : 
return   whisperEnabled  !=  WHISPER_ENABLED_EDEFAULT  ; 
case   ActionstepPackage  .  EXTENSION_SPY__PRIVATE_WHISPER_ENABLED  : 
return   privateWhisperEnabled  !=  PRIVATE_WHISPER_ENABLED_EDEFAULT  ; 
case   ActionstepPackage  .  EXTENSION_SPY__CHANNEL_NAME  : 
return   channelName  !=  null  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 





@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  EXTENSION_SPY__CALL1  : 
return   CallPackage  .  CALL_CONSUMER1__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eBaseStructuralFeatureID  (  derivedFeatureID  ,  baseClass  )  ; 
} 





@  Override 
public   int   eDerivedStructuralFeatureID  (  int   baseFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  baseFeatureID  )  { 
case   CallPackage  .  CALL_CONSUMER1__CALL1  : 
return   ActionstepPackage  .  EXTENSION_SPY__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eDerivedStructuralFeatureID  (  baseFeatureID  ,  baseClass  )  ; 
} 





@  Override 
public   String   toString  (  )  { 
if  (  eIsProxy  (  )  )  return   super  .  toString  (  )  ; 
StringBuffer   result  =  new   StringBuffer  (  super  .  toString  (  )  )  ; 
result  .  append  (  " (spyBridgedOnly: "  )  ; 
result  .  append  (  spyBridgedOnly  )  ; 
result  .  append  (  ", group: "  )  ; 
result  .  append  (  group  )  ; 
result  .  append  (  ", beep: "  )  ; 
result  .  append  (  beep  )  ; 
result  .  append  (  ", recordFilenamePrefix: "  )  ; 
result  .  append  (  recordFilenamePrefix  )  ; 
result  .  append  (  ", volume: "  )  ; 
result  .  append  (  volume  )  ; 
result  .  append  (  ", whisperEnabled: "  )  ; 
result  .  append  (  whisperEnabled  )  ; 
result  .  append  (  ", privateWhisperEnabled: "  )  ; 
result  .  append  (  privateWhisperEnabled  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

