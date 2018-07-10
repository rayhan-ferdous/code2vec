package   Common  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 

public   class   Utils  { 










public   static   double   trim  (  double   num  ,  int   numOfDigits  ,  boolean   round  )  { 
int   accuracy  =  (  int  )  Math  .  pow  (  10  ,  numOfDigits  )  ; 
num  =  num  *  accuracy  ; 
num  =  round  ?  Math  .  round  (  num  )  :  Math  .  floor  (  num  )  ; 
num  =  num  /  accuracy  ; 
return   num  ; 
} 








public   static   boolean   doubleEquals  (  double   a  ,  double   b  )  { 
return  (  a  >  b  -  0.00001  )  &&  (  a  <  b  +  0.00001  )  ; 
} 








public   static   double   calcTrimmedMean  (  List  <  Integer  >  list  ,  double   alpha  )  { 
if  (  list  .  size  (  )  ==  0  )  { 
return   0  ; 
}  else   if  (  list  .  size  (  )  ==  1  )  { 
return   list  .  get  (  0  )  ; 
} 
Collections  .  sort  (  list  )  ; 
int   elementsToCut  =  (  int  )  Math  .  floor  (  list  .  size  (  )  *  alpha  )  ; 
for  (  int   i  =  0  ;  i  <  elementsToCut  ;  i  ++  )  { 
list  .  remove  (  0  )  ; 
list  .  remove  (  list  .  size  (  )  -  1  )  ; 
} 
int   sum  =  0  ; 
for  (  int   element  :  list  )  { 
sum  +=  element  ; 
} 
return  (  double  )  sum  /  (  double  )  list  .  size  (  )  ; 
} 








public   static   double   calcTrimmedMeanOnCopy  (  List  <  Integer  >  list  ,  double   alpha  )  { 
List  <  Integer  >  listCopy  =  new   ArrayList  <  Integer  >  (  list  .  size  (  )  )  ; 
for  (  Integer   value  :  list  )  { 
listCopy  .  add  (  value  )  ; 
} 
return   calcTrimmedMean  (  listCopy  ,  alpha  )  ; 
} 








public   static   double   calcTrimmedDoubleMean  (  List  <  Double  >  list  ,  double   alpha  )  { 
if  (  list  .  size  (  )  ==  0  )  { 
return   0  ; 
}  else   if  (  list  .  size  (  )  ==  1  )  { 
return   list  .  get  (  0  )  ; 
} 
Collections  .  sort  (  list  )  ; 
int   elementsToCut  =  (  int  )  Math  .  floor  (  list  .  size  (  )  *  alpha  )  ; 
for  (  int   i  =  0  ;  i  <  elementsToCut  ;  i  ++  )  { 
list  .  remove  (  0  )  ; 
list  .  remove  (  list  .  size  (  )  -  1  )  ; 
} 
double   sum  =  0  ; 
for  (  Double   element  :  list  )  { 
sum  +=  element  ; 
} 
return  (  double  )  sum  /  (  double  )  list  .  size  (  )  ; 
} 







public   static   Properties   readPropertiesFile  (  String   filePath  )  throws   FileNotFoundException  ,  IOException  { 
Properties   properties  =  new   Properties  (  )  ; 
FileInputStream   inStream  =  new   FileInputStream  (  filePath  )  ; 
properties  .  load  (  inStream  )  ; 
inStream  .  close  (  )  ; 
return   properties  ; 
} 








public   static   void   writePropertiesFile  (  Properties   properties  ,  String   filePath  ,  String   comments  )  throws   FileNotFoundException  ,  IOException  { 
FileOutputStream   outStream  =  new   FileOutputStream  (  filePath  )  ; 
properties  .  store  (  outStream  ,  comments  )  ; 
outStream  .  close  (  )  ; 
} 







public   static   String   createDotSeparatedString  (  String  ...  strs  )  { 
boolean   firstIter  =  true  ; 
String   result  =  null  ; 
for  (  String   str  :  strs  )  { 
if  (  firstIter  )  { 
result  =  str  ; 
firstIter  =  false  ; 
}  else  { 
result  +=  Constants  .  DOT  +  str  ; 
} 
} 
return   result  ; 
} 








public   static   boolean   isPropertyExist  (  Properties   properties  ,  String   key  )  { 
return   properties  .  getProperty  (  key  )  !=  null  ; 
} 








public   static   int   getPropertyAsInteger  (  Properties   properties  ,  String   key  )  { 
String   value  =  properties  .  getProperty  (  key  )  ; 
return   Integer  .  parseInt  (  value  )  ; 
} 








public   static   double   getPropertyAsDouble  (  Properties   properties  ,  String   key  )  { 
String   value  =  properties  .  getProperty  (  key  )  ; 
return   Double  .  parseDouble  (  value  )  ; 
} 







public   static   String   extractFileExtension  (  String   filePath  )  { 
int   beginIndex  =  filePath  .  lastIndexOf  (  '.'  )  ; 
return   filePath  .  substring  (  beginIndex  +  1  )  ; 
} 







public   static   String   getCurrentTimeString  (  )  { 
Date   date  =  new   Date  (  )  ; 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "dd.MM.yy_HH-mm-ss"  )  ; 
return   formatter  .  format  (  date  )  ; 
} 










