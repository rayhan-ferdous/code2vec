package   com  .  gregor  .  taglibs  .  rrd  ; 

import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Font  ; 
import   java  .  awt  .  Frame  ; 
import   java  .  awt  .  Graphics2D  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  SecureRandom  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  SortedMap  ; 
import   java  .  util  .  TreeMap  ; 
import   javax  .  servlet  .  ServletContext  ; 
import   javax  .  servlet  .  ServletException  ; 
import   javax  .  servlet  .  ServletOutputStream  ; 
import   javax  .  servlet  .  http  .  HttpServlet  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   com  .  gregor  .  rrd  .  RRDTool  ; 
import   com  .  gregor  .  rrd  .  RRDException  ; 
import   com  .  keypoint  .  PngEncoderB  ; 









public   class   GraphServlet   extends   HttpServlet  { 

private   static   final   String   s_initparameter_base  =  "com.gregor.rrd.GraphServlet"  ; 

private   static   final   long   s_expire_default  =  1000  *  60  *  10  ; 

private   static   final   String   s_rrdclass_default  =  "com.gregor.rrd.RRDPipe"  ; 

private   static   final   String   s_securerandom_algorithm_default  =  "SHA1PRNG"  ; 

private   static   final   String   s_messagedigest_algorithm_default  =  "SHA"  ; 

private   String   m_rrdclass  =  null  ; 

private   String   m_properties  =  null  ; 

private   String   m_securerandom_algorithm  =  null  ; 

private   String   m_messagedigest_algorithm  =  null  ; 

private   long   m_expire  =  0  ; 

private   RRDTool   m_rrd  =  null  ; 

private   SecureRandom   m_random  =  null  ; 

private   SortedMap   m_map  =  null  ; 











public   void   init  (  )  throws   ServletException  { 
if  (  (  m_rrdclass  =  getInitParameter  (  s_initparameter_base  +  ".rrdclass"  )  )  ==  null  )  { 
m_rrdclass  =  s_rrdclass_default  ; 
} 
m_properties  =  getInitParameter  (  s_initparameter_base  +  ".rrdproperties"  )  ; 
if  (  (  m_messagedigest_algorithm  =  getInitParameter  (  s_initparameter_base  +  ".messagedigest_algorithm"  )  )  ==  null  )  { 
m_messagedigest_algorithm  =  s_messagedigest_algorithm_default  ; 
} 
if  (  (  m_securerandom_algorithm  =  getInitParameter  (  s_initparameter_base  +  ".securerandom_algorithm"  )  )  ==  null  )  { 
m_securerandom_algorithm  =  s_securerandom_algorithm_default  ; 
} 
String   expire  =  getInitParameter  (  s_initparameter_base  +  ".expire"  )  ; 
if  (  expire  ==  null  )  { 
m_expire  =  s_expire_default  ; 
}  else  { 
try  { 
m_expire  =  Long  .  parseLong  (  expire  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   ServletException  (  "NumberFormatException while "  +  "attempting to parse value of "  +  s_initparameter_base  +  ".expire"  +  ": \""  +  expire  +  "\": "  +  e  .  toString  (  )  )  ; 
} 
} 
try  { 
m_rrd  =  RRDTool  .  newInstanceOf  (  m_rrdclass  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
throw   new   ServletException  (  "ClassNotFoundException while trying "  +  "to create newInstanceOf(\""  +  m_rrdclass  +  "\"): "  +  e  .  toString  (  )  )  ; 
}  catch  (  InstantiationException   e  )  { 
throw   new   ServletException  (  "InstantiationException while trying "  +  "to create newInstanceOf(\""  +  m_rrdclass  +  "\"): "  +  e  .  toString  (  )  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
throw   new   ServletException  (  "IllegalAccessException while trying "  +  "to create newInstanceOf(\""  +  m_rrdclass  +  "\"): "  +  e  .  toString  (  )  )  ; 
} 
if  (  m_properties  !=  null  )  { 
try  { 
m_rrd  .  setProperties  (  m_properties  )  ; 
}  catch  (  RRDException   e  )  { 
throw   new   ServletException  (  "RRDException while attempting "  +  "setProperties(\""  +  m_properties  +  "\"): "  +  e  .  toString  (  )  )  ; 
} 
} 
try  { 
m_random  =  SecureRandom  .  getInstance  (  m_securerandom_algorithm  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   ServletException  (  "NoSuchAlgorithmException while "  +  "attempting to get instance of \""  +  m_securerandom_algorithm  +  "\": "  +  e  )  ; 
} 
createCommandMap  (  )  ; 
} 





public   void   destroy  (  )  { 
try  { 
m_rrd  .  deInit  (  )  ; 
}  catch  (  IOException   e  )  { 
}  catch  (  RRDException   e  )  { 
} 
} 










public   void   doGet  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
String   command  =  null  ; 
if  (  request  .  getAttribute  (  "showmap"  )  !=  null  )  { 
returnShowMap  (  request  ,  response  )  ; 
return  ; 
} 
if  (  (  command  =  (  String  )  request  .  getAttribute  (  "command"  )  )  !=  null  )  { 
PrintWriter   out  =  response  .  getWriter  (  )  ; 
if  (  request  .  getAttribute  (  "printonly"  )  !=  null  )  { 
String  [  ]  printValues  =  printCommand  (  request  ,  response  ,  command  ,  true  )  ; 
request  .  setAttribute  (  "printvalues"  ,  printValues  )  ; 
}  else  { 
out  .  println  (  graphCommand  (  request  ,  response  ,  command  ,  (  String  )  request  .  getAttribute  (  "name"  )  )  )  ; 
} 
out  .  flush  (  )  ; 
return  ; 
} 
long   startTime  =  System  .  currentTimeMillis  (  )  ; 
response  .  setHeader  (  "Cache-Control"  ,  "no-store, private"  )  ; 
response  .  setDateHeader  (  "Date"  ,  startTime  )  ; 
response  .  setDateHeader  (  "Expires"  ,  startTime  )  ; 
String   p  ; 
if  (  (  p  =  request  .  getParameter  (  "id"  )  )  !=  null  )  { 
returnGraphOnDemand  (  request  ,  response  ,  p  )  ; 
}  else   if  (  (  p  =  request  .  getParameter  (  "name"  )  )  !=  null  )  { 
returnGraphByName  (  request  ,  response  ,  p  )  ; 
}  else  { 
graphError  (  request  ,  response  ,  "Request missing information"  ,  "The request is missing information "  +  "indicating which data to return.  "  +  "The parameters \"id\" or \"name\" must be set."  ,  (  request  .  getParameter  (  "debug"  )  !=  null  )  )  ; 
} 
} 















protected   String   graphCommand  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   command  )  throws   ServletException  { 
String   id  =  insertCommand  (  command  )  ; 
return  "<IMG SRC=\""  +  response  .  encodeURL  (  selfURL  (  request  )  +  "?id="  +  id  )  +  "\">"  ; 
} 





















protected   String   graphCommand  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   command  ,  String   name  )  throws   ServletException  { 
if  (  name  ==  null  )  { 
return   graphCommand  (  request  ,  response  ,  command  )  ; 
} 
String   ret  [  ]  ; 
int   sizes  [  ]  ; 
String   path  =  getGraphPath  (  name  )  ; 
try  { 
ret  =  m_rrd  .  graph  (  path  ,  command  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ServletException  (  "IO exception while working with "  +  "RRD: "  +  e  )  ; 
}  catch  (  RRDException   e  )  { 
return  "[Error from rrdtool: "  +  e  .  getMessage  (  )  +  "]"  ; 
} 
try  { 
sizes  =  m_rrd  .  parseGraphSize  (  ret  [  0  ]  )  ; 
}  catch  (  RRDException   e  )  { 
return  "[Error parsing graph sizes: "  +  e  .  getMessage  (  )  +  "]"  ; 
} 
String   file_encoded  ; 
try  { 
file_encoded  =  URLEncoder  .  encode  (  name  ,  "US-ASCII"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
throw   new   ServletException  (  "Could not create a URLEncoder for"  +  "US-ASCII: "  +  e  )  ; 
} 
return  "<IMG SRC=\""  +  response  .  encodeURL  (  selfURL  (  request  )  +  "?name="  +  file_encoded  )  +  "\"/>"  ; 
} 

protected   String  [  ]  printCommand  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   command  ,  boolean   printOnly  )  throws   ServletException  { 
String  [  ]  printValues  ; 
int  [  ]  sizes  ; 
try  { 
printValues  =  m_rrd  .  graph  (  "/dev/null"  ,  command  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ServletException  (  "IO exception while working with "  +  "RRD: "  +  e  )  ; 
}  catch  (  RRDException   e  )  { 
throw   new   ServletException  (  "Error from rrdtool: "  +  e  .  getMessage  (  )  )  ; 
} 
try  { 
sizes  =  m_rrd  .  parseGraphSize  (  printValues  [  0  ]  )  ; 
}  catch  (  RRDException   e  )  { 
throw   new   ServletException  (  "Error parsing graph sizes: "  +  e  .  getMessage  (  )  )  ; 
} 
return   printValues  ; 
} 






























protected   void   returnGraphOnDemand  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   id  )  throws   ServletException  ,  IOException  { 
String   command  =  (  String  )  m_map  .  get  (  id  )  ; 
expireCommandMap  (  )  ; 
if  (  command  ==  null  )  { 
graphError  (  request  ,  response  ,  "Graph not found"  ,  "The stored graph command for \""  +  id  +  "\" was not found. "  +  "Do you need to refresh the calling page?"  ,  (  request  .  getParameter  (  "debug"  )  !=  null  )  )  ; 
return  ; 
} 
if  (  request  .  getParameter  (  "debug"  )  !=  null  )  { 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
try  { 
m_rrd  .  graph  (  out  ,  "- "  +  command  )  ; 
}  catch  (  RRDException   e  )  { 
throw   new   ServletException  (  "Error while graphing: "  +  e  )  ; 
} 
response  .  setContentType  (  "image/png"  )  ; 
out  .  writeTo  (  response  .  getOutputStream  (  )  )  ; 
}  else  { 
try  { 
response  .  setContentType  (  "image/png"  )  ; 
m_rrd  .  graph  (  response  .  getOutputStream  (  )  ,  "- "  +  command  )  ; 
}  catch  (  RRDException   e  )  { 
graphError  (  request  ,  response  ,  e  .  getMessage  (  )  ,  e  .  toString  (  )  ,  false  )  ; 
return  ; 
} 
} 
} 























protected   void   returnGraphByName  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   name  )  throws   ServletException  ,  IOException  { 
FileInputStream   in  =  null  ; 
try  { 
in  =  new   FileInputStream  (  getGraphPath  (  name  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
graphError  (  request  ,  response  ,  "Graph not found"  ,  "The stored graph file for \""  +  name  +  "\" was not found. "  +  "Do you need to refresh the calling page?"  ,  (  request  .  getParameter  (  "debug"  )  !=  null  )  )  ; 
} 
OutputStream   out  =  response  .  getOutputStream  (  )  ; 
response  .  setContentType  (  "image/png"  )  ; 
try  { 
byte  [  ]  buffer  =  new   byte  [  16  *  1024  ]  ; 
int   bytesRead  ; 
while  (  (  bytesRead  =  in  .  read  (  buffer  )  )  !=  -  1  )  { 
out  .  write  (  buffer  ,  0  ,  bytesRead  )  ; 
} 
}  catch  (  IOException   e  )  { 
in  .  close  (  )  ; 
throw   e  ; 
} 
} 












protected   void   returnShowMap  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   IOException  { 
response  .  setContentType  (  "text/html"  )  ; 
PrintWriter   out  =  response  .  getWriter  (  )  ; 
out  .  println  (  "<html><body><p>"  )  ; 
synchronized  (  m_map  )  { 
out  .  println  (  "Current time: "  +  System  .  currentTimeMillis  (  )  +  "<br/>Expire: "  +  m_expire  +  "<br/><br/>"  )  ; 
out  .  println  (  "Count: "  +  m_map  .  size  (  )  +  "<br/>"  )  ; 
Iterator   i  =  m_map  .  keySet  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
out  .  println  (  (  String  )  i  .  next  (  )  +  "<br/>"  )  ; 
} 
out  .  println  (  "<br/>"  )  ; 
long   time  =  System  .  currentTimeMillis  (  )  -  m_expire  ; 
SortedMap   head  =  m_map  .  headMap  (  time  +  "-"  )  ; 
i  =  head  .  keySet  (  )  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
out  .  println  (  (  String  )  i  .  next  (  )  +  "<br/>"  )  ; 
} 
out  .  println  (  "Count: "  +  head  .  size  (  )  +  "<br/>"  )  ; 
} 
out  .  println  (  "</p></body></html"  )  ; 
} 




protected   String   getGraphPath  (  String   name  )  throws   ServletException  { 
String   hash  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  m_messagedigest_algorithm  )  ; 
md  .  update  (  name  .  getBytes  (  )  )  ; 
hash  =  bytesToHex  (  md  .  digest  (  )  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   ServletException  (  "NoSuchAlgorithmException while "  +  "attempting to hash file name: "  +  e  )  ; 
} 
File   tempDir  =  (  File  )  getServletContext  (  )  .  getAttribute  (  "javax.servlet.context.tempdir"  )  ; 
return   tempDir  .  getAbsolutePath  (  )  +  File  .  separatorChar  +  hash  ; 
} 











protected   void   graphError  (  HttpServletRequest   request  ,  HttpServletResponse   response  ,  String   error  ,  String   details  ,  boolean   debug  )  throws   IOException  { 
if  (  debug  )  { 
response  .  setContentType  (  "text/html"  )  ; 
PrintWriter   out  =  response  .  getWriter  (  )  ; 
out  .  println  (  "<html>"  )  ; 
out  .  println  (  " <head>"  )  ; 
out  .  println  (  "  <title>"  )  ; 
out  .  println  (  "   "  +  error  )  ; 
out  .  println  (  "  </title>"  )  ; 
out  .  println  (  " </head>"  )  ; 
out  .  println  (  " <body>"  )  ; 
out  .  println  (  "  <h1>"  )  ; 
out  .  println  (  "   "  +  error  )  ; 
out  .  println  (  "  </h1>"  )  ; 
out  .  println  (  "  <p>"  )  ; 
out  .  println  (  "   "  +  details  )  ; 
out  .  println  (  "  </p>"  )  ; 
out  .  println  (  " </body>"  )  ; 
out  .  println  (  "</html>"  )  ; 
}  else  { 
makeImage  (  "[Error: "  +  error  +  "]"  ,  response  )  ; 
} 
} 











protected   String   selfURL  (  HttpServletRequest   request  )  { 
String   context_path  =  (  String  )  request  .  getAttribute  (  "javax.servlet.include.context_path"  )  ; 
String   servlet_path  =  (  String  )  request  .  getAttribute  (  "javax.servlet.include.servlet_path"  )  ; 
return   context_path  +  servlet_path  ; 
} 









protected   void   createCommandMap  (  )  { 
m_map  =  Collections  .  synchronizedSortedMap  (  new   TreeMap  (  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
String   s1  =  (  String  )  o1  ; 
String   s2  =  (  String  )  o2  ; 
int   i1  =  s1  .  indexOf  (  '-'  )  ; 
int   i2  =  s2  .  indexOf  (  '-'  )  ; 
if  (  i1  !=  -  1  )  { 
s1  =  s1  .  substring  (  0  ,  i1  )  ; 
} 
if  (  i2  !=  -  1  )  { 
s2  =  s2  .  substring  (  0  ,  i2  )  ; 
} 
try  { 
long   l1  =  Long  .  parseLong  (  s1  )  ; 
long   l2  =  Long  .  parseLong  (  s2  )  ; 
if  (  l1  <  l2  )  { 
return  -  1  ; 
}  else   if  (  l1  >  l2  )  { 
return   1  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
} 
return   s1  .  compareTo  (  s2  )  ; 
} 
}  )  )  ; 
} 










