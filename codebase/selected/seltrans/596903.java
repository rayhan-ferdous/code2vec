package   teachin  .  domain  .  randomlib  .  data  .  dataGeneration  ; 

import   java  .  math  .  BigDecimal  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  GregorianCalendar  ; 
import   java  .  util  .  Random  ; 
import   org  .  apache  .  commons  .  lang  .  ArrayUtils  ; 
import   teachin  .  domain  .  randomlib  .  data  .  CountryCodes  ; 






public   class   BasicDataGenerator  { 




public   static   final   Random   generator  =  new   Random  (  )  ; 




private   static   ArrayList  <  Object  >  set  =  new   ArrayList  <  Object  >  (  )  ; 







public   static   String   generateRandomString  (  int   len  )  { 
return   generateRandomString  (  len  ,  false  )  ; 
} 








public   static   String   generateRandomString  (  int   len  ,  boolean   unique  )  { 
int   maxLen  =  Math  .  min  (  len  ,  1000  )  ; 
StringBuffer   sb  =  null  ; 
if  (  unique  )  { 
do  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
if  (  generator  .  nextBoolean  (  )  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  26  )  +  65  )  )  ; 
}  else  { 
sb  .  append  (  generator  .  nextInt  (  10  )  )  ; 
} 
} 
}  while  (  set  .  contains  (  sb  .  toString  (  )  )  )  ; 
set  .  add  (  sb  .  toString  (  )  )  ; 
}  else  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
if  (  generator  .  nextBoolean  (  )  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  26  )  +  65  )  )  ; 
}  else  { 
sb  .  append  (  generator  .  nextInt  (  10  )  )  ; 
} 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   String   generateRandomStringChar  (  int   len  )  { 
return   generateRandomStringChar  (  len  ,  true  )  ; 
} 








public   static   String   generateRandomStringChar  (  int   len  ,  boolean   unique  )  { 
int   maxLen  =  Math  .  min  (  len  ,  1000  )  ; 
StringBuffer   sb  =  null  ; 
if  (  unique  )  { 
do  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  26  )  +  65  )  )  ; 
} 
}  while  (  set  .  contains  (  sb  .  toString  (  )  )  )  ; 
set  .  add  (  sb  .  toString  (  )  )  ; 
}  else  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  26  )  +  65  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   String   generateRandomSpecialChar  (  int   len  )  { 
return   generateRandomSpecialChar  (  len  ,  true  )  ; 
} 








