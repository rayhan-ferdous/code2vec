package   org  .  limewire  .  nio  .  ssl  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  net  .  SocketAddress  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  ClosedChannelException  ; 
import   java  .  util  .  concurrent  .  Executor  ; 
import   java  .  util  .  concurrent  .  atomic  .  AtomicBoolean  ; 
import   javax  .  net  .  ssl  .  SSLContext  ; 
import   javax  .  net  .  ssl  .  SSLEngine  ; 
import   javax  .  net  .  ssl  .  SSLEngineResult  ; 
import   javax  .  net  .  ssl  .  SSLSession  ; 
import   javax  .  net  .  ssl  .  SSLEngineResult  .  HandshakeStatus  ; 
import   javax  .  net  .  ssl  .  SSLEngineResult  .  Status  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  limewire  .  nio  .  ByteBufferCache  ; 
import   org  .  limewire  .  nio  .  channel  .  ChannelReader  ; 
import   org  .  limewire  .  nio  .  channel  .  ChannelWriter  ; 
import   org  .  limewire  .  nio  .  channel  .  InterestReadableByteChannel  ; 
import   org  .  limewire  .  nio  .  channel  .  InterestWritableByteChannel  ; 
import   org  .  limewire  .  nio  .  observer  .  Shutdownable  ; 
import   org  .  limewire  .  nio  .  observer  .  WriteObserver  ; 
import   org  .  limewire  .  util  .  BufferUtils  ; 
import   org  .  limewire  .  util  .  FileUtils  ; 





