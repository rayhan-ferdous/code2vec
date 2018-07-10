package   com  .  newbee  .  util  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  Date  ; 
import   com  .  newbee  .  database  .  Table  ; 

public   class   Format  { 






public   static   final   String   getToday  (  String   format  )  { 
SimpleDateFormat   sd  =  new   SimpleDateFormat  (  format  )  ; 
String   todayStr  =  sd  .  format  (  new   Date  (  )  )  ; 
return   todayStr  ; 
} 







public   static   final   String   getYesterDay  (  String   ft  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  ft  )  ; 
Date   today  =  new   Date  (  )  ; 
long   d  =  today  .  getTime  (  )  -  1000  *  60  *  60  *  24  ; 
Date   yesDay  =  new   Date  (  d  )  ; 
String   yesDayStr  =  sdf  .  format  (  yesDay  )  ; 
return   yesDayStr  ; 
} 








public   static   final   String   getAnyDays  (  String   ft  ,  int   x  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  ft  )  ; 
Date   today  =  new   Date  (  )  ; 
long   d  =  today  .  getTime  (  )  +  (  1000  *  60  *  60  *  24  )  *  x  ; 
Date   yesDay  =  new   Date  (  d  )  ; 
String   yesDayStr  =  sdf  .  format  (  yesDay  )  ; 
return   yesDayStr  ; 
} 






public   static   final   long   compareToTime  (  String   time  )  { 
long   diffTime  =  0  ; 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "HH:mm:ss"  )  ; 
try  { 
Date   s  =  sdf  .  parse  (  time  )  ; 
Date   current  =  sdf  .  parse  (  nowTime  (  )  )  ; 
diffTime  =  s  .  getTime  (  )  -  current  .  getTime  (  )  ; 
}  catch  (  ParseException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   diffTime  ; 
} 






public   static   String   nowTime  (  )  { 
return   new   SimpleDateFormat  (  "HH:mm:ss"  )  .  format  (  new   Date  (  )  )  ; 
} 






public   static   String   getFullTime  (  )  { 
SimpleDateFormat   sdt  =  new   SimpleDateFormat  (  "yyyy/MM/dd HH:mm:ss"  )  ; 
return   sdt  .  format  (  new   Date  (  )  )  ; 
} 








public   static   String   getNumber  (  String   s  ,  int   len  )  { 
double   d  =  0  ; 
try  { 
d  =  Double  .  parseDouble  (  s  )  ; 
}  catch  (  Exception   e  )  { 
return   getNumber  (  "0"  ,  len  )  ; 
} 
return   getNumber  (  d  ,  len  )  ; 
} 






public   static   String   getNo  (  int   i  ,  int   len  )  { 
String   fmt  =  "0"  ; 
for  (  int   j  =  1  ;  j  <  len  ;  j  ++  )  { 
fmt  +=  "0"  ; 
} 
DecimalFormat   decimalFormat  =  new   DecimalFormat  (  fmt  )  ; 
return   decimalFormat  .  format  (  i  )  ; 
} 








public   static   String   getNumber  (  float   f  ,  int   len  )  { 
return   getNumber  (  (  double  )  f  ,  len  )  ; 
} 








public   static   String   getNumber  (  double   d  ,  int   len  )  { 
BigInteger   a1  =  BigInteger  .  valueOf  (  10  )  .  pow  (  len  )  ; 
double   a2  =  d  *  a1  .  doubleValue  (  )  ; 
double   e  =  (  Math  .  round  (  a2  )  )  /  a1  .  doubleValue  (  )  ; 
String   formatStr  =  "#0"  ; 
if  (  len  >  0  )  { 
formatStr  +=  "."  ; 
} 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
formatStr  +=  "0"  ; 
} 
return   new   DecimalFormat  (  formatStr  )  .  format  (  e  )  ; 
} 







public   static   String   getInt  (  String   s  )  { 
return   new   DecimalFormat  (  "#"  )  .  format  (  new   Double  (  s  )  .  doubleValue  (  )  )  ; 
} 








public   static   String   getPercent  (  String   s  ,  int   len  )  { 
double   d  =  0  ; 
try  { 
d  =  new   Double  (  s  )  .  doubleValue  (  )  *  100  ; 
}  catch  (  Exception   e  )  { 
return  "0%"  ; 
} 
return   getNumber  (  d  ,  len  )  +  "%"  ; 
} 









