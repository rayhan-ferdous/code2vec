package   org  .  apache  .  axiom  .  om  .  xpath  ; 

import   org  .  apache  .  axiom  .  om  .  OMAttribute  ; 
import   org  .  apache  .  axiom  .  om  .  OMComment  ; 
import   org  .  apache  .  axiom  .  om  .  OMContainer  ; 
import   org  .  apache  .  axiom  .  om  .  OMDocument  ; 
import   org  .  apache  .  axiom  .  om  .  OMElement  ; 
import   org  .  apache  .  axiom  .  om  .  OMFactory  ; 
import   org  .  apache  .  axiom  .  om  .  OMNamespace  ; 
import   org  .  apache  .  axiom  .  om  .  OMNode  ; 
import   org  .  apache  .  axiom  .  om  .  OMProcessingInstruction  ; 
import   org  .  apache  .  axiom  .  om  .  OMText  ; 
import   org  .  apache  .  axiom  .  om  .  impl  .  OMNamespaceImpl  ; 
import   org  .  apache  .  axiom  .  om  .  impl  .  builder  .  StAXOMBuilder  ; 
import   org  .  apache  .  axiom  .  om  .  util  .  StAXUtils  ; 
import   org  .  jaxen  .  BaseXPath  ; 
import   org  .  jaxen  .  DefaultNavigator  ; 
import   org  .  jaxen  .  FunctionCallException  ; 
import   org  .  jaxen  .  JaxenConstants  ; 
import   org  .  jaxen  .  UnsupportedAxisException  ; 
import   org  .  jaxen  .  XPath  ; 
import   org  .  jaxen  .  saxpath  .  SAXPathException  ; 
import   org  .  jaxen  .  util  .  SingleObjectIterator  ; 
import   javax  .  xml  .  namespace  .  QName  ; 
import   javax  .  xml  .  stream  .  XMLInputFactory  ; 
import   javax  .  xml  .  stream  .  XMLStreamReader  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 

