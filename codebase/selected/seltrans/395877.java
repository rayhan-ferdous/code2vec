package   ucar  .  multiarray  ; 

import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  io  .  IOException  ; 

public   class   ArrayMultiArray   implements   MultiArray  { 





ArrayMultiArray  (  Object   aro  ,  int   theRank  ,  Class   componentType  )  { 
jla  =  aro  ; 
rank  =  theRank  ; 
this  .  componentType  =  componentType  ; 
} 











public   ArrayMultiArray  (  Object   aro  )  { 
int   rank_  =  0  ; 
Class   componentType_  =  aro  .  getClass  (  )  ; 
while  (  componentType_  .  isArray  (  )  )  { 
rank_  ++  ; 
componentType_  =  componentType_  .  getComponentType  (  )  ; 
} 
if  (  rank_  ==  0  )  throw   new   IllegalArgumentException  (  )  ; 
jla  =  aro  ; 
rank  =  rank_  ; 
componentType  =  componentType_  ; 
} 










public   ArrayMultiArray  (  Class   componentType  ,  int  [  ]  dimensions  )  { 
rank  =  dimensions  .  length  ; 
if  (  rank  ==  0  )  throw   new   IllegalArgumentException  (  )  ; 
this  .  componentType  =  componentType  ; 
jla  =  Array  .  newInstance  (  componentType  ,  dimensions  )  ; 
} 








public   ArrayMultiArray  (  MultiArray   ma  )  throws   IOException  { 
rank  =  ma  .  getRank  (  )  ; 
if  (  rank  ==  0  )  throw   new   IllegalArgumentException  (  )  ; 
componentType  =  ma  .  getComponentType  (  )  ; 
final   int  [  ]  lengths  =  ma  .  getLengths  (  )  ; 
jla  =  Array  .  newInstance  (  componentType  ,  lengths  )  ; 
IndexIterator   odo  =  new   IndexIterator  (  lengths  )  ; 
for  (  ;  odo  .  notDone  (  )  ;  odo  .  incr  (  )  )  { 
final   int  [  ]  index  =  odo  .  value  (  )  ; 
this  .  set  (  index  ,  ma  .  get  (  index  )  )  ; 
} 
} 









public   Class   getComponentType  (  )  { 
return   componentType  ; 
} 





public   int   getRank  (  )  { 
return   rank  ; 
} 










public   int  [  ]  getLengths  (  )  { 
int  [  ]  lengths  =  new   int  [  rank  ]  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  rank  ;  ii  ++  )  { 
lengths  [  ii  ]  =  Array  .  getLength  (  oo  )  ; 
oo  =  Array  .  get  (  oo  ,  0  )  ; 
} 
return   lengths  ; 
} 







public   boolean   isUnlimited  (  )  { 
return   false  ; 
} 






public   boolean   isScalar  (  )  { 
return   rank  ==  0  ; 
} 




public   Object   get  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  get  (  oo  ,  index  [  end  ]  )  ; 
} 




public   boolean   getBoolean  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getBoolean  (  oo  ,  index  [  end  ]  )  ; 
} 




public   char   getChar  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getChar  (  oo  ,  index  [  end  ]  )  ; 
} 




public   byte   getByte  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getByte  (  oo  ,  index  [  end  ]  )  ; 
} 




public   short   getShort  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getShort  (  oo  ,  index  [  end  ]  )  ; 
} 




public   int   getInt  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getInt  (  oo  ,  index  [  end  ]  )  ; 
} 




public   long   getLong  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getLong  (  oo  ,  index  [  end  ]  )  ; 
} 




public   float   getFloat  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getFloat  (  oo  ,  index  [  end  ]  )  ; 
} 




public   double   getDouble  (  int  [  ]  index  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  getDouble  (  oo  ,  index  [  end  ]  )  ; 
} 




public   void   set  (  int  [  ]  index  ,  Object   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  set  (  oo  ,  index  [  end  ]  ,  value  )  ; 
return  ; 
} 




