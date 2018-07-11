package   cern  .  colt  .  list  ; 

import   cern  .  colt  .  function  .  FloatComparator  ; 
import   cern  .  colt  .  function  .  FloatProcedure  ; 





public   abstract   class   AbstractFloatList   extends   AbstractList  { 







protected   int   size  ; 




protected   AbstractFloatList  (  )  { 
} 






public   void   add  (  float   element  )  { 
beforeInsert  (  size  ,  element  )  ; 
} 









public   void   addAllOfFromTo  (  AbstractFloatList   other  ,  int   from  ,  int   to  )  { 
beforeInsertAllOfFromTo  (  size  ,  other  ,  from  ,  to  )  ; 
} 










public   void   beforeInsert  (  int   index  ,  float   element  )  { 
beforeInsertDummies  (  index  ,  1  )  ; 
set  (  index  ,  element  )  ; 
} 













public   void   beforeInsertAllOfFromTo  (  int   index  ,  AbstractFloatList   other  ,  int   from  ,  int   to  )  { 
int   length  =  to  -  from  +  1  ; 
this  .  beforeInsertDummies  (  index  ,  length  )  ; 
this  .  replaceFromToWithFrom  (  index  ,  index  +  length  -  1  ,  other  ,  from  )  ; 
} 











protected   void   beforeInsertDummies  (  int   index  ,  int   length  )  { 
if  (  index  >  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
if  (  length  >  0  )  { 
ensureCapacity  (  size  +  length  )  ; 
setSizeRaw  (  size  +  length  )  ; 
replaceFromToWithFrom  (  index  +  length  ,  size  -  1  ,  this  ,  index  )  ; 
} 
} 





















public   int   binarySearch  (  float   key  )  { 
return   this  .  binarySearchFromTo  (  key  ,  0  ,  size  -  1  )  ; 
} 























public   int   binarySearchFromTo  (  float   key  ,  int   from  ,  int   to  )  { 
int   low  =  from  ; 
int   high  =  to  ; 
while  (  low  <=  high  )  { 
int   mid  =  (  low  +  high  )  /  2  ; 
float   midVal  =  get  (  mid  )  ; 
if  (  midVal  <  key  )  low  =  mid  +  1  ;  else   if  (  midVal  >  key  )  high  =  mid  -  1  ;  else   return   mid  ; 
} 
return  -  (  low  +  1  )  ; 
} 






public   Object   clone  (  )  { 
return   partFromTo  (  0  ,  size  -  1  )  ; 
} 






public   boolean   contains  (  float   elem  )  { 
return   indexOfFromTo  (  elem  ,  0  ,  size  -  1  )  >=  0  ; 
} 







public   void   delete  (  float   element  )  { 
int   index  =  indexOfFromTo  (  element  ,  0  ,  size  -  1  )  ; 
if  (  index  >=  0  )  remove  (  index  )  ; 
} 









public   float  [  ]  elements  (  )  { 
float  [  ]  myElements  =  new   float  [  size  ]  ; 
for  (  int   i  =  size  ;  --  i  >=  0  ;  )  myElements  [  i  ]  =  getQuick  (  i  )  ; 
return   myElements  ; 
} 










public   AbstractFloatList   elements  (  float  [  ]  elements  )  { 
clear  (  )  ; 
addAllOfFromTo  (  new   FloatArrayList  (  elements  )  ,  0  ,  elements  .  length  -  1  )  ; 
return   this  ; 
} 







public   abstract   void   ensureCapacity  (  int   minCapacity  )  ; 











public   boolean   equals  (  Object   otherObj  )  { 
if  (  !  (  otherObj   instanceof   AbstractFloatList  )  )  { 
return   false  ; 
} 
if  (  this  ==  otherObj  )  return   true  ; 
if  (  otherObj  ==  null  )  return   false  ; 
AbstractFloatList   other  =  (  AbstractFloatList  )  otherObj  ; 
if  (  size  (  )  !=  other  .  size  (  )  )  return   false  ; 
for  (  int   i  =  size  (  )  ;  --  i  >=  0  ;  )  { 
if  (  getQuick  (  i  )  !=  other  .  getQuick  (  i  )  )  return   false  ; 
} 
return   true  ; 
} 








public   void   fillFromToWith  (  int   from  ,  int   to  ,  float   val  )  { 
checkRangeFromTo  (  from  ,  to  ,  this  .  size  )  ; 
for  (  int   i  =  from  ;  i  <=  to  ;  )  setQuick  (  i  ++  ,  val  )  ; 
} 







public   boolean   forEach  (  FloatProcedure   procedure  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  )  if  (  !  procedure  .  apply  (  get  (  i  ++  )  )  )  return   false  ; 
return   true  ; 
} 








public   float   get  (  int   index  )  { 
if  (  index  >=  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
return   getQuick  (  index  )  ; 
} 












protected   abstract   float   getQuick  (  int   index  )  ; 








public   int   indexOf  (  float   element  )  { 
return   indexOfFromTo  (  element  ,  0  ,  size  -  1  )  ; 
} 













public   int   indexOfFromTo  (  float   element  ,  int   from  ,  int   to  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  )  ; 
for  (  int   i  =  from  ;  i  <=  to  ;  i  ++  )  { 
if  (  element  ==  getQuick  (  i  )  )  return   i  ; 
} 
return  -  1  ; 
} 








public   int   lastIndexOf  (  float   element  )  { 
return   lastIndexOfFromTo  (  element  ,  0  ,  size  -  1  )  ; 
} 













public   int   lastIndexOfFromTo  (  float   element  ,  int   from  ,  int   to  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  (  )  )  ; 
for  (  int   i  =  to  ;  i  >=  from  ;  i  --  )  { 
if  (  element  ==  getQuick  (  i  )  )  return   i  ; 
} 
return  -  1  ; 
} 

