class   SSLReadWriteChannel   implements   InterestReadableByteChannel  ,  InterestWritableByteChannel  ,  ChannelReader  ,  ChannelWriter  { 

private   static   final   Log   LOG  =  LogFactory  .  getLog  (  SSLReadWriteChannel  .  class  )  ; 


private   final   SSLContext   context  ; 


private   final   Executor   sslBlockingExecutor  ; 


private   SSLEngine   engine  ; 


private   ByteBuffer   readIncoming  ; 


private   ByteBuffer   readOutgoing  ; 


private   ByteBuffer   writeOutgoing  ; 


private   volatile   InterestReadableByteChannel   readSink  ; 


private   volatile   InterestWritableByteChannel   writeSink  ; 


private   volatile   WriteObserver   writeWanter  ; 


private   volatile   boolean   needsHandshakeWrap  =  false  ; 


private   volatile   boolean   needsHandshakeUnwrap  =  false  ; 


private   volatile   boolean   readDataLeft  =  false  ; 


private   final   AtomicBoolean   firstReadDone  =  new   AtomicBoolean  (  false  )  ; 

private   volatile   long   readConsumed  ; 

private   volatile   long   readProduced  ; 

private   volatile   long   writeConsumed  ; 

private   volatile   long   writeProduced  ; 










private   volatile   boolean   shutdown  =  false  ; 

private   final   Object   initLock  =  new   Object  (  )  ; 
















private   final   Object   taskLock  =  new   Object  (  )  ; 

private   volatile   boolean   taskScheduled  =  false  ; 













private   boolean   readInterest  =  false  ; 

private   final   Object   readInterestLock  =  new   Object  (  )  ; 

private   final   ByteBufferCache   byteBufferCache  ; 

private   final   Executor   networkExecutor  ; 

public   SSLReadWriteChannel  (  SSLContext   context  ,  Executor   sslBlockingExecutor  ,  ByteBufferCache   byteBufferCache  ,  Executor   networkExecutor  )  { 
this  .  sslBlockingExecutor  =  sslBlockingExecutor  ; 
this  .  context  =  context  ; 
this  .  byteBufferCache  =  byteBufferCache  ; 
this  .  networkExecutor  =  networkExecutor  ; 
} 










void   initialize  (  SocketAddress   addr  ,  String  [  ]  cipherSuites  ,  boolean   clientMode  ,  boolean   needClientAuth  )  { 
synchronized  (  initLock  )  { 
if  (  shutdown  )  { 
LOG  .  debug  (  "Not initializing because already shutdown."  )  ; 
return  ; 
} 
if  (  addr  !=  null  )  { 
if  (  !  (  addr   instanceof   InetSocketAddress  )  )  throw   new   IllegalArgumentException  (  "unsupported SocketAddress"  )  ; 
InetSocketAddress   iaddr  =  (  InetSocketAddress  )  addr  ; 
String   host  =  iaddr  .  getAddress  (  )  .  getHostAddress  (  )  ; 
int   port  =  iaddr  .  getPort  (  )  ; 
engine  =  context  .  createSSLEngine  (  host  ,  port  )  ; 
}  else  { 
engine  =  context  .  createSSLEngine  (  )  ; 
} 
engine  .  setEnabledCipherSuites  (  cipherSuites  )  ; 
engine  .  setUseClientMode  (  clientMode  )  ; 
if  (  !  clientMode  )  { 
engine  .  setWantClientAuth  (  needClientAuth  )  ; 
engine  .  setNeedClientAuth  (  needClientAuth  )  ; 
} 
SSLSession   session  =  engine  .  getSession  (  )  ; 
readIncoming  =  byteBufferCache  .  getHeap  (  session  .  getPacketBufferSize  (  )  )  ; 
writeOutgoing  =  byteBufferCache  .  getHeap  (  session  .  getPacketBufferSize  (  )  )  ; 
if  (  LOG  .  isTraceEnabled  (  )  )  LOG  .  trace  (  "Initialized engine: "  +  engine  +  ", session: "  +  session  )  ; 
} 
} 

public   int   read  (  ByteBuffer   dst  )  throws   IOException  { 
if  (  shutdown  )  throw   new   ClosedChannelException  (  )  ; 
if  (  taskScheduled  )  return   0  ; 
int   transferred  =  0  ; 
if  (  readOutgoing  !=  null  &&  readOutgoing  .  position  (  )  >  0  )  { 
transferred  +=  BufferUtils  .  transfer  (  readOutgoing  ,  dst  )  ; 
if  (  readOutgoing  .  hasRemaining  (  )  )  { 
LOG  .  debug  (  "Transferred less than we have left!"  )  ; 
return   transferred  ; 
} 
} 
while  (  true  )  { 
if  (  firstReadDone  .  get  (  )  &&  !  dst  .  hasRemaining  (  )  &&  engine  .  getHandshakeStatus  (  )  ==  HandshakeStatus  .  NOT_HANDSHAKING  )  { 
LOG  .  debug  (  "No room left to transfer data, exiting"  )  ; 
return   transferred  ; 
} 
int   read  =  -  1  ; 
while  (  readIncoming  .  hasRemaining  (  )  &&  (  read  =  readSink  .  read  (  readIncoming  )  )  >  0  )  ; 
if  (  read  ==  -  1  &&  readIncoming  .  position  (  )  ==  0  )  { 
LOG  .  debug  (  "Read EOF, no data to transfer.  Connection finished"  )  ; 
return  -  1  ; 
} 
if  (  readIncoming  .  position  (  )  ==  0  )  { 
LOG  .  debug  (  "Unable to read anything, exiting read loop"  )  ; 
return   0  ; 
} 
readIncoming  .  flip  (  )  ; 
SSLEngineResult   result  =  engine  .  unwrap  (  readIncoming  ,  dst  )  ; 
readProduced  +=  result  .  bytesProduced  (  )  ; 
readConsumed  +=  result  .  bytesConsumed  (  )  ; 
transferred  +=  result  .  bytesProduced  (  )  ; 
SSLEngineResult  .  Status   status  =  result  .  getStatus  (  )  ; 
if  (  status  ==  Status  .  BUFFER_OVERFLOW  )  { 
if  (  readOutgoing  ==  null  )  { 
synchronized  (  initLock  )  { 
if  (  shutdown  )  throw   new   IOException  (  "Shutdown while sizing"  )  ; 
readOutgoing  =  byteBufferCache  .  getHeap  (  engine  .  getSession  (  )  .  getApplicationBufferSize  (  )  )  ; 
} 
} 
result  =  engine  .  unwrap  (  readIncoming  ,  readOutgoing  )  ; 
readProduced  +=  result  .  bytesProduced  (  )  ; 
readConsumed  +=  result  .  bytesConsumed  (  )  ; 
status  =  result  .  getStatus  (  )  ; 
if  (  status  ==  Status  .  BUFFER_OVERFLOW  )  { 
if  (  readIncoming  .  position  (  )  ==  0  &&  readIncoming  .  capacity  (  )  ==  16665  &&  engine  .  getSession  (  )  .  getPacketBufferSize  (  )  ==  33049  )  { 
synchronized  (  initLock  )  { 
if  (  shutdown  )  throw   new   IOException  (  "Shutdown while resizing."  )  ; 
ByteBuffer   newIncoming  =  byteBufferCache  .  getHeap  (  engine  .  getSession  (  )  .  getPacketBufferSize  (  )  )  ; 
BufferUtils  .  transfer  (  readIncoming  ,  newIncoming  ,  false  )  ; 
newIncoming  .  flip  (  )  ; 
assert   newIncoming  .  limit  (  )  ==  readIncoming  .  position  (  )  ; 
assert   newIncoming  .  position  (  )  ==  0  ; 
byteBufferCache  .  release  (  readIncoming  )  ; 
readIncoming  =  newIncoming  ; 
assert   readOutgoing  .  position  (  )  ==  0  ; 
byteBufferCache  .  release  (  readOutgoing  )  ; 
readOutgoing  =  byteBufferCache  .  getHeap  (  engine  .  getSession  (  )  .  getApplicationBufferSize  (  )  )  ; 
result  =  engine  .  unwrap  (  readIncoming  ,  readOutgoing  )  ; 
readProduced  +=  result  .  bytesProduced  (  )  ; 
readConsumed  +=  result  .  bytesConsumed  (  )  ; 
status  =  result  .  getStatus  (  )  ; 
if  (  status  ==  Status  .  BUFFER_OVERFLOW  )  throw   new   IllegalStateException  (  "tried resizing, but still not enough room in fallback TLS buffer!  readOutgoing: "  +  readOutgoing  +  ", readIncoming: "  +  readIncoming  +  ", packet size: "  +  engine  .  getSession  (  )  .  getPacketBufferSize  (  )  +  ", appl size: "  +  engine  .  getSession  (  )  .  getApplicationBufferSize  (  )  )  ; 
} 
}  else  { 
throw   new   IllegalStateException  (  "cannot resize, and not enough room in fallback TLS buffer!  readOutgoing: "  +  readOutgoing  +  ", readIncoming: "  +  readIncoming  +  ", packet size: "  +  engine  .  getSession  (  )  .  getPacketBufferSize  (  )  +  ", appl size: "  +  engine  .  getSession  (  )  .  getApplicationBufferSize  (  )  )  ; 
} 
} 
transferred  +=  BufferUtils  .  transfer  (  readOutgoing  ,  dst  )  ; 
} 
firstReadDone  .  set  (  true  )  ; 
if  (  readIncoming  .  hasRemaining  (  )  )  { 
readDataLeft  =  true  ; 
readIncoming  .  compact  (  )  ; 
}  else  { 
readDataLeft  =  false  ; 
readIncoming  .  clear  (  )  ; 
} 
if  (  LOG  .  isDebugEnabled  (  )  )  LOG  .  debug  (  "Read unwrap result: "  +  result  +  ", transferred: "  +  transferred  )  ; 
if  (  status  ==  Status  .  BUFFER_UNDERFLOW  )  { 
if  (  transferred  ==  0  &&  read  ==  -  1  )  { 
LOG  .  debug  (  "Read EOF & underflow when unwrapping.  Connection finished"  )  ; 
return  -  1  ; 
}  else  { 
return   transferred  ; 
} 
} 
if  (  status  ==  Status  .  CLOSED  )  { 
if  (  transferred  ==  0  )  return  -  1  ;  else   return   transferred  ; 
} 
if  (  !  processHandshakeResult  (  true  ,  false  ,  result  .  getHandshakeStatus  (  )  )  )  return   transferred  ; 
} 
} 








private   boolean   processHandshakeResult  (  boolean   reading  ,  boolean   writing  ,  HandshakeStatus   hs  )  { 
if  (  LOG  .  isTraceEnabled  (  )  )  LOG  .  trace  (  "Processing result from: "  +  engine  +  ", result: "  +  hs  )  ; 
needsHandshakeWrap  =  false  ; 
needsHandshakeUnwrap  =  false  ; 
switch  (  hs  )  { 
case   NEED_TASK  : 
needTask  (  )  ; 
return   false  ; 
case   NEED_WRAP  : 
needsHandshakeWrap  =  true  ; 
readSink  .  interestRead  (  false  )  ; 
writeSink  .  interestWrite  (  this  ,  true  )  ; 
return   writing  ; 
case   NEED_UNWRAP  : 
writeSink  .  interestWrite  (  null  ,  false  )  ; 
synchronized  (  readInterestLock  )  { 
needsHandshakeUnwrap  =  true  ; 
readSink  .  interestRead  (  true  )  ; 
} 
if  (  readDataLeft  &&  !  reading  )  networkExecutor  .  execute  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
read  (  BufferUtils  .  getEmptyBuffer  (  )  )  ; 
}  catch  (  IOException   iox  )  { 
FileUtils  .  close  (  SSLReadWriteChannel  .  this  )  ; 
} 
} 
}  )  ; 
return   reading  ; 
case   FINISHED  : 
synchronized  (  readInterestLock  )  { 
readSink  .  interestRead  (  readInterest  )  ; 
} 
writeSink  .  interestWrite  (  this  ,  true  )  ; 
case   NOT_HANDSHAKING  : 
default  : 
return   true  ; 
} 
} 


