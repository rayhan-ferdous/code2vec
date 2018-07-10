package   org  .  blueoxygen  .  util  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  BreakIterator  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Random  ; 




public   class   StringUtils  { 

private   static   final   char  [  ]  QUOTE_ENCODE  =  "&quot;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  AMP_ENCODE  =  "&amp;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  LT_ENCODE  =  "&lt;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  GT_ENCODE  =  "&gt;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  APOS_ENCODE  =  "&apos;"  .  toCharArray  (  )  ; 





private   static   Object   initLock  =  new   Object  (  )  ; 













public   static   final   String   replace  (  String   line  ,  String   oldString  ,  String   newString  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
if  (  (  i  =  line  .  indexOf  (  oldString  ,  i  )  )  >=  0  )  { 
char  [  ]  line2  =  line  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  newString  .  toCharArray  (  )  ; 
int   oLength  =  oldString  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  line2  .  length  )  ; 
buf  .  append  (  line2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  line  .  indexOf  (  oldString  ,  i  )  )  >  0  )  { 
buf  .  append  (  line2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  line2  ,  j  ,  line2  .  length  -  j  )  ; 
return   buf  .  toString  (  )  ; 
} 
return   line  ; 
} 














public   static   final   String   replaceIgnoreCase  (  String   line  ,  String   oldString  ,  String   newString  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
String   lcLine  =  line  .  toLowerCase  (  )  ; 
String   lcOldString  =  oldString  .  toLowerCase  (  )  ; 
int   i  =  0  ; 
if  (  (  i  =  lcLine  .  indexOf  (  lcOldString  ,  i  )  )  >=  0  )  { 
char  [  ]  line2  =  line  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  newString  .  toCharArray  (  )  ; 
int   oLength  =  oldString  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  line2  .  length  )  ; 
buf  .  append  (  line2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  lcLine  .  indexOf  (  lcOldString  ,  i  )  )  >  0  )  { 
buf  .  append  (  line2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  line2  ,  j  ,  line2  .  length  -  j  )  ; 
return   buf  .  toString  (  )  ; 
} 
return   line  ; 
} 


















public   static   final   String   replaceIgnoreCase  (  String   line  ,  String   oldString  ,  String   newString  ,  int  [  ]  count  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
String   lcLine  =  line  .  toLowerCase  (  )  ; 
String   lcOldString  =  oldString  .  toLowerCase  (  )  ; 
int   i  =  0  ; 
if  (  (  i  =  lcLine  .  indexOf  (  lcOldString  ,  i  )  )  >=  0  )  { 
int   counter  =  0  ; 
char  [  ]  line2  =  line  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  newString  .  toCharArray  (  )  ; 
int   oLength  =  oldString  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  line2  .  length  )  ; 
buf  .  append  (  line2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  lcLine  .  indexOf  (  lcOldString  ,  i  )  )  >  0  )  { 
counter  ++  ; 
buf  .  append  (  line2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  line2  ,  j  ,  line2  .  length  -  j  )  ; 
count  [  0  ]  =  counter  ; 
return   buf  .  toString  (  )  ; 
} 
return   line  ; 
} 














public   static   final   String   replace  (  String   line  ,  String   oldString  ,  String   newString  ,  int  [  ]  count  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
if  (  (  i  =  line  .  indexOf  (  oldString  ,  i  )  )  >=  0  )  { 
int   counter  =  0  ; 
counter  ++  ; 
char  [  ]  line2  =  line  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  newString  .  toCharArray  (  )  ; 
int   oLength  =  oldString  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  line2  .  length  )  ; 
buf  .  append  (  line2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  line  .  indexOf  (  oldString  ,  i  )  )  >  0  )  { 
counter  ++  ; 
buf  .  append  (  line2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  line2  ,  j  ,  line2  .  length  -  j  )  ; 
count  [  0  ]  =  counter  ; 
return   buf  .  toString  (  )  ; 
} 
return   line  ; 
} 











