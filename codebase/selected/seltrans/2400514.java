package   prajna  .  semantic  .  writer  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  *  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilder  ; 
import   javax  .  xml  .  parsers  .  DocumentBuilderFactory  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Text  ; 
import   prajna  .  data  .  *  ; 
import   prajna  .  util  .  Graph  ; 
import   prajna  .  util  .  Grid  ; 
import   prajna  .  util  .  Tree  ; 






public   class   SolrUpdater   implements   SemanticWriter  { 

private   static   DocumentBuilder   docBuild  ; 

private   static   Transformer   transformer  ; 

private   String   baseUrl  ; 

private   Document   doc  ; 

private   int   writeLimit  =  1000  ; 

private   boolean   debug  =  false  ; 

private   SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"  )  ; 

private   Writer   outWriter  ; 

static  { 
try  { 
docBuild  =  DocumentBuilderFactory  .  newInstance  (  )  .  newDocumentBuilder  (  )  ; 
transformer  =  TransformerFactory  .  newInstance  (  )  .  newTransformer  (  )  ; 
transformer  .  setOutputProperty  (  "omit-xml-declaration"  ,  "yes"  )  ; 
}  catch  (  Exception   exc  )  { 
System  .  err  .  println  (  "Cannot initialize SolrUpdater:"  )  ; 
exc  .  printStackTrace  (  )  ; 
} 
} 






public   SolrUpdater  (  String   solrUrl  )  { 
baseUrl  =  solrUrl  ; 
} 










private   void   addFieldToElement  (  Element   recElem  ,  String   field  ,  Set  <  ?  >  vals  )  { 
if  (  !  field  .  startsWith  (  "__"  )  )  { 
for  (  Object   val  :  vals  )  { 
Element   fieldElem  =  doc  .  createElement  (  "field"  )  ; 
fieldElem  .  setAttribute  (  "name"  ,  field  )  ; 
Text   txtNode  =  doc  .  createTextNode  (  val  .  toString  (  )  )  ; 
fieldElem  .  appendChild  (  txtNode  )  ; 
recElem  .  appendChild  (  fieldElem  )  ; 
} 
} 
} 










private   void   addTimeToElement  (  Element   recElem  ,  String   field  ,  Set  <  TimeSpan  >  vals  )  { 
if  (  !  field  .  startsWith  (  "__"  )  )  { 
for  (  TimeSpan   val  :  vals  )  { 
Element   fieldElem  =  doc  .  createElement  (  "field"  )  ; 
fieldElem  .  setAttribute  (  "name"  ,  field  )  ; 
Text   txtNode  =  doc  .  createTextNode  (  format  .  format  (  val  .  getStartDate  (  )  )  )  ; 
fieldElem  .  appendChild  (  txtNode  )  ; 
recElem  .  appendChild  (  fieldElem  )  ; 
} 
} 
} 




public   void   clear  (  )  { 
} 





public   void   close  (  )  { 
try  { 
writeCommand  (  "<commit/>"  )  ; 
writeCommand  (  "<optimize/>"  )  ; 
if  (  outWriter  !=  null  )  { 
outWriter  .  flush  (  )  ; 
outWriter  .  close  (  )  ; 
} 
}  catch  (  IOException   exc  )  { 
exc  .  printStackTrace  (  )  ; 
} 
} 









public   String   getContentType  (  )  { 
return  "text/xml"  ; 
} 







private   void   postData  (  )  throws   IOException  { 
try  { 
URL   url  =  new   URL  (  baseUrl  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setRequestMethod  (  "POST"  )  ; 
conn  .  setDoOutput  (  true  )  ; 
conn  .  setDoInput  (  true  )  ; 
conn  .  setUseCaches  (  false  )  ; 
conn  .  setAllowUserInteraction  (  false  )  ; 
conn  .  setRequestProperty  (  "Content-type"  ,  "text/xml; charset=UTF-8"  )  ; 
OutputStreamWriter   solrWriter  =  new   OutputStreamWriter  (  conn  .  getOutputStream  (  )  )  ; 
transformer  .  transform  (  new   DOMSource  (  doc  )  ,  new   StreamResult  (  solrWriter  )  )  ; 
solrWriter  .  flush  (  )  ; 
solrWriter  .  close  (  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  =  null  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  line  )  ; 
} 
if  (  outWriter  !=  null  )  { 
outWriter  .  write  (  line  )  ; 
outWriter  .  write  (  '\n'  )  ; 
} 
} 
rd  .  close  (  )  ; 
}  catch  (  TransformerException   exc  )  { 
exc  .  printStackTrace  (  )  ; 
} 
} 








