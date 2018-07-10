package   com  .  limegroup  .  gnutella  .  gui  .  mp3  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   javax  .  sound  .  sampled  .  AudioFileFormat  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  FloatControl  ; 
import   javax  .  sound  .  sampled  .  LineUnavailableException  ; 
import   javax  .  sound  .  sampled  .  SourceDataLine  ; 
import   javax  .  sound  .  sampled  .  UnsupportedAudioFileException  ; 
import   org  .  limewire  .  util  .  FileUtils  ; 
import   org  .  limewire  .  util  .  GenericsUtils  ; 
import   org  .  limewire  .  util  .  GenericsUtils  .  ScanMode  ; 
import   org  .  tritonus  .  share  .  sampled  .  TAudioFormat  ; 
import   org  .  tritonus  .  share  .  sampled  .  file  .  TAudioFileFormat  ; 
































public   class   LimeAudioFormat  { 





public   static   final   String   AUDIO_LENGTH_BYTES  =  "audio.length.bytes"  ; 

public   static   final   String   AUDIO_LENGTH_FRAMES  =  "audio.length.frames"  ; 

public   static   final   String   AUDIO_TYPE  =  "audio.type"  ; 

public   static   final   String   AUDIO_FRAMERATE_FPS  =  "audio.framerate.fps"  ; 

public   static   final   String   AUDIO_FRAMESIZE_BYTES  =  "audio.framesize.bytes"  ; 

public   static   final   String   AUDIO_SAMPLERATE_HZ  =  "audio.samplerate.hz"  ; 

public   static   final   String   AUDIO_SAMPLESIZE_BITS  =  "audio.samplesize.bits"  ; 

public   static   final   String   AUDIO_CHANNELS  =  "audio.channels"  ; 




private   final   AudioInputStream   audioInputStream  ; 




private   final   SourceDataLine   sourceDataLine  ; 






private   final   AudioInputStream   encodedAudioInputStream  ; 




private   final   AudioSource   audioSource  ; 




private   final   Map  <  String  ,  Object  >  properties  ; 




private   final   long   totalLength  ; 




private   FloatControl   gainControl  ; 






public   LimeAudioFormat  (  File   file  ,  long   position  )  throws   UnsupportedAudioFileException  ,  IOException  ,  LineUnavailableException  { 
this  (  new   AudioSource  (  file  )  ,  position  )  ; 
} 






public   LimeAudioFormat  (  InputStream   stream  ,  long   position  )  throws   UnsupportedAudioFileException  ,  IOException  ,  LineUnavailableException  { 
this  (  new   AudioSource  (  stream  )  ,  position  )  ; 
} 






public   LimeAudioFormat  (  AudioSource   audioSource  ,  long   position  )  throws   UnsupportedAudioFileException  ,  IOException  ,  LineUnavailableException  { 
if  (  audioSource  ==  null  )  throw   new   NullPointerException  (  "Couldn't load song"  )  ; 
this  .  audioSource  =  audioSource  ; 
encodedAudioInputStream  =  createAudioInputStream  (  audioSource  ,  position  )  ; 
properties  =  createProperties  (  audioSource  )  ; 
if  (  audioSource  .  getFile  (  )  !=  null  )  totalLength  =  audioSource  .  getFile  (  )  .  length  (  )  ;  else   totalLength  =  encodedAudioInputStream  .  available  (  )  ; 
audioInputStream  =  createDecodedAudioInputStream  (  encodedAudioInputStream  )  ; 
sourceDataLine  =  createSourceDataLine  (  audioInputStream  )  ; 
} 




















public   static   AudioInputStream   createAudioInputStream  (  AudioSource   source  ,  long   skip  )  throws   UnsupportedAudioFileException  ,  IOException  { 
AudioInputStream   stream  ; 
if  (  source  .  getFile  (  )  !=  null  )  { 
if  (  skip  <  0  ||  skip  >  source  .  getFile  (  )  .  length  (  )  -  10000  )  skip  =  0  ; 
if  (  source  .  getFile  (  )  .  getName  (  )  .  toLowerCase  (  Locale  .  US  )  .  endsWith  (  ".mp3"  )  )  { 
RandomAudioInputStream   i  =  new   RandomAudioInputStream  (  new   RandomAccessFile  (  source  .  getFile  (  )  ,  "rw"  )  )  ; 
i  .  skip  (  skip  )  ; 
try  { 
stream  =  AudioSystem  .  getAudioInputStream  (  i  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
throw   e  ; 
} 
}  else  { 
stream  =  AudioSystem  .  getAudioInputStream  (  source  .  getFile  (  )  )  ; 
stream  .  skip  (  skip  )  ; 
} 
}  else   if  (  source  .  getStream  (  )  !=  null  )  { 
stream  =  AudioSystem  .  getAudioInputStream  (  source  .  getStream  (  )  )  ; 
}  else   if  (  source  .  getURL  (  )  !=  null  )  { 
stream  =  AudioSystem  .  getAudioInputStream  (  source  .  getURL  (  )  .  openStream  (  )  )  ; 
}  else   throw   new   IllegalArgumentException  (  "Attempting to open invalid audio source"  )  ; 
return   stream  ; 
} 



















public   static   AudioInputStream   createDecodedAudioInputStream  (  AudioInputStream   audioInputStream  )  { 
AudioFormat   sourceFormat  =  audioInputStream  .  getFormat  (  )  ; 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  sourceFormat  ,  LimeWirePlayer  .  EXTERNAL_BUFFER_SIZE  )  ; 
if  (  AudioSystem  .  isLineSupported  (  info  )  )  { 
return   audioInputStream  ; 
}  else  { 
int   nSampleSizeInBits  =  sourceFormat  .  getSampleSizeInBits  (  )  ; 
if  (  nSampleSizeInBits  <=  0  )  nSampleSizeInBits  =  16  ; 
if  (  (  sourceFormat  .  getEncoding  (  )  ==  AudioFormat  .  Encoding  .  ULAW  )  ||  (  sourceFormat  .  getEncoding  (  )  ==  AudioFormat  .  Encoding  .  ALAW  )  )  nSampleSizeInBits  =  16  ; 
if  (  nSampleSizeInBits  !=  8  )  nSampleSizeInBits  =  16  ; 
AudioFormat   targetFormat  =  new   AudioFormat  (  AudioFormat  .  Encoding  .  PCM_SIGNED  ,  sourceFormat  .  getSampleRate  (  )  ,  nSampleSizeInBits  ,  sourceFormat  .  getChannels  (  )  ,  sourceFormat  .  getChannels  (  )  *  (  nSampleSizeInBits  /  8  )  ,  sourceFormat  .  getSampleRate  (  )  ,  false  )  ; 
info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  targetFormat  ,  LimeWirePlayer  .  EXTERNAL_BUFFER_SIZE  )  ; 
return   AudioSystem  .  getAudioInputStream  (  targetFormat  ,  audioInputStream  )  ; 
} 
} 















