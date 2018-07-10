package   com  .  syntelos  .  foam  ; 













public   class   B64  { 



public   static   final   int   MAX_LINE_LENGTH  =  76  ; 



public   static   final   byte   EQUALS_SIGN  =  (  byte  )  '='  ; 



public   static   final   byte  [  ]  NEW_LINE  =  {  (  byte  )  '\r'  ,  (  byte  )  '\n'  }  ; 



public   static   final   byte  [  ]  ALPHABET  =  {  (  byte  )  'A'  ,  (  byte  )  'B'  ,  (  byte  )  'C'  ,  (  byte  )  'D'  ,  (  byte  )  'E'  ,  (  byte  )  'F'  ,  (  byte  )  'G'  ,  (  byte  )  'H'  ,  (  byte  )  'I'  ,  (  byte  )  'J'  ,  (  byte  )  'K'  ,  (  byte  )  'L'  ,  (  byte  )  'M'  ,  (  byte  )  'N'  ,  (  byte  )  'O'  ,  (  byte  )  'P'  ,  (  byte  )  'Q'  ,  (  byte  )  'R'  ,  (  byte  )  'S'  ,  (  byte  )  'T'  ,  (  byte  )  'U'  ,  (  byte  )  'V'  ,  (  byte  )  'W'  ,  (  byte  )  'X'  ,  (  byte  )  'Y'  ,  (  byte  )  'Z'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  ,  (  byte  )  'g'  ,  (  byte  )  'h'  ,  (  byte  )  'i'  ,  (  byte  )  'j'  ,  (  byte  )  'k'  ,  (  byte  )  'l'  ,  (  byte  )  'm'  ,  (  byte  )  'n'  ,  (  byte  )  'o'  ,  (  byte  )  'p'  ,  (  byte  )  'q'  ,  (  byte  )  'r'  ,  (  byte  )  's'  ,  (  byte  )  't'  ,  (  byte  )  'u'  ,  (  byte  )  'v'  ,  (  byte  )  'w'  ,  (  byte  )  'x'  ,  (  byte  )  'y'  ,  (  byte  )  'z'  ,  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  '+'  ,  (  byte  )  '/'  }  ; 





public   static   final   byte  [  ]  DECODABET  =  {  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  5  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  62  ,  -  9  ,  -  9  ,  -  9  ,  63  ,  52  ,  53  ,  54  ,  55  ,  56  ,  57  ,  58  ,  59  ,  60  ,  61  ,  -  9  ,  -  9  ,  -  9  ,  -  1  ,  -  9  ,  -  9  ,  -  9  ,  0  ,  1  ,  2  ,  3  ,  4  ,  5  ,  6  ,  7  ,  8  ,  9  ,  10  ,  11  ,  12  ,  13  ,  14  ,  15  ,  16  ,  17  ,  18  ,  19  ,  20  ,  21  ,  22  ,  23  ,  24  ,  25  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  26  ,  27  ,  28  ,  29  ,  30  ,  31  ,  32  ,  33  ,  34  ,  35  ,  36  ,  37  ,  38  ,  39  ,  40  ,  41  ,  42  ,  43  ,  44  ,  45  ,  46  ,  47  ,  48  ,  49  ,  50  ,  51  ,  -  9  ,  -  9  ,  -  9  ,  -  9  }  ; 

public   static   final   byte   BAD_ENCODING  =  -  9  ; 

public   static   final   byte   WHITE_SPACE_ENC  =  -  5  ; 

public   static   final   byte   EQUALS_SIGN_ENC  =  -  1  ; 









public   static   final   byte  [  ]  encode3to4  (  byte  [  ]  threeBytes  )  { 
return   encode3to4  (  threeBytes  ,  3  )  ; 
} 














public   static   final   byte  [  ]  encode3to4  (  byte  [  ]  threeBytes  ,  int   numSigBytes  )  { 
byte  [  ]  dest  =  new   byte  [  4  ]  ; 
encode3to4  (  threeBytes  ,  0  ,  numSigBytes  ,  dest  ,  0  )  ; 
return   dest  ; 
} 
















public   static   final   byte  [  ]  encode3to4  (  byte  [  ]  b4  ,  byte  [  ]  threeBytes  ,  int   numSigBytes  )  { 
encode3to4  (  threeBytes  ,  0  ,  numSigBytes  ,  b4  ,  0  )  ; 
return   b4  ; 
} 






















public   static   final   byte  [  ]  encode3to4  (  byte  [  ]  source  ,  int   srcOffset  ,  int   numSigBytes  ,  byte  [  ]  destination  ,  int   destOffset  )  { 
int   inBuff  =  (  numSigBytes  >  0  ?  (  (  source  [  srcOffset  ]  <<  24  )  >  >  >  8  )  :  0  )  |  (  numSigBytes  >  1  ?  (  (  source  [  srcOffset  +  1  ]  <<  24  )  >  >  >  16  )  :  0  )  |  (  numSigBytes  >  2  ?  (  (  source  [  srcOffset  +  2  ]  <<  24  )  >  >  >  24  )  :  0  )  ; 
switch  (  numSigBytes  )  { 
case   3  : 
destination  [  destOffset  ]  =  ALPHABET  [  (  inBuff  >  >  >  18  )  ]  ; 
destination  [  destOffset  +  1  ]  =  ALPHABET  [  (  inBuff  >  >  >  12  )  &  0x3f  ]  ; 
destination  [  destOffset  +  2  ]  =  ALPHABET  [  (  inBuff  >  >  >  6  )  &  0x3f  ]  ; 
destination  [  destOffset  +  3  ]  =  ALPHABET  [  (  inBuff  )  &  0x3f  ]  ; 
return   destination  ; 
case   2  : 
destination  [  destOffset  ]  =  ALPHABET  [  (  inBuff  >  >  >  18  )  ]  ; 
destination  [  destOffset  +  1  ]  =  ALPHABET  [  (  inBuff  >  >  >  12  )  &  0x3f  ]  ; 
destination  [  destOffset  +  2  ]  =  ALPHABET  [  (  inBuff  >  >  >  6  )  &  0x3f  ]  ; 
destination  [  destOffset  +  3  ]  =  EQUALS_SIGN  ; 
return   destination  ; 
case   1  : 
destination  [  destOffset  ]  =  ALPHABET  [  (  inBuff  >  >  >  18  )  ]  ; 
destination  [  destOffset  +  1  ]  =  ALPHABET  [  (  inBuff  >  >  >  12  )  &  0x3f  ]  ; 
destination  [  destOffset  +  2  ]  =  EQUALS_SIGN  ; 
destination  [  destOffset  +  3  ]  =  EQUALS_SIGN  ; 
return   destination  ; 
default  : 
return   destination  ; 
} 
} 






public   static   final   byte  [  ]  encode  (  byte  [  ]  source  )  { 
if  (  null  ==  source  )  return   null  ;  else   return   encode  (  source  ,  0  ,  source  .  length  )  ; 
} 






public   static   final   int   encode  (  java  .  io  .  InputStream   in  ,  java  .  io  .  OutputStream   out  )  throws   java  .  io  .  IOException  { 
Encoder   enc  =  new   Encoder  (  out  )  ; 
int   read  ,  buflen  =  512  ,  acc  =  0  ; 
byte  [  ]  buf  =  new   byte  [  buflen  ]  ; 
while  (  0  <  (  read  =  in  .  read  (  buf  ,  0  ,  buflen  )  )  )  { 
enc  .  write  (  buf  ,  0  ,  read  )  ; 
acc  +=  read  ; 
} 
enc  .  flush  (  )  ; 
return   acc  ; 
} 








public   static   final   byte  [  ]  encode  (  byte  [  ]  source  ,  int   off  ,  int   len  )  { 
int   expansion  =  (  (  len  *  4  )  /  3  )  ; 
int   buflen  =  expansion  ; 
buflen  +=  (  (  (  len  %  3  )  >  0  )  ?  (  4  )  :  (  0  )  )  ; 
buflen  +=  (  (  (  expansion  *  2  )  /  MAX_LINE_LENGTH  )  )  ; 
byte  [  ]  outbuf  =  new   byte  [  buflen  ]  ; 
int   srcp  =  0  ; 
int   outp  =  0  ; 
int   len2  =  len  -  2  ; 
int   linelen  =  0  ; 
while  (  srcp  <  len2  )  { 
encode3to4  (  source  ,  (  srcp  +  off  )  ,  3  ,  outbuf  ,  outp  )  ; 
srcp  +=  3  ; 
outp  +=  4  ; 
linelen  +=  4  ; 
if  (  linelen  ==  MAX_LINE_LENGTH  )  { 
outbuf  [  outp  ++  ]  =  NEW_LINE  [  0  ]  ; 
outbuf  [  outp  ++  ]  =  NEW_LINE  [  1  ]  ; 
linelen  =  0  ; 
} 
} 
if  (  srcp  <  len  )  { 
encode3to4  (  source  ,  (  srcp  +  off  )  ,  (  len  -  srcp  )  ,  outbuf  ,  outp  )  ; 
outp  +=  4  ; 
} 
if  (  outp  <  outbuf  .  length  )  { 
byte  [  ]  re  =  new   byte  [  outp  ]  ; 
System  .  arraycopy  (  outbuf  ,  0  ,  re  ,  0  ,  outp  )  ; 
return   re  ; 
}  else   return   outbuf  ; 
} 










public   static   final   byte  [  ]  decode4to3  (  byte  [  ]  fourBytes  )  { 
byte  [  ]  outbuf1  =  new   byte  [  3  ]  ; 
int   count  =  decode4to3  (  fourBytes  ,  0  ,  outbuf1  ,  0  )  ; 
byte  [  ]  outbuf2  =  new   byte  [  count  ]  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  outbuf2  [  i  ]  =  outbuf1  [  i  ]  ; 
return   outbuf2  ; 
} 






















public   static   final   int   decode4to3  (  byte  [  ]  source  ,  int   srcOffset  ,  byte  [  ]  destination  ,  int   destOffset  )  { 
if  (  source  [  srcOffset  +  2  ]  ==  EQUALS_SIGN  )  { 
int   outbuf  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outbuf  >  >  >  16  )  ; 
return   1  ; 
}  else   if  (  source  [  srcOffset  +  3  ]  ==  EQUALS_SIGN  )  { 
int   outbuf  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  |  (  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  &  0xFF  )  <<  6  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outbuf  >  >  >  16  )  ; 
destination  [  destOffset  +  1  ]  =  (  byte  )  (  outbuf  >  >  >  8  )  ; 
return   2  ; 
}  else  { 
try  { 
int   outbuf  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  |  (  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  &  0xFF  )  <<  6  )  |  (  (  DECODABET  [  source  [  srcOffset  +  3  ]  ]  &  0xFF  )  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outbuf  >  >  16  )  ; 
destination  [  destOffset  +  1  ]  =  (  byte  )  (  outbuf  >  >  8  )  ; 
destination  [  destOffset  +  2  ]  =  (  byte  )  (  outbuf  )  ; 
return   3  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  ""  +  source  [  srcOffset  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  1  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  2  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  3  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  3  ]  ]  )  )  ; 
return  -  1  ; 
} 
} 
} 






