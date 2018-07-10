package   com  .  objectwave  .  templateMerge  ; 

import   com  .  objectwave  .  exception  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Vector  ; 





public   class   MergeTemplateWriter  { 




static   java  .  util  .  Hashtable   templateRefs  =  new   java  .  util  .  Hashtable  (  )  ; 

static   InformationToken  [  ]  tokenList  =  new   InformationToken  [  0  ]  ; 






public   static   void   setTokenList  (  InformationToken  [  ]  toks  )  { 
tokenList  =  toks  ; 
} 









public   static   InformationToken   getToken  (  String   name  )  { 
InformationToken  [  ]  tokens  =  getTokenList  (  )  ; 
for  (  int   i  =  0  ;  i  <  tokens  .  length  ;  i  ++  )  { 
if  (  tokens  [  i  ]  !=  null  &&  tokens  [  i  ]  .  getTokenString  (  )  .  equals  (  name  )  )  { 
return   tokens  [  i  ]  ; 
} 
} 
return   null  ; 
} 






public   static   InformationToken  [  ]  getTokenList  (  )  { 
return   tokenList  ; 
} 







public   static   void   containsSubTemplates  (  MergeTemplate   result  ,  Vector   templateNames  )  { 
templateRefs  .  put  (  result  ,  templateNames  )  ; 
} 









public   static   void   dumpToText  (  Enumeration   e  ,  java  .  io  .  BufferedWriter   buff  )  throws   IOException  { 
MergeTemplateWriter   writer  =  new   MergeTemplateWriter  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
MergeTemplate   t  =  (  MergeTemplate  )  e  .  nextElement  (  )  ; 
writer  .  writeOut  (  t  ,  buff  )  ; 
} 
} 










public   static   void   dumpToXML  (  Enumeration   e  ,  java  .  io  .  BufferedWriter   buff  )  throws   ConfigurationException  { 
XMLTemplateWriter   xmlWriter  =  new   XMLTemplateWriter  (  e  ,  buff  )  ; 
} 








public   static   KnownTemplates   importFromText  (  java  .  io  .  BufferedReader   buff  )  throws   java  .  io  .  IOException  { 
MergeTemplateWriter   writer  =  new   MergeTemplateWriter  (  )  ; 
KnownTemplates   workingTemplates  =  new   KnownTemplates  (  )  ; 
while  (  true  )  { 
MergeTemplate   t  =  writer  .  readFrom  (  buff  )  ; 
System  .  out  .  println  (  "Template Read "  +  t  )  ; 
if  (  t  ==  null  )  { 
break  ; 
} 
workingTemplates  .  addTemplate  (  t  )  ; 
} 
writer  .  realizeTemplateCache  (  workingTemplates  )  ; 
return   workingTemplates  ; 
} 

public   static   KnownTemplates   importFromXML  (  Reader   xmlFile  )  throws   ConfigurationException  { 
MergeTemplateWriter   writer  =  new   MergeTemplateWriter  (  )  ; 
XMLTemplateRead   xtr  =  new   XMLTemplateRead  (  xmlFile  )  ; 
KnownTemplates   workingTemplates  =  xtr  .  getTemplates  (  )  ; 
writer  .  realizeTemplateCache  (  workingTemplates  )  ; 
return   workingTemplates  ; 
} 








public   static   void   writeOut  (  MergeTemplate   temp  ,  BufferedWriter   buff  )  throws   IOException  { 
buff  .  write  (  "---------------"  )  ; 
buff  .  write  (  temp  .  getTemplateName  (  )  )  ; 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "PRE"  )  ; 
buff  .  newLine  (  )  ; 
InformationToken  [  ]  toks  =  temp  .  getPreTokens  (  )  ; 
if  (  toks  ==  null  )  { 
toks  =  new   InformationToken  [  0  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  toks  .  length  ;  i  ++  )  { 
if  (  toks  [  i  ]  !=  null  )  { 
buff  .  write  (  toks  [  i  ]  .  getTokenString  (  )  )  ; 
buff  .  newLine  (  )  ; 
} 
} 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "'"  +  temp  .  getPreBody  (  )  +  "'"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
MergeTemplate  [  ]  temps  =  temp  .  getTemplates  (  )  ; 
if  (  temps  ==  null  )  { 
temps  =  new   MergeTemplate  [  0  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  temps  .  length  ;  i  ++  )  { 
buff  .  write  (  temps  [  i  ]  .  getTemplateName  (  )  )  ; 
buff  .  newLine  (  )  ; 
} 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "POST"  )  ; 
buff  .  newLine  (  )  ; 
toks  =  temp  .  getPostTokens  (  )  ; 
if  (  toks  ==  null  )  { 
toks  =  new   InformationToken  [  0  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  toks  .  length  ;  i  ++  )  { 
if  (  toks  [  i  ]  !=  null  )  { 
buff  .  write  (  toks  [  i  ]  .  getTokenString  (  )  )  ; 
buff  .  newLine  (  )  ; 
} 
} 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "'"  +  temp  .  getPostBody  (  )  +  "'"  )  ; 
buff  .  newLine  (  )  ; 
buff  .  write  (  "---------------"  )  ; 
buff  .  newLine  (  )  ; 
} 










