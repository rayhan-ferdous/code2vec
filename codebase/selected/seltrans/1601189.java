package   er  .  extensions  .  crypting  ; 

import   java  .  io  .  IOException  ; 
import   java  .  security  .  Key  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  util  .  Enumeration  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   com  .  webobjects  .  foundation  .  NSArray  ; 
import   com  .  webobjects  .  foundation  .  NSDictionary  ; 
import   com  .  webobjects  .  foundation  .  NSForwardException  ; 
import   com  .  webobjects  .  foundation  .  NSMutableDictionary  ; 
import   er  .  extensions  .  foundation  .  ERXProperties  ; 
import   er  .  extensions  .  foundation  .  ERXStringUtilities  ; 

















public   class   ERXCrypto  { 


public   static   final   Logger   log  =  Logger  .  getLogger  (  ERXCrypto  .  class  )  ; 




public   static   final   String   DES  =  "DES"  ; 




public   static   final   String   BLOWFISH  =  "Blowfish"  ; 




public   static   final   String   AES  =  "AES"  ; 

private   static   NSMutableDictionary  <  String  ,  ERXCrypterInterface  >  _crypters  ; 

private   static   synchronized   NSMutableDictionary  <  String  ,  ERXCrypterInterface  >  crypters  (  )  { 
if  (  _crypters  ==  null  )  { 
_crypters  =  new   NSMutableDictionary  <  String  ,  ERXCrypterInterface  >  (  )  ; 
_crypters  .  setObjectForKey  (  new   ERXDESCrypter  (  )  ,  ERXCrypto  .  DES  )  ; 
_crypters  .  setObjectForKey  (  new   ERXBlowfishCrypter  (  )  ,  ERXCrypto  .  BLOWFISH  )  ; 
_crypters  .  setObjectForKey  (  new   ERXAESCrypter  (  )  ,  ERXCrypto  .  AES  )  ; 
NSArray  <  String  >  crypterAlgorithms  =  ERXProperties  .  componentsSeparatedByString  (  "er.extensions.ERXCrypto.crypters"  ,  ","  )  ; 
if  (  crypterAlgorithms  !=  null  )  { 
for  (  String   crypterAlgorithm  :  crypterAlgorithms  )  { 
String   crypterClassName  =  ERXProperties  .  stringForKey  (  "er.extensions.ERXCrypto.crypter."  +  crypterAlgorithm  )  ; 
if  (  crypterClassName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "You did not provide a crypter class definition for 'er.extensions.ERXCrypto.crypter."  +  crypterAlgorithm  +  "'."  )  ; 
} 
try  { 
ERXCrypterInterface   crypter  =  Class  .  forName  (  crypterClassName  )  .  asSubclass  (  ERXCrypterInterface  .  class  )  .  newInstance  (  )  ; 
_crypters  .  setObjectForKey  (  crypter  ,  crypterAlgorithm  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   NSForwardException  (  e  ,  "Failed to create "  +  crypterAlgorithm  +  " crypter '"  +  crypterClassName  +  "'."  )  ; 
} 
} 
} 
} 
return   _crypters  ; 
} 







public   static   ERXCrypterInterface   defaultCrypter  (  )  { 
String   defaultCrypterAlgorithm  =  ERXProperties  .  stringForKeyWithDefault  (  "er.extensions.ERXCrypto.default"  ,  ERXCrypto  .  BLOWFISH  )  ; 
return   ERXCrypto  .  crypterForAlgorithm  (  defaultCrypterAlgorithm  )  ; 
} 









public   static   void   setCrypterForAlgorithm  (  ERXCrypterInterface   crypter  ,  String   algorithm  )  { 
NSMutableDictionary  <  String  ,  ERXCrypterInterface  >  crypters  =  ERXCrypto  .  crypters  (  )  ; 
crypters  .  setObjectForKey  (  crypter  ,  algorithm  )  ; 
} 











public   static   ERXCrypterInterface   crypterForAlgorithm  (  String   algorithm  )  { 
NSMutableDictionary  <  String  ,  ERXCrypterInterface  >  crypters  =  ERXCrypto  .  crypters  (  )  ; 
ERXCrypterInterface   crypter  =  crypters  .  objectForKey  (  algorithm  )  ; 
if  (  crypter  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Unknown encryption algorithm '"  +  algorithm  +  "'."  )  ; 
} 
return   crypter  ; 
} 










public   static   NSMutableDictionary  <  String  ,  String  >  decodedFormValuesDictionary  (  NSDictionary  <  String  ,  NSArray  <  String  >  >  dict  )  { 
NSMutableDictionary  <  String  ,  String  >  result  =  new   NSMutableDictionary  <  String  ,  String  >  (  )  ; 
for  (  Enumeration   e  =  dict  .  allKeys  (  )  .  objectEnumerator  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   key  =  (  String  )  e  .  nextElement  (  )  ; 
NSArray  <  String  >  objects  =  dict  .  objectForKey  (  key  )  ; 
String   value  =  ERXCrypto  .  defaultCrypter  (  )  .  decrypt  (  objects  .  lastObject  (  )  )  .  trim  (  )  ; 
result  .  setObjectForKey  (  value  ,  key  )  ; 
} 
return   result  ; 
} 





public   static   String   base64HashedString  (  String   v  )  { 
String   base64HashedPassword  =  null  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "SHA"  )  ; 
md  .  update  (  v  .  getBytes  (  )  )  ; 
String   hashedPassword  =  new   String  (  md  .  digest  (  )  )  ; 
sun  .  misc  .  BASE64Encoder   enc  =  new   sun  .  misc  .  BASE64Encoder  (  )  ; 
base64HashedPassword  =  enc  .  encode  (  hashedPassword  .  getBytes  (  )  )  ; 
}  catch  (  java  .  security  .  NoSuchAlgorithmException   e  )  { 
throw   new   NSForwardException  (  e  ,  "Couldn't find the SHA hash algorithm; perhaps you do not have the SunJCE security provider installed properly?"  )  ; 
} 
return   base64HashedPassword  ; 
} 











