package   dinamica  ; 

import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  sql  .  *  ; 





























public   class   Recordset   implements   Serializable  { 




private   static   final   long   serialVersionUID  =  1L  ; 


private   HashMap  <  String  ,  RecordsetField  >  _fields  =  new   HashMap  <  String  ,  RecordsetField  >  (  )  ; 


private   ArrayList  <  Record  >  _data  =  new   ArrayList  <  Record  >  (  )  ; 


private   int   _recordNumber  =  -  1  ; 


private   int   _pageCount  =  0  ; 

private   int   _pageSize  =  0  ; 

private   int   _currentPage  =  0  ; 


private   String   _ID  =  null  ; 

private   String   _lastSortColName  =  null  ; 

private   String   _lastSortMode  =  null  ; 






public   int   getPageSize  (  )  { 
return   _pageSize  ; 
} 







public   Recordset   getRecordsetInfo  (  )  throws   Throwable  { 
Recordset   rs  =  new   Recordset  (  )  ; 
rs  .  append  (  "recordcount"  ,  Types  .  INTEGER  )  ; 
rs  .  append  (  "pagecount"  ,  Types  .  INTEGER  )  ; 
rs  .  append  (  "currentpage"  ,  Types  .  INTEGER  )  ; 
rs  .  append  (  _ID  +  ".recordcount"  ,  Types  .  INTEGER  )  ; 
rs  .  addNew  (  )  ; 
rs  .  setValue  (  "recordcount"  ,  Integer  .  valueOf  (  _data  .  size  (  )  )  )  ; 
rs  .  setValue  (  "pagecount"  ,  Integer  .  valueOf  (  _pageCount  )  )  ; 
rs  .  setValue  (  "currentpage"  ,  Integer  .  valueOf  (  _currentPage  )  )  ; 
rs  .  setValue  (  _ID  +  ".recordcount"  ,  Integer  .  valueOf  (  _data  .  size  (  )  )  )  ; 
return   rs  ; 
} 





public   void   setID  (  String   id  )  { 
_ID  =  id  ; 
} 





public   int   getPageCount  (  )  { 
return   _pageCount  ; 
} 





public   int   getPageNumber  (  )  { 
return   _currentPage  ; 
} 





public   void   setPageSize  (  int   p  )  throws   Throwable  { 
if  (  p  <=  0  )  { 
throw   new   Throwable  (  "Invalid page size, must be > 0!"  )  ; 
} 
if  (  _data  .  size  (  )  ==  0  )  { 
throw   new   Throwable  (  "Invalid page size, recordset is empty!"  )  ; 
} 
_pageSize  =  p  ; 
java  .  math  .  BigDecimal   b1  =  new   java  .  math  .  BigDecimal  (  _data  .  size  (  )  )  ; 
java  .  math  .  BigDecimal   b2  =  new   java  .  math  .  BigDecimal  (  _pageSize  )  ; 
_pageCount  =  b1  .  divide  (  b2  ,  java  .  math  .  BigDecimal  .  ROUND_UP  )  .  intValue  (  )  ; 
if  (  getRecordCount  (  )  >  0  )  _currentPage  =  1  ; 
} 





@  SuppressWarnings  (  "unchecked"  ) 
public   Recordset   getPage  (  int   p  )  throws   Throwable  { 
if  (  p  <  1  ||  p  >  _pageCount  )  throw   new   Throwable  (  "Invalid page number: "  +  p  +  " - the Recordset contains "  +  _pageCount  +  " pages."  )  ; 
_currentPage  =  p  ; 
int   row1  =  (  p  -  1  )  *  _pageSize  ; 
int   row2  =  (  p  *  _pageSize  )  -  1  ; 
if  (  row2  >  (  _data  .  size  (  )  -  1  )  )  row2  =  _data  .  size  (  )  -  1  ; 
ArrayList  <  Record  >  newData  =  new   ArrayList  <  Record  >  (  _pageSize  )  ; 
for  (  int   i  =  row1  ;  i  <=  row2  ;  i  ++  )  { 
newData  .  add  (  (  Record  )  _data  .  get  (  i  )  )  ; 
} 
Recordset   x  =  new   Recordset  (  )  ; 
x  .  setFields  (  (  HashMap  <  String  ,  RecordsetField  >  )  _fields  .  clone  (  )  )  ; 
x  .  setData  (  newData  )  ; 
return   x  ; 
} 





@  SuppressWarnings  (  "unchecked"  ) 
protected   void   setFields  (  HashMap   fields  )  { 
this  .  _fields  =  fields  ; 
} 





@  SuppressWarnings  (  "unchecked"  ) 
protected   void   setData  (  ArrayList   data  )  { 
this  .  _data  =  data  ; 
} 





public   int   getRecordNumber  (  )  { 
return   _recordNumber  ; 
} 





public   int   getRecordCount  (  )  { 
return   _data  .  size  (  )  ; 
} 





public   int   getFieldCount  (  )  { 
return   _fields  .  size  (  )  ; 
} 






public   HashMap  <  String  ,  RecordsetField  >  getFields  (  )  { 
return   _fields  ; 
} 





public   ArrayList  <  Record  >  getData  (  )  { 
return   _data  ; 
} 








private   void   append  (  String   fieldName  ,  String   nativeSqlType  ,  int   type  )  { 
RecordsetField   f  =  new   RecordsetField  (  fieldName  ,  nativeSqlType  ,  type  )  ; 
_fields  .  put  (  fieldName  ,  f  )  ; 
} 








public   void   append  (  String   fieldName  ,  int   type  )  throws   RecordsetException  { 
String   sqlTypeName  =  null  ; 
switch  (  type  )  { 
case   Types  .  INTEGER  : 
sqlTypeName  =  "INTEGER"  ; 
break  ; 
case   Types  .  BIGINT  : 
sqlTypeName  =  "LONG"  ; 
break  ; 
case   Types  .  VARCHAR  : 
sqlTypeName  =  "VARCHAR"  ; 
break  ; 
case   Types  .  DATE  : 
sqlTypeName  =  "DATE"  ; 
break  ; 
case   Types  .  TIMESTAMP  : 
sqlTypeName  =  "TIMESTAMP"  ; 
break  ; 
case   Types  .  DOUBLE  : 
sqlTypeName  =  "DOUBLE"  ; 
break  ; 
} 
if  (  sqlTypeName  ==  null  )  { 
String   args  [  ]  =  {  String  .  valueOf  (  type  )  }  ; 
String   msg  =  Errors  .  INVALID_DATATYPE  ; 
msg  =  MessageFormat  .  format  (  msg  ,  (  Object  [  ]  )  args  )  ; 
throw   new   RecordsetException  (  msg  )  ; 
} 
append  (  fieldName  ,  sqlTypeName  ,  type  )  ; 
} 






public   void   addNew  (  )  { 
HashMap  <  String  ,  Object  >  values  =  new   HashMap  <  String  ,  Object  >  (  )  ; 
Iterator  <  String  >  i  =  _fields  .  keySet  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
String   f  =  (  String  )  i  .  next  (  )  ; 
values  .  put  (  f  ,  null  )  ; 
} 
_data  .  add  (  new   Record  (  values  )  )  ; 
_recordNumber  ++  ; 
} 






