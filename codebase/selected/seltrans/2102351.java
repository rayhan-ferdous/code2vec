package   net  .  noderunner  .  exml  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   net  .  noderunner  .  exml  .  ElementRule  .  ElementRuleState  ; 








































public   class   XmlReader  { 




private   XmlScanner   scanner  ; 




private   boolean   inEntityScan  ; 




private   static   final   int   cbufSize  =  64  ; 

char   cbuf  [  ]  =  new   char  [  cbufSize  ]  ; 





private   Dtd   dtd  ; 




private   RuleStack   eruleStack  ; 




private   ElementRule  .  AttributeRuleState   aruleState  ; 





private   XmlCharArrayWriter   attValue  =  new   XmlCharArrayWriter  (  64  )  ; 




private   NamespaceImpl   namespaceElement  =  new   NamespaceImpl  (  )  ; 




private   NamespaceImpl   namespaceAttr  =  new   NamespaceImpl  (  )  ; 




private   SystemLiteralResolver   resolver  ; 






public   XmlReader  (  Reader   reader  ,  Dtd   dtd  ,  SystemLiteralResolver   resolver  )  { 
this  .  dtd  =  dtd  ; 
this  .  resolver  =  resolver  ; 
this  .  eruleStack  =  new   RuleStack  (  )  ; 
this  .  aruleState  =  new   ElementRule  .  AttributeRuleState  (  )  ; 
scanner  =  createXmlScanner  (  reader  )  ; 
setupStringPool  (  )  ; 
} 

private   void   setupStringPool  (  )  { 
scanner  .  getStringPool  (  )  .  add  (  XmlTags  .  XMLNS  )  ; 
scanner  .  getStringPool  (  )  .  addAll  (  dtd  .  getKnownElements  (  )  .  keySet  (  )  )  ; 
Iterator   i  =  dtd  .  getKnownElements  (  )  .  values  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
ElementRule   er  =  (  ElementRule  )  i  .  next  (  )  ; 
List   l  =  er  .  getAttributeRules  (  )  ; 
if  (  l  !=  null  )  { 
for  (  int   j  =  0  ;  j  <  l  .  size  (  )  ;  j  ++  )  { 
String   name  =  (  (  AttributeRule  )  l  .  get  (  j  )  )  .  getName  (  )  ; 
scanner  .  getStringPool  (  )  .  add  (  name  )  ; 
} 
} 
} 
} 

private   static   XmlScanner   createXmlScanner  (  Reader   reader  )  { 
return   new   XmlScanner  (  reader  ,  1024  *  4  ,  cbufSize  )  ; 
} 





public   XmlReader  (  Reader   reader  ,  Dtd   dtd  )  { 
this  (  reader  ,  dtd  ,  NullResolver  .  getInstance  (  )  )  ; 
} 




public   XmlReader  (  Reader   reader  )  { 
this  (  reader  ,  new   Dtd  (  )  )  ; 
} 





public   XmlReader  (  String   s  )  { 
this  (  )  ; 
setReadString  (  s  )  ; 
} 






public   XmlReader  (  )  { 
this  (  NullReader  .  getInstance  (  )  )  ; 
} 




public   void   setReader  (  Reader   reader  )  { 
scanner  .  setReader  (  reader  )  ; 
} 




public   void   setReadString  (  String   xml  )  { 
scanner  =  new   XmlScanner  (  xml  )  ; 
} 




public   Dtd   getDtd  (  )  { 
return   dtd  ; 
} 




public   XmlScanner   getScanner  (  )  { 
return   scanner  ; 
} 







public   StringPool   getStringPool  (  )  { 
return   scanner  .  getStringPool  (  )  ; 
} 




public   void   setResolver  (  SystemLiteralResolver   resolver  )  { 
this  .  resolver  =  resolver  ; 
} 




public   SystemLiteralResolver   getResolver  (  )  { 
return   resolver  ; 
} 







public   boolean   hasMoreData  (  )  throws   IOException  { 
int   c  =  scanner  .  peek  (  )  ; 
if  (  c  ==  -  1  )  return   false  ; 
return   true  ; 
} 






public   void   close  (  )  throws   IOException  { 
scanner  .  close  (  )  ; 
} 










public   Document   document  (  )  throws   IOException  ,  XmlException  { 
Document   d  =  new   Document  (  )  ; 
Prolog  (  d  )  ; 
Element   e  =  element  (  )  ; 
if  (  e  ==  null  )  { 
throw   new   XmlException  (  "Could not find root object"  )  ; 
} 
String   docname  =  dtd  .  getName  (  )  ; 
if  (  docname  !=  null  &&  !  docname  .  equals  (  e  .  getName  (  )  )  )  { 
throw   new   XmlException  (  "Root element name does not match doctype "  +  docname  )  ; 
} 
d  .  appendChild  (  e  )  ; 
return   d  ; 
} 







public   static   boolean   Char  (  int   i  )  { 
if  (  i  >=  0x20  &&  i  <=  0xD7FF  )  return   true  ; 
if  (  i  ==  0x09  ||  i  ==  0x0a  ||  i  ==  0x0d  )  return   true  ; 
if  (  i  >=  0xE000  &&  i  <=  0xFFFD  )  return   true  ; 
if  (  i  >=  0x10000  &&  i  <=  0x10FFFF  )  return   true  ; 
return   false  ; 
} 




boolean   isS  (  int   c  )  { 
return  (  c  ==  0x20  ||  c  ==  0x0a  ||  c  ==  0x0d  ||  c  ==  0x09  )  ; 
} 








public   boolean   S  (  )  throws   IOException  { 
boolean   found  =  false  ; 
while  (  true  )  { 
if  (  !  isS  (  scanner  .  peek  (  )  )  )  return   found  ; 
scanner  .  read  (  )  ; 
found  =  true  ; 
} 
} 








public   static   boolean   NameChar  (  final   char   c  )  { 
if  (  (  c  >=  0x61  &&  c  <=  0x7a  )  ||  (  c  >=  0x41  &&  c  <=  0x5a  )  ||  (  c  >=  0x30  &&  c  <=  0x3a  )  ||  c  ==  '.'  ||  c  ==  '-'  ||  c  ==  '_'  )  return   true  ; 
if  (  c  <  128  )  return   false  ; 
return   Character  .  isLetterOrDigit  (  c  )  ; 
} 




static   boolean   FirstNameChar  (  final   char   c  )  { 
if  (  (  c  >=  0x61  &&  c  <=  0x7a  )  ||  (  c  >=  0x41  &&  c  <=  0x5a  )  ||  c  ==  ':'  ||  c  ==  '_'  )  return   true  ; 
if  (  c  <  128  )  return   false  ; 
return   Character  .  isLetter  (  c  )  ; 
} 









public   String   Name  (  )  throws   XmlException  ,  IOException  { 
String   name  =  scanner  .  getName  (  )  ; 
return   name  ; 
} 










public   List  <  String  >  Names  (  )  throws   XmlException  ,  IOException  { 
List  <  String  >  l  =  new   ArrayList  <  String  >  (  )  ; 
while  (  true  )  { 
String   s  =  Name  (  )  ; 
if  (  s  ==  null  )  break  ; 
l  .  add  (  s  )  ; 
S  (  )  ; 
} 
return   l  ; 
} 






