package   com  .  laoer  .  bbscs  .  comm  ; 

import   java  .  util  .  regex  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  security  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  util  .  *  ; 

public   class   Util  { 

private   static   final   char  [  ]  QUOTE_ENCODE  =  "&quot;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  AMP_ENCODE  =  "&amp;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  LT_ENCODE  =  "&lt;"  .  toCharArray  (  )  ; 

private   static   final   char  [  ]  GT_ENCODE  =  "&gt;"  .  toCharArray  (  )  ; 

private   static   MessageDigest   digest  =  null  ; 

public   Util  (  )  { 
} 

public   static   boolean   validateUserName  (  String   username  )  { 
Pattern   p  =  Pattern  .  compile  (  "^\\w+$"  )  ; 
Matcher   m  =  p  .  matcher  (  username  )  ; 
if  (  m  .  find  (  )  )  { 
return   true  ; 
} 
return   false  ; 
} 

public   static   String   Array2String  (  String  [  ]  values  )  { 
String   result  =  ""  ; 
if  (  values  ==  null  )  { 
return   result  ; 
} 
int   len  =  values  .  length  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
result  +=  values  [  i  ]  +  ","  ; 
} 
if  (  result  .  endsWith  (  ","  )  )  { 
result  =  result  .  substring  (  result  .  length  (  )  -  1  )  ; 
} 
return   result  ; 
} 

public   static   String   Array2String  (  Object  [  ]  values  )  { 
String   result  =  ""  ; 
if  (  values  ==  null  )  { 
return   result  ; 
} 
int   len  =  values  .  length  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
result  +=  values  [  i  ]  .  toString  (  )  +  ","  ; 
} 
if  (  result  .  endsWith  (  ","  )  )  { 
result  =  result  .  substring  (  result  .  length  (  )  -  1  )  ; 
} 
return   result  ; 
} 

public   static   String   Array2String  (  List   values  )  { 
String   result  =  ""  ; 
if  (  values  ==  null  )  { 
return   result  ; 
} 
int   len  =  values  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
result  +=  values  .  get  (  i  )  .  toString  (  )  +  ","  ; 
} 
if  (  result  .  endsWith  (  ","  )  )  { 
result  =  result  .  substring  (  result  .  length  (  )  -  1  )  ; 
} 
return   result  ; 
} 

public   static   String   base64Encode  (  String   txt  )  { 
if  (  txt  !=  null  &&  txt  .  length  (  )  >  0  )  { 
txt  =  new   sun  .  misc  .  BASE64Encoder  (  )  .  encode  (  txt  .  getBytes  (  )  )  ; 
} 
return   txt  ; 
} 

public   static   String   base64Encode  (  byte  [  ]  txt  )  { 
String   encodeTxt  =  ""  ; 
if  (  txt  !=  null  &&  txt  .  length  >  0  )  { 
encodeTxt  =  new   sun  .  misc  .  BASE64Encoder  (  )  .  encode  (  txt  )  ; 
} 
return   encodeTxt  ; 
} 

public   static   String   base64decode  (  String   txt  )  { 
if  (  txt  !=  null  &&  txt  .  length  (  )  >  0  )  { 
byte  [  ]  buf  ; 
try  { 
buf  =  new   sun  .  misc  .  BASE64Decoder  (  )  .  decodeBuffer  (  txt  )  ; 
txt  =  new   String  (  buf  )  ; 
}  catch  (  IOException   ex  )  { 
} 
} 
return   txt  ; 
} 

public   static   byte  [  ]  base64decodebyte  (  String   txt  )  { 
byte  [  ]  buf  =  null  ; 
if  (  txt  !=  null  &&  txt  .  length  (  )  >  0  )  { 
try  { 
buf  =  new   sun  .  misc  .  BASE64Decoder  (  )  .  decodeBuffer  (  txt  )  ; 
}  catch  (  IOException   ex  )  { 
} 
} 
return   buf  ; 
} 













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

























