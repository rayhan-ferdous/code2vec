package   gov  .  sns  .  apps  .  ringinjection  ; 

import   gov  .  sns  .  tools  .  *  ; 
import   gov  .  sns  .  xal  .  smf  .  *  ; 
import   gov  .  sns  .  xal  .  smf  .  impl  .  *  ; 
import   gov  .  sns  .  tools  .  *  ; 
import   gov  .  sns  .  ca  .  *  ; 
import   java  .  util  .  *  ; 










public   class   BpmAgent  { 

protected   BPM   bpmNode  ; 

protected   AcceleratorSeq   sequence  ; 

protected   Channel   xavgch  ; 

protected   Channel   yavgch  ; 

public   double  [  ]  xtbtfitparams  ; 

public   double  [  ]  ytbtfitparams  ; 

public   double  [  ]  xfoilresults  =  new   double  [  4  ]  ; 

public   double  [  ]  yfoilresults  =  new   double  [  4  ]  ; 

public   double  [  ]  xtbtdata  ; 

public   double  [  ]  ytbtdata  ; 

public   int   fitsize  ; 


public   BpmAgent  (  AcceleratorSeq   aSequence  ,  BPM   newBpmNode  )  { 
bpmNode  =  newBpmNode  ; 
sequence  =  aSequence  ; 
xavgch  =  bpmNode  .  getChannel  (  BPM  .  X_TBT_HANDLE  )  ; 
yavgch  =  bpmNode  .  getChannel  (  BPM  .  Y_TBT_HANDLE  )  ; 
xavgch  .  requestConnection  (  )  ; 
yavgch  .  requestConnection  (  )  ; 
bpmNode  .  getChannel  (  BPM  .  X_AVG_HANDLE  )  .  requestConnection  (  )  ; 
bpmNode  .  getChannel  (  BPM  .  Y_AVG_HANDLE  )  .  requestConnection  (  )  ; 
} 


public   String   name  (  )  { 
return   bpmNode  .  getId  (  )  ; 
} 


public   BPM   getNode  (  )  { 
return   bpmNode  ; 
} 


public   double   position  (  )  { 
return   getPosition  (  )  ; 
} 


public   double   getPosition  (  )  { 
return   sequence  .  getPosition  (  bpmNode  )  ; 
} 


public   boolean   isConnected  (  )  { 
return   xavgch  .  isConnected  (  )  &&  yavgch  .  isConnected  (  )  &&  bpmNode  .  getChannel  (  BPM  .  X_AVG_HANDLE  )  .  isConnected  (  )  &&  bpmNode  .  getChannel  (  BPM  .  Y_AVG_HANDLE  )  .  isConnected  (  )  ; 
} 


public   boolean   isOkay  (  )  { 
return   isOkay  (  bpmNode  )  ; 
} 


public   static   boolean   isOkay  (  BPM   bpm  )  { 
return   bpm  .  getStatus  (  )  ; 
} 


public   static   boolean   nodeCanConnect  (  BPM   bpm  )  { 
boolean   canConnectx  =  true  ; 
boolean   canConnecty  =  true  ; 
boolean   canConnect  =  false  ; 
try  { 
canConnectx  =  bpm  .  getChannel  (  BPM  .  X_TBT_HANDLE  )  .  connectAndWait  (  )  ; 
canConnectx  =  bpm  .  getChannel  (  BPM  .  Y_TBT_HANDLE  )  .  connectAndWait  (  )  ; 
}  catch  (  NoSuchChannelException   excpt  )  { 
if  (  !  canConnectx  ||  !  canConnecty  )  { 
canConnect  =  false  ; 
} 
} 
return   canConnect  ; 
} 


public   String   toString  (  )  { 
return   name  (  )  ; 
} 


public   static   List   getNodes  (  List   bpmAgents  )  { 
int   count  =  bpmAgents  .  size  (  )  ; 
List   bpmNodes  =  new   ArrayList  (  count  )  ; 
for  (  int   index  =  0  ;  index  <  count  ;  index  ++  )  { 
BpmAgent   bpmAgent  =  (  BpmAgent  )  bpmAgents  .  get  (  index  )  ; 
bpmNodes  .  add  (  bpmAgent  .  getNode  (  )  )  ; 
} 
return   bpmNodes  ; 
} 


public   double   getXAvg  (  )  { 
double   xavg  =  0.0  ; 
try  { 
xavg  =  bpmNode  .  getXAvg  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
System  .  out  .  println  (  "Could not connect with:"  +  bpmNode  .  getId  (  )  )  ; 
}  catch  (  GetException   e  )  { 
System  .  out  .  println  (  "Could not get value for:"  +  bpmNode  .  getId  (  )  )  ; 
} 
return   xavg  ; 
} 


public   double  [  ]  getXAvgTBTArray  (  )  { 
double  [  ]  value  ; 
int   size  =  0  ; 
try  { 
size  =  xavgch  .  elementCount  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
} 
value  =  new   double  [  size  ]  ; 
try  { 
value  =  xavgch  .  getArrDbl  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
System  .  err  .  println  (  "Unable to connect to: "  +  xavgch  .  channelName  (  )  )  ; 
}  catch  (  GetException   e  )  { 
System  .  err  .  println  (  "Unable to get process variables."  )  ; 
} 
return   value  ; 
} 


public   double   getYAvg  (  )  { 
double   yavg  =  0.0  ; 
try  { 
yavg  =  bpmNode  .  getYAvg  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
System  .  out  .  println  (  "Could not connect with:"  +  bpmNode  .  getId  (  )  )  ; 
}  catch  (  GetException   e  )  { 
System  .  out  .  println  (  "Could not get value from:"  +  bpmNode  .  getId  (  )  )  ; 
} 
return   yavg  ; 
} 


public   double  [  ]  getYAvgTBTArray  (  )  { 
double  [  ]  value  ; 
int   size  =  0  ; 
try  { 
size  =  yavgch  .  elementCount  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
} 
value  =  new   double  [  size  ]  ; 
try  { 
value  =  yavgch  .  getArrDbl  (  )  ; 
}  catch  (  ConnectionException   e  )  { 
System  .  err  .  println  (  "Unable to connect to channel access."  )  ; 
}  catch  (  GetException   e  )  { 
System  .  err  .  println  (  "Unable to get process variables."  )  ; 
} 
return   value  ; 
} 


public   void   setDataSize  (  int   size  )  { 
fitsize  =  size  ; 
} 


public   int   getDataSize  (  )  { 
return   fitsize  ; 
} 


public   void   setXTBTFitParams  (  double  [  ]  params  )  { 
xtbtfitparams  =  params  ; 
} 


public   double  [  ]  getXTBTFitParams  (  )  { 
return   xtbtfitparams  ; 
} 


public   void   setYTBTFitParams  (  double  [  ]  params  )  { 
ytbtfitparams  =  params  ; 
} 


public   double  [  ]  getYTBTFitParams  (  )  { 
return   ytbtfitparams  ; 
} 


public   void   setXFoilResults  (  double  [  ]  params  )  { 
xfoilresults  =  params  ; 
} 


public   double  [  ]  getXFoilResults  (  )  { 
return   xfoilresults  ; 
} 


public   void   setYFoilResults  (  double  [  ]  params  )  { 
yfoilresults  =  params  ; 
} 


public   double  [  ]  getYFoilResults  (  )  { 
return   yfoilresults  ; 
} 


public   void   saveXTBTData  (  double  [  ]  data  )  { 
xtbtdata  =  data  ; 
} 


public   double  [  ]  getXTBTData  (  )  { 
return   xtbtdata  ; 
} 


public   void   saveYTBTData  (  double  [  ]  data  )  { 
ytbtdata  =  data  ; 
} 


public   double  [  ]  getYTBTData  (  )  { 
return   ytbtdata  ; 
} 
} 

