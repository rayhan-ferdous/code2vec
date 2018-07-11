package   joptima  .  fortran  .  linalg  ; 









































































public   class   Blas_f77   extends   Object  { 

















public   static   int   isamax_f77  (  int   n  ,  double   x  [  ]  ,  int   incx  )  { 
double   xmax  ; 
int   isamax  ,  i  ,  ix  ; 
if  (  n  <  1  )  { 
isamax  =  0  ; 
}  else   if  (  n  ==  1  )  { 
isamax  =  1  ; 
}  else   if  (  incx  ==  1  )  { 
isamax  =  1  ; 
xmax  =  Math  .  abs  (  x  [  1  ]  )  ; 
for  (  i  =  2  ;  i  <=  n  ;  i  ++  )  { 
if  (  Math  .  abs  (  x  [  i  ]  )  >  xmax  )  { 
isamax  =  i  ; 
xmax  =  Math  .  abs  (  x  [  i  ]  )  ; 
} 
} 
}  else  { 
isamax  =  1  ; 
ix  =  1  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  )  ; 
ix  +=  incx  ; 
for  (  i  =  2  ;  i  <=  n  ;  i  ++  )  { 
if  (  Math  .  abs  (  x  [  ix  ]  )  >  xmax  )  { 
isamax  =  i  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  )  ; 
} 
ix  +=  incx  ; 
} 
} 
return   isamax  ; 
} 



















public   static   int   colisamax_f77  (  int   n  ,  double   x  [  ]  [  ]  ,  int   incx  ,  int   begin  ,  int   j  )  { 
double   xmax  ; 
int   isamax  ,  i  ,  ix  ; 
if  (  n  <  1  )  { 
isamax  =  0  ; 
}  else   if  (  n  ==  1  )  { 
isamax  =  1  ; 
}  else   if  (  incx  ==  1  )  { 
isamax  =  1  ; 
ix  =  begin  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  ; 
ix  ++  ; 
for  (  i  =  2  ;  i  <=  n  ;  i  ++  )  { 
if  (  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  >  xmax  )  { 
isamax  =  i  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  ; 
} 
ix  ++  ; 
} 
}  else  { 
isamax  =  1  ; 
ix  =  begin  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  ; 
ix  +=  incx  ; 
for  (  i  =  2  ;  i  <=  n  ;  i  ++  )  { 
if  (  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  >  xmax  )  { 
isamax  =  i  ; 
xmax  =  Math  .  abs  (  x  [  ix  ]  [  j  ]  )  ; 
} 
ix  +=  incx  ; 
} 
} 
return   isamax  ; 
} 





















public   static   void   daxpy_f77  (  int   n  ,  double   da  ,  double   dx  [  ]  ,  int   incx  ,  double   dy  [  ]  ,  int   incy  )  { 
int   i  ,  ix  ,  iy  ,  m  ; 
if  (  n  <=  0  )  return  ; 
if  (  da  ==  0.0  )  return  ; 
if  (  (  incx  ==  1  )  &&  (  incy  ==  1  )  )  { 
m  =  n  %  4  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
dy  [  i  ]  +=  da  *  dx  [  i  ]  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  4  )  { 
dy  [  i  ]  +=  da  *  dx  [  i  ]  ; 
dy  [  i  +  1  ]  +=  da  *  dx  [  i  +  1  ]  ; 
dy  [  i  +  2  ]  +=  da  *  dx  [  i  +  2  ]  ; 
dy  [  i  +  3  ]  +=  da  *  dx  [  i  +  3  ]  ; 
} 
return  ; 
}  else  { 
ix  =  1  ; 
iy  =  1  ; 
if  (  incx  <  0  )  ix  =  (  -  n  +  1  )  *  incx  +  1  ; 
if  (  incy  <  0  )  iy  =  (  -  n  +  1  )  *  incy  +  1  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
dy  [  iy  ]  +=  da  *  dx  [  ix  ]  ; 
ix  +=  incx  ; 
iy  +=  incy  ; 
} 
return  ; 
} 
} 



















