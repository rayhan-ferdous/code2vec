package   net  .  sf  .  freecol  .  client  .  gui  .  sound  ; 

import   java  .  beans  .  PropertyChangeEvent  ; 
import   java  .  beans  .  PropertyChangeListener  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  FloatControl  ; 
import   javax  .  sound  .  sampled  .  Mixer  ; 
import   javax  .  sound  .  sampled  .  SourceDataLine  ; 
import   net  .  sf  .  freecol  .  FreeCol  ; 
import   net  .  sf  .  freecol  .  common  .  option  .  AudioMixerOption  ; 
import   net  .  sf  .  freecol  .  common  .  option  .  AudioMixerOption  .  MixerWrapper  ; 
import   net  .  sf  .  freecol  .  common  .  option  .  PercentageOption  ; 




public   class   SoundPlayer  { 

private   static   Logger   logger  =  Logger  .  getLogger  (  SoundPlayer  .  class  .  getName  (  )  )  ; 

private   Mixer   mixer  ; 

private   int   volume  ; 

private   SoundPlayerThread   soundPlayerThread  ; 







public   SoundPlayer  (  AudioMixerOption   mixerOption  ,  PercentageOption   volumeOption  )  { 
setMixer  (  mixerOption  .  getValue  (  )  )  ; 
if  (  mixer  ==  null  )  { 
throw   new   IllegalStateException  (  "Mixer unavailable."  )  ; 
} 
mixerOption  .  addPropertyChangeListener  (  new   PropertyChangeListener  (  )  { 

public   void   propertyChange  (  PropertyChangeEvent   e  )  { 
setMixer  (  (  MixerWrapper  )  e  .  getNewValue  (  )  )  ; 
} 
}  )  ; 
setVolume  (  volumeOption  .  getValue  (  )  )  ; 
volumeOption  .  addPropertyChangeListener  (  new   PropertyChangeListener  (  )  { 

public   void   propertyChange  (  PropertyChangeEvent   e  )  { 
setVolume  (  (  Integer  )  e  .  getNewValue  (  )  )  ; 
} 
}  )  ; 
soundPlayerThread  =  new   SoundPlayerThread  (  )  ; 
soundPlayerThread  .  start  (  )  ; 
} 






public   Mixer   getMixer  (  )  { 
return   mixer  ; 
} 

private   void   setMixer  (  MixerWrapper   mw  )  { 
try  { 
mixer  =  AudioSystem  .  getMixer  (  mw  .  getMixerInfo  (  )  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Could not set mixer"  ,  e  )  ; 
mixer  =  null  ; 
} 
} 






public   int   getVolume  (  )  { 
return   volume  ; 
} 

private   void   setVolume  (  int   volume  )  { 
this  .  volume  =  volume  ; 
} 






public   void   playOnce  (  File   file  )  { 
if  (  getMixer  (  )  ==  null  )  return  ; 
soundPlayerThread  .  add  (  file  )  ; 
soundPlayerThread  .  awaken  (  )  ; 
} 




public   void   stop  (  )  { 
soundPlayerThread  .  stopPlaying  (  )  ; 
soundPlayerThread  .  awaken  (  )  ; 
} 




private   class   SoundPlayerThread   extends   Thread  { 

private   final   List  <  File  >  playList  =  new   ArrayList  <  File  >  (  )  ; 

private   boolean   playDone  =  true  ; 

public   SoundPlayerThread  (  )  { 
super  (  FreeCol  .  CLIENT_THREAD  +  "SoundPlayer"  )  ; 
} 

private   synchronized   void   awaken  (  )  { 
this  .  notify  (  )  ; 
} 

private   synchronized   void   goToSleep  (  )  throws   InterruptedException  { 
this  .  wait  (  )  ; 
} 

public   synchronized   boolean   keepPlaying  (  )  { 
return  !  playDone  ; 
} 

public   synchronized   void   startPlaying  (  )  { 
playDone  =  false  ; 
} 

public   synchronized   void   stopPlaying  (  )  { 
playDone  =  true  ; 
} 

public   synchronized   void   add  (  File   file  )  { 
playList  .  add  (  file  )  ; 
} 

public   void   run  (  )  { 
for  (  ;  ;  )  { 
if  (  playList  .  isEmpty  (  )  )  { 
try  { 
goToSleep  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
continue  ; 
} 
}  else  { 
playSound  (  playList  .  remove  (  0  )  )  ; 
} 
} 
} 

private   void   sleep  (  int   t  )  { 
try  { 
Thread  .  sleep  (  t  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 

private   void   setVolume  (  SourceDataLine   line  ,  int   vol  )  { 
try  { 
FloatControl   control  =  (  FloatControl  )  line  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
if  (  control  !=  null  )  { 
float   gain  =  (  vol  <=  0  )  ?  control  .  getMinimum  (  )  :  (  vol  >=  100  )  ?  control  .  getMaximum  (  )  :  20.0f  *  (  float  )  Math  .  log10  (  0.01f  *  vol  )  ; 
control  .  setValue  (  gain  )  ; 
logger  .  finest  (  "Using volume "  +  vol  +  "%, gain = "  +  gain  )  ; 
}  else  { 
logger  .  warning  (  "No master gain control,"  +  " unable to change the volume."  )  ; 
} 
}  catch  (  Exception   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Could not set volume"  ,  e  )  ; 
} 
} 

private   SourceDataLine   openLine  (  AudioFormat   audioFormat  )  { 
SourceDataLine   line  =  null  ; 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  SourceDataLine  .  class  ,  audioFormat  )  ; 
try  { 
line  =  (  SourceDataLine  )  mixer  .  getLine  (  info  )  ; 
line  .  open  (  audioFormat  )  ; 
line  .  start  (  )  ; 
setVolume  (  line  ,  volume  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Can not open SourceDataLine"  ,  e  )  ; 
} 
return   line  ; 
} 

private   boolean   playSound  (  File   file  )  { 
BufferedInputStream   bis  ; 
try  { 
bis  =  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ; 
bis  .  mark  (  1000  )  ; 
bis  .  skip  (  1  )  ; 
bis  .  reset  (  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
logger  .  warning  (  "Could not find audio file: "  +  file  .  getName  (  )  )  ; 
return   false  ; 
}  catch  (  IOException   e  )  { 
logger  .  warning  (  "Could not prepare stream for: "  +  file  .  getName  (  )  )  ; 
return   false  ; 
} 
AudioInputStream   in  ; 
try  { 
in  =  AudioSystem  .  getAudioInputStream  (  bis  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warning  (  "Could not get audio input stream for: "  +  file  .  getName  (  )  )  ; 
return   false  ; 
} 
boolean   ret  =  false  ; 
AudioFormat   baseFormat  =  in  .  getFormat  (  )  ; 
AudioFormat   decodedFormat  =  new   AudioFormat  (  AudioFormat  .  Encoding  .  PCM_SIGNED  ,  baseFormat  .  getSampleRate  (  )  ,  16  ,  baseFormat  .  getChannels  (  )  ,  baseFormat  .  getChannels  (  )  *  (  16  /  8  )  ,  baseFormat  .  getSampleRate  (  )  ,  baseFormat  .  isBigEndian  (  )  )  ; 
AudioInputStream   din  =  AudioSystem  .  getAudioInputStream  (  decodedFormat  ,  in  )  ; 
if  (  din  ==  null  )  { 
logger  .  warning  (  "Can not get decoded audio input stream"  )  ; 
}  else  { 
SourceDataLine   line  =  openLine  (  decodedFormat  )  ; 
if  (  line  !=  null  )  { 
try  { 
startPlaying  (  )  ; 
rawplay  (  din  ,  line  )  ; 
ret  =  true  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Error playing: "  +  file  .  getName  (  )  ,  e  )  ; 
}  finally  { 
stopPlaying  (  )  ; 
line  .  drain  (  )  ; 
line  .  stop  (  )  ; 
line  .  close  (  )  ; 
} 
} 
} 
try  { 
if  (  din  !=  null  )  din  .  close  (  )  ; 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   ret  ; 
} 

private   void   rawplay  (  AudioInputStream   din  ,  SourceDataLine   lin  )  throws   IOException  { 
byte  [  ]  data  =  new   byte  [  8192  ]  ; 
for  (  ;  ;  )  { 
if  (  !  keepPlaying  (  )  )  { 
break  ; 
} 
int   read  =  din  .  read  (  data  ,  0  ,  data  .  length  )  ; 
if  (  read  <  0  )  { 
break  ; 
}  else   if  (  read  >  0  )  { 
lin  .  write  (  data  ,  0  ,  read  )  ; 
}  else  { 
sleep  (  50  )  ; 
} 
} 
} 
} 
} 

