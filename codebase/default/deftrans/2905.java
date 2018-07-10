import   pspdash  .  *  ; 
import   pspdash  .  data  .  DataRepository  ; 
import   pspdash  .  data  .  InterpolatingFilter  ; 
import   pspdash  .  data  .  ListData  ; 
import   pspdash  .  data  .  SaveableData  ; 
import   pspdash  .  data  .  SimpleData  ; 
import   pspdash  .  data  .  StringData  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  StringTokenizer  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  w3c  .  dom  .  *  ; 













































































public   class   file   extends   TinyCGIBase  { 

public   static   final   String   FILE_PARAM  =  "file"  ; 

public   static   final   String   FILE_XML_DATANAME  =  "FILES_XML"  ; 

public   static   final   String   NAME_ATTR  =  "name"  ; 

public   static   final   String   PATH_ATTR  =  "path"  ; 

public   static   final   String   DEFAULT_PATH_ATTR  =  "defaultPath"  ; 

public   static   final   String   TEMPLATE_PATH_ATTR  =  "templatePath"  ; 

public   static   final   String   TEMPLATE_VAL_ATTR  =  "templateVal"  ; 

public   static   final   String   DIRECTORY_TAG_NAME  =  "directory"  ; 

public   static   final   String   TEMPLATE_ROOT_WIN  =  "\\Templates\\"  ; 

public   static   final   String   TEMPLATE_ROOT_UNIX  =  "/Templates/"  ; 

protected   void   writeHeader  (  )  { 
} 


protected   void   writeContents  (  )  throws   IOException  { 
parseFormData  (  )  ; 
String   filename  =  getParameter  (  FILE_PARAM  )  ; 
if  (  filename  ==  null  )  ; 
Element   file  =  findFile  (  filename  )  ; 
if  (  file  ==  null  )  { 
sendNoSuchFileMessage  (  filename  )  ; 
return  ; 
} 
File   result  =  computePath  (  file  ,  false  )  ; 
if  (  !  metaPathVariables  .  isEmpty  (  )  )  { 
pathVariables  =  metaPathVariables  ; 
pathVariableNames  =  metaPathVariableNames  ; 
displayNeedInfoForm  (  filename  ,  null  ,  false  ,  file  )  ; 
return  ; 
} 
if  (  result  ==  null  &&  !  needPathInfo  (  )  )  { 
sendNoSuchFileMessage  (  filename  )  ; 
return  ; 
} 
if  (  result  ==  null  )  { 
displayNeedInfoForm  (  filename  ,  result  ,  false  ,  file  )  ; 
return  ; 
} 
if  (  !  result  .  exists  (  )  &&  isDirectory  )  { 
if  (  !  result  .  mkdirs  (  )  )  { 
sendCopyTemplateError  (  "Could not create the directory '"  +  result  .  getPath  (  )  +  "'."  )  ; 
return  ; 
} 
} 
if  (  result  .  exists  (  )  )  { 
redirectTo  (  filename  ,  result  )  ; 
return  ; 
} 
savePathInfo  (  )  ; 
File   template  =  computePath  (  file  ,  true  )  ; 
if  (  !  metaPathVariables  .  isEmpty  (  )  )  { 
pathVariables  =  metaPathVariables  ; 
pathVariableNames  =  metaPathVariableNames  ; 
displayNeedInfoForm  (  filename  ,  null  ,  true  ,  file  )  ; 
return  ; 
} 
if  (  !  foundTemplate  )  { 
restorePathInfo  (  )  ; 
displayNeedInfoForm  (  filename  ,  result  ,  false  ,  file  )  ; 
return  ; 
} 
String   templateURL  =  null  ; 
if  (  isTemplateURL  (  template  )  )  try  { 
templateURL  =  template  .  toURL  (  )  .  toString  (  )  ; 
templateURL  =  templateURL  .  substring  (  templateURL  .  indexOf  (  TEMPLATE_ROOT_UNIX  )  +  TEMPLATE_ROOT_UNIX  .  length  (  )  -  1  )  ; 
}  catch  (  MalformedURLException   mue  )  { 
} 
if  (  template  ==  null  ||  (  templateURL  ==  null  &&  !  template  .  exists  (  )  )  )  { 
displayNeedInfoForm  (  filename  ,  template  ,  true  ,  file  )  ; 
return  ; 
} 
File   resultDir  =  result  .  getParentFile  (  )  ; 
if  (  !  resultDir  .  exists  (  )  )  if  (  !  resultDir  .  mkdirs  (  )  )  { 
sendCopyTemplateError  (  "Could not create the directory '"  +  resultDir  .  getPath  (  )  +  "'."  )  ; 
return  ; 
} 
if  (  copyFile  (  template  ,  templateURL  ,  result  )  ==  false  )  { 
sendCopyTemplateError  (  "Could not copy '"  +  template  .  getPath  (  )  +  "' to '"  +  result  .  getPath  (  )  +  "'."  )  ; 
return  ; 
} 
redirectTo  (  filename  ,  result  )  ; 
} 


private   void   redirectTo  (  String   filename  ,  File   result  )  { 
try  { 
out  .  print  (  "Location: "  +  result  .  toURL  (  )  +  "\r\n\r\n"  )  ; 
}  catch  (  MalformedURLException   mue  )  { 
System  .  out  .  println  (  "Exception: "  +  mue  )  ; 
displayNeedInfoForm  (  filename  ,  result  ,  false  ,  null  )  ; 
} 
} 


private   boolean   copyFile  (  File   template  ,  String   templateURL  ,  File   result  )  { 
if  (  template  ==  result  )  return   true  ; 
if  (  templateURL  ==  null  &&  !  template  .  isFile  (  )  )  return   true  ; 
try  { 
InputStream   in  =  openInput  (  template  ,  templateURL  )  ; 
in  =  new   InterpolatingFilter  (  in  ,  getDataRepository  (  )  ,  getPrefix  (  )  )  ; 
OutputStream   out  =  new   FileOutputStream  (  result  )  ; 
copyFile  (  in  ,  out  )  ; 
return   true  ; 
}  catch  (  IOException   ioe  )  { 
} 
return   false  ; 
} 

private   InputStream   openInput  (  File   template  ,  String   templateURL  )  throws   IOException  { 
if  (  templateURL  !=  null  )  return   new   ByteArrayInputStream  (  getRequest  (  templateURL  ,  true  )  )  ;  else   return   new   FileInputStream  (  template  )  ; 
} 


private   void   copyFile  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  in  .  read  (  buffer  )  )  !=  -  1  )  out  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 

