package   com  .  sun  .  kvem  .  midp  .  pim  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   javax  .  microedition  .  pim  .  Contact  ; 
import   javax  .  microedition  .  pim  .  FieldFullException  ; 
import   javax  .  microedition  .  pim  .  PIM  ; 
import   javax  .  microedition  .  pim  .  PIMException  ; 
import   javax  .  microedition  .  pim  .  PIMItem  ; 
import   javax  .  microedition  .  pim  .  PIMList  ; 
import   javax  .  microedition  .  pim  .  UnsupportedFieldException  ; 






public   abstract   class   AbstractPIMItem   implements   PIMItem  { 


private   int  [  ]  fieldKeys  =  new   int  [  0  ]  ; 


private   PIMField  [  ]  fieldValues  =  new   PIMField  [  0  ]  ; 


private   String  [  ]  categories  =  null  ; 


private   boolean   modified  =  true  ; 


private   AbstractPIMList   pimList  ; 





private   final   int   listType  ; 





private   Object   pimListHandle  ; 


private   Object   key  =  null  ; 


private   PIMHandler   pimHandler  ; 






protected   AbstractPIMItem  (  AbstractPIMList   pimList  ,  int   type  )  { 
this  .  pimList  =  pimList  ; 
this  .  listType  =  type  ; 
pimHandler  =  PIMHandler  .  getInstance  (  )  ; 
try  { 
pimListHandle  =  pimList  !=  null  ?  pimList  .  getHandle  (  )  :  pimHandler  .  openList  (  type  ,  null  ,  PIM  .  READ_ONLY  )  ; 
}  catch  (  PIMException   e  )  { 
throw   new   RuntimeException  (  "Error while opening default list"  )  ; 
} 
} 






protected   AbstractPIMItem  (  AbstractPIMList   pimList  ,  PIMItem   baseItem  )  { 
this  (  pimList  ,  pimList  .  getType  (  )  )  ; 
int  [  ]  fields  =  baseItem  .  getFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
int   field  =  fields  [  i  ]  ; 
if  (  !  pimList  .  isSupportedField  (  field  )  )  { 
continue  ; 
} 
int   dataType  =  pimList  .  getFieldDataType  (  field  )  ; 
int   indices  =  baseItem  .  countValues  (  field  )  ; 
for  (  int   index  =  0  ;  index  <  indices  ;  index  ++  )  { 
int   attributes  =  baseItem  .  getAttributes  (  field  ,  index  )  ; 
Object   value  =  null  ; 
switch  (  dataType  )  { 
case   PIMItem  .  BINARY  : 
{ 
value  =  baseItem  .  getBinary  (  field  ,  index  )  ; 
break  ; 
} 
case   PIMItem  .  BOOLEAN  : 
{ 
value  =  new   Boolean  (  baseItem  .  getBoolean  (  field  ,  index  )  )  ; 
break  ; 
} 
case   PIMItem  .  DATE  : 
{ 
value  =  new   Long  (  baseItem  .  getDate  (  field  ,  index  )  )  ; 
break  ; 
} 
case   PIMItem  .  INT  : 
{ 
value  =  new   Integer  (  baseItem  .  getInt  (  field  ,  index  )  )  ; 
break  ; 
} 
case   PIMItem  .  STRING  : 
{ 
value  =  baseItem  .  getString  (  field  ,  index  )  ; 
break  ; 
} 
case   PIMItem  .  STRING_ARRAY  : 
{ 
value  =  baseItem  .  getStringArray  (  field  ,  index  )  ; 
break  ; 
} 
default  : 
{ 
} 
} 
try  { 
addValue  (  field  ,  attributes  ,  value  ,  true  )  ; 
}  catch  (  FieldFullException   ffe  )  { 
}  catch  (  IllegalArgumentException   iae  )  { 
} 
} 
} 
updateRevision  (  )  ; 
} 










PIMField   getField  (  int   field  ,  boolean   create  ,  boolean   check  )  { 
PIMField   f  =  getField  (  field  )  ; 
if  (  f  ==  null  )  { 
if  (  check  &&  !  pimHandler  .  isSupportedField  (  pimListHandle  ,  field  )  )  { 
throw   complaintAboutField  (  listType  ,  field  )  ; 
} 
if  (  create  )  { 
f  =  new   EmptyPIMField  (  )  ; 
putField  (  field  ,  f  )  ; 
} 
} 
return   f  ; 
} 









