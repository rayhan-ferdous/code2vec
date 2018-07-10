package   org  .  math  .  array  ; 

import   org  .  math  .  array  .  util  .  Function  ; 
import   org  .  math  .  array  .  util  .  IndexFunction  ; 
import   org  .  math  .  array  .  util  .  Random  ; 
import   org  .  math  .  array  .  util  .  Sorting  ; 







public   class   DoubleArray  { 

public   static   void   main  (  String  [  ]  args  )  { 
double  [  ]  array  =  random  (  6  )  ; 
System  .  out  .  println  (  DoubleArray  .  toString  (  array  )  )  ; 
getReverseFast  (  array  )  ; 
System  .  out  .  println  (  DoubleArray  .  toString  (  array  )  )  ; 
} 







public   static   double  [  ]  [  ]  add  (  double  [  ]  [  ]  M  ,  double   a  )  { 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  M  [  i  ]  .  length  ;  j  ++  )  { 
M  [  i  ]  [  j  ]  =  M  [  i  ]  [  j  ]  +  a  ; 
} 
} 
return   M  ; 
} 






public   static   double  [  ]  [  ]  add  (  double  [  ]  [  ]  M1  ,  double  [  ]  [  ]  M2  )  { 
if  (  M1  .  length  !=  M2  .  length  )  { 
System  .  err  .  println  (  "Matrices must be of the same dimension"  )  ; 
return   M1  ; 
} 
double  [  ]  [  ]  M  =  new   double  [  M1  .  length  ]  [  M1  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M1  .  length  ;  i  ++  )  { 
if  (  M1  [  i  ]  .  length  !=  M2  [  i  ]  .  length  )  { 
System  .  err  .  println  (  "Matrices must be of the same dimension"  )  ; 
return   M1  ; 
} 
for  (  int   j  =  0  ;  j  <  M1  [  i  ]  .  length  ;  j  ++  )  { 
M  [  i  ]  [  j  ]  =  M1  [  i  ]  [  j  ]  +  M2  [  i  ]  [  j  ]  ; 
} 
} 
return   M  ; 
} 














public   static   double  [  ]  [  ]  identity  (  int   m  )  { 
return   diagonal  (  m  ,  1.0  )  ; 
} 















public   static   double  [  ]  [  ]  diagonal  (  int   m  ,  double   c  )  { 
if  (  m  <  1  )  { 
throw   new   IllegalArgumentException  (  "First argument must be > 0"  )  ; 
} 
double  [  ]  [  ]  I  =  new   double  [  m  ]  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
I  [  i  ]  [  i  ]  =  c  ; 
} 
return   I  ; 
} 














public   static   double  [  ]  [  ]  diagonal  (  double  ...  c  )  { 
double  [  ]  [  ]  I  =  new   double  [  c  .  length  ]  [  c  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
I  [  i  ]  [  i  ]  =  c  [  i  ]  ; 
} 
return   I  ; 
} 








public   static   double  [  ]  [  ]  one  (  int   m  ,  int   n  )  { 
return   fill  (  m  ,  n  ,  1.0  )  ; 
} 









public   static   double  [  ]  [  ]  one  (  int   m  ,  int   n  ,  double   c  )  { 
return   fill  (  m  ,  n  ,  c  )  ; 
} 







public   static   double  [  ]  one  (  int   m  )  { 
return   fill  (  m  ,  1.0  )  ; 
} 








public   static   double  [  ]  one  (  int   m  ,  double   c  )  { 
return   fill  (  m  ,  c  )  ; 
} 














public   static   double  [  ]  [  ]  fill  (  int   m  ,  int   n  ,  double   c  )  { 
double  [  ]  [  ]  o  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  o  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  o  [  i  ]  .  length  ;  j  ++  )  { 
o  [  i  ]  [  j  ]  =  c  ; 
} 
} 
return   o  ; 
} 












public   static   double  [  ]  fill  (  int   m  ,  double   c  )  { 
double  [  ]  o  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  o  .  length  ;  i  ++  )  { 
o  [  i  ]  =  c  ; 
} 
return   o  ; 
} 








public   static   double  [  ]  [  ]  random  (  int   m  ,  int   n  )  { 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  Random  .  raw  (  )  ; 
} 
} 
return   array  ; 
} 






public   static   double  [  ]  random  (  int   m  )  { 
double  [  ]  array  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
array  [  i  ]  =  Random  .  raw  (  )  ; 
} 
return   array  ; 
} 