private   boolean   isTemplateURL  (  File   f  )  { 
if  (  f  ==  null  )  return   false  ; 
String   path  =  f  .  getPath  (  )  ; 
return  (  path  .  startsWith  (  TEMPLATE_ROOT_WIN  )  ||  path  .  startsWith  (  TEMPLATE_ROOT_UNIX  )  )  ; 
} 

private   boolean   foundTemplate  ,  isDirectory  ; 

private   Map   pathVariables  ,  savedPathVariables  ,  metaPathVariables  ; 

private   ArrayList   pathVariableNames  ,  savedPathVariableNames  ,  metaPathVariableNames  ; 


private   void   savePathInfo  (  )  { 
savedPathVariables  =  pathVariables  ; 
savedPathVariableNames  =  pathVariableNames  ; 
} 


private   void   restorePathInfo  (  )  { 
pathVariables  =  savedPathVariables  ; 
pathVariableNames  =  savedPathVariableNames  ; 
} 


private   boolean   needPathInfo  (  )  { 
return  !  pathVariableNames  .  isEmpty  (  )  ; 
} 


private   File   computePath  (  Node   n  ,  boolean   lookForTemplate  )  { 
if  (  n  ==  null  )  { 
pathVariables  =  new   HashMap  (  )  ; 
pathVariableNames  =  new   ArrayList  (  )  ; 
metaPathVariables  =  new   HashMap  (  )  ; 
metaPathVariableNames  =  new   ArrayList  (  )  ; 
foundTemplate  =  false  ; 
return   null  ; 
} 
File   parentPath  =  computePath  (  n  .  getParentNode  (  )  ,  lookForTemplate  )  ; 
String   pathVal  =  null  ; 
boolean   isTemplate  =  false  ; 
if  (  n   instanceof   Element  )  { 
if  (  lookForTemplate  )  { 
pathVal  =  (  (  Element  )  n  )  .  getAttribute  (  TEMPLATE_PATH_ATTR  )  ; 
setTemplatePathVariables  (  (  Element  )  n  )  ; 
} 
if  (  XMLUtils  .  hasValue  (  pathVal  )  )  isTemplate  =  true  ;  else   pathVal  =  (  (  Element  )  n  )  .  getAttribute  (  PATH_ATTR  )  ; 
} 
if  (  !  XMLUtils  .  hasValue  (  pathVal  )  )  return   parentPath  ; 
isDirectory  =  DIRECTORY_TAG_NAME  .  equals  (  (  (  Element  )  n  )  .  getTagName  (  )  )  ; 
String   defaultPath  =  (  (  Element  )  n  )  .  getAttribute  (  DEFAULT_PATH_ATTR  )  ; 
StringTokenizer   tok  =  new   StringTokenizer  (  pathVal  ,  "[]"  ,  true  )  ; 
StringBuffer   path  =  new   StringBuffer  (  )  ; 
String   token  ; 
PathVariable   pathVar  =  null  ; 
boolean   unknownsPresent  =  false  ,  firstItem  =  true  ; 
while  (  tok  .  hasMoreTokens  (  )  )  { 
token  =  tok  .  nextToken  (  )  ; 
if  (  "["  .  equals  (  token  )  )  { 
token  =  tok  .  nextToken  (  )  ; 
tok  .  nextToken  (  )  ; 
String   impliedPath  =  null  ; 
if  (  firstItem  &&  parentPath  !=  null  )  impliedPath  =  parentPath  .  getPath  (  )  ; 
String   defaultValue  =  null  ; 
if  (  firstItem  &&  !  tok  .  hasMoreTokens  (  )  &&  !  isTemplate  &&  XMLUtils  .  hasValue  (  defaultPath  )  )  defaultValue  =  defaultPath  ; 
pathVar  =  getPathVariable  (  token  ,  impliedPath  ,  defaultValue  )  ; 
if  (  pathVar  .  isUnknown  (  )  )  unknownsPresent  =  true  ;  else   path  .  append  (  pathVar  .  getValue  (  )  )  ; 
}  else  { 
path  .  append  (  token  )  ; 
} 
firstItem  =  false  ; 
} 
String   selfPath  =  path  .  toString  (  )  ; 
if  (  unknownsPresent  )  { 
if  (  !  isTemplate  &&  XMLUtils  .  hasValue  (  defaultPath  )  )  selfPath  =  defaultPath  ;  else  { 
foundTemplate  =  (  foundTemplate  ||  isTemplate  )  ; 
return   null  ; 
} 
} 
File   f  =  new   File  (  selfPath  )  ; 
if  (  f  .  isAbsolute  (  )  ||  isTemplateURL  (  f  )  )  foundTemplate  =  isTemplate  ;  else  { 
foundTemplate  =  (  foundTemplate  ||  isTemplate  )  ; 
if  (  parentPath  ==  null  )  f  =  null  ;  else   f  =  new   File  (  parentPath  ,  selfPath  )  ; 
} 
return   f  ; 
} 

