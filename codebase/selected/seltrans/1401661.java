package   net  .  sf  .  saxon  ; 

import   net  .  sf  .  saxon  .  event  .  *  ; 
import   net  .  sf  .  saxon  .  expr  .  XPathContext  ; 
import   net  .  sf  .  saxon  .  expr  .  XPathContextMajor  ; 
import   net  .  sf  .  saxon  .  expr  .  PathMap  ; 
import   net  .  sf  .  saxon  .  functions  .  Component  ; 
import   net  .  sf  .  saxon  .  functions  .  EscapeURI  ; 
import   net  .  sf  .  saxon  .  instruct  .  *  ; 
import   net  .  sf  .  saxon  .  om  .  *  ; 
import   net  .  sf  .  saxon  .  sort  .  IntHashMap  ; 
import   net  .  sf  .  saxon  .  tinytree  .  TinyBuilder  ; 
import   net  .  sf  .  saxon  .  trace  .  *  ; 
import   net  .  sf  .  saxon  .  trans  .  *  ; 
import   net  .  sf  .  saxon  .  tree  .  TreeBuilder  ; 
import   net  .  sf  .  saxon  .  value  .  DateTimeValue  ; 
import   net  .  sf  .  saxon  .  type  .  SchemaURIResolver  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   javax  .  xml  .  transform  .  *  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
































public   class   Controller   extends   Transformer  { 

private   Configuration   config  ; 

private   Item   initialContextItem  ; 

private   Item   contextForGlobalVariables  ; 

private   Bindery   bindery  ; 

private   NamePool   namePool  ; 

private   Receiver   messageEmitter  ; 

private   RuleManager   ruleManager  ; 

private   Properties   localOutputProperties  ; 

private   GlobalParameterSet   parameters  ; 

private   PreparedStylesheet   preparedStylesheet  ; 

private   TraceListener   traceListener  ; 

private   boolean   tracingPaused  ; 

private   PrintStream   traceFunctionDestination  =  System  .  err  ; 

private   URIResolver   standardURIResolver  ; 

private   URIResolver   userURIResolver  ; 

private   Result   principalResult  ; 

private   String   principalResultURI  ; 

private   String   cookedPrincipalResultURI  ; 

private   boolean   thereHasBeenAnExplicitResultDocument  ; 

private   OutputURIResolver   outputURIResolver  ; 

private   UnparsedTextURIResolver   unparsedTextResolver  ; 

private   SchemaURIResolver   schemaURIResolver  ; 

private   ErrorListener   errorListener  ; 

private   int   recoveryPolicy  ; 

private   Executable   executable  ; 

private   int   treeModel  =  Builder  .  TINY_TREE  ; 

private   Template   initialTemplate  =  null  ; 

private   HashSet   allOutputDestinations  ; 

private   DocumentPool   sourceDocumentPool  ; 

private   SequenceOutputter   reusableSequenceOutputter  =  null  ; 

private   HashMap   userDataTable  ; 

private   DateTimeValue   currentDateTime  ; 

private   boolean   dateTimePreset  =  false  ; 

private   StructuredQName   initialMode  =  null  ; 

private   NodeInfo   lastRememberedNode  =  null  ; 

private   int   lastRememberedNumber  =  -  1  ; 

private   ClassLoader   classLoader  ; 

private   PathMap   pathMap  =  null  ; 








public   Controller  (  Configuration   config  )  { 
this  .  config  =  config  ; 
executable  =  new   Executable  (  config  )  ; 
executable  .  setHostLanguage  (  config  .  getHostLanguage  (  )  )  ; 
sourceDocumentPool  =  new   DocumentPool  (  )  ; 
reset  (  )  ; 
} 







public   Controller  (  Configuration   config  ,  Executable   executable  )  { 
this  .  config  =  config  ; 
this  .  executable  =  executable  ; 
sourceDocumentPool  =  new   DocumentPool  (  )  ; 
reset  (  )  ; 
} 
























public   void   reset  (  )  { 
bindery  =  new   Bindery  (  )  ; 
namePool  =  config  .  getNamePool  (  )  ; 
standardURIResolver  =  config  .  getSystemURIResolver  (  )  ; 
userURIResolver  =  config  .  getURIResolver  (  )  ; 
outputURIResolver  =  config  .  getOutputURIResolver  (  )  ; 
schemaURIResolver  =  config  .  getSchemaURIResolver  (  )  ; 
unparsedTextResolver  =  new   StandardUnparsedTextResolver  (  )  ; 
errorListener  =  config  .  getErrorListener  (  )  ; 
recoveryPolicy  =  config  .  getRecoveryPolicy  (  )  ; 
if  (  errorListener   instanceof   StandardErrorListener  )  { 
PrintStream   ps  =  (  (  StandardErrorListener  )  errorListener  )  .  getErrorOutput  (  )  ; 
errorListener  =  (  (  StandardErrorListener  )  errorListener  )  .  makeAnother  (  executable  .  getHostLanguage  (  )  )  ; 
(  (  StandardErrorListener  )  errorListener  )  .  setErrorOutput  (  ps  )  ; 
(  (  StandardErrorListener  )  errorListener  )  .  setRecoveryPolicy  (  recoveryPolicy  )  ; 
} 
userDataTable  =  new   HashMap  (  20  )  ; 
traceListener  =  null  ; 
tracingPaused  =  false  ; 
traceFunctionDestination  =  System  .  err  ; 
TraceListener   tracer  ; 
try  { 
tracer  =  config  .  makeTraceListener  (  )  ; 
}  catch  (  XPathException   err  )  { 
throw   new   IllegalStateException  (  err  .  getMessage  (  )  )  ; 
} 
if  (  tracer  !=  null  )  { 
addTraceListener  (  tracer  )  ; 
} 
setTreeModel  (  config  .  getTreeModel  (  )  )  ; 
initialContextItem  =  null  ; 
contextForGlobalVariables  =  null  ; 
messageEmitter  =  null  ; 
localOutputProperties  =  null  ; 
parameters  =  null  ; 
principalResult  =  null  ; 
principalResultURI  =  null  ; 
initialTemplate  =  null  ; 
allOutputDestinations  =  null  ; 
thereHasBeenAnExplicitResultDocument  =  false  ; 
currentDateTime  =  null  ; 
dateTimePreset  =  false  ; 
initialMode  =  null  ; 
lastRememberedNode  =  null  ; 
lastRememberedNumber  =  -  1  ; 
classLoader  =  null  ; 
} 







public   Configuration   getConfiguration  (  )  { 
return   config  ; 
} 















public   void   setInitialMode  (  String   expandedModeName  )  { 
if  (  expandedModeName  ==  null  )  return  ; 
if  (  expandedModeName  .  length  (  )  ==  0  )  return  ; 
initialMode  =  StructuredQName  .  fromClarkName  (  expandedModeName  )  ; 
} 





public   String   getInitialMode  (  )  { 
return   initialMode  .  getClarkName  (  )  ; 
} 






















public   void   setOutputProperties  (  Properties   properties  )  { 
if  (  properties  ==  null  )  { 
localOutputProperties  =  null  ; 
}  else  { 
Enumeration   keys  =  properties  .  propertyNames  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
String   key  =  (  String  )  keys  .  nextElement  (  )  ; 
setOutputProperty  (  key  ,  properties  .  getProperty  (  key  )  )  ; 
} 
} 
} 

















