package   org  .  eclipse  .  emf  .  common  .  util  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  ConcurrentModificationException  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ListIterator  ; 
import   java  .  util  .  RandomAccess  ; 





public   abstract   class   ArrayDelegatingEList  <  E  >  extends   AbstractEList  <  E  >  implements   RandomAccess  ,  Cloneable  ,  Serializable  { 

private   static   final   long   serialVersionUID  =  1L  ; 





public   ArrayDelegatingEList  (  )  { 
super  (  )  ; 
} 





public   ArrayDelegatingEList  (  Collection  <  ?  extends   E  >  collection  )  { 
int   size  =  collection  .  size  (  )  ; 
if  (  size  >  0  )  { 
Object  [  ]  data  =  newData  (  size  )  ; 
collection  .  toArray  (  data  )  ; 
setData  (  data  )  ; 
} 
} 





protected   ArrayDelegatingEList  (  Object  [  ]  data  )  { 
setData  (  data  )  ; 
} 







protected   Object  [  ]  newData  (  int   capacity  )  { 
return   new   Object  [  capacity  ]  ; 
} 









protected   E   assign  (  Object  [  ]  data  ,  int   index  ,  E   object  )  { 
data  [  index  ]  =  object  ; 
return   object  ; 
} 





@  Override 
public   final   int   size  (  )  { 
Object  [  ]  data  =  data  (  )  ; 
return   data  ==  null  ?  0  :  data  .  length  ; 
} 





@  Override 
public   boolean   isEmpty  (  )  { 
return   data  (  )  !=  null  ; 
} 








@  Override 
public   boolean   contains  (  Object   object  )  { 
Object  [  ]  data  =  data  (  )  ; 
if  (  data  !=  null  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  Object   datum  :  data  )  { 
if  (  object  .  equals  (  datum  )  )  { 
return   true  ; 
} 
} 
}  else  { 
for  (  Object   datum  :  data  )  { 
if  (  datum  ==  object  )  { 
return   true  ; 
} 
} 
} 
} 
return   false  ; 
} 







@  Override 
public   int   indexOf  (  Object   object  )  { 
Object  [  ]  data  =  data  (  )  ; 
if  (  data  !=  null  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  int   i  =  0  ,  size  =  data  .  length  ;  i  <  size  ;  ++  i  )  { 
if  (  object  .  equals  (  data  [  i  ]  )  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ,  size  =  data  .  length  ;  i  <  size  ;  ++  i  )  { 
if  (  data  [  i  ]  ==  object  )  { 
return   i  ; 
} 
} 
} 
} 
return  -  1  ; 
} 







