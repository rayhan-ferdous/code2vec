import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  lang  .  reflect  .  Field  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  lang  .  reflect  .  Member  ; 
import   java  .  util  .  Vector  ; 







public   class   RJavaTools  { 








public   static   Class   getClass  (  Class   cl  ,  String   name  ,  boolean   staticRequired  )  { 
Class  [  ]  clazzes  =  cl  .  getClasses  (  )  ; 
for  (  int   i  =  0  ;  i  <  clazzes  .  length  ;  i  ++  )  { 
if  (  getSimpleName  (  clazzes  [  i  ]  .  getName  (  )  )  .  equals  (  name  )  &&  (  !  staticRequired  ||  isStatic  (  clazzes  [  i  ]  )  )  )  { 
return   clazzes  [  i  ]  ; 
} 
} 
return   null  ; 
} 







public   static   Class  [  ]  getStaticClasses  (  Class   cl  )  { 
Class  [  ]  clazzes  =  cl  .  getClasses  (  )  ; 
if  (  clazzes  ==  null  )  return   null  ; 
Vector   vec  =  new   Vector  (  )  ; 
int   n  =  clazzes  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
Class   clazz  =  clazzes  [  i  ]  ; 
if  (  isStatic  (  clazz  )  )  { 
vec  .  add  (  clazz  )  ; 
} 
} 
if  (  vec  .  size  (  )  ==  0  )  { 
return   null  ; 
} 
Class  [  ]  out  =  new   Class  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  out  )  ; 
return   out  ; 
} 







public   static   boolean   isStatic  (  Class   clazz  )  { 
return  (  clazz  .  getModifiers  (  )  &  Modifier  .  STATIC  )  !=  0  ; 
} 







public   static   Field  [  ]  getStaticFields  (  Class   cl  )  { 
Field  [  ]  members  =  cl  .  getFields  (  )  ; 
if  (  members  .  length  ==  0  )  { 
return   null  ; 
} 
Vector   vec  =  new   Vector  (  )  ; 
int   n  =  members  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
Field   memb  =  members  [  i  ]  ; 
if  (  isStatic  (  memb  )  )  { 
vec  .  add  (  memb  )  ; 
} 
} 
if  (  vec  .  size  (  )  ==  0  )  { 
return   null  ; 
} 
Field  [  ]  out  =  new   Field  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  out  )  ; 
return   out  ; 
} 







public   static   Method  [  ]  getStaticMethods  (  Class   cl  )  { 
Method  [  ]  members  =  cl  .  getMethods  (  )  ; 
Vector   vec  =  new   Vector  (  )  ; 
if  (  members  .  length  ==  0  )  { 
return   null  ; 
} 
int   n  =  members  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
Method   memb  =  members  [  i  ]  ; 
if  (  isStatic  (  memb  )  )  { 
vec  .  add  (  memb  )  ; 
} 
} 
if  (  vec  .  size  (  )  ==  0  )  { 
return   null  ; 
} 
Method  [  ]  out  =  new   Method  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  out  )  ; 
return   out  ; 
} 








public   static   String  [  ]  getFieldNames  (  Class   cl  ,  boolean   staticRequired  )  { 
return   getMemberNames  (  cl  .  getFields  (  )  ,  staticRequired  )  ; 
} 









public   static   String  [  ]  getMethodNames  (  Class   cl  ,  boolean   staticRequired  )  { 
return   getMemberNames  (  cl  .  getMethods  (  )  ,  staticRequired  )  ; 
} 