protected   String   insertCommand  (  String   command  )  throws   ServletException  { 
String   digest  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  m_messagedigest_algorithm  )  ; 
md  .  update  (  command  .  getBytes  (  )  )  ; 
byte   bytes  [  ]  =  new   byte  [  20  ]  ; 
m_random  .  nextBytes  (  bytes  )  ; 
md  .  update  (  bytes  )  ; 
digest  =  bytesToHex  (  md  .  digest  (  )  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   ServletException  (  "NoSuchAlgorithmException while "  +  "attempting to generate graph ID: "  +  e  )  ; 
} 
String   id  =  System  .  currentTimeMillis  (  )  +  "-"  +  digest  ; 
m_map  .  put  (  id  ,  command  )  ; 
return   id  ; 
} 









public   static   String   bytesToHex  (  byte  [  ]  data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
buf  .  append  (  byteToHex  (  data  [  i  ]  )  )  ; 
} 
return  (  buf  .  toString  (  )  )  ; 
} 









public   static   String   byteToHex  (  byte   data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  toHexChar  (  (  data  >  >  >  4  )  &  0x0F  )  )  ; 
buf  .  append  (  toHexChar  (  data  &  0x0F  )  )  ; 
return   buf  .  toString  (  )  ; 
} 









public   static   char   toHexChar  (  int   i  )  { 
if  (  (  0  <=  i  )  &&  (  i  <=  9  )  )  return  (  char  )  (  '0'  +  i  )  ;  else   return  (  char  )  (  'a'  +  (  i  -  10  )  )  ; 
} 





