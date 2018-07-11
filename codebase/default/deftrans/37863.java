import   java  .  io  .  *  ; 















public   class   StraightStreamReader   extends   Reader  { 




private   InputStream   in  ; 








private   byte  [  ]  buffer  ; 






public   StraightStreamReader  (  InputStream   in  )  { 
this  .  in  =  in  ; 
} 






public   void   close  (  )  throws   IOException  { 
in  .  close  (  )  ; 
} 











public   void   mark  (  int   readAheadLimit  )  throws   IOException  { 
in  .  mark  (  readAheadLimit  )  ; 
} 






public   boolean   markSupported  (  )  { 
return   in  .  markSupported  (  )  ; 
} 









public   int   read  (  )  throws   IOException  { 
return   in  .  read  (  )  ; 
} 









public   int   read  (  char  [  ]  cbuf  )  throws   IOException  { 
return   read  (  cbuf  ,  0  ,  cbuf  .  length  )  ; 
} 











public   int   read  (  char  [  ]  cbuf  ,  int   off  ,  int   len  )  throws   IOException  { 
if  (  buffer  ==  null  ||  buffer  .  length  <  len  )  { 
buffer  =  new   byte  [  len  ]  ; 
} 
int   length  =  in  .  read  (  buffer  ,  0  ,  len  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
cbuf  [  off  +  i  ]  =  (  char  )  (  0xFF  &  buffer  [  i  ]  )  ; 
} 
return   length  ; 
} 








public   boolean   ready  (  )  throws   IOException  { 
return  (  in  .  available  (  )  >  0  )  ; 
} 











public   void   reset  (  )  throws   IOException  { 
in  .  reset  (  )  ; 
} 










public   long   skip  (  long   n  )  throws   IOException  { 
return   in  .  skip  (  n  )  ; 
} 











private   static   void   main  (  String  [  ]  args  )  { 
try  { 
File   f  =  new   File  (  "test.txt"  )  ; 
if  (  f  .  exists  (  )  )  { 
throw   new   IOException  (  f  +  " already exists.  I don't want to overwrite it."  )  ; 
} 
StraightStreamReader   in  ; 
char  [  ]  cbuf  =  new   char  [  0x1000  ]  ; 
int   read  ; 
int   totRead  ; 
FileOutputStream   out  =  new   FileOutputStream  (  f  )  ; 
for  (  int   i  =  0x00  ;  i  <  0x100  ;  i  ++  )  { 
out  .  write  (  i  )  ; 
} 
out  .  close  (  )  ; 
in  =  new   StraightStreamReader  (  new   FileInputStream  (  f  )  )  ; 
for  (  int   i  =  0x00  ;  i  <  0x100  ;  i  ++  )  { 
read  =  in  .  read  (  )  ; 
if  (  read  !=  i  )  { 
System  .  err  .  println  (  "Error: "  +  i  +  " read as "  +  read  )  ; 
} 
} 
in  .  close  (  )  ; 
in  =  new   StraightStreamReader  (  new   FileInputStream  (  f  )  )  ; 
totRead  =  in  .  read  (  cbuf  )  ; 
if  (  totRead  !=  0x100  )  { 
System  .  err  .  println  (  "Simple buffered read did not read the full amount: 0x"  +  Integer  .  toHexString  (  totRead  )  )  ; 
} 
for  (  int   i  =  0x00  ;  i  <  totRead  ;  i  ++  )  { 
if  (  cbuf  [  i  ]  !=  i  )  { 
System  .  err  .  println  (  "Error: 0x"  +  i  +  " read as 0x"  +  cbuf  [  i  ]  )  ; 
} 
} 
in  .  close  (  )  ; 
in  =  new   StraightStreamReader  (  new   FileInputStream  (  f  )  )  ; 
totRead  =  0  ; 
while  (  totRead  <=  0x100  &&  (  read  =  in  .  read  (  cbuf  ,  totRead  ,  0x100  -  totRead  )  )  >  0  )  { 
totRead  +=  read  ; 
} 
if  (  totRead  !=  0x100  )  { 
System  .  err  .  println  (  "Not enough read. Bytes read: "  +  Integer  .  toHexString  (  totRead  )  )  ; 
} 
for  (  int   i  =  0x00  ;  i  <  totRead  ;  i  ++  )  { 
if  (  cbuf  [  i  ]  !=  i  )  { 
System  .  err  .  println  (  "Error: 0x"  +  i  +  " read as 0x"  +  cbuf  [  i  ]  )  ; 
} 
} 
in  .  close  (  )  ; 
in  =  new   StraightStreamReader  (  new   FileInputStream  (  f  )  )  ; 
totRead  =  0  ; 
while  (  totRead  <=  0x100  &&  (  read  =  in  .  read  (  cbuf  ,  totRead  +  0x123  ,  0x100  -  totRead  )  )  >  0  )  { 
totRead  +=  read  ; 
} 
if  (  totRead  !=  0x100  )  { 
System  .  err  .  println  (  "Not enough read. Bytes read: "  +  Integer  .  toHexString  (  totRead  )  )  ; 
} 
for  (  int   i  =  0x00  ;  i  <  totRead  ;  i  ++  )  { 
if  (  cbuf  [  i  +  0x123  ]  !=  i  )  { 
System  .  err  .  println  (  "Error: 0x"  +  i  +  " read as 0x"  +  cbuf  [  i  +  0x123  ]  )  ; 
} 
} 
in  .  close  (  )  ; 
in  =  new   StraightStreamReader  (  new   FileInputStream  (  f  )  )  ; 
totRead  =  0  ; 
while  (  totRead  <=  0x100  &&  (  read  =  in  .  read  (  cbuf  ,  totRead  +  0x123  ,  7  )  )  >  0  )  { 
totRead  +=  read  ; 
} 
if  (  totRead  !=  0x100  )  { 
System  .  err  .  println  (  "Not enough read. Bytes read: "  +  Integer  .  toHexString  (  totRead  )  )  ; 
} 
for  (  int   i  =  0x00  ;  i  <  totRead  ;  i  ++  )  { 
if  (  cbuf  [  i  +  0x123  ]  !=  i  )  { 
System  .  err  .  println  (  "Error: 0x"  +  i  +  " read as 0x"  +  cbuf  [  i  +  0x123  ]  )  ; 
} 
} 
in  .  close  (  )  ; 
f  .  delete  (  )  ; 
}  catch  (  IOException   x  )  { 
System  .  err  .  println  (  x  .  getMessage  (  )  )  ; 
} 
} 
} 

