package   com  .  gpfcomics  .  android  .  cryptnos  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  spec  .  AlgorithmParameterSpec  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   javax  .  crypto  .  Cipher  ; 
import   javax  .  crypto  .  SecretKey  ; 
import   javax  .  crypto  .  SecretKeyFactory  ; 
import   javax  .  crypto  .  spec  .  PBEKeySpec  ; 
import   javax  .  crypto  .  spec  .  PBEParameterSpec  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   org  .  bouncycastle  .  crypto  .  BufferedBlockCipher  ; 
import   org  .  bouncycastle  .  crypto  .  engines  .  RijndaelEngine  ; 
import   org  .  bouncycastle  .  crypto  .  generators  .  PKCS5S2ParametersGenerator  ; 
import   org  .  bouncycastle  .  crypto  .  modes  .  CBCBlockCipher  ; 
import   org  .  bouncycastle  .  crypto  .  params  .  ParametersWithIV  ; 
import   org  .  bouncycastle  .  crypto  .  paddings  .  PaddedBufferedBlockCipher  ; 
import   org  .  xml  .  sax  .  Attributes  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 
import   android  .  app  .  Activity  ; 
import   android  .  app  .  ActivityManager  ; 
import   android  .  app  .  ActivityManager  .  MemoryInfo  ; 
import   android  .  app  .  ProgressDialog  ; 
import   android  .  content  .  Context  ; 
import   android  .  content  .  pm  .  PackageInfo  ; 
import   android  .  content  .  pm  .  PackageManager  ; 
import   android  .  database  .  Cursor  ; 
import   android  .  os  .  Bundle  ; 
import   android  .  os  .  Handler  ; 
import   android  .  os  .  Message  ; 
import   android  .  text  .  TextUtils  ; 
import   android  .  widget  .  Toast  ; 