@  Override 
public   int   lastIndexOf  (  Object   object  )  { 
Object  [  ]  data  =  data  (  )  ; 
if  (  data  !=  null  )  { 
if  (  useEquals  (  )  &&  object  !=  null  )  { 
for  (  int   i  =  data  .  length  -  1  ;  i  >=  0  ;  --  i  )  { 
if  (  object  .  equals  (  data  [  i  ]  )  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  data  .  length  -  1  ;  i  >=  0  ;  --  i  )  { 
if  (  data  [  i  ]  ==  object  )  { 
return   i  ; 
} 
} 
} 
} 
return  -  1  ; 
} 







@  Override 
public   Object  [  ]  toArray  (  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
Object  [  ]  result  =  newData  (  size  )  ; 
if  (  size  >  0  )  { 
System  .  arraycopy  (  data  ,  0  ,  result  ,  0  ,  size  )  ; 
} 
return   result  ; 
} 








@  Override 
public  <  T  >  T  [  ]  toArray  (  T  [  ]  array  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
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









public   abstract   Object  [  ]  data  (  )  ; 








public   void   setData  (  Object  [  ]  data  )  { 
++  modCount  ; 
} 











@  SuppressWarnings  (  {  "unchecked"  ,  "null"  }  ) 
@  Override 
public   E   get  (  int   index  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return   resolve  (  index  ,  (  E  )  data  [  index  ]  )  ; 
} 









@  SuppressWarnings  (  {  "unchecked"  ,  "null"  }  ) 
@  Override 
public   E   basicGet  (  int   index  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return  (  E  )  data  [  index  ]  ; 
} 

@  Override 
@  SuppressWarnings  (  {  "unchecked"  }  ) 
protected   E   primitiveGet  (  int   index  )  { 
return  (  E  )  data  (  )  [  index  ]  ; 
} 











@  Override 
public   E   setUnique  (  int   index  ,  E   object  )  { 
Object  [  ]  data  =  copy  (  )  ; 
@  SuppressWarnings  (  "unchecked"  )  E   oldObject  =  (  E  )  data  [  index  ]  ; 
assign  (  data  ,  index  ,  validate  (  index  ,  object  )  )  ; 
setData  (  data  )  ; 
didSet  (  index  ,  object  ,  oldObject  )  ; 
didChange  (  )  ; 
return   oldObject  ; 
} 









@  Override 
public   void   addUnique  (  E   object  )  { 
int   size  =  size  (  )  ; 
Object  [  ]  data  =  grow  (  size  +  1  )  ; 
assign  (  data  ,  size  ,  validate  (  size  ,  object  )  )  ; 
setData  (  data  )  ; 
didAdd  (  size  ,  object  )  ; 
didChange  (  )  ; 
} 








@  Override 
public   void   addUnique  (  int   index  ,  E   object  )  { 
Object  [  ]  oldData  =  data  (  )  ; 
int   size  =  oldData  ==  null  ?  0  :  oldData  .  length  ; 
Object  [  ]  data  =  grow  (  size  +  1  )  ; 
E   validatedObject  =  validate  (  index  ,  object  )  ; 
if  (  index  !=  size  )  { 
System  .  arraycopy  (  oldData  ,  index  ,  data  ,  index  +  1  ,  size  -  index  )  ; 
} 
assign  (  data  ,  index  ,  validatedObject  )  ; 
setData  (  data  )  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
} 








@  Override 
public   boolean   addAllUnique  (  Collection  <  ?  extends   E  >  collection  )  { 
int   growth  =  collection  .  size  (  )  ; 
if  (  growth  !=  0  )  { 
int   oldSize  =  size  (  )  ; 
int   size  =  oldSize  +  growth  ; 
Object  [  ]  data  =  grow  (  size  )  ; 
Iterator  <  ?  extends   E  >  objects  =  collection  .  iterator  (  )  ; 
for  (  int   i  =  oldSize  ;  i  <  size  ;  ++  i  )  { 
E   object  =  objects  .  next  (  )  ; 
assign  (  data  ,  i  ,  validate  (  i  ,  object  )  )  ; 
} 
setData  (  data  )  ; 
for  (  int   i  =  oldSize  ;  i  <  size  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  data  [  i  ]  ; 
didAdd  (  i  ,  object  )  ; 
didChange  (  )  ; 
} 
return   true  ; 
}  else  { 
++  modCount  ; 
return   false  ; 
} 
} 











@  Override 
public   boolean   addAllUnique  (  int   index  ,  Collection  <  ?  extends   E  >  collection  )  { 
int   growth  =  collection  .  size  (  )  ; 
if  (  growth  !=  0  )  { 
Object  [  ]  oldData  =  data  (  )  ; 
int   oldSize  =  oldData  ==  null  ?  0  :  oldData  .  length  ; 
int   size  =  oldSize  +  growth  ; 
Object  [  ]  data  =  grow  (  size  )  ; 
int   shifted  =  oldSize  -  index  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  oldData  ,  index  ,  data  ,  index  +  growth  ,  shifted  )  ; 
} 
Iterator  <  ?  extends   E  >  objects  =  collection  .  iterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  growth  ;  ++  i  )  { 
E   object  =  objects  .  next  (  )  ; 
int   currentIndex  =  index  +  i  ; 
assign  (  data  ,  currentIndex  ,  validate  (  currentIndex  ,  object  )  )  ; 
} 
setData  (  data  )  ; 
for  (  int   i  =  0  ;  i  <  growth  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  data  [  index  ]  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
index  ++  ; 
} 
return   true  ; 
}  else  { 
++  modCount  ; 
return   false  ; 
} 
} 












