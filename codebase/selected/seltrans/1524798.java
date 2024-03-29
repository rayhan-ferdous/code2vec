package   org  .  bungeni  .  editor  .  section  .  refactor  .  xml  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  bungeni  .  editor  .  section  .  refactor  .  changelog  .  ChangeLog  ; 
import   org  .  openoffice  .  odf  .  pkg  .  OdfPackage  ; 






public   class   OdfPackageBackup  { 

private   static   org  .  apache  .  log4j  .  Logger   log  =  org  .  apache  .  log4j  .  Logger  .  getLogger  (  OdfPackageBackup  .  class  .  getName  (  )  )  ; 

private   OdfPackage   odfPackage  ; 

private   String   generatedDateTimeStamp  =  ""  ; 

public   static   final   String   BACKUP_FILE_NAME_DATE_FORMAT  =  "yyyyMMddhhmm"  ; 

public   static   final   String   BACKUP_FILE_NAME_PREFIX  =  "bk_"  ; 

public   static   final   String   BACKUP_FILE_FOLDER  =  ".backup"  ; 








public   void   copyFile  (  File   sourceFile  ,  File   destFile  )  throws   IOException  { 
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

public   String   dateNow  (  String   dateFormat  )  { 
Calendar   cal  =  Calendar  .  getInstance  (  )  ; 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  dateFormat  )  ; 
return   sdf  .  format  (  cal  .  getTime  (  )  )  ; 
} 

public   OdfPackageBackup  (  OdfPackage   pkg  )  { 
this  .  odfPackage  =  pkg  ; 
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





public   File   generateBackup  (  )  { 
File   backupFile  =  null  ; 
try  { 
File   origFile  =  getPackageFile  (  )  ; 
String   fileName  =  origFile  .  getName  (  )  ; 
String   fileDir  =  origFile  .  getParentFile  (  )  .  getPath  (  )  ; 
this  .  generatedDateTimeStamp  =  dateNow  (  BACKUP_FILE_NAME_DATE_FORMAT  )  ; 
String   pathtoBackupDir  =  fileDir  +  File  .  separator  +  ".backup"  ; 
File   fbackupdir  =  new   File  (  pathtoBackupDir  )  ; 
if  (  !  fbackupdir  .  exists  (  )  )  { 
fbackupdir  .  mkdir  (  )  ; 
} 
backupFile  =  new   File  (  pathtoBackupDir  +  File  .  separator  +  BACKUP_FILE_NAME_PREFIX  +  generatedDateTimeStamp  +  "_"  +  fileName  )  ; 
copyFile  (  origFile  ,  backupFile  )  ; 
generateChangeLog  (  backupFile  )  ; 
}  catch  (  IOException   ex  )  { 
log  .  error  (  "OdfPackageBackup "  +  ex  .  getMessage  (  )  )  ; 
}  finally  { 
return   backupFile  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
OdfPackageBackup   bkp  =  new   OdfPackageBackup  (  OdfPackage  .  loadPackage  (  "/Users/ashok/Desktop/ken_bill_2009_1_10_eng_main.odt"  )  )  ; 
bkp  .  generateBackup  (  )  ; 
}  catch  (  Exception   ex  )  { 
Logger  .  getLogger  (  OdfPackageBackup  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 

private   String   sourceSection  ; 

private   String   targetSection  ; 

private   String   changeType  ; 

private   String   commitLog  ; 








void   updateChangeInfo  (  String   sourceSection  ,  String   targetSection  ,  String   changeType  ,  String   commitLog  )  { 
this  .  sourceSection  =  sourceSection  ; 
this  .  targetSection  =  targetSection  ; 
this  .  changeType  =  changeType  ; 
this  .  commitLog  =  commitLog  ; 
} 






private   boolean   generateChangeLog  (  File   backupFile  )  { 
boolean   bState  =  false  ; 
try  { 
ChangeLog   cl  =  new   ChangeLog  (  backupFile  .  getPath  (  )  ,  changeType  ,  sourceSection  ,  targetSection  ,  "move"  ,  commitLog  ,  generatedDateTimeStamp  )  ; 
cl  .  saveLog  (  )  ; 
bState  =  true  ; 
}  catch  (  Exception   ex  )  { 
log  .  error  (  "generateChangeLog : "  +  ex  .  getMessage  (  )  )  ; 
}  finally  { 
return   bState  ; 
} 
} 
} 

