package   org  .  photovault  .  dcraw  ; 

import   java  .  awt  .  RenderingHints  ; 
import   java  .  awt  .  Transparency  ; 
import   java  .  awt  .  color  .  ColorSpace  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  awt  .  image  .  ColorModel  ; 
import   java  .  awt  .  image  .  ComponentColorModel  ; 
import   java  .  awt  .  image  .  DataBuffer  ; 
import   java  .  awt  .  image  .  RenderedImage  ; 
import   java  .  awt  .  image  .  SampleModel  ; 
import   java  .  awt  .  image  .  WritableRaster  ; 
import   java  .  awt  .  image  .  renderable  .  ParameterBlock  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  text  .  DateFormat  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   javax  .  imageio  .  ImageReader  ; 
import   javax  .  imageio  .  stream  .  ImageInputStream  ; 
import   javax  .  media  .  jai  .  BorderExtender  ; 
import   javax  .  media  .  jai  .  Histogram  ; 
import   javax  .  media  .  jai  .  Interpolation  ; 
import   javax  .  media  .  jai  .  JAI  ; 
import   javax  .  media  .  jai  .  KernelJAI  ; 
import   javax  .  media  .  jai  .  LookupTableJAI  ; 
import   javax  .  media  .  jai  .  PlanarImage  ; 
import   javax  .  media  .  jai  .  RenderableOp  ; 
import   javax  .  media  .  jai  .  RenderedImageAdapter  ; 
import   javax  .  media  .  jai  .  RenderedOp  ; 
import   javax  .  media  .  jai  .  TiledImage  ; 
import   javax  .  media  .  jai  .  operator  .  BandCombineDescriptor  ; 
import   javax  .  media  .  jai  .  operator  .  HistogramDescriptor  ; 
import   javax  .  media  .  jai  .  operator  .  LookupDescriptor  ; 
import   javax  .  media  .  jai  .  operator  .  RenderableDescriptor  ; 
import   javax  .  media  .  jai  .  operator  .  ScaleDescriptor  ; 
import   org  .  photovault  .  common  .  PhotovaultException  ; 
import   org  .  photovault  .  image  .  PhotovaultImage  ; 






































