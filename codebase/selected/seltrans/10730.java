package   cross  .  io  .  misc  ; 



































































































public   class   Base64  { 









public   static   class   InputStream   extends   java  .  io  .  FilterInputStream  { 

private   final   boolean   encode  ; 

private   int   position  ; 

private   final   byte  [  ]  buffer  ; 

private   final   int   bufferLength  ; 

private   int   numSigBytes  ; 

private   int   lineLength  ; 

private   final   boolean   breakLines  ; 

private   final   int   options  ; 

private   final   byte  [  ]  alphabet  ; 

private   final   byte  [  ]  decodabet  ; 








public   InputStream  (  final   java  .  io  .  InputStream   in  )  { 
this  (  in  ,  Base64  .  DECODE  )  ; 
} 


























public   InputStream  (  final   java  .  io  .  InputStream   in  ,  final   int   options  )  { 
super  (  in  )  ; 
this  .  breakLines  =  (  options  &  Base64  .  DONT_BREAK_LINES  )  !=  Base64  .  DONT_BREAK_LINES  ; 
this  .  encode  =  (  options  &  Base64  .  ENCODE  )  ==  Base64  .  ENCODE  ; 
this  .  bufferLength  =  this  .  encode  ?  4  :  3  ; 
this  .  buffer  =  new   byte  [  this  .  bufferLength  ]  ; 
this  .  position  =  -  1  ; 
this  .  lineLength  =  0  ; 
this  .  options  =  options  ; 
this  .  alphabet  =  Base64  .  getAlphabet  (  options  )  ; 
this  .  decodabet  =  Base64  .  getDecodabet  (  options  )  ; 
} 








@  Override 
public   int   read  (  )  throws   java  .  io  .  IOException  { 
if  (  this  .  position  <  0  )  { 
if  (  this  .  encode  )  { 
final   byte  [  ]  b3  =  new   byte  [  3  ]  ; 
int   numBinaryBytes  =  0  ; 
for  (  int   i  =  0  ;  i  <  3  ;  i  ++  )  { 
try  { 
final   int   b  =  this  .  in  .  read  (  )  ; 
if  (  b  >=  0  )  { 
b3  [  i  ]  =  (  byte  )  b  ; 
numBinaryBytes  ++  ; 
} 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
if  (  i  ==  0  )  { 
throw   e  ; 
} 
} 
} 
if  (  numBinaryBytes  >  0  )  { 
Base64  .  encode3to4  (  b3  ,  0  ,  numBinaryBytes  ,  this  .  buffer  ,  0  ,  this  .  options  )  ; 
this  .  position  =  0  ; 
this  .  numSigBytes  =  4  ; 
}  else  { 
return  -  1  ; 
} 
}  else  { 
final   byte  [  ]  b4  =  new   byte  [  4  ]  ; 
int   i  =  0  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
int   b  =  0  ; 
do  { 
b  =  this  .  in  .  read  (  )  ; 
}  while  (  (  b  >=  0  )  &&  (  this  .  decodabet  [  b  &  0x7f  ]  <=  Base64  .  WHITE_SPACE_ENC  )  )  ; 
if  (  b  <  0  )  { 
break  ; 
} 
b4  [  i  ]  =  (  byte  )  b  ; 
} 
if  (  i  ==  4  )  { 
this  .  numSigBytes  =  Base64  .  decode4to3  (  b4  ,  0  ,  this  .  buffer  ,  0  ,  this  .  options  )  ; 
this  .  position  =  0  ; 
}  else   if  (  i  ==  0  )  { 
return  -  1  ; 
}  else  { 
throw   new   java  .  io  .  IOException  (  "Improperly padded Base64 input."  )  ; 
} 
} 
} 
if  (  this  .  position  >=  0  )  { 
if  (  this  .  position  >=  this  .  numSigBytes  )  { 
return  -  1  ; 
} 
if  (  this  .  encode  &&  this  .  breakLines  &&  (  this  .  lineLength  >=  Base64  .  MAX_LINE_LENGTH  )  )  { 
this  .  lineLength  =  0  ; 
return  '\n'  ; 
}  else  { 
this  .  lineLength  ++  ; 
final   int   b  =  this  .  buffer  [  this  .  position  ++  ]  ; 
if  (  this  .  position  >=  this  .  bufferLength  )  { 
this  .  position  =  -  1  ; 
} 
return   b  &  0xFF  ; 
} 
}  else  { 
throw   new   java  .  io  .  IOException  (  "Error in Base64 code reading stream."  )  ; 
} 
} 















@  Override 
public   int   read  (  final   byte  [  ]  dest  ,  final   int   off  ,  final   int   len  )  throws   java  .  io  .  IOException  { 
int   i  ; 
int   b  ; 
for  (  i  =  0  ;  i  <  len  ;  i  ++  )  { 
b  =  read  (  )  ; 
if  (  b  >=  0  )  { 
dest  [  off  +  i  ]  =  (  byte  )  b  ; 
}  else   if  (  i  ==  0  )  { 
return  -  1  ; 
}  else  { 
break  ; 
} 
} 
return   i  ; 
} 
} 









