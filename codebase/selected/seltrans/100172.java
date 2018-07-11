package   matrix  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StreamTokenizer  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  DecimalFormatSymbols  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Vector  ; 
import   util  .  Hypotenuse  ; 

public   class   Matrix   implements   Cloneable  ,  java  .  io  .  Serializable  { 









public   static   Matrix   constructWithCopy  (  double  [  ]  [  ]  A  )  { 
int   m  =  A  .  length  ; 
int   n  =  A  [  0  ]  .  length  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  A  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length."  )  ; 
} 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 










public   static   Matrix   identity  (  int   m  ,  int   n  )  { 
Matrix   A  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  X  [  i  ]  [  j  ]  =  (  i  ==  j  ?  1.0  :  0.0  )  ; 
} 
return   A  ; 
} 










public   static   Matrix   random  (  int   m  ,  int   n  )  { 
Matrix   A  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  Math  .  random  (  )  ; 
} 
} 
return   A  ; 
} 










public   static   Matrix   read  (  BufferedReader   input  )  throws   java  .  io  .  IOException  { 
StreamTokenizer   tokenizer  =  new   StreamTokenizer  (  input  )  ; 
tokenizer  .  resetSyntax  (  )  ; 
tokenizer  .  wordChars  (  0  ,  255  )  ; 
tokenizer  .  whitespaceChars  (  0  ,  ' '  )  ; 
tokenizer  .  eolIsSignificant  (  true  )  ; 
java  .  util  .  Vector  <  Object  >  v  =  new   java  .  util  .  Vector  <  Object  >  (  )  ; 
while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_EOL  )  ; 
if  (  tokenizer  .  ttype  ==  StreamTokenizer  .  TT_EOF  )  throw   new   java  .  io  .  IOException  (  "Unexpected EOF on matrix read."  )  ; 
do  { 
v  .  addElement  (  Double  .  valueOf  (  tokenizer  .  sval  )  )  ; 
}  while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  ; 
int   n  =  v  .  size  (  )  ; 
double   row  [  ]  =  new   double  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  row  [  j  ]  =  (  (  Double  )  v  .  elementAt  (  j  )  )  .  doubleValue  (  )  ; 
v  .  removeAllElements  (  )  ; 
v  .  addElement  (  row  )  ; 
while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  { 
v  .  addElement  (  row  =  new   double  [  n  ]  )  ; 
int   j  =  0  ; 
do  { 
if  (  j  >=  n  )  throw   new   java  .  io  .  IOException  (  "Row "  +  v  .  size  (  )  +  " is too long."  )  ; 
row  [  j  ++  ]  =  Double  .  valueOf  (  tokenizer  .  sval  )  .  doubleValue  (  )  ; 
}  while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  ; 
if  (  j  <  n  )  throw   new   java  .  io  .  IOException  (  "Row "  +  v  .  size  (  )  +  " is too short."  )  ; 
} 
int   m  =  v  .  size  (  )  ; 
double  [  ]  [  ]  A  =  new   double  [  m  ]  [  ]  ; 
v  .  copyInto  (  A  )  ; 
return   new   Matrix  (  A  )  ; 
} 










public   static   Matrix   zero  (  int   m  ,  int   n  )  { 
Matrix   A  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  X  [  i  ]  [  j  ]  =  0.0  ; 
} 
return   A  ; 
} 






private   double  [  ]  [  ]  A  ; 







private   int   m  ,  n  ; 












public   Matrix  (  double   vals  [  ]  ,  int   m  )  { 
this  .  m  =  m  ; 
n  =  (  m  !=  0  ?  vals  .  length  /  m  :  0  )  ; 
if  (  m  *  n  !=  vals  .  length  )  { 
throw   new   IllegalArgumentException  (  "Array length must be a multiple of m."  )  ; 
} 
A  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  vals  [  i  +  j  *  m  ]  ; 
} 
} 
} 










public   Matrix  (  double  [  ]  [  ]  A  )  { 
m  =  A  .  length  ; 
n  =  A  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  A  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length."  )  ; 
} 
} 
this  .  A  =  A  ; 
} 











public   Matrix  (  double  [  ]  [  ]  A  ,  int   m  ,  int   n  )  { 
this  .  A  =  A  ; 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
} 









public   Matrix  (  int   m  ,  int   n  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   double  [  m  ]  [  n  ]  ; 
} 











public   Matrix  (  int   m  ,  int   n  ,  double   s  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 
} 
} 








