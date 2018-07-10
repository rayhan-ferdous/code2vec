package   org  .  mobicents  .  media  .  server  .  impl  .  rtp  ; 

import   java  .  io  .  Serializable  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  mobicents  .  media  .  Format  ; 
import   org  .  mobicents  .  media  .  Server  ; 





















public   class   JitterBuffer   implements   Serializable  { 

static   final   int   QUEUE_SIZE  =  100  ; 

static   final   int   RTP_SEQ_MAX  =  65535  ; 




static   final   int   LATE_THRESHOLD  =  QUEUE_SIZE  -  RTP_SEQ_MAX  ; 

private   int   jitter  ; 

private   boolean   readStarted  =  true  ; 

private   boolean   writeStarted  =  false  ; 

private   RtpPacket  [  ]  queue  =  new   RtpPacket  [  QUEUE_SIZE  ]  ; 

private   int   readCursor  ; 

private   int   writeCursor  ; 

private   volatile   boolean   ready  =  false  ; 

private   long   duration  ; 

private   long   timestamp  ; 

private   Format   format  ; 

private   RtpClock   clock  ; 

private   static   Logger   logger  =  Logger  .  getLogger  (  JitterBuffer  .  class  )  ; 

private   long   drift  ; 

private   long   r  ,  s  ; 

private   double   j  ,  jm  ; 




private   int   firstSeqNumberReceived  =  0  ; 




private   long   lastRtpPackReceivedTimeStamp  ; 

private   int   seqNoCycles  =  0  ; 









public   JitterBuffer  (  int   jitter  )  { 
this  .  jitter  =  jitter  ; 
} 

public   void   setClock  (  RtpClock   clock  )  { 
this  .  clock  =  clock  ; 
if  (  format  !=  null  )  { 
clock  .  setFormat  (  format  )  ; 
} 
} 

public   void   setFormat  (  Format   format  )  { 
this  .  format  =  format  ; 
if  (  clock  !=  null  &&  format  !=  Format  .  ANY  )  { 
clock  .  setFormat  (  format  )  ; 
} 
} 

public   int   getJitter  (  )  { 
return   jitter  ; 
} 

public   double   getInterArrivalJitter  (  )  { 
return   j  ; 
} 

public   double   getMaxJitter  (  )  { 
return   jm  ; 
} 

public   void   write  (  RtpPacket   packet  )  { 
if  (  logger  .  isTraceEnabled  (  )  )  { 
logger  .  trace  (  "Receive "  +  packet  )  ; 
} 
lastRtpPackReceivedTimeStamp  =  Server  .  scheduler  .  getTimestamp  (  )  ; 
long   t  =  clock  .  getTime  (  packet  .  getTimestamp  (  )  )  ; 
packet  .  setTime  (  t  )  ; 
if  (  r  >  0  &&  s  >  0  )  { 
long   D  =  (  lastRtpPackReceivedTimeStamp  -  r  )  -  (  packet  .  getTime  (  )  -  s  )  ; 
if  (  D  <  0  )  { 
D  =  -  D  ; 
} 
j  =  j  +  (  D  -  j  )  /  16  ; 
if  (  jm  <  j  )  { 
jm  =  j  ; 
} 
} 
s  =  packet  .  getTime  (  )  ; 
r  =  lastRtpPackReceivedTimeStamp  ; 
if  (  !  writeStarted  )  { 
queue  [  0  ]  =  packet  ; 
writeStarted  =  true  ; 
firstSeqNumberReceived  =  packet  .  getSeqNumber  (  )  ; 
}  else  { 
RtpPacket   prev  =  queue  [  writeCursor  ]  ; 
long   diff  =  packet  .  getSeqNumber  (  )  -  prev  .  getSeqNumber  (  )  ; 
this  .  process  (  diff  ,  packet  ,  prev  )  ; 
} 
if  (  !  ready  &&  duration  >  (  jitter  )  )  { 
ready  =  true  ; 
} 
} 

private   void   process  (  long   diff  ,  RtpPacket   packet  ,  RtpPacket   prev  )  { 
if  (  diff  ==  1  )  { 
long   lduration  =  packet  .  getTime  (  )  -  prev  .  getTime  (  )  ; 
if  (  detectSilenceGap  (  lduration  ,  packet  ,  prev  )  )  { 
return  ; 
} 
this  .  writeCursor  =  inc  (  this  .  writeCursor  ,  1  )  ; 
checkSimpleOverflow  (  this  .  writeCursor  )  ; 
this  .  queue  [  this  .  writeCursor  ]  =  packet  ; 
prev  .  setDuration  (  lduration  )  ; 
this  .  duration  +=  prev  .  getDuration  (  )  ; 
}  else   if  (  diff  >  1  )  { 
long   lduration  =  packet  .  getTime  (  )  -  prev  .  getTime  (  )  ; 
if  (  detectSilenceGap  (  lduration  ,  packet  ,  prev  )  )  { 
return  ; 
} 
prev  .  setDuration  (  lduration  )  ; 
int   nextWriteCursor  =  inc  (  writeCursor  ,  (  int  )  diff  )  ; 
checkPositiveOverflow  (  nextWriteCursor  ,  diff  )  ; 
this  .  writeCursor  =  nextWriteCursor  ; 
this  .  queue  [  this  .  writeCursor  ]  =  packet  ; 
this  .  duration  +=  prev  .  getDuration  (  )  ; 
}  else   if  (  diff  <=  LATE_THRESHOLD  )  { 
this  .  seqNoCycles  ++  ; 
diff  =  diff  +  RTP_SEQ_MAX  ; 
process  (  diff  ,  packet  ,  prev  )  ; 
}  else  { 
int   rightIndex  =  this  .  writeCursor  ; 
this  .  writeCursor  =  inc  (  this  .  writeCursor  ,  (  int  )  diff  )  ; 
this  .  queue  [  this  .  writeCursor  ]  =  packet  ; 
int   i  =  dec  (  this  .  writeCursor  ,  1  )  ; 
int   count  =  0  ; 
while  (  this  .  queue  [  i  ]  ==  null  &&  count  <  this  .  queue  .  length  -  1  )  { 
i  =  dec  (  i  ,  1  )  ; 
count  ++  ; 
} 
this  .  queue  [  i  ]  .  setDuration  (  packet  .  getTime  (  )  -  this  .  queue  [  i  ]  .  getTime  (  )  )  ; 
i  =  inc  (  this  .  writeCursor  ,  1  )  ; 
while  (  this  .  queue  [  i  ]  ==  null  &&  i  <  rightIndex  )  { 
i  =  inc  (  i  ,  1  )  ; 
} 
packet  .  setDuration  (  this  .  queue  [  i  ]  .  getTime  (  )  -  packet  .  getTime  (  )  )  ; 
} 
} 




private   boolean   isSinglePacketPresent  (  )  { 
return   this  .  readCursor  ==  this  .  writeCursor  ; 
} 

private   boolean   detectSilenceGap  (  long   lduration  ,  RtpPacket   packet  ,  RtpPacket   prev  )  { 
if  (  isSinglePacketPresent  (  )  )  { 
int   seq  =  prev  .  getSeqNumber  (  )  -  firstSeqNumberReceived  ; 
if  (  seq  ==  0  )  { 
seq  =  1  ; 
}  else   if  (  seq  <  0  )  { 
seq  +=  RTP_SEQ_MAX  ; 
} 
long   avgPacketDuration  =  prev  .  getTime  (  )  /  (  seq  +  seqNoCycles  *  RTP_SEQ_MAX  )  ; 
if  (  avgPacketDuration  *  3  <  lduration  )  { 
this  .  softReset  (  )  ; 
this  .  write  (  packet  )  ; 
return   true  ; 
} 
} 
return   false  ; 
} 

private   void   checkSimpleOverflow  (  int   toBeWrittenCurson  )  { 
if  (  this  .  readCursor  ==  toBeWrittenCurson  )  { 
RtpPacket   removed  =  this  .  queue  [  this  .  readCursor  ]  ; 
this  .  queue  [  this  .  readCursor  ]  =  null  ; 
this  .  readCursor  =  inc  (  this  .  readCursor  ,  1  )  ; 
this  .  duration  -=  removed  .  getDuration  (  )  ; 
} 
} 

private   void   checkPositiveOverflow  (  int   nextWriteCursor  ,  long   diff  )  { 
long   boundry  =  this  .  writeCursor  +  diff  ; 
if  (  boundry  >=  QUEUE_SIZE  )  { 
if  (  (  this  .  readCursor  >  this  .  writeCursor  )  &&  (  nextWriteCursor  <  this  .  readCursor  )  )  { 
this  .  cleanBufferOnPositiveOverflow  (  nextWriteCursor  )  ; 
}  else   if  (  (  this  .  readCursor  <  this  .  writeCursor  )  &&  (  nextWriteCursor  >=  this  .  readCursor  )  )  { 
this  .  cleanBufferOnPositiveOverflow  (  nextWriteCursor  )  ; 
}  else  { 
} 
}  else  { 
if  (  (  this  .  readCursor  >  this  .  writeCursor  )  &&  (  nextWriteCursor  >=  this  .  readCursor  )  )  { 
this  .  cleanBufferOnPositiveOverflow  (  nextWriteCursor  )  ; 
}  else  { 
} 
} 
} 

private   void   cleanBufferOnPositiveOverflow  (  int   nextWriteCursor  )  { 
int   oldRead  =  dec  (  this  .  readCursor  ,  1  )  ; 
this  .  readCursor  =  inc  (  nextWriteCursor  ,  1  )  ; 
while  (  nextWriteCursor  !=  oldRead  )  { 
if  (  this  .  queue  [  nextWriteCursor  ]  ==  null  )  { 
return  ; 
} 
RtpPacket   removed  =  this  .  queue  [  nextWriteCursor  ]  ; 
this  .  queue  [  nextWriteCursor  ]  =  null  ; 
this  .  duration  -=  removed  .  getDuration  (  )  ; 
nextWriteCursor  =  dec  (  nextWriteCursor  ,  1  )  ; 
} 
} 

private   int   inc  (  int   a  ,  int   diff  )  { 
int   res  =  a  +  diff  ; 
if  (  res  >=  queue  .  length  )  { 
res  =  res  -  queue  .  length  ; 
} 
return   res  ; 
} 

private   int   dec  (  int   a  ,  int   diff  )  { 
int   res  =  a  -  diff  ; 
if  (  res  <  0  )  { 
res  =  queue  .  length  +  res  ; 
} 
return   res  ; 
} 





public   void   softReset  (  )  { 
duration  =  0  ; 
drift  =  0  ; 
r  =  0  ; 
s  =  0  ; 
ready  =  false  ; 
readStarted  =  true  ; 
writeStarted  =  false  ; 
readCursor  =  0  ; 
writeCursor  =  0  ; 
lastRtpPackReceivedTimeStamp  =  0  ; 
} 

public   void   reset  (  )  { 
softReset  (  )  ; 
clock  .  reset  (  )  ; 
seqNoCycles  =  0  ; 
firstSeqNumberReceived  =  0  ; 
} 





public   RtpPacket   read  (  long   timestamp  )  { 
if  (  !  ready  )  { 
return   null  ; 
} 
if  (  !  readStarted  )  { 
readStarted  =  true  ; 
drift  =  queue  [  0  ]  .  getTime  (  )  -  timestamp  ; 
} 
this  .  timestamp  =  timestamp  +  drift  ; 
if  (  duration  ==  0  )  { 
return   null  ; 
} 
RtpPacket   packet  =  queue  [  readCursor  ]  ; 
queue  [  readCursor  ]  =  null  ; 
duration  -=  packet  .  getDuration  (  )  ; 
readCursor  =  inc  (  readCursor  ,  1  )  ; 
while  (  duration  >=  0  &&  queue  [  readCursor  ]  ==  null  )  { 
this  .  readCursor  =  inc  (  this  .  readCursor  ,  1  )  ; 
} 
return   packet  ; 
} 

public   int   getSeqNoCycles  (  )  { 
return   seqNoCycles  ; 
} 

public   RtpPacket   getLastRtpPacketRecd  (  )  { 
return   queue  [  writeCursor  ]  ; 
} 

public   long   getLastRtpPackReceivedTimeStamp  (  )  { 
return   lastRtpPackReceivedTimeStamp  ; 
} 





public   int   getExpectedPacketCount  (  )  { 
RtpPacket   rtpPacket  =  getLastRtpPacketRecd  (  )  ; 
if  (  rtpPacket  !=  null  )  { 
return   rtpPacket  .  getSeqNumber  (  )  +  this  .  seqNoCycles  *  65535  -  firstSeqNumberReceived  +  1  ; 
} 
return   0  ; 
} 
} 

