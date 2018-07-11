package   org  .  jnetpcap  .  packet  ; 

import   java  .  nio  .  ByteBuffer  ; 
import   org  .  jnetpcap  .  IncompatiblePeer  ; 
import   org  .  jnetpcap  .  PcapHeader  ; 
import   org  .  jnetpcap  .  nio  .  JBuffer  ; 
import   org  .  jnetpcap  .  nio  .  JMemoryPool  ; 







































































































































































































































































































































public   class   PcapPacket   extends   JPacket  { 

private   static   final   int   STATE_SIZE  =  PcapHeader  .  sizeof  (  )  +  JPacket  .  State  .  sizeof  (  DEFAULT_STATE_HEADER_COUNT  )  ; 




static  { 
try  { 
initIds  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




private   static   native   void   initIds  (  )  ; 

private   final   PcapHeader   header  =  new   PcapHeader  (  Type  .  POINTER  )  ; 
























public   PcapPacket  (  byte  [  ]  buffer  )  { 
super  (  Type  .  POINTER  )  ; 
transferStateAndDataFrom  (  buffer  )  ; 
} 
























public   PcapPacket  (  ByteBuffer   buffer  )  { 
super  (  Type  .  POINTER  )  ; 
transferStateAndDataFrom  (  buffer  )  ; 
} 










public   PcapPacket  (  int   size  )  { 
super  (  size  ,  STATE_SIZE  )  ; 
} 









public   PcapPacket  (  int   size  ,  int   headerCount  )  { 
super  (  size  ,  PcapHeader  .  sizeof  (  )  +  JPacket  .  State  .  sizeof  (  headerCount  )  )  ; 
} 
























public   PcapPacket  (  JBuffer   buffer  )  { 
super  (  Type  .  POINTER  )  ; 
transferStateAndDataFrom  (  buffer  )  ; 
} 









public   PcapPacket  (  JPacket   src  )  { 
super  (  Type  .  POINTER  )  ; 
if  (  src   instanceof   PcapPacket  )  { 
(  (  PcapPacket  )  src  )  .  transferStateAndDataTo  (  this  )  ; 
}  else  { 
throw   new   UnsupportedOperationException  (  "Unsupported packet type for this constructor"  )  ; 
} 
} 











public   PcapPacket  (  PcapHeader   header  ,  ByteBuffer   buffer  )  { 
super  (  Type  .  POINTER  )  ; 
transferHeaderAndDataFrom  (  header  ,  buffer  )  ; 
} 











public   PcapPacket  (  PcapHeader   header  ,  JBuffer   buffer  )  { 
super  (  Type  .  POINTER  )  ; 
transferHeaderAndDataFrom  (  header  ,  buffer  )  ; 
} 









public   PcapPacket  (  PcapPacket   src  )  { 
super  (  Type  .  POINTER  )  ; 
src  .  transferStateAndDataTo  (  this  )  ; 
} 










public   PcapPacket  (  Type   type  )  { 
super  (  type  )  ; 
} 






@  Override 
public   PcapHeader   getCaptureHeader  (  )  { 
return   header  ; 
} 







public   int   getTotalSize  (  )  { 
return   super  .  size  (  )  +  state  .  size  (  )  +  header  .  size  (  )  ; 
} 








public   int   peerHeaderAndData  (  JBuffer   buffer  )  { 
int   o  =  header  .  peer  (  buffer  ,  0  )  ; 
o  +=  super  .  peer  (  buffer  ,  o  ,  buffer  .  size  (  )  -  header  .  size  (  )  )  ; 
return   o  ; 
} 






public   int   peerHeaderAndData  (  PcapHeader   header  ,  ByteBuffer   buffer  )  throws   PeeringException  { 
int   o  =  this  .  header  .  peerTo  (  header  ,  0  )  ; 
o  +=  super  .  peer  (  buffer  )  ; 
return   o  ; 
} 

public   int   peerHeaderAndData  (  PcapHeader   header  ,  JBuffer   buffer  )  { 
int   o  =  this  .  header  .  peerTo  (  header  ,  0  )  ; 
o  +=  super  .  peer  (  buffer  )  ; 
return   o  ; 
} 
























public   int   peerStateAndData  (  ByteBuffer   buffer  )  throws   PeeringException  { 
if  (  buffer  .  isDirect  (  )  ==  false  )  { 
throw   new   PeeringException  (  "unable to peer a non-direct ByteBuffer"  )  ; 
} 
return   peerStateAndData  (  getMemoryBuffer  (  buffer  )  ,  0  )  ; 
} 




















public   int   peerStateAndData  (  JBuffer   buffer  )  { 
return   peerStateAndData  (  getMemoryBuffer  (  buffer  )  ,  0  )  ; 
} 

private   int   peerStateAndData  (  JBuffer   memory  ,  int   offset  )  { 
int   o  =  header  .  peer  (  memory  ,  offset  )  ; 
state  .  peerTo  (  memory  ,  offset  +  o  ,  State  .  sizeof  (  0  )  )  ; 
o  +=  state  .  peerTo  (  memory  ,  offset  +  o  ,  State  .  sizeof  (  state  .  getHeaderCount  (  )  )  )  ; 
o  +=  super  .  peer  (  memory  ,  offset  +  o  ,  header  .  caplen  (  )  )  ; 
return   o  ; 
} 












public   int   transferHeaderAndDataFrom  (  PcapHeader   header  ,  ByteBuffer   buffer  )  { 
final   int   len  =  buffer  .  limit  (  )  -  buffer  .  position  (  )  ; 
JBuffer   b  =  getMemoryBuffer  (  header  .  size  (  )  +  len  )  ; 
int   o  =  header  .  transferTo  (  b  ,  0  )  ; 
o  +=  b  .  transferFrom  (  buffer  ,  o  )  ; 
peerHeaderAndData  (  b  )  ; 
return   o  ; 
} 












public   int   transferHeaderAndDataFrom  (  PcapHeader   header  ,  JBuffer   buffer  )  { 
JBuffer   b  =  getMemoryBuffer  (  header  .  size  (  )  +  buffer  .  size  (  )  )  ; 
int   o  =  header  .  transferTo  (  b  ,  0  )  ; 
o  +=  buffer  .  transferTo  (  b  ,  0  ,  buffer  .  size  (  )  ,  o  )  ; 
peerHeaderAndData  (  b  )  ; 
return   o  ; 
} 

























public   int   transferStateAndDataFrom  (  byte  [  ]  buffer  )  { 
JBuffer   b  =  getMemoryBuffer  (  buffer  )  ; 
return   peerStateAndData  (  b  ,  0  )  ; 
} 


























public   int   transferStateAndDataFrom  (  ByteBuffer   buffer  )  { 
final   int   len  =  buffer  .  limit  (  )  -  buffer  .  position  (  )  ; 
JBuffer   b  =  getMemoryBuffer  (  len  )  ; 
b  .  transferFrom  (  buffer  ,  0  )  ; 
return   peerStateAndData  (  b  ,  0  )  ; 
} 

























public   int   transferStateAndDataFrom  (  JBuffer   buffer  )  { 
final   int   len  =  buffer  .  size  (  )  ; 
JBuffer   b  =  getMemoryBuffer  (  len  )  ; 
buffer  .  transferTo  (  b  )  ; 
return   peerStateAndData  (  b  ,  0  )  ; 
} 













public   int   transferStateAndDataFrom  (  PcapPacket   packet  )  { 
return   packet  .  transferStateAndDataTo  (  this  )  ; 
} 



























public   int   transferStateAndDataTo  (  byte  [  ]  buffer  )  { 
int   o  =  header  .  transferTo  (  buffer  ,  0  )  ; 
o  +=  state  .  transferTo  (  buffer  ,  o  )  ; 
o  +=  super  .  transferTo  (  buffer  ,  0  ,  size  (  )  ,  o  )  ; 
return   o  ; 
} 



























public   int   transferStateAndDataTo  (  ByteBuffer   buffer  )  { 
int   o  =  header  .  transferTo  (  buffer  )  ; 
o  +=  state  .  transferTo  (  buffer  )  ; 
o  +=  super  .  transferTo  (  buffer  )  ; 
return   o  ; 
} 



























public   int   transferStateAndDataTo  (  JBuffer   buffer  )  { 
return   transferStateAndDataTo  (  buffer  ,  0  )  ; 
} 



























public   int   transferStateAndDataTo  (  JBuffer   buffer  ,  int   offset  )  { 
int   o  =  header  .  transferTo  (  buffer  ,  offset  )  ; 
o  +=  state  .  transferTo  (  buffer  ,  0  ,  state  .  size  (  )  ,  offset  +  o  )  ; 
o  +=  super  .  transferTo  (  buffer  ,  0  ,  size  (  )  ,  offset  +  o  )  ; 
return   o  ; 
} 













public   int   transferStateAndDataTo  (  PcapPacket   packet  )  { 
JBuffer   buffer  =  packet  .  getMemoryBuffer  (  this  .  getTotalSize  (  )  )  ; 
int   o  =  header  .  transferTo  (  buffer  ,  0  )  ; 
packet  .  header  .  peerTo  (  buffer  ,  0  )  ; 
packet  .  state  .  peerTo  (  buffer  ,  o  ,  state  .  size  (  )  )  ; 
o  +=  state  .  transferTo  (  packet  .  state  )  ; 
packet  .  peer  (  buffer  ,  o  ,  size  (  )  )  ; 
o  +=  this  .  transferTo  (  buffer  ,  0  ,  size  (  )  ,  o  )  ; 
return   o  ; 
} 
} 