private   void   setValue  (  int   field  ,  int   index  ,  int   attributes  ,  Object   value  ,  boolean   force  )  { 
try  { 
checkType  (  field  ,  value  )  ; 
PIMField   pimField  =  getField  (  field  ,  false  ,  true  )  ; 
if  (  pimField  ==  null  )  { 
throw   new   IndexOutOfBoundsException  (  "Empty field: "  +  field  )  ; 
} 
int   currentValues  =  pimField  .  getValueCount  (  )  ; 
if  (  index  <  0  ||  index  >=  currentValues  )  { 
throw   new   IndexOutOfBoundsException  (  "0 <= index < "  +  currentValues  +  ", "  +  index  +  " not in range"  )  ; 
} 
if  (  !  force  )  { 
checkReadOnlyFields  (  field  )  ; 
} 
if  (  value   instanceof   Integer  )  { 
checkIntValue  (  field  ,  (  (  Integer  )  value  )  .  intValue  (  )  )  ; 
} 
attributes  =  filterAttributes  (  field  ,  attributes  )  ; 
pimField  .  setValue  (  attributes  ,  value  ,  index  )  ; 
modified  =  true  ; 
}  catch  (  ClassCastException   e  )  { 
throw   new   IllegalArgumentException  (  "Wrong type for field"  )  ; 
} 
} 










private   void   addValue  (  int   field  ,  int   attributes  ,  Object   value  ,  boolean   force  )  { 
checkType  (  field  ,  value  )  ; 
PIMField   pimField  =  getField  (  field  ,  true  ,  true  )  ; 
int   maxValues  =  pimHandler  .  getMaximumValues  (  pimListHandle  ,  field  )  ; 
int   currentValues  =  pimField  .  getValueCount  (  )  ; 
if  (  maxValues  !=  -  1  &&  currentValues  >=  maxValues  )  { 
throw   new   FieldFullException  (  "Can only store "  +  maxValues  +  " in field"  ,  field  )  ; 
} 
if  (  !  force  )  { 
checkReadOnlyFields  (  field  )  ; 
} 
if  (  value   instanceof   Integer  )  { 
checkIntValue  (  field  ,  (  (  Integer  )  value  )  .  intValue  (  )  )  ; 
} 
if  (  pimField  .  isScalar  (  )  )  { 
if  (  currentValues  ==  0  )  { 
pimField  =  new   ScalarPIMField  (  )  ; 
putField  (  field  ,  pimField  )  ; 
}  else  { 
Object   value0  =  pimField  .  getValue  (  0  )  ; 
int   attributes0  =  pimField  .  getAttributes  (  0  )  ; 
pimField  =  new   VectorPIMField  (  )  ; 
pimField  .  addValue  (  attributes0  ,  value0  )  ; 
putField  (  field  ,  pimField  )  ; 
} 
} 
attributes  =  filterAttributes  (  field  ,  attributes  )  ; 
pimField  .  addValue  (  attributes  ,  value  )  ; 
modified  =  true  ; 
} 

private   void   checkIntValue  (  int   field  ,  int   value  )  { 
if  (  (  listType  ==  PIM  .  CONTACT_LIST  &&  field  ==  Contact  .  CLASS  )  ||  (  listType  ==  PIM  .  EVENT_LIST  &&  field  ==  Event  .  CLASS  )  ||  (  listType  ==  PIM  .  TODO_LIST  &&  field  ==  ToDo  .  CLASS  )  )  { 
validateClass  (  value  )  ; 
} 
if  (  listType  ==  PIM  .  TODO_LIST  &&  field  ==  ToDo  .  PRIORITY  )  { 
validatePriority  (  value  )  ; 
} 
} 







private   int   filterAttributes  (  int   field  ,  int   attributes  )  { 
if  (  attributes  ==  0  )  { 
return   0  ; 
}  else  { 
return   attributes  &  pimHandler  .  getSupportedAttributesMask  (  pimListHandle  ,  field  )  ; 
} 
} 







private   Object   getValue  (  int   field  ,  int   index  )  { 
PIMField   pimField  =  getField  (  field  ,  false  ,  true  )  ; 
if  (  pimField  ==  null  )  { 
throw   new   IndexOutOfBoundsException  (  "Empty field: "  +  field  )  ; 
} 
int   currentValues  =  pimField  .  getValueCount  (  )  ; 
if  (  index  <  0  ||  index  >=  currentValues  )  { 
throw   new   IndexOutOfBoundsException  (  "0 <= index < "  +  currentValues  +  ", "  +  index  +  " not in range"  )  ; 
} 
return   pimField  .  getValue  (  index  )  ; 
} 

