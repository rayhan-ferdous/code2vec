package   org  .  tigr  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StreamTokenizer  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  NumberFormat  ; 











































public   class   FloatMatrix   implements   Cloneable  { 




public   float  [  ]  [  ]  A  ; 





public   int   m  ,  n  ; 





public   FloatMatrix  (  int   m  ,  int   n  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   float  [  m  ]  [  n  ]  ; 
} 






public   FloatMatrix  (  int   m  ,  int   n  ,  float   s  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   float  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 
} 
} 






public   FloatMatrix  (  float  [  ]  [  ]  A  )  { 
m  =  A  .  length  ; 
n  =  A  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  A  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length."  )  ; 
} 
} 
this  .  A  =  A  ; 
} 






public   FloatMatrix  (  float  [  ]  [  ]  A  ,  int   m  ,  int   n  )  { 
this  .  A  =  A  ; 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
} 






public   FloatMatrix  (  float   vals  [  ]  ,  int   m  )  { 
this  .  m  =  m  ; 
n  =  (  m  !=  0  ?  vals  .  length  /  m  :  0  )  ; 
if  (  m  *  n  !=  vals  .  length  )  { 
throw   new   IllegalArgumentException  (  "Array length must be a multiple of m."  )  ; 
} 
A  =  new   float  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  vals  [  i  +  j  *  m  ]  ; 
} 
} 
} 

public   static   String  [  ]  getPersistenceDelegateArgs  (  )  { 
return   new   String  [  ]  {  "array"  ,  "rowDimension"  ,  "columnDimension"  }  ; 
} 





public   static   FloatMatrix   constructWithCopy  (  float  [  ]  [  ]  A  )  { 
int   m  =  A  .  length  ; 
int   n  =  A  [  0  ]  .  length  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
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



public   FloatMatrix   copy  (  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
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




public   float  [  ]  [  ]  getArray  (  )  { 
return   A  ; 
} 




public   float  [  ]  [  ]  getArrayCopy  (  )  { 
float  [  ]  [  ]  C  =  new   float  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   C  ; 
} 




public   float  [  ]  getColumnPackedCopy  (  )  { 
float  [  ]  vals  =  new   float  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  +  j  *  m  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
} 




public   float  [  ]  getRowPackedCopy  (  )  { 
float  [  ]  vals  =  new   float  [  m  *  n  ]  ; 
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







public   float   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 









public   FloatMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
float  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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







public   FloatMatrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  r  .  length  ,  c  .  length  )  ; 
float  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   FloatMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  i1  -  i0  +  1  ,  c  .  length  )  ; 
float  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   FloatMatrix   getMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  r  .  length  ,  j1  -  j0  +  1  )  ; 
float  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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







public   void   set  (  int   i  ,  int   j  ,  float   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 









public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  FloatMatrix   X  )  { 
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







public   void   setMatrix  (  int  [  ]  r  ,  int  [  ]  c  ,  FloatMatrix   X  )  { 
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








public   void   setMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  ,  FloatMatrix   X  )  { 
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








public   void   setMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  ,  FloatMatrix   X  )  { 
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




public   FloatMatrix   transpose  (  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  n  ,  m  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  j  ]  [  i  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 




public   float   norm1  (  )  { 
float   f  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
float   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  Math  .  abs  (  A  [  i  ]  [  j  ]  )  ; 
} 
f  =  Math  .  max  (  f  ,  s  )  ; 
} 
return   f  ; 
} 




public   float   normInf  (  )  { 
float   f  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
float   s  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  +=  Math  .  abs  (  A  [  i  ]  [  j  ]  )  ; 
} 
f  =  Math  .  max  (  f  ,  s  )  ; 
} 
return   f  ; 
} 