public   static   double   ddot_f77  (  int   n  ,  double   dx  [  ]  ,  int   incx  ,  double   dy  [  ]  ,  int   incy  )  { 
double   ddot  ; 
int   i  ,  ix  ,  iy  ,  m  ; 
ddot  =  0.0  ; 
if  (  n  <=  0  )  return   ddot  ; 
if  (  (  incx  ==  1  )  &&  (  incy  ==  1  )  )  { 
m  =  n  %  5  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
ddot  +=  dx  [  i  ]  *  dy  [  i  ]  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  5  )  { 
ddot  +=  dx  [  i  ]  *  dy  [  i  ]  +  dx  [  i  +  1  ]  *  dy  [  i  +  1  ]  +  dx  [  i  +  2  ]  *  dy  [  i  +  2  ]  +  dx  [  i  +  3  ]  *  dy  [  i  +  3  ]  +  dx  [  i  +  4  ]  *  dy  [  i  +  4  ]  ; 
} 
return   ddot  ; 
}  else  { 
ix  =  1  ; 
iy  =  1  ; 
if  (  incx  <  0  )  ix  =  (  -  n  +  1  )  *  incx  +  1  ; 
if  (  incy  <  0  )  iy  =  (  -  n  +  1  )  *  incy  +  1  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
ddot  +=  dx  [  ix  ]  *  dy  [  iy  ]  ; 
ix  +=  incx  ; 
iy  +=  incy  ; 
} 
return   ddot  ; 
} 
} 


















public   static   void   dscal_f77  (  int   n  ,  double   da  ,  double   dx  [  ]  ,  int   incx  )  { 
int   i  ,  m  ,  nincx  ; 
if  (  n  <=  0  ||  incx  <=  0  )  return  ; 
if  (  incx  ==  1  )  { 
m  =  n  %  5  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
dx  [  i  ]  *=  da  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  5  )  { 
dx  [  i  ]  *=  da  ; 
dx  [  i  +  1  ]  *=  da  ; 
dx  [  i  +  2  ]  *=  da  ; 
dx  [  i  +  3  ]  *=  da  ; 
dx  [  i  +  4  ]  *=  da  ; 
} 
return  ; 
}  else  { 
nincx  =  n  *  incx  ; 
for  (  i  =  1  ;  i  <=  nincx  ;  i  +=  incx  )  { 
dx  [  i  ]  *=  da  ; 
} 
return  ; 
} 
} 



















public   static   void   dswap_f77  (  int   n  ,  double   dx  [  ]  ,  int   incx  ,  double   dy  [  ]  ,  int   incy  )  { 
double   dtemp  ; 
int   i  ,  ix  ,  iy  ,  m  ; 
if  (  n  <=  0  )  return  ; 
if  (  (  incx  ==  1  )  &&  (  incy  ==  1  )  )  { 
m  =  n  %  3  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
dtemp  =  dx  [  i  ]  ; 
dx  [  i  ]  =  dy  [  i  ]  ; 
dy  [  i  ]  =  dtemp  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  3  )  { 
dtemp  =  dx  [  i  ]  ; 
dx  [  i  ]  =  dy  [  i  ]  ; 
dy  [  i  ]  =  dtemp  ; 
dtemp  =  dx  [  i  +  1  ]  ; 
dx  [  i  +  1  ]  =  dy  [  i  +  1  ]  ; 
dy  [  i  +  1  ]  =  dtemp  ; 
dtemp  =  dx  [  i  +  2  ]  ; 
dx  [  i  +  2  ]  =  dy  [  i  +  2  ]  ; 
dy  [  i  +  2  ]  =  dtemp  ; 
} 
return  ; 
}  else  { 
ix  =  1  ; 
iy  =  1  ; 
if  (  incx  <  0  )  ix  =  (  -  n  +  1  )  *  incx  +  1  ; 
if  (  incy  <  0  )  iy  =  (  -  n  +  1  )  *  incy  +  1  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
dtemp  =  dx  [  ix  ]  ; 
dx  [  ix  ]  =  dy  [  iy  ]  ; 
dy  [  iy  ]  =  dtemp  ; 
ix  +=  incx  ; 
iy  +=  incy  ; 
} 
return  ; 
} 
} 




