public   static   final   synchronized   String   hash  (  String   data  )  { 
if  (  digest  ==  null  )  { 
try  { 
digest  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
System  .  err  .  println  (  "Failed to load the MD5 MessageDigest. "  +  "We will be unable to function normally."  )  ; 
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
return   buf  .  toString  (  )  .  toUpperCase  (  )  ; 
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













public   static   final   String  [  ]  toLowerCaseWordArray  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  length  (  )  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
ArrayList  <  String  >  wordList  =  new   ArrayList  <  String  >  (  )  ; 
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

public   static   final   String   escapeForSpecial  (  String   string  )  { 
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
}  else   if  (  ch  ==  '>'  )  { 
if  (  i  >  last  )  { 
out  .  append  (  input  ,  last  ,  i  -  last  )  ; 
} 
last  =  i  +  1  ; 
out  .  append  (  GT_ENCODE  )  ; 
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

private   static   final   char  [  ]  zeroArray  =  "0000000000000000"  .  toCharArray  (  )  ; 















public   static   final   String   zeroPadString  (  String   string  ,  int   length  )  { 
if  (  string  ==  null  ||  string  .  length  (  )  >  length  )  { 
return   string  ; 
} 
StringBuffer   buf  =  new   StringBuffer  (  length  )  ; 
buf  .  append  (  zeroArray  ,  0  ,  length  -  string  .  length  (  )  )  .  append  (  string  )  ; 
return   buf  .  toString  (  )  ; 
} 







public   static   final   String   dateToMillis  (  Date   date  )  { 
return   zeroPadString  (  Long  .  toString  (  date  .  getTime  (  )  )  ,  15  )  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   static   final   String   collectionToString  (  Collection   c  ,  String   spilt  )  { 
if  (  c  ==  null  )  { 
return   null  ; 
} 
if  (  spilt  ==  null  )  { 
return   null  ; 
} 
String   ret  =  ""  ; 
ArrayList   a  =  new   ArrayList  (  c  )  ; 
try  { 
for  (  int   i  =  0  ;  i  <  a  .  size  (  )  ;  i  ++  )  { 
String   t  =  (  String  )  a  .  get  (  i  )  ; 
if  (  i  ==  a  .  size  (  )  -  1  )  { 
ret  =  ret  +  t  ; 
}  else  { 
ret  =  ret  +  t  +  spilt  ; 
} 
} 
return   ret  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 

public   static   String   genPassword  (  int   length  )  { 
if  (  length  <  1  )  { 
return   null  ; 
} 
String  [  ]  strChars  =  {  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  ,  "a"  ,  "b"  ,  "c"  ,  "d"  ,  "e"  ,  "f"  ,  "g"  ,  "h"  ,  "i"  ,  "j"  ,  "k"  ,  "m"  ,  "n"  ,  "p"  ,  "q"  ,  "r"  ,  "s"  ,  "t"  ,  "u"  ,  "v"  ,  "w"  ,  "x"  ,  "y"  ,  "z"  ,  "a"  }  ; 
StringBuffer   strPassword  =  new   StringBuffer  (  )  ; 
int   nRand  =  (  int  )  java  .  lang  .  Math  .  round  (  java  .  lang  .  Math  .  random  (  )  *  100  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
nRand  =  (  int  )  java  .  lang  .  Math  .  round  (  java  .  lang  .  Math  .  random  (  )  *  100  )  ; 
strPassword  .  append  (  strChars  [  nRand  %  (  strChars  .  length  -  1  )  ]  )  ; 
} 
return   strPassword  .  toString  (  )  ; 
} 

public   static   String   genNumPassword  (  int   length  )  { 
if  (  length  <  1  )  { 
return   null  ; 
} 
String  [  ]  strChars  =  {  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  }  ; 
StringBuffer   strPassword  =  new   StringBuffer  (  )  ; 
int   nRand  =  (  int  )  java  .  lang  .  Math  .  round  (  java  .  lang  .  Math  .  random  (  )  *  100  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
nRand  =  (  int  )  java  .  lang  .  Math  .  round  (  java  .  lang  .  Math  .  random  (  )  *  100  )  ; 
strPassword  .  append  (  strChars  [  nRand  %  (  strChars  .  length  -  1  )  ]  )  ; 
} 
return   strPassword  .  toString  (  )  ; 
} 

public   static   String   genEmptyString  (  int   length  )  { 
if  (  length  <  1  )  { 
return   null  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
sb  .  append  (  " "  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   String   getAsciiString  (  int   digit  )  { 
byte   ret  [  ]  =  new   byte  [  1  ]  ; 
ret  [  0  ]  =  (  byte  )  digit  ; 
return   new   String  (  ret  )  ; 
} 

public   static   int   getAsciiNum  (  String   s  )  { 
if  (  s  .  length  (  )  <  1  )  { 
return   0  ; 
} 
byte   b  =  s  .  getBytes  (  )  [  0  ]  ; 
return   b  ; 
} 

public   static   String   getCurrTime  (  )  { 
Date   now  =  new   Date  (  )  ; 
SimpleDateFormat   outFormat  =  new   SimpleDateFormat  (  "yyyyMMddHHmmss"  )  ; 
String   s  =  outFormat  .  format  (  now  )  ; 
return   s  ; 
} 

public   static   String   formatDate  (  Date   date  ,  String   format  )  { 
SimpleDateFormat   outFormat  =  new   SimpleDateFormat  (  format  )  ; 
return   outFormat  .  format  (  date  )  ; 
} 




public   static   String   formatDate  (  Date   date  )  { 
SimpleDateFormat   outFormat  =  new   SimpleDateFormat  (  "yyyy-MM-dd"  )  ; 
return   outFormat  .  format  (  date  )  ; 
} 




public   static   String   formatDateTime  (  Date   date  )  { 
SimpleDateFormat   outFormat  =  new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  ; 
return   outFormat  .  format  (  date  )  ; 
} 

public   static   String   formatDate2  (  Date   myDate  )  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "yyyy/MM/dd"  )  ; 
String   strDate  =  formatter  .  format  (  myDate  )  ; 
return   strDate  ; 
} 

public   static   String   formatDate3  (  Date   myDate  )  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "MM-dd HH:mm"  )  ; 
String   strDate  =  formatter  .  format  (  myDate  )  ; 
return   strDate  ; 
} 

public   static   String   formatDate4  (  Date   myDate  )  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "yyyyMMdd"  )  ; 
String   strDate  =  formatter  .  format  (  myDate  )  ; 
return   strDate  ; 
} 

public   static   String   formatDate5  (  Date   myDate  )  { 
String   strDate  =  getYear  (  myDate  )  +  "-"  +  getMonth  (  myDate  )  +  "-"  +  getDay  (  myDate  )  ; 
return   strDate  ; 
} 

public   static   String   formatDate6  (  Date   myDate  )  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm"  )  ; 
String   strDate  =  formatter  .  format  (  myDate  )  ; 
return   strDate  ; 
} 

public   static   long   Date2Long  (  int   year  ,  int   month  ,  int   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
month  =  month  -  1  ; 
cld  .  set  (  year  ,  month  ,  date  )  ; 
return   cld  .  getTime  (  )  .  getTime  (  )  ; 
} 

public   static   long   Time2Long  (  int   year  ,  int   month  ,  int   date  ,  int   hour  ,  int   minute  ,  int   second  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
month  =  month  -  1  ; 
cld  .  set  (  year  ,  month  ,  date  ,  hour  ,  minute  ,  second  )  ; 
return   cld  .  getTime  (  )  .  getTime  (  )  ; 
} 

public   static   int   getYear  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  YEAR  )  ; 
} 

public   static   int   getMonth  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  MONTH  )  +  1  ; 
} 

