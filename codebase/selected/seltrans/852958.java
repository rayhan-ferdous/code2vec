package   com  .  msli  .  graphic  .  math  ; 

import   com  .  msli  .  core  .  util  .  Quantitative  ; 







public   final   class   MathUtils  { 

private   MathUtils  (  )  { 
} 


public   static   final   Scalar   ZERO_TUPLE1  =  new   Scalar  .  Unmod  (  new   Scalar  .  Impl  (  0.0  )  )  ; 


public   static   final   Scalar   MAX_TUPLE1  =  new   Scalar  .  Unmod  (  new   Scalar  .  Impl  (  Double  .  POSITIVE_INFINITY  )  )  ; 


public   static   final   Scalar   MIN_TUPLE1  =  new   Scalar  .  Unmod  (  new   Scalar  .  Impl  (  Double  .  NEGATIVE_INFINITY  )  )  ; 


public   static   final   Scalar   NAN_TUPLE1  =  new   Scalar  .  Unmod  (  new   Scalar  .  Impl  (  Double  .  NaN  )  )  ; 


public   static   final   Tuple2   ZERO_TUPLE2  =  new   Tuple2  .  Unmod  (  new   Tuple2  .  Impl  (  0.0  ,  0.0  )  )  ; 


public   static   final   Tuple2   MAX_TUPLE2  =  new   Tuple2  .  Unmod  (  new   Tuple2  .  Impl  (  Double  .  POSITIVE_INFINITY  ,  Double  .  POSITIVE_INFINITY  )  )  ; 


public   static   final   Tuple2   MIN_TUPLE2  =  new   Tuple2  .  Unmod  (  new   Tuple2  .  Impl  (  Double  .  NEGATIVE_INFINITY  ,  Double  .  NEGATIVE_INFINITY  )  )  ; 


public   static   final   Tuple2   NAN_TUPLE2  =  new   Tuple2  .  Unmod  (  new   Tuple2  .  Impl  (  Double  .  NaN  ,  Double  .  NaN  )  )  ; 


public   static   final   UnitVector2   PLUS_X_DIRECTION2  =  new   UnitVector2  .  Unmod  (  new   UnitVector2  .  Impl  (  1.0  ,  0.0  )  )  ; 


public   static   final   UnitVector2   PLUS_Y_DIRECTION2  =  new   UnitVector2  .  Unmod  (  new   UnitVector2  .  Impl  (  0.0  ,  1.0  )  )  ; 


public   static   final   UnitVector2   MINUS_X_DIRECTION2  =  new   UnitVector2  .  Unmod  (  new   UnitVector2  .  Impl  (  -  1.0  ,  0.0  )  )  ; 


public   static   final   UnitVector2   MINUS_Y_DIRECTION2  =  new   UnitVector2  .  Unmod  (  new   UnitVector2  .  Impl  (  0.0  ,  -  1.0  )  )  ; 


public   static   final   Point2   ORIGIN2  =  new   Point2  .  Unmod  (  new   Point2  .  Impl  (  ZERO_TUPLE2  )  )  ; 


public   static   final   UnitVector2   X_AXIS2  =  PLUS_X_DIRECTION2  ; 


public   static   final   UnitVector2   Y_AXIS2  =  PLUS_Y_DIRECTION2  ; 





public   static   final   UnitVector2   NORMALIZED_ZERO_TUPLE2  =  new   UnitVector2  .  Unmod  (  new   UnitVector2  .  Impl  (  D  .  NORMALIZED_ZERO_TUPLE2_X  ,  D  .  NORMALIZED_ZERO_TUPLE2_Y  )  )  ; 


public   static   final   Point2   DEFAULT_POSITION2  =  ORIGIN2  ; 





public   static   final   UnitVector2   DEFAULT_DIRECTION2  =  PLUS_X_DIRECTION2  ; 






public   static   UnitVector2   getAxis2  (  int   dimI  )  { 
switch  (  dimI  )  { 
case   0  : 
return   X_AXIS2  ; 
case   1  : 
return   Y_AXIS2  ; 
default  : 
throw   new   IndexOutOfBoundsException  (  "Axis dimension must be [0, 1]. dimI="  +  dimI  )  ; 
} 
} 


public   static   final   Tuple3   ZERO_TUPLE3  =  new   Tuple3  .  Unmod  (  new   Tuple3  .  Impl  (  0.0  ,  0.0  ,  0.0  )  )  ; 


public   static   final   Tuple3   MAX_TUPLE3  =  new   Tuple3  .  Unmod  (  new   Tuple3  .  Impl  (  Double  .  POSITIVE_INFINITY  ,  Double  .  POSITIVE_INFINITY  ,  Double  .  POSITIVE_INFINITY  )  )  ; 


public   static   final   Tuple3   MIN_TUPLE3  =  new   Tuple3  .  Unmod  (  new   Tuple3  .  Impl  (  Double  .  NEGATIVE_INFINITY  ,  Double  .  NEGATIVE_INFINITY  ,  Double  .  NEGATIVE_INFINITY  )  )  ; 


public   static   final   Tuple3   NAN_TUPLE3  =  new   Tuple3  .  Unmod  (  new   Tuple3  .  Impl  (  Double  .  NaN  ,  Double  .  NaN  ,  Double  .  NaN  )  )  ; 


public   static   final   UnitVector3   PLUS_X_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  1.0  ,  0.0  ,  0.0  )  )  ; 


