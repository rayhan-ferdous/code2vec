package   org  .  simpleframework  .  http  .  transport  ; 

import   java  .  nio  .  channels  .  SocketChannel  ; 
import   org  .  simpleframework  .  util  .  select  .  Operation  ; 










class   Dispatcher   implements   Operation  { 




private   final   Negotiator   negotiator  ; 




private   final   Transport   transport  ; 










public   Dispatcher  (  Transport   transport  ,  Negotiator   negotiator  )  { 
this  .  negotiator  =  negotiator  ; 
this  .  transport  =  transport  ; 
} 










public   SocketChannel   getChannel  (  )  { 
return   transport  .  getSocket  (  )  ; 
} 







public   void   run  (  )  { 
try  { 
negotiator  .  process  (  transport  )  ; 
}  catch  (  Exception   e  )  { 
cancel  (  )  ; 
} 
} 








public   void   cancel  (  )  { 
try  { 
transport  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
return  ; 
} 
} 
} 

