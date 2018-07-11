package   ch  .  unibe  .  inkml  ; 

import   java  .  awt  .  Graphics2D  ; 
import   java  .  awt  .  Polygon  ; 
import   java  .  awt  .  geom  .  Point2D  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Scanner  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   ch  .  unibe  .  eindermu  .  Messenger  ; 
import   ch  .  unibe  .  eindermu  .  utils  .  Aspect  ; 
import   ch  .  unibe  .  eindermu  .  utils  .  NotImplementedException  ; 
import   ch  .  unibe  .  eindermu  .  utils  .  Observer  ; 
import   ch  .  unibe  .  inkml  .  InkChannel  .  ChannelName  ; 
import   ch  .  unibe  .  inkml  .  util  .  Formatter  ; 
import   ch  .  unibe  .  inkml  .  util  .  Timespan  ; 
import   ch  .  unibe  .  inkml  .  util  .  TraceBound  ; 
import   ch  .  unibe  .  inkml  .  util  .  TraceVisitor  ; 

public   class   InkTraceLeaf   extends   InkTrace  { 

public   static   final   String   INKML_NAME  =  "trace"  ; 

public   static   final   String   INKML_ATTR_TYPE  =  "type"  ; 

public   static   final   String   INKML_ATTR_TYPE_VALUE_PENDOWN  =  "penDown"  ; 

public   static   final   String   INKML_ATTR_TYPE_VALUE_PENUP  =  "penUp"  ; 

public   static   final   String   INKML_ATTR_TYPE_VALUE_INDETERMINATE  =  "indeterminate"  ; 

public   static   final   String   INKML_ATTR_CONTINUATION  =  "continuation"  ; 

public   static   final   String   INKML_ATTR_CONTINUATION_VALUE_BEGIN  =  "begin"  ; 

public   static   final   String   INKML_ATTR_CONTINUATION_VALUE_END  =  "end"  ; 

public   static   final   String   INKML_ATTR_CONTINUATION_VALUE_MIDDLE  =  "middle"  ; 

public   static   final   String   INKML_ATTR_PRIORREF  =  "priorRef"  ; 

public   static   final   String   INKML_ATTR_BRUSHREF  =  "brushRef"  ; 

public   static   final   String   INKML_ATTR_DURATION  =  "duration"  ; 

public   static   final   String   INKML_ATTR_TIMEOFFSET  =  "timeOffset"  ; 

public   static   final   String   ID_PREFIX  =  "t"  ; 




private   double  [  ]  [  ]  sourcePoints  ; 




private   double  [  ]  [  ]  points  ; 

private   int   size  =  0  ; 

public   enum   Type  { 

PEN_DOWN  ,  PEN_UP  ,  INDETERMINATE  ; 

public   String   toString  (  )  { 
switch  (  this  )  { 
case   PEN_DOWN  : 
return   INKML_ATTR_TYPE_VALUE_PENDOWN  ; 
case   PEN_UP  : 
return   INKML_ATTR_TYPE_VALUE_PENUP  ; 
case   INDETERMINATE  : 
return   INKML_ATTR_TYPE_VALUE_INDETERMINATE  ; 
default  : 
return   super  .  toString  (  )  ; 
} 
} 

public   static   Type   getValue  (  String   name  )  { 
for  (  Type   t  :  Type  .  values  (  )  )  { 
if  (  t  .  toString  (  )  .  equalsIgnoreCase  (  name  )  )  { 
return   t  ; 
} 
} 
return   null  ; 
} 
} 

; 













private   Type   type  ; 

public   enum   Continuation  { 

BEGIN  ,  END  ,  MIDDLE  ; 

public   String   toString  (  )  { 
switch  (  this  )  { 
case   BEGIN  : 
return   INKML_ATTR_CONTINUATION_VALUE_BEGIN  ; 
case   END  : 
return   INKML_ATTR_CONTINUATION_VALUE_END  ; 
case   MIDDLE  : 
return   INKML_ATTR_CONTINUATION_VALUE_MIDDLE  ; 
} 
return   null  ; 
} 

public   static   Continuation   getValue  (  String   name  )  { 
for  (  Continuation   t  :  Continuation  .  values  (  )  )  { 
if  (  t  .  toString  (  )  .  equalsIgnoreCase  (  name  )  )  { 
return   t  ; 
} 
} 
return   null  ; 
} 
} 

; 





















private   Continuation   continuation  ; 

private   String   priorRef  ; 

private   String   brushRef  ; 

private   Double   duration  ; 

private   Double   timeOffset  ; 




private   Timespan   cacheTimespan  ; 




private   TraceBound   cacheBound  ; 




private   Point2D   cacheCenterOfGravity  =  new   Point2D  .  Double  (  )  ; 




private   Map  <  ChannelName  ,  Integer  >  cacheSourceIndex  ; 

private   boolean   tainted  =  false  ; 

private   InkTraceFormat   targetFormat  ; 

public   class   ProxyInkTracePoint   extends   InkTracePoint  { 

private   int   i  =  0  ; 

public   ProxyInkTracePoint  (  int   index  )  { 
i  =  index  ; 
} 






public   void   set  (  ChannelName   channel  ,  Object   value  )  { 
set  (  channel  ,  doubleize  (  channel  ,  value  )  )  ; 
} 






public   void   set  (  ChannelName   name  ,  double   d  )  { 
points  [  i  ]  [  getIndex  (  name  )  ]  =  d  ; 
taint  (  )  ; 
notifyObserver  (  InkInk  .  ON_CHANGE  )  ; 
} 

public   Object   getObject  (  ChannelName   name  )  { 
return   objectify  (  name  ,  get  (  name  )  )  ; 
} 

public   double   get  (  ChannelName   t  )  { 
return   points  [  i  ]  [  getIndex  (  t  )  ]  ; 
} 

public   int   index  (  )  { 
return   i  ; 
} 
} 

public   InkTraceLeaf  (  InkInk   ink  ,  InkTraceGroup   parent  )  { 
super  (  ink  ,  parent  )  ; 
cacheSourceIndex  =  getSourceFormat  (  )  .  getIndex  (  )  ; 
} 

protected   void   initialize  (  )  { 
super  .  initialize  (  )  ; 
registerFor  (  ON_CHANGE  ,  new   Observer  (  )  { 

@  Override 
public   void   notifyFor  (  Aspect   event  ,  Object   subject  )  { 
renewCache  (  )  ; 
if  (  isRoot  (  )  )  { 
getInk  (  )  .  notifyObserver  (  InkInk  .  ON_CHANGE  ,  subject  )  ; 
}  else  { 
getParent  (  )  .  notifyObserver  (  InkTrace  .  ON_CHANGE  ,  subject  )  ; 
} 
notifyObserver  (  InkTraceView  .  ON_DATA_CHANGE  ,  subject  )  ; 
} 
}  )  ; 
} 

private   void   taint  (  )  { 
tainted  =  true  ; 
} 

private   void   renewCache  (  )  { 
cacheCenterOfGravity  =  InkTracePoint  .  getCenterOfGravity  (  this  )  ; 
if  (  getPointCount  (  )  >  0  )  { 
if  (  !  getTargetFormat  (  )  .  containsChannel  (  ChannelName  .  T  )  )  { 
Messenger  .  error  (  "point has no time coordinates can not deliver timeSpan"  )  ; 
} 
int   t  =  getIndex  (  ChannelName  .  T  )  ; 
cacheTimespan  =  new   Timespan  (  points  [  0  ]  [  t  ]  ,  points  [  size  -  1  ]  [  t  ]  )  ; 
} 
cacheBound  =  new   TraceBound  (  getPoint  (  0  )  )  ; 
for  (  InkTracePoint   p  :  this  )  { 
cacheBound  .  add  (  p  )  ; 
} 
} 

public   TraceBound   getBounds  (  )  { 
return   cacheBound  ; 
} 





public   Point2D   getCenterOfGravity  (  )  { 
return   cacheCenterOfGravity  ; 
} 

public   Timespan   getTimeSpan  (  )  { 
return   cacheTimespan  ; 
} 

public   void   backTransformPoints  (  )  throws   InkMLComplianceException  { 
getCanvasTransform  (  )  .  backTransform  (  points  ,  sourcePoints  ,  getTargetFormat  (  )  ,  getSourceFormat  (  )  )  ; 
} 

public   InkCanvasTransform   getCanvasTransform  (  )  { 
return   this  .  getContext  (  )  .  getCanvasTransform  (  )  ; 
} 





public   InkBrush   getBrush  (  )  { 
if  (  this  .  brushRef  !=  null  )  { 
return  (  InkBrush  )  this  .  getInk  (  )  .  getDefinitions  (  )  .  get  (  this  .  brushRef  )  ; 
}  else   if  (  this  .  hasLocalContext  (  )  &&  this  .  getLocalContext  (  )  .  getBrush  (  )  !=  null  )  { 
return   this  .  getLocalContext  (  )  .  getBrush  (  )  ; 
}  else   if  (  !  this  .  isRoot  (  )  )  { 
return   this  .  getParent  (  )  .  getBrush  (  )  ; 
}  else  { 
return   this  .  getContext  (  )  .  getBrush  (  )  ; 
} 
} 






public   InkTraceFormat   getSourceFormat  (  )  { 
return   this  .  getContext  (  )  .  getSourceFormat  (  )  ; 
} 





private   InkTraceFormat   getTargetFormat  (  )  { 
if  (  targetFormat  ==  null  )  { 
targetFormat  =  getCanvasFormat  (  )  ; 
} 
return   targetFormat  ; 
} 






public   InkTraceFormat   getCanvasFormat  (  )  { 
return   this  .  getContext  (  )  .  getCanvasTraceFormat  (  )  ; 
} 

public   List  <  InkTracePoint  >  getPoints  (  )  { 
List  <  InkTracePoint  >  l  =  new   LinkedList  <  InkTracePoint  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  getPointCount  (  )  ;  i  ++  )  { 
l  .  add  (  getPoint  (  i  )  )  ; 
} 
return   l  ; 
} 