private   void   setTemplatePathVariables  (  Element   n  )  { 
String   attrVal  =  n  .  getAttribute  (  TEMPLATE_VAL_ATTR  )  ; 
if  (  !  XMLUtils  .  hasValue  (  attrVal  )  )  return  ; 
StringTokenizer   values  =  new   StringTokenizer  (  attrVal  ,  ";"  )  ; 
while  (  values  .  hasMoreTokens  (  )  )  setTemplatePathVariable  (  values  .  nextToken  (  )  )  ; 
} 

private   void   setTemplatePathVariable  (  String   valSetting  )  { 
int   bracePos  =  valSetting  .  indexOf  (  ']'  )  ; 
if  (  bracePos  <  1  )  return  ; 
String   valName  =  valSetting  .  substring  (  1  ,  bracePos  )  ; 
int   equalsPos  =  valSetting  .  indexOf  (  '='  ,  bracePos  )  ; 
if  (  equalsPos  ==  -  1  )  return  ; 
String   setting  =  valSetting  .  substring  (  equalsPos  +  1  )  .  trim  (  )  ; 
PathVariable   pathVar  =  getPathVariable  (  valName  )  ; 
pathVar  .  dataName  =  null  ; 
pathVar  .  value  =  setting  ; 
} 



private   PathVariable   getPathVariable  (  String   mname  ,  String   impliedPath  ,  String   defaultValue  )  { 
String   name  =  resolveMetaReferences  (  mname  )  ; 
PathVariable   result  =  (  PathVariable  )  pathVariables  .  get  (  name  )  ; 
if  (  result  ==  null  )  { 
result  =  new   PathVariable  (  name  ,  mname  ,  impliedPath  ,  defaultValue  )  ; 
pathVariables  .  put  (  name  ,  result  )  ; 
pathVariableNames  .  add  (  name  )  ; 
} 
return   result  ; 
} 

