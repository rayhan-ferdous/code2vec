package   org  .  objectstyle  .  cayenne  .  access  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   org  .  apache  .  log4j  .  Level  ; 
import   org  .  objectstyle  .  cayenne  .  CayenneException  ; 
import   org  .  objectstyle  .  cayenne  .  CayenneRuntimeException  ; 
import   org  .  objectstyle  .  cayenne  .  DataChannel  ; 
import   org  .  objectstyle  .  cayenne  .  DataObject  ; 
import   org  .  objectstyle  .  cayenne  .  DataObjectUtils  ; 
import   org  .  objectstyle  .  cayenne  .  DataRow  ; 
import   org  .  objectstyle  .  cayenne  .  DeleteDenyException  ; 
import   org  .  objectstyle  .  cayenne  .  Fault  ; 
import   org  .  objectstyle  .  cayenne  .  FaultFailureException  ; 
import   org  .  objectstyle  .  cayenne  .  ObjectContext  ; 
import   org  .  objectstyle  .  cayenne  .  ObjectId  ; 
import   org  .  objectstyle  .  cayenne  .  PersistenceState  ; 
import   org  .  objectstyle  .  cayenne  .  Persistent  ; 
import   org  .  objectstyle  .  cayenne  .  QueryResponse  ; 
import   org  .  objectstyle  .  cayenne  .  access  .  event  .  DataContextEvent  ; 
import   org  .  objectstyle  .  cayenne  .  access  .  util  .  IteratedSelectObserver  ; 
import   org  .  objectstyle  .  cayenne  .  conf  .  Configuration  ; 
import   org  .  objectstyle  .  cayenne  .  event  .  EventManager  ; 
import   org  .  objectstyle  .  cayenne  .  event  .  EventSubject  ; 
import   org  .  objectstyle  .  cayenne  .  graph  .  CompoundDiff  ; 
import   org  .  objectstyle  .  cayenne  .  graph  .  GraphDiff  ; 
import   org  .  objectstyle  .  cayenne  .  graph  .  GraphEvent  ; 
import   org  .  objectstyle  .  cayenne  .  graph  .  GraphManager  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  DataMap  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  DbJoin  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  DbRelationship  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  EntityResolver  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  ObjAttribute  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  ObjEntity  ; 
import   org  .  objectstyle  .  cayenne  .  map  .  ObjRelationship  ; 
import   org  .  objectstyle  .  cayenne  .  property  .  ClassDescriptor  ; 
import   org  .  objectstyle  .  cayenne  .  property  .  CollectionProperty  ; 
import   org  .  objectstyle  .  cayenne  .  property  .  Property  ; 
import   org  .  objectstyle  .  cayenne  .  property  .  PropertyVisitor  ; 
import   org  .  objectstyle  .  cayenne  .  property  .  SingleObjectArcProperty  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  NamedQuery  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  ObjectIdQuery  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  PrefetchTreeNode  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  Query  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  QueryMetadata  ; 
import   org  .  objectstyle  .  cayenne  .  query  .  SelectQuery  ; 
import   org  .  objectstyle  .  cayenne  .  util  .  EventUtil  ; 
import   org  .  objectstyle  .  cayenne  .  util  .  GenericResponse  ; 
import   org  .  objectstyle  .  cayenne  .  util  .  Util  ; 
































