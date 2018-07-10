package   joelib2  .  math  ; 

import   org  .  apache  .  log4j  .  Category  ; 





































public   class   CoordinateTransformation   implements   java  .  io  .  Serializable  { 

private   static   final   long   serialVersionUID  =  1L  ; 

private   static   Category   logger  =  Category  .  getInstance  (  CoordinateTransformation  .  class  .  getName  (  )  )  ; 




protected   double  [  ]  _euler  =  new   double  [  3  ]  ; 




protected   double  [  ]  _rmat  =  new   double  [  9  ]  ; 




protected   double  [  ]  _trans  =  new   double  [  3  ]  ; 




public   CoordinateTransformation  (  )  { 
_trans  [  0  ]  =  0.0  ; 
_trans  [  1  ]  =  0.0  ; 
_trans  [  2  ]  =  0.0  ; 
_euler  [  0  ]  =  0.0  ; 
_euler  [  1  ]  =  0.0  ; 
_euler  [  2  ]  =  0.0  ; 
_rmat  [  0  ]  =  1.0  ; 
_rmat  [  1  ]  =  0.0  ; 
_rmat  [  2  ]  =  0.0  ; 
_rmat  [  3  ]  =  0.0  ; 
_rmat  [  4  ]  =  1.0  ; 
_rmat  [  5  ]  =  0.0  ; 
_rmat  [  6  ]  =  0.0  ; 
_rmat  [  7  ]  =  0.0  ; 
_rmat  [  8  ]  =  1.0  ; 
} 








public   final   CoordinateTransformation   add  (  final   CoordinateTransformation   ct2  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
CoordinateTransformation   ct  =  new   CoordinateTransformation  (  )  ; 
transform  (  xyz  ,  4  )  ; 
ct2  .  transform  (  xyz  ,  4  )  ; 
ct  .  setup  (  xyz  )  ; 
return   ct  ; 
} 











public   CoordinateTransformation   adding  (  final   CoordinateTransformation   ct  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
transform  (  xyz  ,  4  )  ; 
ct  .  transform  (  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
return   this  ; 
} 




public   void   clear  (  )  { 
_trans  [  0  ]  =  0.0f  ; 
_trans  [  1  ]  =  0.0f  ; 
_trans  [  2  ]  =  0.0f  ; 
_euler  [  0  ]  =  0.0f  ; 
_euler  [  1  ]  =  0.0f  ; 
_euler  [  2  ]  =  0.0f  ; 
_rmat  [  0  ]  =  1.0f  ; 
_rmat  [  1  ]  =  0.0f  ; 
_rmat  [  2  ]  =  0.0f  ; 
_rmat  [  3  ]  =  0.0f  ; 
_rmat  [  4  ]  =  1.0f  ; 
_rmat  [  5  ]  =  0.0f  ; 
_rmat  [  6  ]  =  0.0f  ; 
_rmat  [  7  ]  =  0.0f  ; 
_rmat  [  8  ]  =  1.0f  ; 
} 












public   final   void   getEulerTranslation  (  double  [  ]  euler  ,  double  [  ]  trans  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
euler  [  i  ]  =  _euler  [  i  ]  ; 
trans  [  i  ]  =  _trans  [  i  ]  ; 
} 
} 











public   final   void   getRmatrixTranslation  (  double  [  ]  rmat  ,  double  [  ]  trans  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
trans  [  i  ]  =  _trans  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  9  ;  i  ++  )  { 
rmat  [  i  ]  =  _rmat  [  i  ]  ; 
} 
} 








public   void   getRmatrixTranslation  (  BasicMatrix3D   rmatrix  ,  BasicVector3D   tvec  )  { 
double  [  ]  rmat  =  new   double  [  9  ]  ; 
double  [  ]  trans  =  new   double  [  3  ]  ; 
getRmatrixTranslation  (  rmat  ,  trans  )  ; 
int   irow  ; 
int   icolumn  ; 
for  (  irow  =  0  ;  irow  <  3  ;  irow  ++  )  { 
for  (  icolumn  =  0  ;  icolumn  <  3  ;  icolumn  ++  )  { 
rmatrix  .  set  (  irow  ,  icolumn  ,  rmat  [  (  3  *  irow  )  +  icolumn  ]  )  ; 
} 
} 
tvec  .  set  (  trans  )  ; 
} 












