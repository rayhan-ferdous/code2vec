package   gnu  .  java  .  awt  .  peer  .  qt  ; 

import   java  .  awt  .  Graphics  ; 
import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  image  .  ColorModel  ; 
import   java  .  awt  .  image  .  DirectColorModel  ; 
import   java  .  awt  .  image  .  MemoryImageSource  ; 
import   java  .  awt  .  image  .  ImageConsumer  ; 
import   java  .  awt  .  image  .  ImageObserver  ; 
import   java  .  awt  .  image  .  ImageProducer  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  WeakHashMap  ; 
import   java  .  util  .  Vector  ; 





public   class   QtImage   extends   Image  { 

int   width  =  -  1  ,  height  =  -  1  ; 




Hashtable   props  ; 




boolean   isLoaded  ; 




long   nativeObject  ; 




Vector   observers  ; 




boolean   errorLoading  ; 




ImageProducer   source  ; 

static   ColorModel   nativeModel  =  new   DirectColorModel  (  32  ,  0x00FF0000  ,  0x0000FF00  ,  0x000000FF  ,  0xFF000000  )  ; 




WeakHashMap   painters  ; 




boolean   killFlag  ; 




public   native   void   clear  (  )  ; 




private   native   int  [  ]  getPixels  (  )  ; 




private   native   void   setPixels  (  int  [  ]  pixels  )  ; 




private   native   boolean   loadImage  (  String   name  )  ; 




private   native   boolean   loadImageFromData  (  byte  [  ]  data  )  ; 




private   native   void   createImage  (  )  ; 




private   native   synchronized   void   freeImage  (  )  ; 




private   native   void   createScaledImage  (  QtImage   src  ,  int   hints  )  ; 




native   void   drawPixels  (  QtGraphics   gc  ,  int   bg_red  ,  int   bg_green  ,  int   bg_blue  ,  int   x  ,  int   y  ,  boolean   composite  )  ; 




private   native   void   drawPixelsScaled  (  QtGraphics   gc  ,  int   bg_red  ,  int   bg_green  ,  int   bg_blue  ,  int   x  ,  int   y  ,  int   width  ,  int   height  ,  boolean   composite  )  ; 




private   native   void   drawPixelsTransformed  (  QtGraphics   gc  ,  QMatrix   transform  )  ; 




native   void   drawPixelsScaledFlipped  (  QtGraphics   gc  ,  int   bg_red  ,  int   bg_green  ,  int   bg_blue  ,  boolean   flipX  ,  boolean   flipY  ,  int   srcX  ,  int   srcY  ,  int   srcWidth  ,  int   srcHeight  ,  int   dstX  ,  int   dstY  ,  int   dstWidth  ,  int   dstHeight  ,  boolean   composite  )  ; 




public   QtImage  (  ImageProducer   producer  )  { 
killFlag  =  false  ; 
isLoaded  =  false  ; 
observers  =  new   Vector  (  )  ; 
source  =  producer  ; 
errorLoading  =  false  ; 
if  (  producer  !=  null  )  source  .  startProduction  (  new   QtImageConsumer  (  this  ,  source  )  )  ; 
} 




public   QtImage  (  URL   url  )  { 
killFlag  =  false  ; 
isLoaded  =  false  ; 
observers  =  new   Vector  (  )  ; 
errorLoading  =  false  ; 
if  (  url  ==  null  )  return  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  5000  )  ; 
try  { 
BufferedInputStream   bis  =  new   BufferedInputStream  (  url  .  openStream  (  )  )  ; 
byte  [  ]  buf  =  new   byte  [  5000  ]  ; 
int   n  =  0  ; 
while  (  (  n  =  bis  .  read  (  buf  )  )  !=  -  1  )  baos  .  write  (  buf  ,  0  ,  n  )  ; 
bis  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IllegalArgumentException  (  "Couldn't load image."  )  ; 
} 
if  (  loadImageFromData  (  baos  .  toByteArray  (  )  )  !=  true  )  throw   new   IllegalArgumentException  (  "Couldn't load image."  )  ; 
isLoaded  =  true  ; 
observers  =  null  ; 
props  =  new   Hashtable  (  )  ; 
} 






