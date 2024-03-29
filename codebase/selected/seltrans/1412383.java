package   org  .  roller  .  presentation  .  pagecache  .  rollercache  ; 

import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  TreeMap  ; 
import   javax  .  servlet  .  FilterChain  ; 
import   javax  .  servlet  .  FilterConfig  ; 
import   javax  .  servlet  .  ServletException  ; 
import   javax  .  servlet  .  ServletRequest  ; 
import   javax  .  servlet  .  ServletResponse  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  roller  .  pojos  .  UserData  ; 
import   org  .  roller  .  presentation  .  LanguageUtil  ; 
import   org  .  roller  .  presentation  .  pagecache  .  FilterHandler  ; 
import   org  .  roller  .  util  .  LRUCache2  ; 











public   class   LRUCacheHandler2   implements   FilterHandler  { 

private   static   Log   mLogger  =  LogFactory  .  getFactory  (  )  .  getInstance  (  LRUCacheHandler2  .  class  )  ; 

private   LRUCache2   mPageCache  =  null  ; 

private   String   mName  =  null  ; 

private   int   misses  =  0  ; 

private   int   hits  =  0  ; 

private   static   final   String   FILE_SEPARATOR  =  "/"  ; 

private   static   final   char   FILE_SEPARATOR_CHAR  =  FILE_SEPARATOR  .  charAt  (  0  )  ; 

private   static   final   short   AVERAGE_KEY_LENGTH  =  30  ; 

private   static   final   String   m_strBase64Chars  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"  ; 

public   LRUCacheHandler2  (  FilterConfig   config  )  { 
mName  =  config  .  getFilterName  (  )  ; 
mLogger  .  info  (  "Initializing for: "  +  mName  )  ; 
int   size  =  200  ; 
try  { 
size  =  Integer  .  parseInt  (  config  .  getInitParameter  (  "size"  )  )  ; 
}  catch  (  Exception   e  )  { 
mLogger  .  warn  (  config  .  getFilterName  (  )  +  "Can't read cache size parameter, using default..."  )  ; 
} 
mLogger  .  info  (  mName  +  " size="  +  size  )  ; 
long   timeout  =  30000  ; 
try  { 
timeout  =  Long  .  parseLong  (  config  .  getInitParameter  (  "timeout"  )  )  ; 
if  (  timeout  <  (  30  *  1000  )  )  { 
timeout  =  30  *  1000  ; 
mLogger  .  warn  (  config  .  getFilterName  (  )  +  "timeout cannot be less than 30 seconds"  )  ; 
} 
}  catch  (  Exception   e  )  { 
mLogger  .  warn  (  config  .  getFilterName  (  )  +  "Can't read timeout parameter, using default."  )  ; 
} 
mLogger  .  info  (  mName  +  " timeout="  +  timeout  )  ; 
mPageCache  =  new   LRUCache2  (  size  ,  timeout  )  ; 
} 




public   void   destroy  (  )  { 
} 






public   void   doFilter  (  ServletRequest   req  ,  ServletResponse   res  ,  FilterChain   chain  )  throws   ServletException  ,  IOException  { 
HttpServletRequest   request  =  (  HttpServletRequest  )  req  ; 
HttpServletResponse   response  =  (  HttpServletResponse  )  res  ; 
Locale   locale  =  LanguageUtil  .  getViewLocale  (  request  )  ; 
String   generatedKey  =  null  ; 
if  (  locale  !=  null  )  { 
generatedKey  =  generateEntryKey  (  null  ,  request  ,  1  ,  locale  .  getLanguage  (  )  )  ; 
}  else  { 
generatedKey  =  generateEntryKey  (  null  ,  request  ,  1  ,  null  )  ; 
} 
java  .  security  .  Principal   prince  =  request  .  getUserPrincipal  (  )  ; 
StringBuffer   keyb  =  new   StringBuffer  (  )  ; 
keyb  .  append  (  generatedKey  )  ; 
if  (  prince  !=  null  )  { 
keyb  .  append  (  "_"  )  ; 
keyb  .  append  (  prince  )  ; 
} 
String   key  =  keyb  .  toString  (  )  ; 
ResponseContent   respContent  =  (  ResponseContent  )  getFromCache  (  key  )  ; 
if  (  respContent  ==  null  )  { 
try  { 
CacheHttpServletResponseWrapper   cacheResponse  =  new   CacheHttpServletResponseWrapper  (  response  )  ; 
chain  .  doFilter  (  request  ,  cacheResponse  )  ; 
if  (  request  .  getAttribute  (  "DisplayException"  )  ==  null  )  { 
ResponseContent   rc  =  cacheResponse  .  getContent  (  )  ; 
putToCache  (  key  ,  rc  )  ; 
}  else  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  "Display exception, cache, key="  )  ; 
sb  .  append  (  key  )  ; 
mLogger  .  error  (  sb  .  toString  (  )  )  ; 
} 
}  catch  (  java  .  net  .  SocketException   se  )  { 
}  catch  (  Exception   e  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  "Error rendering page, key="  )  ; 
sb  .  append  (  key  )  ; 
mLogger  .  error  (  sb  .  toString  (  )  )  ; 
} 
}  else  { 
try  { 
respContent  .  writeTo  (  response  )  ; 
}  catch  (  java  .  net  .  SocketException   se  )  { 
}  catch  (  Exception   e  )  { 
if  (  mLogger  .  isDebugEnabled  (  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  "Probably a client abort exception, key="  )  ; 
sb  .  append  (  key  )  ; 
mLogger  .  error  (  sb  .  toString  (  )  )  ; 
} 
} 
} 
} 