public   static   double  [  ]  [  ]  random  (  int   m  ,  int   n  ,  double   min  ,  double   max  )  { 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  min  +  Random  .  raw  (  )  *  (  max  -  min  )  ; 
} 
} 
return   array  ; 
} 








public   static   double  [  ]  random  (  int   m  ,  double   min  ,  double   max  )  { 
double  [  ]  array  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
array  [  i  ]  =  min  +  Random  .  raw  (  )  *  (  max  -  min  )  ; 
} 
return   array  ; 
} 











public   static   double  [  ]  [  ]  random  (  int   m  ,  int   n  ,  double  [  ]  min  ,  double  [  ]  max  )  { 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  min  [  j  ]  +  Random  .  raw  (  )  *  (  max  [  j  ]  -  min  [  j  ]  )  ; 
} 
} 
return   array  ; 
} 










public   static   double  [  ]  [  ]  increment  (  int   m  ,  int   n  ,  double   begin  ,  double   pitch  )  { 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  begin  +  i  *  pitch  ; 
} 
} 
return   array  ; 
} 








public   static   double  [  ]  increment  (  int   m  ,  double   begin  ,  double   pitch  )  { 
double  [  ]  array  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
array  [  i  ]  =  begin  +  i  *  pitch  ; 
} 
return   array  ; 
} 










public   static   double  [  ]  [  ]  increment  (  int   m  ,  int   n  ,  double  [  ]  begin  ,  double  [  ]  pitch  )  { 
if  (  begin  .  length  !=  n  ||  pitch  .  length  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Length of 3rd and 4th arguments must = second argument"  )  ; 
} 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  begin  [  j  ]  +  i  *  pitch  [  j  ]  ; 
} 
} 
return   array  ; 
} 









public   static   double  [  ]  increment  (  double   begin  ,  double   pitch  ,  double   end  )  { 
double  [  ]  array  =  new   double  [  (  int  )  (  (  end  -  begin  )  /  pitch  )  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
array  [  i  ]  =  begin  +  i  *  pitch  ; 
} 
return   array  ; 
} 






public   static   double  [  ]  copy  (  double  [  ]  M  )  { 
double  [  ]  array  =  new   double  [  M  .  length  ]  ; 
System  .  arraycopy  (  M  ,  0  ,  array  ,  0  ,  M  .  length  )  ; 
return   array  ; 
} 






public   static   double  [  ]  [  ]  copy  (  double  [  ]  [  ]  M  )  { 
double  [  ]  [  ]  array  =  new   double  [  M  .  length  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 








public   static   double  [  ]  [  ]  resize  (  double  [  ]  [  ]  M  ,  int   m  ,  int   n  )  { 
double  [  ]  [  ]  array  =  new   double  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  Math  .  min  (  M  .  length  ,  m  )  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  Math  .  min  (  M  [  i  ]  .  length  ,  n  )  )  ; 
} 
return   array  ; 
} 

























public   static   double  [  ]  [  ]  getSubMatrixRangeCopy  (  double  [  ]  [  ]  M  ,  int   i1  ,  int   i2  ,  int   j1  ,  int   j2  )  { 
double  [  ]  [  ]  array  =  new   double  [  i2  -  i1  +  1  ]  [  j2  -  j1  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  i2  -  i1  +  1  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  +  i1  ]  ,  j1  ,  array  [  i  ]  ,  0  ,  j2  -  j1  +  1  )  ; 
} 
return   array  ; 
} 

























public   static   double  [  ]  [  ]  getColumnsRangeCopy  (  double  [  ]  [  ]  M  ,  int   j1  ,  int   j2  )  { 
double  [  ]  [  ]  array  =  new   double  [  M  .  length  ]  [  j2  -  j1  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  ]  ,  j1  ,  array  [  i  ]  ,  0  ,  j2  -  j1  +  1  )  ; 
} 
return   array  ; 
} 
























public   static   double  [  ]  [  ]  getColumnsCopy  (  double  [  ]  [  ]  M  ,  int  ...  J  )  { 
double  [  ]  [  ]  array  =  new   double  [  M  .  length  ]  [  J  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  J  .  length  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  M  [  i  ]  [  J  [  j  ]  ]  ; 
} 
} 
return   array  ; 
} 





















