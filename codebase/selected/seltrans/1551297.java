package   com  .  volantis  .  devrep  .  repository  .  accessors  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   com  .  volantis  .  mcs  .  accessors  .  xml  .  ZipArchive  ; 
import   com  .  volantis  .  devrep  .  repository  .  api  .  accessors  .  xml  .  EclipseEntityResolver  ; 
import   com  .  volantis  .  mcs  .  repository  .  RepositoryException  ; 
import   com  .  volantis  .  synergetics  .  io  .  IOUtils  ; 
import   com  .  volantis  .  xml  .  schema  .  JarFileEntityResolver  ; 
import   com  .  volantis  .  xml  .  schema  .  W3CSchemata  ; 
import   com  .  volantis  .  synergetics  .  cornerstone  .  utilities  .  xml  .  jaxp  .  TransformerMetaFactory  ; 
import   com  .  volantis  .  synergetics  .  log  .  LogDispatcher  ; 
import   com  .  volantis  .  devrep  .  localization  .  LocalizationFactory  ; 
import   com  .  volantis  .  synergetics  .  UndeclaredThrowableException  ; 
import   com  .  volantis  .  synergetics  .  localization  .  ExceptionLocalizer  ; 
import   com  .  volantis  .  devrep  .  device  .  api  .  xml  .  DeviceSchemas  ; 
import   com  .  volantis  .  devrep  .  repository  .  api  .  accessors  .  xml  .  DeviceRepositoryConstants  ; 
import   com  .  volantis  .  devrep  .  repository  .  api  .  devices  .  DeviceRepositorySchemaConstants  ; 
import   org  .  jdom  .  Document  ; 
import   org  .  jdom  .  Element  ; 
import   org  .  jdom  .  JDOMException  ; 
import   org  .  jdom  .  Namespace  ; 
import   org  .  jdom  .  input  .  DefaultJDOMFactory  ; 
import   org  .  jdom  .  input  .  JDOMFactory  ; 
import   org  .  jdom  .  input  .  SAXBuilder  ; 
import   org  .  jdom  .  output  .  XMLOutputter  ; 
import   org  .  xml  .  sax  .  XMLReader  ; 
import   org  .  xml  .  sax  .  XMLFilter  ; 
















public   class   EclipseDeviceRepository  { 




private   static   String   mark  =  "(c) Volantis Systems Ltd 2004. "  ; 




private   static   final   LogDispatcher   logger  =  LocalizationFactory  .  createLogger  (  EclipseDeviceRepository  .  class  )  ; 




private   static   final   ExceptionLocalizer   exceptionLocalizer  =  LocalizationFactory  .  createExceptionLocalizer  (  EclipseDeviceRepository  .  class  )  ; 




private   static   final   String   XSI  =  "xsi"  ; 




private   static   final   String   SCHEMA_LOCATION  =  "schemaLocation"  ; 




public   static   final   DeletionFilter   STANDARD_ELEMENT_FILTER  =  new   DeletionFilter  (  new   DeletionFilter  .  NodeIdentifier  [  ]  {  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  DEVICE_CURRENT  .  getNamespaceURL  (  )  )  ,  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  IDENTIFICATION_CURRENT  .  getNamespaceURL  (  )  )  ,  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  TAC_IDENTIFICATION_CURRENT  .  getNamespaceURL  (  )  )  }  )  ; 





private   boolean   validation  ; 




private   boolean   ignoreWhitespace  ; 




private   ZipArchive   repositoryArchive  ; 




private   Document   xmlDefinitionsDocument  ; 




private   Document   xmlHierarchyDocument  =  null  ; 




private   Document   xmlIdentificationDocument  =  null  ; 




private   Document   xmlTACIdentificationDocument  =  null  ; 




private   Properties   properties  ; 





private   String   standardPropertiesPath  ; 





private   String   customPropertiesPath  ; 




private   String   version  ; 




private   String   revision  ; 




private   JDOMFactory   factory  ; 




private   XMLFilter   xmlFilter  ; 












public   EclipseDeviceRepository  (  String   repositoryFilename  ,  TransformerMetaFactory   transformerMetaFactory  ,  JDOMFactory   jdomFactory  ,  XMLFilter   xmlFilter  )  throws   RepositoryException  { 
this  (  repositoryFilename  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  xmlFilter  )  ; 
} 












public   EclipseDeviceRepository  (  MDPRArchiveAccessor   mdprAccessor  ,  JDOMFactory   factory  )  throws   RepositoryException  { 
this  (  mdprAccessor  ,  factory  ,  true  ,  true  ,  null  )  ; 
} 
















public   EclipseDeviceRepository  (  String   repositoryFilename  ,  TransformerMetaFactory   transformerMetaFactory  ,  JDOMFactory   jdomFactory  ,  boolean   validation  ,  boolean   ignoreWhitespace  ,  XMLFilter   xmlFilter  )  throws   RepositoryException  { 
this  (  new   MDPRArchiveAccessor  (  repositoryFilename  ,  transformerMetaFactory  )  ,  jdomFactory  ,  validation  ,  ignoreWhitespace  ,  xmlFilter  )  ; 
} 
















public   EclipseDeviceRepository  (  MDPRArchiveAccessor   mdprAccessor  ,  JDOMFactory   factory  ,  boolean   validation  ,  boolean   ignoreWhitespace  ,  XMLFilter   xmlFilter  )  throws   RepositoryException  { 
if  (  mdprAccessor  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Archive accessor cannot be null."  )  ; 
} 
if  (  factory  ==  null  )  { 
throw   new   IllegalArgumentException  (  "factory cannot be null."  )  ; 
} 
repositoryArchive  =  mdprAccessor  .  getArchive  (  )  ; 
version  =  retrieveVersion  (  repositoryArchive  )  ; 
this  .  factory  =  factory  ; 
this  .  validation  =  validation  ; 
this  .  ignoreWhitespace  =  ignoreWhitespace  ; 
this  .  xmlFilter  =  xmlFilter  ; 
InputStream   input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  STANDARD_DEFINITIONS_XML  )  ; 
if  (  input  !=  null  )  { 
xmlDefinitionsDocument  =  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  CUSTOM_DEFINITIONS_XML  )  ; 
if  (  input  !=  null  )  { 
Document   customDefinitions  =  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
mergeDefinitionDocuments  (  xmlDefinitionsDocument  ,  customDefinitions  )  ; 
} 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  DEFINITIONS_XML  )  )  ; 
} 
input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  HIERARCHY_XML  )  ; 
if  (  input  !=  null  )  { 
xmlHierarchyDocument  =  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  HIERARCHY_XML  )  )  ; 
} 
input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  IDENTIFICATION_XML  )  ; 
if  (  input  !=  null  )  { 
xmlIdentificationDocument  =  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  IDENTIFICATION_XML  )  )  ; 
} 
input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  TAC_IDENTIFICATION_XML  )  ; 
if  (  input  !=  null  )  { 
xmlTACIdentificationDocument  =  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
}  else  { 
} 
revision  =  retrieveRevision  (  repositoryArchive  ,  factory  )  ; 
try  { 
properties  =  createMergedProperties  (  repositoryArchive  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  "policies.properties"  )  ,  ioe  )  ; 
} 
} 