public   static   String   shaEncode  (  String   text  )  { 
if  (  text  ==  null  )  { 
return   text  ; 
} 
byte  [  ]  buf  =  text  .  getBytes  (  )  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "SHA"  )  ; 
md  .  update  (  buf  )  ; 
return   ERXStringUtilities  .  byteArrayToHexString  (  md  .  digest  (  )  )  ; 
}  catch  (  java  .  security  .  NoSuchAlgorithmException   ex  )  { 
throw   new   NSForwardException  (  ex  ,  "Couldn't find the SHA algorithm; perhaps you do not have the SunJCE security provider installed properly?"  )  ; 
} 
} 




public   static   String   base64Encode  (  byte  [  ]  byteArray  )  { 
sun  .  misc  .  BASE64Encoder   enc  =  new   sun  .  misc  .  BASE64Encoder  (  )  ; 
String   base64String  =  enc  .  encode  (  byteArray  )  ; 
return   base64String  ; 
} 




public   static   String   base64urlEncode  (  byte  [  ]  byteArray  )  { 
String   base64String  =  base64Encode  (  byteArray  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  base64String  .  length  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  base64String  .  length  (  )  ;  i  ++  )  { 
char   ch  =  base64String  .  charAt  (  i  )  ; 
if  (  ch  ==  '+'  )  { 
sb  .  append  (  '-'  )  ; 
}  else   if  (  ch  ==  '/'  )  { 
sb  .  append  (  '_'  )  ; 
}  else   if  (  ch  ==  '\r'  ||  ch  ==  '\n'  )  { 
}  else   if  (  ch  !=  '='  )  { 
sb  .  append  (  ch  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   byte  [  ]  base64Decode  (  String   s  )  throws   IOException  { 
sun  .  misc  .  BASE64Decoder   enc  =  new   sun  .  misc  .  BASE64Decoder  (  )  ; 
byte  [  ]  raw  =  enc  .  decodeBuffer  (  s  )  ; 
return   raw  ; 
} 




public   static   byte  [  ]  base64urlDecode  (  String   s  )  throws   IOException  { 
int   length  =  s  .  length  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  length  )  ; 
int   i  =  0  ; 
while  (  i  <  length  ||  (  i  &  2  )  !=  0  )  { 
if  (  i  >=  length  )  { 
sb  .  append  (  '='  )  ; 
}  else  { 
char   ch  =  s  .  charAt  (  i  )  ; 
if  (  ch  ==  '-'  )  { 
sb  .  append  (  '+'  )  ; 
}  else   if  (  ch  ==  '_'  )  { 
sb  .  append  (  '/'  )  ; 
}  else  { 
sb  .  append  (  ch  )  ; 
} 
} 
i  ++  ; 
} 
return   base64Decode  (  sb  .  toString  (  )  )  ; 
} 





@  Deprecated 
public   static   String   bytesToString  (  byte  [  ]  bytes  )  { 
return   ERXStringUtilities  .  byteArrayToHexString  (  bytes  )  ; 
} 





@  Deprecated 
public   static   String   base64EncryptedString  (  String   clearText  )  { 
return   ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  .  encrypt  (  clearText  )  ; 
} 





@  Deprecated 
public   static   String   base64EncryptedString  (  String   clearText  ,  Key   secretKey  )  { 
return  (  (  ERXDESCrypter  )  ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  )  .  encrypt  (  clearText  ,  secretKey  )  ; 
} 





@  Deprecated 
public   static   String   decryptedBase64String  (  String   encryptedText  )  { 
return   ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  .  decrypt  (  encryptedText  )  ; 
} 





@  Deprecated 
public   static   String   decryptedBase64String  (  String   encryptedText  ,  Key   secretKey  )  { 
return  (  (  ERXDESCrypter  )  ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  )  .  decrypt  (  encryptedText  ,  secretKey  )  ; 
} 





@  Deprecated 
public   static   String   blowfishEncode  (  String   clearText  )  { 
return   ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  BLOWFISH  )  .  encrypt  (  clearText  )  ; 
} 





@  Deprecated 
public   static   String   blowfishDecode  (  String   encryptedText  )  { 
return   ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  BLOWFISH  )  .  decrypt  (  encryptedText  )  ; 
} 





@  Deprecated 
public   static   void   setSecretKeyPathFramework  (  String   secretKeyPathFramework  )  { 
(  (  ERXDESCrypter  )  ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  )  .  setSecretKeyPathFramework  (  secretKeyPathFramework  )  ; 
} 





@  Deprecated 
public   static   void   setSecretKeyPath  (  String   secretKeyPath  )  { 
(  (  ERXDESCrypter  )  ERXCrypto  .  crypterForAlgorithm  (  ERXCrypto  .  DES  )  )  .  setSecretKeyPath  (  secretKeyPath  )  ; 
} 









public   static   void   main  (  String  [  ]  args  )  { 
if  (  args  .  length  ==  0  )  { 
System  .  out  .  println  (  "Usage: ERXCrypto [plaintext]"  )  ; 
System  .  out  .  println  (  "       returns the encrypted form of the given plaintext using the default crypter"  )  ; 
System  .  exit  (  0  )  ; 
} 
String   plaintext  =  args  [  0  ]  ; 
String   encrypted  =  ERXCrypto  .  defaultCrypter  (  )  .  encrypt  (  plaintext  )  ; 
System  .  out  .  println  (  "ERXCrypto.main: Encrypted form of '"  +  plaintext  +  "' is '"  +  encrypted  +  "'"  )  ; 
} 
} 