public   class   DocumentNavigator   extends   DefaultNavigator  { 

private   static   final   long   serialVersionUID  =  7325116153349780805L  ; 










public   XPath   parseXPath  (  String   xpath  )  throws   SAXPathException  { 
return   new   BaseXPath  (  xpath  ,  this  )  ; 
} 







public   String   getElementNamespaceUri  (  Object   object  )  { 
OMElement   attr  =  (  OMElement  )  object  ; 
return   attr  .  getQName  (  )  .  getNamespaceURI  (  )  ; 
} 







public   String   getElementName  (  Object   object  )  { 
OMElement   attr  =  (  OMElement  )  object  ; 
return   attr  .  getQName  (  )  .  getLocalPart  (  )  ; 
} 







public   String   getElementQName  (  Object   object  )  { 
OMElement   attr  =  (  OMElement  )  object  ; 
String   prefix  =  null  ; 
OMNamespace   namespace  =  attr  .  getNamespace  (  )  ; 
if  (  namespace  !=  null  )  { 
prefix  =  namespace  .  getPrefix  (  )  ; 
} 
if  (  prefix  ==  null  ||  ""  .  equals  (  prefix  )  )  { 
return   attr  .  getQName  (  )  .  getLocalPart  (  )  ; 
} 
return   prefix  +  ":"  +  namespace  .  getNamespaceURI  (  )  ; 
} 







public   String   getAttributeNamespaceUri  (  Object   object  )  { 
OMAttribute   attr  =  (  OMAttribute  )  object  ; 
return   attr  .  getQName  (  )  .  getNamespaceURI  (  )  ; 
} 







public   String   getAttributeName  (  Object   object  )  { 
OMAttribute   attr  =  (  OMAttribute  )  object  ; 
return   attr  .  getQName  (  )  .  getLocalPart  (  )  ; 
} 







public   String   getAttributeQName  (  Object   object  )  { 
OMAttribute   attr  =  (  OMAttribute  )  object  ; 
String   prefix  =  attr  .  getNamespace  (  )  .  getPrefix  (  )  ; 
if  (  prefix  ==  null  ||  ""  .  equals  (  prefix  )  )  { 
return   attr  .  getQName  (  )  .  getLocalPart  (  )  ; 
} 
return   prefix  +  ":"  +  attr  .  getNamespace  (  )  .  getNamespaceURI  (  )  ; 
} 









public   boolean   isDocument  (  Object   object  )  { 
return   object   instanceof   OMDocument  ; 
} 








public   boolean   isElement  (  Object   object  )  { 
return   object   instanceof   OMElement  ; 
} 








public   boolean   isAttribute  (  Object   object  )  { 
return   object   instanceof   OMAttribute  ; 
} 








public   boolean   isNamespace  (  Object   object  )  { 
return   object   instanceof   OMNamespace  ; 
} 







public   boolean   isComment  (  Object   object  )  { 
return  (  object   instanceof   OMComment  )  ; 
} 







public   boolean   isText  (  Object   object  )  { 
return  (  object   instanceof   OMText  )  ; 
} 








public   boolean   isProcessingInstruction  (  Object   object  )  { 
return  (  object   instanceof   OMProcessingInstruction  )  ; 
} 








public   String   getCommentStringValue  (  Object   object  )  { 
return  (  (  OMComment  )  object  )  .  getValue  (  )  ; 
} 








public   String   getElementStringValue  (  Object   object  )  { 
if  (  isElement  (  object  )  )  { 
return   getStringValue  (  (  OMElement  )  object  ,  new   StringBuffer  (  )  )  .  toString  (  )  ; 
} 
return   null  ; 
} 

private   StringBuffer   getStringValue  (  OMNode   node  ,  StringBuffer   buffer  )  { 
if  (  isText  (  node  )  )  { 
buffer  .  append  (  (  (  OMText  )  node  )  .  getText  (  )  )  ; 
}  else   if  (  node   instanceof   OMElement  )  { 
Iterator   children  =  (  (  OMElement  )  node  )  .  getChildren  (  )  ; 
while  (  children  .  hasNext  (  )  )  { 
getStringValue  (  (  OMNode  )  children  .  next  (  )  ,  buffer  )  ; 
} 
} 
return   buffer  ; 
} 








public   String   getAttributeStringValue  (  Object   object  )  { 
return  (  (  OMAttribute  )  object  )  .  getAttributeValue  (  )  ; 
} 








public   String   getNamespaceStringValue  (  Object   object  )  { 
return  (  (  OMNamespace  )  object  )  .  getNamespaceURI  (  )  ; 
} 








public   String   getTextStringValue  (  Object   object  )  { 
return  (  (  OMText  )  object  )  .  getText  (  )  ; 
} 







public   String   getNamespacePrefix  (  Object   object  )  { 
return  (  (  OMNamespace  )  object  )  .  getPrefix  (  )  ; 
} 









public   Iterator   getChildAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
if  (  contextNode   instanceof   OMContainer  )  { 
return  (  (  OMContainer  )  contextNode  )  .  getChildren  (  )  ; 
} 
return   JaxenConstants  .  EMPTY_ITERATOR  ; 
} 

public   Iterator   getDescendantAxisIterator  (  Object   object  )  throws   UnsupportedAxisException  { 
return   super  .  getDescendantAxisIterator  (  object  )  ; 
} 









public   Iterator   getAttributeAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
if  (  isElement  (  contextNode  )  )  { 
ArrayList   attributes  =  new   ArrayList  (  )  ; 
Iterator   i  =  (  (  OMElement  )  contextNode  )  .  getAllAttributes  (  )  ; 
while  (  i  !=  null  &&  i  .  hasNext  (  )  )  { 
attributes  .  add  (  new   OMAttributeEx  (  (  OMAttribute  )  i  .  next  (  )  ,  (  OMContainer  )  contextNode  ,  (  (  OMElement  )  contextNode  )  .  getOMFactory  (  )  )  )  ; 
} 
return   attributes  .  iterator  (  )  ; 
} 
return   JaxenConstants  .  EMPTY_ITERATOR  ; 
} 









