package   JSci  .  maths  ; 

import   JSci  .  maths  .  groups  .  AbelianGroup  ; 
import   JSci  .  maths  .  algebras  .  *  ; 
import   JSci  .  maths  .  fields  .  *  ; 






public   class   IntegerMatrix   extends   Matrix  { 




protected   static   final   int   ARRAY_2D  =  1  ; 




protected   int   matrix  [  ]  [  ]  ; 




protected   IntegerMatrix  (  final   int   rows  ,  final   int   cols  ,  final   int   storeID  )  { 
super  (  rows  ,  cols  )  ; 
storageFormat  =  storeID  ; 
} 






public   IntegerMatrix  (  final   int   rows  ,  final   int   cols  )  { 
this  (  rows  ,  cols  ,  ARRAY_2D  )  ; 
matrix  =  new   int  [  rows  ]  [  cols  ]  ; 
} 





public   IntegerMatrix  (  final   int   array  [  ]  [  ]  )  { 
this  (  array  .  length  ,  array  [  0  ]  .  length  ,  ARRAY_2D  )  ; 
matrix  =  array  ; 
} 





public   IntegerMatrix  (  final   IntegerVector   array  [  ]  )  { 
this  (  array  [  0  ]  .  dimension  (  )  ,  array  .  length  )  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  matrix  [  i  ]  [  j  ]  =  array  [  j  ]  .  getComponent  (  i  )  ; 
} 
} 





protected   void   finalize  (  )  throws   Throwable  { 
matrix  =  null  ; 
super  .  finalize  (  )  ; 
} 