public   String   Nmtoken  (  )  throws   XmlException  ,  IOException  { 
int   c  =  scanner  .  peek  (  )  ; 
if  (  !  NameChar  (  (  char  )  c  )  )  { 
return   null  ; 
} 
int   len  =  0  ; 
attValue  .  reset  (  )  ; 
while  (  true  )  { 
c  =  scanner  .  read  (  )  ; 
if  (  !  NameChar  (  (  char  )  c  )  )  { 
if  (  c  >  0  )  scanner  .  unread  (  c  )  ; 
return   attValue  .  toString  (  )  ; 
} 
attValue  .  write  (  (  char  )  c  )  ; 
if  (  len  ++  >  XmlReaderPrefs  .  MAX_NAME_LEN  )  { 
throw   new   XmlException  (  "Exceeded MAX_NAME_LEN, read to "  +  attValue  )  ; 
} 
} 
} 









public   List  <  String  >  Nmtokens  (  )  throws   XmlException  ,  IOException  { 
List  <  String  >  l  =  new   ArrayList  <  String  >  (  )  ; 
while  (  true  )  { 
String   s  =  Nmtoken  (  )  ; 
if  (  s  ==  null  )  break  ; 
l  .  add  (  s  )  ; 
S  (  )  ; 
} 
return   l  ; 
} 








public   String   EntityValue  (  )  throws   IOException  ,  XmlException  { 
int   q  =  scanner  .  peek  (  )  ; 
if  (  q  !=  '"'  &&  q  !=  '\''  &&  q  !=  '%'  )  throw   new   XmlException  (  "Expected EntityValue quote or %, got "  +  (  char  )  q  )  ; 
if  (  q  ==  '%'  )  { 
Entity   e  =  PEReference  (  )  ; 
return   e  .  resolveAll  (  this  )  ; 
} 
return   AttValue  (  true  )  ; 
} 





private   String   AttValue  (  boolean   resolvePE  )  throws   IOException  ,  XmlException  { 
int   q  =  scanner  .  read  (  )  ; 
if  (  q  !=  '"'  &&  q  !=  '\''  )  throw   new   XmlException  (  "Expected AttValue quote, got "  +  (  char  )  q  )  ; 
attValue  .  reset  (  )  ; 
int   c  ; 
while  (  true  )  { 
c  =  scanner  .  peek  (  )  ; 
if  (  c  ==  -  1  )  throw   new   XmlException  (  "EOF in AttValue"  )  ; 
if  (  c  ==  '>'  &&  !  resolvePE  )  throw   new   XmlException  (  "Must not have > in AttValue"  )  ; 
if  (  c  ==  '&'  )  { 
if  (  CharRef  (  )  )  { 
attValue  .  write  (  scanner  .  read  (  )  )  ; 
continue  ; 
}  else   if  (  !  resolvePE  )  { 
Entity   ent  =  Reference  (  )  ; 
if  (  ent  !=  null  )  { 
if  (  ent  .  isExternal  (  )  )  throw   new   XmlException  (  "No external entity in att value"  )  ; 
attValue  .  write  (  ent  .  resolveAll  (  this  )  )  ; 
}  else  { 
attValue  .  write  (  (  char  )  scanner  .  read  (  )  )  ; 
} 
continue  ; 
}  else  { 
checkEntityReference  (  attValue  )  ; 
} 
} 
if  (  c  ==  '%'  &&  resolvePE  )  { 
Entity   e  =  PEReference  (  )  ; 
attValue  .  write  (  e  .  resolveAll  (  this  )  )  ; 
continue  ; 
} 
c  =  scanner  .  read  (  )  ; 
if  (  q  ==  c  )  return   attValue  .  toString  (  )  ; 
attValue  .  write  (  c  )  ; 
if  (  attValue  .  size  (  )  >  XmlReaderPrefs  .  MAX_ATTRIBUTE_LEN  )  { 
throw   new   XmlException  (  "Exceeded MAX_ATTRIBUTE_LEN, read to "  +  attValue  )  ; 
} 
} 
} 








public   String   AttValue  (  )  throws   IOException  ,  XmlException  { 
return   AttValue  (  false  )  ; 
} 







public   String   SystemLiteral  (  )  throws   IOException  ,  XmlException  { 
int   q  =  scanner  .  read  (  )  ; 
if  (  q  !=  '"'  &&  q  !=  '\''  )  throw   new   XmlException  (  "Expected SystemLiteral quote, got "  +  q  )  ; 
attValue  .  reset  (  )  ; 
int   c  ; 
int   len  =  0  ; 
while  (  true  )  { 
c  =  scanner  .  read  (  )  ; 
if  (  q  ==  c  )  return   attValue  .  toString  (  )  ; 
if  (  len  ++  >  XmlReaderPrefs  .  MAX_SYSTEM_LITERAL_LEN  )  throw   new   XmlException  (  "Exceeded MAX_SYSTEM_LITERAL_LEN, read to "  +  attValue  )  ; 
if  (  c  ==  -  1  )  { 
throw   new   XmlException  (  "Expecting closing quote, read to "  +  attValue  )  ; 
} 
attValue  .  write  (  (  char  )  c  )  ; 
} 
} 







public   String   PubidLiteral  (  )  throws   IOException  ,  XmlException  { 
String   s  =  SystemLiteral  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  !  PubidChar  (  s  .  charAt  (  i  )  )  )  throw   new   XmlException  (  "Bad PubidChar in "  +  s  )  ; 
} 
return   s  ; 
} 






public   static   boolean   PubidChar  (  char   c  )  { 
if  (  c  ==  '\"'  ||  c  ==  '&'  ||  c  ==  '\''  ||  c  ==  '<'  ||  c  ==  '>'  )  return   false  ; 
if  (  c  >=  ' '  &&  c  <=  'Z'  )  return   true  ; 
if  (  c  >=  'a'  &&  c  <=  'z'  )  return   true  ; 
if  (  c  ==  '_'  )  return   true  ; 
return   false  ; 
} 







public   void   CharData  (  Writer   w  )  throws   IOException  ,  XmlException  { 
scanner  .  copyUntil  (  w  ,  '<'  ,  '&'  )  ; 
} 









public   Comment   comment  (  boolean   skip  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  COMMENT  ,  4  )  )  return   null  ; 
Comment   c  =  null  ; 
if  (  skip  )  { 
copyUntil  (  NullWriter  .  getInstance  (  )  ,  XmlTags  .  COMMENT_DASH  )  ; 
}  else  { 
c  =  new   Comment  (  )  ; 
copyUntil  (  c  .  getWriter  (  )  ,  XmlTags  .  COMMENT_DASH  )  ; 
} 
if  (  scanner  .  read  (  )  !=  '>'  )  throw   new   XmlException  (  "Cannot have -- in comment"  )  ; 
return   c  ; 
} 











public   PI   pi  (  boolean   skip  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  PI  ,  2  )  )  return   null  ; 
String   target  =  PITarget  (  )  ; 
S  (  )  ; 
if  (  skip  )  { 
copyUntil  (  NullWriter  .  getInstance  (  )  ,  XmlTags  .  PI_END  )  ; 
}  else  { 
PI   pi  =  new   PI  (  target  )  ; 
copyUntil  (  pi  .  getWriter  (  )  ,  XmlTags  .  PI_END  )  ; 
return   pi  ; 
} 
return   null  ; 
} 