public   void   addStringArray  (  int   field  ,  int   attributes  ,  String  [  ]  value  )  { 
checkType  (  field  ,  STRING_ARRAY  )  ; 
validateStringArray  (  field  ,  value  )  ; 
addValue  (  field  ,  attributes  ,  value  ,  false  )  ; 
} 

public   void   addBoolean  (  int   field  ,  int   attributes  ,  boolean   value  )  { 
addValue  (  field  ,  attributes  ,  new   Boolean  (  value  )  ,  false  )  ; 
} 

public   void   removeFromCategory  (  String   category  )  { 
if  (  category  ==  null  )  { 
throw   new   NullPointerException  (  "Null category"  )  ; 
} 
if  (  categories  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  category  .  equals  (  categories  [  i  ]  )  )  { 
if  (  categories  .  length  ==  1  )  { 
this  .  categories  =  null  ; 
}  else  { 
String  [  ]  a  =  new   String  [  categories  .  length  -  1  ]  ; 
System  .  arraycopy  (  categories  ,  0  ,  a  ,  0  ,  i  )  ; 
System  .  arraycopy  (  categories  ,  i  +  1  ,  a  ,  i  ,  a  .  length  -  i  )  ; 
this  .  categories  =  a  ; 
} 
this  .  modified  =  true  ; 
return  ; 
} 
} 
} 
} 

public   int  [  ]  getFields  (  )  { 
int   emptyFields  =  0  ; 
for  (  int   i  =  0  ;  i  <  fieldValues  .  length  ;  i  ++  )  { 
if  (  fieldValues  [  i  ]  .  getValueCount  (  )  ==  0  )  { 
emptyFields  ++  ; 
} 
} 
int  [  ]  keys  =  new   int  [  fieldKeys  .  length  -  emptyFields  ]  ; 
for  (  int   i  =  0  ,  j  =  0  ;  i  <  keys  .  length  ;  i  ++  )  { 
if  (  emptyFields  ==  0  ||  fieldValues  [  i  ]  .  getValueCount  (  )  !=  0  )  { 
keys  [  j  ++  ]  =  fieldKeys  [  i  ]  ; 
}  else  { 
emptyFields  --  ; 
} 
} 
return   keys  ; 
} 

public   boolean   getBoolean  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  BOOLEAN  )  ; 
return  (  (  Boolean  )  getValue  (  field  ,  index  )  )  .  booleanValue  (  )  ; 
} 

public   void   addDate  (  int   field  ,  int   attributes  ,  long   value  )  { 
addValue  (  field  ,  attributes  ,  new   Long  (  value  )  ,  false  )  ; 
} 

public   int   maxCategories  (  )  { 
return  -  1  ; 
} 

public   void   setDate  (  int   field  ,  int   index  ,  int   attributes  ,  long   value  )  { 
setValue  (  field  ,  index  ,  attributes  ,  new   Long  (  value  )  ,  false  )  ; 
} 

public   int   getInt  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  INT  )  ; 
try  { 
return  (  (  Integer  )  getValue  (  field  ,  index  )  )  .  intValue  (  )  ; 
}  catch  (  ClassCastException   e  )  { 
String   message  =  "Cannot convert to integer on field "  +  field  +  ": "  +  getValue  (  field  ,  index  )  .  getClass  (  )  ; 
throw   new   ClassCastException  (  message  )  ; 
} 
} 

public   void   setBinary  (  int   field  ,  int   index  ,  int   attributes  ,  byte  [  ]  value  ,  int   offset  ,  int   length  )  { 
validateBinaryValue  (  value  ,  offset  ,  length  )  ; 
length  =  Math  .  min  (  length  ,  value  .  length  -  offset  )  ; 
byte  [  ]  b  =  new   byte  [  length  ]  ; 
System  .  arraycopy  (  value  ,  offset  ,  b  ,  0  ,  length  )  ; 
setValue  (  field  ,  index  ,  attributes  ,  b  ,  false  )  ; 
} 

public   int   getAttributes  (  int   field  ,  int   index  )  { 
return   getField  (  field  ,  true  ,  true  )  .  getAttributes  (  index  )  ; 
} 

public   int   countValues  (  int   field  )  { 
PIMField   pimField  =  getField  (  field  ,  false  ,  true  )  ; 
return   pimField  ==  null  ?  0  :  pimField  .  getValueCount  (  )  ; 
} 

