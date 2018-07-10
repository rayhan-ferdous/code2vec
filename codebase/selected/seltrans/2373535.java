package   org  .  openejb  .  admin  .  web  .  ejbgen  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 

public   abstract   class   EJBTemplate  { 

private   String   name  ; 

private   String   desc  ; 

private   String   auth  ; 

private   String   pack  ; 

private   String   sloc  ; 

private   String   styp  ; 

private   File   cdir  ; 

private   File   ddir  ; 

private   File   backup  ; 

private   File   packup  ; 

private   String   psep  =  System  .  getProperty  (  "file.separator"  )  ; 

public   abstract   void   createEJB  (  )  ; 

public   abstract   void   createEJBXML  (  )  ; 

public   abstract   void   createObjCode  (  )  ; 

public   abstract   void   createHmeCode  (  )  ; 

public   abstract   void   createBenCode  (  )  ; 









public   void   setVars  (  String   ejbname  ,  String   ejbdesc  ,  String   ejbauth  ,  String   ejbpack  ,  String   ejbsloc  ,  String   ejbstyp  )  { 
name  =  ejbname  ; 
desc  =  ejbdesc  ; 
auth  =  ejbauth  ; 
pack  =  ejbpack  ; 
styp  =  ejbstyp  ; 
sloc  =  ejbsloc  +  psep  +  ejbname  ; 
} 





public   void   createBackup  (  )  { 
File   metaback  =  new   File  (  backup  .  getPath  (  )  +  psep  +  "META-INF"  )  ; 
File   cbdir  =  new   File  (  backup  .  getPath  (  )  )  ; 
StringTokenizer   dirs  =  new   StringTokenizer  (  pack  ,  "\\."  )  ; 
backup  .  mkdir  (  )  ; 
metaback  .  mkdir  (  )  ; 
while  (  dirs  .  hasMoreTokens  (  )  )  { 
File   ndir  =  new   File  (  cbdir  .  getPath  (  )  +  psep  +  dirs  .  nextToken  (  )  )  ; 
ndir  .  mkdir  (  )  ; 
cbdir  =  new   File  (  ndir  .  getPath  (  )  )  ; 
} 
packup  =  new   File  (  cbdir  .  getPath  (  )  )  ; 
} 




public   void   createPackage  (  )  { 
cdir  =  new   File  (  sloc  )  ; 
ddir  =  new   File  (  sloc  )  ; 
backup  =  new   File  (  ddir  .  getPath  (  )  +  psep  +  "backup"  )  ; 
StringTokenizer   dirs  =  new   StringTokenizer  (  pack  ,  "\\."  )  ; 
ddir  .  mkdir  (  )  ; 
while  (  dirs  .  hasMoreTokens  (  )  )  { 
File   ndir  =  new   File  (  cdir  .  getPath  (  )  +  psep  +  dirs  .  nextToken  (  )  )  ; 
ndir  .  mkdir  (  )  ; 
cdir  =  new   File  (  ndir  .  getPath  (  )  )  ; 
} 
} 







public   void   createClass  (  String   ejbname  ,  String   ejbobj  )  { 
File   ejbObj  =  new   File  (  cdir  .  getPath  (  )  +  psep  +  ejbname  +  ejbobj  )  ; 
if  (  ejbObj  .  exists  (  )  )  { 
createBackup  (  )  ; 
File   fbackup  =  new   File  (  packup  .  getPath  (  )  +  psep  +  ejbname  +  ejbobj  )  ; 
ejbObj  .  renameTo  (  fbackup  )  ; 
ejbObj  .  delete  (  )  ; 
} 
try  { 
ejbObj  .  createNewFile  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Couldn\'t create file!"  )  ; 
} 
} 








public   void   writeClass  (  String   filename  ,  String   code  )  { 
File   wrtFile  =  new   File  (  cdir  .  getPath  (  )  +  psep  +  filename  )  ; 
try  { 
FileWriter   wrtWriter  =  new   FileWriter  (  wrtFile  )  ; 
wrtWriter  .  write  (  code  )  ; 
wrtWriter  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "File Not Found. - "  +  e  )  ; 
} 
} 







