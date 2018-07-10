package   jhomenet  .  commons  .  responsive  .  condition  ; 

import   org  .  apache  .  log4j  .  Logger  ; 
import   jhomenet  .  commons  .  GeneralApplicationContext  ; 
import   jhomenet  .  commons  .  data  .  ValueData  ; 
import   jhomenet  .  commons  .  hw  .  HardwareException  ; 
import   jhomenet  .  commons  .  hw  .  RegisteredHardware  ; 
import   jhomenet  .  commons  .  hw  .  mngt  .  NoSuchHardwareException  ; 
import   jhomenet  .  commons  .  hw  .  sensor  .  *  ; 
import   jhomenet  .  commons  .  hw  .  states  .  State  ; 
import   jhomenet  .  commons  .  responsive  .  ResponsiveException  ; 


















public   class   StateCondition   extends   SensorCondition  <  StateSensor  >  { 




private   static   Logger   logger  =  Logger  .  getLogger  (  StateCondition  .  class  .  getName  (  )  )  ; 




private   State   testState  ; 









public   StateCondition  (  String   conditionName  ,  StateSensor   stateSensor  ,  Integer   channel  ,  State   testState  )  { 
super  (  conditionName  ,  stateSensor  ,  channel  )  ; 
this  .  testState  =  testState  ; 
} 








public   StateCondition  (  String   conditionName  ,  StateSensor   stateSensor  ,  State   testState  )  { 
super  (  conditionName  ,  stateSensor  )  ; 
this  .  testState  =  testState  ; 
} 




private   StateCondition  (  )  { 
this  (  null  ,  null  ,  null  )  ; 
} 




@  Override 
public   void   injectAppContext  (  GeneralApplicationContext   serverContext  )  { 
try  { 
RegisteredHardware   hw  =  serverContext  .  getHardwareManager  (  )  .  getRegisteredHardware  (  getHardwareAddr  (  )  )  ; 
if  (  hw   instanceof   StateSensor  )  this  .  setSensor  (  (  StateSensor  )  hw  )  ; 
}  catch  (  NoSuchHardwareException   nshe  )  { 
} 
} 






public   void   setTestState  (  State   testState  )  { 
this  .  testState  =  testState  ; 
} 






public   State   getTestState  (  )  { 
return   this  .  testState  ; 
} 




public   ConditionResult   evaluate  (  )  throws   ResponsiveException  { 
try  { 
State   currentState  =  getSensor  (  )  .  readFromSensor  (  getChannel  (  )  )  .  getDataObject  (  )  ; 
if  (  currentState  .  equals  (  testState  )  )  { 
return   new   StateConditionResult  (  Boolean  .  TRUE  ,  currentState  )  ; 
}  else  { 
return   new   StateConditionResult  (  Boolean  .  FALSE  ,  currentState  )  ; 
} 
}  catch  (  HardwareException   he  )  { 
logger  .  error  (  "Hardware exception while evaluating state condition: "  +  he  .  getMessage  (  )  )  ; 
throw   new   ResponsiveException  (  he  )  ; 
} 
} 




@  Override 
public   int   hashCode  (  )  { 
final   int   prime  =  31  ; 
int   result  =  super  .  hashCode  (  )  ; 
result  =  prime  *  result  +  (  (  testState  ==  null  )  ?  0  :  testState  .  hashCode  (  )  )  ; 
return   result  ; 
} 




@  Override 
public   boolean   equals  (  Object   obj  )  { 
if  (  this  ==  obj  )  return   true  ; 
if  (  !  super  .  equals  (  obj  )  )  return   false  ; 
if  (  getClass  (  )  !=  obj  .  getClass  (  )  )  return   false  ; 
final   StateCondition   other  =  (  StateCondition  )  obj  ; 
if  (  testState  ==  null  )  { 
if  (  other  .  testState  !=  null  )  return   false  ; 
}  else   if  (  !  testState  .  equals  (  other  .  testState  )  )  return   false  ; 
return   true  ; 
} 

private   class   StateConditionResult   implements   ConditionResult  { 

private   final   Boolean   result  ; 

private   final   State   state  ; 

private   StateConditionResult  (  Boolean   result  ,  State   state  )  { 
super  (  )  ; 
this  .  result  =  result  ; 
this  .  state  =  state  ; 
} 




@  Override 
public   Boolean   getResult  (  )  { 
return   this  .  result  ; 
} 




@  Override 
public   String   getResultAsString  (  )  { 
return   String  .  valueOf  (  result  )  ; 
} 




@  Override 
public   String   getResultDetails  (  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "State condition result"  )  ; 
buf  .  append  (  "  Hardware desc: "  +  getSensor  (  )  .  getHardwareSetupDescription  (  )  )  ; 
buf  .  append  (  "  Hardware address: "  +  getSensor  (  )  .  getHardwareAddr  (  )  )  ; 
buf  .  append  (  "  State: "  +  state  .  toString  (  )  )  ; 
return   buf  .  toString  (  )  ; 
} 
} 
} 

