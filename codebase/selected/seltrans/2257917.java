package   edu  .  berkeley  .  guir  .  quill  .  util  ; 

import   java  .  lang  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  BitSet  ; 
import   edu  .  berkeley  .  guir  .  quill  .  util  .  Misc  ; 








public   class   Matrix  { 

public   static   int   sigFigs  =  6  ; 

public   static   int   width  =  12  ; 

private   Matrix  (  )  { 
} 




public   static   void   prettyPrint  (  double   matrix  [  ]  [  ]  )  { 
int   rows  =  matrix  .  length  ; 
int   cols  =  matrix  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
String   s  =  Misc  .  toString  (  matrix  [  i  ]  [  j  ]  ,  sigFigs  )  ; 
for  (  int   foo  =  s  .  length  (  )  ;  foo  <  width  ;  foo  ++  )  System  .  out  .  print  (  " "  )  ; 
System  .  out  .  print  (  s  )  ; 
} 
System  .  out  .  println  (  )  ; 
} 
} 

public   static   void   prettyPrint  (  Double   matrix  [  ]  [  ]  )  { 
int   rows  =  matrix  .  length  ; 
int   cols  =  matrix  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
String   s  =  Misc  .  toString  (  matrix  [  i  ]  [  j  ]  .  doubleValue  (  )  ,  sigFigs  )  ; 
for  (  int   foo  =  s  .  length  (  )  ;  foo  <  width  ;  foo  ++  )  System  .  out  .  print  (  " "  )  ; 
System  .  out  .  print  (  s  )  ; 
} 
System  .  out  .  println  (  )  ; 
} 
} 

public   static   void   prettyPrint  (  double   vector  [  ]  )  { 
double  [  ]  [  ]  m  =  {  vector  }  ; 
prettyPrint  (  m  )  ; 
} 




public   static   void   print  (  double   matrix  [  ]  [  ]  )  { 
int   rows  =  matrix  .  length  ; 
int   cols  =  matrix  [  0  ]  .  length  ; 
System  .  out  .  print  (  "#MATRIX< "  )  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
System  .  out  .  print  (  "( "  )  ; 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
System  .  out  .  print  (  matrix  [  i  ]  [  j  ]  +  " "  )  ; 
} 
System  .  out  .  print  (  ")"  )  ; 
} 
System  .  out  .  println  (  ">"  )  ; 
} 





public   static   double  [  ]  [  ]  transpose  (  double   matrix  [  ]  [  ]  ,  boolean   inPlace  )  { 
int   rows  =  matrix  .  length  ; 
int   cols  =  matrix  [  0  ]  .  length  ; 
if  (  !  inPlace  )  { 
double   result  [  ]  [  ]  =  new   double  [  cols  ]  [  rows  ]  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  j  ]  [  i  ]  =  matrix  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 
if  (  rows  !=  cols  )  { 
System  .  out  .  println  (  "Transpose in place requires a square matrix"  )  ; 
}  else  { 
double   dum  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
dum  =  matrix  [  i  ]  [  j  ]  ; 
matrix  [  i  ]  [  j  ]  =  matrix  [  j  ]  [  i  ]  ; 
matrix  [  j  ]  [  i  ]  =  dum  ; 
} 
} 
} 
return   matrix  ; 
} 




