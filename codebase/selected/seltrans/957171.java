package   org  .  xmlcml  .  euclidnew  ; 

import   org  .  apache  .  log4j  .  Logger  ; 













public   class   RealSquareMatrix   extends   RealMatrix  { 


public   enum   Type  { 


UPPER_TRIANGLE  (  1  )  , 
LOWER_TRIANGLE  (  2  )  , 
SYMMETRIC  (  3  )  , 
DIAGONAL  (  4  )  , 
OUTER_PRODUCT  (  5  )  , 
UNKNOWN  (  6  )  ; 


public   int   i  ; 

private   Type  (  int   i  )  { 
this  .  i  =  i  ; 
} 
} 

static   final   Logger   logger  =  Logger  .  getLogger  (  RealSquareMatrix  .  class  )  ; 




public   RealSquareMatrix  (  )  { 
super  (  )  ; 
} 







public   RealSquareMatrix  (  int   rows  )  { 
super  (  rows  ,  rows  )  ; 
} 








public   static   RealSquareMatrix   outerProduct  (  RealArray   f  )  { 
int   rows  =  f  .  size  (  )  ; 
RealSquareMatrix   temp  =  new   RealSquareMatrix  (  rows  )  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  rows  ;  j  ++  )  { 
temp  .  flmat  [  i  ]  [  j  ]  =  f  .  elementAt  (  i  )  *  f  .  elementAt  (  j  )  ; 
} 
} 
return   temp  ; 
} 








public   static   RealSquareMatrix   diagonal  (  RealArray   f  )  { 
int   rows  =  f  .  size  (  )  ; 
RealSquareMatrix   temp  =  new   RealSquareMatrix  (  rows  )  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
temp  .  flmat  [  i  ]  [  i  ]  =  f  .  elementAt  (  i  )  ; 
} 
return   temp  ; 
} 













public   RealSquareMatrix  (  int   rows  ,  double  [  ]  array  )  throws   EuclidRuntimeException  { 
super  (  rows  ,  rows  ,  array  )  ; 
} 









public   RealSquareMatrix  (  int   rows  ,  double   f  )  { 
super  (  rows  ,  rows  ,  f  )  ; 
} 
















public   RealSquareMatrix  (  RealMatrix   m  ,  int   lowrow  ,  int   lowcol  ,  int   rows  )  throws   EuclidRuntimeException  { 
super  (  m  ,  lowrow  ,  lowrow  +  rows  -  1  ,  lowcol  ,  lowcol  +  rows  -  1  )  ; 
} 







public   RealSquareMatrix  (  RealSquareMatrix   m  )  { 
super  (  m  )  ; 
} 












public   RealSquareMatrix  (  RealMatrix   m  )  throws   EuclidRuntimeException  { 
super  (  m  .  rows  ,  m  .  cols  )  ; 
if  (  m  .  cols  !=  m  .  rows  )  { 
throw   new   EuclidRuntimeException  (  "non square matrix"  )  ; 
} 
this  .  flmat  =  m  .  flmat  ; 
} 












public   RealSquareMatrix  (  double  [  ]  [  ]  matrix  )  throws   EuclidRuntimeException  { 
super  (  matrix  )  ; 
if  (  cols  !=  rows  )  { 
throw   new   EuclidRuntimeException  (  "non square matrix"  )  ; 
} 
} 










public   void   shallowCopy  (  RealSquareMatrix   m  )  throws   EuclidRuntimeException  { 
super  .  shallowCopy  (  (  RealMatrix  )  m  )  ; 
} 






public   static   RealSquareMatrix   fromLowerTriangle  (  RealArray   f  )  { 
int   n  =  f  .  size  (  )  ; 
int   rows  =  (  int  )  Math  .  round  (  (  Math  .  sqrt  (  8  *  n  +  1  )  -  1  +  0.001  )  /  2.  )  ; 
if  (  (  rows  *  (  rows  +  1  )  )  /  2  !=  n  )  { 
throw   new   RuntimeException  (  "band number of values ("  +  n  +  ") for lower Triangle"  )  ; 
} 
RealSquareMatrix   temp  =  new   RealSquareMatrix  (  rows  )  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <=  i  ;  j  ++  )  { 
temp  .  flmat  [  i  ]  [  j  ]  =  f  .  elementAt  (  count  )  ; 
if  (  i  !=  j  )  { 
temp  .  flmat  [  j  ]  [  i  ]  =  0.0  ; 
} 
count  ++  ; 
} 
} 
return   temp  ; 
} 






