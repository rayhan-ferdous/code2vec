package   freemarker  .  ext  .  beans  ; 

import   java  .  beans  .  BeanInfo  ; 
import   java  .  beans  .  IndexedPropertyDescriptor  ; 
import   java  .  beans  .  IntrospectionException  ; 
import   java  .  beans  .  Introspector  ; 
import   java  .  beans  .  MethodDescriptor  ; 
import   java  .  beans  .  PropertyDescriptor  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  lang  .  reflect  .  AccessibleObject  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  lang  .  reflect  .  Field  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  math  .  BigDecimal  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  StringTokenizer  ; 
import   freemarker  .  ext  .  util  .  IdentityHashMap  ; 
import   freemarker  .  ext  .  util  .  ModelCache  ; 
import   freemarker  .  ext  .  util  .  ModelFactory  ; 
import   freemarker  .  ext  .  util  .  WrapperTemplateModel  ; 
import   freemarker  .  log  .  Logger  ; 
import   freemarker  .  template  .  AdapterTemplateModel  ; 
import   freemarker  .  template  .  ObjectWrapper  ; 
import   freemarker  .  template  .  TemplateBooleanModel  ; 
import   freemarker  .  template  .  TemplateCollectionModel  ; 
import   freemarker  .  template  .  TemplateDateModel  ; 
import   freemarker  .  template  .  TemplateHashModel  ; 
import   freemarker  .  template  .  TemplateModel  ; 
import   freemarker  .  template  .  TemplateModelException  ; 
import   freemarker  .  template  .  TemplateNumberModel  ; 
import   freemarker  .  template  .  TemplateScalarModel  ; 
import   freemarker  .  template  .  TemplateSequenceModel  ; 
import   freemarker  .  template  .  utility  .  ClassUtil  ; 
import   freemarker  .  template  .  utility  .  Collections12  ; 
import   freemarker  .  template  .  utility  .  SecurityUtilities  ; 
import   freemarker  .  template  .  utility  .  UndeclaredThrowableException  ; 







