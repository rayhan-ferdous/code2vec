package   er  .  extensions  .  foundation  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   com  .  webobjects  .  appserver  .  WOApplication  ; 
import   com  .  webobjects  .  appserver  .  WOResourceManager  ; 
import   com  .  webobjects  .  appserver  .  _private  .  WOEncodingDetector  ; 
import   com  .  webobjects  .  foundation  .  NSArray  ; 
import   com  .  webobjects  .  foundation  .  NSBundle  ; 
import   com  .  webobjects  .  foundation  .  NSData  ; 
import   com  .  webobjects  .  foundation  .  NSForwardException  ; 
import   com  .  webobjects  .  foundation  .  NSMutableArray  ; 
import   com  .  webobjects  .  foundation  .  NSPropertyListSerialization  ; 
import   er  .  extensions  .  ERXExtensions  ; 
import   er  .  extensions  .  foundation  .  ERXRuntimeUtilities  .  Result  ; 
import   er  .  extensions  .  foundation  .  ERXRuntimeUtilities  .  TimeoutException  ; 




public   class   ERXFileUtilities  { 


public   static   final   Logger   log  =  Logger  .  getLogger  (  ERXFileUtilities  .  class  )  ; 










public   static   File   writeUrlToTempFile  (  String   url  ,  String   prefix  ,  String   suffix  )  throws   IOException  { 
return   ERXFileUtilities  .  writeUrlToTempFile  (  new   URL  (  url  )  ,  prefix  ,  suffix  )  ; 
} 










public   static   File   writeUrlToTempFile  (  URL   url  ,  String   prefix  ,  String   suffix  )  throws   IOException  { 
String   extension  ; 
if  (  suffix  ==  null  )  { 
String   urlStr  =  url  .  toExternalForm  (  )  ; 
int   dotIndex  =  urlStr  .  lastIndexOf  (  '.'  )  ; 
if  (  dotIndex  >=  0  )  { 
int   questionMarkIndex  =  urlStr  .  indexOf  (  '?'  ,  dotIndex  )  ; 
if  (  questionMarkIndex  ==  -  1  )  { 
extension  =  urlStr  .  substring  (  dotIndex  )  ; 
}  else  { 
extension  =  urlStr  .  substring  (  dotIndex  ,  questionMarkIndex  )  ; 
} 
}  else  { 
extension  =  ""  ; 
} 
}  else  { 
extension  =  suffix  ; 
} 
File   tempFile  =  ERXFileUtilities  .  writeInputStreamToTempFile  (  url  .  openStream  (  )  ,  prefix  ,  extension  )  ; 
return   tempFile  ; 
} 








public   static   void   writeUrlToTempFile  (  String   url  ,  File   file  )  throws   IOException  { 
ERXFileUtilities  .  writeUrlToTempFile  (  new   URL  (  url  )  ,  file  )  ; 
} 








public   static   void   writeUrlToTempFile  (  URL   url  ,  File   file  )  throws   IOException  { 
ERXFileUtilities  .  writeInputStreamToFile  (  url  .  openStream  (  )  ,  file  )  ; 
} 







public   static   byte  [  ]  bytesFromInputStream  (  InputStream   in  )  throws   IOException  { 
if  (  in  ==  null  )  throw   new   IllegalArgumentException  (  "null input stream"  )  ; 
ByteArrayOutputStream   bout  =  new   ByteArrayOutputStream  (  )  ; 
int   read  =  -  1  ; 
byte  [  ]  buf  =  new   byte  [  1024  *  50  ]  ; 
while  (  (  read  =  in  .  read  (  buf  )  )  !=  -  1  )  { 
bout  .  write  (  buf  ,  0  ,  read  )  ; 
} 
return   bout  .  toByteArray  (  )  ; 
} 








public   static   String   stringFromInputStream  (  InputStream   in  ,  String   encoding  )  throws   IOException  { 
return   new   String  (  bytesFromInputStream  (  in  )  ,  encoding  )  ; 
} 







public   static   String   stringFromInputStream  (  InputStream   in  )  throws   IOException  { 
return   new   String  (  bytesFromInputStream  (  in  )  )  ; 
} 








public   static   String   stringFromURL  (  URL   url  )  throws   IOException  { 
InputStream   is  =  url  .  openStream  (  )  ; 
try  { 
return   ERXFileUtilities  .  stringFromInputStream  (  is  )  ; 
}  finally  { 
is  .  close  (  )  ; 
} 
} 







public   static   byte  [  ]  bytesFromGZippedFile  (  File   f  )  throws   IOException  { 
if  (  f  ==  null  )  throw   new   IllegalArgumentException  (  "null file"  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  f  )  ; 
GZIPInputStream   gis  =  new   GZIPInputStream  (  fis  )  ; 
byte  [  ]  result  =  bytesFromInputStream  (  gis  )  ; 
fis  .  close  (  )  ; 
gis  .  close  (  )  ; 
return   result  ; 
} 







public   static   byte  [  ]  bytesFromFile  (  File   f  )  throws   IOException  { 
if  (  f  ==  null  )  throw   new   IllegalArgumentException  (  "null file"  )  ; 
return   bytesFromFile  (  f  ,  (  int  )  f  .  length  (  )  )  ; 
} 








public   static   byte  [  ]  bytesFromFile  (  File   f  ,  int   n  )  throws   IOException  { 
if  (  f  ==  null  )  throw   new   IllegalArgumentException  (  "null file"  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  f  )  ; 
try  { 
byte  [  ]  result  =  bytesFromInputStream  (  fis  ,  n  )  ; 
return   result  ; 
}  finally  { 
fis  .  close  (  )  ; 
} 
} 








public   static   byte  [  ]  bytesFromInputStream  (  InputStream   fis  ,  int   n  )  throws   IOException  { 
byte  [  ]  data  =  new   byte  [  n  ]  ; 
int   bytesRead  =  0  ; 
while  (  bytesRead  <  n  )  bytesRead  +=  fis  .  read  (  data  ,  bytesRead  ,  n  -  bytesRead  )  ; 
return   data  ; 
} 




@  SuppressWarnings  (  "dep-ann"  ) 
public   static   void   writeInputStreamToFile  (  File   f  ,  InputStream   is  )  throws   IOException  { 
writeInputStreamToFile  (  is  ,  f  )  ; 
} 








public   static   File   writeInputStreamToTempFile  (  InputStream   stream  )  throws   IOException  { 
return   ERXFileUtilities  .  writeInputStreamToTempFile  (  stream  ,  "_Wonder"  ,  ".tmp"  )  ; 
} 










public   static   File   writeInputStreamToTempFile  (  InputStream   stream  ,  String   prefix  ,  String   suffix  )  throws   IOException  { 
File   tempFile  ; 
try  { 
tempFile  =  File  .  createTempFile  (  prefix  ,  suffix  )  ; 
try  { 
ERXFileUtilities  .  writeInputStreamToFile  (  stream  ,  tempFile  )  ; 
}  catch  (  RuntimeException   e  )  { 
tempFile  .  delete  (  )  ; 
throw   e  ; 
}  catch  (  IOException   e  )  { 
tempFile  .  delete  (  )  ; 
throw   e  ; 
} 
}  catch  (  RuntimeException   e  )  { 
stream  .  close  (  )  ; 
throw   e  ; 
}  catch  (  IOException   e  )  { 
stream  .  close  (  )  ; 
throw   e  ; 
} 
return   tempFile  ; 
} 






public   static   void   writeInputStreamToFile  (  InputStream   stream  ,  File   file  )  throws   IOException  { 
FileOutputStream   out  ; 
try  { 
if  (  file  ==  null  )  throw   new   IllegalArgumentException  (  "Attempting to write to a null file!"  )  ; 
File   parent  =  file  .  getParentFile  (  )  ; 
if  (  parent  !=  null  &&  !  parent  .  exists  (  )  )  { 
parent  .  mkdirs  (  )  ; 
} 
out  =  new   FileOutputStream  (  file  )  ; 
}  catch  (  IOException   e  )  { 
stream  .  close  (  )  ; 
throw   e  ; 
}  catch  (  RuntimeException   e  )  { 
stream  .  close  (  )  ; 
throw   e  ; 
} 
ERXFileUtilities  .  writeInputStreamToOutputStream  (  stream  ,  true  ,  out  ,  true  )  ; 
} 

public   static   void   writeInputStreamToGZippedFile  (  InputStream   stream  ,  File   file  )  throws   IOException  { 
if  (  file  ==  null  )  throw   new   IllegalArgumentException  (  "Attempting to write to a null file!"  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  file  )  ; 
ERXFileUtilities  .  writeInputStreamToOutputStream  (  stream  ,  false  ,  new   GZIPOutputStream  (  out  )  ,  true  )  ; 
} 









public   static   void   writeInputStreamToOutputStream  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
ERXFileUtilities  .  writeInputStreamToOutputStream  (  in  ,  true  ,  out  ,  true  )  ; 
} 










