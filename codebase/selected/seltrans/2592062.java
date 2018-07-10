package   JSci  .  maths  ; 






public   class   IntegerSquareMatrix   extends   IntegerMatrix  { 

protected   transient   DoubleSquareMatrix   LU  [  ]  ; 

protected   transient   int   LUpivot  [  ]  ; 




protected   IntegerSquareMatrix  (  final   int   size  ,  final   int   storeID  )  { 
super  (  size  ,  size  ,  storeID  )  ; 
} 





public   IntegerSquareMatrix  (  final   int   size  )  { 
super  (  size  ,  size  )  ; 
} 






public   IntegerSquareMatrix  (  final   int   array  [  ]  [  ]  )  { 
super  (  array  )  ; 
if  (  array  .  length  !=  array  [  0  ]  .  length  )  { 
matrix  =  null  ; 
throw   new   MatrixDimensionException  (  "The array is not square."  )  ; 
} 
} 






public   IntegerSquareMatrix  (  final   IntegerVector   array  [  ]  )  { 
super  (  array  )  ; 
if  (  array  .  length  !=  array  [  0  ]  .  dimension  (  )  )  { 
matrix  =  null  ; 
throw   new   MatrixDimensionException  (  "The array does not form a square matrix."  )  ; 
} 
} 




public   boolean   isSymmetric  (  )  { 
return   this  .  equals  (  this  .  transpose  (  )  )  ; 
} 




public   boolean   isUnitary  (  )  { 
return   this  .  multiply  (  this  .  transpose  (  )  )  .  equals  (  IntegerDiagonalMatrix  .  identity  (  numRows  )  )  ; 
} 




public   int   det  (  )  { 
if  (  numRows  ==  2  )  { 
return   matrix  [  0  ]  [  0  ]  *  matrix  [  1  ]  [  1  ]  -  matrix  [  0  ]  [  1  ]  *  matrix  [  1  ]  [  0  ]  ; 
}  else  { 
final   DoubleSquareMatrix   lu  [  ]  =  this  .  luDecompose  (  null  )  ; 
double   det  =  lu  [  1  ]  .  matrix  [  0  ]  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  numRows  ;  i  ++  )  det  *=  lu  [  1  ]  .  matrix  [  i  ]  [  i  ]  ; 
return  (  int  )  (  det  )  *  LUpivot  [  numRows  ]  ; 
} 
} 




public   int   trace  (  )  { 
int   result  =  matrix  [  0  ]  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  numRows  ;  i  ++  )  result  +=  matrix  [  i  ]  [  i  ]  ; 
return   result  ; 
} 






