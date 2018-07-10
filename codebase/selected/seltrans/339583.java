package   net  .  sourceforge  .  javautil  .  common  .  reflection  .  cache  ; 

import   java  .  lang  .  annotation  .  Annotation  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Member  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  lang  .  reflect  .  Type  ; 
import   net  .  sourceforge  .  javautil  .  common  .  reflection  .  ReflectionContext  ; 









public   class   ClassProperty   extends   ClassMember   implements   IClassMemberWritableValue  { 




protected   final   String   name  ; 




protected   final   ClassMethod   reader  ; 




protected   final   ClassMethod   writer  ; 




protected   final   Class  <  ?  >  type  ; 







public   ClassProperty  (  ClassDescriptor   descriptor  ,  String   name  ,  ClassMethod   reader  ,  ClassMethod   writer  )  { 
super  (  descriptor  )  ; 
this  .  name  =  name  ; 
this  .  reader  =  reader  ; 
this  .  writer  =  writer  ; 
this  .  type  =  reader  !=  null  ?  reader  .  getReturnType  (  )  :  writer  .  getParameterTypes  (  )  [  0  ]  ; 
} 




public   ClassDescriptor  <  ?  >  getDescriptor  (  )  { 
return   descriptor  ; 
} 









public  <  A   extends   Annotation  >  A   getAnnotation  (  Class  <  A  >  clazz  )  { 
A   annotation  =  reader  !=  null  ?  reader  .  getAnnotation  (  clazz  )  :  null  ; 
return   annotation  ==  null  &&  writer  !=  null  ?  writer  .  getAnnotation  (  clazz  )  :  annotation  ; 
} 

@  Override 
public   Annotation  [  ]  getAnnotations  (  )  { 
return   reader  ==  null  ?  writer  .  getAnnotations  (  )  :  reader  .  getAnnotations  (  )  ; 
} 

@  Override 
public   Class   getBaseType  (  )  { 
return   reader  ==  null  ?  writer  .  getBaseType  (  )  :  reader  .  getBaseType  (  )  ; 
} 

@  Override 
public   Method   getJavaMember  (  )  { 
return   reader  ==  null  ?  writer  .  getJavaMember  (  )  :  reader  .  getJavaMember  (  )  ; 
} 

@  Override 
public   boolean   isStatic  (  )  { 
return   Modifier  .  isStatic  (  this  .  getJavaMember  (  )  .  getModifiers  (  )  )  ; 
} 




public   String   getName  (  )  { 
return   name  ; 
} 




public   ClassMethod   getReader  (  )  { 
return   reader  ; 
} 




public   ClassMethod   getWriter  (  )  { 
return   writer  ; 
} 






public   boolean   isReadable  (  )  { 
return   this  .  reader  !=  null  ; 
} 






public   boolean   isWritable  (  )  { 
return   this  .  writer  !=  null  ; 
} 




public   Class  <  ?  >  getType  (  )  { 
return   type  ; 
} 




public   Type   getGenericType  (  )  { 
return   reader  ==  null  ?  writer  .  getGenericParameterTypes  (  )  [  0  ]  :  reader  .  getGenericReturnType  (  )  ; 
} 




public   ClassDescriptor  <  ?  >  getTypeDescriptor  (  )  { 
return   descriptor  .  cache  .  getDescriptor  (  type  )  ; 
} 










public   void   setValue  (  Object   instance  ,  Object   value  )  { 
if  (  writer  ==  null  )  throw   new   ClassPropertyAccessException  (  descriptor  ,  this  ,  "Property not writable"  )  ; 
writer  .  invoke  (  instance  ,  value  )  ; 
} 










public   Object   getValue  (  Object   instance  )  { 
if  (  reader  ==  null  )  throw   new   ClassPropertyAccessException  (  descriptor  ,  this  ,  "Property not readable"  )  ; 
return   reader  .  invoke  (  instance  )  ; 
} 
} 