public   static   void   writeInputStreamToOutputStream  (  InputStream   in  ,  boolean   closeInputStream  ,  OutputStream   out  ,  boolean   closeOutputStream  )  throws   IOException  { 
try  { 
BufferedInputStream   bis  =  new   BufferedInputStream  (  in  )  ; 
try  { 
byte   buf  [  ]  =  new   byte  [  1024  *  50  ]  ; 
int   read  =  -  1  ; 
while  (  (  read  =  bis  .  read  (  buf  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  read  )  ; 
} 
}  finally  { 
if  (  closeInputStream  )  { 
bis  .  close  (  )  ; 
} 
} 
out  .  flush  (  )  ; 
}  finally  { 
if  (  closeOutputStream  )  { 
out  .  close  (  )  ; 
} 
} 
} 

public   static   void   stringToGZippedFile  (  String   s  ,  File   f  )  throws   IOException  { 
if  (  s  ==  null  )  throw   new   NullPointerException  (  "string argument cannot be null"  )  ; 
if  (  f  ==  null  )  throw   new   NullPointerException  (  "file argument cannot be null"  )  ; 
byte  [  ]  bytes  =  s  .  getBytes  (  )  ; 
ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  bytes  )  ; 
writeInputStreamToGZippedFile  (  bais  ,  f  )  ; 
} 








public   static   void   stringToFile  (  String   s  ,  File   f  )  throws   IOException  { 
stringToFile  (  s  ,  f  ,  System  .  getProperty  (  "file.encoding"  )  )  ; 
} 









