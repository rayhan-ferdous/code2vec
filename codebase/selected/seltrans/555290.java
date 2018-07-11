package   net  .  sourceforge  .  ondex  .  core  .  util  ; 

import   it  .  unimi  .  dsi  .  fastutil  .  ints  .  IntIterator  ; 
import   java  .  util  .  Iterator  ; 







public   class   AnanianSparseBitSet   implements   Iterable  <  Integer  >  ,  Cloneable  ,  ONDEXBitSet  { 


int   offs  [  ]  ; 


long   bits  [  ]  ; 


int   size  ; 


private   static   final   int   LG_BITS  =  6  ; 


private   static   final   int   BITS  =  1  <<  LG_BITS  ; 


private   static   final   int   BITS_M1  =  BITS  -  1  ; 




public   AnanianSparseBitSet  (  )  { 
this  (  4  )  ; 
} 







public   AnanianSparseBitSet  (  int   nbits  )  { 
bits  =  new   long  [  nbits  ]  ; 
offs  =  new   int  [  nbits  ]  ; 
size  =  0  ; 
} 




public   AnanianSparseBitSet  (  AnanianSparseBitSet   set  )  { 
bits  =  set  .  bits  .  clone  (  )  ; 
offs  =  set  .  offs  .  clone  (  )  ; 
size  =  set  .  size  ; 
} 

private   void   new_block  (  int   idx  ,  int   bnum  )  { 
if  (  size  ==  bits  .  length  )  { 
long  [  ]  nbits  =  new   long  [  size  *  3  ]  ; 
int  [  ]  noffs  =  new   int  [  size  *  3  ]  ; 
System  .  arraycopy  (  bits  ,  0  ,  nbits  ,  0  ,  size  )  ; 
System  .  arraycopy  (  offs  ,  0  ,  noffs  ,  0  ,  size  )  ; 
bits  =  nbits  ; 
offs  =  noffs  ; 
} 
insert_block  (  idx  ,  bnum  )  ; 
} 

private   void   insert_block  (  int   idx  ,  int   bnum  )  { 
System  .  arraycopy  (  bits  ,  idx  ,  bits  ,  idx  +  1  ,  size  -  idx  )  ; 
System  .  arraycopy  (  offs  ,  idx  ,  offs  ,  idx  +  1  ,  size  -  idx  )  ; 
offs  [  idx  ]  =  bnum  ; 
bits  [  idx  ]  =  0  ; 
size  ++  ; 
} 

private   int   bsearch  (  int   bnum  )  { 
int   l  =  0  ,  r  =  size  ; 
while  (  l  <  r  )  { 
int   p  =  (  l  +  r  )  /  2  ; 
if  (  bnum  <  offs  [  p  ]  )  r  =  p  ;  else   if  (  bnum  >  offs  [  p  ]  )  l  =  p  +  1  ;  else   return   p  ; 
} 
return   l  ; 
} 







public   void   set  (  int   bit  )  { 
int   bnum  =  bit  >  >  LG_BITS  ; 
int   idx  =  bsearch  (  bnum  )  ; 
if  (  idx  >=  size  ||  offs  [  idx  ]  !=  bnum  )  new_block  (  idx  ,  bnum  )  ; 
bits  [  idx  ]  |=  (  1L  <<  (  bit  &  BITS_M1  )  )  ; 
} 







public   void   clear  (  int   bit  )  { 
int   bnum  =  bit  >  >  LG_BITS  ; 
int   idx  =  bsearch  (  bnum  )  ; 
if  (  idx  >=  size  ||  offs  [  idx  ]  !=  bnum  )  new_block  (  idx  ,  bnum  )  ; 
bits  [  idx  ]  &=  ~  (  1L  <<  (  bit  &  BITS_M1  )  )  ; 
} 




public   void   clearAll  (  )  { 
size  =  0  ; 
} 







public   boolean   get  (  int   bit  )  { 
int   bnum  =  bit  >  >  LG_BITS  ; 
int   idx  =  bsearch  (  bnum  )  ; 
if  (  idx  >=  size  ||  offs  [  idx  ]  !=  bnum  )  return   false  ; 
return   0  !=  (  bits  [  idx  ]  &  (  1L  <<  (  bit  &  BITS_M1  )  )  )  ; 
} 







public   void   and  (  AnanianSparseBitSet   set  )  { 
binop  (  this  ,  set  ,  AND  )  ; 
} 







public   void   or  (  AnanianSparseBitSet   set  )  { 
binop  (  this  ,  set  ,  OR  )  ; 
} 







public   void   xor  (  AnanianSparseBitSet   set  )  { 
binop  (  this  ,  set  ,  XOR  )  ; 
} 

private   static   interface   BinOp  { 

public   long   op  (  long   a  ,  long   b  )  ; 
} 

private   static   final   BinOp   AND  =  new   BinOp  (  )  { 

public   final   long   op  (  long   a  ,  long   b  )  { 
return   a  &  b  ; 
} 
}  ; 

private   static   final   BinOp   OR  =  new   BinOp  (  )  { 

public   final   long   op  (  long   a  ,  long   b  )  { 
return   a  |  b  ; 
} 
}  ; 

private   static   final   BinOp   XOR  =  new   BinOp  (  )  { 

public   final   long   op  (  long   a  ,  long   b  )  { 
return   a  ^  b  ; 
} 
}  ; 

private   static   final   void   binop  (  AnanianSparseBitSet   a  ,  AnanianSparseBitSet   b  ,  BinOp   op  )  { 
int   nsize  =  a  .  size  +  b  .  size  ; 
long  [  ]  nbits  ; 
int  [  ]  noffs  ; 
int   a_zero  ,  a_size  ; 
if  (  a  .  bits  .  length  <  nsize  )  { 
nbits  =  new   long  [  nsize  ]  ; 
noffs  =  new   int  [  nsize  ]  ; 
a_zero  =  0  ; 
a_size  =  a  .  size  ; 
}  else  { 
nbits  =  a  .  bits  ; 
noffs  =  a  .  offs  ; 
a_zero  =  a  .  bits  .  length  -  a  .  size  ; 
a_size  =  a  .  bits  .  length  ; 
System  .  arraycopy  (  a  .  bits  ,  0  ,  a  .  bits  ,  a_zero  ,  a  .  size  )  ; 
System  .  arraycopy  (  a  .  offs  ,  0  ,  a  .  offs  ,  a_zero  ,  a  .  size  )  ; 
} 
nsize  =  0  ; 
for  (  int   i  =  a_zero  ,  j  =  0  ;  i  <  a_size  ||  j  <  b  .  size  ;  )  { 
long   nb  ; 
int   no  ; 
if  (  i  <  a_size  &&  (  j  >=  b  .  size  ||  a  .  offs  [  i  ]  <  b  .  offs  [  j  ]  )  )  { 
nb  =  op  .  op  (  a  .  bits  [  i  ]  ,  0  )  ; 
no  =  a  .  offs  [  i  ]  ; 
i  ++  ; 
}  else   if  (  j  <  b  .  size  &&  (  i  >=  a_size  ||  a  .  offs  [  i  ]  >  b  .  offs  [  j  ]  )  )  { 
nb  =  op  .  op  (  0  ,  b  .  bits  [  j  ]  )  ; 
no  =  b  .  offs  [  j  ]  ; 
j  ++  ; 
}  else  { 
nb  =  op  .  op  (  a  .  bits  [  i  ]  ,  b  .  bits  [  j  ]  )  ; 
no  =  a  .  offs  [  i  ]  ; 
i  ++  ; 
j  ++  ; 
} 
if  (  nb  !=  0  )  { 
nbits  [  nsize  ]  =  nb  ; 
noffs  [  nsize  ]  =  no  ; 
nsize  ++  ; 
} 
} 
a  .  bits  =  nbits  ; 
a  .  offs  =  noffs  ; 
a  .  size  =  nsize  ; 
} 




public   int   hashCode  (  )  { 
long   h  =  1234  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  h  ^=  bits  [  i  ]  *  offs  [  i  ]  ; 
return  (  int  )  (  (  h  >  >  32  )  ^  h  )  ; 
} 








public   boolean   equals  (  Object   obj  )  { 
if  (  (  obj  !=  null  )  &&  (  obj   instanceof   AnanianSparseBitSet  )  )  return   equals  (  this  ,  (  AnanianSparseBitSet  )  obj  )  ; 
return   false  ; 
} 






public   static   boolean   equals  (  AnanianSparseBitSet   a  ,  AnanianSparseBitSet   b  )  { 
for  (  int   i  =  0  ,  j  =  0  ;  i  <  a  .  size  ||  j  <  b  .  size  ;  )  { 
if  (  i  <  a  .  size  &&  (  j  >=  b  .  size  ||  a  .  offs  [  i  ]  <  b  .  offs  [  j  ]  )  )  { 
if  (  a  .  bits  [  i  ++  ]  !=  0  )  return   false  ; 
}  else   if  (  j  <  b  .  size  &&  (  i  >=  a  .  size  ||  a  .  offs  [  i  ]  >  b  .  offs  [  j  ]  )  )  { 
if  (  b  .  bits  [  j  ++  ]  !=  0  )  return   false  ; 
}  else  { 
if  (  a  .  bits  [  i  ++  ]  !=  b  .  bits  [  j  ++  ]  )  return   false  ; 
} 
} 
return   true  ; 
} 




public   Object   clone  (  )  { 
try  { 
AnanianSparseBitSet   set  =  (  AnanianSparseBitSet  )  super  .  clone  (  )  ; 
set  .  bits  =  (  long  [  ]  )  bits  .  clone  (  )  ; 
set  .  offs  =  (  int  [  ]  )  offs  .  clone  (  )  ; 
return   set  ; 
}  catch  (  CloneNotSupportedException   e  )  { 
throw   new   InternalError  (  )  ; 
} 
} 





public   IntIterator   iterator  (  )  { 
return   new   IntIterator  (  )  { 

int   idx  =  -  1  ,  bit  =  BITS  ; 

{ 
advance  (  )  ; 
} 

public   boolean   hasNext  (  )  { 
return  (  idx  <  size  )  ; 
} 

public   int   nextInt  (  )  { 
int   r  =  bit  +  (  offs  [  idx  ]  <<  LG_BITS  )  ; 
advance  (  )  ; 
return   new   Integer  (  r  )  ; 
} 

private   void   advance  (  )  { 
while  (  idx  <  size  )  { 
while  (  ++  bit  <  BITS  )  if  (  0  !=  (  bits  [  idx  ]  &  (  1L  <<  bit  )  )  )  return  ; 
idx  ++  ; 
bit  =  -  1  ; 
} 
} 

public   void   remove  (  )  { 
clear  (  bit  +  (  offs  [  idx  ]  <<  LG_BITS  )  )  ; 
} 

@  Override 
public   int   skip  (  int   arg0  )  { 
int   i  =  0  ; 
while  (  hasNext  (  )  )  { 
nextInt  (  )  ; 
i  ++  ; 
if  (  i  >  arg0  )  break  ; 
} 
return   i  ; 
} 

@  Override 
public   Integer   next  (  )  { 
return   nextInt  (  )  ; 
} 
}  ; 
} 




public   String   toString  (  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  '{'  )  ; 
for  (  Iterator  <  Integer  >  i  =  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
if  (  sb  .  length  (  )  >  1  )  sb  .  append  (  ", "  )  ; 
sb  .  append  (  i  .  next  (  )  )  ; 
} 
sb  .  append  (  '}'  )  ; 
return   sb  .  toString  (  )  ; 
} 

