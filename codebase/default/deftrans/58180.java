import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  EventObject  ; 
import   java  .  util  .  Iterator  ; 
import   opencard  .  core  .  terminal  .  *  ; 
import   opencard  .  core  .  service  .  *  ; 
import   opencard  .  core  .  event  .  *  ; 
import   opencard  .  core  .  util  .  *  ; 
import   com  .  ibutton  .  oc  .  *  ; 
import   com  .  ibutton  .  oc  .  terminal  .  jib  .  *  ; 
import   com  .  ibutton  .  oc  .  JibMultiFactory  ; 
import   com  .  ibutton  .  oc  .  JibMultiListener  ; 
import   com  .  ibutton  .  oc  .  JibMultiEvent  ; 

















public   class   IButtonAnimalProxy   extends   AnimalProxy   implements   AnimalConstants  { 

private   SlotChannel   channel  ; 

private   int  [  ]  rom_id  ; 

private   byte   gender  =  Byte  .  MIN_VALUE  ; 

private   byte   legs  =  Byte  .  MIN_VALUE  ; 

private   String   species  ,  creator  ,  cry  ,  picture  ,  sound  ; 


private   boolean   removed  =  false  ; 








public   static   void   start  (  final   Farmyard   yard  )  { 
try  { 
SmartCard  .  start  (  )  ; 
CardTerminalRegistry   registry  =  CardTerminalRegistry  .  getRegistry  (  )  ; 
registry  .  setPollInterval  (  200  )  ; 
JibMultiFactory   factory  =  new   JibMultiFactory  (  )  ; 
factory  .  addJiBListener  (  new   JibMultiListener  (  )  { 

public   void   iButtonInserted  (  JibMultiEvent   event  )  { 
Farmyard  .  playSound  (  "inserted.au"  )  ; 
int   slot  =  event  .  getSlotID  (  )  ; 
SlotChannel   channel  =  event  .  getChannel  (  )  ; 
iButtonCardTerminal   terminal  =  (  iButtonCardTerminal  )  channel  .  getCardTerminal  (  )  ; 
int  [  ]  buttonId  =  terminal  .  getiButtonId  (  channel  .  getSlotNumber  (  )  )  ; 
IButtonAnimalProxy   re_ap  =  (  IButtonAnimalProxy  )  AnimalRegistry  .  getReusableAnimalProxy  (  buttonId  )  ; 
if  (  re_ap  ==  null  )  { 
boolean   selected  =  IButtonAnimalProxy  .  selectApplet  (  channel  )  ; 
if  (  selected  )  { 
LocationPanel   lp  =  yard  .  getSelectedLocationPanel  (  )  ; 
AnimalProxy   new_ap  ; 
if  (  lp  !=  null  )  new_ap  =  lp  .  getFactory  (  )  .  createAnimalProxy  (  channel  ,  buttonId  )  ;  else  { 
try  { 
new_ap  =  new   IButtonAnimalProxy  (  channel  ,  buttonId  )  ; 
}  catch  (  java  .  rmi  .  RemoteException   e  )  { 
return  ; 
} 
} 
yard  .  addAnimal  (  new_ap  )  ; 
}  else  { 
System  .  err  .  println  (  "iButton applet \"Animal\" not found"  )  ; 
Farmyard  .  playSound  (  "failure.au"  )  ; 
} 
}  else  { 
re_ap  .  setChannel  (  channel  )  ; 
} 
} 

public   void   iButtonRemoved  (  JibMultiEvent   event  )  { 
Farmyard  .  playSound  (  "removed.au"  )  ; 
} 
}  )  ; 
registry  .  addCTListener  (  factory  )  ; 
}  catch  (  CardTerminalException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  CardServiceException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  OpenCardPropertyLoadingException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 







public   static   void   shutdown  (  )  { 
try  { 
SmartCard  .  shutdown  (  )  ; 
}  catch  (  CardTerminalException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 




protected   IButtonAnimalProxy  (  )  throws   java  .  rmi  .  RemoteException  { 
} 









public   IButtonAnimalProxy  (  SlotChannel   sc  ,  int  [  ]  buttonId  )  throws   java  .  rmi  .  RemoteException  { 
channel  =  sc  ; 
rom_id  =  buttonId  ; 
AnimalRegistry  .  add  (  this  )  ; 
final   CardTerminalRegistry   registry  =  CardTerminalRegistry  .  getRegistry  (  )  ; 
final   JibMultiFactory   factory  =  new   JibMultiFactory  (  )  ; 
factory  .  addJiBListener  (  new   JibMultiListener  (  )  { 

public   void   iButtonInserted  (  JibMultiEvent   event  )  { 
} 

public   void   iButtonRemoved  (  JibMultiEvent   event  )  { 
if  (  channel  .  getSlotNumber  (  )  ==  event  .  getSlotID  (  )  )  { 
Thread   th  =  new   Thread  (  )  { 

public   void   run  (  )  { 
if  (  AnimalRegistry  .  awaitRemove  (  IButtonAnimalProxy  .  this  ,  800  )  )  { 
removed  =  true  ; 
processEvent  (  new   AnimalEvent  (  IButtonAnimalProxy  .  this  ,  42  )  )  ; 
registry  .  removeCTListener  (  factory  )  ; 
} 
} 
}  ; 
try  { 
th  .  start  (  )  ; 
}  catch  (  IllegalThreadStateException   e  )  { 
} 
} 
} 
}  )  ; 
registry  .  addCTListener  (  factory  )  ; 
} 








public   SlotChannel   getChannel  (  )  { 
return   channel  ; 
} 









public   void   setChannel  (  SlotChannel   sc  )  { 
channel  =  sc  ; 
} 









protected   void   processEvent  (  EventObject   event  )  { 
if  (  event   instanceof   AnimalEvent  )  processAnimalEvent  (  (  AnimalEvent  )  event  )  ; 
} 








protected   void   processAnimalEvent  (  AnimalEvent   event  )  { 
for  (  Iterator   e  =  animal_listeners  .  iterator  (  )  ;  e  .  hasNext  (  )  ;  )  { 
AnimalListener   l  =  (  AnimalListener  )  e  .  next  (  )  ; 
l  .  animalRemoved  (  event  )  ; 
} 
} 






public   boolean   isRemoved  (  )  { 
return   removed  ; 
} 








public   boolean   selectApplet  (  )  { 
return   selectApplet  (  "Animal"  )  ; 
} 









public   boolean   selectApplet  (  String   appletName  )  { 
return   selectApplet  (  channel  ,  appletName  )  ; 
} 









public   static   boolean   selectApplet  (  SlotChannel   channel  )  { 
return   selectApplet  (  channel  ,  "Animal"  )  ; 
} 










public   static   synchronized   boolean   selectApplet  (  SlotChannel   channel  ,  String   appletName  )  { 
char  [  ]  appletNameChars  =  appletName  .  toCharArray  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  appletName  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  buffer  .  length  ;  i  ++  )  buffer  [  i  ]  =  (  byte  )  appletNameChars  [  i  ]  ; 
com  .  ibutton  .  oc  .  CommandAPDU   selectAPDU  =  new   com  .  ibutton  .  oc  .  CommandAPDU  (  (  byte  )  SELECT_CLA  ,  (  byte  )  SELECT_INS  ,  (  byte  )  SELECT_P1  ,  (  byte  )  SELECT_P1  ,  buffer  ,  (  byte  )  0  )  ; 
try  { 
ResponseAPDU   response  =  channel  .  sendAPDU  (  selectAPDU  )  ; 
return  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  ; 
}  catch  (  CardTerminalException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
} 





























protected   synchronized   void   whoAreYou  (  )  { 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_WHO_ARE_YOU  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
byte  [  ]  ba  =  response  .  data  (  )  ; 
gender  =  ba  [  0  ]  ; 
legs  =  ba  [  1  ]  ; 
StringTokenizer   st  =  new   StringTokenizer  (  new   String  (  ba  ,  2  ,  ba  .  length  -  2  )  ,  "\0"  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  species  =  st  .  nextToken  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  creator  =  st  .  nextToken  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  cry  =  st  .  nextToken  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  picture  =  st  .  nextToken  (  )  ; 
if  (  st  .  hasMoreTokens  (  )  )  sound  =  st  .  nextToken  (  )  ; 
}  else  { 
System  .  err  .  println  (  "Cannot read who-are-you message from the animal"  )  ; 
} 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
} 
























public   synchronized   void   whereYouAre  (  int   insideTemp  ,  int   outsideTemp  ,  byte   light  ,  String   location  )  { 
try  { 
char  [  ]  locationChars  =  location  .  toCharArray  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  location  .  length  (  )  +  LOCATION_OFFSET  ]  ; 
buffer  [  INDOOR_TEMPERATURE_OFFSET  ]  =  (  byte  )  insideTemp  ; 
buffer  [  OUTDOOR_TEMPERATURE_OFFSET  ]  =  (  byte  )  outsideTemp  ; 
buffer  [  LIGHT_OFFSET  ]  =  light  ; 
for  (  int   i  =  0  ;  i  <  locationChars  .  length  ;  i  ++  )  buffer  [  i  +  LOCATION_OFFSET  ]  =  (  byte  )  locationChars  [  i  ]  ; 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_WHERE_YOU_ARE  ,  buffer  )  ; 
if  (  response  .  sw  (  )  !=  SW_NO_ERROR  )  System  .  err  .  println  (  "Cannot write where-you-are information to the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to send where-you-are infomation to the animal (card)"  )  ; 
} 
} 




































public   byte   feed  (  byte   food  )  { 
byte  [  ]  ba  =  new   byte  [  1  ]  ; 
ba  [  0  ]  =  food  ; 
return   feed  (  ba  )  ; 
} 





































public   synchronized   byte   feed  (  byte  [  ]  food  )  { 
byte   feed_response  =  RESPONSE_NONE  ; 
byte   food_response  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_FEED  ,  food  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
byte  [  ]  ba  =  response  .  data  (  )  ; 
feed_response  =  ba  [  0  ]  ; 
food_response  =  ba  [  1  ]  ; 
}  else  { 
System  .  err  .  println  (  "Cannot read feed-response message from the animal"  )  ; 
} 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to send feed infomation to the animal (card)"  )  ; 
} 
return   feed_response  ; 
} 





public   boolean   isMale  (  )  { 
return   getGender  (  )  ==  ANIMAL_GENDER_MALE  ; 
} 





public   boolean   isFemale  (  )  { 
return  !  isMale  (  )  ; 
} 












public   byte   getGender  (  )  { 
if  (  gender  ==  Byte  .  MIN_VALUE  )  whoAreYou  (  )  ; 
return   gender  ; 
} 





public   byte   getLegs  (  )  { 
if  (  legs  ==  Byte  .  MIN_VALUE  )  whoAreYou  (  )  ; 
return   legs  ; 
} 





public   String   getSpecies  (  )  { 
if  (  species  ==  null  )  whoAreYou  (  )  ; 
return   species  ; 
} 






public   String   getCreator  (  )  { 
if  (  creator  ==  null  )  whoAreYou  (  )  ; 
return   creator  ; 
} 








public   String   getCry  (  )  { 
if  (  cry  ==  null  )  whoAreYou  (  )  ; 
return   cry  ; 
} 












public   String   getPicture  (  )  { 
if  (  picture  ==  null  )  whoAreYou  (  )  ; 
return   picture  ; 
} 












public   String   getSound  (  )  { 
if  (  sound  ==  null  )  whoAreYou  (  )  ; 
return   sound  ; 
} 
















public   synchronized   String   getMessage  (  )  { 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_MESSAGE  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  return   new   String  (  response  .  data  (  )  )  ;  else   System  .  err  .  println  (  "Cannot read the message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return  ""  ; 
} 

private   String   last_location_cache  =  null  ; 













public   synchronized   String   getLastLocation  (  )  { 
if  (  last_location_cache  !=  null  )  return   last_location_cache  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_LAST_LOCATION  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
if  (  response  .  data  (  )  !=  null  )  { 
String   result  =  new   String  (  response  .  data  (  )  )  ; 
last_location_cache  =  result  ; 
return   result  ; 
}  else   return  ""  ; 
}  else   System  .  err  .  println  (  "Cannot read last-location message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return  ""  ; 
} 















public   synchronized   byte   getMode  (  )  { 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_MODE  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
byte  [  ]  ba  =  response  .  data  (  )  ; 
return   ba  [  0  ]  ; 
}  else   System  .  err  .  println  (  "Cannot read mode from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return   MODE_NORMAL  ; 
} 

private   List   op_names  =  null  ; 

private   List   op_ids  =  null  ; 































public   synchronized   List   getOperations  (  )  { 
if  (  op_names  !=  null  )  return   op_names  ; 
op_names  =  new   ArrayList  (  )  ; 
op_ids  =  new   ArrayList  (  )  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_OPERATIONS  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
byte  [  ]  ba  =  response  .  data  (  )  ; 
int   i  =  0  ; 
while  (  i  <  ba  .  length  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
while  (  i  <  ba  .  length  &&  ba  [  i  ]  !=  0  )  { 
s  .  append  (  (  char  )  ba  [  i  ++  ]  )  ; 
} 
op_names  .  add  (  s  .  toString  (  )  )  ; 
if  (  i  ++  <  ba  .  length  )  op_ids  .  add  (  new   Byte  (  ba  [  i  ++  ]  )  )  ; 
} 
}  else  { 
System  .  err  .  println  (  "Cannot read the operations from the animal"  )  ; 
} 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return   op_names  ; 
} 




















public   List   getOperationIds  (  )  { 
if  (  op_ids  ==  null  )  getOperations  (  )  ; 
return   op_ids  ; 
} 

















public   synchronized   void   performOperation  (  byte   operation_id  )  { 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_OP  ,  operation_id  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
return  ; 
}  else  { 
System  .  err  .  println  (  "Cannot perform animal-specific operation"  )  ; 
} 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
} 