public   static   void   stringToFile  (  String   s  ,  File   f  ,  String   encoding  )  throws   IOException  { 
if  (  s  ==  null  )  throw   new   IllegalArgumentException  (  "string argument cannot be null"  )  ; 
if  (  f  ==  null  )  throw   new   IllegalArgumentException  (  "file argument cannot be null"  )  ; 
if  (  encoding  ==  null  )  throw   new   IllegalArgumentException  (  "encoding argument cannot be null"  )  ; 
Reader   reader  =  new   BufferedReader  (  new   StringReader  (  s  )  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  f  )  ; 
Writer   out  ; 
if  (  encoding  ==  null  )  out  =  new   BufferedWriter  (  new   OutputStreamWriter  (  fos  )  )  ;  else   out  =  new   BufferedWriter  (  new   OutputStreamWriter  (  fos  ,  encoding  )  )  ; 
char   buf  [  ]  =  new   char  [  1024  *  50  ]  ; 
int   read  =  -  1  ; 
while  (  (  read  =  reader  .  read  (  buf  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  read  )  ; 
} 
reader  .  close  (  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
} 








public   static   void   remoteCopyFile  (  String   srcHost  ,  String   srcPath  ,  String   dstHost  ,  String   dstPath  )  throws   IOException  { 
if  (  srcPath  ==  null  )  throw   new   IllegalArgumentException  (  "null source path not allowed"  )  ; 
if  (  dstPath  ==  null  )  throw   new   IllegalArgumentException  (  "null source path not allowed"  )  ; 
NSMutableArray  <  String  >  args  =  new   NSMutableArray  <  String  >  (  7  )  ; 
args  .  addObject  (  "/usr/bin/scp"  )  ; 
args  .  addObject  (  "-B"  )  ; 
args  .  addObject  (  "-q"  )  ; 
args  .  addObject  (  "-o"  )  ; 
args  .  addObject  (  "StrictHostKeyChecking=no"  )  ; 
args  .  addObject  (  (  (  srcHost  !=  null  )  ?  (  srcHost  +  ":"  )  :  ""  )  +  srcPath  )  ; 
args  .  addObject  (  (  (  dstHost  !=  null  )  ?  (  dstHost  +  ":"  )  :  ""  )  +  dstPath  )  ; 
String  [  ]  cmd  =  ERXArrayUtilities  .  toStringArray  (  args  )  ; 
try  { 
Result   result  =  ERXRuntimeUtilities  .  execute  (  cmd  ,  null  ,  null  ,  0L  )  ; 
if  (  result  .  getExitValue  (  )  !=  0  )  { 
throw   new   IOException  (  "Unable to remote copy file: (exit status = "  +  result  .  getExitValue  (  )  +  ") "  +  result  .  getErrorAsString  (  )  +  "\n"  )  ; 
} 
}  catch  (  TimeoutException   e  )  { 
throw   new   IOException  (  "Command timed out"  )  ; 
} 
} 







public   static   void   remoteCopyFile  (  File   srcFile  ,  String   dstHost  ,  String   dstPath  )  throws   IOException  { 
remoteCopyFile  (  null  ,  srcFile  .  getPath  (  )  ,  dstHost  ,  dstPath  )  ; 
} 







public   static   void   remoteCopyFile  (  String   srcHost  ,  String   srcPath  ,  File   dstFile  )  throws   IOException  { 
remoteCopyFile  (  srcHost  ,  srcPath  ,  null  ,  dstFile  .  getPath  (  )  )  ; 
} 







public   static   String   stringFromGZippedFile  (  File   f  )  throws   IOException  { 
return   new   String  (  bytesFromGZippedFile  (  f  )  )  ; 
} 







public   static   String   stringFromFile  (  File   f  )  throws   IOException  { 
return   new   String  (  bytesFromFile  (  f  )  )  ; 
} 








public   static   String   stringFromFile  (  File   f  ,  String   encoding  )  throws   IOException  { 
if  (  encoding  ==  null  )  { 
return   new   String  (  bytesFromFile  (  f  )  )  ; 
} 
return   new   String  (  bytesFromFile  (  f  )  ,  encoding  )  ; 
} 










public   static   String   pathForResourceNamed  (  String   fileName  ,  String   frameworkName  ,  NSArray   languages  )  { 
String   path  =  null  ; 
NSBundle   bundle  =  "app"  .  equals  (  frameworkName  )  ?  NSBundle  .  mainBundle  (  )  :  NSBundle  .  bundleForName  (  frameworkName  )  ; 
if  (  bundle  !=  null  &&  bundle  .  isJar  (  )  )  { 
log  .  warn  (  "Can't get path when run as jar: "  +  frameworkName  +  " - "  +  fileName  )  ; 
}  else  { 
WOApplication   application  =  WOApplication  .  application  (  )  ; 
if  (  application  !=  null  )  { 
URL   url  =  application  .  resourceManager  (  )  .  pathURLForResourceNamed  (  fileName  ,  frameworkName  ,  languages  )  ; 
if  (  url  !=  null  )  { 
path  =  url  .  getFile  (  )  ; 
} 
}  else   if  (  bundle  !=  null  )  { 
URL   url  =  bundle  .  pathURLForResourcePath  (  fileName  )  ; 
if  (  url  !=  null  )  { 
path  =  url  .  getFile  (  )  ; 
} 
} 
} 
return   path  ; 
} 










public   static   boolean   resourceExists  (  String   fileName  ,  String   frameworkName  ,  NSArray   languages  )  { 
URL   url  =  WOApplication  .  application  (  )  .  resourceManager  (  )  .  pathURLForResourceNamed  (  fileName  ,  frameworkName  ,  languages  )  ; 
return   url  !=  null  ; 
} 









public   static   InputStream   inputStreamForResourceNamed  (  String   fileName  ,  String   frameworkName  ,  NSArray   languages  )  { 
return   WOApplication  .  application  (  )  .  resourceManager  (  )  .  inputStreamForResourceNamed  (  fileName  ,  frameworkName  ,  languages  )  ; 
} 






public   static   String   datePathWithRoot  (  String   rootPath  )  { 
Calendar   defaultCalendar  =  Calendar  .  getInstance  (  )  ; 
defaultCalendar  .  setTime  (  new   Date  (  )  )  ; 
int   year  =  defaultCalendar  .  get  (  Calendar  .  YEAR  )  ; 
int   month  =  defaultCalendar  .  get  (  Calendar  .  MONTH  )  +  1  ; 
int   day  =  defaultCalendar  .  get  (  Calendar  .  DAY_OF_MONTH  )  ; 
int   hour  =  defaultCalendar  .  get  (  Calendar  .  HOUR_OF_DAY  )  ; 
String   datePath  =  rootPath  +  "/y"  +  year  +  (  (  month  >  9  )  ?  "/m"  :  "/m0"  )  +  month  +  (  (  day  >  9  )  ?  "/d"  :  "/d0"  )  +  day  +  (  (  hour  >  9  )  ?  "/h"  :  "/h0"  )  +  hour  ; 
return   datePath  ; 
} 











public   static   URL   pathURLForResourceNamed  (  String   fileName  ,  String   frameworkName  ,  NSArray   languages  )  { 
URL   url  =  null  ; 
WOApplication   application  =  WOApplication  .  application  (  )  ; 
if  (  application  !=  null  )  { 
WOResourceManager   resourceManager  =  application  .  resourceManager  (  )  ; 
if  (  resourceManager  !=  null  )  { 
url  =  resourceManager  .  pathURLForResourceNamed  (  fileName  ,  frameworkName  ,  languages  )  ; 
} 
} 
return   url  ; 
} 






public   static   URL   URLFromFile  (  File   file  )  { 
URL   url  =  null  ; 
if  (  file  !=  null  )  { 
try  { 
url  =  URLFromPath  (  file  .  getCanonicalPath  (  )  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   NSForwardException  (  ex  )  ; 
} 
} 
return   url  ; 
} 






public   static   URL   URLFromPath  (  String   fileName  )  { 
URL   url  =  null  ; 
if  (  fileName  !=  null  )  { 
try  { 
url  =  new   URL  (  "file://"  +  fileName  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
throw   new   NSForwardException  (  ex  )  ; 
} 
} 
return   url  ; 
} 

public   static   long   lastModifiedDateForFileInFramework  (  String   fileName  ,  String   frameworkName  )  { 
long   lastModified  =  0  ; 
String   filePath  =  pathForResourceNamed  (  fileName  ,  frameworkName  ,  null  )  ; 
if  (  filePath  !=  null  )  { 
lastModified  =  new   File  (  filePath  )  .  lastModified  (  )  ; 
} 
return   lastModified  ; 
} 










public   static   Object   readPropertyListFromFileInFramework  (  String   fileName  ,  String   aFrameWorkName  )  { 
return   readPropertyListFromFileInFramework  (  fileName  ,  aFrameWorkName  ,  null  ,  System  .  getProperty  (  "file.encoding"  )  )  ; 
} 












public   static   Object   readPropertyListFromFileInFramework  (  String   fileName  ,  String   aFrameWorkName  ,  String   encoding  )  { 
return   readPropertyListFromFileInFramework  (  fileName  ,  aFrameWorkName  ,  null  ,  encoding  )  ; 
} 












public   static   Object   readPropertyListFromFileInFramework  (  String   fileName  ,  String   aFrameWorkName  ,  NSArray   languageList  )  { 
Object   plist  =  null  ; 
try  { 
plist  =  readPropertyListFromFileInFramework  (  fileName  ,  aFrameWorkName  ,  languageList  ,  System  .  getProperty  (  "file.encoding"  )  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
try  { 
plist  =  readPropertyListFromFileInFramework  (  fileName  ,  aFrameWorkName  ,  languageList  ,  "UTF-16"  )  ; 
}  catch  (  IllegalArgumentException   e1  )  { 
plist  =  readPropertyListFromFileInFramework  (  fileName  ,  aFrameWorkName  ,  languageList  ,  "UTF-8"  )  ; 
} 
} 
return   plist  ; 
} 














public   static   Object   readPropertyListFromFileInFramework  (  String   fileName  ,  String   aFrameWorkName  ,  NSArray   languageList  ,  String   encoding  )  { 
Object   result  =  null  ; 
InputStream   stream  =  inputStreamForResourceNamed  (  fileName  ,  aFrameWorkName  ,  languageList  )  ; 
try  { 
if  (  stream  !=  null  )  { 
String   stringFromFile  ; 
if  (  true  )  { 
stringFromFile  =  stringFromInputStream  (  stream  ,  encoding  )  ; 
}  else  { 
byte   bytes  [  ]  =  bytesFromInputStream  (  stream  )  ; 
String   guessed  =  WOEncodingDetector  .  sharedInstance  (  )  .  guessEncodingForData  (  new   NSData  (  bytes  )  )  ; 
if  (  !  guessed  .  equals  (  encoding  )  &&  !  "ASCII"  .  equals  (  guessed  )  )  { 
stringFromFile  =  new   String  (  bytes  ,  guessed  )  ; 
log  .  info  (  "Encoding differs, guessed: "  +  guessed  +  " wanted: "  +  encoding  +  " fileName:"  +  aFrameWorkName  +  "/"  +  fileName  +  languageList  )  ; 
}  else  { 
stringFromFile  =  new   String  (  bytes  ,  encoding  )  ; 
} 
} 
result  =  NSPropertyListSerialization  .  propertyListFromString  (  stringFromFile  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
log  .  error  (  "ConfigurationManager: Error reading file <"  +  fileName  +  "> from framework "  +  aFrameWorkName  )  ; 
}  finally  { 
try  { 
if  (  stream  !=  null  )  { 
stream  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
log  .  error  (  "Failed attempt to close stream."  )  ; 
} 
} 
return   result  ; 
} 







public   static   void   deleteFilesInDirectory  (  File   directory  ,  boolean   recurseIntoDirectories  )  { 
deleteFilesInDirectory  (  directory  ,  null  ,  recurseIntoDirectories  ,  true  )  ; 
} 









public   static   void   deleteFilesInDirectory  (  File   directory  ,  FileFilter   filter  ,  boolean   recurseIntoDirectories  ,  boolean   removeDirectories  )  { 
if  (  !  directory  .  exists  (  )  )  throw   new   RuntimeException  (  "Attempting to delete files from a non-existant directory: "  +  directory  )  ; 
if  (  !  directory  .  isDirectory  (  )  )  throw   new   RuntimeException  (  "Attmepting to delete files from a file that is not a directory: "  +  directory  )  ; 
File   files  [  ]  =  filter  !=  null  ?  directory  .  listFiles  (  filter  )  :  directory  .  listFiles  (  )  ; 
if  (  files  !=  null  &&  files  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   aFile  =  files  [  i  ]  ; 
if  (  aFile  .  isDirectory  (  )  &&  recurseIntoDirectories  )  { 
deleteFilesInDirectory  (  aFile  ,  filter  ,  recurseIntoDirectories  ,  removeDirectories  )  ; 
} 
if  (  aFile  .  isFile  (  )  ||  (  aFile  .  isDirectory  (  )  &&  removeDirectories  &&  (  aFile  .  listFiles  (  )  ==  null  ||  aFile  .  listFiles  (  )  .  length  ==  0  )  )  )  { 
aFile  .  delete  (  )  ; 
} 
} 
} 
} 






public   static   boolean   deleteDirectory  (  File   directory  )  { 
if  (  !  directory  .  isDirectory  (  )  )  return   directory  .  delete  (  )  ; 
boolean   deletedAllFiles  =  true  ; 
String  [  ]  fileNames  =  directory  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  fileNames  .  length  ;  i  ++  )  { 
File   file  =  new   File  (  directory  ,  fileNames  [  i  ]  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
if  (  !  deleteDirectory  (  file  )  &&  deletedAllFiles  )  deletedAllFiles  =  false  ; 
}  else  { 
if  (  !  file  .  delete  (  )  &&  deletedAllFiles  )  deletedAllFiles  =  false  ; 
} 
} 
if  (  !  directory  .  delete  (  )  &&  deletedAllFiles  )  deletedAllFiles  =  false  ; 
return   deletedAllFiles  ; 
} 








public   static   void   chmod  (  File   file  ,  String   mode  )  throws   IOException  { 
Runtime  .  getRuntime  (  )  .  exec  (  new   String  [  ]  {  "chmod"  ,  mode  ,  file  .  getAbsolutePath  (  )  }  )  ; 
} 








public   static   void   chmodRecursively  (  File   dir  ,  String   mode  )  throws   IOException  { 
Runtime  .  getRuntime  (  )  .  exec  (  new   String  [  ]  {  "chmod"  ,  "-R"  ,  mode  ,  dir  .  getAbsolutePath  (  )  }  )  ; 
} 











public   static   void   linkFiles  (  File   source  ,  File   destination  ,  boolean   symbolic  ,  boolean   allowUnlink  ,  boolean   followSymbolicLinks  )  throws   IOException  { 
if  (  destination  ==  null  ||  source  ==  null  )  throw   new   IllegalArgumentException  (  "null source or destination not allowed"  )  ; 
ArrayList  <  String  >  array  =  new   ArrayList  <  String  >  (  )  ; 
array  .  add  (  "ln"  )  ; 
if  (  allowUnlink  )  array  .  add  (  "-f"  )  ; 
if  (  symbolic  )  array  .  add  (  "-s"  )  ; 
if  (  !  followSymbolicLinks  )  array  .  add  (  "-n"  )  ; 
array  .  add  (  source  .  getPath  (  )  )  ; 
array  .  add  (  destination  .  getPath  (  )  )  ; 
String  [  ]  cmd  =  new   String  [  array  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  size  (  )  ;  i  ++  )  cmd  [  i  ]  =  array  .  get  (  i  )  ; 
Process   task  =  null  ; 
try  { 
task  =  Runtime  .  getRuntime  (  )  .  exec  (  cmd  )  ; 
while  (  true  )  { 
try  { 
task  .  waitFor  (  )  ; 
break  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  task  .  exitValue  (  )  !=  0  )  { 
BufferedReader   err  =  new   BufferedReader  (  new   InputStreamReader  (  task  .  getErrorStream  (  )  )  )  ; 
throw   new   IOException  (  "Unable to create link: "  +  err  .  readLine  (  )  )  ; 
} 
}  finally  { 
ERXExtensions  .  freeProcessResources  (  task  )  ; 
} 
} 










public   static   void   copyFilesFromDirectory  (  File   srcDirectory  ,  File   dstDirectory  ,  boolean   deleteOriginals  ,  boolean   recursiveCopy  ,  FileFilter   filter  )  throws   FileNotFoundException  ,  IOException  { 
copyFilesFromDirectory  (  srcDirectory  ,  dstDirectory  ,  deleteOriginals  ,  true  ,  recursiveCopy  ,  filter  )  ; 
} 











public   static   void   copyFilesFromDirectory  (  File   srcDirectory  ,  File   dstDirectory  ,  boolean   deleteOriginals  ,  boolean   replaceExistingFiles  ,  boolean   recursiveCopy  ,  FileFilter   filter  )  throws   FileNotFoundException  ,  IOException  { 
if  (  !  srcDirectory  .  exists  (  )  ||  !  dstDirectory  .  exists  (  )  )  throw   new   RuntimeException  (  "Both the src and dst directories must exist! Src: "  +  srcDirectory  +  " Dst: "  +  dstDirectory  )  ; 
File   srcFiles  [  ]  =  filter  !=  null  ?  srcDirectory  .  listFiles  (  filter  )  :  srcDirectory  .  listFiles  (  )  ; 
if  (  srcFiles  !=  null  &&  srcFiles  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  srcFiles  .  length  ;  i  ++  )  { 
File   srcFile  =  srcFiles  [  i  ]  ; 
File   dstFile  =  new   File  (  dstDirectory  ,  srcFile  .  getName  (  )  )  ; 
if  (  srcFile  .  isDirectory  (  )  &&  recursiveCopy  )  { 
if  (  deleteOriginals  )  { 
renameTo  (  srcFile  ,  dstFile  )  ; 
}  else  { 
dstFile  .  mkdirs  (  )  ; 
copyFilesFromDirectory  (  srcFile  ,  dstFile  ,  deleteOriginals  ,  replaceExistingFiles  ,  recursiveCopy  ,  filter  )  ; 
} 
}  else   if  (  !  srcFile  .  isDirectory  (  )  )  { 
if  (  replaceExistingFiles  ||  !  dstFile  .  exists  (  )  )  { 
copyFileToFile  (  srcFile  ,  dstFile  ,  deleteOriginals  ,  true  )  ; 
}  else   if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Destination file: "  +  dstFile  +  " skipped as it exists and replaceExistingFiles is set to false."  )  ; 
} 
}  else   if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Source file: "  +  srcFile  +  " is a directory inside: "  +  dstDirectory  +  " and recursive copy is set to false."  )  ; 
} 
} 
} 
} 










public   static   void   copyFileToFile  (  File   srcFile  ,  File   dstFile  ,  boolean   deleteOriginals  ,  boolean   forceDelete  )  throws   FileNotFoundException  ,  IOException  { 
if  (  srcFile  .  exists  (  )  &&  srcFile  .  isFile  (  )  )  { 
boolean   copied  =  false  ; 
if  (  deleteOriginals  &&  (  !  forceDelete  ||  srcFile  .  canWrite  (  )  )  )  { 
copied  =  srcFile  .  renameTo  (  dstFile  )  ; 
} 
if  (  !  copied  )  { 
Throwable   thrownException  =  null  ; 
File   parent  =  dstFile  .  getParentFile  (  )  ; 
if  (  !  parent  .  exists  (  )  &&  !  parent  .  mkdirs  (  )  )  { 
throw   new   IOException  (  "Failed to create the directory "  +  parent  +  "."  )  ; 
} 
FileInputStream   in  =  new   FileInputStream  (  srcFile  )  ; 
try  { 
FileChannel   srcChannel  =  in  .  getChannel  (  )  ; 
try  { 
FileOutputStream   out  =  new   FileOutputStream  (  dstFile  )  ; 
try  { 
FileChannel   dstChannel  =  out  .  getChannel  (  )  ; 
try  { 
dstChannel  .  transferFrom  (  srcChannel  ,  0  ,  srcChannel  .  size  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
thrownException  =  t  ; 
}  finally  { 
dstChannel  .  close  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
if  (  thrownException  ==  null  )  { 
thrownException  =  t  ; 
} 
}  finally  { 
out  .  close  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
if  (  thrownException  ==  null  )  { 
thrownException  =  t  ; 
} 
}  finally  { 
srcChannel  .  close  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
if  (  thrownException  ==  null  )  { 
thrownException  =  t  ; 
} 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
if  (  thrownException  ==  null  )  { 
thrownException  =  e  ; 
} 
} 
} 
if  (  deleteOriginals  &&  (  srcFile  .  canWrite  (  )  ||  forceDelete  )  )  { 
if  (  !  srcFile  .  delete  (  )  )  { 
throw   new   IOException  (  "Failed to delete "  +  srcFile  +  "."  )  ; 
} 
} 
if  (  thrownException  !=  null  )  { 
if  (  thrownException   instanceof   IOException  )  { 
throw  (  IOException  )  thrownException  ; 
}  else   if  (  thrownException   instanceof   Error  )  { 
throw  (  Error  )  thrownException  ; 
}  else  { 
throw  (  RuntimeException  )  thrownException  ; 
} 
} 
} 
} 
} 








public   static   final   File   createTempDir  (  )  throws   IOException  { 
File   f  =  File  .  createTempFile  (  "WonderTempDir"  ,  ""  )  ; 
f  .  delete  (  )  ; 
f  .  delete  (  )  ; 
f  .  mkdirs  (  )  ; 
return   f  ; 
} 








public   static   final   File   createTempDir  (  String   prefix  ,  String   suffix  )  throws   IOException  { 
File   f  =  File  .  createTempFile  (  prefix  ,  suffix  )  ; 
f  .  delete  (  )  ; 
f  .  delete  (  )  ; 
f  .  mkdirs  (  )  ; 
return   f  ; 
} 










public   static   NSArray  <  File  >  arrayByAddingFilesInDirectory  (  File   directory  ,  boolean   recursive  )  { 
ERXFile   erxDirectory  =  new   ERXFile  (  directory  .  getAbsolutePath  (  )  )  ; 
NSMutableArray  <  File  >  files  =  new   NSMutableArray  <  File  >  (  )  ; 
if  (  !  erxDirectory  .  exists  (  )  )  { 
return   files  ; 
} 
File  [  ]  fileList  =  erxDirectory  .  listFiles  (  )  ; 
if  (  fileList  ==  null  )  { 
return   files  ; 
} 
for  (  int   i  =  0  ;  i  <  fileList  .  length  ;  i  ++  )  { 
File   f  =  fileList  [  i  ]  ; 
if  (  f  .  isDirectory  (  )  &&  recursive  )  { 
files  .  addObjectsFromArray  (  ERXFileUtilities  .  arrayByAddingFilesInDirectory  (  f  ,  true  )  )  ; 
}  else  { 
files  .  addObject  (  f  )  ; 
} 
} 
return   files  ; 
} 









public   static   String   replaceFileExtension  (  String   path  ,  String   newExtension  )  { 
String   tmp  =  "."  +  newExtension  ; 
if  (  path  .  endsWith  (  tmp  )  )  { 
return   path  ; 
}  else  { 
int   index  =  path  .  lastIndexOf  (  "."  )  ; 
if  (  index  >  0  )  { 
String   p  =  path  .  substring  (  0  ,  index  )  ; 
return   p  +  tmp  ; 
}  else  { 
return   path  +  tmp  ; 
} 
} 
} 



















public   static   File   unzipFile  (  File   f  ,  File   destination  )  throws   IOException  { 
if  (  !  f  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "file "  +  f  +  " does not exist"  )  ; 
} 
String   absolutePath  ; 
if  (  destination  !=  null  )  { 
absolutePath  =  destination  .  getAbsolutePath  (  )  ; 
if  (  !  destination  .  exists  (  )  )  { 
destination  .  mkdirs  (  )  ; 
}  else   if  (  !  destination  .  isDirectory  (  )  )  { 
absolutePath  =  absolutePath  .  substring  (  0  ,  absolutePath  .  lastIndexOf  (  File  .  separator  )  )  ; 
} 
}  else  { 
absolutePath  =  System  .  getProperty  (  "java.io.tmpdir"  )  ; 
} 
if  (  !  absolutePath  .  endsWith  (  File  .  separator  )  )  { 
absolutePath  +=  File  .  separator  ; 
} 
ZipFile   zipFile  =  new   ZipFile  (  f  )  ; 
Enumeration   en  =  zipFile  .  entries  (  )  ; 
if  (  en  .  hasMoreElements  (  )  )  { 
ZipEntry   firstEntry  =  (  ZipEntry  )  en  .  nextElement  (  )  ; 
if  (  firstEntry  .  isDirectory  (  )  ||  en  .  hasMoreElements  (  )  )  { 
String   dir  =  absolutePath  +  f  .  getName  (  )  ; 
if  (  dir  .  endsWith  (  ".zip"  )  )  { 
dir  =  dir  .  substring  (  0  ,  dir  .  length  (  )  -  4  )  ; 
} 
new   File  (  dir  )  .  mkdirs  (  )  ; 
absolutePath  =  dir  +  File  .  separator  ; 
} 
}  else  { 
return   null  ; 
} 
for  (  Enumeration   e  =  zipFile  .  entries  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
ZipEntry   ze  =  (  ZipEntry  )  e  .  nextElement  (  )  ; 
String   name  =  ze  .  getName  (  )  ; 
if  (  ze  .  isDirectory  (  )  )  { 
File   d  =  new   File  (  absolutePath  +  name  )  ; 
d  .  mkdirs  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "created directory "  +  d  .  getAbsolutePath  (  )  )  ; 
} 
}  else  { 
InputStream   is  =  zipFile  .  getInputStream  (  ze  )  ; 
writeInputStreamToFile  (  new   File  (  absolutePath  +  name  )  ,  is  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "unzipped file "  +  ze  .  getName  (  )  +  " into "  +  (  absolutePath  +  name  )  )  ; 
} 
} 
} 
return   new   File  (  absolutePath  )  ; 
} 








public   static   File   zipFile  (  File   f  ,  boolean   absolutePaths  ,  boolean   deleteOriginal  ,  boolean   forceDelete  )  throws   IOException  { 
return   zipFile  (  f  ,  absolutePaths  ,  deleteOriginal  ,  forceDelete  ,  9  )  ; 
} 

public   static   File   zipFile  (  File   f  ,  boolean   absolutePaths  ,  boolean   deleteOriginal  ,  boolean   forceDelete  ,  int   level  )  throws   IOException  { 
if  (  !  f  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "file "  +  f  +  " does not exist"  )  ; 
} 
File   destination  =  new   File  (  f  .  getAbsolutePath  (  )  +  ".zip"  )  ; 
if  (  destination  .  exists  (  )  )  { 
throw   new   IOException  (  "zipped file "  +  destination  +  " exists"  )  ; 
} 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  destination  )  )  )  ; 
zout  .  setLevel  (  level  )  ; 
NSArray  <  File  >  files  =  f  .  isDirectory  (  )  ?  arrayByAddingFilesInDirectory  (  f  ,  true  )  :  new   NSArray  <  File  >  (  f  )  ; 
try  { 
BufferedInputStream   origin  =  null  ; 
byte   data  [  ]  =  new   byte  [  2048  ]  ; 
for  (  int   i  =  0  ;  i  <  files  .  count  (  )  ;  i  ++  )  { 
File   currentFile  =  files  .  objectAtIndex  (  i  )  ; 
FileInputStream   fi  =  new   FileInputStream  (  currentFile  )  ; 
origin  =  new   BufferedInputStream  (  fi  ,  2048  )  ; 
String   entryName  =  currentFile  .  getAbsolutePath  (  )  ; 
if  (  !  absolutePaths  )  { 
if  (  f  .  isDirectory  (  )  )  { 
entryName  =  entryName  .  substring  (  f  .  getAbsolutePath  (  )  .  length  (  )  +  1  ,  entryName  .  length  (  )  )  ; 
}  else  { 
entryName  =  entryName  .  substring  (  f  .  getParentFile  (  )  .  getAbsolutePath  (  )  .  length  (  )  +  1  ,  entryName  .  length  (  )  )  ; 
} 
} 
ZipEntry   entry  =  new   ZipEntry  (  entryName  )  ; 
zout  .  putNextEntry  (  entry  )  ; 
int   count  ; 
while  (  (  count  =  origin  .  read  (  data  ,  0  ,  2048  )  )  !=  -  1  )  { 
zout  .  write  (  data  ,  0  ,  count  )  ; 
} 
origin  .  close  (  )  ; 
} 
zout  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
if  (  deleteOriginal  )  { 
if  (  f  .  canWrite  (  )  ||  forceDelete  )  { 
if  (  !  deleteDirectory  (  f  )  )  { 
deleteDirectory  (  f  )  ; 
} 
} 
} 
return   destination  ; 
} 








