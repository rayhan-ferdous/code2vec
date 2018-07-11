package   org  .  ignition  .  blojsom  .  util  ; 

import   org  .  ignition  .  blojsom  .  BlojsomException  ; 
import   org  .  ignition  .  blojsom  .  blog  .  FileBackedBlogEntry  ; 
import   javax  .  servlet  .  ServletConfig  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  *  ; 







public   class   BlojsomUtils   implements   BlojsomConstants  { 




private   static   final   FileFilter   DIRECTORY_FILTER  =  new   FileFilter  (  )  { 

public   boolean   accept  (  File   pathname  )  { 
return  (  pathname  .  isDirectory  (  )  )  ; 
} 
}  ; 






private   static   final   ThreadLocal   RFC_822_DATE_FORMAT_OBJECT  =  new   ThreadLocal  (  )  { 

protected   Object   initialValue  (  )  { 
return   new   SimpleDateFormat  (  RFC_822_DATE_FORMAT  )  ; 
} 
}  ; 






private   static   final   ThreadLocal   ISO_8601_DATE_FORMAT_OBJECT  =  new   ThreadLocal  (  )  { 

protected   Object   initialValue  (  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  ISO_8601_DATE_FORMAT  )  ; 
sdf  .  getTimeZone  (  )  .  setID  (  "+00:00"  )  ; 
return   sdf  ; 
} 
}  ; 






private   static   final   ThreadLocal   UTC_DATE_FORMAT_OBJECT  =  new   ThreadLocal  (  )  { 

protected   Object   initialValue  (  )  { 
return   new   SimpleDateFormat  (  UTC_DATE_FORMAT  )  ; 
} 
}  ; 

private   BlojsomUtils  (  )  { 
} 






public   static   FileFilter   getDirectoryFilter  (  )  { 
return   DIRECTORY_FILTER  ; 
} 