public   int  [  ]  getId  (  )  { 
return   rom_id  ; 
} 










protected   void   setId  (  int  [  ]  id  )  { 
rom_id  =  id  ; 
} 

private   String   animal_name_cache  =  null  ; 












public   synchronized   String   getAnimalName  (  )  { 
if  (  animal_name_cache  !=  null  )  return   animal_name_cache  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_NAME  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
if  (  response  .  data  (  )  !=  null  )  { 
animal_name_cache  =  new   String  (  response  .  data  (  )  )  ; 
return   new   String  (  response  .  data  (  )  )  ; 
}  else  { 
animal_name_cache  =  ""  ; 
return  ""  ; 
} 
}  else   System  .  err  .  println  (  "Cannot read animal-name message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return   null  ; 
} 















public   synchronized   void   setAnimalName  (  String   str  )  { 
animal_name_cache  =  null  ; 
byte  [  ]  buffer  =  new   byte  [  str  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  buffer  .  length  ;  i  ++  )  buffer  [  i  ]  =  (  byte  )  str  .  charAt  (  i  )  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_SET_NAME  ,  buffer  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  animal_name_cache  =  str  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to send animal-name infomation to the animal (card)"  )  ; 
} 
} 

private   String   owner_name_cache  =  null  ; 












public   synchronized   String   getOwnerName  (  )  { 
if  (  owner_name_cache  !=  null  )  return   owner_name_cache  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_OWNER_NAME  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
if  (  response  .  data  (  )  !=  null  )  { 
owner_name_cache  =  new   String  (  response  .  data  (  )  )  ; 
return   new   String  (  response  .  data  (  )  )  ; 
}  else  { 
owner_name_cache  =  ""  ; 
return  ""  ; 
} 
}  else   System  .  err  .  println  (  "Cannot read owner-name message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return  ""  ; 
} 