public   static   class   OutputStream   extends   java  .  io  .  FilterOutputStream  { 

private   final   boolean   encode  ; 

private   int   position  ; 

private   byte  [  ]  buffer  ; 

private   final   int   bufferLength  ; 

private   int   lineLength  ; 

private   final   boolean   breakLines  ; 

private   final   byte  [  ]  b4  ; 

private   boolean   suspendEncoding  ; 

private   final   int   options  ; 

private   final   byte  [  ]  alphabet  ; 

private   final   byte  [  ]  decodabet  ; 









public   OutputStream  (  final   java  .  io  .  OutputStream   out  )  { 
this  (  out  ,  Base64  .  ENCODE  )  ; 
} 


























public   OutputStream  (  final   java  .  io  .  OutputStream   out  ,  final   int   options  )  { 
super  (  out  )  ; 
this  .  breakLines  =  (  options  &  Base64  .  DONT_BREAK_LINES  )  !=  Base64  .  DONT_BREAK_LINES  ; 
this  .  encode  =  (  options  &  Base64  .  ENCODE  )  ==  Base64  .  ENCODE  ; 
this  .  bufferLength  =  this  .  encode  ?  3  :  4  ; 
this  .  buffer  =  new   byte  [  this  .  bufferLength  ]  ; 
this  .  position  =  0  ; 
this  .  lineLength  =  0  ; 
this  .  suspendEncoding  =  false  ; 
this  .  b4  =  new   byte  [  4  ]  ; 
this  .  options  =  options  ; 
this  .  alphabet  =  Base64  .  getAlphabet  (  options  )  ; 
this  .  decodabet  =  Base64  .  getDecodabet  (  options  )  ; 
} 






@  Override 
public   void   close  (  )  throws   java  .  io  .  IOException  { 
flushBase64  (  )  ; 
super  .  close  (  )  ; 
this  .  buffer  =  null  ; 
this  .  out  =  null  ; 
} 





public   void   flushBase64  (  )  throws   java  .  io  .  IOException  { 
if  (  this  .  position  >  0  )  { 
if  (  this  .  encode  )  { 
this  .  out  .  write  (  Base64  .  encode3to4  (  this  .  b4  ,  this  .  buffer  ,  this  .  position  ,  this  .  options  )  )  ; 
this  .  position  =  0  ; 
}  else  { 
throw   new   java  .  io  .  IOException  (  "Base64 input not properly padded."  )  ; 
} 
} 
} 







public   void   resumeEncoding  (  )  { 
this  .  suspendEncoding  =  false  ; 
} 







public   void   suspendEncoding  (  )  throws   java  .  io  .  IOException  { 
flushBase64  (  )  ; 
this  .  suspendEncoding  =  true  ; 
} 













@  Override 
public   void   write  (  final   byte  [  ]  theBytes  ,  final   int   off  ,  final   int   len  )  throws   java  .  io  .  IOException  { 
if  (  this  .  suspendEncoding  )  { 
super  .  out  .  write  (  theBytes  ,  off  ,  len  )  ; 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
write  (  theBytes  [  off  +  i  ]  )  ; 
} 
} 











@  Override 
public   void   write  (  final   int   theByte  )  throws   java  .  io  .  IOException  { 
if  (  this  .  suspendEncoding  )  { 
super  .  out  .  write  (  theByte  )  ; 
return  ; 
} 
if  (  this  .  encode  )  { 
this  .  buffer  [  this  .  position  ++  ]  =  (  byte  )  theByte  ; 
if  (  this  .  position  >=  this  .  bufferLength  )  { 
this  .  out  .  write  (  Base64  .  encode3to4  (  this  .  b4  ,  this  .  buffer  ,  this  .  bufferLength  ,  this  .  options  )  )  ; 
this  .  lineLength  +=  4  ; 
if  (  this  .  breakLines  &&  (  this  .  lineLength  >=  Base64  .  MAX_LINE_LENGTH  )  )  { 
this  .  out  .  write  (  Base64  .  NEW_LINE  )  ; 
this  .  lineLength  =  0  ; 
} 
this  .  position  =  0  ; 
} 
}  else  { 
if  (  this  .  decodabet  [  theByte  &  0x7f  ]  >  Base64  .  WHITE_SPACE_ENC  )  { 
this  .  buffer  [  this  .  position  ++  ]  =  (  byte  )  theByte  ; 
if  (  this  .  position  >=  this  .  bufferLength  )  { 
final   int   len  =  Base64  .  decode4to3  (  this  .  buffer  ,  0  ,  this  .  b4  ,  0  ,  this  .  options  )  ; 
this  .  out  .  write  (  this  .  b4  ,  0  ,  len  )  ; 
this  .  position  =  0  ; 
} 
}  else   if  (  this  .  decodabet  [  theByte  &  0x7f  ]  !=  Base64  .  WHITE_SPACE_ENC  )  { 
throw   new   java  .  io  .  IOException  (  "Invalid character in Base64 data."  )  ; 
} 
} 
} 
} 


public   static   final   int   NO_OPTIONS  =  0  ; 


public   static   final   int   ENCODE  =  1  ; 


public   static   final   int   DECODE  =  0  ; 


public   static   final   int   GZIP  =  2  ; 


public   static   final   int   DONT_BREAK_LINES  =  8  ; 










public   static   final   int   URL_SAFE  =  16  ; 






public   static   final   int   ORDERED  =  32  ; 


private   static   final   int   MAX_LINE_LENGTH  =  76  ; 


private   static   final   byte   EQUALS_SIGN  =  (  byte  )  '='  ; 


private   static   final   byte   NEW_LINE  =  (  byte  )  '\n'  ; 