public   static   int   getDay  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  DAY_OF_MONTH  )  ; 
} 

public   static   int   getHour  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  HOUR_OF_DAY  )  ; 
} 

public   static   int   getMinute  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  MINUTE  )  ; 
} 

public   static   int   getSecond  (  long   t  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
if  (  t  >  0  )  { 
cld  .  setTime  (  new   java  .  util  .  Date  (  t  )  )  ; 
} 
return   cld  .  get  (  Calendar  .  SECOND  )  ; 
} 

public   static   int   getYear  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  YEAR  )  ; 
} 

public   static   int   getMonth  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  MONTH  )  +  1  ; 
} 

public   static   int   getDay  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  DAY_OF_MONTH  )  ; 
} 

public   static   int   getHour  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  HOUR_OF_DAY  )  ; 
} 

public   static   int   getMinute  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  MINUTE  )  ; 
} 

public   static   int   getSecond  (  Date   date  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  date  )  ; 
return   cld  .  get  (  Calendar  .  SECOND  )  ; 
} 

public   static   int   getYear  (  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  new   java  .  util  .  Date  (  )  )  ; 
return   cld  .  get  (  Calendar  .  YEAR  )  ; 
} 

public   static   int   getMonth  (  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  new   java  .  util  .  Date  (  )  )  ; 
return   cld  .  get  (  Calendar  .  MONTH  )  +  1  ; 
} 

public   static   int   getDay  (  )  { 
Calendar   cld  =  Calendar  .  getInstance  (  )  ; 
cld  .  setTime  (  new   java  .  util  .  Date  (  )  )  ; 
return   cld  .  get  (  Calendar  .  DAY_OF_MONTH  )  ; 
} 

public   static   String   replaceComma  (  String   text  )  { 
if  (  text  !=  null  )  { 
text  =  text  .  replaceAll  (  "，"  ,  ","  )  ; 
} 
return   text  ; 
} 

public   static   String   replaceBr  (  String   text  )  { 
if  (  text  !=  null  )  { 
text  =  text  .  replaceAll  (  "\n"  ,  "<BR>"  )  ; 
} 
return   text  ; 
} 

public   static   long   getLongTime  (  )  { 
return   System  .  currentTimeMillis  (  )  ; 
} 








public   static   boolean   nullOrBlank  (  String   param  )  { 
return  (  param  ==  null  ||  param  .  length  (  )  ==  0  ||  param  .  trim  (  )  .  equals  (  ""  )  )  ?  true  :  false  ; 
} 

public   static   String   notNull  (  String   param  )  { 
return   param  ==  null  ?  ""  :  param  .  trim  (  )  ; 
} 









public   static   boolean   parseBoolean  (  String   param  )  { 
if  (  nullOrBlank  (  param  )  )  { 
return   false  ; 
} 
switch  (  param  .  charAt  (  0  )  )  { 
case  '1'  : 
case  'y'  : 
case  'Y'  : 
case  't'  : 
case  'T'  : 
return   true  ; 
} 
return   false  ; 
} 
} 