private   PathVariable   getPathVariable  (  String   name  )  { 
return   getPathVariable  (  name  ,  null  ,  null  )  ; 
} 


private   String   resolveMetaReferences  (  String   name  )  { 
int   beg  ,  end  ; 
String   metaName  ; 
while  (  (  beg  =  name  .  indexOf  (  '{'  )  )  !=  -  1  )  { 
end  =  name  .  indexOf  (  '}'  ,  beg  )  ; 
metaName  =  name  .  substring  (  beg  +  1  ,  end  )  ; 
PathVariable   pv  =  (  PathVariable  )  metaPathVariables  .  get  (  metaName  )  ; 
if  (  pv  ==  null  )  pv  =  new   PathVariable  (  metaName  ,  metaName  ,  null  ,  ""  )  ; 
if  (  pv  .  isUnknown  (  )  )  { 
metaPathVariables  .  put  (  metaName  ,  pv  )  ; 
metaPathVariableNames  .  add  (  metaName  )  ; 
} 
name  =  name  .  substring  (  0  ,  beg  )  +  pv  .  getValue  (  )  +  name  .  substring  (  end  +  1  )  ; 
} 
name  =  name  .  replace  (  '\t'  ,  ' '  )  .  replace  (  '\n'  ,  ' '  )  .  replace  (  '\r'  ,  ' '  )  .  trim  (  )  ; 
while  (  name  .  indexOf  (  "  "  )  !=  -  1  )  name  =  StringUtils  .  findAndReplace  (  name  ,  "  "  ,  " "  )  ; 
return   name  ; 
} 



class   PathVariable  { 

String   metaName  ,  dataName  ,  value  =  null  ; 

public   PathVariable  (  String   name  ,  String   impliedPath  )  { 
this  (  name  ,  name  ,  impliedPath  ,  null  )  ; 
} 

public   PathVariable  (  String   name  )  { 
this  (  name  ,  name  ,  null  ,  null  )  ; 
} 

public   PathVariable  (  String   name  ,  String   metaName  ,  String   impliedPath  ,  String   defaultValue  )  { 
this  .  metaName  =  metaName  ; 
SaveableData   val  =  null  ; 
DataRepository   data  =  getDataRepository  (  )  ; 
if  (  name  .  startsWith  (  "/"  )  )  { 
val  =  data  .  getSimpleValue  (  dataName  =  name  )  ; 
}  else  { 
StringBuffer   prefix  =  new   StringBuffer  (  getPrefix  (  )  )  ; 
val  =  data  .  getInheritableValue  (  prefix  ,  name  )  ; 
if  (  val  !=  null  &&  !  (  val   instanceof   SimpleData  )  )  val  =  val  .  getSimpleValue  (  )  ; 
dataName  =  data  .  createDataName  (  prefix  .  toString  (  )  ,  name  )  ; 
} 
String   postedValue  =  getParameter  (  name  )  ; 
if  (  postedValue  !=  null  )  { 
value  =  postedValue  ; 
if  (  pathStartsWith  (  value  ,  impliedPath  )  )  { 
value  =  value  .  substring  (  impliedPath  .  length  (  )  )  ; 
if  (  value  .  startsWith  (  File  .  separator  )  )  value  =  value  .  substring  (  1  )  ; 
} 
if  (  !  pathEqual  (  value  ,  defaultValue  )  )  { 
data  .  putValue  (  dataName  ,  StringData  .  create  (  value  )  )  ; 
} 
}  else   if  (  val   instanceof   SimpleData  )  value  =  (  (  SimpleData  )  val  )  .  format  (  )  ; 
if  (  isUnknown  (  )  &&  defaultValue  !=  null  )  value  =  defaultValue  ; 
} 

public   String   getDataname  (  )  { 
return   dataName  ; 
} 

private   String   getValue  (  )  { 
return   value  ; 
} 

private   boolean   isUnknown  (  )  { 
return  (  value  ==  null  ||  value  .  length  (  )  ==  0  ||  value  .  indexOf  (  '?'  )  !=  -  1  )  ; 
} 

private   boolean   pathStartsWith  (  String   path  ,  String   prefix  )  { 
if  (  path  ==  null  ||  prefix  ==  null  )  return   false  ; 
if  (  path  .  length  (  )  <  prefix  .  length  (  )  )  return   false  ; 
return   pathEqual  (  path  .  substring  (  0  ,  prefix  .  length  (  )  )  ,  prefix  )  ; 
} 

private   boolean   pathEqual  (  String   a  ,  String   b  )  { 
if  (  a  ==  null  ||  b  ==  null  )  return   false  ; 
File   aa  =  new   File  (  a  )  ,  bb  =  new   File  (  b  )  ; 
return   aa  .  equals  (  bb  )  ; 
} 

private   String   displayName  =  null  ; 

private   String   commentText  =  null  ; 

public   void   lookupExtraInfo  (  Element   e  )  { 
if  (  e  ==  null  )  return  ; 
Document   doc  =  e  .  getOwnerDocument  (  )  ; 
if  (  doc  ==  null  )  return  ; 
e  =  (  new   FileFinder  (  "["  +  metaName  +  "]"  ,  doc  )  )  .  file  ; 
if  (  e  !=  null  )  { 
displayName  =  e  .  getAttribute  (  "displayName"  )  ; 
if  (  e  .  hasChildNodes  (  )  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
NodeList   list  =  e  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  list  .  getLength  (  )  ;  i  ++  )  buf  .  append  (  list  .  item  (  i  )  .  toString  (  )  )  ; 
commentText  =  buf  .  toString  (  )  ; 
} 
} 
} 