public   class   DataContext   implements   ObjectContext  ,  DataChannel  ,  QueryEngine  ,  Serializable  { 

public   static   final   EventSubject   WILL_COMMIT  =  EventSubject  .  getSubject  (  DataContext  .  class  ,  "DataContextWillCommit"  )  ; 

public   static   final   EventSubject   DID_COMMIT  =  EventSubject  .  getSubject  (  DataContext  .  class  ,  "DataContextDidCommit"  )  ; 

public   static   final   EventSubject   DID_ROLLBACK  =  EventSubject  .  getSubject  (  DataContext  .  class  ,  "DataContextDidRollback"  )  ; 

protected   static   final   ThreadLocal   threadDataContext  =  new   ThreadLocal  (  )  ; 

private   static   boolean   transactionEventsEnabledDefault  ; 

private   boolean   transactionEventsEnabled  ; 

private   DataContextDelegate   delegate  ; 

protected   boolean   usingSharedSnaphsotCache  ; 

protected   boolean   validatingObjectsOnCommit  ; 

protected   ObjectStore   objectStore  ; 

protected   transient   DataChannel   channel  ; 

protected   transient   EntityResolver   entityResolver  ; 

protected   transient   DataContextMergeHandler   mergeHandler  ; 






protected   Map   userProperties  ; 







protected   transient   String   lazyInitParentDomainName  ; 










public   static   DataContext   getThreadDataContext  (  )  throws   IllegalStateException  { 
DataContext   dc  =  (  DataContext  )  threadDataContext  .  get  (  )  ; 
if  (  dc  ==  null  )  { 
throw   new   IllegalStateException  (  "Current thread has no bound DataContext."  )  ; 
} 
return   dc  ; 
} 








public   static   void   bindThreadDataContext  (  DataContext   context  )  { 
threadDataContext  .  set  (  context  )  ; 
} 








public   static   DataContext   createDataContext  (  )  { 
return   Configuration  .  getSharedConfiguration  (  )  .  getDomain  (  )  .  createDataContext  (  )  ; 
} 










public   static   DataContext   createDataContext  (  boolean   useSharedCache  )  { 
return   Configuration  .  getSharedConfiguration  (  )  .  getDomain  (  )  .  createDataContext  (  useSharedCache  )  ; 
} 






public   static   DataContext   createDataContext  (  String   domainName  )  { 
DataDomain   domain  =  Configuration  .  getSharedConfiguration  (  )  .  getDomain  (  domainName  )  ; 
if  (  domain  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Non-existent domain: "  +  domainName  )  ; 
} 
return   domain  .  createDataContext  (  )  ; 
} 








public   static   DataContext   createDataContext  (  String   domainName  ,  boolean   useSharedCache  )  { 
DataDomain   domain  =  Configuration  .  getSharedConfiguration  (  )  .  getDomain  (  domainName  )  ; 
if  (  domain  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Non-existent domain: "  +  domainName  )  ; 
} 
return   domain  .  createDataContext  (  useSharedCache  )  ; 
} 




public   DataContext  (  )  { 
this  (  (  DataChannel  )  null  ,  null  )  ; 
} 













public   DataContext  (  QueryEngine   parent  ,  ObjectStore   objectStore  )  { 
this  (  (  DataChannel  )  parent  ,  objectStore  )  ; 
} 






public   DataContext  (  DataChannel   channel  ,  ObjectStore   objectStore  )  { 
setChannel  (  channel  )  ; 
this  .  setTransactionEventsEnabled  (  transactionEventsEnabledDefault  )  ; 
if  (  objectStore  !=  null  )  { 
this  .  objectStore  =  objectStore  ; 
objectStore  .  setContext  (  this  )  ; 
DataDomain   domain  =  getParentDataDomain  (  )  ; 
this  .  usingSharedSnaphsotCache  =  domain  !=  null  &&  objectStore  .  getDataRowCache  (  )  ==  domain  .  getSharedSnapshotCache  (  )  ; 
} 
} 






protected   Map   getUserProperties  (  )  { 
if  (  userProperties  ==  null  )  { 
userProperties  =  new   HashMap  (  )  ; 
} 
return   userProperties  ; 
} 






public   DataContext   createChildDataContext  (  )  { 
DataContextFactory   factory  =  getParentDataDomain  (  )  .  getDataContextFactory  (  )  ; 
ObjectStore   objectStore  =  new   ObjectStore  (  )  ; 
DataContext   child  =  factory  !=  null  ?  factory  .  createDataContext  (  this  ,  objectStore  )  :  new   DataContext  (  (  DataChannel  )  this  ,  objectStore  )  ; 
child  .  setValidatingObjectsOnCommit  (  isValidatingObjectsOnCommit  (  )  )  ; 
child  .  usingSharedSnaphsotCache  =  isUsingSharedSnapshotCache  (  )  ; 
return   child  ; 
} 







public   Object   getUserProperty  (  String   key  )  { 
return   getUserProperties  (  )  .  get  (  key  )  ; 
} 







public   void   setUserProperty  (  String   key  ,  Object   value  )  { 
getUserProperties  (  )  .  put  (  key  ,  value  )  ; 
} 







public   QueryEngine   getParent  (  )  { 
return   getParentDataDomain  (  )  ; 
} 






public   void   setParent  (  QueryEngine   parent  )  { 
if  (  parent  ==  null  ||  parent   instanceof   DataChannel  )  { 
setChannel  (  (  DataChannel  )  parent  )  ; 
}  else  { 
throw   new   CayenneRuntimeException  (  "Only parents that implement DataChannel are supported."  )  ; 
} 
} 






public   DataChannel   getChannel  (  )  { 
return   channel  ; 
} 




public   void   setChannel  (  DataChannel   channel  )  { 
if  (  this  .  channel  !=  channel  )  { 
if  (  this  .  mergeHandler  !=  null  )  { 
this  .  mergeHandler  .  setActive  (  false  )  ; 
} 
this  .  entityResolver  =  null  ; 
this  .  mergeHandler  =  null  ; 
this  .  channel  =  channel  ; 
if  (  channel  !=  null  )  { 
this  .  entityResolver  =  channel  .  getEntityResolver  (  )  ; 
EventManager   eventManager  =  channel  .  getEventManager  (  )  ; 
if  (  eventManager  !=  null  )  { 
this  .  mergeHandler  =  new   DataContextMergeHandler  (  this  )  ; 
EventUtil  .  listenForChannelEvents  (  channel  ,  mergeHandler  )  ; 
} 
} 
} 
} 










public   DataDomain   getParentDataDomain  (  )  { 
awakeFromDeserialization  (  )  ; 
if  (  channel  ==  null  )  { 
return   null  ; 
} 
if  (  channel   instanceof   DataDomain  )  { 
return  (  DataDomain  )  channel  ; 
} 
if  (  channel   instanceof   DataContext  )  { 
return  (  (  DataContext  )  channel  )  .  getParentDataDomain  (  )  ; 
} 
return   null  ; 
} 







public   void   setDelegate  (  DataContextDelegate   delegate  )  { 
this  .  delegate  =  delegate  ; 
} 






public   DataContextDelegate   getDelegate  (  )  { 
return   delegate  ; 
} 







DataContextDelegate   nonNullDelegate  (  )  { 
return  (  delegate  !=  null  )  ?  delegate  :  NoopDelegate  .  noopDelegate  ; 
} 




public   ObjectStore   getObjectStore  (  )  { 
return   objectStore  ; 
} 





public   boolean   hasChanges  (  )  { 
return   getObjectStore  (  )  .  hasChanges  (  )  ; 
} 





public   Collection   newObjects  (  )  { 
return   getObjectStore  (  )  .  objectsInState  (  PersistenceState  .  NEW  )  ; 
} 





public   Collection   deletedObjects  (  )  { 
return   getObjectStore  (  )  .  objectsInState  (  PersistenceState  .  DELETED  )  ; 
} 





public   Collection   modifiedObjects  (  )  { 
return   getObjectStore  (  )  .  objectsInState  (  PersistenceState  .  MODIFIED  )  ; 
} 






public   Collection   uncommittedObjects  (  )  { 
int   len  =  getObjectStore  (  )  .  registeredObjectsCount  (  )  ; 
if  (  len  ==  0  )  { 
return   Collections  .  EMPTY_LIST  ; 
} 
Collection   objects  =  new   ArrayList  (  len  >  100  ?  len  /  2  :  len  )  ; 
Iterator   it  =  getObjectStore  (  )  .  getObjectIterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Persistent   object  =  (  Persistent  )  it  .  next  (  )  ; 
int   state  =  object  .  getPersistenceState  (  )  ; 
if  (  state  ==  PersistenceState  .  MODIFIED  ||  state  ==  PersistenceState  .  NEW  ||  state  ==  PersistenceState  .  DELETED  )  { 
objects  .  add  (  object  )  ; 
} 
} 
return   objects  ; 
} 



