public   static   final   int   decode  (  java  .  io  .  InputStream   in  ,  java  .  io  .  OutputStream   out  )  throws   java  .  io  .  IOException  { 
Decoder   dec  =  new   Decoder  (  in  )  ; 
int   read  ,  buflen  =  512  ,  acc  =  0  ; 
byte  [  ]  buf  =  new   byte  [  buflen  ]  ; 
while  (  0  <  (  read  =  dec  .  read  (  buf  ,  0  ,  buflen  )  )  )  { 
acc  +=  read  ; 
out  .  write  (  buf  ,  0  ,  read  )  ; 
} 
return   acc  ; 
} 

public   static   final   byte  [  ]  decode  (  byte  [  ]  bytes  )  { 
if  (  null  ==  bytes  )  return   null  ;  else   return   decode  (  bytes  ,  0  ,  bytes  .  length  )  ; 
} 









public   static   final   byte  [  ]  decode  (  byte  [  ]  source  ,  int   off  ,  int   len  )  { 
int   len34  =  len  *  3  /  4  ; 
byte  [  ]  outbuf  =  new   byte  [  len34  ]  ; 
int   outbufPosn  =  0  ; 
byte  [  ]  b4  =  new   byte  [  4  ]  ; 
int   b4Posn  =  0  ; 
int   i  =  0  ; 
byte   sbiCrop  =  0  ; 
byte   sbiDecode  =  0  ; 
for  (  i  =  off  ;  i  <  off  +  len  ;  i  ++  )  { 
sbiCrop  =  (  byte  )  (  source  [  i  ]  &  0x7f  )  ; 
sbiDecode  =  DECODABET  [  sbiCrop  ]  ; 
if  (  sbiDecode  >=  WHITE_SPACE_ENC  )  { 
if  (  sbiDecode  >=  EQUALS_SIGN_ENC  )  { 
b4  [  b4Posn  ++  ]  =  sbiCrop  ; 
if  (  b4Posn  >  3  )  { 
outbufPosn  +=  decode4to3  (  b4  ,  0  ,  outbuf  ,  outbufPosn  )  ; 
b4Posn  =  0  ; 
if  (  sbiCrop  ==  EQUALS_SIGN  )  break  ; 
} 
} 
}  else   throw   new   IllegalStateException  (  "Byte value 0x"  +  Integer  .  toHexString  (  source  [  i  ]  )  +  " at offset "  +  i  )  ; 
} 
byte  [  ]  out  =  new   byte  [  outbufPosn  ]  ; 
System  .  arraycopy  (  outbuf  ,  0  ,  out  ,  0  ,  outbufPosn  )  ; 
return   out  ; 
} 