public   Iterator   getNamespaceAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
if  (  !  (  contextNode   instanceof   OMContainer  &&  contextNode   instanceof   OMElement  )  )  { 
return   JaxenConstants  .  EMPTY_ITERATOR  ; 
} 
List   nsList  =  new   ArrayList  (  )  ; 
HashSet   prefixes  =  new   HashSet  (  )  ; 
for  (  OMContainer   context  =  (  OMContainer  )  contextNode  ;  context  !=  null  &&  !  (  context   instanceof   OMDocument  )  ;  context  =  (  (  OMElement  )  context  )  .  getParent  (  )  )  { 
OMElement   element  =  (  OMElement  )  context  ; 
ArrayList   declaredNS  =  new   ArrayList  (  )  ; 
Iterator   i  =  element  .  getAllDeclaredNamespaces  (  )  ; 
while  (  i  !=  null  &&  i  .  hasNext  (  )  )  { 
declaredNS  .  add  (  i  .  next  (  )  )  ; 
} 
declaredNS  .  add  (  element  .  getNamespace  (  )  )  ; 
for  (  Iterator   iter  =  element  .  getAllAttributes  (  )  ;  iter  !=  null  &&  iter  .  hasNext  (  )  ;  )  { 
OMAttribute   attr  =  (  OMAttribute  )  iter  .  next  (  )  ; 
OMNamespace   namespace  =  attr  .  getNamespace  (  )  ; 
if  (  namespace  !=  null  )  { 
declaredNS  .  add  (  namespace  )  ; 
} 
} 
for  (  Iterator   iter  =  declaredNS  .  iterator  (  )  ;  iter  !=  null  &&  iter  .  hasNext  (  )  ;  )  { 
OMNamespace   namespace  =  (  OMNamespace  )  iter  .  next  (  )  ; 
if  (  namespace  !=  null  )  { 
String   prefix  =  namespace  .  getPrefix  (  )  ; 
if  (  prefix  !=  null  &&  !  prefixes  .  contains  (  prefix  )  )  { 
prefixes  .  add  (  prefix  )  ; 
nsList  .  add  (  new   OMNamespaceEx  (  namespace  ,  context  )  )  ; 
} 
} 
} 
} 
nsList  .  add  (  new   OMNamespaceEx  (  new   OMNamespaceImpl  (  "http://www.w3.org/XML/1998/namespace"  ,  "xml"  )  ,  (  OMContainer  )  contextNode  )  )  ; 
return   nsList  .  iterator  (  )  ; 
} 









public   Iterator   getSelfAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getSelfAxisIterator  (  contextNode  )  ; 
} 









public   Iterator   getDescendantOrSelfAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getDescendantOrSelfAxisIterator  (  contextNode  )  ; 
} 









public   Iterator   getAncestorOrSelfAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getAncestorOrSelfAxisIterator  (  contextNode  )  ; 
} 









public   Iterator   getParentAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
if  (  contextNode   instanceof   OMNode  )  { 
return   new   SingleObjectIterator  (  (  (  OMNode  )  contextNode  )  .  getParent  (  )  )  ; 
}  else   if  (  contextNode   instanceof   OMNamespaceEx  )  { 
return   new   SingleObjectIterator  (  (  (  OMNamespaceEx  )  contextNode  )  .  getParent  (  )  )  ; 
}  else   if  (  contextNode   instanceof   OMAttributeEx  )  { 
return   new   SingleObjectIterator  (  (  (  OMAttributeEx  )  contextNode  )  .  getParent  (  )  )  ; 
} 
return   JaxenConstants  .  EMPTY_ITERATOR  ; 
} 









