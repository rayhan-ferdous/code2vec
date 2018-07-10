package   cz  .  razor  .  vizrank  ; 

import   java  .  util  .  *  ; 
import   java  .  math  .  *  ; 
import   java  .  io  .  *  ; 





























































































































































































































































public   class   Permutation   implements   Cloneable  ,  Serializable  ,  Iterator  ,  Comparator  ,  Comparable  { 


private   int  [  ]  Al  ; 


private   int   N  ; 

private   BigInteger  [  ]  fak  =  null  ; 












public   Permutation  (  int   n  )  { 
if  (  n  <  1  )  throw   new   IllegalArgumentException  (  "Wrong n(<1): new Permutation(int n)"  )  ; 
N  =  n  ; 
Al  =  new   int  [  N  ]  ; 
first  (  )  ; 
} 




public   Permutation  (  Permutation   p  )  { 
N  =  p  .  N  ; 
Al  =  new   int  [  N  ]  ; 
System  .  arraycopy  (  p  .  Al  ,  0  ,  Al  ,  0  ,  N  )  ; 
} 





public   Permutation  (  int  [  ]  A  )  { 
N  =  A  .  length  ; 
boolean  [  ]  hb  =  new   boolean  [  N  ]  ; 
Al  =  new   int  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  { 
Al  [  i  ]  =  A  [  i  ]  ; 
System  .  out  .  println  (  A  [  i  ]  )  ; 
if  (  hb  [  A  [  i  ]  ]  )  throw   new   IllegalArgumentException  (  "Wrong int[]A: new Permutation(int[] A)"  )  ; 
hb  [  A  [  i  ]  ]  =  true  ; 
} 
} 







public   Object   clone  (  )  { 
Permutation   p  =  new   Permutation  (  this  )  ; 
return   p  ; 
} 










public   int   indexOf  (  int   elem  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  if  (  Al  [  i  ]  ==  elem  )  return   i  ; 
return  -  1  ; 
} 









public   int   get  (  int   index  )  { 
return   Al  [  index  ]  ; 
} 












public   void   swap  (  int   i  ,  int   j  )  { 
if  (  i  !=  j  )  { 
int   h  =  Al  [  i  ]  ; 
Al  [  i  ]  =  Al  [  j  ]  ; 
Al  [  j  ]  =  h  ; 
} 
} 




















public   void   shuffle  (  )  { 
Random   r  =  new   Random  (  )  ; 
for  (  int   i  =  0  ;  i  <  N  -  1  ;  i  ++  )  swap  (  i  ,  i  +  r  .  nextInt  (  N  -  i  )  )  ; 
} 











public   int   size  (  )  { 
return   N  ; 
} 











public   boolean   hasNext  (  )  { 
return  !  isLast  (  )  ; 
} 











public   boolean   isLast  (  )  { 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  if  (  Al  [  i  ]  !=  N  -  1  -  i  )  return   false  ; 
return   true  ; 
} 











public   boolean   hasPrevious  (  )  { 
return  !  isFirst  (  )  ; 
} 






public   boolean   isFirst  (  )  { 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  if  (  Al  [  i  ]  !=  i  )  return   false  ; 
return   true  ; 
} 








public   boolean   isEven  (  )  { 
int  [  ]  h  =  new   int  [  N  ]  ; 
int   i  ; 
boolean   result  =  true  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  h  [  i  ]  =  Al  [  i  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  while  (  h  [  i  ]  !=  i  )  { 
int   j  =  h  [  i  ]  ; 
h  [  i  ]  =  h  [  j  ]  ; 
h  [  j  ]  =  j  ; 
result  =  !  result  ; 
} 
return   result  ; 
} 









public   boolean   isOdd  (  )  { 
return  !  isEven  (  )  ; 
} 










public   int   numTranspos  (  )  { 
int   result  =  0  ; 
int  [  ]  h  =  new   int  [  N  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  h  [  i  ]  =  Al  [  i  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  i  ++  )  while  (  h  [  i  ]  !=  i  )  { 
int   j  =  h  [  i  ]  ; 
h  [  i  ]  =  h  [  j  ]  ; 
h  [  j  ]  =  j  ; 
result  ++  ; 
} 
return   result  ; 
} 








public   boolean   isDerangement  (  )  { 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  if  (  Al  [  i  ]  ==  i  )  return   false  ; 
return   true  ; 
} 

public   int  [  ]  toArray  (  )  { 
int  [  ]  al  =  new   int  [  N  ]  ; 
System  .  arraycopy  (  al  ,  0  ,  Al  ,  0  ,  N  )  ; 
return   al  ; 
} 
















public   String   toString  (  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "["  )  ; 
for  (  int   i  =  0  ;  i  <  N  -  1  ;  i  ++  )  buf  .  append  (  Al  [  i  ]  +  ","  )  ; 
buf  .  append  (  Al  [  N  -  1  ]  +  "]"  )  ; 
return   buf  .  toString  (  )  ; 
} 































public   int   compare  (  Object   o1  ,  Object   o2  )  { 
if  (  (  !  (  o1   instanceof   Permutation  )  )  ||  (  !  (  o2   instanceof   Permutation  )  )  )  throw   new   ClassCastException  (  )  ; 
Permutation   p1  =  (  Permutation  )  o1  ,  p2  =  (  Permutation  )  o2  ; 
int   maxIndex  ; 
maxIndex  =  (  p1  .  N  <  p2  .  N  )  ?  p1  .  N  :  p2  .  N  ; 
for  (  int   i  =  0  ;  i  <  maxIndex  ;  i  ++  )  if  (  p1  .  Al  [  i  ]  !=  p2  .  Al  [  i  ]  )  if  (  p1  .  Al  [  i  ]  <  p2  .  Al  [  i  ]  )  return  -  1  ;  else   return   1  ; 
if  (  p1  .  N  <  p2  .  N  )  return  -  1  ; 
if  (  p1  .  N  >  p2  .  N  )  return   1  ; 
return   0  ; 
} 































public   int   compareTo  (  Object   o  )  { 
if  (  !  (  o   instanceof   Permutation  )  )  throw   new   ClassCastException  (  )  ; 
Permutation   p  =  (  Permutation  )  o  ; 
int   maxIndex  ; 
maxIndex  =  (  this  .  N  <  p  .  N  )  ?  this  .  N  :  p  .  N  ; 
for  (  int   i  =  0  ;  i  <  maxIndex  ;  i  ++  )  if  (  this  .  Al  [  i  ]  <  p  .  Al  [  i  ]  )  return  -  1  ; 
if  (  this  .  N  <  p  .  N  )  return  -  1  ; 
if  (  this  .  N  >  p  .  N  )  return   1  ; 
return   0  ; 
} 

















public   boolean   equals  (  Object   o  )  { 
if  (  o  ==  this  )  return   true  ; 
if  (  !  (  o   instanceof   Permutation  )  )  return   false  ; 
int  [  ]  al  =  (  (  Permutation  )  o  )  .  Al  ; 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  if  (  Al  [  i  ]  !=  al  [  i  ]  )  return   false  ; 
return   true  ; 
} 






public   Permutation   inverse  (  )  { 
int  [  ]  h  =  new   int  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  h  [  Al  [  i  ]  ]  =  i  ; 
return   new   Permutation  (  h  )  ; 
} 















public   void   first  (  )  { 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  Al  [  i  ]  =  i  ; 
} 

















public   void   last  (  )  { 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  Al  [  i  ]  =  N  -  i  -  1  ; 
} 












































public   Object   next  (  )  { 
int   Nm1  =  N  -  1  ; 
int   p  =  Nm1  ,  low  ,  high  ,  s  ,  m  ; 
while  (  (  p  >  0  )  &&  (  Al  [  p  ]  <  Al  [  p  -  1  ]  )  )  p  --  ; 
if  (  p  >  0  )  { 
s  =  Al  [  p  -  1  ]  ; 
if  (  Al  [  Nm1  ]  >  s  )  low  =  Nm1  ;  else  { 
high  =  Nm1  ; 
low  =  p  ; 
while  (  high  >  low  +  1  )  { 
m  =  (  high  +  low  )  >  >  1  ; 
if  (  Al  [  m  ]  <  s  )  high  =  m  ;  else   low  =  m  ; 
} 
} 
Al  [  p  -  1  ]  =  Al  [  low  ]  ; 
Al  [  low  ]  =  s  ; 
} 
high  =  Nm1  ; 
while  (  high  >  p  )  { 
m  =  Al  [  high  ]  ; 
Al  [  high  ]  =  Al  [  p  ]  ; 
Al  [  p  ]  =  m  ; 
p  ++  ; 
high  --  ; 
} 
return   this  ; 
} 













































public   Object   previous  (  )  { 
int   Nm1  =  N  -  1  ; 
int   p  =  Nm1  ,  low  ,  high  ,  s  ,  m  ; 
while  (  (  p  >  0  )  &&  (  Al  [  p  ]  >  Al  [  p  -  1  ]  )  )  p  --  ; 
if  (  p  >  0  )  { 
s  =  Al  [  p  -  1  ]  ; 
if  (  Al  [  Nm1  ]  <  s  )  low  =  Nm1  ;  else  { 
high  =  Nm1  ; 
low  =  p  ; 
while  (  high  >  low  +  1  )  { 
m  =  (  high  +  low  )  >  >  1  ; 
if  (  Al  [  m  ]  >  s  )  high  =  m  ;  else   low  =  m  ; 
} 
} 
Al  [  p  -  1  ]  =  Al  [  low  ]  ; 
Al  [  low  ]  =  s  ; 
} 
high  =  Nm1  ; 
while  (  high  >  p  )  { 
m  =  Al  [  high  ]  ; 
Al  [  high  ]  =  Al  [  p  ]  ; 
Al  [  p  ]  =  m  ; 
p  ++  ; 
high  --  ; 
} 
return   this  ; 
} 





































public   Object   next  (  int   index  )  { 
if  (  index  >=  N  -  2  )  next  (  )  ;  else  { 
int   m  =  Al  [  index  ]  ; 
sort  (  index  +  1  ,  N  -  1  )  ; 
if  (  m  >  Al  [  N  -  1  ]  )  { 
if  (  index  >  0  )  return   next  (  index  -  1  )  ; 
first  (  )  ; 
}  else  { 
int   o  ; 
for  (  o  =  N  -  2  ;  Al  [  o  ]  >  m  ;  o  --  )  ; 
Al  [  index  ]  =  Al  [  o  +  1  ]  ; 
Al  [  o  +  1  ]  =  m  ; 
} 
} 
return   this  ; 
} 


















































public   int   nextGE  (  int   index  ,  int   value  )  { 
if  (  Al  [  index  ]  ==  value  )  return   1  ; 
if  (  Al  [  index  ]  >  value  )  return   0  ; 
int   pos  =  N  ,  compare  =  N  ; 
for  (  int   i  =  index  +  1  ;  i  <  N  ;  i  ++  )  if  (  (  Al  [  i  ]  >=  value  )  &&  (  Al  [  i  ]  <  compare  )  )  { 
compare  =  Al  [  i  ]  ; 
pos  =  i  ; 
} 
if  (  pos  <  N  )  { 
Al  [  pos  ]  =  Al  [  index  ]  ; 
Al  [  index  ]  =  compare  ; 
sort  (  index  +  1  ,  N  -  1  )  ; 
if  (  compare  ==  value  )  return   1  ;  else   return   0  ; 
} 
return  -  1  ; 
} 

public   boolean   nextEQ  (  int   index  ,  int   value  ,  int   save  )  { 
int   pos  ; 
do  { 
if  (  Al  [  index  ]  ==  value  )  return   true  ; 
for  (  pos  =  save  +  1  ;  (  (  pos  <  N  )  &&  (  Al  [  pos  ]  !=  value  )  )  ;  pos  ++  )  ; 
if  (  pos  >=  N  )  return   false  ; 
if  (  pos  <  index  )  next  (  pos  )  ; 
}  while  (  pos  <  index  )  ; 
if  (  value  >  Al  [  index  ]  )  { 
Al  [  pos  ]  =  Al  [  index  ]  ; 
Al  [  index  ]  =  value  ; 
sort  (  index  +  1  ,  N  -  1  )  ; 
return   true  ; 
}  else  { 
if  (  save  ==  index  -  1  )  return   false  ; 
next  (  index  -  1  )  ; 
return   nextEQ  (  index  ,  value  ,  save  )  ; 
} 
} 





public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  ""  )  ; 
} 

