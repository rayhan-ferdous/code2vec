package   petrieditor  .  modules  .  invariants  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  DecimalFormatSymbols  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  Locale  ; 

public   class   Matrix  { 





private   int  [  ]  [  ]  A  ; 






private   int   m  ,  n  ; 






public   Matrix  (  int   m  ,  int   n  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   int  [  m  ]  [  n  ]  ; 
} 







public   Matrix  (  int   m  ,  int   n  ,  int   s  )  { 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   int  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 
} 
} 







public   Matrix  (  int  [  ]  [  ]  A  )  { 
if  (  A  !=  null  )  { 
m  =  A  .  length  ; 
if  (  A  .  length  >=  1  )  { 
n  =  A  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  A  [  i  ]  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "All rows must have the same length."  )  ; 
} 
} 
this  .  A  =  A  ; 
} 
} 
} 







public   Matrix  (  int  [  ]  [  ]  A  ,  int   m  ,  int   n  )  { 
this  .  A  =  A  ; 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
} 







public   Matrix  (  int   vals  [  ]  ,  int   m  )  { 
this  .  m  =  m  ; 
n  =  (  m  !=  0  ?  vals  .  length  /  m  :  0  )  ; 
if  (  m  *  n  !=  vals  .  length  )  { 
throw   new   IllegalArgumentException  (  "Array length must be a multiple of m."  )  ; 
} 
A  =  new   int  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  vals  [  i  +  j  *  m  ]  ; 
} 
} 
} 

public   Matrix   mul  (  Matrix   a  ,  Matrix   b  )  { 
if  (  a  .  getColumnDimension  (  )  !=  b  .  getRowDimension  (  )  )  { 
System  .  out  .  println  (  " zle wymiary "  )  ; 
return   null  ; 
} 
Matrix   res  =  new   Matrix  (  a  .  getRowDimension  (  )  ,  b  .  getColumnDimension  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  a  .  getRowDimension  (  )  ;  i  ++  )  for  (  int   j  =  0  ;  j  <  b  .  getColumnDimension  (  )  ;  j  ++  )  for  (  int   k  =  0  ;  k  <  a  .  getColumnDimension  (  )  ;  k  ++  )  res  .  set  (  i  ,  j  ,  res  .  get  (  i  ,  j  )  +  a  .  get  (  i  ,  k  )  *  b  .  get  (  k  ,  j  )  )  ; 
return   res  ; 
} 

public   int  [  ]  getRow  (  int   r  )  { 
return   this  .  A  [  r  ]  ; 
} 

public   void   setRow  (  int   r  ,  int   row  [  ]  )  { 
for  (  int   i  =  0  ;  i  <  getColumnDimension  (  )  ;  i  ++  )  A  [  r  ]  [  i  ]  =  row  [  i  ]  ; 
} 

public   int  [  ]  getRowCopy  (  int   r  )  { 
int   row  [  ]  =  new   int  [  getColumnDimension  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  getColumnDimension  (  )  ;  i  ++  )  row  [  i  ]  =  A  [  r  ]  [  i  ]  ; 
return   row  ; 
} 







public   static   Matrix   constructWithCopy  (  int  [  ]  [  ]  A  )  { 
int   m  =  A  .  length  ; 
int   n  =  A  [  0  ]  .  length  ; 
Matrix   X  =  new   Matrix  (  m  ,  n  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
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

public   Matrix   eliminateRow  (  int   row  )  { 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
int  [  ]  rows  =  new   int  [  m  -  1  ]  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  i  !=  row  )  rows  [  count  ++  ]  =  i  ; 
} 
Matrix   reduced  =  getMatrix  (  rows  ,  0  ,  n  -  1  )  ; 
return   reduced  ; 
} 





public   int  [  ]  [  ]  getArray  (  )  { 
return   A  ; 
} 





public   int  [  ]  [  ]  getArrayCopy  (  )  { 
int  [  ]  [  ]  C  =  new   int  [  m  ]  [  n  ]  ; 
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








public   int   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 









public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  { 
Matrix   X  =  new   Matrix  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
int  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   Matrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  { 
Matrix   X  =  new   Matrix  (  r  .  length  ,  c  .  length  )  ; 
int  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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









public   Matrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  { 
Matrix   X  =  new   Matrix  (  i1  -  i0  +  1  ,  c  .  length  )  ; 
int  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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
int  [  ]  [  ]  B  =  X  .  getArray  (  )  ; 
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








public   void   set  (  int   i  ,  int   j  ,  int   s  )  { 
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





public   boolean   isZeroMatrix  (  )  { 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
boolean   isZero  =  true  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  i  ,  j  )  !=  0  )  return   false  ; 
} 
} 
return   isZero  ; 
} 





public   Matrix   transpose  (  )  { 
Matrix   X  =  new   Matrix  (  n  ,  m  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  j  ]  [  i  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 







public   static   Matrix   identity  (  int   m  ,  int   n  )  { 
Matrix   A  =  new   Matrix  (  m  ,  n  )  ; 
int  [  ]  [  ]  X  =  A  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
X  [  i  ]  [  j  ]  =  (  i  ==  j  ?  1  :  0  )  ; 
} 
} 
return   A  ; 
} 







public   void   print  (  int   w  ,  int   d  )  { 
print  (  new   PrintWriter  (  System  .  out  ,  true  )  ,  w  ,  d  )  ; 
} 








public   void   print  (  PrintWriter   output  ,  int   w  ,  int   d  )  { 
DecimalFormat   format  =  new   DecimalFormat  (  )  ; 
format  .  setDecimalFormatSymbols  (  new   DecimalFormatSymbols  (  Locale  .  UK  )  )  ; 
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





private   void   checkMatrixDimensions  (  Matrix   B  )  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 





public   void   printArray  (  int  [  ]  a  )  { 
int   n  =  a  .  length  ; 
} 
} 

