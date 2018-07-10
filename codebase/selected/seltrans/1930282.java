package   gestorDeFicheros  ; 

import   java  .  io  .  *  ; 
import   java  .  security  .  *  ; 







public   class   MD5Sum  { 

public   static   final   int   SCOUR_MD5_BYTE_LIMIT  =  (  300  *  1024  )  ; 

private   static   MessageDigest   _md  =  null  ; 









public   static   String   md5Sum  (  String   str  )  { 
try  { 
return   md5Sum  (  str  .  getBytes  (  "UTF-8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   IllegalStateException  (  e  .  getMessage  (  )  )  ; 
} 
} 

public   static   String   md5Sum  (  byte  [  ]  input  )  { 
return   md5Sum  (  input  ,  -  1  )  ; 
} 

public   static   String   md5Sum  (  byte  [  ]  input  ,  int   limit  )  { 
try  { 
if  (  _md  ==  null  )  { 
_md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
} 
_md  .  reset  (  )  ; 
byte  [  ]  digest  ; 
if  (  limit  ==  -  1  )  { 
digest  =  _md  .  digest  (  input  )  ; 
}  else  { 
_md  .  update  (  input  ,  0  ,  limit  >  input  .  length  ?  input  .  length  :  limit  )  ; 
digest  =  _md  .  digest  (  )  ; 
} 
StringBuffer   hexString  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  digest  .  length  ;  i  ++  )  { 
hexString  .  append  (  hexDigit  (  digest  [  i  ]  )  )  ; 
} 
return   hexString  .  toString  (  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   IllegalStateException  (  e  .  getMessage  (  )  )  ; 
} 
} 








private   static   String   hexDigit  (  byte   x  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
char   c  ; 
c  =  (  char  )  (  (  x  >  >  4  )  &  0xf  )  ; 
if  (  c  >  9  )  { 
c  =  (  char  )  (  (  c  -  10  )  +  'a'  )  ; 
}  else  { 
c  =  (  char  )  (  c  +  '0'  )  ; 
} 
sb  .  append  (  c  )  ; 
c  =  (  char  )  (  x  &  0xf  )  ; 
if  (  c  >  9  )  { 
c  =  (  char  )  (  (  c  -  10  )  +  'a'  )  ; 
}  else  { 
c  =  (  char  )  (  c  +  '0'  )  ; 
} 
sb  .  append  (  c  )  ; 
return   sb  .  toString  (  )  ; 
} 













public   static   String   getFileMD5Sum  (  File   f  )  { 
String   sum  =  null  ; 
try  { 
FileInputStream   in  =  new   FileInputStream  (  f  .  getAbsolutePath  (  )  )  ; 
byte  [  ]  b  =  new   byte  [  1024  ]  ; 
int   num  =  0  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
while  (  (  num  =  in  .  read  (  b  )  )  !=  -  1  )  { 
out  .  write  (  b  ,  0  ,  num  )  ; 
if  (  out  .  size  (  )  >  SCOUR_MD5_BYTE_LIMIT  )  { 
sum  =  md5Sum  (  out  .  toByteArray  (  )  ,  SCOUR_MD5_BYTE_LIMIT  )  ; 
break  ; 
} 
} 
if  (  sum  ==  null  )  { 
sum  =  md5Sum  (  out  .  toByteArray  (  )  ,  SCOUR_MD5_BYTE_LIMIT  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   sum  ; 
} 
} 

