package   net  .  sf  .  howabout  .  plugin  ; 

import   java  .  util  .  GregorianCalendar  ; 







public   class   Event  { 

private   GregorianCalendar   date  ; 

private   String   name  ; 

private   String   description  ; 

private   String   genre  ; 

private   String   channel  ; 




public   Event  (  )  { 
} 









public   Event  (  GregorianCalendar   date  ,  String   name  ,  String   description  ,  String   genre  ,  String   channel  )  { 
this  .  date  =  date  ; 
this  .  name  =  name  ; 
this  .  description  =  description  ; 
this  .  genre  =  genre  ; 
this  .  channel  =  channel  ; 
} 





public   String   getChannel  (  )  { 
return   channel  ; 
} 





public   void   setChannel  (  String   channel  )  { 
this  .  channel  =  channel  ; 
} 





public   String   getDescription  (  )  { 
return   description  ; 
} 





public   void   setDescription  (  String   description  )  { 
this  .  description  =  description  ; 
} 





public   GregorianCalendar   getDate  (  )  { 
return   date  ; 
} 





public   void   setDate  (  GregorianCalendar   date  )  { 
this  .  date  =  date  ; 
} 





public   String   getGenre  (  )  { 
return   genre  ; 
} 





public   void   setGenre  (  String   genre  )  { 
this  .  genre  =  genre  ; 
} 





public   String   getName  (  )  { 
return   name  ; 
} 





public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 
} 