public   static   FileFilter   getDirectoryFilter  (  final   String  [  ]  excludedDirectories  )  { 
if  (  excludedDirectories  ==  null  )  { 
return   DIRECTORY_FILTER  ; 
} 
return   new   FileFilter  (  )  { 

public   boolean   accept  (  File   pathname  )  { 
if  (  !  pathname  .  isDirectory  (  )  )  { 
return   false  ; 
}  else  { 
for  (  int   i  =  0  ;  i  <  excludedDirectories  .  length  ;  i  ++  )  { 
String   excludedDirectory  =  excludedDirectories  [  i  ]  ; 
if  (  pathname  .  toString  (  )  .  matches  (  excludedDirectory  )  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 
}  ; 
} 







public   static   String   getRFC822Date  (  Date   date  )  { 
return  (  (  SimpleDateFormat  )  RFC_822_DATE_FORMAT_OBJECT  .  get  (  )  )  .  format  (  date  )  ; 
} 








public   static   String   getFormattedDate  (  Date   date  ,  String   format  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  format  )  ; 
return   sdf  .  format  (  date  )  ; 
} 








public   static   String   getISO8601Date  (  Date   date  )  { 
return  (  (  SimpleDateFormat  )  ISO_8601_DATE_FORMAT_OBJECT  .  get  (  )  )  .  format  (  date  )  .  replaceAll  (  "GMT"  ,  ""  )  ; 
} 








public   static   String   getUTCDate  (  Date   date  )  { 
return  (  (  SimpleDateFormat  )  UTC_DATE_FORMAT_OBJECT  .  get  (  )  )  .  format  (  date  )  ; 
} 







public   static   FileFilter   getRegularExpressionFilter  (  final   String  [  ]  expressions  )  { 
return   new   FileFilter  (  )  { 

private   Date   today  =  new   Date  (  )  ; 

public   boolean   accept  (  File   pathname  )  { 
for  (  int   i  =  0  ;  i  <  expressions  .  length  ;  i  ++  )  { 
if  (  pathname  .  isDirectory  (  )  )  { 
return   false  ; 
} 
String   expression  =  expressions  [  i  ]  ; 
if  (  pathname  .  getName  (  )  .  matches  (  expression  )  )  { 
return   pathname  .  lastModified  (  )  <=  today  .  getTime  (  )  ; 
} 
} 
return   false  ; 
} 
}  ; 
} 







public   static   FileFilter   getExtensionsFilter  (  final   String  [  ]  extensions  )  { 
return   new   FileFilter  (  )  { 

public   boolean   accept  (  File   pathname  )  { 
for  (  int   i  =  0  ;  i  <  extensions  .  length  ;  i  ++  )  { 
if  (  pathname  .  isDirectory  (  )  )  { 
return   false  ; 
} 
String   extension  =  extensions  [  i  ]  ; 
if  (  pathname  .  getName  (  )  .  endsWith  (  extension  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 
}  ; 
} 







public   static   FileFilter   getExtensionFilter  (  final   String   extension  )  { 
return   getExtensionsFilter  (  new   String  [  ]  {  extension  }  )  ; 
} 







public   static   String  [  ]  parseCommaList  (  String   commaList  )  { 
return   parseDelimitedList  (  commaList  ,  ", "  )  ; 
} 








public   static   String  [  ]  parseDelimitedList  (  String   commaList  ,  String   delimiter  )  { 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  commaList  ,  delimiter  )  ; 
List   list  =  new   ArrayList  (  )  ; 
while  (  tokenizer  .  hasMoreTokens  (  )  )  { 
list  .  add  (  tokenizer  .  nextToken  (  )  )  ; 
} 
if  (  list  .  size  (  )  ==  0  )  { 
return   new   String  [  ]  {  }  ; 
} 
return  (  String  [  ]  )  list  .  toArray  (  new   String  [  list  .  size  (  )  ]  )  ; 
} 







public   static   String   convertRequestParams  (  HttpServletRequest   request  )  { 
Enumeration   paramNames  =  request  .  getParameterNames  (  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
while  (  paramNames  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  paramNames  .  nextElement  (  )  ; 
String   value  =  request  .  getParameter  (  name  )  ; 
try  { 
buffer  .  append  (  URLEncoder  .  encode  (  name  ,  "UTF-8"  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  value  ,  "UTF-8"  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
} 
if  (  paramNames  .  hasMoreElements  (  )  )  { 
buffer  .  append  (  "&"  )  ; 
} 
} 
return   buffer  .  toString  (  )  ; 
} 








public   static   String   getBlogCategory  (  String   blogHome  ,  String   requestedCategory  )  { 
requestedCategory  =  requestedCategory  .  replace  (  '\\'  ,  '/'  )  ; 
int   indexOfBlogHome  =  requestedCategory  .  indexOf  (  blogHome  )  ; 
if  (  indexOfBlogHome  ==  -  1  )  { 
return  ""  ; 
} 
indexOfBlogHome  +=  blogHome  .  length  (  )  ; 
String   returnCategory  =  requestedCategory  .  substring  (  indexOfBlogHome  )  ; 
returnCategory  =  removeInitialSlash  (  returnCategory  )  ; 
return  '/'  +  returnCategory  ; 
} 








public   static   String   getBlogSiteURL  (  String   blogURL  ,  String   servletPath  )  { 
if  (  servletPath  ==  null  ||  ""  .  equals  (  servletPath  )  )  { 
return   blogURL  ; 
} 
int   servletPathIndex  =  blogURL  .  indexOf  (  servletPath  ,  7  )  ; 
if  (  servletPathIndex  ==  -  1  )  { 
return   blogURL  ; 
} 
return   blogURL  .  substring  (  0  ,  servletPathIndex  )  ; 
} 







public   static   String   escapeString  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
String   unescaped  =  replace  (  input  ,  "&"  ,  "&amp;"  )  ; 
unescaped  =  replace  (  unescaped  ,  "<"  ,  "&lt;"  )  ; 
unescaped  =  replace  (  unescaped  ,  ">"  ,  "&gt;"  )  ; 
return   unescaped  ; 
} 







public   static   String   escapeMetaAndLink  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
String   cleanedInput  =  input  .  replaceAll  (  "<[mM][eE][tT][aA]"  ,  "&lt;meta"  )  ; 
cleanedInput  =  cleanedInput  .  replaceAll  (  "<[lL][iI][nN][kK]"  ,  "&lt;link"  )  ; 
return   cleanedInput  ; 
} 









public   static   String   replace  (  String   str  ,  String   pattern  ,  String   replace  )  { 
if  (  str  ==  null  ||  ""  .  equals  (  str  )  )  { 
return   str  ; 
} 
if  (  replace  ==  null  )  { 
return   str  ; 
} 
if  (  ""  .  equals  (  pattern  )  )  { 
return   str  ; 
} 
int   s  =  0  ; 
int   e  =  0  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
while  (  (  e  =  str  .  indexOf  (  pattern  ,  s  )  )  >=  0  )  { 
result  .  append  (  str  .  substring  (  s  ,  e  )  )  ; 
result  .  append  (  replace  )  ; 
s  =  e  +  pattern  .  length  (  )  ; 
} 
result  .  append  (  str  .  substring  (  s  )  )  ; 
return   result  .  toString  (  )  ; 
} 








public   static   String   getFileExtension  (  String   filename  )  { 
int   dotIndex  =  filename  .  lastIndexOf  (  "."  )  ; 
if  (  dotIndex  ==  -  1  )  { 
return   null  ; 
}  else  { 
return   filename  .  substring  (  dotIndex  +  1  )  ; 
} 
} 








public   static   String   getFilename  (  String   filename  )  { 
int   dotIndex  =  filename  .  lastIndexOf  (  "."  )  ; 
if  (  dotIndex  ==  -  1  )  { 
return   filename  ; 
}  else  { 
return   filename  .  substring  (  0  ,  dotIndex  )  ; 
} 
} 







public   static   String   getDateKey  (  Date   date  )  { 
StringBuffer   value  =  new   StringBuffer  (  )  ; 
Calendar   calendar  =  Calendar  .  getInstance  (  )  ; 
long   l  =  0  ; 
calendar  .  setTime  (  date  )  ; 
value  .  append  (  calendar  .  get  (  Calendar  .  YEAR  )  )  ; 
l  =  calendar  .  get  (  Calendar  .  MONTH  )  +  1  ; 
if  (  l  <  10  )  { 
value  .  append  (  "0"  )  ; 
} 
value  .  append  (  l  )  ; 
l  =  calendar  .  get  (  Calendar  .  DAY_OF_MONTH  )  ; 
if  (  l  <  10  )  { 
value  .  append  (  "0"  )  ; 
} 
value  .  append  (  l  )  ; 
return   value  .  toString  (  )  ; 
} 







public   static   String   removeInitialSlash  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
if  (  !  input  .  startsWith  (  "/"  )  )  { 
return   input  ; 
}  else  { 
return   input  .  substring  (  1  )  ; 
} 
} 







public   static   String   getFirstLine  (  String   input  ,  int   length  )  { 
String   result  ; 
String   lineSeparator  =  System  .  getProperty  (  "line.separator"  )  ; 
int   titleIndex  =  input  .  indexOf  (  lineSeparator  )  ; 
if  (  titleIndex  ==  -  1  )  { 
result  =  input  .  substring  (  0  ,  length  )  +  "..."  ; 
}  else  { 
result  =  input  .  substring  (  0  ,  titleIndex  )  ; 
} 
return   result  ; 
} 








public   static   final   String   getTemplateForPage  (  String   flavorTemplate  ,  String   page  )  { 
int   dotIndex  =  flavorTemplate  .  lastIndexOf  (  "."  )  ; 
if  (  dotIndex  ==  -  1  )  { 
return   flavorTemplate  +  '-'  +  page  ; 
}  else  { 
StringBuffer   newTemplate  =  new   StringBuffer  (  )  ; 
if  (  page  .  startsWith  (  "/"  )  )  { 
newTemplate  .  append  (  removeInitialSlash  (  page  )  )  ; 
}  else  { 
newTemplate  .  append  (  flavorTemplate  .  substring  (  0  ,  dotIndex  )  )  ; 
newTemplate  .  append  (  "-"  )  ; 
newTemplate  .  append  (  page  )  ; 
} 
newTemplate  .  append  (  "."  )  ; 
newTemplate  .  append  (  flavorTemplate  .  substring  (  dotIndex  +  1  ,  flavorTemplate  .  length  (  )  )  )  ; 
return   newTemplate  .  toString  (  )  ; 
} 
} 









public   static   final   String   getRequestValue  (  String   key  ,  HttpServletRequest   httpServletRequest  )  { 
if  (  httpServletRequest  .  getParameter  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getParameter  (  key  )  ; 
}  else   if  (  httpServletRequest  .  getAttribute  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getAttribute  (  key  )  .  toString  (  )  ; 
} 
return   null  ; 
} 








public   static   final   String   getFilenameForPermalink  (  String   permalink  ,  String  [  ]  blogEntryExtensions  )  { 
if  (  permalink  ==  null  )  { 
return   null  ; 
} 
boolean   matchesExtension  =  false  ; 
for  (  int   i  =  0  ;  i  <  blogEntryExtensions  .  length  ;  i  ++  )  { 
String   blogEntryExtension  =  blogEntryExtensions  [  i  ]  ; 
if  (  permalink  .  matches  (  blogEntryExtension  )  )  { 
matchesExtension  =  true  ; 
break  ; 
} 
} 
if  (  !  matchesExtension  )  { 
return   null  ; 
} 
int   indexOfSlash  =  permalink  .  lastIndexOf  (  "/"  )  ; 
if  (  indexOfSlash  ==  -  1  )  { 
return   permalink  ; 
}  else  { 
String   sanitizedPermalink  =  permalink  .  substring  (  indexOfSlash  +  1  ,  permalink  .  length  (  )  )  ; 
if  (  sanitizedPermalink  .  startsWith  (  ".."  )  )  { 
sanitizedPermalink  =  sanitizedPermalink  .  substring  (  2  ,  sanitizedPermalink  .  length  (  )  )  ; 
}  else   if  (  sanitizedPermalink  .  startsWith  (  "."  )  )  { 
sanitizedPermalink  =  sanitizedPermalink  .  substring  (  1  ,  sanitizedPermalink  .  length  (  )  )  ; 
} 
return   sanitizedPermalink  ; 
} 
} 







public   static   final   String   urlEncode  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
try  { 
return   URLEncoder  .  encode  (  input  ,  UTF8  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   input  ; 
} 
} 







public   static   final   String   urlDecode  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
try  { 
return   URLDecoder  .  decode  (  input  ,  UTF8  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   null  ; 
} 
} 










public   static   String   getCalendarNavigationUrl  (  String   prefix  ,  int   month  ,  int   day  ,  int   year  )  { 
StringBuffer   dateurl  =  new   StringBuffer  (  prefix  )  ; 
if  (  month  !=  -  1  )  { 
dateurl  .  append  (  "?month="  )  .  append  (  month  )  ; 
} 
if  (  day  !=  -  1  )  { 
dateurl  .  append  (  "&amp;day="  )  .  append  (  day  )  ; 
} 
if  (  year  !=  -  1  )  { 
dateurl  .  append  (  "&amp;year="  )  .  append  (  year  )  ; 
} 
return   dateurl  .  toString  (  )  ; 
} 






public   static   final   Comparator   FILE_TIME_COMPARATOR  =  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
File   f1  ; 
File   f2  ; 
if  (  (  o1   instanceof   FileBackedBlogEntry  )  &&  (  o2   instanceof   FileBackedBlogEntry  )  )  { 
f1  =  (  (  FileBackedBlogEntry  )  o1  )  .  getSource  (  )  ; 
f2  =  (  (  FileBackedBlogEntry  )  o2  )  .  getSource  (  )  ; 
}  else  { 
f1  =  (  File  )  o1  ; 
f2  =  (  File  )  o2  ; 
} 
if  (  f1  .  lastModified  (  )  >  f2  .  lastModified  (  )  )  { 
return  -  1  ; 
}  else   if  (  f1  .  lastModified  (  )  <  f2  .  lastModified  (  )  )  { 
return   1  ; 
}  else  { 
return   f1  .  getName  (  )  .  compareTo  (  f2  .  getName  (  )  )  ; 
} 
} 
}  ; 






public   static   final   Comparator   FILE_TIME_ASCENDING_COMPARATOR  =  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
File   f1  ; 
File   f2  ; 
if  (  (  o1   instanceof   FileBackedBlogEntry  )  &&  (  o2   instanceof   FileBackedBlogEntry  )  )  { 
f1  =  (  (  FileBackedBlogEntry  )  o1  )  .  getSource  (  )  ; 
f2  =  (  (  FileBackedBlogEntry  )  o2  )  .  getSource  (  )  ; 
}  else  { 
f1  =  (  File  )  o1  ; 
f2  =  (  File  )  o2  ; 
} 
if  (  f1  .  lastModified  (  )  >  f2  .  lastModified  (  )  )  { 
return   1  ; 
}  else   if  (  f1  .  lastModified  (  )  <  f2  .  lastModified  (  )  )  { 
return  -  1  ; 
}  else  { 
return   f1  .  getName  (  )  .  compareTo  (  f2  .  getName  (  )  )  ; 
} 
} 
}  ; 




public   static   final   Comparator   FILE_NAME_COMPARATOR  =  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
String   s1  =  (  String  )  o1  ; 
String   s2  =  (  String  )  o2  ; 
return   s1  .  compareTo  (  s2  )  ; 
} 
}  ; 