private   void   needTask  (  )  { 
synchronized  (  taskLock  )  { 
taskScheduled  =  true  ; 
readSink  .  interestRead  (  false  )  ; 
writeSink  .  interestWrite  (  null  ,  false  )  ; 
} 
while  (  true  )  { 
final   Runnable   runner  =  engine  .  getDelegatedTask  (  )  ; 
if  (  runner  ==  null  )  { 
sslBlockingExecutor  .  execute  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
synchronized  (  taskLock  )  { 
taskScheduled  =  false  ; 
} 
HandshakeStatus   status  =  engine  .  getHandshakeStatus  (  )  ; 
if  (  LOG  .  isDebugEnabled  (  )  )  LOG  .  debug  (  "Task(s) finished, status: "  +  status  )  ; 
processHandshakeResult  (  false  ,  false  ,  status  )  ; 
} 
}  )  ; 
break  ; 
}  else  { 
sslBlockingExecutor  .  execute  (  runner  )  ; 
} 
} 
} 

public   int   write  (  ByteBuffer   src  )  throws   IOException  { 
if  (  shutdown  )  throw   new   ClosedChannelException  (  )  ; 
if  (  taskScheduled  )  return   0  ; 
int   consumed  =  0  ; 
do  { 
boolean   wasEmpty  =  writeOutgoing  .  position  (  )  ==  0  ; 
SSLEngineResult   result  =  engine  .  wrap  (  src  ,  writeOutgoing  )  ; 
writeProduced  +=  result  .  bytesProduced  (  )  ; 
writeConsumed  +=  result  .  bytesConsumed  (  )  ; 
if  (  LOG  .  isDebugEnabled  (  )  )  LOG  .  debug  (  "Wrap result: "  +  result  )  ; 
consumed  +=  result  .  bytesConsumed  (  )  ; 
SSLEngineResult  .  Status   status  =  result  .  getStatus  (  )  ; 
if  (  status  ==  Status  .  CLOSED  &&  !  isOpen  (  )  )  throw   new   ClosedChannelException  (  )  ; 
if  (  !  processHandshakeResult  (  false  ,  true  ,  result  .  getHandshakeStatus  (  )  )  )  break  ; 
if  (  status  ==  Status  .  BUFFER_OVERFLOW  )  { 
if  (  wasEmpty  )  throw   new   IllegalStateException  (  "outgoing TLS buffer not large enough!"  )  ;  else   break  ; 
} 
}  while  (  src  .  hasRemaining  (  )  )  ; 
return   consumed  ; 
} 