private   static   final   String   PREFERRED_ENCODING  =  "UTF-8"  ; 

private   static   final   byte   WHITE_SPACE_ENC  =  -  5  ; 

private   static   final   byte   EQUALS_SIGN_ENC  =  -  1  ; 

private   static   final   byte  [  ]  _STANDARD_ALPHABET  =  {  (  byte  )  'A'  ,  (  byte  )  'B'  ,  (  byte  )  'C'  ,  (  byte  )  'D'  ,  (  byte  )  'E'  ,  (  byte  )  'F'  ,  (  byte  )  'G'  ,  (  byte  )  'H'  ,  (  byte  )  'I'  ,  (  byte  )  'J'  ,  (  byte  )  'K'  ,  (  byte  )  'L'  ,  (  byte  )  'M'  ,  (  byte  )  'N'  ,  (  byte  )  'O'  ,  (  byte  )  'P'  ,  (  byte  )  'Q'  ,  (  byte  )  'R'  ,  (  byte  )  'S'  ,  (  byte  )  'T'  ,  (  byte  )  'U'  ,  (  byte  )  'V'  ,  (  byte  )  'W'  ,  (  byte  )  'X'  ,  (  byte  )  'Y'  ,  (  byte  )  'Z'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  ,  (  byte  )  'g'  ,  (  byte  )  'h'  ,  (  byte  )  'i'  ,  (  byte  )  'j'  ,  (  byte  )  'k'  ,  (  byte  )  'l'  ,  (  byte  )  'm'  ,  (  byte  )  'n'  ,  (  byte  )  'o'  ,  (  byte  )  'p'  ,  (  byte  )  'q'  ,  (  byte  )  'r'  ,  (  byte  )  's'  ,  (  byte  )  't'  ,  (  byte  )  'u'  ,  (  byte  )  'v'  ,  (  byte  )  'w'  ,  (  byte  )  'x'  ,  (  byte  )  'y'  ,  (  byte  )  'z'  ,  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  '+'  ,  (  byte  )  '/'  }  ; 





private   static   final   byte  [  ]  _STANDARD_DECODABET  =  {  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  5  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  62  ,  -  9  ,  -  9  ,  -  9  ,  63  ,  52  ,  53  ,  54  ,  55  ,  56  ,  57  ,  58  ,  59  ,  60  ,  61  ,  -  9  ,  -  9  ,  -  9  ,  -  1  ,  -  9  ,  -  9  ,  -  9  ,  0  ,  1  ,  2  ,  3  ,  4  ,  5  ,  6  ,  7  ,  8  ,  9  ,  10  ,  11  ,  12  ,  13  ,  14  ,  15  ,  16  ,  17  ,  18  ,  19  ,  20  ,  21  ,  22  ,  23  ,  24  ,  25  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  26  ,  27  ,  28  ,  29  ,  30  ,  31  ,  32  ,  33  ,  34  ,  35  ,  36  ,  37  ,  38  ,  39  ,  40  ,  41  ,  42  ,  43  ,  44  ,  45  ,  46  ,  47  ,  48  ,  49  ,  50  ,  51  ,  -  9  ,  -  9  ,  -  9  ,  -  9  }  ; 








private   static   final   byte  [  ]  _URL_SAFE_ALPHABET  =  {  (  byte  )  'A'  ,  (  byte  )  'B'  ,  (  byte  )  'C'  ,  (  byte  )  'D'  ,  (  byte  )  'E'  ,  (  byte  )  'F'  ,  (  byte  )  'G'  ,  (  byte  )  'H'  ,  (  byte  )  'I'  ,  (  byte  )  'J'  ,  (  byte  )  'K'  ,  (  byte  )  'L'  ,  (  byte  )  'M'  ,  (  byte  )  'N'  ,  (  byte  )  'O'  ,  (  byte  )  'P'  ,  (  byte  )  'Q'  ,  (  byte  )  'R'  ,  (  byte  )  'S'  ,  (  byte  )  'T'  ,  (  byte  )  'U'  ,  (  byte  )  'V'  ,  (  byte  )  'W'  ,  (  byte  )  'X'  ,  (  byte  )  'Y'  ,  (  byte  )  'Z'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  ,  (  byte  )  'g'  ,  (  byte  )  'h'  ,  (  byte  )  'i'  ,  (  byte  )  'j'  ,  (  byte  )  'k'  ,  (  byte  )  'l'  ,  (  byte  )  'm'  ,  (  byte  )  'n'  ,  (  byte  )  'o'  ,  (  byte  )  'p'  ,  (  byte  )  'q'  ,  (  byte  )  'r'  ,  (  byte  )  's'  ,  (  byte  )  't'  ,  (  byte  )  'u'  ,  (  byte  )  'v'  ,  (  byte  )  'w'  ,  (  byte  )  'x'  ,  (  byte  )  'y'  ,  (  byte  )  'z'  ,  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  '-'  ,  (  byte  )  '_'  }  ; 