private   void   sendData  (  Collection  <  DataRecord  >  records  ,  boolean   isUpdate  )  throws   IOException  { 
if  (  debug  )  { 
System  .  out  .  println  (  "About to start update: "  +  records  .  size  (  )  +  " records at "  +  new   Date  (  )  )  ; 
} 
doc  =  docBuild  .  newDocument  (  )  ; 
Element   addElem  =  isUpdate  ?  doc  .  createElement  (  "update"  )  :  doc  .  createElement  (  "add"  )  ; 
doc  .  appendChild  (  addElem  )  ; 
int   cnt  =  0  ; 
for  (  DataRecord   rec  :  records  )  { 
Element   recElem  =  doc  .  createElement  (  "doc"  )  ; 
addElem  .  appendChild  (  recElem  )  ; 
Set  <  String  >  fields  =  rec  .  getEnumFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  String  >  vals  =  rec  .  getEnumValues  (  field  )  ; 
addFieldToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
fields  =  rec  .  getIntFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  Integer  >  vals  =  rec  .  getIntValues  (  field  )  ; 
addFieldToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
fields  =  rec  .  getLocationFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  Location  >  vals  =  rec  .  getLocationValues  (  field  )  ; 
addFieldToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
fields  =  rec  .  getMeasureFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  Measure  >  vals  =  rec  .  getMeasureValues  (  field  )  ; 
addFieldToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
fields  =  rec  .  getTextFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  String  >  vals  =  rec  .  getTextValues  (  field  )  ; 
addFieldToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
fields  =  rec  .  getTimeFieldNames  (  )  ; 
for  (  String   field  :  fields  )  { 
Set  <  TimeSpan  >  vals  =  rec  .  getTimeValues  (  field  )  ; 
addTimeToElement  (  recElem  ,  field  ,  vals  )  ; 
} 
cnt  ++  ; 
if  (  writeLimit  >  0  &&  cnt  <=  writeLimit  )  { 
postData  (  )  ; 
doc  =  docBuild  .  newDocument  (  )  ; 
addElem  =  doc  .  createElement  (  "add"  )  ; 
doc  .  appendChild  (  addElem  )  ; 
cnt  =  0  ; 
} 
} 
} 






public   void   setDebug  (  boolean   flag  )  { 
debug  =  flag  ; 
} 







public   void   setDestination  (  String   destination  )  { 
baseUrl  =  destination  ; 
} 






public   void   setTimeZone  (  TimeZone   zone  )  { 
format  .  setTimeZone  (  zone  )  ; 
} 







public   void   setWriteLimit  (  int   limit  )  { 
writeLimit  =  limit  ; 
} 






public   void   setWriter  (  Writer   writer  )  { 
outWriter  =  writer  ; 
} 








public   void   updateData  (  Collection  <  DataRecord  >  records  )  throws   IOException  { 
sendData  (  records  ,  true  )  ; 
} 








public   void   writeCommand  (  String   cmd  )  throws   IOException  { 
if  (  debug  )  { 
System  .  out  .  println  (  "About to write command: "  +  cmd  +  " to "  +  baseUrl  )  ; 
} 
URL   url  =  new   URL  (  baseUrl  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setDoOutput  (  true  )  ; 
conn  .  setDoInput  (  true  )  ; 
conn  .  setUseCaches  (  false  )  ; 
conn  .  setAllowUserInteraction  (  false  )  ; 
conn  .  setRequestProperty  (  "Content-type"  ,  "text/xml; charset=UTF-8"  )  ; 
OutputStreamWriter   solrWriter  =  new   OutputStreamWriter  (  conn  .  getOutputStream  (  )  )  ; 
solrWriter  .  write  (  cmd  ,  0  ,  cmd  .  length  (  )  )  ; 
solrWriter  .  flush  (  )  ; 
solrWriter  .  close  (  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  =  null  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  line  )  ; 
} 
if  (  outWriter  !=  null  )  { 
outWriter  .  write  (  line  )  ; 
outWriter  .  write  (  '\n'  )  ; 
} 
} 
rd  .  close  (  )  ; 
} 








public   void   writeData  (  Collection  <  DataRecord  >  records  )  throws   IOException  { 
sendData  (  records  ,  false  )  ; 
} 







public   void   writeGraph  (  Graph  <  DataRecord  ,  DataRecord  >  graph  )  { 
try  { 
writeData  (  graph  .  getNodeSet  (  )  )  ; 
writeData  (  graph  .  getEdgeSet  (  )  )  ; 
}  catch  (  IOException   exc  )  { 
throw   new   RuntimeException  (  "Cannot write graph data: "  ,  exc  )  ; 
} 
} 






public   void   writeGrid  (  Grid  <  DataRecord  >  grid  )  { 
try  { 
writeData  (  grid  .  getAllValues  (  )  )  ; 
}  catch  (  IOException   exc  )  { 
throw   new   RuntimeException  (  "Cannot write grid data: "  ,  exc  )  ; 
} 
} 






public   void   writeTree  (  Tree  <  DataRecord  >  tree  )  { 
HashSet  <  DataRecord  >  recs  =  new   HashSet  <  DataRecord  >  (  )  ; 
for  (  Iterator  <  DataRecord  >  iter  =  tree  .  breadthFirstIterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
DataRecord   rec  =  iter  .  next  (  )  ; 
recs  .  add  (  rec  )  ; 
} 
try  { 
writeData  (  recs  )  ; 
}  catch  (  IOException   exc  )  { 
throw   new   RuntimeException  (  "Cannot write tree data: "  ,  exc  )  ; 
} 
} 










public   static   void   main  (  String  [  ]  args  )  throws   IOException  { 
if  (  args  .  length  >  0  )  { 
SolrUpdater   updater  =  new   SolrUpdater  (  args  [  0  ]  )  ; 
updater  .  setDebug  (  true  )  ; 
if  (  args  .  length  >  1  )  { 
System  .  out  .  println  (  "Running "  +  args  [  1  ]  )  ; 
updater  .  writeCommand  (  args  [  1  ]  )  ; 
}  else  { 
updater  .  close  (  )  ; 
} 
} 
} 
} 

