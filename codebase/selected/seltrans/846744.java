package   org  .  commsuite  .  util  ; 

import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  SecureRandom  ; 
import   java  .  util  .  Random  ; 
import   org  .  apache  .  log4j  .  Logger  ; 




















































public   class   RandomGUID  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  RandomGUID  .  class  )  ; 

private   String   valueBeforeMD5  =  ""  ; 

private   String   valueAfterMD5  =  ""  ; 

private   static   final   Random   myRand  ; 

private   static   final   SecureRandom   mySecureRand  ; 

private   static   String   sId  ; 

static  { 
mySecureRand  =  new   SecureRandom  (  )  ; 
final   long   secureInitializer  =  mySecureRand  .  nextLong  (  )  ; 
myRand  =  new   Random  (  secureInitializer  )  ; 
try  { 
sId  =  InetAddress  .  getLocalHost  (  )  .  toString  (  )  ; 
}  catch  (  UnknownHostException   e  )  { 
logger  .  fatal  (  ""  ,  e  )  ; 
} 
} 





private   RandomGUID  (  )  { 
getRandomGUID  (  false  )  ; 
} 






private   RandomGUID  (  boolean   secure  )  { 
getRandomGUID  (  secure  )  ; 
} 




private   void   getRandomGUID  (  boolean   secure  )  { 
MessageDigest   md5  =  null  ; 
final   StringBuilder   sbValueBeforeMD5  =  new   StringBuilder  (  )  ; 
try  { 
md5  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
logger  .  fatal  (  ""  ,  e  )  ; 
return  ; 
} 
try  { 
final   long   time  =  System  .  currentTimeMillis  (  )  ; 
long   rand  =  0  ; 
if  (  secure  )  { 
rand  =  mySecureRand  .  nextLong  (  )  ; 
}  else  { 
rand  =  myRand  .  nextLong  (  )  ; 
} 
sbValueBeforeMD5  .  append  (  sId  )  ; 
sbValueBeforeMD5  .  append  (  ":"  )  ; 
sbValueBeforeMD5  .  append  (  Long  .  toString  (  time  )  )  ; 
sbValueBeforeMD5  .  append  (  ":"  )  ; 
sbValueBeforeMD5  .  append  (  Long  .  toString  (  rand  )  )  ; 
valueBeforeMD5  =  sbValueBeforeMD5  .  toString  (  )  ; 
md5  .  update  (  valueBeforeMD5  .  getBytes  (  )  )  ; 
final   byte  [  ]  array  =  md5  .  digest  (  )  ; 
final   StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  int   j  =  0  ;  j  <  array  .  length  ;  ++  j  )  { 
final   int   b  =  array  [  j  ]  &  0xFF  ; 
if  (  b  <  0x10  )  { 
sb  .  append  (  '0'  )  ; 
} 
sb  .  append  (  Integer  .  toHexString  (  b  )  )  ; 
} 
valueAfterMD5  =  sb  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  fatal  (  ""  ,  e  )  ; 
} 
} 





public   String   toString  (  )  { 
final   String   raw  =  valueAfterMD5  .  toUpperCase  (  )  ; 
final   StringBuilder   sb  =  new   StringBuilder  (  )  ; 
sb  .  append  (  raw  .  substring  (  0  ,  8  )  )  ; 
sb  .  append  (  "-"  )  ; 
sb  .  append  (  raw  .  substring  (  8  ,  12  )  )  ; 
sb  .  append  (  "-"  )  ; 
sb  .  append  (  raw  .  substring  (  12  ,  16  )  )  ; 
sb  .  append  (  "-"  )  ; 
sb  .  append  (  raw  .  substring  (  16  ,  20  )  )  ; 
sb  .  append  (  "-"  )  ; 
sb  .  append  (  raw  .  substring  (  20  )  )  ; 
return   sb  .  toString  (  )  ; 
} 

public   static   final   String   getGUID  (  )  { 
return   getGUID  (  false  )  ; 
} 

public   static   final   String   getGUID  (  boolean   secure  )  { 
return   new   RandomGUID  (  secure  )  .  toString  (  )  ; 
} 
} 

