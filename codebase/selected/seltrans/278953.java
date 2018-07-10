package   org  .  restlet  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  Channels  ; 
import   java  .  nio  .  channels  .  ClosedChannelException  ; 
import   java  .  nio  .  channels  .  Pipe  ; 
import   java  .  nio  .  channels  .  ReadableByteChannel  ; 
import   java  .  nio  .  channels  .  SelectableChannel  ; 
import   java  .  nio  .  channels  .  SelectionKey  ; 
import   java  .  nio  .  channels  .  Selector  ; 
import   java  .  nio  .  channels  .  WritableByteChannel  ; 
import   java  .  util  .  concurrent  .  ArrayBlockingQueue  ; 
import   java  .  util  .  concurrent  .  BlockingQueue  ; 
import   java  .  util  .  concurrent  .  TimeUnit  ; 
import   org  .  restlet  .  data  .  CharacterSet  ; 
import   org  .  restlet  .  resource  .  Representation  ; 






public   final   class   ByteUtils  { 









public   static   ReadableByteChannel   getChannel  (  InputStream   inputStream  )  throws   IOException  { 
return  (  inputStream  !=  null  )  ?  Channels  .  newChannel  (  inputStream  )  :  null  ; 
} 







public   static   WritableByteChannel   getChannel  (  OutputStream   outputStream  )  throws   IOException  { 
return  (  outputStream  !=  null  )  ?  Channels  .  newChannel  (  outputStream  )  :  null  ; 
} 








public   static   ReadableByteChannel   getChannel  (  final   Representation   representation  )  throws   IOException  { 
final   Pipe   pipe  =  Pipe  .  open  (  )  ; 
Thread   writer  =  new   Thread  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
WritableByteChannel   wbc  =  pipe  .  sink  (  )  ; 
representation  .  write  (  wbc  )  ; 
wbc  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
}  ; 
writer  .  setDaemon  (  false  )  ; 
writer  .  start  (  )  ; 
return   pipe  .  source  (  )  ; 
} 








public   static   InputStream   getStream  (  ReadableByteChannel   readableChannel  )  throws   IOException  { 
return  (  readableChannel  !=  null  )  ?  Channels  .  newInputStream  (  readableChannel  )  :  null  ; 
} 








public   static   InputStream   getStream  (  final   Representation   representation  )  throws   IOException  { 
if  (  representation  !=  null  )  { 
final   PipeStream   pipe  =  new   PipeStream  (  )  ; 
Thread   writer  =  new   Thread  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
OutputStream   os  =  pipe  .  getOutputStream  (  )  ; 
representation  .  write  (  os  )  ; 
os  .  write  (  -  1  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
}  ; 
writer  .  setDaemon  (  false  )  ; 
writer  .  start  (  )  ; 
return   pipe  .  getInputStream  (  )  ; 
}  else  { 
return   null  ; 
} 
} 








public   static   OutputStream   getStream  (  WritableByteChannel   writableChannel  )  { 
OutputStream   result  =  null  ; 
if  (  writableChannel   instanceof   SelectableChannel  )  { 
SelectableChannel   selectableChannel  =  (  SelectableChannel  )  writableChannel  ; 
synchronized  (  selectableChannel  .  blockingLock  (  )  )  { 
if  (  selectableChannel  .  isBlocking  (  )  )  { 
result  =  Channels  .  newOutputStream  (  writableChannel  )  ; 
}  else  { 
result  =  new   NbChannelOutputStream  (  writableChannel  )  ; 
} 
} 
}  else  { 
result  =  new   NbChannelOutputStream  (  writableChannel  )  ; 
} 
return   result  ; 
} 














public   static   String   toString  (  InputStream   inputStream  )  { 
return   toString  (  inputStream  ,  null  )  ; 
} 














