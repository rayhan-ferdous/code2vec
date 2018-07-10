package   com  .  jcorporate  .  expresso  .  core  .  dbobj  ; 

import   com  .  jcorporate  .  expresso  .  core  .  db  .  DBConnection  ; 
import   com  .  jcorporate  .  expresso  .  core  .  db  .  DBConnectionPool  ; 
import   com  .  jcorporate  .  expresso  .  core  .  db  .  DBException  ; 
import   com  .  jcorporate  .  expresso  .  core  .  misc  .  StringUtil  ; 
import   com  .  jcorporate  .  expresso  .  kernel  .  util  .  FastStringBuffer  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  sql  .  PreparedStatement  ; 
import   java  .  sql  .  SQLException  ; 















































public   class   LOBSupport  { 

protected   static   LOBSupport   theInstance  =  null  ; 

private   static   Logger   log  =  Logger  .  getLogger  (  "com.jcorporate"  +  ".expresso.core.dbobj.LOBSupport"  )  ; 




protected   LOBSupport  (  )  { 
} 





public   static   synchronized   LOBSupport   getInstance  (  )  { 
if  (  theInstance  ==  null  )  { 
theInstance  =  new   LOBSupport  (  )  ; 
} 
return   theInstance  ; 
} 

public   String   getCLOB  (  DBObject   baseObject  ,  String   fieldName  )  throws   DBException  { 
DBConnectionPool   aPool  =  DBConnectionPool  .  getInstance  (  baseObject  .  getDataContext  (  )  )  ; 
DBConnection   localConnection  =  aPool  .  getConnection  (  )  ; 
String   returnValue  =  null  ; 
try  { 
returnValue  =  this  .  getCLOB  (  baseObject  ,  fieldName  ,  localConnection  )  ; 
}  finally  { 
aPool  .  release  (  localConnection  )  ; 
} 
return   returnValue  ; 
} 













public   String   getCLOB  (  DBObject   baseObject  ,  String   fieldName  ,  DBConnection   theConnection  )  throws   DBException  { 
prepSelectResultSet  (  baseObject  ,  fieldName  ,  theConnection  )  ; 
if  (  theConnection  .  next  (  )  )  { 
return   StringUtil  .  notNull  (  theConnection  .  getString  (  1  )  )  ; 
} 
return  ""  ; 
} 










public   void   setCLOB  (  DBObject   baseObject  ,  String   fieldName  ,  String   theData  ,  DBConnection   theConnection  )  throws   DBException  { 
PreparedStatement   preparedStatement  =  prepUpdate  (  baseObject  ,  fieldName  ,  theConnection  )  ; 
try  { 
if  (  "interbase.interclient.Driver"  .  equals  (  theConnection  .  getDBDriver  (  )  )  )  { 
byte  [  ]  data  =  theData  .  getBytes  (  )  ; 
java  .  io  .  ByteArrayInputStream   bis  =  new   java  .  io  .  ByteArrayInputStream  (  data  )  ; 
preparedStatement  .  setAsciiStream  (  1  ,  bis  ,  data  .  length  )  ; 
}  else  { 
java  .  io  .  Reader   r  =  new   java  .  io  .  StringReader  (  theData  )  ; 
preparedStatement  .  setCharacterStream  (  1  ,  r  ,  theData  .  length  (  )  )  ; 
} 
}  catch  (  SQLException   ex  )  { 
throw   new   DBException  (  "Unable to set CharacterStream to CLOB object."  ,  ex  )  ; 
} 
finalizeUpdate  (  theConnection  )  ; 
} 













public   byte  [  ]  getBLOB  (  DBObject   baseObject  ,  String   fieldName  ,  DBConnection   theConnection  )  throws   DBException  { 
prepSelectResultSet  (  baseObject  ,  fieldName  ,  theConnection  )  ; 
if  (  theConnection  .  next  (  )  )  { 
byte   retVal  [  ]  =  theConnection  .  getBytes  (  1  )  ; 
if  (  retVal  ==  null  )  { 
return   new   byte  [  0  ]  ; 
}  else  { 
return   retVal  ; 
} 
} 
return   new   byte  [  0  ]  ; 
} 









public   void   setBLOB  (  DBObject   baseObject  ,  String   fieldName  ,  byte   theData  [  ]  ,  DBConnection   theConnection  )  throws   DBException  { 
PreparedStatement   preparedStatement  =  prepUpdate  (  baseObject  ,  fieldName  ,  theConnection  )  ; 
try  { 
preparedStatement  .  setBytes  (  1  ,  theData  )  ; 
}  catch  (  SQLException   ex  )  { 
throw   new   DBException  (  "Error setting BLOB object"  ,  ex  )  ; 
} 
finalizeUpdate  (  theConnection  )  ; 
return  ; 
} 











public   void   setBLOB  (  DBObject   baseObject  ,  String   fieldName  ,  InputStream   theData  ,  int   dataLength  ,  DBConnection   theConnection  )  throws   DBException  { 
PreparedStatement   preparedStatement  =  prepUpdate  (  baseObject  ,  fieldName  ,  theConnection  )  ; 
try  { 
preparedStatement  .  setBinaryStream  (  1  ,  theData  ,  dataLength  )  ; 
}  catch  (  SQLException   ex  )  { 
throw   new   DBException  (  "Error setting BLOB object"  ,  ex  )  ; 
} 
finalizeUpdate  (  theConnection  )  ; 
return  ; 
} 










protected   void   prepSelectResultSet  (  DBObject   baseObject  ,  String   fieldName  ,  DBConnection   theConnection  )  throws   DBException  { 
FastStringBuffer   prepStatement  =  FastStringBuffer  .  getInstance  (  )  ; 
try  { 
prepStatement  .  append  (  "SELECT "  )  ; 
prepStatement  .  append  (  fieldName  )  ; 
prepStatement  .  append  (  " from "  )  ; 
prepStatement  .  append  (  baseObject  .  getJDBCMetaData  (  )  .  getTargetTable  (  )  )  ; 
String   whereClause  =  baseObject  .  buildWhereClause  (  false  )  ; 
prepStatement  .  append  (  whereClause  )  ; 
String   thePrepString  =  prepStatement  .  toString  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Preparing prepared statement:  "  +  thePrepString  )  ; 
} 
PreparedStatement   prep  =  theConnection  .  createPreparedStatement  (  thePrepString  )  ; 
if  (  prep  ==  null  )  { 
throw   new   DBException  (  "Unable to create prepared statement for CLOB retrieval."  +  "  Check DBConnection log for details"  )  ; 
} 
theConnection  .  execute  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Succesfully executed prepared statement"  )  ; 
} 
}  finally  { 
prepStatement  .  release  (  )  ; 
prepStatement  =  null  ; 
} 
} 









