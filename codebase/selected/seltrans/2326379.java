package   vavi  .  sound  .  mfi  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  logging  .  Level  ; 
import   javax  .  sound  .  midi  .  InvalidMidiDataException  ; 
import   vavi  .  sound  .  mfi  .  spi  .  MfiDeviceProvider  ; 
import   vavi  .  sound  .  mfi  .  spi  .  MfiFileReader  ; 
import   vavi  .  sound  .  mfi  .  spi  .  MfiFileWriter  ; 
import   vavi  .  util  .  Debug  ; 











public   final   class   MfiSystem  { 


private   MfiSystem  (  )  { 
} 


public   static   MfiDevice  .  Info  [  ]  getMfiDeviceInfo  (  )  { 
return   provider  .  getDeviceInfo  (  )  ; 
} 


public   static   MfiDevice   getMfiDevice  (  MfiDevice  .  Info   info  )  throws   MfiUnavailableException  { 
return   provider  .  getDevice  (  info  )  ; 
} 











public   static   Sequencer   getSequencer  (  )  throws   MfiUnavailableException  { 
MfiDevice  .  Info  [  ]  infos  =  provider  .  getDeviceInfo  (  )  ; 
for  (  int   i  =  0  ;  i  <  infos  .  length  ;  i  ++  )  { 
MfiDevice   device  =  provider  .  getDevice  (  infos  [  i  ]  )  ; 
if  (  device   instanceof   Sequencer  )  { 
return  (  Sequencer  )  device  ; 
} 
} 
throw   new   MfiUnavailableException  (  "no sequencer available"  )  ; 
} 


public   static   javax  .  sound  .  midi  .  MetaEventListener   getMetaEventListener  (  )  throws   MfiUnavailableException  { 
MfiDevice  .  Info  [  ]  infos  =  provider  .  getDeviceInfo  (  )  ; 
for  (  int   i  =  0  ;  i  <  infos  .  length  ;  i  ++  )  { 
MfiDevice   device  =  provider  .  getDevice  (  infos  [  i  ]  )  ; 
if  (  device   instanceof   javax  .  sound  .  midi  .  MetaEventListener  )  { 
return  (  javax  .  sound  .  midi  .  MetaEventListener  )  device  ; 
} 
} 
throw   new   MfiUnavailableException  (  "no MetaEventListener available"  )  ; 
} 


public   static   MidiConverter   getMidiConverter  (  )  throws   MfiUnavailableException  { 
MfiDevice  .  Info  [  ]  infos  =  provider  .  getDeviceInfo  (  )  ; 
for  (  int   i  =  0  ;  i  <  infos  .  length  ;  i  ++  )  { 
MfiDevice   device  =  provider  .  getDevice  (  infos  [  i  ]  )  ; 
if  (  device   instanceof   MidiConverter  )  { 
return  (  MidiConverter  )  device  ; 
} 
} 
throw   new   MfiUnavailableException  (  "no midiConverter available"  )  ; 
} 


public   static   Sequence   toMfiSequence  (  javax  .  sound  .  midi  .  Sequence   sequence  )  throws   InvalidMidiDataException  ,  MfiUnavailableException  { 
MidiConverter   converter  =  MfiSystem  .  getMidiConverter  (  )  ; 
return   converter  .  toMfiSequence  (  sequence  )  ; 
} 






public   static   Sequence   toMfiSequence  (  javax  .  sound  .  midi  .  Sequence   sequence  ,  int   type  )  throws   InvalidMidiDataException  ,  MfiUnavailableException  { 
MidiConverter   converter  =  MfiSystem  .  getMidiConverter  (  )  ; 
return   converter  .  toMfiSequence  (  sequence  ,  type  )  ; 
} 


public   static   javax  .  sound  .  midi  .  Sequence   toMidiSequence  (  Sequence   sequence  )  throws   InvalidMfiDataException  ,  MfiUnavailableException  { 
MidiConverter   converter  =  MfiSystem  .  getMidiConverter  (  )  ; 
return   converter  .  toMidiSequence  (  sequence  )  ; 
} 


public   static   MfiFileFormat   getMfiFileFormat  (  InputStream   stream  )  throws   InvalidMfiDataException  ,  IOException  { 
for  (  int   i  =  0  ;  i  <  readers  .  length  ;  i  ++  )  { 
try  { 
MfiFileFormat   mff  =  readers  [  i  ]  .  getMfiFileFormat  (  stream  )  ; 
return   mff  ; 
}  catch  (  Exception   e  )  { 
Debug  .  println  (  e  )  ; 
} 
} 
throw   new   InvalidMfiDataException  (  "unsupported stream: "  +  stream  )  ; 
} 


public   static   MfiFileFormat   getMfiFileFormat  (  File   file  )  throws   InvalidMfiDataException  ,  IOException  { 
return   getMfiFileFormat  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
} 


public   static   MfiFileFormat   getMfiFileFormat  (  URL   url  )  throws   InvalidMfiDataException  ,  IOException  { 
return   getMfiFileFormat  (  new   BufferedInputStream  (  url  .  openStream  (  )  )  )  ; 
} 


public   static   Sequence   getSequence  (  InputStream   stream  )  throws   InvalidMfiDataException  ,  IOException  { 
for  (  int   i  =  0  ;  i  <  readers  .  length  ;  i  ++  )  { 
try  { 
Sequence   sequence  =  readers  [  i  ]  .  getSequence  (  stream  )  ; 
return   sequence  ; 
}  catch  (  InvalidMfiDataException   e  )  { 
Debug  .  println  (  e  )  ; 
continue  ; 
} 
} 
throw   new   InvalidMfiDataException  (  "unsupported stream: "  +  stream  )  ; 
} 


public   static   Sequence   getSequence  (  File   file  )  throws   InvalidMfiDataException  ,  IOException  { 
return   getSequence  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
} 


public   static   Sequence   getSequence  (  URL   url  )  throws   InvalidMfiDataException  ,  IOException  { 
return   getSequence  (  new   BufferedInputStream  (  url  .  openStream  (  )  )  )  ; 
} 


public   static   int  [  ]  getMfiFileTypes  (  )  { 
List  <  Integer  >  types  =  new   ArrayList  <  Integer  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  writers  .  length  ;  i  ++  )  { 
int  [  ]  ts  =  writers  [  i  ]  .  getMfiFileTypes  (  )  ; 
for  (  int   j  =  0  ;  j  <  ts  .  length  ;  j  ++  )  { 
types  .  add  (  ts  [  j  ]  )  ; 
} 
} 
int  [  ]  result  =  new   int  [  types  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  result  .  length  ;  i  ++  )  { 
result  [  i  ]  =  types  .  get  (  i  )  ; 
} 
return   result  ; 
} 