private   static   final   byte  [  ]  _URL_SAFE_DECODABET  =  {  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  5  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  62  ,  -  9  ,  -  9  ,  52  ,  53  ,  54  ,  55  ,  56  ,  57  ,  58  ,  59  ,  60  ,  61  ,  -  9  ,  -  9  ,  -  9  ,  -  1  ,  -  9  ,  -  9  ,  -  9  ,  0  ,  1  ,  2  ,  3  ,  4  ,  5  ,  6  ,  7  ,  8  ,  9  ,  10  ,  11  ,  12  ,  13  ,  14  ,  15  ,  16  ,  17  ,  18  ,  19  ,  20  ,  21  ,  22  ,  23  ,  24  ,  25  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  63  ,  -  9  ,  26  ,  27  ,  28  ,  29  ,  30  ,  31  ,  32  ,  33  ,  34  ,  35  ,  36  ,  37  ,  38  ,  39  ,  40  ,  41  ,  42  ,  43  ,  44  ,  45  ,  46  ,  47  ,  48  ,  49  ,  50  ,  51  ,  -  9  ,  -  9  ,  -  9  ,  -  9  }  ; 






private   static   final   byte  [  ]  _ORDERED_ALPHABET  =  {  (  byte  )  '-'  ,  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  'A'  ,  (  byte  )  'B'  ,  (  byte  )  'C'  ,  (  byte  )  'D'  ,  (  byte  )  'E'  ,  (  byte  )  'F'  ,  (  byte  )  'G'  ,  (  byte  )  'H'  ,  (  byte  )  'I'  ,  (  byte  )  'J'  ,  (  byte  )  'K'  ,  (  byte  )  'L'  ,  (  byte  )  'M'  ,  (  byte  )  'N'  ,  (  byte  )  'O'  ,  (  byte  )  'P'  ,  (  byte  )  'Q'  ,  (  byte  )  'R'  ,  (  byte  )  'S'  ,  (  byte  )  'T'  ,  (  byte  )  'U'  ,  (  byte  )  'V'  ,  (  byte  )  'W'  ,  (  byte  )  'X'  ,  (  byte  )  'Y'  ,  (  byte  )  'Z'  ,  (  byte  )  '_'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  ,  (  byte  )  'g'  ,  (  byte  )  'h'  ,  (  byte  )  'i'  ,  (  byte  )  'j'  ,  (  byte  )  'k'  ,  (  byte  )  'l'  ,  (  byte  )  'm'  ,  (  byte  )  'n'  ,  (  byte  )  'o'  ,  (  byte  )  'p'  ,  (  byte  )  'q'  ,  (  byte  )  'r'  ,  (  byte  )  's'  ,  (  byte  )  't'  ,  (  byte  )  'u'  ,  (  byte  )  'v'  ,  (  byte  )  'w'  ,  (  byte  )  'x'  ,  (  byte  )  'y'  ,  (  byte  )  'z'  }  ; 




private   static   final   byte  [  ]  _ORDERED_DECODABET  =  {  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  5  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  5  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  0  ,  -  9  ,  -  9  ,  1  ,  2  ,  3  ,  4  ,  5  ,  6  ,  7  ,  8  ,  9  ,  10  ,  -  9  ,  -  9  ,  -  9  ,  -  1  ,  -  9  ,  -  9  ,  -  9  ,  11  ,  12  ,  13  ,  14  ,  15  ,  16  ,  17  ,  18  ,  19  ,  20  ,  21  ,  22  ,  23  ,  24  ,  25  ,  26  ,  27  ,  28  ,  29  ,  30  ,  31  ,  32  ,  33  ,  34  ,  35  ,  36  ,  -  9  ,  -  9  ,  -  9  ,  -  9  ,  37  ,  -  9  ,  38  ,  39  ,  40  ,  41  ,  42  ,  43  ,  44  ,  45  ,  46  ,  47  ,  48  ,  49  ,  50  ,  51  ,  52  ,  53  ,  54  ,  55  ,  56  ,  57  ,  58  ,  59  ,  60  ,  61  ,  62  ,  63  ,  -  9  ,  -  9  ,  -  9  ,  -  9  }  ; 















public   static   byte  [  ]  decode  (  final   byte  [  ]  source  ,  final   int   off  ,  final   int   len  ,  final   int   options  )  { 
final   byte  [  ]  DECODABET  =  Base64  .  getDecodabet  (  options  )  ; 
final   int   len34  =  len  *  3  /  4  ; 
final   byte  [  ]  outBuff  =  new   byte  [  len34  ]  ; 
int   outBuffPosn  =  0  ; 
final   byte  [  ]  b4  =  new   byte  [  4  ]  ; 
int   b4Posn  =  0  ; 
int   i  =  0  ; 
byte   sbiCrop  =  0  ; 
byte   sbiDecode  =  0  ; 
for  (  i  =  off  ;  i  <  off  +  len  ;  i  ++  )  { 
sbiCrop  =  (  byte  )  (  source  [  i  ]  &  0x7f  )  ; 
sbiDecode  =  DECODABET  [  sbiCrop  ]  ; 
if  (  sbiDecode  >=  Base64  .  WHITE_SPACE_ENC  )  { 
if  (  sbiDecode  >=  Base64  .  EQUALS_SIGN_ENC  )  { 
b4  [  b4Posn  ++  ]  =  sbiCrop  ; 
if  (  b4Posn  >  3  )  { 
outBuffPosn  +=  Base64  .  decode4to3  (  b4  ,  0  ,  outBuff  ,  outBuffPosn  ,  options  )  ; 
b4Posn  =  0  ; 
if  (  sbiCrop  ==  Base64  .  EQUALS_SIGN  )  { 
break  ; 
} 
} 
} 
}  else  { 
System  .  err  .  println  (  "Bad Base64 input character at "  +  i  +  ": "  +  source  [  i  ]  +  "(decimal)"  )  ; 
return   null  ; 
} 
} 
final   byte  [  ]  out  =  new   byte  [  outBuffPosn  ]  ; 
System  .  arraycopy  (  outBuff  ,  0  ,  out  ,  0  ,  outBuffPosn  )  ; 
return   out  ; 
} 










