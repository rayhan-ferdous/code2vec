package   com  .  solidmatrix  .  regxmaker  .  util  .  shared  ; 










































public   class   SortUtils  { 











public   static   int  [  ]  sortRetain  (  long  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort1  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  long  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort1  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  int  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort1  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  int  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort1  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  short  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort1  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  short  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort1  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  char  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort1  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  char  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort1  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  byte  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort1  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  byte  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort1  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  double  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort2  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  double  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort2  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 











public   static   int  [  ]  sortRetain  (  float  [  ]  a  )  { 
int  [  ]  pos  =  getPosArray  (  a  .  length  )  ; 
sort2  (  a  ,  pos  ,  0  ,  a  .  length  )  ; 
return   pos  ; 
} 





















public   static   int  [  ]  sortRetain  (  float  [  ]  a  ,  int   fromIndex  ,  int   toIndex  )  { 
rangeCheck  (  a  .  length  ,  fromIndex  ,  toIndex  )  ; 
int  [  ]  pos  =  getPosArray  (  a  .  length  ,  fromIndex  )  ; 
sort2  (  a  ,  pos  ,  fromIndex  ,  toIndex  -  fromIndex  )  ; 
return   pos  ; 
} 

private   static   void   sort2  (  double   a  [  ]  ,  int  [  ]  y  ,  int   fromIndex  ,  int   toIndex  )  { 
final   long   NEG_ZERO_BITS  =  Double  .  doubleToLongBits  (  -  0.0d  )  ; 
int   numNegZeros  =  0  ; 
int   i  =  fromIndex  ,  n  =  toIndex  ; 
while  (  i  <  n  )  { 
if  (  a  [  i  ]  !=  a  [  i  ]  )  { 
a  [  i  ]  =  a  [  --  n  ]  ; 
a  [  n  ]  =  Double  .  NaN  ; 
int   o  =  y  [  i  ]  ; 
y  [  i  ]  =  y  [  n  ]  ; 
y  [  n  ]  =  o  ; 
}  else  { 
if  (  a  [  i  ]  ==  0  &&  Double  .  doubleToLongBits  (  a  [  i  ]  )  ==  NEG_ZERO_BITS  )  { 
a  [  i  ]  =  0.0d  ; 
numNegZeros  ++  ; 
} 
i  ++  ; 
} 
} 
sort1  (  a  ,  y  ,  fromIndex  ,  n  -  fromIndex  )  ; 
if  (  numNegZeros  !=  0  )  { 
int   j  =  binarySearch  (  a  ,  0.0d  ,  fromIndex  ,  n  -  1  )  ; 
do  { 
j  --  ; 
}  while  (  j  >=  0  &&  a  [  j  ]  ==  0.0d  )  ; 
for  (  int   k  =  0  ;  k  <  numNegZeros  ;  k  ++  )  a  [  ++  j  ]  =  -  0.0d  ; 
} 
} 

private   static   void   sort2  (  float   a  [  ]  ,  int  [  ]  y  ,  int   fromIndex  ,  int   toIndex  )  { 
final   int   NEG_ZERO_BITS  =  Float  .  floatToIntBits  (  -  0.0f  )  ; 
int   numNegZeros  =  0  ; 
int   i  =  fromIndex  ,  n  =  toIndex  ; 
while  (  i  <  n  )  { 
if  (  a  [  i  ]  !=  a  [  i  ]  )  { 
a  [  i  ]  =  a  [  --  n  ]  ; 
a  [  n  ]  =  Float  .  NaN  ; 
int   o  =  y  [  i  ]  ; 
y  [  i  ]  =  y  [  n  ]  ; 
y  [  n  ]  =  o  ; 
}  else  { 
if  (  a  [  i  ]  ==  0  &&  Float  .  floatToIntBits  (  a  [  i  ]  )  ==  NEG_ZERO_BITS  )  { 
a  [  i  ]  =  0.0f  ; 
numNegZeros  ++  ; 
} 
i  ++  ; 
} 
} 
sort1  (  a  ,  y  ,  fromIndex  ,  n  -  fromIndex  )  ; 
if  (  numNegZeros  !=  0  )  { 
int   j  =  binarySearch  (  a  ,  0.0f  ,  fromIndex  ,  n  -  1  )  ; 
do  { 
j  --  ; 
}  while  (  j  >=  0  &&  a  [  j  ]  ==  0.0f  )  ; 
for  (  int   k  =  0  ;  k  <  numNegZeros  ;  k  ++  )  a  [  ++  j  ]  =  -  0.0f  ; 
} 
} 




private   static   void   sort1  (  long   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
long   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  long   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
long   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  long   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  long   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  int   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
int   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  int   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
int   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  int   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  int   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  short   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
short   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  short   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
short   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  short   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  short   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  char   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
char   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  char   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
char   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  char   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  char   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  byte   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
byte   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  byte   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
byte   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  byte   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  byte   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  double   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
double   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  double   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
double   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  double   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  double   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 




private   static   void   sort1  (  float   x  [  ]  ,  int  [  ]  y  ,  int   off  ,  int   len  )  { 
if  (  len  <  7  )  { 
for  (  int   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  for  (  int   j  =  i  ;  j  >  off  &&  x  [  j  -  1  ]  >  x  [  j  ]  ;  j  --  )  swap  (  x  ,  y  ,  j  ,  j  -  1  )  ; 
return  ; 
} 
int   m  =  off  +  len  /  2  ; 
if  (  len  >  7  )  { 
int   l  =  off  ; 
int   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
int   s  =  len  /  8  ; 
l  =  med3  (  x  ,  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  x  ,  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  x  ,  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  x  ,  l  ,  m  ,  n  )  ; 
} 
float   v  =  x  [  m  ]  ; 
int   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  x  [  b  ]  <=  v  )  { 
if  (  x  [  b  ]  ==  v  )  swap  (  x  ,  y  ,  a  ++  ,  b  )  ; 
b  ++  ; 
} 
while  (  c  >=  b  &&  x  [  c  ]  >=  v  )  { 
if  (  x  [  c  ]  ==  v  )  swap  (  x  ,  y  ,  c  ,  d  --  )  ; 
c  --  ; 
} 
if  (  b  >  c  )  break  ; 
swap  (  x  ,  y  ,  b  ++  ,  c  --  )  ; 
} 
int   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  x  ,  y  ,  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  x  ,  y  ,  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  sort1  (  x  ,  y  ,  off  ,  s  )  ; 
if  (  (  s  =  d  -  c  )  >  1  )  sort1  (  x  ,  y  ,  n  -  s  ,  s  )  ; 
} 




private   static   void   swap  (  float   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  )  { 
float   t  =  x  [  a  ]  ; 
x  [  a  ]  =  x  [  b  ]  ; 
x  [  b  ]  =  t  ; 
int   o  =  y  [  a  ]  ; 
y  [  a  ]  =  y  [  b  ]  ; 
y  [  b  ]  =  o  ; 
} 




private   static   void   vecswap  (  float   x  [  ]  ,  int  [  ]  y  ,  int   a  ,  int   b  ,  int   n  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  a  ++  ,  b  ++  )  swap  (  x  ,  y  ,  a  ,  b  )  ; 
} 




private   static   int   med3  (  float   x  [  ]  ,  int   a  ,  int   b  ,  int   c  )  { 
return  (  x  [  a  ]  <  x  [  b  ]  ?  (  x  [  b  ]  <  x  [  c  ]  ?  b  :  x  [  a  ]  <  x  [  c  ]  ?  c  :  a  )  :  (  x  [  b  ]  >  x  [  c  ]  ?  b  :  x  [  a  ]  >  x  [  c  ]  ?  c  :  a  )  )  ; 
} 





















public   static   int   binarySearch  (  double  [  ]  a  ,  double   key  )  { 
return   binarySearch  (  a  ,  key  ,  0  ,  a  .  length  -  1  )  ; 
} 

private   static   int   binarySearch  (  double  [  ]  a  ,  double   key  ,  int   low  ,  int   high  )  { 
while  (  low  <=  high  )  { 
int   mid  =  (  low  +  high  )  /  2  ; 
double   midVal  =  a  [  mid  ]  ; 
int   cmp  ; 
if  (  midVal  <  key  )  { 
cmp  =  -  1  ; 
}  else   if  (  midVal  >  key  )  { 
cmp  =  1  ; 
}  else  { 
long   midBits  =  Double  .  doubleToLongBits  (  midVal  )  ; 
long   keyBits  =  Double  .  doubleToLongBits  (  key  )  ; 
cmp  =  (  midBits  ==  keyBits  ?  0  :  (  midBits  <  keyBits  ?  -  1  :  1  )  )  ; 
} 
if  (  cmp  <  0  )  low  =  mid  +  1  ;  else   if  (  cmp  >  0  )  high  =  mid  -  1  ;  else   return   mid  ; 
} 
return  -  (  low  +  1  )  ; 
} 





















public   static   int   binarySearch  (  float  [  ]  a  ,  float   key  )  { 
return   binarySearch  (  a  ,  key  ,  0  ,  a  .  length  -  1  )  ; 
} 

private   static   int   binarySearch  (  float  [  ]  a  ,  float   key  ,  int   low  ,  int   high  )  { 
while  (  low  <=  high  )  { 
int   mid  =  (  low  +  high  )  /  2  ; 
float   midVal  =  a  [  mid  ]  ; 
int   cmp  ; 
if  (  midVal  <  key  )  { 
cmp  =  -  1  ; 
}  else   if  (  midVal  >  key  )  { 
cmp  =  1  ; 
}  else  { 
int   midBits  =  Float  .  floatToIntBits  (  midVal  )  ; 
int   keyBits  =  Float  .  floatToIntBits  (  key  )  ; 
cmp  =  (  midBits  ==  keyBits  ?  0  :  (  midBits  <  keyBits  ?  -  1  :  1  )  )  ; 
} 
if  (  cmp  <  0  )  low  =  mid  +  1  ;  else   if  (  cmp  >  0  )  high  =  mid  -  1  ;  else   return   mid  ; 
} 
return  -  (  low  +  1  )  ; 
} 





private   static   void   rangeCheck  (  int   arrayLen  ,  int   fromIndex  ,  int   toIndex  )  { 
if  (  fromIndex  >  toIndex  )  throw   new   IllegalArgumentException  (  "fromIndex("  +  fromIndex  +  ") > toIndex("  +  toIndex  +  ")"  )  ; 
if  (  fromIndex  <  0  )  throw   new   ArrayIndexOutOfBoundsException  (  fromIndex  )  ; 
if  (  toIndex  >  arrayLen  )  throw   new   ArrayIndexOutOfBoundsException  (  toIndex  )  ; 
} 




private   static   int  [  ]  getPosArray  (  int   size  )  { 
int  [  ]  pos  =  new   int  [  size  ]  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  pos  [  i  ]  =  i  ; 
return   pos  ; 
} 





private   static   int  [  ]  getPosArray  (  int   size  ,  int   start  )  { 
int  [  ]  pos  =  new   int  [  size  ]  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  pos  [  i  ]  =  start  +  i  ; 
return   pos  ; 
} 
} 