public   Iterator   getAncestorAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getAncestorAxisIterator  (  contextNode  )  ; 
} 









public   Iterator   getFollowingSiblingAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
ArrayList   list  =  new   ArrayList  (  )  ; 
if  (  contextNode  !=  null  &&  contextNode   instanceof   OMNode  )  { 
while  (  contextNode  !=  null  &&  contextNode   instanceof   OMNode  )  { 
contextNode  =  (  (  OMNode  )  contextNode  )  .  getNextOMSibling  (  )  ; 
if  (  contextNode  !=  null  )  list  .  add  (  contextNode  )  ; 
} 
} 
return   list  .  iterator  (  )  ; 
} 









public   Iterator   getPrecedingSiblingAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
ArrayList   list  =  new   ArrayList  (  )  ; 
if  (  contextNode  !=  null  &&  contextNode   instanceof   OMNode  )  { 
while  (  contextNode  !=  null  &&  contextNode   instanceof   OMNode  )  { 
contextNode  =  (  (  OMNode  )  contextNode  )  .  getPreviousOMSibling  (  )  ; 
if  (  contextNode  !=  null  )  list  .  add  (  contextNode  )  ; 
} 
} 
return   list  .  iterator  (  )  ; 
} 









public   Iterator   getFollowingAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getFollowingAxisIterator  (  contextNode  )  ; 
} 









public   Iterator   getPrecedingAxisIterator  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
return   super  .  getPrecedingAxisIterator  (  contextNode  )  ; 
} 








public   Object   getDocument  (  String   uri  )  throws   FunctionCallException  { 
try  { 
XMLStreamReader   parser  ; 
XMLInputFactory   xmlInputFactory  =  StAXUtils  .  getXMLInputFactory  (  )  ; 
Boolean   oldValue  =  (  Boolean  )  xmlInputFactory  .  getProperty  (  XMLInputFactory  .  IS_COALESCING  )  ; 
try  { 
xmlInputFactory  .  setProperty  (  XMLInputFactory  .  IS_COALESCING  ,  Boolean  .  TRUE  )  ; 
if  (  uri  .  indexOf  (  ':'  )  ==  -  1  )  { 
parser  =  xmlInputFactory  .  createXMLStreamReader  (  new   FileInputStream  (  uri  )  )  ; 
}  else  { 
URL   url  =  new   URL  (  uri  )  ; 
parser  =  xmlInputFactory  .  createXMLStreamReader  (  url  .  openStream  (  )  )  ; 
} 
}  finally  { 
if  (  oldValue  !=  null  )  { 
xmlInputFactory  .  setProperty  (  XMLInputFactory  .  IS_COALESCING  ,  oldValue  )  ; 
} 
StAXUtils  .  releaseXMLInputFactory  (  xmlInputFactory  )  ; 
} 
StAXOMBuilder   builder  =  new   StAXOMBuilder  (  parser  )  ; 
return   builder  .  getDocumentElement  (  )  .  getParent  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   FunctionCallException  (  e  )  ; 
} 
} 











public   Object   getElementById  (  Object   contextNode  ,  String   elementId  )  { 
return   super  .  getElementById  (  contextNode  ,  elementId  )  ; 
} 








public   Object   getDocumentNode  (  Object   contextNode  )  { 
if  (  contextNode   instanceof   OMDocument  )  { 
return   contextNode  ; 
} 
OMContainer   parent  =  (  (  OMNode  )  contextNode  )  .  getParent  (  )  ; 
if  (  parent  ==  null  )  { 
return   contextNode  ; 
}  else  { 
return   getDocumentNode  (  parent  )  ; 
} 
} 












public   String   translateNamespacePrefixToUri  (  String   prefix  ,  Object   element  )  { 
return   super  .  translateNamespacePrefixToUri  (  prefix  ,  element  )  ; 
} 