public   Iterable  <  InkTracePoint  >  pointIterable  (  )  { 
return   this  ; 
} 

public   Iterator  <  InkTracePoint  >  iterator  (  )  { 
return   new   Iterator  <  InkTracePoint  >  (  )  { 

private   int   pos  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pos  <  getPointCount  (  )  ; 
} 

public   InkTracePoint   next  (  )  { 
return   getPoint  (  pos  ++  )  ; 
} 

public   void   remove  (  )  { 
throw   new   NotImplementedException  (  )  ; 
} 
}  ; 
} 






protected   int   getIndex  (  ChannelName   name  )  { 
return   getTargetFormat  (  )  .  indexOf  (  name  )  ; 
} 








protected   Object   objectify  (  ChannelName   name  ,  double   d  )  { 
return   getTargetFormat  (  )  .  objectify  (  name  ,  d  )  ; 
} 







protected   double   doubleize  (  ChannelName   name  ,  Object   o  )  { 
return   getTargetFormat  (  )  .  doubleize  (  name  ,  o  )  ; 
} 

@  Override 
public   List  <  InkTracePoint  >  getPoints  (  String   from  ,  String   to  )  { 
int   f  =  Integer  .  parseInt  (  from  )  -  1  ; 
int   t  =  (  to  !=  null  )  ?  Integer  .  parseInt  (  to  )  :  getPointCount  (  )  ; 
return   getPoints  (  )  .  subList  (  f  ,  t  )  ; 
} 

