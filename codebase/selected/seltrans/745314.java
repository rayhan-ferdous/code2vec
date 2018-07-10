package   jsesh  .  graphics  .  glyphs  .  model  ; 

import   java  .  awt  .  BasicStroke  ; 
import   java  .  awt  .  geom  .  Area  ; 
import   java  .  awt  .  geom  .  GeneralPath  ; 
import   java  .  awt  .  geom  .  Point2D  ; 
import   java  .  awt  .  geom  .  Rectangle2D  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Stack  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   javax  .  xml  .  parsers  .  FactoryConfigurationError  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   jsesh  .  hieroglyphs  .  GardinerCode  ; 
import   jsesh  .  hieroglyphs  .  HorizontalGravity  ; 
import   jsesh  .  hieroglyphs  .  LigatureZone  ; 
import   jsesh  .  hieroglyphs  .  ShapeChar  ; 
import   jsesh  .  hieroglyphs  .  VerticalGravity  ; 
import   org  .  xml  .  sax  .  Attributes  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 





























public   class   SVGSignSource   implements   SimpleSignSourceModel  { 

private   ShapeChar   shape  ; 

private   String   code  ; 





private   int   pos  ; 

public   SVGSignSource  (  File   file  )  { 
try  { 
initSVGSignSource  (  file  .  toURI  (  )  .  toURL  (  )  )  ; 
}  catch  (  Exception   e  )  { 
String   errorSource  =  file  .  getPath  (  )  ; 
processError  (  e  ,  errorSource  )  ; 
} 
} 








private   void   processError  (  Exception   e  ,  String   errorSource  )  { 
System  .  err  .  println  (  "Error reading "  +  errorSource  )  ; 
e  .  printStackTrace  (  )  ; 
this  .  code  =  "Error "  +  errorSource  ; 
this  .  shape  =  new   ShapeChar  (  )  ; 
this  .  shape  .  setShape  (  new   Rectangle2D  .  Double  (  0  ,  0  ,  14  ,  14  )  )  ; 
} 

public   SVGSignSource  (  URL   url  )  { 
try  { 
initSVGSignSource  (  url  )  ; 
}  catch  (  Exception   e  )  { 
String   errorSource  =  url  .  toExternalForm  (  )  ; 
processError  (  e  ,  errorSource  )  ; 
} 
} 

public   SVGSignSource  (  String   signCode  ,  InputStream   in  )  { 
try  { 
initSVGSignSource  (  signCode  ,  in  ,  signCode  )  ; 
}  catch  (  Exception   e  )  { 
String   errorSource  =  signCode  ; 
processError  (  e  ,  errorSource  )  ; 
} 
} 

private   void   initSVGSignSource  (  URL   url  )  throws   IOException  { 
String   ressourceName  =  url  .  toString  (  )  ; 
InputStream   in  =  url  .  openStream  (  )  ; 
initSVGSignSource  (  ressourceName  ,  in  ,  getCodeForURL  (  url  )  )  ; 
} 

private   void   initSVGSignSource  (  String   ressourceName  ,  InputStream   in  ,  String   signCode  )  throws   FactoryConfigurationError  { 
try  { 
code  =  signCode  ; 
if  (  code  ==  null  )  code  =  ""  ; 
SAXParserFactory   parserFactory  =  SAXParserFactory  .  newInstance  (  )  ; 
parserFactory  .  setValidating  (  false  )  ; 
parserFactory  .  setFeature  (  "http://xml.org/sax/features/namespaces"  ,  true  )  ; 
parserFactory  .  setFeature  (  "http://xml.org/sax/features/namespace-prefixes"  ,  false  )  ; 
SAXParser   parser  =  parserFactory  .  newSAXParser  (  )  ; 
SVGReader   handler  =  new   SVGReader  (  )  ; 
parser  .  parse  (  in  ,  handler  )  ; 
shape  =  new   ShapeChar  (  )  ; 
if  (  handler  .  getZones  (  )  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  handler  .  getZones  (  )  .  length  ;  i  ++  )  { 
shape  .  setZone  (  i  ,  handler  .  getZones  (  )  [  i  ]  )  ; 
} 
} 
shape  .  setShape  (  handler  .  getGeneralPath  (  )  )  ; 
if  (  shape  .  getBbox  (  )  .  getHeight  (  )  >  60  )  { 
double   newHeight  =  shape  .  getBbox  (  )  .  getHeight  (  )  /  100.0  ; 
shape  .  scaleToHeight  (  newHeight  )  ; 
} 
simplifyShape  (  )  ; 
shape  .  setAuthor  (  handler  .  author  )  ; 
shape  .  setDocumentation  (  handler  .  description  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
processError  (  e  ,  ressourceName  )  ; 
}  catch  (  SAXException   e  )  { 
processError  (  e  ,  ressourceName  )  ; 
}  catch  (  IOException   e  )  { 
processError  (  e  ,  ressourceName  )  ; 
} 
beforeFirst  (  )  ; 
} 




private   void   simplifyShape  (  )  { 
} 

public   ShapeChar   getCurrentShape  (  )  { 
return   shape  ; 
} 

public   boolean   hasNext  (  )  { 
return   pos  ==  -  1  ; 
} 

public   boolean   hasPrevious  (  )  { 
return   pos  ==  1  ; 
} 

public   void   next  (  )  { 
if  (  hasNext  (  )  )  pos  ++  ; 
} 

public   void   previous  (  )  { 
if  (  hasPrevious  (  )  )  pos  --  ; 
} 

public   void   beforeFirst  (  )  { 
pos  =  -  1  ; 
} 

public   void   afterLast  (  )  { 
pos  =  1  ; 
} 

static   class   Style  { 




boolean   blackPaint  =  true  ; 





boolean   filled  =  true  ; 




boolean   outlined  =  false  ; 

float   strokeWidth  =  0f  ; 

public   Style  (  )  { 
} 

public   Style  (  Style   parent  )  { 
if  (  parent  !=  null  )  { 
blackPaint  =  parent  .  blackPaint  ; 
outlined  =  parent  .  outlined  ; 
filled  =  parent  .  filled  ; 
strokeWidth  =  parent  .  strokeWidth  ; 
} 
} 

public   Style  (  Style   parent  ,  Attributes   attributes  )  { 
this  (  parent  )  ; 
String   styleDef  =  attributes  .  getValue  (  ""  ,  "style"  )  ; 
if  (  styleDef  !=  null  )  { 
parseStyleDef  (  styleDef  )  ; 
} 
parseAttributes  (  attributes  )  ; 
} 

private   void   parseAttributes  (  Attributes   attributes  )  { 
String   fillColorAttr  =  attributes  .  getValue  (  ""  ,  "fill"  )  ; 
if  (  fillColorAttr  !=  null  )  { 
if  (  "none"  .  equals  (  fillColorAttr  )  )  filled  =  false  ;  else  { 
blackPaint  =  false  ; 
if  (  fillColorAttr  .  startsWith  (  "#000000"  )  )  blackPaint  =  true  ; 
if  (  "black"  .  equals  (  fillColorAttr  )  )  blackPaint  =  true  ; 
if  (  fillColorAttr  .  startsWith  (  "rgb(0,0,0"  )  )  blackPaint  =  true  ; 
if  (  fillColorAttr  .  startsWith  (  "currentColor"  )  )  blackPaint  =  true  ; 
} 
} 
if  (  "none"  .  equals  (  attributes  .  getValue  (  ""  ,  "stroke"  )  )  )  outlined  =  false  ;  else   if  (  attributes  .  getValue  (  ""  ,  "stroke"  )  !=  null  )  { 
outlined  =  true  ; 
} 
if  (  attributes  .  getValue  (  "stroke-width"  )  !=  null  )  { 
String   w  =  attributes  .  getValue  (  "stroke-width"  )  ; 
int   end  =  w  .  length  (  )  -  1  ; 
if  (  end  >=  0  )  { 
while  (  end  >=  0  &&  !  Character  .  isDigit  (  w  .  charAt  (  end  )  )  )  end  --  ; 
this  .  strokeWidth  =  Float  .  parseFloat  (  w  .  substring  (  0  ,  end  +  1  )  )  ; 
}  else   strokeWidth  =  0f  ; 
} 
} 

private   void   parseStyleDef  (  String   styleDef  )  { 
if  (  styleDef  ==  null  )  return  ; 
if  (  styleDef  .  indexOf  (  "fill:none"  )  !=  -  1  )  filled  =  false  ;  else   if  (  styleDef  .  indexOf  (  "fill:"  )  !=  -  1  )  { 
filled  =  true  ; 
if  (  styleDef  .  indexOf  (  "fill:#000000"  )  !=  -  1  ||  styleDef  .  indexOf  (  "fill:rgb(0,0,0)"  )  !=  -  1  ||  styleDef  .  indexOf  (  "fill:black"  )  !=  -  1  )  blackPaint  =  true  ;  else   blackPaint  =  false  ; 
} 
if  (  styleDef  .  indexOf  (  "stroke:none"  )  !=  -  1  )  { 
outlined  =  false  ; 
}  else   if  (  styleDef  .  indexOf  (  "stroke:"  )  !=  -  1  )  { 
outlined  =  true  ; 
} 
int   i  =  styleDef  .  indexOf  (  "stroke-width:"  )  ; 
if  (  i  !=  -  1  )  { 
String   widthDef  ; 
i  =  styleDef  .  indexOf  (  ':'  ,  i  )  ; 
i  ++  ; 
int   end  =  styleDef  .  indexOf  (  ';'  ,  i  )  ; 
if  (  end  ==  -  1  )  end  =  styleDef  .  length  (  )  -  1  ; 
while  (  !  Character  .  isDigit  (  styleDef  .  charAt  (  end  )  )  )  end  --  ; 
widthDef  =  styleDef  .  substring  (  i  ,  end  +  1  )  ; 
strokeWidth  =  Float  .  parseFloat  (  widthDef  )  ; 
} 
} 
} 

class   SVGReader   extends   DefaultHandler  { 

public   static   final   String   INKSCAPE_NAMESPACES  =  "http://www.inkscape.org/namespaces/inkscape"  ; 

public   static   final   String   DUBLIN_CORE_NAMESPACE  =  "http://purl.org/dc/elements/1.1/"  ; 









private   String   zoneName  =  null  ; 

private   LigatureZone  [  ]  zones  ; 

private   Area   area  ; 

private   Stack   styles  ; 

boolean   inPattern  =  false  ; 

private   int   clipPathDepth  =  0  ; 

String   description  =  ""  ; 

String   author  =  ""  ; 

String   license  ; 

StringBuffer   currentText  =  new   StringBuffer  (  )  ; 

boolean   recordText  =  false  ; 

public   SVGReader  (  )  { 
area  =  new   Area  (  )  ; 
styles  =  new   Stack  (  )  ; 
zones  =  null  ; 
} 

public   void   characters  (  char  [  ]  ch  ,  int   start  ,  int   length  )  throws   SAXException  { 
if  (  recordText  )  currentText  .  append  (  ch  ,  start  ,  length  )  ; 
} 

public   void   startElement  (  String   uri  ,  String   localName  ,  String   qName  ,  Attributes   attributes  )  throws   SAXException  { 
Style   parentStyle  =  null  ; 
if  (  !  styles  .  isEmpty  (  )  )  parentStyle  =  (  Style  )  styles  .  peek  (  )  ; 
Style   style  =  new   Style  (  parentStyle  ,  attributes  )  ; 
styles  .  push  (  style  )  ; 
if  (  "a"  .  equals  (  localName  )  )  { 
readAnchor  (  attributes  )  ; 
}  else   if  (  "rect"  .  equals  (  localName  )  )  { 
if  (  attributes  .  getValue  (  "id"  )  !=  null  &&  attributes  .  getValue  (  "id"  )  .  startsWith  (  "zone"  )  )  { 
zoneName  =  attributes  .  getValue  (  ""  ,  "id"  )  ; 
defineZone  (  attributes  )  ; 
zoneName  =  null  ; 
}  else   if  (  zoneName  !=  null  )  defineZone  (  attributes  )  ; 
}  else   if  (  "path"  .  equals  (  localName  )  )  { 
String   d  =  attributes  .  getValue  (  ""  ,  "d"  )  ; 
GeneralPath   subPath  =  parsePath  (  d  )  ; 
if  (  subPath  !=  null  )  { 
addPathToArea  (  style  ,  subPath  )  ; 
} 
}  else   if  (  "polygon"  .  equals  (  localName  )  )  { 
GeneralPath   subPath  =  new   GeneralPath  (  )  ; 
String   pointsDef  =  attributes  .  getValue  (  ""  ,  "points"  )  ; 
if  (  pointsDef  !=  null  )  { 
String  [  ]  ps  =  pointsDef  .  split  (  " "  )  ; 
for  (  int   i  =  0  ;  i  <  ps  .  length  ;  i  ++  )  { 
String  [  ]  coords  =  ps  [  i  ]  .  split  (  ","  )  ; 
if  (  coords  .  length  !=  2  )  return  ;  else  { 
if  (  i  ==  0  )  subPath  .  moveTo  (  Float  .  parseFloat  (  coords  [  0  ]  )  ,  java  .  lang  .  Float  .  parseFloat  (  coords  [  1  ]  )  )  ;  else   subPath  .  lineTo  (  Float  .  parseFloat  (  coords  [  0  ]  )  ,  java  .  lang  .  Float  .  parseFloat  (  coords  [  1  ]  )  )  ; 
} 
} 
if  (  !  style  .  outlined  )  { 
subPath  .  closePath  (  )  ; 
} 
addPathToArea  (  style  ,  subPath  )  ; 
} 
}  else   if  (  "pattern"  .  equals  (  localName  )  )  { 
inPattern  =  true  ; 
}  else   if  (  "clipPath"  .  equals  (  localName  )  )  { 
clipPathDepth  ++  ; 
}  else   if  (  isDCDescription  (  uri  ,  localName  )  )  { 
recordText  =  true  ; 
currentText  .  setLength  (  0  )  ; 
}  else   if  (  isDCCreator  (  uri  ,  localName  )  )  { 
recordText  =  true  ; 
currentText  .  setLength  (  0  )  ; 
} 
} 

private   boolean   isDCDescription  (  String   uri  ,  String   localName  )  { 
return  "description"  .  equals  (  localName  )  &&  DUBLIN_CORE_NAMESPACE  .  equals  (  uri  )  ; 
} 

private   boolean   isDCCreator  (  String   uri  ,  String   localName  )  { 
return  "creator"  .  equals  (  localName  )  &&  DUBLIN_CORE_NAMESPACE  .  equals  (  uri  )  ; 
} 

private   void   addPathToArea  (  Style   style  ,  GeneralPath   subPath  )  { 
if  (  inPattern  ||  clipPathDepth  >  0  )  return  ; 
if  (  style  .  outlined  )  { 
area  .  add  (  new   Area  (  new   BasicStroke  (  style  .  strokeWidth  )  .  createStrokedShape  (  subPath  )  )  )  ; 
} 
if  (  style  .  filled  )  { 
if  (  style  .  blackPaint  )  { 
area  .  add  (  new   Area  (  subPath  )  )  ; 
}  else  { 
area  .  subtract  (  new   Area  (  subPath  )  )  ; 
} 
} 
} 






















private   void   defineZone  (  Attributes   attributes  )  { 
int   zoneId  =  0  ; 
if  (  "zone2"  .  equals  (  zoneName  )  )  { 
zoneId  =  1  ; 
} 
if  (  zones  ==  null  )  zones  =  new   LigatureZone  [  2  ]  ; 
double   values  [  ]  =  new   double  [  4  ]  ; 
String   attrNames  [  ]  =  {  "x"  ,  "y"  ,  "width"  ,  "height"  }  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
try  { 
values  [  i  ]  =  java  .  lang  .  Double  .  parseDouble  (  attributes  .  getValue  (  ""  ,  attrNames  [  i  ]  )  )  ; 
}  catch  (  NumberFormatException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
} 
zones  [  zoneId  ]  =  new   LigatureZone  (  new   Rectangle2D  .  Double  (  values  [  0  ]  ,  values  [  1  ]  ,  values  [  2  ]  ,  values  [  3  ]  )  )  ; 
String   label  =  attributes  .  getValue  (  ""  ,  "label"  )  ; 
if  (  label  ==  null  )  label  =  attributes  .  getValue  (  INKSCAPE_NAMESPACES  ,  "label"  )  ; 
if  (  label  !=  null  )  { 
String  [  ]  additionnalData  =  label  .  split  (  "; *"  )  ; 
for  (  int   i  =  0  ;  i  <  additionnalData  .  length  ;  i  ++  )  { 
String   args  [  ]  =  additionnalData  [  i  ]  .  split  (  ":"  )  ; 
if  (  args  !=  null  &&  args  .  length  ==  2  )  { 
if  (  args  [  0  ]  .  equals  (  "gravity"  )  )  { 
for  (  int   k  =  0  ;  k  <  args  [  1  ]  .  length  (  )  ;  k  ++  )  { 
switch  (  args  [  1  ]  .  charAt  (  k  )  )  { 
case  't'  : 
zones  [  zoneId  ]  .  setVerticalGravity  (  VerticalGravity  .  TOP  )  ; 
break  ; 
case  'b'  : 
zones  [  zoneId  ]  .  setVerticalGravity  (  VerticalGravity  .  BOTTOM  )  ; 
break  ; 
case  's'  : 
zones  [  zoneId  ]  .  setHorizontalGravity  (  HorizontalGravity  .  START  )  ; 
break  ; 
case  'e'  : 
zones  [  zoneId  ]  .  setHorizontalGravity  (  HorizontalGravity  .  END  )  ; 
break  ; 
} 
} 
} 
} 
} 
} 
} 




private   void   readAnchor  (  Attributes   attributes  )  { 
zoneName  =  null  ; 
for  (  int   i  =  0  ;  zoneName  ==  null  &&  i  <  attributes  .  getLength  (  )  ;  i  ++  )  { 
if  (  "title"  .  equals  (  attributes  .  getLocalName  (  i  )  )  )  zoneName  =  attributes  .  getValue  (  i  )  ; 
} 
} 

public   void   endElement  (  String   uri  ,  String   localName  ,  String   qName  )  throws   SAXException  { 
if  (  "a"  .  equals  (  localName  )  )  this  .  zoneName  =  null  ;  else   if  (  "pattern"  .  equals  (  localName  )  )  { 
inPattern  =  false  ; 
}  else   if  (  "clipPath"  .  equals  (  localName  )  )  { 
clipPathDepth  --  ; 
}  else   if  (  isDCDescription  (  uri  ,  localName  )  )  { 
recordText  =  false  ; 
description  =  currentText  .  toString  (  )  .  trim  (  )  ; 
currentText  .  setLength  (  0  )  ; 
}  else   if  (  isDCCreator  (  uri  ,  localName  )  )  { 
recordText  =  false  ; 
author  =  currentText  .  toString  (  )  .  trim  (  )  ; 
currentText  .  setLength  (  0  )  ; 
} 
styles  .  pop  (  )  ; 
} 


































private   GeneralPath   parsePath  (  String   pathData  )  { 
GeneralPath   result  =  new   GeneralPath  (  )  ; 
String  [  ]  l  =  pathToList  (  pathData  )  ; 
Point2D  .  Float   currentPos  =  new   Point2D  .  Float  (  )  ; 
Point2D  .  Float   previousCubicControlPoint  =  null  ; 
Point2D  .  Float   previousQuadricControlPoint  =  null  ; 
Point2D  .  Float   pathStart  =  new   Point2D  .  Float  (  )  ; 
float  [  ]  args  =  null  ; 
char   command  =  'M'  ; 
for  (  int   i  =  0  ;  i  <  l  .  length  ;  i  ++  )  { 
char   firstChar  =  l  [  i  ]  .  charAt  (  0  )  ; 
if  (  Character  .  isLetter  (  firstChar  )  )  { 
command  =  firstChar  ; 
}  else  { 
i  --  ; 
} 
boolean   relative  =  Character  .  isLowerCase  (  command  )  ; 
switch  (  command  )  { 
case  'm'  : 
case  'M'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  ++  i  ]  )  ,  Float  .  parseFloat  (  l  [  ++  i  ]  )  }  ; 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  moveTo  (  args  [  0  ]  ,  args  [  1  ]  )  ; 
pathStart  .  setLocation  (  args  [  0  ]  ,  args  [  1  ]  )  ; 
if  (  command  ==  'm'  )  command  =  'l'  ;  else   command  =  'L'  ; 
} 
break  ; 
case  'l'  : 
case  'L'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  2  ]  )  }  ; 
i  +=  2  ; 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  lineTo  (  args  [  0  ]  ,  args  [  1  ]  )  ; 
} 
break  ; 
case  'c'  : 
case  'C'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  2  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  3  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  4  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  5  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  6  ]  )  }  ; 
i  +=  6  ; 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  curveTo  (  args  [  0  ]  ,  args  [  1  ]  ,  args  [  2  ]  ,  args  [  3  ]  ,  args  [  4  ]  ,  args  [  5  ]  )  ; 
previousCubicControlPoint  =  new   Point2D  .  Float  (  args  [  2  ]  ,  args  [  3  ]  )  ; 
} 
break  ; 
case  's'  : 
case  'S'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  2  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  3  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  4  ]  )  }  ; 
i  +=  4  ; 
float   cx  ,  cy  ; 
if  (  previousCubicControlPoint  !=  null  )  { 
cx  =  2  *  currentPos  .  x  -  previousCubicControlPoint  .  x  ; 
cy  =  2  *  currentPos  .  y  -  previousCubicControlPoint  .  y  ; 
}  else  { 
cx  =  currentPos  .  x  ; 
cy  =  currentPos  .  y  ; 
} 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  curveTo  (  cx  ,  cy  ,  args  [  0  ]  ,  args  [  1  ]  ,  args  [  2  ]  ,  args  [  3  ]  )  ; 
previousCubicControlPoint  =  new   Point2D  .  Float  (  args  [  0  ]  ,  args  [  1  ]  )  ; 
} 
break  ; 
case  'q'  : 
case  'Q'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  2  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  3  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  4  ]  )  }  ; 
i  +=  4  ; 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  quadTo  (  args  [  0  ]  ,  args  [  1  ]  ,  args  [  2  ]  ,  args  [  3  ]  )  ; 
previousQuadricControlPoint  =  new   Point2D  .  Float  (  args  [  0  ]  ,  args  [  1  ]  )  ; 
} 
break  ; 
case  't'  : 
case  'T'  : 
{ 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  2  ]  )  }  ; 
i  +=  2  ; 
float   cx  ,  cy  ; 
if  (  previousQuadricControlPoint  !=  null  )  { 
cx  =  2  *  currentPos  .  x  -  previousQuadricControlPoint  .  x  ; 
cy  =  2  *  currentPos  .  y  -  previousQuadricControlPoint  .  y  ; 
}  else  { 
cx  =  currentPos  .  x  ; 
cy  =  currentPos  .  y  ; 
} 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
result  .  quadTo  (  cx  ,  cy  ,  args  [  0  ]  ,  args  [  1  ]  )  ; 
previousQuadricControlPoint  =  new   Point2D  .  Float  (  cx  ,  cy  )  ; 
} 
break  ; 
case  'a'  : 
case  'A'  : 
{ 
float   startx  =  currentPos  .  x  ; 
float   starty  =  currentPos  .  y  ; 
float   rx  =  Float  .  parseFloat  (  l  [  i  +  1  ]  )  ; 
float   ry  =  Float  .  parseFloat  (  l  [  i  +  2  ]  )  ; 
float   rot  =  Float  .  parseFloat  (  l  [  i  +  3  ]  )  ; 
float   largeArc  =  Float  .  parseFloat  (  l  [  i  +  4  ]  )  ; 
float   sweep  =  Float  .  parseFloat  (  l  [  i  +  5  ]  )  ; 
args  =  new   float  [  ]  {  Float  .  parseFloat  (  l  [  i  +  6  ]  )  ,  Float  .  parseFloat  (  l  [  i  +  7  ]  )  }  ; 
i  +=  7  ; 
fixArgsAndCurrentPos  (  args  ,  currentPos  ,  relative  )  ; 
drawArc  (  result  ,  startx  ,  starty  ,  rx  ,  ry  ,  rot  ,  largeArc  ,  sweep  ,  args  [  0  ]  ,  args  [  1  ]  )  ; 
} 
break  ; 
case  'z'  : 
case  'Z'  : 
{ 
result  .  closePath  (  )  ; 
currentPos  .  setLocation  (  pathStart  )  ; 
} 
break  ; 
case  'h'  : 
{ 
float   dx  =  Float  .  parseFloat  (  l  [  ++  i  ]  )  ; 
result  .  lineTo  (  dx  +  currentPos  .  x  ,  currentPos  .  y  )  ; 
currentPos  .  x  +=  dx  ; 
} 
break  ; 
case  'H'  : 
{ 
float   newx  =  Float  .  parseFloat  (  l  [  ++  i  ]  )  ; 
result  .  lineTo  (  newx  ,  currentPos  .  y  )  ; 
currentPos  .  x  =  newx  ; 
} 
break  ; 
case  'v'  : 
{ 
float   dy  =  Float  .  parseFloat  (  l  [  ++  i  ]  )  ; 
result  .  lineTo  (  currentPos  .  x  ,  dy  +  currentPos  .  y  )  ; 
currentPos  .  y  +=  dy  ; 
} 
break  ; 
case  'V'  : 
{ 
float   newy  =  Float  .  parseFloat  (  l  [  ++  i  ]  )  ; 
result  .  lineTo  (  currentPos  .  x  ,  newy  )  ; 
currentPos  .  y  =  newy  ; 
} 
break  ; 
default  : 
System  .  err  .  println  (  "unknown code"  +  l  [  i  ]  )  ; 
return   null  ; 
} 
if  (  Character  .  toLowerCase  (  command  )  !=  'c'  &&  Character  .  toLowerCase  (  command  )  !=  's'  )  previousCubicControlPoint  =  null  ; 
if  (  Character  .  toLowerCase  (  command  )  !=  'q'  &&  Character  .  toLowerCase  (  command  )  !=  't'  )  previousQuadricControlPoint  =  null  ; 
} 
return   result  ; 
} 

