public   static   double   dnrm2_f77  (  int   n  ,  double   x  [  ]  ,  int   incx  )  { 
double   absxi  ,  norm  ,  scale  ,  ssq  ,  fac  ; 
int   ix  ,  limit  ; 
if  (  n  <  1  ||  incx  <  1  )  { 
norm  =  0.0  ; 
}  else   if  (  n  ==  1  )  { 
norm  =  Math  .  abs  (  x  [  1  ]  )  ; 
}  else  { 
scale  =  0.0  ; 
ssq  =  1.0  ; 
limit  =  1  +  (  n  -  1  )  *  incx  ; 
for  (  ix  =  1  ;  ix  <=  limit  ;  ix  +=  incx  )  { 
if  (  x  [  ix  ]  !=  0.0  )  { 
absxi  =  Math  .  abs  (  x  [  ix  ]  )  ; 
if  (  scale  <  absxi  )  { 
fac  =  scale  /  absxi  ; 
ssq  =  1.0  +  ssq  *  fac  *  fac  ; 
scale  =  absxi  ; 
}  else  { 
fac  =  absxi  /  scale  ; 
ssq  +=  fac  *  fac  ; 
} 
} 
} 
norm  =  scale  *  Math  .  sqrt  (  ssq  )  ; 
} 
return   norm  ; 
} 



















public   static   void   dcopy_f77  (  int   n  ,  double   dx  [  ]  ,  int   incx  ,  double   dy  [  ]  ,  int   incy  )  { 
double   dtemp  ; 
int   i  ,  ix  ,  iy  ,  m  ; 
if  (  n  <=  0  )  return  ; 
if  (  (  incx  ==  1  )  &&  (  incy  ==  1  )  )  { 
m  =  n  %  7  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
dy  [  i  ]  =  dx  [  i  ]  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  7  )  { 
dy  [  i  ]  =  dx  [  i  ]  ; 
dy  [  i  +  1  ]  =  dx  [  i  +  1  ]  ; 
dy  [  i  +  2  ]  =  dx  [  i  +  2  ]  ; 
dy  [  i  +  3  ]  =  dx  [  i  +  3  ]  ; 
dy  [  i  +  4  ]  =  dx  [  i  +  4  ]  ; 
dy  [  i  +  5  ]  =  dx  [  i  +  5  ]  ; 
dy  [  i  +  6  ]  =  dx  [  i  +  6  ]  ; 
} 
return  ; 
}  else  { 
ix  =  1  ; 
iy  =  1  ; 
if  (  incx  <  0  )  ix  =  (  -  n  +  1  )  *  incx  +  1  ; 
if  (  incy  <  0  )  iy  =  (  -  n  +  1  )  *  incy  +  1  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
dy  [  iy  ]  =  dx  [  ix  ]  ; 
ix  +=  incx  ; 
iy  +=  incy  ; 
} 
return  ; 
} 
} 

















public   static   void   drotg_f77  (  double   rotvec  [  ]  )  { 
double   a  ,  b  ,  c  ,  s  ,  roe  ,  scale  ,  r  ,  z  ,  ra  ,  rb  ; 
a  =  rotvec  [  1  ]  ; 
b  =  rotvec  [  2  ]  ; 
roe  =  b  ; 
if  (  Math  .  abs  (  a  )  >  Math  .  abs  (  b  )  )  roe  =  a  ; 
scale  =  Math  .  abs  (  a  )  +  Math  .  abs  (  b  )  ; 
if  (  scale  !=  0.0  )  { 
ra  =  a  /  scale  ; 
rb  =  b  /  scale  ; 
r  =  scale  *  Math  .  sqrt  (  ra  *  ra  +  rb  *  rb  )  ; 
r  =  sign_f77  (  1.0  ,  roe  )  *  r  ; 
c  =  a  /  r  ; 
s  =  b  /  r  ; 
z  =  1.0  ; 
if  (  Math  .  abs  (  a  )  >  Math  .  abs  (  b  )  )  z  =  s  ; 
if  (  (  Math  .  abs  (  b  )  >=  Math  .  abs  (  a  )  )  &&  (  c  !=  0.0  )  )  z  =  1.0  /  c  ; 
}  else  { 
c  =  1.0  ; 
s  =  0.0  ; 
r  =  0.0  ; 
z  =  0.0  ; 
} 
a  =  r  ; 
b  =  z  ; 
rotvec  [  1  ]  =  a  ; 
rotvec  [  2  ]  =  b  ; 
rotvec  [  3  ]  =  c  ; 
rotvec  [  4  ]  =  s  ; 
return  ; 
} 























