package   cubeworld  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  SocketTimeoutException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Vector  ; 




public   class   SocketSession   implements   Session  { 


private   ArrayList  <  String  >  mOutput  =  new   ArrayList  <  String  >  (  )  ; 


private   Socket   mSocket  ; 


private   ArrayList  <  String  >  mInput  =  new   ArrayList  <  String  >  (  )  ; 


private   ArrayList  <  String  >  mChannels  =  new   ArrayList  <  String  >  (  )  ; 


private   String   mName  =  null  ; 




private   boolean   mAlive  =  true  ; 




private   boolean   mVerified  =  false  ; 




static   final   int   READ_BUFFER_SIZE  =  1024  ; 


private   byte  [  ]  mBuffer  =  new   byte  [  READ_BUFFER_SIZE  ]  ; 


private   int   mBufferIndex  =  0  ; 






public   SocketSession  (  final   Socket   socket  )  { 
mSocket  =  socket  ; 
} 




public   final   Socket   getSocket  (  )  { 
return   mSocket  ; 
} 




public   final   void   setSocket  (  final   Socket   socket  )  { 
mSocket  =  socket  ; 
} 






public   final   Collection  <  String  >  getOutput  (  )  { 
Vector  <  String  >  output  =  new   Vector  <  String  >  (  )  ; 
output  .  addAll  (  mOutput  )  ; 
mOutput  .  clear  (  )  ; 
return   output  ; 
} 






public   final   void   addInput  (  final   String   line  )  { 
mInput  .  add  (  line  )  ; 
} 






public   final   void   addOutput  (  final   String   line  )  { 
mOutput  .  add  (  line  +  "\n"  )  ; 
} 




public   final   void   read  (  )  throws   IOException  { 
InputStream   inputStream  =  mSocket  .  getInputStream  (  )  ; 
try  { 
int   count  =  inputStream  .  read  (  mBuffer  ,  mBufferIndex  ,  mBuffer  .  length  -  mBufferIndex  )  ; 
if  (  0  <  count  )  { 
getLinesFromBuffer  (  count  )  ; 
} 
}  catch  (  SocketTimeoutException   e  )  { 
assert   true  ; 
} 
} 





private   void   getLinesFromBuffer  (  final   int   count  )  { 
int   actualSize  =  mBufferIndex  +  count  ; 
ByteArrayInputStream   bis  =  new   ByteArrayInputStream  (  mBuffer  ,  0  ,  actualSize  )  ; 
mBufferIndex  =  0  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  bis  )  )  ; 
try  { 
String   line  =  reader  .  readLine  (  )  ; 
while  (  null  !=  line  )  { 
addInput  (  line  )  ; 
line  =  reader  .  readLine  (  )  ; 
} 
}  catch  (  IOException   i  )  { 
assert   true  ; 
} 
int   remaining  =  bis  .  available  (  )  ; 
System  .  arraycopy  (  mBuffer  ,  actualSize  -  remaining  ,  mBuffer  ,  0  ,  remaining  )  ; 
mBufferIndex  =  remaining  ; 
} 






public   final   void   write  (  )  throws   IOException  { 
OutputStream   outputStream  =  mSocket  .  getOutputStream  (  )  ; 
for  (  String   line  :  getOutput  (  )  )  { 
outputStream  .  write  (  line  .  getBytes  (  )  )  ; 
} 
outputStream  .  flush  (  )  ; 
} 






public   final   Collection  <  String  >  getInput  (  )  { 
Vector  <  String  >  input  =  new   Vector  <  String  >  (  )  ; 
input  .  addAll  (  mInput  )  ; 
mInput  .  clear  (  )  ; 
return   input  ; 
} 






public   final   boolean   isAlive  (  )  { 
return   mAlive  ; 
} 







public   final   void   setAlive  (  final   boolean   aliveFlag  )  { 
mAlive  =  aliveFlag  ; 
} 






public   final   String   getName  (  )  { 
return   mName  ; 
} 







public   final   void   addChannel  (  final   String   channel  )  { 
if  (  !  mChannels  .  contains  (  channel  )  )  { 
mChannels  .  add  (  channel  )  ; 
} 
} 







public   final   void   removeChannel  (  final   String   channel  )  { 
if  (  mChannels  .  contains  (  channel  )  )  { 
mChannels  .  remove  (  channel  )  ; 
} 
} 




public   final   Collection  <  String  >  getChannels  (  )  { 
return   mChannels  ; 
} 






public   final   boolean   isVerified  (  )  { 
return   mVerified  ; 
} 





public   final   void   setName  (  final   String   name  )  { 
mName  =  name  ; 
} 




public   final   void   setVerified  (  final   boolean   verified  )  { 
mVerified  =  verified  ; 
} 





public   void   setPlayer  (  final   Player   player  )  { 
} 
} 