public   void   addString  (  int   field  ,  int   attributes  ,  String   value  )  { 
validateString  (  value  )  ; 
addValue  (  field  ,  attributes  ,  value  ,  false  )  ; 
} 

public   String  [  ]  getCategories  (  )  { 
if  (  categories  ==  null  )  { 
return   new   String  [  0  ]  ; 
} 
String  [  ]  cs  =  new   String  [  categories  .  length  ]  ; 
System  .  arraycopy  (  categories  ,  0  ,  cs  ,  0  ,  categories  .  length  )  ; 
return   cs  ; 
} 

String  [  ]  getCategoriesRaw  (  )  { 
return   categories  ; 
} 

public   void   setInt  (  int   field  ,  int   index  ,  int   attributes  ,  int   value  )  { 
setValue  (  field  ,  index  ,  attributes  ,  new   Integer  (  value  )  ,  false  )  ; 
} 

public   void   setStringArray  (  int   field  ,  int   index  ,  int   attributes  ,  String  [  ]  value  )  { 
checkType  (  field  ,  STRING_ARRAY  )  ; 
validateStringArray  (  field  ,  value  )  ; 
setValue  (  field  ,  index  ,  attributes  ,  value  ,  false  )  ; 
} 











private   void   validateStringArray  (  int   field  ,  String  [  ]  a  )  { 
int   requiredLength  =  pimHandler  .  getStringArraySize  (  pimListHandle  ,  field  )  ; 
if  (  a  .  length  !=  requiredLength  )  { 
throw   new   IllegalArgumentException  (  "String array length incorrect: should be "  +  requiredLength  )  ; 
} 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
if  (  a  [  i  ]  !=  null  )  { 
return  ; 
} 
} 
throw   new   IllegalArgumentException  (  "No non-null elements in array"  )  ; 
} 





private   void   validateString  (  String   value  )  { 
if  (  value  ==  null  )  { 
throw   new   NullPointerException  (  "String field value should not be null"  )  ; 
} 
} 

public   long   getDate  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  DATE  )  ; 
try  { 
return  (  (  Long  )  getValue  (  field  ,  index  )  )  .  longValue  (  )  ; 
}  catch  (  ClassCastException   e  )  { 
throw   e  ; 
} 
} 

public   void   addToCategory  (  String   category  )  throws   PIMException  { 
if  (  category  ==  null  )  { 
throw   new   NullPointerException  (  "Null category"  )  ; 
} 
if  (  categories  ==  null  )  { 
this  .  categories  =  new   String  [  ]  {  category  }  ; 
this  .  modified  =  true  ; 
}  else  { 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  categories  [  i  ]  .  equals  (  category  )  )  { 
return  ; 
} 
} 
String  [  ]  a  =  new   String  [  categories  .  length  +  1  ]  ; 
System  .  arraycopy  (  categories  ,  0  ,  a  ,  0  ,  categories  .  length  )  ; 
a  [  categories  .  length  ]  =  category  ; 
this  .  categories  =  a  ; 
this  .  modified  =  true  ; 
} 
} 

public   void   addInt  (  int   field  ,  int   attributes  ,  int   value  )  { 
addValue  (  field  ,  attributes  ,  new   Integer  (  value  )  ,  false  )  ; 
} 

public   byte  [  ]  getBinary  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  BINARY  )  ; 
return  (  byte  [  ]  )  getValue  (  field  ,  index  )  ; 
} 

public   void   addBinary  (  int   field  ,  int   attributes  ,  byte  [  ]  value  ,  int   offset  ,  int   length  )  { 
validateBinaryValue  (  value  ,  offset  ,  length  )  ; 
length  =  Math  .  min  (  length  ,  value  .  length  -  offset  )  ; 
byte  [  ]  b  =  new   byte  [  length  ]  ; 
System  .  arraycopy  (  value  ,  offset  ,  b  ,  0  ,  length  )  ; 
addValue  (  field  ,  attributes  ,  b  ,  false  )  ; 
} 










private   void   validateBinaryValue  (  byte  [  ]  value  ,  int   offset  ,  int   length  )  { 
if  (  value  ==  null  )  { 
throw   new   NullPointerException  (  "Binary field value"  +  " should not be null"  )  ; 
} 
if  (  offset  <  0  )  { 
throw   new   IllegalArgumentException  (  "Negative offset"  )  ; 
} 
if  (  offset  +  length  >  value  .  length  )  { 
throw   new   IllegalArgumentException  (  "Offset out of range"  )  ; 
} 
if  (  length  <=  0  )  { 
throw   new   IllegalArgumentException  (  "Length must be at least 1"  )  ; 
} 
if  (  value  .  length  ==  0  )  { 
throw   new   IllegalArgumentException  (  "Binary array value "  +  "has zero length"  )  ; 
} 
} 