public   static   byte  [  ]  decode  (  final   String   s  )  { 
return   Base64  .  decode  (  s  ,  Base64  .  NO_OPTIONS  )  ; 
} 












public   static   byte  [  ]  decode  (  final   String   s  ,  final   int   options  )  { 
byte  [  ]  bytes  ; 
try  { 
bytes  =  s  .  getBytes  (  Base64  .  PREFERRED_ENCODING  )  ; 
}  catch  (  final   java  .  io  .  UnsupportedEncodingException   uee  )  { 
bytes  =  s  .  getBytes  (  )  ; 
} 
bytes  =  Base64  .  decode  (  bytes  ,  0  ,  bytes  .  length  ,  options  )  ; 
if  (  (  bytes  !=  null  )  &&  (  bytes  .  length  >=  4  )  )  { 
final   int   head  =  (  bytes  [  0  ]  &  0xff  )  |  (  (  bytes  [  1  ]  <<  8  )  &  0xff00  )  ; 
if  (  java  .  util  .  zip  .  GZIPInputStream  .  GZIP_MAGIC  ==  head  )  { 
java  .  io  .  ByteArrayInputStream   bais  =  null  ; 
java  .  util  .  zip  .  GZIPInputStream   gzis  =  null  ; 
java  .  io  .  ByteArrayOutputStream   baos  =  null  ; 
final   byte  [  ]  buffer  =  new   byte  [  2048  ]  ; 
int   length  =  0  ; 
try  { 
baos  =  new   java  .  io  .  ByteArrayOutputStream  (  )  ; 
bais  =  new   java  .  io  .  ByteArrayInputStream  (  bytes  )  ; 
gzis  =  new   java  .  util  .  zip  .  GZIPInputStream  (  bais  )  ; 
while  (  (  length  =  gzis  .  read  (  buffer  )  )  >=  0  )  { 
baos  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
bytes  =  baos  .  toByteArray  (  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
}  finally  { 
try  { 
baos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
gzis  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
bais  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
} 
} 
return   bytes  ; 
} 































private   static   int   decode4to3  (  final   byte  [  ]  source  ,  final   int   srcOffset  ,  final   byte  [  ]  destination  ,  final   int   destOffset  ,  final   int   options  )  { 
final   byte  [  ]  DECODABET  =  Base64  .  getDecodabet  (  options  )  ; 
if  (  source  [  srcOffset  +  2  ]  ==  Base64  .  EQUALS_SIGN  )  { 
final   int   outBuff  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outBuff  >  >  >  16  )  ; 
return   1  ; 
}  else   if  (  source  [  srcOffset  +  3  ]  ==  Base64  .  EQUALS_SIGN  )  { 
final   int   outBuff  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  |  (  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  &  0xFF  )  <<  6  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outBuff  >  >  >  16  )  ; 
destination  [  destOffset  +  1  ]  =  (  byte  )  (  outBuff  >  >  >  8  )  ; 
return   2  ; 
}  else  { 
try  { 
final   int   outBuff  =  (  (  DECODABET  [  source  [  srcOffset  ]  ]  &  0xFF  )  <<  18  )  |  (  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  &  0xFF  )  <<  12  )  |  (  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  &  0xFF  )  <<  6  )  |  (  (  DECODABET  [  source  [  srcOffset  +  3  ]  ]  &  0xFF  )  )  ; 
destination  [  destOffset  ]  =  (  byte  )  (  outBuff  >  >  16  )  ; 
destination  [  destOffset  +  1  ]  =  (  byte  )  (  outBuff  >  >  8  )  ; 
destination  [  destOffset  +  2  ]  =  (  byte  )  (  outBuff  )  ; 
return   3  ; 
}  catch  (  final   Exception   e  )  { 
System  .  out  .  println  (  ""  +  source  [  srcOffset  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  1  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  1  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  2  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  2  ]  ]  )  )  ; 
System  .  out  .  println  (  ""  +  source  [  srcOffset  +  3  ]  +  ": "  +  (  DECODABET  [  source  [  srcOffset  +  3  ]  ]  )  )  ; 
return  -  1  ; 
} 
} 
} 











public   static   boolean   decodeFileToFile  (  final   String   infile  ,  final   String   outfile  )  { 
boolean   success  =  false  ; 
java  .  io  .  InputStream   in  =  null  ; 
java  .  io  .  OutputStream   out  =  null  ; 
try  { 
in  =  new   Base64  .  InputStream  (  new   java  .  io  .  BufferedInputStream  (  new   java  .  io  .  FileInputStream  (  infile  )  )  ,  Base64  .  DECODE  )  ; 
out  =  new   java  .  io  .  BufferedOutputStream  (  new   java  .  io  .  FileOutputStream  (  outfile  )  )  ; 
final   byte  [  ]  buffer  =  new   byte  [  65536  ]  ; 
int   read  =  -  1  ; 
while  (  (  read  =  in  .  read  (  buffer  )  )  >=  0  )  { 
out  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
success  =  true  ; 
}  catch  (  final   java  .  io  .  IOException   exc  )  { 
exc  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  final   Exception   exc  )  { 
} 
try  { 
out  .  close  (  )  ; 
}  catch  (  final   Exception   exc  )  { 
} 
} 
return   success  ; 
} 










