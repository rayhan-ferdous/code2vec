package   de  .  nec  .  nle  .  siafu  .  externalCommand  ; 

import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  ALL  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  AUTO  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  FIND_NEARBY_AGENTS  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  FIND_NEARBY_PLACES  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  GET_CONTEXT  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  HIDE  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  IMAGE  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  MARK  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  MOVE  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  SET_CONTEXT  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  SET_PREVIOUS_IMAGE  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  UNHIDE  ; 
import   static   de  .  nec  .  nle  .  siafu  .  externalCommand  .  CommandNames  .  UNMARK  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   de  .  nec  .  nle  .  siafu  .  control  .  Controller  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  InfoFieldsLockedException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  InvalidFlatDataException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  NothingNearException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  AgentNotFoundException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  PlaceNotFoundException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  PositionUnreachableException  ; 
import   de  .  nec  .  nle  .  siafu  .  exceptions  .  UnknownContextException  ; 
import   de  .  nec  .  nle  .  siafu  .  graphics  .  markers  .  BalloonMarker  ; 
import   de  .  nec  .  nle  .  siafu  .  graphics  .  markers  .  InvalidColorException  ; 
import   de  .  nec  .  nle  .  siafu  .  graphics  .  markers  .  Marker  ; 
import   de  .  nec  .  nle  .  siafu  .  graphics  .  markers  .  SpotMarker  ; 
import   de  .  nec  .  nle  .  siafu  .  graphics  .  markers  .  StickMarker  ; 
import   de  .  nec  .  nle  .  siafu  .  model  .  Agent  ; 
import   de  .  nec  .  nle  .  siafu  .  model  .  Place  ; 
import   de  .  nec  .  nle  .  siafu  .  model  .  Position  ; 
import   de  .  nec  .  nle  .  siafu  .  model  .  Trackable  ; 
























public   class   CommandListener   implements   Runnable  { 


private   static   final   int   IMAGE_PARTS  =  3  ; 


private   static   final   int   AUTO_PARTS  =  3  ; 


private   static   final   int   FIND_NEARBY_AGENTS_PARTS  =  3  ; 


private   static   final   int   FIND_NEARBY_PLACES_PARTS  =  3  ; 


private   static   final   int   MOVE_PARTS  =  4  ; 


private   static   final   int   GET_CONTEXT_PARTS  =  4  ; 


private   static   final   int   SET_CONTEXT_PARTS  =  4  ; 


private   static   final   int   SET_CONTEXT_VALUE_INDEX  =  3  ; 


private   static   final   int   MARK_COLOR_INDEX  =  3  ; 


private   static   final   int   MOVE_LONGITUDE_INDEX  =  3  ; 


private   static   final   int   MOVE_LATITUDE_INDEX  =  2  ; 


private   static   final   int   MARK_PARTS  =  3  ; 


private   ServerSocket   serverSocket  ; 


private   Socket   socket  ; 


private   BufferedReader   in  ; 


private   PrintWriter   out  ; 


private   Controller   control  ; 





private   CommandProcessor   cp  ; 


private   boolean   ended  ; 









public   CommandListener  (  final   Controller   control  ,  final   int   tcpPort  )  throws   IOException  { 
System  .  out  .  println  (  "Creating the command listener."  )  ; 
this  .  control  =  control  ; 
this  .  serverSocket  =  new   ServerSocket  (  tcpPort  )  ; 
System  .  out  .  println  (  "Listening for external commands."  )  ; 
cp  =  new   CommandProcessor  (  control  )  ; 
} 


public   synchronized   void   die  (  )  { 
ended  =  true  ; 
try  { 
if  (  out  !=  null  )  { 
out  .  close  (  )  ; 
} 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
if  (  socket  !=  null  )  { 
socket  .  close  (  )  ; 
} 
serverSocket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Error closing the listening socket"  ,  e  )  ; 
} 
System  .  out  .  println  (  "Command listener closed."  )  ; 
} 





public   void   run  (  )  { 
while  (  !  ended  )  { 
try  { 
socket  =  serverSocket  .  accept  (  )  ; 
in  =  new   BufferedReader  (  new   InputStreamReader  (  socket  .  getInputStream  (  )  )  )  ; 
out  =  new   PrintWriter  (  socket  .  getOutputStream  (  )  ,  true  )  ; 
String   command  =  in  .  readLine  (  )  ; 
while  (  command  !=  null  &&  !  ended  )  { 
synchronized  (  this  )  { 
if  (  control  .  isSimulationRunning  (  )  )  { 
processCommand  (  command  )  ; 
}  else  { 
sendError  (  "Siafu can't receive commands "  +  "right now.\nThere's probably no "  +  "simulation loaded"  )  ; 
} 
} 
command  =  in  .  readLine  (  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
socket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
if  (  !  ended  )  { 
e  .  printStackTrace  (  )  ; 
control  .  endSimulator  (  )  ; 
} 
} 
} 
} 






private   void   processCommand  (  final   String   rawCommand  )  { 
String   command  =  (  rawCommand  .  split  (  "\n"  )  )  [  0  ]  .  trim  (  )  ; 
String  [  ]  part  =  command  .  split  (  " "  )  ; 
if  (  part  [  0  ]  .  equalsIgnoreCase  (  MARK  )  )  { 
processMark  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  UNMARK  )  )  { 
processUnMark  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  MOVE  )  )  { 
processMove  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  AUTO  )  )  { 
processAuto  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  IMAGE  )  )  { 
processImage  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  SET_PREVIOUS_IMAGE  )  )  { 
processSetPreviousImage  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  GET_CONTEXT  )  )  { 
processGetContext  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  SET_CONTEXT  )  )  { 
processSetContext  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  FIND_NEARBY_AGENTS  )  )  { 
processFindNearbyAgents  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  FIND_NEARBY_PLACES  )  )  { 
processFindNearbyPlaces  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  HIDE  )  )  { 
processHide  (  part  )  ; 
}  else   if  (  part  [  0  ]  .  equalsIgnoreCase  (  UNHIDE  )  )  { 
processUnhide  (  part  )  ; 
}  else  { 
usage  (  part  [  0  ]  )  ; 
} 
} 






