package   org  .  monome  .  pages  ; 

import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  util  .  ArrayList  ; 
import   javax  .  sound  .  midi  .  MidiDevice  ; 
import   javax  .  sound  .  midi  .  MidiMessage  ; 
import   javax  .  sound  .  midi  .  ShortMessage  ; 
import   javax  .  sound  .  midi  .  MidiUnavailableException  ; 
import   javax  .  sound  .  midi  .  Receiver  ; 
import   javax  .  sound  .  midi  .  Transmitter  ; 
import   org  .  apache  .  commons  .  lang  .  StringEscapeUtils  ; 
import   com  .  illposed  .  osc  .  OSCMessage  ; 
import   com  .  illposed  .  osc  .  OSCPortIn  ; 
import   com  .  illposed  .  osc  .  OSCPortOut  ; 









public   class   Configuration   implements   Receiver  { 




private   String   name  ; 




private   int   numMonomeConfigurations  =  0  ; 




private   ArrayList  <  MonomeConfiguration  >  monomeConfigurations  =  new   ArrayList  <  MonomeConfiguration  >  (  )  ; 




private   ArrayList  <  MidiDevice  >  midiInDevices  =  new   ArrayList  <  MidiDevice  >  (  )  ; 




private   ArrayList  <  Transmitter  >  midiInTransmitters  =  new   ArrayList  <  Transmitter  >  (  )  ; 




private   ArrayList  <  MidiDevice  >  midiOutDevices  =  new   ArrayList  <  MidiDevice  >  (  )  ; 




private   ArrayList  <  Receiver  >  midiOutReceivers  =  new   ArrayList  <  Receiver  >  (  )  ; 




private   int   monomeSerialOSCInPortNumber  =  8000  ; 




public   OSCPortIn   monomeSerialOSCPortIn  ; 




private   int   monomeSerialOSCOutPortNumber  =  8080  ; 




public   OSCPortOut   monomeSerialOSCPortOut  ; 




private   String   monomeHostname  =  "localhost"  ; 




private   int   abletonOSCInPortNumber  =  9001  ; 




private   OSCPortIn   abletonOSCPortIn  ; 




private   int   abletonOSCOutPortNumber  =  9000  ; 





private   AbletonOSCClipUpdater   abletonOSCClipUpdater  ; 





private   AbletonOSCListener   abletonOSCListener  ; 




private   OSCPortOut   abletonOSCPortOut  ; 




private   String   abletonHostname  =  "localhost"  ; 

private   Receiver   abletonReceiver  ; 

private   Transmitter   abletonTransmitter  ; 

private   AbletonSysexReceiver   abletonSysexReceiver  =  new   AbletonSysexReceiver  (  this  )  ; 

private   String   abletonMode  =  "OSC"  ; 

private   AbletonMIDIClipUpdater   abletonMIDIClipUpdater  ; 

private   String   abletonMIDIInDeviceName  ; 

private   String   abletonMIDIOutDeviceName  ; 

private   AbletonControl   abletonControl  ; 

public   AbletonState   abletonState  ; 

private   int   abletonOSCUpdateDelay  =  100  ; 

private   int   abletonMIDIUpdateDelay  =  100  ; 

private   boolean   abletonInitialized  =  false  ; 




public   Configuration  (  String   name  )  { 
this  .  name  =  name  ; 
this  .  abletonState  =  new   AbletonState  (  )  ; 
} 





public   Receiver   getMidiReceiver  (  int   index  )  { 
return   this  .  midiOutReceivers  .  get  (  index  )  ; 
} 




public   ArrayList  <  MidiDevice  >  getMidiInDevices  (  )  { 
return   this  .  midiInDevices  ; 
} 




public   ArrayList  <  MidiDevice  >  getMidiOutDevices  (  )  { 
return   this  .  midiOutDevices  ; 
} 









public   int   addMonomeConfiguration  (  String   prefix  ,  int   sizeX  ,  int   sizeY  ,  boolean   usePageChangeButton  ,  boolean   useMIDIPageChanging  ,  ArrayList  <  MIDIPageChangeRule  >  midiPageChangeRules  )  { 
MonomeConfiguration   monome  =  new   MonomeConfiguration  (  this  ,  this  .  numMonomeConfigurations  ,  prefix  ,  sizeX  ,  sizeY  ,  usePageChangeButton  ,  useMIDIPageChanging  ,  midiPageChangeRules  )  ; 
this  .  monomeConfigurations  .  add  (  this  .  numMonomeConfigurations  ,  monome  )  ; 
this  .  initMonome  (  monome  )  ; 
this  .  numMonomeConfigurations  ++  ; 
return   this  .  numMonomeConfigurations  -  1  ; 
} 




public   void   closeMonomeConfigurationWindows  (  )  { 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
monomeConfigurations  .  get  (  i  )  .  dispose  (  )  ; 
} 
} 




