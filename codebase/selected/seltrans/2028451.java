package   gate  .  yam  .  convert  ; 

import   gate  .  util  .  GateException  ; 
import   gate  .  yam  .  YamFile  ; 
import   org  .  jdom  .  *  ; 
import   org  .  jdom  .  filter  .  ContentFilter  ; 
import   org  .  jdom  .  filter  .  ElementFilter  ; 
import   org  .  jdom  .  filter  .  Filter  ; 
import   org  .  springframework  .  core  .  io  .  FileSystemResource  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 





public   class   JSPWikiToYamConverter  { 


private   static   final   String   INPUT_ENCODING  =  "ISO-8859-1"  ; 

private   static   final   String   OUTPUT_ENCODING  =  "UTF-8"  ; 




private   static   final   char  [  ]  YAM_SPECIAL_CHARACTERS  =  "_*^"  .  toCharArray  (  )  ; 









public   static   String   stringToString  (  String   jspWikiSource  )  throws   TransformerException  ,  IOException  { 
Reader   reader  =  new   StringReader  (  jspWikiSource  )  ; 
return   readerToString  (  reader  )  ; 
} 








public   static   String   readerToString  (  Reader   jspReader  )  throws   TransformerException  ,  IOException  { 
return   readerToStringWithTitle  (  jspReader  ,  null  )  ; 
} 










public   static   String   readerToStringWithTitle  (  Reader   jspReader  ,  String   title  )  throws   TransformerException  ,  IOException  { 
JSPWikiMarkupParser   parser  =  new   JSPWikiMarkupParser  (  jspReader  )  ; 
Document   jdomDoc  =  parser  .  parse  (  )  ; 
processHeadings  (  jdomDoc  )  ; 
processEscapes  (  jdomDoc  )  ; 
massageLinks  (  jdomDoc  )  ; 
processEntityReferences  (  jdomDoc  )  ; 
processSpecifics  (  jdomDoc  )  ; 
if  (  title  !=  null  )  addTitle  (  jdomDoc  ,  title  )  ; 
return   HtmlToYamConverter  .  jdomToString  (  jdomDoc  )  ; 
} 








private   static   void   processHeadings  (  org  .  jdom  .  Document   jdomDoc  )  { 
final   Pattern   headingPattern  =  Pattern  .  compile  (  "[Hh][123456]"  )  ; 
class   HeadingFilter   implements   Filter  { 

public   boolean   matches  (  Object   obj  )  { 
if  (  !  (  obj   instanceof   Element  )  )  return   false  ; 
Element   el  =  (  Element  )  obj  ; 
return   headingPattern  .  matcher  (  el  .  getName  (  )  )  .  matches  (  )  ; 
} 
} 
List  <  Element  >  toAddParaAfter  =  new   ArrayList  <  Element  >  (  )  ; 
for  (  Iterator   hIt  =  jdomDoc  .  getDescendants  (  new   HeadingFilter  (  )  )  ;  hIt  .  hasNext  (  )  ;  )  { 
Element   hEl  =  (  Element  )  hIt  .  next  (  )  ; 
Content   next  =  getNextSibling  (  hEl  )  ; 
boolean   emptyPara  =  false  ; 
if  (  next   instanceof   Element  )  { 
Element   nextEl  =  (  Element  )  next  ; 
if  (  nextEl  .  getName  (  )  .  equalsIgnoreCase  (  "p"  )  &&  nextEl  .  getChildren  (  )  .  isEmpty  (  )  )  { 
emptyPara  =  true  ; 
} 
} 
if  (  !  emptyPara  )  { 
toAddParaAfter  .  add  (  hEl  )  ; 
} 
} 
for  (  Element   hEl  :  toAddParaAfter  )  { 
Element   parEl  =  hEl  .  getParentElement  (  )  ; 
int   hIndex  =  parEl  .  indexOf  (  hEl  )  ; 
parEl  .  addContent  (  hIndex  +  1  ,  new   Element  (  "p"  )  )  ; 
} 
} 







private   static   void   addTitle  (  org  .  jdom  .  Document   jdomDoc  ,  String   title  )  { 
Element   body  =  jdomDoc  .  getRootElement  (  )  .  getChild  (  "body"  )  ; 
body  .  addContent  (  0  ,  new   Text  (  title  )  )  ; 
body  .  addContent  (  1  ,  new   Element  (  "p"  )  )  ; 
body  .  addContent  (  2  ,  new   Element  (  "p"  )  )  ; 
} 







private   static   void   massageLinks  (  org  .  jdom  .  Document   jdomDoc  )  { 
for  (  Iterator   aIt  =  jdomDoc  .  getDescendants  (  new   ElementFilter  (  "a"  )  )  ;  aIt  .  hasNext  (  )  ;  )  { 
Element   aEl  =  (  Element  )  aIt  .  next  (  )  ; 
String   href  =  aEl  .  getAttributeValue  (  "href"  )  ; 
if  (  href  !=  null  )  { 
if  (  href  .  startsWith  (  "VIEW"  )  )  { 
href  =  href  .  substring  (  4  )  +  ".html"  ; 
} 
href  =  href  .  replace  (  ","  ,  "\\,"  )  ; 
href  =  href  .  replace  (  " "  ,  "\\ "  )  ; 
aEl  .  setAttribute  (  "href"  ,  href  )  ; 
} 
} 
} 






private   static   void   processSpecifics  (  org  .  jdom  .  Document   jdomDoc  )  { 
List  <  Content  >  toRemove  =  new   ArrayList  <  Content  >  (  )  ; 
for  (  Iterator   textIt  =  jdomDoc  .  getDescendants  (  new   ContentFilter  (  ContentFilter  .  TEXT  )  )  ;  textIt  .  hasNext  (  )  ;  )  { 
Text   text  =  (  Text  )  textIt  .  next  (  )  ; 
String   content  =  text  .  getText  (  )  ; 
if  (  content  .  contains  (  "Group.jsp?group"  )  )  { 
toRemove  .  add  (  text  )  ; 
} 
} 
for  (  Content   remove  :  toRemove  )  { 
Element   parent  =  remove  .  getParentElement  (  )  ; 
parent  .  removeContent  (  remove  )  ; 
if  (  parent  .  getName  (  )  .  equals  (  "li"  )  &&  parent  .  getChildren  (  )  .  size  (  )  ==  0  )  { 
Element   grandParent  =  parent  .  getParentElement  (  )  ; 
grandParent  .  removeContent  (  parent  )  ; 
} 
} 
} 




private   static   Map  <  String  ,  String  >  SPECIAL_ENTITIES  ; 

static  { 
SPECIAL_ENTITIES  =  new   HashMap  <  String  ,  String  >  (  )  ; 
SPECIAL_ENTITIES  .  put  (  "&lt;"  ,  "<"  )  ; 
SPECIAL_ENTITIES  .  put  (  "&gt;"  ,  ">"  )  ; 
SPECIAL_ENTITIES  .  put  (  "&amp;"  ,  "&"  )  ; 
SPECIAL_ENTITIES  .  put  (  "&quot;"  ,  "\""  )  ; 
} 






private   static   void   processEntityReferences  (  org  .  jdom  .  Document   jdomDoc  )  { 
for  (  Iterator   textIt  =  jdomDoc  .  getDescendants  (  new   ContentFilter  (  ContentFilter  .  TEXT  )  )  ;  textIt  .  hasNext  (  )  ;  )  { 
Text   text  =  (  Text  )  textIt  .  next  (  )  ; 
String   content  =  text  .  getText  (  )  ; 
for  (  String   key  :  SPECIAL_ENTITIES  .  keySet  (  )  )  { 
content  =  content  .  replace  (  key  ,  SPECIAL_ENTITIES  .  get  (  key  )  )  ; 
} 
if  (  text  .  getParentElement  (  )  .  getName  (  )  .  equalsIgnoreCase  (  "li"  )  )  content  =  content  .  replaceAll  (  "^(?:\\r?\\n)*"  ,  " "  )  .  replaceAll  (  "(?:\\r?\\n)*$"  ,  " "  )  ; 
text  .  setText  (  content  )  ; 
} 
} 







private   static   void   processEscapes  (  org  .  jdom  .  Document   jdomDoc  )  { 
org  .  jdom  .  Content   currentNode  =  jdomDoc  .  getRootElement  (  )  ; 
boolean   finished  =  false  ; 
while  (  !  finished  )  { 
if  (  currentNode   instanceof   org  .  jdom  .  Text  &&  !  currentNode  .  getParentElement  (  )  .  getName  (  )  .  equalsIgnoreCase  (  "pre"  )  )  { 
org  .  jdom  .  Text   textNode  =  (  org  .  jdom  .  Text  )  currentNode  ; 
String   textData  =  textNode  .  getText  (  )  ; 
for  (  char   c  :  YAM_SPECIAL_CHARACTERS  )  { 
if  (  textData  .  indexOf  (  c  )  !=  -  1  )  { 
textData  =  textData  .  replace  (  Character  .  toString  (  c  )  ,  "\\"  +  c  )  ; 
} 
} 
textNode  .  setText  (  textData  )  ; 
} 
Content   nextNode  =  null  ; 
if  (  currentNode   instanceof   Parent  &&  (  (  Parent  )  currentNode  )  .  getContentSize  (  )  >  0  )  { 
nextNode  =  (  (  Parent  )  currentNode  )  .  getContent  (  0  )  ; 
} 
if  (  nextNode  ==  null  )  { 
nextNode  =  getNextSibling  (  currentNode  )  ; 
if  (  nextNode  ==  null  )  { 
while  (  nextNode  ==  null  &&  !  finished  )  { 
Parent   parent  =  currentNode  .  getParent  (  )  ; 
if  (  parent  ==  null  ||  parent   instanceof   org  .  jdom  .  Document  )  { 
finished  =  true  ; 
}  else  { 
currentNode  =  (  Content  )  parent  ; 
nextNode  =  getNextSibling  (  (  Content  )  parent  )  ; 
} 
} 
} 
} 
currentNode  =  nextNode  ; 
} 
} 






private   static   Content   getNextSibling  (  Content   node  )  { 
Parent   parent  =  node  .  getParent  (  )  ; 
if  (  parent  !=  null  )  { 
int   currentIndex  =  parent  .  indexOf  (  node  )  ; 
if  (  parent  .  getContentSize  (  )  >  (  currentIndex  +  1  )  )  { 
return   parent  .  getContent  (  currentIndex  +  1  )  ; 
} 
} 
return   null  ; 
} 







private   static   void   processAttachments  (  File   jspwFile  ,  File   yamFile  )  throws   IOException  { 
String   jspwFilePath  =  jspwFile  .  getAbsolutePath  (  )  ; 
String   jspwAttachDirPath  =  jspwFilePath  .  substring  (  0  ,  jspwFilePath  .  length  (  )  -  4  )  ; 
File   jspwAttachDir  =  new   File  (  jspwAttachDirPath  )  ; 
String   yamAttachDirName  =  jspwAttachDir  .  getName  (  )  ; 
File   yamAttachDir  =  new   File  (  yamFile  .  getParent  (  )  ,  yamAttachDirName  )  ; 
if  (  jspwAttachDir  .  isDirectory  (  )  )  { 
List  <  String  >  yamAttachFileNames  =  new   ArrayList  <  String  >  (  )  ; 
for  (  File   jspwAttachFile  :  jspwAttachDir  .  listFiles  (  )  )  { 
if  (  jspwAttachFile  .  isFile  (  )  &&  !  jspwAttachFile  .  isHidden  (  )  )  { 
String   yamAttachFileName  =  jspwAttachFile  .  getName  (  )  ; 
yamAttachFileNames  .  add  (  yamAttachFileName  )  ; 
File   yamAttachFile  =  new   File  (  yamAttachDir  ,  yamAttachFileName  )  ; 
copy  (  jspwAttachFile  ,  yamAttachFile  )  ; 
} 
} 
StringBuilder   strB  =  new   StringBuilder  (  )  ; 
strB  .  append  (  "---\n"  )  ; 
strB  .  append  (  "%2* Attachments\n"  )  ; 
for  (  String   fileName  :  yamAttachFileNames  )  { 
strB  .  append  (  "- %("  )  ; 
strB  .  append  (  yamAttachDirName  )  .  append  (  "/"  )  .  append  (  fileName  )  ; 
strB  .  append  (  ", "  )  .  append  (  fileName  )  .  append  (  ")\n"  )  ; 
} 
PrintWriter   pw  =  new   PrintWriter  (  new   FileOutputStream  (  yamFile  ,  true  )  ,  true  )  ; 
pw  .  append  (  strB  .  toString  (  )  )  ; 
pw  .  close  (  )  ; 
} 
} 







private   static   void   copy  (  File   in  ,  File   out  )  throws   IOException  { 
if  (  !  out  .  getParentFile  (  )  .  isDirectory  (  )  )  out  .  getParentFile  (  )  .  mkdirs  (  )  ; 
FileChannel   ic  =  new   FileInputStream  (  in  )  .  getChannel  (  )  ; 
FileChannel   oc  =  new   FileOutputStream  (  out  )  .  getChannel  (  )  ; 
ic  .  transferTo  (  0  ,  ic  .  size  (  )  ,  oc  )  ; 
ic  .  close  (  )  ; 
oc  .  close  (  )  ; 
} 




private   static   class   JSPWikiFileFilter   implements   FilenameFilter  { 


public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   name  .  endsWith  (  ".txt"  )  ; 
} 
} 






