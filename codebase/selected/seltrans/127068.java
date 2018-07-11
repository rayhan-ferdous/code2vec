package   jmat  .  data  ; 

import   jmat  .  data  .  arrayTools  .  *  ; 
import   jmat  .  data  .  matrixDecompositions  .  *  ; 
import   jmat  .  function  .  DoubleFunction  ; 
import   jmat  .  io  .  data  .  MatrixFile  ; 
import   jmat  .  io  .  data  .  MatrixMMLFile  ; 
import   jmat  .  io  .  data  .  fileTools  .  MatrixMML  ; 
import   jmat  .  io  .  data  .  fileTools  .  MatrixString  ; 
import   jmat  .  io  .  gui  .  FrameView  ; 
import   jmat  .  io  .  gui  .  MatrixPlot2D  ; 
import   jmat  .  io  .  gui  .  MatrixPlot3D  ; 
import   jmat  .  io  .  gui  .  MatrixTable  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  Serializable  ; 
import   org  .  jdom  .  Element  ; 










public   class   Matrix   implements   Cloneable  ,  Serializable  { 




protected   double  [  ]  [  ]  A  ; 





protected   int   m  ; 





protected   int   n  ; 





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






public   Matrix  (  double  [  ]  [  ]  B  )  { 
m  =  B  .  length  ; 
n  =  B  [  0  ]  .  length  ; 
A  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  B  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length : "  +  n  )  ; 
} 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  B  [  i  ]  [  j  ]  ; 
} 
} 
} 








public   Matrix  (  double  [  ]  [  ]  B  ,  int   m  ,  int   n  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  B  [  i  ]  .  length  <  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have a length >= "  +  n  )  ; 
} 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  B  [  i  ]  [  j  ]  ; 
} 
} 
} 






public   Matrix  (  double  [  ]  vals  ,  int   m  )  { 
this  .  m  =  m  ; 
n  =  (  (  m  !=  0  )  ?  (  vals  .  length  /  m  )  :  0  )  ; 
if  (  (  m  *  n  )  !=  vals  .  length  )  { 
throw   new   IllegalArgumentException  (  "Array length must be a multiple of "  +  m  )  ; 
} 
A  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  vals  [  i  +  (  j  *  m  )  ]  ; 
} 
} 
} 





public   static   void   checkIndicesDimensions  (  int  [  ]  [  ]  i  ,  int  [  ]  [  ]  j  )  { 
if  (  (  i  .  length  !=  j  .  length  )  ||  (  i  [  0  ]  .  length  !=  j  [  0  ]  .  length  )  )  { 
throw   new   IllegalArgumentException  (  "Indices dimensions must equals"  )  ; 
} 
} 





public   static   void   checkIndicesLengths  (  int  [  ]  i  ,  int  [  ]  j  )  { 
if  (  i  .  length  !=  j  .  length  )  { 
throw   new   IllegalArgumentException  (  "Indices lenghts must equals"  )  ; 
} 
} 






public   static   Matrix   identity  (  int   m  ,  int   n  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  (  (  i  ==  j  )  ?  1.0  :  0.0  )  ; 
} 
} 
return   X  ; 
} 








public   static   Matrix   increment  (  int   m  ,  int   n  ,  double   begin  ,  double   pitch  )  { 
return   incrementRows  (  m  ,  n  ,  begin  ,  pitch  )  ; 
} 








public   static   Matrix   incrementColumns  (  int   m  ,  int   n  ,  double   begin  ,  double   pitch  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  begin  +  (  j  *  pitch  )  ; 
} 
} 
return   X  ; 
} 








public   static   Matrix   incrementRows  (  int   m  ,  int   n  ,  double   begin  ,  double   pitch  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  begin  +  (  i  *  pitch  )  ; 
} 
} 
return   X  ; 
} 





public   static   Matrix   merge  (  Matrix  [  ]  Xs  )  { 
return   mergeRows  (  Xs  )  ; 
} 





