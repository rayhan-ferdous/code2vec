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
import   com  .  safi  .  asterisk  .  actionstep  .  Festival  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 
















public   class   FestivalImpl   extends   AsteriskActionStepImpl   implements   Festival  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   text  ; 









protected   static   final   String   INTERRUPT_KEYS_EDEFAULT  =  null  ; 









protected   String   interruptKeys  =  INTERRUPT_KEYS_EDEFAULT  ; 






protected   FestivalImpl  (  )  { 
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
String   txt  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  resolveDynamicValue  (  text  ,  context  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Festival about to vocalize: "  +  txt  )  ; 
StringBuffer   appCmd  =  new   StringBuffer  (  txt  )  ; 
if  (  StringUtils  .  isNotBlank  (  interruptKeys  )  )  appCmd  .  append  (  ","  )  .  append  (  interruptKeys  )  ; 
int   result  =  channel  .  exec  (  "Festival"  ,  appCmd  .  toString  (  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Festival returned "  +  translateAppReturnValue  (  result  )  +  " of int "  +  result  )  ; 
if  (  result  ==  -  2  )  { 
exception  =  new   ActionStepException  (  "Application Festival not found"  )  ; 
}  else   if  (  result  ==  -  1  )  { 
exception  =  new   ActionStepException  (  "Channel was hung up or command failed"  )  ; 
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
return   ActionstepPackage  .  Literals  .  FESTIVAL  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  FESTIVAL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  FESTIVAL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getText  (  )  { 
return   text  ; 
} 






public   NotificationChain   basicSetText  (  DynamicValue   newText  ,  NotificationChain   msgs  )  { 
DynamicValue   oldText  =  text  ; 
text  =  newText  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  FESTIVAL__TEXT  ,  oldText  ,  newText  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setText  (  DynamicValue   newText  )  { 
if  (  newText  !=  text  )  { 
NotificationChain   msgs  =  null  ; 
if  (  text  !=  null  )  msgs  =  (  (  InternalEObject  )  text  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  FESTIVAL__TEXT  ,  null  ,  msgs  )  ; 
if  (  newText  !=  null  )  msgs  =  (  (  InternalEObject  )  newText  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  FESTIVAL__TEXT  ,  null  ,  msgs  )  ; 
msgs  =  basicSetText  (  newText  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  FESTIVAL__TEXT  ,  newText  ,  newText  )  )  ; 
} 






public   String   getInterruptKeys  (  )  { 
return   interruptKeys  ; 
} 






public   void   setInterruptKeys  (  String   newInterruptKeys  )  { 
String   oldInterruptKeys  =  interruptKeys  ; 
interruptKeys  =  newInterruptKeys  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  FESTIVAL__INTERRUPT_KEYS  ,  oldInterruptKeys  ,  interruptKeys  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  FESTIVAL__TEXT  : 
return   basicSetText  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  FESTIVAL__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  FESTIVAL__TEXT  : 
return   getText  (  )  ; 
case   ActionstepPackage  .  FESTIVAL__INTERRUPT_KEYS  : 
return   getInterruptKeys  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  FESTIVAL__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  FESTIVAL__TEXT  : 
setText  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  FESTIVAL__INTERRUPT_KEYS  : 
setInterruptKeys  (  (  String  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  FESTIVAL__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  FESTIVAL__TEXT  : 
setText  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  FESTIVAL__INTERRUPT_KEYS  : 
setInterruptKeys  (  INTERRUPT_KEYS_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  FESTIVAL__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  FESTIVAL__TEXT  : 
return   text  !=  null  ; 
case   ActionstepPackage  .  FESTIVAL__INTERRUPT_KEYS  : 
return   INTERRUPT_KEYS_EDEFAULT  ==  null  ?  interruptKeys  !=  null  :  !  INTERRUPT_KEYS_EDEFAULT  .  equals  (  interruptKeys  )  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  FESTIVAL__CALL1  : 
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
return   ActionstepPackage  .  FESTIVAL__CALL1  ; 
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
result  .  append  (  " (interruptKeys: "  )  ; 
result  .  append  (  interruptKeys  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

