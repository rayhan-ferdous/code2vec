package   vi  .  crawl  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   org  .  apache  .  commons  .  lang  .  StringEscapeUtils  ; 
import   vi  .  Doc  ; 
import   vi  .  Link  ; 
import   vi  .  Repository  ; 







public   abstract   class   Crawler  { 





protected   Object   queue  ; 




protected   Repository   repository  =  null  ; 




protected   Set  <  URL  >  visitedLinks  =  new   HashSet  <  URL  >  (  )  ; 




protected   int   downloadedLinks  =  0  ; 






protected   abstract   Link   getNextLink  (  )  ; 






protected   abstract   boolean   isEmptyQueue  (  )  ; 







protected   abstract   void   addLinks  (  Link  [  ]  links  ,  int   depth  )  ; 

protected   Crawler  (  Repository   repository  ,  Object   queue  )  { 
this  .  repository  =  repository  ; 
this  .  queue  =  queue  ; 
} 







public   void   crawlURL  (  URL   startUrl  )  throws   Exception  { 
crawlURL  (  startUrl  ,  null  )  ; 
} 








public   void   crawlURL  (  URL   startUrl  ,  Filter   filter  )  throws   Exception  { 
Link   startLink  =  new   Link  (  startUrl  ,  null  )  ; 
addLinks  (  new   Link  [  ]  {  startLink  }  ,  0  )  ; 
String   lastHost  =  ""  ; 
while  (  !  isEmptyQueue  (  )  )  { 
Link   link  =  getNextLink  (  )  ; 
URL   url  =  link  .  getUrl  (  )  ; 
if  (  url  .  getRef  (  )  !=  null  )  { 
String   str  =  url  .  toString  (  )  ; 
url  =  new   URL  (  str  .  substring  (  0  ,  str  .  indexOf  (  '#'  )  )  )  ; 
} 
int   depth  =  link  .  getDepth  (  )  ; 
if  (  filter  !=  null  &&  filter  .  filterDepth  (  depth  )  )  { 
continue  ; 
} 
if  (  visitedLinks  .  contains  (  url  )  )  { 
continue  ; 
} 
if  (  lastHost  .  equals  (  url  .  getHost  (  )  )  )  { 
sleep  (  500  )  ; 
} 
lastHost  =  url  .  getHost  (  )  ==  null  ?  ""  :  url  .  getHost  (  )  ; 
URLConnection   connection  =  getURLConnection  (  url  )  ; 
if  (  connection  ==  null  )  { 
continue  ; 
} 
visitedLinks  .  add  (  url  )  ; 
String   content  ; 
try  { 
content  =  readURL  (  url  )  ; 
}  catch  (  IOException   e  )  { 
continue  ; 
} 
int   fileSize  =  connection  .  getContentLength  (  )  ; 
String   contentType  =  connection  .  getContentType  (  )  ; 
String   title  =  getXMLvalue  (  content  ,  "title"  ,  false  )  ; 
String   body  =  getXMLvalue  (  content  ,  "body"  ,  false  )  ; 
Link  [  ]  links  =  {  }  ; 
if  (  Filter  .  isHtmlFile  (  contentType  )  )  { 
links  =  Link  .  extractLinks  (  content  ,  url  )  ; 
addLinks  (  links  ,  depth  +  1  )  ; 
} 
Doc   doc  =  new   Doc  (  url  ,  title  ,  body  ,  links  )  ; 
if  (  filter  !=  null  )  { 
boolean   filterFile  =  filter  .  filterFile  (  depth  ,  fileSize  ,  contentType  ,  downloadedLinks  ,  doc  )  ; 
if  (  filterFile  )  { 
if  (  filter  .  isDownloadLimitReached  (  downloadedLinks  )  )  { 
return  ; 
} 
continue  ; 
} 
} 
downloadedLinks  ++  ; 
repository  .  processDoc  (  doc  )  ; 
} 
} 

private   void   sleep  (  int   milisecconds  )  { 
try  { 
Thread  .  sleep  (  milisecconds  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 

private   URLConnection   getURLConnection  (  URL   url  )  { 
try  { 
return   url  .  openConnection  (  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 

public   static   String  [  ]  splitXML  (  String   xml  ,  String   tag  )  { 
Pattern   p  =  Pattern  .  compile  (  "</"  +  tag  +  ">"  )  ; 
String  [  ]  items  =  p  .  split  (  xml  )  ; 
return   items  ; 
} 

public   String   getXMLvalue  (  String   xml  ,  String   tag  ,  boolean   strict  )  { 
String   value  =  ""  ; 
String   vP  =  ".+"  ; 
if  (  strict  )  vP  =  "[^<]+"  ; 
Pattern   p  =  Pattern  .  compile  (  "<"  +  tag  +  "[^>]*>("  +  vP  +  ")</"  +  tag  +  ">"  )  ; 
Matcher   m  =  p  .  matcher  (  xml  )  ; 
while  (  m  .  find  (  )  )  { 
value  =  m  .  group  (  1  )  .  trim  (  )  ; 
} 
value  =  StringEscapeUtils  .  unescapeXml  (  value  )  ; 
return   value  ; 
} 

private   static   String   readURL  (  URL   url  )  throws   IOException  { 
BufferedReader   in  =  null  ; 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
try  { 
in  =  new   BufferedReader  (  new   InputStreamReader  (  url  .  openStream  (  )  )  )  ; 
String   str  ; 
while  (  (  str  =  in  .  readLine  (  )  )  !=  null  )  { 
s  .  append  (  str  )  ; 
} 
}  finally  { 
if  (  in  !=  null  )  in  .  close  (  )  ; 
} 
return   s  .  toString  (  )  ; 
} 
} 