public   String   PITarget  (  )  throws   IOException  ,  XmlException  { 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected a Name following <? in Processing Instruction"  )  ; 
if  (  name  .  toLowerCase  (  )  .  equals  (  "xml"  )  )  throw   new   XmlException  (  "PITarget cannot be xml"  )  ; 
return   name  ; 
} 











public   boolean   CDSect  (  Writer   w  )  throws   IOException  ,  XmlException  { 
boolean   match  =  matches  (  XmlTags  .  CDATA_BEGIN  )  ; 
if  (  !  match  )  return   false  ; 
copyUntil  (  w  ,  XmlTags  .  CDATA_END  )  ; 
return   true  ; 
} 





public   void   Prolog  (  Document   d  )  throws   IOException  ,  XmlException  { 
XmlDecl  (  )  ; 
while  (  Misc  (  d  )  )  ; 
boolean   found  =  doctypedecl  (  )  ; 
if  (  found  )  while  (  Misc  (  d  )  )  ; 
} 





boolean   XmlDecl  (  boolean   allowSA  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  XML_DECL  )  )  return   false  ; 
VersionInfo  (  )  ; 
EncodingDecl  (  )  ; 
if  (  allowSA  )  SDDecl  (  )  ; 
S  (  )  ; 
if  (  !  matches  (  XmlTags  .  PI_END  )  )  throw   new   XmlException  (  "Expected closing ?> for XmlDecl"  )  ; 
return   true  ; 
} 







public   boolean   XmlDecl  (  )  throws   IOException  ,  XmlException  { 
return   XmlDecl  (  true  )  ; 
} 





public   void   VersionInfo  (  )  throws   IOException  ,  XmlException  { 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after XML"  )  ; 
if  (  !  matches  (  XmlTags  .  VERSION_BEGIN  )  )  { 
throw   new   XmlException  (  "Expected 'version'"  )  ; 
} 
Eq  (  )  ; 
String   version  =  SystemLiteral  (  )  ; 
if  (  !  VersionNum  (  version  )  )  throw   new   XmlException  (  "Version contains invalid characters: "  +  version  )  ; 
} 





public   void   Eq  (  )  throws   IOException  ,  XmlException  { 
S  (  )  ; 
if  (  scanner  .  read  (  )  !=  '='  )  throw   new   XmlException  (  "Expected ="  )  ; 
S  (  )  ; 
} 







public   boolean   VersionNum  (  String   ver  )  throws   IOException  ,  XmlException  { 
int   len  =  ver  .  length  (  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
char   c  =  ver  .  charAt  (  i  )  ; 
if  (  (  c  >=  'a'  &&  c  <=  'z'  )  ||  (  c  >=  'A'  &&  c  <=  'Z'  )  ||  (  c  >=  '0'  &&  c  <=  '9'  )  ||  (  c  ==  '.'  )  )  { 
continue  ; 
} 
return   false  ; 
} 
return   true  ; 
} 












public   boolean   Misc  (  Document   d  )  throws   IOException  ,  XmlException  { 
if  (  S  (  )  )  return   true  ; 
Comment   c  =  comment  (  false  )  ; 
if  (  c  !=  null  )  { 
if  (  d  !=  null  )  d  .  appendChild  (  c  )  ; 
return   true  ; 
} 
PI   pi  =  pi  (  false  )  ; 
if  (  pi  !=  null  )  { 
if  (  d  !=  null  )  d  .  appendChild  (  pi  )  ; 
return   true  ; 
} 
return   false  ; 
} 






public   boolean   doctypedecl  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  DOCTYPE_BEGIN  )  )  return   false  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after doctypedecl."  )  ; 
String   docname  =  Name  (  )  ; 
dtd  .  setName  (  docname  )  ; 
boolean   space  =  S  (  )  ; 
Entity   dtd  =  null  ; 
if  (  space  )  { 
dtd  =  ExternalID  (  )  ; 
if  (  dtd  !=  null  )  S  (  )  ; 
} 
int   c  =  scanner  .  peek  (  )  ; 
if  (  c  ==  '['  )  { 
scanner  .  read  (  )  ; 
while  (  true  )  { 
S  (  )  ; 
if  (  markupdecl  (  )  )  { 
continue  ; 
} 
if  (  scanner  .  peek  (  )  ==  '%'  )  { 
peScan  (  PEReference  (  )  )  ; 
continue  ; 
} 
c  =  scanner  .  read  (  )  ; 
if  (  c  !=  ']'  )  throw   new   XmlException  (  "expected ] to end doctypedecl"  )  ; 
break  ; 
} 
S  (  )  ; 
} 
c  =  scanner  .  read  (  )  ; 
if  (  c  !=  '>'  )  throw   new   XmlException  (  "Expected > at end of doctypedecl, not "  +  (  char  )  c  )  ; 
if  (  dtd  !=  null  )  { 
resolveExtSubset  (  dtd  )  ; 
} 
return   true  ; 
} 







public   boolean   markupdecl  (  )  throws   IOException  ,  XmlException  { 
switch  (  scanner  .  peekEvent  (  )  )  { 
case   XmlEvent  .  ELEMENT_DECL  : 
if  (  !  elementdecl  (  )  )  throw   new   XmlException  (  "Expected elementdecl"  )  ; 
break  ; 
case   XmlEvent  .  ATTLIST_DECL  : 
if  (  !  AttlistDecl  (  )  )  throw   new   XmlException  (  "Expected AttlistDecl"  )  ; 
break  ; 
case   XmlEvent  .  ENTITY_DECL  : 
if  (  !  EntityDecl  (  )  )  throw   new   XmlException  (  "Expected EntityDecl"  )  ; 
break  ; 
case   XmlEvent  .  NOTATATION_DECL  : 
if  (  !  NotationDecl  (  )  )  throw   new   XmlException  (  "Expected NotationDecl"  )  ; 
break  ; 
case   XmlEvent  .  PI  : 
if  (  pi  (  false  )  ==  null  )  throw   new   XmlException  (  "Expected PI"  )  ; 
break  ; 
case   XmlEvent  .  COMMENT  : 
if  (  comment  (  false  )  ==  null  )  throw   new   XmlException  (  "Expected Comment"  )  ; 
break  ; 
default  : 
return   false  ; 
} 
return   true  ; 
} 






public   void   extSubset  (  )  throws   XmlException  ,  IOException  { 
TextDecl  (  )  ; 
extSubsetDecl  (  )  ; 
} 




private   void   peScan  (  Entity   entity  )  throws   IOException  ,  XmlException  { 
XmlScanner   newScanner  ; 
XmlScanner   oldScanner  =  scanner  ; 
if  (  entity  .  getValue  (  )  ==  null  )  { 
Reader   r  =  resolver  .  resolve  (  entity  .  getSystemID  (  )  )  ; 
newScanner  =  createXmlScanner  (  r  )  ; 
}  else  { 
newScanner  =  new   XmlScanner  (  entity  .  getValue  (  )  )  ; 
} 
scanner  =  newScanner  ; 
extSubsetDecl  (  )  ; 
if  (  hasMoreData  (  )  )  throw   new   XmlException  (  "Incomplete parameter entity content "  +  entity  )  ; 
scanner  =  oldScanner  ; 
} 







