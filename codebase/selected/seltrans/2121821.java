package   ipmss  .  security  ; 

import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   javax  .  inject  .  Named  ; 






public   class   MyMessageDigest  { 


String   algorithm  ; 


MessageDigest   digest  ; 


String   charEncoding  =  "utf8"  ; 







public   MyMessageDigest  (  String   algorithm  )  throws   NoSuchAlgorithmException  { 
digest  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
} 








public   MyMessageDigest  (  String   algorithm  ,  String   charEncoding  )  throws   NoSuchAlgorithmException  { 
digest  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
this  .  charEncoding  =  charEncoding  ; 
} 






void   setCharEncoding  (  String   charEncoding  )  { 
this  .  charEncoding  =  charEncoding  ; 
} 








public   String   convertMessage  (  String   message  )  throws   UnsupportedEncodingException  { 
digest  .  update  (  message  .  getBytes  (  charEncoding  )  ,  0  ,  message  .  length  (  )  )  ; 
byte  [  ]  byteMessage  =  digest  .  digest  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  byteMessage  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  byteMessage  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  byteMessage  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 
} 

