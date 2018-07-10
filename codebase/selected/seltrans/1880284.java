package   net  .  sf  .  opendf  .  math  .  cmatrix  ; 

import   java  .  text  .  NumberFormat  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  DecimalFormatSymbols  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  text  .  FieldPosition  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  StreamTokenizer  ; 
import   net  .  sf  .  opendf  .  math  .  Complex  ; 
import   net  .  sf  .  opendf  .  math  .  util  .  Maths  ; 











































public   class   CMatrix   implements   Cloneable  ,  java  .  io  .  Serializable  { 




private   Complex  [  ]  [  ]  A  ; 





private   int   m  ,  n  ; 





public   CMatrix  (  int   m  ,  int   n  )  { 
this  (  m  ,  n  ,  0  )  ; 
} 

public   CMatrix  (  int   m  ,  int   n  ,  double   s  )  { 
this  (  m  ,  n  ,  new   Complex  (  s  ,  0  )  )  ; 
} 






public   CMatrix  (  int   m  ,  int   n  ,  Complex   s  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   Complex  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 
} 
} 






public   CMatrix  (  Complex  [  ]  [  ]  A  )  { 
m  =  A  .  length  ; 
n  =  A  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  A  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length."  )  ; 
} 
} 
this  .  A  =  A  ; 
} 






public   CMatrix  (  Complex  [  ]  [  ]  A  ,  int   m  ,  int   n  )  { 
this  .  A  =  A  ; 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
} 

public   CMatrix  (  List  <  List  <  Object  >  >  A  )  { 
this  (  createArray  (  A  )  )  ; 
} 



