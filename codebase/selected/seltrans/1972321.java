package   bluetoothgateway  ; 

import   java  .  io  .  InvalidClassException  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Vector  ; 





public   class   DataPacket  { 

private   Date   m_timestamp  ; 

private   Date   m_original_timestamp  ; 

private   Vector   m_data  ; 

private   String   m_sensorname  ; 

private   String  [  ]  m_channels  ; 

public   DataPacket  (  Date   timestamp  ,  Vector   data  ,  String   sensorname  ,  String  [  ]  channels  )  { 
m_original_timestamp  =  m_timestamp  =  timestamp  ; 
m_data  =  data  ; 
m_sensorname  =  sensorname  ; 
m_channels  =  channels  ; 
} 

public   String   getSensorname  (  )  { 
return   m_sensorname  ; 
} 

public   String  [  ]  getChannels  (  )  { 
return   m_channels  ; 
} 

public   Vector   getValues  (  )  { 
return   m_data  ; 
} 

public   long   getTimestamp  (  )  { 
return   m_timestamp  .  getTime  (  )  ; 
} 

public   void   setTimestamp  (  Date   timestamp  )  { 
m_timestamp  =  timestamp  ; 
} 






public   Date   getOriginalTimestamp  (  )  { 
return   m_original_timestamp  ; 
} 





public   String   toString  (  )  { 
String   strMessage  =  printDigits  (  m_timestamp  .  getTime  (  )  ,  12  ,  true  )  ; 
for  (  int   i  =  0  ;  i  <  m_data  .  size  (  )  ;  i  ++  )  { 
if  (  m_data  .  get  (  i  )  instanceof   Character  )  { 
strMessage  +=  " "  +  Character  .  getNumericValue  (  (  (  Character  )  m_data  .  get  (  i  )  )  .  charValue  (  )  )  ; 
}  else  { 
strMessage  +=  " "  +  m_data  .  get  (  i  )  ; 
} 
} 
return   strMessage  ; 
} 








public   int   getIntValue  (  int   index  )  throws   IndexOutOfBoundsException  ,  InvalidClassException  { 
if  (  index  >=  m_data  .  size  (  )  )  throw   new   IndexOutOfBoundsException  (  )  ; 
Object   obj  =  m_data  .  get  (  index  )  ; 
int   value  ; 
if  (  obj   instanceof   Character  )  { 
value  =  (  int  )  (  (  Character  )  obj  )  .  charValue  (  )  ; 
}  else   if  (  obj   instanceof   Short  )  { 
value  =  (  int  )  (  (  Short  )  obj  )  .  shortValue  (  )  ; 
}  else   if  (  obj   instanceof   Integer  )  { 
value  =  (  int  )  (  (  Integer  )  obj  )  .  intValue  (  )  ; 
}  else  { 
throw   new   InvalidClassException  (  "Unkown data type within DataPacket value"  )  ; 
} 
return   value  ; 
} 








public   long   getMaxValue  (  int   index  )  throws   IndexOutOfBoundsException  ,  InvalidClassException  { 
if  (  index  >=  m_data  .  size  (  )  )  throw   new   IndexOutOfBoundsException  (  )  ; 
Object   obj  =  m_data  .  get  (  index  )  ; 
long   value  ; 
if  (  obj   instanceof   Character  )  { 
value  =  Character  .  MAX_VALUE  ; 
}  else   if  (  obj   instanceof   Short  )  { 
value  =  Short  .  MAX_VALUE  ; 
} 
if  (  obj   instanceof   Integer  )  { 
value  =  Integer  .  MAX_VALUE  ; 
}  else  { 
throw   new   InvalidClassException  (  "Unkown data type within DataPacket value"  )  ; 
} 
return   value  ; 
} 








private   String   printDigits  (  long   iNumber  ,  int   iDigits  ,  boolean   zeroPadding  )  { 
byte  [  ]  converted  =  new   byte  [  iDigits  ]  ; 
long   curNumber  =  iNumber  ; 
for  (  int   i  =  0  ;  i  <  converted  .  length  ;  i  ++  )  { 
if  (  curNumber  >  0  )  { 
converted  [  converted  .  length  -  1  -  i  ]  =  (  byte  )  (  '0'  +  curNumber  %  10  )  ; 
curNumber  /=  10  ; 
}  else  { 
converted  [  converted  .  length  -  1  -  i  ]  =  (  byte  )  (  zeroPadding  ?  '0'  :  ' '  )  ; 
} 
} 
return   new   String  (  converted  )  ; 
} 
} 

