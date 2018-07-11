package   org  .  math  .  array  ; 








public   class   IntegerArray  { 

public   static   final   int  [  ]  plus  (  int  [  ]  a  ,  int   value  )  { 
int   size  =  a  .  length  ; 
int  [  ]  result  =  new   int  [  size  ]  ; 
for  (  int   x  =  0  ;  x  <  size  ;  x  ++  )  { 
result  [  x  ]  =  a  [  x  ]  +  value  ; 
} 
return   result  ; 
} 

public   static   final   void   plusAndNoReturn  (  int  [  ]  a  ,  int   value  )  { 
int   size  =  a  .  length  ; 
for  (  int   x  =  0  ;  x  <  size  ;  x  ++  )  { 
a  [  x  ]  =  a  [  x  ]  +  value  ; 
} 
} 

; 








public   static   int  [  ]  [  ]  diagonal  (  int   m  ,  int   c  )  { 
int  [  ]  [  ]  I  =  new   int  [  m  ]  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
I  [  i  ]  [  i  ]  =  c  ; 
} 
return   I  ; 
} 








public   static   int  [  ]  [  ]  fill  (  int   m  ,  int   n  ,  int   c  )  { 
int  [  ]  [  ]  o  =  new   int  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  o  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  o  [  i  ]  .  length  ;  j  ++  )  { 
o  [  i  ]  [  j  ]  =  c  ; 
} 
} 
return   o  ; 
} 







public   static   int  [  ]  fill  (  int   m  ,  int   c  )  { 
int  [  ]  o  =  new   int  [  m  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
o  [  i  ]  =  c  ; 
} 
return   o  ; 
} 






public   static   int  [  ]  [  ]  floor  (  double  [  ]  [  ]  v  )  { 
int  [  ]  [  ]  ia  =  new   int  [  v  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
ia  [  i  ]  =  new   int  [  v  [  i  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  ia  [  i  ]  .  length  ;  j  ++  )  { 
ia  [  i  ]  [  j  ]  =  (  int  )  Math  .  floor  (  v  [  i  ]  [  j  ]  )  ; 
} 
} 
return   ia  ; 
} 






public   static   int  [  ]  floor  (  double  [  ]  v  )  { 
int  [  ]  ia  =  new   int  [  v  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
ia  [  i  ]  =  (  int  )  Math  .  floor  (  v  [  i  ]  )  ; 
} 
return   ia  ; 
} 






public   static   double  [  ]  [  ]  int2double  (  int  [  ]  [  ]  v  )  { 
double  [  ]  [  ]  ia  =  new   double  [  v  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
ia  [  i  ]  =  new   double  [  v  [  i  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  ia  [  i  ]  .  length  ;  j  ++  )  { 
ia  [  i  ]  [  j  ]  =  v  [  i  ]  [  j  ]  ; 
} 
} 
return   ia  ; 
} 






public   static   double  [  ]  int2double  (  int  [  ]  v  )  { 
double  [  ]  ia  =  new   double  [  v  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
ia  [  i  ]  =  v  [  i  ]  ; 
} 
return   ia  ; 
} 






public   static   int  [  ]  copy  (  int  [  ]  M  )  { 
int  [  ]  array  =  new   int  [  M  .  length  ]  ; 
System  .  arraycopy  (  M  ,  0  ,  array  ,  0  ,  M  .  length  )  ; 
return   array  ; 
} 






public   static   int  [  ]  [  ]  copy  (  int  [  ]  [  ]  M  )  { 
int  [  ]  [  ]  array  =  new   int  [  M  .  length  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 

























public   static   int  [  ]  [  ]  getSubMatrixRangeCopy  (  int  [  ]  [  ]  M  ,  int   i1  ,  int   i2  ,  int   j1  ,  int   j2  )  { 
int  [  ]  [  ]  array  =  new   int  [  i2  -  i1  +  1  ]  [  j2  -  j1  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  i2  -  i1  +  1  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  +  i1  ]  ,  j1  ,  array  [  i  ]  ,  0  ,  j2  -  j1  +  1  )  ; 
} 
return   array  ; 
} 

























public   static   int  [  ]  [  ]  getColumnsRangeCopy  (  int  [  ]  [  ]  M  ,  int   j1  ,  int   j2  )  { 
int  [  ]  [  ]  array  =  new   int  [  M  .  length  ]  [  j2  -  j1  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  ]  ,  j1  ,  array  [  i  ]  ,  0  ,  j2  -  j1  +  1  )  ; 
} 
return   array  ; 
} 
