public   BigInteger   getPermutationNumber  (  )  { 
if  (  fak  ==  null  )  { 
fak  =  new   BigInteger  [  N  ]  ; 
fak  [  N  -  1  ]  =  BigInteger  .  ONE  ; 
for  (  int   i  =  N  -  2  ;  i  >=  0  ;  i  --  )  fak  [  i  ]  =  fak  [  i  +  1  ]  .  multiply  (  new   BigInteger  (  N  -  i  -  1  +  ""  )  )  ; 
} 
int  [  ]  h  =  new   int  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  h  [  i  ]  =  i  ; 
BigInteger   PermNumber  =  BigInteger  .  ZERO  ; 
for  (  int   i  =  0  ;  i  <  N  -  1  ;  i  ++  )  { 
int   j  =  0  ; 
int   m  =  Al  [  i  ]  ; 
while  (  h  [  i  +  j  ]  !=  m  )  j  ++  ; 
PermNumber  =  PermNumber  .  add  (  fak  [  i  ]  .  multiply  (  new   BigInteger  (  j  +  ""  )  )  )  ; 
for  (  ;  j  >  0  ;  j  --  )  h  [  i  +  j  ]  =  h  [  i  +  j  -  1  ]  ; 
} 
return   PermNumber  ; 
} 



