private   static   String  [  ]  getMemberNames  (  Member  [  ]  members  ,  boolean   staticRequired  )  { 
String  [  ]  result  =  null  ; 
int   nm  =  members  .  length  ; 
if  (  nm  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
if  (  staticRequired  )  { 
Vector   names  =  new   Vector  (  )  ; 
for  (  int   i  =  0  ;  i  <  nm  ;  i  ++  )  { 
Member   member  =  members  [  i  ]  ; 
if  (  isStatic  (  member  )  )  { 
names  .  add  (  getCompletionName  (  member  )  )  ; 
} 
} 
if  (  names  .  size  (  )  ==  0  )  { 
return   new   String  [  0  ]  ; 
} 
result  =  new   String  [  names  .  size  (  )  ]  ; 
names  .  toArray  (  result  )  ; 
}  else  { 
result  =  new   String  [  nm  ]  ; 
for  (  int   i  =  0  ;  i  <  nm  ;  i  ++  )  { 
result  [  i  ]  =  getCompletionName  (  members  [  i  ]  )  ; 
} 
} 
return   result  ; 
} 













public   static   String   getCompletionName  (  Member   m  )  { 
if  (  m   instanceof   Field  )  return   m  .  getName  (  )  ; 
if  (  m   instanceof   Method  )  { 
String   suffix  =  (  (  (  Method  )  m  )  .  getParameterTypes  (  )  .  length  ==  0  )  ?  ")"  :  ""  ; 
return   m  .  getName  (  )  +  "("  +  suffix  ; 
} 
return  ""  ; 
} 







public   static   boolean   isStatic  (  Member   member  )  { 
return  (  member  .  getModifiers  (  )  &  Modifier  .  STATIC  )  !=  0  ; 
} 











public   static   boolean   hasField  (  Object   o  ,  String   name  )  { 
return   classHasField  (  o  .  getClass  (  )  ,  name  ,  false  )  ; 
} 











public   static   boolean   hasClass  (  Object   o  ,  String   name  )  { 
return   classHasClass  (  o  .  getClass  (  )  ,  name  ,  false  )  ; 
} 












public   static   boolean   classHasField  (  Class   cl  ,  String   name  ,  boolean   staticRequired  )  { 
Field  [  ]  fields  =  cl  .  getFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  if  (  name  .  equals  (  fields  [  i  ]  .  getName  (  )  )  &&  (  !  staticRequired  ||  (  (  fields  [  i  ]  .  getModifiers  (  )  &  Modifier  .  STATIC  )  !=  0  )  )  )  return   true  ; 
return   false  ; 
} 












public   static   boolean   classHasMethod  (  Class   cl  ,  String   name  ,  boolean   staticRequired  )  { 
Method  [  ]  methodz  =  cl  .  getMethods  (  )  ; 
for  (  int   i  =  0  ;  i  <  methodz  .  length  ;  i  ++  )  if  (  name  .  equals  (  methodz  [  i  ]  .  getName  (  )  )  &&  (  !  staticRequired  ||  (  (  methodz  [  i  ]  .  getModifiers  (  )  &  Modifier  .  STATIC  )  !=  0  )  )  )  return   true  ; 
return   false  ; 
} 












public   static   boolean   classHasClass  (  Class   cl  ,  String   name  ,  boolean   staticRequired  )  { 
Class  [  ]  clazzes  =  cl  .  getClasses  (  )  ; 
for  (  int   i  =  0  ;  i  <  clazzes  .  length  ;  i  ++  )  if  (  name  .  equals  (  getSimpleName  (  clazzes  [  i  ]  .  getName  (  )  )  )  &&  (  !  staticRequired  ||  isStatic  (  clazzes  [  i  ]  )  )  )  return   true  ; 
return   false  ; 
} 











public   static   boolean   hasMethod  (  Object   o  ,  String   name  )  { 
return   classHasMethod  (  o  .  getClass  (  )  ,  name  ,  false  )  ; 
} 





public   static   Object   newInstance  (  Class   o_clazz  ,  Object  [  ]  args  ,  Class  [  ]  clazzes  )  throws   Throwable  { 
boolean  [  ]  is_null  =  new   boolean  [  args  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
is_null  [  i  ]  =  (  args  [  i  ]  ==  null  )  ; 
} 
Constructor   cons  =  getConstructor  (  o_clazz  ,  clazzes  ,  is_null  )  ; 
boolean   access  =  cons  .  isAccessible  (  )  ; 
cons  .  setAccessible  (  true  )  ; 
Object   o  ; 
try  { 
o  =  cons  .  newInstance  (  args  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
throw   e  .  getTargetException  (  )  ; 
}  finally  { 
cons  .  setAccessible  (  access  )  ; 
} 
return   o  ; 
} 

static   boolean  [  ]  arg_is_null  (  Object  [  ]  args  )  { 
if  (  args  ==  null  )  return   null  ; 
boolean  [  ]  is_null  =  new   boolean  [  args  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
is_null  [  i  ]  =  (  args  [  i  ]  ==  null  )  ; 
} 
return   is_null  ; 
} 






public   static   Object   invokeMethod  (  Class   o_clazz  ,  Object   o  ,  String   name  ,  Object  [  ]  args  ,  Class  [  ]  clazzes  )  throws   Throwable  { 
Method   m  =  getMethod  (  o_clazz  ,  name  ,  clazzes  ,  arg_is_null  (  args  )  )  ; 
boolean   access  =  m  .  isAccessible  (  )  ; 
m  .  setAccessible  (  true  )  ; 
Object   out  ; 
try  { 
out  =  m  .  invoke  (  o  ,  args  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
throw   e  .  getTargetException  (  )  ; 
}  finally  { 
m  .  setAccessible  (  access  )  ; 
} 
return   out  ; 
} 












public   static   Constructor   getConstructor  (  Class   o_clazz  ,  Class  [  ]  arg_clazz  ,  boolean  [  ]  arg_is_null  )  throws   SecurityException  ,  NoSuchMethodException  { 
if  (  o_clazz  ==  null  )  return   null  ; 
Constructor   cons  =  null  ; 
if  (  arg_clazz  ==  null  ||  arg_clazz  .  length  ==  0  )  { 
cons  =  o_clazz  .  getConstructor  (  (  Class  [  ]  )  null  )  ; 
return   cons  ; 
} 
try  { 
cons  =  o_clazz  .  getConstructor  (  arg_clazz  )  ; 
if  (  cons  !=  null  )  return   cons  ; 
}  catch  (  NoSuchMethodException   e  )  { 
} 
cons  =  null  ; 
Constructor  [  ]  candidates  =  o_clazz  .  getConstructors  (  )  ; 
for  (  int   k  =  0  ;  k  <  candidates  .  length  ;  k  ++  )  { 
Constructor   c  =  candidates  [  k  ]  ; 
Class  [  ]  param_clazz  =  c  .  getParameterTypes  (  )  ; 
if  (  arg_clazz  .  length  !=  param_clazz  .  length  )  continue  ; 
int   n  =  arg_clazz  .  length  ; 
boolean   ok  =  true  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  arg_is_null  [  i  ]  )  { 
if  (  isPrimitive  (  arg_clazz  [  i  ]  )  )  { 
ok  =  false  ; 
break  ; 
} 
}  else  { 
if  (  arg_clazz  [  i  ]  !=  null  &&  !  param_clazz  [  i  ]  .  isAssignableFrom  (  arg_clazz  [  i  ]  )  )  { 
ok  =  false  ; 
break  ; 
} 
} 
} 
if  (  ok  &&  (  cons  ==  null  ||  isMoreSpecific  (  c  ,  cons  )  )  )  cons  =  c  ; 
} 
if  (  cons  ==  null  )  { 
throw   new   NoSuchMethodException  (  "No constructor matching the given parameters"  )  ; 
} 
return   cons  ; 
} 

static   boolean   isPrimitive  (  Class   cl  )  { 
return   cl  .  equals  (  Boolean  .  TYPE  )  ||  cl  .  equals  (  Integer  .  TYPE  )  ||  cl  .  equals  (  Double  .  TYPE  )  ||  cl  .  equals  (  Float  .  TYPE  )  ||  cl  .  equals  (  Long  .  TYPE  )  ||  cl  .  equals  (  Short  .  TYPE  )  ||  cl  .  equals  (  Character  .  TYPE  )  ; 
} 












public   static   Method   getMethod  (  Class   o_clazz  ,  String   name  ,  Class  [  ]  arg_clazz  ,  boolean  [  ]  arg_is_null  )  throws   SecurityException  ,  NoSuchMethodException  { 
if  (  o_clazz  ==  null  )  return   null  ; 
if  (  arg_clazz  ==  null  ||  arg_clazz  .  length  ==  0  )  { 
return   o_clazz  .  getMethod  (  name  ,  (  Class  [  ]  )  null  )  ; 
} 
Method   met  ; 
try  { 
met  =  o_clazz  .  getMethod  (  name  ,  arg_clazz  )  ; 
if  (  met  !=  null  )  return   met  ; 
}  catch  (  NoSuchMethodException   e  )  { 
} 
met  =  null  ; 
Method  [  ]  ml  =  o_clazz  .  getMethods  (  )  ; 
for  (  int   k  =  0  ;  k  <  ml  .  length  ;  k  ++  )  { 
Method   m  =  ml  [  k  ]  ; 
if  (  !  m  .  getName  (  )  .  equals  (  name  )  )  continue  ; 
Class  [  ]  param_clazz  =  m  .  getParameterTypes  (  )  ; 
if  (  arg_clazz  .  length  !=  param_clazz  .  length  )  continue  ; 
int   n  =  arg_clazz  .  length  ; 
boolean   ok  =  true  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  arg_is_null  [  i  ]  )  { 
if  (  isPrimitive  (  arg_clazz  [  i  ]  )  )  { 
ok  =  false  ; 
break  ; 
} 
}  else  { 
if  (  arg_clazz  [  i  ]  !=  null  &&  !  param_clazz  [  i  ]  .  isAssignableFrom  (  arg_clazz  [  i  ]  )  )  { 
ok  =  false  ; 
break  ; 
} 
} 
} 
if  (  ok  &&  (  met  ==  null  ||  isMoreSpecific  (  m  ,  met  )  )  )  met  =  m  ; 
} 
if  (  met  ==  null  )  { 
throw   new   NoSuchMethodException  (  "No suitable method for the given parameters"  )  ; 
} 
return   met  ; 
} 










