package   ch  .  unizh  .  ini  .  jaer  .  chip  .  dvs320  ; 

import   ch  .  unizh  .  ini  .  jaer  .  chip  .  retina  .  *  ; 
import   net  .  sf  .  jaer  .  aemonitor  .  *  ; 
import   net  .  sf  .  jaer  .  biasgen  .  *  ; 
import   net  .  sf  .  jaer  .  chip  .  *  ; 
import   net  .  sf  .  jaer  .  event  .  *  ; 
import   net  .  sf  .  jaer  .  hardwareinterface  .  *  ; 
import   java  .  awt  .  BorderLayout  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Observable  ; 
import   java  .  util  .  StringTokenizer  ; 
import   javax  .  swing  .  BoxLayout  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JTabbedPane  ; 
import   net  .  sf  .  jaer  .  Description  ; 
import   net  .  sf  .  jaer  .  biasgen  .  Pot  .  Sex  ; 
import   net  .  sf  .  jaer  .  biasgen  .  Pot  .  Type  ; 
import   net  .  sf  .  jaer  .  graphics  .  DisplayMethod  ; 
import   net  .  sf  .  jaer  .  util  .  RemoteControl  ; 
import   net  .  sf  .  jaer  .  util  .  RemoteControlCommand  ; 
import   net  .  sf  .  jaer  .  util  .  RemoteControlled  ; 















@  Description  (  "cDVSTest color Dynamic Vision Test chip"  ) 
public   class   cDVSTest10   extends   AETemporalConstastRetina   implements   HasIntensity  { 


private   float   intensity  =  0  ; 


public   cDVSTest10  (  )  { 
setName  (  "cDVSTest10"  )  ; 
setSizeX  (  140  )  ; 
setSizeY  (  64  )  ; 
setNumCellTypes  (  3  )  ; 
setPixelHeightUm  (  14.5f  )  ; 
setPixelWidthUm  (  14.5f  )  ; 
setEventExtractor  (  new   cDVSTestExtractor  (  this  )  )  ; 
setBiasgen  (  new   cDVSTest10  .  cDVSTestBiasgen  (  this  )  )  ; 
DisplayMethod   m  =  getCanvas  (  )  .  getDisplayMethod  (  )  ; 
DVSWithIntensityDisplayMethod   intenDisplayMethod  =  new   DVSWithIntensityDisplayMethod  (  getCanvas  (  )  )  ; 
intenDisplayMethod  .  setIntensitySource  (  this  )  ; 
getCanvas  (  )  .  removeDisplayMethod  (  m  )  ; 
getCanvas  (  )  .  addDisplayMethod  (  intenDisplayMethod  )  ; 
getCanvas  (  )  .  setDisplayMethod  (  intenDisplayMethod  )  ; 
} 




public   cDVSTest10  (  HardwareInterface   hardwareInterface  )  { 
this  (  )  ; 
setHardwareInterface  (  hardwareInterface  )  ; 
} 

public   float   getIntensity  (  )  { 
return   intensity  ; 
} 

public   void   setIntensity  (  float   f  )  { 
intensity  =  f  ; 
} 














public   class   cDVSTestExtractor   extends   RetinaExtractor  { 

public   static   final   int   XMASK  =  0x0fe  ,  XSHIFT  =  1  ,  YMASK  =  0x3f000  ,  YSHIFT  =  12  ,  INTENSITYMASK  =  0x40000  ; 

private   int   lastIntenTs  =  0  ; 

public   cDVSTestExtractor  (  cDVSTest10   chip  )  { 
super  (  chip  )  ; 
} 

private   float   avdt  =  100  ; 





@  Override 
public   synchronized   EventPacket   extractPacket  (  AEPacketRaw   in  )  { 
if  (  out  ==  null  )  { 
out  =  new   EventPacket  <  PolarityEvent  >  (  chip  .  getEventClass  (  )  )  ; 
}  else  { 
out  .  clear  (  )  ; 
} 
if  (  in  ==  null  )  { 
return   out  ; 
} 
int   n  =  in  .  getNumEvents  (  )  ; 
int   skipBy  =  1  ; 
if  (  isSubSamplingEnabled  (  )  )  { 
while  (  n  /  skipBy  >  getSubsampleThresholdEventCount  (  )  )  { 
skipBy  ++  ; 
} 
} 
int  [  ]  a  =  in  .  getAddresses  (  )  ; 
int  [  ]  timestamps  =  in  .  getTimestamps  (  )  ; 
OutputEventIterator   outItr  =  out  .  outputIterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  +=  skipBy  )  { 
int   addr  =  a  [  i  ]  ; 
if  (  (  addr  &  INTENSITYMASK  )  !=  0  )  { 
int   dt  =  timestamps  [  i  ]  -  lastIntenTs  ; 
if  (  dt  >  50  )  { 
avdt  =  0.2f  *  dt  +  0.8f  *  avdt  ; 
setIntensity  (  50f  /  avdt  )  ; 
} 
lastIntenTs  =  timestamps  [  i  ]  ; 
} 
PolarityEvent   e  =  (  PolarityEvent  )  outItr  .  nextOutput  (  )  ; 
e  .  address  =  addr  ; 
e  .  timestamp  =  (  timestamps  [  i  ]  )  ; 
e  .  x  =  (  short  )  (  (  (  addr  &  XMASK  )  >  >  >  XSHIFT  )  )  ; 
if  (  e  .  x  <  0  )  { 
e  .  x  =  0  ; 
}  else   if  (  e  .  x  >  319  )  { 
} 
e  .  y  =  (  short  )  (  (  addr  &  YMASK  )  >  >  >  YSHIFT  )  ; 
if  (  e  .  y  >  239  )  { 
e  .  y  =  239  ; 
}  else   if  (  e  .  y  <  0  )  { 
e  .  y  =  0  ; 
} 
e  .  type  =  (  byte  )  (  addr  &  1  )  ; 
e  .  polarity  =  e  .  type  ==  0  ?  PolarityEvent  .  Polarity  .  Off  :  PolarityEvent  .  Polarity  .  On  ; 
} 
return   out  ; 
} 
} 