public   static   Date   extrectDateFromPattern  (  String   str  )  { 
Pattern   p  =  Pattern  .  compile  (  "\\d\\d\\.\\d\\d\\.\\d\\d_\\d\\d-\\d\\d-\\d\\d"  )  ; 
Matcher   m  =  p  .  matcher  (  str  )  ; 
if  (  !  m  .  find  (  )  )  { 
return   null  ; 
} 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "dd.MM.yy_HH-mm-ss"  )  ; 
Date   date  =  null  ; 
try  { 
date  =  formatter  .  parse  (  m  .  group  (  )  )  ; 
}  catch  (  ParseException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   date  ; 
} 







public   static   void   serializeObject  (  String   filePath  ,  Serializable   object  )  { 
FileOutputStream   fileOut  =  null  ; 
ObjectOutputStream   out  =  null  ; 
try  { 
fileOut  =  new   FileOutputStream  (  filePath  )  ; 
out  =  new   ObjectOutputStream  (  fileOut  )  ; 
out  .  writeObject  (  object  )  ; 
out  .  close  (  )  ; 
fileOut  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
try  { 
if  (  fileOut  !=  null  &&  out  !=  null  )  { 
out  .  close  (  )  ; 
fileOut  .  close  (  )  ; 
} 
}  catch  (  IOException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
File   file  =  new   File  (  filePath  )  ; 
if  (  file  .  exists  (  )  )  { 
file  .  delete  (  )  ; 
} 
} 
} 









public   static   Object   deserializeObject  (  String   filePath  )  throws   IOException  ,  ClassNotFoundException  { 
FileInputStream   fileIn  =  new   FileInputStream  (  filePath  )  ; 
ObjectInputStream   in  =  new   ObjectInputStream  (  fileIn  )  ; 
Object   object  =  in  .  readObject  (  )  ; 
in  .  close  (  )  ; 
fileIn  .  close  (  )  ; 
return   object  ; 
} 









public   static   String   getLatestFile  (  String   path  )  { 
File   latestFile  =  null  ; 
Date   latestDate  =  null  ; 
for  (  File   file  :  new   File  (  path  )  .  listFiles  (  )  )  { 
if  (  file  .  isDirectory  (  )  )  { 
continue  ; 
} 
if  (  latestDate  ==  null  )  { 
latestDate  =  extrectDateFromPattern  (  file  .  getPath  (  )  )  ; 
latestFile  =  file  ; 
}  else  { 
Date   tempDate  =  extrectDateFromPattern  (  file  .  getPath  (  )  )  ; 
if  (  tempDate  .  getTime  (  )  -  latestDate  .  getTime  (  )  >  0  )  { 
latestFile  =  file  ; 
latestDate  =  tempDate  ; 
} 
} 
} 
return   latestFile  ==  null  ?  null  :  latestFile  .  getAbsolutePath  (  )  ; 
} 








public   static   void   getFileFromUrl  (  String   url  ,  String   outputFilePath  )  throws   IOException  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   URL  (  url  )  .  openStream  (  )  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  outputFilePath  )  ; 
BufferedOutputStream   bout  =  new   BufferedOutputStream  (  fos  ,  1024  )  ; 
byte   data  [  ]  =  new   byte  [  1024  ]  ; 
int   count  =  in  .  read  (  data  ,  0  ,  1024  )  ; 
while  (  count  >  0  )  { 
bout  .  write  (  data  ,  0  ,  count  )  ; 
count  =  in  .  read  (  data  ,  0  ,  1024  )  ; 
} 
bout  .  close  (  )  ; 
in  .  close  (  )  ; 
} 









public   static   File   unGzip  (  File   infile  ,  boolean   deleteGzipfileOnSuccess  )  throws   IOException  { 
GZIPInputStream   gin  =  new   GZIPInputStream  (  new   FileInputStream  (  infile  )  )  ; 
File   outFile  =  new   File  (  infile  .  getParent  (  )  ,  infile  .  getName  (  )  .  replaceAll  (  "\\.gz$"  ,  ""  )  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  outFile  )  ; 
byte  [  ]  buf  =  new   byte  [  100000  ]  ; 
int   len  ; 
while  (  (  len  =  gin  .  read  (  buf  )  )  >  0  )  fos  .  write  (  buf  ,  0  ,  len  )  ; 
gin  .  close  (  )  ; 
fos  .  close  (  )  ; 
if  (  deleteGzipfileOnSuccess  )  infile  .  delete  (  )  ; 
return   outFile  ; 
} 
} 

