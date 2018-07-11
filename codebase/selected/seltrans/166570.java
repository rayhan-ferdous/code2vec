package   net  .  sourceforge  .  openstego  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   net  .  sourceforge  .  openstego  .  util  .  LabelUtil  ; 





public   class   DataHeader  { 




public   static   final   byte  [  ]  DATA_STAMP  =  "OPENSTEGO"  .  getBytes  (  )  ; 





public   static   final   byte  [  ]  HEADER_VERSION  =  new   byte  [  ]  {  (  byte  )  1  }  ; 




private   static   final   int   FIXED_HEADER_LENGTH  =  8  ; 




private   int   dataLength  =  0  ; 




private   int   channelBitsUsed  =  0  ; 




private   byte  [  ]  fileName  =  null  ; 




private   OpenStegoConfig   config  =  null  ; 








public   DataHeader  (  int   dataLength  ,  int   channelBitsUsed  ,  String   fileName  ,  OpenStegoConfig   config  )  { 
this  .  dataLength  =  dataLength  ; 
this  .  channelBitsUsed  =  channelBitsUsed  ; 
this  .  config  =  config  ; 
if  (  fileName  ==  null  )  { 
this  .  fileName  =  new   byte  [  0  ]  ; 
}  else  { 
try  { 
this  .  fileName  =  fileName  .  getBytes  (  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   unEx  )  { 
this  .  fileName  =  fileName  .  getBytes  (  )  ; 
} 
} 
} 







public   DataHeader  (  InputStream   dataInStream  ,  OpenStegoConfig   config  )  throws   OpenStegoException  { 
int   stampLen  =  0  ; 
int   versionLen  =  0  ; 
int   fileNameLen  =  0  ; 
int   channelBits  =  0  ; 
byte  [  ]  header  =  null  ; 
byte  [  ]  stamp  =  null  ; 
byte  [  ]  version  =  null  ; 
stampLen  =  DATA_STAMP  .  length  ; 
versionLen  =  HEADER_VERSION  .  length  ; 
header  =  new   byte  [  FIXED_HEADER_LENGTH  ]  ; 
stamp  =  new   byte  [  stampLen  ]  ; 
version  =  new   byte  [  versionLen  ]  ; 
try  { 
dataInStream  .  read  (  stamp  ,  0  ,  stampLen  )  ; 
if  (  !  (  new   String  (  stamp  )  )  .  equals  (  new   String  (  DATA_STAMP  )  )  )  { 
throw   new   OpenStegoException  (  OpenStegoException  .  INVALID_STEGO_HEADER  ,  null  )  ; 
} 
dataInStream  .  read  (  version  ,  0  ,  versionLen  )  ; 
if  (  !  (  new   String  (  version  )  )  .  equals  (  new   String  (  HEADER_VERSION  )  )  )  { 
throw   new   OpenStegoException  (  OpenStegoException  .  INVALID_HEADER_VERSION  ,  null  )  ; 
} 
dataInStream  .  read  (  header  ,  0  ,  FIXED_HEADER_LENGTH  )  ; 
dataLength  =  (  byteToInt  (  header  [  0  ]  )  +  (  byteToInt  (  header  [  1  ]  )  <<  8  )  +  (  byteToInt  (  header  [  2  ]  )  <<  16  )  +  (  byteToInt  (  header  [  3  ]  )  <<  32  )  )  ; 
channelBits  =  header  [  4  ]  ; 
fileNameLen  =  header  [  5  ]  ; 
config  .  setUseCompression  (  header  [  6  ]  ==  1  )  ; 
config  .  setUseEncryption  (  header  [  7  ]  ==  1  )  ; 
if  (  fileNameLen  ==  0  )  { 
fileName  =  new   byte  [  0  ]  ; 
}  else  { 
fileName  =  new   byte  [  fileNameLen  ]  ; 
dataInStream  .  read  (  fileName  ,  0  ,  fileNameLen  )  ; 
} 
}  catch  (  OpenStegoException   osEx  )  { 
throw   osEx  ; 
}  catch  (  Exception   ex  )  { 
throw   new   OpenStegoException  (  OpenStegoException  .  UNHANDLED_EXCEPTION  ,  ex  )  ; 
} 
channelBitsUsed  =  channelBits  ; 
this  .  config  =  config  ; 
} 





public   byte  [  ]  getHeaderData  (  )  { 
byte  [  ]  out  =  null  ; 
int   stampLen  =  0  ; 
int   versionLen  =  0  ; 
int   currIndex  =  0  ; 
stampLen  =  DATA_STAMP  .  length  ; 
versionLen  =  HEADER_VERSION  .  length  ; 
out  =  new   byte  [  stampLen  +  versionLen  +  FIXED_HEADER_LENGTH  +  fileName  .  length  ]  ; 
System  .  arraycopy  (  DATA_STAMP  ,  0  ,  out  ,  currIndex  ,  stampLen  )  ; 
currIndex  +=  stampLen  ; 
System  .  arraycopy  (  HEADER_VERSION  ,  0  ,  out  ,  currIndex  ,  versionLen  )  ; 
currIndex  +=  versionLen  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  (  dataLength  &  0x000000FF  )  )  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  (  dataLength  &  0x0000FF00  )  >  >  8  )  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  (  dataLength  &  0x00FF0000  )  >  >  16  )  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  (  dataLength  &  0xFF000000  )  >  >  32  )  ; 
out  [  currIndex  ++  ]  =  (  byte  )  channelBitsUsed  ; 
out  [  currIndex  ++  ]  =  (  byte  )  fileName  .  length  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  config  .  isUseCompression  (  )  ?  1  :  0  )  ; 
out  [  currIndex  ++  ]  =  (  byte  )  (  config  .  isUseEncryption  (  )  ?  1  :  0  )  ; 
if  (  fileName  .  length  >  0  )  { 
System  .  arraycopy  (  fileName  ,  0  ,  out  ,  currIndex  ,  fileName  .  length  )  ; 
currIndex  +=  fileName  .  length  ; 
} 
return   out  ; 
} 





public   int   getChannelBitsUsed  (  )  { 
return   channelBitsUsed  ; 
} 





public   void   setChannelBitsUsed  (  int   channelBitsUsed  )  { 
this  .  channelBitsUsed  =  channelBitsUsed  ; 
} 





public   int   getDataLength  (  )  { 
return   dataLength  ; 
} 





public   String   getFileName  (  )  { 
String   name  =  null  ; 
try  { 
name  =  new   String  (  fileName  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   unEx  )  { 
name  =  new   String  (  fileName  )  ; 
} 
return   name  ; 
} 





public   int   getHeaderSize  (  )  { 
return   DATA_STAMP  .  length  +  HEADER_VERSION  .  length  +  FIXED_HEADER_LENGTH  +  fileName  .  length  ; 
} 





public   static   int   getMaxHeaderSize  (  )  { 
return   DATA_STAMP  .  length  +  HEADER_VERSION  .  length  +  FIXED_HEADER_LENGTH  +  256  ; 
} 






public   static   int   byteToInt  (  int   b  )  { 
int   i  =  (  int  )  b  ; 
if  (  i  <  0  )  { 
i  =  i  +  256  ; 
} 
return   i  ; 
} 
} 