public   QtImage  (  String   filename  )  { 
killFlag  =  false  ; 
File   f  =  new   File  (  filename  )  ; 
observers  =  null  ; 
props  =  new   Hashtable  (  )  ; 
try  { 
String   fn  =  f  .  getCanonicalPath  (  )  ; 
if  (  loadImage  (  fn  )  !=  true  )  { 
errorLoading  =  true  ; 
isLoaded  =  false  ; 
return  ; 
} 
}  catch  (  IOException   e  )  { 
errorLoading  =  true  ; 
isLoaded  =  false  ; 
return  ; 
} 
errorLoading  =  false  ; 
isLoaded  =  true  ; 
} 






public   QtImage  (  byte  [  ]  data  )  { 
if  (  loadImageFromData  (  data  )  !=  true  )  throw   new   IllegalArgumentException  (  "Couldn't load image."  )  ; 
killFlag  =  false  ; 
isLoaded  =  true  ; 
observers  =  null  ; 
errorLoading  =  false  ; 
props  =  new   Hashtable  (  )  ; 
} 




public   QtImage  (  int   width  ,  int   height  )  { 
this  .  width  =  width  ; 
this  .  height  =  height  ; 
props  =  new   Hashtable  (  )  ; 
isLoaded  =  true  ; 
killFlag  =  false  ; 
observers  =  null  ; 
errorLoading  =  false  ; 
createImage  (  )  ; 
clear  (  )  ; 
} 




private   QtImage  (  QtImage   src  ,  int   width  ,  int   height  ,  int   hints  )  { 
this  .  width  =  width  ; 
this  .  height  =  height  ; 
props  =  new   Hashtable  (  )  ; 
isLoaded  =  true  ; 
killFlag  =  false  ; 
observers  =  null  ; 
errorLoading  =  false  ; 
createScaledImage  (  src  ,  hints  )  ; 
} 




public   void   setImage  (  int   width  ,  int   height  ,  int  [  ]  pixels  ,  Hashtable   properties  )  { 
this  .  width  =  width  ; 
this  .  height  =  height  ; 
props  =  (  properties  !=  null  )  ?  properties  :  new   Hashtable  (  )  ; 
if  (  width  <=  0  ||  height  <=  0  ||  pixels  ==  null  )  { 
errorLoading  =  true  ; 
return  ; 
} 
isLoaded  =  true  ; 
deliver  (  )  ; 
createImage  (  )  ; 
setPixels  (  pixels  )  ; 
} 

public   int   getWidth  (  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return  -  1  ; 
return   width  ; 
} 

public   int   getHeight  (  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return  -  1  ; 
return   height  ; 
} 

public   Object   getProperty  (  String   name  ,  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return   UndefinedProperty  ; 
Object   value  =  props  .  get  (  name  )  ; 
return  (  value  ==  null  )  ?  UndefinedProperty  :  value  ; 
} 




public   ImageProducer   getSource  (  )  { 
if  (  !  isLoaded  )  return   null  ; 
return   new   MemoryImageSource  (  width  ,  height  ,  nativeModel  ,  getPixels  (  )  ,  0  ,  width  )  ; 
} 

void   putPainter  (  QtImageGraphics   g  )  { 
if  (  painters  ==  null  )  painters  =  new   WeakHashMap  (  )  ; 
painters  .  put  (  g  ,  "dummy"  )  ; 
} 

void   removePainter  (  QtImageGraphics   g  )  { 
painters  .  remove  (  g  )  ; 
if  (  killFlag  &&  painters  .  isEmpty  (  )  )  freeImage  (  )  ; 
} 