public   synchronized   void   flushCache  (  HttpServletRequest   req  )  { 
mPageCache  .  purge  (  )  ; 
} 




public   synchronized   void   removeFromCache  (  HttpServletRequest   req  ,  UserData   user  )  { 
String   rssString  =  "/rss/"  +  user  .  getUserName  (  )  ; 
String   pageString  =  "/page/"  +  user  .  getUserName  (  )  ; 
String   mainRssString  =  "/rss_"  ; 
int   beforeSize  =  mPageCache  .  size  (  )  ; 
mPageCache  .  purge  (  new   String  [  ]  {  rssString  ,  pageString  ,  mainRssString  }  )  ; 
int   afterSize  =  mPageCache  .  size  (  )  ; 
if  (  mLogger  .  isDebugEnabled  (  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  "Purged, count="  )  ; 
sb  .  append  (  beforeSize  -  afterSize  )  ; 
sb  .  append  (  ", user="  )  ; 
sb  .  append  (  user  .  getUserName  (  )  )  ; 
mLogger  .  debug  (  sb  .  toString  (  )  )  ; 
} 
} 





public   synchronized   Object   getFromCache  (  String   key  )  { 
Object   entry  =  mPageCache  .  get  (  key  )  ; 
if  (  entry  !=  null  &&  mLogger  .  isDebugEnabled  (  )  )  { 
hits  ++  ; 
} 
return   entry  ; 
} 

public   synchronized   void   putToCache  (  String   key  ,  Object   entry  )  { 
mPageCache  .  put  (  key  ,  entry  )  ; 
if  (  mLogger  .  isDebugEnabled  (  )  )  { 
misses  ++  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
sb  .  append  (  "Missed, cache size="  )  ; 
sb  .  append  (  mPageCache  .  size  (  )  )  ; 
sb  .  append  (  ", hits="  )  ; 
sb  .  append  (  hits  )  ; 
sb  .  append  (  ", misses="  )  ; 
sb  .  append  (  misses  )  ; 
sb  .  append  (  ", key="  )  ; 
sb  .  append  (  key  )  ; 
mLogger  .  debug  (  sb  .  toString  (  )  )  ; 
} 
} 

