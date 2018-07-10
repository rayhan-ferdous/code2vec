package   com  .  teletalk  .  jserver  .  util  .  primitive  ; 

import   java  .  util  .  Arrays  ; 








public   final   class   IntList   extends   PrimitiveList  { 

static   final   long   serialVersionUID  =  6594756081706899907L  ; 

private   int  [  ]  intArray  ; 

private   final   int   defaultValue  ; 





public   IntList  (  )  { 
this  (  10  ,  10  )  ; 
} 







public   IntList  (  final   int   initialCapacity  )  { 
this  (  initialCapacity  ,  10  )  ; 
} 








public   IntList  (  final   int   initialCapacity  ,  final   int   capacityIncrement  )  { 
this  (  initialCapacity  ,  capacityIncrement  ,  0  )  ; 
} 








public   IntList  (  final   int   initialCapacity  ,  final   int   capacityIncrement  ,  final   int   defaultValue  )  { 
super  (  initialCapacity  ,  capacityIncrement  )  ; 
this  .  defaultValue  =  defaultValue  ; 
} 







public   IntList  (  final   int  [  ]  array  )  { 
this  (  array  ,  10  )  ; 
} 








public   IntList  (  final   int  [  ]  array  ,  final   int   capacityIncrement  )  { 
this  (  array  ,  capacityIncrement  ,  0  )  ; 
} 








public   IntList  (  final   int  [  ]  array  ,  final   int   capacityIncrement  ,  final   int   defaultValue  )  { 
super  (  array  ,  array  .  length  ,  capacityIncrement  )  ; 
this  .  defaultValue  =  defaultValue  ; 
} 

protected   Object   getPrimitiveArray  (  )  { 
return   intArray  ; 
} 

protected   void   setPrimitiveArray  (  Object   primitiveArray  )  { 
this  .  intArray  =  (  int  [  ]  )  primitiveArray  ; 
} 








public   int   add  (  final   int   value  )  { 
final   int   addIndex  =  super  .  prepareAdd  (  1  )  ; 
this  .  intArray  [  addIndex  ]  =  value  ; 
return   addIndex  ; 
} 










public   void   add  (  final   int   index  ,  final   int   value  )  { 
super  .  prepareAdd  (  1  ,  index  )  ; 
this  .  intArray  [  index  ]  =  value  ; 
} 








public   int   addSorted  (  final   int   value  )  { 
int   addIndex  =  this  .  binarySearch  (  value  )  ; 
if  (  addIndex  <  0  )  addIndex  =  (  -  addIndex  )  -  1  ; 
this  .  add  (  addIndex  ,  value  )  ; 
return   addIndex  ; 
} 








public   int   addAll  (  final   int  [  ]  values  )  { 
final   int   startIndex  =  super  .  prepareAdd  (  values  .  length  )  ; 
System  .  arraycopy  (  values  ,  0  ,  this  .  intArray  ,  startIndex  ,  values  .  length  )  ; 
return   startIndex  ; 
} 










public   void   addAll  (  final   int   addIndex  ,  final   int  [  ]  values  )  throws   IndexOutOfBoundsException  { 
super  .  prepareAdd  (  values  .  length  ,  addIndex  )  ; 
System  .  arraycopy  (  values  ,  0  ,  this  .  intArray  ,  addIndex  ,  values  .  length  )  ; 
} 










public   int   remove  (  final   int   index  )  throws   IndexOutOfBoundsException  { 
final   int   oldValue  =  this  .  intArray  [  index  ]  ; 
super  .  removeValueAtIndex  (  index  )  ; 
return   oldValue  ; 
} 








public   boolean   removeValue  (  final   int   value  )  { 
final   int   arrayLength  =  super  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  arrayLength  ;  i  ++  )  { 
if  (  this  .  intArray  [  i  ]  ==  value  )  { 
remove  (  i  )  ; 
return   true  ; 
} 
} 
return   false  ; 
} 








public   boolean   removeValueSorted  (  final   int   value  )  { 
final   int   removeIndex  =  this  .  binarySearch  (  value  )  ; 
if  (  removeIndex  >=  0  )  { 
remove  (  removeIndex  )  ; 
return   true  ; 
} 
return   false  ; 
} 










public   int   get  (  final   int   index  )  throws   IndexOutOfBoundsException  { 
super  .  boundsCheck  (  index  )  ; 
return   this  .  intArray  [  index  ]  ; 
} 









public   int   set  (  final   int   index  ,  final   int   value  )  throws   IndexOutOfBoundsException  { 
super  .  boundsCheck  (  index  )  ; 
final   int   oldValue  =  this  .  intArray  [  index  ]  ; 
this  .  intArray  [  index  ]  =  value  ; 
return   oldValue  ; 
} 

protected   void   resetValues  (  final   int   fromIndex  ,  final   int   toIndex  )  { 
for  (  int   i  =  fromIndex  ;  i  <  toIndex  ;  i  ++  )  { 
this  .  intArray  [  i  ]  =  defaultValue  ; 
} 
} 

protected   Object   newArrary  (  final   int   size  )  { 
return   new   int  [  size  ]  ; 
} 






public   int  [  ]  toArray  (  )  { 
final   int   valueCount  =  super  .  size  (  )  ; 
final   int  [  ]  all  =  new   int  [  valueCount  ]  ; 
System  .  arraycopy  (  this  .  intArray  ,  0  ,  all  ,  0  ,  valueCount  )  ; 
return   all  ; 
} 












public   int   binarySearch  (  final   int   key  )  { 
int   low  =  0  ; 
int   middle  ; 
int   high  =  super  .  size  (  )  -  1  ; 
int   middleValue  ; 
while  (  low  <=  high  )  { 
middle  =  (  low  +  high  )  /  2  ; 
middleValue  =  this  .  intArray  [  middle  ]  ; 
if  (  middleValue  <  key  )  { 
low  =  middle  +  1  ; 
}  else   if  (  middleValue  >  key  )  { 
high  =  middle  -  1  ; 
}  else  { 
return   middle  ; 
} 
} 
return  -  (  low  +  1  )  ; 
} 










public   boolean   contains  (  final   int   key  )  { 
for  (  int   i  =  0  ;  i  <  intArray  .  length  ;  i  ++  )  { 
if  (  intArray  [  i  ]  ==  key  )  return   true  ; 
} 
return   false  ; 
} 




public   void   sort  (  )  { 
Arrays  .  sort  (  this  .  intArray  ,  0  ,  this  .  size  (  )  )  ; 
} 











public   boolean   equals  (  Object   o  )  { 
int  [  ]  otherArray  ; 
if  (  o   instanceof   IntList  )  otherArray  =  (  (  IntList  )  o  )  .  toArray  (  )  ;  else   if  (  o   instanceof   int  [  ]  )  otherArray  =  (  int  [  ]  )  o  ;  else   return   false  ; 
if  (  super  .  size  (  )  !=  otherArray  .  length  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  otherArray  .  length  ;  i  ++  )  { 
if  (  this  .  intArray  [  i  ]  !=  otherArray  [  i  ]  )  return   false  ; 
} 
return   true  ; 
} 




public   String   toString  (  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "["  )  ; 
final   int   valueCount  =  super  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  valueCount  ;  i  ++  )  { 
buf  .  append  (  String  .  valueOf  (  this  .  intArray  [  i  ]  )  )  ; 
if  (  i  <  (  valueCount  -  1  )  )  buf  .  append  (  ","  )  ; 
} 
buf  .  append  (  "]"  )  ; 
return   buf  .  toString  (  )  ; 
} 
} 

