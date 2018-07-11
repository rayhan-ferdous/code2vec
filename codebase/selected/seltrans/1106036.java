package   net  .  sourceforge  .  freecol  .  gui  .  sound  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   javax  .  sound  .  midi  .  InvalidMidiDataException  ; 
import   javax  .  sound  .  midi  .  MetaEventListener  ; 
import   javax  .  sound  .  midi  .  MetaMessage  ; 
import   javax  .  sound  .  midi  .  MidiChannel  ; 
import   javax  .  sound  .  midi  .  MidiSystem  ; 
import   javax  .  sound  .  midi  .  Sequence  ; 
import   javax  .  sound  .  midi  .  Sequencer  ; 
import   javax  .  sound  .  midi  .  Synthesizer  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  sound  .  sampled  .  AudioSystem  ; 
import   javax  .  sound  .  sampled  .  Clip  ; 
import   javax  .  sound  .  sampled  .  DataLine  ; 
import   javax  .  sound  .  sampled  .  LineEvent  ; 
import   javax  .  sound  .  sampled  .  LineListener  ; 




public   class   SoundPlayer  { 

public   static   final   String   COPYRIGHT  =  "Copyright (C) 2003 The FreeCol Team"  ; 

public   static   final   String   LICENSE  =  "http://www.gnu.org/licenses/gpl.html"  ; 

public   static   final   String   REVISION  =  "$Revision: 599 $"  ; 


private   ThreadGroup   soundPlayerThreads  =  new   ThreadGroup  (  "soundPlayerThreads"  )  ; 


private   boolean   soundPaused  =  false  ; 


private   boolean   soundStopped  =  true  ; 


private   boolean   isMIDIEnabled  =  false  ; 






private   boolean   multipleSounds  ; 


private   boolean   defaultPlayContinues  ; 






private   final   int   defaultRepeatMode  ; 






private   final   int   defaultPickMode  ; 














public   SoundPlayer  (  boolean   multipleSounds  ,  boolean   isMIDIEnabled  ,  boolean   defaultPlayContinues  )  { 
this  (  multipleSounds  ,  isMIDIEnabled  ,  defaultPlayContinues  ,  Playlist  .  REPEAT_ALL  ,  Playlist  .  FORWARDS  )  ; 
} 














public   SoundPlayer  (  boolean   multipleSounds  ,  boolean   isMIDIEnabled  ,  boolean   defaultPlayContinues  ,  int   defaultRepeatMode  ,  int   defaultPickMode  )  { 
this  .  multipleSounds  =  multipleSounds  ; 
this  .  isMIDIEnabled  =  isMIDIEnabled  ; 
this  .  defaultPlayContinues  =  defaultPlayContinues  ; 
this  .  defaultRepeatMode  =  defaultRepeatMode  ; 
this  .  defaultPickMode  =  defaultPickMode  ; 
} 




public   void   play  (  Playlist   playlist  )  { 
play  (  playlist  ,  defaultPlayContinues  ,  defaultRepeatMode  ,  defaultPickMode  )  ; 
} 




public   void   play  (  Playlist   playlist  ,  boolean   playContinues  ,  int   repeatMode  ,  int   pickMode  )  { 
if  (  playlist  !=  null  )  { 
SoundPlayerThread   soundPlayerThread  =  new   SoundPlayerThread  (  playlist  ,  playContinues  ,  repeatMode  ,  pickMode  )  ; 
soundPlayerThread  .  start  (  )  ; 
} 
} 




public   void   stop  (  )  { 
soundStopped  =  true  ; 
soundPaused  =  false  ; 
} 




public   boolean   isStopped  (  )  { 
return   soundStopped  ; 
} 




public   void   pause  (  )  { 
soundPaused  =  true  ; 
} 




public   boolean   isPaused  (  )  { 
return   soundPaused  ; 
} 


class   SoundPlayerThread   extends   Thread   implements   LineListener  ,  MetaEventListener  { 


private   Playlist   playlist  ; 


private   boolean   playContinues  ; 






private   int   repeatMode  ; 






private   int   pickMode  ; 


private   boolean   repeatSound  ; 


private   Object   currentSound  ; 


private   boolean   midiEOM  ; 


private   boolean   audioEOM  ; 


private   Sequencer   sequencer  ; 

private   Synthesizer   synthesizer  ; 


private   MidiChannel   channels  [  ]  ; 









public   SoundPlayerThread  (  Playlist   playlist  ,  boolean   playContinues  ,  int   repeatMode  ,  int   pickMode  )  { 
super  (  soundPlayerThreads  ,  "soundPlayerThread"  )  ; 
this  .  playlist  =  playlist  ; 
this  .  playContinues  =  playContinues  ; 
this  .  repeatMode  =  repeatMode  ; 
this  .  pickMode  =  pickMode  ; 
if  (  isMIDIEnabled  )  enableMIDI  (  )  ; 
} 




public   void   run  (  )  { 
playlist  .  setRepeatMode  (  repeatMode  )  ; 
playlist  .  setPickMode  (  pickMode  )  ; 
soundPaused  =  false  ; 
soundStopped  =  false  ; 
do  { 
if  (  loadSound  (  playlist  .  next  (  )  )  )  playSound  (  )  ; 
try  { 
this  .  sleep  (  222  )  ; 
}  catch  (  Exception   e  )  { 
break  ; 
} 
}  while  (  playContinues  &&  playlist  .  hasNext  (  )  &&  !  soundStopped  )  ; 
} 





private   boolean   loadSound  (  File   file  )  { 
try  { 
currentSound  =  AudioSystem  .  getAudioInputStream  (  file  )  ; 
}  catch  (  Exception   e1  )  { 
try  { 
FileInputStream   fileInputStream  =  new   FileInputStream  (  file  )  ; 
currentSound  =  new   BufferedInputStream  (  fileInputStream  ,  1024  )  ; 
}  catch  (  Exception   e3  )  { 
currentSound  =  null  ; 
return   false  ; 
} 
} 
if  (  currentSound   instanceof   AudioInputStream  )  { 
try  { 
AudioInputStream   stream  =  (  AudioInputStream  )  currentSound  ; 
AudioFormat   format  =  stream  .  getFormat  (  )  ; 
if  (  (  format  .  getEncoding  (  )  ==  AudioFormat  .  Encoding  .  ULAW  )  ||  (  format  .  getEncoding  (  )  ==  AudioFormat  .  Encoding  .  ALAW  )  )  { 
AudioFormat   tmp  =  new   AudioFormat  (  AudioFormat  .  Encoding  .  PCM_SIGNED  ,  format  .  getSampleRate  (  )  ,  format  .  getSampleSizeInBits  (  )  *  2  ,  format  .  getChannels  (  )  ,  format  .  getFrameSize  (  )  *  2  ,  format  .  getFrameRate  (  )  ,  true  )  ; 
stream  =  AudioSystem  .  getAudioInputStream  (  tmp  ,  stream  )  ; 
format  =  tmp  ; 
} 
DataLine  .  Info   info  =  new   DataLine  .  Info  (  Clip  .  class  ,  stream  .  getFormat  (  )  ,  (  (  int  )  stream  .  getFrameLength  (  )  *  format  .  getFrameSize  (  )  )  )  ; 
Clip   clip  =  (  Clip  )  AudioSystem  .  getLine  (  info  )  ; 
clip  .  addLineListener  (  this  )  ; 
clip  .  open  (  stream  )  ; 
currentSound  =  clip  ; 
}  catch  (  Exception   ex  )  { 
currentSound  =  null  ; 
return   false  ; 
} 
}  else   if  (  currentSound   instanceof   Sequence  ||  currentSound   instanceof   BufferedInputStream  )  { 
if  (  isMIDIEnabled  )  { 
try  { 
sequencer  .  open  (  )  ; 
if  (  currentSound   instanceof   Sequence  )  { 
sequencer  .  setSequence  (  (  Sequence  )  currentSound  )  ; 
}  else  { 
sequencer  .  setSequence  (  (  BufferedInputStream  )  currentSound  )  ; 
} 
}  catch  (  InvalidMidiDataException   imde  )  { 
currentSound  =  null  ; 
return   false  ; 
}  catch  (  Exception   ex  )  { 
currentSound  =  null  ; 
return   false  ; 
} 
}  else  { 
currentSound  =  null  ; 
return   false  ; 
} 
} 
return   true  ; 
} 





private   void   playSound  (  )  { 
midiEOM  =  audioEOM  =  false  ; 
if  (  currentSound   instanceof   Sequence  ||  currentSound   instanceof   BufferedInputStream  &&  !  soundStopped  )  { 
sequencer  .  start  (  )  ; 
while  (  !  midiEOM  &&  !  soundStopped  )  { 
try  { 
this  .  sleep  (  99  )  ; 
}  catch  (  Exception   e  )  { 
break  ; 
} 
} 
sequencer  .  stop  (  )  ; 
sequencer  .  close  (  )  ; 
}  else   if  (  currentSound   instanceof   Clip  &&  !  soundStopped  )  { 
Clip   clip  =  (  Clip  )  currentSound  ; 
clip  .  start  (  )  ; 
try  { 
this  .  sleep  (  99  )  ; 
}  catch  (  Exception   e  )  { 
} 
while  (  (  soundPaused  ||  clip  .  isActive  (  )  )  &&  !  soundStopped  )  { 
try  { 
this  .  sleep  (  1000  )  ; 
}  catch  (  Exception   e  )  { 
break  ; 
} 
} 
clip  .  stop  (  )  ; 
clip  .  close  (  )  ; 
} 
currentSound  =  null  ; 
} 


public   void   enableMIDI  (  )  { 
try  { 
sequencer  =  MidiSystem  .  getSequencer  (  )  ; 
if  (  sequencer   instanceof   Synthesizer  )  { 
synthesizer  =  (  Synthesizer  )  sequencer  ; 
channels  =  synthesizer  .  getChannels  (  )  ; 
} 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
return  ; 
} 
sequencer  .  addMetaEventListener  (  this  )  ; 
isMIDIEnabled  =  true  ; 
} 


public   void   disableMIDI  (  )  { 
if  (  sequencer  !=  null  )  { 
sequencer  .  close  (  )  ; 
isMIDIEnabled  =  false  ; 
} 
} 


public   void   meta  (  MetaMessage   message  )  { 
if  (  message  .  getType  (  )  ==  47  )  { 
midiEOM  =  true  ; 
} 
} 


public   void   update  (  LineEvent   event  )  { 
if  (  event  .  getType  (  )  ==  LineEvent  .  Type  .  STOP  &&  !  soundPaused  )  { 
audioEOM  =  true  ; 
} 
} 
} 
} 