public   void   extSubsetDecl  (  )  throws   IOException  ,  XmlException  { 
while  (  true  )  { 
S  (  )  ; 
switch  (  scanner  .  peekEvent  (  )  )  { 
case   XmlEvent  .  ELEMENT_DECL  : 
case   XmlEvent  .  ATTLIST_DECL  : 
case   XmlEvent  .  ENTITY_DECL  : 
case   XmlEvent  .  NOTATATION_DECL  : 
case   XmlEvent  .  PI  : 
case   XmlEvent  .  COMMENT  : 
markupdecl  (  )  ; 
break  ; 
case   XmlEvent  .  CONDITIONAL_SECT  : 
conditionalSect  (  )  ; 
break  ; 
default  : 
if  (  scanner  .  peek  (  )  ==  '%'  )  { 
peScan  (  PEReference  (  )  )  ; 
}  else  { 
return  ; 
} 
} 
} 
} 







public   String   SDDecl  (  )  throws   XmlException  ,  IOException  { 
boolean   space  =  S  (  )  ; 
if  (  matches  (  XmlTags  .  STANDALONE_BEGIN  )  )  { 
if  (  !  space  )  throw   new   XmlException  (  "Expected space before standalone declaration"  )  ; 
Eq  (  )  ; 
String   yesNo  =  AttValue  (  )  ; 
if  (  !  yesNo  .  equals  (  "yes"  )  &&  !  yesNo  .  equals  (  "no"  )  )  throw   new   XmlException  (  "Expected yes or no for standalone value"  )  ; 
return   yesNo  ; 
} 
return   null  ; 
} 







public   Element   element  (  )  throws   IOException  ,  XmlException  { 
Element   e  =  STag  (  )  ; 
if  (  e  ==  null  )  { 
return   null  ; 
} 
if  (  e  .  isOpen  (  )  )  { 
content  (  e  )  ; 
if  (  !  ETag  (  e  )  )  { 
throw   new   XmlException  (  "Expected tag </"  +  e  .  getName  (  )  +  ">"  )  ; 
} 
} 
return   e  ; 
} 





private   Element   makeElement  (  NamespaceImpl   namespace  ,  List  <  Attribute  >  attr  ,  boolean   open  )  throws   XmlException  { 
UriMap   m  =  scanner  .  getUriMap  (  )  ; 
boolean   useNS  =  namespace  .  hasNamespace  (  )  ; 
if  (  attr  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  attr  .  size  (  )  ;  i  ++  )  { 
Attribute   a  =  attr  .  get  (  i  )  ; 
if  (  a  .  getPrefix  (  )  ==  XmlTags  .  XMLNS  )  { 
String   ln  =  a  .  getLocalName  (  )  ; 
String   v  =  a  .  getValue  (  )  ; 
m  .  put  (  ln  ,  v  )  ; 
} 
if  (  a  .  getName  (  )  ==  XmlTags  .  XMLNS  )  { 
String   v  =  a  .  getValue  (  )  ; 
if  (  v  .  length  (  )  !=  0  )  v  =  null  ; 
namespace  .  setNamespaceURI  (  v  )  ; 
useNS  =  true  ; 
} 
} 
} 
if  (  useNS  )  return   new   ElementNS  (  namespace  ,  attr  ,  open  )  ;  else   return   new   Element  (  namespace  .  getName  (  )  ,  attr  ,  open  )  ; 
} 








public   Element   STag  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  STAG  ,  1  )  )  return   null  ; 
scanner  .  readNamespace  (  namespaceElement  )  ; 
ElementRule   rule  =  dtd  .  getElementRule  (  namespaceElement  .  getName  (  )  )  ; 
if  (  rule  !=  null  )  aruleState  .  clear  (  )  ; 
List  <  Attribute  >  attr  =  null  ; 
int   c  ; 
while  (  true  )  { 
boolean   space  =  S  (  )  ; 
c  =  scanner  .  read  (  )  ; 
if  (  c  ==  '/'  )  { 
c  =  scanner  .  read  (  )  ; 
if  (  c  !=  '>'  )  throw   new   XmlException  (  "Expected > for empty element"  )  ; 
if  (  rule  !=  null  )  attr  =  rule  .  encounterEnd  (  aruleState  ,  attr  )  ; 
return   makeElement  (  namespaceElement  ,  attr  ,  false  )  ; 
} 
if  (  c  ==  '>'  )  { 
if  (  rule  !=  null  )  attr  =  rule  .  encounterEnd  (  aruleState  ,  attr  )  ; 
return   makeElement  (  namespaceElement  ,  attr  ,  true  )  ; 
} 
scanner  .  unread  (  c  )  ; 
if  (  !  space  )  throw   new   XmlException  (  "Expected whitespace after name or attribute"  )  ; 
Attribute   a  =  Attribute  (  )  ; 
if  (  rule  !=  null  )  rule  .  encounterAttribute  (  a  ,  aruleState  )  ; 
if  (  attr  ==  null  )  attr  =  new   ArrayList  <  Attribute  >  (  )  ; 
attr  .  add  (  a  )  ; 
} 
} 










public   Attribute   Attribute  (  )  throws   IOException  ,  XmlException  { 
scanner  .  readNamespace  (  namespaceAttr  )  ; 
if  (  namespaceAttr  .  isClear  (  )  )  throw   new   XmlException  (  "Expected Attribute name"  )  ; 
Eq  (  )  ; 
String   value  =  AttValue  (  )  ; 
if  (  namespaceAttr  .  hasNamespace  (  )  )  return   new   AttributeNS  (  namespaceAttr  ,  value  )  ;  else   return   new   Attribute  (  namespaceAttr  .  getName  (  )  ,  value  )  ; 
} 






public   Element   ETag  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  ETAG  ,  2  )  )  return   null  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name for ETag"  )  ; 
S  (  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  !=  '>'  )  throw   new   XmlException  (  "Expected > for ETag "  +  name  )  ; 
return   new   Element  (  name  )  ; 
} 








public   boolean   ETag  (  Element   match  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  ETAG  ,  2  )  )  return   false  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name for ETag"  )  ; 
if  (  name  !=  match  .  getName  (  )  &&  !  name  .  equals  (  match  .  getName  (  )  )  )  throw   new   XmlException  (  "Expected </"  +  match  .  getName  (  )  +  "> got </"  +  name  +  ">"  )  ; 
S  (  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  !=  '>'  )  throw   new   XmlException  (  "Expected > for ETag "  +  name  )  ; 
return   true  ; 
} 




private   void   entityScan  (  Element   e  ,  Entity   entity  )  throws   IOException  ,  XmlException  { 
XmlScanner   newScanner  ; 
XmlScanner   oldScanner  =  scanner  ; 
boolean   oldInEntityScan  =  inEntityScan  ; 
if  (  entity  .  getValue  (  )  ==  null  )  { 
Reader   r  =  resolver  .  resolve  (  entity  .  getSystemID  (  )  )  ; 
newScanner  =  createXmlScanner  (  r  )  ; 
}  else  { 
newScanner  =  new   XmlScanner  (  entity  .  getValue  (  )  )  ; 
} 
newScanner  .  setStringPool  (  oldScanner  .  getStringPool  (  )  )  ; 
if  (  entity  .  isResolving  (  )  )  throw   new   XmlException  (  "Circular entity evaluation for "  +  entity  )  ; 
entity  .  setResolving  (  true  )  ; 
inEntityScan  =  true  ; 
scanner  =  newScanner  ; 
content  (  e  )  ; 
if  (  hasMoreData  (  )  )  throw   new   XmlException  (  "Unmatched end tag in entity content "  +  entity  )  ; 
if  (  !  oldInEntityScan  )  inEntityScan  =  false  ; 
scanner  =  oldScanner  ; 
entity  .  setResolving  (  false  )  ; 
} 







