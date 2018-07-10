package   org  .  simpleframework  .  transport  .  connect  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  SocketAddress  ; 
import   java  .  nio  .  channels  .  SelectableChannel  ; 
import   java  .  nio  .  channels  .  ServerSocketChannel  ; 
import   java  .  nio  .  channels  .  SocketChannel  ; 
import   javax  .  net  .  ssl  .  SSLContext  ; 
import   javax  .  net  .  ssl  .  SSLEngine  ; 
import   org  .  simpleframework  .  transport  .  Server  ; 
import   org  .  simpleframework  .  transport  .  Socket  ; 
import   org  .  simpleframework  .  transport  .  reactor  .  Operation  ; 

















class   Acceptor   implements   Operation  { 




private   final   ServerSocketChannel   server  ; 




private   final   ServerSocket   socket  ; 




private   final   SSLContext   context  ; 




private   final   Server   handler  ; 











public   Acceptor  (  SocketAddress   address  ,  SSLContext   context  ,  Server   handler  )  throws   IOException  { 
this  .  server  =  ServerSocketChannel  .  open  (  )  ; 
this  .  socket  =  server  .  socket  (  )  ; 
this  .  handler  =  handler  ; 
this  .  context  =  context  ; 
this  .  bind  (  address  )  ; 
} 









public   SocketAddress   getAddress  (  )  { 
return   socket  .  getLocalSocketAddress  (  )  ; 
} 










public   SelectableChannel   getChannel  (  )  { 
return   server  ; 
} 









public   void   run  (  )  { 
try  { 
accept  (  )  ; 
}  catch  (  Exception   e  )  { 
pause  (  )  ; 
} 
} 







private   void   pause  (  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  Exception   e  )  { 
return  ; 
} 
} 







public   void   cancel  (  )  { 
try  { 
close  (  )  ; 
}  catch  (  Throwable   e  )  { 
return  ; 
} 
} 









private   void   bind  (  SocketAddress   address  )  throws   IOException  { 
server  .  configureBlocking  (  false  )  ; 
socket  .  setReuseAddress  (  true  )  ; 
socket  .  bind  (  address  ,  100  )  ; 
} 










private   void   accept  (  )  throws   IOException  { 
SocketChannel   channel  =  server  .  accept  (  )  ; 
while  (  channel  !=  null  )  { 
configure  (  channel  )  ; 
if  (  context  ==  null  )  { 
process  (  channel  ,  null  )  ; 
}  else  { 
process  (  channel  )  ; 
} 
channel  =  server  .  accept  (  )  ; 
} 
} 









private   void   configure  (  SocketChannel   channel  )  throws   IOException  { 
channel  .  socket  (  )  .  setTcpNoDelay  (  true  )  ; 
channel  .  configureBlocking  (  false  )  ; 
} 









private   void   process  (  SocketChannel   channel  )  throws   IOException  { 
SSLEngine   engine  =  context  .  createSSLEngine  (  )  ; 
try  { 
process  (  channel  ,  engine  )  ; 
}  catch  (  Exception   e  )  { 
channel  .  close  (  )  ; 
} 
} 










private   void   process  (  SocketChannel   channel  ,  SSLEngine   engine  )  throws   IOException  { 
Socket   socket  =  new   Subscription  (  channel  ,  engine  )  ; 
try  { 
handler  .  process  (  socket  )  ; 
}  catch  (  Exception   e  )  { 
channel  .  close  (  )  ; 
} 
} 









public   void   close  (  )  throws   IOException  { 
server  .  close  (  )  ; 
} 
} 

