package   ces  .  platform  .  infoplat  .  utils  .  jar  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  util  .  jar  .  JarEntry  ; 
import   java  .  util  .  jar  .  JarOutputStream  ; 






public   class   JarCompressor   extends   Compressor  { 

private   JarOutputStream   mJos  ; 

private   int   miCurrentCount  ; 

public   JarCompressor  (  File   jarfl  )  { 
super  (  jarfl  )  ; 
miCurrentCount  =  0  ; 
} 




public   void   open  (  )  throws   Exception  { 
try  { 
mJos  =  new   JarOutputStream  (  new   FileOutputStream  (  mCompressedFile  )  )  ; 
mObserverCont  .  start  (  )  ; 
mObserverCont  .  setCount  (  0  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
throw   ex  ; 
} 
} 




protected   void   setCompressionLevel  (  int   level  )  { 
mJos  .  setLevel  (  level  )  ; 
} 




protected   void   addFile  (  File   newEntry  ,  String   name  )  { 
if  (  newEntry  .  isDirectory  (  )  )  { 
return  ; 
} 
try  { 
JarEntry   je  =  new   JarEntry  (  name  )  ; 
mJos  .  putNextEntry  (  je  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  newEntry  )  ; 
byte   fdata  [  ]  =  new   byte  [  1024  ]  ; 
int   readCount  =  0  ; 
while  (  (  readCount  =  fis  .  read  (  fdata  )  )  !=  -  1  )  { 
mJos  .  write  (  fdata  ,  0  ,  readCount  )  ; 
} 
fis  .  close  (  )  ; 
mJos  .  closeEntry  (  )  ; 
mObserverCont  .  setNext  (  je  )  ; 
mObserverCont  .  setCount  (  ++  miCurrentCount  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
} 
} 




public   void   close  (  )  { 
try  { 
mJos  .  finish  (  )  ; 
mJos  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
mObserverCont  .  setError  (  ex  .  getMessage  (  )  )  ; 
}  finally  { 
mObserverCont  .  end  (  )  ; 
} 
} 
} 