public   void   setRecordNumber  (  int   recNum  )  throws   RecordsetException  { 
checkRecordPosition  (  recNum  )  ; 
_recordNumber  =  recNum  ; 
} 







public   void   setValue  (  String   fieldName  ,  Object   value  )  throws   RecordsetException  { 
checkRecordPosition  (  )  ; 
RecordsetField   f  ; 
try  { 
f  =  getField  (  fieldName  )  ; 
}  catch  (  Throwable   e  )  { 
throw   new   RecordsetException  (  e  .  getMessage  (  )  )  ; 
} 
if  (  value  !=  null  )  { 
switch  (  f  .  getType  (  )  )  { 
case   java  .  sql  .  Types  .  DATE  : 
if  (  !  (  value   instanceof   java  .  util  .  Date  )  )  throw   new   RecordsetException  (  "Invalid data type of field: "  +  fieldName  +  "; passed value must be a DATE object."  )  ; 
break  ; 
case   java  .  sql  .  Types  .  INTEGER  : 
if  (  !  (  value   instanceof   java  .  lang  .  Integer  )  &&  !  (  value   instanceof   java  .  lang  .  Long  )  )  throw   new   RecordsetException  (  "Invalid data type of field: "  +  fieldName  +  "; passed value must be an INTEGER object."  )  ; 
break  ; 
case   java  .  sql  .  Types  .  DOUBLE  : 
if  (  !  (  value   instanceof   java  .  lang  .  Double  )  )  throw   new   RecordsetException  (  "Invalid data type of field: "  +  fieldName  +  "; passed value must be an DOUBLE object."  )  ; 
break  ; 
} 
} 
Record   rec  =  (  Record  )  _data  .  get  (  _recordNumber  )  ; 
rec  .  setValue  (  fieldName  ,  value  )  ; 
} 