public   static   byte  [  ]  md5  (  File   file  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  file  )  ; 
try  { 
return   md5  (  fis  )  ; 
}  finally  { 
fis  .  close  (  )  ; 
} 
} 








public   static   byte  [  ]  md5  (  InputStream   in  )  throws   IOException  { 
try  { 
java  .  security  .  MessageDigest   md5  =  java  .  security  .  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte  [  ]  buf  =  new   byte  [  50  *  1024  ]  ; 
int   numRead  ; 
while  (  (  numRead  =  in  .  read  (  buf  )  )  !=  -  1  )  { 
md5  .  update  (  buf  ,  0  ,  numRead  )  ; 
} 
return   md5  .  digest  (  )  ; 
}  catch  (  java  .  security  .  NoSuchAlgorithmException   e  )  { 
throw   new   NSForwardException  (  e  )  ; 
} 
} 








public   static   String   md5Hex  (  File   file  )  throws   IOException  { 
return   ERXStringUtilities  .  byteArrayToHexString  (  md5  (  file  )  )  ; 
} 








public   static   String   md5Hex  (  InputStream   in  )  throws   IOException  { 
return   ERXStringUtilities  .  byteArrayToHexString  (  md5  (  in  )  )  ; 
} 

public   static   long   length  (  File   f  )  { 
if  (  !  f  .  isDirectory  (  )  )  { 
return   f  .  length  (  )  ; 
}  else  { 
long   length  =  0  ; 
File  [  ]  files  =  f  .  listFiles  (  )  ; 
for  (  int   i  =  files  .  length  ;  i  --  >  0  ;  )  { 
length  +=  length  (  files  [  i  ]  )  ; 
} 
return   length  ; 
} 
} 








