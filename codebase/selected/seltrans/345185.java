package   org  .  icepdf  .  core  .  util  ; 

import   org  .  icepdf  .  core  .  io  .  SeekableByteArrayInputStream  ; 
import   org  .  icepdf  .  core  .  io  .  SeekableInput  ; 
import   java  .  io  .  *  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  logging  .  Level  ; 






public   class   Utils  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  Utils  .  class  .  toString  (  )  )  ; 









public   static   void   setIntIntoByteArrayBE  (  int   value  ,  byte  [  ]  buffer  ,  int   offset  )  { 
buffer  [  offset  +  0  ]  =  (  byte  )  (  (  value  >  >  >  24  )  &  0xff  )  ; 
buffer  [  offset  +  1  ]  =  (  byte  )  (  (  value  >  >  >  16  )  &  0xff  )  ; 
buffer  [  offset  +  2  ]  =  (  byte  )  (  (  value  >  >  >  8  )  &  0xff  )  ; 
buffer  [  offset  +  3  ]  =  (  byte  )  (  (  value  >  >  >  0  )  &  0xff  )  ; 
} 









public   static   void   setShortIntoByteArrayBE  (  short   value  ,  byte  [  ]  buffer  ,  int   offset  )  { 
buffer  [  offset  +  0  ]  =  (  byte  )  (  (  value  >  >  >  8  )  &  0xff  )  ; 
buffer  [  offset  +  1  ]  =  (  byte  )  (  (  value  >  >  >  0  )  &  0xff  )  ; 
} 







public   static   long   readLongWithVaryingBytesBE  (  InputStream   in  ,  int   numBytes  )  throws   IOException  { 
long   val  =  0  ; 
for  (  int   i  =  0  ;  i  <  numBytes  ;  i  ++  )  { 
int   curr  =  in  .  read  (  )  ; 
if  (  curr  <  0  )  throw   new   EOFException  (  )  ; 
val  <<=  8  ; 
val  |=  (  (  (  long  )  curr  )  &  (  (  long  )  0xFF  )  )  ; 
} 
return   val  ; 
} 







public   static   int   readIntWithVaryingBytesBE  (  InputStream   in  ,  int   numBytes  )  throws   IOException  { 
int   val  =  0  ; 
for  (  int   i  =  0  ;  i  <  numBytes  ;  i  ++  )  { 
int   curr  =  in  .  read  (  )  ; 
if  (  curr  <  0  )  throw   new   EOFException  (  )  ; 
val  <<=  8  ; 
val  |=  (  curr  &  0xFF  )  ; 
} 
return   val  ; 
} 

public   static   String   convertByteArrayToHexString  (  byte  [  ]  buffer  ,  boolean   addSpaceSeparator  )  { 
return   convertByteArrayToHexString  (  buffer  ,  0  ,  buffer  .  length  ,  addSpaceSeparator  ,  -  1  ,  (  char  )  0  )  ; 
} 

public   static   String   convertByteArrayToHexString  (  byte  [  ]  buffer  ,  boolean   addSpaceSeparator  ,  int   addDelimiterEverNBytes  ,  char   delimiter  )  { 
return   convertByteArrayToHexString  (  buffer  ,  0  ,  buffer  .  length  ,  addSpaceSeparator  ,  addDelimiterEverNBytes  ,  delimiter  )  ; 
} 