private   void   processHide  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  2  )  { 
usage  (  HIDE  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
if  (  name  .  equalsIgnoreCase  (  ALL  )  )  { 
cp  .  hideAll  (  )  ; 
}  else  { 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
cp  .  hide  (  person  )  ; 
} 
} 






private   void   processUnhide  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  2  )  { 
usage  (  UNHIDE  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
if  (  name  .  equalsIgnoreCase  (  ALL  )  )  { 
cp  .  unhideAll  (  )  ; 
}  else  { 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
cp  .  unhide  (  person  )  ; 
} 
} 






private   void   processImage  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  IMAGE_PARTS  )  { 
usage  (  IMAGE  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
String   image  =  part  [  2  ]  ; 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
cp  .  image  (  person  ,  image  )  ; 
sendOk  (  )  ; 
} 







private   void   processSetPreviousImage  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  2  )  { 
usage  (  SET_PREVIOUS_IMAGE  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
cp  .  resetImage  (  person  )  ; 
sendOk  (  )  ; 
} 







private   void   processAuto  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  AUTO_PARTS  )  { 
usage  (  AUTO  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
String   autoSettingStr  =  part  [  2  ]  ; 
boolean   autoSetting  ; 
if  (  autoSettingStr  .  equalsIgnoreCase  (  "true"  )  )  { 
autoSetting  =  true  ; 
}  else   if  (  autoSettingStr  .  equalsIgnoreCase  (  "false"  )  )  { 
autoSetting  =  false  ; 
}  else  { 
usage  (  AUTO  )  ; 
return  ; 
} 
if  (  name  .  equalsIgnoreCase  (  ALL  )  )  { 
cp  .  autoAll  (  autoSetting  )  ; 
}  else  { 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
cp  .  auto  (  person  ,  autoSetting  )  ; 
} 
sendOk  (  )  ; 
} 






private   void   processFindNearbyAgents  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  FIND_NEARBY_AGENTS_PARTS  )  { 
usage  (  FIND_NEARBY_AGENTS  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
Integer   dist  =  new   Integer  (  part  [  2  ]  )  ; 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
Position   pos  =  person  .  getPos  (  )  ; 
try  { 
send  (  cp  .  findAgentsNear  (  pos  ,  dist  )  )  ; 
}  catch  (  NothingNearException   e  )  { 
sendError  (  "No one found"  )  ; 
} 
} 






private   void   processFindNearbyPlaces  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  FIND_NEARBY_PLACES_PARTS  )  { 
usage  (  FIND_NEARBY_PLACES  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
Integer   dist  =  new   Integer  (  part  [  2  ]  )  ; 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
Position   pos  =  person  .  getPos  (  )  ; 
try  { 
send  (  cp  .  findPlacesNear  (  pos  ,  dist  )  )  ; 
}  catch  (  NothingNearException   e  )  { 
sendError  (  "No place found"  )  ; 
} 
} 






private   void   processMove  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  MOVE_PARTS  )  { 
usage  (  MOVE  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
double   latitude  ; 
double   longitude  ; 
try  { 
latitude  =  new   Double  (  part  [  MOVE_LATITUDE_INDEX  ]  )  .  doubleValue  (  )  ; 
longitude  =  new   Double  (  part  [  MOVE_LONGITUDE_INDEX  ]  )  .  doubleValue  (  )  ; 
}  catch  (  Exception   e  )  { 
usage  (  MOVE  )  ; 
return  ; 
} 
Agent   person  ; 
try  { 
person  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  "Who's "  +  name  +  "?"  )  ; 
return  ; 
} 
Place   tempPlace  ; 
try  { 
tempPlace  =  new   Place  (  "Unknown"  ,  new   Position  (  latitude  ,  longitude  )  ,  control  .  getWorld  (  )  ,  person  .  getPos  (  )  )  ; 
}  catch  (  PositionUnreachableException   e  )  { 
sendError  (  "Your destination is unreachable "  +  "(and inside a wall, probably)"  )  ; 
return  ; 
} 
cp  .  move  (  person  ,  tempPlace  )  ; 
sendOk  (  )  ; 
} 






