package   hu  .  akarnokd  .  tools  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilenameFilter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 





public   final   class   BuildJar  { 




private   BuildJar  (  )  { 
} 









static   void   processDirectory  (  String   baseDir  ,  String   currentDir  ,  ZipOutputStream   zout  ,  FilenameFilter   filter  )  throws   IOException  { 
File  [  ]  files  =  new   File  (  currentDir  )  .  listFiles  (  new   FileFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   pathname  )  { 
return  !  pathname  .  isHidden  (  )  ; 
} 
}  )  ; 
if  (  files  !=  null  )  { 
for  (  File   f  :  files  )  { 
if  (  f  .  isDirectory  (  )  )  { 
processDirectory  (  baseDir  ,  f  .  getPath  (  )  ,  zout  ,  filter  )  ; 
}  else  { 
String   fpath  =  f  .  getPath  (  )  ; 
String   fpath2  =  fpath  .  substring  (  baseDir  .  length  (  )  )  ; 
if  (  filter  ==  null  ||  filter  .  accept  (  f  .  getParentFile  (  )  ,  f  .  getName  (  )  )  )  { 
System  .  out  .  printf  (  "Adding %s as %s%n"  ,  fpath  ,  fpath2  )  ; 
ZipEntry   ze  =  new   ZipEntry  (  fpath2  .  replace  (  '\\'  ,  '/'  )  )  ; 
ze  .  setSize  (  f  .  length  (  )  )  ; 
ze  .  setTime  (  f  .  lastModified  (  )  )  ; 
zout  .  putNextEntry  (  ze  )  ; 
zout  .  write  (  IOUtils  .  load  (  f  )  )  ; 
} 
} 
} 
} 
} 






public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
ZipOutputStream   zout  =  null  ; 
String   baseProject  =  "..\\Reactive4Java\\"  ; 
String   targetJar  =  "reactive4java"  ; 
String   version  =  "0.90"  ; 
zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  baseProject  +  targetJar  +  "-"  +  version  +  ".jar"  )  ,  1024  *  1024  )  )  ; 
zout  .  setLevel  (  9  )  ; 
try  { 
processDirectory  (  baseProject  +  ".\\bin\\"  ,  baseProject  +  ".\\bin"  ,  zout  ,  new   FilenameFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   dir  ,  String   name  )  { 
return  !  name  .  equals  (  ".svn"  )  ; 
} 
}  )  ; 
processDirectory  (  baseProject  +  ".\\src\\"  ,  baseProject  +  ".\\src"  ,  zout  ,  new   FilenameFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   dir  ,  String   name  )  { 
return  !  name  .  equals  (  ".svn"  )  ; 
} 
}  )  ; 
addFile  (  "META-INF/MANIFEST.MF"  ,  baseProject  +  "META-INF/MANIFEST.MF"  ,  zout  )  ; 
addFile  (  "LICENSE.txt"  ,  baseProject  +  "LICENSE.txt"  ,  zout  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
} 








static   void   addFile  (  String   entryName  ,  String   file  ,  ZipOutputStream   zout  )  throws   IOException  { 
ZipEntry   mf  =  new   ZipEntry  (  entryName  )  ; 
File   mfm  =  new   File  (  file  )  ; 
mf  .  setSize  (  mfm  .  length  (  )  )  ; 
mf  .  setTime  (  mfm  .  lastModified  (  )  )  ; 
zout  .  putNextEntry  (  mf  )  ; 
zout  .  write  (  IOUtils  .  load  (  mfm  )  )  ; 
} 
} 