public   void   stopMonomeSerialOSC  (  )  { 
if  (  this  .  monomeSerialOSCPortIn  !=  null  )  { 
if  (  this  .  monomeSerialOSCPortIn  .  isListening  (  )  )  { 
this  .  monomeSerialOSCPortIn  .  stopListening  (  )  ; 
} 
this  .  monomeSerialOSCPortIn  .  close  (  )  ; 
} 
if  (  this  .  monomeSerialOSCPortOut  !=  null  )  { 
this  .  monomeSerialOSCPortOut  .  close  (  )  ; 
} 
} 




public   void   stopAbleton  (  )  { 
if  (  this  .  abletonOSCPortIn  !=  null  )  { 
if  (  this  .  abletonOSCPortIn  .  isListening  (  )  )  { 
this  .  abletonOSCPortIn  .  stopListening  (  )  ; 
} 
this  .  abletonOSCPortIn  .  close  (  )  ; 
} 
if  (  this  .  abletonOSCPortOut  !=  null  )  { 
this  .  abletonOSCPortOut  .  close  (  )  ; 
} 
this  .  abletonControl  =  null  ; 
this  .  stopAbletonClipUpdaters  (  )  ; 
} 




public   void   destroyAllPages  (  )  { 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
this  .  monomeConfigurations  .  get  (  i  )  .  destroyPage  (  )  ; 
} 
} 





public   MonomeConfiguration   getMonomeConfigurationFrame  (  int   index  )  { 
return   monomeConfigurations  .  get  (  index  )  ; 
} 






public   void   closeMonome  (  int   index  )  { 
this  .  monomeConfigurations  .  get  (  index  )  .  dispose  (  )  ; 
} 




public   void   closeMidiDevices  (  )  { 
for  (  int   i  =  0  ;  i  <  this  .  midiInTransmitters  .  size  (  )  ;  i  ++  )  { 
this  .  midiInTransmitters  .  get  (  i  )  .  close  (  )  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  midiInDevices  .  size  (  )  ;  i  ++  )  { 
this  .  midiInDevices  .  get  (  i  )  .  close  (  )  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  midiOutDevices  .  size  (  )  ;  i  ++  )  { 
this  .  midiOutDevices  .  get  (  i  )  .  close  (  )  ; 
} 
} 