public   Graphics   getGraphics  (  )  { 
if  (  !  isLoaded  ||  killFlag  )  return   null  ; 
return   new   QtImageGraphics  (  this  )  ; 
} 




Graphics   getDirectGraphics  (  QtComponentPeer   peer  )  { 
if  (  !  isLoaded  )  return   null  ; 
return   new   QtImageDirectGraphics  (  this  ,  peer  )  ; 
} 




public   Image   getScaledInstance  (  int   width  ,  int   height  ,  int   hints  )  { 
if  (  width  <=  0  ||  height  <=  0  )  throw   new   IllegalArgumentException  (  "Width and height of scaled bitmap"  +  "must be >= 0"  )  ; 
return   new   QtImage  (  this  ,  width  ,  height  ,  hints  )  ; 
} 









public   synchronized   void   flush  (  )  { 
if  (  isLoaded  &&  source  !=  null  )  { 
observers  =  new   Vector  (  )  ; 
isLoaded  =  false  ; 
freeImage  (  )  ; 
source  .  startProduction  (  new   QtImageConsumer  (  this  ,  source  )  )  ; 
} 
} 

public   void   finalize  (  )  { 
dispose  (  )  ; 
} 

public   void   dispose  (  )  { 
if  (  isLoaded  )  { 
if  (  painters  ==  null  ||  painters  .  isEmpty  (  )  )  freeImage  (  )  ;  else   killFlag  =  true  ; 
} 
} 




public   int   checkImage  (  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  { 
if  (  errorLoading  ==  true  )  return   ImageObserver  .  ERROR  ;  else   return   0  ; 
} 
return   ImageObserver  .  ALLBITS  |  ImageObserver  .  WIDTH  |  ImageObserver  .  HEIGHT  ; 
} 




public   boolean   drawImage  (  QtGraphics   g  ,  QMatrix   matrix  ,  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return   false  ; 
drawPixelsTransformed  (  g  ,  matrix  )  ; 
return   true  ; 
} 





public   boolean   drawImage  (  QtGraphics   g  ,  int   x  ,  int   y  ,  Color   bgcolor  ,  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return   false  ; 
if  (  bgcolor  !=  null  )  drawPixels  (  g  ,  bgcolor  .  getRed  (  )  ,  bgcolor  .  getGreen  (  )  ,  bgcolor  .  getBlue  (  )  ,  x  ,  y  ,  true  )  ;  else   drawPixels  (  g  ,  0  ,  0  ,  0  ,  x  ,  y  ,  false  )  ; 
return   true  ; 
} 





public   boolean   drawImage  (  QtGraphics   g  ,  int   x  ,  int   y  ,  int   width  ,  int   height  ,  Color   bgcolor  ,  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return   false  ; 
if  (  bgcolor  !=  null  )  drawPixelsScaled  (  g  ,  bgcolor  .  getRed  (  )  ,  bgcolor  .  getGreen  (  )  ,  bgcolor  .  getBlue  (  )  ,  x  ,  y  ,  width  ,  height  ,  true  )  ;  else   drawPixelsScaled  (  g  ,  0  ,  0  ,  0  ,  x  ,  y  ,  width  ,  height  ,  false  )  ; 
return   true  ; 
} 