public   String   getDisplayName  (  )  { 
return   displayName  ; 
} 

public   String   getCommentText  (  )  { 
return   commentText  ; 
} 
} 




private   void   sendNoSuchFileMessage  (  String   filename  )  { 
out  .  print  (  "Content-type: text/html\r\n\r\n"  +  "<html><head><title>No such file</title></head>\n"  +  "<body><h1>No such file</h1>\n"  +  "None of the document description files\n"  +  "contain an entry for any file named &quot;"  )  ; 
out  .  print  (  filename  )  ; 
out  .  print  (  "&quot;.</body></html>"  )  ; 
} 

private   void   sendCopyTemplateError  (  String   message  )  { 
out  .  print  (  "Content-type: text/html\r\n\r\n"  +  "<html><head><title>Problem copying template</title></head>\n"  +  "<body><h1>Problem copying template</h1>\n"  )  ; 
out  .  print  (  message  )  ; 
out  .  print  (  "</body></html>"  )  ; 
} 




private   void   displayNeedInfoForm  (  String   filename  ,  File   file  ,  boolean   isTemplate  ,  Element   e  )  { 
out  .  print  (  "Content-type: text/html\r\n\r\n"  +  "<html><head><title>Enter File Information</title></head>\n"  +  "<body><h1>Enter File Information</h1>\n"  )  ; 
if  (  file  !=  null  )  { 
out  .  print  (  "The dashboard tried to find the "  )  ; 
out  .  print  (  isTemplate  ?  "<b>template</b>"  :  "file"  )  ; 
out  .  print  (  " in the following location: <PRE>        "  )  ; 
out  .  print  (  file  .  getPath  (  )  )  ; 
out  .  println  (  "</PRE>but no such file exists.<P>"  )  ; 
} 
out  .  print  (  "Please provide the following information to help locate "  +  "the '"  )  ; 
out  .  print  (  filename  )  ; 
out  .  println  (  isTemplate  ?  "' template."  :  "'."  )  ; 
out  .  print  (  "<form method='POST' action='"  )  ; 
out  .  print  (  (  String  )  env  .  get  (  "SCRIPT_PATH"  )  )  ; 
out  .  println  (  "'><table>"  )  ; 
for  (  int   i  =  0  ;  i  <  pathVariableNames  .  size  (  )  ;  i  ++  )  { 
String   varName  =  (  String  )  pathVariableNames  .  get  (  i  )  ; 
PathVariable   pathVar  =  getPathVariable  (  varName  )  ; 
if  (  pathVar  .  getDataname  (  )  ==  null  )  continue  ; 
pathVar  .  lookupExtraInfo  (  e  )  ; 
out  .  print  (  "<tr><td valign='top'>"  )  ; 
String   displayName  =  pathVar  .  getDisplayName  (  )  ; 
if  (  !  XMLUtils  .  hasValue  (  displayName  )  )  displayName  =  varName  ; 
if  (  displayName  .  startsWith  (  "/"  )  )  displayName  =  displayName  .  substring  (  1  )  ; 
out  .  print  (  TinyWebServer  .  encodeHtmlEntities  (  displayName  )  )  ; 
out  .  print  (  "&nbsp;</td><td valign='top'>"  +  "<input size='40' type='text' name='"  )  ; 
out  .  print  (  TinyWebServer  .  encodeHtmlEntities  (  varName  )  )  ; 
String   value  =  pathVar  .  getValue  (  )  ; 
if  (  value  !=  null  )  { 
out  .  print  (  "' value='"  )  ; 
out  .  print  (  TinyWebServer  .  encodeHtmlEntities  (  value  )  )  ; 
} 
out  .  print  (  "'>"  )  ; 
String   comment  =  pathVar  .  getCommentText  (  )  ; 
if  (  XMLUtils  .  hasValue  (  comment  )  )  { 
out  .  print  (  "<br><i>"  )  ; 
out  .  print  (  comment  )  ; 
out  .  print  (  "</i><br>&nbsp;"  )  ; 
} 
out  .  println  (  "</td></tr>"  )  ; 
} 
out  .  println  (  "</table>"  )  ; 
out  .  print  (  "<input type='hidden' name='"  +  FILE_PARAM  +  "' value='"  )  ; 
out  .  print  (  TinyWebServer  .  encodeHtmlEntities  (  filename  )  )  ; 
out  .  println  (  "'>\n"  +  "<input type='submit' name='OK' value='OK'>\n"  +  "</form></body></html>"  )  ; 
} 