public   final   String   escapeHTMLTags  (  String   in  )  { 
if  (  in  ==  null  )  { 
return   null  ; 
} 
char   ch  ; 
int   i  =  0  ; 
int   last  =  0  ; 
char  [  ]  input  =  in  .  toCharArray  (  )  ; 
int   len  =  input  .  length  ; 
StringBuffer   out  =  new   StringBuffer  (  (  int  )  (  len  *  1.3  )  )  ; 
for  (  ;  i  <  len  ;  i  ++  )  { 
ch  =  input  [  i  ]  ; 
if  (  ch  >  '>'  )  { 
continue  ; 
}  else   if  (  ch  ==  '<'  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
out  .  append  (  LT_ENCODE  )  ; 
}  else   if  (  ch  ==  '>'  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
out  .  append  (  GT_ENCODE  )  ; 
} 
} 
if  (  last  ==  0  )  { 
return   in  ; 
} 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
return   out  .  toString  (  )  ; 
} 




private   static   MessageDigest   digest  =  null  ; 

























public   static   final   synchronized   String   hash  (  String   data  )  { 
if  (  digest  ==  null  )  { 
try  { 
digest  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
System  .  err  .  println  (  "Failed to load the MD5 MessageDigest. "  +  "unable to function normally."  )  ; 
nsae  .  printStackTrace  (  )  ; 
} 
} 
digest  .  update  (  data  .  getBytes  (  )  )  ; 
return   encodeHex  (  digest  .  digest  (  )  )  ; 
} 