public   static   int  [  ]  getMfiFileTypes  (  Sequence   sequence  )  { 
List  <  Integer  >  types  =  new   ArrayList  <  Integer  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  writers  .  length  ;  i  ++  )  { 
int  [  ]  ts  =  writers  [  i  ]  .  getMfiFileTypes  (  sequence  )  ; 
for  (  int   j  =  0  ;  j  <  ts  .  length  ;  j  ++  )  { 
types  .  add  (  ts  [  j  ]  )  ; 
} 
} 
int  [  ]  result  =  new   int  [  types  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  result  .  length  ;  i  ++  )  { 
result  [  i  ]  =  types  .  get  (  i  )  ; 
} 
return   result  ; 
} 


public   static   boolean   isFileTypeSupported  (  int   fileType  )  { 
for  (  int   i  =  0  ;  i  <  writers  .  length  ;  i  ++  )  { 
if  (  writers  [  i  ]  .  isFileTypeSupported  (  fileType  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 


public   static   boolean   isFileTypeSupported  (  int   fileType  ,  Sequence   sequence  )  { 
for  (  int   i  =  0  ;  i  <  writers  .  length  ;  i  ++  )  { 
if  (  writers  [  i  ]  .  isFileTypeSupported  (  fileType  ,  sequence  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 


public   static   int   write  (  Sequence   in  ,  int   fileType  ,  OutputStream   out  )  throws   IOException  { 
for  (  int   i  =  0  ;  i  <  writers  .  length  ;  i  ++  )  { 
if  (  writers  [  i  ]  .  isFileTypeSupported  (  fileType  ,  in  )  )  { 
return   writers  [  i  ]  .  write  (  in  ,  fileType  ,  out  )  ; 
} 
} 
Debug  .  println  (  Level  .  WARNING  ,  "no writer found for: "  +  fileType  )  ; 
return   0  ; 
} 


public   static   int   write  (  Sequence   in  ,  int   fileType  ,  File   out  )  throws   IOException  { 
return   write  (  in  ,  fileType  ,  new   BufferedOutputStream  (  new   FileOutputStream  (  out  )  )  )  ; 
} 


private   static   MfiDeviceProvider  [  ]  providers  ; 


private   static   MfiFileReader  [  ]  readers  ; 


private   static   MfiFileWriter  [  ]  writers  ; 


private   static   MfiDeviceProvider   provider  ; 





static  { 
final   String   dir  =  "/META-INF/services/"  ; 
final   String   providerFile  =  "vavi.sound.mfi.spi.MfiDeviceProvider"  ; 
final   String   readerFile  =  "vavi.sound.mfi.spi.MfiFileReader"  ; 
final   String   writerFile  =  "vavi.sound.mfi.spi.MfiFileWriter"  ; 
Properties   props  =  new   Properties  (  )  ; 
Properties   mfiSystemProps  =  new   Properties  (  )  ; 
try  { 
Class  <  ?  >  clazz  =  MfiSystem  .  class  ; 
mfiSystemProps  .  load  (  clazz  .  getResourceAsStream  (  "MfiSystem.properties"  )  )  ; 
String   defaultProvider  =  mfiSystemProps  .  getProperty  (  "default.provider"  )  ; 
props  .  load  (  clazz  .  getResourceAsStream  (  dir  +  providerFile  )  )  ; 
props  .  list  (  System  .  err  )  ; 
Enumeration  <  ?  >  e  =  props  .  propertyNames  (  )  ; 
int   i  =  0  ; 
providers  =  new   MfiDeviceProvider  [  props  .  size  (  )  ]  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
@  SuppressWarnings  (  "unchecked"  )  Class  <  MfiDeviceProvider  >  c  =  (  Class  <  MfiDeviceProvider  >  )  Class  .  forName  (  (  String  )  e  .  nextElement  (  )  )  ; 
providers  [  i  ]  =  c  .  newInstance  (  )  ; 
if  (  c  .  getName  (  )  .  equals  (  defaultProvider  )  )  { 
provider  =  providers  [  i  ]  ; 
} 
i  ++  ; 
} 
Debug  .  println  (  "default provider: "  +  provider  .  getClass  (  )  .  getName  (  )  )  ; 
props  .  clear  (  )  ; 
props  .  load  (  clazz  .  getResourceAsStream  (  dir  +  readerFile  )  )  ; 
props  .  list  (  System  .  err  )  ; 
e  =  props  .  propertyNames  (  )  ; 
i  =  0  ; 
readers  =  new   MfiFileReader  [  props  .  size  (  )  ]  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
@  SuppressWarnings  (  "unchecked"  )  Class  <  MfiFileReader  >  c  =  (  Class  <  MfiFileReader  >  )  Class  .  forName  (  (  String  )  e  .  nextElement  (  )  )  ; 
readers  [  i  ++  ]  =  c  .  newInstance  (  )  ; 
} 
props  .  clear  (  )  ; 
props  .  load  (  clazz  .  getResourceAsStream  (  dir  +  writerFile  )  )  ; 
props  .  list  (  System  .  err  )  ; 
e  =  props  .  propertyNames  (  )  ; 
i  =  0  ; 
writers  =  new   MfiFileWriter  [  props  .  size  (  )  ]  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
@  SuppressWarnings  (  "unchecked"  )  Class  <  MfiFileWriter  >  c  =  (  Class  <  MfiFileWriter  >  )  Class  .  forName  (  (  String  )  e  .  nextElement  (  )  )  ; 
writers  [  i  ++  ]  =  c  .  newInstance  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
Debug  .  println  (  Level  .  SEVERE  ,  e  )  ; 
Debug  .  printStackTrace  (  e  )  ; 
System  .  exit  (  1  )  ; 
} 
} 






public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
final   Sequencer   sequencer  =  MfiSystem  .  getSequencer  (  )  ; 
sequencer  .  open  (  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
Debug  .  println  (  "START: "  +  args  [  i  ]  )  ; 
Sequence   sequence  =  MfiSystem  .  getSequence  (  new   File  (  args  [  i  ]  )  )  ; 
sequencer  .  setSequence  (  sequence  )  ; 
if  (  i  ==  args  .  length  -  1  )  { 
sequencer  .  addMetaEventListener  (  new   MetaEventListener  (  )  { 

public   void   meta  (  MetaMessage   meta  )  { 
Debug  .  println  (  meta  .  getType  (  )  )  ; 
if  (  meta  .  getType  (  )  ==  47  )  { 
sequencer  .  close  (  )  ; 
} 
} 
}  )  ; 
} 
sequencer  .  start  (  )  ; 
Debug  .  println  (  "END: "  +  args  [  i  ]  )  ; 
} 
} 
} 

