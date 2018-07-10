package   edu  .  hawaii  .  jmotif  .  logic  .  math  ; 

import   java  .  util  .  Formatter  ; 
import   java  .  util  .  Locale  ; 







public   final   class   MatrixFactory  { 

private   static   final   String   CR  =  "\n"  ; 




private   MatrixFactory  (  )  { 
super  (  )  ; 
} 







public   static   double  [  ]  [  ]  zeros  (  int   n  )  { 
return   new   double  [  1  ]  [  n  ]  ; 
} 








public   static   double  [  ]  [  ]  zeros  (  int   n  ,  int   m  )  { 
return   new   double  [  n  ]  [  m  ]  ; 
} 








public   static   boolean   equals  (  double  [  ]  [  ]  a  ,  double  [  ]  [  ]  b  )  { 
int   rowsA  =  a  .  length  ; 
int   colsA  =  a  [  0  ]  .  length  ; 
int   rowsB  =  b  .  length  ; 
int   colsB  =  b  [  0  ]  .  length  ; 
if  (  (  rowsA  ==  rowsB  )  &&  (  colsA  ==  colsB  )  )  { 
for  (  int   i  =  0  ;  i  <  rowsA  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  colsA  ;  j  ++  )  { 
if  (  a  [  i  ]  [  j  ]  !=  b  [  i  ]  [  j  ]  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 







public   static   double  [  ]  [  ]  clone  (  double  [  ]  [  ]  a  )  { 
double  [  ]  [  ]  res  =  new   double  [  a  .  length  ]  [  a  [  0  ]  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  a  [  0  ]  .  length  ;  j  ++  )  { 
res  [  i  ]  [  j  ]  =  a  [  i  ]  [  j  ]  ; 
} 
} 
return   res  ; 
} 







public   static   double  [  ]  [  ]  transpose  (  double  [  ]  [  ]  a  )  { 
int   rows  =  a  .  length  ; 
int   cols  =  a  [  0  ]  .  length  ; 
double  [  ]  [  ]  res  =  new   double  [  cols  ]  [  rows  ]  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
res  [  j  ]  [  i  ]  =  a  [  i  ]  [  j  ]  ; 
} 
} 
return   res  ; 
} 











public   static   double  [  ]  [  ]  reshape  (  double  [  ]  [  ]  a  ,  int   n  ,  int   m  )  { 
int   cEl  =  0  ; 
int   aRows  =  a  .  length  ; 
double  [  ]  [  ]  res  =  new   double  [  n  ]  [  m  ]  ; 
for  (  int   j  =  0  ;  j  <  m  ;  j  ++  )  { 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
res  [  i  ]  [  j  ]  =  a  [  cEl  %  aRows  ]  [  cEl  /  aRows  ]  ; 
cEl  ++  ; 
} 
} 
return   res  ; 
} 







public   static   double  [  ]  colMeans  (  double  [  ]  [  ]  a  )  { 
double  [  ]  res  =  new   double  [  a  [  0  ]  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  a  [  0  ]  .  length  ;  j  ++  )  { 
double   sum  =  0  ; 
int   counter  =  0  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
if  (  Double  .  isNaN  (  a  [  i  ]  [  j  ]  )  ||  Double  .  isInfinite  (  a  [  i  ]  [  j  ]  )  )  { 
continue  ; 
} 
sum  +=  a  [  i  ]  [  j  ]  ; 
counter  +=  1  ; 
} 
if  (  counter  ==  0  )  { 
res  [  j  ]  =  Double  .  NaN  ; 
}  else  { 
res  [  j  ]  =  sum  /  (  (  Integer  )  counter  )  .  doubleValue  (  )  ; 
} 
} 
return   res  ; 
} 







public   static   String   toString  (  double  [  ]  [  ]  a  )  { 
int   rows  =  a  .  length  ; 
int   cols  =  a  [  0  ]  .  length  ; 
StringBuffer   sb  =  new   StringBuffer  (  4000  )  ; 
Formatter   formatter  =  new   Formatter  (  sb  ,  Locale  .  US  )  ; 
sb  .  append  (  "       "  )  ; 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
formatter  .  format  (  "     [%1$3d]"  ,  j  )  ; 
} 
sb  .  append  (  CR  )  ; 
for  (  int   i  =  0  ;  i  <  rows  ;  i  ++  )  { 
formatter  .  format  (  " [%1$3d] "  ,  i  )  ; 
for  (  int   j  =  0  ;  j  <  cols  ;  j  ++  )  { 
formatter  .  format  (  " %1$ 6f"  ,  a  [  i  ]  [  j  ]  )  ; 
} 
sb  .  append  (  CR  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 
} 