public   class   BeansWrapper   implements   ObjectWrapper  { 

public   static   final   Object   CAN_NOT_UNWRAP  =  new   Object  (  )  ; 

private   static   final   Class   BIGINTEGER_CLASS  =  java  .  math  .  BigInteger  .  class  ; 

private   static   final   Class   BOOLEAN_CLASS  =  Boolean  .  class  ; 

private   static   final   Class   CHARACTER_CLASS  =  Character  .  class  ; 

private   static   final   Class   COLLECTION_CLASS  =  Collection  .  class  ; 

private   static   final   Class   DATE_CLASS  =  Date  .  class  ; 

private   static   final   Class   HASHADAPTER_CLASS  =  HashAdapter  .  class  ; 

private   static   final   Class   ITERABLE_CLASS  ; 

private   static   final   Class   LIST_CLASS  =  List  .  class  ; 

private   static   final   Class   MAP_CLASS  =  Map  .  class  ; 

private   static   final   Class   NUMBER_CLASS  =  Number  .  class  ; 

private   static   final   Class   OBJECT_CLASS  =  Object  .  class  ; 

private   static   final   Class   SEQUENCEADAPTER_CLASS  =  SequenceAdapter  .  class  ; 

private   static   final   Class   SET_CLASS  =  Set  .  class  ; 

private   static   final   Class   SETADAPTER_CLASS  =  SetAdapter  .  class  ; 

private   static   final   Class   STRING_CLASS  =  String  .  class  ; 

static  { 
Class   iterable  ; 
try  { 
iterable  =  Class  .  forName  (  "java.lang.Iterable"  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
iterable  =  null  ; 
} 
ITERABLE_CLASS  =  iterable  ; 
} 

private   static   final   boolean   DEVELOPMENT  =  "true"  .  equals  (  SecurityUtilities  .  getSystemProperty  (  "freemarker.development"  )  )  ; 

private   static   final   Constructor   ENUMS_MODEL_CTOR  =  enumsModelCtor  (  )  ; 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  "freemarker.beans"  )  ; 

private   static   final   Set   UNSAFE_METHODS  =  createUnsafeMethodsSet  (  )  ; 

static   final   Object   GENERIC_GET_KEY  =  new   Object  (  )  ; 

private   static   final   Object   CONSTRUCTORS  =  new   Object  (  )  ; 

private   static   final   Object   ARGTYPES  =  new   Object  (  )  ; 

private   static   final   boolean   javaRebelAvailable  =  isJavaRebelAvailable  (  )  ; 




private   static   final   BeansWrapper   INSTANCE  =  new   BeansWrapper  (  )  ; 

private   final   Map   classCache  =  new   HashMap  (  )  ; 

private   Set   cachedClassNames  =  new   HashSet  (  )  ; 

private   final   StaticModels   staticModels  =  new   StaticModels  (  this  )  ; 

private   final   ClassBasedModelFactory   enumModels  =  createEnumModels  (  this  )  ; 

private   final   ModelCache   modelCache  =  new   BeansModelCache  (  this  )  ; 

private   final   BooleanModel   FALSE  =  new   BooleanModel  (  Boolean  .  FALSE  ,  this  )  ; 

private   final   BooleanModel   TRUE  =  new   BooleanModel  (  Boolean  .  TRUE  ,  this  )  ; 





public   static   final   int   EXPOSE_ALL  =  0  ; 












public   static   final   int   EXPOSE_SAFE  =  1  ; 






public   static   final   int   EXPOSE_PROPERTIES_ONLY  =  2  ; 









public   static   final   int   EXPOSE_NOTHING  =  3  ; 

private   int   exposureLevel  =  EXPOSE_SAFE  ; 

private   TemplateModel   nullModel  =  null  ; 

private   boolean   methodsShadowItems  =  true  ; 

private   boolean   exposeFields  =  false  ; 

private   int   defaultDateType  =  TemplateDateModel  .  UNKNOWN  ; 

private   ObjectWrapper   outerIdentity  =  this  ; 

private   boolean   simpleMapWrapper  ; 

private   boolean   strict  =  false  ; 







public   BeansWrapper  (  )  { 
if  (  javaRebelAvailable  )  { 
JavaRebelIntegration  .  registerWrapper  (  this  )  ; 
} 
} 




public   boolean   isStrict  (  )  { 
return   strict  ; 
} 






















public   void   setStrict  (  boolean   strict  )  { 
this  .  strict  =  strict  ; 
} 










public   void   setOuterIdentity  (  ObjectWrapper   outerIdentity  )  { 
this  .  outerIdentity  =  outerIdentity  ; 
} 





public   ObjectWrapper   getOuterIdentity  (  )  { 
return   outerIdentity  ; 
} 











public   void   setSimpleMapWrapper  (  boolean   simpleMapWrapper  )  { 
this  .  simpleMapWrapper  =  simpleMapWrapper  ; 
} 







public   boolean   isSimpleMapWrapper  (  )  { 
return   simpleMapWrapper  ; 
} 






public   void   setExposureLevel  (  int   exposureLevel  )  { 
if  (  exposureLevel  <  EXPOSE_ALL  ||  exposureLevel  >  EXPOSE_NOTHING  )  { 
throw   new   IllegalArgumentException  (  "Illegal exposure level "  +  exposureLevel  )  ; 
} 
this  .  exposureLevel  =  exposureLevel  ; 
} 

int   getExposureLevel  (  )  { 
return   exposureLevel  ; 
} 











public   void   setExposeFields  (  boolean   exposeFields  )  { 
this  .  exposeFields  =  exposeFields  ; 
} 






public   boolean   isExposeFields  (  )  { 
return   exposeFields  ; 
} 











public   synchronized   void   setMethodsShadowItems  (  boolean   methodsShadowItems  )  { 
this  .  methodsShadowItems  =  methodsShadowItems  ; 
} 

boolean   isMethodsShadowItems  (  )  { 
return   methodsShadowItems  ; 
} 








public   synchronized   void   setDefaultDateType  (  int   defaultDateType  )  { 
this  .  defaultDateType  =  defaultDateType  ; 
} 






protected   int   getDefaultDateType  (  )  { 
return   defaultDateType  ; 
} 







public   void   setUseCache  (  boolean   useCache  )  { 
modelCache  .  setUseCache  (  useCache  )  ; 
} 









public   void   setNullModel  (  TemplateModel   nullModel  )  { 
this  .  nullModel  =  nullModel  ; 
} 












public   static   final   BeansWrapper   getDefaultInstance  (  )  { 
return   INSTANCE  ; 
} 





















public   TemplateModel   wrap  (  Object   object  )  throws   TemplateModelException  { 
if  (  object  ==  null  )  return   nullModel  ; 
return   modelCache  .  getInstance  (  object  )  ; 
} 








protected   TemplateModel   getInstance  (  Object   object  ,  ModelFactory   factory  )  { 
return   factory  .  create  (  object  ,  this  )  ; 
} 

private   final   ModelFactory   BOOLEAN_FACTORY  =  new   ModelFactory  (  )  { 

public   TemplateModel   create  (  Object   object  ,  ObjectWrapper   wrapper  )  { 
return  (  (  Boolean  )  object  )  .  booleanValue  (  )  ?  TRUE  :  FALSE  ; 
} 
}  ; 

private   static   final   ModelFactory   ITERATOR_FACTORY  =  new   ModelFactory  (  )  { 

public   TemplateModel   create  (  Object   object  ,  ObjectWrapper   wrapper  )  { 
return   new   IteratorModel  (  (  Iterator  )  object  ,  (  BeansWrapper  )  wrapper  )  ; 
} 
}  ; 

private   static   final   ModelFactory   ENUMERATION_FACTORY  =  new   ModelFactory  (  )  { 

public   TemplateModel   create  (  Object   object  ,  ObjectWrapper   wrapper  )  { 
return   new   EnumerationModel  (  (  Enumeration  )  object  ,  (  BeansWrapper  )  wrapper  )  ; 
} 
}  ; 

protected   ModelFactory   getModelFactory  (  Class   clazz  )  { 
if  (  Map  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   simpleMapWrapper  ?  SimpleMapModel  .  FACTORY  :  MapModel  .  FACTORY  ; 
} 
if  (  Collection  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   CollectionModel  .  FACTORY  ; 
} 
if  (  Number  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   NumberModel  .  FACTORY  ; 
} 
if  (  Date  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   DateModel  .  FACTORY  ; 
} 
if  (  Boolean  .  class  ==  clazz  )  { 
return   BOOLEAN_FACTORY  ; 
} 
if  (  ResourceBundle  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   ResourceBundleModel  .  FACTORY  ; 
} 
if  (  Iterator  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   ITERATOR_FACTORY  ; 
} 
if  (  Enumeration  .  class  .  isAssignableFrom  (  clazz  )  )  { 
return   ENUMERATION_FACTORY  ; 
} 
if  (  clazz  .  isArray  (  )  )  { 
return   ArrayModel  .  FACTORY  ; 
} 
return   StringModel  .  FACTORY  ; 
} 










public   Object   unwrap  (  TemplateModel   model  )  throws   TemplateModelException  { 
return   unwrap  (  model  ,  OBJECT_CLASS  )  ; 
} 

public   Object   unwrap  (  TemplateModel   model  ,  Class   hint  )  throws   TemplateModelException  { 
return   unwrap  (  model  ,  hint  ,  null  )  ; 
} 

private   Object   unwrap  (  TemplateModel   model  ,  Class   hint  ,  Map   recursionStops  )  throws   TemplateModelException  { 
if  (  model  ==  nullModel  )  { 
return   null  ; 
} 
boolean   isBoolean  =  Boolean  .  TYPE  ==  hint  ; 
boolean   isChar  =  Character  .  TYPE  ==  hint  ; 
if  (  model   instanceof   AdapterTemplateModel  )  { 
Object   adapted  =  (  (  AdapterTemplateModel  )  model  )  .  getAdaptedObject  (  hint  )  ; 
if  (  hint  .  isInstance  (  adapted  )  )  { 
return   adapted  ; 
} 
if  (  adapted   instanceof   Number  &&  (  (  hint  .  isPrimitive  (  )  &&  !  isChar  &&  !  isBoolean  )  ||  NUMBER_CLASS  .  isAssignableFrom  (  hint  )  )  )  { 
Number   number  =  convertUnwrappedNumber  (  hint  ,  (  Number  )  adapted  )  ; 
if  (  number  !=  null  )  { 
return   number  ; 
} 
} 
} 
if  (  model   instanceof   WrapperTemplateModel  )  { 
Object   wrapped  =  (  (  WrapperTemplateModel  )  model  )  .  getWrappedObject  (  )  ; 
if  (  hint  .  isInstance  (  wrapped  )  )  { 
return   wrapped  ; 
} 
if  (  wrapped   instanceof   Number  &&  (  (  hint  .  isPrimitive  (  )  &&  !  isChar  &&  !  isBoolean  )  ||  NUMBER_CLASS  .  isAssignableFrom  (  hint  )  )  )  { 
Number   number  =  convertUnwrappedNumber  (  hint  ,  (  Number  )  wrapped  )  ; 
if  (  number  !=  null  )  { 
return   number  ; 
} 
} 
} 
if  (  STRING_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateScalarModel  )  { 
return  (  (  TemplateScalarModel  )  model  )  .  getAsString  (  )  ; 
} 
return   CAN_NOT_UNWRAP  ; 
} 
if  (  (  hint  .  isPrimitive  (  )  &&  !  isChar  &&  !  isBoolean  )  ||  NUMBER_CLASS  .  isAssignableFrom  (  hint  )  )  { 
if  (  model   instanceof   TemplateNumberModel  )  { 
Number   number  =  convertUnwrappedNumber  (  hint  ,  (  (  TemplateNumberModel  )  model  )  .  getAsNumber  (  )  )  ; 
if  (  number  !=  null  )  { 
return   number  ; 
} 
} 
} 
if  (  isBoolean  ||  BOOLEAN_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateBooleanModel  )  { 
return  (  (  TemplateBooleanModel  )  model  )  .  getAsBoolean  (  )  ?  Boolean  .  TRUE  :  Boolean  .  FALSE  ; 
} 
return   CAN_NOT_UNWRAP  ; 
} 
if  (  MAP_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateHashModel  )  { 
return   new   HashAdapter  (  (  TemplateHashModel  )  model  ,  this  )  ; 
} 
} 
if  (  LIST_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateSequenceModel  )  { 
return   new   SequenceAdapter  (  (  TemplateSequenceModel  )  model  ,  this  )  ; 
} 
} 
if  (  SET_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateCollectionModel  )  { 
return   new   SetAdapter  (  (  TemplateCollectionModel  )  model  ,  this  )  ; 
} 
} 
if  (  COLLECTION_CLASS  ==  hint  ||  ITERABLE_CLASS  ==  hint  )  { 
if  (  model   instanceof   TemplateCollectionModel  )  { 
return   new   CollectionAdapter  (  (  TemplateCollectionModel  )  model  ,  this  )  ; 
} 
if  (  model   instanceof   TemplateSequenceModel  )  { 
return   new   SequenceAdapter  (  (  TemplateSequenceModel  )  model  ,  this  )  ; 
} 
} 
if  (  hint  .  isArray  (  )  )  { 
if  (  model   instanceof   TemplateSequenceModel  )  { 
if  (  recursionStops  !=  null  )  { 
Object   retval  =  recursionStops  .  get  (  model  )  ; 
if  (  retval  !=  null  )  { 
return   retval  ; 
} 
}  else  { 
recursionStops  =  new   IdentityHashMap  (  )  ; 
} 
TemplateSequenceModel   seq  =  (  TemplateSequenceModel  )  model  ; 
Class   componentType  =  hint  .  getComponentType  (  )  ; 
Object   array  =  Array  .  newInstance  (  componentType  ,  seq  .  size  (  )  )  ; 
recursionStops  .  put  (  model  ,  array  )  ; 
try  { 
int   size  =  seq  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
Object   val  =  unwrap  (  seq  .  get  (  i  )  ,  componentType  ,  recursionStops  )  ; 
if  (  val  ==  CAN_NOT_UNWRAP  )  { 
return   CAN_NOT_UNWRAP  ; 
} 
Array  .  set  (  array  ,  i  ,  val  )  ; 
} 
}  finally  { 
recursionStops  .  remove  (  model  )  ; 
} 
return   array  ; 
} 
return   CAN_NOT_UNWRAP  ; 
} 
if  (  isChar  ||  hint  ==  CHARACTER_CLASS  )  { 
if  (  model   instanceof   TemplateScalarModel  )  { 
String   s  =  (  (  TemplateScalarModel  )  model  )  .  getAsString  (  )  ; 
if  (  s  .  length  (  )  ==  1  )  { 
return   new   Character  (  s  .  charAt  (  0  )  )  ; 
} 
} 
return   CAN_NOT_UNWRAP  ; 
} 
if  (  DATE_CLASS  .  isAssignableFrom  (  hint  )  )  { 
if  (  model   instanceof   TemplateDateModel  )  { 
Date   date  =  (  (  TemplateDateModel  )  model  )  .  getAsDate  (  )  ; 
if  (  hint  .  isInstance  (  date  )  )  { 
return   date  ; 
} 
} 
} 
if  (  model   instanceof   TemplateNumberModel  )  { 
Number   number  =  (  (  TemplateNumberModel  )  model  )  .  getAsNumber  (  )  ; 
if  (  hint  .  isInstance  (  number  )  )  { 
return   number  ; 
} 
} 
if  (  model   instanceof   TemplateDateModel  )  { 
Date   date  =  (  (  TemplateDateModel  )  model  )  .  getAsDate  (  )  ; 
if  (  hint  .  isInstance  (  date  )  )  { 
return   date  ; 
} 
} 
if  (  model   instanceof   TemplateScalarModel  &&  hint  .  isAssignableFrom  (  STRING_CLASS  )  )  { 
return  (  (  TemplateScalarModel  )  model  )  .  getAsString  (  )  ; 
} 
if  (  model   instanceof   TemplateBooleanModel  &&  hint  .  isAssignableFrom  (  BOOLEAN_CLASS  )  )  { 
return  (  (  TemplateBooleanModel  )  model  )  .  getAsBoolean  (  )  ?  Boolean  .  TRUE  :  Boolean  .  FALSE  ; 
} 
if  (  model   instanceof   TemplateHashModel  &&  hint  .  isAssignableFrom  (  HASHADAPTER_CLASS  )  )  { 
return   new   HashAdapter  (  (  TemplateHashModel  )  model  ,  this  )  ; 
} 
if  (  model   instanceof   TemplateSequenceModel  &&  hint  .  isAssignableFrom  (  SEQUENCEADAPTER_CLASS  )  )  { 
return   new   SequenceAdapter  (  (  TemplateSequenceModel  )  model  ,  this  )  ; 
} 
if  (  model   instanceof   TemplateCollectionModel  &&  hint  .  isAssignableFrom  (  SETADAPTER_CLASS  )  )  { 
return   new   SetAdapter  (  (  TemplateCollectionModel  )  model  ,  this  )  ; 
} 
if  (  hint  .  isInstance  (  model  )  )  { 
return   model  ; 
} 
return   CAN_NOT_UNWRAP  ; 
} 