public   float   normF  (  )  { 
float   f  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
f  =  (  float  )  Maths  .  hypot  (  f  ,  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   f  ; 
} 




public   FloatMatrix   uminus  (  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  -  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   plus  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   plusEquals  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 





public   FloatMatrix   minus  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   minusEquals  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 





public   FloatMatrix   arrayTimes  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  *  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   arrayTimesEquals  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  *  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 





public   FloatMatrix   arrayRightDivide  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   arrayRightDivideEquals  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 





public   FloatMatrix   arrayLeftDivide  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  /  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   arrayLeftDivideEquals  (  FloatMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  /  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 





public   FloatMatrix   times  (  float   s  )  { 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 





public   FloatMatrix   timesEquals  (  float   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 






public   FloatMatrix   times  (  FloatMatrix   B  )  { 
if  (  B  .  m  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix inner dimensions must agree."  )  ; 
} 
FloatMatrix   X  =  new   FloatMatrix  (  m  ,  B  .  n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
float  [  ]  Bcolj  =  new   float  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  B  .  n  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
Bcolj  [  k  ]  =  B  .  A  [  k  ]  [  j  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
float  [  ]  Arowi  =  A  [  i  ]  ; 
float   s  =  0  ; 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
s  +=  Arowi  [  k  ]  *  Bcolj  [  k  ]  ; 
} 
C  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 




public   float   trace  (  )  { 
float   t  =  0  ; 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  m  ,  n  )  ;  i  ++  )  { 
t  +=  A  [  i  ]  [  i  ]  ; 
} 
return   t  ; 
} 






public   static   FloatMatrix   random  (  int   m  ,  int   n  )  { 
FloatMatrix   A  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  (  float  )  Math  .  random  (  )  ; 
} 
} 
return   A  ; 
} 






public   static   FloatMatrix   identity  (  int   m  ,  int   n  )  { 
FloatMatrix   A  =  new   FloatMatrix  (  m  ,  n  )  ; 
float  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  (  float  )  (  i  ==  j  ?  1.0  :  0.0  )  ; 
} 
} 
return   A  ; 
} 






public   void   print  (  int   w  ,  int   d  )  { 
print  (  new   PrintWriter  (  System  .  out  ,  true  )  ,  w  ,  d  )  ; 
} 







public   void   print  (  PrintWriter   output  ,  int   w  ,  int   d  )  { 
DecimalFormat   format  =  new   DecimalFormat  (  )  ; 
format  .  setMinimumIntegerDigits  (  1  )  ; 
format  .  setMaximumFractionDigits  (  d  )  ; 
format  .  setMinimumFractionDigits  (  d  )  ; 
format  .  setGroupingUsed  (  false  )  ; 
print  (  output  ,  format  ,  w  +  2  )  ; 
} 







public   void   print  (  NumberFormat   format  ,  int   width  )  { 
print  (  new   PrintWriter  (  System  .  out  ,  true  )  ,  format  ,  width  )  ; 
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







public   static   FloatMatrix   read  (  BufferedReader   input  )  throws   java  .  io  .  IOException  { 
StreamTokenizer   tokenizer  =  new   StreamTokenizer  (  input  )  ; 
tokenizer  .  resetSyntax  (  )  ; 
tokenizer  .  wordChars  (  0  ,  255  )  ; 
tokenizer  .  whitespaceChars  (  0  ,  ' '  )  ; 
tokenizer  .  eolIsSignificant  (  true  )  ; 
java  .  util  .  Vector   v  =  new   java  .  util  .  Vector  (  )  ; 
while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_EOL  )  ; 
if  (  tokenizer  .  ttype  ==  StreamTokenizer  .  TT_EOF  )  throw   new   java  .  io  .  IOException  (  "Unexpected EOF on matrix read."  )  ; 
do  { 
v  .  addElement  (  Float  .  valueOf  (  tokenizer  .  sval  )  )  ; 
}  while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  ; 
int   n  =  v  .  size  (  )  ; 
float   row  [  ]  =  new   float  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  row  [  j  ]  =  (  (  Float  )  v  .  elementAt  (  j  )  )  .  floatValue  (  )  ; 
v  .  removeAllElements  (  )  ; 
v  .  addElement  (  row  )  ; 
while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  { 
v  .  addElement  (  row  =  new   float  [  n  ]  )  ; 
int   j  =  0  ; 
do  { 
if  (  j  >=  n  )  throw   new   java  .  io  .  IOException  (  "Row "  +  v  .  size  (  )  +  " is too long."  )  ; 
row  [  j  ++  ]  =  Float  .  valueOf  (  tokenizer  .  sval  )  .  floatValue  (  )  ; 
}  while  (  tokenizer  .  nextToken  (  )  ==  StreamTokenizer  .  TT_WORD  )  ; 
if  (  j  <  n  )  throw   new   java  .  io  .  IOException  (  "Row "  +  v  .  size  (  )  +  " is too short."  )  ; 
} 
int   m  =  v  .  size  (  )  ; 
float  [  ]  [  ]  A  =  new   float  [  m  ]  [  ]  ; 
v  .  copyInto  (  A  )  ; 
return   new   FloatMatrix  (  A  )  ; 
} 


private   void   checkMatrixDimensions  (  FloatMatrix   B  )  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 




public   FloatMatrix   getSubMatrix  (  int   displayInterval  )  { 
if  (  displayInterval  <=  0  )  return   this  ; 
int   sub_m  =  m  /  displayInterval  ; 
int   sub_n  =  n  /  displayInterval  ; 
FloatMatrix   X  =  new   FloatMatrix  (  sub_m  ,  sub_n  )  ; 
float  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ,  k  =  0  ;  i  <  m  &&  k  <  sub_m  ;  i  +=  displayInterval  ,  k  ++  )  { 
for  (  int   j  =  0  ,  l  =  0  ;  j  <  n  &&  l  <  sub_n  ;  j  +=  displayInterval  ,  l  ++  )  { 
C  [  k  ]  [  l  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 
} 

