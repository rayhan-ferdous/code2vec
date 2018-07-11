package   pipe  .  dataLayer  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  DecimalFormatSymbols  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  Locale  ; 

public   class   PNMatrix  { 






private   int  [  ]  [  ]  A  ; 







private   int   m  ,  n  ; 


public   boolean   matrixChanged  ; 









public   PNMatrix  (  int   m  ,  int   n  )  { 
this  (  )  ; 
this  .  m  =  m  ; 
this  .  n  =  n  ; 
A  =  new   int  [  m  ]  [  n  ]  ; 
} 









public   PNMatrix  (  PNMatrix   B  )  { 
this  (  )  ; 
this  .  m  =  B  .  m  ; 
this  .  n  =  B  .  n  ; 
A  =  B  .  A  .  clone  (  )  ; 
} 










public   PNMatrix  (  int  [  ]  [  ]  A  )  { 
this  (  )  ; 
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












public   PNMatrix  (  int   vals  [  ]  ,  int   m  )  { 
this  (  )  ; 
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

private   PNMatrix  (  )  { 
matrixChanged  =  true  ; 
} 










public   PNMatrix   appendVector  (  PNMatrix   X  )  { 
PNMatrix   R  =  new   PNMatrix  (  m  ,  n  +  1  )  ; 
R  .  setMatrix  (  0  ,  m  -  1  ,  0  ,  n  -  1  ,  this  )  ; 
try  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
R  .  set  (  i  ,  n  ,  X  .  get  (  i  ,  0  )  )  ; 
} 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Row indices incompatible"  )  ; 
} 
return   R  ; 
} 








public   int   cardinalityCondition  (  )  { 
int   cardRow  =  -  1  ; 
int   pPlusCard  =  0  ,  pMinusCard  =  0  ,  countpPlus  =  0  ,  countpMinus  =  0  ; 
int  [  ]  pPlus  ,  pMinus  ; 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
int   pLength  =  n  ,  mLength  =  n  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
countpPlus  =  0  ; 
countpMinus  =  0  ; 
pPlus  =  getPositiveIndices  (  i  )  ; 
pMinus  =  getNegativeIndices  (  i  )  ; 
for  (  int   j  =  0  ;  j  <  pLength  ;  j  ++  )  { 
if  (  pPlus  [  j  ]  !=  0  )  { 
countpPlus  ++  ; 
} 
} 
for  (  int   j  =  0  ;  j  <  mLength  ;  j  ++  )  { 
if  (  pMinus  [  j  ]  !=  0  )  { 
countpMinus  ++  ; 
} 
} 
if  (  countpPlus  ==  1  ||  countpMinus  ==  1  )  { 
return   i  ; 
} 
} 
return   cardRow  ; 
} 








public   int   cardinalityOne  (  )  { 
int   k  =  -  1  ; 
int   pPlusCard  =  0  ,  pMinusCard  =  0  ,  countpPlus  =  0  ,  countpMinus  =  0  ; 
int  [  ]  pPlus  ,  pMinus  ; 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
int   pLength  =  n  ,  mLength  =  n  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
countpPlus  =  0  ; 
countpMinus  =  0  ; 
pPlus  =  getPositiveIndices  (  i  )  ; 
pMinus  =  getNegativeIndices  (  i  )  ; 
for  (  int   j  =  0  ;  j  <  pLength  ;  j  ++  )  { 
if  (  pPlus  [  j  ]  !=  0  )  { 
countpPlus  ++  ; 
} 
} 
for  (  int   j  =  0  ;  j  <  mLength  ;  j  ++  )  { 
if  (  pMinus  [  j  ]  !=  0  )  { 
countpMinus  ++  ; 
} 
} 
if  (  countpPlus  ==  1  )  { 
return   pPlus  [  0  ]  -  1  ; 
} 
if  (  countpMinus  ==  1  )  { 
return   pMinus  [  0  ]  -  1  ; 
} 
} 
return   k  ; 
} 