private   static   Number   convertUnwrappedNumber  (  Class   hint  ,  Number   number  )  { 
if  (  hint  ==  Integer  .  TYPE  ||  hint  ==  Integer  .  class  )  { 
return   number   instanceof   Integer  ?  (  Integer  )  number  :  new   Integer  (  number  .  intValue  (  )  )  ; 
} 
if  (  hint  ==  Long  .  TYPE  ||  hint  ==  Long  .  class  )  { 
return   number   instanceof   Long  ?  (  Long  )  number  :  new   Long  (  number  .  longValue  (  )  )  ; 
} 
if  (  hint  ==  Float  .  TYPE  ||  hint  ==  Float  .  class  )  { 
return   number   instanceof   Float  ?  (  Float  )  number  :  new   Float  (  number  .  floatValue  (  )  )  ; 
} 
if  (  hint  ==  Double  .  TYPE  ||  hint  ==  Double  .  class  )  { 
return   number   instanceof   Double  ?  (  Double  )  number  :  new   Double  (  number  .  doubleValue  (  )  )  ; 
} 
if  (  hint  ==  Byte  .  TYPE  ||  hint  ==  Byte  .  class  )  { 
return   number   instanceof   Byte  ?  (  Byte  )  number  :  new   Byte  (  number  .  byteValue  (  )  )  ; 
} 
if  (  hint  ==  Short  .  TYPE  ||  hint  ==  Short  .  class  )  { 
return   number   instanceof   Short  ?  (  Short  )  number  :  new   Short  (  number  .  shortValue  (  )  )  ; 
} 
if  (  hint  ==  BigInteger  .  class  )  { 
return   number   instanceof   BigInteger  ?  number  :  new   BigInteger  (  number  .  toString  (  )  )  ; 
} 
if  (  hint  ==  BigDecimal  .  class  )  { 
if  (  number   instanceof   BigDecimal  )  { 
return   number  ; 
} 
if  (  number   instanceof   BigInteger  )  { 
return   new   BigDecimal  (  (  BigInteger  )  number  )  ; 
} 
if  (  number   instanceof   Long  )  { 
return   new   BigDecimal  (  number  .  toString  (  )  )  ; 
} 
return   new   BigDecimal  (  number  .  doubleValue  (  )  )  ; 
} 
if  (  hint  .  isInstance  (  number  )  )  { 
return   number  ; 
} 
return   null  ; 
} 

