public   void   content  (  Element   e  )  throws   IOException  ,  XmlException  { 
ElementRule   rule  =  dtd  .  getElementRule  (  e  .  getName  (  )  )  ; 
ElementRuleState   eruleState  =  null  ; 
boolean   pcdata  =  true  ; 
if  (  rule  !=  null  )  { 
pcdata  =  rule  .  isPCDataAllowed  (  )  ; 
if  (  inEntityScan  )  eruleState  =  eruleStack  .  state  (  )  ;  else   eruleState  =  eruleStack  .  startElement  (  )  ; 
if  (  !  inEntityScan  )  eruleState  .  clear  (  )  ; 
} 
Writer   w  =  NullWriter  .  getInstance  (  )  ; 
CharacterData   cd  =  null  ; 
whileLoop  :  while  (  true  )  { 
if  (  !  pcdata  )  S  (  )  ; 
switch  (  scanner  .  peekEvent  (  )  )  { 
case   XmlEvent  .  ETAG  : 
if  (  cd  !=  null  )  e  .  appendChild  (  cd  )  ; 
break   whileLoop  ; 
case   XmlEvent  .  STAG  : 
if  (  cd  !=  null  )  e  .  appendChild  (  cd  )  ; 
Element   child  =  element  (  )  ; 
if  (  rule  !=  null  )  rule  .  encounterElement  (  child  ,  eruleState  )  ; 
e  .  appendChild  (  child  )  ; 
break  ; 
case   XmlEvent  .  COMMENT  : 
if  (  cd  !=  null  )  e  .  appendChild  (  cd  )  ; 
Comment   c  =  comment  (  false  )  ; 
if  (  c  !=  null  )  e  .  appendChild  (  c  )  ; 
break  ; 
case   XmlEvent  .  PI  : 
if  (  cd  !=  null  )  e  .  appendChild  (  cd  )  ; 
PI   pi  =  pi  (  false  )  ; 
if  (  pi  !=  null  )  e  .  appendChild  (  pi  )  ; 
break  ; 
case   XmlEvent  .  CHARDATA  : 
if  (  !  pcdata  )  throw   new   ElementRuleException  (  "Not allowed to have PCDATA section: "  +  e  ,  rule  )  ; 
if  (  cd  ==  null  )  { 
cd  =  new   CharacterData  (  )  ; 
w  =  cd  .  getWriter  (  )  ; 
} 
CharData  (  w  )  ; 
break  ; 
case   XmlEvent  .  CDSECT  : 
if  (  !  pcdata  )  throw   new   ElementRuleException  (  "Not allowed to have PCDATA section: "  +  e  ,  rule  )  ; 
if  (  cd  ==  null  )  { 
cd  =  new   CharacterData  (  )  ; 
w  =  cd  .  getWriter  (  )  ; 
} 
if  (  !  CDSect  (  w  )  )  { 
throw   new   XmlException  (  "Bad CDSect tag found"  )  ; 
} 
break  ; 
case   XmlEvent  .  REFERENCE  : 
Entity   entity  =  Reference  (  )  ; 
if  (  entity  !=  null  )  { 
entityScan  (  e  ,  entity  )  ; 
}  else  { 
if  (  !  pcdata  )  throw   new   ElementRuleException  (  "Not allowed to have PCDATA section: "  +  e  ,  rule  )  ; 
if  (  cd  ==  null  )  { 
cd  =  new   CharacterData  (  )  ; 
w  =  cd  .  getWriter  (  )  ; 
} 
w  .  write  (  (  char  )  scanner  .  read  (  )  )  ; 
} 
break  ; 
case   XmlEvent  .  NONE  : 
throw   new   XmlException  (  "Illegal content for element"  )  ; 
case   XmlEvent  .  EOD  : 
if  (  inEntityScan  )  { 
if  (  cd  !=  null  )  e  .  appendChild  (  cd  )  ; 
break   whileLoop  ; 
} 
throw   new   XmlException  (  "EOF in scanning"  )  ; 
default  : 
throw   new   XmlException  (  "Unknown content for element "  +  e  )  ; 
} 
} 
if  (  rule  !=  null  )  { 
if  (  !  inEntityScan  )  { 
rule  .  encounterEnd  (  eruleState  )  ; 
eruleStack  .  endElement  (  )  ; 
} 
} 
} 







public   boolean   elementdecl  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  ELEMENT_DECL_BEGIN  )  )  return   false  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after elementdecl"  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after elementdecl: "  +  this  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after elementdecl name"  )  ; 
ElementReq   req  =  contentspec  (  )  ; 
S  (  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  !=  '>'  )  throw   new   XmlException  (  "Expected > at end of elementdecl"  )  ; 
ElementRule   rule  =  dtd  .  getElementRule  (  name  )  ; 
if  (  rule  ==  null  )  { 
dtd  .  addElementRule  (  name  ,  new   ElementRule  (  req  ,  null  )  )  ; 
}  else  { 
rule  .  setRootReq  (  req  )  ; 
} 
return   true  ; 
} 








public   ElementReq   contentspec  (  )  throws   IOException  ,  XmlException  { 
ElementReq   req  ; 
if  (  matches  (  XmlTags  .  CONTENTSPEC_EMPTY  )  )  { 
req  =  new   ElementReq  (  )  ; 
}  else   if  (  matches  (  XmlTags  .  CONTENTSPEC_ANY  )  )  { 
req  =  new   ElementReq  (  )  ; 
req  .  setANY  (  )  ; 
}  else  { 
req  =  Mixed  (  )  ; 
if  (  req  ==  null  )  throw   new   XmlException  (  "Expected Mixed or children in contentspec"  )  ; 
} 
return   req  ; 
} 






public   ElementReq   children  (  )  throws   IOException  ,  XmlException  { 
ElementReq   req  =  choiceSeq  (  true  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  ==  '>'  ||  isS  (  c  )  )  { 
scanner  .  unread  (  c  )  ; 
}  else   if  (  c  !=  '?'  &&  c  !=  '*'  &&  c  !=  '+'  )  { 
throw   new   XmlException  (  "Expected *, ?, or +"  )  ; 
} 
req  .  setRepeating  (  c  )  ; 
return   req  ; 
} 






public   ElementReq   cp  (  )  throws   IOException  ,  XmlException  { 
ElementReq   req  ; 
String   name  =  Name  (  )  ; 
if  (  name  !=  null  )  { 
req  =  new   ElementReq  (  name  )  ; 
}  else  { 
req  =  choiceSeq  (  )  ; 
} 
int   c  =  scanner  .  peek  (  )  ; 
if  (  c  ==  '?'  ||  c  ==  '*'  ||  c  ==  '+'  )  scanner  .  read  (  )  ; 
req  .  setRepeating  (  c  )  ; 
return   req  ; 
} 







