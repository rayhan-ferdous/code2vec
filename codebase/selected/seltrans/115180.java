package   lejos  .  util  ; 

import   java  .  io  .  PrintStream  ; 

public   class   Matrix   implements   Cloneable  ,  java  .  io  .  Serializable  { 

private   static   final   long   serialVersionUID  =  5724948160773341967L  ; 




private   double  [  ]  [  ]  A  ; 





private   int   m  ,  n  ; 





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






public   Matrix  (  double  [  ]  [  ]  A  )  throws   IllegalArgumentException  { 
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






public   Matrix  (  double   vals  [  ]  ,  int   m  )  throws   IllegalArgumentException  { 
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





public   static   Matrix   constructWithCopy  (  double  [  ]  [  ]  A  )  throws   IllegalArgumentException  { 
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



public   Object   clone  (  )  { 
return   this  .  copy  (  )  ; 
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




public   double  [  ]  getColumnPackedCopy  (  )  { 
double  [  ]  vals  =  new   double  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  +  j  *  m  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
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




public   int   getRowDimension  (  )  { 
return   m  ; 
} 




public   int   getColumnDimension  (  )  { 
return   n  ; 
} 






public   double   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 









public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  throws   ArrayIndexOutOfBoundsException  { 
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







public   Matrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  throws   ArrayIndexOutOfBoundsException  { 
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








public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  throws   ArrayIndexOutOfBoundsException  { 
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








public   Matrix   getMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  )  throws   ArrayIndexOutOfBoundsException  { 
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







public   void   set  (  int   i  ,  int   j  ,  double   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 









public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  Matrix   X  )  throws   ArrayIndexOutOfBoundsException  { 
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







public   void   setMatrix  (  int  [  ]  r  ,  int  [  ]  c  ,  Matrix   X  )  throws   ArrayIndexOutOfBoundsException  { 
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








public   void   setMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  ,  Matrix   X  )  throws   ArrayIndexOutOfBoundsException  { 
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








public   void   setMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  ,  Matrix   X  )  throws   ArrayIndexOutOfBoundsException  { 
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




public   double   normF  (  )  { 
double   f  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
f  =  hypot  (  f  ,  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   f  ; 
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





public   Matrix   timesEquals  (  double   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 






public   Matrix   times  (  Matrix   B  )  throws   IllegalArgumentException  { 
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




public   double   trace  (  )  { 
double   t  =  0  ; 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  m  ,  n  )  ;  i  ++  )  { 
t  +=  A  [  i  ]  [  i  ]  ; 
} 
return   t  ; 
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






public   static   Matrix   identity  (  int   m  ,  int   n  )  { 
Matrix   A  =  new   Matrix  (  m  ,  n  )  ; 
double  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  (  i  ==  j  ?  1.0  :  0.0  )  ; 
} 
} 
return   A  ; 
} 




public   Matrix   inverse  (  )  { 
return   solve  (  identity  (  m  ,  m  )  )  ; 
} 





public   Matrix   solve  (  Matrix   B  )  { 
return   new   LUDecomposition  (  this  )  .  solve  (  B  )  ; 
} 

public   void   print  (  PrintStream   out  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
System  .  out  .  print  (  A  [  i  ]  [  j  ]  )  ; 
} 
System  .  out  .  println  (  ""  )  ; 
} 
} 


private   void   checkMatrixDimensions  (  Matrix   B  )  throws   IllegalArgumentException  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 








private   double   hypot  (  double   a  ,  double   b  )  { 
double   r  ; 
if  (  Math  .  abs  (  a  )  >  Math  .  abs  (  b  )  )  { 
r  =  b  /  a  ; 
r  =  Math  .  abs  (  a  )  *  Math  .  sqrt  (  1  +  r  *  r  )  ; 
}  else   if  (  b  !=  0  )  { 
r  =  a  /  b  ; 
r  =  Math  .  abs  (  b  )  *  Math  .  sqrt  (  1  +  r  *  r  )  ; 
}  else  { 
r  =  0.0  ; 
} 
return   r  ; 
} 
} 

