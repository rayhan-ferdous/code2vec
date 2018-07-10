package   halo  .  web  .  action  .  upload  .  cos  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   javax  .  servlet  .  ServletInputStream  ; 

public   class   FilePart   extends   Part  { 


private   String   fileName  ; 


private   String   filePath  ; 


private   String   contentType  ; 


private   PartInputStream   partInput  ; 


private   FileRenamePolicy   policy  ; 















FilePart  (  String   name  ,  ServletInputStream   in  ,  String   boundary  ,  String   contentType  ,  String   fileName  ,  String   filePath  )  { 
super  (  name  )  ; 
this  .  fileName  =  fileName  ; 
this  .  filePath  =  filePath  ; 
this  .  contentType  =  contentType  ; 
partInput  =  new   PartInputStream  (  in  ,  boundary  )  ; 
} 




public   void   setRenamePolicy  (  FileRenamePolicy   policy  )  { 
this  .  policy  =  policy  ; 
} 














public   String   getFileName  (  )  { 
return   fileName  ; 
} 










public   String   getFilePath  (  )  { 
return   filePath  ; 
} 






public   String   getContentType  (  )  { 
return   contentType  ; 
} 










public   InputStream   getInputStream  (  )  { 
return   partInput  ; 
} 










public   long   writeTo  (  File   fileOrDirectory  )  throws   IOException  { 
long   written  =  0  ; 
OutputStream   fileOut  =  null  ; 
try  { 
if  (  fileName  !=  null  )  { 
File   file  ; 
if  (  fileOrDirectory  .  isDirectory  (  )  )  { 
file  =  new   File  (  fileOrDirectory  ,  fileName  )  ; 
}  else  { 
file  =  fileOrDirectory  ; 
} 
if  (  policy  !=  null  )  { 
file  =  policy  .  rename  (  file  )  ; 
fileName  =  file  .  getName  (  )  ; 
} 
fileOut  =  new   BufferedOutputStream  (  new   FileOutputStream  (  file  )  )  ; 
written  =  write  (  fileOut  )  ; 
} 
}  finally  { 
if  (  fileOut  !=  null  )  fileOut  .  close  (  )  ; 
} 
return   written  ; 
} 








public   long   writeTo  (  OutputStream   out  )  throws   IOException  { 
long   size  =  0  ; 
if  (  fileName  !=  null  )  { 
size  =  write  (  out  )  ; 
} 
return   size  ; 
} 








long   write  (  OutputStream   out  )  throws   IOException  { 
OutputStream   _out  =  null  ; 
if  (  contentType  .  equals  (  "application/x-macbinary"  )  )  { 
_out  =  new   MacBinaryDecoderOutputStream  (  out  )  ; 
}  else  { 
_out  =  out  ; 
} 
long   size  =  0  ; 
int   read  ; 
byte  [  ]  buf  =  new   byte  [  8  *  1024  ]  ; 
while  (  (  read  =  partInput  .  read  (  buf  )  )  !=  -  1  )  { 
_out  .  write  (  buf  ,  0  ,  read  )  ; 
size  +=  read  ; 
} 
return   size  ; 
} 






@  Override 
public   boolean   isFile  (  )  { 
return   true  ; 
} 
} 