TemplateModel   invokeMethod  (  Object   object  ,  Method   method  ,  Object  [  ]  args  )  throws   InvocationTargetException  ,  IllegalAccessException  ,  TemplateModelException  { 
Object   retval  =  method  .  invoke  (  object  ,  args  )  ; 
return   method  .  getReturnType  (  )  ==  Void  .  TYPE  ?  TemplateModel  .  NOTHING  :  getOuterIdentity  (  )  .  wrap  (  retval  )  ; 
} 














public   TemplateHashModel   getStaticModels  (  )  { 
return   staticModels  ; 
} 
















public   TemplateHashModel   getEnumModels  (  )  { 
if  (  enumModels  ==  null  )  { 
throw   new   UnsupportedOperationException  (  "Enums not supported on pre-1.5 JRE"  )  ; 
} 
return   enumModels  ; 
} 

public   Object   newInstance  (  Class   clazz  ,  List   arguments  )  throws   TemplateModelException  { 
try  { 
introspectClass  (  clazz  )  ; 
Map   classInfo  =  (  Map  )  classCache  .  get  (  clazz  )  ; 
Object   ctors  =  classInfo  .  get  (  CONSTRUCTORS  )  ; 
if  (  ctors  ==  null  )  { 
throw   new   TemplateModelException  (  "Class "  +  clazz  .  getName  (  )  +  " has no public constructors."  )  ; 
} 
Constructor   ctor  =  null  ; 
Object  [  ]  objargs  ; 
if  (  ctors   instanceof   SimpleMemberModel  )  { 
SimpleMemberModel   smm  =  (  SimpleMemberModel  )  ctors  ; 
ctor  =  (  Constructor  )  smm  .  getMember  (  )  ; 
objargs  =  smm  .  unwrapArguments  (  arguments  ,  this  )  ; 
}  else   if  (  ctors   instanceof   MethodMap  )  { 
MethodMap   methodMap  =  (  MethodMap  )  ctors  ; 
MemberAndArguments   maa  =  methodMap  .  getMemberAndArguments  (  arguments  )  ; 
objargs  =  maa  .  getArgs  (  )  ; 
ctor  =  (  Constructor  )  maa  .  getMember  (  )  ; 
}  else  { 
throw   new   Error  (  )  ; 
} 
return   ctor  .  newInstance  (  objargs  )  ; 
}  catch  (  TemplateModelException   e  )  { 
throw   e  ; 
}  catch  (  Exception   e  )  { 
throw   new   TemplateModelException  (  "Could not create instance of class "  +  clazz  .  getName  (  )  ,  e  )  ; 
} 
} 