public   Matrix   arrayLeftDivide  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  /  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   Matrix   arrayLeftDivideEquals  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  /  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 








public   Matrix   arrayRightDivide  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   Matrix   arrayRightDivideEquals  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 








public   Matrix   arrayTimes  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  *  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   Matrix   arrayTimesEquals  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  *  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 


private   void   checkMatrixDimensions  (  Matrix   B  )  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 







public   CholeskyDecomposition   chol  (  )  { 
return   new   CholeskyDecomposition  (  this  )  ; 
} 




public   Object   clone  (  )  { 
return   this  .  copy  (  )  ; 
} 






public   double   cond  (  )  { 
return   new   SingularValueDecomposition  (  this  )  .  cond  (  )  ; 
} 




public   Matrix   copy  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 






public   double   det  (  )  { 
return   new   LUDecomposition  (  this  )  .  det  (  )  ; 
} 







public   EigenvalueDecomposition   eig  (  )  { 
return   new   EigenvalueDecomposition  (  this  )  ; 
} 











public   double   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 






public   double  [  ]  [  ]  getArray  (  )  { 
return   A  ; 
} 






public   double  [  ]  [  ]  getArrayCopy  (  )  { 
double  [  ]  [  ]  C  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   C  ; 
} 






public   int   getColumnDimension  (  )  { 
return   n  ; 
} 






public   double  [  ]  getColumnPackedCopy  (  )  { 
double  [  ]  vals  =  new   double  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  +  j  *  m  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
} 
















