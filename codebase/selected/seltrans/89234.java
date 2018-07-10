package   org  .  apache  .  axis  .  utils  ; 

import   org  .  apache  .  axis  .  AxisEngine  ; 
import   org  .  apache  .  axis  .  Constants  ; 
import   org  .  apache  .  axis  .  InternalException  ; 
import   org  .  apache  .  axis  .  Message  ; 
import   org  .  apache  .  axis  .  MessageContext  ; 
import   org  .  apache  .  axis  .  AxisProperties  ; 
import   org  .  apache  .  axis  .  components  .  encoding  .  XMLEncoder  ; 
import   org  .  apache  .  axis  .  components  .  encoding  .  XMLEncoderFactory  ; 
import   org  .  apache  .  axis  .  components  .  logger  .  LogFactory  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  w3c  .  dom  .  Attr  ; 
import   org  .  w3c  .  dom  .  CharacterData  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  NamedNodeMap  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   org  .  w3c  .  dom  .  Text  ; 
import   org  .  xml  .  sax  .  ErrorHandler  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   org  .  xml  .  sax  .  XMLReader  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 
import   javax  .  xml  .  namespace  .  QName  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilder  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   javax  .  xml  .  soap  .  SOAPException  ; 
import   javax  .  xml  .  soap  .  SOAPMessage  ; 
import   javax  .  xml  .  transform  .  Source  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  sax  .  SAXSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamSource  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  ProtocolException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Stack  ; 

public   class   XMLUtils  { 

protected   static   Log   log  =  LogFactory  .  getLog  (  XMLUtils  .  class  .  getName  (  )  )  ; 

public   static   final   String   httpAuthCharEncoding  =  "ISO-8859-1"  ; 

private   static   final   String   saxParserFactoryProperty  =  "javax.xml.parsers.SAXParserFactory"  ; 

private   static   DocumentBuilderFactory   dbf  =  getDOMFactory  (  )  ; 

private   static   SAXParserFactory   saxFactory  ; 

private   static   Stack   saxParsers  =  new   Stack  (  )  ; 

private   static   DefaultHandler   doNothingContentHandler  =  new   DefaultHandler  (  )  ; 

private   static   String   EMPTY  =  ""  ; 

private   static   ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  EMPTY  .  getBytes  (  )  )  ; 

private   static   boolean   tryReset  =  true  ; 

protected   static   boolean   enableParserReuse  =  false  ; 

private   static   class   ThreadLocalDocumentBuilder   extends   ThreadLocal  { 

protected   Object   initialValue  (  )  { 
try  { 
return   getDOMFactory  (  )  .  newDocumentBuilder  (  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "parserConfigurationException00"  )  ,  e  )  ; 
} 
return   null  ; 
} 
} 

private   static   ThreadLocalDocumentBuilder   documentBuilder  =  new   ThreadLocalDocumentBuilder  (  )  ; 

static  { 
initSAXFactory  (  null  ,  true  ,  false  )  ; 
String   value  =  AxisProperties  .  getProperty  (  AxisEngine  .  PROP_XML_REUSE_SAX_PARSERS  ,  ""  +  true  )  ; 
if  (  value  .  equalsIgnoreCase  (  "true"  )  ||  value  .  equals  (  "1"  )  ||  value  .  equalsIgnoreCase  (  "yes"  )  )  { 
enableParserReuse  =  true  ; 
}  else  { 
enableParserReuse  =  false  ; 
} 
} 






public   static   String   xmlEncodeString  (  String   orig  )  { 
XMLEncoder   encoder  =  getXMLEncoder  (  MessageContext  .  getCurrentContext  (  )  )  ; 
return   encoder  .  encode  (  orig  )  ; 
} 





public   static   XMLEncoder   getXMLEncoder  (  MessageContext   msgContext  )  { 
return   getXMLEncoder  (  getEncoding  (  null  ,  msgContext  )  )  ; 
} 





