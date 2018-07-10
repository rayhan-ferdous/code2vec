package   org  .  az  .  paccman  .  services  ; 

import   java  .  math  .  BigInteger  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   org  .  apache  .  commons  .  lang  .  RandomStringUtils  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  az  .  tb  .  common  .  vo  .  client  .  exceptions  .  TechException  ; 








public   class   CryptoTicketUtil  { 

private   static   Logger   logger  =  Logger  .  getLogger  (  CryptoTicketUtil  .  class  )  ; 

private   CryptoTicketUtil  (  )  { 
} 







public   static   String   MD5_hex  (  String   p  )  { 
MessageDigest   md  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md  .  update  (  p  .  getBytes  (  )  )  ; 
BigInteger   hash  =  new   BigInteger  (  1  ,  md  .  digest  (  )  )  ; 
String   ret  =  hash  .  toString  (  16  )  ; 
return   ret  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
logger  .  error  (  "can not create confirmation key"  ,  e  )  ; 
throw   new   TechException  (  e  )  ; 
} 
} 

public   static   String   generateInvitationCode  (  int   length  )  { 
return   RandomStringUtils  .  random  (  length  ,  true  ,  true  )  ; 
} 
} 