private   static   String   retrieveRevision  (  ZipArchive   repositoryArchive  ,  JDOMFactory   factory  )  throws   RepositoryException  { 
InputStream   input  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  REPOSITORY_XML  )  ; 
return   retrieveRevision  (  input  ,  factory  )  ; 
} 








private   static   String   retrieveRevision  (  InputStream   is  ,  JDOMFactory   factory  )  throws   RepositoryException  { 
String   revision  =  null  ; 
if  (  is  !=  null  )  { 
Document   repositoryDocument  =  createNewDocument  (  new   BufferedInputStream  (  is  )  ,  factory  ,  true  ,  true  )  ; 
revision  =  repositoryDocument  .  getRootElement  (  )  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  REVISION_ATTRIBUTE_NAME  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  REPOSITORY_XML  )  )  ; 
} 
return   revision  ; 
} 









private   static   String   retrieveVersion  (  ZipArchive   repositoryArchive  )  throws   RepositoryException  { 
InputStream   is  =  null  ; 
String   version  =  null  ; 
try  { 
is  =  repositoryArchive  .  getInputFrom  (  DeviceRepositoryConstants  .  VERSION_FILENAME  )  ; 
if  (  is  ==  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  VERSION_FILENAME  )  )  ; 
} 
version  =  retrieveVersion  (  is  )  ; 
return   version  ; 
}  finally  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "unexpected-ioexception"  ,  e  )  ; 
if  (  version  !=  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "unexpected-ioexception"  )  ,  e  )  ; 
} 
} 
} 
} 
} 







private   static   String   retrieveVersion  (  InputStream   is  )  throws   RepositoryException  { 
ByteArrayOutputStream   buffer  =  new   ByteArrayOutputStream  (  )  ; 
try  { 
IOUtils  .  copy  (  is  ,  buffer  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  DeviceRepositoryConstants  .  VERSION_FILENAME  )  ,  e  )  ; 
} 
return   buffer  .  toString  (  )  .  trim  (  )  ; 
} 








public   void   setRepositoryName  (  String   filename  )  { 
repositoryArchive  .  setArchiveFilename  (  filename  )  ; 
} 





public   Document   getDeviceHierarchyDocument  (  )  { 
return   xmlHierarchyDocument  ; 
} 





public   Document   getDeviceIdentificationDocument  (  )  { 
return   xmlIdentificationDocument  ; 
} 






public   Document   getDeviceTACIdentificationDocument  (  )  { 
return   xmlTACIdentificationDocument  ; 
} 





public   Document   getDevicePolicyDefinitions  (  )  { 
return   xmlDefinitionsDocument  ; 
} 














private   void   writeFile  (  ZipArchive   archive  ,  String   directory  ,  String   filename  ,  Document   document  )  throws   RepositoryException  { 
String   path  =  getXMLFilePath  (  directory  ,  filename  )  ; 
OutputStream   output  =  archive  .  getOutputTo  (  path  )  ; 
try  { 
writeDocument  (  document  ,  output  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-update-failure"  ,  path  )  ,  e  )  ; 
} 
} 















public   void   moveDevice  (  String   device  ,  String   newParentDevice  )  throws   RepositoryException  { 
if  (  device  ==  null  )  { 
throw   new   IllegalArgumentException  (  "device cannot be null"  )  ; 
} 
if  (  newParentDevice  ==  null  )  { 
throw   new   IllegalArgumentException  (  "newParentDevice cannot be null"  )  ; 
} 
Element   deviceElement  =  getHierarchyDeviceElement  (  device  )  ; 
if  (  deviceElement  ==  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-definition-missing"  ,  device  )  )  ; 
} 
Element   parentElement  =  getHierarchyDeviceElement  (  newParentDevice  )  ; 
if  (  parentElement  ==  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-definition-missing"  ,  newParentDevice  )  )  ; 
} 
Element   oldParent  =  deviceElement  .  getParent  (  )  ; 
try  { 
deviceElement  .  detach  (  )  ; 
parentElement  .  addContent  (  deviceElement  )  ; 
}  finally  { 
if  (  deviceElement  .  getParent  (  )  ==  null  )  { 
oldParent  .  addContent  (  deviceElement  )  ; 
} 
} 
} 












private   String   getXMLFilePath  (  String   directory  ,  String   filename  )  { 
StringBuffer   result  =  new   StringBuffer  (  directory  )  ; 
if  (  !  (  directory  .  endsWith  (  "/"  )  ||  directory  .  endsWith  (  "\\"  )  )  )  { 
result  .  append  (  File  .  separator  )  ; 
} 
result  .  append  (  filename  )  ; 
if  (  !  (  filename  .  endsWith  (  DeviceRepositoryConstants  .  XML_SUFFIX  )  )  )  { 
result  .  append  (  DeviceRepositoryConstants  .  XML_SUFFIX  )  ; 
} 
return   result  .  toString  (  )  ; 
} 





public   String   getVersion  (  )  { 
return   version  ; 
} 





public   String   getRevision  (  )  { 
return   revision  ; 
} 









public   Element   retrieveDeviceIdentificationElement  (  String   deviceName  )  { 
return   retrieveDeviceElementFromDocument  (  deviceName  ,  xmlIdentificationDocument  )  ; 
} 









public   Element   retrieveTACDeviceElement  (  String   deviceName  )  { 
return   retrieveDeviceElementFromDocument  (  deviceName  ,  xmlTACIdentificationDocument  )  ; 
} 















private   Element   retrieveDeviceElementFromDocument  (  String   deviceName  ,  Document   document  )  { 
if  (  deviceName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "deviceName cannot be null"  )  ; 
} 
Element   idElement  =  null  ; 
if  (  document  !=  null  )  { 
List   children  =  document  .  getRootElement  (  )  .  getChildren  (  )  ; 
Iterator   iterator  =  children  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  &&  idElement  ==  null  )  { 
Element   child  =  (  Element  )  iterator  .  next  (  )  ; 
String   childDeviceName  =  child  .  getAttributeValue  (  "name"  )  ; 
if  (  childDeviceName  .  equals  (  deviceName  )  )  { 
idElement  =  child  ; 
} 
} 
} 
return   idElement  ; 
} 






public   static   String   getCustomDeviceNamePrefix  (  )  { 
return   DeviceRepositoryConstants  .  CUSTOM_DEVICE_NAME_PREFIX  ; 
} 






