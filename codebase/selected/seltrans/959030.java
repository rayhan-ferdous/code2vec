package   wotlas  .  common  ; 

import   java  .  awt  .  Image  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Properties  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   wotlas  .  libs  .  aswing  .  ASwingResourceLocator  ; 
import   wotlas  .  libs  .  graphics2d  .  FontResourceLocator  ; 
import   wotlas  .  libs  .  graphics2d  .  ImageResourceLocator  ; 
import   wotlas  .  libs  .  log  .  LogResourceLocator  ; 
import   wotlas  .  libs  .  persistence  .  PersistenceException  ; 
import   wotlas  .  libs  .  persistence  .  PropertiesConverter  ; 
import   wotlas  .  libs  .  sound  .  MusicResourceLocator  ; 
import   wotlas  .  libs  .  sound  .  SoundResourceLocator  ; 
import   wotlas  .  libs  .  wizard  .  WizardResourceLocator  ; 
import   wotlas  .  utils  .  Debug  ; 
import   wotlas  .  utils  .  FileTools  ; 
import   wotlas  .  utils  .  ResourceLookup  ; 
import   wotlas  .  utils  .  Tools  ; 








public   class   ResourceManager   implements   LogResourceLocator  ,  ImageResourceLocator  ,  FontResourceLocator  ,  MusicResourceLocator  ,  SoundResourceLocator  ,  WizardResourceLocator  ,  ASwingResourceLocator  { 



public   static   final   String   WOTLAS_VERSION  =  "1.3"  ; 





public   static   final   String   DEFAULT_BASE_PATH  =  "base"  ; 




public   static   final   String   DEFAULT_HELP_DOCS_PATH  =  "docs/help"  ; 




public   static   final   String   DEFAULT_BIN_PATH  =  "bin"  ; 



public   static   final   String   WOTLAS_CLIENT_JAR  =  "client.jar"  ; 



public   static   final   String   WOTLAS_SERVER_JAR  =  "server.jar"  ; 



public   static   final   String   WOTLAS_ALL_JAR  =  "wotlas.jar"  ; 



public   static   final   String   WOTLAS_COMMON_JAR  =  "common"  ; 





public   static   final   String   WOTLAS_JAR_ROOT_RESOURCE_DIR  =  "/base"  ; 




public   static   final   String   WOTLAS_JAR_ROOT_DOCS_DIR  =  "/docs/help"  ; 





public   static   final   String   WOTLAS_JAR_EXTERNAL_DIR  =  "base-ext"  ; 



public   static   final   String   CONFIGS_DIR  =  "configs"  ; 



public   static   final   String   FONTS_DIR  =  "fonts"  ; 



public   static   final   String   GUI_IMAGES_DIR  =  "gui"  ; 



public   static   final   String   IMAGE_LIBRARY_DIR  =  "graphics/imagelib"  ; 



public   static   final   String   LAYOUTS_DIR  =  "layouts"  ; 



public   static   final   String   LOGS_DIR  =  "logs"  ; 



public   static   final   String   MACROS_DIR  =  "configs/macros"  ; 



public   static   final   String   MUSICS_DIR  =  "music"  ; 



public   static   final   String   PLAYERS_HOME_DIR  =  "home"  ; 



public   static   final   String   SERVER_CONFIGS_DIR  =  "servers"  ; 



public   static   final   String   SMILEYS_IMAGES_DIR  =  "gui/chat"  ; 



public   static   final   String   SOUNDS_DIR  =  "sounds"  ; 



public   static   final   String   UNIVERSE_DATA_DIR  =  "universe"  ; 



public   static   final   String   WIZARD_STEPS_DIR  =  "wizard"  ; 



public   static   final   String   WIN_BINARY_DIR  =  "win32"  ; 



public   static   final   String   UNIX_BINARY_DIR  =  "unix"  ; 



public   static   final   String   TRANSFER_SCRIPT_NAME  =  "transferScript"  ; 




public   static   final   String   JAR_RES_LIST_FILENAME  =  "resources.lst"  ; 




public   static   final   String   JAR_SEP  =  "/"  ; 




public   static   final   String   JAR_LIST_UNIVERSE_FILES  =  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  JAR_SEP  +  UNIVERSE_DATA_DIR  +  JAR_SEP  +  JAR_RES_LIST_FILENAME  ; 




public   static   final   String   JAR_LIST_BASE_FILES  =  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  JAR_SEP  +  JAR_RES_LIST_FILENAME  ; 




public   static   final   String   JAR_LIST_IMAGE_LIBRARY_FILES  =  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  JAR_SEP  +  IMAGE_LIBRARY_DIR  +  JAR_SEP  +  JAR_RES_LIST_FILENAME  ; 




public   static   final   String   JAR_LIST_WIZARD_STEPS_FILES  =  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  JAR_SEP  +  WIZARD_STEPS_DIR  +  JAR_SEP  +  JAR_RES_LIST_FILENAME  ; 




private   String   basePath  ; 



private   boolean   inJar  ; 



private   boolean   inClientJar  ; 



private   boolean   inServerJar  ; 



private   String   jarName  ; 




private   String   wotlasJarExternalDir  ; 









public   ResourceManager  (  String   defaultBasePath  ,  boolean   serverSide  )  { 
this  .  inJar  =  false  ; 
this  .  jarName  =  getJarName  (  )  ; 
Debug  .  signal  (  Debug  .  NOTICE  ,  this  ,  "ResourceManager loaded from  jar ? "  +  this  .  jarName  )  ; 
if  (  this  .  jarName  ==  null  )  { 
if  (  defaultBasePath  ==  null  ||  defaultBasePath  .  length  (  )  ==  0  )  { 
this  .  basePath  =  ResourceManager  .  DEFAULT_BASE_PATH  ; 
}  else  { 
this  .  basePath  =  defaultBasePath  ; 
} 
return  ; 
} 
this  .  wotlasJarExternalDir  =  createJarExternalDir  (  defaultBasePath  )  ; 
Debug  .  signal  (  Debug  .  NOTICE  ,  this  ,  "ResourceManager jarExternalDir= "  +  this  .  wotlasJarExternalDir  )  ; 
if  (  this  .  jarName  .  indexOf  (  ResourceManager  .  WOTLAS_CLIENT_JAR  )  >=  0  )  { 
this  .  inClientJar  =  true  ; 
this  .  inJar  =  true  ; 
try  { 
repairClassPath  (  )  ; 
createExternalClientDirectories  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Tools  .  displayDebugMessage  (  "Deployment Failed"  ,  "Wotlas failed to extract config files to the local directory."  +  "\nCheck the access rights of the installation directory of wotlas."  )  ; 
Debug  .  exit  (  )  ; 
} 
return  ; 
} 
if  (  this  .  jarName  .  indexOf  (  ResourceManager  .  WOTLAS_SERVER_JAR  )  >=  0  )  { 
this  .  inServerJar  =  true  ; 
this  .  inJar  =  true  ; 
try  { 
repairClassPath  (  )  ; 
createExternalServerDirectories  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Tools  .  displayDebugMessage  (  "Deployment Failed"  ,  "Wotlas failed to extract config files to the local directory."  +  "\nCheck the access rights of the installation directory of wotlas."  )  ; 
Debug  .  exit  (  )  ; 
} 
return  ; 
} 
if  (  this  .  jarName  .  indexOf  (  ResourceManager  .  WOTLAS_ALL_JAR  )  >=  0  ||  this  .  jarName  .  indexOf  (  ResourceManager  .  WOTLAS_COMMON_JAR  )  >=  0  )  { 
if  (  serverSide  )  { 
this  .  inServerJar  =  true  ; 
}  else  { 
this  .  inClientJar  =  true  ; 
} 
this  .  inJar  =  true  ; 
try  { 
repairClassPath  (  )  ; 
if  (  serverSide  )  { 
createExternalServerDirectories  (  )  ; 
}  else  { 
createExternalClientDirectories  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Tools  .  displayDebugMessage  (  "Deployment Failed"  ,  "Wotlas failed to extract config files to the local directory."  +  "\nCheck the access rights of the installation directory of wotlas."  )  ; 
Debug  .  exit  (  )  ; 
} 
return  ; 
} 
Tools  .  displayDebugMessage  (  "Wrong Jar Name"  ,  "The Jar file name should end with a 'client' or 'server' keyword"  )  ; 
Debug  .  exit  (  )  ; 
} 






private   String   createJarExternalDir  (  String   defaultBasePath  )  { 
String   extDir  =  null  ; 
String   userDir  =  null  ; 
try  { 
userDir  =  System  .  getProperty  (  "user.dir"  )  ; 
}  catch  (  SecurityException   se  )  { 
} 
if  (  defaultBasePath  !=  null  &&  defaultBasePath  .  length  (  )  !=  0  )  { 
File   extFile  =  new   File  (  defaultBasePath  )  ; 
if  (  extFile  .  exists  (  )  &&  extFile  .  canWrite  (  )  )  { 
extDir  =  extFile  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
} 
if  (  extDir  ==  null  &&  userDir  !=  null  &&  userDir  .  length  (  )  !=  0  )  { 
extFile  =  new   File  (  userDir  ,  defaultBasePath  )  ; 
if  (  extFile  .  exists  (  )  &&  extFile  .  canWrite  (  )  )  { 
extDir  =  extFile  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
} 
} 
} 
if  (  extDir  ==  null  &&  userDir  !=  null  &&  userDir  .  length  (  )  !=  0  )  { 
File   extFile  =  new   File  (  userDir  ,  ResourceManager  .  WOTLAS_JAR_EXTERNAL_DIR  )  ; 
if  (  extFile  .  exists  (  )  &&  extFile  .  canWrite  (  )  )  { 
extDir  =  extFile  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
} 
} 
if  (  extDir  ==  null  )  { 
extDir  =  getJarDir  (  )  +  ResourceManager  .  WOTLAS_JAR_EXTERNAL_DIR  ; 
} 
return   extDir  ; 
} 





public   void   setBasePath  (  String   basePath  )  { 
if  (  this  .  inJar  )  { 
Debug  .  signal  (  Debug  .  WARNING  ,  this  ,  "Attempt to change the external basePath ! No need to we are in a JAR !"  )  ; 
return  ; 
} 
this  .  basePath  =  basePath  ; 
} 




public   boolean   inJar  (  )  { 
return   this  .  inJar  ; 
} 




protected   String   getJarName  (  )  { 
URL   url  =  this  .  getClass  (  )  .  getResource  (  "ResourceManager.class"  )  ; 
if  (  url  ==  null  )  { 
return   null  ; 
} 
String   sUrl  =  ""  +  url  ; 
if  (  !  sUrl  .  startsWith  (  "jar:"  )  )  { 
return   null  ; 
} 
int   end  =  sUrl  .  indexOf  (  '!'  )  ; 
if  (  end  <  0  )  { 
return   null  ; 
} 
int   beg  =  sUrl  .  indexOf  (  "/"  )  ; 
if  (  beg  <  0  )  { 
return   null  ; 
} 
return   sUrl  .  substring  (  beg  +  1  ,  end  )  ; 
} 




protected   String   getJarDir  (  )  { 
if  (  this  .  jarName  ==  null  )  { 
return   null  ; 
} 
int   index  =  this  .  jarName  .  lastIndexOf  (  "/"  )  ; 
if  (  index  <  0  )  { 
return   null  ; 
} 
return   this  .  jarName  .  substring  (  0  ,  index  +  1  )  ; 
} 



protected   void   repairClassPath  (  )  { 
if  (  Tools  .  hasJar  (  this  .  jarName  )  )  { 
return  ; 
} 
String   cp  =  System  .  getProperty  (  "java.class.path"  ,  "."  )  +  System  .  getProperty  (  "path.separator"  ,  ";"  )  +  this  .  jarName  ; 
if  (  this  .  jarName  .  indexOf  (  "wotlas-common"  )  !=  -  1  )  { 
String   libdir  =  this  .  jarName  .  substring  (  0  ,  this  .  jarName  .  lastIndexOf  (  "/"  )  )  ; 
cp  =  cp  +  System  .  getProperty  (  "path.separator"  ,  ";"  )  +  libdir  +  "/wotlas-base-common.jar"  ; 
cp  =  cp  +  System  .  getProperty  (  "path.separator"  ,  ";"  )  +  libdir  +  "/wotlas-base-randland.jar"  ; 
} 
System  .  setProperty  (  "java.class.path"  ,  cp  )  ; 
} 







public   boolean   isExternal  (  String   pathName  )  { 
if  (  pathName  .  startsWith  (  this  .  wotlasJarExternalDir  )  )  { 
return   true  ; 
} 
if  (  pathName  .  indexOf  (  ResourceManager  .  PLAYERS_HOME_DIR  )  !=  -  1  )  { 
return   true  ; 
} 
return   false  ; 
} 



public   void   createExternalClientDirectories  (  )  { 
if  (  !  this  .  inClientJar  )  { 
return  ; 
} 
new   File  (  getExternalConfigsDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalMacrosDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalLogsDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalServerConfigsDir  (  )  )  .  mkdirs  (  )  ; 
} 



public   void   createExternalServerDirectories  (  )  { 
if  (  !  this  .  inServerJar  )  { 
return  ; 
} 
new   File  (  getExternalConfigsDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalLogsDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalServerConfigsDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getExternalPlayersHomeDir  (  )  )  .  mkdirs  (  )  ; 
new   File  (  getUniverseDataDir  (  )  )  .  mkdirs  (  )  ; 
} 




protected   String   getResourceDir  (  String   dirName  )  { 
if  (  this  .  inJar  )  { 
return   ResourceManager  .  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  "/"  +  dirName  +  "/"  ; 
} 
return   this  .  basePath  +  File  .  separator  +  dirName  +  File  .  separator  ; 
} 








protected   String   getExternalResourceDir  (  String   dirName  )  { 
if  (  this  .  inJar  )  { 
return   this  .  wotlasJarExternalDir  +  "/"  +  dirName  +  "/"  ; 
} 
return   this  .  basePath  +  File  .  separator  +  dirName  +  File  .  separator  ; 
} 



public   String   getConfigsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  CONFIGS_DIR  )  ; 
} 



