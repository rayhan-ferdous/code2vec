package   spacewalklib  ; 

import   java  .  net  .  MalformedURLException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   redstone  .  xmlrpc  .  XmlRpcClient  ; 
import   redstone  .  xmlrpc  .  XmlRpcException  ; 
import   redstone  .  xmlrpc  .  XmlRpcFault  ; 
import   redstone  .  xmlrpc  .  XmlRpcStruct  ; 









public   class   RhnSwChannel  { 


private   Integer   id_  ; 


private   String   name_  ; 


private   String   label_  ; 


private   String   parent_channel_label_  ; 


private   String   spacewalk_  ; 


private   String   connectionId_  ; 


private   RhnConn   connection_  ; 


private   List  <  RhnSwChannel  >  child_channels_  =  new   ArrayList  <  RhnSwChannel  >  (  )  ; 














public   RhnSwChannel  (  Integer   id  ,  String   name  ,  String   label  ,  String   parent_channel  ,  String   spacewalk  )  { 
this  .  id_  =  id  ; 
this  .  name_  =  name  ; 
this  .  label_  =  label  ; 
this  .  parent_channel_label_  =  parent_channel  ; 
this  .  spacewalk_  =  spacewalk  ; 
} 















public   RhnSwChannel  (  String   spacewalk  ,  String   user  ,  String   password  ,  Integer   id  )  throws   RhnConnFault  ,  RhnChannelNotFoundException  { 
RhnConn   connection  =  new   RhnConn  (  spacewalk  ,  user  ,  password  )  ; 
this  .  connectionId_  =  connection  .  getId  (  )  ; 
this  .  spacewalk_  =  spacewalk  ; 
this  .  connection_  =  connection  ; 
this  .  getInfo  (  id  )  ; 
} 















public   RhnSwChannel  (  String   spacewalk  ,  String   user  ,  String   password  ,  String   label  )  throws   RhnConnFault  ,  RhnChannelNotFoundException  { 
RhnConn   connection  =  new   RhnConn  (  spacewalk  ,  user  ,  password  )  ; 
this  .  connectionId_  =  connection  .  getId  (  )  ; 
this  .  spacewalk_  =  spacewalk  ; 
this  .  connection_  =  connection  ; 
this  .  getInfo  (  label  )  ; 
} 













public   RhnSwChannel  (  RhnConn   connection  ,  String   label  )  throws   RhnConnFault  ,  RhnChannelNotFoundException  { 
this  .  connectionId_  =  connection  .  getId  (  )  ; 
this  .  spacewalk_  =  connection  .  getServer  (  )  ; 
this  .  connection_  =  connection  ; 
this  .  getInfo  (  label  )  ; 
} 












private   void   getInfo  (  Integer   id  )  throws   RhnConnFault  ,  RhnChannelNotFoundException  { 
try  { 
XmlRpcClient   client  =  new   XmlRpcClient  (  this  .  spacewalk_  ,  false  )  ; 
List   args  =  new   ArrayList  (  )  ; 
args  .  add  (  this  .  connectionId_  )  ; 
args  .  add  (  id  )  ; 
XmlRpcStruct   channel  =  (  XmlRpcStruct  )  client  .  invoke  (  "channel.software.getDetails"  ,  args  )  ; 
if  (  channel  .  isEmpty  (  )  )  { 
throw   new   RhnChannelNotFoundException  (  "Software channel with id "  +  id  +  " not found"  )  ; 
}  else  { 
this  .  id_  =  (  Integer  )  channel  .  get  (  "id"  )  ; 
this  .  label_  =  channel  .  get  (  "label"  )  .  toString  (  )  ; 
this  .  name_  =  channel  .  get  (  "name"  )  .  toString  (  )  ; 
this  .  parent_channel_label_  =  channel  .  get  (  "parent_channel_label"  )  .  toString  (  )  ; 
} 
}  catch  (  MalformedURLException   ex  )  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk. Problem found in server URL: "  +  ex  .  getMessage  (  )  )  ; 
}  catch  (  XmlRpcFault   ex  )  { 
if  (  ex  .  getMessage  (  )  .  contains  (  "channel does not exist"  )  )  { 
throw   new   RhnChannelNotFoundException  (  "Channel with id "  +  id  +  " not found: "  +  ex  .  getMessage  (  )  )  ; 
}  else  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk server. Problem found in connection: "  +  ex  .  getMessage  (  )  )  ; 
} 
}  catch  (  XmlRpcException   ex  )  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk server. Problem found in connection: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 












