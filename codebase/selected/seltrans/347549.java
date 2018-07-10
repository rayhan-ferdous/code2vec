package   game  .  engine  ; 

import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Comparator  ; 










public   final   class   GameObjectCollection  { 




public   String   gameObjectCollectionName  ; 





public   GameObject  [  ]  gameObjects  ; 




public   int   size  ; 






public   enum   SortOrder  { 

X  ,  Y 
} 

private   SortOrder   sortOrder  =  SortOrder  .  X  ; 





public   GameObjectCollection  (  String   gameObjectCollectionName  )  { 
this  (  gameObjectCollectionName  ,  10  )  ; 
} 










public   GameObjectCollection  (  String   gameObjectCollectionName  ,  int   initialCapacity  )  { 
if  (  initialCapacity  <  0  )  { 
throw   new   IllegalArgumentException  (  "Illegal Capacity: "  +  initialCapacity  )  ; 
} 
this  .  gameObjectCollectionName  =  gameObjectCollectionName  ; 
gameObjects  =  new   GameObject  [  initialCapacity  ]  ; 
} 






public   void   trimToSize  (  )  { 
int   oldCapacity  =  gameObjects  .  length  ; 
if  (  size  <  oldCapacity  )  { 
gameObjects  =  Arrays  .  copyOf  (  gameObjects  ,  size  )  ; 
} 
} 









public   void   ensureCapacity  (  int   minCapacity  )  { 
int   oldCapacity  =  gameObjects  .  length  ; 
if  (  minCapacity  >=  oldCapacity  )  { 
int   newCapacity  =  (  oldCapacity  *  3  )  /  2  +  1  ; 
if  (  newCapacity  <  minCapacity  )  { 
newCapacity  =  minCapacity  ; 
} 
gameObjects  =  Arrays  .  copyOf  (  gameObjects  ,  newCapacity  )  ; 
} 
} 






public   int   size  (  )  { 
return   size  ; 
} 






public   boolean   isEmpty  (  )  { 
return   size  ==  0  ; 
} 











public   boolean   contains  (  Object   o  )  { 
return   indexOf  (  o  )  >=  0  ; 
} 








public   int   indexOf  (  Object   o  )  { 
if  (  o  ==  null  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
if  (  gameObjects  [  i  ]  ==  null  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
if  (  o  .  equals  (  gameObjects  [  i  ]  )  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 








public   int   lastIndexOf  (  Object   o  )  { 
if  (  o  ==  null  )  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  i  --  )  { 
if  (  gameObjects  [  i  ]  ==  null  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  size  -  1  ;  i  >=  0  ;  i  --  )  { 
if  (  o  .  equals  (  gameObjects  [  i  ]  )  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 
















public   Object  [  ]  toArray  (  )  { 
return   Arrays  .  copyOf  (  gameObjects  ,  size  )  ; 
} 










public   GameObject   get  (  int   index  )  { 
return   gameObjects  [  index  ]  ; 
} 













public   GameObject   set  (  int   index  ,  GameObject   gameObject  )  { 
GameObject   oldValue  =  gameObjects  [  index  ]  ; 
gameObjects  [  index  ]  =  gameObject  ; 
return   oldValue  ; 
} 








public   boolean   add  (  GameObject   e  )  { 
ensureCapacity  (  size  +  1  )  ; 
gameObjects  [  size  ++  ]  =  e  ; 
return   true  ; 
} 













public   void   add  (  int   index  ,  GameObject   element  )  { 
if  (  index  >  size  ||  index  <  0  )  { 
throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
} 
ensureCapacity  (  size  +  1  )  ; 
System  .  arraycopy  (  gameObjects  ,  index  ,  gameObjects  ,  index  +  1  ,  size  -  index  )  ; 
gameObjects  [  index  ]  =  element  ; 
size  ++  ; 
} 











public   GameObject   remove  (  int   index  )  { 
GameObject   oldValue  =  gameObjects  [  index  ]  ; 
int   numMoved  =  size  -  index  -  1  ; 
if  (  numMoved  >  0  )  { 
System  .  arraycopy  (  gameObjects  ,  index  +  1  ,  gameObjects  ,  index  ,  numMoved  )  ; 
} 
gameObjects  [  --  size  ]  =  null  ; 
return   oldValue  ; 
} 















public   boolean   remove  (  Object   o  )  { 
if  (  o  ==  null  )  { 
for  (  int   index  =  0  ;  index  <  size  ;  index  ++  )  { 
if  (  gameObjects  [  index  ]  ==  null  )  { 
fastRemove  (  index  )  ; 
return   true  ; 
} 
} 
}  else  { 
for  (  int   index  =  0  ;  index  <  size  ;  index  ++  )  { 
if  (  o  .  equals  (  gameObjects  [  index  ]  )  )  { 
fastRemove  (  index  )  ; 
return   true  ; 
} 
} 
} 
return   false  ; 
} 

private   void   fastRemove  (  int   index  )  { 
int   numMoved  =  size  -  index  -  1  ; 
if  (  numMoved  >  0  )  { 
System  .  arraycopy  (  gameObjects  ,  index  +  1  ,  gameObjects  ,  index  ,  numMoved  )  ; 
} 
gameObjects  [  --  size  ]  =  null  ; 
} 





public   void   clear  (  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
gameObjects  [  i  ]  =  null  ; 
} 
size  =  0  ; 
} 















public   boolean   addAll  (  Collection  <  GameObject  >  c  )  { 
Object  [  ]  a  =  c  .  toArray  (  )  ; 
int   numNew  =  a  .  length  ; 
ensureCapacity  (  size  +  numNew  )  ; 
System  .  arraycopy  (  a  ,  0  ,  gameObjects  ,  size  ,  numNew  )  ; 
size  +=  numNew  ; 
return   numNew  !=  0  ; 
} 



















public   boolean   addAll  (  int   index  ,  Collection  <  GameObject  >  c  )  { 
if  (  index  >  size  ||  index  <  0  )  { 
throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
} 
Object  [  ]  a  =  c  .  toArray  (  )  ; 
int   numNew  =  a  .  length  ; 
ensureCapacity  (  size  +  numNew  )  ; 
int   numMoved  =  size  -  index  ; 
if  (  numMoved  >  0  )  { 
System  .  arraycopy  (  gameObjects  ,  index  ,  gameObjects  ,  index  +  numNew  ,  numMoved  )  ; 
} 
System  .  arraycopy  (  a  ,  0  ,  gameObjects  ,  index  ,  numNew  )  ; 
size  +=  numNew  ; 
return   numNew  !=  0  ; 
} 

















protected   void   removeRange  (  int   fromIndex  ,  int   toIndex  )  { 
int   numMoved  =  size  -  toIndex  ; 
System  .  arraycopy  (  gameObjects  ,  toIndex  ,  gameObjects  ,  fromIndex  ,  numMoved  )  ; 
int   newSize  =  size  -  (  toIndex  -  fromIndex  )  ; 
while  (  size  !=  newSize  )  { 
gameObjects  [  --  size  ]  =  null  ; 
} 
} 







public   void   setSortOrder  (  SortOrder   sortOrder  )  { 
this  .  sortOrder  =  sortOrder  ; 
} 






public   SortOrder   getSortOrder  (  )  { 
return   sortOrder  ; 
} 











public   void   sortGameObjects  (  )  { 
for  (  int   idx  =  size  ;  idx  <  gameObjects  .  length  ;  idx  ++  )  { 
gameObjects  [  idx  ]  =  null  ; 
} 
Comparator  <  GameObject  >  comparator  ; 
if  (  sortOrder  ==  SortOrder  .  X  )  { 
comparator  =  new   Comparator  <  GameObject  >  (  )  { 

public   int   compare  (  GameObject   gameObject1  ,  GameObject   gameObject2  )  { 
if  (  gameObject1  ==  null  ||  gameObject2  ==  null  )  { 
if  (  gameObject1  !=  null  )  { 
return  -  1  ; 
}  else   if  (  gameObject2  !=  null  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else  { 
if  (  gameObject1  .  x  <  gameObject2  .  x  )  { 
return  -  1  ; 
}  else   if  (  gameObject1  .  x  >  gameObject2  .  x  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
} 
} 
}  ; 
}  else  { 
comparator  =  new   Comparator  <  GameObject  >  (  )  { 

public   int   compare  (  GameObject   gameObject1  ,  GameObject   gameObject2  )  { 
if  (  gameObject1  ==  null  ||  gameObject2  ==  null  )  { 
if  (  gameObject1  !=  null  )  { 
return  -  1  ; 
}  else   if  (  gameObject2  !=  null  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else  { 
if  (  gameObject1  .  y  <  gameObject2  .  y  )  { 
return  -  1  ; 
}  else   if  (  gameObject1  .  y  >  gameObject2  .  y  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
} 
} 
}  ; 
} 
Arrays  .  sort  (  gameObjects  ,  comparator  )  ; 
} 












public   void   incrementalSortGameObjects  (  )  { 
if  (  sortOrder  ==  SortOrder  .  X  )  { 
incrementalSortGameObjectsOnXPosition  (  )  ; 
}  else  { 
incrementalSortGameObjectsOnYPosition  (  )  ; 
} 
} 




private   void   incrementalSortGameObjectsOnXPosition  (  )  { 
GameObject   tempGameObject  ; 
for  (  int   i  =  0  ;  i  <  size  -  1  ;  i  ++  )  { 
if  (  gameObjects  [  i  ]  .  x  -  gameObjects  [  i  ]  .  boundingDimension  /  2  >  gameObjects  [  i  +  1  ]  .  x  -  gameObjects  [  i  +  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  i  ]  ; 
gameObjects  [  i  ]  =  gameObjects  [  i  +  1  ]  ; 
gameObjects  [  i  +  1  ]  =  tempGameObject  ; 
int   j  =  i  ; 
while  (  j  >  0  &&  gameObjects  [  j  ]  .  x  -  gameObjects  [  j  ]  .  boundingDimension  /  2  <  gameObjects  [  j  -  1  ]  .  x  -  gameObjects  [  j  -  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  j  ]  ; 
gameObjects  [  j  ]  =  gameObjects  [  j  -  1  ]  ; 
gameObjects  [  j  -  1  ]  =  tempGameObject  ; 
j  --  ; 
} 
j  =  i  +  1  ; 
while  (  j  <  size  -  1  &&  gameObjects  [  j  ]  .  x  -  gameObjects  [  j  ]  .  boundingDimension  /  2  >  gameObjects  [  j  +  1  ]  .  x  -  gameObjects  [  j  +  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  j  ]  ; 
gameObjects  [  j  ]  =  gameObjects  [  j  +  1  ]  ; 
gameObjects  [  j  +  1  ]  =  tempGameObject  ; 
j  ++  ; 
} 
} 
} 
} 




private   void   incrementalSortGameObjectsOnYPosition  (  )  { 
GameObject   tempGameObject  ; 
for  (  int   i  =  0  ;  i  <  size  -  1  ;  i  ++  )  { 
if  (  gameObjects  [  i  ]  .  y  -  gameObjects  [  i  ]  .  boundingDimension  /  2  >  gameObjects  [  i  +  1  ]  .  y  -  gameObjects  [  i  +  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  i  ]  ; 
gameObjects  [  i  ]  =  gameObjects  [  i  +  1  ]  ; 
gameObjects  [  i  +  1  ]  =  tempGameObject  ; 
int   j  =  i  ; 
while  (  j  >  0  &&  gameObjects  [  j  ]  .  y  -  gameObjects  [  j  ]  .  boundingDimension  /  2  <  gameObjects  [  j  -  1  ]  .  y  -  gameObjects  [  j  -  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  j  ]  ; 
gameObjects  [  j  ]  =  gameObjects  [  j  -  1  ]  ; 
gameObjects  [  j  -  1  ]  =  tempGameObject  ; 
j  --  ; 
} 
j  =  i  +  1  ; 
while  (  j  <  size  -  1  &&  gameObjects  [  j  ]  .  y  -  gameObjects  [  j  ]  .  boundingDimension  /  2  >  gameObjects  [  j  +  1  ]  .  y  -  gameObjects  [  j  +  1  ]  .  boundingDimension  /  2  )  { 
tempGameObject  =  gameObjects  [  j  ]  ; 
gameObjects  [  j  ]  =  gameObjects  [  j  +  1  ]  ; 
gameObjects  [  j  +  1  ]  =  tempGameObject  ; 
j  ++  ; 
} 
} 
} 
} 
} 