public   boolean   drawImage  (  QtGraphics   g  ,  int   dx1  ,  int   dy1  ,  int   dx2  ,  int   dy2  ,  int   sx1  ,  int   sy1  ,  int   sx2  ,  int   sy2  ,  Color   bgcolor  ,  ImageObserver   observer  )  { 
if  (  addObserver  (  observer  )  )  return   false  ; 
boolean   flipX  =  (  dx1  >  dx2  )  ^  (  sx1  >  sx2  )  ; 
boolean   flipY  =  (  dy1  >  dy2  )  ^  (  sy1  >  sy2  )  ; 
int   dstWidth  =  Math  .  abs  (  dx2  -  dx1  )  ; 
int   dstHeight  =  Math  .  abs  (  dy2  -  dy1  )  ; 
int   srcWidth  =  Math  .  abs  (  sx2  -  sx1  )  ; 
int   srcHeight  =  Math  .  abs  (  sy2  -  sy1  )  ; 
int   srcX  =  (  sx1  <  sx2  )  ?  sx1  :  sx2  ; 
int   srcY  =  (  sy1  <  sy2  )  ?  sy1  :  sy2  ; 
int   dstX  =  (  dx1  <  dx2  )  ?  dx1  :  dx2  ; 
int   dstY  =  (  dy1  <  dy2  )  ?  dy1  :  dy2  ; 
if  (  srcWidth  >  width  )  { 
dstWidth  =  (  int  )  (  (  double  )  dstWidth  *  (  (  double  )  width  /  (  double  )  srcWidth  )  )  ; 
srcWidth  =  width  -  srcX  ; 
} 
if  (  srcHeight  >  height  )  { 
dstHeight  =  (  int  )  (  (  double  )  dstHeight  *  (  (  double  )  height  /  (  double  )  srcHeight  )  )  ; 
srcHeight  =  height  -  srcY  ; 
} 
if  (  srcWidth  +  srcX  >  width  )  { 
dstWidth  =  (  int  )  (  (  double  )  dstWidth  *  (  double  )  (  width  -  srcX  )  /  (  double  )  srcWidth  )  ; 
srcWidth  =  width  -  srcX  ; 
} 
if  (  srcHeight  +  srcY  >  height  )  { 
dstHeight  =  (  int  )  (  (  double  )  dstHeight  *  (  double  )  (  width  -  srcY  )  /  (  double  )  srcHeight  )  ; 
srcHeight  =  height  -  srcY  ; 
} 
if  (  srcWidth  <=  0  ||  srcHeight  <=  0  ||  dstWidth  <=  0  ||  dstHeight  <=  0  )  return   true  ; 
if  (  bgcolor  !=  null  )  drawPixelsScaledFlipped  (  g  ,  bgcolor  .  getRed  (  )  ,  bgcolor  .  getGreen  (  )  ,  bgcolor  .  getBlue  (  )  ,  flipX  ,  flipY  ,  srcX  ,  srcY  ,  srcWidth  ,  srcHeight  ,  dstX  ,  dstY  ,  dstWidth  ,  dstHeight  ,  true  )  ;  else   drawPixelsScaledFlipped  (  g  ,  0  ,  0  ,  0  ,  flipX  ,  flipY  ,  srcX  ,  srcY  ,  srcWidth  ,  srcHeight  ,  dstX  ,  dstY  ,  dstWidth  ,  dstHeight  ,  false  )  ; 
return   true  ; 
} 

public   native   void   copyArea  (  int   x  ,  int   y  ,  int   width  ,  int   height  ,  int   dx  ,  int   dy  )  ; 




private   void   deliver  (  )  { 
int   flags  =  ImageObserver  .  HEIGHT  |  ImageObserver  .  WIDTH  |  ImageObserver  .  PROPERTIES  |  ImageObserver  .  ALLBITS  ; 
if  (  observers  !=  null  )  for  (  int   i  =  0  ;  i  <  observers  .  size  (  )  ;  i  ++  )  (  (  ImageObserver  )  observers  .  elementAt  (  i  )  )  .  imageUpdate  (  this  ,  flags  ,  0  ,  0  ,  width  ,  height  )  ; 
observers  =  null  ; 
} 





private   boolean   addObserver  (  ImageObserver   observer  )  { 
if  (  !  isLoaded  )  { 
if  (  observer  !=  null  )  if  (  !  observers  .  contains  (  observer  )  )  observers  .  addElement  (  observer  )  ; 
return   true  ; 
} 
return   false  ; 
} 

public   String   toString  (  )  { 
return  "QtImage [isLoaded="  +  isLoaded  +  ", width="  +  width  +  ", height="  +  height  +  "]"  ; 
} 
} 