public   static   String   convertByteArrayToHexString  (  byte  [  ]  buffer  ,  int   offset  ,  int   length  ,  boolean   addSpaceSeparator  ,  int   addDelimiterEverNBytes  ,  char   delimiter  )  { 
int   presize  =  length  *  (  addSpaceSeparator  ?  3  :  2  )  ; 
if  (  addDelimiterEverNBytes  >  0  )  presize  +=  (  length  /  addDelimiterEverNBytes  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  presize  )  ; 
int   delimiterCount  =  0  ; 
int   end  =  offset  +  length  ; 
for  (  int   index  =  offset  ;  index  <  end  ;  index  ++  )  { 
int   currValue  =  0  ; 
currValue  |=  (  0xff  &  (  (  int  )  buffer  [  index  ]  )  )  ; 
String   s  =  Integer  .  toHexString  (  currValue  )  ; 
for  (  int   i  =  s  .  length  (  )  ;  i  <  2  ;  i  ++  )  sb  .  append  (  '0'  )  ; 
sb  .  append  (  s  )  ; 
if  (  addSpaceSeparator  )  sb  .  append  (  ' '  )  ; 
delimiterCount  ++  ; 
if  (  addDelimiterEverNBytes  >  0  &&  delimiterCount  ==  addDelimiterEverNBytes  )  { 
delimiterCount  =  0  ; 
sb  .  append  (  delimiter  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 











public   static   boolean   reflectGraphicsEnvironmentISHeadlessInstance  (  Object   graphicsEnvironment  ,  boolean   defaultReturnIfNoMethod  )  { 
try  { 
Class   clazz  =  graphicsEnvironment  .  getClass  (  )  ; 
Method   isHeadlessInstanceMethod  =  clazz  .  getMethod  (  "isHeadlessInstance"  ,  new   Class  [  ]  {  }  )  ; 
if  (  isHeadlessInstanceMethod  !=  null  )  { 
Object   ret  =  isHeadlessInstanceMethod  .  invoke  (  graphicsEnvironment  ,  new   Object  [  ]  {  }  )  ; 
if  (  ret   instanceof   Boolean  )  return  (  (  Boolean  )  ret  )  .  booleanValue  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
logger  .  log  (  Level  .  FINE  ,  "ImageCache: Java 1.4 Headless support not found."  )  ; 
} 
return   defaultReturnIfNoMethod  ; 
} 

public   static   String   getContentAndReplaceInputStream  (  InputStream  [  ]  inArray  ,  boolean   convertToHex  )  { 
String   content  =  null  ; 
try  { 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  1024  )  ; 
InputStream   in  =  inArray  [  0  ]  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
while  (  true  )  { 
int   read  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  ; 
if  (  read  <  0  )  break  ; 
out  .  write  (  buf  ,  0  ,  read  )  ; 
} 
if  (  !  (  in   instanceof   SeekableInput  )  )  in  .  close  (  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
byte  [  ]  data  =  out  .  toByteArray  (  )  ; 
out  =  null  ; 
inArray  [  0  ]  =  new   ByteArrayInputStream  (  data  )  ; 
if  (  convertToHex  )  content  =  Utils  .  convertByteArrayToHexString  (  data  ,  true  )  ;  else   content  =  new   String  (  data  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  log  (  Level  .  FINE  ,  "Problem getting debug string"  )  ; 
}  catch  (  Throwable   e  )  { 
logger  .  log  (  Level  .  FINE  ,  "Problem getting content stream, skipping"  )  ; 
} 
return   content  ; 
} 

public   static   String   getContentFromSeekableInput  (  SeekableInput   in  ,  boolean   convertToHex  )  { 
String   content  =  null  ; 
try  { 
long   position  =  in  .  getAbsolutePosition  (  )  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
while  (  true  )  { 
int   read  =  in  .  getInputStream  (  )  .  read  (  )  ; 
if  (  read  <  0  )  break  ; 
out  .  write  (  read  )  ; 
} 
in  .  seekAbsolute  (  position  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
byte  [  ]  data  =  out  .  toByteArray  (  )  ; 
if  (  convertToHex  )  content  =  Utils  .  convertByteArrayToHexString  (  data  ,  true  )  ;  else   content  =  new   String  (  data  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  log  (  Level  .  FINE  ,  "Problem getting debug string"  )  ; 
} 
return   content  ; 
} 

public   static   SeekableInput   replaceInputStreamWithSeekableInput  (  InputStream   in  )  { 
if  (  in   instanceof   SeekableInput  )  return  (  SeekableInput  )  in  ; 
SeekableInput   sin  =  null  ; 
try  { 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  1024  )  ; 
while  (  true  )  { 
int   read  =  in  .  read  (  )  ; 
if  (  read  <  0  )  break  ; 
out  .  write  (  read  )  ; 
} 
in  .  close  (  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
byte  [  ]  data  =  out  .  toByteArray  (  )  ; 
sin  =  new   SeekableByteArrayInputStream  (  data  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  log  (  Level  .  FINE  ,  "Problem getting debug string"  )  ; 
} 
return   sin  ; 
} 

public   static   void   printMemory  (  String   str  )  { 
long   total  =  Runtime  .  getRuntime  (  )  .  totalMemory  (  )  ; 
long   free  =  Runtime  .  getRuntime  (  )  .  freeMemory  (  )  ; 
long   used  =  total  -  free  ; 
System  .  out  .  println  (  "MEM  "  +  str  +  "    used: "  +  (  used  /  1024  )  +  " KB    delta: "  +  (  (  used  -  lastMemUsed  )  /  1024  )  +  " KB"  )  ; 
lastMemUsed  =  used  ; 
} 

private   static   long   lastMemUsed  =  0  ; 

public   static   int   numBytesToHoldBits  (  int   numBits  )  { 
int   numBytes  =  (  numBits  /  8  )  ; 
if  (  (  numBits  %  8  )  >  0  )  numBytes  ++  ; 
return   numBytes  ; 
} 
} 

