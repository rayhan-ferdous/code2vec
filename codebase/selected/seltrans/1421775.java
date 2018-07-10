package   org  .  eclipse  .  emf  .  common  .  util  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ListIterator  ; 
import   java  .  util  .  RandomAccess  ; 




public   class   BasicEList  <  E  >  extends   AbstractEList  <  E  >  implements   RandomAccess  ,  Cloneable  ,  Serializable  { 

private   static   final   long   serialVersionUID  =  1L  ; 




protected   int   size  ; 




protected   transient   Object  [  ]  data  ; 





public   BasicEList  (  )  { 
super  (  )  ; 
} 






public   BasicEList  (  int   initialCapacity  )  { 
if  (  initialCapacity  <  0  )  { 
throw   new   IllegalArgumentException  (  "Illegal Capacity: "  +  initialCapacity  )  ; 
} 
data  =  newData  (  initialCapacity  )  ; 
} 





public   BasicEList  (  Collection  <  ?  extends   E  >  collection  )  { 
size  =  collection  .  size  (  )  ; 
if  (  size  >  0  )  { 
data  =  newData  (  size  +  size  /  8  +  1  )  ; 
collection  .  toArray  (  data  )  ; 
} 
} 






protected   BasicEList  (  int   size  ,  Object  [  ]  data  )  { 
this  .  size  =  size  ; 
this  .  data  =  data  ; 
} 







protected   Object  [  ]  newData  (  int   capacity  )  { 
return   new   Object  [  capacity  ]  ; 
} 









protected   E   assign  (  int   index  ,  E   object  )  { 
data  [  index  ]  =  object  ; 
return   object  ; 
} 





@  Override 
public   int   size  (  )  { 
return   size  ; 
} 





@  Override 
public   boolean   isEmpty  (  )  { 
return   size  ==  0  ; 
} 








@  Override 
public   boolean   contains  (  Object   object  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
if  (  object  .  equals  (  data  [  i  ]  )  )  { 
return   true  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
if  (  data  [  i  ]  ==  object  )  { 
return   true  ; 
} 
} 
} 
return   false  ; 
} 







