package   org  .  asteriskjava  .  manager  .  action  ; 








public   class   MonitorAction   extends   AbstractManagerAction  { 




private   static   final   long   serialVersionUID  =  6840975934278794758L  ; 

private   String   channel  ; 

private   String   file  ; 

private   String   format  ; 

private   Boolean   mix  ; 




public   MonitorAction  (  )  { 
super  (  )  ; 
} 











public   MonitorAction  (  String   channel  ,  String   file  )  { 
super  (  )  ; 
this  .  channel  =  channel  ; 
this  .  file  =  file  ; 
} 












public   MonitorAction  (  String   channel  ,  String   file  ,  String   format  )  { 
super  (  )  ; 
this  .  channel  =  channel  ; 
this  .  file  =  file  ; 
this  .  format  =  format  ; 
} 













public   MonitorAction  (  String   channel  ,  String   file  ,  String   format  ,  Boolean   mix  )  { 
super  (  )  ; 
this  .  channel  =  channel  ; 
this  .  file  =  file  ; 
this  .  format  =  format  ; 
this  .  mix  =  mix  ; 
} 




@  Override 
public   String   getAction  (  )  { 
return  "Monitor"  ; 
} 




public   String   getChannel  (  )  { 
return   channel  ; 
} 





public   void   setChannel  (  String   channel  )  { 
this  .  channel  =  channel  ; 
} 




public   String   getFile  (  )  { 
return   file  ; 
} 






public   void   setFile  (  String   file  )  { 
this  .  file  =  file  ; 
} 




public   String   getFormat  (  )  { 
return   format  ; 
} 





public   void   setFormat  (  String   format  )  { 
this  .  format  =  format  ; 
} 





public   Boolean   getMix  (  )  { 
return   mix  ; 
} 





public   void   setMix  (  Boolean   mix  )  { 
this  .  mix  =  mix  ; 
} 
} 

