package   java  .  awt  .  geom  ; 

import   java  .  awt  .  Rectangle  ; 
import   java  .  awt  .  Shape  ; 
import   java  .  util  .  NoSuchElementException  ; 















public   abstract   class   QuadCurve2D   implements   Shape  ,  Cloneable  { 

private   static   final   double   BIG_VALUE  =  java  .  lang  .  Double  .  MAX_VALUE  /  10.0  ; 

private   static   final   double   EPSILON  =  1E-10  ; 






protected   QuadCurve2D  (  )  { 
} 





public   abstract   double   getX1  (  )  ; 





public   abstract   double   getY1  (  )  ; 




public   abstract   Point2D   getP1  (  )  ; 





public   abstract   double   getCtrlX  (  )  ; 





public   abstract   double   getCtrlY  (  )  ; 




public   abstract   Point2D   getCtrlPt  (  )  ; 





public   abstract   double   getX2  (  )  ; 





public   abstract   double   getY2  (  )  ; 




public   abstract   Point2D   getP2  (  )  ; 























public   abstract   void   setCurve  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  ; 


















public   void   setCurve  (  double  [  ]  coords  ,  int   offset  )  { 
setCurve  (  coords  [  offset  ++  ]  ,  coords  [  offset  ++  ]  ,  coords  [  offset  ++  ]  ,  coords  [  offset  ++  ]  ,  coords  [  offset  ++  ]  ,  coords  [  offset  ++  ]  )  ; 
} 

















public   void   setCurve  (  Point2D   p1  ,  Point2D   c  ,  Point2D   p2  )  { 
setCurve  (  p1  .  getX  (  )  ,  p1  .  getY  (  )  ,  c  .  getX  (  )  ,  c  .  getY  (  )  ,  p2  .  getX  (  )  ,  p2  .  getY  (  )  )  ; 
} 



















public   void   setCurve  (  Point2D  [  ]  pts  ,  int   offset  )  { 
setCurve  (  pts  [  offset  ]  .  getX  (  )  ,  pts  [  offset  ]  .  getY  (  )  ,  pts  [  offset  +  1  ]  .  getX  (  )  ,  pts  [  offset  +  1  ]  .  getY  (  )  ,  pts  [  offset  +  2  ]  .  getX  (  )  ,  pts  [  offset  +  2  ]  .  getY  (  )  )  ; 
} 






public   void   setCurve  (  QuadCurve2D   c  )  { 
setCurve  (  c  .  getX1  (  )  ,  c  .  getY1  (  )  ,  c  .  getCtrlX  (  )  ,  c  .  getCtrlY  (  )  ,  c  .  getX2  (  )  ,  c  .  getY2  (  )  )  ; 
} 





















public   static   double   getFlatnessSq  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  { 
return   Line2D  .  ptSegDistSq  (  x1  ,  y1  ,  x2  ,  y2  ,  cx  ,  cy  )  ; 
} 





















public   static   double   getFlatness  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  { 
return   Line2D  .  ptSegDist  (  x1  ,  y1  ,  x2  ,  y2  ,  cx  ,  cy  )  ; 
} 



























public   static   double   getFlatnessSq  (  double  [  ]  coords  ,  int   offset  )  { 
return   Line2D  .  ptSegDistSq  (  coords  [  offset  ]  ,  coords  [  offset  +  1  ]  ,  coords  [  offset  +  4  ]  ,  coords  [  offset  +  5  ]  ,  coords  [  offset  +  2  ]  ,  coords  [  offset  +  3  ]  )  ; 
} 



























public   static   double   getFlatness  (  double  [  ]  coords  ,  int   offset  )  { 
return   Line2D  .  ptSegDist  (  coords  [  offset  ]  ,  coords  [  offset  +  1  ]  ,  coords  [  offset  +  4  ]  ,  coords  [  offset  +  5  ]  ,  coords  [  offset  +  2  ]  ,  coords  [  offset  +  3  ]  )  ; 
} 














public   double   getFlatnessSq  (  )  { 
return   Line2D  .  ptSegDistSq  (  getX1  (  )  ,  getY1  (  )  ,  getX2  (  )  ,  getY2  (  )  ,  getCtrlX  (  )  ,  getCtrlY  (  )  )  ; 
} 














public   double   getFlatness  (  )  { 
return   Line2D  .  ptSegDist  (  getX1  (  )  ,  getY1  (  )  ,  getX2  (  )  ,  getY2  (  )  ,  getCtrlX  (  )  ,  getCtrlY  (  )  )  ; 
} 
















public   void   subdivide  (  QuadCurve2D   left  ,  QuadCurve2D   right  )  { 
double  [  ]  d  =  new   double  [  ]  {  getX1  (  )  ,  getY1  (  )  ,  getCtrlX  (  )  ,  getCtrlY  (  )  ,  getX2  (  )  ,  getY2  (  )  ,  0  ,  0  ,  0  ,  0  }  ; 
subdivide  (  d  ,  0  ,  d  ,  0  ,  d  ,  4  )  ; 
if  (  left  !=  null  )  left  .  setCurve  (  d  ,  0  )  ; 
if  (  right  !=  null  )  right  .  setCurve  (  d  ,  4  )  ; 
} 


















public   static   void   subdivide  (  QuadCurve2D   src  ,  QuadCurve2D   left  ,  QuadCurve2D   right  )  { 
src  .  subdivide  (  left  ,  right  )  ; 
} 












































public   static   void   subdivide  (  double  [  ]  src  ,  int   srcOff  ,  double  [  ]  left  ,  int   leftOff  ,  double  [  ]  right  ,  int   rightOff  )  { 
double   x1  ; 
double   y1  ; 
double   xc  ; 
double   yc  ; 
double   x2  ; 
double   y2  ; 
x1  =  src  [  srcOff  ]  ; 
y1  =  src  [  srcOff  +  1  ]  ; 
xc  =  src  [  srcOff  +  2  ]  ; 
yc  =  src  [  srcOff  +  3  ]  ; 
x2  =  src  [  srcOff  +  4  ]  ; 
y2  =  src  [  srcOff  +  5  ]  ; 
if  (  left  !=  null  )  { 
left  [  leftOff  ]  =  x1  ; 
left  [  leftOff  +  1  ]  =  y1  ; 
} 
if  (  right  !=  null  )  { 
right  [  rightOff  +  4  ]  =  x2  ; 
right  [  rightOff  +  5  ]  =  y2  ; 
} 
x1  =  (  x1  +  xc  )  /  2  ; 
x2  =  (  xc  +  x2  )  /  2  ; 
xc  =  (  x1  +  x2  )  /  2  ; 
y1  =  (  y1  +  yc  )  /  2  ; 
y2  =  (  y2  +  yc  )  /  2  ; 
yc  =  (  y1  +  y2  )  /  2  ; 
if  (  left  !=  null  )  { 
left  [  leftOff  +  2  ]  =  x1  ; 
left  [  leftOff  +  3  ]  =  y1  ; 
left  [  leftOff  +  4  ]  =  xc  ; 
left  [  leftOff  +  5  ]  =  yc  ; 
} 
if  (  right  !=  null  )  { 
right  [  rightOff  ]  =  xc  ; 
right  [  rightOff  +  1  ]  =  yc  ; 
right  [  rightOff  +  2  ]  =  x2  ; 
right  [  rightOff  +  3  ]  =  y2  ; 
} 
} 








































public   static   int   solveQuadratic  (  double  [  ]  eqn  )  { 
return   solveQuadratic  (  eqn  ,  eqn  )  ; 
} 









































public   static   int   solveQuadratic  (  double  [  ]  eqn  ,  double  [  ]  res  )  { 
double   a  ; 
double   b  ; 
double   c  ; 
double   disc  ; 
c  =  eqn  [  0  ]  ; 
b  =  eqn  [  1  ]  ; 
a  =  eqn  [  2  ]  ; 
if  (  a  ==  0  )  { 
if  (  b  ==  0  )  return  -  1  ; 
res  [  0  ]  =  -  c  /  b  ; 
return   1  ; 
} 
disc  =  b  *  b  -  4  *  a  *  c  ; 
if  (  disc  <  0  )  return   0  ; 
if  (  disc  ==  0  )  { 
res  [  0  ]  =  -  0.5  *  b  /  a  ; 
return   1  ; 
} 
if  (  b  ==  0  )  { 
double   r  ; 
r  =  Math  .  abs  (  0.5  *  Math  .  sqrt  (  disc  )  /  a  )  ; 
res  [  0  ]  =  -  r  ; 
res  [  1  ]  =  r  ; 
}  else  { 
double   sgnb  ; 
double   temp  ; 
sgnb  =  (  b  >  0  ?  1  :  -  1  )  ; 
temp  =  -  0.5  *  (  b  +  sgnb  *  Math  .  sqrt  (  disc  )  )  ; 
res  [  0  ]  =  temp  /  a  ; 
res  [  1  ]  =  c  /  temp  ; 
} 
return   2  ; 
} 











public   boolean   contains  (  double   x  ,  double   y  )  { 
if  (  !  getBounds2D  (  )  .  contains  (  x  ,  y  )  )  return   false  ; 
return  (  (  getAxisIntersections  (  x  ,  y  ,  true  ,  BIG_VALUE  )  &  1  )  !=  0  )  ; 
} 











public   boolean   contains  (  Point2D   p  )  { 
return   contains  (  p  .  getX  (  )  ,  p  .  getY  (  )  )  ; 
} 











public   boolean   intersects  (  double   x  ,  double   y  ,  double   w  ,  double   h  )  { 
if  (  !  getBounds2D  (  )  .  contains  (  x  ,  y  ,  w  ,  h  )  )  return   false  ; 
if  (  getAxisIntersections  (  x  ,  y  ,  true  ,  w  )  !=  0  ||  getAxisIntersections  (  x  ,  y  +  h  ,  true  ,  w  )  !=  0  ||  getAxisIntersections  (  x  +  w  ,  y  ,  false  ,  h  )  !=  0  ||  getAxisIntersections  (  x  ,  y  ,  false  ,  h  )  !=  0  )  return   true  ; 
if  (  (  getAxisIntersections  (  x  ,  y  ,  true  ,  BIG_VALUE  )  &  1  )  !=  0  )  return   true  ; 
return   false  ; 
} 






public   boolean   intersects  (  Rectangle2D   r  )  { 
return   intersects  (  r  .  getX  (  )  ,  r  .  getY  (  )  ,  r  .  getWidth  (  )  ,  r  .  getHeight  (  )  )  ; 
} 












public   boolean   contains  (  double   x  ,  double   y  ,  double   w  ,  double   h  )  { 
if  (  !  getBounds2D  (  )  .  intersects  (  x  ,  y  ,  w  ,  h  )  )  return   false  ; 
if  (  getAxisIntersections  (  x  ,  y  ,  true  ,  w  )  !=  0  ||  getAxisIntersections  (  x  ,  y  +  h  ,  true  ,  w  )  !=  0  ||  getAxisIntersections  (  x  +  w  ,  y  ,  false  ,  h  )  !=  0  ||  getAxisIntersections  (  x  ,  y  ,  false  ,  h  )  !=  0  )  return   false  ; 
if  (  (  getAxisIntersections  (  x  ,  y  ,  true  ,  BIG_VALUE  )  &  1  )  !=  0  )  return   true  ; 
return   false  ; 
} 






public   boolean   contains  (  Rectangle2D   r  )  { 
return   contains  (  r  .  getX  (  )  ,  r  .  getY  (  )  ,  r  .  getWidth  (  )  ,  r  .  getHeight  (  )  )  ; 
} 











public   Rectangle   getBounds  (  )  { 
return   getBounds2D  (  )  .  getBounds  (  )  ; 
} 

public   PathIterator   getPathIterator  (  final   AffineTransform   at  )  { 
return   new   PathIterator  (  )  { 


private   int   current  =  0  ; 

public   int   getWindingRule  (  )  { 
return   WIND_NON_ZERO  ; 
} 

public   boolean   isDone  (  )  { 
return   current  >=  2  ; 
} 

public   void   next  (  )  { 
current  ++  ; 
} 

public   int   currentSegment  (  float  [  ]  coords  )  { 
int   result  ; 
switch  (  current  )  { 
case   0  : 
coords  [  0  ]  =  (  float  )  getX1  (  )  ; 
coords  [  1  ]  =  (  float  )  getY1  (  )  ; 
result  =  SEG_MOVETO  ; 
break  ; 
case   1  : 
coords  [  0  ]  =  (  float  )  getCtrlX  (  )  ; 
coords  [  1  ]  =  (  float  )  getCtrlY  (  )  ; 
coords  [  2  ]  =  (  float  )  getX2  (  )  ; 
coords  [  3  ]  =  (  float  )  getY2  (  )  ; 
result  =  SEG_QUADTO  ; 
break  ; 
default  : 
throw   new   NoSuchElementException  (  "quad iterator out of bounds"  )  ; 
} 
if  (  at  !=  null  )  at  .  transform  (  coords  ,  0  ,  coords  ,  0  ,  2  )  ; 
return   result  ; 
} 

public   int   currentSegment  (  double  [  ]  coords  )  { 
int   result  ; 
switch  (  current  )  { 
case   0  : 
coords  [  0  ]  =  getX1  (  )  ; 
coords  [  1  ]  =  getY1  (  )  ; 
result  =  SEG_MOVETO  ; 
break  ; 
case   1  : 
coords  [  0  ]  =  getCtrlX  (  )  ; 
coords  [  1  ]  =  getCtrlY  (  )  ; 
coords  [  2  ]  =  getX2  (  )  ; 
coords  [  3  ]  =  getY2  (  )  ; 
result  =  SEG_QUADTO  ; 
break  ; 
default  : 
throw   new   NoSuchElementException  (  "quad iterator out of bounds"  )  ; 
} 
if  (  at  !=  null  )  at  .  transform  (  coords  ,  0  ,  coords  ,  0  ,  2  )  ; 
return   result  ; 
} 
}  ; 
} 

public   PathIterator   getPathIterator  (  AffineTransform   at  ,  double   flatness  )  { 
return   new   FlatteningPathIterator  (  getPathIterator  (  at  )  ,  flatness  )  ; 
} 






public   Object   clone  (  )  { 
try  { 
return   super  .  clone  (  )  ; 
}  catch  (  CloneNotSupportedException   e  )  { 
throw  (  Error  )  new   InternalError  (  )  .  initCause  (  e  )  ; 
} 
} 














private   int   getAxisIntersections  (  double   x  ,  double   y  ,  boolean   useYaxis  ,  double   distance  )  { 
int   nCrossings  =  0  ; 
double   a0  ; 
double   a1  ; 
double   a2  ; 
double   b0  ; 
double   b1  ; 
double   b2  ; 
double  [  ]  r  =  new   double  [  3  ]  ; 
int   nRoots  ; 
a0  =  a2  =  0.0  ; 
if  (  useYaxis  )  { 
a0  =  getY1  (  )  -  y  ; 
a1  =  getCtrlY  (  )  -  y  ; 
a2  =  getY2  (  )  -  y  ; 
b0  =  getX1  (  )  -  x  ; 
b1  =  getCtrlX  (  )  -  x  ; 
b2  =  getX2  (  )  -  x  ; 
}  else  { 
a0  =  getX1  (  )  -  x  ; 
a1  =  getCtrlX  (  )  -  x  ; 
a2  =  getX2  (  )  -  x  ; 
b0  =  getY1  (  )  -  y  ; 
b1  =  getCtrlY  (  )  -  y  ; 
b2  =  getY2  (  )  -  y  ; 
} 
if  (  a0  ==  0.0  ||  a2  ==  0.0  )  { 
double   small  =  getFlatness  (  )  *  EPSILON  ; 
if  (  a0  ==  0.0  )  a0  -=  small  ; 
if  (  a2  ==  0.0  )  a2  -=  small  ; 
} 
r  [  0  ]  =  a0  ; 
r  [  1  ]  =  2  *  (  a1  -  a0  )  ; 
r  [  2  ]  =  (  a2  -  2  *  a1  +  a0  )  ; 
nRoots  =  solveQuadratic  (  r  )  ; 
for  (  int   i  =  0  ;  i  <  nRoots  ;  i  ++  )  { 
double   t  =  r  [  i  ]  ; 
if  (  t  >=  0.0  &&  t  <=  1.0  )  { 
double   crossing  =  t  *  t  *  (  b2  -  2  *  b1  +  b0  )  +  2  *  t  *  (  b1  -  b0  )  +  b0  ; 
if  (  crossing  >  0  &&  crossing  <  distance  )  nCrossings  +=  (  nRoots  ==  1  )  ?  2  :  1  ; 
} 
} 
if  (  useYaxis  )  { 
if  (  Line2D  .  linesIntersect  (  b0  ,  a0  ,  b2  ,  a2  ,  EPSILON  ,  0.0  ,  distance  ,  0.0  )  )  nCrossings  ++  ; 
}  else  { 
if  (  Line2D  .  linesIntersect  (  a0  ,  b0  ,  a2  ,  b2  ,  0.0  ,  EPSILON  ,  0.0  ,  distance  )  )  nCrossings  ++  ; 
} 
return  (  nCrossings  )  ; 
} 











public   static   class   Double   extends   QuadCurve2D  { 




public   double   x1  ; 




public   double   y1  ; 




public   double   ctrlx  ; 




public   double   ctrly  ; 




public   double   x2  ; 




public   double   y2  ; 






public   Double  (  )  { 
} 
























public   Double  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  { 
this  .  x1  =  x1  ; 
this  .  y1  =  y1  ; 
ctrlx  =  cx  ; 
ctrly  =  cy  ; 
this  .  x2  =  x2  ; 
this  .  y2  =  y2  ; 
} 





public   double   getX1  (  )  { 
return   x1  ; 
} 





public   double   getY1  (  )  { 
return   y1  ; 
} 




public   Point2D   getP1  (  )  { 
return   new   Point2D  .  Double  (  x1  ,  y1  )  ; 
} 





public   double   getCtrlX  (  )  { 
return   ctrlx  ; 
} 





public   double   getCtrlY  (  )  { 
return   ctrly  ; 
} 




public   Point2D   getCtrlPt  (  )  { 
return   new   Point2D  .  Double  (  ctrlx  ,  ctrly  )  ; 
} 





public   double   getX2  (  )  { 
return   x2  ; 
} 





public   double   getY2  (  )  { 
return   y2  ; 
} 




public   Point2D   getP2  (  )  { 
return   new   Point2D  .  Double  (  x2  ,  y2  )  ; 
} 






















public   void   setCurve  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  { 
this  .  x1  =  x1  ; 
this  .  y1  =  y1  ; 
ctrlx  =  cx  ; 
ctrly  =  cy  ; 
this  .  x2  =  x2  ; 
this  .  y2  =  y2  ; 
} 











public   Rectangle2D   getBounds2D  (  )  { 
double   nx1  =  Math  .  min  (  Math  .  min  (  x1  ,  ctrlx  )  ,  x2  )  ; 
double   ny1  =  Math  .  min  (  Math  .  min  (  y1  ,  ctrly  )  ,  y2  )  ; 
double   nx2  =  Math  .  max  (  Math  .  max  (  x1  ,  ctrlx  )  ,  x2  )  ; 
double   ny2  =  Math  .  max  (  Math  .  max  (  y1  ,  ctrly  )  ,  y2  )  ; 
return   new   Rectangle2D  .  Double  (  nx1  ,  ny1  ,  nx2  -  nx1  ,  ny2  -  ny1  )  ; 
} 
} 











public   static   class   Float   extends   QuadCurve2D  { 




public   float   x1  ; 




public   float   y1  ; 




public   float   ctrlx  ; 




public   float   ctrly  ; 




public   float   x2  ; 




public   float   y2  ; 






public   Float  (  )  { 
} 
























public   Float  (  float   x1  ,  float   y1  ,  float   cx  ,  float   cy  ,  float   x2  ,  float   y2  )  { 
this  .  x1  =  x1  ; 
this  .  y1  =  y1  ; 
ctrlx  =  cx  ; 
ctrly  =  cy  ; 
this  .  x2  =  x2  ; 
this  .  y2  =  y2  ; 
} 





public   double   getX1  (  )  { 
return   x1  ; 
} 





public   double   getY1  (  )  { 
return   y1  ; 
} 




public   Point2D   getP1  (  )  { 
return   new   Point2D  .  Float  (  x1  ,  y1  )  ; 
} 





public   double   getCtrlX  (  )  { 
return   ctrlx  ; 
} 





public   double   getCtrlY  (  )  { 
return   ctrly  ; 
} 




public   Point2D   getCtrlPt  (  )  { 
return   new   Point2D  .  Float  (  ctrlx  ,  ctrly  )  ; 
} 





public   double   getX2  (  )  { 
return   x2  ; 
} 





public   double   getY2  (  )  { 
return   y2  ; 
} 




public   Point2D   getP2  (  )  { 
return   new   Point2D  .  Float  (  x2  ,  y2  )  ; 
} 























public   void   setCurve  (  double   x1  ,  double   y1  ,  double   cx  ,  double   cy  ,  double   x2  ,  double   y2  )  { 
this  .  x1  =  (  float  )  x1  ; 
this  .  y1  =  (  float  )  y1  ; 
ctrlx  =  (  float  )  cx  ; 
ctrly  =  (  float  )  cy  ; 
this  .  x2  =  (  float  )  x2  ; 
this  .  y2  =  (  float  )  y2  ; 
} 























public   void   setCurve  (  float   x1  ,  float   y1  ,  float   cx  ,  float   cy  ,  float   x2  ,  float   y2  )  { 
this  .  x1  =  x1  ; 
this  .  y1  =  y1  ; 
ctrlx  =  cx  ; 
ctrly  =  cy  ; 
this  .  x2  =  x2  ; 
this  .  y2  =  y2  ; 
} 











public   Rectangle2D   getBounds2D  (  )  { 
float   nx1  =  (  float  )  Math  .  min  (  Math  .  min  (  x1  ,  ctrlx  )  ,  x2  )  ; 
float   ny1  =  (  float  )  Math  .  min  (  Math  .  min  (  y1  ,  ctrly  )  ,  y2  )  ; 
float   nx2  =  (  float  )  Math  .  max  (  Math  .  max  (  x1  ,  ctrlx  )  ,  x2  )  ; 
float   ny2  =  (  float  )  Math  .  max  (  Math  .  max  (  y1  ,  ctrly  )  ,  y2  )  ; 
return   new   Rectangle2D  .  Float  (  nx1  ,  ny1  ,  nx2  -  nx1  ,  ny2  -  ny1  )  ; 
} 
} 
} 