public   DataObject   registeredObject  (  ObjectId   id  )  { 
return  (  DataObject  )  localObject  (  id  ,  null  )  ; 
} 












public   DataRow   currentSnapshot  (  DataObject   object  )  { 
ObjEntity   entity  =  getEntityResolver  (  )  .  lookupObjEntity  (  object  )  ; 
if  (  object  .  getPersistenceState  (  )  ==  PersistenceState  .  HOLLOW  &&  object  .  getDataContext  (  )  !=  null  )  { 
return   getObjectStore  (  )  .  getSnapshot  (  object  .  getObjectId  (  )  )  ; 
} 
DataRow   snapshot  =  new   DataRow  (  10  )  ; 
Iterator   attributes  =  entity  .  getAttributeMap  (  )  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  attributes  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  attributes  .  next  (  )  ; 
String   attrName  =  (  String  )  entry  .  getKey  (  )  ; 
ObjAttribute   objAttr  =  (  ObjAttribute  )  entry  .  getValue  (  )  ; 
snapshot  .  put  (  objAttr  .  getDbAttributePath  (  )  ,  object  .  readPropertyDirectly  (  attrName  )  )  ; 
} 
Iterator   relationships  =  entity  .  getRelationshipMap  (  )  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  relationships  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  relationships  .  next  (  )  ; 
ObjRelationship   rel  =  (  ObjRelationship  )  entry  .  getValue  (  )  ; 
if  (  rel  .  isSourceIndependentFromTargetChange  (  )  )  { 
continue  ; 
} 
Object   targetObject  =  object  .  readPropertyDirectly  (  rel  .  getName  (  )  )  ; 
if  (  targetObject  ==  null  )  { 
continue  ; 
} 
if  (  targetObject   instanceof   Fault  )  { 
DataRow   storedSnapshot  =  getObjectStore  (  )  .  getSnapshot  (  object  .  getObjectId  (  )  )  ; 
if  (  storedSnapshot  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "No matching objects found for ObjectId "  +  object  .  getObjectId  (  )  +  ". Object may have been deleted externally."  )  ; 
} 
DbRelationship   dbRel  =  (  DbRelationship  )  rel  .  getDbRelationships  (  )  .  get  (  0  )  ; 
Iterator   joins  =  dbRel  .  getJoins  (  )  .  iterator  (  )  ; 
while  (  joins  .  hasNext  (  )  )  { 
DbJoin   join  =  (  DbJoin  )  joins  .  next  (  )  ; 
String   key  =  join  .  getSourceName  (  )  ; 
snapshot  .  put  (  key  ,  storedSnapshot  .  get  (  key  )  )  ; 
} 
continue  ; 
} 
DataObject   target  =  (  DataObject  )  targetObject  ; 
Map   idParts  =  target  .  getObjectId  (  )  .  getIdSnapshot  (  )  ; 
if  (  idParts  .  isEmpty  (  )  )  { 
continue  ; 
} 
DbRelationship   dbRel  =  (  DbRelationship  )  rel  .  getDbRelationships  (  )  .  get  (  0  )  ; 
Map   fk  =  dbRel  .  srcFkSnapshotWithTargetSnapshot  (  idParts  )  ; 
snapshot  .  putAll  (  fk  )  ; 
} 
Map   thisIdParts  =  object  .  getObjectId  (  )  .  getIdSnapshot  (  )  ; 
if  (  thisIdParts  !=  null  )  { 
Iterator   idIterator  =  thisIdParts  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  idIterator  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  idIterator  .  next  (  )  ; 
Object   nextKey  =  entry  .  getKey  (  )  ; 
if  (  !  snapshot  .  containsKey  (  nextKey  )  )  { 
snapshot  .  put  (  nextKey  ,  entry  .  getValue  (  )  )  ; 
} 
} 
} 
return   snapshot  ; 
} 
















public   List   localObjects  (  List   objects  )  { 
List   localObjects  =  new   ArrayList  (  objects  .  size  (  )  )  ; 
Iterator   it  =  objects  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
DataObject   object  =  (  DataObject  )  it  .  next  (  )  ; 
if  (  object  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Null object"  )  ; 
} 
localObjects  .  add  (  localObject  (  object  .  getObjectId  (  )  ,  null  )  )  ; 
} 
return   localObjects  ; 
} 






public   List   objectsFromDataRows  (  ObjEntity   entity  ,  List   dataRows  ,  boolean   refresh  ,  boolean   resolveInheritanceHierarchy  )  { 
return   new   ObjectResolver  (  this  ,  entity  ,  refresh  ,  resolveInheritanceHierarchy  )  .  synchronizedObjectsFromDataRows  (  dataRows  )  ; 
} 










public   List   objectsFromDataRows  (  Class   objectClass  ,  List   dataRows  ,  boolean   refresh  ,  boolean   resolveInheritanceHierarchy  )  { 
ObjEntity   entity  =  this  .  getEntityResolver  (  )  .  lookupObjEntity  (  objectClass  )  ; 
if  (  entity  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Unmapped Java class: "  +  objectClass  )  ; 
} 
return   objectsFromDataRows  (  entity  ,  dataRows  ,  refresh  ,  resolveInheritanceHierarchy  )  ; 
} 