public   String  [  ]  getStringArray  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  STRING_ARRAY  )  ; 
return  (  String  [  ]  )  getValue  (  field  ,  index  )  ; 
} 

public   void   setBoolean  (  int   field  ,  int   index  ,  int   attributes  ,  boolean   value  )  { 
setValue  (  field  ,  index  ,  attributes  ,  new   Boolean  (  value  )  ,  false  )  ; 
} 

public   PIMList   getPIMList  (  )  { 
return   pimList  ; 
} 








public   Object   getPIMListHandle  (  )  { 
return   pimListHandle  ; 
} 





void   setPIMList  (  AbstractPIMList   list  )  { 
this  .  pimList  =  list  ; 
pimListHandle  =  list  .  getHandle  (  )  ; 
} 

public   void   removeValue  (  int   field  ,  int   index  )  { 
PIMField   pimField  =  getField  (  field  ,  false  ,  true  )  ; 
if  (  pimField  ==  null  )  { 
throw   new   IndexOutOfBoundsException  (  "Empty field: "  +  field  )  ; 
} 
int   currentValues  =  pimField  .  getValueCount  (  )  ; 
if  (  index  <  0  ||  index  >=  currentValues  )  { 
throw   new   IndexOutOfBoundsException  (  "0 <= index < "  +  currentValues  +  ", "  +  index  +  " not in range"  )  ; 
} 
checkReadOnlyFields  (  field  )  ; 
pimField  .  removeValue  (  index  )  ; 
currentValues  --  ; 
if  (  currentValues  ==  0  )  { 
removeField  (  field  )  ; 
}  else   if  (  currentValues  ==  1  )  { 
Object   value  =  pimField  .  getValue  (  0  )  ; 
int   attributes  =  pimField  .  getAttributes  (  0  )  ; 
pimField  =  new   ScalarPIMField  (  )  ; 
pimField  .  addValue  (  attributes  ,  value  )  ; 
putField  (  field  ,  pimField  )  ; 
} 
modified  =  true  ; 
} 

public   String   getString  (  int   field  ,  int   index  )  { 
checkType  (  field  ,  STRING  )  ; 
return  (  String  )  getValue  (  field  ,  index  )  ; 
} 

public   void   setString  (  int   field  ,  int   index  ,  int   attributes  ,  String   value  )  { 
validateString  (  value  )  ; 
setValue  (  field  ,  index  ,  attributes  ,  value  ,  false  )  ; 
} 

public   boolean   isModified  (  )  { 
return   modified  ; 
} 





void   setModified  (  boolean   modified  )  { 
this  .  modified  =  modified  ; 
} 

public   void   commit  (  )  throws   PIMException  { 
if  (  pimList  ==  null  )  { 
throw   new   PIMException  (  "Item is not in a list"  )  ; 
} 
pimList  .  checkWritePermission  (  )  ; 
pimList  .  checkOpen  (  )  ; 
updateRevision  (  )  ; 
setDefaultValues  (  )  ; 
try  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
PIMFormat   format  =  getEncodingFormat  (  )  ; 
format  .  encode  (  baos  ,  "UTF-8"  ,  this  )  ; 
Object   newKey  =  pimList  .  commit  (  key  ,  baos  .  toByteArray  (  )  ,  categories  )  ; 
if  (  key  ==  null  )  { 
pimList  .  addItem  (  this  )  ; 
} 
setKey  (  newKey  )  ; 
updateUID  (  )  ; 
modified  =  false  ; 
}  catch  (  IOException   e  )  { 
throw   new   PIMException  (  "Error persisting PIMItem"  )  ; 
} 
} 






abstract   PIMFormat   getEncodingFormat  (  )  ; 






