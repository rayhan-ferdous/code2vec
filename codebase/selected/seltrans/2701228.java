package   com  .  ec  .  core  .  util  ; 

import   java  .  security  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  lang  .  Math  ; 
import   javax  .  crypto  .  spec  .  SecretKeySpec  ; 
import   javax  .  crypto  .  SecretKey  ; 




public   class   StringUtils  { 





private   static   Object   initLock  =  new   Object  (  )  ; 

public   static   String   format  =  "yyyy-MM-dd HH:mm:ss"  ; 

private   static   SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "EEE yyyy-MM-dd G HH:mm:ss z"  )  ; 

private   static   String  [  ]  HanDigiStr  =  new   String  [  ]  {  "��"  ,  "Ҽ"  ,  "��"  ,  "��"  ,  "��"  ,  "��"  ,  "½"  ,  "��"  ,  "��"  ,  "��"  }  ; 

private   static   String  [  ]  HanDiviStr  =  new   String  [  ]  {  ""  ,  "ʰ"  ,  "��"  ,  "Ǫ"  ,  "��"  ,  "ʰ"  ,  "��"  ,  "Ǫ"  ,  "��"  ,  "ʰ"  ,  "��"  ,  "Ǫ"  ,  "��"  ,  "ʰ"  ,  "��"  ,  "Ǫ"  ,  "��"  ,  "ʰ"  ,  "��"  ,  "Ǫ"  ,  "��"  ,  "ʰ"  ,  "��"  ,  "Ǫ"  }  ; 




public   static   Date   parseDate  (  String   date  ,  String   format  )  { 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  format  ,  java  .  util  .  Locale  .  US  )  ; 
ParsePosition   pos  =  new   ParsePosition  (  0  )  ; 
Date   ret  =  formatter  .  parse  (  date  ,  pos  )  ; 
return   ret  ; 
} 

public   static   String   formatDate  (  Date   d  )  { 
return   sdf  .  format  (  d  )  ; 
} 

public   static   String   formatDate  (  java  .  util  .  Date   newDate  ,  String   format  )  { 
if  (  newDate  ==  null  )  return  ""  ; 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  format  )  ; 
return   sdf  .  format  (  newDate  )  ; 
} 