public   DataObject   objectFromDataRow  (  Class   objectClass  ,  DataRow   dataRow  ,  boolean   refresh  )  { 
List   list  =  objectsFromDataRows  (  objectClass  ,  Collections  .  singletonList  (  dataRow  )  ,  refresh  ,  true  )  ; 
return  (  DataObject  )  list  .  get  (  0  )  ; 
} 












public   DataObject   createAndRegisterNewObject  (  String   objEntityName  )  { 
ClassDescriptor   descriptor  =  getEntityResolver  (  )  .  getClassDescriptor  (  objEntityName  )  ; 
if  (  descriptor  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Invalid entity name: "  +  objEntityName  )  ; 
} 
DataObject   dataObject  ; 
try  { 
dataObject  =  (  DataObject  )  descriptor  .  createObject  (  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   CayenneRuntimeException  (  "Error instantiating object."  ,  ex  )  ; 
} 
descriptor  .  injectValueHolders  (  dataObject  )  ; 
dataObject  .  setObjectId  (  new   ObjectId  (  objEntityName  )  )  ; 
dataObject  .  setDataContext  (  this  )  ; 
dataObject  .  setPersistenceState  (  PersistenceState  .  NEW  )  ; 
getObjectStore  (  )  .  recordObjectCreated  (  dataObject  )  ; 
return   dataObject  ; 
} 







public   Persistent   newObject  (  Class   persistentClass  )  { 
if  (  persistentClass  ==  null  )  { 
throw   new   NullPointerException  (  "Null 'persistentClass'"  )  ; 
} 
if  (  !  DataObject  .  class  .  isAssignableFrom  (  persistentClass  )  )  { 
throw   new   IllegalArgumentException  (  this  +  ": this implementation of ObjectContext only supports full DataObjects. Class "  +  persistentClass  +  " is invalid."  )  ; 
} 
return   createAndRegisterNewObject  (  persistentClass  )  ; 
} 







public   DataObject   createAndRegisterNewObject  (  Class   objectClass  )  { 
if  (  objectClass  ==  null  )  { 
throw   new   NullPointerException  (  "DataObject class can't be null."  )  ; 
} 
ObjEntity   entity  =  getEntityResolver  (  )  .  lookupObjEntity  (  objectClass  )  ; 
if  (  entity  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Class is not mapped with Cayenne: "  +  objectClass  .  getName  (  )  )  ; 
} 
return   createAndRegisterNewObject  (  entity  .  getName  (  )  )  ; 
} 







public   void   registerNewObject  (  final   DataObject   object  )  { 
if  (  object  ==  null  )  { 
throw   new   NullPointerException  (  "Can't register null object."  )  ; 
} 
ObjEntity   entity  =  getEntityResolver  (  )  .  lookupObjEntity  (  object  )  ; 
if  (  entity  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Can't find ObjEntity for DataObject class: "  +  object  .  getClass  (  )  .  getName  (  )  +  ", class is likely not mapped."  )  ; 
} 
if  (  object  .  getObjectId  (  )  !=  null  )  { 
if  (  object  .  getDataContext  (  )  ==  this  )  { 
return  ; 
}  else   if  (  object  .  getDataContext  (  )  !=  null  )  { 
throw   new   IllegalStateException  (  "DataObject is already registered with another DataContext. "  +  "Try using 'localObjects()' instead."  )  ; 
} 
}  else  { 
object  .  setObjectId  (  new   ObjectId  (  entity  .  getName  (  )  )  )  ; 
} 
object  .  setDataContext  (  this  )  ; 
object  .  setPersistenceState  (  PersistenceState  .  NEW  )  ; 
getObjectStore  (  )  .  recordObjectCreated  (  object  )  ; 
ClassDescriptor   descriptor  =  getEntityResolver  (  )  .  getClassDescriptor  (  entity  .  getName  (  )  )  ; 
if  (  descriptor  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Invalid entity name: "  +  entity  .  getName  (  )  )  ; 
} 
descriptor  .  visitProperties  (  new   PropertyVisitor  (  )  { 

public   boolean   visitCollectionArc  (  CollectionProperty   property  )  { 
property  .  injectValueHolder  (  object  )  ; 
if  (  !  property  .  isFault  (  object  )  )  { 
Iterator   it  =  (  (  Collection  )  property  .  readProperty  (  object  )  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   target  =  it  .  next  (  )  ; 
if  (  target   instanceof   DataObject  )  { 
DataObject   targetDO  =  (  DataObject  )  target  ; 
registerNewObject  (  targetDO  )  ; 
getObjectStore  (  )  .  recordArcCreated  (  object  ,  targetDO  .  getObjectId  (  )  ,  property  .  getName  (  )  )  ; 
} 
} 
} 
return   true  ; 
} 

public   boolean   visitSingleObjectArc  (  SingleObjectArcProperty   property  )  { 
Object   target  =  property  .  readPropertyDirectly  (  object  )  ; 
if  (  target   instanceof   DataObject  )  { 
DataObject   targetDO  =  (  DataObject  )  target  ; 
registerNewObject  (  targetDO  )  ; 
getObjectStore  (  )  .  recordArcCreated  (  object  ,  targetDO  .  getObjectId  (  )  ,  property  .  getName  (  )  )  ; 
} 
return   true  ; 
} 

public   boolean   visitProperty  (  Property   property  )  { 
return   true  ; 
} 
}  )  ; 
} 








public   void   unregisterObjects  (  Collection   dataObjects  )  { 
getObjectStore  (  )  .  objectsUnregistered  (  dataObjects  )  ; 
} 








public   void   invalidateObjects  (  Collection   dataObjects  )  { 
getObjectStore  (  )  .  objectsInvalidated  (  dataObjects  )  ; 
} 















public   void   deleteObjects  (  Collection   objects  )  { 
if  (  objects  .  isEmpty  (  )  )  { 
return  ; 
} 
Iterator   it  =  new   ArrayList  (  objects  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
DataObject   object  =  (  DataObject  )  it  .  next  (  )  ; 
deleteObject  (  object  )  ; 
} 
} 












public   void   deleteObject  (  Persistent   object  )  throws   DeleteDenyException  { 
new   DataContextDeleteAction  (  this  )  .  performDelete  (  object  )  ; 
} 









public   DataObject   refetchObject  (  ObjectId   oid  )  { 
if  (  oid  ==  null  )  { 
throw   new   NullPointerException  (  "Null ObjectId"  )  ; 
} 
if  (  oid  .  isTemporary  (  )  )  { 
throw   new   CayenneRuntimeException  (  "Can't refetch ObjectId "  +  oid  +  ", as it is a temporary id."  )  ; 
} 
synchronized  (  getObjectStore  (  )  )  { 
DataObject   object  =  (  DataObject  )  objectStore  .  getNode  (  oid  )  ; 
if  (  object  !=  null  )  { 
this  .  invalidateObjects  (  Collections  .  singleton  (  object  )  )  ; 
} 
} 
DataObject   object  =  (  DataObject  )  DataObjectUtils  .  objectForQuery  (  this  ,  new   ObjectIdQuery  (  oid  )  )  ; 
if  (  object  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Refetch failure: no matching objects found for ObjectId "  +  oid  )  ; 
} 
return   object  ; 
} 








public   DataNode   lookupDataNode  (  DataMap   dataMap  )  { 
DataDomain   domain  =  getParentDataDomain  (  )  ; 
if  (  domain  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "DataContext is not attached to a DataDomain "  )  ; 
} 
return   domain  .  lookupDataNode  (  dataMap  )  ; 
} 

