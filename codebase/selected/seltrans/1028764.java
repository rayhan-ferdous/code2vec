package   com  .  noahsloan  .  nutils  ; 

import   java  .  beans  .  IntrospectionException  ; 
import   java  .  beans  .  PropertyDescriptor  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashSet  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   javax  .  xml  .  transform  .  OutputKeys  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  dom  .  DOMSource  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   com  .  noahsloan  .  nutils  .  function  .  SafeFunction1  ; 











public   class   Utils  { 










public   abstract   static   class   StaticIterator  <  T  >  implements   Iterator  <  T  >  { 

public   final   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 








public   static  <  T  >  T   getValue  (  T   value  ,  T   def  )  { 
return   value  ==  null  ?  def  :  value  ; 
} 













public   static  <  T  >  T   getValue  (  Object   obj  ,  T   isNull  ,  T   isntNull  )  { 
return   obj  ==  null  ?  isNull  :  isntNull  ; 
} 












public   static  <  K  ,  V  >  V   getValue  (  Map  <  K  ,  V  >  map  ,  K   key  ,  V   def  )  { 
return   getValue  (  map  .  get  (  key  )  ,  def  )  ; 
} 












public   static  <  K  ,  V  >  V   mapDefault  (  Map  <  K  ,  V  >  map  ,  K   key  ,  V   def  )  { 
V   v  =  map  .  get  (  key  )  ; 
if  (  v  ==  null  )  { 
map  .  put  (  key  ,  def  )  ; 
v  =  def  ; 
} 
return   v  ; 
} 









public   static   String   getValue  (  String   s  ,  String   def  )  { 
return   isEmpty  (  s  )  ?  def  :  s  ; 
} 







public   static   boolean   isEmpty  (  Object   o  )  { 
return   isEmpty  (  Utils  .  toString  (  o  )  )  ; 
} 






public   static   boolean   isEmpty  (  Collection  <  ?  >  c  )  { 
return   c  ==  null  ||  c  .  isEmpty  (  )  ; 
} 








public   static   boolean   isEmpty  (  CharSequence   s  )  { 
if  (  s  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  !  Character  .  isWhitespace  (  s  .  charAt  (  i  )  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 








public   static   String   join  (  final   Object  [  ]  array  ,  Object   del  )  { 
return   array  ==  null  ?  ""  :  join  (  array  ,  del  ,  0  ,  array  .  length  )  ; 
} 














public   static   String   join  (  final   Object  [  ]  array  ,  Object   del  ,  final   int   startIndex  ,  final   int   length  )  { 
return   join  (  arrayIterator  (  array  ,  startIndex  ,  length  )  ,  del  )  ; 
} 











public   static   class   ArrayIterable  <  T  >  implements   Iterable  <  T  >  { 

private   final   T  [  ]  array  ; 

private   final   int   startIndex  ; 

private   final   int   length  ; 

public   ArrayIterable  (  final   T  [  ]  array  )  { 
this  (  array  ,  0  ,  array  .  length  )  ; 
} 





public   ArrayIterable  (  final   T  [  ]  array  ,  final   int   startIndex  ,  final   int   length  )  { 
super  (  )  ; 
this  .  array  =  array  ; 
this  .  startIndex  =  startIndex  ; 
this  .  length  =  length  ; 
} 




public   Iterator  <  T  >  iterator  (  )  { 
return   arrayIterator  (  array  ,  startIndex  ,  length  )  ; 
} 




public   static  <  T  >  Iterable  <  T  >  iterable  (  T  [  ]  array  ,  int   startIndex  ,  int   length  )  { 
return   new   ArrayIterable  <  T  >  (  array  ,  startIndex  ,  length  )  ; 
} 
} 








public   static  <  T  >  Iterator  <  T  >  arrayIterator  (  final   T  ...  array  )  { 
return   arrayIterator  (  array  ,  0  ,  array  .  length  )  ; 
} 


























public   static  <  T  >  Iterator  <  T  >  arrayIterator  (  final   T  [  ]  array  ,  final   int   startIndex  ,  final   int   length  )  { 
if  (  length  <  0  )  { 
return   new   StaticIterator  <  T  >  (  )  { 

int   i  =  wrap  (  startIndex  ,  array  .  length  )  ; 

int   remaining  =  array  .  length  >  0  ?  -  length  :  0  ; 

public   boolean   hasNext  (  )  { 
return   remaining  >  0  ; 
} 

public   T   next  (  )  { 
T   t  =  array  [  i  ]  ; 
i  =  wrap  (  i  -  1  ,  array  .  length  )  ; 
remaining  --  ; 
return   t  ; 
} 
}  ; 
} 
return   new   StaticIterator  <  T  >  (  )  { 

int   i  =  wrap  (  startIndex  ,  array  .  length  )  ; 

int   remaining  =  array  .  length  >  0  ?  length  :  0  ; 

public   boolean   hasNext  (  )  { 
return   remaining  >  0  ; 
} 

public   T   next  (  )  { 
T   t  =  array  [  i  ]  ; 
i  =  wrap  (  i  +  1  ,  array  .  length  )  ; 
remaining  --  ; 
return   t  ; 
} 
}  ; 
} 










public   static   int   wrap  (  int   value  ,  int   top  )  { 
if  (  top  <=  0  )  { 
return   0  ; 
} 
value  %=  top  ; 
return   value  <  0  ?  top  +  value  :  value  ; 
} 








public   static   String   join  (  Iterable  <  ?  >  it  ,  Object   del  )  { 
return   join  (  it  .  iterator  (  )  ,  del  )  ; 
} 










public   static   String   join  (  Iterator  <  ?  >  it  ,  Object   del  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
if  (  it  .  hasNext  (  )  )  { 
sb  .  append  (  it  .  next  (  )  )  ; 
while  (  it  .  hasNext  (  )  )  { 
sb  .  append  (  del  )  ; 
sb  .  append  (  it  .  next  (  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 











public   static   String   join  (  Iterator  <  ?  >  it  ,  Object   delimeter  ,  String   def  )  { 
if  (  it  ==  null  ||  !  it  .  hasNext  (  )  )  { 
return   def  ; 
} 
return   join  (  it  ,  delimeter  )  ; 
} 









public   static   String   join  (  Iterable  <  ?  >  it  ,  Object   delimeter  ,  String   def  )  { 
return   join  (  it  .  iterator  (  )  ,  delimeter  ,  def  )  ; 
} 








public   static   String  [  ]  split  (  Object   object  ,  String   del  )  { 
return   split  (  object  ,  del  ,  0  )  ; 
} 









public   static   String  [  ]  split  (  Object   object  ,  String   del  ,  int   limit  )  { 
return   toString  (  object  )  .  split  (  del  ,  limit  )  ; 
} 







public   static   String   toString  (  Object   obj  )  { 
return   toString  (  obj  ,  ""  )  ; 
} 







public   static   String   toString  (  Object   obj  ,  String   def  )  { 
return   obj  ==  null  ?  def  :  obj  .  toString  (  )  ; 
} 












public   static   Method   findMethod  (  Class  <  ?  >  clazz  ,  Class  <  ?  >  returnType  ,  Class  <  ?  >  ...  paramTypes  )  { 
Method  [  ]  methods  =  clazz  .  getMethods  (  )  ; 
methodLoop  :  for  (  Method   method  :  methods  )  { 
Class  <  ?  >  [  ]  types  =  method  .  getParameterTypes  (  )  ; 
if  (  types  .  length  ==  paramTypes  .  length  &&  (  returnType  ==  null  ||  method  .  getReturnType  (  )  .  isAssignableFrom  (  returnType  )  )  )  { 
for  (  int   i  =  0  ;  i  <  types  .  length  ;  i  ++  )  { 
if  (  !  types  [  i  ]  .  isAssignableFrom  (  paramTypes  [  i  ]  )  )  { 
continue   methodLoop  ; 
} 
} 
return   method  ; 
} 
} 
return   null  ; 
} 











public   static   Method   findMethod  (  Object   object  ,  Class  <  ?  >  returnType  ,  Class  <  ?  >  ...  paramTypes  )  { 
return   object  ==  null  ?  null  :  findMethod  (  object  .  getClass  (  )  ,  returnType  ,  paramTypes  )  ; 
} 










public   static  <  T  >  T   get  (  List  <  T  >  list  ,  int   index  )  { 
if  (  list  !=  null  &&  list  .  size  (  )  >  index  &&  index  >=  0  )  { 
return   list  .  get  (  index  )  ; 
} 
return   null  ; 
} 









public   static   boolean   equals  (  Object   o1  ,  Object   o2  )  { 
if  (  o1  ==  o2  )  { 
return   true  ; 
} 
return   o1  !=  null  &&  o1  .  equals  (  o2  )  ; 
} 








public   static  <  T   extends   Comparable  <  ?  super   T  >  >  int   compare  (  T   c1  ,  T   c2  )  { 
return   compare  (  c1  ,  c2  ,  true  )  ; 
} 













public   static  <  T   extends   Comparable  <  ?  super   T  >  >  int   compare  (  T   c1  ,  T   c2  ,  boolean   nullFirst  )  { 
if  (  c1  ==  c2  )  { 
return   0  ; 
} 
if  (  c1  ==  null  )  { 
return   nullFirst  ?  -  1  :  1  ; 
} 
if  (  c2  ==  null  )  { 
return   nullFirst  ?  1  :  -  1  ; 
} 
return   c1  .  compareTo  (  c2  )  ; 
} 










public   static   class   SafeComparator  <  T   extends   Comparable  <  ?  super   T  >  >  implements   Comparator  <  T  >  { 

private   boolean   nullFirst  ; 

public   SafeComparator  (  )  { 
this  (  true  )  ; 
} 

public   SafeComparator  (  boolean   nullFirst  )  { 
super  (  )  ; 
this  .  nullFirst  =  nullFirst  ; 
} 

public   int   compare  (  T   o1  ,  T   o2  )  { 
return   Utils  .  compare  (  o1  ,  o2  ,  nullFirst  )  ; 
} 
} 











public   static   String   resourceToString  (  String   resource  ,  String   encoding  )  throws   IOException  { 
InputStream   stream  =  Utils  .  class  .  getResourceAsStream  (  resource  )  ; 
if  (  stream  ==  null  )  { 
throw   new   FileNotFoundException  (  resource  )  ; 
} 
return   toString  (  stream  ,  encoding  )  ; 
} 












public   static   String   resourceToString  (  String   resource  ,  String   encoding  ,  String   def  )  { 
try  { 
return   resourceToString  (  resource  ,  encoding  )  ; 
}  catch  (  IOException   e  )  { 
return   def  ; 
} 
} 









public   static   String   toString  (  InputStream   in  ,  String   encoding  )  throws   IOException  { 
return   toString  (  new   InputStreamReader  (  in  ,  getCharset  (  encoding  )  )  )  ; 
} 










public   static   String   toString  (  byte  [  ]  bytes  ,  String   encoding  )  throws   UnsupportedEncodingException  { 
return   encoding  ==  null  ?  new   String  (  bytes  )  :  new   String  (  bytes  ,  encoding  )  ; 
} 








public   static   String   toString  (  Reader   reader  )  throws   IOException  { 
StringWriter   writer  =  new   StringWriter  (  )  ; 
writeAll  (  reader  ,  writer  )  ; 
return   writer  .  toString  (  )  ; 
} 








public   static   Charset   getCharset  (  String   charsetName  )  { 
return   charsetName  !=  null  &&  Charset  .  isSupported  (  charsetName  )  ?  Charset  .  forName  (  charsetName  )  :  Charset  .  defaultCharset  (  )  ; 
} 




private   static   final   int   BUFFER_SIZE  =  16384  ; 









public   static   void   writeAll  (  Reader   in  ,  Writer   out  )  throws   IOException  { 
writeAll  (  in  ,  out  ,  new   char  [  BUFFER_SIZE  ]  )  ; 
} 









public   static   void   writeAll  (  Reader   in  ,  Writer   out  ,  char  [  ]  b  )  throws   IOException  { 
int   read  ; 
while  (  (  read  =  in  .  read  (  b  )  )  !=  -  1  )  { 
out  .  write  (  b  ,  0  ,  read  )  ; 
} 
} 









public   static   void   writeAll  (  InputStream   in  ,  OutputStream   out  ,  byte  [  ]  b  )  throws   IOException  { 
int   read  ; 
while  (  (  read  =  in  .  read  (  b  )  )  !=  -  1  )  { 
out  .  write  (  b  ,  0  ,  read  )  ; 
} 
} 









public   static   void   writeAll  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
writeAll  (  in  ,  out  ,  new   byte  [  BUFFER_SIZE  ]  )  ; 
} 

public   static   void   writeAllAndClose  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
writeAll  (  in  ,  out  )  ; 
out  .  close  (  )  ; 
} 






public   static   String   capitalize  (  String   string  )  { 
return   string  !=  null  &&  string  .  length  (  )  >  0  ?  Character  .  toUpperCase  (  string  .  charAt  (  0  )  )  +  string  .  substring  (  1  )  :  ""  ; 
} 






public   static   String   uncapitalize  (  String   string  )  { 
return   string  !=  null  &&  string  .  length  (  )  >  0  ?  Character  .  toLowerCase  (  string  .  charAt  (  0  )  )  +  string  .  substring  (  1  )  :  ""  ; 
} 








@  SuppressWarnings  (  "unchecked"  ) 
public   static   String  [  ]  append  (  String  [  ]  array  ,  String  ...  append  )  { 
return   doAppend  (  new   String  [  array  .  length  +  append  .  length  ]  ,  array  ,  append  )  ; 
} 








public   static   Object  [  ]  append  (  Object  [  ]  array  ,  Object  ...  append  )  { 
return   doAppend  (  new   Object  [  array  .  length  +  append  .  length  ]  ,  array  ,  append  )  ; 
} 












public   static  <  T  >  T  [  ]  doAppend  (  T  [  ]  _new  ,  T  [  ]  first  ,  T  [  ]  second  )  { 
System  .  arraycopy  (  first  ,  0  ,  _new  ,  0  ,  first  .  length  )  ; 
System  .  arraycopy  (  second  ,  0  ,  _new  ,  first  .  length  ,  second  .  length  )  ; 
return   _new  ; 
} 













@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T   getProperty  (  Object   obj  ,  String   property  )  throws   IntrospectionException  ,  IllegalArgumentException  ,  IllegalAccessException  ,  InvocationTargetException  { 
PropertyDescriptor   pd  =  new   PropertyDescriptor  (  property  ,  obj  .  getClass  (  )  )  ; 
return  (  T  )  pd  .  getReadMethod  (  )  .  invoke  (  obj  )  ; 
} 









public   static  <  T  >  T   get  (  T  [  ]  array  ,  int   index  )  { 
return   get  (  array  ,  index  ,  null  )  ; 
} 










public   static  <  T  >  T   get  (  T  [  ]  array  ,  int   index  ,  T   def  )  { 
return   array  !=  null  &&  index  <  array  .  length  &&  index  >=  0  ?  array  [  index  ]  :  def  ; 
} 









public   static  <  T  >  LinkedHashSet  <  T  >  asSet  (  T  ...  items  )  { 
LinkedHashSet  <  T  >  set  =  new   LinkedHashSet  <  T  >  (  )  ; 
if  (  items  !=  null  )  { 
for  (  T   item  :  items  )  { 
set  .  add  (  item  )  ; 
} 
} 
return   set  ; 
} 










public   static  <  K  ,  V  >  Set  <  V  >  getValues  (  Map  <  K  ,  V  >  map  ,  Iterator  <  K  >  keys  )  { 
Set  <  V  >  values  =  new   HashSet  <  V  >  (  )  ; 
while  (  keys  .  hasNext  (  )  )  { 
values  .  add  (  map  .  get  (  keys  .  next  (  )  )  )  ; 
} 
return   values  ; 
} 








public   static   boolean  [  ]  fill  (  boolean  [  ]  array  ,  boolean   value  )  { 
Arrays  .  fill  (  array  ,  value  )  ; 
return   array  ; 
} 








public   static  <  T  >  T  [  ]  fill  (  T  [  ]  array  ,  T   value  )  { 
Arrays  .  fill  (  array  ,  value  )  ; 
return   array  ; 
} 







public   static   byte  [  ]  truncate  (  int  [  ]  bytes  )  { 
byte  [  ]  array  =  new   byte  [  bytes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
array  [  i  ]  =  (  byte  )  bytes  [  i  ]  ; 
} 
return   array  ; 
} 







public   static   char  [  ]  toCharArray  (  int  [  ]  chars  )  { 
char  [  ]  array  =  new   char  [  chars  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
array  [  i  ]  =  (  char  )  chars  [  i  ]  ; 
} 
return   array  ; 
} 








public   static   boolean  [  ]  set  (  boolean  [  ]  array  ,  int   index  )  { 
array  [  index  ]  =  true  ; 
return   array  ; 
} 








public   static   boolean  [  ]  unset  (  boolean  [  ]  array  ,  int   index  )  { 
array  [  index  ]  =  false  ; 
return   array  ; 
} 






public   static   Object  [  ]  prune  (  Object  [  ]  array  )  { 
List  <  Object  >  list  =  new   ArrayList  <  Object  >  (  array  .  length  )  ; 
for  (  Object   o  :  array  )  { 
if  (  o  !=  null  )  { 
list  .  add  (  o  )  ; 
} 
} 
return   list  .  toArray  (  )  ; 
} 






public   static   String  [  ]  prune  (  String  [  ]  array  )  { 
List  <  String  >  list  =  new   ArrayList  <  String  >  (  array  .  length  )  ; 
for  (  String   o  :  array  )  { 
if  (  !  Utils  .  isEmpty  (  o  )  )  { 
list  .  add  (  o  )  ; 
} 
} 
return   list  .  toArray  (  new   String  [  list  .  size  (  )  ]  )  ; 
} 







public   static   String   toStringConstant  (  Object   obj  )  { 
return   obj  ==  null  ?  "null"  :  "\""  +  toString  (  obj  )  .  replaceAll  (  "\""  ,  "\\\""  )  .  replaceAll  (  "\n"  ,  "\\n"  )  +  "\""  ; 
} 







public   static   String   toStringConstantArray  (  Object  ...  array  )  { 
return   toStringConstantArray  (  arrayIterator  (  array  )  )  ; 
} 







public   static   String   toStringConstantArray  (  Iterable  <  ?  >  it  )  { 
return   toStringConstantArray  (  it  .  iterator  (  )  )  ; 
} 










public   static   String   toStringConstantArray  (  Iterator  <  ?  >  it  )  { 
StringBuilder   sb  =  new   StringBuilder  (  "{ "  )  ; 
if  (  it  !=  null  &&  it  .  hasNext  (  )  )  { 
sb  .  append  (  toStringConstant  (  it  .  next  (  )  )  )  ; 
while  (  it  .  hasNext  (  )  )  { 
sb  .  append  (  ", "  )  .  append  (  toStringConstant  (  it  .  next  (  )  )  )  ; 
} 
} 
sb  .  append  (  " }"  )  ; 
return   sb  .  toString  (  )  ; 
} 










public   static   String   toConstantName  (  CharSequence   name  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  name  .  length  (  )  ;  i  ++  )  { 
char   ch  =  name  .  charAt  (  i  )  ; 
if  (  Character  .  isUpperCase  (  ch  )  )  { 
sb  .  append  (  '_'  )  ; 
} 
sb  .  append  (  Character  .  isLetterOrDigit  (  ch  )  ?  Character  .  toUpperCase  (  ch  )  :  '_'  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 








public   static   void   notNull  (  Object   value  ,  String   message  )  throws   NullPointerException  { 
if  (  value  ==  null  )  { 
throw   new   NullPointerException  (  message  )  ; 
} 
} 








public   static   void   notEmpty  (  CharSequence   string  ,  String   message  )  { 
if  (  isEmpty  (  string  )  )  { 
throw   new   IllegalArgumentException  (  message  )  ; 
} 
} 








public   static  <  T  >  List  <  T  >  asList  (  Iterator  <  T  >  it  )  { 
List  <  T  >  list  =  new   ArrayList  <  T  >  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
list  .  add  (  it  .  next  (  )  )  ; 
} 
return   list  ; 
} 










public   static   void   write  (  Document   document  ,  OutputStream   out  )  throws   TransformerException  { 
Transformer   serializer  =  TransformerFactory  .  newInstance  (  )  .  newTransformer  (  )  ; 
serializer  .  setOutputProperty  (  OutputKeys  .  ENCODING  ,  "UTF-8"  )  ; 
serializer  .  setOutputProperty  (  OutputKeys  .  METHOD  ,  "xml"  )  ; 
serializer  .  setOutputProperty  (  OutputKeys  .  INDENT  ,  "yes"  )  ; 
serializer  .  transform  (  new   DOMSource  (  document  )  ,  new   StreamResult  (  out  )  )  ; 
} 













public   static   boolean   createFileParents  (  final   File   file  )  { 
return   file  .  getParentFile  (  )  .  exists  (  )  ||  file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
} 







public   static   String   xmlEscape  (  String   string  )  { 
return   Utils  .  toString  (  string  )  .  replaceAll  (  "&"  ,  "&amp;"  )  .  replaceAll  (  "<"  ,  "&lt;"  )  ; 
} 







public   static   String   xmlAttribEscape  (  String   string  )  { 
return   xmlEscape  (  string  )  .  replaceAll  (  "\""  ,  "&quot;"  )  ; 
} 











public   static   String  [  ]  [  ]  doubleSplit  (  String   string  ,  String   del1  ,  String   del2  ,  int   limit1  ,  int   limit2  )  { 
String  [  ]  split  =  split  (  string  ,  del1  ,  limit1  )  ; 
String  [  ]  [  ]  matrix  =  new   String  [  split  .  length  ]  [  ]  ; 
int   i  =  0  ; 
for  (  String   item  :  split  )  { 
matrix  [  i  ++  ]  =  item  .  split  (  del2  ,  limit2  )  ; 
} 
return   matrix  ; 
} 









public   static   Map  <  String  ,  String  >  asMap  (  String  [  ]  [  ]  keyValues  )  { 
Map  <  String  ,  String  >  map  =  new   HashMap  <  String  ,  String  >  (  )  ; 
for  (  String  [  ]  pair  :  keyValues  )  { 
map  .  put  (  get  (  pair  ,  0  )  ,  get  (  pair  ,  1  )  )  ; 
} 
return   map  ; 
} 









public   static   int   getInt  (  String   number  ,  int   def  )  { 
try  { 
return   Integer  .  parseInt  (  number  )  ; 
}  catch  (  NumberFormatException   e  )  { 
return   def  ; 
} 
} 








public   static   StringBuilder   repeat  (  CharSequence   s  ,  int   times  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  int   i  =  0  ;  i  <  times  ;  i  ++  )  { 
sb  .  append  (  s  )  ; 
} 
return   sb  ; 
} 










public   static   Iterator  <  ?  >  getIterator  (  Object   o  )  { 
if  (  o  ==  null  )  { 
return   Collections  .  EMPTY_LIST  .  iterator  (  )  ; 
}  else   if  (  o  .  getClass  (  )  .  isArray  (  )  )  { 
return   Utils  .  arrayIterator  (  (  Object  [  ]  )  o  )  ; 
}  else   if  (  o   instanceof   Iterable  )  { 
return  (  (  Iterable  <  ?  >  )  o  )  .  iterator  (  )  ; 
}  else  { 
return   Collections  .  singleton  (  o  )  .  iterator  (  )  ; 
} 
} 







public   static   String   toString  (  Throwable   t  )  { 
StringWriter   w  =  new   StringWriter  (  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  w  )  ; 
t  .  printStackTrace  (  pw  )  ; 
pw  .  close  (  )  ; 
return   w  .  toString  (  )  ; 
} 






public   static   String  [  ]  toStringArray  (  Iterator  <  ?  >  it  )  { 
return   toStringArray  (  it  ,  null  )  ; 
} 










public   static   String  [  ]  toStringArray  (  Iterator  <  ?  >  it  ,  String   def  )  { 
List  <  String  >  list  =  new   ArrayList  <  String  >  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
list  .  add  (  toString  (  it  .  next  (  )  ,  def  )  )  ; 
} 
return   list  .  toArray  (  new   String  [  list  .  size  (  )  ]  )  ; 
} 










public   static  <  T   extends   Comparable  <  ?  super   T  >  >  int   binarySearch  (  List  <  T  >  list  ,  T   key  )  { 
return   binarySearch  (  list  ,  key  ,  new   Comparator  <  T  >  (  )  { 

public   int   compare  (  T   o1  ,  T   o2  )  { 
return   o1  .  compareTo  (  o2  )  ; 
} 
}  )  ; 
} 














public   static  <  T  >  int   binarySearch  (  List  <  T  >  list  ,  T   key  ,  Comparator  <  ?  super   T  >  c  )  { 
int   low  =  0  ; 
int   high  =  list  .  size  (  )  -  1  ; 
while  (  low  <=  high  )  { 
int   mid  =  (  low  +  high  )  >  >  >  1  ; 
int   result  =  c  .  compare  (  key  ,  list  .  get  (  mid  )  )  ; 
if  (  result  <  0  )  { 
low  =  mid  +  1  ; 
}  else   if  (  result  >  0  )  { 
high  =  mid  -  1  ; 
}  else  { 
return   mid  ; 
} 
} 
return  -  (  low  +  1  )  ; 
} 

public   static  <  E  >  int   hashCode  (  Iterable  <  E  >  iterable  )  { 
int   h  =  0  ; 
for  (  E   e  :  iterable  )  { 
if  (  e  !=  null  )  { 
h  +=  e  .  hashCode  (  )  ; 
} 
} 
return   h  ; 
} 

public   static   int   hashCode  (  Object   o  )  { 
return   o  ==  null  ?  0  :  o  .  hashCode  (  )  ; 
} 








public   static  <  E  >  StringBuilder   toString  (  Iterable  <  E  >  iterable  )  { 
return   toString  (  iterable  .  iterator  (  )  ,  new   SafeFunction1  <  E  ,  CharSequence  >  (  )  { 

public   String   apply  (  E   p1  )  throws   RuntimeException  { 
return   p1  ==  null  ?  null  :  p1  .  toString  (  )  ; 
} 
}  )  ; 
} 

public   static  <  E  >  StringBuilder   toString  (  Iterable  <  E  >  iterable  ,  SafeFunction1  <  E  ,  ?  extends   CharSequence  >  toString  )  { 
return   toString  (  iterable  .  iterator  (  )  ,  toString  )  ; 
} 








public   static  <  E  >  StringBuilder   toString  (  Iterator  <  E  >  it  ,  SafeFunction1  <  E  ,  ?  extends   CharSequence  >  toString  )  { 
StringBuilder   sb  =  new   StringBuilder  (  "["  )  ; 
if  (  it  .  hasNext  (  )  )  { 
sb  .  append  (  toString  .  apply  (  it  .  next  (  )  )  )  ; 
} 
while  (  it  .  hasNext  (  )  )  { 
sb  .  append  (  ", "  )  .  append  (  toString  .  apply  (  it  .  next  (  )  )  )  ; 
} 
return   sb  ; 
} 
} 

