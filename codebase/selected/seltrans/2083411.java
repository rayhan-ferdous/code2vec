package   jacg  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 






public   class   FileUtil  { 




public   static   final   String   FILE_SEPARATOR  =  System  .  getProperty  (  "file.separator"  ,  "/"  )  ; 




public   static   final   String   LINE_SEPARATOR  =  System  .  getProperty  (  "line.separator"  ,  "\n"  )  ; 









public   static   byte  [  ]  streamToByteArray  (  InputStream   is  )  throws   IOException  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   read  ; 
while  (  (  read  =  is  .  read  (  buffer  )  )  !=  -  1  )  { 
baos  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
is  .  close  (  )  ; 
return   baos  .  toByteArray  (  )  ; 
} 










public   static   void   byteArrayToStream  (  byte  [  ]  data  ,  OutputStream   os  )  throws   IOException  { 
os  .  write  (  data  )  ; 
os  .  close  (  )  ; 
} 
} 

