package   org  .  ujorm  .  orm  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  lang  .  annotation  .  Annotation  ; 
import   java  .  lang  .  reflect  .  Field  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  logging  .  Level  ; 
import   org  .  ujorm  .  logger  .  UjoLogger  ; 
import   org  .  ujorm  .  UjoProperty  ; 
import   org  .  ujorm  .  core  .  UjoManager  ; 
import   org  .  ujorm  .  core  .  UjoManagerXML  ; 
import   org  .  ujorm  .  CompositeProperty  ; 
import   org  .  ujorm  .  logger  .  UjoLoggerFactory  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaDatabase  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaRoot  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaColumn  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaParams  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaProcedure  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaRelation2Many  ; 
import   org  .  ujorm  .  orm  .  metaModel  .  MetaTable  ; 







public   class   OrmHandler  { 


private   static   final   UjoLogger   LOGGER  =  UjoLoggerFactory  .  getLogger  (  OrmHandler  .  class  )  ; 


private   static   OrmHandler   handler  =  new   OrmHandler  (  )  ; 


private   MetaRoot   databases  =  new   MetaRoot  (  )  ; 


private   MetaRoot   configuration  ; 


private   Session   session  ; 


private   final   HashMap  <  UjoProperty  ,  MetaRelation2Many  >  propertyMap  =  new   HashMap  <  UjoProperty  ,  MetaRelation2Many  >  (  )  ; 


private   final   HashMap  <  Class  ,  MetaTable  >  entityMap  =  new   HashMap  <  Class  ,  MetaTable  >  (  )  ; 


private   final   HashMap  <  Class  ,  MetaProcedure  >  procedureMap  =  new   HashMap  <  Class  ,  MetaProcedure  >  (  )  ; 


public   OrmHandler  (  )  { 
} 


public  <  UJO   extends   OrmUjo  >  OrmHandler  (  final   Class  <  UJO  >  databaseModel  )  { 
this  (  )  ; 
loadDatabase  (  databaseModel  )  ; 
} 


public  <  UJO   extends   OrmUjo  >  OrmHandler  (  final   Class  <  UJO  >  ...  databaseModels  )  { 
this  (  )  ; 
loadDatabase  (  databaseModels  )  ; 
} 

public   static   OrmHandler   getInstance  (  )  { 
return   handler  ; 
} 





public   Session   getSession  (  )  { 
if  (  session  ==  null  )  { 
session  =  createSession  (  )  ; 
} 
return   session  ; 
} 


public   Session   createSession  (  )  { 
return   new   Session  (  this  )  ; 
} 











public   boolean   config  (  String   url  )  throws   IllegalArgumentException  { 
try  { 
if  (  url  .  startsWith  (  "~"  )  )  { 
final   String   file  =  System  .  getProperty  (  "user.home"  )  +  url  .  substring  (  1  )  ; 
return   config  (  new   File  (  file  )  .  toURI  (  )  .  toURL  (  )  ,  true  )  ; 
}  else  { 
return   config  (  new   URL  (  url  )  ,  true  )  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
throw   new   IllegalArgumentException  (  "Configuration file is not valid "  +  url  ,  e  )  ; 
} 
} 




public   void   config  (  MetaParams   params  )  throws   IllegalArgumentException  { 
MetaRoot  .  PARAMETERS  .  setValue  (  databases  ,  params  )  ; 
} 




public   void   config  (  MetaRoot   config  )  throws   IllegalArgumentException  { 
this  .  configuration  =  config  ; 
MetaParams   params  =  MetaRoot  .  PARAMETERS  .  of  (  configuration  )  ; 
if  (  params  !=  null  )  { 
config  (  params  )  ; 
} 
} 




public   boolean   config  (  URL   url  ,  boolean   throwsException  )  throws   IllegalArgumentException  { 
try  { 
final   MetaRoot   conf  =  UjoManagerXML  .  getInstance  (  )  .  parseXML  (  new   BufferedInputStream  (  url  .  openStream  (  )  )  ,  MetaRoot  .  class  ,  this  )  ; 
config  (  conf  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
if  (  throwsException  )  { 
throw   new   IllegalArgumentException  (  "Configuration file is not valid "  ,  e  )  ; 
}  else  { 
return   false  ; 
} 
} 
} 


public   boolean   isPersistent  (  UjoProperty   property  )  { 
final   boolean   resultFalse  =  property  .  isTypeOf  (  List  .  class  )  ||  UjoManager  .  getInstance  (  )  .  isTransientProperty  (  property  )  ; 
return  !  resultFalse  ; 
} 


private  <  UJO   extends   OrmUjo  >  MetaDatabase   loadDatabaseInternal  (  Class  <  UJO  >  databaseModel  )  { 
String   databaseId  =  databaseModel  .  getSimpleName  (  )  ; 
MetaDatabase   paramDb  =  configuration  !=  null  ?  configuration  .  removeDb  (  databaseId  )  :  null  ; 
UJO   root  =  getInstance  (  databaseModel  )  ; 
MetaDatabase   dbModel  =  new   MetaDatabase  (  this  ,  root  ,  paramDb  ,  databases  .  getDatabaseCount  (  )  )  ; 
databases  .  add  (  dbModel  )  ; 
return   dbModel  ; 
} 




@  SuppressWarnings  (  "unchecked"  ) 
public   final  <  UJO   extends   OrmUjo  >  void   loadDatabase  (  final   Class  <  UJO  >  databaseModel  )  { 
loadDatabase  (  new   Class  [  ]  {  databaseModel  }  )  ; 
} 




public   synchronized  <  UJO   extends   OrmUjo  >  void   loadDatabase  (  final   Class  <  UJO  >  ...  databaseModel  )  { 
if  (  isReadOnly  (  )  )  { 
throw   new   IllegalArgumentException  (  "The meta-model is locked and can´t be changed."  )  ; 
} 
for  (  Class  <  UJO  >  db  :  databaseModel  )  { 
loadDatabaseInternal  (  db  )  ; 
} 
MetaParams   params  =  getParameters  (  )  ; 
for  (  MetaRelation2Many   r  :  propertyMap  .  values  (  )  )  { 
if  (  r  .  isColumn  (  )  )  { 
(  (  MetaColumn  )  r  )  .  initTypeCode  (  )  ; 
} 
} 
databases  .  setReadOnly  (  true  )  ; 
final   Level   level  =  MetaParams  .  LOG_METAMODEL_INFO  .  of  (  params  )  ?  Level  .  INFO  :  Level  .  FINE  ; 
if  (  LOGGER  .  isLoggable  (  level  )  )  { 
final   String   msg  =  "DATABASE META-MODEL:\n"  +  getConfig  (  )  ; 
LOGGER  .  log  (  level  ,  msg  )  ; 
} 
final   File   outConfigFile  =  MetaParams  .  SAVE_CONFIG_TO_FILE  .  of  (  getParameters  (  )  )  ; 
if  (  outConfigFile  !=  null  )  try  { 
databases  .  print  (  outConfigFile  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IllegalStateException  (  "Can't create configuration "  +  outConfigFile  ,  e  )  ; 
} 
for  (  MetaDatabase   dbModel  :  getDatabases  (  )  )  { 
switch  (  MetaDatabase  .  ORM2DLL_POLICY  .  of  (  dbModel  )  )  { 
case   CREATE_DDL  : 
case   CREATE_OR_UPDATE_DDL  : 
case   VALIDATE  : 
dbModel  .  create  (  getSession  (  )  )  ; 
} 
} 
} 


private  <  UJO   extends   OrmUjo  >  UJO   getInstance  (  Class  <  UJO  >  databaseModel  )  { 
try  { 
return   databaseModel  .  newInstance  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   IllegalArgumentException  (  "Can't create instance of "  +  databaseModel  ,  e  )  ; 
} 
} 


public   boolean   isReadOnly  (  )  { 
final   List  <  MetaDatabase  >  dbs  =  getDatabases  (  )  ; 
final   boolean   result  =  dbs  ==  null  ||  dbs  .  isEmpty  (  )  ?  false  :  dbs  .  get  (  0  )  .  readOnly  (  )  ; 
return   result  ; 
} 


@  SuppressWarnings  (  "unchecked"  ) 
public   void   addProcedureModel  (  MetaProcedure   metaProcedure  )  { 
procedureMap  .  put  (  MetaProcedure  .  DB_PROPERTY  .  of  (  metaProcedure  )  .  getType  (  )  ,  metaProcedure  )  ; 
} 


@  SuppressWarnings  (  "unchecked"  ) 
public   void   addTableModel  (  MetaTable   metaTable  )  { 
entityMap  .  put  (  metaTable  .  getType  (  )  ,  metaTable  )  ; 
} 


@  SuppressWarnings  (  "unchecked"  ) 
public   void   addColumnModel  (  MetaRelation2Many   column  )  { 
UjoProperty   property  =  column  .  getProperty  (  )  ; 
MetaRelation2Many   oldColumn  =  findColumnModel  (  property  )  ; 
if  (  oldColumn  ==  null  )  { 
propertyMap  .  put  (  property  ,  column  )  ; 
}  else  { 
final   Class   oldType  =  oldColumn  .  getTableClass  (  )  ; 
final   Class   newType  =  column  .  getTableClass  (  )  ; 
if  (  newType  .  isAssignableFrom  (  oldType  )  )  { 
propertyMap  .  put  (  property  ,  column  )  ; 
} 
} 
} 




public  <  T   extends   Annotation  >  T   findAnnotation  (  UjoProperty   property  ,  Class  <  T  >  annotationClass  )  { 
if  (  !  property  .  isDirect  (  )  )  { 
property  =  (  (  CompositeProperty  )  property  )  .  getFirstProperty  (  )  ; 
} 
try  { 
for  (  Field   field  :  findColumnModel  (  property  )  .  getTableClass  (  )  .  getFields  (  )  )  { 
if  (  field  .  getModifiers  (  )  ==  UjoManager  .  PROPERTY_MODIFIER  &&  field  .  get  (  null  )  ==  property  )  { 
return  (  T  )  field  .  getAnnotation  (  annotationClass  )  ; 
} 
} 
}  catch  (  Throwable   e  )  { 
throw   new   IllegalStateException  (  "Illegal state for: "  +  property  ,  e  )  ; 
} 
return   null  ; 
} 





public   MetaRelation2Many   findColumnModel  (  UjoProperty   pathProperty  )  { 
if  (  pathProperty  !=  null  &&  !  pathProperty  .  isDirect  (  )  )  { 
pathProperty  =  (  (  CompositeProperty  )  pathProperty  )  .  getLastProperty  (  )  ; 
} 
final   MetaRelation2Many   result  =  propertyMap  .  get  (  pathProperty  )  ; 
return   result  ; 
} 




public   MetaTable   findTableModel  (  Class  <  ?  extends   OrmUjo  >  dbClass  )  throws   IllegalStateException  { 
MetaTable   result  =  entityMap  .  get  (  dbClass  )  ; 
if  (  result  ==  null  )  { 
final   String   msg  =  "An entity mapping bug: the "  +  dbClass  +  " is not mapped to the Database."  ; 
throw   new   IllegalStateException  (  msg  )  ; 
} 
return   result  ; 
} 




public   MetaProcedure   findProcedureModel  (  Class  <  ?  extends   DbProcedure  >  procedureClass  )  throws   IllegalStateException  { 
MetaProcedure   result  =  procedureMap  .  get  (  procedureClass  )  ; 
if  (  result  ==  null  )  { 
final   String   msg  =  "An procedure mapping bug: the "  +  procedureClass  +  " is not mapped to the Database."  ; 
throw   new   IllegalStateException  (  msg  )  ; 
} 
return   result  ; 
} 


public   MetaParams   getParameters  (  )  { 
return   MetaRoot  .  PARAMETERS  .  of  (  databases  )  ; 
} 


public   boolean   isDatabaseLoaded  (  )  { 
int   itemCount  =  MetaRoot  .  DATABASES  .  getItemCount  (  databases  )  ; 
return   itemCount  >  0  ; 
} 


public   List  <  MetaDatabase  >  getDatabases  (  )  { 
return   MetaRoot  .  DATABASES  .  of  (  databases  )  ; 
} 




public   List  <  UjoProperty  >  findPropertiesByType  (  Class   type  )  { 
List  <  UjoProperty  >  result  =  new   ArrayList  <  UjoProperty  >  (  )  ; 
for  (  UjoProperty   p  :  propertyMap  .  keySet  (  )  )  { 
if  (  p  .  isTypeOf  (  type  )  )  { 
result  .  add  (  p  )  ; 
} 
} 
return   result  ; 
} 


public   String   getConfig  (  )  { 
return   databases  .  toString  (  )  ; 
} 
} 

