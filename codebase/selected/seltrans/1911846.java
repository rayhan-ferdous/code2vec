package   gov  .  nasa  .  worldwind  .  formats  .  shapefile  ; 

import   com  .  sun  .  opengl  .  util  .  BufferUtil  ; 
import   gov  .  nasa  .  worldwind  .  Exportable  ; 
import   gov  .  nasa  .  worldwind  .  avlist  .  *  ; 
import   gov  .  nasa  .  worldwind  .  exception  .  *  ; 
import   gov  .  nasa  .  worldwind  .  formats  .  worldfile  .  WorldFile  ; 
import   gov  .  nasa  .  worldwind  .  geom  .  *  ; 
import   gov  .  nasa  .  worldwind  .  ogc  .  kml  .  KMLConstants  ; 
import   gov  .  nasa  .  worldwind  .  util  .  *  ; 
import   javax  .  xml  .  stream  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  logging  .  Level  ; 


































































































public   class   Shapefile   extends   AVListImpl   implements   Closeable  ,  Exportable  { 

protected   static   final   int   FILE_CODE  =  0x0000270A  ; 

protected   static   final   int   HEADER_LENGTH  =  100  ; 

protected   static   final   String   SHAPE_FILE_SUFFIX  =  ".shp"  ; 

protected   static   final   String   INDEX_FILE_SUFFIX  =  ".shx"  ; 

protected   static   final   String   ATTRIBUTE_FILE_SUFFIX  =  ".dbf"  ; 

protected   static   final   String   PROJECTION_FILE_SUFFIX  =  ".prj"  ; 

protected   static   final   String  [  ]  SHAPE_CONTENT_TYPES  =  {  "application/shp"  ,  "application/octet-stream"  }  ; 

protected   static   final   String  [  ]  INDEX_CONTENT_TYPES  =  {  "application/shx"  ,  "application/octet-stream"  }  ; 

protected   static   final   String  [  ]  PROJECTION_CONTENT_TYPES  =  {  "application/prj"  ,  "application/octet-stream"  ,  "text/plain"  }  ; 

public   static   final   String   SHAPE_NULL  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapeNull"  ; 

public   static   final   String   SHAPE_POINT  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePoint"  ; 

public   static   final   String   SHAPE_MULTI_POINT  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapeMultiPoint"  ; 

public   static   final   String   SHAPE_POLYLINE  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolyline"  ; 

public   static   final   String   SHAPE_POLYGON  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolygon"  ; 

public   static   final   String   SHAPE_POINT_M  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePointM"  ; 

public   static   final   String   SHAPE_MULTI_POINT_M  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapeMultiPointM"  ; 

public   static   final   String   SHAPE_POLYLINE_M  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolylineM"  ; 

public   static   final   String   SHAPE_POLYGON_M  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolygonM"  ; 

public   static   final   String   SHAPE_POINT_Z  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePointZ"  ; 

public   static   final   String   SHAPE_MULTI_POINT_Z  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapeMultiPointZ"  ; 

public   static   final   String   SHAPE_POLYLINE_Z  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolylineZ"  ; 

public   static   final   String   SHAPE_POLYGON_Z  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapePolygonZ"  ; 

public   static   final   String   SHAPE_MULTI_PATCH  =  "gov.nasa.worldwind.formats.shapefile.Shapefile.ShapeMultiPatch"  ; 

protected   static   List  <  String  >  measureTypes  =  new   ArrayList  <  String  >  (  Arrays  .  asList  (  Shapefile  .  SHAPE_POINT_M  ,  Shapefile  .  SHAPE_POINT_Z  ,  Shapefile  .  SHAPE_MULTI_POINT_M  ,  Shapefile  .  SHAPE_MULTI_POINT_Z  ,  Shapefile  .  SHAPE_POLYLINE_M  ,  Shapefile  .  SHAPE_POLYLINE_Z  ,  Shapefile  .  SHAPE_POLYGON_M  ,  Shapefile  .  SHAPE_POLYGON_Z  )  )  ; 

protected   static   List  <  String  >  zTypes  =  new   ArrayList  <  String  >  (  Arrays  .  asList  (  Shapefile  .  SHAPE_POINT_Z  ,  Shapefile  .  SHAPE_MULTI_POINT_Z  ,  Shapefile  .  SHAPE_POLYLINE_Z  ,  Shapefile  .  SHAPE_POLYGON_Z  )  )  ; 

protected   static   class   Header  { 

public   int   fileCode  =  FILE_CODE  ; 

public   int   fileLength  ; 

public   int   version  ; 

public   String   shapeType  ; 

public   double  [  ]  boundingRectangle  ; 

public   boolean   normalizePoints  ; 
} 

protected   Header   header  ; 

protected   int  [  ]  index  ; 

protected   CompoundVecBuffer   pointBuffer  ; 

protected   ReadableByteChannel   shpChannel  ; 

protected   ReadableByteChannel   shxChannel  ; 

protected   ReadableByteChannel   prjChannel  ; 

protected   DBaseFile   attributeFile  ; 

protected   boolean   open  ; 






protected   boolean   normalizePoints  ; 

protected   int   numRecordsRead  ; 

protected   int   numBytesRead  ; 

protected   ByteBuffer   recordHeaderBuffer  ; 

protected   ByteBuffer   recordContentBuffer  ; 

protected   MappedByteBuffer   mappedShpBuffer  ; 





















public   Shapefile  (  Object   source  ,  AVList   params  )  { 
if  (  source  ==  null  ||  WWUtil  .  isEmpty  (  source  )  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.SourceIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
this  .  setValue  (  AVKey  .  DISPLAY_NAME  ,  source  .  toString  (  )  )  ; 
if  (  source   instanceof   File  )  this  .  initializeFromFile  (  (  File  )  source  ,  params  )  ;  else   if  (  source   instanceof   URL  )  this  .  initializeFromURL  (  (  URL  )  source  ,  params  )  ;  else   if  (  source   instanceof   InputStream  )  this  .  initializeFromStreams  (  (  InputStream  )  source  ,  null  ,  null  ,  null  ,  params  )  ;  else   if  (  source   instanceof   String  )  this  .  initializeFromPath  (  (  String  )  source  ,  params  )  ;  else  { 
String   message  =  Logging  .  getMessage  (  "generic.UnrecognizedSourceType"  ,  source  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
}  catch  (  Exception   e  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToReadShapefile"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ; 
Logging  .  logger  (  )  .  log  (  Level  .  SEVERE  ,  message  ,  e  )  ; 
throw   new   WWRuntimeException  (  message  ,  e  )  ; 
} 
} 



















public   Shapefile  (  Object   source  )  { 
this  (  source  ,  (  (  AVList  )  null  )  )  ; 
} 




















public   Shapefile  (  InputStream   shpStream  ,  InputStream   shxStream  ,  InputStream   dbfStream  ,  InputStream   prjStream  ,  AVList   params  )  { 
if  (  shpStream  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.InputStreamIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
this  .  setValue  (  AVKey  .  DISPLAY_NAME  ,  shpStream  .  toString  (  )  )  ; 
this  .  initializeFromStreams  (  shpStream  ,  shxStream  ,  dbfStream  ,  prjStream  ,  params  )  ; 
}  catch  (  Exception   e  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToReadShapefile"  ,  shpStream  )  ; 
Logging  .  logger  (  )  .  log  (  Level  .  SEVERE  ,  message  ,  e  )  ; 
throw   new   WWRuntimeException  (  message  ,  e  )  ; 
} 
} 



















public   Shapefile  (  InputStream   shpStream  ,  InputStream   shxStream  ,  InputStream   dbfStream  ,  InputStream   prjStream  )  { 
this  (  shpStream  ,  shxStream  ,  dbfStream  ,  prjStream  ,  null  )  ; 
} 



















public   Shapefile  (  InputStream   shpStream  ,  InputStream   shxStream  ,  InputStream   dbfStream  ,  AVList   params  )  { 
this  (  shpStream  ,  shxStream  ,  dbfStream  ,  null  ,  params  )  ; 
} 















public   Shapefile  (  InputStream   shpStream  ,  InputStream   shxStream  ,  InputStream   dbfStream  )  { 
this  (  shpStream  ,  shxStream  ,  dbfStream  ,  null  ,  null  )  ; 
} 






public   int   getVersion  (  )  { 
return   this  .  header  !=  null  ?  this  .  header  .  version  :  -  1  ; 
} 






public   int   getLength  (  )  { 
return   this  .  header  !=  null  ?  this  .  header  .  fileLength  :  -  1  ; 
} 























public   String   getShapeType  (  )  { 
return   this  .  header  !=  null  ?  this  .  header  .  shapeType  :  null  ; 
} 









public   double  [  ]  getBoundingRectangle  (  )  { 
return   this  .  header  !=  null  ?  this  .  header  .  boundingRectangle  :  null  ; 
} 






public   int   getNumberOfRecords  (  )  { 
return   this  .  index  !=  null  ?  this  .  index  .  length  /  2  :  -  1  ; 
} 






public   CompoundVecBuffer   getPointBuffer  (  )  { 
return   this  .  pointBuffer  ; 
} 







public   boolean   hasNext  (  )  { 
if  (  !  this  .  open  ||  this  .  header  ==  null  )  return   false  ; 
int   contentLength  =  this  .  header  .  fileLength  -  HEADER_LENGTH  ; 
return   this  .  numBytesRead  <  contentLength  ; 
} 




















public   ShapefileRecord   nextRecord  (  )  { 
if  (  !  this  .  open  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.ShapefileClosed"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
if  (  this  .  header  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.HeaderIsNull"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
int   contentLength  =  this  .  header  .  fileLength  -  HEADER_LENGTH  ; 
if  (  contentLength  <=  0  ||  this  .  numBytesRead  >=  contentLength  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.NoRecords"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalStateException  (  message  )  ; 
} 
ShapefileRecord   record  ; 
try  { 
record  =  this  .  readNextRecord  (  )  ; 
}  catch  (  Exception   e  )  { 
String   message  =  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToReadShapefileRecord"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ; 
Logging  .  logger  (  )  .  log  (  Level  .  SEVERE  ,  message  ,  e  )  ; 
throw   new   WWRuntimeException  (  message  ,  e  )  ; 
} 
this  .  numRecordsRead  ++  ; 
return   record  ; 
} 











public   void   close  (  )  { 
if  (  this  .  shpChannel  !=  null  )  { 
WWIO  .  closeStream  (  this  .  shpChannel  ,  null  )  ; 
this  .  shpChannel  =  null  ; 
} 
if  (  this  .  shxChannel  !=  null  )  { 
WWIO  .  closeStream  (  this  .  shxChannel  ,  null  )  ; 
this  .  shxChannel  =  null  ; 
} 
if  (  this  .  prjChannel  !=  null  )  { 
WWIO  .  closeStream  (  this  .  prjChannel  ,  null  )  ; 
this  .  prjChannel  =  null  ; 
} 
if  (  this  .  attributeFile  !=  null  )  { 
this  .  attributeFile  .  close  (  )  ; 
this  .  attributeFile  =  null  ; 
} 
this  .  recordHeaderBuffer  =  null  ; 
this  .  recordContentBuffer  =  null  ; 
this  .  mappedShpBuffer  =  null  ; 
this  .  open  =  false  ; 
} 






protected   boolean   isNormalizePoints  (  )  { 
return   this  .  normalizePoints  ; 
} 







protected   void   setNormalizePoints  (  boolean   normalizePoints  )  { 
this  .  normalizePoints  =  normalizePoints  ; 
} 

protected   void   initializeFromFile  (  File   file  ,  AVList   params  )  throws   IOException  { 
if  (  !  file  .  exists  (  )  )  { 
String   message  =  Logging  .  getMessage  (  "generic.FileNotFound"  ,  file  .  getPath  (  )  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   FileNotFoundException  (  message  )  ; 
} 
if  (  file  .  canRead  (  )  &&  file  .  canWrite  (  )  )  { 
try  { 
this  .  mappedShpBuffer  =  WWIO  .  mapFile  (  file  ,  FileChannel  .  MapMode  .  PRIVATE  )  ; 
Logging  .  logger  (  )  .  finer  (  Logging  .  getMessage  (  "SHP.MemoryMappingEnabled"  ,  file  .  getPath  (  )  )  )  ; 
}  catch  (  IOException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToMemoryMap"  ,  file  .  getPath  (  )  )  ,  e  )  ; 
} 
} 
if  (  this  .  mappedShpBuffer  ==  null  )  this  .  shpChannel  =  Channels  .  newChannel  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
InputStream   shxStream  =  this  .  getFileStream  (  WWIO  .  replaceSuffix  (  file  .  getPath  (  )  ,  INDEX_FILE_SUFFIX  )  )  ; 
if  (  shxStream  !=  null  )  this  .  shxChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  shxStream  )  )  ; 
InputStream   prjStream  =  this  .  getFileStream  (  WWIO  .  replaceSuffix  (  file  .  getPath  (  )  ,  PROJECTION_FILE_SUFFIX  )  )  ; 
if  (  prjStream  !=  null  )  this  .  prjChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  prjStream  )  )  ; 
this  .  setValue  (  AVKey  .  DISPLAY_NAME  ,  file  .  getPath  (  )  )  ; 
this  .  initialize  (  params  )  ; 
File   dbfFile  =  new   File  (  WWIO  .  replaceSuffix  (  file  .  getPath  (  )  ,  ATTRIBUTE_FILE_SUFFIX  )  )  ; 
if  (  dbfFile  .  exists  (  )  )  { 
try  { 
this  .  attributeFile  =  new   DBaseFile  (  dbfFile  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 

protected   void   initializeFromURL  (  URL   url  ,  AVList   params  )  throws   IOException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
String   message  =  this  .  validateURLConnection  (  connection  ,  SHAPE_CONTENT_TYPES  )  ; 
if  (  message  !=  null  )  { 
throw   new   IOException  (  message  )  ; 
} 
this  .  shpChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  connection  .  getInputStream  (  )  )  )  ; 
URLConnection   shxConnection  =  this  .  getURLConnection  (  WWIO  .  replaceSuffix  (  url  .  toString  (  )  ,  INDEX_FILE_SUFFIX  )  )  ; 
if  (  shxConnection  !=  null  )  { 
message  =  this  .  validateURLConnection  (  shxConnection  ,  INDEX_CONTENT_TYPES  )  ; 
if  (  message  !=  null  )  Logging  .  logger  (  )  .  warning  (  message  )  ;  else  { 
InputStream   shxStream  =  this  .  getURLStream  (  shxConnection  )  ; 
if  (  shxStream  !=  null  )  this  .  shxChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  shxStream  )  )  ; 
} 
} 
URLConnection   prjConnection  =  this  .  getURLConnection  (  WWIO  .  replaceSuffix  (  url  .  toString  (  )  ,  PROJECTION_FILE_SUFFIX  )  )  ; 
if  (  prjConnection  !=  null  )  { 
message  =  this  .  validateURLConnection  (  prjConnection  ,  PROJECTION_CONTENT_TYPES  )  ; 
if  (  message  !=  null  )  Logging  .  logger  (  )  .  warning  (  message  )  ;  else  { 
InputStream   prjStream  =  this  .  getURLStream  (  prjConnection  )  ; 
if  (  prjStream  !=  null  )  this  .  prjChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  prjStream  )  )  ; 
} 
} 
this  .  setValue  (  AVKey  .  DISPLAY_NAME  ,  url  .  toString  (  )  )  ; 
this  .  initialize  (  params  )  ; 
URL   dbfURL  =  WWIO  .  makeURL  (  WWIO  .  replaceSuffix  (  url  .  toString  (  )  ,  ATTRIBUTE_FILE_SUFFIX  )  )  ; 
if  (  dbfURL  !=  null  )  { 
try  { 
this  .  attributeFile  =  new   DBaseFile  (  dbfURL  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 

protected   void   initializeFromStreams  (  InputStream   shpStream  ,  InputStream   shxStream  ,  InputStream   dbfStream  ,  InputStream   prjStream  ,  AVList   params  )  throws   IOException  { 
if  (  shpStream  !=  null  )  this  .  shpChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  shpStream  )  )  ; 
if  (  shxStream  !=  null  )  this  .  shxChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  shxStream  )  )  ; 
if  (  prjStream  !=  null  )  this  .  prjChannel  =  Channels  .  newChannel  (  WWIO  .  getBufferedInputStream  (  prjStream  )  )  ; 
this  .  initialize  (  params  )  ; 
if  (  dbfStream  !=  null  )  { 
try  { 
this  .  attributeFile  =  new   DBaseFile  (  dbfStream  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 

protected   void   initializeFromPath  (  String   path  ,  AVList   params  )  throws   IOException  { 
File   file  =  new   File  (  path  )  ; 
if  (  file  .  exists  (  )  )  { 
this  .  initializeFromFile  (  file  ,  params  )  ; 
return  ; 
} 
URL   url  =  WWIO  .  makeURL  (  path  )  ; 
if  (  url  !=  null  )  { 
this  .  initializeFromURL  (  url  ,  params  )  ; 
return  ; 
} 
String   message  =  Logging  .  getMessage  (  "generic.UnrecognizedSourceType"  ,  path  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 










protected   void   initialize  (  AVList   params  )  throws   IOException  { 
try  { 
AVList   csParams  =  this  .  readCoordinateSystem  (  )  ; 
if  (  csParams  !=  null  )  this  .  setValues  (  csParams  )  ; 
}  catch  (  IOException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToReadProjection"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ,  e  )  ; 
} 
if  (  params  !=  null  )  { 
this  .  setValues  (  params  )  ; 
} 
String   message  =  this  .  validateCoordinateSystem  (  this  )  ; 
if  (  message  !=  null  )  { 
throw   new   WWRuntimeException  (  message  )  ; 
} 
try  { 
this  .  index  =  this  .  readIndex  (  )  ; 
}  catch  (  IOException   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  Logging  .  getMessage  (  "SHP.ExceptionAttemptingToReadIndex"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ,  e  )  ; 
} 
this  .  header  =  this  .  readHeader  (  )  ; 
this  .  open  =  true  ; 
this  .  setNormalizePoints  (  this  .  header  .  normalizePoints  )  ; 
} 

protected   InputStream   getFileStream  (  String   path  )  { 
try  { 
return   new   FileInputStream  (  path  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 

protected   URLConnection   getURLConnection  (  String   urlString  )  { 
try  { 
URL   url  =  new   URL  (  urlString  )  ; 
return   url  .  openConnection  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 

protected   InputStream   getURLStream  (  URLConnection   connection  )  { 
try  { 
return   connection  .  getInputStream  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 

protected   String   validateURLConnection  (  URLConnection   connection  ,  String  [  ]  acceptedContentTypes  )  { 
try  { 
if  (  connection   instanceof   HttpURLConnection  &&  (  (  HttpURLConnection  )  connection  )  .  getResponseCode  (  )  !=  HttpURLConnection  .  HTTP_OK  )  { 
return   Logging  .  getMessage  (  "HTTP.ResponseCode"  ,  (  (  HttpURLConnection  )  connection  )  .  getResponseCode  (  )  ,  connection  .  getURL  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
return   Logging  .  getMessage  (  "URLRetriever.ErrorOpeningConnection"  ,  connection  .  getURL  (  )  )  ; 
} 
String   contentType  =  connection  .  getContentType  (  )  ; 
if  (  WWUtil  .  isEmpty  (  contentType  )  )  return   null  ; 
for  (  String   type  :  acceptedContentTypes  )  { 
if  (  contentType  .  trim  (  )  .  toLowerCase  (  )  .  startsWith  (  type  )  )  return   null  ; 
} 
return   Logging  .  getMessage  (  "HTTP.UnexpectedContentType"  ,  contentType  ,  Arrays  .  toString  (  acceptedContentTypes  )  )  ; 
} 








protected   Header   readHeader  (  )  throws   IOException  { 
ByteBuffer   buffer  ; 
if  (  this  .  mappedShpBuffer  !=  null  )  { 
buffer  =  this  .  mappedShpBuffer  ; 
}  else  { 
buffer  =  ByteBuffer  .  allocate  (  HEADER_LENGTH  )  ; 
WWIO  .  readChannelToBuffer  (  this  .  shpChannel  ,  buffer  )  ; 
} 
if  (  buffer  .  remaining  (  )  <  HEADER_LENGTH  )  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "generic.InvalidFileLength"  ,  buffer  .  remaining  (  )  )  )  ; 
} 
return   this  .  readHeaderFromBuffer  (  buffer  )  ; 
} 













protected   Header   readHeaderFromBuffer  (  ByteBuffer   buffer  )  throws   IOException  { 
Header   header  =  null  ; 
int   pos  =  buffer  .  position  (  )  ; 
try  { 
buffer  .  order  (  ByteOrder  .  BIG_ENDIAN  )  ; 
int   fileCode  =  buffer  .  getInt  (  )  ; 
if  (  fileCode  !=  FILE_CODE  )  { 
throw   new   WWUnrecognizedException  (  Logging  .  getMessage  (  "SHP.UnrecognizedShapefile"  ,  fileCode  )  )  ; 
} 
buffer  .  position  (  buffer  .  position  (  )  +  5  *  4  )  ; 
int   lengthInWords  =  buffer  .  getInt  (  )  ; 
buffer  .  order  (  ByteOrder  .  LITTLE_ENDIAN  )  ; 
int   version  =  buffer  .  getInt  (  )  ; 
int   type  =  buffer  .  getInt  (  )  ; 
BoundingRectangle   rect  =  this  .  readBoundingRectangle  (  buffer  )  ; 
String   shapeType  =  getShapeType  (  type  )  ; 
if  (  shapeType  ==  null  )  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "SHP.UnsupportedShapeType"  ,  type  )  )  ; 
} 
header  =  new   Header  (  )  ; 
header  .  fileLength  =  lengthInWords  *  2  ; 
header  .  version  =  version  ; 
header  .  shapeType  =  shapeType  ; 
header  .  boundingRectangle  =  rect  .  coords  ; 
header  .  normalizePoints  =  rect  .  isNormalized  ; 
}  finally  { 
buffer  .  position  (  pos  +  HEADER_LENGTH  )  ; 
} 
return   header  ; 
} 












protected   int  [  ]  readIndex  (  )  throws   IOException  { 
if  (  this  .  shxChannel  ==  null  )  return   null  ; 
ByteBuffer   buffer  =  ByteBuffer  .  allocate  (  HEADER_LENGTH  )  ; 
WWIO  .  readChannelToBuffer  (  this  .  shxChannel  ,  buffer  )  ; 
if  (  buffer  .  remaining  (  )  <  HEADER_LENGTH  )  return   null  ; 
Header   indexHeader  =  this  .  readHeaderFromBuffer  (  buffer  )  ; 
int   numRecords  =  (  indexHeader  .  fileLength  -  HEADER_LENGTH  )  /  8  ; 
int   numElements  =  2  *  numRecords  ; 
int   indexLength  =  8  *  numRecords  ; 
int  [  ]  array  ; 
try  { 
buffer  =  ByteBuffer  .  allocate  (  indexLength  )  ; 
array  =  new   int  [  numElements  ]  ; 
}  catch  (  OutOfMemoryError   e  )  { 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  Logging  .  getMessage  (  "SHP.OutOfMemoryAllocatingIndex"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ,  e  )  ; 
return   null  ; 
} 
buffer  .  order  (  ByteOrder  .  BIG_ENDIAN  )  ; 
WWIO  .  readChannelToBuffer  (  this  .  shxChannel  ,  buffer  )  ; 
buffer  .  asIntBuffer  (  )  .  get  (  array  )  ; 
for  (  int   i  =  0  ;  i  <  numElements  ;  i  ++  )  { 
array  [  i  ]  *=  2  ; 
} 
return   array  ; 
} 












protected   AVList   readCoordinateSystem  (  )  throws   IOException  { 
if  (  this  .  prjChannel  ==  null  )  return   null  ; 
String   text  =  WWIO  .  readChannelToString  (  this  .  prjChannel  ,  null  )  ; 
if  (  WWUtil  .  isEmpty  (  text  )  )  return   null  ; 
return   WorldFile  .  decodeOGCCoordinateSystemWKT  (  text  ,  null  )  ; 
} 









protected   String   validateCoordinateSystem  (  AVList   params  )  { 
Object   o  =  params  .  getValue  (  AVKey  .  COORDINATE_SYSTEM  )  ; 
if  (  !  this  .  hasKey  (  AVKey  .  COORDINATE_SYSTEM  )  )  { 
Logging  .  logger  (  )  .  warning  (  Logging  .  getMessage  (  "generic.UnspecifiedCoordinateSystem"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  )  ; 
return   null  ; 
}  else   if  (  AVKey  .  COORDINATE_SYSTEM_GEOGRAPHIC  .  equals  (  o  )  )  { 
return   null  ; 
}  else   if  (  AVKey  .  COORDINATE_SYSTEM_PROJECTED  .  equals  (  o  )  )  { 
return   this  .  validateProjection  (  params  )  ; 
}  else  { 
return   Logging  .  getMessage  (  "generic.UnsupportedCoordinateSystem"  ,  o  )  ; 
} 
} 









protected   String   validateProjection  (  AVList   params  )  { 
Object   proj  =  params  .  getValue  (  AVKey  .  PROJECTION_NAME  )  ; 
if  (  AVKey  .  PROJECTION_UTM  .  equals  (  proj  )  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
Object   o  =  params  .  getValue  (  AVKey  .  PROJECTION_ZONE  )  ; 
if  (  o  ==  null  )  sb  .  append  (  Logging  .  getMessage  (  "generic.ZoneIsMissing"  )  )  ;  else   if  (  !  (  o   instanceof   Integer  )  ||  (  (  Integer  )  o  )  <  1  ||  (  (  Integer  )  o  )  >  60  )  sb  .  append  (  Logging  .  getMessage  (  "generic.ZoneIsInvalid"  ,  o  )  )  ; 
o  =  params  .  getValue  (  AVKey  .  PROJECTION_HEMISPHERE  )  ; 
if  (  o  ==  null  )  sb  .  append  (  sb  .  length  (  )  >  0  ?  ", "  :  ""  )  .  append  (  Logging  .  getMessage  (  "generic.HemisphereIsMissing"  )  )  ;  else   if  (  !  o  .  equals  (  AVKey  .  NORTH  )  &&  !  o  .  equals  (  AVKey  .  SOUTH  )  )  sb  .  append  (  sb  .  length  (  )  >  0  ?  ", "  :  ""  )  .  append  (  Logging  .  getMessage  (  "generic.HemisphereIsInvalid"  ,  o  )  )  ; 
return   sb  .  length  (  )  >  0  ?  sb  .  toString  (  )  :  null  ; 
}  else  { 
return   Logging  .  getMessage  (  "generic.UnsupportedProjection"  ,  proj  )  ; 
} 
} 









protected   ShapefileRecord   readNextRecord  (  )  throws   IOException  { 
ByteBuffer   buffer  ; 
if  (  this  .  mappedShpBuffer  !=  null  )  { 
int   pos  =  this  .  mappedShpBuffer  .  position  (  )  ; 
this  .  mappedShpBuffer  .  order  (  ByteOrder  .  BIG_ENDIAN  )  ; 
int   contentLength  =  this  .  mappedShpBuffer  .  getInt  (  pos  +  4  )  *  2  ; 
int   recordLength  =  ShapefileRecord  .  RECORD_HEADER_LENGTH  +  contentLength  ; 
this  .  mappedShpBuffer  .  position  (  pos  )  ; 
this  .  mappedShpBuffer  .  limit  (  pos  +  recordLength  )  ; 
this  .  numBytesRead  +=  recordLength  ; 
buffer  =  this  .  mappedShpBuffer  ; 
}  else  { 
if  (  this  .  recordHeaderBuffer  ==  null  )  this  .  recordHeaderBuffer  =  ByteBuffer  .  allocate  (  ShapefileRecord  .  RECORD_HEADER_LENGTH  )  ; 
this  .  recordHeaderBuffer  .  clear  (  )  ; 
this  .  recordHeaderBuffer  .  order  (  ByteOrder  .  BIG_ENDIAN  )  ; 
WWIO  .  readChannelToBuffer  (  this  .  shpChannel  ,  this  .  recordHeaderBuffer  )  ; 
int   contentLength  =  this  .  recordHeaderBuffer  .  getInt  (  4  )  *  2  ; 
int   recordLength  =  ShapefileRecord  .  RECORD_HEADER_LENGTH  +  contentLength  ; 
if  (  this  .  recordContentBuffer  ==  null  ||  this  .  recordContentBuffer  .  capacity  (  )  <  recordLength  )  this  .  recordContentBuffer  =  ByteBuffer  .  allocate  (  recordLength  )  ; 
this  .  recordContentBuffer  .  limit  (  recordLength  )  ; 
this  .  recordContentBuffer  .  rewind  (  )  ; 
this  .  recordContentBuffer  .  put  (  this  .  recordHeaderBuffer  )  ; 
WWIO  .  readChannelToBuffer  (  this  .  shpChannel  ,  this  .  recordContentBuffer  )  ; 
this  .  numBytesRead  +=  recordLength  ; 
buffer  =  this  .  recordContentBuffer  ; 
} 
ShapefileRecord   record  ; 
try  { 
record  =  this  .  readRecordFromBuffer  (  buffer  )  ; 
}  finally  { 
if  (  this  .  mappedShpBuffer  !=  null  )  this  .  mappedShpBuffer  .  limit  (  this  .  mappedShpBuffer  .  capacity  (  )  )  ; 
} 
return   record  ; 
} 












protected   ShapefileRecord   readRecordFromBuffer  (  ByteBuffer   buffer  )  { 
ShapefileRecord   record  =  this  .  createRecord  (  buffer  )  ; 
if  (  record  !=  null  )  { 
if  (  this  .  attributeFile  !=  null  &&  this  .  attributeFile  .  hasNext  (  )  )  { 
record  .  setAttributes  (  this  .  attributeFile  .  nextRecord  (  )  )  ; 
} 
} 
return   record  ; 
} 





















protected   ShapefileRecord   createRecord  (  ByteBuffer   buffer  )  { 
String   shapeType  =  this  .  readRecordShapeType  (  buffer  )  ; 
if  (  isPointType  (  shapeType  )  )  { 
return   this  .  createPoint  (  buffer  )  ; 
}  else   if  (  isMultiPointType  (  shapeType  )  )  { 
return   this  .  createMultiPoint  (  buffer  )  ; 
}  else   if  (  isPolylineType  (  shapeType  )  )  { 
return   this  .  createPolyline  (  buffer  )  ; 
}  else   if  (  isPolygonType  (  shapeType  )  )  { 
return   this  .  createPolygon  (  buffer  )  ; 
}  else   if  (  isNullType  (  shapeType  )  )  { 
return   this  .  createNull  (  buffer  )  ; 
} 
return   null  ; 
} 











protected   ShapefileRecord   createNull  (  ByteBuffer   buffer  )  { 
return   new   ShapefileRecordNull  (  this  ,  buffer  )  ; 
} 











protected   ShapefileRecord   createPoint  (  ByteBuffer   buffer  )  { 
return   new   ShapefileRecordPoint  (  this  ,  buffer  )  ; 
} 












protected   ShapefileRecord   createMultiPoint  (  ByteBuffer   buffer  )  { 
return   new   ShapefileRecordMultiPoint  (  this  ,  buffer  )  ; 
} 











protected   ShapefileRecord   createPolyline  (  ByteBuffer   buffer  )  { 
return   new   ShapefileRecordPolyline  (  this  ,  buffer  )  ; 
} 











protected   ShapefileRecord   createPolygon  (  ByteBuffer   buffer  )  { 
return   new   ShapefileRecordPolygon  (  this  ,  buffer  )  ; 
} 








protected   String   readRecordShapeType  (  ByteBuffer   buffer  )  { 
buffer  .  order  (  ByteOrder  .  LITTLE_ENDIAN  )  ; 
int   type  =  buffer  .  getInt  (  buffer  .  position  (  )  +  2  *  4  )  ; 
String   shapeType  =  this  .  getShapeType  (  type  )  ; 
if  (  shapeType  ==  null  )  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "SHP.UnsupportedShapeType"  ,  type  )  )  ; 
} 
return   shapeType  ; 
} 








protected   String   getShapeType  (  int   type  )  { 
switch  (  type  )  { 
case   0  : 
return   SHAPE_NULL  ; 
case   1  : 
return   SHAPE_POINT  ; 
case   3  : 
return   SHAPE_POLYLINE  ; 
case   5  : 
return   SHAPE_POLYGON  ; 
case   8  : 
return   SHAPE_MULTI_POINT  ; 
case   11  : 
return   SHAPE_POINT_Z  ; 
case   13  : 
return   SHAPE_POLYLINE_Z  ; 
case   15  : 
return   SHAPE_POLYGON_Z  ; 
case   18  : 
return   SHAPE_MULTI_POINT_Z  ; 
case   21  : 
return   SHAPE_POINT_M  ; 
case   23  : 
return   SHAPE_POLYLINE_M  ; 
case   25  : 
return   SHAPE_POLYGON_M  ; 
case   28  : 
return   SHAPE_MULTI_POINT_M  ; 
default  : 
return   null  ; 
} 
} 













protected   int   addPoints  (  ShapefileRecord   record  ,  ByteBuffer   buffer  ,  int   numPoints  )  { 
DoubleBuffer   pointBuffer  ; 
int   pos  =  buffer  .  position  (  )  ; 
int   limit  =  buffer  .  position  (  )  +  2  *  WWBufferUtil  .  SIZEOF_DOUBLE  *  numPoints  ; 
try  { 
buffer  .  limit  (  limit  )  ; 
pointBuffer  =  this  .  readPoints  (  record  ,  buffer  )  ; 
}  finally  { 
buffer  .  clear  (  )  ; 
buffer  .  position  (  limit  )  ; 
} 
if  (  this  .  mappedShpBuffer  !=  null  )  { 
if  (  this  .  pointBuffer  ==  null  )  { 
ByteBuffer   buf  =  this  .  mappedShpBuffer  .  duplicate  (  )  ; 
buf  .  order  (  ByteOrder  .  LITTLE_ENDIAN  )  ; 
buf  .  clear  (  )  ; 
this  .  pointBuffer  =  new   VecBufferBlocks  (  2  ,  AVKey  .  FLOAT64  ,  buf  )  ; 
} 
return  (  (  VecBufferBlocks  )  this  .  pointBuffer  )  .  addBlock  (  pos  ,  limit  -  1  )  ; 
}  else  { 
if  (  this  .  pointBuffer  ==  null  )  { 
int   totalPointsEstimate  =  this  .  computeNumberOfPointsEstimate  (  )  ; 
DoubleBuffer   doubleBuffer  ; 
try  { 
doubleBuffer  =  BufferUtil  .  newDoubleBuffer  (  2  *  totalPointsEstimate  )  ; 
}  catch  (  OutOfMemoryError   e  )  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "SHP.OutOfMemoryAllocatingPointBuffer"  ,  this  .  getValue  (  AVKey  .  DISPLAY_NAME  )  )  ,  e  )  ; 
} 
this  .  pointBuffer  =  new   VecBufferSequence  (  new   VecBuffer  (  2  ,  new   BufferWrapper  .  DoubleBufferWrapper  (  doubleBuffer  )  )  )  ; 
} 
VecBuffer   vecBuffer  =  new   VecBuffer  (  2  ,  new   BufferWrapper  .  DoubleBufferWrapper  (  pointBuffer  )  )  ; 
return  (  (  VecBufferSequence  )  this  .  pointBuffer  )  .  append  (  vecBuffer  )  ; 
} 
} 






@  SuppressWarnings  (  {  "StringEquality"  }  ) 
protected   int   computeNumberOfPointsEstimate  (  )  { 
final   int   numRecords  =  this  .  getNumberOfRecords  (  )  ; 
if  (  numRecords  <  0  )  return  (  this  .  getLength  (  )  -  HEADER_LENGTH  )  /  16  ; 
int   overhead  =  HEADER_LENGTH  +  numRecords  *  12  ; 
String   shapeType  =  this  .  getShapeType  (  )  ; 
if  (  shapeType  ==  SHAPE_POINT  ||  shapeType  ==  SHAPE_POINT_M  )  return  (  this  .  getLength  (  )  -  overhead  )  /  16  ; 
if  (  shapeType  ==  SHAPE_MULTI_POINT  ||  shapeType  ==  SHAPE_MULTI_POINT_M  )  return  (  this  .  getLength  (  )  -  (  overhead  +  numRecords  *  (  32  +  4  )  )  )  /  16  ; 
if  (  shapeType  ==  SHAPE_POLYLINE  ||  shapeType  ==  SHAPE_POLYGON  ||  shapeType  ==  SHAPE_POLYLINE_M  ||  shapeType  ==  SHAPE_POLYGON_M  )  return  (  this  .  getLength  (  )  -  (  overhead  +  numRecords  *  (  32  +  8  )  )  )  /  16  ; 
if  (  shapeType  ==  SHAPE_POINT_Z  )  return  (  this  .  getLength  (  )  -  overhead  )  /  24  ; 
if  (  shapeType  ==  SHAPE_MULTI_POINT_Z  )  return  (  this  .  getLength  (  )  -  (  overhead  +  numRecords  *  (  48  +  4  )  )  )  /  24  ; 
if  (  shapeType  ==  SHAPE_POLYLINE_Z  ||  shapeType  ==  SHAPE_POLYGON_Z  )  return  (  this  .  getLength  (  )  -  (  overhead  +  numRecords  *  (  48  +  8  )  )  )  /  24  ; 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "SHP.UnsupportedShapeType"  ,  shapeType  )  )  ; 
} 

















protected   DoubleBuffer   readPoints  (  ShapefileRecord   record  ,  ByteBuffer   buffer  )  { 
if  (  buffer  ==  null  ||  !  buffer  .  hasRemaining  (  )  )  return   null  ; 
Object   o  =  this  .  getValue  (  AVKey  .  COORDINATE_SYSTEM  )  ; 
if  (  !  this  .  hasKey  (  AVKey  .  COORDINATE_SYSTEM  )  )  return   this  .  readUnspecifiedPoints  (  record  ,  buffer  )  ;  else   if  (  AVKey  .  COORDINATE_SYSTEM_GEOGRAPHIC  .  equals  (  o  )  )  return   this  .  readGeographicPoints  (  record  ,  buffer  )  ;  else   if  (  AVKey  .  COORDINATE_SYSTEM_PROJECTED  .  equals  (  o  )  )  return   this  .  readProjectedPoints  (  record  ,  buffer  )  ;  else  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "generic.UnsupportedCoordinateSystem"  ,  o  )  )  ; 
} 
} 










