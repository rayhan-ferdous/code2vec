package   com  .  sun  .  speech  .  freetts  ; 

import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  File  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLClassLoader  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  JarURLConnection  ; 
import   java  .  util  .  LinkedHashMap  ; 
import   java  .  util  .  LinkedHashSet  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  SortedSet  ; 
import   java  .  util  .  jar  .  Attributes  ; 
import   java  .  util  .  jar  .  Attributes  .  Name  ; 
import   java  .  net  .  URLStreamHandlerFactory  ; 










public   class   VoiceManager  { 

private   static   final   VoiceManager   INSTANCE  =  new   VoiceManager  (  )  ; 

private   static   final   String   fileSeparator  =  System  .  getProperty  (  "file.separator"  )  ; 

private   static   final   String   pathSeparator  =  System  .  getProperty  (  "path.separator"  )  ; 







private   static   final   DynamicClassLoader   classLoader  =  new   DynamicClassLoader  (  new   URL  [  0  ]  ,  VoiceDirectory  .  class  .  getClassLoader  (  )  )  ; 

private   VoiceManager  (  )  { 
} 






public   static   VoiceManager   getInstance  (  )  { 
return   INSTANCE  ; 
} 









































public   Voice  [  ]  getVoices  (  )  { 
UniqueVector   voices  =  new   UniqueVector  (  )  ; 
VoiceDirectory  [  ]  voiceDirectories  =  getVoiceDirectories  (  )  ; 
for  (  int   i  =  0  ;  i  <  voiceDirectories  .  length  ;  i  ++  )  { 
voices  .  addArray  (  voiceDirectories  [  i  ]  .  getVoices  (  )  )  ; 
} 
Voice  [  ]  voiceArray  =  new   Voice  [  voices  .  size  (  )  ]  ; 
return  (  Voice  [  ]  )  voices  .  toArray  (  voiceArray  )  ; 
} 






public   String   getVoiceInfo  (  )  { 
String   infoString  =  ""  ; 
VoiceDirectory  [  ]  voiceDirectories  =  getVoiceDirectories  (  )  ; 
for  (  int   i  =  0  ;  i  <  voiceDirectories  .  length  ;  i  ++  )  { 
infoString  +=  voiceDirectories  [  i  ]  .  toString  (  )  ; 
} 
return   infoString  ; 
} 









private   VoiceDirectory  [  ]  getVoiceDirectories  (  )  { 
try  { 
String   voiceClasses  =  System  .  getProperty  (  "freetts.voices"  )  ; 
if  (  voiceClasses  !=  null  )  { 
return   getVoiceDirectoryNamesFromProperty  (  voiceClasses  )  ; 
} 
UniqueVector   voiceDirectoryNames  =  getVoiceDirectoryNamesFromFiles  (  )  ; 
UniqueVector   pathURLs  =  getVoiceJarURLs  (  )  ; 
voiceDirectoryNames  .  addVector  (  getVoiceDirectoryNamesFromJarURLs  (  pathURLs  )  )  ; 
URL  [  ]  voiceJarURLs  =  (  URL  [  ]  )  pathURLs  .  toArray  (  new   URL  [  pathURLs  .  size  (  )  ]  )  ; 
for  (  int   i  =  0  ;  i  <  voiceJarURLs  .  length  ;  i  ++  )  { 
getDependencyURLs  (  voiceJarURLs  [  i  ]  ,  pathURLs  )  ; 
} 
for  (  int   i  =  0  ;  i  <  pathURLs  .  size  (  )  ;  i  ++  )  { 
classLoader  .  addUniqueURL  (  (  URL  )  pathURLs  .  get  (  i  )  )  ; 
} 
Set  <  VoiceDirectory  >  voiceDirectories  =  new   LinkedHashSet  <  VoiceDirectory  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  voiceDirectoryNames  .  size  (  )  ;  i  ++  )  { 
Class   c  =  Class  .  forName  (  (  String  )  voiceDirectoryNames  .  get  (  i  )  ,  true  ,  classLoader  )  ; 
voiceDirectories  .  add  (  (  VoiceDirectory  )  c  .  newInstance  (  )  )  ; 
} 
return  (  VoiceDirectory  [  ]  )  voiceDirectories  .  toArray  (  new   VoiceDirectory  [  voiceDirectories  .  size  (  )  ]  )  ; 
}  catch  (  InstantiationException   e  )  { 
throw   new   Error  (  "Unable to load voice directory. "  +  e  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
throw   new   Error  (  "Unable to load voice directory. "  +  e  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
throw   new   Error  (  "Unable to load voice directory. "  +  e  )  ; 
} 
} 





private   VoiceDirectory  [  ]  getVoiceDirectoryNamesFromProperty  (  String   voiceClasses  )  throws   InstantiationException  ,  IllegalAccessException  ,  ClassNotFoundException  { 
String  [  ]  classnames  =  voiceClasses  .  split  (  ","  )  ; 
VoiceDirectory  [  ]  directories  =  new   VoiceDirectory  [  classnames  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  directories  .  length  ;  i  ++  )  { 
Class   c  =  Class  .  forName  (  classnames  [  i  ]  )  ; 
directories  [  i  ]  =  (  VoiceDirectory  )  c  .  newInstance  (  )  ; 
} 
return   directories  ; 
} 



















private   void   getDependencyURLs  (  URL   url  ,  UniqueVector   dependencyURLs  )  { 
try  { 
String   urlDirName  =  getURLDirName  (  url  )  ; 
if  (  url  .  getProtocol  (  )  .  equals  (  "jar"  )  )  { 
JarURLConnection   jarConnection  =  (  JarURLConnection  )  url  .  openConnection  (  )  ; 
Attributes   attributes  =  jarConnection  .  getMainAttributes  (  )  ; 
String   fullClassPath  =  attributes  .  getValue  (  Attributes  .  Name  .  CLASS_PATH  )  ; 
if  (  fullClassPath  ==  null  ||  fullClassPath  .  equals  (  ""  )  )  { 
return  ; 
} 
String  [  ]  classPath  =  fullClassPath  .  split  (  "\\s+"  )  ; 
URL   classPathURL  ; 
for  (  int   i  =  0  ;  i  <  classPath  .  length  ;  i  ++  )  { 
try  { 
if  (  classPath  [  i  ]  .  endsWith  (  "/"  )  )  { 
classPathURL  =  new   URL  (  "file:"  +  urlDirName  +  classPath  [  i  ]  )  ; 
}  else  { 
classPathURL  =  new   URL  (  "jar"  ,  ""  ,  "file:"  +  urlDirName  +  classPath  [  i  ]  +  "!/"  )  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
System  .  err  .  println  (  "Warning: unable to resolve dependency "  +  classPath  [  i  ]  +  " referenced by "  +  url  )  ; 
continue  ; 
} 
if  (  !  dependencyURLs  .  contains  (  classPathURL  )  )  { 
dependencyURLs  .  add  (  classPathURL  )  ; 
getDependencyURLs  (  classPathURL  ,  dependencyURLs  )  ; 
} 
} 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 








private   UniqueVector   getVoiceDirectoryNamesFromFiles  (  )  { 
try  { 
UniqueVector   voiceDirectoryNames  =  new   UniqueVector  (  )  ; 
InputStream   is  =  this  .  getClass  (  )  .  getResourceAsStream  (  "internal_voices.txt"  )  ; 
if  (  is  !=  null  )  { 
voiceDirectoryNames  .  addVector  (  getVoiceDirectoryNamesFromInputStream  (  is  )  )  ; 
} 
try  { 
voiceDirectoryNames  .  addVector  (  getVoiceDirectoryNamesFromFile  (  getBaseDirectory  (  )  +  "voices.txt"  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
String   voicesFile  =  System  .  getProperty  (  "freetts.voicesfile"  )  ; 
if  (  voicesFile  !=  null  )  { 
voiceDirectoryNames  .  addVector  (  getVoiceDirectoryNamesFromFile  (  voicesFile  )  )  ; 
} 
return   voiceDirectoryNames  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "Error reading voices files. "  +  e  )  ; 
} 
} 











private   UniqueVector   getVoiceDirectoryNamesFromJarURLs  (  UniqueVector   urls  )  { 
try  { 
UniqueVector   voiceDirectoryNames  =  new   UniqueVector  (  )  ; 
for  (  int   i  =  0  ;  i  <  urls  .  size  (  )  ;  i  ++  )  { 
JarURLConnection   jarConnection  =  (  JarURLConnection  )  (  (  URL  )  urls  .  get  (  i  )  )  .  openConnection  (  )  ; 
Attributes   attributes  =  jarConnection  .  getMainAttributes  (  )  ; 
String   mainClass  =  attributes  .  getValue  (  Attributes  .  Name  .  MAIN_CLASS  )  ; 
if  (  mainClass  ==  null  ||  mainClass  .  trim  (  )  .  equals  (  ""  )  )  { 
throw   new   Error  (  "No Main-Class found in jar "  +  (  URL  )  urls  .  get  (  i  )  )  ; 
} 
voiceDirectoryNames  .  add  (  mainClass  )  ; 
} 
return   voiceDirectoryNames  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "Error reading jarfile manifests. "  )  ; 
} 
} 










private   UniqueVector   getVoiceJarURLs  (  )  { 
UniqueVector   voiceJarURLs  =  new   UniqueVector  (  )  ; 
try  { 
String   baseDirectory  =  getBaseDirectory  (  )  ; 
if  (  !  baseDirectory  .  equals  (  ""  )  )  { 
voiceJarURLs  .  addVector  (  getVoiceJarURLsFromDir  (  baseDirectory  )  )  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
} 
String   voicesPath  =  System  .  getProperty  (  "freetts.voicespath"  ,  ""  )  ; 
if  (  !  voicesPath  .  equals  (  ""  )  )  { 
String  [  ]  dirNames  =  voicesPath  .  split  (  pathSeparator  )  ; 
for  (  int   i  =  0  ;  i  <  dirNames  .  length  ;  i  ++  )  { 
try  { 
voiceJarURLs  .  addVector  (  getVoiceJarURLsFromDir  (  dirNames  [  i  ]  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   Error  (  "Error loading jars from voicespath "  +  dirNames  [  i  ]  +  ". "  )  ; 
} 
} 
} 
return   voiceJarURLs  ; 
} 







private   UniqueVector   getVoiceJarURLsFromDir  (  String   dirName  )  throws   FileNotFoundException  { 
try  { 
UniqueVector   voiceJarURLs  =  new   UniqueVector  (  )  ; 
File   dir  =  new   File  (  new   URI  (  "file://"  +  dirName  )  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  { 
throw   new   FileNotFoundException  (  "File is not a directory: "  +  dirName  )  ; 
} 
File  [  ]  files  =  dir  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isFile  (  )  &&  (  !  files  [  i  ]  .  isHidden  (  )  )  &&  files  [  i  ]  .  getName  (  )  .  endsWith  (  ".jar"  )  )  { 
URL   jarURL  =  files  [  i  ]  .  toURL  (  )  ; 
jarURL  =  new   URL  (  "jar"  ,  ""  ,  "file:"  +  jarURL  .  getPath  (  )  +  "!/"  )  ; 
JarURLConnection   jarConnection  =  (  JarURLConnection  )  jarURL  .  openConnection  (  )  ; 
Attributes   attributes  =  jarConnection  .  getMainAttributes  (  )  ; 
if  (  attributes  !=  null  )  { 
String   isVoice  =  attributes  .  getValue  (  "FreeTTSVoiceDefinition"  )  ; 
if  (  isVoice  !=  null  &&  isVoice  .  trim  (  )  .  equals  (  "true"  )  )  { 
voiceJarURLs  .  add  (  jarURL  )  ; 
} 
} 
} 
} 
return   voiceJarURLs  ; 
}  catch  (  java  .  net  .  URISyntaxException   e  )  { 
throw   new   Error  (  "Error reading directory name '"  +  dirName  +  "'."  )  ; 
}  catch  (  MalformedURLException   e  )  { 
throw   new   Error  (  "Error reading jars from directory "  +  dirName  +  ". "  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "Error reading jars from directory "  +  dirName  +  ". "  )  ; 
} 
} 









public   String   toString  (  )  { 
String   names  =  ""  ; 
Voice  [  ]  voices  =  getVoices  (  )  ; 
for  (  int   i  =  0  ;  i  <  voices  .  length  ;  i  ++  )  { 
if  (  i  ==  voices  .  length  -  1  )  { 
if  (  i  ==  0  )  { 
names  =  voices  [  i  ]  .  getName  (  )  ; 
}  else  { 
names  +=  "or "  +  voices  [  i  ]  .  getName  (  )  ; 
} 
}  else  { 
names  +=  voices  [  i  ]  .  getName  (  )  +  " "  ; 
} 
} 
return   names  ; 
} 









public   boolean   contains  (  String   voiceName  )  { 
return  (  getVoice  (  voiceName  )  !=  null  )  ; 
} 









public   Voice   getVoice  (  String   voiceName  )  { 
Voice  [  ]  voices  =  getVoices  (  )  ; 
for  (  int   i  =  0  ;  i  <  voices  .  length  ;  i  ++  )  { 
if  (  voices  [  i  ]  .  getName  (  )  .  equals  (  voiceName  )  )  { 
return   voices  [  i  ]  ; 
} 
} 
return   null  ; 
} 










private   String   getBaseDirectory  (  )  { 
String   name  =  this  .  getClass  (  )  .  getName  (  )  ; 
int   lastdot  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  lastdot  !=  -  1  )  { 
name  =  name  .  substring  (  lastdot  +  1  )  ; 
} 
URL   url  =  this  .  getClass  (  )  .  getResource  (  name  +  ".class"  )  ; 
return   getURLDirName  (  url  )  ; 
} 








private   String   getURLDirName  (  URL   url  )  { 
String   urlFileName  =  url  .  getPath  (  )  ; 
int   i  =  urlFileName  .  lastIndexOf  (  '!'  )  ; 
if  (  i  ==  -  1  )  { 
i  =  urlFileName  .  length  (  )  ; 
} 
int   dir  =  urlFileName  .  lastIndexOf  (  "/"  ,  i  )  ; 
if  (  !  urlFileName  .  startsWith  (  "file:"  )  )  { 
return  ""  ; 
} 
return   urlFileName  .  substring  (  5  ,  dir  )  +  "/"  ; 
} 












private   UniqueVector   getVoiceDirectoryNamesFromFile  (  final   String   fileName  )  throws   FileNotFoundException  ,  IOException  { 
InputStream   is  =  new   FileInputStream  (  fileName  )  ; 
if  (  is  ==  null  )  { 
throw   new   IOException  (  )  ; 
}  else  { 
return   getVoiceDirectoryNamesFromInputStream  (  is  )  ; 
} 
} 











private   UniqueVector   getVoiceDirectoryNamesFromInputStream  (  final   InputStream   is  )  throws   IOException  { 
UniqueVector   names  =  new   UniqueVector  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  is  )  )  ; 
while  (  true  )  { 
String   line  =  reader  .  readLine  (  )  ; 
if  (  line  ==  null  )  { 
break  ; 
} 
line  =  line  .  trim  (  )  ; 
if  (  !  line  .  startsWith  (  "#"  )  &&  !  line  .  equals  (  ""  )  )  { 
names  .  add  (  line  )  ; 
} 
} 
return   names  ; 
} 









public   static   URLClassLoader   getVoiceClassLoader  (  )  { 
return   classLoader  ; 
} 
} 





class   DynamicClassLoader   extends   URLClassLoader  { 

private   java  .  util  .  HashSet   classPath  ; 




















public   DynamicClassLoader  (  URL  [  ]  urls  )  { 
super  (  urls  )  ; 
classPath  =  new   java  .  util  .  HashSet  (  urls  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
classPath  .  add  (  urls  [  i  ]  )  ; 
} 
} 




















public   DynamicClassLoader  (  URL  [  ]  urls  ,  ClassLoader   parent  )  { 
super  (  urls  ,  parent  )  ; 
classPath  =  new   java  .  util  .  HashSet  (  urls  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
classPath  .  add  (  urls  [  i  ]  )  ; 
} 
} 




















public   DynamicClassLoader  (  URL  [  ]  urls  ,  ClassLoader   parent  ,  URLStreamHandlerFactory   factory  )  { 
super  (  urls  ,  parent  ,  factory  )  ; 
classPath  =  new   java  .  util  .  HashSet  (  urls  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  urls  .  length  ;  i  ++  )  { 
classPath  .  add  (  urls  [  i  ]  )  ; 
} 
} 






public   synchronized   void   addUniqueURL  (  URL   url  )  { 
if  (  !  classPath  .  contains  (  url  )  )  { 
super  .  addURL  (  url  )  ; 
classPath  .  add  (  url  )  ; 
} 
} 
} 







class   UniqueVector  { 

private   java  .  util  .  HashSet   elementSet  ; 

private   java  .  util  .  Vector   elementVector  ; 




public   UniqueVector  (  )  { 
elementSet  =  new   java  .  util  .  HashSet  (  )  ; 
elementVector  =  new   java  .  util  .  Vector  (  )  ; 
} 







public   void   add  (  Object   o  )  { 
if  (  !  contains  (  o  )  )  { 
elementSet  .  add  (  o  )  ; 
elementVector  .  add  (  o  )  ; 
} 
} 







public   void   addVector  (  UniqueVector   v  )  { 
for  (  int   i  =  0  ;  i  <  v  .  size  (  )  ;  i  ++  )  { 
add  (  v  .  get  (  i  )  )  ; 
} 
} 







public   void   addArray  (  Object  [  ]  a  )  { 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
add  (  a  [  i  ]  )  ; 
} 
} 






public   int   size  (  )  { 
return   elementVector  .  size  (  )  ; 
} 










public   boolean   contains  (  Object   o  )  { 
return   elementSet  .  contains  (  o  )  ; 
} 









public   Object   get  (  int   index  )  { 
return   elementVector  .  get  (  index  )  ; 
} 







public   Object  [  ]  toArray  (  )  { 
return   elementVector  .  toArray  (  )  ; 
} 







public   Object  [  ]  toArray  (  Object  [  ]  a  )  { 
return   elementVector  .  toArray  (  a  )  ; 
} 
} 

