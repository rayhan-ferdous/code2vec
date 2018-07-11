package   com  .  google  .  gwt  .  util  .  tools  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  LineNumberReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  Map  .  Entry  ; 




public   final   class   Utility  { 

private   static   String   sInstallPath  =  null  ; 





public   static   void   close  (  InputStream   is  )  { 
try  { 
if  (  is  !=  null  )  { 
is  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 





public   static   void   close  (  OutputStream   os  )  { 
try  { 
if  (  os  !=  null  )  { 
os  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 





public   static   void   close  (  RandomAccessFile   f  )  { 
if  (  f  !=  null  )  { 
try  { 
f  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 





public   static   void   close  (  Reader   reader  )  { 
try  { 
if  (  reader  !=  null  )  { 
reader  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 





public   static   void   close  (  Socket   socket  )  { 
try  { 
if  (  socket  !=  null  )  { 
socket  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 





public   static   void   close  (  Writer   writer  )  { 
try  { 
if  (  writer  !=  null  )  { 
writer  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 









public   static   File   createNormalFile  (  File   parent  ,  String   fileName  ,  boolean   overwrite  ,  boolean   ignore  )  throws   IOException  { 
File   file  =  new   File  (  parent  ,  fileName  )  ; 
if  (  file  .  createNewFile  (  )  )  { 
System  .  out  .  println  (  "Created file "  +  file  )  ; 
return   file  ; 
} 
if  (  !  file  .  exists  (  )  ||  file  .  isDirectory  (  )  )  { 
throw   new   IOException  (  file  .  getPath  (  )  +  " : could not create normal file."  )  ; 
} 
if  (  ignore  )  { 
System  .  out  .  println  (  file  +  " already exists; skipping"  )  ; 
return   null  ; 
} 
if  (  !  overwrite  )  { 
throw   new   IOException  (  file  .  getPath  (  )  +  " : already exists; please remove it or use the -overwrite or -ignore option."  )  ; 
} 
System  .  out  .  println  (  "Overwriting existing file "  +  file  )  ; 
return   file  ; 
} 








public   static   File   getDirectory  (  File   parent  ,  String   dirName  ,  boolean   create  )  throws   IOException  { 
File   dir  =  new   File  (  parent  ,  dirName  )  ; 
boolean   alreadyExisted  =  dir  .  exists  (  )  ; 
if  (  create  )  { 
dir  .  mkdirs  (  )  ; 
} 
if  (  !  dir  .  exists  (  )  ||  !  dir  .  isDirectory  (  )  )  { 
if  (  create  )  { 
throw   new   IOException  (  dir  .  getPath  (  )  +  " : could not create directory."  )  ; 
}  else  { 
throw   new   IOException  (  dir  .  getPath  (  )  +  " : could not find directory."  )  ; 
} 
} 
if  (  create  &&  !  alreadyExisted  )  { 
System  .  out  .  println  (  "Created directory "  +  dir  )  ; 
} 
return   dir  ; 
} 







public   static   File   getDirectory  (  String   dirPath  ,  boolean   create  )  throws   IOException  { 
return   getDirectory  (  null  ,  dirPath  ,  create  )  ; 
} 











public   static   String   getFileFromClassPath  (  String   partialPath  )  throws   IOException  { 
InputStream   in  =  Utility  .  class  .  getClassLoader  (  )  .  getResourceAsStream  (  partialPath  )  ; 
try  { 
if  (  in  ==  null  )  { 
throw   new   FileNotFoundException  (  partialPath  )  ; 
} 
ByteArrayOutputStream   os  =  new   ByteArrayOutputStream  (  )  ; 
streamOut  (  in  ,  os  ,  1024  )  ; 
return   new   String  (  os  .  toByteArray  (  )  ,  "UTF-8"  )  ; 
}  finally  { 
close  (  in  )  ; 
} 
} 

public   static   String   getInstallPath  (  )  { 
if  (  sInstallPath  ==  null  )  { 
computeInstallationPath  (  )  ; 
} 
return   sInstallPath  ; 
} 











public   static   File   makeTemporaryDirectory  (  File   baseDir  ,  String   prefix  )  throws   IOException  { 
if  (  baseDir  ==  null  )  { 
baseDir  =  new   File  (  System  .  getProperty  (  "java.io.tmpdir"  )  )  ; 
} 
baseDir  .  mkdirs  (  )  ; 
for  (  int   tries  =  0  ;  tries  <  3  ;  ++  tries  )  { 
File   result  =  File  .  createTempFile  (  prefix  ,  null  ,  baseDir  )  ; 
if  (  !  result  .  delete  (  )  )  { 
throw   new   IOException  (  "Couldn't delete temporary file "  +  result  .  getAbsolutePath  (  )  +  " to replace with a directory."  )  ; 
} 
if  (  result  .  mkdirs  (  )  )  { 
return   result  ; 
} 
} 
throw   new   IOException  (  "Couldn't create temporary directory after 3 tries in "  +  baseDir  .  getAbsolutePath  (  )  )  ; 
} 

public   static   void   streamOut  (  File   file  ,  OutputStream   out  ,  int   bufferSize  )  throws   IOException  { 
FileInputStream   fis  =  null  ; 
try  { 
fis  =  new   FileInputStream  (  file  )  ; 
streamOut  (  fis  ,  out  ,  bufferSize  )  ; 
}  finally  { 
com  .  google  .  gwt  .  util  .  tools  .  Utility  .  close  (  fis  )  ; 
} 
} 

public   static   void   streamOut  (  InputStream   in  ,  OutputStream   out  ,  int   bufferSize  )  throws   IOException  { 
assert  (  bufferSize  >=  0  )  ; 
byte  [  ]  buffer  =  new   byte  [  bufferSize  ]  ; 
int   bytesRead  =  0  ; 
while  (  true  )  { 
bytesRead  =  in  .  read  (  buffer  )  ; 
if  (  bytesRead  >=  0  )  { 
out  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
}  else  { 
return  ; 
} 
} 
} 

public   static   void   writeTemplateFile  (  File   file  ,  String   contents  ,  Map  <  String  ,  String  >  replacements  )  throws   IOException  { 
String   replacedContents  =  contents  ; 
Set  <  Entry  <  String  ,  String  >  >  entries  =  replacements  .  entrySet  (  )  ; 
for  (  Iterator  <  Entry  <  String  ,  String  >  >  iter  =  entries  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Entry  <  String  ,  String  >  entry  =  iter  .  next  (  )  ; 
String   replaceThis  =  entry  .  getKey  (  )  ; 
String   withThis  =  entry  .  getValue  (  )  ; 
withThis  =  withThis  .  replaceAll  (  "\\\\"  ,  "\\\\\\\\"  )  ; 
withThis  =  withThis  .  replaceAll  (  "\\$"  ,  "\\\\\\$"  )  ; 
replacedContents  =  replacedContents  .  replaceAll  (  replaceThis  ,  withThis  )  ; 
} 
PrintWriter   pw  =  new   PrintWriter  (  file  )  ; 
LineNumberReader   lnr  =  new   LineNumberReader  (  new   StringReader  (  replacedContents  )  )  ; 
for  (  String   line  =  lnr  .  readLine  (  )  ;  line  !=  null  ;  line  =  lnr  .  readLine  (  )  )  { 
pw  .  println  (  line  )  ; 
} 
close  (  pw  )  ; 
} 

private   static   void   computeInstallationPath  (  )  { 
try  { 
String   override  =  System  .  getProperty  (  "gwt.devjar"  )  ; 
if  (  override  ==  null  )  { 
String   partialPath  =  Utility  .  class  .  getName  (  )  .  replace  (  '.'  ,  '/'  )  .  concat  (  ".class"  )  ; 
URL   url  =  Utility  .  class  .  getClassLoader  (  )  .  getResource  (  partialPath  )  ; 
if  (  url  !=  null  &&  "jar"  .  equals  (  url  .  getProtocol  (  )  )  )  { 
String   path  =  url  .  toString  (  )  ; 
String   jarPath  =  path  .  substring  (  path  .  indexOf  (  "file:"  )  ,  path  .  lastIndexOf  (  '!'  )  )  ; 
File   devJarFile  =  new   File  (  URI  .  create  (  jarPath  )  )  ; 
if  (  !  devJarFile  .  isFile  (  )  )  { 
throw   new   IOException  (  "Could not find jar file; "  +  devJarFile  .  getCanonicalPath  (  )  +  " does not appear to be a valid file"  )  ; 
} 
String   dirPath  =  jarPath  .  substring  (  0  ,  jarPath  .  lastIndexOf  (  '/'  )  +  1  )  ; 
File   installDirFile  =  new   File  (  URI  .  create  (  dirPath  )  )  ; 
if  (  !  installDirFile  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "Could not find installation directory; "  +  installDirFile  .  getCanonicalPath  (  )  +  " does not appear to be a valid directory"  )  ; 
} 
sInstallPath  =  installDirFile  .  getCanonicalPath  (  )  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
}  else  { 
throw   new   IOException  (  "Cannot determine installation directory; apparently not running from a jar"  )  ; 
} 
}  else  { 
override  =  override  .  replace  (  '\\'  ,  '/'  )  ; 
int   pos  =  override  .  lastIndexOf  (  '/'  )  ; 
if  (  pos  <  0  )  { 
sInstallPath  =  ""  ; 
}  else  { 
sInstallPath  =  override  .  substring  (  0  ,  pos  )  ; 
} 
} 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Installation problem detected, please reinstall GWT"  ,  e  )  ; 
} 
} 
} 

