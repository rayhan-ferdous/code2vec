package   repast  .  simphony  .  integration  ; 

import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  Stack  ; 








public   class   RandomAccessWriter   extends   PrintWriter  { 

private   FileChannel   fileChannel  ; 

private   Stack  <  Long  >  markStack  ; 








public   RandomAccessWriter  (  File   file  )  throws   FileNotFoundException  { 
super  (  file  )  ; 
init  (  null  ,  file  )  ; 
} 








public   RandomAccessWriter  (  String   fileName  )  throws   FileNotFoundException  { 
super  (  fileName  )  ; 
init  (  fileName  ,  null  )  ; 
} 

private   void   init  (  String   fileName  ,  File   file  )  throws   FileNotFoundException  { 
markStack  =  new   Stack  <  Long  >  (  )  ; 
captureFileChannel  (  fileName  ,  file  )  ; 
} 

private   void   captureFileChannel  (  String   fileName  ,  File   file  )  throws   FileNotFoundException  { 
FileOutputStream   outStream  ; 
if  (  fileName  !=  null  )  { 
outStream  =  new   FileOutputStream  (  fileName  )  ; 
}  else  { 
outStream  =  new   FileOutputStream  (  file  )  ; 
} 
super  .  out  =  new   BufferedWriter  (  new   OutputStreamWriter  (  outStream  )  )  ; 
this  .  fileChannel  =  outStream  .  getChannel  (  )  ; 
} 

private   long   getFileOffsetInternal  (  )  throws   IOException  { 
super  .  flush  (  )  ; 
return   fileChannel  .  position  (  )  ; 
} 








public   long   getTrueOffset  (  )  throws   IOException  { 
long   fileOffset  =  getFileOffsetInternal  (  )  ; 
if  (  fileOffset  <=  0  )  { 
return   fileOffset  ; 
} 
return   fileOffset  ; 
} 







public   void   mark  (  )  throws   IOException  { 
markStack  .  push  (  getTrueOffset  (  )  )  ; 
} 






public   Long   popMark  (  )  { 
if  (  !  markStack  .  isEmpty  (  )  )  { 
return   markStack  .  pop  (  )  ; 
}  else  { 
return   null  ; 
} 
} 








public   void   reset  (  )  throws   IOException  { 
super  .  flush  (  )  ; 
position  (  markStack  .  peek  (  )  )  ; 
} 








public   void   position  (  long   position  )  throws   IOException  { 
fileChannel  .  position  (  position  )  ; 
} 








public   FileChannel   truncateToPosition  (  )  throws   IOException  { 
return   truncate  (  getTrueOffset  (  )  )  ; 
} 









public   FileChannel   truncate  (  long   size  )  throws   IOException  { 
super  .  flush  (  )  ; 
return   fileChannel  .  truncate  (  size  )  ; 
} 




@  Override 
public   void   close  (  )  { 
try  { 
super  .  close  (  )  ; 
fileChannel  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 