public   static   String   getCustomPolicyNamePrefix  (  )  { 
return   DeviceRepositoryConstants  .  CUSTOM_POLICY_NAME_PREFIX  ; 
} 









public   static   boolean   isStandardDevice  (  String   deviceName  )  { 
return  !  deviceName  .  startsWith  (  getCustomDeviceNamePrefix  (  )  )  ; 
} 









public   Element   retrieveDeviceElement  (  String   deviceName  )  throws   RepositoryException  { 
Document   deviceDocument  =  null  ; 
Element   hierarchyDeviceElement  =  getHierarchyDeviceElement  (  deviceName  )  ; 
if  (  hierarchyDeviceElement  !=  null  )  { 
String   src  =  getXMLFilePath  (  DeviceRepositoryConstants  .  STANDARD_DEVICE_DIRECTORY  ,  deviceName  )  ; 
InputStream   standardInput  =  repositoryArchive  .  getInputFrom  (  src  )  ; 
if  (  standardInput  !=  null  )  { 
deviceDocument  =  createNewDocument  (  standardInput  )  ; 
src  =  getXMLFilePath  (  DeviceRepositoryConstants  .  CUSTOM_DEVICE_DIRECTORY  ,  deviceName  )  ; 
InputStream   customInput  =  repositoryArchive  .  getInputFrom  (  src  )  ; 
if  (  customInput  !=  null  )  { 
Document   customDocument  =  createNewDocument  (  customInput  )  ; 
mergeDeviceDocuments  (  deviceDocument  ,  customDocument  )  ; 
} 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-definition-missing"  ,  deviceName  )  )  ; 
} 
Element   device  =  deviceDocument  .  getRootElement  (  )  ; 
String   deviceNameAttr  =  device  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
if  (  deviceNameAttr  ==  null  ||  deviceNameAttr  .  length  (  )  ==  0  )  { 
device  .  setAttribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  deviceName  )  ; 
} 
} 
return   deviceDocument  .  getRootElement  (  )  ; 
} 













private   void   mergeDefinitionDocuments  (  Document   standard  ,  Document   custom  )  { 
Element   sDefs  =  standard  .  getRootElement  (  )  ; 
Element   cDefs  =  custom  .  getRootElement  (  )  ; 
List   cChildren  =  cDefs  .  getChildren  (  )  ; 
while  (  cChildren  .  size  (  )  >  0  )  { 
Element   child  =  (  Element  )  cChildren  .  get  (  0  )  ; 
child  .  detach  (  )  ; 
if  (  !  (  DeviceRepositorySchemaConstants  .  POLICY_DEFINITION_TYPES_ELEMENT_NAME  .  equals  (  child  .  getName  (  )  )  )  )  { 
sDefs  .  addContent  (  child  )  ; 
} 
} 
} 








private   void   mergeDeviceDocuments  (  Document   standard  ,  Document   custom  )  { 
Element   sDevice  =  standard  .  getRootElement  (  )  ; 
Element   sPolicies  =  sDevice  .  getChild  (  DeviceRepositorySchemaConstants  .  POLICIES_ELEMENT_NAME  ,  sDevice  .  getNamespace  (  )  )  ; 
Element   cDevice  =  custom  .  getRootElement  (  )  ; 
Element   cPolicies  =  cDevice  .  getChild  (  DeviceRepositorySchemaConstants  .  POLICIES_ELEMENT_NAME  ,  cDevice  .  getNamespace  (  )  )  ; 
List   cChildren  =  cPolicies  .  getChildren  (  )  ; 
while  (  cChildren  .  size  (  )  >  0  )  { 
Element   cPolicy  =  (  Element  )  cChildren  .  get  (  0  )  ; 
cPolicy  .  detach  (  )  ; 
sPolicies  .  addContent  (  cPolicy  )  ; 
} 
} 


















public   void   removeDevice  (  String   deviceName  )  throws   RepositoryException  { 
if  (  deviceName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "deviceName cannot be null"  )  ; 
} 
Element   element  =  getHierarchyDeviceElement  (  deviceName  )  ; 
if  (  element  !=  null  )  { 
removeDevice  (  element  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "cannot-delete-device"  ,  deviceName  )  )  ; 
} 
} 
















private   void   removeDevice  (  Element   element  )  throws   RepositoryException  { 
List   children  =  new   ArrayList  (  element  .  getChildren  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  size  (  )  ;  i  ++  )  { 
Element   child  =  (  Element  )  children  .  get  (  i  )  ; 
removeDevice  (  child  )  ; 
} 
String   deviceName  =  element  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEVICE_DIRECTORY  ,  true  ,  deviceName  )  ; 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEVICE_DIRECTORY  ,  false  ,  deviceName  )  ; 
element  .  detach  (  )  ; 
removeDeviceIdentifiers  (  deviceName  )  ; 
removeDeviceTACs  (  deviceName  )  ; 
repositoryArchive  =  archive  ; 
} 











private   void   removeDeviceIdentifiers  (  String   deviceName  )  { 
Element   identifiers  =  retrieveDeviceIdentificationElement  (  deviceName  )  ; 
if  (  identifiers  !=  null  )  { 
identifiers  .  detach  (  )  ; 
} 
} 











private   void   removeDeviceTACs  (  String   deviceName  )  { 
Element   tacs  =  retrieveTACDeviceElement  (  deviceName  )  ; 
if  (  tacs  !=  null  )  { 
tacs  .  detach  (  )  ; 
} 
} 












private   void   renameDeviceIdentifiers  (  String   oldName  ,  String   newName  )  { 
Element   identifiers  =  retrieveDeviceIdentificationElement  (  oldName  )  ; 
if  (  identifiers  !=  null  )  { 
identifiers  .  setAttribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  newName  )  ; 
} 
} 











private   void   renameDeviceTACs  (  String   oldName  ,  String   newName  )  { 
Element   tacs  =  retrieveTACDeviceElement  (  oldName  )  ; 
if  (  tacs  !=  null  )  { 
tacs  .  setAttribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  newName  )  ; 
} 
} 



















private   void   removeFile  (  ZipArchive   archive  ,  String   directory  ,  boolean   mandatory  ,  String   filename  )  throws   RepositoryException  { 
String   path  =  getXMLFilePath  (  directory  ,  filename  )  ; 
boolean   deleted  =  archive  .  delete  (  path  )  ; 
if  (  mandatory  &&  !  deleted  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "cannot-delete-file"  ,  path  )  )  ; 
} 
} 










