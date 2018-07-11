package   com  .  io_software  .  utils  .  web  ; 

import   java  .  net  .  URL  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  rmi  .  RemoteException  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  Collections  ; 
import   com  .  abb  .  util  .  RequestManager  ; 
import   com  .  abb  .  util  .  Request  ; 
import   ec  .  search  .  SearchEngine  ; 
import   ec  .  search  .  SearchEngineQuery  ; 
import   ec  .  search  .  MetaCrawlerSearchEngine  ; 
import   ec  .  search  .  WebSearchResult  ; 

















public   class   FormContentSearcher  { 







public   FormContentSearcher  (  )  throws   IOException  { 
searchEngine  =  new   MetaCrawlerSearchEngine  (  )  ; 
} 


























public   URL   submitAndSearch  (  Form   form  ,  ActualFormParameters   params  )  throws   Exception  { 
Reader   r  =  form  .  submitForm  (  params  )  ; 
Vector   phrases  =  getLongPhrases  (  r  )  ; 
SearchEngineQuery   query  =  getStrongQuery  (  phrases  )  ; 
System  .  err  .  println  (  "\nLooking for: "  +  query  )  ; 
Set   lookupResults  =  searchEngine  .  search  (  query  ,  null  )  ; 
URL   result  =  null  ; 
if  (  lookupResults  .  size  (  )  >  0  )  { 
int   count  =  0  ; 
for  (  Iterator   i  =  lookupResults  .  iterator  (  )  ;  result  ==  null  &&  count  <  howManySearchResultsToVerify  &&  i  .  hasNext  (  )  ;  count  ++  )  { 
URL   u  =  (  (  WebSearchResult  )  i  .  next  (  )  )  .  getURL  (  )  ; 
if  (  verify  (  u  ,  phrases  )  )  result  =  u  ; 
} 
} 
return   result  ; 
} 















public   boolean   verify  (  URL   url  ,  Vector   phrases  )  throws   IOException  { 
String   contents  =  removeHTMLComments  (  normalizeWhitespaces  (  getURLContents  (  url  )  )  )  ; 
for  (  Enumeration   e  =  phrases  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   phrase  =  (  String  )  e  .  nextElement  (  )  ; 
if  (  contents  .  indexOf  (  phrase  )  <  0  )  return   false  ; 
} 
return   true  ; 
} 












public   double   computePublicResultsRatio  (  Form   form  )  throws   Exception  { 
int   total  =  0  ; 
int   found  =  0  ; 
for  (  Enumeration   pse  =  getRandomParameterSpaceSample  (  form  )  ;  pse  .  hasMoreElements  (  )  ;  total  ++  )  { 
ActualFormParameters   afps  =  (  ActualFormParameters  )  pse  .  nextElement  (  )  ; 
try  { 
URL   url  =  submitAndSearch  (  form  ,  afps  )  ; 
if  (  url  !=  null  )  found  ++  ; 
}  catch  (  IOException   ioe  )  { 
System  .  err  .  println  (  "Exception while searching form results"  )  ; 
ioe  .  printStackTrace  (  )  ; 
System  .  err  .  println  (  "Ignoring and continuing"  )  ; 
} 
} 
return  (  double  )  (  (  (  double  )  found  )  /  (  (  double  )  total  )  )  ; 
} 











private   Enumeration   getRandomParameterSpaceSample  (  Form   form  )  { 
Vector   v  =  new   Vector  (  )  ; 
int   count  =  0  ; 
for  (  Enumeration   e  =  form  .  enumerateParameterSpace  (  )  ;  e  .  hasMoreElements  (  )  &&  count  <  100  ;  count  ++  )  v  .  addElement  (  e  .  nextElement  (  )  )  ; 
return   v  .  elements  (  )  ; 
} 














private   SearchEngineQuery   getStrongQuery  (  Vector   phrases  )  throws   IOException  { 
StringBuffer   queryBuffer  =  new   StringBuffer  (  )  ; 
int   i  =  0  ; 
for  (  Enumeration   e  =  phrases  .  elements  (  )  ;  e  .  hasMoreElements  (  )  &&  i  <  10  ;  i  ++  )  { 
String   phrase  =  (  String  )  e  .  nextElement  (  )  ; 
queryBuffer  .  append  (  "\""  )  ; 
queryBuffer  .  append  (  phrase  )  ; 
queryBuffer  .  append  (  "\""  )  ; 
if  (  e  .  hasMoreElements  (  )  )  queryBuffer  .  append  (  " "  )  ; 
} 
SearchEngineQuery   query  =  new   SearchEngineQuery  (  queryBuffer  .  toString  (  )  ,  SearchEngineQuery  .  ALL_MODE  )  ; 
return   query  ; 
} 









protected   static   String   getURLContents  (  URL   url  )  throws   IOException  { 
Reader   r  =  new   InputStreamReader  (  url  .  openStream  (  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
int   read  =  r  .  read  (  )  ; 
while  (  read  !=  -  1  )  { 
sb  .  append  (  (  char  )  read  )  ; 
read  =  r  .  read  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 









private   String   normalizeWhitespaces  (  String   s  )  { 
String   blanked  =  s  .  replace  (  '\r'  ,  ' '  )  .  replace  (  '\n'  ,  ' '  )  .  replace  (  '\t'  ,  ' '  )  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
boolean   lastWasBlank  =  false  ; 
for  (  int   i  =  0  ;  i  <  blanked  .  length  (  )  ;  i  ++  )  { 
if  (  blanked  .  charAt  (  i  )  !=  ' '  ||  !  lastWasBlank  )  result  .  append  (  blanked  .  charAt  (  i  )  )  ; 
lastWasBlank  =  (  blanked  .  charAt  (  i  )  ==  ' '  )  ; 
} 
return   result  .  toString  (  )  .  trim  (  )  ; 
} 

















public   Vector   getLongPhrases  (  Reader   r  )  throws   IOException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
int   read  =  r  .  read  (  )  ; 
while  (  read  !=  -  1  )  { 
sb  .  append  (  (  char  )  read  )  ; 
read  =  r  .  read  (  )  ; 
} 
String   pageString  =  normalizeWhitespaces  (  sb  .  toString  (  )  )  ; 
Vector   strings  =  new   Vector  (  new   HashSet  (  getASCIIParagraphs  (  pageString  )  )  )  ; 
Collections  .  sort  (  strings  ,  new   Comparator  (  )  { 


public   int   compare  (  Object   o1  ,  Object   o2  )  { 
String   s1  =  (  String  )  o1  ; 
String   s2  =  (  String  )  o2  ; 
return   s2  .  length  (  )  -  s1  .  length  (  )  ; 
} 
}  )  ; 
return   strings  ; 
} 









private   String   getTitle  (  String   page  )  { 
String   result  =  ""  ; 
int   start  =  page  .  indexOf  (  "<title>"  )  ; 
if  (  start  ==  -  1  )  start  =  page  .  indexOf  (  "<TITLE>"  )  ; 
if  (  start  ==  -  1  )  start  =  page  .  indexOf  (  "<Title>"  )  ; 
if  (  start  !=  -  1  )  { 
int   end  =  page  .  indexOf  (  "</title>"  )  ; 
if  (  end  ==  -  1  )  end  =  page  .  indexOf  (  "</TITLE>"  )  ; 
if  (  end  ==  -  1  )  end  =  page  .  indexOf  (  "</Title>"  )  ; 
if  (  end  !=  -  1  )  result  =  page  .  substring  (  start  +  "<title>"  .  length  (  )  ,  end  )  ; 
} 
return   result  .  trim  (  )  ; 
} 




















































private   Vector   getASCIIParagraphs  (  String   rawPage  )  { 
Vector   result  =  new   Vector  (  )  ; 
int   i  =  0  ; 
int   phraseStart  =  0  ; 
int   lastValidPhraseEnd  =  -  1  ; 
int   inTag  =  0  ; 
String   page  =  removeHTMLComments  (  rawPage  )  ; 
while  (  i  <  page  .  length  (  )  )  { 
switch  (  inTag  )  { 
case   0  : 
if  (  page  .  charAt  (  i  )  ==  '<'  )  { 
if  (  phraseStart  !=  -  1  )  { 
addPhrase  (  page  ,  phraseStart  ,  i  ,  result  )  ; 
phraseStart  =  -  1  ; 
lastValidPhraseEnd  =  -  1  ; 
} 
inTag  =  1  ; 
}  else   if  (  page  .  charAt  (  i  )  ==  '&'  ||  isPhraseSeparator  (  page  .  charAt  (  i  )  )  )  { 
if  (  phraseStart  !=  -  1  &&  lastValidPhraseEnd  !=  -  1  )  addPhrase  (  page  ,  phraseStart  ,  lastValidPhraseEnd  ,  result  )  ; 
phraseStart  =  -  1  ; 
lastValidPhraseEnd  =  -  1  ; 
if  (  page  .  charAt  (  i  )  ==  '&'  )  inTag  =  2  ; 
}  else   if  (  whitespaceChars  .  indexOf  (  (  int  )  page  .  charAt  (  i  )  )  !=  -  1  )  { 
if  (  phraseStart  ==  -  1  )  phraseStart  =  i  +  1  ;  else   lastValidPhraseEnd  =  i  ; 
} 
break  ; 
case   1  : 
if  (  page  .  charAt  (  i  )  ==  '>'  )  { 
inTag  =  0  ; 
phraseStart  =  i  +  1  ; 
lastValidPhraseEnd  =  -  1  ; 
} 
break  ; 
case   2  : 
if  (  page  .  charAt  (  i  )  ==  ';'  )  inTag  =  0  ; 
break  ; 
} 
i  ++  ; 
} 
return   result  ; 
} 







private   String   removeHTMLComments  (  String   s  )  { 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  s  .  charAt  (  i  )  ==  '<'  &&  s  .  length  (  )  >  i  +  "!--"  .  length  (  )  &&  s  .  charAt  (  i  +  1  )  ==  '!'  &&  s  .  charAt  (  i  +  2  )  ==  '-'  &&  s  .  charAt  (  i  +  3  )  ==  '-'  )  { 
i  =  s  .  indexOf  (  "-->"  ,  i  )  +  "-->"  .  length  (  )  ; 
if  (  i  ==  -  1  )  i  =  s  .  length  (  )  ; 
i  --  ; 
}  else   result  .  append  (  s  .  charAt  (  i  )  )  ; 
} 
return   result  .  toString  (  )  ; 
} 
















private   void   addPhrase  (  String   page  ,  int   startIndex  ,  int   endIndex  ,  Vector   result  )  { 
final   int   minLength  =  3  ; 
StringBuffer   phrase  =  new   StringBuffer  (  page  .  substring  (  startIndex  ,  endIndex  )  .  trim  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  phrase  .  length  (  )  ;  i  ++  )  { 
if  (  whitespaceChars  .  indexOf  (  (  int  )  phrase  .  charAt  (  i  )  )  !=  -  1  )  phrase  .  setCharAt  (  i  ,  ' '  )  ; 
} 
if  (  phrase  .  length  (  )  >=  minLength  )  result  .  addElement  (  phrase  .  toString  (  )  )  ; 
} 












private   boolean   isPhraseSeparator  (  char   c  )  { 
int   a  =  (  int  )  c  ; 
return  !  (  (  c  >=  'A'  &&  c  <=  'Z'  )  ||  (  c  >=  'a'  &&  c  <=  'z'  )  ||  (  c  >=  '0'  &&  c  <=  '9'  )  ||  c  ==  '_'  ||  c  ==  ' '  )  ; 
} 










public   static   void   main  (  String  [  ]  args  )  { 
System  .  setProperty  (  "java.protocol.handler.pkgs"  ,  "com.sun.net.ssl.internal.www.protocol"  )  ; 
String   rootName  =  null  ; 
if  (  args  .  length  >  0  )  rootName  =  args  [  0  ]  ; 
try  { 
CrawlerManager   cm  =  CrawlerManagerImpl  .  getCrawlerManager  (  rootName  ,  false  ,  false  )  ; 
computePublicResultsRatio  (  cm  )  ; 
System  .  exit  (  0  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  1  )  ; 
} 
} 















private   static   void   computePublicResultsRatio  (  CrawlerManager   cm  )  throws   RemoteException  ,  IOException  { 
FormContentSearcher   searcher  =  new   FormContentSearcher  (  )  ; 
RequestManager   rm  =  new   RequestManager  (  50  )  ; 
for  (  Enumeration   e  =  cm  .  getCrawlers  (  )  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
Crawler   c  =  (  Crawler  )  e  .  nextElement  (  )  ; 
Map   results  =  c  .  getResults  (  )  ; 
for  (  Iterator   r  =  results  .  values  (  )  .  iterator  (  )  ;  r  .  hasNext  (  )  ;  )  { 
Object   o  =  r  .  next  (  )  ; 
if  (  o   instanceof   HTMLPage  )  { 
HTMLPage   page  =  (  HTMLPage  )  o  ; 
for  (  Enumeration   f  =  page  .  getForms  (  )  ;  f  .  hasMoreElements  (  )  ;  )  { 
Form   form  =  (  Form  )  f  .  nextElement  (  )  ; 
if  (  form  .  hasFiniteParameterSpace  (  )  &&  form  .  getPublicResultsRatio  (  )  <  0  )  addRatioComputingRequest  (  searcher  ,  rm  ,  form  )  ; 
} 
} 
} 
} 
rm  .  waitForLastRequestToFinish  (  )  ; 
} 











private   static   void   addRatioComputingRequest  (  FormContentSearcher   searcher  ,  RequestManager   rm  ,  Form   form  )  { 
RatioComputingRequest   request  =  searcher  .  new   RatioComputingRequest  (  form  ,  rm  )  ; 
rm  .  addRequest  (  request  )  ; 
} 















public   class   RatioComputingRequest   implements   Request  { 










public   RatioComputingRequest  (  Form   form  ,  RequestManager   rm  )  { 
this  .  form  =  form  ; 
this  .  rm  =  rm  ; 
} 











public   void   execute  (  )  { 
try  { 
System  .  out  .  println  (  "computing public results ratio for form "  +  "with action URL "  +  form  .  getActionURL  (  )  )  ; 
double   ratio  =  computePublicResultsRatio  (  form  )  ; 
synchronized  (  rm  )  { 
form  .  setPublicResultsRatio  (  ratio  )  ; 
} 
System  .  out  .  println  (  "done with "  +  form  .  getActionURL  (  )  )  ; 
}  catch  (  Exception   e  )  { 
System  .  err  .  println  (  "Exception while trying to compute "  +  "public results ratio for form "  +  form  +  ".\nLeaving ratio unset. "  +  "Exception was:"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 


private   Form   form  ; 


private   RequestManager   rm  ; 
} 


private   SearchEngine   searchEngine  ; 





private   static   final   int   howManySearchResultsToVerify  =  10  ; 




private   static   final   String   whitespaceChars  =  " \n\r\t "  ; 
} 

