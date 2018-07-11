package   org  .  jcvi  .  io  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  Closeable  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  sql  .  Connection  ; 
import   java  .  sql  .  ResultSet  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  sql  .  Statement  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Scanner  ; 
import   org  .  jcvi  .  common  .  util  .  Range  ; 

public   final   class   IOUtil  { 

public   enum   ENDIAN  { 

BIG  ,  LITTLE 
} 

private   IOUtil  (  )  { 
} 








public   static   void   recursiveDelete  (  File   file  )  throws   IOException  { 
if  (  file  .  exists  (  )  )  { 
if  (  file  .  isDirectory  (  )  )  { 
for  (  File   subfile  :  file  .  listFiles  (  )  )  { 
recursiveDelete  (  subfile  )  ; 
} 
} 
delete  (  file  )  ; 
} 
} 










public   static   void   delete  (  File   file  )  throws   IOException  { 
if  (  !  file  .  delete  (  )  )  { 
throw   new   IOException  (  "unable to delete "  +  file  )  ; 
} 
} 

public   static   void   deleteIgnoreError  (  File   file  )  { 
file  .  delete  (  )  ; 
} 










public   static   void   mkdirs  (  File   dir  )  throws   IOException  { 
if  (  !  dir  .  mkdirs  (  )  )  { 
throw   new   IOException  (  "unable to mkdirs for "  +  dir  )  ; 
} 
} 









public   static   void   mkdir  (  File   dir  )  throws   IOException  { 
if  (  !  dir  .  mkdir  (  )  )  { 
throw   new   IOException  (  "unable to mkdir for "  +  dir  )  ; 
} 
} 

















public   static   void   writeToOutputStream  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
writeToOutputStream  (  in  ,  out  ,  1024  )  ; 
} 












public   static   void   writeToOutputStream  (  InputStream   in  ,  OutputStream   out  ,  int   bufferSize  )  throws   IOException  { 
if  (  bufferSize  <  1  )  { 
throw   new   IllegalArgumentException  (  "can not have a 0 or negative bufferSize"  )  ; 
} 
byte  [  ]  buf  =  new   byte  [  bufferSize  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
out  .  flush  (  )  ; 
} 
} 






public   static   void   closeAndIgnoreErrors  (  Closeable   closeable  )  { 
try  { 
if  (  closeable  !=  null  )  { 
closeable  .  close  (  )  ; 
} 
}  catch  (  IOException   ignore  )  { 
} 
} 








public   static   void   closeAndIgnoreErrors  (  Closeable  ...  closeables  )  { 
for  (  Closeable   closeable  :  closeables  )  { 
closeAndIgnoreErrors  (  closeable  )  ; 
} 
} 






public   static   void   closeAndIgnoreErrors  (  Statement   statement  )  { 
try  { 
if  (  statement  !=  null  )  { 
statement  .  close  (  )  ; 
} 
}  catch  (  SQLException   ignore  )  { 
} 
} 






public   static   void   closeAndIgnoreErrors  (  ResultSet   resultSet  )  { 
try  { 
if  (  resultSet  !=  null  )  { 
resultSet  .  close  (  )  ; 
} 
}  catch  (  SQLException   ignore  )  { 
} 
} 






public   static   void   closeAndIgnoreErrors  (  Connection   connection  )  { 
try  { 
if  (  connection  !=  null  )  { 
connection  .  close  (  )  ; 
} 
}  catch  (  SQLException   ignore  )  { 
} 
} 






public   static   void   closeAndIgnoreErrors  (  Scanner   scanner  )  { 
if  (  scanner  !=  null  )  { 
scanner  .  close  (  )  ; 
} 
} 









public   static   void   blockingSkip  (  InputStream   in  ,  long   numberOfBytes  )  throws   IOException  { 
long   leftToSkip  =  numberOfBytes  ; 
while  (  leftToSkip  >  0  )  { 
long   actuallySkipped  =  in  .  skip  (  leftToSkip  )  ; 
leftToSkip  -=  actuallySkipped  ; 
} 
} 