public   String   getExternalConfigsDir  (  )  { 
return   getExternalResourceDir  (  ResourceManager  .  CONFIGS_DIR  )  ; 
} 



public   String   getFontsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  FONTS_DIR  )  ; 
} 



public   String   getGuiImageDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  GUI_IMAGES_DIR  )  ; 
} 



public   String   getImageLibraryDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  IMAGE_LIBRARY_DIR  )  ; 
} 



public   String   getExternalLogsDir  (  )  { 
return   getExternalResourceDir  (  ResourceManager  .  LOGS_DIR  )  ; 
} 



public   String   getExternalMacrosDir  (  )  { 
return   getExternalResourceDir  (  ResourceManager  .  MACROS_DIR  )  ; 
} 



public   String   getMusicsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  MUSICS_DIR  )  ; 
} 



public   String   getExternalPlayersHomeDir  (  )  { 
return   getExternalResourceDir  (  ResourceManager  .  PLAYERS_HOME_DIR  )  ; 
} 



public   String   getExternalServerConfigsDir  (  )  { 
return   getExternalResourceDir  (  ResourceManager  .  SERVER_CONFIGS_DIR  )  ; 
} 



public   String   getGuiSmileysDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  SMILEYS_IMAGES_DIR  )  ; 
} 



public   String   getSoundsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  SOUNDS_DIR  )  ; 
} 




