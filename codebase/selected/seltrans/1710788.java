package   gumbo  .  core  .  util  ; 

import   gumbo  .  core  .  life  .  ObjectPool  ; 
import   gumbo  .  core  .  life  .  impl  .  ObjectPoolImpl  ; 







public   class   MathUtils  { 

private   MathUtils  (  )  { 
} 







public   static   long   newRandomSeed  (  )  { 
return  ++  seedUniquifier  +  System  .  nanoTime  (  )  ; 
} 

private   static   volatile   long   seedUniquifier  =  8682522807148012L  ; 


public   static   final   class   I  { 









public   static   int   mod  (  int   value  ,  int   modulus  )  { 
if  (  modulus  ==  0  )  throw   new   IllegalArgumentException  (  "modulus cannot be zero."  )  ; 
return  (  modulus  +  value  %  modulus  )  %  modulus  ; 
} 








public   static   int   rem  (  int   value  ,  int   modulus  )  { 
if  (  modulus  ==  0  )  throw   new   IllegalArgumentException  (  "modulus cannot be zero."  )  ; 
return   value  %  modulus  ; 
} 
} 


public   static   final   class   L  { 









public   static   long   mod  (  long   value  ,  long   modulus  )  { 
if  (  modulus  ==  0  )  throw   new   IllegalArgumentException  (  "modulus cannot be zero."  )  ; 
return  (  modulus  +  value  %  modulus  )  %  modulus  ; 
} 








public   static   long   rem  (  long   value  ,  long   modulus  )  { 
if  (  modulus  ==  0  )  throw   new   IllegalArgumentException  (  "modulus cannot be zero."  )  ; 
return   value  %  modulus  ; 
} 
} 


public   static   final   class   F  { 

public   static   final   float   SQRT_TWO  =  (  float  )  MathUtils  .  D  .  SQRT_TWO  ; 

public   static   final   float   SQRT_THREE  =  (  float  )  MathUtils  .  D  .  SQRT_THREE  ; 

public   static   final   float   CBRT_THREE  =  (  float  )  MathUtils  .  D  .  CBRT_THREE  ; 

public   static   final   float   PI  =  (  float  )  MathUtils  .  D  .  PI  ; 

public   static   final   float   TWO_PI  =  (  float  )  MathUtils  .  D  .  TWO_PI  ; 

public   static   final   float   FOUR_PI  =  (  float  )  MathUtils  .  D  .  FOUR_PI  ; 

public   static   final   float   HALF_PI  =  (  float  )  MathUtils  .  D  .  HALF_PI  ; 

public   static   final   float   QUARTER_PI  =  (  float  )  MathUtils  .  D  .  QUARTER_PI  ; 

public   static   final   float   SQUARE_PI  =  (  float  )  MathUtils  .  D  .  SQUARE_PI  ; 





public   static   final   String   FORMAT  =  "%6.3f"  ; 






public   static   String   toString  (  float   val  )  { 
return   String  .  format  (  FORMAT  ,  val  )  ; 
} 





public   static   final   float   TOLERANCE  =  (  float  )  0.001  ; 


public   static   void   copyArray  (  float  [  ]  in  ,  float  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  Float  [  ]  in  ,  float  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  float  [  ]  in  ,  Float  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  Float  [  ]  in  ,  Float  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyMatrix  (  float  [  ]  [  ]  in  ,  float  [  ]  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  Float  [  ]  [  ]  in  ,  float  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  float  [  ]  [  ]  in  ,  Float  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  Float  [  ]  [  ]  in  ,  Float  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 








public   float   mod  (  float   value  ,  float   modulus  )  { 
float   div  =  modulus  /  value  ; 
return   value  *  (  div  -  (  int  )  div  )  ; 
} 








public   static   float   rem  (  float   value  ,  float   modulus  )  { 
return   value  %  modulus  ; 
} 










public   static   float   modFloor  (  float   val  ,  float   min  ,  float   max  )  { 
if  (  Float  .  isInfinite  (  val  )  ||  Float  .  isNaN  (  val  )  )  return   min  ; 
float   range  =  max  -  min  ; 
while  (  val  >=  max  )  val  -=  range  ; 
while  (  val  <  min  )  val  +=  range  ; 
return   val  ; 
} 










public   static   float   modCeil  (  float   val  ,  float   min  ,  float   max  )  { 
if  (  Float  .  isInfinite  (  val  )  ||  Float  .  isNaN  (  val  )  )  return   max  ; 
float   range  =  max  -  min  ; 
while  (  val  >  max  )  val  -=  range  ; 
while  (  val  <=  min  )  val  +=  range  ; 
return   val  ; 
} 
} 


public   static   final   class   D  { 

private   D  (  )  { 
} 

public   static   final   double   SQRT_TWO  =  Math  .  pow  (  2.0  ,  1.0  /  2.0  )  ; 

public   static   final   double   SQRT_THREE  =  Math  .  pow  (  3.0  ,  1.0  /  2.0  )  ; 

public   static   final   double   CBRT_THREE  =  Math  .  pow  (  3.0  ,  1.0  /  3.0  )  ; 

public   static   final   double   PI  =  Math  .  PI  ; 

public   static   final   double   TWO_PI  =  Math  .  PI  *  2.0  ; 

public   static   final   double   FOUR_PI  =  Math  .  PI  *  4.0  ; 

public   static   final   double   HALF_PI  =  Math  .  PI  /  2.0  ; 

public   static   final   double   QUARTER_PI  =  Math  .  PI  /  4.0  ; 

public   static   final   double   SQUARE_PI  =  Math  .  PI  *  Math  .  PI  ; 





public   static   final   String   FORMAT  =  "%6.3f"  ; 






public   static   String   toString  (  double   val  )  { 
return   String  .  format  (  FORMAT  ,  val  )  ; 
} 





public   static   final   double   TOLERANCE  =  QuantUtils  .  TOLERANCE  ; 








public   static   boolean   equalsValue  (  double   valA  ,  double   valB  )  { 
return   QuantUtils  .  equalsValue  (  valA  ,  valB  ,  TOLERANCE  )  ; 
} 











public   static   boolean   equalsValue  (  double   valA  ,  double   valB  ,  double   tolerance  )  { 
return   QuantUtils  .  equalsValue  (  valA  ,  valB  ,  tolerance  )  ; 
} 







public   static   double   modRadian  (  double   angle  )  { 
if  (  angle  >=  0.0  &&  angle  <  MathUtils  .  D  .  TWO_PI  )  { 
return   angle  ; 
} 
return   angle  -  (  TWO_PI  *  Math  .  floor  (  angle  /  TWO_PI  )  )  ; 
} 







public   static   double   modDegree  (  double   angle  )  { 
if  (  angle  >=  0.0  &&  angle  <  360.0  )  { 
return   angle  ; 
} 
return   angle  -  (  360.0  *  Math  .  floor  (  angle  /  360.0  )  )  ; 
} 








public   static   double   mod  (  double   value  ,  double   modulus  )  { 
double   div  =  modulus  /  value  ; 
return   value  *  (  div  -  (  int  )  div  )  ; 
} 








public   static   double   rem  (  double   value  ,  double   modulus  )  { 
return   value  %  modulus  ; 
} 










public   static   double   modFloor  (  double   val  ,  double   min  ,  double   max  )  { 
if  (  Double  .  isInfinite  (  val  )  ||  Double  .  isNaN  (  val  )  )  return   min  ; 
double   range  =  max  -  min  ; 
while  (  val  >=  max  )  val  -=  range  ; 
while  (  val  <  min  )  val  +=  range  ; 
return   val  ; 
} 










public   static   double   modCeil  (  double   val  ,  double   min  ,  double   max  )  { 
if  (  Double  .  isInfinite  (  val  )  ||  Double  .  isNaN  (  val  )  )  return   max  ; 
double   range  =  max  -  min  ; 
while  (  val  >  max  )  val  -=  range  ; 
while  (  val  <=  min  )  val  +=  range  ; 
return   val  ; 
} 
} 


public   static   final   class   D2  { 

private   D2  (  )  { 
} 

public   static   Maker  .  FromNone  <  double  [  ]  >  getTupleMaker  (  )  { 
return   TUPLE_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  >  TUPLE_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  make  (  )  { 
return   new   double  [  2  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  >  getTuplePool  (  )  { 
return   TUPLE_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  >  TUPLE_POOL  =  new   ObjectPoolImpl  <  double  [  ]  >  (  getTupleMaker  (  )  )  ; 


public   static   void   setTuple  (  double   x  ,  double   y  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
} 


public   static   void   clearTuple  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
} 


public   static   void   clearTuple  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 







public   static   final   Double   NORMALIZED_ZERO_TUPLE_X  =  1.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE_Y  =  0.0  ; 








public   static   double  [  ]  normalizeTuple  (  double  [  ]  in  ,  double  [  ]  out  )  { 
double   x  =  in  [  0  ]  ; 
double   y  =  in  [  1  ]  ; 
if  (  x  ==  0.0  &&  y  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE_Y  ; 
}  else  { 
double   length  =  Math  .  sqrt  (  x  *  x  +  y  *  y  )  ; 
if  (  length  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE_Y  ; 
}  else   if  (  length  ==  1.0  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
}  else  { 
double   lengthInv  =  1.0  /  length  ; 
out  [  0  ]  =  lengthInv  *  x  ; 
out  [  1  ]  =  lengthInv  *  y  ; 
} 
} 
return   out  ; 
} 

public   static   Maker  .  FromNone  <  double  [  ]  [  ]  >  getMatrixMaker  (  )  { 
return   MATRIX_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  [  ]  >  MATRIX_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  [  ]  make  (  )  { 
return   new   double  [  2  ]  [  2  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  [  ]  >  getMatrixPool  (  )  { 
return   MATRIX_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  [  ]  >  MATRIX_POOL  =  new   ObjectPoolImpl  <  double  [  ]  [  ]  >  (  getMatrixMaker  (  )  )  ; 


public   static   void   copyMatrix  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   toMatrixByRow  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   toMatrixByCol  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByRow  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByCol  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 





public   static   void   setIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
if  (  rowI  ==  colI  )  val  [  rowI  ]  [  colI  ]  =  1.0  ;  else   val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 





public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   diagI  =  0  ;  diagI  <  2  ;  diagI  ++  )  { 
if  (  val  [  diagI  ]  [  diagI  ]  !=  1.0  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  val  [  rowI  ]  [  colI  ]  !=  0.0  )  return   false  ; 
} 
} 
return   true  ; 
} 







public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  ,  double   tolerance  )  { 
if  (  tolerance  <  0.0  )  tolerance  =  -  tolerance  ; 
for  (  int   diagI  =  0  ;  diagI  <  2  ;  diagI  ++  )  { 
if  (  !  D  .  equalsValue  (  val  [  diagI  ]  [  diagI  ]  ,  1.0  ,  tolerance  )  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  2  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  2  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  !  D  .  equalsValue  (  val  [  rowI  ]  [  colI  ]  ,  0.0  ,  tolerance  )  )  return   false  ; 
} 
} 
return   true  ; 
} 
} 


public   static   final   class   D3  { 

private   D3  (  )  { 
} 

public   static   Maker  .  FromNone  <  double  [  ]  >  getTupleMaker  (  )  { 
return   TUPLE_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  >  TUPLE_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  make  (  )  { 
return   new   double  [  3  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  >  getTuplePool  (  )  { 
return   TUPLE_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  >  TUPLE_POOL  =  new   ObjectPoolImpl  <  double  [  ]  >  (  getTupleMaker  (  )  )  ; 


public   static   void   setTuple  (  double   x  ,  double   y  ,  double   z  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
out  [  2  ]  =  z  ; 
} 


public   static   void   clearTuple  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
} 


public   static   void   clearTuple  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 







public   static   final   Double   NORMALIZED_ZERO_TUPLE_X  =  1.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE_Y  =  0.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE_Z  =  0.0  ; 








public   static   double  [  ]  normalizeTuple  (  double  [  ]  in  ,  double  [  ]  out  )  { 
double   x  =  in  [  0  ]  ; 
double   y  =  in  [  1  ]  ; 
double   z  =  in  [  2  ]  ; 
if  (  x  ==  0.0  &&  y  ==  0.0  &&  z  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE_Y  ; 
out  [  2  ]  =  NORMALIZED_ZERO_TUPLE_Z  ; 
}  else  { 
double   length  =  Math  .  sqrt  (  x  *  x  +  y  *  y  +  z  *  z  )  ; 
if  (  length  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE_Y  ; 
out  [  2  ]  =  NORMALIZED_ZERO_TUPLE_Z  ; 
}  else   if  (  length  ==  1.0  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
out  [  2  ]  =  z  ; 
}  else  { 
double   lengthInv  =  1.0  /  length  ; 
out  [  0  ]  =  lengthInv  *  x  ; 
out  [  1  ]  =  lengthInv  *  y  ; 
out  [  2  ]  =  lengthInv  *  z  ; 
} 
} 
return   out  ; 
} 

public   static   Maker  .  FromNone  <  double  [  ]  [  ]  >  getMatrixMaker  (  )  { 
return   MATRIX_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  [  ]  >  MATRIX_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  [  ]  make  (  )  { 
return   new   double  [  3  ]  [  3  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  [  ]  >  getMatrixPool  (  )  { 
return   MATRIX_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  [  ]  >  MATRIX_POOL  =  new   ObjectPoolImpl  <  double  [  ]  [  ]  >  (  getMatrixMaker  (  )  )  ; 


public   static   void   copyMatrix  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   toMatrixByRow  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   toMatrixByCol  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByRow  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByCol  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 





public   static   void   setIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
if  (  rowI  ==  colI  )  val  [  rowI  ]  [  colI  ]  =  1.0  ;  else   val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 





public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   diagI  =  0  ;  diagI  <  3  ;  diagI  ++  )  { 
if  (  val  [  diagI  ]  [  diagI  ]  !=  1.0  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  val  [  rowI  ]  [  colI  ]  !=  0.0  )  return   false  ; 
} 
} 
return   true  ; 
} 







public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  ,  double   tolerance  )  { 
if  (  tolerance  <  0.0  )  tolerance  =  -  tolerance  ; 
for  (  int   diagI  =  0  ;  diagI  <  3  ;  diagI  ++  )  { 
if  (  !  D  .  equalsValue  (  val  [  diagI  ]  [  diagI  ]  ,  1.0  ,  tolerance  )  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  !  D  .  equalsValue  (  val  [  rowI  ]  [  colI  ]  ,  0.0  ,  tolerance  )  )  return   false  ; 
} 
} 
return   true  ; 
} 






public   static   void   transpose  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D3  .  isIdentity  (  in  )  )  { 
if  (  in  !=  out  )  D3  .  setIdentity  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  matIn  =  D3  .  getMatrixPool  (  )  .  borrowObject  (  )  ; 
try  { 
double  [  ]  [  ]  copyIn  ; 
if  (  in  ==  out  )  { 
D3  .  copyMatrix  (  in  ,  matIn  )  ; 
copyIn  =  matIn  ; 
}  else  { 
copyIn  =  in  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
out  [  colI  ]  [  rowI  ]  =  copyIn  [  rowI  ]  [  colI  ]  ; 
} 
} 
}  finally  { 
D3  .  getMatrixPool  (  )  .  returnObject  (  matIn  )  ; 
} 
} 







public   static   void   invert  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D3  .  isIdentity  (  in  )  )  { 
if  (  in  !=  out  )  D3  .  setIdentity  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  matIn  =  D3  .  getMatrixPool  (  )  .  borrowObject  (  )  ; 
try  { 
D3  .  copyMatrix  (  in  ,  matIn  )  ; 
double  [  ]  [  ]  copyIn  =  matIn  ; 
D3  .  setIdentity  (  out  )  ; 
for  (  int   i  =  0  ;  i  <  3  ;  i  ++  )  { 
double   alpha  =  copyIn  [  i  ]  [  i  ]  ; 
if  (  alpha  ==  0.0  )  { 
throw   new   IllegalStateException  (  "Matrix is singular.  in="  +  toStringMatrix  (  in  )  )  ; 
} 
for  (  int   j  =  0  ;  j  <  3  ;  j  ++  )  { 
copyIn  [  i  ]  [  j  ]  =  copyIn  [  i  ]  [  j  ]  /  alpha  ; 
out  [  i  ]  [  j  ]  =  out  [  i  ]  [  j  ]  /  alpha  ; 
} 
for  (  int   k  =  0  ;  k  <  3  ;  k  ++  )  { 
if  (  (  k  -  i  )  !=  0  )  { 
double   beta  =  copyIn  [  k  ]  [  i  ]  ; 
for  (  int   j  =  0  ;  j  <  3  ;  j  ++  )  { 
copyIn  [  k  ]  [  j  ]  -=  beta  *  copyIn  [  i  ]  [  j  ]  ; 
out  [  k  ]  [  j  ]  -=  beta  *  out  [  i  ]  [  j  ]  ; 
} 
} 
} 
} 
}  finally  { 
D3  .  getMatrixPool  (  )  .  returnObject  (  matIn  )  ; 
} 
} 








public   static   void   multiply  (  double  [  ]  [  ]  inA  ,  double  [  ]  [  ]  inB  ,  double  [  ]  [  ]  out  )  { 
if  (  inA  ==  out  )  throw   new   IllegalArgumentException  (  "inA and out must be different references."  )  ; 
if  (  inB  ==  out  )  throw   new   IllegalArgumentException  (  "inB and out must be different references."  )  ; 
if  (  D3  .  isIdentity  (  inA  )  )  { 
D3  .  copyMatrix  (  inB  ,  out  )  ; 
return  ; 
} 
if  (  D3  .  isIdentity  (  inB  )  )  { 
D3  .  copyMatrix  (  inA  ,  out  )  ; 
return  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
double   total  =  0.0  ; 
for  (  int   dimI  =  0  ;  dimI  <  3  ;  dimI  ++  )  { 
total  +=  inA  [  rowI  ]  [  dimI  ]  *  inB  [  dimI  ]  [  colI  ]  ; 
} 
out  [  rowI  ]  [  colI  ]  =  total  ; 
} 
} 
} 





public   static   String   toStringMatrix  (  double  [  ]  [  ]  matrix  )  { 
return  "["  +  "["  +  matrix  [  0  ]  [  0  ]  +  " "  +  matrix  [  0  ]  [  1  ]  +  " "  +  matrix  [  0  ]  [  2  ]  +  "]"  +  "["  +  matrix  [  1  ]  [  0  ]  +  " "  +  matrix  [  1  ]  [  1  ]  +  " "  +  matrix  [  1  ]  [  2  ]  +  "]"  +  "["  +  matrix  [  2  ]  [  0  ]  +  " "  +  matrix  [  2  ]  [  1  ]  +  " "  +  matrix  [  2  ]  [  2  ]  +  "]"  +  "]"  ; 
} 
} 


public   static   final   class   D4  { 

private   D4  (  )  { 
} 

public   static   Maker  .  FromNone  <  double  [  ]  >  getTupleMaker  (  )  { 
return   TUPLE_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  >  TUPLE_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  make  (  )  { 
return   new   double  [  4  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  >  getTuplePool  (  )  { 
return   TUPLE_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  >  TUPLE_POOL  =  new   ObjectPoolImpl  <  double  [  ]  >  (  getTupleMaker  (  )  )  ; 


public   static   void   setTuple4  (  double   x  ,  double   y  ,  double   z  ,  double   w  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
out  [  2  ]  =  z  ; 
out  [  3  ]  =  w  ; 
} 


public   static   void   clearTuple  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
out  [  3  ]  =  0.0  ; 
} 


public   static   void   clearTuple  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
out  [  3  ]  =  0.0  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 

public   static   Maker  .  FromNone  <  double  [  ]  [  ]  >  getMatrixMaker  (  )  { 
return   MATRIX_MAKER  ; 
} 

private   static   final   Maker  .  FromNone  <  double  [  ]  [  ]  >  MATRIX_MAKER  =  new   Maker  .  FromNone  <  double  [  ]  [  ]  >  (  )  { 

@  Override 
public   double  [  ]  [  ]  make  (  )  { 
return   new   double  [  4  ]  [  4  ]  ; 
} 
}  ; 

public   static   ObjectPool  <  double  [  ]  [  ]  >  getMatrixPool  (  )  { 
return   MATRIX_POOL  ; 
} 

private   static   final   ObjectPool  <  double  [  ]  [  ]  >  MATRIX_POOL  =  new   ObjectPoolImpl  <  double  [  ]  [  ]  >  (  getMatrixMaker  (  )  )  ; 


public   static   void   copyMatrix  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   toMatrixByRow  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   toMatrixByCol  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByRow  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   fromMatrixByCol  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 





public   static   void   setIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
if  (  rowI  ==  colI  )  val  [  rowI  ]  [  colI  ]  =  1.0  ;  else   val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 





public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  )  { 
for  (  int   diagI  =  0  ;  diagI  <  4  ;  diagI  ++  )  { 
if  (  val  [  diagI  ]  [  diagI  ]  !=  1.0  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  val  [  rowI  ]  [  colI  ]  !=  0.0  )  return   false  ; 
} 
} 
return   true  ; 
} 







public   static   boolean   isIdentity  (  double  [  ]  [  ]  val  ,  double   tolerance  )  { 
if  (  tolerance  <  0.0  )  tolerance  =  -  tolerance  ; 
for  (  int   diagI  =  0  ;  diagI  <  4  ;  diagI  ++  )  { 
if  (  !  D  .  equalsValue  (  val  [  diagI  ]  [  diagI  ]  ,  1.0  ,  tolerance  )  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  !  D  .  equalsValue  (  val  [  rowI  ]  [  colI  ]  ,  0.0  ,  tolerance  )  )  return   false  ; 
} 
} 
return   true  ; 
} 






public   static   void   transpose  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D4  .  isIdentity  (  in  )  )  { 
if  (  in  !=  out  )  D4  .  setIdentity  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  matIn  =  D4  .  getMatrixPool  (  )  .  borrowObject  (  )  ; 
try  { 
double  [  ]  [  ]  copyIn  ; 
if  (  in  ==  out  )  { 
D4  .  copyMatrix  (  in  ,  matIn  )  ; 
copyIn  =  matIn  ; 
}  else  { 
copyIn  =  in  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
out  [  colI  ]  [  rowI  ]  =  copyIn  [  rowI  ]  [  colI  ]  ; 
} 
} 
}  finally  { 
D4  .  getMatrixPool  (  )  .  returnObject  (  matIn  )  ; 
} 
} 







public   static   void   invert  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D4  .  isIdentity  (  in  )  )  { 
if  (  in  !=  out  )  D4  .  setIdentity  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  matIn  =  D4  .  getMatrixPool  (  )  .  borrowObject  (  )  ; 
try  { 
D4  .  copyMatrix  (  in  ,  matIn  )  ; 
double  [  ]  [  ]  copyIn  =  matIn  ; 
D4  .  setIdentity  (  out  )  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
double   alpha  =  copyIn  [  i  ]  [  i  ]  ; 
if  (  alpha  ==  0.0  )  { 
throw   new   IllegalStateException  (  "Matrix is singular.  in="  +  toStringMatrix  (  in  )  )  ; 
} 
for  (  int   j  =  0  ;  j  <  4  ;  j  ++  )  { 
copyIn  [  i  ]  [  j  ]  =  copyIn  [  i  ]  [  j  ]  /  alpha  ; 
out  [  i  ]  [  j  ]  =  out  [  i  ]  [  j  ]  /  alpha  ; 
} 
for  (  int   k  =  0  ;  k  <  4  ;  k  ++  )  { 
if  (  (  k  -  i  )  !=  0  )  { 
double   beta  =  copyIn  [  k  ]  [  i  ]  ; 
for  (  int   j  =  0  ;  j  <  4  ;  j  ++  )  { 
copyIn  [  k  ]  [  j  ]  -=  beta  *  copyIn  [  i  ]  [  j  ]  ; 
out  [  k  ]  [  j  ]  -=  beta  *  out  [  i  ]  [  j  ]  ; 
} 
} 
} 
} 
}  finally  { 
D4  .  getMatrixPool  (  )  .  returnObject  (  matIn  )  ; 
} 
} 