public   CMatrix   copy  (  )  { 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
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




public   Complex  [  ]  [  ]  getArray  (  )  { 
return   A  ; 
} 

public   List  <  List  <  Complex  >  >  getList  (  )  { 
List  <  List  <  Complex  >  >  res  =  new   ArrayList  <  List  <  Complex  >  >  (  )  ; 
for  (  Complex  [  ]  r  :  A  )  { 
List  <  Complex  >  row  =  new   ArrayList  <  Complex  >  (  )  ; 
res  .  add  (  row  )  ; 
for  (  Complex   v  :  r  )  { 
row  .  add  (  v  )  ; 
} 
} 
return   res  ; 
} 




public   Complex  [  ]  [  ]  getArrayCopy  (  )  { 
Complex  [  ]  [  ]  C  =  new   Complex  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   C  ; 
} 




public   int   getRowDimension  (  )  { 
return   m  ; 
} 




public   int   getColumnDimension  (  )  { 
return   n  ; 
} 







public   Complex   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 









public   CMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  { 
CMatrix   X  =  new   CMatrix  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
Complex  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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







public   CMatrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  { 
CMatrix   X  =  new   CMatrix  (  r  .  length  ,  c  .  length  )  ; 
Complex  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   CMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  { 
CMatrix   X  =  new   CMatrix  (  i1  -  i0  +  1  ,  c  .  length  )  ; 
Complex  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   CMatrix   getMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  )  { 
CMatrix   X  =  new   CMatrix  (  r  .  length  ,  j1  -  j0  +  1  )  ; 
Complex  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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







public   void   set  (  int   i  ,  int   j  ,  Complex   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 









public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  CMatrix   X  )  { 
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







public   void   setMatrix  (  int  [  ]  r  ,  int  [  ]  c  ,  CMatrix   X  )  { 
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








public   void   setMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  ,  CMatrix   X  )  { 
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








public   void   setMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  ,  CMatrix   X  )  { 
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




public   CMatrix   transpose  (  )  { 
CMatrix   X  =  new   CMatrix  (  n  ,  m  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
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
s  +=  A  [  i  ]  [  j  ]  .  abs  (  )  ; 
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
s  +=  A  [  i  ]  [  j  ]  .  abs  (  )  ; 
} 
f  =  Math  .  max  (  f  ,  s  )  ; 
} 
return   f  ; 
} 




public   CMatrix   uminus  (  )  { 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  negate  (  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   plus  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  add  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   plusEquals  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  add  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 





public   CMatrix   minus  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  subtract  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   minusEquals  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  subtract  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 





public   CMatrix   arrayTimes  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  multiply  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   arrayTimesEquals  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  multiply  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 





public   CMatrix   arrayRightDivide  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  divide  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   arrayRightDivideEquals  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  .  divide  (  B  .  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 





public   CMatrix   arrayLeftDivide  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  .  divide  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 





public   CMatrix   arrayLeftDivideEquals  (  CMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  B  .  A  [  i  ]  [  j  ]  .  divide  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 





public   CMatrix   times  (  double   s  )  { 
return   times  (  new   Complex  (  s  ,  0  )  )  ; 
} 

public   CMatrix   times  (  Complex   s  )  { 
CMatrix   X  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  s  .  multiply  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   X  ; 
} 

public   CMatrix   timesEquals  (  double   s  )  { 
return   timesEquals  (  new   Complex  (  s  ,  0  )  )  ; 
} 





public   CMatrix   timesEquals  (  Complex   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  .  multiply  (  A  [  i  ]  [  j  ]  )  ; 
} 
} 
return   this  ; 
} 






public   CMatrix   times  (  CMatrix   B  )  { 
if  (  B  .  m  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix inner dimensions must agree."  )  ; 
} 
CMatrix   X  =  new   CMatrix  (  m  ,  B  .  n  )  ; 
Complex  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
Complex  [  ]  Bcolj  =  new   Complex  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  B  .  n  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
Bcolj  [  k  ]  =  B  .  A  [  k  ]  [  j  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
Complex  [  ]  Arowi  =  A  [  i  ]  ; 
Complex   s  =  Complex  .  ZERO  ; 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
s  =  s  .  add  (  Arowi  [  k  ]  .  multiply  (  Bcolj  [  k  ]  )  )  ; 
} 
C  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 




public   Complex   trace  (  )  { 
Complex   t  =  Complex  .  ZERO  ; 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  m  ,  n  )  ;  i  ++  )  { 
t  =  t  .  add  (  A  [  i  ]  [  i  ]  )  ; 
} 
return   t  ; 
} 






public   static   CMatrix   random  (  int   m  ,  int   n  )  { 
CMatrix   A  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  new   Complex  (  Math  .  random  (  )  ,  Math  .  random  (  )  )  ; 
} 
} 
return   A  ; 
} 






public   static   CMatrix   identity  (  int   m  ,  int   n  )  { 
CMatrix   A  =  new   CMatrix  (  m  ,  n  )  ; 
Complex  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  (  i  ==  j  ?  Complex  .  ONE  :  Complex  .  ZERO  )  ; 
} 
} 
return   A  ; 
} 






public   void   print  (  int   w  ,  int   d  )  { 
print  (  new   PrintWriter  (  System  .  out  ,  true  )  ,  w  ,  d  )  ; 
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


private   void   checkMatrixDimensions  (  CMatrix   B  )  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 

private   static   Complex  [  ]  [  ]  createArray  (  List  <  List  <  Object  >  >  A  )  { 
Complex  [  ]  [  ]  a  =  new   Complex  [  A  .  size  (  )  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
List  <  Object  >  R  =  A  .  get  (  i  )  ; 
Complex  [  ]  r  =  a  [  i  ]  =  new   Complex  [  R  .  size  (  )  ]  ; 
for  (  int   j  =  0  ;  j  <  r  .  length  ;  j  ++  )  { 
r  [  j  ]  =  makeComplex  (  R  .  get  (  j  )  )  ; 
} 
} 
return   a  ; 
} 

private   static   Complex   makeComplex  (  Object   v  )  { 
if  (  v   instanceof   Complex  )  { 
return  (  Complex  )  v  ; 
}  else   if  (  v   instanceof   Number  )  { 
return   new   Complex  (  (  (  Number  )  v  )  .  doubleValue  (  )  ,  0  )  ; 
}  else  { 
throw   new   RuntimeException  (  "Must be either a Number or a Complex. ("  +  v  +  ")"  )  ; 
} 
} 
} 