public   void   setHardwareInterface  (  final   HardwareInterface   hardwareInterface  )  { 
this  .  hardwareInterface  =  hardwareInterface  ; 
try  { 
if  (  getBiasgen  (  )  ==  null  )  { 
setBiasgen  (  new   cDVSTest10  .  cDVSTestBiasgen  (  this  )  )  ; 
}  else  { 
getBiasgen  (  )  .  setHardwareInterface  (  (  BiasgenHardwareInterface  )  hardwareInterface  )  ; 
} 
}  catch  (  ClassCastException   e  )  { 
log  .  warning  (  e  .  getMessage  (  )  +  ": probably this chip object has a biasgen but the hardware interface doesn't, ignoring"  )  ; 
} 
} 


























public   class   cDVSTestBiasgen   extends   net  .  sf  .  jaer  .  biasgen  .  Biasgen  { 

ArrayList  <  HasPreference  >  hasPreferencesList  =  new   ArrayList  <  HasPreference  >  (  )  ; 

private   ConfigurableIPotRev0   pcas  ,  diffOn  ,  diffOff  ,  diff  ,  red  ,  blue  ,  amp  ; 

private   ConfigurableIPotRev0   refr  ,  pr  ,  foll  ; 

cDVSTest10ControlPanel   controlPanel  ; 

AllMuxes   allMuxes  =  new   AllMuxes  (  )  ; 

private   ShiftedSourceBias   ssn  ,  ssp  ,  ssnMid  ,  sspMid  ; 

private   ShiftedSourceBias  [  ]  ssBiases  =  new   ShiftedSourceBias  [  4  ]  ; 

int   pos  =  0  ; 




public   cDVSTestBiasgen  (  Chip   chip  )  { 
super  (  chip  )  ; 
setName  (  "cDVSTest"  )  ; 
getMasterbias  (  )  .  setKPrimeNFet  (  55e-3f  )  ; 
getMasterbias  (  )  .  setMultiplier  (  9  *  (  24f  /  2.4f  )  /  (  4.8f  /  2.4f  )  )  ; 
getMasterbias  (  )  .  setWOverL  (  4.8f  /  2.4f  )  ; 
ssn  =  new   ShiftedSourceBias  (  this  )  ; 
ssn  .  setSex  (  Pot  .  Sex  .  N  )  ; 
ssn  .  setName  (  "SSN"  )  ; 
ssn  .  setTooltipString  (  "n-type shifted source that generates a regulated voltage near ground"  )  ; 
ssn  .  addObserver  (  this  )  ; 
ssp  =  new   ShiftedSourceBias  (  this  )  ; 
ssp  .  setSex  (  Pot  .  Sex  .  P  )  ; 
ssp  .  setName  (  "SSP"  )  ; 
ssp  .  setTooltipString  (  "p-type shifted source that generates a regulated voltage near Vdd"  )  ; 
ssp  .  addObserver  (  this  )  ; 
ssnMid  =  new   ShiftedSourceBias  (  this  )  ; 
ssnMid  .  setSex  (  Pot  .  Sex  .  N  )  ; 
ssnMid  .  setName  (  "SSNMid"  )  ; 
ssnMid  .  setTooltipString  (  "n-type shifted source that generates a regulated voltage inside rail, about 2 diode drops from ground"  )  ; 
ssnMid  .  addObserver  (  this  )  ; 
sspMid  =  new   ShiftedSourceBias  (  this  )  ; 
sspMid  .  setSex  (  Pot  .  Sex  .  P  )  ; 
sspMid  .  setName  (  "SSPMid"  )  ; 
sspMid  .  setTooltipString  (  "p-type shifted source that generates a regulated voltage about 2 diode drops from Vdd"  )  ; 
sspMid  .  addObserver  (  this  )  ; 
ssBiases  [  0  ]  =  ssnMid  ; 
ssBiases  [  1  ]  =  ssn  ; 
ssBiases  [  2  ]  =  sspMid  ; 
ssBiases  [  3  ]  =  ssp  ; 
setPotArray  (  new   IPotArray  (  this  )  )  ; 
try  { 
pot  (  "diff,n,normal,differencing amp"  )  ; 
pot  (  "ON,n,normal,DVS brighter threshold"  )  ; 
pot  (  "OFF,n,normal,DVS darker threshold"  )  ; 
pot  (  "Red,n,normal,Redder threshold"  )  ; 
pot  (  "Blue,n,normal,Bluer threshold"  )  ; 
pot  (  "Amp,n,normal,DVS ON threshold"  )  ; 
pot  (  "pcas,p,cascode,DVS ON threshold"  )  ; 
pot  (  "pixInvB,n,normal,pixel inverter bias"  )  ; 
pot  (  "pr,p,normal,photoreceptor bias current"  )  ; 
pot  (  "fb,p,normal,photoreceptor follower bias current"  )  ; 
pot  (  "refr,p,normal,DVS refractory current"  )  ; 
pot  (  "AReqPd,n,normal,request pulldown threshold"  )  ; 
pot  (  "AReqEndPd,n,normal,handshake state machine pulldown bias current"  )  ; 
pot  (  "AEPuX,p,normal,AER column pullup"  )  ; 
pot  (  "AEPuY,p,normal,AER row pullup"  )  ; 
pot  (  "If_threshold,n,normal,integrate and fire intensity neuroon threshold"  )  ; 
pot  (  "If_refractory,n,normal,integrate and fire intensity neuron refractory period bias current"  )  ; 
pot  (  "FollPadBias,n,normal,follower pad buffer bias current"  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   Error  (  e  .  toString  (  )  )  ; 
} 
loadPreferences  (  )  ; 
} 

private   void   pot  (  String   s  )  throws   ParseException  { 
try  { 
String   d  =  ","  ; 
StringTokenizer   t  =  new   StringTokenizer  (  s  ,  d  )  ; 
if  (  t  .  countTokens  (  )  !=  4  )  { 
throw   new   Error  (  "only "  +  t  .  countTokens  (  )  +  " tokens in pot "  +  s  +  "; use , to separate tokens for name,sex,type,tooltip\nsex=n|p, type=normal|cascode"  )  ; 
} 
String   name  =  t  .  nextToken  (  )  ; 
String   a  ; 
a  =  t  .  nextToken  (  )  ; 
Sex   sex  =  null  ; 
if  (  a  .  equalsIgnoreCase  (  "n"  )  )  { 
sex  =  Sex  .  N  ; 
}  else   if  (  a  .  equalsIgnoreCase  (  "p"  )  )  { 
sex  =  Sex  .  P  ; 
}  else  { 
throw   new   ParseException  (  s  ,  s  .  lastIndexOf  (  a  )  )  ; 
} 
a  =  t  .  nextToken  (  )  ; 
Type   type  =  null  ; 
if  (  a  .  equalsIgnoreCase  (  "normal"  )  )  { 
type  =  Type  .  NORMAL  ; 
}  else   if  (  a  .  equalsIgnoreCase  (  "cascode"  )  )  { 
type  =  Type  .  CASCODE  ; 
}  else  { 
throw   new   ParseException  (  s  ,  s  .  lastIndexOf  (  a  )  )  ; 
} 
String   tip  =  t  .  nextToken  (  )  ; 
getPotArray  (  )  .  addPot  (  new   ConfigurableIPotCDVSTest  (  this  ,  name  ,  pos  ++  ,  type  ,  sex  ,  false  ,  true  ,  ConfigurableIPotCDVSTest  .  maxBitValue  /  2  ,  ConfigurableIPotCDVSTest  .  maxBuffeBitValue  ,  pos  ,  tip  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   Error  (  e  .  toString  (  )  )  ; 
} 
} 

@  Override 
public   void   loadPreferences  (  )  { 
super  .  loadPreferences  (  )  ; 
if  (  hasPreferencesList  !=  null  )  { 
for  (  HasPreference   hp  :  hasPreferencesList  )  { 
hp  .  loadPreference  (  )  ; 
} 
} 
if  (  ssBiases  !=  null  )  for  (  ShiftedSourceBias   ss  :  ssBiases  )  ss  .  loadPreferences  (  )  ; 
} 

@  Override 
public   void   storePreferences  (  )  { 
super  .  storePreferences  (  )  ; 
for  (  HasPreference   hp  :  hasPreferencesList  )  { 
hp  .  storePreference  (  )  ; 
} 
if  (  ssBiases  !=  null  )  for  (  ShiftedSourceBias   ss  :  ssBiases  )  ss  .  storePreferences  (  )  ; 
} 







@  Override 
public   JPanel   buildControlPanel  (  )  { 
JPanel   panel  =  new   JPanel  (  )  ; 
panel  .  setLayout  (  new   BorderLayout  (  )  )  ; 
JTabbedPane   pane  =  new   JTabbedPane  (  )  ; 
JPanel   combinedBiasShiftedSourcePanel  =  new   JPanel  (  )  ; 
combinedBiasShiftedSourcePanel  .  setLayout  (  new   BoxLayout  (  combinedBiasShiftedSourcePanel  ,  BoxLayout  .  Y_AXIS  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  super  .  buildControlPanel  (  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssn  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssp  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssnMid  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  sspMid  )  )  ; 
pane  .  addTab  (  "Biases"  ,  combinedBiasShiftedSourcePanel  )  ; 
pane  .  addTab  (  "Output control"  ,  new   cDVSTest10ControlPanel  (  cDVSTest10  .  this  )  )  ; 
panel  .  add  (  pane  ,  BorderLayout  .  CENTER  )  ; 
return   panel  ; 
} 


@  Override 
public   byte  [  ]  formatConfigurationBytes  (  Biasgen   biasgen  )  { 
ByteBuffer   bb  =  ByteBuffer  .  allocate  (  1000  )  ; 
byte  [  ]  biasBytes  =  super  .  formatConfigurationBytes  (  biasgen  )  ; 
byte  [  ]  configBytes  =  allMuxes  .  formatConfigurationBytes  (  )  ; 
bb  .  put  (  configBytes  )  ; 
bb  .  put  (  biasBytes  )  ; 
for  (  ShiftedSourceBias   ss  :  ssBiases  )  { 
bb  .  put  (  ss  .  getBinaryRepresentation  (  )  )  ; 
} 
byte  [  ]  allBytes  =  new   byte  [  bb  .  position  (  )  ]  ; 
bb  .  flip  (  )  ; 
bb  .  get  (  allBytes  )  ; 
return   allBytes  ; 
} 


public   final   float   RATIO  =  1.05f  ; 


public   final   float   MIN_THRESHOLD_RATIO  =  2f  ; 

public   final   float   MAX_DIFF_ON_CURRENT  =  6e-6f  ; 

public   final   float   MIN_DIFF_OFF_CURRENT  =  1e-9f  ; 

public   synchronized   void   increaseThreshold  (  )  { 
if  (  diffOn  .  getCurrent  (  )  *  RATIO  >  MAX_DIFF_ON_CURRENT  )  { 
return  ; 
} 
if  (  diffOff  .  getCurrent  (  )  /  RATIO  <  MIN_DIFF_OFF_CURRENT  )  { 
return  ; 
} 
diffOn  .  changeByRatio  (  RATIO  )  ; 
diffOff  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   decreaseThreshold  (  )  { 
float   diffI  =  diff  .  getCurrent  (  )  ; 
if  (  diffOn  .  getCurrent  (  )  /  MIN_THRESHOLD_RATIO  <  diffI  )  { 
return  ; 
} 
if  (  diffOff  .  getCurrent  (  )  >  diffI  /  MIN_THRESHOLD_RATIO  )  { 
return  ; 
} 
diffOff  .  changeByRatio  (  RATIO  )  ; 
diffOn  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   increaseRefractoryPeriod  (  )  { 
refr  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   decreaseRefractoryPeriod  (  )  { 
refr  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   increaseBandwidth  (  )  { 
pr  .  changeByRatio  (  RATIO  )  ; 
foll  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   decreaseBandwidth  (  )  { 
pr  .  changeByRatio  (  1  /  RATIO  )  ; 
foll  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   moreONType  (  )  { 
diffOn  .  changeByRatio  (  1  /  RATIO  )  ; 
diffOff  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   moreOFFType  (  )  { 
diffOn  .  changeByRatio  (  RATIO  )  ; 
diffOff  .  changeByRatio  (  1  /  RATIO  )  ; 
} 


class   OutputMux   extends   Observable   implements   HasPreference  ,  RemoteControlled  { 

int   nSrBits  ; 

int   nInputs  ; 

OutputMap   map  ; 

private   String   name  =  "OutputMux"  ; 

int   selectedChannel  =  -  1  ; 

String   bitString  =  null  ; 

final   String   CMD_SELECTMUX  =  "selectMux_"  ; 

OutputMux  (  int   nsr  ,  int   nin  ,  OutputMap   m  )  { 
nSrBits  =  nsr  ; 
nInputs  =  nin  ; 
map  =  m  ; 
hasPreferencesList  .  add  (  this  )  ; 
} 

public   String   toString  (  )  { 
return  "OutputMux name="  +  name  +  " nSrBits="  +  nSrBits  +  " nInputs="  +  nInputs  +  " selectedChannel="  +  selectedChannel  +  " channelName="  +  getChannelName  (  selectedChannel  )  +  " code="  +  getCode  (  selectedChannel  )  +  " getBitString="  +  bitString  ; 
} 

void   select  (  int   i  )  { 
selectWithoutNotify  (  i  )  ; 
setChanged  (  )  ; 
notifyObservers  (  )  ; 
} 

void   selectWithoutNotify  (  int   i  )  { 
selectedChannel  =  i  ; 
try  { 
sendConfiguration  (  cDVSTest10  .  cDVSTestBiasgen  .  this  )  ; 
}  catch  (  HardwareInterfaceException   ex  )  { 
log  .  warning  (  "selecting output: "  +  ex  )  ; 
} 
} 

void   put  (  int   k  ,  String   name  )  { 
map  .  put  (  k  ,  name  )  ; 
} 

OutputMap   getMap  (  )  { 
return   map  ; 
} 

int   getCode  (  int   i  )  { 
return   map  .  get  (  i  )  ; 
} 





String   getBitString  (  )  { 
StringBuilder   s  =  new   StringBuilder  (  )  ; 
int   code  =  selectedChannel  !=  -  1  ?  getCode  (  selectedChannel  )  :  0  ; 
int   k  =  nSrBits  -  1  ; 
while  (  k  >=  0  )  { 
int   x  =  code  &  (  1  <<  k  )  ; 
boolean   b  =  (  x  ==  0  )  ; 
s  .  append  (  b  ?  '0'  :  '1'  )  ; 
k  --  ; 
} 
bitString  =  s  .  toString  (  )  ; 
return   bitString  ; 
} 

String   getChannelName  (  int   i  )  { 
return   map  .  nameMap  .  get  (  i  )  ; 
} 

public   String   getName  (  )  { 
return   name  ; 
} 

public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 

private   String   key  (  )  { 
return  "cDVSTest."  +  getClass  (  )  .  getSimpleName  (  )  +  "."  +  name  +  ".selectedChannel"  ; 
} 

public   void   loadPreference  (  )  { 
select  (  getPrefs  (  )  .  getInt  (  key  (  )  ,  -  1  )  )  ; 
} 

public   void   storePreference  (  )  { 
getPrefs  (  )  .  putInt  (  key  (  )  ,  selectedChannel  )  ; 
} 







public   String   processRemoteControlCommand  (  RemoteControlCommand   command  ,  String   input  )  { 
String  [  ]  t  =  input  .  split  (  "\\s"  )  ; 
if  (  t  .  length  <  2  )  { 
return  "? "  +  this  +  "\n"  ; 
}  else  { 
String   s  =  t  [  0  ]  ,  a  =  t  [  1  ]  ; 
try  { 
select  (  Integer  .  parseInt  (  a  )  )  ; 
return   this  +  "\n"  ; 
}  catch  (  NumberFormatException   e  )  { 
log  .  warning  (  "Bad number format: "  +  input  +  " caused "  +  e  )  ; 
return   e  .  toString  (  )  +  "\n"  ; 
}  catch  (  Exception   ex  )  { 
log  .  warning  (  ex  .  toString  (  )  )  ; 
return   ex  .  toString  (  )  ; 
} 
} 
} 
} 

class   OutputMap   extends   HashMap  <  Integer  ,  Integer  >  { 

HashMap  <  Integer  ,  String  >  nameMap  =  new   HashMap  <  Integer  ,  String  >  (  )  ; 

void   put  (  int   k  ,  int   v  ,  String   name  )  { 
put  (  k  ,  v  )  ; 
nameMap  .  put  (  k  ,  name  )  ; 
} 

void   put  (  int   k  ,  String   name  )  { 
nameMap  .  put  (  k  ,  name  )  ; 
} 
} 

class   VoltageOutputMap   extends   OutputMap  { 

void   put  (  int   k  ,  int   v  )  { 
put  (  k  ,  v  ,  "Voltage "  +  k  )  ; 
} 

VoltageOutputMap  (  )  { 
put  (  0  ,  1  )  ; 
put  (  1  ,  3  )  ; 
put  (  2  ,  5  )  ; 
put  (  3  ,  7  )  ; 
put  (  4  ,  9  )  ; 
put  (  5  ,  11  )  ; 
put  (  6  ,  13  )  ; 
put  (  7  ,  15  )  ; 
} 
} 

class   DigitalOutputMap   extends   OutputMap  { 

DigitalOutputMap  (  )  { 
for  (  int   i  =  0  ;  i  <  16  ;  i  ++  )  { 
put  (  i  ,  i  ,  "DigOut "  +  i  )  ; 
} 
} 
} 

class   VoltageOutputMux   extends   OutputMux  { 

VoltageOutputMux  (  int   n  )  { 
super  (  4  ,  8  ,  new   VoltageOutputMap  (  )  )  ; 
setName  (  "Voltages"  +  n  )  ; 
} 
} 

class   LogicMux   extends   OutputMux  { 

LogicMux  (  int   n  )  { 
super  (  4  ,  16  ,  new   DigitalOutputMap  (  )  )  ; 
setName  (  "LogicSignals"  +  n  )  ; 
} 
} 

class   BiasOutputMux   extends   OutputMux  { 

BiasOutputMux  (  )  { 
super  (  4  ,  16  ,  new   DigitalOutputMap  (  )  )  ; 
setName  (  "Bias"  )  ; 
RemoteControl   rc  =  getRemoteControl  (  )  ; 
if  (  rc  !=  null  )  { 
rc  .  addCommandListener  (  this  ,  CMD_SELECTMUX  +  getName  (  )  +  " <channelNumber>"  ,  "Selects a multiplexer output"  )  ; 
} 
} 
} 

class   AllMuxes   extends   ArrayList  <  OutputMux  >  { 

OutputMux  [  ]  vmuxes  =  {  new   VoltageOutputMux  (  1  )  ,  new   VoltageOutputMux  (  2  )  ,  new   VoltageOutputMux  (  3  )  ,  new   VoltageOutputMux  (  4  )  }  ; 

OutputMux  [  ]  dmuxes  =  {  new   LogicMux  (  1  )  ,  new   LogicMux  (  2  )  ,  new   LogicMux  (  3  )  ,  new   LogicMux  (  4  )  ,  new   LogicMux  (  5  )  }  ; 

OutputMux   biasmux  =  new   BiasOutputMux  (  )  ; 

byte  [  ]  formatConfigurationBytes  (  )  { 
int   nBits  =  0  ; 
StringBuilder   s  =  new   StringBuilder  (  )  ; 
for  (  OutputMux   m  :  this  )  { 
s  .  append  (  m  .  getBitString  (  )  )  ; 
nBits  +=  m  .  nSrBits  ; 
} 
BigInteger   bi  =  new   BigInteger  (  s  .  toString  (  )  ,  2  )  ; 
byte  [  ]  byteArray  =  bi  .  toByteArray  (  )  ; 
int   nbytes  =  (  nBits  %  8  ==  0  )  ?  (  nBits  /  8  )  :  (  nBits  /  8  +  1  )  ; 
if  (  nbytes  <  byteArray  .  length  )  { 
nbytes  =  byteArray  .  length  ; 
} 
byte  [  ]  bytes  =  new   byte  [  nbytes  ]  ; 
System  .  arraycopy  (  byteArray  ,  0  ,  bytes  ,  nbytes  -  byteArray  .  length  ,  byteArray  .  length  )  ; 
return   bytes  ; 
} 

AllMuxes  (  )  { 
addAll  (  Arrays  .  asList  (  dmuxes  )  )  ; 
addAll  (  Arrays  .  asList  (  vmuxes  )  )  ; 
add  (  biasmux  )  ; 
biasmux  .  put  (  0  ,  "diffB"  )  ; 
biasmux  .  put  (  1  ,  "onB"  )  ; 
biasmux  .  put  (  2  ,  "offB"  )  ; 
biasmux  .  put  (  3  ,  "redB"  )  ; 
biasmux  .  put  (  4  ,  "blueB"  )  ; 
biasmux  .  put  (  5  ,  "AmpB"  )  ; 
biasmux  .  put  (  6  ,  "PcasB"  )  ; 
biasmux  .  put  (  7  ,  "pixInvB"  )  ; 
biasmux  .  put  (  8  ,  "prB"  )  ; 
biasmux  .  put  (  9  ,  "fB"  )  ; 
biasmux  .  put  (  10  ,  "refrB"  )  ; 
biasmux  .  put  (  11  ,  "pdBias"  )  ; 
biasmux  .  put  (  12  ,  "RxEB"  )  ; 
biasmux  .  put  (  13  ,  "puXB"  )  ; 
biasmux  .  put  (  14  ,  "puYB"  )  ; 
biasmux  .  put  (  15  ,  "VthB"  )  ; 
dmuxes  [  0  ]  .  setName  (  "DigMux4"  )  ; 
dmuxes  [  1  ]  .  setName  (  "DigMux3"  )  ; 
dmuxes  [  2  ]  .  setName  (  "DigMux2"  )  ; 
dmuxes  [  3  ]  .  setName  (  "DigMux1"  )  ; 
dmuxes  [  4  ]  .  setName  (  "DigMux0"  )  ; 
for  (  int   i  =  0  ;  i  <  5  ;  i  ++  )  { 
dmuxes  [  i  ]  .  put  (  0  ,  "RCarb"  )  ; 
dmuxes  [  i  ]  .  put  (  1  ,  "FF2"  )  ; 
dmuxes  [  i  ]  .  put  (  2  ,  "nArow"  )  ; 
dmuxes  [  i  ]  .  put  (  3  ,  "RxcolG"  )  ; 
dmuxes  [  i  ]  .  put  (  4  ,  "Rrow"  )  ; 
dmuxes  [  i  ]  .  put  (  5  ,  "Rcol"  )  ; 
dmuxes  [  i  ]  .  put  (  6  ,  "Acol"  )  ; 
dmuxes  [  i  ]  .  put  (  7  ,  "FF1"  )  ; 
dmuxes  [  i  ]  .  put  (  8  ,  "arbtopA"  )  ; 
dmuxes  [  i  ]  .  put  (  9  ,  "arbtopR"  )  ; 
dmuxes  [  i  ]  .  put  (  10  ,  "nRXon"  )  ; 
dmuxes  [  i  ]  .  put  (  11  ,  "nAX0"  )  ; 
dmuxes  [  i  ]  .  put  (  12  ,  "AY0"  )  ; 
dmuxes  [  i  ]  .  put  (  13  ,  "nRY0"  )  ; 
dmuxes  [  i  ]  .  put  (  14  ,  "nAxcolE"  )  ; 
dmuxes  [  i  ]  .  put  (  15  ,  "nRxcolE"  )  ; 
} 
vmuxes  [  0  ]  .  setName  (  "AnaMux3"  )  ; 
vmuxes  [  1  ]  .  setName  (  "AnaMux2"  )  ; 
vmuxes  [  2  ]  .  setName  (  "AnaMux1"  )  ; 
vmuxes  [  3  ]  .  setName  (  "AnaMux0"  )  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
vmuxes  [  i  ]  .  put  (  0  ,  "Vsb"  )  ; 
vmuxes  [  i  ]  .  put  (  1  ,  "Vt"  )  ; 
vmuxes  [  i  ]  .  put  (  2  ,  "Vtb"  )  ; 
vmuxes  [  i  ]  .  put  (  3  ,  "Vs"  )  ; 
} 
vmuxes  [  0  ]  .  put  (  4  ,  "Vdiff"  )  ; 
vmuxes  [  0  ]  .  put  (  5  ,  "top"  )  ; 
vmuxes  [  0  ]  .  put  (  6  ,  "nResetColor2"  )  ; 
vmuxes  [  0  ]  .  put  (  7  ,  "topin"  )  ; 
vmuxes  [  1  ]  .  put  (  4  ,  "Vdiff"  )  ; 
vmuxes  [  1  ]  .  put  (  5  ,  "top"  )  ; 
vmuxes  [  1  ]  .  put  (  6  ,  "nResetColor2"  )  ; 
vmuxes  [  1  ]  .  put  (  7  ,  "topin"  )  ; 
vmuxes  [  2  ]  .  put  (  4  ,  "nResetColor1"  )  ; 
vmuxes  [  2  ]  .  put  (  5  ,  "Vcoldiff"  )  ; 
vmuxes  [  2  ]  .  put  (  6  ,  "sum"  )  ; 
vmuxes  [  2  ]  .  put  (  7  ,  "sumin"  )  ; 
vmuxes  [  3  ]  .  put  (  4  ,  "nResetColor1"  )  ; 
vmuxes  [  3  ]  .  put  (  5  ,  "Vcoldiff"  )  ; 
vmuxes  [  3  ]  .  put  (  6  ,  "sum"  )  ; 
vmuxes  [  3  ]  .  put  (  7  ,  "sumin"  )  ; 
} 
} 
} 
} 

