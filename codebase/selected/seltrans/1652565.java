package   org  .  dbe  .  composer  .  wfengine  .  bpel  .  server  .  logging  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  sql  .  ResultSet  ; 
import   java  .  sql  .  SQLException  ; 
import   org  .  apache  .  commons  .  dbutils  .  QueryRunner  ; 
import   org  .  apache  .  commons  .  dbutils  .  ResultSetHandler  ; 
import   org  .  dbe  .  composer  .  wfengine  .  bpel  .  server  .  engine  .  storage  .  sql  .  SdlSQLConfig  ; 
import   org  .  dbe  .  composer  .  wfengine  .  util  .  SdlCloser  ; 




public   class   SdlSequentialClobStream   extends   Reader   implements   ResultSetHandler  { 

private   static   final   String   SQL_GET_PROCESS_LOG_STREAM  =  "SequentialClobStream.GetProcessLogStream"  ; 


private   QueryRunner   mQueryRunner  ; 


private   Long   mProcessId  ; 


private   int   mCounter  ; 


private   Reader   mCurrentStream  ; 


private   String   mSqlStatement  ; 






public   SdlSequentialClobStream  (  SdlSQLConfig   aSQLConfig  ,  QueryRunner   aQueryRunner  ,  long   aProcessId  )  { 
mSqlStatement  =  aSQLConfig  .  getSQLStatement  (  SQL_GET_PROCESS_LOG_STREAM  )  +  aSQLConfig  .  getLimitStatement  (  1  )  ; 
mQueryRunner  =  aQueryRunner  ; 
mProcessId  =  new   Long  (  aProcessId  )  ; 
} 




public   int   read  (  char  [  ]  aCbuf  ,  int   aOff  ,  int   aLen  )  throws   IOException  { 
makeStreamReady  (  )  ; 
int   result  =  -  1  ; 
try  { 
while  (  mCurrentStream  !=  null  &&  (  result  =  mCurrentStream  .  read  (  aCbuf  ,  aOff  ,  aLen  )  )  ==  -  1  )  prepNextStream  (  )  ; 
}  catch  (  IOException   e  )  { 
close  (  )  ; 
throw   e  ; 
} 
return   result  ; 
} 






private   void   prepNextStream  (  )  throws   IOException  { 
closeCurrentStream  (  )  ; 
makeStreamReady  (  )  ; 
} 




private   void   closeCurrentStream  (  )  { 
SdlCloser  .  close  (  mCurrentStream  )  ; 
mCurrentStream  =  null  ; 
} 





private   void   makeStreamReady  (  )  throws   IOException  { 
if  (  mCurrentStream  ==  null  )  { 
Object  [  ]  args  =  {  mProcessId  ,  new   Integer  (  mCounter  )  }  ; 
try  { 
mCurrentStream  =  (  Reader  )  mQueryRunner  .  query  (  mSqlStatement  ,  args  ,  this  )  ; 
}  catch  (  SQLException   e  )  { 
throw   new   IOException  (  e  .  getMessage  (  )  )  ; 
} 
} 
} 




public   void   close  (  )  throws   IOException  { 
closeCurrentStream  (  )  ; 
} 




public   Object   handle  (  ResultSet   rs  )  throws   SQLException  { 
Reader   input  =  null  ; 
if  (  rs  .  next  (  )  )  { 
mCounter  =  rs  .  getInt  (  2  )  ; 
Reader   inFromClob  =  rs  .  getClob  (  1  )  .  getCharacterStream  (  )  ; 
char  [  ]  buffer  =  new   char  [  1024  *  4  ]  ; 
int   read  ; 
StringWriter   writer  =  new   StringWriter  (  )  ; 
try  { 
while  (  (  read  =  inFromClob  .  read  (  buffer  )  )  !=  -  1  )  { 
writer  .  write  (  buffer  ,  0  ,  read  )  ; 
} 
input  =  new   StringReader  (  writer  .  toString  (  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   SQLException  (  e  .  getMessage  (  )  )  ; 
}  finally  { 
SdlCloser  .  close  (  inFromClob  )  ; 
} 
} 
return   input  ; 
} 
} 

