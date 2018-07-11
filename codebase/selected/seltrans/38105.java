package   com  .  ars_eclectica  .  traceview  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 










public   class   SCFFile   implements   TraceFile  { 

private   SCFHeader   header  ; 

private   int  [  ]  A  ; 

private   int  [  ]  C  ; 

private   int  [  ]  G  ; 

private   int  [  ]  T  ; 

private   int   max  ; 

private   String   sequence  ; 

private   int  [  ]  centers  ; 











public   SCFFile  (  File   file  )  throws   IOException  ,  SecurityException  { 
header  =  new   SCFHeader  (  new   FileInputStream  (  file  )  )  ; 
if  (  !  header  .  magicNumber  .  equals  (  ".scf"  )  )  throw   new   RuntimeException  (  file  +  " is not an SCF file"  )  ; 
RandomAccessFile   f  =  null  ; 
try  { 
f  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
A  =  new   int  [  header  .  samples  ]  ; 
C  =  new   int  [  header  .  samples  ]  ; 
G  =  new   int  [  header  .  samples  ]  ; 
T  =  new   int  [  header  .  samples  ]  ; 
max  =  Integer  .  MIN_VALUE  ; 
f  .  seek  (  header  .  samplesOffset  )  ; 
if  (  header  .  sampleSize  ==  1  )  { 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
C  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
G  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
T  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
C  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
G  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
T  [  i  ]  =  f  .  readUnsignedByte  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
} 
}  else   if  (  header  .  sampleSize  ==  2  )  { 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
C  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
G  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
T  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
C  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
G  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
T  [  i  ]  =  f  .  readUnsignedShort  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
} 
} 
centers  =  new   int  [  header  .  bases  ]  ; 
byte  [  ]  buf  =  new   byte  [  header  .  bases  ]  ; 
f  .  seek  (  header  .  basesOffset  )  ; 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  bases  ;  ++  i  )  { 
centers  [  i  ]  =  f  .  readInt  (  )  ; 
f  .  skipBytes  (  4  )  ; 
buf  [  i  ]  =  f  .  readByte  (  )  ; 
f  .  skipBytes  (  3  )  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  bases  ;  ++  i  )  centers  [  i  ]  =  f  .  readInt  (  )  ; 
f  .  skipBytes  (  4  *  header  .  bases  )  ; 
f  .  readFully  (  buf  )  ; 
} 
sequence  =  new   String  (  buf  )  ; 
f  .  close  (  )  ; 
} 











public   SCFFile  (  URL   url  )  throws   IOException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
byte  [  ]  content  =  new   byte  [  connection  .  getContentLength  (  )  ]  ; 
DataInputStream   dis  =  new   DataInputStream  (  connection  .  getInputStream  (  )  )  ; 
dis  .  readFully  (  content  )  ; 
dis  .  close  (  )  ; 
dis  =  new   DataInputStream  (  new   ByteArrayInputStream  (  content  )  )  ; 
header  =  new   SCFHeader  (  dis  )  ; 
if  (  !  header  .  magicNumber  .  equals  (  ".scf"  )  )  throw   new   RuntimeException  (  url  +  " is not an SCF file"  )  ; 
A  =  new   int  [  header  .  samples  ]  ; 
C  =  new   int  [  header  .  samples  ]  ; 
G  =  new   int  [  header  .  samples  ]  ; 
T  =  new   int  [  header  .  samples  ]  ; 
max  =  Integer  .  MIN_VALUE  ; 
dis  .  reset  (  )  ; 
dis  .  skipBytes  (  header  .  samplesOffset  )  ; 
if  (  header  .  sampleSize  ==  1  )  { 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
C  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
G  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
T  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
C  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
G  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
T  [  i  ]  =  dis  .  readUnsignedByte  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
} 
}  else   if  (  header  .  sampleSize  ==  2  )  { 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
C  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
G  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
T  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
A  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  A  [  i  ]  >  max  )  max  =  A  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
C  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  C  [  i  ]  >  max  )  max  =  C  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
G  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  G  [  i  ]  >  max  )  max  =  G  [  i  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  header  .  samples  ;  ++  i  )  { 
T  [  i  ]  =  dis  .  readUnsignedShort  (  )  ; 
if  (  T  [  i  ]  >  max  )  max  =  T  [  i  ]  ; 
} 
} 
} 
centers  =  new   int  [  header  .  bases  ]  ; 
byte  [  ]  buf  =  new   byte  [  header  .  bases  ]  ; 
dis  .  reset  (  )  ; 
dis  .  skipBytes  (  header  .  basesOffset  )  ; 
if  (  header  .  version  <  3.00  )  { 
for  (  int   i  =  0  ;  i  <  header  .  bases  ;  ++  i  )  { 
centers  [  i  ]  =  dis  .  readInt  (  )  ; 
dis  .  skipBytes  (  4  )  ; 
buf  [  i  ]  =  dis  .  readByte  (  )  ; 
dis  .  skipBytes  (  3  )  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  header  .  bases  ;  ++  i  )  centers  [  i  ]  =  dis  .  readInt  (  )  ; 
dis  .  skipBytes  (  4  *  header  .  bases  )  ; 
dis  .  readFully  (  buf  )  ; 
} 
sequence  =  new   String  (  buf  )  ; 
dis  .  close  (  )  ; 
} 