public   ElementReq   choiceSeq  (  )  throws   IOException  ,  XmlException  { 
return   choiceSeq  (  false  )  ; 
} 

private   ElementReq   choiceSeq  (  boolean   initial  )  throws   IOException  ,  XmlException  { 
boolean   isSeq  =  false  ; 
boolean   isChoice  =  false  ; 
ElementReq   req  =  new   ElementReq  (  )  ; 
if  (  !  initial  )  { 
if  (  !  matches  (  XmlTags  .  PAREN_BEGIN  )  )  throw   new   XmlException  (  "Expected ( in choiceSeq"  +  this  )  ; 
} 
while  (  true  )  { 
S  (  )  ; 
req  .  add  (  cp  (  )  )  ; 
S  (  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  ==  ','  )  { 
if  (  isChoice  )  throw   new   XmlException  (  "Expect | not , in choice"  )  ; 
isSeq  =  true  ; 
continue  ; 
} 
if  (  c  ==  '|'  )  { 
if  (  isSeq  )  throw   new   XmlException  (  "Expect , not | in sequence"  )  ; 
isChoice  =  true  ; 
continue  ; 
} 
if  (  c  ==  ')'  )  { 
if  (  isSeq  )  req  .  setSequence  (  )  ;  else   req  .  setChoice  (  )  ; 
return   req  ; 
} 
throw   new   XmlException  (  "Expect ) to end choiceSeq"  )  ; 
} 
} 

void   fillMixed  (  ElementReq   req  )  throws   IOException  ,  XmlException  { 
while  (  true  )  { 
S  (  )  ; 
if  (  unreadPEReference  (  )  )  S  (  )  ; 
if  (  matches  (  XmlTags  .  PAREN_END_STAR  )  )  { 
req  .  setChoice  (  )  ; 
req  .  setStar  (  )  ; 
return  ; 
} 
if  (  matches  (  XmlTags  .  PAREN_END  )  )  { 
if  (  req  .  size  (  )  >  0  )  throw   new   XmlException  (  "Expected )* not )"  )  ; 
return  ; 
} 
if  (  !  matches  (  XmlTags  .  BAR  )  )  throw   new   XmlException  (  "Expected | or )*"  )  ; 
S  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected element name"  )  ; 
req  .  add  (  new   ElementReq  (  name  )  )  ; 
} 
} 








public   ElementReq   Mixed  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  PAREN_BEGIN  )  )  throw   new   XmlException  (  "Expected ("  )  ; 
S  (  )  ; 
if  (  matches  (  XmlTags  .  PCDATA_TAG  )  )  { 
ElementReq   req  =  new   ElementReq  (  )  ; 
req  .  setPCDATA  (  )  ; 
fillMixed  (  req  )  ; 
return   req  ; 
}  else  { 
return   children  (  )  ; 
} 
} 







public   boolean   AttlistDecl  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  ATTLIST_DECL_BEGIN  )  )  return   false  ; 
boolean   space  =  S  (  )  ; 
if  (  unreadPEReference  (  )  )  { 
space  =  S  (  )  ; 
} 
if  (  !  space  )  throw   new   XmlException  (  "Expected space after AttlistDecl"  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after AttlistDecl"  )  ; 
ElementRule   rule  =  dtd  .  getElementRule  (  name  )  ; 
if  (  rule  ==  null  )  { 
rule  =  new   ElementRule  (  null  )  ; 
dtd  .  addElementRule  (  name  ,  rule  )  ; 
} 
while  (  true  )  { 
AttDef  (  rule  )  ; 
int   c  =  scanner  .  peek  (  )  ; 
if  (  c  ==  '>'  )  break  ; 
} 
scanner  .  read  (  )  ; 
return   true  ; 
} 







public   void   AttDef  (  ElementRule   erule  )  throws   IOException  ,  XmlException  { 
boolean   space  =  S  (  )  ; 
if  (  unreadPEReference  (  )  )  space  =  S  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  { 
return  ; 
} 
if  (  space  ==  false  )  throw   new   XmlException  (  "Expected space before AttDef name"  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after AttDef name"  )  ; 
AttributeRule   arule  =  AttType  (  )  ; 
arule  .  setName  (  name  )  ; 
unreadPEReference  (  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after AttDef name"  )  ; 
DefaultDecl  (  arule  )  ; 
erule  .  addAttributeRule  (  arule  )  ; 
} 









public   AttributeRule   AttType  (  )  throws   IOException  ,  XmlException  { 
String   type  =  Name  (  )  ; 
if  (  type  !=  null  )  { 
if  (  type  .  equals  (  XmlTags  .  ST_CDATA  )  )  return   new   AttributeRule  (  AttributeValueType  .  CDATA  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_ID  )  )  return   new   AttributeRule  (  AttributeValueType  .  ID  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_IDREF  )  )  return   new   AttributeRule  (  AttributeValueType  .  IDREF  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_IDREFS  )  )  return   new   AttributeRule  (  AttributeValueType  .  IDREFS  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_ENTITY  )  )  return   new   AttributeRule  (  AttributeValueType  .  ENTITY  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_ENTITIES  )  )  return   new   AttributeRule  (  AttributeValueType  .  ENTITIES  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_NMTOKEN  )  )  return   new   AttributeRule  (  AttributeValueType  .  NMTOKEN  )  ; 
if  (  type  .  equals  (  XmlTags  .  TT_NMTOKENS  )  )  return   new   AttributeRule  (  AttributeValueType  .  NMTOKENS  )  ; 
if  (  type  .  equals  (  XmlTags  .  ET_NOTATION  )  )  { 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after NOTATION"  )  ; 
return   new   AttributeRule  (  AttributeValueType  .  NOTATION  ,  Enumeration  (  )  )  ; 
} 
throw   new   XmlException  (  "Unknown AttType "  +  type  )  ; 
} 
return   new   AttributeRule  (  AttributeValueType  .  NAME_GROUP  ,  Enumeration  (  )  )  ; 
} 






public   Collection  <  String  >  Enumeration  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  PAREN_BEGIN  )  )  throw   new   XmlException  (  "Expected ( in Enumeration"  )  ; 
Collection  <  String  >  list  =  new   ArrayList  <  String  >  (  )  ; 
while  (  true  )  { 
S  (  )  ; 
String   nmtoken  =  Nmtoken  (  )  ; 
list  .  add  (  nmtoken  )  ; 
S  (  )  ; 
int   c  =  scanner  .  read  (  )  ; 
if  (  c  ==  ')'  )  break  ; 
if  (  c  !=  '|'  )  throw   new   XmlException  (  "Expected | in Enumeration: "  +  (  char  )  c  )  ; 
} 
return   list  ; 
} 







public   void   DefaultDecl  (  AttributeRule   rule  )  throws   IOException  ,  XmlException  { 
unreadPEReference  (  )  ; 
if  (  matches  (  XmlTags  .  REQUIRED_BEGIN  )  )  { 
rule  .  setRequired  (  )  ; 
}  else   if  (  matches  (  XmlTags  .  IMPLIED_BEGIN  )  )  { 
}  else   if  (  matches  (  XmlTags  .  FIXED_BEGIN  )  )  { 
S  (  )  ; 
String   v  =  AttValue  (  )  ; 
rule  .  setFixed  (  v  )  ; 
}  else  { 
S  (  )  ; 
String   v  =  AttValue  (  )  ; 
rule  .  setDefault  (  v  )  ; 
} 
} 






