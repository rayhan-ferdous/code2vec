package   com  .  guzzservices  .  manager  .  impl  .  ip  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  ByteOrder  ; 
import   java  .  nio  .  MappedByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  guzz  .  util  .  Assert  ; 







public   class   CZIPLoader  { 

private   static   Log   log  =  LogFactory  .  getLog  (  CZIPLoader  .  class  )  ; 

private   static   class   IPLocation  { 

private   String   country  ; 

private   String   area  ; 
} 

private   static   final   int   IP_RECORD_LENGTH  =  7  ; 

private   static   final   byte   REDIRECT_MODE_1  =  0x01  ; 

private   static   final   byte   REDIRECT_MODE_2  =  0x02  ; 

private   RandomAccessFile   ipFile  ; 

private   MappedByteBuffer   mbb  ; 

private   long   ipBegin  ,  ipEnd  ; 

private   IPLocation   loc  =  new   IPLocation  (  )  ; 

private   byte  [  ]  buf  =  new   byte  [  1000  ]  ; 

private   byte  [  ]  b4  =  new   byte  [  4  ]  ; 

private   String   unknown_area  ; 

public   CZIPLoader  (  File   file  )  throws   IOException  { 
ipFile  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
Assert  .  assertResouceNotNull  (  ipFile  ,  "unable to open QQWry.Dat file:"  +  file  )  ; 
ipBegin  =  readLong4  (  0  )  ; 
ipEnd  =  readLong4  (  4  )  ; 
if  (  ipBegin  ==  -  1  ||  ipEnd  ==  -  1  )  { 
ipFile  .  close  (  )  ; 
ipFile  =  null  ; 
throw   new   IOException  (  "unknown format of QQWry.Dat file:"  +  file  )  ; 
} 
FileChannel   fc  =  ipFile  .  getChannel  (  )  ; 
mbb  =  fc  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  ipFile  .  length  (  )  )  ; 
mbb  .  order  (  ByteOrder  .  LITTLE_ENDIAN  )  ; 
} 







private   long   readLong4  (  long   offset  )  { 
long   ret  =  0  ; 
try  { 
ipFile  .  seek  (  offset  )  ; 
ret  |=  (  ipFile  .  readByte  (  )  &  0xFF  )  ; 
ret  |=  (  (  ipFile  .  readByte  (  )  <<  8  )  &  0xFF00  )  ; 
ret  |=  (  (  ipFile  .  readByte  (  )  <<  16  )  &  0xFF0000  )  ; 
ret  |=  (  (  ipFile  .  readByte  (  )  <<  24  )  &  0xFF000000  )  ; 
return   ret  ; 
}  catch  (  IOException   e  )  { 
return  -  1  ; 
} 
} 





public   int   loadIPTable  (  BSTree  <  CityMark  >  tree  )  throws   IOException  { 
int   count  =  0  ; 
int   endOffset  =  (  int  )  ipEnd  ; 
for  (  int   offset  =  (  int  )  ipBegin  +  4  ;  offset  <=  endOffset  ;  offset  +=  IP_RECORD_LENGTH  )  { 
int   temp  =  readInt3  (  offset  )  ; 
if  (  temp  !=  -  1  )  { 
IPLocation   ipLoc  =  getIPLocation  (  temp  )  ; 
readIP  (  offset  -  4  ,  b4  )  ; 
String   beginIp  =  CZUtil  .  getIpStringFromBytes  (  b4  )  ; 
readIP  (  temp  ,  b4  )  ; 
String   endIp  =  CZUtil  .  getIpStringFromBytes  (  b4  )  ; 
String   country  =  CleanUtil  .  escapeCZ  (  loc  .  country  )  ; 
String   area  =  CleanUtil  .  escapeCZ  (  loc  .  area  )  ; 
if  (  country  ==  null  )  continue  ; 
String   cityMarker  =  CleanUtil  .  getCityMarker  (  country  )  ; 
if  (  country  .  equals  (  cityMarker  )  )  { 
cityMarker  =  null  ; 
} 
CityMark   cm  =  new   CityMark  (  beginIp  ,  endIp  ,  country  ,  area  ,  cityMarker  )  ; 
tree  .  insert  (  cm  )  ; 
count  ++  ; 
} 
} 
return   count  ; 
} 







private   int   readInt3  (  int   offset  )  { 
mbb  .  position  (  offset  )  ; 
return   mbb  .  getInt  (  )  &  0x00FFFFFF  ; 
} 






private   int   readInt3  (  )  { 
return   mbb  .  getInt  (  )  &  0x00FFFFFF  ; 
} 








private   void   readIP  (  int   offset  ,  byte  [  ]  ip  )  { 
mbb  .  position  (  offset  )  ; 
mbb  .  get  (  ip  )  ; 
byte   temp  =  ip  [  0  ]  ; 
ip  [  0  ]  =  ip  [  3  ]  ; 
ip  [  3  ]  =  temp  ; 
temp  =  ip  [  1  ]  ; 
ip  [  1  ]  =  ip  [  2  ]  ; 
ip  [  2  ]  =  temp  ; 
} 








private   IPLocation   getIPLocation  (  int   offset  )  { 
mbb  .  position  (  offset  +  4  )  ; 
byte   b  =  mbb  .  get  (  )  ; 
if  (  b  ==  REDIRECT_MODE_1  )  { 
int   countryOffset  =  readInt3  (  )  ; 
mbb  .  position  (  countryOffset  )  ; 
b  =  mbb  .  get  (  )  ; 
if  (  b  ==  REDIRECT_MODE_2  )  { 
loc  .  country  =  readString  (  readInt3  (  )  )  ; 
mbb  .  position  (  countryOffset  +  4  )  ; 
}  else   loc  .  country  =  readString  (  countryOffset  )  ; 
loc  .  area  =  readArea  (  mbb  .  position  (  )  )  ; 
}  else   if  (  b  ==  REDIRECT_MODE_2  )  { 
loc  .  country  =  readString  (  readInt3  (  )  )  ; 
loc  .  area  =  readArea  (  offset  +  8  )  ; 
}  else  { 
loc  .  country  =  readString  (  mbb  .  position  (  )  -  1  )  ; 
loc  .  area  =  readArea  (  mbb  .  position  (  )  )  ; 
} 
return   loc  ; 
} 






private   String   readArea  (  int   offset  )  { 
mbb  .  position  (  offset  )  ; 
byte   b  =  mbb  .  get  (  )  ; 
if  (  b  ==  REDIRECT_MODE_1  ||  b  ==  REDIRECT_MODE_2  )  { 
int   areaOffset  =  readInt3  (  )  ; 
if  (  areaOffset  ==  0  )  return   unknown_area  ;  else   return   readString  (  areaOffset  )  ; 
}  else   return   readString  (  offset  )  ; 
} 








private   String   readString  (  int   offset  )  { 
try  { 
mbb  .  position  (  offset  )  ; 
int   i  ; 
for  (  i  =  0  ,  buf  [  i  ]  =  mbb  .  get  (  )  ;  buf  [  i  ]  !=  0  ;  buf  [  ++  i  ]  =  mbb  .  get  (  )  )  ; 
if  (  i  !=  0  )  { 
if  (  i  >  100  )  { 
log  .  info  (  CZUtil  .  getString  (  buf  ,  0  ,  i  ,  "GBK"  )  )  ; 
} 
return   CZUtil  .  getString  (  buf  ,  0  ,  i  ,  "GBK"  )  ; 
} 
}  catch  (  IllegalArgumentException   e  )  { 
log  .  error  (  e  .  getMessage  (  )  )  ; 
} 
return  ""  ; 
} 
} 

