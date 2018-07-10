package   org  .  dwgsoftware  .  raistlin  .  repository  .  util  ; 

import   java  .  net  .  URL  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Enumeration  ; 
import   javax  .  naming  .  directory  .  Attribute  ; 
import   javax  .  naming  .  directory  .  Attributes  ; 
import   javax  .  naming  .  directory  .  BasicAttribute  ; 
import   javax  .  naming  .  directory  .  BasicAttributes  ; 
import   org  .  dwgsoftware  .  raistlin  .  repository  .  Artifact  ; 
import   org  .  dwgsoftware  .  raistlin  .  repository  .  RepositoryException  ; 








public   class   RepositoryUtils  { 


public   static   final   String   META  =  "meta"  ; 











public   static   Attributes   getAsAttributes  (  Properties   properties  )  { 
if  (  null  ==  properties  )  throw   new   NullPointerException  (  "properties"  )  ; 
Attributes   l_attrs  =  new   BasicAttributes  (  false  )  ; 
Enumeration   l_list  =  properties  .  propertyNames  (  )  ; 
while  (  l_list  .  hasMoreElements  (  )  )  { 
String   l_key  =  (  String  )  l_list  .  nextElement  (  )  ; 
if  (  isEnumerated  (  l_key  )  )  { 
String   l_keyBase  =  getEnumeratedBase  (  l_key  )  ; 
Attribute   l_attr  =  l_attrs  .  get  (  l_keyBase  )  ; 
if  (  null  ==  l_attr  )  { 
l_attr  =  new   BasicAttribute  (  l_keyBase  ,  false  )  ; 
} 
l_attr  .  add  (  properties  .  getProperty  (  l_key  )  )  ; 
l_attrs  .  put  (  l_attr  )  ; 
}  else  { 
l_attrs  .  put  (  l_key  ,  properties  .  getProperty  (  l_key  )  )  ; 
} 
} 
return   l_attrs  ; 
} 









public   static   Attributes   getAttributes  (  String  [  ]  repositories  ,  Artifact   artifact  )  throws   RepositoryException  { 
return   getAsAttributes  (  getProperties  (  repositories  ,  artifact  )  )  ; 
} 









public   static   Attributes   getAttributes  (  File   cache  ,  Artifact   artifact  )  throws   RepositoryException  { 
return   getAsAttributes  (  getProperties  (  cache  ,  artifact  )  )  ; 
} 










public   static   Properties   getProperties  (  File   cache  ,  Artifact   artifact  )  throws   RepositoryException  { 
File   local  =  new   File  (  cache  ,  artifact  .  getPath  (  )  +  "."  +  META  )  ; 
if  (  !  local  .  exists  (  )  )  { 
final   String   error  =  "Cannot load metadata due to missing resurce."  ; 
Throwable   cause  =  new   FileNotFoundException  (  local  .  toString  (  )  )  ; 
throw   new   RepositoryException  (  error  ,  cause  )  ; 
} 
try  { 
Properties   properties  =  new   Properties  (  )  ; 
InputStream   input  =  new   FileInputStream  (  local  )  ; 
properties  .  load  (  input  )  ; 
return   properties  ; 
}  catch  (  Throwable   e  )  { 
final   String   error  =  "Unexpected error while attempting to load properties from local meta: "  +  local  .  toString  (  )  ; 
throw   new   RepositoryException  (  error  ,  e  )  ; 
} 
} 










public   static   Properties   getProperties  (  String  [  ]  repositories  ,  Artifact   artifact  )  throws   RepositoryException  { 
if  (  null  ==  repositories  )  throw   new   NullPointerException  (  "repositories"  )  ; 
if  (  null  ==  artifact  )  throw   new   NullPointerException  (  "artifact"  )  ; 
Throwable   l_throwable  =  null  ; 
Properties   l_props  =  null  ; 
for  (  int   ii  =  0  ;  ii  <  repositories  .  length  ;  ii  ++  )  { 
StringBuffer   l_buf  =  new   StringBuffer  (  )  ; 
l_buf  .  append  (  artifact  .  getURL  (  repositories  [  ii  ]  )  )  ; 
l_buf  .  append  (  "."  )  ; 
l_buf  .  append  (  META  )  ; 
try  { 
URL   l_url  =  new   URL  (  l_buf  .  toString  (  )  )  ; 
l_props  =  getProperties  (  l_url  )  ; 
return   l_props  ; 
}  catch  (  Throwable   e  )  { 
l_throwable  =  e  ; 
} 
} 
StringBuffer   l_repos  =  new   StringBuffer  (  )  ; 
for  (  int   ii  =  0  ;  ii  <  repositories  .  length  ;  ii  ++  )  { 
l_repos  .  append  (  repositories  [  ii  ]  )  .  append  (  ','  )  ; 
} 
throw   new   RepositoryException  (  "None of the repositories ["  +  l_repos  .  toString  (  )  +  "] contained the metadata properties for "  +  artifact  ,  l_throwable  )  ; 
} 








public   static   Properties   getProperties  (  URL   url  )  throws   IOException  { 
InputStream   l_in  =  null  ; 
Properties   l_props  =  new   Properties  (  )  ; 
l_in  =  url  .  openStream  (  )  ; 
l_props  .  load  (  l_in  )  ; 
if  (  l_in  !=  null  )  { 
l_in  .  close  (  )  ; 
} 
return   l_props  ; 
} 











public   static   boolean   isEnumerated  (  String   key  )  { 
int   l_lastDot  =  key  .  lastIndexOf  (  '.'  )  ; 
String   l_lastComponent  =  null  ; 
if  (  -  1  ==  l_lastDot  )  { 
return   false  ; 
} 
l_lastComponent  =  key  .  substring  (  l_lastDot  +  1  )  ; 
if  (  key  .  equals  (  key  .  substring  (  l_lastDot  )  )  )  { 
return   false  ; 
} 
try  { 
Integer  .  parseInt  (  l_lastComponent  )  ; 
}  catch  (  NumberFormatException   e  )  { 
return   false  ; 
} 
return   true  ; 
} 








public   static   String   getEnumeratedBase  (  String   key  )  { 
if  (  null  ==  key  )  { 
return   null  ; 
} 
if  (  !  isEnumerated  (  key  )  )  { 
return   key  ; 
} 
int   l_lastDot  =  key  .  lastIndexOf  (  '.'  )  ; 
String   l_base  =  null  ; 
if  (  -  1  ==  l_lastDot  )  { 
return   key  ; 
} 
return   key  .  substring  (  0  ,  l_lastDot  )  ; 
} 

public   static   String  [  ]  getDelimited  (  char   a_delim  ,  String   a_substrate  )  { 
int   l_start  =  0  ,  l_end  =  0  ; 
ArrayList   l_list  =  new   ArrayList  (  )  ; 
if  (  null  ==  a_substrate  ||  a_substrate  .  equals  (  ""  )  )  { 
return   null  ; 
} 
while  (  l_end  <  a_substrate  .  length  (  )  )  { 
l_end  =  a_substrate  .  indexOf  (  ','  ,  l_start  )  ; 
if  (  -  1  ==  l_end  )  { 
l_end  =  a_substrate  .  length  (  )  ; 
l_list  .  add  (  a_substrate  .  substring  (  l_start  ,  l_end  )  )  ; 
break  ; 
} 
l_list  .  add  (  a_substrate  .  substring  (  l_start  ,  l_end  )  )  ; 
l_start  =  l_end  +  1  ; 
} 
return  (  String  [  ]  )  l_list  .  toArray  (  new   String  [  0  ]  )  ; 
} 






public   static   URL  [  ]  convertToURLs  (  String  [  ]  hosts  )  { 
ArrayList   list  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  hosts  .  length  ;  i  ++  )  { 
URL   url  =  convertToURL  (  hosts  [  i  ]  )  ; 
if  (  url  !=  null  )  list  .  add  (  url  )  ; 
} 
return  (  URL  [  ]  )  list  .  toArray  (  new   URL  [  0  ]  )  ; 
} 








public   static   URL   convertToURL  (  String   host  )  throws   IllegalArgumentException  { 
try  { 
return   new   URL  (  host  )  ; 
}  catch  (  Throwable   e  )  { 
final   String   error  =  "Unable to convert a supplied host spec to a url: "  +  host  ; 
throw   new   IllegalArgumentException  (  error  )  ; 
} 
} 







public   static   String  [  ]  getCleanPaths  (  String  [  ]  hosts  )  { 
String  [  ]  paths  =  new   String  [  hosts  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  hosts  .  length  ;  i  ++  )  { 
String   path  =  hosts  [  i  ]  ; 
if  (  !  path  .  endsWith  (  "/"  )  )  { 
paths  [  i  ]  =  path  +  "/"  ; 
}  else  { 
paths  [  i  ]  =  path  ; 
} 
} 
return   paths  ; 
} 
} 