public   synchronized   void   setOwnerName  (  String   str  )  { 
owner_name_cache  =  null  ; 
byte  [  ]  buffer  =  new   byte  [  str  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  buffer  .  length  ;  i  ++  )  buffer  [  i  ]  =  (  byte  )  str  .  charAt  (  i  )  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_OWNER_SET_NAME  ,  buffer  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  owner_name_cache  =  str  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to send owner-name infomation to the animal (card)"  )  ; 
} 
} 

private   String   owner_email_cache  =  null  ; 












public   synchronized   String   getOwnerEmail  (  )  { 
if  (  owner_email_cache  !=  null  )  return   owner_email_cache  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_OWNER_EMAIL  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
if  (  response  .  data  (  )  !=  null  )  { 
owner_email_cache  =  new   String  (  response  .  data  (  )  )  ; 
return   new   String  (  response  .  data  (  )  )  ; 
}  else  { 
owner_email_cache  =  ""  ; 
return  ""  ; 
} 
}  else   System  .  err  .  println  (  "Cannot read owner-email message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return  ""  ; 
} 
















public   synchronized   void   setOwnerEmail  (  String   str  )  { 
owner_email_cache  =  null  ; 
byte  [  ]  buffer  =  new   byte  [  str  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  buffer  .  length  ;  i  ++  )  buffer  [  i  ]  =  (  byte  )  str  .  charAt  (  i  )  ; 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_OWNER_SET_EMAIL  ,  buffer  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  owner_email_cache  =  str  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to send owner-email infomation to the animal (card)"  )  ; 
} 
} 






