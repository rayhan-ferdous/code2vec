package   javax  .  help  ; 

import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  Point  ; 
import   javax  .  help  .  event  .  EventListenerList  ; 
import   javax  .  help  .  DefaultHelpBroker  ; 
import   javax  .  help  .  event  .  HelpSetListener  ; 
import   javax  .  help  .  event  .  HelpSetEvent  ; 
import   javax  .  help  .  Map  .  ID  ; 
import   com  .  sun  .  java  .  help  .  impl  .  Parser  ; 
import   com  .  sun  .  java  .  help  .  impl  .  ParserListener  ; 
import   com  .  sun  .  java  .  help  .  impl  .  ParserEvent  ; 
import   com  .  sun  .  java  .  help  .  impl  .  Tag  ; 
import   com  .  sun  .  java  .  help  .  impl  .  TagProperties  ; 
import   com  .  sun  .  java  .  help  .  impl  .  XmlReader  ; 
import   com  .  sun  .  java  .  help  .  impl  .  LangElement  ; 
import   javax  .  help  .  Map  .  ID  ; 
import   java  .  beans  .  PropertyChangeSupport  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 











public   class   HelpSet   implements   Serializable  { 

private   static   String   errorMsg  =  null  ; 

protected   EventListenerList   listenerList  =  new   EventListenerList  (  )  ; 




public   static   final   String   publicIDString  =  "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"  ; 




public   static   final   String   publicIDString_V2  =  "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"  ; 







public   static   final   Object   implRegistry  =  new   StringBuffer  (  "HelpSet.implRegistry"  )  ; 

public   static   final   String   helpBrokerClass  =  "helpBroker/class"  ; 

public   static   final   String   helpBrokerLoader  =  "helpBroker/loader"  ; 










public   static   final   Object   kitTypeRegistry  =  new   StringBuffer  (  "JHelpViewer.kitTypeRegistry"  )  ; 

public   static   final   Object   kitLoaderRegistry  =  new   StringBuffer  (  "JHelpViewer.kitLoaderRegistry"  )  ; 






public   HelpSet  (  ClassLoader   loader  )  { 
this  .  helpsets  =  new   Vector  (  )  ; 
this  .  loader  =  loader  ; 
} 




public   HelpSet  (  )  { 
this  .  helpsets  =  new   Vector  (  )  ; 
this  .  loader  =  null  ; 
} 













public   HelpSet  (  ClassLoader   loader  ,  URL   helpset  )  throws   HelpSetException  { 
this  (  loader  )  ; 
this  .  helpset  =  helpset  ; 
HelpSetFactory   factory  =  new   DefaultHelpSetFactory  (  )  ; 
parseInto  (  helpset  ,  factory  )  ; 
HelpSet   x  =  factory  .  parsingEnded  (  this  )  ; 
if  (  x  ==  null  )  { 
throw   new   HelpSetException  (  "Could not parse\n"  +  errorMsg  )  ; 
} 
} 













public   static   URL   findHelpSet  (  ClassLoader   cl  ,  String   shortName  ,  String   extension  ,  Locale   locale  )  { 
return   HelpUtilities  .  getLocalizedResource  (  cl  ,  shortName  ,  extension  ,  locale  ,  true  )  ; 
} 













public   static   URL   findHelpSet  (  ClassLoader   cl  ,  String   name  ,  Locale   locale  )  { 
String   shortName  ; 
String   extension  ; 
if  (  name  .  endsWith  (  ".hs"  )  )  { 
shortName  =  name  .  substring  (  0  ,  name  .  length  (  )  -  3  )  ; 
extension  =  ".hs"  ; 
}  else  { 
shortName  =  name  ; 
extension  =  ".hs"  ; 
} 
return   findHelpSet  (  cl  ,  shortName  ,  extension  ,  locale  )  ; 
} 









public   static   URL   findHelpSet  (  ClassLoader   cl  ,  String   name  )  { 
return   findHelpSet  (  cl  ,  name  ,  Locale  .  getDefault  (  )  )  ; 
} 











public   HelpBroker   createHelpBroker  (  )  { 
return   createHelpBroker  (  null  )  ; 
} 















public   HelpBroker   createHelpBroker  (  String   presentationName  )  { 
HelpBroker   back  =  null  ; 
String   classname  =  (  String  )  getKeyData  (  implRegistry  ,  helpBrokerClass  )  ; 
ClassLoader   loader  =  (  ClassLoader  )  getKeyData  (  implRegistry  ,  helpBrokerLoader  )  ; 
if  (  loader  ==  null  )  { 
loader  =  getLoader  (  )  ; 
} 
try  { 
Class   c  ; 
if  (  loader  !=  null  )  { 
c  =  loader  .  loadClass  (  classname  )  ; 
}  else  { 
c  =  Class  .  forName  (  classname  )  ; 
} 
back  =  (  HelpBroker  )  c  .  newInstance  (  )  ; 
}  catch  (  Throwable   e  )  { 
back  =  null  ; 
} 
if  (  back  !=  null  )  { 
back  .  setHelpSet  (  this  )  ; 
HelpSet  .  Presentation   hsPres  =  null  ; 
if  (  presentationName  !=  null  )  { 
hsPres  =  getPresentation  (  presentationName  )  ; 
}  else  { 
hsPres  =  getDefaultPresentation  (  )  ; 
} 
if  (  hsPres  !=  null  )  { 
back  .  setHelpSetPresentation  (  hsPres  )  ; 
} 
} 
return   back  ; 
} 








public   void   add  (  HelpSet   hs  )  { 
debug  (  "add("  +  hs  +  ")"  )  ; 
helpsets  .  addElement  (  hs  )  ; 
fireHelpSetAdded  (  this  ,  hs  )  ; 
combinedMap  =  null  ; 
} 








public   boolean   remove  (  HelpSet   hs  )  { 
if  (  helpsets  .  removeElement  (  hs  )  )  { 
fireHelpSetRemoved  (  this  ,  hs  )  ; 
combinedMap  =  null  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 







public   Enumeration   getHelpSets  (  )  { 
return   helpsets  .  elements  (  )  ; 
} 







public   boolean   contains  (  HelpSet   hs  )  { 
if  (  hs  ==  this  )  { 
return   true  ; 
} 
for  (  Enumeration   e  =  helpsets  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
HelpSet   child  =  (  HelpSet  )  e  .  nextElement  (  )  ; 
if  (  child  .  contains  (  hs  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 









public   void   addHelpSetListener  (  HelpSetListener   l  )  { 
debug  (  "addHelpSetListener("  +  l  +  ")"  )  ; 
listenerList  .  add  (  HelpSetListener  .  class  ,  l  )  ; 
} 








public   void   removeHelpSetListener  (  HelpSetListener   l  )  { 
listenerList  .  remove  (  HelpSetListener  .  class  ,  l  )  ; 
} 




protected   void   fireHelpSetAdded  (  Object   source  ,  HelpSet   helpset  )  { 
Object  [  ]  listeners  =  listenerList  .  getListenerList  (  )  ; 
HelpSetEvent   e  =  null  ; 
for  (  int   i  =  listeners  .  length  -  2  ;  i  >=  0  ;  i  -=  2  )  { 
if  (  listeners  [  i  ]  ==  HelpSetListener  .  class  )  { 
if  (  e  ==  null  )  { 
e  =  new   HelpSetEvent  (  this  ,  helpset  ,  HelpSetEvent  .  HELPSET_ADDED  )  ; 
} 
(  (  HelpSetListener  )  listeners  [  i  +  1  ]  )  .  helpSetAdded  (  e  )  ; 
} 
} 
} 




protected   void   fireHelpSetRemoved  (  Object   source  ,  HelpSet   helpset  )  { 
Object  [  ]  listeners  =  listenerList  .  getListenerList  (  )  ; 
HelpSetEvent   e  =  null  ; 
for  (  int   i  =  listeners  .  length  -  2  ;  i  >=  0  ;  i  -=  2  )  { 
if  (  listeners  [  i  ]  ==  HelpSetListener  .  class  )  { 
if  (  e  ==  null  )  { 
e  =  new   HelpSetEvent  (  this  ,  helpset  ,  HelpSetEvent  .  HELPSET_REMOVED  )  ; 
} 
(  (  HelpSetListener  )  listeners  [  i  +  1  ]  )  .  helpSetRemoved  (  e  )  ; 
} 
} 
} 






public   String   getTitle  (  )  { 
if  (  title  ==  null  )  { 
return  ""  ; 
}  else  { 
return   title  ; 
} 
} 






public   void   setTitle  (  String   title  )  { 
String   oldTitle  =  this  .  title  ; 
this  .  title  =  title  ; 
changes  .  firePropertyChange  (  "title"  ,  oldTitle  ,  title  )  ; 
} 






public   Locale   getLocale  (  )  { 
return   locale  ; 
} 







private   void   setLocale  (  Locale   l  )  { 
Locale   oldLocale  =  locale  ; 
locale  =  l  ; 
changes  .  firePropertyChange  (  "locale"  ,  oldLocale  ,  locale  )  ; 
} 










public   ID   getHomeID  (  )  { 
if  (  homeID  ==  null  )  { 
return   null  ; 
}  else  { 
try  { 
return   ID  .  create  (  homeID  ,  this  )  ; 
}  catch  (  Exception   ex  )  { 
return   null  ; 
} 
} 
} 






public   void   setHomeID  (  String   homeID  )  { 
String   oldID  =  homeID  ; 
this  .  homeID  =  homeID  ; 
changes  .  firePropertyChange  (  "homeID"  ,  oldID  ,  homeID  )  ; 
} 







public   Map   getCombinedMap  (  )  { 
if  (  combinedMap  ==  null  )  { 
combinedMap  =  new   TryMap  (  )  ; 
if  (  map  !=  null  )  { 
combinedMap  .  add  (  map  )  ; 
} 
for  (  Enumeration   e  =  helpsets  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
HelpSet   hs  =  (  HelpSet  )  e  .  nextElement  (  )  ; 
combinedMap  .  add  (  hs  .  getCombinedMap  (  )  )  ; 
} 
} 
return   combinedMap  ; 
} 







public   Map   getLocalMap  (  )  { 
return   this  .  map  ; 
} 







public   void   setLocalMap  (  Map   map  )  { 
this  .  map  =  map  ; 
} 






public   URL   getHelpSetURL  (  )  { 
return   helpset  ; 
} 







public   ClassLoader   getLoader  (  )  { 
return   loader  ; 
} 







public   NavigatorView  [  ]  getNavigatorViews  (  )  { 
NavigatorView   back  [  ]  =  new   NavigatorView  [  views  .  size  (  )  ]  ; 
views  .  copyInto  (  back  )  ; 
return   back  ; 
} 






public   NavigatorView   getNavigatorView  (  String   name  )  { 
debug  (  "getNavigatorView("  +  name  +  ")"  )  ; 
for  (  int   i  =  0  ;  i  <  views  .  size  (  )  ;  i  ++  )  { 
NavigatorView   view  =  (  NavigatorView  )  views  .  elementAt  (  i  )  ; 
if  (  view  .  getName  (  )  .  equals  (  name  )  )  { 
debug  (  "  = "  +  view  )  ; 
return   view  ; 
} 
} 
debug  (  "  = null"  )  ; 
return   null  ; 
} 







public   HelpSet  .  Presentation  [  ]  getPresentations  (  )  { 
HelpSet  .  Presentation   back  [  ]  =  new   HelpSet  .  Presentation  [  presentations  .  size  (  )  ]  ; 
presentations  .  copyInto  (  back  )  ; 
return   back  ; 
} 






public   HelpSet  .  Presentation   getPresentation  (  String   name  )  { 
debug  (  "getPresentation("  +  name  +  ")"  )  ; 
for  (  int   i  =  0  ;  i  <  presentations  .  size  (  )  ;  i  ++  )  { 
HelpSet  .  Presentation   pres  =  (  HelpSet  .  Presentation  )  presentations  .  elementAt  (  i  )  ; 
if  (  pres  .  getName  (  )  .  equals  (  name  )  )  { 
debug  (  "  = "  +  pres  )  ; 
return   pres  ; 
} 
} 
debug  (  "  = null"  )  ; 
return   null  ; 
} 

public   HelpSet  .  Presentation   getDefaultPresentation  (  )  { 
return   defaultPresentation  ; 
} 




public   String   toString  (  )  { 
return   getTitle  (  )  ; 
} 




public   static   HelpSet   parse  (  URL   url  ,  ClassLoader   loader  ,  HelpSetFactory   factory  )  { 
HelpSet   hs  =  new   HelpSet  (  loader  )  ; 
hs  .  helpset  =  url  ; 
hs  .  parseInto  (  url  ,  factory  )  ; 
return   factory  .  parsingEnded  (  hs  )  ; 
} 




public   void   parseInto  (  URL   url  ,  HelpSetFactory   factory  )  { 
Reader   src  ; 
try  { 
URLConnection   uc  =  url  .  openConnection  (  )  ; 
src  =  XmlReader  .  createReader  (  uc  )  ; 
factory  .  parsingStarted  (  url  )  ; 
(  new   HelpSetParser  (  factory  )  )  .  parseInto  (  src  ,  this  )  ; 
src  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
factory  .  reportMessage  (  "Got an IOException ("  +  ex  .  getMessage  (  )  +  ")"  ,  false  )  ; 
if  (  debug  )  ex  .  printStackTrace  (  )  ; 
} 
for  (  int   i  =  0  ;  i  <  subHelpSets  .  size  (  )  ;  i  ++  )  { 
HelpSet   subHS  =  (  HelpSet  )  subHelpSets  .  elementAt  (  i  )  ; 
add  (  subHS  )  ; 
} 
} 




public   static   class   DefaultHelpSetFactory   implements   HelpSetFactory  { 

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
if  (  publicID  ==  null  ||  (  publicID  .  compareTo  (  publicIDString  )  !=  0  &&  publicID  .  compareTo  (  publicIDString_V2  )  !=  0  )  )  { 
parsingError  (  "helpset.wrongPublicID"  ,  publicID  )  ; 
} 
} 




public   void   processPI  (  HelpSet   hs  ,  String   target  ,  String   data  )  { 
} 




public   void   processTitle  (  HelpSet   hs  ,  String   value  )  { 
String   title  =  hs  .  getTitle  (  )  ; 
if  (  (  title  !=  null  )  &&  !  title  .  equals  (  ""  )  )  { 
parsingWarning  (  "helpset.wrongTitle"  ,  value  ,  title  )  ; 
} 
hs  .  setTitle  (  value  )  ; 
} 




public   void   processHomeID  (  HelpSet   hs  ,  String   value  )  { 
ID   homeID  =  hs  .  getHomeID  (  )  ; 
if  (  (  homeID  ==  null  )  ||  homeID  .  equals  (  ""  )  )  { 
hs  .  setHomeID  (  value  )  ; 
}  else  { 
parsingError  (  "helpset.wrongHomeID"  ,  value  ,  homeID  .  id  )  ; 
} 
} 







public   void   processMapRef  (  HelpSet   hs  ,  Hashtable   attributes  )  { 
String   spec  =  (  String  )  attributes  .  get  (  "location"  )  ; 
URL   hsURL  =  hs  .  getHelpSetURL  (  )  ; 
try  { 
Map   map  =  new   FlatMap  (  new   URL  (  hsURL  ,  spec  )  ,  hs  )  ; 
Map   omap  =  hs  .  getLocalMap  (  )  ; 
if  (  omap  ==  null  )  { 
debug  (  "map is null"  )  ; 
hs  .  setLocalMap  (  map  )  ; 
}  else  { 
if  (  omap   instanceof   TryMap  )  { 
debug  (  "map is TryMap"  )  ; 
(  (  TryMap  )  omap  )  .  add  (  map  )  ; 
hs  .  setLocalMap  (  omap  )  ; 
}  else  { 
debug  (  "map is not TryMap"  )  ; 
TryMap   tmap  =  new   TryMap  (  )  ; 
tmap  .  add  (  omap  )  ; 
tmap  .  add  (  map  )  ; 
hs  .  setLocalMap  (  tmap  )  ; 
} 
} 
}  catch  (  MalformedURLException   ee  )  { 
parsingError  (  "helpset.malformedURL"  ,  spec  )  ; 
}  catch  (  IOException   ee  )  { 
parsingError  (  "helpset.incorrectURL"  ,  spec  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 

public   void   processView  (  HelpSet   hs  ,  String   name  ,  String   label  ,  String   type  ,  Hashtable   viewAttributes  ,  String   data  ,  Hashtable   dataAttributes  ,  Locale   locale  )  { 
try  { 
NavigatorView   view  ; 
if  (  data  !=  null  )  { 
if  (  dataAttributes  ==  null  )  { 
dataAttributes  =  new   Hashtable  (  )  ; 
} 
dataAttributes  .  put  (  "data"  ,  data  )  ; 
} 
view  =  NavigatorView  .  create  (  hs  ,  name  ,  label  ,  locale  ,  type  ,  dataAttributes  )  ; 
if  (  view  ==  null  )  { 
}  else  { 
hs  .  addView  (  view  )  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 

public   void   processPresentation  (  HelpSet   hs  ,  String   name  ,  boolean   defaultPresentation  ,  boolean   displayViews  ,  boolean   displayViewImages  ,  Dimension   size  ,  Point   location  ,  String   title  ,  String   imageID  ,  boolean   toolbar  ,  Vector   helpActions  )  { 
Map  .  ID   imageMapID  =  null  ; 
try  { 
imageMapID  =  ID  .  create  (  imageID  ,  hs  )  ; 
}  catch  (  BadIDException   bex2  )  { 
} 
try  { 
HelpSet  .  Presentation   presentation  =  new   HelpSet  .  Presentation  (  name  ,  displayViews  ,  displayViewImages  ,  size  ,  location  ,  title  ,  imageMapID  ,  toolbar  ,  helpActions  )  ; 
if  (  presentation  ==  null  )  { 
}  else  { 
hs  .  addPresentation  (  presentation  ,  defaultPresentation  )  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 




public   void   processSubHelpSet  (  HelpSet   hs  ,  Hashtable   attributes  )  { 
debug  (  "createSubHelpSet"  )  ; 
String   spec  =  (  String  )  attributes  .  get  (  "location"  )  ; 
URL   base  =  hs  .  getHelpSetURL  (  )  ; 
debug  (  "  location: "  +  spec  )  ; 
debug  (  "  base helpset: "  +  base  )  ; 
URL   u  =  null  ; 
HelpSet   subHS  =  null  ; 
try  { 
u  =  new   URL  (  base  ,  spec  )  ; 
InputStream   is  =  u  .  openStream  (  )  ; 
if  (  is  !=  null  )  { 
subHS  =  new   HelpSet  (  hs  .  getLoader  (  )  ,  u  )  ; 
if  (  subHS  !=  null  )  { 
hs  .  addSubHelpSet  (  subHS  )  ; 
} 
} 
}  catch  (  MalformedURLException   ex  )  { 
}  catch  (  IOException   ex  )  { 
}  catch  (  HelpSetException   ex  )  { 
parsingError  (  "helpset.subHelpSetTrouble"  ,  spec  )  ; 
} 
} 




public   void   reportMessage  (  String   msg  ,  boolean   validParse  )  { 
messages  .  addElement  (  msg  )  ; 
this  .  validParse  =  this  .  validParse  &&  validParse  ; 
} 




public   Enumeration   listMessages  (  )  { 
return   messages  .  elements  (  )  ; 
} 






public   HelpSet   parsingEnded  (  HelpSet   hs  )  { 
HelpSet   back  =  hs  ; 
if  (  !  validParse  )  { 
back  =  null  ; 
String   errMsg  =  "Parsing failed for "  +  source  ; 
messages  .  addElement  (  errMsg  )  ; 
for  (  Enumeration   e  =  messages  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   msg  =  (  String  )  e  .  nextElement  (  )  ; 
if  (  debug  )  System  .  err  .  println  (  msg  )  ; 
if  (  HelpSet  .  errorMsg  ==  null  )  HelpSet  .  errorMsg  =  msg  ;  else  { 
HelpSet  .  errorMsg  =  HelpSet  .  errorMsg  +  "\n"  ; 
HelpSet  .  errorMsg  =  HelpSet  .  errorMsg  +  msg  ; 
} 
} 
} 
return   back  ; 
} 

private   void   parsingError  (  String   key  )  { 
String   s  =  HelpUtilities  .  getText  (  key  )  ; 
reportMessage  (  s  ,  false  )  ; 
} 




private   void   parsingError  (  String   key  ,  String   s  )  { 
String   msg  =  HelpUtilities  .  getText  (  key  ,  s  )  ; 
reportMessage  (  msg  ,  false  )  ; 
} 




private   void   parsingError  (  String   key  ,  String   s1  ,  String   s2  )  { 
String   msg  =  HelpUtilities  .  getText  (  key  ,  s1  ,  s2  )  ; 
reportMessage  (  msg  ,  false  )  ; 
} 

private   void   parsingWarning  (  String   key  ,  String   s1  ,  String   s2  )  { 
String   msg  =  HelpUtilities  .  getText  (  key  ,  s1  ,  s2  )  ; 
reportMessage  (  msg  ,  true  )  ; 
} 
} 






public   static   class   Presentation  { 

private   String   name  ; 

private   boolean   displayViews  ; 

private   boolean   displayViewImages  ; 

private   Dimension   size  ; 

private   Point   location  ; 

private   String   title  ; 

private   boolean   toolbar  ; 

private   Vector   helpActions  ; 

private   Map  .  ID   imageID  ; 

public   Presentation  (  String   name  ,  boolean   displayViews  ,  boolean   displayViewImages  ,  Dimension   size  ,  Point   location  ,  String   title  ,  Map  .  ID   imageID  ,  boolean   toolbar  ,  Vector   helpActions  )  { 
this  .  name  =  name  ; 
this  .  displayViews  =  displayViews  ; 
this  .  displayViewImages  =  displayViewImages  ; 
this  .  size  =  size  ; 
this  .  location  =  location  ; 
this  .  title  =  title  ; 
this  .  imageID  =  imageID  ; 
this  .  toolbar  =  toolbar  ; 
this  .  helpActions  =  helpActions  ; 
} 

public   String   getName  (  )  { 
return   name  ; 
} 

public   String   getTitle  (  )  { 
return   title  ; 
} 

public   Map  .  ID   getImageID  (  )  { 
return   imageID  ; 
} 

public   boolean   isViewDisplayed  (  )  { 
return   displayViews  ; 
} 

public   boolean   isViewImagesDisplayed  (  )  { 
return   displayViewImages  ; 
} 

public   Dimension   getSize  (  )  { 
return   size  ; 
} 

public   Point   getLocation  (  )  { 
return   location  ; 
} 

public   boolean   isToolbar  (  )  { 
return   toolbar  ; 
} 







public   Enumeration   getHelpActions  (  HelpSet   hs  ,  Object   control  )  { 
Vector   actions  =  new   Vector  (  )  ; 
ClassLoader   loader  =  hs  .  getLoader  (  )  ; 
Class   klass  ; 
Constructor   konstructor  ; 
HelpAction   action  ; 
if  (  helpActions  ==  null  )  { 
return   actions  .  elements  (  )  ; 
} 
Enumeration   actionEnum  =  helpActions  .  elements  (  )  ; 
while  (  actionEnum  .  hasMoreElements  (  )  )  { 
HelpSetFactory  .  HelpAction   act  =  (  HelpSetFactory  .  HelpAction  )  actionEnum  .  nextElement  (  )  ; 
try  { 
Class   types  [  ]  =  {  Object  .  class  }  ; 
Object   args  [  ]  =  {  control  }  ; 
if  (  loader  ==  null  )  { 
klass  =  Class  .  forName  (  act  .  className  )  ; 
}  else  { 
klass  =  loader  .  loadClass  (  act  .  className  )  ; 
} 
konstructor  =  klass  .  getConstructor  (  types  )  ; 
action  =  (  HelpAction  )  konstructor  .  newInstance  (  args  )  ; 
if  (  act  .  attr  .  containsKey  (  "image"  )  )  { 
String   imageID  =  (  String  )  act  .  attr  .  get  (  "image"  )  ; 
try  { 
Map  .  ID   id  =  Map  .  ID  .  create  (  imageID  ,  hs  )  ; 
javax  .  swing  .  ImageIcon   icon  =  null  ; 
Map   map  =  hs  .  getCombinedMap  (  )  ; 
URL   url  =  map  .  getURLFromID  (  id  )  ; 
icon  =  new   javax  .  swing  .  ImageIcon  (  url  )  ; 
action  .  putValue  (  "icon"  ,  icon  )  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
actions  .  add  (  action  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   RuntimeException  (  "Could not create HelpAction "  +  act  .  className  )  ; 
} 
} 
return   actions  .  elements  (  )  ; 
} 
} 




protected   void   addView  (  NavigatorView   view  )  { 
views  .  addElement  (  view  )  ; 
} 




protected   void   addSubHelpSet  (  HelpSet   hs  )  { 
subHelpSets  .  addElement  (  hs  )  ; 
} 




protected   void   addPresentation  (  HelpSet  .  Presentation   presentation  ,  boolean   defaultPres  )  { 
presentations  .  addElement  (  presentation  )  ; 
if  (  defaultPres  )  { 
defaultPresentation  =  presentation  ; 
} 
} 





public   Object   getKeyData  (  Object   context  ,  String   key  )  { 
Object   back  =  null  ; 
Hashtable   h  =  (  Hashtable  )  localKeys  .  get  (  context  )  ; 
if  (  h  !=  null  )  { 
back  =  h  .  get  (  key  )  ; 
} 
if  (  back  ==  null  )  { 
h  =  (  Hashtable  )  defaultKeys  .  get  (  context  )  ; 
if  (  h  !=  null  )  { 
back  =  h  .  get  (  key  )  ; 
} 
} 
return   back  ; 
} 





public   void   setKeyData  (  Object   context  ,  String   key  ,  Object   data  )  { 
Hashtable   h  =  (  Hashtable  )  localKeys  .  get  (  context  )  ; 
if  (  h  ==  null  )  { 
h  =  new   Hashtable  (  )  ; 
localKeys  .  put  (  context  ,  h  )  ; 
} 
h  .  put  (  key  ,  data  )  ; 
} 




private   static   void   setDefaultKeyData  (  Object   context  ,  String   key  ,  Object   data  )  { 
if  (  defaultKeys  ==  null  )  { 
defaultKeys  =  new   Hashtable  (  )  ; 
} 
Hashtable   h  =  (  Hashtable  )  defaultKeys  .  get  (  context  )  ; 
if  (  h  ==  null  )  { 
h  =  new   Hashtable  (  )  ; 
defaultKeys  .  put  (  context  ,  h  )  ; 
} 
h  .  put  (  key  ,  data  )  ; 
} 




static  { 
setDefaultKeyData  (  implRegistry  ,  helpBrokerClass  ,  "javax.help.DefaultHelpBroker"  )  ; 
setDefaultKeyData  (  kitTypeRegistry  ,  "text/html"  ,  "com.sun.java.help.impl.CustomKit"  )  ; 
ClassLoader   cl  =  HelpSet  .  class  .  getClassLoader  (  )  ; 
if  (  cl  !=  null  )  { 
setDefaultKeyData  (  implRegistry  ,  helpBrokerLoader  ,  cl  )  ; 
setDefaultKeyData  (  kitLoaderRegistry  ,  "text/html"  ,  cl  )  ; 
} 
} 

private   String   title  ; 

private   Map   map  ; 

private   TryMap   combinedMap  ; 

private   URL   helpset  ; 

private   String   homeID  ; 

private   Locale   locale  =  Locale  .  getDefault  (  )  ; 

private   transient   ClassLoader   loader  ; 

private   Vector   views  =  new   Vector  (  )  ; 

private   Vector   presentations  =  new   Vector  (  )  ; 

private   HelpSet  .  Presentation   defaultPresentation  =  null  ; 

private   Vector   helpsets  ; 

private   static   HelpBroker   defaultHelpBroker  =  null  ; 

private   Vector   subHelpSets  =  new   Vector  (  )  ; 

private   static   Hashtable   defaultKeys  ; 

private   Hashtable   localKeys  =  new   Hashtable  (  )  ; 

private   PropertyChangeSupport   changes  =  new   PropertyChangeSupport  (  this  )  ; 








private   static   class   HelpSetParser   implements   ParserListener  { 

private   Stack   tagStack  ; 

private   Locale   defaultLocale  ; 

private   Locale   lastLocale  ; 

private   HelpSet   myHS  ; 

private   Locale   myHSLocale  ; 

private   HelpSetFactory   factory  ; 

private   String   tagName  ; 

private   String   viewLabel  ; 

private   String   viewType  ; 

private   String   viewEngine  ; 

private   String   tagImage  ; 

private   String   helpActionImage  ; 

private   String   viewData  ; 

private   String   viewMergeType  ; 

private   Hashtable   htData  ; 

private   boolean   defaultPresentation  =  false  ; 

private   boolean   displayViews  =  true  ; 

private   boolean   displayViewImages  =  true  ; 

private   Dimension   size  ; 

private   Point   location  ; 

private   String   presentationTitle  ; 

private   boolean   toolbar  ; 

private   Vector   helpActions  ; 

private   String   helpAction  ; 




HelpSetParser  (  HelpSetFactory   factory  )  { 
this  .  factory  =  factory  ; 
} 




synchronized   void   parseInto  (  Reader   src  ,  HelpSet   hs  )  throws   IOException  { 
tagStack  =  new   Stack  (  )  ; 
defaultLocale  =  hs  .  getLocale  (  )  ; 
lastLocale  =  defaultLocale  ; 
myHS  =  hs  ; 
myHSLocale  =  hs  .  getLocale  (  )  ; 
Parser   parser  =  new   Parser  (  src  )  ; 
parser  .  addParserListener  (  this  )  ; 
parser  .  parse  (  )  ; 
} 

public   void   tagFound  (  ParserEvent   e  )  { 
debug  (  "tagFound "  +  e  .  getTag  (  )  .  name  )  ; 
Locale   locale  =  null  ; 
LangElement   le  ; 
Tag   tag  =  e  .  getTag  (  )  ; 
String   name  =  tag  .  name  ; 
int   x  =  0  ,  y  =  0  ,  width  =  0  ,  height  =  0  ; 
TagProperties   attr  =  tag  .  atts  ; 
Hashtable   ht  =  (  attr  ==  null  )  ?  null  :  attr  .  getHashtable  (  )  ; 
if  (  attr  !=  null  )  { 
String   lang  =  attr  .  getProperty  (  "xml:lang"  )  ; 
locale  =  HelpUtilities  .  localeFromLang  (  lang  )  ; 
viewMergeType  =  attr  .  getProperty  (  "mergetype"  )  ; 
helpActionImage  =  attr  .  getProperty  (  "image"  )  ; 
String   value  =  null  ; 
value  =  attr  .  getProperty  (  "width"  )  ; 
if  (  value  !=  null  )  { 
width  =  Integer  .  parseInt  (  value  )  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "height"  )  ; 
if  (  value  !=  null  )  { 
height  =  Integer  .  parseInt  (  value  )  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "x"  )  ; 
if  (  value  !=  null  )  { 
x  =  Integer  .  parseInt  (  value  )  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "y"  )  ; 
if  (  value  !=  null  )  { 
y  =  Integer  .  parseInt  (  value  )  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "default"  )  ; 
if  (  value  !=  null  &&  value  .  equals  (  "true"  )  )  { 
defaultPresentation  =  true  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "displayviews"  )  ; 
if  (  value  !=  null  &&  value  .  equals  (  "false"  )  )  { 
displayViews  =  false  ; 
} 
value  =  null  ; 
value  =  attr  .  getProperty  (  "displayviewimages"  )  ; 
if  (  value  !=  null  &&  value  .  equals  (  "false"  )  )  { 
displayViewImages  =  false  ; 
} 
} 
if  (  locale  ==  null  )  { 
locale  =  lastLocale  ; 
} 
if  (  name  .  equals  (  "helpset"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  locale  .  equals  (  defaultLocale  )  &&  !  locale  .  equals  (  myHSLocale  )  )  { 
if  (  locale  !=  null  )  { 
myHS  .  setLocale  (  locale  )  ; 
defaultLocale  =  locale  ; 
} 
} 
if  (  attr  !=  null  )  { 
String   version  =  attr  .  getProperty  (  "version"  )  ; 
if  (  version  !=  null  &&  (  version  .  compareTo  (  "1.0"  )  !=  0  &&  version  .  compareTo  (  "2.0"  )  !=  0  )  )  { 
parsingError  (  "helpset.unknownVersion"  ,  version  )  ; 
} 
} 
addTag  (  tag  ,  locale  )  ; 
} 
return  ; 
} 
if  (  tagStack  .  empty  (  )  )  { 
parsingError  (  "helpset.wrongTopLevel"  ,  name  )  ; 
} 
le  =  (  LangElement  )  tagStack  .  peek  (  )  ; 
String   pname  =  (  (  Tag  )  le  .  getTag  (  )  )  .  name  ; 
if  (  name  .  equals  (  "title"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  (  !  pname  .  equals  (  "helpset"  )  )  &&  (  !  pname  .  equals  (  "presentation"  )  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
} 
if  (  !  locale  .  equals  (  defaultLocale  )  &&  !  locale  .  equals  (  myHSLocale  )  )  { 
wrongLocale  (  locale  ,  defaultLocale  ,  myHSLocale  )  ; 
} 
addTag  (  tag  ,  locale  )  ; 
} 
}  else   if  (  name  .  equals  (  "homeID"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "maps"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
} 
addTag  (  tag  ,  locale  )  ; 
} 
}  else   if  (  name  .  equals  (  "mapref"  )  )  { 
if  (  tag  .  isEnd  &&  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "maps"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
} 
if  (  !  tag  .  isEmpty  )  { 
addTag  (  tag  ,  locale  )  ; 
} 
factory  .  processMapRef  (  myHS  ,  ht  )  ; 
} 
}  else   if  (  name  .  equals  (  "data"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "view"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
htData  =  ht  ; 
} 
}  else   if  (  name  .  equals  (  "name"  )  ||  name  .  equals  (  "type"  )  ||  name  .  equals  (  "image"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  (  !  pname  .  equals  (  "view"  )  )  &&  (  !  pname  .  equals  (  "presentation"  )  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "label"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "view"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
if  (  !  locale  .  equals  (  defaultLocale  )  &&  !  locale  .  equals  (  myHSLocale  )  )  { 
wrongLocale  (  locale  ,  defaultLocale  ,  myHSLocale  )  ; 
} 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "view"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
if  (  tagImage  !=  null  )  { 
if  (  htData  ==  null  )  { 
htData  =  new   Hashtable  (  )  ; 
} 
htData  .  put  (  "imageID"  ,  tagImage  )  ; 
} 
if  (  viewMergeType  !=  null  )  { 
if  (  htData  ==  null  )  { 
htData  =  new   Hashtable  (  )  ; 
} 
htData  .  put  (  "mergetype"  ,  viewMergeType  )  ; 
} 
factory  .  processView  (  myHS  ,  tagName  ,  viewLabel  ,  viewType  ,  ht  ,  viewData  ,  htData  ,  locale  )  ; 
tagName  =  null  ; 
viewLabel  =  null  ; 
viewType  =  null  ; 
tagImage  =  null  ; 
viewData  =  null  ; 
htData  =  null  ; 
viewMergeType  =  null  ; 
}  else  { 
if  (  !  pname  .  equals  (  "helpset"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "presentation"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
factory  .  processPresentation  (  myHS  ,  tagName  ,  defaultPresentation  ,  displayViews  ,  displayViewImages  ,  size  ,  location  ,  presentationTitle  ,  tagImage  ,  toolbar  ,  helpActions  )  ; 
tagName  =  null  ; 
defaultPresentation  =  false  ; 
displayViews  =  true  ; 
displayViewImages  =  true  ; 
size  =  null  ; 
location  =  null  ; 
presentationTitle  =  null  ; 
tagImage  =  null  ; 
toolbar  =  false  ; 
helpActions  =  null  ; 
}  else  { 
if  (  !  pname  .  equals  (  "helpset"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "size"  )  )  { 
if  (  tag  .  isEnd  )  { 
if  (  size  ==  null  )  { 
size  =  new   Dimension  (  width  ,  height  )  ; 
}  else  { 
size  .  setSize  (  width  ,  height  )  ; 
} 
width  =  0  ; 
height  =  0  ; 
if  (  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
} 
}  else  { 
if  (  !  pname  .  equals  (  "presentation"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
size  =  new   Dimension  (  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "location"  )  )  { 
if  (  tag  .  isEnd  )  { 
if  (  location  ==  null  )  { 
location  =  new   Point  (  x  ,  y  )  ; 
}  else  { 
location  .  setLocation  (  x  ,  y  )  ; 
} 
x  =  0  ; 
y  =  0  ; 
if  (  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
} 
}  else  { 
if  (  !  pname  .  equals  (  "presentation"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
location  =  new   Point  (  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "toolbar"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "presentation"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
helpActions  =  new   Vector  (  )  ; 
toolbar  =  true  ; 
} 
} 
}  else   if  (  name  .  equals  (  "helpaction"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
if  (  helpAction  !=  null  )  { 
Hashtable   tmp  =  new   Hashtable  (  )  ; 
helpActions  .  add  (  new   HelpSetFactory  .  HelpAction  (  helpAction  ,  tmp  )  )  ; 
if  (  helpActionImage  !=  null  )  { 
tmp  .  put  (  "image"  ,  helpActionImage  )  ; 
helpActionImage  =  null  ; 
} 
helpAction  =  null  ; 
} 
}  else  { 
if  (  !  pname  .  equals  (  "toolbar"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "maps"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "helpset"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "subhelpset"  )  )  { 
if  (  tag  .  isEnd  &&  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  tag  .  isEmpty  )  { 
addTag  (  tag  ,  locale  )  ; 
} 
factory  .  processSubHelpSet  (  myHS  ,  ht  )  ; 
} 
}  else   if  (  name  .  equals  (  "impl"  )  )  { 
if  (  tag  .  isEnd  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "helpset"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
addTag  (  tag  ,  locale  )  ; 
} 
} 
}  else   if  (  name  .  equals  (  "helpsetregistry"  )  )  { 
if  (  tag  .  isEnd  &&  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "impl"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
if  (  !  tag  .  isEnd  )  { 
addTag  (  tag  ,  locale  )  ; 
} 
if  (  attr  !=  null  )  { 
String   hbClass  =  attr  .  getProperty  (  "helpbrokerclass"  )  ; 
if  (  hbClass  !=  null  )  { 
myHS  .  setKeyData  (  implRegistry  ,  helpBrokerClass  ,  hbClass  )  ; 
} 
} 
} 
} 
}  else   if  (  name  .  equals  (  "viewerregistry"  )  )  { 
if  (  tag  .  isEnd  &&  !  tag  .  isEmpty  )  { 
removeTag  (  tag  )  ; 
}  else  { 
if  (  !  pname  .  equals  (  "impl"  )  )  { 
wrongParent  (  name  ,  pname  )  ; 
}  else  { 
if  (  !  tag  .  isEnd  )  { 
addTag  (  tag  ,  locale  )  ; 
} 
if  (  attr  !=  null  )  { 
String   viewerType  =  attr  .  getProperty  (  "viewertype"  )  ; 
String   viewerClass  =  attr  .  getProperty  (  "viewerclass"  )  ; 
if  (  viewerType  !=  null  &&  viewerClass  !=  null  )  { 
ClassLoader   cl  =  HelpSet  .  class  .  getClassLoader  (  )  ; 
myHS  .  setKeyData  (  kitTypeRegistry  ,  viewerType  ,  viewerClass  )  ; 
myHS  .  setKeyData  (  kitLoaderRegistry  ,  viewerType  ,  cl  )  ; 
} 
} 
} 
} 
} 
} 

public   void   piFound  (  ParserEvent   e  )  { 
factory  .  processPI  (  myHS  ,  e  .  getTarget  (  )  ,  e  .  getData  (  )  )  ; 
} 

public   void   doctypeFound  (  ParserEvent   e  )  { 
factory  .  processDOCTYPE  (  e  .  getRoot  (  )  ,  e  .  getPublicId  (  )  ,  e  .  getSystemId  (  )  )  ; 
} 

private   void   checkNull  (  String   name  ,  String   t  )  { 
if  (  !  t  .  equals  (  ""  )  )  { 
parsingError  (  "helpset.wrongText"  ,  name  ,  t  )  ; 
} 
} 

public   void   textFound  (  ParserEvent   e  )  { 
debug  (  "textFound: "  )  ; 
debug  (  "  text: "  +  e  .  getText  (  )  )  ; 
if  (  tagStack  .  empty  (  )  )  { 
return  ; 
} 
LangElement   le  =  (  LangElement  )  tagStack  .  peek  (  )  ; 
Tag   tag  =  le  .  getTag  (  )  ; 
TagProperties   attr  =  tag  .  atts  ; 
Hashtable   ht  =  (  attr  ==  null  )  ?  null  :  attr  .  getHashtable  (  )  ; 
String   text  =  e  .  getText  (  )  .  trim  (  )  ; 
String   name  =  tag  .  name  ; 
if  (  name  .  equals  (  "helpset"  )  )  { 
checkNull  (  "helpset"  ,  text  )  ; 
return  ; 
} 
int   depth  =  tagStack  .  size  (  )  ; 
String   pname  =  ""  ; 
if  (  depth  >=  2  )  { 
le  =  (  LangElement  )  tagStack  .  elementAt  (  depth  -  2  )  ; 
pname  =  (  (  Tag  )  le  .  getTag  (  )  )  .  name  ; 
} 
if  (  name  .  equals  (  "title"  )  )  { 
if  (  pname  .  equals  (  "helpset"  )  )  { 
factory  .  processTitle  (  myHS  ,  text  )  ; 
}  else  { 
presentationTitle  =  text  .  trim  (  )  ; 
} 
}  else   if  (  name  .  equals  (  "homeID"  )  )  { 
factory  .  processHomeID  (  myHS  ,  text  )  ; 
}  else   if  (  name  .  equals  (  "mapref"  )  )  { 
checkNull  (  "mapref"  ,  text  )  ; 
}  else   if  (  name  .  equals  (  "subhelpset"  )  )  { 
checkNull  (  "subhelpset"  ,  text  )  ; 
}  else   if  (  name  .  equals  (  "data"  )  )  { 
viewData  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "label"  )  )  { 
viewLabel  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "name"  )  )  { 
tagName  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "helpaction"  )  )  { 
helpAction  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "type"  )  )  { 
viewType  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "image"  )  )  { 
tagImage  =  text  .  trim  (  )  ; 
}  else   if  (  name  .  equals  (  "view"  )  )  { 
checkNull  (  "view"  ,  text  )  ; 
}  else   if  (  name  .  equals  (  "maps"  )  )  { 
checkNull  (  "maps"  ,  text  )  ; 
}  else   if  (  name  .  equals  (  "mergetype"  )  )  { 
checkNull  (  "mergetype"  ,  text  )  ; 
} 
} 




public   void   errorFound  (  ParserEvent   e  )  { 
} 




public   void   commentFound  (  ParserEvent   e  )  { 
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
if  (  tagStack  .  empty  (  )  )  unbalanced  (  name  )  ; 
el  =  (  LangElement  )  tagStack  .  pop  (  )  ; 
if  (  el  .  getTag  (  )  .  name  .  equals  (  name  )  )  { 
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




private   void   parsingError  (  String   key  )  { 
String   s  =  HelpUtilities  .  getText  (  key  )  ; 
factory  .  reportMessage  (  s  ,  false  )  ; 
} 

private   void   parsingError  (  String   key  ,  String   s  )  { 
String   msg  =  HelpUtilities  .  getText  (  key  ,  s  )  ; 
factory  .  reportMessage  (  msg  ,  false  )  ; 
} 

private   void   parsingError  (  String   key  ,  String   s1  ,  String   s2  )  { 
String   msg  =  HelpUtilities  .  getText  (  key  ,  s1  ,  s2  )  ; 
factory  .  reportMessage  (  msg  ,  false  )  ; 
} 

private   void   wrongParent  (  String   name  ,  String   pname  )  { 
parsingError  (  "helpset.wrongParent"  ,  name  ,  pname  )  ; 
} 

private   void   unbalanced  (  String   name  )  { 
parsingError  (  "helpset.unbalanced"  ,  name  )  ; 
} 

private   void   wrongLocale  (  Locale   found  ,  Locale   l1  ,  Locale   l2  )  { 
String   msg  =  HelpUtilities  .  getText  (  "helpset.wrongLocale"  ,  found  .  toString  (  )  ,  l1  .  toString  (  )  ,  l2  .  toString  (  )  )  ; 
factory  .  reportMessage  (  msg  ,  true  )  ; 
} 
} 




private   static   final   boolean   debug  =  false  ; 

private   static   void   debug  (  String   str  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  "HelpSet: "  +  str  )  ; 
} 
} 
} 

