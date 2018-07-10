package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   java  .  util  .  logging  .  Level  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  SetChannelVariable  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 
















public   class   SetChannelVariableImpl   extends   AsteriskActionStepImpl   implements   SetChannelVariable  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   value  ; 









protected   static   final   String   VARIABLE_EDEFAULT  =  null  ; 









protected   String   variable  =  VARIABLE_EDEFAULT  ; 






protected   SetChannelVariableImpl  (  )  { 
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
String   valueStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  resolveDynamicValue  (  value  ,  context  )  )  ; 
String   varStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  variable  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  { 
debug  (  "Setting Asterisk channel variable "  +  varStr  +  " to value "  +  valueStr  +  " for channel "  +  channel  .  getName  (  )  )  ; 
} 
channel  .  setVariable  (  varStr  ,  valueStr  )  ; 
}  catch  (  Exception   e  )  { 
handleException  (  context  ,  e  )  ; 
return  ; 
} 
handleSuccess  (  context  )  ; 
} 






@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  SET_CHANNEL_VARIABLE  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getValue  (  )  { 
return   value  ; 
} 






public   NotificationChain   basicSetValue  (  DynamicValue   newValue  ,  NotificationChain   msgs  )  { 
DynamicValue   oldValue  =  value  ; 
value  =  newValue  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  ,  oldValue  ,  newValue  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setValue  (  DynamicValue   newValue  )  { 
if  (  newValue  !=  value  )  { 
NotificationChain   msgs  =  null  ; 
if  (  value  !=  null  )  msgs  =  (  (  InternalEObject  )  value  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  ,  null  ,  msgs  )  ; 
if  (  newValue  !=  null  )  msgs  =  (  (  InternalEObject  )  newValue  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  ,  null  ,  msgs  )  ; 
msgs  =  basicSetValue  (  newValue  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  ,  newValue  ,  newValue  )  )  ; 
} 






public   String   getVariable  (  )  { 
return   variable  ; 
} 






public   void   setVariable  (  String   newVariable  )  { 
String   oldVariable  =  variable  ; 
variable  =  newVariable  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  SET_CHANNEL_VARIABLE__VARIABLE  ,  oldVariable  ,  variable  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  : 
return   basicSetValue  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  : 
return   getValue  (  )  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VARIABLE  : 
return   getVariable  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  : 
setValue  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VARIABLE  : 
setVariable  (  (  String  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  : 
setValue  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VARIABLE  : 
setVariable  (  VARIABLE_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VALUE  : 
return   value  !=  null  ; 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__VARIABLE  : 
return   VARIABLE_EDEFAULT  ==  null  ?  variable  !=  null  :  !  VARIABLE_EDEFAULT  .  equals  (  variable  )  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  : 
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
return   ActionstepPackage  .  SET_CHANNEL_VARIABLE__CALL1  ; 
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
result  .  append  (  " (variable: "  )  ; 
result  .  append  (  variable  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

