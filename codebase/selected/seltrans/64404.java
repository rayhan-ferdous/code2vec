package   com  .  spring  .  workflow  .  util  ; 

import   java  .  lang  .  reflect  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 






public   class   StringUtils  { 

private   static   MessageDigest   digest  =  null  ; 

public   StringUtils  (  )  { 
} 

public   static   final   String   fill  (  String   someString  ,  int   number  ,  String   fillString  )  { 
if  (  someString  .  length  (  )  <  number  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  someString  )  ; 
int   totalLength  =  someString  .  length  (  )  ; 
while  (  totalLength  <  number  )  { 
sb  .  append  (  fillString  )  ; 
totalLength  +=  fillString  .  length  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 
return   someString  ; 
} 












public   static   final   String   surroundString  (  String   originalText  ,  String   searchFor  ,  String   insertBefore  ,  String   insertAfter  )  { 
return   surroundString  (  originalText  ,  searchFor  ,  insertBefore  ,  insertAfter  ,  true  )  ; 
} 













public   static   final   String   surroundString  (  String   originalText  ,  String   searchFor  ,  String   insertBefore  ,  String   insertAfter  ,  boolean   caseSensitive  )  { 
if  (  originalText  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
String   compareLine  =  originalText  ; 
if  (  !  caseSensitive  )  { 
compareLine  =  compareLine  .  toLowerCase  (  )  ; 
searchFor  =  searchFor  .  toLowerCase  (  )  ; 
} 
StringBuffer   returnValue  =  new   StringBuffer  (  )  ; 
if  (  (  i  =  compareLine  .  indexOf  (  searchFor  ,  i  )  )  >=  0  )  { 
while  (  (  i  =  compareLine  .  indexOf  (  searchFor  ,  i  )  )  >=  0  )  { 
returnValue  .  append  (  originalText  .  substring  (  0  ,  i  )  )  ; 
returnValue  .  append  (  insertBefore  )  ; 
returnValue  .  append  (  originalText  .  substring  (  i  ,  i  +  searchFor  .  length  (  )  )  )  ; 
returnValue  .  append  (  insertAfter  )  ; 
if  (  originalText  .  length  (  )  >=  i  +  searchFor  .  length  (  )  )  { 
originalText  =  originalText  .  substring  (  i  +  searchFor  .  length  (  )  )  ; 
compareLine  =  compareLine  .  substring  (  i  +  searchFor  .  length  (  )  )  ; 
}  else  { 
compareLine  =  ""  ; 
} 
} 
returnValue  .  append  (  compareLine  )  ; 
return   returnValue  .  toString  (  )  ; 
} 
return   originalText  ; 
} 













public   static   final   String   replaceCaseInSensitive  (  String   originalText  ,  String   searchFor  ,  String   replaceWith  )  { 
if  (  originalText  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
String   compareLine  =  originalText  .  toLowerCase  (  )  ; 
searchFor  =  searchFor  .  toLowerCase  (  )  ; 
if  (  (  i  =  compareLine  .  indexOf  (  searchFor  ,  i  )  )  >=  0  )  { 
char  [  ]  originalChars  =  originalText  .  toCharArray  (  )  ; 
char  [  ]  replaceWithChars  =  replaceWith  .  toCharArray  (  )  ; 
int   searchForLength  =  searchFor  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  originalChars  .  length  )  ; 
buf  .  append  (  originalChars  ,  0  ,  i  )  .  append  (  replaceWithChars  )  ; 
i  +=  searchForLength  ; 
int   j  =  i  ; 
while  (  (  i  =  compareLine  .  indexOf  (  searchFor  ,  i  )  )  >  0  )  { 
buf  .  append  (  originalChars  ,  j  ,  i  -  j  )  .  append  (  replaceWithChars  )  ; 
i  +=  searchForLength  ; 
j  =  i  ; 
} 
buf  .  append  (  originalChars  ,  j  ,  originalChars  .  length  -  j  )  ; 
return   buf  .  toString  (  )  ; 
} 
return   originalText  ; 
} 













public   static   final   String   replace  (  String   originalText  ,  String   searchFor  ,  String   replaceWith  )  { 
if  (  originalText  ==  null  )  { 
return   null  ; 
} 
int   i  =  0  ; 
if  (  (  i  =  originalText  .  indexOf  (  searchFor  ,  i  )  )  >=  0  )  { 
char  [  ]  originalText2  =  originalText  .  toCharArray  (  )  ; 
char  [  ]  newString2  =  replaceWith  .  toCharArray  (  )  ; 
int   oLength  =  searchFor  .  length  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  originalText2  .  length  )  ; 
buf  .  append  (  originalText2  ,  0  ,  i  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
int   j  =  i  ; 
while  (  (  i  =  originalText  .  indexOf  (  searchFor  ,  i  )  )  >  0  )  { 
buf  .  append  (  originalText2  ,  j  ,  i  -  j  )  .  append  (  newString2  )  ; 
i  +=  oLength  ; 
j  =  i  ; 
} 
buf  .  append  (  originalText2  ,  j  ,  originalText2  .  length  -  j  )  ; 
return   buf  .  toString  (  )  ; 
} 
return   originalText  ; 
} 









public   static   final   String   findBetween  (  String   searchIn  ,  String   beginString  ,  String   endString  )  { 
int   beginpos  =  searchIn  .  indexOf  (  beginString  )  ; 
int   endpos  =  -  1  ; 
if  (  beginpos  !=  -  1  )  { 
endpos  =  searchIn  .  substring  (  beginpos  +  beginString  .  length  (  )  )  .  indexOf  (  endString  )  ; 
if  (  endpos  !=  -  1  )  { 
return   searchIn  .  substring  (  beginpos  +  beginString  .  length  (  )  ,  beginpos  +  beginString  .  length  (  )  +  endpos  )  ; 
} 
} 
return   null  ; 
} 










public   static   final   String   convert2Html  (  String   stringToConvert  )  { 
if  (  stringToConvert  ==  null  )  { 
return   null  ; 
} 
char   content  [  ]  =  new   char  [  stringToConvert  .  length  (  )  ]  ; 
stringToConvert  .  getChars  (  0  ,  stringToConvert  .  length  (  )  ,  content  ,  0  )  ; 
StringBuffer   result  =  new   StringBuffer  (  content  .  length  +  50  )  ; 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  i  ++  )  switch  (  content  [  i  ]  )  { 
case   60  : 
result  .  append  (  "&lt;"  )  ; 
break  ; 
case   62  : 
result  .  append  (  "&gt;"  )  ; 
break  ; 
case   38  : 
result  .  append  (  "&amp;"  )  ; 
break  ; 
case   34  : 
result  .  append  (  "&quot;"  )  ; 
break  ; 
case   8364  : 
case   128  : 
result  .  append  (  "&euro;"  )  ; 
break  ; 
default  : 
result  .  append  (  content  [  i  ]  )  ; 
break  ; 
} 
return   result  .  toString  (  )  ; 
} 








public   static   final   String   toString  (  Object   o  )  { 
if  (  o  ==  null  )  { 
return   null  ; 
} 
if  (  o   instanceof   Boolean  )  { 
return  (  (  Boolean  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Byte  )  { 
return  (  (  Byte  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Double  )  { 
return  (  (  Double  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Float  )  { 
return  (  (  Float  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Integer  )  { 
return  (  (  Integer  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Long  )  { 
return  (  (  Long  )  o  )  .  toString  (  )  ; 
}  else   if  (  o   instanceof   Short  )  { 
return  (  (  Short  )  o  )  .  toString  (  )  ; 
} 
return   o  .  toString  (  )  ; 
} 









public   static   String   replaceForXML  (  String   returnValue  )  { 
if  (  returnValue  !=  null  )  { 
char  [  ]  originalText  =  returnValue  .  toCharArray  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
int   length  =  originalText  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
if  (  originalText  [  i  ]  >  122  ||  originalText  [  i  ]  ==  '&'  ||  originalText  [  i  ]  ==  '<'  ||  originalText  [  i  ]  ==  '>'  )  { 
buf  .  append  (  "&#"  )  .  append  (  (  int  )  originalText  [  i  ]  )  .  append  (  ";"  )  ; 
}  else   if  (  originalText  [  i  ]  <  32  &&  (  originalText  [  i  ]  !=  9  &&  originalText  [  i  ]  !=  10  &&  originalText  [  i  ]  !=  13  )  )  { 
continue  ; 
}  else  { 
buf  .  append  (  originalText  [  i  ]  )  ; 
} 
} 
return   buf  .  toString  (  )  ; 
} 
return  ""  ; 
} 







public   static   boolean   checkEmail  (  String   value  )  { 
int   i  =  value  .  indexOf  (  " "  )  ; 
if  (  i  !=  -  1  )  { 
return   false  ; 
} 
i  =  value  .  indexOf  (  "@"  )  ; 
if  (  i  <=  0  )  { 
return   false  ; 
}  else  { 
String   temp  =  value  .  substring  (  i  +  1  )  ; 
i  =  temp  .  indexOf  (  "."  )  ; 
if  (  i  <=  0  )  { 
return   false  ; 
} 
temp  =  temp  .  substring  (  i  +  1  )  ; 
if  (  temp  .  length  (  )  ==  0  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 







public   static   String   printObject  (  Object   ob  )  { 
StringBuffer   sb  =  new   StringBuffer  (  ob  .  getClass  (  )  .  getName  (  )  +  "\n"  )  ; 
try  { 
Field  [  ]  fa  =  ob  .  getClass  (  )  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fa  .  length  ;  i  ++  )  { 
Field   f  =  fa  [  i  ]  ; 
f  .  setAccessible  (  true  )  ; 
java  .  lang  .  Object   fob  =  fa  [  i  ]  .  get  (  ob  )  ; 
sb  .  append  (  "<field><field-name>"  +  f  .  getName  (  )  +  "</fieldname><field-value>"  +  fob  +  "</field-value></field>\n"  )  ; 
if  (  fob   instanceof   java  .  lang  .  Object  [  ]  )  { 
java  .  lang  .  Object  [  ]  oba  =  (  java  .  lang  .  Object  [  ]  )  fob  ; 
for  (  int   j  =  0  ;  j  <  oba  .  length  ;  j  ++  )  { 
sb  .  append  (  "      "  +  oba  [  j  ]  +  "\n"  )  ; 
} 
} 
} 
}  catch  (  IllegalAccessException   iae  )  { 
iae  .  printStackTrace  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 

public   static   int   getIntParameter  (  Map   parameters  ,  String   theKey  ,  int   defaultValue  )  { 
if  (  parameters  .  containsKey  (  theKey  )  )  { 
try  { 
return   Integer  .  parseInt  (  (  String  )  parameters  .  get  (  theKey  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
return   defaultValue  ; 
} 

public   static   String   nativeToAscii  (  String   text  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
char  [  ]  chars  =  text  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  chars  .  length  ;  i  ++  )  { 
if  (  chars  [  i  ]  <  128  )  { 
buffer  .  append  (  chars  [  i  ]  )  ; 
}  else   if  (  chars  [  i  ]  >=  128  &&  chars  [  i  ]  <  256  )  { 
buffer  .  append  (  "\\u00"  +  Integer  .  toString  (  chars  [  i  ]  ,  16  )  )  ; 
}  else  { 
buffer  .  append  (  "\\u"  +  Integer  .  toString  (  chars  [  i  ]  ,  16  )  )  ; 
} 
} 
return   buffer  .  toString  (  )  ; 
} 

public   static   String   asciiToNative  (  String   theString  )  { 
char   aChar  ; 
int   len  =  theString  .  length  (  )  ; 
StringBuffer   outBuffer  =  new   StringBuffer  (  len  )  ; 
for  (  int   x  =  0  ;  x  <  len  ;  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
if  (  aChar  ==  '\\'  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
if  (  aChar  ==  'u'  )  { 
int   value  =  0  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
switch  (  aChar  )  { 
case  '0'  : 
case  '1'  : 
case  '2'  : 
case  '3'  : 
case  '4'  : 
case  '5'  : 
case  '6'  : 
case  '7'  : 
case  '8'  : 
case  '9'  : 
value  =  (  value  <<  4  )  +  aChar  -  '0'  ; 
break  ; 
case  'a'  : 
case  'b'  : 
case  'c'  : 
case  'd'  : 
case  'e'  : 
case  'f'  : 
value  =  (  value  <<  4  )  +  10  +  aChar  -  'a'  ; 
break  ; 
case  'A'  : 
case  'B'  : 
case  'C'  : 
case  'D'  : 
case  'E'  : 
case  'F'  : 
value  =  (  value  <<  4  )  +  10  +  aChar  -  'A'  ; 
break  ; 
default  : 
throw   new   IllegalArgumentException  (  "Malformed \\uxxxx encoding."  )  ; 
} 
} 
outBuffer  .  append  (  (  char  )  value  )  ; 
}  else  { 
if  (  aChar  ==  't'  )  aChar  =  '\t'  ;  else   if  (  aChar  ==  'r'  )  aChar  =  '\r'  ;  else   if  (  aChar  ==  'n'  )  aChar  =  '\n'  ;  else   if  (  aChar  ==  'f'  )  aChar  =  '\f'  ; 
outBuffer  .  append  (  aChar  )  ; 
} 
}  else  { 
outBuffer  .  append  (  aChar  )  ; 
} 
} 
return   outBuffer  .  toString  (  )  ; 
} 

























public   static   final   synchronized   String   hash  (  String   data  )  { 
if  (  digest  ==  null  )  { 
try  { 
digest  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
System  .  err  .  println  (  "Failed to load the MD5 MessageDigest. "  +  "Jive will be unable to function normally."  )  ; 
nsae  .  printStackTrace  (  )  ; 
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
} 