public   boolean   handleWrite  (  )  throws   IOException  { 
if  (  shutdown  )  throw   new   ClosedChannelException  (  )  ; 
InterestWritableByteChannel   source  =  writeSink  ; 
if  (  source  ==  null  )  throw   new   IllegalStateException  (  "writing with no source."  )  ; 
while  (  true  )  { 
if  (  writeOutgoing  .  position  (  )  >  0  )  { 
writeOutgoing  .  flip  (  )  ; 
writeSink  .  write  (  writeOutgoing  )  ; 
if  (  writeOutgoing  .  hasRemaining  (  )  )  { 
writeOutgoing  .  compact  (  )  ; 
return   true  ; 
} 
writeOutgoing  .  clear  (  )  ; 
} 
if  (  needsHandshakeWrap  )  { 
LOG  .  debug  (  "Forcing a handshake wrap"  )  ; 
write  (  BufferUtils  .  getEmptyBuffer  (  )  )  ; 
if  (  writeOutgoing  .  position  (  )  >  0  )  continue  ; 
} 
WriteObserver   interested  =  writeWanter  ; 
if  (  interested  !=  null  )  { 
if  (  LOG  .  isDebugEnabled  (  )  )  LOG  .  debug  (  "Telling interested parties to write.  (a "  +  interested  +  ")"  )  ; 
interested  .  handleWrite  (  )  ; 
} 
if  (  writeOutgoing  .  position  (  )  ==  0  )  { 
synchronized  (  this  )  { 
if  (  writeWanter  ==  null  )  source  .  interestWrite  (  this  ,  false  )  ; 
} 
return   false  ; 
} 
} 
} 