protected   void   expireCommandMap  (  )  { 
long   time  =  System  .  currentTimeMillis  (  )  -  m_expire  ; 
synchronized  (  m_map  )  { 
m_map  .  headMap  (  time  +  "-"  )  .  keySet  (  )  .  clear  (  )  ; 
} 
} 












public   void   makeImage  (  String   message  ,  HttpServletResponse   response  )  throws   IOException  { 
final   int   width  =  400  ; 
final   int   height  =  16  ; 
Graphics2D   g  =  null  ; 
ServletOutputStream   out  =  response  .  getOutputStream  (  )  ; 
try  { 
BufferedImage   image  =  new   BufferedImage  (  width  ,  height  ,  BufferedImage  .  TYPE_INT_RGB  )  ; 
g  =  image  .  createGraphics  (  )  ; 
g  .  setColor  (  Color  .  white  )  ; 
g  .  fillRect  (  0  ,  0  ,  width  ,  height  )  ; 
g  .  setColor  (  Color  .  black  )  ; 
g  .  setFont  (  new   Font  (  "Serif"  ,  Font  .  ITALIC  ,  12  )  )  ; 
g  .  drawString  (  message  ,  1  ,  height  -  (  height  /  4  )  -  1  )  ; 
response  .  setContentType  (  "image/png"  )  ; 
PngEncoderB   encoder  =  new   PngEncoderB  (  image  )  ; 
out  .  write  (  encoder  .  pngEncode  (  )  )  ; 
}  finally  { 
if  (  g  !=  null  )  { 
g  .  dispose  (  )  ; 
} 
} 
} 
} 