public   void   addDeviceElement  (  String   deviceName  )  throws   RepositoryException  { 
if  (  deviceName  ==  null  ||  deviceName  .  trim  (  )  .  length  (  )  ==  0  )  { 
throw   new   IllegalArgumentException  (  "deviceName cannot be null or empty"  )  ; 
} 
Map   map  =  new   HashMap  (  )  ; 
map  .  put  (  deviceName  ,  createEmptyDeviceDocument  (  )  .  getRootElement  (  )  )  ; 
writeDeviceElements  (  map  )  ; 
} 










public   void   addIdentifiersDeviceElement  (  String   deviceName  )  throws   RepositoryException  { 
addDeviceElementToDocumentRoot  (  deviceName  ,  xmlIdentificationDocument  )  ; 
} 










public   void   addTACDeviceElement  (  String   deviceName  )  throws   RepositoryException  { 
addDeviceElementToDocumentRoot  (  deviceName  ,  xmlTACIdentificationDocument  )  ; 
} 









private   void   addDeviceElementToDocumentRoot  (  String   deviceName  ,  Document   document  )  throws   RepositoryException  { 
if  (  deviceName  ==  null  ||  deviceName  .  trim  (  )  .  length  (  )  ==  0  )  { 
throw   new   IllegalArgumentException  (  "deviceName cannot be null or empty"  )  ; 
} 
Element   existing  =  retrieveDeviceElementFromDocument  (  deviceName  ,  document  )  ; 
if  (  existing  !=  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-already-exists"  ,  deviceName  )  )  ; 
} 
Element   root  =  document  .  getRootElement  (  )  ; 
root  .  addContent  (  createDeviceElement  (  deviceName  ,  root  .  getNamespace  (  )  )  )  ; 
} 













public   void   renameDevice  (  String   deviceName  ,  String   newDeviceName  )  throws   RepositoryException  { 
if  (  deviceName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Cannot rename a null device name."  )  ; 
} 
if  (  newDeviceName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Cannot rename a device name to null name."  )  ; 
} 
if  (  deviceExists  (  newDeviceName  )  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-already-exists-cannot-rename"  ,  deviceName  )  )  ; 
} 
Element   element  =  getHierarchyDeviceElement  (  deviceName  )  ; 
if  (  element  ==  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-not-found-cannot-rename"  ,  deviceName  )  )  ; 
} 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
Element   parent  =  element  .  getParent  (  )  ; 
element  .  detach  (  )  ; 
try  { 
renameDeviceFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEVICE_DIRECTORY  ,  true  ,  deviceName  ,  newDeviceName  )  ; 
renameDeviceFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEVICE_DIRECTORY  ,  false  ,  deviceName  ,  newDeviceName  )  ; 
renameDeviceIdentifiers  (  deviceName  ,  newDeviceName  )  ; 
renameDeviceTACs  (  deviceName  ,  newDeviceName  )  ; 
element  .  setAttribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  newDeviceName  )  ; 
repositoryArchive  =  archive  ; 
}  finally  { 
parent  .  addContent  (  element  )  ; 
} 
} 





















private   void   renameDeviceFile  (  ZipArchive   archive  ,  String   deviceDirectory  ,  boolean   mandatory  ,  String   deviceName  ,  String   newDeviceName  )  throws   RepositoryException  { 
String   devicePath  =  getXMLFilePath  (  deviceDirectory  ,  deviceName  )  ; 
if  (  mandatory  ||  archive  .  exists  (  devicePath  )  )  { 
String   newDevicePath  =  getXMLFilePath  (  deviceDirectory  ,  newDeviceName  )  ; 
if  (  !  archive  .  rename  (  devicePath  ,  newDevicePath  )  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-rename-failed"  ,  new   Object  [  ]  {  devicePath  ,  newDevicePath  }  )  )  ; 
} 
Document   deviceDoc  =  createNewDocument  (  archive  .  getInputFrom  (  newDevicePath  )  )  ; 
Element   deviceElement  =  deviceDoc  .  getRootElement  (  )  ; 
deviceElement  .  setAttribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  newDeviceName  )  ; 
try  { 
writeDocument  (  deviceDoc  ,  archive  .  getOutputTo  (  newDevicePath  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-update-failure"  ,  newDevicePath  )  ,  e  )  ; 
} 
} 
} 








public   List   getChildDeviceNames  (  String   deviceName  )  { 
List   list  =  null  ; 
Element   element  =  getHierarchyDeviceElement  (  deviceName  )  ; 
if  (  element  !=  null  )  { 
List   children  =  element  .  getChildren  (  )  ; 
if  (  children  !=  null  &&  children  .  size  (  )  >  0  )  { 
list  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  size  (  )  ;  i  ++  )  { 
Element   child  =  (  Element  )  children  .  get  (  i  )  ; 
if  (  DeviceRepositorySchemaConstants  .  DEVICE_ELEMENT_NAME  .  equals  (  child  .  getName  (  )  )  )  { 
list  .  add  (  child  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  )  ; 
} 
} 
} 
} 
return   list  ; 
} 






public   String   getRootDeviceName  (  )  throws   RepositoryException  { 
String   result  =  null  ; 
if  (  xmlHierarchyDocument  !=  null  )  { 
Element   root  =  xmlHierarchyDocument  .  getRootElement  (  )  ; 
List   list  =  root  .  getChildren  (  )  ; 
if  (  list  !=  null  )  { 
if  (  list  .  size  (  )  ==  1  )  { 
result  =  (  (  Element  )  list  .  get  (  0  )  )  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
}  else   if  (  list  .  size  (  )  >  1  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-hierarchy-too-many-roots"  ,  list  )  )  ; 
} 
} 
} 
return   result  ; 
} 









public   String   getFallbackDeviceName  (  String   deviceName  )  { 
String   result  =  null  ; 
Element   element  =  getHierarchyDeviceElement  (  deviceName  )  ; 
if  (  (  element  !=  null  )  &&  !  element  .  isRootElement  (  )  )  { 
result  =  element  .  getParent  (  )  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
} 
return   result  ; 
} 









public   boolean   deviceExists  (  String   deviceName  )  { 
return   getHierarchyDeviceElement  (  deviceName  )  !=  null  ; 
} 