private   int   getInt  (  byte  [  ]  ba  )  { 
return  (  (  (  (  (  int  )  ba  [  3  ]  )  <<  24  )  &  0xFF000000  )  |  (  (  (  (  int  )  ba  [  2  ]  )  <<  16  )  &  0x00FF0000  )  |  (  (  (  (  int  )  ba  [  1  ]  )  <<  8  )  &  0x0000FF00  )  |  (  (  (  int  )  ba  [  0  ]  )  &  0x000000FF  )  )  ; 
} 






public   synchronized   int   getClock  (  )  { 
try  { 
ResponseAPDU   response  =  sendCommandAPDU  (  ANIMAL_CLA  ,  ANIMAL_CLOCK  )  ; 
if  (  response  .  sw  (  )  ==  SW_NO_ERROR  )  { 
if  (  response  .  data  (  )  !=  null  )  return   getInt  (  response  .  data  (  )  )  ;  else   return   0  ; 
}  else   System  .  err  .  println  (  "Cannot read clock message from the animal"  )  ; 
}  catch  (  CardTerminalException   e  )  { 
System  .  err  .  println  (  "Unable to communicate with the animal (card)"  )  ; 
} 
return   0  ; 
} 













protected   ResponseAPDU   sendCommandAPDU  (  byte   CLA  ,  byte   INS  )  throws   CardTerminalException  { 
com  .  ibutton  .  oc  .  CommandAPDU   animalAPDU  =  new   com  .  ibutton  .  oc  .  CommandAPDU  (  CLA  ,  INS  ,  (  byte  )  0  ,  (  byte  )  0  )  ; 
return   channel  .  sendAPDU  (  animalAPDU  )  ; 
} 














protected   ResponseAPDU   sendCommandAPDU  (  byte   CLA  ,  byte   INS  ,  byte  [  ]  buffer  )  throws   CardTerminalException  { 
com  .  ibutton  .  oc  .  CommandAPDU   animalAPDU  =  new   com  .  ibutton  .  oc  .  CommandAPDU  (  CLA  ,  INS  ,  (  byte  )  0  ,  (  byte  )  0  ,  buffer  )  ; 
return   channel  .  sendAPDU  (  animalAPDU  )  ; 
} 
} 