public   static   final   UnitVector3   PLUS_Y_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  0.0  ,  1.0  ,  0.0  )  )  ; 


public   static   final   UnitVector3   PLUS_Z_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  0.0  ,  0.0  ,  1.0  )  )  ; 


public   static   final   UnitVector3   MINUS_X_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  -  1.0  ,  0.0  ,  0.0  )  )  ; 


public   static   final   UnitVector3   MINUS_Y_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  0.0  ,  -  1.0  ,  0.0  )  )  ; 


public   static   final   UnitVector3   MINUS_Z_DIRECTION3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  0.0  ,  0.0  ,  -  1.0  )  )  ; 


public   static   final   Point3   ORIGIN3  =  new   Point3  .  Unmod  (  new   Point3  .  Impl  (  ZERO_TUPLE3  )  )  ; 


public   static   final   UnitVector3   X_AXIS3  =  PLUS_X_DIRECTION3  ; 


public   static   final   UnitVector3   Y_AXIS3  =  PLUS_Y_DIRECTION3  ; 


public   static   final   UnitVector3   Z_AXIS3  =  PLUS_Z_DIRECTION3  ; 





public   static   final   UnitVector3   NORMALIZED_ZERO_TUPLE3  =  new   UnitVector3  .  Unmod  (  new   UnitVector3  .  Impl  (  D  .  NORMALIZED_ZERO_TUPLE3_X  ,  D  .  NORMALIZED_ZERO_TUPLE3_Y  ,  D  .  NORMALIZED_ZERO_TUPLE3_Z  )  )  ; 


public   static   final   Point3   DEFAULT_POSITION3  =  ORIGIN3  ; 





public   static   final   UnitVector3   DEFAULT_DIRECTION3  =  PLUS_X_DIRECTION3  ; 






public   static   UnitVector3   getAxis3  (  int   dimI  )  { 
switch  (  dimI  )  { 
case   0  : 
return   X_AXIS3  ; 
case   1  : 
return   Y_AXIS3  ; 
case   2  : 
return   Z_AXIS3  ; 
default  : 
throw   new   IndexOutOfBoundsException  (  "Axis dimension must be [0, 2]. dimI="  +  dimI  )  ; 
} 
} 