public   static   final   int   compareDate  (  String   date1  ,  String   date2  )  { 
int   tValue  =  0  ; 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "yyyy/MM/dd"  )  ; 
try  { 
Date   oldT  =  sdf  .  parse  (  date1  )  ; 
Date   newT  =  sdf  .  parse  (  date2  )  ; 
tValue  =  newT  .  compareTo  (  oldT  )  ; 
}  catch  (  ParseException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   tValue  ; 
} 








public   static   final   int   compareTime  (  String   oldTime  ,  String   newTime  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "HH:mm"  )  ; 
int   tValue  =  0  ; 
try  { 
Date   oldT  =  sdf  .  parse  (  oldTime  )  ; 
Date   newT  =  sdf  .  parse  (  newTime  )  ; 
tValue  =  newT  .  compareTo  (  oldT  )  ; 
}  catch  (  ParseException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   tValue  ; 
} 








public   static   String   replaceAll  (  String   s  ,  String   pix  ,  String   rs  )  { 
String   newStr  =  s  ; 
int   index  =  -  1  ; 
while  (  (  index  =  newStr  .  indexOf  (  pix  )  )  !=  -  1  )  { 
newStr  =  newStr  .  substring  (  0  ,  index  )  +  rs  +  newStr  .  substring  (  index  +  pix  .  length  (  )  )  ; 
} 
return   newStr  ; 
} 








public   static   Date   dateAdd  (  Date   date  ,  int   field  ,  int   amount  )  { 
Calendar   cal  =  Calendar  .  getInstance  (  )  ; 
cal  .  setTime  (  date  )  ; 
cal  .  add  (  field  ,  amount  )  ; 
return   cal  .  getTime  (  )  ; 
} 

public   static   String   nvl  (  String   s  )  { 
return   s  ==  null  ?  ""  :  s  ; 
} 

public   static   String   getHTML  (  String   s  )  { 
if  (  s  !=  null  )  { 
s  =  s  .  replaceAll  (  "\n"  ,  "<BR>"  )  ; 
s  =  s  .  replaceAll  (  "\r"  ,  ""  )  ; 
} 
return   s  ; 
} 

public   static   String   getHTMLPel  (  String   s  )  { 
if  (  s  !=  null  )  { 
s  =  s  .  replaceAll  (  "<"  ,  "&lt;"  )  ; 
s  =  s  .  replaceAll  (  ">"  ,  "&gt;"  )  ; 
s  =  s  .  replaceAll  (  " "  ,  "&nbsp;"  )  ; 
s  =  s  .  replaceAll  (  "\""  ,  "&quot;"  )  ; 
} 
return   s  ; 
} 







public   static   String   getSQLInsertValue  (  String   s  )  { 
if  (  s  !=  null  )  { 
s  =  s  .  replaceAll  (  "'"  ,  "''"  )  ; 
} 
return   s  ; 
} 







public   static   String   getScriptDispalyValue  (  String   s  )  { 
if  (  s  !=  null  )  { 
s  =  s  .  replaceAll  (  "\'"  ,  "\\\\'"  )  .  replaceAll  (  "\""  ,  "\\\\\""  )  ; 
} 
return   s  ; 
} 

public   static   String   byte2hex  (  byte  [  ]  b  )  { 
String   hs  =  ""  ; 
String   stmp  =  ""  ; 
for  (  int   n  =  0  ;  n  <  b  .  length  ;  n  ++  )  { 
stmp  =  (  java  .  lang  .  Integer  .  toHexString  (  b  [  n  ]  &  0XFF  )  )  ; 
if  (  stmp  .  length  (  )  ==  1  )  { 
hs  =  hs  +  "0"  +  stmp  ; 
}  else  { 
hs  =  hs  +  stmp  ; 
} 
} 
return   hs  .  toUpperCase  (  )  ; 
} 

public   static   byte  [  ]  hex2byte  (  String   h  )  { 
byte  [  ]  ret  =  new   byte  [  h  .  length  (  )  /  2  ]  ; 
for  (  int   i  =  0  ;  i  <  ret  .  length  ;  i  ++  )  { 
ret  [  i  ]  =  Integer  .  decode  (  "#"  +  h  .  substring  (  2  *  i  ,  2  *  i  +  2  )  )  .  byteValue  (  )  ; 
} 
return   ret  ; 
} 






