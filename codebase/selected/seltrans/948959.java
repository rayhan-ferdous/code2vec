package   org  .  eugenes  .  lusearch  .  fileIO  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  Arrays  ; 
import   org  .  eugenes  .  lusearch  .  settings  .  *  ; 
import   org  .  eugenes  .  lusearch  .  sort  .  *  ; 
import   org  .  eugenes  .  lusearch  .  util  .  ArgosUtils  ; 

























public   class   ArgosIndexFile   extends   ArgosFile  { 





public   ArgosIndexFile  (  String   filename  )  throws   IOException  { 
super  (  filename  )  ; 
} 







public   ArgosIndexFile  (  String   prefix  ,  String   suffix  ,  File   directory  )  throws   IOException  { 
super  (  prefix  ,  suffix  ,  directory  )  ; 
} 












public   ArgosFieldData  [  ]  sort  (  String   field  ,  boolean   ascending  )  throws   IOException  { 
ArgosFieldData  [  ]  fieldData  =  getFieldData  (  field  )  ; 
Arrays  .  sort  (  fieldData  ,  new   ArgosCompareFieldData  (  ascending  )  )  ; 
return   fieldData  ; 
} 

private   ArgosFieldData  [  ]  getFieldData  (  String   field  )  throws   IOException  { 
String   fileData  =  this  .  getFileData  (  )  ; 
String  [  ]  lines  =  fileData  .  split  (  ArgosDefaults  .  NEWLINE  )  ; 
ArgosFieldData  [  ]  fd  =  new   ArgosFieldData  [  lines  .  length  ]  ; 
field  =  field  .  trim  (  )  ; 
int   fieldCol  =  getFieldColumn  (  field  ,  lines  [  0  ]  )  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  i  ++  )  { 
String  [  ]  tokens  =  this  .  getFieldsFromLine  (  lines  [  i  ]  )  ; 
fd  [  i  ]  =  new   ArgosFieldData  (  field  ,  this  .  getData  (  tokens  [  fieldCol  ]  )  ,  Integer  .  parseInt  (  tokens  [  tokens  .  length  -  1  ]  )  )  ; 
} 
return   fd  ; 
} 

private   String   getData  (  String   fieldData  )  { 
String   value  =  fieldData  .  substring  (  fieldData  .  indexOf  (  ArgosDefaults  .  FIELD_DELIM  )  +  1  )  ; 
value  =  ArgosUtils  .  controlUnescape  (  value  ,  ArgosDefaults  .  FIELD_DELIM  )  ; 
return   value  ; 
} 

private   int   getFieldColumn  (  String   field  ,  String   line  )  throws   IOException  { 
String  [  ]  fields  =  this  .  getFieldsFromLine  (  line  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  if  (  fields  [  i  ]  .  startsWith  (  field  )  )  return   i  ; 
throw   new   IOException  (  "Requested field "  +  field  +  " is not present in index field."  )  ; 
} 

private   String  [  ]  getFieldsFromLine  (  String   line  )  { 
return   line  .  split  (  "["  +  ArgosDefaults  .  DATA_SEPARATOR  +  "]"  )  ; 
} 

private   String   getFileData  (  )  throws   IOException  { 
super  .  openFile  (  "r"  )  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
while  (  in  .  available  (  )  !=  0  )  bos  .  write  (  in  .  readByte  (  )  )  ; 
super  .  closeFile  (  )  ; 
return   bos  .  toString  (  )  ; 
} 
} 