@  Override 
public   boolean   addAllUnique  (  Object  [  ]  objects  ,  int   start  ,  int   end  )  { 
int   growth  =  end  -  start  ; 
if  (  growth  >  0  )  { 
int   oldSize  =  size  (  )  ; 
int   size  =  oldSize  +  growth  ; 
Object  [  ]  data  =  grow  (  size  )  ; 
for  (  int   i  =  start  ,  index  =  size  ;  i  <  end  ;  ++  i  ,  ++  index  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
assign  (  data  ,  index  ,  validate  (  index  ,  object  )  )  ; 
} 
setData  (  data  )  ; 
for  (  int   i  =  start  ,  index  =  size  ;  i  <  end  ;  ++  i  ,  ++  index  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
didAdd  (  index  ,  object  )  ; 
didChange  (  )  ; 
} 
return   true  ; 
}  else  { 
++  modCount  ; 
return   false  ; 
} 
} 













@  Override 
public   boolean   addAllUnique  (  int   index  ,  Object  [  ]  objects  ,  int   start  ,  int   end  )  { 
int   growth  =  end  -  start  ; 
if  (  growth  >  0  )  { 
Object  [  ]  oldData  =  data  (  )  ; 
int   oldSize  =  oldData  ==  null  ?  0  :  oldData  .  length  ; 
int   size  =  oldSize  +  growth  ; 
Object  [  ]  data  =  grow  (  size  )  ; 
int   shifted  =  oldSize  -  index  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  oldData  ,  index  ,  data  ,  index  +  growth  ,  shifted  )  ; 
} 
for  (  int   i  =  start  ,  currentIndex  =  index  ;  i  <  end  ;  ++  i  ,  ++  currentIndex  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
assign  (  data  ,  currentIndex  ,  validate  (  currentIndex  ,  object  )  )  ; 
} 
setData  (  data  )  ; 
for  (  int   i  =  start  ,  currentIndex  =  index  ;  i  <  end  ;  ++  i  ,  ++  currentIndex  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objects  [  i  ]  ; 
didAdd  (  currentIndex  ,  object  )  ; 
didChange  (  )  ; 
} 
return   true  ; 
}  else  { 
++  modCount  ; 
return   false  ; 
} 
} 






@  SuppressWarnings  (  "null"  ) 
@  Override 
public   boolean   removeAll  (  Collection  <  ?  >  collection  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
boolean   modified  =  false  ; 
for  (  int   i  =  size  ;  --  i  >=  0  ;  )  { 
if  (  collection  .  contains  (  data  [  i  ]  )  )  { 
remove  (  i  )  ; 
modified  =  true  ; 
} 
} 
return   modified  ; 
} 








@  SuppressWarnings  (  "null"  ) 
@  Override 
public   E   remove  (  int   index  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  index  >=  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
@  SuppressWarnings  (  "unchecked"  )  E   oldObject  =  (  E  )  data  [  index  ]  ; 
Object  [  ]  newData  =  newData  (  size  -  1  )  ; 
System  .  arraycopy  (  data  ,  0  ,  newData  ,  0  ,  index  )  ; 
int   shifted  =  size  -  index  -  1  ; 
if  (  shifted  >  0  )  { 
System  .  arraycopy  (  data  ,  index  +  1  ,  newData  ,  index  ,  shifted  )  ; 
} 
setData  (  newData  )  ; 
didRemove  (  index  ,  oldObject  )  ; 
didChange  (  )  ; 
return   oldObject  ; 
} 









@  SuppressWarnings  (  "null"  ) 
@  Override 
public   boolean   retainAll  (  Collection  <  ?  >  collection  )  { 
boolean   modified  =  false  ; 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
for  (  int   i  =  size  ;  --  i  >=  0  ;  )  { 
if  (  !  collection  .  contains  (  data  [  i  ]  )  )  { 
remove  (  i  )  ; 
modified  =  true  ; 
} 
} 
return   modified  ; 
} 