public   boolean   checkCase11  (  )  { 
boolean   satisfies11  =  false  ; 
boolean   pPlusEmpty  =  true  ,  pMinusEmpty  =  true  ; 
int  [  ]  pPlus  ,  pMinus  ; 
int   m  =  getRowDimension  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
pPlusEmpty  =  true  ; 
pMinusEmpty  =  true  ; 
pPlus  =  getPositiveIndices  (  i  )  ; 
pMinus  =  getNegativeIndices  (  i  )  ; 
int   pLength  =  pPlus  .  length  ,  mLength  =  pMinus  .  length  ; 
for  (  int   j  =  0  ;  j  <  pLength  ;  j  ++  )  { 
if  (  pPlus  [  j  ]  !=  0  )  { 
pPlusEmpty  =  false  ; 
} 
} 
for  (  int   j  =  0  ;  j  <  mLength  ;  j  ++  )  { 
if  (  pMinus  [  j  ]  !=  0  )  { 
pMinusEmpty  =  false  ; 
} 
} 
if  (  (  pPlusEmpty  ||  pMinusEmpty  )  &&  !  isZeroRow  (  i  )  )  { 
return   true  ; 
} 
for  (  int   j  =  0  ;  j  <  pLength  ;  j  ++  )  { 
pPlus  [  j  ]  =  0  ; 
} 
for  (  int   j  =  0  ;  j  <  mLength  ;  j  ++  )  { 
pMinus  [  j  ]  =  0  ; 
} 
} 
return   satisfies11  ; 
} 






public   int  [  ]  colsToUpdate  (  )  { 
int   js  [  ]  =  null  ; 
int   pPlusCard  =  0  ,  pMinusCard  =  0  ,  countpPlus  =  0  ,  countpMinus  =  0  ; 
int  [  ]  pPlus  ,  pMinus  ; 
int   m  =  getRowDimension  (  )  ; 
int   n  =  getColumnDimension  (  )  ; 
int   pLength  =  n  ,  mLength  =  n  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
countpPlus  =  0  ; 
countpMinus  =  0  ; 
pPlus  =  getPositiveIndices  (  i  )  ; 
pMinus  =  getNegativeIndices  (  i  )  ; 
for  (  int   j  =  0  ;  j  <  pLength  ;  j  ++  )  { 
if  (  pPlus  [  j  ]  !=  0  )  { 
countpPlus  ++  ; 
} 
} 
for  (  int   j  =  0  ;  j  <  mLength  ;  j  ++  )  { 
if  (  pMinus  [  j  ]  !=  0  )  { 
countpMinus  ++  ; 
} 
} 
if  (  countpPlus  ==  1  )  { 
return   pMinus  ; 
}  else   if  (  countpMinus  ==  1  )  { 
return   pPlus  ; 
} 
} 
return   js  ; 
} 










public   static   PNMatrix   constructWithCopy  (  int  [  ]  [  ]  A  )  { 
int   m  =  A  .  length  ; 
int   n  =  A  [  0  ]  .  length  ; 
PNMatrix   X  =  new   PNMatrix  (  m  ,  n  )  ; 
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






public   PNMatrix   copy  (  )  { 
PNMatrix   X  =  new   PNMatrix  (  m  ,  n  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 






public   Object   clone  (  )  { 
return   this  .  copy  (  )  ; 
} 








public   PNMatrix   eliminateCol  (  int   toDelete  )  { 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
PNMatrix   reduced  =  new   PNMatrix  (  m  ,  n  )  ; 
int  [  ]  cols  =  new   int  [  n  -  1  ]  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  i  !=  toDelete  )  { 
cols  [  count  ++  ]  =  i  ; 
} 
} 
reduced  =  getMatrix  (  0  ,  m  -  1  ,  cols  )  ; 
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






public   int  [  ]  getColumnPackedCopy  (  )  { 
int  [  ]  vals  =  new   int  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  +  j  *  m  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
} 






public   int  [  ]  getRowPackedCopy  (  )  { 
int  [  ]  vals  =  new   int  [  m  *  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
vals  [  i  *  n  +  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   vals  ; 
} 






public   int   getRowDimension  (  )  { 
return   m  ; 
} 






public   int   getColumnDimension  (  )  { 
return   n  ; 
} 







public   int   firstNonZeroRowIndex  (  )  { 
int   m  =  getRowDimension  (  )  ; 
int   n  =  getColumnDimension  (  )  ; 
int   h  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  i  ,  j  )  !=  0  )  { 
return   i  ; 
} 
} 
} 
return   h  ; 
} 







