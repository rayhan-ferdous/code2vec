package   de  .  iftm  .  dcm4che  .  services  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  StringTokenizer  ; 
import   org  .  dcm4che  .  data  .  *  ; 
import   org  .  dcm4che  .  dict  .  *  ; 
import   org  .  apache  .  log4j  .  *  ; 










public   class   ConfigProperties   extends   Properties  { 

static   final   Logger   log  =  Logger  .  getLogger  (  "ConfigProperties"  )  ; 

private   static   String   replace  (  String   val  ,  String   from  ,  String   to  )  { 
return   from  .  equals  (  val  )  ?  to  :  val  ; 
} 





public   ConfigProperties  (  )  { 
} 







public   ConfigProperties  (  URL   url  )  throws   IOException  { 
InputStream   in  =  null  ; 
try  { 
load  (  in  =  url  .  openStream  (  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   IOException  (  "Could not load configuration from "  +  url  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ignore  )  { 
} 
} 
} 
} 

public   String   getProperty  (  String   key  ,  String   defaultValue  ,  String   replace  ,  String   to  )  { 
return   replace  (  getProperty  (  key  ,  defaultValue  )  ,  replace  ,  to  )  ; 
} 

public   List   tokenize  (  String   s  ,  List   result  )  { 
StringTokenizer   stk  =  new   StringTokenizer  (  s  ,  ", "  )  ; 
while  (  stk  .  hasMoreTokens  (  )  )  { 
String   tk  =  stk  .  nextToken  (  )  ; 
if  (  tk  .  startsWith  (  "$"  )  )  { 
tokenize  (  getProperty  (  tk  .  substring  (  1  )  ,  ""  )  ,  result  )  ; 
}  else  { 
result  .  add  (  tk  )  ; 
} 
} 
return   result  ; 
} 

public   String  [  ]  tokenize  (  String   s  )  { 
if  (  s  ==  null  )  return   null  ; 
List   l  =  tokenize  (  s  ,  new   LinkedList  (  )  )  ; 
return  (  String  [  ]  )  l  .  toArray  (  new   String  [  l  .  size  (  )  ]  )  ; 
} 


















public   static   File   uriToFile  (  String   uriString  )  { 
URI   baseURI  ; 
URI   uri  ; 
if  (  uriString  ==  null  )  { 
return   null  ; 
} 
if  (  uriString  .  equals  (  ""  )  )  { 
return   null  ; 
} 
try  { 
uri  =  new   URI  (  uriString  )  ; 
if  (  !  uri  .  isAbsolute  (  )  )  { 
baseURI  =  (  new   File  (  System  .  getProperty  (  "user.dir"  )  )  )  .  toURI  (  )  ; 
uri  =  baseURI  .  resolve  (  uri  )  ; 
} 
return   new   File  (  uri  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 


















public   static   URL   fileRefToURL  (  URL   baseURL  ,  String   fileRef  )  throws   URISyntaxException  ,  MalformedURLException  { 
URL   resultURL  =  null  ; 
URI   fileRefURI  ; 
URI   baseURI  ; 
fileRefURI  =  new   URI  (  fileRef  )  ; 
if  (  fileRefURI  .  isAbsolute  (  )  )  { 
resultURL  =  fileRefURI  .  toURL  (  )  ; 
}  else  { 
resultURL  =  new   URL  (  baseURL  ,  fileRef  )  ; 
} 
return   resultURL  ; 
} 
} 