void   introspectClass  (  Class   clazz  )  { 
synchronized  (  classCache  )  { 
if  (  !  classCache  .  containsKey  (  clazz  )  )  { 
introspectClassInternal  (  clazz  )  ; 
} 
} 
} 

void   removeIntrospectionInfo  (  Class   clazz  )  { 
synchronized  (  classCache  )  { 
classCache  .  remove  (  clazz  )  ; 
staticModels  .  removeIntrospectionInfo  (  clazz  )  ; 
if  (  enumModels  !=  null  )  { 
enumModels  .  removeIntrospectionInfo  (  clazz  )  ; 
} 
cachedClassNames  .  remove  (  clazz  .  getName  (  )  )  ; 
synchronized  (  this  )  { 
modelCache  .  clearCache  (  )  ; 
} 
} 
} 

private   void   introspectClassInternal  (  Class   clazz  )  { 
String   className  =  clazz  .  getName  (  )  ; 
if  (  cachedClassNames  .  contains  (  className  )  )  { 
if  (  logger  .  isInfoEnabled  (  )  )  { 
logger  .  info  (  "Detected a reloaded class ["  +  className  +  "]. Clearing BeansWrapper caches."  )  ; 
} 
classCache  .  clear  (  )  ; 
cachedClassNames  =  new   HashSet  (  )  ; 
synchronized  (  this  )  { 
modelCache  .  clearCache  (  )  ; 
} 
staticModels  .  clearCache  (  )  ; 
if  (  enumModels  !=  null  )  { 
enumModels  .  clearCache  (  )  ; 
} 
} 
classCache  .  put  (  clazz  ,  populateClassMap  (  clazz  )  )  ; 
cachedClassNames  .  add  (  className  )  ; 
} 

Map   getClassKeyMap  (  Class   clazz  )  { 
Map   map  ; 
synchronized  (  classCache  )  { 
map  =  (  Map  )  classCache  .  get  (  clazz  )  ; 
if  (  map  ==  null  )  { 
introspectClassInternal  (  clazz  )  ; 
map  =  (  Map  )  classCache  .  get  (  clazz  )  ; 
} 
} 
return   map  ; 
} 







int   keyCount  (  Class   clazz  )  { 
Map   map  =  getClassKeyMap  (  clazz  )  ; 
int   count  =  map  .  size  (  )  ; 
if  (  map  .  containsKey  (  CONSTRUCTORS  )  )  count  --  ; 
if  (  map  .  containsKey  (  GENERIC_GET_KEY  )  )  count  --  ; 
if  (  map  .  containsKey  (  ARGTYPES  )  )  count  --  ; 
return   count  ; 
} 







Set   keySet  (  Class   clazz  )  { 
Set   set  =  new   HashSet  (  getClassKeyMap  (  clazz  )  .  keySet  (  )  )  ; 
set  .  remove  (  CONSTRUCTORS  )  ; 
set  .  remove  (  GENERIC_GET_KEY  )  ; 
set  .  remove  (  ARGTYPES  )  ; 
return   set  ; 
} 







private   Map   populateClassMap  (  Class   clazz  )  { 
Map   map  =  populateClassMapWithBeanInfo  (  clazz  )  ; 
try  { 
Constructor  [  ]  ctors  =  clazz  .  getConstructors  (  )  ; 
if  (  ctors  .  length  ==  1  )  { 
Constructor   ctor  =  ctors  [  0  ]  ; 
map  .  put  (  CONSTRUCTORS  ,  new   SimpleMemberModel  (  ctor  ,  ctor  .  getParameterTypes  (  )  )  )  ; 
}  else   if  (  ctors  .  length  >  1  )  { 
MethodMap   ctorMap  =  new   MethodMap  (  "<init>"  ,  this  )  ; 
for  (  int   i  =  0  ;  i  <  ctors  .  length  ;  i  ++  )  { 
ctorMap  .  addMember  (  ctors  [  i  ]  )  ; 
} 
map  .  put  (  CONSTRUCTORS  ,  ctorMap  )  ; 
} 
}  catch  (  SecurityException   e  )  { 
logger  .  warn  (  "Canont discover constructors for class "  +  clazz  .  getName  (  )  ,  e  )  ; 
} 
switch  (  map  .  size  (  )  )  { 
case   0  : 
{ 
map  =  Collections12  .  EMPTY_MAP  ; 
break  ; 
} 
case   1  : 
{ 
Map  .  Entry   e  =  (  Map  .  Entry  )  map  .  entrySet  (  )  .  iterator  (  )  .  next  (  )  ; 
map  =  Collections12  .  singletonMap  (  e  .  getKey  (  )  ,  e  .  getValue  (  )  )  ; 
break  ; 
} 
} 
return   map  ; 
} 