public   static   Matrix   mergeColumns  (  Matrix  [  ]  Xs  )  { 
int   m  =  Xs  [  0  ]  .  m  ; 
int   N  =  0  ; 
for  (  int   k  =  0  ;  k  <  Xs  .  length  ;  k  ++  )  { 
N  =  N  +  Xs  [  k  ]  .  n  ; 
} 
Matrix   X  =  new   Matrix  (  m  ,  N  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
int   ind  =  0  ; 
for  (  int   k  =  0  ;  k  <  Xs  .  length  ;  k  ++  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  Xs  [  k  ]  .  n  ;  j  ++  )  { 
C  [  i  ]  [  j  +  ind  ]  =  Xs  [  k  ]  .  A  [  i  ]  [  j  ]  ; 
} 
} 
ind  =  ind  +  Xs  [  k  ]  .  n  ; 
} 
return   X  ; 
} 





public   static   Matrix   mergeRows  (  Matrix  [  ]  Xs  )  { 
int   n  =  Xs  [  0  ]  .  n  ; 
int   M  =  0  ; 
for  (  int   k  =  0  ;  k  <  Xs  .  length  ;  k  ++  )  { 
M  =  M  +  Xs  [  k  ]  .  m  ; 
} 
Matrix   X  =  new   Matrix  (  M  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
int   ind  =  0  ; 
for  (  int   k  =  0  ;  k  <  Xs  .  length  ;  k  ++  )  { 
for  (  int   i  =  0  ;  i  <  Xs  [  k  ]  .  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  +  ind  ]  [  j  ]  =  Xs  [  k  ]  .  A  [  i  ]  [  j  ]  ; 
} 
} 
ind  =  ind  +  Xs  [  k  ]  .  m  ; 
} 
return   X  ; 
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





public   void   setColumn  (  int   c  ,  Matrix   B  )  { 
B  .  checkMatrixDimensions  (  m  ,  1  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
A  [  i  ]  [  c  ]  =  B  .  A  [  i  ]  [  0  ]  ; 
} 
} 





public   Matrix   getColumn  (  int   c  )  { 
Matrix   X  =  new   Matrix  (  m  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
C  [  i  ]  [  0  ]  =  A  [  i  ]  [  c  ]  ; 
} 
return   X  ; 
} 





public   double  [  ]  getColumnArrayCopy  (  int   c  )  { 
double  [  ]  C  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
C  [  i  ]  =  A  [  i  ]  [  c  ]  ; 
} 
return   C  ; 
} 




public   int   getColumnDimension  (  )  { 
return   n  ; 
} 




public   double  [  ]  getColumnPackedCopy  (  )  { 
double  [  ]  C  =  new   double  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  +  (  j  *  m  )  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   C  ; 
} 





public   void   setColumns  (  int  [  ]  c  ,  Matrix   B  )  { 
B  .  checkMatrixDimensions  (  m  ,  c  .  length  )  ; 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
A  [  i  ]  [  c  [  j  ]  ]  =  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
} 





public   Matrix   getColumns  (  int  [  ]  c  )  { 
Matrix   X  =  new   Matrix  (  m  ,  c  .  length  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   j  =  0  ;  j  <  c  .  length  ;  j  ++  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  c  [  j  ]  ]  ; 
} 
} 
return   X  ; 
} 