public   String   getProcessingInstructionTarget  (  Object   object  )  { 
return  (  (  OMProcessingInstruction  )  object  )  .  getTarget  (  )  ; 
} 







public   String   getProcessingInstructionData  (  Object   object  )  { 
return  (  (  OMProcessingInstruction  )  object  )  .  getValue  (  )  ; 
} 








public   short   getNodeType  (  Object   node  )  { 
return   super  .  getNodeType  (  node  )  ; 
} 












public   Object   getParentNode  (  Object   contextNode  )  throws   UnsupportedAxisException  { 
if  (  contextNode  ==  null  ||  contextNode   instanceof   OMDocument  )  { 
return   null  ; 
}  else   if  (  contextNode   instanceof   OMAttributeEx  )  { 
return  (  (  OMAttributeEx  )  contextNode  )  .  getParent  (  )  ; 
}  else   if  (  contextNode   instanceof   OMNamespaceEx  )  { 
return  (  (  OMNamespaceEx  )  contextNode  )  .  getParent  (  )  ; 
} 
return  (  (  OMNode  )  contextNode  )  .  getParent  (  )  ; 
} 

class   OMNamespaceEx   implements   OMNamespace  { 

OMNamespace   originalNsp  =  null  ; 

OMContainer   parent  =  null  ; 

OMNamespaceEx  (  OMNamespace   nsp  ,  OMContainer   parent  )  { 
originalNsp  =  nsp  ; 
this  .  parent  =  parent  ; 
} 

public   boolean   equals  (  String   uri  ,  String   prefix  )  { 
return   originalNsp  .  equals  (  uri  ,  prefix  )  ; 
} 

public   String   getPrefix  (  )  { 
return   originalNsp  .  getPrefix  (  )  ; 
} 

public   String   getName  (  )  { 
return   originalNsp  .  getNamespaceURI  (  )  ; 
} 

public   String   getNamespaceURI  (  )  { 
return   originalNsp  .  getNamespaceURI  (  )  ; 
} 

public   OMContainer   getParent  (  )  { 
return   parent  ; 
} 
} 

class   OMAttributeEx   implements   OMAttribute  { 

OMAttribute   attribute  =  null  ; 

OMContainer   parent  =  null  ; 

OMFactory   factory  ; 

OMAttributeEx  (  OMAttribute   attribute  ,  OMContainer   parent  ,  OMFactory   factory  )  { 
this  .  attribute  =  attribute  ; 
this  .  parent  =  parent  ; 
} 

public   String   getLocalName  (  )  { 
return   attribute  .  getLocalName  (  )  ; 
} 

public   void   setLocalName  (  String   localName  )  { 
attribute  .  setLocalName  (  localName  )  ; 
} 

public   String   getAttributeValue  (  )  { 
return   attribute  .  getAttributeValue  (  )  ; 
} 

public   String   getAttributeType  (  )  { 
return   attribute  .  getAttributeType  (  )  ; 
} 

public   void   setAttributeValue  (  String   value  )  { 
attribute  .  setAttributeValue  (  value  )  ; 
} 

public   void   setAttributeType  (  String   type  )  { 
attribute  .  setAttributeType  (  type  )  ; 
} 

public   void   setOMNamespace  (  OMNamespace   omNamespace  )  { 
attribute  .  setOMNamespace  (  omNamespace  )  ; 
} 

public   OMNamespace   getNamespace  (  )  { 
return   attribute  .  getNamespace  (  )  ; 
} 

public   QName   getQName  (  )  { 
return   attribute  .  getQName  (  )  ; 
} 

public   OMContainer   getParent  (  )  { 
return   parent  ; 
} 

public   OMFactory   getOMFactory  (  )  { 
return   this  .  factory  ; 
} 









public   OMElement   getOwner  (  )  { 
return  (  parent   instanceof   OMElement  )  ?  (  OMElement  )  parent  :  null  ; 
} 
} 
} 

