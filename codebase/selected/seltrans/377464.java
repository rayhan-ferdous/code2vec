package   org  .  jopenray  .  rdp  .  rdp5  ; 

import   java  .  io  .  IOException  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  jopenray  .  rdp  .  Input  ; 
import   org  .  jopenray  .  rdp  .  MCS  ; 
import   org  .  jopenray  .  rdp  .  Options  ; 
import   org  .  jopenray  .  rdp  .  RdesktopException  ; 
import   org  .  jopenray  .  rdp  .  RdpPacket_Localised  ; 
import   org  .  jopenray  .  rdp  .  crypto  .  CryptoException  ; 

public   class   VChannels  { 

protected   static   Logger   logger  =  Logger  .  getLogger  (  Input  .  class  )  ; 

public   static   final   int   WAVE_FORMAT_PCM  =  1  ; 

public   static   final   int   WAVE_FORMAT_ADPCM  =  2  ; 

public   static   final   int   WAVE_FORMAT_ALAW  =  6  ; 

public   static   final   int   WAVE_FORMAT_MULAW  =  7  ; 

public   static   final   int   CHANNEL_OPTION_INITIALIZED  =  0x80000000  ; 

public   static   final   int   CHANNEL_OPTION_ENCRYPT_RDP  =  0x40000000  ; 

public   static   final   int   CHANNEL_OPTION_COMPRESS_RDP  =  0x00800000  ; 

public   static   final   int   CHANNEL_OPTION_SHOW_PROTOCOL  =  0x00200000  ; 

public   static   final   int   STATUS_SUCCESS  =  0x00000000  ; 

public   static   final   int   STATUS_INVALID_PARAMETER  =  0xc000000d  ; 

public   static   final   int   STATUS_INVALID_DEVICE_REQUEST  =  0xc0000010  ; 

public   static   final   int   STATUS_ACCESS_DENIED  =  0xc0000022  ; 

public   static   final   int   MAX_CHANNELS  =  4  ; 

public   static   final   int   CHANNEL_CHUNK_LENGTH  =  1600  ; 

public   static   final   int   CHANNEL_FLAG_FIRST  =  0x01  ; 

public   static   final   int   CHANNEL_FLAG_LAST  =  0x02  ; 

public   static   final   int   CHANNEL_FLAG_SHOW_PROTOCOL  =  0x10  ; 

private   VChannel   channels  [  ]  =  new   VChannel  [  MAX_CHANNELS  ]  ; 

private   int   num_channels  ; 

public   int   num_channels  (  )  { 
return   num_channels  ; 
} 

private   byte  [  ]  fragment_buffer  =  null  ; 








public   int   mcs_id  (  int   c  )  { 
return   MCS  .  MCS_GLOBAL_CHANNEL  +  1  +  c  ; 
} 




public   VChannels  (  )  { 
channels  =  new   VChannel  [  MAX_CHANNELS  ]  ; 
} 








public   VChannel   channel  (  int   c  )  { 
if  (  c  <  num_channels  )  return   channels  [  c  ]  ;  else   return   null  ; 
} 








public   VChannel   find_channel_by_channelno  (  int   channelno  )  { 
if  (  channelno  >  MCS  .  MCS_GLOBAL_CHANNEL  +  num_channels  )  { 
logger  .  warn  (  "Channel "  +  channelno  +  " not defined. Highest channel defined is "  +  MCS  .  MCS_GLOBAL_CHANNEL  +  num_channels  )  ; 
return   null  ; 
}  else   return   channels  [  channelno  -  MCS  .  MCS_GLOBAL_CHANNEL  -  1  ]  ; 
} 




public   void   clear  (  )  { 
channels  =  new   VChannel  [  MAX_CHANNELS  ]  ; 
num_channels  =  0  ; 
} 







public   boolean   register  (  VChannel   v  )  throws   RdesktopException  { 
if  (  !  Options  .  use_rdp5  )  { 
return   false  ; 
} 
if  (  num_channels  >=  MAX_CHANNELS  )  throw   new   RdesktopException  (  "Channel table full. Could not register channel."  )  ; 
channels  [  num_channels  ]  =  v  ; 
v  .  set_mcs_id  (  MCS  .  MCS_GLOBAL_CHANNEL  +  1  +  num_channels  )  ; 
num_channels  ++  ; 
return   true  ; 
} 









public   void   channel_process  (  RdpPacket_Localised   data  ,  int   mcsChannel  )  throws   RdesktopException  ,  IOException  ,  CryptoException  { 
int   length  ,  flags  ; 
int   thislength  =  0  ; 
VChannel   channel  =  null  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  num_channels  ;  i  ++  )  { 
if  (  mcs_id  (  i  )  ==  mcsChannel  )  { 
channel  =  channels  [  i  ]  ; 
break  ; 
} 
} 
if  (  i  >=  num_channels  )  return  ; 
length  =  data  .  getLittleEndian32  (  )  ; 
flags  =  data  .  getLittleEndian32  (  )  ; 
if  (  (  (  flags  &  CHANNEL_FLAG_FIRST  )  !=  0  )  &&  (  (  flags  &  CHANNEL_FLAG_LAST  )  !=  0  )  )  { 
channel  .  process  (  data  )  ; 
}  else  { 
byte  [  ]  content  =  new   byte  [  data  .  getEnd  (  )  -  data  .  getPosition  (  )  ]  ; 
data  .  copyToByteArray  (  content  ,  0  ,  data  .  getPosition  (  )  ,  content  .  length  )  ; 
fragment_buffer  =  append  (  fragment_buffer  ,  content  )  ; 
if  (  (  flags  &  CHANNEL_FLAG_LAST  )  !=  0  )  { 
RdpPacket_Localised   fullpacket  =  new   RdpPacket_Localised  (  fragment_buffer  .  length  )  ; 
fullpacket  .  copyFromByteArray  (  fragment_buffer  ,  0  ,  0  ,  fragment_buffer  .  length  )  ; 
channel  .  process  (  fullpacket  )  ; 
fragment_buffer  =  null  ; 
} 
} 
} 







static   Object   arrayExpand  (  Object   a  ,  int   amount  )  { 
Class   cl  =  a  .  getClass  (  )  ; 
if  (  !  cl  .  isArray  (  )  )  return   null  ; 
int   length  =  Array  .  getLength  (  a  )  ; 
int   newLength  =  length  +  amount  ; 
Class   componentType  =  a  .  getClass  (  )  .  getComponentType  (  )  ; 
Object   newArray  =  Array  .  newInstance  (  componentType  ,  newLength  )  ; 
System  .  arraycopy  (  a  ,  0  ,  newArray  ,  0  ,  length  )  ; 
return   newArray  ; 
} 







static   byte  [  ]  append  (  byte  [  ]  target  ,  byte  [  ]  source  )  { 
if  (  target  ==  null  ||  target  .  length  <=  0  )  return   source  ;  else   if  (  source  ==  null  ||  source  .  length  <=  0  )  return   target  ;  else  { 
byte  [  ]  out  =  (  byte  [  ]  )  arrayExpand  (  target  ,  source  .  length  )  ; 
System  .  arraycopy  (  source  ,  0  ,  out  ,  target  .  length  ,  source  .  length  )  ; 
return   out  ; 
} 
} 
} 