public   static   int  [  ]  [  ]  getColumnsCopy  (  int  [  ]  [  ]  M  ,  int  ...  J  )  { 
int  [  ]  [  ]  array  =  new   int  [  M  .  length  ]  [  J  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  J  .  length  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  M  [  i  ]  [  J  [  j  ]  ]  ; 
} 
} 
return   array  ; 
} 




















public   static   int  [  ]  getColumnCopy  (  int  [  ]  [  ]  M  ,  int   j  )  { 
int  [  ]  array  =  new   int  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  i  ]  [  j  ]  ; 
} 
return   array  ; 
} 









public   static   int  [  ]  getColumnCopy  (  int  [  ]  [  ]  [  ]  M  ,  int   j  ,  int   k  )  { 
int  [  ]  array  =  new   int  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  M  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  i  ]  [  j  ]  [  k  ]  ; 
} 
return   array  ; 
} 





















public   static   int  [  ]  [  ]  getRowsCopy  (  int  [  ]  [  ]  M  ,  int  ...  I  )  { 
int  [  ]  [  ]  array  =  new   int  [  I  .  length  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  I  [  i  ]  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  I  [  i  ]  ]  .  length  )  ; 
} 
return   array  ; 
} 




















public   static   int  [  ]  getRowCopy  (  int  [  ]  [  ]  M  ,  int   i  )  { 
int  [  ]  array  =  new   int  [  M  [  0  ]  .  length  ]  ; 
System  .  arraycopy  (  M  [  i  ]  ,  0  ,  array  ,  0  ,  M  [  i  ]  .  length  )  ; 
return   array  ; 
} 























public   static   int  [  ]  [  ]  getRowsRangeCopy  (  int  [  ]  [  ]  M  ,  int   i1  ,  int   i2  )  { 
int  [  ]  [  ]  array  =  new   int  [  i2  -  i1  +  1  ]  [  M  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  i2  -  i1  +  1  ;  i  ++  )  { 
System  .  arraycopy  (  M  [  i  +  i1  ]  ,  0  ,  array  [  i  ]  ,  0  ,  M  [  i  +  i1  ]  .  length  )  ; 
} 
return   array  ; 
} 















public   static   int  [  ]  getRangeCopy  (  int  [  ]  M  ,  int   j1  ,  int   j2  )  { 
int  [  ]  array  =  new   int  [  j2  -  j1  +  1  ]  ; 
System  .  arraycopy  (  M  ,  j1  ,  array  ,  0  ,  j2  -  j1  +  1  )  ; 
return   array  ; 
} 















public   static   int  [  ]  getCopy  (  int  [  ]  M  ,  int  ...  I  )  { 
int  [  ]  array  =  new   int  [  I  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I  .  length  ;  i  ++  )  { 
array  [  i  ]  =  M  [  I  [  i  ]  ]  ; 
} 
return   array  ; 
} 







public   static   int   getColumnDimension  (  int  [  ]  [  ]  M  ,  int   i  )  { 
return   M  [  i  ]  .  length  ; 
} 




















public   static   int  [  ]  getDiagonal  (  int  [  ]  [  ]  M  ,  int   I  )  { 
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
int  [  ]  d  =  new   int  [  nd  ]  ; 
for  (  int   i  =  0  ;  i  <  d  .  length  ;  i  ++  )  { 
d  [  i  ]  =  M  [  i  +  I  ]  [  i  +  I  ]  ; 
} 
return   d  ; 
} 














public   static   int  [  ]  [  ]  mergeRows  (  int  [  ]  ...  x  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
array  [  i  ]  =  new   int  [  x  [  i  ]  .  length  ]  ; 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  array  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 

















public   static   int  [  ]  [  ]  mergeColumns  (  int  [  ]  ...  x  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  [  0  ]  .  length  ]  [  x  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  array  [  i  ]  .  length  ;  j  ++  )  { 
array  [  i  ]  [  j  ]  =  x  [  j  ]  [  i  ]  ; 
} 
} 
return   array  ; 
} 