public   void   setBoolean  (  int  [  ]  index  ,  boolean   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setBoolean  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setChar  (  int  [  ]  index  ,  char   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setChar  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setByte  (  int  [  ]  index  ,  byte   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setByte  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setShort  (  int  [  ]  index  ,  short   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setShort  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setInt  (  int  [  ]  index  ,  int   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setInt  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setLong  (  int  [  ]  index  ,  long   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setLong  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setFloat  (  int  [  ]  index  ,  float   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setFloat  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   void   setDouble  (  int  [  ]  index  ,  double   value  )  { 
if  (  index  .  length  <  rank  )  throw   new   IllegalArgumentException  (  )  ; 
final   int   end  =  rank  -  1  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
Array  .  setDouble  (  oo  ,  index  [  end  ]  ,  value  )  ; 
} 




public   MultiArray   copyout  (  int  [  ]  origin  ,  int  [  ]  shape  )  { 
if  (  origin  .  length  !=  rank  ||  shape  .  length  !=  rank  )  throw   new   IllegalArgumentException  (  "Rank Mismatch"  )  ; 
final   int  [  ]  shp  =  (  int  [  ]  )  shape  .  clone  (  )  ; 
final   int  [  ]  pducts  =  new   int  [  shp  .  length  ]  ; 
final   int   product  =  MultiArrayImpl  .  numberOfElements  (  shp  ,  pducts  )  ; 
final   Object   dst  =  Array  .  newInstance  (  getComponentType  (  )  ,  product  )  ; 
int   ji  =  rank  -  1  ; 
int   src_pos  =  origin  [  ji  ]  ; 
if  (  ji  ==  0  )  { 
System  .  arraycopy  (  jla  ,  src_pos  ,  dst  ,  0  ,  product  )  ; 
}  else  { 
ji  --  ; 
final   int   contig  =  pducts  [  ji  ]  ; 
final   OffsetIndexIterator   odo  =  new   OffsetIndexIterator  (  truncCopy  (  origin  )  ,  getTruncLengths  (  )  )  ; 
for  (  int   dst_pos  =  0  ;  dst_pos  <  product  ;  dst_pos  +=  contig  )  { 
System  .  arraycopy  (  getLeaf  (  odo  .  value  (  )  )  ,  src_pos  ,  dst  ,  dst_pos  ,  contig  )  ; 
odo  .  incr  (  )  ; 
} 
} 
return   new   MultiArrayImpl  (  shp  ,  pducts  ,  dst  )  ; 
} 




public   void   copyin  (  int  [  ]  origin  ,  MultiArray   data  )  throws   IOException  { 
if  (  origin  .  length  !=  rank  ||  data  .  getRank  (  )  !=  rank  )  throw   new   IllegalArgumentException  (  "Rank Mismatch"  )  ; 
if  (  data  .  getComponentType  (  )  !=  componentType  )  throw   new   ArrayStoreException  (  )  ; 
AbstractAccessor  .  copy  (  data  ,  data  .  getLengths  (  )  ,  this  ,  origin  )  ; 
} 




public   Object   toArray  (  )  { 
return   this  .  toArray  (  null  ,  null  ,  null  )  ; 
} 

public   Object   getStorage  (  )  { 
return   jla  ; 
} 




public   Object   toArray  (  Object   dst  ,  int  [  ]  origin  ,  int  [  ]  shape  )  { 
if  (  origin  ==  null  )  origin  =  new   int  [  rank  ]  ;  else   if  (  origin  .  length  !=  rank  )  throw   new   IllegalArgumentException  (  "Rank Mismatch"  )  ; 
int  [  ]  shp  =  null  ; 
if  (  shape  ==  null  )  shp  =  getLengths  (  )  ;  else   if  (  shape  .  length  ==  rank  )  shp  =  (  int  [  ]  )  shape  .  clone  (  )  ;  else   throw   new   IllegalArgumentException  (  "Rank Mismatch"  )  ; 
final   int  [  ]  pducts  =  new   int  [  shp  .  length  ]  ; 
final   int   product  =  MultiArrayImpl  .  numberOfElements  (  shp  ,  pducts  )  ; 
dst  =  MultiArrayImpl  .  fixDest  (  dst  ,  product  ,  componentType  )  ; 
int   ji  =  rank  -  1  ; 
int   src_pos  =  origin  [  ji  ]  ; 
if  (  ji  ==  0  )  { 
System  .  arraycopy  (  jla  ,  src_pos  ,  dst  ,  0  ,  product  )  ; 
}  else  { 
ji  --  ; 
final   int   contig  =  pducts  [  ji  ]  ; 
final   OffsetIndexIterator   odo  =  new   OffsetIndexIterator  (  truncCopy  (  origin  )  ,  getTruncLengths  (  )  )  ; 
for  (  int   dst_pos  =  0  ;  dst_pos  <  product  ;  dst_pos  +=  contig  )  { 
System  .  arraycopy  (  getLeaf  (  odo  .  value  (  )  )  ,  src_pos  ,  dst  ,  dst_pos  ,  contig  )  ; 
odo  .  incr  (  )  ; 
} 
} 
return   dst  ; 
} 

static   int  [  ]  truncCopy  (  int  [  ]  src  )  { 
final   int   len  =  src  .  length  -  1  ; 
int  [  ]  dst  =  new   int  [  len  ]  ; 
System  .  arraycopy  (  src  ,  0  ,  dst  ,  0  ,  len  )  ; 
return   dst  ; 
} 








public   Object   get  (  int   index  )  { 
if  (  rank  ==  1  )  return   Array  .  get  (  jla  ,  index  )  ; 
return   new   ArrayMultiArray  (  Array  .  get  (  jla  ,  index  )  ,  rank  -  1  ,  componentType  )  ; 
} 




public   Object   getLeaf  (  int  [  ]  index  )  { 
final   int   end  =  rank  -  2  ; 
if  (  index  .  length  <=  end  )  throw   new   IllegalArgumentException  (  )  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  end  ;  ii  ++  )  oo  =  Array  .  get  (  oo  ,  index  [  ii  ]  )  ; 
return   Array  .  get  (  oo  ,  index  [  end  ]  )  ; 
} 






private   int  [  ]  getTruncLengths  (  )  { 
final   int   containRank  =  rank  -  1  ; 
int  [  ]  lengths  =  new   int  [  containRank  ]  ; 
Object   oo  =  jla  ; 
for  (  int   ii  =  0  ;  ii  <  containRank  ;  ii  ++  )  { 
lengths  [  ii  ]  =  Array  .  getLength  (  oo  )  ; 
oo  =  Array  .  get  (  oo  ,  0  )  ; 
} 
return   lengths  ; 
} 




public   final   Object   jla  ; 

private   final   int   rank  ; 

private   final   Class   componentType  ; 

public   static   void   main  (  String  [  ]  args  )  { 
System  .  out  .  println  (  ">>  "  +  System  .  currentTimeMillis  (  )  )  ; 
final   int  [  ]  shape  =  {  48  ,  64  }  ; 
MultiArrayImpl   init  =  new   MultiArrayImpl  (  Integer  .  TYPE  ,  shape  )  ; 
{ 
final   int   size  =  MultiArrayImpl  .  numberOfElements  (  shape  )  ; 
for  (  int   ii  =  0  ;  ii  <  size  ;  ii  ++  )  java  .  lang  .  reflect  .  Array  .  setInt  (  init  .  storage  ,  ii  ,  ii  )  ; 
} 
ArrayMultiArray   src  =  (  ArrayMultiArray  )  null  ; 
try  { 
src  =  new   ArrayMultiArray  (  init  )  ; 
}  catch  (  java  .  io  .  IOException   ee  )  { 
} 
int  [  ]  clip  =  new   int  [  ]  {  32  ,  64  }  ; 
int  [  ]  origin  =  new   int  [  ]  {  8  ,  0  }  ; 
MultiArray   ma  =  src  .  copyout  (  origin  ,  clip  )  ; 
try  { 
System  .  out  .  println  (  "Rank  "  +  ma  .  getRank  (  )  )  ; 
int  [  ]  lengths  =  ma  .  getLengths  (  )  ; 
System  .  out  .  println  (  "Shape { "  +  lengths  [  0  ]  +  ", "  +  lengths  [  1  ]  +  " }"  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  0  ,  0  }  )  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  1  ,  0  }  )  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  lengths  [  0  ]  -  1  ,  lengths  [  1  ]  -  1  }  )  )  ; 
}  catch  (  java  .  io  .  IOException   ee  )  { 
} 
clip  =  new   int  [  ]  {  48  ,  48  }  ; 
origin  =  new   int  [  ]  {  0  ,  8  }  ; 
ma  =  src  .  copyout  (  origin  ,  clip  )  ; 
try  { 
System  .  out  .  println  (  "Rank  "  +  ma  .  getRank  (  )  )  ; 
int  [  ]  lengths  =  ma  .  getLengths  (  )  ; 
System  .  out  .  println  (  "Shape { "  +  lengths  [  0  ]  +  ", "  +  lengths  [  1  ]  +  " }"  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  0  ,  0  }  )  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  1  ,  0  }  )  )  ; 
System  .  out  .  println  (  ma  .  getInt  (  new   int  [  ]  {  lengths  [  0  ]  -  1  ,  lengths  [  1  ]  -  1  }  )  )  ; 
}  catch  (  java  .  io  .  IOException   ee  )  { 
} 
ArrayMultiArray   dest  =  new   ArrayMultiArray  (  Integer  .  TYPE  ,  shape  )  ; 
try  { 
dest  .  copyin  (  origin  ,  ma  )  ; 
System  .  out  .  println  (  "***Rank  "  +  dest  .  getRank  (  )  )  ; 
int  [  ]  lengths  =  dest  .  getLengths  (  )  ; 
System  .  out  .  println  (  "Shape { "  +  lengths  [  0  ]  +  ", "  +  lengths  [  1  ]  +  " }"  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  0  ,  0  }  )  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  0  ,  7  }  )  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  0  ,  8  }  )  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  47  ,  55  }  )  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  47  ,  56  }  )  )  ; 
System  .  out  .  println  (  dest  .  getInt  (  new   int  [  ]  {  47  ,  63  }  )  )  ; 
}  catch  (  java  .  io  .  IOException   ee  )  { 
} 
} 
} 

