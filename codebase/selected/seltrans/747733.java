package   be  .  ac  .  fundp  .  infonet  .  econf  .  producer  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  zip  .  *  ; 
import   be  .  ac  .  fundp  .  infonet  .  econf  .  *  ; 
import   be  .  ac  .  fundp  .  infonet  .  econf  .  util  .  *  ; 
import   be  .  ac  .  fundp  .  infonet  .  econf  .  resource  .  *  ; 






public   class   Archive  { 




private   static   org  .  apache  .  log4j  .  Category   m_logCat  =  org  .  apache  .  log4j  .  Category  .  getInstance  (  Archive  .  class  .  getName  (  )  )  ; 




private   File   archiveFile  =  null  ; 




private   ZipOutputStream   out  =  null  ; 




private   Vector   roots  =  null  ; 




private   String   soundFilePath  =  null  ; 




private   boolean   empty  =  true  ; 






public   Archive  (  String   archivePath  )  throws   IllegalStateException  { 
if  (  archivePath  ==  null  )  { 
throw   new   IllegalStateException  (  "null value for the archive path not allowed!"  )  ; 
} 
archiveFile  =  new   File  (  archivePath  )  ; 
if  (  archiveFile  .  exists  (  )  )  { 
throw   new   IllegalStateException  (  "Archive file : "  +  archivePath  +  " exists already!"  )  ; 
} 
roots  =  new   Vector  (  )  ; 
} 




public   void   init  (  )  { 
try  { 
if  (  out  ==  null  )  if  (  empty  )  out  =  new   ZipOutputStream  (  new   FileOutputStream  (  archiveFile  )  )  ;  else   load  (  )  ; 
}  catch  (  IOException   ioe  )  { 
m_logCat  .  error  (  "Cannot init archive: "  +  archiveFile  ,  ioe  )  ; 
} 
} 




public   void   close  (  )  { 
try  { 
if  (  out  !=  null  )  { 
out  .  close  (  )  ; 
out  =  null  ; 
} 
}  catch  (  IOException   ioe  )  { 
m_logCat  .  error  (  "Cannot close archive: "  +  archiveFile  ,  ioe  )  ; 
} 
} 




private   void   load  (  )  { 
File   backFile  =  null  ; 
ZipFile   zipFile  =  null  ; 
Enumeration   zippedFiles  =  null  ; 
ZipEntry   currEntry  =  null  ; 
ZipEntry   entry  =  null  ; 
try  { 
String   oldName  =  archiveFile  .  toString  (  )  +  ".bak"  ; 
archiveFile  .  renameTo  (  new   File  (  oldName  )  )  ; 
backFile  =  new   File  (  archiveFile  .  toString  (  )  +  ".bak"  )  ; 
zipFile  =  new   ZipFile  (  backFile  .  getAbsolutePath  (  )  )  ; 
zippedFiles  =  zipFile  .  entries  (  )  ; 
out  =  new   ZipOutputStream  (  new   FileOutputStream  (  archiveFile  )  )  ; 
long   presentTime  =  Calendar  .  getInstance  (  )  .  getTime  (  )  .  getTime  (  )  ; 
out  .  setMethod  (  out  .  DEFLATED  )  ; 
while  (  zippedFiles  .  hasMoreElements  (  )  )  { 
currEntry  =  (  ZipEntry  )  zippedFiles  .  nextElement  (  )  ; 
BufferedInputStream   reader  =  new   BufferedInputStream  (  zipFile  .  getInputStream  (  currEntry  )  )  ; 
int   b  ; 
out  .  putNextEntry  (  new   ZipEntry  (  currEntry  .  getName  (  )  )  )  ; 
while  (  (  b  =  reader  .  read  (  )  )  !=  -  1  )  out  .  write  (  b  )  ; 
reader  .  close  (  )  ; 
out  .  flush  (  )  ; 
out  .  closeEntry  (  )  ; 
} 
zipFile  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
m_logCat  .  error  (  "Cannot load zip file"  ,  e  )  ; 
} 
} 








public   void   add  (  File   f  ,  String   archivePath  )  { 
if  (  out  ==  null  )  init  (  )  ; 
empty  =  false  ; 
m_logCat  .  info  (  "Adding "  +  f  +  " in "  +  archivePath  )  ; 
int   b  =  0  ; 
File   currentFilePath  =  new   File  (  archivePath  )  ; 
try  { 
out  .  putNextEntry  (  new   ZipEntry  (  archivePath  )  )  ; 
BufferedInputStream   cacheIn  =  new   BufferedInputStream  (  new   FileInputStream  (  f  )  )  ; 
while  (  (  b  =  cacheIn  .  read  (  )  )  !=  -  1  )  out  .  write  (  b  )  ; 
cacheIn  .  close  (  )  ; 
out  .  closeEntry  (  )  ; 
}  catch  (  ZipException   ze  )  { 
String   s  =  ze  .  getMessage  (  )  ; 
if  (  s  .  indexOf  (  "duplicate entry"  )  ==  -  1  )  { 
m_logCat  .  error  (  "I/O error "  ,  ze  )  ; 
} 
}  catch  (  FileNotFoundException   fe  )  { 
m_logCat  .  error  (  "File not found "  ,  fe  )  ; 
}  catch  (  IOException   e  )  { 
m_logCat  .  error  (  "I/O error "  +  e  .  getMessage  (  )  )  ; 
} 
} 




public   File   getArchive  (  )  { 
return   archiveFile  ; 
} 




public   Vector   getRootFiles  (  )  { 
return   roots  ; 
} 






public   void   addRoot  (  Slide   root  )  { 
roots  .  add  (  root  )  ; 
} 









public   void   addSoundFile  (  File   soundFile  ,  String   archivePath  )  { 
add  (  soundFile  ,  archivePath  )  ; 
soundFilePath  =  archivePath  .  replace  (  '\\'  ,  '/'  )  ; 
} 




public   String   getSoundFilePath  (  )  { 
return   soundFilePath  ; 
} 






public   Archive   clone  (  String   newArchivePath  )  throws   IllegalStateException  { 
if  (  out  !=  null  )  { 
try  { 
out  .  close  (  )  ; 
out  =  null  ; 
}  catch  (  IOException   ioe  )  { 
m_logCat  .  error  (  "Cannot close archive: "  +  archiveFile  ,  ioe  )  ; 
} 
} 
Archive   res  =  new   Archive  (  newArchivePath  )  ; 
res  .  roots  =  roots  ; 
res  .  empty  =  empty  ; 
res  .  soundFilePath  =  soundFilePath  ; 
Utilities  .  copyFile  (  archiveFile  ,  res  .  getArchive  (  )  )  ; 
return   res  ; 
} 




public   void   initSlideList  (  HTTPRootFile   f  )  { 
m_logCat  .  info  (  "Init slide list with a blank content at http://blank/"  )  ; 
long   msec  =  f  .  getRequestedTime  (  )  .  getTime  (  )  -  SessionManager  .  getSessionStartTime  (  )  .  getTime  (  )  ; 
m_logCat  .  info  (  "Blank content has "  +  msec  +  "msec"  )  ; 
Slide   s  =  new   Slide  (  "blank.html"  ,  msec  ,  "ms"  )  ; 
addRoot  (  s  )  ; 
} 
} 