public   Properties   getOutputProperties  (  )  { 
if  (  localOutputProperties  ==  null  )  { 
if  (  executable  ==  null  )  { 
return   new   Properties  (  )  ; 
}  else  { 
localOutputProperties  =  new   Properties  (  executable  .  getDefaultOutputProperties  (  )  )  ; 
} 
} 
Properties   newProps  =  new   Properties  (  )  ; 
Enumeration   keys  =  localOutputProperties  .  propertyNames  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
String   key  =  (  String  )  keys  .  nextElement  (  )  ; 
newProps  .  setProperty  (  key  ,  localOutputProperties  .  getProperty  (  key  )  )  ; 
} 
return   newProps  ; 
} 


















public   void   setOutputProperty  (  String   name  ,  String   value  )  { 
if  (  localOutputProperties  ==  null  )  { 
localOutputProperties  =  getOutputProperties  (  )  ; 
} 
try  { 
SaxonOutputKeys  .  checkOutputProperty  (  name  ,  value  ,  getConfiguration  (  )  .  getNameChecker  (  )  )  ; 
}  catch  (  XPathException   err  )  { 
throw   new   IllegalArgumentException  (  err  .  getMessage  (  )  )  ; 
} 
localOutputProperties  .  setProperty  (  name  ,  value  )  ; 
} 
















public   String   getOutputProperty  (  String   name  )  { 
try  { 
SaxonOutputKeys  .  checkOutputProperty  (  name  ,  null  ,  getConfiguration  (  )  .  getNameChecker  (  )  )  ; 
}  catch  (  XPathException   err  )  { 
throw   new   IllegalArgumentException  (  err  .  getMessage  (  )  )  ; 
} 
if  (  localOutputProperties  ==  null  )  { 
if  (  executable  ==  null  )  { 
return   null  ; 
}  else  { 
localOutputProperties  =  executable  .  getDefaultOutputProperties  (  )  ; 
} 
} 
return   localOutputProperties  .  getProperty  (  name  )  ; 
} 















public   void   setBaseOutputURI  (  String   uri  )  { 
principalResultURI  =  uri  ; 
} 















public   String   getBaseOutputURI  (  )  { 
return   principalResultURI  ; 
} 







public   String   getCookedBaseOutputURI  (  )  { 
if  (  cookedPrincipalResultURI  ==  null  )  { 
String   base  =  getBaseOutputURI  (  )  ; 
if  (  base  ==  null  &&  config  .  isAllowExternalFunctions  (  )  )  { 
base  =  new   File  (  System  .  getProperty  (  "user.dir"  )  )  .  toURI  (  )  .  toString  (  )  ; 
} 
if  (  base  !=  null  )  { 
base  =  EscapeURI  .  iriToUri  (  base  )  .  toString  (  )  ; 
} 
cookedPrincipalResultURI  =  base  ; 
} 
return   cookedPrincipalResultURI  ; 
} 







public   Result   getPrincipalResult  (  )  { 
return   principalResult  ; 
} 









public   boolean   checkUniqueOutputDestination  (  String   uri  )  { 
if  (  uri  ==  null  )  { 
return   true  ; 
} 
if  (  allOutputDestinations  ==  null  )  { 
allOutputDestinations  =  new   HashSet  (  20  )  ; 
} 
if  (  uri  .  startsWith  (  "file:///"  )  )  { 
uri  =  "file:/"  +  uri  .  substring  (  8  )  ; 
} 
return  !  allOutputDestinations  .  contains  (  uri  )  ; 
} 






public   void   addUnavailableOutputDestination  (  String   uri  )  { 
if  (  allOutputDestinations  ==  null  )  { 
allOutputDestinations  =  new   HashSet  (  20  )  ; 
} 
allOutputDestinations  .  add  (  uri  )  ; 
} 






public   void   removeUnavailableOutputDestination  (  String   uri  )  { 
if  (  allOutputDestinations  !=  null  )  { 
allOutputDestinations  .  remove  (  uri  )  ; 
} 
} 










