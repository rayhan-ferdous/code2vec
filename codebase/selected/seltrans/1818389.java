package   deduced  ; 

import   java  .  util  .  *  ; 






class   PropertyCollectionAsPropertyList   implements   List  { 


private   PropertyCollection   _collection  ; 






public   PropertyCollectionAsPropertyList  (  PropertyCollection   collection  )  { 
_collection  =  collection  ; 
} 






public   int   size  (  )  { 
return   _collection  .  getSize  (  )  ; 
} 






public   void   clear  (  )  { 
_collection  .  clear  (  )  ; 
} 






public   boolean   isEmpty  (  )  { 
return   _collection  .  isEmpty  (  )  ; 
} 






public   Object  [  ]  toArray  (  )  { 
Object  [  ]  retVal  =  new   Object  [  size  (  )  ]  ; 
Iterator   it  =  _collection  .  iterator  (  )  ; 
int   i  =  0  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   element  =  it  .  next  (  )  ; 
retVal  [  i  ]  =  element  ; 
++  i  ; 
} 
return   retVal  ; 
} 






public   Object   get  (  int   index  )  { 
return   _collection  .  getProperty  (  _collection  .  getKeyList  (  )  .  toArray  (  )  [  index  ]  )  ; 
} 






public   Object   remove  (  int   index  )  { 
return   _collection  .  removeProperty  (  _collection  .  getKeyList  (  )  .  toArray  (  )  [  index  ]  )  ; 
} 






public   void   add  (  int   index  ,  Object   element  )  { 
_collection  .  addProperty  (  new   Integer  (  index  )  ,  (  Property  )  element  )  ; 
} 






public   int   indexOf  (  Object   o  )  { 
if  (  o  ==  null  )  { 
return   DeducedConstant  .  INDEX_NOT_FOUND  ; 
} 
int   index  =  0  ; 
Iterator   it  =  _collection  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   element  =  it  .  next  (  )  ; 
if  (  o  .  equals  (  element  )  )  { 
return   index  ; 
} 
++  index  ; 
} 
return   DeducedConstant  .  INDEX_NOT_FOUND  ; 
} 






public   int   lastIndexOf  (  Object   o  )  { 
if  (  o  ==  null  )  { 
return   DeducedConstant  .  INDEX_NOT_FOUND  ; 
} 
int   index  =  0  ; 
int   retVal  =  DeducedConstant  .  INDEX_NOT_FOUND  ; 
Iterator   it  =  _collection  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   element  =  it  .  next  (  )  ; 
if  (  o  .  equals  (  element  )  )  { 
retVal  =  index  ; 
} 
++  index  ; 
} 
return   retVal  ; 
} 






public   boolean   add  (  Object   o  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   boolean   contains  (  Object   o  )  { 
return   indexOf  (  o  )  !=  DeducedConstant  .  INDEX_NOT_FOUND  ; 
} 






public   boolean   remove  (  Object   o  )  { 
return   remove  (  indexOf  (  o  )  )  !=  null  ; 
} 






public   boolean   addAll  (  int   index  ,  Collection   c  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   boolean   addAll  (  Collection   c  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   boolean   containsAll  (  Collection   c  )  { 
Iterator   it  =  c  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   element  =  it  .  next  (  )  ; 
if  (  !  contains  (  element  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 






public   boolean   removeAll  (  Collection   c  )  { 
Iterator   it  =  c  .  iterator  (  )  ; 
boolean   retVal  =  false  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   element  =  it  .  next  (  )  ; 
if  (  remove  (  element  )  )  { 
retVal  =  true  ; 
} 
} 
return   retVal  ; 
} 






public   boolean   retainAll  (  Collection   c  )  { 
boolean   modified  =  false  ; 
Iterator   e  =  iterator  (  )  ; 
while  (  e  .  hasNext  (  )  )  { 
if  (  !  c  .  contains  (  e  .  next  (  )  )  )  { 
e  .  remove  (  )  ; 
modified  =  true  ; 
} 
} 
return   modified  ; 
} 






public   Iterator   iterator  (  )  { 
return   _collection  .  iterator  (  )  ; 
} 






public   List   subList  (  int   fromIndex  ,  int   toIndex  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   ListIterator   listIterator  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   ListIterator   listIterator  (  int   index  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 






public   Object   set  (  int   index  ,  Object   element  )  { 
Property   p  =  (  Property  )  element  ; 
return   _collection  .  setProperty  (  _collection  .  getKeyList  (  )  .  toArray  (  )  [  index  ]  ,  p  .  getValue  (  )  )  ; 
} 






public   Object  [  ]  toArray  (  Object  [  ]  a  )  { 
int   size  =  size  (  )  ; 
if  (  a  .  length  <  size  )  { 
a  =  (  Object  [  ]  )  java  .  lang  .  reflect  .  Array  .  newInstance  (  a  .  getClass  (  )  .  getComponentType  (  )  ,  size  )  ; 
} 
System  .  arraycopy  (  toArray  (  )  ,  0  ,  a  ,  0  ,  size  )  ; 
if  (  a  .  length  >  size  )  { 
a  [  size  ]  =  null  ; 
} 
return   a  ; 
} 
} 