public   static   int  [  ]  merge  (  int  [  ]  ...  x  )  { 
int  [  ]  xlength_array  =  new   int  [  x  .  length  ]  ; 
xlength_array  [  0  ]  =  x  [  0  ]  .  length  ; 
for  (  int   i  =  1  ;  i  <  x  .  length  ;  i  ++  )  { 
xlength_array  [  i  ]  =  x  [  i  ]  .  length  +  xlength_array  [  i  -  1  ]  ; 
} 
int  [  ]  array  =  new   int  [  xlength_array  [  x  .  length  -  1  ]  ]  ; 
System  .  arraycopy  (  x  [  0  ]  ,  0  ,  array  ,  0  ,  x  [  0  ]  .  length  )  ; 
for  (  int   i  =  1  ;  i  <  x  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  ,  xlength_array  [  i  -  1  ]  ,  x  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 



























public   static   int  [  ]  [  ]  insertColumns  (  int  [  ]  [  ]  x  ,  int   J  ,  int  [  ]  ...  y  )  { 
return   transpose  (  insertRows  (  transpose  (  x  )  ,  J  ,  y  )  )  ; 
} 






















public   static   int  [  ]  [  ]  insertRows  (  int  [  ]  [  ]  x  ,  int   I  ,  int  [  ]  ...  y  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  .  length  +  y  .  length  ]  [  x  [  0  ]  .  length  ]  ; 
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


















public   static   int  [  ]  insert  (  int  [  ]  x  ,  int   I  ,  int  ...  y  )  { 
int  [  ]  array  =  new   int  [  x  .  length  +  y  .  length  ]  ; 
System  .  arraycopy  (  x  ,  0  ,  array  ,  0  ,  I  )  ; 
System  .  arraycopy  (  y  ,  0  ,  array  ,  I  ,  y  .  length  )  ; 
System  .  arraycopy  (  x  ,  I  ,  array  ,  I  +  y  .  length  ,  x  .  length  -  I  )  ; 
return   array  ; 
} 



















public   static   int  [  ]  [  ]  deleteColumnsRange  (  int  [  ]  [  ]  x  ,  int   J1  ,  int   J2  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  .  length  ]  [  x  [  0  ]  .  length  -  (  J2  -  J1  +  1  )  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  J1  )  ; 
System  .  arraycopy  (  x  [  i  ]  ,  J2  +  1  ,  array  [  i  ]  ,  J1  ,  x  [  i  ]  .  length  -  (  J2  +  1  )  )  ; 
} 
return   array  ; 
} 



















public   static   int  [  ]  [  ]  deleteColumns  (  int  [  ]  [  ]  x  ,  int  ...  J  )  { 
return   transpose  (  deleteRows  (  transpose  (  x  )  ,  J  )  )  ; 
} 
















public   static   int  [  ]  [  ]  deleteRowsRange  (  int  [  ]  [  ]  x  ,  int   I1  ,  int   I2  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  .  length  -  (  I2  -  I1  +  1  )  ]  [  x  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  I1  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
for  (  int   i  =  0  ;  i  <  x  .  length  -  I2  -  1  ;  i  ++  )  { 
System  .  arraycopy  (  x  [  i  +  I2  +  1  ]  ,  0  ,  array  [  i  +  I1  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
} 
return   array  ; 
} 
















public   static   int  [  ]  [  ]  deleteRows  (  int  [  ]  [  ]  x  ,  int  ...  I  )  { 
int  [  ]  [  ]  array  =  new   int  [  x  .  length  -  I  .  length  ]  [  x  [  0  ]  .  length  ]  ; 
int   i2  =  0  ; 
for  (  int   i  =  0  ;  i  <  x  .  length  ;  i  ++  )  { 
if  (  !  into  (  i  ,  I  )  )  { 
System  .  arraycopy  (  x  [  i  ]  ,  0  ,  array  [  i2  ]  ,  0  ,  x  [  i  ]  .  length  )  ; 
i2  ++  ; 
} 
} 
return   array  ; 
} 















public   static   int  [  ]  deleteRange  (  int  [  ]  x  ,  int   J1  ,  int   J2  )  { 
int  [  ]  array  =  new   int  [  x  .  length  -  (  J2  -  J1  +  1  )  ]  ; 
System  .  arraycopy  (  x  ,  0  ,  array  ,  0  ,  J1  )  ; 
System  .  arraycopy  (  x  ,  J2  +  1  ,  array  ,  J1  ,  x  .  length  -  (  J2  +  1  )  )  ; 
return   array  ; 
} 














public   static   int  [  ]  delete  (  int  [  ]  x  ,  int  ...  J  )  { 
int  [  ]  array  =  new   int  [  x  .  length  -  J  .  length  ]  ; 
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

















public   static   int  [  ]  min  (  int  [  ]  [  ]  M  )  { 
int  [  ]  min  =  new   int  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  min  .  length  ;  j  ++  )  { 
min  [  j  ]  =  M  [  0  ]  [  j  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
min  [  j  ]  =  Math  .  min  (  min  [  j  ]  ,  M  [  i  ]  [  j  ]  )  ; 
} 
} 
return   min  ; 
} 






public   static   int   min  (  int  ...  M  )  { 
int   min  =  M  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
min  =  Math  .  min  (  min  ,  M  [  i  ]  )  ; 
} 
return   min  ; 
} 

















public   static   int  [  ]  max  (  int  [  ]  [  ]  M  )  { 
int  [  ]  max  =  new   int  [  M  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  max  .  length  ;  j  ++  )  { 
max  [  j  ]  =  M  [  0  ]  [  j  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
max  [  j  ]  =  Math  .  max  (  max  [  j  ]  ,  M  [  i  ]  [  j  ]  )  ; 
} 
} 
return   max  ; 
} 






public   static   int   max  (  int  ...  M  )  { 
int   max  =  M  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
max  =  Math  .  max  (  max  ,  M  [  i  ]  )  ; 
} 
return   max  ; 
} 

















