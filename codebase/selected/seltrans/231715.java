package   com  .  gpfcomics  .  android  .  ppp  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  SecureRandom  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   javax  .  crypto  .  Cipher  ; 
import   org  .  bouncycastle  .  crypto  .  BufferedBlockCipher  ; 
import   org  .  bouncycastle  .  crypto  .  engines  .  RijndaelEngine  ; 
import   org  .  bouncycastle  .  crypto  .  generators  .  PKCS5S2ParametersGenerator  ; 
import   org  .  bouncycastle  .  crypto  .  modes  .  CBCBlockCipher  ; 
import   org  .  bouncycastle  .  crypto  .  paddings  .  PaddedBufferedBlockCipher  ; 
import   org  .  bouncycastle  .  crypto  .  params  .  ParametersWithIV  ; 
import   android  .  app  .  Application  ; 
import   android  .  content  .  Context  ; 
import   android  .  content  .  SharedPreferences  ; 
import   android  .  content  .  pm  .  PackageInfo  ; 
import   android  .  content  .  pm  .  PackageManager  ; 









public   class   PPPApplication   extends   Application  { 


private   static   final   String   PREF_FILE  =  "PPPPrefs"  ; 



private   static   final   String   PREF_VERSION  =  "version"  ; 




private   static   final   String   PREF_PASSWORD  =  "password"  ; 




private   static   final   String   PREF_COPY_PASSCODES_TO_CLIPBOARD  =  "pass_to_clip"  ; 



private   static   final   String   PREF_SALT  =  "salt"  ; 




private   static   final   int   KEY_ITERATION_COUNT  =  50  ; 




private   static   final   String   SALT_HASH  =  "SHA-512"  ; 


private   static   final   String   SALT  =  "cSg6Vo1mV3hsENK6njMIkr8adrZ4lbGByu8fd8PClRknqhEC8DOmbDCtgUAtbir"  ; 



private   static   final   String   ENCODING  =  "UTF-8"  ; 


private   static   final   int   KEY_SIZE  =  256  ; 


private   static   final   int   IV_SIZE  =  128  ; 



private   static   CardDBAdapter   DBHelper  =  null  ; 



private   static   SharedPreferences   prefs  =  null  ; 



private   static   boolean   copyPasscodes  =  true  ; 


private   static   int   versionCode  =  -  1  ; 


private   static   String   versionName  =  null  ; 


private   static   BufferedBlockCipher   cipher  =  null  ; 


private   static   ParametersWithIV   iv  =  null  ; 

private   byte  [  ]  salt  =  null  ; 

@  Override 
public   void   onCreate  (  )  { 
super  .  onCreate  (  )  ; 
DBHelper  =  new   CardDBAdapter  (  this  )  ; 
DBHelper  .  open  (  )  ; 
try  { 
prefs  =  getSharedPreferences  (  PREF_FILE  ,  Context  .  MODE_PRIVATE  )  ; 
PackageInfo   info  =  getPackageManager  (  )  .  getPackageInfo  (  getPackageName  (  )  ,  PackageManager  .  GET_META_DATA  )  ; 
versionCode  =  info  .  versionCode  ; 
versionName  =  info  .  versionName  ; 
int   oldVersion  =  prefs  .  getInt  (  PREF_VERSION  ,  -  1  )  ; 
if  (  oldVersion  ==  -  1  )  { 
SharedPreferences  .  Editor   editor  =  prefs  .  edit  (  )  ; 
editor  .  putInt  (  PREF_VERSION  ,  versionCode  )  ; 
editor  .  putBoolean  (  PREF_COPY_PASSCODES_TO_CLIPBOARD  ,  copyPasscodes  )  ; 
editor  .  commit  (  )  ; 
}  else   if  (  oldVersion  >  versionCode  )  { 
}  else   if  (  versionCode  >  oldVersion  )  { 
} 
copyPasscodes  =  prefs  .  getBoolean  (  PREF_COPY_PASSCODES_TO_CLIPBOARD  ,  true  )  ; 
String   saltString  =  prefs  .  getString  (  PREF_SALT  ,  null  )  ; 
if  (  saltString  ==  null  )  { 
SecureRandom   devRandom  =  new   SecureRandom  (  )  ; 
salt  =  new   byte  [  512  ]  ; 
devRandom  .  nextBytes  (  salt  )  ; 
SharedPreferences  .  Editor   editor  =  prefs  .  edit  (  )  ; 
editor  .  putString  (  PREF_SALT  ,  bytesToHexString  (  salt  )  )  ; 
editor  .  commit  (  )  ; 
}  else  { 
salt  =  hexStringToBytes  (  saltString  )  ; 
} 
if  (  promptForPassword  (  )  )  createCipher  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 





public   CardDBAdapter   getDBHelper  (  )  { 
return   DBHelper  ; 
} 






public   boolean   promptForPassword  (  )  { 
String   password  =  prefs  .  getString  (  PREF_PASSWORD  ,  null  )  ; 
return   password  !=  null  ; 
} 







public   boolean   isValidPassword  (  String   password  )  { 
try  { 
String   stored_password  =  prefs  .  getString  (  PREF_PASSWORD  ,  null  )  ; 
if  (  stored_password  ==  null  )  return   false  ; 
String   enc_password  =  encryptPassword  (  password  )  ; 
return   enc_password  .  compareTo  (  stored_password  )  ==  0  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 






public   boolean   copyPasscodesToClipboard  (  )  { 
return   copyPasscodes  ; 
} 






public   boolean   toggleCopyPasscodesSetting  (  )  { 
try  { 
copyPasscodes  =  !  copyPasscodes  ; 
SharedPreferences  .  Editor   editor  =  prefs  .  edit  (  )  ; 
editor  .  putBoolean  (  PREF_COPY_PASSCODES_TO_CLIPBOARD  ,  copyPasscodes  )  ; 
editor  .  commit  (  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 







public   static   String   bytesToHexString  (  byte  [  ]  bytes  )  { 
if  (  bytes  ==  null  )  return   null  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  byte   b  :  bytes  )  sb  .  append  (  String  .  format  (  "%1$02X"  ,  b  )  )  ; 
return   sb  .  toString  (  )  .  toUpperCase  (  )  ; 
} 










public   static   byte  [  ]  hexStringToBytes  (  String   hex  )  { 
if  (  hex  ==  null  ||  hex  .  length  (  )  ==  0  ||  hex  .  length  (  )  %  2  !=  0  )  throw   new   IllegalArgumentException  (  "Invalid hexadecimal string"  )  ; 
if  (  !  Pattern  .  matches  (  "^[0-9a-fA-F]+$"  ,  hex  )  )  throw   new   IllegalArgumentException  (  "Invalid hexadecimal string"  )  ; 
int   outputSize  =  hex  .  length  (  )  /  2  ; 
byte  [  ]  out  =  new   byte  [  outputSize  ]  ; 
String   temp  =  null  ; 
for  (  int   i  =  0  ;  i  <  outputSize  ;  ++  i  )  { 
temp  =  hex  .  substring  (  i  *  2  ,  i  *  2  +  2  )  ; 
try  { 
int   b  =  Integer  .  parseInt  (  temp  ,  16  )  ; 
out  [  i  ]  =  (  byte  )  b  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   IllegalArgumentException  (  "Invalid hexadecimal string"  )  ; 
} 
} 
return   out  ; 
} 





public   static   int   getVersionCode  (  )  { 
return   versionCode  ; 
} 





public   static   String   getVersionName  (  )  { 
return   versionName  ; 
} 






boolean   setPassword  (  String   password  )  { 
try  { 
SharedPreferences  .  Editor   editor  =  prefs  .  edit  (  )  ; 
editor  .  putString  (  PREF_PASSWORD  ,  encryptPassword  (  password  )  )  ; 
editor  .  commit  (  )  ; 
createCipher  (  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 






boolean   clearPassword  (  )  { 
try  { 
SharedPreferences  .  Editor   editor  =  prefs  .  edit  (  )  ; 
editor  .  remove  (  PREF_PASSWORD  )  ; 
editor  .  commit  (  )  ; 
createCipher  (  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 








String   encryptSequenceKey  (  String   original  )  { 
try  { 
byte  [  ]  output  =  cryptSeqKey  (  original  .  getBytes  (  ENCODING  )  ,  Cipher  .  ENCRYPT_MODE  )  ; 
if  (  output  ==  null  )  return   original  ;  else   return   bytesToHexString  (  output  )  ; 
}  catch  (  Exception   e  )  { 
return   original  ; 
} 
} 








String   decryptSequenceKey  (  String   original  )  { 
try  { 
byte  [  ]  output  =  cryptSeqKey  (  hexStringToBytes  (  original  )  ,  Cipher  .  DECRYPT_MODE  )  ; 
if  (  output  ==  null  )  return   original  ;  else  { 
String   outString  =  new   String  (  output  ,  ENCODING  )  ; 
return   outString  .  trim  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
return   original  ; 
} 
} 






private   static   String   encryptPassword  (  String   password  )  { 
try  { 
MessageDigest   hasher  =  MessageDigest  .  getInstance  (  SALT_HASH  )  ; 
byte  [  ]  digest  =  hasher  .  digest  (  password  .  getBytes  (  ENCODING  )  )  ; 
for  (  int   i  =  0  ;  i  <  9  ;  i  ++  )  digest  =  hasher  .  digest  (  digest  )  ; 
return   bytesToHexString  (  digest  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






private   void   createCipher  (  )  { 
try  { 
String   password  =  prefs  .  getString  (  PREF_PASSWORD  ,  null  )  ; 
if  (  password  !=  null  )  { 
String   uniqueID  =  null  ; 
try  { 
AndroidID   id  =  AndroidID  .  newInstance  (  this  )  ; 
uniqueID  =  id  .  getAndroidID  (  )  ; 
}  catch  (  Exception   e1  )  { 
} 
if  (  uniqueID  ==  null  )  uniqueID  =  SALT  ;  else   uniqueID  =  uniqueID  .  concat  (  SALT  )  ; 
byte  [  ]  uniqueIDBytes  =  uniqueID  .  getBytes  (  ENCODING  )  ; 
byte  [  ]  finalSalt  =  new   byte  [  uniqueIDBytes  .  length  +  salt  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  uniqueIDBytes  .  length  ;  i  ++  )  { 
finalSalt  [  i  ]  =  uniqueIDBytes  [  i  ]  ; 
} 
for  (  int   j  =  0  ;  j  <  salt  .  length  ;  j  ++  )  { 
finalSalt  [  uniqueIDBytes  .  length  +  j  ]  =  salt  [  j  ]  ; 
} 
MessageDigest   hasher  =  MessageDigest  .  getInstance  (  SALT_HASH  )  ; 
for  (  int   i  =  0  ;  i  <  KEY_ITERATION_COUNT  ;  i  ++  )  finalSalt  =  hasher  .  digest  (  finalSalt  )  ; 
byte  [  ]  pwd  =  password  .  concat  (  uniqueID  )  .  getBytes  (  ENCODING  )  ; 
for  (  int   i  =  0  ;  i  <  KEY_ITERATION_COUNT  ;  i  ++  )  pwd  =  hasher  .  digest  (  pwd  )  ; 
PKCS5S2ParametersGenerator   generator  =  new   PKCS5S2ParametersGenerator  (  )  ; 
generator  .  init  (  pwd  ,  finalSalt  ,  KEY_ITERATION_COUNT  )  ; 
iv  =  (  (  ParametersWithIV  )  generator  .  generateDerivedParameters  (  KEY_SIZE  ,  IV_SIZE  )  )  ; 
RijndaelEngine   engine  =  new   RijndaelEngine  (  )  ; 
cipher  =  new   PaddedBufferedBlockCipher  (  new   CBCBlockCipher  (  engine  )  )  ; 
}  else  { 
cipher  =  null  ; 
iv  =  null  ; 
} 
}  catch  (  Exception   e  )  { 
cipher  =  null  ; 
iv  =  null  ; 
} 
} 





















private   byte  [  ]  cryptSeqKey  (  byte  [  ]  original  ,  int   mode  )  { 
if  (  original  ==  null  ||  original  .  length  ==  0  ||  cipher  ==  null  )  return   null  ; 
try  { 
if  (  mode  ==  Cipher  .  ENCRYPT_MODE  )  cipher  .  init  (  true  ,  iv  )  ;  else   cipher  .  init  (  false  ,  iv  )  ; 
byte  [  ]  result  =  new   byte  [  cipher  .  getOutputSize  (  original  .  length  )  ]  ; 
int   bytesSoFar  =  cipher  .  processBytes  (  original  ,  0  ,  original  .  length  ,  result  ,  0  )  ; 
cipher  .  doFinal  (  result  ,  bytesSoFar  )  ; 
return   result  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
} 

