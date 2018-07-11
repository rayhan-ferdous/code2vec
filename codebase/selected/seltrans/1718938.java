package   org  .  asteriskjava  .  manager  .  action  ; 
















public   class   RedirectAction   extends   AbstractManagerAction  { 




static   final   long   serialVersionUID  =  1869279324159418150L  ; 

private   String   channel  ; 

private   String   extraChannel  ; 

private   String   exten  ; 

private   String   context  ; 

private   Integer   priority  ; 

private   String   extraExten  ; 

private   String   extraContext  ; 

private   Integer   extraPriority  ; 




public   RedirectAction  (  )  { 
} 











public   RedirectAction  (  String   channel  ,  String   context  ,  String   exten  ,  Integer   priority  )  { 
this  .  channel  =  channel  ; 
this  .  context  =  context  ; 
this  .  exten  =  exten  ; 
this  .  priority  =  priority  ; 
} 
















public   RedirectAction  (  String   channel  ,  String   extraChannel  ,  String   context  ,  String   exten  ,  Integer   priority  )  { 
this  .  channel  =  channel  ; 
this  .  extraChannel  =  extraChannel  ; 
this  .  context  =  context  ; 
this  .  exten  =  exten  ; 
this  .  priority  =  priority  ; 
} 


















public   RedirectAction  (  String   channel  ,  String   extraChannel  ,  String   context  ,  String   exten  ,  Integer   priority  ,  String   extraContext  ,  String   extraExten  ,  Integer   extraPriority  )  { 
this  .  channel  =  channel  ; 
this  .  extraChannel  =  extraChannel  ; 
this  .  context  =  context  ; 
this  .  exten  =  exten  ; 
this  .  priority  =  priority  ; 
this  .  extraContext  =  extraContext  ; 
this  .  extraExten  =  extraExten  ; 
this  .  extraPriority  =  extraPriority  ; 
} 




@  Override 
public   String   getAction  (  )  { 
return  "Redirect"  ; 
} 






public   String   getChannel  (  )  { 
return   channel  ; 
} 






public   void   setChannel  (  String   channel  )  { 
this  .  channel  =  channel  ; 
} 






public   String   getExtraChannel  (  )  { 
return   extraChannel  ; 
} 






public   void   setExtraChannel  (  String   extraChannel  )  { 
this  .  extraChannel  =  extraChannel  ; 
} 






public   String   getContext  (  )  { 
return   context  ; 
} 






public   void   setContext  (  String   context  )  { 
this  .  context  =  context  ; 
} 






public   String   getExten  (  )  { 
return   exten  ; 
} 






public   void   setExten  (  String   exten  )  { 
this  .  exten  =  exten  ; 
} 






public   Integer   getPriority  (  )  { 
return   priority  ; 
} 






public   void   setPriority  (  Integer   priority  )  { 
this  .  priority  =  priority  ; 
} 







public   String   getExtraContext  (  )  { 
return   extraContext  ; 
} 







public   void   setExtraContext  (  String   extraContext  )  { 
this  .  extraContext  =  extraContext  ; 
} 







public   String   getExtraExten  (  )  { 
return   extraExten  ; 
} 







public   void   setExtraExten  (  String   extraExten  )  { 
this  .  extraExten  =  extraExten  ; 
} 







public   Integer   getExtraPriority  (  )  { 
return   extraPriority  ; 
} 







public   void   setExtraPriority  (  Integer   extraPriority  )  { 
this  .  extraPriority  =  extraPriority  ; 
} 
} 

