package   net  .  sf  .  xwav  .  soundrenderer  ; 

import   java  .  util  .  *  ; 
import   javax  .  sound  .  sampled  .  *  ; 



















public   class   FloatSampleTools  { 


public   static   final   float   DEFAULT_DITHER_BITS  =  0.7f  ; 

private   static   Random   random  =  null  ; 

static   final   int   F_8  =  1  ; 

static   final   int   F_16  =  2  ; 

static   final   int   F_24_3  =  3  ; 

static   final   int   F_24_4  =  4  ; 

static   final   int   F_32  =  5  ; 

static   final   int   F_SAMPLE_WIDTH_MASK  =  F_8  |  F_16  |  F_24_3  |  F_24_4  |  F_32  ; 

static   final   int   F_SIGNED  =  8  ; 

static   final   int   F_BIGENDIAN  =  16  ; 

static   final   int   CT_8S  =  F_8  |  F_SIGNED  ; 

static   final   int   CT_8U  =  F_8  ; 

static   final   int   CT_16SB  =  F_16  |  F_SIGNED  |  F_BIGENDIAN  ; 

static   final   int   CT_16SL  =  F_16  |  F_SIGNED  ; 

static   final   int   CT_24_3SB  =  F_24_3  |  F_SIGNED  |  F_BIGENDIAN  ; 

static   final   int   CT_24_3SL  =  F_24_3  |  F_SIGNED  ; 

static   final   int   CT_24_4SB  =  F_24_4  |  F_SIGNED  |  F_BIGENDIAN  ; 

static   final   int   CT_24_4SL  =  F_24_4  |  F_SIGNED  ; 

static   final   int   CT_32SB  =  F_32  |  F_SIGNED  |  F_BIGENDIAN  ; 

static   final   int   CT_32SL  =  F_32  |  F_SIGNED  ; 


private   FloatSampleTools  (  )  { 
} 







static   void   checkSupportedSampleSize  (  int   ssib  ,  int   channels  ,  int   frameSize  )  { 
if  (  ssib  ==  24  &&  frameSize  ==  4  *  channels  )  { 
return  ; 
} 
if  (  (  ssib  *  channels  )  !=  frameSize  *  8  )  { 
throw   new   IllegalArgumentException  (  "unsupported sample size: "  +  ssib  +  " bits stored in "  +  (  frameSize  /  channels  )  +  " bytes."  )  ; 
} 
} 






static   int   getFormatType  (  AudioFormat   format  )  { 
boolean   signed  =  format  .  getEncoding  (  )  .  equals  (  AudioFormat  .  Encoding  .  PCM_SIGNED  )  ; 
if  (  !  signed  &&  !  format  .  getEncoding  (  )  .  equals  (  AudioFormat  .  Encoding  .  PCM_UNSIGNED  )  )  { 
throw   new   IllegalArgumentException  (  "unsupported encoding: only PCM encoding supported."  )  ; 
} 
if  (  !  signed  &&  format  .  getSampleSizeInBits  (  )  !=  8  )  { 
throw   new   IllegalArgumentException  (  "unsupported encoding: only 8-bit can be unsigned"  )  ; 
} 
checkSupportedSampleSize  (  format  .  getSampleSizeInBits  (  )  ,  format  .  getChannels  (  )  ,  format  .  getFrameSize  (  )  )  ; 
int   formatType  =  getFormatType  (  format  .  getSampleSizeInBits  (  )  ,  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ,  signed  ,  format  .  isBigEndian  (  )  )  ; 
return   formatType  ; 
} 




static   int   getFormatType  (  int   ssib  ,  int   bytesPerSample  ,  boolean   signed  ,  boolean   bigEndian  )  { 
int   res  =  0  ; 
if  (  ssib  ==  24  ||  (  bytesPerSample  ==  ssib  /  8  )  )  { 
if  (  ssib  ==  8  )  { 
res  =  F_8  ; 
}  else   if  (  ssib  ==  16  )  { 
res  =  F_16  ; 
}  else   if  (  ssib  ==  24  )  { 
if  (  bytesPerSample  ==  3  )  { 
res  =  F_24_3  ; 
}  else   if  (  bytesPerSample  ==  4  )  { 
res  =  F_24_4  ; 
} 
}  else   if  (  ssib  ==  32  )  { 
res  =  F_32  ; 
} 
} 
if  (  res  ==  0  )  { 
throw   new   IllegalArgumentException  (  "ConversionTool: unsupported sample size of "  +  ssib  +  " bits per sample in "  +  bytesPerSample  +  " bytes."  )  ; 
} 
if  (  !  signed  &&  bytesPerSample  >  1  )  { 
throw   new   IllegalArgumentException  (  "ConversionTool: unsigned samples larger than "  +  "8 bit are not supported"  )  ; 
} 
if  (  signed  )  { 
res  |=  F_SIGNED  ; 
} 
if  (  bigEndian  &&  (  ssib  !=  8  )  )  { 
res  |=  F_BIGENDIAN  ; 
} 
return   res  ; 
} 

static   int   getSampleSize  (  int   formatType  )  { 
switch  (  formatType  &  F_SAMPLE_WIDTH_MASK  )  { 
case   F_8  : 
return   1  ; 
case   F_16  : 
return   2  ; 
case   F_24_3  : 
return   3  ; 
case   F_24_4  : 
return   4  ; 
case   F_32  : 
return   4  ; 
} 
return   0  ; 
} 




static   String   formatType2Str  (  int   formatType  )  { 
String   res  =  ""  +  formatType  +  ": "  ; 
switch  (  formatType  &  F_SAMPLE_WIDTH_MASK  )  { 
case   F_8  : 
res  +=  "8bit"  ; 
break  ; 
case   F_16  : 
res  +=  "16bit"  ; 
break  ; 
case   F_24_3  : 
res  +=  "24_3bit"  ; 
break  ; 
case   F_24_4  : 
res  +=  "24_4bit"  ; 
break  ; 
case   F_32  : 
res  +=  "32bit"  ; 
break  ; 
} 
res  +=  (  (  formatType  &  F_SIGNED  )  ==  F_SIGNED  )  ?  " signed"  :  " unsigned"  ; 
if  (  (  formatType  &  F_SAMPLE_WIDTH_MASK  )  !=  F_8  )  { 
res  +=  (  (  formatType  &  F_BIGENDIAN  )  ==  F_BIGENDIAN  )  ?  " big endian"  :  " little endian"  ; 
} 
return   res  ; 
} 

private   static   final   float   twoPower7  =  128.0f  ; 

private   static   final   float   twoPower15  =  32768.0f  ; 

private   static   final   float   twoPower23  =  8388608.0f  ; 

private   static   final   float   twoPower31  =  2147483648.0f  ; 

private   static   final   float   invTwoPower7  =  1  /  twoPower7  ; 

private   static   final   float   invTwoPower15  =  1  /  twoPower15  ; 

private   static   final   float   invTwoPower23  =  1  /  twoPower23  ; 

private   static   final   float   invTwoPower31  =  1  /  twoPower31  ; 






public   static   void   byte2float  (  byte  [  ]  input  ,  int   inByteOffset  ,  List  <  float  [  ]  >  output  ,  int   outOffset  ,  int   frameCount  ,  AudioFormat   format  )  { 
byte2float  (  input  ,  inByteOffset  ,  output  ,  outOffset  ,  frameCount  ,  format  ,  true  )  ; 
} 









public   static   void   byte2float  (  byte  [  ]  input  ,  int   inByteOffset  ,  Object  [  ]  output  ,  int   outOffset  ,  int   frameCount  ,  AudioFormat   format  )  { 
byte2float  (  input  ,  inByteOffset  ,  output  ,  outOffset  ,  frameCount  ,  format  ,  true  )  ; 
} 











public   static   void   byte2float  (  byte  [  ]  input  ,  int   inByteOffset  ,  Object  [  ]  output  ,  int   outOffset  ,  int   frameCount  ,  AudioFormat   format  ,  boolean   allowAddChannel  )  { 
int   channels  =  format  .  getChannels  (  )  ; 
if  (  !  allowAddChannel  &&  channels  >  output  .  length  )  { 
channels  =  output  .  length  ; 
} 
if  (  output  .  length  <  channels  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "too few channel output array"  )  ; 
} 
for  (  int   channel  =  0  ;  channel  <  channels  ;  channel  ++  )  { 
float  [  ]  data  =  (  float  [  ]  )  output  [  channel  ]  ; 
if  (  data  .  length  <  frameCount  +  outOffset  )  { 
data  =  new   float  [  frameCount  +  outOffset  ]  ; 
output  [  channel  ]  =  data  ; 
} 
byte2floatGeneric  (  input  ,  inByteOffset  ,  format  .  getFrameSize  (  )  ,  data  ,  outOffset  ,  frameCount  ,  format  )  ; 
inByteOffset  +=  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ; 
} 
} 






























