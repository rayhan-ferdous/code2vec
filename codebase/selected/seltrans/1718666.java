package   javax  .  media  .  ding3d  .  loaders  .  lw3d  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  IOException  ; 
import   javax  .  media  .  ding3d  .  loaders  .  ParsingErrorException  ; 

class   LWOBFileReader   extends   BufferedInputStream  { 

static   final   int   TRACE  =  DebugOutput  .  TRACE  ; 

static   final   int   VALUES  =  DebugOutput  .  VALUES  ; 

static   final   int   MISC  =  DebugOutput  .  MISC  ; 

static   final   int   LINE_TRACE  =  DebugOutput  .  LINE_TRACE  ; 

static   final   int   NONE  =  DebugOutput  .  NONE  ; 

static   final   int   EXCEPTION  =  DebugOutput  .  EXCEPTION  ; 

protected   DebugOutput   debugPrinter  ; 

protected   String   theFilename  ; 

protected   int   marker  ; 

protected   void   debugOutputLn  (  int   outputType  ,  String   theOutput  )  { 
if  (  theOutput  .  equals  (  ""  )  )  debugPrinter  .  println  (  outputType  ,  theOutput  )  ;  else   debugPrinter  .  println  (  outputType  ,  getClass  (  )  .  getName  (  )  +  "::"  +  theOutput  )  ; 
} 

public   String   getToken  (  )  throws   ParsingErrorException  { 
byte   tokenBuffer  [  ]  =  new   byte  [  4  ]  ; 
try  { 
int   readResult  =  read  (  tokenBuffer  ,  0  ,  4  )  ; 
if  (  readResult  ==  -  1  )  { 
debugOutputLn  (  LINE_TRACE  ,  "no token - returning null"  )  ; 
return   null  ; 
} 
return   new   String  (  tokenBuffer  )  ; 
}  catch  (  IOException   e  )  { 
debugOutputLn  (  EXCEPTION  ,  "getToken: "  +  e  )  ; 
throw   new   ParsingErrorException  (  e  .  getMessage  (  )  )  ; 
} 
} 




public   void   skipLength  (  int   amount  )  throws   ParsingErrorException  { 
try  { 
skip  (  (  long  )  amount  )  ; 
marker  +=  amount  ; 
}  catch  (  IOException   e  )  { 
debugOutputLn  (  EXCEPTION  ,  "skipLength: "  +  e  )  ; 
throw   new   ParsingErrorException  (  e  .  getMessage  (  )  )  ; 
} 
} 




public   int   getInt  (  )  throws   ParsingErrorException  { 
try  { 
int   x  =  0  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
int   readResult  =  read  (  )  ; 
if  (  readResult  ==  -  1  )  throw   new   ParsingErrorException  (  "Unexpected EOF"  )  ; 
x  =  (  x  <<  8  )  |  readResult  ; 
} 
return   x  ; 
}  catch  (  IOException   e  )  { 
debugOutputLn  (  EXCEPTION  ,  "getInt: "  +  e  )  ; 
throw   new   ParsingErrorException  (  e  .  getMessage  (  )  )  ; 
} 
} 




public   float   getFloat  (  )  throws   ParsingErrorException  { 
return   Float  .  intBitsToFloat  (  getInt  (  )  )  ; 
} 




public   String   getFilename  (  )  { 
return   theFilename  ; 
} 





public   String   getString  (  )  throws   ParsingErrorException  { 
byte   buf  [  ]  =  new   byte  [  512  ]  ; 
try  { 
byte   b  ; 
int   len  =  0  ; 
do  { 
b  =  (  byte  )  read  (  )  ; 
buf  [  len  ++  ]  =  b  ; 
}  while  (  b  !=  0  )  ; 
if  (  len  %  2  !=  0  )  read  (  )  ; 
}  catch  (  IOException   e  )  { 
debugOutputLn  (  EXCEPTION  ,  "getString: "  +  e  )  ; 
throw   new   ParsingErrorException  (  e  .  getMessage  (  )  )  ; 
} 
return   new   String  (  buf  )  ; 
} 




public   void   getVerts  (  float   ar  [  ]  ,  int   num  )  throws   ParsingErrorException  { 
for  (  int   i  =  0  ;  i  <  num  ;  i  ++  )  { 
ar  [  i  *  3  +  0  ]  =  getFloat  (  )  ; 
ar  [  i  *  3  +  1  ]  =  getFloat  (  )  ; 
ar  [  i  *  3  +  2  ]  =  -  getFloat  (  )  ; 
} 
} 




public   int   getShortInt  (  )  throws   ParsingErrorException  { 
int   i  =  0  ; 
try  { 
i  =  read  (  )  ; 
i  =  (  i  <<  8  )  |  read  (  )  ; 
if  (  (  i  &  0x8000  )  !=  0  )  i  |=  0xffff0000  ; 
}  catch  (  IOException   e  )  { 
debugOutputLn  (  EXCEPTION  ,  "getShortInt: "  +  e  )  ; 
throw   new   ParsingErrorException  (  e  .  getMessage  (  )  )  ; 
} 
return   i  ; 
} 




public   int   getMarker  (  )  { 
return   marker  ; 
} 

public   int   read  (  )  throws   IOException  { 
marker  ++  ; 
return   super  .  read  (  )  ; 
} 

public   int   read  (  byte  [  ]  buffer  ,  int   offset  ,  int   count  )  throws   IOException  { 
int   ret  =  super  .  read  (  buffer  ,  offset  ,  count  )  ; 
if  (  ret  !=  -  1  )  marker  +=  ret  ; 
return   ret  ; 
} 




public   LWOBFileReader  (  String   filename  )  throws   FileNotFoundException  { 
super  (  new   FileInputStream  (  filename  )  )  ; 
debugPrinter  =  new   DebugOutput  (  127  )  ; 
marker  =  0  ; 
} 

public   LWOBFileReader  (  java  .  net  .  URL   url  )  throws   java  .  io  .  IOException  { 
super  (  url  .  openStream  (  )  )  ; 
debugPrinter  =  new   DebugOutput  (  127  )  ; 
marker  =  0  ; 
} 
} 