public   static   double  [  ]  getColumnCopy  (  double  [  ]  [  ]  M  ,  int   j  )  { 
double  [  ]  array  =  new   double  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  i  ]  [  j  ]  ; 
} 
return   array  ; 
} 









public   static   double  [  ]  getColumnCopy  (  double  [  ]  [  ]  [  ]  M  ,  int   j  ,  int   k  )  { 
double  [  ]  array  =  new   double  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  i  ]  [  j  ]  [  k  ]  ; 
} 
return   array  ; 
} 





















public   static   double  [  ]  [  ]  getRowsCopy  (  double  [  ]  [  ]  M  ,  int  ...  I  )  { 
double  [  ]  [  ]  array  =  new   double  [  I  .  length  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  I  [  i  ]  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  I  [  i  ]  ]  .  length  )  ; 
} 
return   array  ; 
} 




















public   static   double  [  ]  getRowCopy  (  double  [  ]  [  ]  M  ,  int   i  )  { 
double  [  ]  array  =  new   double  [  M  [  0  ]  .  length  ]  ; 
System  .  arraycopy  (  M  [  i  ]  ,  0  ,  array  ,  0  ,  M  [  i  ]  .  length  )  ; 
return   array  ; 
} 























public   static   double  [  ]  [  ]  getRowsRangeCopy  (  double  [  ]  [  ]  M  ,  int   i1  ,  int   i2  )  { 
double  [  ]  [  ]  array  =  new   double  [  i2  -  i1  +  1  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  i2  -  i1  +  1  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  +  i1  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  i  +  i1  ]  .  length  )  ; 
} 
return   array  ; 
} 















public   static   double  [  ]  getRangeCopy  (  double  [  ]  M  ,  int   j1  ,  int   j2  )  { 
double  [  ]  array  =  new   double  [  j2  -  j1  +  1  ]  ; 
System  .  arraycopy  (  M  ,  j1  ,  array  ,  0  ,  j2  -  j1  +  1  )  ; 
return   array  ; 
} 















public   static   double  [  ]  getCopy  (  double  [  ]  M  ,  int  ...  I  )  { 
double  [  ]  array  =  new   double  [  I  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  I  [  i  ]  ]  ; 
} 
return   array  ; 
} 







public   static   int   getColumnDimension  (  double  [  ]  [  ]  M  ,  int   i  )  { 
return   M  [  i  ]  .  length  ; 
} 




















public   static   double  [  ]  getDiagonal  (  double  [  ]  [  ]  M  ,  int   I  )  { 
int   nr  =  M  .  length  ,  nc  =  M  .  length  ; 
int   nd  =  0  ; 
if  (  nc  <  nr  )  { 
if  (  I  >=  0  )  { 
nd  =  nc  -  I  ; 
}  else   if  (  I  <  (  nc  -  nr  )  )  { 
nd  =  nr  +  I  ; 
}  else  { 
nd  =  nc  ; 
} 
}  else  { 
if  (  I  <=  0  )  { 
nd  =  nr  +  I  ; 
}  else   if  (  I  >  (  nc  -  nr  )  )  { 
nd  =  nc  -  I  ; 
}  else  { 
nd  =  nr  ; 
} 
} 
double  [  ]  d  =  new   double  [  nd  ]  ; 
for  (  int   i  =  0  ;  i  <  d  .  length  ;  i  ++  )  { 
d  [  i  ]  =  M  [  i  +  I  ]  [  i  +  I  ]  ; 
} 
return   d  ; 
} 














public   static   double  [  ]  [  ]  mergeRows  (  double  [  ]  ...  x  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
array  [  i  ]  =  new   double  [  x  [  i  ]  .  length  ]  ; 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  array  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 

















public   static   double  [  ]  [  ]  mergeColumns  (  double  [  ]  ...  x  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  [  0  ]  .  length  ]  [  x  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  array  [  i  ]  .  length  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  x  [  j  ]  [  i  ]  ; 
} 
} 
return   array  ; 
} 






public   static   double  [  ]  [  ]  columnVector  (  double  [  ]  x  )  { 
return   mergeColumns  (  x  )  ; 
} 






public   static   double  [  ]  [  ]  rowVector  (  double  [  ]  x  )  { 
return   mergeRows  (  x  )  ; 
} 













