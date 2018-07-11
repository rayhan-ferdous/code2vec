package   websiteschema  .  device  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  *  ; 
import   org  .  apache  .  felix  .  framework  .  util  .  Util  ; 
import   org  .  osgi  .  framework  .  Constants  ; 
import   org  .  osgi  .  framework  .  FrameworkEvent  ; 
import   org  .  osgi  .  framework  .  launch  .  Framework  ; 
import   org  .  osgi  .  framework  .  launch  .  FrameworkFactory  ; 










public   class   Main  { 




public   static   final   String   BUNDLE_DIR_SWITCH  =  "-b"  ; 





public   static   final   String   SHUTDOWN_HOOK_PROP  =  "felix.shutdown.hook"  ; 





public   static   final   String   SYSTEM_PROPERTIES_PROP  =  "felix.system.properties"  ; 




public   static   final   String   SYSTEM_PROPERTIES_FILE_VALUE  =  "system.properties"  ; 





public   static   final   String   CONFIG_PROPERTIES_PROP  =  "felix.config.properties"  ; 




public   static   final   String   CONFIG_PROPERTIES_FILE_VALUE  =  "config.properties"  ; 




public   static   final   String   CONFIG_DIRECTORY  =  "conf"  ; 

private   static   Framework   m_fwk  =  null  ; 


























































































































public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
String   bundleDir  =  null  ; 
String   cacheDir  =  null  ; 
boolean   expectBundleDir  =  false  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  equals  (  BUNDLE_DIR_SWITCH  )  )  { 
expectBundleDir  =  true  ; 
}  else   if  (  expectBundleDir  )  { 
bundleDir  =  args  [  i  ]  ; 
expectBundleDir  =  false  ; 
}  else  { 
cacheDir  =  args  [  i  ]  ; 
} 
} 
if  (  (  args  .  length  >  3  )  ||  (  expectBundleDir  &&  bundleDir  ==  null  )  )  { 
System  .  out  .  println  (  "Usage: [-b <bundle-deploy-dir>] [<bundle-cache-dir>]"  )  ; 
System  .  exit  (  0  )  ; 
} 
Main  .  loadSystemProperties  (  )  ; 
Properties   configProps  =  Main  .  loadConfigProperties  (  )  ; 
if  (  configProps  ==  null  )  { 
System  .  err  .  println  (  "No "  +  CONFIG_PROPERTIES_FILE_VALUE  +  " found."  )  ; 
configProps  =  new   Properties  (  )  ; 
} 
Main  .  copySystemProperties  (  configProps  )  ; 
if  (  bundleDir  !=  null  )  { 
configProps  .  setProperty  (  AutoProcessor  .  AUTO_DEPLOY_DIR_PROPERY  ,  bundleDir  )  ; 
} 
if  (  cacheDir  !=  null  )  { 
configProps  .  setProperty  (  Constants  .  FRAMEWORK_STORAGE  ,  cacheDir  )  ; 
} 
String   enableHook  =  configProps  .  getProperty  (  SHUTDOWN_HOOK_PROP  )  ; 
if  (  (  enableHook  ==  null  )  ||  !  enableHook  .  equalsIgnoreCase  (  "false"  )  )  { 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   Thread  (  "Felix Shutdown Hook"  )  { 

public   void   run  (  )  { 
try  { 
if  (  m_fwk  !=  null  )  { 
m_fwk  .  stop  (  )  ; 
m_fwk  .  waitForStop  (  0  )  ; 
} 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  "Error stopping framework: "  +  ex  )  ; 
} 
} 
}  )  ; 
} 
try  { 
FrameworkFactory   factory  =  getFrameworkFactory  (  )  ; 
m_fwk  =  factory  .  newFramework  (  configProps  )  ; 
m_fwk  .  init  (  )  ; 
AutoProcessor  .  process  (  configProps  ,  m_fwk  .  getBundleContext  (  )  )  ; 
FrameworkEvent   event  ; 
do  { 
m_fwk  .  start  (  )  ; 
event  =  m_fwk  .  waitForStop  (  0  )  ; 
}  while  (  event  .  getType  (  )  ==  FrameworkEvent  .  STOPPED_UPDATE  )  ; 
System  .  exit  (  0  )  ; 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  "Could not create framework: "  +  ex  )  ; 
ex  .  printStackTrace  (  )  ; 
System  .  exit  (  0  )  ; 
} 
} 








private   static   FrameworkFactory   getFrameworkFactory  (  )  throws   Exception  { 
URL   url  =  Main  .  class  .  getClassLoader  (  )  .  getResource  (  "META-INF/services/org.osgi.framework.launch.FrameworkFactory"  )  ; 
if  (  url  !=  null  )  { 
BufferedReader   br  =  new   BufferedReader  (  new   InputStreamReader  (  url  .  openStream  (  )  )  )  ; 
try  { 
for  (  String   s  =  br  .  readLine  (  )  ;  s  !=  null  ;  s  =  br  .  readLine  (  )  )  { 
s  =  s  .  trim  (  )  ; 
if  (  (  s  .  length  (  )  >  0  )  &&  (  s  .  charAt  (  0  )  !=  '#'  )  )  { 
return  (  FrameworkFactory  )  Class  .  forName  (  s  )  .  newInstance  (  )  ; 
} 
} 
}  finally  { 
if  (  br  !=  null  )  br  .  close  (  )  ; 
} 
} 
throw   new   Exception  (  "Could not find framework factory."  )  ; 
} 















