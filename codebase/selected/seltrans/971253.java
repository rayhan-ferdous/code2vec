package   org  .  dbunit  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  slf4j  .  Logger  ; 
import   org  .  slf4j  .  LoggerFactory  ; 
import   org  .  xml  .  sax  .  InputSource  ; 








public   class   FileHelper  { 

private   static   Logger   logger  =  LoggerFactory  .  getLogger  (  FileHelper  .  class  )  ; 

private   FileHelper  (  )  { 
} 






public   static   void   deleteDirectory  (  File   directory  ,  boolean   failOnError  )  { 
boolean   success  =  deleteDirectory  (  directory  )  ; 
if  (  !  success  )  { 
throw   new   RuntimeException  (  "Failed to delete directory "  +  directory  )  ; 
} 
} 






public   static   boolean   deleteDirectory  (  File   directory  )  { 
if  (  !  directory  .  isDirectory  (  )  )  { 
logger  .  warn  (  "The directory '"  +  directory  +  "' does not exist. Will return without delete."  )  ; 
return   false  ; 
} 
File  [  ]  containedFiles  =  directory  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  containedFiles  .  length  ;  i  ++  )  { 
File   currentFile  =  containedFiles  [  i  ]  ; 
if  (  currentFile  .  isDirectory  (  )  )  { 
deleteDirectory  (  currentFile  )  ; 
}  else  { 
boolean   success  =  currentFile  .  delete  (  )  ; 
if  (  !  success  )  { 
logger  .  warn  (  "Failed to delete file '"  +  currentFile  +  "'"  )  ; 
} 
} 
} 
boolean   success  =  directory  .  delete  (  )  ; 
if  (  !  success  )  { 
logger  .  warn  (  "Failed to delete file '"  +  directory  +  "'"  )  ; 
} 
return   success  ; 
} 

public   static   InputSource   createInputSource  (  File   file  )  throws   MalformedURLException  { 
String   uri  =  file  .  toURI  (  )  .  toURL  (  )  .  toString  (  )  ; 
InputSource   source  =  new   InputSource  (  uri  )  ; 
return   source  ; 
} 








public   static   void   copyFile  (  File   srcFile  ,  File   destFile  )  throws   IOException  { 
logger  .  debug  (  "copyFile(srcFile={}, destFile={}) - start"  ,  srcFile  ,  destFile  )  ; 
FileChannel   srcChannel  =  new   FileInputStream  (  srcFile  )  .  getChannel  (  )  ; 
FileChannel   dstChannel  =  new   FileOutputStream  (  destFile  )  .  getChannel  (  )  ; 
try  { 
dstChannel  .  transferFrom  (  srcChannel  ,  0  ,  srcChannel  .  size  (  )  )  ; 
}  finally  { 
srcChannel  .  close  (  )  ; 
dstChannel  .  close  (  )  ; 
} 
} 









public   static   List   readLines  (  File   theFile  )  throws   IOException  { 
logger  .  debug  (  "readLines(theFile={}) - start"  ,  theFile  )  ; 
InputStream   tableListStream  =  new   FileInputStream  (  theFile  )  ; 
try  { 
List   orderedNames  =  new   ArrayList  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  tableListStream  )  )  ; 
String   line  =  null  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
String   table  =  line  .  trim  (  )  ; 
if  (  table  .  length  (  )  >  0  )  { 
orderedNames  .  add  (  table  )  ; 
} 
} 
return   orderedNames  ; 
}  finally  { 
tableListStream  .  close  (  )  ; 
} 
} 
} 

