package   ces  .  platform  .  infoplat  .  utils  .  io  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  Random  ; 






public   class   IoUtils  { 




private   static   final   Random   RANDOM_GEN  =  new   Random  (  System  .  currentTimeMillis  (  )  )  ; 




public   static   BufferedInputStream   getBufferedInputStream  (  InputStream   in  )  { 
BufferedInputStream   bin  =  null  ; 
if  (  in   instanceof   java  .  io  .  BufferedInputStream  )  { 
bin  =  (  BufferedInputStream  )  in  ; 
}  else  { 
bin  =  new   BufferedInputStream  (  in  )  ; 
} 
return   bin  ; 
} 




public   static   BufferedOutputStream   getBufferedOutputStream  (  OutputStream   out  )  { 
BufferedOutputStream   bout  =  null  ; 
if  (  out   instanceof   java  .  io  .  BufferedOutputStream  )  { 
bout  =  (  BufferedOutputStream  )  out  ; 
}  else  { 
bout  =  new   BufferedOutputStream  (  out  )  ; 
} 
return   bout  ; 
} 




public   static   BufferedReader   getBufferedReader  (  Reader   rd  )  { 
BufferedReader   br  =  null  ; 
if  (  br   instanceof   java  .  io  .  BufferedReader  )  { 
br  =  (  BufferedReader  )  rd  ; 
}  else  { 
br  =  new   BufferedReader  (  rd  )  ; 
} 
return   br  ; 
} 




public   static   BufferedWriter   getBufferedWriter  (  Writer   wr  )  { 
BufferedWriter   bw  =  null  ; 
if  (  wr   instanceof   java  .  io  .  BufferedWriter  )  { 
bw  =  (  BufferedWriter  )  wr  ; 
}  else  { 
bw  =  new   BufferedWriter  (  wr  )  ; 
} 
return   bw  ; 
} 




public   static   File   getUniqueFile  (  File   oldFile  )  { 
File   newFile  =  oldFile  ; 
while  (  true  )  { 
if  (  !  newFile  .  exists  (  )  )  { 
break  ; 
} 
newFile  =  new   File  (  oldFile  .  getAbsolutePath  (  )  +  '.'  +  Math  .  abs  (  RANDOM_GEN  .  nextLong  (  )  )  )  ; 
} 
return   newFile  ; 
} 




public   static   void   close  (  InputStream   is  )  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 




public   static   void   close  (  OutputStream   os  )  { 
if  (  os  !=  null  )  { 
try  { 
os  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 




public   static   void   close  (  Reader   rd  )  { 
if  (  rd  !=  null  )  { 
try  { 
rd  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 




public   static   void   close  (  Writer   wr  )  { 
if  (  wr  !=  null  )  { 
try  { 
wr  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 




public   static   String   getStackTrace  (  Throwable   ex  )  { 
String   result  =  ""  ; 
try  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
ex  .  printStackTrace  (  pw  )  ; 
pw  .  close  (  )  ; 
sw  .  close  (  )  ; 
result  =  sw  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   result  ; 
} 





public   static   void   copy  (  Reader   input  ,  Writer   output  ,  int   bufferSize  )  throws   IOException  { 
char   buffer  [  ]  =  new   char  [  bufferSize  ]  ; 
int   n  =  0  ; 
while  (  (  n  =  input  .  read  (  buffer  )  )  !=  -  1  )  { 
output  .  write  (  buffer  ,  0  ,  n  )  ; 
} 
} 





public   static   void   copy  (  InputStream   input  ,  OutputStream   output  ,  int   bufferSize  )  throws   IOException  { 
byte   buffer  [  ]  =  new   byte  [  bufferSize  ]  ; 
int   n  =  0  ; 
while  (  (  n  =  input  .  read  (  buffer  )  )  !=  -  1  )  { 
output  .  write  (  buffer  ,  0  ,  n  )  ; 
} 
} 




public   static   String   readFully  (  Reader   reader  )  throws   IOException  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
copy  (  reader  ,  writer  ,  1024  )  ; 
return   writer  .  toString  (  )  ; 
} 




public   static   String   readFully  (  InputStream   input  )  throws   IOException  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
InputStreamReader   reader  =  new   InputStreamReader  (  input  )  ; 
copy  (  reader  ,  writer  ,  1024  )  ; 
return   writer  .  toString  (  )  ; 
} 
} 