public   static   void   colaxpy_f77  (  int   nrow  ,  double   a  ,  double   x  [  ]  [  ]  ,  int   begin  ,  int   j1  ,  int   j2  )  { 
int   i  ,  m  ,  mpbegin  ,  end  ; 
if  (  nrow  <=  0  )  return  ; 
if  (  a  ==  0.0  )  return  ; 
m  =  nrow  %  4  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
x  [  i  ]  [  j2  ]  +=  a  *  x  [  i  ]  [  j1  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  4  )  { 
x  [  i  ]  [  j2  ]  +=  a  *  x  [  i  ]  [  j1  ]  ; 
x  [  i  +  1  ]  [  j2  ]  +=  a  *  x  [  i  +  1  ]  [  j1  ]  ; 
x  [  i  +  2  ]  [  j2  ]  +=  a  *  x  [  i  +  2  ]  [  j1  ]  ; 
x  [  i  +  3  ]  [  j2  ]  +=  a  *  x  [  i  +  3  ]  [  j1  ]  ; 
} 
return  ; 
} 























public   static   void   colvaxpy_f77  (  int   nrow  ,  double   a  ,  double   x  [  ]  [  ]  ,  double   y  [  ]  ,  int   begin  ,  int   j  )  { 
int   i  ,  m  ,  mpbegin  ,  end  ; 
if  (  nrow  <=  0  )  return  ; 
if  (  a  ==  0.0  )  return  ; 
m  =  nrow  %  4  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
y  [  i  ]  +=  a  *  x  [  i  ]  [  j  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  4  )  { 
y  [  i  ]  +=  a  *  x  [  i  ]  [  j  ]  ; 
y  [  i  +  1  ]  +=  a  *  x  [  i  +  1  ]  [  j  ]  ; 
y  [  i  +  2  ]  +=  a  *  x  [  i  +  2  ]  [  j  ]  ; 
y  [  i  +  3  ]  +=  a  *  x  [  i  +  3  ]  [  j  ]  ; 
} 
return  ; 
} 
























public   static   void   colvraxpy_f77  (  int   nrow  ,  double   a  ,  double   y  [  ]  ,  double   x  [  ]  [  ]  ,  int   begin  ,  int   j  )  { 
int   i  ,  m  ,  mpbegin  ,  end  ; 
if  (  nrow  <=  0  )  return  ; 
if  (  a  ==  0.0  )  return  ; 
m  =  nrow  %  4  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
x  [  i  ]  [  j  ]  +=  a  *  y  [  i  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  4  )  { 
x  [  i  ]  [  j  ]  +=  a  *  y  [  i  ]  ; 
x  [  i  +  1  ]  [  j  ]  +=  a  *  y  [  i  +  1  ]  ; 
x  [  i  +  2  ]  [  j  ]  +=  a  *  y  [  i  +  2  ]  ; 
x  [  i  +  3  ]  [  j  ]  +=  a  *  y  [  i  +  3  ]  ; 
} 
return  ; 
} 



















