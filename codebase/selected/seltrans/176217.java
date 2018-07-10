package   net  .  sourceforge  .  jbackupfw  .  manager  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  Backup  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  Restore  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  Update  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  data  .  BackUpInfo  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  data  .  BackUpInfoFileGroup  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  data  .  BackupException  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  util  .  ExportData  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  util  .  ExportProperties  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  util  .  ImportData  ; 
import   net  .  sourceforge  .  jbackupfw  .  core  .  util  .  ImportProperties  ; 
import   net  .  sourceforge  .  jbackupfw  .  features  .  scheduling  .  JobScheduler  ; 
import   net  .  sourceforge  .  jbackupfw  .  features  .  scheduling  .  TaskManager  ; 
import   net  .  sourceforge  .  jbackupfw  .  features  .  scheduling  .  data  .  ScheduledJobs  ; 
import   net  .  sourceforge  .  jbackupfw  .  features  .  scheduling  .  util  .  JobWorker  ; 
import   org  .  dom4j  .  DocumentException  ; 






public   class   OperationManager  { 



private   BackUpInfo   backUpInfo  =  null  ; 


private   Backup   backUp  =  null  ; 


private   Update   update  =  null  ; 


private   Restore   restore  =  null  ; 


private   ExportProperties   exportProperties  =  null  ; 


private   ImportProperties   importProperties  =  null  ; 


private   ImportData   importData  =  null  ; 


private   ExportData   exportData  =  null  ; 


private   TaskManager   taskManager  =  null  ; 


private   JobScheduler   jobSchedule  =  null  ; 


private   JobWorker   scheduledJobs  =  null  ; 


private   static   final   byte  [  ]  BUFFER  =  new   byte  [  2156  ]  ; 


private   static   final   String   FILE_INFO  =  "backUpExternalInfo.out"  ; 


private   static   final   String   FOLDER_SCHEDULE  =  "schedule//"  ; 


private   static   final   String   CONFIG_FILE  =  "settings.cfg"  ; 





public   OperationManager  (  )  { 
backUpInfo  =  new   BackUpInfo  (  )  ; 
} 

















public   String   backUp  (  LinkedList  <  String  >  fileBackupList  ,  String   pathTo  ,  String   groupName  ,  String   groupId  )  { 
if  (  !  pathTo  .  endsWith  (  "\\"  )  )  { 
groupId  =  pathTo  .  substring  (  pathTo  .  lastIndexOf  (  "\\"  )  +  1  ,  pathTo  .  lastIndexOf  (  "."  )  )  ; 
pathTo  =  pathTo  .  substring  (  0  ,  pathTo  .  lastIndexOf  (  "\\"  )  +  1  )  ; 
} 
backUp  =  new   Backup  (  groupName  ,  groupId  )  ; 
if  (  !  groupId  .  endsWith  (  ".bk"  )  )  { 
groupId  +=  ".bk"  ; 
} 
File   zipFile  =  new   File  (  pathTo  +  groupId  )  ; 
ZipOutputStream   zos  ; 
try  { 
if  (  zipFile  .  exists  (  )  )  { 
File   tempFile  =  File  .  createTempFile  (  zipFile  .  getName  (  )  ,  null  )  ; 
tempFile  .  delete  (  )  ; 
zipFile  .  renameTo  (  tempFile  )  ; 
ZipInputStream   zis  =  new   ZipInputStream  (  new   FileInputStream  (  tempFile  )  )  ; 
zos  =  new   ZipOutputStream  (  new   FileOutputStream  (  zipFile  )  )  ; 
ZipEntry   entry  =  zis  .  getNextEntry  (  )  ; 
while  (  entry  !=  null  )  { 
if  (  entry  .  getName  (  )  .  equals  (  FILE_INFO  )  )  { 
entry  =  zis  .  getNextEntry  (  )  ; 
continue  ; 
}  else  { 
zos  .  putNextEntry  (  new   ZipEntry  (  entry  .  getName  (  )  )  )  ; 
int   length  ; 
while  (  (  length  =  zis  .  read  (  BUFFER  )  )  >  0  )  { 
zos  .  write  (  BUFFER  ,  0  ,  length  )  ; 
} 
entry  =  zis  .  getNextEntry  (  )  ; 
} 
} 
zis  .  close  (  )  ; 
backUp  .  execute  (  fileBackupList  ,  zos  )  ; 
BackUpInfoFileGroup   fileGroup  =  importData  (  tempFile  )  ; 
for  (  int   i  =  0  ;  i  <  backUp  .  getFileGroup  (  )  .  getFileList  (  )  .  size  (  )  ;  i  ++  )  { 
fileGroup  .  getFileList  (  )  .  add  (  backUp  .  getFileGroup  (  )  .  getFileList  (  )  .  get  (  i  )  )  ; 
} 
fileGroup  .  setSize  (  fileGroup  .  getSize  (  )  +  backUp  .  getFileGroup  (  )  .  getSize  (  )  )  ; 
if  (  exportData  ==  null  )  { 
exportData  =  new   ExportData  (  )  ; 
} 
exportData  .  execute  (  fileGroup  ,  zos  ,  FILE_INFO  )  ; 
}  else  { 
zos  =  new   ZipOutputStream  (  new   FileOutputStream  (  pathTo  +  groupId  )  )  ; 
backUp  .  execute  (  fileBackupList  ,  zos  )  ; 
if  (  exportData  ==  null  )  { 
exportData  =  new   ExportData  (  )  ; 
} 
exportData  .  execute  (  backUp  .  getFileGroup  (  )  ,  zos  ,  FILE_INFO  )  ; 
} 
zos  .  close  (  )  ; 
return   groupId  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   BackupException  (  e  .  getMessage  (  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   BackupException  (  e  .  getMessage  (  )  )  ; 
} 
} 












public   void   restore  (  File   archive  ,  File   outputDir  ,  LinkedList  <  String  >  restoreList  )  { 
if  (  restore  ==  null  )  { 
restore  =  new   Restore  (  )  ; 
} 
restore  .  execute  (  archive  ,  outputDir  ,  importData  (  archive  )  ,  restoreList  )  ; 
} 












public   void   update  (  BackUpInfoFileGroup   backedUpFiles  ,  BackUpInfoFileGroup   updateFiles  ,  String   filePath  )  { 
if  (  update  ==  null  )  { 
update  =  new   Update  (  )  ; 
} 
try  { 
File   zipFile  =  new   File  (  filePath  )  ; 
File   tempFile  =  File  .  createTempFile  (  zipFile  .  getName  (  )  ,  null  )  ; 
tempFile  .  delete  (  )  ; 
zipFile  .  renameTo  (  tempFile  )  ; 
ZipInputStream   zis  =  new   ZipInputStream  (  new   FileInputStream  (  tempFile  )  )  ; 
ZipOutputStream   zos  =  new   ZipOutputStream  (  new   FileOutputStream  (  zipFile  )  )  ; 
ZipEntry   entry  =  zis  .  getNextEntry  (  )  ; 
while  (  entry  !=  null  )  { 
if  (  entry  .  getName  (  )  .  equals  (  FILE_INFO  )  )  { 
entry  =  zis  .  getNextEntry  (  )  ; 
continue  ; 
}  else   if  (  !  update  .  isToUpdate  (  updateFiles  ,  entry  .  getName  (  )  )  )  { 
zos  .  putNextEntry  (  new   ZipEntry  (  entry  .  getName  (  )  )  )  ; 
int   len  ; 
while  (  (  len  =  zis  .  read  (  BUFFER  )  )  >  0  )  { 
zos  .  write  (  BUFFER  ,  0  ,  len  )  ; 
} 
} 
entry  =  zis  .  getNextEntry  (  )  ; 
} 
zis  .  close  (  )  ; 
update  .  execute  (  updateFiles  ,  zos  )  ; 
if  (  exportData  ==  null  )  { 
exportData  =  new   ExportData  (  )  ; 
} 
exportData  .  execute  (  update  .  updateInformationFile  (  backedUpFiles  ,  updateFiles  )  ,  zos  ,  FILE_INFO  )  ; 
zos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   BackupException  (  e  .  getMessage  (  )  )  ; 
} 
} 





public   void   startTaskManager  (  )  { 
String  [  ]  schedule  =  new   File  (  FOLDER_SCHEDULE  )  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  schedule  .  length  ;  i  ++  )  { 
taskManager  =  new   TaskManager  (  this  ,  FOLDER_SCHEDULE  +  schedule  [  i  ]  )  ; 
taskManager  .  run  (  )  ; 
} 
} 




public   void   stopTaskManager  (  )  { 
if  (  taskManager  !=  null  )  { 
taskManager  .  stop  (  )  ; 
} 
} 












public   void   schedualBackUpJob  (  LinkedList  <  String  >  files  ,  String   time  ,  String   baseName  ,  String   groupName  ,  String   pathTo  )  { 
if  (  jobSchedule  ==  null  )  { 
jobSchedule  =  new   JobScheduler  (  )  ; 
} 
String   name  =  FOLDER_SCHEDULE  +  "sc"  +  String  .  valueOf  (  Math  .  random  (  )  )  ; 
jobSchedule  .  executeScheduleBackUp  (  files  ,  time  ,  name  ,  baseName  ,  groupName  ,  pathTo  )  ; 
scheduledJobs  .  addBackUpJob  (  jobSchedule  .  createName  (  name  )  .  substring  (  10  )  ,  files  ,  time  ,  baseName  ,  groupName  ,  pathTo  )  ; 
if  (  taskManager  !=  null  )  { 
taskManager  =  new   TaskManager  (  this  ,  jobSchedule  .  createName  (  name  )  )  ; 
taskManager  .  run  (  )  ; 
} 
} 











public   void   schedualUpdateJob  (  LinkedList  <  String  >  fileId  ,  String   time  ,  String   period  ,  String   path  )  { 
if  (  jobSchedule  ==  null  )  { 
jobSchedule  =  new   JobScheduler  (  )  ; 
} 
String   name  =  FOLDER_SCHEDULE  +  "sc"  +  String  .  valueOf  (  Math  .  random  (  )  )  ; 
jobSchedule  .  executeScheduleUpdate  (  fileId  ,  time  ,  name  ,  period  ,  path  )  ; 
scheduledJobs  .  addUpdateJob  (  jobSchedule  .  createName  (  name  )  .  substring  (  10  )  ,  fileId  ,  time  ,  period  ,  path  )  ; 
if  (  taskManager  !=  null  )  { 
taskManager  =  new   TaskManager  (  this  ,  jobSchedule  .  createName  (  name  )  )  ; 
taskManager  .  run  (  )  ; 
} 
} 






public   void   removeBackUpJob  (  int   i  )  { 
if  (  scheduledJobs  !=  null  )  { 
scheduledJobs  .  removeBackUpJob  (  i  )  ; 
} 
} 






public   void   removeBackUpJob  (  String   jobPath  )  { 
if  (  scheduledJobs  !=  null  )  { 
scheduledJobs  .  removeBackUpJob  (  jobPath  )  ; 
} 
} 






public   void   removeUpdateJob  (  int   i  )  { 
if  (  scheduledJobs  !=  null  )  { 
scheduledJobs  .  removeUpdateJob  (  i  )  ; 
} 
} 






public   void   removeUpdateJob  (  String   jobPath  )  { 
if  (  scheduledJobs  !=  null  )  { 
scheduledJobs  .  removeUpdateJob  (  jobPath  )  ; 
} 
} 









public   ScheduledJobs   extractScheduledJobs  (  )  { 
if  (  scheduledJobs  ==  null  )  { 
scheduledJobs  =  new   JobWorker  (  FOLDER_SCHEDULE  )  ; 
} 
try  { 
return   scheduledJobs  .  execute  (  )  ; 
}  catch  (  DocumentException   e  )  { 
throw   new   BackupException  (  e  .  getMessage  (  )  )  ; 
} 
} 








public   void   exportProperties  (  LinkedList  <  String  >  properties  ,  LinkedList  <  String  >  propertieValue  )  { 
if  (  exportProperties  ==  null  )  { 
exportProperties  =  new   ExportProperties  (  )  ; 
} 
exportProperties  .  execute  (  properties  ,  propertieValue  ,  CONFIG_FILE  )  ; 
} 








public   void   addExternalArchive  (  BackUpInfoFileGroup   archive  )  { 
backUpInfo  .  getBackUpDataBase  (  )  .  add  (  archive  )  ; 
} 







public   void   importDataBase  (  LinkedList  <  File  >  archive  )  { 
if  (  importData  ==  null  )  { 
importData  =  new   ImportData  (  )  ; 
} 
backUpInfo  .  getBackUpDataBase  (  )  .  clear  (  )  ; 
boolean   error  =  false  ; 
for  (  int   i  =  0  ;  i  <  archive  .  size  (  )  ;  i  ++  )  { 
BackUpInfoFileGroup   fileGroup  =  importData  .  execute  (  archive  .  get  (  i  )  )  ; 
if  (  fileGroup  !=  null  )  { 
backUpInfo  .  getBackUpDataBase  (  )  .  add  (  fileGroup  )  ; 
}  else  { 
error  =  true  ; 
} 
} 
if  (  error  )  { 
throw   new   BackupException  (  ""  )  ; 
} 
} 















public   BackUpInfoFileGroup   importData  (  File   archive  )  { 
if  (  importData  ==  null  )  { 
importData  =  new   ImportData  (  )  ; 
} 
return   importData  .  execute  (  archive  )  ; 
} 










public   String   getDefaultbackUpFolder  (  )  throws   DocumentException  { 
if  (  importProperties  ==  null  )  { 
importProperties  =  new   ImportProperties  (  CONFIG_FILE  )  ; 
} 
return   importProperties  .  getDefaultbackUpFolder  (  )  ; 
} 










public   String   getPropertieDataBaseName  (  )  throws   DocumentException  { 
if  (  importProperties  ==  null  )  { 
importProperties  =  new   ImportProperties  (  CONFIG_FILE  )  ; 
} 
return   importProperties  .  getPropertieDataBaseName  (  )  ; 
} 










public   String   getPropertieTaskManagerEnabled  (  )  throws   DocumentException  { 
if  (  importProperties  ==  null  )  { 
importProperties  =  new   ImportProperties  (  CONFIG_FILE  )  ; 
} 
return   importProperties  .  getPropertieTaskManagerEnabled  (  )  ; 
} 










public   String   getPropertieDataBaseFolder  (  )  throws   DocumentException  { 
if  (  importProperties  ==  null  )  { 
importProperties  =  new   ImportProperties  (  CONFIG_FILE  )  ; 
} 
return   importProperties  .  getPropertieDataBaseFolder  (  )  ; 
} 










public   String   getPropertieBackUpNumberOfCopies  (  )  throws   DocumentException  { 
if  (  importProperties  ==  null  )  { 
importProperties  =  new   ImportProperties  (  CONFIG_FILE  )  ; 
} 
return   importProperties  .  getPropertieBackUpNumberOfCopies  (  )  ; 
} 






public   BackUpInfo   getBackUpInfo  (  )  { 
return   backUpInfo  ; 
} 







public   int   getBackUpCounter  (  )  { 
if  (  backUp  ==  null  )  { 
return   0  ; 
} 
return   backUp  .  getCounter  (  )  ; 
} 







public   int   getRestoreCounter  (  )  { 
if  (  restore  ==  null  )  { 
return   0  ; 
} 
return   restore  .  getCounter  (  )  ; 
} 






public   void   setTaskOver  (  boolean   over  )  { 
taskManager  .  setTaskOver  (  over  )  ; 
} 






public   boolean   isTaskOver  (  )  { 
if  (  taskManager  ==  null  )  { 
return   false  ; 
} 
return   taskManager  .  isTaskOver  (  )  ; 
} 
} 

