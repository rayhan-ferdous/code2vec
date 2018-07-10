package   org  .  mobicents  .  eclipslee  .  util  ; 

import   java  .  net  .  URL  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   org  .  mobicents  .  eclipslee  .  util  .  exception  .  XMLException  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Attr  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   org  .  w3c  .  dom  .  NamedNodeMap  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   org  .  xml  .  sax  .  EntityResolver  ; 
import   org  .  xml  .  sax  .  ErrorHandler  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilder  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 

public   final   class   XMLParser  { 








public   static   Document   getDocument  (  URL   url  )  throws   IllegalArgumentException  ,  IOException  { 
return   getDocument  (  url  ,  null  )  ; 
} 










public   static   Document   getDocument  (  URL   url  ,  EntityResolver   resolver  )  throws   IllegalArgumentException  ,  IOException  { 
return   getDocument  (  url  ,  resolver  ,  false  )  ; 
} 

private   static   class   ParseErrorHandler   implements   ErrorHandler  { 

public   void   error  (  SAXParseException   e  )  throws   SAXException  { 
throw   e  ; 
} 

public   void   fatalError  (  SAXParseException   e  )  throws   SAXException  { 
throw   e  ; 
} 

public   void   warning  (  SAXParseException   e  )  throws   SAXException  { 
System  .  err  .  println  (  "Warning: "  +  e  )  ; 
} 
} 

public   static   Document   getDocument  (  URL   url  ,  EntityResolver   resolver  ,  boolean   validating  )  throws   IllegalArgumentException  ,  IOException  { 
if  (  url  ==  null  )  throw   new   IllegalArgumentException  (  "URL is null"  )  ; 
InputStream   is  =  null  ; 
try  { 
is  =  url  .  openStream  (  )  ; 
InputSource   source  =  new   InputSource  (  is  )  ; 
source  .  setSystemId  (  url  .  toString  (  )  )  ; 
return   getDocument  (  source  ,  resolver  ,  validating  )  ; 
}  finally  { 
try  { 
if  (  is  !=  null  )  is  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
} 
} 
} 

public   static   Document   getDocument  (  InputSource   source  ,  EntityResolver   resolver  ,  boolean   validating  )  throws   IOException  { 
try  { 
DocumentBuilderFactory   factory  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
factory  .  setValidating  (  validating  )  ; 
DocumentBuilder   builder  =  factory  .  newDocumentBuilder  (  )  ; 
if  (  resolver  !=  null  )  builder  .  setEntityResolver  (  resolver  )  ; 
builder  .  setErrorHandler  (  new   ParseErrorHandler  (  )  )  ; 
return   builder  .  parse  (  source  )  ; 
}  catch  (  SAXParseException   e  )  { 
throw   new   XMLException  (  "parse error at "  +  e  .  getSystemId  (  )  +  ":"  +  e  .  getLineNumber  (  )  ,  e  )  ; 
}  catch  (  SAXException   e  )  { 
throw   new   XMLException  (  "error reading document"  ,  e  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
throw   new   XMLException  (  "config error"  ,  e  )  ; 
} 
} 














public   static   List   getElementsByTagName  (  Element   parent  ,  String   tagName  )  throws   IllegalArgumentException  { 
if  (  parent  ==  null  )  throw   new   IllegalArgumentException  (  "parent is null"  )  ; 
if  (  tagName  ==  null  )  throw   new   IllegalArgumentException  (  "tagName is null"  )  ; 
if  (  tagName  .  length  (  )  ==  0  )  throw   new   IllegalArgumentException  (  "tagName is zero-length"  )  ; 
NodeList   nodelist  =  parent  .  getChildNodes  (  )  ; 
ArrayList   elements  =  new   ArrayList  (  nodelist  .  getLength  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  nodelist  .  getLength  (  )  ;  i  ++  )  { 
Node   node  =  nodelist  .  item  (  i  )  ; 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  tagName  .  equals  (  (  (  Element  )  node  )  .  getTagName  (  )  )  )  { 
elements  .  add  (  (  Element  )  node  )  ; 
} 
} 
return   elements  ; 
} 












public   static   Element   getUniqueElement  (  Element   parent  ,  String   tagName  )  throws   IllegalArgumentException  ,  XMLException  { 
Iterator   elements  =  getElementsByTagName  (  parent  ,  tagName  )  .  iterator  (  )  ; 
if  (  elements  .  hasNext  (  )  )  { 
Element   element  =  (  Element  )  elements  .  next  (  )  ; 
if  (  elements  .  hasNext  (  )  )  { 
throw   new   XMLException  (  "Only one \""  +  tagName  +  "\" element expected"  )  ; 
} 
return   element  ; 
}  else  { 
throw   new   XMLException  (  "The \""  +  tagName  +  "\" element was not found"  )  ; 
} 
} 












public   static   Element   getOptionalElement  (  Element   parent  ,  String   tagName  )  throws   IllegalArgumentException  ,  XMLException  { 
Iterator   elements  =  getElementsByTagName  (  parent  ,  tagName  )  .  iterator  (  )  ; 
if  (  elements  .  hasNext  (  )  )  { 
Element   element  =  (  Element  )  elements  .  next  (  )  ; 
if  (  elements  .  hasNext  (  )  )  { 
throw   new   XMLException  (  "At most one \""  +  tagName  +  "\" element expected"  )  ; 
} 
return   element  ; 
}  else  { 
return   null  ; 
} 
} 










public   static   String   getElementContent  (  Element   element  )  throws   IllegalArgumentException  ,  XMLException  { 
return   getRawElementContent  (  element  )  .  trim  (  )  ; 
} 









private   static   String   getRawElementContent  (  Element   element  )  throws   IllegalArgumentException  ,  XMLException  { 
if  (  element  ==  null  )  throw   new   IllegalArgumentException  (  "Element is null"  )  ; 
NodeList   nodelist  =  element  .  getChildNodes  (  )  ; 
if  (  nodelist  .  getLength  (  )  ==  0  )  return  ""  ; 
if  (  (  nodelist  .  getLength  (  )  !=  1  )  ||  (  nodelist  .  item  (  0  )  .  getNodeType  (  )  !=  Node  .  TEXT_NODE  )  )  throw   new   XMLException  (  "Not a text node: "  +  element  )  ; 
return   nodelist  .  item  (  0  )  .  getNodeValue  (  )  ; 
} 










public   static   String   getTextElement  (  Element   parent  ,  String   name  )  throws   XMLException  { 
return   getElementContent  (  getUniqueElement  (  parent  ,  name  )  )  ; 
} 










public   static   String   getRawTextElement  (  Element   parent  ,  String   name  )  throws   XMLException  { 
return   getRawElementContent  (  getUniqueElement  (  parent  ,  name  )  )  ; 
} 

public   static   String   getOptionalTextElement  (  Element   parent  ,  String   name  )  throws   XMLException  { 
Element   e  =  getOptionalElement  (  parent  ,  name  )  ; 
if  (  e  ==  null  )  return   null  ; 
return   getElementContent  (  e  )  ; 
} 









public   static   void   createTextElement  (  Element   parent  ,  String   tagName  ,  String   text  )  { 
Document   doc  =  parent  .  getOwnerDocument  (  )  ; 
Element   newElement  =  doc  .  createElement  (  tagName  )  ; 
newElement  .  appendChild  (  doc  .  createTextNode  (  text  )  )  ; 
parent  .  appendChild  (  newElement  )  ; 
} 











































public   static   void   mergeDocuments  (  Document   toModify  ,  Document   toMerge  ,  String  [  ]  tagsToMerge  )  throws   XMLException  { 
mergeTree  (  toModify  .  getDocumentElement  (  )  ,  toModify  .  getDocumentElement  (  )  ,  toMerge  .  getDocumentElement  (  )  ,  tagsToMerge  )  ; 
} 






public   static   void   mergeDocuments  (  Document   toModify  ,  Document   toMerge  )  throws   XMLException  { 
mergeDocuments  (  toModify  ,  toMerge  ,  new   String  [  0  ]  )  ; 
} 

private   static   Element   findID  (  Node   top  ,  String   id  )  { 
NodeList   childList  =  top  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  childList  .  getLength  (  )  ;  ++  i  )  { 
Node   child  =  childList  .  item  (  i  )  ; 
if  (  child  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   elem  =  (  Element  )  child  ; 
Attr   idAttr  =  elem  .  getAttributeNode  (  "id"  )  ; 
if  (  idAttr  !=  null  )  { 
if  (  idAttr  .  getValue  (  )  .  equals  (  id  )  )  return   elem  ;  else   continue  ; 
} 
} 
Element   found  =  findID  (  child  ,  id  )  ; 
if  (  found  !=  null  )  return   found  ; 
} 
return   null  ; 
} 

