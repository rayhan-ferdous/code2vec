package   wdc  .  utils  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  zip  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  text  .  *  ; 




public   class   Utilities  { 

public   static   final   int   GREGORIAN  =  0  ; 

public   static   final   int   JULIAN  =  1  ; 







public   static   double   getJulianDate  (  int   year  ,  int   month  ,  int   day  ,  int   h  ,  int   m  ,  int   s  )  { 
int   JDN  =  JDN  =  (  1461  *  (  year  +  4800  +  (  month  -  14  )  /  12  )  )  /  4  +  (  367  *  (  month  -  2  -  12  *  (  (  month  -  14  )  /  12  )  )  )  /  12  -  (  3  *  (  (  year  +  4900  +  (  month  -  14  )  /  12  )  /  100  )  )  /  4  +  day  -  32075  ; 
double   JD  =  JDN  +  (  h  -  12  )  /  24.0  +  m  /  1440.0  +  s  /  86400.0  ; 
return   JD  ; 
} 

public   static   String   compressFile  (  String   fileName  )  throws   IOException  { 
String   newFileName  =  fileName  +  ".gz"  ; 
FileInputStream   fis  =  new   FileInputStream  (  fileName  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  newFileName  )  ; 
GZIPOutputStream   gzos  =  new   GZIPOutputStream  (  fos  )  ; 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  fis  .  read  (  buf  )  )  >  0  )  gzos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
fis  .  close  (  )  ; 
gzos  .  close  (  )  ; 
return   newFileName  ; 
} 








public   static   String   uncompressFile  (  String   inputFile  ,  String   outputFile  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  inputFile  )  ; 
return   uncompressStream  (  fis  ,  outputFile  )  ; 
} 








public   static   String   uncompressStream  (  InputStream   is  ,  String   outputFile  )  throws   IOException  { 
GZIPInputStream   gzis  =  new   GZIPInputStream  (  is  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  outputFile  )  ; 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  gzis  .  read  (  buf  )  )  >  0  )  fos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
gzis  .  close  (  )  ; 
fos  .  close  (  )  ; 
return   outputFile  ; 
} 







public   static   String   compressWithZip  (  String   fileName  )  throws   IOException  { 
String   zipFileName  =  fileName  +  ".zip"  ; 
compressWithZip  (  fileName  ,  zipFileName  )  ; 
return   zipFileName  ; 
} 







public   static   void   compressWithZip  (  String   fileName  ,  String   zipFileName  )  throws   IOException  { 
Vector   fileList  =  new   Vector  (  )  ; 
fileList  .  add  (  fileName  )  ; 
compressWithZip  (  fileList  ,  zipFileName  )  ; 
} 







public   static   void   compressWithZip  (  Vector   fileList  ,  String   zipFileName  )  throws   IOException  { 
if  (  fileList  ==  null  ||  fileList  .  size  (  )  ==  0  )  return  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  zipFileName  )  ; 
ZipOutputStream   zos  =  new   ZipOutputStream  (  fos  )  ; 
Iterator   iter  =  fileList  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
String   fileName  =  (  String  )  iter  .  next  (  )  ; 
int   ind  =  Math  .  max  (  fileName  .  lastIndexOf  (  '/'  )  ,  fileName  .  lastIndexOf  (  '\\'  )  )  ; 
String   shortName  =  "unknown"  ; 
if  (  ind  <  fileName  .  length  (  )  -  1  )  shortName  =  fileName  .  substring  (  ind  +  1  )  ; 
zos  .  putNextEntry  (  new   ZipEntry  (  shortName  )  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  fileName  )  ; 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  fis  .  read  (  buf  )  )  >  0  )  zos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
fis  .  close  (  )  ; 
zos  .  closeEntry  (  )  ; 
} 
zos  .  close  (  )  ; 
} 








public   static   String   uncompressZipFile  (  String   inputFile  ,  String   outputFile  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  inputFile  )  ; 
return   uncompressZipStream  (  fis  ,  outputFile  )  ; 
} 








public   static   String   uncompressZipStream  (  InputStream   is  ,  String   outputFile  )  throws   IOException  { 
ZipInputStream   zis  =  new   ZipInputStream  (  is  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  outputFile  )  ; 
if  (  zis  .  getNextEntry  (  )  ==  null  )  throw   new   IOException  (  "Not ZIP format"  )  ; 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  zis  .  read  (  buf  )  )  >  0  )  fos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
zis  .  closeEntry  (  )  ; 
zis  .  close  (  )  ; 
fos  .  close  (  )  ; 
return   outputFile  ; 
} 