public   static   class   Decoder   extends   java  .  io  .  FilterInputStream  { 

private   static   final   int   bufferLength  =  3  ; 

private   int   position  =  -  1  ; 

private   byte  [  ]  buffer  =  new   byte  [  bufferLength  ]  ; 

private   int   numSigBytes  ; 



public   Decoder  (  java  .  io  .  InputStream   in  )  { 
super  (  in  )  ; 
} 





public   int   read  (  )  throws   java  .  io  .  IOException  { 
if  (  0  >  this  .  position  )  { 
byte  [  ]  b4  =  new   byte  [  4  ]  ; 
int   cc  =  0  ; 
for  (  cc  =  0  ;  cc  <  4  ;  cc  ++  )  { 
int   b  =  0  ; 
do  { 
b  =  this  .  in  .  read  (  )  ; 
}  while  (  b  >=  0  &&  DECODABET  [  b  &  0x7f  ]  <=  WHITE_SPACE_ENC  )  ; 
if  (  b  <  0  )  break  ;  else   b4  [  cc  ]  =  (  byte  )  b  ; 
} 
if  (  cc  ==  4  )  { 
this  .  numSigBytes  =  decode4to3  (  b4  ,  0  ,  this  .  buffer  ,  0  )  ; 
this  .  position  =  0  ; 
}  else   if  (  0  ==  cc  )  return  -  1  ;  else   throw   new   java  .  io  .  IOException  (  "Improperly padded Base64 input."  )  ; 
} 
if  (  -  1  <  this  .  position  )  { 
if  (  this  .  numSigBytes  <=  this  .  position  )  return  -  1  ;  else  { 
int   b  =  this  .  buffer  [  this  .  position  ++  ]  ; 
if  (  this  .  position  >=  this  .  bufferLength  )  this  .  position  =  -  1  ; 
return  (  b  &  0xff  )  ; 
} 
}  else   throw   new   java  .  io  .  IOException  (  "Error in Base64 code reading stream."  )  ; 
} 










public   int   read  (  byte  [  ]  dest  ,  int   off  ,  int   len  )  throws   java  .  io  .  IOException  { 
int   cc  ; 
int   ch  ; 
for  (  cc  =  0  ;  cc  <  len  ;  cc  ++  )  { 
ch  =  read  (  )  ; 
if  (  -  1  <  ch  )  dest  [  off  +  cc  ]  =  (  byte  )  ch  ;  else   if  (  0  ==  cc  )  return  -  1  ;  else   break  ; 
} 
return   cc  ; 
} 
} 








