package   org  .  aitools  .  util  .  xml  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  CharacterIterator  ; 
import   java  .  text  .  StringCharacterIterator  ; 
import   java  .  util  .  Map  ; 







public   class   Characters  { 


private   static   final   String   SYSTEM_ENCODING  =  System  .  getProperty  (  "file.encoding"  ,  "UTF-8"  )  ; 

private   static   final   String   AMPERSAND  =  "&"  ; 

private   static   final   String   XML_AMPERSAND  =  "&amp;"  ; 

private   static   final   String   XML_AMPERSAND_REGEX  =  "&(amp|#0*38|#x0*26);"  ; 

private   static   final   String   LESS_THAN  =  "<"  ; 

private   static   final   String   XML_LESS_THAN  =  "&lt;"  ; 

private   static   final   String   XML_LESS_THAN_REGEX  =  "&(lt|#0*60|#x0*3[cC]);"  ; 

private   static   final   String   GREATER_THAN  =  ">"  ; 

private   static   final   String   XML_GREATER_THAN  =  "&gt;"  ; 

private   static   final   String   XML_GREATER_THAN_REGEX  =  "&(gt|#0*62|#x0*3[eE]);"  ; 

private   static   final   String   QUOTE  =  "\""  ; 

private   static   final   String   XML_QUOTE  =  "&quot;"  ; 

private   static   final   String   XML_QUOTE_REGEX  =  "&(quot|#0*34|#x0*22);"  ; 

private   static   final   String   APOSTROPHE  =  "'"  ; 

private   static   final   String   XML_APOSTROPHE  =  "&apos;"  ; 

private   static   final   String   XML_APOSTROPHE_REGEX  =  "&(apos|#0*39|#x0*27);"  ; 


















public   static   String   escapeXMLChars  (  String   input  )  { 
if  (  input  ==  null  )  { 
return  ""  ; 
} 
return   input  .  replace  (  AMPERSAND  ,  XML_AMPERSAND  )  .  replace  (  LESS_THAN  ,  XML_LESS_THAN  )  .  replace  (  GREATER_THAN  ,  XML_GREATER_THAN  )  .  replace  (  QUOTE  ,  XML_QUOTE  )  .  replace  (  APOSTROPHE  ,  XML_APOSTROPHE  )  ; 
} 










public   static   String   escapeXMLChars  (  char  [  ]  ch  ,  int   start  ,  int   length  )  { 
if  (  ch  ==  null  ||  length  <  1  ||  start  >=  ch  .  length  ||  start  <  0  ||  ch  .  length  ==  0  )  { 
return  ""  ; 
} 
StringBuilder   result  =  new   StringBuilder  (  length  )  ; 
int   end  =  start  +  length  ; 
for  (  int   index  =  start  ;  index  <  end  ;  index  ++  )  { 
char   cha  =  ch  [  index  ]  ; 
switch  (  cha  )  { 
case  '&'  : 
result  .  append  (  XML_AMPERSAND  )  ; 
break  ; 
case  '<'  : 
result  .  append  (  XML_LESS_THAN  )  ; 
break  ; 
case  '>'  : 
result  .  append  (  XML_GREATER_THAN  )  ; 
break  ; 
case  '"'  : 
result  .  append  (  XML_QUOTE  )  ; 
break  ; 
case  '\''  : 
result  .  append  (  XML_APOSTROPHE  )  ; 
break  ; 
default  : 
result  .  append  (  cha  )  ; 
} 
} 
return   result  .  toString  (  )  ; 
} 


















public   static   String   unescapeXMLChars  (  String   input  )  { 
return   input  .  replaceAll  (  XML_LESS_THAN_REGEX  ,  LESS_THAN  )  .  replaceAll  (  XML_GREATER_THAN_REGEX  ,  GREATER_THAN  )  .  replaceAll  (  XML_AMPERSAND_REGEX  ,  AMPERSAND  )  .  replaceAll  (  XML_QUOTE_REGEX  ,  QUOTE  )  .  replaceAll  (  XML_APOSTROPHE_REGEX  ,  APOSTROPHE  )  ; 
} 








public   static   String   filterXML  (  String   input  )  { 
if  (  input  ==  null  )  { 
return  ""  ; 
} 
String   _input  =  input  .  trim  (  )  ; 
if  (  _input  .  equals  (  (  ""  )  )  )  { 
return  ""  ; 
} 
StringBuilder   result  =  new   StringBuilder  (  _input  .  length  (  )  )  ; 
StringCharacterIterator   iterator  =  new   StringCharacterIterator  (  _input  )  ; 
for  (  char   aChar  =  iterator  .  first  (  )  ;  aChar  !=  CharacterIterator  .  DONE  ;  aChar  =  iterator  .  next  (  )  )  { 
if  (  (  aChar  ==  '	'  )  ||  (  aChar  ==  '\n'  )  ||  (  aChar  ==  '\r'  )  ||  (  (  ' '  <=  aChar  )  &&  (  aChar  <=  '퟿'  )  )  ||  (  (  ''  <=  aChar  )  &&  (  aChar  <=  '�'  )  )  )  { 
result  .  append  (  aChar  )  ; 
} 
} 
if  (  result  .  length  (  )  >  _input  .  length  (  )  )  { 
return   result  .  toString  (  )  ; 
} 
return   _input  ; 
} 














public   static   String   convertXMLUnicodeEntities  (  String   input  )  { 
int   inputLength  =  input  .  length  (  )  ; 
int   pointer  =  0  ; 
StringBuilder   result  =  new   StringBuilder  (  inputLength  )  ; 
while  (  pointer  <  input  .  length  (  )  )  { 
if  (  input  .  charAt  (  pointer  )  ==  '&'  )  { 
if  (  input  .  charAt  (  pointer  +  1  )  ==  '#'  )  { 
if  (  input  .  charAt  (  pointer  +  2  )  ==  'x'  )  { 
int   semicolon  =  input  .  indexOf  (  ';'  ,  pointer  +  3  )  ; 
if  (  semicolon  <  pointer  +  7  )  { 
try  { 
result  .  append  (  (  char  )  Integer  .  decode  (  input  .  substring  (  pointer  +  2  ,  semicolon  )  )  .  intValue  (  )  )  ; 
pointer  +=  (  semicolon  -  pointer  +  1  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
}  else  { 
int   semicolon  =  input  .  indexOf  (  ';'  ,  pointer  +  2  )  ; 
if  (  semicolon  <  pointer  +  7  )  { 
try  { 
result  .  append  (  (  char  )  Integer  .  parseInt  (  input  .  substring  (  pointer  +  2  ,  semicolon  )  )  )  ; 
pointer  +=  (  semicolon  -  pointer  +  1  )  ; 
continue  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
} 
} 
} 
result  .  append  (  input  .  charAt  (  pointer  )  )  ; 
pointer  ++  ; 
} 
return   result  .  toString  (  )  ; 
} 








public   static   String   getDeclaredXMLEncoding  (  URL   url  )  throws   IOException  { 
InputStream   stream  =  url  .  openStream  (  )  ; 
BufferedReader   buffReader  =  new   BufferedReader  (  new   InputStreamReader  (  stream  )  )  ; 
String   firstLine  =  buffReader  .  readLine  (  )  ; 
if  (  firstLine  ==  null  )  { 
return   SYSTEM_ENCODING  ; 
} 
int   piStart  =  firstLine  .  indexOf  (  "<?xml version=\"1.0\""  )  ; 
if  (  piStart  !=  -  1  )  { 
int   attributeStart  =  firstLine  .  indexOf  (  "encoding=\""  )  ; 
if  (  attributeStart  >=  0  )  { 
int   nextQuote  =  firstLine  .  indexOf  (  '"'  ,  attributeStart  +  10  )  ; 
if  (  nextQuote  >=  0  )  { 
String   encoding  =  firstLine  .  substring  (  attributeStart  +  10  ,  nextQuote  )  ; 
return   encoding  .  trim  (  )  ; 
} 
} 
} 
stream  .  close  (  )  ; 
return   SYSTEM_ENCODING  ; 
} 







public   static   String   removeMarkup  (  String   input  )  { 
if  (  input  ==  null  )  { 
return  ""  ; 
} 
String   _input  =  input  .  trim  (  )  ; 
if  (  _input  .  equals  (  ""  )  )  { 
return   _input  ; 
} 
int   tagStart  =  _input  .  indexOf  (  '<'  )  ; 
if  (  tagStart  ==  -  1  )  { 
return   _input  ; 
} 
int   tagEnd  =  0  ; 
int   lastEnd  =  0  ; 
int   inputLength  =  _input  .  length  (  )  ; 
StringBuilder   result  =  new   StringBuilder  (  )  ; 
while  (  (  tagStart  >  -  1  )  &&  (  tagEnd  >  -  1  )  )  { 
tagEnd  =  _input  .  indexOf  (  '>'  ,  lastEnd  )  ; 
if  (  tagStart  >  0  )  { 
result  .  append  (  _input  .  substring  (  lastEnd  ,  tagStart  )  )  ; 
} 
lastEnd  =  tagEnd  +  1  ; 
tagStart  =  _input  .  indexOf  (  '<'  ,  lastEnd  )  ; 
} 
if  (  (  lastEnd  <  inputLength  )  &&  (  lastEnd  >  0  )  )  { 
result  .  append  (  _input  .  substring  (  lastEnd  )  )  ; 
} 
return   result  .  toString  (  )  ; 
} 







public   static   String   renderAttributes  (  Map  <  String  ,  String  >  attributes  )  { 
StringBuilder   result  =  new   StringBuilder  (  )  ; 
if  (  attributes  !=  null  )  { 
for  (  Map  .  Entry  <  String  ,  String  >  attribute  :  attributes  .  entrySet  (  )  )  { 
String   attributeName  =  attribute  .  getKey  (  )  ; 
if  (  attributeName  !=  null  &&  !  "xmlns"  .  equals  (  attributeName  )  )  { 
result  .  append  (  String  .  format  (  " %s=\"%s\""  ,  attributeName  ,  attribute  .  getValue  (  )  )  )  ; 
} 
} 
} 
return   result  .  toString  (  )  ; 
} 
} 