public   static   final   class   F  { 





public   static   final   float   TOLERANCE  =  (  float  )  0.001  ; 

public   static   final   float   PI  =  (  float  )  Math  .  PI  ; 

public   static   final   float   TWO_PI  =  (  float  )  (  Math  .  PI  *  2.0  )  ; 

public   static   final   float   HALF_PI  =  (  float  )  (  Math  .  PI  /  2.0  )  ; 

public   static   final   float   QUARTER_PI  =  (  float  )  (  Math  .  PI  /  4.0  )  ; 


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





public   static   final   double   TOLERANCE  =  Quantitative  .  Utils  .  TOLERANCE  ; 








public   static   boolean   equalsValue  (  double   valA  ,  double   valB  )  { 
return   Quantitative  .  Utils  .  equalsValue  (  valA  ,  valB  ,  TOLERANCE  )  ; 
} 











public   static   boolean   equalsValue  (  double   valA  ,  double   valB  ,  double   tolerance  )  { 
return   Quantitative  .  Utils  .  equalsValue  (  valA  ,  valB  ,  tolerance  )  ; 
} 

public   static   final   double   PI  =  Math  .  PI  ; 

public   static   final   double   TWO_PI  =  Math  .  PI  *  2.0  ; 

public   static   final   double   HALF_PI  =  Math  .  PI  /  2.0  ; 

public   static   final   double   QUARTER_PI  =  Math  .  PI  /  4.0  ; 







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


public   static   void   matrixToArray  (  double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   matrixToArray  (  Double  [  ]  [  ]  in  ,  double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   matrixToArray  (  double  [  ]  [  ]  in  ,  Double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 


public   static   void   matrixToArray  (  Double  [  ]  [  ]  in  ,  Double  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  in  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  in  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  eleI  ++  ]  =  in  [  rowI  ]  [  colI  ]  ; 
} 
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





public   static   void   identityMatrix  (  double  [  ]  [  ]  val  )  { 
int   rowC  =  val  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  val  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
if  (  rowI  ==  colI  )  val  [  rowI  ]  [  colI  ]  =  1.0  ;  else   val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 





public   static   void   identityMatrix  (  Double  [  ]  [  ]  val  )  { 
int   rowC  =  val  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  val  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
if  (  rowI  ==  colI  )  val  [  rowI  ]  [  colI  ]  =  1.0  ;  else   val  [  rowI  ]  [  colI  ]  =  0.0  ; 
} 
} 
} 





public   static   boolean   isIdentityMatrix  (  double  [  ]  [  ]  val  )  { 
int   rowC  =  val  .  length  ; 
for  (  int   diagI  =  0  ;  diagI  <  rowC  ;  diagI  ++  )  { 
if  (  val  [  diagI  ]  [  diagI  ]  !=  1.0  )  return   false  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  rowC  ;  colI  ++  )  { 
if  (  colI  ==  rowI  )  continue  ; 
if  (  val  [  rowI  ]  [  colI  ]  !=  0.0  )  return   false  ; 
} 
} 
return   true  ; 
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


public   static   void   arrayToMatrix  (  double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  out  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   arrayToMatrix  (  Double  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  out  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   arrayToMatrix  (  double  [  ]  in  ,  Double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  out  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   arrayToMatrix  (  Double  [  ]  in  ,  Double  [  ]  [  ]  out  )  { 
int   eleI  =  0  ; 
int   rowC  =  out  .  length  ; 
for  (  int   rowI  =  0  ;  rowI  <  rowC  ;  rowI  ++  )  { 
int   colC  =  out  [  rowI  ]  .  length  ; 
for  (  int   colI  =  0  ;  colI  <  colC  ;  colI  ++  )  { 
out  [  rowI  ]  [  colI  ]  =  in  [  eleI  ++  ]  ; 
} 
} 
} 


public   static   void   setTuple2  (  double   x  ,  double   y  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
} 


public   static   void   clearTuple2  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
} 


public   static   void   clearTuple2  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
} 


public   static   void   copyTuple2  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple2  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple2  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   copyTuple2  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
} 


public   static   void   setTuple3  (  double   x  ,  double   y  ,  double   z  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
out  [  2  ]  =  z  ; 
} 


public   static   void   clearTuple3  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
} 


public   static   void   clearTuple3  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
} 


public   static   void   copyTuple3  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple3  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple3  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   copyTuple3  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
} 


public   static   void   setTuple4  (  double   x  ,  double   y  ,  double   z  ,  double   w  ,  double  [  ]  out  )  { 
out  [  0  ]  =  x  ; 
out  [  1  ]  =  y  ; 
out  [  2  ]  =  z  ; 
out  [  3  ]  =  w  ; 
} 


