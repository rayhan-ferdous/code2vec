package   jpcsp  .  GUI  ; 

import   java  .  awt  .  Image  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  ReadableByteChannel  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  LineUnavailableException  ; 
import   javax  .  sound  .  sampled  .  SourceDataLine  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   javax  .  swing  .  JLabel  ; 
import   jpcsp  .  Emulator  ; 
import   jpcsp  .  filesystems  .  umdiso  .  UmdIsoFile  ; 
import   jpcsp  .  filesystems  .  umdiso  .  UmdIsoReader  ; 
import   jpcsp  .  media  .  MediaEngine  ; 
import   jpcsp  .  settings  .  Settings  ; 
import   com  .  xuggle  .  xuggler  .  Global  ; 
import   com  .  xuggle  .  xuggler  .  IAudioSamples  ; 
import   com  .  xuggle  .  xuggler  .  ICodec  ; 
import   com  .  xuggle  .  xuggler  .  IContainer  ; 
import   com  .  xuggle  .  xuggler  .  IPacket  ; 
import   com  .  xuggle  .  xuggler  .  IPixelFormat  ; 
import   com  .  xuggle  .  xuggler  .  IStream  ; 
import   com  .  xuggle  .  xuggler  .  IStreamCoder  ; 
import   com  .  xuggle  .  xuggler  .  IVideoPicture  ; 
import   com  .  xuggle  .  xuggler  .  IVideoResampler  ; 
import   com  .  xuggle  .  xuggler  .  video  .  ConverterFactory  ; 
import   com  .  xuggle  .  xuggler  .  video  .  IConverter  ; 

