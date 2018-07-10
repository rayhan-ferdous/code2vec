package   Clone  .  Core  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 






public   class   ByteArrayBuilder  { 





public   static   byte  [  ]  fromStream  (  InputStream   in  )  throws   IOException  { 
ByteArrayOutputStream   store  =  new   ByteArrayOutputStream  (  )  ; 
InputStreamReader   reader  =  new   InputStreamReader  (  in  )  ; 
byte  [  ]  bData  =  new   byte  [  10000  ]  ; 
int   read  =  0  ; 
while  (  reader  .  ready  (  )  )  { 
read  =  in  .  read  (  bData  )  ; 
if  (  read  >  0  )  { 
store  .  write  (  bData  ,  0  ,  read  )  ; 
}  else  { 
break  ; 
} 
} 
return   store  .  toByteArray  (  )  ; 
} 
} 

