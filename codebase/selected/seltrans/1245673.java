package   Utilities  ; 

import   Beans  .  PermissionsBean  ; 
import   Beans  .  ServerBean  ; 
import   Managers  .  DatabaseManager  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  sql  .  Timestamp  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  UUID  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   javax  .  xml  .  ws  .  WebServiceException  ; 






public   class   Functions  { 


public   static   final   Pattern   locationPattern  =  Pattern  .  compile  (  "[^/\"\\\\|`?*<>:\n\t\f\r]+"  )  ; 


public   static   final   Pattern   virtualBoxExceptionPattern  =  Pattern  .  compile  (  "VirtualBox\\serror:\\s(.*)\\s\\(0x(.*)\\)"  )  ; 

private   Functions  (  )  { 
} 











public   static   String   hashPassword  (  final   String   plainTextPassword  ,  String   hashingAlgorithm  ,  String   charsetName  )  throws   NoSuchAlgorithmException  ,  UnsupportedEncodingException  { 
String   result  =  null  ; 
MessageDigest   digest  =  MessageDigest  .  getInstance  (  hashingAlgorithm  )  ; 
result  =  new   String  (  digest  .  digest  (  plainTextPassword  .  getBytes  (  charsetName  )  )  ,  charsetName  )  ; 
return   result  ; 
} 







public   static   boolean   isUserIdValid  (  final   int   userId  )  { 
if  (  userId  >  Constants  .  INVALID_USER_ID  )  return   true  ;  else   return   false  ; 
} 







public   static   boolean   isServerIdValid  (  final   int   serverId  )  { 
if  (  serverId  >  Constants  .  INVALID_SERVER_ID  )  return   true  ;  else   return   false  ; 
} 







public   static   boolean   arePermissionsValid  (  final   String   permissions  )  { 
if  (  permissions  !=  null  &&  permissions  .  length  (  )  ==  3  )  { 
try  { 
Integer  .  parseInt  (  permissions  )  ; 
return   true  ; 
}  catch  (  NumberFormatException   e  )  { 
return   false  ; 
} 
}  else   return   false  ; 
} 







public   static   boolean   isTimestampValid  (  final   Timestamp   timestamp  )  { 
if  (  timestamp  !=  null  )  return   true  ;  else   return   false  ; 
} 









public   static   boolean   isUUIDValid  (  final   String   uuid  )  { 
if  (  uuid  !=  null  )  { 
try  { 
UUID  .  fromString  (  uuid  )  ; 
return   true  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
}  else  ; 
return   false  ; 
} 









public   static   boolean   isLocationValid  (  final   String   location  )  { 
return   locationPattern  .  matcher  (  location  )  .  matches  (  )  ; 
} 











public   static   boolean   isControllerPortValid  (  final   int   port  )  { 
if  (  port  >=  0  &&  port  <=  29  )  return   true  ;  else   return   false  ; 
} 









public   static   boolean   isControllerSlotValid  (  final   int   slot  )  { 
if  (  slot  ==  0  ||  slot  ==  1  )  return   true  ;  else   return   false  ; 
} 













public   static   boolean   isUserServerManager  (  final   int   userId  ,  final   PermissionsBean   permissions  )  { 
String   managerPermissions  =  "777"  ; 
if  (  userId  ==  permissions  .  getUserId  (  )  &&  permissions  .  getMachinesPermissions  (  )  .  equals  (  managerPermissions  )  &&  permissions  .  getMediaPermissions  (  )  .  equals  (  managerPermissions  )  &&  permissions  .  getNetworksPermissions  (  )  .  equals  (  managerPermissions  )  )  return   true  ;  else   return   false  ; 
} 















public   static   String   allocateVRDPPorts  (  DatabaseManager   manager  ,  int   serverId  )  throws   IllegalArgumentException  { 
if  (  manager  !=  null  &&  isServerIdValid  (  serverId  )  )  { 
ServerBean   serverData  =  manager  .  getServer  (  serverId  )  ; 
if  (  serverData  !=  null  &&  serverData  .  isValid  (  )  )  { 
Random   generator  =  new   Random  (  )  ; 
int   numberOfSlots  =  (  serverData  .  getVrdpPortsRangeHigh  (  )  -  serverData  .  getVrdpPortsRangeLow  (  )  )  /  serverData  .  getVrdpPortsPerMachine  (  )  ; 
String  [  ]  slots  =  serverData  .  getVrdpAllocatedSlots  (  )  .  split  (  ";"  )  ; 
if  (  slots  .  length  >=  numberOfSlots  )  return   null  ;  else  ; 
boolean   done  =  false  ; 
int   generatedSlot  =  -  1  ; 
Pattern   newPattern  =  null  ; 
StringBuilder   expression  =  new   StringBuilder  (  )  ; 
do  { 
generatedSlot  =  generator  .  nextInt  (  numberOfSlots  )  ; 
expression  .  delete  (  0  ,  expression  .  length  (  )  )  ; 
expression  .  append  (  ";"  )  ; 
expression  .  append  (  generatedSlot  )  ; 
expression  .  append  (  ";|;"  )  ; 
expression  .  append  (  generatedSlot  )  ; 
expression  .  append  (  "$"  )  ; 
newPattern  =  Pattern  .  compile  (  expression  .  toString  (  )  )  ; 
if  (  !  newPattern  .  matcher  (  serverData  .  getVrdpAllocatedSlots  (  )  )  .  find  (  )  )  done  =  true  ;  else  ; 
}  while  (  !  done  )  ; 
if  (  generatedSlot  >=  0  )  { 
manager  .  updateServerVRDPAllocationSlots  (  serverId  ,  serverData  .  getVrdpAllocatedSlots  (  )  +  ";"  +  generatedSlot  )  ; 
int   firstPort  =  serverData  .  getVrdpPortsRangeLow  (  )  +  generatedSlot  *  serverData  .  getVrdpPortsPerMachine  (  )  ; 
String   result  =  ""  ; 
for  (  int   i  =  0  ;  i  <  serverData  .  getVrdpPortsPerMachine  (  )  ;  i  ++  )  result  +=  (  firstPort  +  i  )  +  ","  ; 
return   result  .  substring  (  0  ,  result  .  length  (  )  -  1  )  ; 
}  else   return   null  ; 
}  else   throw   new   IllegalArgumentException  (  "Invalid server ID, server does not exist."  )  ; 
}  else   throw   new   IllegalArgumentException  (  "No database manager or invalid server ID supplied."  )  ; 
} 













public   static   void   freeVRDPPorts  (  DatabaseManager   manager  ,  int   serverId  ,  String   ports  )  throws   IllegalArgumentException  { 
if  (  ports  ==  null  ||  ports  .  trim  (  )  .  equals  (  ""  )  )  return  ;  else  ; 
if  (  manager  !=  null  &&  isServerIdValid  (  serverId  )  )  { 
ServerBean   serverData  =  manager  .  getServer  (  serverId  )  ; 
if  (  serverData  !=  null  &&  serverData  .  isValid  (  )  )  { 
int   slot  =  (  Integer  .  parseInt  (  ports  .  split  (  ","  )  [  0  ]  )  -  serverData  .  getVrdpPortsRangeLow  (  )  )  /  serverData  .  getVrdpPortsPerMachine  (  )  ; 
StringBuilder   expression  =  new   StringBuilder  (  )  ; 
expression  .  append  (  ";"  )  ; 
expression  .  append  (  slot  )  ; 
expression  .  append  (  "(;)|;"  )  ; 
expression  .  append  (  slot  )  ; 
expression  .  append  (  "$"  )  ; 
manager  .  updateServerVRDPAllocationSlots  (  serverId  ,  serverData  .  getVrdpAllocatedSlots  (  )  .  replaceFirst  (  expression  .  toString  (  )  ,  "$1"  )  )  ; 
}  else   throw   new   IllegalArgumentException  (  "Invalid server ID, server does not exist."  )  ; 
}  else   throw   new   IllegalArgumentException  (  "No database manager or invalid server ID supplied."  )  ; 
} 










public   static   ApplicationException   parseVirtualBoxException  (  final   WebServiceException   exception  )  { 
ApplicationException   result  =  null  ; 
Matcher   exceptionMatcher  =  virtualBoxExceptionPattern  .  matcher  (  exception  .  getMessage  (  )  )  ; 
if  (  exceptionMatcher  .  find  (  )  )  result  =  new   ApplicationException  (  exceptionMatcher  .  group  (  2  )  ,  exceptionMatcher  .  group  (  1  )  )  ;  else   result  =  new   ApplicationException  (  "-1"  ,  "Failed to parse exception message: "  +  exception  .  getMessage  (  )  )  ; 
return   result  ; 
} 
} 

