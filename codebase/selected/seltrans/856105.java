package   org  .  simpleframework  .  http  .  core  ; 

import   java  .  nio  .  channels  .  SocketChannel  ; 
import   org  .  simpleframework  .  util  .  select  .  Operation  ; 













class   Reader   implements   Operation  { 




private   final   Selector   source  ; 




private   final   Collector   task  ; 




private   final   Channel   channel  ; 









public   Reader  (  Selector   source  ,  Collector   task  )  { 
this  .  channel  =  task  .  getChannel  (  )  ; 
this  .  source  =  source  ; 
this  .  task  =  task  ; 
} 









public   SocketChannel   getChannel  (  )  { 
return   task  .  getSocket  (  )  ; 
} 








public   void   run  (  )  { 
try  { 
task  .  collect  (  source  )  ; 
}  catch  (  Throwable   e  )  { 
cancel  (  )  ; 
} 
} 







public   void   cancel  (  )  { 
try  { 
channel  .  close  (  )  ; 
}  catch  (  Throwable   e  )  { 
return  ; 
} 
} 
} 

