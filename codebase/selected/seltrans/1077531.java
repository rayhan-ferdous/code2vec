package   org  .  jomc  .  ant  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  net  .  SocketTimeoutException  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  logging  .  Level  ; 
import   javax  .  xml  .  bind  .  JAXBElement  ; 
import   javax  .  xml  .  bind  .  JAXBException  ; 
import   javax  .  xml  .  bind  .  Marshaller  ; 
import   javax  .  xml  .  bind  .  Unmarshaller  ; 
import   javax  .  xml  .  bind  .  util  .  JAXBResult  ; 
import   javax  .  xml  .  bind  .  util  .  JAXBSource  ; 
import   javax  .  xml  .  transform  .  Source  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerConfigurationException  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamSource  ; 
import   org  .  apache  .  tools  .  ant  .  BuildException  ; 
import   org  .  apache  .  tools  .  ant  .  Project  ; 
import   org  .  jomc  .  ant  .  types  .  NameType  ; 
import   org  .  jomc  .  ant  .  types  .  ResourceType  ; 
import   org  .  jomc  .  ant  .  types  .  TransformerResourceType  ; 
import   org  .  jomc  .  model  .  Module  ; 
import   org  .  jomc  .  model  .  Modules  ; 
import   org  .  jomc  .  model  .  ObjectFactory  ; 
import   org  .  jomc  .  model  .  modlet  .  DefaultModelProvider  ; 
import   org  .  jomc  .  modlet  .  ModelContext  ; 
import   org  .  jomc  .  modlet  .  ModelException  ; 
import   org  .  jomc  .  modlet  .  ModelValidationReport  ; 







