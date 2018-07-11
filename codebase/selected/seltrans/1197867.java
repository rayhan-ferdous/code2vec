package   tuwien  .  auto  .  eicl  .  struct  .  eibnetip  ; 

import   java  .  io  .  *  ; 
import   tuwien  .  auto  .  eicl  .  struct  .  eibnetip  .  util  .  EIBNETIP_Constants  ; 
import   tuwien  .  auto  .  eicl  .  struct  .  eibnetip  .  util  .  HPAI  ; 
import   tuwien  .  auto  .  eicl  .  util  .  *  ; 










public   class   Disconnect_Request  { 

private   short   channelid  ; 

private   short   reserved  ; 

private   HPAI   endpoint  ; 












public   Disconnect_Request  (  byte  [  ]  _Disconnect_Request  )  throws   EICLException  { 
ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  _Disconnect_Request  )  ; 
channelid  =  (  short  )  bais  .  read  (  )  ; 
reserved  =  (  short  )  bais  .  read  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  8  ]  ; 
try  { 
bais  .  read  (  buffer  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   EICLException  (  ex  .  getMessage  (  )  )  ; 
} 
endpoint  =  new   HPAI  (  buffer  )  ; 
} 













public   Disconnect_Request  (  short   _Channelid  ,  int   _LocalPort  )  throws   EICLException  { 
channelid  =  _Channelid  ; 
endpoint  =  new   HPAI  (  _LocalPort  )  ; 
} 








public   byte  [  ]  toByteArray  (  )  throws   EICLException  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
try  { 
EIBnetIPPacket   header  =  new   EIBnetIPPacket  (  EIBNETIP_Constants  .  DISCONNECT_REQUEST  ,  (  EIBNETIP_Constants  .  HEADER_SIZE_10  +  endpoint  .  getStructLength  (  )  +  2  )  )  ; 
baos  .  write  (  header  .  toByteArray  (  )  )  ; 
baos  .  write  (  (  byte  )  channelid  &  0x00FF  )  ; 
baos  .  write  (  (  byte  )  reserved  &  0x00FF  )  ; 
baos  .  write  (  endpoint  .  toByteArray  (  )  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   EICLException  (  ex  .  getMessage  (  )  )  ; 
} 
return   baos  .  toByteArray  (  )  ; 
} 






public   short   getChannelID  (  )  { 
return   channelid  ; 
} 






public   short   getReserved  (  )  { 
return   reserved  ; 
} 






public   HPAI   getEndPoint  (  )  { 
return   endpoint  ; 
} 
} 