public   static   String   shortenFilename  (  String   name  ,  int   maxLength  )  { 
String   ext  =  fileExtension  (  name  )  ; 
String   s  =  removeFileExtension  (  name  )  ; 
String   elips  =  "..."  ; 
int   elipsLength  =  elips  .  length  (  )  ; 
int   stringLength  =  s  .  length  (  )  ; 
if  (  stringLength  ==  maxLength  )  return   name  ; 
if  (  maxLength  <=  elipsLength  )  maxLength  =  elipsLength  +  1  ; 
int   noOfChars  =  maxLength  -  elipsLength  ; 
int   mod  =  noOfChars  %  2  ; 
int   firstHalf  =  noOfChars  /  2  +  mod  ; 
int   secondHalf  =  firstHalf  -  mod  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  s  .  substring  (  0  ,  firstHalf  )  )  ; 
sb  .  append  (  elips  )  ; 
sb  .  append  (  s  .  substring  (  stringLength  -  secondHalf  ,  stringLength  )  )  ; 
sb  .  append  (  "."  )  ; 
sb  .  append  (  ext  )  ; 
return   sb  .  toString  (  )  ; 
} 





public   static   String   removeFileExtension  (  String   name  )  { 
int   index  =  name  .  lastIndexOf  (  "."  )  ; 
if  (  index  ==  -  1  )  { 
return   name  ; 
}  else  { 
return   name  .  substring  (  0  ,  index  )  ; 
} 
} 





