package   uk  .  ac  .  gla  .  terrier  .  compression  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   uk  .  ac  .  gla  .  terrier  .  utility  .  io  .  RandomDataOutput  ; 


































public   class   OldBitFile   extends   BitFile  { 


private   static   Logger   logger  =  Logger  .  getRootLogger  (  )  ; 


protected   static   final   double   LOG_E_2  =  Math  .  log  (  2.0d  )  ; 



protected   static   final   String   DEFAULT_FILE_MODE  =  "rw"  ; 


protected   boolean   isLastByteIncomplete  ; 


protected   byte   bitOffset  ; 


protected   ByteArrayOutputStream   outBuffer  ; 


protected   byte   buffer  ; 


protected   int   readBitOffset  ; 



public   OldBitFile  (  File   file  )  { 
super  (  file  )  ; 
bitOffset  =  0  ; 
byteOffset  =  0  ; 
} 


public   OldBitFile  (  File   file  ,  String   access  )  { 
super  (  file  ,  access  )  ; 
bitOffset  =  0  ; 
byteOffset  =  0  ; 
} 


public   OldBitFile  (  String   filename  )  { 
super  (  filename  )  ; 
bitOffset  =  0  ; 
byteOffset  =  0  ; 
} 


public   OldBitFile  (  String   filename  ,  String   access  )  { 
super  (  filename  ,  access  )  ; 
} 




public   void   close  (  )  { 
try  { 
file  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Input/Output exception while closing the BitFile. Stack trace follows"  ,  ioe  )  ; 
} 
} 







public   byte   getBitOffset  (  )  { 
return   bitOffset  ; 
} 






public   long   getByteOffset  (  )  { 
return   byteOffset  ; 
} 





public   int   readGamma  (  )  { 
final   int   unaryPart  =  readUnary  (  )  ; 
int   binaryPart  =  0  ; 
for  (  int   i  =  0  ;  i  <  unaryPart  -  1  ;  i  ++  )  { 
if  (  (  inBuffer  [  (  int  )  byteOffset  ]  &  (  1  <<  (  bitOffset  )  )  )  !=  0  )  binaryPart  =  binaryPart  +  (  1  <<  i  )  ; 
readBitOffset  ++  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 
} 
return   binaryPart  +  (  1  <<  (  unaryPart  -  1  )  )  ; 
} 





public   void   align  (  )  { 
if  (  (  bitOffset  &  7  )  ==  0  )  return  ; 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 














public   BitIn   readReset  (  long   startByteOffset  ,  byte   startBitOffset  ,  long   endByteOffset  ,  byte   endBitOffset  )  { 
try  { 
file  .  seek  (  startByteOffset  )  ; 
inBuffer  =  new   byte  [  (  int  )  (  endByteOffset  -  startByteOffset  +  1  )  ]  ; 
file  .  readFully  (  inBuffer  )  ; 
readBits  =  (  inBuffer  .  length  *  8  )  -  startBitOffset  -  (  8  -  endBitOffset  )  +  startBitOffset  ; 
byteOffset  =  0  ; 
readBitOffset  =  startBitOffset  ; 
bitOffset  =  startBitOffset  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Input/Output exception while reading from a random access file. Stack trace follows"  ,  ioe  )  ; 
} 
return   this  ; 
} 





public   int   readUnary  (  )  { 
int   result  =  0  ; 
while  (  readBitOffset  <=  readBits  )  { 
if  (  (  inBuffer  [  (  int  )  byteOffset  ]  &  (  1  <<  (  bitOffset  )  )  )  !=  0  )  { 
result  ++  ; 
readBitOffset  ++  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 
}  else  { 
result  ++  ; 
readBitOffset  ++  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 
break  ; 
} 
} 
return   result  ; 
} 


public   byte  [  ]  getInBuffer  (  )  { 
return   inBuffer  ; 
} 






public   void   writeFlush  (  )  { 
try  { 
if  (  bitOffset  >  0  )  outBuffer  .  write  (  buffer  )  ; 
if  (  isLastByteIncomplete  )  { 
writeFile  .  seek  (  file  .  length  (  )  -  1  )  ; 
writeFile  .  write  (  outBuffer  .  toByteArray  (  )  )  ; 
}  else   writeFile  .  write  (  outBuffer  .  toByteArray  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Input/Output exception while writing to the file. Stack trace follows."  ,  ioe  )  ; 
} 
} 








public   int   readBinary  (  final   int   noBits  )  { 
if  (  noBits  ==  0  )  return   0  ; 
int   binary  =  0  ; 
for  (  int   i  =  0  ;  i  <  noBits  ;  i  ++  )  { 
if  (  (  inBuffer  [  (  int  )  byteOffset  ]  &  (  1  <<  (  bitOffset  )  )  )  !=  0  )  { 
binary  =  binary  +  (  1  <<  i  )  ; 
} 
readBitOffset  ++  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 
} 
return   binary  ; 
} 




public   void   skipBits  (  final   int   noBits  )  { 
if  (  noBits  ==  0  )  return  ; 
for  (  int   i  =  0  ;  i  <  noBits  ;  i  ++  )  { 
readBitOffset  ++  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
} 
} 
} 








public   int   writeBinary  (  int   bitsToWrite  ,  int   n  )  { 
byte   rem  ; 
while  (  n  !=  0  )  { 
rem  =  (  byte  )  (  n  %  2  )  ; 
buffer  |=  (  rem  <<  bitOffset  )  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
n  =  n  /  2  ; 
bitsToWrite  --  ; 
} 
while  (  bitsToWrite  >  0  )  { 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
bitsToWrite  --  ; 
} 
return  -  1  ; 
} 







public   int   writeGamma  (  int   n  )  { 
final   byte   mask  =  1  ; 
final   int   floor  =  (  int  )  Math  .  floor  (  Math  .  log  (  n  )  /  LOG_E_2  )  ; 
writeUnary  (  floor  +  1  )  ; 
final   int   secondPart  =  (  int  )  (  n  -  (  1  <<  (  floor  )  )  )  ; 
for  (  int   i  =  0  ;  i  <  floor  ;  i  ++  )  { 
if  (  (  secondPart  &  (  1  <<  i  )  )  !=  0  )  { 
buffer  |=  (  mask  <<  bitOffset  )  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
}  else  { 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
} 
} 
return  -  1  ; 
} 







public   void   writeReset  (  )  throws   IOException  { 
if  (  !  (  file   instanceof   RandomDataOutput  )  )  throw   new   IOException  (  "Cannot write to read only BitFile file"  )  ; 
writeFile  =  (  RandomDataOutput  )  file  ; 
outBuffer  =  new   ByteArrayOutputStream  (  )  ; 
if  (  bitOffset  !=  0  )  { 
isLastByteIncomplete  =  true  ; 
try  { 
byteOffset  =  file  .  length  (  )  -  1  ; 
file  .  seek  (  byteOffset  )  ; 
buffer  =  file  .  readByte  (  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Input/output exception while reading file. Stack trace follows."  ,  ioe  )  ; 
} 
}  else  { 
isLastByteIncomplete  =  false  ; 
buffer  =  0  ; 
} 
} 







public   int   writeUnary  (  int   n  )  { 
final   byte   mask  =  1  ; 
for  (  int   i  =  0  ;  i  <  n  -  1  ;  i  ++  )  { 
buffer  |=  (  mask  <<  bitOffset  )  ; 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
} 
bitOffset  ++  ; 
if  (  bitOffset  ==  8  )  { 
bitOffset  =  0  ; 
byteOffset  ++  ; 
outBuffer  .  write  (  buffer  )  ; 
buffer  =  0  ; 
} 
return  -  1  ; 
} 
} 

