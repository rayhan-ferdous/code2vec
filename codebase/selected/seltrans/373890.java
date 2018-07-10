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
import   com  .  safi  .  asterisk  .  actionstep  .  VoicemailMain  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 



















public   class   VoicemailMainImpl   extends   AsteriskActionStepImpl   implements   VoicemailMain  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   mailbox  ; 









protected   static   final   boolean   SKIP_PASSWORD_CHECK_EDEFAULT  =  false  ; 









protected   boolean   skipPasswordCheck  =  SKIP_PASSWORD_CHECK_EDEFAULT  ; 









protected   static   final   boolean   USE_PREFIX_EDEFAULT  =  false  ; 









protected   boolean   usePrefix  =  USE_PREFIX_EDEFAULT  ; 









protected   static   final   int   RECORDING_GAIN_EDEFAULT  =  0  ; 









protected   int   recordingGain  =  RECORDING_GAIN_EDEFAULT  ; 









protected   DynamicValue   defaultFolder  ; 






protected   VoicemailMainImpl  (  )  { 
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
String   mb  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  resolveDynamicValue  (  mailbox  ,  context  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Getting VoicemailMain for mailbox: "  +  mb  )  ; 
if  (  StringUtils  .  isBlank  (  mb  )  )  { 
exception  =  new   ActionStepException  (  "mailbox is required for VoicemailMain"  )  ; 
}  else  { 
String   folder  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  resolveDynamicValue  (  this  .  defaultFolder  ,  context  )  )  ; 
if  (  StringUtils  .  isBlank  (  folder  )  )  { 
folder  =  null  ; 
} 
StringBuffer   appCmd  =  new   StringBuffer  (  )  ; 
appCmd  .  append  (  mb  )  ; 
if  (  skipPasswordCheck  )  appCmd  .  append  (  "|s"  )  ; 
if  (  usePrefix  )  appCmd  .  append  (  "|p"  )  ; 
if  (  recordingGain  !=  0  )  appCmd  .  append  (  "|g("  )  .  append  (  recordingGain  )  .  append  (  ")"  )  ; 
if  (  folder  !=  null  )  appCmd  .  append  (  "|a("  )  .  append  (  folder  )  .  append  (  ")"  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  { 
debug  (  "sending: VoiceMailMain "  +  appCmd  )  ; 
} 
int   result  =  channel  .  exec  (  "VoiceMailMain"  ,  appCmd  .  toString  (  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "VoiceMailMain returned "  +  translateAppReturnValue  (  result  )  +  " of int "  +  result  )  ; 
if  (  result  ==  -  2  )  { 
exception  =  new   ActionStepException  (  "Application VoiceMailMain not found"  )  ; 
}  else   if  (  result  ==  -  1  )  { 
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
return   ActionstepPackage  .  Literals  .  VOICEMAIL_MAIN  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getMailbox  (  )  { 
return   mailbox  ; 
} 






public   NotificationChain   basicSetMailbox  (  DynamicValue   newMailbox  ,  NotificationChain   msgs  )  { 
DynamicValue   oldMailbox  =  mailbox  ; 
mailbox  =  newMailbox  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  ,  oldMailbox  ,  newMailbox  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setMailbox  (  DynamicValue   newMailbox  )  { 
if  (  newMailbox  !=  mailbox  )  { 
NotificationChain   msgs  =  null  ; 
if  (  mailbox  !=  null  )  msgs  =  (  (  InternalEObject  )  mailbox  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  ,  null  ,  msgs  )  ; 
if  (  newMailbox  !=  null  )  msgs  =  (  (  InternalEObject  )  newMailbox  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  ,  null  ,  msgs  )  ; 
msgs  =  basicSetMailbox  (  newMailbox  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  ,  newMailbox  ,  newMailbox  )  )  ; 
} 






public   boolean   isSkipPasswordCheck  (  )  { 
return   skipPasswordCheck  ; 
} 






public   void   setSkipPasswordCheck  (  boolean   newSkipPasswordCheck  )  { 
boolean   oldSkipPasswordCheck  =  skipPasswordCheck  ; 
skipPasswordCheck  =  newSkipPasswordCheck  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK  ,  oldSkipPasswordCheck  ,  skipPasswordCheck  )  )  ; 
} 






public   boolean   isUsePrefix  (  )  { 
return   usePrefix  ; 
} 






public   void   setUsePrefix  (  boolean   newUsePrefix  )  { 
boolean   oldUsePrefix  =  usePrefix  ; 
usePrefix  =  newUsePrefix  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__USE_PREFIX  ,  oldUsePrefix  ,  usePrefix  )  )  ; 
} 






public   int   getRecordingGain  (  )  { 
return   recordingGain  ; 
} 






public   void   setRecordingGain  (  int   newRecordingGain  )  { 
int   oldRecordingGain  =  recordingGain  ; 
recordingGain  =  newRecordingGain  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__RECORDING_GAIN  ,  oldRecordingGain  ,  recordingGain  )  )  ; 
} 






