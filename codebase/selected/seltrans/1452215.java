package   org  .  rsbot  .  script  .  methods  ; 

import   org  .  rsbot  .  script  .  wrappers  .  RSComponent  ; 






public   class   ClanChat   extends   MethodProvider  { 

public   static   final   int   INTERFACE_CLAN_CHAT  =  589  ; 

public   static   final   int   INTERFACE_CLAN_CHAT_CHANNEL_NAME  =  0  ; 

public   static   final   int   INTERFACE_CLAN_CHAT_CHANNEL_OWNER  =  1  ; 

public   static   final   int   INTERFACE_CLAN_CHAT_CHANNEL_USERS_LIST  =  5  ; 

public   static   final   int   INTERFACE_CLAN_CHAT_BUTTON_JOIN  =  11  ; 

public   static   final   int   INTERFACE_JOIN_CLAN_CHAT  =  752  ; 

public   static   final   int   INTERFACE_JOIN_CLAN_CHAT_LAST_CHANNEL  =  3  ; 

private   String   lastChannel  =  null  ; 

ClanChat  (  final   MethodContext   ctx  )  { 
super  (  ctx  )  ; 
} 








public   boolean   join  (  String   channel  )  { 
methods  .  game  .  openTab  (  Game  .  TAB_CLAN  )  ; 
if  (  isInChannel  (  )  )  { 
if  (  !  leave  (  )  )  { 
return   false  ; 
} 
} 
methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_BUTTON_JOIN  )  .  doClick  (  )  ; 
sleep  (  random  (  200  ,  400  )  )  ; 
if  (  methods  .  interfaces  .  get  (  INTERFACE_JOIN_CLAN_CHAT  )  .  isValid  (  )  )  { 
lastChannel  =  methods  .  interfaces  .  getComponent  (  INTERFACE_JOIN_CLAN_CHAT  ,  INTERFACE_JOIN_CLAN_CHAT_LAST_CHANNEL  )  .  getText  (  )  ; 
methods  .  keyboard  .  sendText  (  channel  ,  true  )  ; 
sleep  (  random  (  1550  ,  1800  )  )  ; 
if  (  isInChannel  (  )  )  { 
lastChannel  =  channel  ; 
return   true  ; 
} 
} 
return   false  ; 
} 






public   boolean   leave  (  )  { 
methods  .  game  .  openTab  (  Game  .  TAB_CLAN  )  ; 
if  (  isInChannel  (  )  )  { 
lastChannel  =  getChannelOwner  (  )  ; 
methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_BUTTON_JOIN  )  .  doClick  (  )  ; 
sleep  (  random  (  650  ,  900  )  )  ; 
return   isInChannel  (  )  ; 
} 
return   true  ; 
} 






public   String   getLastChannel  (  )  { 
if  (  isInChannel  (  )  )  { 
lastChannel  =  getChannelOwner  (  )  ; 
} 
return   lastChannel  ; 
} 






public   String   getChannelOwner  (  )  { 
String   owner  =  methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_CHANNEL_OWNER  )  .  getText  (  )  ; 
return   owner  .  substring  (  owner  .  lastIndexOf  (  58  )  +  2  )  .  replaceAll  (  "<.+>"  ,  ""  )  ; 
} 






public   String   getChannelName  (  )  { 
String   name  =  methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_CHANNEL_NAME  )  .  getText  (  )  ; 
return   name  .  substring  (  name  .  lastIndexOf  (  58  )  +  2  )  .  replaceAll  (  "<.+>"  ,  ""  )  ; 
} 






public   String  [  ]  getChannelUsers  (  )  { 
if  (  isInChannel  (  )  )  { 
java  .  util  .  ArrayList  <  String  >  users  =  new   java  .  util  .  ArrayList  <  String  >  (  )  ; 
RSComponent  [  ]  comps  =  methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_CHANNEL_USERS_LIST  )  .  getComponents  (  )  ; 
for  (  RSComponent   comp  :  comps  )  { 
String   text  =  comp  .  getText  (  )  ; 
if  (  text  ==  null  ||  text  .  isEmpty  (  )  )  { 
continue  ; 
}  else   if  (  comp  .  getRelativeX  (  )  >=  36  )  { 
continue  ; 
} 
String   user  =  text  .  trim  (  )  ; 
if  (  user  .  endsWith  (  "..."  )  )  { 
String  [  ]  actions  =  comp  .  getActions  (  )  ; 
if  (  actions  !=  null  )  { 
for  (  String   action  :  actions  )  { 
if  (  action  !=  null  &&  action  .  toLowerCase  (  )  .  matches  (  "^(add|remove)"  )  )  { 
user  =  action  .  substring  (  action  .  indexOf  (  32  )  +  1  )  ; 
user  =  user  .  substring  (  user  .  indexOf  (  32  )  )  .  trim  (  )  ; 
break  ; 
} 
} 
} 
} 
users  .  add  (  user  )  ; 
} 
return   users  .  toArray  (  new   String  [  users  .  size  (  )  ]  )  ; 
} 
return   null  ; 
} 






public   boolean   isInChannel  (  )  { 
String   text  =  methods  .  interfaces  .  getComponent  (  INTERFACE_CLAN_CHAT  ,  INTERFACE_CLAN_CHAT_BUTTON_JOIN  )  .  getText  (  )  ; 
return   text  .  toLowerCase  (  )  .  contains  (  "leave chat"  )  ; 
} 
} 