public   static   double  [  ]  [  ]  add  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  ,  double   result  [  ]  [  ]  )  { 
int   rows  =  arg1  .  length  ; 
int   cols  =  arg1  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  [  i  ]  [  j  ]  +  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 




public   static   double  [  ]  [  ]  add  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  )  { 
int   rows  =  arg1  .  length  ; 
int   cols  =  arg1  [  0  ]  .  length  ; 
double   result  [  ]  [  ]  =  new   double  [  rows  ]  [  cols  ]  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  [  i  ]  [  j  ]  +  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 




public   static   double  [  ]  [  ]  subtract  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  ,  double   result  [  ]  [  ]  )  { 
int   rows  =  arg1  .  length  ; 
int   cols  =  arg1  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  [  i  ]  [  j  ]  -  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 




public   static   double  [  ]  [  ]  subtract  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  )  { 
int   rows  =  arg1  .  length  ; 
int   cols  =  arg1  [  0  ]  .  length  ; 
double   result  [  ]  [  ]  =  new   double  [  rows  ]  [  cols  ]  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  [  i  ]  [  j  ]  -  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 




public   static   double  [  ]  [  ]  multiply  (  double   arg1  ,  double   arg2  [  ]  [  ]  ,  double   result  [  ]  [  ]  )  { 
int   rows  =  arg2  .  length  ; 
int   cols  =  arg2  [  0  ]  .  length  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  *  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 




public   static   double  [  ]  [  ]  multiply  (  double   arg1  ,  double   arg2  [  ]  [  ]  )  { 
int   rows  =  arg2  .  length  ; 
int   cols  =  arg2  [  0  ]  .  length  ; 
double   result  [  ]  [  ]  =  new   double  [  rows  ]  [  cols  ]  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
result  [  i  ]  [  j  ]  =  arg1  *  arg2  [  i  ]  [  j  ]  ; 
} 
} 
return   result  ; 
} 






public   static   double  [  ]  [  ]  multiply  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  ,  double   result  [  ]  [  ]  )  { 
double   sum  ; 
for  (  int   i  =  0  ;  i  <  arg1  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  arg2  [  0  ]  .  length  ;  j  ++  )  { 
sum  =  0  ; 
for  (  int   k  =  0  ;  k  <  arg1  [  0  ]  .  length  ;  k  ++  )  { 
sum  +=  arg1  [  i  ]  [  k  ]  *  arg2  [  k  ]  [  j  ]  ; 
} 
result  [  i  ]  [  j  ]  =  sum  ; 
} 
} 
return   result  ; 
} 






public   static   double  [  ]  [  ]  multiply  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  )  { 
double  [  ]  [  ]  result  =  new   double  [  arg1  .  length  ]  [  arg2  [  0  ]  .  length  ]  ; 
double   sum  ; 
for  (  int   i  =  0  ;  i  <  arg1  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  arg2  [  0  ]  .  length  ;  j  ++  )  { 
sum  =  0  ; 
for  (  int   k  =  0  ;  k  <  arg1  [  0  ]  .  length  ;  k  ++  )  { 
sum  +=  arg1  [  i  ]  [  k  ]  *  arg2  [  k  ]  [  j  ]  ; 
} 
result  [  i  ]  [  j  ]  =  sum  ; 
} 
} 
return   result  ; 
} 





public   static   double   dotMultiply  (  double   arg1  [  ]  [  ]  ,  double   arg2  [  ]  [  ]  )  { 
double   result  =  0  ; 
if  (  arg2  .  length  ==  1  )  { 
for  (  int   i  =  0  ;  i  <  arg1  .  length  ;  i  ++  )  { 
result  +=  arg1  [  i  ]  [  0  ]  *  arg2  [  0  ]  [  i  ]  ; 
} 
} 
if  (  arg1  .  length  ==  1  )  { 
for  (  int   i  =  0  ;  i  <  arg2  .  length  ;  i  ++  )  { 
result  +=  arg1  [  0  ]  [  i  ]  *  arg2  [  i  ]  [  0  ]  ; 
} 
} 
return   result  ; 
} 

public   static   double   dotMultiply  (  double   v1  [  ]  ,  double   v2  [  ]  )  { 
double   result  =  0  ; 
for  (  int   i  =  0  ;  i  <  v1  .  length  ;  i  ++  )  { 
result  +=  v1  [  i  ]  *  v2  [  i  ]  ; 
} 
return   result  ; 
} 