public   void   saveRepositoryArchive  (  )  throws   RepositoryException  { 
try  { 
repositoryArchive  .  save  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 











public   void   writeIdentifiers  (  )  throws   RepositoryException  { 
try  { 
if  (  xmlIdentificationDocument  !=  null  )  { 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
OutputStream   out  =  archive  .  getOutputTo  (  DeviceRepositoryConstants  .  IDENTIFICATION_XML  )  ; 
writeDocument  (  xmlIdentificationDocument  ,  out  )  ; 
repositoryArchive  =  archive  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 











public   void   writeTACs  (  )  throws   RepositoryException  { 
try  { 
if  (  xmlTACIdentificationDocument  !=  null  )  { 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
OutputStream   out  =  archive  .  getOutputTo  (  DeviceRepositoryConstants  .  TAC_IDENTIFICATION_XML  )  ; 
writeDocument  (  xmlTACIdentificationDocument  ,  out  )  ; 
repositoryArchive  =  archive  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 











public   void   writeHierarchy  (  )  throws   RepositoryException  { 
try  { 
if  (  xmlHierarchyDocument  !=  null  )  { 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
OutputStream   out  =  archive  .  getOutputTo  (  DeviceRepositoryConstants  .  HIERARCHY_XML  )  ; 
writeDocument  (  xmlHierarchyDocument  ,  out  )  ; 
repositoryArchive  =  archive  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-invalid"  ,  DeviceRepositoryConstants  .  HIERARCHY_XML  )  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 












public   void   writeDefinitions  (  )  throws   RepositoryException  { 
Document   standardDefDoc  =  createDocument  (  DeviceRepositorySchemaConstants  .  DEFINITIONS_ELEMENT_NAME  ,  DeviceSchemas  .  POLICY_DEFINITIONS_CURRENT  .  getNamespaceURL  (  )  ,  DeviceSchemas  .  POLICY_DEFINITIONS_CURRENT  .  getLocationURL  (  )  )  ; 
Element   standardDefRoot  =  standardDefDoc  .  getRootElement  (  )  ; 
Document   customDefDoc  =  createDocument  (  DeviceRepositorySchemaConstants  .  DEFINITIONS_ELEMENT_NAME  ,  DeviceSchemas  .  POLICY_DEFINITIONS_CURRENT  .  getNamespaceURL  (  )  ,  DeviceSchemas  .  POLICY_DEFINITIONS_CURRENT  .  getLocationURL  (  )  )  ; 
Element   customDefRoot  =  customDefDoc  .  getRootElement  (  )  ; 
boolean   hasCustomDefs  =  false  ; 
Element   defRoot  =  xmlDefinitionsDocument  .  getRootElement  (  )  ; 
List   children  =  defRoot  .  getChildren  (  )  ; 
Element   element  =  null  ; 
Element   clone  =  null  ; 
for  (  int   i  =  0  ;  i  <  children  .  size  (  )  ;  i  ++  )  { 
element  =  (  Element  )  children  .  get  (  i  )  ; 
if  (  DeviceRepositorySchemaConstants  .  CATEGORY_ELEMENT_NAME  .  equals  (  element  .  getName  (  )  )  )  { 
clone  =  (  Element  )  element  .  clone  (  )  ; 
String   catName  =  clone  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  CATEGORY_NAME_ATTRIBUTE  )  ; 
if  (  catName  !=  null  &&  catName  .  equals  (  DeviceRepositorySchemaConstants  .  CUSTOM_CATEGORY_NAME  )  )  { 
customDefRoot  .  addContent  (  clone  )  ; 
hasCustomDefs  =  true  ; 
}  else  { 
standardDefRoot  .  addContent  (  clone  )  ; 
} 
}  else   if  (  DeviceRepositorySchemaConstants  .  POLICY_DEFINITION_TYPES_ELEMENT_NAME  .  equals  (  element  .  getName  (  )  )  )  { 
clone  =  (  Element  )  element  .  clone  (  )  ; 
standardDefRoot  .  addContent  (  clone  )  ; 
clone  =  (  Element  )  element  .  clone  (  )  ; 
customDefRoot  .  addContent  (  clone  )  ; 
} 
} 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEFINITIONS_DIRECTORY  ,  false  ,  DeviceRepositorySchemaConstants  .  DEFINITIONS_DOCUMENT_NAME  )  ; 
if  (  hasCustomDefs  )  { 
writeFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEFINITIONS_DIRECTORY  ,  DeviceRepositorySchemaConstants  .  DEFINITIONS_DOCUMENT_NAME  ,  customDefDoc  )  ; 
} 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEFINITIONS_DIRECTORY  ,  false  ,  DeviceRepositorySchemaConstants  .  DEFINITIONS_DOCUMENT_NAME  )  ; 
writeFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEFINITIONS_DIRECTORY  ,  DeviceRepositorySchemaConstants  .  DEFINITIONS_DOCUMENT_NAME  ,  standardDefDoc  )  ; 
repositoryArchive  =  archive  ; 
} 













public   void   writeDeviceElements  (  Map   devices  )  throws   RepositoryException  { 
for  (  Iterator   i  =  devices  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   deviceName  =  (  String  )  i  .  next  (  )  ; 
Element   deviceElement  =  (  Element  )  devices  .  get  (  deviceName  )  ; 
if  (  deviceElement  !=  null  )  { 
Document   standardDeviceDocument  =  createEmptyDeviceDocument  (  )  ; 
Document   customDeviceDocument  =  null  ; 
Element   policiesElement  =  deviceElement  .  getChild  (  DeviceRepositorySchemaConstants  .  POLICIES_ELEMENT_NAME  ,  deviceElement  .  getNamespace  (  )  )  ; 
if  (  policiesElement  !=  null  )  { 
for  (  Iterator   j  =  policiesElement  .  getChildren  (  )  .  iterator  (  )  ;  j  .  hasNext  (  )  ;  )  { 
Element   policyElement  =  (  Element  )  j  .  next  (  )  ; 
if  (  policyElement  !=  null  )  { 
Element   clone  =  (  Element  )  policyElement  .  clone  (  )  ; 
String   name  =  policyElement  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  POLICY_NAME_ATTRIBUTE  )  ; 
Document   target  =  standardDeviceDocument  ; 
if  (  name  !=  null  &&  name  .  startsWith  (  DeviceRepositoryConstants  .  CUSTOM_POLICY_NAME_PREFIX  )  )  { 
if  (  customDeviceDocument  ==  null  )  { 
customDeviceDocument  =  createEmptyDeviceDocument  (  )  ; 
} 
target  =  customDeviceDocument  ; 
} 
target  .  getRootElement  (  )  .  getChild  (  DeviceRepositorySchemaConstants  .  POLICIES_ELEMENT_NAME  ,  deviceElement  .  getNamespace  (  )  )  .  addContent  (  clone  )  ; 
} 
} 
} 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEVICE_DIRECTORY  ,  false  ,  deviceName  )  ; 
if  (  customDeviceDocument  !=  null  )  { 
writeFile  (  archive  ,  DeviceRepositoryConstants  .  CUSTOM_DEVICE_DIRECTORY  ,  deviceName  ,  customDeviceDocument  )  ; 
} 
removeFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEVICE_DIRECTORY  ,  false  ,  deviceName  )  ; 
writeFile  (  archive  ,  DeviceRepositoryConstants  .  STANDARD_DEVICE_DIRECTORY  ,  deviceName  ,  standardDeviceDocument  )  ; 
repositoryArchive  =  archive  ; 
} 
} 
} 






private   Document   createEmptyDeviceDocument  (  )  { 
Document   document  =  createDocument  (  DeviceRepositorySchemaConstants  .  DEVICE_ELEMENT_NAME  ,  DeviceSchemas  .  DEVICE_CURRENT  .  getNamespaceURL  (  )  ,  DeviceSchemas  .  DEVICE_CURRENT  .  getLocationURL  (  )  )  ; 
Element   device  =  document  .  getRootElement  (  )  ; 
device  .  addContent  (  factory  .  element  (  DeviceRepositorySchemaConstants  .  POLICIES_ELEMENT_NAME  ,  device  .  getNamespace  (  )  )  )  ; 
return   document  ; 
} 






public   void   writeProperties  (  )  throws   RepositoryException  { 
OutputStream   standardPropsOut  =  null  ; 
OutputStream   customPropsOut  =  null  ; 
try  { 
if  (  properties  !=  null  )  { 
Properties   customProps  =  new   Properties  (  )  ; 
Properties   standardProps  =  new   Properties  (  )  ; 
Enumeration   allPropertyNames  =  properties  .  propertyNames  (  )  ; 
while  (  allPropertyNames  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  allPropertyNames  .  nextElement  (  )  ; 
String   value  =  properties  .  getProperty  (  name  )  ; 
if  (  name  .  startsWith  (  DeviceRepositoryConstants  .  CUSTOM_POLICY_RESOURCE_PREFIX  )  )  { 
customProps  .  setProperty  (  name  ,  value  )  ; 
}  else  { 
standardProps  .  setProperty  (  name  ,  value  )  ; 
} 
} 
ZipArchive   archive  =  new   ZipArchive  (  repositoryArchive  )  ; 
standardPropsOut  =  archive  .  getOutputTo  (  standardPropertiesPath  )  ; 
try  { 
standardProps  .  store  (  standardPropsOut  ,  null  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
if  (  customProps  .  size  (  )  >  0  )  { 
if  (  customPropertiesPath  ==  null  )  { 
customPropertiesPath  =  DeviceRepositoryConstants  .  CUSTOM_POLICIES_PROPERTIES_PREFIX  +  DeviceRepositoryConstants  .  POLICIES_PROPERTIES_SUFFIX  ; 
} 
customPropsOut  =  archive  .  getOutputTo  (  customPropertiesPath  )  ; 
try  { 
customProps  .  store  (  customPropsOut  ,  null  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 
repositoryArchive  =  archive  ; 
} 
}  finally  { 
if  (  standardPropsOut  !=  null  )  { 
try  { 
standardPropsOut  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 
if  (  customPropsOut  !=  null  )  { 
try  { 
customPropsOut  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
} 
} 
} 







public   Element   getHierarchyDeviceElement  (  String   deviceName  )  { 
return   getDeviceElement  (  xmlHierarchyDocument  .  getRootElement  (  )  ,  deviceName  )  ; 
} 











public   void   addHierarchyDeviceElement  (  String   deviceName  ,  String   fallback  )  throws   RepositoryException  { 
if  (  deviceName  ==  null  ||  deviceName  .  trim  (  )  .  length  (  )  ==  0  )  { 
throw   new   IllegalArgumentException  (  "deviceName cannot be null or empty."  )  ; 
} 
if  (  deviceExists  (  deviceName  )  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-already-exists"  ,  deviceName  )  )  ; 
} 
Element   parent  =  null  ; 
if  (  fallback  ==  null  )  { 
parent  =  xmlHierarchyDocument  .  getRootElement  (  )  ; 
List   children  =  parent  .  getChildren  (  )  ; 
if  (  (  children  !=  null  )  &&  (  children  .  size  (  )  >  0  )  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-hierarchy-only-one-root"  )  )  ; 
} 
}  else  { 
parent  =  getHierarchyDeviceElement  (  fallback  )  ; 
} 
if  (  parent  !=  null  )  { 
parent  .  addContent  (  createDeviceElement  (  deviceName  ,  xmlHierarchyDocument  .  getRootElement  (  )  .  getNamespace  (  )  )  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-fallback-invalid"  ,  new   Object  [  ]  {  fallback  ,  deviceName  }  )  )  ; 
} 
} 







private   Element   createDeviceElement  (  String   name  ,  Namespace   namespace  )  { 
Element   device  =  factory  .  element  (  DeviceRepositorySchemaConstants  .  DEVICE_ELEMENT_NAME  ,  namespace  )  ; 
device  .  setAttribute  (  factory  .  attribute  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  ,  name  )  )  ; 
return   device  ; 
} 










private   Element   getDeviceElement  (  Element   parent  ,  String   deviceName  )  { 
Element   result  =  null  ; 
if  (  parent  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Parent cannot be null."  )  ; 
} 
if  (  deviceName  !=  null  )  { 
List   namedChildren  =  parent  .  getChildren  (  )  ; 
if  (  namedChildren  !=  null  )  { 
for  (  int   i  =  0  ;  (  result  ==  null  )  &&  (  i  <  namedChildren  .  size  (  )  )  ;  i  ++  )  { 
Element   child  =  (  Element  )  namedChildren  .  get  (  i  )  ; 
String   attributeValue  =  child  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
String   name  =  child  .  getName  (  )  ; 
if  (  DeviceRepositorySchemaConstants  .  DEVICE_ELEMENT_NAME  .  equals  (  name  )  &&  deviceName  .  equals  (  attributeValue  )  )  { 
result  =  child  ; 
} 
} 
for  (  int   i  =  0  ;  (  result  ==  null  )  &&  (  i  <  namedChildren  .  size  (  )  )  ;  i  ++  )  { 
result  =  getDeviceElement  (  (  Element  )  namedChildren  .  get  (  i  )  ,  deviceName  )  ; 
} 
} 
} 
return   result  ; 
} 











private   Document   createDocument  (  String   rootName  ,  String   namespaceURI  ,  String   schemaURI  )  { 
Element   root  =  factory  .  element  (  rootName  ,  namespaceURI  )  ; 
Namespace   xsi  =  Namespace  .  getNamespace  (  XSI  ,  W3CSchemata  .  XSI_NAMESPACE  )  ; 
root  .  addNamespaceDeclaration  (  xsi  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  namespaceURI  .  length  (  )  +  schemaURI  .  length  (  )  +  1  )  ; 
buffer  .  append  (  namespaceURI  )  .  append  (  ' '  )  .  append  (  schemaURI  )  ; 
root  .  setAttribute  (  SCHEMA_LOCATION  ,  buffer  .  toString  (  )  ,  xsi  )  ; 
return   factory  .  document  (  root  )  ; 
} 










public   Document   createNewDocument  (  InputStream   inputStream  )  throws   RepositoryException  { 
return   createNewDocument  (  inputStream  ,  factory  ,  validation  ,  ignoreWhitespace  )  ; 
} 














private   static   Document   createNewDocument  (  InputStream   inputStream  ,  JDOMFactory   factory  ,  boolean   validation  ,  boolean   ignoreWhitespace  )  throws   RepositoryException  { 
Document   result  =  null  ; 
SAXBuilder   builder  =  new   SAXBuilder  (  )  { 

protected   XMLReader   createParser  (  )  throws   JDOMException  { 
XMLReader   parser  =  new   com  .  volantis  .  xml  .  xerces  .  parsers  .  SAXParser  (  )  ; 
setFeaturesAndProperties  (  parser  ,  true  )  ; 
return   parser  ; 
} 
}  ; 
builder  .  setFeature  (  "http://xml.org/sax/features/namespaces"  ,  true  )  ; 
builder  .  setFactory  (  factory  )  ; 
builder  .  setXMLFilter  (  new   DefaultNamespaceAdapterFilter  (  DeviceRepositoryConstants  .  DEFAULT_NAMESPACE_PREFIX  )  )  ; 
if  (  validation  )  { 
builder  .  setValidation  (  true  )  ; 
builder  .  setFeature  (  "http://apache.org/xml/features/validation/"  +  "schema-full-checking"  ,  true  )  ; 
builder  .  setFeature  (  "http://xml.org/sax/features/validation"  ,  true  )  ; 
builder  .  setFeature  (  "http://apache.org/xml/features/validation/"  +  "schema"  ,  true  )  ; 
builder  .  setIgnoringElementContentWhitespace  (  ignoreWhitespace  )  ; 
JarFileEntityResolver   resolver  =  new   EclipseEntityResolver  (  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  DEVICE_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  CORE_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  HEIRARCHY_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  IDENTIFICATION_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  TAC_IDENTIFICATION_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  POLICY_DEFINITIONS_CURRENT  )  ; 
resolver  .  addSystemIdMapping  (  DeviceSchemas  .  REPOSITORY_CURRENT  )  ; 
builder  .  setEntityResolver  (  resolver  )  ; 
}  else  { 
builder  .  setValidation  (  false  )  ; 
} 
try  { 
result  =  builder  .  build  (  inputStream  )  ; 
}  catch  (  JDOMException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RepositoryException  (  e  )  ; 
} 
return   result  ; 
} 








private   void   writeDocument  (  Document   document  ,  OutputStream   out  )  throws   IOException  { 
XMLOutputter   outputter  =  new   XMLOutputter  (  )  ; 
Document   currentDocument  =  document  ; 
if  (  xmlFilter  !=  null  )  { 
SAXBuilder   parser  =  new   SAXBuilder  (  )  ; 
parser  .  setXMLFilter  (  xmlFilter  )  ; 
try  { 
currentDocument  =  parser  .  build  (  new   StringReader  (  outputter  .  outputString  (  document  )  )  )  ; 
}  catch  (  JDOMException   e  )  { 
e  .  printStackTrace  (  )  ; 
throw   new   UndeclaredThrowableException  (  e  ,  "Could not build the a filtered document."  )  ; 
} 
} 
outputter  .  setIndent  (  "    "  )  ; 
outputter  .  setTextTrim  (  true  )  ; 
outputter  .  setNewlines  (  true  )  ; 
outputter  .  output  (  currentDocument  ,  out  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
} 




private   void   dumpHierarchy  (  StringBuffer   buffer  )  { 
buffer  .  append  (  "\nXML Device Repository Hierarchy\n"  )  ; 
buffer  .  append  (  "-------------------------------\n"  )  ; 
String   rootDeviceName  =  null  ; 
try  { 
rootDeviceName  =  getRootDeviceName  (  )  ; 
dumpHierarchy  (  buffer  ,  rootDeviceName  ,  0  )  ; 
}  catch  (  RepositoryException   e  )  { 
e  .  printStackTrace  (  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Could not obtain root device name."  ,  e  )  ; 
} 
} 
} 




private   void   dumpHierarchy  (  StringBuffer   buffer  ,  String   deviceName  ,  int   level  )  { 
for  (  int   i  =  0  ;  i  <  level  ;  i  ++  )  { 
buffer  .  append  (  "    "  )  ; 
} 
buffer  .  append  (  deviceName  )  .  append  (  "\n"  )  ; 
List   list  =  getChildDeviceNames  (  deviceName  )  ; 
++  level  ; 
if  (  list  !=  null  )  { 
Iterator   iterator  =  list  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
String   s  =  (  String  )  iterator  .  next  (  )  ; 
dumpHierarchy  (  buffer  ,  s  ,  level  )  ; 
} 
} 
} 






public   String   toString  (  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
dumpHierarchy  (  buffer  )  ; 
return   buffer  .  toString  (  )  ; 
} 












public   static   String   getRepositoryVersion  (  File   repositoryFile  )  throws   RepositoryException  { 
return   queryRepository  (  repositoryFile  ,  DeviceRepositoryConstants  .  VERSION_FILENAME  ,  new   DeviceRepositoryQuery  (  )  { 

public   String   doQuery  (  InputStream   zipEntryStream  )  throws   RepositoryException  { 
return   retrieveVersion  (  zipEntryStream  )  ; 
} 
}  )  ; 
} 












public   static   String   getRepositoryRevision  (  File   repositoryFile  )  throws   RepositoryException  { 
return   queryRepository  (  repositoryFile  ,  DeviceRepositoryConstants  .  REPOSITORY_XML  ,  new   DeviceRepositoryQuery  (  )  { 

public   String   doQuery  (  InputStream   zipEntryStream  )  throws   RepositoryException  { 
return   retrieveRevision  (  zipEntryStream  ,  new   DefaultJDOMFactory  (  )  )  ; 
} 
}  )  ; 
} 











private   static   String   queryRepository  (  File   repositoryFile  ,  String   zipEntryName  ,  DeviceRepositoryQuery   query  )  throws   RepositoryException  { 
if  (  !  repositoryFile  .  canRead  (  )  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "file-cannot-be-read"  ,  repositoryFile  )  )  ; 
} 
InputStream   is  =  null  ; 
String   queryResult  =  null  ; 
try  { 
ZipFile   repositoryZip  =  new   ZipFile  (  repositoryFile  ,  ZipFile  .  OPEN_READ  )  ; 
ZipEntry   versionEntry  =  repositoryZip  .  getEntry  (  zipEntryName  )  ; 
is  =  repositoryZip  .  getInputStream  (  versionEntry  )  ; 
queryResult  =  query  .  doQuery  (  is  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "unexpected-ioexception"  ,  e  )  ; 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "unexpected-ioexception"  )  ,  e  )  ; 
}  finally  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  "unexpected-ioexception"  ,  e  )  ; 
if  (  queryResult  !=  null  )  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "unexpected-ioexception"  )  ,  e  )  ; 
} 
} 
} 
} 
return   queryResult  ; 
} 