public   static   double  [  ]  merge  (  double  [  ]  ...  x  )  { 
int  [  ]  xlength_array  =  new   int  [  x  .  length  ]  ; 
xlength_array  [  0  ]  =  x  [  0  ]  .  length  ; 
for  (  int   i  =  1  ;  i  <  x  .  length  ;  i  ++  )  { 
xlength_array  [  i  ]  =  x  [  i  ]  .  length  +  xlength_array  [  i  -  1  ]  ; 
} 
double  [  ]  array  =  new   double  [  xlength_array  [  x  .  length  -  1  ]  ]  ; 
System  .  arraycopy  (  x  [  0  ]  ,  0  ,  array  ,  0  ,  x  [  0  ]  .  length  )  ; 
for  (  int   i  =  1  ;  i  <  x  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  ,  xlength_array  [  i  -  1  ]  ,  x  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 



























public   static   double  [  ]  [  ]  insertColumns  (  double  [  ]  [  ]  x  ,  int   J  ,  double  [  ]  ...  y  )  { 
return   transpose  (  insertRows  (  transpose  (  x  )  ,  J  ,  y  )  )  ; 
} 






















public   static   double  [  ]  [  ]  insertRows  (  double  [  ]  [  ]  x  ,  int   I  ,  double  [  ]  ...  y  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  .  length  +  y  .  length  ]  [  x  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
for  (  int   i  =  0  ;  i  <  y  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  y  [  i  ]  ,  0  ,  array  [  i  +  I  ]  ,  0  ,  y  [  i  ]  .  length  )  ; 
} 
for  (  int   i  =  0  ;  i  <  x  .  length  -  I  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  +  I  ]  ,  0  ,  array  [  i  +  I  +  y  .  length  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 


















public   static   double  [  ]  insert  (  double  [  ]  x  ,  int   I  ,  double  ...  y  )  { 
double  [  ]  array  =  new   double  [  x  .  length  +  y  .  length  ]  ; 
System  .  arraycopy  (  x  ,  0  ,  array  ,  0  ,  I  )  ; 
System  .  arraycopy  (  y  ,  0  ,  array  ,  I  ,  y  .  length  )  ; 
System  .  arraycopy  (  x  ,  I  ,  array  ,  I  +  y  .  length  ,  x  .  length  -  I  )  ; 
return   array  ; 
} 



















public   static   double  [  ]  [  ]  deleteColumnsRange  (  double  [  ]  [  ]  x  ,  int   J1  ,  int   J2  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  .  length  ]  [  x  [  0  ]  .  length  -  (  J2  -  J1  +  1  )  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  J1  )  ; 
System  .  arraycopy  (  x  [  i  ]  ,  J2  +  1  ,  array  [  i  ]  ,  J1  ,  x  [  i  ]  .  length  -  (  J2  +  1  )  )  ; 
} 
return   array  ; 
} 



















public   static   double  [  ]  [  ]  deleteColumns  (  double  [  ]  [  ]  x  ,  int  ...  J  )  { 
return   transpose  (  deleteRows  (  transpose  (  x  )  ,  J  )  )  ; 
} 
















public   static   double  [  ]  [  ]  deleteRowsRange  (  double  [  ]  [  ]  x  ,  int   I1  ,  int   I2  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  .  length  -  (  I2  -  I1  +  1  )  ]  [  x  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I1  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
for  (  int   i  =  0  ;  i  <  x  .  length  -  I2  -  1  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  +  I2  +  1  ]  ,  0  ,  array  [  i  +  I1  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 
















public   static   double  [  ]  [  ]  deleteRows  (  double  [  ]  [  ]  x  ,  int  ...  I  )  { 
double  [  ]  [  ]  array  =  new   double  [  x  .  length  -  I  .  length  ]  [  x  [  0  ]  .  length  ]  ; 
int   i2  =  0  ; 
for  (  int   i  =  0  ;  i  <  x  .  length  ;  i  ++  )  { 
if  (  !  into  (  i  ,  I  )  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i2  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
i2  ++  ; 
} 
} 
return   array  ; 
} 















public   static   double  [  ]  deleteRange  (  double  [  ]  x  ,  int   J1  ,  int   J2  )  { 
double  [  ]  array  =  new   double  [  x  .  length  -  (  J2  -  J1  +  1  )  ]  ; 
System  .  arraycopy  (  x  ,  0  ,  array  ,  0  ,  J1  )  ; 
System  .  arraycopy  (  x  ,  J2  +  1  ,  array  ,  J1  ,  x  .  length  -  (  J2  +  1  )  )  ; 
return   array  ; 
} 














public   static   double  [  ]  delete  (  double  [  ]  x  ,  int  ...  J  )  { 
double  [  ]  array  =  new   double  [  x  .  length  -  J  .  length  ]  ; 
int   j2  =  0  ; 
for  (  int   j  =  0  ;  j  <  x  .  length  ;  j  ++  )  { 
if  (  !  into  (  j  ,  J  )  )  { 
array  [  j2  ]  =  x  [  j  ]  ; 
j2  ++  ; 
} 
} 
return   array  ; 
} 







private   static   boolean   into  (  int   i  ,  int  [  ]  I  )  { 
boolean   in  =  false  ; 
for  (  int   j  =  0  ;  j  <  I  .  length  ;  j  ++  )  { 
in  =  in  ||  (  i  ==  I  [  j  ]  )  ; 
} 
return   in  ; 
} 

























public   static   double  [  ]  [  ]  buildXY  (  double   Xmin  ,  double   Xmax  ,  double  [  ]  Y  )  { 
if  (  Xmax  <  Xmin  )  { 
throw   new   IllegalArgumentException  (  "First argument must be less than second"  )  ; 
} 
int   n  =  Y  .  length  ; 
double  [  ]  [  ]  XY  =  new   double  [  n  ]  [  2  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
XY  [  i  ]  [  0  ]  =  Xmin  +  (  Xmax  -  Xmin  )  *  (  double  )  i  /  (  double  )  (  n  -  1  )  ; 
XY  [  i  ]  [  1  ]  =  Y  [  i  ]  ; 
} 
return   XY  ; 
} 















public   static   double  [  ]  [  ]  buildXY  (  double  [  ]  X  ,  double  [  ]  Y  )  { 
return   mergeColumns  (  X  ,  Y  )  ; 
} 

















public   static   double  [  ]  min  (  double  [  ]  [  ]  M  )  { 
double  [  ]  min  =  new   double  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  min  .  length  ;  j  ++  )  { 
min  [  j  ]  =  M  [  0  ]  [  j  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
min  [  j  ]  =  Math  .  min  (  min  [  j  ]  ,  M  [  i  ]  [  j  ]  )  ; 
} 
} 
return   min  ; 
} 






public   static   double   min  (  double  ...  M  )  { 
double   min  =  M  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
min  =  Math  .  min  (  min  ,  M  [  i  ]  )  ; 
} 
return   min  ; 
} 

















public   static   double  [  ]  max  (  double  [  ]  [  ]  M  )  { 
double  [  ]  max  =  new   double  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  max  .  length  ;  j  ++  )  { 
max  [  j  ]  =  M  [  0  ]  [  j  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
max  [  j  ]  =  Math  .  max  (  max  [  j  ]  ,  M  [  i  ]  [  j  ]  )  ; 
} 
} 
return   max  ; 
} 