private   Map   populateClassMapWithBeanInfo  (  Class   clazz  )  { 
Map   classMap  =  new   HashMap  (  )  ; 
if  (  exposeFields  )  { 
Field  [  ]  fields  =  clazz  .  getFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
Field   field  =  fields  [  i  ]  ; 
if  (  (  field  .  getModifiers  (  )  &  Modifier  .  STATIC  )  ==  0  )  { 
classMap  .  put  (  field  .  getName  (  )  ,  field  )  ; 
} 
} 
} 
Map   accessibleMethods  =  discoverAccessibleMethods  (  clazz  )  ; 
Method   genericGet  =  getFirstAccessibleMethod  (  MethodSignature  .  GET_STRING_SIGNATURE  ,  accessibleMethods  )  ; 
if  (  genericGet  ==  null  )  { 
genericGet  =  getFirstAccessibleMethod  (  MethodSignature  .  GET_OBJECT_SIGNATURE  ,  accessibleMethods  )  ; 
} 
if  (  genericGet  !=  null  )  { 
classMap  .  put  (  GENERIC_GET_KEY  ,  genericGet  )  ; 
} 
if  (  exposureLevel  ==  EXPOSE_NOTHING  )  { 
return   classMap  ; 
} 
try  { 
BeanInfo   beanInfo  =  Introspector  .  getBeanInfo  (  clazz  )  ; 
PropertyDescriptor  [  ]  pda  =  beanInfo  .  getPropertyDescriptors  (  )  ; 
MethodDescriptor  [  ]  mda  =  beanInfo  .  getMethodDescriptors  (  )  ; 
for  (  int   i  =  pda  .  length  -  1  ;  i  >=  0  ;  --  i  )  { 
PropertyDescriptor   pd  =  pda  [  i  ]  ; 
if  (  pd   instanceof   IndexedPropertyDescriptor  )  { 
IndexedPropertyDescriptor   ipd  =  (  IndexedPropertyDescriptor  )  pd  ; 
Method   readMethod  =  ipd  .  getIndexedReadMethod  (  )  ; 
Method   publicReadMethod  =  getAccessibleMethod  (  readMethod  ,  accessibleMethods  )  ; 
if  (  publicReadMethod  !=  null  &&  isSafeMethod  (  publicReadMethod  )  )  { 
try  { 
if  (  readMethod  !=  publicReadMethod  )  { 
ipd  =  new   IndexedPropertyDescriptor  (  ipd  .  getName  (  )  ,  ipd  .  getReadMethod  (  )  ,  ipd  .  getWriteMethod  (  )  ,  publicReadMethod  ,  ipd  .  getIndexedWriteMethod  (  )  )  ; 
} 
classMap  .  put  (  ipd  .  getName  (  )  ,  ipd  )  ; 
getArgTypes  (  classMap  )  .  put  (  publicReadMethod  ,  publicReadMethod  .  getParameterTypes  (  )  )  ; 
}  catch  (  IntrospectionException   e  )  { 
logger  .  warn  (  "Failed creating a publicly-accessible "  +  "property descriptor for "  +  clazz  .  getName  (  )  +  " indexed property "  +  pd  .  getName  (  )  +  ", read method "  +  publicReadMethod  +  ", write method "  +  ipd  .  getIndexedWriteMethod  (  )  ,  e  )  ; 
} 
} 
}  else  { 
Method   readMethod  =  pd  .  getReadMethod  (  )  ; 
Method   publicReadMethod  =  getAccessibleMethod  (  readMethod  ,  accessibleMethods  )  ; 
if  (  publicReadMethod  !=  null  &&  isSafeMethod  (  publicReadMethod  )  )  { 
try  { 
if  (  readMethod  !=  publicReadMethod  )  { 
pd  =  new   PropertyDescriptor  (  pd  .  getName  (  )  ,  publicReadMethod  ,  pd  .  getWriteMethod  (  )  )  ; 
pd  .  setReadMethod  (  publicReadMethod  )  ; 
} 
classMap  .  put  (  pd  .  getName  (  )  ,  pd  )  ; 
}  catch  (  IntrospectionException   e  )  { 
logger  .  warn  (  "Failed creating a publicly-accessible "  +  "property descriptor for "  +  clazz  .  getName  (  )  +  " property "  +  pd  .  getName  (  )  +  ", read method "  +  publicReadMethod  +  ", write method "  +  pd  .  getWriteMethod  (  )  ,  e  )  ; 
} 
} 
} 
} 
if  (  exposureLevel  <  EXPOSE_PROPERTIES_ONLY  )  { 
for  (  int   i  =  mda  .  length  -  1  ;  i  >=  0  ;  --  i  )  { 
MethodDescriptor   md  =  mda  [  i  ]  ; 
Method   method  =  md  .  getMethod  (  )  ; 
Method   publicMethod  =  getAccessibleMethod  (  method  ,  accessibleMethods  )  ; 
if  (  publicMethod  !=  null  &&  isSafeMethod  (  publicMethod  )  )  { 
String   name  =  md  .  getName  (  )  ; 
Object   previous  =  classMap  .  get  (  name  )  ; 
if  (  previous   instanceof   Method  )  { 
MethodMap   methodMap  =  new   MethodMap  (  name  ,  this  )  ; 
methodMap  .  addMember  (  (  Method  )  previous  )  ; 
methodMap  .  addMember  (  publicMethod  )  ; 
classMap  .  put  (  name  ,  methodMap  )  ; 
getArgTypes  (  classMap  )  .  remove  (  previous  )  ; 
}  else   if  (  previous   instanceof   MethodMap  )  { 
(  (  MethodMap  )  previous  )  .  addMember  (  publicMethod  )  ; 
}  else  { 
classMap  .  put  (  name  ,  publicMethod  )  ; 
getArgTypes  (  classMap  )  .  put  (  publicMethod  ,  publicMethod  .  getParameterTypes  (  )  )  ; 
} 
} 
} 
} 
return   classMap  ; 
}  catch  (  IntrospectionException   e  )  { 
logger  .  warn  (  "Couldn't properly perform introspection for class "  +  clazz  ,  e  )  ; 
return   new   HashMap  (  )  ; 
} 
} 

private   static   Map   getArgTypes  (  Map   classMap  )  { 
Map   argTypes  =  (  Map  )  classMap  .  get  (  ARGTYPES  )  ; 
if  (  argTypes  ==  null  )  { 
argTypes  =  new   HashMap  (  )  ; 
classMap  .  put  (  ARGTYPES  ,  argTypes  )  ; 
} 
return   argTypes  ; 
} 

static   Class  [  ]  getArgTypes  (  Map   classMap  ,  AccessibleObject   methodOrCtor  )  { 
return  (  Class  [  ]  )  (  (  Map  )  classMap  .  get  (  ARGTYPES  )  )  .  get  (  methodOrCtor  )  ; 
} 

private   static   Method   getFirstAccessibleMethod  (  MethodSignature   sig  ,  Map   accessibles  )  { 
List   l  =  (  List  )  accessibles  .  get  (  sig  )  ; 
if  (  l  ==  null  ||  l  .  isEmpty  (  )  )  { 
return   null  ; 
} 
return  (  Method  )  l  .  iterator  (  )  .  next  (  )  ; 
} 

