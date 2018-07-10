package   visualizador  ; 

import   java  .  util  .  Date  ; 
import   net  .  javahispano  .  jsignalwb  .  jsignalmonitor  .  JSignalMonitorDataSourceAdapter  ; 















public   class   JSMDataSource   extends   JSignalMonitorDataSourceAdapter  { 

private   Date   timeOriginDate  ; 

private   float   ecgSamplingRate  =  1  ,  hrSamplingRate  =  1  ; 

private   float   ecgRangeMinimum  =  -  20  ,  ecgRangeMax  =  20  ,  heartRateRangeMinimum  =  0  ,  heartRateRangeMax  =  120  ; 

private   float  [  ]  ecg  ; 

private   float  [  ]  heartRate  ; 

private   static   JSMDataSource   dataSource  =  null  ; 

private   JSMDataSource  (  )  { 
timeOriginDate  =  new   Date  (  )  ; 
ecg  =  new   float  [  1000  ]  ; 
heartRate  =  new   float  [  1000  ]  ; 
for  (  int   i  =  0  ;  i  <  ecg  .  length  ;  i  ++  )  { 
ecg  [  i  ]  =  i  ; 
heartRate  [  i  ]  =  i  ; 
} 
} 

public   static   JSMDataSource   getJSWBManagerInstance  (  )  { 
if  (  dataSource  ==  null  )  { 
dataSource  =  new   JSMDataSource  (  )  ; 
} 
return   dataSource  ; 
} 




















public   float  [  ]  getChannelData  (  String   signalName  ,  long   firstValue  ,  long   lastValue  )  { 
if  (  firstValue  >=  lastValue  )  { 
return   new   float  [  0  ]  ; 
} 
float  [  ]  datos  ; 
float  [  ]  fuente  ; 
int   pos1  ,  pos2  ; 
if  (  signalName  .  equals  (  "ECG"  )  )  { 
pos1  =  TimePositionConverter  .  timeToPosition  (  timeOriginDate  .  getTime  (  )  ,  firstValue  ,  ecgSamplingRate  )  ; 
pos2  =  TimePositionConverter  .  timeToPosition  (  timeOriginDate  .  getTime  (  )  ,  lastValue  ,  ecgSamplingRate  )  ; 
datos  =  new   float  [  pos2  -  pos1  ]  ; 
fuente  =  this  .  ecg  ; 
}  else  { 
pos1  =  TimePositionConverter  .  timeToPosition  (  timeOriginDate  .  getTime  (  )  ,  firstValue  ,  hrSamplingRate  )  ; 
pos2  =  TimePositionConverter  .  timeToPosition  (  timeOriginDate  .  getTime  (  )  ,  lastValue  ,  hrSamplingRate  )  ; 
datos  =  new   float  [  pos2  -  pos1  ]  ; 
fuente  =  this  .  heartRate  ; 
} 
int   i  ; 
for  (  i  =  0  ;  i  <  datos  .  length  &&  pos1  +  i  <  fuente  .  length  ;  i  ++  )  { 
datos  [  i  ]  =  fuente  [  pos1  +  i  ]  ; 
} 
for  (  ;  i  <  datos  .  length  ;  i  ++  )  { 
datos  [  i  ]  =  0  ; 
} 
return   datos  ; 
} 















public   float   getChannelValueAtTime  (  String   signalName  ,  long   time  )  { 
int   pos1  =  TimePositionConverter  .  timeToPosition  (  timeOriginDate  .  getTime  (  )  ,  time  ,  ecgSamplingRate  )  ; 
float   dato  ; 
if  (  signalName  .  endsWith  (  "ECG"  )  )  { 
dato  =  this  .  ecg  [  pos1  ]  ; 
}  else  { 
dato  =  this  .  heartRate  [  pos1  ]  ; 
} 
return   dato  ; 
} 

public   Date   getTimeOriginDate  (  )  { 
return   timeOriginDate  ; 
} 

public   float  [  ]  getEcg  (  )  { 
return   ecg  ; 
} 

public   float  [  ]  getHeartRate  (  )  { 
return   heartRate  ; 
} 

public   float   getEcgSamplingRate  (  )  { 
return   ecgSamplingRate  ; 
} 

public   float   getHrSamplingRate  (  )  { 
return   hrSamplingRate  ; 
} 

public   float   getHeartRateRangeMax  (  )  { 
return   heartRateRangeMax  ; 
} 

public   float   getHeartRateRangeMinimum  (  )  { 
return   heartRateRangeMinimum  ; 
} 

public   float   getEcgRangeMax  (  )  { 
return   ecgRangeMax  ; 
} 

public   float   getEcgRangeMinimum  (  )  { 
return   ecgRangeMinimum  ; 
} 

public   float   getSamplingRate  (  )  { 
return   ecgSamplingRate  ; 
} 

public   void   setEcg  (  float  [  ]  ecg  )  { 
this  .  ecg  =  ecg  ; 
} 

public   void   setHeartRate  (  float  [  ]  heartRate  )  { 
this  .  heartRate  =  heartRate  ; 
} 

public   void   setSamplingRate  (  float   samplingRate  )  { 
this  .  ecgSamplingRate  =  samplingRate  ; 
} 

public   void   setTimeOriginDate  (  Date   timeOriginDate  )  { 
this  .  timeOriginDate  =  timeOriginDate  ; 
} 

public   void   setEcgSamplingRate  (  float   ecgSamplingRate  )  { 
this  .  ecgSamplingRate  =  ecgSamplingRate  ; 
} 

public   void   setHrSamplingRate  (  float   hrSamplingRate  )  { 
this  .  hrSamplingRate  =  hrSamplingRate  ; 
} 

public   void   setHeartRateRangeMinimum  (  float   heartRateRangeMinimum  )  { 
this  .  heartRateRangeMinimum  =  heartRateRangeMinimum  ; 
} 

public   void   setHeartRateRangeMax  (  float   heartRateRangeMax  )  { 
this  .  heartRateRangeMax  =  heartRateRangeMax  ; 
} 

public   void   setEcgRangeMinimum  (  float   ecgRangeMinimum  )  { 
this  .  ecgRangeMinimum  =  ecgRangeMinimum  ; 
} 

public   void   setEcgRangeMax  (  float   ecgRangeMax  )  { 
this  .  ecgRangeMax  =  ecgRangeMax  ; 
} 
} 