public   static   double   coldot_f77  (  int   nrow  ,  double   x  [  ]  [  ]  ,  int   begin  ,  int   j1  ,  int   j2  )  { 
double   coldot  ; 
int   i  ,  m  ,  mpbegin  ,  end  ; 
coldot  =  0.0  ; 
if  (  nrow  <=  0  )  return   coldot  ; 
m  =  nrow  %  5  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
coldot  +=  x  [  i  ]  [  j1  ]  *  x  [  i  ]  [  j2  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  5  )  { 
coldot  +=  x  [  i  ]  [  j1  ]  *  x  [  i  ]  [  j2  ]  +  x  [  i  +  1  ]  [  j1  ]  *  x  [  i  +  1  ]  [  j2  ]  +  x  [  i  +  2  ]  [  j1  ]  *  x  [  i  +  2  ]  [  j2  ]  +  x  [  i  +  3  ]  [  j1  ]  *  x  [  i  +  3  ]  [  j2  ]  +  x  [  i  +  4  ]  [  j1  ]  *  x  [  i  +  4  ]  [  j2  ]  ; 
} 
return   coldot  ; 
} 




















public   static   double   colvdot_f77  (  int   nrow  ,  double   x  [  ]  [  ]  ,  double   y  [  ]  ,  int   begin  ,  int   j  )  { 
double   colvdot  ; 
int   i  ,  m  ,  mpbegin  ,  end  ; 
colvdot  =  0.0  ; 
if  (  nrow  <=  0  )  return   colvdot  ; 
m  =  nrow  %  5  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
colvdot  +=  x  [  i  ]  [  j  ]  *  y  [  i  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  5  )  { 
colvdot  +=  x  [  i  ]  [  j  ]  *  y  [  i  ]  +  x  [  i  +  1  ]  [  j  ]  *  y  [  i  +  1  ]  +  x  [  i  +  2  ]  [  j  ]  *  y  [  i  +  2  ]  +  x  [  i  +  3  ]  [  j  ]  *  y  [  i  +  3  ]  +  x  [  i  +  4  ]  [  j  ]  *  y  [  i  +  4  ]  ; 
} 
return   colvdot  ; 
} 



















public   static   void   colscal_f77  (  int   nrow  ,  double   a  ,  double   x  [  ]  [  ]  ,  int   begin  ,  int   j  )  { 
int   i  ,  m  ,  mpbegin  ,  end  ; 
if  (  nrow  <=  0  )  return  ; 
m  =  nrow  %  5  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
x  [  i  ]  [  j  ]  *=  a  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  5  )  { 
x  [  i  ]  [  j  ]  *=  a  ; 
x  [  i  +  1  ]  [  j  ]  *=  a  ; 
x  [  i  +  2  ]  [  j  ]  *=  a  ; 
x  [  i  +  3  ]  [  j  ]  *=  a  ; 
x  [  i  +  4  ]  [  j  ]  *=  a  ; 
} 
return  ; 
} 


















public   static   void   dscalp_f77  (  int   nrow  ,  double   a  ,  double   x  [  ]  ,  int   begin  )  { 
int   i  ,  m  ,  mpbegin  ,  end  ; 
if  (  nrow  <=  0  )  return  ; 
m  =  nrow  %  5  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
x  [  i  ]  *=  a  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  5  )  { 
x  [  i  ]  *=  a  ; 
x  [  i  +  1  ]  *=  a  ; 
x  [  i  +  2  ]  *=  a  ; 
x  [  i  +  3  ]  *=  a  ; 
x  [  i  +  4  ]  *=  a  ; 
} 
return  ; 
} 


















public   static   void   colswap_f77  (  int   n  ,  double   x  [  ]  [  ]  ,  int   j1  ,  int   j2  )  { 
double   temp  ; 
int   i  ,  m  ; 
if  (  n  <=  0  )  return  ; 
m  =  n  %  3  ; 
for  (  i  =  1  ;  i  <=  m  ;  i  ++  )  { 
temp  =  x  [  i  ]  [  j1  ]  ; 
x  [  i  ]  [  j1  ]  =  x  [  i  ]  [  j2  ]  ; 
x  [  i  ]  [  j2  ]  =  temp  ; 
} 
for  (  i  =  m  +  1  ;  i  <=  n  ;  i  +=  3  )  { 
temp  =  x  [  i  ]  [  j1  ]  ; 
x  [  i  ]  [  j1  ]  =  x  [  i  ]  [  j2  ]  ; 
x  [  i  ]  [  j2  ]  =  temp  ; 
temp  =  x  [  i  +  1  ]  [  j1  ]  ; 
x  [  i  +  1  ]  [  j1  ]  =  x  [  i  +  1  ]  [  j2  ]  ; 
x  [  i  +  1  ]  [  j2  ]  =  temp  ; 
temp  =  x  [  i  +  2  ]  [  j1  ]  ; 
x  [  i  +  2  ]  [  j1  ]  =  x  [  i  +  2  ]  [  j2  ]  ; 
x  [  i  +  2  ]  [  j2  ]  =  temp  ; 
} 
return  ; 
} 





















