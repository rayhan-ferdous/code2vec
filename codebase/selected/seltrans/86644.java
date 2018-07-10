package   de  .  teamwork  .  irc  .  msgutils  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  StringTokenizer  ; 
import   de  .  teamwork  .  irc  .  *  ; 

























public   class   NamesMessage  { 




private   NamesMessage  (  )  { 
} 











public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  )  { 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_NAMES  ,  null  )  ; 
} 












public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  String   channel  )  { 
String  [  ]  args  =  new   String  [  ]  {  channel  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_NAMES  ,  args  )  ; 
} 













public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  String   channel  ,  String   target  )  { 
String  [  ]  args  =  new   String  [  ]  {  channel  ,  target  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_NAMES  ,  args  )  ; 
} 












public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  ArrayList   channels  )  { 
StringBuffer   chn  =  new   StringBuffer  (  (  String  )  channels  .  get  (  0  )  )  ; 
for  (  int   i  =  1  ;  i  <  channels  .  size  (  )  ;  i  ++  )  chn  .  append  (  ","  +  (  String  )  channels  .  get  (  i  )  )  ; 
String  [  ]  args  =  new   String  [  ]  {  chn  .  toString  (  )  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_NAMES  ,  args  )  ; 
} 













public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  ArrayList   channels  ,  String   target  )  { 
StringBuffer   chn  =  new   StringBuffer  (  (  String  )  channels  .  get  (  0  )  )  ; 
for  (  int   i  =  1  ;  i  <  channels  .  size  (  )  ;  i  ++  )  chn  .  append  (  ","  +  (  String  )  channels  .  get  (  i  )  )  ; 
String  [  ]  args  =  new   String  [  ]  {  chn  .  toString  (  )  ,  target  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_NAMES  ,  args  )  ; 
} 








public   static   boolean   containsChannelList  (  IRCMessage   msg  )  { 
try  { 
return  (  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  )  .  indexOf  (  ","  )  >=  0  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return   false  ; 
} 
} 






public   static   String   getChannel  (  IRCMessage   msg  )  { 
try  { 
return  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return  ""  ; 
} 
} 







public   static   ArrayList   getChannels  (  IRCMessage   msg  )  { 
ArrayList   list  =  new   ArrayList  (  1  )  ; 
try  { 
StringTokenizer   t  =  new   StringTokenizer  (  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  ,  ","  ,  false  )  ; 
while  (  t  .  hasMoreTokens  (  )  )  list  .  add  (  t  .  nextToken  (  )  )  ; 
return   list  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return   list  ; 
} 
} 






public   static   String   getTarget  (  IRCMessage   msg  )  { 
try  { 
return  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  1  )  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return  ""  ; 
} 
} 
} 