public   static   RealSquareMatrix   fromUpperTriangle  (  RealArray   f  )  { 
int   n  =  f  .  size  (  )  ; 
int   rows  =  (  int  )  Math  .  round  (  (  Math  .  sqrt  (  8  *  n  +  1  )  -  1  +  0.001  )  /  2.  )  ; 
if  (  (  rows  *  (  rows  +  1  )  )  /  2  !=  n  )  { 
throw   new   RuntimeException  (  "band number of values ("  +  n  +  ") for lower Triangle"  )  ; 
} 
RealSquareMatrix   temp  =  new   RealSquareMatrix  (  rows  )  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  i  ;  j  <  rows  ;  j  ++  )  { 
temp  .  flmat  [  i  ]  [  j  ]  =  f  .  elementAt  (  count  )  ; 
if  (  i  !=  j  )  { 
temp  .  flmat  [  j  ]  [  i  ]  =  0.0  ; 
} 
count  ++  ; 
} 
} 
return   temp  ; 
} 








public   boolean   isEqualTo  (  RealSquareMatrix   r  )  { 
return   super  .  isEqualTo  (  (  RealMatrix  )  r  )  ; 
} 











public   RealSquareMatrix   plus  (  RealSquareMatrix   m  )  throws   EuclidRuntimeException  { 
RealMatrix   temp  =  super  .  plus  (  (  RealMatrix  )  m  )  ; 
RealSquareMatrix   sqm  =  new   RealSquareMatrix  (  temp  )  ; 
return   sqm  ; 
} 











public   RealSquareMatrix   subtract  (  RealSquareMatrix   m  )  throws   EuclidRuntimeException  { 
RealMatrix   temp  =  super  .  subtract  (  (  RealMatrix  )  m  )  ; 
RealSquareMatrix   sqm  =  new   RealSquareMatrix  (  temp  )  ; 
return   sqm  ; 
} 













public   RealSquareMatrix   multiply  (  RealSquareMatrix   m  )  throws   EuclidRuntimeException  { 
RealMatrix   temp  =  super  .  multiply  (  (  RealMatrix  )  m  )  ; 
RealSquareMatrix   sqm  =  new   RealSquareMatrix  (  temp  )  ; 
return   sqm  ; 
} 







public   double   determinant  (  )  { 
double   det  =  0.0  ; 
if  (  rows  ==  1  )  { 
det  =  flmat  [  0  ]  [  0  ]  ; 
}  else   if  (  rows  ==  2  )  { 
det  =  flmat  [  0  ]  [  0  ]  *  flmat  [  1  ]  [  1  ]  -  flmat  [  1  ]  [  0  ]  *  flmat  [  0  ]  [  1  ]  ; 
}  else   if  (  rows  ==  3  )  { 
det  =  flmat  [  0  ]  [  0  ]  *  (  flmat  [  1  ]  [  1  ]  *  flmat  [  2  ]  [  2  ]  -  flmat  [  1  ]  [  2  ]  *  flmat  [  2  ]  [  1  ]  )  +  flmat  [  0  ]  [  1  ]  *  (  flmat  [  1  ]  [  2  ]  *  flmat  [  2  ]  [  0  ]  -  flmat  [  1  ]  [  0  ]  *  flmat  [  2  ]  [  2  ]  )  +  flmat  [  0  ]  [  2  ]  *  (  flmat  [  1  ]  [  0  ]  *  flmat  [  2  ]  [  1  ]  -  flmat  [  1  ]  [  1  ]  *  flmat  [  2  ]  [  0  ]  )  ; 
}  else  { 
int   sign  =  1  ; 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
det  +=  sign  *  flmat  [  0  ]  [  j  ]  *  minorDet  (  j  )  ; 
sign  =  -  sign  ; 
} 
} 
return   det  ; 
} 

private   double   minorDet  (  int   ii  )  { 
int   r  =  rows  -  1  ; 
double   array  [  ]  =  new   double  [  r  *  r  ]  ; 
int   countN  =  0  ; 
for  (  int   i  =  1  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
if  (  j  !=  ii  )  { 
array  [  countN  ++  ]  =  flmat  [  i  ]  [  j  ]  ; 
} 
} 
} 
RealSquareMatrix   mm  =  null  ; 
try  { 
mm  =  new   RealSquareMatrix  (  r  ,  array  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   EuclidRuntimeException  (  e  .  toString  (  )  )  ; 
} 
double   d  =  mm  .  determinant  (  )  ; 
return   d  ; 
} 






public   double   trace  (  )  { 
double   trace  =  0.0  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
trace  +=  flmat  [  i  ]  [  i  ]  ; 
} 
return   trace  ; 
} 






