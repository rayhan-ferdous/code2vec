package   cn  .  edu  .  wuse  .  musicxml  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilenameFilter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 






public   class   FileTool  { 







public   static   String   convertFileLength  (  long   l  )  { 
int   x  =  0  ; 
int   r  =  0  ; 
String   s  =  ""  ; 
if  (  l  <  1024  )  return   l  +  "b"  ; 
while  (  l  >=  1024  )  { 
r  =  (  int  )  (  r  +  l  %  1024  *  Math  .  pow  (  1024  ,  x  )  )  ; 
l  =  l  /  1024  ; 
x  ++  ; 
} 
String   rs  =  r  *  1.0  /  Math  .  pow  (  1024  ,  x  )  +  ""  ; 
s  =  l  +  "."  +  (  rs  .  length  (  )  >  4  ?  rs  .  substring  (  2  ,  4  )  :  rs  .  substring  (  2  )  )  ; 
if  (  x  ==  1  )  s  +=  "K"  ;  else   if  (  x  ==  2  )  s  +=  "M"  ;  else   if  (  x  ==  3  )  s  +=  "G"  ; 
return   s  ; 
} 






public   static   String   formatTime  (  long   t  )  { 
return   new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  .  format  (  new   Date  (  t  )  )  ; 
} 

public   static   String   convertTime  (  long   m  )  { 
m  =  m  /  1000  ; 
long   r  =  0  ; 
String   s  =  "s"  ; 
for  (  int   i  =  0  ;  i  <  3  ;  i  ++  )  { 
r  =  m  %  60  ; 
s  =  r  +  s  ; 
if  (  r  <  10  )  s  =  ":0"  +  s  ;  else   s  =  ":"  +  s  ; 
m  =  m  /  60  ; 
} 
return   s  .  substring  (  1  )  ; 
} 

public   static   byte  [  ]  getBytes  (  String   s  ,  String   incoding  )  { 
byte  [  ]  ret  =  {  }  ; 
try  { 
ret  =  s  .  getBytes  (  incoding  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   ret  ; 
} 







public   static   boolean   zip  (  String   source  ,  String   zipfile  )  throws   Exception  { 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   FileOutputStream  (  zipfile  )  )  ; 
File   f  =  new   File  (  source  )  ; 
doZip  (  out  ,  f  ,  ""  ,  new   NoFilter  (  )  )  ; 
return   false  ; 
} 








public   static   boolean   unZip  (  String   target  ,  String   zipfile  )  throws   IOException  { 
ZipFile   zf  =  new   ZipFile  (  zipfile  )  ; 
Enumeration   en  =  zf  .  entries  (  )  ; 
while  (  en  .  hasMoreElements  (  )  )  { 
ZipEntry   fi  =  (  ZipEntry  )  en  .  nextElement  (  )  ; 
if  (  fi  .  isDirectory  (  )  )  { 
File   f  =  new   File  (  target  +  fi  .  getName  (  )  )  ; 
f  .  mkdirs  (  )  ; 
}  else  { 
InputStream   in  =  zf  .  getInputStream  (  fi  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  target  +  fi  .  getName  (  )  )  ; 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
int   len  =  0  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  out  .  write  (  buf  ,  0  ,  len  )  ; 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
} 
} 
zf  .  close  (  )  ; 
return   true  ; 
} 










public   static   boolean   zipMxl  (  String   source  ,  String   mxlfile  )  throws   Exception  { 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   FileOutputStream  (  mxlfile  )  )  ; 
File   f  =  new   File  (  source  )  ; 
doZip  (  out  ,  f  ,  ""  ,  new   MusicXmlFileFilter  (  )  )  ; 
return   false  ; 
} 







public   static   boolean   upZipMxl  (  String   target  ,  String   mxlfile  )  throws   Exception  { 
ZipFile   zf  =  new   ZipFile  (  mxlfile  )  ; 
Enumeration   en  =  zf  .  entries  (  )  ; 
List  <  String  >  subMxls  =  new   ArrayList  <  String  >  (  )  ; 
while  (  en  .  hasMoreElements  (  )  )  { 
ZipEntry   fi  =  (  ZipEntry  )  en  .  nextElement  (  )  ; 
if  (  fi  .  isDirectory  (  )  )  { 
File   f  =  new   File  (  target  +  fi  .  getName  (  )  )  ; 
f  .  mkdirs  (  )  ; 
}  else  { 
if  (  fi  .  getName  (  )  .  toLowerCase  (  )  .  endsWith  (  "mxl"  )  )  subMxls  .  add  (  target  +  fi  .  getName  (  )  )  ; 
InputStream   in  =  zf  .  getInputStream  (  fi  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  target  +  fi  .  getName  (  )  )  ; 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
int   len  =  0  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  out  .  write  (  buf  ,  0  ,  len  )  ; 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
} 
} 
zf  .  close  (  )  ; 
for  (  String   subMxl  :  subMxls  )  { 
String   subDir  =  subMxl  .  substring  (  subMxl  .  lastIndexOf  (  File  .  separator  )  ,  subMxl  .  lastIndexOf  (  "."  )  )  ; 
upZipMxl  (  target  +  subDir  ,  subMxl  )  ; 
} 
return   true  ; 
} 

private   static   void   doZip  (  ZipOutputStream   out  ,  File   f  ,  String   base  ,  FilenameFilter   filter  )  throws   Exception  { 
if  (  f  .  isDirectory  (  )  )  { 
File  [  ]  fl  =  f  .  listFiles  (  filter  )  ; 
out  .  putNextEntry  (  new   ZipEntry  (  base  +  "/"  )  )  ; 
base  =  base  .  length  (  )  ==  0  ?  ""  :  base  +  "/"  ; 
for  (  int   i  =  0  ;  i  <  fl  .  length  ;  i  ++  )  { 
doZip  (  out  ,  fl  [  i  ]  ,  base  +  fl  [  i  ]  .  getName  (  )  ,  filter  )  ; 
} 
}  else  { 
out  .  putNextEntry  (  new   ZipEntry  (  base  )  )  ; 
FileInputStream   in  =  new   FileInputStream  (  f  )  ; 
int   b  ; 
System  .  out  .  println  (  base  )  ; 
while  (  (  b  =  in  .  read  (  )  )  !=  -  1  )  { 
out  .  write  (  b  )  ; 
} 
in  .  close  (  )  ; 
} 
} 

private   static   class   MusicXmlFileFilter   implements   FilenameFilter  { 

public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   name  .  toLowerCase  (  )  .  endsWith  (  "xml"  )  ||  name  .  toLowerCase  (  )  .  endsWith  (  "mxl"  )  ; 
} 
} 

private   static   class   NoFilter   implements   FilenameFilter  { 

public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   true  ; 
} 
} 
} 