@  SuppressWarnings  (  {  "UnusedDeclaration"  }  ) 
protected   DoubleBuffer   readUnspecifiedPoints  (  ShapefileRecord   record  ,  ByteBuffer   buffer  )  { 
return   buffer  .  asDoubleBuffer  (  )  ; 
} 











protected   DoubleBuffer   readGeographicPoints  (  ShapefileRecord   record  ,  ByteBuffer   buffer  )  { 
DoubleBuffer   doubleBuffer  =  buffer  .  asDoubleBuffer  (  )  ; 
if  (  record  !=  null  &&  record  .  isNormalizePoints  (  )  )  { 
WWUtil  .  normalizeGeographicCoordinates  (  doubleBuffer  )  ; 
doubleBuffer  .  rewind  (  )  ; 
} 
return   doubleBuffer  ; 
} 














@  SuppressWarnings  (  {  "UnusedDeclaration"  }  ) 
protected   DoubleBuffer   readProjectedPoints  (  ShapefileRecord   record  ,  ByteBuffer   buffer  )  { 
Object   o  =  this  .  getValue  (  AVKey  .  PROJECTION_NAME  )  ; 
if  (  AVKey  .  PROJECTION_UTM  .  equals  (  o  )  )  { 
Integer   zone  =  (  Integer  )  this  .  getValue  (  AVKey  .  PROJECTION_ZONE  )  ; 
String   hemisphere  =  (  String  )  this  .  getValue  (  AVKey  .  PROJECTION_HEMISPHERE  )  ; 
DoubleBuffer   doubleBuffer  =  buffer  .  asDoubleBuffer  (  )  ; 
WWUtil  .  convertUTMCoordinatesToGeographic  (  zone  ,  hemisphere  ,  doubleBuffer  )  ; 
doubleBuffer  .  rewind  (  )  ; 
return   doubleBuffer  ; 
}  else  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "generic.UnsupportedProjection"  ,  o  )  )  ; 
} 
} 