public   static   final   String   replace  (  String   line  ,  String   oldString  ,  String   newString  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
oldString  =  filterAfterDB  (  oldString  )  ; 
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
oldString  =  filterAfterDB  (  oldString  )  ; 
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











public   static   final   String   replace  (  String   line  ,  String   oldString  ,  String   newString  ,  int  [  ]  count  )  { 
if  (  line  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
oldString  =  filterAfterDB  (  oldString  )  ; 
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










public   static   final   String   escapeHTMLTags  (  String   input  )  { 
if  (  input  ==  null  ||  input  .  length  (  )  ==  0  )  { 
return   input  ; 
} 
StringBuffer   buf  =  new   StringBuffer  (  input  .  length  (  )  )  ; 
char   ch  =  ' '  ; 
for  (  int   i  =  0  ;  i  <  input  .  length  (  )  ;  i  ++  )  { 
ch  =  input  .  charAt  (  i  )  ; 
if  (  ch  ==  '<'  )  { 
buf  .  append  (  "&lt;"  )  ; 
}  else   if  (  ch  ==  '>'  )  { 
buf  .  append  (  "&gt;"  )  ; 
}  else   if  (  ch  ==  '&'  )  { 
buf  .  append  (  "&amp;"  )  ; 
}  else   if  (  ch  ==  '"'  )  { 
buf  .  append  (  "&quot;"  )  ; 
}  else  { 
buf  .  append  (  ch  )  ; 
} 
} 
return   buf  .  toString  (  )  ; 
} 




private   static   MessageDigest   digest  =  null  ; 

























public   static   final   String   hash  (  String   data  )  { 
if  (  digest  ==  null  )  { 
synchronized  (  StringUtils  .  class  )  { 
if  (  digest  ==  null  )  { 
try  { 
digest  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
} 
} 
} 
} 
digest  .  update  (  data  .  getBytes  (  )  )  ; 
return   toHex  (  digest  .  digest  (  )  )  ; 
} 












public   static   final   String   toHex  (  byte   hash  [  ]  )  { 
StringBuffer   buf  =  new   StringBuffer  (  hash  .  length  *  2  )  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  hash  .  length  ;  i  ++  )  { 
if  (  (  (  int  )  hash  [  i  ]  &  0xff  )  <  0x10  )  { 
buf  .  append  (  "0"  )  ; 
} 
buf  .  append  (  Long  .  toString  (  (  int  )  hash  [  i  ]  &  0xff  ,  16  )  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 











public   static   final   String  [  ]  toLowerCaseWordArray  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  length  (  )  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
StringTokenizer   tokens  =  new   StringTokenizer  (  text  ,  " ,\r\n.:/\\+"  )  ; 
String  [  ]  words  =  new   String  [  tokens  .  countTokens  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  words  .  length  ;  i  ++  )  { 
words  [  i  ]  =  tokens  .  nextToken  (  )  .  toLowerCase  (  )  ; 
} 
return   words  ; 
} 











public   static   final   String  [  ]  toStringArray  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  length  (  )  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
StringTokenizer   tokens  =  new   StringTokenizer  (  text  ,  ",\r\n/\\"  )  ; 
String  [  ]  words  =  new   String  [  tokens  .  countTokens  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  words  .  length  ;  i  ++  )  { 
words  [  i  ]  =  tokens  .  nextToken  (  )  ; 
} 
return   words  ; 
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


















public   static   final   String   randomString  (  int   length  )  { 
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
return   string  .  substring  (  0  ,  i  )  ; 
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












public   static   final   String   highlightWords  (  String   string  ,  String  [  ]  words  ,  String   startHighlight  ,  String   endHighlight  )  { 
if  (  string  ==  null  ||  words  ==  null  ||  startHighlight  ==  null  ||  endHighlight  ==  null  )  { 
return   null  ; 
} 
for  (  int   x  =  0  ;  x  <  words  .  length  ;  x  ++  )  { 
String   lcString  =  string  .  toLowerCase  (  )  ; 
char  [  ]  string2  =  string  .  toCharArray  (  )  ; 
String   word  =  words  [  x  ]  .  toLowerCase  (  )  ; 
int   i  =  0  ; 
if  (  (  i  =  lcString  .  indexOf  (  word  ,  i  )  )  >=  0  )  { 
int   oLength  =  word  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  string2  .  length  )  ; 
boolean   startSpace  =  false  ; 
char   startChar  =  ' '  ; 
if  (  i  -  1  >  0  )  { 
startChar  =  string2  [  i  -  1  ]  ; 
if  (  !  Character  .  isLetter  (  startChar  )  )  { 
startSpace  =  true  ; 
} 
} 
boolean   endSpace  =  false  ; 
char   endChar  =  ' '  ; 
if  (  i  +  oLength  <  string2  .  length  )  { 
endChar  =  string2  [  i  +  oLength  ]  ; 
if  (  !  Character  .  isLetter  (  endChar  )  )  { 
endSpace  =  true  ; 
} 
} 
if  (  (  startSpace  &&  endSpace  )  ||  (  i  ==  0  &&  endSpace  )  )  { 
buf  .  append  (  string2  ,  0  ,  i  )  ; 
if  (  startSpace  &&  startChar  ==  ' '  )  { 
buf  .  append  (  startChar  )  ; 
} 
buf  .  append  (  startHighlight  )  ; 
buf  .  append  (  string2  ,  i  ,  oLength  )  .  append  (  endHighlight  )  ; 
if  (  endSpace  &&  endChar  ==  ' '  )  { 
buf  .  append  (  endChar  )  ; 
} 
}  else  { 
buf  .  append  (  string2  ,  0  ,  i  )  ; 
buf  .  append  (  string2  ,  i  ,  oLength  )  ; 
} 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  lcString  .  indexOf  (  word  ,  i  )  )  >  0  )  { 
startSpace  =  false  ; 
startChar  =  string2  [  i  -  1  ]  ; 
if  (  !  Character  .  isLetter  (  startChar  )  )  { 
startSpace  =  true  ; 
} 
endSpace  =  false  ; 
if  (  i  +  oLength  <  string2  .  length  )  { 
endChar  =  string2  [  i  +  oLength  ]  ; 
if  (  !  Character  .  isLetter  (  endChar  )  )  { 
endSpace  =  true  ; 
} 
} 
if  (  (  startSpace  &&  endSpace  )  ||  i  +  oLength  ==  string2  .  length  )  { 
buf  .  append  (  string2  ,  j  ,  i  -  j  )  ; 
if  (  startSpace  &&  startChar  ==  ' '  )  { 
buf  .  append  (  startChar  )  ; 
} 
buf  .  append  (  startHighlight  )  ; 
buf  .  append  (  string2  ,  i  ,  oLength  )  .  append  (  endHighlight  )  ; 
if  (  endSpace  &&  endChar  ==  ' '  )  { 
buf  .  append  (  endChar  )  ; 
} 
}  else  { 
buf  .  append  (  string2  ,  j  ,  i  -  j  )  ; 
buf  .  append  (  string2  ,  i  ,  oLength  )  ; 
} 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  string2  ,  j  ,  string2  .  length  -  j  )  ; 
string  =  buf  .  toString  (  )  ; 
} 
} 
return   string  ; 
} 








public   static   final   String   escapeForXML  (  String   string  )  { 
if  (  string  ==  null  ||  string  .  length  (  )  ==  0  )  { 
return   string  ; 
} 
char  [  ]  sArray  =  string  .  toCharArray  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  sArray  .  length  )  ; 
char   ch  ; 
for  (  int   i  =  0  ;  i  <  sArray  .  length  ;  i  ++  )  { 
ch  =  sArray  [  i  ]  ; 
if  (  ch  ==  '<'  )  { 
buf  .  append  (  "&lt;"  )  ; 
}  else   if  (  ch  ==  '&'  )  { 
buf  .  append  (  "&amp;"  )  ; 
}  else   if  (  ch  ==  '"'  )  { 
buf  .  append  (  "&quot;"  )  ; 
}  else  { 
buf  .  append  (  ch  )  ; 
} 
} 
return   buf  .  toString  (  )  ; 
} 

public   static   String   filterAndQutation4DB  (  String   source  )  { 
String   result  =  source  ; 
result  =  filterQutation4DBString  (  result  )  ; 
result  =  filterBeforeDB  (  result  )  ; 
return   result  ; 
} 




public   static   String   filterBeforeDB  (  String   source  )  { 
if  (  source  ==  null  )  return   null  ; 
String   tar  =  null  ; 
try  { 
tar  =  new   String  (  source  .  getBytes  (  "GBK"  )  ,  "ISO8859_1"  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   tar  ; 
} 




public   static   String   filterAfterDB  (  String   str  )  { 
try  { 
if  (  str  ==  null  )  return   null  ; 
return   new   String  (  str  .  getBytes  (  "ISO8859_1"  )  ,  "GBK"  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   str  ; 
} 









public   static   String   upperEnglish  (  String   source  )  { 
if  (  source  ==  null  )  return  ""  ; 
source  =  source  .  trim  (  )  ; 
byte  [  ]  chars  =  source  .  getBytes  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  chars  .  length  )  ; 
byte  [  ]  chinese  =  new   byte  [  2  ]  ; 
int   count  =  0  ; 
int   line  =  0  ; 
int   i  =  0  ; 
for  (  ;  i  <  chars  .  length  ;  i  ++  )  { 
if  (  chars  [  i  ]  <  0  )  { 
chinese  [  0  ]  =  chars  [  i  ]  ; 
chinese  [  1  ]  =  chars  [  i  +  1  ]  ; 
sb  .  append  (  new   String  (  chinese  )  )  ; 
i  ++  ; 
}  else  { 
String   tmpstr  =  (  char  )  chars  [  i  ]  +  ""  ; 
tmpstr  =  tmpstr  .  toUpperCase  (  )  ; 
sb  .  append  (  tmpstr  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   boolean   equalsIgnoreCase  (  String   a  ,  String   b  )  { 
return   upperEnglish  (  a  .  trim  (  )  )  .  equals  (  upperEnglish  (  b  .  trim  (  )  )  )  ; 
} 




public   static   String   filterString  (  String   source  ,  String   src  ,  String   dest  )  { 
if  (  source  ==  null  )  return  ""  ; 
try  { 
int   i  =  0  ; 
int   s_len  =  src  .  length  (  )  ; 
int   d_len  =  dest  .  length  (  )  ; 
String   u_src  =  upperEnglish  (  src  )  ; 
String   u_source  =  upperEnglish  (  source  )  ; 
int   pos  =  stringIndexOf  (  u_source  ,  u_src  ,  i  )  ; 
while  (  pos  !=  -  1  )  { 
String   pre  =  source  .  substring  (  0  ,  pos  )  ; 
String   last  =  source  .  substring  (  pos  +  s_len  )  ; 
source  =  pre  +  dest  +  last  ; 
u_source  =  pre  +  dest  +  last  ; 
u_source  =  u_source  .  toUpperCase  (  )  ; 
i  =  pos  +  d_len  ; 
pos  =  stringIndexOf  (  u_source  ,  u_src  ,  i  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
return   source  ; 
} 




public   static   String   NullToSpace  (  String   src  )  { 
if  (  src  ==  null  )  return  ""  ;  else   return   src  ; 
} 

public   static   String   HighLight  (  String   source  ,  String   target  ,  String   background  ,  String   foreground  )  { 
source  =  filterString  (  source  ,  target  ,  "<font style='background:"  +  background  +  ";color:"  +  foreground  +  "'>"  +  target  +  "</font>"  )  ; 
return   source  ; 
} 




public   static   String   filterQutationString  (  String   source  )  { 
source  =  filterString  (  source  ,  "\'"  ,  "\\\'"  )  ; 
source  =  filterString  (  source  ,  "\""  ,  "\\\""  )  ; 
return   source  ; 
} 

public   static   String   filterQutation4DBString  (  String   source  )  { 
if  (  source  ==  null  ||  source  .  trim  (  )  .  length  (  )  ==  0  )  return  ""  ; 
source  =  filterString  (  source  ,  "\'"  ,  "\'\'"  )  ; 
return   source  ; 
} 




public   static   int   stringIndexOf  (  String   str  ,  String   in  ,  int   l  )  { 
int   pos  =  -  1  ; 
byte  [  ]  strbytes  =  str  .  getBytes  (  )  ; 
byte  [  ]  inbytes  =  in  .  getBytes  (  )  ; 
byte   temp  ; 
boolean   cn  =  false  ; 
int   strf  =  (  l  <  0  )  ?  0  :  l  ; 
int   strs  =  strf  ; 
int   inf  =  0  ,  cni  =  0  ; 
while  (  strs  <=  (  strbytes  .  length  -  inbytes  .  length  )  )  { 
if  (  cn  ==  false  )  { 
cni  =  0  ; 
cn  =  ChineseChr  (  strbytes  [  strf  ]  )  ; 
} 
if  (  (  cn  )  &&  (  cni  ==  1  )  &&  (  inf  ==  0  )  )  { 
strf  ++  ; 
strs  =  strf  ; 
cn  =  false  ; 
cni  =  0  ; 
}  else  { 
if  (  strbytes  [  strf  ]  ==  inbytes  [  inf  ]  )  { 
if  (  inf  ==  0  &&  inf  !=  inbytes  .  length  -  1  )  strs  =  strf  ;  else   if  (  inf  ==  inbytes  .  length  -  1  )  { 
pos  =  strs  ; 
break  ; 
} 
strf  ++  ; 
inf  ++  ; 
if  (  strbytes  .  length  <=  strf  )  return   pos  ; 
if  (  inbytes  .  length  ==  inf  )  return   pos  ; 
}  else  { 
if  (  inf  ==  0  )  { 
strf  ++  ; 
strs  =  strf  ; 
}  else  { 
if  (  ChineseChr  (  strbytes  [  strs  ]  )  )  strf  =  strs  +  2  ;  else   strf  =  strs  +  1  ; 
inf  =  0  ; 
} 
if  (  strbytes  .  length  <=  strf  )  return   pos  ; 
} 
if  (  cn  )  cni  ++  ; 
} 
} 
return   pos  ; 
} 




public   static   int   stringIndexOf  (  String   str  ,  String   in  )  { 
return   stringIndexOf  (  str  ,  in  ,  0  )  ; 
} 










public   static   boolean   ChineseChr  (  byte   b  )  { 
if  (  b  >=  0  )  return   false  ;  else   return   true  ; 
} 








public   static   String   substringChinese  (  String   str  ,  int   b  ,  int   e  )  { 
if  (  str  ==  null  ||  str  .  equals  (  ""  )  )  return   str  ; 
if  (  e  <=  0  )  return   null  ; 
int   strlen  =  str  .  length  (  )  ; 
int   lastp  =  e  >  strlen  ?  strlen  :  e  ; 
int   firstp  =  b  >  0  ?  b  :  0  ; 
if  (  lastp  <=  firstp  )  return   null  ; 
byte   strbytes  [  ]  =  str  .  getBytes  (  )  ; 
byte   retbytes  [  ]  =  new   byte  [  lastp  -  firstp  ]  ; 
boolean   cn  =  false  ; 
int   i  =  firstp  ; 
for  (  i  =  firstp  ;  i  <  lastp  ;  i  ++  )  { 
if  (  cn  )  { 
retbytes  [  i  -  firstp  ]  =  strbytes  [  i  ]  ; 
cn  =  false  ; 
continue  ; 
} 
if  (  ChineseChr  (  strbytes  [  i  ]  )  )  { 
cn  =  true  ; 
if  (  i  >=  (  lastp  -  1  )  )  { 
break  ; 
} 
} 
retbytes  [  i  -  firstp  ]  =  strbytes  [  i  ]  ; 
} 
int   retlen  =  i  -  firstp  ; 
if  (  retlen  <=  0  )  return  ""  ; 
String   ret  =  new   String  (  retbytes  ,  0  ,  retlen  )  ; 
return   ret  ; 
} 

public   static   String   substringChinese  (  String   str  ,  int   l  )  { 
return  (  substringChinese  (  str  ,  0  ,  l  )  )  ; 
} 




public   static   String   breakString  (  String   str  ,  int   l  )  { 
if  (  str  ==  null  ||  str  .  length  (  )  ==  0  ||  l  <=  1  )  return   str  ; 
String   temp  =  new   String  (  str  )  ; 
String   ret  =  null  ,  oneline  =  null  ; 
while  (  temp  !=  null  &&  temp  .  length  (  )  >  l  )  { 
oneline  =  substringChinese  (  temp  ,  l  )  ; 
try  { 
temp  =  temp  .  substring  (  oneline  .  length  (  )  )  ; 
}  catch  (  Exception   e  )  { 
temp  =  null  ; 
} 
if  (  oneline  ==  null  )  break  ; 
if  (  ret  ==  null  )  ret  =  oneline  ;  else   ret  +=  "<br>"  +  oneline  ; 
} 
if  (  temp  !=  null  &&  temp  .  length  (  )  >  0  )  { 
if  (  ret  ==  null  )  ret  =  temp  ;  else   ret  +=  "<br>"  +  temp  ; 
} 
return   ret  ; 
} 











public   static   String   filterFixString  (  String   source  ,  int   length  ,  int   linelen  )  { 
StringBuffer   sb  =  new   StringBuffer  (  length  )  ; 
byte  [  ]  chars  =  source  .  getBytes  (  )  ; 
byte  [  ]  chinese  =  new   byte  [  2  ]  ; 
int   count  =  0  ; 
int   line  =  0  ; 
int   i  =  0  ; 
for  (  ;  i  <  chars  .  length  &&  i  <  length  ;  i  ++  )  { 
if  (  chars  [  i  ]  <  0  )  { 
chinese  [  0  ]  =  chars  [  i  ++  ]  ; 
chinese  [  1  ]  =  chars  [  i  ]  ; 
sb  .  append  (  new   String  (  chinese  )  )  ; 
}  else  { 
if  (  chars  [  i  ]  ==  13  )  sb  .  append  (  "<br>"  )  ; 
if  (  chars  [  i  ]  ==  32  )  sb  .  append  (  "&nbsp;"  )  ; 
if  (  chars  [  i  ]  ==  9  )  sb  .  append  (  "&nbsp;&nbsp;&nbsp;&nbsp;"  )  ; 
sb  .  append  (  (  char  )  chars  [  i  ]  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   String   filterBR  (  String   source  )  { 
StringBuffer   sb  =  new   StringBuffer  (  source  .  length  (  )  )  ; 
byte  [  ]  chars  =  source  .  getBytes  (  )  ; 
byte  [  ]  chinese  =  new   byte  [  2  ]  ; 
int   count  =  0  ; 
int   line  =  0  ; 
int   i  =  0  ; 
for  (  ;  i  <  chars  .  length  ;  i  ++  )  { 
if  (  chars  [  i  ]  <  0  )  { 
chinese  [  0  ]  =  chars  [  i  ++  ]  ; 
chinese  [  1  ]  =  chars  [  i  ]  ; 
sb  .  append  (  new   String  (  chinese  )  )  ; 
}  else  { 
if  (  (  chars  [  i  ]  ==  13  )  )  sb  .  append  (  "<br>"  )  ; 
if  (  chars  [  i  ]  ==  32  )  sb  .  append  (  "&nbsp;"  )  ; 
if  (  chars  [  i  ]  ==  9  )  sb  .  append  (  "&nbsp;&nbsp;"  )  ; 
sb  .  append  (  (  char  )  chars  [  i  ]  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 






public   static   String   loadFromFile  (  String   fname  )  throws   FileNotFoundException  ,  IOException  { 
FileInputStream   fis  =  null  ; 
String   ret  =  null  ; 
try  { 
fis  =  new   FileInputStream  (  fname  )  ; 
byte   mybuffer  [  ]  =  new   byte  [  4096  ]  ; 
int   totalnum  =  fis  .  read  (  mybuffer  )  ; 
while  (  totalnum  >  0  )  { 
String   buf  =  new   String  (  mybuffer  ,  0  ,  totalnum  )  ; 
if  (  ret  ==  null  )  ret  =  buf  ;  else   ret  +=  buf  ; 
totalnum  =  fis  .  read  (  mybuffer  )  ; 
} 
return   ret  ; 
}  finally  { 
try  { 
fis  .  close  (  )  ; 
}  catch  (  Exception   e1  )  { 
} 
} 
} 






public   static   void   saveToFile  (  String   fname  ,  String   str  )  throws   IOException  { 
if  (  str  ==  null  )  return  ; 
BufferedWriter   bw  =  null  ; 
try  { 
bw  =  new   BufferedWriter  (  new   FileWriter  (  fname  )  )  ; 
bw  .  write  (  str  )  ; 
}  finally  { 
try  { 
if  (  bw  !=  null  )  bw  .  close  (  )  ; 
}  catch  (  Exception   e1  )  { 
} 
} 
} 






public   static   String   getFileExt  (  String   fname  )  { 
if  (  fname  ==  null  ||  fname  .  length  (  )  ==  0  )  return   null  ; 
int   loc  =  fname  .  lastIndexOf  (  "."  )  ; 
try  { 
if  (  loc  >  0  )  { 
String   ret  =  fname  .  substring  (  loc  +  1  )  ; 
ret  =  ret  .  trim  (  )  ; 
if  (  ret  .  length  (  )  ==  0  )  return   null  ; 
return   ret  ; 
}  else  { 
return   null  ; 
} 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 







public   static   String   getFileNameWithoutExt  (  String   fname  )  { 
if  (  fname  ==  null  ||  fname  .  length  (  )  ==  0  )  return   fname  ; 
int   loc  =  fname  .  lastIndexOf  (  "/"  )  ; 
try  { 
String   name  =  null  ; 
if  (  loc  >  0  )  name  =  fname  .  substring  (  loc  +  1  )  ; 
if  (  name  ==  null  ||  name  .  length  (  )  ==  0  )  return   name  ; 
loc  =  name  .  lastIndexOf  (  "."  )  ; 
if  (  loc  >  0  )  { 
String   ret  =  name  .  substring  (  0  ,  loc  )  ; 
return   ret  ; 
}  else  { 
return   name  ; 
} 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 








public   static   String  [  ]  split  (  String   oldString  ,  String   delim  )  { 
if  (  oldString  ==  null  )  return   null  ; 
String  [  ]  newArray  =  null  ; 
StringTokenizer   st  =  new   StringTokenizer  (  oldString  ,  delim  )  ; 
newArray  =  new   String  [  st  .  countTokens  (  )  ]  ; 
int   count  =  0  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
newArray  [  count  ]  =  st  .  nextToken  (  )  .  trim  (  )  ; 
count  ++  ; 
} 
return   newArray  ; 
} 

public   static   ArrayList   split  (  String   s  )  { 
byte  [  ]  strbytes  =  s  .  getBytes  (  )  ; 
ArrayList   sar  =  new   ArrayList  (  )  ; 
String   c  =  ""  ; 
for  (  int   i  =  0  ;  i  <  strbytes  .  length  ;  i  ++  )  { 
if  (  StringUtils  .  ChineseChr  (  strbytes  [  i  ]  )  )  { 
byte  [  ]  b  =  {  strbytes  [  i  ]  ,  strbytes  [  ++  i  ]  }  ; 
c  =  new   String  (  b  )  ; 
sar  .  add  (  c  )  ; 
}  else  { 
byte  [  ]  b  =  {  strbytes  [  i  ]  }  ; 
c  =  new   String  (  b  )  ; 
sar  .  add  (  c  )  ; 
} 
} 
return   sar  ; 
} 








public   static   javax  .  crypto  .  SecretKey   genDESKey  (  byte  [  ]  key_byte  )  throws   Exception  { 
SecretKey   k  =  null  ; 
k  =  new   SecretKeySpec  (  key_byte  ,  "DESede"  )  ; 
return   k  ; 
} 








public   static   byte  [  ]  desEncrypt  (  byte  [  ]  src  )  throws   Exception  { 
javax  .  crypto  .  SecretKey   key  =  genDESKey  (  "123456781234567812345678"  .  getBytes  (  )  )  ; 
javax  .  crypto  .  Cipher   cipher  =  javax  .  crypto  .  Cipher  .  getInstance  (  "DESede"  )  ; 
cipher  .  init  (  javax  .  crypto  .  Cipher  .  ENCRYPT_MODE  ,  key  )  ; 
return   cipher  .  doFinal  (  src  )  ; 
} 








public   static   byte  [  ]  desDecrypt  (  byte  [  ]  crypt  )  throws   Exception  { 
javax  .  crypto  .  SecretKey   key  =  genDESKey  (  "123456781234567812345678"  .  getBytes  (  )  )  ; 
javax  .  crypto  .  Cipher   cipher  =  javax  .  crypto  .  Cipher  .  getInstance  (  "DESede"  )  ; 
cipher  .  init  (  javax  .  crypto  .  Cipher  .  DECRYPT_MODE  ,  key  )  ; 
return   cipher  .  doFinal  (  crypt  )  ; 
} 

public   static   byte  [  ]  encrypt  (  byte  [  ]  src  )  throws   Exception  { 
byte  [  ]  ret  =  null  ; 
javax  .  crypto  .  SecretKey   key  =  genDESKey  (  "123456781234567812345678"  .  getBytes  (  )  )  ; 
javax  .  crypto  .  Cipher   cipher  =  javax  .  crypto  .  Cipher  .  getInstance  (  "DESede/ECB/PKCS5Padding"  )  ; 
cipher  .  init  (  javax  .  crypto  .  Cipher  .  ENCRYPT_MODE  ,  key  )  ; 
int   blocks  =  1  ; 
byte  [  ]  temp  =  null  ; 
List   en  =  new   Vector  (  )  ; 
for  (  int   i  =  0  ;  i  <  blocks  ;  i  ++  )  { 
temp  =  cipher  .  update  (  src  )  ; 
{ 
for  (  int   j  =  0  ;  j  <  temp  .  length  ;  j  ++  )  { 
Byte   b  =  new   Byte  (  temp  [  j  ]  )  ; 
en  .  add  (  b  )  ; 
} 
} 
} 
temp  =  cipher  .  doFinal  (  )  ; 
for  (  int   j  =  0  ;  j  <  temp  .  length  ;  j  ++  )  { 
Byte   b  =  new   Byte  (  temp  [  j  ]  )  ; 
en  .  add  (  b  )  ; 
} 
ret  =  new   byte  [  en  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  en  .  size  (  )  ;  i  ++  )  { 
Byte   b  =  (  Byte  )  en  .  get  (  i  )  ; 
ret  [  i  ]  =  b  .  byteValue  (  )  ; 
} 
return   ret  ; 
} 

public   static   byte  [  ]  decrypt  (  byte  [  ]  src  )  throws   Exception  { 
byte  [  ]  ret  =  null  ; 
javax  .  crypto  .  SecretKey   key  =  genDESKey  (  "123456781234567812345678"  .  getBytes  (  )  )  ; 
javax  .  crypto  .  Cipher   cipher  =  javax  .  crypto  .  Cipher  .  getInstance  (  "DESede/ECB/PKCS5Padding"  )  ; 
cipher  .  init  (  javax  .  crypto  .  Cipher  .  DECRYPT_MODE  ,  key  )  ; 
int   blocks  =  1  ; 
byte  [  ]  temp  =  null  ; 
List   en  =  new   Vector  (  )  ; 
for  (  int   i  =  0  ;  i  <  blocks  ;  i  ++  )  { 
temp  =  cipher  .  update  (  src  )  ; 
{ 
for  (  int   j  =  0  ;  j  <  temp  .  length  ;  j  ++  )  { 
Byte   b  =  new   Byte  (  temp  [  j  ]  )  ; 
en  .  add  (  b  )  ; 
} 
} 
} 
temp  =  cipher  .  doFinal  (  )  ; 
for  (  int   j  =  0  ;  j  <  temp  .  length  ;  j  ++  )  { 
Byte   b  =  new   Byte  (  temp  [  j  ]  )  ; 
en  .  add  (  b  )  ; 
} 
ret  =  new   byte  [  en  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  en  .  size  (  )  ;  i  ++  )  { 
Byte   b  =  (  Byte  )  en  .  get  (  i  )  ; 
ret  [  i  ]  =  b  .  byteValue  (  )  ; 
} 
return   ret  ; 
} 

public   static   String   byteArr2HexStr  (  byte  [  ]  arrB  )  throws   Exception  { 
int   iLen  =  arrB  .  length  ; 
StringBuffer   sb  =  new   StringBuffer  (  iLen  *  2  )  ; 
for  (  int   i  =  0  ;  i  <  iLen  ;  i  ++  )  { 
int   intTmp  =  arrB  [  i  ]  ; 
while  (  intTmp  <  0  )  { 
intTmp  =  intTmp  +  256  ; 
} 
if  (  intTmp  <  16  )  { 
sb  .  append  (  "0"  )  ; 
} 
sb  .  append  (  Integer  .  toString  (  intTmp  ,  16  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   byte  [  ]  hexStr2ByteArr  (  String   strIn  )  throws   Exception  { 
byte  [  ]  arrB  =  strIn  .  getBytes  (  )  ; 
int   iLen  =  arrB  .  length  ; 
byte  [  ]  arrOut  =  new   byte  [  iLen  /  2  ]  ; 
for  (  int   i  =  0  ;  i  <  iLen  ;  i  =  i  +  2  )  { 
String   strTmp  =  new   String  (  arrB  ,  i  ,  2  )  ; 
arrOut  [  i  /  2  ]  =  (  byte  )  Integer  .  parseInt  (  strTmp  ,  16  )  ; 
} 
return   arrOut  ; 
} 

public   static   String   keepTextStyle  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  equals  (  ""  )  )  { 
return  ""  ; 
} 
text  =  text  .  replaceAll  (  "\n"  ,  "<br>"  )  ; 
text  =  text  .  replaceAll  (  " "  ,  "&nbsp;"  )  ; 
return   text  ; 
} 

public   static   String   convertTextStyle  (  String   text  )  { 
if  (  text  ==  null  ||  text  .  equals  (  ""  )  )  { 
return  ""  ; 
} 
text  =  text  .  replaceAll  (  "<br>"  ,  "\n"  )  ; 
text  =  text  .  replaceAll  (  "&nbsp;"  ,  " "  )  ; 
return   text  ; 
} 

public   static   String   toUpperChinese  (  double   val  )  { 
String   SignStr  =  ""  ; 
String   TailStr  =  ""  ; 
long   fraction  ,  integer  ; 
int   jiao  ,  fen  ; 
if  (  val  <  0  )  { 
val  =  -  val  ; 
SignStr  =  "��"  ; 
} 
if  (  val  >  99999999999999.999  ||  val  <  -  99999999999999.999  )  return  "��ֵλ����!"  ; 
long   temp  =  Math  .  round  (  val  *  100  )  ; 
integer  =  temp  /  100  ; 
fraction  =  temp  %  100  ; 
jiao  =  (  int  )  fraction  /  10  ; 
fen  =  (  int  )  fraction  %  10  ; 
if  (  jiao  ==  0  &&  fen  ==  0  )  { 
TailStr  =  "��"  ; 
}  else  { 
TailStr  =  HanDigiStr  [  jiao  ]  ; 
if  (  jiao  !=  0  )  TailStr  +=  "��"  ; 
if  (  integer  ==  0  &&  jiao  ==  0  )  TailStr  =  ""  ; 
if  (  fen  !=  0  )  TailStr  +=  HanDigiStr  [  fen  ]  +  "��"  ; 
} 
return  "��"  +  SignStr  +  PositiveIntegerToHanStr  (  String  .  valueOf  (  integer  )  )  +  "Ԫ"  +  TailStr  ; 
} 

private   static   String   PositiveIntegerToHanStr  (  String   NumStr  )  { 
String   RMBStr  =  ""  ; 
boolean   lastzero  =  false  ; 
boolean   hasvalue  =  false  ; 
int   len  ,  n  ; 
len  =  NumStr  .  length  (  )  ; 
if  (  len  >  15  )  return  "��ֵ���!"  ; 
for  (  int   i  =  len  -  1  ;  i  >=  0  ;  i  --  )  { 
if  (  NumStr  .  charAt  (  len  -  i  -  1  )  ==  ' '  )  continue  ; 
n  =  NumStr  .  charAt  (  len  -  i  -  1  )  -  '0'  ; 
if  (  n  <  0  ||  n  >  9  )  return  "���뺬�������ַ�!"  ; 
if  (  n  !=  0  )  { 
if  (  lastzero  )  RMBStr  +=  HanDigiStr  [  0  ]  ; 
if  (  !  (  n  ==  1  &&  (  i  %  4  )  ==  1  &&  i  ==  len  -  1  )  )  RMBStr  +=  HanDigiStr  [  n  ]  ; 
RMBStr  +=  HanDiviStr  [  i  ]  ; 
hasvalue  =  true  ; 
}  else  { 
if  (  (  i  %  8  )  ==  0  ||  (  (  i  %  8  )  ==  4  &&  hasvalue  )  )  RMBStr  +=  HanDiviStr  [  i  ]  ; 
} 
if  (  i  %  8  ==  0  )  hasvalue  =  false  ; 
lastzero  =  (  n  ==  0  )  &&  (  i  %  4  !=  0  )  ; 
} 
if  (  RMBStr  .  length  (  )  ==  0  )  return   HanDigiStr  [  0  ]  ; 
return   RMBStr  ; 
} 
} 

