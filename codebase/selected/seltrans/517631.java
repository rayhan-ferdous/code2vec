package   easyJ  .  http  .  upload  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 





public   class   BufferedMultipartInputStream   extends   InputStream  { 




protected   InputStream   inputStream  ; 




protected   byte  [  ]  buffer  ; 




protected   int   bufferOffset  =  0  ; 




protected   int   bufferSize  =  8192  ; 





protected   int   bufferLength  =  0  ; 




protected   int   totalLength  =  0  ; 




protected   long   contentLength  ; 





protected   long   maxSize  =  -  1  ; 




protected   boolean   contentLengthMet  =  false  ; 




protected   boolean   maxLengthMet  =  false  ; 














public   BufferedMultipartInputStream  (  InputStream   inputStream  ,  int   bufferSize  ,  long   contentLength  ,  long   maxSize  )  throws   IOException  { 
this  .  inputStream  =  inputStream  ; 
this  .  bufferSize  =  bufferSize  ; 
this  .  contentLength  =  contentLength  ; 
this  .  maxSize  =  maxSize  ; 
if  (  (  maxSize  !=  -  1  )  &&  (  maxSize  <  contentLength  )  )  { 
throw   new   MaxLengthExceededException  (  maxSize  )  ; 
} 
buffer  =  new   byte  [  bufferSize  ]  ; 
fill  (  )  ; 
} 





public   int   available  (  )  { 
return   bufferLength  -  bufferOffset  ; 
} 




public   void   close  (  )  throws   IOException  { 
inputStream  .  close  (  )  ; 
} 




public   void   mark  (  int   position  )  { 
inputStream  .  mark  (  position  )  ; 
} 







public   boolean   markSupported  (  )  { 
return   inputStream  .  markSupported  (  )  ; 
} 




public   boolean   maxLengthMet  (  )  { 
return   maxLengthMet  ; 
} 




public   boolean   contentLengthMet  (  )  { 
return   contentLengthMet  ; 
} 








public   int   read  (  )  throws   IOException  { 
if  (  maxLengthMet  )  { 
throw   new   MaxLengthExceededException  (  maxSize  )  ; 
} 
if  (  contentLengthMet  )  { 
throw   new   ContentLengthExceededException  (  contentLength  )  ; 
} 
if  (  buffer  ==  null  )  { 
return  -  1  ; 
} 
if  (  bufferOffset  <  bufferLength  )  { 
return  (  int  )  (  char  )  buffer  [  bufferOffset  ++  ]  ; 
} 
fill  (  )  ; 
return   read  (  )  ; 
} 





public   int   read  (  byte  [  ]  b  )  throws   IOException  { 
return   read  (  b  ,  0  ,  b  .  length  )  ; 
} 





public   int   read  (  byte  [  ]  b  ,  int   offset  ,  int   length  )  throws   IOException  { 
int   count  =  0  ; 
int   read  =  read  (  )  ; 
if  (  read  ==  -  1  )  { 
return  -  1  ; 
} 
while  (  (  read  !=  -  1  )  &&  (  count  <  length  )  )  { 
b  [  offset  ]  =  (  byte  )  read  ; 
read  =  read  (  )  ; 
count  ++  ; 
offset  ++  ; 
} 
return   count  ; 
} 






public   int   readLine  (  byte  [  ]  b  ,  int   offset  ,  int   length  )  throws   IOException  { 
int   count  =  0  ; 
int   read  =  read  (  )  ; 
if  (  read  ==  -  1  )  { 
return  -  1  ; 
} 
while  (  (  read  !=  -  1  )  &&  (  count  <  length  )  )  { 
if  (  read  ==  '\n'  )  break  ; 
b  [  offset  ]  =  (  byte  )  read  ; 
count  ++  ; 
offset  ++  ; 
read  =  read  (  )  ; 
} 
return   count  ; 
} 






public   byte  [  ]  readLine  (  )  throws   IOException  { 
int   read  =  read  (  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
if  (  -  1  ==  read  )  return   null  ; 
while  (  (  read  !=  -  1  )  &&  (  read  !=  '\n'  )  )  { 
baos  .  write  (  read  )  ; 
read  =  read  (  )  ; 
} 
return   baos  .  toByteArray  (  )  ; 
} 





public   void   reset  (  )  throws   IOException  { 
inputStream  .  reset  (  )  ; 
} 






protected   void   fill  (  )  throws   IOException  { 
if  (  (  bufferOffset  >  -  1  )  &&  (  bufferLength  >  -  1  )  )  { 
int   length  =  Math  .  min  (  bufferSize  ,  (  (  (  int  )  contentLength  +  1  )  -  totalLength  )  )  ; 
if  (  length  ==  0  )  { 
contentLengthMet  =  true  ; 
} 
if  (  (  maxSize  >  -  1  )  &&  (  length  >  0  )  )  { 
length  =  Math  .  min  (  length  ,  (  (  int  )  maxSize  -  totalLength  )  )  ; 
if  (  length  ==  0  )  { 
maxLengthMet  =  true  ; 
} 
} 
int   bytesRead  =  -  1  ; 
if  (  length  >  0  )  { 
bytesRead  =  inputStream  .  read  (  buffer  ,  0  ,  length  )  ; 
} 
if  (  bytesRead  ==  -  1  )  { 
buffer  =  null  ; 
bufferOffset  =  -  1  ; 
bufferLength  =  -  1  ; 
}  else  { 
bufferLength  =  bytesRead  ; 
totalLength  +=  bytesRead  ; 
bufferOffset  =  0  ; 
} 
} 
} 
} 

