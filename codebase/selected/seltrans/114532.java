package   hambo  .  xpres  ; 

import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Vector  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  IOException  ; 
import   org  .  w3c  .  dom  .  *  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  apache  .  xerces  .  parsers  .  DOMParser  ; 
import   hambo  .  app  .  util  .  DOMUtil  ; 
import   hambo  .  util  .  XMLUtil  ; 
import   hambo  .  util  .  TaskMgr  ; 
import   hambo  .  util  .  Task  ; 
import   hambo  .  xpres  .  XpresApplication  ; 






public   class   NewsHolder   extends   Task  { 

private   static   NewsHolder   instance  =  new   NewsHolder  (  )  ; 

Document   doc  ; 

final   String   newsFile  =  XpresApplication  .  NEWS_FILE  .  trim  (  )  ; 

long   reloadTime  =  15  *  60000  ; 

public   HashMap   bookmarksHT  =  new   HashMap  (  )  ; 

public   HashSet   languages  =  new   HashSet  (  )  ; 

public   String   nodeDelim  =  PropertyHandler  .  getNodeDelim  (  )  ; 

public   HashSet   applications  =  PropertyHandler  .  getApplicationsHS  (  )  ; 





private   NewsHolder  (  )  { 
execute  (  )  ; 
TaskMgr  .  getTaskMgr  (  )  .  executePeriodically  (  reloadTime  ,  this  ,  false  )  ; 
try  { 
reloadTime  =  Long  .  parseLong  (  XpresApplication  .  RELOAD_TIME  .  trim  (  )  )  *  60000  ; 
}  catch  (  Exception   e  )  { 
System  .  err  .  println  (  "NewsHolder.NewsHolder() : couldn't get xpres reload time from Portal"  )  ; 
} 
} 




public   static   NewsHolder   getInstance  (  )  { 
return   instance  ; 
} 





public   void   execute  (  )  { 
long   time  =  System  .  currentTimeMillis  (  )  ; 
Document   newDoc  =  null  ; 
InputStream   in  =  null  ; 
try  { 
in  =  openReader  (  XpresApplication  .  SERVER  +  newsFile  )  ; 
DOMParser   parser  =  new   DOMParser  (  )  ; 
parser  .  parse  (  new   InputSource  (  in  )  )  ; 
newDoc  =  parser  .  getDocument  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
if  (  newDoc  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder: ParserToolXML: doc is null"  )  ; 
System  .  err  .  println  (  "NewsHolder: Document will not change..."  )  ; 
}  else  { 
synchronized  (  this  )  { 
doc  =  newDoc  ; 
} 
HashMap   newBookmarksHT  =  new   HashMap  (  )  ; 
updateBookmarks  (  doc  .  getDocumentElement  (  )  ,  newBookmarksHT  )  ; 
HashSet   newLanguages  =  PropertyHandler  .  getLanguagesHS  (  )  ; 
synchronized  (  this  )  { 
bookmarksHT  =  newBookmarksHT  ; 
languages  =  newLanguages  ; 
} 
System  .  out  .  println  (  "NewsHolder.execute(): Time to fetch content: "  +  (  System  .  currentTimeMillis  (  )  -  time  )  )  ; 
} 
} 






void   updateBookmarks  (  Node   elem  ,  HashMap   bmHT  )  { 
if  (  elem  !=  null  &&  elem  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
String   bm  =  XMLUtil  .  shadow  (  (  (  Element  )  elem  )  .  getAttribute  (  "bookmark"  )  .  trim  (  )  .  toLowerCase  (  )  .  replace  (  ' '  ,  '_'  )  )  ; 
if  (  !  bm  .  equals  (  ""  )  )  { 
bmHT  .  put  (  bm  ,  getNewsLink  (  (  Element  )  elem  )  )  ; 
} 
NodeList   nl  =  elem  .  getChildNodes  (  )  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  updateBookmarks  (  nl  .  item  (  i  )  ,  bmHT  )  ; 
} 
} 





InputStream   openReader  (  String   s  )  { 
System  .  err  .  println  (  "Fetcher: trying url "  +  s  )  ; 
try  { 
URL   url  =  new   URL  (  s  )  ; 
HttpURLConnection   con  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
return   url  .  openStream  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   null  ; 
} 




public   final   Node   getDocument  (  )  { 
if  (  doc  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder.getNodes(): Warning! Document doc is null."  )  ; 
return   null  ; 
} 
return   doc  ; 
} 






public   final   Node   getNode  (  String   appId  ,  String   nodeId  )  { 
if  (  doc  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder.getNodes(): Warning! Document doc is null."  )  ; 
return   null  ; 
} 
String   idStr  =  appId  ; 
if  (  nodeId  !=  null  &&  !  nodeId  .  equals  (  ""  )  )  { 
idStr  =  appId  +  nodeDelim  +  nodeId  ; 
} 
return   DOMUtil  .  getElementById  (  doc  ,  idStr  )  ; 
} 






public   final   NodeList   getNodes  (  String   appId  ,  String   nodeId  )  { 
if  (  doc  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder.getNodes(): Warning! Document doc is null."  )  ; 
return   null  ; 
} 
String   idStr  =  appId  ; 
if  (  nodeId  !=  null  &&  !  nodeId  .  equals  (  ""  )  )  { 
idStr  =  appId  +  nodeDelim  +  nodeId  ; 
} 
if  (  DOMUtil  .  getElementById  (  doc  ,  idStr  )  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder.getNodes(): Warning! Can't find element in doc with tag id: "  +  idStr  +  "."  )  ; 
return   getRootNodes  (  appId  )  ; 
} 
return   DOMUtil  .  getElementById  (  doc  ,  idStr  )  .  getChildNodes  (  )  ; 
} 




public   final   NodeList   getRootNodes  (  String   appId  )  { 
if  (  doc  ==  null  )  { 
System  .  err  .  println  (  "NewsHolder.getRootNodes(): Warning! Document doc is null."  )  ; 
return   null  ; 
} 
return   DOMUtil  .  getElementById  (  doc  ,  appId  )  .  getChildNodes  (  )  ; 
} 






public   NewsLink   getNewsLink  (  Element   elem  )  { 
NewsLink   nloOut  =  null  ; 
if  (  elem  !=  null  )  { 
String   nodeName  =  elem  .  getNodeName  (  )  .  toLowerCase  (  )  ; 
if  (  nodeName  .  equals  (  "root"  )  )  { 
nloOut  =  new   CategoryNewsLink  (  elem  )  ; 
}  else   if  (  nodeName  .  equals  (  "externallink"  )  )  { 
nloOut  =  new   ExternalNewsLink  (  elem  )  ; 
}  else   if  (  nodeName  .  equals  (  "internallink"  )  )  { 
nloOut  =  new   InternalNewsLink  (  elem  )  ; 
}  else   if  (  nodeName  .  equals  (  "category"  )  )  { 
nloOut  =  new   CategoryNewsLink  (  elem  )  ; 
}  else   if  (  nodeName  .  equals  (  "headline"  )  )  { 
nloOut  =  new   StoryNewsLink  (  getDefaultStoryFromHeadline  (  elem  )  )  ; 
}  else   if  (  nodeName  .  equals  (  "story"  )  )  { 
nloOut  =  new   StoryNewsLink  (  elem  )  ; 
} 
} 
if  (  nloOut  ==  null  )  System  .  err  .  println  (  "NewsHolder.getNewsLink(): Warning! null NewsLink returned."  )  ; 
return   nloOut  ; 
} 







public   NewsLink   getNewsLink  (  String   appId  ,  String   nodeId  )  { 
Element   elem  =  (  Element  )  getNode  (  appId  ,  nodeId  )  ; 
return   getNewsLink  (  elem  )  ; 
} 







public   Vector   getNewsLinks  (  String   appId  ,  String   nodeId  )  { 
Vector   outV  =  new   Vector  (  )  ; 
NodeList   nl  =  getNodes  (  appId  ,  nodeId  )  ; 
if  (  nl  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   node  =  nl  .  item  (  i  )  ; 
if  (  node  !=  null  &&  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
if  (  PropertyHandler  .  getAllowedLinksHS  (  )  .  contains  (  node  .  getNodeName  (  )  )  )  { 
NewsLink   nwlk  =  getNewsLink  (  (  Element  )  node  )  ; 
if  (  nwlk  !=  null  )  outV  .  add  (  nwlk  )  ; 
} 
} 
} 
} 
return   outV  ; 
} 









public   Vector   getNewsLinks  (  String   appId  ,  String   nodeId  ,  String   lang_select  )  { 
if  (  lang_select  ==  null  ||  !  languages  .  contains  (  lang_select  )  )  { 
return   getNewsLinks  (  appId  ,  nodeId  )  ; 
} 
Vector   outV  =  new   Vector  (  )  ; 
NodeList   nl  =  getNodes  (  appId  ,  nodeId  )  ; 
if  (  nl  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   node  =  nl  .  item  (  i  )  ; 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
if  (  PropertyHandler  .  getAllowedLinksHS  (  )  .  contains  (  node  .  getNodeName  (  )  )  )  { 
NewsLink   nwlk  =  getNewsLink  (  (  Element  )  node  )  ; 
if  (  nwlk  !=  null  &&  nwlk  .  supportsLanguage  (  lang_select  )  )  { 
outV  .  add  (  nwlk  )  ; 
} 
} 
} 
} 
} 
return   outV  ; 
} 