public   class   UmdBrowserPmf  { 

private   UmdIsoReader   iso  ; 

private   UmdIsoFile   isoFile  ; 

private   String   fileName  ; 

private   IContainer   container  ; 

private   IVideoResampler   resampler  ; 

private   int   videoStreamId  ; 

private   IStreamCoder   videoCoder  ; 

private   int   audioStreamId  ; 

private   IStreamCoder   audioCoder  ; 

private   IPacket   packet  ; 

private   long   firstTimestampInStream  ; 

private   long   systemClockStartTime  ; 

private   IConverter   converter  ; 

private   BufferedImage   image  ; 

private   boolean   done  ; 

private   boolean   endOfVideo  ; 

private   boolean   threadExit  ; 

private   JLabel   display  ; 

private   PmfDisplayThread   displayThread  ; 

private   PmfByteChannel   byteChannel  ; 

private   SourceDataLine   mLine  ; 

public   UmdBrowserPmf  (  UmdIsoReader   iso  ,  String   fileName  ,  JLabel   display  )  { 
this  .  iso  =  iso  ; 
this  .  fileName  =  fileName  ; 
this  .  display  =  display  ; 
init  (  )  ; 
initVideo  (  )  ; 
} 

private   void   init  (  )  { 
image  =  null  ; 
done  =  false  ; 
threadExit  =  false  ; 
MediaEngine  .  initXuggler  (  )  ; 
isoFile  =  null  ; 
try  { 
isoFile  =  iso  .  getFile  (  fileName  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
}  catch  (  IOException   e  )  { 
Emulator  .  log  .  error  (  e  )  ; 
} 
} 

private   Image   getImage  (  )  { 
return   image  ; 
} 

public   boolean   initVideo  (  )  { 
if  (  isoFile  ==  null  )  { 
return   false  ; 
} 
if  (  !  startVideo  (  )  )  { 
return   false  ; 
} 
displayThread  =  new   PmfDisplayThread  (  )  ; 
displayThread  .  setDaemon  (  true  )  ; 
displayThread  .  setName  (  "UMD Browser - PMF Display Thread"  )  ; 
displayThread  .  start  (  )  ; 
return   true  ; 
} 

private   boolean   startVideo  (  )  { 
endOfVideo  =  false  ; 
try  { 
container  =  IContainer  .  make  (  )  ; 
}  catch  (  Throwable   e  )  { 
Emulator  .  log  .  error  (  e  )  ; 
return   false  ; 
} 
try  { 
isoFile  .  seek  (  0  )  ; 
}  catch  (  IOException   e  )  { 
Emulator  .  log  .  error  (  e  )  ; 
return   false  ; 
} 
byteChannel  =  new   PmfByteChannel  (  isoFile  )  ; 
if  (  container  .  open  (  byteChannel  ,  null  )  <  0  )  { 
Emulator  .  log  .  error  (  "could not open file: "  +  fileName  )  ; 
return   false  ; 
} 
int   numStreams  =  container  .  getNumStreams  (  )  ; 
videoStreamId  =  -  1  ; 
videoCoder  =  null  ; 
audioStreamId  =  -  1  ; 
audioCoder  =  null  ; 
boolean   audioMuted  =  Settings  .  getInstance  (  )  .  readBool  (  "emu.mutesound"  )  ; 
for  (  int   i  =  0  ;  i  <  numStreams  ;  i  ++  )  { 
IStream   stream  =  container  .  getStream  (  i  )  ; 
IStreamCoder   coder  =  stream  .  getStreamCoder  (  )  ; 
if  (  coder  .  getCodecType  (  )  ==  ICodec  .  Type  .  CODEC_TYPE_VIDEO  )  { 
videoStreamId  =  i  ; 
videoCoder  =  coder  ; 
}  else   if  (  coder  .  getCodecType  (  )  ==  ICodec  .  Type  .  CODEC_TYPE_AUDIO  &&  !  audioMuted  )  { 
audioStreamId  =  i  ; 
audioCoder  =  coder  ; 
} 
} 
if  (  videoCoder  !=  null  &&  videoCoder  .  open  (  null  ,  null  )  <  0  )  { 
Emulator  .  log  .  error  (  "could not open video decoder for container: "  +  fileName  )  ; 
return   false  ; 
} 
if  (  audioCoder  !=  null  &&  audioCoder  .  open  (  null  ,  null  )  <  0  )  { 
Emulator  .  log  .  info  (  "AT3+ audio format is not yet supported by Jpcsp (file="  +  fileName  +  ")"  )  ; 
return   false  ; 
} 
resampler  =  null  ; 
if  (  videoCoder  !=  null  )  { 
converter  =  ConverterFactory  .  createConverter  (  ConverterFactory  .  XUGGLER_BGR_24  ,  IPixelFormat  .  Type  .  BGR24  ,  videoCoder  .  getWidth  (  )  ,  videoCoder  .  getHeight  (  )  )  ; 
if  (  videoCoder  .  getPixelType  (  )  !=  IPixelFormat  .  Type  .  BGR24  )  { 
resampler  =  IVideoResampler  .  make  (  videoCoder  .  getWidth  (  )  ,  videoCoder  .  getHeight  (  )  ,  IPixelFormat  .  Type  .  BGR24  ,  videoCoder  .  getWidth  (  )  ,  videoCoder  .  getHeight  (  )  ,  videoCoder  .  getPixelType  (  )  )  ; 
if  (  resampler  ==  null  )  { 
Emulator  .  log  .  error  (  "could not create color space resampler for: "  +  fileName  )  ; 
return   false  ; 
} 
} 
} 
if  (  audioCoder  !=  null  )  { 
openAudio  (  audioCoder  )  ; 
} 
packet  =  IPacket  .  make  (  )  ; 
firstTimestampInStream  =  Global  .  NO_PTS  ; 
systemClockStartTime  =  0  ; 
return   true  ; 
} 

private   void   closeVideo  (  )  { 
if  (  container  !=  null  )  { 
container  .  close  (  )  ; 
container  =  null  ; 
} 
if  (  videoCoder  !=  null  )  { 
videoCoder  .  close  (  )  ; 
videoCoder  =  null  ; 
} 
if  (  resampler  !=  null  )  { 
resampler  .  delete  (  )  ; 
resampler  =  null  ; 
} 
if  (  converter  !=  null  )  { 
converter  .  delete  (  )  ; 
converter  =  null  ; 
} 
if  (  packet  !=  null  )  { 
packet  .  delete  (  )  ; 
packet  =  null  ; 
} 
} 

private   void   loopVideo  (  )  { 
closeVideo  (  )  ; 
closeAudio  (  )  ; 
startVideo  (  )  ; 
} 

private   void   stopDisplayThread  (  )  { 
while  (  displayThread  !=  null  &&  !  threadExit  )  { 
done  =  true  ; 
sleep  (  1  )  ; 
} 
displayThread  =  null  ; 
} 

public   void   stopVideo  (  )  { 
stopDisplayThread  (  )  ; 
closeVideo  (  )  ; 
closeAudio  (  )  ; 
if  (  isoFile  !=  null  )  { 
try  { 
isoFile  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 

public   void   stepVideo  (  )  { 
if  (  container  .  readNextPacket  (  packet  )  >=  0  )  { 
if  (  packet  .  getStreamIndex  (  )  ==  videoStreamId  &&  videoCoder  !=  null  )  { 
IVideoPicture   picture  =  IVideoPicture  .  make  (  videoCoder  .  getPixelType  (  )  ,  videoCoder  .  getWidth  (  )  ,  videoCoder  .  getHeight  (  )  )  ; 
int   offset  =  0  ; 
while  (  offset  <  packet  .  getSize  (  )  )  { 
int   bytesDecoded  =  videoCoder  .  decodeVideo  (  picture  ,  packet  ,  offset  )  ; 
if  (  bytesDecoded  <  0  )  { 
throw   new   RuntimeException  (  "got error decoding video in: "  +  fileName  )  ; 
} 
offset  +=  bytesDecoded  ; 
if  (  picture  .  isComplete  (  )  )  { 
IVideoPicture   newPic  =  picture  ; 
if  (  resampler  !=  null  )  { 
newPic  =  IVideoPicture  .  make  (  resampler  .  getOutputPixelFormat  (  )  ,  picture  .  getWidth  (  )  ,  picture  .  getHeight  (  )  )  ; 
if  (  resampler  .  resample  (  newPic  ,  picture  )  <  0  )  { 
throw   new   RuntimeException  (  "could not resample video from: "  +  fileName  )  ; 
} 
} 
if  (  newPic  .  getPixelType  (  )  !=  IPixelFormat  .  Type  .  BGR24  )  { 
throw   new   RuntimeException  (  "could not decode video BGR 24 bit data in: "  +  fileName  )  ; 
} 
if  (  firstTimestampInStream  ==  Global  .  NO_PTS  )  { 
firstTimestampInStream  =  picture  .  getTimeStamp  (  )  ; 
systemClockStartTime  =  System  .  currentTimeMillis  (  )  ; 
}  else  { 
long   systemClockCurrentTime  =  System  .  currentTimeMillis  (  )  ; 
long   millisecondsClockTimeSinceStartofVideo  =  systemClockCurrentTime  -  systemClockStartTime  ; 
long   millisecondsStreamTimeSinceStartOfVideo  =  (  picture  .  getTimeStamp  (  )  -  firstTimestampInStream  )  /  1000  ; 
final   long   millisecondsTolerance  =  50  ; 
final   long   millisecondsToSleep  =  (  millisecondsStreamTimeSinceStartOfVideo  -  (  millisecondsClockTimeSinceStartofVideo  +  millisecondsTolerance  )  )  ; 
sleep  (  millisecondsToSleep  )  ; 
} 
image  =  converter  .  toImage  (  newPic  )  ; 
} 
} 
}  else   if  (  packet  .  getStreamIndex  (  )  ==  audioStreamId  &&  audioCoder  !=  null  )  { 
IAudioSamples   samples  =  IAudioSamples  .  make  (  1024  ,  audioCoder  .  getChannels  (  )  )  ; 
int   offset  =  0  ; 
while  (  offset  <  packet  .  getSize  (  )  )  { 
int   bytesDecoded  =  audioCoder  .  decodeAudio  (  samples  ,  packet  ,  offset  )  ; 
if  (  bytesDecoded  <  0  )  { 
throw   new   RuntimeException  (  "got error decoding audio in: "  +  fileName  )  ; 
} 
offset  +=  bytesDecoded  ; 
if  (  samples  .  isComplete  (  )  )  { 
playAudio  (  samples  )  ; 
} 
} 
} 
}  else  { 
endOfVideo  =  true  ; 
} 
} 

private   void   openAudio  (  IStreamCoder   aAudioCoder  )  { 
AudioFormat   audioFormat  =  new   AudioFormat  (  aAudioCoder  .  getSampleRate  (  )  ,  (  int  )  IAudioSamples  .  findSampleBitDepth  (  aAudioCoder  .  getSampleFormat  (  )  )  ,  aAudioCoder  .  getChannels  (  )  ,  true  ,  false  )  ; 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  audioFormat  )  ; 
try  { 
mLine  =  (  SourceDataLine  )  AudioSystem  .  getLine  (  info  )  ; 
mLine  .  open  (  audioFormat  )  ; 
mLine  .  start  (  )  ; 
}  catch  (  LineUnavailableException   e  )  { 
throw   new   RuntimeException  (  "could not open audio line"  )  ; 
} 
} 

private   void   playAudio  (  IAudioSamples   aSamples  )  { 
byte  [  ]  rawBytes  =  aSamples  .  getData  (  )  .  getByteArray  (  0  ,  aSamples  .  getSize  (  )  )  ; 
mLine  .  write  (  rawBytes  ,  0  ,  aSamples  .  getSize  (  )  )  ; 
} 

private   void   closeAudio  (  )  { 
if  (  mLine  !=  null  )  { 
mLine  .  drain  (  )  ; 
mLine  .  close  (  )  ; 
mLine  =  null  ; 
} 
} 

private   void   sleep  (  long   millis  )  { 
if  (  millis  >  0  )  { 
try  { 
Thread  .  sleep  (  millis  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 

private   class   PmfDisplayThread   extends   Thread  { 

@  Override 
public   void   run  (  )  { 
while  (  !  done  )  { 
while  (  !  endOfVideo  &&  !  done  )  { 
stepVideo  (  )  ; 
if  (  display  !=  null  &&  getImage  (  )  !=  null  )  { 
display  .  setIcon  (  new   ImageIcon  (  getImage  (  )  )  )  ; 
} 
} 
if  (  !  done  )  { 
loopVideo  (  )  ; 
} 
} 
threadExit  =  true  ; 
} 
} 

private   static   class   PmfByteChannel   implements   ReadableByteChannel  { 

private   UmdIsoFile   file  ; 

private   byte  [  ]  buffer  ; 

public   PmfByteChannel  (  UmdIsoFile   file  )  { 
this  .  file  =  file  ; 
} 

@  Override 
public   int   read  (  ByteBuffer   dst  )  throws   IOException  { 
int   available  =  dst  .  remaining  (  )  ; 
if  (  buffer  ==  null  ||  buffer  .  length  <  available  )  { 
buffer  =  new   byte  [  available  ]  ; 
} 
int   length  =  file  .  read  (  buffer  ,  0  ,  available  )  ; 
if  (  length  >  0  )  { 
dst  .  put  (  buffer  ,  0  ,  length  )  ; 
} 
return   length  ; 
} 

@  Override 
public   void   close  (  )  throws   IOException  { 
file  .  close  (  )  ; 
file  =  null  ; 
} 

@  Override 
public   boolean   isOpen  (  )  { 
return   file  !=  null  ; 
} 
} 
} 