public   void   createXML  (  String   filename  ,  String   code  )  { 
String   location  =  sloc  ; 
File   metainf  =  new   File  (  location  +  psep  +  "META-INF"  )  ; 
File   xmlfile  =  new   File  (  location  +  psep  +  "META-INF"  +  psep  +  filename  )  ; 
if  (  metainf  .  exists  (  )  )  { 
if  (  xmlfile  .  exists  (  )  )  { 
File   nfile  =  new   File  (  backup  .  getPath  (  )  +  psep  +  "META-INF"  +  psep  +  filename  )  ; 
xmlfile  .  renameTo  (  nfile  )  ; 
xmlfile  .  delete  (  )  ; 
} 
}  else  { 
metainf  .  mkdir  (  )  ; 
} 
try  { 
xmlfile  .  createNewFile  (  )  ; 
try  { 
FileWriter   ejbWriter  =  new   FileWriter  (  xmlfile  )  ; 
ejbWriter  .  write  (  code  )  ; 
ejbWriter  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
System  .  out  .  println  (  "I/O Exception: "  +  e1  )  ; 
} 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "I/O Exception: "  +  e  )  ; 
} 
} 




public   void   buildZipFile  (  )  { 
File   bdir  =  new   File  (  getBeanDir  (  )  )  ; 
File   mdir  =  new   File  (  sloc  +  psep  +  "META-INF"  )  ; 
File   zdir  =  new   File  (  sloc  )  ; 
String   zname  =  name  +  ".zip"  ; 
File   myZipFile  =  new   File  (  zdir  ,  zname  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
try  { 
myZipFile  .  createNewFile  (  )  ; 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   FileOutputStream  (  myZipFile  )  )  ; 
File  [  ]  mfiles  =  mdir  .  listFiles  (  )  ; 
File  [  ]  bfiles  =  bdir  .  listFiles  (  )  ; 
StringTokenizer   dirs  =  new   StringTokenizer  (  pack  ,  "\\."  )  ; 
String   dpath  =  ""  ; 
out  .  putNextEntry  (  new   ZipEntry  (  "META-INF/"  )  )  ; 
for  (  int   i  =  0  ;  i  <  mfiles  .  length  ;  i  ++  )  { 
File   cfile  =  new   File  (  sloc  +  psep  +  "META-INF"  +  psep  +  mfiles  [  i  ]  .  getName  (  )  )  ; 
FileInputStream   in  =  new   FileInputStream  (  cfile  )  ; 
out  .  putNextEntry  (  new   ZipEntry  (  "META-INF/"  +  mfiles  [  i  ]  .  getName  (  )  )  )  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
out  .  closeEntry  (  )  ; 
in  .  close  (  )  ; 
} 
while  (  dirs  .  hasMoreTokens  (  )  )  { 
dpath  =  dpath  +  dirs  .  nextToken  (  )  +  "/"  ; 
out  .  putNextEntry  (  new   ZipEntry  (  dpath  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  bfiles  .  length  ;  i  ++  )  { 
File   cfile  =  new   File  (  getBeanDir  (  )  +  psep  +  bfiles  [  i  ]  .  getName  (  )  )  ; 
FileInputStream   in  =  new   FileInputStream  (  cfile  )  ; 
out  .  putNextEntry  (  new   ZipEntry  (  dpath  +  bfiles  [  i  ]  .  getName  (  )  )  )  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
out  .  closeEntry  (  )  ; 
in  .  close  (  )  ; 
} 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "I/O Exception: "  +  e  )  ; 
} 
} 






public   String   getBeanDir  (  )  { 
StringTokenizer   dirs  =  new   StringTokenizer  (  pack  ,  "\\."  )  ; 
String   beandir  =  sloc  ; 
while  (  dirs  .  hasMoreTokens  (  )  )  { 
beandir  =  beandir  +  psep  +  dirs  .  nextToken  (  )  ; 
} 
return   beandir  ; 
} 
} 