public   static   void   byte2float  (  byte  [  ]  input  ,  int   inByteOffset  ,  List  <  float  [  ]  >  output  ,  int   outOffset  ,  int   frameCount  ,  AudioFormat   format  ,  boolean   allowAddChannel  )  { 
int   channels  =  format  .  getChannels  (  )  ; 
if  (  !  allowAddChannel  &&  channels  >  output  .  size  (  )  )  { 
channels  =  output  .  size  (  )  ; 
} 
for  (  int   channel  =  0  ;  channel  <  channels  ;  channel  ++  )  { 
float  [  ]  data  ; 
if  (  output  .  size  (  )  <  channel  )  { 
data  =  new   float  [  frameCount  +  outOffset  ]  ; 
output  .  add  (  data  )  ; 
}  else  { 
data  =  output  .  get  (  channel  )  ; 
if  (  data  .  length  <  frameCount  +  outOffset  )  { 
data  =  new   float  [  frameCount  +  outOffset  ]  ; 
output  .  set  (  channel  ,  data  )  ; 
} 
} 
byte2floatGeneric  (  input  ,  inByteOffset  ,  format  .  getFrameSize  (  )  ,  data  ,  outOffset  ,  frameCount  ,  format  )  ; 
inByteOffset  +=  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ; 
} 
} 























