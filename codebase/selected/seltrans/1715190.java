package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  StreamAudioExtended  ; 
import   com  .  safi  .  asterisk  .  saflet  .  AsteriskSafletContext  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValueType  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 




















public   class   StreamAudioExtendedImpl   extends   AsteriskActionStepImpl   implements   StreamAudioExtended  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   filename  ; 









protected   static   final   String   ESCAPE_DIGITS_EDEFAULT  =  "#"  ; 









protected   String   escapeDigits  =  ESCAPE_DIGITS_EDEFAULT  ; 









protected   static   final   int   OFFSET_EDEFAULT  =  1000  ; 









protected   int   offset  =  OFFSET_EDEFAULT  ; 









protected   static   final   String   FORWARD_DIGIT_EDEFAULT  =  "3"  ; 









protected   String   forwardDigit  =  FORWARD_DIGIT_EDEFAULT  ; 









protected   static   final   String   REWIND_DIGIT_EDEFAULT  =  "1"  ; 









protected   String   rewindDigit  =  REWIND_DIGIT_EDEFAULT  ; 









protected   static   final   String   PAUSE_DIGIT_EDEFAULT  =  "2"  ; 









protected   String   pauseDigit  =  PAUSE_DIGIT_EDEFAULT  ; 






protected   StreamAudioExtendedImpl  (  )  { 
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
Object   dynValue  =  resolveDynamicValue  (  filename  ,  context  )  ; 
String   filenameStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
if  (  StringUtils  .  isBlank  (  filenameStr  )  )  exception  =  new   ActionStepException  (  "Filename was null"  )  ;  else  { 
if  (  filename  .  getType  (  )  ==  DynamicValueType  .  CUSTOM  )  filenameStr  =  getSaflet  (  )  .  getPromptPathByName  (  filenameStr  )  ; 
char   c  =  channel  .  controlStreamFile  (  filenameStr  ,  escapeDigits  ,  offset  ,  forwardDigit  ,  rewindDigit  ,  pauseDigit  )  ; 
if  (  c  !=  0  )  (  (  AsteriskSafletContext  )  context  )  .  appendBufferedDigits  (  String  .  valueOf  (  c  )  )  ; 
String   value  =  channel  .  getVariable  (  "CPLAYBACKSTATUS"  )  ; 
if  (  StringUtils  .  equalsIgnoreCase  (  "ERROR"  ,  value  )  )  { 
exception  =  new   ActionStepException  (  "StreamAudioExtended returned "  +  value  )  ; 
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
return   ActionstepPackage  .  Literals  .  STREAM_AUDIO_EXTENDED  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getFilename  (  )  { 
return   filename  ; 
} 






public   NotificationChain   basicSetFilename  (  DynamicValue   newFilename  ,  NotificationChain   msgs  )  { 
DynamicValue   oldFilename  =  filename  ; 
filename  =  newFilename  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  ,  oldFilename  ,  newFilename  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setFilename  (  DynamicValue   newFilename  )  { 
if  (  newFilename  !=  filename  )  { 
NotificationChain   msgs  =  null  ; 
if  (  filename  !=  null  )  msgs  =  (  (  InternalEObject  )  filename  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  ,  null  ,  msgs  )  ; 
if  (  newFilename  !=  null  )  msgs  =  (  (  InternalEObject  )  newFilename  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  ,  null  ,  msgs  )  ; 
msgs  =  basicSetFilename  (  newFilename  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  ,  newFilename  ,  newFilename  )  )  ; 
} 






public   String   getEscapeDigits  (  )  { 
return   escapeDigits  ; 
} 






public   void   setEscapeDigits  (  String   newEscapeDigits  )  { 
String   oldEscapeDigits  =  escapeDigits  ; 
escapeDigits  =  newEscapeDigits  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__ESCAPE_DIGITS  ,  oldEscapeDigits  ,  escapeDigits  )  )  ; 
} 






public   int   getOffset  (  )  { 
return   offset  ; 
} 






public   void   setOffset  (  int   newOffset  )  { 
int   oldOffset  =  offset  ; 
offset  =  newOffset  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__OFFSET  ,  oldOffset  ,  offset  )  )  ; 
} 






