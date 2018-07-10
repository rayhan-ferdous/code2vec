package   net  .  sf  .  appfw  .  common  .  hivemind  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Properties  ; 
import   org  .  apache  .  hivemind  .  ClassResolver  ; 
import   org  .  apache  .  hivemind  .  Resource  ; 
import   org  .  apache  .  hivemind  .  impl  .  AbstractMessages  ; 
import   org  .  apache  .  hivemind  .  impl  .  DefaultClassResolver  ; 
import   org  .  apache  .  hivemind  .  util  .  ClasspathResource  ; 
import   org  .  apache  .  hivemind  .  util  .  LocalizedNameGenerator  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   net  .  sf  .  appfw  .  common  .  util  .  XMLProperties  ; 











public   class   XmlMessagesImpl   extends   AbstractMessages  { 

private   static   final   Log   _log  =  LogFactory  .  getLog  (  XmlMessagesImpl  .  class  )  ; 

private   Properties   _properties  ; 

private   Locale   _locale  ; 












public   XmlMessagesImpl  (  Class   claz  )  { 
this  (  claz  ,  Locale  .  getDefault  (  )  )  ; 
} 



















public   XmlMessagesImpl  (  Class   claz  ,  boolean   addMsgs  )  { 
this  (  claz  ,  Locale  .  getDefault  (  )  ,  addMsgs  )  ; 
} 











public   XmlMessagesImpl  (  Class   claz  ,  Locale   locale  )  { 
this  (  claz  ,  locale  ,  true  )  ; 
} 










public   XmlMessagesImpl  (  String   path  )  { 
this  (  path  ,  Locale  .  getDefault  (  )  )  ; 
} 




















public   XmlMessagesImpl  (  Class   claz  ,  Locale   locale  ,  boolean   addMsgs  )  { 
ClassResolver   resolver  =  new   DefaultClassResolver  (  )  ; 
Resource   location  =  new   ClasspathResource  (  resolver  ,  claz  .  getName  (  )  .  replace  (  '.'  ,  '/'  )  +  (  addMsgs  ?  "Msgs.xml"  :  ".xml"  )  ,  locale  )  ; 
_locale  =  locale  ; 
initialize  (  location  )  ; 
} 







public   XmlMessagesImpl  (  String   path  ,  Locale   locale  )  { 
ClassResolver   resolver  =  new   DefaultClassResolver  (  )  ; 
Resource   location  =  new   ClasspathResource  (  resolver  ,  path  ,  locale  )  ; 
_locale  =  locale  ; 
initialize  (  location  )  ; 
} 








public   XmlMessagesImpl  (  Resource   location  ,  Locale   locale  )  { 
_locale  =  locale  ; 
initialize  (  location  )  ; 
} 









private   void   initialize  (  Resource   location  )  { 
if  (  _log  .  isDebugEnabled  (  )  )  _log  .  debug  (  "loading messages from location: "  +  location  )  ; 
String   descriptorName  =  location  .  getName  (  )  ; 
int   dotx  =  descriptorName  .  lastIndexOf  (  '.'  )  ; 
String   baseName  =  descriptorName  .  substring  (  0  ,  dotx  )  ; 
String   suffix  =  descriptorName  .  substring  (  dotx  +  1  )  ; 
LocalizedNameGenerator   g  =  new   LocalizedNameGenerator  (  baseName  ,  _locale  ,  "."  +  suffix  )  ; 
List   urls  =  new   ArrayList  (  )  ; 
while  (  g  .  more  (  )  )  { 
String   name  =  g  .  next  (  )  ; 
Resource   l  =  location  .  getRelativeResource  (  name  )  ; 
URL   url  =  l  .  getResourceURL  (  )  ; 
if  (  url  !=  null  )  urls  .  add  (  url  )  ; 
} 
_properties  =  new   XMLProperties  (  )  ; 
int   count  =  urls  .  size  (  )  ; 
boolean   loaded  =  false  ; 
for  (  int   i  =  count  -  1  ;  i  >=  0  &&  !  loaded  ;  i  --  )  { 
URL   url  =  (  URL  )  urls  .  get  (  i  )  ; 
InputStream   stream  =  null  ; 
try  { 
stream  =  url  .  openStream  (  )  ; 
_properties  .  load  (  stream  )  ; 
loaded  =  true  ; 
if  (  _log  .  isDebugEnabled  (  )  )  _log  .  debug  (  "Messages loaded from URL: "  +  url  )  ; 
}  catch  (  IOException   ex  )  { 
if  (  _log  .  isDebugEnabled  (  )  )  _log  .  debug  (  "Unable to load messages from URL: "  +  url  ,  ex  )  ; 
}  finally  { 
if  (  stream  !=  null  )  try  { 
stream  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
} 
} 
} 
if  (  !  loaded  )  { 
_log  .  error  (  "Messages can not be loaded from location: "  +  location  )  ; 
} 
} 





protected   String   findMessage  (  String   key  )  { 
return   _properties  .  getProperty  (  key  )  ; 
} 




protected   Locale   getLocale  (  )  { 
return   _locale  ; 
} 
} 

