package   org  .  subrecord  .  utils  ; 

import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 





public   class   ShaEncoder  { 




private   static   String   convertToHex  (  byte  [  ]  data  )  { 
StringBuilder   buf  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 




public   static   String   sha1  (  String   text  )  throws   NoSuchAlgorithmException  ,  UnsupportedEncodingException  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "SHA-1"  )  ; 
md  .  update  (  text  .  getBytes  (  "UTF-8"  )  ,  0  ,  text  .  length  (  )  )  ; 
byte  [  ]  sha1hash  =  md  .  digest  (  )  ; 
return   convertToHex  (  sha1hash  )  ; 
} 
} 

