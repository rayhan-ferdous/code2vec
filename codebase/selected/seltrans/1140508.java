package   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  service  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   org  .  apache  .  http  .  HttpEntity  ; 
import   org  .  apache  .  http  .  HttpResponse  ; 
import   org  .  apache  .  http  .  client  .  HttpClient  ; 
import   org  .  apache  .  http  .  client  .  methods  .  HttpGet  ; 
import   org  .  apache  .  http  .  impl  .  client  .  DefaultHttpClient  ; 
import   android  .  app  .  Notification  ; 
import   android  .  app  .  NotificationManager  ; 
import   android  .  app  .  PendingIntent  ; 
import   android  .  app  .  Service  ; 
import   android  .  content  .  Context  ; 
import   android  .  content  .  Intent  ; 
import   android  .  os  .  Environment  ; 
import   android  .  os  .  IBinder  ; 
import   android  .  util  .  Log  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  DictionaryForMIDs  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  FileList  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  InstallDictionary  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  Preferences  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  Preferences  .  DictionaryType  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  R  ; 
import   de  .  kugihan  .  dictionaryformids  .  hmi_android  .  data  .  DownloadDictionaryItem  ; 






public   final   class   DictionaryInstallationService   extends   Service  { 




private   static   final   int   COPY_BUFFER_SIZE  =  1024  ; 




private   static   final   String   PATTERN_DELETE_FROM_PATH  =  "dictionary/"  ; 




private   static   final   String   PATTERN_RETURN_PATH  =  ".*\\.properties"  ; 




private   static   final   String   PATTERN_EXTRACT_PATH  =  "dictionary/.*"  ; 




private   static   final   String   PATTERN_JAR_ARCHIVE  =  ".*\\.jar"  ; 




private   static   final   String   EXTENSION_JAR_ARCHIVE  =  ".jar"  ; 




private   static   final   String   EXTENSION_ZIP_ARCHIVE  =  ".zip"  ; 




public   static   final   String   BUNDLE_EXCEPTION  =  "exception"  ; 





public   static   final   String   BUNDLE_LOAD_DICTIONARY  =  "loadDictionary"  ; 





public   static   final   String   BUNDLE_SHOW_DICTIONARY_INSTALLATION  =  "showDictionaryInstallation"  ; 




public   static   final   String   BUNDLE_DOWNLOAD_DICTIONARY_ITEM  =  "downloadDictionaryItem"  ; 




public   static   final   int   STATUS_DOWNLOADING  =  0  ; 





public   static   final   int   STATUS_EXTRACTING_DICTIONARY  =  1  ; 





public   static   final   int   STATUS_EXTRACTING_JAR  =  2  ; 




public   static   final   int   STATUS_CREATING_FOLDERS  =  3  ; 




public   static   final   int   PERCENTAGE_BASE  =  1000  ; 




private   static   ServiceUpdateListener   listener  =  null  ; 




private   static   volatile   Thread   thread  =  null  ; 




private   static   int   lastSendType  =  -  1  ; 




private   static   int   lastSendPercentage  =  -  1  ; 




private   static   final   int   NOTIFICATION_STATUS_UPDATE  =  1  ; 




private   static   final   int   NOTIFICATION_RESULT  =  2  ; 




private   static   final   int   NOTIFICATION_EXCEPTION  =  3  ; 




private   NotificationManager   notificationManager  =  null  ; 





@  SuppressWarnings  (  "rawtypes"  ) 
private   static   final   Class  [  ]  startForegroundSignature  =  new   Class  [  ]  {  int  .  class  ,  Notification  .  class  }  ; 





@  SuppressWarnings  (  "rawtypes"  ) 
private   static   final   Class  [  ]  stopForegroundSignature  =  new   Class  [  ]  {  boolean  .  class  }  ; 





private   Method   startForeground  ; 





private   Method   stopForeground  ; 





private   final   Object  [  ]  startForegroundArgs  =  new   Object  [  2  ]  ; 





private   final   Object  [  ]  stopForegroundArgs  =  new   Object  [  1  ]  ; 




@  Override 
public   IBinder   onBind  (  final   Intent   intent  )  { 
return   null  ; 
} 




@  Override 
public   void   onCreate  (  )  { 
super  .  onCreate  (  )  ; 
notificationManager  =  (  NotificationManager  )  getSystemService  (  Context  .  NOTIFICATION_SERVICE  )  ; 
notificationManager  .  cancel  (  NOTIFICATION_STATUS_UPDATE  )  ; 
try  { 
startForeground  =  getClass  (  )  .  getMethod  (  "startForeground"  ,  startForegroundSignature  )  ; 
stopForeground  =  getClass  (  )  .  getMethod  (  "stopForeground"  ,  stopForegroundSignature  )  ; 
}  catch  (  NoSuchMethodException   e  )  { 
startForeground  =  null  ; 
stopForeground  =  null  ; 
} 
} 





void   startForegroundCompat  (  int   id  ,  Notification   notification  )  { 
if  (  startForeground  !=  null  )  { 
startForegroundArgs  [  0  ]  =  Integer  .  valueOf  (  id  )  ; 
startForegroundArgs  [  1  ]  =  notification  ; 
try  { 
startForeground  .  invoke  (  this  ,  startForegroundArgs  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
Log  .  w  (  DictionaryForMIDs  .  LOG_TAG  ,  "Unable to invoke startForeground"  ,  e  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
Log  .  w  (  DictionaryForMIDs  .  LOG_TAG  ,  "Unable to invoke startForeground"  ,  e  )  ; 
} 
return  ; 
} 
setForeground  (  true  )  ; 
notificationManager  .  notify  (  id  ,  notification  )  ; 
} 





void   stopForegroundCompat  (  int   id  )  { 
if  (  stopForeground  !=  null  )  { 
stopForegroundArgs  [  0  ]  =  Boolean  .  TRUE  ; 
try  { 
stopForeground  .  invoke  (  this  ,  stopForegroundArgs  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
Log  .  w  (  DictionaryForMIDs  .  LOG_TAG  ,  "Unable to invoke stopForeground"  ,  e  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
Log  .  w  (  DictionaryForMIDs  .  LOG_TAG  ,  "Unable to invoke stopForeground"  ,  e  )  ; 
} 
return  ; 
} 
notificationManager  .  cancel  (  id  )  ; 
setForeground  (  false  )  ; 
} 




@  Override 
public   void   onStart  (  final   Intent   intent  ,  final   int   startId  )  { 
final   DownloadDictionaryItem   dictionaryItem  =  (  DownloadDictionaryItem  )  intent  .  getParcelableExtra  (  BUNDLE_DOWNLOAD_DICTIONARY_ITEM  )  ; 
if  (  dictionaryItem  ==  null  )  { 
handleException  (  new   IllegalArgumentException  (  )  )  ; 
}  else  { 
startService  (  dictionaryItem  )  ; 
} 
} 




@  Override 
public   int   onStartCommand  (  final   Intent   intent  ,  final   int   flags  ,  final   int   startId  )  { 
onStart  (  intent  ,  startId  )  ; 
return   START_REDELIVER_INTENT  ; 
} 




@  Override 
public   void   onDestroy  (  )  { 
super  .  onDestroy  (  )  ; 
stopForegroundCompat  (  NOTIFICATION_STATUS_UPDATE  )  ; 
if  (  thread  ==  null  )  { 
return  ; 
} 
final   Thread   current  =  thread  ; 
thread  =  null  ; 
current  .  interrupt  (  )  ; 
} 







private   void   startService  (  final   DownloadDictionaryItem   item  )  { 
thread  =  new   InstallDictionaryThread  (  item  )  ; 
thread  .  start  (  )  ; 
} 







public   static   void   setUpdateListener  (  final   ServiceUpdateListener   updateListener  )  { 
DictionaryInstallationService  .  listener  =  updateListener  ; 
} 






public   static   boolean   isRunning  (  )  { 
if  (  thread  ==  null  )  { 
return   false  ; 
} 
if  (  thread  .  isAlive  (  )  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   void   removePendingStatusNotifications  (  Context   context  )  { 
if  (  DictionaryInstallationService  .  isRunning  (  )  )  { 
return  ; 
} 
(  (  NotificationManager  )  context  .  getSystemService  (  Context  .  NOTIFICATION_SERVICE  )  )  .  cancel  (  NOTIFICATION_STATUS_UPDATE  )  ; 
} 











private   void   handleUpdate  (  final   int   type  ,  final   int   percentage  )  { 
lastSendType  =  type  ; 
lastSendPercentage  =  percentage  ; 
final   double   progressBarPercentage  =  InstallDictionary  .  getProgressBarLength  (  type  ,  percentage  )  /  100.0  ; 
final   int   icon  =  R  .  drawable  .  defaulticon  ; 
final   CharSequence   tickerText  =  getText  (  R  .  string  .  msg_installing_dictionary  )  ; 
final   long   when  =  System  .  currentTimeMillis  (  )  ; 
final   Context   context  =  getApplicationContext  (  )  ; 
final   CharSequence   contentTitle  =  getText  (  R  .  string  .  title_installation_status  )  ; 
final   CharSequence   contentText  =  getString  (  R  .  string  .  msg_installation_status  ,  progressBarPercentage  )  ; 
final   Intent   notificationIntent  =  new   Intent  (  this  ,  DictionaryForMIDs  .  class  )  ; 
notificationIntent  .  putExtra  (  BUNDLE_SHOW_DICTIONARY_INSTALLATION  ,  true  )  ; 
final   PendingIntent   contentIntent  =  PendingIntent  .  getActivity  (  this  ,  0  ,  notificationIntent  ,  PendingIntent  .  FLAG_UPDATE_CURRENT  )  ; 
final   Notification   notification  =  new   Notification  (  icon  ,  tickerText  ,  when  )  ; 
notification  .  setLatestEventInfo  (  context  ,  contentTitle  ,  contentText  ,  contentIntent  )  ; 
notificationManager  .  notify  (  NOTIFICATION_STATUS_UPDATE  ,  notification  )  ; 
startForegroundCompat  (  NOTIFICATION_STATUS_UPDATE  ,  notification  )  ; 
if  (  listener  !=  null  )  { 
listener  .  onProgressUpdate  (  type  ,  percentage  )  ; 
} 
} 






public   static   int   pollLastType  (  )  { 
return   lastSendType  ; 
} 






public   static   int   pollLastPercentage  (  )  { 
return   lastSendPercentage  ; 
} 









private   void   handleResult  (  final   DownloadDictionaryItem   dictionaryItem  ,  final   String   path  )  { 
notificationManager  .  cancel  (  NOTIFICATION_STATUS_UPDATE  )  ; 
final   boolean   hasAutoInstallDictionary  =  Preferences  .  hasAutoInstallDictionary  (  )  ; 
final   int   id  =  dictionaryItem  .  getId  (  )  ; 
final   int   autoInstallDictionaryId  =  Preferences  .  getAutoInstallDictionaryId  (  )  ; 
if  (  hasAutoInstallDictionary  &&  id  ==  autoInstallDictionaryId  )  { 
Preferences  .  removeAutoInstallDictionaryId  (  )  ; 
} 
if  (  listener  !=  null  )  { 
listener  .  onFinished  (  path  )  ; 
stopSelf  (  )  ; 
return  ; 
} 
lastSendType  =  -  1  ; 
lastSendPercentage  =  0  ; 
final   int   icon  =  R  .  drawable  .  defaulticon  ; 
final   CharSequence   tickerText  =  getText  (  R  .  string  .  msg_installation_complete  )  ; 
final   long   when  =  System  .  currentTimeMillis  (  )  ; 
final   Context   context  =  getApplicationContext  (  )  ; 
final   CharSequence   contentTitle  =  getText  (  R  .  string  .  title_installation_finished  )  ; 
final   CharSequence   contentText  =  getText  (  R  .  string  .  msg_successfully_installed_dictionary  )  ; 
final   Intent   notificationIntent  =  new   Intent  (  this  ,  DictionaryForMIDs  .  class  )  ; 
notificationIntent  .  putExtra  (  BUNDLE_LOAD_DICTIONARY  ,  true  )  ; 
notificationIntent  .  putExtra  (  FileList  .  FILE_PATH  ,  path  )  ; 
notificationIntent  .  addFlags  (  Intent  .  FLAG_ACTIVITY_CLEAR_TOP  )  ; 
final   PendingIntent   contentIntent  =  PendingIntent  .  getActivity  (  this  ,  0  ,  notificationIntent  ,  PendingIntent  .  FLAG_UPDATE_CURRENT  )  ; 
final   Notification   notification  =  new   Notification  (  icon  ,  tickerText  ,  when  )  ; 
notification  .  setLatestEventInfo  (  context  ,  contentTitle  ,  contentText  ,  contentIntent  )  ; 
notification  .  flags  =  Notification  .  FLAG_AUTO_CANCEL  ; 
notificationManager  .  notify  (  NOTIFICATION_RESULT  ,  notification  )  ; 
final   String  [  ]  languages  =  {  dictionaryItem  .  getLocalizedName  (  getResources  (  )  )  }  ; 
Preferences  .  attachToContext  (  getApplicationContext  (  )  )  ; 
Preferences  .  addRecentDictionaryUrl  (  DictionaryType  .  DIRECTORY  ,  path  ,  languages  )  ; 
stopSelf  (  )  ; 
} 







private   void   handleException  (  final   Exception   exception  )  { 
notificationManager  .  cancel  (  NOTIFICATION_STATUS_UPDATE  )  ; 
if  (  listener  !=  null  )  { 
listener  .  onExitWithException  (  exception  )  ; 
stopSelf  (  )  ; 
return  ; 
} 
final   int   icon  =  R  .  drawable  .  defaulticon  ; 
final   CharSequence   tickerText  =  getText  (  R  .  string  .  msg_installation_error  )  ; 
final   long   when  =  System  .  currentTimeMillis  (  )  ; 
final   Context   context  =  getApplicationContext  (  )  ; 
final   CharSequence   contentTitle  =  getText  (  R  .  string  .  title_exception  )  ; 
final   CharSequence   contentText  =  exception  .  getMessage  (  )  ; 
final   Intent   notificationIntent  =  new   Intent  (  this  ,  DictionaryForMIDs  .  class  )  ; 
notificationIntent  .  putExtra  (  BUNDLE_EXCEPTION  ,  exception  )  ; 
notificationIntent  .  addFlags  (  Intent  .  FLAG_ACTIVITY_CLEAR_TOP  )  ; 
final   PendingIntent   contentIntent  =  PendingIntent  .  getActivity  (  this  ,  0  ,  notificationIntent  ,  PendingIntent  .  FLAG_UPDATE_CURRENT  )  ; 
final   Notification   notification  =  new   Notification  (  icon  ,  tickerText  ,  when  )  ; 
notification  .  setLatestEventInfo  (  context  ,  contentTitle  ,  contentText  ,  contentIntent  )  ; 
notification  .  flags  =  Notification  .  FLAG_AUTO_CANCEL  ; 
notificationManager  .  notify  (  NOTIFICATION_EXCEPTION  ,  notification  )  ; 
stopSelf  (  )  ; 
} 









private   void   createParentDirectories  (  final   File   file  )  throws   IOException  { 
if  (  file  .  exists  (  )  )  { 
return  ; 
} 
boolean   directoryWasCreated  =  true  ; 
if  (  !  file  .  getParentFile  (  )  .  exists  (  )  )  { 
directoryWasCreated  =  file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
} 
if  (  !  directoryWasCreated  )  { 
throw   new   IOException  (  getString  (  R  .  string  .  exception_failed_create_directory  ,  file  .  getParentFile  (  )  .  toString  (  )  )  )  ; 
} 
} 









private   void   createFile  (  final   File   file  )  throws   IOException  { 
if  (  file  .  exists  (  )  )  { 
if  (  !  file  .  delete  (  )  )  { 
throw   new   IOException  (  getString  (  R  .  string  .  exception_failed_delete_file  ,  file  .  toString  (  )  )  )  ; 
} 
} 
if  (  !  file  .  createNewFile  (  )  )  { 
throw   new   IOException  (  getString  (  R  .  string  .  exception_failed_create_file  ,  file  .  toString  (  )  )  )  ; 
} 
} 






private   final   class   InstallDictionaryThread   extends   Thread  { 




private   final   DownloadDictionaryItem   dictionaryItem  ; 







private   InstallDictionaryThread  (  final   DownloadDictionaryItem   dictionaryItem  )  { 
this  .  dictionaryItem  =  dictionaryItem  ; 
} 




@  Override 
public   void   run  (  )  { 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
handleUpdate  (  STATUS_CREATING_FOLDERS  ,  0  )  ; 
final   String   storageState  =  Environment  .  getExternalStorageState  (  )  ; 
if  (  !  Environment  .  MEDIA_MOUNTED  .  equals  (  storageState  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_error_accessing_storage  ,  storageState  )  )  )  ; 
return  ; 
} 
File   zipDownloadDirectory  =  new   File  (  getString  (  R  .  string  .  attribute_zip_directory  ,  Environment  .  getExternalStorageDirectory  (  )  )  )  ; 
try  { 
createParentDirectories  (  zipDownloadDirectory  )  ; 
}  catch  (  IOException   exception  )  { 
handleException  (  exception  )  ; 
return  ; 
} 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
handleUpdate  (  STATUS_CREATING_FOLDERS  ,  PERCENTAGE_BASE  /  2  )  ; 
File   jarDirectory  =  new   File  (  getString  (  R  .  string  .  attribute_jar_directory  ,  Environment  .  getExternalStorageDirectory  (  )  )  )  ; 
try  { 
createParentDirectories  (  jarDirectory  )  ; 
}  catch  (  IOException   exception  )  { 
handleException  (  exception  )  ; 
return  ; 
} 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
handleUpdate  (  STATUS_CREATING_FOLDERS  ,  PERCENTAGE_BASE  )  ; 
final   String   zipFile  =  getString  (  R  .  string  .  attribute_zip_directory  ,  Environment  .  getExternalStorageDirectory  (  )  )  +  dictionaryItem  .  getFileName  (  )  +  EXTENSION_ZIP_ARCHIVE  ; 
final   String   jarFile  =  getString  (  R  .  string  .  attribute_jar_directory  ,  Environment  .  getExternalStorageDirectory  (  )  )  +  dictionaryItem  .  getFileName  (  )  +  EXTENSION_JAR_ARCHIVE  ; 
final   String   dictionaryDirectory  =  getString  (  R  .  string  .  attribute_installation_directory  ,  Environment  .  getExternalStorageDirectory  (  )  )  +  dictionaryItem  .  getFileName  (  )  +  File  .  separator  ; 
String   resultPath  ; 
try  { 
downloadFile  (  dictionaryItem  .  getLink  (  )  ,  zipFile  )  ; 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
handleUpdate  (  STATUS_DOWNLOADING  ,  PERCENTAGE_BASE  )  ; 
extractFirstMatchingEntry  (  zipFile  ,  jarFile  ,  PATTERN_JAR_ARCHIVE  )  ; 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
handleUpdate  (  STATUS_EXTRACTING_JAR  ,  PERCENTAGE_BASE  )  ; 
resultPath  =  extractAllMatchingEntries  (  jarFile  ,  dictionaryDirectory  ,  PATTERN_EXTRACT_PATH  ,  PATTERN_RETURN_PATH  ,  PATTERN_DELETE_FROM_PATH  )  ; 
}  catch  (  IOException   e  )  { 
handleException  (  e  )  ; 
return  ; 
} 
if  (  interrupted  (  )  )  { 
handleException  (  new   InterruptedException  (  getString  (  R  .  string  .  msg_installation_aborted  )  )  )  ; 
return  ; 
} 
File   zipDeleteFile  =  new   File  (  zipFile  )  ; 
if  (  !  zipDeleteFile  .  delete  (  )  )  { 
Log  .  v  (  DictionaryForMIDs  .  LOG_TAG  ,  "Failed to delete zip: "  +  zipFile  )  ; 
} 
File   jarDeleteFile  =  new   File  (  jarFile  )  ; 
if  (  !  jarDeleteFile  .  delete  (  )  )  { 
Log  .  v  (  DictionaryForMIDs  .  LOG_TAG  ,  "Failed to delete jar: "  +  jarFile  )  ; 
} 
handleResult  (  dictionaryItem  ,  resultPath  )  ; 
} 











private   void   downloadFile  (  final   String   downloadUrl  ,  final   String   destinationFile  )  throws   IOException  { 
HttpClient   client  =  new   DefaultHttpClient  (  )  ; 
HttpGet   httpGet  =  new   HttpGet  (  downloadUrl  )  ; 
final   File   outputFile  =  new   File  (  destinationFile  )  ; 
createParentDirectories  (  outputFile  )  ; 
FileOutputStream   outputStream  ; 
outputStream  =  new   FileOutputStream  (  outputFile  )  ; 
final   HttpResponse   response  =  client  .  execute  (  httpGet  )  ; 
if  (  isInterrupted  (  )  )  { 
outputStream  .  close  (  )  ; 
return  ; 
} 
final   HttpEntity   entity  =  response  .  getEntity  (  )  ; 
InputStream   inputStream  =  null  ; 
try  { 
if  (  entity  !=  null  )  { 
inputStream  =  entity  .  getContent  (  )  ; 
CopyStreamStatusCallback   callback  =  new   CopyStreamStatusCallback  (  )  { 

@  Override 
public   long   getSkipBetweenUpdates  (  )  { 
return   entity  .  getContentLength  (  )  *  2  /  PERCENTAGE_BASE  ; 
} 

@  Override 
public   void   onUpdate  (  final   long   copiedLength  )  { 
int   percentage  =  (  int  )  (  copiedLength  *  PERCENTAGE_BASE  /  entity  .  getContentLength  (  )  )  ; 
handleUpdate  (  STATUS_DOWNLOADING  ,  percentage  )  ; 
} 
}  ; 
copyStreams  (  inputStream  ,  outputStream  ,  callback  )  ; 
} 
}  finally  { 
try  { 
outputStream  .  close  (  )  ; 
if  (  inputStream  !=  null  )  { 
inputStream  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
Log  .  v  (  DictionaryForMIDs  .  LOG_TAG  ,  "Exception while closing stream: "  +  e  )  ; 
} 
} 
} 


















public   String   extractAllMatchingEntries  (  final   String   sourceFile  ,  final   String   destinationDirectory  ,  final   String   filePattern  ,  final   String   selectPathPattern  ,  final   String   pathDeletePattern  )  throws   IOException  { 
ZipFile   zipFile  =  new   ZipFile  (  sourceFile  )  ; 
Enumeration  <  ?  extends   ZipEntry  >  entries  =  zipFile  .  entries  (  )  ; 
String   resultPath  =  null  ; 
int   elementIndex  =  -  1  ; 
final   float   elementProgressSize  =  (  float  )  PERCENTAGE_BASE  /  (  float  )  zipFile  .  size  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
elementIndex  ++  ; 
final   int   currentStart  =  (  int  )  (  elementIndex  *  elementProgressSize  )  ; 
if  (  lastSendType  !=  STATUS_EXTRACTING_DICTIONARY  ||  lastSendPercentage  +  PERCENTAGE_BASE  /  1000  <  currentStart  )  { 
handleUpdate  (  STATUS_EXTRACTING_DICTIONARY  ,  currentStart  )  ; 
} 
final   ZipEntry   entry  =  entries  .  nextElement  (  )  ; 
String   fileName  =  entry  .  getName  (  )  .  replaceFirst  (  pathDeletePattern  ,  ""  )  ; 
File   file  =  new   File  (  destinationDirectory  +  File  .  separator  +  fileName  )  ; 
final   boolean   fileNameMatchesPattern  =  entry  .  getName  (  )  .  matches  (  filePattern  )  ; 
if  (  !  fileNameMatchesPattern  )  { 
continue  ; 
} 
if  (  entry  .  isDirectory  (  )  )  { 
continue  ; 
} 
createParentDirectories  (  file  )  ; 
createFile  (  file  )  ; 
InputStream   input  =  zipFile  .  getInputStream  (  entry  )  ; 
FileOutputStream   output  =  new   FileOutputStream  (  file  )  ; 
if  (  elementProgressSize  <  1  ||  entry  .  getSize  (  )  <  10000  )  { 
copyStreams  (  input  ,  output  ,  null  )  ; 
}  else  { 
CopyStreamStatusCallback   callback  =  new   CopyStreamStatusCallback  (  )  { 

@  Override 
public   long   getSkipBetweenUpdates  (  )  { 
return  (  long  )  (  elementProgressSize  *  3  /  PERCENTAGE_BASE  )  ; 
} 

@  Override 
public   void   onUpdate  (  final   long   copiedLength  )  { 
final   int   currentFilePercentage  =  (  int  )  (  copiedLength  *  elementProgressSize  /  entry  .  getSize  (  )  )  ; 
final   int   percentage  =  currentStart  +  currentFilePercentage  ; 
handleUpdate  (  STATUS_EXTRACTING_DICTIONARY  ,  percentage  )  ; 
} 
}  ; 
copyStreams  (  input  ,  output  ,  callback  )  ; 
} 
if  (  file  .  getName  (  )  .  matches  (  selectPathPattern  )  )  { 
resultPath  =  file  .  getParent  (  )  ; 
} 
} 
return   resultPath  ; 
} 













public   void   extractFirstMatchingEntry  (  final   String   sourceFile  ,  final   String   destinationFile  ,  final   String   filePattern  )  throws   IOException  { 
ZipFile   zipFile  =  new   ZipFile  (  sourceFile  )  ; 
Enumeration  <  ?  extends   ZipEntry  >  entries  =  zipFile  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
final   ZipEntry   entry  =  entries  .  nextElement  (  )  ; 
final   boolean   fileNameMatchesPattern  =  entry  .  getName  (  )  .  matches  (  filePattern  )  ; 
if  (  !  fileNameMatchesPattern  )  { 
continue  ; 
} 
File   extractedFile  =  new   File  (  destinationFile  )  ; 
if  (  entry  .  isDirectory  (  )  )  { 
continue  ; 
} 
createParentDirectories  (  extractedFile  )  ; 
createFile  (  extractedFile  )  ; 
InputStream   input  =  zipFile  .  getInputStream  (  entry  )  ; 
FileOutputStream   output  =  new   FileOutputStream  (  extractedFile  )  ; 
CopyStreamStatusCallback   callback  =  new   CopyStreamStatusCallback  (  )  { 

@  Override 
public   long   getSkipBetweenUpdates  (  )  { 
return   entry  .  getSize  (  )  *  2  /  PERCENTAGE_BASE  ; 
} 

@  Override 
public   void   onUpdate  (  final   long   copiedLength  )  { 
final   int   percentage  =  (  int  )  (  copiedLength  *  PERCENTAGE_BASE  /  entry  .  getSize  (  )  )  ; 
handleUpdate  (  STATUS_EXTRACTING_JAR  ,  percentage  )  ; 
} 
}  ; 
copyStreams  (  input  ,  output  ,  callback  )  ; 
return  ; 
} 
final   String   message  =  getString  (  R  .  string  .  msg_compressed_entry_not_found  ,  sourceFile  ,  filePattern  )  ; 
throw   new   FileNotFoundException  (  message  )  ; 
} 













public   void   copyStreams  (  final   InputStream   input  ,  final   OutputStream   output  ,  final   CopyStreamStatusCallback   statusCallback  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  COPY_BUFFER_SIZE  ]  ; 
int   length  ; 
long   copiedLength  =  0  ; 
long   modulo  =  0  ; 
while  (  (  length  =  input  .  read  (  buffer  )  )  >=  0  )  { 
if  (  isInterrupted  (  )  )  { 
return  ; 
} 
output  .  write  (  buffer  ,  0  ,  length  )  ; 
copiedLength  +=  length  ; 
if  (  statusCallback  !=  null  &&  copiedLength  >=  modulo  )  { 
statusCallback  .  onUpdate  (  copiedLength  )  ; 
while  (  copiedLength  >=  modulo  )  { 
long   add  =  statusCallback  .  getSkipBetweenUpdates  (  )  ; 
if  (  add  <=  0  )  { 
add  =  1  ; 
} 
modulo  +=  add  ; 
} 
} 
} 
} 
} 






private   interface   CopyStreamStatusCallback  { 







void   onUpdate  (  long   copiedLength  )  ; 







long   getSkipBetweenUpdates  (  )  ; 
} 












public   static   boolean   startDictionaryInstallation  (  final   Context   context  ,  final   DownloadDictionaryItem   dictionaryItem  )  { 
if  (  DictionaryInstallationService  .  isRunning  (  )  )  { 
return   false  ; 
} 
Intent   intent  =  new   Intent  (  context  ,  DictionaryInstallationService  .  class  )  ; 
intent  .  putExtra  (  DictionaryInstallationService  .  BUNDLE_DOWNLOAD_DICTIONARY_ITEM  ,  dictionaryItem  )  ; 
context  .  startService  (  intent  )  ; 
return   true  ; 
} 
} 