public   final   void   getTranslationEuler  (  double  [  ]  trans  ,  double  [  ]  euler  )  { 
euler  [  0  ]  =  _euler  [  0  ]  ; 
euler  [  1  ]  =  _euler  [  1  ]  ; 
euler  [  2  ]  =  _euler  [  2  ]  ; 
trans  [  0  ]  =  0.0f  ; 
trans  [  1  ]  =  0.0f  ; 
trans  [  2  ]  =  0.0f  ; 
transform  (  trans  ,  1  )  ; 
applyEulerInvert  (  euler  ,  trans  ,  1  )  ; 
} 











public   final   void   getTranslationRmatrix  (  double  [  ]  trans  ,  double  [  ]  rmat  )  { 
double  [  ]  euler  =  new   double  [  3  ]  ; 
getTranslationEuler  (  trans  ,  euler  )  ; 
eulerToRmatrix  (  euler  ,  rmat  )  ; 
} 








public   void   getTranslationRmatrix  (  BasicVector3D   tvec  ,  BasicMatrix3D   rmatrix  )  { 
double  [  ]  rmat  =  new   double  [  9  ]  ; 
double  [  ]  trans  =  new   double  [  3  ]  ; 
getTranslationRmatrix  (  trans  ,  rmat  )  ; 
int   irow  ; 
int   icolumn  ; 
for  (  irow  =  0  ;  irow  <  3  ;  irow  ++  )  { 
for  (  icolumn  =  0  ;  icolumn  <  3  ;  icolumn  ++  )  { 
rmatrix  .  set  (  irow  ,  icolumn  ,  rmat  [  (  3  *  irow  )  +  icolumn  ]  )  ; 
} 
} 
tvec  .  set  (  trans  )  ; 
} 