public   class   ImportExportHandler  { 






private   static   final   int   SALT_ITERATION_COUNT  =  10  ; 


private   static   final   int   KEY_ITERATION_COUNT  =  100  ; 


private   static   final   int   KEY_SIZE  =  256  ; 


private   static   final   int   IV_SIZE  =  128  ; 


private   CryptnosApplication   theApp  =  null  ; 


private   Activity   caller  =  null  ; 


private   ImportListener   importListener  =  null  ; 


private   ParamsDbAdapter   DBHelper  =  null  ; 


private   ProgressDialog   progressDialog  =  null  ; 



private   int   progressDialogID  =  0  ; 


private   Exporter   exporter  =  null  ; 



private   OldFormatImporter   oldFormatImporter  =  null  ; 



private   XMLFormat1Importer   xmlFormatImporter  =  null  ; 


private   String   importFilename  =  null  ; 


private   String   importPassword  =  null  ; 




private   Object  [  ]  importedSites  =  null  ; 













public   ImportExportHandler  (  Activity   caller  ,  ProgressDialog   progressDialog  ,  int   progressDialogID  )  { 
this  .  caller  =  caller  ; 
this  .  progressDialog  =  progressDialog  ; 
this  .  progressDialogID  =  progressDialogID  ; 
theApp  =  (  CryptnosApplication  )  caller  .  getApplication  (  )  ; 
DBHelper  =  theApp  .  getDBHelper  (  )  ; 
} 










public   void   exportToFile  (  String   filename  ,  String   password  ,  String  [  ]  sites  )  { 
if  (  filename  !=  null  &&  password  !=  null  &&  sites  !=  null  &&  sites  .  length  >  0  )  { 
exporter  =  new   Exporter  (  caller  ,  handler  ,  sites  ,  password  ,  filename  ,  theApp  )  ; 
exporter  .  start  (  )  ; 
}  else  { 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_export_params  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
} 
} 













public   void   importFromFile  (  String   filename  ,  String   password  ,  ImportListener   importListener  )  { 
if  (  filename  !=  null  &&  password  !=  null  )  { 
this  .  importListener  =  importListener  ; 
importFilename  =  filename  ; 
importPassword  =  password  ; 
xmlFormatImporter  =  new   XMLFormat1Importer  (  handler  ,  password  ,  filename  ,  caller  )  ; 
xmlFormatImporter  .  start  (  )  ; 
}  else  { 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_import_params  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
} 
} 














private   static   Cipher   createOldFormatCipher  (  String   password  ,  int   mode  )  throws   Exception  { 
try  { 
byte  [  ]  salt  =  password  .  getBytes  (  )  ; 
MessageDigest   hasher  =  MessageDigest  .  getInstance  (  "SHA-512"  )  ; 
for  (  int   i  =  0  ;  i  <  CryptnosApplication  .  SALT_ITERATION_COUNT  ;  i  ++  )  salt  =  hasher  .  digest  (  salt  )  ; 
PBEKeySpec   pbeKeySpec  =  new   PBEKeySpec  (  password  .  toCharArray  (  )  ,  salt  ,  CryptnosApplication  .  KEY_ITERATION_COUNT  ,  CryptnosApplication  .  KEY_LENGTH  )  ; 
SecretKeyFactory   keyFac  =  SecretKeyFactory  .  getInstance  (  CryptnosApplication  .  KEY_FACTORY  )  ; 
SecretKey   key  =  keyFac  .  generateSecret  (  pbeKeySpec  )  ; 
AlgorithmParameterSpec   aps  =  new   PBEParameterSpec  (  salt  ,  CryptnosApplication  .  KEY_ITERATION_COUNT  )  ; 
Cipher   cipher  =  Cipher  .  getInstance  (  CryptnosApplication  .  KEY_FACTORY  )  ; 
switch  (  mode  )  { 
case   Cipher  .  ENCRYPT_MODE  : 
cipher  .  init  (  Cipher  .  ENCRYPT_MODE  ,  key  ,  aps  )  ; 
break  ; 
case   Cipher  .  DECRYPT_MODE  : 
cipher  .  init  (  Cipher  .  DECRYPT_MODE  ,  key  ,  aps  )  ; 
break  ; 
default  : 
throw   new   Exception  (  "Invalid cipher mode"  )  ; 
} 
return   cipher  ; 
}  catch  (  Exception   e  )  { 
throw   e  ; 
} 
} 







private   static   byte  [  ]  generateSaltFromPassword  (  String   password  ,  CryptnosApplication   theApp  )  throws   Exception  { 
byte  [  ]  salt  =  password  .  getBytes  (  CryptnosApplication  .  TEXT_ENCODING_UTF8  )  ; 
MessageDigest   hasher  =  MessageDigest  .  getInstance  (  "SHA-512"  )  ; 
for  (  int   i  =  0  ;  i  <  SALT_ITERATION_COUNT  ;  i  ++  )  { 
salt  =  hasher  .  digest  (  salt  )  ; 
} 
return   salt  ; 
} 











private   static   BufferedBlockCipher   createXMLFormatCipher  (  String   password  ,  boolean   encrypt  ,  CryptnosApplication   theApp  )  throws   Exception  { 
try  { 
byte  [  ]  pwd  =  password  .  getBytes  (  CryptnosApplication  .  TEXT_ENCODING_UTF8  )  ; 
byte  [  ]  salt  =  generateSaltFromPassword  (  password  ,  theApp  )  ; 
PKCS5S2ParametersGenerator   generator  =  new   PKCS5S2ParametersGenerator  (  )  ; 
generator  .  init  (  pwd  ,  salt  ,  KEY_ITERATION_COUNT  )  ; 
ParametersWithIV   iv  =  (  (  ParametersWithIV  )  generator  .  generateDerivedParameters  (  KEY_SIZE  ,  IV_SIZE  )  )  ; 
RijndaelEngine   engine  =  new   RijndaelEngine  (  )  ; 
BufferedBlockCipher   cipher  =  new   PaddedBufferedBlockCipher  (  new   CBCBlockCipher  (  engine  )  )  ; 
cipher  .  init  (  encrypt  ,  iv  )  ; 
return   cipher  ; 
}  catch  (  Exception   e  )  { 
throw   e  ; 
} 
} 











private   static   boolean   haveSufficientMemory  (  BufferedBlockCipher   cipher  ,  boolean   encrypting  ,  long   fileSize  ,  Activity   caller  )  { 
if  (  fileSize  >  (  long  )  Integer  .  MAX_VALUE  )  return   false  ; 
if  (  cipher  ==  null  ||  caller  ==  null  )  return   false  ; 
try  { 
MemoryInfo   mi  =  new   MemoryInfo  (  )  ; 
ActivityManager   activityManager  =  (  ActivityManager  )  caller  .  getSystemService  (  Context  .  ACTIVITY_SERVICE  )  ; 
activityManager  .  getMemoryInfo  (  mi  )  ; 
if  (  encrypting  )  return   mi  .  availMem  >  fileSize  +  (  long  )  cipher  .  getOutputSize  (  (  int  )  fileSize  )  ;  else   return   mi  .  availMem  >  (  long  )  cipher  .  getBlockSize  (  )  +  (  long  )  cipher  .  getOutputSize  (  (  int  )  fileSize  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 











private   static   boolean   haveSufficientMemory  (  Cipher   cipher  ,  long   fileSize  ,  Activity   caller  )  { 
if  (  fileSize  >  (  long  )  Integer  .  MAX_VALUE  )  return   false  ; 
if  (  cipher  ==  null  ||  caller  ==  null  )  return   false  ; 
try  { 
MemoryInfo   mi  =  new   MemoryInfo  (  )  ; 
ActivityManager   activityManager  =  (  ActivityManager  )  caller  .  getSystemService  (  Context  .  ACTIVITY_SERVICE  )  ; 
activityManager  .  getMemoryInfo  (  mi  )  ; 
return   mi  .  availMem  >  fileSize  +  (  long  )  cipher  .  getOutputSize  (  (  int  )  fileSize  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 











private   final   Handler   handler  =  new   Handler  (  )  { 

public   void   handleMessage  (  Message   msg  )  { 
int   total  =  msg  .  getData  (  )  .  getInt  (  "percent_done"  )  ; 
if  (  total  >=  0  )  progressDialog  .  setProgress  (  total  )  ; 
int   count  =  msg  .  getData  (  )  .  getInt  (  "site_count"  )  ; 
if  (  total  >=  100  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
String   message  =  null  ; 
if  (  oldFormatImporter  !=  null  ||  xmlFormatImporter  !=  null  )  { 
importListener  .  onSitesImported  (  importedSites  )  ; 
}  else  { 
message  =  caller  .  getResources  (  )  .  getString  (  R  .  string  .  export_complete_message  )  ; 
message  =  message  .  replace  (  caller  .  getResources  (  )  .  getString  (  R  .  string  .  meta_replace_token  )  ,  String  .  valueOf  (  count  )  )  ; 
Toast  .  makeText  (  caller  ,  message  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
caller  .  finish  (  )  ; 
} 
}  else   if  (  total  ==  -  1  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_import_file_or_password  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
}  else   if  (  total  ==  -  2  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_import_file  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
}  else   if  (  total  ==  -  3  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_export  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
}  else   if  (  total  ==  -  4  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_bad_export_params  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
}  else   if  (  total  ==  -  5  )  { 
caller  .  removeDialog  (  progressDialogID  )  ; 
Toast  .  makeText  (  caller  ,  R  .  string  .  error_insufficient_memory  ,  Toast  .  LENGTH_LONG  )  .  show  (  )  ; 
}  else   if  (  total  ==  -  1000  )  { 
oldFormatImporter  =  new   OldFormatImporter  (  handler  ,  importPassword  ,  importFilename  ,  caller  )  ; 
oldFormatImporter  .  start  (  )  ; 
} 
} 
}  ; 









private   class   Exporter   extends   Thread  { 


private   Handler   mHandler  ; 


private   Activity   mCaller  ; 


private   String   mPassword  ; 


private   String   mFilename  ; 



private   String  [  ]  mSites  =  null  ; 

private   CryptnosApplication   theApp  =  null  ; 










Exporter  (  Activity   caller  ,  Handler   handler  ,  String  [  ]  sites  ,  String   password  ,  String   filename  ,  CryptnosApplication   app  )  { 
mCaller  =  caller  ; 
mHandler  =  handler  ; 
mSites  =  sites  ; 
mPassword  =  password  ; 
mFilename  =  filename  ; 
theApp  =  app  ; 
} 

@  Override 
public   void   run  (  )  { 
Message   msg  =  null  ; 
Bundle   b  =  null  ; 
if  (  mSites  .  length  >  0  )  { 
try  { 
ByteArrayOutputStream   ms  =  new   ByteArrayOutputStream  (  )  ; 
PrintStream   out  =  new   PrintStream  (  new   GZIPOutputStream  (  ms  )  ,  true  ,  CryptnosApplication  .  TEXT_ENCODING_UTF8  )  ; 
out  .  println  (  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"  )  ; 
out  .  println  (  "<cryptnos xmlns=\"http://www.cryptnos.com/\">"  )  ; 
out  .  println  (  "\t<version>1</version>"  )  ; 
try  { 
PackageInfo   info  =  theApp  .  getPackageManager  (  )  .  getPackageInfo  (  theApp  .  getPackageName  (  )  ,  PackageManager  .  GET_META_DATA  )  ; 
out  .  println  (  "\t<generator>Cryptnos for Android v"  +  info  .  versionName  +  "</generator>"  )  ; 
}  catch  (  Exception   e  )  { 
} 
out  .  println  (  "\t<siteCount>"  +  String  .  valueOf  (  mSites  .  length  )  +  "</siteCount>"  )  ; 
out  .  println  (  "\t<sites>"  )  ; 
Cursor   cursor  =  null  ; 
for  (  int   i  =  0  ;  i  <  mSites  .  length  ;  i  ++  )  { 
cursor  =  DBHelper  .  fetchRecord  (  SiteParameters  .  generateKeyFromSite  (  mSites  [  i  ]  ,  theApp  )  )  ; 
cursor  .  moveToFirst  (  )  ; 
if  (  cursor  .  getCount  (  )  ==  1  )  { 
SiteParameters   params  =  new   SiteParameters  (  theApp  ,  cursor  .  getString  (  1  )  ,  cursor  .  getString  (  2  )  )  ; 
out  .  println  (  "\t\t<site>"  )  ; 
out  .  println  (  "\t\t\t<siteToken>"  +  TextUtils  .  htmlEncode  (  params  .  getSite  (  )  )  +  "</siteToken>"  )  ; 
out  .  println  (  "\t\t\t<hash>"  +  TextUtils  .  htmlEncode  (  params  .  getHash  (  )  )  +  "</hash>"  )  ; 
out  .  println  (  "\t\t\t<iterations>"  +  String  .  valueOf  (  params  .  getIterations  (  )  )  +  "</iterations>"  )  ; 
out  .  println  (  "\t\t\t<charTypes>"  +  String  .  valueOf  (  params  .  getCharTypes  (  )  )  +  "</charTypes>"  )  ; 
out  .  println  (  "\t\t\t<charLimit>"  +  String  .  valueOf  (  params  .  getCharLimit  (  )  )  +  "</charLimit>"  )  ; 
out  .  println  (  "\t\t</site>"  )  ; 
} 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  (  int  )  (  Math  .  floor  (  (  (  double  )  i  /  (  double  )  mSites  .  length  *  90.0d  )  )  )  )  ; 
b  .  putInt  (  "site_count"  ,  mSites  .  length  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
cursor  .  close  (  )  ; 
} 
out  .  println  (  "\t</sites>"  )  ; 
out  .  println  (  "</cryptnos>"  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
byte  [  ]  plaintext  =  ms  .  toByteArray  (  )  ; 
ms  .  close  (  )  ; 
ms  =  null  ; 
out  =  null  ; 
BufferedBlockCipher   cipher  =  createXMLFormatCipher  (  mPassword  ,  true  ,  theApp  )  ; 
if  (  ImportExportHandler  .  haveSufficientMemory  (  cipher  ,  true  ,  (  long  )  plaintext  .  length  ,  mCaller  )  )  { 
byte  [  ]  ciphertext  =  new   byte  [  cipher  .  getOutputSize  (  plaintext  .  length  )  ]  ; 
int   bytesSoFar  =  cipher  .  processBytes  (  plaintext  ,  0  ,  plaintext  .  length  ,  ciphertext  ,  0  )  ; 
cipher  .  doFinal  (  ciphertext  ,  bytesSoFar  )  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  95  )  ; 
b  .  putInt  (  "site_count"  ,  mSites  .  length  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
plaintext  =  null  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  mFilename  )  ; 
fos  .  write  (  ciphertext  )  ; 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  100  )  ; 
b  .  putInt  (  "site_count"  ,  mSites  .  length  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
ciphertext  =  null  ; 
fos  =  null  ; 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  5  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  catch  (  Exception   e  )  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  3  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  4  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
} 
} 








private   class   OldFormatImporter   extends   Thread  { 


private   Handler   mHandler  ; 


private   String   mPassword  ; 


private   String   mFilename  ; 


private   Activity   mActivity  ; 








OldFormatImporter  (  Handler   handler  ,  String   password  ,  String   filename  ,  Activity   activity  )  { 
mHandler  =  handler  ; 
mPassword  =  password  ; 
mFilename  =  filename  ; 
mActivity  =  activity  ; 
} 

@  Override 
public   void   run  (  )  { 
Message   msg  =  null  ; 
Bundle   b  =  null  ; 
try  { 
File   file  =  new   File  (  mFilename  )  ; 
if  (  file  .  exists  (  )  &&  file  .  isFile  (  )  &&  file  .  canRead  (  )  &&  file  .  length  (  )  <=  (  long  )  Integer  .  MAX_VALUE  )  { 
Cipher   cipher  =  createOldFormatCipher  (  mPassword  ,  Cipher  .  DECRYPT_MODE  )  ; 
if  (  ImportExportHandler  .  haveSufficientMemory  (  cipher  ,  file  .  length  (  )  ,  mActivity  )  )  { 
byte  [  ]  plaintext  =  new   byte  [  cipher  .  getOutputSize  (  (  int  )  file  .  length  (  )  )  ]  ; 
int   bytesSoFar  =  0  ; 
int   bytesRead  =  0  ; 
int   blockSize  =  cipher  .  getBlockSize  (  )  ; 
int   fileLength  =  (  int  )  file  .  length  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  blockSize  ]  ; 
FileInputStream   fis  =  new   FileInputStream  (  file  )  ; 
while  (  bytesSoFar  <  fileLength  )  { 
bytesRead  =  fis  .  read  (  buffer  ,  0  ,  blockSize  )  ; 
if  (  bytesRead  <=  0  )  break  ; 
bytesRead  =  cipher  .  update  (  buffer  ,  0  ,  bytesRead  ,  plaintext  ,  bytesSoFar  )  ; 
bytesSoFar  +=  bytesRead  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  (  int  )  (  Math  .  floor  (  (  (  double  )  bytesSoFar  /  (  double  )  fileLength  *  50.0d  )  )  )  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
fis  .  close  (  )  ; 
fis  =  null  ; 
buffer  =  null  ; 
cipher  .  doFinal  (  plaintext  ,  bytesSoFar  )  ; 
String   unencryptedData  =  new   String  (  plaintext  )  ; 
plaintext  =  null  ; 
cipher  =  null  ; 
String  [  ]  sites  =  unencryptedData  .  split  (  "\n"  )  ; 
int   siteCount  =  sites  .  length  -  1  ; 
if  (  siteCount  >  0  )  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  50  )  ; 
b  .  putInt  (  "site_count"  ,  siteCount  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
importedSites  =  new   Object  [  siteCount  ]  ; 
for  (  int   i  =  0  ;  i  <  siteCount  ;  i  ++  )  { 
SiteParameters   params  =  new   SiteParameters  (  theApp  ,  sites  [  i  ]  )  ; 
importedSites  [  i  ]  =  (  Object  )  params  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  (  int  )  (  Math  .  floor  (  (  (  double  )  i  /  (  double  )  siteCount  *  50.0d  )  )  )  +  50  )  ; 
b  .  putInt  (  "site_count"  ,  siteCount  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  100  )  ; 
b  .  putInt  (  "site_count"  ,  siteCount  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  1  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  5  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  2  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  catch  (  Exception   e  )  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  1  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
} 
} 









private   class   XMLHandler   extends   DefaultHandler  { 



private   ArrayList  <  SiteParameters  >  siteList  =  null  ; 


private   SiteParameters   currentSite  =  null  ; 



private   StringBuilder   builder  =  null  ; 



private   int   siteCount  =  0  ; 


private   boolean   inCryptnosTag  =  false  ; 


private   boolean   inVersionTag  =  false  ; 




private   boolean   inIgnoredTag  =  false  ; 


private   boolean   inSiteCountTag  =  false  ; 


private   boolean   inSitesTag  =  false  ; 


private   boolean   inSiteTag  =  false  ; 


private   boolean   inParamTag  =  false  ; 



private   Handler   topHandler  =  null  ; 


private   Message   msg  =  null  ; 


private   Bundle   b  =  null  ; 






XMLHandler  (  Handler   topHandler  )  { 
super  (  )  ; 
this  .  topHandler  =  topHandler  ; 
} 

@  Override 
public   void   startDocument  (  )  throws   SAXException  { 
super  .  startDocument  (  )  ; 
siteList  =  new   ArrayList  <  SiteParameters  >  (  )  ; 
builder  =  new   StringBuilder  (  )  ; 
} 

@  Override 
public   void   startElement  (  String   uri  ,  String   localName  ,  String   name  ,  Attributes   attributes  )  throws   SAXException  { 
super  .  startElement  (  uri  ,  localName  ,  name  ,  attributes  )  ; 
if  (  builder  .  toString  (  )  .  matches  (  "^\\s+$"  )  )  builder  .  setLength  (  0  )  ; 
if  (  localName  .  equalsIgnoreCase  (  "cryptnos"  )  &&  !  inVersionTag  &&  !  inIgnoredTag  &&  !  inSitesTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inSiteCountTag  &&  !  inCryptnosTag  )  { 
inCryptnosTag  =  true  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "version"  )  &&  inCryptnosTag  &&  !  inIgnoredTag  &&  !  inSitesTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inVersionTag  &&  !  inSiteCountTag  )  { 
inVersionTag  =  true  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "siteCount"  )  &&  inCryptnosTag  &&  !  inIgnoredTag  &&  !  inSitesTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inVersionTag  &&  !  inSiteCountTag  )  { 
inSiteCountTag  =  true  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "sites"  )  &&  inCryptnosTag  &&  !  inVersionTag  &&  !  inIgnoredTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inSitesTag  &&  !  inSiteCountTag  )  { 
inSitesTag  =  true  ; 
msg  =  topHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  15  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
topHandler  .  sendMessage  (  msg  )  ; 
}  else   if  (  (  localName  .  equalsIgnoreCase  (  "generator"  )  ||  localName  .  equalsIgnoreCase  (  "comment"  )  )  &&  inCryptnosTag  &&  !  inVersionTag  &&  !  inSitesTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inIgnoredTag  &&  !  inSiteCountTag  )  { 
inIgnoredTag  =  true  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "site"  )  &&  inCryptnosTag  &&  inSitesTag  &&  !  inVersionTag  &&  !  inIgnoredTag  &&  !  inSiteTag  &&  !  inParamTag  &&  !  inSiteCountTag  )  { 
this  .  currentSite  =  new   SiteParameters  (  theApp  )  ; 
inSiteTag  =  true  ; 
}  else   if  (  inCryptnosTag  &&  inSitesTag  &&  inSiteTag  &&  !  inVersionTag  &&  !  inIgnoredTag  &&  !  inParamTag  &&  !  inSiteCountTag  &&  (  localName  .  equalsIgnoreCase  (  "siteToken"  )  ||  localName  .  equalsIgnoreCase  (  "hash"  )  ||  localName  .  equalsIgnoreCase  (  "iterations"  )  ||  localName  .  equalsIgnoreCase  (  "charTypes"  )  ||  localName  .  equalsIgnoreCase  (  "charLimit"  )  )  )  { 
inParamTag  =  true  ; 
}  else   throw   new   SAXException  (  "Unexpected tag or invalid tag order"  )  ; 
} 

@  Override 
public   void   characters  (  char  [  ]  ch  ,  int   start  ,  int   length  )  throws   SAXException  { 
super  .  characters  (  ch  ,  start  ,  length  )  ; 
builder  .  append  (  ch  ,  start  ,  length  )  ; 
} 

@  Override 
public   void   endElement  (  String   uri  ,  String   localName  ,  String   name  )  throws   SAXException  { 
super  .  endElement  (  uri  ,  localName  ,  name  )  ; 
try  { 
if  (  localName  .  equalsIgnoreCase  (  "cryptnos"  )  &&  inCryptnosTag  )  { 
inCryptnosTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "version"  )  &&  inVersionTag  )  { 
inVersionTag  =  false  ; 
int   version  =  Integer  .  parseInt  (  builder  .  toString  (  )  .  trim  (  )  )  ; 
if  (  version  !=  1  )  throw   new   Exception  (  )  ; 
}  else   if  (  (  localName  .  equalsIgnoreCase  (  "generator"  )  ||  localName  .  equalsIgnoreCase  (  "comment"  )  )  &&  inIgnoredTag  )  { 
inIgnoredTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "siteCount"  )  &&  inSiteCountTag  )  { 
inSiteCountTag  =  false  ; 
siteCount  =  Integer  .  parseInt  (  builder  .  toString  (  )  .  trim  (  )  )  ; 
if  (  siteCount  <=  0  )  throw   new   Exception  (  )  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "sites"  )  &&  inSitesTag  )  { 
inSitesTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "siteToken"  )  &&  inSiteTag  &&  inParamTag  &&  currentSite  !=  null  )  { 
currentSite  .  setSite  (  builder  .  toString  (  )  .  trim  (  )  )  ; 
inParamTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "hash"  )  &&  inSiteTag  &&  inParamTag  &&  currentSite  !=  null  )  { 
currentSite  .  setHash  (  builder  .  toString  (  )  .  trim  (  )  )  ; 
inParamTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "iterations"  )  &&  inSiteTag  &&  inParamTag  &&  currentSite  !=  null  )  { 
currentSite  .  setIterations  (  Integer  .  parseInt  (  builder  .  toString  (  )  .  trim  (  )  )  )  ; 
inParamTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "charTypes"  )  &&  inSiteTag  &&  inParamTag  &&  currentSite  !=  null  )  { 
currentSite  .  setCharTypes  (  Integer  .  parseInt  (  builder  .  toString  (  )  .  trim  (  )  )  )  ; 
inParamTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "charLimit"  )  &&  inSiteTag  &&  inParamTag  &&  currentSite  !=  null  )  { 
currentSite  .  setCharLimit  (  Integer  .  parseInt  (  builder  .  toString  (  )  .  trim  (  )  )  )  ; 
inParamTag  =  false  ; 
}  else   if  (  localName  .  equalsIgnoreCase  (  "site"  )  &&  inSiteTag  &&  currentSite  !=  null  )  { 
siteList  .  add  (  currentSite  )  ; 
inSiteTag  =  false  ; 
msg  =  topHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  (  int  )  (  Math  .  floor  (  (  (  double  )  siteList  .  size  (  )  /  (  double  )  siteCount  *  33.0d  )  )  )  +  33  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
b  .  putInt  (  "site_count"  ,  siteCount  )  ; 
msg  .  setData  (  b  )  ; 
topHandler  .  sendMessage  (  msg  )  ; 
}  else   throw   new   Exception  (  )  ; 
builder  .  setLength  (  0  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   SAXException  (  "Invalid data type"  )  ; 
} 
} 





Object  [  ]  getSites  (  )  { 
if  (  siteList  ==  null  ||  siteList  .  isEmpty  (  )  )  return   null  ;  else   return   siteList  .  toArray  (  )  ; 
} 
} 








private   class   XMLFormat1Importer   extends   Thread  { 


private   Handler   mHandler  ; 


private   String   mPassword  ; 


private   String   mFilename  ; 


private   Activity   mActivity  ; 








XMLFormat1Importer  (  Handler   handler  ,  String   password  ,  String   filename  ,  Activity   activity  )  { 
mHandler  =  handler  ; 
mPassword  =  password  ; 
mFilename  =  filename  ; 
mActivity  =  activity  ; 
} 

@  Override 
public   void   run  (  )  { 
Message   msg  =  null  ; 
Bundle   b  =  null  ; 
try  { 
File   file  =  new   File  (  mFilename  )  ; 
if  (  file  .  exists  (  )  &&  file  .  isFile  (  )  &&  file  .  canRead  (  )  &&  file  .  length  (  )  <  (  long  )  Integer  .  MAX_VALUE  )  { 
BufferedBlockCipher   cipher  =  createXMLFormatCipher  (  mPassword  ,  false  ,  theApp  )  ; 
if  (  ImportExportHandler  .  haveSufficientMemory  (  cipher  ,  false  ,  file  .  length  (  )  ,  mActivity  )  )  { 
byte  [  ]  plaintext  =  new   byte  [  cipher  .  getOutputSize  (  (  int  )  file  .  length  (  )  )  ]  ; 
int   bytesSoFar  =  0  ; 
int   bytesRead  =  0  ; 
int   blockSize  =  cipher  .  getBlockSize  (  )  ; 
int   fileLength  =  (  int  )  file  .  length  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  blockSize  ]  ; 
FileInputStream   fis  =  new   FileInputStream  (  file  )  ; 
while  (  bytesSoFar  <  fileLength  )  { 
bytesRead  =  fis  .  read  (  buffer  ,  0  ,  blockSize  )  ; 
if  (  bytesRead  <=  0  )  break  ; 
bytesRead  =  cipher  .  processBytes  (  buffer  ,  0  ,  bytesRead  ,  plaintext  ,  bytesSoFar  )  ; 
bytesSoFar  +=  bytesRead  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  (  int  )  (  Math  .  floor  (  (  (  double  )  bytesRead  /  (  double  )  fileLength  *  33.0d  )  )  )  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
fis  .  close  (  )  ; 
fis  =  null  ; 
buffer  =  null  ; 
cipher  .  doFinal  (  plaintext  ,  bytesSoFar  )  ; 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  33  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
cipher  =  null  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   GZIPInputStream  (  new   ByteArrayInputStream  (  plaintext  )  )  )  ; 
XMLHandler   xmlHandler  =  new   XMLHandler  (  mHandler  )  ; 
SAXParser   parser  =  SAXParserFactory  .  newInstance  (  )  .  newSAXParser  (  )  ; 
parser  .  parse  (  in  ,  xmlHandler  )  ; 
importedSites  =  xmlHandler  .  getSites  (  )  ; 
in  .  close  (  )  ; 
plaintext  =  null  ; 
xmlHandler  =  null  ; 
in  =  null  ; 
if  (  importedSites  !=  null  &&  importedSites  .  length  >  0  )  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  100  )  ; 
b  .  putInt  (  "site_count"  ,  importedSites  .  length  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  1000  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  1000  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  else  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  5  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
}  catch  (  Exception   e  )  { 
msg  =  mHandler  .  obtainMessage  (  )  ; 
b  =  new   Bundle  (  )  ; 
b  .  putInt  (  "percent_done"  ,  -  1000  )  ; 
b  .  putInt  (  "site_count"  ,  0  )  ; 
msg  .  setData  (  b  )  ; 
mHandler  .  sendMessage  (  msg  )  ; 
} 
} 
} 
} 

