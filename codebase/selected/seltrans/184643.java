package   com  .  tms  .  webservices  .  applications  .  xtvd  ; 

import   com  .  tms  .  webservices  .  applications  .  datatypes  .  AbstractDataType  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  Date  ; 











public   class   Map   extends   AbstractDataType  { 





private   int   station  =  0  ; 








private   String   channel  ; 





private   int   channelMinor  =  0  ; 





private   Date   from  =  null  ; 





private   Date   to  =  null  ; 






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











public   Map  (  int   station  ,  String   channel  ,  int   channelMinor  ,  Date   from  ,  Date   to  )  { 
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






public   final   Date   getFrom  (  )  { 
return   from  ; 
} 






public   final   void   setFrom  (  Date   from  )  { 
this  .  from  =  from  ; 
} 






public   final   Date   getTo  (  )  { 
return   to  ; 
} 






public   final   void   setTo  (  Date   to  )  { 
this  .  to  =  to  ; 
} 
} 

