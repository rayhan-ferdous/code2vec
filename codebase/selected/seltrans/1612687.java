package   temmental  ; 

import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 

public   class   StringUtils  { 

private   static   final   char  [  ]  HEXADECIMAL_DIGITS  =  {  '0'  ,  '1'  ,  '2'  ,  '3'  ,  '4'  ,  '5'  ,  '6'  ,  '7'  ,  '8'  ,  '9'  ,  'a'  ,  'b'  ,  'c'  ,  'd'  ,  'e'  ,  'f'  }  ; 







public   static   String   join  (  String   sep  ,  List  <  String  >  words  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
for  (  Iterator  <  String  >  iterator  =  words  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
s  .  append  (  iterator  .  next  (  )  )  ; 
if  (  iterator  .  hasNext  (  )  )  { 
s  .  append  (  sep  )  ; 
} 
} 
return   s  .  toString  (  )  ; 
} 







public   static   String   join  (  String   sep  ,  String  ...  words  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  words  .  length  ;  i  ++  )  { 
s  .  append  (  words  [  i  ]  )  ; 
if  (  i  <  words  .  length  -  1  )  { 
s  .  append  (  sep  )  ; 
} 
} 
return   s  .  toString  (  )  ; 
} 






public   static   boolean   isNotEmpty  (  String   string  )  { 
return   string  !=  null  &&  !  string  .  equals  (  ""  )  ; 
} 






public   static   boolean   isEmpty  (  String   string  )  { 
return   string  ==  null  ||  string  .  equals  (  ""  )  ; 
} 






public   static   String   hexlify  (  byte  [  ]  data  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   low  =  (  int  )  (  data  [  i  ]  &  0x0f  )  ; 
int   high  =  (  int  )  (  (  data  [  i  ]  &  0xf0  )  >  >  4  )  ; 
buffer  .  append  (  HEXADECIMAL_DIGITS  [  high  ]  )  ; 
buffer  .  append  (  HEXADECIMAL_DIGITS  [  low  ]  )  ; 
} 
return   buffer  .  toString  (  )  ; 
} 

private   static   String   convertToHex  (  byte  [  ]  data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 









public   static   String   digest  (  String   text  ,  String   algorithm  ,  String   charsetName  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
md  .  update  (  text  .  getBytes  (  charsetName  )  ,  0  ,  text  .  length  (  )  )  ; 
return   convertToHex  (  md  .  digest  (  )  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   RuntimeException  (  "unexpected exception: "  +  e  ,  e  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   RuntimeException  (  "unexpected exception: "  +  e  ,  e  )  ; 
} 
} 







public   static   String   remove  (  String   string  ,  String  ...  words  )  { 
List  <  String  >  list  =  new   ArrayList  <  String  >  (  )  ; 
for  (  String   word  :  words  )  { 
list  .  add  (  word  )  ; 
} 
return   remove  (  string  ,  list  )  ; 
} 







public   static   String   remove  (  String   string  ,  List  <  String  >  words  )  { 
Map  <  String  ,  String  >  map  =  new   HashMap  <  String  ,  String  >  (  )  ; 
for  (  String   word  :  words  )  { 
map  .  put  (  word  ,  ""  )  ; 
} 
return   replace  (  string  ,  map  )  ; 
} 







public   static   String   replace  (  String   string  ,  Map  <  String  ,  String  >  map  )  { 
List  <  String  >  keys  =  new   ArrayList  <  String  >  (  map  .  keySet  (  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  ""  )  ; 
int   i  =  0  ; 
while  (  i  <  string  .  length  (  )  )  { 
int   minFoundAt  =  -  1  ; 
String   foundKey  =  null  ; 
for  (  String   key  :  keys  )  { 
int   foundAt  =  string  .  indexOf  (  key  ,  i  )  ; 
if  (  foundAt  >=  0  )  { 
if  (  minFoundAt  ==  -  1  )  { 
minFoundAt  =  foundAt  ; 
foundKey  =  key  ; 
}  else  { 
if  (  foundAt  <  minFoundAt  )  { 
minFoundAt  =  foundAt  ; 
foundKey  =  key  ; 
} 
} 
} 
} 
if  (  minFoundAt  ==  -  1  )  { 
sb  .  append  (  string  .  substring  (  i  )  )  ; 
return   sb  .  toString  (  )  ; 
} 
sb  .  append  (  string  .  substring  (  i  ,  minFoundAt  )  )  ; 
sb  .  append  (  map  .  get  (  foundKey  )  )  ; 
i  =  minFoundAt  +  foundKey  .  length  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   String   replace  (  String   string  ,  String  ...  oldNewWords  )  { 
Map  <  String  ,  String  >  map  =  new   HashMap  <  String  ,  String  >  (  )  ; 
if  (  oldNewWords  .  length  %  2  !=  0  )  throw   new   RuntimeException  (  "Invalid number of elements (key/value list implies an even size)."  )  ; 
for  (  int   i  =  0  ;  i  <  oldNewWords  .  length  /  2  ;  i  ++  )  { 
map  .  put  (  oldNewWords  [  2  *  i  ]  ,  oldNewWords  [  2  *  i  +  1  ]  )  ; 
} 
return   replace  (  string  ,  map  )  ; 
} 
} 

