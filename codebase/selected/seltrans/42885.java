package   ontorama  .  webkbtools  .  inputsource  ; 

import   ontorama  .  OntoramaConfig  ; 
import   ontorama  .  model  .  GraphNode  ; 
import   ontorama  .  webkbtools  .  inputsource  .  webkb  .  AmbiguousChoiceDialog  ; 
import   ontorama  .  webkbtools  .  inputsource  .  webkb  .  WebkbQueryStringConstructor  ; 
import   ontorama  .  webkbtools  .  query  .  Query  ; 
import   ontorama  .  webkbtools  .  query  .  parser  .  rdf  .  RdfWebkbParser  ; 
import   ontorama  .  webkbtools  .  util  .  CancelledQueryException  ; 
import   ontorama  .  webkbtools  .  util  .  NoSuchPropertyException  ; 
import   ontorama  .  webkbtools  .  util  .  ParserException  ; 
import   ontorama  .  webkbtools  .  util  .  SourceException  ; 
import   java  .  awt  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  security  .  AccessControlException  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  List  ; 

public   class   WebKB2Source   implements   Source  { 




private   Query   query  ; 




private   List   docs  =  new   LinkedList  (  )  ; 




private   List   typesList  =  new   LinkedList  (  )  ; 





private   String   readerString  =  ""  ; 




private   String   webkbErorrStartPattern  =  "<br><b>"  ; 

private   String   webkbErrorEndPattern  =  "</b><br>"  ; 








private   String   synPropName  =  "Synonym"  ; 






















public   SourceResult   getSourceResult  (  String   uri  ,  Query   query  )  throws   SourceException  ,  CancelledQueryException  { 
this  .  query  =  query  ; 
this  .  docs  =  new   LinkedList  (  )  ; 
this  .  typesList  =  new   LinkedList  (  )  ; 
this  .  readerString  =  ""  ; 
int   queryDepth  =  query  .  getDepth  (  )  ; 
Query   testQuery  =  query  ; 
testQuery  .  setDepth  (  1  )  ; 
String   fullUri  =  constructQueryUrl  (  uri  ,  testQuery  )  ; 
query  .  setDepth  (  queryDepth  )  ; 
Reader   resultReader  =  null  ; 
BufferedReader   br  =  null  ; 
try  { 
Reader   reader  =  executeWebkbQuery  (  fullUri  )  ; 
br  =  new   BufferedReader  (  reader  )  ; 
checkForMultiRdfDocuments  (  br  )  ; 
System  .  out  .  println  (  "docs size = "  +  docs  .  size  (  )  )  ; 
if  (  docs  .  size  (  )  ==  0  )  { 
String   webkbError  =  checkForWebkbErrors  (  readerString  )  ; 
throw   new   SourceException  (  "WebKB Error: "  +  webkbError  )  ; 
} 
if  (  resultIsAmbiguous  (  )  )  { 
Query   newQuery  =  processAmbiguousResultSet  (  )  ; 
return  (  new   SourceResult  (  false  ,  null  ,  newQuery  )  )  ; 
} 
reader  .  close  (  )  ; 
resultReader  =  executeWebkbQuery  (  constructQueryUrl  (  uri  ,  query  )  )  ; 
}  catch  (  IOException   ioExc  )  { 
throw   new   SourceException  (  "Couldn't read input data source for "  +  fullUri  +  ", error: "  +  ioExc  .  getMessage  (  )  )  ; 
}  catch  (  ParserException   parserExc  )  { 
throw   new   SourceException  (  "Error parsing returned RDF data, here is error provided by parser: "  +  parserExc  .  getMessage  (  )  )  ; 
}  catch  (  InterruptedException   intExc  )  { 
throw   new   CancelledQueryException  (  )  ; 
} 
System  .  out  .  println  (  "resultReader = "  +  resultReader  )  ; 
return  (  new   SourceResult  (  true  ,  resultReader  ,  null  )  )  ; 
} 




private   InputStreamReader   getInputStreamReader  (  String   uri  )  throws   MalformedURLException  ,  IOException  { 
URL   url  =  new   URL  (  uri  )  ; 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
return   new   InputStreamReader  (  connection  .  getInputStream  (  )  )  ; 
} 




private   String   constructQueryUrl  (  String   uri  ,  Query   query  )  { 
WebkbQueryStringConstructor   queryConstructor  =  new   WebkbQueryStringConstructor  (  )  ; 
String   resultUrl  =  uri  +  queryConstructor  .  getQueryString  (  query  ,  OntoramaConfig  .  queryOutputFormat  )  ; 
return   resultUrl  ; 
} 




private   Reader   executeWebkbQuery  (  String   fullUrl  )  throws   IOException  { 
if  (  OntoramaConfig  .  DEBUG  )  { 
System  .  out  .  println  (  "fullUrl = "  +  fullUrl  )  ; 
} 
System  .  out  .  println  (  "class WebKB2Source, fullUrl = "  +  fullUrl  )  ; 
InputStreamReader   reader  =  getInputStreamReader  (  fullUrl  )  ; 
return   reader  ; 
} 










private   String   checkForWebkbErrors  (  String   doc  )  { 
String   extractedErrorStr  =  doc  ; 
int   startPatternInd  =  doc  .  indexOf  (  webkbErorrStartPattern  )  ; 
int   endPatternInd  =  doc  .  indexOf  (  webkbErrorEndPattern  )  ; 
if  (  endPatternInd  !=  -  1  )  { 
extractedErrorStr  =  extractedErrorStr  .  substring  (  0  ,  endPatternInd  )  ; 
} 
if  (  startPatternInd  !=  -  1  )  { 
extractedErrorStr  =  extractedErrorStr  .  substring  (  webkbErorrStartPattern  .  length  (  )  )  ; 
} 
return   extractedErrorStr  ; 
} 












private   void   checkForMultiRdfDocuments  (  BufferedReader   br  )  throws   IOException  ,  InterruptedException  { 
String   token  ; 
String   buf  =  ""  ; 
String   line  =  br  .  readLine  (  )  ; 
StringTokenizer   st  ; 
while  (  line  !=  null  )  { 
System  .  out  .  print  (  "."  )  ; 
if  (  Thread  .  currentThread  (  )  .  isInterrupted  (  )  )  { 
throw   new   InterruptedException  (  "Query was cancelled"  )  ; 
} 
readerString  =  readerString  +  line  ; 
st  =  new   StringTokenizer  (  line  ,  "<"  ,  true  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
token  =  st  .  nextToken  (  )  ; 
buf  =  buf  +  token  ; 
if  (  token  .  equals  (  "/rdf:RDF>"  )  )  { 
docs  .  add  (  new   String  (  buf  )  )  ; 
buf  =  ""  ; 
} 
} 
buf  =  buf  +  "\n"  ; 
line  =  br  .  readLine  (  )  ; 
} 
} 






private   Query   processAmbiguousResultSet  (  )  throws   ParserException  { 
getRootTypesFromStreams  (  )  ; 
Frame  [  ]  frames  =  ontorama  .  view  .  OntoRamaApp  .  getFrames  (  )  ; 
String   selectedType  =  (  (  GraphNode  )  typesList  .  get  (  0  )  )  .  getName  (  )  ; 
if  (  frames  .  length  >  0  )  { 
AmbiguousChoiceDialog   dialog  =  new   AmbiguousChoiceDialog  (  typesList  ,  frames  [  0  ]  )  ; 
selectedType  =  dialog  .  getSelected  (  )  ; 
}  else  { 
} 
System  .  out  .  println  (  "\n\n\nselectedType = "  +  selectedType  )  ; 
String   newTermName  =  selectedType  ; 
Query   newQuery  =  new   Query  (  newTermName  ,  this  .  query  .  getRelationLinksList  (  )  )  ; 
newQuery  .  setDepth  (  this  .  query  .  getDepth  (  )  )  ; 
return   newQuery  ; 
} 










private   void   getRootTypesFromStreams  (  )  throws   ParserException  { 
Iterator   it  =  docs  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
String   nextDocStr  =  (  String  )  it  .  next  (  )  ; 
StringReader   curReader  =  new   StringReader  (  nextDocStr  )  ; 
List   curTypesList  =  getTypesListFromRdfStream  (  curReader  ,  query  .  getQueryTypeName  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  curTypesList  .  size  (  )  ;  i  ++  )  { 
GraphNode   node  =  (  GraphNode  )  curTypesList  .  get  (  i  )  ; 
if  (  !  typesList  .  contains  (  node  )  )  { 
typesList  .  add  (  node  )  ; 
} 
} 
} 
} 