protected   static   class   BoundingRectangle  { 


public   double  [  ]  coords  ; 


public   boolean   isNormalized  ; 
} 











protected   BoundingRectangle   readBoundingRectangle  (  ByteBuffer   buffer  )  { 
Object   o  =  this  .  getValue  (  AVKey  .  COORDINATE_SYSTEM  )  ; 
if  (  !  this  .  hasKey  (  AVKey  .  COORDINATE_SYSTEM  )  )  return   this  .  readUnspecifiedBoundingRectangle  (  buffer  )  ;  else   if  (  AVKey  .  COORDINATE_SYSTEM_GEOGRAPHIC  .  equals  (  o  )  )  return   this  .  readGeographicBoundingRectangle  (  buffer  )  ;  else   if  (  AVKey  .  COORDINATE_SYSTEM_PROJECTED  .  equals  (  o  )  )  return   this  .  readProjectedBoundingRectangle  (  buffer  )  ;  else  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "generic.UnsupportedCoordinateSystem"  ,  o  )  )  ; 
} 
} 











protected   BoundingRectangle   readUnspecifiedBoundingRectangle  (  ByteBuffer   buffer  )  { 
BoundingRectangle   rect  =  new   BoundingRectangle  (  )  ; 
rect  .  coords  =  this  .  readBoundingRectangleCoordinates  (  buffer  )  ; 
return   rect  ; 
} 