private   void   processMark  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  MARK_PARTS  )  { 
usage  (  MARK  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
Trackable   t  ; 
try  { 
t  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
try  { 
t  =  (  Place  )  control  .  getWorld  (  )  .  getPlaceByName  (  name  )  ; 
}  catch  (  PlaceNotFoundException   e1  )  { 
sendError  (  "Who or where is "  +  name  +  "?"  )  ; 
return  ; 
} 
} 
String   color  =  "#FFFF00"  ; 
if  (  part  .  length  >=  MARK_PARTS  +  1  )  { 
color  =  part  [  MARK_COLOR_INDEX  ]  ; 
} 
Marker   mark  ; 
try  { 
mark  =  parseMark  (  t  ,  part  [  2  ]  ,  color  )  ; 
}  catch  (  InvalidColorException   e  )  { 
sendError  (  "Unparseable color, use #RRGGBB. e.g.: #AA0000"  )  ; 
return  ; 
} 
if  (  mark  ==  null  )  { 
sendError  (  "Unknown style "  +  part  [  2  ]  )  ; 
return  ; 
} 
cp  .  mark  (  mark  )  ; 
sendOk  (  )  ; 
} 






private   void   processGetContext  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  GET_CONTEXT_PARTS  )  { 
usage  (  GET_CONTEXT  )  ; 
return  ; 
} 
int   separator  ; 
for  (  separator  =  1  ;  separator  <  part  .  length  ;  separator  ++  )  { 
if  (  part  [  separator  ]  .  equals  (  "/"  )  )  { 
break  ; 
} 
} 
if  (  separator  ==  part  .  length  ||  separator  <=  0  )  { 
usage  (  GET_CONTEXT  )  ; 
return  ; 
} 
String  [  ]  agents  =  new   String  [  separator  -  1  ]  ; 
String  [  ]  contexts  =  new   String  [  part  .  length  -  1  -  separator  ]  ; 
for  (  int   i  =  0  ;  i  <  agents  .  length  ;  i  ++  )  { 
agents  [  i  ]  =  part  [  i  +  1  ]  ; 
} 
int   offset  =  separator  +  1  ; 
for  (  int   i  =  0  ;  i  <  contexts  .  length  ;  i  ++  )  { 
contexts  [  i  ]  =  part  [  i  +  offset  ]  ; 
} 
String   reply  ; 
try  { 
reply  =  cp  .  getContext  (  agents  ,  contexts  )  ; 
}  catch  (  UnknownContextException   e  )  { 
sendError  (  e  .  getMessage  (  )  )  ; 
return  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  e  .  getMessage  (  )  )  ; 
return  ; 
} 
send  (  reply  )  ; 
} 