public   static   double   max  (  double  ...  M  )  { 
double   max  =  M  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
max  =  Math  .  max  (  max  ,  M  [  i  ]  )  ; 
} 
return   max  ; 
} 

















public   static   int  [  ]  minIndex  (  double  [  ]  [  ]  M  )  { 
int  [  ]  minI  =  new   int  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  minI  .  length  ;  j  ++  )  { 
minI  [  j  ]  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  [  j  ]  <  M  [  minI  [  j  ]  ]  [  j  ]  )  { 
minI  [  j  ]  =  i  ; 
} 
} 
} 
return   minI  ; 
} 






public   static   int   minIndex  (  double  ...  M  )  { 
int   minI  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  <  M  [  minI  ]  )  { 
minI  =  i  ; 
} 
} 
return   minI  ; 
} 

















public   static   int  [  ]  maxIndex  (  double  [  ]  [  ]  M  )  { 
int  [  ]  maxI  =  new   int  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  maxI  .  length  ;  j  ++  )  { 
maxI  [  j  ]  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  [  j  ]  >  M  [  maxI  [  j  ]  ]  [  j  ]  )  { 
maxI  [  j  ]  =  i  ; 
} 
} 
} 
return   maxI  ; 
} 






public   static   int   maxIndex  (  double  ...  M  )  { 
int   maxI  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  >  M  [  maxI  ]  )  { 
maxI  =  i  ; 
} 
} 
return   maxI  ; 
} 