protected   BoundingRectangle   readGeographicBoundingRectangle  (  ByteBuffer   buffer  )  { 
BoundingRectangle   rect  =  new   BoundingRectangle  (  )  ; 
rect  .  coords  =  this  .  readBoundingRectangleCoordinates  (  buffer  )  ; 
if  (  rect  .  coords  [  0  ]  <  -  90  )  { 
double   normalizedLat  =  Angle  .  normalizedLatitude  (  Angle  .  fromDegrees  (  rect  .  coords  [  0  ]  )  )  .  degrees  ; 
rect  .  coords  [  0  ]  =  90  ; 
rect  .  isNormalized  =  true  ; 
if  (  rect  .  coords  [  1  ]  <  normalizedLat  )  rect  .  coords  [  1  ]  =  normalizedLat  ; 
} 
if  (  rect  .  coords  [  1  ]  >  90  )  { 
double   normalizedLat  =  Angle  .  normalizedLatitude  (  Angle  .  fromDegrees  (  rect  .  coords  [  1  ]  )  )  .  degrees  ; 
rect  .  coords  [  1  ]  =  90  ; 
rect  .  isNormalized  =  true  ; 
if  (  rect  .  coords  [  0  ]  >  normalizedLat  )  rect  .  coords  [  0  ]  =  normalizedLat  ; 
} 
if  (  rect  .  coords  [  2  ]  <  -  180  ||  rect  .  coords  [  3  ]  >  180  )  { 
rect  .  coords  [  2  ]  =  -  180  ; 
rect  .  coords  [  3  ]  =  180  ; 
rect  .  isNormalized  =  true  ; 
} 
return   rect  ; 
} 















