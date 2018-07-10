package   cn  .  com  .  pxto  .  commons  ; 

import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  BreakIterator  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Random  ; 
import   org  .  apache  .  log4j  .  Logger  ; 





public   class   StringUtils  { 

protected   static   Logger   log  =  Logger  .  getLogger  (  "StringUtils"  )  ; 

private   static   final   char  [  ]  QUOTE_ENCODE  =  "&quot;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  AMP_ENCODE  =  "&amp;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  LT_ENCODE  =  "&lt;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  GT_ENCODE  =  "&gt;"  .  toCharArray  (  )  ; 










public   static   final   String   replace  (  String   string  ,  String   oldString  ,  String   newString  )  { 
if  (  string  ==  null  )  { 
return   null  ; 
} 
if  (  newString  ==  null  )  { 
return   string  ; 
} 
int   i  =  0  ; 
if  (  (  i  =  string  .  indexOf  (  oldString  ,  i  )  )  >=  0  )  { 
char  [  ]  string2  =  string  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  newString  .  toCharArray  (  )  ; 
int   oLength  =  oldString  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  string2  .  length  )  ; 
buf  .  append  (  string2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  string  .  indexOf  (  oldString  ,  i  )  )  >  0  )  { 
buf  .  append  (  string2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  string2  ,  j  ,  string2  .  length  -  j  )  ; 
return   buf  .  toString  (  )  ; 
} 
return   string  ; 
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
int   counter  =  1  ; 
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










public   static   final   StringBuffer   replace  (  StringBuffer   buffer  ,  String   startString  ,  String   endString  ,  String   replaceWith  )  { 
if  (  buffer  .  indexOf  (  startString  )  >  -  1  )  { 
buffer  =  buffer  .  replace  (  buffer  .  indexOf  (  startString  )  ,  buffer  .  indexOf  (  endString  ,  buffer  .  indexOf  (  startString  )  )  +  1  ,  replaceWith  )  ; 
if  (  buffer  .  indexOf  (  startString  )  >  -  1  )  buffer  =  replace  (  buffer  ,  startString  ,  endString  ,  replaceWith  )  ; 
} 
return   buffer  ; 
} 











public   static   final   String   replace  (  String   line  ,  String   oldString  ,  String   newString  ,  int  [  ]  count  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
if  (  (  i  =  line  .  indexOf  (  oldString  ,  i  )  )  >=  0  )  { 
int   counter  =  1  ; 
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








public   static   final   String   stripTags  (  String   in  )  { 
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
if  (  i  +  3  <  len  &&  input  [  i  +  1  ]  ==  'b'  &&  input  [  i  +  2  ]  ==  'r'  &&  input  [  i  +  3  ]  ==  '>'  )  { 
i  +=  3  ; 
continue  ; 
} 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
}  else   if  (  ch  ==  '>'  )  { 
last  =  i  +  1  ; 
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










public   static   final   String   escapeHTMLTags  (  String   in  )  { 
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
log  .  error  (  "Failed to load the MD5 MessageDigest. "  +  "Jive will be unable to function normally."  ,  nsae  )  ; 
} 
} 
try  { 
digest  .  update  (  data  .  getBytes  (  "utf-8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
log  .  error  (  e  )  ; 
} 
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
int   newByte  =  0x00  ; 
newByte  |=  hexCharToByte  (  chars  [  i  ]  )  ; 
newByte  <<=  4  ; 
newByte  |=  hexCharToByte  (  chars  [  i  +  1  ]  )  ; 
bytes  [  byteCount  ]  =  (  byte  )  newByte  ; 
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







public   static   String   encodeBase64  (  String   data  )  { 
byte  [  ]  bytes  =  null  ; 
try  { 
bytes  =  data  .  getBytes  (  "ISO-8859-1"  )  ; 
}  catch  (  UnsupportedEncodingException   uee  )  { 
log  .  error  (  uee  )  ; 
} 
return   encodeBase64  (  bytes  )  ; 
} 







public   static   String   encodeBase64  (  byte  [  ]  data  )  { 
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







public   static   String   decodeBase64  (  String   data  )  { 
byte  [  ]  bytes  =  null  ; 
try  { 
bytes  =  data  .  getBytes  (  "ISO-8859-1"  )  ; 
}  catch  (  UnsupportedEncodingException   uee  )  { 
log  .  error  (  uee  )  ; 
} 
return   decodeBase64  (  bytes  )  ; 
} 







public   static   String   decodeBase64  (  byte  [  ]  data  )  { 
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
c  =  cvt  .  indexOf  (  c  )  ; 
c1  =  (  (  c1  <<  4  )  &  0xf0  )  |  (  (  c  >  >  2  )  &  0xf  )  ; 
ret  .  append  (  (  char  )  c1  )  ; 
} 
if  (  ++  i  <  len  )  { 
c1  =  data  [  i  ]  ; 
if  (  fillchar  ==  c1  )  break  ; 
c1  =  cvt  .  indexOf  (  c1  )  ; 
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
} 
} 
return  (  String  [  ]  )  wordList  .  toArray  (  new   String  [  wordList  .  size  (  )  ]  )  ; 
} 






private   static   Random   randGen  =  new   Random  (  )  ; 







private   static   char  [  ]  numbersAndLetters  =  (  "0123456789abcdefghijklmnopqrstuvwxyz"  +  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"  )  .  toCharArray  (  )  ; 














public   static   final   String   randomString  (  int   length  )  { 
if  (  length  <  1  )  { 
return   null  ; 
} 
char  [  ]  randBuffer  =  new   char  [  length  ]  ; 
for  (  int   i  =  0  ;  i  <  randBuffer  .  length  ;  i  ++  )  { 
randBuffer  [  i  ]  =  numbersAndLetters  [  randGen  .  nextInt  (  71  )  ]  ; 
} 
return   new   String  (  randBuffer  )  ; 
} 


















public   static   final   String   chopAtWord  (  String   string  ,  int   length  )  { 
if  (  string  ==  null  ||  string  .  length  (  )  ==  0  )  { 
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
if  (  string  .  length  (  )  <=  length  )  { 
return   string  ; 
} 
for  (  int   i  =  length  -  1  ;  i  >  0  ;  i  --  )  { 
if  (  charArray  [  i  ]  ==  ' '  )  { 
return   string  .  substring  (  0  ,  i  )  .  trim  (  )  ; 
} 
} 
return   string  .  substring  (  0  ,  length  )  ; 
} 
















public   static   String   chopAtWordsAround  (  String   input  ,  String  [  ]  wordList  ,  int   numChars  )  { 
if  (  input  ==  null  ||  ""  .  equals  (  input  .  trim  (  )  )  ||  wordList  ==  null  ||  wordList  .  length  ==  0  ||  numChars  ==  0  )  { 
return   null  ; 
} 
String   lc  =  input  .  toLowerCase  (  )  ; 
for  (  int   i  =  0  ;  i  <  wordList  .  length  ;  i  ++  )  { 
int   pos  =  lc  .  indexOf  (  wordList  [  i  ]  )  ; 
if  (  pos  >  -  1  )  { 
int   beginIdx  =  pos  -  numChars  ; 
if  (  beginIdx  <  0  )  { 
beginIdx  =  0  ; 
} 
int   endIdx  =  pos  +  numChars  ; 
if  (  endIdx  >  input  .  length  (  )  -  1  )  { 
endIdx  =  input  .  length  (  )  -  1  ; 
} 
char  [  ]  chars  =  input  .  toCharArray  (  )  ; 
while  (  beginIdx  >  0  &&  chars  [  beginIdx  ]  !=  ' '  &&  chars  [  beginIdx  ]  !=  '\n'  &&  chars  [  beginIdx  ]  !=  '\r'  )  { 
beginIdx  --  ; 
} 
while  (  endIdx  <  input  .  length  (  )  &&  chars  [  endIdx  ]  !=  ' '  &&  chars  [  endIdx  ]  !=  '\n'  &&  chars  [  endIdx  ]  !=  '\r'  )  { 
endIdx  ++  ; 
} 
return   input  .  substring  (  beginIdx  ,  endIdx  )  ; 
} 
} 
return   input  .  substring  (  0  ,  (  input  .  length  (  )  >=  200  )  ?  200  :  input  .  length  (  )  )  ; 
} 














public   static   String   wordWrap  (  String   input  ,  int   width  ,  Locale   locale  )  { 
if  (  input  ==  null  )  { 
return  ""  ; 
}  else   if  (  width  <  5  )  { 
return   input  ; 
}  else   if  (  width  >=  input  .  length  (  )  )  { 
return   input  ; 
} 
if  (  locale  ==  null  )  { 
} 
StringBuffer   buf  =  new   StringBuffer  (  input  )  ; 
boolean   endOfLine  =  false  ; 
int   lineStart  =  0  ; 
for  (  int   i  =  0  ;  i  <  buf  .  length  (  )  ;  i  ++  )  { 
if  (  buf  .  charAt  (  i  )  ==  '\n'  )  { 
lineStart  =  i  +  1  ; 
endOfLine  =  true  ; 
} 
if  (  i  >  lineStart  +  width  -  1  )  { 
if  (  !  endOfLine  )  { 
int   limit  =  i  -  lineStart  -  1  ; 
BreakIterator   breaks  =  BreakIterator  .  getLineInstance  (  locale  )  ; 
breaks  .  setText  (  buf  .  substring  (  lineStart  ,  i  )  )  ; 
int   end  =  breaks  .  last  (  )  ; 
if  (  end  ==  limit  +  1  )  { 
if  (  !  Character  .  isWhitespace  (  buf  .  charAt  (  lineStart  +  end  )  )  )  { 
end  =  breaks  .  preceding  (  end  -  1  )  ; 
} 
} 
if  (  end  !=  BreakIterator  .  DONE  &&  end  ==  limit  +  1  )  { 
buf  .  replace  (  lineStart  +  end  ,  lineStart  +  end  +  1  ,  "\n"  )  ; 
lineStart  =  lineStart  +  end  ; 
}  else   if  (  end  !=  BreakIterator  .  DONE  &&  end  !=  0  )  { 
buf  .  insert  (  lineStart  +  end  ,  '\n'  )  ; 
lineStart  =  lineStart  +  end  +  1  ; 
}  else  { 
buf  .  insert  (  i  ,  '\n'  )  ; 
lineStart  =  i  +  1  ; 
} 
}  else  { 
buf  .  insert  (  i  ,  '\n'  )  ; 
lineStart  =  i  +  1  ; 
endOfLine  =  false  ; 
} 
} 
} 
return   buf  .  toString  (  )  ; 
} 








public   static   final   String   escapeForXML  (  String   string  )  { 
if  (  string  ==  null  )  { 
return   null  ; 
} 
char   ch  ; 
int   i  =  0  ; 
int   last  =  0  ; 
char  [  ]  input  =  string  .  toCharArray  (  )  ; 
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
}  else   if  (  ch  ==  '&'  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
out  .  append  (  AMP_ENCODE  )  ; 
}  else   if  (  ch  ==  '"'  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
out  .  append  (  QUOTE_ENCODE  )  ; 
}  else   if  (  ch  ==  10  ||  ch  ==  13  ||  ch  ==  9  )  { 
continue  ; 
}  else   if  (  ch  <  32  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
} 
} 
if  (  last  ==  0  )  { 
return   string  ; 
} 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
return   out  .  toString  (  )  ; 
} 








public   static   final   String   unescapeFromXML  (  String   string  )  { 
string  =  replace  (  string  ,  "&lt;"  ,  "<"  )  ; 
string  =  replace  (  string  ,  "&gt;"  ,  ">"  )  ; 
string  =  replace  (  string  ,  "&quot;"  ,  "\""  )  ; 
return   replace  (  string  ,  "&amp;"  ,  "&"  )  ; 
} 

private   static   final   char  [  ]  zeroArray  =  "0000000000000000000000000000000000000000000000000000000000000000"  .  toCharArray  (  )  ; 














public   static   final   String   zeroPadString  (  String   string  ,  int   length  )  { 
if  (  string  ==  null  ||  string  .  length  (  )  >  length  )  { 
return   string  ; 
} 
StringBuffer   buf  =  new   StringBuffer  (  length  )  ; 
buf  .  append  (  zeroArray  ,  0  ,  length  -  string  .  length  (  )  )  .  append  (  string  )  ; 
return   buf  .  toString  (  )  ; 
} 







public   static   final   String   dateToMillis  (  Date   date  )  { 
return   Long  .  toString  (  date  .  getTime  (  )  )  ; 
} 




public   static   String   Strings2String  (  String  [  ]  strings  )  { 
String   tempString  =  ""  ; 
for  (  int   i  =  0  ;  i  <  strings  .  length  ;  i  ++  )  tempString  =  tempString  +  strings  [  i  ]  +  ","  ; 
tempString  =  tempString  .  substring  (  0  ,  tempString  .  length  (  )  -  1  )  ; 
return   tempString  ; 
} 

public   static   Long   dateString2Long  (  String   date  )  { 
SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyy-MM-dd"  )  ; 
Date   thisDate  =  null  ; 
try  { 
thisDate  =  format  .  parse  (  date  )  ; 
}  catch  (  ParseException   e  )  { 
return   null  ; 
} 
return   new   Long  (  thisDate  .  getTime  (  )  )  ; 
} 

public   static   Long   dateTimeString2Long  (  String   date  )  { 
SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  ; 
Date   thisDate  =  null  ; 
try  { 
thisDate  =  format  .  parse  (  date  )  ; 
}  catch  (  ParseException   e  )  { 
return   null  ; 
} 
return   new   Long  (  thisDate  .  getTime  (  )  )  ; 
} 

public   static   String   long2DateString  (  Long   time  )  { 
SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyy-MM-dd"  )  ; 
if  (  time  !=  null  )  { 
if  (  time  .  longValue  (  )  !=  0  )  { 
return   format  .  format  (  new   Date  (  time  .  longValue  (  )  )  )  ; 
} 
} 
return   null  ; 
} 

public   static   String   long2TimeString  (  Long   time  )  { 
SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  ; 
if  (  time  !=  null  )  { 
if  (  time  .  longValue  (  )  !=  0  )  { 
return   format  .  format  (  new   Date  (  time  .  longValue  (  )  )  )  ; 
} 
} 
return   null  ; 
} 

public   static   Integer   getDayOfMonth  (  )  { 
SimpleDateFormat   sf  =  new   SimpleDateFormat  (  "dd"  )  ; 
return   Integer  .  valueOf  (  sf  .  format  (  new   Date  (  )  )  )  ; 
} 

public   static   Date   string2Date  (  String   s  )  { 
try  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "yyyy-MM-dd"  )  ; 
return   formatter  .  parse  (  s  )  ; 
}  catch  (  ParseException   e  )  { 
return   null  ; 
} 
} 

public   static   String   filterStr  (  String   str  ,  String   filterStr  )  { 
if  (  str  ==  null  )  return   null  ; 
String   newStr  =  ""  ; 
char  [  ]  filterChars  =  filterStr  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
char   thisChar  =  str  .  charAt  (  i  )  ; 
if  (  filterStr  .  indexOf  (  thisChar  )  <  0  )  { 
newStr  =  newStr  +  thisChar  ; 
} 
} 
return   newStr  ; 
} 

public   static   String   filterStr  (  String   str  )  { 
return   filterStr  (  str  ,  getFilterChars  (  )  )  ; 
} 

public   static   String   getFilterChars  (  )  { 
return  "'%+/\\"  +  '"'  ; 
} 
} 