public   void   toggleMidiOutDevice  (  MidiDevice   midiOutDevice  )  { 
for  (  int   i  =  0  ;  i  <  this  .  midiOutDevices  .  size  (  )  ;  i  ++  )  { 
if  (  this  .  midiOutDevices  .  get  (  i  )  .  equals  (  midiOutDevice  )  )  { 
System  .  out  .  println  (  "closing midi out device "  +  i  +  " / "  +  this  .  midiOutDevices  .  get  (  i  )  .  getDeviceInfo  (  )  )  ; 
MidiDevice   outDevice  =  this  .  midiOutDevices  .  get  (  i  )  ; 
this  .  midiOutReceivers  .  remove  (  i  )  ; 
this  .  midiOutDevices  .  remove  (  i  )  ; 
outDevice  .  close  (  )  ; 
outDevice  .  close  (  )  ; 
return  ; 
} 
} 
try  { 
midiOutDevice  .  open  (  )  ; 
Receiver   recv  =  midiOutDevice  .  getReceiver  (  )  ; 
this  .  midiOutDevices  .  add  (  midiOutDevice  )  ; 
this  .  midiOutReceivers  .  add  (  recv  )  ; 
}  catch  (  MidiUnavailableException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 






public   void   toggleMidiInDevice  (  MidiDevice   midiInDevice  )  { 
for  (  int   i  =  0  ;  i  <  this  .  midiInDevices  .  size  (  )  ;  i  ++  )  { 
if  (  this  .  midiInDevices  .  get  (  i  )  .  equals  (  midiInDevice  )  )  { 
MidiDevice   inDevice  =  this  .  midiInDevices  .  get  (  i  )  ; 
System  .  out  .  println  (  "closing midi in device "  +  i  +  " / "  +  this  .  midiInDevices  .  get  (  i  )  .  getDeviceInfo  (  )  )  ; 
Transmitter   transmitter  =  this  .  midiInTransmitters  .  get  (  i  )  ; 
this  .  midiInTransmitters  .  remove  (  i  )  ; 
this  .  midiInDevices  .  remove  (  i  )  ; 
transmitter  .  close  (  )  ; 
inDevice  .  close  (  )  ; 
return  ; 
} 
} 
try  { 
midiInDevice  .  open  (  )  ; 
Transmitter   transmitter  =  midiInDevice  .  getTransmitter  (  )  ; 
transmitter  .  setReceiver  (  this  )  ; 
this  .  midiInDevices  .  add  (  midiInDevice  )  ; 
this  .  midiInTransmitters  .  add  (  transmitter  )  ; 
}  catch  (  MidiUnavailableException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   send  (  MidiMessage   message  ,  long   lTimeStamp  )  { 
ShortMessage   shortMessage  ; 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
this  .  monomeConfigurations  .  get  (  i  )  .  send  (  message  ,  lTimeStamp  )  ; 
} 
if  (  message   instanceof   ShortMessage  )  { 
shortMessage  =  (  ShortMessage  )  message  ; 
switch  (  shortMessage  .  getCommand  (  )  )  { 
case   0xF0  : 
if  (  shortMessage  .  getChannel  (  )  ==  8  )  { 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
this  .  monomeConfigurations  .  get  (  i  )  .  tick  (  )  ; 
} 
} 
if  (  shortMessage  .  getChannel  (  )  ==  0x0C  )  { 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
this  .  monomeConfigurations  .  get  (  i  )  .  reset  (  )  ; 
} 
} 
break  ; 
default  : 
break  ; 
} 
} 
} 

public   void   close  (  )  { 
return  ; 
} 




public   void   setMonomeSerialOSCInPortNumber  (  int   inport  )  { 
this  .  monomeSerialOSCInPortNumber  =  inport  ; 
} 




public   int   getMonomeSerialOSCInPortNumber  (  )  { 
return   this  .  monomeSerialOSCInPortNumber  ; 
} 




public   void   setMonomeSerialOSCOutPortNumber  (  int   outport  )  { 
this  .  monomeSerialOSCOutPortNumber  =  outport  ; 
} 




public   int   getMonomeSerialOSCOutPortNumber  (  )  { 
return   this  .  monomeSerialOSCOutPortNumber  ; 
} 




public   void   setAbletonOSCInPortNumber  (  int   inport  )  { 
this  .  abletonOSCInPortNumber  =  inport  ; 
} 




public   int   getAbletonOSCInPortNumber  (  )  { 
return   this  .  abletonOSCInPortNumber  ; 
} 




public   void   setAbletonOSCOutPortNumber  (  int   outport  )  { 
this  .  abletonOSCOutPortNumber  =  outport  ; 
} 




public   int   getAbletonOSCOutPortNumber  (  )  { 
return   this  .  abletonOSCOutPortNumber  ; 
} 




public   OSCPortOut   getAbletonOSCPortOut  (  )  { 
return   this  .  abletonOSCPortOut  ; 
} 




public   void   setAbletonHostname  (  String   hostname  )  { 
this  .  abletonHostname  =  hostname  ; 
} 




public   String   getAbletonHostname  (  )  { 
return   this  .  abletonHostname  ; 
} 




public   void   setMonomeHostname  (  String   hostname  )  { 
this  .  monomeHostname  =  hostname  ; 
} 




public   String   getMonomeHostname  (  )  { 
return   this  .  monomeHostname  ; 
} 







private   boolean   initMonome  (  MonomeConfiguration   monome  )  { 
try  { 
MonomeOSCListener   oscListener  =  new   MonomeOSCListener  (  monome  )  ; 
if  (  this  .  monomeSerialOSCPortIn  ==  null  )  { 
this  .  monomeSerialOSCPortIn  =  new   OSCPortIn  (  this  .  monomeSerialOSCInPortNumber  )  ; 
} 
if  (  this  .  monomeSerialOSCPortOut  ==  null  )  { 
this  .  monomeSerialOSCPortOut  =  new   OSCPortOut  (  InetAddress  .  getByName  (  this  .  monomeHostname  )  ,  this  .  monomeSerialOSCOutPortNumber  )  ; 
} 
Object   args  [  ]  =  new   Object  [  1  ]  ; 
args  [  0  ]  =  new   Integer  (  1  )  ; 
OSCMessage   msg  =  new   OSCMessage  (  monome  .  prefix  +  "/tiltmode"  ,  args  )  ; 
try  { 
this  .  monomeSerialOSCPortOut  .  send  (  msg  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
this  .  monomeSerialOSCPortIn  .  addListener  (  monome  .  prefix  +  "/press"  ,  oscListener  )  ; 
this  .  monomeSerialOSCPortIn  .  addListener  (  monome  .  prefix  +  "/adc"  ,  oscListener  )  ; 
this  .  monomeSerialOSCPortIn  .  addListener  (  monome  .  prefix  +  "/tilt"  ,  oscListener  )  ; 
this  .  monomeSerialOSCPortIn  .  startListening  (  )  ; 
monome  .  clearMonome  (  )  ; 
}  catch  (  SocketException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
}  catch  (  UnknownHostException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
return   true  ; 
} 






public   void   initAbleton  (  )  { 
if  (  !  abletonInitialized  )  { 
this  .  stopAbletonClipUpdaters  (  )  ; 
if  (  this  .  abletonMode  .  equals  (  "OSC"  )  )  { 
this  .  initAbletonOSCMode  (  )  ; 
}  else   if  (  this  .  abletonMode  .  equals  (  "MIDI"  )  )  { 
this  .  initAbletonMIDIMode  (  )  ; 
} 
abletonInitialized  =  true  ; 
try  { 
Thread  .  sleep  (  500  )  ; 
}  catch  (  InterruptedException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
this  .  abletonControl  .  refreshAbleton  (  )  ; 
} 
} 

public   void   initAbletonMIDIMode  (  )  { 
this  .  initAbletonMIDIInPort  (  this  .  abletonMIDIInDeviceName  )  ; 
this  .  initAbletonMIDIOutPort  (  this  .  abletonMIDIOutDeviceName  )  ; 
this  .  initAbletonMIDIClipUpdater  (  )  ; 
this  .  abletonControl  =  new   AbletonMIDIControl  (  this  .  abletonReceiver  )  ; 
} 

public   void   initAbletonMIDIClipUpdater  (  )  { 
this  .  abletonMIDIClipUpdater  =  new   AbletonMIDIClipUpdater  (  this  ,  this  .  abletonReceiver  )  ; 
new   Thread  (  this  .  abletonMIDIClipUpdater  )  .  start  (  )  ; 
} 

public   void   initAbletonOSCMode  (  )  { 
this  .  abletonOSCListener  =  new   AbletonOSCListener  (  this  )  ; 
this  .  initAbletonOSCOut  (  )  ; 
this  .  initAbletonOSCIn  (  )  ; 
this  .  initAbletonOSCClipUpdater  (  )  ; 
this  .  abletonControl  =  new   AbletonOSCControl  (  this  )  ; 
} 

public   void   initAbletonOSCOut  (  )  { 
try  { 
this  .  abletonOSCPortOut  =  new   OSCPortOut  (  InetAddress  .  getByName  (  this  .  abletonHostname  )  ,  this  .  abletonOSCOutPortNumber  )  ; 
}  catch  (  SocketException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  UnknownHostException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   initAbletonOSCIn  (  )  { 
try  { 
if  (  this  .  abletonOSCPortIn  !=  null  )  { 
this  .  abletonOSCPortIn  .  stopListening  (  )  ; 
this  .  abletonOSCPortIn  .  close  (  )  ; 
} 
this  .  abletonOSCPortIn  =  new   OSCPortIn  (  this  .  abletonOSCInPortNumber  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/track/info"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/clip/info"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/state"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/mute"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/arm"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/solo"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/scene"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/tempo"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/overdub"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/refresh"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  addListener  (  "/live/reset"  ,  this  .  abletonOSCListener  )  ; 
this  .  abletonOSCPortIn  .  startListening  (  )  ; 
}  catch  (  SocketException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   initAbletonOSCClipUpdater  (  )  { 
this  .  abletonOSCClipUpdater  =  new   AbletonOSCClipUpdater  (  this  ,  this  .  abletonOSCPortOut  )  ; 
new   Thread  (  this  .  abletonOSCClipUpdater  )  .  start  (  )  ; 
} 

public   void   stopAbletonClipUpdaters  (  )  { 
if  (  this  .  abletonMIDIClipUpdater  !=  null  )  { 
this  .  abletonMIDIClipUpdater  .  stop  (  )  ; 
this  .  abletonMIDIClipUpdater  =  null  ; 
} 
if  (  this  .  abletonOSCClipUpdater  !=  null  )  { 
this  .  abletonOSCClipUpdater  .  stop  (  )  ; 
this  .  abletonOSCClipUpdater  =  null  ; 
} 
} 






public   String   toXml  (  )  { 
String   xml  ; 
xml  =  "<configuration>\n"  ; 
xml  +=  "  <name>"  +  this  .  name  +  "</name>\n"  ; 
xml  +=  "  <hostname>"  +  this  .  monomeHostname  +  "</hostname>\n"  ; 
xml  +=  "  <oscinport>"  +  this  .  monomeSerialOSCInPortNumber  +  "</oscinport>\n"  ; 
xml  +=  "  <oscoutport>"  +  this  .  monomeSerialOSCOutPortNumber  +  "</oscoutport>\n"  ; 
xml  +=  "  <abletonmode>"  +  this  .  abletonMode  +  "</abletonmode>\n"  ; 
if  (  this  .  abletonMode  .  equals  (  "OSC"  )  )  { 
xml  +=  "  <abletonhostname>"  +  this  .  abletonHostname  +  "</abletonhostname>\n"  ; 
xml  +=  "  <abletonoscinport>"  +  this  .  abletonOSCInPortNumber  +  "</abletonoscinport>\n"  ; 
xml  +=  "  <abletonoscoutport>"  +  this  .  abletonOSCOutPortNumber  +  "</abletonoscoutport>\n"  ; 
xml  +=  "  <abletonoscupdatedelay>"  +  this  .  abletonOSCUpdateDelay  +  "</abletonoscupdatedelay>\n"  ; 
}  else   if  (  this  .  abletonMode  .  equals  (  "MIDI"  )  )  { 
xml  +=  "  <abletonmidiinport>"  +  StringEscapeUtils  .  escapeXml  (  this  .  abletonMIDIInDeviceName  )  +  "</abletonmidiinport>\n"  ; 
xml  +=  "  <abletonmidioutport>"  +  StringEscapeUtils  .  escapeXml  (  this  .  abletonMIDIOutDeviceName  )  +  "</abletonmidioutport>\n"  ; 
xml  +=  "  <abletonmidiupdatedelay>"  +  this  .  abletonMIDIUpdateDelay  +  "</abletonmidiupdatedelay>\n"  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  midiInDevices  .  size  (  )  ;  i  ++  )  { 
xml  +=  "  <midiinport>"  +  StringEscapeUtils  .  escapeXml  (  this  .  midiInDevices  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  )  +  "</midiinport>\n"  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  midiOutDevices  .  size  (  )  ;  i  ++  )  { 
xml  +=  "  <midioutport>"  +  StringEscapeUtils  .  escapeXml  (  this  .  midiOutDevices  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  )  +  "</midioutport>\n"  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
xml  +=  this  .  monomeConfigurations  .  get  (  i  )  .  toXml  (  )  ; 
} 
xml  +=  "</configuration>\n"  ; 
return   xml  ; 
} 

public   void   redrawAbletonPages  (  )  { 
for  (  int   i  =  0  ;  i  <  this  .  numMonomeConfigurations  ;  i  ++  )  { 
monomeConfigurations  .  get  (  i  )  .  redrawAbletonPages  (  )  ; 
} 
} 




public   String  [  ]  getMidiOutOptions  (  )  { 
ArrayList  <  MidiDevice  >  midiOuts  =  this  .  getMidiOutDevices  (  )  ; 
String  [  ]  midiOutOptions  =  new   String  [  midiOuts  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  midiOuts  .  size  (  )  ;  i  ++  )  { 
midiOutOptions  [  i  ]  =  midiOuts  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  ; 
} 
return   midiOutOptions  ; 
} 




public   String  [  ]  getMidiInOptions  (  )  { 
ArrayList  <  MidiDevice  >  midiIns  =  this  .  getMidiInDevices  (  )  ; 
String  [  ]  midiOutOptions  =  new   String  [  midiIns  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  midiIns  .  size  (  )  ;  i  ++  )  { 
midiOutOptions  [  i  ]  =  midiIns  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  ; 
} 
return   midiOutOptions  ; 
} 

public   void   initAbletonMIDIInPort  (  String   midiInDevice  )  { 
this  .  abletonTransmitter  =  this  .  getMIDITransmitterByName  (  midiInDevice  )  ; 
if  (  this  .  abletonTransmitter  !=  null  )  { 
this  .  abletonTransmitter  .  setReceiver  (  this  .  abletonSysexReceiver  )  ; 
} 
} 

public   void   initAbletonMIDIOutPort  (  String   midiOutDevice  )  { 
this  .  abletonReceiver  =  this  .  getMIDIReceiverByName  (  midiOutDevice  )  ; 
} 

public   void   setAbletonMode  (  String   abletonMode  )  { 
this  .  abletonMode  =  abletonMode  ; 
} 

public   Receiver   getMIDIReceiverByName  (  String   midiDeviceName  )  { 
for  (  int   i  =  0  ;  i  <  this  .  midiOutDevices  .  size  (  )  ;  i  ++  )  { 
if  (  this  .  midiOutDevices  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  .  compareTo  (  midiDeviceName  )  ==  0  )  { 
Receiver   receiver  =  this  .  midiOutReceivers  .  get  (  i  )  ; 
return   receiver  ; 
} 
} 
return   null  ; 
} 

public   Transmitter   getMIDITransmitterByName  (  String   midiDeviceName  )  { 
for  (  int   i  =  0  ;  i  <  this  .  midiInDevices  .  size  (  )  ;  i  ++  )  { 
if  (  this  .  midiInDevices  .  get  (  i  )  .  getDeviceInfo  (  )  .  toString  (  )  .  compareTo  (  midiDeviceName  )  ==  0  )  { 
Transmitter   transmitter  =  this  .  midiInTransmitters  .  get  (  i  )  ; 
return   transmitter  ; 
} 
} 
return   null  ; 
} 

public   String   getAbletonMIDIInDeviceName  (  )  { 
return   this  .  abletonMIDIInDeviceName  ; 
} 

public   void   setAbletonMIDIInDeviceName  (  String   midiInDevice  )  { 
this  .  abletonMIDIInDeviceName  =  midiInDevice  ; 
} 

public   String   getAbletonMIDIOutDeviceName  (  )  { 
return   this  .  abletonMIDIOutDeviceName  ; 
} 

public   void   setAbletonMIDIOutDeviceName  (  String   midiOutDevice  )  { 
this  .  abletonMIDIOutDeviceName  =  midiOutDevice  ; 
} 

public   String   getAbletonMode  (  )  { 
return   this  .  abletonMode  ; 
} 

public   AbletonControl   getAbletonControl  (  )  { 
return   this  .  abletonControl  ; 
} 

public   int   getAbletonOSCUpdateDelay  (  )  { 
return   this  .  abletonOSCUpdateDelay  ; 
} 

public   void   setAbletonOSCUpdateDelay  (  int   oscUpdateDelay  )  { 
this  .  abletonOSCUpdateDelay  =  oscUpdateDelay  ; 
} 

public   int   getAbletonMIDIUpdateDelay  (  )  { 
return   this  .  abletonMIDIUpdateDelay  ; 
} 

public   void   setAbletonMIDIUpdateDelay  (  int   midiUpdateDelay  )  { 
this  .  abletonMIDIUpdateDelay  =  midiUpdateDelay  ; 
} 
} 