public   static   double   colnrm2_f77  (  int   nrow  ,  double   x  [  ]  [  ]  ,  int   begin  ,  int   j  )  { 
double   absxij  ,  norm  ,  scale  ,  ssq  ,  fac  ; 
int   i  ,  end  ; 
if  (  nrow  <  1  )  { 
norm  =  0.0  ; 
}  else   if  (  nrow  ==  1  )  { 
norm  =  Math  .  abs  (  x  [  begin  ]  [  j  ]  )  ; 
}  else  { 
scale  =  0.0  ; 
ssq  =  1.0  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <=  end  ;  i  ++  )  { 
if  (  x  [  i  ]  [  j  ]  !=  0.0  )  { 
absxij  =  Math  .  abs  (  x  [  i  ]  [  j  ]  )  ; 
if  (  scale  <  absxij  )  { 
fac  =  scale  /  absxij  ; 
ssq  =  1.0  +  ssq  *  fac  *  fac  ; 
scale  =  absxij  ; 
}  else  { 
fac  =  absxij  /  scale  ; 
ssq  +=  fac  *  fac  ; 
} 
} 
} 
norm  =  scale  *  Math  .  sqrt  (  ssq  )  ; 
} 
return   norm  ; 
} 




















public   static   double   dnrm2p_f77  (  int   nrow  ,  double   x  [  ]  ,  int   begin  )  { 
double   absxi  ,  norm  ,  scale  ,  ssq  ,  fac  ; 
int   i  ,  end  ; 
if  (  nrow  <  1  )  { 
norm  =  0.0  ; 
}  else   if  (  nrow  ==  1  )  { 
norm  =  Math  .  abs  (  x  [  begin  ]  )  ; 
}  else  { 
scale  =  0.0  ; 
ssq  =  1.0  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <=  end  ;  i  ++  )  { 
if  (  x  [  i  ]  !=  0.0  )  { 
absxi  =  Math  .  abs  (  x  [  i  ]  )  ; 
if  (  scale  <  absxi  )  { 
fac  =  scale  /  absxi  ; 
ssq  =  1.0  +  ssq  *  fac  *  fac  ; 
scale  =  absxi  ; 
}  else  { 
fac  =  absxi  /  scale  ; 
ssq  +=  fac  *  fac  ; 
} 
} 
} 
norm  =  scale  *  Math  .  sqrt  (  ssq  )  ; 
} 
return   norm  ; 
} 



















public   static   void   dcopyp_f77  (  int   nrow  ,  double   x  [  ]  ,  double   y  [  ]  ,  int   begin  )  { 
double   temp  ; 
int   i  ,  m  ,  mpbegin  ,  end  ; 
m  =  nrow  %  7  ; 
mpbegin  =  m  +  begin  ; 
end  =  begin  +  nrow  -  1  ; 
for  (  i  =  begin  ;  i  <  mpbegin  ;  i  ++  )  { 
y  [  i  ]  =  x  [  i  ]  ; 
} 
for  (  i  =  mpbegin  ;  i  <=  end  ;  i  +=  7  )  { 
y  [  i  ]  =  x  [  i  ]  ; 
y  [  i  +  1  ]  =  x  [  i  +  1  ]  ; 
y  [  i  +  2  ]  =  x  [  i  +  2  ]  ; 
y  [  i  +  3  ]  =  x  [  i  +  3  ]  ; 
y  [  i  +  4  ]  =  x  [  i  +  4  ]  ; 
y  [  i  +  5  ]  =  x  [  i  +  5  ]  ; 
y  [  i  +  6  ]  =  x  [  i  +  6  ]  ; 
} 
return  ; 
} 



