public   void   mergeSortFromTo  (  int   from  ,  int   to  )  { 
int   mySize  =  size  (  )  ; 
checkRangeFromTo  (  from  ,  to  ,  mySize  )  ; 
float  [  ]  myElements  =  elements  (  )  ; 
cern  .  colt  .  Sorting  .  mergeSort  (  myElements  ,  from  ,  to  +  1  )  ; 
elements  (  myElements  )  ; 
setSizeRaw  (  mySize  )  ; 
} 






























public   void   mergeSortFromTo  (  int   from  ,  int   to  ,  FloatComparator   c  )  { 
int   mySize  =  size  (  )  ; 
checkRangeFromTo  (  from  ,  to  ,  mySize  )  ; 
float  [  ]  myElements  =  elements  (  )  ; 
cern  .  colt  .  Sorting  .  mergeSort  (  myElements  ,  from  ,  to  +  1  ,  c  )  ; 
elements  (  myElements  )  ; 
setSizeRaw  (  mySize  )  ; 
} 








public   AbstractFloatList   partFromTo  (  int   from  ,  int   to  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  )  ; 
int   length  =  to  -  from  +  1  ; 
FloatArrayList   part  =  new   FloatArrayList  (  length  )  ; 
part  .  addAllOfFromTo  (  this  ,  from  ,  to  )  ; 
return   part  ; 
} 

















public   void   quickSortFromTo  (  int   from  ,  int   to  )  { 
int   mySize  =  size  (  )  ; 
checkRangeFromTo  (  from  ,  to  ,  mySize  )  ; 
float  [  ]  myElements  =  elements  (  )  ; 
java  .  util  .  Arrays  .  sort  (  myElements  ,  from  ,  to  +  1  )  ; 
elements  (  myElements  )  ; 
setSizeRaw  (  mySize  )  ; 
} 




























