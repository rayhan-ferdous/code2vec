package   net  .  asfun  .  jangod  .  lib  .  filter  ; 

import   static   net  .  asfun  .  jangod  .  util  .  logging  .  JangodLogger  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   net  .  asfun  .  jangod  .  interpret  .  InterpretException  ; 
import   net  .  asfun  .  jangod  .  interpret  .  JangodInterpreter  ; 
import   net  .  asfun  .  jangod  .  lib  .  Filter  ; 

public   class   Md5Filter   implements   Filter  { 

final   String  [  ]  NOSTR  =  {  "0"  ,  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  ,  "a"  ,  "b"  ,  "c"  ,  "d"  ,  "e"  ,  "f"  }  ; 

final   String   md5  =  "MD5"  ; 

private   String   byteToArrayString  (  byte   bByte  )  { 
int   temp  =  bByte  ; 
if  (  temp  <  0  )  { 
temp  +=  256  ; 
} 
int   iD1  =  temp  /  16  ; 
int   iD2  =  temp  %  16  ; 
return   NOSTR  [  iD1  ]  +  NOSTR  [  iD2  ]  ; 
} 

private   String   byteToString  (  byte  [  ]  bByte  )  { 
StringBuffer   sBuffer  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  bByte  .  length  ;  i  ++  )  { 
sBuffer  .  append  (  byteToArrayString  (  bByte  [  i  ]  )  )  ; 
} 
return   sBuffer  .  toString  (  )  ; 
} 

public   String   md5  (  String   str  )  { 
String   result  =  null  ; 
MessageDigest   md  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  md5  )  ; 
result  =  byteToString  (  md  .  digest  (  str  .  getBytes  (  )  )  )  ; 
}  catch  (  NoSuchAlgorithmException   ex  )  { 
JangodLogger  .  severe  (  ex  .  getMessage  (  )  )  ; 
} 
return   result  ; 
} 

@  Override 
public   Object   filter  (  Object   object  ,  JangodInterpreter   interpreter  ,  String  ...  arg  )  throws   InterpretException  { 
if  (  object   instanceof   String  )  { 
return   md5  (  (  String  )  object  )  ; 
} 
return   object  ; 
} 

@  Override 
public   String   getName  (  )  { 
return  "md5"  ; 
} 
} 