public   static   void   main  (  String  [  ]  args  )  { 
if  (  args  .  length  <  1  )  { 
printUsage  (  )  ; 
System  .  exit  (  1  )  ; 
} 
File   inFile  =  new   File  (  args  [  0  ]  )  ; 
List  <  File  >  filesToConvert  =  new   ArrayList  <  File  >  (  )  ; 
if  (  inFile  .  isFile  (  )  )  { 
filesToConvert  .  add  (  inFile  )  ; 
}  else   if  (  inFile  .  isDirectory  (  )  )  { 
File  [  ]  filesInDir  =  inFile  .  listFiles  (  new   JSPWikiFileFilter  (  )  )  ; 
filesToConvert  .  addAll  (  Arrays  .  asList  (  filesInDir  )  )  ; 
}  else  { 
printUsage  (  )  ; 
System  .  exit  (  1  )  ; 
} 
String   outDirName  =  null  ; 
if  (  args  .  length  >  1  )  { 
outDirName  =  args  [  1  ]  ; 
if  (  !  new   File  (  outDirName  )  .  isDirectory  (  )  )  { 
printUsage  (  )  ; 
System  .  exit  (  1  )  ; 
} 
} 
List  <  String  >  errors  =  new   ArrayList  <  String  >  (  )  ; 
List  <  YamFile  >  yamsToGenerate  =  new   ArrayList  <  YamFile  >  (  )  ; 
for  (  File   jspwFile  :  filesToConvert  )  { 
String   jspwFileName  =  jspwFile  .  getName  (  )  ; 
String   prefix  =  jspwFileName  .  substring  (  0  ,  jspwFileName  .  length  (  )  -  4  )  ; 
String   yamFileName  =  prefix  +  ".yam"  ; 
File   yamDiskFile  =  new   File  (  outDirName  ,  yamFileName  )  ; 
try  { 
System  .  out  .  println  (  "Translating "  +  jspwFileName  )  ; 
PrintWriter   yamOut  =  new   PrintWriter  (  yamDiskFile  ,  OUTPUT_ENCODING  )  ; 
Reader   reader  =  new   InputStreamReader  (  new   FileInputStream  (  jspwFile  )  ,  INPUT_ENCODING  )  ; 
yamOut  .  println  (  readerToStringWithTitle  (  reader  ,  prefix  )  )  ; 
yamOut  .  flush  (  )  ; 
YamFile   yamFile  =  YamFile  .  get  (  new   FileSystemResource  (  yamDiskFile  .  getCanonicalPath  (  )  )  )  ; 
processAttachments  (  jspwFile  ,  yamDiskFile  )  ; 
yamFile  .  setContextPath  (  outDirName  )  ; 
yamsToGenerate  .  add  (  yamFile  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
errors  .  add  (  yamFileName  +  ": "  +  e  .  toString  (  )  )  ; 
} 
for  (  YamFile   yam  :  yamsToGenerate  )  { 
try  { 
yam  .  generate  (  )  ; 
}  catch  (  GateException   ge  )  { 
ge  .  printStackTrace  (  )  ; 
errors  .  add  (  yam  +  ": "  +  ge  .  toString  (  )  )  ; 
} 
} 
} 
System  .  out  .  println  (  "Translation finished with "  +  errors  .  size  (  )  +  " errors"  )  ; 
for  (  String   error  :  errors  )  { 
System  .  out  .  println  (  error  )  ; 
} 
} 




private   static   void   printUsage  (  )  { 
System  .  out  .  println  (  "JSPWikiToYamConverter - convert JSPWiki files to YAM"  )  ; 
System  .  out  .  println  (  "Usage:"  )  ; 
System  .  out  .  println  (  "  JSPWikiToYamConverter (file|directory) [outputDir]"  )  ; 
System  .  out  .  println  (  "    file:      JSPWiki file to translate"  )  ; 
System  .  out  .  println  (  "    directory: directory of files to translate"  )  ; 
System  .  out  .  println  (  "    outputDir: directory to write YAM files to"  )  ; 
System  .  out  .  println  (  "               (defaults to current directory)"  )  ; 
} 
} 