public   boolean   isUnusedOutputDestination  (  String   uri  )  { 
return   allOutputDestinations  ==  null  ||  !  allOutputDestinations  .  contains  (  uri  )  ; 
} 





public   void   checkImplicitResultTree  (  )  throws   XPathException  { 
if  (  !  checkUniqueOutputDestination  (  principalResultURI  )  )  { 
XPathException   err  =  new   XPathException  (  "Cannot write an implicit result document if an explicit result document has been written to the same URI: "  +  principalResultURI  )  ; 
err  .  setErrorCode  (  "XTDE1490"  )  ; 
throw   err  ; 
} 
} 




public   void   setThereHasBeenAnExplicitResultDocument  (  )  { 
thereHasBeenAnExplicitResultDocument  =  true  ; 
} 





public   boolean   hasThereBeenAnExplicitResultDocument  (  )  { 
return   thereHasBeenAnExplicitResultDocument  ; 
} 








public   SequenceOutputter   allocateSequenceOutputter  (  int   size  )  { 
if  (  reusableSequenceOutputter  !=  null  )  { 
SequenceOutputter   out  =  reusableSequenceOutputter  ; 
reusableSequenceOutputter  =  null  ; 
return   out  ; 
}  else  { 
return   new   SequenceOutputter  (  this  ,  size  )  ; 
} 
} 





public   void   reuseSequenceOutputter  (  SequenceOutputter   out  )  { 
reusableSequenceOutputter  =  out  ; 
} 

















public   void   setInitialTemplate  (  String   expandedName  )  throws   XPathException  { 
if  (  expandedName  ==  null  )  { 
initialTemplate  =  null  ; 
return  ; 
} 
StructuredQName   qName  =  StructuredQName  .  fromClarkName  (  expandedName  )  ; 
Template   t  =  getExecutable  (  )  .  getNamedTemplate  (  qName  )  ; 
if  (  t  ==  null  )  { 
XPathException   err  =  new   XPathException  (  "There is no named template with expanded name "  +  expandedName  )  ; 
err  .  setErrorCode  (  "XTDE0040"  )  ; 
reportFatalError  (  err  )  ; 
throw   err  ; 
}  else   if  (  t  .  hasRequiredParams  (  )  )  { 
XPathException   err  =  new   XPathException  (  "The named template "  +  expandedName  +  " has required parameters, so cannot be used as the entry point"  )  ; 
err  .  setErrorCode  (  "XTDE0060"  )  ; 
reportFatalError  (  err  )  ; 
throw   err  ; 
}  else  { 
initialTemplate  =  t  ; 
} 
} 






public   String   getInitialTemplate  (  )  { 
if  (  initialTemplate  ==  null  )  { 
return   null  ; 
}  else  { 
return   initialTemplate  .  getTemplateName  (  )  .  getClarkName  (  )  ; 
} 
} 









public   PipelineConfiguration   makePipelineConfiguration  (  )  { 
PipelineConfiguration   pipe  =  new   PipelineConfiguration  (  )  ; 
pipe  .  setConfiguration  (  getConfiguration  (  )  )  ; 
pipe  .  setErrorListener  (  getErrorListener  (  )  )  ; 
pipe  .  setURIResolver  (  userURIResolver  ==  null  ?  standardURIResolver  :  userURIResolver  )  ; 
pipe  .  setSchemaURIResolver  (  schemaURIResolver  )  ; 
pipe  .  setExpandAttributeDefaults  (  getConfiguration  (  )  .  isExpandAttributeDefaults  (  )  )  ; 
pipe  .  setUseXsiSchemaLocation  (  (  (  Boolean  )  getConfiguration  (  )  .  getConfigurationProperty  (  FeatureKeys  .  USE_XSI_SCHEMA_LOCATION  )  )  .  booleanValue  (  )  )  ; 
pipe  .  setController  (  this  )  ; 
final   Executable   executable  =  getExecutable  (  )  ; 
if  (  executable  !=  null  )  { 
pipe  .  setLocationProvider  (  executable  .  getLocationMap  (  )  )  ; 
pipe  .  setHostLanguage  (  executable  .  getHostLanguage  (  )  )  ; 
} 
return   pipe  ; 
} 











private   Receiver   makeMessageEmitter  (  )  throws   XPathException  { 
String   emitterClass  =  config  .  getMessageEmitterClass  (  )  ; 
Object   messageReceiver  =  config  .  getInstance  (  emitterClass  ,  getClassLoader  (  )  )  ; 
if  (  !  (  messageReceiver   instanceof   Receiver  )  )  { 
throw   new   XPathException  (  emitterClass  +  " is not a Receiver"  )  ; 
} 
setMessageEmitter  (  (  Receiver  )  messageReceiver  )  ; 
return  (  Receiver  )  messageReceiver  ; 
} 




































public   void   setMessageEmitter  (  Receiver   receiver  )  { 
messageEmitter  =  receiver  ; 
if  (  receiver  .  getPipelineConfiguration  (  )  ==  null  )  { 
messageEmitter  .  setPipelineConfiguration  (  makePipelineConfiguration  (  )  )  ; 
} 
if  (  messageEmitter   instanceof   Emitter  &&  (  (  Emitter  )  messageEmitter  )  .  getOutputProperties  (  )  ==  null  )  { 
try  { 
Properties   props  =  new   Properties  (  )  ; 
props  .  setProperty  (  OutputKeys  .  METHOD  ,  "xml"  )  ; 
props  .  setProperty  (  OutputKeys  .  INDENT  ,  "yes"  )  ; 
props  .  setProperty  (  OutputKeys  .  OMIT_XML_DECLARATION  ,  "yes"  )  ; 
(  (  Emitter  )  messageEmitter  )  .  setOutputProperties  (  props  )  ; 
}  catch  (  XPathException   e  )  { 
} 
} 
} 