public   String   getForwardDigit  (  )  { 
return   forwardDigit  ; 
} 






public   void   setForwardDigit  (  String   newForwardDigit  )  { 
String   oldForwardDigit  =  forwardDigit  ; 
forwardDigit  =  newForwardDigit  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FORWARD_DIGIT  ,  oldForwardDigit  ,  forwardDigit  )  )  ; 
} 






public   String   getRewindDigit  (  )  { 
return   rewindDigit  ; 
} 






public   void   setRewindDigit  (  String   newRewindDigit  )  { 
String   oldRewindDigit  =  rewindDigit  ; 
rewindDigit  =  newRewindDigit  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__REWIND_DIGIT  ,  oldRewindDigit  ,  rewindDigit  )  )  ; 
} 






public   String   getPauseDigit  (  )  { 
return   pauseDigit  ; 
} 






public   void   setPauseDigit  (  String   newPauseDigit  )  { 
String   oldPauseDigit  =  pauseDigit  ; 
pauseDigit  =  newPauseDigit  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  STREAM_AUDIO_EXTENDED__PAUSE_DIGIT  ,  oldPauseDigit  ,  pauseDigit  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  : 
return   basicSetFilename  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  : 
return   getFilename  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__ESCAPE_DIGITS  : 
return   getEscapeDigits  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__OFFSET  : 
return   getOffset  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FORWARD_DIGIT  : 
return   getForwardDigit  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__REWIND_DIGIT  : 
return   getRewindDigit  (  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__PAUSE_DIGIT  : 
return   getPauseDigit  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  : 
setFilename  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__ESCAPE_DIGITS  : 
setEscapeDigits  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__OFFSET  : 
setOffset  (  (  Integer  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FORWARD_DIGIT  : 
setForwardDigit  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__REWIND_DIGIT  : 
setRewindDigit  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__PAUSE_DIGIT  : 
setPauseDigit  (  (  String  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  : 
setFilename  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__ESCAPE_DIGITS  : 
setEscapeDigits  (  ESCAPE_DIGITS_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__OFFSET  : 
setOffset  (  OFFSET_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FORWARD_DIGIT  : 
setForwardDigit  (  FORWARD_DIGIT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__REWIND_DIGIT  : 
setRewindDigit  (  REWIND_DIGIT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__PAUSE_DIGIT  : 
setPauseDigit  (  PAUSE_DIGIT_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FILENAME  : 
return   filename  !=  null  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__ESCAPE_DIGITS  : 
return   ESCAPE_DIGITS_EDEFAULT  ==  null  ?  escapeDigits  !=  null  :  !  ESCAPE_DIGITS_EDEFAULT  .  equals  (  escapeDigits  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__OFFSET  : 
return   offset  !=  OFFSET_EDEFAULT  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__FORWARD_DIGIT  : 
return   FORWARD_DIGIT_EDEFAULT  ==  null  ?  forwardDigit  !=  null  :  !  FORWARD_DIGIT_EDEFAULT  .  equals  (  forwardDigit  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__REWIND_DIGIT  : 
return   REWIND_DIGIT_EDEFAULT  ==  null  ?  rewindDigit  !=  null  :  !  REWIND_DIGIT_EDEFAULT  .  equals  (  rewindDigit  )  ; 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__PAUSE_DIGIT  : 
return   PAUSE_DIGIT_EDEFAULT  ==  null  ?  pauseDigit  !=  null  :  !  PAUSE_DIGIT_EDEFAULT  .  equals  (  pauseDigit  )  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  : 
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
return   ActionstepPackage  .  STREAM_AUDIO_EXTENDED__CALL1  ; 
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
result  .  append  (  " (escapeDigits: "  )  ; 
result  .  append  (  escapeDigits  )  ; 
result  .  append  (  ", offset: "  )  ; 
result  .  append  (  offset  )  ; 
result  .  append  (  ", forwardDigit: "  )  ; 
result  .  append  (  forwardDigit  )  ; 
result  .  append  (  ", rewindDigit: "  )  ; 
result  .  append  (  rewindDigit  )  ; 
result  .  append  (  ", pauseDigit: "  )  ; 
result  .  append  (  pauseDigit  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