public   boolean   isUnit  (  )  { 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  rows  ;  j  ++  )  { 
double   f  =  flmat  [  i  ]  [  j  ]  ; 
if  (  (  !  Real  .  isZero  (  f  ,  Real  .  getEpsilon  (  )  )  &&  (  i  !=  j  )  )  ||  (  !  Real  .  isEqual  (  f  ,  1.0  ,  Real  .  getEpsilon  (  )  )  &&  (  i  ==  j  )  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 






public   boolean   isSymmetric  (  )  { 
for  (  int   i  =  0  ;  i  <  rows  -  1  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  rows  ;  j  ++  )  { 
if  (  !  Real  .  isEqual  (  flmat  [  i  ]  [  j  ]  ,  flmat  [  j  ]  [  i  ]  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 










public   RealSquareMatrix   orthonormalize  (  )  { 
if  (  cols  ==  1  )  { 
flmat  [  0  ]  [  0  ]  =  1.  ; 
}  else   if  (  cols  ==  2  )  { 
Vector3   v1  =  new   Vector3  (  flmat  [  0  ]  [  0  ]  ,  flmat  [  0  ]  [  1  ]  ,  0.  )  ; 
v1  =  v1  .  normalize  (  )  ; 
Vector3   v2  =  new   Vector3  (  flmat  [  1  ]  [  0  ]  ,  flmat  [  1  ]  [  1  ]  ,  0.  )  ; 
v2  =  v2  .  normalize  (  )  ; 
Vector3   v3  =  v1  .  cross  (  v2  )  ; 
v2  =  v3  .  cross  (  v1  )  ; 
v2  =  v2  .  normalize  (  )  ; 
flmat  [  0  ]  [  0  ]  =  v1  .  flarray  [  0  ]  ; 
flmat  [  0  ]  [  1  ]  =  v1  .  flarray  [  1  ]  ; 
flmat  [  1  ]  [  0  ]  =  v2  .  flarray  [  0  ]  ; 
flmat  [  1  ]  [  1  ]  =  v2  .  flarray  [  1  ]  ; 
}  else   if  (  cols  ==  3  )  { 
Vector3   v0  =  new   Vector3  (  extractRowData  (  0  )  )  ; 
Vector3   v1  =  new   Vector3  (  extractRowData  (  1  )  )  ; 
Vector3   v2  =  new   Vector3  (  extractRowData  (  2  )  )  ; 
double   det  =  v0  .  getScalarTripleProduct  (  v1  ,  v2  )  ; 
v0  .  normalize  (  )  ; 
v2  =  v0  .  cross  (  v1  )  ; 
v2  .  normalize  (  )  ; 
v1  =  v2  .  cross  (  v0  )  ; 
if  (  det  <  0.0  )  { 
v2  .  negative  (  )  ; 
} 
replaceRowData  (  0  ,  v0  .  getArray  (  )  )  ; 
replaceRowData  (  1  ,  v1  .  getArray  (  )  )  ; 
replaceRowData  (  2  ,  v2  .  getArray  (  )  )  ; 
}  else  { 
throw   new   EuclidRuntimeException  (  "Sorry: orthonormalise only up to 3x3 matrices: had "  +  cols  +  " columns"  )  ; 
} 
return   this  ; 
} 








public   boolean   isOrthonormal  (  )  { 
return   isUnitary  (  )  ; 
} 






public   boolean   isUpperTriangular  (  )  { 
for  (  int   i  =  1  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
if  (  !  Real  .  isZero  (  flmat  [  i  ]  [  j  ]  ,  Real  .  getEpsilon  (  )  )  )  return   false  ; 
} 
} 
return   true  ; 
} 






public   boolean   isLowerTriangular  (  )  { 
for  (  int   i  =  0  ;  i  <  rows  -  1  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  rows  ;  j  ++  )  { 
if  (  !  Real  .  isZero  (  flmat  [  i  ]  [  j  ]  ,  Real  .  getEpsilon  (  )  )  )  return   false  ; 
} 
} 
return   true  ; 
} 

double   rowDotproduct  (  int   row1  ,  int   row2  )  { 
double   sum  =  0.0  ; 
for  (  int   i  =  0  ;  i  <  cols  ;  i  ++  )  { 
sum  +=  flmat  [  row1  ]  [  i  ]  *  flmat  [  row2  ]  [  i  ]  ; 
} 
return   sum  ; 
} 








public   boolean   isOrthogonal  (  )  { 
for  (  int   i  =  0  ;  i  <  rows  -  1  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  rows  ;  j  ++  )  { 
double   dp  =  rowDotproduct  (  i  ,  j  )  ; 
if  (  !  Real  .  isZero  (  dp  ,  Real  .  getEpsilon  (  )  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 







public   boolean   isImproperRotation  (  )  { 
double   f  =  determinant  (  )  ; 
return  (  Real  .  isEqual  (  f  ,  -  1.0  )  &&  isOrthogonal  (  )  )  ; 
} 








public   boolean   isUnitary  (  )  { 
double   f  =  determinant  (  )  ; 
double   fa  =  Math  .  abs  (  f  )  ; 
return  (  Real  .  isEqual  (  fa  ,  1.0  )  &&  isOrthogonal  (  )  )  ; 
} 






public   RealSquareMatrix   copyUpperToLower  (  )  { 
for  (  int   i  =  0  ;  i  <  cols  -  1  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  cols  ;  j  ++  )  { 
flmat  [  j  ]  [  i  ]  =  flmat  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 






public   RealSquareMatrix   copyLowerToUpper  (  )  { 
for  (  int   i  =  0  ;  i  <  cols  -  1  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  cols  ;  j  ++  )  { 
flmat  [  i  ]  [  j  ]  =  flmat  [  j  ]  [  i  ]  ; 
} 
} 
return   this  ; 
} 






public   RealArray   lowerTriangle  (  )  { 
int   n  =  rows  ; 
RealArray   triangle  =  new   RealArray  (  (  n  *  (  n  +  1  )  )  /  2  )  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <=  i  ;  j  ++  )  { 
triangle  .  setElementAt  (  count  ++  ,  flmat  [  i  ]  [  j  ]  )  ; 
} 
} 
return   triangle  ; 
} 




public   void   transpose  (  )  { 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
double   t  =  flmat  [  i  ]  [  j  ]  ; 
flmat  [  i  ]  [  j  ]  =  flmat  [  j  ]  [  i  ]  ; 
flmat  [  j  ]  [  i  ]  =  t  ; 
} 
} 
} 













public   int   diagonaliseAndReturnRank  (  RealArray   eigenvalues  ,  RealSquareMatrix   eigenvectors  ,  EuclidRuntimeException   illCond  )  throws   EuclidRuntimeException  { 
RealArray   lowert  =  this  .  lowerTriangle  (  )  ; 
double  [  ]  lower77  =  new   double  [  lowert  .  size  (  )  +  1  ]  ; 
System  .  arraycopy  (  lowert  .  getArray  (  )  ,  0  ,  lower77  ,  1  ,  lowert  .  size  (  )  )  ; 
int   order  =  rows  ; 
if  (  rows  <  2  )  { 
throw   new   EuclidRuntimeException  (  "need at least 2 rows"  )  ; 
} 
double  [  ]  eigenval77  =  new   double  [  rows  +  1  ]  ; 
double  [  ]  eigenvect77  =  new   double  [  rows  *  rows  +  1  ]  ; 
illCond  =  null  ; 
int   rank  =  Diagonalise  .  vneigl  (  order  ,  lower77  ,  eigenval77  ,  eigenvect77  ,  illCond  )  ; 
double  [  ]  eigenval  =  new   double  [  rows  ]  ; 
System  .  arraycopy  (  eigenval77  ,  1  ,  eigenval  ,  0  ,  rows  )  ; 
double  [  ]  eigenvect  =  new   double  [  rows  *  rows  ]  ; 
System  .  arraycopy  (  eigenvect77  ,  1  ,  eigenvect  ,  0  ,  rows  *  rows  )  ; 
eigenvalues  .  shallowCopy  (  new   RealArray  (  eigenval  )  )  ; 
eigenvectors  .  shallowCopy  (  new   RealSquareMatrix  (  rows  ,  eigenvect  )  )  ; 
return   rank  ; 
} 








public   void   orthogonalise  (  )  throws   EuclidRuntimeException  { 
if  (  cols  ==  3  )  { 
Vector3   v0  =  new   Vector3  (  extractRowData  (  0  )  )  ; 
Vector3   v1  =  new   Vector3  (  extractRowData  (  1  )  )  ; 
Vector3   v2  =  new   Vector3  (  extractRowData  (  2  )  )  ; 
double   l0  =  v0  .  getLength  (  )  ; 
double   l1  =  v1  .  getLength  (  )  ; 
double   l2  =  v2  .  getLength  (  )  ; 
double   det  =  v0  .  getScalarTripleProduct  (  v1  ,  v2  )  ; 
v0  .  normalize  (  )  ; 
v2  =  v0  .  cross  (  v1  )  ; 
v2  .  normalize  (  )  ; 
v1  =  v2  .  cross  (  v0  )  ; 
if  (  det  <  0.0  )  { 
v2  =  v2  .  negative  (  )  ; 
} 
v0  =  v0  .  multiplyBy  (  l0  )  ; 
v1  =  v1  .  multiplyBy  (  l1  )  ; 
v2  =  v2  .  multiplyBy  (  l2  )  ; 
replaceRowData  (  0  ,  v0  .  getArray  (  )  )  ; 
replaceRowData  (  1  ,  v1  .  getArray  (  )  )  ; 
replaceRowData  (  2  ,  v2  .  getArray  (  )  )  ; 
}  else  { 
throw   new   EuclidRuntimeException  (  "Sorry: orthogonalise only up to 3x3 matrices"  )  ; 
} 
} 











public   static   RealSquareMatrix   getCrystallographicOrthogonalisation  (  double  [  ]  celleng  ,  double  [  ]  angle  )  { 
RealSquareMatrix   orthMat  =  new   RealSquareMatrix  (  3  )  ; 
double   dtor  =  Math  .  PI  /  180.0  ; 
double   sina  =  Math  .  sin  (  dtor  *  angle  [  0  ]  )  ; 
double   cosa  =  Math  .  cos  (  dtor  *  angle  [  0  ]  )  ; 
double   sinb  =  Math  .  sin  (  dtor  *  angle  [  1  ]  )  ; 
double   cosb  =  Math  .  cos  (  dtor  *  angle  [  1  ]  )  ; 
double   cosg  =  Math  .  cos  (  dtor  *  angle  [  2  ]  )  ; 
double   cosgstar  =  (  cosa  *  cosb  -  cosg  )  /  (  sina  *  sinb  )  ; 
double   singstar  =  Math  .  sqrt  (  1.0  -  cosgstar  *  cosgstar  )  ; 
double  [  ]  [  ]  omat  =  orthMat  .  getMatrix  (  )  ; 
omat  [  0  ]  [  0  ]  =  celleng  [  0  ]  *  sinb  *  singstar  ; 
omat  [  0  ]  [  1  ]  =  0.0  ; 
omat  [  0  ]  [  2  ]  =  0.0  ; 
omat  [  1  ]  [  0  ]  =  -  celleng  [  0  ]  *  sinb  *  cosgstar  ; 
omat  [  1  ]  [  1  ]  =  celleng  [  1  ]  *  sina  ; 
omat  [  1  ]  [  2  ]  =  0.0  ; 
omat  [  2  ]  [  0  ]  =  celleng  [  0  ]  *  cosb  ; 
omat  [  2  ]  [  1  ]  =  celleng  [  1  ]  *  cosa  ; 
omat  [  2  ]  [  2  ]  =  celleng  [  2  ]  ; 
return   orthMat  ; 
} 









public   RealSquareMatrix   getInverse  (  )  throws   EuclidRuntimeException  { 
double  [  ]  [  ]  inv  =  new   double  [  rows  ]  [  rows  ]  ; 
double  [  ]  [  ]  temp  =  getMatrix  (  )  ; 
double   det  =  this  .  determinant  (  )  ; 
if  (  det  ==  0  )  { 
throw   new   EuclidRuntimeException  (  "Cannot invert matrix: determinant=0"  )  ; 
} 
double   detr  =  1  /  det  ; 
if  (  this  .  rows  ==  1  )  { 
inv  [  0  ]  [  0  ]  =  detr  ; 
}  else   if  (  this  .  rows  ==  2  )  { 
inv  [  0  ]  [  0  ]  =  detr  *  temp  [  1  ]  [  1  ]  ; 
inv  [  1  ]  [  0  ]  =  0  -  (  detr  *  temp  [  0  ]  [  1  ]  )  ; 
inv  [  0  ]  [  1  ]  =  0  -  (  detr  *  temp  [  1  ]  [  0  ]  )  ; 
inv  [  1  ]  [  1  ]  =  detr  *  temp  [  0  ]  [  0  ]  ; 
}  else   if  (  this  .  rows  ==  3  )  { 
inv  [  0  ]  [  0  ]  =  detr  *  (  temp  [  1  ]  [  1  ]  *  temp  [  2  ]  [  2  ]  -  temp  [  1  ]  [  2  ]  *  temp  [  2  ]  [  1  ]  )  ; 
inv  [  0  ]  [  1  ]  =  detr  *  (  temp  [  0  ]  [  2  ]  *  temp  [  2  ]  [  1  ]  -  temp  [  0  ]  [  1  ]  *  temp  [  2  ]  [  2  ]  )  ; 
inv  [  0  ]  [  2  ]  =  detr  *  (  temp  [  0  ]  [  1  ]  *  temp  [  1  ]  [  2  ]  -  temp  [  0  ]  [  2  ]  *  temp  [  1  ]  [  1  ]  )  ; 
inv  [  1  ]  [  0  ]  =  detr  *  (  temp  [  1  ]  [  2  ]  *  temp  [  2  ]  [  0  ]  -  temp  [  1  ]  [  0  ]  *  temp  [  2  ]  [  2  ]  )  ; 
inv  [  1  ]  [  1  ]  =  detr  *  (  temp  [  0  ]  [  0  ]  *  temp  [  2  ]  [  2  ]  -  temp  [  0  ]  [  2  ]  *  temp  [  2  ]  [  0  ]  )  ; 
inv  [  1  ]  [  2  ]  =  detr  *  (  temp  [  0  ]  [  2  ]  *  temp  [  1  ]  [  0  ]  -  temp  [  0  ]  [  0  ]  *  temp  [  1  ]  [  2  ]  )  ; 
inv  [  2  ]  [  0  ]  =  detr  *  (  temp  [  1  ]  [  0  ]  *  temp  [  2  ]  [  1  ]  -  temp  [  1  ]  [  1  ]  *  temp  [  2  ]  [  0  ]  )  ; 
inv  [  2  ]  [  1  ]  =  detr  *  (  temp  [  0  ]  [  1  ]  *  temp  [  2  ]  [  0  ]  -  temp  [  0  ]  [  0  ]  *  temp  [  2  ]  [  1  ]  )  ; 
inv  [  2  ]  [  2  ]  =  detr  *  (  temp  [  0  ]  [  0  ]  *  temp  [  1  ]  [  1  ]  -  temp  [  0  ]  [  1  ]  *  temp  [  1  ]  [  0  ]  )  ; 
}  else  { 
throw   new   EuclidRuntimeException  (  "Inverse of larger than 3x3 matricies: NYI"  )  ; 
} 
RealSquareMatrix   imat  =  new   RealSquareMatrix  (  inv  )  ; 
return   imat  ; 
} 




private   boolean   dopivot  (  double  [  ]  [  ]  A  ,  double  [  ]  [  ]  I  ,  int   diag  ,  int   nelem  )  { 
if  (  A  [  diag  ]  [  diag  ]  !=  0.0  )  return   true  ; 
int   i  ; 
for  (  i  =  diag  +  1  ;  i  <  nelem  ;  i  ++  )  { 
if  (  A  [  i  ]  [  diag  ]  !=  0.0  )  { 
double  [  ]  t  ; 
t  =  A  [  diag  ]  ; 
A  [  diag  ]  =  A  [  i  ]  ; 
A  [  i  ]  =  t  ; 
t  =  I  [  diag  ]  ; 
I  [  diag  ]  =  I  [  i  ]  ; 
I  [  i  ]  =  t  ; 
break  ; 
} 
} 
return   i  <  nelem  ; 
} 

@  SuppressWarnings  (  "unused"  ) 
private   void   matinv  (  double  [  ]  [  ]  A  ,  double  [  ]  [  ]  I  ,  int   nelem  )  throws   EuclidRuntimeException  { 
for  (  int   i  =  0  ;  i  <  nelem  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  nelem  ;  j  ++  )  { 
I  [  i  ]  [  j  ]  =  0.0  ; 
} 
I  [  i  ]  [  i  ]  =  1.0  ; 
} 
for  (  int   diag  =  0  ;  diag  <  nelem  ;  diag  ++  )  { 
if  (  !  dopivot  (  A  ,  I  ,  diag  ,  nelem  )  )  { 
throw   new   EuclidRuntimeException  (  "singular matrix"  )  ; 
} 
double   div  =  A  [  diag  ]  [  diag  ]  ; 
if  (  div  !=  1.0  )  { 
A  [  diag  ]  [  diag  ]  =  1.0  ; 
for  (  int   j  =  diag  +  1  ;  j  <  nelem  ;  j  ++  )  A  [  diag  ]  [  j  ]  /=  div  ; 
for  (  int   j  =  0  ;  j  <  nelem  ;  j  ++  )  I  [  diag  ]  [  j  ]  /=  div  ; 
} 
for  (  int   i  =  0  ;  i  <  nelem  ;  i  ++  )  { 
if  (  i  ==  diag  )  continue  ; 
double   sub  =  A  [  i  ]  [  diag  ]  ; 
if  (  sub  !=  0.0  )  { 
A  [  i  ]  [  diag  ]  =  0.0  ; 
for  (  int   j  =  diag  +  1  ;  j  <  nelem  ;  j  ++  )  A  [  i  ]  [  j  ]  -=  sub  *  A  [  diag  ]  [  j  ]  ; 
for  (  int   j  =  0  ;  j  <  nelem  ;  j  ++  )  I  [  i  ]  [  j  ]  -=  sub  *  I  [  diag  ]  [  j  ]  ; 
} 
} 
} 
} 
} 

class   Diagonalise   implements   EuclidConstants  { 

static   final   double   ZERO  =  0.0  ; 

static   final   double   ONE  =  1.0  ; 

static   final   double   TWO  =  2.0  ; 

static   final   double   SQRT2  =  Math  .  sqrt  (  TWO  )  ; 


















public   static   int   vneigl  (  int   order  ,  double  [  ]  a  ,  double  [  ]  eigval  ,  double  [  ]  eigvec  ,  EuclidRuntimeException   illCond  )  throws   EuclidRuntimeException  { 
double   cosx  ,  sinx  ,  xxyy  ,  cosx2  ,  sinx2  =  0.0  ; 
int   i  ,  j  ,  k  ,  l  ,  m  =  0  ; 
double   x  ,  y  ,  range  ,  anorm  ,  sincs  ,  anrmx  =  0.0  ; 
int   ia  ,  ij  ,  ll  ,  lm  ,  iq  ,  mm  ,  jq  ,  lq  ,  mq  ,  ind  ,  ilq  ,  imq  ,  ilr  ,  imr  =  0  ; 
double   thr  =  0.0  ; 
cosx  =  0.0  ; 
sinx  =  0.0  ; 
xxyy  =  0.0  ; 
cosx2  =  0.0  ; 
sinx2  =  0.0  ; 
i  =  0  ; 
j  =  0  ; 
k  =  0  ; 
l  =  0  ; 
m  =  0  ; 
x  =  0.0  ; 
y  =  0.0  ; 
range  =  0.0  ; 
anorm  =  0.0  ; 
sincs  =  0.0  ; 
anrmx  =  0.0  ; 
ia  =  0  ; 
ij  =  0  ; 
ll  =  0  ; 
lm  =  0  ; 
iq  =  0  ; 
mm  =  0  ; 
jq  =  0  ; 
lq  =  0  ; 
mq  =  0  ; 
ind  =  0  ; 
ilq  =  0  ; 
imq  =  0  ; 
ilr  =  0  ; 
imr  =  0  ; 
thr  =  0.0  ; 
RealSquareMatrix  .  logger  .  info  (  "O..."  +  order  )  ; 
if  (  order  <  2  )  { 
throw   new   EuclidRuntimeException  (  "order too small"  )  ; 
} 
range  =  1e-6f  ; 
iq  =  -  (  order  )  ; 
for  (  j  =  1  ;  j  <=  order  ;  ++  j  )  { 
iq  +=  order  ; 
for  (  i  =  1  ;  i  <=  order  ;  ++  i  )  { 
ij  =  iq  +  i  ; 
eigvec  [  ij  ]  =  ZERO  ; 
if  (  i  -  j  ==  0  )  { 
eigvec  [  ij  ]  =  ONE  ; 
} 
} 
} 
RealSquareMatrix  .  logger  .  info  (  "O "  +  order  )  ; 
anorm  =  ZERO  ; 
for  (  i  =  1  ;  i  <=  order  ;  ++  i  )  { 
for  (  j  =  i  ;  j  <=  order  ;  ++  j  )  { 
if  (  i  -  j  !=  0  )  { 
ia  =  i  +  (  j  *  j  -  j  )  /  2  ; 
anorm  +=  a  [  ia  ]  *  a  [  ia  ]  ; 
} 
} 
} 
if  (  anorm  >  ZERO  )  { 
anorm  =  Math  .  sqrt  (  anorm  )  *  SQRT2  ; 
anrmx  =  anorm  *  range  /  (  new   Double  (  order  )  .  doubleValue  (  )  )  ; 
ind  =  0  ; 
thr  =  anorm  ; 
while  (  true  )  { 
thr  /=  new   Double  (  order  )  .  doubleValue  (  )  ; 
while  (  true  )  { 
l  =  1  ; 
while  (  true  )  { 
m  =  l  +  1  ; 
while  (  true  )  { 
mq  =  (  m  *  m  -  m  )  /  2  ; 
lq  =  (  l  *  l  -  l  )  /  2  ; 
lm  =  l  +  mq  ; 
if  (  Math  .  abs  (  a  [  lm  ]  )  -  thr  >=  ZERO  )  { 
ind  =  1  ; 
ll  =  l  +  lq  ; 
mm  =  m  +  mq  ; 
x  =  (  a  [  ll  ]  -  a  [  mm  ]  )  *  .5f  ; 
y  =  -  a  [  lm  ]  /  Math  .  sqrt  (  a  [  lm  ]  *  a  [  lm  ]  +  x  *  x  )  ; 
if  (  x  <  ZERO  )  { 
y  =  -  y  ; 
} 
xxyy  =  ONE  -  y  *  y  ; 
if  (  xxyy  <  ZERO  )  { 
xxyy  =  ZERO  ; 
} 
sinx  =  y  /  Math  .  sqrt  (  (  Math  .  sqrt  (  xxyy  )  +  ONE  )  *  TWO  )  ; 
sinx2  =  sinx  *  sinx  ; 
cosx  =  Math  .  sqrt  (  ONE  -  sinx2  )  ; 
cosx2  =  cosx  *  cosx  ; 
sincs  =  sinx  *  cosx  ; 
ilq  =  order  *  (  l  -  1  )  ; 
imq  =  order  *  (  m  -  1  )  ; 
Diagonalise  .  subroutine  (  order  ,  a  ,  ilq  ,  imq  ,  l  ,  m  ,  lq  ,  mq  ,  ll  ,  lm  ,  mm  ,  sinx  ,  cosx  ,  eigval  ,  eigvec  ,  sincs  ,  sinx2  ,  cosx2  )  ; 
} 
if  (  m  ==  order  )  { 
break  ; 
} 
++  m  ; 
} 
if  (  l  ==  (  order  -  1  )  )  { 
RealSquareMatrix  .  logger  .  info  (  S_LSQUARE  +  l  +  EC  .  S_SLASH  +  order  +  EC  .  S_RSQUARE  )  ; 
break  ; 
} 
++  l  ; 
} 
RealSquareMatrix  .  logger  .  info  (  S_PLUS  +  l  +  EC  .  S_SLASH  +  ind  )  ; 
if  (  ind  !=  1  )  { 
break  ; 
} 
ind  =  0  ; 
} 
RealSquareMatrix  .  logger  .  info  (  "====================broke"  )  ; 
if  (  thr  -  anrmx  <=  ZERO  )  { 
break  ; 
} 
} 
} 
iq  =  -  (  order  )  ; 
for  (  i  =  1  ;  i  <=  order  ;  ++  i  )  { 
iq  +=  order  ; 
ll  =  i  +  (  i  *  i  -  i  )  /  2  ; 
jq  =  order  *  (  i  -  2  )  ; 
for  (  j  =  i  ;  j  <=  order  ;  ++  j  )  { 
jq  +=  order  ; 
mm  =  j  +  (  j  *  j  -  j  )  /  2  ; 
if  (  a  [  ll  ]  -  a  [  mm  ]  >=  ZERO  )  { 
continue  ; 
} 
x  =  a  [  ll  ]  ; 
a  [  ll  ]  =  a  [  mm  ]  ; 
a  [  mm  ]  =  x  ; 
for  (  k  =  1  ;  k  <=  order  ;  ++  k  )  { 
ilr  =  iq  +  k  ; 
imr  =  jq  +  k  ; 
x  =  eigvec  [  ilr  ]  ; 
eigvec  [  ilr  ]  =  eigvec  [  imr  ]  ; 
eigvec  [  imr  ]  =  x  ; 
} 
} 
} 
return   Diagonalise  .  eigtest  (  order  ,  a  ,  eigval  ,  range  )  ; 
} 

private   static   int   eigtest  (  int   order  ,  double  [  ]  a  ,  double  [  ]  eigval  ,  double   range  )  { 
int   rank  =  0  ; 
for  (  int   i  =  order  ;  i  >=  1  ;  --  i  )  { 
eigval  [  i  ]  =  a  [  (  i  *  i  +  i  )  /  2  ]  ; 
if  (  eigval  [  i  ]  <  ZERO  )  { 
break  ; 
}  else   if  (  eigval  [  i  ]  <  range  )  { 
RealSquareMatrix  .  logger  .  info  (  "SING"  )  ; 
break  ; 
}  else  { 
++  rank  ; 
} 
} 
return   rank  ; 
} 

static   void   subroutine  (  int   order  ,  double  [  ]  a  ,  int   ilq  ,  int   imq  ,  int   l  ,  int   m  ,  int   lq  ,  int   mq  ,  int   ll  ,  int   lm  ,  int   mm  ,  double   sinx  ,  double   cosx  ,  double  [  ]  eigval  ,  double  [  ]  eigvec  ,  double   sincs  ,  double   sinx2  ,  double   cosx2  )  { 
double   x  ,  y  ; 
for  (  int   i  =  1  ;  i  <=  order  ;  ++  i  )  { 
int   iq  =  (  i  *  i  -  i  )  /  2  ; 
if  (  i  !=  l  )  { 
int   im  ,  il  ; 
int   ivar2  =  i  -  m  ; 
if  (  ivar2  !=  0  )  { 
if  (  ivar2  <  0  )  { 
im  =  i  +  mq  ; 
}  else  { 
im  =  m  +  iq  ; 
} 
if  (  i  >=  l  )  { 
il  =  l  +  iq  ; 
}  else  { 
il  =  i  +  lq  ; 
} 
x  =  a  [  il  ]  *  cosx  -  a  [  im  ]  *  sinx  ; 
a  [  im  ]  =  a  [  il  ]  *  sinx  +  a  [  im  ]  *  cosx  ; 
a  [  il  ]  =  x  ; 
} 
int   ilr  =  ilq  +  i  ; 
int   imr  =  imq  +  i  ; 
x  =  eigvec  [  ilr  ]  *  cosx  -  eigvec  [  imr  ]  *  sinx  ; 
eigvec  [  imr  ]  =  eigvec  [  ilr  ]  *  sinx  +  eigvec  [  imr  ]  *  cosx  ; 
eigvec  [  ilr  ]  =  x  ; 
} 
x  =  a  [  lm  ]  *  2.f  *  sincs  ; 
y  =  a  [  ll  ]  *  cosx2  +  a  [  mm  ]  *  sinx2  -  x  ; 
x  =  a  [  ll  ]  *  sinx2  +  a  [  mm  ]  *  cosx2  +  x  ; 
a  [  lm  ]  =  (  a  [  ll  ]  -  a  [  mm  ]  )  *  sincs  +  a  [  lm  ]  *  (  cosx2  -  sinx2  )  ; 
a  [  ll  ]  =  y  ; 
a  [  mm  ]  =  x  ; 
} 
} 
} 

