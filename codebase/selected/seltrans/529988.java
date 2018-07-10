package   oracle  .  toplink  .  essentials  .  internal  .  weaving  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  jar  .  JarEntry  ; 





public   class   StaticWeaveDirectoryOutputHandler   extends   AbstractStaticWeaveOutputHandler  { 

private   URL   source  =  null  ; 

private   URL   target  =  null  ; 






public   StaticWeaveDirectoryOutputHandler  (  URL   source  ,  URL   target  )  { 
this  .  source  =  source  ; 
this  .  target  =  target  ; 
} 






public   void   addDirEntry  (  String   dirPath  )  throws   IOException  { 
File   file  =  new   File  (  this  .  target  .  getPath  (  )  +  File  .  separator  +  dirPath  )  .  getAbsoluteFile  (  )  ; 
if  (  !  file  .  exists  (  )  )  { 
file  .  mkdirs  (  )  ; 
} 
} 







public   void   addEntry  (  JarEntry   targetEntry  ,  byte  [  ]  entryBytes  )  throws   IOException  { 
File   target  =  new   File  (  this  .  target  .  getPath  (  )  +  targetEntry  .  getName  (  )  )  .  getAbsoluteFile  (  )  ; 
if  (  !  target  .  exists  (  )  )  { 
target  .  createNewFile  (  )  ; 
} 
(  new   FileOutputStream  (  target  )  )  .  write  (  entryBytes  )  ; 
} 







public   void   addEntry  (  InputStream   jis  ,  JarEntry   entry  )  throws   IOException  ,  URISyntaxException  { 
File   target  =  new   File  (  this  .  target  .  getPath  (  )  +  entry  .  getName  (  )  )  .  getAbsoluteFile  (  )  ; 
if  (  !  target  .  exists  (  )  )  { 
target  .  createNewFile  (  )  ; 
} 
if  (  (  new   File  (  this  .  source  .  toURI  (  )  )  )  .  isDirectory  (  )  )  { 
File   sourceEntry  =  new   File  (  this  .  source  .  getPath  (  )  +  entry  .  getName  (  )  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  sourceEntry  )  ; 
byte  [  ]  classBytes  =  new   byte  [  fis  .  available  (  )  ]  ; 
fis  .  read  (  classBytes  )  ; 
(  new   FileOutputStream  (  target  )  )  .  write  (  classBytes  )  ; 
}  else  { 
readwriteStreams  (  jis  ,  (  new   FileOutputStream  (  target  )  )  )  ; 
} 
} 
} 