public   static   byte  [  ]  decodeFromFile  (  final   String   filename  )  { 
byte  [  ]  decodedData  =  null  ; 
Base64  .  InputStream   bis  =  null  ; 
try  { 
final   java  .  io  .  File   file  =  new   java  .  io  .  File  (  filename  )  ; 
byte  [  ]  buffer  =  null  ; 
int   length  =  0  ; 
int   numBytes  =  0  ; 
if  (  file  .  length  (  )  >  Integer  .  MAX_VALUE  )  { 
System  .  err  .  println  (  "File is too big for this convenience method ("  +  file  .  length  (  )  +  " bytes)."  )  ; 
return   null  ; 
} 
buffer  =  new   byte  [  (  int  )  file  .  length  (  )  ]  ; 
bis  =  new   Base64  .  InputStream  (  new   java  .  io  .  BufferedInputStream  (  new   java  .  io  .  FileInputStream  (  file  )  )  ,  Base64  .  DECODE  )  ; 
while  (  (  numBytes  =  bis  .  read  (  buffer  ,  length  ,  4096  )  )  >=  0  )  { 
length  +=  numBytes  ; 
} 
decodedData  =  new   byte  [  length  ]  ; 
System  .  arraycopy  (  buffer  ,  0  ,  decodedData  ,  0  ,  length  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
System  .  err  .  println  (  "Error decoding from file "  +  filename  )  ; 
}  finally  { 
try  { 
bis  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
return   decodedData  ; 
} 












public   static   boolean   decodeToFile  (  final   String   dataToDecode  ,  final   String   filename  )  { 
boolean   success  =  false  ; 
Base64  .  OutputStream   bos  =  null  ; 
try  { 
bos  =  new   Base64  .  OutputStream  (  new   java  .  io  .  FileOutputStream  (  filename  )  ,  Base64  .  DECODE  )  ; 
bos  .  write  (  dataToDecode  .  getBytes  (  Base64  .  PREFERRED_ENCODING  )  )  ; 
success  =  true  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
success  =  false  ; 
}  finally  { 
try  { 
bos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
return   success  ; 
} 










public   static   Object   decodeToObject  (  final   String   encodedObject  )  { 
final   byte  [  ]  objBytes  =  Base64  .  decode  (  encodedObject  )  ; 
java  .  io  .  ByteArrayInputStream   bais  =  null  ; 
java  .  io  .  ObjectInputStream   ois  =  null  ; 
Object   obj  =  null  ; 
try  { 
bais  =  new   java  .  io  .  ByteArrayInputStream  (  objBytes  )  ; 
ois  =  new   java  .  io  .  ObjectInputStream  (  bais  )  ; 
obj  =  ois  .  readObject  (  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  final   java  .  lang  .  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
bais  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
ois  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
return   obj  ; 
} 


















private   static   byte  [  ]  encode3to4  (  final   byte  [  ]  b4  ,  final   byte  [  ]  threeBytes  ,  final   int   numSigBytes  ,  final   int   options  )  { 
Base64  .  encode3to4  (  threeBytes  ,  0  ,  numSigBytes  ,  b4  ,  0  ,  options  )  ; 
return   b4  ; 
} 































private   static   byte  [  ]  encode3to4  (  final   byte  [  ]  source  ,  final   int   srcOffset  ,  final   int   numSigBytes  ,  final   byte  [  ]  destination  ,  final   int   destOffset  ,  final   int   options  )  { 
final   byte  [  ]  ALPHABET  =  Base64  .  getAlphabet  (  options  )  ; 
final   int   inBuff  =  (  numSigBytes  >  0  ?  (  (  source  [  srcOffset  ]  <<  24  )  >  >  >  8  )  :  0  )  |  (  numSigBytes  >  1  ?  (  (  source  [  srcOffset  +  1  ]  <<  24  )  >  >  >  16  )  :  0  )  |  (  numSigBytes  >  2  ?  (  (  source  [  srcOffset  +  2  ]  <<  24  )  >  >  >  24  )  :  0  )  ; 
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
destination  [  destOffset  +  3  ]  =  Base64  .  EQUALS_SIGN  ; 
return   destination  ; 
case   1  : 
destination  [  destOffset  ]  =  ALPHABET  [  (  inBuff  >  >  >  18  )  ]  ; 
destination  [  destOffset  +  1  ]  =  ALPHABET  [  (  inBuff  >  >  >  12  )  &  0x3f  ]  ; 
destination  [  destOffset  +  2  ]  =  Base64  .  EQUALS_SIGN  ; 
destination  [  destOffset  +  3  ]  =  Base64  .  EQUALS_SIGN  ; 
return   destination  ; 
default  : 
return   destination  ; 
} 
} 








public   static   String   encodeBytes  (  final   byte  [  ]  source  )  { 
return   Base64  .  encodeBytes  (  source  ,  0  ,  source  .  length  ,  Base64  .  NO_OPTIONS  )  ; 
} 


























public   static   String   encodeBytes  (  final   byte  [  ]  source  ,  final   int   options  )  { 
return   Base64  .  encodeBytes  (  source  ,  0  ,  source  .  length  ,  options  )  ; 
} 












public   static   String   encodeBytes  (  final   byte  [  ]  source  ,  final   int   off  ,  final   int   len  )  { 
return   Base64  .  encodeBytes  (  source  ,  off  ,  len  ,  Base64  .  NO_OPTIONS  )  ; 
} 

































public   static   String   encodeBytes  (  final   byte  [  ]  source  ,  final   int   off  ,  final   int   len  ,  final   int   options  )  { 
final   int   dontBreakLines  =  (  options  &  Base64  .  DONT_BREAK_LINES  )  ; 
final   int   gzip  =  (  options  &  Base64  .  GZIP  )  ; 
if  (  gzip  ==  Base64  .  GZIP  )  { 
java  .  io  .  ByteArrayOutputStream   baos  =  null  ; 
java  .  util  .  zip  .  GZIPOutputStream   gzos  =  null  ; 
Base64  .  OutputStream   b64os  =  null  ; 
try  { 
baos  =  new   java  .  io  .  ByteArrayOutputStream  (  )  ; 
b64os  =  new   Base64  .  OutputStream  (  baos  ,  Base64  .  ENCODE  |  options  )  ; 
gzos  =  new   java  .  util  .  zip  .  GZIPOutputStream  (  b64os  )  ; 
gzos  .  write  (  source  ,  off  ,  len  )  ; 
gzos  .  close  (  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
}  finally  { 
try  { 
gzos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
b64os  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
baos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
try  { 
return   new   String  (  baos  .  toByteArray  (  )  ,  Base64  .  PREFERRED_ENCODING  )  ; 
}  catch  (  final   java  .  io  .  UnsupportedEncodingException   uue  )  { 
return   new   String  (  baos  .  toByteArray  (  )  )  ; 
} 
}  else  { 
final   boolean   breakLines  =  dontBreakLines  ==  0  ; 
final   int   len43  =  len  *  4  /  3  ; 
final   byte  [  ]  outBuff  =  new   byte  [  (  len43  )  +  (  (  len  %  3  )  >  0  ?  4  :  0  )  +  (  breakLines  ?  (  len43  /  Base64  .  MAX_LINE_LENGTH  )  :  0  )  ]  ; 
int   d  =  0  ; 
int   e  =  0  ; 
final   int   len2  =  len  -  2  ; 
int   lineLength  =  0  ; 
for  (  ;  d  <  len2  ;  d  +=  3  ,  e  +=  4  )  { 
Base64  .  encode3to4  (  source  ,  d  +  off  ,  3  ,  outBuff  ,  e  ,  options  )  ; 
lineLength  +=  4  ; 
if  (  breakLines  &&  (  lineLength  ==  Base64  .  MAX_LINE_LENGTH  )  )  { 
outBuff  [  e  +  4  ]  =  Base64  .  NEW_LINE  ; 
e  ++  ; 
lineLength  =  0  ; 
} 
} 
if  (  d  <  len  )  { 
Base64  .  encode3to4  (  source  ,  d  +  off  ,  len  -  d  ,  outBuff  ,  e  ,  options  )  ; 
e  +=  4  ; 
} 
try  { 
return   new   String  (  outBuff  ,  0  ,  e  ,  Base64  .  PREFERRED_ENCODING  )  ; 
}  catch  (  final   java  .  io  .  UnsupportedEncodingException   uue  )  { 
return   new   String  (  outBuff  ,  0  ,  e  )  ; 
} 
} 
} 











public   static   boolean   encodeFileToFile  (  final   String   infile  ,  final   String   outfile  )  { 
boolean   success  =  false  ; 
java  .  io  .  InputStream   in  =  null  ; 
java  .  io  .  OutputStream   out  =  null  ; 
try  { 
in  =  new   Base64  .  InputStream  (  new   java  .  io  .  BufferedInputStream  (  new   java  .  io  .  FileInputStream  (  infile  )  )  ,  Base64  .  ENCODE  )  ; 
out  =  new   java  .  io  .  BufferedOutputStream  (  new   java  .  io  .  FileOutputStream  (  outfile  )  )  ; 
final   byte  [  ]  buffer  =  new   byte  [  65536  ]  ; 
int   read  =  -  1  ; 
while  (  (  read  =  in  .  read  (  buffer  )  )  >=  0  )  { 
out  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
success  =  true  ; 
}  catch  (  final   java  .  io  .  IOException   exc  )  { 
exc  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  final   Exception   exc  )  { 
} 
try  { 
out  .  close  (  )  ; 
}  catch  (  final   Exception   exc  )  { 
} 
} 
return   success  ; 
} 










public   static   String   encodeFromFile  (  final   String   filename  )  { 
String   encodedData  =  null  ; 
Base64  .  InputStream   bis  =  null  ; 
try  { 
final   java  .  io  .  File   file  =  new   java  .  io  .  File  (  filename  )  ; 
final   byte  [  ]  buffer  =  new   byte  [  Math  .  max  (  (  int  )  (  file  .  length  (  )  *  1.4  )  ,  40  )  ]  ; 
int   length  =  0  ; 
int   numBytes  =  0  ; 
bis  =  new   Base64  .  InputStream  (  new   java  .  io  .  BufferedInputStream  (  new   java  .  io  .  FileInputStream  (  file  )  )  ,  Base64  .  ENCODE  )  ; 
while  (  (  numBytes  =  bis  .  read  (  buffer  ,  length  ,  4096  )  )  >=  0  )  { 
length  +=  numBytes  ; 
} 
encodedData  =  new   String  (  buffer  ,  0  ,  length  ,  Base64  .  PREFERRED_ENCODING  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
System  .  err  .  println  (  "Error encoding from file "  +  filename  )  ; 
}  finally  { 
try  { 
bis  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
return   encodedData  ; 
} 












public   static   String   encodeObject  (  final   java  .  io  .  Serializable   serializableObject  )  { 
return   Base64  .  encodeObject  (  serializableObject  ,  Base64  .  NO_OPTIONS  )  ; 
} 




























public   static   String   encodeObject  (  final   java  .  io  .  Serializable   serializableObject  ,  final   int   options  )  { 
java  .  io  .  ByteArrayOutputStream   baos  =  null  ; 
java  .  io  .  OutputStream   b64os  =  null  ; 
java  .  io  .  ObjectOutputStream   oos  =  null  ; 
java  .  util  .  zip  .  GZIPOutputStream   gzos  =  null  ; 
final   int   gzip  =  (  options  &  Base64  .  GZIP  )  ; 
final   int   dontBreakLines  =  (  options  &  Base64  .  DONT_BREAK_LINES  )  ; 
try  { 
baos  =  new   java  .  io  .  ByteArrayOutputStream  (  )  ; 
b64os  =  new   Base64  .  OutputStream  (  baos  ,  Base64  .  ENCODE  |  options  )  ; 
if  (  gzip  ==  Base64  .  GZIP  )  { 
gzos  =  new   java  .  util  .  zip  .  GZIPOutputStream  (  b64os  )  ; 
oos  =  new   java  .  io  .  ObjectOutputStream  (  gzos  )  ; 
}  else  { 
oos  =  new   java  .  io  .  ObjectOutputStream  (  b64os  )  ; 
} 
oos  .  writeObject  (  serializableObject  )  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
}  finally  { 
try  { 
oos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
gzos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
b64os  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
try  { 
baos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
try  { 
return   new   String  (  baos  .  toByteArray  (  )  ,  Base64  .  PREFERRED_ENCODING  )  ; 
}  catch  (  final   java  .  io  .  UnsupportedEncodingException   uue  )  { 
return   new   String  (  baos  .  toByteArray  (  )  )  ; 
} 
} 












public   static   boolean   encodeToFile  (  final   byte  [  ]  dataToEncode  ,  final   String   filename  )  { 
boolean   success  =  false  ; 
Base64  .  OutputStream   bos  =  null  ; 
try  { 
bos  =  new   Base64  .  OutputStream  (  new   java  .  io  .  FileOutputStream  (  filename  )  ,  Base64  .  ENCODE  )  ; 
bos  .  write  (  dataToEncode  )  ; 
success  =  true  ; 
}  catch  (  final   java  .  io  .  IOException   e  )  { 
success  =  false  ; 
}  finally  { 
try  { 
bos  .  close  (  )  ; 
}  catch  (  final   Exception   e  )  { 
} 
} 
return   success  ; 
} 







private   static   final   byte  [  ]  getAlphabet  (  final   int   options  )  { 
if  (  (  options  &  Base64  .  URL_SAFE  )  ==  Base64  .  URL_SAFE  )  { 
return   Base64  .  _URL_SAFE_ALPHABET  ; 
}  else   if  (  (  options  &  Base64  .  ORDERED  )  ==  Base64  .  ORDERED  )  { 
return   Base64  .  _ORDERED_ALPHABET  ; 
}  else  { 
return   Base64  .  _STANDARD_ALPHABET  ; 
} 
} 







private   static   final   byte  [  ]  getDecodabet  (  final   int   options  )  { 
if  (  (  options  &  Base64  .  URL_SAFE  )  ==  Base64  .  URL_SAFE  )  { 
return   Base64  .  _URL_SAFE_DECODABET  ; 
}  else   if  (  (  options  &  Base64  .  ORDERED  )  ==  Base64  .  ORDERED  )  { 
return   Base64  .  _ORDERED_DECODABET  ; 
}  else  { 
return   Base64  .  _STANDARD_DECODABET  ; 
} 
} 






public   static   final   void   main  (  final   String  [  ]  args  )  { 
if  (  args  .  length  <  3  )  { 
Base64  .  usage  (  "Not enough arguments."  )  ; 
}  else  { 
final   String   flag  =  args  [  0  ]  ; 
final   String   infile  =  args  [  1  ]  ; 
final   String   outfile  =  args  [  2  ]  ; 
if  (  flag  .  equals  (  "-e"  )  )  { 
Base64  .  encodeFileToFile  (  infile  ,  outfile  )  ; 
}  else   if  (  flag  .  equals  (  "-d"  )  )  { 
Base64  .  decodeFileToFile  (  infile  ,  outfile  )  ; 
}  else  { 
Base64  .  usage  (  "Unknown flag: "  +  flag  )  ; 
} 
} 
} 







private   static   final   void   usage  (  final   String   msg  )  { 
System  .  err  .  println  (  msg  )  ; 
System  .  err  .  println  (  "Usage: java Base64 -e|-d inputfile outputfile"  )  ; 
} 


private   Base64  (  )  { 
} 
} 