public   void   quickSortFromTo  (  int   from  ,  int   to  ,  FloatComparator   c  )  { 
int   mySize  =  size  (  )  ; 
checkRangeFromTo  (  from  ,  to  ,  mySize  )  ; 
float  [  ]  myElements  =  elements  (  )  ; 
cern  .  colt  .  Sorting  .  quickSort  (  myElements  ,  from  ,  to  +  1  ,  c  )  ; 
elements  (  myElements  )  ; 
setSizeRaw  (  mySize  )  ; 
} 








public   boolean   removeAll  (  AbstractFloatList   other  )  { 
if  (  other  .  size  (  )  ==  0  )  return   false  ; 
int   limit  =  other  .  size  (  )  -  1  ; 
int   j  =  0  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
if  (  other  .  indexOfFromTo  (  getQuick  (  i  )  ,  0  ,  limit  )  <  0  )  setQuick  (  j  ++  ,  getQuick  (  i  )  )  ; 
} 
boolean   modified  =  (  j  !=  size  )  ; 
setSize  (  j  )  ; 
return   modified  ; 
} 











public   void   removeFromTo  (  int   from  ,  int   to  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  )  ; 
int   numMoved  =  size  -  to  -  1  ; 
if  (  numMoved  >  0  )  { 
replaceFromToWithFrom  (  from  ,  from  -  1  +  numMoved  ,  this  ,  to  +  1  )  ; 
} 
int   width  =  to  -  from  +  1  ; 
if  (  width  >  0  )  setSizeRaw  (  size  -  width  )  ; 
} 











public   void   replaceFromToWithFrom  (  int   from  ,  int   to  ,  AbstractFloatList   other  ,  int   otherFrom  )  { 
int   length  =  to  -  from  +  1  ; 
if  (  length  >  0  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  (  )  )  ; 
checkRangeFromTo  (  otherFrom  ,  otherFrom  +  length  -  1  ,  other  .  size  (  )  )  ; 
if  (  from  <=  otherFrom  )  { 
for  (  ;  --  length  >=  0  ;  )  setQuick  (  from  ++  ,  other  .  getQuick  (  otherFrom  ++  )  )  ; 
}  else  { 
int   otherTo  =  otherFrom  +  length  -  1  ; 
for  (  ;  --  length  >=  0  ;  )  setQuick  (  to  --  ,  other  .  getQuick  (  otherTo  --  )  )  ; 
} 
} 
} 












































public   void   replaceFromToWithFromTo  (  int   from  ,  int   to  ,  AbstractFloatList   other  ,  int   otherFrom  ,  int   otherTo  )  { 
if  (  otherFrom  >  otherTo  )  { 
throw   new   IndexOutOfBoundsException  (  "otherFrom: "  +  otherFrom  +  ", otherTo: "  +  otherTo  )  ; 
} 
if  (  this  ==  other  &&  to  -  from  !=  otherTo  -  otherFrom  )  { 
replaceFromToWithFromTo  (  from  ,  to  ,  partFromTo  (  otherFrom  ,  otherTo  )  ,  0  ,  otherTo  -  otherFrom  )  ; 
return  ; 
} 
int   length  =  otherTo  -  otherFrom  +  1  ; 
int   diff  =  length  ; 
int   theLast  =  from  -  1  ; 
if  (  to  >=  from  )  { 
diff  -=  (  to  -  from  +  1  )  ; 
theLast  =  to  ; 
} 
if  (  diff  >  0  )  { 
beforeInsertDummies  (  theLast  +  1  ,  diff  )  ; 
}  else  { 
if  (  diff  <  0  )  { 
removeFromTo  (  theLast  +  diff  ,  theLast  -  1  )  ; 
} 
} 
if  (  length  >  0  )  { 
replaceFromToWithFrom  (  from  ,  from  +  length  -  1  ,  other  ,  otherFrom  )  ; 
} 
} 