public   static   void   loadSystemProperties  (  )  { 
URL   propURL  =  null  ; 
String   custom  =  System  .  getProperty  (  SYSTEM_PROPERTIES_PROP  )  ; 
if  (  custom  !=  null  )  { 
try  { 
propURL  =  new   URL  (  custom  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
System  .  err  .  print  (  "Main: "  +  ex  )  ; 
return  ; 
} 
}  else  { 
File   confDir  =  null  ; 
String   classpath  =  System  .  getProperty  (  "java.class.path"  )  ; 
int   index  =  classpath  .  toLowerCase  (  )  .  indexOf  (  "felix.jar"  )  ; 
int   start  =  classpath  .  lastIndexOf  (  File  .  pathSeparator  ,  index  )  +  1  ; 
if  (  index  >=  start  )  { 
String   jarLocation  =  classpath  .  substring  (  start  ,  index  )  ; 
confDir  =  new   File  (  new   File  (  new   File  (  jarLocation  )  .  getAbsolutePath  (  )  )  .  getParent  (  )  ,  CONFIG_DIRECTORY  )  ; 
}  else  { 
confDir  =  new   File  (  System  .  getProperty  (  "user.dir"  )  ,  CONFIG_DIRECTORY  )  ; 
} 
try  { 
propURL  =  new   File  (  confDir  ,  SYSTEM_PROPERTIES_FILE_VALUE  )  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
System  .  err  .  print  (  "Main: "  +  ex  )  ; 
return  ; 
} 
} 
Properties   props  =  new   Properties  (  )  ; 
InputStream   is  =  null  ; 
try  { 
is  =  propURL  .  openConnection  (  )  .  getInputStream  (  )  ; 
props  .  load  (  is  )  ; 
is  .  close  (  )  ; 
}  catch  (  FileNotFoundException   ex  )  { 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  "Main: Error loading system properties from "  +  propURL  )  ; 
System  .  err  .  println  (  "Main: "  +  ex  )  ; 
try  { 
if  (  is  !=  null  )  is  .  close  (  )  ; 
}  catch  (  IOException   ex2  )  { 
} 
return  ; 
} 
for  (  Enumeration   e  =  props  .  propertyNames  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   name  =  (  String  )  e  .  nextElement  (  )  ; 
System  .  setProperty  (  name  ,  Util  .  substVars  (  props  .  getProperty  (  name  )  ,  name  ,  null  ,  null  )  )  ; 
} 
} 

















public   static   Properties   loadConfigProperties  (  )  { 
URL   propURL  =  null  ; 
String   custom  =  System  .  getProperty  (  CONFIG_PROPERTIES_PROP  )  ; 
if  (  custom  !=  null  )  { 
try  { 
propURL  =  new   URL  (  custom  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
System  .  err  .  print  (  "Main: "  +  ex  )  ; 
return   null  ; 
} 
}  else  { 
File   confDir  =  null  ; 
String   classpath  =  System  .  getProperty  (  "java.class.path"  )  ; 
int   index  =  classpath  .  toLowerCase  (  )  .  indexOf  (  "felix.jar"  )  ; 
int   start  =  classpath  .  lastIndexOf  (  File  .  pathSeparator  ,  index  )  +  1  ; 
if  (  index  >=  start  )  { 
String   jarLocation  =  classpath  .  substring  (  start  ,  index  )  ; 
confDir  =  new   File  (  new   File  (  new   File  (  jarLocation  )  .  getAbsolutePath  (  )  )  .  getParent  (  )  ,  CONFIG_DIRECTORY  )  ; 
}  else  { 
confDir  =  new   File  (  System  .  getProperty  (  "user.dir"  )  ,  CONFIG_DIRECTORY  )  ; 
} 
try  { 
propURL  =  new   File  (  confDir  ,  CONFIG_PROPERTIES_FILE_VALUE  )  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   ex  )  { 
System  .  err  .  print  (  "Main: "  +  ex  )  ; 
return   null  ; 
} 
} 
Properties   props  =  new   Properties  (  )  ; 
InputStream   is  =  null  ; 
try  { 
is  =  propURL  .  openConnection  (  )  .  getInputStream  (  )  ; 
props  .  load  (  is  )  ; 
is  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
try  { 
if  (  is  !=  null  )  is  .  close  (  )  ; 
}  catch  (  IOException   ex2  )  { 
} 
return   null  ; 
} 
for  (  Enumeration   e  =  props  .  propertyNames  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   name  =  (  String  )  e  .  nextElement  (  )  ; 
props  .  setProperty  (  name  ,  Util  .  substVars  (  props  .  getProperty  (  name  )  ,  name  ,  null  ,  props  )  )  ; 
} 
return   props  ; 
} 

public   static   void   copySystemProperties  (  Properties   configProps  )  { 
for  (  Enumeration   e  =  System  .  getProperties  (  )  .  propertyNames  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   key  =  (  String  )  e  .  nextElement  (  )  ; 
if  (  key  .  startsWith  (  "felix."  )  ||  key  .  startsWith  (  "org.osgi.framework."  )  )  { 
configProps  .  setProperty  (  key  ,  System  .  getProperty  (  key  )  )  ; 
} 
} 
} 
} 