protected   BoundingRectangle   readProjectedBoundingRectangle  (  ByteBuffer   buffer  )  { 
Object   o  =  this  .  getValue  (  AVKey  .  PROJECTION_NAME  )  ; 
if  (  AVKey  .  PROJECTION_UTM  .  equals  (  o  )  )  { 
double  [  ]  coords  =  ShapefileUtils  .  readDoubleArray  (  buffer  ,  4  )  ; 
Integer   zone  =  (  Integer  )  this  .  getValue  (  AVKey  .  PROJECTION_ZONE  )  ; 
String   hemisphere  =  (  String  )  this  .  getValue  (  AVKey  .  PROJECTION_HEMISPHERE  )  ; 
Sector   sector  =  Sector  .  fromUTMRectangle  (  zone  ,  hemisphere  ,  coords  [  0  ]  ,  coords  [  2  ]  ,  coords  [  1  ]  ,  coords  [  3  ]  )  ; 
BoundingRectangle   rect  =  new   BoundingRectangle  (  )  ; 
rect  .  coords  =  sector  .  toArrayDegrees  (  )  ; 
return   rect  ; 
}  else  { 
throw   new   WWRuntimeException  (  Logging  .  getMessage  (  "generic.UnsupportedProjection"  ,  o  )  )  ; 
} 
} 