@  Override 
public   int   indexOf  (  Object   object  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
if  (  object  .  equals  (  data  [  i  ]  )  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
if  (  data  [  i  ]  ==  object  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 







@  Override 
public   int   lastIndexOf  (  Object   object  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  --  i  )  { 
if  (  object  .  equals  (  data  [  i  ]  )  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  --  i  )  { 
if  (  data  [  i  ]  ==  object  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 







@  Override 
public   Object  [  ]  toArray  (  )  { 
Object  [  ]  result  =  newData  (  size  )  ; 
if  (  size  >  0  )  { 
System  .  arraycopy  (  data  ,  0  ,  result  ,  0  ,  size  )  ; 
} 
return   result  ; 
} 








@  Override 
public  <  T  >  T  [  ]  toArray  (  T  [  ]  array  )  { 
if  (  size  >  0  )  { 
if  (  array  .  length  <  size  )  { 
@  SuppressWarnings  (  "unchecked"  )  T  [  ]  newArray  =  (  T  [  ]  )  Array  .  newInstance  (  array  .  getClass  (  )  .  getComponentType  (  )  ,  size  )  ; 
array  =  newArray  ; 
} 
System  .  arraycopy  (  data  ,  0  ,  array  ,  0  ,  size  )  ; 
} 
if  (  array  .  length  >  size  )  { 
array  [  size  ]  =  null  ; 
} 
return   array  ; 
} 







public   Object  [  ]  data  (  )  { 
return   data  ; 
} 






public   void   setData  (  int   size  ,  Object  [  ]  data  )  { 
this  .  size  =  size  ; 
this  .  data  =  data  ; 
++  modCount  ; 
} 





protected   static   class   BasicIndexOutOfBoundsException   extends   AbstractEList  .  BasicIndexOutOfBoundsException  { 

private   static   final   long   serialVersionUID  =  1L  ; 




public   BasicIndexOutOfBoundsException  (  int   index  ,  int   size  )  { 
super  (  index  ,  size  )  ; 
} 
} 











@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   E   get  (  int   index  )  { 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return   resolve  (  index  ,  (  E  )  data  [  index  ]  )  ; 
} 









@  Override 
public   E   basicGet  (  int   index  )  { 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return   primitiveGet  (  index  )  ; 
} 









@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
protected   E   primitiveGet  (  int   index  )  { 
return  (  E  )  data  [  index  ]  ; 
} 











@  Override 
public   E   setUnique  (  int   index  ,  E   object  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   oldObject  =  (  E  )  data  [  index  ]  ; 
assign  (  index  ,  validate  (  index  ,  object  )  )  ; 
didSet  (  index  ,  object  ,  oldObject  )  ; 
didChange  (  )  ; 
return   oldObject  ; 
} 









@  Override 
public   void   addUnique  (  E   object  )  { 
grow  (  size  +  1  )  ; 
assign  (  size  ,  validate  (  size  ,  object  )  )  ; 
didAdd  (  size  ++  ,  object  )  ; 
didChange  (  )  ; 
} 








@  Override 
public   void   addUnique  (  int   index  ,  E   object  )  { 
grow  (  size  +  1  )  ; 
E   validatedObject  =  validate  (  index  ,  object  )  ; 
if  (  index  !=  size  )  { 
System  .  arraycopy  (  data  ,  index  ,  data  ,  index  +  1  ,  size  -  index  )  ; 
} 
assign  (  index  ,  validatedObject  )  ; 
++  size  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
} 








@  Override 
public   boolean   addAllUnique  (  Collection  <  ?  extends   E  >  collection  )  { 
int   growth  =  collection  .  size  (  )  ; 
grow  (  size  +  growth  )  ; 
Iterator  <  ?  extends   E  >  objects  =  collection  .  iterator  (  )  ; 
int   oldSize  =  size  ; 
size  +=  growth  ; 
for  (  int   i  =  oldSize  ;  i  <  size  ;  ++  i  )  { 
E   object  =  objects  .  next  (  )  ; 
assign  (  i  ,  validate  (  i  ,  object  )  )  ; 
didAdd  (  i  ,  object  )  ; 
didChange  (  )  ; 
} 
return   growth  !=  0  ; 
} 











@  Override 
public   boolean   addAllUnique  (  int   index  ,  Collection  <  ?  extends   E  >  collection  )  { 
int   growth  =  collection  .  size  (  )  ; 
grow  (  size  +  growth  )  ; 
int   shifted  =  size  -  index  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  data  ,  index  ,  data  ,  index  +  growth  ,  shifted  )  ; 
} 
Iterator  <  ?  extends   E  >  objects  =  collection  .  iterator  (  )  ; 
size  +=  growth  ; 
for  (  int   i  =  0  ;  i  <  growth  ;  ++  i  )  { 
E   object  =  objects  .  next  (  )  ; 
assign  (  index  ,  validate  (  index  ,  object  )  )  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
++  index  ; 
} 
return   growth  !=  0  ; 
} 












@  Override 
public   boolean   addAllUnique  (  Object  [  ]  objects  ,  int   start  ,  int   end  )  { 
int   growth  =  end  -  start  ; 
grow  (  size  +  growth  )  ; 
size  +=  growth  ; 
int   index  =  size  ; 
for  (  int   i  =  start  ;  i  <  end  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
assign  (  index  ,  validate  (  index  ,  object  )  )  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
++  index  ; 
} 
return   growth  !=  0  ; 
} 













@  Override 
public   boolean   addAllUnique  (  int   index  ,  Object  [  ]  objects  ,  int   start  ,  int   end  )  { 
int   growth  =  end  -  start  ; 
grow  (  size  +  growth  )  ; 
int   shifted  =  size  -  index  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  data  ,  index  ,  data  ,  index  +  growth  ,  shifted  )  ; 
} 
size  +=  growth  ; 
for  (  int   i  =  start  ;  i  <  end  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
assign  (  index  ,  validate  (  index  ,  object  )  )  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
++  index  ; 
} 
return   growth  !=  0  ; 
} 








@  Override 
public   E   remove  (  int   index  )  { 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
++  modCount  ; 
@  SuppressWarnings  (  "unchecked"  )  E   oldObject  =  (  E  )  data  [  index  ]  ; 
int   shifted  =  size  -  index  -  1  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  data  ,  index  +  1  ,  data  ,  index  ,  shifted  )  ; 
} 
data  [  --  size  ]  =  null  ; 
didRemove  (  index  ,  oldObject  )  ; 
didChange  (  )  ; 
return   oldObject  ; 
} 






