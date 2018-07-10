package   com  .  plarpebu  .  basicplayer  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   javax  .  sound  .  sampled  .  AudioFileFormat  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  Control  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  FloatControl  ; 
import   javax  .  sound  .  sampled  .  LineUnavailableException  ; 
import   javax  .  sound  .  sampled  .  SourceDataLine  ; 
import   javax  .  sound  .  sampled  .  UnsupportedAudioFileException  ; 
import   javazoom  .  jlgui  .  basicplayer  .  BasicController  ; 
import   javazoom  .  jlgui  .  basicplayer  .  BasicPlayerEvent  ; 
import   javazoom  .  jlgui  .  basicplayer  .  BasicPlayerEventLauncher  ; 
import   javazoom  .  jlgui  .  basicplayer  .  BasicPlayerException  ; 
import   javazoom  .  jlgui  .  basicplayer  .  BasicPlayerListener  ; 
import   javazoom  .  spi  .  PropertiesContainer  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  tritonus  .  share  .  sampled  .  TAudioFormat  ; 
import   org  .  tritonus  .  share  .  sampled  .  file  .  TAudioFileFormat  ; 




public   class   BasicMP3Player   extends   BasicPlayer   implements   BasicController  ,  Runnable  { 

private   static   final   int   EXTERNAL_BUFFER_SIZE  =  4000  *  4  ; 

private   static   final   int   SKIP_INACCURACY_SIZE  =  1200  ; 

private   Thread   m_thread  =  null  ; 

private   Object   m_dataSource  ; 

private   AudioInputStream   m_encodedaudioInputStream  ; 

private   int   encodedLength  =  -  1  ; 

private   AudioInputStream   m_audioInputStream  ; 

private   AudioFileFormat   m_audioFileFormat  ; 

private   SourceDataLine   m_line  ; 

private   FloatControl   m_gainControl  ; 

private   FloatControl   m_panControl  ; 

private   int   lineBufferSize  =  -  1  ; 

private   long   threadSleep  =  -  1  ; 

private   static   Log   log  =  LogFactory  .  getLog  (  BasicPlayer  .  class  )  ; 





public   static   final   int   UNKNOWN  =  -  1  ; 

public   static   final   int   PLAYING  =  0  ; 

public   static   final   int   PAUSED  =  1  ; 

public   static   final   int   STOPPED  =  2  ; 

public   static   final   int   OPENED  =  3  ; 

public   static   final   int   SEEKING  =  4  ; 

private   int   m_status  =  UNKNOWN  ; 

private   Collection   m_listeners  =  null  ; 

private   Map   empty_map  =  new   HashMap  (  )  ; 




public   BasicMP3Player  (  )  { 
setSupportedFileTypeExtensions  (  )  ; 
m_dataSource  =  null  ; 
m_listeners  =  new   ArrayList  (  )  ; 
reset  (  )  ; 
} 

protected   void   reset  (  )  { 
m_status  =  UNKNOWN  ; 
if  (  m_audioInputStream  !=  null  )  { 
synchronized  (  m_audioInputStream  )  { 
closeStream  (  )  ; 
} 
} 
m_audioInputStream  =  null  ; 
m_audioFileFormat  =  null  ; 
m_encodedaudioInputStream  =  null  ; 
encodedLength  =  -  1  ; 
if  (  m_line  !=  null  )  { 
m_line  .  stop  (  )  ; 
m_line  .  close  (  )  ; 
m_line  =  null  ; 
} 
m_gainControl  =  null  ; 
m_panControl  =  null  ; 
} 

public   void   addBasicPlayerListener  (  BasicPlayerListener   bpl  )  { 
m_listeners  .  add  (  bpl  )  ; 
} 








public   void   setLineBufferSize  (  int   size  )  { 
lineBufferSize  =  size  ; 
} 






public   int   getLineBufferSize  (  )  { 
return   lineBufferSize  ; 
} 







public   void   setSleepTime  (  long   time  )  { 
threadSleep  =  time  ; 
} 






public   long   getSleepTime  (  )  { 
return   threadSleep  ; 
} 






public   int   getStatus  (  )  { 
return   m_status  ; 
} 




public   void   open  (  File   file  )  throws   BasicPlayerException  { 
log  .  info  (  "open("  +  file  +  ")"  )  ; 
if  (  file  !=  null  )  { 
m_dataSource  =  file  ; 
initAudioInputStream  (  )  ; 
} 
} 




public   void   open  (  URL   url  )  throws   BasicPlayerException  { 
log  .  info  (  "open("  +  url  +  ")"  )  ; 
if  (  url  !=  null  )  { 
m_dataSource  =  url  ; 
initAudioInputStream  (  )  ; 
} 
} 




public   void   open  (  InputStream   inputStream  )  throws   BasicPlayerException  { 
log  .  info  (  "open("  +  inputStream  +  ")"  )  ; 
if  (  inputStream  !=  null  )  { 
m_dataSource  =  inputStream  ; 
initAudioInputStream  (  )  ; 
} 
} 






private   void   initAudioInputStream  (  )  throws   BasicPlayerException  { 
try  { 
reset  (  )  ; 
notifyEvent  (  BasicPlayerEvent  .  OPENING  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  m_dataSource  )  ; 
if  (  m_dataSource   instanceof   URL  )  { 
initAudioInputStream  (  (  URL  )  m_dataSource  )  ; 
}  else   if  (  m_dataSource   instanceof   File  )  { 
initAudioInputStream  (  (  File  )  m_dataSource  )  ; 
}  else   if  (  m_dataSource   instanceof   InputStream  )  { 
initAudioInputStream  (  (  InputStream  )  m_dataSource  )  ; 
} 
createLine  (  )  ; 
Map   properties  =  null  ; 
if  (  m_audioFileFormat   instanceof   TAudioFileFormat  )  { 
properties  =  (  (  TAudioFileFormat  )  m_audioFileFormat  )  .  properties  (  )  ; 
properties  =  deepCopy  (  properties  )  ; 
}  else   properties  =  new   HashMap  (  )  ; 
if  (  m_audioFileFormat  .  getByteLength  (  )  >  0  )  properties  .  put  (  "audio.length.bytes"  ,  new   Integer  (  m_audioFileFormat  .  getByteLength  (  )  )  )  ; 
if  (  m_audioFileFormat  .  getFrameLength  (  )  >  0  )  properties  .  put  (  "audio.length.frames"  ,  new   Integer  (  m_audioFileFormat  .  getFrameLength  (  )  )  )  ; 
if  (  m_audioFileFormat  .  getType  (  )  !=  null  )  properties  .  put  (  "audio.type"  ,  (  m_audioFileFormat  .  getType  (  )  .  toString  (  )  )  )  ; 
AudioFormat   audioFormat  =  m_audioFileFormat  .  getFormat  (  )  ; 
if  (  audioFormat  .  getFrameRate  (  )  >  0  )  properties  .  put  (  "audio.framerate.fps"  ,  new   Float  (  audioFormat  .  getFrameRate  (  )  )  )  ; 
if  (  audioFormat  .  getFrameSize  (  )  >  0  )  properties  .  put  (  "audio.framesize.bytes"  ,  new   Integer  (  audioFormat  .  getFrameSize  (  )  )  )  ; 
if  (  audioFormat  .  getSampleRate  (  )  >  0  )  properties  .  put  (  "audio.samplerate.hz"  ,  new   Float  (  audioFormat  .  getSampleRate  (  )  )  )  ; 
if  (  audioFormat  .  getSampleSizeInBits  (  )  >  0  )  properties  .  put  (  "audio.samplesize.bits"  ,  new   Integer  (  audioFormat  .  getSampleSizeInBits  (  )  )  )  ; 
if  (  audioFormat  .  getChannels  (  )  >  0  )  properties  .  put  (  "audio.channels"  ,  new   Integer  (  audioFormat  .  getChannels  (  )  )  )  ; 
if  (  audioFormat   instanceof   TAudioFormat  )  { 
Map   addproperties  =  (  (  TAudioFormat  )  audioFormat  )  .  properties  (  )  ; 
properties  .  putAll  (  addproperties  )  ; 
} 
Iterator   it  =  m_listeners  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
BasicPlayerListener   bpl  =  (  BasicPlayerListener  )  it  .  next  (  )  ; 
bpl  .  opened  (  m_dataSource  ,  properties  )  ; 
} 
m_status  =  OPENED  ; 
notifyEvent  (  BasicPlayerEvent  .  OPENED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
}  catch  (  LineUnavailableException   e  )  { 
throw   new   BasicPlayerException  (  e  )  ; 
}  catch  (  UnsupportedAudioFileException   e  )  { 
throw   new   BasicPlayerException  (  e  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   BasicPlayerException  (  e  )  ; 
} 
} 




private   void   initAudioInputStream  (  File   file  )  throws   UnsupportedAudioFileException  ,  IOException  { 
m_audioInputStream  =  AudioSystem  .  getAudioInputStream  (  file  )  ; 
m_audioFileFormat  =  AudioSystem  .  getAudioFileFormat  (  file  )  ; 
} 




private   void   initAudioInputStream  (  URL   url  )  throws   UnsupportedAudioFileException  ,  IOException  { 
m_audioInputStream  =  AudioSystem  .  getAudioInputStream  (  url  )  ; 
m_audioFileFormat  =  AudioSystem  .  getAudioFileFormat  (  url  )  ; 
} 




private   void   initAudioInputStream  (  InputStream   inputStream  )  throws   UnsupportedAudioFileException  ,  IOException  { 
m_audioInputStream  =  AudioSystem  .  getAudioInputStream  (  inputStream  )  ; 
m_audioFileFormat  =  AudioSystem  .  getAudioFileFormat  (  inputStream  )  ; 
} 




protected   void   initLine  (  )  throws   LineUnavailableException  { 
log  .  info  (  "initLine()"  )  ; 
if  (  m_line  ==  null  )  createLine  (  )  ; 
if  (  !  m_line  .  isOpen  (  )  )  { 
openLine  (  )  ; 
}  else  { 
AudioFormat   lineAudioFormat  =  m_line  .  getFormat  (  )  ; 
AudioFormat   audioInputStreamFormat  =  m_audioInputStream  ==  null  ?  null  :  m_audioInputStream  .  getFormat  (  )  ; 
if  (  !  lineAudioFormat  .  equals  (  audioInputStreamFormat  )  )  { 
m_line  .  close  (  )  ; 
openLine  (  )  ; 
} 
} 
} 











private   void   createLine  (  )  throws   LineUnavailableException  { 
log  .  info  (  "Create Line"  )  ; 
if  (  m_line  ==  null  )  { 
AudioFormat   sourceFormat  =  m_audioInputStream  .  getFormat  (  )  ; 
log  .  info  (  "Create Line : Source format : "  +  sourceFormat  .  toString  (  )  )  ; 
AudioFormat   targetFormat  =  new   AudioFormat  (  AudioFormat  .  Encoding  .  PCM_SIGNED  ,  sourceFormat  .  getSampleRate  (  )  ,  16  ,  sourceFormat  .  getChannels  (  )  ,  sourceFormat  .  getChannels  (  )  *  2  ,  sourceFormat  .  getSampleRate  (  )  ,  false  )  ; 
log  .  info  (  "Create Line : Target format: "  +  targetFormat  )  ; 
m_encodedaudioInputStream  =  m_audioInputStream  ; 
try  { 
encodedLength  =  m_encodedaudioInputStream  .  available  (  )  ; 
}  catch  (  IOException   e  )  { 
log  .  error  (  "Cannot get m_encodedaudioInputStream.available()"  ,  e  )  ; 
} 
m_audioInputStream  =  AudioSystem  .  getAudioInputStream  (  targetFormat  ,  m_audioInputStream  )  ; 
AudioFormat   audioFormat  =  m_audioInputStream  .  getFormat  (  )  ; 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  audioFormat  ,  AudioSystem  .  NOT_SPECIFIED  )  ; 
m_line  =  (  SourceDataLine  )  AudioSystem  .  getLine  (  info  )  ; 
Control  [  ]  c  =  m_line  .  getControls  (  )  ; 
for  (  int   p  =  0  ;  p  <  c  .  length  ;  p  ++  )  { 
log  .  debug  (  "Controls : "  +  c  [  p  ]  .  toString  (  )  )  ; 
} 
if  (  m_line  .  isControlSupported  (  FloatControl  .  Type  .  MASTER_GAIN  )  )  { 
m_gainControl  =  (  FloatControl  )  m_line  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
log  .  info  (  "Master Gain Control : ["  +  m_gainControl  .  getMinimum  (  )  +  ","  +  m_gainControl  .  getMaximum  (  )  +  "] "  +  m_gainControl  .  getPrecision  (  )  )  ; 
} 
if  (  m_line  .  isControlSupported  (  FloatControl  .  Type  .  PAN  )  )  { 
m_panControl  =  (  FloatControl  )  m_line  .  getControl  (  FloatControl  .  Type  .  PAN  )  ; 
log  .  info  (  "Pan Control : ["  +  m_panControl  .  getMinimum  (  )  +  ","  +  m_panControl  .  getMaximum  (  )  +  "] "  +  m_panControl  .  getPrecision  (  )  )  ; 
} 
} 
} 




private   void   openLine  (  )  throws   LineUnavailableException  { 
if  (  m_line  !=  null  )  { 
AudioFormat   audioFormat  =  m_audioInputStream  .  getFormat  (  )  ; 
int   buffersize  =  lineBufferSize  ; 
if  (  buffersize  <=  0  )  buffersize  =  m_line  .  getBufferSize  (  )  ; 
m_line  .  open  (  audioFormat  ,  buffersize  )  ; 
log  .  info  (  "Open Line : BufferSize="  +  buffersize  )  ; 
} 
} 






protected   void   stopPlayback  (  )  { 
if  (  (  m_status  ==  PLAYING  )  ||  (  m_status  ==  PAUSED  )  )  { 
if  (  m_line  !=  null  )  { 
m_line  .  flush  (  )  ; 
m_line  .  stop  (  )  ; 
} 
m_status  =  STOPPED  ; 
notifyEvent  (  BasicPlayerEvent  .  STOPPED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
synchronized  (  m_audioInputStream  )  { 
closeStream  (  )  ; 
} 
log  .  info  (  "stopPlayback() completed"  )  ; 
} 
} 





protected   void   pausePlayback  (  )  { 
if  (  m_line  !=  null  )  { 
if  (  m_status  ==  PLAYING  )  { 
m_line  .  flush  (  )  ; 
m_line  .  stop  (  )  ; 
m_status  =  PAUSED  ; 
log  .  info  (  "pausePlayback() completed"  )  ; 
notifyEvent  (  BasicPlayerEvent  .  PAUSED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
} 
} 
} 





protected   void   resumePlayback  (  )  { 
if  (  m_line  !=  null  )  { 
if  (  m_status  ==  PAUSED  )  { 
m_line  .  start  (  )  ; 
m_status  =  PLAYING  ; 
log  .  info  (  "resumePlayback() completed"  )  ; 
notifyEvent  (  BasicPlayerEvent  .  RESUMED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
} 
} 
} 




protected   void   startPlayback  (  )  throws   BasicPlayerException  { 
if  (  m_status  ==  STOPPED  )  initAudioInputStream  (  )  ; 
if  (  m_status  ==  OPENED  )  { 
log  .  info  (  "startPlayback called"  )  ; 
if  (  !  (  m_thread  ==  null  ||  !  m_thread  .  isAlive  (  )  )  )  { 
log  .  info  (  "WARNING: old thread still running!!"  )  ; 
int   cnt  =  0  ; 
while  (  m_status  !=  OPENED  )  { 
try  { 
if  (  m_thread  !=  null  )  { 
log  .  info  (  "Waiting ... "  +  cnt  )  ; 
cnt  ++  ; 
Thread  .  sleep  (  1000  )  ; 
if  (  cnt  >  2  )  { 
m_thread  .  interrupt  (  )  ; 
} 
} 
}  catch  (  InterruptedException   e  )  { 
throw   new   BasicPlayerException  (  "Wait error"  ,  e  )  ; 
} 
} 
} 
try  { 
initLine  (  )  ; 
}  catch  (  LineUnavailableException   e  )  { 
throw   new   BasicPlayerException  (  "Cannot init line"  ,  e  )  ; 
} 
log  .  info  (  "Creating new thread"  )  ; 
m_thread  =  new   Thread  (  this  )  ; 
m_thread  .  start  (  )  ; 
if  (  m_line  !=  null  )  { 
m_line  .  start  (  )  ; 
m_status  =  PLAYING  ; 
notifyEvent  (  BasicPlayerEvent  .  PLAYING  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
} 
} 
} 






public   void   run  (  )  { 
log  .  info  (  "Thread Running"  )  ; 
int   nBytesRead  =  1  ; 
byte  [  ]  abData  =  new   byte  [  EXTERNAL_BUFFER_SIZE  ]  ; 
synchronized  (  m_audioInputStream  )  { 
while  (  (  nBytesRead  !=  -  1  )  &&  (  m_status  !=  STOPPED  )  &&  (  m_status  !=  SEEKING  )  &&  (  m_status  !=  UNKNOWN  )  )  { 
if  (  m_status  ==  PLAYING  )  { 
try  { 
nBytesRead  =  m_audioInputStream  .  read  (  abData  ,  0  ,  abData  .  length  )  ; 
if  (  nBytesRead  >=  0  )  { 
byte  [  ]  pcm  =  new   byte  [  nBytesRead  ]  ; 
System  .  arraycopy  (  abData  ,  0  ,  pcm  ,  0  ,  nBytesRead  )  ; 
m_line  .  write  (  abData  ,  0  ,  nBytesRead  )  ; 
int   nEncodedBytes  =  getEncodedStreamPosition  (  )  ; 
Iterator   it  =  m_listeners  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
BasicPlayerListener   bpl  =  (  BasicPlayerListener  )  it  .  next  (  )  ; 
if  (  m_audioInputStream   instanceof   PropertiesContainer  )  { 
Map   properties  =  (  (  PropertiesContainer  )  m_audioInputStream  )  .  properties  (  )  ; 
bpl  .  progress  (  nEncodedBytes  ,  m_line  .  getMicrosecondPosition  (  )  ,  pcm  ,  properties  )  ; 
}  else   bpl  .  progress  (  nEncodedBytes  ,  m_line  .  getMicrosecondPosition  (  )  ,  pcm  ,  empty_map  )  ; 
} 
} 
}  catch  (  IOException   e  )  { 
log  .  error  (  "Thread cannot run()"  ,  e  )  ; 
m_status  =  STOPPED  ; 
notifyEvent  (  BasicPlayerEvent  .  STOPPED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
} 
if  (  threadSleep  >  0  )  { 
try  { 
Thread  .  sleep  (  threadSleep  )  ; 
}  catch  (  InterruptedException   e  )  { 
log  .  error  (  "Thread cannot sleep("  +  threadSleep  +  ")"  ,  e  )  ; 
} 
} 
}  else  { 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  InterruptedException   e  )  { 
log  .  error  (  "Thread cannot sleep(1000)"  ,  e  )  ; 
} 
} 
} 
if  (  m_line  !=  null  )  { 
m_line  .  drain  (  )  ; 
m_line  .  stop  (  )  ; 
m_line  .  close  (  )  ; 
m_line  =  null  ; 
} 
if  (  nBytesRead  ==  -  1  )  { 
notifyEvent  (  BasicPlayerEvent  .  EOM  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
} 
closeStream  (  )  ; 
} 
m_status  =  STOPPED  ; 
notifyEvent  (  BasicPlayerEvent  .  STOPPED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
log  .  info  (  "Thread completed"  )  ; 
} 









protected   long   skipBytes  (  long   bytes  )  throws   BasicPlayerException  { 
long   totalSkipped  =  0  ; 
if  (  m_dataSource   instanceof   File  )  { 
log  .  info  (  "Bytes to skip : "  +  bytes  )  ; 
int   previousStatus  =  m_status  ; 
m_status  =  SEEKING  ; 
long   skipped  =  0  ; 
try  { 
synchronized  (  m_audioInputStream  )  { 
notifyEvent  (  BasicPlayerEvent  .  SEEKING  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
initAudioInputStream  (  )  ; 
if  (  m_audioInputStream  !=  null  )  { 
while  (  totalSkipped  <  (  bytes  -  SKIP_INACCURACY_SIZE  )  )  { 
skipped  =  m_audioInputStream  .  skip  (  bytes  -  totalSkipped  )  ; 
totalSkipped  =  totalSkipped  +  skipped  ; 
log  .  info  (  "Skipped : "  +  totalSkipped  +  "/"  +  bytes  )  ; 
if  (  totalSkipped  ==  -  1  )  throw   new   BasicPlayerException  (  "Skip not supported"  )  ; 
} 
} 
} 
notifyEvent  (  BasicPlayerEvent  .  SEEKED  ,  getEncodedStreamPosition  (  )  ,  -  1  ,  null  )  ; 
m_status  =  OPENED  ; 
if  (  previousStatus  ==  PLAYING  )  startPlayback  (  )  ;  else   if  (  previousStatus  ==  PAUSED  )  { 
startPlayback  (  )  ; 
pausePlayback  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   BasicPlayerException  (  e  )  ; 
} 
} 
return   totalSkipped  ; 
} 









protected   void   notifyEvent  (  int   code  ,  int   position  ,  double   value  ,  Object   description  )  { 
BasicPlayerEventLauncher   trigger  =  new   BasicPlayerEventLauncher  (  code  ,  position  ,  value  ,  description  ,  m_listeners  ,  this  )  ; 
trigger  .  start  (  )  ; 
} 

protected   int   getEncodedStreamPosition  (  )  { 
int   nEncodedBytes  =  -  1  ; 
if  (  m_dataSource   instanceof   File  )  { 
try  { 
if  (  m_encodedaudioInputStream  !=  null  )  { 
nEncodedBytes  =  encodedLength  -  m_encodedaudioInputStream  .  available  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
return   nEncodedBytes  ; 
} 

protected   void   closeStream  (  )  { 
try  { 
if  (  m_audioInputStream  !=  null  )  { 
m_audioInputStream  .  close  (  )  ; 
log  .  info  (  "Stream closed"  )  ; 
} 
}  catch  (  IOException   e  )  { 
log  .  info  (  "Cannot close stream"  ,  e  )  ; 
} 
} 




public   boolean   hasGainControl  (  )  { 
return   m_gainControl  !=  null  ; 
} 




public   float   getGainValue  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   m_gainControl  .  getValue  (  )  ; 
} 
return   0.0F  ; 
} 




public   float   getMaximumGain  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   m_gainControl  .  getMaximum  (  )  ; 
} 
return   0.0F  ; 
} 




public   float   getMinimumGain  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   m_gainControl  .  getMinimum  (  )  ; 
} 
return   0.0F  ; 
} 




public   boolean   hasPanControl  (  )  { 
return   m_panControl  !=  null  ; 
} 




public   float   getPrecision  (  )  { 
if  (  hasPanControl  (  )  )  { 
return   m_panControl  .  getPrecision  (  )  ; 
} 
return   0.0F  ; 
} 




public   float   getPan  (  )  { 
if  (  hasPanControl  (  )  )  { 
return   m_panControl  .  getValue  (  )  ; 
} 
return   0.0F  ; 
} 







private   Map   deepCopy  (  Map   src  )  { 
HashMap   map  =  new   HashMap  (  )  ; 
if  (  src  !=  null  )  { 
Iterator   it  =  src  .  keySet  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   key  =  it  .  next  (  )  ; 
Object   value  =  src  .  get  (  key  )  ; 
map  .  put  (  key  ,  value  )  ; 
} 
} 
return   map  ; 
} 




public   long   seek  (  long   bytes  )  throws   BasicPlayerException  { 
return   skipBytes  (  bytes  )  ; 
} 




public   void   play  (  )  throws   BasicPlayerException  { 
startPlayback  (  )  ; 
} 




public   void   stop  (  )  throws   BasicPlayerException  { 
stopPlayback  (  )  ; 
} 




public   void   pause  (  )  throws   BasicPlayerException  { 
pausePlayback  (  )  ; 
} 




public   void   resume  (  )  throws   BasicPlayerException  { 
resumePlayback  (  )  ; 
} 




public   void   setPan  (  double   fPan  )  throws   BasicPlayerException  { 
if  (  hasPanControl  (  )  )  { 
log  .  debug  (  "Pan : "  +  fPan  )  ; 
m_panControl  .  setValue  (  (  float  )  fPan  )  ; 
notifyEvent  (  BasicPlayerEvent  .  PAN  ,  getEncodedStreamPosition  (  )  ,  fPan  ,  null  )  ; 
}  else   throw   new   BasicPlayerException  (  "Pan control not supported"  )  ; 
} 




public   void   setGain  (  double   fGain  )  throws   BasicPlayerException  { 
if  (  hasGainControl  (  )  )  { 
double   minGainDB  =  getMinimumGain  (  )  ; 
double   ampGainDB  =  (  (  10.0f  /  20.0f  )  *  getMaximumGain  (  )  )  -  getMinimumGain  (  )  ; 
double   cste  =  Math  .  log  (  10.0  )  /  20  ; 
double   valueDB  =  minGainDB  +  (  1  /  cste  )  *  Math  .  log  (  1  +  (  Math  .  exp  (  cste  *  ampGainDB  )  -  1  )  *  fGain  )  ; 
log  .  debug  (  "Gain : "  +  valueDB  )  ; 
m_gainControl  .  setValue  (  (  float  )  valueDB  )  ; 
notifyEvent  (  BasicPlayerEvent  .  GAIN  ,  getEncodedStreamPosition  (  )  ,  fGain  ,  null  )  ; 
}  else   throw   new   BasicPlayerException  (  "Gain control not supported"  )  ; 
} 

public   void   setSupportedFileTypeExtensions  (  )  { 
supportedFileTypeExtensions  =  new   String  [  ]  {  ".mp3"  ,  ".ogg"  }  ; 
} 
} 