public   static   void   byte2floatInterleaved  (  byte  [  ]  input  ,  int   inByteOffset  ,  float  [  ]  output  ,  int   outOffset  ,  int   frameCount  ,  AudioFormat   format  )  { 
byte2floatGeneric  (  input  ,  inByteOffset  ,  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ,  output  ,  outOffset  ,  frameCount  *  format  .  getChannels  (  )  ,  format  )  ; 
} 


























static   void   byte2floatGeneric  (  byte  [  ]  input  ,  int   inByteOffset  ,  int   inByteStep  ,  float  [  ]  output  ,  int   outOffset  ,  int   sampleCount  ,  AudioFormat   format  )  { 
int   formatType  =  getFormatType  (  format  )  ; 
byte2floatGeneric  (  input  ,  inByteOffset  ,  inByteStep  ,  output  ,  outOffset  ,  sampleCount  ,  formatType  )  ; 
} 
















static   void   byte2floatGeneric  (  byte  [  ]  input  ,  int   inByteOffset  ,  int   inByteStep  ,  float  [  ]  output  ,  int   outOffset  ,  int   sampleCount  ,  int   formatType  )  { 
int   endCount  =  outOffset  +  sampleCount  ; 
int   inIndex  =  inByteOffset  ; 
for  (  int   outIndex  =  outOffset  ;  outIndex  <  endCount  ;  outIndex  ++  ,  inIndex  +=  inByteStep  )  { 
switch  (  formatType  )  { 
case   CT_8S  : 
output  [  outIndex  ]  =  (  (  float  )  input  [  inIndex  ]  )  *  invTwoPower7  ; 
break  ; 
case   CT_8U  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  ]  &  0xFF  )  -  128  )  )  *  invTwoPower7  ; 
break  ; 
case   CT_16SB  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  ]  <<  8  )  |  (  input  [  inIndex  +  1  ]  &  0xFF  )  )  )  *  invTwoPower15  ; 
break  ; 
case   CT_16SL  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  +  1  ]  <<  8  )  |  (  input  [  inIndex  ]  &  0xFF  )  )  )  *  invTwoPower15  ; 
break  ; 
case   CT_24_3SB  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  ]  <<  16  )  |  (  (  input  [  inIndex  +  1  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  +  2  ]  &  0xFF  )  )  )  *  invTwoPower23  ; 
break  ; 
case   CT_24_3SL  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  +  2  ]  <<  16  )  |  (  (  input  [  inIndex  +  1  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  ]  &  0xFF  )  )  )  *  invTwoPower23  ; 
break  ; 
case   CT_24_4SB  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  +  1  ]  <<  16  )  |  (  (  input  [  inIndex  +  2  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  +  3  ]  &  0xFF  )  )  )  *  invTwoPower23  ; 
break  ; 
case   CT_24_4SL  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  +  3  ]  <<  16  )  |  (  (  input  [  inIndex  +  2  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  +  1  ]  &  0xFF  )  )  )  *  invTwoPower23  ; 
break  ; 
case   CT_32SB  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  ]  <<  24  )  |  (  (  input  [  inIndex  +  1  ]  &  0xFF  )  <<  16  )  |  (  (  input  [  inIndex  +  2  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  +  3  ]  &  0xFF  )  )  )  *  invTwoPower31  ; 
break  ; 
case   CT_32SL  : 
output  [  outIndex  ]  =  (  (  float  )  (  (  input  [  inIndex  +  3  ]  <<  24  )  |  (  (  input  [  inIndex  +  2  ]  &  0xFF  )  <<  16  )  |  (  (  input  [  inIndex  +  1  ]  &  0xFF  )  <<  8  )  |  (  input  [  inIndex  ]  &  0xFF  )  )  )  *  invTwoPower31  ; 
break  ; 
default  : 
throw   new   IllegalArgumentException  (  "unsupported format="  +  formatType2Str  (  formatType  )  )  ; 
} 
} 
} 