private   SourceDataLine   createSourceDataLine  (  AudioInputStream   audioInputStream  )  throws   LineUnavailableException  { 
return   createSourceDataLine  (  audioInputStream  ,  -  1  )  ; 
} 















private   SourceDataLine   createSourceDataLine  (  AudioInputStream   audioInputStream  ,  int   bufferSize  )  throws   LineUnavailableException  { 
if  (  audioInputStream  ==  null  )  throw   new   NullPointerException  (  "input stream is null"  )  ; 
AudioFormat   audioFormat  =  audioInputStream  .  getFormat  (  )  ; 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  audioFormat  ,  AudioSystem  .  NOT_SPECIFIED  )  ; 
SourceDataLine   line  =  (  SourceDataLine  )  AudioSystem  .  getLine  (  info  )  ; 
if  (  bufferSize  <=  0  )  bufferSize  =  line  .  getBufferSize  (  )  ; 
line  .  open  (  audioFormat  ,  bufferSize  )  ; 
if  (  line  .  isControlSupported  (  FloatControl  .  Type  .  MASTER_GAIN  )  )  { 
gainControl  =  (  FloatControl  )  line  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
} 
return   line  ; 
} 



















private   static   Map  <  String  ,  Object  >  createProperties  (  AudioSource   source  )  throws   UnsupportedAudioFileException  ,  IOException  { 
AudioFileFormat   audioFileFormat  ; 
Map  <  String  ,  Object  >  properties  =  new   HashMap  <  String  ,  Object  >  (  )  ; 
if  (  source  .  getFile  (  )  !=  null  )  { 
audioFileFormat  =  AudioSystem  .  getAudioFileFormat  (  source  .  getFile  (  )  )  ; 
}  else   if  (  source  .  getStream  (  )  !=  null  )  { 
audioFileFormat  =  AudioSystem  .  getAudioFileFormat  (  source  .  getStream  (  )  )  ; 
}  else   return   properties  ; 
if  (  audioFileFormat   instanceof   TAudioFileFormat  )  { 
properties  =  GenericsUtils  .  scanForMap  (  (  (  TAudioFileFormat  )  audioFileFormat  )  .  properties  (  )  ,  String  .  class  ,  Object  .  class  ,  ScanMode  .  REMOVE  )  ; 
Map  <  String  ,  Object  >  newMap  =  new   HashMap  <  String  ,  Object  >  (  properties  )  ; 
properties  =  newMap  ; 
} 
if  (  audioFileFormat  .  getByteLength  (  )  >  0  )  properties  .  put  (  AUDIO_LENGTH_BYTES  ,  audioFileFormat  .  getByteLength  (  )  )  ; 
if  (  audioFileFormat  .  getFrameLength  (  )  >  0  )  properties  .  put  (  AUDIO_LENGTH_FRAMES  ,  audioFileFormat  .  getFrameLength  (  )  )  ; 
if  (  audioFileFormat  .  getType  (  )  !=  null  )  properties  .  put  (  AUDIO_TYPE  ,  (  audioFileFormat  .  getType  (  )  .  toString  (  )  )  )  ; 
AudioFormat   audioFormat  =  audioFileFormat  .  getFormat  (  )  ; 
if  (  audioFormat  .  getFrameRate  (  )  >  0  )  properties  .  put  (  AUDIO_FRAMERATE_FPS  ,  audioFormat  .  getFrameRate  (  )  )  ; 
if  (  audioFormat  .  getFrameSize  (  )  >  0  )  properties  .  put  (  AUDIO_FRAMESIZE_BYTES  ,  audioFormat  .  getFrameSize  (  )  )  ; 
if  (  audioFormat  .  getSampleRate  (  )  >  0  )  properties  .  put  (  AUDIO_SAMPLERATE_HZ  ,  audioFormat  .  getSampleRate  (  )  )  ; 
if  (  audioFormat  .  getSampleSizeInBits  (  )  >  0  )  properties  .  put  (  AUDIO_SAMPLESIZE_BITS  ,  audioFormat  .  getSampleSizeInBits  (  )  )  ; 
if  (  audioFormat  .  getChannels  (  )  >  0  )  properties  .  put  (  AUDIO_CHANNELS  ,  audioFormat  .  getChannels  (  )  )  ; 
if  (  audioFormat   instanceof   TAudioFormat  )  { 
Map  <  String  ,  Object  >  addproperties  =  GenericsUtils  .  scanForMap  (  (  (  TAudioFormat  )  audioFormat  )  .  properties  (  )  ,  String  .  class  ,  Object  .  class  ,  ScanMode  .  REMOVE  )  ; 
properties  .  putAll  (  addproperties  )  ; 
} 
return   properties  ; 
} 