public   static   XMLEncoder   getXMLEncoder  (  String   encoding  )  { 
XMLEncoder   encoder  =  null  ; 
try  { 
encoder  =  XMLEncoderFactory  .  getEncoder  (  encoding  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
encoder  =  XMLEncoderFactory  .  getDefaultEncoder  (  )  ; 
} 
return   encoder  ; 
} 





public   static   String   getEncoding  (  MessageContext   msgContext  )  { 
XMLEncoder   encoder  =  getXMLEncoder  (  msgContext  )  ; 
return   encoder  .  getEncoding  (  )  ; 
} 





public   static   String   getEncoding  (  )  { 
XMLEncoder   encoder  =  getXMLEncoder  (  MessageContext  .  getCurrentContext  (  )  )  ; 
return   encoder  .  getEncoding  (  )  ; 
} 














public   static   void   initSAXFactory  (  String   factoryClassName  ,  boolean   namespaceAware  ,  boolean   validating  )  { 
if  (  factoryClassName  !=  null  )  { 
try  { 
saxFactory  =  (  SAXParserFactory  )  Class  .  forName  (  factoryClassName  )  .  newInstance  (  )  ; 
if  (  System  .  getProperty  (  saxParserFactoryProperty  )  ==  null  )  { 
System  .  setProperty  (  saxParserFactoryProperty  ,  factoryClassName  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
saxFactory  =  null  ; 
} 
}  else  { 
saxFactory  =  SAXParserFactory  .  newInstance  (  )  ; 
} 
saxFactory  .  setNamespaceAware  (  namespaceAware  )  ; 
saxFactory  .  setValidating  (  validating  )  ; 
saxParsers  .  clear  (  )  ; 
} 

private   static   DocumentBuilderFactory   getDOMFactory  (  )  { 
DocumentBuilderFactory   dbf  ; 
try  { 
dbf  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
dbf  .  setNamespaceAware  (  true  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  e  )  ; 
dbf  =  null  ; 
} 
return  (  dbf  )  ; 
} 






public   static   DocumentBuilder   getDocumentBuilder  (  )  throws   ParserConfigurationException  { 
return  (  DocumentBuilder  )  documentBuilder  .  get  (  )  ; 
} 





public   static   void   releaseDocumentBuilder  (  DocumentBuilder   db  )  { 
try  { 
db  .  setErrorHandler  (  null  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set ErrorHandler to null on DocumentBuilder"  ,  t  )  ; 
} 
try  { 
db  .  setEntityResolver  (  null  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set EntityResolver to null on DocumentBuilder"  ,  t  )  ; 
} 
} 





public   static   synchronized   SAXParser   getSAXParser  (  )  { 
if  (  enableParserReuse  &&  !  saxParsers  .  empty  (  )  )  { 
return  (  SAXParser  )  saxParsers  .  pop  (  )  ; 
} 
try  { 
SAXParser   parser  =  saxFactory  .  newSAXParser  (  )  ; 
XMLReader   reader  =  parser  .  getXMLReader  (  )  ; 
try  { 
reader  .  setEntityResolver  (  new   DefaultEntityResolver  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set EntityResolver on DocumentBuilder"  ,  t  )  ; 
} 
reader  .  setFeature  (  "http://xml.org/sax/features/namespace-prefixes"  ,  false  )  ; 
return   parser  ; 
}  catch  (  ParserConfigurationException   e  )  { 
log  .  error  (  Messages  .  getMessage  (  "parserConfigurationException00"  )  ,  e  )  ; 
return   null  ; 
}  catch  (  SAXException   se  )  { 
log  .  error  (  Messages  .  getMessage  (  "SAXException00"  )  ,  se  )  ; 
return   null  ; 
} 
} 




public   static   void   releaseSAXParser  (  SAXParser   parser  )  { 
if  (  !  tryReset  ||  !  enableParserReuse  )  return  ; 
try  { 
XMLReader   xmlReader  =  parser  .  getXMLReader  (  )  ; 
if  (  null  !=  xmlReader  )  { 
xmlReader  .  setContentHandler  (  doNothingContentHandler  )  ; 
xmlReader  .  setDTDHandler  (  doNothingContentHandler  )  ; 
try  { 
xmlReader  .  setEntityResolver  (  doNothingContentHandler  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set EntityResolver on DocumentBuilder"  ,  t  )  ; 
} 
try  { 
xmlReader  .  setErrorHandler  (  doNothingContentHandler  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set ErrorHandler on DocumentBuilder"  ,  t  )  ; 
} 
synchronized  (  XMLUtils  .  class  )  { 
saxParsers  .  push  (  parser  )  ; 
} 
}  else  { 
tryReset  =  false  ; 
} 
}  catch  (  org  .  xml  .  sax  .  SAXException   e  )  { 
tryReset  =  false  ; 
} 
} 







public   static   Document   newDocument  (  )  throws   ParserConfigurationException  { 
DocumentBuilder   db  =  null  ; 
try  { 
db  =  getDocumentBuilder  (  )  ; 
Document   doc  =  db  .  newDocument  (  )  ; 
return   doc  ; 
}  finally  { 
if  (  db  !=  null  )  { 
releaseDocumentBuilder  (  db  )  ; 
} 
} 
} 








public   static   Document   newDocument  (  InputSource   inp  )  throws   ParserConfigurationException  ,  SAXException  ,  IOException  { 
DocumentBuilder   db  =  null  ; 
try  { 
db  =  getDocumentBuilder  (  )  ; 
try  { 
db  .  setEntityResolver  (  new   DefaultEntityResolver  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set EntityResolver on DocumentBuilder"  ,  t  )  ; 
} 
try  { 
db  .  setErrorHandler  (  new   XMLUtils  .  ParserErrorHandler  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
log  .  debug  (  "Failed to set ErrorHandler on DocumentBuilder"  ,  t  )  ; 
} 
Document   doc  =  db  .  parse  (  inp  )  ; 
return   doc  ; 
}  finally  { 
if  (  db  !=  null  )  { 
releaseDocumentBuilder  (  db  )  ; 
} 
} 
} 








public   static   Document   newDocument  (  InputStream   inp  )  throws   ParserConfigurationException  ,  SAXException  ,  IOException  { 
return   XMLUtils  .  newDocument  (  new   InputSource  (  inp  )  )  ; 
} 








public   static   Document   newDocument  (  String   uri  )  throws   ParserConfigurationException  ,  SAXException  ,  IOException  { 
return   XMLUtils  .  newDocument  (  uri  ,  null  ,  null  )  ; 
} 











public   static   Document   newDocument  (  String   uri  ,  String   username  ,  String   password  )  throws   ParserConfigurationException  ,  SAXException  ,  IOException  { 
InputSource   ins  =  XMLUtils  .  getInputSourceFromURI  (  uri  ,  username  ,  password  )  ; 
Document   doc  =  XMLUtils  .  newDocument  (  ins  )  ; 
if  (  ins  .  getByteStream  (  )  !=  null  )  { 
ins  .  getByteStream  (  )  .  close  (  )  ; 
}  else   if  (  ins  .  getCharacterStream  (  )  !=  null  )  { 
ins  .  getCharacterStream  (  )  .  close  (  )  ; 
} 
return   doc  ; 
} 

private   static   String   privateElementToString  (  Element   element  ,  boolean   omitXMLDecl  )  { 
return   DOM2Writer  .  nodeToString  (  element  ,  omitXMLDecl  )  ; 
} 






public   static   String   ElementToString  (  Element   element  )  { 
return   privateElementToString  (  element  ,  true  )  ; 
} 






public   static   String   DocumentToString  (  Document   doc  )  { 
return   privateElementToString  (  doc  .  getDocumentElement  (  )  ,  false  )  ; 
} 

public   static   String   PrettyDocumentToString  (  Document   doc  )  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
PrettyElementToWriter  (  doc  .  getDocumentElement  (  )  ,  sw  )  ; 
return   sw  .  toString  (  )  ; 
} 

public   static   void   privateElementToWriter  (  Element   element  ,  Writer   writer  ,  boolean   omitXMLDecl  ,  boolean   pretty  )  { 
DOM2Writer  .  serializeAsXML  (  element  ,  writer  ,  omitXMLDecl  ,  pretty  )  ; 
} 

public   static   void   ElementToStream  (  Element   element  ,  OutputStream   out  )  { 
Writer   writer  =  getWriter  (  out  )  ; 
privateElementToWriter  (  element  ,  writer  ,  true  ,  false  )  ; 
} 

public   static   void   PrettyElementToStream  (  Element   element  ,  OutputStream   out  )  { 
Writer   writer  =  getWriter  (  out  )  ; 
privateElementToWriter  (  element  ,  writer  ,  true  ,  true  )  ; 
} 

public   static   void   ElementToWriter  (  Element   element  ,  Writer   writer  )  { 
privateElementToWriter  (  element  ,  writer  ,  true  ,  false  )  ; 
} 

public   static   void   PrettyElementToWriter  (  Element   element  ,  Writer   writer  )  { 
privateElementToWriter  (  element  ,  writer  ,  true  ,  true  )  ; 
} 

public   static   void   DocumentToStream  (  Document   doc  ,  OutputStream   out  )  { 
Writer   writer  =  getWriter  (  out  )  ; 
privateElementToWriter  (  doc  .  getDocumentElement  (  )  ,  writer  ,  false  ,  false  )  ; 
} 

public   static   void   PrettyDocumentToStream  (  Document   doc  ,  OutputStream   out  )  { 
Writer   writer  =  getWriter  (  out  )  ; 
privateElementToWriter  (  doc  .  getDocumentElement  (  )  ,  writer  ,  false  ,  true  )  ; 
} 

private   static   Writer   getWriter  (  OutputStream   os  )  { 
Writer   writer  =  null  ; 
try  { 
writer  =  new   OutputStreamWriter  (  os  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   uee  )  { 
log  .  error  (  Messages  .  getMessage  (  "exception00"  )  ,  uee  )  ; 
writer  =  new   OutputStreamWriter  (  os  )  ; 
} 
return   writer  ; 
} 

public   static   void   DocumentToWriter  (  Document   doc  ,  Writer   writer  )  { 
privateElementToWriter  (  doc  .  getDocumentElement  (  )  ,  writer  ,  false  ,  false  )  ; 
} 

public   static   void   PrettyDocumentToWriter  (  Document   doc  ,  Writer   writer  )  { 
privateElementToWriter  (  doc  .  getDocumentElement  (  )  ,  writer  ,  false  ,  true  )  ; 
} 









public   static   Element   StringToElement  (  String   namespace  ,  String   name  ,  String   string  )  { 
try  { 
Document   doc  =  XMLUtils  .  newDocument  (  )  ; 
Element   element  =  doc  .  createElementNS  (  namespace  ,  name  )  ; 
Text   text  =  doc  .  createTextNode  (  string  )  ; 
element  .  appendChild  (  text  )  ; 
return   element  ; 
}  catch  (  ParserConfigurationException   e  )  { 
throw   new   InternalException  (  e  )  ; 
} 
} 








public   static   String   getInnerXMLString  (  Element   element  )  { 
String   elementString  =  ElementToString  (  element  )  ; 
int   start  ,  end  ; 
start  =  elementString  .  indexOf  (  ">"  )  +  1  ; 
end  =  elementString  .  lastIndexOf  (  "</"  )  ; 
if  (  end  >  0  )  return   elementString  .  substring  (  start  ,  end  )  ;  else   return   null  ; 
} 

public   static   String   getPrefix  (  String   uri  ,  Node   e  )  { 
while  (  e  !=  null  &&  (  e  .  getNodeType  (  )  ==  Element  .  ELEMENT_NODE  )  )  { 
NamedNodeMap   attrs  =  e  .  getAttributes  (  )  ; 
for  (  int   n  =  0  ;  n  <  attrs  .  getLength  (  )  ;  n  ++  )  { 
Attr   a  =  (  Attr  )  attrs  .  item  (  n  )  ; 
String   name  ; 
if  (  (  name  =  a  .  getName  (  )  )  .  startsWith  (  "xmlns:"  )  &&  a  .  getNodeValue  (  )  .  equals  (  uri  )  )  { 
return   name  .  substring  (  6  )  ; 
} 
} 
e  =  e  .  getParentNode  (  )  ; 
} 
return   null  ; 
} 













public   static   String   getNamespace  (  String   prefix  ,  Node   e  ,  Node   stopNode  )  { 
while  (  e  !=  null  &&  (  e  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  )  { 
Attr   attr  =  null  ; 
if  (  prefix  ==  null  )  { 
attr  =  (  (  Element  )  e  )  .  getAttributeNode  (  "xmlns"  )  ; 
}  else  { 
attr  =  (  (  Element  )  e  )  .  getAttributeNodeNS  (  Constants  .  NS_URI_XMLNS  ,  prefix  )  ; 
} 
if  (  attr  !=  null  )  return   attr  .  getValue  (  )  ; 
if  (  e  ==  stopNode  )  return   null  ; 
e  =  e  .  getParentNode  (  )  ; 
} 
return   null  ; 
} 

public   static   String   getNamespace  (  String   prefix  ,  Node   e  )  { 
return   getNamespace  (  prefix  ,  e  ,  null  )  ; 
} 







public   static   QName   getQNameFromString  (  String   str  ,  Node   e  )  { 
return   getQNameFromString  (  str  ,  e  ,  false  )  ; 
} 








public   static   QName   getFullQNameFromString  (  String   str  ,  Node   e  )  { 
return   getQNameFromString  (  str  ,  e  ,  true  )  ; 
} 

private   static   QName   getQNameFromString  (  String   str  ,  Node   e  ,  boolean   defaultNS  )  { 
if  (  str  ==  null  ||  e  ==  null  )  return   null  ; 
int   idx  =  str  .  indexOf  (  ':'  )  ; 
if  (  idx  >  -  1  )  { 
String   prefix  =  str  .  substring  (  0  ,  idx  )  ; 
String   ns  =  getNamespace  (  prefix  ,  e  )  ; 
if  (  ns  ==  null  )  return   null  ; 
return   new   QName  (  ns  ,  str  .  substring  (  idx  +  1  )  )  ; 
}  else  { 
if  (  defaultNS  )  { 
String   ns  =  getNamespace  (  null  ,  e  )  ; 
if  (  ns  !=  null  )  return   new   QName  (  ns  ,  str  )  ; 
} 
return   new   QName  (  ""  ,  str  )  ; 
} 
} 





public   static   String   getStringForQName  (  QName   qname  ,  Element   e  )  { 
String   uri  =  qname  .  getNamespaceURI  (  )  ; 
String   prefix  =  getPrefix  (  uri  ,  e  )  ; 
if  (  prefix  ==  null  )  { 
int   i  =  1  ; 
prefix  =  "ns"  +  i  ; 
while  (  getNamespace  (  prefix  ,  e  )  !=  null  )  { 
i  ++  ; 
prefix  =  "ns"  +  i  ; 
} 
e  .  setAttributeNS  (  Constants  .  NS_URI_XMLNS  ,  "xmlns:"  +  prefix  ,  uri  )  ; 
} 
return   prefix  +  ":"  +  qname  .  getLocalPart  (  )  ; 
} 










public   static   String   getChildCharacterData  (  Element   parentEl  )  { 
if  (  parentEl  ==  null  )  { 
return   null  ; 
} 
Node   tempNode  =  parentEl  .  getFirstChild  (  )  ; 
StringBuffer   strBuf  =  new   StringBuffer  (  )  ; 
CharacterData   charData  ; 
while  (  tempNode  !=  null  )  { 
switch  (  tempNode  .  getNodeType  (  )  )  { 
case   Node  .  TEXT_NODE  : 
case   Node  .  CDATA_SECTION_NODE  : 
charData  =  (  CharacterData  )  tempNode  ; 
strBuf  .  append  (  charData  .  getData  (  )  )  ; 
break  ; 
} 
tempNode  =  tempNode  .  getNextSibling  (  )  ; 
} 
return   strBuf  .  toString  (  )  ; 
} 

public   static   class   ParserErrorHandler   implements   ErrorHandler  { 

protected   static   Log   log  =  LogFactory  .  getLog  (  ParserErrorHandler  .  class  .  getName  (  )  )  ; 




private   String   getParseExceptionInfo  (  SAXParseException   spe  )  { 
String   systemId  =  spe  .  getSystemId  (  )  ; 
if  (  systemId  ==  null  )  { 
systemId  =  "null"  ; 
} 
String   info  =  "URI="  +  systemId  +  " Line="  +  spe  .  getLineNumber  (  )  +  ": "  +  spe  .  getMessage  (  )  ; 
return   info  ; 
} 

public   void   warning  (  SAXParseException   spe  )  throws   SAXException  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  Messages  .  getMessage  (  "warning00"  ,  getParseExceptionInfo  (  spe  )  )  )  ; 
} 

public   void   error  (  SAXParseException   spe  )  throws   SAXException  { 
String   message  =  "Error: "  +  getParseExceptionInfo  (  spe  )  ; 
throw   new   SAXException  (  message  )  ; 
} 

public   void   fatalError  (  SAXParseException   spe  )  throws   SAXException  { 
String   message  =  "Fatal Error: "  +  getParseExceptionInfo  (  spe  )  ; 
throw   new   SAXException  (  message  )  ; 
} 
} 









public   static   InputSource   getInputSourceFromURI  (  String   uri  )  { 
return   new   InputSource  (  uri  )  ; 
} 






public   static   InputSource   sourceToInputSource  (  Source   source  )  { 
if  (  source   instanceof   SAXSource  )  { 
return  (  (  SAXSource  )  source  )  .  getInputSource  (  )  ; 
}  else   if  (  source   instanceof   DOMSource  )  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
Node   node  =  (  (  DOMSource  )  source  )  .  getNode  (  )  ; 
if  (  node   instanceof   Document  )  { 
node  =  (  (  Document  )  node  )  .  getDocumentElement  (  )  ; 
} 
Element   domElement  =  (  Element  )  node  ; 
ElementToStream  (  domElement  ,  baos  )  ; 
InputSource   isource  =  new   InputSource  (  source  .  getSystemId  (  )  )  ; 
isource  .  setByteStream  (  new   ByteArrayInputStream  (  baos  .  toByteArray  (  )  )  )  ; 
return   isource  ; 
}  else   if  (  source   instanceof   StreamSource  )  { 
StreamSource   ss  =  (  StreamSource  )  source  ; 
InputSource   isource  =  new   InputSource  (  ss  .  getSystemId  (  )  )  ; 
isource  .  setByteStream  (  ss  .  getInputStream  (  )  )  ; 
isource  .  setCharacterStream  (  ss  .  getReader  (  )  )  ; 
isource  .  setPublicId  (  ss  .  getPublicId  (  )  )  ; 
return   isource  ; 
}  else  { 
return   getInputSourceFromURI  (  source  .  getSystemId  (  )  )  ; 
} 
} 















private   static   InputSource   getInputSourceFromURI  (  String   uri  ,  String   username  ,  String   password  )  throws   IOException  ,  ProtocolException  ,  UnsupportedEncodingException  { 
URL   wsdlurl  =  null  ; 
try  { 
wsdlurl  =  new   URL  (  uri  )  ; 
}  catch  (  MalformedURLException   e  )  { 
return   new   InputSource  (  uri  )  ; 
} 
if  (  username  ==  null  &&  wsdlurl  .  getUserInfo  (  )  ==  null  )  { 
return   new   InputSource  (  uri  )  ; 
} 
if  (  !  wsdlurl  .  getProtocol  (  )  .  startsWith  (  "http"  )  )  { 
return   new   InputSource  (  uri  )  ; 
} 
URLConnection   connection  =  wsdlurl  .  openConnection  (  )  ; 
if  (  !  (  connection   instanceof   HttpURLConnection  )  )  { 
return   new   InputSource  (  uri  )  ; 
} 
HttpURLConnection   uconn  =  (  HttpURLConnection  )  connection  ; 
String   userinfo  =  wsdlurl  .  getUserInfo  (  )  ; 
uconn  .  setRequestMethod  (  "GET"  )  ; 
uconn  .  setAllowUserInteraction  (  false  )  ; 
uconn  .  setDefaultUseCaches  (  false  )  ; 
uconn  .  setDoInput  (  true  )  ; 
uconn  .  setDoOutput  (  false  )  ; 
uconn  .  setInstanceFollowRedirects  (  true  )  ; 
uconn  .  setUseCaches  (  false  )  ; 
String   auth  =  null  ; 
if  (  userinfo  !=  null  )  { 
auth  =  userinfo  ; 
}  else   if  (  username  !=  null  )  { 
auth  =  (  password  ==  null  )  ?  username  :  username  +  ":"  +  password  ; 
} 
if  (  auth  !=  null  )  { 
uconn  .  setRequestProperty  (  "Authorization"  ,  "Basic "  +  base64encode  (  auth  .  getBytes  (  httpAuthCharEncoding  )  )  )  ; 
} 
uconn  .  connect  (  )  ; 
return   new   InputSource  (  uconn  .  getInputStream  (  )  )  ; 
} 

public   static   final   String   base64encode  (  byte  [  ]  bytes  )  { 
return   new   String  (  Base64  .  encode  (  bytes  )  )  ; 
} 

public   static   InputSource   getEmptyInputSource  (  )  { 
return   new   InputSource  (  bais  )  ; 
} 








public   static   Node   findNode  (  Node   node  ,  QName   name  )  { 
if  (  name  .  getNamespaceURI  (  )  .  equals  (  node  .  getNamespaceURI  (  )  )  &&  name  .  getLocalPart  (  )  .  equals  (  node  .  getLocalName  (  )  )  )  return   node  ; 
NodeList   children  =  node  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  getLength  (  )  ;  i  ++  )  { 
Node   ret  =  findNode  (  children  .  item  (  i  )  ,  name  )  ; 
if  (  ret  !=  null  )  return   ret  ; 
} 
return   null  ; 
} 






public   static   void   normalize  (  Node   node  )  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  TEXT_NODE  )  { 
String   data  =  (  (  Text  )  node  )  .  getData  (  )  ; 
if  (  data  .  length  (  )  >  0  )  { 
char   ch  =  data  .  charAt  (  data  .  length  (  )  -  1  )  ; 
if  (  ch  ==  '\n'  ||  ch  ==  '\r'  ||  ch  ==  ' '  )  { 
String   data2  =  trim  (  data  )  ; 
(  (  Text  )  node  )  .  setData  (  data2  )  ; 
} 
} 
} 
for  (  Node   currentChild  =  node  .  getFirstChild  (  )  ;  currentChild  !=  null  ;  currentChild  =  currentChild  .  getNextSibling  (  )  )  { 
normalize  (  currentChild  )  ; 
} 
} 

public   static   String   trim  (  String   str  )  { 
if  (  str  .  length  (  )  ==  0  )  { 
return   str  ; 
} 
if  (  str  .  length  (  )  ==  1  )  { 
if  (  "\r"  .  equals  (  str  )  ||  "\n"  .  equals  (  str  )  )  { 
return  ""  ; 
}  else  { 
return   str  ; 
} 
} 
int   lastIdx  =  str  .  length  (  )  -  1  ; 
char   last  =  str  .  charAt  (  lastIdx  )  ; 
while  (  lastIdx  >  0  )  { 
if  (  last  !=  '\n'  &&  last  !=  '\r'  &&  last  !=  ' '  )  break  ; 
lastIdx  --  ; 
last  =  str  .  charAt  (  lastIdx  )  ; 
} 
if  (  lastIdx  ==  0  )  return  ""  ; 
return   str  .  substring  (  0  ,  lastIdx  )  ; 
} 







public   static   Element  [  ]  asElementArray  (  List   list  )  { 
Element  [  ]  elements  =  new   Element  [  list  .  size  (  )  ]  ; 
int   i  =  0  ; 
Iterator   detailIter  =  list  .  iterator  (  )  ; 
while  (  detailIter  .  hasNext  (  )  )  { 
elements  [  i  ++  ]  =  (  Element  )  detailIter  .  next  (  )  ; 
} 
return   elements  ; 
} 

public   static   String   getEncoding  (  Message   message  ,  MessageContext   msgContext  )  { 
return   getEncoding  (  message  ,  msgContext  ,  XMLEncoderFactory  .  getDefaultEncoder  (  )  )  ; 
} 

public   static   String   getEncoding  (  Message   message  ,  MessageContext   msgContext  ,  XMLEncoder   defaultEncoder  )  { 
String   encoding  =  null  ; 
try  { 
if  (  message  !=  null  )  { 
encoding  =  (  String  )  message  .  getProperty  (  SOAPMessage  .  CHARACTER_SET_ENCODING  )  ; 
} 
}  catch  (  SOAPException   e  )  { 
} 
if  (  msgContext  ==  null  )  { 
msgContext  =  MessageContext  .  getCurrentContext  (  )  ; 
} 
if  (  msgContext  !=  null  &&  encoding  ==  null  )  { 
encoding  =  (  String  )  msgContext  .  getProperty  (  SOAPMessage  .  CHARACTER_SET_ENCODING  )  ; 
} 
if  (  msgContext  !=  null  &&  encoding  ==  null  &&  msgContext  .  getAxisEngine  (  )  !=  null  )  { 
encoding  =  (  String  )  msgContext  .  getAxisEngine  (  )  .  getOption  (  AxisEngine  .  PROP_XML_ENCODING  )  ; 
} 
if  (  encoding  ==  null  &&  defaultEncoder  !=  null  )  { 
encoding  =  defaultEncoder  .  getEncoding  (  )  ; 
} 
return   encoding  ; 
} 
} 

