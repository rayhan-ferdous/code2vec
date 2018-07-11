package   de  .  teamwork  .  irc  .  msgutils  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  StringTokenizer  ; 
import   de  .  teamwork  .  irc  .  *  ; 





























public   class   JoinMessage  { 




private   JoinMessage  (  )  { 
} 












public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  String   channel  )  { 
String  [  ]  args  =  new   String  [  ]  {  channel  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_JOIN  ,  args  )  ; 
} 













public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  String   channel  ,  String   key  )  { 
String  [  ]  args  =  new   String  [  ]  {  channel  ,  key  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_JOIN  ,  args  )  ; 
} 












public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  ArrayList   channels  )  { 
StringBuffer   chn  =  new   StringBuffer  (  (  String  )  channels  .  get  (  0  )  )  ; 
for  (  int   i  =  1  ;  i  <  channels  .  size  (  )  ;  i  ++  )  chn  .  append  (  ","  +  (  String  )  channels  .  get  (  i  )  )  ; 
String  [  ]  args  =  new   String  [  ]  {  chn  .  toString  (  )  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_JOIN  ,  args  )  ; 
} 













public   static   IRCMessage   createMessage  (  String   msgNick  ,  String   msgUser  ,  String   msgHost  ,  ArrayList   channels  ,  ArrayList   keys  )  { 
StringBuffer   chn  =  new   StringBuffer  (  (  String  )  channels  .  get  (  0  )  )  ; 
StringBuffer   key  =  new   StringBuffer  (  (  String  )  keys  .  get  (  0  )  )  ; 
for  (  int   i  =  1  ;  i  <  channels  .  size  (  )  ;  i  ++  )  chn  .  append  (  ","  +  (  String  )  channels  .  get  (  i  )  )  ; 
for  (  int   i  =  1  ;  i  <  keys  .  size  (  )  ;  i  ++  )  key  .  append  (  ","  +  (  String  )  keys  .  get  (  i  )  )  ; 
String  [  ]  args  =  new   String  [  ]  {  chn  .  toString  (  )  ,  key  .  toString  (  )  }  ; 
return   new   IRCMessage  (  msgNick  ,  msgUser  ,  msgHost  ,  IRCMessageTypes  .  MSG_JOIN  ,  args  )  ; 
} 








public   static   boolean   containsChannelList  (  IRCMessage   msg  )  { 
return  (  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  )  .  indexOf  (  ","  )  >=  0  ; 
} 






public   static   String   getChannel  (  IRCMessage   msg  )  { 
return  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  ; 
} 






public   static   ArrayList   getChannels  (  IRCMessage   msg  )  { 
StringTokenizer   t  =  new   StringTokenizer  (  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  0  )  ,  ","  ,  false  )  ; 
ArrayList   list  =  new   ArrayList  (  1  )  ; 
while  (  t  .  hasMoreTokens  (  )  )  list  .  add  (  t  .  nextToken  (  )  )  ; 
return   list  ; 
} 






public   static   String   getKey  (  IRCMessage   msg  )  { 
try  { 
return  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  1  )  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return  ""  ; 
} 
} 







public   static   ArrayList   getKeys  (  IRCMessage   msg  )  { 
ArrayList   list  =  new   ArrayList  (  1  )  ; 
try  { 
StringTokenizer   t  =  new   StringTokenizer  (  (  String  )  msg  .  getArgs  (  )  .  elementAt  (  1  )  ,  ","  ,  false  )  ; 
while  (  t  .  hasMoreTokens  (  )  )  list  .  add  (  t  .  nextToken  (  )  )  ; 
return   list  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
return   list  ; 
} 
} 
} 