public   static   String   getStandardPolicyResourcePrefix  (  )  { 
return   DeviceRepositoryConstants  .  STANDARD_POLICY_RESOURCE_PREFIX  ; 
} 





private   interface   DeviceRepositoryQuery  { 








public   String   doQuery  (  InputStream   zipEntryStream  )  throws   RepositoryException  ; 
} 



















private   String   populateProperties  (  ZipArchive   deviceRepository  ,  String   prefix  ,  Properties   properties  )  throws   IOException  { 
Locale   locale  =  Locale  .  getDefault  (  )  ; 
String   language  =  locale  .  getLanguage  (  )  ; 
String   country  =  locale  .  getCountry  (  )  ; 
String   variant  =  locale  .  getVariant  (  )  ; 
String   propertiesFile  =  null  ; 
if  (  language  .  length  (  )  !=  0  &&  country  .  length  (  )  !=  0  &&  variant  .  length  (  )  !=  0  )  { 
String   checkPath  =  getPropertiesPath  (  prefix  ,  language  ,  country  ,  variant  )  ; 
if  (  deviceRepository  .  exists  (  checkPath  )  )  { 
propertiesFile  =  checkPath  ; 
} 
} 
if  (  propertiesFile  ==  null  &&  language  .  length  (  )  !=  0  &&  country  .  length  (  )  !=  0  )  { 
String   checkPath  =  getPropertiesPath  (  prefix  ,  language  ,  country  ,  null  )  ; 
if  (  deviceRepository  .  exists  (  checkPath  )  )  { 
propertiesFile  =  checkPath  ; 
} 
} 
if  (  propertiesFile  ==  null  &&  language  .  length  (  )  !=  0  )  { 
String   checkPath  =  getPropertiesPath  (  prefix  ,  language  ,  null  ,  null  )  ; 
if  (  deviceRepository  .  exists  (  checkPath  )  )  { 
propertiesFile  =  checkPath  ; 
} 
} 
if  (  propertiesFile  ==  null  )  { 
String   checkPath  =  getPropertiesPath  (  prefix  ,  null  ,  null  ,  null  )  ; 
if  (  deviceRepository  .  exists  (  checkPath  )  )  { 
propertiesFile  =  checkPath  ; 
} 
} 
if  (  propertiesFile  !=  null  )  { 
properties  .  load  (  deviceRepository  .  getInputFrom  (  propertiesFile  )  )  ; 
}  else   if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Could not locate the properties file for the prefix "  +  prefix  )  ; 
} 
return   propertiesFile  ; 
} 










