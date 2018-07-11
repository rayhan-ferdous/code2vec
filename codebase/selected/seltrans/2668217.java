package   gov  .  sns  .  apps  .  viewers  .  arraypvviewer  .  controller  ; 

import   java  .  util  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   gov  .  sns  .  ca  .  *  ; 
import   gov  .  sns  .  tools  .  scan  .  SecondEdition  .  MonitoredPV  ; 










public   class   ArrayDataPV  { 

private   Object   syncObj  =  new   Object  (  )  ; 

private   Vector   controllers  =  new   Vector  (  )  ; 

private   double  [  ]  vals  =  new   double  [  0  ]  ; 

private   MonitoredPV   mpv  =  null  ; 

private   ActionListener   updateListener  =  null  ; 

private   static   int   nextIndex  =  0  ; 

private   volatile   boolean   switchOn  =  true  ; 


public   ArrayDataPV  (  )  { 
updateListener  =  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
synchronized  (  syncObj  )  { 
if  (  !  switchOn  )  { 
if  (  vals  .  length  !=  0  )  { 
vals  =  new   double  [  0  ]  ; 
} 
return  ; 
} 
if  (  mpv  !=  null  &&  mpv  .  isGood  (  )  )  { 
double  [  ]  localVals  =  new   double  [  0  ]  ; 
Channel   ch  =  mpv  .  getChannel  (  )  ; 
try  { 
localVals  =  ch  .  getArrDbl  (  )  ; 
}  catch  (  ConnectionException   exp  )  { 
vals  =  new   double  [  0  ]  ; 
}  catch  (  GetException   exp  )  { 
vals  =  new   double  [  0  ]  ; 
} 
if  (  localVals  .  length  !=  vals  .  length  )  { 
vals  =  new   double  [  localVals  .  length  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  localVals  .  length  ;  i  ++  )  { 
vals  [  i  ]  =  localVals  [  i  ]  ; 
} 
}  else  { 
vals  =  new   double  [  0  ]  ; 
} 
} 
update  (  )  ; 
} 
}  ; 
mpv  =  MonitoredPV  .  getMonitoredPV  (  "ArrayDataPV_"  +  nextIndex  )  ; 
mpv  .  addValueListener  (  updateListener  )  ; 
nextIndex  ++  ; 
} 








public   double  [  ]  getValues  (  )  { 
return   vals  ; 
} 






public   void   setChannelName  (  String   chanName  )  { 
mpv  .  setChannelName  (  chanName  )  ; 
} 






public   String   getChannelName  (  )  { 
return   mpv  .  getChannelName  (  )  ; 
} 






public   void   setChannel  (  Channel   chIn  )  { 
mpv  .  setChannel  (  chIn  )  ; 
} 






public   Channel   getChannel  (  )  { 
return   mpv  .  getChannel  (  )  ; 
} 





public   void   update  (  )  { 
for  (  int   j  =  0  ;  j  <  controllers  .  size  (  )  ;  j  ++  )  { 
(  (  UpdatingController  )  controllers  .  get  (  j  )  )  .  update  (  )  ; 
} 
} 







protected   Vector   getControllers  (  )  { 
return   new   Vector  (  controllers  )  ; 
} 






public   Object   getSyncObject  (  )  { 
return   syncObj  ; 
} 







protected   void   setSyncObject  (  Object   syncObj  )  { 
synchronized  (  syncObj  )  { 
synchronized  (  this  .  syncObj  )  { 
this  .  syncObj  =  syncObj  ; 
} 
} 
} 







protected   void   addController  (  UpdatingController   cntr  )  { 
if  (  cntr  ==  null  )  { 
return  ; 
} 
if  (  !  controllers  .  contains  (  cntr  )  )  { 
Object   localSyncObj  =  cntr  .  getSyncObj  (  )  ; 
synchronized  (  localSyncObj  )  { 
synchronized  (  syncObj  )  { 
controllers  .  add  (  cntr  )  ; 
setSyncObject  (  localSyncObj  )  ; 
startMonitoring  (  )  ; 
} 
} 
} 
} 







protected   void   removeController  (  UpdatingController   cntr  )  { 
if  (  cntr  ==  null  )  { 
return  ; 
} 
synchronized  (  cntr  .  getSyncObj  (  )  )  { 
controllers  .  remove  (  cntr  )  ; 
if  (  controllers  .  size  (  )  ==  0  )  { 
stopMonitoring  (  )  ; 
setSyncObject  (  new   Object  (  )  )  ; 
} 
} 
} 





public   void   removeControllers  (  )  { 
Vector   cntrls  =  getControllers  (  )  ; 
for  (  int   i  =  0  ;  i  <  cntrls  .  size  (  )  ;  i  ++  )  { 
(  (  UpdatingController  )  cntrls  .  get  (  i  )  )  .  removeArrayDataPV  (  this  )  ; 
} 
} 






public   boolean   getSwitchOn  (  )  { 
return   switchOn  ; 
} 






public   void   setSwitchOn  (  boolean   switchOn  )  { 
this  .  switchOn  =  switchOn  ; 
synchronized  (  syncObj  )  { 
if  (  !  switchOn  )  { 
if  (  vals  .  length  !=  0  )  { 
vals  =  new   double  [  0  ]  ; 
} 
} 
} 
update  (  )  ; 
} 


private   void   stopMonitoring  (  )  { 
mpv  .  stopMonitor  (  )  ; 
} 


private   void   startMonitoring  (  )  { 
mpv  .  startMonitor  (  )  ; 
} 


@  Override 
protected   void   finalize  (  )  { 
MonitoredPV  .  removeMonitoredPV  (  mpv  )  ; 
} 
} 