public   String   getUniverseDataDir  (  )  { 
if  (  this  .  inClientJar  )  { 
return   ResourceManager  .  WOTLAS_JAR_ROOT_RESOURCE_DIR  +  "/"  +  ResourceManager  .  UNIVERSE_DATA_DIR  +  "/"  ; 
}  else   if  (  this  .  inServerJar  )  { 
return   this  .  wotlasJarExternalDir  +  "/"  +  ResourceManager  .  UNIVERSE_DATA_DIR  +  "/"  ; 
} 
return   this  .  basePath  +  File  .  separator  +  ResourceManager  .  UNIVERSE_DATA_DIR  +  File  .  separator  ; 
} 



public   String   getWizardStepsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  WIZARD_STEPS_DIR  )  ; 
} 



public   String   getLayoutsDir  (  )  { 
return   getResourceDir  (  ResourceManager  .  LAYOUTS_DIR  )  ; 
} 



public   String   getHelpDocsDir  (  )  { 
if  (  this  .  inJar  )  { 
return   ResourceManager  .  WOTLAS_JAR_ROOT_DOCS_DIR  +  "/"  ; 
} 
return   ResourceManager  .  DEFAULT_HELP_DOCS_PATH  +  File  .  separator  ; 
} 











public   String  [  ]  listUniverseFiles  (  String   dirName  ,  String   ext  )  { 
if  (  ext  ==  null  )  { 
ext  =  ""  ; 
} 
if  (  this  .  inClientJar  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_UNIVERSE_FILES  ,  dirName  ,  ext  )  ; 
}  else  { 
return   FileTools  .  listFiles  (  dirName  ,  ext  )  ; 
} 
} 










