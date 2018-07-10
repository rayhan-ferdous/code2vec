package   com  .  google  .  gwt  .  dev  .  util  ; 

import   com  .  google  .  gwt  .  core  .  ext  .  TreeLogger  ; 
import   com  .  google  .  gwt  .  core  .  ext  .  UnableToCompleteException  ; 
import   com  .  google  .  gwt  .  core  .  ext  .  typeinfo  .  TypeOracle  ; 
import   com  .  google  .  gwt  .  util  .  tools  .  Utility  ; 
import   org  .  w3c  .  dom  .  Attr  ; 
import   org  .  w3c  .  dom  .  DOMException  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  NamedNodeMap  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  Text  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilenameFilter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  SortedSet  ; 
import   java  .  util  .  TreeSet  ; 






public   final   class   Util  { 

public   static   String   DEFAULT_ENCODING  =  "UTF-8"  ; 

public   static   final   File  [  ]  EMPTY_ARRAY_FILE  =  new   File  [  0  ]  ; 

public   static   final   String  [  ]  EMPTY_ARRAY_STRING  =  new   String  [  0  ]  ; 

public   static   char  [  ]  HEX_CHARS  =  new   char  [  ]  {  '0'  ,  '1'  ,  '2'  ,  '3'  ,  '4'  ,  '5'  ,  '6'  ,  '7'  ,  '8'  ,  '9'  ,  'A'  ,  'B'  ,  'C'  ,  'D'  ,  'E'  ,  'F'  }  ; 






private   static   final   int   THREAD_LOCAL_BUF_SIZE  =  16  *  1024  ; 




private   static   final   ThreadLocal  <  byte  [  ]  >  threadLocalBuf  =  new   ThreadLocal  <  byte  [  ]  >  (  )  ; 

public   static   byte  [  ]  append  (  byte  [  ]  xs  ,  byte   x  )  { 
int   n  =  xs  .  length  ; 
byte  [  ]  t  =  new   byte  [  n  +  1  ]  ; 
System  .  arraycopy  (  xs  ,  0  ,  t  ,  0  ,  n  )  ; 
t  [  n  ]  =  x  ; 
return   t  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  append  (  T  [  ]  xs  ,  T   x  )  { 
int   n  =  xs  .  length  ; 
T  [  ]  t  =  (  T  [  ]  )  Array  .  newInstance  (  xs  .  getClass  (  )  .  getComponentType  (  )  ,  n  +  1  )  ; 
System  .  arraycopy  (  xs  ,  0  ,  t  ,  0  ,  n  )  ; 
t  [  n  ]  =  x  ; 
return   t  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  append  (  T  [  ]  appendToThis  ,  T  [  ]  these  )  { 
if  (  appendToThis  ==  null  )  { 
throw   new   NullPointerException  (  "attempt to append to a null array"  )  ; 
} 
if  (  these  ==  null  )  { 
throw   new   NullPointerException  (  "attempt to append a null array"  )  ; 
} 
T  [  ]  result  ; 
int   newSize  =  appendToThis  .  length  +  these  .  length  ; 
Class  <  ?  >  componentType  =  appendToThis  .  getClass  (  )  .  getComponentType  (  )  ; 
result  =  (  T  [  ]  )  Array  .  newInstance  (  componentType  ,  newSize  )  ; 
System  .  arraycopy  (  appendToThis  ,  0  ,  result  ,  0  ,  appendToThis  .  length  )  ; 
System  .  arraycopy  (  these  ,  0  ,  result  ,  appendToThis  .  length  ,  these  .  length  )  ; 
return   result  ; 
} 







public   static   String   computeStrongName  (  byte  [  ]  content  )  { 
return   computeStrongName  (  new   byte  [  ]  [  ]  {  content  }  )  ; 
} 







public   static   String   computeStrongName  (  byte  [  ]  [  ]  contents  )  { 
MessageDigest   md5  ; 
try  { 
md5  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   RuntimeException  (  "Error initializing MD5"  ,  e  )  ; 
} 
ByteBuffer   b  =  ByteBuffer  .  allocate  (  (  contents  .  length  +  1  )  *  4  )  ; 
b  .  putInt  (  contents  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  contents  .  length  ;  i  ++  )  { 
b  .  putInt  (  contents  [  i  ]  .  length  )  ; 
} 
b  .  flip  (  )  ; 
md5  .  update  (  b  )  ; 
for  (  int   i  =  0  ;  i  <  contents  .  length  ;  i  ++  )  { 
md5  .  update  (  contents  [  i  ]  )  ; 
} 
return   toHexString  (  md5  .  digest  (  )  )  ; 
} 

public   static   void   copy  (  InputStream   is  ,  OutputStream   os  )  throws   IOException  { 
try  { 
copyNoClose  (  is  ,  os  )  ; 
}  finally  { 
Utility  .  close  (  is  )  ; 
Utility  .  close  (  os  )  ; 
} 
} 

public   static   boolean   copy  (  TreeLogger   logger  ,  File   in  ,  File   out  )  throws   UnableToCompleteException  { 
try  { 
if  (  in  .  lastModified  (  )  >  out  .  lastModified  (  )  )  { 
copy  (  logger  ,  new   FileInputStream  (  in  )  ,  out  )  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to open file '"  +  in  .  getAbsolutePath  (  )  +  "'"  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 
} 




public   static   void   copy  (  TreeLogger   logger  ,  InputStream   is  ,  File   out  )  throws   UnableToCompleteException  { 
try  { 
out  .  getParentFile  (  )  .  mkdirs  (  )  ; 
copy  (  logger  ,  is  ,  new   FileOutputStream  (  out  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to create file '"  +  out  .  getAbsolutePath  (  )  +  "'"  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 
} 





public   static   void   copy  (  TreeLogger   logger  ,  InputStream   is  ,  OutputStream   os  )  throws   UnableToCompleteException  { 
try  { 
copy  (  is  ,  os  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Error during copy"  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 
} 

public   static   boolean   copy  (  TreeLogger   logger  ,  URL   in  ,  File   out  )  throws   UnableToCompleteException  { 
try  { 
URLConnection   conn  =  in  .  openConnection  (  )  ; 
if  (  conn  .  getLastModified  (  )  >  out  .  lastModified  (  )  )  { 
copy  (  logger  ,  in  .  openStream  (  )  ,  out  )  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to open '"  +  in  .  toExternalForm  (  )  +  "'"  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 
} 





public   static   void   copyNoClose  (  InputStream   is  ,  OutputStream   os  )  throws   IOException  { 
byte  [  ]  buf  =  takeThreadLocalBuf  (  )  ; 
try  { 
int   i  ; 
while  (  (  i  =  is  .  read  (  buf  )  )  !=  -  1  )  { 
os  .  write  (  buf  ,  0  ,  i  )  ; 
} 
}  finally  { 
releaseThreadLocalBuf  (  buf  )  ; 
} 
} 

public   static   Reader   createReader  (  TreeLogger   logger  ,  URL   url  )  throws   UnableToCompleteException  { 
try  { 
return   new   InputStreamReader  (  url  .  openStream  (  )  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to open resource: "  +  url  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 
} 

public   static   void   deleteFilesInDirectory  (  File   dir  )  { 
File  [  ]  files  =  dir  .  listFiles  (  )  ; 
if  (  files  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
if  (  file  .  isFile  (  )  )  { 
file  .  delete  (  )  ; 
} 
} 
} 
} 




public   static   void   deleteFilesStartingWith  (  File   dir  ,  final   String   prefix  )  { 
File  [  ]  toDelete  =  dir  .  listFiles  (  new   FilenameFilter  (  )  { 

public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   name  .  startsWith  (  prefix  )  ; 
} 
}  )  ; 
if  (  toDelete  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  toDelete  .  length  ;  i  ++  )  { 
toDelete  [  i  ]  .  delete  (  )  ; 
} 
} 
} 




public   static   String   escapeXml  (  String   unescaped  )  { 
StringBuilder   builder  =  new   StringBuilder  (  )  ; 
escapeXml  (  unescaped  ,  0  ,  unescaped  .  length  (  )  ,  true  ,  builder  )  ; 
return   builder  .  toString  (  )  ; 
} 














public   static   void   escapeXml  (  String   code  ,  int   start  ,  int   end  ,  boolean   quoteApostrophe  ,  StringBuilder   builder  )  { 
int   lastIndex  =  0  ; 
int   len  =  end  -  start  ; 
char  [  ]  c  =  new   char  [  len  ]  ; 
code  .  getChars  (  start  ,  end  ,  c  ,  0  )  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
switch  (  c  [  i  ]  )  { 
case  '&'  : 
builder  .  append  (  c  ,  lastIndex  ,  i  -  lastIndex  )  ; 
builder  .  append  (  "&amp;"  )  ; 
lastIndex  =  i  +  1  ; 
break  ; 
case  '>'  : 
builder  .  append  (  c  ,  lastIndex  ,  i  -  lastIndex  )  ; 
builder  .  append  (  "&gt;"  )  ; 
lastIndex  =  i  +  1  ; 
break  ; 
case  '<'  : 
builder  .  append  (  c  ,  lastIndex  ,  i  -  lastIndex  )  ; 
builder  .  append  (  "&lt;"  )  ; 
lastIndex  =  i  +  1  ; 
break  ; 
case  '\"'  : 
builder  .  append  (  c  ,  lastIndex  ,  i  -  lastIndex  )  ; 
builder  .  append  (  "&quot;"  )  ; 
lastIndex  =  i  +  1  ; 
break  ; 
case  '\''  : 
if  (  quoteApostrophe  )  { 
builder  .  append  (  c  ,  lastIndex  ,  i  -  lastIndex  )  ; 
builder  .  append  (  "&apos;"  )  ; 
lastIndex  =  i  +  1  ; 
} 
break  ; 
default  : 
break  ; 
} 
} 
builder  .  append  (  c  ,  lastIndex  ,  len  -  lastIndex  )  ; 
} 

public   static   URL   findSourceInClassPath  (  ClassLoader   cl  ,  String   sourceTypeName  )  { 
String   toTry  =  sourceTypeName  .  replace  (  '.'  ,  '/'  )  +  ".java"  ; 
URL   foundURL  =  cl  .  getResource  (  toTry  )  ; 
if  (  foundURL  !=  null  )  { 
return   foundURL  ; 
} 
int   i  =  sourceTypeName  .  lastIndexOf  (  '.'  )  ; 
if  (  i  !=  -  1  )  { 
return   findSourceInClassPath  (  cl  ,  sourceTypeName  .  substring  (  0  ,  i  )  )  ; 
}  else  { 
return   null  ; 
} 
} 




public   static   byte  [  ]  getBytes  (  String   s  )  { 
try  { 
return   s  .  getBytes  (  DEFAULT_ENCODING  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   RuntimeException  (  "The JVM does not support the compiler's default encoding."  ,  e  )  ; 
} 
} 





public   static   byte  [  ]  [  ]  getBytes  (  String  [  ]  s  )  { 
byte  [  ]  [  ]  bytes  =  new   byte  [  s  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  ;  i  ++  )  { 
bytes  [  i  ]  =  getBytes  (  s  [  i  ]  )  ; 
} 
return   bytes  ; 
} 





public   static   String   getClassName  (  Class  <  ?  >  cls  )  { 
return   getClassName  (  cls  .  getName  (  )  )  ; 
} 





public   static   String   getClassName  (  String   className  )  { 
return   className  .  substring  (  className  .  lastIndexOf  (  '.'  )  +  1  )  ; 
} 







public   static   String   getFileFromInstallPath  (  String   relativePath  )  { 
String   installPath  =  Utility  .  getInstallPath  (  )  ; 
File   file  =  new   File  (  installPath  +  '/'  +  relativePath  )  ; 
return   readFileAsString  (  file  )  ; 
} 




public   static   void   hex4  (  char   c  ,  StringBuffer   sb  )  { 
sb  .  append  (  HEX_CHARS  [  (  c  &  0xF000  )  >  >  12  ]  )  ; 
sb  .  append  (  HEX_CHARS  [  (  c  &  0x0F00  )  >  >  8  ]  )  ; 
sb  .  append  (  HEX_CHARS  [  (  c  &  0x00F0  )  >  >  4  ]  )  ; 
sb  .  append  (  HEX_CHARS  [  c  &  0x000F  ]  )  ; 
} 










public   static   void   invokeInaccessableMethod  (  Class  <  ?  >  targetClass  ,  String   methodName  ,  Class  <  ?  >  [  ]  argumentTypes  ,  TypeOracle   target  ,  Object  [  ]  arguments  )  { 
String   failedReflectErrMsg  =  "The definition of "  +  targetClass  .  getName  (  )  +  "."  +  methodName  +  " has changed in an "  +  "incompatible way."  ; 
try  { 
Method   m  =  targetClass  .  getDeclaredMethod  (  methodName  ,  argumentTypes  )  ; 
m  .  setAccessible  (  true  )  ; 
m  .  invoke  (  target  ,  arguments  )  ; 
}  catch  (  NoSuchMethodException   e  )  { 
throw   new   RuntimeException  (  failedReflectErrMsg  ,  e  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
throw   new   RuntimeException  (  failedReflectErrMsg  ,  e  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
throw   new   RuntimeException  (  failedReflectErrMsg  ,  e  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
throw   new   RuntimeException  (  e  .  getTargetException  (  )  )  ; 
} 
} 

public   static   boolean   isCompilationUnitOnDisk  (  String   loc  )  { 
try  { 
if  (  new   File  (  loc  )  .  exists  (  )  )  { 
return   true  ; 
} 
URL   url  =  new   URL  (  loc  )  ; 
String   s  =  url  .  toExternalForm  (  )  ; 
if  (  s  .  startsWith  (  "file:"  )  ||  s  .  startsWith  (  "jar:file:"  )  ||  s  .  startsWith  (  "zip:file:"  )  )  { 
return   true  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
} 
return   false  ; 
} 

public   static   boolean   isValidJavaIdent  (  String   token  )  { 
if  (  token  .  length  (  )  ==  0  )  { 
return   false  ; 
} 
if  (  !  Character  .  isJavaIdentifierStart  (  token  .  charAt  (  0  )  )  )  { 
return   false  ; 
} 
for  (  int   i  =  1  ,  n  =  token  .  length  (  )  ;  i  <  n  ;  i  ++  )  { 
if  (  !  Character  .  isJavaIdentifierPart  (  token  .  charAt  (  i  )  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 

public   static   void   logMissingTypeErrorWithHints  (  TreeLogger   logger  ,  String   missingType  )  { 
logger  =  logger  .  branch  (  TreeLogger  .  ERROR  ,  "Unable to find type '"  +  missingType  +  "'"  ,  null  )  ; 
ClassLoader   cl  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  ; 
URL   sourceURL  =  findSourceInClassPath  (  cl  ,  missingType  )  ; 
if  (  sourceURL  !=  null  )  { 
if  (  missingType  .  indexOf  (  ".client."  )  !=  -  1  )  { 
Messages  .  HINT_PRIOR_COMPILER_ERRORS  .  log  (  logger  ,  null  )  ; 
Messages  .  HINT_CHECK_MODULE_INHERITANCE  .  log  (  logger  ,  null  )  ; 
}  else  { 
if  (  findSourceInClassPath  (  cl  ,  missingType  )  ==  null  )  { 
Messages  .  HINT_CHECK_MODULE_NONCLIENT_SOURCE_DECL  .  log  (  logger  ,  null  )  ; 
}  else  { 
Messages  .  HINT_PRIOR_COMPILER_ERRORS  .  log  (  logger  ,  null  )  ; 
} 
} 
}  else   if  (  !  missingType  .  equals  (  "java.lang.Object"  )  )  { 
Messages  .  HINT_CHECK_TYPENAME  .  log  (  logger  ,  missingType  ,  null  )  ; 
Messages  .  HINT_CHECK_CLASSPATH_SOURCE_ENTRIES  .  log  (  logger  ,  null  )  ; 
} 
if  (  missingType  .  indexOf  (  "java.lang."  )  ==  0  )  { 
Messages  .  HINT_CHECK_INHERIT_CORE  .  log  (  logger  ,  null  )  ; 
}  else   if  (  missingType  .  indexOf  (  "com.google.gwt.core."  )  ==  0  )  { 
Messages  .  HINT_CHECK_INHERIT_CORE  .  log  (  logger  ,  null  )  ; 
}  else   if  (  missingType  .  indexOf  (  "com.google.gwt.user."  )  ==  0  )  { 
Messages  .  HINT_CHECK_INHERIT_USER  .  log  (  logger  ,  null  )  ; 
} 
} 









public   static   File   makeRelativeFile  (  File   from  ,  File   to  )  { 
String   toPath  =  tryMakeCanonical  (  to  )  .  getAbsolutePath  (  )  ; 
File   currentFrom  =  tryMakeCanonical  (  from  .  isDirectory  (  )  ?  from  :  from  .  getParentFile  (  )  )  ; 
int   numberOfBackups  =  0  ; 
while  (  currentFrom  !=  null  )  { 
String   currentFromPath  =  currentFrom  .  getPath  (  )  ; 
if  (  toPath  .  startsWith  (  currentFromPath  )  )  { 
break  ; 
}  else  { 
++  numberOfBackups  ; 
currentFrom  =  currentFrom  .  getParentFile  (  )  ; 
} 
} 
if  (  currentFrom  ==  null  )  { 
return   null  ; 
} 
String   trailingToPath  =  toPath  .  substring  (  currentFrom  .  getAbsolutePath  (  )  .  length  (  )  )  ; 
if  (  currentFrom  .  getParentFile  (  )  !=  null  &&  trailingToPath  .  length  (  )  >  0  )  { 
trailingToPath  =  trailingToPath  .  substring  (  1  )  ; 
} 
File   relativeFile  =  new   File  (  trailingToPath  )  ; 
for  (  int   i  =  0  ;  i  <  numberOfBackups  ;  ++  i  )  { 
relativeFile  =  new   File  (  ".."  ,  relativeFile  .  getPath  (  )  )  ; 
} 
return   relativeFile  ; 
} 

public   static   String   makeRelativePath  (  File   from  ,  File   to  )  { 
File   f  =  makeRelativeFile  (  from  ,  to  )  ; 
return  (  f  !=  null  ?  f  .  getPath  (  )  :  null  )  ; 
} 

public   static   String   makeRelativePath  (  File   from  ,  String   to  )  { 
File   f  =  makeRelativeFile  (  from  ,  new   File  (  to  )  )  ; 
return  (  f  !=  null  ?  f  .  getPath  (  )  :  null  )  ; 
} 




public   static   void   maybeDumpSource  (  TreeLogger   logger  ,  String   location  ,  String   source  ,  String   typeName  )  { 
if  (  isCompilationUnitOnDisk  (  location  )  )  { 
return  ; 
} 
if  (  !  logger  .  isLoggable  (  TreeLogger  .  INFO  )  )  { 
return  ; 
} 
File   tmpSrc  ; 
Throwable   caught  =  null  ; 
try  { 
tmpSrc  =  File  .  createTempFile  (  typeName  ,  ".java"  )  ; 
writeStringAsFile  (  tmpSrc  ,  source  )  ; 
String   dumpPath  =  tmpSrc  .  getAbsolutePath  (  )  ; 
logger  .  log  (  TreeLogger  .  INFO  ,  "See snapshot: "  +  dumpPath  ,  null  )  ; 
return  ; 
}  catch  (  IOException   e  )  { 
caught  =  e  ; 
} 
logger  .  log  (  TreeLogger  .  INFO  ,  "Unable to dump source to disk"  ,  caught  )  ; 
} 

public   static   byte  [  ]  readFileAsBytes  (  File   file  )  { 
FileInputStream   fileInputStream  =  null  ; 
try  { 
fileInputStream  =  new   FileInputStream  (  file  )  ; 
int   length  =  (  int  )  file  .  length  (  )  ; 
return   readBytesFromInputStream  (  fileInputStream  ,  length  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
}  finally  { 
Utility  .  close  (  fileInputStream  )  ; 
} 
} 

public   static   char  [  ]  readFileAsChars  (  File   file  )  { 
String   string  =  readFileAsString  (  file  )  ; 
if  (  string  !=  null  )  { 
return   string  .  toCharArray  (  )  ; 
} 
return   null  ; 
} 

public   static  <  T   extends   Serializable  >  T   readFileAsObject  (  File   file  ,  Class  <  T  >  type  )  throws   ClassNotFoundException  ,  IOException  { 
FileInputStream   fileInputStream  =  null  ; 
try  { 
fileInputStream  =  new   FileInputStream  (  file  )  ; 
return   readStreamAsObject  (  fileInputStream  ,  type  )  ; 
}  finally  { 
Utility  .  close  (  fileInputStream  )  ; 
} 
} 

public   static   String   readFileAsString  (  File   file  )  { 
byte  [  ]  bytes  =  readFileAsBytes  (  file  )  ; 
if  (  bytes  !=  null  )  { 
return   toString  (  bytes  ,  DEFAULT_ENCODING  )  ; 
} 
return   null  ; 
} 







public   static   String   readNextLine  (  BufferedReader   br  )  { 
try  { 
String   line  =  br  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
line  =  line  .  trim  (  )  ; 
if  (  line  .  length  (  )  >  0  )  { 
break  ; 
} 
line  =  br  .  readLine  (  )  ; 
} 
return   line  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 

public   static  <  T  >  T   readStreamAsObject  (  InputStream   inputStream  ,  Class  <  T  >  type  )  throws   ClassNotFoundException  ,  IOException  { 
ObjectInputStream   objectInputStream  =  null  ; 
try  { 
objectInputStream  =  new   ObjectInputStream  (  inputStream  )  ; 
return   type  .  cast  (  objectInputStream  .  readObject  (  )  )  ; 
}  finally  { 
Utility  .  close  (  objectInputStream  )  ; 
} 
} 




public   static   String   readStreamAsString  (  InputStream   in  )  { 
try  { 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  1024  )  ; 
copy  (  in  ,  out  )  ; 
return   out  .  toString  (  DEFAULT_ENCODING  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   RuntimeException  (  "The JVM does not support the compiler's default encoding."  ,  e  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 




public   static   byte  [  ]  readURLAsBytes  (  URL   url  )  { 
try  { 
URLConnection   conn  =  url  .  openConnection  (  )  ; 
conn  .  setUseCaches  (  false  )  ; 
return   readURLConnectionAsBytes  (  conn  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 




public   static   char  [  ]  readURLAsChars  (  URL   url  )  { 
byte  [  ]  bytes  =  readURLAsBytes  (  url  )  ; 
if  (  bytes  !=  null  )  { 
return   toString  (  bytes  ,  DEFAULT_ENCODING  )  .  toCharArray  (  )  ; 
} 
return   null  ; 
} 




public   static   String   readURLAsString  (  URL   url  )  { 
byte  [  ]  bytes  =  readURLAsBytes  (  url  )  ; 
if  (  bytes  !=  null  )  { 
return   toString  (  bytes  ,  DEFAULT_ENCODING  )  ; 
} 
return   null  ; 
} 

public   static   byte  [  ]  readURLConnectionAsBytes  (  URLConnection   connection  )  { 
InputStream   input  =  null  ; 
try  { 
input  =  connection  .  getInputStream  (  )  ; 
int   contentLength  =  connection  .  getContentLength  (  )  ; 
if  (  contentLength  <  0  )  { 
return   null  ; 
} 
return   readBytesFromInputStream  (  input  ,  contentLength  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
}  finally  { 
Utility  .  close  (  input  )  ; 
} 
} 












public   static   void   recursiveDelete  (  File   file  ,  boolean   childrenOnly  )  { 
recursiveDelete  (  file  ,  childrenOnly  ,  null  )  ; 
} 














public   static   void   recursiveDelete  (  File   file  ,  boolean   childrenOnly  ,  FileFilter   filter  )  { 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  children  =  file  .  listFiles  (  )  ; 
if  (  children  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
recursiveDelete  (  children  [  i  ]  ,  false  ,  filter  )  ; 
} 
} 
if  (  childrenOnly  )  { 
return  ; 
} 
} 
if  (  filter  ==  null  ||  filter  .  accept  (  file  )  )  { 
file  .  delete  (  )  ; 
} 
} 









public   static   SortedSet  <  String  >  recursiveListPartialPaths  (  File   parent  ,  boolean   includeDirs  )  { 
assert   parent  !=  null  ; 
TreeSet  <  String  >  toReturn  =  new   TreeSet  <  String  >  (  )  ; 
int   start  =  parent  .  getAbsolutePath  (  )  .  length  (  )  +  1  ; 
List  <  File  >  q  =  new   LinkedList  <  File  >  (  )  ; 
q  .  add  (  parent  )  ; 
while  (  !  q  .  isEmpty  (  )  )  { 
File   f  =  q  .  remove  (  0  )  ; 
if  (  f  .  isDirectory  (  )  )  { 
if  (  includeDirs  )  { 
toReturn  .  add  (  f  .  getAbsolutePath  (  )  .  substring  (  start  )  )  ; 
} 
q  .  addAll  (  Arrays  .  asList  (  f  .  listFiles  (  )  )  )  ; 
}  else  { 
toReturn  .  add  (  f  .  getAbsolutePath  (  )  .  substring  (  start  )  )  ; 
} 
} 
return   toReturn  ; 
} 





public   static   void   releaseThreadLocalBuf  (  byte  [  ]  buf  )  { 
assert   buf  .  length  ==  THREAD_LOCAL_BUF_SIZE  ; 
threadLocalBuf  .  set  (  buf  )  ; 
} 

public   static   File   removeExtension  (  File   file  )  { 
String   name  =  file  .  getName  (  )  ; 
int   lastDot  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  lastDot  !=  -  1  )  { 
name  =  name  .  substring  (  0  ,  lastDot  )  ; 
} 
return   new   File  (  file  .  getParentFile  (  )  ,  name  )  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  removeNulls  (  T  [  ]  a  )  { 
int   n  =  a  .  length  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
if  (  a  [  i  ]  ==  null  )  { 
--  n  ; 
} 
} 
Class  <  ?  >  componentType  =  a  .  getClass  (  )  .  getComponentType  (  )  ; 
T  [  ]  t  =  (  T  [  ]  )  Array  .  newInstance  (  componentType  ,  n  )  ; 
int   out  =  0  ; 
for  (  int   in  =  0  ;  in  <  t  .  length  ;  in  ++  )  { 
if  (  a  [  in  ]  !=  null  )  { 
t  [  out  ++  ]  =  a  [  in  ]  ; 
} 
} 
return   t  ; 
} 





public   static   String   slashify  (  String   path  )  { 
path  =  path  .  replace  (  File  .  separatorChar  ,  '/'  )  ; 
if  (  path  .  endsWith  (  "/"  )  )  { 
path  =  path  .  substring  (  0  ,  path  .  length  (  )  -  1  )  ; 
} 
return   path  ; 
} 









public   static   byte  [  ]  takeThreadLocalBuf  (  )  { 
byte  [  ]  buf  =  threadLocalBuf  .  get  (  )  ; 
if  (  buf  ==  null  )  { 
buf  =  new   byte  [  THREAD_LOCAL_BUF_SIZE  ]  ; 
}  else  { 
threadLocalBuf  .  set  (  null  )  ; 
} 
return   buf  ; 
} 









@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  toArray  (  Class  <  ?  super   T  >  componentType  ,  Collection  <  ?  extends   T  >  coll  )  { 
int   n  =  coll  .  size  (  )  ; 
T  [  ]  a  =  (  T  [  ]  )  Array  .  newInstance  (  componentType  ,  n  )  ; 
return   coll  .  toArray  (  a  )  ; 
} 





@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  toArrayReversed  (  Class  <  ?  super   T  >  componentType  ,  Collection  <  ?  extends   T  >  coll  )  { 
int   n  =  coll  .  size  (  )  ; 
T  [  ]  a  =  (  T  [  ]  )  Array  .  newInstance  (  componentType  ,  n  )  ; 
int   i  =  n  -  1  ; 
for  (  Iterator  <  ?  extends   T  >  iter  =  coll  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  --  i  )  { 
a  [  i  ]  =  iter  .  next  (  )  ; 
} 
return   a  ; 
} 









public   static   String   toHexString  (  byte  [  ]  bytes  )  { 
char  [  ]  hexString  =  new   char  [  2  *  bytes  .  length  ]  ; 
int   j  =  0  ; 
for  (  int   i  =  0  ;  i  <  bytes  .  length  ;  i  ++  )  { 
hexString  [  j  ++  ]  =  Util  .  HEX_CHARS  [  (  bytes  [  i  ]  &  0xF0  )  >  >  4  ]  ; 
hexString  [  j  ++  ]  =  Util  .  HEX_CHARS  [  bytes  [  i  ]  &  0x0F  ]  ; 
} 
return   new   String  (  hexString  )  ; 
} 





public   static   String   toString  (  byte  [  ]  bytes  )  { 
return   toString  (  bytes  ,  DEFAULT_ENCODING  )  ; 
} 




public   static   String  [  ]  toStringArray  (  Collection  <  String  >  coll  )  { 
return   toArray  (  String  .  class  ,  coll  )  ; 
} 

public   static   String  [  ]  toStrings  (  byte  [  ]  [  ]  bytes  )  { 
String  [  ]  strings  =  new   String  [  bytes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  bytes  .  length  ;  i  ++  )  { 
strings  [  i  ]  =  toString  (  bytes  [  i  ]  )  ; 
} 
return   strings  ; 
} 

public   static   URL   toURL  (  File   f  )  { 
try  { 
return   f  .  toURI  (  )  .  toURL  (  )  ; 
}  catch  (  MalformedURLException   e  )  { 
throw   new   RuntimeException  (  "Failed to convert a File to a URL"  ,  e  )  ; 
} 
} 

public   static   String   toXml  (  Document   doc  )  { 
Throwable   caught  =  null  ; 
try  { 
byte  [  ]  bytes  =  toXmlUtf8  (  doc  )  ; 
return   new   String  (  bytes  ,  DEFAULT_ENCODING  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
caught  =  e  ; 
} 
throw   new   RuntimeException  (  "Unable to encode xml string as utf-8"  ,  caught  )  ; 
} 

public   static   byte  [  ]  toXmlUtf8  (  Document   doc  )  { 
Throwable   caught  =  null  ; 
try  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
writeDocument  (  pw  ,  doc  )  ; 
return   sw  .  toString  (  )  .  getBytes  (  DEFAULT_ENCODING  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
caught  =  e  ; 
}  catch  (  IOException   e  )  { 
caught  =  e  ; 
} 
throw   new   RuntimeException  (  "Unable to encode xml document object as a string"  ,  caught  )  ; 
} 

public   static   File   tryCombine  (  File   parentMaybeIgnored  ,  File   childMaybeAbsolute  )  { 
if  (  childMaybeAbsolute  ==  null  )  { 
return   parentMaybeIgnored  ; 
}  else   if  (  childMaybeAbsolute  .  isAbsolute  (  )  )  { 
return   childMaybeAbsolute  ; 
}  else  { 
return   new   File  (  parentMaybeIgnored  ,  childMaybeAbsolute  .  getPath  (  )  )  ; 
} 
} 

public   static   File   tryCombine  (  File   parentMaybeIgnored  ,  String   childMaybeAbsolute  )  { 
return   tryCombine  (  parentMaybeIgnored  ,  new   File  (  childMaybeAbsolute  )  )  ; 
} 







public   static   File   tryMakeCanonical  (  File   file  )  { 
try  { 
return   file  .  getCanonicalFile  (  )  ; 
}  catch  (  IOException   e  )  { 
return   file  ; 
} 
} 

public   static   void   writeBytesToFile  (  TreeLogger   logger  ,  File   where  ,  byte  [  ]  what  )  throws   UnableToCompleteException  { 
writeBytesToFile  (  logger  ,  where  ,  new   byte  [  ]  [  ]  {  what  }  )  ; 
} 




public   static   void   writeBytesToFile  (  TreeLogger   logger  ,  File   where  ,  byte  [  ]  [  ]  what  )  throws   UnableToCompleteException  { 
FileOutputStream   f  =  null  ; 
Throwable   caught  ; 
try  { 
where  .  getParentFile  (  )  .  mkdirs  (  )  ; 
f  =  new   FileOutputStream  (  where  )  ; 
for  (  int   i  =  0  ;  i  <  what  .  length  ;  i  ++  )  { 
f  .  write  (  what  [  i  ]  )  ; 
} 
return  ; 
}  catch  (  FileNotFoundException   e  )  { 
caught  =  e  ; 
}  catch  (  IOException   e  )  { 
caught  =  e  ; 
}  finally  { 
Utility  .  close  (  f  )  ; 
} 
String   msg  =  "Unable to write file '"  +  where  +  "'"  ; 
logger  .  log  (  TreeLogger  .  ERROR  ,  msg  ,  caught  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
} 

public   static   void   writeCharsAsFile  (  TreeLogger   logger  ,  File   file  ,  char  [  ]  chars  )  throws   UnableToCompleteException  { 
FileOutputStream   stream  =  null  ; 
OutputStreamWriter   writer  =  null  ; 
BufferedWriter   buffered  =  null  ; 
try  { 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
stream  =  new   FileOutputStream  (  file  )  ; 
writer  =  new   OutputStreamWriter  (  stream  ,  DEFAULT_ENCODING  )  ; 
buffered  =  new   BufferedWriter  (  writer  )  ; 
buffered  .  write  (  chars  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to write file: "  +  file  .  getAbsolutePath  (  )  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
}  finally  { 
Utility  .  close  (  buffered  )  ; 
Utility  .  close  (  writer  )  ; 
Utility  .  close  (  stream  )  ; 
} 
} 




public   static   void   writeObjectAsFile  (  TreeLogger   logger  ,  File   file  ,  Object  ...  objects  )  throws   UnableToCompleteException  { 
FileOutputStream   stream  =  null  ; 
try  { 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
stream  =  new   FileOutputStream  (  file  )  ; 
writeObjectToStream  (  stream  ,  objects  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to write file: "  +  file  .  getAbsolutePath  (  )  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
}  finally  { 
Utility  .  close  (  stream  )  ; 
} 
} 




public   static   void   writeObjectToStream  (  OutputStream   stream  ,  Object  ...  objects  )  throws   IOException  { 
ObjectOutputStream   objectStream  =  null  ; 
objectStream  =  new   ObjectOutputStream  (  stream  )  ; 
for  (  Object   object  :  objects  )  { 
objectStream  .  writeObject  (  object  )  ; 
} 
objectStream  .  flush  (  )  ; 
} 

public   static   boolean   writeStringAsFile  (  File   file  ,  String   string  )  { 
FileOutputStream   stream  =  null  ; 
OutputStreamWriter   writer  =  null  ; 
BufferedWriter   buffered  =  null  ; 
try  { 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
stream  =  new   FileOutputStream  (  file  )  ; 
writer  =  new   OutputStreamWriter  (  stream  ,  DEFAULT_ENCODING  )  ; 
buffered  =  new   BufferedWriter  (  writer  )  ; 
buffered  .  write  (  string  )  ; 
}  catch  (  IOException   e  )  { 
return   false  ; 
}  finally  { 
Utility  .  close  (  buffered  )  ; 
Utility  .  close  (  writer  )  ; 
Utility  .  close  (  stream  )  ; 
} 
return   true  ; 
} 

public   static   void   writeStringAsFile  (  TreeLogger   logger  ,  File   file  ,  String   string  )  throws   UnableToCompleteException  { 
FileOutputStream   stream  =  null  ; 
OutputStreamWriter   writer  =  null  ; 
BufferedWriter   buffered  =  null  ; 
try  { 
stream  =  new   FileOutputStream  (  file  )  ; 
writer  =  new   OutputStreamWriter  (  stream  ,  DEFAULT_ENCODING  )  ; 
buffered  =  new   BufferedWriter  (  writer  )  ; 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
buffered  .  write  (  string  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  log  (  TreeLogger  .  ERROR  ,  "Unable to write file: "  +  file  .  getAbsolutePath  (  )  ,  e  )  ; 
throw   new   UnableToCompleteException  (  )  ; 
}  finally  { 
Utility  .  close  (  buffered  )  ; 
Utility  .  close  (  writer  )  ; 
Utility  .  close  (  stream  )  ; 
} 
} 






public   static   void   writeUtf8  (  StringBuilder   builder  ,  OutputStream   out  )  throws   IOException  { 
int   buflen  =  1024  ; 
char  [  ]  inBuf  =  new   char  [  buflen  ]  ; 
byte  [  ]  outBuf  =  new   byte  [  4  *  buflen  ]  ; 
int   length  =  builder  .  length  (  )  ; 
int   start  =  0  ; 
while  (  start  <  length  )  { 
int   end  =  Math  .  min  (  start  +  buflen  ,  length  )  ; 
builder  .  getChars  (  start  ,  end  ,  inBuf  ,  0  )  ; 
int   index  =  0  ; 
int   len  =  end  -  start  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
int   c  =  inBuf  [  i  ]  &  0xffff  ; 
if  (  c  <  0x80  )  { 
outBuf  [  index  ++  ]  =  (  byte  )  c  ; 
}  else   if  (  c  <  0x800  )  { 
int   y  =  c  >  >  8  ; 
int   x  =  c  &  0xff  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0xc0  |  (  y  <<  2  )  |  (  x  >  >  6  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  x  &  0x3f  )  )  ; 
}  else   if  (  c  <  0xD800  ||  c  >  0xDFFF  )  { 
int   y  =  (  c  >  >  8  )  &  0xff  ; 
int   x  =  c  &  0xff  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0xe0  |  (  y  >  >  4  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  (  y  <<  2  )  &  0x3c  )  |  (  x  >  >  6  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  x  &  0x3f  )  )  ; 
}  else  { 
if  (  i  +  1  <  len  )  { 
int   hi  =  c  &  0x3ff  ; 
int   lo  =  inBuf  [  i  +  1  ]  &  0x3ff  ; 
int   full  =  0x10000  +  (  (  hi  <<  10  )  |  lo  )  ; 
int   z  =  (  full  >  >  16  )  &  0xff  ; 
int   y  =  (  full  >  >  8  )  &  0xff  ; 
int   x  =  full  &  0xff  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0xf0  |  (  z  >  >  5  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  (  z  <<  4  )  &  0x30  )  |  (  y  >  >  4  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  (  y  <<  2  )  &  0x3c  )  |  (  x  >  >  6  )  )  ; 
outBuf  [  index  ++  ]  =  (  byte  )  (  0x80  |  (  x  &  0x3f  )  )  ; 
i  ++  ; 
} 
} 
} 
out  .  write  (  outBuf  ,  0  ,  index  )  ; 
start  =  end  ; 
} 
} 









private   static   byte  [  ]  readBytesFromInputStream  (  InputStream   input  ,  int   byteLength  )  { 
try  { 
byte  [  ]  bytes  =  new   byte  [  byteLength  ]  ; 
int   byteOffset  =  0  ; 
while  (  byteOffset  <  byteLength  )  { 
int   bytesReadCount  =  input  .  read  (  bytes  ,  byteOffset  ,  byteLength  -  byteOffset  )  ; 
if  (  bytesReadCount  ==  -  1  )  { 
return   null  ; 
} 
byteOffset  +=  bytesReadCount  ; 
} 
return   bytes  ; 
}  catch  (  IOException   e  )  { 
} 
return   null  ; 
} 










private   static   String   toString  (  byte  [  ]  bytes  ,  String   charsetName  )  { 
try  { 
return   new   String  (  bytes  ,  charsetName  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
} 
return   null  ; 
} 

private   static   void   writeAttribute  (  PrintWriter   w  ,  Attr   attr  ,  int   depth  )  throws   IOException  { 
w  .  write  (  attr  .  getName  (  )  )  ; 
w  .  write  (  '='  )  ; 
Node   c  =  attr  .  getFirstChild  (  )  ; 
while  (  c  !=  null  )  { 
w  .  write  (  '"'  )  ; 
writeNode  (  w  ,  c  ,  depth  )  ; 
w  .  write  (  '"'  )  ; 
c  =  c  .  getNextSibling  (  )  ; 
} 
} 

private   static   void   writeDocument  (  PrintWriter   w  ,  Document   d  )  throws   IOException  { 
w  .  println  (  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  )  ; 
Node   c  =  d  .  getFirstChild  (  )  ; 
while  (  c  !=  null  )  { 
writeNode  (  w  ,  c  ,  0  )  ; 
c  =  c  .  getNextSibling  (  )  ; 
} 
} 

private   static   void   writeElement  (  PrintWriter   w  ,  Element   el  ,  int   depth  )  throws   IOException  { 
String   tagName  =  el  .  getTagName  (  )  ; 
writeIndent  (  w  ,  depth  )  ; 
w  .  write  (  '<'  )  ; 
w  .  write  (  tagName  )  ; 
NamedNodeMap   attrs  =  el  .  getAttributes  (  )  ; 
for  (  int   i  =  0  ,  n  =  attrs  .  getLength  (  )  ;  i  <  n  ;  ++  i  )  { 
w  .  write  (  ' '  )  ; 
writeNode  (  w  ,  attrs  .  item  (  i  )  ,  depth  )  ; 
} 
Node   c  =  el  .  getFirstChild  (  )  ; 
if  (  c  !=  null  )  { 
w  .  println  (  '>'  )  ; 
while  (  c  !=  null  )  { 
writeNode  (  w  ,  c  ,  depth  +  1  )  ; 
w  .  println  (  )  ; 
c  =  c  .  getNextSibling  (  )  ; 
} 
writeIndent  (  w  ,  depth  )  ; 
w  .  write  (  "</"  )  ; 
w  .  write  (  tagName  )  ; 
w  .  print  (  '>'  )  ; 
}  else  { 
w  .  print  (  "/>"  )  ; 
} 
} 

private   static   void   writeIndent  (  PrintWriter   w  ,  int   depth  )  { 
for  (  int   i  =  0  ;  i  <  depth  ;  ++  i  )  { 
w  .  write  (  '\t'  )  ; 
} 
} 

private   static   void   writeNode  (  PrintWriter   w  ,  Node   node  ,  int   depth  )  throws   IOException  { 
short   nodeType  =  node  .  getNodeType  (  )  ; 
switch  (  nodeType  )  { 
case   Node  .  ELEMENT_NODE  : 
writeElement  (  w  ,  (  Element  )  node  ,  depth  )  ; 
break  ; 
case   Node  .  ATTRIBUTE_NODE  : 
writeAttribute  (  w  ,  (  Attr  )  node  ,  depth  )  ; 
break  ; 
case   Node  .  DOCUMENT_NODE  : 
writeDocument  (  w  ,  (  Document  )  node  )  ; 
break  ; 
case   Node  .  TEXT_NODE  : 
writeText  (  w  ,  (  Text  )  node  )  ; 
break  ; 
case   Node  .  COMMENT_NODE  : 
case   Node  .  CDATA_SECTION_NODE  : 
case   Node  .  ENTITY_REFERENCE_NODE  : 
case   Node  .  ENTITY_NODE  : 
case   Node  .  PROCESSING_INSTRUCTION_NODE  : 
default  : 
throw   new   RuntimeException  (  "Unsupported DOM node type: "  +  nodeType  )  ; 
} 
} 

private   static   void   writeText  (  PrintWriter   w  ,  Text   text  )  throws   DOMException  { 
String   nodeValue  =  text  .  getNodeValue  (  )  ; 
String   escaped  =  escapeXml  (  nodeValue  )  ; 
w  .  write  (  escaped  )  ; 
} 




private   Util  (  )  { 
} 
} 