public   static   double  [  ]  [  ]  luDecompose  (  double   matrix  [  ]  [  ]  ,  int   indx  [  ]  ,  boolean   parity  )  { 
int   n  =  matrix  .  length  ; 
int   i  ,  j  ,  k  ,  imax  =  0  ; 
double   amax  ,  dum  =  0  ; 
double   scaling  [  ]  =  new   double  [  n  ]  ; 
double   tiny  =  1.0E-20  ; 
parity  =  true  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
amax  =  0  ; 
for  (  j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  Math  .  abs  (  matrix  [  i  ]  [  j  ]  )  >  amax  )  { 
amax  =  matrix  [  i  ]  [  j  ]  ; 
} 
} 
if  (  amax  ==  0  )  { 
System  .  err  .  println  (  "Matrix.luDecompose: Singular Matrix"  )  ; 
} 
scaling  [  i  ]  =  1.0  /  amax  ; 
} 
for  (  j  =  0  ;  j  <  n  ;  j  ++  )  { 
for  (  i  =  0  ;  i  <  j  ;  i  ++  )  { 
dum  =  matrix  [  i  ]  [  j  ]  ; 
for  (  k  =  0  ;  k  <  i  ;  k  ++  )  { 
dum  -=  matrix  [  i  ]  [  k  ]  *  matrix  [  k  ]  [  j  ]  ; 
} 
matrix  [  i  ]  [  j  ]  =  dum  ; 
} 
amax  =  0.0  ; 
for  (  i  =  j  ;  i  <  n  ;  i  ++  )  { 
dum  =  matrix  [  i  ]  [  j  ]  ; 
for  (  k  =  0  ;  k  <  j  ;  k  ++  )  { 
dum  -=  matrix  [  i  ]  [  k  ]  *  matrix  [  k  ]  [  j  ]  ; 
} 
matrix  [  i  ]  [  j  ]  =  dum  ; 
if  (  scaling  [  i  ]  *  Math  .  abs  (  dum  )  >  amax  )  { 
amax  =  scaling  [  i  ]  *  Math  .  abs  (  dum  )  ; 
imax  =  i  ; 
} 
} 
if  (  j  !=  imax  )  { 
for  (  k  =  0  ;  k  <  n  ;  k  ++  )  { 
dum  =  matrix  [  imax  ]  [  k  ]  ; 
matrix  [  imax  ]  [  k  ]  =  matrix  [  j  ]  [  k  ]  ; 
matrix  [  j  ]  [  k  ]  =  dum  ; 
} 
parity  =  !  parity  ; 
scaling  [  imax  ]  =  scaling  [  j  ]  ; 
} 
indx  [  j  ]  =  imax  ; 
if  (  matrix  [  j  ]  [  j  ]  ==  0.0  )  { 
matrix  [  j  ]  [  j  ]  =  tiny  ; 
} 
if  (  j  !=  n  )  { 
dum  =  1.0  /  matrix  [  j  ]  [  j  ]  ; 
for  (  i  =  j  +  1  ;  i  <  n  ;  i  ++  )  { 
matrix  [  i  ]  [  j  ]  *=  dum  ; 
} 
} 
} 
return   matrix  ; 
} 







public   static   double  [  ]  [  ]  luBackSubstitute  (  double  [  ]  [  ]  a  ,  double  [  ]  [  ]  b  ,  int   indx  [  ]  )  { 
int   n  =  a  .  length  ; 
int   i  ,  ip  ,  j  ,  ii  =  -  1  ; 
double   sum  =  0  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
ip  =  indx  [  i  ]  ; 
sum  =  b  [  ip  ]  [  0  ]  ; 
b  [  ip  ]  [  0  ]  =  b  [  i  ]  [  0  ]  ; 
if  (  ii  !=  -  1  )  { 
for  (  j  =  ii  ;  j  <  i  ;  j  ++  )  { 
sum  -=  a  [  i  ]  [  j  ]  *  b  [  j  ]  [  0  ]  ; 
} 
}  else  { 
if  (  sum  !=  0  )  { 
ii  =  i  ; 
} 
} 
b  [  i  ]  [  0  ]  =  sum  ; 
} 
for  (  i  =  n  -  1  ;  i  >=  0  ;  i  --  )  { 
sum  =  b  [  i  ]  [  0  ]  ; 
for  (  j  =  i  +  1  ;  j  <  n  ;  j  ++  )  { 
sum  -=  a  [  i  ]  [  j  ]  *  b  [  j  ]  [  0  ]  ; 
} 
b  [  i  ]  [  0  ]  =  sum  /  a  [  i  ]  [  i  ]  ; 
} 
return   b  ; 
} 