private   void   drawArc  (  GeneralPath   result  ,  float   startx  ,  float   starty  ,  float   rx  ,  float   ry  ,  float   rot  ,  float   largeArc  ,  float   sweep  ,  float   endx  ,  float   endy  )  { 
result  .  lineTo  (  endx  ,  endy  )  ; 
} 
















private   String  [  ]  pathToList  (  String   pathData  )  { 
String   real  =  "[-+]?(?:\\d*\\.\\d+|\\d+\\.?)(?:[eE][-+]?\\d+)?"  ; 
String   keys  =  "[aAtTQqSsCcVvHhLlZzMm]"  ; 
String   command  =  "("  +  real  +  "|"  +  keys  +  ")"  ; 
Pattern   p  =  Pattern  .  compile  (  command  )  ; 
Matcher   m  =  p  .  matcher  (  pathData  )  ; 
ArrayList   result  =  new   ArrayList  (  )  ; 
while  (  m  .  find  (  )  )  { 
result  .  add  (  m  .  group  (  )  )  ; 
} 
return  (  String  [  ]  )  result  .  toArray  (  new   String  [  result  .  size  (  )  ]  )  ; 
} 

public   InputSource   resolveEntity  (  String   publicId  ,  String   systemId  )  throws   SAXException  { 
if  (  publicId  .  indexOf  (  " SVG "  )  !=  -  1  )  { 
return   new   InputSource  (  new   StringReader  (  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  )  )  ; 
}  else  { 
try  { 
return   super  .  resolveEntity  (  publicId  ,  systemId  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
} 

public   GeneralPath   getGeneralPath  (  )  { 
GeneralPath   shape  =  new   GeneralPath  (  )  ; 
shape  .  append  (  area  .  getPathIterator  (  null  )  ,  false  )  ; 
return   shape  ; 
} 




public   LigatureZone  [  ]  getZones  (  )  { 
return   zones  ; 
} 











private   void   fixArgsAndCurrentPos  (  float  [  ]  args  ,  Point2D  .  Float   current  ,  boolean   relative  )  { 
int   i  =  0  ; 
for  (  i  =  0  ;  i  <  args  .  length  ;  i  +=  2  )  { 
if  (  relative  )  { 
args  [  i  ]  =  args  [  i  ]  +  current  .  x  ; 
args  [  i  +  1  ]  =  args  [  i  +  1  ]  +  current  .  y  ; 
} 
} 
current  .  setLocation  (  args  [  i  -  2  ]  ,  args  [  i  -  1  ]  )  ; 
} 
} 

public   String   getCurrentCode  (  )  { 
return   code  ; 
} 

private   String   getCodeForURL  (  URL   url  )  { 
String   path  =  url  .  getPath  (  )  ; 
int   id  =  path  .  lastIndexOf  (  '/'  )  ; 
String   code  =  GardinerCode  .  getCodeForFileName  (  path  .  substring  (  id  +  1  )  )  ; 
if  (  code  ==  null  )  { 
code  =  path  .  substring  (  id  +  1  )  ; 
code  =  code  .  substring  (  0  ,  code  .  indexOf  (  '.'  )  )  ; 
} 
return   code  ; 
} 
} 