public   static   String   toString  (  InputStream   inputStream  ,  CharacterSet   characterSet  )  { 
String   result  =  null  ; 
if  (  inputStream  !=  null  )  { 
try  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
InputStreamReader   isr  =  null  ; 
if  (  characterSet  !=  null  )  { 
isr  =  new   InputStreamReader  (  inputStream  ,  characterSet  .  getName  (  )  )  ; 
}  else  { 
isr  =  new   InputStreamReader  (  inputStream  )  ; 
} 
BufferedReader   br  =  new   BufferedReader  (  isr  )  ; 
int   nextByte  =  br  .  read  (  )  ; 
while  (  nextByte  !=  -  1  )  { 
sb  .  append  (  (  char  )  nextByte  )  ; 
nextByte  =  br  .  read  (  )  ; 
} 
br  .  close  (  )  ; 
result  =  sb  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return   result  ; 
} 











public   static   void   write  (  InputStream   inputStream  ,  OutputStream   outputStream  )  throws   IOException  { 
int   bytesRead  ; 
byte  [  ]  buffer  =  new   byte  [  2048  ]  ; 
while  (  (  bytesRead  =  inputStream  .  read  (  buffer  )  )  >  0  )  { 
outputStream  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
} 
inputStream  .  close  (  )  ; 
} 











public   static   void   write  (  ReadableByteChannel   readableChannel  ,  WritableByteChannel   writableChannel  )  throws   IOException  { 
if  (  (  readableChannel  !=  null  )  &&  (  writableChannel  !=  null  )  )  { 
write  (  Channels  .  newInputStream  (  readableChannel  )  ,  Channels  .  newOutputStream  (  writableChannel  )  )  ; 
} 
} 





private   ByteUtils  (  )  { 
} 







private   static   final   class   PipeStream  { 

private   static   final   long   QUEUE_TIMEOUT  =  5  ; 


private   final   BlockingQueue  <  Integer  >  queue  ; 


public   PipeStream  (  )  { 
this  .  queue  =  new   ArrayBlockingQueue  <  Integer  >  (  1024  )  ; 
} 






public   InputStream   getInputStream  (  )  { 
return   new   InputStream  (  )  { 

private   boolean   endReached  =  false  ; 

@  Override 
public   int   read  (  )  throws   IOException  { 
try  { 
if  (  endReached  )  return  -  1  ; 
Integer   value  =  queue  .  poll  (  QUEUE_TIMEOUT  ,  TimeUnit  .  SECONDS  )  ; 
if  (  value  ==  null  )  { 
throw   new   IOException  (  "Timeout while reading from the queue-based input stream"  )  ; 
} 
endReached  =  (  value  .  intValue  (  )  ==  -  1  )  ; 
return   value  ; 
}  catch  (  InterruptedException   ie  )  { 
throw   new   IOException  (  "Interruption occurred while writing in the queue"  )  ; 
} 
} 
}  ; 
} 






public   OutputStream   getOutputStream  (  )  { 
return   new   OutputStream  (  )  { 

@  Override 
public   void   write  (  int   b  )  throws   IOException  { 
try  { 
if  (  !  queue  .  offer  (  b  ,  QUEUE_TIMEOUT  ,  TimeUnit  .  SECONDS  )  )  { 
throw   new   IOException  (  "Timeout while writing to the queue-based output stream"  )  ; 
} 
}  catch  (  InterruptedException   ie  )  { 
throw   new   IOException  (  "Interruption occurred while writing in the queue"  )  ; 
} 
} 
}  ; 
} 
} 




private   static   final   class   NbChannelOutputStream   extends   OutputStream  { 


private   WritableByteChannel   channel  ; 

private   Selector   selector  ; 

private   SelectionKey   selectionKey  ; 

private   SelectableChannel   selectableChannel  ; 







public   NbChannelOutputStream  (  WritableByteChannel   channel  )  { 
this  .  channel  =  channel  ; 
if  (  !  (  channel   instanceof   SelectableChannel  )  )  { 
throw   new   IllegalArgumentException  (  "Invalid channel provided. Please use only selectable channels."  )  ; 
}  else  { 
this  .  selectableChannel  =  (  SelectableChannel  )  channel  ; 
this  .  selector  =  null  ; 
this  .  selectionKey  =  null  ; 
if  (  this  .  selectableChannel  .  isBlocking  (  )  )  { 
throw   new   IllegalArgumentException  (  "Invalid blocking channel provided. Please use only non-blocking channels."  )  ; 
} 
} 
} 

@  Override 
public   void   write  (  int   b  )  throws   IOException  { 
ByteBuffer   bb  =  ByteBuffer  .  wrap  (  new   byte  [  ]  {  (  byte  )  b  }  )  ; 
if  (  (  this  .  channel  !=  null  )  &&  (  bb  !=  null  )  )  { 
try  { 
int   bytesWritten  ; 
while  (  bb  .  hasRemaining  (  )  )  { 
bytesWritten  =  this  .  channel  .  write  (  bb  )  ; 
if  (  bytesWritten  <  0  )  { 
throw   new   IOException  (  "Unexpected negative number of bytes written."  )  ; 
}  else   if  (  bytesWritten  ==  0  )  { 
registerSelectionKey  (  )  ; 
if  (  getSelector  (  )  .  select  (  10000  )  ==  0  )  { 
throw   new   IOException  (  "Unable to select the channel to write to it. Selection timed out."  )  ; 
} 
} 
} 
}  catch  (  IOException   ioe  )  { 
throw   new   IOException  (  "Unable to write to the non-blocking channel. "  +  ioe  .  getLocalizedMessage  (  )  )  ; 
} 
}  else  { 
throw   new   IOException  (  "Unable to write. Null byte buffer or channel detected."  )  ; 
} 
} 

private   Selector   getSelector  (  )  throws   IOException  { 
if  (  this  .  selector  ==  null  )  this  .  selector  =  Selector  .  open  (  )  ; 
return   this  .  selector  ; 
} 

private   void   registerSelectionKey  (  )  throws   ClosedChannelException  ,  IOException  { 
this  .  selectionKey  =  this  .  selectableChannel  .  register  (  getSelector  (  )  ,  SelectionKey  .  OP_WRITE  )  ; 
} 

@  Override 
public   void   close  (  )  throws   IOException  { 
if  (  this  .  selectionKey  !=  null  )  { 
this  .  selectionKey  .  cancel  (  )  ; 
} 
if  (  this  .  selector  !=  null  )  this  .  selector  .  close  (  )  ; 
super  .  close  (  )  ; 
} 
} 
} 