public   final   class   MergeModulesTask   extends   JomcModelTask  { 


private   String   moduleEncoding  ; 


private   File   moduleFile  ; 


private   String   moduleName  ; 


private   String   moduleVersion  ; 


private   String   moduleVendor  ; 


private   Set  <  NameType  >  moduleIncludes  ; 


private   Set  <  NameType  >  moduleExcludes  ; 


private   List  <  TransformerResourceType  >  modelObjectStylesheetResources  ; 


public   MergeModulesTask  (  )  { 
super  (  )  ; 
} 








public   File   getModuleFile  (  )  { 
return   this  .  moduleFile  ; 
} 








public   void   setModuleFile  (  final   File   value  )  { 
this  .  moduleFile  =  value  ; 
} 








public   String   getModuleEncoding  (  )  { 
if  (  this  .  moduleEncoding  ==  null  )  { 
this  .  moduleEncoding  =  new   OutputStreamWriter  (  new   ByteArrayOutputStream  (  )  )  .  getEncoding  (  )  ; 
} 
return   this  .  moduleEncoding  ; 
} 








public   void   setModuleEncoding  (  final   String   value  )  { 
this  .  moduleEncoding  =  value  ; 
} 








public   String   getModuleName  (  )  { 
return   this  .  moduleName  ; 
} 








public   void   setModuleName  (  final   String   value  )  { 
this  .  moduleName  =  value  ; 
} 








public   String   getModuleVersion  (  )  { 
return   this  .  moduleVersion  ; 
} 








public   void   setModuleVersion  (  final   String   value  )  { 
this  .  moduleVersion  =  value  ; 
} 








public   String   getModuleVendor  (  )  { 
return   this  .  moduleVendor  ; 
} 








public   void   setModuleVendor  (  final   String   value  )  { 
this  .  moduleVendor  =  value  ; 
} 











public   Set  <  NameType  >  getModuleIncludes  (  )  { 
if  (  this  .  moduleIncludes  ==  null  )  { 
this  .  moduleIncludes  =  new   HashSet  <  NameType  >  (  )  ; 
} 
return   this  .  moduleIncludes  ; 
} 








public   NameType   createModuleInclude  (  )  { 
final   NameType   moduleInclude  =  new   NameType  (  )  ; 
this  .  getModuleIncludes  (  )  .  add  (  moduleInclude  )  ; 
return   moduleInclude  ; 
} 











public   Set  <  NameType  >  getModuleExcludes  (  )  { 
if  (  this  .  moduleExcludes  ==  null  )  { 
this  .  moduleExcludes  =  new   HashSet  <  NameType  >  (  )  ; 
} 
return   this  .  moduleExcludes  ; 
} 








public   NameType   createModuleExclude  (  )  { 
final   NameType   moduleExclude  =  new   NameType  (  )  ; 
this  .  getModuleExcludes  (  )  .  add  (  moduleExclude  )  ; 
return   moduleExclude  ; 
} 











public   List  <  TransformerResourceType  >  getModelObjectStylesheetResources  (  )  { 
if  (  this  .  modelObjectStylesheetResources  ==  null  )  { 
this  .  modelObjectStylesheetResources  =  new   LinkedList  <  TransformerResourceType  >  (  )  ; 
} 
return   this  .  modelObjectStylesheetResources  ; 
} 








public   TransformerResourceType   createModelObjectStylesheetResource  (  )  { 
final   TransformerResourceType   modelObjectStylesheetResource  =  new   TransformerResourceType  (  )  ; 
this  .  getModelObjectStylesheetResources  (  )  .  add  (  modelObjectStylesheetResource  )  ; 
return   modelObjectStylesheetResource  ; 
} 


@  Override 
public   void   preExecuteTask  (  )  throws   BuildException  { 
super  .  preExecuteTask  (  )  ; 
this  .  assertNotNull  (  "moduleFile"  ,  this  .  getModuleFile  (  )  )  ; 
this  .  assertNotNull  (  "moduleName"  ,  this  .  getModuleName  (  )  )  ; 
this  .  assertNamesNotNull  (  this  .  getModuleExcludes  (  )  )  ; 
this  .  assertNamesNotNull  (  this  .  getModuleIncludes  (  )  )  ; 
this  .  assertLocationsNotNull  (  this  .  getModelObjectStylesheetResources  (  )  )  ; 
} 






@  Override 
public   void   executeTask  (  )  throws   BuildException  { 
ProjectClassLoader   classLoader  =  null  ; 
boolean   suppressExceptionOnClose  =  true  ; 
try  { 
this  .  log  (  Messages  .  getMessage  (  "mergingModules"  ,  this  .  getModel  (  )  )  )  ; 
classLoader  =  this  .  newProjectClassLoader  (  )  ; 
final   Modules   modules  =  new   Modules  (  )  ; 
final   Set  <  ResourceType  >  resources  =  new   HashSet  <  ResourceType  >  (  this  .  getModuleResources  (  )  )  ; 
final   ModelContext   context  =  this  .  newModelContext  (  classLoader  )  ; 
final   Marshaller   marshaller  =  context  .  createMarshaller  (  this  .  getModel  (  )  )  ; 
final   Unmarshaller   unmarshaller  =  context  .  createUnmarshaller  (  this  .  getModel  (  )  )  ; 
if  (  this  .  isModelResourceValidationEnabled  (  )  )  { 
unmarshaller  .  setSchema  (  context  .  createSchema  (  this  .  getModel  (  )  )  )  ; 
} 
if  (  resources  .  isEmpty  (  )  )  { 
final   ResourceType   defaultResource  =  new   ResourceType  (  )  ; 
defaultResource  .  setLocation  (  DefaultModelProvider  .  getDefaultModuleLocation  (  )  )  ; 
defaultResource  .  setOptional  (  true  )  ; 
resources  .  add  (  defaultResource  )  ; 
} 
for  (  ResourceType   resource  :  resources  )  { 
final   URL  [  ]  urls  =  this  .  getResources  (  context  ,  resource  .  getLocation  (  )  )  ; 
if  (  urls  .  length  ==  0  )  { 
if  (  resource  .  isOptional  (  )  )  { 
this  .  logMessage  (  Level  .  WARNING  ,  Messages  .  getMessage  (  "moduleResourceNotFound"  ,  resource  .  getLocation  (  )  )  )  ; 
}  else  { 
throw   new   BuildException  (  Messages  .  getMessage  (  "moduleResourceNotFound"  ,  resource  .  getLocation  (  )  )  ,  this  .  getLocation  (  )  )  ; 
} 
} 
for  (  int   i  =  urls  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
InputStream   in  =  null  ; 
suppressExceptionOnClose  =  true  ; 
try  { 
this  .  logMessage  (  Level  .  FINEST  ,  Messages  .  getMessage  (  "reading"  ,  urls  [  i  ]  .  toExternalForm  (  )  )  )  ; 
final   URLConnection   con  =  urls  [  i  ]  .  openConnection  (  )  ; 
con  .  setConnectTimeout  (  resource  .  getConnectTimeout  (  )  )  ; 
con  .  setReadTimeout  (  resource  .  getReadTimeout  (  )  )  ; 
con  .  connect  (  )  ; 
in  =  con  .  getInputStream  (  )  ; 
final   Source   source  =  new   StreamSource  (  in  ,  urls  [  i  ]  .  toURI  (  )  .  toASCIIString  (  )  )  ; 
Object   o  =  unmarshaller  .  unmarshal  (  source  )  ; 
if  (  o   instanceof   JAXBElement  <  ?  >  )  { 
o  =  (  (  JAXBElement  <  ?  >  )  o  )  .  getValue  (  )  ; 
} 
if  (  o   instanceof   Module  )  { 
modules  .  getModule  (  )  .  add  (  (  Module  )  o  )  ; 
}  else   if  (  o   instanceof   Modules  )  { 
modules  .  getModule  (  )  .  addAll  (  (  (  Modules  )  o  )  .  getModule  (  )  )  ; 
}  else  { 
this  .  log  (  Messages  .  getMessage  (  "unsupportedModuleResource"  ,  urls  [  i  ]  .  toExternalForm  (  )  )  ,  Project  .  MSG_WARN  )  ; 
} 
suppressExceptionOnClose  =  false  ; 
}  catch  (  final   SocketTimeoutException   e  )  { 
String   message  =  Messages  .  getMessage  (  e  )  ; 
message  =  Messages  .  getMessage  (  "resourceTimeout"  ,  message  !=  null  ?  " "  +  message  :  ""  )  ; 
if  (  resource  .  isOptional  (  )  )  { 
this  .  getProject  (  )  .  log  (  message  ,  e  ,  Project  .  MSG_WARN  )  ; 
}  else  { 
throw   new   BuildException  (  message  ,  e  ,  this  .  getLocation  (  )  )  ; 
} 
}  catch  (  final   IOException   e  )  { 
String   message  =  Messages  .  getMessage  (  e  )  ; 
message  =  Messages  .  getMessage  (  "resourceFailure"  ,  message  !=  null  ?  " "  +  message  :  ""  )  ; 
if  (  resource  .  isOptional  (  )  )  { 
this  .  getProject  (  )  .  log  (  message  ,  e  ,  Project  .  MSG_WARN  )  ; 
}  else  { 
throw   new   BuildException  (  message  ,  e  ,  this  .  getLocation  (  )  )  ; 
} 
}  finally  { 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
}  catch  (  final   IOException   e  )  { 
if  (  suppressExceptionOnClose  )  { 
this  .  logMessage  (  Level  .  SEVERE  ,  Messages  .  getMessage  (  e  )  ,  e  )  ; 
}  else  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
} 
} 
} 
} 
suppressExceptionOnClose  =  true  ; 
} 
for  (  final   Iterator  <  Module  >  it  =  modules  .  getModule  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
final   Module   module  =  it  .  next  (  )  ; 
if  (  !  this  .  isModuleIncluded  (  module  )  ||  this  .  isModuleExcluded  (  module  )  )  { 
it  .  remove  (  )  ; 
this  .  log  (  Messages  .  getMessage  (  "excludingModule"  ,  module  .  getName  (  )  )  )  ; 
}  else  { 
this  .  log  (  Messages  .  getMessage  (  "includingModule"  ,  module  .  getName  (  )  )  )  ; 
} 
} 
Module   classpathModule  =  null  ; 
if  (  this  .  isModelObjectClasspathResolutionEnabled  (  )  )  { 
classpathModule  =  modules  .  getClasspathModule  (  Modules  .  getDefaultClasspathModuleName  (  )  ,  classLoader  )  ; 
if  (  classpathModule  !=  null  &&  modules  .  getModule  (  Modules  .  getDefaultClasspathModuleName  (  )  )  ==  null  )  { 
modules  .  getModule  (  )  .  add  (  classpathModule  )  ; 
}  else  { 
classpathModule  =  null  ; 
} 
} 
final   ModelValidationReport   validationReport  =  context  .  validateModel  (  this  .  getModel  (  )  ,  new   JAXBSource  (  marshaller  ,  new   ObjectFactory  (  )  .  createModules  (  modules  )  )  )  ; 
this  .  logValidationReport  (  context  ,  validationReport  )  ; 
if  (  !  validationReport  .  isModelValid  (  )  )  { 
throw   new   ModelException  (  Messages  .  getMessage  (  "invalidModel"  ,  this  .  getModel  (  )  )  )  ; 
} 
if  (  classpathModule  !=  null  )  { 
modules  .  getModule  (  )  .  remove  (  classpathModule  )  ; 
} 
Module   mergedModule  =  modules  .  getMergedModule  (  this  .  getModuleName  (  )  )  ; 
mergedModule  .  setVendor  (  this  .  getModuleVendor  (  )  )  ; 
mergedModule  .  setVersion  (  this  .  getModuleVersion  (  )  )  ; 
for  (  int   i  =  0  ,  s0  =  this  .  getModelObjectStylesheetResources  (  )  .  size  (  )  ;  i  <  s0  ;  i  ++  )  { 
final   Transformer   transformer  =  this  .  getTransformer  (  this  .  getModelObjectStylesheetResources  (  )  .  get  (  i  )  )  ; 
if  (  transformer  !=  null  )  { 
final   JAXBSource   source  =  new   JAXBSource  (  marshaller  ,  new   ObjectFactory  (  )  .  createModule  (  mergedModule  )  )  ; 
final   JAXBResult   result  =  new   JAXBResult  (  unmarshaller  )  ; 
transformer  .  transform  (  source  ,  result  )  ; 
if  (  result  .  getResult  (  )  instanceof   JAXBElement  <  ?  >  &&  (  (  JAXBElement  <  ?  >  )  result  .  getResult  (  )  )  .  getValue  (  )  instanceof   Module  )  { 
mergedModule  =  (  Module  )  (  (  JAXBElement  <  ?  >  )  result  .  getResult  (  )  )  .  getValue  (  )  ; 
}  else  { 
throw   new   BuildException  (  Messages  .  getMessage  (  "illegalTransformationResult"  ,  this  .  getModelObjectStylesheetResources  (  )  .  get  (  i  )  .  getLocation  (  )  )  ,  this  .  getLocation  (  )  )  ; 
} 
} 
} 
this  .  log  (  Messages  .  getMessage  (  "writingEncoded"  ,  this  .  getModuleFile  (  )  .  getAbsolutePath  (  )  ,  this  .  getModuleEncoding  (  )  )  )  ; 
marshaller  .  setProperty  (  Marshaller  .  JAXB_ENCODING  ,  this  .  getModuleEncoding  (  )  )  ; 
marshaller  .  setProperty  (  Marshaller  .  JAXB_FORMATTED_OUTPUT  ,  Boolean  .  TRUE  )  ; 
marshaller  .  setSchema  (  context  .  createSchema  (  this  .  getModel  (  )  )  )  ; 
marshaller  .  marshal  (  new   ObjectFactory  (  )  .  createModule  (  mergedModule  )  ,  this  .  getModuleFile  (  )  )  ; 
suppressExceptionOnClose  =  false  ; 
}  catch  (  final   URISyntaxException   e  )  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
}  catch  (  final   JAXBException   e  )  { 
String   message  =  Messages  .  getMessage  (  e  )  ; 
if  (  message  ==  null  )  { 
message  =  Messages  .  getMessage  (  e  .  getLinkedException  (  )  )  ; 
} 
throw   new   BuildException  (  message  ,  e  ,  this  .  getLocation  (  )  )  ; 
}  catch  (  final   TransformerConfigurationException   e  )  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
}  catch  (  final   TransformerException   e  )  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
}  catch  (  final   ModelException   e  )  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
}  finally  { 
try  { 
if  (  classLoader  !=  null  )  { 
classLoader  .  close  (  )  ; 
} 
}  catch  (  final   IOException   e  )  { 
if  (  suppressExceptionOnClose  )  { 
this  .  logMessage  (  Level  .  SEVERE  ,  Messages  .  getMessage  (  e  )  ,  e  )  ; 
}  else  { 
throw   new   BuildException  (  Messages  .  getMessage  (  e  )  ,  e  ,  this  .  getLocation  (  )  )  ; 
} 
} 
} 
} 