public   static   class   Encoder   extends   java  .  io  .  FilterOutputStream  { 

private   static   final   int   bufferLength  =  3  ; 

private   int   position  =  0  ; 

private   byte  [  ]  buffer  =  new   byte  [  bufferLength  ]  ; 

private   int   linelen  =  0  ; 

private   byte  [  ]  b4  =  new   byte  [  4  ]  ; 



public   Encoder  (  java  .  io  .  OutputStream   out  )  { 
super  (  out  )  ; 
} 






public   void   write  (  int   bb  )  throws   java  .  io  .  IOException  { 
this  .  buffer  [  this  .  position  ++  ]  =  (  byte  )  bb  ; 
if  (  bufferLength  <=  this  .  position  )  { 
this  .  out  .  write  (  encode3to4  (  this  .  b4  ,  this  .  buffer  ,  bufferLength  )  )  ; 
this  .  linelen  +=  4  ; 
if  (  linelen  >=  MAX_LINE_LENGTH  )  { 
this  .  out  .  write  (  NEW_LINE  )  ; 
this  .  linelen  =  0  ; 
} 
this  .  position  =  0  ; 
} 
} 





public   void   write  (  byte  [  ]  bbs  ,  int   off  ,  int   len  )  throws   java  .  io  .  IOException  { 
for  (  int   cc  =  0  ;  cc  <  len  ;  cc  ++  )  this  .  write  (  bbs  [  off  +  cc  ]  )  ; 
} 




public   void   flush  (  )  throws   java  .  io  .  IOException  { 
if  (  0  <  this  .  position  )  { 
this  .  out  .  write  (  encode3to4  (  this  .  b4  ,  this  .  buffer  ,  this  .  position  )  )  ; 
this  .  position  =  0  ; 
} 
super  .  flush  (  )  ; 
} 
} 

