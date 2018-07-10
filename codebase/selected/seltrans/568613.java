package   com  .  bretth  .  osmosis  .  core  .  xml  .  v0_5  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 
import   java  .  util  .  zip  .  Inflater  ; 
import   java  .  util  .  zip  .  InflaterInputStream  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   org  .  openstreetmap  .  osmosis  .  core  .  OsmosisRuntimeException  ; 
import   org  .  openstreetmap  .  osmosis  .  core  .  task  .  v0_5  .  RunnableSource  ; 
import   org  .  openstreetmap  .  osmosis  .  core  .  task  .  v0_5  .  Sink  ; 
import   org  .  openstreetmap  .  osmosis  .  core  .  xml  .  v0_5  .  impl  .  OsmHandler  ; 







public   class   XmlDownloader   implements   RunnableSource  { 




private   static   final   int   RESPONSECODE_OK  =  200  ; 




private   static   Logger   log  =  Logger  .  getLogger  (  XmlDownloader  .  class  .  getName  (  )  )  ; 




private   static   final   int   TIMEOUT  =  15000  ; 




private   Sink   mySink  ; 




private   double   myLeft  ; 




private   double   myRight  ; 




private   double   myTop  ; 




private   double   myBottom  ; 





private   String   myBaseUrl  =  "http://www.openstreetmap.org/api/0.5"  ; 




private   HttpURLConnection   myActiveConnection  ; 




private   InputStream   responseStream  ; 
















public   XmlDownloader  (  final   double   left  ,  final   double   right  ,  final   double   top  ,  final   double   bottom  ,  final   String   baseUrl  )  { 
this  .  myLeft  =  Math  .  min  (  top  ,  bottom  )  ; 
this  .  myRight  =  Math  .  max  (  top  ,  bottom  )  ; 
this  .  myTop  =  Math  .  max  (  left  ,  right  )  ; 
this  .  myBottom  =  Math  .  min  (  left  ,  right  )  ; 
if  (  baseUrl  !=  null  )  this  .  myBaseUrl  =  baseUrl  ; 
} 




public   void   setSink  (  final   Sink   aSink  )  { 
this  .  mySink  =  aSink  ; 
} 




private   void   cleanup  (  )  { 
if  (  myActiveConnection  !=  null  )  { 
try  { 
myActiveConnection  .  disconnect  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  log  (  Level  .  SEVERE  ,  "Unable to disconnect."  ,  e  )  ; 
} 
myActiveConnection  =  null  ; 
} 
if  (  responseStream  !=  null  )  { 
try  { 
responseStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
log  .  log  (  Level  .  SEVERE  ,  "Unable to close response stream."  ,  e  )  ; 
} 
responseStream  =  null  ; 
} 
} 






private   SAXParser   createParser  (  )  { 
try  { 
return   SAXParserFactory  .  newInstance  (  )  .  newSAXParser  (  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
throw   new   OsmosisRuntimeException  (  "Unable to create SAX Parser."  ,  e  )  ; 
}  catch  (  SAXException   e  )  { 
throw   new   OsmosisRuntimeException  (  "Unable to create SAX Parser."  ,  e  )  ; 
} 
} 




public   void   run  (  )  { 
try  { 
SAXParser   parser  =  createParser  (  )  ; 
String   url  =  myBaseUrl  +  "/map?bbox="  +  myLeft  +  ","  +  myBottom  +  ","  +  myRight  +  ","  +  myTop  ; 
System  .  out  .  println  (  url  )  ; 
InputStream   inputStream  =  getInputStream  (  url  )  ; 
try  { 
parser  .  parse  (  inputStream  ,  new   OsmHandler  (  mySink  ,  true  )  )  ; 
}  finally  { 
inputStream  .  close  (  )  ; 
inputStream  =  null  ; 
} 
mySink  .  complete  (  )  ; 
}  catch  (  SAXParseException   e  )  { 
throw   new   OsmosisRuntimeException  (  "Unable to parse xml"  +  ".  publicId=("  +  e  .  getPublicId  (  )  +  "), systemId=("  +  e  .  getSystemId  (  )  +  "), lineNumber="  +  e  .  getLineNumber  (  )  +  ", columnNumber="  +  e  .  getColumnNumber  (  )  +  "."  ,  e  )  ; 
}  catch  (  SAXException   e  )  { 
throw   new   OsmosisRuntimeException  (  "Unable to parse XML."  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   OsmosisRuntimeException  (  "Unable to read XML."  ,  e  )  ; 
}  finally  { 
mySink  .  release  (  )  ; 
cleanup  (  )  ; 
} 
} 












private   InputStream   getInputStream  (  final   String   pUrlStr  )  throws   IOException  { 
URL   url  ; 
int   responseCode  ; 
String   encoding  ; 
url  =  new   URL  (  pUrlStr  )  ; 
myActiveConnection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
myActiveConnection  .  setRequestProperty  (  "Accept-Encoding"  ,  "gzip, deflate"  )  ; 
responseCode  =  myActiveConnection  .  getResponseCode  (  )  ; 
if  (  responseCode  !=  RESPONSECODE_OK  )  { 
String   message  ; 
String   apiErrorMessage  ; 
apiErrorMessage  =  myActiveConnection  .  getHeaderField  (  "Error"  )  ; 
if  (  apiErrorMessage  !=  null  )  { 
message  =  "Received API HTTP response code "  +  responseCode  +  " with message \""  +  apiErrorMessage  +  "\" for URL \""  +  pUrlStr  +  "\"."  ; 
}  else  { 
message  =  "Received API HTTP response code "  +  responseCode  +  " for URL \""  +  pUrlStr  +  "\"."  ; 
} 
throw   new   OsmosisRuntimeException  (  message  )  ; 
} 
myActiveConnection  .  setConnectTimeout  (  TIMEOUT  )  ; 
encoding  =  myActiveConnection  .  getContentEncoding  (  )  ; 
responseStream  =  myActiveConnection  .  getInputStream  (  )  ; 
if  (  encoding  !=  null  &&  encoding  .  equalsIgnoreCase  (  "gzip"  )  )  { 
responseStream  =  new   GZIPInputStream  (  responseStream  )  ; 
}  else   if  (  encoding  !=  null  &&  encoding  .  equalsIgnoreCase  (  "deflate"  )  )  { 
responseStream  =  new   InflaterInputStream  (  responseStream  ,  new   Inflater  (  true  )  )  ; 
} 
return   responseStream  ; 
} 
} 

