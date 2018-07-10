package   uk  .  ac  .  gla  .  terrier  .  indexing  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  List  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   uk  .  ac  .  gla  .  terrier  .  utility  .  Files  ; 
import   uk  .  ac  .  gla  .  terrier  .  utility  .  ApplicationSetup  ; 








public   class   SimpleFileCollection   implements   Collection  { 

protected   static   Logger   logger  =  Logger  .  getRootLogger  (  )  ; 



public   static   final   String   NAMESPACE_DOCUMENTS  =  "uk.ac.gla.terrier.indexing."  ; 


protected   LinkedList  <  String  >  FileList  =  new   LinkedList  <  String  >  (  )  ; 



protected   List  <  String  >  firstList  ; 


protected   List  <  String  >  indexedFiles  =  new   ArrayList  <  String  >  (  )  ; 


protected   int   Docid  =  0  ; 


protected   boolean   Recurse  =  true  ; 




protected   Map  <  String  ,  Class  >  extension_DocumentClass  =  new   HashMap  <  String  ,  Class  >  (  )  ; 


protected   File   thisFile  ; 


protected   String   thisFilename  ; 




protected   InputStream   currentStream  =  null  ; 





public   SimpleFileCollection  (  List  <  String  >  filelist  ,  boolean   recurse  )  { 
FileList  =  new   LinkedList  <  String  >  (  filelist  )  ; 
firstList  =  new   LinkedList  (  filelist  )  ; 
createExtensionDocumentMapping  (  )  ; 
} 






public   SimpleFileCollection  (  )  { 
this  (  ApplicationSetup  .  COLLECTION_SPEC  )  ; 
} 







public   SimpleFileCollection  (  String   addressCollectionFilename  )  { 
ArrayList  <  String  >  generatedFileList  =  new   ArrayList  <  String  >  (  )  ; 
try  { 
BufferedReader   br  =  Files  .  openFileReader  (  addressCollectionFilename  )  ; 
String   filename  =  br  .  readLine  (  )  ; 
while  (  filename  !=  null  )  { 
if  (  filename  .  startsWith  (  "#"  )  )  { 
filename  =  br  .  readLine  (  )  ; 
continue  ; 
} 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Added "  +  filename  +  " to filelist for SimpleFileCollection"  )  ; 
} 
generatedFileList  .  add  (  filename  )  ; 
filename  =  br  .  readLine  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "problem opening address list of files in SimpleFileCollectio: "  ,  ioe  )  ; 
} 
FileList  =  new   LinkedList  <  String  >  (  generatedFileList  )  ; 
firstList  =  new   LinkedList  <  String  >  (  generatedFileList  )  ; 
createExtensionDocumentMapping  (  )  ; 
} 