public   static   int   blockingRead  (  InputStream   in  ,  byte  [  ]  buf  ,  int   offset  ,  int   length  )  throws   IOException  { 
int   currentBytesRead  =  0  ; 
int   totalBytesRead  =  0  ; 
while  (  (  currentBytesRead  =  in  .  read  (  buf  ,  offset  +  totalBytesRead  ,  length  -  totalBytesRead  )  )  >  0  )  { 
totalBytesRead  +=  currentBytesRead  ; 
if  (  totalBytesRead  ==  length  )  { 
break  ; 
} 
} 
return   totalBytesRead  ; 
} 

public   static   byte  [  ]  readByteArray  (  InputStream   in  ,  int   expectedLength  )  throws   IOException  { 
return   readByteArray  (  in  ,  expectedLength  ,  ENDIAN  .  BIG  )  ; 
} 

public   static   byte  [  ]  readByteArray  (  InputStream   in  ,  int   expectedLength  ,  ENDIAN   endian  )  throws   IOException  { 
byte  [  ]  array  =  new   byte  [  expectedLength  ]  ; 
int   bytesRead  =  blockingRead  (  in  ,  array  ,  0  ,  expectedLength  )  ; 
if  (  bytesRead  !=  expectedLength  )  { 
throw   new   IOException  (  "only was able to read "  +  bytesRead  +  "expected "  +  expectedLength  )  ; 
} 
if  (  endian  ==  ENDIAN  .  LITTLE  )  { 
return   IOUtil  .  switchEndian  (  array  )  ; 
} 
return   array  ; 
} 

public   static   short  [  ]  readUnsignedByteArray  (  InputStream   in  ,  int   expectedLength  )  throws   IOException  { 
short  [  ]  array  =  new   short  [  expectedLength  ]  ; 
for  (  int   i  =  0  ;  i  <  expectedLength  ;  i  ++  )  { 
array  [  i  ]  =  (  short  )  in  .  read  (  )  ; 
} 
return   array  ; 
} 

public   static   short  [  ]  readShortArray  (  InputStream   in  ,  int   expectedLength  )  throws   IOException  { 
short  [  ]  array  =  new   short  [  expectedLength  ]  ; 
DataInputStream   dataStream  =  new   DataInputStream  (  in  )  ; 
for  (  int   i  =  0  ;  i  <  expectedLength  ;  i  ++  )  { 
array  [  i  ]  =  dataStream  .  readShort  (  )  ; 
} 
return   array  ; 
} 

public   static   void   putShortArray  (  ByteBuffer   buf  ,  short  [  ]  array  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
buf  .  putShort  (  array  [  i  ]  )  ; 
} 
} 

public   static   void   putUnsignedByteArray  (  ByteBuffer   buf  ,  short  [  ]  array  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
buf  .  put  (  (  byte  )  array  [  i  ]  )  ; 
} 
} 








public   static   String   readStream  (  InputStream   in  )  throws   IOException  { 
final   ByteArrayOutputStream   expectedOutStream  =  new   ByteArrayOutputStream  (  )  ; 
OutputStreamReader   reader  =  new   OutputStreamReader  (  expectedOutStream  )  ; 
reader  .  read  (  in  )  ; 
return   expectedOutStream  .  toString  (  )  ; 
} 

public   static   byte  [  ]  readStreamAsBytes  (  InputStream   in  )  throws   IOException  { 
final   ByteArrayOutputStream   expectedOutStream  =  new   ByteArrayOutputStream  (  )  ; 
OutputStreamReader   reader  =  new   OutputStreamReader  (  expectedOutStream  )  ; 
reader  .  read  (  in  )  ; 
return   expectedOutStream  .  toByteArray  (  )  ; 
} 






public   static   int   convertToUnsignedByte  (  byte   value  )  { 
return   value  &  0xFF  ; 
} 

public   static   byte   convertUnsignedByteToSignedByte  (  long   unsignedByte  )  { 
if  (  unsignedByte  >  127  )  { 
return  (  byte  )  (  unsignedByte  -  256  )  ; 
} 
return  (  byte  )  unsignedByte  ; 
} 

public   static   short   convertUnsignedShortToSignedShort  (  long   unsignedShort  )  { 
if  (  unsignedShort  >  Short  .  MAX_VALUE  )  { 
return  (  short  )  (  unsignedShort  -  (  2  *  (  Short  .  MAX_VALUE  +  1  )  )  )  ; 
} 
return  (  short  )  unsignedShort  ; 
} 

