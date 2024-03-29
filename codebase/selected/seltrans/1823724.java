package   net  .  sf  .  xtvdclient  .  xtvd  .  datatypes  ; 











public   class   Map   extends   AbstractDataType  { 





private   int   station  =  0  ; 








private   String   channel  ; 





private   int   channelMinor  =  0  ; 





private   XtvdDate   from  =  null  ; 





private   XtvdDate   to  =  null  ; 






public   Map  (  )  { 
super  (  )  ; 
} 








public   Map  (  int   station  ,  String   channel  )  { 
this  (  )  ; 
setStation  (  station  )  ; 
setChannel  (  channel  )  ; 
} 









public   Map  (  int   station  ,  String   channel  ,  int   channelMinor  )  { 
this  (  )  ; 
setStation  (  station  )  ; 
setChannel  (  channel  )  ; 
setChannelMinor  (  channelMinor  )  ; 
} 











public   Map  (  int   station  ,  String   channel  ,  int   channelMinor  ,  XtvdDate   from  ,  XtvdDate   to  )  { 
this  (  station  ,  channel  )  ; 
setChannelMinor  (  channelMinor  )  ; 
setFrom  (  from  )  ; 
setTo  (  to  )  ; 
} 








public   String   toString  (  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  64  )  ; 
buffer  .  append  (  "<map station='"  )  .  append  (  station  )  ; 
buffer  .  append  (  "' channel='"  )  .  append  (  channel  )  ; 
if  (  channelMinor  !=  0  )  { 
buffer  .  append  (  "' channelMinor='"  )  .  append  (  channelMinor  )  ; 
} 
if  (  from  !=  null  )  { 
buffer  .  append  (  "' from='"  )  .  append  (  from  .  toString  (  )  )  ; 
} 
if  (  to  !=  null  )  { 
buffer  .  append  (  "' to='"  )  .  append  (  to  .  toString  (  )  )  ; 
} 
buffer  .  append  (  "'/>"  )  ; 
return   buffer  .  toString  (  )  ; 
} 






public   final   int   getStation  (  )  { 
return   station  ; 
} 






public   final   void   setStation  (  int   station  )  { 
this  .  station  =  station  ; 
} 






public   final   String   getChannel  (  )  { 
return   channel  ; 
} 






public   final   void   setChannel  (  String   channel  )  { 
this  .  channel  =  channel  ; 
} 






public   final   int   getChannelMinor  (  )  { 
return   channelMinor  ; 
} 






public   final   void   setChannelMinor  (  int   channelMinor  )  { 
this  .  channelMinor  =  channelMinor  ; 
} 






public   final   XtvdDate   getFrom  (  )  { 
return   from  ; 
} 






public   final   void   setFrom  (  XtvdDate   from  )  { 
this  .  from  =  from  ; 
} 






public   final   XtvdDate   getTo  (  )  { 
return   to  ; 
} 






public   final   void   setTo  (  XtvdDate   to  )  { 
this  .  to  =  to  ; 
} 
} 