public   Receiver   getMessageEmitter  (  )  { 
return   messageEmitter  ; 
} 
















public   CharacterMapExpander   makeCharacterMapExpander  (  String   useMaps  ,  SerializerFactory   sf  )  throws   XPathException  { 
CharacterMapExpander   characterMapExpander  =  null  ; 
HashMap   characterMapIndex  =  getExecutable  (  )  .  getCharacterMapIndex  (  )  ; 
if  (  useMaps  !=  null  &&  characterMapIndex  !=  null  )  { 
List   characterMaps  =  new   ArrayList  (  5  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  useMaps  ,  " \t\n\r"  ,  false  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   expandedName  =  st  .  nextToken  (  )  ; 
StructuredQName   qName  =  StructuredQName  .  fromClarkName  (  expandedName  )  ; 
IntHashMap   map  =  (  IntHashMap  )  characterMapIndex  .  get  (  qName  )  ; 
if  (  map  ==  null  )  { 
throw   new   XPathException  (  "Character map '"  +  expandedName  +  "' has not been defined"  )  ; 
} 
characterMaps  .  add  (  map  )  ; 
} 
if  (  !  characterMaps  .  isEmpty  (  )  )  { 
characterMapExpander  =  sf  .  newCharacterMapExpander  (  )  ; 
characterMapExpander  .  setCharacterMaps  (  characterMaps  )  ; 
} 
} 
return   characterMapExpander  ; 
} 







public   void   setRecoveryPolicy  (  int   policy  )  { 
recoveryPolicy  =  policy  ; 
if  (  errorListener   instanceof   StandardErrorListener  )  { 
(  (  StandardErrorListener  )  errorListener  )  .  setRecoveryPolicy  (  policy  )  ; 
} 
} 








public   int   getRecoveryPolicy  (  )  { 
return   recoveryPolicy  ; 
} 






public   void   setErrorListener  (  ErrorListener   listener  )  { 
errorListener  =  listener  ; 
} 






public   ErrorListener   getErrorListener  (  )  { 
return   errorListener  ; 
} 












public   void   recoverableError  (  XPathException   err  )  throws   XPathException  { 
try  { 
if  (  executable  .  getHostLanguage  (  )  ==  Configuration  .  XQUERY  )  { 
reportFatalError  (  err  )  ; 
throw   err  ; 
}  else  { 
errorListener  .  error  (  err  )  ; 
} 
}  catch  (  TransformerException   e  )  { 
XPathException   de  =  XPathException  .  makeXPathException  (  e  )  ; 
de  .  setHasBeenReported  (  )  ; 
throw   de  ; 
} 
} 





public   void   reportFatalError  (  XPathException   err  )  { 
if  (  !  err  .  hasBeenReported  (  )  )  { 
try  { 
getErrorListener  (  )  .  fatalError  (  err  )  ; 
}  catch  (  TransformerException   e  )  { 
} 
err  .  setHasBeenReported  (  )  ; 
} 
} 








public   Executable   getExecutable  (  )  { 
return   executable  ; 
} 








public   DocumentPool   getDocumentPool  (  )  { 
return   sourceDocumentPool  ; 
} 







public   void   clearDocumentPool  (  )  { 
sourceDocumentPool  =  new   DocumentPool  (  )  ; 
} 












public   void   setPrincipalSourceDocument  (  DocumentInfo   doc  )  { 
initialContextItem  =  doc  ; 
} 
























public   void   setInitialContextItem  (  Item   item  )  { 
initialContextItem  =  item  ; 
contextForGlobalVariables  =  item  ; 
} 








public   Bindery   getBindery  (  )  { 
return   bindery  ; 
} 









public   Item   getInitialContextItem  (  )  { 
return   initialContextItem  ; 
} 








public   Item   getContextForGlobalVariables  (  )  { 
return   contextForGlobalVariables  ; 
} 








public   void   setURIResolver  (  URIResolver   resolver  )  { 
userURIResolver  =  resolver  ; 
if  (  resolver   instanceof   StandardURIResolver  )  { 
(  (  StandardURIResolver  )  resolver  )  .  setConfiguration  (  getConfiguration  (  )  )  ; 
} 
} 










public   URIResolver   getURIResolver  (  )  { 
return   userURIResolver  ; 
} 









public   URIResolver   getStandardURIResolver  (  )  { 
return   standardURIResolver  ; 
} 
















public   void   setOutputURIResolver  (  OutputURIResolver   resolver  )  { 
if  (  resolver  ==  null  )  { 
outputURIResolver  =  config  .  getOutputURIResolver  (  )  ; 
}  else  { 
outputURIResolver  =  resolver  ; 
} 
} 









public   OutputURIResolver   getOutputURIResolver  (  )  { 
return   outputURIResolver  ; 
} 








public   void   setUnparsedTextURIResolver  (  UnparsedTextURIResolver   resolver  )  { 
unparsedTextResolver  =  resolver  ; 
} 








public   UnparsedTextURIResolver   getUnparsedTextURIResolver  (  )  { 
return   unparsedTextResolver  ; 
} 







public   void   setSchemaURIResolver  (  SchemaURIResolver   resolver  )  { 
schemaURIResolver  =  resolver  ; 
} 







public   SchemaURIResolver   getSchemaURIResolver  (  )  { 
return   schemaURIResolver  ; 
} 








public   KeyManager   getKeyManager  (  )  { 
return   executable  .  getKeyManager  (  )  ; 
} 










public   NamePool   getNamePool  (  )  { 
return   namePool  ; 
} 