public   class   RawImage   extends   PhotovaultImage  { 

static   org  .  apache  .  log4j  .  Logger   log  =  org  .  apache  .  log4j  .  Logger  .  getLogger  (  RawImage  .  class  .  getName  (  )  )  ; 




ColorProfileDesc   colorProfile  =  null  ; 




DCRawProcessWrapper   dcraw  =  null  ; 




File   f  =  null  ; 






PlanarImage   rawImage  =  null  ; 




RenderableOp   wbAdjustedRawImage  =  null  ; 




boolean   rawIsHalfSized  =  true  ; 






RenderableOp   correctedImage  =  null  ; 




boolean   validRawFile  =  false  ; 




int   white  =  0  ; 





double   evCorr  =  0  ; 




byte  [  ]  gammaLut  =  new   byte  [  0x10000  ]  ; 




private   Date   timestamp  =  null  ; 




private   String   camera  =  null  ; 




private   int   filmSpeed  =  -  1  ; 




private   double   shutterSpeed  =  0  ; 




private   double   aperture  =  0  ; 




private   double   focalLength  =  0  ; 




private   boolean   hasICCProfile  =  false  ; 




private   double   cameraMultipliers  [  ]  =  null  ; 





private   double   daylightMultipliers  [  ]  =  null  ; 





private   double   chanMultipliers  [  ]  =  null  ; 




private   double   ctemp  =  0.0  ; 




private   double   greenGain  =  1.0  ; 




private   int   black  =  0  ; 




private   int   width  ; 




private   int   height  ; 

private   int   histBins  [  ]  [  ]  ; 




public   boolean   isValidRawFile  (  )  { 
return   validRawFile  ; 
} 






public   Date   getTimestamp  (  )  { 
return  (  timestamp  !=  null  )  ?  (  Date  )  timestamp  .  clone  (  )  :  null  ; 
} 





public   String   getCamera  (  )  { 
return   camera  ; 
} 





public   void   setCamera  (  String   camera  )  { 
this  .  camera  =  camera  ; 
} 





public   int   getFilmSpeed  (  )  { 
return   filmSpeed  ; 
} 





public   double   getShutterSpeed  (  )  { 
return   shutterSpeed  ; 
} 





public   double   getAperture  (  )  { 
return   aperture  ; 
} 





public   double   getFocalLength  (  )  { 
return   focalLength  ; 
} 






public   boolean   isHasICCProfile  (  )  { 
return   hasICCProfile  ; 
} 





public   double  [  ]  getCameraMultipliers  (  )  { 
return  (  cameraMultipliers  !=  null  )  ?  cameraMultipliers  .  clone  (  )  :  null  ; 
} 






public   double  [  ]  getDaylightMultipliers  (  )  { 
return  (  daylightMultipliers  !=  null  )  ?  daylightMultipliers  .  clone  (  )  :  null  ; 
} 






public   void   setEvCorr  (  double   evCorr  )  { 
this  .  evCorr  =  evCorr  ; 
applyGammaLut  (  )  ; 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 





public   double   getEvCorr  (  )  { 
return   evCorr  ; 
} 






double   highlightCompression  =  0.0  ; 






public   void   setHighlightCompression  (  double   c  )  { 
highlightCompression  =  c  ; 
applyGammaLut  (  )  ; 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 





public   double   getHighlightCompression  (  )  { 
return   highlightCompression  ; 
} 

public   int  [  ]  [  ]  getHistogramBins  (  )  { 
return  (  histBins  !=  null  )  ?  histBins  .  clone  (  )  :  null  ; 
} 





public   RawImage  (  File   f  )  throws   PhotovaultException  { 
this  .  f  =  f  ; 
readFileInfo  (  )  ; 
} 





ArrayList   listeners  =  new   ArrayList  (  )  ; 





public   void   addChangeListener  (  RawImageChangeListener   l  )  { 
listeners  .  add  (  l  )  ; 
} 





public   void   removeChangeListener  (  RawImageChangeListener   l  )  { 
listeners  .  remove  (  l  )  ; 
} 





private   void   fireChangeEvent  (  RawImageChangeEvent   ev  )  { 
Iterator   iter  =  listeners  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
RawImageChangeListener   l  =  (  RawImageChangeListener  )  iter  .  next  (  )  ; 
l  .  rawImageSettingsChanged  (  ev  )  ; 
} 
} 

public   RenderedImage   getImage  (  )  { 
return   null  ; 
} 





public   RenderableOp   getCorrectedImage  (  int   minWidth  ,  int   minHeight  ,  boolean   isLowQualityAcceptable  )  { 
boolean   isHalfSizeEnough  =  (  minWidth  *  2  <=  width  )  &&  (  minHeight  *  2  <=  height  )  ; 
if  (  rawImage  ==  null  ||  (  rawIsHalfSized  &&  !  isHalfSizeEnough  )  )  { 
dcraw  .  setHalfSize  (  isHalfSizeEnough  )  ; 
loadRawImage  (  )  ; 
correctedImage  =  null  ; 
} 
if  (  correctedImage  ==  null  )  { 
createGammaLut  (  )  ; 
LookupTableJAI   jailut  =  new   LookupTableJAI  (  gammaLut  )  ; 
correctedImage  =  LookupDescriptor  .  createRenderable  (  wbAdjustedRawImage  ,  jailut  ,  null  )  ; 
ColorSpace   cs  =  ColorSpace  .  getInstance  (  ColorSpace  .  CS_sRGB  )  ; 
cm  =  new   ComponentColorModel  (  cs  ,  new   int  [  ]  {  8  ,  8  ,  8  }  ,  false  ,  false  ,  Transparency  .  OPAQUE  ,  DataBuffer  .  TYPE_BYTE  )  ; 
} 
return   correctedImage  ; 
} 












public   boolean   setMinimumPreferredSize  (  int   minWidth  ,  int   minHeight  )  { 
boolean   needsReload  =  (  correctedImage  ==  null  )  ; 
if  (  minWidth  *  2  >  width  ||  minHeight  *  2  >  height  )  { 
dcraw  .  setHalfSize  (  false  )  ; 
if  (  rawIsHalfSized  )  { 
needsReload  =  true  ; 
rawImage  =  null  ; 
correctedImage  =  null  ; 
} 
}  else  { 
dcraw  .  setHalfSize  (  true  )  ; 
} 
return   needsReload  ; 
} 




ColorModel   cm  =  null  ; 




SampleModel   sm  =  null  ; 




public   SampleModel   getCorrectedImageSampleModel  (  )  { 
return   sm  ; 
} 




public   ColorModel   getCorrectedImageColorModel  (  )  { 
return   cm  ; 
} 





private   void   loadRawImage  (  )  { 
if  (  dcraw  ==  null  )  { 
dcraw  =  new   DCRawProcessWrapper  (  )  ; 
} 
try  { 
if  (  colorProfile  !=  null  )  { 
dcraw  .  setIccProfile  (  colorProfile  .  getInstanceFile  (  )  )  ; 
}  else  { 
dcraw  .  setIccProfile  (  null  )  ; 
} 
if  (  this  .  chanMultipliers  ==  null  )  { 
chanMultipliers  =  cameraMultipliers  .  clone  (  )  ; 
calcCTemp  (  )  ; 
} 
double   dl  [  ]  =  new   double  [  4  ]  ; 
dl  [  0  ]  =  daylightMultipliers  [  0  ]  ; 
dl  [  1  ]  =  daylightMultipliers  [  1  ]  ; 
dl  [  2  ]  =  daylightMultipliers  [  2  ]  ; 
dl  [  3  ]  =  daylightMultipliers  [  1  ]  ; 
dcraw  .  setWbCoeffs  (  dl  )  ; 
InputStream   is  =  dcraw  .  getRawImageAsTiff  (  f  )  ; 
Iterator   readers  =  ImageIO  .  getImageReadersByFormatName  (  "TIFF"  )  ; 
ImageReader   reader  =  (  ImageReader  )  readers  .  next  (  )  ; 
log  .  debug  (  "Creating stream"  )  ; 
ImageInputStream   iis  =  ImageIO  .  createImageInputStream  (  is  )  ; 
reader  .  setInput  (  iis  ,  false  ,  false  )  ; 
BufferedImage   img  =  reader  .  read  (  0  )  ; 
WritableRaster   r  =  img  .  getRaster  (  )  ; 
ColorSpace   cs  =  ColorSpace  .  getInstance  (  ColorSpace  .  CS_LINEAR_RGB  )  ; 
ColorModel   targetCM  =  new   ComponentColorModel  (  cs  ,  new   int  [  ]  {  16  ,  16  ,  16  }  ,  false  ,  false  ,  Transparency  .  OPAQUE  ,  DataBuffer  .  TYPE_USHORT  )  ; 
rawImage  =  new   TiledImage  (  new   BufferedImage  (  targetCM  ,  r  ,  true  ,  null  )  ,  256  ,  256  )  ; 
final   float  [  ]  DEFAULT_KERNEL_1D  =  {  0.25f  ,  0.5f  ,  0.25f  }  ; 
ParameterBlock   pb  =  new   ParameterBlock  (  )  ; 
KernelJAI   kernel  =  new   KernelJAI  (  DEFAULT_KERNEL_1D  .  length  ,  DEFAULT_KERNEL_1D  .  length  ,  DEFAULT_KERNEL_1D  .  length  /  2  ,  DEFAULT_KERNEL_1D  .  length  /  2  ,  DEFAULT_KERNEL_1D  ,  DEFAULT_KERNEL_1D  )  ; 
pb  .  add  (  kernel  )  ; 
BorderExtender   extender  =  BorderExtender  .  createInstance  (  BorderExtender  .  BORDER_COPY  )  ; 
RenderingHints   hints  =  JAI  .  getDefaultInstance  (  )  .  getRenderingHints  (  )  ; 
if  (  hints  ==  null  )  { 
hints  =  new   RenderingHints  (  JAI  .  KEY_BORDER_EXTENDER  ,  extender  )  ; 
}  else  { 
hints  .  put  (  JAI  .  KEY_BORDER_EXTENDER  ,  extender  )  ; 
} 
RenderedOp   filter  =  new   RenderedOp  (  "convolve"  ,  pb  ,  hints  )  ; 
pb  =  new   ParameterBlock  (  )  ; 
pb  .  addSource  (  filter  )  ; 
pb  .  add  (  new   Float  (  0.5F  )  )  .  add  (  new   Float  (  0.5F  )  )  ; 
pb  .  add  (  new   Float  (  0.0F  )  )  .  add  (  new   Float  (  0.0F  )  )  ; 
pb  .  add  (  Interpolation  .  getInstance  (  Interpolation  .  INTERP_NEAREST  )  )  ; 
RenderedOp   downSampler  =  new   RenderedOp  (  "scale"  ,  pb  ,  null  )  ; 
RenderableOp   rawImageRenderable  =  RenderableDescriptor  .  createRenderable  (  rawImage  ,  downSampler  ,  null  ,  null  ,  null  ,  null  ,  null  )  ; 
double   colorCorrMat  [  ]  [  ]  =  new   double  [  ]  [  ]  {  {  colorCorr  [  0  ]  ,  0.0  ,  0.0  ,  0.0  }  ,  {  0.0  ,  colorCorr  [  1  ]  ,  0.0  ,  0.0  }  ,  {  0.0  ,  0.0  ,  colorCorr  [  2  ]  ,  0.0  }  }  ; 
wbAdjustedRawImage  =  BandCombineDescriptor  .  createRenderable  (  rawImageRenderable  ,  colorCorrMat  ,  null  )  ; 
reader  .  getImageMetadata  (  0  )  ; 
rawIsHalfSized  =  dcraw  .  ishalfSize  (  )  ; 
createHistogram  (  )  ; 
}  catch  (  FileNotFoundException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
}  catch  (  PhotovaultException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
if  (  autoExposeRequested  )  { 
doAutoExpose  (  )  ; 
} 
} 




private   void   createHistogram  (  )  { 
int   numBins  [  ]  =  {  65536  }  ; 
double   lowVal  [  ]  =  {  0.  }  ; 
double   highVal  [  ]  =  {  65535.  }  ; 
RenderedOp   histOp  =  HistogramDescriptor  .  create  (  rawImage  ,  null  ,  Integer  .  valueOf  (  1  )  ,  Integer  .  valueOf  (  1  )  ,  numBins  ,  lowVal  ,  highVal  ,  null  )  ; 
Histogram   hist  =  (  Histogram  )  histOp  .  getProperty  (  "histogram"  )  ; 
histBins  =  hist  .  getBins  (  )  ; 
} 




private   double   logAvg  =  0  ; 





private   boolean   autoExposeRequested  =  true  ; 








public   void   autoExpose  (  )  { 
autoExposeRequested  =  true  ; 
if  (  rawImage  !=  null  )  { 
doAutoExpose  (  )  ; 
}  else  { 
loadRawImage  (  )  ; 
} 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 

static   double   MAX_AUTO_HLIGHT_COMP  =  1.0  ; 

static   double   MIN_AUTO_HLIGHT_COMP  =  0.0  ; 




private   void   doAutoExpose  (  )  { 
autoExposeRequested  =  false  ; 
double   lumMat  [  ]  [  ]  =  {  {  0.27  ,  0.67  ,  0.06  ,  0.0  }  }  ; 
RenderedOp   lumImg  =  BandCombineDescriptor  .  create  (  rawImage  ,  lumMat  ,  null  )  ; 
int   numBins  [  ]  =  {  65536  }  ; 
double   lowVal  [  ]  =  {  0.  }  ; 
double   highVal  [  ]  =  {  65535.  }  ; 
RenderedOp   histOp  =  HistogramDescriptor  .  create  (  lumImg  ,  null  ,  Integer  .  valueOf  (  1  )  ,  Integer  .  valueOf  (  1  )  ,  numBins  ,  lowVal  ,  highVal  ,  null  )  ; 
Histogram   hist  =  (  Histogram  )  histOp  .  getProperty  (  "histogram"  )  ; 
int  [  ]  [  ]  histBins  =  hist  .  getBins  (  )  ; 
double   logSum  =  0.0  ; 
int   pixelCount  =  0  ; 
for  (  int   n  =  0  ;  n  <  histBins  [  0  ]  .  length  ;  n  ++  )  { 
double   l  =  Math  .  log1p  (  n  )  ; 
logSum  +=  l  *  histBins  [  0  ]  [  n  ]  ; 
pixelCount  +=  histBins  [  0  ]  [  n  ]  ; 
} 
double   dw  =  65536.  ; 
if  (  pixelCount  >  0  )  { 
logAvg  =  Math  .  exp  (  logSum  /  pixelCount  )  ; 
dw  =  logAvg  /  0.18  ; 
} 
int   whitePixels  =  pixelCount  /  100  ; 
int   brighterPixels  =  0  ; 
int   bin  =  0xffff  ; 
for  (  ;  bin  >  0  ;  bin  --  )  { 
brighterPixels  +=  histBins  [  0  ]  [  bin  ]  ; 
if  (  brighterPixels  >=  whitePixels  )  break  ; 
} 
double   hcRatio  =  (  (  double  )  bin  )  /  dw  ; 
white  =  (  int  )  dw  ; 
highlightCompression  =  Math  .  log  (  hcRatio  )  /  Math  .  log  (  2.0  )  ; 
highlightCompression  =  Math  .  min  (  MAX_AUTO_HLIGHT_COMP  ,  Math  .  max  (  MIN_AUTO_HLIGHT_COMP  ,  highlightCompression  )  )  ; 
} 






private   void   readFileInfo  (  )  throws   PhotovaultException  { 
if  (  dcraw  ==  null  )  { 
dcraw  =  new   DCRawProcessWrapper  (  )  ; 
} 
Map   values  =  null  ; 
try  { 
values  =  dcraw  .  getFileInfo  (  f  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   PhotovaultException  (  ex  .  getMessage  (  )  ,  ex  )  ; 
} 
if  (  values  .  containsKey  (  "Decodable with dcraw"  )  )  { 
if  (  values  .  get  (  "Decodable with dcraw"  )  .  equals  (  "yes"  )  )  { 
validRawFile  =  true  ; 
}  else  { 
return  ; 
} 
} 
if  (  values  .  containsKey  (  "Camera"  )  )  { 
camera  =  (  String  )  values  .  get  (  "Camera"  )  ; 
} 
if  (  values  .  containsKey  (  "ISO speed"  )  )  { 
String   isoStr  =  (  String  )  values  .  get  (  "ISO speed"  )  ; 
try  { 
filmSpeed  =  Integer  .  parseInt  (  isoStr  .  trim  (  )  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
if  (  values  .  containsKey  (  "Shutter"  )  )  { 
String   shutterStr  =  (  String  )  values  .  get  (  "Shutter"  )  ; 
shutterStr  =  shutterStr  .  replaceAll  (  "sec"  ,  ""  )  ; 
String   fraction  [  ]  =  shutterStr  .  split  (  "/"  )  ; 
try  { 
if  (  fraction  .  length  ==  1  )  { 
shutterSpeed  =  Double  .  parseDouble  (  fraction  [  0  ]  .  trim  (  )  )  ; 
}  else  { 
shutterSpeed  =  Double  .  parseDouble  (  fraction  [  0  ]  .  trim  (  )  )  /  Double  .  parseDouble  (  fraction  [  1  ]  .  trim  (  )  )  ; 
} 
}  catch  (  NumberFormatException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
if  (  values  .  containsKey  (  "Output size"  )  )  { 
String   value  =  (  String  )  values  .  get  (  "Output size"  )  ; 
String   dims  [  ]  =  value  .  split  (  "x"  )  ; 
if  (  dims  .  length  ==  2  )  { 
try  { 
width  =  Integer  .  parseInt  (  dims  [  0  ]  .  trim  (  )  )  ; 
height  =  Integer  .  parseInt  (  dims  [  1  ]  .  trim  (  )  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  values  .  containsKey  (  "Timestamp"  )  )  { 
String   tsStr  =  (  String  )  values  .  get  (  "Timestamp"  )  ; 
DateFormat   df  =  new   SimpleDateFormat  (  "EEE MMM d HH:mm:ss yyyy"  ,  Locale  .  US  )  ; 
try  { 
timestamp  =  df  .  parse  (  tsStr  )  ; 
}  catch  (  ParseException   ex  )  { 
log  .  error  (  ex  .  getMessage  (  )  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
} 
if  (  values  .  containsKey  (  "Embedded ICC profile"  )  )  { 
String   str  =  (  String  )  values  .  get  (  "Embedded ICC profile"  )  ; 
this  .  hasICCProfile  =  str  .  trim  (  )  .  equals  (  "yes"  )  ; 
} 
if  (  values  .  containsKey  (  "Aperture"  )  )  { 
String   str  =  (  String  )  values  .  get  (  "Aperture"  )  ; 
int   apertureStart  =  str  .  indexOf  (  "f/"  )  +  2  ; 
if  (  apertureStart  >=  2  &&  apertureStart  <  str  .  length  (  )  )  { 
try  { 
aperture  =  Double  .  parseDouble  (  str  .  substring  (  apertureStart  )  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
log  .  error  (  ex  .  getMessage  (  )  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  values  .  containsKey  (  "Focal Length"  )  )  { 
String   value  =  (  String  )  values  .  get  (  "Focal Length"  )  ; 
int   numberEnd  =  value  .  indexOf  (  "mm"  )  ; 
if  (  numberEnd  >=  0  )  { 
value  =  value  .  substring  (  0  ,  numberEnd  )  .  trim  (  )  ; 
try  { 
focalLength  =  Double  .  parseDouble  (  value  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  values  .  containsKey  (  "Daylight multipliers"  )  )  { 
String   value  =  (  String  )  values  .  get  (  "Daylight multipliers"  )  ; 
String   mstr  [  ]  =  value  .  split  (  " "  )  ; 
daylightMultipliers  =  new   double  [  mstr  .  length  ]  ; 
for  (  int   n  =  0  ;  n  <  mstr  .  length  ;  n  ++  )  { 
try  { 
daylightMultipliers  [  n  ]  =  Double  .  parseDouble  (  mstr  [  n  ]  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
log  .  error  (  ex  .  getMessage  (  )  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  values  .  containsKey  (  "Camera multipliers"  )  )  { 
String   value  =  (  String  )  values  .  get  (  "Camera multipliers"  )  ; 
String   mstr  [  ]  =  value  .  split  (  " "  )  ; 
cameraMultipliers  =  new   double  [  mstr  .  length  ]  ; 
for  (  int   n  =  0  ;  n  <  mstr  .  length  ;  n  ++  )  { 
try  { 
cameraMultipliers  [  n  ]  =  Double  .  parseDouble  (  mstr  [  n  ]  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
log  .  error  (  ex  .  getMessage  (  )  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
} 
}  else   if  (  daylightMultipliers  !=  null  )  { 
cameraMultipliers  =  new   double  [  4  ]  ; 
cameraMultipliers  [  0  ]  =  daylightMultipliers  [  0  ]  ; 
cameraMultipliers  [  1  ]  =  daylightMultipliers  [  1  ]  ; 
cameraMultipliers  [  2  ]  =  daylightMultipliers  [  2  ]  ; 
cameraMultipliers  [  3  ]  =  daylightMultipliers  [  1  ]  ; 
}  else  { 
validRawFile  =  false  ; 
} 
} 





private   void   createGammaLut  (  )  { 
double   dw  =  white  ; 
double   exposureMult  =  Math  .  pow  (  2  ,  evCorr  )  ; 
for  (  int   n  =  0  ;  n  <  gammaLut  .  length  ;  n  ++  )  { 
double   r  =  exposureMult  *  (  (  double  )  (  n  -  black  )  )  /  dw  ; 
double   whiteLum  =  Math  .  pow  (  2  ,  highlightCompression  )  ; 
r  =  (  r  *  (  1  +  (  r  /  (  whiteLum  *  whiteLum  )  )  )  )  /  (  1  +  r  )  ; 
double   val  =  (  r  <=  0.018  )  ?  r  *  4.5  :  Math  .  pow  (  r  ,  0.45  )  *  1.099  -  0.099  ; 
if  (  val  >  1.  )  { 
val  =  1.  ; 
}  else   if  (  val  <  0.  )  { 
val  =  0.  ; 
} 
int   intVal  =  (  int  )  (  val  *  256.  )  ; 
if  (  intVal  >  255  )  { 
intVal  =  255  ; 
} 
gammaLut  [  n  ]  =  (  byte  )  (  intVal  )  ; 
} 
} 

private   void   applyGammaLut  (  )  { 
createGammaLut  (  )  ; 
LookupTableJAI   jailut  =  new   LookupTableJAI  (  gammaLut  )  ; 
if  (  correctedImage  !=  null  )  { 
correctedImage  .  setParameter  (  jailut  ,  0  )  ; 
} 
} 





public   int   getWidth  (  )  { 
return   width  ; 
} 





public   int   getHeight  (  )  { 
return   height  ; 
} 

public   byte  [  ]  getGammaLut  (  )  { 
if  (  gammaLut  ==  null  )  { 
createGammaLut  (  )  ; 
} 
return  (  gammaLut  !=  null  )  ?  gammaLut  .  clone  (  )  :  null  ; 
} 

static   final   double   XYZ_to_RGB  [  ]  [  ]  =  {  {  3.24071  ,  -  0.969258  ,  0.0556352  }  ,  {  -  1.53726  ,  1.87599  ,  -  0.203996  }  ,  {  -  0.498571  ,  0.0415557  ,  1.05707  }  }  ; 







public   double  [  ]  colorTempToRGB  (  double   T  )  { 
int   c  ; 
double   xD  ,  yD  ,  X  ,  Y  ,  Z  ,  max  ; 
double   RGB  [  ]  =  new   double  [  3  ]  ; 
if  (  T  <=  4000  )  { 
xD  =  0.27475e9  /  (  T  *  T  *  T  )  -  0.98598e6  /  (  T  *  T  )  +  1.17444e3  /  T  +  0.145986  ; 
}  else   if  (  T  <=  7000  )  { 
xD  =  -  4.6070e9  /  (  T  *  T  *  T  )  +  2.9678e6  /  (  T  *  T  )  +  0.09911e3  /  T  +  0.244063  ; 
}  else  { 
xD  =  -  2.0064e9  /  (  T  *  T  *  T  )  +  1.9018e6  /  (  T  *  T  )  +  0.24748e3  /  T  +  0.237040  ; 
} 
yD  =  -  3  *  xD  *  xD  +  2.87  *  xD  -  0.275  ; 
X  =  xD  /  yD  ; 
Y  =  1  ; 
Z  =  (  1  -  xD  -  yD  )  /  yD  ; 
max  =  0  ; 
for  (  c  =  0  ;  c  <  3  ;  c  ++  )  { 
RGB  [  c  ]  =  X  *  XYZ_to_RGB  [  0  ]  [  c  ]  +  Y  *  XYZ_to_RGB  [  1  ]  [  c  ]  +  Z  *  XYZ_to_RGB  [  2  ]  [  c  ]  ; 
if  (  RGB  [  c  ]  >  max  )  max  =  RGB  [  c  ]  ; 
} 
for  (  c  =  0  ;  c  <  3  ;  c  ++  )  RGB  [  c  ]  =  RGB  [  c  ]  /  max  ; 
return   RGB  ; 
} 







double  [  ]  rgbToColorTemp  (  double   rgb  [  ]  )  { 
double   Tmax  ; 
double   Tmin  ; 
double   testRGB  [  ]  =  null  ; 
Tmin  =  2000  ; 
Tmax  =  12000  ; 
double   T  ; 
for  (  T  =  (  Tmax  +  Tmin  )  /  2  ;  Tmax  -  Tmin  >  10  ;  T  =  (  Tmax  +  Tmin  )  /  2  )  { 
testRGB  =  colorTempToRGB  (  T  )  ; 
if  (  testRGB  [  2  ]  /  testRGB  [  0  ]  >  rgb  [  2  ]  /  rgb  [  0  ]  )  Tmax  =  T  ;  else   Tmin  =  T  ; 
} 
double   green  =  (  testRGB  [  1  ]  /  testRGB  [  0  ]  )  /  (  rgb  [  1  ]  /  rgb  [  0  ]  )  ; 
double   result  [  ]  =  {  T  ,  green  }  ; 
return   result  ; 
} 








double   colorCorr  [  ]  =  new   double  [  ]  {  1.0  ,  1.0  ,  1.0  }  ; 





public   void   setColorTemp  (  double   T  )  { 
ctemp  =  T  ; 
applyWbCorrection  (  )  ; 
double   rgb  [  ]  =  colorTempToRGB  (  T  )  ; 
chanMultipliers  =  new   double  [  4  ]  ; 
chanMultipliers  [  0  ]  =  daylightMultipliers  [  0  ]  /  rgb  [  0  ]  ; 
chanMultipliers  [  1  ]  =  daylightMultipliers  [  1  ]  /  rgb  [  1  ]  *  greenGain  ; 
chanMultipliers  [  2  ]  =  daylightMultipliers  [  2  ]  /  rgb  [  2  ]  ; 
chanMultipliers  [  3  ]  =  chanMultipliers  [  1  ]  ; 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 





public   void   setGreenGain  (  double   g  )  { 
greenGain  =  g  ; 
applyWbCorrection  (  )  ; 
rawSettings  =  null  ; 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 

private   void   applyWbCorrection  (  )  { 
double   rgb  [  ]  =  colorTempToRGB  (  ctemp  )  ; 
colorCorr  =  new   double  [  3  ]  ; 
colorCorr  [  0  ]  =  1.0  /  rgb  [  0  ]  ; 
colorCorr  [  1  ]  =  greenGain  /  rgb  [  1  ]  ; 
colorCorr  [  2  ]  =  1.0  /  rgb  [  2  ]  ; 
double   colorCorrMat  [  ]  [  ]  =  new   double  [  ]  [  ]  {  {  colorCorr  [  0  ]  ,  0.0  ,  0.0  ,  0.0  }  ,  {  0.0  ,  colorCorr  [  1  ]  ,  0.0  ,  0.0  }  ,  {  0.0  ,  0.0  ,  colorCorr  [  2  ]  ,  0.0  }  }  ; 
if  (  wbAdjustedRawImage  !=  null  )  { 
wbAdjustedRawImage  .  setParameter  (  colorCorrMat  ,  0  )  ; 
} 
} 





public   double   getColorTemp  (  )  { 
return   ctemp  ; 
} 

public   double   getGreenGain  (  )  { 
return   greenGain  ; 
} 

void   calcCTemp  (  )  { 
if  (  chanMultipliers  ==  null  )  { 
chanMultipliers  =  cameraMultipliers  .  clone  (  )  ; 
} 
double   rgb  [  ]  =  {  daylightMultipliers  [  0  ]  /  chanMultipliers  [  0  ]  ,  daylightMultipliers  [  1  ]  /  chanMultipliers  [  1  ]  ,  daylightMultipliers  [  2  ]  /  chanMultipliers  [  2  ]  }  ; 
double   ct  [  ]  =  rgbToColorTemp  (  rgb  )  ; 
ctemp  =  ct  [  0  ]  ; 
greenGain  =  ct  [  1  ]  ; 
} 





public   RawConversionSettings   getRawSettings  (  )  { 
RawSettingsFactory   f  =  new   RawSettingsFactory  (  null  )  ; 
f  .  setDaylightMultipliers  (  daylightMultipliers  )  ; 
f  .  setRedGreenRation  (  chanMultipliers  [  0  ]  /  chanMultipliers  [  1  ]  )  ; 
f  .  setBlueGreenRatio  (  chanMultipliers  [  2  ]  /  chanMultipliers  [  1  ]  )  ; 
f  .  setBlack  (  black  )  ; 
f  .  setWhite  (  white  )  ; 
f  .  setEvCorr  (  evCorr  )  ; 
f  .  setHlightComp  (  highlightCompression  )  ; 
f  .  setUseEmbeddedProfile  (  hasICCProfile  )  ; 
f  .  setColorProfile  (  colorProfile  )  ; 
RawConversionSettings   s  =  null  ; 
try  { 
s  =  f  .  create  (  )  ; 
}  catch  (  PhotovaultException   ex  )  { 
log  .  error  (  "Error while reacting raw settings object: "  +  ex  .  getMessage  (  )  )  ; 
} 
return   s  ; 
} 





RawConversionSettings   rawSettings  =  null  ; 





public   void   setRawSettings  (  RawConversionSettings   s  )  { 
if  (  s  ==  null  )  { 
log  .  error  (  "null raw settings"  )  ; 
return  ; 
} 
if  (  rawSettings  !=  null  &&  rawSettings  .  equals  (  s  )  )  { 
return  ; 
} 
rawSettings  =  s  ; 
if  (  chanMultipliers  ==  null  ||  Math  .  abs  (  daylightMultipliers  [  0  ]  -  s  .  getDaylightRedGreenRatio  (  )  )  >  0.001  ||  Math  .  abs  (  daylightMultipliers  [  2  ]  -  s  .  getDaylightBlueGreenRatio  (  )  )  >  0.001  )  { 
daylightMultipliers  =  new   double  [  3  ]  ; 
daylightMultipliers  [  0  ]  =  s  .  getDaylightRedGreenRatio  (  )  ; 
daylightMultipliers  [  1  ]  =  1.  ; 
daylightMultipliers  [  2  ]  =  s  .  getDaylightBlueGreenRatio  (  )  ; 
correctedImage  =  null  ; 
rawImage  =  null  ; 
} 
chanMultipliers  =  new   double  [  4  ]  ; 
chanMultipliers  [  0  ]  =  s  .  getRedGreenRatio  (  )  ; 
chanMultipliers  [  1  ]  =  1.  ; 
chanMultipliers  [  2  ]  =  s  .  getBlueGreenRatio  (  )  ; 
chanMultipliers  [  3  ]  =  1.  ; 
calcCTemp  (  )  ; 
applyWbCorrection  (  )  ; 
evCorr  =  s  .  getEvCorr  (  )  ; 
highlightCompression  =  s  .  getHighlightCompression  (  )  ; 
white  =  s  .  getWhite  (  )  ; 
black  =  s  .  getBlack  (  )  ; 
applyGammaLut  (  )  ; 
hasICCProfile  =  s  .  getUseEmbeddedICCProfile  (  )  ; 
colorProfile  =  s  .  getColorProfile  (  )  ; 
autoExposeRequested  =  false  ; 
fireChangeEvent  (  new   RawImageChangeEvent  (  this  )  )  ; 
} 
} 