public   int   size  (  )  { 
return  (  size  ==  0  )  ?  0  :  (  (  1  +  offs  [  size  -  1  ]  )  <<  LG_BITS  )  ; 
} 

@  Override 
public   int   length  (  )  { 
return   0  ; 
} 

@  Override 
public   void   and  (  ONDEXBitSet   set  )  { 
if  (  set   instanceof   AnanianSparseBitSet  )  { 
and  (  (  AnanianSparseBitSet  )  set  )  ; 
}  else  { 
AnanianSparseBitSet   bs  =  new   AnanianSparseBitSet  (  )  ; 
IntIterator   ints  =  set  .  iterator  (  )  ; 
while  (  ints  .  hasNext  (  )  )  { 
int   i  =  ints  .  next  (  )  ; 
if  (  get  (  i  )  )  { 
bs  .  set  (  i  )  ; 
} 
} 
bits  =  bs  .  bits  ; 
offs  =  bs  .  offs  ; 
size  =  bs  .  size  ; 
} 
} 

@  Override 
public   void   andNot  (  ONDEXBitSet   set  )  { 
IntIterator   ints  =  set  .  iterator  (  )  ; 
while  (  ints  .  hasNext  (  )  )  { 
clear  (  ints  .  next  (  )  )  ; 
} 
} 

