package   org  .  mmtk  .  utility  .  gcspy  .  drivers  ; 

import   org  .  mmtk  .  policy  .  LargeObjectSpace  ; 
import   org  .  mmtk  .  utility  .  gcspy  .  Color  ; 
import   org  .  mmtk  .  utility  .  gcspy  .  StreamConstants  ; 
import   org  .  mmtk  .  utility  .  gcspy  .  Subspace  ; 
import   org  .  mmtk  .  vm  .  gcspy  .  IntStream  ; 
import   org  .  mmtk  .  vm  .  gcspy  .  ShortStream  ; 
import   org  .  mmtk  .  vm  .  gcspy  .  ServerInterpreter  ; 
import   org  .  mmtk  .  utility  .  Conversions  ; 
import   org  .  mmtk  .  utility  .  Log  ; 
import   org  .  mmtk  .  vm  .  VM  ; 
import   org  .  vmmagic  .  unboxed  .  *  ; 
import   org  .  vmmagic  .  pragma  .  *  ; 




@  Uninterruptible 
public   class   TreadmillDriver   extends   AbstractDriver  { 

private   static   final   boolean   DEBUG  =  false  ; 

protected   IntStream   usedSpaceStream  ; 

protected   ShortStream   objectsStream  ; 

protected   ShortStream   rootsStream  ; 

protected   ShortStream   refFromImmortalStream  ; 

protected   Subspace   subspace  ; 

protected   int   allTileNum  ; 

protected   int   totalObjects  =  0  ; 

protected   int   totalUsedSpace  =  0  ; 

protected   int   totalRoots  =  0  ; 

protected   int   totalRefFromImmortal  =  0  ; 

protected   Address   maxAddr  ; 

protected   int   threshold  ; 











public   TreadmillDriver  (  ServerInterpreter   server  ,  String   spaceName  ,  LargeObjectSpace   lospace  ,  int   blockSize  ,  int   threshold  ,  boolean   mainSpace  )  { 
super  (  server  ,  spaceName  ,  lospace  ,  blockSize  ,  mainSpace  )  ; 
if  (  DEBUG  )  { 
Log  .  write  (  "TreadmillDriver for "  )  ; 
Log  .  write  (  spaceName  )  ; 
Log  .  write  (  ", blocksize="  )  ; 
Log  .  write  (  blockSize  )  ; 
Log  .  write  (  ", start="  )  ; 
Log  .  write  (  lospace  .  getStart  (  )  )  ; 
Log  .  write  (  ", extent="  )  ; 
Log  .  write  (  lospace  .  getExtent  (  )  )  ; 
Log  .  write  (  ", maxTileNum="  )  ; 
Log  .  writeln  (  maxTileNum  )  ; 
} 
this  .  threshold  =  threshold  ; 
subspace  =  createSubspace  (  lospace  )  ; 
allTileNum  =  0  ; 
maxAddr  =  lospace  .  getStart  (  )  ; 
usedSpaceStream  =  createUsedSpaceStream  (  )  ; 
objectsStream  =  createObjectsStream  (  )  ; 
rootsStream  =  createRootsStream  (  )  ; 
refFromImmortalStream  =  createRefFromImmortalStream  (  )  ; 
serverSpace  .  resize  (  0  )  ; 
resetData  (  )  ; 
} 





protected   String   getDriverName  (  )  { 
return  "MMTk TreadmillDriver"  ; 
} 

@  Interruptible 
private   IntStream   createUsedSpaceStream  (  )  { 
return   VM  .  newGCspyIntStream  (  this  ,  "Used Space stream"  ,  0  ,  blockSize  ,  0  ,  0  ,  "Space used: "  ,  " bytes"  ,  StreamConstants  .  PRESENTATION_PERCENT  ,  StreamConstants  .  PAINT_STYLE_ZERO  ,  0  ,  Color  .  Red  ,  true  )  ; 
} 

@  Interruptible 
private   ShortStream   createObjectsStream  (  )  { 
return   VM  .  newGCspyShortStream  (  this  ,  "Objects stream"  ,  (  short  )  0  ,  (  short  )  (  blockSize  /  threshold  )  ,  (  short  )  0  ,  (  short  )  0  ,  "No. of objects = "  ,  " objects"  ,  StreamConstants  .  PRESENTATION_PLUS  ,  StreamConstants  .  PAINT_STYLE_ZERO  ,  0  ,  Color  .  Green  ,  true  )  ; 
} 

@  Interruptible 
private   ShortStream   createRootsStream  (  )  { 
return   VM  .  newGCspyShortStream  (  this  ,  "Roots stream"  ,  (  short  )  0  ,  (  short  )  (  maxObjectsPerBlock  (  blockSize  )  /  8  )  ,  (  short  )  0  ,  (  short  )  0  ,  "Roots: "  ,  " objects"  ,  StreamConstants  .  PRESENTATION_PLUS  ,  StreamConstants  .  PAINT_STYLE_ZERO  ,  0  ,  Color  .  Blue  ,  true  )  ; 
} 

@  Interruptible 
private   ShortStream   createRefFromImmortalStream  (  )  { 
return   VM  .  newGCspyShortStream  (  this  ,  "References from Immortal stream"  ,  (  short  )  0  ,  (  short  )  (  maxObjectsPerBlock  (  blockSize  )  /  8  )  ,  (  short  )  0  ,  (  short  )  0  ,  "References from immortal space: "  ,  " references"  ,  StreamConstants  .  PRESENTATION_PLUS  ,  StreamConstants  .  PAINT_STYLE_ZERO  ,  0  ,  Color  .  Blue  ,  true  )  ; 
} 




public   void   resetData  (  )  { 
super  .  resetData  (  )  ; 
usedSpaceStream  .  resetData  (  )  ; 
objectsStream  .  resetData  (  )  ; 
refFromImmortalStream  .  resetData  (  )  ; 
totalUsedSpace  =  0  ; 
totalObjects  =  0  ; 
totalRefFromImmortal  =  0  ; 
} 








public   void   scan  (  Address   addr  )  { 
int   index  =  subspace  .  getIndex  (  addr  )  ; 
int   length  =  (  (  LargeObjectSpace  )  mmtkSpace  )  .  getSize  (  addr  )  .  toInt  (  )  ; 
if  (  DEBUG  )  { 
Log  .  write  (  "TreadmillDriver: super="  ,  addr  )  ; 
Log  .  write  (  ", index="  ,  index  )  ; 
Log  .  write  (  ", pages="  ,  length  )  ; 
Log  .  write  (  ", bytes="  ,  Conversions  .  pagesToBytes  (  length  )  .  toInt  (  )  )  ; 
Log  .  writeln  (  ", max="  ,  usedSpaceStream  .  getMaxValue  (  )  )  ; 
} 
totalObjects  ++  ; 
totalUsedSpace  +=  length  ; 
objectsStream  .  increment  (  index  ,  (  short  )  1  )  ; 
int   remainder  =  subspace  .  spaceRemaining  (  addr  )  ; 
usedSpaceStream  .  distribute  (  index  ,  remainder  ,  blockSize  ,  length  )  ; 
Address   tmp  =  addr  .  plus  (  length  )  ; 
if  (  tmp  .  GT  (  maxAddr  )  )  maxAddr  =  tmp  ; 
} 






public   void   transmit  (  int   event  )  { 
if  (  !  isConnected  (  event  )  )  return  ; 
Address   start  =  subspace  .  getStart  (  )  ; 
int   required  =  countTileNum  (  start  ,  maxAddr  ,  blockSize  )  ; 
int   current  =  subspace  .  getBlockNum  (  )  ; 
if  (  required  >  current  ||  maxAddr  !=  subspace  .  getEnd  (  )  )  { 
subspace  .  reset  (  start  ,  maxAddr  ,  0  ,  required  )  ; 
allTileNum  =  required  ; 
serverSpace  .  resize  (  allTileNum  )  ; 
setTilenames  (  subspace  ,  allTileNum  )  ; 
} 
setupSummaries  (  )  ; 
controlValues  (  CONTROL_USED  ,  subspace  .  getFirstIndex  (  )  ,  subspace  .  getBlockNum  (  )  )  ; 
Offset   size  =  subspace  .  getEnd  (  )  .  diff  (  subspace  .  getStart  (  )  )  ; 
setSpaceInfo  (  size  )  ; 
send  (  event  ,  allTileNum  )  ; 
} 





protected   void   setupSummaries  (  )  { 
usedSpaceStream  .  setSummary  (  totalUsedSpace  ,  subspace  .  getEnd  (  )  .  diff  (  subspace  .  getStart  (  )  )  .  toInt  (  )  )  ; 
objectsStream  .  setSummary  (  totalObjects  )  ; 
rootsStream  .  setSummary  (  totalRoots  )  ; 
refFromImmortalStream  .  setSummary  (  totalRefFromImmortal  )  ; 
} 







public   boolean   handleRoot  (  Address   addr  )  { 
if  (  subspace  .  addressInRange  (  addr  )  )  { 
int   index  =  subspace  .  getIndex  (  addr  )  ; 
rootsStream  .  increment  (  index  ,  (  short  )  1  )  ; 
this  .  totalRoots  ++  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 






public   void   resetRootsStream  (  )  { 
rootsStream  .  resetData  (  )  ; 
totalRoots  =  0  ; 
} 







public   boolean   handleReferenceFromImmortalSpace  (  Address   addr  )  { 
if  (  subspace  .  addressInRange  (  addr  )  )  { 
int   index  =  subspace  .  getIndex  (  addr  )  ; 
refFromImmortalStream  .  increment  (  index  ,  (  short  )  1  )  ; 
this  .  totalRefFromImmortal  ++  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 
} 