@  Override 
public   void   clear  (  )  { 
++  modCount  ; 
Object  [  ]  oldData  =  data  (  )  ; 
int   oldSize  =  oldData  ==  null  ?  0  :  oldData  .  length  ; 
setData  (  null  )  ; 
didClear  (  oldSize  ,  oldData  )  ; 
didChange  (  )  ; 
} 










@  SuppressWarnings  (  "null"  ) 
@  Override 
public   E   move  (  int   targetIndex  ,  int   sourceIndex  )  { 
Object  [  ]  data  =  copy  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  targetIndex  >=  size  )  throw   new   IndexOutOfBoundsException  (  "targetIndex="  +  targetIndex  +  ", size="  +  size  )  ; 
if  (  sourceIndex  >=  size  )  throw   new   IndexOutOfBoundsException  (  "sourceIndex="  +  sourceIndex  +  ", size="  +  size  )  ; 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  data  [  sourceIndex  ]  ; 
if  (  targetIndex  !=  sourceIndex  )  { 
if  (  targetIndex  <  sourceIndex  )  { 
System  .  arraycopy  (  data  ,  targetIndex  ,  data  ,  targetIndex  +  1  ,  sourceIndex  -  targetIndex  )  ; 
}  else  { 
System  .  arraycopy  (  data  ,  sourceIndex  +  1  ,  data  ,  sourceIndex  ,  targetIndex  -  sourceIndex  )  ; 
} 
assign  (  data  ,  targetIndex  ,  object  )  ; 
setData  (  data  )  ; 
didMove  (  targetIndex  ,  object  ,  sourceIndex  )  ; 
didChange  (  )  ; 
} 
return   object  ; 
} 




protected   Object  [  ]  grow  (  int   size  )  { 
Object  [  ]  oldData  =  data  (  )  ; 
Object  [  ]  data  =  newData  (  size  )  ; 
if  (  oldData  !=  null  )  { 
System  .  arraycopy  (  oldData  ,  0  ,  data  ,  0  ,  oldData  .  length  )  ; 
} 
return   data  ; 
} 

private   static   Object  [  ]  EMPTY_ARRAY  =  new   Object  [  0  ]  ; 




protected   Object  [  ]  copy  (  )  { 
Object  [  ]  data  =  data  (  )  ; 
if  (  data  !=  null  )  { 
Object  [  ]  newData  =  newData  (  data  .  length  )  ; 
System  .  arraycopy  (  data  ,  0  ,  newData  ,  0  ,  data  .  length  )  ; 
return   newData  ; 
}  else  { 
return   EMPTY_ARRAY  ; 
} 
} 

private   synchronized   void   writeObject  (  ObjectOutputStream   objectOutputStream  )  throws   IOException  { 
objectOutputStream  .  defaultWriteObject  (  )  ; 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  data  ==  null  )  { 
objectOutputStream  .  writeInt  (  0  )  ; 
}  else  { 
objectOutputStream  .  writeInt  (  size  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
objectOutputStream  .  writeObject  (  data  [  i  ]  )  ; 
} 
} 
} 

private   synchronized   void   readObject  (  ObjectInputStream   objectInputStream  )  throws   IOException  ,  ClassNotFoundException  { 
objectInputStream  .  defaultReadObject  (  )  ; 
int   size  =  objectInputStream  .  readInt  (  )  ; 
Object  [  ]  data  ; 
if  (  size  >  0  )  { 
try  { 
data  =  newData  (  size  )  ; 
}  catch  (  Throwable   exception  )  { 
data  =  new   Object  [  size  ]  ; 
} 
setData  (  data  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  ++  i  )  { 
@  SuppressWarnings  (  "unchecked"  )  E   object  =  (  E  )  objectInputStream  .  readObject  (  )  ; 
didAdd  (  i  ,  assign  (  data  ,  i  ,  object  )  )  ; 
} 
} 
} 





@  Override 
public   Object   clone  (  )  { 
try  { 
@  SuppressWarnings  (  "unchecked"  )  ArrayDelegatingEList  <  E  >  clone  =  (  ArrayDelegatingEList  <  E  >  )  super  .  clone  (  )  ; 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  size  >  0  )  { 
Object  [  ]  newData  =  newData  (  size  )  ; 
clone  .  setData  (  newData  )  ; 
System  .  arraycopy  (  data  ,  0  ,  newData  ,  0  ,  size  )  ; 
} 
return   clone  ; 
}  catch  (  CloneNotSupportedException   exception  )  { 
throw   new   InternalError  (  )  ; 
} 
} 







