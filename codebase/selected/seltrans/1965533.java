package   com  .  teletalk  .  jserver  .  util  .  primitive  ; 

import   java  .  util  .  Arrays  ; 








public   final   class   DoubleList   extends   PrimitiveList  { 

static   final   long   serialVersionUID  =  6452525263708624736L  ; 

private   double  [  ]  doubleArray  ; 

private   final   double   defaultValue  ; 





public   DoubleList  (  )  { 
this  (  10  ,  10  )  ; 
} 







public   DoubleList  (  final   int   initialCapacity  )  { 
this  (  initialCapacity  ,  10  )  ; 
} 








public   DoubleList  (  final   int   initialCapacity  ,  final   int   capacityIncrement  )  { 
this  (  initialCapacity  ,  capacityIncrement  ,  0  )  ; 
} 








public   DoubleList  (  final   int   initialCapacity  ,  final   int   capacityIncrement  ,  final   double   defaultValue  )  { 
super  (  initialCapacity  ,  capacityIncrement  )  ; 
this  .  defaultValue  =  defaultValue  ; 
} 







public   DoubleList  (  final   double  [  ]  array  )  { 
this  (  array  ,  10  )  ; 
} 








public   DoubleList  (  final   double  [  ]  array  ,  final   int   capacityIncrement  )  { 
this  (  array  ,  capacityIncrement  ,  0  )  ; 
} 








public   DoubleList  (  final   double  [  ]  array  ,  final   int   capacityIncrement  ,  final   double   defaultValue  )  { 
super  (  array  ,  array  .  length  ,  capacityIncrement  )  ; 
this  .  defaultValue  =  defaultValue  ; 
} 

protected   Object   getPrimitiveArray  (  )  { 
return   doubleArray  ; 
} 

protected   void   setPrimitiveArray  (  Object   primitiveArray  )  { 
this  .  doubleArray  =  (  double  [  ]  )  primitiveArray  ; 
} 








public   int   add  (  final   double   value  )  { 
final   int   addIndex  =  super  .  prepareAdd  (  1  )  ; 
this  .  doubleArray  [  addIndex  ]  =  value  ; 
return   addIndex  ; 
} 










public   void   add  (  final   int   index  ,  final   double   value  )  throws   IndexOutOfBoundsException  { 
super  .  prepareAdd  (  1  ,  index  )  ; 
this  .  doubleArray  [  index  ]  =  value  ; 
} 








public   int   addSorted  (  final   double   value  )  { 
int   addIndex  =  this  .  binarySearch  (  value  )  ; 
if  (  addIndex  <  0  )  addIndex  =  (  -  addIndex  )  -  1  ; 
this  .  add  (  addIndex  ,  value  )  ; 
return   addIndex  ; 
} 








public   int   addAll  (  final   double  [  ]  values  )  { 
final   int   startIndex  =  super  .  prepareAdd  (  values  .  length  )  ; 
System  .  arraycopy  (  values  ,  0  ,  this  .  doubleArray  ,  startIndex  ,  values  .  length  )  ; 
return   startIndex  ; 
} 










public   void   addAll  (  final   int   addIndex  ,  final   double  [  ]  values  )  throws   IndexOutOfBoundsException  { 
super  .  prepareAdd  (  values  .  length  ,  addIndex  )  ; 
System  .  arraycopy  (  values  ,  0  ,  this  .  doubleArray  ,  addIndex  ,  values  .  length  )  ; 
} 










public   double   remove  (  final   int   index  )  throws   IndexOutOfBoundsException  { 
final   double   oldValue  =  this  .  doubleArray  [  index  ]  ; 
super  .  removeValueAtIndex  (  index  )  ; 
return   oldValue  ; 
} 








public   boolean   removeValue  (  final   double   value  )  { 
final   int   arrayLength  =  super  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  arrayLength  ;  i  ++  )  { 
if  (  this  .  doubleArray  [  i  ]  ==  value  )  { 
remove  (  i  )  ; 
return   true  ; 
} 
} 
return   false  ; 
} 








public   boolean   removeValueSorted  (  final   double   value  )  { 
final   int   removeIndex  =  this  .  binarySearch  (  value  )  ; 
if  (  removeIndex  >=  0  )  { 
remove  (  removeIndex  )  ; 
return   true  ; 
} 
return   false  ; 
} 










public   double   get  (  final   int   index  )  throws   IndexOutOfBoundsException  { 
super  .  boundsCheck  (  index  )  ; 
return   this  .  doubleArray  [  index  ]  ; 
} 









public   double   set  (  final   int   index  ,  final   double   value  )  throws   IndexOutOfBoundsException  { 
super  .  boundsCheck  (  index  )  ; 
final   double   oldValue  =  this  .  doubleArray  [  index  ]  ; 
this  .  doubleArray  [  index  ]  =  value  ; 
return   oldValue  ; 
} 

protected   void   resetValues  (  final   int   fromIndex  ,  final   int   toIndex  )  { 
for  (  int   i  =  fromIndex  ;  i  <  toIndex  ;  i  ++  )  { 
this  .  doubleArray  [  i  ]  =  defaultValue  ; 
} 
} 

protected   Object   newArrary  (  final   int   size  )  { 
return   new   double  [  size  ]  ; 
} 






public   double  [  ]  toArray  (  )  { 
final   int   valueCount  =  super  .  size  (  )  ; 
final   double  [  ]  all  =  new   double  [  valueCount  ]  ; 
System  .  arraycopy  (  this  .  doubleArray  ,  0  ,  all  ,  0  ,  valueCount  )  ; 
return   all  ; 
} 












public   int   binarySearch  (  final   double   key  )  { 
int   low  =  0  ; 
int   middle  ; 
int   high  =  super  .  size  (  )  -  1  ; 
double   middleValue  ; 
while  (  low  <=  high  )  { 
middle  =  (  low  +  high  )  /  2  ; 
middleValue  =  this  .  doubleArray  [  middle  ]  ; 
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










public   boolean   contains  (  final   double   key  )  { 
for  (  int   i  =  0  ;  i  <  doubleArray  .  length  ;  i  ++  )  { 
if  (  doubleArray  [  i  ]  ==  key  )  return   true  ; 
} 
return   false  ; 
} 




public   void   sort  (  )  { 
Arrays  .  sort  (  this  .  doubleArray  ,  0  ,  this  .  size  (  )  )  ; 
} 











public   boolean   equals  (  Object   o  )  { 
double  [  ]  otherArray  ; 
if  (  o   instanceof   DoubleList  )  otherArray  =  (  (  DoubleList  )  o  )  .  toArray  (  )  ;  else   if  (  o   instanceof   double  [  ]  )  otherArray  =  (  double  [  ]  )  o  ;  else   return   false  ; 
if  (  super  .  size  (  )  !=  otherArray  .  length  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  otherArray  .  length  ;  i  ++  )  { 
if  (  this  .  doubleArray  [  i  ]  !=  otherArray  [  i  ]  )  return   false  ; 
} 
return   true  ; 
} 




public   String   toString  (  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "["  )  ; 
final   int   valueCount  =  super  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  valueCount  ;  i  ++  )  { 
buf  .  append  (  String  .  valueOf  (  this  .  doubleArray  [  i  ]  )  )  ; 
if  (  i  <  (  valueCount  -  1  )  )  buf  .  append  (  ","  )  ; 
} 
buf  .  append  (  "]"  )  ; 
return   buf  .  toString  (  )  ; 
} 
} 