public   void   conditionalSect  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlEvent  .  CONDITIONAL_SECT  ,  3  )  )  return  ; 
S  (  )  ; 
unreadPEReference  (  )  ; 
S  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected INCLUDE or IGNORE"  )  ; 
S  (  )  ; 
if  (  scanner  .  read  (  )  !=  '['  )  throw   new   XmlException  (  "Expected [ after "  +  name  )  ; 
if  (  name  .  equals  (  XmlTags  .  IGNORE_BEGIN  )  )  ignoreSect  (  )  ;  else   if  (  name  .  equals  (  XmlTags  .  INCLUDE_BEGIN  )  )  includeSect  (  )  ;  else   throw   new   XmlException  (  "Expected INCLUDE or IGNORE, not "  +  name  )  ; 
} 





public   void   includeSect  (  )  throws   IOException  ,  XmlException  { 
extSubsetDecl  (  )  ; 
if  (  !  matches  (  XmlTags  .  CDATA_END  )  )  throw   new   XmlException  (  "Expected ]]> at end of includeSect"  )  ; 
} 





public   void   ignoreSect  (  )  throws   IOException  ,  XmlException  { 
ignoreSectContents  (  )  ; 
} 





public   void   ignoreSectContents  (  )  throws   IOException  ,  XmlException  { 
while  (  true  )  { 
int   c  =  scanner  .  skipUntil  (  '<'  ,  ']'  )  ; 
if  (  c  ==  '<'  &&  matches  (  XmlTags  .  CONDITIONAL_BEGIN  )  )  { 
ignoreSectContents  (  )  ; 
} 
if  (  c  ==  ']'  &&  matches  (  XmlTags  .  CDATA_END  )  )  { 
break  ; 
} 
if  (  c  ==  -  1  )  throw   new   XmlException  (  "EOF in ignoreSectContents"  )  ; 
scanner  .  read  (  )  ; 
} 
} 





public   void   Ignore  (  )  { 
} 







public   boolean   CharRef  (  )  throws   IOException  ,  XmlException  { 
return   scanner  .  charRef  (  )  ; 
} 





private   void   checkEntityReference  (  Writer   writer  )  throws   IOException  ,  XmlException  { 
scanner  .  read  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after & in entity"  )  ; 
if  (  scanner  .  read  (  )  !=  ';'  )  throw   new   XmlException  (  "Expected ; after "  +  name  +  " in entity"  )  ; 
writer  .  write  (  '&'  )  ; 
writer  .  write  (  name  )  ; 
writer  .  write  (  ';'  )  ; 
} 







public   Entity   Reference  (  )  throws   IOException  ,  XmlException  { 
if  (  scanner  .  peek  (  )  !=  '&'  )  return   null  ; 
if  (  scanner  .  translateReference  (  )  )  return   null  ; 
if  (  CharRef  (  )  )  return   null  ; 
scanner  .  read  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after &"  )  ; 
if  (  scanner  .  read  (  )  !=  ';'  )  throw   new   XmlException  (  "Expected ; after "  +  name  )  ; 
Entity   entity  =  dtd  .  getEntity  (  name  )  ; 
if  (  entity  ==  null  )  throw   new   XmlException  (  "Unknown entity "  +  name  )  ; 
return   entity  ; 
} 

boolean   unreadPEReference  (  )  throws   IOException  ,  XmlException  { 
Entity   e  =  PEReference  (  )  ; 
if  (  e  !=  null  )  { 
scanner  .  unread  (  ' '  )  ; 
scanner  .  unread  (  e  .  resolveAll  (  this  )  )  ; 
scanner  .  unread  (  ' '  )  ; 
return   true  ; 
} 
return   false  ; 
} 







public   Entity   PEReference  (  )  throws   IOException  ,  XmlException  { 
if  (  scanner  .  peek  (  )  !=  '%'  )  return   null  ; 
scanner  .  read  (  )  ; 
String   pe  =  Name  (  )  ; 
if  (  pe  ==  null  )  throw   new   XmlException  (  "Expected name after %"  )  ; 
if  (  scanner  .  read  (  )  !=  ';'  )  throw   new   XmlException  (  "Expected ; after "  +  pe  )  ; 
Entity   entity  =  dtd  .  getParameterEntity  (  pe  )  ; 
if  (  entity  ==  null  )  throw   new   XmlException  (  "Unknown parameter entity "  +  pe  )  ; 
return   entity  ; 
} 







public   boolean   EntityDecl  (  )  throws   IOException  ,  XmlException  { 
if  (  !  matches  (  XmlTags  .  ENTITY_DECL_BEGIN  )  )  return   false  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after <!ENTITY in entity declaration"  )  ; 
String   name  =  null  ; 
boolean   pe  =  false  ; 
if  (  scanner  .  peek  (  )  ==  '%'  )  { 
scanner  .  read  (  )  ; 
if  (  S  (  )  )  { 
pe  =  true  ; 
}  else  { 
scanner  .  unread  (  '%'  )  ; 
unreadPEReference  (  )  ; 
} 
} 
name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected Name in entity declaration"  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after name in entity declaration"  )  ; 
if  (  pe  )  { 
Entity   value  =  PEDef  (  )  ; 
dtd  .  addParameterEntity  (  name  ,  value  )  ; 
}  else  { 
Entity   value  =  EntityDef  (  )  ; 
dtd  .  addEntity  (  name  ,  value  )  ; 
} 
S  (  )  ; 
if  (  scanner  .  read  (  )  !=  '>'  )  throw   new   XmlException  (  "Expected > after for entity declaration"  )  ; 
return   true  ; 
} 





public   void   GEDecl  (  )  throws   IOException  ,  XmlException  { 
EntityDecl  (  )  ; 
} 





public   void   PEDecl  (  )  throws   IOException  ,  XmlException  { 
EntityDecl  (  )  ; 
} 







public   Entity   EntityDef  (  )  throws   IOException  ,  XmlException  { 
Entity   entity  =  ExternalID  (  )  ; 
if  (  entity  !=  null  )  { 
NDataDecl  (  )  ; 
}  else  { 
String   value  =  EntityValue  (  )  ; 
entity  =  new   Entity  (  value  )  ; 
} 
return   entity  ; 
} 







public   Entity   PEDef  (  )  throws   IOException  ,  XmlException  { 
Entity   entity  =  ExternalID  (  )  ; 
if  (  entity  ==  null  )  { 
String   value  =  EntityValue  (  )  ; 
entity  =  new   Entity  (  value  )  ; 
} 
return   entity  ; 
} 

private   Entity   ExternalID  (  boolean   reqSystemLiteral  )  throws   IOException  ,  XmlException  { 
if  (  matches  (  XmlTags  .  PUBLIC_BEGIN  )  )  { 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after PUBLIC"  )  ; 
String   pubid  =  PubidLiteral  (  )  ; 
Entity   entity  ; 
if  (  S  (  )  )  { 
String   sys  =  SystemLiteral  (  )  ; 
entity  =  new   Entity  (  pubid  ,  sys  )  ; 
}  else  { 
if  (  reqSystemLiteral  )  { 
throw   new   XmlException  (  "Expected SystemLiteral"  )  ; 
} 
entity  =  new   Entity  (  pubid  ,  null  )  ; 
} 
return   entity  ; 
} 
if  (  matches  (  XmlTags  .  SYSTEM_BEGIN  )  )  { 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after SYSTEM"  )  ; 
String   sys  =  SystemLiteral  (  )  ; 
Entity   entity  =  new   Entity  (  null  ,  sys  )  ; 
return   entity  ; 
} 
return   null  ; 
} 






