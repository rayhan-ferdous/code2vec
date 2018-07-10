package   gatech  .  mmpm  .  learningengine  ; 

import   gatech  .  mmpm  .  ConfigurationException  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 











public   class   MEExecutorFactory  { 











public   static   IMEExecutor   BuildMEExecutor  (  String   info  ,  gatech  .  mmpm  .  IDomain   idomain  )  throws   ConfigurationException  { 
IMEExecutor   meExecutor  =  null  ; 
String  [  ]  splitInfo  =  info  .  split  (  "@@@"  )  ; 
if  (  (  splitInfo  .  length  <  3  )  ||  (  splitInfo  .  length  >  4  )  )  throw   new   ConfigurationException  (  "Unexpected Format. It should be: MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]"  )  ; 
meExecutor  =  BuildMEExecutorInternal  (  splitInfo  [  0  ]  ,  "gatech.mmpm.learningengine.IMEExecutor"  )  ; 
if  (  splitInfo  .  length  >  3  )  { 
LazyMEExecutor   lazyMEExecutor  =  (  LazyMEExecutor  )  BuildMEExecutorInternal  (  splitInfo  [  3  ]  ,  "gatech.mmpm.learningengine.LazyMEExecutor"  )  ; 
lazyMEExecutor  .  setMEOrig  (  meExecutor  )  ; 
meExecutor  =  lazyMEExecutor  ; 
} 
java  .  io  .  InputStream   me  =  null  ; 
if  (  splitInfo  [  1  ]  .  equals  (  "file"  )  )  try  { 
me  =  new   java  .  io  .  FileInputStream  (  splitInfo  [  2  ]  )  ; 
}  catch  (  Exception   e  )  { 
try  { 
URL   url  =  new   URL  (  splitInfo  [  2  ]  )  ; 
me  =  url  .  openStream  (  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ConfigurationException  (  splitInfo  [  2  ]  +  ": File not found."  )  ; 
} 
}  else   if  (  splitInfo  [  1  ]  .  equals  (  "zipfile"  )  )  try  { 
me  =  getMEFromZipFile  (  splitInfo  [  2  ]  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ConfigurationException  (  splitInfo  [  2  ]  +  ": File not found."  )  ; 
}  else   throw   new   ConfigurationException  (  splitInfo  [  1  ]  +  " is not a valid way of ME retrieval."  )  ; 
meExecutor  .  loadME  (  me  ,  idomain  )  ; 
return   meExecutor  ; 
} 

private   static   java  .  io  .  InputStream   getMEFromZipFile  (  String   fileName  )  throws   Exception  { 
java  .  io  .  InputStream   me  =  null  ; 
try  { 
me  =  new   java  .  io  .  FileInputStream  (  fileName  )  ; 
}  catch  (  Exception   ex  )  { 
URL   url  =  new   URL  (  fileName  )  ; 
me  =  url  .  openStream  (  )  ; 
} 
java  .  util  .  zip  .  ZipInputStream   zf  ; 
zf  =  new   java  .  util  .  zip  .  ZipInputStream  (  me  )  ; 
zf  .  getNextEntry  (  )  ; 
me  =  zf  ; 
return   me  ; 
} 











private   static   IMEExecutor   BuildMEExecutorInternal  (  String   askedClassName  ,  String   baseClassName  )  throws   ConfigurationException  { 
IMEExecutor   meExecutor  =  null  ; 
Class  <  ?  extends   IMEExecutor  >  concreteMEExecutorClass  ; 
Class  <  ?  extends   IMEExecutor  >  baseClass  ; 
Class  <  IMEExecutor  >  IMEExecutorInterface  =  IMEExecutor  .  class  ; 
Class  <  ?  >  askedClass  ; 
try  { 
askedClass  =  Class  .  forName  (  baseClassName  )  ; 
}  catch  (  java  .  lang  .  ClassNotFoundException   e  )  { 
throw   new   ConfigurationException  (  askedClassName  +  " not found."  )  ; 
} 
try  { 
baseClass  =  askedClass  .  asSubclass  (  IMEExecutorInterface  )  ; 
}  catch  (  java  .  lang  .  ClassCastException   e  )  { 
throw   new   ConfigurationException  (  askedClass  .  getName  (  )  +  " does not implements "  +  IMEExecutorInterface  .  getName  (  )  )  ; 
} 
try  { 
askedClass  =  Class  .  forName  (  askedClassName  )  ; 
}  catch  (  java  .  lang  .  ClassNotFoundException   e  )  { 
throw   new   ConfigurationException  (  askedClassName  +  " not found."  )  ; 
} 
try  { 
concreteMEExecutorClass  =  askedClass  .  asSubclass  (  baseClass  )  ; 
}  catch  (  java  .  lang  .  ClassCastException   e  )  { 
throw   new   ConfigurationException  (  askedClass  .  getName  (  )  +  " does not implements "  +  baseClassName  )  ; 
} 
try  { 
meExecutor  =  concreteMEExecutorClass  .  newInstance  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ConfigurationException  (  concreteMEExecutorClass  .  getName  (  )  +  " cannot be instanciated."  )  ; 
} 
return   meExecutor  ; 
} 








public   static   String   getUserFriendlyHelp  (  )  { 
System  .  out  .  println  (  "\t                The String needed for creating a MEExecutor that loads a Me must be:"  )  ; 
System  .  out  .  println  (  "\t                MEExecutorClassName@@@MEStoredway@@@MEDirection[@@@DecoratorClassName]"  )  ; 
System  .  out  .  println  (  "\t                Where: MEExecutorClassName is the class of the concrete MEExecutor."  )  ; 
System  .  out  .  println  (  "\t                       MEStoredway must be: file|zipfile|[other ways in the future]."  )  ; 
System  .  out  .  println  (  "\t                       MEDirection, the place in which the ME is located (file, etc.)."  )  ; 
System  .  out  .  println  (  "\t                       [optional] DecoratorClassName is the class of the decorator"  )  ; 
return  ""  ; 
} 
} 

