package   quizgame  .  common  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  Clip  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  FloatControl  ; 
import   javax  .  sound  .  sampled  .  LineUnavailableException  ; 
import   javax  .  sound  .  sampled  .  UnsupportedAudioFileException  ; 





public   class   SoundPlayer  { 

private   Map  <  String  ,  Clip  >  soundClipMap  ; 

private   boolean   soundEnabled  =  true  ; 

private   static   SoundPlayer   theInstance  ; 

private   int   masterGain  =  100  ; 


private   SoundPlayer  (  )  { 
soundClipMap  =  Collections  .  synchronizedMap  (  new   HashMap  <  String  ,  Clip  >  (  )  )  ; 
} 

public   static   SoundPlayer   getInstance  (  )  { 
if  (  theInstance  ==  null  )  { 
theInstance  =  new   SoundPlayer  (  )  ; 
} 
return   theInstance  ; 
} 





public   void   loadSound  (  String   filename  ,  String   sound  )  { 
synchronized  (  soundClipMap  )  { 
AudioInputStream   ais  =  null  ; 
try  { 
ais  =  AudioSystem  .  getAudioInputStream  (  new   File  (  filename  )  )  ; 
}  catch  (  UnsupportedAudioFileException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
} 
AudioFormat   audioFormat  =  ais  .  getFormat  (  )  ; 
if  (  audioFormat  .  getEncoding  (  )  !=  AudioFormat  .  Encoding  .  PCM_SIGNED  )  { 
AudioFormat   targetFormat  =  new   AudioFormat  (  AudioFormat  .  Encoding  .  PCM_SIGNED  ,  audioFormat  .  getSampleRate  (  )  ,  16  ,  audioFormat  .  getChannels  (  )  ,  audioFormat  .  getChannels  (  )  *  2  ,  audioFormat  .  getSampleRate  (  )  ,  false  )  ; 
ais  =  AudioSystem  .  getAudioInputStream  (  targetFormat  ,  ais  )  ; 
audioFormat  =  ais  .  getFormat  (  )  ; 
} 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  Clip  .  class  ,  audioFormat  )  ; 
Clip   clip  =  null  ; 
try  { 
clip  =  (  Clip  )  AudioSystem  .  getLine  (  info  )  ; 
}  catch  (  LineUnavailableException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
} 
try  { 
clip  .  open  (  ais  )  ; 
}  catch  (  LineUnavailableException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
} 
soundClipMap  .  put  (  sound  ,  clip  )  ; 
FloatControl   c  =  (  FloatControl  )  clip  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
c  .  setValue  (  calcGain  (  c  ,  masterGain  )  )  ; 
} 
} 






public   boolean   isLoaded  (  String   sound  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 





public   void   play  (  String   sound  )  { 
if  (  soundEnabled  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
clip  .  flush  (  )  ; 
clip  .  setFramePosition  (  0  )  ; 
clip  .  start  (  )  ; 
}  else  { 
System  .  out  .  println  (  "Tried to play() a sound that wasn't loaded."  )  ; 
} 
} 
} 





public   void   stop  (  String   sound  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
clip  .  setFramePosition  (  0  )  ; 
}  else  { 
System  .  out  .  println  (  "Tried to stop() a sound that wasn't loaded."  )  ; 
} 
} 






public   void   loop  (  String   sound  ,  int   nLoops  )  { 
if  (  soundEnabled  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
if  (  nLoops  <  0  )  { 
clip  .  loop  (  clip  .  LOOP_CONTINUOUSLY  )  ; 
}  else  { 
clip  .  loop  (  nLoops  )  ; 
} 
} 
} 
} 





public   void   pause  (  String   sound  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
} 
} 




public   void   stopAll  (  )  { 
for  (  Clip   clip  :  soundClipMap  .  values  (  )  )  { 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
clip  .  setFramePosition  (  0  )  ; 
} 
} 




public   void   pauseAll  (  )  { 
for  (  Clip   clip  :  soundClipMap  .  values  (  )  )  { 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
} 
} 





public   void   unloadSound  (  String   sound  )  { 
synchronized  (  soundClipMap  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
clip  .  close  (  )  ; 
soundClipMap  .  remove  (  sound  )  ; 
} 
} 
} 




public   void   unloadAllsounds  (  )  { 
synchronized  (  soundClipMap  )  { 
for  (  Clip   clip  :  soundClipMap  .  values  (  )  )  { 
clip  .  stop  (  )  ; 
clip  .  flush  (  )  ; 
clip  .  close  (  )  ; 
} 
soundClipMap  .  clear  (  )  ; 
} 
} 





public   void   setSoundEnabled  (  boolean   value  )  { 
soundEnabled  =  value  ; 
if  (  !  soundEnabled  )  { 
stopAll  (  )  ; 
} 
} 





public   boolean   isSoundEnabled  (  )  { 
return   soundEnabled  ; 
} 






public   void   setGain  (  String   sound  ,  int   value  )  { 
if  (  soundClipMap  .  containsKey  (  sound  )  )  { 
Clip   clip  =  soundClipMap  .  get  (  sound  )  ; 
FloatControl   c  =  (  FloatControl  )  clip  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
c  .  setValue  (  calcGain  (  c  ,  value  )  )  ; 
} 
} 




public   void   setMasterGain  (  int   value  )  { 
synchronized  (  soundClipMap  )  { 
for  (  Clip   clip  :  soundClipMap  .  values  (  )  )  { 
FloatControl   c  =  (  FloatControl  )  clip  .  getControl  (  FloatControl  .  Type  .  MASTER_GAIN  )  ; 
c  .  setValue  (  calcGain  (  c  ,  value  )  )  ; 
} 
masterGain  =  value  ; 
} 
} 

private   float   calcGain  (  FloatControl   c  ,  int   value  )  { 
return  (  float  )  c  .  getMinimum  (  )  +  50.0f  +  (  (  c  .  getMaximum  (  )  -  c  .  getMinimum  (  )  -  50.0f  )  *  (  (  float  )  value  /  100.0f  )  )  ; 
} 
} 

