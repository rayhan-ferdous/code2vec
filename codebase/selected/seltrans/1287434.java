package   net  .  wotonomy  .  util  ; 

import   net  .  wotonomy  .  foundation  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 












public   class   Duplicator  { 







public   static   final   Object   NULL  =  NSNull  .  nullValue  (  )  ; 

private   static   NSSelector   clone  =  new   NSSelector  (  "clone"  )  ; 







public   static   Map   readPropertiesForObject  (  Object   anObject  )  { 
List   readProperties  =  new   ArrayList  (  )  ; 
String  [  ]  read  =  Introspector  .  getReadPropertiesForObject  (  anObject  )  ; 
for  (  int   i  =  0  ;  i  <  read  .  length  ;  i  ++  )  { 
readProperties  .  add  (  read  [  i  ]  )  ; 
} 
List   properties  =  new   ArrayList  (  )  ; 
String  [  ]  write  =  Introspector  .  getWritePropertiesForObject  (  anObject  )  ; 
for  (  int   i  =  0  ;  i  <  write  .  length  ;  i  ++  )  { 
properties  .  add  (  write  [  i  ]  )  ; 
} 
properties  .  retainAll  (  readProperties  )  ; 
NSMutableDictionary   result  =  new   NSMutableDictionary  (  )  ; 
String   key  ; 
Object   value  ; 
Iterator   it  =  properties  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
key  =  it  .  next  (  )  .  toString  (  )  ; 
value  =  Introspector  .  get  (  anObject  ,  key  )  ; 
if  (  value  ==  null  )  value  =  NULL  ; 
result  .  setObjectForKey  (  value  ,  key  )  ; 
} 
return   result  ; 
} 





public   static   Map   clonePropertiesForObject  (  Object   anObject  )  { 
Object   key  ; 
Map   result  =  readPropertiesForObject  (  anObject  )  ; 
Iterator   it  =  result  .  keySet  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
key  =  it  .  next  (  )  ; 
result  .  put  (  key  ,  deepClone  (  result  .  get  (  key  )  )  )  ; 
} 
return   result  ; 
} 






public   static   void   writePropertiesForObject  (  Map   aMap  ,  Object   anObject  )  { 
String   key  ; 
Object   value  ; 
Iterator   it  =  aMap  .  keySet  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
key  =  it  .  next  (  )  .  toString  (  )  ; 
value  =  aMap  .  get  (  key  )  ; 
if  (  NULL  .  equals  (  value  )  )  value  =  null  ; 
Introspector  .  set  (  anObject  ,  key  ,  value  )  ; 
} 
} 








public   static   Object   clone  (  Object   aSource  )  { 
Object   result  =  null  ; 
if  (  clone  .  implementedByObject  (  aSource  )  )  { 
try  { 
result  =  clone  .  invoke  (  aSource  )  ; 
return   result  ; 
}  catch  (  Exception   exc  )  { 
} 
} 
Class   c  =  aSource  .  getClass  (  )  ; 
try  { 
result  =  c  .  newInstance  (  )  ; 
}  catch  (  Exception   exc  )  { 
throw   new   WotonomyException  (  exc  )  ; 
} 
return   copy  (  aSource  ,  result  )  ; 
} 







public   static   Object   deepClone  (  Object   aSource  )  { 
try  { 
ByteArrayOutputStream   byteOutput  =  new   ByteArrayOutputStream  (  )  ; 
ObjectOutputStream   objectOutput  =  new   ObjectOutputStream  (  byteOutput  )  ; 
objectOutput  .  writeObject  (  aSource  )  ; 
objectOutput  .  flush  (  )  ; 
objectOutput  .  close  (  )  ; 
ByteArrayInputStream   byteInput  =  new   ByteArrayInputStream  (  byteOutput  .  toByteArray  (  )  )  ; 
ObjectInputStream   objectInput  =  new   ObjectInputStream  (  byteInput  )  ; 
return   objectInput  .  readObject  (  )  ; 
}  catch  (  Exception   exc  )  { 
throw   new   WotonomyException  (  exc  )  ; 
} 
} 






public   static   Object   copy  (  Object   aSource  ,  Object   aDestination  )  { 
try  { 
writePropertiesForObject  (  readPropertiesForObject  (  aSource  )  ,  aDestination  )  ; 
}  catch  (  RuntimeException   exc  )  { 
throw   new   WotonomyException  (  exc  )  ; 
} 
return   aDestination  ; 
} 







public   static   Object   deepCopy  (  Object   aSource  ,  Object   aDestination  )  { 
try  { 
writePropertiesForObject  (  clonePropertiesForObject  (  aSource  )  ,  aDestination  )  ; 
}  catch  (  RuntimeException   exc  )  { 
throw   new   WotonomyException  (  exc  )  ; 
} 
return   aDestination  ; 
} 
} 