private   static   final   void   usage  (  java  .  io  .  PrintStream   out  )  { 
out  .  println  (  )  ; 
out  .  println  (  " Usage: B64 (-e|-d)"  )  ; 
out  .  println  (  )  ; 
out  .  println  (  "\tEncode or Decode stdin to stdout."  )  ; 
out  .  println  (  )  ; 
System  .  exit  (  1  )  ; 
} 

public   static   void   main  (  String  [  ]  argv  )  { 
try  { 
boolean   fwd  =  true  ; 
if  (  null  !=  argv  )  { 
int   alen  =  argv  .  length  ; 
if  (  0  <  alen  )  { 
String   arg  ; 
int   arglen  ,  ch  ; 
for  (  int   argc  =  0  ;  argc  <  alen  ;  argc  ++  )  { 
arg  =  argv  [  argc  ]  ; 
arglen  =  arg  .  length  (  )  ; 
if  (  '-'  ==  arg  .  charAt  (  0  )  )  { 
if  (  1  <  arglen  )  { 
ch  =  arg  .  charAt  (  1  )  ; 
if  (  'e'  ==  ch  )  { 
fwd  =  true  ; 
}  else   if  (  'd'  ==  ch  )  { 
fwd  =  false  ; 
}  else   usage  (  System  .  err  )  ; 
}  else   usage  (  System  .  err  )  ; 
}  else   usage  (  System  .  err  )  ; 
} 
}  else   usage  (  System  .  err  )  ; 
}  else   usage  (  System  .  err  )  ; 
if  (  fwd  )  encode  (  System  .  in  ,  System  .  out  )  ;  else   decode  (  System  .  in  ,  System  .  out  )  ; 
System  .  err  .  println  (  )  ; 
System  .  exit  (  0  )  ; 
}  catch  (  IllegalArgumentException   ilarg  )  { 
System  .  err  .  println  (  "Error: "  +  ilarg  .  getMessage  (  )  )  ; 
}  catch  (  Exception   exc  )  { 
exc  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
} 
} 