private   static   Method   getAccessibleMethod  (  Method   m  ,  Map   accessibles  )  { 
if  (  m  ==  null  )  { 
return   null  ; 
} 
MethodSignature   sig  =  new   MethodSignature  (  m  )  ; 
List   l  =  (  List  )  accessibles  .  get  (  sig  )  ; 
if  (  l  ==  null  )  { 
return   null  ; 
} 
for  (  Iterator   iterator  =  l  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
Method   am  =  (  Method  )  iterator  .  next  (  )  ; 
if  (  am  .  getReturnType  (  )  ==  m  .  getReturnType  (  )  )  { 
return   am  ; 
} 
} 
return   null  ; 
} 

boolean   isSafeMethod  (  Method   method  )  { 
return   exposureLevel  <  EXPOSE_SAFE  ||  !  UNSAFE_METHODS  .  contains  (  method  )  ; 
} 








private   static   Map   discoverAccessibleMethods  (  Class   clazz  )  { 
Map   map  =  new   HashMap  (  )  ; 
discoverAccessibleMethods  (  clazz  ,  map  )  ; 
return   map  ; 
} 

private   static   void   discoverAccessibleMethods  (  Class   clazz  ,  Map   map  )  { 
if  (  Modifier  .  isPublic  (  clazz  .  getModifiers  (  )  )  )  { 
try  { 
Method  [  ]  methods  =  clazz  .  getMethods  (  )  ; 
for  (  int   i  =  0  ;  i  <  methods  .  length  ;  i  ++  )  { 
Method   method  =  methods  [  i  ]  ; 
MethodSignature   sig  =  new   MethodSignature  (  method  )  ; 
List   methodList  =  (  List  )  map  .  get  (  sig  )  ; 
if  (  methodList  ==  null  )  { 
methodList  =  new   LinkedList  (  )  ; 
map  .  put  (  sig  ,  methodList  )  ; 
} 
methodList  .  add  (  method  )  ; 
} 
return  ; 
}  catch  (  SecurityException   e  )  { 
logger  .  warn  (  "Could not discover accessible methods of class "  +  clazz  .  getName  (  )  +  ", attemping superclasses/interfaces."  ,  e  )  ; 
} 
} 
Class  [  ]  interfaces  =  clazz  .  getInterfaces  (  )  ; 
for  (  int   i  =  0  ;  i  <  interfaces  .  length  ;  i  ++  )  { 
discoverAccessibleMethods  (  interfaces  [  i  ]  ,  map  )  ; 
} 
Class   superclass  =  clazz  .  getSuperclass  (  )  ; 
if  (  superclass  !=  null  )  { 
discoverAccessibleMethods  (  superclass  ,  map  )  ; 
} 
} 

private   static   final   class   MethodSignature  { 

private   static   final   MethodSignature   GET_STRING_SIGNATURE  =  new   MethodSignature  (  "get"  ,  new   Class  [  ]  {  STRING_CLASS  }  )  ; 

private   static   final   MethodSignature   GET_OBJECT_SIGNATURE  =  new   MethodSignature  (  "get"  ,  new   Class  [  ]  {  OBJECT_CLASS  }  )  ; 

private   final   String   name  ; 

private   final   Class  [  ]  args  ; 

private   MethodSignature  (  String   name  ,  Class  [  ]  args  )  { 
this  .  name  =  name  ; 
this  .  args  =  args  ; 
} 

MethodSignature  (  Method   method  )  { 
this  (  method  .  getName  (  )  ,  method  .  getParameterTypes  (  )  )  ; 
} 

public   boolean   equals  (  Object   o  )  { 
if  (  o   instanceof   MethodSignature  )  { 
MethodSignature   ms  =  (  MethodSignature  )  o  ; 
return   ms  .  name  .  equals  (  name  )  &&  Arrays  .  equals  (  args  ,  ms  .  args  )  ; 
} 
return   false  ; 
} 

public   int   hashCode  (  )  { 
return   name  .  hashCode  (  )  ^  args  .  length  ; 
} 
} 