public   static   String   fileExtension  (  String   name  )  { 
int   index  =  name  .  lastIndexOf  (  "."  )  ; 
if  (  index  ==  -  1  )  { 
return  ""  ; 
}  else  { 
return   name  .  substring  (  index  +  1  )  ; 
} 
} 






public   static   boolean   deleteFiles  (  NSMutableArray   filesToDelete  )  { 
boolean   deletedAllFiles  =  true  ; 
for  (  int   i  =  filesToDelete  .  count  (  )  ;  i  --  >  0  ;  )  { 
File   currentFile  =  (  File  )  filesToDelete  .  objectAtIndex  (  i  )  ; 
if  (  !  deleteFile  (  currentFile  )  &&  deletedAllFiles  )  deletedAllFiles  =  false  ; 
} 
return   deletedAllFiles  ; 
} 

public   static   boolean   deleteFile  (  File   fileToDelete  )  { 
return   deleteDirectory  (  fileToDelete  )  ; 
} 







public   static   File  [  ]  listDirectories  (  File   baseDir  ,  boolean   recursive  )  { 
File  [  ]  files  =  baseDir  .  listFiles  (  new   FileFilter  (  )  { 

public   boolean   accept  (  File   f  )  { 
return   f  .  isDirectory  (  )  ; 
} 
}  )  ; 
if  (  recursive  )  { 
NSMutableArray  <  File  >  a  =  new   NSMutableArray  <  File  >  (  files  )  ; 
for  (  int   i  =  files  .  length  ;  i  --  >  0  ;  )  { 
File   currentDir  =  files  [  i  ]  ; 
File  [  ]  currentDirs  =  listDirectories  (  currentDir  ,  true  )  ; 
a  .  addObjects  (  currentDirs  )  ; 
} 
Object  [  ]  objects  =  a  .  objects  (  )  ; 
files  =  new   File  [  objects  .  length  ]  ; 
System  .  arraycopy  (  objects  ,  0  ,  files  ,  0  ,  objects  .  length  )  ; 
} 
return   files  ; 
} 