public   void   setTreeModel  (  int   model  )  { 
treeModel  =  model  ; 
} 











public   int   getTreeModel  (  )  { 
return   treeModel  ; 
} 







public   Builder   makeBuilder  (  )  { 
Builder   b  ; 
if  (  treeModel  ==  Builder  .  TINY_TREE  )  { 
b  =  new   TinyBuilder  (  )  ; 
}  else  { 
b  =  new   TreeBuilder  (  )  ; 
} 
b  .  setTiming  (  config  .  isTiming  (  )  )  ; 
b  .  setLineNumbering  (  config  .  isLineNumbering  (  )  )  ; 
b  .  setPipelineConfiguration  (  makePipelineConfiguration  (  )  )  ; 
return   b  ; 
} 



















public   Stripper   makeStripper  (  Receiver   b  )  { 
if  (  config  .  isStripsAllWhiteSpace  (  )  )  { 
if  (  b  ==  null  )  { 
return   AllElementStripper  .  getInstance  (  )  ; 
}  else  { 
Stripper   s  =  new   AllElementStripper  (  )  ; 
s  .  setUnderlyingReceiver  (  b  )  ; 
s  .  setPipelineConfiguration  (  b  .  getPipelineConfiguration  (  )  )  ; 
return   s  ; 
} 
} 
Stripper   stripper  ; 
if  (  executable  ==  null  )  { 
stripper  =  new   Stripper  (  new   Mode  (  Mode  .  STRIPPER_MODE  ,  Mode  .  DEFAULT_MODE_NAME  )  )  ; 
}  else  { 
stripper  =  executable  .  newStripper  (  )  ; 
} 
stripper  .  setXPathContext  (  newXPathContext  (  )  )  ; 
if  (  b  ==  null  )  { 
stripper  .  setPipelineConfiguration  (  makePipelineConfiguration  (  )  )  ; 
}  else  { 
stripper  .  setPipelineConfiguration  (  b  .  getPipelineConfiguration  (  )  )  ; 
stripper  .  setUnderlyingReceiver  (  b  )  ; 
} 
return   stripper  ; 
} 









public   void   registerDocument  (  DocumentInfo   doc  ,  String   systemId  )  { 
sourceDocumentPool  .  add  (  doc  ,  systemId  )  ; 
} 








public   void   setRuleManager  (  RuleManager   r  )  { 
ruleManager  =  r  ; 
} 









public   RuleManager   getRuleManager  (  )  { 
return   ruleManager  ; 
} 











public   TraceListener   getTraceListener  (  )  { 
return   traceListener  ; 
} 










public   final   boolean   isTracing  (  )  { 
return   traceListener  !=  null  &&  !  tracingPaused  ; 
} 








public   final   void   pauseTracing  (  boolean   pause  )  { 
tracingPaused  =  pause  ; 
} 















public   void   addTraceListener  (  TraceListener   trace  )  { 
if  (  trace  !=  null  )  { 
traceListener  =  TraceEventMulticaster  .  add  (  traceListener  ,  trace  )  ; 
} 
} 








public   void   removeTraceListener  (  TraceListener   trace  )  { 
traceListener  =  TraceEventMulticaster  .  remove  (  traceListener  ,  trace  )  ; 
} 










public   void   setTraceFunctionDestination  (  PrintStream   stream  )  { 
traceFunctionDestination  =  stream  ; 
} 








public   PrintStream   getTraceFunctionDestination  (  )  { 
return   traceFunctionDestination  ; 
} 








public   void   setPreparedStylesheet  (  PreparedStylesheet   sheet  )  { 
preparedStylesheet  =  sheet  ; 
executable  =  sheet  .  getExecutable  (  )  ; 
} 









public   void   setExecutable  (  Executable   exec  )  { 
executable  =  exec  ; 
} 






public   void   initializeController  (  )  throws   XPathException  { 
setRuleManager  (  executable  .  getRuleManager  (  )  )  ; 
if  (  traceListener  !=  null  )  { 
traceListener  .  open  (  )  ; 
} 
bindery  =  new   Bindery  (  )  ; 
executable  .  initializeBindery  (  bindery  )  ; 
defineGlobalParameters  (  )  ; 
} 









public   void   defineGlobalParameters  (  )  throws   XPathException  { 
executable  .  checkAllRequiredParamsArePresent  (  parameters  )  ; 
bindery  .  defineGlobalParameters  (  parameters  )  ; 
} 






public   void   allocateGlobalVariables  (  int   numberOfVariables  )  { 
SlotManager   map  =  executable  .  getGlobalVariableMap  (  )  ; 
map  .  setNumberOfVariables  (  numberOfVariables  )  ; 
bindery  .  allocateGlobals  (  map  )  ; 
} 
















public   Object   getUserData  (  Object   key  ,  String   name  )  { 
String   keyValue  =  key  .  hashCode  (  )  +  " "  +  name  ; 
return   userDataTable  .  get  (  keyValue  )  ; 
} 

















public   void   setUserData  (  Object   key  ,  String   name  ,  Object   data  )  { 
String   keyVal  =  key  .  hashCode  (  )  +  " "  +  name  ; 
if  (  data  ==  null  )  { 
userDataTable  .  remove  (  keyVal  )  ; 
}  else  { 
userDataTable  .  put  (  keyVal  ,  data  )  ; 
} 
} 