@  Override 
public   Iterator  <  E  >  iterator  (  )  { 
return   new   EIterator  <  E  >  (  )  ; 
} 




protected   class   EIterator  <  E1  >  extends   AbstractEList  <  E  >  .  EIterator  <  E1  >  { 




protected   Object  [  ]  expectedData  =  data  (  )  ; 





@  Override 
protected   void   checkModCount  (  )  { 
if  (  modCount  !=  expectedModCount  ||  data  (  )  !=  expectedData  )  { 
throw   new   ConcurrentModificationException  (  )  ; 
} 
} 
} 






@  Override 
protected   Iterator  <  E  >  basicIterator  (  )  { 
return   new   NonResolvingEIterator  <  E  >  (  )  ; 
} 




protected   class   NonResolvingEIterator  <  E1  >  extends   AbstractEList  <  E  >  .  NonResolvingEIterator  <  E1  >  { 




protected   Object  [  ]  expectedData  =  data  (  )  ; 





@  Override 
protected   void   checkModCount  (  )  { 
if  (  modCount  !=  expectedModCount  ||  data  (  )  !=  expectedData  )  { 
throw   new   ConcurrentModificationException  (  )  ; 
} 
} 
} 







@  Override 
public   ListIterator  <  E  >  listIterator  (  )  { 
return   new   EListIterator  <  E  >  (  )  ; 
} 









@  Override 
public   ListIterator  <  E  >  listIterator  (  int   index  )  { 
int   size  =  size  (  )  ; 
if  (  index  <  0  ||  index  >  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return   new   EListIterator  <  E  >  (  index  )  ; 
} 




protected   class   EListIterator  <  E1  >  extends   AbstractEList  <  E  >  .  EListIterator  <  E1  >  { 




protected   Object  [  ]  expectedData  =  data  (  )  ; 




public   EListIterator  (  )  { 
super  (  )  ; 
} 





public   EListIterator  (  int   index  )  { 
super  (  index  )  ; 
} 





@  Override 
protected   void   checkModCount  (  )  { 
if  (  modCount  !=  expectedModCount  ||  data  (  )  !=  expectedData  )  { 
throw   new   ConcurrentModificationException  (  )  ; 
} 
} 
} 






@  Override 
protected   ListIterator  <  E  >  basicListIterator  (  )  { 
return   new   NonResolvingEListIterator  <  E  >  (  )  ; 
} 








@  Override 
protected   ListIterator  <  E  >  basicListIterator  (  int   index  )  { 
int   size  =  size  (  )  ; 
if  (  index  <  0  ||  index  >  size  )  throw   new   BasicIndexOutOfBoundsException  (  index  ,  size  )  ; 
return   new   NonResolvingEListIterator  <  E  >  (  index  )  ; 
} 




protected   class   NonResolvingEListIterator  <  E1  >  extends   AbstractEList  <  E  >  .  NonResolvingEListIterator  <  E1  >  { 




protected   Object  [  ]  expectedData  =  data  (  )  ; 




public   NonResolvingEListIterator  (  )  { 
super  (  )  ; 
} 





public   NonResolvingEListIterator  (  int   index  )  { 
super  (  index  )  ; 
} 





@  Override 
protected   void   checkModCount  (  )  { 
if  (  modCount  !=  expectedModCount  ||  data  (  )  !=  expectedData  )  { 
throw   new   ConcurrentModificationException  (  )  ; 
} 
} 
} 





@  Override 
protected   List  <  E  >  basicList  (  )  { 
Object  [  ]  data  =  data  (  )  ; 
int   size  =  data  ==  null  ?  0  :  data  .  length  ; 
if  (  size  ==  0  )  { 
return   ECollections  .  emptyEList  (  )  ; 
}  else  { 
return   new   BasicEList  .  UnmodifiableEList  <  E  >  (  size  ,  data  )  ; 
} 
} 
} 