@  Override 
public   void   clear  (  )  { 
++  modCount  ; 
Object  [  ]  oldData  =  data  ; 
int   oldSize  =  size  ; 
data  =  null  ; 
size  =  0  ; 
didClear  (  oldSize  ,  oldData  )  ; 
didChange  (  )  ; 
} 










@  Override 
public   E   move  (  int   targetIndex  ,  int   sourceIndex  )  { 
++  modCount  ; 
if  (  targetIndex  >=  size  )  throw   new   IndexOutOfBoundsException  (  "targetIndex="  +  targetIndex  +  ", size="  +  size  )  ; 
if  (  sourceIndex  >=  size  )  throw   new   IndexOutOfBoundsException  (  "sourceIndex="  +  sourceIndex  +  ", size="  +  size  )  ; 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  data  [  sourceIndex  ]  ; 
if  (  targetIndex  !=  sourceIndex  )  { 
if  (  targetIndex  <  sourceIndex  )  { 
System  .  arraycopy  (  data  ,  targetIndex  ,  data  ,  targetIndex  +  1  ,  sourceIndex  -  targetIndex  )  ; 
}  else  { 
System  .  arraycopy  (  data  ,  sourceIndex  +  1  ,  data  ,  sourceIndex  ,  targetIndex  -  sourceIndex  )  ; 
} 
assign  (  targetIndex  ,  object  )  ; 
didMove  (  targetIndex  ,  object  ,  sourceIndex  )  ; 
didChange  (  )  ; 
} 
return   object  ; 
} 





public   void   shrink  (  )  { 
++  modCount  ; 
if  (  size  ==  0  )  { 
data  =  null  ; 
}  else   if  (  size  <  data  .  length  )  { 
Object  [  ]  oldData  =  data  ; 
data  =  newData  (  size  )  ; 
System  .  arraycopy  (  oldData  ,  0  ,  data  ,  0  ,  size  )  ; 
} 
} 






public   void   grow  (  int   minimumCapacity  )  { 
++  modCount  ; 
int   oldCapacity  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  minimumCapacity  >  oldCapacity  )  { 
Object   oldData  [  ]  =  data  ; 
int   newCapacity  =  oldCapacity  +  oldCapacity  /  2  +  4  ; 
if  (  newCapacity  <  minimumCapacity  )  { 
newCapacity  =  minimumCapacity  ; 
} 
data  =  newData  (  newCapacity  )  ; 
if  (  oldData  !=  null  )  { 
System  .  arraycopy  (  oldData  ,  0  ,  data  ,  0  ,  size  )  ; 
} 
} 
} 

private   synchronized   void   writeObject  (  ObjectOutputStream   objectOutputStream  )  throws   IOException  { 
objectOutputStream  .  defaultWriteObject  (  )  ; 
if  (  data  ==  null  )  { 
objectOutputStream  .  writeInt  (  0  )  ; 
}  else  { 
objectOutputStream  .  writeInt  (  data  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
objectOutputStream  .  writeObject  (  data  [  i  ]  )  ; 
} 
} 
} 

