package   org  .  melati  .  poem  ; 

import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Map  ; 
import   java  .  io  .  PrintStream  ; 
import   org  .  melati  .  util  .  Transaction  ; 
import   org  .  melati  .  util  .  Transactioned  ; 
import   org  .  melati  .  util  .  MappedEnumeration  ; 
import   org  .  melati  .  util  .  FlattenedEnumeration  ; 
import   org  .  melati  .  util  .  MelatiLocale  ; 













public   class   Persistent   extends   Transactioned   implements   Cloneable  ,  Persistable  { 

private   Table   table  ; 

private   Integer   troid  ; 

private   AccessToken   clearedToken  ; 

private   boolean   knownCanRead  =  false  ,  knownCanWrite  =  false  ,  knownCanDelete  =  false  ; 











boolean   dirty  =  false  ; 

private   static   final   int   NONEXISTENT  =  0  ,  EXISTENT  =  1  ,  DELETED  =  2  ; 

private   int   status  =  NONEXISTENT  ; 

private   Object  [  ]  extras  =  null  ; 

public   Persistent  (  Table   table  ,  Integer   troid  )  { 
super  (  table  .  getDatabase  (  )  )  ; 
this  .  table  =  table  ; 
this  .  troid  =  troid  ; 
} 

public   Persistent  (  Table   table  )  { 
this  .  table  =  table  ; 
} 

public   Persistent  (  )  { 
} 

final   void   setStatusNonexistent  (  )  { 
status  =  NONEXISTENT  ; 
} 

final   void   setStatusExistent  (  )  { 
status  =  EXISTENT  ; 
} 

public   final   boolean   statusNonexistent  (  )  { 
return   status  ==  NONEXISTENT  ; 
} 

public   final   boolean   statusExistent  (  )  { 
return   status  ==  EXISTENT  ; 
} 

private   void   assertNotFloating  (  )  { 
if  (  troid  ==  null  )  throw   new   InvalidOperationOnFloatingPersistentPoemException  (  this  )  ; 
} 

private   void   assertNotDeleted  (  )  { 
if  (  status  ==  DELETED  )  throw   new   RowDisappearedPoemException  (  this  )  ; 
} 

protected   void   load  (  Transaction   transaction  )  { 
if  (  troid  ==  null  )  throw   new   InvalidOperationOnFloatingPersistentPoemException  (  this  )  ; 
table  .  load  (  (  PoemTransaction  )  transaction  ,  this  )  ; 
} 

protected   boolean   upToDate  (  Transaction   transaction  )  { 
return   false  ; 
} 









protected   void   writeDown  (  Transaction   transaction  )  { 
if  (  status  !=  DELETED  )  { 
assertNotFloating  (  )  ; 
table  .  writeDown  (  (  PoemTransaction  )  transaction  ,  this  )  ; 
} 
} 

protected   void   writeLock  (  Transaction   transaction  )  { 
if  (  troid  !=  null  )  { 
super  .  writeLock  (  transaction  )  ; 
assertNotDeleted  (  )  ; 
dirty  =  true  ; 
table  .  notifyTouched  (  (  PoemTransaction  )  transaction  ,  this  )  ; 
} 
} 




protected   void   readLock  (  Transaction   transaction  )  { 
if  (  troid  !=  null  )  { 
super  .  readLock  (  transaction  )  ; 
assertNotDeleted  (  )  ; 
} 
} 

protected   void   commit  (  Transaction   transaction  )  { 
if  (  status  !=  DELETED  )  { 
assertNotFloating  (  )  ; 
super  .  commit  (  transaction  )  ; 
} 
} 

protected   void   rollback  (  Transaction   transaction  )  { 
if  (  status  !=  DELETED  )  { 
assertNotFloating  (  )  ; 
super  .  rollback  (  transaction  )  ; 
} 
} 


public   final   void   makePersistent  (  )  { 
getTable  (  )  .  create  (  this  )  ; 
} 

synchronized   Object  [  ]  extras  (  )  { 
if  (  extras  ==  null  )  extras  =  new   Object  [  table  .  extrasCount  (  )  ]  ; 
return   extras  ; 
} 




public   final   Table   getTable  (  )  { 
return   table  ; 
} 

synchronized   void   setTable  (  Table   table  ,  Integer   troid  )  { 
setTransactionPool  (  table  .  getDatabase  (  )  )  ; 
this  .  table  =  table  ; 
this  .  troid  =  troid  ; 
} 





public   final   Database   getDatabase  (  )  { 
return   table  .  getDatabase  (  )  ; 
} 






public   final   Integer   troid  (  )  { 
return   troid  ; 
} 















public   final   Integer   getTroid  (  )  throws   AccessPoemException  { 
assertCanRead  (  )  ; 
return   troid  (  )  ; 
} 

protected   void   existenceLock  (  SessionToken   sessionToken  )  { 
super  .  readLock  (  sessionToken  .  transaction  )  ; 
} 

protected   void   readLock  (  SessionToken   sessionToken  )  throws   AccessPoemException  { 
assertCanRead  (  sessionToken  .  accessToken  )  ; 
readLock  (  sessionToken  .  transaction  )  ; 
} 

protected   void   writeLock  (  SessionToken   sessionToken  )  throws   AccessPoemException  { 
if  (  troid  !=  null  )  assertCanWrite  (  sessionToken  .  accessToken  )  ; 
writeLock  (  sessionToken  .  transaction  )  ; 
} 

protected   void   deleteLock  (  SessionToken   sessionToken  )  throws   AccessPoemException  { 
if  (  troid  !=  null  )  assertCanDelete  (  sessionToken  .  accessToken  )  ; 
writeLock  (  sessionToken  .  transaction  )  ; 
} 

public   void   existenceLock  (  )  { 
existenceLock  (  PoemThread  .  sessionToken  (  )  )  ; 
} 

protected   void   readLock  (  )  throws   AccessPoemException  { 
readLock  (  PoemThread  .  sessionToken  (  )  )  ; 
} 

protected   void   writeLock  (  )  throws   AccessPoemException  { 
writeLock  (  PoemThread  .  sessionToken  (  )  )  ; 
} 













protected   Capability   getCanRead  (  )  { 
Column   crCol  =  getTable  (  )  .  canReadColumn  (  )  ; 
return   crCol  ==  null  ?  null  :  (  Capability  )  crCol  .  getType  (  )  .  cookedOfRaw  (  crCol  .  getRaw_unsafe  (  this  )  )  ; 
} 









































public   void   assertCanRead  (  AccessToken   token  )  throws   AccessPoemException  { 
if  (  !  (  clearedToken  ==  token  &&  knownCanRead  )  &&  troid  !=  null  )  { 
Capability   canRead  =  getCanRead  (  )  ; 
if  (  canRead  ==  null  )  canRead  =  getTable  (  )  .  getDefaultCanRead  (  )  ; 
if  (  canRead  !=  null  )  { 
if  (  !  token  .  givesCapability  (  canRead  )  )  throw   new   ReadPersistentAccessPoemException  (  this  ,  token  ,  canRead  )  ; 
if  (  clearedToken  !=  token  )  { 
knownCanWrite  =  false  ; 
knownCanDelete  =  false  ; 
} 
clearedToken  =  token  ; 
knownCanRead  =  true  ; 
} 
} 
} 

public   final   void   assertCanRead  (  )  throws   AccessPoemException  { 
assertCanRead  (  PoemThread  .  accessToken  (  )  )  ; 
} 






public   final   boolean   getReadable  (  )  { 
try  { 
assertCanRead  (  )  ; 
return   true  ; 
}  catch  (  AccessPoemException   e  )  { 
return   false  ; 
} 
} 













protected   Capability   getCanWrite  (  )  { 
Column   cwCol  =  getTable  (  )  .  canWriteColumn  (  )  ; 
return   cwCol  ==  null  ?  null  :  (  Capability  )  cwCol  .  getType  (  )  .  cookedOfRaw  (  cwCol  .  getRaw_unsafe  (  this  )  )  ; 
} 














public   void   assertCanWrite  (  AccessToken   token  )  throws   AccessPoemException  { 
if  (  !  (  clearedToken  ==  token  &&  knownCanWrite  )  &&  troid  !=  null  )  { 
Capability   canWrite  =  getCanWrite  (  )  ; 
if  (  canWrite  ==  null  )  canWrite  =  getTable  (  )  .  getDefaultCanWrite  (  )  ; 
if  (  canWrite  !=  null  )  { 
if  (  !  token  .  givesCapability  (  canWrite  )  )  throw   new   WritePersistentAccessPoemException  (  this  ,  token  ,  canWrite  )  ; 
if  (  clearedToken  !=  token  )  { 
knownCanRead  =  false  ; 
knownCanDelete  =  false  ; 
} 
clearedToken  =  token  ; 
knownCanWrite  =  true  ; 
} 
} 
} 

public   final   void   assertCanWrite  (  )  throws   AccessPoemException  { 
assertCanWrite  (  PoemThread  .  accessToken  (  )  )  ; 
} 













protected   Capability   getCanDelete  (  )  { 
Column   cwCol  =  getTable  (  )  .  canDeleteColumn  (  )  ; 
return   cwCol  ==  null  ?  null  :  (  Capability  )  cwCol  .  getType  (  )  .  cookedOfRaw  (  cwCol  .  getRaw_unsafe  (  this  )  )  ; 
} 














public   void   assertCanDelete  (  AccessToken   token  )  throws   AccessPoemException  { 
if  (  !  (  clearedToken  ==  token  &&  knownCanDelete  )  &&  troid  !=  null  )  { 
Capability   canDelete  =  getCanDelete  (  )  ; 
if  (  canDelete  ==  null  )  canDelete  =  getTable  (  )  .  getDefaultCanDelete  (  )  ; 
if  (  canDelete  !=  null  )  { 
if  (  !  token  .  givesCapability  (  canDelete  )  )  throw   new   DeletePersistentAccessPoemException  (  this  ,  token  ,  canDelete  )  ; 
if  (  clearedToken  !=  token  )  { 
knownCanRead  =  false  ; 
knownCanWrite  =  false  ; 
} 
clearedToken  =  token  ; 
knownCanDelete  =  true  ; 
} 
} 
} 

public   final   void   assertCanDelete  (  )  throws   AccessPoemException  { 
assertCanDelete  (  PoemThread  .  accessToken  (  )  )  ; 
} 









protected   Capability   getCanSelect  (  )  { 
Column   c  =  getTable  (  )  .  canSelectColumn  (  )  ; 
return   c  ==  null  ?  null  :  (  Capability  )  c  .  getType  (  )  .  cookedOfRaw  (  c  .  getRaw_unsafe  (  this  )  )  ; 
} 




















public   void   assertCanCreate  (  AccessToken   token  )  { 
Capability   canCreate  =  getTable  (  )  .  getCanCreate  (  )  ; 
if  (  canCreate  !=  null  &&  !  token  .  givesCapability  (  canCreate  )  )  throw   new   CreationAccessPoemException  (  getTable  (  )  ,  token  ,  canCreate  )  ; 
} 

public   final   void   assertCanCreate  (  )  throws   AccessPoemException  { 
assertCanCreate  (  PoemThread  .  accessToken  (  )  )  ; 
} 


























































public   Object   getRaw  (  String   name  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  { 
return   getTable  (  )  .  getColumn  (  name  )  .  getRaw  (  this  )  ; 
} 






























public   final   String   getRawString  (  String   name  )  throws   AccessPoemException  ,  NoSuchColumnPoemException  { 
Column   column  =  getTable  (  )  .  getColumn  (  name  )  ; 
return   column  .  getType  (  )  .  stringOfRaw  (  column  .  getRaw  (  this  )  )  ; 
} 



































































public   void   setRaw  (  String   name  ,  Object   raw  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  ,  ValidationPoemException  { 
getTable  (  )  .  getColumn  (  name  )  .  setRaw  (  this  ,  raw  )  ; 
} 































public   final   void   setRawString  (  String   name  ,  String   string  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  ,  ParsingPoemException  ,  ValidationPoemException  { 
Column   column  =  getTable  (  )  .  getColumn  (  name  )  ; 
column  .  setRaw  (  this  ,  column  .  getType  (  )  .  rawOfString  (  string  )  )  ; 
} 








































public   Object   getCooked  (  String   name  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  { 
return   getTable  (  )  .  getColumn  (  name  )  .  getCooked  (  this  )  ; 
} 































public   final   String   getCookedString  (  String   name  ,  MelatiLocale   locale  ,  int   style  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  { 
Column   column  =  getTable  (  )  .  getColumn  (  name  )  ; 
return   column  .  getType  (  )  .  stringOfCooked  (  column  .  getCooked  (  this  )  ,  locale  ,  style  )  ; 
} 




































public   void   setCooked  (  String   name  ,  Object   cooked  )  throws   NoSuchColumnPoemException  ,  ValidationPoemException  ,  AccessPoemException  { 
getTable  (  )  .  getColumn  (  name  )  .  setCooked  (  this  ,  cooked  )  ; 
} 
















public   final   Field   getField  (  String   name  )  throws   NoSuchColumnPoemException  ,  AccessPoemException  { 
return   getTable  (  )  .  getColumn  (  name  )  .  asField  (  this  )  ; 
} 

public   Enumeration   fieldsOfColumns  (  Enumeration   columns  )  { 
final   Persistent   _this  =  this  ; 
return   new   MappedEnumeration  (  columns  )  { 

public   Object   mapped  (  Object   column  )  { 
return  (  (  Column  )  column  )  .  asField  (  _this  )  ; 
} 
}  ; 
} 







public   Enumeration   getFields  (  )  { 
return   fieldsOfColumns  (  getTable  (  )  .  columns  (  )  )  ; 
} 









public   Enumeration   getRecordDisplayFields  (  )  { 
return   fieldsOfColumns  (  getTable  (  )  .  getRecordDisplayColumns  (  )  )  ; 
} 







public   Enumeration   getDetailDisplayFields  (  )  { 
return   fieldsOfColumns  (  getTable  (  )  .  getDetailDisplayColumns  (  )  )  ; 
} 







public   Enumeration   getSummaryDisplayFields  (  )  { 
return   fieldsOfColumns  (  getTable  (  )  .  getSummaryDisplayColumns  (  )  )  ; 
} 

public   Enumeration   getSearchCriterionFields  (  )  { 
return   fieldsOfColumns  (  getTable  (  )  .  getSearchCriterionColumns  (  )  )  ; 
} 

public   void   delete_unsafe  (  )  throws   AccessPoemException  { 
assertNotFloating  (  )  ; 
SessionToken   sessionToken  =  PoemThread  .  sessionToken  (  )  ; 
deleteLock  (  sessionToken  )  ; 
try  { 
status  =  DELETED  ; 
table  .  delete  (  troid  (  )  ,  sessionToken  .  transaction  )  ; 
}  catch  (  PoemException   e  )  { 
status  =  EXISTENT  ; 
throw   e  ; 
} 
} 

public   Field   getPrimaryDisplayField  (  )  { 
return   getTable  (  )  .  displayColumn  (  )  .  asField  (  this  )  ; 
} 























public   void   delete  (  Map   integrityFixOfColumn  )  { 
assertNotFloating  (  )  ; 
deleteLock  (  PoemThread  .  sessionToken  (  )  )  ; 
Enumeration   columns  =  getDatabase  (  )  .  referencesTo  (  getTable  (  )  )  ; 
Vector   refEnumerations  =  new   Vector  (  )  ; 
while  (  columns  .  hasMoreElements  (  )  )  { 
Column   column  =  (  Column  )  columns  .  nextElement  (  )  ; 
IntegrityFix   fix  ; 
try  { 
fix  =  integrityFixOfColumn  ==  null  ?  null  :  (  IntegrityFix  )  integrityFixOfColumn  .  get  (  column  )  ; 
}  catch  (  ClassCastException   e  )  { 
throw   new   AppBugPoemException  (  "integrityFixOfColumn argument to Persistent.deleteAndCommit "  +  "is meant to be a Map from Column to IntegrityFix"  ,  e  )  ; 
} 
if  (  fix  ==  null  )  fix  =  column  .  getIntegrityFix  (  )  ; 
refEnumerations  .  addElement  (  fix  .  referencesTo  (  this  ,  column  ,  column  .  selectionWhereEq  (  troid  (  )  )  ,  integrityFixOfColumn  )  )  ; 
} 
Enumeration   refs  =  new   FlattenedEnumeration  (  refEnumerations  .  elements  (  )  )  ; 
if  (  refs  .  hasMoreElements  (  )  )  throw   new   DeletionIntegrityPoemException  (  this  ,  refs  )  ; 
delete_unsafe  (  )  ; 
} 

public   final   void   delete  (  )  { 
delete  (  null  )  ; 
} 












public   void   deleteAndCommit  (  Map   integrityFixOfColumn  )  throws   AccessPoemException  ,  DeletionIntegrityPoemException  { 
getDatabase  (  )  .  beginExclusiveLock  (  )  ; 
try  { 
delete  (  integrityFixOfColumn  )  ; 
PoemThread  .  commit  (  )  ; 
}  catch  (  RuntimeException   e  )  { 
PoemThread  .  rollback  (  )  ; 
throw   e  ; 
}  finally  { 
getDatabase  (  )  .  endExclusiveLock  (  )  ; 
} 
} 

public   final   void   deleteAndCommit  (  )  throws   AccessPoemException  ,  DeletionIntegrityPoemException  { 
deleteAndCommit  (  null  )  ; 
} 







public   Persistent   duplicated  (  )  throws   AccessPoemException  { 
assertNotFloating  (  )  ; 
assertNotDeleted  (  )  ; 
return  (  Persistent  )  clone  (  )  ; 
} 










public   Persistent   duplicatedFloating  (  )  throws   AccessPoemException  { 
return  (  Persistent  )  clone  (  )  ; 
} 





public   String   toString  (  )  { 
if  (  getTable  (  )  ==  null  )  { 
return  "null/"  +  troid  (  )  ; 
} 
return   getTable  (  )  .  getName  (  )  +  "/"  +  troid  (  )  ; 
} 








public   String   displayString  (  MelatiLocale   locale  ,  int   style  )  throws   AccessPoemException  { 
Column   displayColumn  =  getTable  (  )  .  displayColumn  (  )  ; 
if  (  displayColumn  ==  null  )  return   String  .  valueOf  (  getTroid  (  )  )  ;  else   return   displayColumn  .  getType  (  )  .  stringOfCooked  (  displayColumn  .  getCooked  (  this  )  ,  locale  ,  style  )  ; 
} 

public   final   int   hashCode  (  )  { 
if  (  troid  ==  null  )  throw   new   InvalidOperationOnFloatingPersistentPoemException  (  this  )  ; 
return   getTable  (  )  .  hashCode  (  )  +  troid  (  )  .  intValue  (  )  ; 
} 

public   final   boolean   equals  (  Object   object  )  { 
if  (  object  ==  null  ||  !  (  object   instanceof   Persistent  )  )  return   false  ;  else  { 
Persistent   other  =  (  Persistent  )  object  ; 
return   other  .  troid  (  )  ==  troid  (  )  &&  other  .  getTable  (  )  ==  getTable  (  )  ; 
} 
} 

public   synchronized   void   invalidate  (  )  { 
assertNotFloating  (  )  ; 
super  .  invalidate  (  )  ; 
extras  =  null  ; 
} 

protected   Object   clone  (  )  { 
assertCanRead  (  )  ; 
Persistent   it  ; 
try  { 
it  =  (  Persistent  )  super  .  clone  (  )  ; 
}  catch  (  CloneNotSupportedException   e  )  { 
throw   new   UnexpectedExceptionPoemException  (  e  )  ; 
} 
it  .  extras  =  (  Object  [  ]  )  extras  (  )  .  clone  (  )  ; 
it  .  reset  (  )  ; 
it  .  troid  =  null  ; 
it  .  status  =  NONEXISTENT  ; 
return   it  ; 
} 

public   void   dump  (  PrintStream   p  )  { 
p  .  println  (  getTable  (  )  .  getName  (  )  +  "/"  +  troid  (  )  )  ; 
for  (  Enumeration   f  =  getRecordDisplayFields  (  )  ;  f  .  hasMoreElements  (  )  ;  )  { 
p  .  print  (  "  "  )  ; 
(  (  Field  )  f  .  nextElement  (  )  )  .  dump  (  p  )  ; 
p  .  println  (  )  ; 
} 
} 










public   void   postWrite  (  )  { 
} 








public   void   postInsert  (  )  { 
} 











public   void   postModify  (  )  { 
} 












public   void   preEdit  (  )  { 
} 

























public   void   postEdit  (  boolean   creating  )  { 
} 





public   String   countMatchSQL  (  boolean   includeDeleted  ,  boolean   cannotSelect  )  { 
return   getTable  (  )  .  countSQL  (  fromClause  (  )  ,  getTable  (  )  .  whereClause  (  this  )  ,  includeDeleted  ,  cannotSelect  )  ; 
} 












public   String   fromClause  (  )  { 
String   result  =  getTable  (  )  .  quotedName  (  )  ; 
Table  [  ]  other  =  otherMatchTables  (  )  ; 
for  (  int   i  =  0  ;  i  <  other  .  length  ;  i  ++  )  { 
result  +=  ", "  +  other  [  i  ]  .  quotedName  (  )  ; 
} 
return   result  ; 
} 








public   Table  [  ]  otherMatchTables  (  )  { 
return   new   Table  [  0  ]  ; 
} 
























public   class   Saved  { 

Persistent   copy  =  null  ; 






public   void   save  (  )  { 
if  (  copy  !=  null  )  { 
throw   new   IllegalStateException  (  "Bug in caller"  )  ; 
} 
ensureSaved  (  )  ; 
} 




public   final   void   ensureSaved  (  )  { 
if  (  copy  ==  null  )  { 
copy  =  (  Persistent  )  Persistent  .  this  .  clone  (  )  ; 
} 
} 










public   void   substituteNew  (  )  { 
if  (  copy  !=  null  )  { 
throw   new   IllegalStateException  (  "Bug in caller"  )  ; 
} 
copy  =  getTable  (  )  .  newPersistent  (  )  ; 
} 




public   void   discard  (  )  { 
copy  =  null  ; 
} 




public   Persistent   get  (  )  { 
return   copy  ; 
} 




public   boolean   isDifferent  (  Column   column  )  { 
return  !  column  .  asField  (  Persistent  .  this  )  .  sameRawAs  (  column  .  asField  (  copy  )  )  ; 
} 
} 
} 

