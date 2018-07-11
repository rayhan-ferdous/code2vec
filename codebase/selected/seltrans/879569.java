package   net  .  jwde  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   javax  .  xml  .  XMLConstants  ; 
import   javax  .  xml  .  transform  .  *  ; 
import   javax  .  xml  .  transform  .  sax  .  SAXSource  ; 
import   javax  .  xml  .  transform  .  stream  .  *  ; 
import   javax  .  xml  .  validation  .  SchemaFactory  ; 
import   javax  .  xml  .  validation  .  Schema  ; 
import   javax  .  xml  .  validation  .  Validator  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 
import   javax  .  xml  .  validation  .  ValidatorHandler  ; 
import   org  .  jdom  .  *  ; 
import   org  .  jdom  .  input  .  *  ; 
import   org  .  jdom  .  output  .  *  ; 
import   org  .  jdom  .  transform  .  *  ; 
import   org  .  xml  .  sax  .  *  ; 
import   org  .  w3c  .  tidy  .  *  ; 












public   class   XMLHelper  { 











public   static   Document   createXml  (  String   rootName  )  { 
Document   doc  =  new   Document  (  )  ; 
if  (  rootName  ==  null  ||  rootName  .  trim  (  )  .  equals  (  ""  )  )  return   doc  ; 
doc  .  setRootElement  (  new   Element  (  rootName  )  )  ; 
return   doc  ; 
} 













public   static   Document   parseXMLFromURLString  (  String   url  )  throws   XMLHelperException  { 
return   parseXMLFromURL  (  convertStringToURL  (  url  )  )  ; 
} 













public   static   Document   parseXMLFromURL  (  URL   url  )  throws   XMLHelperException  { 
try  { 
URLConnection   inConnection  =  url  .  openConnection  (  )  ; 
InputSource   is  =  new   InputSource  (  inConnection  .  getInputStream  (  )  )  ; 
return   parseXMLFromInputSource  (  is  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   XMLHelperException  (  "Unable to read from source string"  ,  ioe  )  ; 
} 
} 












public   static   Document   parseXMLFromString  (  String   source  )  throws   XMLHelperException  { 
InputSource   is  =  new   InputSource  (  new   StringReader  (  source  )  )  ; 
return   parseXMLFromInputSource  (  is  )  ; 
} 












public   static   Document   parseXMLFromFile  (  File   sourceFile  )  throws   XMLHelperException  { 
Document   doc  =  null  ; 
try  { 
SAXBuilder   saxBuilder  =  new   SAXBuilder  (  )  ; 
doc  =  saxBuilder  .  build  (  sourceFile  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   doc  ; 
} 












public   static   Document   parseXMLFromFile  (  String   sourceFile  )  throws   XMLHelperException  { 
Document   doc  =  null  ; 
File   f1  =  new   File  (  sourceFile  )  ; 
try  { 
SAXBuilder   saxBuilder  =  new   SAXBuilder  (  )  ; 
doc  =  saxBuilder  .  build  (  f1  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   doc  ; 
} 

private   static   Document   parseXMLFromInputSource  (  InputSource   is  )  throws   XMLHelperException  { 
Document   doc  =  null  ; 
try  { 
SAXBuilder   saxBuilder  =  new   SAXBuilder  (  )  ; 
doc  =  saxBuilder  .  build  (  is  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   doc  ; 
} 
















public   static   Document   transformXML  (  Document   xmlDoc  ,  Document   xslDoc  )  throws   XMLHelperException  { 
String   result  =  null  ; 
Document   resultDoc  =  null  ; 
try  { 
TransformerFactory   tFactory  =  TransformerFactory  .  newInstance  (  )  ; 
Transformer   transformer  =  tFactory  .  newTransformer  (  new   JDOMSource  (  xslDoc  )  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
transformer  .  transform  (  new   JDOMSource  (  xmlDoc  )  ,  new   StreamResult  (  baos  )  )  ; 
baos  .  close  (  )  ; 
result  =  baos  .  toString  (  )  ; 
resultDoc  =  parseXMLFromString  (  result  )  ; 
return   resultDoc  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultDoc  ; 
} 









public   static   Document   transformXML  (  String   xmlDoc  ,  String   xslDoc  )  { 
String   result  =  null  ; 
try  { 
TransformerFactory   tFactory  =  TransformerFactory  .  newInstance  (  )  ; 
Transformer   transformer  =  tFactory  .  newTransformer  (  new   StreamSource  (  xslDoc  )  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
transformer  .  transform  (  new   StreamSource  (  xmlDoc  )  ,  new   StreamResult  (  baos  )  )  ; 
baos  .  close  (  )  ; 
result  =  baos  .  toString  (  )  ; 
return   parseXMLFromString  (  result  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   null  ; 
} 















public   static   void   outputXML  (  Document   doc  ,  PrintStream   stream  )  throws   XMLHelperException  { 
try  { 
Format   format  =  Format  .  getPrettyFormat  (  )  ; 
format  .  setTextMode  (  Format  .  TextMode  .  NORMALIZE  )  ; 
XMLOutputter   xmlOutputter  =  new   XMLOutputter  (  format  )  ; 
xmlOutputter  .  output  (  doc  ,  stream  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
throw   new   XMLHelperException  (  "Unable to write to the given print stream"  ,  ioe  )  ; 
} 
} 
















public   static   void   outputXMLToFile  (  Document   doc  ,  String   fileName  )  throws   XMLHelperException  { 
try  { 
File   f1  =  new   File  (  fileName  )  ; 
FileWriter   fout  =  new   FileWriter  (  f1  )  ; 
Format   format  =  Format  .  getPrettyFormat  (  )  ; 
format  .  setTextMode  (  Format  .  TextMode  .  NORMALIZE  )  ; 
XMLOutputter   xmlOutputter  =  new   XMLOutputter  (  format  )  ; 
xmlOutputter  .  output  (  doc  ,  fout  )  ; 
fout  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   XMLHelperException  (  "Unable to write to the given file"  ,  ioe  )  ; 
} 
} 

public   static   void   outputXMLToURL  (  Document   doc  ,  URL   url  )  throws   XMLHelperException  { 
try  { 
File   f1  =  new   File  (  url  .  getFile  (  )  )  ; 
FileWriter   fout  =  new   FileWriter  (  f1  )  ; 
Format   format  =  Format  .  getPrettyFormat  (  )  ; 
format  .  setTextMode  (  Format  .  TextMode  .  NORMALIZE  )  ; 
XMLOutputter   xmlOutputter  =  new   XMLOutputter  (  format  )  ; 
xmlOutputter  .  output  (  doc  ,  fout  )  ; 
fout  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   XMLHelperException  (  "Unable to write to the given file"  ,  ioe  )  ; 
} 
} 










public   static   String   convertXMLToString  (  Document   doc  )  throws   XMLHelperException  { 
try  { 
Format   format  =  Format  .  getPrettyFormat  (  )  ; 
format  .  setTextMode  (  Format  .  TextMode  .  NORMALIZE  )  ; 
XMLOutputter   xmlOutputter  =  new   XMLOutputter  (  format  )  ; 
return   xmlOutputter  .  outputString  (  doc  )  ; 
}  catch  (  Exception   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
throw   new   XMLHelperException  (  "Unable to write to the string"  ,  ioe  )  ; 
} 
} 

public   static   Document   tidyHTML  (  File   file  )  throws   XMLHelperException  { 
return   tidyHTML  (  convertFileToURL  (  file  )  )  ; 
} 

public   static   URL   convertFileToURL  (  File   f1  )  { 
try  { 
return   f1  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   me  )  { 
me  .  printStackTrace  (  )  ; 
} 
return   null  ; 
} 

















public   static   Document   tidyHTML  (  String   url  )  throws   XMLHelperException  { 
return   tidyHTML  (  convertStringToURL  (  url  )  )  ; 
} 
















public   static   Document   tidyHTML  (  URL   url  )  throws   XMLHelperException  { 
try  { 
URLConnection   inConnection  =  url  .  openConnection  (  )  ; 
if  (  inConnection  .  getContentType  (  )  .  startsWith  (  "text/xml"  )  ||  inConnection  .  getContentType  (  )  .  startsWith  (  "text/xhtml"  )  )  { 
return   parseXMLFromURL  (  url  )  ; 
}  else   if  (  inConnection  .  getContentType  (  )  .  startsWith  (  "text/html"  )  )  { 
InputStream   is  =  inConnection  .  getInputStream  (  )  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
int   totalBytes  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  65536  ]  ; 
while  (  true  )  { 
int   bytesRead  =  is  .  read  (  buffer  ,  0  ,  buffer  .  length  )  ; 
if  (  bytesRead  <  0  )  break  ; 
for  (  int   i  =  0  ;  i  <  bytesRead  ;  i  ++  )  { 
byte   b  =  buffer  [  i  ]  ; 
if  (  b  <  32  &&  b  !=  10  &&  b  !=  13  &&  b  !=  9  )  b  =  32  ; 
buffer  [  i  ]  =  b  ; 
} 
out  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
totalBytes  +=  bytesRead  ; 
} 
is  .  close  (  )  ; 
out  .  close  (  )  ; 
String   outContent  =  out  .  toString  (  )  ; 
InputStream   in  =  new   ByteArrayInputStream  (  out  .  toByteArray  (  )  )  ; 
Tidy   tidy  =  new   Tidy  (  )  ; 
tidy  .  setShowWarnings  (  false  )  ; 
tidy  .  setXmlOut  (  true  )  ; 
tidy  .  setXmlPi  (  false  )  ; 
tidy  .  setDocType  (  "omit"  )  ; 
tidy  .  setXHTML  (  false  )  ; 
tidy  .  setRawOut  (  true  )  ; 
tidy  .  setNumEntities  (  true  )  ; 
tidy  .  setQuiet  (  true  )  ; 
tidy  .  setFixComments  (  true  )  ; 
tidy  .  setIndentContent  (  true  )  ; 
tidy  .  setCharEncoding  (  org  .  w3c  .  tidy  .  Configuration  .  ASCII  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
org  .  w3c  .  dom  .  Document   tNode  =  (  org  .  w3c  .  dom  .  Document  )  tidy  .  parseDOM  (  in  ,  baos  )  ; 
String   result  =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"  +  baos  .  toString  (  )  ; 
int   startIndex  =  0  ; 
int   endIndex  =  0  ; 
if  (  (  startIndex  =  result  .  indexOf  (  "<!DOCTYPE"  )  )  >=  0  )  { 
endIndex  =  result  .  indexOf  (  ">"  ,  startIndex  )  ; 
result  =  result  .  substring  (  0  ,  startIndex  )  +  result  .  substring  (  endIndex  +  1  ,  result  .  length  (  )  )  ; 
} 
while  (  (  startIndex  =  result  .  indexOf  (  "<script"  )  )  >=  0  )  { 
endIndex  =  result  .  indexOf  (  "</script>"  )  ; 
result  =  result  .  substring  (  0  ,  startIndex  )  +  result  .  substring  (  endIndex  +  9  ,  result  .  length  (  )  )  ; 
} 
in  .  close  (  )  ; 
baos  .  close  (  )  ; 
return   parseXMLFromString  (  result  )  ; 
}  else  { 
throw   new   XMLHelperException  (  "Unable to tidy content type: "  +  inConnection  .  getContentType  (  )  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
throw   new   XMLHelperException  (  "Unable to perform input/output"  ,  ioe  )  ; 
} 
} 

private   static   URL   convertStringToURL  (  String   url  )  throws   XMLHelperException  { 
try  { 
return   new   URL  (  url  )  ; 
}  catch  (  MalformedURLException   murle  )  { 
throw   new   XMLHelperException  (  url  +  " is not a well formed URL"  ,  murle  )  ; 
} 
} 

public   static   org  .  w3c  .  dom  .  Document   JDOM2DOM  (  Document   doc  )  throws   JDOMException  { 
org  .  w3c  .  dom  .  Document   dom  =  null  ; 
DOMOutputter   outputter  =  new   DOMOutputter  (  )  ; 
try  { 
if  (  doc  !=  null  )  dom  =  outputter  .  output  (  doc  )  ;  else   return   dom  ; 
}  catch  (  JDOMException   jdomEx  )  { 
throw   jdomEx  ; 
} 
return   dom  ; 
} 








public   static   void   validate  (  URL   xmlURL  )  throws   SAXException  ,  IOException  { 
try  { 
Schema   schema  =  SchemaFactory  .  newInstance  (  XMLConstants  .  W3C_XML_SCHEMA_NS_URI  )  .  newSchema  (  )  ; 
Validator   validator  =  schema  .  newValidator  (  )  ; 
URLConnection   inConnection  =  xmlURL  .  openConnection  (  )  ; 
InputStream   is  =  inConnection  .  getInputStream  (  )  ; 
validator  .  validate  (  new   SAXSource  (  new   InputSource  (  is  )  )  )  ; 
}  catch  (  SAXException   saxEx  )  { 
throw   saxEx  ; 
}  catch  (  IOException   ioEx  )  { 
throw   ioEx  ; 
} 
} 
} 