public   void   invert  (  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
xyz  [  (  3  *  i  )  +  0  ]  -=  _trans  [  0  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  -=  _trans  [  1  ]  ; 
xyz  [  (  3  *  i  )  +  2  ]  -=  _trans  [  2  ]  ; 
} 
applyEulerInvert  (  _euler  ,  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
} 







public   CoordinateTransformation   set  (  final   CoordinateTransformation   cp  )  { 
_trans  [  0  ]  =  cp  .  _trans  [  0  ]  ; 
_trans  [  1  ]  =  cp  .  _trans  [  1  ]  ; 
_trans  [  2  ]  =  cp  .  _trans  [  2  ]  ; 
_euler  [  0  ]  =  cp  .  _euler  [  0  ]  ; 
_euler  [  1  ]  =  cp  .  _euler  [  1  ]  ; 
_euler  [  2  ]  =  cp  .  _euler  [  2  ]  ; 
_rmat  [  0  ]  =  cp  .  _rmat  [  0  ]  ; 
_rmat  [  1  ]  =  cp  .  _rmat  [  1  ]  ; 
_rmat  [  2  ]  =  cp  .  _rmat  [  2  ]  ; 
_rmat  [  3  ]  =  cp  .  _rmat  [  3  ]  ; 
_rmat  [  4  ]  =  cp  .  _rmat  [  4  ]  ; 
_rmat  [  5  ]  =  cp  .  _rmat  [  5  ]  ; 
_rmat  [  6  ]  =  cp  .  _rmat  [  6  ]  ; 
_rmat  [  7  ]  =  cp  .  _rmat  [  7  ]  ; 
_rmat  [  8  ]  =  cp  .  _rmat  [  8  ]  ; 
return   this  ; 
} 









public   boolean   setup  (  double  [  ]  in_xyz  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
double  [  ]  y  =  new   double  [  4  ]  ; 
double  [  ]  z  =  new   double  [  4  ]  ; 
int   i  ; 
System  .  arraycopy  (  in_xyz  ,  0  ,  xyz  ,  0  ,  12  )  ; 
System  .  arraycopy  (  in_xyz  ,  3  *  1  ,  y  ,  0  ,  3  )  ; 
System  .  arraycopy  (  in_xyz  ,  3  *  2  ,  z  ,  0  ,  3  )  ; 
_trans  [  0  ]  =  xyz  [  0  ]  ; 
_trans  [  1  ]  =  xyz  [  1  ]  ; 
_trans  [  2  ]  =  xyz  [  2  ]  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
xyz  [  (  3  *  i  )  +  0  ]  -=  _trans  [  0  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  -=  _trans  [  1  ]  ; 
xyz  [  (  3  *  i  )  +  2  ]  -=  _trans  [  2  ]  ; 
} 
double   sn  ; 
double   cs  ; 
double   mag  ; 
mag  =  Math  .  sqrt  (  (  z  [  0  ]  *  z  [  0  ]  )  +  (  z  [  1  ]  *  z  [  1  ]  )  )  ; 
if  (  mag  >  0.000001  )  { 
cs  =  z  [  1  ]  /  mag  ; 
sn  =  z  [  0  ]  /  mag  ; 
_euler  [  2  ]  =  (  double  )  angle  (  sn  ,  cs  )  ; 
}  else  { 
cs  =  1.0f  ; 
sn  =  0.0f  ; 
_euler  [  2  ]  =  0.0f  ; 
} 
double   xx  ; 
double   yy  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs  *  xx  )  -  (  sn  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs  *  yy  )  +  (  sn  *  xx  )  ; 
} 
cs  =  z  [  2  ]  ; 
sn  =  z  [  1  ]  ; 
_euler  [  1  ]  =  (  double  )  angle  (  sn  ,  cs  )  ; 
double   zz  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
zz  =  xyz  [  (  3  *  i  )  +  2  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs  *  yy  )  -  (  sn  *  zz  )  ; 
xyz  [  (  3  *  i  )  +  2  ]  =  (  cs  *  zz  )  +  (  sn  *  yy  )  ; 
} 
cs  =  y  [  1  ]  ; 
sn  =  y  [  0  ]  ; 
_euler  [  0  ]  =  (  double  )  angle  (  sn  ,  cs  )  ; 
eulerToRmatrix  (  _euler  ,  _rmat  )  ; 
for  (  i  =  0  ;  i  <  4  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs  *  xx  )  -  (  sn  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs  *  yy  )  +  (  sn  *  xx  )  ; 
} 
boolean   error  =  false  ; 
double   tol  =  0.0001f  ; 
if  (  Math  .  abs  (  xyz  [  0  ]  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  Math  .  abs  (  xyz  [  1  ]  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  Math  .  abs  (  xyz  [  2  ]  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  Math  .  abs  (  xyz  [  (  3  *  1  )  +  0  ]  -  1.0  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  Math  .  abs  (  xyz  [  (  3  *  2  )  +  1  ]  -  1.0  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  Math  .  abs  (  xyz  [  (  3  *  3  )  +  2  ]  -  1.0  )  >  tol  )  { 
error  =  true  ; 
} 
if  (  error  )  { 
logger  .  error  (  "WARNING! JOECoordTrans.setup(double []) probable invalid input"  )  ; 
return   false  ; 
} 
return   true  ; 
} 

















public   void   setup  (  double  [  ]  init_xyz  ,  double  [  ]  final_xyz  ,  int   n  )  { 
clear  (  )  ; 
double  [  ]  xyz1  =  new   double  [  12  ]  ; 
double  [  ]  xyz2  =  new   double  [  12  ]  ; 
int   i  ; 
int   j  ; 
if  (  n  !=  0  )  { 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xyz1  [  i  ]  =  init_xyz  [  i  ]  ; 
xyz2  [  i  ]  =  final_xyz  [  i  ]  ; 
} 
}  else  { 
return  ; 
} 
if  (  n  >  1  )  { 
double   dist  ; 
double   maxdist  ; 
int   id  =  1  ; 
maxdist  =  0.0  ; 
for  (  i  =  1  ;  i  <  n  ;  i  ++  )  { 
for  (  dist  =  0.0  ,  j  =  0  ;  j  <  3  ;  j  ++  )  { 
dist  +=  (  (  xyz1  [  j  ]  -  init_xyz  [  (  3  *  i  )  +  j  ]  )  *  (  xyz1  [  j  ]  -  init_xyz  [  (  3  *  i  )  +  j  ]  )  )  ; 
} 
dist  =  Math  .  sqrt  (  dist  )  ; 
if  (  dist  >  maxdist  )  { 
maxdist  =  dist  ; 
id  =  i  ; 
} 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xyz1  [  3  +  i  ]  =  init_xyz  [  (  3  *  id  )  +  i  ]  ; 
xyz2  [  3  +  i  ]  =  final_xyz  [  (  3  *  id  )  +  i  ]  ; 
} 
}  else  { 
double  [  ]  euler  =  new   double  [  3  ]  ; 
double  [  ]  trans  =  new   double  [  3  ]  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
euler  [  i  ]  =  0.0f  ; 
trans  [  i  ]  =  xyz2  [  i  ]  -  xyz1  [  i  ]  ; 
} 
setupEulerTranslation  (  euler  ,  trans  )  ; 
return  ; 
} 
if  (  n  >  2  )  { 
double   mag  ; 
double   maxcross  ; 
double  [  ]  xx  =  new   double  [  3  ]  ; 
double  [  ]  yy  =  new   double  [  3  ]  ; 
double  [  ]  cr  =  new   double  [  3  ]  ; 
int   ic  =  1  ; 
for  (  j  =  0  ;  j  <  3  ;  j  ++  )  { 
xx  [  j  ]  =  xyz1  [  3  +  j  ]  -  xyz1  [  j  ]  ; 
} 
maxcross  =  0.0f  ; 
for  (  i  =  1  ;  i  <  n  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  3  ;  j  ++  )  { 
yy  [  j  ]  =  init_xyz  [  (  3  *  i  )  +  j  ]  -  xyz1  [  j  ]  ; 
} 
cr  [  0  ]  =  (  xx  [  1  ]  *  yy  [  2  ]  )  -  (  xx  [  2  ]  *  yy  [  1  ]  )  ; 
cr  [  1  ]  =  (  -  xx  [  0  ]  *  yy  [  2  ]  )  +  (  xx  [  2  ]  *  yy  [  0  ]  )  ; 
cr  [  2  ]  =  (  xx  [  0  ]  *  yy  [  1  ]  )  -  (  xx  [  1  ]  *  yy  [  0  ]  )  ; 
mag  =  Math  .  sqrt  (  (  cr  [  0  ]  *  cr  [  0  ]  )  +  (  cr  [  1  ]  *  cr  [  1  ]  )  +  (  cr  [  2  ]  *  cr  [  2  ]  )  )  ; 
if  (  mag  >  maxcross  )  { 
maxcross  =  mag  ; 
ic  =  i  ; 
} 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xyz1  [  6  +  i  ]  =  init_xyz  [  (  3  *  ic  )  +  i  ]  ; 
xyz2  [  6  +  i  ]  =  final_xyz  [  (  3  *  ic  )  +  i  ]  ; 
} 
}  else  { 
double  [  ]  xx  =  new   double  [  3  ]  ; 
double  [  ]  yy  =  new   double  [  3  ]  ; 
xx  [  0  ]  =  xyz1  [  3  +  0  ]  -  xyz1  [  0  ]  ; 
xx  [  1  ]  =  xyz1  [  3  +  1  ]  -  xyz1  [  1  ]  ; 
xx  [  2  ]  =  xyz1  [  3  +  2  ]  -  xyz1  [  2  ]  ; 
yy  [  0  ]  =  xx  [  2  ]  ; 
yy  [  1  ]  =  xx  [  0  ]  ; 
yy  [  2  ]  =  xx  [  1  ]  ; 
xyz1  [  6  +  0  ]  =  yy  [  0  ]  +  xyz1  [  0  ]  ; 
xyz1  [  6  +  1  ]  =  yy  [  1  ]  +  xyz1  [  1  ]  ; 
xyz1  [  6  +  2  ]  =  yy  [  2  ]  +  xyz1  [  2  ]  ; 
xx  [  0  ]  =  xyz2  [  3  +  0  ]  -  xyz2  [  0  ]  ; 
xx  [  1  ]  =  xyz2  [  3  +  1  ]  -  xyz2  [  1  ]  ; 
xx  [  2  ]  =  xyz2  [  3  +  2  ]  -  xyz2  [  2  ]  ; 
yy  [  0  ]  =  xx  [  2  ]  ; 
yy  [  1  ]  =  xx  [  0  ]  ; 
yy  [  2  ]  =  xx  [  1  ]  ; 
xyz2  [  6  +  0  ]  =  yy  [  0  ]  +  xyz2  [  0  ]  ; 
xyz2  [  6  +  1  ]  =  yy  [  1  ]  +  xyz2  [  1  ]  ; 
xyz2  [  6  +  2  ]  =  yy  [  2  ]  +  xyz2  [  2  ]  ; 
} 
double   mag  ; 
double   dot  ; 
double  [  ]  xx1  =  new   double  [  3  ]  ; 
double  [  ]  yy1  =  new   double  [  3  ]  ; 
double  [  ]  zz1  =  new   double  [  3  ]  ; 
mag  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xx1  [  i  ]  =  xyz1  [  3  +  i  ]  -  xyz1  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
mag  +=  (  xx1  [  i  ]  *  xx1  [  i  ]  )  ; 
} 
mag  =  Math  .  sqrt  (  mag  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xx1  [  i  ]  /=  mag  ; 
xyz1  [  3  +  i  ]  =  xx1  [  i  ]  +  xyz1  [  i  ]  ; 
} 
dot  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy1  [  i  ]  =  xyz1  [  6  +  i  ]  -  xyz1  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
dot  +=  (  xx1  [  i  ]  *  yy1  [  i  ]  )  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy1  [  i  ]  -=  (  xx1  [  i  ]  *  dot  )  ; 
} 
mag  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
mag  +=  (  yy1  [  i  ]  *  yy1  [  i  ]  )  ; 
} 
mag  =  Math  .  sqrt  (  mag  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy1  [  i  ]  /=  mag  ; 
xyz1  [  6  +  i  ]  =  yy1  [  i  ]  +  xyz1  [  i  ]  ; 
} 
zz1  [  0  ]  =  (  xx1  [  1  ]  *  yy1  [  2  ]  )  -  (  xx1  [  2  ]  *  yy1  [  1  ]  )  ; 
zz1  [  1  ]  =  (  -  xx1  [  0  ]  *  yy1  [  2  ]  )  +  (  xx1  [  2  ]  *  yy1  [  0  ]  )  ; 
zz1  [  2  ]  =  (  xx1  [  0  ]  *  yy1  [  1  ]  )  -  (  xx1  [  1  ]  *  yy1  [  0  ]  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xyz1  [  9  +  i  ]  =  zz1  [  i  ]  +  xyz1  [  i  ]  ; 
} 
double  [  ]  xx2  =  new   double  [  3  ]  ; 
double  [  ]  yy2  =  new   double  [  3  ]  ; 
double  [  ]  zz2  =  new   double  [  3  ]  ; 
mag  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xx2  [  i  ]  =  xyz2  [  3  +  i  ]  -  xyz2  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
mag  +=  (  xx2  [  i  ]  *  xx2  [  i  ]  )  ; 
} 
mag  =  Math  .  sqrt  (  mag  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xx2  [  i  ]  /=  mag  ; 
xyz2  [  3  +  i  ]  =  xx2  [  i  ]  +  xyz2  [  i  ]  ; 
} 
dot  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy2  [  i  ]  =  xyz2  [  6  +  i  ]  -  xyz2  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
dot  +=  (  xx2  [  i  ]  *  yy2  [  i  ]  )  ; 
} 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy2  [  i  ]  -=  (  xx2  [  i  ]  *  dot  )  ; 
} 
mag  =  0.0f  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
mag  +=  (  yy2  [  i  ]  *  yy2  [  i  ]  )  ; 
} 
mag  =  Math  .  sqrt  (  mag  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
yy2  [  i  ]  /=  mag  ; 
xyz2  [  6  +  i  ]  =  yy2  [  i  ]  +  xyz2  [  i  ]  ; 
} 
zz2  [  0  ]  =  (  xx2  [  1  ]  *  yy2  [  2  ]  )  -  (  xx2  [  2  ]  *  yy2  [  1  ]  )  ; 
zz2  [  1  ]  =  (  -  xx2  [  0  ]  *  yy2  [  2  ]  )  +  (  xx2  [  2  ]  *  yy2  [  0  ]  )  ; 
zz2  [  2  ]  =  (  xx2  [  0  ]  *  yy2  [  1  ]  )  -  (  xx2  [  1  ]  *  yy2  [  0  ]  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
xyz2  [  9  +  i  ]  =  zz2  [  i  ]  +  xyz2  [  i  ]  ; 
} 
CoordinateTransformation   cti  =  new   CoordinateTransformation  (  )  ; 
CoordinateTransformation   ctf  =  new   CoordinateTransformation  (  )  ; 
cti  .  setup  (  xyz1  )  ; 
ctf  .  setup  (  xyz2  )  ; 
cti  .  invert  (  )  ; 
this  .  set  (  cti  .  add  (  ctf  )  )  ; 
} 