protected   double  [  ]  readBoundingRectangleCoordinates  (  ByteBuffer   buffer  )  { 
double   minx  =  buffer  .  getDouble  (  )  ; 
double   miny  =  buffer  .  getDouble  (  )  ; 
double   maxx  =  buffer  .  getDouble  (  )  ; 
double   maxy  =  buffer  .  getDouble  (  )  ; 
return   new   double  [  ]  {  miny  ,  maxy  ,  minx  ,  maxx  }  ; 
} 










public   static   boolean   isMeasureType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   measureTypes  .  contains  (  shapeType  )  ; 
} 










public   static   boolean   isZType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   zTypes  .  contains  (  shapeType  )  ; 
} 










public   static   boolean   isNullType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   shapeType  .  equals  (  Shapefile  .  SHAPE_NULL  )  ; 
} 











public   static   boolean   isPointType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   shapeType  .  equals  (  Shapefile  .  SHAPE_POINT  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POINT_Z  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POINT_M  )  ; 
} 











public   static   boolean   isMultiPointType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   shapeType  .  equals  (  Shapefile  .  SHAPE_MULTI_POINT  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_MULTI_POINT_Z  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_MULTI_POINT_M  )  ; 
} 











public   static   boolean   isPolylineType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   shapeType  .  equals  (  Shapefile  .  SHAPE_POLYLINE  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POLYLINE_Z  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POLYLINE_M  )  ; 
} 