public   static   void   colrot_f77  (  int   n  ,  double   x  [  ]  [  ]  ,  int   j1  ,  int   j2  ,  double   c  ,  double   s  )  { 
double   temp  ; 
int   i  ; 
if  (  n  <=  0  )  return  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
temp  =  c  *  x  [  i  ]  [  j1  ]  +  s  *  x  [  i  ]  [  j2  ]  ; 
x  [  i  ]  [  j2  ]  =  c  *  x  [  i  ]  [  j2  ]  -  s  *  x  [  i  ]  [  j1  ]  ; 
x  [  i  ]  [  j1  ]  =  temp  ; 
} 
return  ; 
} 













public   static   double   sign_f77  (  double   a  ,  double   b  )  { 
if  (  b  <  0.0  )  { 
return  -  Math  .  abs  (  a  )  ; 
}  else  { 
return   Math  .  abs  (  a  )  ; 
} 
} 
















public   static   void   matmat_f77  (  double   a  [  ]  [  ]  ,  double   b  [  ]  [  ]  ,  double   c  [  ]  [  ]  ,  int   n  ,  int   p  ,  int   r  )  { 
double   vdot  ; 
int   i  ,  j  ,  k  ,  m  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
for  (  j  =  1  ;  j  <=  r  ;  j  ++  )  { 
vdot  =  0.0  ; 
m  =  p  %  5  ; 
for  (  k  =  1  ;  k  <=  m  ;  k  ++  )  { 
vdot  +=  a  [  i  ]  [  k  ]  *  b  [  k  ]  [  j  ]  ; 
} 
for  (  k  =  m  +  1  ;  k  <=  p  ;  k  +=  5  )  { 
vdot  +=  a  [  i  ]  [  k  ]  *  b  [  k  ]  [  j  ]  +  a  [  i  ]  [  k  +  1  ]  *  b  [  k  +  1  ]  [  j  ]  +  a  [  i  ]  [  k  +  2  ]  *  b  [  k  +  2  ]  [  j  ]  +  a  [  i  ]  [  k  +  3  ]  *  b  [  k  +  3  ]  [  j  ]  +  a  [  i  ]  [  k  +  4  ]  *  b  [  k  +  4  ]  [  j  ]  ; 
} 
c  [  i  ]  [  j  ]  =  vdot  ; 
} 
} 
} 














public   static   void   mattran_f77  (  double   a  [  ]  [  ]  ,  double   at  [  ]  [  ]  ,  int   n  ,  int   p  )  { 
int   i  ,  j  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
for  (  j  =  1  ;  j  <=  p  ;  j  ++  )  { 
at  [  j  ]  [  i  ]  =  a  [  i  ]  [  j  ]  ; 
} 
} 
} 















public   static   void   matvec_f77  (  double   a  [  ]  [  ]  ,  double   b  [  ]  ,  double   c  [  ]  ,  int   n  ,  int   p  )  { 
double   vdot  ; 
int   i  ,  j  ,  m  ; 
for  (  i  =  1  ;  i  <=  n  ;  i  ++  )  { 
vdot  =  0.0  ; 
m  =  p  %  5  ; 
for  (  j  =  1  ;  j  <=  m  ;  j  ++  )  { 
vdot  +=  a  [  i  ]  [  j  ]  *  b  [  j  ]  ; 
} 
for  (  j  =  m  +  1  ;  j  <=  p  ;  j  +=  5  )  { 
vdot  +=  a  [  i  ]  [  j  ]  *  b  [  j  ]  +  a  [  i  ]  [  j  +  1  ]  *  b  [  j  +  1  ]  +  a  [  i  ]  [  j  +  2  ]  *  b  [  j  +  2  ]  +  a  [  i  ]  [  j  +  3  ]  *  b  [  j  +  3  ]  +  a  [  i  ]  [  j  +  4  ]  *  b  [  j  +  4  ]  ; 
} 
c  [  i  ]  =  vdot  ; 
} 
} 
} 