public   static   void   compressFromWeb  (  Vector   urlList  ,  String   zipFileName  )  throws   IOException  { 
if  (  urlList  ==  null  ||  urlList  .  size  (  )  ==  0  )  return  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  zipFileName  )  ; 
ZipOutputStream   zos  =  new   ZipOutputStream  (  fos  )  ; 
Iterator   iter  =  urlList  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
String   fileUrl  =  (  String  )  iter  .  next  (  )  ; 
if  (  fileUrl  ==  null  ||  fileUrl  .  trim  (  )  .  equals  (  ""  )  )  continue  ; 
int   ind  =  Math  .  max  (  fileUrl  .  lastIndexOf  (  '/'  )  ,  fileUrl  .  lastIndexOf  (  '\\'  )  )  ; 
String   shortName  =  "unknown"  ; 
if  (  ind  <  fileUrl  .  length  (  )  -  1  )  shortName  =  fileUrl  .  substring  (  ind  +  1  )  ; 
zos  .  putNextEntry  (  new   ZipEntry  (  shortName  )  )  ; 
InputStream   is  =  null  ; 
URL   url  =  null  ; 
try  { 
url  =  new   URL  (  fileUrl  )  ; 
is  =  url  .  openStream  (  )  ; 
}  catch  (  Exception   e  )  { 
zos  .  closeEntry  (  )  ; 
if  (  is  !=  null  )  is  .  close  (  )  ; 
continue  ; 
} 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  is  .  read  (  buf  )  )  >  0  )  zos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
is  .  close  (  )  ; 
zos  .  closeEntry  (  )  ; 
} 
zos  .  close  (  )  ; 
} 








public   static   String   stream2file  (  InputStream   is  ,  String   outputFile  )  throws   IOException  { 
FileOutputStream   fos  =  new   FileOutputStream  (  outputFile  )  ; 
byte  [  ]  buf  =  new   byte  [  10000  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  is  .  read  (  buf  )  )  >  0  )  fos  .  write  (  buf  ,  0  ,  bytesRead  )  ; 
is  .  close  (  )  ; 
fos  .  close  (  )  ; 
return   outputFile  ; 
} 






public   static   String   prepareMessage  (  String   message  )  { 
if  (  message  !=  null  )  { 
message  =  message  .  replace  (  '\"'  ,  '\''  )  ; 
message  =  message  .  replace  (  '\n'  ,  ' '  )  ; 
message  =  message  .  replace  (  '\r'  ,  ' '  )  ; 
}  else   message  =  ""  ; 
return   message  ; 
} 






public   static   String  [  ]  splitString  (  String   str  )  { 
if  (  str  ==  null  )  str  =  ""  ; 
StringTokenizer   st  =  new   StringTokenizer  (  str  ,  ",; \t\r\n"  )  ; 
int   numTokens  =  st  .  countTokens  (  )  ; 
String  [  ]  list  =  new   String  [  numTokens  ]  ; 
for  (  int   k  =  0  ;  k  <  numTokens  ;  k  ++  )  list  [  k  ]  =  st  .  nextToken  (  )  ; 
return   list  ; 
} 






public   static   byte  [  ]  obj2bytes  (  Object   obj  )  throws   IOException  { 
if  (  obj  ==  null  )  return   null  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
GZIPOutputStream   gzos  =  new   GZIPOutputStream  (  bos  )  ; 
ObjectOutputStream   oos  =  new   ObjectOutputStream  (  gzos  )  ; 
oos  .  writeObject  (  obj  )  ; 
oos  .  flush  (  )  ; 
oos  .  close  (  )  ; 
byte  [  ]  byteArray  =  bos  .  toByteArray  (  )  ; 
bos  .  close  (  )  ; 
return   byteArray  ; 
} 






public   static   Object   bytes2obj  (  byte  [  ]  bytes  )  throws   IOException  ,  ClassNotFoundException  { 
if  (  bytes  ==  null  )  return   null  ; 
GZIPInputStream   gzis  =  new   GZIPInputStream  (  new   ByteArrayInputStream  (  bytes  )  )  ; 
ObjectInputStream   ois  =  new   ObjectInputStream  (  gzis  )  ; 
Object   obj  =  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
gzis  .  close  (  )  ; 
return   obj  ; 
} 






public   static   String   formatTime  (  long   timeId  )  { 
return   formatTime  (  timeId  ,  "yyyy-MM-dd HH:mm:ss"  )  ; 
} 







public   static   String   formatTime  (  long   timeId  ,  String   template  )  { 
SimpleDateFormat   df  =  new   SimpleDateFormat  (  template  ,  Locale  .  US  )  ; 
df  .  setTimeZone  (  TimeZone  .  getTimeZone  (  "GMT"  )  )  ; 
GregorianCalendar   clndr  =  new   GregorianCalendar  (  TimeZone  .  getTimeZone  (  "GMT"  )  ,  Locale  .  US  )  ; 
clndr  .  set  (  (  int  )  (  timeId  /  10000000000L  )  ,  (  int  )  (  (  timeId  /  100000000L  )  %  100  )  -  1  ,  (  int  )  (  (  timeId  /  1000000L  )  %  100  )  ,  (  int  )  (  (  timeId  /  10000  )  %  100  )  ,  (  int  )  (  (  timeId  /  100  )  %  100  )  ,  (  int  )  (  timeId  %  100  )  )  ; 
return   df  .  format  (  clndr  .  getTime  (  )  )  ; 
} 





public   static   void   main  (  String   args  [  ]  )  { 
System  .  out  .  println  (  "Start"  )  ; 
try  { 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "Error: "  +  e  .  toString  (  )  )  ; 
} 
System  .  out  .  println  (  "Finish"  )  ; 
} 
} 