public   int   getPointCount  (  )  { 
return   this  .  size  ; 
} 

public   InkTracePoint   getPoint  (  final   int   pos  )  { 
return   new   ProxyInkTracePoint  (  pos  )  ; 
} 

public   void   drawPolyLine  (  Graphics2D   g  )  { 
Polygon   p  =  getPolygon  (  )  ; 
g  .  drawPolyline  (  p  .  xpoints  ,  p  .  ypoints  ,  p  .  npoints  )  ; 
} 

public   boolean   isLeaf  (  )  { 
return   true  ; 
} 





public   void   buildFromXMLNode  (  Element   node  )  throws   InkMLComplianceException  { 
super  .  buildFromXMLNode  (  node  )  ; 
if  (  node  .  hasAttribute  (  INKML_ATTR_TYPE  )  )  { 
this  .  type  =  Type  .  getValue  (  loadAttribute  (  node  ,  INKML_ATTR_TYPE  ,  null  )  )  ; 
} 
if  (  node  .  hasAttribute  (  INKML_ATTR_CONTINUATION  )  )  { 
this  .  continuation  =  Continuation  .  getValue  (  node  .  getAttribute  (  INKML_ATTR_CONTINUATION  )  )  ; 
} 
if  (  this  .  continuation  ==  Continuation  .  END  ||  this  .  continuation  ==  Continuation  .  MIDDLE  )  { 
this  .  priorRef  =  loadAttribute  (  node  ,  INKML_ATTR_PRIORREF  ,  null  )  ; 
} 
this  .  brushRef  =  loadAttribute  (  node  ,  INKML_ATTR_BRUSHREF  ,  null  )  ; 
if  (  node  .  hasAttribute  (  INKML_ATTR_DURATION  )  )  { 
this  .  duration  =  Double  .  parseDouble  (  node  .  getAttribute  (  INKML_ATTR_DURATION  )  )  ; 
} 
if  (  node  .  hasAttribute  (  INKML_ATTR_TIMEOFFSET  )  )  { 
this  .  duration  =  Double  .  parseDouble  (  node  .  getAttribute  (  INKML_ATTR_TIMEOFFSET  )  )  ; 
} 
final   List  <  Formatter  >  formatter  =  new   ArrayList  <  Formatter  >  (  )  ; 
for  (  InkChannel   c  :  this  .  getSourceFormat  (  )  )  { 
formatter  .  add  (  c  .  formatterFactory  (  )  )  ; 
} 
final   String   input  =  node  .  getTextContent  (  )  .  trim  (  )  ; 
int   total  =  0  ; 
char  [  ]  chars  =  input  .  toCharArray  (  )  ; 
boolean   onemore  =  false  ; 
for  (  int   i  =  0  ;  i  <  chars  .  length  ;  i  ++  )  { 
switch  (  chars  [  i  ]  )  { 
case  ','  : 
total  ++  ; 
onemore  =  false  ; 
break  ; 
case  ' '  : 
case  '\n'  : 
case  '\t'  : 
case  '\r'  : 
break  ; 
default  : 
onemore  =  true  ; 
} 
} 
if  (  onemore  )  total  ++  ; 
addPoints  (  new   PointConstructionBlock  (  total  )  { 

public   void   addPoints  (  )  throws   InkMLComplianceException  { 
Scanner   stringScanner  =  new   Scanner  (  input  )  ; 
Pattern   pattern  =  Pattern  .  compile  (  ",|F|T|\\*|\\?|[\"'!]?-?(\\.[0-9]+|[0-9]+\\.[0-9]+|[0-9]+)"  )  ; 
int   i  =  0  ; 
while  (  true  )  { 
String   result  =  stringScanner  .  findWithinHorizon  (  pattern  ,  input  .  length  (  )  )  ; 
if  (  result  ==  null  )  { 
break  ; 
} 
if  (  result  .  equals  (  ","  )  ||  i  >=  formatter  .  size  (  )  )  { 
next  (  )  ; 
i  =  0  ; 
continue  ; 
} 
set  (  formatter  .  get  (  i  )  .  getChannel  (  )  .  getName  (  )  ,  formatter  .  get  (  i  )  .  consume  (  result  )  )  ; 
i  ++  ; 
} 
} 
}  )  ; 
} 