public   static   double  [  ]  [  ]  solve  (  double   a  [  ]  [  ]  ,  double   b  [  ]  [  ]  )  { 
int   size  =  a  .  length  ; 
boolean   parity  =  true  ; 
int   indx  [  ]  =  new   int  [  size  ]  ; 
Matrix  .  luDecompose  (  a  ,  indx  ,  parity  )  ; 
Matrix  .  luBackSubstitute  (  a  ,  b  ,  indx  )  ; 
return   b  ; 
} 




public   static   double  [  ]  [  ]  invert  (  double   arg  [  ]  [  ]  )  { 
int   size  =  arg  .  length  ; 
double   col  [  ]  [  ]  =  new   double  [  size  ]  [  1  ]  ; 
int   indx  [  ]  =  new   int  [  size  ]  ; 
double   result  [  ]  [  ]  =  new   double  [  size  ]  [  size  ]  ; 
double   lud  [  ]  [  ]  =  new   double  [  size  ]  [  size  ]  ; 
lud  =  Matrix  .  luDecompose  (  arg  ,  indx  ,  true  )  ; 
for  (  int   j  =  0  ;  j  <  size  ;  j  ++  )  { 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
col  [  i  ]  [  0  ]  =  0  ; 
} 
col  [  j  ]  [  0  ]  =  1  ; 
Matrix  .  luBackSubstitute  (  lud  ,  col  ,  indx  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
result  [  i  ]  [  j  ]  =  col  [  i  ]  [  0  ]  ; 
} 
} 
return   result  ; 
} 

public   static   double  [  ]  [  ]  clone  (  double  [  ]  [  ]  src  )  { 
double  [  ]  [  ]  result  =  new   double  [  src  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  src  .  length  ;  i  ++  )  result  [  i  ]  =  (  double  [  ]  )  src  [  i  ]  .  clone  (  )  ; 
return   result  ; 
} 

public   static   double  [  ]  [  ]  safeInvert  (  double   m  [  ]  [  ]  )  { 
double   dummy  [  ]  [  ]  =  clone  (  m  )  ; 
return   invert  (  dummy  )  ; 
} 

public   static   int   bitcount  (  BitSet   mask  )  { 
int   result  =  0  ; 
for  (  int   i  =  0  ;  i  <  mask  .  size  (  )  ;  i  ++  )  if  (  mask  .  get  (  i  )  )  result  ++  ; 
return   result  ; 
} 

public   static   double  [  ]  [  ]  slice  (  double  [  ]  [  ]  m  ,  BitSet   rowMask  ,  BitSet   colMask  )  { 
int   i  ,  ri  ,  j  ,  rj  ; 
int   nrows  =  m  .  length  ; 
int   ncols  =  m  [  0  ]  .  length  ; 
double  [  ]  [  ]  r  =  new   double  [  nrows  ]  [  ncols  ]  ; 
for  (  i  =  0  ,  ri  =  0  ;  i  <  nrows  ;  i  ++  )  if  (  rowMask  .  get  (  i  )  )  { 
for  (  j  =  0  ,  rj  =  0  ;  j  <  ncols  ;  j  ++  )  if  (  colMask  .  get  (  j  )  )  r  [  ri  ]  [  rj  ++  ]  =  m  [  i  ]  [  j  ]  ; 
ri  ++  ; 
} 
return   r  ; 
} 





