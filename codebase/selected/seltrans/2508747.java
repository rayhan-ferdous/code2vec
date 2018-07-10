package   clientserver  ; 

import   com  .  sun  .  speech  .  freetts  .  Voice  ; 
import   com  .  sun  .  speech  .  freetts  .  VoiceManager  ; 
import   com  .  sun  .  speech  .  freetts  .  util  .  Utilities  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  Socket  ; 
import   util  .  TTSServer  ; 










public   class   Server   extends   TTSServer  { 

private   Voice   voice8k  ; 

private   String   voice8kName  =  Utilities  .  getProperty  (  "voice8kName"  ,  "kevin"  )  ; 

private   Voice   voice16k  ; 

private   String   voice16kName  =  Utilities  .  getProperty  (  "voice16kName"  ,  "kevin16"  )  ; 





public   Server  (  )  { 
port  =  Utilities  .  getInteger  (  "port"  ,  5555  )  .  intValue  (  )  ; 
try  { 
VoiceManager   voiceManager  =  VoiceManager  .  getInstance  (  )  ; 
voice8k  =  voiceManager  .  getVoice  (  voice8kName  )  ; 
voice16k  =  voiceManager  .  getVoice  (  voice16kName  )  ; 
voice8k  .  allocate  (  )  ; 
voice16k  .  allocate  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
} 






public   Voice   get8kVoice  (  )  { 
return   voice8k  ; 
} 






public   Voice   get16kVoice  (  )  { 
return   voice16k  ; 
} 






protected   void   spawnProtocolHandler  (  Socket   socket  )  { 
try  { 
SocketTTSHandler   handler  =  new   SocketTTSHandler  (  socket  ,  this  )  ; 
(  new   Thread  (  handler  )  )  .  start  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   static   void   main  (  String  [  ]  args  )  { 
Server   server  =  new   Server  (  )  ; 
(  new   Thread  (  server  )  )  .  start  (  )  ; 
} 
} 




class   SocketTTSHandler   implements   Runnable  { 

private   Voice   voice  ; 

private   Server   server  ; 

private   Socket   socket  ; 

private   SocketAudioPlayer   socketAudioPlayer  ; 

private   BufferedReader   reader  ; 

private   PrintWriter   writer  ; 

private   static   final   int   INVALID_SAMPLE_RATE  =  1  ; 

private   boolean   metrics  =  Utilities  .  getBoolean  (  "metrics"  )  ; 

private   long   requestReceivedTime  ; 

@  SuppressWarnings  (  "unused"  ) 
private   long   requestSpeakTime  ; 








public   SocketTTSHandler  (  Socket   socket  ,  Server   server  )  { 
setSocket  (  socket  )  ; 
this  .  server  =  server  ; 
this  .  socketAudioPlayer  =  new   SocketAudioPlayer  (  socket  )  ; 
} 






private   void   setSocket  (  Socket   socket  )  { 
this  .  socket  =  socket  ; 
if  (  socket  !=  null  )  { 
try  { 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  socket  .  getInputStream  (  )  )  )  ; 
writer  =  new   PrintWriter  (  socket  .  getOutputStream  (  )  ,  true  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
println  (  "Socket reader/writer not instantiated"  )  ; 
throw   new   Error  (  )  ; 
} 
} 
} 






private   void   sendLine  (  String   line  )  { 
writer  .  print  (  line  )  ; 
writer  .  print  (  '\n'  )  ; 
writer  .  flush  (  )  ; 
} 




public   void   run  (  )  { 
try  { 
sendLine  (  "READY"  )  ; 
String   command  =  null  ; 
int   status  ; 
while  (  (  command  =  reader  .  readLine  (  )  )  !=  null  &&  command  .  equals  (  "TTS"  )  )  { 
requestReceivedTime  =  System  .  currentTimeMillis  (  )  ; 
status  =  handleSynthesisRequest  (  )  ; 
if  (  status  ==  INVALID_SAMPLE_RATE  )  { 
println  (  "Invalid sample rate\nexit."  )  ; 
return  ; 
}  else   if  (  metrics  )  { 
System  .  out  .  println  (  "Time To Sending First Byte: "  +  (  socketAudioPlayer  .  getFirstByteSentTime  (  )  -  requestReceivedTime  )  +  " ms"  )  ; 
} 
} 
if  (  command  !=  null  )  { 
if  (  command  .  equals  (  "DONE"  )  )  { 
socket  .  close  (  )  ; 
println  (  "... closed socket connection"  )  ; 
}  else  { 
println  (  "invalid command: "  +  command  )  ; 
} 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 




private   int   handleSynthesisRequest  (  )  { 
try  { 
String   sampleRateLine  =  reader  .  readLine  (  )  ; 
int   sampleRate  =  Integer  .  parseInt  (  sampleRateLine  )  ; 
if  (  sampleRate  ==  8000  )  { 
voice  =  server  .  get8kVoice  (  )  ; 
}  else   if  (  sampleRate  ==  16000  )  { 
voice  =  server  .  get16kVoice  (  )  ; 
}  else  { 
sendLine  (  "-2"  )  ; 
return   INVALID_SAMPLE_RATE  ; 
} 
String   text  =  reader  .  readLine  (  )  ; 
voice  .  setAudioPlayer  (  socketAudioPlayer  )  ; 
voice  .  speak  (  text  )  ; 
sendLine  (  "-1"  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
return   0  ; 
} 






private   void   println  (  String   message  )  { 
System  .  out  .  println  (  message  )  ; 
} 
} 