public   void   setupEulerTranslation  (  double  [  ]  euler  ,  double  [  ]  trans  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
applyEuler  (  euler  ,  xyz  ,  4  )  ; 
applyTranslation  (  trans  ,  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
} 













public   void   setupRmatrixTranslation  (  double  [  ]  rmat  ,  double  [  ]  trans  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
applyRmatrix  (  rmat  ,  xyz  ,  4  )  ; 
applyTranslation  (  trans  ,  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
} 











public   void   setupRmatrixTranslation  (  BasicMatrix3D   rmatrix  ,  BasicVector3D   tvec  )  { 
double  [  ]  rmat  =  new   double  [  9  ]  ; 
double  [  ]  trans  =  new   double  [  3  ]  ; 
int   irow  ; 
int   icolumn  ; 
for  (  irow  =  0  ;  irow  <  3  ;  irow  ++  )  { 
for  (  icolumn  =  0  ;  icolumn  <  3  ;  icolumn  ++  )  { 
rmat  [  (  3  *  irow  )  +  icolumn  ]  =  rmatrix  .  get  (  irow  ,  icolumn  )  ; 
} 
} 
tvec  .  get  (  trans  )  ; 
setupRmatrixTranslation  (  rmat  ,  trans  )  ; 
} 














public   void   setupTranslationEuler  (  double  [  ]  trans  ,  double  [  ]  euler  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0f  ; 
applyTranslation  (  trans  ,  xyz  ,  4  )  ; 
applyEuler  (  euler  ,  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
} 











public   void   setupTranslationRmatrix  (  double  [  ]  trans  ,  double  [  ]  rmat  )  { 
double  [  ]  xyz  =  new   double  [  12  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  12  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  1  )  +  0  ]  =  1.0  ; 
xyz  [  (  3  *  2  )  +  1  ]  =  1.0  ; 
xyz  [  (  3  *  3  )  +  2  ]  =  1.0  ; 
applyTranslation  (  trans  ,  xyz  ,  4  )  ; 
applyRmatrix  (  rmat  ,  xyz  ,  4  )  ; 
setup  (  xyz  )  ; 
} 











public   void   setupTranslationRmatrix  (  BasicVector3D   tvec  ,  BasicMatrix3D   rmatrix  )  { 
double  [  ]  rmat  =  new   double  [  9  ]  ; 
double  [  ]  trans  =  new   double  [  3  ]  ; 
int   irow  ; 
int   icolumn  ; 
for  (  irow  =  0  ;  irow  <  3  ;  irow  ++  )  { 
for  (  icolumn  =  0  ;  icolumn  <  3  ;  icolumn  ++  )  { 
rmat  [  (  3  *  irow  )  +  icolumn  ]  =  rmatrix  .  get  (  irow  ,  icolumn  )  ; 
} 
} 
tvec  .  get  (  trans  )  ; 
setupTranslationRmatrix  (  trans  ,  rmat  )  ; 
} 







public   final   void   transform  (  double  [  ]  xyz  ,  int   n  )  { 
int   i  ; 
double   x  ; 
double   y  ; 
double   z  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
x  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
y  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
z  =  xyz  [  (  3  *  i  )  +  2  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  _rmat  [  0  ]  *  x  )  +  (  _rmat  [  1  ]  *  y  )  +  (  _rmat  [  2  ]  *  z  )  +  _trans  [  0  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  _rmat  [  3  ]  *  x  )  +  (  _rmat  [  4  ]  *  y  )  +  (  _rmat  [  5  ]  *  z  )  +  _trans  [  1  ]  ; 
xyz  [  (  3  *  i  )  +  2  ]  =  (  _rmat  [  6  ]  *  x  )  +  (  _rmat  [  7  ]  *  y  )  +  (  _rmat  [  8  ]  *  z  )  +  _trans  [  2  ]  ; 
} 
} 








protected   double   angle  (  double   sn  ,  double   cs  )  { 
double   angle  =  0.0  ; 
if  (  Math  .  abs  (  cs  )  <  Math  .  abs  (  sn  )  )  { 
angle  =  Math  .  acos  (  cs  )  ; 
if  (  (  sn  <  0.0f  )  &&  (  angle  <  Math  .  PI  )  )  { 
angle  =  (  2.0  *  Math  .  PI  )  -  angle  ; 
}  else   if  (  (  sn  >  0.0f  )  &&  (  angle  >  Math  .  PI  )  )  { 
angle  =  (  2.0  *  Math  .  PI  )  -  angle  ; 
} 
}  else  { 
angle  =  Math  .  asin  (  sn  )  ; 
if  (  angle  <  Math  .  PI  )  { 
if  (  (  cs  <  0.0f  )  &&  (  angle  <  (  0.5  *  Math  .  PI  )  )  )  { 
angle  =  Math  .  PI  -  angle  ; 
}  else   if  (  (  cs  >  0.0f  )  &&  (  angle  >  (  0.5  *  Math  .  PI  )  )  )  { 
angle  =  Math  .  PI  -  angle  ; 
} 
}  else  { 
if  (  (  cs  <  0.0f  )  &&  (  angle  >  (  1.5  *  Math  .  PI  )  )  )  { 
angle  =  (  3.0  *  Math  .  PI  )  -  angle  ; 
} 
if  (  (  cs  >  0.0f  )  &&  (  angle  <  (  1.5  *  Math  .  PI  )  )  )  { 
angle  =  (  3.0  *  Math  .  PI  )  -  angle  ; 
} 
} 
} 
return   angle  ; 
} 











protected   final   void   applyEuler  (  double  [  ]  euler  ,  double  [  ]  xyz  ,  int   n  )  { 
double   cs0  =  Math  .  cos  (  euler  [  0  ]  )  ; 
double   cs1  =  Math  .  cos  (  euler  [  1  ]  )  ; 
double   cs2  =  Math  .  cos  (  euler  [  2  ]  )  ; 
double   sn0  =  Math  .  sin  (  euler  [  0  ]  )  ; 
double   sn1  =  Math  .  sin  (  euler  [  1  ]  )  ; 
double   sn2  =  Math  .  sin  (  euler  [  2  ]  )  ; 
int   i  ; 
double   xx  ; 
double   yy  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs0  *  xx  )  +  (  sn0  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs0  *  yy  )  -  (  sn0  *  xx  )  ; 
} 
double   zz  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
zz  =  xyz  [  (  3  *  i  )  +  2  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs1  *  yy  )  +  (  sn1  *  zz  )  ; 
xyz  [  (  3  *  i  )  +  2  ]  =  (  cs1  *  zz  )  -  (  sn1  *  yy  )  ; 
} 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs2  *  xx  )  +  (  sn2  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs2  *  yy  )  -  (  sn2  *  xx  )  ; 
} 
} 















