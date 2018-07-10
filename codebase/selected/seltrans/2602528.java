package   sun  .  tools  .  jar  ; 

import   java  .  io  .  *  ; 
import   java  .  nio  .  file  .  Path  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  zip  .  *  ; 
import   java  .  util  .  jar  .  *  ; 
import   java  .  util  .  jar  .  Manifest  ; 
import   java  .  text  .  MessageFormat  ; 
import   sun  .  misc  .  JarIndex  ; 
import   static   sun  .  misc  .  JarIndex  .  INDEX_NAME  ; 
import   static   java  .  util  .  jar  .  JarFile  .  MANIFEST_NAME  ; 
import   static   java  .  nio  .  file  .  StandardCopyOption  .  REPLACE_EXISTING  ; 






public   class   Main  { 

String   program  ; 

PrintStream   out  ,  err  ; 

String   fname  ,  mname  ,  ename  ; 

String   zname  =  ""  ; 

String  [  ]  files  ; 

String   rootjar  =  null  ; 

Map  <  String  ,  File  >  entryMap  =  new   HashMap  <  String  ,  File  >  (  )  ; 

Set  <  File  >  entries  =  new   LinkedHashSet  <  File  >  (  )  ; 

Set  <  String  >  paths  =  new   HashSet  <  String  >  (  )  ; 

boolean   cflag  ,  uflag  ,  xflag  ,  tflag  ,  vflag  ,  flag0  ,  Mflag  ,  iflag  ; 

static   final   String   MANIFEST_DIR  =  "META-INF/"  ; 

static   final   String   VERSION  =  "1.0"  ; 

private   static   ResourceBundle   rsrc  ; 






private   static   final   boolean   useExtractionTime  =  Boolean  .  getBoolean  (  "sun.tools.jar.useExtractionTime"  )  ; 




static  { 
try  { 
rsrc  =  ResourceBundle  .  getBundle  (  "sun.tools.jar.resources.jar"  )  ; 
}  catch  (  MissingResourceException   e  )  { 
throw   new   Error  (  "Fatal: Resource for jar is missing"  )  ; 
} 
} 

private   String   getMsg  (  String   key  )  { 
try  { 
return  (  rsrc  .  getString  (  key  )  )  ; 
}  catch  (  MissingResourceException   e  )  { 
throw   new   Error  (  "Error in message file"  )  ; 
} 
} 

private   String   formatMsg  (  String   key  ,  String   arg  )  { 
String   msg  =  getMsg  (  key  )  ; 
String  [  ]  args  =  new   String  [  1  ]  ; 
args  [  0  ]  =  arg  ; 
return   MessageFormat  .  format  (  msg  ,  (  Object  [  ]  )  args  )  ; 
} 

private   String   formatMsg2  (  String   key  ,  String   arg  ,  String   arg1  )  { 
String   msg  =  getMsg  (  key  )  ; 
String  [  ]  args  =  new   String  [  2  ]  ; 
args  [  0  ]  =  arg  ; 
args  [  1  ]  =  arg1  ; 
return   MessageFormat  .  format  (  msg  ,  (  Object  [  ]  )  args  )  ; 
} 

public   Main  (  PrintStream   out  ,  PrintStream   err  ,  String   program  )  { 
this  .  out  =  out  ; 
this  .  err  =  err  ; 
this  .  program  =  program  ; 
} 





private   static   File   createTempFileInSameDirectoryAs  (  File   file  )  throws   IOException  { 
File   dir  =  file  .  getParentFile  (  )  ; 
if  (  dir  ==  null  )  dir  =  new   File  (  "."  )  ; 
return   File  .  createTempFile  (  "jartmp"  ,  null  ,  dir  )  ; 
} 

private   boolean   ok  ; 




public   synchronized   boolean   run  (  String   args  [  ]  )  { 
ok  =  true  ; 
if  (  !  parseArgs  (  args  )  )  { 
return   false  ; 
} 
try  { 
if  (  cflag  ||  uflag  )  { 
if  (  fname  !=  null  )  { 
zname  =  fname  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
if  (  zname  .  startsWith  (  "./"  )  )  { 
zname  =  zname  .  substring  (  2  )  ; 
} 
} 
} 
if  (  cflag  )  { 
Manifest   manifest  =  null  ; 
InputStream   in  =  null  ; 
if  (  !  Mflag  )  { 
if  (  mname  !=  null  )  { 
in  =  new   FileInputStream  (  mname  )  ; 
manifest  =  new   Manifest  (  new   BufferedInputStream  (  in  )  )  ; 
}  else  { 
manifest  =  new   Manifest  (  )  ; 
} 
addVersion  (  manifest  )  ; 
addCreatedBy  (  manifest  )  ; 
if  (  isAmbiguousMainClass  (  manifest  )  )  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
return   false  ; 
} 
if  (  ename  !=  null  )  { 
addMainClass  (  manifest  ,  ename  )  ; 
} 
} 
OutputStream   out  ; 
if  (  fname  !=  null  )  { 
out  =  new   FileOutputStream  (  fname  )  ; 
}  else  { 
out  =  new   FileOutputStream  (  FileDescriptor  .  out  )  ; 
if  (  vflag  )  { 
vflag  =  false  ; 
} 
} 
expand  (  null  ,  files  ,  false  )  ; 
create  (  new   BufferedOutputStream  (  out  ,  4096  )  ,  manifest  )  ; 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
out  .  close  (  )  ; 
}  else   if  (  uflag  )  { 
File   inputFile  =  null  ,  tmpFile  =  null  ; 
FileInputStream   in  ; 
FileOutputStream   out  ; 
if  (  fname  !=  null  )  { 
inputFile  =  new   File  (  fname  )  ; 
tmpFile  =  createTempFileInSameDirectoryAs  (  inputFile  )  ; 
in  =  new   FileInputStream  (  inputFile  )  ; 
out  =  new   FileOutputStream  (  tmpFile  )  ; 
}  else  { 
in  =  new   FileInputStream  (  FileDescriptor  .  in  )  ; 
out  =  new   FileOutputStream  (  FileDescriptor  .  out  )  ; 
vflag  =  false  ; 
} 
InputStream   manifest  =  (  !  Mflag  &&  (  mname  !=  null  )  )  ?  (  new   FileInputStream  (  mname  )  )  :  null  ; 
expand  (  null  ,  files  ,  true  )  ; 
boolean   updateOk  =  update  (  in  ,  new   BufferedOutputStream  (  out  )  ,  manifest  ,  null  )  ; 
if  (  ok  )  { 
ok  =  updateOk  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
if  (  manifest  !=  null  )  { 
manifest  .  close  (  )  ; 
} 
if  (  fname  !=  null  )  { 
inputFile  .  delete  (  )  ; 
if  (  !  tmpFile  .  renameTo  (  inputFile  )  )  { 
tmpFile  .  delete  (  )  ; 
throw   new   IOException  (  getMsg  (  "error.write.file"  )  )  ; 
} 
tmpFile  .  delete  (  )  ; 
} 
}  else   if  (  tflag  )  { 
replaceFSC  (  files  )  ; 
if  (  fname  !=  null  )  { 
list  (  fname  ,  files  )  ; 
}  else  { 
InputStream   in  =  new   FileInputStream  (  FileDescriptor  .  in  )  ; 
try  { 
list  (  new   BufferedInputStream  (  in  )  ,  files  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 
}  else   if  (  xflag  )  { 
replaceFSC  (  files  )  ; 
if  (  fname  !=  null  &&  files  !=  null  )  { 
extract  (  fname  ,  files  )  ; 
}  else  { 
InputStream   in  =  (  fname  ==  null  )  ?  new   FileInputStream  (  FileDescriptor  .  in  )  :  new   FileInputStream  (  fname  )  ; 
try  { 
extract  (  new   BufferedInputStream  (  in  )  ,  files  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 
}  else   if  (  iflag  )  { 
genIndex  (  rootjar  ,  files  )  ; 
} 
}  catch  (  IOException   e  )  { 
fatalError  (  e  )  ; 
ok  =  false  ; 
}  catch  (  Error   ee  )  { 
ee  .  printStackTrace  (  )  ; 
ok  =  false  ; 
}  catch  (  Throwable   t  )  { 
t  .  printStackTrace  (  )  ; 
ok  =  false  ; 
} 
out  .  flush  (  )  ; 
err  .  flush  (  )  ; 
return   ok  ; 
} 




boolean   parseArgs  (  String   args  [  ]  )  { 
try  { 
args  =  CommandLine  .  parse  (  args  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
fatalError  (  formatMsg  (  "error.cant.open"  ,  e  .  getMessage  (  )  )  )  ; 
return   false  ; 
}  catch  (  IOException   e  )  { 
fatalError  (  e  )  ; 
return   false  ; 
} 
int   count  =  1  ; 
try  { 
String   flags  =  args  [  0  ]  ; 
if  (  flags  .  startsWith  (  "-"  )  )  { 
flags  =  flags  .  substring  (  1  )  ; 
} 
for  (  int   i  =  0  ;  i  <  flags  .  length  (  )  ;  i  ++  )  { 
switch  (  flags  .  charAt  (  i  )  )  { 
case  'c'  : 
if  (  xflag  ||  tflag  ||  uflag  )  { 
usageError  (  )  ; 
return   false  ; 
} 
cflag  =  true  ; 
break  ; 
case  'u'  : 
if  (  cflag  ||  xflag  ||  tflag  )  { 
usageError  (  )  ; 
return   false  ; 
} 
uflag  =  true  ; 
break  ; 
case  'x'  : 
if  (  cflag  ||  uflag  ||  tflag  )  { 
usageError  (  )  ; 
return   false  ; 
} 
xflag  =  true  ; 
break  ; 
case  't'  : 
if  (  cflag  ||  uflag  ||  xflag  )  { 
usageError  (  )  ; 
return   false  ; 
} 
tflag  =  true  ; 
break  ; 
case  'M'  : 
Mflag  =  true  ; 
break  ; 
case  'v'  : 
vflag  =  true  ; 
break  ; 
case  'f'  : 
fname  =  args  [  count  ++  ]  ; 
break  ; 
case  'm'  : 
mname  =  args  [  count  ++  ]  ; 
break  ; 
case  '0'  : 
flag0  =  true  ; 
break  ; 
case  'i'  : 
rootjar  =  args  [  count  ++  ]  ; 
iflag  =  true  ; 
break  ; 
case  'e'  : 
ename  =  args  [  count  ++  ]  ; 
break  ; 
default  : 
error  (  formatMsg  (  "error.illegal.option"  ,  String  .  valueOf  (  flags  .  charAt  (  i  )  )  )  )  ; 
usageError  (  )  ; 
return   false  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
usageError  (  )  ; 
return   false  ; 
} 
if  (  !  cflag  &&  !  tflag  &&  !  xflag  &&  !  uflag  &&  !  iflag  )  { 
error  (  getMsg  (  "error.bad.option"  )  )  ; 
usageError  (  )  ; 
return   false  ; 
} 
int   n  =  args  .  length  -  count  ; 
if  (  n  >  0  )  { 
int   k  =  0  ; 
String  [  ]  nameBuf  =  new   String  [  n  ]  ; 
try  { 
for  (  int   i  =  count  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  equals  (  "-C"  )  )  { 
String   dir  =  args  [  ++  i  ]  ; 
dir  =  (  dir  .  endsWith  (  File  .  separator  )  ?  dir  :  (  dir  +  File  .  separator  )  )  ; 
dir  =  dir  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
while  (  dir  .  indexOf  (  "//"  )  >  -  1  )  { 
dir  =  dir  .  replace  (  "//"  ,  "/"  )  ; 
} 
paths  .  add  (  dir  .  replace  (  File  .  separatorChar  ,  '/'  )  )  ; 
nameBuf  [  k  ++  ]  =  dir  +  args  [  ++  i  ]  ; 
}  else  { 
nameBuf  [  k  ++  ]  =  args  [  i  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
usageError  (  )  ; 
return   false  ; 
} 
files  =  new   String  [  k  ]  ; 
System  .  arraycopy  (  nameBuf  ,  0  ,  files  ,  0  ,  k  )  ; 
}  else   if  (  cflag  &&  (  mname  ==  null  )  )  { 
error  (  getMsg  (  "error.bad.cflag"  )  )  ; 
usageError  (  )  ; 
return   false  ; 
}  else   if  (  uflag  )  { 
if  (  (  mname  !=  null  )  ||  (  ename  !=  null  )  )  { 
return   true  ; 
}  else  { 
error  (  getMsg  (  "error.bad.uflag"  )  )  ; 
usageError  (  )  ; 
return   false  ; 
} 
} 
return   true  ; 
} 





void   expand  (  File   dir  ,  String  [  ]  files  ,  boolean   isUpdate  )  { 
if  (  files  ==  null  )  { 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   f  ; 
if  (  dir  ==  null  )  { 
f  =  new   File  (  files  [  i  ]  )  ; 
}  else  { 
f  =  new   File  (  dir  ,  files  [  i  ]  )  ; 
} 
if  (  f  .  isFile  (  )  )  { 
if  (  entries  .  add  (  f  )  )  { 
if  (  isUpdate  )  entryMap  .  put  (  entryName  (  f  .  getPath  (  )  )  ,  f  )  ; 
} 
}  else   if  (  f  .  isDirectory  (  )  )  { 
if  (  entries  .  add  (  f  )  )  { 
if  (  isUpdate  )  { 
String   dirPath  =  f  .  getPath  (  )  ; 
dirPath  =  (  dirPath  .  endsWith  (  File  .  separator  )  )  ?  dirPath  :  (  dirPath  +  File  .  separator  )  ; 
entryMap  .  put  (  entryName  (  dirPath  )  ,  f  )  ; 
} 
expand  (  f  ,  f  .  list  (  )  ,  isUpdate  )  ; 
} 
}  else  { 
error  (  formatMsg  (  "error.nosuch.fileordir"  ,  String  .  valueOf  (  f  )  )  )  ; 
ok  =  false  ; 
} 
} 
} 




void   create  (  OutputStream   out  ,  Manifest   manifest  )  throws   IOException  { 
ZipOutputStream   zos  =  new   JarOutputStream  (  out  )  ; 
if  (  flag0  )  { 
zos  .  setMethod  (  ZipOutputStream  .  STORED  )  ; 
} 
if  (  manifest  !=  null  )  { 
if  (  vflag  )  { 
output  (  getMsg  (  "out.added.manifest"  )  )  ; 
} 
ZipEntry   e  =  new   ZipEntry  (  MANIFEST_DIR  )  ; 
e  .  setTime  (  System  .  currentTimeMillis  (  )  )  ; 
e  .  setSize  (  0  )  ; 
e  .  setCrc  (  0  )  ; 
zos  .  putNextEntry  (  e  )  ; 
e  =  new   ZipEntry  (  MANIFEST_NAME  )  ; 
e  .  setTime  (  System  .  currentTimeMillis  (  )  )  ; 
if  (  flag0  )  { 
crc32Manifest  (  e  ,  manifest  )  ; 
} 
zos  .  putNextEntry  (  e  )  ; 
manifest  .  write  (  zos  )  ; 
zos  .  closeEntry  (  )  ; 
} 
for  (  File   file  :  entries  )  { 
addFile  (  zos  ,  file  )  ; 
} 
zos  .  close  (  )  ; 
} 

private   char   toUpperCaseASCII  (  char   c  )  { 
return  (  c  <  'a'  ||  c  >  'z'  )  ?  c  :  (  char  )  (  c  +  'A'  -  'a'  )  ; 
} 







private   boolean   equalsIgnoreCase  (  String   s  ,  String   upper  )  { 
assert   upper  .  toUpperCase  (  java  .  util  .  Locale  .  ENGLISH  )  .  equals  (  upper  )  ; 
int   len  ; 
if  (  (  len  =  s  .  length  (  )  )  !=  upper  .  length  (  )  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
char   c1  =  s  .  charAt  (  i  )  ; 
char   c2  =  upper  .  charAt  (  i  )  ; 
if  (  c1  !=  c2  &&  toUpperCaseASCII  (  c1  )  !=  c2  )  return   false  ; 
} 
return   true  ; 
} 




boolean   update  (  InputStream   in  ,  OutputStream   out  ,  InputStream   newManifest  ,  JarIndex   jarIndex  )  throws   IOException  { 
ZipInputStream   zis  =  new   ZipInputStream  (  in  )  ; 
ZipOutputStream   zos  =  new   JarOutputStream  (  out  )  ; 
ZipEntry   e  =  null  ; 
boolean   foundManifest  =  false  ; 
boolean   updateOk  =  true  ; 
if  (  jarIndex  !=  null  )  { 
addIndex  (  jarIndex  ,  zos  )  ; 
} 
while  (  (  e  =  zis  .  getNextEntry  (  )  )  !=  null  )  { 
String   name  =  e  .  getName  (  )  ; 
boolean   isManifestEntry  =  equalsIgnoreCase  (  name  ,  MANIFEST_NAME  )  ; 
if  (  (  jarIndex  !=  null  &&  equalsIgnoreCase  (  name  ,  INDEX_NAME  )  )  ||  (  Mflag  &&  isManifestEntry  )  )  { 
continue  ; 
}  else   if  (  isManifestEntry  &&  (  (  newManifest  !=  null  )  ||  (  ename  !=  null  )  )  )  { 
foundManifest  =  true  ; 
if  (  newManifest  !=  null  )  { 
FileInputStream   fis  =  new   FileInputStream  (  mname  )  ; 
boolean   ambiguous  =  isAmbiguousMainClass  (  new   Manifest  (  fis  )  )  ; 
fis  .  close  (  )  ; 
if  (  ambiguous  )  { 
return   false  ; 
} 
} 
Manifest   old  =  new   Manifest  (  zis  )  ; 
if  (  newManifest  !=  null  )  { 
old  .  read  (  newManifest  )  ; 
} 
updateManifest  (  old  ,  zos  )  ; 
}  else  { 
if  (  !  entryMap  .  containsKey  (  name  )  )  { 
ZipEntry   e2  =  new   ZipEntry  (  name  )  ; 
e2  .  setMethod  (  e  .  getMethod  (  )  )  ; 
e2  .  setTime  (  e  .  getTime  (  )  )  ; 
e2  .  setComment  (  e  .  getComment  (  )  )  ; 
e2  .  setExtra  (  e  .  getExtra  (  )  )  ; 
if  (  e  .  getMethod  (  )  ==  ZipEntry  .  STORED  )  { 
e2  .  setSize  (  e  .  getSize  (  )  )  ; 
e2  .  setCrc  (  e  .  getCrc  (  )  )  ; 
} 
zos  .  putNextEntry  (  e2  )  ; 
copy  (  zis  ,  zos  )  ; 
}  else  { 
File   f  =  entryMap  .  get  (  name  )  ; 
addFile  (  zos  ,  f  )  ; 
entryMap  .  remove  (  name  )  ; 
entries  .  remove  (  f  )  ; 
} 
} 
} 
for  (  File   f  :  entries  )  { 
addFile  (  zos  ,  f  )  ; 
} 
if  (  !  foundManifest  )  { 
if  (  newManifest  !=  null  )  { 
Manifest   m  =  new   Manifest  (  newManifest  )  ; 
updateOk  =  !  isAmbiguousMainClass  (  m  )  ; 
if  (  updateOk  )  { 
updateManifest  (  m  ,  zos  )  ; 
} 
}  else   if  (  ename  !=  null  )  { 
updateManifest  (  new   Manifest  (  )  ,  zos  )  ; 
} 
} 
zis  .  close  (  )  ; 
zos  .  close  (  )  ; 
return   updateOk  ; 
} 

private   void   addIndex  (  JarIndex   index  ,  ZipOutputStream   zos  )  throws   IOException  { 
ZipEntry   e  =  new   ZipEntry  (  INDEX_NAME  )  ; 
e  .  setTime  (  System  .  currentTimeMillis  (  )  )  ; 
if  (  flag0  )  { 
CRC32OutputStream   os  =  new   CRC32OutputStream  (  )  ; 
index  .  write  (  os  )  ; 
os  .  updateEntry  (  e  )  ; 
} 
zos  .  putNextEntry  (  e  )  ; 
index  .  write  (  zos  )  ; 
zos  .  closeEntry  (  )  ; 
} 

private   void   updateManifest  (  Manifest   m  ,  ZipOutputStream   zos  )  throws   IOException  { 
addVersion  (  m  )  ; 
addCreatedBy  (  m  )  ; 
if  (  ename  !=  null  )  { 
addMainClass  (  m  ,  ename  )  ; 
} 
ZipEntry   e  =  new   ZipEntry  (  MANIFEST_NAME  )  ; 
e  .  setTime  (  System  .  currentTimeMillis  (  )  )  ; 
if  (  flag0  )  { 
crc32Manifest  (  e  ,  m  )  ; 
} 
zos  .  putNextEntry  (  e  )  ; 
m  .  write  (  zos  )  ; 
if  (  vflag  )  { 
output  (  getMsg  (  "out.update.manifest"  )  )  ; 
} 
} 

private   String   entryName  (  String   name  )  { 
name  =  name  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
String   matchPath  =  ""  ; 
for  (  String   path  :  paths  )  { 
if  (  name  .  startsWith  (  path  )  &&  (  path  .  length  (  )  >  matchPath  .  length  (  )  )  )  { 
matchPath  =  path  ; 
} 
} 
name  =  name  .  substring  (  matchPath  .  length  (  )  )  ; 
if  (  name  .  startsWith  (  "/"  )  )  { 
name  =  name  .  substring  (  1  )  ; 
}  else   if  (  name  .  startsWith  (  "./"  )  )  { 
name  =  name  .  substring  (  2  )  ; 
} 
return   name  ; 
} 

private   void   addVersion  (  Manifest   m  )  { 
Attributes   global  =  m  .  getMainAttributes  (  )  ; 
if  (  global  .  getValue  (  Attributes  .  Name  .  MANIFEST_VERSION  )  ==  null  )  { 
global  .  put  (  Attributes  .  Name  .  MANIFEST_VERSION  ,  VERSION  )  ; 
} 
} 

private   void   addCreatedBy  (  Manifest   m  )  { 
Attributes   global  =  m  .  getMainAttributes  (  )  ; 
if  (  global  .  getValue  (  new   Attributes  .  Name  (  "Created-By"  )  )  ==  null  )  { 
String   javaVendor  =  System  .  getProperty  (  "java.vendor"  )  ; 
String   jdkVersion  =  System  .  getProperty  (  "java.version"  )  ; 
global  .  put  (  new   Attributes  .  Name  (  "Created-By"  )  ,  jdkVersion  +  " ("  +  javaVendor  +  ")"  )  ; 
} 
} 

private   void   addMainClass  (  Manifest   m  ,  String   mainApp  )  { 
Attributes   global  =  m  .  getMainAttributes  (  )  ; 
global  .  put  (  Attributes  .  Name  .  MAIN_CLASS  ,  mainApp  )  ; 
} 

private   boolean   isAmbiguousMainClass  (  Manifest   m  )  { 
if  (  ename  !=  null  )  { 
Attributes   global  =  m  .  getMainAttributes  (  )  ; 
if  (  (  global  .  get  (  Attributes  .  Name  .  MAIN_CLASS  )  !=  null  )  )  { 
error  (  getMsg  (  "error.bad.eflag"  )  )  ; 
usageError  (  )  ; 
return   true  ; 
} 
} 
return   false  ; 
} 




void   addFile  (  ZipOutputStream   zos  ,  File   file  )  throws   IOException  { 
String   name  =  file  .  getPath  (  )  ; 
boolean   isDir  =  file  .  isDirectory  (  )  ; 
if  (  isDir  )  { 
name  =  name  .  endsWith  (  File  .  separator  )  ?  name  :  (  name  +  File  .  separator  )  ; 
} 
name  =  entryName  (  name  )  ; 
if  (  name  .  equals  (  ""  )  ||  name  .  equals  (  "."  )  ||  name  .  equals  (  zname  )  )  { 
return  ; 
}  else   if  (  (  name  .  equals  (  MANIFEST_DIR  )  ||  name  .  equals  (  MANIFEST_NAME  )  )  &&  !  Mflag  )  { 
if  (  vflag  )  { 
output  (  formatMsg  (  "out.ignore.entry"  ,  name  )  )  ; 
} 
return  ; 
} 
long   size  =  isDir  ?  0  :  file  .  length  (  )  ; 
if  (  vflag  )  { 
out  .  print  (  formatMsg  (  "out.adding"  ,  name  )  )  ; 
} 
ZipEntry   e  =  new   ZipEntry  (  name  )  ; 
e  .  setTime  (  file  .  lastModified  (  )  )  ; 
if  (  size  ==  0  )  { 
e  .  setMethod  (  ZipEntry  .  STORED  )  ; 
e  .  setSize  (  0  )  ; 
e  .  setCrc  (  0  )  ; 
}  else   if  (  flag0  )  { 
crc32File  (  e  ,  file  )  ; 
} 
zos  .  putNextEntry  (  e  )  ; 
if  (  !  isDir  )  { 
copy  (  file  ,  zos  )  ; 
} 
zos  .  closeEntry  (  )  ; 
if  (  vflag  )  { 
size  =  e  .  getSize  (  )  ; 
long   csize  =  e  .  getCompressedSize  (  )  ; 
out  .  print  (  formatMsg2  (  "out.size"  ,  String  .  valueOf  (  size  )  ,  String  .  valueOf  (  csize  )  )  )  ; 
if  (  e  .  getMethod  (  )  ==  ZipEntry  .  DEFLATED  )  { 
long   ratio  =  0  ; 
if  (  size  !=  0  )  { 
ratio  =  (  (  size  -  csize  )  *  100  )  /  size  ; 
} 
output  (  formatMsg  (  "out.deflated"  ,  String  .  valueOf  (  ratio  )  )  )  ; 
}  else  { 
output  (  getMsg  (  "out.stored"  )  )  ; 
} 
} 
} 






private   byte  [  ]  copyBuf  =  new   byte  [  8192  ]  ; 









private   void   copy  (  InputStream   from  ,  OutputStream   to  )  throws   IOException  { 
int   n  ; 
while  (  (  n  =  from  .  read  (  copyBuf  )  )  !=  -  1  )  to  .  write  (  copyBuf  ,  0  ,  n  )  ; 
} 









private   void   copy  (  File   from  ,  OutputStream   to  )  throws   IOException  { 
InputStream   in  =  new   FileInputStream  (  from  )  ; 
try  { 
copy  (  in  ,  to  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 









private   void   copy  (  InputStream   from  ,  File   to  )  throws   IOException  { 
OutputStream   out  =  new   FileOutputStream  (  to  )  ; 
try  { 
copy  (  from  ,  out  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
} 





private   void   crc32Manifest  (  ZipEntry   e  ,  Manifest   m  )  throws   IOException  { 
CRC32OutputStream   os  =  new   CRC32OutputStream  (  )  ; 
m  .  write  (  os  )  ; 
os  .  updateEntry  (  e  )  ; 
} 





private   void   crc32File  (  ZipEntry   e  ,  File   f  )  throws   IOException  { 
CRC32OutputStream   os  =  new   CRC32OutputStream  (  )  ; 
copy  (  f  ,  os  )  ; 
if  (  os  .  n  !=  f  .  length  (  )  )  { 
throw   new   JarException  (  formatMsg  (  "error.incorrect.length"  ,  f  .  getPath  (  )  )  )  ; 
} 
os  .  updateEntry  (  e  )  ; 
} 

void   replaceFSC  (  String   files  [  ]  )  { 
if  (  files  !=  null  )  { 
for  (  String   file  :  files  )  { 
file  =  file  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
} 
} 
} 

@  SuppressWarnings  (  "serial"  ) 
Set  <  ZipEntry  >  newDirSet  (  )  { 
return   new   HashSet  <  ZipEntry  >  (  )  { 

public   boolean   add  (  ZipEntry   e  )  { 
return  (  (  e  ==  null  ||  useExtractionTime  )  ?  false  :  super  .  add  (  e  )  )  ; 
} 
}  ; 
} 

void   updateLastModifiedTime  (  Set  <  ZipEntry  >  zes  )  throws   IOException  { 
for  (  ZipEntry   ze  :  zes  )  { 
long   lastModified  =  ze  .  getTime  (  )  ; 
if  (  lastModified  !=  -  1  )  { 
File   f  =  new   File  (  ze  .  getName  (  )  .  replace  (  '/'  ,  File  .  separatorChar  )  )  ; 
f  .  setLastModified  (  lastModified  )  ; 
} 
} 
} 




void   extract  (  InputStream   in  ,  String   files  [  ]  )  throws   IOException  { 
ZipInputStream   zis  =  new   ZipInputStream  (  in  )  ; 
ZipEntry   e  ; 
Set  <  ZipEntry  >  dirs  =  newDirSet  (  )  ; 
while  (  (  e  =  zis  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  files  ==  null  )  { 
dirs  .  add  (  extractFile  (  zis  ,  e  )  )  ; 
}  else  { 
String   name  =  e  .  getName  (  )  ; 
for  (  String   file  :  files  )  { 
if  (  name  .  startsWith  (  file  )  )  { 
dirs  .  add  (  extractFile  (  zis  ,  e  )  )  ; 
break  ; 
} 
} 
} 
} 
updateLastModifiedTime  (  dirs  )  ; 
} 




void   extract  (  String   fname  ,  String   files  [  ]  )  throws   IOException  { 
ZipFile   zf  =  new   ZipFile  (  fname  )  ; 
Set  <  ZipEntry  >  dirs  =  newDirSet  (  )  ; 
Enumeration  <  ?  extends   ZipEntry  >  zes  =  zf  .  entries  (  )  ; 
while  (  zes  .  hasMoreElements  (  )  )  { 
ZipEntry   e  =  zes  .  nextElement  (  )  ; 
InputStream   is  ; 
if  (  files  ==  null  )  { 
dirs  .  add  (  extractFile  (  zf  .  getInputStream  (  e  )  ,  e  )  )  ; 
}  else  { 
String   name  =  e  .  getName  (  )  ; 
for  (  String   file  :  files  )  { 
if  (  name  .  startsWith  (  file  )  )  { 
dirs  .  add  (  extractFile  (  zf  .  getInputStream  (  e  )  ,  e  )  )  ; 
break  ; 
} 
} 
} 
} 
zf  .  close  (  )  ; 
updateLastModifiedTime  (  dirs  )  ; 
} 






ZipEntry   extractFile  (  InputStream   is  ,  ZipEntry   e  )  throws   IOException  { 
ZipEntry   rc  =  null  ; 
String   name  =  e  .  getName  (  )  ; 
File   f  =  new   File  (  e  .  getName  (  )  .  replace  (  '/'  ,  File  .  separatorChar  )  )  ; 
if  (  e  .  isDirectory  (  )  )  { 
if  (  f  .  exists  (  )  )  { 
if  (  !  f  .  isDirectory  (  )  )  { 
throw   new   IOException  (  formatMsg  (  "error.create.dir"  ,  f  .  getPath  (  )  )  )  ; 
} 
}  else  { 
if  (  !  f  .  mkdirs  (  )  )  { 
throw   new   IOException  (  formatMsg  (  "error.create.dir"  ,  f  .  getPath  (  )  )  )  ; 
}  else  { 
rc  =  e  ; 
} 
} 
if  (  vflag  )  { 
output  (  formatMsg  (  "out.create"  ,  name  )  )  ; 
} 
}  else  { 
if  (  f  .  getParent  (  )  !=  null  )  { 
File   d  =  new   File  (  f  .  getParent  (  )  )  ; 
if  (  !  d  .  exists  (  )  &&  !  d  .  mkdirs  (  )  ||  !  d  .  isDirectory  (  )  )  { 
throw   new   IOException  (  formatMsg  (  "error.create.dir"  ,  d  .  getPath  (  )  )  )  ; 
} 
} 
try  { 
copy  (  is  ,  f  )  ; 
}  finally  { 
if  (  is   instanceof   ZipInputStream  )  (  (  ZipInputStream  )  is  )  .  closeEntry  (  )  ;  else   is  .  close  (  )  ; 
} 
if  (  vflag  )  { 
if  (  e  .  getMethod  (  )  ==  ZipEntry  .  DEFLATED  )  { 
output  (  formatMsg  (  "out.inflated"  ,  name  )  )  ; 
}  else  { 
output  (  formatMsg  (  "out.extracted"  ,  name  )  )  ; 
} 
} 
} 
if  (  !  useExtractionTime  )  { 
long   lastModified  =  e  .  getTime  (  )  ; 
if  (  lastModified  !=  -  1  )  { 
f  .  setLastModified  (  lastModified  )  ; 
} 
} 
return   rc  ; 
} 




void   list  (  InputStream   in  ,  String   files  [  ]  )  throws   IOException  { 
ZipInputStream   zis  =  new   ZipInputStream  (  in  )  ; 
ZipEntry   e  ; 
while  (  (  e  =  zis  .  getNextEntry  (  )  )  !=  null  )  { 
zis  .  closeEntry  (  )  ; 
printEntry  (  e  ,  files  )  ; 
} 
} 




void   list  (  String   fname  ,  String   files  [  ]  )  throws   IOException  { 
ZipFile   zf  =  new   ZipFile  (  fname  )  ; 
Enumeration  <  ?  extends   ZipEntry  >  zes  =  zf  .  entries  (  )  ; 
while  (  zes  .  hasMoreElements  (  )  )  { 
printEntry  (  zes  .  nextElement  (  )  ,  files  )  ; 
} 
zf  .  close  (  )  ; 
} 





void   dumpIndex  (  String   rootjar  ,  JarIndex   index  )  throws   IOException  { 
File   jarFile  =  new   File  (  rootjar  )  ; 
Path   jarPath  =  jarFile  .  toPath  (  )  ; 
Path   tmpPath  =  createTempFileInSameDirectoryAs  (  jarFile  )  .  toPath  (  )  ; 
try  { 
if  (  update  (  jarPath  .  newInputStream  (  )  ,  tmpPath  .  newOutputStream  (  )  ,  null  ,  index  )  )  { 
try  { 
tmpPath  .  moveTo  (  jarPath  ,  REPLACE_EXISTING  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  getMsg  (  "error.write.file"  )  ,  e  )  ; 
} 
} 
}  finally  { 
tmpPath  .  deleteIfExists  (  )  ; 
} 
} 

private   HashSet  <  String  >  jarPaths  =  new   HashSet  <  String  >  (  )  ; 





List  <  String  >  getJarPath  (  String   jar  )  throws   IOException  { 
List  <  String  >  files  =  new   ArrayList  <  String  >  (  )  ; 
files  .  add  (  jar  )  ; 
jarPaths  .  add  (  jar  )  ; 
String   path  =  jar  .  substring  (  0  ,  Math  .  max  (  0  ,  jar  .  lastIndexOf  (  '/'  )  +  1  )  )  ; 
JarFile   rf  =  new   JarFile  (  jar  .  replace  (  '/'  ,  File  .  separatorChar  )  )  ; 
if  (  rf  !=  null  )  { 
Manifest   man  =  rf  .  getManifest  (  )  ; 
if  (  man  !=  null  )  { 
Attributes   attr  =  man  .  getMainAttributes  (  )  ; 
if  (  attr  !=  null  )  { 
String   value  =  attr  .  getValue  (  Attributes  .  Name  .  CLASS_PATH  )  ; 
if  (  value  !=  null  )  { 
StringTokenizer   st  =  new   StringTokenizer  (  value  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   ajar  =  st  .  nextToken  (  )  ; 
if  (  !  ajar  .  endsWith  (  "/"  )  )  { 
ajar  =  path  .  concat  (  ajar  )  ; 
if  (  !  jarPaths  .  contains  (  ajar  )  )  { 
files  .  addAll  (  getJarPath  (  ajar  )  )  ; 
} 
} 
} 
} 
} 
} 
} 
rf  .  close  (  )  ; 
return   files  ; 
} 




void   genIndex  (  String   rootjar  ,  String  [  ]  files  )  throws   IOException  { 
List  <  String  >  jars  =  getJarPath  (  rootjar  )  ; 
int   njars  =  jars  .  size  (  )  ; 
String  [  ]  jarfiles  ; 
if  (  njars  ==  1  &&  files  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
jars  .  addAll  (  getJarPath  (  files  [  i  ]  )  )  ; 
} 
njars  =  jars  .  size  (  )  ; 
} 
jarfiles  =  jars  .  toArray  (  new   String  [  njars  ]  )  ; 
JarIndex   index  =  new   JarIndex  (  jarfiles  )  ; 
dumpIndex  (  rootjar  ,  index  )  ; 
} 




void   printEntry  (  ZipEntry   e  ,  String  [  ]  files  )  throws   IOException  { 
if  (  files  ==  null  )  { 
printEntry  (  e  )  ; 
}  else  { 
String   name  =  e  .  getName  (  )  ; 
for  (  String   file  :  files  )  { 
if  (  name  .  startsWith  (  file  )  )  { 
printEntry  (  e  )  ; 
return  ; 
} 
} 
} 
} 




void   printEntry  (  ZipEntry   e  )  throws   IOException  { 
if  (  vflag  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
String   s  =  Long  .  toString  (  e  .  getSize  (  )  )  ; 
for  (  int   i  =  6  -  s  .  length  (  )  ;  i  >  0  ;  --  i  )  { 
sb  .  append  (  ' '  )  ; 
} 
sb  .  append  (  s  )  .  append  (  ' '  )  .  append  (  new   Date  (  e  .  getTime  (  )  )  .  toString  (  )  )  ; 
sb  .  append  (  ' '  )  .  append  (  e  .  getName  (  )  )  ; 
output  (  sb  .  toString  (  )  )  ; 
}  else  { 
output  (  e  .  getName  (  )  )  ; 
} 
} 




void   usageError  (  )  { 
error  (  getMsg  (  "usage"  )  )  ; 
} 




void   fatalError  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 





void   fatalError  (  String   s  )  { 
error  (  program  +  ": "  +  s  )  ; 
} 




protected   void   output  (  String   s  )  { 
out  .  println  (  s  )  ; 
} 




protected   void   error  (  String   s  )  { 
err  .  println  (  s  )  ; 
} 




public   static   void   main  (  String   args  [  ]  )  { 
Main   jartool  =  new   Main  (  System  .  out  ,  System  .  err  ,  "jar"  )  ; 
System  .  exit  (  jartool  .  run  (  args  )  ?  0  :  1  )  ; 
} 






private   static   class   CRC32OutputStream   extends   java  .  io  .  OutputStream  { 

final   CRC32   crc  =  new   CRC32  (  )  ; 

long   n  =  0  ; 

CRC32OutputStream  (  )  { 
} 

public   void   write  (  int   r  )  throws   IOException  { 
crc  .  update  (  r  )  ; 
n  ++  ; 
} 

public   void   write  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
crc  .  update  (  b  ,  off  ,  len  )  ; 
n  +=  len  ; 
} 





public   void   updateEntry  (  ZipEntry   e  )  { 
e  .  setMethod  (  ZipEntry  .  STORED  )  ; 
e  .  setSize  (  n  )  ; 
e  .  setCrc  (  crc  .  getValue  (  )  )  ; 
} 
} 
} 