public   MergeTemplate   readFrom  (  BufferedReader   rdr  )  throws   IOException  { 
MergeTemplate   result  =  new   MergeTemplate  (  )  ; 
String   line  =  rdr  .  readLine  (  )  ; 
if  (  line  ==  null  )  { 
return   null  ; 
} 
readName  (  result  ,  line  )  ; 
line  =  rdr  .  readLine  (  )  ; 
if  (  !  readTokens  (  result  ,  rdr  )  )  { 
return   null  ; 
} 
if  (  !  readTemplates  (  result  ,  rdr  )  )  { 
return   null  ; 
} 
if  (  !  readTokens  (  result  ,  rdr  )  )  { 
return   null  ; 
} 
return   result  ; 
} 









protected   String   readBody  (  MergeTemplate   result  ,  BufferedReader   rdr  )  throws   IOException  { 
String   line  =  rdr  .  readLine  (  )  ; 
StringBuffer   buff  =  new   StringBuffer  (  line  )  ; 
while  (  line  !=  null  )  { 
line  =  rdr  .  readLine  (  )  ; 
int   idx  =  line  .  indexOf  (  "---------------"  )  ; 
if  (  idx  >  -  1  )  { 
char  [  ]  chars  =  new   char  [  buff  .  length  (  )  -  2  ]  ; 
buff  .  getChars  (  1  ,  buff  .  length  (  )  -  1  ,  chars  ,  0  )  ; 
return   new   String  (  chars  )  ; 
} 
buff  .  append  (  '\n'  )  ; 
buff  .  append  (  line  )  ; 
} 
return   null  ; 
} 







protected   void   readName  (  MergeTemplate   result  ,  String   line  )  { 
String   border  =  "---------------"  ; 
int   x  =  border  .  length  (  )  ; 
int   idx  =  line  .  lastIndexOf  (  border  )  ; 
result  .  setTemplateName  (  line  .  substring  (  x  ,  idx  )  )  ; 
} 









protected   boolean   readTemplates  (  MergeTemplate   result  ,  BufferedReader   rdr  )  throws   IOException  { 
String   line  =  rdr  .  readLine  (  )  ; 
Vector   templateCacheNames  =  null  ; 
while  (  line  !=  null  )  { 
int   idx  =  line  .  indexOf  (  "---------------"  )  ; 
if  (  idx  >  -  1  )  { 
if  (  templateCacheNames  !=  null  )  { 
containsSubTemplates  (  result  ,  templateCacheNames  )  ; 
} 
return   true  ; 
} 
if  (  templateCacheNames  ==  null  )  { 
templateCacheNames  =  new   Vector  (  )  ; 
} 
templateCacheNames  .  addElement  (  line  )  ; 
line  =  rdr  .  readLine  (  )  ; 
} 
return   false  ; 
} 









protected   boolean   readTokens  (  MergeTemplate   result  ,  BufferedReader   rdr  )  throws   IOException  { 
String   tok  =  rdr  .  readLine  (  )  ; 
String   line  =  rdr  .  readLine  (  )  ; 
Vector   tokens  =  new   Vector  (  )  ; 
while  (  line  !=  null  )  { 
int   idx  =  line  .  indexOf  (  "---------------"  )  ; 
if  (  idx  >  -  1  )  { 
String   body  =  readBody  (  result  ,  rdr  )  ; 
if  (  body  ==  null  )  { 
return   false  ; 
} 
if  (  body  .  equals  (  "null"  )  )  { 
body  =  null  ; 
} 
InformationToken  [  ]  toks  =  new   InformationToken  [  tokens  .  size  (  )  ]  ; 
tokens  .  copyInto  (  toks  )  ; 
if  (  tok  .  equals  (  "PRE"  )  )  { 
result  .  setPreTokens  (  toks  )  ; 
result  .  setPreBody  (  body  )  ; 
}  else  { 
result  .  setPostTokens  (  toks  )  ; 
result  .  setPostBody  (  body  )  ; 
} 
return   true  ; 
} 
InformationToken   token  =  getToken  (  line  )  ; 
if  (  token  !=  null  )  { 
tokens  .  addElement  (  token  )  ; 
} 
line  =  rdr  .  readLine  (  )  ; 
} 
return   false  ; 
} 






protected   void   realizeTemplateCache  (  KnownTemplates   known  )  { 
Enumeration   e  =  templateRefs  .  keys  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
MergeTemplate   template  =  (  MergeTemplate  )  e  .  nextElement  (  )  ; 
Vector   templateCacheNames  =  (  Vector  )  templateRefs  .  get  (  template  )  ; 
int   size  =  templateCacheNames  .  size  (  )  ; 
Vector   result  =  new   Vector  (  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
MergeTemplate   t  =  known  .  getTemplate  (  (  String  )  templateCacheNames  .  elementAt  (  i  )  )  ; 
if  (  t  !=  null  )  { 
result  .  addElement  (  t  )  ; 
} 
} 
MergeTemplate  [  ]  temps  =  new   MergeTemplate  [  result  .  size  (  )  ]  ; 
result  .  copyInto  (  temps  )  ; 
template  .  setTemplates  (  temps  )  ; 
} 
} 
} 