private   List   getTypesListFromRdfStream  (  Reader   reader  ,  String   termName  )  throws   ParserException  ,  AccessControlException  { 
List   typeNamesList  =  new   LinkedList  (  )  ; 
RdfWebkbParser   parser  =  new   RdfWebkbParser  (  )  ; 
List   nodesList  =  parser  .  getResult  (  reader  )  .  getNodesList  (  )  ; 
Iterator   typesIt  =  nodesList  .  iterator  (  )  ; 
while  (  typesIt  .  hasNext  (  )  )  { 
GraphNode   curNode  =  (  GraphNode  )  typesIt  .  next  (  )  ; 
try  { 
List   synonyms  =  curNode  .  getProperty  (  synPropName  )  ; 
if  (  synonyms  .  contains  (  termName  )  )  { 
typeNamesList  .  add  (  curNode  )  ; 
} 
}  catch  (  NoSuchPropertyException   e  )  { 
System  .  out  .  println  (  "NoSuchPropertyException for property "  +  synPropName  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 
return   typeNamesList  ; 
} 




protected   boolean   resultIsAmbiguous  (  )  { 
if  (  docs  .  size  (  )  >  1  )  { 
System  .  out  .  println  (  "docs.size = "  +  docs  .  size  (  )  )  ; 
return   true  ; 
} 
return   false  ; 
} 




protected   int   getNumOfChoices  (  )  { 
return   typesList  .  size  (  )  ; 
} 




protected   List   getChoicesList  (  )  { 
return   typesList  ; 
} 
} 