public   Object   getValue  (  String   fieldName  )  throws   Throwable  { 
checkRecordPosition  (  )  ; 
if  (  fieldName  .  equals  (  "_rowIndex"  )  )  { 
return   Integer  .  valueOf  (  _recordNumber  )  ; 
}  else   if  (  fieldName  .  equals  (  "_rowNumber"  )  )  { 
return   Integer  .  valueOf  (  _recordNumber  +  1  )  ; 
}  else  { 
Record   rec  =  (  Record  )  _data  .  get  (  _recordNumber  )  ; 
return   rec  .  getFieldValue  (  fieldName  )  ; 
} 
} 









private   void   loadRecords  (  java  .  sql  .  ResultSet   rs  )  throws   Throwable  { 
ResultSetMetaData   md  =  rs  .  getMetaData  (  )  ; 
int   cols  =  md  .  getColumnCount  (  )  ; 
for  (  int   i  =  1  ;  i  <=  cols  ;  i  ++  )  { 
append  (  md  .  getColumnName  (  i  )  .  toLowerCase  (  )  ,  md  .  getColumnTypeName  (  i  )  ,  md  .  getColumnType  (  i  )  )  ; 
} 
while  (  rs  .  next  (  )  )  { 
HashMap  <  String  ,  Object  >  flds  =  new   HashMap  <  String  ,  Object  >  (  cols  )  ; 
for  (  int   i  =  1  ;  i  <=  cols  ;  i  ++  )  { 
flds  .  put  (  md  .  getColumnName  (  i  )  .  toLowerCase  (  )  ,  rs  .  getObject  (  i  )  )  ; 
} 
_data  .  add  (  new   Record  (  flds  )  )  ; 
} 
} 








public   Recordset  (  ResultSet   rs  )  throws   Throwable  { 
loadRecords  (  rs  )  ; 
} 







public   Recordset  (  Connection   conn  ,  String   sql  )  throws   Throwable  { 
this  (  conn  ,  sql  ,  0  )  ; 
} 








public   Recordset  (  java  .  sql  .  Connection   conn  ,  String   sql  ,  int   limit  )  throws   Throwable  { 
ResultSet   rs  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
stmt  =  conn  .  createStatement  (  )  ; 
if  (  limit  >  0  )  stmt  .  setMaxRows  (  limit  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
loadRecords  (  rs  )  ; 
}  catch  (  Throwable   e  )  { 
throw   e  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
} 
} 






public   Recordset  (  )  { 
} 