static   final   byte  [  ]  HEX_DIGITS  =  {  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  }  ; 







public   static   String   digestString  (  String   data  )  { 
String   result  =  null  ; 
if  (  data  !=  null  )  { 
try  { 
MessageDigest   _md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
_md  .  update  (  data  .  getBytes  (  )  )  ; 
byte  [  ]  _digest  =  _md  .  digest  (  )  ; 
String   _ds  =  toHexString  (  _digest  ,  0  ,  _digest  .  length  )  ; 
result  =  _ds  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
result  =  null  ; 
} 
} 
return   result  ; 
} 









private   static   void   toHexValue  (  byte  [  ]  buf  ,  int   offset  ,  int   length  ,  int   value  )  { 
do  { 
buf  [  offset  +  --  length  ]  =  HEX_DIGITS  [  value  &  0x0f  ]  ; 
value  >  >  >=  4  ; 
}  while  (  value  !=  0  &&  length  >  0  )  ; 
while  (  --  length  >=  0  )  { 
buf  [  offset  +  length  ]  =  HEX_DIGITS  [  0  ]  ; 
} 
} 









private   static   String   toHexString  (  byte  [  ]  buf  ,  int   offset  ,  int   length  )  { 
byte  [  ]  buf1  =  new   byte  [  length  <<  1  ]  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
toHexValue  (  buf1  ,  i  <<  1  ,  2  ,  buf  [  i  +  offset  ]  )  ; 
} 
return   new   String  (  buf1  )  ; 
} 