public   String  [  ]  listUniverseDirectories  (  String   dirName  )  { 
if  (  this  .  inClientJar  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_UNIVERSE_FILES  ,  dirName  ,  null  )  ; 
}  else  { 
return   FileTools  .  listDirs  (  dirName  )  ; 
} 
} 







public   Object   loadObject  (  String   filePath  )  { 
if  (  this  .  inClientJar  &&  !  isExternal  (  filePath  )  )  { 
URL   uri  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  filePath  )  ; 
try  { 
if  (  uri  !=  null  )  { 
InputStream   is  =  uri  .  openStream  (  )  ; 
Object   o  =  null  ; 
try  { 
o  =  PropertiesConverter  .  load  (  is  )  ; 
}  catch  (  PersistenceException   pe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  pe  )  ; 
} 
is  .  close  (  )  ; 
return   o  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
}  else  { 
try  { 
return   PropertiesConverter  .  load  (  filePath  )  ; 
}  catch  (  PersistenceException   pe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ""  +  pe  )  ; 
} 
} 
return   null  ; 
} 








public   boolean   saveObject  (  Object   o  ,  String   filePath  )  { 
try  { 
PropertiesConverter  .  save  (  o  ,  filePath  )  ; 
return   true  ; 
}  catch  (  PersistenceException   pe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  pe  )  ; 
return   false  ; 
} 
} 







