package   org  .  jsynthlib  .  synthdrivers  .  AlesisQS  ; 

import   org  .  jsynthlib  .  core  .  Device  ; 
import   org  .  jsynthlib  .  core  .  Driver  ; 
import   org  .  jsynthlib  .  core  .  Logger  ; 
import   org  .  jsynthlib  .  core  .  Patch  ; 
import   org  .  jsynthlib  .  core  .  SysexHandler  ; 










public   class   AlesisQSEffectsDriver   extends   Driver  { 

public   AlesisQSEffectsDriver  (  final   Device   device  )  { 
super  (  device  ,  "Effects"  ,  "Zellyn Hunter"  )  ; 
sysexID  =  "F000000E0E**"  ; 
sysexRequestDump  =  new   SysexHandler  (  "F0 00 00 0E 0E *opcode* *patchNum* F7"  )  ; 
patchSize  =  QSConstants  .  PATCH_SIZE_EFFECTS  ; 
deviceIDoffset  =  0  ; 
checksumStart  =  0  ; 
checksumEnd  =  0  ; 
checksumOffset  =  0  ; 
bankNumbers  =  QSConstants  .  WRITEABLE_BANK_NAMES  ; 
patchNumbers  =  QSConstants  .  PATCH_NUMBERS_EFFECTS_WITH_EDIT_BUFFERS  ; 
} 




public   String   toBinaryStr  (  byte   b  )  { 
String   output  =  new   String  (  )  ; 
for  (  int   i  =  7  ;  i  >=  0  ;  i  --  )  { 
output  +=  (  (  b  >  >  i  )  &  1  )  ; 
} 
return   output  ; 
} 








protected   void   calculateChecksum  (  Patch   p  ,  int   start  ,  int   end  ,  int   ofs  )  { 
} 





public   Patch   createNewPatch  (  )  { 
byte  [  ]  sysex  =  new   byte  [  patchSize  ]  ; 
for  (  int   i  =  0  ;  i  <  QSConstants  .  GENERIC_HEADER  .  length  ;  i  ++  )  { 
sysex  [  i  ]  =  QSConstants  .  GENERIC_HEADER  [  i  ]  ; 
} 
sysex  [  QSConstants  .  POSITION_OPCODE  ]  =  QSConstants  .  OPCODE_MIDI_USER_EFFECTS_DUMP  ; 
sysex  [  QSConstants  .  POSITION_LOCATION  ]  =  0  ; 
return   new   Patch  (  sysex  ,  this  )  ; 
} 











public   void   requestPatchDump  (  int   bankNum  ,  int   patchNum  )  { 
int   location  =  patchNum  ; 
int   opcode  =  QSConstants  .  OPCODE_MIDI_USER_EFFECTS_DUMP_REQ  ; 
if  (  location  >  QSConstants  .  MAX_LOCATION_PROG  )  { 
location  -=  (  QSConstants  .  MAX_LOCATION_PROG  +  1  )  ; 
opcode  =  QSConstants  .  OPCODE_MIDI_EDIT_EFFECTS_DUMP_REQ  ; 
} 
send  (  sysexRequestDump  .  toSysexMessage  (  getChannel  (  )  ,  new   SysexHandler  .  NameValue  (  "opcode"  ,  opcode  )  ,  new   SysexHandler  .  NameValue  (  "patchNum"  ,  location  )  )  )  ; 
} 





public   void   sendPatch  (  Patch   p  )  { 
storePatch  (  p  ,  0  ,  QSConstants  .  MAX_LOCATION_PROG  +  1  )  ; 
} 









public   void   storePatch  (  Patch   p  ,  int   bankNum  ,  int   patchNum  )  { 
int   location  =  patchNum  ; 
byte   opcode  =  QSConstants  .  OPCODE_MIDI_USER_EFFECTS_DUMP  ; 
byte   oldOpcode  =  (  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_OPCODE  ]  ; 
byte   oldLocation  =  (  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_LOCATION  ]  ; 
if  (  location  >  QSConstants  .  MAX_LOCATION_PROG  )  { 
location  -=  (  QSConstants  .  MAX_LOCATION_PROG  +  1  )  ; 
opcode  =  QSConstants  .  OPCODE_MIDI_EDIT_EFFECTS_DUMP  ; 
} 
(  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_OPCODE  ]  =  opcode  ; 
(  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_LOCATION  ]  =  (  byte  )  location  ; 
Logger  .  reportStatus  (  (  (  Patch  )  p  )  .  sysex  )  ; 
sendPatchWorker  (  p  )  ; 
(  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_OPCODE  ]  =  oldOpcode  ; 
(  (  Patch  )  p  )  .  sysex  [  QSConstants  .  POSITION_LOCATION  ]  =  oldLocation  ; 
} 
} 

