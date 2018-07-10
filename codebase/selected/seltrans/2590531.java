package   gov  .  sns  .  apps  .  scope  ; 

import   gov  .  sns  .  ca  .  *  ; 
import   gov  .  sns  .  tools  .  correlator  .  RecordFilter  ; 
import   gov  .  sns  .  tools  .  data  .  *  ; 
import   gov  .  sns  .  tools  .  messaging  .  MessageCenter  ; 









public   class   Trigger   implements   DataListener  ,  ConnectionListener  { 

static   final   String   dataLabel  =  "Trigger"  ; 

protected   Channel   channel  ; 

protected   boolean   isEnabled  ; 

protected   TriggerFilter   triggerFilter  ; 

protected   volatile   boolean   isSettingChannel  ; 

protected   MessageCenter   messageCenter  ; 

protected   TriggerListener   triggerChangeProxy  ; 

protected   SettingListener   settingProxy  ; 


public   Trigger  (  )  { 
messageCenter  =  new   MessageCenter  (  "Trigger Model"  )  ; 
triggerChangeProxy  =  messageCenter  .  registerSource  (  this  ,  TriggerListener  .  class  )  ; 
settingProxy  =  messageCenter  .  registerSource  (  this  ,  SettingListener  .  class  )  ; 
triggerFilter  =  null  ; 
setChannel  (  null  )  ; 
} 





public   String   dataLabel  (  )  { 
return   dataLabel  ; 
} 




public   void   update  (  DataAdaptor   adaptor  )  { 
if  (  adaptor  .  hasAttribute  (  "channel"  )  )  { 
setChannel  (  adaptor  .  stringValue  (  "channel"  )  )  ; 
} 
DataAdaptor   filterAdaptor  =  adaptor  .  childAdaptor  (  TriggerFilter  .  dataLabel  )  ; 
if  (  filterAdaptor  !=  null  )  { 
triggerFilter  =  TriggerFilterFactory  .  decodeFilter  (  filterAdaptor  )  ; 
} 
setEnabled  (  adaptor  .  booleanValue  (  "enabled"  )  )  ; 
} 





public   void   write  (  DataAdaptor   adaptor  )  { 
if  (  channel  !=  null  )  { 
adaptor  .  setValue  (  "channel"  ,  channel  .  channelName  (  )  )  ; 
} 
adaptor  .  setValue  (  "enabled"  ,  isEnabled  )  ; 
if  (  triggerFilter  !=  null  )  { 
adaptor  .  writeNode  (  triggerFilter  )  ; 
} 
} 





public   void   addTriggerListener  (  TriggerListener   listener  )  { 
messageCenter  .  registerTarget  (  listener  ,  this  ,  TriggerListener  .  class  )  ; 
} 





public   void   removeTriggerListener  (  TriggerListener   listener  )  { 
messageCenter  .  removeTarget  (  listener  ,  this  ,  TriggerListener  .  class  )  ; 
} 





void   addSettingListener  (  SettingListener   listener  )  { 
messageCenter  .  registerTarget  (  listener  ,  this  ,  SettingListener  .  class  )  ; 
} 





void   removeSettingListener  (  SettingListener   listener  )  { 
messageCenter  .  removeTarget  (  listener  ,  this  ,  SettingListener  .  class  )  ; 
} 





public   boolean   isSettingChannel  (  )  { 
return   isSettingChannel  ; 
} 







public   void   setChannel  (  String   channelName  )  throws   ChannelSetException  { 
isSettingChannel  =  true  ; 
try  { 
if  (  channel  !=  null  )  { 
channel  .  removeConnectionListener  (  this  )  ; 
} 
if  (  channelName  ==  null  )  { 
setEnabled  (  false  )  ; 
channel  =  null  ; 
return  ; 
} 
channel  =  ChannelFactory  .  defaultFactory  (  )  .  getChannel  (  channelName  )  ; 
setEnabled  (  false  )  ; 
triggerChangeProxy  .  channelStateChanged  (  this  )  ; 
channel  .  addConnectionListener  (  this  )  ; 
setEnabled  (  true  )  ; 
settingProxy  .  settingChanged  (  this  )  ; 
}  finally  { 
isSettingChannel  =  false  ; 
triggerChangeProxy  .  channelStateChanged  (  this  )  ; 
} 
} 





public   Channel   getChannel  (  )  { 
return   channel  ; 
} 





public   String   getChannelName  (  )  { 
return  (  channel  !=  null  )  ?  channel  .  channelName  (  )  :  ""  ; 
} 





public   boolean   isConnected  (  )  { 
return   channel  !=  null  &&  channel  .  isConnected  (  )  ; 
} 






public   boolean   canEnable  (  )  { 
return   channel  !=  null  ; 
} 





public   boolean   isEnabled  (  )  { 
return   isEnabled  ; 
} 




public   void   toggleEnable  (  )  { 
setEnabled  (  !  isEnabled  )  ; 
} 





public   void   setEnabled  (  boolean   state  )  { 
if  (  canEnable  (  )  ||  !  state  &&  isEnabled  !=  state  )  { 
isEnabled  =  state  ; 
if  (  isEnabled  )  { 
triggerChangeProxy  .  triggerEnabled  (  this  )  ; 
}  else  { 
triggerChangeProxy  .  triggerDisabled  (  this  )  ; 
} 
} 
settingProxy  .  settingChanged  (  this  )  ; 
} 




public   void   setTriggerFilter  (  TriggerFilter   filter  )  { 
boolean   enableState  =  isEnabled  ; 
setEnabled  (  false  )  ; 
triggerFilter  =  filter  ; 
setEnabled  (  enableState  )  ; 
} 




public   void   refresh  (  )  { 
if  (  triggerFilter  !=  null  )  { 
triggerFilter  .  updateFilter  (  )  ; 
} 
setTriggerFilter  (  triggerFilter  )  ; 
} 





public   TriggerFilter   getTriggerFilter  (  )  { 
return   triggerFilter  ; 
} 





public   gov  .  sns  .  tools  .  Parameter  [  ]  getFilterParameters  (  )  { 
return  (  triggerFilter  !=  null  )  ?  triggerFilter  .  getParameters  (  )  :  new   gov  .  sns  .  tools  .  Parameter  [  0  ]  ; 
} 





public   String   getFilterLabel  (  )  { 
return  (  triggerFilter  !=  null  )  ?  triggerFilter  .  getLabel  (  )  :  "None"  ; 
} 









public   RecordFilter   getRecordFilter  (  )  { 
return  (  triggerFilter  !=  null  )  ?  triggerFilter  .  getRecordFilter  (  )  :  null  ; 
} 





public   void   connectionMade  (  Channel   channel  )  { 
triggerChangeProxy  .  channelStateChanged  (  this  )  ; 
} 





public   void   connectionDropped  (  Channel   channel  )  { 
triggerChangeProxy  .  channelStateChanged  (  this  )  ; 
} 
} 