private   static   boolean   isMoreSpecific  (  Method   m1  ,  Method   m2  )  { 
Class  [  ]  m1_param_clazz  =  m1  .  getParameterTypes  (  )  ; 
Class  [  ]  m2_param_clazz  =  m2  .  getParameterTypes  (  )  ; 
return   isMoreSpecific  (  m1_param_clazz  ,  m2_param_clazz  )  ; 
} 










private   static   boolean   isMoreSpecific  (  Constructor   cons1  ,  Constructor   cons2  )  { 
Class  [  ]  cons1_param_clazz  =  cons1  .  getParameterTypes  (  )  ; 
Class  [  ]  cons2_param_clazz  =  cons2  .  getParameterTypes  (  )  ; 
return   isMoreSpecific  (  cons1_param_clazz  ,  cons2_param_clazz  )  ; 
} 














private   static   boolean   isMoreSpecific  (  Class  [  ]  c1  ,  Class  [  ]  c2  )  { 
int   n  =  c1  .  length  ; 
int   res  =  0  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  if  (  c1  [  i  ]  !=  c2  [  i  ]  )  { 
if  (  c1  [  i  ]  .  isAssignableFrom  (  c2  [  i  ]  )  )  res  --  ;  else   if  (  c2  [  i  ]  .  isAssignableFrom  (  c2  [  i  ]  )  )  res  ++  ; 
} 
return   res  >  0  ; 
} 