public   void   replaceFromWith  (  int   from  ,  java  .  util  .  Collection   other  )  { 
checkRange  (  from  ,  size  (  )  )  ; 
java  .  util  .  Iterator   e  =  other  .  iterator  (  )  ; 
int   index  =  from  ; 
int   limit  =  Math  .  min  (  size  (  )  -  from  ,  other  .  size  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  limit  ;  i  ++  )  set  (  index  ++  ,  (  (  Number  )  e  .  next  (  )  )  .  floatValue  (  )  )  ; 
} 








public   boolean   retainAll  (  AbstractFloatList   other  )  { 
if  (  other  .  size  (  )  ==  0  )  { 
if  (  size  ==  0  )  return   false  ; 
setSize  (  0  )  ; 
return   true  ; 
} 
int   limit  =  other  .  size  (  )  -  1  ; 
int   j  =  0  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
if  (  other  .  indexOfFromTo  (  getQuick  (  i  )  ,  0  ,  limit  )  >=  0  )  setQuick  (  j  ++  ,  getQuick  (  i  )  )  ; 
} 
boolean   modified  =  (  j  !=  size  )  ; 
setSize  (  j  )  ; 
return   modified  ; 
} 





public   void   reverse  (  )  { 
float   tmp  ; 
int   limit  =  size  (  )  /  2  ; 
int   j  =  size  (  )  -  1  ; 
for  (  int   i  =  0  ;  i  <  limit  ;  )  { 
tmp  =  getQuick  (  i  )  ; 
setQuick  (  i  ++  ,  getQuick  (  j  )  )  ; 
setQuick  (  j  --  ,  tmp  )  ; 
} 
} 








public   void   set  (  int   index  ,  float   element  )  { 
if  (  index  >=  size  ||  index  <  0  )  throw   new   IndexOutOfBoundsException  (  "Index: "  +  index  +  ", Size: "  +  size  )  ; 
setQuick  (  index  ,  element  )  ; 
} 













protected   abstract   void   setQuick  (  int   index  ,  float   element  )  ; 
















protected   void   setSizeRaw  (  int   newSize  )  { 
size  =  newSize  ; 
} 







public   void   shuffleFromTo  (  int   from  ,  int   to  )  { 
checkRangeFromTo  (  from  ,  to  ,  size  (  )  )  ; 
cern  .  jet  .  random  .  Uniform   gen  =  new   cern  .  jet  .  random  .  Uniform  (  new   cern  .  jet  .  random  .  engine  .  DRand  (  new   java  .  util  .  Date  (  )  )  )  ; 
for  (  int   i  =  from  ;  i  <  to  ;  i  ++  )  { 
int   random  =  gen  .  nextIntFromTo  (  i  ,  to  )  ; 
float   tmpElement  =  getQuick  (  random  )  ; 
setQuick  (  random  ,  getQuick  (  i  )  )  ; 
setQuick  (  i  ,  tmpElement  )  ; 
} 
} 






public   int   size  (  )  { 
return   size  ; 
} 





public   AbstractFloatList   times  (  int   times  )  { 
AbstractFloatList   newList  =  new   FloatArrayList  (  times  *  size  (  )  )  ; 
for  (  int   i  =  times  ;  --  i  >=  0  ;  )  { 
newList  .  addAllOfFromTo  (  this  ,  0  ,  size  (  )  -  1  )  ; 
} 
return   newList  ; 
} 




public   java  .  util  .  ArrayList   toList  (  )  { 
int   mySize  =  size  (  )  ; 
java  .  util  .  ArrayList   list  =  new   java  .  util  .  ArrayList  (  mySize  )  ; 
for  (  int   i  =  0  ;  i  <  mySize  ;  i  ++  )  list  .  add  (  new   Float  (  get  (  i  )  )  )  ; 
return   list  ; 
} 





public   String   toString  (  )  { 
return   cern  .  colt  .  Arrays  .  toString  (  partFromTo  (  0  ,  size  (  )  -  1  )  .  elements  (  )  )  ; 
} 
} 