public   boolean   isModuleIncluded  (  final   Module   module  )  { 
if  (  module  ==  null  )  { 
throw   new   NullPointerException  (  "module"  )  ; 
} 
for  (  NameType   include  :  this  .  getModuleIncludes  (  )  )  { 
if  (  include  .  getName  (  )  .  equals  (  module  .  getName  (  )  )  )  { 
return   true  ; 
} 
} 
return   this  .  getModuleIncludes  (  )  .  isEmpty  (  )  ?  true  :  false  ; 
} 












public   boolean   isModuleExcluded  (  final   Module   module  )  { 
if  (  module  ==  null  )  { 
throw   new   NullPointerException  (  "module"  )  ; 
} 
for  (  NameType   exclude  :  this  .  getModuleExcludes  (  )  )  { 
if  (  exclude  .  getName  (  )  .  equals  (  module  .  getName  (  )  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 


@  Override 
public   MergeModulesTask   clone  (  )  { 
final   MergeModulesTask   clone  =  (  MergeModulesTask  )  super  .  clone  (  )  ; 
clone  .  moduleFile  =  this  .  moduleFile  !=  null  ?  new   File  (  this  .  moduleFile  .  getAbsolutePath  (  )  )  :  null  ; 
if  (  this  .  moduleExcludes  !=  null  )  { 
clone  .  moduleExcludes  =  new   HashSet  <  NameType  >  (  this  .  moduleExcludes  .  size  (  )  )  ; 
for  (  NameType   e  :  this  .  moduleExcludes  )  { 
clone  .  moduleExcludes  .  add  (  e  .  clone  (  )  )  ; 
} 
} 
if  (  this  .  moduleIncludes  !=  null  )  { 
clone  .  moduleIncludes  =  new   HashSet  <  NameType  >  (  this  .  moduleIncludes  .  size  (  )  )  ; 
for  (  NameType   e  :  this  .  moduleIncludes  )  { 
clone  .  moduleIncludes  .  add  (  e  .  clone  (  )  )  ; 
} 
} 
if  (  this  .  modelObjectStylesheetResources  !=  null  )  { 
clone  .  modelObjectStylesheetResources  =  new   ArrayList  <  TransformerResourceType  >  (  this  .  modelObjectStylesheetResources  .  size  (  )  )  ; 
for  (  TransformerResourceType   e  :  this  .  modelObjectStylesheetResources  )  { 
clone  .  modelObjectStylesheetResources  .  add  (  e  .  clone  (  )  )  ; 
} 
} 
return   clone  ; 
} 
} 