@  Override 
public   void   exportToInkML  (  Element   parent  )  throws   InkMLComplianceException  { 
if  (  this  .  isRoot  (  )  &&  parent  .  getNodeName  (  )  .  equals  (  InkInk  .  INKML_NAME  )  &&  this  .  getCurrentContext  (  )  !=  this  .  getInk  (  )  .  getCurrentContext  (  )  )  { 
this  .  getCurrentContext  (  )  .  exportToInkML  (  parent  )  ; 
} 
if  (  tainted  )  { 
backTransformPoints  (  )  ; 
} 
Element   traceNode  =  parent  .  getOwnerDocument  (  )  .  createElement  (  INKML_NAME  )  ; 
parent  .  appendChild  (  traceNode  )  ; 
super  .  exportToInkML  (  traceNode  )  ; 
writeAttribute  (  traceNode  ,  INKML_ATTR_TYPE  ,  this  .  getType  (  )  .  toString  (  )  ,  Type  .  PEN_DOWN  .  toString  (  )  )  ; 
if  (  getContinuation  (  )  !=  null  )  { 
writeAttribute  (  traceNode  ,  INKML_ATTR_CONTINUATION  ,  getContinuation  (  )  .  toString  (  )  ,  null  )  ; 
} 
writeAttribute  (  traceNode  ,  INKML_ATTR_PRIORREF  ,  priorRef  ,  ""  )  ; 
writeAttribute  (  traceNode  ,  INKML_ATTR_BRUSHREF  ,  brushRef  ,  null  )  ; 
if  (  duration  !=  null  )  writeAttribute  (  traceNode  ,  INKML_ATTR_DURATION  ,  duration  .  toString  (  )  ,  null  )  ; 
if  (  timeOffset  !=  null  )  writeAttribute  (  traceNode  ,  INKML_ATTR_TIMEOFFSET  ,  timeOffset  .  toString  (  )  ,  null  )  ; 
StringBuffer   pointString  =  new   StringBuffer  (  )  ; 
List  <  Formatter  >  formatter  =  new   ArrayList  <  Formatter  >  (  )  ; 
for  (  InkChannel   c  :  this  .  getSourceFormat  (  )  )  { 
formatter  .  add  (  c  .  formatterFactory  (  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  getPointCount  (  )  ;  i  ++  )  { 
for  (  int   d  =  0  ;  d  <  formatter  .  size  (  )  ;  d  ++  )  { 
pointString  .  append  (  formatter  .  get  (  d  )  .  getNext  (  sourcePoints  [  i  ]  [  d  ]  )  )  ; 
} 
pointString  .  append  (  ","  )  ; 
} 
pointString  .  deleteCharAt  (  pointString  .  length  (  )  -  1  )  ; 
traceNode  .  setTextContent  (  pointString  .  toString  (  )  )  ; 
} 










public   InkTraceLeaf  .  Continuation   getContinuation  (  )  { 
return   this  .  continuation  ; 
} 








public   List  <  InkTraceView  >  getSubSet  (  InkTraceViewContainer   tw  ,  List  <  Integer  >  from  ,  List  <  Integer  >  to  )  { 
final   InkTraceViewLeaf   tv  =  new   InkTraceViewLeaf  (  this  .  getInk  (  )  ,  tw  )  ; 
tv  .  setTraceDataRef  (  this  .  getIdNow  (  ID_PREFIX  )  )  ; 
if  (  !  from  .  isEmpty  (  )  )  { 
tv  .  setFrom  (  from  .  get  (  0  )  .  toString  (  )  )  ; 
} 
if  (  !  to  .  isEmpty  (  )  )  { 
tv  .  setTo  (  to  .  get  (  0  )  .  toString  (  )  )  ; 
} 
List  <  InkTraceView  >  l  =  new   ArrayList  <  InkTraceView  >  (  )  ; 
l  .  add  (  tv  )  ; 
return   l  ; 
} 





public   InkTraceViewLeaf   createView  (  )  { 
return   createView  (  null  )  ; 
} 






public   InkTraceViewLeaf   createView  (  InkTraceViewContainer   parent  )  { 
InkTraceViewLeaf   i  =  new   InkTraceViewLeaf  (  this  .  getInk  (  )  ,  parent  )  ; 
i  .  setTraceDataRef  (  this  .  getIdNow  (  ID_PREFIX  )  )  ; 
return   i  ; 
} 







public   void   setBrush  (  InkBrush   b  )  { 
this  .  brushRef  =  b  .  getIdNow  (  InkBrush  .  ID_PREFIX  )  ; 
} 






public   InkTraceLeaf  .  Type   getType  (  )  { 
if  (  this  .  type  ==  null  )  { 
return   InkTraceLeaf  .  Type  .  PEN_DOWN  ; 
} 
return   this  .  type  ; 
} 

@  Override 
public   boolean   isView  (  )  { 
return   false  ; 
} 

public   Polygon   getPolygon  (  )  { 
int  [  ]  xpoints  =  new   int  [  getPointCount  (  )  ]  ; 
int  [  ]  ypoints  =  new   int  [  getPointCount  (  )  ]  ; 
int   x  =  getIndex  (  ChannelName  .  X  )  ; 
int   y  =  getIndex  (  ChannelName  .  Y  )  ; 
for  (  int   i  =  0  ;  i  <  xpoints  .  length  ;  i  ++  )  { 
xpoints  [  i  ]  =  (  int  )  points  [  i  ]  [  x  ]  ; 
ypoints  [  i  ]  =  (  int  )  points  [  i  ]  [  y  ]  ; 
} 
return   new   Polygon  (  xpoints  ,  ypoints  ,  getPointCount  (  )  )  ; 
} 








public   void   reloadPoints  (  )  throws   InkMLComplianceException  { 
transform  (  )  ; 
} 






private   void   transform  (  )  throws   InkMLComplianceException  { 
if  (  points  ==  null  )  { 
points  =  new   double  [  size  ]  [  getTargetFormat  (  )  .  getChannelCount  (  )  ]  ; 
} 
getCanvasTransform  (  )  .  transform  (  sourcePoints  ,  points  ,  getSourceFormat  (  )  ,  getTargetFormat  (  )  )  ; 
notifyObserver  (  ON_CHANGE  )  ; 
} 








public   int   indexOfPoint  (  InkTracePoint   point  )  { 
if  (  point   instanceof   ProxyInkTracePoint  )  { 
return  (  (  ProxyInkTracePoint  )  point  )  .  index  (  )  ; 
} 
return  -  1  ; 
} 




@  Override 
public   boolean   testFormat  (  InkTraceFormat   canvasTraceFormat  )  { 
return   true  ; 
} 







public   abstract   class   PointConstructionBlock  { 

private   int   i  =  0  ; 

public   PointConstructionBlock  (  int   length  )  { 
sourcePoints  =  new   double  [  length  ]  [  cacheSourceIndex  .  size  (  )  ]  ; 
size  =  length  ; 
} 





public   abstract   void   addPoints  (  )  throws   InkMLComplianceException  ; 






public   void   set  (  ChannelName   name  ,  double   value  )  { 
sourcePoints  [  i  ]  [  cacheSourceIndex  .  get  (  name  )  ]  =  value  ; 
} 





public   void   next  (  )  { 
if  (  i  >=  size  )  { 
throw   new   IndexOutOfBoundsException  (  "Index "  +  i  +  " is larger than Bound: "  +  size  )  ; 
} 
i  ++  ; 
if  (  i  <  size  )  { 
for  (  int   c  =  0  ;  c  <  sourcePoints  [  i  -  1  ]  .  length  ;  c  ++  )  { 
sourcePoints  [  i  ]  [  c  ]  =  Double  .  NaN  ; 
} 
} 
} 





public   void   reduce  (  )  { 
size  --  ; 
} 

private   void   finish  (  )  { 
} 
} 








public   void   addPoints  (  PointConstructionBlock   block  )  throws   InkMLComplianceException  { 
block  .  addPoints  (  )  ; 
block  .  finish  (  )  ; 
transform  (  )  ; 
} 

public   void   accept  (  TraceVisitor   visitor  )  { 
visitor  .  visit  (  this  )  ; 
} 
} 