public   static   String   generateRandomSpecialChar  (  int   len  ,  boolean   unique  )  { 
int   maxLen  =  Math  .  min  (  len  ,  1000  )  ; 
StringBuffer   sb  =  null  ; 
if  (  unique  )  { 
do  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  14  )  +  33  )  )  ; 
} 
}  while  (  set  .  contains  (  sb  .  toString  (  )  )  )  ; 
set  .  add  (  sb  .  toString  (  )  )  ; 
}  else  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  (  char  )  (  generator  .  nextInt  (  14  )  +  33  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   String   generateRandomNumericString  (  int   len  )  { 
return   generateRandomNumericString  (  len  ,  true  )  ; 
} 








public   static   String   generateRandomNumericString  (  int   len  ,  boolean   unique  )  { 
int   maxLen  =  Math  .  min  (  len  ,  1000  )  ; 
StringBuffer   sb  =  null  ; 
if  (  unique  )  { 
do  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  generator  .  nextInt  (  10  )  )  ; 
} 
}  while  (  set  .  contains  (  sb  .  toString  (  )  )  )  ; 
set  .  add  (  sb  .  toString  (  )  )  ; 
}  else  { 
sb  =  new   StringBuffer  (  maxLen  )  ; 
for  (  int   i  =  0  ;  i  <  maxLen  ;  i  ++  )  { 
sb  .  append  (  generator  .  nextInt  (  10  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   Long   generateRandomLong  (  int   upperLimit  )  { 
return   generateRandomLong  (  upperLimit  ,  true  )  ; 
} 








public   static   Long   generateRandomLong  (  int   upperLimit  ,  boolean   unique  )  { 
Long   randomLong  =  null  ; 
if  (  unique  )  { 
do  { 
randomLong  =  (  long  )  generator  .  nextInt  (  upperLimit  )  ; 
}  while  (  set  .  contains  (  randomLong  )  )  ; 
set  .  add  (  randomLong  )  ; 
}  else  { 
randomLong  =  (  long  )  generator  .  nextInt  (  upperLimit  )  ; 
} 
return   randomLong  ; 
} 






public   static   Long   generateRandomLong  (  )  { 
return   generateRandomLong  (  true  )  ; 
} 







public   static   Long   generateRandomLong  (  boolean   unique  )  { 
Long   randomLong  =  null  ; 
if  (  unique  )  { 
do  { 
randomLong  =  (  long  )  generator  .  nextInt  (  214748  )  ; 
}  while  (  set  .  contains  (  randomLong  )  )  ; 
set  .  add  (  randomLong  )  ; 
}  else  { 
randomLong  =  (  long  )  generator  .  nextInt  (  214748  )  ; 
} 
return   randomLong  ; 
} 







public   static   Integer   generateRandomInt  (  int   upperLimit  )  { 
return   generateRandomInt  (  upperLimit  ,  true  )  ; 
} 







public   static   Integer   generateRandomInt  (  int   upperLimit  ,  boolean   unique  )  { 
Integer   randomInteger  =  null  ; 
if  (  unique  )  { 
do  { 
randomInteger  =  generator  .  nextInt  (  upperLimit  +  1  )  ; 
}  while  (  set  .  contains  (  randomInteger  )  )  ; 
set  .  add  (  randomInteger  )  ; 
}  else  { 
randomInteger  =  generator  .  nextInt  (  upperLimit  +  1  )  ; 
} 
return   randomInteger  ; 
} 






public   static   Integer   generateRandomInt  (  )  { 
return   generateRandomInt  (  false  )  ; 
} 






public   static   Integer   generateRandomInt  (  boolean   unique  )  { 
Integer   randomInteger  =  null  ; 
if  (  unique  )  { 
do  { 
randomInteger  =  generator  .  nextInt  (  21474  )  ; 
}  while  (  set  .  contains  (  randomInteger  )  )  ; 
set  .  add  (  randomInteger  )  ; 
}  else  { 
randomInteger  =  generator  .  nextInt  (  21474  )  ; 
} 
return   randomInteger  ; 
} 






public   static   Byte   generateRandomTinyInt  (  )  { 
return   generateRandomTinyInt  (  false  )  ; 
} 






public   static   Byte   generateRandomTinyInt  (  boolean   unique  )  { 
return  (  byte  )  (  Math  .  abs  (  generateRandomSmallInt  (  unique  )  &  0x7F  )  )  ; 
} 






public   static   Integer   generateRandomSmallInt  (  )  { 
return   generateRandomSmallInt  (  false  )  ; 
} 






public   static   Integer   generateRandomSmallInt  (  boolean   unique  )  { 
Integer   randomInteger  =  null  ; 
if  (  unique  )  { 
do  { 
randomInteger  =  generator  .  nextInt  (  )  ; 
}  while  (  set  .  contains  (  randomInteger  )  )  ; 
set  .  add  (  randomInteger  )  ; 
}  else  { 
randomInteger  =  generator  .  nextInt  (  )  ; 
} 
return   randomInteger  ; 
} 







public   static   Long   generateRandomMediumInt  (  )  { 
return   generateRandomMediumInt  (  false  )  ; 
} 







public   static   Long   generateRandomMediumInt  (  boolean   unique  )  { 
Long   randomLong  =  null  ; 
if  (  unique  )  { 
do  { 
randomLong  =  Long  .  valueOf  (  generator  .  nextInt  (  16777215  )  )  ; 
}  while  (  set  .  contains  (  randomLong  )  )  ; 
set  .  add  (  randomLong  )  ; 
}  else  { 
randomLong  =  Long  .  valueOf  (  generator  .  nextInt  (  16777215  )  )  ; 
} 
return   randomLong  ; 
} 






public   static   Float   generateRandomFloat  (  )  { 
return   generateRandomFloat  (  false  )  ; 
} 






public   static   Float   generateRandomFloat  (  boolean   unique  )  { 
Float   randomFloat  =  null  ; 
if  (  unique  )  { 
do  { 
randomFloat  =  generator  .  nextFloat  (  )  ; 
}  while  (  set  .  contains  (  randomFloat  )  )  ; 
set  .  add  (  randomFloat  )  ; 
}  else  { 
randomFloat  =  generator  .  nextFloat  (  )  ; 
} 
return   randomFloat  ; 
} 






public   static   Byte   generateRandomByte  (  )  { 
return   generateRandomByte  (  false  )  ; 
} 






public   static   Byte   generateRandomByte  (  boolean   unique  )  { 
Byte   randomByte  =  null  ; 
if  (  unique  )  { 
do  { 
randomByte  =  (  byte  )  generator  .  nextInt  (  99  )  ; 
}  while  (  set  .  contains  (  randomByte  )  )  ; 
set  .  add  (  randomByte  )  ; 
}  else  { 
randomByte  =  (  byte  )  generator  .  nextInt  (  99  )  ; 
} 
return   randomByte  ; 
} 






public   static   Double   generateRandomDouble  (  )  { 
return   generateRandomDouble  (  false  )  ; 
} 






public   static   Double   generateRandomDouble  (  boolean   unique  )  { 
Double   randomDouble  =  null  ; 
if  (  unique  )  { 
do  { 
randomDouble  =  generator  .  nextDouble  (  )  ; 
}  while  (  set  .  contains  (  randomDouble  )  )  ; 
set  .  add  (  randomDouble  )  ; 
}  else  { 
randomDouble  =  generator  .  nextDouble  (  )  ; 
} 
return   randomDouble  ; 
} 







public   static   BigDecimal   generateRandomDecimal  (  int   integral  ,  int   fraction  )  { 
return   generateRandomDecimal  (  integral  ,  fraction  ,  false  )  ; 
} 








public   static   BigDecimal   generateRandomDecimal  (  int   integral  ,  int   fraction  ,  boolean   unique  )  { 
String   integralString  =  null  ; 
String   fractionString  =  null  ; 
String   decimalString  =  null  ; 
BigDecimal   randomBigDecimal  =  null  ; 
do  { 
integralString  =  generateRandomNumericString  (  integral  -  fraction  )  ; 
fractionString  =  generateRandomNumericString  (  fraction  )  ; 
decimalString  =  integralString  +  "."  +  fractionString  ; 
randomBigDecimal  =  new   BigDecimal  (  decimalString  )  ; 
}  while  (  unique  &&  set  .  contains  (  randomBigDecimal  )  )  ; 
if  (  unique  )  { 
set  .  add  (  randomBigDecimal  )  ; 
} 
return   randomBigDecimal  ; 
} 






public   static   Integer   generateNumericCountryCode  (  )  { 
return   generateNumericCountryCode  (  false  )  ; 
} 






public   static   Integer   generateNumericCountryCode  (  boolean   unique  )  { 
Integer   countryCode  =  null  ; 
do  { 
int   choice  =  generator  .  nextInt  (  CountryCodes  .  getCountryCodesNumLength  (  )  )  ; 
countryCode  =  Integer  .  parseInt  (  CountryCodes  .  getCountryCodesNum  (  choice  )  )  ; 
}  while  (  unique  &&  set  .  contains  (  countryCode  )  )  ; 
if  (  unique  )  { 
set  .  add  (  countryCode  )  ; 
} 
return   countryCode  ; 
} 






public   static   String   generateCountryCode  (  )  { 
return   generateCountryCode  (  false  )  ; 
} 






public   static   String   generateCountryCode  (  boolean   unique  )  { 
String   countryCode  =  null  ; 
if  (  unique  )  { 
do  { 
int   choice  =  generator  .  nextInt  (  CountryCodes  .  getCountryCodesAcLength  (  )  )  ; 
countryCode  =  CountryCodes  .  getCountryCodesAc  (  choice  )  ; 
}  while  (  set  .  contains  (  countryCode  )  )  ; 
set  .  add  (  countryCode  )  ; 
}  else  { 
int   choice  =  generator  .  nextInt  (  CountryCodes  .  getCountryCodesAcLength  (  )  )  ; 
countryCode  =  CountryCodes  .  getCountryCodesAc  (  choice  )  ; 
} 
return   countryCode  ; 
} 







public   static   Byte  [  ]  generateRandomBinary  (  int   count  )  { 
return   generateRandomBinary  (  count  ,  false  )  ; 
} 







public   static   Byte  [  ]  generateRandomBinary  (  int   count  ,  boolean   unique  )  { 
byte  [  ]  data  =  new   byte  [  Math  .  min  (  100000  ,  count  )  ]  ; 
if  (  unique  )  { 
do  { 
generator  .  nextBytes  (  data  )  ; 
}  while  (  set  .  contains  (  data  )  )  ; 
set  .  add  (  data  )  ; 
}  else  { 
generator  .  nextBytes  (  data  )  ; 
} 
return   ArrayUtils  .  toObject  (  data  )  ; 
} 






public   static   Boolean   generateRandomBoolean  (  )  { 
return   generator  .  nextBoolean  (  )  ; 
} 






public   static   Enum  <  ?  >  generateRandomEnum  (  Enum  <  ?  >  [  ]  currentEnum  )  { 
int   x  =  generator  .  nextInt  (  currentEnum  .  length  )  ; 
return   currentEnum  [  x  ]  ; 
} 






public   static   java  .  sql  .  Timestamp   generateDate  (  )  { 
Date   date  =  new   Date  (  )  ; 
Calendar   cal  =  new   GregorianCalendar  (  )  ; 
cal  .  setTime  (  date  )  ; 
cal  .  set  (  Calendar  .  HOUR_OF_DAY  ,  0  )  ; 
cal  .  set  (  Calendar  .  MINUTE  ,  0  )  ; 
cal  .  set  (  Calendar  .  SECOND  ,  0  )  ; 
cal  .  set  (  Calendar  .  MILLISECOND  ,  0  )  ; 
return   new   java  .  sql  .  Timestamp  (  cal  .  getTime  (  )  .  getTime  (  )  )  ; 
} 







public   static   java  .  sql  .  Timestamp   generateRandomFutureDate  (  Date   presentDate  )  { 
Date   date  =  new   Date  (  presentDate  .  getTime  (  )  +  generateRandomLong  (  )  )  ; 
Calendar   cal  =  new   GregorianCalendar  (  )  ; 
cal  .  setTime  (  date  )  ; 
cal  .  set  (  Calendar  .  HOUR_OF_DAY  ,  0  )  ; 
cal  .  set  (  Calendar  .  MINUTE  ,  0  )  ; 
cal  .  set  (  Calendar  .  SECOND  ,  0  )  ; 
cal  .  set  (  Calendar  .  MILLISECOND  ,  0  )  ; 
return   new   java  .  sql  .  Timestamp  (  cal  .  getTime  (  )  .  getTime  (  )  )  ; 
} 







public   static   java  .  sql  .  Timestamp   generateRandomPastDate  (  Date   presentDate  )  { 
Date   date  =  new   Date  (  presentDate  .  getTime  (  )  -  generateRandomLong  (  )  )  ; 
Calendar   cal  =  new   GregorianCalendar  (  )  ; 
cal  .  setTime  (  date  )  ; 
cal  .  set  (  Calendar  .  HOUR_OF_DAY  ,  0  )  ; 
cal  .  set  (  Calendar  .  MINUTE  ,  0  )  ; 
cal  .  set  (  Calendar  .  SECOND  ,  0  )  ; 
cal  .  set  (  Calendar  .  MILLISECOND  ,  0  )  ; 
return   new   java  .  sql  .  Timestamp  (  cal  .  getTime  (  )  .  getTime  (  )  )  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T   extends   Enum  <  ?  >  >  T   generateRandomEnumFromClass  (  String   className  )  { 
Class  <  T  >  loadedClass  =  null  ; 
try  { 
loadedClass  =  (  Class  <  T  >  )  Class  .  forName  (  className  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return  (  generateRandomEnumFromClass  (  loadedClass  )  )  ; 
} 







public   static  <  T   extends   Enum  <  ?  >  >  T   generateRandomEnumFromClass  (  Class  <  T  >  c  )  { 
T  [  ]  enums  =  c  .  getEnumConstants  (  )  ; 
ArrayList  <  T  >  enumList  =  new   ArrayList  <  T  >  (  )  ; 
for  (  T   enumObject  :  enums  )  { 
enumList  .  add  (  enumObject  )  ; 
} 
int   choice  =  generator  .  nextInt  (  enumList  .  size  (  )  )  ; 
T   enumToReturn  =  enumList  .  get  (  choice  )  ; 
return   enumToReturn  ; 
} 
} 