public   static   double  [  ]  [  ]  deSlice  (  double  [  ]  [  ]  m  ,  double   fill  ,  BitSet   rowMask  ,  BitSet   colMask  ,  double  [  ]  [  ]  r  )  { 
int   i  ,  ri  ,  j  ,  rj  ; 
int   nrows  =  m  .  length  ; 
int   ncols  =  m  [  0  ]  .  length  ; 
if  (  r  ==  null  )  r  =  new   double  [  nrows  ]  [  ncols  ]  ; 
fill  (  r  ,  fill  )  ; 
for  (  i  =  0  ,  ri  =  0  ;  i  <  nrows  ;  i  ++  )  { 
if  (  rowMask  .  get  (  i  )  )  { 
for  (  j  =  0  ,  rj  =  0  ;  j  <  ncols  ;  j  ++  )  { 
if  (  colMask  .  get  (  j  )  )  r  [  i  ]  [  j  ]  =  m  [  ri  ]  [  rj  ++  ]  ; 
} 
ri  ++  ; 
} 
} 
return   r  ; 
} 

public   static   void   fill  (  double  [  ]  [  ]  m  ,  double   fill  )  { 
for  (  int   i  =  0  ;  i  <  m  .  length  ;  i  ++  )  for  (  int   j  =  0  ;  j  <  m  [  i  ]  .  length  ;  j  ++  )  m  [  i  ]  [  j  ]  =  fill  ; 
} 


















public   static   double   myInvert  (  double  [  ]  [  ]  ym  ,  double  [  ]  [  ]  rm  )  { 
int   i  ,  j  ,  k  ; 
double   det  ,  biga  ,  recip_biga  ,  hold  ; 
int   l  [  ]  =  new   int  [  ym  .  length  ]  ; 
int   m  [  ]  =  new   int  [  ym  .  length  ]  ; 
int   n  ; 
if  (  ym  .  length  !=  ym  [  0  ]  .  length  )  { 
System  .  err  .  println  (  "myInvert: matrix not square"  )  ; 
return   Double  .  NaN  ; 
} 
n  =  ym  .  length  ; 
if  (  n  !=  rm  .  length  ||  n  !=  rm  [  0  ]  .  length  )  { 
System  .  err  .  println  (  "myInvert: result wrong size"  )  ; 
return   Double  .  NaN  ; 
} 
if  (  ym  !=  rm  )  for  (  i  =  0  ;  i  <  n  ;  i  ++  )  for  (  j  =  0  ;  j  <  n  ;  j  ++  )  rm  [  i  ]  [  j  ]  =  ym  [  i  ]  [  j  ]  ; 
det  =  1.0  ; 
for  (  k  =  0  ;  k  <  n  ;  k  ++  )  { 
l  [  k  ]  =  k  ; 
m  [  k  ]  =  k  ; 
biga  =  rm  [  k  ]  [  k  ]  ; 
for  (  i  =  k  ;  i  <  n  ;  i  ++  )  for  (  j  =  k  ;  j  <  n  ;  j  ++  )  if  (  Math  .  abs  (  rm  [  i  ]  [  j  ]  )  >  Math  .  abs  (  biga  )  )  { 
biga  =  rm  [  i  ]  [  j  ]  ; 
l  [  k  ]  =  i  ; 
m  [  k  ]  =  j  ; 
} 
i  =  l  [  k  ]  ; 
if  (  i  >  k  )  for  (  j  =  0  ;  j  <  n  ;  j  ++  )  { 
hold  =  -  rm  [  k  ]  [  j  ]  ; 
rm  [  k  ]  [  j  ]  =  rm  [  i  ]  [  j  ]  ; 
rm  [  i  ]  [  j  ]  =  hold  ; 
} 
j  =  m  [  k  ]  ; 
if  (  j  >  k  )  for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
hold  =  -  rm  [  i  ]  [  k  ]  ; 
rm  [  i  ]  [  k  ]  =  rm  [  i  ]  [  j  ]  ; 
rm  [  i  ]  [  j  ]  =  hold  ; 
} 
if  (  biga  ==  0.0  )  { 
return   0.0  ; 
} 
recip_biga  =  1  /  biga  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  if  (  i  !=  k  )  rm  [  i  ]  [  k  ]  *=  -  recip_biga  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  if  (  i  !=  k  )  { 
hold  =  rm  [  i  ]  [  k  ]  ; 
for  (  j  =  0  ;  j  <  n  ;  j  ++  )  if  (  j  !=  k  )  rm  [  i  ]  [  j  ]  +=  hold  *  rm  [  k  ]  [  j  ]  ; 
} 
for  (  j  =  0  ;  j  <  n  ;  j  ++  )  if  (  j  !=  k  )  rm  [  k  ]  [  j  ]  *=  recip_biga  ; 
det  *=  biga  ; 
rm  [  k  ]  [  k  ]  =  recip_biga  ; 
} 
for  (  k  =  n  -  1  ;  k  >=  0  ;  k  --  )  { 
i  =  l  [  k  ]  ; 
if  (  i  >  k  )  for  (  j  =  0  ;  j  <  n  ;  j  ++  )  { 
hold  =  rm  [  j  ]  [  k  ]  ; 
rm  [  j  ]  [  k  ]  =  -  rm  [  j  ]  [  i  ]  ; 
rm  [  j  ]  [  i  ]  =  hold  ; 
} 
j  =  m  [  k  ]  ; 
if  (  j  >  k  )  for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
hold  =  rm  [  k  ]  [  i  ]  ; 
rm  [  k  ]  [  i  ]  =  -  rm  [  j  ]  [  i  ]  ; 
rm  [  j  ]  [  i  ]  =  hold  ; 
} 
} 
return   det  ; 
} 