protected   final   void   applyEulerInvert  (  double  [  ]  euler  ,  double  [  ]  xyz  ,  int   n  )  { 
double   cs0  =  Math  .  cos  (  euler  [  0  ]  )  ; 
double   cs1  =  Math  .  cos  (  euler  [  1  ]  )  ; 
double   cs2  =  Math  .  cos  (  euler  [  2  ]  )  ; 
double   sn0  =  Math  .  sin  (  euler  [  0  ]  )  ; 
double   sn1  =  Math  .  sin  (  euler  [  1  ]  )  ; 
double   sn2  =  Math  .  sin  (  euler  [  2  ]  )  ; 
int   i  ; 
double   xx  ; 
double   yy  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs2  *  xx  )  -  (  sn2  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs2  *  yy  )  +  (  sn2  *  xx  )  ; 
} 
double   zz  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
zz  =  xyz  [  (  3  *  i  )  +  2  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs1  *  yy  )  -  (  sn1  *  zz  )  ; 
xyz  [  (  3  *  i  )  +  2  ]  =  (  cs1  *  zz  )  +  (  sn1  *  yy  )  ; 
} 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
xx  =  xyz  [  (  3  *  i  )  +  0  ]  ; 
yy  =  xyz  [  (  3  *  i  )  +  1  ]  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  (  cs0  *  xx  )  -  (  sn0  *  yy  )  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  (  cs0  *  yy  )  +  (  sn0  *  xx  )  ; 
} 
} 