boolean   isInCategory  (  String   category  )  { 
if  (  categories  ==  null  )  { 
return   false  ; 
}  else  { 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  categories  [  i  ]  .  equals  (  category  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 
} 





void   setKey  (  Object   key  )  { 
this  .  key  =  key  ; 
if  (  key  !=  null  )  { 
updateUID  (  )  ; 
} 
} 





Object   getKey  (  )  { 
return   key  ; 
} 




void   remove  (  )  throws   PIMException  { 
if  (  pimList  ==  null  )  { 
throw   new   PIMException  (  "Item is not in a list"  )  ; 
} 
pimList  .  checkWritePermission  (  )  ; 
pimList  .  commit  (  key  ,  null  ,  null  )  ; 
setKey  (  null  )  ; 
pimList  =  null  ; 
} 




protected   void   setDefaultValues  (  )  { 
int  [  ]  supportedFields  =  pimList  .  getSupportedFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  supportedFields  .  length  ;  i  ++  )  { 
int   field  =  supportedFields  [  i  ]  ; 
PIMField   pimField  =  getField  (  field  ,  false  ,  true  )  ; 
if  (  (  pimField  ==  null  ||  pimField  .  getValueCount  (  )  ==  0  )  &&  pimHandler  .  hasDefaultValue  (  pimListHandle  ,  field  )  )  { 
Object   value  =  null  ; 
switch  (  pimList  .  getFieldDataType  (  field  )  )  { 
case   PIMItem  .  BOOLEAN  : 
value  =  new   Boolean  (  pimHandler  .  getDefaultBooleanValue  (  pimListHandle  ,  field  )  )  ; 
break  ; 
case   PIMItem  .  BINARY  : 
value  =  pimHandler  .  getDefaultBinaryValue  (  pimListHandle  ,  field  )  ; 
break  ; 
case   PIMItem  .  DATE  : 
value  =  new   Long  (  pimHandler  .  getDefaultDateValue  (  pimListHandle  ,  field  )  )  ; 
break  ; 
case   PIMItem  .  INT  : 
value  =  new   Integer  (  pimHandler  .  getDefaultIntValue  (  pimListHandle  ,  field  )  )  ; 
break  ; 
case   PIMItem  .  STRING  : 
value  =  pimHandler  .  getDefaultStringValue  (  pimListHandle  ,  field  )  ; 
break  ; 
case   PIMItem  .  STRING_ARRAY  : 
value  =  pimHandler  .  getDefaultStringArrayValue  (  pimListHandle  ,  field  )  ; 
break  ; 
default  : 
continue  ; 
} 
addValue  (  field  ,  PIMItem  .  ATTR_NONE  ,  value  ,  false  )  ; 
} 
} 
} 







static   boolean   isValidPIMField  (  int   type  ,  int   field  )  { 
switch  (  type  )  { 
case   PIM  .  CONTACT_LIST  : 
return   ContactImpl  .  isValidPIMField  (  field  )  ; 
case   PIM  .  EVENT_LIST  : 
return   EventImpl  .  isValidPIMField  (  field  )  ; 
case   PIM  .  TODO_LIST  : 
return   ToDoImpl  .  isValidPIMField  (  field  )  ; 
default  : 
return   false  ; 
} 
} 








private   void   checkType  (  int   field  ,  Object   value  )  { 
try  { 
int   dataType  =  pimHandler  .  getFieldDataType  (  pimListHandle  ,  field  )  ; 
switch  (  dataType  )  { 
case   PIMItem  .  BINARY  : 
{ 
byte  [  ]  b  =  (  byte  [  ]  )  value  ; 
break  ; 
} 
case   PIMItem  .  BOOLEAN  : 
{ 
Boolean   b  =  (  Boolean  )  value  ; 
break  ; 
} 
case   PIMItem  .  DATE  : 
{ 
Long   l  =  (  Long  )  value  ; 
break  ; 
} 
case   PIMItem  .  INT  : 
{ 
Integer   i  =  (  Integer  )  value  ; 
break  ; 
} 
case   PIMItem  .  STRING  : 
{ 
String   s  =  (  String  )  value  ; 
break  ; 
} 
case   PIMItem  .  STRING_ARRAY  : 
{ 
String  [  ]  s  =  (  String  [  ]  )  value  ; 
break  ; 
} 
default  : 
throw   complaintAboutField  (  listType  ,  field  )  ; 
} 
}  catch  (  ClassCastException   cce  )  { 
throw   new   IllegalArgumentException  (  cce  .  getMessage  (  )  )  ; 
} 
} 







private   void   checkType  (  int   field  ,  int   dataType  )  { 
int   correctDataType  =  pimHandler  .  getFieldDataType  (  pimListHandle  ,  field  )  ; 
if  (  dataType  !=  correctDataType  &&  correctDataType  !=  -  1  )  { 
throw   new   IllegalArgumentException  (  "Wrong data type"  )  ; 
} 
if  (  correctDataType  ==  -  1  )  { 
throw   complaintAboutField  (  listType  ,  field  )  ; 
} 
} 








static   RuntimeException   complaintAboutField  (  int   type  ,  int   field  )  { 
if  (  isValidPIMField  (  type  ,  field  )  )  { 
return   new   UnsupportedFieldException  (  String  .  valueOf  (  field  )  )  ; 
}  else  { 
return   new   IllegalArgumentException  (  "Invalid field "  +  field  )  ; 
} 
} 









private   int   findFieldKey  (  int   key  )  { 
int   lowerBound  =  0  ; 
int   upperBound  =  fieldKeys  .  length  ; 
while  (  lowerBound  !=  upperBound  )  { 
int   index  =  lowerBound  +  (  upperBound  -  lowerBound  )  /  2  ; 
int   indexKey  =  fieldKeys  [  index  ]  ; 
if  (  indexKey  >  key  )  { 
if  (  index  ==  upperBound  )  { 
upperBound  --  ; 
}  else  { 
upperBound  =  index  ; 
} 
}  else   if  (  indexKey  ==  key  )  { 
return   index  ; 
}  else  { 
if  (  index  ==  lowerBound  )  { 
lowerBound  ++  ; 
}  else  { 
lowerBound  =  index  ; 
} 
} 
} 
return  ~  lowerBound  ; 
} 






public   void   putField  (  int   key  ,  PIMField   field  )  { 
int   index  =  findFieldKey  (  key  )  ; 
if  (  index  >=  0  )  { 
fieldValues  [  index  ]  =  field  ; 
}  else  { 
index  =  ~  index  ; 
int  [  ]  newKeys  =  new   int  [  fieldKeys  .  length  +  1  ]  ; 
PIMField  [  ]  newFields  =  new   PIMField  [  fieldValues  .  length  +  1  ]  ; 
System  .  arraycopy  (  fieldKeys  ,  0  ,  newKeys  ,  0  ,  index  )  ; 
System  .  arraycopy  (  fieldValues  ,  0  ,  newFields  ,  0  ,  index  )  ; 
newKeys  [  index  ]  =  key  ; 
newFields  [  index  ]  =  field  ; 
System  .  arraycopy  (  fieldKeys  ,  index  ,  newKeys  ,  index  +  1  ,  fieldKeys  .  length  -  index  )  ; 
System  .  arraycopy  (  fieldValues  ,  index  ,  newFields  ,  index  +  1  ,  fieldKeys  .  length  -  index  )  ; 
this  .  fieldKeys  =  newKeys  ; 
this  .  fieldValues  =  newFields  ; 
} 
} 






public   PIMField   getField  (  int   key  )  { 
int   index  =  findFieldKey  (  key  )  ; 
if  (  index  >=  0  )  { 
return   fieldValues  [  index  ]  ; 
}  else  { 
return   null  ; 
} 
} 





public   void   removeField  (  int   key  )  { 
int   index  =  findFieldKey  (  key  )  ; 
if  (  index  >=  0  )  { 
int  [  ]  newKeys  =  new   int  [  fieldKeys  .  length  -  1  ]  ; 
PIMField  [  ]  newFields  =  new   PIMField  [  fieldValues  .  length  -  1  ]  ; 
System  .  arraycopy  (  fieldKeys  ,  0  ,  newKeys  ,  0  ,  index  )  ; 
System  .  arraycopy  (  fieldValues  ,  0  ,  newFields  ,  0  ,  index  )  ; 
System  .  arraycopy  (  fieldKeys  ,  index  +  1  ,  newKeys  ,  index  ,  newKeys  .  length  -  index  )  ; 
System  .  arraycopy  (  fieldValues  ,  index  +  1  ,  newFields  ,  index  ,  newKeys  .  length  -  index  )  ; 
this  .  fieldKeys  =  newKeys  ; 
this  .  fieldValues  =  newFields  ; 
} 
} 






private   void   checkReadOnlyFields  (  int   field  )  { 
if  (  key  !=  null  )  { 
if  (  field  ==  getRevisionField  (  )  )  { 
throw   new   IllegalArgumentException  (  "REVISION field is read only"  +  " except on newly created PIMItems"  )  ; 
}  else   if  (  field  ==  getUIDField  (  )  )  { 
throw   new   IllegalArgumentException  (  "UID field is read only except on newly created PIMItems"  )  ; 
} 
} 
} 




private   void   updateRevision  (  )  { 
Long   value  =  new   Long  (  System  .  currentTimeMillis  (  )  )  ; 
int   field  =  getRevisionField  (  )  ; 
if  (  countValues  (  field  )  ==  0  )  { 
addValue  (  field  ,  0  ,  value  ,  true  )  ; 
}  else  { 
setValue  (  field  ,  0  ,  0  ,  value  ,  true  )  ; 
} 
} 




private   void   updateUID  (  )  { 
String   value  =  key  .  toString  (  )  ; 
int   field  =  getUIDField  (  )  ; 
if  (  countValues  (  field  )  ==  0  )  { 
addValue  (  field  ,  0  ,  value  ,  true  )  ; 
} 
} 









protected   abstract   int   getRevisionField  (  )  ; 








protected   abstract   int   getUIDField  (  )  ; 





protected   String   formatData  (  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  fieldValues  .  length  ;  i  ++  )  { 
if  (  fieldValues  [  i  ]  .  getValueCount  (  )  !=  0  )  { 
PIMField   pimField  =  fieldValues  [  i  ]  ; 
int   field  =  fieldKeys  [  i  ]  ; 
int   valueCount  =  pimField  .  getValueCount  (  )  ; 
if  (  valueCount  ==  0  )  { 
continue  ; 
} 
if  (  i  !=  0  )  { 
sb  .  append  (  ", "  )  ; 
} 
String   label  =  pimHandler  .  getFieldLabel  (  pimListHandle  ,  field  )  ; 
int   dataType  =  pimHandler  .  getFieldDataType  (  pimListHandle  ,  field  )  ; 
for  (  int   j  =  0  ;  j  <  valueCount  ;  j  ++  )  { 
sb  .  append  (  label  )  ; 
if  (  valueCount  !=  1  )  { 
sb  .  append  (  "["  )  ; 
sb  .  append  (  j  )  ; 
sb  .  append  (  "]"  )  ; 
} 
sb  .  append  (  "="  )  ; 
Object   value  =  pimField  .  getValue  (  j  )  ; 
if  (  value  ==  null  )  { 
sb  .  append  (  "null"  )  ; 
continue  ; 
} 
switch  (  dataType  )  { 
case   STRING_ARRAY  : 
{ 
String  [  ]  aValue  =  (  String  [  ]  )  value  ; 
sb  .  append  (  "["  )  ; 
for  (  int   k  =  0  ;  k  <  aValue  .  length  ;  k  ++  )  { 
if  (  k  !=  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  aValue  [  k  ]  )  ; 
} 
sb  .  append  (  "]"  )  ; 
break  ; 
} 
case   BINARY  : 
{ 
byte  [  ]  bValue  =  (  byte  [  ]  )  value  ; 
sb  .  append  (  "<"  +  bValue  .  length  +  " bytes>"  )  ; 
break  ; 
} 
case   DATE  : 
{ 
long   dValue  =  (  (  Long  )  value  )  .  longValue  (  )  ; 
sb  .  append  (  pimHandler  .  composeDateTime  (  dValue  )  )  ; 
break  ; 
} 
default  : 
sb  .  append  (  value  )  ; 
} 
} 
} 
} 
if  (  categories  !=  null  &&  categories  .  length  !=  0  )  { 
if  (  sb  .  length  (  )  >  0  )  { 
sb  .  append  (  ", "  )  ; 
} 
sb  .  append  (  "Categories=["  )  ; 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  i  >  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  categories  [  i  ]  )  ; 
} 
sb  .  append  (  "]"  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 





protected   abstract   String   toDisplayableString  (  )  ; 





public   String   toString  (  )  { 
return  "true"  .  equals  (  System  .  getProperty  (  "pim.debug"  )  )  ?  toDisplayableString  (  )  :  super  .  toString  (  )  ; 
} 






private   void   validateClass  (  int   value  )  { 
switch  (  value  )  { 
case   javax  .  microedition  .  pim  .  Contact  .  CLASS_CONFIDENTIAL  : 
case   Contact  .  CLASS_PRIVATE  : 
case   Contact  .  CLASS_PUBLIC  : 
return  ; 
default  : 
throw   new   IllegalArgumentException  (  "Invalid CLASS value: "  +  value  )  ; 
} 
} 






private   void   validatePriority  (  int   value  )  { 
if  (  value  <  0  ||  value  >  9  )  { 
throw   new   IllegalArgumentException  (  "Invalid PRIORITY value: "  +  value  )  ; 
} 
} 
} 

