package   shu  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 














public   class   JCopy  { 

public   static   void   main  (  String   args  [  ]  )  { 
try  { 
JCopy  .  copyFile  (  new   File  (  args  [  0  ]  )  ,  new   File  (  args  [  1  ]  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   static   void   copyFile  (  File   in  ,  File   out  )  throws   IOException  { 
InputStream   is  =  new   BufferedInputStream  (  new   FileInputStream  (  in  )  )  ; 
OutputStream   fos  =  new   BufferedOutputStream  (  new   FileOutputStream  (  out  )  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
int   i  =  0  ; 
while  (  (  i  =  is  .  read  (  buf  )  )  !=  -  1  )  { 
fos  .  write  (  buf  ,  0  ,  i  )  ; 
} 
is  .  close  (  )  ; 
fos  .  close  (  )  ; 
} 







public   static   void   copyFileByNIO  (  File   in  ,  File   out  )  throws   IOException  { 
FileChannel   sourceChannel  =  new   FileInputStream  (  in  )  .  getChannel  (  )  ; 
FileChannel   destinationChannel  =  new   FileOutputStream  (  out  )  .  getChannel  (  )  ; 
sourceChannel  .  transferTo  (  0  ,  sourceChannel  .  size  (  )  ,  destinationChannel  )  ; 
sourceChannel  .  close  (  )  ; 
destinationChannel  .  close  (  )  ; 
} 
} 