public   void   setMatrix  (  int   i0  ,  int   j0  ,  Matrix   X  )  { 
try  { 
for  (  int   i  =  i0  ;  i  <  (  i0  +  X  .  m  )  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <  (  j0  +  X  .  n  )  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  X  .  A  [  i  -  i0  ]  [  j  -  j0  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 







public   void   setMatrix  (  int  [  ]  I0  ,  int  [  ]  J0  ,  Matrix   X  )  { 
checkIndicesLengths  (  I0  ,  J0  )  ; 
for  (  int   k  =  0  ;  k  <  I0  .  length  ;  k  ++  )  { 
int   i0  =  I0  [  k  ]  ; 
int   j0  =  J0  [  k  ]  ; 
try  { 
for  (  int   i  =  i0  ;  i  <  (  i0  +  X  .  m  )  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <  (  j0  +  X  .  n  )  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  X  .  A  [  i  -  i0  ]  [  j  -  j0  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 
} 









public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  double   v  )  { 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  v  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 









public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  Matrix   X  )  { 
X  .  checkMatrixDimensions  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
try  { 
for  (  int   i  =  i0  ;  i  <=  i1  ;  i  ++  )  { 
for  (  int   j  =  j0  ;  j  <=  j1  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  X  .  A  [  i  -  i0  ]  [  j  -  j0  ]  ; 
} 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
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





public   void   setRow  (  int   l  ,  Matrix   B  )  { 
B  .  checkMatrixDimensions  (  1  ,  n  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  l  ]  [  j  ]  =  B  .  A  [  0  ]  [  j  ]  ; 
} 
} 





public   Matrix   getRow  (  int   l  )  { 
Matrix   X  =  new   Matrix  (  1  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  0  ]  [  j  ]  =  A  [  l  ]  [  j  ]  ; 
} 
return   X  ; 
} 





public   double  [  ]  getRowArrayCopy  (  int   l  )  { 
double  [  ]  C  =  new   double  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
C  [  i  ]  =  A  [  l  ]  [  i  ]  ; 
} 
return   C  ; 
} 




public   int   getRowDimension  (  )  { 
return   m  ; 
} 




public   double  [  ]  getRowPackedCopy  (  )  { 
double  [  ]  C  =  new   double  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  (  i  *  n  )  +  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   C  ; 
} 





public   void   setRows  (  int  [  ]  l  ,  Matrix   B  )  { 
B  .  checkMatrixDimensions  (  l  .  length  ,  n  )  ; 
for  (  int   i  =  0  ;  i  <  l  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  l  [  i  ]  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
} 





public   Matrix   getRows  (  int  [  ]  l  )  { 
Matrix   X  =  new   Matrix  (  l  .  length  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  l  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  l  [  i  ]  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 




public   void   checkColumnDimension  (  Matrix   B  )  { 
if  (  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix Columns dimensions must equals "  +  B  .  n  )  ; 
} 
} 




public   void   checkColumnDimension  (  int   column  )  { 
if  (  column  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix Columns dimensions must equals "  +  column  )  ; 
} 
} 




public   void   checkMatrixDimensions  (  Matrix   B  )  { 
if  (  (  B  .  m  !=  m  )  ||  (  B  .  n  !=  n  )  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must equals "  +  B  .  m  +  " x "  +  B  .  n  )  ; 
} 
} 





public   void   checkMatrixDimensions  (  int   m2  ,  int   n2  )  { 
if  (  (  m2  !=  m  )  ||  (  n2  !=  n  )  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must equals "  +  m2  +  " x "  +  n2  )  ; 
} 
} 




public   void   checkRowDimension  (  Matrix   B  )  { 
if  (  B  .  m  !=  m  )  { 
throw   new   IllegalArgumentException  (  "Matrix Rows dimensions must equals "  +  B  .  m  )  ; 
} 
} 




public   void   checkRowDimension  (  int   row  )  { 
if  (  row  !=  m  )  { 
throw   new   IllegalArgumentException  (  "Matrix Rows dimensions must equals "  +  row  )  ; 
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




public   Matrix   diag  (  )  { 
int   d  =  Math  .  min  (  m  ,  n  )  ; 
Matrix   X  =  new   Matrix  (  d  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  d  ;  i  ++  )  { 
C  [  i  ]  [  0  ]  =  A  [  i  ]  [  i  ]  ; 
} 
return   X  ; 
} 





public   Matrix   diag  (  int   num  )  { 
int   l  =  0  ; 
if  (  n  <  m  )  { 
if  (  num  >=  0  )  { 
l  =  n  -  num  ; 
}  else   if  (  num  <  (  n  -  m  )  )  { 
l  =  m  +  num  ; 
}  else  { 
l  =  n  ; 
} 
}  else  { 
if  (  num  <=  0  )  { 
l  =  m  +  num  ; 
}  else   if  (  num  >  (  n  -  m  )  )  { 
l  =  n  -  num  ; 
}  else  { 
l  =  m  ; 
} 
} 
Matrix   X  =  new   Matrix  (  l  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  l  ;  i  ++  )  { 
C  [  i  ]  [  0  ]  =  (  num  >  0  )  ?  (  A  [  i  ]  [  i  +  num  ]  )  :  (  A  [  i  -  num  ]  [  i  ]  )  ; 
} 
return   X  ; 
} 





public   Matrix   dist  (  Matrix   B  )  { 
return   distRows  (  B  )  ; 
} 





public   Matrix   distColumns  (  Matrix   B  )  { 
checkRowDimension  (  B  )  ; 
Matrix   X  =  new   Matrix  (  n  ,  B  .  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  B  .  n  ;  k  ++  )  { 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  =  s  +  (  (  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  k  ]  )  *  (  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  k  ]  )  )  ; 
} 
C  [  k  ]  [  j  ]  =  Math  .  sqrt  (  s  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   distRows  (  Matrix   B  )  { 
checkColumnDimension  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  B  .  m  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   k  =  0  ;  k  <  B  .  m  ;  k  ++  )  { 
double   s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  s  +  (  (  A  [  i  ]  [  j  ]  -  B  .  A  [  k  ]  [  j  ]  )  *  (  A  [  i  ]  [  j  ]  -  B  .  A  [  k  ]  [  j  ]  )  )  ; 
} 
C  [  i  ]  [  k  ]  =  Math  .  sqrt  (  s  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   divide  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  s  ; 
} 
} 
return   X  ; 
} 







public   Matrix   divide  (  Matrix   B  )  { 
B  =  B  .  inverse  (  )  ; 
if  (  B  .  m  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix inner dimensions must agree."  )  ; 
} 
return   times  (  B  )  ; 
} 




public   Matrix   ebeAbs  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  abs  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 




public   Matrix   ebeCos  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  cos  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeDivide  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  s  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeDivide  (  Matrix   B  )  { 
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




public   Matrix   ebeExp  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  exp  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeFun  (  DoubleFunction   fun  )  { 
fun  .  checkArgNumber  (  1  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double  [  ]  arg  =  {  A  [  i  ]  [  j  ]  }  ; 
C  [  i  ]  [  j  ]  =  fun  .  eval  (  arg  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeIndFun  (  DoubleFunction   fun  )  { 
fun  .  checkArgNumber  (  3  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double  [  ]  args  =  {  A  [  i  ]  [  j  ]  ,  i  ,  j  }  ; 
C  [  i  ]  [  j  ]  =  fun  .  eval  (  args  )  ; 
} 
} 
return   X  ; 
} 




public   Matrix   ebeInv  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  1  /  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 




public   Matrix   ebeLog  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  log  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeMinus  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  s  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebePlus  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  s  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebePow  (  double   p  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  pow  (  A  [  i  ]  [  j  ]  ,  p  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebePow  (  Matrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  pow  (  A  [  i  ]  [  j  ]  ,  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 




public   Matrix   ebeSin  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  sin  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 




public   Matrix   ebeSqrt  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  Math  .  sqrt  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeTimes  (  double   s  )  { 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   Matrix   ebeTimes  (  Matrix   B  )  { 
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





public   EigenvalueDecomposition   eig  (  )  { 
return   new   EigenvalueDecomposition  (  this  )  ; 
} 





public   int  [  ]  [  ]  find  (  double   e  )  { 
Find   f  =  new   Find  (  getArrayCopy  (  )  ,  e  )  ; 
return   f  .  getIndices  (  )  ; 
} 






public   int  [  ]  [  ]  find  (  String   test  ,  double   e  )  { 
Find   f  =  new   Find  (  getArrayCopy  (  )  ,  test  ,  e  )  ; 
return   f  .  getIndices  (  )  ; 
} 





public   Matrix   findMatrix  (  double   e  )  { 
Find   f  =  new   Find  (  getArrayCopy  (  )  ,  e  )  ; 
return   new   Matrix  (  f  .  getDoubleArray  (  )  )  ; 
} 






public   Matrix   findMatrix  (  String   test  ,  double   e  )  { 
Find   f  =  new   Find  (  getArrayCopy  (  )  ,  test  ,  e  )  ; 
return   new   Matrix  (  f  .  getDoubleArray  (  )  )  ; 
} 





public   static   Matrix   fromFile  (  String   fileName  )  { 
MatrixFile   mf  =  new   MatrixFile  (  fileName  )  ; 
return   mf  .  getMatrix  (  )  ; 
} 





public   static   Matrix   fromFile  (  File   file  )  { 
MatrixFile   mf  =  new   MatrixFile  (  file  )  ; 
return   mf  .  getMatrix  (  )  ; 
} 





public   static   Matrix   fromMMLElement  (  Element   e  )  { 
Matrix   m  =  MatrixMML  .  readMatrix  (  e  )  ; 
return   m  ; 
} 





public   static   Matrix   fromMMLFile  (  File   file  )  { 
MatrixMMLFile   mf  =  new   MatrixMMLFile  (  file  )  ; 
return   mf  .  getMatrix  (  )  ; 
} 





public   static   Matrix   fromString  (  String   s  )  { 
Matrix   m  =  MatrixString  .  readMatrix  (  s  )  ; 
return   m  ; 
} 







public   double   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 







public   Matrix   get  (  int  [  ]  I  ,  int  [  ]  J  )  { 
checkIndicesLengths  (  I  ,  J  )  ; 
Matrix   X  =  new   Matrix  (  I  .  length  ,  1  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
B  [  i  ]  [  0  ]  =  A  [  I  [  i  ]  ]  [  J  [  i  ]  ]  ; 
} 
return   X  ; 
} 







public   Matrix   get  (  int  [  ]  [  ]  I  ,  int  [  ]  [  ]  J  )  { 
checkIndicesDimensions  (  I  ,  J  )  ; 
Matrix   X  =  new   Matrix  (  I  .  length  ,  I  [  0  ]  .  length  )  ; 
double  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  I  [  i  ]  .  length  ;  j  ++  )  { 
B  [  i  ]  [  j  ]  =  A  [  I  [  i  ]  [  j  ]  ]  [  J  [  i  ]  [  j  ]  ]  ; 
} 
} 
return   X  ; 
} 




public   Matrix   inverse  (  )  { 
return   solve  (  identity  (  m  ,  m  )  )  ; 
} 





public   LUDecomposition   lu  (  )  { 
return   new   LUDecomposition  (  this  )  ; 
} 




public   Matrix   max  (  )  { 
return   maxRows  (  )  ; 
} 




public   Matrix   maxColumns  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
double   maxval  =  A  [  i  ]  [  0  ]  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
maxval  =  Math  .  max  (  maxval  ,  A  [  i  ]  [  j  ]  )  ; 
} 
C  [  i  ]  [  0  ]  =  maxval  ; 
} 
return   X  ; 
} 




public   Matrix   maxRows  (  )  { 
Matrix   X  =  new   Matrix  (  1  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double   maxval  =  A  [  0  ]  [  j  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
maxval  =  Math  .  max  (  maxval  ,  A  [  i  ]  [  j  ]  )  ; 
} 
C  [  0  ]  [  j  ]  =  maxval  ; 
} 
return   X  ; 
} 





public   Matrix   merge  (  Matrix   B  )  { 
return   mergeRows  (  B  )  ; 
} 





public   Matrix   mergeColumns  (  Matrix   B  )  { 
checkRowDimension  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  +  B  .  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  B  .  n  ;  j  ++  )  { 
C  [  i  ]  [  j  +  n  ]  =  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   Matrix   mergeRows  (  Matrix   B  )  { 
checkColumnDimension  (  B  )  ; 
Matrix   X  =  new   Matrix  (  m  +  B  .  m  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  B  .  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  +  m  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 




public   Matrix   min  (  )  { 
return   minRows  (  )  ; 
} 




public   Matrix   minColumns  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  1  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
double   minval  =  get  (  i  ,  0  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
minval  =  Math  .  min  (  minval  ,  get  (  i  ,  j  )  )  ; 
} 
X  .  set  (  i  ,  0  ,  minval  )  ; 
} 
return   X  ; 
} 




public   Matrix   minRows  (  )  { 
Matrix   X  =  new   Matrix  (  1  ,  n  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double   minval  =  get  (  0  ,  j  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
minval  =  Math  .  min  (  minval  ,  get  (  i  ,  j  )  )  ; 
} 
X  .  set  (  0  ,  j  ,  minval  )  ; 
} 
return   X  ; 
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
f  =  Mathfun  .  hypot  (  f  ,  A  [  i  ]  [  j  ]  )  ; 
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




public   Matrix   prod  (  )  { 
return   prodRows  (  )  ; 
} 




public   Matrix   prodColumns  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  =  1  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
C  [  i  ]  [  0  ]  =  s  ; 
} 
return   X  ; 
} 




public   Matrix   prodRows  (  )  { 
Matrix   X  =  new   Matrix  (  1  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
double   s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
C  [  0  ]  [  j  ]  =  s  ; 
} 
return   X  ; 
} 





public   QRDecomposition   qr  (  )  { 
return   new   QRDecomposition  (  this  )  ; 
} 




public   int   rank  (  )  { 
return   new   SingularValueDecomposition  (  this  )  .  rank  (  )  ; 
} 






public   Matrix   reshape  (  int   m2  ,  int   n2  )  { 
return   reshapeRows  (  m2  ,  n2  )  ; 
} 






public   Matrix   reshapeColumns  (  int   m2  ,  int   n2  )  { 
if  (  (  m2  *  n2  )  !=  (  m  *  n  )  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions products must be equals."  )  ; 
} 
Matrix   X  =  new   Matrix  (  m2  ,  n2  )  ; 
double  [  ]  [  ]  C2  =  X  .  getArray  (  )  ; 
double  [  ]  C  =  getColumnPackedCopy  (  )  ; 
for  (  int   i  =  0  ;  i  <  m2  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n2  ;  j  ++  )  { 
C2  [  i  ]  [  j  ]  =  C  [  (  (  i  +  1  )  +  (  (  j  )  *  m2  )  )  -  1  ]  ; 
} 
} 
return   X  ; 
} 






public   Matrix   reshapeRows  (  int   m2  ,  int   n2  )  { 
if  (  (  m2  *  n2  )  !=  (  m  *  n  )  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions products must be equals."  )  ; 
} 
Matrix   X  =  new   Matrix  (  m2  ,  n2  )  ; 
double  [  ]  [  ]  C2  =  X  .  getArray  (  )  ; 
double  [  ]  C  =  getRowPackedCopy  (  )  ; 
for  (  int   i  =  0  ;  i  <  m2  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n2  ;  j  ++  )  { 
C2  [  i  ]  [  j  ]  =  C  [  (  (  j  +  1  )  +  (  (  i  )  *  n2  )  )  -  1  ]  ; 
} 
} 
return   X  ; 
} 






public   Matrix   resize  (  int   m2  ,  int   n2  )  { 
Matrix   X  =  new   Matrix  (  m2  ,  n2  )  ; 
for  (  int   i  =  0  ;  i  <  m2  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n2  ;  j  ++  )  { 
X  .  A  [  i  ]  [  j  ]  =  (  (  i  <  m  )  &&  (  j  <  n  )  )  ?  (  A  [  i  ]  [  j  ]  )  :  (  0  )  ; 
} 
} 
return   X  ; 
} 







public   void   set  (  int   i  ,  int   j  ,  double   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 







public   void   set  (  int  [  ]  I  ,  int  [  ]  J  ,  double   s  )  { 
checkIndicesLengths  (  I  ,  J  )  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
A  [  I  [  i  ]  ]  [  J  [  i  ]  ]  =  s  ; 
} 
} 





public   Matrix   solve  (  Matrix   B  )  { 
return  (  (  m  ==  n  )  ?  (  new   LUDecomposition  (  this  )  )  .  solve  (  B  )  :  (  new   QRDecomposition  (  this  )  )  .  solve  (  B  )  )  ; 
} 





public   int  [  ]  sort  (  int   c  )  { 
return   sortRows  (  c  )  ; 
} 





public   int  [  ]  sortColumns  (  int   l  )  { 
if  (  l  >  m  )  { 
throw   new   IllegalArgumentException  (  "Matrix Rows dimensions must < "  +  l  )  ; 
} 
int  [  ]  I  =  new   int  [  n  ]  ; 
Sort   order  =  new   Sort  (  getRowArrayCopy  (  l  )  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
I  [  i  ]  =  order  .  getOrder  (  i  )  ; 
} 
return   I  ; 
} 





public   int  [  ]  sortRows  (  int   c  )  { 
if  (  c  >  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix Columns dimensions must < "  +  c  )  ; 
} 
int  [  ]  I  =  new   int  [  m  ]  ; 
Sort   order  =  new   Sort  (  getColumnArrayCopy  (  c  )  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
I  [  i  ]  =  order  .  getOrder  (  i  )  ; 
} 
return   I  ; 
} 





public   Matrix   sortedColumnsMatrix  (  int   l  )  { 
if  (  l  >  m  )  { 
throw   new   IllegalArgumentException  (  "Matrix Rows dimensions must < "  +  l  )  ; 
} 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
Sort   order  =  new   Sort  (  getRowArrayCopy  (  l  )  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
X  .  setColumn  (  i  ,  getColumn  (  order  .  getOrder  (  i  )  )  )  ; 
} 
return   X  ; 
} 





public   Matrix   sortedMatrix  (  int   c  )  { 
return   sortedRowsMatrix  (  c  )  ; 
} 





public   Matrix   sortedRowsMatrix  (  int   c  )  { 
if  (  c  >  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix Columns dimensions must < "  +  c  )  ; 
} 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
Sort   order  =  new   Sort  (  getColumnArrayCopy  (  c  )  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
X  .  setRow  (  i  ,  getRow  (  order  .  getOrder  (  i  )  )  )  ; 
} 
return   X  ; 
} 




public   Matrix   sum  (  )  { 
return   sumRows  (  )  ; 
} 




public   Matrix   sumColumns  (  )  { 
Matrix   X  =  new   Matrix  (  m  ,  1  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  s  +  A  [  i  ]  [  j  ]  ; 
} 
C  [  i  ]  [  0  ]  =  s  ; 
} 
return   X  ; 
} 




public   Matrix   sumRows  (  )  { 
Matrix   X  =  new   Matrix  (  1  ,  n  )  ; 
double  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
double   s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  =  s  +  A  [  i  ]  [  j  ]  ; 
} 
C  [  0  ]  [  j  ]  =  s  ; 
} 
return   X  ; 
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
s  +=  (  Arowi  [  k  ]  *  Bcolj  [  k  ]  )  ; 
} 
C  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 




public   void   toCommandLine  (  String   title  )  { 
System  .  out  .  println  (  title  +  " ="  )  ; 
System  .  out  .  println  (  MatrixString  .  printMatrix  (  this  )  )  ; 
} 




public   void   toFile  (  String   fileName  )  { 
new   MatrixFile  (  fileName  ,  this  )  ; 
} 




public   void   toFile  (  File   file  )  { 
new   MatrixFile  (  file  ,  this  )  ; 
} 





public   MatrixPlot2D   toFramePlot2D  (  String   title  )  { 
MatrixPlot2D   mp2d  =  toPanelPlot2D  (  )  ; 
new   FrameView  (  title  ,  mp2d  )  ; 
return   mp2d  ; 
} 






public   MatrixPlot2D   toFramePlot2D  (  String   title  ,  Matrix   X  )  { 
MatrixPlot2D   mp2d  =  toPanelPlot2D  (  X  )  ; 
new   FrameView  (  title  ,  mp2d  )  ; 
return   mp2d  ; 
} 





public   MatrixPlot3D   toFramePlot3D  (  String   title  )  { 
MatrixPlot3D   mp3d  =  toPanelPlot3D  (  )  ; 
new   FrameView  (  title  ,  mp3d  )  ; 
return   mp3d  ; 
} 







public   MatrixPlot3D   toFramePlot3D  (  String   title  ,  Matrix   X  ,  Matrix   Y  )  { 
MatrixPlot3D   mp3d  =  toPanelPlot3D  (  X  ,  Y  )  ; 
new   FrameView  (  title  ,  mp3d  )  ; 
return   mp3d  ; 
} 




public   void   toFrameTable  (  String   title  )  { 
new   FrameView  (  title  ,  toPanelTable  (  )  )  ; 
} 




public   Element   toMMLElement  (  )  { 
Element   e  =  MatrixMML  .  printMatrix  (  this  )  ; 
return   e  ; 
} 




public   void   toMMLFile  (  File   file  )  { 
new   MatrixMMLFile  (  file  ,  this  )  ; 
} 




public   MatrixPlot2D   toPanelPlot2D  (  )  { 
MatrixPlot2D   mp2d  =  new   MatrixPlot2D  (  this  )  ; 
return   mp2d  ; 
} 





public   MatrixPlot2D   toPanelPlot2D  (  Matrix   X  )  { 
MatrixPlot2D   mp2d  ; 
if  (  X  .  m  ==  1  )  { 
mp2d  =  new   MatrixPlot2D  (  X  .  times  (  new   Matrix  (  1  ,  n  ,  1  )  )  ,  this  )  ; 
return   mp2d  ; 
}  else  { 
mp2d  =  new   MatrixPlot2D  (  X  ,  this  )  ; 
return   mp2d  ; 
} 
} 




public   MatrixPlot3D   toPanelPlot3D  (  )  { 
MatrixPlot3D   mp3d  =  new   MatrixPlot3D  (  this  )  ; 
return   mp3d  ; 
} 






public   MatrixPlot3D   toPanelPlot3D  (  Matrix   X  ,  Matrix   Y  )  { 
MatrixPlot3D   mp3d  ; 
if  (  (  X  .  m  ==  1  )  &&  (  Y  .  m  ==  1  )  )  { 
mp3d  =  new   MatrixPlot3D  (  X  .  times  (  new   Matrix  (  1  ,  n  ,  1  )  )  ,  Y  .  times  (  new   Matrix  (  1  ,  n  ,  1  )  )  ,  this  .  copy  (  )  )  ; 
return   mp3d  ; 
}  else  { 
mp3d  =  new   MatrixPlot3D  (  X  ,  Y  ,  this  )  ; 
return   mp3d  ; 
} 
} 




public   MatrixTable   toPanelTable  (  )  { 
return   new   MatrixTable  (  this  )  ; 
} 




public   String   toString  (  )  { 
String   s  =  MatrixString  .  printMatrix  (  this  )  ; 
return   s  ; 
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
} 