public   static   boolean   isPolygonType  (  String   shapeType  )  { 
if  (  shapeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.ShapeType"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
return   shapeType  .  equals  (  Shapefile  .  SHAPE_POLYGON  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POLYGON_Z  )  ||  shapeType  .  equals  (  Shapefile  .  SHAPE_POLYGON_M  )  ; 
} 

public   String   isExportFormatSupported  (  String   mimeType  )  { 
if  (  KMLConstants  .  KML_MIME_TYPE  .  equalsIgnoreCase  (  mimeType  )  )  return   FORMAT_SUPPORTED  ; 
return   Arrays  .  binarySearch  (  SHAPE_CONTENT_TYPES  ,  mimeType  )  >=  0  ?  FORMAT_SUPPORTED  :  FORMAT_NOT_SUPPORTED  ; 
} 

public   void   export  (  String   mimeType  ,  Object   output  )  throws   IOException  ,  UnsupportedOperationException  { 
if  (  mimeType  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.Format"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  output  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "nullValue.OutputBufferIsNull"  )  ; 
Logging  .  logger  (  )  .  severe  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
try  { 
this  .  doExport  (  mimeType  ,  output  )  ; 
}  catch  (  XMLStreamException   e  )  { 
Logging  .  logger  (  )  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "export"  ,  e  )  ; 
throw   new   IOException  (  e  )  ; 
} 
} 

protected   void   doExport  (  String   mimeType  ,  Object   output  )  throws   IOException  ,  XMLStreamException  { 
XMLStreamWriter   xmlWriter  =  null  ; 
XMLOutputFactory   factory  =  XMLOutputFactory  .  newInstance  (  )  ; 
boolean   closeWriterWhenFinished  =  true  ; 
if  (  output   instanceof   XMLStreamWriter  )  { 
xmlWriter  =  (  XMLStreamWriter  )  output  ; 
closeWriterWhenFinished  =  false  ; 
}  else   if  (  output   instanceof   Writer  )  { 
xmlWriter  =  factory  .  createXMLStreamWriter  (  (  Writer  )  output  )  ; 
}  else   if  (  output   instanceof   OutputStream  )  { 
xmlWriter  =  factory  .  createXMLStreamWriter  (  (  OutputStream  )  output  )  ; 
} 
if  (  xmlWriter  ==  null  )  { 
String   message  =  Logging  .  getMessage  (  "Export.UnsupportedOutputObject"  )  ; 
Logging  .  logger  (  )  .  warning  (  message  )  ; 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
if  (  KMLConstants  .  KML_MIME_TYPE  .  equals  (  mimeType  )  )  exportAsKML  (  xmlWriter  )  ;  else   exportAsXML  (  xmlWriter  )  ; 
xmlWriter  .  flush  (  )  ; 
if  (  closeWriterWhenFinished  )  xmlWriter  .  close  (  )  ; 
} 

protected   void   exportAsXML  (  XMLStreamWriter   xmlWriter  )  throws   IOException  ,  XMLStreamException  { 
xmlWriter  .  writeStartElement  (  "Shapefile"  )  ; 
xmlWriter  .  writeCharacters  (  "\n"  )  ; 
while  (  this  .  hasNext  (  )  )  { 
try  { 
ShapefileRecord   nr  =  this  .  nextRecord  (  )  ; 
if  (  nr  ==  null  )  continue  ; 
nr  .  exportAsXML  (  xmlWriter  )  ; 
xmlWriter  .  writeCharacters  (  "\n"  )  ; 
}  catch  (  Exception   e  )  { 
String   message  =  Logging  .  getMessage  (  "Export.Exception.ShapefileRecord"  )  ; 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  message  ,  e  )  ; 
continue  ; 
} 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 

protected   void   exportAsKML  (  XMLStreamWriter   xmlWriter  )  throws   IOException  ,  XMLStreamException  { 
while  (  this  .  hasNext  (  )  )  { 
try  { 
ShapefileRecord   nr  =  this  .  nextRecord  (  )  ; 
if  (  nr  ==  null  )  continue  ; 
nr  .  exportAsKML  (  xmlWriter  )  ; 
}  catch  (  Exception   e  )  { 
String   message  =  Logging  .  getMessage  (  "Export.Exception.ShapefileRecord"  )  ; 
Logging  .  logger  (  )  .  log  (  Level  .  WARNING  ,  message  ,  e  )  ; 
continue  ; 
} 
} 
} 

public   void   printInfo  (  boolean   printCoordinates  )  { 
while  (  this  .  hasNext  (  )  )  { 
this  .  nextRecord  (  )  .  printInfo  (  printCoordinates  )  ; 
} 
} 
} 