public   void   rollbackChangesLocally  (  )  { 
if  (  getChannel  (  )  instanceof   DataDomain  )  { 
rollbackChanges  (  )  ; 
}  else  { 
throw   new   CayenneRuntimeException  (  "Implementation pending."  )  ; 
} 
} 





public   void   rollbackChanges  (  )  { 
if  (  objectStore  .  hasChanges  (  )  )  { 
GraphDiff   diff  =  getObjectStore  (  )  .  getChanges  (  )  ; 
getObjectStore  (  )  .  objectsRolledBack  (  )  ; 
if  (  channel  !=  null  )  { 
channel  .  onSync  (  this  ,  null  ,  DataChannel  .  ROLLBACK_CASCADE_SYNC  )  ; 
} 
fireDataChannelRolledback  (  this  ,  diff  )  ; 
} 
} 











public   void   commitChangesToParent  (  )  { 
flushToParent  (  false  )  ; 
} 




public   void   commitChanges  (  Level   logLevel  )  throws   CayenneRuntimeException  { 
commitChanges  (  )  ; 
} 





public   void   commitChanges  (  )  throws   CayenneRuntimeException  { 
flushToParent  (  true  )  ; 
} 






public   EventManager   getEventManager  (  )  { 
return   channel  !=  null  ?  channel  .  getEventManager  (  )  :  null  ; 
} 







public   GraphDiff   onSync  (  ObjectContext   originatingContext  ,  GraphDiff   changes  ,  int   syncType  )  { 
switch  (  syncType  )  { 
case   DataChannel  .  ROLLBACK_CASCADE_SYNC  : 
return   onContextRollback  (  originatingContext  )  ; 
case   DataChannel  .  FLUSH_NOCASCADE_SYNC  : 
return   onContextFlush  (  originatingContext  ,  changes  ,  false  )  ; 
case   DataChannel  .  FLUSH_CASCADE_SYNC  : 
return   onContextFlush  (  originatingContext  ,  changes  ,  true  )  ; 
default  : 
throw   new   CayenneRuntimeException  (  "Unrecognized SyncMessage type: "  +  syncType  )  ; 
} 
} 

GraphDiff   onContextRollback  (  ObjectContext   originatingContext  )  { 
rollbackChanges  (  )  ; 
return  (  channel  !=  null  )  ?  channel  .  onSync  (  this  ,  null  ,  DataChannel  .  ROLLBACK_CASCADE_SYNC  )  :  new   CompoundDiff  (  )  ; 
} 

GraphDiff   onContextFlush  (  ObjectContext   originatingContext  ,  GraphDiff   changes  ,  boolean   cascade  )  { 
if  (  this  !=  originatingContext  &&  changes  !=  null  )  { 
changes  .  apply  (  new   ChildDiffLoader  (  this  )  )  ; 
fireDataChannelChanged  (  originatingContext  ,  changes  )  ; 
} 
return  (  cascade  )  ?  flushToParent  (  true  )  :  new   CompoundDiff  (  )  ; 
} 