private   static   byte   quantize8  (  float   sample  ,  float   ditherBits  )  { 
if  (  ditherBits  !=  0  )  { 
sample  +=  random  .  nextFloat  (  )  *  ditherBits  ; 
} 
if  (  sample  >=  127.0f  )  { 
return  (  byte  )  127  ; 
}  else   if  (  sample  <=  -  128.0f  )  { 
return  (  byte  )  -  128  ; 
}  else  { 
return  (  byte  )  (  sample  <  0  ?  (  sample  -  0.5f  )  :  (  sample  +  0.5f  )  )  ; 
} 
} 

private   static   int   quantize16  (  float   sample  ,  float   ditherBits  )  { 
if  (  ditherBits  !=  0  )  { 
sample  +=  random  .  nextFloat  (  )  *  ditherBits  ; 
} 
if  (  sample  >=  32767.0f  )  { 
return   32767  ; 
}  else   if  (  sample  <=  -  32768.0f  )  { 
return  -  32768  ; 
}  else  { 
return  (  int  )  (  sample  <  0  ?  (  sample  -  0.5f  )  :  (  sample  +  0.5f  )  )  ; 
} 
} 

private   static   int   quantize24  (  float   sample  ,  float   ditherBits  )  { 
if  (  ditherBits  !=  0  )  { 
sample  +=  random  .  nextFloat  (  )  *  ditherBits  ; 
} 
if  (  sample  >=  8388607.0f  )  { 
return   8388607  ; 
}  else   if  (  sample  <=  -  8388608.0f  )  { 
return  -  8388608  ; 
}  else  { 
return  (  int  )  (  sample  <  0  ?  (  sample  -  0.5f  )  :  (  sample  +  0.5f  )  )  ; 
} 
} 

