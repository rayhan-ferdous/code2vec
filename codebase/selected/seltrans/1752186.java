package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  asteriskjava  .  manager  .  ManagerConnection  ; 
import   org  .  asteriskjava  .  manager  .  TimeoutException  ; 
import   org  .  asteriskjava  .  manager  .  action  .  MonitorAction  ; 
import   org  .  asteriskjava  .  manager  .  response  .  ManagerError  ; 
import   org  .  asteriskjava  .  manager  .  response  .  ManagerResponse  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  RecordCall  ; 
import   com  .  safi  .  asterisk  .  util  .  AsteriskSafletConstants  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepFactory  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  Output  ; 
import   com  .  safi  .  core  .  actionstep  .  OutputType  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  Saflet  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 

















public   class   RecordCallImpl   extends   AsteriskActionStepImpl   implements   RecordCall  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   filename  ; 









protected   static   final   String   FORMAT_EDEFAULT  =  "wav"  ; 









protected   String   format  =  FORMAT_EDEFAULT  ; 









protected   static   final   boolean   MIX_EDEFAULT  =  true  ; 









protected   boolean   mix  =  MIX_EDEFAULT  ; 






protected   RecordCallImpl  (  )  { 
super  (  )  ; 
} 

@  Override 
public   void   beginProcessing  (  SafletContext   context  )  throws   ActionStepException  { 
super  .  beginProcessing  (  context  )  ; 
Exception   exception  =  null  ; 
int   idx  =  1  ; 
Object   variableRawValue  =  context  .  getVariableRawValue  (  AsteriskSafletConstants  .  VAR_KEY_MANAGER_CONNECTION  )  ; 
if  (  variableRawValue  ==  null  ||  !  (  variableRawValue   instanceof   ManagerConnection  )  )  exception  =  new   ActionStepException  (  "No manager connection found in current context"  )  ;  else  { 
ManagerConnection   connection  =  (  ManagerConnection  )  variableRawValue  ; 
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
MonitorAction   action  =  new   MonitorAction  (  )  ; 
Object   dynValue  =  resolveDynamicValue  (  filename  ,  context  )  ; 
String   filename  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
action  .  setFile  (  filename  )  ; 
action  .  setFormat  (  format  )  ; 
action  .  setMix  (  mix  )  ; 
String   chan  =  channel  .  getName  (  )  ; 
action  .  setChannel  (  chan  )  ; 
ManagerResponse   response  =  connection  .  sendAction  (  action  ,  Saflet  .  DEFAULT_MANAGER_ACTION_TIMEOUT  )  ; 
if  (  response   instanceof   ManagerError  )  exception  =  new   ActionStepException  (  "Couldn't redirect call to extension: "  +  response  )  ; 
}  catch  (  Exception   e  )  { 
exception  =  e  ; 
} 
} 
if  (  exception  !=  null  )  { 
if  (  exception   instanceof   TimeoutException  )  { 
idx  =  2  ; 
}  else  { 
handleException  (  context  ,  exception  )  ; 
return  ; 
} 
} 
handleSuccess  (  context  ,  idx  )  ; 
} 

@  Override 
public   void   createDefaultOutputs  (  )  { 
super  .  createDefaultOutputs  (  )  ; 
Output   o  =  ActionStepFactory  .  eINSTANCE  .  createOutput  (  )  ; 
o  .  setOutputType  (  OutputType  .  CHOICE  )  ; 
o  .  setName  (  "timeout"  )  ; 
setErrorOutput  (  o  )  ; 
getOutputs  (  )  .  add  (  o  )  ; 
} 






@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  RECORD_CALL  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  RECORD_CALL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
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
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  RECORD_CALL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getFilename  (  )  { 
return   filename  ; 
} 






public   NotificationChain   basicSetFilename  (  DynamicValue   newFilename  ,  NotificationChain   msgs  )  { 
DynamicValue   oldFilename  =  filename  ; 
filename  =  newFilename  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  RECORD_CALL__FILENAME  ,  oldFilename  ,  newFilename  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setFilename  (  DynamicValue   newFilename  )  { 
if  (  newFilename  !=  filename  )  { 
NotificationChain   msgs  =  null  ; 
if  (  filename  !=  null  )  msgs  =  (  (  InternalEObject  )  filename  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  RECORD_CALL__FILENAME  ,  null  ,  msgs  )  ; 
if  (  newFilename  !=  null  )  msgs  =  (  (  InternalEObject  )  newFilename  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  RECORD_CALL__FILENAME  ,  null  ,  msgs  )  ; 
msgs  =  basicSetFilename  (  newFilename  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  RECORD_CALL__FILENAME  ,  newFilename  ,  newFilename  )  )  ; 
} 






public   String   getFormat  (  )  { 
return   format  ; 
} 






public   void   setFormat  (  String   newFormat  )  { 
String   oldFormat  =  format  ; 
format  =  newFormat  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  RECORD_CALL__FORMAT  ,  oldFormat  ,  format  )  )  ; 
} 






public   boolean   isMix  (  )  { 
return   mix  ; 
} 






public   void   setMix  (  boolean   newMix  )  { 
boolean   oldMix  =  mix  ; 
mix  =  newMix  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  RECORD_CALL__MIX  ,  oldMix  ,  mix  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__FILENAME  : 
return   basicSetFilename  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  RECORD_CALL__FILENAME  : 
return   getFilename  (  )  ; 
case   ActionstepPackage  .  RECORD_CALL__FORMAT  : 
return   getFormat  (  )  ; 
case   ActionstepPackage  .  RECORD_CALL__MIX  : 
return   isMix  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__FILENAME  : 
setFilename  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__FORMAT  : 
setFormat  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__MIX  : 
setMix  (  (  Boolean  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__FILENAME  : 
setFilename  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__FORMAT  : 
setFormat  (  FORMAT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  RECORD_CALL__MIX  : 
setMix  (  MIX_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  RECORD_CALL__FILENAME  : 
return   filename  !=  null  ; 
case   ActionstepPackage  .  RECORD_CALL__FORMAT  : 
return   FORMAT_EDEFAULT  ==  null  ?  format  !=  null  :  !  FORMAT_EDEFAULT  .  equals  (  format  )  ; 
case   ActionstepPackage  .  RECORD_CALL__MIX  : 
return   mix  !=  MIX_EDEFAULT  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  RECORD_CALL__CALL1  : 
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
return   ActionstepPackage  .  RECORD_CALL__CALL1  ; 
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
result  .  append  (  " (format: "  )  ; 
result  .  append  (  format  )  ; 
result  .  append  (  ", mix: "  )  ; 
result  .  append  (  mix  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