public   boolean   equals  (  Object   m  )  { 
if  (  m  !=  null  &&  (  m   instanceof   IntegerMatrix  )  &&  numRows  ==  (  (  IntegerMatrix  )  m  )  .  rows  (  )  &&  numCols  ==  (  (  IntegerMatrix  )  m  )  .  columns  (  )  )  { 
final   IntegerMatrix   im  =  (  IntegerMatrix  )  m  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  { 
if  (  matrix  [  i  ]  [  j  ]  !=  im  .  getElement  (  i  ,  j  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 




public   String   toString  (  )  { 
final   StringBuffer   buf  =  new   StringBuffer  (  numRows  *  numCols  )  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  { 
buf  .  append  (  matrix  [  i  ]  [  j  ]  )  ; 
buf  .  append  (  ' '  )  ; 
} 
buf  .  append  (  '\n'  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 




public   int   hashCode  (  )  { 
return  (  int  )  Math  .  exp  (  infNorm  (  )  )  ; 
} 





public   DoubleMatrix   toDoubleMatrix  (  )  { 
final   double   ans  [  ]  [  ]  =  new   double  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  ans  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   DoubleMatrix  (  ans  )  ; 
} 





public   ComplexMatrix   toComplexMatrix  (  )  { 
final   double   arrayRe  [  ]  [  ]  =  new   double  [  numRows  ]  [  numCols  ]  ; 
final   double   arrayIm  [  ]  [  ]  =  new   double  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  arrayRe  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   ComplexMatrix  (  arrayRe  ,  arrayIm  )  ; 
} 







public   int   getElement  (  final   int   i  ,  final   int   j  )  { 
if  (  i  >=  0  &&  i  <  numRows  &&  j  >=  0  &&  j  <  numCols  )  return   matrix  [  i  ]  [  j  ]  ;  else   throw   new   MatrixDimensionException  (  getInvalidElementMsg  (  i  ,  j  )  )  ; 
} 








public   void   setElement  (  final   int   i  ,  final   int   j  ,  final   int   x  )  { 
if  (  i  >=  0  &&  i  <  numRows  &&  j  >=  0  &&  j  <  numCols  )  matrix  [  i  ]  [  j  ]  =  x  ;  else   throw   new   MatrixDimensionException  (  getInvalidElementMsg  (  i  ,  j  )  )  ; 
} 





public   int   infNorm  (  )  { 
int   result  =  0  ,  tmpResult  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
tmpResult  =  0  ; 
for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  tmpResult  +=  Math  .  abs  (  matrix  [  i  ]  [  j  ]  )  ; 
if  (  tmpResult  >  result  )  result  =  tmpResult  ; 
} 
return   result  ; 
} 




public   double   frobeniusNorm  (  )  { 
double   result  =  0  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  for  (  j  =  0  ;  j  <  numCols  ;  j  ++  )  result  =  ExtraMath  .  hypot  (  result  ,  matrix  [  i  ]  [  j  ]  )  ; 
return   result  ; 
} 




public   AbelianGroup  .  Member   negate  (  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  -  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  -  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerMatrix  (  array  )  ; 
} 




public   AbelianGroup  .  Member   add  (  final   AbelianGroup  .  Member   m  )  { 
if  (  m   instanceof   IntegerMatrix  )  return   add  (  (  IntegerMatrix  )  m  )  ;  else   throw   new   IllegalArgumentException  (  "Member class not recognised by this method."  )  ; 
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
return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 

private   IntegerMatrix   rawAdd  (  final   IntegerMatrix   m  )  { 
if  (  numRows  ==  m  .  numRows  &&  numCols  ==  m  .  numCols  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  +  m  .  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  +  m  .  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 




public   AbelianGroup  .  Member   subtract  (  final   AbelianGroup  .  Member   m  )  { 
if  (  m   instanceof   IntegerMatrix  )  return   subtract  (  (  IntegerMatrix  )  m  )  ;  else   throw   new   IllegalArgumentException  (  "Member class not recognised by this method."  )  ; 
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
return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 
} 

private   IntegerMatrix   rawSubtract  (  final   IntegerMatrix   m  )  { 
if  (  numRows  ==  m  .  numRows  &&  numCols  ==  m  .  numCols  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  matrix  [  i  ]  [  0  ]  -  m  .  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  matrix  [  i  ]  [  j  ]  -  m  .  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Matrices are different sizes."  )  ; 
} 




public   Module  .  Member   scalarMultiply  (  Ring  .  Member   x  )  { 
if  (  x   instanceof   MathInteger  )  return   scalarMultiply  (  (  (  MathInteger  )  x  )  .  value  (  )  )  ;  else   throw   new   IllegalArgumentException  (  "Member class not recognised by this method."  )  ; 
} 






public   IntegerMatrix   scalarMultiply  (  final   int   x  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  numCols  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  i  ]  [  0  ]  =  x  *  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  i  ]  [  j  ]  =  x  *  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerMatrix  (  array  )  ; 
} 




public   VectorSpace  .  Member   scalarDivide  (  Field  .  Member   x  )  { 
throw   new   IllegalArgumentException  (  "Member class not recognised by this method."  )  ; 
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




public   Ring  .  Member   multiply  (  final   Ring  .  Member   m  )  { 
if  (  m   instanceof   IntegerMatrix  )  return   multiply  (  (  IntegerMatrix  )  m  )  ;  else   throw   new   IllegalArgumentException  (  "Member class not recognised by this method."  )  ; 
} 







public   IntegerMatrix   multiply  (  final   IntegerMatrix   m  )  { 
switch  (  m  .  storageFormat  )  { 
case   ARRAY_2D  : 
return   rawMultiply  (  m  )  ; 
default  : 
if  (  numCols  ==  m  .  rows  (  )  )  { 
int   n  ,  k  ; 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  m  .  columns  (  )  ]  ; 
for  (  int   j  =  0  ;  j  <  numRows  ;  j  ++  )  { 
for  (  k  =  0  ;  k  <  m  .  columns  (  )  ;  k  ++  )  { 
array  [  j  ]  [  k  ]  =  matrix  [  j  ]  [  0  ]  *  m  .  getElement  (  0  ,  k  )  ; 
for  (  n  =  1  ;  n  <  numCols  ;  n  ++  )  array  [  j  ]  [  k  ]  +=  matrix  [  j  ]  [  n  ]  *  m  .  getElement  (  n  ,  k  )  ; 
} 
} 
if  (  numRows  ==  m  .  columns  (  )  )  return   new   IntegerSquareMatrix  (  array  )  ;  else   return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Incompatible matrices."  )  ; 
} 
} 

private   IntegerMatrix   rawMultiply  (  final   IntegerMatrix   m  )  { 
if  (  numCols  ==  m  .  numRows  )  { 
int   n  ,  k  ; 
final   int   array  [  ]  [  ]  =  new   int  [  numRows  ]  [  m  .  numCols  ]  ; 
for  (  int   j  =  0  ;  j  <  numRows  ;  j  ++  )  { 
for  (  k  =  0  ;  k  <  m  .  numCols  ;  k  ++  )  { 
array  [  j  ]  [  k  ]  =  matrix  [  j  ]  [  0  ]  *  m  .  matrix  [  0  ]  [  k  ]  ; 
for  (  n  =  1  ;  n  <  numCols  ;  n  ++  )  array  [  j  ]  [  k  ]  +=  matrix  [  j  ]  [  n  ]  *  m  .  matrix  [  n  ]  [  k  ]  ; 
} 
} 
if  (  numRows  ==  m  .  numCols  )  return   new   IntegerSquareMatrix  (  array  )  ;  else   return   new   IntegerMatrix  (  array  )  ; 
}  else   throw   new   MatrixDimensionException  (  "Incompatible matrices."  )  ; 
} 





public   Matrix   transpose  (  )  { 
final   int   array  [  ]  [  ]  =  new   int  [  numCols  ]  [  numRows  ]  ; 
for  (  int   j  ,  i  =  0  ;  i  <  numRows  ;  i  ++  )  { 
array  [  0  ]  [  i  ]  =  matrix  [  i  ]  [  0  ]  ; 
for  (  j  =  1  ;  j  <  numCols  ;  j  ++  )  array  [  j  ]  [  i  ]  =  matrix  [  i  ]  [  j  ]  ; 
} 
return   new   IntegerMatrix  (  array  )  ; 
} 
} 