GraphDiff   flushToParent  (  boolean   cascade  )  { 
if  (  this  .  getChannel  (  )  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Cannot commit changes - channel is not set."  )  ; 
} 
int   syncType  =  cascade  ?  DataChannel  .  FLUSH_CASCADE_SYNC  :  DataChannel  .  FLUSH_NOCASCADE_SYNC  ; 
synchronized  (  getObjectStore  (  )  )  { 
DataContextFlushEventHandler   eventHandler  =  null  ; 
ObjectStoreGraphDiff   changes  =  getObjectStore  (  )  .  getChanges  (  )  ; 
boolean   noop  =  isValidatingObjectsOnCommit  (  )  ?  changes  .  validateAndCheckNoop  (  )  :  changes  .  isNoop  (  )  ; 
if  (  noop  )  { 
getObjectStore  (  )  .  postprocessAfterPhantomCommit  (  )  ; 
return   new   CompoundDiff  (  )  ; 
} 
if  (  isTransactionEventsEnabled  (  )  )  { 
eventHandler  =  new   DataContextFlushEventHandler  (  this  )  ; 
eventHandler  .  registerForDataContextEvents  (  )  ; 
fireWillCommit  (  )  ; 
} 
try  { 
GraphDiff   returnChanges  =  getChannel  (  )  .  onSync  (  this  ,  changes  ,  syncType  )  ; 
getObjectStore  (  )  .  postprocessAfterCommit  (  returnChanges  )  ; 
fireTransactionCommitted  (  )  ; 
fireDataChannelCommitted  (  this  ,  changes  )  ; 
if  (  !  returnChanges  .  isNoop  (  )  )  { 
fireDataChannelCommitted  (  getChannel  (  )  ,  returnChanges  )  ; 
} 
return   returnChanges  ; 
}  catch  (  CayenneRuntimeException   ex  )  { 
fireTransactionRolledback  (  )  ; 
Throwable   unwound  =  Util  .  unwindException  (  ex  )  ; 
if  (  unwound   instanceof   CayenneRuntimeException  )  { 
throw  (  CayenneRuntimeException  )  unwound  ; 
}  else  { 
throw   new   CayenneRuntimeException  (  "Commit Exception"  ,  unwound  )  ; 
} 
}  finally  { 
if  (  isTransactionEventsEnabled  (  )  )  { 
eventHandler  .  unregisterFromDataContextEvents  (  )  ; 
} 
} 
} 
} 





public   ResultIterator   performIteratedQuery  (  Query   query  )  throws   CayenneException  { 
IteratedSelectObserver   observer  =  new   IteratedSelectObserver  (  )  ; 
getParentDataDomain  (  )  .  performQueries  (  Collections  .  singletonList  (  query  )  ,  observer  )  ; 
return   observer  .  getResultIterator  (  )  ; 
} 






public   QueryResponse   performGenericQuery  (  Query   query  )  { 
query  =  nonNullDelegate  (  )  .  willPerformGenericQuery  (  this  ,  query  )  ; 
if  (  query  ==  null  )  { 
return   new   GenericResponse  (  )  ; 
} 
if  (  this  .  getChannel  (  )  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Can't run query - parent DataChannel is not set."  )  ; 
} 
return   onQuery  (  this  ,  query  )  ; 
} 




















public   List   performQuery  (  Query   query  )  { 
query  =  nonNullDelegate  (  )  .  willPerformQuery  (  this  ,  query  )  ; 
if  (  query  ==  null  )  { 
return   new   ArrayList  (  1  )  ; 
} 
query  =  filterThroughDelegateDeprecated  (  query  )  ; 
if  (  query  ==  null  )  { 
return   new   ArrayList  (  1  )  ; 
} 
List   result  =  onQuery  (  this  ,  query  )  .  firstList  (  )  ; 
return   result  !=  null  ?  result  :  new   ArrayList  (  1  )  ; 
} 







public   QueryResponse   onQuery  (  ObjectContext   context  ,  Query   query  )  { 
return   new   DataContextQueryAction  (  this  ,  context  ,  query  )  .  execute  (  )  ; 
} 







public   int  [  ]  performNonSelectingQuery  (  Query   query  )  { 
int  [  ]  count  =  performGenericQuery  (  query  )  .  firstUpdateCount  (  )  ; 
return   count  !=  null  ?  count  :  new   int  [  0  ]  ; 
} 







public   int  [  ]  performNonSelectingQuery  (  String   queryName  )  { 
return   performNonSelectingQuery  (  new   NamedQuery  (  queryName  )  )  ; 
} 







public   int  [  ]  performNonSelectingQuery  (  String   queryName  ,  Map   parameters  )  { 
return   performNonSelectingQuery  (  new   NamedQuery  (  queryName  ,  parameters  )  )  ; 
} 







public   void   performQueries  (  Collection   queries  ,  OperationObserver   callback  )  { 
List   finalQueries  =  new   ArrayList  (  queries  .  size  (  )  )  ; 
Iterator   it  =  queries  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Query   query  =  (  Query  )  it  .  next  (  )  ; 
query  =  filterThroughDelegateDeprecated  (  query  )  ; 
if  (  query  !=  null  )  { 
finalQueries  .  add  (  query  )  ; 
} 
} 
if  (  !  finalQueries  .  isEmpty  (  )  )  { 
getParentDataDomain  (  )  .  performQueries  (  queries  ,  callback  )  ; 
} 
} 

Query   filterThroughDelegateDeprecated  (  Query   query  )  { 
if  (  query   instanceof   org  .  objectstyle  .  cayenne  .  query  .  GenericSelectQuery  )  { 
org  .  objectstyle  .  cayenne  .  query  .  GenericSelectQuery   genericSelect  =  (  org  .  objectstyle  .  cayenne  .  query  .  GenericSelectQuery  )  query  ; 
return   nonNullDelegate  (  )  .  willPerformSelect  (  this  ,  genericSelect  )  ; 
} 
return   query  ; 
} 









public   void   performQueries  (  Collection   queries  ,  OperationObserver   callback  ,  Transaction   transaction  )  { 
Transaction  .  bindThreadTransaction  (  transaction  )  ; 
try  { 
performQueries  (  queries  ,  callback  )  ; 
}  finally  { 
Transaction  .  bindThreadTransaction  (  null  )  ; 
} 
} 