public   boolean   next  (  )  { 
if  (  _recordNumber  <  (  _data  .  size  (  )  -  1  )  )  { 
_recordNumber  ++  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 






public   String   toString  (  )  { 
StringWriter   sw  =  new   StringWriter  (  1000  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
pw  .  println  (  "Recordset Information"  )  ; 
pw  .  println  (  "Record Count: "  +  getRecordCount  (  )  )  ; 
pw  .  println  (  "Field Count: "  +  getFieldCount  (  )  )  ; 
pw  .  println  (  "Structure:"  )  ; 
pw  .  println  (  "----------------------------------"  )  ; 
pw  .  println  (  "NAME|SQL-TYPE-NAME|JDBC-TYPE-ID"  )  ; 
Iterator  <  RecordsetField  >  i  =  _fields  .  values  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
RecordsetField   f  =  (  RecordsetField  )  i  .  next  (  )  ; 
pw  .  println  (  f  .  getName  (  )  +  "|"  +  f  .  getSqlTypeName  (  )  +  "|"  +  f  .  getType  (  )  )  ; 
} 
return   sw  .  toString  (  )  ; 
} 






public   void   top  (  )  { 
_recordNumber  =  -  1  ; 
} 





public   void   first  (  )  throws   Throwable  { 
setRecordNumber  (  0  )  ; 
} 





public   void   last  (  )  throws   Throwable  { 
setRecordNumber  (  _data  .  size  (  )  -  1  )  ; 
} 





public   void   delete  (  int   recNum  )  throws   Throwable  { 
checkRecordPosition  (  recNum  )  ; 
_data  .  remove  (  recNum  )  ; 
_recordNumber  --  ; 
} 







public   RecordsetField   getField  (  String   fieldName  )  throws   Throwable  { 
if  (  _fields  .  containsKey  (  fieldName  )  )  return  (  RecordsetField  )  _fields  .  get  (  fieldName  )  ;  else   throw   new   Throwable  (  "Field not found:"  +  fieldName  )  ; 
} 













public   void   copyValues  (  Recordset   rs  )  throws   Throwable  { 
checkRecordPosition  (  )  ; 
HashMap  <  String  ,  RecordsetField  >  flds  =  rs  .  getFields  (  )  ; 
Iterator  <  RecordsetField  >  i  =  _fields  .  values  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
RecordsetField   f  =  (  RecordsetField  )  i  .  next  (  )  ; 
String   name  =  f  .  getName  (  )  ; 
if  (  flds  .  containsKey  (  name  )  )  { 
rs  .  setValue  (  name  ,  getValue  (  name  )  )  ; 
} 
} 
} 







public   String   getString  (  String   colName  )  throws   Throwable  { 
Object   obj  =  getValue  (  colName  )  ; 
if  (  obj  !=  null  )  return   String  .  valueOf  (  obj  )  ;  else   return   null  ; 
} 







public   java  .  util  .  Date   getDate  (  String   colName  )  throws   Throwable  { 
java  .  util  .  Date   d  =  null  ; 
d  =  (  java  .  util  .  Date  )  getValue  (  colName  )  ; 
return   d  ; 
} 







public   double   getDouble  (  String   colName  )  throws   Throwable  { 
Double   d  =  new   Double  (  String  .  valueOf  (  getValue  (  colName  )  )  )  ; 
return   d  .  doubleValue  (  )  ; 
} 







public   int   getInt  (  String   colName  )  throws   Throwable  { 
Integer   i  =  new   Integer  (  String  .  valueOf  (  getValue  (  colName  )  )  )  ; 
return   i  .  intValue  (  )  ; 
} 







public   Integer   getInteger  (  String   colName  )  throws   Throwable  { 
Integer   i  =  new   Integer  (  String  .  valueOf  (  getValue  (  colName  )  )  )  ; 
return   i  ; 
} 








public   boolean   isNull  (  String   colName  )  throws   Throwable  { 
if  (  getValue  (  colName  )  ==  null  )  return   true  ;  else   return   false  ; 
} 






public   boolean   containsField  (  String   name  )  { 
if  (  _fields  .  containsKey  (  name  )  )  return   true  ;  else   return   false  ; 
} 







private   void   checkRecordPosition  (  int   recNum  )  throws   RecordsetException  { 
if  (  recNum  <  0  ||  recNum  >  _data  .  size  (  )  -  1  )  { 
StringBuffer   errMsg  =  new   StringBuffer  (  )  ; 
errMsg  .  append  (  "Invalid record position: "  +  recNum  +  "; "  )  ; 
if  (  recNum  ==  -  1  )  errMsg  .  append  (  "After creating a Recordset you must move to a valid record using next(), first(), last() or setRecordNumber() methods before attempting read/write operations with any record of this Recordset; "  )  ; 
errMsg  .  append  (  "This Recordset contains "  +  _data  .  size  (  )  +  " record(s); Set the record position between 0 and N-1 where N is the number of records."  )  ; 
throw   new   RecordsetException  (  errMsg  .  toString  (  )  )  ; 
} 
} 







private   void   checkRecordPosition  (  )  throws   RecordsetException  { 
checkRecordPosition  (  this  .  _recordNumber  )  ; 
} 






public   void   setChildrenRecordset  (  Recordset   rs  )  throws   Throwable  { 
checkRecordPosition  (  )  ; 
Record   rec  =  (  Record  )  _data  .  get  (  _recordNumber  )  ; 
rec  .  setChildren  (  rs  )  ; 
} 






public   Recordset   getChildrenRecordset  (  )  throws   Throwable  { 
checkRecordPosition  (  )  ; 
Record   rec  =  (  Record  )  _data  .  get  (  _recordNumber  )  ; 
return   rec  .  getChildren  (  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
public   void   sort  (  String   col  )  throws   Throwable  { 
if  (  _lastSortColName  !=  null  &&  _lastSortColName  .  equals  (  col  )  )  { 
if  (  _lastSortMode  ==  null  )  _lastSortMode  =  "desc"  ;  else   _lastSortMode  =  null  ; 
}  else  { 
_lastSortMode  =  null  ; 
_lastSortColName  =  col  ; 
} 
Comparator   comp  =  new   Comp  (  col  ,  _lastSortMode  )  ; 
Collections  .  sort  (  _data  ,  comp  )  ; 
} 




class   Comp   implements   Serializable  ,  Comparator  <  Record  >  { 




private   static   final   long   serialVersionUID  =  1L  ; 

private   String   _sortCol  =  null  ; 

private   String   _sortMode  =  null  ; 

public   Comp  (  String   colName  ,  String   sortMode  )  throws   Throwable  { 
_sortMode  =  sortMode  ; 
_sortCol  =  colName  ; 
if  (  !  containsField  (  colName  )  )  throw   new   Throwable  (  "Invalid column name passed to sort() method: "  +  colName  )  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   int   compare  (  Record   r1  ,  Record   r2  )  { 
int   result  =  0  ; 
try  { 
if  (  r1  .  getFieldValue  (  _sortCol  )  ==  null  )  { 
result  =  0  ; 
}  else   if  (  r2  .  getFieldValue  (  _sortCol  )  ==  null  )  { 
result  =  1  ; 
}  else  { 
Comparable  <  Object  >  x1  =  (  Comparable  )  r1  .  getFieldValue  (  _sortCol  )  ; 
result  =  x1  .  compareTo  (  r2  .  getFieldValue  (  _sortCol  )  )  ; 
if  (  _sortMode  !=  null  )  { 
if  (  result  <  0  )  result  =  1  ;  else   if  (  result  >  0  )  result  =  -  1  ; 
} 
} 
}  catch  (  Throwable   e  )  { 
System  .  err  .  println  (  "SORT ERROR: "  +  e  .  getMessage  (  )  )  ; 
} 
return   result  ; 
} 
} 









public   Recordset   getMetaData  (  )  throws   Throwable  { 
Recordset   rs  =  new   Recordset  (  )  ; 
rs  .  append  (  "name"  ,  java  .  sql  .  Types  .  VARCHAR  )  ; 
rs  .  append  (  "typename"  ,  java  .  sql  .  Types  .  VARCHAR  )  ; 
rs  .  append  (  "typeid"  ,  java  .  sql  .  Types  .  INTEGER  )  ; 
HashMap  <  String  ,  RecordsetField  >  flds  =  this  .  getFields  (  )  ; 
Iterator  <  RecordsetField  >  i  =  flds  .  values  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
RecordsetField   f  =  (  RecordsetField  )  i  .  next  (  )  ; 
rs  .  addNew  (  )  ; 
rs  .  setValue  (  "name"  ,  f  .  getName  (  )  )  ; 
rs  .  setValue  (  "typename"  ,  f  .  getSqlTypeName  (  )  )  ; 
rs  .  setValue  (  "typeid"  ,  Integer  .  valueOf  (  f  .  getType  (  )  )  )  ; 
} 
return   rs  ; 
} 






public   Recordset   copyStructure  (  )  throws   Throwable  { 
Recordset   newRS  =  new   Recordset  (  )  ; 
Recordset   infoRS  =  getMetaData  (  )  ; 
infoRS  .  top  (  )  ; 
while  (  infoRS  .  next  (  )  )  { 
String   name  =  infoRS  .  getString  (  "name"  )  ; 
int   jdbcTypeId  =  infoRS  .  getInt  (  "typeid"  )  ; 
newRS  .  append  (  name  ,  jdbcTypeId  )  ; 
} 
return   newRS  ; 
} 




public   void   clear  (  )  throws   Throwable  { 
checkRecordPosition  (  )  ; 
Iterator  <  RecordsetField  >  i  =  _fields  .  values  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
RecordsetField   f  =  (  RecordsetField  )  i  .  next  (  )  ; 
setValue  (  f  .  getName  (  )  ,  null  )  ; 
} 
} 








public   int   findRecord  (  String   colName  ,  int   value  )  throws   Throwable  { 
int   rc  =  -  1  ; 
top  (  )  ; 
while  (  next  (  )  )  { 
if  (  value  ==  getInt  (  colName  )  )  { 
rc  =  this  .  getRecordNumber  (  )  ; 
break  ; 
} 
} 
return   rc  ; 
} 








public   int   findRecord  (  String   colName  ,  String   value  )  throws   Throwable  { 
int   rc  =  -  1  ; 
top  (  )  ; 
while  (  next  (  )  )  { 
if  (  value  .  equals  (  getString  (  colName  )  )  )  { 
rc  =  this  .  getRecordNumber  (  )  ; 
break  ; 
} 
} 
return   rc  ; 
} 








public   int   findRecord  (  String   colName  ,  java  .  util  .  Date   value  )  throws   Throwable  { 
int   rc  =  -  1  ; 
top  (  )  ; 
while  (  next  (  )  )  { 
if  (  value  .  compareTo  (  getDate  (  colName  )  )  ==  0  )  { 
rc  =  this  .  getRecordNumber  (  )  ; 
break  ; 
} 
} 
return   rc  ; 
} 
} 