@  Override 
public   void   or  (  ONDEXBitSet   set  )  { 
if  (  set   instanceof   AnanianSparseBitSet  )  { 
or  (  (  AnanianSparseBitSet  )  set  )  ; 
}  else  { 
IntIterator   ints  =  set  .  iterator  (  )  ; 
while  (  ints  .  hasNext  (  )  )  { 
set  (  ints  .  next  (  )  )  ; 
} 
} 
} 

@  Override 
public   void   xor  (  ONDEXBitSet   set  )  { 
if  (  set   instanceof   AnanianSparseBitSet  )  { 
xor  (  (  AnanianSparseBitSet  )  set  )  ; 
}  else  { 
AnanianSparseBitSet   exclusiveSet  =  new   AnanianSparseBitSet  (  size  )  ; 
IntIterator   ints  =  set  .  iterator  (  )  ; 
while  (  ints  .  hasNext  (  )  )  { 
int   i  =  ints  .  next  (  )  ; 
if  (  !  get  (  i  )  )  { 
exclusiveSet  .  set  (  i  )  ; 
}  else  { 
clear  (  i  )  ; 
} 
} 
or  (  exclusiveSet  )  ; 
} 
} 

@  Override 
public   ONDEXBitSet   copy  (  )  { 
return   new   AnanianSparseBitSet  (  this  )  ; 
} 

@  Override 
public   int   countClearBits  (  )  { 
int   largestBit  =  0  ; 
int   count  =  0  ; 
IntIterator   it  =  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
int   bit  =  it  .  next  (  )  ; 
if  (  bit  >  largestBit  )  largestBit  =  bit  ; 
count  ++  ; 
} 
return  (  largestBit  -  count  )  +  1  ; 
} 

@  Override 
public   int   countSetBits  (  )  { 
int   count  =  0  ; 
IntIterator   it  =  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
it  .  next  (  )  ; 
count  ++  ; 
} 
return   count  ; 
} 
} 

