package   org  .  rubato  .  audio  .  midi  ; 

import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  *  ; 
import   javax  .  sound  .  midi  .  *  ; 
import   org  .  rubato  .  base  .  Repository  ; 
import   org  .  rubato  .  math  .  yoneda  .  Denotator  ; 
import   org  .  rubato  .  math  .  yoneda  .  FactorDenotator  ; 
import   org  .  rubato  .  math  .  yoneda  .  Form  ; 









public   class   MidiPlayer   implements   MetaEventListener  { 





public   MidiPlayer  (  )  { 
} 





public   static   String  [  ]  getInstruments  (  )  { 
String  [  ]  instruments  =  new   String  [  0  ]  ; 
try  { 
Synthesizer   s  =  MidiSystem  .  getSynthesizer  (  )  ; 
if  (  s  !=  null  )  { 
s  .  open  (  )  ; 
Soundbank   sb  =  s  .  getDefaultSoundbank  (  )  ; 
if  (  sb  !=  null  )  { 
Instrument  [  ]  ins  =  sb  .  getInstruments  (  )  ; 
instruments  =  new   String  [  ins  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  ins  .  length  ;  i  ++  )  { 
instruments  [  i  ]  =  ins  [  i  ]  .  getName  (  )  ; 
} 
} 
s  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
return   instruments  ; 
} 





public   void   open  (  )  throws   MidiUnavailableException  { 
sequencer  =  MidiSystem  .  getSequencer  (  )  ; 
sequencer  .  addMetaEventListener  (  this  )  ; 
sequencer  .  open  (  )  ; 
synthesizer  =  MidiSystem  .  getSynthesizer  (  )  ; 
synthesizer  .  open  (  )  ; 
latency  =  synthesizer  .  getLatency  (  )  ; 
receiver  =  synthesizer  .  getReceiver  (  )  ; 
transmitter  =  sequencer  .  getTransmitter  (  )  ; 
transmitter  .  setReceiver  (  receiver  )  ; 
for  (  Receiver   r  :  receivers  )  { 
sequencer  .  getTransmitter  (  )  .  setReceiver  (  r  )  ; 
} 
opened  =  true  ; 
} 




public   void   close  (  )  { 
if  (  receiver  !=  null  )  { 
receiver  .  close  (  )  ; 
} 
if  (  transmitter  !=  null  )  { 
transmitter  .  close  (  )  ; 
} 
if  (  synthesizer  !=  null  )  { 
synthesizer  .  close  (  )  ; 
} 
if  (  sequencer  !=  null  )  { 
sequencer  .  close  (  )  ; 
} 
for  (  Receiver   r  :  receivers  )  { 
r  .  close  (  )  ; 
} 
opened  =  false  ; 
} 




public   void   setResolution  (  int   resolution  )  { 
this  .  resolution  =  resolution  ; 
} 




public   int   getResolution  (  )  { 
return   resolution  ; 
} 




public   void   setTempo  (  int   mspq  )  { 
this  .  mspq  =  mspq  ; 
} 




public   int   getTempo  (  )  { 
return   mspq  ; 
} 







public   void   setPrograms  (  int  [  ]  voices  )  { 
System  .  out  .  println  (  voices  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  voices  .  length  ;  i  ++  )  { 
channelProgram  .  put  (  i  ,  voices  [  i  ]  )  ; 
} 
updateControlTrack  (  )  ; 
} 




public   void   setProgram  (  int   channel  ,  int   program  )  { 
channelProgram  .  put  (  channel  ,  program  )  ; 
updateControlTrack  (  )  ; 
} 

private   void   updateControlTrack  (  )  { 
if  (  controlTrack  !=  null  )  { 
sequence  .  deleteTrack  (  controlTrack  )  ; 
} 
controlTrack  =  sequence  .  createTrack  (  )  ; 
controlTrack  .  add  (  makeTempoEvent  (  mspq  ,  0  )  )  ; 
System  .  out  .  println  (  channelProgram  .  entrySet  (  )  .  size  (  )  )  ; 
System  .  out  .  println  (  channelProgram  )  ; 
for  (  Map  .  Entry  <  Integer  ,  Integer  >  entry  :  channelProgram  .  entrySet  (  )  )  { 
controlTrack  .  add  (  makeProgramChangeEvent  (  entry  .  getKey  (  )  ,  0  ,  entry  .  getValue  (  )  )  )  ; 
} 
} 




public   void   setScore  (  Denotator   score  )  { 
if  (  !  score  .  hasForm  (  scoreForm  )  )  { 
throw   new   IllegalArgumentException  (  "Argument denotator is not of form Score."  )  ; 
} 
this  .  score  =  score  ; 
newSequence  (  )  ; 
setProgram  (  0  ,  0  )  ; 
ArrayList  <  MidiChange  >  midiChanges  =  scoreToMidiNotes  (  )  ; 
try  { 
createTracksFromNotes  (  midiChanges  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   void   newSequence  (  )  { 
try  { 
sequence  =  new   Sequence  (  Sequence  .  PPQ  ,  resolution  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   void   play  (  )  { 
if  (  !  opened  )  { 
throw   new   IllegalStateException  (  "MidiPlayer is not open."  )  ; 
} 
if  (  sequence  !=  null  )  { 
try  { 
sequencer  .  setSequence  (  sequence  )  ; 
sequencer  .  start  (  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 




public   void   pause  (  )  { 
if  (  opened  )  { 
sequencer  .  stop  (  )  ; 
} 
} 




public   void   resume  (  )  { 
if  (  opened  )  { 
sequencer  .  start  (  )  ; 
} 
} 







public   void   setPlayTempoFactor  (  float   f  )  { 
if  (  sequencer  !=  null  )  { 
sequencer  .  setTempoFactor  (  f  )  ; 
} 
} 




public   long   getTickPosition  (  )  { 
return   sequencer  .  getTickPosition  (  )  ; 
} 




public   void   setTickPosition  (  long   tick  )  { 
sequencer  .  setTickPosition  (  tick  )  ; 
} 





public   double   getPosition  (  )  { 
return   getTickPosition  (  )  /  (  double  )  resolution  ; 
} 





public   void   setPosition  (  double   pos  )  { 
setTickPosition  (  (  int  )  pos  *  resolution  )  ; 
} 

public   double   getLatency  (  )  { 
return   latency  ; 
} 

public   void   addReceiver  (  Receiver   r  )  { 
receivers  .  add  (  r  )  ; 
} 




public   boolean   isOpen  (  )  { 
return   sequencer  !=  null  &&  sequencer  .  isOpen  (  )  ; 
} 




public   boolean   isRunning  (  )  { 
return   sequencer  !=  null  &&  sequencer  .  isRunning  (  )  ; 
} 

public   void   setStopListener  (  ActionListener   stopListener  )  { 
this  .  stopListener  =  stopListener  ; 
} 




public   void   writeSequence  (  OutputStream   out  )  throws   IOException  { 
if  (  sequence  !=  null  )  { 
MidiSystem  .  write  (  sequence  ,  1  ,  out  )  ; 
} 
} 

public   void   meta  (  MetaMessage   event  )  { 
if  (  event  .  getType  (  )  ==  47  &&  stopListener  !=  null  )  { 
stopListener  .  actionPerformed  (  new   ActionEvent  (  MidiPlayer  .  this  ,  0  ,  ""  )  )  ; 
} 
} 





private   ArrayList  <  MidiChange  >  scoreToMidiNotes  (  )  { 
List  <  Denotator  >  noteList  =  (  (  FactorDenotator  )  score  )  .  getFactors  (  )  ; 
ArrayList  <  MidiChange  >  midiChanges  =  new   ArrayList  <  MidiChange  >  (  noteList  .  size  (  )  *  3  )  ; 
int   begin  =  Integer  .  MAX_VALUE  ; 
for  (  Denotator   d  :  noteList  )  { 
MidiChange   midiChange  =  new   MidiChange  (  (  FactorDenotator  )  d  ,  resolution  )  ; 
begin  =  Math  .  min  (  midiChange  .  getOnset  (  )  ,  begin  )  ; 
midiChanges  .  add  (  midiChange  )  ; 
midiChanges  .  add  (  midiChange  .  getNoteOff  (  )  )  ; 
} 
int   shift  =  -  begin  +  1  ; 
for  (  MidiChange   c  :  midiChanges  )  { 
c  .  shiftOnset  (  shift  )  ; 
} 
Collections  .  sort  (  midiChanges  )  ; 
return   midiChanges  ; 
} 





private   void   createTracksFromNotes  (  ArrayList  <  MidiChange  >  midiChanges  )  throws   InvalidMidiDataException  { 
Track  [  ]  tracks  =  new   Track  [  30  ]  ; 
int   channel  =  0  ; 
int   pitch  =  0  ; 
int   loudness  =  0  ; 
int   onset  =  0  ; 
int   track  =  0  ; 
for  (  MidiChange   midiChange  :  midiChanges  )  { 
onset  =  midiChange  .  getOnset  (  )  ; 
pitch  =  midiChange  .  getPitch  (  )  ; 
loudness  =  midiChange  .  getLoudness  (  )  ; 
channel  =  midiChange  .  getChannel  (  )  ; 
track  =  midiChange  .  getTrack  (  )  ; 
ShortMessage   msg  =  new   ShortMessage  (  )  ; 
switch  (  midiChange  .  getType  (  )  )  { 
case   MidiChange  .  NOTE_ON  : 
{ 
msg  .  setMessage  (  ShortMessage  .  NOTE_ON  ,  channel  ,  pitch  ,  loudness  )  ; 
break  ; 
} 
case   MidiChange  .  NOTE_OFF  : 
{ 
msg  .  setMessage  (  ShortMessage  .  NOTE_OFF  ,  channel  ,  pitch  ,  loudness  )  ; 
break  ; 
} 
case   MidiChange  .  CONTROL_CHANGE  : 
{ 
msg  .  setMessage  (  ShortMessage  .  CONTROL_CHANGE  ,  channel  ,  pitch  ,  loudness  )  ; 
break  ; 
} 
} 
loudness  =  Math  .  max  (  0  ,  Math  .  min  (  loudness  ,  127  )  )  ; 
pitch  =  Math  .  max  (  0  ,  Math  .  min  (  pitch  ,  127  )  )  ; 
if  (  tracks  [  track  ]  ==  null  )  { 
tracks  [  track  ]  =  sequence  .  createTrack  (  )  ; 
} 
tracks  [  track  ]  .  add  (  new   MidiEvent  (  msg  ,  onset  )  )  ; 
} 
if  (  tracks  [  0  ]  ==  null  )  { 
tracks  [  0  ]  =  sequence  .  createTrack  (  )  ; 
} 
ShortMessage   msg  =  new   ShortMessage  (  )  ; 
msg  .  setMessage  (  ShortMessage  .  STOP  )  ; 
tracks  [  0  ]  .  add  (  new   MidiEvent  (  msg  ,  onset  +  1500  )  )  ; 
} 





private   MidiEvent   makeProgramChangeEvent  (  int   channel  ,  int   onset  ,  int   program  )  { 
try  { 
ShortMessage   msg  =  new   ShortMessage  (  )  ; 
msg  .  setMessage  (  ShortMessage  .  PROGRAM_CHANGE  ,  channel  ,  program  ,  0  )  ; 
return   new   MidiEvent  (  msg  ,  onset  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 





private   MidiEvent   makeTempoEvent  (  int   mspq  ,  long   tick  )  { 
MetaMessage   msg  =  new   MetaMessage  (  )  ; 
byte  [  ]  data  =  new   byte  [  3  ]  ; 
data  [  2  ]  =  (  byte  )  (  mspq  &  0xff  )  ; 
mspq  >  >=  8  ; 
data  [  1  ]  =  (  byte  )  (  mspq  &  0xff  )  ; 
mspq  >  >=  8  ; 
data  [  0  ]  =  (  byte  )  (  mspq  &  0xff  )  ; 
try  { 
msg  .  setMessage  (  0x51  ,  data  ,  3  )  ; 
return   new   MidiEvent  (  msg  ,  tick  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 

private   ActionListener   stopListener  =  null  ; 

private   Receiver   receiver  =  null  ; 

private   Transmitter   transmitter  =  null  ; 

private   Synthesizer   synthesizer  =  null  ; 

private   Sequencer   sequencer  =  null  ; 

private   boolean   opened  =  false  ; 

private   double   latency  ; 

private   List  <  Receiver  >  receivers  =  new   LinkedList  <  Receiver  >  (  )  ; 

private   Sequence   sequence  =  null  ; 

private   int   resolution  =  480  ; 

private   int   mspq  =  500000  ; 

private   Denotator   score  =  null  ; 

private   Track   controlTrack  =  null  ; 

private   HashMap  <  Integer  ,  Integer  >  channelProgram  =  new   HashMap  <  Integer  ,  Integer  >  (  )  ; 

private   static   Repository   rep  =  Repository  .  systemRepository  (  )  ; 

private   static   Form   scoreForm  =  rep  .  getForm  (  "Score"  )  ; 
} 