public   AudioSource   getSource  (  )  { 
return   audioSource  ; 
} 




public   AudioInputStream   getAudioInputStream  (  )  { 
return   audioInputStream  ; 
} 




public   SourceDataLine   getSourceDataLine  (  )  { 
return   sourceDataLine  ; 
} 





public   Map  <  String  ,  Object  >  getProperties  (  )  { 
if  (  properties  !=  null  )  return   properties  ;  else   return   new   HashMap  <  String  ,  Object  >  (  )  ; 
} 




public   long   totalLength  (  )  { 
return   totalLength  ; 
} 




public   int   available  (  )  { 
int   avail  =  -  1  ; 
if  (  encodedAudioInputStream  !=  null  )  { 
try  { 
avail  =  encodedAudioInputStream  .  available  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   avail  ; 
} 





public   void   startSourceDataLine  (  )  { 
if  (  sourceDataLine  !=  null  &&  !  sourceDataLine  .  isRunning  (  )  )  { 
if  (  !  sourceDataLine  .  isOpen  (  )  )  try  { 
sourceDataLine  .  open  (  )  ; 
}  catch  (  LineUnavailableException   e  )  { 
System  .  out  .  println  (  "LimeAudioFormat.startSoutceDataLine() EXCEPTION"  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  out  .  println  (  "===="  )  ; 
} 
sourceDataLine  .  start  (  )  ; 
} 
} 





public   void   stopSourceDataLine  (  )  { 
if  (  sourceDataLine  !=  null  &&  sourceDataLine  .  isRunning  (  )  )  { 
sourceDataLine  .  flush  (  )  ; 
sourceDataLine  .  stop  (  )  ; 
} 
} 




public   int   getEncodedStreamPosition  (  )  { 
return  (  int  )  (  totalLength  -  available  (  )  )  ; 
} 







public   long   seek  (  long   position  )  { 
return  -  1  ; 
} 





public   void   closeStreams  (  )  { 
FileUtils  .  close  (  encodedAudioInputStream  )  ; 
FileUtils  .  close  (  audioInputStream  )  ; 
if  (  sourceDataLine  !=  null  )  { 
sourceDataLine  .  stop  (  )  ; 
sourceDataLine  .  close  (  )  ; 
} 
} 




public   float   getGainValue  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   gainControl  .  getValue  (  )  ; 
}  else  { 
return   0.0F  ; 
} 
} 




public   float   getMaximumGain  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   gainControl  .  getMaximum  (  )  ; 
}  else  { 
return   0.0F  ; 
} 
} 