private   synchronized   void   readObject  (  ObjectInputStream   objectInputStream  )  throws   IOException  ,  ClassNotFoundException  { 
objectInputStream  .  defaultReadObject  (  )  ; 
int   arrayLength  =  objectInputStream  .  readInt  (  )  ; 
if  (  arrayLength  >  0  )  { 
try  { 
data  =  newData  (  arrayLength  )  ; 
}  catch  (  Throwable   exception  )  { 
data  =  new   Object  [  arrayLength  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objectInputStream  .  readObject  (  )  ; 
didAdd  (  i  ,  assign  (  i  ,  object  )  )  ; 
} 
} 
} 





@  Override 
public   Object   clone  (  )  { 
try  { 
@  SuppressWarnings  (  "unchecked"  )  BasicEList  <  E  >  clone  =  (  BasicEList  <  E  >  )  super  .  clone  (  )  ; 
if  (  size  >  0  )  { 
clone  .  size  =  size  ; 
clone  .  data  =  newData  (  size  )  ; 
System  .  arraycopy  (  data  ,  0  ,  clone  .  data  ,  0  ,  size  )  ; 
} 
return   clone  ; 
}  catch  (  CloneNotSupportedException   exception  )  { 
throw   new   InternalError  (  )  ; 
} 
} 






@  Deprecated 
protected   class   EIterator  <  E1  >  extends   AbstractEList  <  E  >  .  EIterator  <  E1  >  { 
} 






@  Deprecated 
protected   class   NonResolvingEIterator  <  E1  >  extends   AbstractEList  <  E  >  .  NonResolvingEIterator  <  E1  >  { 
} 






@  Deprecated 
protected   class   EListIterator  <  E1  >  extends   AbstractEList  <  E  >  .  EListIterator  <  E1  >  { 




public   EListIterator  (  )  { 
super  (  )  ; 
} 





public   EListIterator  (  int   index  )  { 
super  (  index  )  ; 
cursor  =  index  ; 
} 
} 






@  Deprecated 
protected   class   NonResolvingEListIterator  <  E1  >  extends   AbstractEList  <  E  >  .  NonResolvingEListIterator  <  E1  >  { 




public   NonResolvingEListIterator  (  )  { 
super  (  )  ; 
} 





public   NonResolvingEListIterator  (  int   index  )  { 
super  (  index  )  ; 
} 
} 




public   static   class   UnmodifiableEList  <  E  >  extends   BasicEList  <  E  >  { 

private   static   final   long   serialVersionUID  =  1L  ; 






public   UnmodifiableEList  (  int   size  ,  Object  [  ]  data  )  { 
this  .  size  =  size  ; 
this  .  data  =  data  ; 
} 





@  Override 
public   E   set  (  int   index  ,  E   object  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   add  (  E   object  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   void   add  (  int   index  ,  E   object  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   addAll  (  Collection  <  ?  extends   E  >  collection  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   addAll  (  int   index  ,  Collection  <  ?  extends   E  >  collection  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   remove  (  Object   object  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   E   remove  (  int   index  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   removeAll  (  Collection  <  ?  >  collection  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   boolean   retainAll  (  Collection  <  ?  >  collection  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   void   clear  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   void   move  (  int   index  ,  E   object  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   E   move  (  int   targetIndex  ,  int   sourceIndex  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   void   shrink  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   void   grow  (  int   minimumCapacity  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 





@  Override 
public   Iterator  <  E  >  iterator  (  )  { 
return   basicIterator  (  )  ; 
} 





@  Override 
public   ListIterator  <  E  >  listIterator  (  )  { 
return   basicListIterator  (  )  ; 
} 






@  Override 
public   ListIterator  <  E  >  listIterator  (  int   index  )  { 
return   basicListIterator  (  index  )  ; 
} 
} 





@  Override 
protected   List  <  E  >  basicList  (  )  { 
if  (  size  ==  0  )  { 
return   ECollections  .  emptyEList  (  )  ; 
}  else  { 
return   new   UnmodifiableEList  <  E  >  (  size  ,  data  )  ; 
} 
} 




public   static   class   FastCompare  <  E  >  extends   BasicEList  <  E  >  { 

private   static   final   long   serialVersionUID  =  1L  ; 




public   FastCompare  (  )  { 
super  (  )  ; 
} 






public   FastCompare  (  int   initialCapacity  )  { 
super  (  initialCapacity  )  ; 
} 





public   FastCompare  (  Collection  <  ?  extends   E  >  collection  )  { 
super  (  collection  .  size  (  )  )  ; 
addAll  (  collection  )  ; 
} 





@  Override 
protected   boolean   useEquals  (  )  { 
return   false  ; 
} 
} 
} 