protected   PreparedStatement   prepUpdate  (  DBObject   baseObject  ,  String   fieldName  ,  DBConnection   theConnection  )  throws   DBException  { 
String   whereClause  =  baseObject  .  buildWhereClause  (  false  )  ; 
FastStringBuffer   prepStatement  =  FastStringBuffer  .  getInstance  (  )  ; 
String   theSQL  =  null  ; 
try  { 
prepStatement  .  append  (  "UPDATE "  )  ; 
prepStatement  .  append  (  baseObject  .  getJDBCMetaData  (  )  .  getTargetTable  (  )  )  ; 
prepStatement  .  append  (  " SET "  )  ; 
prepStatement  .  append  (  fieldName  )  ; 
prepStatement  .  append  (  " = ? "  )  ; 
prepStatement  .  append  (  whereClause  )  ; 
theSQL  =  prepStatement  .  toString  (  )  ; 
}  finally  { 
prepStatement  .  release  (  )  ; 
prepStatement  =  null  ; 
} 
return   theConnection  .  createPreparedStatement  (  theSQL  )  ; 
} 

protected   void   finalizeUpdate  (  DBConnection   theConnection  )  throws   DBException  { 
boolean   alreadyInTransaction  =  !  (  theConnection  .  getAutoCommit  (  )  )  ; 
boolean   success  =  false  ; 
if  (  !  alreadyInTransaction  &&  theConnection  .  supportsTransactions  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Turning off auto-commit"  )  ; 
} 
theConnection  .  setAutoCommit  (  false  )  ; 
} 
try  { 
theConnection  .  executeUpdate  (  null  )  ; 
success  =  true  ; 
}  finally  { 
if  (  success  ==  false  )  { 
if  (  !  alreadyInTransaction  &&  theConnection  .  supportsTransactions  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "rolling back"  )  ; 
} 
theConnection  .  rollback  (  )  ; 
} 
} 
if  (  !  alreadyInTransaction  &&  theConnection  .  supportsTransactions  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "Finishing commit and turning auto-commit back to true"  )  ; 
} 
theConnection  .  commit  (  )  ; 
theConnection  .  setAutoCommit  (  true  )  ; 
} 
} 
} 
} 

