package   au  .  edu  .  qut  .  yawl  .  engine  .  interfce  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  *  ; 





public   class   InterfaceD  { 

private   boolean   debug  =  false  ; 

private   URL   url  ; 

private   HttpURLConnection   connection  ; 




public   InterfaceD  (  )  { 
} 






public   void   connect  (  String   urlStr  )  throws   IOException  { 
url  =  new   URL  (  urlStr  )  ; 
connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
connection  .  setDoOutput  (  true  )  ; 
connection  .  setDoInput  (  true  )  ; 
connection  .  setRequestMethod  (  "POST"  )  ; 
connection  .  setRequestProperty  (  "Content-Type"  ,  "text/xml"  )  ; 
} 





private   void   setParameters  (  Map   _parameters  )  { 
Map   parameters  =  Collections  .  synchronizedMap  (  new   TreeMap  (  _parameters  )  )  ; 
Set   s  =  _parameters  .  keySet  (  )  ; 
Iterator   it  =  s  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   key  =  it  .  next  (  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "Parameters Map key: "  +  key  .  toString  (  )  +  ", Map value: "  +  parameters  .  get  (  key  )  )  ; 
} 
connection  .  setRequestProperty  (  key  .  toString  (  )  ,  parameters  .  get  (  key  )  .  toString  (  )  )  ; 
} 
} 








public   void   postData  (  String   data  ,  Map   parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
PrintWriter   out  =  new   PrintWriter  (  connection  .  getOutputStream  (  )  )  ; 
out  .  print  (  data  )  ; 
out  .  flush  (  )  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
InputStream   inputStr  =  connection  .  getInputStream  (  )  ; 
InputStreamReader   isReader  =  new   InputStreamReader  (  inputStr  )  ; 
BufferedReader   in  =  new   BufferedReader  (  isReader  )  ; 
String   inputLine  ; 
while  (  (  inputLine  =  in  .  readLine  (  )  )  !=  null  )  { 
result  .  append  (  inputLine  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
connection  .  disconnect  (  )  ; 
} 









public   void   postCommand  (  Map   parameters  )  throws   IOException  { 
setParameters  (  parameters  )  ; 
PrintWriter   out  =  new   PrintWriter  (  connection  .  getOutputStream  (  )  )  ; 
out  .  print  (  ""  )  ; 
out  .  flush  (  )  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
String   inputLine  ; 
while  (  (  inputLine  =  in  .  readLine  (  )  )  !=  null  )  { 
result  .  append  (  inputLine  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
connection  .  disconnect  (  )  ; 
} 
} 

