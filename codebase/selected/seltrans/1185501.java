package   org  .  leo  .  oglexplorer  .  model  .  engine  ; 

import   java  .  awt  .  Container  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  concurrent  .  CopyOnWriteArrayList  ; 
import   javax  .  swing  .  Icon  ; 
import   javax  .  swing  .  JComponent  ; 
import   javax  .  swing  .  JPanel  ; 
import   org  .  leo  .  oglexplorer  .  model  .  result  .  SearchResult  ; 
import   org  .  leo  .  oglexplorer  .  model  .  result  .  SearchType  ; 
import   org  .  leo  .  oglexplorer  .  resources  .  APIKeyNotFoundException  ; 
import   org  .  leo  .  oglexplorer  .  ui  .  task  .  CancelMonitor  ; 
import   org  .  leo  .  oglexplorer  .  util  .  CustomRunnable  ; 













public   abstract   class   SearchEngine  { 


private   String   _words  ; 


private   List  <  SearchResult  >  _searchResults  ; 


private   volatile   int   _index  ; 


private   SearchType   _currentSearchType  ; 


private   boolean   _previousWasNext  ; 


protected   int   _count  ; 


protected   int   _size  ; 






public   SearchEngine  (  )  { 
_currentSearchType  =  SearchType  .  WEB  ; 
_searchResults  =  new   CopyOnWriteArrayList  <  SearchResult  >  (  )  ; 
} 











protected   abstract   List  <  ?  extends   SearchResult  >  searchImpl  (  String   words  ,  int   number  ,  int   offset  ,  SearchType   type  ,  CancelMonitor   cancelMonitor  )  throws   APIKeyNotFoundException  ; 






public   abstract   String   getName  (  )  ; 






public   abstract   SearchType  [  ]  availableSearchTypes  (  )  ; 






public   JComponent   additionalSearchComponent  (  JPanel   parent  )  { 
return   null  ; 
} 






public   CustomRunnable   configPanel  (  Container   parent  )  { 
return   null  ; 
} 






public   abstract   Icon   getLogo  (  )  ; 






public   void   initSearch  (  String   words  )  { 
synchronized  (  this  )  { 
if  (  _searchResults  !=  null  )  { 
final   List  <  SearchResult  >  toDispose  =  new   ArrayList  <  SearchResult  >  (  _searchResults  )  ; 
new   Thread  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
for  (  SearchResult   result  :  toDispose  )  { 
result  .  dispose  (  )  ; 
} 
} 
}  )  .  start  (  )  ; 
_searchResults  .  clear  (  )  ; 
} 
_index  =  0  ; 
try  { 
_words  =  URLEncoder  .  encode  (  words  ,  "UTF-8"  )  ; 
}  catch  (  Exception   e  )  { 
_words  =  words  ; 
} 
_previousWasNext  =  true  ; 
_count  =  10  ; 
} 
} 










public   List  <  ?  extends   SearchResult  >  populateSearch  (  int   count  ,  CancelMonitor   cancelMonitor  )  throws   APIKeyNotFoundException  { 
SearchType   searchType  ; 
synchronized  (  _currentSearchType  )  { 
searchType  =  _currentSearchType  ; 
} 
synchronized  (  this  )  { 
if  (  _words  ==  null  ||  searchType  ==  null  )  { 
return   null  ; 
} 
_count  =  count  ; 
List  <  ?  extends   SearchResult  >  results  =  searchImpl  (  _words  ,  count  ,  _searchResults  .  size  (  )  ,  searchType  ,  cancelMonitor  )  ; 
if  (  results  ==  null  )  { 
return   null  ; 
} 
_searchResults  .  addAll  (  results  )  ; 
_index  +=  count  ; 
return   results  ; 
} 
} 









public   SearchResult   next  (  CancelMonitor   cancelMonitor  )  throws   APIKeyNotFoundException  { 
SearchType   searchType  ; 
synchronized  (  _currentSearchType  )  { 
searchType  =  _currentSearchType  ; 
} 
synchronized  (  this  )  { 
if  (  _words  ==  null  )  { 
return   null  ; 
} 
if  (  !  _previousWasNext  &&  _index  >  0  )  { 
_index  ++  ; 
_previousWasNext  =  true  ; 
} 
if  (  _index  >  _searchResults  .  size  (  )  -  5  )  { 
List  <  ?  extends   SearchResult  >  newPage  =  searchImpl  (  _words  ,  _count  ,  _searchResults  .  size  (  )  ,  searchType  ,  cancelMonitor  )  ; 
if  (  newPage  !=  null  &&  !  newPage  .  isEmpty  (  )  )  { 
_searchResults  .  addAll  (  newPage  )  ; 
} 
} 
if  (  _index  >  2  *  _count  )  { 
_searchResults  .  get  (  _index  -  2  *  _count  )  .  dispose  (  )  ; 
} 
if  (  _index  <  _searchResults  .  size  (  )  )  { 
return   _searchResults  .  get  (  _index  ++  )  ; 
} 
return   null  ; 
} 
} 








public   SearchResult   previous  (  CancelMonitor   cancelMonitor  )  { 
synchronized  (  this  )  { 
if  (  _words  ==  null  )  { 
return   null  ; 
} 
if  (  _index  -  _count  <=  0  )  { 
return   null  ; 
} 
if  (  _index  +  3  *  _count  <  _searchResults  .  size  (  )  )  { 
_searchResults  .  get  (  _searchResults  .  size  (  )  -  1  )  .  dispose  (  )  ; 
} 
if  (  _index  -  _count  -  1  >  0  )  { 
_searchResults  .  get  (  _index  -  _count  -  1  )  .  preload  (  )  ; 
} 
return   _searchResults  .  get  (  (  --  _index  )  -  _count  )  ; 
} 
} 




public   void   dispose  (  )  { 
_searchResults  .  clear  (  )  ; 
} 






public   void   setCurrentSearchType  (  SearchType   currentSearchType  )  { 
synchronized  (  _currentSearchType  )  { 
_currentSearchType  =  currentSearchType  ; 
} 
} 






public   SearchType   getCurrentSearchType  (  )  { 
synchronized  (  _currentSearchType  )  { 
return   _currentSearchType  ; 
} 
} 








public   static   String   call  (  String   url  )  throws   IOException  { 
BufferedReader   bis  =  null  ; 
InputStream   is  =  null  ; 
try  { 
URLConnection   connection  =  new   URL  (  url  )  .  openConnection  (  )  ; 
is  =  connection  .  getInputStream  (  )  ; 
bis  =  new   BufferedReader  (  new   InputStreamReader  (  is  ,  "UTF-8"  )  )  ; 
String   line  =  null  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
while  (  (  line  =  bis  .  readLine  (  )  )  !=  null  )  { 
result  .  append  (  line  )  ; 
} 
return   result  .  toString  (  )  ; 
}  finally  { 
if  (  bis  !=  null  )  { 
try  { 
bis  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
} 

