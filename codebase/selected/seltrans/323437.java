package   cn  .  myapps  .  core  .  workflow  .  utility  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  Vector  ; 







public   class   CommonUtil  { 

public   static   final   int   ADD_BACK  =  1  ; 

public   static   final   int   ADD_FORE  =  2  ; 







public   static   String   getCurrentDateTime  (  )  { 
String   week  =  new   String  (  "日一二三四五六"  )  ; 
Date   currentDate  =  new   Date  (  )  ; 
String   str  =  ""  ; 
str  =  (  2000  +  currentDate  .  getYear  (  )  %  100  )  +  "年 "  ; 
str  =  str  +  currentDate  .  getMonth  (  )  +  "月"  ; 
str  =  str  +  currentDate  .  getDate  (  )  +  "日 "  ; 
int   day  =  currentDate  .  getDay  (  )  ; 
str  =  str  +  "星期"  +  week  .  substring  (  day  ,  day  +  1  )  +  " "  ; 
str  =  str  +  currentDate  .  getHours  (  )  +  "点 "  ; 
str  =  str  +  currentDate  .  getMinutes  (  )  +  "分 "  ; 
str  =  str  +  currentDate  .  getSeconds  (  )  +  "秒 "  ; 
return   str  ; 
} 









public   static   String   getNext  (  String   curr  ,  int   len  )  { 
if  (  curr  ==  null  )  { 
return   CommonUtil  .  setChar  (  '0'  ,  len  )  ; 
} 
String   ret  =  ""  ; 
long   lValue  =  new   Long  (  curr  )  .  longValue  (  )  +  1  ; 
ret  +=  lValue  ; 
ret  =  CommonUtil  .  setChar  (  '0'  ,  len  -  ret  .  length  (  )  )  +  ret  ; 
return   ret  ; 
} 

public   static   boolean   getBoolean  (  String   str  )  { 
if  (  isBooleanString  (  str  )  )  { 
return  (  new   Boolean  (  str  )  )  .  booleanValue  (  )  ; 
}  else  { 
return   false  ; 
} 
} 










public   static   String   setChar  (  char   chr  ,  int   totalLen  )  { 
return   CommonUtil  .  setChar  (  ""  ,  chr  ,  totalLen  ,  CommonUtil  .  ADD_FORE  )  ; 
} 











public   static   String   setChar  (  String   str  ,  char   chr  ,  int   totalLen  )  { 
return   CommonUtil  .  setChar  (  str  ,  chr  ,  totalLen  ,  CommonUtil  .  ADD_FORE  )  ; 
} 












public   static   String   setChar  (  String   str  ,  char   chr  ,  int   totalLen  ,  int   position  )  { 
if  (  totalLen  <  0  ||  str  ==  null  )  { 
return   str  ; 
} 
int   sLen  =  str  .  length  (  )  ; 
String   ret  =  str  ; 
for  (  int   i  =  0  ;  i  <  totalLen  -  sLen  ;  i  ++  )  { 
if  (  position  ==  CommonUtil  .  ADD_BACK  )  { 
ret  =  ret  +  chr  ; 
}  else  { 
ret  =  chr  +  ret  ; 
} 
} 
return   ret  ; 
} 






public   static   boolean   isValidDate  (  String   strDate  )  { 
try  { 
return   strToDate  (  strDate  )  !=  null  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 









public   static   boolean   isNumberString  (  String   str  )  { 
if  (  str  ==  null  ||  str  .  trim  (  )  .  equals  (  ""  )  )  { 
return   false  ; 
} 
try  { 
double   t  =  new   Double  (  str  )  .  doubleValue  (  )  ; 
}  catch  (  Exception   e  )  { 
CommonUtil  .  printDebugInfo  (  "it is not a number!"  )  ; 
return   false  ; 
} 
return   true  ; 
} 

public   static   boolean   isBooleanString  (  String   str  )  { 
if  (  str  ==  null  ||  str  .  trim  (  )  .  equals  (  ""  )  )  { 
return   false  ; 
} 
try  { 
Boolean   t  =  new   Boolean  (  str  )  ; 
}  catch  (  Exception   e  )  { 
CommonUtil  .  printDebugInfo  (  "it is not a boolean!"  )  ; 
return   false  ; 
} 
return   true  ; 
} 

public   static   int   getFirstIndexInStrArray  (  String  [  ]  list  ,  String   str  )  { 
int   pos  =  -  1  ; 
if  (  list  !=  null  &&  list  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  list  .  length  ;  i  ++  )  { 
if  (  list  [  i  ]  !=  null  &&  list  [  i  ]  .  equals  (  str  )  )  { 
pos  =  i  ; 
break  ; 
} 
} 
} 
return   pos  ; 
} 









public   static   boolean   isLetterString  (  String   str  )  { 
if  (  str  ==  null  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
if  (  !  Character  .  isLetter  (  str  .  charAt  (  i  )  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 









public   static   boolean   isLetterOrDigitString  (  String   str  )  { 
if  (  str  ==  null  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
if  (  !  Character  .  isLetterOrDigit  (  str  .  charAt  (  i  )  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 









public   static   boolean   isNumberString  (  String   str  ,  int   bit  )  { 
if  (  bit  <=  0  )  { 
return   false  ; 
} 
if  (  isNumberString  (  str  )  &&  str  .  length  (  )  <=  bit  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 






public   static   String   getDateStr  (  Date   date  )  { 
if  (  date  ==  null  )  { 
return  ""  ; 
} 
int   year  =  date  .  getYear  (  )  +  1900  ; 
int   month  =  date  .  getMonth  (  )  +  1  ; 
int   day  =  date  .  getDate  (  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  ; 
} 






public   static   String   getCurrentDateToSerializeNo  (  )  { 
Date   date  =  new   Date  (  )  ; 
int   year  =  date  .  getYear  (  )  +  1900  ; 
int   month  =  date  .  getMonth  (  )  +  1  ; 
int   day  =  date  .  getDate  (  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  ; 
} 






public   static   String   getTimeStr  (  java  .  sql  .  Time   time  )  { 
if  (  time  ==  null  )  { 
return  ""  ; 
} 
return  " "  +  time  .  getHours  (  )  +  ":"  +  time  .  getMinutes  (  )  +  ":"  +  "00"  ; 
} 









public   static   java  .  sql  .  Date   strToDate  (  String   str  )  { 
String   dtstr  =  new   String  (  str  )  ; 
dtstr  .  replace  (  '/'  ,  '-'  )  ; 
while  (  dtstr  .  indexOf  (  "  "  )  >=  0  )  { 
dtstr  =  dtstr  .  substring  (  0  ,  dtstr  .  indexOf  (  "  "  )  )  +  dtstr  .  substring  (  dtstr  .  indexOf  (  "  "  )  +  1  )  ; 
} 
if  (  dtstr  ==  null  ||  dtstr  .  length  (  )  <  8  )  { 
return   null  ; 
} 
int   len  =  str  .  length  (  )  ; 
String   year  =  "0"  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
year  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  substring  (  1  )  ; 
} 
String   month  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
month  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   date  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
date  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   hour  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
hour  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   minute  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
minute  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   second  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
second  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
int   iyear  =  new   Integer  (  year  )  .  intValue  (  )  -  1900  ; 
int   imonth  =  new   Integer  (  month  )  .  intValue  (  )  -  1  ; 
int   idate  =  new   Integer  (  date  )  .  intValue  (  )  ; 
int   ihour  =  new   Integer  (  hour  )  .  intValue  (  )  ; 
int   iminute  =  new   Integer  (  minute  )  .  intValue  (  )  ; 
int   isecond  =  new   Integer  (  second  )  .  intValue  (  )  ; 
java  .  util  .  Date   uDate  =  new   java  .  util  .  Date  (  iyear  ,  imonth  ,  idate  ,  ihour  ,  iminute  ,  isecond  )  ; 
java  .  sql  .  Date   sDate  =  new   java  .  sql  .  Date  (  0  )  ; 
sDate  .  setTime  (  uDate  .  getTime  (  )  )  ; 
return   sDate  ; 
} 









public   static   String   createStandDateStr  (  String   strDate  )  { 
String   year  ,  month  ,  day  ; 
String   hour  =  "00"  ; 
String   minute  =  "00"  ; 
String   second  =  "00"  ; 
int   i  =  0  ; 
String   sub  =  strDate  .  substring  (  0  )  ; 
if  (  sub  .  indexOf  (  "-"  )  <  0  )  { 
return   null  ; 
} 
year  =  sub  .  substring  (  i  ,  sub  .  indexOf  (  "-"  )  )  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  year  ,  4  )  )  { 
return   null  ; 
} 
year  =  CommonUtil  .  setChar  (  year  ,  '0'  ,  4  )  ; 
i  =  sub  .  indexOf  (  "-"  )  +  1  ; 
sub  =  sub  .  substring  (  i  )  ; 
if  (  sub  .  indexOf  (  "-"  )  <  0  )  { 
return   null  ; 
} 
month  =  sub  .  substring  (  0  ,  sub  .  indexOf  (  "-"  )  )  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  month  ,  2  )  )  { 
return   null  ; 
} 
month  =  CommonUtil  .  setChar  (  month  ,  '0'  ,  2  )  ; 
i  =  sub  .  indexOf  (  "-"  )  +  1  ; 
sub  =  sub  .  substring  (  i  )  ; 
if  (  sub  .  indexOf  (  " "  )  <  0  )  { 
day  =  sub  .  trim  (  )  ; 
if  (  day  .  length  (  )  <=  0  )  { 
return   null  ; 
} 
if  (  day  .  length  (  )  <=  2  )  { 
day  =  sub  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  day  ,  2  )  )  { 
return   null  ; 
} 
day  =  CommonUtil  .  setChar  (  day  ,  '0'  ,  2  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  second  ; 
}  else  { 
return   null  ; 
} 
}  else  { 
day  =  sub  .  substring  (  0  ,  sub  .  indexOf  (  " "  )  )  .  trim  (  )  ; 
if  (  day  .  length  (  )  <=  2  )  { 
day  =  day  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  day  ,  2  )  )  { 
return   null  ; 
} 
day  =  CommonUtil  .  setChar  (  day  ,  '0'  ,  2  )  ; 
}  else  { 
return   null  ; 
} 
} 
i  =  sub  .  indexOf  (  " "  )  +  1  ; 
sub  =  sub  .  substring  (  i  )  ; 
if  (  sub  .  indexOf  (  ":"  )  <  0  )  { 
hour  =  sub  .  trim  (  )  ; 
if  (  hour  .  length  (  )  <=  0  )  { 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  "00"  +  ":"  +  minute  +  ":"  +  second  ; 
} 
if  (  hour  .  length  (  )  <=  2  )  { 
hour  =  hour  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  hour  ,  2  )  )  { 
return   null  ; 
} 
hour  =  CommonUtil  .  setChar  (  hour  ,  '0'  ,  2  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  second  ; 
}  else  { 
return   null  ; 
} 
}  else  { 
hour  =  sub  .  substring  (  0  ,  sub  .  indexOf  (  ":"  )  )  .  trim  (  )  ; 
if  (  hour  .  length  (  )  <=  2  )  { 
hour  =  hour  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  day  ,  2  )  )  { 
return   null  ; 
} 
hour  =  CommonUtil  .  setChar  (  hour  ,  '0'  ,  2  )  ; 
}  else  { 
return   null  ; 
} 
} 
i  =  sub  .  indexOf  (  ":"  )  +  1  ; 
sub  =  sub  .  substring  (  i  )  ; 
if  (  sub  .  indexOf  (  ":"  )  <  0  )  { 
minute  =  sub  .  trim  (  )  ; 
if  (  minute  .  length  (  )  <=  0  )  { 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  "00"  +  ":"  +  second  ; 
} 
if  (  minute  .  length  (  )  <=  2  )  { 
minute  =  sub  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  minute  ,  2  )  )  { 
return   null  ; 
} 
minute  =  CommonUtil  .  setChar  (  minute  ,  '0'  ,  2  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  second  ; 
}  else  { 
return   null  ; 
} 
}  else  { 
minute  =  sub  .  substring  (  0  ,  sub  .  indexOf  (  ":"  )  )  .  trim  (  )  ; 
if  (  minute  .  length  (  )  <=  2  )  { 
minute  =  minute  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  minute  ,  2  )  )  { 
return   null  ; 
} 
minute  =  CommonUtil  .  setChar  (  minute  ,  '0'  ,  2  )  ; 
}  else  { 
return   null  ; 
} 
} 
i  =  sub  .  indexOf  (  ":"  )  +  1  ; 
sub  =  sub  .  substring  (  i  )  ; 
second  =  sub  .  trim  (  )  ; 
if  (  second  .  length  (  )  <=  0  )  { 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  "00"  ; 
} 
if  (  second  .  length  (  )  <=  2  )  { 
second  =  sub  .  trim  (  )  ; 
if  (  !  CommonUtil  .  isNumberString  (  second  ,  2  )  )  { 
return   null  ; 
} 
second  =  CommonUtil  .  setChar  (  second  ,  '0'  ,  2  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  second  ; 
}  else  { 
return   null  ; 
} 
} 






public   static   boolean   isValidTime  (  String   strTime  )  { 
try  { 
return   strToTime  (  strTime  )  !=  null  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 









public   static   java  .  sql  .  Time   strToTime  (  String   str  )  { 
String   dtstr  =  new   String  (  str  )  ; 
dtstr  .  replace  (  '/'  ,  '-'  )  ; 
while  (  dtstr  .  indexOf  (  "  "  )  >=  0  )  { 
dtstr  =  dtstr  .  substring  (  0  ,  dtstr  .  indexOf  (  "  "  )  )  +  dtstr  .  substring  (  dtstr  .  indexOf  (  "  "  )  +  1  )  ; 
} 
if  (  dtstr  ==  null  ||  dtstr  .  length  (  )  <  3  )  return   null  ; 
int   len  =  str  .  length  (  )  ; 
String   hour  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
hour  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   minute  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
minute  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
String   second  =  "0"  ; 
dtstr  =  dtstr  .  trim  (  )  .  length  (  )  >  0  ?  dtstr  .  trim  (  )  .  substring  (  1  )  :  ""  ; 
while  (  !  dtstr  .  equals  (  ""  )  &&  !  dtstr  .  startsWith  (  "-"  )  &&  !  dtstr  .  startsWith  (  ":"  )  &&  !  dtstr  .  startsWith  (  " "  )  )  { 
second  +=  dtstr  .  charAt  (  0  )  ; 
dtstr  =  dtstr  .  trim  (  )  .  substring  (  1  )  ; 
} 
int   ihour  =  new   Integer  (  hour  )  .  intValue  (  )  ; 
int   iminute  =  new   Integer  (  minute  )  .  intValue  (  )  ; 
int   isecond  =  new   Integer  (  second  )  .  intValue  (  )  ; 
java  .  sql  .  Time   sTime  =  new   java  .  sql  .  Time  (  ihour  ,  iminute  ,  isecond  )  ; 
return   sTime  ; 
} 







public   static   String  [  ]  split  (  String   src  ,  char   sepChar  )  { 
Vector   v  =  new   Vector  (  )  ; 
if  (  src  ==  null  ||  src  .  trim  (  )  .  equals  (  ""  )  )  { 
return   null  ; 
}  else  { 
String   s  =  src  ; 
String   elm  =  null  ; 
int   p  =  -  1  ; 
while  (  s  .  length  (  )  >  0  )  { 
p  =  s  .  indexOf  (  sepChar  )  ; 
if  (  p  >=  0  )  { 
elm  =  s  .  substring  (  0  ,  p  )  ; 
v  .  addElement  (  elm  )  ; 
s  =  s  .  substring  (  p  +  1  )  ; 
}  else  { 
if  (  s  !=  null  )  { 
v  .  addElement  (  s  )  ; 
} 
s  =  ""  ; 
} 
} 
String   rtn  [  ]  =  new   String  [  v  .  size  (  )  ]  ; 
Enumeration   enum11  =  v  .  elements  (  )  ; 
int   i  =  0  ; 
while  (  enum11  .  hasMoreElements  (  )  )  { 
Object   item  =  (  Object  )  enum11  .  nextElement  (  )  ; 
rtn  [  i  ]  =  (  String  )  item  ; 
i  ++  ; 
} 
return   rtn  ; 
} 
} 

public   static   String  [  ]  split  (  String   str  ,  String   separator  )  { 
StringTokenizer   tok  =  null  ; 
int   max  =  -  1  ; 
if  (  separator  ==  null  )  { 
tok  =  new   StringTokenizer  (  str  )  ; 
}  else  { 
tok  =  new   StringTokenizer  (  str  ,  separator  )  ; 
} 
int   listSize  =  tok  .  countTokens  (  )  ; 
if  (  max  >  0  &&  listSize  >  max  )  { 
listSize  =  max  ; 
} 
String  [  ]  list  =  new   String  [  listSize  ]  ; 
int   i  =  0  ; 
int   lastTokenBegin  =  0  ; 
int   lastTokenEnd  =  0  ; 
while  (  tok  .  hasMoreTokens  (  )  )  { 
if  (  max  >  0  &&  i  ==  listSize  -  1  )  { 
String   endToken  =  tok  .  nextToken  (  )  ; 
lastTokenBegin  =  str  .  indexOf  (  endToken  ,  lastTokenEnd  )  ; 
list  [  i  ]  =  str  .  substring  (  lastTokenBegin  )  ; 
break  ; 
}  else  { 
list  [  i  ]  =  tok  .  nextToken  (  )  ; 
lastTokenBegin  =  str  .  indexOf  (  list  [  i  ]  ,  lastTokenEnd  )  ; 
lastTokenEnd  =  lastTokenBegin  +  list  [  i  ]  .  length  (  )  ; 
} 
i  ++  ; 
} 
return   list  ; 
} 






public   static   String   arrayToString  (  String  [  ]  strs  ,  char   splt  )  { 
String   rtn  =  ""  ; 
if  (  strs  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  strs  .  length  ;  i  ++  )  { 
if  (  i  <  strs  .  length  )  { 
rtn  +=  strs  [  i  ]  +  splt  ; 
}  else  { 
rtn  +=  strs  [  i  ]  ; 
} 
} 
} 
return   rtn  ; 
} 




public   static   String   strToPathfrmt  (  String   str  )  { 
String   s  =  ""  ; 
int   pos  =  0  ; 
pos  =  str  .  indexOf  (  "\\"  )  ; 
while  (  pos  >  0  )  { 
s  +=  str  .  substring  (  0  ,  pos  )  +  "\\\\"  ; 
str  =  str  .  substring  (  pos  +  1  )  ; 
CommonUtil  .  printDebugInfo  (  str  )  ; 
pos  =  str  .  indexOf  (  "\\"  )  ; 
} 
s  +=  str  ; 
return   s  ; 
} 






public   static   int  [  ]  getDateParts  (  String   str  )  { 
String  [  ]  adstr  =  split  (  str  ,  '-'  )  ; 
int  [  ]  datePart  =  new   int  [  6  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  datePart  .  length  ;  i  ++  )  { 
datePart  [  i  ]  =  Integer  .  parseInt  (  adstr  [  i  ]  )  ; 
} 
return   datePart  ; 
} 






public   static   String  [  ]  sort  (  String  [  ]  strs  )  { 
int   i  =  0  ,  j  =  1  ,  len  =  strs  .  length  ; 
if  (  len  <=  1  )  { 
return   strs  ; 
} 
String   strTmp  =  null  ; 
for  (  i  =  0  ;  i  <  len  -  1  ;  i  ++  )  { 
for  (  j  =  i  +  1  ;  j  <  len  ;  j  ++  )  { 
if  (  strs  [  i  ]  .  compareTo  (  strs  [  j  ]  )  >  0  )  { 
strTmp  =  strs  [  i  ]  ; 
strs  [  i  ]  =  strs  [  j  ]  ; 
strs  [  j  ]  =  strTmp  ; 
} 
} 
} 
return   strs  ; 
} 





public   static   String   getCurrDateStr  (  )  { 
java  .  util  .  Date   date  =  new   Date  (  )  ; 
int   year  =  date  .  getYear  (  )  +  1900  ; 
int   month  =  date  .  getMonth  (  )  +  1  ; 
int   day  =  date  .  getDate  (  )  ; 
int   hour  =  date  .  getHours  (  )  ; 
int   minute  =  date  .  getMinutes  (  )  ; 
int   second  =  date  .  getSeconds  (  )  ; 
return   year  +  "-"  +  month  +  "-"  +  day  +  " "  +  hour  +  ":"  +  minute  +  ":"  +  second  ; 
} 






public   static   String  [  ]  moneyToRMB  (  long   money  )  { 
String  [  ]  returnStr  =  {  ""  ,  ""  ,  ""  ,  ""  ,  ""  ,  ""  ,  ""  ,  ""  ,  ""  ,  ""  }  ; 
int   i  ,  j  ,  index  ; 
long   mod  ,  result  ; 
String  [  ]  RMB  =  {  "零"  ,  "壹"  ,  "贰"  ,  "叁"  ,  "肆"  ,  "伍"  ,  "陆"  ,  "柒"  ,  "捌"  ,  "玖"  }  ; 
for  (  i  =  0  ;  i  <  10  ;  i  ++  )  { 
mod  =  1  ; 
for  (  j  =  0  ;  j  <  (  9  -  i  )  ;  j  ++  )  { 
mod  =  mod  *  10  ; 
} 
result  =  money  /  mod  ; 
Long   tmp  =  new   Long  (  result  )  ; 
index  =  tmp  .  intValue  (  )  ; 
if  (  result  ==  0  )  { 
returnStr  [  9  -  i  ]  =  RMB  [  0  ]  ; 
}  else  { 
try  { 
returnStr  [  9  -  i  ]  =  RMB  [  index  ]  ; 
money  =  money  -  mod  *  result  ; 
}  catch  (  Exception   e  )  { 
CommonUtil  .  printDebugInfo  (  " CommonUtil.java.moneyToRMB() error"  +  e  .  toString  (  )  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
return   returnStr  ; 
} 








public   static   String   padding  (  String   str  ,  int   length  ,  char   c  )  { 
int   i  ,  len  ; 
len  =  length  -  str  .  length  (  )  ; 
for  (  i  =  0  ;  i  <  len  ;  i  ++  )  { 
str  =  c  +  str  ; 
} 
return   str  ; 
} 







public   static   String   gbTo8859  (  String   str  )  { 
return   str  ; 
} 







public   static   String   toGB  (  String   str  )  { 
return   str  ; 
} 

public   static   double   twoPrecision  (  double   d  )  { 
long   l  =  (  long  )  (  d  *  100  +  0.5  )  ; 
return   l  /  100  ; 
} 

public   static   float   twoPrecision  (  float   f  )  { 
long   l  =  (  long  )  (  f  *  100  +  0.5  )  ; 
return   l  /  100  ; 
} 

public   static   double   iPrecision  (  double   d  ,  int   i  )  { 
long   l  =  (  long  )  (  d  *  (  10  ^  i  )  +  0.5  )  ; 
return   l  /  10  ^  i  ; 
} 






public   static   void   printDebugInfo  (  java  .  lang  .  Object   obj  )  { 
} 

public   static   String   native2Unicode  (  String   s  )  { 
if  (  s  ==  null  ||  s  .  length  (  )  ==  0  )  { 
return   null  ; 
} 
byte  [  ]  buffer  =  new   byte  [  s  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
buffer  [  i  ]  =  (  byte  )  s  .  charAt  (  i  )  ; 
} 
return   new   String  (  buffer  )  ; 
} 

public   static   String   unicode2Native  (  String   s  )  { 
if  (  s  ==  null  ||  s  .  length  (  )  ==  0  )  { 
return   null  ; 
} 
char  [  ]  buffer  =  new   char  [  s  .  length  (  )  *  2  ]  ; 
char   c  ; 
int   j  =  0  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  s  .  charAt  (  i  )  >=  0x100  )  { 
c  =  s  .  charAt  (  i  )  ; 
byte  [  ]  buf  =  (  ""  +  c  )  .  getBytes  (  )  ; 
buffer  [  j  ++  ]  =  (  char  )  buf  [  0  ]  ; 
buffer  [  j  ++  ]  =  (  char  )  buf  [  1  ]  ; 
}  else  { 
buffer  [  j  ++  ]  =  s  .  charAt  (  i  )  ; 
} 
} 
return   new   String  (  buffer  ,  0  ,  j  )  ; 
} 

public   static   String   encodeToMD5  (  String   str  )  { 
if  (  str  ==  null  )  { 
return   null  ; 
} 
String   digstr  =  ""  ; 
MessageDigest   MD  =  null  ; 
try  { 
MD  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
byte  [  ]  oldbyte  =  new   byte  [  str  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
oldbyte  [  i  ]  =  (  byte  )  str  .  charAt  (  i  )  ; 
} 
MD  .  update  (  oldbyte  )  ; 
byte  [  ]  newbyte  =  null  ; 
newbyte  =  MD  .  digest  (  oldbyte  )  ; 
for  (  int   i  =  0  ;  i  <  newbyte  .  length  ;  i  ++  )  { 
digstr  =  digstr  +  newbyte  [  i  ]  ; 
} 
return   digstr  ; 
} 

public   static   String   foldString  (  String   src  ,  int   length  )  { 
if  (  src  ==  null  ||  src  .  equals  (  ""  )  ||  length  <=  0  )  { 
return   src  ; 
} 
StringBuffer   rtn  =  new   StringBuffer  (  )  ; 
int   pos  =  0  ; 
while  (  pos  <=  src  .  length  (  )  )  { 
if  (  pos  >  0  )  { 
rtn  .  append  (  "\n"  )  ; 
} 
rtn  .  append  (  src  .  substring  (  pos  ,  pos  +  length  <=  src  .  length  (  )  ?  pos  +  length  :  src  .  length  (  )  )  )  ; 
pos  +=  length  ; 
} 
return   rtn  .  toString  (  )  ; 
} 

public   static   String   replaceCharacter  (  String   str  )  { 
if  (  str  !=  null  )  { 
String   tmp  =  str  .  replaceAll  (  "&"  ,  "@amp;"  )  ; 
tmp  =  tmp  .  replaceAll  (  "\""  ,  "@quot;"  )  ; 
tmp  =  tmp  .  replaceAll  (  "<"  ,  "@lt;"  )  ; 
tmp  =  tmp  .  replaceAll  (  ">"  ,  "@gt;"  )  ; 
return   tmp  ; 
} 
return  ""  ; 
} 

public   static   String   undoReplaceCharacter  (  String   str  )  { 
if  (  str  !=  null  )  { 
String   tmp  =  str  .  replaceAll  (  "@amp;"  ,  "&"  )  ; 
tmp  =  tmp  .  replaceAll  (  "@quot;"  ,  "\""  )  ; 
tmp  =  tmp  .  replaceAll  (  "@lt;"  ,  "<"  )  ; 
tmp  =  tmp  .  replaceAll  (  "@gt;"  ,  ">"  )  ; 
return   tmp  ; 
} 
return  ""  ; 
} 

public   static   boolean   compareTime  (  String   beforeDateTime  ,  String   afterDateTime  )  { 
SimpleDateFormat   d  =  new   SimpleDateFormat  (  "yyyy-MM-dd hh:mm:ss"  )  ; 
long   timeCount  =  0  ; 
try  { 
java  .  util  .  Date   d1  =  d  .  parse  (  beforeDateTime  )  ; 
java  .  util  .  Date   d2  =  d  .  parse  (  afterDateTime  )  ; 
return   d2  .  after  (  d1  )  ; 
}  catch  (  ParseException   e  )  { 
return   false  ; 
} 
} 

public   static   double   converStrToDouble  (  String   str  )  { 
str  =  str  .  trim  (  )  ; 
if  (  str  .  indexOf  (  ","  )  >  0  )  str  =  str  .  replaceAll  (  ","  ,  ""  )  ; 
if  (  str  .  indexOf  (  "-"  )  >  0  )  str  =  "-"  +  str  .  replaceAll  (  "-"  ,  ""  )  ; 
return   Double  .  parseDouble  (  str  )  ; 
} 

public   static   void   main  (  String  [  ]  args  )  { 
String   t1  =  "2005-3-25 16:49:30:345"  ; 
String   t2  =  "2005-3-25 16:49:31:362"  ; 
System  .  out  .  println  (  CommonUtil  .  compareTime  (  t1  ,  t2  )  )  ; 
} 
} 