public   String   loadText  (  String   filePath  )  { 
if  (  this  .  inJar  &&  !  isExternal  (  filePath  )  )  { 
URL   uri  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  filePath  )  ; 
try  { 
if  (  uri  !=  null  )  { 
InputStream   is  =  uri  .  openStream  (  )  ; 
String   txt  =  FileTools  .  loadTextFromStream  (  is  )  ; 
is  .  close  (  )  ; 
return   txt  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
return  ""  ; 
}  else  { 
return   FileTools  .  loadTextFromFile  (  filePath  )  ; 
} 
} 







public   boolean   saveText  (  String   filePath  ,  String   text  )  { 
return   FileTools  .  saveTextToFile  (  filePath  ,  text  )  ; 
} 







public   Properties   loadProperties  (  String   filePath  )  { 
if  (  this  .  inJar  &&  !  isExternal  (  filePath  )  )  { 
URL   uri  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  filePath  )  ; 
try  { 
if  (  uri  !=  null  )  { 
InputStream   is  =  uri  .  openStream  (  )  ; 
Properties   prop  =  FileTools  .  loadPropertiesFromStream  (  is  )  ; 
is  .  close  (  )  ; 
return   prop  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
return   new   Properties  (  )  ; 
}  else  { 
return   FileTools  .  loadPropertiesFile  (  filePath  )  ; 
} 
} 








public   boolean   saveProperties  (  Properties   props  ,  String   filePath  ,  String   header  )  { 
return   FileTools  .  savePropertiesFile  (  props  ,  filePath  ,  header  )  ; 
} 










public   String  [  ]  listFiles  (  String   dirName  )  { 
return   listFiles  (  dirName  ,  ""  )  ; 
} 











public   String  [  ]  listFiles  (  String   dirName  ,  String   ext  )  { 
if  (  ext  ==  null  )  { 
ext  =  ""  ; 
} 
if  (  this  .  inJar  &&  !  isExternal  (  dirName  )  )  { 
if  (  dirName  .  indexOf  (  IMAGE_LIBRARY_DIR  )  !=  -  1  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_IMAGE_LIBRARY_FILES  ,  dirName  ,  ext  )  ; 
} 
if  (  dirName  .  indexOf  (  WIZARD_STEPS_DIR  )  !=  -  1  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_WIZARD_STEPS_FILES  ,  dirName  ,  ext  )  ; 
} 
Debug  .  signal  (  Debug  .  WARNING  ,  this  ,  "Attempt to find a list of files in the default resources list :"  +  dirName  +  ", "  +  ResourceManager  .  JAR_LIST_BASE_FILES  )  ; 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_BASE_FILES  ,  dirName  ,  ext  )  ; 
}  else  { 
return   FileTools  .  listFiles  (  dirName  ,  ext  )  ; 
} 
} 










