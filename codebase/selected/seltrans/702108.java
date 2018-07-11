package   org  .  bungeni  .  odfdom  .  utils  ; 

import   org  .  odftoolkit  .  odfdom  .  pkg  .  OdfPackage  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 






public   class   BungeniOdfFileCopy  { 

private   static   org  .  apache  .  log4j  .  Logger   log  =  org  .  apache  .  log4j  .  Logger  .  getLogger  (  BungeniOdfFileCopy  .  class  .  getName  (  )  )  ; 

private   OdfPackage   odfPackage  ; 

public   BungeniOdfFileCopy  (  OdfPackage   pkg  )  { 
this  .  odfPackage  =  pkg  ; 
} 







public   void   copyFile  (  File   toFile  )  throws   IOException  { 
File   origFile  =  getPackageFile  (  )  ; 
copyFile  (  origFile  ,  toFile  )  ; 
} 








public   static   void   copyFile  (  File   sourceFile  ,  File   destFile  )  throws   IOException  { 
if  (  !  destFile  .  exists  (  )  )  { 
destFile  .  createNewFile  (  )  ; 
} 
FileChannel   source  =  null  ; 
FileChannel   destination  =  null  ; 
try  { 
source  =  new   FileInputStream  (  sourceFile  )  .  getChannel  (  )  ; 
destination  =  new   FileOutputStream  (  destFile  )  .  getChannel  (  )  ; 
destination  .  transferFrom  (  source  ,  0  ,  source  .  size  (  )  )  ; 
}  finally  { 
if  (  source  !=  null  )  { 
source  .  close  (  )  ; 
} 
if  (  destination  !=  null  )  { 
destination  .  close  (  )  ; 
} 
} 
} 





private   File   getPackageFile  (  )  { 
File   origFile  =  null  ; 
try  { 
URI   pkgURI  =  new   URI  (  odfPackage  .  getBaseURI  (  )  )  ; 
origFile  =  new   File  (  pkgURI  )  ; 
}  catch  (  URISyntaxException   ex  )  { 
log  .  error  (  "getPackageFile : "  +  ex  .  getMessage  (  )  )  ; 
}  finally  { 
return   origFile  ; 
} 
} 





public   File   copyTo  (  String   stringSuffix  ,  boolean   bOverWrite  )  { 
File   backupFile  =  null  ; 
try  { 
File   origFile  =  getPackageFile  (  )  ; 
String   fileName  =  origFile  .  getName  (  )  ; 
String   fileDir  =  origFile  .  getParentFile  (  )  .  getPath  (  )  ; 
String   newFileName  =  fileName  .  substring  (  0  ,  fileName  .  indexOf  (  ".odt"  )  )  +  stringSuffix  +  ".odt"  ; 
backupFile  =  new   File  (  fileDir  +  File  .  separator  +  newFileName  )  ; 
copyFile  (  origFile  ,  backupFile  )  ; 
}  catch  (  IOException   ex  )  { 
log  .  error  (  "OdfPackageBackup "  +  ex  .  getMessage  (  )  )  ; 
}  finally  { 
return   backupFile  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
BungeniOdfFileCopy   bkp  =  new   BungeniOdfFileCopy  (  OdfPackage  .  loadPackage  (  "/home/undesa/test.odt"  )  )  ; 
bkp  .  copyTo  (  "_plain"  ,  true  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
} 