public   static   File  [  ]  listFiles  (  File   baseDir  ,  boolean   recursive  ,  FileFilter   filter  )  { 
File  [  ]  files  =  baseDir  .  listFiles  (  filter  )  ; 
if  (  files  !=  null  &&  recursive  )  { 
NSMutableArray  <  File  >  a  =  new   NSMutableArray  <  File  >  (  )  ; 
for  (  int   i  =  files  .  length  ;  i  --  >  0  ;  )  { 
File   currentFile  =  files  [  i  ]  ; 
a  .  addObject  (  currentFile  )  ; 
if  (  currentFile  .  isDirectory  (  )  )  { 
File  [  ]  currentFiles  =  listFiles  (  currentFile  ,  true  ,  filter  )  ; 
a  .  addObjects  (  currentFiles  )  ; 
} 
} 
Object  [  ]  objects  =  a  .  objects  (  )  ; 
files  =  new   File  [  objects  .  length  ]  ; 
System  .  arraycopy  (  objects  ,  0  ,  files  ,  0  ,  objects  .  length  )  ; 
} 
return   files  ; 
} 









public   static   void   renameTo  (  File   source  ,  File   destination  )  throws   FileNotFoundException  ,  IOException  { 
if  (  !  source  .  renameTo  (  destination  )  )  { 
ERXFileUtilities  .  copyFileToFile  (  source  ,  destination  ,  true  ,  true  )  ; 
} 
} 