public   void   prefetchRelationships  (  SelectQuery   query  ,  List   objects  )  { 
QueryMetadata   metadata  =  query  .  getMetaData  (  getEntityResolver  (  )  )  ; 
Collection   prefetches  =  metadata  .  getPrefetchTree  (  )  !=  null  ?  query  .  getPrefetchTree  (  )  .  nonPhantomNodes  (  )  :  Collections  .  EMPTY_LIST  ; 
if  (  objects  ==  null  ||  objects  .  size  (  )  ==  0  ||  prefetches  .  size  (  )  ==  0  )  { 
return  ; 
} 
ObjEntity   entity  =  metadata  .  getObjEntity  (  )  ; 
Iterator   prefetchesIt  =  prefetches  .  iterator  (  )  ; 
while  (  prefetchesIt  .  hasNext  (  )  )  { 
PrefetchTreeNode   prefetch  =  (  PrefetchTreeNode  )  prefetchesIt  .  next  (  )  ; 
String   path  =  prefetch  .  getPath  (  )  ; 
if  (  path  .  indexOf  (  '.'  )  >=  0  )  { 
throw   new   CayenneRuntimeException  (  "Only one-step relationships are "  +  "supported at the moment, this will be fixed soon. "  +  "Unsupported path : "  +  path  )  ; 
} 
ObjRelationship   relationship  =  (  ObjRelationship  )  entity  .  getRelationship  (  path  )  ; 
if  (  relationship  ==  null  )  { 
throw   new   CayenneRuntimeException  (  "Invalid relationship: "  +  path  )  ; 
} 
if  (  relationship  .  isToMany  (  )  )  { 
throw   new   CayenneRuntimeException  (  "Only to-one relationships are supported at the moment. "  +  "Can't prefetch to-many: "  +  path  )  ; 
} 
org  .  objectstyle  .  cayenne  .  access  .  util  .  PrefetchHelper  .  resolveToOneRelations  (  this  ,  objects  ,  path  )  ; 
} 
} 













public   List   performQuery  (  String   queryName  ,  boolean   expireCachedLists  )  { 
return   performQuery  (  queryName  ,  Collections  .  EMPTY_MAP  ,  expireCachedLists  )  ; 
} 














public   List   performQuery  (  String   queryName  ,  Map   parameters  ,  boolean   expireCachedLists  )  { 
NamedQuery   query  =  new   NamedQuery  (  queryName  ,  parameters  )  ; 
query  .  setForceNoCache  (  expireCachedLists  )  ; 
return   performQuery  (  query  )  ; 
} 





public   EntityResolver   getEntityResolver  (  )  { 
awakeFromDeserialization  (  )  ; 
return   entityResolver  ; 
} 




public   static   void   setTransactionEventsEnabledDefault  (  boolean   flag  )  { 
transactionEventsEnabledDefault  =  flag  ; 
} 




public   void   setTransactionEventsEnabled  (  boolean   flag  )  { 
this  .  transactionEventsEnabled  =  flag  ; 
} 

public   boolean   isTransactionEventsEnabled  (  )  { 
return   this  .  transactionEventsEnabled  ; 
} 







public   boolean   isUsingSharedSnapshotCache  (  )  { 
return   usingSharedSnaphsotCache  ; 
} 







public   boolean   isValidatingObjectsOnCommit  (  )  { 
return   validatingObjectsOnCommit  ; 
} 







public   void   setValidatingObjectsOnCommit  (  boolean   flag  )  { 
this  .  validatingObjectsOnCommit  =  flag  ; 
} 




public   Collection   getDataMaps  (  )  { 
return  (  getEntityResolver  (  )  !=  null  )  ?  getEntityResolver  (  )  .  getDataMaps  (  )  :  Collections  .  EMPTY_LIST  ; 
} 

void   fireWillCommit  (  )  { 
if  (  this  .  transactionEventsEnabled  )  { 
DataContextEvent   commitChangesEvent  =  new   DataContextEvent  (  this  )  ; 
getEventManager  (  )  .  postEvent  (  commitChangesEvent  ,  DataContext  .  WILL_COMMIT  )  ; 
} 
} 

void   fireTransactionRolledback  (  )  { 
if  (  (  this  .  transactionEventsEnabled  )  )  { 
DataContextEvent   commitChangesEvent  =  new   DataContextEvent  (  this  )  ; 
getEventManager  (  )  .  postEvent  (  commitChangesEvent  ,  DataContext  .  DID_ROLLBACK  )  ; 
} 
} 

void   fireTransactionCommitted  (  )  { 
if  (  (  this  .  transactionEventsEnabled  )  )  { 
DataContextEvent   commitChangesEvent  =  new   DataContextEvent  (  this  )  ; 
getEventManager  (  )  .  postEvent  (  commitChangesEvent  ,  DataContext  .  DID_COMMIT  )  ; 
} 
} 




void   fireDataChannelCommitted  (  Object   postedBy  ,  GraphDiff   changes  )  { 
EventManager   manager  =  getEventManager  (  )  ; 
if  (  manager  !=  null  )  { 
GraphEvent   e  =  new   GraphEvent  (  this  ,  postedBy  ,  changes  )  ; 
manager  .  postEvent  (  e  ,  DataChannel  .  GRAPH_FLUSHED_SUBJECT  )  ; 
} 
} 




void   fireDataChannelRolledback  (  Object   postedBy  ,  GraphDiff   changes  )  { 
EventManager   manager  =  getEventManager  (  )  ; 
if  (  manager  !=  null  )  { 
GraphEvent   e  =  new   GraphEvent  (  this  ,  postedBy  ,  changes  )  ; 
manager  .  postEvent  (  e  ,  DataChannel  .  GRAPH_ROLLEDBACK_SUBJECT  )  ; 
} 
} 




void   fireDataChannelChanged  (  Object   postedBy  ,  GraphDiff   changes  )  { 
EventManager   manager  =  getEventManager  (  )  ; 
if  (  manager  !=  null  )  { 
GraphEvent   e  =  new   GraphEvent  (  this  ,  postedBy  ,  changes  )  ; 
manager  .  postEvent  (  e  ,  DataChannel  .  GRAPH_CHANGED_SUBJECT  )  ; 
} 
} 