public   static   int   convertUnsignedIntToSignedInt  (  long   unsignedInt  )  { 
if  (  unsignedInt  >  Integer  .  MAX_VALUE  )  { 
return  (  int  )  (  unsignedInt  -  (  2  *  (  Integer  .  MAX_VALUE  +  1  )  )  )  ; 
} 
return  (  int  )  unsignedInt  ; 
} 






public   static   int   convertToUnsignedShort  (  short   value  )  { 
return   value  &  0xFFFF  ; 
} 






public   static   long   convertToUnsignedInt  (  int   value  )  { 
return   value  &  0xFFFFFFFFL  ; 
} 

public   static   byte  [  ]  reverse  (  byte  [  ]  input  )  { 
ByteBuffer   result  =  ByteBuffer  .  allocate  (  input  .  length  )  ; 
for  (  int   i  =  input  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
result  .  put  (  input  [  i  ]  )  ; 
} 
return   result  .  array  (  )  ; 
} 

public   static   Properties   readPropertiesFromFile  (  File   propertiesFile  )  throws   IOException  { 
return   readPropertiesFromFile  (  propertiesFile  ,  new   Properties  (  )  )  ; 
} 

public   static   Properties   readPropertiesFromFile  (  File   propertiesFile  ,  Properties   properties  )  throws   IOException  { 
return   readPropertiesFromStream  (  new   FileInputStream  (  propertiesFile  )  ,  properties  )  ; 
} 

public   static   Properties   readPropertiesFromStream  (  InputStream   inputStream  )  throws   IOException  { 
return   readPropertiesFromStream  (  inputStream  ,  new   Properties  (  )  )  ; 
} 

public   static   Properties   readPropertiesFromStream  (  InputStream   inputStream  ,  Properties   props  )  throws   IOException  { 
try  { 
props  .  load  (  inputStream  )  ; 
return   props  ; 
}  finally  { 
closeAndIgnoreErrors  (  inputStream  )  ; 
} 
} 








public   static   byte  [  ]  switchEndian  (  byte  [  ]  byteArray  )  { 
byte   newByteArray  [  ]  =  new   byte  [  byteArray  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  byteArray  .  length  /  2  ;  i  ++  )  { 
newByteArray  [  i  ]  =  byteArray  [  byteArray  .  length  -  1  -  i  ]  ; 
newByteArray  [  byteArray  .  length  -  1  -  i  ]  =  byteArray  [  i  ]  ; 
} 
if  (  byteArray  .  length  %  2  ==  1  )  { 
int   center  =  byteArray  .  length  /  2  ; 
newByteArray  [  center  ]  =  byteArray  [  center  ]  ; 
} 
return   newByteArray  ; 
} 

public   static   BigInteger   readUnsignedLong  (  InputStream   in  ,  ENDIAN   endian  )  throws   IOException  { 
return   new   BigInteger  (  1  ,  IOUtil  .  readByteArray  (  in  ,  8  ,  endian  )  )  ; 
} 

public   static   long   readUnsignedInt  (  InputStream   in  ,  ENDIAN   endian  )  throws   IOException  { 
return   new   BigInteger  (  1  ,  IOUtil  .  readByteArray  (  in  ,  4  ,  endian  )  )  .  longValue  (  )  ; 
} 

public   static   long   readUnsignedInt  (  byte  [  ]  array  )  { 
return   new   BigInteger  (  1  ,  array  )  .  longValue  (  )  ; 
} 

public   static   int   readUnsignedShort  (  byte  [  ]  array  )  { 
return   new   BigInteger  (  1  ,  array  )  .  intValue  (  )  ; 
} 

public   static   short   readUnsignedByte  (  byte  [  ]  array  )  { 
return   new   BigInteger  (  1  ,  array  )  .  shortValue  (  )  ; 
} 

public   static   int   readUnsignedShort  (  InputStream   in  ,  ENDIAN   endian  )  throws   IOException  { 
return   new   BigInteger  (  1  ,  IOUtil  .  readByteArray  (  in  ,  2  ,  endian  )  )  .  intValue  (  )  ; 
} 

public   static   short   readUnsignedByte  (  InputStream   in  ,  ENDIAN   endian  )  throws   IOException  { 
return   new   BigInteger  (  1  ,  IOUtil  .  readByteArray  (  in  ,  1  ,  endian  )  )  .  shortValue  (  )  ; 
} 