public   String  [  ]  listDirectories  (  String   dirName  )  { 
if  (  this  .  inJar  &&  !  isExternal  (  dirName  )  )  { 
if  (  dirName  .  indexOf  (  IMAGE_LIBRARY_DIR  )  !=  -  1  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_IMAGE_LIBRARY_FILES  ,  dirName  ,  null  )  ; 
} 
if  (  dirName  .  indexOf  (  WIZARD_STEPS_DIR  )  !=  -  1  )  { 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_WIZARD_STEPS_FILES  ,  dirName  ,  null  )  ; 
} 
Debug  .  signal  (  Debug  .  WARNING  ,  this  ,  "Attempt to find a list of directories in the default resources list :"  +  dirName  +  ", "  +  ResourceManager  .  JAR_LIST_BASE_FILES  )  ; 
return   Tools  .  listFilesInJar  (  ResourceManager  .  JAR_LIST_BASE_FILES  ,  dirName  ,  null  )  ; 
}  else  { 
return   FileTools  .  listDirs  (  dirName  )  ; 
} 
} 





public   InputStream   getFontStream  (  String   fontPath  )  { 
String   fDir  =  getFontsDir  (  )  ; 
if  (  !  fontPath  .  startsWith  (  fDir  )  )  { 
fontPath  =  fDir  +  fontPath  ; 
} 
if  (  this  .  inJar  )  { 
try  { 
URL   uri  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  fontPath  )  ; 
if  (  uri  !=  null  )  { 
return   uri  .  openStream  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
}  else  { 
try  { 
return   new   FileInputStream  (  fontPath  )  ; 
}  catch  (  FileNotFoundException   fe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  fe  )  ; 
} 
} 
return   null  ; 
} 





public   InputStream   getMusicStream  (  String   musicName  )  { 
String   mDir  =  getMusicsDir  (  )  ; 
if  (  !  musicName  .  startsWith  (  mDir  )  )  { 
musicName  =  mDir  +  musicName  ; 
} 
if  (  this  .  inJar  )  { 
try  { 
URL   uri  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  musicName  )  ; 
if  (  uri  !=  null  )  { 
return   uri  .  openStream  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
}  else  { 
try  { 
return   new   FileInputStream  (  musicName  )  ; 
}  catch  (  FileNotFoundException   fe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  fe  )  ; 
} 
} 
return   null  ; 
} 





public   InputStream   getSoundStream  (  String   soundName  )  { 
String   sDir  =  getSoundsDir  (  )  ; 
if  (  !  soundName  .  startsWith  (  sDir  )  )  { 
soundName  =  sDir  +  soundName  ; 
} 
if  (  this  .  inJar  )  { 
try  { 
URL   url  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  soundName  )  ; 
if  (  url  !=  null  )  { 
return   url  .  openStream  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
}  else  { 
try  { 
return   new   FileInputStream  (  soundName  )  ; 
}  catch  (  FileNotFoundException   fe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  fe  )  ; 
} 
} 
return   null  ; 
} 