public   void   setPermutationNumber  (  String   s  )  { 
setPermutationNumber  (  new   BigInteger  (  s  )  )  ; 
} 








public   void   setPermutationNumber  (  long   l  )  { 
setPermutationNumber  (  new   BigInteger  (  l  +  ""  )  )  ; 
} 



























public   void   setPermutationNumber  (  BigInteger   Number  )  { 
if  (  fak  ==  null  )  { 
fak  =  new   BigInteger  [  N  ]  ; 
fak  [  N  -  1  ]  =  BigInteger  .  ONE  ; 
for  (  int   i  =  N  -  2  ;  i  >=  0  ;  i  --  )  fak  [  i  ]  =  fak  [  i  +  1  ]  .  multiply  (  new   BigInteger  (  N  -  i  -  1  +  ""  )  )  ; 
} 
Number  =  Number  .  mod  (  fakultaet  (  N  )  )  ; 
for  (  int   i  =  0  ;  i  <  N  ;  i  ++  )  Al  [  i  ]  =  i  ; 
if  (  Number  .  equals  (  BigInteger  .  ZERO  )  )  return  ; 
for  (  int   i  =  0  ;  i  <  N  -  1  ;  i  ++  )  { 
BigInteger  [  ]  bi  =  Number  .  divideAndRemainder  (  fak  [  i  ]  )  ; 
int   j  =  bi  [  0  ]  .  intValue  (  )  ; 
if  (  j  !=  0  )  { 
int   m  =  Al  [  i  +  j  ]  ; 
for  (  ;  j  >  0  ;  j  --  )  Al  [  i  +  j  ]  =  Al  [  i  +  j  -  1  ]  ; 
Al  [  i  ]  =  m  ; 
Number  =  bi  [  1  ]  ; 
} 
} 
} 