public   static   double   sum  (  double  [  ]  v  )  { 
int   m  =  v  .  length  ; 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  ; 
} 
return   s  ; 
} 






public   static   double  [  ]  sum  (  double  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
double  [  ]  X  =  new   double  [  n  ]  ; 
double   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  [  j  ]  ; 
} 
X  [  j  ]  =  s  ; 
} 
return   X  ; 
} 













public   static   double  [  ]  cumSum  (  double  [  ]  v  )  { 
int   m  =  v  .  length  ; 
double  [  ]  X  =  new   double  [  m  ]  ; 
double   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  ; 
X  [  i  ]  =  s  ; 
} 
return   X  ; 
} 





















public   static   double  [  ]  [  ]  cumSum  (  double  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
double  [  ]  [  ]  X  =  new   double  [  m  ]  [  n  ]  ; 
double   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  [  j  ]  ; 
X  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 






public   static   double   product  (  double  [  ]  v  )  { 
int   m  =  v  .  length  ; 
double   p  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
p  *=  v  [  i  ]  ; 
} 
return   p  ; 
} 







public   static   double  [  ]  product  (  double  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
double  [  ]  X  =  new   double  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
double   p  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
p  *=  v  [  i  ]  [  j  ]  ; 
} 
X  [  j  ]  =  p  ; 
} 
return   X  ; 
} 













public   static   double  [  ]  cumProduct  (  double  [  ]  v  )  { 
int   m  =  v  .  length  ; 
double  [  ]  X  =  new   double  [  m  ]  ; 
double   s  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  *=  v  [  i  ]  ; 
X  [  i  ]  =  s  ; 
} 
return   X  ; 
} 




















public   static   double  [  ]  [  ]  cumProduct  (  double  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
double  [  ]  [  ]  X  =  new   double  [  m  ]  [  n  ]  ; 
double   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  *=  v  [  i  ]  [  j  ]  ; 
X  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 
















public   static   String   toString  (  double  [  ]  ...  v  )  { 
StringBuffer   str  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  v  [  i  ]  .  length  -  1  ;  j  ++  )  { 
str  .  append  (  v  [  i  ]  [  j  ]  +  " "  )  ; 
} 
str  .  append  (  v  [  i  ]  [  v  [  i  ]  .  length  -  1  ]  )  ; 
if  (  i  <  v  .  length  -  1  )  { 
str  .  append  (  "\n"  )  ; 
} 
} 
return   str  .  toString  (  )  ; 
} 


















public   static   String   toString  (  String   format  ,  double  [  ]  ...  v  )  { 
StringBuffer   str  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  v  [  i  ]  .  length  -  1  ;  j  ++  )  { 
str  .  append  (  String  .  format  (  format  ,  v  [  i  ]  [  j  ]  )  )  ; 
str  .  append  (  ' '  )  ; 
} 
str  .  append  (  String  .  format  (  format  ,  v  [  i  ]  [  v  [  i  ]  .  length  -  1  ]  )  )  ; 
if  (  i  <  v  .  length  -  1  )  { 
str  .  append  (  "\n"  )  ; 
} 
} 
return   str  .  toString  (  )  ; 
} 







public   static   void   throwError  (  String   msg  )  { 
throw   new   IllegalArgumentException  (  msg  )  ; 
} 








public   static   void   checkColumnDimension  (  double  [  ]  [  ]  M  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  .  length  !=  n  )  { 
throwError  (  "row "  +  i  +  " have "  +  M  [  i  ]  .  length  +  " columns instead of "  +  n  +  " columns expected."  )  ; 
} 
} 
} 