public   static   void   multiply  (  double  [  ]  [  ]  inA  ,  double  [  ]  [  ]  inB  ,  double  [  ]  [  ]  out  )  { 
if  (  inA  ==  out  )  throw   new   IllegalArgumentException  (  "inA and out must be different references."  )  ; 
if  (  inB  ==  out  )  throw   new   IllegalArgumentException  (  "inB and out must be different references."  )  ; 
if  (  D4  .  isIdentity  (  inA  )  )  { 
D4  .  copyMatrix  (  inB  ,  out  )  ; 
return  ; 
} 
if  (  D4  .  isIdentity  (  inB  )  )  { 
D4  .  copyMatrix  (  inA  ,  out  )  ; 
return  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
double   total  =  0.0  ; 
for  (  int   dimI  =  0  ;  dimI  <  4  ;  dimI  ++  )  { 
total  +=  inA  [  rowI  ]  [  dimI  ]  *  inB  [  dimI  ]  [  colI  ]  ; 
} 
out  [  rowI  ]  [  colI  ]  =  total  ; 
} 
} 
} 





public   static   String   toStringMatrix  (  double  [  ]  [  ]  matrix  )  { 
return  "["  +  "["  +  matrix  [  0  ]  [  0  ]  +  " "  +  matrix  [  0  ]  [  1  ]  +  " "  +  matrix  [  0  ]  [  2  ]  +  " "  +  matrix  [  0  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  1  ]  [  0  ]  +  " "  +  matrix  [  1  ]  [  1  ]  +  " "  +  matrix  [  1  ]  [  2  ]  +  " "  +  matrix  [  1  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  2  ]  [  0  ]  +  " "  +  matrix  [  2  ]  [  1  ]  +  " "  +  matrix  [  2  ]  [  2  ]  +  " "  +  matrix  [  2  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  3  ]  [  0  ]  +  " "  +  matrix  [  3  ]  [  1  ]  +  " "  +  matrix  [  3  ]  [  2  ]  +  " "  +  matrix  [  3  ]  [  3  ]  +  "]"  +  "]"  ; 
} 
} 