public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  { 
Matrix   X  =  new   Matrix  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
B  [  i  -  i0  ]  [  j  -  j0  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
return   X  ; 
} 














public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  { 
Matrix   X  =  new   Matrix  (  i1  -  i0  +  1  ,  c  .  length  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
B  [  i  -  i0  ]  [  j  ]  =  A  [  i  ]  [  c  [  j  ]  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
return   X  ; 
} 














public   Matrix   getMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  )  { 
Matrix   X  =  new   Matrix  (  r  .  length  ,  j1  -  j0  +  1  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
try  { 
for  (  int   i  =  0  ;  i  <  r  .  length  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
B  [  i  ]  [  j  -  j0  ]  =  A  [  r  [  i  ]  ]  [  j  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
return   X  ; 
} 












public   Matrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  { 
Matrix   X  =  new   Matrix  (  r  .  length  ,  c  .  length  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
try  { 
for  (  int   i  =  0  ;  i  <  r  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
B  [  i  ]  [  j  ]  =  A  [  r  [  i  ]  ]  [  c  [  j  ]  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
return   X  ; 
} 

public   Vector  <  Double  >  getRowAsVector  (  int   rowIndex  )  { 
Vector  <  Double  >  v  =  new   Vector  <  Double  >  (  )  ; 
for  (  double   k  :  A  [  rowIndex  ]  )  v  .  add  (  k  )  ; 
return   v  ; 
} 






public   int   getRowDimension  (  )  { 
return   m  ; 
} 






public   double  [  ]  getRowPackedCopy  (  )  { 
double  [  ]  vals  =  new   double  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  *  n  +  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
} 

private   void   swap  (  double  [  ]  [  ]  A  ,  int   i  ,  int   k  )  { 
int   m  =  A  [  0  ]  .  length  ; 
double   temp  ; 
for  (  int   q  =  0  ;  q  <  m  ;  q  ++  )  { 
temp  =  A  [  i  ]  [  q  ]  ; 
A  [  i  ]  [  q  ]  =  A  [  k  ]  [  q  ]  ; 
A  [  k  ]  [  q  ]  =  temp  ; 
} 
} 

private   void   divide  (  double  [  ]  [  ]  A  ,  int   i  ,  int   j  )  { 
int   m  =  A  [  0  ]  .  length  -  1  ; 
for  (  int   q  =  j  +  1  ;  q  <=  m  ;  q  ++  )  A  [  i  ]  [  q  ]  /=  A  [  i  ]  [  j  ]  ; 
A  [  i  ]  [  j  ]  =  1  ; 
} 

private   void   eliminate  (  double  [  ]  [  ]  A  ,  int   i  ,  int   j  )  { 
int   n  =  A  .  length  ; 
int   m  =  A  [  0  ]  .  length  ; 
for  (  int   p  =  0  ;  p  <  n  ;  p  ++  )  { 
if  (  p  !=  i  &&  A  [  p  ]  [  j  ]  !=  0  )  { 
for  (  int   q  =  j  +  1  ;  q  <  m  ;  q  ++  )  { 
A  [  p  ]  [  q  ]  -=  A  [  p  ]  [  j  ]  *  A  [  i  ]  [  q  ]  ; 
} 
A  [  p  ]  [  j  ]  =  0  ; 
} 
} 
} 

public   Matrix   gj  (  )  { 
Matrix   newMatrix  =  this  .  copy  (  )  ; 
int   i  =  0  ; 
int   j  =  0  ; 
while  (  i  <  newMatrix  .  getRowDimension  (  )  &&  j  <  newMatrix  .  getColumnDimension  (  )  )  { 
int   k  =  i  ; 
while  (  k  <  newMatrix  .  getRowDimension  (  )  &&  newMatrix  .  A  [  k  ]  [  j  ]  ==  0  )  k  ++  ; 
if  (  k  <  newMatrix  .  getRowDimension  (  )  )  { 
if  (  k  !=  i  )  swap  (  newMatrix  .  A  ,  i  ,  k  )  ; 
if  (  A  [  i  ]  [  j  ]  !=  1  )  divide  (  newMatrix  .  A  ,  i  ,  j  )  ; 
eliminate  (  newMatrix  .  A  ,  i  ,  j  )  ; 
i  ++  ; 
} 
j  ++  ; 
} 
return   newMatrix  ; 
} 






public   Matrix   inverse  (  )  { 
return   solve  (  identity  (  m  ,  m  )  )  ; 
} 







public   LUDecomposition   lu  (  )  { 
return   new   LUDecomposition  (  this  )  ; 
} 








public   Matrix   minus  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   Matrix   minusEquals  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 






public   double   norm1  (  )  { 
double   f  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  Math  .  abs  (  A  [  i  ]  [  j  ]  )  ; 
} 
f  =  Math  .  max  (  f  ,  s  )  ; 
} 
return   f  ; 
} 






public   double   norm2  (  )  { 
return  (  new   SingularValueDecomposition  (  this  )  .  norm2  (  )  )  ; 
} 






public   double   normF  (  )  { 
double   f  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
f  =  Hypotenuse  .  hypot  (  f  ,  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   f  ; 
} 






public   double   normInf  (  )  { 
double   f  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
double   s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  +=  Math  .  abs  (  A  [  i  ]  [  j  ]  )  ; 
} 
f  =  Math  .  max  (  f  ,  s  )  ; 
} 
return   f  ; 
} 








public   Matrix   plus  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   Matrix   plusEquals  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 













public   void   print  (  NumberFormat   format  ,  int   width  )  { 
print  (  new   PrintWriter  (  System  .  out  ,  true  )  ,  format  ,  width  )  ; 
} 












public   void   print  (  PrintWriter   output  ,  int   w  ,  int   d  )  { 
DecimalFormat   format  =  new   DecimalFormat  (  )  ; 
format  .  setDecimalFormatSymbols  (  new   DecimalFormatSymbols  (  Locale  .  US  )  )  ; 
format  .  setMinimumIntegerDigits  (  1  )  ; 
format  .  setMaximumFractionDigits  (  d  )  ; 
format  .  setMinimumFractionDigits  (  d  )  ; 
format  .  setGroupingUsed  (  false  )  ; 
print  (  output  ,  format  ,  w  +  2  )  ; 
} 















public   void   print  (  PrintWriter   output  ,  NumberFormat   format  ,  int   width  )  { 
output  .  println  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
String   s  =  format  .  format  (  A  [  i  ]  [  j  ]  )  ; 
int   padding  =  Math  .  max  (  1  ,  width  -  s  .  length  (  )  )  ; 
for  (  int   k  =  0  ;  k  <  padding  ;  k  ++  )  output  .  print  (  ' '  )  ; 
output  .  print  (  s  )  ; 
} 
output  .  println  (  )  ; 
} 
output  .  println  (  )  ; 
} 







public   QRDecomposition   qr  (  )  { 
return   new   QRDecomposition  (  this  )  ; 
} 






public   int   rank  (  )  { 
return   new   SingularValueDecomposition  (  this  )  .  rank  (  )  ; 
} 












public   void   set  (  int   i  ,  int   j  ,  double   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 

















public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  Matrix   X  )  { 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  X  .  get  (  i  -  i0  ,  j  -  j0  )  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 















public   void   setMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  ,  Matrix   X  )  { 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
A  [  i  ]  [  c  [  j  ]  ]  =  X  .  get  (  i  -  i0  ,  j  )  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 















public   void   setMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  ,  Matrix   X  )  { 
try  { 
for  (  int   i  =  0  ;  i  <  r  .  length  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
A  [  r  [  i  ]  ]  [  j  ]  =  X  .  get  (  i  ,  j  -  j0  )  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 













public   void   setMatrix  (  int  [  ]  r  ,  int  [  ]  c  ,  Matrix   X  )  { 
try  { 
for  (  int   i  =  0  ;  i  <  r  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
A  [  r  [  i  ]  ]  [  c  [  j  ]  ]  =  X  .  get  (  i  ,  j  )  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 








public   Matrix   solve  (  Matrix   B  )  { 
return  (  m  ==  n  ?  (  new   LUDecomposition  (  this  )  )  .  solve  (  B  )  :  (  new   QRDecomposition  (  this  )  )  .  solve  (  B  )  )  ; 
} 








public   Matrix   solveTranspose  (  Matrix   B  )  { 
return   transpose  (  )  .  solve  (  B  .  transpose  (  )  )  ; 
} 







public   SingularValueDecomposition   svd  (  )  { 
return   new   SingularValueDecomposition  (  this  )  ; 
} 








public   Matrix   times  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 










public   Matrix   times  (  Matrix   B  )  { 
if  (  B  .  m  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix inner dimensions must agree."  )  ; 
} 
Matrix   X  =  new   Matrix  (  m  ,  B  .  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
double  [  ]  Bcolj  =  new   double  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  B  .  n  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
Bcolj  [  k  ]  =  B  .  A  [  k  ]  [  j  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
double  [  ]  Arowi  =  A  [  i  ]  ; 
double   s  =  0  ; 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
s  +=  Arowi  [  k  ]  *  Bcolj  [  k  ]  ; 
} 
C  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 








public   Matrix   timesEquals  (  double   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 

public   String   toString  (  )  { 
NumberFormat   format  =  NumberFormat  .  getInstance  (  )  ; 
int   width  =  10  ; 
String   output  =  new   String  (  "[\n"  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
String   s  ; 
if  (  A  [  i  ]  [  j  ]  !=  0  )  s  =  format  .  format  (  A  [  i  ]  [  j  ]  )  ;  else   s  =  "0"  ; 
int   padding  =  Math  .  max  (  1  ,  width  -  s  .  length  (  )  )  ; 
for  (  int   k  =  0  ;  k  <  padding  ;  k  ++  )  output  +=  ' '  ; 
output  +=  s  ; 
} 
output  +=  "\n"  ; 
} 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  width  ;  j  ++  )  output  +=  " "  ; 
} 
output  +=  "    ]"  ; 
return   output  ; 
} 

public   Vector  <  Vector  <  Double  >  >  toVector  (  )  { 
Vector  <  Vector  <  Double  >  >  matrix  =  new   Vector  <  Vector  <  Double  >  >  (  this  .  getRowDimension  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  this  .  getRowDimension  (  )  ;  i  ++  )  { 
Vector  <  Double  >  rowVector  =  new   Vector  <  Double  >  (  this  .  getColumnDimension  (  )  )  ; 
for  (  int   j  =  0  ;  j  <  this  .  getColumnDimension  (  )  ;  j  ++  )  rowVector  .  add  (  A  [  i  ]  [  j  ]  )  ; 
matrix  .  add  (  rowVector  )  ; 
} 
return   matrix  ; 
} 






public   double   trace  (  )  { 
double   t  =  0  ; 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  m  ,  n  )  ;  i  ++  )  { 
t  +=  A  [  i  ]  [  i  ]  ; 
} 
return   t  ; 
} 






public   Matrix   transpose  (  )  { 
Matrix   X  =  new   Matrix  (  n  ,  m  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  j  ]  [  i  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 






public   Matrix   uminus  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  -  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 

public   Matrix   augmentColumn  (  Matrix   augmentColumn  )  { 
double  [  ]  [  ]  newM  =  new   double  [  this  .  getRowDimension  (  )  ]  [  this  .  getColumnDimension  (  )  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  this  .  getRowDimension  (  )  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  this  .  getColumnDimension  (  )  ;  j  ++  )  { 
newM  [  i  ]  [  j  ]  =  this  .  A  [  i  ]  [  j  ]  ; 
} 
newM  [  i  ]  [  this  .  getColumnDimension  (  )  ]  =  augmentColumn  .  A  [  i  ]  [  0  ]  ; 
} 
return   new   Matrix  (  newM  )  ; 
} 
} 