public   PNMatrix   nonZeroIndices  (  )  { 
PNMatrix   X  =  new   PNMatrix  (  m  ,  n  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  i  ,  j  )  ==  0  )  { 
X  .  set  (  i  ,  j  ,  0  )  ; 
}  else  { 
X  .  set  (  i  ,  j  ,  i  +  1  )  ; 
} 
} 
} 
return   X  ; 
} 







public   int   findNonMinimal  (  )  { 
int   k  =  -  1  ; 
int   m  =  getRowDimension  (  )  ,  n  =  getColumnDimension  (  )  ; 
PNMatrix   X  =  new   PNMatrix  (  m  ,  1  )  ; 
PNMatrix   Y  =  new   PNMatrix  (  m  ,  1  )  ; 
PNMatrix   Z  =  new   PNMatrix  (  m  ,  1  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
X  =  getMatrix  (  0  ,  m  -  1  ,  i  ,  i  )  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  i  !=  j  )  { 
Y  =  getMatrix  (  0  ,  m  -  1  ,  j  ,  j  )  ; 
Z  =  X  .  minus  (  Y  )  ; 
if  (  !  (  Z  .  hasNegativeElements  (  )  )  )  { 
return   i  ; 
} 
} 
} 
} 
return   k  ; 
} 






public   boolean   hasNegativeElements  (  )  { 
boolean   hasNegative  =  false  ; 
int   m  =  getRowDimension  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  get  (  i  ,  0  )  <  0  )  { 
return   true  ; 
} 
} 
return   hasNegative  ; 
} 









public   int   firstNonZeroElementIndex  (  int   h  )  { 
int   n  =  getColumnDimension  (  )  ; 
int   k  =  -  1  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  h  ,  j  )  !=  0  )  { 
return   j  ; 
} 
} 
return   k  ; 
} 









public   int  [  ]  findRemainingNZIndices  (  int   h  )  { 
int   n  =  getColumnDimension  (  )  ; 
int  [  ]  k  =  new   int  [  n  ]  ; 
int   count  =  0  ; 
for  (  int   j  =  1  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  h  ,  j  )  !=  0  )  k  [  count  ++  ]  =  j  ; 
} 
return   k  ; 
} 










public   int  [  ]  findRemainingNZCoef  (  int   h  )  { 
int   n  =  getColumnDimension  (  )  ; 
int  [  ]  k  =  new   int  [  n  ]  ; 
int   count  =  0  ; 
int   anElement  ; 
for  (  int   j  =  1  ;  j  <  n  ;  j  ++  )  { 
if  (  (  anElement  =  get  (  h  ,  j  )  )  !=  0  )  { 
k  [  count  ++  ]  =  anElement  ; 
} 
} 
return   k  ; 
} 











public   int   get  (  int   i  ,  int   j  )  { 
return   A  [  i  ]  [  j  ]  ; 
} 
















public   PNMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  )  { 
PNMatrix   X  =  new   PNMatrix  (  i1  -  i0  +  1  ,  j1  -  j0  +  1  )  ; 
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












public   PNMatrix   getMatrix  (  int  [  ]  r  ,  int  [  ]  c  )  { 
PNMatrix   X  =  new   PNMatrix  (  r  .  length  ,  c  .  length  )  ; 
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














public   PNMatrix   getMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  )  { 
PNMatrix   X  =  new   PNMatrix  (  i1  -  i0  +  1  ,  c  .  length  )  ; 
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














public   PNMatrix   getMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  )  { 
PNMatrix   X  =  new   PNMatrix  (  r  .  length  ,  j1  -  j0  +  1  )  ; 
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











public   int  [  ]  getNegativeIndices  (  int   rowNo  )  { 
int   n  =  getColumnDimension  (  )  ; 
try  { 
PNMatrix   A  =  new   PNMatrix  (  1  ,  n  )  ; 
A  =  getMatrix  (  rowNo  ,  rowNo  ,  0  ,  n  -  1  )  ; 
int   count  =  0  ; 
int  [  ]  negativesArray  =  new   int  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  negativesArray  [  i  ]  =  0  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  A  .  get  (  0  ,  i  )  <  0  )  negativesArray  [  count  ++  ]  =  i  +  1  ; 
} 
return   negativesArray  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 











public   int  [  ]  getPositiveIndices  (  int   rowNo  )  { 
int   n  =  getColumnDimension  (  )  ; 
try  { 
PNMatrix   A  =  new   PNMatrix  (  1  ,  n  )  ; 
A  =  getMatrix  (  rowNo  ,  rowNo  ,  0  ,  n  -  1  )  ; 
int   count  =  0  ; 
int  [  ]  positivesArray  =  new   int  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  positivesArray  [  i  ]  =  0  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  A  .  get  (  0  ,  i  )  >  0  )  positivesArray  [  count  ++  ]  =  i  +  1  ; 
} 
return   positivesArray  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
throw   new   ArrayIndexOutOfBoundsException  (  "Submatrix indices"  )  ; 
} 
} 






