package   com  .  emental  .  mindraider  .  ui  .  rdf  ; 

import   java  .  awt  .  Color  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   com  .  emental  .  mindraider  .  MindRaiderConstants  ; 
import   com  .  emental  .  mindraider  .  core  .  MindRaider  ; 
import   com  .  emental  .  mindraider  .  core  .  desktop  .  Desktop  ; 
import   com  .  emental  .  mindraider  .  ui  .  graph  .  spiders  .  SpidersGraph  ; 
import   com  .  emental  .  mindraider  .  ui  .  panels  .  bars  .  StatusBar  ; 
import   com  .  emental  .  mindraider  .  utils  .  Utils  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Model  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  ModelFactory  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Property  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  RDFNode  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Resource  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Seq  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Statement  ; 
import   com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  StmtIterator  ; 













public   class   RdfModel  { 




public   static   final   int   GENERATED_MODEL_TYPE  =  1  ; 




public   static   final   int   FILE_MODEL_TYPE  =  2  ; 




public   static   final   int   URL_MODEL_TYPE  =  3  ; 




private   static   final   Logger   logger  =  Logger  .  getLogger  (  RdfModel  .  class  )  ; 




private   Model   model  ; 




private   String   filename  ; 




private   int   type  ; 









public   RdfModel  (  String   uri  )  throws   Exception  { 
this  .  filename  =  uri  ; 
if  (  uri  !=  null  )  { 
if  (  uri  .  startsWith  (  "http://"  )  )  { 
model  =  downloadModel  (  uri  )  ; 
type  =  URL_MODEL_TYPE  ; 
return  ; 
} 
if  (  uri  .  equals  (  MindRaiderConstants  .  MR_DESKTOP_MODEL  )  )  { 
model  =  new   Desktop  (  )  .  getDesktopModel  (  MindRaider  .  profile  )  ; 
type  =  GENERATED_MODEL_TYPE  ; 
return  ; 
} 
model  =  loadModel  (  uri  )  ; 
type  =  FILE_MODEL_TYPE  ; 
} 
} 









public   RdfModel  (  String   name  ,  int   type  )  { 
this  .  filename  =  name  ; 
this  .  type  =  type  ; 
switch  (  type  )  { 
case   GENERATED_MODEL_TYPE  : 
model  =  ModelFactory  .  createDefaultModel  (  )  ; 
break  ; 
case   FILE_MODEL_TYPE  : 
model  =  loadModel  (  name  )  ; 
break  ; 
case   URL_MODEL_TYPE  : 
model  =  downloadModel  (  name  )  ; 
break  ; 
default  : 
break  ; 
} 
} 









public   RdfModel  (  String   name  ,  Model   model  )  { 
this  .  filename  =  name  ; 
this  .  model  =  model  ; 
type  =  GENERATED_MODEL_TYPE  ; 
} 






public   Model   getModel  (  )  { 
return   model  ; 
} 






public   String   getModelName  (  )  { 
return   filename  ; 
} 






public   boolean   load  (  )  { 
if  (  (  model  =  loadModel  (  filename  )  )  !=  null  )  { 
return   true  ; 
} 
return   false  ; 
} 






public   boolean   save  (  )  { 
return   saveModel  (  model  ,  filename  )  ; 
} 








public   boolean   saveAs  (  String   filename  )  { 
this  .  filename  =  filename  ; 
return   saveModel  (  model  ,  filename  )  ; 
} 




public   void   show  (  )  { 
if  (  model  !=  null  )  { 
model  .  write  (  System  .  out  )  ; 
} 
} 









public   static   Model   loadModel  (  String   filename  )  { 
try  { 
Model   model  =  ModelFactory  .  createDefaultModel  (  )  ; 
File   file  =  new   File  (  filename  )  ; 
InputStreamReader   in  =  null  ; 
try  { 
if  (  file  !=  null  &&  file  .  exists  (  )  )  { 
in  =  new   InputStreamReader  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  ,  "UTF-8"  )  ; 
}  else  { 
System  .  err  .  println  (  "File: "  +  filename  +  " not found"  )  ; 
return   null  ; 
} 
model  .  read  (  in  ,  null  ,  "RDF/XML"  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
} 
return   model  ; 
}  catch  (  Exception   e  )  { 
logger  .  debug  (  "Unable to load model!"  ,  e  )  ; 
return   null  ; 
} 
} 








public   static   Model   downloadModel  (  String   url  )  { 
Model   model  =  ModelFactory  .  createDefaultModel  (  )  ; 
try  { 
URLConnection   connection  =  new   URL  (  url  )  .  openConnection  (  )  ; 
if  (  connection   instanceof   HttpURLConnection  )  { 
HttpURLConnection   httpConnection  =  (  HttpURLConnection  )  connection  ; 
httpConnection  .  setRequestProperty  (  "Accept"  ,  "application/rdf+xml, */*;q=.1"  )  ; 
httpConnection  .  setRequestProperty  (  "Accept-Language"  ,  "en"  )  ; 
} 
InputStream   in  =  connection  .  getInputStream  (  )  ; 
model  .  read  (  in  ,  url  )  ; 
in  .  close  (  )  ; 
return   model  ; 
}  catch  (  MalformedURLException   e  )  { 
logger  .  debug  (  "Unable to download model from "  +  url  ,  e  )  ; 
throw   new   RuntimeException  (  e  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  debug  (  "Unable to download model from "  +  url  ,  e  )  ; 
throw   new   RuntimeException  (  e  )  ; 
} 
} 











public   static   boolean   saveModel  (  Model   model  ,  String   filename  )  { 
OutputStreamWriter   out  =  null  ; 
try  { 
if  (  filename  !=  null  )  { 
if  (  SpidersGraph  .  MINDRAIDER_NEW_MODEL  .  equals  (  filename  )  )  { 
JFileChooser   fc  =  new   JFileChooser  (  )  ; 
fc  .  setCurrentDirectory  (  new   File  (  MindRaider  .  rdfCustodian  .  getModelsDirectory  (  )  )  )  ; 
fc  .  setDialogTitle  (  "Save Model As..."  )  ; 
int   returnVal  =  fc  .  showSaveDialog  (  MindRaider  .  mainJFrame  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
filename  =  file  .  getPath  (  )  ; 
MindRaider  .  masterToolBar  .  setModelLocation  (  filename  )  ; 
} 
} 
StatusBar  .  show  (  " Saving model "  +  filename  )  ; 
File   f  =  Utils  .  renewFile  (  filename  )  ; 
out  =  new   OutputStreamWriter  (  new   BufferedOutputStream  (  new   FileOutputStream  (  f  )  )  ,  "UTF-8"  )  ; 
model  .  write  (  out  ,  "RDF/XML"  )  ; 
StatusBar  .  show  (  "Model "  +  filename  +  " saved."  )  ; 
} 
}  catch  (  Exception   e  )  { 
logger  .  debug  (  "Unable to save model!"  ,  e  )  ; 
StatusBar  .  show  (  "Failed to save model "  +  filename  ,  Color  .  red  )  ; 
return   false  ; 
}  finally  { 
if  (  out  !=  null  )  { 
try  { 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
logger  .  debug  (  "Unable to close stream!"  ,  e1  )  ; 
} 
} 
} 
return   true  ; 
} 














public   Statement   createStatement  (  String   subjectUri  ,  String   predicateUri  ,  String   objectUri  ,  boolean   literal  )  { 
return   createStatement  (  (  Resource  )  newResource  (  subjectUri  ,  false  )  ,  predicateUri  ,  objectUri  ,  literal  )  ; 
} 














public   Statement   createStatement  (  Resource   subject  ,  String   predicateUri  ,  String   objectUri  ,  boolean   literal  )  { 
RDFNode   objectResource  ; 
Property   predicateResource  =  model  .  createProperty  (  predicateUri  )  ; 
if  (  literal  )  { 
objectResource  =  model  .  createLiteral  (  objectUri  )  ; 
}  else  { 
objectResource  =  model  .  createResource  (  objectUri  )  ; 
} 
subject  .  addProperty  (  predicateResource  ,  objectResource  )  ; 
StmtIterator   i  =  model  .  listStatements  (  subject  ,  predicateResource  ,  objectResource  )  ; 
if  (  i  .  hasNext  (  )  )  { 
return   i  .  nextStatement  (  )  ; 
} 
JOptionPane  .  showMessageDialog  (  MindRaider  .  mainJFrame  ,  "Unable to fetch statement."  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return   null  ; 
} 








public   Resource   getResource  (  String   uri  )  { 
return   model  .  getResource  (  uri  )  ; 
} 










public   RDFNode   newResource  (  String   uri  ,  boolean   literal  )  { 
if  (  literal  )  { 
return   model  .  createLiteral  (  uri  )  ; 
} 
return   model  .  createResource  (  uri  )  ; 
} 







public   void   setFilename  (  String   filename  )  { 
this  .  filename  =  filename  ; 
} 







public   void   setType  (  int   type  )  { 
this  .  type  =  type  ; 
} 











public   void   deleteStatement  (  RDFNode   subject  ,  Property   property  ,  RDFNode   object  )  { 
StmtIterator   i  =  model  .  listStatements  (  (  Resource  )  subject  ,  property  ,  object  )  ; 
if  (  i  .  hasNext  (  )  )  { 
model  .  remove  (  i  .  nextStatement  (  )  )  ; 
} 
} 










public   static   Statement   getStatementByPredicate  (  Model   model  ,  Property   property  )  { 
StmtIterator   i  =  model  .  listStatements  (  (  Resource  )  null  ,  property  ,  (  RDFNode  )  null  )  ; 
if  (  i  .  hasNext  (  )  )  { 
return   i  .  nextStatement  (  )  ; 
} 
return   null  ; 
} 












public   static   Statement   getStatementByPredicateAndLiteral  (  Model   model  ,  Property   property  ,  String   literal  )  { 
StmtIterator   i  =  model  .  listStatements  (  (  Resource  )  null  ,  property  ,  literal  )  ; 
if  (  i  .  hasNext  (  )  )  { 
return   i  .  nextStatement  (  )  ; 
} 
return   null  ; 
} 









public   static   void   deleteStatementByPredicate  (  Model   model  ,  Property   property  )  { 
StmtIterator   i  =  model  .  listStatements  (  (  Resource  )  null  ,  property  ,  (  RDFNode  )  null  )  ; 
if  (  i  .  hasNext  (  )  )  { 
model  .  remove  (  i  .  nextStatement  (  )  )  ; 
} 
} 









public   static   void   deleteStatementByObject  (  Model   model  ,  RDFNode   object  )  { 
StmtIterator   i  =  model  .  listStatements  (  (  Resource  )  null  ,  null  ,  object  )  ; 
if  (  i  .  hasNext  (  )  )  { 
model  .  remove  (  i  .  nextStatement  (  )  )  ; 
} 
} 










public   boolean   upInSequence  (  Resource   parentResource  ,  String   resourceUri  )  { 
Seq   seq  =  model  .  getSeq  (  parentResource  )  ; 
Resource   movedResource  =  model  .  getResource  (  resourceUri  )  ; 
int   movedResourceIndex  =  seq  .  indexOf  (  movedResource  )  ; 
logger  .  debug  (  "  UP: concept index in sequence is "  +  movedResourceIndex  )  ; 
if  (  movedResourceIndex  >  1  )  { 
com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Resource   auxResource  =  seq  .  getResource  (  movedResourceIndex  -  1  )  ; 
seq  .  set  (  movedResourceIndex  ,  auxResource  )  ; 
seq  .  set  (  movedResourceIndex  -  1  ,  movedResource  )  ; 
save  (  )  ; 
logger  .  debug  (  "  UP: after up index: "  +  seq  .  indexOf  (  movedResource  )  )  ; 
return   true  ; 
} 
return   false  ; 
} 










public   boolean   downInSequence  (  Resource   parentResource  ,  String   resourceUri  )  { 
Seq   seq  =  model  .  getSeq  (  parentResource  )  ; 
Resource   movedResource  =  model  .  getResource  (  resourceUri  )  ; 
int   movedResourceIndex  =  seq  .  indexOf  (  movedResource  )  ; 
logger  .  debug  (  "  DOWN: concept index in sequence is "  +  movedResourceIndex  )  ; 
if  (  movedResourceIndex  <  seq  .  size  (  )  )  { 
com  .  hp  .  hpl  .  jena  .  rdf  .  model  .  Resource   auxResource  =  seq  .  getResource  (  movedResourceIndex  +  1  )  ; 
seq  .  set  (  movedResourceIndex  ,  auxResource  )  ; 
seq  .  set  (  movedResourceIndex  +  1  ,  movedResource  )  ; 
save  (  )  ; 
logger  .  debug  (  "  DOWN: after down index: "  +  seq  .  indexOf  (  movedResource  )  )  ; 
return   true  ; 
} 
return   false  ; 
} 
} 