public   String   generateEntryKey  (  String   key  ,  HttpServletRequest   request  ,  int   scope  ,  String   language  )  { 
StringBuffer   cBuffer  =  new   StringBuffer  (  AVERAGE_KEY_LENGTH  )  ; 
if  (  language  !=  null  )  { 
cBuffer  .  append  (  FILE_SEPARATOR  )  .  append  (  language  )  ; 
} 
if  (  key  !=  null  )  { 
cBuffer  .  append  (  FILE_SEPARATOR  )  .  append  (  key  )  ; 
}  else  { 
String   generatedKey  =  request  .  getRequestURI  (  )  ; 
if  (  generatedKey  .  charAt  (  0  )  !=  FILE_SEPARATOR_CHAR  )  { 
cBuffer  .  append  (  FILE_SEPARATOR_CHAR  )  ; 
} 
cBuffer  .  append  (  generatedKey  )  ; 
cBuffer  .  append  (  "_"  )  .  append  (  request  .  getMethod  (  )  )  .  append  (  "_"  )  ; 
generatedKey  =  getSortedQueryString  (  request  )  ; 
if  (  generatedKey  !=  null  )  { 
try  { 
java  .  security  .  MessageDigest   digest  =  java  .  security  .  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte  [  ]  b  =  digest  .  digest  (  generatedKey  .  getBytes  (  )  )  ; 
cBuffer  .  append  (  "_"  )  ; 
cBuffer  .  append  (  toBase64  (  b  )  .  replace  (  '/'  ,  '_'  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
return   cBuffer  .  toString  (  )  ; 
} 

protected   String   getSortedQueryString  (  HttpServletRequest   request  )  { 
Map   paramMap  =  request  .  getParameterMap  (  )  ; 
if  (  paramMap  .  isEmpty  (  )  )  { 
return   null  ; 
} 
Set   paramSet  =  new   TreeMap  (  paramMap  )  .  entrySet  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
boolean   first  =  true  ; 
for  (  Iterator   it  =  paramSet  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  it  .  next  (  )  ; 
String  [  ]  values  =  (  String  [  ]  )  entry  .  getValue  (  )  ; 
for  (  int   i  =  0  ;  i  <  values  .  length  ;  i  ++  )  { 
String   key  =  (  String  )  entry  .  getKey  (  )  ; 
if  (  (  key  .  length  (  )  !=  10  )  ||  !  "jsessionid"  .  equals  (  key  )  )  { 
if  (  first  )  { 
first  =  false  ; 
}  else  { 
buf  .  append  (  '&'  )  ; 
} 
buf  .  append  (  key  )  .  append  (  '='  )  .  append  (  values  [  i  ]  )  ; 
} 
} 
} 
if  (  buf  .  length  (  )  ==  0  )  { 
return   null  ; 
}  else  { 
return   buf  .  toString  (  )  ; 
} 
} 




private   static   String   toBase64  (  byte  [  ]  aValue  )  { 
int   byte1  ; 
int   byte2  ; 
int   byte3  ; 
int   iByteLen  =  aValue  .  length  ; 
StringBuffer   tt  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  iByteLen  ;  i  +=  3  )  { 
boolean   bByte2  =  (  i  +  1  )  <  iByteLen  ; 
boolean   bByte3  =  (  i  +  2  )  <  iByteLen  ; 
byte1  =  aValue  [  i  ]  &  0xFF  ; 
byte2  =  (  bByte2  )  ?  (  aValue  [  i  +  1  ]  &  0xFF  )  :  0  ; 
byte3  =  (  bByte3  )  ?  (  aValue  [  i  +  2  ]  &  0xFF  )  :  0  ; 
tt  .  append  (  m_strBase64Chars  .  charAt  (  byte1  /  4  )  )  ; 
tt  .  append  (  m_strBase64Chars  .  charAt  (  (  byte2  /  16  )  +  (  (  byte1  &  0x3  )  *  16  )  )  )  ; 
tt  .  append  (  (  (  bByte2  )  ?  m_strBase64Chars  .  charAt  (  (  byte3  /  64  )  +  (  (  byte2  &  0xF  )  *  4  )  )  :  '='  )  )  ; 
tt  .  append  (  (  (  bByte3  )  ?  m_strBase64Chars  .  charAt  (  byte3  &  0x3F  )  :  '='  )  )  ; 
} 
return   tt  .  toString  (  )  ; 
} 
} 

