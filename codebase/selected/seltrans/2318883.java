package   com  .  astromine  .  base  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 






public   class   FileHelper  { 









public   static   void   copyURLToFile  (  String   srcURL  ,  String   filename  )  throws   MalformedURLException  ,  IOException  { 
URL   url  =  new   URL  (  srcURL  )  ; 
copyURLToFile  (  url  ,  filename  )  ; 
} 









public   static   void   copyURLToFile  (  URL   url  ,  String   filename  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  1  ]  ; 
InputStream   in  =  url  .  openStream  (  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  filename  )  ; 
while  (  (  in  .  read  (  buf  ,  0  ,  1  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  1  )  ; 
} 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 









public   static   void   copyBufferToFile  (  byte  [  ]  data  ,  String   filename  )  throws   IOException  { 
FileOutputStream   out  =  new   FileOutputStream  (  filename  )  ; 
out  .  write  (  data  )  ; 
out  .  close  (  )  ; 
} 






public   static   Map  <  String  ,  List  <  String  >  >  getURLHeadersForMP3Server  (  URL   url  )  { 
Map  <  String  ,  List  <  String  >  >  header  =  null  ; 
try  { 
URLConnection   connection  =  (  URLConnection  )  url  .  openConnection  (  )  ; 
connection  .  setRequestProperty  (  "Icy-MetaData"  ,  "1"  )  ; 
connection  .  setRequestProperty  (  "User-Agent"  ,  "Winamp/5.52"  )  ; 
connection  .  setRequestProperty  (  "Accept"  ,  "*/*"  )  ; 
connection  .  setRequestProperty  (  "Connection"  ,  "close"  )  ; 
connection  .  setConnectTimeout  (  7000  )  ; 
connection  .  connect  (  )  ; 
header  =  connection  .  getHeaderFields  (  )  ; 
if  (  header  .  get  (  "Server"  )  ==  null  ||  connection  .  getContentType  (  )  .  equalsIgnoreCase  (  "unknown/unknown"  )  )  { 
Log  .  writeToStdout  (  Log  .  WARNING  ,  "FileHelper"  ,  "getURLHeaderForMP3Server"  ,  "Header not found in "  +  url  .  toString  (  )  +  " Retry Force"  )  ; 
header  =  new   Hashtable  <  String  ,  List  <  String  >  >  (  )  ; 
String   inputLine  ; 
int   lineNumber  =  0  ; 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
while  (  (  inputLine  =  in  .  readLine  (  )  )  !=  null  &&  lineNumber  <  15  )  { 
lineNumber  ++  ; 
if  (  inputLine  .  length  (  )  ==  0  )  { 
break  ; 
} 
List  <  String  >  values  =  null  ; 
String   key  =  ""  ; 
String   value  =  ""  ; 
int   location  =  inputLine  .  indexOf  (  ":"  )  ; 
if  (  location  >  0  )  { 
key  =  inputLine  .  substring  (  0  ,  location  )  ; 
if  (  key  .  trim  (  )  .  equalsIgnoreCase  (  "content-type"  )  )  { 
key  =  "Content-type"  ; 
} 
location  ++  ; 
}  else  { 
location  =  0  ; 
} 
if  (  inputLine  .  length  (  )  >  location  )  { 
value  =  inputLine  .  substring  (  location  )  .  trim  (  )  ; 
} 
values  =  header  .  get  (  key  )  ; 
if  (  values  !=  null  &&  value  !=  null  )  { 
values  .  add  (  value  )  ; 
}  else   if  (  values  ==  null  &&  value  !=  null  )  { 
values  =  new   ArrayList  <  String  >  (  )  ; 
values  .  add  (  value  )  ; 
} 
header  .  put  (  key  ,  values  )  ; 
} 
in  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
Log  .  writeToStdout  (  Log  .  WARNING  ,  "FileHelper"  ,  "getURLHeaderForMP3Server"  ,  e  .  getClass  (  )  +  " "  +  e  .  getMessage  (  )  )  ; 
} 
return   header  ; 
} 






public   static   Map  <  String  ,  List  <  String  >  >  getCurrentSongForMP3Stream  (  URL   url  )  { 
Map  <  String  ,  List  <  String  >  >  header  =  null  ; 
HashMap  <  String  ,  List  <  String  >  >  meta  =  new   HashMap  <  String  ,  List  <  String  >  >  (  )  ; 
int   maxLen  =  24000  ; 
byte  [  ]  data  =  null  ; 
String   song  =  "Unknown - Unknown"  ; 
try  { 
URLConnection   connection  =  (  URLConnection  )  url  .  openConnection  (  )  ; 
connection  .  setRequestProperty  (  "Icy-MetaData"  ,  "1"  )  ; 
connection  .  setRequestProperty  (  "Accept"  ,  "*/*"  )  ; 
connection  .  setRequestProperty  (  "User-Agent"  ,  "Winamp/5.52"  )  ; 
connection  .  setRequestProperty  (  "Connection"  ,  "close"  )  ; 
connection  .  setConnectTimeout  (  7000  )  ; 
connection  .  connect  (  )  ; 
header  =  connection  .  getHeaderFields  (  )  ; 
meta  .  putAll  (  header  )  ; 
if  (  header  .  get  (  "icy-metaint"  )  !=  null  )  { 
maxLen  =  ValueHelper  .  asInt  (  header  .  get  (  "icy-metaint"  )  .  get  (  0  )  )  +  512  ; 
} 
data  =  FileHelper  .  loadURLToBuffer  (  connection  ,  maxLen  )  ; 
String   sData  =  new   String  (  data  )  ; 
int   begin  =  sData  .  indexOf  (  "StreamTitle"  )  ; 
int   end  =  sData  .  indexOf  (  ";"  ,  begin  )  ; 
if  (  begin  >=  0  &&  end  >=  0  )  { 
song  =  sData  .  substring  (  begin  +  13  ,  end  -  1  )  ; 
} 
ArrayList  <  String  >  streamTitle  =  new   ArrayList  <  String  >  (  )  ; 
streamTitle  .  add  (  song  )  ; 
meta  .  put  (  "StreamTitle"  ,  streamTitle  )  ; 
}  catch  (  IOException   e  )  { 
Log  .  writeToStdout  (  Log  .  WARNING  ,  "FileHelper"  ,  "getCurrentSongForMP3Server"  ,  e  .  getClass  (  )  +  " "  +  e  .  getMessage  (  )  )  ; 
}  catch  (  Exception   e  )  { 
Log  .  writeToStdout  (  Log  .  WARNING  ,  "FileHelper"  ,  "getCurrentSongForMP3Server"  ,  e  .  getClass  (  )  +  " "  +  e  .  getMessage  (  )  )  ; 
} 
return   meta  ; 
} 







public   static   InputStream   getResourceAsStream  (  String   fileName  )  { 
InputStream   in  =  null  ; 
try  { 
ClassLoader   cldr  =  FileHelper  .  class  .  getClassLoader  (  )  ; 
in  =  cldr  .  getResourceAsStream  (  fileName  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   in  ; 
} 








public   static   byte  [  ]  loadFileToBuffer  (  String   srcFileName  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
byte  [  ]  data  =  null  ; 
byte  [  ]  temp  =  null  ; 
int   iCount  =  0  ; 
int   iTotal  =  0  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   FileInputStream  (  srcFileName  )  ,  20480  )  ; 
while  (  (  iCount  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  )  { 
if  (  iTotal  ==  0  )  { 
data  =  new   byte  [  iCount  ]  ; 
System  .  arraycopy  (  buf  ,  0  ,  data  ,  0  ,  iCount  )  ; 
iTotal  =  iCount  ; 
}  else  { 
temp  =  new   byte  [  iCount  +  iTotal  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  temp  ,  0  ,  iTotal  )  ; 
System  .  arraycopy  (  buf  ,  0  ,  temp  ,  iTotal  ,  iCount  )  ; 
data  =  temp  ; 
iTotal  =  iTotal  +  iCount  ; 
} 
} 
in  .  close  (  )  ; 
return   data  ; 
} 








public   static   Properties   loadProperties  (  String   propertyFileName  )  { 
Properties   theProps  =  null  ; 
try  { 
FileInputStream   inStream  =  new   FileInputStream  (  propertyFileName  )  ; 
theProps  =  new   Properties  (  )  ; 
theProps  .  load  (  inStream  )  ; 
inStream  .  close  (  )  ; 
}  catch  (  FileNotFoundException   fnfe  )  { 
fnfe  .  printStackTrace  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   theProps  ; 
} 








public   static   Properties   loadPropertiesFromResource  (  String   propertyFileName  )  { 
Properties   theProps  =  null  ; 
try  { 
InputStream   in  =  getResourceAsStream  (  propertyFileName  )  ; 
theProps  =  new   Properties  (  )  ; 
theProps  .  load  (  in  )  ; 
in  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
theProps  =  null  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   theProps  ; 
} 








public   static   byte  [  ]  loadInputStreamToBuffer  (  InputStream   stream  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
byte  [  ]  data  =  null  ; 
byte  [  ]  temp  =  null  ; 
int   iCount  =  0  ; 
int   iTotal  =  0  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  stream  ,  20480  )  ; 
while  (  (  iCount  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  )  { 
if  (  iTotal  ==  0  )  { 
data  =  new   byte  [  iCount  ]  ; 
System  .  arraycopy  (  buf  ,  0  ,  data  ,  0  ,  iCount  )  ; 
iTotal  =  iCount  ; 
}  else  { 
temp  =  new   byte  [  iCount  +  iTotal  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  temp  ,  0  ,  iTotal  )  ; 
System  .  arraycopy  (  buf  ,  0  ,  temp  ,  iTotal  ,  iCount  )  ; 
data  =  temp  ; 
iTotal  =  iTotal  +  iCount  ; 
} 
} 
in  .  close  (  )  ; 
return   data  ; 
} 










public   static   byte  [  ]  loadURLToBuffer  (  URL   url  ,  int   maxLength  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  maxLength  ]  ; 
byte  [  ]  data  =  null  ; 
int   iCount  =  0  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  url  .  openStream  (  )  ,  20480  )  ; 
iCount  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  ; 
if  (  iCount  !=  -  1  )  { 
data  =  new   byte  [  iCount  ]  ; 
System  .  arraycopy  (  buf  ,  0  ,  data  ,  0  ,  iCount  )  ; 
} 
in  .  close  (  )  ; 
return   data  ; 
} 








public   static   byte  [  ]  loadURLToBuffer  (  URL   url  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
byte  [  ]  data  =  null  ; 
byte  [  ]  temp  =  null  ; 
int   iCount  =  0  ; 
int   iTotal  =  0  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  url  .  openStream  (  )  ,  20480  )  ; 
while  (  (  iCount  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  )  { 
if  (  iTotal  ==  0  )  { 
data  =  new   byte  [  iCount  ]  ; 
System  .  arraycopy  (  buf  ,  0  ,  data  ,  0  ,  iCount  )  ; 
iTotal  =  iCount  ; 
}  else  { 
temp  =  new   byte  [  iCount  +  iTotal  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  temp  ,  0  ,  iTotal  )  ; 
System  .  arraycopy  (  buf  ,  0  ,  temp  ,  iTotal  ,  iCount  )  ; 
data  =  temp  ; 
iTotal  =  iTotal  +  iCount  ; 
} 
} 
in  .  close  (  )  ; 
return   data  ; 
} 









public   static   byte  [  ]  loadURLToBuffer  (  URLConnection   connection  ,  int   maxLength  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
byte  [  ]  data  =  null  ; 
byte  [  ]  temp  =  null  ; 
int   iCount  =  0  ; 
int   iTotal  =  0  ; 
connection  .  connect  (  )  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  connection  .  getInputStream  (  )  ,  20480  )  ; 
while  (  (  iCount  =  in  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  &&  iTotal  <  maxLength  )  { 
if  (  iTotal  ==  0  )  { 
data  =  new   byte  [  iCount  ]  ; 
System  .  arraycopy  (  buf  ,  0  ,  data  ,  0  ,  iCount  )  ; 
iTotal  =  iCount  ; 
}  else  { 
temp  =  new   byte  [  iCount  +  iTotal  ]  ; 
System  .  arraycopy  (  data  ,  0  ,  temp  ,  0  ,  iTotal  )  ; 
System  .  arraycopy  (  buf  ,  0  ,  temp  ,  iTotal  ,  iCount  )  ; 
data  =  temp  ; 
iTotal  =  iTotal  +  iCount  ; 
} 
} 
in  .  close  (  )  ; 
return   data  ; 
} 









public   static   ArrayList  <  String  >  loadURLToStrings  (  URLConnection   connection  ,  int   maxLines  )  throws   IOException  { 
String   inputLine  ; 
ArrayList  <  String  >  lines  =  new   ArrayList  <  String  >  (  )  ; 
connection  .  connect  (  )  ; 
int   lineNumber  =  0  ; 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
while  (  (  inputLine  =  in  .  readLine  (  )  )  !=  null  &&  (  lineNumber  <  maxLines  ||  maxLines  ==  -  1  )  )  { 
lines  .  add  (  inputLine  )  ; 
lineNumber  ++  ; 
} 
in  .  close  (  )  ; 
return   lines  ; 
} 









public   static   ArrayList  <  String  >  loadURLToStrings  (  URL   url  )  throws   IOException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
return   loadURLToStrings  (  connection  ,  -  1  )  ; 
} 









public   static   ArrayList  <  String  >  loadURLToStrings  (  URL   url  ,  int   maxLines  ,  String   userAgent  ,  int   timeout  )  throws   IOException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
if  (  userAgent  !=  null  &&  userAgent  .  trim  (  )  .  length  (  )  >  0  )  { 
connection  .  setRequestProperty  (  "User-Agent"  ,  userAgent  )  ; 
}  else  { 
connection  .  setRequestProperty  (  "User-Agent"  ,  "Mozilla/5.0 (compatible; astrominer/1.0;)"  )  ; 
} 
if  (  timeout  >  0  )  { 
connection  .  setConnectTimeout  (  timeout  )  ; 
} 
connection  .  connect  (  )  ; 
return   loadURLToStrings  (  connection  ,  maxLines  )  ; 
} 




public   FileHelper  (  )  { 
super  (  )  ; 
} 
} 

