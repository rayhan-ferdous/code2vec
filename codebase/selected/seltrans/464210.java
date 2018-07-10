package   fido  .  db  ; 

import   java  .  util  .  *  ; 
import   java  .  sql  .  *  ; 
import   fido  .  util  .  FidoDataSource  ; 





public   class   LanguageMorphologyTable  { 

private   static   final   String   CONSONANTS  =  "bcdfghjklmnpqrstvwxyz"  ; 

private   static   final   String   VOWELS  =  "aeiou"  ; 

public   LanguageMorphologyTable  (  )  { 
} 

private   int   getFirstRowForTag  (  Statement   stmt  ,  String   language  ,  String   tag  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select Rank from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' order by Rank"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  return   1  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
return   num  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   int   getFirstRegularFormForTag  (  Statement   stmt  ,  String   language  ,  String   tag  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select Rank from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and root like '%*%' order by Rank"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  return   getAppendRowForTag  (  stmt  ,  language  ,  tag  )  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
return   num  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   int   getAppendRowForTag  (  Statement   stmt  ,  String   language  ,  String   tag  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select Rank from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' order by Rank desc"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  return   1  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
return  (  num  +  1  )  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   boolean   isRuleUnique  (  Statement   stmt  ,  String   language  ,  String   tag  ,  String   root  ,  String   surface  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select count(1) from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and "  +  "      Root = '"  +  root  +  "' and "  +  "      Surface = '"  +  surface  +  "' and "  +  "      MorphologyTag = '"  +  tag  +  "'"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  throw   new   SQLException  (  "No rows returned for count(1) query"  )  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
if  (  num  ==  0  )  return   true  ;  else   return   false  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   boolean   determineRecognitionUse  (  String   root  ,  String   surface  )  { 
if  (  root  .  equals  (  surface  )  ==  true  )  return   false  ; 
return   true  ; 
} 

private   void   bumpAllRowsDown  (  Statement   stmt  ,  String   language  ,  String   tag  ,  int   row  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select max(Rank) from LanguageMorphologies where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and Rank >= "  +  row  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  true  )  { 
int   num  =  rs  .  getInt  (  1  )  ; 
for  (  int   i  =  num  ;  i  >=  row  ;  --  i  )  { 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  (  i  +  1  )  +  " where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and Rank = "  +  i  )  ; 
} 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   void   bumpAllRowsUp  (  Statement   stmt  ,  String   language  ,  String   tag  ,  int   row  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select max(Rank) from LanguageMorphologies where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and Rank >= "  +  row  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  true  )  { 
int   num  =  rs  .  getInt  (  1  )  ; 
for  (  int   i  =  row  ;  i  <  num  ;  ++  i  )  { 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  i  +  " where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and "  +  "       Rank = "  +  (  i  +  1  )  )  ; 
} 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 

private   int   findMaxRank  (  Statement   stmt  ,  String   language  ,  String   tag  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select max(Rank) from LanguageMorphologies "  +  "where MorphologyTag = '"  +  tag  +  "' and LanguageName = '"  +  language  +  "'"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  throw   new   SQLException  (  "No rows returned for select max() query"  )  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
return   num  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 
























































public   void   add  (  String   language  ,  String   tag  ,  String   root  ,  String   surface  )  throws   FidoDatabaseException  ,  MorphologyTagNotFoundException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
conn  =  FidoDataSource  .  getConnection  (  )  ; 
conn  .  setAutoCommit  (  false  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
if  (  containsTag  (  stmt  ,  tag  )  ==  false  )  throw   new   MorphologyTagNotFoundException  (  tag  )  ; 
if  (  isRuleUnique  (  stmt  ,  language  ,  tag  ,  root  ,  surface  )  ==  false  )  return  ; 
int   row  ; 
if  (  root  .  equals  (  "*"  )  ==  true  )  row  =  getAppendRowForTag  (  stmt  ,  language  ,  tag  )  ;  else   if  (  root  .  indexOf  (  '*'  )  ==  -  1  )  row  =  getFirstRowForTag  (  stmt  ,  language  ,  tag  )  ;  else   row  =  getFirstRegularFormForTag  (  stmt  ,  language  ,  tag  )  ; 
boolean   use  =  determineRecognitionUse  (  root  ,  surface  )  ; 
bumpAllRowsDown  (  stmt  ,  language  ,  tag  ,  row  )  ; 
String   sql  =  "insert into LanguageMorphologies (LanguageName, Rank, Root, Surface, MorphologyTag, Used) "  +  "values ('"  +  language  +  "', "  +  row  +  ", '"  +  root  +  "', '"  +  surface  +  "', '"  +  tag  +  "', "  ; 
if  (  use  ==  true  )  sql  =  sql  +  "TRUE)"  ;  else   sql  =  sql  +  "FALSE)"  ; 
stmt  .  executeUpdate  (  sql  )  ; 
conn  .  commit  (  )  ; 
}  catch  (  SQLException   e  )  { 
if  (  conn  !=  null  )  conn  .  rollback  (  )  ; 
throw   e  ; 
}  finally  { 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

public   void   modify  (  String   language  ,  String   tag  ,  int   rank  ,  String   root  ,  String   surface  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
boolean   use  =  determineRecognitionUse  (  root  ,  surface  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  "update LanguageMorphologies set Root = '"  +  root  +  "', Surface = '"  +  surface  +  "', "  )  ; 
if  (  use  ==  true  )  sb  .  append  (  "used = TRUE "  )  ;  else   sb  .  append  (  "used = FALSE "  )  ; 
sb  .  append  (  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and "  +  "      Rank = "  +  rank  )  ; 
String   sql  =  sb  .  toString  (  )  ; 
stmt  .  executeUpdate  (  sql  )  ; 
}  finally  { 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 








public   void   delete  (  String   language  ,  String   tag  ,  int   row  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
String   sql  =  "delete from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and "  +  "      Rank = "  +  row  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
conn  .  setAutoCommit  (  false  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
stmt  .  executeUpdate  (  sql  )  ; 
bumpAllRowsUp  (  stmt  ,  language  ,  tag  ,  row  )  ; 
conn  .  commit  (  )  ; 
}  catch  (  SQLException   e  )  { 
if  (  conn  !=  null  )  conn  .  rollback  (  )  ; 
throw   e  ; 
}  finally  { 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 










public   void   moveRuleUp  (  String   language  ,  String   tag  ,  int   row  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
conn  .  setAutoCommit  (  false  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
int   max  =  findMaxRank  (  stmt  ,  language  ,  tag  )  ; 
if  (  (  row  <  2  )  ||  (  row  >  max  )  )  throw   new   IllegalArgumentException  (  "Row number ("  +  row  +  ") was not between 2 and "  +  max  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = -1 "  +  "where Rank = "  +  row  +  " and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  row  +  "where Rank = "  +  (  row  -  1  )  +  " and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  (  row  -  1  )  +  "where Rank = -1 and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
conn  .  commit  (  )  ; 
}  catch  (  SQLException   e  )  { 
if  (  conn  !=  null  )  conn  .  rollback  (  )  ; 
throw   e  ; 
}  finally  { 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 










public   void   moveRuleDown  (  String   language  ,  String   tag  ,  int   row  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
try  { 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
conn  .  setAutoCommit  (  false  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
int   max  =  findMaxRank  (  stmt  ,  language  ,  tag  )  ; 
if  (  (  row  <  1  )  ||  (  row  >  (  max  -  1  )  )  )  throw   new   IllegalArgumentException  (  "Row number ("  +  row  +  ") was not between 1 and "  +  (  max  -  1  )  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = -1 "  +  "where Rank = "  +  row  +  " and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  row  +  "where Rank = "  +  (  row  +  1  )  +  " and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
stmt  .  executeUpdate  (  "update LanguageMorphologies set Rank = "  +  (  row  +  1  )  +  "where Rank = -1 and MorphologyTag = '"  +  tag  +  "' and "  +  "      LanguageName = '"  +  language  +  "'"  )  ; 
conn  .  commit  (  )  ; 
}  catch  (  SQLException   e  )  { 
if  (  conn  !=  null  )  conn  .  rollback  (  )  ; 
throw   e  ; 
}  finally  { 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

private   boolean   matchPattern  (  String   t1  ,  String   t2  )  { 
boolean   alreadyC  =  false  ; 
char   lastC  =  ' '  ; 
int   sizeT1  =  t1  .  length  (  )  ; 
int   sizeT2  =  t2  .  length  (  )  ; 
int   i  =  0  ; 
int   j  =  0  ; 
for  (  ;  (  (  i  <  sizeT1  )  &&  (  j  <  sizeT2  )  )  ;  ++  i  ,  ++  j  )  { 
if  (  t1  .  charAt  (  i  )  ==  '*'  )  { 
++  i  ; 
if  (  i  ==  sizeT1  )  return   true  ; 
while  (  true  )  { 
if  (  t1  .  charAt  (  i  )  ==  'C'  )  while  (  (  j  <  sizeT2  )  &&  (  CONSONANTS  .  indexOf  (  t2  .  charAt  (  j  )  )  ==  -  1  )  )  ++  j  ;  else   if  (  t1  .  charAt  (  i  )  ==  'V'  )  while  (  (  j  <  sizeT2  )  &&  (  VOWELS  .  indexOf  (  t2  .  charAt  (  j  )  )  ==  -  1  )  )  ++  j  ;  else   while  (  (  j  <  sizeT2  )  &&  (  t2  .  charAt  (  j  )  !=  t1  .  charAt  (  i  )  )  )  ++  j  ; 
if  (  j  ==  sizeT2  )  return   false  ; 
++  j  ; 
if  (  matchPattern  (  t1  .  substring  (  i  +  1  )  ,  t2  .  substring  (  j  )  )  )  return   true  ; 
} 
} 
if  (  t1  .  charAt  (  i  )  ==  'C'  )  { 
if  (  alreadyC  )  { 
if  (  t2  .  charAt  (  j  )  ==  lastC  )  continue  ;  else   return   false  ; 
} 
if  (  CONSONANTS  .  indexOf  (  t2  .  charAt  (  j  )  )  !=  -  1  )  { 
lastC  =  t2  .  charAt  (  j  )  ; 
alreadyC  =  true  ; 
continue  ; 
} 
break  ; 
} 
if  (  t1  .  charAt  (  i  )  ==  'V'  )  { 
if  (  VOWELS  .  indexOf  (  t2  .  charAt  (  j  )  )  !=  -  1  )  continue  ; 
break  ; 
} 
if  (  t1  .  charAt  (  i  )  ==  t2  .  charAt  (  j  )  )  continue  ; 
break  ; 
} 
if  (  (  i  ==  sizeT1  )  &&  (  j  ==  sizeT2  )  )  return   true  ; 
return   false  ; 
} 

private   boolean   matchRest  (  String   r1  ,  String   r2  )  { 
int   r1Size  =  r1  .  length  (  )  ; 
int   r2Size  =  r2  .  length  (  )  ; 
int   i  =  0  ; 
int   j  =  0  ; 
while  (  (  i  <  r1Size  )  &&  (  j  <  r2Size  )  )  { 
char   ch  =  r1  .  charAt  (  i  )  ; 
if  (  ch  ==  'C'  )  { 
if  (  CONSONANTS  .  indexOf  (  r2  .  charAt  (  j  )  )  ==  -  1  )  return   false  ; 
}  else   if  (  ch  ==  'V'  )  { 
if  (  VOWELS  .  indexOf  (  r2  .  charAt  (  j  )  )  ==  -  1  )  return   false  ; 
}  else   if  (  ch  !=  r2  .  charAt  (  j  )  )  return   false  ; 
++  i  ; 
++  j  ; 
} 
if  (  j  ==  r2Size  )  return   true  ; 
return   false  ; 
} 

private   String   makePattern  (  String   str  ,  String   r1  ,  String   r2  )  { 
int   i  =  0  ; 
int   s  =  0  ; 
StringBuffer   nw  =  new   StringBuffer  (  )  ; 
for  (  int   j  =  0  ;  j  <  r2  .  length  (  )  ;  ++  j  )  { 
if  (  r2  .  charAt  (  j  )  ==  '*'  )  { 
while  (  r1  .  charAt  (  i  )  !=  '*'  )  { 
++  i  ; 
++  s  ; 
} 
++  i  ; 
if  (  i  !=  r1  .  length  (  )  )  { 
while  (  true  )  { 
if  (  r1  .  charAt  (  i  )  ==  'C'  )  while  (  (  s  !=  str  .  length  (  )  )  &&  (  CONSONANTS  .  indexOf  (  str  .  charAt  (  s  )  )  ==  -  1  )  )  nw  .  append  (  str  .  charAt  (  s  ++  )  )  ;  else   if  (  r1  .  charAt  (  i  )  ==  'V'  )  while  (  (  s  !=  str  .  length  (  )  )  &&  (  VOWELS  .  indexOf  (  str  .  charAt  (  s  )  )  ==  -  1  )  )  nw  .  append  (  str  .  charAt  (  s  ++  )  )  ;  else   while  (  (  s  !=  str  .  length  (  )  )  &&  (  str  .  charAt  (  s  )  !=  r1  .  charAt  (  i  )  )  )  nw  .  append  (  str  .  charAt  (  s  ++  )  )  ; 
if  (  (  i  ==  r1  .  length  (  )  )  ||  (  s  ==  str  .  length  (  )  )  )  break  ; 
if  (  matchRest  (  r1  .  substring  (  i  +  1  )  ,  str  .  substring  (  s  +  1  )  )  )  break  ; 
nw  .  append  (  str  .  charAt  (  s  ++  )  )  ; 
} 
}  else  { 
while  (  s  !=  str  .  length  (  )  )  nw  .  append  (  str  .  charAt  (  s  ++  )  )  ; 
continue  ; 
} 
}  else   if  (  r2  .  charAt  (  j  )  ==  'C'  )  { 
while  (  r1  .  charAt  (  i  )  !=  'C'  )  { 
++  i  ; 
++  s  ; 
} 
nw  .  append  (  str  .  charAt  (  s  )  )  ; 
}  else   if  (  r2  .  charAt  (  j  )  ==  'V'  )  { 
while  (  r1  .  charAt  (  i  )  !=  'V'  )  { 
++  i  ; 
++  s  ; 
} 
nw  .  append  (  str  .  charAt  (  s  ++  )  )  ; 
++  i  ; 
}  else   nw  .  append  (  r2  .  charAt  (  j  )  )  ; 
} 
return   nw  .  toString  (  )  ; 
} 














public   Collection   list  (  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select LanguageName, MorphologyTag, Rank, Root, Surface, Used "  +  "from LanguageMorphologies order by LanguageName, MorphologyTag, Rank"  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
Vector   rules  =  new   Vector  (  )  ; 
while  (  rs  .  next  (  )  ==  true  )  { 
MorphologyRule   rule  =  new   MorphologyRule  (  rs  .  getString  (  1  )  ,  rs  .  getString  (  2  )  ,  rs  .  getInt  (  3  )  ,  rs  .  getString  (  4  )  ,  rs  .  getString  (  5  )  ,  rs  .  getBoolean  (  6  )  )  ; 
rules  .  add  (  rule  )  ; 
} 
return   rules  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

public   Collection   listLanguages  (  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select distinct LanguageName from LanguageMorphologies "  +  "order by LanguageName"  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
Vector   list  =  new   Vector  (  )  ; 
while  (  rs  .  next  (  )  ==  true  )  list  .  add  (  rs  .  getString  (  1  )  )  ; 
return   list  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

private   Collection   internalGetRules  (  String   language  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select MorphologyTag, Rank, Root, Surface from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and Used = TRUE "  +  "order by Rank"  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
Vector   list  =  new   Vector  (  )  ; 
while  (  rs  .  next  (  )  ==  true  )  { 
MorphologyRule   rule  =  new   MorphologyRule  (  language  ,  rs  .  getString  (  1  )  ,  rs  .  getInt  (  2  )  ,  rs  .  getString  (  3  )  ,  rs  .  getString  (  4  )  ,  true  )  ; 
list  .  add  (  rule  )  ; 
} 
return   list  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

private   boolean   compareMatches  (  Match   match1  ,  Match   match2  )  { 
Collection   tags1  =  match1  .  getMorphologyTags  (  )  ; 
Collection   tags2  =  match2  .  getMorphologyTags  (  )  ; 
return  (  (  match1  .  getRootString  (  )  .  equals  (  match2  .  getRootString  (  )  )  )  &&  (  tags1  .  containsAll  (  tags2  )  )  &&  (  tags2  .  containsAll  (  tags1  )  )  )  ; 
} 

private   Vector   copyDistinct  (  Vector   in  )  { 
Vector   out  =  new   Vector  (  )  ; 
for  (  Iterator   it1  =  in  .  iterator  (  )  ;  it1  .  hasNext  (  )  ;  )  { 
Match   match1  =  (  Match  )  it1  .  next  (  )  ; 
boolean   found  =  false  ; 
for  (  Iterator   it2  =  out  .  iterator  (  )  ;  it2  .  hasNext  (  )  ;  )  { 
Match   match2  =  (  Match  )  it2  .  next  (  )  ; 
if  (  compareMatches  (  match1  ,  match2  )  ==  true  )  { 
found  =  true  ; 
break  ; 
} 
} 
if  (  found  ==  false  )  out  .  add  (  match1  )  ; 
} 
return   out  ; 
} 



























public   Collection   recognize  (  String   language  ,  String   str  )  throws   FidoDatabaseException  { 
Collection   rules  =  internalGetRules  (  language  )  ; 
Vector   matches  =  null  ; 
Vector   current  =  new   Vector  (  )  ; 
current  .  add  (  new   Match  (  str  )  )  ; 
boolean   allUnmodified  =  false  ; 
while  (  allUnmodified  ==  false  )  { 
matches  =  new   Vector  (  )  ; 
for  (  Iterator   it1  =  current  .  iterator  (  )  ;  it1  .  hasNext  (  )  ;  )  { 
Match   on  =  (  Match  )  it1  .  next  (  )  ; 
boolean   matchedRule  =  false  ; 
for  (  Iterator   it2  =  rules  .  iterator  (  )  ;  it2  .  hasNext  (  )  ;  )  { 
MorphologyRule   rule  =  (  MorphologyRule  )  it2  .  next  (  )  ; 
if  (  matchPattern  (  rule  .  getSurface  (  )  ,  on  .  getRootString  (  )  )  ==  true  )  { 
String   modified  =  makePattern  (  on  .  getRootString  (  )  ,  rule  .  getSurface  (  )  ,  rule  .  getRoot  (  )  )  ; 
Match   match  =  new   Match  (  modified  ,  on  .  getMorphologyTags  (  )  ,  rule  .  getTag  (  )  )  ; 
matches  .  add  (  match  )  ; 
matchedRule  =  true  ; 
} 
} 
if  (  matchedRule  ==  false  )  { 
on  .  modified  =  false  ; 
matches  .  add  (  on  )  ; 
} 
} 
current  =  matches  ; 
boolean   anyModified  =  false  ; 
for  (  Iterator   it  =  current  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
Match   match  =  (  Match  )  it  .  next  (  )  ; 
if  (  match  .  modified  ==  true  )  anyModified  =  true  ; 
} 
if  (  anyModified  ==  false  )  allUnmodified  =  true  ; 
} 
matches  =  copyDistinct  (  matches  )  ; 
return   matches  ; 
} 




















public   String   generate  (  String   language  ,  String   tag  ,  String   str  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select Root, Surface from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and "  +  "      MorphologyTag = '"  +  tag  +  "'"  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rs  .  next  (  )  ==  true  )  { 
String   root  =  rs  .  getString  (  1  )  ; 
String   surface  =  rs  .  getString  (  2  )  ; 
if  (  matchPattern  (  root  ,  str  )  ==  true  )  return   makePattern  (  str  ,  root  ,  surface  )  ; 
} 
return   str  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

private   boolean   containsTag  (  Statement   stmt  ,  String   name  )  throws   SQLException  { 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select count(1) from MorphologyTags where TagName = '"  +  name  +  "'"  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
if  (  rs  .  next  (  )  ==  false  )  throw   new   SQLException  (  "No rows returned for count(1) query"  )  ;  else  { 
int   num  =  rs  .  getInt  (  1  )  ; 
if  (  num  ==  1  )  return   true  ;  else   return   false  ; 
} 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
} 
} 






public   Collection   listTags  (  )  throws   FidoDatabaseException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select TagName from MorphologyTags order by TagName"  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
Vector   tags  =  new   Vector  (  )  ; 
while  (  rs  .  next  (  )  ==  true  )  tags  .  add  (  rs  .  getString  (  1  )  )  ; 
return   tags  ; 
}  finally  { 
try  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
}  catch  (  SQLException   e  )  { 
} 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 

public   int   hashCode  (  String   language  ,  String   tag  ,  String   rank  )  throws   FidoDatabaseException  ,  MorphologyNotFoundException  { 
try  { 
Connection   conn  =  null  ; 
Statement   stmt  =  null  ; 
ResultSet   rs  =  null  ; 
try  { 
String   sql  =  "select Root, Surface "  +  "from LanguageMorphologies "  +  "where LanguageName = '"  +  language  +  "' and MorphologyTag = '"  +  tag  +  "' and Rank = "  +  rank  ; 
conn  =  fido  .  util  .  FidoDataSource  .  getConnection  (  )  ; 
stmt  =  conn  .  createStatement  (  )  ; 
rs  =  stmt  .  executeQuery  (  sql  )  ; 
Vector   list  =  new   Vector  (  )  ; 
if  (  rs  .  next  (  )  ==  false  )  throw   new   MorphologyNotFoundException  (  language  +  " "  +  tag  +  " "  +  rank  )  ; 
list  .  add  (  language  )  ; 
list  .  add  (  tag  )  ; 
list  .  add  (  rank  )  ; 
list  .  add  (  rs  .  getString  (  1  )  )  ; 
list  .  add  (  rs  .  getString  (  2  )  )  ; 
return   list  .  hashCode  (  )  ; 
}  finally  { 
if  (  rs  !=  null  )  rs  .  close  (  )  ; 
if  (  stmt  !=  null  )  stmt  .  close  (  )  ; 
if  (  conn  !=  null  )  conn  .  close  (  )  ; 
} 
}  catch  (  SQLException   e  )  { 
throw   new   FidoDatabaseException  (  e  )  ; 
} 
} 
} 

class   Match   extends   MorphologyRecognizeMatch  { 

public   boolean   modified  ; 

public   Match  (  String   root  )  { 
super  (  root  )  ; 
modified  =  true  ; 
} 

public   Match  (  String   root  ,  Collection   previousTags  ,  String   morphologyTag  )  { 
super  (  root  ,  previousTags  ,  morphologyTag  )  ; 
modified  =  true  ; 
} 
} 