public   static   final   String   encodeHex  (  byte  [  ]  bytes  )  { 
StringBuffer   buf  =  new   StringBuffer  (  bytes  .  length  *  2  )  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  bytes  .  length  ;  i  ++  )  { 
if  (  (  (  int  )  bytes  [  i  ]  &  0xff  )  <  0x10  )  { 
buf  .  append  (  "0"  )  ; 
} 
buf  .  append  (  Long  .  toString  (  (  int  )  bytes  [  i  ]  &  0xff  ,  16  )  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 









public   static   final   byte  [  ]  decodeHex  (  String   hex  )  { 
char  [  ]  chars  =  hex  .  toCharArray  (  )  ; 
byte  [  ]  bytes  =  new   byte  [  chars  .  length  /  2  ]  ; 
int   byteCount  =  0  ; 
for  (  int   i  =  0  ;  i  <  chars  .  length  ;  i  +=  2  )  { 
byte   newByte  =  0x00  ; 
newByte  |=  hexCharToByte  (  chars  [  i  ]  )  ; 
newByte  <<=  4  ; 
newByte  |=  hexCharToByte  (  chars  [  i  +  1  ]  )  ; 
bytes  [  byteCount  ]  =  newByte  ; 
byteCount  ++  ; 
} 
return   bytes  ; 
} 









private   static   final   byte   hexCharToByte  (  char   ch  )  { 
switch  (  ch  )  { 
case  '0'  : 
return   0x00  ; 
case  '1'  : 
return   0x01  ; 
case  '2'  : 
return   0x02  ; 
case  '3'  : 
return   0x03  ; 
case  '4'  : 
return   0x04  ; 
case  '5'  : 
return   0x05  ; 
case  '6'  : 
return   0x06  ; 
case  '7'  : 
return   0x07  ; 
case  '8'  : 
return   0x08  ; 
case  '9'  : 
return   0x09  ; 
case  'a'  : 
return   0x0A  ; 
case  'b'  : 
return   0x0B  ; 
case  'c'  : 
return   0x0C  ; 
case  'd'  : 
return   0x0D  ; 
case  'e'  : 
return   0x0E  ; 
case  'f'  : 
return   0x0F  ; 
} 
return   0x00  ; 
} 








public   String   encodeBase64  (  String   data  )  { 
return   encodeBase64  (  data  .  getBytes  (  )  )  ; 
} 








public   String   encodeBase64  (  byte  [  ]  data  )  { 
int   c  ; 
int   len  =  data  .  length  ; 
StringBuffer   ret  =  new   StringBuffer  (  (  (  len  /  3  )  +  1  )  *  4  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  ++  i  )  { 
c  =  (  data  [  i  ]  >  >  2  )  &  0x3f  ; 
ret  .  append  (  cvt  .  charAt  (  c  )  )  ; 
c  =  (  data  [  i  ]  <<  4  )  &  0x3f  ; 
if  (  ++  i  <  len  )  c  |=  (  data  [  i  ]  >  >  4  )  &  0x0f  ; 
ret  .  append  (  cvt  .  charAt  (  c  )  )  ; 
if  (  i  <  len  )  { 
c  =  (  data  [  i  ]  <<  2  )  &  0x3f  ; 
if  (  ++  i  <  len  )  c  |=  (  data  [  i  ]  >  >  6  )  &  0x03  ; 
ret  .  append  (  cvt  .  charAt  (  c  )  )  ; 
}  else  { 
++  i  ; 
ret  .  append  (  (  char  )  fillchar  )  ; 
} 
if  (  i  <  len  )  { 
c  =  data  [  i  ]  &  0x3f  ; 
ret  .  append  (  cvt  .  charAt  (  c  )  )  ; 
}  else  { 
ret  .  append  (  (  char  )  fillchar  )  ; 
} 
} 
return   ret  .  toString  (  )  ; 
} 









public   String   decodeBase64  (  String   data  )  { 
return   decodeBase64  (  data  .  getBytes  (  )  )  ; 
} 









public   String   decodeBase64  (  byte  [  ]  data  )  { 
int   c  ,  c1  ; 
int   len  =  data  .  length  ; 
StringBuffer   ret  =  new   StringBuffer  (  (  len  *  3  )  /  4  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  ++  i  )  { 
c  =  cvt  .  indexOf  (  data  [  i  ]  )  ; 
++  i  ; 
c1  =  cvt  .  indexOf  (  data  [  i  ]  )  ; 
c  =  (  (  c  <<  2  )  |  (  (  c1  >  >  4  )  &  0x3  )  )  ; 
ret  .  append  (  (  char  )  c  )  ; 
if  (  ++  i  <  len  )  { 
c  =  data  [  i  ]  ; 
if  (  fillchar  ==  c  )  break  ; 
c  =  cvt  .  indexOf  (  (  char  )  c  )  ; 
c1  =  (  (  c1  <<  4  )  &  0xf0  )  |  (  (  c  >  >  2  )  &  0xf  )  ; 
ret  .  append  (  (  char  )  c1  )  ; 
} 
if  (  ++  i  <  len  )  { 
c1  =  data  [  i  ]  ; 
if  (  fillchar  ==  c1  )  break  ; 
c1  =  cvt  .  indexOf  (  (  char  )  c1  )  ; 
c  =  (  (  c  <<  6  )  &  0xc0  )  |  c1  ; 
ret  .  append  (  (  char  )  c  )  ; 
} 
} 
return   ret  .  toString  (  )  ; 
} 

private   static   final   int   fillchar  =  '='  ; 

private   static   final   String   cvt  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ"  +  "abcdefghijklmnopqrstuvwxyz"  +  "0123456789+/"  ; 













public   static   final   String  [  ]  toLowerCaseWordArray  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  length  (  )  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
ArrayList   wordList  =  new   ArrayList  (  )  ; 
BreakIterator   boundary  =  BreakIterator  .  getWordInstance  (  )  ; 
boundary  .  setText  (  text  )  ; 
int   count  =  0  ; 
int   start  =  0  ; 
for  (  int   end  =  boundary  .  next  (  )  ;  end  !=  BreakIterator  .  DONE  ;  start  =  end  ,  end  =  boundary  .  next  (  )  )  { 
String   tmp  =  text  .  substring  (  start  ,  end  )  .  trim  (  )  ; 
tmp  =  replace  (  tmp  ,  "+"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "/"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "\\"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "#"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "*"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  ")"  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "("  ,  ""  )  ; 
tmp  =  replace  (  tmp  ,  "&"  ,  ""  )  ; 
if  (  tmp  .  length  (  )  >  0  )  { 
wordList  .  add  (  tmp  )  ; 
count  ++  ; 
} 
} 
return  (  String  [  ]  )  wordList  .  toArray  (  new   String  [  wordList  .  size  (  )  ]  )  ; 
} 






private   static   final   String  [  ]  commonWords  =  new   String  [  ]  {  "a"  ,  "and"  ,  "as"  ,  "at"  ,  "be"  ,  "do"  ,  "i"  ,  "if"  ,  "in"  ,  "is"  ,  "it"  ,  "so"  ,  "the"  ,  "to"  }  ; 

private   static   Map   commonWordsMap  =  null  ; 






public   static   final   String  [  ]  removeCommonWords  (  String  [  ]  words  )  { 
if  (  commonWordsMap  ==  null  )  { 
synchronized  (  initLock  )  { 
if  (  commonWordsMap  ==  null  )  { 
commonWordsMap  =  new   HashMap  (  )  ; 
for  (  int   i  =  0  ;  i  <  commonWords  .  length  ;  i  ++  )  { 
commonWordsMap  .  put  (  commonWords  [  i  ]  ,  commonWords  [  i  ]  )  ; 
} 
} 
} 
} 
ArrayList   results  =  new   ArrayList  (  words  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  words  .  length  ;  i  ++  )  { 
if  (  !  commonWordsMap  .  containsKey  (  words  [  i  ]  )  )  { 
results  .  add  (  words  [  i  ]  )  ; 
} 
} 
return  (  String  [  ]  )  results  .  toArray  (  new   String  [  results  .  size  (  )  ]  )  ; 
} 






private   static   Random   randGen  =  null  ; 







private   static   char  [  ]  numbersAndLetters  =  null  ; 
















public   final   String   randomString  (  int   length  )  { 
if  (  length  <  1  )  { 
return   null  ; 
} 
if  (  randGen  ==  null  )  { 
synchronized  (  initLock  )  { 
if  (  randGen  ==  null  )  { 
randGen  =  new   Random  (  )  ; 
numbersAndLetters  =  (  "0123456789abcdefghijklmnopqrstuvwxyz"  +  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"  )  .  toCharArray  (  )  ; 
} 
} 
} 
char  [  ]  randBuffer  =  new   char  [  length  ]  ; 
for  (  int   i  =  0  ;  i  <  randBuffer  .  length  ;  i  ++  )  { 
randBuffer  [  i  ]  =  numbersAndLetters  [  randGen  .  nextInt  (  71  )  ]  ; 
} 
return   new   String  (  randBuffer  )  ; 
} 





















public   static   final   String   chopAtWord  (  String   string  ,  int   length  )  { 
if  (  string  ==  null  )  { 
return   string  ; 
} 
char  [  ]  charArray  =  string  .  toCharArray  (  )  ; 
int   sLength  =  string  .  length  (  )  ; 
if  (  length  <  sLength  )  { 
sLength  =  length  ; 
} 
for  (  int   i  =  0  ;  i  <  sLength  -  1  ;  i  ++  )  { 
if  (  charArray  [  i  ]  ==  '\r'  &&  charArray  [  i  +  1  ]  ==  '\n'  )  { 
return   string  .  substring  (  0  ,  i  +  1  )  ; 
}  else   if  (  charArray  [  i  ]  ==  '\n'  )  { 
return   string  .  substring  (  0  ,  i  )  ; 
} 
} 
if  (  charArray  [  sLength  -  1  ]  ==  '\n'  )  { 
return   string  .  substring  (  0  ,  sLength  -  1  )  ; 
} 
if  (  string  .  length  (  )  <  length  )  { 
return   string  ; 
} 
for  (  int   i  =  length  -  1  ;  i  >  0  ;  i  --  )  { 
if  (  charArray  [  i  ]  ==  ' '  )  { 
return   string  .  substring  (  0  ,  i  )  .  trim  (  )  ; 
} 
} 
return   string  .  substring  (  0  ,  length  )  ; 
} 

private   static   final   char  [  ]  zeroArray  =  "0000000000000000"  .  toCharArray  (  )  ; 















public   static   final   String   zeroPadString  (  String   string  ,  int   length  )  { 
StringBuffer   buf  =  new   StringBuffer  (  length  )  ; 
buf  .  append  (  zeroArray  ,  0  ,  length  -  string  .  length  (  )  )  .  append  (  string  )  ; 
return   buf  .  toString  (  )  ; 
} 







public   static   final   String   dateToMillis  (  Date   date  )  { 
return   zeroPadString  (  Long  .  toString  (  date  .  getTime  (  )  )  ,  15  )  ; 
} 









public   final   String   addBackSlashQuote  (  String   str  )  { 
String   str1  ; 
str1  =  (  "s/'/\\\\'/g"  )  ; 
return   str  ; 
} 









public   final   String   replaceLineBreak  (  String   string  )  { 
String   str  =  replace  (  string  ,  "\n"  ,  "<br>"  )  ; 
return   str  ; 
} 









public   final   String   replaceHTMLInput  (  String   string  )  { 
String   str  ; 
str  =  escapeHTMLTags  (  string  )  ; 
str  =  addBackSlashQuote  (  str  )  ; 
str  =  replaceLineBreak  (  str  )  ; 
return   str  ; 
} 
} 

