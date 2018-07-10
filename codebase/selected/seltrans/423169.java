package   machine  ; 

import   java  .  awt  .  Point  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Map  .  Entry  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilder  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   javax  .  xml  .  transform  .  OutputKeys  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   main  .  Config  ; 
import   main  .  Constants  ; 
import   modules  .  machineTree  .  MachineTree  ; 
import   org  .  w3c  .  dom  .  Attr  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  xml  .  sax  .  ErrorHandler  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 







public   class   XML_converter  { 














private   static   Element   createElement_string  (  String   title  ,  String   str  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  title  )  ; 
elem  .  appendChild  (  doc  .  createTextNode  (  str  )  )  ; 
return   elem  ; 
} 
















private   static   Element   createElement_pos  (  int   depth  ,  String   title  ,  Point   pos  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  title  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
Element   elemSub  =  doc  .  createElement  (  "posX"  )  ; 
elemSub  .  appendChild  (  doc  .  createTextNode  (  String  .  valueOf  (  pos  .  x  )  )  )  ; 
elem  .  appendChild  (  elemSub  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
elemSub  =  doc  .  createElement  (  "posY"  )  ; 
elemSub  .  appendChild  (  doc  .  createTextNode  (  String  .  valueOf  (  pos  .  y  )  )  )  ; 
elem  .  appendChild  (  elemSub  )  ; 
createCRLF_TAB  (  depth  -  1  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 
















private   static   Element   createElement_symbPos  (  int   depth  ,  String   strVal  ,  int   intVal  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "symbPos"  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "symbol"  ,  strVal  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
Element   elemSub  =  doc  .  createElement  (  "pos"  )  ; 
elemSub  .  appendChild  (  doc  .  createTextNode  (  String  .  valueOf  (  intVal  )  )  )  ; 
elem  .  appendChild  (  elemSub  )  ; 
createCRLF_TAB  (  depth  -  1  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_opns  (  int   depth  ,  TransitionOpns   op  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "operation"  )  ; 
Attr   att  =  doc  .  createAttribute  (  "headMove"  )  ; 
att  .  setValue  (  op  .  getHm  (  )  .  shortToString  (  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
att  =  doc  .  createAttribute  (  "tapeID"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  op  .  getTape  (  )  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
if  (  op  .  getReadSymb  (  )  .  getSymbol  (  )  !=  null  )  elem  .  appendChild  (  createElement_string  (  "symbolRead"  ,  op  .  getReadSymb  (  )  .  getSymbol  (  )  ,  doc  )  )  ;  else   if  (  op  .  getReadSymb  (  )  .  isBlank  (  )  )  elem  .  appendChild  (  createElement_string  (  "symbolRead"  ,  Constants  .  XML_BLANK  ,  doc  )  )  ;  else   if  (  op  .  getReadSymb  (  )  .  isEpsilon  (  )  )  elem  .  appendChild  (  createElement_string  (  "symbolRead"  ,  Constants  .  XML_EPSILON  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
if  (  op  .  getWriteSymb  (  )  .  getSymbol  (  )  !=  null  )  elem  .  appendChild  (  createElement_string  (  "symbolWrite"  ,  op  .  getReadSymb  (  )  .  getSymbol  (  )  ,  doc  )  )  ;  else   if  (  op  .  getWriteSymb  (  )  .  isBlank  (  )  )  elem  .  appendChild  (  createElement_string  (  "symbolWrite"  ,  Constants  .  XML_BLANK  ,  doc  )  )  ;  else   if  (  op  .  getWriteSymb  (  )  .  isEpsilon  (  )  )  elem  .  appendChild  (  createElement_string  (  "symbolWrite"  ,  Constants  .  XML_EPSILON  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  -  1  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_alphabet  (  int   depth  ,  Vector  <  String  >  symbols  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "alphabet"  )  ; 
for  (  String   it  :  symbols  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "symbol"  ,  it  ,  doc  )  )  ; 
} 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_state  (  int   depth  ,  State   s  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "state"  )  ; 
Attr   att  =  doc  .  createAttribute  (  "stateID"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  s  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
att  =  doc  .  createAttribute  (  "kind"  )  ; 
att  .  setValue  (  s  .  getType  (  )  .  toXMLString  (  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "shortName"  ,  s  .  getName  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_pos  (  depth  +  2  ,  "position"  ,  s  .  getPosition  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_submachine  (  int   depth  ,  Submachine   s  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "submachine"  )  ; 
Attr   att  =  doc  .  createAttribute  (  "submachineID"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  s  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "shortName"  ,  s  .  getName  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "path"  ,  Config  .  makeRelative  (  s  .  getPath  (  )  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_pos  (  depth  +  2  ,  "position"  ,  s  .  getPosition  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_transitions  (  int   depth  ,  Transition   t  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "trans"  )  ; 
Attr   att  =  doc  .  createAttribute  (  "to"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  t  .  getEnd  (  )  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
att  =  doc  .  createAttribute  (  "from"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  t  .  getStart  (  )  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
for  (  TransitionOpns   it  :  t  .  getOpns  (  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_opns  (  depth  +  2  ,  it  ,  doc  )  )  ; 
} 
if  (  t  .  getStart  (  )  .  getClass  (  )  .  equals  (  Submachine  .  class  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "startName"  ,  t  .  getStartName  (  )  ,  doc  )  )  ; 
} 
if  (  t  .  getEnd  (  )  .  getClass  (  )  .  equals  (  Submachine  .  class  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "endName"  ,  t  .  getEndName  (  )  ,  doc  )  )  ; 
} 
if  (  t  .  getAnchorExpr  (  )  !=  null  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "anchorExpr"  ,  t  .  getAnchorExpr  (  )  ,  doc  )  )  ; 
} 
if  (  !  t  .  isNormalized  (  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_pos  (  depth  +  2  ,  "positionAnchor"  ,  t  .  getTextAnchor  (  )  .  getPosition  (  )  ,  doc  )  )  ; 
} 
if  (  !  t  .  isNormalized  (  )  )  for  (  TransitionDot   it  :  t  .  getDots  (  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_pos  (  depth  +  2  ,  "position"  ,  it  .  getPosition  (  )  ,  doc  )  )  ; 
} 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 












private   static   Element   createElement_tape  (  int   depth  ,  Tape   t  ,  Document   doc  )  { 
Element   elem  =  doc  .  createElement  (  "tape"  )  ; 
Attr   att  =  doc  .  createAttribute  (  "tapeID"  )  ; 
att  .  setValue  (  "x"  +  String  .  valueOf  (  t  .  getID  (  )  )  )  ; 
elem  .  setAttributeNode  (  att  )  ; 
for  (  Entry   it  :  t  .  getContent  (  )  .  entrySet  (  )  )  { 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_symbPos  (  depth  +  2  ,  (  (  Symbol  )  it  .  getValue  (  )  )  .  getSymbol  (  )  ,  (  (  Integer  )  it  .  getKey  (  )  )  .  intValue  (  )  ,  doc  )  )  ; 
} 
createCRLF_TAB  (  depth  +  1  ,  elem  ,  doc  )  ; 
elem  .  appendChild  (  createElement_string  (  "description"  ,  t  .  getDescription  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  elem  ,  doc  )  ; 
return   elem  ; 
} 















protected   static   void   save  (  Turingmachine   tm  ,  OutputStream   os  )  throws   TransformerException  ,  ParserConfigurationException  { 
DocumentBuilderFactory   f  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
DocumentBuilder   db  =  f  .  newDocumentBuilder  (  )  ; 
Document   doc  =  db  .  newDocument  (  )  ; 
int   depth  =  1  ; 
Element   root  =  doc  .  createElement  (  "machine"  )  ; 
doc  .  appendChild  (  root  )  ; 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_string  (  "description"  ,  tm  .  getDescription  (  )  ,  doc  )  )  ; 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_alphabet  (  depth  ,  tm  .  getSymbols  (  )  ,  doc  )  )  ; 
for  (  State   it  :  tm  .  getStates  (  )  )  { 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_state  (  depth  ,  it  ,  doc  )  )  ; 
} 
for  (  Submachine   it  :  tm  .  getSubmachines  (  )  )  { 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_submachine  (  depth  ,  it  ,  doc  )  )  ; 
} 
for  (  Tape   it  :  tm  .  getTapes  (  )  )  { 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_tape  (  depth  ,  it  ,  doc  )  )  ; 
} 
for  (  Transition   it  :  tm  .  getTransitions  (  )  )  { 
createCRLF_TAB  (  depth  ,  root  ,  doc  )  ; 
root  .  appendChild  (  createElement_transitions  (  depth  ,  it  ,  doc  )  )  ; 
} 
root  .  appendChild  (  doc  .  createTextNode  (  "\n"  )  )  ; 
TransformerFactory   tFactory  =  TransformerFactory  .  newInstance  (  )  ; 
Transformer   transformer  =  tFactory  .  newTransformer  (  )  ; 
transformer  .  setOutputProperty  (  OutputKeys  .  DOCTYPE_SYSTEM  ,  Constants  .  MACHINE_DTD_FILE  )  ; 
DOMSource   source  =  new   DOMSource  (  doc  )  ; 
StreamResult   result  =  new   StreamResult  (  os  )  ; 
transformer  .  transform  (  source  ,  result  )  ; 
} 












private   static   void   createCRLF_TAB  (  int   depth  ,  Element   root  ,  Document   doc  )  { 
String   str  =  "\n"  ; 
while  (  depth  --  >  0  )  str  +=  "   "  ; 
root  .  appendChild  (  doc  .  createTextNode  (  str  )  )  ; 
} 






















public   static   void   load  (  InputStream   is  ,  Turingmachine   res  ,  MachineTree   machTree  )  throws   Exception  { 
DocumentBuilderFactory   dbFactory  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
dbFactory  .  setValidating  (  true  )  ; 
DocumentBuilder   docBuilder  =  dbFactory  .  newDocumentBuilder  (  )  ; 
docBuilder  .  setErrorHandler  (  new   ErrorHandler  (  )  { 

public   void   warning  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 

public   void   error  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 

public   void   fatalError  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 
}  )  ; 
Document   doc  =  docBuilder  .  parse  (  is  ,  (  new   File  (  Constants  .  MACHINE_DTD_FILE  )  )  .  toURI  (  )  .  toString  (  )  )  ; 
HashMap  <  Integer  ,  Integer  >  idMap  =  new   HashMap  <  Integer  ,  Integer  >  (  )  ; 
Element   root  =  doc  .  getDocumentElement  (  )  ; 
Node   child  =  root  .  getFirstChild  (  )  ; 
while  (  child  !=  null  )  { 
if  (  child  .  getNodeName  (  )  .  equals  (  "description"  )  )  { 
res  .  setDescription  (  child  .  getFirstChild  (  )  .  getNodeValue  (  )  )  ; 
}  else   if  (  child  .  getNodeName  (  )  .  equals  (  "alphabet"  )  )  { 
Vector  <  String  >  tmp  =  getStringValuesOutOfNode  (  child  ,  "symbol"  )  ; 
for  (  String   it  :  tmp  )  res  .  addSymbols  (  it  )  ; 
}  else   if  (  child  .  getNodeName  (  )  .  equals  (  "state"  )  )  { 
String   shortName  =  null  ; 
String   posX  =  null  ; 
String   posY  =  null  ; 
String   stateID  =  null  ; 
String   kind  =  null  ; 
stateID  =  child  .  getAttributes  (  )  .  getNamedItem  (  "stateID"  )  .  getNodeValue  (  )  ; 
kind  =  child  .  getAttributes  (  )  .  getNamedItem  (  "kind"  )  .  getNodeValue  (  )  ; 
Node   subChild  =  child  .  getFirstChild  (  )  ; 
while  (  subChild  !=  null  )  { 
if  (  subChild  .  getNodeName  (  )  .  equals  (  "shortName"  )  )  { 
shortName  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "position"  )  )  { 
posX  =  getStringValuesOutOfNode  (  subChild  ,  "posX"  )  .  elementAt  (  0  )  ; 
posY  =  getStringValuesOutOfNode  (  subChild  ,  "posY"  )  .  elementAt  (  0  )  ; 
} 
subChild  =  subChild  .  getNextSibling  (  )  ; 
} 
StateType   st  =  null  ; 
if  (  kind  .  equals  (  Constants  .  STATE_A_XML_STRING  )  )  st  =  new   StateType  (  false  ,  true  )  ;  else   if  (  kind  .  equals  (  Constants  .  STATE_SA_XML_STRING  )  )  st  =  new   StateType  (  true  ,  true  )  ;  else   if  (  kind  .  equals  (  Constants  .  STATE_N_XML_STRING  )  )  st  =  new   StateType  (  false  ,  false  )  ;  else   if  (  kind  .  equals  (  Constants  .  STATE_S_XML_STRING  )  )  st  =  new   StateType  (  true  ,  false  )  ; 
int   curID  =  res  .  addState  (  shortName  ,  st  ,  new   Point  (  Integer  .  parseInt  (  posX  )  ,  Integer  .  parseInt  (  posY  )  )  )  ; 
idMap  .  put  (  Integer  .  valueOf  (  Integer  .  parseInt  (  stateID  .  substring  (  1  )  )  )  ,  Integer  .  valueOf  (  curID  )  )  ; 
}  else   if  (  child  .  getNodeName  (  )  .  equals  (  "submachine"  )  )  { 
String   shortName  =  null  ; 
String   posX  =  null  ; 
String   posY  =  null  ; 
String   submachineID  =  null  ; 
String   path  =  null  ; 
submachineID  =  child  .  getAttributes  (  )  .  getNamedItem  (  "submachineID"  )  .  getNodeValue  (  )  ; 
Node   subChild  =  child  .  getFirstChild  (  )  ; 
while  (  subChild  !=  null  )  { 
if  (  subChild  .  getNodeName  (  )  .  equals  (  "shortName"  )  )  { 
shortName  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "position"  )  )  { 
posX  =  getStringValuesOutOfNode  (  subChild  ,  "posX"  )  .  elementAt  (  0  )  ; 
posY  =  getStringValuesOutOfNode  (  subChild  ,  "posY"  )  .  elementAt  (  0  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "path"  )  )  { 
path  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
} 
subChild  =  subChild  .  getNextSibling  (  )  ; 
} 
Turingmachine   tm  ; 
tm  =  machTree  .  getMachineByFilePath  (  Config  .  makeAbsolute  (  path  )  )  ; 
int   curID  =  res  .  addSubmachine  (  shortName  ,  new   Point  (  Integer  .  parseInt  (  posX  )  ,  Integer  .  parseInt  (  posY  )  )  ,  tm  )  ; 
idMap  .  put  (  Integer  .  valueOf  (  Integer  .  parseInt  (  submachineID  .  substring  (  1  )  )  )  ,  Integer  .  valueOf  (  curID  )  )  ; 
}  else   if  (  child  .  getNodeName  (  )  .  equals  (  "trans"  )  )  { 
Vector  <  TransitionOpns  >  opns  =  new   Vector  <  TransitionOpns  >  (  )  ; 
Vector  <  Point  >  dots  =  new   Vector  <  Point  >  (  )  ; 
String   startName  =  null  ; 
String   endName  =  null  ; 
String   anchorExpr  =  null  ; 
Point   anchor  =  null  ; 
String   toID  =  null  ; 
String   fromID  =  null  ; 
toID  =  child  .  getAttributes  (  )  .  getNamedItem  (  "to"  )  .  getNodeValue  (  )  ; 
fromID  =  child  .  getAttributes  (  )  .  getNamedItem  (  "from"  )  .  getNodeValue  (  )  ; 
Node   subChild  =  child  .  getFirstChild  (  )  ; 
while  (  subChild  !=  null  )  { 
if  (  subChild  .  getNodeName  (  )  .  equals  (  "startName"  )  )  { 
startName  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "endName"  )  )  { 
endName  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "anchorExpr"  )  )  { 
anchorExpr  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "position"  )  )  { 
String   posX  =  null  ; 
String   posY  =  null  ; 
posX  =  getStringValuesOutOfNode  (  subChild  ,  "posX"  )  .  elementAt  (  0  )  ; 
posY  =  getStringValuesOutOfNode  (  subChild  ,  "posY"  )  .  elementAt  (  0  )  ; 
dots  .  add  (  new   Point  (  Integer  .  parseInt  (  posX  )  ,  Integer  .  parseInt  (  posY  )  )  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "positionAnchor"  )  )  { 
String   posX  =  null  ; 
String   posY  =  null  ; 
posX  =  getStringValuesOutOfNode  (  subChild  ,  "posX"  )  .  elementAt  (  0  )  ; 
posY  =  getStringValuesOutOfNode  (  subChild  ,  "posY"  )  .  elementAt  (  0  )  ; 
anchor  =  new   Point  (  Integer  .  parseInt  (  posX  )  ,  Integer  .  parseInt  (  posY  )  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "operation"  )  )  { 
String   tapeID  =  null  ; 
String   headMove  =  null  ; 
String   symbolRead  =  null  ; 
String   symbolWrite  =  null  ; 
tapeID  =  subChild  .  getAttributes  (  )  .  getNamedItem  (  "tapeID"  )  .  getNodeValue  (  )  ; 
headMove  =  subChild  .  getAttributes  (  )  .  getNamedItem  (  "headMove"  )  .  getNodeValue  (  )  ; 
Node   subSubChild  =  subChild  .  getFirstChild  (  )  ; 
while  (  subSubChild  !=  null  )  { 
if  (  subSubChild  .  getNodeName  (  )  .  equals  (  "symbolRead"  )  )  { 
symbolRead  =  subSubChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subSubChild  .  getNodeName  (  )  .  equals  (  "symbolWrite"  )  )  { 
symbolWrite  =  subSubChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
} 
subSubChild  =  subSubChild  .  getNextSibling  (  )  ; 
} 
HeadMove   hm  =  null  ; 
if  (  headMove  .  equals  (  Constants  .  HEAD_NONE_STRING_SHORT  )  )  hm  =  new   HeadMove  (  HeadMove  .  direction  .  NONE  )  ;  else   if  (  headMove  .  equals  (  Constants  .  HEAD_LEFT_STRING_SHORT  )  )  hm  =  new   HeadMove  (  HeadMove  .  direction  .  LEFT  )  ;  else   if  (  headMove  .  equals  (  Constants  .  HEAD_RIGHT_STRING_SHORT  )  )  hm  =  new   HeadMove  (  HeadMove  .  direction  .  RIGHT  )  ; 
Tape   t  =  res  .  getTapeByID  (  idMap  .  get  (  new   Integer  (  Integer  .  parseInt  (  tapeID  .  substring  (  1  )  )  )  )  .  intValue  (  )  )  ; 
Symbol   read  =  res  .  getSymbol  (  symbolRead  )  ; 
if  (  read  ==  null  &&  symbolRead  .  equals  (  Constants  .  XML_BLANK  )  )  read  =  new   Symbol  (  Symbol  .  symbolType  .  BLANK  )  ;  else   if  (  read  ==  null  &&  symbolRead  .  equals  (  Constants  .  XML_EPSILON  )  )  read  =  new   Symbol  (  Symbol  .  symbolType  .  EPSILON  )  ; 
Symbol   write  =  res  .  getSymbol  (  symbolWrite  )  ; 
if  (  write  ==  null  &&  symbolRead  .  equals  (  Constants  .  XML_BLANK  )  )  write  =  new   Symbol  (  Symbol  .  symbolType  .  BLANK  )  ;  else   if  (  write  ==  null  &&  symbolRead  .  equals  (  Constants  .  XML_EPSILON  )  )  write  =  new   Symbol  (  Symbol  .  symbolType  .  EPSILON  )  ; 
opns  .  add  (  new   TransitionOpns  (  hm  ,  read  ,  write  ,  t  )  )  ; 
} 
subChild  =  subChild  .  getNextSibling  (  )  ; 
} 
int   realFromID  =  idMap  .  get  (  new   Integer  (  Integer  .  parseInt  (  fromID  .  substring  (  1  )  )  )  )  .  intValue  (  )  ; 
int   realToID  =  idMap  .  get  (  new   Integer  (  Integer  .  parseInt  (  toID  .  substring  (  1  )  )  )  )  .  intValue  (  )  ; 
Transition   tran  =  new   Transition  (  res  .  getOval  (  realFromID  )  ,  startName  ,  res  .  getOval  (  realToID  )  ,  endName  ,  dots  ,  opns  ,  anchor  ,  res  )  ; 
res  .  addTransition  (  tran  )  ; 
res  .  setAnchorExpr  (  tran  ,  anchorExpr  )  ; 
}  else   if  (  child  .  getNodeName  (  )  .  equals  (  "tape"  )  )  { 
String   tapeID  =  null  ; 
String   tapeDescription  =  null  ; 
tapeID  =  child  .  getAttributes  (  )  .  getNamedItem  (  "tapeID"  )  .  getNodeValue  (  )  ; 
Vector  <  String  >  itemsStr  =  new   Vector  <  String  >  (  )  ; 
Vector  <  Integer  >  itemsInt  =  new   Vector  <  Integer  >  (  )  ; 
Node   subChild  =  child  .  getFirstChild  (  )  ; 
while  (  subChild  !=  null  )  { 
if  (  subChild  .  getNodeName  (  )  .  equals  (  "description"  )  )  { 
tapeDescription  =  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subChild  .  getNodeName  (  )  .  equals  (  "symbPos"  )  )  { 
Node   subSubChild  =  subChild  .  getFirstChild  (  )  ; 
String   symb  =  null  ; 
String   pos  =  null  ; 
while  (  subSubChild  !=  null  )  { 
if  (  subSubChild  .  getNodeName  (  )  .  equals  (  "symbol"  )  )  { 
symb  =  subSubChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
}  else   if  (  subSubChild  .  getNodeName  (  )  .  equals  (  "pos"  )  )  { 
pos  =  subSubChild  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
} 
subSubChild  =  subSubChild  .  getNextSibling  (  )  ; 
} 
itemsStr  .  add  (  symb  )  ; 
itemsInt  .  add  (  new   Integer  (  Integer  .  parseInt  (  pos  )  )  )  ; 
} 
subChild  =  subChild  .  getNextSibling  (  )  ; 
} 
int   parsedTapeID  =  Integer  .  parseInt  (  tapeID  .  substring  (  1  )  )  ; 
int   realTapeID  =  res  .  addTape  (  tapeDescription  )  .  getID  (  )  ; 
idMap  .  put  (  Integer  .  valueOf  (  parsedTapeID  )  ,  Integer  .  valueOf  (  realTapeID  )  )  ; 
Iterator   itS  =  itemsStr  .  iterator  (  )  ; 
Iterator   itI  =  itemsInt  .  iterator  (  )  ; 
for  (  ;  itS  .  hasNext  (  )  &&  itI  .  hasNext  (  )  ;  )  res  .  setSymbolAt  (  res  .  getTapeByID  (  realTapeID  )  ,  (  String  )  itS  .  next  (  )  ,  (  (  Integer  )  itI  .  next  (  )  )  .  intValue  (  )  )  ; 
} 
child  =  child  .  getNextSibling  (  )  ; 
} 
} 











private   static   Vector  <  String  >  getStringValuesOutOfNode  (  Node   child  ,  String   nodeName  )  { 
Vector  <  String  >  res  =  new   Vector  <  String  >  (  )  ; 
Node   subChild  =  child  .  getFirstChild  (  )  ; 
while  (  subChild  !=  null  )  { 
if  (  subChild  .  getNodeName  (  )  .  equals  (  nodeName  )  )  res  .  add  (  subChild  .  getFirstChild  (  )  .  getNodeValue  (  )  )  ; 
subChild  =  subChild  .  getNextSibling  (  )  ; 
} 
return   res  ; 
} 


















public   static   String   getDescr  (  InputStream   is  )  throws   SAXException  ,  IOException  ,  ParserConfigurationException  { 
DocumentBuilderFactory   dbFactory  =  DocumentBuilderFactory  .  newInstance  (  )  ; 
dbFactory  .  setValidating  (  true  )  ; 
DocumentBuilder   docBuilder  =  dbFactory  .  newDocumentBuilder  (  )  ; 
docBuilder  .  setErrorHandler  (  new   ErrorHandler  (  )  { 

public   void   warning  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 

public   void   error  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 

public   void   fatalError  (  SAXParseException   arg0  )  throws   SAXException  { 
throw   arg0  ; 
} 
}  )  ; 
Document   doc  =  docBuilder  .  parse  (  is  ,  (  new   File  (  Constants  .  MACHINE_DTD_FILE  )  )  .  toURI  (  )  .  toString  (  )  )  ; 
Element   root  =  doc  .  getDocumentElement  (  )  ; 
Node   child  =  root  .  getFirstChild  (  )  ; 
while  (  child  !=  null  )  { 
if  (  child  .  getNodeName  (  )  .  equals  (  "description"  )  )  { 
return   child  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
} 
child  =  child  .  getNextSibling  (  )  ; 
} 
return   null  ; 
} 
} 

