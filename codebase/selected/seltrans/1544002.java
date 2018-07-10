package   org  .  jfree  .  xml  .  generator  ; 

import   java  .  beans  .  BeanInfo  ; 
import   java  .  beans  .  IntrospectionException  ; 
import   java  .  beans  .  Introspector  ; 
import   java  .  beans  .  PropertyDescriptor  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   org  .  jfree  .  io  .  IOUtils  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  ClassDescription  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  Comments  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  DescriptionModel  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  IgnoredPropertyInfo  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  ManualMappingInfo  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  MultiplexMappingInfo  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  PropertyInfo  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  PropertyType  ; 
import   org  .  jfree  .  xml  .  generator  .  model  .  TypeInfo  ; 
import   org  .  jfree  .  xml  .  util  .  AbstractModelReader  ; 
import   org  .  jfree  .  xml  .  util  .  ObjectDescriptionException  ; 




public   class   DefaultModelReader   extends   AbstractModelReader  { 


private   DescriptionModel   model  ; 


private   ClassDescription   currentClassDescription  ; 


private   BeanInfo   currentBeanInfo  ; 


private   URL   baseURL  ; 


private   String   source  ; 


private   MultiplexMappingInfo   multiplexInfo  ; 


private   ArrayList   multiplexTypeInfos  ; 


private   ArrayList   propertyList  ; 


private   ArrayList   constructorList  ; 




public   DefaultModelReader  (  )  { 
super  (  )  ; 
} 











public   synchronized   DescriptionModel   load  (  final   String   file  )  throws   IOException  ,  ObjectDescriptionException  { 
this  .  model  =  new   DescriptionModel  (  )  ; 
this  .  baseURL  =  new   File  (  file  )  .  toURL  (  )  ; 
parseXml  (  this  .  baseURL  )  ; 
fillSuperClasses  (  )  ; 
return   this  .  model  ; 
} 





protected   void   fillSuperClasses  (  )  { 
for  (  int   i  =  0  ;  i  <  this  .  model  .  size  (  )  ;  i  ++  )  { 
final   ClassDescription   cd  =  this  .  model  .  get  (  i  )  ; 
final   Class   parent  =  cd  .  getObjectClass  (  )  .  getSuperclass  (  )  ; 
if  (  parent  ==  null  )  { 
continue  ; 
} 
final   ClassDescription   superCD  =  this  .  model  .  get  (  parent  )  ; 
if  (  superCD  !=  null  )  { 
cd  .  setSuperClass  (  superCD  .  getObjectClass  (  )  )  ; 
} 
} 
} 










protected   boolean   startObjectDefinition  (  final   String   className  ,  final   String   register  ,  final   boolean   ignore  )  { 
final   Class   c  =  loadClass  (  className  )  ; 
if  (  c  ==  null  )  { 
return   false  ; 
} 
this  .  currentClassDescription  =  new   ClassDescription  (  c  )  ; 
this  .  currentClassDescription  .  setPreserve  (  ignore  )  ; 
this  .  currentClassDescription  .  setRegisterKey  (  register  )  ; 
try  { 
this  .  currentBeanInfo  =  Introspector  .  getBeanInfo  (  c  ,  Object  .  class  )  ; 
}  catch  (  IntrospectionException   ie  )  { 
return   false  ; 
} 
this  .  propertyList  =  new   java  .  util  .  ArrayList  (  )  ; 
this  .  constructorList  =  new   java  .  util  .  ArrayList  (  )  ; 
return   true  ; 
} 







protected   void   endObjectDefinition  (  )  throws   ObjectDescriptionException  { 
final   PropertyInfo  [  ]  pis  =  (  PropertyInfo  [  ]  )  this  .  propertyList  .  toArray  (  new   PropertyInfo  [  this  .  propertyList  .  size  (  )  ]  )  ; 
this  .  currentClassDescription  .  setProperties  (  pis  )  ; 
final   TypeInfo  [  ]  tis  =  (  TypeInfo  [  ]  )  this  .  constructorList  .  toArray  (  new   TypeInfo  [  this  .  constructorList  .  size  (  )  ]  )  ; 
this  .  currentClassDescription  .  setConstructorDescription  (  tis  )  ; 
this  .  currentClassDescription  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
this  .  currentClassDescription  .  setSource  (  this  .  source  )  ; 
this  .  model  .  addClassDescription  (  this  .  currentClassDescription  )  ; 
this  .  propertyList  =  null  ; 
this  .  currentBeanInfo  =  null  ; 
this  .  currentClassDescription  =  null  ; 
} 










protected   void   handleAttributeDefinition  (  final   String   name  ,  final   String   attribName  ,  final   String   handlerClass  )  throws   ObjectDescriptionException  { 
final   PropertyInfo   propertyInfo  =  ModelBuilder  .  getInstance  (  )  .  createSimplePropertyInfo  (  getPropertyDescriptor  (  name  )  )  ; 
if  (  propertyInfo  ==  null  )  { 
throw   new   ObjectDescriptionException  (  "Unable to load property "  +  name  )  ; 
} 
propertyInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
propertyInfo  .  setPropertyType  (  PropertyType  .  ATTRIBUTE  )  ; 
propertyInfo  .  setXmlName  (  attribName  )  ; 
propertyInfo  .  setXmlHandler  (  handlerClass  )  ; 
this  .  propertyList  .  add  (  propertyInfo  )  ; 
} 









protected   void   handleConstructorDefinition  (  final   String   tagName  ,  final   String   parameterClass  )  throws   ObjectDescriptionException  { 
final   Class   c  =  loadClass  (  parameterClass  )  ; 
if  (  c  ==  null  )  { 
throw   new   ObjectDescriptionException  (  "Failed to load class "  +  parameterClass  )  ; 
} 
final   TypeInfo   ti  =  new   TypeInfo  (  tagName  ,  c  )  ; 
ti  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
this  .  constructorList  .  add  (  ti  )  ; 
} 









protected   void   handleElementDefinition  (  final   String   name  ,  final   String   element  )  throws   ObjectDescriptionException  { 
final   PropertyInfo   propertyInfo  =  ModelBuilder  .  getInstance  (  )  .  createSimplePropertyInfo  (  getPropertyDescriptor  (  name  )  )  ; 
if  (  propertyInfo  ==  null  )  { 
throw   new   ObjectDescriptionException  (  "Unable to load property "  +  name  )  ; 
} 
propertyInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
propertyInfo  .  setPropertyType  (  PropertyType  .  ELEMENT  )  ; 
propertyInfo  .  setXmlName  (  element  )  ; 
propertyInfo  .  setXmlHandler  (  null  )  ; 
this  .  propertyList  .  add  (  propertyInfo  )  ; 
} 









protected   void   handleLookupDefinition  (  final   String   name  ,  final   String   lookupKey  )  throws   ObjectDescriptionException  { 
final   PropertyInfo   propertyInfo  =  ModelBuilder  .  getInstance  (  )  .  createSimplePropertyInfo  (  getPropertyDescriptor  (  name  )  )  ; 
if  (  propertyInfo  ==  null  )  { 
throw   new   ObjectDescriptionException  (  "Unable to load property "  +  name  )  ; 
} 
propertyInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
propertyInfo  .  setPropertyType  (  PropertyType  .  LOOKUP  )  ; 
propertyInfo  .  setXmlName  (  lookupKey  )  ; 
propertyInfo  .  setXmlHandler  (  null  )  ; 
this  .  propertyList  .  add  (  propertyInfo  )  ; 
} 









protected   PropertyDescriptor   getPropertyDescriptor  (  final   String   propertyName  )  { 
final   PropertyDescriptor  [  ]  pds  =  this  .  currentBeanInfo  .  getPropertyDescriptors  (  )  ; 
for  (  int   i  =  0  ;  i  <  pds  .  length  ;  i  ++  )  { 
if  (  pds  [  i  ]  .  getName  (  )  .  equals  (  propertyName  )  )  { 
return   pds  [  i  ]  ; 
} 
} 
return   null  ; 
} 






protected   void   handleIgnoredProperty  (  final   String   name  )  { 
final   IgnoredPropertyInfo   propertyInfo  =  new   IgnoredPropertyInfo  (  name  )  ; 
propertyInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
this  .  propertyList  .  add  (  propertyInfo  )  ; 
} 












protected   boolean   handleManualMapping  (  final   String   className  ,  final   String   readHandler  ,  final   String   writeHandler  )  throws   ObjectDescriptionException  { 
final   ManualMappingInfo   manualMappingInfo  =  new   ManualMappingInfo  (  loadClass  (  className  )  ,  loadClass  (  readHandler  )  ,  loadClass  (  writeHandler  )  )  ; 
manualMappingInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
manualMappingInfo  .  setSource  (  this  .  source  )  ; 
this  .  model  .  getMappingModel  (  )  .  addManualMapping  (  manualMappingInfo  )  ; 
return   true  ; 
} 







protected   void   startMultiplexMapping  (  final   String   className  ,  final   String   typeAttr  )  { 
this  .  multiplexInfo  =  new   MultiplexMappingInfo  (  loadClass  (  className  )  ,  typeAttr  )  ; 
this  .  multiplexInfo  .  setSource  (  this  .  source  )  ; 
this  .  multiplexTypeInfos  =  new   ArrayList  (  )  ; 
} 









protected   void   handleMultiplexMapping  (  final   String   typeName  ,  final   String   className  )  throws   ObjectDescriptionException  { 
final   TypeInfo   info  =  new   TypeInfo  (  typeName  ,  loadClass  (  className  )  )  ; 
info  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
this  .  multiplexTypeInfos  .  add  (  info  )  ; 
} 






protected   void   endMultiplexMapping  (  )  throws   ObjectDescriptionException  { 
final   TypeInfo  [  ]  typeInfos  =  (  TypeInfo  [  ]  )  this  .  multiplexTypeInfos  .  toArray  (  new   TypeInfo  [  this  .  multiplexTypeInfos  .  size  (  )  ]  )  ; 
this  .  multiplexInfo  .  setComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
this  .  multiplexInfo  .  setChildClasses  (  typeInfos  )  ; 
this  .  model  .  getMappingModel  (  )  .  addMultiplexMapping  (  this  .  multiplexInfo  )  ; 
this  .  multiplexInfo  =  null  ; 
} 






protected   void   startIncludeHandling  (  final   URL   resource  )  { 
this  .  source  =  IOUtils  .  getInstance  (  )  .  createRelativeURL  (  resource  ,  this  .  baseURL  )  ; 
this  .  model  .  addSource  (  this  .  source  )  ; 
this  .  model  .  addIncludeComment  (  this  .  source  ,  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
} 




protected   void   endIncludeHandling  (  )  { 
this  .  source  =  ""  ; 
} 




protected   void   startRootDocument  (  )  { 
this  .  source  =  ""  ; 
} 




protected   void   endRootDocument  (  )  { 
this  .  model  .  setModelComments  (  new   Comments  (  getOpenComment  (  )  ,  getCloseComment  (  )  )  )  ; 
} 
} 

