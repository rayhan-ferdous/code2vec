package   org  .  xmdl  .  ida  .  lib  .  util  ; 

import   java  .  security  .  MessageDigest  ; 
import   org  .  apache  .  commons  .  codec  .  DecoderException  ; 
import   org  .  apache  .  commons  .  codec  .  binary  .  Base64  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 






public   final   class   StringUtil  { 

private   static   final   Log   log  =  LogFactory  .  getLog  (  StringUtil  .  class  )  ; 




private   StringUtil  (  )  { 
} 












public   static   String   encodePassword  (  String   password  ,  String   algorithm  )  { 
byte  [  ]  unencodedPassword  =  password  .  getBytes  (  )  ; 
MessageDigest   md  =  null  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Exception: "  +  e  )  ; 
return   password  ; 
} 
md  .  reset  (  )  ; 
md  .  update  (  unencodedPassword  )  ; 
byte  [  ]  encodedPassword  =  md  .  digest  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  byte   anEncodedPassword  :  encodedPassword  )  { 
if  (  (  anEncodedPassword  &  0xff  )  <  0x10  )  { 
buf  .  append  (  "0"  )  ; 
} 
buf  .  append  (  Long  .  toString  (  anEncodedPassword  &  0xff  ,  16  )  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 











public   static   String   encodeString  (  String   str  )  { 
Base64   encoder  =  new   Base64  (  )  ; 
return   String  .  valueOf  (  encoder  .  encode  (  str  .  getBytes  (  )  )  )  .  trim  (  )  ; 
} 







public   static   String   decodeString  (  String   str  )  { 
Base64   dec  =  new   Base64  (  )  ; 
try  { 
return   String  .  valueOf  (  dec  .  decode  (  str  )  )  ; 
}  catch  (  DecoderException   de  )  { 
throw   new   RuntimeException  (  de  .  getMessage  (  )  ,  de  .  getCause  (  )  )  ; 
} 
} 





public   static   boolean   isEmpty  (  String   str  )  { 
return   str  ==  null  ||  ""  .  equals  (  str  )  ; 
} 
} 