public   static   Properties   loadProperties  (  ServletConfig   servletConfig  ,  String   configurationIP  ,  boolean   required  )  throws   BlojsomException  { 
String   configuration  =  servletConfig  .  getInitParameter  (  configurationIP  )  ; 
Properties   properties  =  new   Properties  (  )  ; 
if  (  configuration  ==  null  ||  ""  .  equals  (  configuration  )  )  { 
if  (  required  )  { 
throw   new   BlojsomException  (  "No value given for: "  +  configurationIP  +  " configuration parameter"  )  ; 
}  else  { 
return   properties  ; 
} 
} 
InputStream   is  =  servletConfig  .  getServletContext  (  )  .  getResourceAsStream  (  configuration  )  ; 
try  { 
properties  .  load  (  is  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   BlojsomException  (  e  )  ; 
}  finally  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   BlojsomException  (  e  )  ; 
} 
} 
return   properties  ; 
} 







public   static   String   normalize  (  String   path  )  { 
if  (  path  ==  null  )  { 
return   null  ; 
} 
String   value  =  path  ; 
value  =  value  .  replaceAll  (  "\\.*"  ,  ""  )  ; 
value  =  value  .  replaceAll  (  "/{2,}"  ,  ""  )  ; 
return   value  ; 
} 








public   static   String   nullToBlank  (  String   input  )  { 
return  (  input  ==  null  )  ?  ""  :  input  ; 
} 
} 