private   static   void   mergeTree  (  Node   top  ,  Node   dest  ,  Node   source  ,  String  [  ]  tagsToMerge  )  throws   XMLException  { 
NodeList   sourceList  =  source  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  sourceList  .  getLength  (  )  ;  ++  i  )  { 
Node   sourceChild  =  sourceList  .  item  (  i  )  ; 
if  (  sourceChild  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   elem  =  (  Element  )  sourceChild  ; 
Attr   idAttr  =  elem  .  getAttributeNode  (  "id"  )  ; 
if  (  idAttr  !=  null  )  { 
Element   mergeDest  =  findID  (  top  ,  idAttr  .  getValue  (  )  )  ; 
if  (  mergeDest  ==  null  )  throw   new   XMLException  (  "Missing ID attribute: "  +  idAttr  .  getValue  (  )  )  ; 
mergeTree  (  mergeDest  ,  mergeDest  ,  elem  ,  tagsToMerge  )  ; 
continue  ; 
} 
boolean   found  =  false  ; 
NodeList   destChildren  =  dest  .  getChildNodes  (  )  ; 
for  (  int   j  =  0  ;  j  <  destChildren  .  getLength  (  )  &&  !  found  ;  ++  j  )  { 
Node   destNode  =  destChildren  .  item  (  j  )  ; 
if  (  destNode  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  (  (  Element  )  destNode  )  .  getTagName  (  )  .  equals  (  elem  .  getTagName  (  )  )  )  { 
for  (  int   k  =  0  ;  k  <  tagsToMerge  .  length  &&  !  found  ;  ++  k  )  { 
if  (  elem  .  getTagName  (  )  .  equals  (  tagsToMerge  [  k  ]  )  )  { 
found  =  true  ; 
mergeTree  (  top  ,  destNode  ,  sourceChild  ,  tagsToMerge  )  ; 
} 
} 
} 
} 
if  (  found  )  continue  ; 
} 
switch  (  sourceChild  .  getNodeType  (  )  )  { 
default  : 
break  ; 
case   Node  .  ELEMENT_NODE  : 
{ 
Node   newNode  =  dest  .  getOwnerDocument  (  )  .  importNode  (  sourceChild  ,  false  )  ; 
dest  .  appendChild  (  newNode  )  ; 
mergeTree  (  top  ,  newNode  ,  sourceChild  ,  tagsToMerge  )  ; 
break  ; 
} 
case   Node  .  PROCESSING_INSTRUCTION_NODE  : 
case   Node  .  TEXT_NODE  : 
case   Node  .  CDATA_SECTION_NODE  : 
case   Node  .  COMMENT_NODE  : 
case   Node  .  ENTITY_REFERENCE_NODE  : 
{ 
Node   newNode  =  dest  .  getOwnerDocument  (  )  .  importNode  (  sourceChild  ,  true  )  ; 
dest  .  appendChild  (  newNode  )  ; 
break  ; 
} 
} 
} 
} 
} 