protected   final   void   applyRmatrix  (  double  [  ]  rmat  ,  double  [  ]  xyz  ,  int   n  )  { 
int   i  ; 
double   x  ; 
double   y  ; 
double   z  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
x  =  (  rmat  [  0  ]  *  xyz  [  (  3  *  i  )  +  0  ]  )  +  (  rmat  [  1  ]  *  xyz  [  (  3  *  i  )  +  1  ]  )  +  (  rmat  [  2  ]  *  xyz  [  (  3  *  i  )  +  2  ]  )  ; 
y  =  (  rmat  [  3  ]  *  xyz  [  (  3  *  i  )  +  0  ]  )  +  (  rmat  [  4  ]  *  xyz  [  (  3  *  i  )  +  1  ]  )  +  (  rmat  [  5  ]  *  xyz  [  (  3  *  i  )  +  2  ]  )  ; 
z  =  (  rmat  [  6  ]  *  xyz  [  (  3  *  i  )  +  0  ]  )  +  (  rmat  [  7  ]  *  xyz  [  (  3  *  i  )  +  1  ]  )  +  (  rmat  [  8  ]  *  xyz  [  (  3  *  i  )  +  2  ]  )  ; 
xyz  [  (  3  *  i  )  +  0  ]  =  x  ; 
xyz  [  (  3  *  i  )  +  1  ]  =  y  ; 
xyz  [  (  3  *  i  )  +  2  ]  =  z  ; 
} 
} 