private   static   final   Set   createUnsafeMethodsSet  (  )  { 
Properties   props  =  new   Properties  (  )  ; 
InputStream   in  =  BeansWrapper  .  class  .  getResourceAsStream  (  "unsafeMethods.txt"  )  ; 
if  (  in  !=  null  )  { 
String   methodSpec  =  null  ; 
try  { 
try  { 
props  .  load  (  in  )  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
Set   set  =  new   HashSet  (  props  .  size  (  )  *  4  /  3  ,  .75f  )  ; 
Map   primClasses  =  createPrimitiveClassesMap  (  )  ; 
for  (  Iterator   iterator  =  props  .  keySet  (  )  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
methodSpec  =  (  String  )  iterator  .  next  (  )  ; 
try  { 
set  .  add  (  parseMethodSpec  (  methodSpec  ,  primClasses  )  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
if  (  DEVELOPMENT  )  { 
throw   e  ; 
} 
}  catch  (  NoSuchMethodException   e  )  { 
if  (  DEVELOPMENT  )  { 
throw   e  ; 
} 
} 
} 
return   set  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Could not load unsafe method "  +  methodSpec  +  " "  +  e  .  getClass  (  )  .  getName  (  )  +  " "  +  e  .  getMessage  (  )  )  ; 
} 
} 
return   Collections  .  EMPTY_SET  ; 
} 

private   static   Method   parseMethodSpec  (  String   methodSpec  ,  Map   primClasses  )  throws   ClassNotFoundException  ,  NoSuchMethodException  { 
int   brace  =  methodSpec  .  indexOf  (  '('  )  ; 
int   dot  =  methodSpec  .  lastIndexOf  (  '.'  ,  brace  )  ; 
Class   clazz  =  ClassUtil  .  forName  (  methodSpec  .  substring  (  0  ,  dot  )  )  ; 
String   methodName  =  methodSpec  .  substring  (  dot  +  1  ,  brace  )  ; 
String   argSpec  =  methodSpec  .  substring  (  brace  +  1  ,  methodSpec  .  length  (  )  -  1  )  ; 
StringTokenizer   tok  =  new   StringTokenizer  (  argSpec  ,  ","  )  ; 
int   argcount  =  tok  .  countTokens  (  )  ; 
Class  [  ]  argTypes  =  new   Class  [  argcount  ]  ; 
for  (  int   i  =  0  ;  i  <  argcount  ;  i  ++  )  { 
String   argClassName  =  tok  .  nextToken  (  )  ; 
argTypes  [  i  ]  =  (  Class  )  primClasses  .  get  (  argClassName  )  ; 
if  (  argTypes  [  i  ]  ==  null  )  { 
argTypes  [  i  ]  =  ClassUtil  .  forName  (  argClassName  )  ; 
} 
} 
return   clazz  .  getMethod  (  methodName  ,  argTypes  )  ; 
} 

private   static   Map   createPrimitiveClassesMap  (  )  { 
Map   map  =  new   HashMap  (  )  ; 
map  .  put  (  "boolean"  ,  Boolean  .  TYPE  )  ; 
map  .  put  (  "byte"  ,  Byte  .  TYPE  )  ; 
map  .  put  (  "char"  ,  Character  .  TYPE  )  ; 
map  .  put  (  "short"  ,  Short  .  TYPE  )  ; 
map  .  put  (  "int"  ,  Integer  .  TYPE  )  ; 
map  .  put  (  "long"  ,  Long  .  TYPE  )  ; 
map  .  put  (  "float"  ,  Float  .  TYPE  )  ; 
map  .  put  (  "double"  ,  Double  .  TYPE  )  ; 
return   map  ; 
} 





public   static   void   coerceBigDecimals  (  AccessibleObject   callable  ,  Object  [  ]  args  )  { 
Class  [  ]  formalTypes  =  null  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  ++  i  )  { 
Object   arg  =  args  [  i  ]  ; 
if  (  arg   instanceof   BigDecimal  )  { 
if  (  formalTypes  ==  null  )  { 
if  (  callable   instanceof   Method  )  { 
formalTypes  =  (  (  Method  )  callable  )  .  getParameterTypes  (  )  ; 
}  else   if  (  callable   instanceof   Constructor  )  { 
formalTypes  =  (  (  Constructor  )  callable  )  .  getParameterTypes  (  )  ; 
}  else  { 
throw   new   IllegalArgumentException  (  "Expected method or "  +  " constructor; callable is "  +  callable  .  getClass  (  )  .  getName  (  )  )  ; 
} 
} 
args  [  i  ]  =  coerceBigDecimal  (  (  BigDecimal  )  arg  ,  formalTypes  [  i  ]  )  ; 
} 
} 
} 





public   static   void   coerceBigDecimals  (  Class  [  ]  formalTypes  ,  Object  [  ]  args  )  { 
int   typeLen  =  formalTypes  .  length  ; 
int   argsLen  =  args  .  length  ; 
int   min  =  Math  .  min  (  typeLen  ,  argsLen  )  ; 
for  (  int   i  =  0  ;  i  <  min  ;  ++  i  )  { 
Object   arg  =  args  [  i  ]  ; 
if  (  arg   instanceof   BigDecimal  )  { 
args  [  i  ]  =  coerceBigDecimal  (  (  BigDecimal  )  arg  ,  formalTypes  [  i  ]  )  ; 
} 
} 
if  (  argsLen  >  typeLen  )  { 
Class   varArgType  =  formalTypes  [  typeLen  -  1  ]  ; 
for  (  int   i  =  typeLen  ;  i  <  argsLen  ;  ++  i  )  { 
Object   arg  =  args  [  i  ]  ; 
if  (  arg   instanceof   BigDecimal  )  { 
args  [  i  ]  =  coerceBigDecimal  (  (  BigDecimal  )  arg  ,  varArgType  )  ; 
} 
} 
} 
} 

public   static   Object   coerceBigDecimal  (  BigDecimal   bd  ,  Class   formalType  )  { 
if  (  formalType  ==  Integer  .  TYPE  ||  formalType  ==  Integer  .  class  )  { 
return   new   Integer  (  bd  .  intValue  (  )  )  ; 
}  else   if  (  formalType  ==  Double  .  TYPE  ||  formalType  ==  Double  .  class  )  { 
return   new   Double  (  bd  .  doubleValue  (  )  )  ; 
}  else   if  (  formalType  ==  Long  .  TYPE  ||  formalType  ==  Long  .  class  )  { 
return   new   Long  (  bd  .  longValue  (  )  )  ; 
}  else   if  (  formalType  ==  Float  .  TYPE  ||  formalType  ==  Float  .  class  )  { 
return   new   Float  (  bd  .  floatValue  (  )  )  ; 
}  else   if  (  formalType  ==  Short  .  TYPE  ||  formalType  ==  Short  .  class  )  { 
return   new   Short  (  bd  .  shortValue  (  )  )  ; 
}  else   if  (  formalType  ==  Byte  .  TYPE  ||  formalType  ==  Byte  .  class  )  { 
return   new   Byte  (  bd  .  byteValue  (  )  )  ; 
}  else   if  (  BIGINTEGER_CLASS  .  isAssignableFrom  (  formalType  )  )  { 
return   bd  .  toBigInteger  (  )  ; 
} 
return   bd  ; 
} 

private   static   ClassBasedModelFactory   createEnumModels  (  BeansWrapper   wrapper  )  { 
if  (  ENUMS_MODEL_CTOR  !=  null  )  { 
try  { 
return  (  ClassBasedModelFactory  )  ENUMS_MODEL_CTOR  .  newInstance  (  new   Object  [  ]  {  wrapper  }  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   UndeclaredThrowableException  (  e  )  ; 
} 
}  else  { 
return   null  ; 
} 
} 

private   static   Constructor   enumsModelCtor  (  )  { 
try  { 
Class  .  forName  (  "java.lang.Enum"  )  ; 
return   Class  .  forName  (  "freemarker.ext.beans.EnumModels"  )  .  getDeclaredConstructor  (  new   Class  [  ]  {  BeansWrapper  .  class  }  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 

private   static   boolean   isJavaRebelAvailable  (  )  { 
try  { 
JavaRebelIntegration  .  testAvailability  (  )  ; 
return   true  ; 
}  catch  (  NoClassDefFoundError   e  )  { 
return   false  ; 
} 
} 
} 