public   static   Class  [  ]  getClasses  (  Object   o  )  { 
Vector   vec  =  new   Vector  (  )  ; 
Class   cl  =  o  .  getClass  (  )  ; 
while  (  cl  !=  null  )  { 
vec  .  add  (  cl  )  ; 
cl  =  cl  .  getSuperclass  (  )  ; 
} 
Class  [  ]  res  =  new   Class  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  res  )  ; 
return   res  ; 
} 






public   static   String  [  ]  getClassNames  (  Object   o  )  { 
Vector   vec  =  new   Vector  (  )  ; 
Class   cl  =  o  .  getClass  (  )  ; 
while  (  cl  !=  null  )  { 
vec  .  add  (  cl  .  getName  (  )  )  ; 
cl  =  cl  .  getSuperclass  (  )  ; 
} 
String  [  ]  res  =  new   String  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  res  )  ; 
return   res  ; 
} 






public   static   String  [  ]  getSimpleClassNames  (  Object   o  ,  boolean   addConditionClasses  )  { 
boolean   hasException  =  false  ; 
Vector   vec  =  new   Vector  (  )  ; 
Class   cl  =  o  .  getClass  (  )  ; 
String   name  ; 
while  (  cl  !=  null  )  { 
name  =  getSimpleName  (  cl  .  getName  (  )  )  ; 
if  (  "Exception"  .  equals  (  name  )  )  { 
hasException  =  true  ; 
} 
vec  .  add  (  name  )  ; 
cl  =  cl  .  getSuperclass  (  )  ; 
} 
if  (  addConditionClasses  )  { 
if  (  !  hasException  )  { 
vec  .  add  (  "Exception"  )  ; 
} 
vec  .  add  (  "error"  )  ; 
vec  .  add  (  "condition"  )  ; 
} 
String  [  ]  res  =  new   String  [  vec  .  size  (  )  ]  ; 
vec  .  toArray  (  res  )  ; 
return   res  ; 
} 

