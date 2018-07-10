package   com  .  simonepezzano  .  hshare  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  Inet4Address  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  NetworkInterface  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  StringTokenizer  ; 
import   org  .  dom4j  .  Attribute  ; 
import   org  .  dom4j  .  Document  ; 
import   org  .  dom4j  .  Element  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Point  ; 
import   org  .  eclipse  .  ui  .  PlatformUI  ; 
import   org  .  mortbay  .  jetty  .  Server  ; 
import   org  .  mortbay  .  jetty  .  servlet  .  Context  ; 
import   org  .  mortbay  .  jetty  .  servlet  .  ServletHolder  ; 
import   com  .  simonepezzano  .  hshare  .  servlets  .  DFile  ; 
import   com  .  simonepezzano  .  hshare  .  servlets  .  GetResource  ; 
import   com  .  simonepezzano  .  hshare  .  servlets  .  ListFiles  ; 
import   com  .  simonepezzano  .  hshare  .  servlets  .  Templater  ; 






public   class   Statics  { 

private   static   Server   server  =  null  ; 

private   static   DecimalFormat   twoDec  =  new   DecimalFormat  (  "#0.00"  )  ; 

public   static   final   String   DIRECTORY  =  "dir"  ; 

public   static   final   String   FILE  =  "file"  ; 




public   static   Server   getServerInstance  (  )  { 
if  (  server  ==  null  )  { 
server  =  new   Server  (  Integer  .  valueOf  (  Conf  .  getInstance  (  )  .  getPort  (  )  )  )  ; 
Context   root  =  new   Context  (  server  ,  "/"  ,  Context  .  SESSIONS  )  ; 
root  .  addServlet  (  new   ServletHolder  (  new   ListFiles  (  )  )  ,  "/*"  )  ; 
root  .  addServlet  (  new   ServletHolder  (  new   DFile  (  )  )  ,  "/Download/*"  )  ; 
root  .  addServlet  (  new   ServletHolder  (  new   GetResource  (  )  )  ,  "/Resources/*"  )  ; 
try  { 
fillBaseTemplates  (  )  ; 
}  catch  (  IOException   e  )  { 
HLog  .  iologger  .  fatal  (  "Cannot copy resources from jar to resources directory"  ,  e  )  ; 
} 
} 
return   server  ; 
} 







public   static   String   MD5  (  String   input  )  throws   Exception  { 
MessageDigest   m  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
m  .  update  (  input  .  getBytes  (  )  ,  0  ,  input  .  length  (  )  )  ; 
input  =  new   BigInteger  (  1  ,  m  .  digest  (  )  )  .  toString  (  16  )  ; 
if  (  input  .  length  (  )  ==  31  )  input  =  "0"  +  input  ; 
return   input  ; 
} 






public   static   String   getSize  (  File   file  )  { 
long   len  =  file  .  length  (  )  ; 
double   kb  =  len  /  1024.0  ; 
double   mb  =  kb  /  1024.0  ; 
if  (  mb  >  0.5  )  return   twoDec  .  format  (  mb  )  .  toString  (  )  +  " MB"  ; 
return   twoDec  .  format  (  kb  )  .  toString  (  )  +  " KB"  ; 
} 






public   static   boolean   checkDir  (  File   dir  )  { 
if  (  !  dir  .  exists  (  )  )  return   dir  .  mkdir  (  )  ; 
return   false  ; 
} 





public   static   boolean   checkResDir  (  )  { 
return   checkDir  (  Conf  .  getInstance  (  )  .  getResDir  (  )  )  ; 
} 





public   static   boolean   checkDirTemplatesDir  (  )  { 
return   checkDir  (  Conf  .  getInstance  (  )  .  getDirTemplates  (  )  )  ; 
} 




public   static   boolean   areTemplatesReady  (  )  { 
return   Templater  .  headFile  (  )  .  exists  (  )  ; 
} 





public   static   void   fillBaseTemplates  (  )  throws   IOException  { 
if  (  areTemplatesReady  (  )  )  return  ; 
copyJarFileToFile  (  Templater  .  headFile  (  )  ,  "/w_res/head.htm.templ"  )  ; 
copyJarFileToFile  (  Templater  .  bottomFile  (  )  ,  "/w_res/bottom.htm.templ"  )  ; 
copyJarFileToFile  (  Templater  .  dirFile  (  )  ,  "/w_res/dir.htm.templ"  )  ; 
copyJarFileToFile  (  Templater  .  fileFile  (  )  ,  "/w_res/file.htm.templ"  )  ; 
copyJarFileToFile  (  Conf  .  getInstance  (  )  .  styleFile  (  )  ,  "/w_res/resources/style.css"  )  ; 
copyJarFileToFile  (  Conf  .  getInstance  (  )  .  logoFile  (  )  ,  "/w_res/resources/logo.png"  )  ; 
} 





public   static   String   getExt  (  String   filename  )  { 
int   index  =  filename  .  lastIndexOf  (  '.'  )  ; 
if  (  index  >  0  )  return   filename  .  substring  (  index  +  1  )  ; 
return  ""  ; 
} 







public   static   boolean   isA  (  Element   el  ,  String   type  )  { 
return   el  .  getName  (  )  .  equals  (  type  )  ; 
} 





public   static   boolean   isAFile  (  Element   el  )  { 
return   isA  (  el  ,  FILE  )  ; 
} 





public   static   boolean   isADir  (  Element   el  )  { 
return   isA  (  el  ,  DIRECTORY  )  ; 
} 








public   static   String   attributeValue  (  Element   el  ,  String   attrName  ,  String   def  )  { 
Attribute   a  =  el  .  attribute  (  attrName  )  ; 
if  (  a  ==  null  )  return   def  ;  else   return   a  .  getStringValue  (  )  ; 
} 







public   static   String   attributeValue  (  Element   el  ,  String   attrName  )  { 
return   attributeValue  (  el  ,  attrName  ,  ""  )  ; 
} 






public   static   String   toUTF8  (  String   string  )  { 
try  { 
return   URLEncoder  .  encode  (  string  ,  "utf-8"  )  .  replace  (  "+"  ,  "%20"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   null  ; 
} 
} 







public   static   URI   toURI  (  String   path  ,  String   filename  )  { 
return   new   File  (  path  )  .  toURI  (  )  .  resolve  (  toUTF8  (  filename  )  )  ; 
} 







public   static   void   printToStream  (  InputStream   is  ,  OutputStream   os  )  throws   IOException  { 
byte  [  ]  buff  =  new   byte  [  4096  ]  ; 
int   len  =  0  ; 
while  (  (  len  =  is  .  read  (  buff  )  )  !=  -  1  )  os  .  write  (  buff  ,  0  ,  len  )  ; 
is  .  close  (  )  ; 
} 







public   static   void   printToFile  (  File   dest  ,  String   text  )  throws   IOException  { 
FileWriter   fw  =  new   FileWriter  (  dest  )  ; 
fw  .  write  (  text  )  ; 
fw  .  close  (  )  ; 
} 







public   static   void   printToFile  (  File   dest  ,  byte  [  ]  data  )  throws   IOException  { 
FileOutputStream   fos  =  new   FileOutputStream  (  dest  )  ; 
fos  .  write  (  data  )  ; 
} 






public   static   String   getLocalIP  (  )  throws   SocketException  { 
Enumeration  <  NetworkInterface  >  interfaces  =  NetworkInterface  .  getNetworkInterfaces  (  )  ; 
String   res  =  ""  ; 
while  (  interfaces  .  hasMoreElements  (  )  )  { 
NetworkInterface   ni  =  interfaces  .  nextElement  (  )  ; 
Enumeration  <  InetAddress  >  addrs  =  ni  .  getInetAddresses  (  )  ; 
while  (  addrs  .  hasMoreElements  (  )  )  { 
InetAddress   address  =  addrs  .  nextElement  (  )  ; 
if  (  address   instanceof   Inet4Address  )  res  +=  " "  +  address  .  getHostAddress  (  )  ; 
} 
if  (  interfaces  .  hasMoreElements  (  )  )  res  +=  " -"  ; 
} 
return   res  ; 
} 






public   static   String   getRemoteIP  (  )  throws   IOException  { 
URL   url  =  new   URL  (  Conf  .  getInstance  (  )  .  getExternalIpServiceURL  (  )  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
HLog  .  netlogger  .  debug  (  "fetching remote IP"  )  ; 
String   res  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  .  readLine  (  )  ; 
conn  .  disconnect  (  )  ; 
return   res  ; 
} 




public   static   Point   getAppCenter  (  )  { 
return   new   Point  (  PlatformUI  .  getWorkbench  (  )  .  getDisplay  (  )  .  getClientArea  (  )  .  width  /  2  ,  PlatformUI  .  getWorkbench  (  )  .  getDisplay  (  )  .  getClientArea  (  )  .  height  /  2  )  ; 
} 







public   static   Point   getOriginForCenter  (  int   width  ,  int   height  )  { 
Point   center  =  getAppCenter  (  )  ; 
return   new   Point  (  center  .  x  -  (  width  /  2  )  ,  center  .  y  -  (  height  /  2  )  )  ; 
} 






public   static   String   getStringContentFromJar  (  String   s  )  { 
String   thisLine  =  null  ; 
String   res  =  ""  ; 
try  { 
InputStream   is  =  Statics  .  class  .  getResourceAsStream  (  s  )  ; 
BufferedReader   br  =  new   BufferedReader  (  new   InputStreamReader  (  is  )  )  ; 
while  (  (  thisLine  =  br  .  readLine  (  )  )  !=  null  )  res  +=  thisLine  +  '\n'  ; 
}  catch  (  Exception   e  )  { 
return  ""  ; 
} 
return   res  ; 
} 







public   static   void   copyJarFileToFile  (  File   dest  ,  String   filename  )  throws   IOException  { 
CopyThread   ct  =  new   CopyThread  (  dest  ,  filename  )  ; 
ct  .  start  (  )  ; 
} 







public   static   byte  [  ]  getBytesContentFromJar  (  String   s  )  throws   IOException  { 
InputStream   is  =  Statics  .  class  .  getResourceAsStream  (  s  )  ; 
ByteArrayOutputStream   bab  =  new   ByteArrayOutputStream  (  )  ; 
byte  [  ]  buff  =  new   byte  [  4096  ]  ; 
int   len  =  0  ; 
while  (  (  len  =  is  .  read  (  buff  )  )  !=  -  1  )  bab  .  write  (  buff  ,  0  ,  len  )  ; 
return   bab  .  toByteArray  (  )  ; 
} 







public   static   void   manageAttribute  (  Element   element  ,  String   attributeName  ,  String   value  )  { 
Attribute   attr  =  element  .  attribute  (  attributeName  )  ; 
if  (  value  ==  null  ||  value  .  length  (  )  ==  0  )  { 
if  (  attr  !=  null  )  element  .  remove  (  attr  )  ; 
}  else   if  (  attr  ==  null  )  element  .  addAttribute  (  attributeName  ,  value  )  ;  else   attr  .  setText  (  value  )  ; 
} 






public   static   LinkedList  <  String  >  splitUsers  (  String   users  )  { 
LinkedList  <  String  >  usrs  =  new   LinkedList  <  String  >  (  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  users  ,  ","  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
usrs  .  add  (  st  .  nextToken  (  )  .  trim  (  )  )  ; 
} 
return   usrs  ; 
} 









public   static   boolean   verifyUser  (  Element   dir  ,  String   username  ,  String   password  )  { 
try  { 
Attribute   users  =  dir  .  attribute  (  "users"  )  ; 
if  (  users  ==  null  ||  users  .  getStringValue  (  )  .  length  (  )  ==  0  )  return   true  ; 
LinkedList  <  String  >  usrs  =  splitUsers  (  users  .  getStringValue  (  )  .  trim  (  )  )  ; 
if  (  usrs  .  size  (  )  ==  0  ||  !  usrs  .  contains  (  username  )  )  return   false  ; 
Document   udoc  =  new   HUsers  (  )  .  getDocument  (  )  ; 
Element   user  =  (  Element  )  udoc  .  selectSingleNode  (  "//user[@username='"  +  username  +  "']"  )  ; 
if  (  user  ==  null  )  return   false  ; 
return   password  .  equals  (  user  .  attributeValue  (  "password"  )  )  ; 
}  catch  (  Exception   e  )  { 
HLog  .  doclogger  .  error  (  "Could not read users file properly"  ,  e  )  ; 
return   false  ; 
} 
} 
} 






class   CopyThread   extends   Thread  { 

private   File   dest  ; 

private   String   filename  ; 






public   CopyThread  (  File   dest  ,  String   filename  )  { 
this  .  dest  =  dest  ; 
this  .  filename  =  filename  ; 
} 

public   void   run  (  )  { 
try  { 
InputStream   is  =  Statics  .  class  .  getResourceAsStream  (  filename  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  dest  )  ; 
Statics  .  printToStream  (  is  ,  fos  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 