private   void   getInfo  (  String   label  )  throws   RhnConnFault  ,  RhnChannelNotFoundException  { 
try  { 
XmlRpcClient   client  =  new   XmlRpcClient  (  this  .  spacewalk_  ,  false  )  ; 
List   args  =  new   ArrayList  (  )  ; 
args  .  add  (  this  .  connectionId_  )  ; 
args  .  add  (  label  )  ; 
XmlRpcStruct   channel  =  (  XmlRpcStruct  )  client  .  invoke  (  "channel.software.getDetails"  ,  args  )  ; 
if  (  channel  .  isEmpty  (  )  )  { 
throw   new   RhnChannelNotFoundException  (  "Software channel with label "  +  label  +  " not found"  )  ; 
}  else  { 
this  .  id_  =  (  Integer  )  channel  .  get  (  "id"  )  ; 
this  .  label_  =  channel  .  get  (  "label"  )  .  toString  (  )  ; 
this  .  name_  =  channel  .  get  (  "name"  )  .  toString  (  )  ; 
this  .  parent_channel_label_  =  channel  .  get  (  "parent_channel_label"  )  .  toString  (  )  ; 
} 
}  catch  (  MalformedURLException   ex  )  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk. Problem found in server URL: "  +  ex  .  getMessage  (  )  )  ; 
}  catch  (  XmlRpcFault   ex  )  { 
if  (  ex  .  getMessage  (  )  .  contains  (  "channel does not exist"  )  ||  ex  .  getMessage  (  )  .  contains  (  "No such channel"  )  )  { 
throw   new   RhnChannelNotFoundException  (  "Channel "  +  label  +  " not found: "  +  ex  .  getMessage  (  )  )  ; 
}  else  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk server. Problem found in connection: "  +  ex  .  getMessage  (  )  )  ; 
} 
}  catch  (  XmlRpcException   ex  )  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk server. Problem found in connection: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 










private   void   getChildChannels  (  )  throws   RhnChannelNotFoundException  ,  RhnConnFault  { 
try  { 
if  (  this  .  parent_channel_label_  .  isEmpty  (  )  )  { 
this  .  child_channels_  .  clear  (  )  ; 
RhnSwChannels   channels  =  new   RhnSwChannels  (  this  .  connection_  )  ; 
for  (  RhnSwChannel   channel  :  channels  .  getChannels  (  )  )  { 
if  (  channel  .  getParentchannel  (  )  .  equals  (  this  .  label_  )  )  { 
this  .  child_channels_  .  add  (  channel  )  ; 
} 
} 
}  else  { 
throw   new   RhnChannelNotFoundException  (  "Channel "  +  this  .  name_  +  " is not a base channel"  )  ; 
} 
}  catch  (  XmlRpcException   ex  )  { 
throw   new   RhnConnFault  (  "Error connecting to spacewalk server. Problem found in connection: "  +  ex  .  getMessage  (  )  )  ; 
} 
} 






public   String   getName  (  )  { 
return   this  .  name_  ; 
} 






public   String   getLabel  (  )  { 
return   this  .  label_  ; 
} 






public   String   getParentchannel  (  )  { 
return   this  .  parent_channel_label_  ; 
} 






public   Integer   getId  (  )  { 
return   this  .  id_  ; 
} 









public   List  <  RhnSwChannel  >  getChildList  (  )  throws   RhnChannelNotFoundException  ,  RhnConnFault  { 
if  (  this  .  child_channels_  .  size  (  )  ==  0  )  { 
this  .  getChildChannels  (  )  ; 
} 
return   this  .  child_channels_  ; 
} 
} 