public   static   int  [  ]  minIndex  (  int  [  ]  [  ]  M  )  { 
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






public   static   int   minIndex  (  int  ...  M  )  { 
int   minI  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  <  M  [  minI  ]  )  { 
minI  =  i  ; 
} 
} 
return   minI  ; 
} 

















public   static   int  [  ]  maxIndex  (  int  [  ]  [  ]  M  )  { 
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






public   static   int   maxIndex  (  int  ...  M  )  { 
int   maxI  =  0  ; 
for  (  int   i  =  1  ;  i  <  M  .  length  ;  i  ++  )  { 
if  (  M  [  i  ]  >  M  [  maxI  ]  )  { 
maxI  =  i  ; 
} 
} 
return   maxI  ; 
} 






public   static   int   sum  (  int  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  ; 
} 
return   s  ; 
} 






public   static   int  [  ]  sum  (  int  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
int  [  ]  X  =  new   int  [  n  ]  ; 
int   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  [  j  ]  ; 
} 
X  [  j  ]  =  s  ; 
} 
return   X  ; 
} 













public   static   int  [  ]  cumSum  (  int  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int  [  ]  X  =  new   int  [  m  ]  ; 
int   s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  ; 
X  [  i  ]  =  s  ; 
} 
return   X  ; 
} 





















public   static   int  [  ]  [  ]  cumSum  (  int  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
int  [  ]  [  ]  X  =  new   int  [  m  ]  [  n  ]  ; 
int   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  0  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  +=  v  [  i  ]  [  j  ]  ; 
X  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 






public   static   int   product  (  int  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   p  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
p  *=  v  [  i  ]  ; 
} 
return   p  ; 
} 







public   static   int  [  ]  product  (  int  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
int  [  ]  X  =  new   int  [  n  ]  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
int   p  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
p  *=  v  [  i  ]  [  j  ]  ; 
} 
X  [  j  ]  =  p  ; 
} 
return   X  ; 
} 













public   static   int  [  ]  cumProduct  (  int  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int  [  ]  X  =  new   int  [  m  ]  ; 
int   s  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  *=  v  [  i  ]  ; 
X  [  i  ]  =  s  ; 
} 
return   X  ; 
} 




















public   static   int  [  ]  [  ]  cumProduct  (  int  [  ]  [  ]  v  )  { 
int   m  =  v  .  length  ; 
int   n  =  v  [  0  ]  .  length  ; 
int  [  ]  [  ]  X  =  new   int  [  m  ]  [  n  ]  ; 
int   s  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
s  =  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
s  *=  v  [  i  ]  [  j  ]  ; 
X  [  i  ]  [  j  ]  =  s  ; 
} 
} 
return   X  ; 
} 















public   static   String   toString  (  int  [  ]  ...  v  )  { 
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


















public   static   String   toString  (  String   format  ,  int  [  ]  ...  v  )  { 
StringBuffer   str  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  v  [  i  ]  .  length  -  1  ;  j  ++  )  { 
str  .  append  (  String  .  format  (  format  +  " "  ,  v  [  i  ]  [  j  ]  )  )  ; 
} 
str  .  append  (  String  .  format  (  format  ,  v  [  i  ]  [  v  [  i  ]  .  length  -  1  ]  )  )  ; 
if  (  i  <  v  .  length  -  1  )  { 
str  .  append  (  "\n"  )  ; 
} 
} 
return   str  .  toString  (  )  ; 
} 







public   static   int  [  ]  [  ]  transpose  (  int  [  ]  [  ]  M  )  { 
int  [  ]  [  ]  tM  =  new   int  [  M  [  0  ]  .  length  ]  [  M  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  tM  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  tM  [  0  ]  .  length  ;  j  ++  )  { 
tM  [  i  ]  [  j  ]  =  M  [  j  ]  [  i  ]  ; 
} 
} 
return   tM  ; 
} 
} 