public   IntegerMatrix   add  (  final   IntegerMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawAdd  (  m  )  ; 
default  : 
if  (  numRows  ==  m  .  rows  (  )  &&  numCols  ==  m  .  columns  (  )  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  +  m  .  getElement  (  i  ,  0  )  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  +  m  .  getElement  (  i  ,  j  )  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 

private   IntegerSquareMatrix   rawAdd  (  final   IntegerMatrix   m  )  { 
if  (  numRows  ==  m  .  numRows  &&  numCols  ==  m  .  numCols  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  +  m  .  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  +  m  .  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 






public   IntegerSquareMatrix   add  (  final   IntegerSquareMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawAdd  (  m  )  ; 
default  : 
if  (  numRows  ==  m  .  rows  (  )  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  +  m  .  getElement  (  i  ,  0  )  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  +  m  .  getElement  (  i  ,  j  )  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 






public   IntegerMatrix   subtract  (  final   IntegerMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawSubtract  (  m  )  ; 
default  : 
if  (  numRows  ==  m  .  rows  (  )  &&  numCols  ==  m  .  columns  (  )  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  -  m  .  getElement  (  i  ,  0  )  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  -  m  .  getElement  (  i  ,  j  )  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 

private   IntegerSquareMatrix   rawSubtract  (  final   IntegerMatrix   m  )  { 
if  (  numRows  ==  m  .  numRows  &&  numCols  ==  m  .  numCols  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  -  m  .  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  -  m  .  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 






public   IntegerSquareMatrix   subtract  (  final   IntegerSquareMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawSubtract  (  m  )  ; 
default  : 
if  (  numRows  ==  m  .  rows  (  )  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  -  m  .  getElement  (  i  ,  0  )  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  -  m  .  getElement  (  i  ,  j  )  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 






public   IntegerMatrix   scalarMultiply  (  final   int   x  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  x  *  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  x  *  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
} 






public   IntegerVector   multiply  (  final   IntegerVector   v  )  { 
if  (  numCols  ==  v  .  dimension  (  )  )  { 
final   int   array  [  ]  =  new   int  [  numRows  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  =  matrix  [  i  ]  [  0  ]  *  v  .  getComponent  (  0  )  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  +=  matrix  [  i  ]  [  j  ]  *  v  .  getComponent  (  j  )  ; 
} 
return   new   IntegerVector  (  array  )  ; 
}  else   throw   new   DimensionException  (  "Matrix and vector are incompatible."  )  ; 
} 






public   IntegerSquareMatrix   multiply  (  final   IntegerSquareMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawMultiply  (  m  )  ; 
default  : 
if  (  numRows  ==  m  .  rows  (  )  )  { 
int   n  ,  k  ; 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  =  0  ;  j  <  numRows  ;  j  ++  )  { 
for  (  k  =  0  ;  k  <  numCols  ;  k  ++  )  { 
array  [  j  ]  [  k  ]  =  matrix  [  j  ]  [  0  ]  *  m  .  getElement  (  0  ,  k  )  ; 
for  (  n  =  1  ;  n  <  numCols  ;  n  ++  )  array  [  j  ]  [  k  ]  +=  matrix  [  j  ]  [  n  ]  *  m  .  getElement  (  n  ,  k  )  ; 
} 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Incompatible matrices."  )  ; 
} 
} 

private   IntegerSquareMatrix   rawMultiply  (  final   IntegerSquareMatrix   m  )  { 
if  (  numRows  ==  m  .  numRows  )  { 
int   n  ,  k  ; 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  =  0  ;  j  <  numRows  ;  j  ++  )  { 
for  (  k  =  0  ;  k  <  numCols  ;  k  ++  )  { 
array  [  j  ]  [  k  ]  =  matrix  [  j  ]  [  0  ]  *  m  .  matrix  [  0  ]  [  k  ]  ; 
for  (  n  =  1  ;  n  <  numCols  ;  n  ++  )  array  [  j  ]  [  k  ]  +=  matrix  [  j  ]  [  n  ]  *  m  .  matrix  [  n  ]  [  k  ]  ; 
} 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Incompatible matrices."  )  ; 
} 





public   Matrix   transpose  (  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numCols  ]  [  numRows  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  0  ]  [  i  ]  =  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  j  ]  [  i  ]  =  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerSquareMatrix  (  array  )  ; 
} 









public   DoubleSquareMatrix  [  ]  luDecompose  (  int   pivot  [  ]  )  { 
if  (  LU  !=  null  )  { 
if  (  pivot  !=  null  )  System  .  arraycopy  (  LUpivot  ,  0  ,  pivot  ,  0  ,  pivot  .  length  )  ; 
return   LU  ; 
} 
int   i  ,  j  ,  k  ,  pivotrow  ; 
final   int   N  =  numRows  ; 
final   double   arrayL  [  ]  [  ]  =  new   double  [  N  ]  [  N  ]  ; 
final   double   arrayU  [  ]  [  ]  =  new   double  [  N  ]  [  N  ]  ; 
double   tmp  ,  max  ; 
if  (  pivot  ==  null  )  pivot  =  new   int  [  N  +  1  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  pivot  [  i  ]  =  i  ; 
pivot  [  N  ]  =  1  ; 
for  (  j  =  0  ;  j  <  N  ;  j  ++  )  { 
for  (  i  =  0  ;  i  <  j  ;  i  ++  )  { 
tmp  =  matrix  [  pivot  [  i  ]  ]  [  j  ]  ; 
for  (  k  =  0  ;  k  <  i  ;  k  ++  )  tmp  -=  arrayU  [  i  ]  [  k  ]  *  arrayU  [  k  ]  [  j  ]  ; 
arrayU  [  i  ]  [  j  ]  =  tmp  ; 
} 
max  =  0.0  ; 
pivotrow  =  j  ; 
for  (  i  =  j  ;  i  <  N  ;  i  ++  )  { 
tmp  =  matrix  [  pivot  [  i  ]  ]  [  j  ]  ; 
for  (  k  =  0  ;  k  <  j  ;  k  ++  )  tmp  -=  arrayU  [  i  ]  [  k  ]  *  arrayU  [  k  ]  [  j  ]  ; 
arrayU  [  i  ]  [  j  ]  =  tmp  ; 
tmp  =  Math  .  abs  (  tmp  )  ; 
if  (  tmp  >  max  )  { 
max  =  tmp  ; 
pivotrow  =  i  ; 
} 
} 
if  (  pivotrow  !=  j  )  { 
for  (  k  =  0  ;  k  <  j  +  1  ;  k  ++  )  { 
tmp  =  arrayU  [  j  ]  [  k  ]  ; 
arrayU  [  j  ]  [  k  ]  =  arrayU  [  pivotrow  ]  [  k  ]  ; 
arrayU  [  pivotrow  ]  [  k  ]  =  tmp  ; 
} 
k  =  pivot  [  j  ]  ; 
pivot  [  j  ]  =  pivot  [  pivotrow  ]  ; 
pivot  [  pivotrow  ]  =  k  ; 
pivot  [  N  ]  =  -  pivot  [  N  ]  ; 
} 
tmp  =  arrayU  [  j  ]  [  j  ]  ; 
for  (  i  =  j  +  1  ;  i  <  N  ;  i  ++  )  arrayU  [  i  ]  [  j  ]  /=  tmp  ; 
} 
for  (  j  =  0  ;  j  <  N  ;  j  ++  )  { 
arrayL  [  j  ]  [  j  ]  =  1.0  ; 
for  (  i  =  j  +  1  ;  i  <  N  ;  i  ++  )  { 
arrayL  [  i  ]  [  j  ]  =  arrayU  [  i  ]  [  j  ]  ; 
arrayU  [  i  ]  [  j  ]  =  0.0  ; 
} 
} 
LU  =  new   DoubleSquareMatrix  [  2  ]  ; 
LU  [  0  ]  =  new   DoubleSquareMatrix  (  arrayL  )  ; 
LU  [  1  ]  =  new   DoubleSquareMatrix  (  arrayU  )  ; 
LUpivot  =  new   int  [  pivot  .  length  ]  ; 
System  .  arraycopy  (  pivot  ,  0  ,  LUpivot  ,  0  ,  pivot  .  length  )  ; 
return   LU  ; 
} 






public   DoubleSquareMatrix  [  ]  choleskyDecomposition  (  )  { 
int   i  ,  j  ,  k  ; 
final   int   N  =  numRows  ; 
final   double   arrayL  [  ]  [  ]  =  new   double  [  N  ]  [  N  ]  ; 
final   double   arrayU  [  ]  [  ]  =  new   double  [  N  ]  [  N  ]  ; 
double   tmp  ; 
arrayL  [  0  ]  [  0  ]  =  arrayU  [  0  ]  [  0  ]  =  Math  .  sqrt  (  matrix  [  0  ]  [  0  ]  )  ; 
for  (  i  =  1  ;  i  <  N  ;  i  ++  )  arrayL  [  i  ]  [  0  ]  =  arrayU  [  0  ]  [  i  ]  =  matrix  [  i  ]  [  0  ]  /  arrayL  [  0  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  N  ;  j  ++  )  { 
tmp  =  matrix  [  j  ]  [  j  ]  ; 
for  (  i  =  0  ;  i  <  j  ;  i  ++  )  tmp  -=  arrayL  [  j  ]  [  i  ]  *  arrayL  [  j  ]  [  i  ]  ; 
arrayL  [  j  ]  [  j  ]  =  arrayU  [  j  ]  [  j  ]  =  Math  .  sqrt  (  tmp  )  ; 
for  (  i  =  j  +  1  ;  i  <  N  ;  i  ++  )  { 
tmp  =  matrix  [  i  ]  [  j  ]  ; 
for  (  k  =  0  ;  k  <  i  ;  k  ++  )  tmp  -=  arrayL  [  j  ]  [  k  ]  *  arrayU  [  k  ]  [  i  ]  ; 
arrayL  [  i  ]  [  j  ]  =  arrayU  [  j  ]  [  i  ]  =  tmp  /  arrayU  [  j  ]  [  j  ]  ; 
} 
} 
final   DoubleSquareMatrix   lu  [  ]  =  new   DoubleSquareMatrix  [  2  ]  ; 
lu  [  0  ]  =  new   DoubleSquareMatrix  (  arrayL  )  ; 
lu  [  1  ]  =  new   DoubleSquareMatrix  (  arrayU  )  ; 
return   lu  ; 
} 
} 