public   boolean   isZeroMatrix  (  )  { 
int   m  =  getRowDimension  (  )  ; 
int   n  =  getColumnDimension  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  i  ,  j  )  !=  0  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 








public   boolean   isZeroRow  (  int   r  )  { 
PNMatrix   A  =  new   PNMatrix  (  1  ,  getColumnDimension  (  )  )  ; 
A  =  getMatrix  (  r  ,  r  ,  0  ,  getColumnDimension  (  )  -  1  )  ; 
return   A  .  isZeroMatrix  (  )  ; 
} 






public   boolean   isCovered  (  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
if  (  isZeroRow  (  i  )  ||  this  .  transpose  (  )  .  hasNegativeElements  (  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 














public   void   linearlyCombine  (  int   k  ,  int   chk  ,  int  [  ]  j  ,  int  [  ]  jC  )  { 
int   chj  =  0  ; 
int   m  =  getRowDimension  (  )  ; 
for  (  int   i  =  0  ;  i  <  j  .  length  ;  i  ++  )  { 
if  (  j  [  i  ]  !=  0  )  { 
chj  =  jC  [  i  ]  ; 
for  (  int   w  =  0  ;  w  <  m  ;  w  ++  )  { 
set  (  w  ,  j  [  i  ]  -  1  ,  chj  *  get  (  w  ,  k  )  +  chk  *  get  (  w  ,  j  [  i  ]  -  1  )  )  ; 
} 
} 
} 
} 














public   void   linearlyCombine  (  int   k  ,  int  [  ]  alpha  ,  int  [  ]  j  ,  int  [  ]  beta  )  { 
int   m  =  getRowDimension  (  )  ,  n  =  j  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  j  [  i  ]  !=  0  )  { 
for  (  int   w  =  0  ;  w  <  m  ;  w  ++  )  { 
set  (  w  ,  j  [  i  ]  ,  alpha  [  i  ]  *  get  (  w  ,  k  )  +  beta  [  i  ]  *  get  (  w  ,  j  [  i  ]  )  )  ; 
} 
} 
} 
} 







public   int   rowWithNegativeElement  (  )  { 
int   m  =  getRowDimension  (  )  ; 
int   n  =  getColumnDimension  (  )  ; 
int   h  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
if  (  get  (  i  ,  j  )  <  0  )  return   i  ; 
} 
} 
return   h  ; 
} 












public   void   set  (  int   i  ,  int   j  ,  int   s  )  { 
A  [  i  ]  [  j  ]  =  s  ; 
} 

