private   static   String   getSimpleClassName  (  Object   o  )  { 
return   getSimpleName  (  o  .  getClass  (  )  .  getName  (  )  )  ; 
} 

private   static   String   getSimpleName  (  String   s  )  { 
int   lastsquare  =  s  .  lastIndexOf  (  '['  )  ; 
if  (  lastsquare  >=  0  )  { 
if  (  s  .  charAt  (  s  .  lastIndexOf  (  '['  )  +  1  )  ==  'L'  )  { 
s  =  s  .  substring  (  s  .  lastIndexOf  (  '['  )  +  2  ,  s  .  lastIndexOf  (  ';'  )  )  ; 
}  else  { 
char   first  =  s  .  charAt  (  0  )  ; 
if  (  first  ==  'I'  )  { 
s  =  "int"  ; 
}  else   if  (  first  ==  'D'  )  { 
s  =  "double"  ; 
}  else   if  (  first  ==  'Z'  )  { 
s  =  "boolean"  ; 
}  else   if  (  first  ==  'B'  )  { 
s  =  "byte"  ; 
}  else   if  (  first  ==  'J'  )  { 
s  =  "long"  ; 
}  else   if  (  first  ==  'F'  )  { 
s  =  "float"  ; 
}  else   if  (  first  ==  'S'  )  { 
s  =  "short"  ; 
}  else   if  (  first  ==  'C'  )  { 
s  =  "char"  ; 
} 
} 
} 
int   lastdollar  =  s  .  lastIndexOf  (  '$'  )  ; 
if  (  lastdollar  >=  0  )  { 
s  =  s  .  substring  (  lastdollar  +  1  )  ; 
} 
int   lastdot  =  s  .  lastIndexOf  (  '.'  )  ; 
if  (  lastdot  >=  0  )  { 
s  =  s  .  substring  (  lastdot  +  1  )  ; 
} 
if  (  lastsquare  >=  0  )  { 
StringBuffer   buf  =  new   StringBuffer  (  s  )  ; 
int   i  ; 
for  (  i  =  0  ;  i  <=  lastsquare  ;  i  ++  )  { 
buf  .  append  (  "[]"  )  ; 
} 
return   buf  .  toString  (  )  ; 
}  else  { 
return   s  ; 
} 
} 








public   static   String   getFieldTypeName  (  Class   cl  ,  String   field  )  { 
String   res  =  null  ; 
try  { 
res  =  cl  .  getField  (  field  )  .  getType  (  )  .  getName  (  )  ; 
}  catch  (  NoSuchFieldException   e  )  { 
res  =  null  ; 
} 
return   res  ; 
} 
} 

