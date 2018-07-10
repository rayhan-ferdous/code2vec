package   org  .  apache  .  roller  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  NoSuchElementException  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   org  .  apache  .  commons  .  lang  .  StringEscapeUtils  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 





















public   class   Utilities  { 


private   static   Log   mLogger  =  LogFactory  .  getLog  (  Utilities  .  class  )  ; 


private   static   Pattern   mLinkPattern  =  Pattern  .  compile  (  "<a href=.*?>"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_B_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;b&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_B_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/b&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_I_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;i&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_I_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/i&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_BLOCKQUOTE_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;blockquote&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_BLOCKQUOTE_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/blockquote&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   BR_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;br */*&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_P_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;p&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_P_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/p&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_PRE_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;pre&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_PRE_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/pre&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_UL_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;ul&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_UL_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/ul&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_OL_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;ol&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_OL_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/ol&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_LI_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;li&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_LI_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/li&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   CLOSING_A_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;/a&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   OPENING_A_TAG_PATTERN  =  Pattern  .  compile  (  "&lt;a href=.*?&gt;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 

private   static   final   Pattern   QUOTE_PATTERN  =  Pattern  .  compile  (  "&quot;"  ,  Pattern  .  CASE_INSENSITIVE  )  ; 




public   static   boolean   isNotEmpty  (  String   str  )  { 
return   StringUtils  .  isNotEmpty  (  str  )  ; 
} 




public   static   boolean   isEmpty  (  String   str  )  { 
return   StringUtils  .  isEmpty  (  str  )  ; 
} 


public   static   String   stripJsessionId  (  String   url  )  { 
int   startPos  =  url  .  indexOf  (  ";jsessionid="  )  ; 
if  (  startPos  !=  -  1  )  { 
int   endPos  =  url  .  indexOf  (  "?"  ,  startPos  )  ; 
if  (  endPos  ==  -  1  )  { 
url  =  url  .  substring  (  0  ,  startPos  )  ; 
}  else  { 
url  =  url  .  substring  (  0  ,  startPos  )  +  url  .  substring  (  endPos  ,  url  .  length  (  )  )  ; 
} 
} 
return   url  ; 
} 





public   static   String   escapeHTML  (  String   s  )  { 
return   escapeHTML  (  s  ,  true  )  ; 
} 






public   static   String   escapeHTML  (  String   s  ,  boolean   escapeAmpersand  )  { 
if  (  escapeAmpersand  )  { 
s  =  stringReplace  (  s  ,  "&"  ,  "&amp;"  )  ; 
} 
s  =  stringReplace  (  s  ,  "&nbsp;"  ,  " "  )  ; 
s  =  stringReplace  (  s  ,  "\""  ,  "&quot;"  )  ; 
s  =  stringReplace  (  s  ,  "<"  ,  "&lt;"  )  ; 
s  =  stringReplace  (  s  ,  ">"  ,  "&gt;"  )  ; 
return   s  ; 
} 

public   static   String   unescapeHTML  (  String   str  )  { 
return   StringEscapeUtils  .  unescapeHtml  (  str  )  ; 
} 






public   static   String   removeHTML  (  String   str  )  { 
return   removeHTML  (  str  ,  true  )  ; 
} 










public   static   String   removeHTML  (  String   str  ,  boolean   addSpace  )  { 
if  (  str  ==  null  )  return  ""  ; 
StringBuffer   ret  =  new   StringBuffer  (  str  .  length  (  )  )  ; 
int   start  =  0  ; 
int   beginTag  =  str  .  indexOf  (  "<"  )  ; 
int   endTag  =  0  ; 
if  (  beginTag  ==  -  1  )  return   str  ; 
while  (  beginTag  >=  start  )  { 
if  (  beginTag  >  0  )  { 
ret  .  append  (  str  .  substring  (  start  ,  beginTag  )  )  ; 
if  (  addSpace  )  ret  .  append  (  " "  )  ; 
} 
endTag  =  str  .  indexOf  (  ">"  ,  beginTag  )  ; 
if  (  endTag  >  -  1  )  { 
start  =  endTag  +  1  ; 
beginTag  =  str  .  indexOf  (  "<"  ,  start  )  ; 
}  else  { 
ret  .  append  (  str  .  substring  (  beginTag  )  )  ; 
break  ; 
} 
} 
if  (  endTag  >  -  1  &&  endTag  +  1  <  str  .  length  (  )  )  { 
ret  .  append  (  str  .  substring  (  endTag  +  1  )  )  ; 
} 
return   ret  .  toString  (  )  .  trim  (  )  ; 
} 





public   static   String   removeAndEscapeHTML  (  String   s  )  { 
if  (  s  ==  null  )  return  ""  ;  else   return   Utilities  .  escapeHTML  (  Utilities  .  removeHTML  (  s  )  )  ; 
} 




public   static   String   autoformat  (  String   s  )  { 
String   ret  =  StringUtils  .  replace  (  s  ,  "\n"  ,  "<br />"  )  ; 
return   ret  ; 
} 




public   static   String   formatIso8601Date  (  Date   d  )  { 
return   DateUtil  .  formatIso8601  (  d  )  ; 
} 




public   static   String   formatIso8601Day  (  Date   d  )  { 
return   DateUtil  .  formatIso8601Day  (  d  )  ; 
} 




public   static   String   formatRfc822Date  (  Date   date  )  { 
return   DateUtil  .  formatRfc822  (  date  )  ; 
} 




public   static   String   format8charsDate  (  Date   date  )  { 
return   DateUtil  .  format8chars  (  date  )  ; 
} 




public   static   String   replaceNonAlphanumeric  (  String   str  )  { 
return   replaceNonAlphanumeric  (  str  ,  '_'  )  ; 
} 





public   static   String   replaceNonAlphanumeric  (  String   str  ,  char   subst  )  { 
StringBuffer   ret  =  new   StringBuffer  (  str  .  length  (  )  )  ; 
char  [  ]  testChars  =  str  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  testChars  .  length  ;  i  ++  )  { 
if  (  Character  .  isLetterOrDigit  (  testChars  [  i  ]  )  )  { 
ret  .  append  (  testChars  [  i  ]  )  ; 
}  else  { 
ret  .  append  (  subst  )  ; 
} 
} 
return   ret  .  toString  (  )  ; 
} 




public   static   String   removeNonAlphanumeric  (  String   str  )  { 
StringBuffer   ret  =  new   StringBuffer  (  str  .  length  (  )  )  ; 
char  [  ]  testChars  =  str  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  testChars  .  length  ;  i  ++  )  { 
if  (  Character  .  isLetterOrDigit  (  testChars  [  i  ]  )  ||  testChars  [  i  ]  ==  '.'  )  { 
ret  .  append  (  testChars  [  i  ]  )  ; 
} 
} 
return   ret  .  toString  (  )  ; 
} 






public   static   String   stringArrayToString  (  String  [  ]  stringArray  ,  String   delim  )  { 
String   ret  =  ""  ; 
for  (  int   i  =  0  ;  i  <  stringArray  .  length  ;  i  ++  )  { 
if  (  ret  .  length  (  )  >  0  )  ret  =  ret  +  delim  +  stringArray  [  i  ]  ;  else   ret  =  stringArray  [  i  ]  ; 
} 
return   ret  ; 
} 




public   static   String   stringReplace  (  String   str  ,  String   str1  ,  String   str2  )  { 
String   ret  =  StringUtils  .  replace  (  str  ,  str1  ,  str2  )  ; 
return   ret  ; 
} 








public   static   String   stringReplace  (  String   str  ,  String   str1  ,  String   str2  ,  int   maxCount  )  { 
String   ret  =  StringUtils  .  replace  (  str  ,  str1  ,  str2  ,  maxCount  )  ; 
return   ret  ; 
} 


public   static   String  [  ]  stringToStringArray  (  String   instr  ,  String   delim  )  throws   NoSuchElementException  ,  NumberFormatException  { 
StringTokenizer   toker  =  new   StringTokenizer  (  instr  ,  delim  )  ; 
String   stringArray  [  ]  =  new   String  [  toker  .  countTokens  (  )  ]  ; 
int   i  =  0  ; 
while  (  toker  .  hasMoreTokens  (  )  )  { 
stringArray  [  i  ++  ]  =  toker  .  nextToken  (  )  ; 
} 
return   stringArray  ; 
} 


public   static   int  [  ]  stringToIntArray  (  String   instr  ,  String   delim  )  throws   NoSuchElementException  ,  NumberFormatException  { 
StringTokenizer   toker  =  new   StringTokenizer  (  instr  ,  delim  )  ; 
int   intArray  [  ]  =  new   int  [  toker  .  countTokens  (  )  ]  ; 
int   i  =  0  ; 
while  (  toker  .  hasMoreTokens  (  )  )  { 
String   sInt  =  toker  .  nextToken  (  )  ; 
int   nInt  =  Integer  .  parseInt  (  sInt  )  ; 
intArray  [  i  ++  ]  =  new   Integer  (  nInt  )  .  intValue  (  )  ; 
} 
return   intArray  ; 
} 


public   static   String   intArrayToString  (  int  [  ]  intArray  )  { 
String   ret  =  ""  ; 
for  (  int   i  =  0  ;  i  <  intArray  .  length  ;  i  ++  )  { 
if  (  ret  .  length  (  )  >  0  )  ret  =  ret  +  ","  +  Integer  .  toString  (  intArray  [  i  ]  )  ;  else   ret  =  Integer  .  toString  (  intArray  [  i  ]  )  ; 
} 
return   ret  ; 
} 

public   static   void   copyFile  (  File   from  ,  File   to  )  throws   IOException  { 
InputStream   in  =  null  ; 
OutputStream   out  =  null  ; 
try  { 
in  =  new   FileInputStream  (  from  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   IOException  (  "Utilities.copyFile: opening input stream '"  +  from  .  getPath  (  )  +  "', "  +  ex  .  getMessage  (  )  )  ; 
} 
try  { 
out  =  new   FileOutputStream  (  to  )  ; 
}  catch  (  Exception   ex  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ex1  )  { 
} 
throw   new   IOException  (  "Utilities.copyFile: opening output stream '"  +  to  .  getPath  (  )  +  "', "  +  ex  .  getMessage  (  )  )  ; 
} 
copyInputToOutput  (  in  ,  out  ,  from  .  length  (  )  )  ; 
} 





public   static   void   copyInputToOutput  (  InputStream   input  ,  OutputStream   output  ,  long   byteCount  )  throws   IOException  { 
int   bytes  ; 
long   length  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  input  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  output  )  ; 
byte  [  ]  buffer  ; 
buffer  =  new   byte  [  8192  ]  ; 
for  (  length  =  byteCount  ;  length  >  0  ;  )  { 
bytes  =  (  int  )  (  length  >  8192  ?  8192  :  length  )  ; 
try  { 
bytes  =  in  .  read  (  buffer  ,  0  ,  bytes  )  ; 
}  catch  (  IOException   ex  )  { 
try  { 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   ex1  )  { 
} 
throw   new   IOException  (  "Reading input stream, "  +  ex  .  getMessage  (  )  )  ; 
} 
if  (  bytes  <  0  )  break  ; 
length  -=  bytes  ; 
try  { 
out  .  write  (  buffer  ,  0  ,  bytes  )  ; 
}  catch  (  IOException   ex  )  { 
try  { 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   ex1  )  { 
} 
throw   new   IOException  (  "Writing output stream, "  +  ex  .  getMessage  (  )  )  ; 
} 
} 
try  { 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   IOException  (  "Closing file streams, "  +  ex  .  getMessage  (  )  )  ; 
} 
} 

public   static   void   copyInputToOutput  (  InputStream   input  ,  OutputStream   output  )  throws   IOException  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  input  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  output  )  ; 
byte   buffer  [  ]  =  new   byte  [  8192  ]  ; 
for  (  int   count  =  0  ;  count  !=  -  1  ;  )  { 
count  =  in  .  read  (  buffer  ,  0  ,  8192  )  ; 
if  (  count  !=  -  1  )  out  .  write  (  buffer  ,  0  ,  count  )  ; 
} 
try  { 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   IOException  (  "Closing file streams, "  +  ex  .  getMessage  (  )  )  ; 
} 
} 












public   static   String   encodePassword  (  String   password  ,  String   algorithm  )  { 
byte  [  ]  unencodedPassword  =  password  .  getBytes  (  )  ; 
MessageDigest   md  =  null  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  algorithm  )  ; 
}  catch  (  Exception   e  )  { 
mLogger  .  error  (  "Exception: "  +  e  )  ; 
return   password  ; 
} 
md  .  reset  (  )  ; 
md  .  update  (  unencodedPassword  )  ; 
byte  [  ]  encodedPassword  =  md  .  digest  (  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  encodedPassword  .  length  ;  i  ++  )  { 
if  (  (  encodedPassword  [  i  ]  &  0xff  )  <  0x10  )  { 
buf  .  append  (  "0"  )  ; 
} 
buf  .  append  (  Long  .  toString  (  encodedPassword  [  i  ]  &  0xff  ,  16  )  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 












public   static   String   encodeString  (  String   str  )  throws   IOException  { 
sun  .  misc  .  BASE64Encoder   encoder  =  new   sun  .  misc  .  BASE64Encoder  (  )  ; 
String   encodedStr  =  encoder  .  encodeBuffer  (  str  .  getBytes  (  )  )  ; 
return  (  encodedStr  .  trim  (  )  )  ; 
} 








public   static   String   decodeString  (  String   str  )  throws   IOException  { 
sun  .  misc  .  BASE64Decoder   dec  =  new   sun  .  misc  .  BASE64Decoder  (  )  ; 
String   value  =  new   String  (  dec  .  decodeBuffer  (  str  )  )  ; 
return  (  value  )  ; 
} 




public   static   String   truncate  (  String   str  ,  int   lower  ,  int   upper  ,  String   appendToEnd  )  { 
String   str2  =  removeHTML  (  str  ,  false  )  ; 
if  (  upper  <  lower  )  { 
upper  =  lower  ; 
} 
if  (  str2  .  length  (  )  >  upper  )  { 
int   loc  ; 
loc  =  str2  .  lastIndexOf  (  ' '  ,  upper  )  ; 
if  (  loc  >=  lower  )  { 
str2  =  str2  .  substring  (  0  ,  loc  )  ; 
}  else  { 
str2  =  str2  .  substring  (  0  ,  upper  )  ; 
loc  =  upper  ; 
} 
str2  =  str2  +  appendToEnd  ; 
} 
return   str2  ; 
} 













public   static   String   truncateNicely  (  String   str  ,  int   lower  ,  int   upper  ,  String   appendToEnd  )  { 
String   str2  =  removeHTML  (  str  ,  false  )  ; 
boolean   diff  =  (  str2  .  length  (  )  <  str  .  length  (  )  )  ; 
if  (  upper  <  lower  )  { 
upper  =  lower  ; 
} 
if  (  str2  .  length  (  )  >  upper  )  { 
int   loc  ; 
loc  =  str2  .  lastIndexOf  (  ' '  ,  upper  )  ; 
if  (  loc  >=  lower  )  { 
str2  =  str2  .  substring  (  0  ,  loc  )  ; 
}  else  { 
str2  =  str2  .  substring  (  0  ,  upper  )  ; 
loc  =  upper  ; 
} 
if  (  diff  )  { 
loc  =  str2  .  lastIndexOf  (  ' '  ,  loc  )  ; 
String   str3  =  str2  .  substring  (  loc  +  1  )  ; 
loc  =  str  .  indexOf  (  str3  ,  loc  )  +  str3  .  length  (  )  ; 
str2  =  str  .  substring  (  0  ,  loc  )  ; 
str3  =  extractHTML  (  str  .  substring  (  loc  )  )  ; 
str  =  str2  +  appendToEnd  +  str3  ; 
}  else  { 
str  =  str2  +  appendToEnd  ; 
} 
} 
return   str  ; 
} 

public   static   String   truncateText  (  String   str  ,  int   lower  ,  int   upper  ,  String   appendToEnd  )  { 
String   str2  =  removeHTML  (  str  ,  false  )  ; 
boolean   diff  =  (  str2  .  length  (  )  <  str  .  length  (  )  )  ; 
if  (  upper  <  lower  )  { 
upper  =  lower  ; 
} 
if  (  str2  .  length  (  )  >  upper  )  { 
int   loc  ; 
loc  =  str2  .  lastIndexOf  (  ' '  ,  upper  )  ; 
if  (  loc  >=  lower  )  { 
str2  =  str2  .  substring  (  0  ,  loc  )  ; 
}  else  { 
str2  =  str2  .  substring  (  0  ,  upper  )  ; 
loc  =  upper  ; 
} 
str  =  str2  +  appendToEnd  ; 
} 
return   str  ; 
} 





private   static   String   stripLineBreaks  (  String   str  )  { 
str  =  str  .  replaceAll  (  "<br>"  ,  ""  )  ; 
str  =  str  .  replaceAll  (  "<br/>"  ,  ""  )  ; 
str  =  str  .  replaceAll  (  "<br />"  ,  ""  )  ; 
str  =  str  .  replaceAll  (  "<p></p>"  ,  ""  )  ; 
str  =  str  .  replaceAll  (  "<p/>"  ,  ""  )  ; 
str  =  str  .  replaceAll  (  "<p />"  ,  ""  )  ; 
return   str  ; 
} 













private   static   String   removeVisibleHTMLTags  (  String   str  )  { 
str  =  stripLineBreaks  (  str  )  ; 
StringBuffer   result  =  new   StringBuffer  (  str  )  ; 
StringBuffer   lcresult  =  new   StringBuffer  (  str  .  toLowerCase  (  )  )  ; 
String  [  ]  visibleTags  =  {  "<img"  }  ; 
int   stringIndex  ; 
for  (  int   j  =  0  ;  j  <  visibleTags  .  length  ;  j  ++  )  { 
while  (  (  stringIndex  =  lcresult  .  indexOf  (  visibleTags  [  j  ]  )  )  !=  -  1  )  { 
if  (  visibleTags  [  j  ]  .  endsWith  (  ">"  )  )  { 
result  .  delete  (  stringIndex  ,  stringIndex  +  visibleTags  [  j  ]  .  length  (  )  )  ; 
lcresult  .  delete  (  stringIndex  ,  stringIndex  +  visibleTags  [  j  ]  .  length  (  )  )  ; 
}  else  { 
int   endIndex  =  result  .  indexOf  (  ">"  ,  stringIndex  )  ; 
if  (  endIndex  >  -  1  )  { 
result  .  delete  (  stringIndex  ,  endIndex  +  1  )  ; 
lcresult  .  delete  (  stringIndex  ,  endIndex  +  1  )  ; 
} 
} 
} 
} 
String  [  ]  openCloseTags  =  {  "li"  ,  "a"  ,  "div"  ,  "h1"  ,  "h2"  ,  "h3"  ,  "h4"  }  ; 
for  (  int   j  =  0  ;  j  <  openCloseTags  .  length  ;  j  ++  )  { 
String   closeTag  =  "</"  +  openCloseTags  [  j  ]  +  ">"  ; 
int   lastStringIndex  =  0  ; 
while  (  (  stringIndex  =  lcresult  .  indexOf  (  "<"  +  openCloseTags  [  j  ]  ,  lastStringIndex  )  )  >  -  1  )  { 
lastStringIndex  =  stringIndex  ; 
int   endIndex  =  lcresult  .  indexOf  (  closeTag  ,  stringIndex  )  ; 
if  (  endIndex  >  -  1  )  { 
result  .  delete  (  stringIndex  ,  endIndex  +  closeTag  .  length  (  )  )  ; 
lcresult  .  delete  (  stringIndex  ,  endIndex  +  closeTag  .  length  (  )  )  ; 
}  else  { 
endIndex  =  lcresult  .  indexOf  (  ">"  ,  stringIndex  )  ; 
int   nextStart  =  lcresult  .  indexOf  (  "<"  ,  stringIndex  +  1  )  ; 
if  (  endIndex  >  stringIndex  &&  lcresult  .  charAt  (  endIndex  -  1  )  ==  '/'  &&  (  endIndex  <  nextStart  ||  nextStart  ==  -  1  )  )  { 
result  .  delete  (  stringIndex  ,  endIndex  +  1  )  ; 
lcresult  .  delete  (  stringIndex  ,  endIndex  +  1  )  ; 
} 
} 
} 
} 
return   result  .  toString  (  )  ; 
} 






public   static   String   extractHTML  (  String   str  )  { 
if  (  str  ==  null  )  return  ""  ; 
StringBuffer   ret  =  new   StringBuffer  (  str  .  length  (  )  )  ; 
int   start  =  0  ; 
int   beginTag  =  str  .  indexOf  (  "<"  )  ; 
int   endTag  =  0  ; 
if  (  beginTag  ==  -  1  )  return   str  ; 
while  (  beginTag  >=  start  )  { 
endTag  =  str  .  indexOf  (  ">"  ,  beginTag  )  ; 
if  (  endTag  >  -  1  )  { 
ret  .  append  (  str  .  substring  (  beginTag  ,  endTag  +  1  )  )  ; 
start  =  endTag  +  1  ; 
beginTag  =  str  .  indexOf  (  "<"  ,  start  )  ; 
}  else  { 
break  ; 
} 
} 
return   ret  .  toString  (  )  ; 
} 

public   static   String   hexEncode  (  String   str  )  { 
if  (  StringUtils  .  isEmpty  (  str  )  )  return   str  ; 
return   RegexUtil  .  encode  (  str  )  ; 
} 

public   static   String   encodeEmail  (  String   str  )  { 
return   str  !=  null  ?  RegexUtil  .  encodeEmail  (  str  )  :  null  ; 
} 










public   static   final   String   charToHTML  (  char   ch  ,  boolean   xml  )  { 
int   c  ; 
if  (  ch  ==  '<'  )  { 
return  (  "&lt;"  )  ; 
}  else   if  (  ch  ==  '>'  )  { 
return  (  "&gt;"  )  ; 
}  else   if  (  ch  ==  '&'  )  { 
return  (  "&amp;"  )  ; 
}  else   if  (  xml  &&  (  ch  ==  '"'  )  )  { 
return  (  "&quot;"  )  ; 
}  else   if  (  xml  &&  (  ch  ==  '\''  )  )  { 
return  (  "&#39;"  )  ; 
}  else  { 
return  (  String  .  valueOf  (  ch  )  )  ; 
} 
} 










public   static   final   String   textToHTML  (  String   text  ,  boolean   xml  )  { 
if  (  text  ==  null  )  return  "null"  ; 
final   StringBuffer   html  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  text  .  length  (  )  ;  i  ++  )  { 
html  .  append  (  charToHTML  (  text  .  charAt  (  i  )  ,  xml  )  )  ; 
} 
return   html  .  toString  (  )  ; 
} 








public   static   final   String   textToHTML  (  String   text  )  { 
return   textToHTML  (  text  ,  false  )  ; 
} 








public   static   final   String   textToXML  (  String   text  )  { 
return   textToHTML  (  text  ,  true  )  ; 
} 






public   static   final   String   textToCDATA  (  String   text  )  { 
if  (  text  ==  null  )  return  "null"  ; 
final   StringBuffer   html  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  text  .  length  (  )  ;  i  ++  )  { 
html  .  append  (  charToCDATA  (  text  .  charAt  (  i  )  )  )  ; 
} 
return   html  .  toString  (  )  ; 
} 






public   static   final   String   charToCDATA  (  char   ch  )  { 
int   c  ; 
if  (  ch  >=  128  )  { 
c  =  ch  ; 
return  (  "&#"  +  c  +  ';'  )  ; 
}  else  { 
return  (  String  .  valueOf  (  ch  )  )  ; 
} 
} 






public   static   final   String   encode  (  String   s  )  { 
try  { 
if  (  s  !=  null  )  return   URLEncoder  .  encode  (  s  ,  "UTF-8"  )  ;  else   return   s  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   s  ; 
} 
} 






public   static   final   String   decode  (  String   s  )  { 
try  { 
if  (  s  !=  null  )  return   URLDecoder  .  decode  (  s  ,  "UTF-8"  )  ;  else   return   s  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   s  ; 
} 
} 





public   static   int   stringToInt  (  String   string  )  { 
try  { 
return   Integer  .  valueOf  (  string  )  .  intValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
mLogger  .  debug  (  "Invalid Integer:"  +  string  )  ; 
} 
return   0  ; 
} 




public   static   String   addNofollow  (  String   html  )  { 
if  (  html  ==  null  ||  html  .  length  (  )  ==  0  )  { 
return   html  ; 
} 
Matcher   m  =  mLinkPattern  .  matcher  (  html  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
while  (  m  .  find  (  )  )  { 
int   start  =  m  .  start  (  )  ; 
int   end  =  m  .  end  (  )  ; 
String   link  =  html  .  substring  (  start  ,  end  )  ; 
buf  .  append  (  html  .  substring  (  0  ,  start  )  )  ; 
if  (  link  .  indexOf  (  "rel=\"nofollow\""  )  ==  -  1  )  { 
buf  .  append  (  link  .  substring  (  0  ,  link  .  length  (  )  -  1  )  +  " rel=\"nofollow\">"  )  ; 
}  else  { 
buf  .  append  (  link  )  ; 
} 
html  =  html  .  substring  (  end  ,  html  .  length  (  )  )  ; 
m  =  mLinkPattern  .  matcher  (  html  )  ; 
} 
buf  .  append  (  html  )  ; 
return   buf  .  toString  (  )  ; 
} 









public   static   String   transformToHTMLSubset  (  String   s  )  { 
if  (  s  ==  null  )  { 
return   null  ; 
} 
s  =  replace  (  s  ,  OPENING_B_TAG_PATTERN  ,  "<b>"  )  ; 
s  =  replace  (  s  ,  CLOSING_B_TAG_PATTERN  ,  "</b>"  )  ; 
s  =  replace  (  s  ,  OPENING_I_TAG_PATTERN  ,  "<i>"  )  ; 
s  =  replace  (  s  ,  CLOSING_I_TAG_PATTERN  ,  "</i>"  )  ; 
s  =  replace  (  s  ,  OPENING_BLOCKQUOTE_TAG_PATTERN  ,  "<blockquote>"  )  ; 
s  =  replace  (  s  ,  CLOSING_BLOCKQUOTE_TAG_PATTERN  ,  "</blockquote>"  )  ; 
s  =  replace  (  s  ,  BR_TAG_PATTERN  ,  "<br />"  )  ; 
s  =  replace  (  s  ,  OPENING_P_TAG_PATTERN  ,  "<p>"  )  ; 
s  =  replace  (  s  ,  CLOSING_P_TAG_PATTERN  ,  "</p>"  )  ; 
s  =  replace  (  s  ,  OPENING_PRE_TAG_PATTERN  ,  "<pre>"  )  ; 
s  =  replace  (  s  ,  CLOSING_PRE_TAG_PATTERN  ,  "</pre>"  )  ; 
s  =  replace  (  s  ,  OPENING_UL_TAG_PATTERN  ,  "<ul>"  )  ; 
s  =  replace  (  s  ,  CLOSING_UL_TAG_PATTERN  ,  "</ul>"  )  ; 
s  =  replace  (  s  ,  OPENING_OL_TAG_PATTERN  ,  "<ol>"  )  ; 
s  =  replace  (  s  ,  CLOSING_OL_TAG_PATTERN  ,  "</ol>"  )  ; 
s  =  replace  (  s  ,  OPENING_LI_TAG_PATTERN  ,  "<li>"  )  ; 
s  =  replace  (  s  ,  CLOSING_LI_TAG_PATTERN  ,  "</li>"  )  ; 
s  =  replace  (  s  ,  QUOTE_PATTERN  ,  "\""  )  ; 
s  =  replace  (  s  ,  CLOSING_A_TAG_PATTERN  ,  "</a>"  )  ; 
Matcher   m  =  OPENING_A_TAG_PATTERN  .  matcher  (  s  )  ; 
while  (  m  .  find  (  )  )  { 
int   start  =  m  .  start  (  )  ; 
int   end  =  m  .  end  (  )  ; 
String   link  =  s  .  substring  (  start  ,  end  )  ; 
link  =  "<"  +  link  .  substring  (  4  ,  link  .  length  (  )  -  4  )  +  ">"  ; 
s  =  s  .  substring  (  0  ,  start  )  +  link  +  s  .  substring  (  end  ,  s  .  length  (  )  )  ; 
m  =  OPENING_A_TAG_PATTERN  .  matcher  (  s  )  ; 
} 
s  =  s  .  replaceAll  (  "&amp;lt;"  ,  "&lt;"  )  ; 
s  =  s  .  replaceAll  (  "&amp;gt;"  ,  "&gt;"  )  ; 
s  =  s  .  replaceAll  (  "&amp;#"  ,  "&#"  )  ; 
return   s  ; 
} 

private   static   String   replace  (  String   string  ,  Pattern   pattern  ,  String   replacement  )  { 
Matcher   m  =  pattern  .  matcher  (  string  )  ; 
return   m  .  replaceAll  (  replacement  )  ; 
} 




public   static   String   toBase64  (  byte  [  ]  aValue  )  { 
final   String   m_strBase64Chars  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"  ; 
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