private   void   sort  (  int   first  ,  int   last  )  { 
int   v  ,  h  ; 
if  (  last  -  first  <  7  )  { 
for  (  int   i  =  first  ;  i  <=  last  ;  i  ++  )  { 
int   j  ; 
h  =  Al  [  i  ]  ; 
for  (  j  =  i  ;  j  >  first  &&  (  v  =  Al  [  j  -  1  ]  )  >  h  ;  j  --  )  Al  [  j  ]  =  v  ; 
Al  [  j  ]  =  h  ; 
} 
return  ; 
} 
int   a  =  first  ,  c  =  last  ; 
int   m  =  (  first  +  last  )  >  >  1  ; 
if  (  Al  [  a  ]  >  Al  [  m  ]  )  { 
h  =  Al  [  a  ]  ; 
Al  [  a  ]  =  Al  [  m  ]  ; 
Al  [  m  ]  =  h  ; 
} 
if  (  Al  [  m  ]  >  Al  [  c  ]  )  { 
h  =  Al  [  c  ]  ; 
Al  [  c  ]  =  Al  [  m  ]  ; 
Al  [  m  ]  =  h  ; 
if  (  Al  [  a  ]  >  h  )  { 
Al  [  m  ]  =  Al  [  a  ]  ; 
Al  [  a  ]  =  h  ; 
} 
} 
v  =  Al  [  m  ]  ; 
a  ++  ; 
c  --  ; 
while  (  true  )  { 
while  (  Al  [  a  ]  <  v  )  a  ++  ; 
while  (  Al  [  c  ]  >  v  )  c  --  ; 
if  (  a  >  c  )  break  ; 
h  =  Al  [  a  ]  ; 
Al  [  a  ]  =  Al  [  c  ]  ; 
Al  [  c  ]  =  h  ; 
a  ++  ; 
c  --  ; 
} 
if  (  c  >  first  )  sort  (  first  ,  c  )  ; 
if  (  a  <  last  )  sort  (  a  ,  last  )  ; 
} 




private   static   BigInteger   fakultaet  (  int   n  )  { 
if  (  n  <  0  )  throw   new   IllegalArgumentException  (  "Wrong n(<1): fakultaet(int n)"  )  ; 
BigInteger   p  =  BigInteger  .  ONE  ; 
for  (  int   i  =  2  ;  i  <=  n  ;  i  ++  )  p  =  p  .  multiply  (  new   BigInteger  (  i  +  ""  )  )  ; 
return   p  ; 
} 
} 