public   void   setMatrix  (  int   i0  ,  int   i1  ,  int   j0  ,  int   j1  ,  PNMatrix   X  )  { 
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













public   void   setMatrix  (  int  [  ]  r  ,  int  [  ]  c  ,  PNMatrix   X  )  { 
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















public   void   setMatrix  (  int  [  ]  r  ,  int   j0  ,  int   j1  ,  PNMatrix   X  )  { 
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















public   void   setMatrix  (  int   i0  ,  int   i1  ,  int  [  ]  c  ,  PNMatrix   X  )  { 
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






public   PNMatrix   transpose  (  )  { 
PNMatrix   X  =  new   PNMatrix  (  n  ,  m  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  j  ]  [  i  ]  =  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 






public   int   gcd  (  )  { 
int   gcd  =  A  [  0  ]  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  m  ;  i  ++  )  { 
if  (  (  A  [  i  ]  [  0  ]  !=  0  )  ||  (  gcd  !=  0  )  )  { 
gcd  =  gcd2  (  gcd  ,  A  [  i  ]  [  0  ]  )  ; 
} 
} 
return   gcd  ; 
} 










private   int   gcd2  (  int   a  ,  int   b  )  { 
int   gcd  ; 
a  =  Math  .  abs  (  a  )  ; 
b  =  Math  .  abs  (  b  )  ; 
if  (  b  <=  a  )  { 
int   tmp  =  b  ; 
b  =  a  ; 
a  =  tmp  ; 
} 
if  (  a  !=  0  )  { 
for  (  int   tmp  ;  (  b  %=  a  )  !=  0  ;  )  { 
tmp  =  b  ; 
b  =  a  ; 
a  =  tmp  ; 
} 
gcd  =  a  ; 
}  else   if  (  b  !=  0  )  { 
gcd  =  b  ; 
}  else  { 
gcd  =  0  ; 
} 
return   gcd  ; 
} 






public   PNMatrix   uminus  (  )  { 
PNMatrix   X  =  new   PNMatrix  (  m  ,  n  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  -  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   PNMatrix   plus  (  PNMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
PNMatrix   X  =  new   PNMatrix  (  m  ,  n  )  ; 
int  [  ]  [  ]  C  =  X  .  getArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   X  ; 
} 








public   PNMatrix   plusEquals  (  PNMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  +  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 








public   PNMatrix   minus  (  PNMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
int  [  ]  [  ]  C  =  new   int  [  m  ]  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
C  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
PNMatrix   X  =  new   PNMatrix  (  C  )  ; 
return   X  ; 
} 








public   PNMatrix   minusEquals  (  PNMatrix   B  )  { 
checkMatrixDimensions  (  B  )  ; 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  -  B  .  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 








public   PNMatrix   timesEquals  (  int   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  s  *  A  [  i  ]  [  j  ]  ; 
} 
} 
return   this  ; 
} 








public   int   vectorTimes  (  PNMatrix   B  )  { 
int   product  =  0  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
product  +=  A  [  0  ]  [  j  ]  *  B  .  get  (  j  ,  0  )  ; 
} 
return   product  ; 
} 








public   PNMatrix   divideEquals  (  int   s  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  A  [  i  ]  [  j  ]  /  s  ; 
} 
} 
return   this  ; 
} 










public   static   PNMatrix   identity  (  int   m  ,  int   n  )  { 
PNMatrix   A  =  new   PNMatrix  (  m  ,  n  )  ; 
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











public   String   printString  (  int   w  ,  int   d  )  { 
if  (  isZeroMatrix  (  )  )  { 
return  "\nNone\n\n"  ; 
} 
ByteArrayOutputStream   arrayStream  =  new   ByteArrayOutputStream  (  )  ; 
print  (  new   PrintWriter  (  arrayStream  ,  true  )  ,  w  ,  d  )  ; 
return   arrayStream  .  toString  (  )  ; 
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
for  (  int   k  =  0  ;  k  <  padding  ;  k  ++  )  { 
output  .  print  (  ' '  )  ; 
} 
output  .  print  (  s  )  ; 
} 
output  .  println  (  )  ; 
} 
output  .  println  (  )  ; 
} 







private   void   checkMatrixDimensions  (  PNMatrix   B  )  { 
if  (  B  .  m  !=  m  ||  B  .  n  !=  n  )  { 
throw   new   IllegalArgumentException  (  "Matrix dimensions must agree."  )  ; 
} 
} 

public   void   setToZero  (  )  { 
for  (  int   i  =  0  ;  i  <  m  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
A  [  i  ]  [  j  ]  =  0  ; 
} 
} 
} 

public   int  [  ]  getColumn  (  int   i  )  { 
int  [  ]  r  =  new   int  [  this  .  getColumnDimension  (  )  ]  ; 
for  (  int   j  =  0  ;  j  <  this  .  getColumnDimension  (  )  ;  j  ++  )  { 
r  [  j  ]  =  A  [  i  ]  [  j  ]  ; 
} 
return   r  ; 
} 

public   int  [  ]  getRow  (  int   i  )  { 
int  [  ]  r  =  new   int  [  this  .  getRowDimension  (  )  ]  ; 
for  (  int   j  =  0  ;  j  <  this  .  getRowDimension  (  )  ;  j  ++  )  { 
r  [  j  ]  =  A  [  j  ]  [  i  ]  ; 
} 
return   r  ; 
} 



public   void   clearColumn  (  int   place  )  { 
for  (  int   j  =  0  ;  j  <  this  .  getColumnDimension  (  )  ;  j  ++  )  { 
A  [  place  ]  [  j  ]  =  0  ; 
} 
} 
} 