public   static   byte  [  ]  getBytesFromFile  (  File   f  )  { 
if  (  f  ==  null  )  { 
return   null  ; 
} 
try  { 
FileInputStream   stream  =  new   FileInputStream  (  f  )  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  1000  )  ; 
byte  [  ]  b  =  new   byte  [  1000  ]  ; 
int   n  ; 
while  (  (  n  =  stream  .  read  (  b  )  )  !=  -  1  )  out  .  write  (  b  ,  0  ,  n  )  ; 
stream  .  close  (  )  ; 
out  .  close  (  )  ; 
return   out  .  toByteArray  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   null  ; 
} 






public   static   File   getFileFromBytes  (  byte  [  ]  b  ,  String   outputFile  )  { 
BufferedOutputStream   stream  =  null  ; 
File   file  =  null  ; 
try  { 
file  =  new   File  (  outputFile  )  ; 
FileOutputStream   fstream  =  new   FileOutputStream  (  file  )  ; 
stream  =  new   BufferedOutputStream  (  fstream  )  ; 
stream  .  write  (  b  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  stream  !=  null  )  { 
try  { 
stream  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
} 
} 
return   file  ; 
} 






public   static   Object   getObjectFromBytes  (  byte  [  ]  objBytes  )  throws   Exception  { 
if  (  objBytes  ==  null  ||  objBytes  .  length  ==  0  )  { 
return   null  ; 
} 
ByteArrayInputStream   bi  =  new   ByteArrayInputStream  (  objBytes  )  ; 
ObjectInputStream   oi  =  new   ObjectInputStream  (  bi  )  ; 
return   oi  .  readObject  (  )  ; 
} 






public   static   byte  [  ]  getBytesFromObject  (  Serializable   obj  )  throws   Exception  { 
if  (  obj  ==  null  )  { 
return   null  ; 
} 
ByteArrayOutputStream   bo  =  new   ByteArrayOutputStream  (  )  ; 
ObjectOutputStream   oo  =  new   ObjectOutputStream  (  bo  )  ; 
oo  .  writeObject  (  obj  )  ; 
return   bo  .  toByteArray  (  )  ; 
} 








public   static   int   doubleByte  (  String   str  )  { 
char   bt  [  ]  =  str  .  toCharArray  (  )  ; 
int   length  =  0  ; 
for  (  int   i  =  0  ;  i  <  bt  .  length  ;  i  ++  )  { 
if  (  bt  [  i  ]  >  255  )  { 
length  +=  2  ; 
}  else  { 
length  +=  1  ; 
} 
} 
return   length  ; 
} 








public   static   int   getRandom  (  int   min  ,  int   max  )  { 
return  (  int  )  (  Math  .  random  (  )  *  (  max  -  min  +  1  )  +  min  )  ; 
} 







public   static   String   getSqlFromFile  (  String   filePath  ,  String   encoding  )  { 
String   sql  =  ""  ; 
String   fn  =  ""  ; 
BufferedReader   reader  =  null  ; 
try  { 
File   f  =  new   File  (  filePath  )  ; 
fn  =  f  .  getName  (  )  ; 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  new   FileInputStream  (  f  )  ,  encoding  )  )  ; 
StringBuffer   strbContent  =  new   StringBuffer  (  )  ; 
while  (  true  )  { 
String   line  =  reader  .  readLine  (  )  ; 
if  (  line  ==  null  )  break  ; 
line  =  line  .  trim  (  )  ; 
if  (  line  .  startsWith  (  "--"  )  )  continue  ; 
int   pos  =  line  .  indexOf  (  "--"  )  ; 
if  (  pos  >  0  )  line  =  line  .  substring  (  0  ,  pos  )  ; 
strbContent  .  append  (  " "  +  line  +  " "  )  ; 
} 
sql  =  strbContent  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
}  finally  { 
try  { 
if  (  reader  !=  null  )  { 
reader  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 
System  .  out  .  println  (  fn  )  ; 
return   sql  ; 
} 








public   static   void   table2File  (  Table   t  )  { 
} 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
System  .  out  .  println  (  new   DecimalFormat  (  "#"  )  .  format  (  new   Double  (  "-10012.989"  )  .  doubleValue  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 