private   String   getPropertiesPath  (  String   prefix  ,  String   language  ,  String   country  ,  String   variant  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  prefix  )  ; 
if  (  language  !=  null  &&  language  .  length  (  )  !=  0  )  { 
buffer  .  append  (  '_'  )  .  append  (  language  )  ; 
if  (  country  !=  null  &&  country  .  length  (  )  !=  0  )  { 
buffer  .  append  (  '_'  )  .  append  (  country  )  ; 
if  (  variant  !=  null  &&  variant  .  length  (  )  !=  0  )  { 
buffer  .  append  (  '_'  )  .  append  (  variant  )  ; 
} 
} 
} 
buffer  .  append  (  DeviceRepositoryConstants  .  POLICIES_PROPERTIES_SUFFIX  )  ; 
return   buffer  .  toString  (  )  ; 
} 










private   Properties   createMergedProperties  (  ZipArchive   deviceRepository  )  throws   IOException  { 
Properties   allProps  =  new   Properties  (  )  ; 
Properties   customProps  =  new   Properties  (  )  ; 
standardPropertiesPath  =  populateProperties  (  deviceRepository  ,  DeviceRepositoryConstants  .  STANDARD_POLICIES_PROPERTIES_PREFIX  ,  allProps  )  ; 
if  (  standardPropertiesPath  ==  null  )  { 
throw   new   IOException  (  "Error reading standard properties file."  )  ; 
} 
customPropertiesPath  =  populateProperties  (  deviceRepository  ,  DeviceRepositoryConstants  .  CUSTOM_POLICIES_PROPERTIES_PREFIX  ,  customProps  )  ; 
if  (  customPropertiesPath  !=  null  )  { 
Enumeration   customEnum  =  customProps  .  propertyNames  (  )  ; 
while  (  customEnum  .  hasMoreElements  (  )  )  { 
String   propertyName  =  (  String  )  customEnum  .  nextElement  (  )  ; 
String   value  =  customProps  .  getProperty  (  propertyName  )  ; 
allProps  .  setProperty  (  propertyName  ,  value  )  ; 
} 
} 
return   allProps  ; 
} 