public   static   double   QuadraticForm  (  double  [  ]  v  ,  double  [  ]  [  ]  m  )  { 
int   n  ; 
double   result  =  0  ; 
n  =  v  .  length  ; 
if  (  n  !=  m  .  length  ||  n  !=  m  [  0  ]  .  length  )  { 
System  .  err  .  println  (  "Matrix.QuadraticForm: bad matrix size"  )  ; 
return   0  ; 
} 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
result  +=  m  [  i  ]  [  j  ]  *  v  [  i  ]  *  v  [  j  ]  ; 
} 
return   result  ; 
} 

public   static   double  [  ]  subtract  (  double   v1  [  ]  ,  double   v2  [  ]  )  { 
double  [  ]  result  =  new   double  [  v1  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v1  .  length  ;  i  ++  )  { 
result  [  i  ]  =  v1  [  i  ]  -  v2  [  i  ]  ; 
} 
return   result  ; 
} 

public   static   double  [  ]  add  (  double   v1  [  ]  ,  double   v2  [  ]  )  { 
double  [  ]  result  =  new   double  [  v1  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v1  .  length  ;  i  ++  )  { 
result  [  i  ]  =  v1  [  i  ]  +  v2  [  i  ]  ; 
} 
return   result  ; 
} 

public   static   double  [  ]  multiply  (  double   factor  ,  double   v  [  ]  )  { 
double  [  ]  result  =  new   double  [  v  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  i  ++  )  { 
result  [  i  ]  =  factor  *  v  [  i  ]  ; 
} 
return   result  ; 
} 




public   static   void   main  (  String   args  [  ]  )  { 
double   a  [  ]  [  ]  =  {  {  3  ,  7  }  ,  {  8  ,  10  }  }  ; 
double   c  [  ]  [  ]  =  {  {  0  ,  0  }  ,  {  0  ,  0  }  }  ; 
double   b  [  ]  [  ]  =  {  {  6  ,  9  ,  3  }  ,  {  -  2  ,  -  1  ,  7  }  ,  {  0  ,  1  ,  5  }  }  ; 
double   d  [  ]  [  ]  =  {  {  0  ,  0  ,  0  }  ,  {  0  ,  0  ,  0  }  ,  {  0  ,  0  ,  0  }  }  ; 
int   indx  [  ]  =  new   int  [  3  ]  ; 
System  .  out  .  println  (  "b:"  )  ; 
Matrix  .  print  (  b  )  ; 
System  .  out  .  println  (  "inverse of b:"  )  ; 
d  =  Matrix  .  safeInvert  (  b  )  ; 
Matrix  .  print  (  d  )  ; 
System  .  out  .  println  (  "b:"  )  ; 
Matrix  .  print  (  b  )  ; 
} 
} 