public   static   void   clearTuple4  (  double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
out  [  3  ]  =  0.0  ; 
} 


public   static   void   clearTuple4  (  Double  [  ]  out  )  { 
out  [  0  ]  =  0.0  ; 
out  [  1  ]  =  0.0  ; 
out  [  2  ]  =  0.0  ; 
out  [  3  ]  =  0.0  ; 
} 


public   static   void   copyTuple4  (  double  [  ]  in  ,  double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple4  (  Double  [  ]  in  ,  double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple4  (  double  [  ]  in  ,  Double  [  ]  out  )  { 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
} 


public   static   void   copyTuple4  (  Double  [  ]  in  ,  Double  [  ]  out  )  { 
if  (  in  ==  out  )  return  ; 
out  [  0  ]  =  in  [  0  ]  ; 
out  [  1  ]  =  in  [  1  ]  ; 
out  [  2  ]  =  in  [  2  ]  ; 
out  [  3  ]  =  in  [  3  ]  ; 
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







public   static   final   Double   NORMALIZED_ZERO_TUPLE2_X  =  1.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE2_Y  =  0.0  ; 







public   static   double  [  ]  normalizeTuple2  (  double  [  ]  in  ,  double  [  ]  out  )  { 
double   x  =  in  [  0  ]  ; 
double   y  =  in  [  1  ]  ; 
if  (  x  ==  0.0  &&  y  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE2_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE2_Y  ; 
}  else  { 
double   length  =  Math  .  sqrt  (  x  *  x  +  y  *  y  )  ; 
if  (  length  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE2_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE2_Y  ; 
}  else  { 
double   lengthInv  =  1.0  /  length  ; 
out  [  0  ]  =  lengthInv  *  x  ; 
out  [  1  ]  =  lengthInv  *  y  ; 
} 
} 
return   out  ; 
} 







public   static   final   Double   NORMALIZED_ZERO_TUPLE3_X  =  1.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE3_Y  =  0.0  ; 

public   static   final   Double   NORMALIZED_ZERO_TUPLE3_Z  =  0.0  ; 







public   static   double  [  ]  normalizeTuple3  (  double  [  ]  in  ,  double  [  ]  out  )  { 
double   x  =  in  [  0  ]  ; 
double   y  =  in  [  1  ]  ; 
double   z  =  in  [  2  ]  ; 
if  (  x  ==  0.0  &&  y  ==  0.0  &&  z  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE3_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE3_Y  ; 
out  [  2  ]  =  NORMALIZED_ZERO_TUPLE3_Z  ; 
}  else  { 
double   length  =  Math  .  sqrt  (  x  *  x  +  y  *  y  +  z  *  z  )  ; 
if  (  length  ==  0.0  )  { 
out  [  0  ]  =  NORMALIZED_ZERO_TUPLE3_X  ; 
out  [  1  ]  =  NORMALIZED_ZERO_TUPLE3_Y  ; 
out  [  2  ]  =  NORMALIZED_ZERO_TUPLE3_Z  ; 
}  else  { 
double   lengthInv  =  1.0  /  length  ; 
out  [  0  ]  =  lengthInv  *  x  ; 
out  [  1  ]  =  lengthInv  *  y  ; 
out  [  2  ]  =  lengthInv  *  z  ; 
} 
} 
return   out  ; 
} 






public   static   void   transpose3  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D  .  isIdentityMatrix  (  in  )  )  { 
if  (  in  !=  out  )  D  .  identityMatrix  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  copyIn  ; 
if  (  in  ==  out  )  { 
D  .  copyMatrix  (  in  ,  _dummyMatrix  )  ; 
copyIn  =  _dummyMatrix  ; 
}  else  { 
copyIn  =  in  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  3  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  3  ;  colI  ++  )  { 
out  [  colI  ]  [  rowI  ]  =  copyIn  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 






public   static   void   transpose4  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D  .  isIdentityMatrix  (  in  )  )  { 
if  (  in  !=  out  )  D  .  identityMatrix  (  out  )  ; 
return  ; 
} 
double  [  ]  [  ]  copyIn  ; 
if  (  in  ==  out  )  { 
D  .  copyMatrix  (  in  ,  _dummyMatrix  )  ; 
copyIn  =  _dummyMatrix  ; 
}  else  { 
copyIn  =  in  ; 
} 
for  (  int   rowI  =  0  ;  rowI  <  4  ;  rowI  ++  )  { 
for  (  int   colI  =  0  ;  colI  <  4  ;  colI  ++  )  { 
out  [  colI  ]  [  rowI  ]  =  copyIn  [  rowI  ]  [  colI  ]  ; 
} 
} 
} 







public   static   void   invert3  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D  .  isIdentityMatrix  (  in  )  )  { 
if  (  in  !=  out  )  D  .  identityMatrix  (  out  )  ; 
return  ; 
} 
D  .  copyMatrix  (  in  ,  _dummyMatrix  )  ; 
double  [  ]  [  ]  copyIn  =  _dummyMatrix  ; 
D  .  identityMatrix  (  out  )  ; 
for  (  int   i  =  0  ;  i  <  3  ;  i  ++  )  { 
double   alpha  =  copyIn  [  i  ]  [  i  ]  ; 
if  (  alpha  ==  0.0  )  { 
throw   new   IllegalStateException  (  "Matrix is singular.  in="  +  toStringMatrix4  (  in  )  )  ; 
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
} 







public   static   void   invert4  (  double  [  ]  [  ]  in  ,  double  [  ]  [  ]  out  )  { 
if  (  D  .  isIdentityMatrix  (  in  )  )  { 
if  (  in  !=  out  )  D  .  identityMatrix  (  out  )  ; 
return  ; 
} 
D  .  copyMatrix  (  in  ,  _dummyMatrix  )  ; 
double  [  ]  [  ]  copyIn  =  _dummyMatrix  ; 
D  .  identityMatrix  (  out  )  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
double   alpha  =  copyIn  [  i  ]  [  i  ]  ; 
if  (  alpha  ==  0.0  )  { 
throw   new   IllegalStateException  (  "Matrix is singular.  in="  +  toStringMatrix4  (  in  )  )  ; 
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
} 





public   static   String   toStringMatrix3  (  double  [  ]  [  ]  matrix  )  { 
return  "["  +  "["  +  matrix  [  0  ]  [  0  ]  +  " "  +  matrix  [  0  ]  [  1  ]  +  " "  +  matrix  [  0  ]  [  2  ]  +  "]"  +  "["  +  matrix  [  1  ]  [  0  ]  +  " "  +  matrix  [  1  ]  [  1  ]  +  " "  +  matrix  [  1  ]  [  2  ]  +  "]"  +  "["  +  matrix  [  2  ]  [  0  ]  +  " "  +  matrix  [  2  ]  [  1  ]  +  " "  +  matrix  [  2  ]  [  2  ]  +  "]"  +  "]"  ; 
} 





public   static   String   toStringMatrix4  (  double  [  ]  [  ]  matrix  )  { 
return  "["  +  "["  +  matrix  [  0  ]  [  0  ]  +  " "  +  matrix  [  0  ]  [  1  ]  +  " "  +  matrix  [  0  ]  [  2  ]  +  " "  +  matrix  [  0  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  1  ]  [  0  ]  +  " "  +  matrix  [  1  ]  [  1  ]  +  " "  +  matrix  [  1  ]  [  2  ]  +  " "  +  matrix  [  1  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  2  ]  [  0  ]  +  " "  +  matrix  [  2  ]  [  1  ]  +  " "  +  matrix  [  2  ]  [  2  ]  +  " "  +  matrix  [  2  ]  [  3  ]  +  "]"  +  "["  +  matrix  [  3  ]  [  0  ]  +  " "  +  matrix  [  3  ]  [  1  ]  +  " "  +  matrix  [  3  ]  [  2  ]  +  " "  +  matrix  [  3  ]  [  3  ]  +  "]"  +  "]"  ; 
} 

private   static   double  [  ]  [  ]  _dummyMatrix  =  new   double  [  4  ]  [  4  ]  ; 
} 
} 