public   static   BigInteger   readUnsignedLong  (  InputStream   in  )  throws   IOException  { 
return   readUnsignedLong  (  in  ,  ENDIAN  .  BIG  )  ; 
} 

public   static   long   readUnsignedInt  (  InputStream   in  )  throws   IOException  { 
return   readUnsignedInt  (  in  ,  ENDIAN  .  BIG  )  ; 
} 

public   static   int   readUnsignedShort  (  InputStream   in  )  throws   IOException  { 
return   readUnsignedShort  (  in  ,  ENDIAN  .  BIG  )  ; 
} 

public   static   short   readUnsignedByte  (  InputStream   in  )  throws   IOException  { 
return   readUnsignedByte  (  in  ,  ENDIAN  .  BIG  )  ; 
} 

public   static   byte  [  ]  convertUnsignedIntToByteArray  (  long   unsignedInt  )  { 
byte  [  ]  result  =  new   byte  [  4  ]  ; 
long   currentValue  =  unsignedInt  ; 
for  (  int   i  =  result  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
result  [  i  ]  =  (  byte  )  (  currentValue  &  0xff  )  ; 
currentValue  >  >  >=  8  ; 
} 
return   result  ; 
} 

public   static   byte  [  ]  convertUnsignedShortToByteArray  (  int   unsignedShort  )  { 
byte  [  ]  result  =  new   byte  [  2  ]  ; 
int   currentValue  =  unsignedShort  ; 
for  (  int   i  =  result  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
result  [  i  ]  =  (  byte  )  (  currentValue  &  0xff  )  ; 
currentValue  >  >  >=  8  ; 
} 
return   result  ; 
} 

public   static   byte  [  ]  convertUnsignedByteToByteArray  (  short   unsignedByte  )  { 
byte  [  ]  result  =  new   byte  [  1  ]  ; 
result  [  0  ]  =  (  byte  )  (  unsignedByte  &  0xff  )  ; 
return   result  ; 
} 

public   static   byte  [  ]  convertUnsignedLongToByteArray  (  BigInteger   unsignedLong  )  { 
String   hexString  =  convertToPaddedHex  (  unsignedLong  ,  16  )  ; 
byte  [  ]  result  =  new   byte  [  8  ]  ; 
for  (  int   i  =  0  ;  i  <  16  ;  i  +=  2  )  { 
String   byteInHex  =  hexString  .  substring  (  i  ,  i  +  2  )  ; 
result  [  i  /  2  ]  =  (  byte  )  Short  .  parseShort  (  byteInHex  ,  16  )  ; 
} 
return   result  ; 
} 

private   static   String   convertToPaddedHex  (  BigInteger   value  ,  int   maxNumberOfHexChars  )  { 
String   hexString  =  value  .  toString  (  16  )  ; 
int   padding  =  maxNumberOfHexChars  -  hexString  .  length  (  )  ; 
StringBuilder   paddingString  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  padding  ;  i  ++  )  { 
paddingString  .  append  (  "0"  )  ; 
} 
paddingString  .  append  (  hexString  )  ; 
String   asHex  =  paddingString  .  toString  (  )  ; 
return   asHex  ; 
} 

public   static   InputStream   createInputStreamFromFile  (  File   file  ,  Range   range  )  throws   IOException  { 
final   FileInputStream   fileInputStream  =  new   FileInputStream  (  file  )  ; 
FileChannel   fastaFileChannel  =  null  ; 
try  { 
fastaFileChannel  =  fileInputStream  .  getChannel  (  )  ; 
ByteBuffer   buf  =  ByteBuffer  .  allocate  (  (  int  )  range  .  size  (  )  )  ; 
fastaFileChannel  .  position  (  (  int  )  range  .  getStart  (  )  )  ; 
int   bytesRead  =  fastaFileChannel  .  read  (  buf  )  ; 
if  (  bytesRead  <  0  )  { 
throw   new   IOException  (  "could not read any bytes from file"  )  ; 
} 
final   ByteArrayInputStream   inputStream  =  new   ByteArrayInputStream  (  buf  .  array  (  )  )  ; 
return   inputStream  ; 
}  finally  { 
IOUtil  .  closeAndIgnoreErrors  (  fileInputStream  ,  fastaFileChannel  )  ; 
} 
} 
} 

