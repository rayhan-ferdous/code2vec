package   org  .  dom4j  .  io  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  CharArrayReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  Reader  ; 
import   java  .  net  .  URL  ; 
import   org  .  dom4j  .  Document  ; 
import   org  .  dom4j  .  DocumentException  ; 
import   org  .  dom4j  .  DocumentFactory  ; 
import   org  .  dom4j  .  Element  ; 
import   org  .  dom4j  .  ElementHandler  ; 
import   org  .  dom4j  .  QName  ; 
import   org  .  xmlpull  .  v1  .  XmlPullParser  ; 
import   org  .  xmlpull  .  v1  .  XmlPullParserException  ; 
import   org  .  xmlpull  .  v1  .  XmlPullParserFactory  ; 












public   class   XPP3Reader  { 


private   DocumentFactory   factory  ; 


private   XmlPullParser   xppParser  ; 


private   XmlPullParserFactory   xppFactory  ; 


private   DispatchHandler   dispatchHandler  ; 

public   XPP3Reader  (  )  { 
} 

public   XPP3Reader  (  DocumentFactory   factory  )  { 
this  .  factory  =  factory  ; 
} 


















public   Document   read  (  File   file  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
String   systemID  =  file  .  getAbsolutePath  (  )  ; 
return   read  (  new   BufferedReader  (  new   FileReader  (  file  )  )  ,  systemID  )  ; 
} 


















public   Document   read  (  URL   url  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
String   systemID  =  url  .  toExternalForm  (  )  ; 
return   read  (  createReader  (  url  .  openStream  (  )  )  ,  systemID  )  ; 
} 


























public   Document   read  (  String   systemID  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
if  (  systemID  .  indexOf  (  ':'  )  >=  0  )  { 
return   read  (  new   URL  (  systemID  )  )  ; 
}  else  { 
return   read  (  new   File  (  systemID  )  )  ; 
} 
} 


















public   Document   read  (  InputStream   in  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
return   read  (  createReader  (  in  )  )  ; 
} 


















public   Document   read  (  Reader   reader  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
getXPPParser  (  )  .  setInput  (  reader  )  ; 
return   parseDocument  (  )  ; 
} 


















public   Document   read  (  char  [  ]  text  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
getXPPParser  (  )  .  setInput  (  new   CharArrayReader  (  text  )  )  ; 
return   parseDocument  (  )  ; 
} 




















public   Document   read  (  InputStream   in  ,  String   systemID  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
return   read  (  createReader  (  in  )  ,  systemID  )  ; 
} 




















public   Document   read  (  Reader   reader  ,  String   systemID  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
Document   document  =  read  (  reader  )  ; 
document  .  setName  (  systemID  )  ; 
return   document  ; 
} 

public   XmlPullParser   getXPPParser  (  )  throws   XmlPullParserException  { 
if  (  xppParser  ==  null  )  { 
xppParser  =  getXPPFactory  (  )  .  newPullParser  (  )  ; 
} 
return   xppParser  ; 
} 

public   XmlPullParserFactory   getXPPFactory  (  )  throws   XmlPullParserException  { 
if  (  xppFactory  ==  null  )  { 
xppFactory  =  XmlPullParserFactory  .  newInstance  (  )  ; 
} 
xppFactory  .  setNamespaceAware  (  true  )  ; 
return   xppFactory  ; 
} 

public   void   setXPPFactory  (  XmlPullParserFactory   xPPfactory  )  { 
this  .  xppFactory  =  xPPfactory  ; 
} 







public   DocumentFactory   getDocumentFactory  (  )  { 
if  (  factory  ==  null  )  { 
factory  =  DocumentFactory  .  getInstance  (  )  ; 
} 
return   factory  ; 
} 












public   void   setDocumentFactory  (  DocumentFactory   documentFactory  )  { 
this  .  factory  =  documentFactory  ; 
} 











public   void   addHandler  (  String   path  ,  ElementHandler   handler  )  { 
getDispatchHandler  (  )  .  addHandler  (  path  ,  handler  )  ; 
} 








public   void   removeHandler  (  String   path  )  { 
getDispatchHandler  (  )  .  removeHandler  (  path  )  ; 
} 










public   void   setDefaultHandler  (  ElementHandler   handler  )  { 
getDispatchHandler  (  )  .  setDefaultHandler  (  handler  )  ; 
} 

protected   Document   parseDocument  (  )  throws   DocumentException  ,  IOException  ,  XmlPullParserException  { 
DocumentFactory   df  =  getDocumentFactory  (  )  ; 
Document   document  =  df  .  createDocument  (  )  ; 
Element   parent  =  null  ; 
XmlPullParser   pp  =  getXPPParser  (  )  ; 
pp  .  setFeature  (  XmlPullParser  .  FEATURE_PROCESS_NAMESPACES  ,  true  )  ; 
while  (  true  )  { 
int   type  =  pp  .  nextToken  (  )  ; 
switch  (  type  )  { 
case   XmlPullParser  .  PROCESSING_INSTRUCTION  : 
{ 
String   text  =  pp  .  getText  (  )  ; 
int   loc  =  text  .  indexOf  (  " "  )  ; 
if  (  loc  >=  0  )  { 
String   target  =  text  .  substring  (  0  ,  loc  )  ; 
String   txt  =  text  .  substring  (  loc  +  1  )  ; 
document  .  addProcessingInstruction  (  target  ,  txt  )  ; 
}  else  { 
document  .  addProcessingInstruction  (  text  ,  ""  )  ; 
} 
break  ; 
} 
case   XmlPullParser  .  COMMENT  : 
{ 
if  (  parent  !=  null  )  { 
parent  .  addComment  (  pp  .  getText  (  )  )  ; 
}  else  { 
document  .  addComment  (  pp  .  getText  (  )  )  ; 
} 
break  ; 
} 
case   XmlPullParser  .  CDSECT  : 
{ 
if  (  parent  !=  null  )  { 
parent  .  addCDATA  (  pp  .  getText  (  )  )  ; 
}  else  { 
String   msg  =  "Cannot have text content outside of the "  +  "root document"  ; 
throw   new   DocumentException  (  msg  )  ; 
} 
break  ; 
} 
case   XmlPullParser  .  ENTITY_REF  : 
break  ; 
case   XmlPullParser  .  END_DOCUMENT  : 
return   document  ; 
case   XmlPullParser  .  START_TAG  : 
{ 
QName   qname  =  (  pp  .  getPrefix  (  )  ==  null  )  ?  df  .  createQName  (  pp  .  getName  (  )  ,  pp  .  getNamespace  (  )  )  :  df  .  createQName  (  pp  .  getName  (  )  ,  pp  .  getPrefix  (  )  ,  pp  .  getNamespace  (  )  )  ; 
Element   newElement  =  df  .  createElement  (  qname  )  ; 
int   nsStart  =  pp  .  getNamespaceCount  (  pp  .  getDepth  (  )  -  1  )  ; 
int   nsEnd  =  pp  .  getNamespaceCount  (  pp  .  getDepth  (  )  )  ; 
for  (  int   i  =  nsStart  ;  i  <  nsEnd  ;  i  ++  )  { 
if  (  pp  .  getNamespacePrefix  (  i  )  !=  null  )  { 
newElement  .  addNamespace  (  pp  .  getNamespacePrefix  (  i  )  ,  pp  .  getNamespaceUri  (  i  )  )  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  pp  .  getAttributeCount  (  )  ;  i  ++  )  { 
QName   qa  =  (  pp  .  getAttributePrefix  (  i  )  ==  null  )  ?  df  .  createQName  (  pp  .  getAttributeName  (  i  )  )  :  df  .  createQName  (  pp  .  getAttributeName  (  i  )  ,  pp  .  getAttributePrefix  (  i  )  ,  pp  .  getAttributeNamespace  (  i  )  )  ; 
newElement  .  addAttribute  (  qa  ,  pp  .  getAttributeValue  (  i  )  )  ; 
} 
if  (  parent  !=  null  )  { 
parent  .  add  (  newElement  )  ; 
}  else  { 
document  .  add  (  newElement  )  ; 
} 
parent  =  newElement  ; 
break  ; 
} 
case   XmlPullParser  .  END_TAG  : 
{ 
if  (  parent  !=  null  )  { 
parent  =  parent  .  getParent  (  )  ; 
} 
break  ; 
} 
case   XmlPullParser  .  TEXT  : 
{ 
String   text  =  pp  .  getText  (  )  ; 
if  (  parent  !=  null  )  { 
parent  .  addText  (  text  )  ; 
}  else  { 
String   msg  =  "Cannot have text content outside of the "  +  "root document"  ; 
throw   new   DocumentException  (  msg  )  ; 
} 
break  ; 
} 
default  : 
break  ; 
} 
} 
} 

protected   DispatchHandler   getDispatchHandler  (  )  { 
if  (  dispatchHandler  ==  null  )  { 
dispatchHandler  =  new   DispatchHandler  (  )  ; 
} 
return   dispatchHandler  ; 
} 

protected   void   setDispatchHandler  (  DispatchHandler   dispatchHandler  )  { 
this  .  dispatchHandler  =  dispatchHandler  ; 
} 












protected   Reader   createReader  (  InputStream   in  )  throws   IOException  { 
return   new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
} 
} 