private   static   int   quantize32  (  float   sample  ,  float   ditherBits  )  { 
if  (  ditherBits  !=  0  )  { 
sample  +=  random  .  nextFloat  (  )  *  ditherBits  ; 
} 
if  (  sample  >=  2147483647.0f  )  { 
return   2147483647  ; 
}  else   if  (  sample  <=  -  2147483648.0f  )  { 
return  -  2147483648  ; 
}  else  { 
return  (  int  )  (  sample  <  0  ?  (  sample  -  0.5f  )  :  (  sample  +  0.5f  )  )  ; 
} 
} 






































public   static   void   float2byte  (  List  <  float  [  ]  >  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   frameCount  ,  AudioFormat   format  ,  float   ditherBits  )  { 
for  (  int   channel  =  0  ;  channel  <  format  .  getChannels  (  )  ;  channel  ++  )  { 
float  [  ]  data  =  (  float  [  ]  )  input  .  get  (  channel  )  ; 
float2byteGeneric  (  data  ,  inOffset  ,  output  ,  outByteOffset  ,  format  .  getFrameSize  (  )  ,  frameCount  ,  format  ,  ditherBits  )  ; 
outByteOffset  +=  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ; 
} 
} 








public   static   void   float2byte  (  Object  [  ]  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   frameCount  ,  AudioFormat   format  ,  float   ditherBits  )  { 
int   channels  =  format  .  getChannels  (  )  ; 
for  (  int   channel  =  0  ;  channel  <  channels  ;  channel  ++  )  { 
float  [  ]  data  =  (  float  [  ]  )  input  [  channel  ]  ; 
float2byteGeneric  (  data  ,  inOffset  ,  output  ,  outByteOffset  ,  format  .  getFrameSize  (  )  ,  frameCount  ,  format  ,  ditherBits  )  ; 
outByteOffset  +=  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ; 
} 
} 











static   void   float2byte  (  Object  [  ]  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   frameCount  ,  int   formatCode  ,  int   channels  ,  int   frameSize  ,  float   ditherBits  )  { 
int   sampleSize  =  frameSize  /  channels  ; 
for  (  int   channel  =  0  ;  channel  <  channels  ;  channel  ++  )  { 
float  [  ]  data  =  (  float  [  ]  )  input  [  channel  ]  ; 
float2byteGeneric  (  data  ,  inOffset  ,  output  ,  outByteOffset  ,  frameSize  ,  frameCount  ,  formatCode  ,  ditherBits  )  ; 
outByteOffset  +=  sampleSize  ; 
} 
} 


































public   static   void   float2byteInterleaved  (  float  [  ]  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   frameCount  ,  AudioFormat   format  ,  float   ditherBits  )  { 
float2byteGeneric  (  input  ,  inOffset  ,  output  ,  outByteOffset  ,  format  .  getFrameSize  (  )  /  format  .  getChannels  (  )  ,  frameCount  *  format  .  getChannels  (  )  ,  format  ,  ditherBits  )  ; 
} 


























static   void   float2byteGeneric  (  float  [  ]  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   outByteStep  ,  int   sampleCount  ,  AudioFormat   format  ,  float   ditherBits  )  { 
int   formatType  =  getFormatType  (  format  )  ; 
float2byteGeneric  (  input  ,  inOffset  ,  output  ,  outByteOffset  ,  outByteStep  ,  sampleCount  ,  formatType  ,  ditherBits  )  ; 
} 
