protected   final   void   applyTranslation  (  double  [  ]  trans  ,  double  [  ]  xyz  ,  int   n  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  n  ;  i  ++  )  { 
xyz  [  (  3  *  i  )  +  0  ]  +=  trans  [  0  ]  ; 
xyz  [  (  3  *  i  )  +  1  ]  +=  trans  [  1  ]  ; 
xyz  [  (  3  *  i  )  +  2  ]  +=  trans  [  2  ]  ; 
} 
} 












protected   final   void   eulerToRmatrix  (  double  [  ]  euler  ,  double  [  ]  rmat  )  { 
double  [  ]  xyz  =  new   double  [  9  ]  ; 
int   i  ; 
int   j  ; 
for  (  i  =  0  ;  i  <  9  ;  i  ++  )  { 
xyz  [  i  ]  =  0.0f  ; 
} 
xyz  [  (  3  *  0  )  +  0  ]  =  1.0f  ; 
xyz  [  (  3  *  1  )  +  1  ]  =  1.0f  ; 
xyz  [  (  3  *  2  )  +  2  ]  =  1.0f  ; 
applyEuler  (  euler  ,  xyz  ,  3  )  ; 
for  (  i  =  0  ;  i  <  3  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  3  ;  j  ++  )  { 
rmat  [  (  3  *  i  )  +  j  ]  =  xyz  [  (  3  *  j  )  +  i  ]  ; 
} 
} 
} 
} 

