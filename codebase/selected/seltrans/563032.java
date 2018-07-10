package   com  .  shimari  .  bot  ; 

import   org  .  schwering  .  irc  .  lib  .  *  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  logging  .  Level  ; 




public   final   class   IRC_Message   implements   Message  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  IRC_Message  .  class  .  getName  (  )  )  ; 

static   final   boolean   ADDRESSED  =  true  ; 

static   final   boolean   UNADDRESSED  =  false  ; 

private   final   IRCUser   my_sender  ; 

private   final   String   my_channel  ; 

private   final   boolean   my_isAddressed  ; 

private   final   String   my_message  ; 

private   final   IRC_Connection   my_conn  ; 

IRC_Message  (  IRC_Connection   conn  ,  IRCUser   sender  ,  String   channel  ,  boolean   isAddressed  ,  String   message  )  { 
this  .  my_conn  =  conn  ; 
this  .  my_sender  =  sender  ; 
this  .  my_channel  =  channel  ; 
this  .  my_isAddressed  =  isAddressed  ; 
this  .  my_message  =  message  ; 
} 




public   String   toString  (  )  { 
return  "IRC_Message("  +  my_conn  +  ","  +  my_sender  .  getNick  (  )  +  "="  +  my_sender  .  getUsername  (  )  +  "@"  +  my_sender  .  getHost  (  )  +  ","  +  my_channel  +  ","  +  (  my_isAddressed  ?  "Addressed"  :  "Unaddressed"  )  +  ","  +  my_message  +  ")"  ; 
} 




public   boolean   isPublic  (  )  { 
return  (  my_channel  !=  null  )  ; 
} 




public   boolean   isAddressed  (  )  { 
return   my_isAddressed  ; 
} 




public   String   getFromNick  (  )  { 
return   my_sender  .  getNick  (  )  ; 
} 




public   String   getFromUser  (  )  { 
return   my_sender  .  getUsername  (  )  ; 
} 




public   String   getFromHost  (  )  { 
return   my_sender  .  getHost  (  )  ; 
} 




public   String   getMessage  (  )  { 
return   my_message  ; 
} 




public   String   getChannel  (  )  { 
return   my_channel  ; 
} 




public   String   getToNick  (  )  { 
return   my_conn  .  getNick  (  )  ; 
} 




public   String   getToServer  (  )  { 
return   my_conn  .  getServer  (  )  ; 
} 




public   void   sendReply  (  String  [  ]  message  )  { 
for  (  int   i  =  0  ;  i  <  message  .  length  ;  i  ++  )  { 
sendReply  (  message  [  i  ]  )  ; 
} 
} 




public   void   sendReply  (  String   message  )  { 
if  (  logger  .  isLoggable  (  Level  .  FINER  )  )  logger  .  finer  (  this  +  " sendReply(message)"  )  ; 
String   fromNick  =  my_sender  .  getNick  (  )  ; 
if  (  isPublic  (  )  )  { 
my_conn  .  sendMessage  (  my_channel  ,  message  )  ; 
}  else   if  (  fromNick  !=  null  )  { 
my_conn  .  sendMessage  (  fromNick  ,  message  )  ; 
}  else  { 
logger  .  warning  (  "Unable to send reply! Null nick: "  +  my_sender  )  ; 
} 
} 

public   Connection   getConnection  (  )  { 
return   my_conn  ; 
} 
} 