public   void   transform  (  Source   source  ,  Result   result  )  throws   TransformerException  { 
if  (  preparedStylesheet  ==  null  )  { 
throw   new   XPathException  (  "Stylesheet has not been prepared"  )  ; 
} 
if  (  !  dateTimePreset  )  { 
currentDateTime  =  null  ; 
} 
boolean   close  =  false  ; 
try  { 
NodeInfo   startNode  =  null  ; 
boolean   wrap  =  true  ; 
int   validationMode  =  config  .  getSchemaValidationMode  (  )  ; 
Source   underSource  =  source  ; 
if  (  source   instanceof   AugmentedSource  )  { 
Boolean   localWrap  =  (  (  AugmentedSource  )  source  )  .  getWrapDocument  (  )  ; 
if  (  localWrap  !=  null  )  { 
wrap  =  localWrap  .  booleanValue  (  )  ; 
} 
close  =  (  (  AugmentedSource  )  source  )  .  isPleaseCloseAfterUse  (  )  ; 
int   localValidate  =  (  (  AugmentedSource  )  source  )  .  getSchemaValidation  (  )  ; 
if  (  localValidate  !=  Validation  .  DEFAULT  )  { 
validationMode  =  localValidate  ; 
} 
if  (  validationMode  ==  Validation  .  STRICT  ||  validationMode  ==  Validation  .  LAX  )  { 
wrap  =  false  ; 
} 
underSource  =  (  (  AugmentedSource  )  source  )  .  getContainedSource  (  )  ; 
} 
Source   s2  =  config  .  getSourceResolver  (  )  .  resolveSource  (  underSource  ,  config  )  ; 
if  (  s2  !=  null  )  { 
underSource  =  s2  ; 
} 
if  (  wrap  &&  (  underSource   instanceof   NodeInfo  ||  underSource   instanceof   DOMSource  )  )  { 
startNode  =  prepareInputTree  (  underSource  )  ; 
registerDocument  (  startNode  .  getDocumentRoot  (  )  ,  underSource  .  getSystemId  (  )  )  ; 
}  else   if  (  source  ==  null  )  { 
if  (  initialTemplate  ==  null  )  { 
throw   new   XPathException  (  "Either a source document or an initial template must be specified"  )  ; 
} 
}  else  { 
Builder   sourceBuilder  =  makeBuilder  (  )  ; 
Sender   sender  =  new   Sender  (  sourceBuilder  .  getPipelineConfiguration  (  )  )  ; 
Receiver   r  =  sourceBuilder  ; 
if  (  config  .  isStripsAllWhiteSpace  (  )  ||  executable  .  stripsWhitespace  (  )  ||  validationMode  ==  Validation  .  STRICT  ||  validationMode  ==  Validation  .  LAX  )  { 
r  =  makeStripper  (  sourceBuilder  )  ; 
} 
if  (  executable  .  stripsInputTypeAnnotations  (  )  )  { 
r  =  config  .  getAnnotationStripper  (  r  )  ; 
} 
sender  .  send  (  source  ,  r  )  ; 
if  (  close  )  { 
(  (  AugmentedSource  )  source  )  .  close  (  )  ; 
} 
DocumentInfo   doc  =  (  DocumentInfo  )  sourceBuilder  .  getCurrentRoot  (  )  ; 
sourceBuilder  .  reset  (  )  ; 
registerDocument  (  doc  ,  source  .  getSystemId  (  )  )  ; 
startNode  =  doc  ; 
} 
transformDocument  (  startNode  ,  result  )  ; 
}  catch  (  TerminationException   err  )  { 
throw   err  ; 
}  catch  (  XPathException   err  )  { 
Throwable   cause  =  err  .  getException  (  )  ; 
if  (  cause  !=  null  &&  cause   instanceof   SAXParseException  )  { 
SAXParseException   spe  =  (  SAXParseException  )  cause  ; 
cause  =  spe  .  getException  (  )  ; 
if  (  cause   instanceof   RuntimeException  )  { 
reportFatalError  (  err  )  ; 
} 
}  else  { 
reportFatalError  (  err  )  ; 
} 
throw   err  ; 
}  finally  { 
if  (  close  )  { 
(  (  AugmentedSource  )  source  )  .  close  (  )  ; 
} 
principalResultURI  =  null  ; 
} 
} 













public   NodeInfo   prepareInputTree  (  Source   source  )  { 
NodeInfo   start  =  getConfiguration  (  )  .  unravel  (  source  )  ; 
if  (  executable  .  stripsWhitespace  (  )  )  { 
DocumentInfo   docInfo  =  start  .  getDocumentRoot  (  )  ; 
StrippedDocument   strippedDoc  =  new   StrippedDocument  (  docInfo  ,  makeStripper  (  null  )  )  ; 
start  =  strippedDoc  .  wrap  (  start  )  ; 
} 
return   start  ; 
} 










public   static   NodeInfo   unravel  (  Source   source  ,  Configuration   config  )  { 
return   config  .  unravel  (  source  )  ; 
} 















