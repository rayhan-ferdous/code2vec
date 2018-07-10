package   net  .  sfjinyan  .  server  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  util  .  *  ; 
import   net  .  sfjinyan  .  server  .  chessgame  .  OnlineChessGame  ; 
import   net  .  sfjinyan  .  server  .  db  .  Ibatis  ; 
import   org  .  apache  .  mina  .  common  .  ByteBuffer  ; 
import   org  .  apache  .  mina  .  common  .  IoAcceptor  ; 
import   org  .  apache  .  mina  .  common  .  SimpleByteBufferAllocator  ; 
import   org  .  apache  .  mina  .  filter  .  codec  .  ProtocolCodecFilter  ; 
import   org  .  apache  .  mina  .  filter  .  codec  .  textline  .  TextLineCodecFactory  ; 
import   org  .  apache  .  mina  .  transport  .  socket  .  nio  .  SocketAcceptor  ; 
import   org  .  apache  .  mina  .  transport  .  socket  .  nio  .  SocketAcceptorConfig  ; 

public   class   JinyanServer  { 




private   IoAcceptor   acceptor  ; 

public   static   final   Configuration   config  =  new   Configuration  (  "config.xml"  )  ; 




public   static   final   Ibatis   ib  =  new   Ibatis  (  )  ; 




private   static   JinyanServer   instance  ; 





private   Map  <  Integer  ,  Channel  >  channels  ; 




private   String   serverVersion  =  "Jinyan 0.2-dev"  ; 




public   static   List  <  OnlineChessGame  >  liveGames  ; 




private   JinyanServer  (  )  { 
ByteBuffer  .  setUseDirectBuffers  (  false  )  ; 
ByteBuffer  .  setAllocator  (  new   SimpleByteBufferAllocator  (  )  )  ; 
acceptor  =  new   SocketAcceptor  (  )  ; 
channels  =  new   HashMap  <  Integer  ,  Channel  >  (  )  ; 
channels  .  put  (  Integer  .  valueOf  (  1  )  ,  new   Channel  (  1  )  )  ; 
liveGames  =  Collections  .  synchronizedList  (  new   ArrayList  <  OnlineChessGame  >  (  )  { 

@  Override 
public   OnlineChessGame   get  (  int   id  )  { 
if  (  size  (  )  <=  id  )  return   null  ; 
return   super  .  get  (  id  )  ; 
} 
}  )  ; 
} 





public   static   JinyanServer   getInstance  (  )  { 
if  (  instance  ==  null  )  { 
instance  =  new   JinyanServer  (  )  ; 
} 
return   instance  ; 
} 






public   Channel   getChannel  (  int   number  )  { 
if  (  !  channels  .  containsKey  (  Integer  .  valueOf  (  number  )  )  )  { 
throw   new   NullPointerException  (  "There is no such channel"  )  ; 
} 
return   channels  .  get  (  Integer  .  valueOf  (  number  )  )  ; 
} 






public   static   ClientConnection   getConnectionOf  (  String   username  )  { 
return   CommandsHandler  .  getConnectionOf  (  username  )  ; 
} 

public   static   boolean   isConnected  (  String   username  )  { 
return   CommandsHandler  .  isConnected  (  username  )  ; 
} 




public   void   run  (  )  { 
System  .  out  .  println  (  "-------------------------------------------------------\n"  +  "                "  +  config  .  getServerName  (  )  .  toUpperCase  (  )  +  "\n-------------------------------------------------------\n"  +  "Version: "  +  serverVersion  +  "\nAuthor: Bogdan Vovk"  +  "\nContributors: Radesh M M"  +  "\nLocation: "  +  config  .  getServerLocation  (  )  +  "\nStart time: "  +  new   Date  (  )  .  toString  (  )  )  ; 
SocketAcceptorConfig   cfg  =  new   SocketAcceptorConfig  (  )  ; 
cfg  .  getFilterChain  (  )  .  addLast  (  "codec"  ,  new   ProtocolCodecFilter  (  new   TextLineCodecFactory  (  Charset  .  forName  (  "UTF-8"  )  )  )  )  ; 
cfg  .  getFilterChain  (  )  .  addLast  (  "filter"  ,  new   DebugFilter  (  )  )  ; 
try  { 
acceptor  .  bind  (  new   InetSocketAddress  (  config  .  getServerPort  (  )  )  ,  new   CommandsHandler  (  )  ,  cfg  )  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
System  .  exit  (  0  )  ; 
} 
} 

public   void   stop  (  )  { 
acceptor  .  unbindAll  (  )  ; 
instance  =  null  ; 
} 

public   static   void   main  (  String   args  [  ]  )  throws   IOException  { 
getInstance  (  )  ; 
instance  .  run  (  )  ; 
} 
} 

