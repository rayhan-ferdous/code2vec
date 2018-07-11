package   ces  .  platform  .  infoplat  .  utils  .  jar  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 






public   class   ZipCompressor   extends   Compressor  { 

private   ZipOutputStream   mZos  ; 

private   int   miCurrentCount  ; 

public   ZipCompressor  (  File   zipfl  )  { 
super  (  zipfl  )  ; 
miCurrentCount  =  0  ; 
} 




public   void   open  (  )  throws   Exception  { 
try  { 
mZos  =  new   ZipOutputStream  (  new   FileOutputStream  (  mCompressedFile  )  )  ; 
mObserverCont  .  start  (  )  ; 
mObserverCont  .  setCount  (  0  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
throw   ex  ; 
} 
} 




protected   void   setCompressionLevel  (  int   level  )  { 
mZos  .  setLevel  (  level  )  ; 
} 




protected   void   addFile  (  File   newEntry  ,  String   name  )  { 
if  (  newEntry  .  isDirectory  (  )  )  { 
return  ; 
} 
try  { 
ZipEntry   ze  =  new   ZipEntry  (  name  )  ; 
mZos  .  putNextEntry  (  ze  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  newEntry  )  ; 
byte   fdata  [  ]  =  new   byte  [  512  ]  ; 
int   readCount  =  0  ; 
while  (  (  readCount  =  fis  .  read  (  fdata  )  )  !=  -  1  )  { 
mZos  .  write  (  fdata  ,  0  ,  readCount  )  ; 
} 
fis  .  close  (  )  ; 
mZos  .  closeEntry  (  )  ; 
mObserverCont  .  setNext  (  ze  )  ; 
mObserverCont  .  setCount  (  ++  miCurrentCount  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
} 
} 




public   void   close  (  )  { 
try  { 
mZos  .  finish  (  )  ; 
mZos  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
}  finally  { 
mObserverCont  .  end  (  )  ; 
} 
} 
} 

