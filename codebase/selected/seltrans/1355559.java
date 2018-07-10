package   com  .  bluebrim  .  collection  .  shared  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 









































































public   class   CoArrayList   extends   AbstractList   implements   List  ,  Cloneable  ,  Serializable  { 





protected   Object   elementData  [  ]  ; 






private   int   size  ; 

private   static   final   long   serialVersionUID  =  152047731822007244L  ; 




public   CoArrayList  (  )  { 
this  (  10  )  ; 
} 






public   CoArrayList  (  int   initialCapacity  )  { 
super  (  )  ; 
this  .  elementData  =  new   Object  [  initialCapacity  ]  ; 
} 







public   CoArrayList  (  Collection   c  )  { 
this  (  (  c  .  size  (  )  *  110  )  /  100  )  ; 
Iterator   i  =  c  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  elementData  [  size  ++  ]  =  i  .  next  (  )  ; 
} 











public   void   add  (  int   index  ,  Object   element  )  { 
if  (  index  >  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
ensureCapacity  (  size  +  1  )  ; 
System  .  arraycopy  (  elementData  ,  index  ,  elementData  ,  index  +  1  ,  size  -  index  )  ; 
elementData  [  index  ]  =  element  ; 
size  ++  ; 
} 







public   boolean   add  (  Object   o  )  { 
ensureCapacity  (  size  +  1  )  ; 
elementData  [  size  ++  ]  =  o  ; 
return   true  ; 
} 















public   boolean   addAll  (  int   index  ,  Collection   c  )  { 
if  (  index  >  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
int   numNew  =  c  .  size  (  )  ; 
ensureCapacity  (  size  +  numNew  )  ; 
int   numMoved  =  size  -  index  ; 
if  (  numMoved  >  0  )  System  .  arraycopy  (  elementData  ,  index  ,  elementData  ,  index  +  numNew  ,  numMoved  )  ; 
Iterator   e  =  c  .  iterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  numNew  ;  i  ++  )  elementData  [  index  ++  ]  =  e  .  next  (  )  ; 
size  +=  numNew  ; 
return   numNew  !=  0  ; 
} 
















public   boolean   addAll  (  Collection   c  )  { 
modCount  ++  ; 
int   numNew  =  c  .  size  (  )  ; 
ensureCapacity  (  size  +  numNew  )  ; 
Iterator   e  =  c  .  iterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  numNew  ;  i  ++  )  elementData  [  size  ++  ]  =  e  .  next  (  )  ; 
return   numNew  !=  0  ; 
} 





public   void   clear  (  )  { 
modCount  ++  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  elementData  [  i  ]  =  null  ; 
size  =  0  ; 
} 







public   Object   clone  (  )  { 
try  { 
CoArrayList   v  =  (  CoArrayList  )  super  .  clone  (  )  ; 
v  .  elementData  =  new   Object  [  size  ]  ; 
System  .  arraycopy  (  elementData  ,  0  ,  v  .  elementData  ,  0  ,  size  )  ; 
v  .  modCount  =  0  ; 
return   v  ; 
}  catch  (  CloneNotSupportedException   e  )  { 
throw   new   InternalError  (  )  ; 
} 
} 






public   boolean   contains  (  Object   elem  )  { 
return   indexOf  (  elem  )  >=  0  ; 
} 








public   void   ensureCapacity  (  int   minCapacity  )  { 
modCount  ++  ; 
int   oldCapacity  =  elementData  .  length  ; 
if  (  minCapacity  >  oldCapacity  )  { 
Object   oldData  [  ]  =  elementData  ; 
int   newCapacity  =  (  oldCapacity  *  3  )  /  2  +  1  ; 
if  (  newCapacity  <  minCapacity  )  newCapacity  =  minCapacity  ; 
elementData  =  new   Object  [  newCapacity  ]  ; 
System  .  arraycopy  (  oldData  ,  0  ,  elementData  ,  0  ,  size  )  ; 
} 
} 









public   Object   get  (  int   index  )  { 
RangeCheck  (  index  )  ; 
return   elementData  [  index  ]  ; 
} 










public   int   indexOf  (  Object   elem  )  { 
if  (  elem  ==  null  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  if  (  elementData  [  i  ]  ==  null  )  return   i  ; 
}  else  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  if  (  elem  .  equals  (  elementData  [  i  ]  )  )  return   i  ; 
} 
return  -  1  ; 
} 







public   boolean   isEmpty  (  )  { 
return   size  ==  0  ; 
} 









public   int   lastIndexOf  (  Object   elem  )  { 
if  (  elem  ==  null  )  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  i  --  )  if  (  elementData  [  i  ]  ==  null  )  return   i  ; 
}  else  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  i  --  )  if  (  elem  .  equals  (  elementData  [  i  ]  )  )  return   i  ; 
} 
return  -  1  ; 
} 





private   void   RangeCheck  (  int   index  )  { 
if  (  index  >=  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
} 











public   Object   remove  (  int   index  )  { 
RangeCheck  (  index  )  ; 
modCount  ++  ; 
Object   oldValue  =  elementData  [  index  ]  ; 
int   numMoved  =  size  -  index  -  1  ; 
if  (  numMoved  >  0  )  System  .  arraycopy  (  elementData  ,  index  +  1  ,  elementData  ,  index  ,  numMoved  )  ; 
elementData  [  --  size  ]  =  null  ; 
return   oldValue  ; 
} 











public   void   removeRange  (  int   fromIndex  ,  int   toIndex  )  { 
modCount  ++  ; 
int   numMoved  =  size  -  toIndex  ; 
System  .  arraycopy  (  elementData  ,  toIndex  ,  elementData  ,  fromIndex  ,  numMoved  )  ; 
int   newSize  =  size  -  (  toIndex  -  fromIndex  )  ; 
while  (  size  !=  newSize  )  elementData  [  --  size  ]  =  null  ; 
} 











public   Object   set  (  int   index  ,  Object   element  )  { 
RangeCheck  (  index  )  ; 
Object   oldValue  =  elementData  [  index  ]  ; 
elementData  [  index  ]  =  element  ; 
return   oldValue  ; 
} 






public   int   size  (  )  { 
return   size  ; 
} 








public   Object  [  ]  toArray  (  )  { 
Object  [  ]  result  =  new   Object  [  size  ]  ; 
System  .  arraycopy  (  elementData  ,  0  ,  result  ,  0  ,  size  )  ; 
return   result  ; 
} 






















public   Object  [  ]  toArray  (  Object   a  [  ]  )  { 
if  (  a  .  length  <  size  )  a  =  (  Object  [  ]  )  java  .  lang  .  reflect  .  Array  .  newInstance  (  a  .  getClass  (  )  .  getComponentType  (  )  ,  size  )  ; 
System  .  arraycopy  (  elementData  ,  0  ,  a  ,  0  ,  size  )  ; 
if  (  a  .  length  >  size  )  a  [  size  ]  =  null  ; 
return   a  ; 
} 






public   void   trimToSize  (  )  { 
modCount  ++  ; 
int   oldCapacity  =  elementData  .  length  ; 
if  (  size  <  oldCapacity  )  { 
Object   oldData  [  ]  =  elementData  ; 
elementData  =  new   Object  [  size  ]  ; 
System  .  arraycopy  (  oldData  ,  0  ,  elementData  ,  0  ,  size  )  ; 
} 
} 
} 

