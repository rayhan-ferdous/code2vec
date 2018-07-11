package   org  .  limewire  .  nio  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketAddress  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  nio  .  channels  .  IllegalBlockingModeException  ; 
import   java  .  nio  .  channels  .  ServerSocketChannel  ; 
import   org  .  limewire  .  concurrent  .  ThreadExecutor  ; 
import   org  .  limewire  .  nio  .  observer  .  AcceptObserver  ; 









public   class   BlockingServerSocketAdapter   extends   ServerSocket  { 

private   final   AcceptObserver   observer  ; 

private   final   ServerSocket   delegate  ; 





public   BlockingServerSocketAdapter  (  )  throws   IOException  { 
this  (  null  )  ; 
} 






public   BlockingServerSocketAdapter  (  AcceptObserver   observer  )  throws   IOException  { 
this  .  delegate  =  new   ServerSocket  (  )  ; 
this  .  observer  =  observer  ; 
} 


public   BlockingServerSocketAdapter  (  int   port  )  throws   IOException  { 
this  (  port  ,  null  )  ; 
} 





public   BlockingServerSocketAdapter  (  int   port  ,  AcceptObserver   observer  )  throws   IOException  { 
this  (  observer  )  ; 
bind  (  new   InetSocketAddress  (  port  )  )  ; 
} 





public   BlockingServerSocketAdapter  (  int   port  ,  int   backlog  )  throws   IOException  { 
this  (  port  ,  backlog  ,  (  AcceptObserver  )  null  )  ; 
} 






public   BlockingServerSocketAdapter  (  int   port  ,  int   backlog  ,  AcceptObserver   observer  )  throws   IOException  { 
this  (  observer  )  ; 
bind  (  new   InetSocketAddress  (  port  )  ,  backlog  )  ; 
} 





public   BlockingServerSocketAdapter  (  int   port  ,  int   backlog  ,  InetAddress   bindAddr  )  throws   IOException  { 
this  (  port  ,  backlog  ,  bindAddr  ,  null  )  ; 
} 






public   BlockingServerSocketAdapter  (  int   port  ,  int   backlog  ,  InetAddress   bindAddr  ,  AcceptObserver   observer  )  throws   IOException  { 
this  (  observer  )  ; 
bind  (  new   InetSocketAddress  (  bindAddr  ,  port  )  ,  backlog  )  ; 
} 

public   void   bind  (  SocketAddress   endpoint  ,  int   backlog  )  throws   IOException  { 
delegate  .  bind  (  endpoint  ,  backlog  )  ; 
startListening  (  )  ; 
} 

public   void   bind  (  SocketAddress   endpoint  )  throws   IOException  { 
delegate  .  bind  (  endpoint  )  ; 
startListening  (  )  ; 
} 







public   Socket   accept  (  )  throws   IOException  { 
if  (  observer  !=  null  )  throw   new   IllegalBlockingModeException  (  )  ; 
return   delegate  .  accept  (  )  ; 
} 

public   void   close  (  )  throws   IOException  { 
delegate  .  close  (  )  ; 
} 

public   boolean   equals  (  Object   obj  )  { 
return   delegate  .  equals  (  obj  )  ; 
} 

public   ServerSocketChannel   getChannel  (  )  { 
return   delegate  .  getChannel  (  )  ; 
} 

public   InetAddress   getInetAddress  (  )  { 
return   delegate  .  getInetAddress  (  )  ; 
} 

public   int   getLocalPort  (  )  { 
return   delegate  .  getLocalPort  (  )  ; 
} 

public   SocketAddress   getLocalSocketAddress  (  )  { 
return   delegate  .  getLocalSocketAddress  (  )  ; 
} 

public   int   getReceiveBufferSize  (  )  throws   SocketException  { 
return   delegate  .  getReceiveBufferSize  (  )  ; 
} 

public   boolean   getReuseAddress  (  )  throws   SocketException  { 
return   delegate  .  getReuseAddress  (  )  ; 
} 

public   int   getSoTimeout  (  )  throws   IOException  { 
return   delegate  .  getSoTimeout  (  )  ; 
} 

public   int   hashCode  (  )  { 
return   delegate  .  hashCode  (  )  ; 
} 

public   boolean   isBound  (  )  { 
return   delegate  .  isBound  (  )  ; 
} 

public   boolean   isClosed  (  )  { 
return   delegate  .  isClosed  (  )  ; 
} 

public   void   setReceiveBufferSize  (  int   size  )  throws   SocketException  { 
delegate  .  setReceiveBufferSize  (  size  )  ; 
} 

public   void   setReuseAddress  (  boolean   on  )  throws   SocketException  { 
delegate  .  setReuseAddress  (  on  )  ; 
} 

public   void   setSoTimeout  (  int   timeout  )  throws   SocketException  { 
delegate  .  setSoTimeout  (  timeout  )  ; 
} 

public   String   toString  (  )  { 
return   delegate  .  toString  (  )  ; 
} 





private   void   startListening  (  )  { 
if  (  observer  !=  null  )  { 
ThreadExecutor  .  startThread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
while  (  !  isClosed  (  )  )  { 
try  { 
Socket   s  =  delegate  .  accept  (  )  ; 
observer  .  handleAccept  (  s  )  ; 
}  catch  (  IOException   ignored  )  { 
} 
} 
observer  .  shutdown  (  )  ; 
} 
}  ,  "BlockingServerSocketEmulator"  )  ; 
} 
} 
} 

