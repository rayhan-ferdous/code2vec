package   javaclient3  ; 

import   java  .  io  .  IOException  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javaclient3  .  structures  .  PlayerMsgHdr  ; 
import   javaclient3  .  structures  .  mcom  .  PlayerMcomConfig  ; 
import   javaclient3  .  structures  .  mcom  .  PlayerMcomData  ; 
import   javaclient3  .  xdr  .  OncRpcException  ; 
import   javaclient3  .  xdr  .  XdrBufferDecodingStream  ; 
import   javaclient3  .  xdr  .  XdrBufferEncodingStream  ; 
















public   class   MComInterface   extends   PlayerDevice  { 

private   static   final   boolean   isDebugging  =  PlayerClient  .  isDebugging  ; 

private   Logger   logger  =  Logger  .  getLogger  (  MComInterface  .  class  .  getName  (  )  )  ; 

private   PlayerMcomData   pmdata  ; 

private   boolean   readyPmdata  =  false  ; 





public   MComInterface  (  PlayerClient   pc  )  { 
super  (  pc  )  ; 
} 




public   synchronized   void   readData  (  PlayerMsgHdr   header  )  { 
try  { 
this  .  timestamp  =  header  .  getTimestamp  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  12  ]  ; 
is  .  readFully  (  buffer  ,  0  ,  12  )  ; 
pmdata  =  new   PlayerMcomData  (  )  ; 
XdrBufferDecodingStream   xdr  =  new   XdrBufferDecodingStream  (  buffer  )  ; 
xdr  .  beginDecoding  (  )  ; 
pmdata  .  setFull  (  (  char  )  xdr  .  xdrDecodeByte  (  )  )  ; 
int   dataCount  =  xdr  .  xdrDecodeInt  (  )  ; 
xdr  .  endDecoding  (  )  ; 
xdr  .  close  (  )  ; 
buffer  =  new   byte  [  MCOM_DATA_LEN  ]  ; 
is  .  readFully  (  buffer  ,  0  ,  dataCount  )  ; 
pmdata  .  setData_count  (  dataCount  )  ; 
pmdata  .  setData  (  new   String  (  buffer  )  .  toCharArray  (  )  )  ; 
if  (  (  dataCount  %  4  )  !=  0  )  is  .  readFully  (  buffer  ,  0  ,  4  -  (  dataCount  %  4  )  )  ; 
readyPmdata  =  true  ; 
}  catch  (  IOException   e  )  { 
throw   new   PlayerException  (  "[MCom] : Error reading payload: "  +  e  .  toString  (  )  ,  e  )  ; 
}  catch  (  OncRpcException   e  )  { 
throw   new   PlayerException  (  "[MCOM] : Error while XDR-decoding payload: "  +  e  .  toString  (  )  ,  e  )  ; 
} 
} 









public   void   sendConfigReq  (  PlayerMcomConfig   pmconfig  ,  int   whichReq  )  { 
try  { 
int   total  =  12  +  4  +  pmconfig  .  getChannel_count  (  )  +  8  +  4  +  pmconfig  .  getData  (  )  .  getData_count  (  )  ; 
sendHeader  (  PLAYER_MSGTYPE_REQ  ,  whichReq  ,  total  )  ; 
XdrBufferEncodingStream   xdr  =  new   XdrBufferEncodingStream  (  24  )  ; 
xdr  .  beginEncoding  (  null  ,  0  )  ; 
xdr  .  xdrEncodeInt  (  pmconfig  .  getCommand  (  )  )  ; 
xdr  .  xdrEncodeInt  (  pmconfig  .  getType  (  )  )  ; 
xdr  .  xdrEncodeInt  (  pmconfig  .  getChannel_count  (  )  )  ; 
xdr  .  xdrEncodeByte  (  (  byte  )  pmconfig  .  getChannel_count  (  )  )  ; 
xdr  .  endEncoding  (  )  ; 
os  .  write  (  xdr  .  getXdrData  (  )  ,  0  ,  xdr  .  getXdrLength  (  )  )  ; 
xdr  .  close  (  )  ; 
os  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
String   subtype  =  ""  ; 
switch  (  whichReq  )  { 
case   PLAYER_MCOM_PUSH  : 
{ 
subtype  =  "PLAYER_MCOM_PUSH"  ; 
break  ; 
} 
case   PLAYER_MCOM_POP  : 
{ 
subtype  =  "PLAYER_MCOM_POP"  ; 
break  ; 
} 
case   PLAYER_MCOM_READ  : 
{ 
subtype  =  "PLAYER_MCOM_READ"  ; 
break  ; 
} 
case   PLAYER_MCOM_CLEAR  : 
{ 
subtype  =  "PLAYER_MCOM_CLEAR"  ; 
break  ; 
} 
case   PLAYER_MCOM_SET_CAPACITY  : 
{ 
subtype  =  "PLAYER_MCOM_SET_CAPACITY"  ; 
break  ; 
} 
default  : 
{ 
logger  .  log  (  Level  .  FINEST  ,  "[MCom] : Couldn't send "  +  subtype  +  " command: "  +  e  .  toString  (  )  )  ; 
} 
} 
} 
} 




public   void   Push  (  int   type  ,  String   channel  ,  char  [  ]  dataT  )  { 
} 




public   void   Pop  (  int   type  ,  String   channel  )  { 
} 




public   void   Read  (  int   type  ,  String   channel  )  { 
} 




public   void   Clear  (  int   type  ,  String   channel  )  { 
} 




public   void   setCapacity  (  int   type  ,  String   channel  ,  char   capacity  )  { 
char  [  ]  dataT  =  new   char  [  MCOM_DATA_LEN  ]  ; 
dataT  [  0  ]  =  capacity  ; 
} 





public   void   handleResponse  (  int   size  )  { 
if  (  size  ==  0  )  { 
if  (  isDebugging  )  System  .  err  .  println  (  "[MCom][Debug] : Unexpected response of size 0!"  )  ; 
return  ; 
} 
try  { 
}  catch  (  Exception   e  )  { 
logger  .  log  (  Level  .  FINEST  ,  "[MCom] : Error when reading payload "  +  e  .  toString  (  )  )  ; 
} 
} 





public   PlayerMcomData   getData  (  )  { 
return   this  .  pmdata  ; 
} 





public   boolean   isDataReady  (  )  { 
if  (  readyPmdata  )  { 
readyPmdata  =  false  ; 
return   true  ; 
} 
return   false  ; 
} 
} 