private   int  [  ]  traceArray  (  char   c  )  { 
switch  (  Character  .  toUpperCase  (  c  )  )  { 
case  'A'  : 
return   A  ; 
case  'C'  : 
return   C  ; 
case  'G'  : 
return   G  ; 
case  'T'  : 
return   T  ; 
default  : 
return   null  ; 
} 
} 

public   int   traceAt  (  char   nucleotide  ,  int   pos  )  { 
return   traceArray  (  nucleotide  )  [  pos  ]  ; 
} 

public   char   baseAt  (  int   pos  )  { 
return   sequence  .  charAt  (  pos  )  ; 
} 

public   int   centerOf  (  int   pos  )  { 
return   centers  [  pos  ]  ; 
} 

public   String   getSequence  (  )  { 
return   sequence  ; 
} 

public   int   getTraceLength  (  )  { 
return   header  .  samples  ; 
} 

public   int   getSequenceLength  (  )  { 
return   header  .  bases  ; 
} 

public   int   getHighestPeak  (  )  { 
return   max  ; 
} 
} 








class   SCFHeader  { 

String   magicNumber  ; 

int   samples  ; 

int   samplesOffset  ; 

int   bases  ; 

int   basesLeftClip  ; 

int   basesRightClip  ; 

int   basesOffset  ; 

int   commentsSize  ; 

int   commentsOffset  ; 

float   version  ; 

int   sampleSize  ; 

int   codeSet  ; 

int   privateSize  ; 

int   privateOffset  ; 









public   SCFHeader  (  InputStream   is  )  throws   IOException  { 
DataInputStream   dis  =  new   DataInputStream  (  is  )  ; 
byte  [  ]  buf  =  new   byte  [  4  ]  ; 
dis  .  readFully  (  buf  )  ; 
magicNumber  =  new   String  (  buf  )  ; 
System  .  err  .  println  (  "MAGIC NUMBER: "  +  magicNumber  )  ; 
samples  =  dis  .  readInt  (  )  ; 
samplesOffset  =  dis  .  readInt  (  )  ; 
bases  =  dis  .  readInt  (  )  ; 
basesLeftClip  =  dis  .  readInt  (  )  ; 
basesRightClip  =  dis  .  readInt  (  )  ; 
basesOffset  =  dis  .  readInt  (  )  ; 
commentsSize  =  dis  .  readInt  (  )  ; 
commentsOffset  =  dis  .  readInt  (  )  ; 
dis  .  readFully  (  buf  )  ; 
version  =  Float  .  parseFloat  (  new   String  (  buf  )  )  ; 
sampleSize  =  dis  .  readInt  (  )  ; 
codeSet  =  dis  .  readInt  (  )  ; 
privateSize  =  dis  .  readInt  (  )  ; 
privateOffset  =  dis  .  readInt  (  )  ; 
if  (  version  <  2.00  )  sampleSize  =  1  ; 
} 







public   String   toString  (  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "magic number\t"  )  ; 
buf  .  append  (  magicNumber  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "number of sample points\t"  )  ; 
buf  .  append  (  samples  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "offset of sample data\t"  )  ; 
buf  .  append  (  samplesOffset  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "sequence length\t"  )  ; 
buf  .  append  (  bases  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "bases left clip\t"  )  ; 
buf  .  append  (  basesLeftClip  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "bases right clip\t"  )  ; 
buf  .  append  (  basesRightClip  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "offset of sequence data\t"  )  ; 
buf  .  append  (  basesOffset  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "length of comments data\t"  )  ; 
buf  .  append  (  commentsSize  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "offset of comments data\t"  )  ; 
buf  .  append  (  commentsOffset  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "version\t"  )  ; 
buf  .  append  (  version  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "precision of sample data\t"  )  ; 
buf  .  append  (  sampleSize  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "code set\t"  )  ; 
buf  .  append  (  codeSet  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "length of private data\t"  )  ; 
buf  .  append  (  privateSize  )  ; 
buf  .  append  (  "\n"  )  ; 
buf  .  append  (  "offset of private data\t"  )  ; 
buf  .  append  (  privateOffset  )  ; 
buf  .  append  (  "\n"  )  ; 
return   buf  .  toString  (  )  ; 
} 
} 