public   void   shutdown  (  )  { 
synchronized  (  initLock  )  { 
if  (  shutdown  )  return  ; 
if  (  !  isOpen  (  )  )  { 
LOG  .  debug  (  "Shutting down SSL channel"  )  ; 
shutdown  =  true  ; 
} 
} 
if  (  shutdown  )  { 
networkExecutor  .  execute  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  readIncoming  !=  null  )  byteBufferCache  .  release  (  readIncoming  )  ; 
if  (  readOutgoing  !=  null  )  byteBufferCache  .  release  (  readOutgoing  )  ; 
if  (  writeOutgoing  !=  null  )  byteBufferCache  .  release  (  writeOutgoing  )  ; 
} 
}  )  ; 
} 
Shutdownable   observer  =  writeWanter  ; 
if  (  observer  !=  null  )  observer  .  shutdown  (  )  ; 
} 

public   InterestReadableByteChannel   getReadChannel  (  )  { 
return   readSink  ; 
} 

public   void   setReadChannel  (  InterestReadableByteChannel   newChannel  )  { 
this  .  readSink  =  newChannel  ; 
} 

public   InterestWritableByteChannel   getWriteChannel  (  )  { 
return   writeSink  ; 
} 

public   void   setWriteChannel  (  InterestWritableByteChannel   newChannel  )  { 
this  .  writeSink  =  newChannel  ; 
} 

public   void   close  (  )  throws   IOException  { 
readSink  .  close  (  )  ; 
writeSink  .  close  (  )  ; 
} 

public   boolean   isOpen  (  )  { 
return   readSink  !=  null  &&  readSink  .  isOpen  (  )  &&  writeSink  !=  null  &&  writeSink  .  isOpen  (  )  ; 
} 

public   void   handleIOException  (  IOException   iox  )  { 
shutdown  (  )  ; 
} 

public   void   interestRead  (  boolean   status  )  { 
synchronized  (  taskLock  )  { 
synchronized  (  readInterestLock  )  { 
readInterest  =  status  ; 
boolean   interest  =  !  taskScheduled  &&  (  needsHandshakeUnwrap  ||  status  )  ; 
readSink  .  interestRead  (  interest  )  ; 
} 
} 
} 

public   synchronized   void   interestWrite  (  WriteObserver   observer  ,  boolean   status  )  { 
this  .  writeWanter  =  status  ?  observer  :  null  ; 
InterestWritableByteChannel   source  =  writeSink  ; 
if  (  source  !=  null  )  { 
synchronized  (  taskLock  )  { 
source  .  interestWrite  (  this  ,  !  taskScheduled  )  ; 
} 
} 
} 


long   getReadBytesProduced  (  )  { 
return   readProduced  ; 
} 


long   getReadBytesConsumed  (  )  { 
return   readConsumed  ; 
} 


long   getWrittenBytesProduced  (  )  { 
return   writeProduced  ; 
} 


long   getWrittenBytesConsumed  (  )  { 
return   writeConsumed  ; 
} 


SSLSession   getSession  (  )  { 
return   engine  !=  null  ?  engine  .  getSession  (  )  :  null  ; 
} 


boolean   isHandshaking  (  )  { 
return  !  firstReadDone  .  get  (  )  ||  engine  .  getHandshakeStatus  (  )  !=  HandshakeStatus  .  NOT_HANDSHAKING  ; 
} 

public   boolean   hasBufferedOutput  (  )  { 
InterestWritableByteChannel   channel  =  this  .  writeSink  ; 
return   writeOutgoing  .  position  (  )  >  0  ||  (  channel  !=  null  &&  channel  .  hasBufferedOutput  (  )  )  ; 
} 
} 

