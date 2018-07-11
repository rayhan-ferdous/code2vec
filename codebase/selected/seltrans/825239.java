package   org  .  blojsom  .  util  ; 

import   org  .  blojsom  .  blog  .  Blog  ; 
import   org  .  blojsom  .  blog  .  Response  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  Collator  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  regex  .  Matcher  ; 








public   class   BlojsomUtils   implements   BlojsomConstants  { 

private   static   final   int   REGEX_OPTIONS  =  Pattern  .  DOTALL  |  Pattern  .  CASE_INSENSITIVE  ; 

private   static   final   Pattern   STRIP_HTML_PATTERN  =  Pattern  .  compile  (  "^[^<>]*>|<.*?>|<[^<>]*$"  ,  REGEX_OPTIONS  )  ; 




private   BlojsomUtils  (  )  { 
} 




private   static   final   FileFilter   DIRECTORY_FILTER  =  new   FileFilter  (  )  { 









public   boolean   accept  (  File   pathname  )  { 
return  (  pathname  .  isDirectory  (  )  )  ; 
} 
}  ; 




private   static   final   FileFilter   FILE_FILTER  =  new   FileFilter  (  )  { 









public   boolean   accept  (  File   pathname  )  { 
return  (  !  pathname  .  isDirectory  (  )  )  ; 
} 
}  ; 






private   static   final   ThreadLocal   RFC_822_DATE_FORMAT_OBJECT  =  new   ThreadLocal  (  )  { 

protected   Object   initialValue  (  )  { 
return   new   SimpleDateFormat  (  RFC_822_DATE_FORMAT  ,  Locale  .  US  )  ; 
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









public   static   String   getFormattedDate  (  Date   date  ,  String   format  ,  Locale   locale  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  format  ,  locale  )  ; 
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
if  (  pathname  .  isDirectory  (  )  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  expressions  .  length  ;  i  ++  )  { 
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
if  (  pathname  .  isDirectory  (  )  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  extensions  .  length  ;  i  ++  )  { 
String   extension  =  extensions  [  i  ]  ; 
if  (  pathname  .  getName  (  )  .  endsWith  (  extension  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 
}  ; 
} 








public   static   FileFilter   getExtensionsFilter  (  final   String  [  ]  extensions  ,  final   String  [  ]  excludedDirectories  ,  final   boolean   returnDirectories  )  { 
return   new   FileFilter  (  )  { 

public   boolean   accept  (  File   pathname  )  { 
if  (  pathname  .  isDirectory  (  )  &&  returnDirectories  )  { 
String   path  =  pathname  .  toString  (  )  ; 
for  (  int   i  =  0  ;  i  <  excludedDirectories  .  length  ;  i  ++  )  { 
String   excludedDirectory  =  excludedDirectories  [  i  ]  ; 
if  (  path  .  matches  (  excludedDirectory  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 
for  (  int   i  =  0  ;  i  <  extensions  .  length  ;  i  ++  )  { 
String   extension  =  extensions  [  i  ]  ; 
if  (  pathname  .  getName  (  )  .  matches  (  extension  )  )  { 
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







public   static   String  [  ]  parseOnlyCommaList  (  String   commaList  )  { 
return   parseOnlyCommaList  (  commaList  ,  false  )  ; 
} 








public   static   String  [  ]  parseOnlyCommaList  (  String   commaList  ,  boolean   trim  )  { 
return   parseDelimitedList  (  commaList  ,  ","  ,  trim  )  ; 
} 







public   static   String  [  ]  parseLastComma  (  String   value  )  { 
if  (  checkNullOrBlank  (  value  )  )  { 
return   new   String  [  ]  {  value  }  ; 
} 
int   lastCommaIndex  =  value  .  lastIndexOf  (  ","  )  ; 
if  (  lastCommaIndex  ==  -  1  )  { 
return   new   String  [  ]  {  value  }  ; 
}  else  { 
return   new   String  [  ]  {  value  .  substring  (  0  ,  lastCommaIndex  )  ,  value  .  substring  (  lastCommaIndex  +  1  )  }  ; 
} 
} 








public   static   String  [  ]  parseDelimitedList  (  String   delimitedList  ,  String   delimiter  )  { 
return   parseDelimitedList  (  delimitedList  ,  delimiter  ,  false  )  ; 
} 









public   static   String  [  ]  parseDelimitedList  (  String   delimitedList  ,  String   delimiter  ,  boolean   trim  )  { 
if  (  delimitedList  ==  null  )  { 
return   null  ; 
} 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  delimitedList  ,  delimiter  )  ; 
ArrayList   list  =  new   ArrayList  (  )  ; 
while  (  tokenizer  .  hasMoreTokens  (  )  )  { 
if  (  trim  )  { 
list  .  add  (  tokenizer  .  nextToken  (  )  .  trim  (  )  )  ; 
}  else  { 
list  .  add  (  tokenizer  .  nextToken  (  )  )  ; 
} 
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
buffer  .  append  (  URLEncoder  .  encode  (  name  ,  UTF8  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  value  ,  UTF8  )  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
} 
if  (  paramNames  .  hasMoreElements  (  )  )  { 
buffer  .  append  (  "&"  )  ; 
} 
} 
return   buffer  .  toString  (  )  ; 
} 








public   static   String   convertRequestParams  (  HttpServletRequest   request  ,  Map   ignoreParams  )  { 
Enumeration   paramNames  =  request  .  getParameterNames  (  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
while  (  paramNames  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  paramNames  .  nextElement  (  )  ; 
String   value  =  request  .  getParameter  (  name  )  ; 
try  { 
if  (  !  ignoreParams  .  containsKey  (  name  )  )  { 
buffer  .  append  (  URLEncoder  .  encode  (  name  ,  UTF8  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  value  ,  UTF8  )  )  .  append  (  "&"  )  ; 
} 
}  catch  (  UnsupportedEncodingException   e  )  { 
} 
} 
return   buffer  .  toString  (  )  ; 
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
unescaped  =  replace  (  unescaped  ,  "\""  ,  "&quot;"  )  ; 
unescaped  =  replace  (  unescaped  ,  "'"  ,  "&#39;"  )  ; 
return   unescaped  ; 
} 







public   static   String   escapeStringSimple  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
String   unescaped  =  replace  (  input  ,  "&"  ,  "&amp;"  )  ; 
unescaped  =  replace  (  unescaped  ,  "<"  ,  "&lt;"  )  ; 
unescaped  =  replace  (  unescaped  ,  ">"  ,  "&gt;"  )  ; 
return   unescaped  ; 
} 







public   static   String   escapeBrackets  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
String   unescaped  =  replace  (  input  ,  "<"  ,  "&lt;"  )  ; 
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
int   e  ; 
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
if  (  filename  ==  null  )  { 
return   null  ; 
} 
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










public   static   String   getFilenameFromPath  (  String   filenameWithPath  )  { 
String   fileName  =  new   File  (  filenameWithPath  )  .  getName  (  )  ; 
int   colonIndex  =  fileName  .  indexOf  (  ":"  )  ; 
if  (  colonIndex  ==  -  1  )  { 
colonIndex  =  fileName  .  indexOf  (  "\\\\"  )  ; 
} 
int   backslashIndex  =  fileName  .  lastIndexOf  (  "\\"  )  ; 
if  (  colonIndex  >  -  1  &&  backslashIndex  >  -  1  )  { 
fileName  =  fileName  .  substring  (  backslashIndex  +  1  )  ; 
} 
return   fileName  ; 
} 







public   static   String   getDateKey  (  Date   date  )  { 
StringBuffer   value  =  new   StringBuffer  (  )  ; 
Calendar   calendar  =  Calendar  .  getInstance  (  )  ; 
long   l  ; 
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







public   static   String   removeTrailingSlash  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
if  (  !  input  .  endsWith  (  "/"  )  )  { 
return   input  ; 
}  else  { 
return   input  .  substring  (  0  ,  input  .  length  (  )  -  1  )  ; 
} 
} 







public   static   String   removeSlashes  (  String   input  )  { 
input  =  removeInitialSlash  (  input  )  ; 
input  =  removeTrailingSlash  (  input  )  ; 
return   input  ; 
} 








public   static   String   getFirstLine  (  String   input  ,  int   length  )  { 
String   result  ; 
String   lineSeparator  =  LINE_SEPARATOR  ; 
int   titleIndex  =  input  .  indexOf  (  lineSeparator  )  ; 
if  (  titleIndex  ==  -  1  )  { 
result  =  input  .  substring  (  0  ,  length  )  +  "..."  ; 
}  else  { 
result  =  input  .  substring  (  0  ,  titleIndex  )  ; 
} 
return   result  ; 
} 








public   static   String   getTemplateForPage  (  String   flavorTemplate  ,  String   page  )  { 
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









public   static   String   getRequestValue  (  String   key  ,  HttpServletRequest   httpServletRequest  )  { 
return   getRequestValue  (  key  ,  httpServletRequest  ,  false  )  ; 
} 










public   static   String   getRequestValue  (  String   key  ,  HttpServletRequest   httpServletRequest  ,  boolean   preferAttributes  )  { 
if  (  !  preferAttributes  )  { 
if  (  httpServletRequest  .  getParameter  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getParameter  (  key  )  ; 
}  else   if  (  httpServletRequest  .  getAttribute  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getAttribute  (  key  )  .  toString  (  )  ; 
} 
}  else  { 
if  (  httpServletRequest  .  getAttribute  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getAttribute  (  key  )  .  toString  (  )  ; 
}  else   if  (  httpServletRequest  .  getParameter  (  key  )  !=  null  )  { 
return   httpServletRequest  .  getParameter  (  key  )  ; 
} 
} 
return   null  ; 
} 








public   static   String  [  ]  getRequestValues  (  String   key  ,  HttpServletRequest   httpServletRequest  )  { 
String  [  ]  values  =  httpServletRequest  .  getParameterValues  (  key  )  ; 
if  (  values  ==  null  )  { 
values  =  new   String  [  0  ]  ; 
} 
return   values  ; 
} 








public   static   String   getFilenameForPermalink  (  String   permalink  ,  String  [  ]  blogEntryExtensions  )  { 
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
indexOfSlash  =  permalink  .  lastIndexOf  (  "\\"  )  ; 
} 
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








public   static   String   urlEncode  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
try  { 
return   URLEncoder  .  encode  (  input  ,  UTF8  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   input  ; 
} 
} 








public   static   String   urlEncodeForLink  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
try  { 
String   result  =  URLEncoder  .  encode  (  input  ,  UTF8  )  ; 
result  =  replace  (  result  ,  "%2F"  ,  "/"  )  ; 
result  =  replace  (  result  ,  "%20"  ,  "+"  )  ; 
return   result  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   input  ; 
} 
} 







public   static   String   urlDecode  (  String   input  )  { 
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
if  (  year  !=  -  1  )  { 
dateurl  .  append  (  year  )  .  append  (  "/"  )  ; 
} 
if  (  month  !=  -  1  )  { 
if  (  month  <  10  )  { 
dateurl  .  append  (  "0"  )  ; 
} 
dateurl  .  append  (  month  )  .  append  (  "/"  )  ; 
} 
if  (  day  !=  -  1  )  { 
if  (  day  <  10  )  { 
dateurl  .  append  (  "0"  )  ; 
} 
dateurl  .  append  (  day  )  .  append  (  "/"  )  ; 
} 
return   dateurl  .  toString  (  )  ; 
} 




public   static   final   Comparator   FILE_NAME_COMPARATOR  =  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
String   s1  =  (  String  )  o1  ; 
String   s2  =  (  String  )  o2  ; 
return   s1  .  compareTo  (  s2  )  ; 
} 
}  ; 

static   final   byte  [  ]  HEX_DIGITS  =  {  (  byte  )  '0'  ,  (  byte  )  '1'  ,  (  byte  )  '2'  ,  (  byte  )  '3'  ,  (  byte  )  '4'  ,  (  byte  )  '5'  ,  (  byte  )  '6'  ,  (  byte  )  '7'  ,  (  byte  )  '8'  ,  (  byte  )  '9'  ,  (  byte  )  'a'  ,  (  byte  )  'b'  ,  (  byte  )  'c'  ,  (  byte  )  'd'  ,  (  byte  )  'e'  ,  (  byte  )  'f'  }  ; 







public   static   String   digestString  (  String   data  )  { 
return   digestString  (  data  ,  DEFAULT_DIGEST_ALGORITHM  )  ; 
} 








public   static   String   digestString  (  String   data  ,  String   algorithm  )  { 
String   result  =  null  ; 
if  (  data  !=  null  )  { 
try  { 
MessageDigest   _md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
_md  .  update  (  data  .  getBytes  (  )  )  ; 
byte  [  ]  _digest  =  _md  .  digest  (  )  ; 
String   _ds  ; 
_ds  =  toHexString  (  _digest  ,  0  ,  _digest  .  length  )  ; 
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









public   static   String   toHexString  (  byte  [  ]  buf  ,  int   offset  ,  int   length  )  { 
byte  [  ]  buf1  =  new   byte  [  length  *  2  ]  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
toHexValue  (  buf1  ,  i  *  2  ,  2  ,  buf  [  i  +  offset  ]  )  ; 
} 
return   new   String  (  buf1  )  ; 
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








public   static   Map   propertiesToMap  (  Properties   properties  )  { 
if  (  properties  ==  null  )  { 
return   new   HashMap  (  )  ; 
}  else  { 
Iterator   keyIterator  =  properties  .  keySet  (  )  .  iterator  (  )  ; 
Object   key  ; 
Object   value  ; 
HashMap   convertedProperties  =  new   HashMap  (  )  ; 
while  (  keyIterator  .  hasNext  (  )  )  { 
key  =  keyIterator  .  next  (  )  ; 
value  =  properties  .  get  (  key  )  ; 
convertedProperties  .  put  (  key  ,  value  )  ; 
} 
return   convertedProperties  ; 
} 
} 








public   static   Map   blojsomPropertiesToMap  (  Properties   properties  )  { 
if  (  properties  ==  null  )  { 
return   new   HashMap  (  )  ; 
}  else  { 
Iterator   keyIterator  =  properties  .  keySet  (  )  .  iterator  (  )  ; 
Object   key  ; 
Object   value  ; 
HashMap   convertedProperties  =  new   HashMap  (  )  ; 
while  (  keyIterator  .  hasNext  (  )  )  { 
key  =  keyIterator  .  next  (  )  ; 
value  =  properties  .  get  (  key  )  ; 
if  (  value   instanceof   List  )  { 
convertedProperties  .  put  (  key  ,  value  )  ; 
}  else  { 
ArrayList   values  =  new   ArrayList  (  )  ; 
values  .  add  (  value  .  toString  (  )  )  ; 
convertedProperties  .  put  (  key  ,  values  )  ; 
} 
} 
return   convertedProperties  ; 
} 
} 









public   static   String   arrayOfStringsToString  (  String  [  ]  array  ,  String   separator  )  { 
if  (  array  ==  null  )  { 
return   null  ; 
} 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
if  (  array  .  length  >  0  )  { 
result  .  append  (  array  [  0  ]  )  ; 
for  (  int   i  =  1  ;  i  <  array  .  length  ;  i  ++  )  { 
result  .  append  (  separator  )  ; 
result  .  append  (  array  [  i  ]  )  ; 
} 
} 
return   result  .  toString  (  )  ; 
} 








public   static   String   arrayOfStringsToString  (  String  [  ]  array  )  { 
return   arrayOfStringsToString  (  array  ,  ", "  )  ; 
} 









public   static   String   getCategoryFromPath  (  String   pathInfo  )  { 
if  (  pathInfo  ==  null  ||  "/"  .  equals  (  pathInfo  )  )  { 
return  "/"  ; 
}  else  { 
int   categoryStart  =  pathInfo  .  indexOf  (  "/"  ,  1  )  ; 
if  (  categoryStart  ==  -  1  )  { 
return  "/"  ; 
}  else  { 
return   pathInfo  .  substring  (  categoryStart  )  ; 
} 
} 
} 









public   static   String   getBlogFromPath  (  String   pathInfo  )  { 
if  (  pathInfo  ==  null  ||  "/"  .  equals  (  pathInfo  )  )  { 
return   null  ; 
}  else  { 
int   userEnd  =  pathInfo  .  indexOf  (  "/"  ,  1  )  ; 
if  (  userEnd  ==  -  1  )  { 
return   pathInfo  .  substring  (  1  )  ; 
}  else  { 
return   pathInfo  .  substring  (  1  ,  userEnd  )  ; 
} 
} 
} 







public   static   boolean   deleteDirectory  (  File   directoryOrFile  )  { 
return   deleteDirectory  (  directoryOrFile  ,  true  )  ; 
} 








public   static   boolean   deleteDirectory  (  File   directoryOrFile  ,  boolean   removeDirectoryOrFile  )  { 
if  (  directoryOrFile  .  isDirectory  (  )  )  { 
File  [  ]  children  =  directoryOrFile  .  listFiles  (  )  ; 
if  (  children  !=  null  &&  children  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
boolean   success  =  deleteDirectory  (  children  [  i  ]  )  ; 
if  (  !  success  )  { 
return   false  ; 
} 
} 
} 
} 
if  (  removeDirectoryOrFile  )  { 
return   directoryOrFile  .  delete  (  )  ; 
} 
return   true  ; 
} 








public   static   void   copyDirectory  (  File   sourceDirectory  ,  File   targetDirectory  )  throws   IOException  { 
File  [  ]  sourceFiles  =  sourceDirectory  .  listFiles  (  FILE_FILTER  )  ; 
File  [  ]  sourceDirectories  =  sourceDirectory  .  listFiles  (  DIRECTORY_FILTER  )  ; 
targetDirectory  .  mkdirs  (  )  ; 
if  (  sourceFiles  !=  null  &&  sourceFiles  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  sourceFiles  .  length  ;  i  ++  )  { 
File   sourceFile  =  sourceFiles  [  i  ]  ; 
FileInputStream   fis  =  new   FileInputStream  (  sourceFile  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  targetDirectory  +  File  .  separator  +  sourceFile  .  getName  (  )  )  ; 
FileChannel   fcin  =  fis  .  getChannel  (  )  ; 
FileChannel   fcout  =  fos  .  getChannel  (  )  ; 
ByteBuffer   buf  =  ByteBuffer  .  allocateDirect  (  8192  )  ; 
long   size  =  fcin  .  size  (  )  ; 
long   n  =  0  ; 
while  (  n  <  size  )  { 
buf  .  clear  (  )  ; 
if  (  fcin  .  read  (  buf  )  <  0  )  { 
break  ; 
} 
buf  .  flip  (  )  ; 
n  +=  fcout  .  write  (  buf  )  ; 
} 
fcin  .  close  (  )  ; 
fcout  .  close  (  )  ; 
fis  .  close  (  )  ; 
fos  .  close  (  )  ; 
} 
} 
if  (  sourceDirectories  !=  null  &&  sourceDirectories  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  sourceDirectories  .  length  ;  i  ++  )  { 
File   directory  =  sourceDirectories  [  i  ]  ; 
File   newTargetDirectory  =  new   File  (  targetDirectory  ,  directory  .  getName  (  )  )  ; 
copyDirectory  (  directory  ,  newTargetDirectory  )  ; 
} 
} 
} 








public   static   Map   arrayOfStringsToMap  (  String  [  ]  array  )  { 
if  (  array  ==  null  )  { 
return   new   HashMap  (  )  ; 
} 
Map   result  =  new   HashMap  (  )  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
result  .  put  (  array  [  i  ]  ,  array  [  i  ]  )  ; 
} 
return   result  ; 
} 







public   static   String   checkStartingAndEndingSlash  (  String   input  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
if  (  !  input  .  startsWith  (  "/"  )  )  { 
input  =  "/"  +  input  ; 
} 
if  (  !  input  .  endsWith  (  "/"  )  )  { 
input  +=  "/"  ; 
} 
return   input  ; 
} 







public   static   boolean   checkNullOrBlank  (  String   input  )  { 
return  (  input  ==  null  ||  ""  .  equals  (  input  .  trim  (  )  )  )  ; 
} 






public   static   void   setNoCacheControlHeaders  (  HttpServletResponse   httpServletResponse  )  { 
httpServletResponse  .  setHeader  (  PRAGMA_HTTP_HEADER  ,  NO_CACHE_HTTP_HEADER_VALUE  )  ; 
httpServletResponse  .  setHeader  (  CACHE_CONTROL_HTTP_HEADER  ,  NO_CACHE_HTTP_HEADER_VALUE  )  ; 
} 









public   static   boolean   checkMapForKey  (  Map   map  ,  String   key  )  { 
if  (  map  ==  null  )  { 
return   false  ; 
} 
if  (  key  ==  null  )  { 
return   false  ; 
} 
return   map  .  containsKey  (  key  )  ; 
} 








public   static   int   daysBetweenDates  (  Date   startDate  ,  Date   endDate  )  { 
if  (  startDate  ==  null  ||  endDate  ==  null  )  { 
return   0  ; 
} 
Calendar   calendarStartDate  =  Calendar  .  getInstance  (  )  ; 
calendarStartDate  .  setTime  (  startDate  )  ; 
int   startDay  =  calendarStartDate  .  get  (  Calendar  .  DAY_OF_YEAR  )  ; 
int   startYear  =  calendarStartDate  .  get  (  Calendar  .  YEAR  )  ; 
Calendar   calendarEndDate  =  Calendar  .  getInstance  (  )  ; 
calendarEndDate  .  setTime  (  endDate  )  ; 
int   endDay  =  calendarEndDate  .  get  (  Calendar  .  DAY_OF_YEAR  )  ; 
int   endYear  =  calendarEndDate  .  get  (  Calendar  .  YEAR  )  ; 
return   Math  .  abs  (  (  endDay  -  startDay  )  +  (  (  endYear  -  startYear  )  *  365  )  )  ; 
} 







public   static   File   getFilenameForDate  (  String   filename  )  { 
String   filenameWithoutExtension  =  getFilename  (  filename  )  ; 
String   fileExtension  =  getFileExtension  (  filename  )  ; 
if  (  fileExtension  ==  null  )  { 
return   null  ; 
}  else  { 
return   new   File  (  filenameWithoutExtension  +  "-"  +  new   Date  (  )  .  getTime  (  )  +  "."  +  fileExtension  )  ; 
} 
} 







public   static   String   stripLineTerminators  (  String   input  )  { 
return   stripLineTerminators  (  input  ,  ""  )  ; 
} 








public   static   String   stripLineTerminators  (  String   input  ,  String   replacement  )  { 
if  (  input  ==  null  )  { 
return   null  ; 
} 
return   input  .  replaceAll  (  "[\n\r\f]"  ,  replacement  )  ; 
} 







public   static   String   getKeysAsStringList  (  Map   input  )  { 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
if  (  input  ==  null  ||  input  .  size  (  )  ==  0  )  { 
return   result  .  toString  (  )  ; 
} 
Iterator   keyIterator  =  input  .  keySet  (  )  .  iterator  (  )  ; 
int   counter  =  0  ; 
while  (  keyIterator  .  hasNext  (  )  )  { 
Object   key  =  keyIterator  .  next  (  )  ; 
result  .  append  (  key  )  ; 
if  (  counter  <  input  .  size  (  )  -  1  )  { 
result  .  append  (  ", "  )  ; 
} 
counter  ++  ; 
} 
return   result  .  toString  (  )  ; 
} 









public   static   String   listToCSV  (  List   values  )  { 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
if  (  values  !=  null  &&  values  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  values  .  size  (  )  ;  i  ++  )  { 
if  (  values  .  get  (  i  )  ==  null  )  { 
result  .  append  (  " "  )  ; 
}  else  { 
result  .  append  (  values  .  get  (  i  )  )  ; 
} 
if  (  i  <  values  .  size  (  )  -  1  )  { 
result  .  append  (  ", "  )  ; 
} 
} 
} 
return   result  .  toString  (  )  ; 
} 








public   static   Map   listToMap  (  List   values  )  { 
Map   valueMap  =  new   HashMap  (  )  ; 
if  (  values  !=  null  &&  values  .  size  (  )  >  0  )  { 
Iterator   valueIterator  =  values  .  iterator  (  )  ; 
Object   value  ; 
while  (  valueIterator  .  hasNext  (  )  )  { 
value  =  valueIterator  .  next  (  )  ; 
if  (  value  !=  null  )  { 
valueMap  .  put  (  value  ,  value  )  ; 
} 
} 
} 
return   valueMap  ; 
} 







public   static   List   csvToList  (  String   valuesAsString  )  { 
String  [  ]  values  =  parseOnlyCommaList  (  valuesAsString  )  ; 
ArrayList   updated  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  values  .  length  ;  i  ++  )  { 
String   value  =  values  [  i  ]  .  trim  (  )  ; 
updated  .  add  (  value  )  ; 
} 
return   updated  ; 
} 







public   static   String   constructBaseURL  (  HttpServletRequest   httpServletRequest  )  { 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
result  .  append  (  httpServletRequest  .  getScheme  (  )  )  .  append  (  "://"  )  ; 
result  .  append  (  httpServletRequest  .  getServerName  (  )  )  ; 
if  (  httpServletRequest  .  getServerPort  (  )  !=  80  )  { 
result  .  append  (  ":"  )  .  append  (  httpServletRequest  .  getServerPort  (  )  )  ; 
} 
result  .  append  (  httpServletRequest  .  getContextPath  (  )  )  ; 
return   result  .  toString  (  )  ; 
} 








public   static   String   constructBlogURL  (  HttpServletRequest   httpServletRequest  ,  String   blogID  )  { 
StringBuffer   result  =  new   StringBuffer  (  constructBaseURL  (  httpServletRequest  )  )  ; 
result  .  append  (  httpServletRequest  .  getServletPath  (  )  )  .  append  (  "/"  )  .  append  (  blogID  )  ; 
return   result  .  toString  (  )  ; 
} 







public   static   String   getHashableContent  (  String   content  )  { 
String   hashable  =  content  ; 
if  (  content  .  length  (  )  >  MAX_HASHABLE_LENGTH  )  { 
hashable  =  hashable  .  substring  (  0  ,  MAX_HASHABLE_LENGTH  )  ; 
} 
return   digestString  (  hashable  )  .  toUpperCase  (  )  ; 
} 








public   static   String   getPostSlug  (  String   title  ,  String   content  )  { 
String   slug  ; 
if  (  !  checkNullOrBlank  (  title  )  )  { 
slug  =  title  .  replaceAll  (  "\\s"  ,  "_"  )  ; 
slug  =  slug  .  replaceAll  (  "'"  ,  ""  )  ; 
slug  =  slug  .  replaceAll  (  "\\p{Punct}"  ,  "_"  )  ; 
slug  =  slug  .  replaceAll  (  "_{2,}"  ,  "_"  )  ; 
slug  =  slug  .  replaceAll  (  "_"  ,  "-"  )  ; 
String   backup  =  slug  ; 
slug  =  slug  .  replaceAll  (  "^-{1,}"  ,  ""  )  ; 
slug  =  slug  .  replaceAll  (  "-{1,}$"  ,  ""  )  ; 
if  (  checkNullOrBlank  (  slug  )  )  { 
slug  =  backup  ; 
} 
}  else  { 
slug  =  getHashableContent  (  content  )  ; 
} 
return   slug  ; 
} 








public   static   Locale   getLocaleFromString  (  String   locale  )  { 
if  (  checkNullOrBlank  (  locale  )  )  { 
return   Locale  .  getDefault  (  )  ; 
} 
String   language  =  locale  ; 
String   country  =  ""  ; 
String   variant  =  ""  ; 
int   index  =  language  .  indexOf  (  '_'  )  ; 
if  (  index  >=  0  )  { 
country  =  language  .  substring  (  index  +  1  )  ; 
language  =  language  .  substring  (  0  ,  index  )  ; 
} 
index  =  country  .  indexOf  (  '_'  )  ; 
if  (  index  >=  0  )  { 
variant  =  country  .  substring  (  index  +  1  )  ; 
country  =  country  .  substring  (  0  ,  index  )  ; 
} 
return   new   Locale  (  language  ,  country  ,  variant  )  ; 
} 







public   static   String  [  ]  getLanguagesForSystem  (  Locale   locale  )  { 
Locale  [  ]  installedLocales  =  Locale  .  getAvailableLocales  (  )  ; 
ArrayList   languageList  =  new   ArrayList  (  installedLocales  .  length  )  ; 
String  [  ]  languages  ; 
String   language  ; 
for  (  int   i  =  0  ;  i  <  installedLocales  .  length  ;  i  ++  )  { 
Locale   installedLocale  =  installedLocales  [  i  ]  ; 
language  =  installedLocale  .  getLanguage  (  )  ; 
if  (  !  languageList  .  contains  (  language  )  &&  !  checkNullOrBlank  (  language  )  )  { 
languageList  .  add  (  language  )  ; 
} 
} 
languages  =  (  String  [  ]  )  languageList  .  toArray  (  new   String  [  languageList  .  size  (  )  ]  )  ; 
Collator   collator  =  Collator  .  getInstance  (  locale  )  ; 
Arrays  .  sort  (  languages  ,  collator  )  ; 
return   languages  ; 
} 







public   static   String  [  ]  getCountriesForSystem  (  Locale   locale  )  { 
Locale  [  ]  installedLocales  =  Locale  .  getAvailableLocales  (  )  ; 
ArrayList   countryList  =  new   ArrayList  (  installedLocales  .  length  )  ; 
String  [  ]  countries  ; 
String   country  ; 
for  (  int   i  =  0  ;  i  <  installedLocales  .  length  ;  i  ++  )  { 
Locale   installedLocale  =  installedLocales  [  i  ]  ; 
country  =  installedLocale  .  getCountry  (  )  ; 
if  (  !  countryList  .  contains  (  country  )  &&  !  checkNullOrBlank  (  country  )  )  { 
countryList  .  add  (  country  )  ; 
} 
} 
countries  =  (  String  [  ]  )  countryList  .  toArray  (  new   String  [  countryList  .  size  (  )  ]  )  ; 
Collator   collator  =  Collator  .  getInstance  (  locale  )  ; 
Arrays  .  sort  (  countries  ,  collator  )  ; 
return   countries  ; 
} 







public   static   String  [  ]  getTimeZonesForSystem  (  Locale   locale  )  { 
String  [  ]  timezones  =  TimeZone  .  getAvailableIDs  (  )  ; 
Collator   collator  =  Collator  .  getInstance  (  locale  )  ; 
Arrays  .  sort  (  timezones  ,  collator  )  ; 
return   timezones  ; 
} 









public   static   void   listFilesInSubdirectories  (  File   directory  ,  String   parentDirectory  ,  List   files  )  { 
if  (  directory  .  isDirectory  (  )  )  { 
String  [  ]  children  =  directory  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
listFilesInSubdirectories  (  new   File  (  directory  ,  children  [  i  ]  )  ,  parentDirectory  ,  files  )  ; 
} 
}  else  { 
if  (  directory  .  getPath  (  )  .  startsWith  (  parentDirectory  )  )  { 
files  .  add  (  new   File  (  directory  .  getPath  (  )  .  substring  (  parentDirectory  .  length  (  )  +  1  )  )  )  ; 
} 
} 
} 









public   static   void   listDirectoriesInSubdirectories  (  File   directory  ,  String   parentDirectory  ,  List   directories  )  { 
if  (  directory  .  isDirectory  (  )  )  { 
String  [  ]  children  =  directory  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
listDirectoriesInSubdirectories  (  new   File  (  directory  ,  children  [  i  ]  )  ,  parentDirectory  ,  directories  )  ; 
} 
if  (  directory  .  getPath  (  )  .  startsWith  (  parentDirectory  )  )  { 
directories  .  add  (  new   File  (  directory  .  getPath  (  )  .  substring  (  parentDirectory  .  length  (  )  )  )  )  ; 
} 
} 
} 







public   static   String   stripHTML  (  String   text  )  { 
if  (  checkNullOrBlank  (  text  )  )  { 
return   text  ; 
} 
Matcher   m  =  STRIP_HTML_PATTERN  .  matcher  (  text  )  ; 
return   m  .  replaceAll  (  ""  )  ; 
} 







public   static   List   arrayToList  (  String  [  ]  input  )  { 
if  (  input  ==  null  ||  input  .  length  ==  0  )  { 
return   new   ArrayList  (  )  ; 
}  else  { 
ArrayList   value  =  new   ArrayList  (  input  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  input  .  length  ;  i  ++  )  { 
String   s  =  input  [  i  ]  ; 
value  .  add  (  s  )  ; 
} 
return   value  ; 
} 
} 







public   static   List   removeNullValues  (  List   input  )  { 
if  (  input  ==  null  )  { 
return   new   ArrayList  (  )  ; 
}  else  { 
ArrayList   sanitizedList  =  new   ArrayList  (  input  .  size  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  input  .  size  (  )  ;  i  ++  )  { 
if  (  input  .  get  (  i  )  !=  null  )  { 
sanitizedList  .  add  (  input  .  get  (  i  )  )  ; 
} 
} 
return   sanitizedList  ; 
} 
} 







public   static   String   addSlashes  (  String   input  )  { 
if  (  input  ==  null  )  { 
return  "/"  ; 
} 
if  (  !  input  .  startsWith  (  "/"  )  )  { 
input  =  "/"  +  input  ; 
} 
if  (  !  input  .  endsWith  (  "/"  )  )  { 
input  +=  "/"  ; 
} 
return   input  ; 
} 







public   static   String   addTrailingSlash  (  String   input  )  { 
if  (  input  ==  null  )  { 
return  "/"  ; 
} 
if  (  !  input  .  endsWith  (  "/"  )  )  { 
input  +=  "/"  ; 
} 
return   input  ; 
} 









public   static   void   resolveDynamicBaseAndBlogURL  (  HttpServletRequest   httpServletRequest  ,  Blog   blog  ,  String   blogID  )  { 
if  (  checkNullOrBlank  (  blog  .  getBlogBaseURL  (  )  )  )  { 
blog  .  setBlogBaseURL  (  constructBaseURL  (  httpServletRequest  )  )  ; 
} 
if  (  checkNullOrBlank  (  blog  .  getBlogURL  (  )  )  )  { 
blog  .  setBlogURL  (  constructBlogURL  (  httpServletRequest  ,  blogID  )  )  ; 
} 
if  (  checkNullOrBlank  (  blog  .  getBlogAdminURL  (  )  )  )  { 
blog  .  setBlogAdminURL  (  constructBlogURL  (  httpServletRequest  ,  blogID  )  )  ; 
} 
if  (  checkNullOrBlank  (  blog  .  getBlogBaseAdminURL  (  )  )  )  { 
blog  .  setBlogBaseAdminURL  (  constructBaseURL  (  httpServletRequest  )  )  ; 
} 
} 








public   static   String   listToString  (  List   values  ,  String   separator  )  { 
StringBuffer   valuesAsString  =  new   StringBuffer  (  )  ; 
if  (  values  !=  null  &&  values  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  values  .  size  (  )  ;  i  ++  )  { 
String   value  =  (  String  )  values  .  get  (  i  )  ; 
valuesAsString  .  append  (  value  )  ; 
if  (  i  <  values  .  size  (  )  -  1  )  { 
valuesAsString  .  append  (  separator  )  ; 
} 
} 
} 
return   valuesAsString  .  toString  (  )  ; 
} 

public   static   Comparator   RESPONSE_COMPARATOR  =  new   Comparator  (  )  { 

public   int   compare  (  Object   object  ,  Object   object1  )  { 
if  (  object   instanceof   Response  &&  object1   instanceof   Response  )  { 
Response   obj  =  (  Response  )  object  ; 
Response   obj1  =  (  Response  )  object1  ; 
if  (  obj  .  getDate  (  )  .  before  (  obj1  .  getDate  (  )  )  )  { 
return  -  1  ; 
}  else   if  (  obj  .  getDate  (  )  .  after  (  obj1  .  getDate  (  )  )  )  { 
return   1  ; 
} 
} 
return   0  ; 
} 
}  ; 








public   static   Date   getFirstDateOfYear  (  Locale   locale  ,  int   year  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  calendar  .  getActualMinimum  (  Calendar  .  MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMinimum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMinimum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMinimum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 








public   static   Date   getLastDateOfYear  (  Locale   locale  ,  int   year  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  calendar  .  getActualMaximum  (  Calendar  .  MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMaximum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMaximum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMaximum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 









public   static   Date   getFirstDateOfYearMonth  (  Locale   locale  ,  int   year  ,  int   month  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  month  )  ; 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMinimum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMinimum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMinimum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 









public   static   Date   getLastDateOfYearMonth  (  Locale   locale  ,  int   year  ,  int   month  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  month  )  ; 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMaximum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMaximum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMaximum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 










public   static   Date   getFirstDateOfYearMonthDay  (  Locale   locale  ,  int   year  ,  int   month  ,  int   day  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  month  )  ; 
if  (  day  <  calendar  .  getActualMinimum  (  Calendar  .  DAY_OF_MONTH  )  )  { 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMinimum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
}  else  { 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  day  )  ; 
} 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMinimum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMinimum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMinimum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 










public   static   Date   getLastDateOfYearMonthDay  (  Locale   locale  ,  int   year  ,  int   month  ,  int   day  )  { 
Calendar   calendar  =  Calendar  .  getInstance  (  locale  )  ; 
calendar  .  set  (  Calendar  .  YEAR  ,  year  )  ; 
calendar  .  set  (  Calendar  .  MONTH  ,  month  )  ; 
if  (  day  >  calendar  .  getActualMaximum  (  Calendar  .  DAY_OF_MONTH  )  )  { 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  calendar  .  getActualMaximum  (  Calendar  .  DAY_OF_MONTH  )  )  ; 
}  else  { 
calendar  .  set  (  Calendar  .  DAY_OF_MONTH  ,  day  )  ; 
} 
calendar  .  set  (  Calendar  .  HOUR_OF_DAY  ,  calendar  .  getActualMaximum  (  Calendar  .  HOUR_OF_DAY  )  )  ; 
calendar  .  set  (  Calendar  .  MINUTE  ,  calendar  .  getActualMaximum  (  Calendar  .  MINUTE  )  )  ; 
calendar  .  set  (  Calendar  .  SECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  SECOND  )  )  ; 
calendar  .  set  (  Calendar  .  MILLISECOND  ,  calendar  .  getActualMaximum  (  Calendar  .  MILLISECOND  )  )  ; 
return   calendar  .  getTime  (  )  ; 
} 
} 