public   static   final   class   Dn  { 

private   Dn  (  )  { 
} 


public   static   void   clearArray  (  double  [  ]  val  )  { 
for  (  int   valI  =  0  ;  valI  <  val  .  length  ;  valI  ++  )  { 
val  [  valI  ]  =  0.0  ; 
} 
} 


public   static   void   clearArray  (  Double  [  ]  val  )  { 
for  (  int   valI  =  0  ;  valI  <  val  .  length  ;  valI  ++  )  { 
val  [  valI  ]  =  0.0  ; 
} 
} 


public   static   void   copyArray  (  double  [  ]  in  ,  double  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   copyArray  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
for  (  int   valI  =  0  ;  valI  <  in  .  length  ;  valI  ++  )  { 
out  [  valI  ]  =  in  [  valI  ]  ; 
} 
} 


public   static   void   clearMatrix  (  double  [  ]  [  ]  val  )  { 
int   rowC  =  val  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  val  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 


public   static   void   clearMatrix  (  Double  [  ]  [  ]  val  )  { 
int   rowC  =  val  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  val  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 


public   static   void   copyMatrix  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  Double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  double  [  ]  [  ]  in  ,  Double  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   copyMatrix  (  Double  [  ]  [  ]  in  ,  Double  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   getMatrixRow  (  double  [  ]  [  ]  in  ,  int   rowI  ,  double  [  ]  out  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  colI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 


public   static   void   getMatrixCol  (  double  [  ]  [  ]  in  ,  int   colI  ,  double  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
out  [  rowI  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 



public   static   void   setMatrixRow  (  double  [  ]  in  ,  int   rowI  ,  double  [  ]  [  ]  out  )  { 
int   colC  =  in  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  colI  ]  ; 
} 
} 



public   static   void   setMatrixCol  (  double  [  ]  in  ,  int   colI  ,  double  [  ]  [  ]  out  )  { 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  rowI  ]  ; 
} 
} 



public   static   void   toMatrixByRow  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
int   colC  =  in  .  length  /  rowC  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 



public   static   void   toMatrixByCol  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
int   colC  =  in  .  length  /  rowC  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 



public   static   void   fromMatrixByRow  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
int   colC  =  out  .  length  /  rowC  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 



public   static   void   fromMatrixByCol  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
int   colC  =  out  .  length  /  rowC  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 









public   static   void   multiply  (  double  [  ]  vecInA  ,  double  [  ]  [  ]  matInB  ,  double  [  ]  vecOut  )  { 
if  (  vecInA  ==  vecOut  )  throw   new   IllegalArgumentException  (  "vecInA and vecOut must be different references."  )  ; 
int   rowC  =  vecInA  .  length  ; 
int   colC  =  vecOut  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
double   total  =  0.0  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
total  +=  vecInA  [  rowI  ]  *  matInB  [  rowI  ]  [  colI  ]  ; 
} 
vecOut  [  colI  ]  =  total  ; 
} 
} 









public   static   void   multiply  (  double  [  ]  [  ]  matInA  ,  double  [  ]  vecInB  ,  double  [  ]  vecOut  )  { 
if  (  vecInB  ==  vecOut  )  throw   new   IllegalArgumentException  (  "inB and out must be different references."  )  ; 
int   colC  =  vecInB  .  length  ; 
int   rowC  =  vecOut  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
double   total  =  0.0  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
total  +=  matInA  [  rowI  ]  [  colI  ]  *  vecInB  [  colI  ]  ; 
} 
vecOut  [  rowI  ]  =  total  ; 
} 
} 
} 
} 