public   Entity   ExternalID  (  )  throws   XmlException  ,  IOException  { 
return   ExternalID  (  true  )  ; 
} 







public   String   NDataDecl  (  )  throws   XmlException  ,  IOException  { 
boolean   spaced  =  S  (  )  ; 
if  (  matches  (  XmlTags  .  NDATA_BEGIN  )  )  { 
if  (  !  spaced  )  throw   new   XmlException  (  "Expected space before NDATA"  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after NDATA"  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after NDATA"  )  ; 
return   name  ; 
} 
return   null  ; 
} 







public   boolean   TextDecl  (  )  throws   IOException  ,  XmlException  { 
return   XmlDecl  (  false  )  ; 
} 







public   void   extParsedEnt  (  Element   e  )  throws   XmlException  ,  IOException  { 
TextDecl  (  )  ; 
inEntityScan  =  true  ; 
content  (  e  )  ; 
inEntityScan  =  false  ; 
} 






public   void   extPE  (  )  throws   XmlException  ,  IOException  { 
TextDecl  (  )  ; 
extSubsetDecl  (  )  ; 
} 







public   String   EncodingDecl  (  )  throws   XmlException  ,  IOException  { 
boolean   space  =  S  (  )  ; 
if  (  matches  (  XmlTags  .  ENCODING_BEGIN  )  )  { 
if  (  !  space  )  throw   new   XmlException  (  "Expected space before encoding declaration"  )  ; 
Eq  (  )  ; 
String   enc  =  AttValue  (  false  )  ; 
EncName  (  enc  )  ; 
return   enc  ; 
}  else  { 
if  (  space  )  scanner  .  unread  (  ' '  )  ; 
} 
return   null  ; 
} 







public   void   EncName  (  String   name  )  throws   XmlException  ,  IOException  { 
int   len  =  name  .  length  (  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
char   c  =  name  .  charAt  (  i  )  ; 
if  (  (  c  >=  'a'  &&  c  <=  'z'  )  ||  (  c  >=  'A'  &&  c  <=  'Z'  )  ||  (  c  >=  '0'  &&  c  <=  '9'  )  ||  c  ==  '-'  ||  c  ==  '_'  ||  c  ==  '.'  )  { 
continue  ; 
} 
throw   new   XmlException  (  "Encoding name invalid "  +  name  )  ; 
} 
} 







public   boolean   NotationDecl  (  )  throws   XmlException  ,  IOException  { 
if  (  !  matches  (  XmlTags  .  NOTATION_DECL_BEGIN  )  )  { 
return   false  ; 
} 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after NotationDecl"  )  ; 
if  (  unreadPEReference  (  )  )  S  (  )  ; 
String   name  =  Name  (  )  ; 
if  (  name  ==  null  )  throw   new   XmlException  (  "Expected name after NotationDecl"  )  ; 
if  (  !  S  (  )  )  throw   new   XmlException  (  "Expected space after NotationDecl name"  )  ; 
Entity   entity  =  PublicID  (  )  ; 
Notation   n  =  new   Notation  (  entity  .  getPublicID  (  )  ,  entity  .  getSystemID  (  )  )  ; 
dtd  .  addNotation  (  name  ,  n  )  ; 
S  (  )  ; 
if  (  scanner  .  read  (  )  !=  '>'  )  throw   new   XmlException  (  "Expected > at end of NotationDecl"  )  ; 
return   true  ; 
} 






public   Entity   PublicID  (  )  throws   XmlException  ,  IOException  { 
return   ExternalID  (  false  )  ; 
} 






public   static   boolean   Letter  (  char   l  )  { 
return   Character  .  isLetter  (  l  )  ; 
} 




private   boolean   matches  (  int   event  ,  int   skip  )  throws   XmlException  ,  IOException  { 
if  (  scanner  .  peekEvent  (  )  ==  event  )  { 
scanner  .  skip  (  skip  )  ; 
return   true  ; 
} 
return   false  ; 
} 









boolean   matches  (  final   char   a  [  ]  )  throws   IOException  { 
int   num  =  scanner  .  peek  (  cbuf  ,  0  ,  a  .  length  )  ; 
if  (  num  <  a  .  length  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  if  (  cbuf  [  i  ]  !=  a  [  i  ]  )  { 
return   false  ; 
} 
scanner  .  skip  (  a  .  length  )  ; 
return   true  ; 
} 









void   copyUntil  (  Writer   w  ,  char  [  ]  a  )  throws   IOException  ,  XmlException  { 
int   num  ; 
int   alen  =  a  .  length  ; 
int   alen_  =  a  .  length  -  1  ; 
while  (  true  )  { 
scanner  .  copyUntil  (  w  ,  a  [  0  ]  ,  a  [  0  ]  )  ; 
num  =  scanner  .  read  (  cbuf  ,  0  ,  alen  )  ; 
if  (  num  <  alen  )  throw   new   XmlException  (  "Premature EOF looking for "  +  new   String  (  a  )  )  ; 
boolean   match  =  true  ; 
for  (  int   i  =  1  ;  i  <  alen  ;  i  ++  )  { 
if  (  cbuf  [  i  ]  !=  a  [  i  ]  )  { 
match  =  false  ; 
} 
} 
if  (  !  match  )  { 
w  .  write  (  a  [  0  ]  )  ; 
scanner  .  unread  (  cbuf  ,  1  ,  alen_  )  ; 
}  else  { 
return  ; 
} 
} 
} 




RuleStack   getRuleStack  (  )  { 
return   eruleStack  ; 
} 




private   void   resolveExtSubset  (  Entity   entity  )  throws   IOException  ,  XmlException  { 
try  { 
Reader   r  =  resolver  .  resolve  (  entity  .  getSystemID  (  )  )  ; 
XmlReader   xr  =  new   XmlReader  (  r  ,  getDtd  (  )  )  ; 
xr  .  setResolver  (  resolver  )  ; 
xr  .  extSubset  (  )  ; 
if  (  xr  .  hasMoreData  (  )  )  throw   new   XmlException  (  "Unknown tags or data found in external subset"  )  ; 
xr  .  close  (  )  ; 
}  catch  (  XmlException   r  )  { 
throw   new   ResolverException  (  "Could not resolveExtSubset "  +  entity  ,  r  )  ; 
} 
} 







@  Override 
public   String   toString  (  )  { 
try  { 
int   num  =  scanner  .  peek  (  cbuf  ,  0  ,  16  )  ; 
if  (  num  ==  -  1  )  return  "XmlReader [ EOF ]"  ; 
String   s  =  new   String  (  cbuf  ,  0  ,  num  )  ; 
return  "XmlReader ["  +  s  +  "] ("  +  num  +  ") "  ; 
}  catch  (  IOException   ioe  )  { 
return   super  .  toString  (  )  +  " "  +  ioe  .  toString  (  )  ; 
} 
} 
} 