static   void   float2byteGeneric  (  float  [  ]  input  ,  int   inOffset  ,  byte  [  ]  output  ,  int   outByteOffset  ,  int   outByteStep  ,  int   sampleCount  ,  int   formatType  ,  float   ditherBits  )  { 
if  (  inOffset  <  0  ||  inOffset  +  sampleCount  >  input  .  length  ||  sampleCount  <  0  )  { 
throw   new   IllegalArgumentException  (  "invalid input index: "  +  "input.length="  +  input  .  length  +  " inOffset="  +  inOffset  +  " sampleCount="  +  sampleCount  )  ; 
} 
if  (  outByteOffset  <  0  ||  outByteOffset  +  (  sampleCount  *  outByteStep  )  >=  (  output  .  length  +  outByteStep  )  ||  outByteStep  <  getSampleSize  (  formatType  )  )  { 
throw   new   IllegalArgumentException  (  "invalid output index: "  +  "output.length="  +  output  .  length  +  " outByteOffset="  +  outByteOffset  +  " outByteStep="  +  outByteStep  +  " sampleCount="  +  sampleCount  +  " format="  +  formatType2Str  (  formatType  )  )  ; 
} 
if  (  ditherBits  !=  0.0f  &&  random  ==  null  )  { 
random  =  new   Random  (  )  ; 
} 
int   endSample  =  inOffset  +  sampleCount  ; 
int   iSample  ; 
int   outIndex  =  outByteOffset  ; 
for  (  int   inIndex  =  inOffset  ;  inIndex  <  endSample  ;  inIndex  ++  ,  outIndex  +=  outByteStep  )  { 
switch  (  formatType  )  { 
case   CT_8S  : 
output  [  outIndex  ]  =  quantize8  (  input  [  inIndex  ]  *  twoPower7  ,  ditherBits  )  ; 
break  ; 
case   CT_8U  : 
output  [  outIndex  ]  =  (  byte  )  (  quantize8  (  (  input  [  inIndex  ]  *  twoPower7  )  ,  ditherBits  )  +  128  )  ; 
break  ; 
case   CT_16SB  : 
iSample  =  quantize16  (  input  [  inIndex  ]  *  twoPower15  ,  ditherBits  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  >  >  8  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_16SL  : 
iSample  =  quantize16  (  input  [  inIndex  ]  *  twoPower15  ,  ditherBits  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  iSample  >  >  8  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_24_3SB  : 
iSample  =  quantize24  (  input  [  inIndex  ]  *  twoPower23  ,  ditherBits  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  >  >  16  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_24_3SL  : 
iSample  =  quantize24  (  input  [  inIndex  ]  *  twoPower23  ,  ditherBits  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  iSample  >  >  16  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_24_4SB  : 
iSample  =  quantize24  (  input  [  inIndex  ]  *  twoPower23  ,  ditherBits  )  ; 
output  [  outIndex  +  0  ]  =  0  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  iSample  >  >  16  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  +  3  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_24_4SL  : 
iSample  =  quantize24  (  input  [  inIndex  ]  *  twoPower23  ,  ditherBits  )  ; 
output  [  outIndex  +  3  ]  =  (  byte  )  (  iSample  >  >  16  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
output  [  outIndex  +  0  ]  =  0  ; 
break  ; 
case   CT_32SB  : 
iSample  =  quantize32  (  input  [  inIndex  ]  *  twoPower31  ,  ditherBits  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  >  >  24  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  (  iSample  >  >  >  16  )  &  0xFF  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  +  3  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
case   CT_32SL  : 
iSample  =  quantize32  (  input  [  inIndex  ]  *  twoPower31  ,  ditherBits  )  ; 
output  [  outIndex  +  3  ]  =  (  byte  )  (  iSample  >  >  24  )  ; 
output  [  outIndex  +  2  ]  =  (  byte  )  (  (  iSample  >  >  >  16  )  &  0xFF  )  ; 
output  [  outIndex  +  1  ]  =  (  byte  )  (  (  iSample  >  >  >  8  )  &  0xFF  )  ; 
output  [  outIndex  ]  =  (  byte  )  (  iSample  &  0xFF  )  ; 
break  ; 
default  : 
throw   new   IllegalArgumentException  (  "unsupported format="  +  formatType2Str  (  formatType  )  )  ; 
} 
} 
} 
} 