protected   void   createExtensionDocumentMapping  (  )  { 
String   staticMappings  =  ApplicationSetup  .  getProperty  (  "indexing.simplefilecollection.extensionsparsers"  ,  "txt:FileDocument,text:FileDocument,tex:FileDocument,bib:FileDocument,"  +  "pdf:PDFDocument,html:HTMLDocument,htm:HTMLDocument,xhtml:HTMLDocument,xml:HTMLDocument,"  +  "doc:MSWordDocument,ppt:MSPowerpointDocument,xls:MSExcelDocument"  )  ; 
String   defaultMapping  =  ApplicationSetup  .  getProperty  (  "indexing.simplefilecollection.defaultparser"  ,  ""  )  ; 
if  (  staticMappings  .  length  (  )  >  0  )  { 
String  [  ]  mappings  =  staticMappings  .  split  (  "\\s*,\\s*"  )  ; 
for  (  int   i  =  0  ;  i  <  mappings  .  length  ;  i  ++  )  { 
if  (  mappings  [  i  ]  .  indexOf  (  ":"  )  <  1  )  continue  ; 
String  [  ]  mapping  =  mappings  [  i  ]  .  split  (  ":"  )  ; 
if  (  mapping  .  length  ==  2  &&  mapping  [  0  ]  .  length  (  )  >  0  &&  mapping  [  1  ]  .  length  (  )  >  0  )  { 
if  (  mapping  [  1  ]  .  indexOf  (  "."  )  ==  -  1  )  mapping  [  1  ]  =  NAMESPACE_DOCUMENTS  +  mapping  [  1  ]  ; 
try  { 
Class   d  =  Class  .  forName  (  mapping  [  1  ]  ,  false  ,  this  .  getClass  (  )  .  getClassLoader  (  )  )  ; 
extension_DocumentClass  .  put  (  mapping  [  0  ]  .  toLowerCase  (  )  ,  d  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Missing class "  +  mapping  [  1  ]  +  " for "  +  mapping  [  0  ]  .  toLowerCase  (  )  +  " files."  ,  e  )  ; 
} 
} 
} 
} 
if  (  !  defaultMapping  .  equals  (  ""  )  )  { 
if  (  defaultMapping  .  indexOf  (  "."  )  ==  -  1  )  defaultMapping  =  NAMESPACE_DOCUMENTS  +  defaultMapping  ; 
try  { 
Class   d  =  Class  .  forName  (  defaultMapping  ,  false  ,  this  .  getClass  (  )  .  getClassLoader  (  )  )  ; 
extension_DocumentClass  .  put  (  "|DEFAULT|"  ,  d  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 





public   boolean   nextDocument  (  )  { 
if  (  FileList  .  size  (  )  ==  0  )  return   false  ; 
boolean   rtr  =  false  ; 
thisFilename  =  null  ; 
while  (  FileList  .  size  (  )  >  0  &&  !  rtr  )  { 
thisFilename  =  FileList  .  removeFirst  (  )  ; 
logger  .  info  (  "NEXT: "  +  thisFilename  )  ; 
if  (  !  Files  .  exists  (  thisFilename  )  ||  !  Files  .  canRead  (  thisFilename  )  )  { 
if  (  !  Files  .  exists  (  thisFilename  )  )  logger  .  warn  (  "File doesn't exist: "  +  thisFilename  )  ;  else   if  (  !  Files  .  canRead  (  thisFilename  )  )  logger  .  warn  (  "File cannot be read: "  +  thisFilename  )  ; 
rtr  =  nextDocument  (  )  ; 
}  else   if  (  Files  .  isDirectory  (  thisFilename  )  )  { 
if  (  Recurse  )  addDirectoryListing  (  )  ; 
}  else  { 
Docid  ++  ; 
rtr  =  true  ; 
} 
} 
return   rtr  ; 
} 





public   Document   getDocument  (  )  { 
InputStream   in  =  null  ; 
if  (  currentStream  !=  null  )  { 
try  { 
currentStream  .  close  (  )  ; 
currentStream  =  null  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  thisFilename  ==  null  )  { 
return   null  ; 
} 
String   filename  =  null  ; 
try  { 
in  =  Files  .  openFileStream  (  thisFilename  )  ; 
filename  =  thisFilename  .  replaceAll  (  "\\.gz$"  ,  ""  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  warn  (  "Problem reading "  +  thisFilename  +  " in "  +  "SimpleFileCollection.getDocuent() : "  ,  ioe  )  ; 
} 
currentStream  =  in  ; 
return   makeDocument  (  filename  ,  thisFile  ,  in  )  ; 
} 









protected   Document   makeDocument  (  String   Filename  ,  File   f  ,  InputStream   in  )  { 
if  (  Filename  ==  null  ||  in  ==  null  )  return   null  ; 
String  [  ]  splitStr  =  Filename  .  split  (  "\\."  )  ; 
String   ext  =  splitStr  [  splitStr  .  length  -  1  ]  .  toLowerCase  (  )  ; 
Class   reader  =  extension_DocumentClass  .  get  (  ext  )  ; 
Document   rtr  =  null  ; 
if  (  reader  ==  null  )  { 
reader  =  extension_DocumentClass  .  get  (  "|DEFAULT|"  )  ; 
} 
if  (  reader  ==  null  )  { 
logger  .  warn  (  "No available parser for file "  +  Filename  +  ", file is ignored."  )  ; 
return   null  ; 
} 
logger  .  debug  (  "Using "  +  reader  .  getName  (  )  +  " to read "  +  Filename  )  ; 
try  { 
Class  [  ]  params  =  {  File  .  class  ,  InputStream  .  class  }  ; 
Object  [  ]  params2  =  {  f  ,  in  }  ; 
rtr  =  (  Document  )  (  reader  .  getConstructor  (  params  )  .  newInstance  (  params2  )  )  ; 
indexedFiles  .  add  (  thisFilename  )  ; 
}  catch  (  OutOfMemoryError   e  )  { 
logger  .  warn  (  "Problem instantiating a document class; Out of memory error occured: "  ,  e  )  ; 
System  .  gc  (  )  ; 
}  catch  (  StackOverflowError   e  )  { 
logger  .  warn  (  "Problem instantiating a document class; Stack Overflow error occured: "  ,  e  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warn  (  "Problem instantiating a document class: "  ,  e  )  ; 
} 
return   rtr  ; 
} 






public   boolean   endOfCollection  (  )  { 
return  (  FileList  .  size  (  )  ==  0  )  ; 
} 




public   void   reset  (  )  { 
Docid  =  0  ; 
FileList  =  new   LinkedList  <  String  >  (  firstList  )  ; 
indexedFiles  =  new   ArrayList  <  String  >  (  )  ; 
} 





public   String   getDocid  (  )  { 
return   Docid  +  ""  ; 
} 

public   void   close  (  )  { 
if  (  currentStream  !=  null  )  { 
try  { 
currentStream  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
logger  .  error  (  "Exception occured while trying to close an IO stream"  ,  ioe  )  ; 
} 
} 
} 


public   List  <  String  >  getFileList  (  )  { 
return   indexedFiles  ; 
} 



protected   void   addDirectoryListing  (  )  { 
String  [  ]  dirContents  =  Files  .  list  (  thisFilename  )  ; 
if  (  dirContents  ==  null  )  return  ; 
for  (  String   e  :  dirContents  )  { 
if  (  e  .  equals  (  "."  )  ||  e  .  equals  (  ".."  )  )  continue  ; 
FileList  .  add  (  thisFilename  +  ApplicationSetup  .  FILE_SEPARATOR  +  e  )  ; 
} 
} 





public   static   void   main  (  String  [  ]  args  )  { 
Indexer   in  =  new   BasicIndexer  (  ApplicationSetup  .  TERRIER_INDEX_PATH  ,  ApplicationSetup  .  TERRIER_INDEX_PREFIX  )  ; 
in  .  createDirectIndex  (  new   Collection  [  ]  {  new   SimpleFileCollection  (  args  [  0  ]  )  }  )  ; 
in  .  createInvertedIndex  (  )  ; 
} 
} 

