package   enyarok  .  server  .  entity  ; 

import   enyarok  .  common  .  NotificationType  ; 
import   enyarok  .  server  .  action  .  admin  .  AdministrationAction  ; 
import   enyarok  .  server  .  entity  .  rpevents  .  PrivateTextEvent  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   marauroa  .  common  .  Configuration  ; 
import   marauroa  .  common  .  game  .  Definition  ; 
import   marauroa  .  common  .  game  .  Definition  .  Type  ; 
import   marauroa  .  common  .  game  .  RPClass  ; 
import   marauroa  .  common  .  game  .  RPObject  ; 
import   marauroa  .  common  .  io  .  UnicodeSupportingInputStreamReader  ; 
import   org  .  apache  .  log4j  .  Logger  ; 





public   class   Player   extends   Entity  { 

private   static   Logger   logger  =  Logger  .  getLogger  (  Player  .  class  )  ; 




private   int   age  ; 

private   String   name  ; 


private   static   List  <  String  >  adminNames  ; 






private   String   lastPrivateChatterName  ; 

public   Player  (  final   RPObject   object  )  { 
super  (  object  )  ; 
setRPClass  (  "player"  )  ; 
setSize  (  1  ,  1  )  ; 
update  (  )  ; 
} 

public   static   void   generateRPClass  (  )  { 
final   RPClass   player  =  new   RPClass  (  "player"  )  ; 
player  .  isA  (  "entity"  )  ; 
player  .  addAttribute  (  "name"  ,  Type  .  STRING  )  ; 
player  .  addAttribute  (  "text"  ,  Type  .  LONG_STRING  ,  Definition  .  VOLATILE  )  ; 
player  .  addRPEvent  (  "private_text"  ,  Definition  .  PRIVATE  )  ; 
player  .  addAttribute  (  "age"  ,  Type  .  INT  )  ; 
player  .  addAttribute  (  "admin"  ,  Type  .  FLAG  )  ; 
player  .  addAttribute  (  "adminlevel"  ,  Type  .  INT  )  ; 
player  .  addAttribute  (  "invisible"  ,  Type  .  FLAG  ,  Definition  .  HIDDEN  )  ; 
player  .  addAttribute  (  "ghostmode"  ,  Type  .  FLAG  )  ; 
player  .  addAttribute  (  "teleclickmode"  ,  Type  .  FLAG  )  ; 
} 







public   String   getLastPrivateChatter  (  )  { 
return   lastPrivateChatterName  ; 
} 






public   String   getName  (  )  { 
return   name  ; 
} 

@  Override 
public   void   update  (  )  { 
super  .  update  (  )  ; 
if  (  has  (  "age"  )  )  { 
age  =  getInt  (  "age"  )  ; 
} 
if  (  has  (  "name"  )  )  { 
name  =  this  .  get  (  "name"  )  ; 
} 
} 

public   static   Player   createEmptyZeroLevelPlayer  (  final   String   characterName  )  { 
final   Player   player  =  new   Player  (  new   RPObject  (  )  )  ; 
player  .  setID  (  RPObject  .  INVALID_ID  )  ; 
player  .  put  (  "name"  ,  characterName  )  ; 
player  .  update  (  )  ; 
return   player  ; 
} 







public   void   sendPrivateText  (  final   String   text  )  { 
sendPrivateText  (  NotificationType  .  PRIVMSG  ,  text  )  ; 
} 









public   void   sendPrivateText  (  final   NotificationType   type  ,  final   String   text  )  { 
addEvent  (  new   PrivateTextEvent  (  type  ,  text  )  )  ; 
this  .  notifyWorldAboutChanges  (  )  ; 
} 







public   void   setLastPrivateChatter  (  final   String   lastPrivateChatterName  )  { 
this  .  lastPrivateChatterName  =  lastPrivateChatterName  ; 
} 







public   static   void   readAdminsFromFile  (  final   Player   player  )  { 
if  (  adminNames  ==  null  )  { 
adminNames  =  new   LinkedList  <  String  >  (  )  ; 
final   String   adminFilename  =  "data/conf/admins.list"  ; 
try  { 
final   InputStream   is  =  player  .  getClass  (  )  .  getClassLoader  (  )  .  getResourceAsStream  (  adminFilename  )  ; 
if  (  is  ==  null  )  { 
logger  .  info  (  "data/conf/admins.list does not exist."  )  ; 
}  else  { 
final   BufferedReader   in  =  new   BufferedReader  (  new   UnicodeSupportingInputStreamReader  (  is  )  )  ; 
try  { 
String   line  ; 
while  (  (  line  =  in  .  readLine  (  )  )  !=  null  )  { 
adminNames  .  add  (  line  )  ; 
} 
}  catch  (  final   Exception   e  )  { 
logger  .  error  (  "Error loading admin names from: "  +  adminFilename  ,  e  )  ; 
} 
in  .  close  (  )  ; 
} 
}  catch  (  final   Exception   e  )  { 
logger  .  error  (  "Error loading admin names from: "  +  adminFilename  ,  e  )  ; 
} 
} 
if  (  adminNames  .  contains  (  player  .  getName  (  )  )  )  { 
player  .  setAdminLevel  (  AdministrationAction  .  REQUIRED_ADMIN_LEVEL_FOR_SUPER  )  ; 
} 
} 







public   int   getAdminLevel  (  )  { 
if  (  !  has  (  "adminlevel"  )  )  { 
return   0  ; 
} 
return   getInt  (  "adminlevel"  )  ; 
} 







public   void   setAdminLevel  (  final   int   adminlevel  )  { 
put  (  "adminlevel"  ,  adminlevel  )  ; 
} 









public   static   void   welcome  (  final   Player   player  )  { 
String   msg  =  "This release is EXPERIMENTAL. Need help? #http://enyarok.sourceforge.net/wiki/index.php?title=AskForHelp - please report problems, suggestions and bugs. Remember to keep your password completely secret, never tell to another friend, player, or admin."  ; 
try  { 
final   Configuration   config  =  Configuration  .  getConfiguration  (  )  ; 
if  (  config  .  has  (  "server_welcome"  )  )  { 
msg  =  config  .  get  (  "server_welcome"  )  ; 
if  (  msg  .  startsWith  (  "http://"  )  )  { 
final   URL   url  =  new   URL  (  msg  )  ; 
HttpURLConnection  .  setFollowRedirects  (  false  )  ; 
final   HttpURLConnection   connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
final   BufferedReader   br  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
msg  =  br  .  readLine  (  )  ; 
br  .  close  (  )  ; 
connection  .  disconnect  (  )  ; 
} 
} 
}  catch  (  final   Exception   e  )  { 
logger  .  warn  (  "Can't read server_welcome from marauroa.ini"  ,  e  )  ; 
} 
if  (  msg  !=  null  )  { 
player  .  sendPrivateText  (  msg  )  ; 
} 
} 

public   String   getTitle  (  )  { 
return   get  (  "name"  )  ; 
} 

public   int   getLevel  (  )  { 
return   0  ; 
} 
} 