protected   static   Hashtable   documentMap  =  new   Hashtable  (  )  ; 

protected   Document   getDocumentTree  (  String   url  )  throws   IOException  { 
Document   result  =  null  ; 
if  (  parameters  .  get  (  "init"  )  ==  null  )  result  =  (  Document  )  documentMap  .  get  (  url  )  ; 
if  (  result  ==  null  )  try  { 
result  =  XMLUtils  .  parse  (  new   ByteArrayInputStream  (  getRequest  (  url  ,  true  )  )  )  ; 
documentMap  .  put  (  url  ,  result  )  ; 
}  catch  (  SAXException   se  )  { 
return   null  ; 
} 
return   result  ; 
} 





protected   Element   findFile  (  String   name  )  throws   IOException  { 
DataRepository   data  =  getDataRepository  (  )  ; 
StringBuffer   prefix  =  new   StringBuffer  (  getPrefix  (  )  )  ; 
ListData   list  ; 
Element   result  =  null  ; 
SaveableData   val  ; 
for  (  val  =  data  .  getInheritableValue  (  prefix  ,  FILE_XML_DATANAME  )  ;  val  !=  null  ;  val  =  data  .  getInheritableValue  (  chop  (  prefix  )  ,  FILE_XML_DATANAME  )  )  { 
if  (  val  !=  null  &&  !  (  val   instanceof   SimpleData  )  )  val  =  val  .  getSimpleValue  (  )  ; 
if  (  val   instanceof   StringData  )  list  =  (  (  StringData  )  val  )  .  asList  (  )  ;  else   if  (  val   instanceof   ListData  )  list  =  (  ListData  )  val  ;  else   list  =  null  ; 
if  (  list  !=  null  )  for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  { 
String   url  =  (  String  )  list  .  get  (  i  )  ; 
Document   docList  =  getDocumentTree  (  url  )  ; 
if  (  docList  !=  null  )  { 
result  =  (  new   FileFinder  (  name  ,  docList  )  )  .  file  ; 
if  (  result  !=  null  )  return   result  ; 
} 
} 
if  (  prefix  .  length  (  )  ==  0  )  break  ; 
} 
return   null  ; 
} 

private   StringBuffer   chop  (  StringBuffer   buf  )  { 
int   slashPos  =  buf  .  toString  (  )  .  lastIndexOf  (  '/'  )  ; 
buf  .  setLength  (  slashPos  ==  -  1  ?  0  :  slashPos  )  ; 
return   buf  ; 
} 

class   FileFinder   extends   XMLDepthFirstIterator  { 

String   name  ; 

Element   file  =  null  ; 

public   FileFinder  (  String   name  ,  Document   docTree  )  { 
this  .  name  =  name  ; 
run  (  docTree  )  ; 
} 

public   void   caseElement  (  Element   e  ,  List   path  )  { 
if  (  name  .  equalsIgnoreCase  (  e  .  getAttribute  (  NAME_ATTR  )  )  ||  name  .  equalsIgnoreCase  (  e  .  getAttribute  (  TEMPLATE_PATH_ATTR  )  )  )  file  =  e  ; 
} 
} 
} 

