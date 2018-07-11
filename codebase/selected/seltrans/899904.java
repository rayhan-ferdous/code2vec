package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   java  .  util  .  Date  ; 
import   java  .  util  .  logging  .  Level  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  SayDateTime  ; 
import   com  .  safi  .  asterisk  .  saflet  .  AsteriskSafletContext  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 


















public   class   SayDateTimeImpl   extends   AsteriskActionStepImpl   implements   SayDateTime  { 









protected   SafiCall   call1  ; 









protected   static   final   String   ESCAPE_DIGITS_EDEFAULT  =  "#"  ; 









protected   String   escapeDigits  =  ESCAPE_DIGITS_EDEFAULT  ; 









protected   static   final   String   FORMAT_EDEFAULT  =  "ABdY \\\'digits/at\\\' IMp"  ; 









protected   String   format  =  FORMAT_EDEFAULT  ; 









protected   DynamicValue   dateTime  ; 









protected   static   final   String   TIMEZONE_EDEFAULT  =  null  ; 









protected   String   timezone  =  TIMEZONE_EDEFAULT  ; 






protected   SayDateTimeImpl  (  )  { 
super  (  )  ; 
} 

@  Override 
public   void   beginProcessing  (  SafletContext   context  )  throws   ActionStepException  { 
super  .  beginProcessing  (  context  )  ; 
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
Date   date  =  (  Date  )  VariableTranslator  .  translateValue  (  VariableType  .  DATETIME  ,  resolveDynamicValue  (  dateTime  ,  context  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "The date is "  +  date  )  ; 
char   d  =  channel  .  sayDateTime  (  date  .  getTime  (  )  /  1000  ,  escapeDigits  ,  format  ,  timezone  )  ; 
if  (  d  !=  0  )  { 
String   digitPressed  =  String  .  valueOf  (  d  )  ; 
(  (  AsteriskSafletContext  )  context  )  .  appendBufferedDigits  (  digitPressed  )  ; 
} 
}  catch  (  Exception   e  )  { 
handleException  (  context  ,  e  )  ; 
return  ; 
} 
handleSuccess  (  context  )  ; 
} 






@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  SAY_DATE_TIME  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  SAY_DATE_TIME__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   String   getEscapeDigits  (  )  { 
return   escapeDigits  ; 
} 






public   void   setEscapeDigits  (  String   newEscapeDigits  )  { 
String   oldEscapeDigits  =  escapeDigits  ; 
escapeDigits  =  newEscapeDigits  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__ESCAPE_DIGITS  ,  oldEscapeDigits  ,  escapeDigits  )  )  ; 
} 






public   String   getFormat  (  )  { 
return   format  ; 
} 






public   void   setFormat  (  String   newFormat  )  { 
String   oldFormat  =  format  ; 
format  =  newFormat  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__FORMAT  ,  oldFormat  ,  format  )  )  ; 
} 






public   DynamicValue   getDateTime  (  )  { 
return   dateTime  ; 
} 






public   NotificationChain   basicSetDateTime  (  DynamicValue   newDateTime  ,  NotificationChain   msgs  )  { 
DynamicValue   oldDateTime  =  dateTime  ; 
dateTime  =  newDateTime  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  ,  oldDateTime  ,  newDateTime  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setDateTime  (  DynamicValue   newDateTime  )  { 
if  (  newDateTime  !=  dateTime  )  { 
NotificationChain   msgs  =  null  ; 
if  (  dateTime  !=  null  )  msgs  =  (  (  InternalEObject  )  dateTime  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  ,  null  ,  msgs  )  ; 
if  (  newDateTime  !=  null  )  msgs  =  (  (  InternalEObject  )  newDateTime  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  ,  null  ,  msgs  )  ; 
msgs  =  basicSetDateTime  (  newDateTime  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  ,  newDateTime  ,  newDateTime  )  )  ; 
} 






public   String   getTimezone  (  )  { 
return   timezone  ; 
} 






public   void   setTimezone  (  String   newTimezone  )  { 
String   oldTimezone  =  timezone  ; 
timezone  =  newTimezone  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SAY_DATE_TIME__TIMEZONE  ,  oldTimezone  ,  timezone  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  : 
return   basicSetDateTime  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__ESCAPE_DIGITS  : 
return   getEscapeDigits  (  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__FORMAT  : 
return   getFormat  (  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  : 
return   getDateTime  (  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__TIMEZONE  : 
return   getTimezone  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__ESCAPE_DIGITS  : 
setEscapeDigits  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__FORMAT  : 
setFormat  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  : 
setDateTime  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__TIMEZONE  : 
setTimezone  (  (  String  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__ESCAPE_DIGITS  : 
setEscapeDigits  (  ESCAPE_DIGITS_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__FORMAT  : 
setFormat  (  FORMAT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  : 
setDateTime  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__TIMEZONE  : 
setTimezone  (  TIMEZONE_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__ESCAPE_DIGITS  : 
return   ESCAPE_DIGITS_EDEFAULT  ==  null  ?  escapeDigits  !=  null  :  !  ESCAPE_DIGITS_EDEFAULT  .  equals  (  escapeDigits  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__FORMAT  : 
return   FORMAT_EDEFAULT  ==  null  ?  format  !=  null  :  !  FORMAT_EDEFAULT  .  equals  (  format  )  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__DATE_TIME  : 
return   dateTime  !=  null  ; 
case   ActionstepPackage  .  SAY_DATE_TIME__TIMEZONE  : 
return   TIMEZONE_EDEFAULT  ==  null  ?  timezone  !=  null  :  !  TIMEZONE_EDEFAULT  .  equals  (  timezone  )  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  SAY_DATE_TIME__CALL1  : 
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
return   ActionstepPackage  .  SAY_DATE_TIME__CALL1  ; 
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
result  .  append  (  ", format: "  )  ; 
result  .  append  (  format  )  ; 
result  .  append  (  ", timezone: "  )  ; 
result  .  append  (  timezone  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