public   static   String   fileNameFromBrowserSubmittedPath  (  String   path  )  { 
String   fileName  =  path  ; 
if  (  path  !=  null  )  { 
int   separatorIndex  =  path  .  lastIndexOf  (  "\\"  )  ; 
if  (  separatorIndex  ==  -  1  )  { 
separatorIndex  =  path  .  lastIndexOf  (  "/"  )  ; 
} 
if  (  separatorIndex  ==  -  1  )  { 
separatorIndex  =  path  .  lastIndexOf  (  ":"  )  ; 
} 
if  (  separatorIndex  !=  -  1  )  { 
fileName  =  path  .  substring  (  separatorIndex  +  1  )  ; 
} 
fileName  =  fileName  .  replaceAll  (  "\\.\\."  ,  "_"  )  ; 
} 
return   fileName  ; 
} 












public   static   File   reserveUniqueFile  (  File   desiredFile  ,  boolean   overwrite  )  throws   IOException  { 
File   destinationFile  =  desiredFile  ; 
File   destinationFolder  =  destinationFile  .  getParentFile  (  )  ; 
if  (  !  destinationFolder  .  exists  (  )  )  { 
if  (  !  destinationFolder  .  mkdirs  (  )  )  { 
if  (  !  destinationFolder  .  exists  (  )  )  { 
throw   new   IOException  (  "Unable to create the destination folder '"  +  destinationFolder  +  "'."  )  ; 
} 
} 
} 
if  (  !  overwrite  )  { 
if  (  !  desiredFile  .  createNewFile  (  )  )  { 
File   parentFolder  =  desiredFile  .  getParentFile  (  )  ; 
String   fileName  =  desiredFile  .  getName  (  )  ; 
int   dotIndex  =  fileName  .  lastIndexOf  (  '.'  )  ; 
String   prefix  ,  suffix  ; 
if  (  dotIndex  <  0  )  { 
prefix  =  fileName  ; 
suffix  =  ""  ; 
}  else  { 
prefix  =  fileName  .  substring  (  0  ,  dotIndex  )  ; 
suffix  =  fileName  .  substring  (  dotIndex  )  ; 
} 
int   counter  =  1  ; 
do  { 
destinationFile  =  new   File  (  parentFolder  ,  prefix  +  "-"  +  counter  +  suffix  )  ; 
counter  ++  ; 
}  while  (  !  destinationFile  .  createNewFile  (  )  )  ; 
} 
} 
return   destinationFile  ; 
} 
} 

