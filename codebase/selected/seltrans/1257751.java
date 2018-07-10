package   de  .  juwimm  .  util  .  xml  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Hashtable  ; 
import   org  .  xml  .  sax  .  Attributes  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  XMLReader  ; 
import   org  .  xml  .  sax  .  ext  .  LexicalHandler  ; 
import   org  .  xml  .  sax  .  helpers  .  AttributesImpl  ; 
import   org  .  xml  .  sax  .  helpers  .  NamespaceSupport  ; 
import   org  .  xml  .  sax  .  helpers  .  XMLFilterImpl  ; 



























































































































































































































public   class   XMLWriter   extends   XMLFilterImpl   implements   LexicalHandler  { 

private   boolean   ignoreHeader  =  false  ; 






public   XMLWriter  (  )  { 
init  (  null  )  ; 
} 









public   XMLWriter  (  Writer   writer  )  { 
init  (  writer  )  ; 
} 









public   XMLWriter  (  XMLReader   xmlreader  )  { 
super  (  xmlreader  )  ; 
init  (  null  )  ; 
} 












public   XMLWriter  (  XMLReader   xmlreader  ,  Writer   writer  )  { 
super  (  xmlreader  )  ; 
init  (  writer  )  ; 
} 









private   void   init  (  Writer   writer  )  { 
setOutput  (  writer  )  ; 
nsSupport  =  new   NamespaceSupport  (  )  ; 
prefixTable  =  new   Hashtable  (  )  ; 
forcedDeclTable  =  new   Hashtable  (  )  ; 
doneDeclTable  =  new   Hashtable  (  )  ; 
} 




















public   void   reset  (  )  { 
elementLevel  =  0  ; 
prefixCounter  =  0  ; 
nsSupport  .  reset  (  )  ; 
} 

public   void   ignoreHeader  (  boolean   ignore  )  { 
this  .  ignoreHeader  =  ignore  ; 
} 















public   void   flush  (  )  throws   IOException  { 
output  .  flush  (  )  ; 
} 









public   void   setOutput  (  Writer   writer  )  { 
if  (  writer  ==  null  )  { 
output  =  new   OutputStreamWriter  (  System  .  out  )  ; 
}  else  { 
output  =  writer  ; 
} 
} 















public   void   setPrefix  (  String   uri  ,  String   prefix  )  { 
prefixTable  .  put  (  uri  ,  prefix  )  ; 
} 








public   String   getPrefix  (  String   uri  )  { 
return  (  String  )  prefixTable  .  get  (  uri  )  ; 
} 

















public   void   forceNSDecl  (  String   uri  )  { 
forcedDeclTable  .  put  (  uri  ,  Boolean  .  TRUE  )  ; 
} 














public   void   forceNSDecl  (  String   uri  ,  String   prefix  )  { 
setPrefix  (  uri  ,  prefix  )  ; 
forceNSDecl  (  uri  )  ; 
} 











public   void   startDocument  (  )  throws   SAXException  { 
reset  (  )  ; 
if  (  !  ignoreHeader  )  { 
write  (  "<?xml version=\"1.0\" standalone=\"yes\"?>\n\n"  )  ; 
} 
super  .  startDocument  (  )  ; 
} 











public   void   endDocument  (  )  throws   SAXException  { 
write  (  '\n'  )  ; 
super  .  endDocument  (  )  ; 
try  { 
flush  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   SAXException  (  e  )  ; 
} 
} 




















public   void   startElement  (  String   uri  ,  String   localName  ,  String   qName  ,  Attributes   atts  )  throws   SAXException  { 
elementLevel  ++  ; 
nsSupport  .  pushContext  (  )  ; 
write  (  '<'  )  ; 
writeName  (  uri  ,  localName  ,  qName  ,  true  )  ; 
writeAttributes  (  atts  )  ; 
if  (  elementLevel  ==  1  )  { 
forceNSDecls  (  )  ; 
} 
writeNSDecls  (  )  ; 
write  (  '>'  )  ; 
super  .  startElement  (  uri  ,  localName  ,  qName  ,  atts  )  ; 
} 



















public   void   endElement  (  String   uri  ,  String   localName  ,  String   qName  )  throws   SAXException  { 
write  (  "</"  )  ; 
writeName  (  uri  ,  localName  ,  qName  ,  true  )  ; 
write  (  '>'  )  ; 
if  (  elementLevel  ==  1  )  { 
write  (  '\n'  )  ; 
} 
super  .  endElement  (  uri  ,  localName  ,  qName  )  ; 
nsSupport  .  popContext  (  )  ; 
elementLevel  --  ; 
} 














public   void   characters  (  char  [  ]  ch  ,  int   start  ,  int   len  )  throws   SAXException  { 
writeEsc  (  ch  ,  start  ,  len  ,  false  )  ; 
super  .  characters  (  ch  ,  start  ,  len  )  ; 
} 














public   void   ignorableWhitespace  (  char  [  ]  ch  ,  int   start  ,  int   length  )  throws   SAXException  { 
writeEsc  (  ch  ,  start  ,  length  ,  false  )  ; 
super  .  ignorableWhitespace  (  ch  ,  start  ,  length  )  ; 
} 













public   void   processingInstruction  (  String   target  ,  String   data  )  throws   SAXException  { 
write  (  "<?"  )  ; 
write  (  target  )  ; 
write  (  ' '  )  ; 
write  (  data  )  ; 
write  (  "?>"  )  ; 
if  (  elementLevel  <  1  )  { 
write  (  '\n'  )  ; 
} 
super  .  processingInstruction  (  target  ,  data  )  ; 
} 

























public   void   emptyElement  (  String   uri  ,  String   localName  ,  String   qName  ,  Attributes   atts  )  throws   SAXException  { 
nsSupport  .  pushContext  (  )  ; 
write  (  '<'  )  ; 
writeName  (  uri  ,  localName  ,  qName  ,  true  )  ; 
writeAttributes  (  atts  )  ; 
if  (  elementLevel  ==  1  )  { 
forceNSDecls  (  )  ; 
} 
writeNSDecls  (  )  ; 
write  (  "/>"  )  ; 
super  .  startElement  (  uri  ,  localName  ,  qName  ,  atts  )  ; 
super  .  endElement  (  uri  ,  localName  ,  qName  )  ; 
} 

















public   void   startElement  (  String   uri  ,  String   localName  )  throws   SAXException  { 
startElement  (  uri  ,  localName  ,  ""  ,  EMPTY_ATTS  )  ; 
} 
















public   void   startElement  (  String   localName  )  throws   SAXException  { 
startElement  (  ""  ,  localName  ,  ""  ,  EMPTY_ATTS  )  ; 
} 















public   void   endElement  (  String   uri  ,  String   localName  )  throws   SAXException  { 
endElement  (  uri  ,  localName  ,  ""  )  ; 
} 















public   void   endElement  (  String   localName  )  throws   SAXException  { 
endElement  (  ""  ,  localName  ,  ""  )  ; 
} 
















public   void   emptyElement  (  String   uri  ,  String   localName  )  throws   SAXException  { 
emptyElement  (  uri  ,  localName  ,  ""  ,  EMPTY_ATTS  )  ; 
} 
















public   void   emptyElement  (  String   localName  )  throws   SAXException  { 
emptyElement  (  ""  ,  localName  ,  ""  ,  EMPTY_ATTS  )  ; 
} 


























public   void   dataElement  (  String   uri  ,  String   localName  ,  String   qName  ,  Attributes   atts  ,  String   content  )  throws   SAXException  { 
startElement  (  uri  ,  localName  ,  qName  ,  atts  )  ; 
characters  (  content  )  ; 
endElement  (  uri  ,  localName  ,  qName  )  ; 
} 

























public   void   dataElement  (  String   uri  ,  String   localName  ,  String   content  )  throws   SAXException  { 
dataElement  (  uri  ,  localName  ,  ""  ,  EMPTY_ATTS  ,  content  )  ; 
} 

























public   void   dataElement  (  String   localName  ,  String   content  )  throws   SAXException  { 
dataElement  (  ""  ,  localName  ,  ""  ,  EMPTY_ATTS  ,  content  )  ; 
} 














public   void   characters  (  String   data  )  throws   SAXException  { 
char  [  ]  ch  =  data  .  toCharArray  (  )  ; 
characters  (  ch  ,  0  ,  ch  .  length  )  ; 
} 







private   void   forceNSDecls  (  )  { 
Enumeration   prefixes  =  forcedDeclTable  .  keys  (  )  ; 
while  (  prefixes  .  hasMoreElements  (  )  )  { 
String   prefix  =  (  String  )  prefixes  .  nextElement  (  )  ; 
doPrefix  (  prefix  ,  null  ,  true  )  ; 
} 
} 












private   String   doPrefix  (  String   uri  ,  String   qName  ,  boolean   isElement  )  { 
String   defaultNS  =  nsSupport  .  getURI  (  ""  )  ; 
if  (  ""  .  equals  (  uri  )  )  { 
if  (  isElement  &&  defaultNS  !=  null  )  { 
nsSupport  .  declarePrefix  (  ""  ,  ""  )  ; 
} 
return   null  ; 
} 
String   prefix  ; 
if  (  isElement  &&  defaultNS  !=  null  &&  uri  .  equals  (  defaultNS  )  )  { 
prefix  =  ""  ; 
}  else  { 
prefix  =  nsSupport  .  getPrefix  (  uri  )  ; 
} 
if  (  prefix  !=  null  )  { 
return   prefix  ; 
} 
prefix  =  (  String  )  doneDeclTable  .  get  (  uri  )  ; 
if  (  prefix  !=  null  &&  (  (  !  isElement  ||  defaultNS  !=  null  )  &&  ""  .  equals  (  prefix  )  ||  nsSupport  .  getURI  (  prefix  )  !=  null  )  )  { 
prefix  =  null  ; 
} 
if  (  prefix  ==  null  )  { 
prefix  =  (  String  )  prefixTable  .  get  (  uri  )  ; 
if  (  prefix  !=  null  &&  (  (  !  isElement  ||  defaultNS  !=  null  )  &&  ""  .  equals  (  prefix  )  ||  nsSupport  .  getURI  (  prefix  )  !=  null  )  )  { 
prefix  =  null  ; 
} 
} 
if  (  prefix  ==  null  &&  qName  !=  null  &&  !  ""  .  equals  (  qName  )  )  { 
int   i  =  qName  .  indexOf  (  ':'  )  ; 
if  (  i  ==  -  1  )  { 
if  (  isElement  &&  defaultNS  ==  null  )  { 
prefix  =  ""  ; 
} 
}  else  { 
prefix  =  qName  .  substring  (  0  ,  i  )  ; 
} 
} 
for  (  ;  prefix  ==  null  ||  nsSupport  .  getURI  (  prefix  )  !=  null  ;  prefix  =  "__NS"  +  ++  prefixCounter  )  { 
; 
} 
nsSupport  .  declarePrefix  (  prefix  ,  uri  )  ; 
doneDeclTable  .  put  (  uri  ,  prefix  )  ; 
return   prefix  ; 
} 









private   void   write  (  char   c  )  throws   SAXException  { 
try  { 
output  .  write  (  c  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   SAXException  (  e  )  ; 
} 
} 









private   void   write  (  String   s  )  throws   SAXException  { 
try  { 
output  .  write  (  s  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   SAXException  (  e  )  ; 
} 
} 











private   void   writeAttributes  (  Attributes   atts  )  throws   SAXException  { 
int   len  =  atts  .  getLength  (  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
char  [  ]  ch  =  atts  .  getValue  (  i  )  .  toCharArray  (  )  ; 
write  (  ' '  )  ; 
writeName  (  atts  .  getURI  (  i  )  ,  atts  .  getLocalName  (  i  )  ,  atts  .  getQName  (  i  )  ,  false  )  ; 
write  (  "=\""  )  ; 
writeEsc  (  ch  ,  0  ,  ch  .  length  ,  true  )  ; 
write  (  '"'  )  ; 
} 
} 












private   void   writeEsc  (  char  [  ]  ch  ,  int   start  ,  int   length  ,  boolean   isAttVal  )  throws   SAXException  { 
for  (  int   i  =  start  ;  i  <  start  +  length  ;  i  ++  )  { 
switch  (  ch  [  i  ]  )  { 
case  '&'  : 
write  (  "&amp;"  )  ; 
break  ; 
case  '<'  : 
write  (  "&lt;"  )  ; 
break  ; 
case  '>'  : 
write  (  "&gt;"  )  ; 
break  ; 
case  '\"'  : 
if  (  isAttVal  )  { 
write  (  "&quot;"  )  ; 
}  else  { 
write  (  '\"'  )  ; 
} 
break  ; 
default  : 
if  (  ch  [  i  ]  >  ''  )  { 
write  (  "&#"  )  ; 
write  (  Integer  .  toString  (  ch  [  i  ]  )  )  ; 
write  (  ';'  )  ; 
}  else  { 
write  (  ch  [  i  ]  )  ; 
} 
} 
} 
} 









private   void   writeNSDecls  (  )  throws   SAXException  { 
Enumeration   prefixes  =  nsSupport  .  getDeclaredPrefixes  (  )  ; 
while  (  prefixes  .  hasMoreElements  (  )  )  { 
String   prefix  =  (  String  )  prefixes  .  nextElement  (  )  ; 
String   uri  =  nsSupport  .  getURI  (  prefix  )  ; 
if  (  uri  ==  null  )  { 
uri  =  ""  ; 
} 
char  [  ]  ch  =  uri  .  toCharArray  (  )  ; 
write  (  ' '  )  ; 
if  (  ""  .  equals  (  prefix  )  )  { 
write  (  "xmlns=\""  )  ; 
}  else  { 
write  (  "xmlns:"  )  ; 
write  (  prefix  )  ; 
write  (  "=\""  )  ; 
} 
writeEsc  (  ch  ,  0  ,  ch  .  length  ,  true  )  ; 
write  (  '\"'  )  ; 
} 
} 













private   void   writeName  (  String   uri  ,  String   localName  ,  String   qName  ,  boolean   isElement  )  throws   SAXException  { 
String   prefix  =  doPrefix  (  uri  ,  qName  ,  isElement  )  ; 
if  (  prefix  !=  null  &&  !  ""  .  equals  (  prefix  )  )  { 
write  (  prefix  )  ; 
write  (  ':'  )  ; 
} 
write  (  localName  )  ; 
} 

private   static   final   Attributes   EMPTY_ATTS  =  new   AttributesImpl  (  )  ; 

private   Hashtable   prefixTable  ; 

private   Hashtable   forcedDeclTable  ; 

private   Hashtable   doneDeclTable  ; 

private   int   elementLevel  =  0  ; 

private   Writer   output  ; 

private   NamespaceSupport   nsSupport  ; 

private   int   prefixCounter  =  0  ; 

public   void   startDTD  (  String   name  ,  String   publicId  ,  String   systemId  )  throws   SAXException  { 
} 

public   void   endDTD  (  )  throws   SAXException  { 
} 

public   void   startEntity  (  String   name  )  throws   SAXException  { 
} 

public   void   endEntity  (  String   name  )  throws   SAXException  { 
} 

public   void   startCDATA  (  )  throws   SAXException  { 
} 

public   void   endCDATA  (  )  throws   SAXException  { 
} 

public   void   comment  (  char  [  ]  ch  ,  int   start  ,  int   length  )  throws   SAXException  { 
} 
} 