private   void   writeObject  (  ObjectOutputStream   out  )  throws   IOException  { 
out  .  defaultWriteObject  (  )  ; 
if  (  this  .  channel  ==  null  &&  this  .  lazyInitParentDomainName  !=  null  )  { 
out  .  writeObject  (  lazyInitParentDomainName  )  ; 
}  else   if  (  this  .  channel   instanceof   DataDomain  )  { 
DataDomain   domain  =  (  DataDomain  )  this  .  channel  ; 
out  .  writeObject  (  domain  .  getName  (  )  )  ; 
}  else  { 
out  .  writeObject  (  this  .  channel  )  ; 
} 
if  (  !  isUsingSharedSnapshotCache  (  )  )  { 
out  .  writeObject  (  objectStore  .  getDataRowCache  (  )  )  ; 
} 
} 

private   void   readObject  (  ObjectInputStream   in  )  throws   IOException  ,  ClassNotFoundException  { 
in  .  defaultReadObject  (  )  ; 
Object   value  =  in  .  readObject  (  )  ; 
if  (  value   instanceof   DataChannel  )  { 
this  .  channel  =  (  DataChannel  )  value  ; 
}  else   if  (  value   instanceof   String  )  { 
this  .  lazyInitParentDomainName  =  (  String  )  value  ; 
}  else  { 
throw   new   CayenneRuntimeException  (  "Parent attribute of DataContext was neither a QueryEngine nor "  +  "the name of a valid DataDomain:"  +  value  )  ; 
} 
if  (  !  isUsingSharedSnapshotCache  (  )  )  { 
DataRowStore   cache  =  (  DataRowStore  )  in  .  readObject  (  )  ; 
objectStore  .  setDataRowCache  (  cache  )  ; 
} 
synchronized  (  getObjectStore  (  )  )  { 
Iterator   it  =  objectStore  .  getObjectIterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
DataObject   object  =  (  DataObject  )  it  .  next  (  )  ; 
object  .  setDataContext  (  this  )  ; 
} 
} 
} 

private   final   void   awakeFromDeserialization  (  )  { 
if  (  channel  ==  null  &&  lazyInitParentDomainName  !=  null  )  { 
setChannel  (  Configuration  .  getSharedConfiguration  (  )  .  getDomain  (  lazyInitParentDomainName  )  )  ; 
} 
} 








public   void   prepareForAccess  (  Persistent   object  ,  String   property  )  { 
if  (  object  .  getPersistenceState  (  )  ==  PersistenceState  .  HOLLOW  )  { 
if  (  !  (  object   instanceof   DataObject  )  )  { 
throw   new   CayenneRuntimeException  (  "Can only resolve DataObjects. Got: "  +  object  )  ; 
} 
getObjectStore  (  )  .  resolveHollow  (  (  DataObject  )  object  )  ; 
if  (  object  .  getPersistenceState  (  )  !=  PersistenceState  .  COMMITTED  )  { 
String   state  =  PersistenceState  .  persistenceStateName  (  object  .  getPersistenceState  (  )  )  ; 
throw   new   FaultFailureException  (  "Error resolving fault for ObjectId: "  +  object  .  getObjectId  (  )  +  " and state ("  +  state  +  "). Possible cause - matching row is missing from the database."  )  ; 
} 
} 
} 






public   void   propertyChanged  (  Persistent   object  ,  String   property  ,  Object   oldValue  ,  Object   newValue  )  { 
if  (  object  .  getPersistenceState  (  )  ==  PersistenceState  .  COMMITTED  )  { 
getObjectStore  (  )  .  registerDiff  (  object  ,  null  )  ; 
} 
} 






public   GraphManager   getGraphManager  (  )  { 
return   objectStore  ; 
} 












public   Persistent   localObject  (  ObjectId   id  ,  Persistent   prototype  )  { 
if  (  id  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Null ObjectId"  )  ; 
} 
ClassDescriptor   descriptor  =  getEntityResolver  (  )  .  getClassDescriptor  (  id  .  getEntityName  (  )  )  ; 
Persistent   cachedObject  =  (  Persistent  )  getGraphManager  (  )  .  getNode  (  id  )  ; 
if  (  cachedObject  !=  null  )  { 
if  (  cachedObject  !=  prototype  &&  cachedObject  .  getPersistenceState  (  )  !=  PersistenceState  .  MODIFIED  &&  cachedObject  .  getPersistenceState  (  )  !=  PersistenceState  .  DELETED  )  { 
descriptor  .  injectValueHolders  (  cachedObject  )  ; 
if  (  prototype  !=  null  &&  prototype  .  getPersistenceState  (  )  !=  PersistenceState  .  HOLLOW  )  { 
descriptor  .  shallowMerge  (  prototype  ,  cachedObject  )  ; 
if  (  cachedObject  .  getPersistenceState  (  )  ==  PersistenceState  .  HOLLOW  )  { 
cachedObject  .  setPersistenceState  (  PersistenceState  .  COMMITTED  )  ; 
} 
} 
} 
return   cachedObject  ; 
}  else  { 
Persistent   localObject  =  (  Persistent  )  descriptor  .  createObject  (  )  ; 
localObject  .  setObjectContext  (  this  )  ; 
localObject  .  setObjectId  (  id  )  ; 
getGraphManager  (  )  .  registerNode  (  id  ,  localObject  )  ; 
if  (  prototype  !=  null  &&  prototype  .  getPersistenceState  (  )  !=  PersistenceState  .  HOLLOW  )  { 
localObject  .  setPersistenceState  (  PersistenceState  .  COMMITTED  )  ; 
descriptor  .  injectValueHolders  (  localObject  )  ; 
descriptor  .  shallowMerge  (  prototype  ,  localObject  )  ; 
}  else  { 
localObject  .  setPersistenceState  (  PersistenceState  .  HOLLOW  )  ; 
} 
return   localObject  ; 
} 
} 
} 

