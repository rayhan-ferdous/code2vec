package   genericirc  .  irccore  ; 

import   java  .  util  .  EventObject  ; 






public   class   NewMessageEvent   extends   EventObject  { 

private   String   hostmask  ; 

private   String   user  ; 

private   String   channel  ; 

private   String   message  ; 










public   NewMessageEvent  (  Object   source  ,  String   hostmask  ,  String   user  ,  String   channel  ,  String   message  )  { 
super  (  source  )  ; 
this  .  hostmask  =  hostmask  ; 
this  .  user  =  user  ; 
this  .  channel  =  channel  ; 
this  .  message  =  message  ; 
} 




public   String   getHostmask  (  )  { 
return   hostmask  ; 
} 




public   String   getUser  (  )  { 
return   user  ; 
} 




public   String   getChannel  (  )  { 
return   channel  ; 
} 




public   String   getMessage  (  )  { 
return   message  ; 
} 
} 