public   void   transformDocument  (  NodeInfo   startNode  ,  Result   result  )  throws   TransformerException  { 
if  (  executable  ==  null  )  { 
throw   new   XPathException  (  "Stylesheet has not been compiled"  )  ; 
} 
if  (  getMessageEmitter  (  )  ==  null  )  { 
Receiver   me  =  makeMessageEmitter  (  )  ; 
setMessageEmitter  (  me  )  ; 
if  (  me   instanceof   Emitter  &&  (  (  Emitter  )  me  )  .  getWriter  (  )  ==  null  )  { 
try  { 
(  (  Emitter  )  me  )  .  setWriter  (  new   OutputStreamWriter  (  System  .  err  )  )  ; 
}  catch  (  Exception   err  )  { 
try  { 
(  (  Emitter  )  me  )  .  setWriter  (  new   OutputStreamWriter  (  System  .  err  ,  "utf8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   XPathException  (  e  )  ; 
} 
} 
} 
} 
getMessageEmitter  (  )  .  open  (  )  ; 
boolean   mustClose  =  (  result   instanceof   StreamResult  &&  (  (  StreamResult  )  result  )  .  getOutputStream  (  )  ==  null  )  ; 
principalResult  =  result  ; 
if  (  principalResultURI  ==  null  )  { 
principalResultURI  =  result  .  getSystemId  (  )  ; 
} 
XPathContextMajor   initialContext  =  newXPathContext  (  )  ; 
initialContext  .  setOriginatingConstructType  (  Location  .  CONTROLLER  )  ; 
if  (  startNode  !=  null  )  { 
initialContextItem  =  startNode  ; 
contextForGlobalVariables  =  startNode  .  getRoot  (  )  ; 
if  (  startNode  .  getConfiguration  (  )  ==  null  )  { 
throw   new   TransformerException  (  "The supplied source document must be associated with a Configuration"  )  ; 
} 
if  (  !  startNode  .  getConfiguration  (  )  .  isCompatible  (  preparedStylesheet  .  getConfiguration  (  )  )  )  { 
throw   new   XPathException  (  "Source document and stylesheet must use the same or compatible Configurations"  ,  SaxonErrorCode  .  SXXP0004  )  ; 
} 
SequenceIterator   currentIter  =  SingletonIterator  .  makeIterator  (  startNode  )  ; 
if  (  initialTemplate  !=  null  )  { 
currentIter  .  next  (  )  ; 
} 
initialContext  .  setCurrentIterator  (  currentIter  )  ; 
} 
initializeController  (  )  ; 
if  (  traceListener  !=  null  )  { 
preEvaluateGlobals  (  initialContext  )  ; 
} 
Properties   xslOutputProps  ; 
if  (  localOutputProperties  ==  null  )  { 
xslOutputProps  =  executable  .  getDefaultOutputProperties  (  )  ; 
}  else  { 
xslOutputProps  =  localOutputProperties  ; 
} 
String   nextInChain  =  xslOutputProps  .  getProperty  (  SaxonOutputKeys  .  NEXT_IN_CHAIN  )  ; 
if  (  nextInChain  !=  null  )  { 
String   baseURI  =  xslOutputProps  .  getProperty  (  SaxonOutputKeys  .  NEXT_IN_CHAIN_BASE_URI  )  ; 
result  =  prepareNextStylesheet  (  nextInChain  ,  baseURI  ,  result  )  ; 
} 
Properties   props  =  new   Properties  (  xslOutputProps  )  ; 
props  .  setProperty  (  SaxonOutputKeys  .  IMPLICIT_RESULT_DOCUMENT  ,  "yes"  )  ; 
initialContext  .  changeOutputDestination  (  props  ,  result  ,  true  ,  Configuration  .  XSLT  ,  Validation  .  PRESERVE  ,  null  )  ; 
if  (  initialTemplate  ==  null  )  { 
initialContextItem  =  startNode  ; 
final   Mode   mode  =  getRuleManager  (  )  .  getMode  (  initialMode  ,  false  )  ; 
if  (  mode  ==  null  ||  (  initialMode  !=  null  &&  mode  .  isEmpty  (  )  )  )  { 
throw   new   XPathException  (  "Requested initial mode "  +  (  initialMode  ==  null  ?  ""  :  initialMode  .  getDisplayName  (  )  )  +  " does not exist"  ,  "XTDE0045"  )  ; 
} 
TailCall   tc  =  ApplyTemplates  .  applyTemplates  (  initialContext  .  getCurrentIterator  (  )  ,  mode  ,  null  ,  null  ,  initialContext  ,  false  ,  0  )  ; 
while  (  tc  !=  null  )  { 
tc  =  tc  .  processLeavingTail  (  )  ; 
} 
}  else  { 
Template   t  =  initialTemplate  ; 
XPathContextMajor   c2  =  initialContext  .  newContext  (  )  ; 
initialContext  .  setOriginatingConstructType  (  Location  .  CONTROLLER  )  ; 
c2  .  openStackFrame  (  t  .  getStackFrameMap  (  )  )  ; 
c2  .  setLocalParameters  (  new   ParameterSet  (  )  )  ; 
c2  .  setTunnelParameters  (  new   ParameterSet  (  )  )  ; 
TailCall   tc  =  t  .  expand  (  c2  )  ; 
while  (  tc  !=  null  )  { 
tc  =  tc  .  processLeavingTail  (  )  ; 
} 
} 
if  (  traceListener  !=  null  )  { 
traceListener  .  close  (  )  ; 
} 
Receiver   out  =  initialContext  .  getReceiver  (  )  ; 
if  (  out   instanceof   ComplexContentOutputter  &&  (  (  ComplexContentOutputter  )  out  )  .  contentHasBeenWritten  (  )  )  { 
if  (  principalResultURI  !=  null  )  { 
if  (  !  checkUniqueOutputDestination  (  principalResultURI  )  )  { 
XPathException   err  =  new   XPathException  (  "Cannot write more than one result document to the same URI, or write to a URI that has been read: "  +  result  .  getSystemId  (  )  )  ; 
err  .  setErrorCode  (  "XTDE1490"  )  ; 
throw   err  ; 
}  else  { 
addUnavailableOutputDestination  (  principalResultURI  )  ; 
} 
} 
} 
out  .  endDocument  (  )  ; 
out  .  close  (  )  ; 
getMessageEmitter  (  )  .  close  (  )  ; 
if  (  mustClose  &&  result   instanceof   StreamResult  )  { 
OutputStream   os  =  (  (  StreamResult  )  result  )  .  getOutputStream  (  )  ; 
if  (  os  !=  null  )  { 
try  { 
os  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   err  )  { 
throw   new   XPathException  (  err  )  ; 
} 
} 
} 
} 








public   void   preEvaluateGlobals  (  XPathContext   context  )  throws   XPathException  { 
HashMap   vars  =  getExecutable  (  )  .  getCompiledGlobalVariables  (  )  ; 
if  (  vars  !=  null  )  { 
Iterator   iter  =  vars  .  values  (  )  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
GlobalVariable   var  =  (  GlobalVariable  )  iter  .  next  (  )  ; 
var  .  evaluateVariable  (  context  )  ; 
} 
} 
} 














public   Result   prepareNextStylesheet  (  String   href  ,  String   baseURI  ,  Result   result  )  throws   TransformerException  { 
PreparedStylesheet   next  =  preparedStylesheet  .  getCachedStylesheet  (  href  ,  baseURI  )  ; 
if  (  next  ==  null  )  { 
Source   source  =  null  ; 
if  (  userURIResolver  !=  null  )  { 
source  =  userURIResolver  .  resolve  (  href  ,  baseURI  )  ; 
} 
if  (  source  ==  null  )  { 
source  =  standardURIResolver  .  resolve  (  href  ,  baseURI  )  ; 
} 
TransformerFactoryImpl   factory  =  new   TransformerFactoryImpl  (  )  ; 
factory  .  setConfiguration  (  config  )  ; 
next  =  (  PreparedStylesheet  )  factory  .  newTemplates  (  source  )  ; 
preparedStylesheet  .  putCachedStylesheet  (  href  ,  baseURI  ,  next  )  ; 
} 
TransformerReceiver   nextTransformer  =  new   TransformerReceiver  (  (  Controller  )  next  .  newTransformer  (  )  )  ; 
nextTransformer  .  setSystemId  (  principalResultURI  )  ; 
nextTransformer  .  setPipelineConfiguration  (  makePipelineConfiguration  (  )  )  ; 
nextTransformer  .  setResult  (  result  )  ; 
nextTransformer  .  open  (  )  ; 
return   nextTransformer  ; 
} 










































public   void   setParameter  (  String   expandedName  ,  Object   value  )  { 
if  (  parameters  ==  null  )  { 
parameters  =  new   GlobalParameterSet  (  )  ; 
} 
parameters  .  put  (  StructuredQName  .  fromClarkName  (  expandedName  )  ,  value  )  ; 
} 






public   void   setParameter  (  StructuredQName   qName  ,  ValueRepresentation   value  )  { 
if  (  parameters  ==  null  )  { 
parameters  =  new   GlobalParameterSet  (  )  ; 
} 
parameters  .  put  (  qName  ,  value  )  ; 
} 




public   void   clearParameters  (  )  { 
parameters  =  null  ; 
} 










public   Object   getParameter  (  String   expandedName  )  { 
if  (  parameters  ==  null  )  { 
return   null  ; 
} 
return   parameters  .  get  (  StructuredQName  .  fromClarkName  (  expandedName  )  )  ; 
} 





public   Iterator   iterateParameters  (  )  { 
if  (  parameters  ==  null  )  { 
return   Collections  .  EMPTY_LIST  .  iterator  (  )  ; 
} 
int   k  =  parameters  .  getNumberOfKeys  (  )  ; 
List   list  =  new   ArrayList  (  k  )  ; 
Collection   keys  =  parameters  .  getKeys  (  )  ; 
for  (  Iterator   it  =  keys  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
StructuredQName   qName  =  (  StructuredQName  )  it  .  next  (  )  ; 
String   clarkName  =  qName  .  getClarkName  (  )  ; 
list  .  add  (  clarkName  )  ; 
} 
return   list  .  iterator  (  )  ; 
} 














public   void   setCurrentDateTime  (  DateTimeValue   dateTime  )  throws   XPathException  { 
if  (  currentDateTime  ==  null  )  { 
if  (  dateTime  .  getComponent  (  Component  .  TIMEZONE  )  ==  null  )  { 
throw   new   XPathException  (  "No timezone is present in supplied value of current date/time"  )  ; 
} 
currentDateTime  =  dateTime  ; 
dateTimePreset  =  true  ; 
}  else  { 
throw   new   IllegalStateException  (  "Current date and time can only be set once, and cannot subsequently be changed"  )  ; 
} 
} 








public   DateTimeValue   getCurrentDateTime  (  )  { 
if  (  currentDateTime  ==  null  )  { 
currentDateTime  =  new   DateTimeValue  (  new   GregorianCalendar  (  )  ,  true  )  ; 
} 
return   currentDateTime  ; 
} 





public   int   getImplicitTimezone  (  )  { 
return   getCurrentDateTime  (  )  .  getTimezoneInMinutes  (  )  ; 
} 








public   XPathContextMajor   newXPathContext  (  )  { 
return   new   XPathContextMajor  (  this  )  ; 
} 









public   void   setRememberedNumber  (  NodeInfo   node  ,  int   number  )  { 
lastRememberedNode  =  node  ; 
lastRememberedNumber  =  number  ; 
} 









public   int   getRememberedNumber  (  NodeInfo   node  )  { 
if  (  lastRememberedNode  ==  node  )  { 
return   lastRememberedNumber  ; 
} 
return  -  1  ; 
} 






public   void   setUseDocumentProjection  (  PathMap   pathMap  )  { 
this  .  pathMap  =  pathMap  ; 
} 





public   PathMap   getPathMapForDocumentProjection  (  )  { 
return   pathMap  ; 
} 












public   void   setClassLoader  (  ClassLoader   loader  )  { 
classLoader  =  loader  ; 
} 









public   ClassLoader   getClassLoader  (  )  { 
return   classLoader  ; 
} 
} 