public   float   getMinimumGain  (  )  { 
if  (  hasGainControl  (  )  )  { 
return   gainControl  .  getMinimum  (  )  ; 
}  else  { 
return   0.0F  ; 
} 
} 




public   boolean   hasGainControl  (  )  { 
if  (  gainControl  ==  null  )  { 
if  (  (  sourceDataLine  !=  null  )  &&  (  sourceDataLine  .  isControlSupported  (  FloatControl  .  Type  .  MASTER_GAIN  )  )  )  gainControl  =  (  FloatControl  )  sourceDataLine  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
} 
return   gainControl  !=  null  ; 
} 








public   void   setGain  (  double   fGain  )  throws   IOException  { 
if  (  hasGainControl  (  )  )  { 
double   ampGainDB  =  (  (  10.0f  /  20.0f  )  *  getMaximumGain  (  )  )  -  getMinimumGain  (  )  ; 
double   cste  =  Math  .  log  (  10.0  )  /  20  ; 
double   valueDB  =  getMinimumGain  (  )  +  (  1  /  cste  )  *  Math  .  log  (  1  +  (  Math  .  exp  (  cste  *  ampGainDB  )  -  1  )  *  fGain  )  ; 
gainControl  .  setValue  (  (  float  )  valueDB  )  ; 
}  else   throw   new   IOException  (  "Volume error"  )  ; 
} 
} 