public   Properties   getProperties  (  )  { 
return   properties  ; 
} 

















public   static   Document   getDeviceHierarchyDocument  (  String   deviceRepository  ,  TransformerMetaFactory   transformerMetaFactory  ,  JDOMFactory   jdomFactory  )  throws   RepositoryException  { 
return   getDocument  (  deviceRepository  ,  transformerMetaFactory  ,  jdomFactory  ,  DeviceRepositoryConstants  .  HIERARCHY_XML  )  ; 
} 

















public   static   Document   getDeviceIdentificationDocument  (  String   deviceRepository  ,  TransformerMetaFactory   transformerMetaFactory  ,  JDOMFactory   jdomFactory  )  throws   RepositoryException  { 
return   getDocument  (  deviceRepository  ,  transformerMetaFactory  ,  jdomFactory  ,  DeviceRepositoryConstants  .  IDENTIFICATION_XML  )  ; 
} 


















private   static   Document   getDocument  (  String   deviceRepository  ,  TransformerMetaFactory   transformerMetaFactory  ,  JDOMFactory   jdomFactory  ,  String   documentFile  )  throws   RepositoryException  { 
Document   document  =  null  ; 
MDPRArchiveAccessor   accessor  =  new   MDPRArchiveAccessor  (  deviceRepository  ,  transformerMetaFactory  )  ; 
InputStream   input  =  accessor  .  getArchiveEntryInputStream  (  documentFile  )  ; 
if  (  input  !=  null  )  { 
document  =  createNewDocument  (  new   BufferedInputStream  (  input  )  ,  jdomFactory  ,  false  ,  false  )  ; 
}  else  { 
throw   new   RepositoryException  (  exceptionLocalizer  .  format  (  "device-repository-file-missing"  ,  documentFile  )  )  ; 
} 
return   document  ; 
} 







protected   static   String   getRequiredMDPRVersion  (  )  { 
return   DeviceRepositoryConstants  .  VERSION  ; 
} 
} 

