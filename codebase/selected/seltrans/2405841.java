package   org  .  ofbiz  .  base  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilder  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javolution  .  util  .  FastList  ; 
import   org  .  apache  .  xml  .  serialize  .  OutputFormat  ; 
import   org  .  apache  .  xml  .  serialize  .  XMLSerializer  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  DocumentFragment  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  xml  .  sax  .  EntityResolver  ; 
import   org  .  xml  .  sax  .  ErrorHandler  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 





public   class   UtilXml  { 

public   static   final   String   module  =  UtilXml  .  class  .  getName  (  )  ; 

public   static   String   writeXmlDocument  (  Document   document  )  throws   java  .  io  .  IOException  { 
if  (  document  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Document was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
return   writeXmlDocument  (  document  .  getDocumentElement  (  )  )  ; 
} 

public   static   String   writeXmlDocument  (  Element   element  )  throws   java  .  io  .  IOException  { 
if  (  element  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Element was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
writeXmlDocument  (  bos  ,  element  )  ; 
String   outString  =  bos  .  toString  (  "UTF-8"  )  ; 
if  (  bos  !=  null  )  bos  .  close  (  )  ; 
return   outString  ; 
} 

public   static   String   writeXmlDocument  (  DocumentFragment   fragment  )  throws   java  .  io  .  IOException  { 
if  (  fragment  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] DocumentFragment was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
List   elementList  =  UtilXml  .  childElementList  (  fragment  )  ; 
Iterator   elementIter  =  elementList  .  iterator  (  )  ; 
while  (  elementIter  .  hasNext  (  )  )  { 
Element   element  =  (  Element  )  elementIter  .  next  (  )  ; 
writeXmlDocument  (  bos  ,  element  )  ; 
} 
String   outString  =  bos  .  toString  (  "UTF-8"  )  ; 
if  (  bos  !=  null  )  bos  .  close  (  )  ; 
return   outString  ; 
} 

public   static   void   writeXmlDocument  (  String   filename  ,  Document   document  )  throws   java  .  io  .  FileNotFoundException  ,  java  .  io  .  IOException  { 
if  (  document  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Document was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
writeXmlDocument  (  filename  ,  document  .  getDocumentElement  (  )  )  ; 
} 

public   static   void   writeXmlDocument  (  String   filename  ,  Element   element  )  throws   java  .  io  .  FileNotFoundException  ,  java  .  io  .  IOException  { 
if  (  element  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Element was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
if  (  filename  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Filename was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
File   outFile  =  new   File  (  filename  )  ; 
FileOutputStream   fos  =  null  ; 
fos  =  new   FileOutputStream  (  outFile  )  ; 
try  { 
writeXmlDocument  (  fos  ,  element  )  ; 
}  finally  { 
if  (  fos  !=  null  )  fos  .  close  (  )  ; 
} 
} 

public   static   void   writeXmlDocument  (  OutputStream   os  ,  Document   document  )  throws   java  .  io  .  IOException  { 
if  (  document  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Document was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
writeXmlDocument  (  os  ,  document  .  getDocumentElement  (  )  )  ; 
} 

public   static   void   writeXmlDocument  (  OutputStream   os  ,  Element   element  )  throws   java  .  io  .  IOException  { 
OutputFormat   format  =  new   OutputFormat  (  element  .  getOwnerDocument  (  )  )  ; 
writeXmlDocument  (  os  ,  element  ,  format  )  ; 
} 

public   static   void   writeXmlDocument  (  OutputStream   os  ,  Element   element  ,  OutputFormat   format  )  throws   java  .  io  .  IOException  { 
if  (  element  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] Element was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
if  (  os  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.writeXmlDocument] OutputStream was null, doing nothing"  ,  module  )  ; 
return  ; 
} 
XMLSerializer   serializer  =  new   XMLSerializer  (  os  ,  format  )  ; 
serializer  .  asDOMSerializer  (  )  ; 
serializer  .  serialize  (  element  )  ; 
} 

public   static   Document   readXmlDocument  (  String   content  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
return   readXmlDocument  (  content  ,  true  )  ; 
} 

public   static   Document   readXmlDocument  (  String   content  ,  boolean   validate  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
if  (  content  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.readXmlDocument] content was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
ByteArrayInputStream   bis  =  new   ByteArrayInputStream  (  content  .  getBytes  (  "UTF-8"  )  )  ; 
return   readXmlDocument  (  bis  ,  validate  ,  "Internal Content"  )  ; 
} 

public   static   Document   readXmlDocument  (  URL   url  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
return   readXmlDocument  (  url  ,  true  )  ; 
} 

public   static   Document   readXmlDocument  (  URL   url  ,  boolean   validate  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
if  (  url  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.readXmlDocument] URL was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
return   readXmlDocument  (  url  .  openStream  (  )  ,  validate  ,  url  .  toString  (  )  )  ; 
} 




public   static   Document   readXmlDocument  (  InputStream   is  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
return   readXmlDocument  (  is  ,  true  ,  null  )  ; 
} 

public   static   Document   readXmlDocument  (  InputStream   is  ,  String   docDescription  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
return   readXmlDocument  (  is  ,  true  ,  docDescription  )  ; 
} 

public   static   Document   readXmlDocument  (  InputStream   is  ,  boolean   validate  ,  String   docDescription  )  throws   SAXException  ,  ParserConfigurationException  ,  java  .  io  .  IOException  { 
if  (  is  ==  null  )  { 
Debug  .  logWarning  (  "[UtilXml.readXmlDocument] InputStream was null, doing nothing"  ,  module  )  ; 
return   null  ; 
} 
long   startTime  =  System  .  currentTimeMillis  (  )  ; 
Document   document  =  null  ; 
DocumentBuilderFactory   factory  =  new   org  .  apache  .  xerces  .  jaxp  .  DocumentBuilderFactoryImpl  (  )  ; 
factory  .  setValidating  (  validate  )  ; 
factory  .  setNamespaceAware  (  true  )  ; 
factory  .  setAttribute  (  "http://xml.org/sax/features/validation"  ,  Boolean  .  TRUE  )  ; 
factory  .  setAttribute  (  "http://apache.org/xml/features/validation/schema"  ,  Boolean  .  TRUE  )  ; 
DocumentBuilder   builder  =  factory  .  newDocumentBuilder  (  )  ; 
if  (  validate  )  { 
LocalResolver   lr  =  new   LocalResolver  (  new   DefaultHandler  (  )  )  ; 
ErrorHandler   eh  =  new   LocalErrorHandler  (  docDescription  ,  lr  )  ; 
builder  .  setEntityResolver  (  lr  )  ; 
builder  .  setErrorHandler  (  eh  )  ; 
} 
document  =  builder  .  parse  (  is  )  ; 
double   totalSeconds  =  (  System  .  currentTimeMillis  (  )  -  startTime  )  /  1000.0  ; 
if  (  Debug  .  timingOn  (  )  )  Debug  .  logTiming  (  "XML Read "  +  totalSeconds  +  "s: "  +  docDescription  ,  module  )  ; 
return   document  ; 
} 

public   static   Document   makeEmptyXmlDocument  (  )  { 
return   makeEmptyXmlDocument  (  null  )  ; 
} 

public   static   Document   makeEmptyXmlDocument  (  String   rootElementName  )  { 
Document   document  =  null  ; 
DocumentBuilderFactory   factory  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
factory  .  setValidating  (  true  )  ; 
try  { 
DocumentBuilder   builder  =  factory  .  newDocumentBuilder  (  )  ; 
document  =  builder  .  newDocument  (  )  ; 
}  catch  (  Exception   e  )  { 
Debug  .  logError  (  e  ,  module  )  ; 
} 
if  (  document  ==  null  )  return   null  ; 
if  (  rootElementName  !=  null  )  { 
Element   rootElement  =  document  .  createElement  (  rootElementName  )  ; 
document  .  appendChild  (  rootElement  )  ; 
} 
return   document  ; 
} 


public   static   Element   addChildElement  (  Element   element  ,  String   childElementName  ,  Document   document  )  { 
Element   newElement  =  document  .  createElement  (  childElementName  )  ; 
element  .  appendChild  (  newElement  )  ; 
return   newElement  ; 
} 




public   static   Element   addChildElementValue  (  Element   element  ,  String   childElementName  ,  String   childElementValue  ,  Document   document  )  { 
Element   newElement  =  addChildElement  (  element  ,  childElementName  ,  document  )  ; 
newElement  .  appendChild  (  document  .  createTextNode  (  childElementValue  )  )  ; 
return   newElement  ; 
} 




public   static   Element   addChildElementCDATAValue  (  Element   element  ,  String   childElementName  ,  String   childElementValue  ,  Document   document  )  { 
Element   newElement  =  addChildElement  (  element  ,  childElementName  ,  document  )  ; 
newElement  .  appendChild  (  document  .  createCDATASection  (  childElementValue  )  )  ; 
return   newElement  ; 
} 


public   static   List   childElementList  (  Element   element  )  { 
if  (  element  ==  null  )  return   null  ; 
List   elements  =  FastList  .  newInstance  (  )  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   childElement  =  (  Element  )  node  ; 
elements  .  add  (  childElement  )  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   elements  ; 
} 




public   static   List   childElementList  (  Element   element  ,  String   childElementName  )  { 
if  (  element  ==  null  )  return   null  ; 
List   elements  =  FastList  .  newInstance  (  )  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  (  childElementName  ==  null  ||  childElementName  .  equals  (  node  .  getNodeName  (  )  )  )  )  { 
Element   childElement  =  (  Element  )  node  ; 
elements  .  add  (  childElement  )  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   elements  ; 
} 




public   static   List   childElementList  (  Element   element  ,  Set   childElementNames  )  { 
if  (  element  ==  null  )  return   null  ; 
List   elements  =  FastList  .  newInstance  (  )  ; 
if  (  childElementNames  ==  null  )  return   elements  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  childElementNames  .  contains  (  node  .  getNodeName  (  )  )  )  { 
Element   childElement  =  (  Element  )  node  ; 
elements  .  add  (  childElement  )  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   elements  ; 
} 


public   static   List   childElementList  (  DocumentFragment   fragment  )  { 
if  (  fragment  ==  null  )  return   null  ; 
List   elements  =  FastList  .  newInstance  (  )  ; 
Node   node  =  fragment  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   childElement  =  (  Element  )  node  ; 
elements  .  add  (  childElement  )  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   elements  ; 
} 



public   static   Element   firstChildElement  (  Element   element  ,  Set   childElementNames  )  { 
if  (  element  ==  null  )  return   null  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  childElementNames  .  contains  (  node  .  getNodeName  (  )  )  )  { 
Element   childElement  =  (  Element  )  node  ; 
return   childElement  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   null  ; 
} 



public   static   Element   firstChildElement  (  Element   element  )  { 
if  (  element  ==  null  )  return   null  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   childElement  =  (  Element  )  node  ; 
return   childElement  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   null  ; 
} 



public   static   Element   firstChildElement  (  Element   element  ,  String   childElementName  )  { 
if  (  element  ==  null  )  return   null  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  (  childElementName  ==  null  ||  childElementName  .  equals  (  node  .  getNodeName  (  )  )  )  )  { 
Element   childElement  =  (  Element  )  node  ; 
return   childElement  ; 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   null  ; 
} 



public   static   Element   firstChildElement  (  Element   element  ,  String   childElementName  ,  String   attrName  ,  String   attrValue  )  { 
if  (  element  ==  null  )  return   null  ; 
Node   node  =  element  .  getFirstChild  (  )  ; 
if  (  node  !=  null  )  { 
do  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  (  childElementName  ==  null  ||  childElementName  .  equals  (  node  .  getNodeName  (  )  )  )  )  { 
Element   childElement  =  (  Element  )  node  ; 
String   value  =  childElement  .  getAttribute  (  attrName  )  ; 
if  (  value  !=  null  &&  value  .  equals  (  attrValue  )  )  { 
return   childElement  ; 
} 
} 
}  while  (  (  node  =  node  .  getNextSibling  (  )  )  !=  null  )  ; 
} 
return   null  ; 
} 


public   static   String   childElementValue  (  Element   element  ,  String   childElementName  )  { 
if  (  element  ==  null  )  return   null  ; 
Element   childElement  =  firstChildElement  (  element  ,  childElementName  )  ; 
return   elementValue  (  childElement  )  ; 
} 


public   static   String   childElementValue  (  Element   element  ,  String   childElementName  ,  String   defaultValue  )  { 
if  (  element  ==  null  )  return   defaultValue  ; 
Element   childElement  =  firstChildElement  (  element  ,  childElementName  )  ; 
String   elementValue  =  elementValue  (  childElement  )  ; 
if  (  elementValue  ==  null  ||  elementValue  .  length  (  )  ==  0  )  return   defaultValue  ;  else   return   elementValue  ; 
} 


public   static   String   elementValue  (  Element   element  )  { 
if  (  element  ==  null  )  return   null  ; 
element  .  normalize  (  )  ; 
Node   textNode  =  element  .  getFirstChild  (  )  ; 
if  (  textNode  ==  null  )  return   null  ; 
StringBuffer   valueBuffer  =  new   StringBuffer  (  )  ; 
do  { 
if  (  textNode  .  getNodeType  (  )  ==  Node  .  CDATA_SECTION_NODE  ||  textNode  .  getNodeType  (  )  ==  Node  .  TEXT_NODE  )  { 
valueBuffer  .  append  (  textNode  .  getNodeValue  (  )  )  ; 
} 
}  while  (  (  textNode  =  textNode  .  getNextSibling  (  )  )  !=  null  )  ; 
return   valueBuffer  .  toString  (  )  ; 
} 

public   static   String   checkEmpty  (  String   string  )  { 
if  (  string  !=  null  &&  string  .  length  (  )  >  0  )  return   string  ;  else   return  ""  ; 
} 

public   static   String   checkEmpty  (  String   string1  ,  String   string2  )  { 
if  (  string1  !=  null  &&  string1  .  length  (  )  >  0  )  return   string1  ;  else   if  (  string2  !=  null  &&  string2  .  length  (  )  >  0  )  return   string2  ;  else   return  ""  ; 
} 

public   static   String   checkEmpty  (  String   string1  ,  String   string2  ,  String   string3  )  { 
if  (  string1  !=  null  &&  string1  .  length  (  )  >  0  )  return   string1  ;  else   if  (  string2  !=  null  &&  string2  .  length  (  )  >  0  )  return   string2  ;  else   if  (  string3  !=  null  &&  string3  .  length  (  )  >  0  )  return   string3  ;  else   return  ""  ; 
} 

public   static   boolean   checkBoolean  (  String   str  )  { 
return   checkBoolean  (  str  ,  false  )  ; 
} 

public   static   boolean   checkBoolean  (  String   str  ,  boolean   defaultValue  )  { 
if  (  defaultValue  )  { 
return  !  "false"  .  equals  (  str  )  ; 
}  else  { 
return  "true"  .  equals  (  str  )  ; 
} 
} 







public   static   class   LocalResolver   implements   EntityResolver  { 

private   boolean   hasDTD  =  false  ; 

private   EntityResolver   defaultResolver  ; 

public   LocalResolver  (  EntityResolver   defaultResolver  )  { 
this  .  defaultResolver  =  defaultResolver  ; 
} 








public   InputSource   resolveEntity  (  String   publicId  ,  String   systemId  )  throws   SAXException  ,  IOException  { 
hasDTD  =  false  ; 
String   dtd  =  UtilProperties  .  getSplitPropertyValue  (  UtilURL  .  fromResource  (  "localdtds.properties"  )  ,  publicId  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  dtd  )  )  { 
if  (  Debug  .  verboseOn  (  )  )  Debug  .  logVerbose  (  "[UtilXml.LocalResolver.resolveEntity] resolving DTD with publicId ["  +  publicId  +  "], systemId ["  +  systemId  +  "] and the dtd file is ["  +  dtd  +  "]"  ,  module  )  ; 
try  { 
URL   dtdURL  =  UtilURL  .  fromResource  (  dtd  )  ; 
if  (  dtdURL  ==  null  )  { 
throw   new   GeneralException  (  "Local DTD not found - "  +  dtd  )  ; 
} 
InputStream   dtdStream  =  dtdURL  .  openStream  (  )  ; 
InputSource   inputSource  =  new   InputSource  (  dtdStream  )  ; 
inputSource  .  setPublicId  (  publicId  )  ; 
hasDTD  =  true  ; 
if  (  Debug  .  verboseOn  (  )  )  Debug  .  logVerbose  (  "[UtilXml.LocalResolver.resolveEntity] got LOCAL DTD input source with publicId ["  +  publicId  +  "] and the dtd file is ["  +  dtd  +  "]"  ,  module  )  ; 
return   inputSource  ; 
}  catch  (  Exception   e  )  { 
Debug  .  logWarning  (  e  ,  module  )  ; 
} 
}  else  { 
int   lastSlash  =  systemId  .  lastIndexOf  (  "/"  )  ; 
String   filename  =  null  ; 
if  (  lastSlash  ==  -  1  )  { 
filename  =  systemId  ; 
}  else  { 
filename  =  systemId  .  substring  (  lastSlash  +  1  )  ; 
} 
URL   resourceUrl  =  UtilURL  .  fromResource  (  filename  )  ; 
if  (  resourceUrl  !=  null  )  { 
InputStream   resStream  =  resourceUrl  .  openStream  (  )  ; 
InputSource   inputSource  =  new   InputSource  (  resStream  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  publicId  )  )  { 
inputSource  .  setPublicId  (  publicId  )  ; 
} 
hasDTD  =  true  ; 
if  (  Debug  .  verboseOn  (  )  )  Debug  .  logVerbose  (  "[UtilXml.LocalResolver.resolveEntity] got LOCAL DTD/Schema input source with publicId ["  +  publicId  +  "] and the file/resource is ["  +  filename  +  "]"  ,  module  )  ; 
return   inputSource  ; 
}  else  { 
Debug  .  logWarning  (  "[UtilXml.LocalResolver.resolveEntity] could not find LOCAL DTD/Schema with publicId ["  +  publicId  +  "] and the file/resource is ["  +  filename  +  "]"  ,  module  )  ; 
return   null  ; 
} 
} 
return   defaultResolver  .  resolveEntity  (  publicId  ,  systemId  )  ; 
} 





public   boolean   hasDTD  (  )  { 
return   hasDTD  ; 
} 
} 




public   static   class   LocalErrorHandler   implements   ErrorHandler  { 

private   String   docDescription  ; 

private   LocalResolver   localResolver  ; 

public   LocalErrorHandler  (  String   docDescription  ,  LocalResolver   localResolver  )  { 
this  .  docDescription  =  docDescription  ; 
this  .  localResolver  =  localResolver  ; 
} 

public   void   error  (  SAXParseException   exception  )  { 
if  (  localResolver  .  hasDTD  (  )  )  { 
Debug  .  logError  (  "XmlFileLoader: File "  +  docDescription  +  " process error. Line: "  +  String  .  valueOf  (  exception  .  getLineNumber  (  )  )  +  ". Error message: "  +  exception  .  getMessage  (  )  ,  module  )  ; 
} 
} 

public   void   fatalError  (  SAXParseException   exception  )  { 
if  (  localResolver  .  hasDTD  (  )  )  { 
Debug  .  logError  (  "XmlFileLoader: File "  +  docDescription  +  " process fatal error. Line: "  +  String  .  valueOf  (  exception  .  getLineNumber  (  )  )  +  ". Error message: "  +  exception  .  getMessage  (  )  ,  module  )  ; 
} 
} 

public   void   warning  (  SAXParseException   exception  )  { 
if  (  localResolver  .  hasDTD  (  )  )  { 
Debug  .  logError  (  "XmlFileLoader: File "  +  docDescription  +  " process warning. Line: "  +  String  .  valueOf  (  exception  .  getLineNumber  (  )  )  +  ". Error message: "  +  exception  .  getMessage  (  )  ,  module  )  ; 
} 
} 
} 
} 

