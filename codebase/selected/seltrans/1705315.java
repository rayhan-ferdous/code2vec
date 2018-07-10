package   javax  .  help  ; 

import   java  .  awt  .  Component  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Stack  ; 
import   java  .  util  .  Enumeration  ; 
import   javax  .  help  .  Map  .  ID  ; 
import   javax  .  swing  .  tree  .  TreeNode  ; 
import   javax  .  swing  .  tree  .  DefaultMutableTreeNode  ; 
import   com  .  sun  .  java  .  help  .  impl  .  *  ; 







public   class   FavoritesView   extends   NavigatorView  { 




public   static   final   String   publicIDString  =  "-//Sun Microsystems Inc.//DTD JavaHelp Favorites Version 2.0//EN"  ; 

private   static   boolean   warningOfFailures  =  false  ; 




private   HelpSet   hs  ; 




private   boolean   enabledSave  =  true  ; 











public   FavoritesView  (  HelpSet   hs  ,  String   name  ,  String   label  ,  Hashtable   params  )  { 
super  (  hs  ,  name  ,  label  ,  hs  .  getLocale  (  )  ,  params  )  ; 
} 












public   FavoritesView  (  HelpSet   hs  ,  String   name  ,  String   label  ,  Locale   locale  ,  Hashtable   params  )  { 
super  (  hs  ,  name  ,  label  ,  locale  ,  params  )  ; 
} 







public   Component   createNavigator  (  HelpModel   model  )  { 
return   new   JHelpFavoritesNavigator  (  this  ,  model  )  ; 
} 




public   String   getMergeType  (  )  { 
String   mergeType  =  super  .  getMergeType  (  )  ; 
if  (  mergeType  ==  null  )  { 
return  "javax.help.NoMerge"  ; 
} 
return   mergeType  ; 
} 









public   FavoritesNode   getDataAsTree  (  )  { 
HelpSet   hs  =  getHelpSet  (  )  ; 
debug  (  "helpSet in "  +  this  +  hs  .  toString  (  )  )  ; 
return  (  FavoritesNode  )  parse  (  hs  ,  hs  .  getLocale  (  )  ,  new   DefaultFavoritesFactory  (  )  )  ; 
} 
















public   FavoritesNode   parse  (  HelpSet   hs  ,  Locale   locale  ,  TreeItemFactory   factory  )  { 
Reader   src  ; 
DefaultMutableTreeNode   node  =  null  ; 
URL   url  =  null  ; 
try  { 
String   user_dir  =  System  .  getProperty  (  "user.home"  )  ; 
File   file  =  new   File  (  user_dir  +  File  .  separator  +  ".JavaHelp"  +  File  .  separator  +  "Favorites.xml"  )  ; 
if  (  !  file  .  exists  (  )  )  return   new   FavoritesNode  (  new   FavoritesItem  (  "Favorites"  )  )  ; 
try  { 
url  =  file  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   e  )  { 
System  .  err  .  println  (  e  )  ; 
} 
URLConnection   uc  =  url  .  openConnection  (  )  ; 
src  =  XmlReader  .  createReader  (  uc  )  ; 
factory  .  parsingStarted  (  url  )  ; 
node  =  (  new   FavoritesParser  (  factory  )  )  .  parse  (  src  ,  hs  ,  locale  )  ; 
src  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
factory  .  reportMessage  (  "Exception caught while parsing "  +  url  +  e  .  toString  (  )  ,  false  )  ; 
} 
return  (  FavoritesNode  )  factory  .  parsingEnded  (  node  )  ; 
} 






public   void   saveFavorites  (  FavoritesNode   node  )  { 
if  (  !  enabledSave  )  return  ; 
try  { 
FileOutputStream   out  ; 
String   user_dir  =  System  .  getProperty  (  "user.home"  )  ; 
File   file  =  new   File  (  user_dir  +  File  .  separator  +  ".JavaHelp"  )  ; 
file  .  mkdirs  (  )  ; 
String   userFile  =  file  .  getPath  (  )  +  File  .  separator  +  "Favorites.xml"  ; 
debug  (  "new file:"  +  userFile  )  ; 
node  .  export  (  out  =  new   FileOutputStream  (  userFile  )  )  ; 
out  .  close  (  )  ; 
}  catch  (  SecurityException   se  )  { 
enabledSave  =  false  ; 
se  .  printStackTrace  (  )  ; 
}  catch  (  Exception   excp  )  { 
excp  .  printStackTrace  (  )  ; 
} 
} 





public   static   class   DefaultFavoritesFactory   implements   TreeItemFactory  { 

private   Vector   messages  =  new   Vector  (  )  ; 

private   URL   source  ; 

private   boolean   validParse  =  true  ; 




public   void   parsingStarted  (  URL   source  )  { 
if  (  source  ==  null  )  { 
throw   new   NullPointerException  (  "source"  )  ; 
} 
this  .  source  =  source  ; 
} 




public   void   processDOCTYPE  (  String   root  ,  String   publicID  ,  String   systemID  )  { 
if  (  publicID  ==  null  ||  !  publicID  .  equals  (  publicIDString  )  )  { 
reportMessage  (  HelpUtilities  .  getText  (  "favorites.invalidFavoritesFormat"  ,  publicID  )  ,  false  )  ; 
} 
} 




public   void   processPI  (  HelpSet   hs  ,  String   target  ,  String   data  )  { 
} 













public   TreeItem   createItem  (  String   tagName  ,  Hashtable   atts  ,  HelpSet   hs  ,  Locale   locale  )  { 
if  (  tagName  ==  null  ||  !  tagName  .  equals  (  "favoriteitem"  )  )  { 
throw   new   IllegalArgumentException  (  "tagName"  )  ; 
} 
FavoritesItem   item  =  null  ; 
String   target  =  null  ; 
String   name  =  null  ; 
String   mergeType  =  null  ; 
String   url  =  null  ; 
String   hstitle  =  null  ; 
if  (  atts  !=  null  )  { 
target  =  (  String  )  atts  .  get  (  "target"  )  ; 
debug  (  "target:"  +  target  )  ; 
name  =  (  String  )  atts  .  get  (  "text"  )  ; 
url  =  (  String  )  atts  .  get  (  "url"  )  ; 
hstitle  =  (  String  )  atts  .  get  (  "hstitle"  )  ; 
item  =  new   FavoritesItem  (  name  ,  target  ,  url  ,  hstitle  ,  locale  )  ; 
if  (  (  item  .  getTarget  (  )  ==  null  )  &&  (  item  .  getURLSpec  (  )  ==  null  )  )  item  .  setAsFolder  (  )  ; 
}  else   item  =  new   FavoritesItem  (  )  ; 
return   item  ; 
} 




public   TreeItem   createItem  (  )  { 
debug  (  "empty item created"  )  ; 
return   new   FavoritesItem  (  )  ; 
} 




public   void   reportMessage  (  String   msg  ,  boolean   validParse  )  { 
messages  .  addElement  (  msg  )  ; 
this  .  validParse  =  this  .  validParse  &&  validParse  ; 
} 




public   Enumeration   listMessages  (  )  { 
return   messages  .  elements  (  )  ; 
} 










public   DefaultMutableTreeNode   parsingEnded  (  DefaultMutableTreeNode   node  )  { 
DefaultMutableTreeNode   back  =  node  ; 
if  (  !  validParse  )  { 
back  =  null  ; 
System  .  err  .  println  (  "Parsing failed for "  +  source  )  ; 
for  (  Enumeration   e  =  messages  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   msg  =  (  String  )  e  .  nextElement  (  )  ; 
System  .  err  .  println  (  msg  )  ; 
} 
} 
return   back  ; 
} 
} 








private   static   class   FavoritesParser   implements   ParserListener  { 

private   HelpSet   currentParseHS  ; 

private   Stack   nodeStack  ; 

private   Stack   itemStack  ; 

private   Stack   tagStack  ; 

private   Locale   defaultLocale  ; 

private   Locale   lastLocale  ; 

private   boolean   startedfavorites  ; 

private   TreeItemFactory   factory  ; 







FavoritesParser  (  TreeItemFactory   factory  )  { 
this  .  factory  =  factory  ; 
} 





synchronized   FavoritesNode   parse  (  Reader   src  ,  HelpSet   context  ,  Locale   locale  )  throws   IOException  { 
nodeStack  =  new   Stack  (  )  ; 
itemStack  =  new   Stack  (  )  ; 
tagStack  =  new   Stack  (  )  ; 
if  (  locale  ==  null  )  { 
defaultLocale  =  Locale  .  getDefault  (  )  ; 
}  else  { 
defaultLocale  =  locale  ; 
} 
lastLocale  =  defaultLocale  ; 
FavoritesNode   node  =  new   FavoritesNode  (  new   FavoritesItem  (  "Favorites"  )  )  ; 
nodeStack  .  push  (  node  )  ; 
currentParseHS  =  context  ; 
Parser   parser  =  new   Parser  (  src  )  ; 
parser  .  addParserListener  (  this  )  ; 
parser  .  parse  (  )  ; 
return   node  ; 
} 




public   void   tagFound  (  ParserEvent   e  )  { 
Locale   locale  =  null  ; 
Tag   tag  =  e  .  getTag  (  )  ; 
TagProperties   attr  =  tag  .  atts  ; 
if  (  attr  !=  null  )  { 
String   lang  =  attr  .  getProperty  (  "xml:lang"  )  ; 
locale  =  HelpUtilities  .  localeFromLang  (  lang  )  ; 
} 
if  (  locale  ==  null  )  { 
locale  =  lastLocale  ; 
} 
if  (  tag  .  name  .  equals  (  "favoriteitem"  )  )  { 
if  (  !  startedfavorites  )  { 
factory  .  reportMessage  (  HelpUtilities  .  getText  (  "favorites.invalidFavoritesFormat"  )  ,  false  )  ; 
} 
if  (  tag  .  isEnd  &&  !  tag  .  isEmpty  )  { 
nodeStack  .  pop  (  )  ; 
itemStack  .  pop  (  )  ; 
removeTag  (  tag  )  ; 
return  ; 
} 
TreeItem   item  =  null  ; 
try  { 
Hashtable   t  =  null  ; 
if  (  attr  !=  null  )  { 
t  =  attr  .  getHashtable  (  )  ; 
} 
item  =  factory  .  createItem  (  "favoriteitem"  ,  t  ,  currentParseHS  ,  locale  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  warningOfFailures  )  { 
String   id  =  null  ; 
if  (  attr  !=  null  )  { 
id  =  attr  .  getProperty  (  "target"  )  ; 
} 
System  .  err  .  println  (  "Failure in FavoritesItem Creation; "  )  ; 
System  .  err  .  println  (  "  id: "  +  id  )  ; 
System  .  err  .  println  (  "  hs: "  +  currentParseHS  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
debug  (  "empty item !"  )  ; 
item  =  factory  .  createItem  (  )  ; 
} 
FavoritesNode   node  =  new   FavoritesNode  (  (  FavoritesItem  )  item  )  ; 
FavoritesNode   parent  =  (  FavoritesNode  )  nodeStack  .  peek  (  )  ; 
parent  .  add  (  node  )  ; 
if  (  !  tag  .  isEmpty  )  { 
itemStack  .  push  (  item  )  ; 
nodeStack  .  push  (  node  )  ; 
addTag  (  tag  ,  locale  )  ; 
} 
}  else   if  (  tag  .  name  .  equals  (  "favorites"  )  )  { 
if  (  !  tag  .  isEnd  )  { 
if  (  attr  !=  null  )  { 
String   version  =  attr  .  getProperty  (  "version"  )  ; 
if  (  version  !=  null  &&  version  .  compareTo  (  "2.0"  )  !=  0  )  { 
factory  .  reportMessage  (  HelpUtilities  .  getText  (  "favorites.unknownVersion"  ,  version  )  ,  false  )  ; 
} 
} 
if  (  startedfavorites  )  { 
factory  .  reportMessage  (  HelpUtilities  .  getText  (  "favorites.invalidFavoritesFormat"  )  ,  false  )  ; 
} 
startedfavorites  =  true  ; 
addTag  (  tag  ,  locale  )  ; 
}  else  { 
if  (  startedfavorites  )  { 
startedfavorites  =  false  ; 
} 
removeTag  (  tag  )  ; 
} 
return  ; 
} 
} 




public   void   piFound  (  ParserEvent   e  )  { 
} 




public   void   doctypeFound  (  ParserEvent   e  )  { 
factory  .  processDOCTYPE  (  e  .  getRoot  (  )  ,  e  .  getPublicId  (  )  ,  e  .  getSystemId  (  )  )  ; 
} 




public   void   textFound  (  ParserEvent   e  )  { 
if  (  tagStack  .  empty  (  )  )  { 
return  ; 
} 
LangElement   le  =  (  LangElement  )  tagStack  .  peek  (  )  ; 
Tag   tag  =  (  Tag  )  le  .  getTag  (  )  ; 
if  (  tag  .  name  .  equals  (  "favoriteitem"  )  )  { 
FavoritesItem   item  =  (  FavoritesItem  )  itemStack  .  peek  (  )  ; 
String   oldName  =  item  .  getName  (  )  ; 
if  (  oldName  ==  null  )  { 
item  .  setName  (  e  .  getText  (  )  .  trim  (  )  )  ; 
}  else  { 
item  .  setName  (  oldName  .  concat  (  e  .  getText  (  )  )  .  trim  (  )  )  ; 
} 
} 
} 

public   void   commentFound  (  ParserEvent   e  )  { 
} 

public   void   errorFound  (  ParserEvent   e  )  { 
factory  .  reportMessage  (  e  .  getText  (  )  ,  false  )  ; 
} 




protected   void   addTag  (  Tag   tag  ,  Locale   locale  )  { 
LangElement   el  =  new   LangElement  (  tag  ,  locale  )  ; 
tagStack  .  push  (  el  )  ; 
if  (  lastLocale  ==  null  )  { 
lastLocale  =  locale  ; 
return  ; 
} 
if  (  locale  ==  null  )  { 
lastLocale  =  locale  ; 
return  ; 
} 
if  (  !  lastLocale  .  equals  (  locale  )  )  { 
lastLocale  =  locale  ; 
} 
} 





protected   void   removeTag  (  Tag   tag  )  { 
LangElement   el  ; 
String   name  =  tag  .  name  ; 
Locale   newLocale  =  null  ; 
for  (  ;  ;  )  { 
if  (  tagStack  .  empty  (  )  )  break  ; 
el  =  (  LangElement  )  tagStack  .  pop  (  )  ; 
if  (  !  el  .  getTag  (  )  .  name  .  equals  (  name  )  )  { 
if  (  tagStack  .  empty  (  )  )  { 
newLocale  =  defaultLocale  ; 
}  else  { 
el  =  (  LangElement  )  tagStack  .  peek  (  )  ; 
newLocale  =  el  .  getLocale  (  )  ; 
} 
break  ; 
} 
} 
if  (  lastLocale  ==  null  )  { 
lastLocale  =  newLocale  ; 
return  ; 
} 
if  (  newLocale  ==  null  )  { 
lastLocale  =  newLocale  ; 
return  ; 
} 
if  (  !  lastLocale  .  equals  (  newLocale  )  )  { 
lastLocale  =  newLocale  ; 
} 
} 
} 




private   static   final   boolean   debug  =  false  ; 

private   static   void   debug  (  String   msg  )  { 
if  (  debug  )  { 
System  .  err  .  println  (  "FavoritesView: "  +  msg  )  ; 
} 
} 
} 