public   static   boolean   isColumnDimension  (  double  [  ]  [  ]  M  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  .  length  !=  n  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 








public   static   void   checkRowDimension  (  double  [  ]  [  ]  M  ,  int   m  )  { 
if  (  M  .  length  !=  m  )  { 
throwError  (  "columns have "  +  M  .  length  +  " rows instead of "  +  m  +  " rows expected."  )  ; 
} 
} 








public   static   boolean   isRowDimension  (  double  [  ]  [  ]  M  ,  int   m  )  { 
if  (  M  .  length  !=  m  )  { 
return   false  ; 
} 
return   true  ; 
} 







public   static   void   checkLength  (  double  [  ]  M  ,  int   n  )  { 
if  (  M  .  length  !=  n  )  { 
throwError  (  "row have "  +  M  .  length  +  " elements instead of "  +  n  +  " elements expected."  )  ; 
} 
} 







public   static   boolean   isLength  (  double  [  ]  M  ,  int   n  )  { 
if  (  M  .  length  !=  n  )  { 
return   false  ; 
} 
return   true  ; 
} 



















public   static   double  [  ]  [  ]  f  (  double  [  ]  [  ]  M  ,  Function   f  )  { 
double  [  ]  [  ]  fM  =  new   double  [  M  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  fM  .  length  ;  i  ++  )  { 
fM  [  i  ]  =  new   double  [  M  [  i  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  fM  [  i  ]  .  length  ;  j  ++  )  { 
fM  [  i  ]  [  j  ]  =  f  .  f  (  M  [  i  ]  [  j  ]  )  ; 
} 
} 
return   fM  ; 
} 
















public   static   double  [  ]  f  (  double  [  ]  M  ,  Function   func  )  { 
double  [  ]  fM  =  new   double  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  fM  .  length  ;  i  ++  )  { 
fM  [  i  ]  =  func  .  f  (  M  [  i  ]  )  ; 
} 
return   fM  ; 
} 
















public   static   double  [  ]  findex  (  int   m  ,  IndexFunction   f  )  { 
double  [  ]  fm  =  new   double  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  fm  .  length  ;  i  ++  )  { 
fm  [  i  ]  =  f  .  fi  (  i  )  ; 
} 
return   fm  ; 
} 






public   static   double  [  ]  sort  (  double  [  ]  values  )  { 
double  [  ]  sorted_values  =  new   double  [  values  .  length  ]  ; 
System  .  arraycopy  (  values  ,  0  ,  sorted_values  ,  0  ,  values  .  length  )  ; 
new   Sorting  (  sorted_values  ,  false  )  ; 
return   sorted_values  ; 
} 























public   static   double  [  ]  [  ]  sort  (  double  [  ]  [  ]  values  ,  int   column  )  { 
double  [  ]  [  ]  sorted_values  =  new   double  [  values  .  length  ]  [  values  [  0  ]  .  length  ]  ; 
Sorting   s  =  new   Sorting  (  getColumnCopy  (  values  ,  column  )  ,  false  )  ; 
for  (  int   i  =  0  ;  i  <  sorted_values  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  values  [  s  .  getIndex  (  i  )  ]  ,  0  ,  sorted_values  [  i  ]  ,  0  ,  values  [  s  .  getIndex  (  i  )  ]  .  length  )  ; 
} 
return   sorted_values  ; 
} 







public   static   double  [  ]  [  ]  transpose  (  double  [  ]  [  ]  M  )  { 
double  [  ]  [  ]  tM  =  new   double  [  M  [  0  ]  .  length  ]  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  tM  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  tM  [  0  ]  .  length  ;  j  ++  )  { 
tM  [  i  ]  [  j  ]  =  M  [  j  ]  [  i  ]  ; 
} 
} 
return   tM  ; 
} 

public   static   void   set  (  double  [  ]  [  ]  array  ,  double   value  )  { 
for  (  int   x  =  0  ;  x  <  array  .  length  ;  x  ++  )  { 
for  (  int   y  =  0  ;  y  <  array  [  0  ]  .  length  ;  y  ++  )  { 
array  [  x  ]  [  y  ]  =  value  ; 
} 
} 
} 

public   static   void   clear  (  double  [  ]  [  ]  array  )  { 
set  (  array  ,  0  )  ; 
} 

public   double  [  ]  getReverse  (  double  [  ]  array  )  { 
int   size  =  array  .  length  ; 
double  [  ]  result  =  new   double  [  size  ]  ; 
for  (  int   x  =  0  ;  x  <  size  ;  x  ++  )  { 
result  [  x  ]  =  array  [  size  -  1  -  x  ]  ; 
} 
return   result  ; 
} 

public   static   void   getReverseFast  (  double  [  ]  array  )  { 
int   size  =  array  .  length  ; 
int   half  =  array  .  length  /  2  ; 
double   tmp  ; 
for  (  int   x  =  0  ;  x  <  half  ;  x  ++  )  { 
tmp  =  array  [  x  ]  ; 
array  [  x  ]  =  array  [  size  -  1  -  x  ]  ; 
array  [  size  -  1  -  x  ]  =  tmp  ; 
} 
} 
} 