public   InputStream   getFileStream  (  String   filePath  )  { 
if  (  this  .  inJar  )  { 
try  { 
URL   url  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  filePath  )  ; 
if  (  url  !=  null  )  { 
return   url  .  openStream  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  ioe  )  ; 
} 
}  else  { 
try  { 
return   new   FileInputStream  (  filePath  )  ; 
}  catch  (  FileNotFoundException   fe  )  { 
Debug  .  signal  (  Debug  .  ERROR  ,  this  ,  fe  )  ; 
} 
} 
return   null  ; 
} 






public   ImageIcon   getImageIcon  (  String   imageName  )  { 
String   imDir  =  getGuiImageDir  (  )  ; 
if  (  !  imageName  .  startsWith  (  imDir  )  )  { 
imageName  =  imDir  +  imageName  ; 
} 
if  (  this  .  inJar  )  { 
URL   url  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  imageName  )  ; 
if  (  url  !=  null  )  { 
return   new   ImageIcon  (  url  )  ; 
} 
}  else  { 
return   new   ImageIcon  (  imageName  )  ; 
} 
return   null  ; 
} 






public   Image   getGuiImage  (  String   imageName  )  { 
String   imDir  =  getGuiImageDir  (  )  ; 
if  (  !  imageName  .  startsWith  (  imDir  )  )  { 
imageName  =  imDir  +  imageName  ; 
} 
if  (  this  .  inJar  )  { 
URL   url  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  imageName  )  ; 
if  (  url  !=  null  )  { 
return   Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  url  )  ; 
} 
}  else  { 
return   Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  imageName  )  ; 
} 
return   null  ; 
} 






public   Image   getLibraryImage  (  String   imagePath  )  { 
if  (  this  .  inJar  )  { 
URL   url  =  getClassResourceUrl  (  this  .  getClass  (  )  ,  imagePath  )  ; 
if  (  url  !=  null  )  { 
return   Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  url  )  ; 
} 
}  else  { 
return   Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  imagePath  )  ; 
} 
return   null  ; 
} 






public   URL   getClassResourceUrl  (  Class   claRef  ,  String   resourceName  )  { 
Class   cla  =  claRef  ; 
if  (  cla  ==  null  )  { 
cla  =  this  .  getClass  (  )  ; 
} 
URL   url  =  ResourceLookup  .  getClassResourceUrl  (  cla  ,  resourceName  )  ; 
if  (  url  ==  null  )  { 
Debug  .  signal  (  Debug  .  WARNING  ,  this  ,  "Resource ("  +  resourceName  +  ") not found !"  )  ; 
} 
return   url  ; 
} 






public   String   getExternalTransferScript  (  )  { 
if  (  this  .  inClientJar  )  { 
return   null  ; 
} 
if  (  Tools  .  isWindowsOS  (  )  )  { 
return   ResourceManager  .  DEFAULT_BIN_PATH  +  File  .  separator  +  ResourceManager  .  WIN_BINARY_DIR  +  File  .  separator  +  ResourceManager  .  TRANSFER_SCRIPT_NAME  +  ".bat"  ; 
}  else  { 
return   ResourceManager  .  DEFAULT_BIN_PATH  +  File  .  separator  +  ResourceManager  .  UNIX_BINARY_DIR  +  File  .  separator  +  ResourceManager  .  TRANSFER_SCRIPT_NAME  +  ".sh"  ; 
} 
} 





public   String   getExternalScriptsDir  (  )  { 
if  (  this  .  inClientJar  )  { 
return   null  ; 
} 
if  (  Tools  .  isWindowsOS  (  )  )  { 
return   ResourceManager  .  DEFAULT_BIN_PATH  +  File  .  separator  +  ResourceManager  .  WIN_BINARY_DIR  +  File  .  separator  ; 
}  else  { 
return   ResourceManager  .  DEFAULT_BIN_PATH  +  File  .  separator  +  ResourceManager  .  UNIX_BINARY_DIR  +  File  .  separator  ; 
} 
} 
} 