public   StoryObject   getStoryObject  (  String   appId  ,  String   nodeId  )  { 
Node   node  =  getNode  (  appId  ,  nodeId  )  ; 
StoryObject   storyObject  =  null  ; 
Element   storyElem  =  null  ; 
if  (  node  !=  null  &&  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  node  .  getNodeName  (  )  .  toLowerCase  (  )  .  equals  (  "headline"  )  )  { 
storyElem  =  getDefaultStoryFromHeadline  (  (  Element  )  node  )  ; 
}  else   if  (  node  !=  null  &&  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  node  .  getNodeName  (  )  .  toLowerCase  (  )  .  equals  (  "story"  )  )  { 
storyElem  =  (  Element  )  node  ; 
} 
if  (  storyElem  !=  null  )  { 
storyObject  =  new   StoryObject  (  storyElem  )  ; 
} 
return   storyObject  ; 
} 







public   Vector   getStoryObjects  (  String   appId  ,  String   nodeId  )  { 
Vector   outV  =  new   Vector  (  )  ; 
NodeList   nl  =  getNodes  (  appId  ,  nodeId  )  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Element   storyElem  =  null  ; 
Node   node  =  nl  .  item  (  i  )  ; 
if  (  node  !=  null  &&  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  node  .  getNodeName  (  )  .  toLowerCase  (  )  .  equals  (  "headline"  )  )  { 
storyElem  =  getDefaultStoryFromHeadline  (  (  Element  )  node  )  ; 
}  else   if  (  node  !=  null  &&  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  &&  node  .  getNodeName  (  )  .  toLowerCase  (  )  .  equals  (  "story"  )  )  { 
storyElem  =  (  Element  )  node  ; 
} 
if  (  storyElem  !=  null  )  { 
outV  .  add  (  new   StoryObject  (  storyElem  )  )  ; 
} 
} 
return   outV  ; 
} 





Element   getDefaultStoryFromHeadline  (  Element   he  )  { 
String   defStoryType  =  he  .  getAttribute  (  "defaultstorytype"  )  .  toLowerCase  (  )  ; 
if  (  defStoryType  .  equals  (  ""  )  )  defStoryType  =  PropertyHandler  .  getDefaultStory  (  )  ; 
NodeList   nl  =  he  .  getElementsByTagName  (  "story"  )  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
Node   node  =  nl  .  item  (  i  )  ; 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
if  (  (  (  Element  )  node  )  .  getAttribute  (  "type"  )  .  toLowerCase  (  )  .  equals  (  defStoryType  )  )  return  (  Element  )  node  ; 
} 
} 
if  (  nl  .  getLength  (  )  >  0  )  return  (  Element  )  nl  .  item  (  0  )  ; 
return   null  ; 
} 
} 