private   void   processSetContext  (  final   String  [  ]  part  )  { 
if  (  part  .  length  !=  SET_CONTEXT_PARTS  )  { 
usage  (  SET_CONTEXT  )  ; 
return  ; 
} 
String   agentName  =  part  [  1  ]  ; 
String   contextVariable  =  part  [  2  ]  ; 
String   value  =  part  [  SET_CONTEXT_VALUE_INDEX  ]  ; 
try  { 
cp  .  setContext  (  agentName  ,  contextVariable  ,  value  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
sendError  (  e  .  getMessage  (  )  )  ; 
return  ; 
}  catch  (  InfoFieldsLockedException   e  )  { 
sendError  (  e  .  getMessage  (  )  )  ; 
return  ; 
}  catch  (  InvalidFlatDataException   e  )  { 
sendError  (  e  .  getMessage  (  )  )  ; 
return  ; 
} 
sendOk  (  )  ; 
} 









private   Marker   parseMark  (  final   Trackable   t  ,  final   String   styleStr  ,  final   String   color  )  { 
if  (  styleStr  .  equalsIgnoreCase  (  "Balloon"  )  )  { 
return   new   BalloonMarker  (  t  ,  color  )  ; 
}  else   if  (  styleStr  .  equalsIgnoreCase  (  "Spot"  )  )  { 
return   new   SpotMarker  (  t  ,  color  )  ; 
}  else   if  (  styleStr  .  equalsIgnoreCase  (  "Stick"  )  )  { 
return   new   StickMarker  (  t  ,  color  )  ; 
}  else  { 
return   null  ; 
} 
} 






private   void   processUnMark  (  final   String  [  ]  part  )  { 
if  (  part  .  length  <  2  )  { 
usage  (  UNMARK  )  ; 
return  ; 
} 
String   name  =  part  [  1  ]  ; 
if  (  name  .  equalsIgnoreCase  (  ALL  )  )  { 
cp  .  unMarkAll  (  )  ; 
}  else  { 
Trackable   t  ; 
try  { 
t  =  (  Agent  )  control  .  getWorld  (  )  .  getPersonByName  (  name  )  ; 
}  catch  (  AgentNotFoundException   e  )  { 
try  { 
t  =  (  Place  )  control  .  getWorld  (  )  .  getPlaceByName  (  name  )  ; 
}  catch  (  PlaceNotFoundException   e1  )  { 
sendError  (  "Who or where is "  +  name  +  "?"  )  ; 
return  ; 
} 
} 
cp  .  unMark  (  t  )  ; 
} 
sendOk  (  )  ; 
} 




private   void   sendOk  (  )  { 
send  (  "OK - Command succeeded"  )  ; 
} 






private   void   sendError  (  final   String   reason  )  { 
String   msg  =  "ERR - "  +  reason  ; 
System  .  err  .  println  (  "External command failed: "  +  msg  )  ; 
send  (  msg  )  ; 
} 






private   void   send  (  final   String   msg  )  { 
out  .  println  (  msg  )  ; 
} 






private   void   usage  (  final   String   command  )  { 
String   msg  =  "I didn't understand your command."  ; 
if  (  command  .  equalsIgnoreCase  (  MARK  )  )  { 
msg  =  "ERR -  Usage: mark name style [color]\n"  +  "where style is one of \"Balloon\", \"Stick\" "  +  "or \"Spot\" and color is of the form #RRGGBB\n\n"  +  "e.g.: mark Agnes balloon #FF0000"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  UNMARK  )  )  { 
msg  =  "ERR - Usage: "  +  UNMARK  +  " name\n"  +  "e.g.: "  +  UNMARK  +  " Agnes"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  MOVE  )  )  { 
msg  =  "ERR - Usage: "  +  MOVE  +  " personName latitude longitude\n"  +  "e.g.: "  +  MOVE  +  " Agnes 49.32343 -3.34321"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  AUTO  )  )  { 
msg  =  "ERR - Usage: "  +  AUTO  +  " personName (true|false)\n"  +  "e.g.: "  +  AUTO  +  " Agnes false"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  IMAGE  )  )  { 
msg  =  "ERR - Usage: "  +  IMAGE  +  " personName imageName\n"  +  "e.g.: "  +  IMAGE  +  " Agnes CarRed"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  SET_PREVIOUS_IMAGE  )  )  { 
msg  =  "ERR - Usage: "  +  SET_PREVIOUS_IMAGE  +  " personName \n"  +  "e.g.: "  +  SET_PREVIOUS_IMAGE  +  " Agnes"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  GET_CONTEXT  )  )  { 
msg  =  "ERR - Usage: "  +  GET_CONTEXT  +  " name1 name2 ... / context1 context2 ... \n"  +  "e.g.: "  +  GET_CONTEXT  +  " Agnes Alban / position temperature"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  SET_CONTEXT  )  )  { 
msg  =  "ERR - Usage: "  +  SET_CONTEXT  +  " user variable value\n"  +  "e.g.: "  +  SET_CONTEXT  +  " Agnes cuisine french"  ; 
}  else   if  (  command  .  equalsIgnoreCase  (  FIND_NEARBY_AGENTS  )  )  { 
msg  =  "ERR - Usage: "  +  FIND_NEARBY_AGENTS  +  " name distance\n"  +  "e.g.: "  +  FIND_NEARBY_AGENTS  +  " Agnes 10"  ; 
}  else  { 
msg  =  "ERR - Unknown command "  +  command  +  ".\nType a command for help on its syntax.\n"  +  "The available commands are: \n"  +  MARK  +  ","  +  UNMARK  +  ","  +  MOVE  +  ","  +  AUTO  +  ","  +  IMAGE  +  ","  +  SET_PREVIOUS_IMAGE  +  ","  +  SET_CONTEXT  +  ","  +  FIND_NEARBY_AGENTS  +  ","  +  FIND_NEARBY_PLACES  +  ","  +  HIDE  +  " and "  +  UNHIDE  ; 
} 
send  (  msg  )  ; 
} 
} 

