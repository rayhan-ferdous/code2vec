package   org  .  cdp1802  .  upb  .  impl  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   static   org  .  cdp1802  .  upb  .  UPBConstants  .  *  ; 
import   org  .  cdp1802  .  upb  .  UPBDeviceEvent  ; 
import   org  .  cdp1802  .  upb  .  UPBDeviceI  ; 
import   org  .  cdp1802  .  upb  .  UPBDeviceListenerI  ; 
import   org  .  cdp1802  .  upb  .  UPBLinkI  ; 
import   org  .  cdp1802  .  upb  .  UPBLinkDevice  ; 
import   org  .  cdp1802  .  upb  .  UPBManager  ; 
import   org  .  cdp1802  .  upb  .  UPBMessage  ; 
import   org  .  cdp1802  .  upb  .  UPBMsgType  ; 
import   org  .  cdp1802  .  upb  .  UPBNetworkI  ; 
import   org  .  cdp1802  .  upb  .  UPBProductI  ; 
import   org  .  cdp1802  .  upb  .  UPBRoomI  ; 























public   class   UPBDevice   implements   UPBDeviceI  { 

UPBNetworkI   deviceNetwork  =  null  ; 

int   deviceID  =  0  ; 

int   upbOptions  =  0  ; 

int   upbVersion  =  0  ; 

UPBProductI   upbProduct  =  null  ; 

int   firmwareVersion  =  0  ; 

int   serialNumber  =  0  ; 

int   channelCount  =  ALL_CHANNELS  ; 

int   primaryChannel  =  INVALID_CHANNEL  ; 

int   receiveComponentCount  =  0  ; 

boolean   transmitsLinks  =  false  ; 

boolean   sendsStateReports  =  true  ; 

boolean   supportsFading  =  false  ; 

String   deviceName  =  ""  ; 

UPBRoomI   deviceRoom  =  null  ; 

ArrayList  <  UPBLinkDevice  >  deviceLinks  =  new   ArrayList  <  UPBLinkDevice  >  (  )  ; 

boolean   isDimmable  [  ]  =  null  ; 

int   deviceState  [  ]  =  null  ; 

boolean   deviceStateValid  =  false  ; 

UPBDevice  (  )  { 
} 

UPBDevice  (  UPBNetworkI   theNetwork  ,  UPBProductI   theProduct  ,  int   deviceID  )  { 
setDeviceInfo  (  theNetwork  ,  theProduct  ,  deviceID  )  ; 
} 

public   void   setDeviceInfo  (  UPBNetworkI   theNetwork  ,  UPBProductI   theProduct  ,  int   deviceID  )  { 
this  .  deviceNetwork  =  theNetwork  ; 
this  .  upbProduct  =  theProduct  ; 
this  .  deviceID  =  deviceID  ; 
channelCount  =  upbProduct  .  getChannelCount  (  )  ; 
primaryChannel  =  upbProduct  .  getPrimaryChannel  (  )  ; 
receiveComponentCount  =  upbProduct  .  getReceiveComponentCount  (  )  ; 
deviceState  =  new   int  [  (  getChannelCount  (  )  <  1  )  ?  1  :  getChannelCount  (  )  ]  ; 
isDimmable  =  new   boolean  [  (  getChannelCount  (  )  <  1  )  ?  1  :  getChannelCount  (  )  ]  ; 
for  (  int   chanIndex  =  0  ;  chanIndex  <  deviceState  .  length  ;  chanIndex  ++  )  { 
deviceState  [  chanIndex  ]  =  UNASSIGNED_DEVICE_STATE  ; 
isDimmable  [  chanIndex  ]  =  upbProduct  .  isDimmingCapable  (  )  ; 
} 
} 

void   debug  (  String   theMessage  )  { 
deviceNetwork  .  getUPBManager  (  )  .  upbDebug  (  "DEVICE["  +  deviceID  +  "]:: "  +  theMessage  )  ; 
} 

void   error  (  String   theMessage  )  { 
deviceNetwork  .  getUPBManager  (  )  .  upbError  (  "DEVICE["  +  deviceID  +  "]:: "  +  theMessage  )  ; 
} 







public   int   getNetworkID  (  )  { 
return   deviceNetwork  .  getNetworkID  (  )  ; 
} 







public   int   getDeviceID  (  )  { 
return   deviceID  ; 
} 







public   UPBProductI   getProductInfo  (  )  { 
return   upbProduct  ; 
} 






public   int   getChannelCount  (  )  { 
return   channelCount  ; 
} 

public   void   setChannelCount  (  int   theCount  )  { 
channelCount  =  theCount  ; 
} 






public   int   getPrimaryChannel  (  )  { 
return   primaryChannel  ; 
} 






public   int   getReceiveComponentCount  (  )  { 
return   receiveComponentCount  ; 
} 

public   void   setReceiveComponentCount  (  int   theCount  )  { 
receiveComponentCount  =  theCount  ; 
} 











public   int   getFirmwareVersion  (  )  { 
return   firmwareVersion  ; 
} 

public   void   setFirmwareVersion  (  int   version  )  { 
firmwareVersion  =  version  ; 
} 









public   int   getSerialNumber  (  )  { 
return   serialNumber  ; 
} 







public   int   getUPBOptions  (  )  { 
return   upbOptions  ; 
} 







public   int   getUPBVersion  (  )  { 
return   upbVersion  ; 
} 









public   boolean   doesTransmitsLinks  (  )  { 
return   transmitsLinks  ; 
} 

public   void   setTransmitsLinks  (  boolean   transmitsLinks  )  { 
this  .  transmitsLinks  =  transmitsLinks  ; 
} 















public   boolean   isDimmable  (  int   theChannel  )  { 
return   upbProduct  .  isDimmingCapable  (  )  &&  isDimmable  [  theChannel  -  1  ]  ; 
} 

public   void   setDimmable  (  boolean   dimmable  ,  int   theChannel  )  { 
isDimmable  [  theChannel  -  1  ]  =  dimmable  ; 
} 














public   boolean   isDimmable  (  )  { 
return   isDimmable  (  upbProduct  .  getPrimaryChannel  (  )  )  ; 
} 












public   boolean   doesSendStateReports  (  )  { 
return   sendsStateReports  ; 
} 

public   boolean   isStatusQueryable  (  )  { 
return   upbProduct  .  isStatusQueryable  (  )  ; 
} 






public   String   getDeviceName  (  )  { 
return   deviceName  ; 
} 

public   void   setDeviceName  (  String   newName  )  { 
if  (  (  newName  ==  null  )  ||  (  newName  .  length  (  )  ==  0  )  ||  newName  .  equals  (  deviceName  )  )  return  ; 
deviceName  =  newName  ; 
deviceNetwork  .  getUPBManager  (  )  .  fireDeviceEvent  (  this  ,  UPBDeviceEvent  .  EventCode  .  DEVICE_ID_CHANGED  ,  ALL_CHANNELS  )  ; 
} 






public   UPBRoomI   getRoom  (  )  { 
return   deviceRoom  ; 
} 

public   void   setRoom  (  UPBRoomI   theRoom  )  { 
if  (  (  theRoom  ==  null  )  ||  (  theRoom  ==  deviceRoom  )  )  return  ; 
if  (  deviceRoom  !=  null  )  deviceRoom  .  removeDevice  (  this  )  ; 
deviceRoom  =  theRoom  ; 
if  (  deviceRoom  !=  null  )  deviceRoom  .  addDevice  (  this  )  ; 
deviceNetwork  .  getUPBManager  (  )  .  fireDeviceEvent  (  this  ,  UPBDeviceEvent  .  EventCode  .  DEVICE_ID_CHANGED  ,  ALL_CHANNELS  )  ; 
} 








public   void   addListener  (  UPBDeviceListenerI   theListener  )  { 
deviceNetwork  .  getUPBManager  (  )  .  addDeviceListener  (  theListener  ,  this  )  ; 
} 








public   void   removeListener  (  UPBDeviceListenerI   theListener  )  { 
deviceNetwork  .  getUPBManager  (  )  .  removeDeviceListener  (  theListener  ,  this  )  ; 
} 







public   void   requestStateFromDevice  (  )  { 
deviceNetwork  .  getUPBManager  (  )  .  queueStateRequest  (  this  )  ; 
} 







public   boolean   isStateRequestPending  (  )  { 
return   deviceNetwork  .  getUPBManager  (  )  .  isStateRequestQueued  (  this  )  ; 
} 








public   int   getLinkCount  (  )  { 
return   deviceLinks  .  size  (  )  ; 
} 








public   UPBLinkDevice   getLinkAt  (  int   linkIndex  )  { 
if  (  (  linkIndex  <  0  )  ||  (  linkIndex  >=  deviceLinks  .  size  (  )  )  )  return   null  ; 
return   deviceLinks  .  get  (  linkIndex  )  ; 
} 








public   UPBLinkDevice   getLinkByID  (  int   linkID  )  { 
for  (  UPBLinkDevice   devLink  :  deviceLinks  )  { 
if  (  devLink  .  getLink  (  )  .  getLinkID  (  )  ==  linkID  )  return   devLink  ; 
} 
return   null  ; 
} 

public   void   removeLinkDevice  (  UPBLinkDevice   linkDevice  )  { 
deviceLinks  .  remove  (  linkDevice  )  ; 
} 









public   List  <  UPBLinkDevice  >  getLinks  (  )  { 
return   deviceLinks  ; 
} 

boolean   addToLink  (  UPBLinkI   toLink  ,  int   deviceLevel  ,  int   fadeRate  ,  int   theChannel  )  { 
return   toLink  .  addDevice  (  this  ,  deviceLevel  ,  fadeRate  ,  theChannel  )  ; 
} 

boolean   removeFromLink  (  UPBLinkI   fromLink  ,  int   fromChannel  )  { 
return   fromLink  .  removeDevice  (  this  ,  fromChannel  )  ; 
} 

boolean   removeFromLink  (  UPBLinkI   fromLink  )  { 
return   removeFromLink  (  fromLink  ,  -  1  )  ; 
} 

void   removeFromAllLinks  (  )  { 
UPBLinkDevice   devLink  ; 
for  (  int   linkIndex  =  deviceLinks  .  size  (  )  -  1  ;  linkIndex  >=  0  ;  linkIndex  --  )  { 
if  (  (  devLink  =  deviceLinks  .  get  (  linkIndex  )  )  ==  null  )  continue  ; 
devLink  .  getLink  (  )  .  removeDevice  (  this  ,  -  1  )  ; 
devLink  .  releaseResources  (  )  ; 
} 
deviceLinks  .  clear  (  )  ; 
} 







public   boolean   isDeviceStateValid  (  )  { 
if  (  deviceStateValid  )  return   true  ; 
for  (  int   chanIndex  =  0  ;  chanIndex  <  deviceState  .  length  ;  chanIndex  ++  )  { 
if  (  deviceState  [  chanIndex  ]  ==  UNASSIGNED_DEVICE_STATE  )  return   false  ; 
} 
return  (  deviceStateValid  =  true  )  ; 
} 









public   boolean   isDeviceOn  (  int   forChannel  )  { 
if  (  (  forChannel  <  1  )  ||  (  forChannel  >  deviceState  .  length  )  )  return   false  ; 
return   deviceState  [  forChannel  -  1  ]  >  0  ; 
} 











public   boolean   isDeviceOn  (  )  { 
return   isDeviceOn  (  upbProduct  .  getPrimaryChannel  (  )  )  ; 
} 









public   void   turnDeviceOn  (  int   forChannel  )  { 
transmitNewDeviceLevel  (  DEFAULT_DIM_LEVEL  ,  DEFAULT_FADE_RATE  ,  forChannel  )  ; 
} 








public   void   turnDeviceOn  (  )  { 
transmitNewDeviceLevel  (  DEFAULT_DIM_LEVEL  ,  DEFAULT_FADE_RATE  ,  ALL_CHANNELS  )  ; 
} 









public   void   turnDeviceOff  (  int   forChannel  )  { 
transmitNewDeviceLevel  (  0  ,  DEFAULT_FADE_RATE  ,  forChannel  )  ; 
} 







public   void   turnDeviceOff  (  )  { 
transmitNewDeviceLevel  (  0  ,  DEFAULT_FADE_RATE  ,  ALL_CHANNELS  )  ; 
} 

void   deviceStateChanged  (  int   forChannel  )  { 
deviceNetwork  .  getUPBManager  (  )  .  fireDeviceEvent  (  this  ,  UPBDeviceEvent  .  EventCode  .  DEVICE_STATE_CHANGED  ,  forChannel  )  ; 
} 

















public   void   updateInternalDeviceLevel  (  int   newLevel  ,  int   atFadeRate  ,  int   forChannel  )  { 
if  (  (  newLevel  <  0  )  ||  (  newLevel  >  100  )  )  { 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Got request to set light to non-normative level -- queueing status request to confirm"  )  ; 
deviceNetwork  .  getUPBManager  (  )  .  queueStateRequest  (  this  )  ; 
return  ; 
} 
int   lowChannel  =  0  ,  highChannel  =  deviceState  .  length  -  1  ; 
if  (  forChannel  >  0  )  { 
lowChannel  =  forChannel  -  1  ; 
highChannel  =  forChannel  -  1  ; 
} 
for  (  int   chanIndex  =  lowChannel  ;  chanIndex  <=  highChannel  ;  chanIndex  ++  )  { 
if  (  newLevel  ==  deviceState  [  chanIndex  ]  )  continue  ; 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Level changing on channel "  +  chanIndex  +  " from "  +  deviceState  [  chanIndex  ]  +  " to "  +  newLevel  )  ; 
deviceState  [  chanIndex  ]  =  newLevel  ; 
deviceStateChanged  (  chanIndex  +  1  )  ; 
} 
} 

void   updateInternalDeviceLevels  (  int  [  ]  vals  )  { 
for  (  int   i  =  0  ;  i  <  vals  .  length  ;  i  ++  )  { 
int   newLevel  =  vals  [  i  ]  ; 
int   forChannel  =  i  +  1  ; 
if  (  (  newLevel  <  0  )  ||  (  newLevel  >  100  )  )  { 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Got request to set light to non-normative level -- queueing status request to confirm"  )  ; 
deviceNetwork  .  getUPBManager  (  )  .  queueStateRequest  (  this  )  ; 
return  ; 
} 
int   lowChannel  =  0  ,  highChannel  =  deviceState  .  length  -  1  ; 
if  (  forChannel  >  0  )  { 
lowChannel  =  forChannel  -  1  ; 
highChannel  =  forChannel  -  1  ; 
} 
for  (  int   chanIndex  =  lowChannel  ;  chanIndex  <=  highChannel  ;  chanIndex  ++  )  { 
if  (  newLevel  ==  deviceState  [  chanIndex  ]  )  continue  ; 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Level changing on channel "  +  chanIndex  +  " from "  +  deviceState  [  chanIndex  ]  +  " to "  +  newLevel  )  ; 
deviceState  [  chanIndex  ]  =  newLevel  ; 
deviceStateChanged  (  chanIndex  +  1  )  ; 
} 
} 
} 

















boolean   sendNewDeviceLevelMessage  (  int   theLevel  ,  int   theFadeRate  ,  int   theChannel  )  { 
return   deviceNetwork  .  getUPBManager  (  )  .  sendConfirmedMessage  (  new   UPBMessage  (  this  ,  UPBMsgType  .  GOTO  ,  theLevel  ,  theFadeRate  ,  theChannel  )  )  ; 
} 















void   installNewDeviceLevel  (  int   theLevel  ,  int   theFadeRate  ,  int   theChannel  )  { 
updateInternalDeviceLevel  (  theLevel  ,  theFadeRate  ,  theChannel  )  ; 
} 



















boolean   isReallyNewDeviceLevel  (  int   newDeviceLevel  ,  int   atFadeRate  ,  int   forChannel  )  { 
if  (  !  isDeviceStateValid  (  )  )  return   true  ; 
if  (  (  newDeviceLevel  <  0  )  ||  (  newDeviceLevel  >  100  )  )  return   true  ; 
int   lowChannel  =  0  ,  highChannel  =  deviceState  .  length  -  1  ; 
if  (  forChannel  >  0  )  { 
lowChannel  =  forChannel  -  1  ; 
highChannel  =  forChannel  -  1  ; 
} 
for  (  int   chanIndex  =  lowChannel  ;  chanIndex  <=  highChannel  ;  chanIndex  ++  )  { 
if  (  newDeviceLevel  !=  deviceState  [  chanIndex  ]  )  return   true  ; 
} 
return   false  ; 
} 















boolean   transmitNewDeviceLevel  (  int   newDeviceLevel  ,  int   atFadeRate  ,  int   toChannel  )  { 
if  (  !  isReallyNewDeviceLevel  (  newDeviceLevel  ,  atFadeRate  ,  toChannel  )  )  return   true  ; 
if  (  !  sendNewDeviceLevelMessage  (  newDeviceLevel  ,  atFadeRate  ,  toChannel  )  )  { 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Request to set device level failed -- Asking device for current state"  )  ; 
deviceNetwork  .  getUPBManager  (  )  .  queueStateRequest  (  this  )  ; 
return   false  ; 
} 
installNewDeviceLevel  (  newDeviceLevel  ,  atFadeRate  ,  toChannel  )  ; 
if  (  UPBManager  .  DEBUG_MODE  )  debug  (  "Confirmed request to set device to level succeedded -- updating internal state"  )  ; 
return   true  ; 
} 









void   receiveDeviceStateReport  (  UPBMessage   theMessage  )  { 
deviceNetwork  .  getUPBManager  (  )  .  cancelStateRequest  (  this  )  ; 
updateInternalDeviceLevels  (  theMessage  .  getValues  (  )  )  ; 
} 







void   receiveDeviceGotoCommand  (  UPBMessage   theMessage  )  { 
updateInternalDeviceLevel  (  theMessage  .  getLevel  (  )  ,  theMessage  .  getFadeRate  (  )  ,  theMessage  .  getChannel  (  )  )  ; 
} 










public   void   receiveMessage  (  UPBMessage   theMessage  )  { 
switch  (  theMessage  .  getMsgType  (  )  )  { 
case   GOTO  : 
case   FADE_START  : 
receiveDeviceGotoCommand  (  theMessage  )  ; 
break  ; 
case   FADE_STOP  : 
deviceNetwork  .  getUPBManager  (  )  .  queueStateRequest  (  this  )  ; 
break  ; 
case   DEVICE_STATE_RPT  : 
receiveDeviceStateReport  (  theMessage  )  ; 
break  ; 
} 
} 

void   customCopyFrom  (  UPBDevice   parentDevice  )  { 
} 

public   void   copyFrom  (  UPBDeviceI   origDevice  )  { 
UPBDevice   parentDevice  =  (  UPBDevice  )  origDevice  ; 
deviceNetwork  =  parentDevice  .  deviceNetwork  ; 
deviceID  =  parentDevice  .  deviceID  ; 
upbOptions  =  parentDevice  .  upbOptions  ; 
upbVersion  =  parentDevice  .  upbVersion  ; 
upbProduct  =  parentDevice  .  upbProduct  ; 
firmwareVersion  =  parentDevice  .  firmwareVersion  ; 
serialNumber  =  parentDevice  .  serialNumber  ; 
deviceName  =  parentDevice  .  deviceName  ; 
setRoom  (  parentDevice  .  deviceRoom  )  ; 
deviceStateValid  =  parentDevice  .  deviceStateValid  ; 
deviceState  =  parentDevice  .  deviceState  ; 
deviceLinks  .  clear  (  )  ; 
deviceLinks  .  addAll  (  parentDevice  .  deviceLinks  )  ; 
for  (  UPBLinkDevice   theDeviceLink  :  deviceLinks  )  { 
theDeviceLink  .  setDevice  (  this  )  ; 
} 
customCopyFrom  (  parentDevice  )  ; 
} 






public   String   toString  (  )  { 
return   deviceName  +  " ("  +  deviceID  +  ")"  ; 
} 







public   void   releaseResources  (  )  { 
if  (  deviceNetwork  !=  null  )  deviceNetwork  .  getUPBManager  (  )  .  removeAllRelatedDeviceListeners  (  this  )  ; 
if  (  deviceRoom  !=  null  )  deviceRoom  .  removeDevice  (  this  )  ; 
if  (  deviceLinks  !=  null  )  removeFromAllLinks  (  )  ; 
isDimmable  =  null  ; 
deviceState  =  null  ; 
deviceNetwork  =  null  ; 
deviceID  =  -  1  ; 
upbProduct  =  null  ; 
deviceName  =  null  ; 
deviceLinks  =  null  ; 
deviceRoom  =  null  ; 
} 
} 