public   DynamicValue   getDefaultFolder  (  )  { 
return   defaultFolder  ; 
} 






public   NotificationChain   basicSetDefaultFolder  (  DynamicValue   newDefaultFolder  ,  NotificationChain   msgs  )  { 
DynamicValue   oldDefaultFolder  =  defaultFolder  ; 
defaultFolder  =  newDefaultFolder  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  ,  oldDefaultFolder  ,  newDefaultFolder  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setDefaultFolder  (  DynamicValue   newDefaultFolder  )  { 
if  (  newDefaultFolder  !=  defaultFolder  )  { 
NotificationChain   msgs  =  null  ; 
if  (  defaultFolder  !=  null  )  msgs  =  (  (  InternalEObject  )  defaultFolder  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  ,  null  ,  msgs  )  ; 
if  (  newDefaultFolder  !=  null  )  msgs  =  (  (  InternalEObject  )  newDefaultFolder  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  ,  null  ,  msgs  )  ; 
msgs  =  basicSetDefaultFolder  (  newDefaultFolder  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  ,  newDefaultFolder  ,  newDefaultFolder  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  : 
return   basicSetMailbox  (  null  ,  msgs  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  : 
return   basicSetDefaultFolder  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  : 
return   getMailbox  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK  : 
return   isSkipPasswordCheck  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__USE_PREFIX  : 
return   isUsePrefix  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__RECORDING_GAIN  : 
return   getRecordingGain  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  : 
return   getDefaultFolder  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  : 
setMailbox  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK  : 
setSkipPasswordCheck  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__USE_PREFIX  : 
setUsePrefix  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__RECORDING_GAIN  : 
setRecordingGain  (  (  Integer  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  : 
setDefaultFolder  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  : 
setMailbox  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK  : 
setSkipPasswordCheck  (  SKIP_PASSWORD_CHECK_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__USE_PREFIX  : 
setUsePrefix  (  USE_PREFIX_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__RECORDING_GAIN  : 
setRecordingGain  (  RECORDING_GAIN_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  : 
setDefaultFolder  (  (  DynamicValue  )  null  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__MAILBOX  : 
return   mailbox  !=  null  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK  : 
return   skipPasswordCheck  !=  SKIP_PASSWORD_CHECK_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__USE_PREFIX  : 
return   usePrefix  !=  USE_PREFIX_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__RECORDING_GAIN  : 
return   recordingGain  !=  RECORDING_GAIN_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL_MAIN__DEFAULT_FOLDER  : 
return   defaultFolder  !=  null  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  : 
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
return   ActionstepPackage  .  VOICEMAIL_MAIN__CALL1  ; 
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
result  .  append  (  " (skipPasswordCheck: "  )  ; 
result  .  append  (  skipPasswordCheck  )  ; 
result  .  append  (  ", usePrefix: "  )  ; 
result  .  append  (  usePrefix  )  ; 
result  .  append  (  ", recordingGain: "  )  ; 
result  .  append  (  recordingGain  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

