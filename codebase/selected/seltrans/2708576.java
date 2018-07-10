package   com  .  toolkit  .  util  .  pdf  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Random  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  xhtmlrenderer  .  resource  .  XMLResource  ; 
import   org  .  xhtmlrenderer  .  util  .  XRLog  ; 
import   com  .  toolkit  .  util  .  Print  ; 












public   class   XHTMLRendererBase  { 

private   static   final   boolean   _DEBUG_  =  false  ; 

private   String   htmlContent  ; 

private   String   replKey  =  ""  ; 

private   String   replVal  =  ""  ; 

private   Object   applicationObject  ; 





public   String   getHtmlContent  (  )  { 
return   htmlContent  ; 
} 





public   void   setHtmlContent  (  final   String   data  )  { 
this  .  htmlContent  =  data  ; 
} 






public   void   setApplicationObject  (  final   Object   obj  )  { 
applicationObject  =  obj  ; 
} 





public   Object   getApplicationObject  (  )  { 
return   applicationObject  ; 
} 






private   static   String   toHexString  (  final   byte  [  ]  bytes  )  { 
final   char  [  ]  ret  =  new   char  [  bytes  .  length  *  2  ]  ; 
for  (  int   i  =  0  ,  j  =  0  ;  i  <  bytes  .  length  ;  i  ++  )  { 
int   c  =  (  int  )  bytes  [  i  ]  ; 
if  (  c  <  0  )  { 
c  +=  0x100  ; 
} 
ret  [  j  ++  ]  =  Character  .  forDigit  (  c  /  0x10  ,  0x10  )  ; 
ret  [  j  ++  ]  =  Character  .  forDigit  (  c  %  0x10  ,  0x10  )  ; 
} 
return   new   String  (  ret  )  ; 
} 






public   static   String   getShortenUniqueKey  (  )  { 
final   String   val  =  (  getUniqueKey  (  )  !=  null  )  ?  getUniqueKey  (  )  :  "valid_filename_key"  ; 
if  (  val  .  length  (  )  >=  10  )  { 
return   val  .  substring  (  0  ,  9  )  ; 
}  else  { 
return  ""  ; 
} 
} 





public   static   String   getUniqueKey  (  )  { 
String   digest  =  ""  ; 
try  { 
final   MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
final   String   timeVal  =  ""  +  (  System  .  currentTimeMillis  (  )  +  1  )  ; 
String   localHost  =  ""  ; 
try  { 
localHost  =  InetAddress  .  getLocalHost  (  )  .  toString  (  )  ; 
}  catch  (  UnknownHostException   e  )  { 
println  (  "Warn: getUniqueKey(), Error trying to get localhost"  +  e  .  getMessage  (  )  )  ; 
} 
final   String   randVal  =  ""  +  new   Random  (  )  .  nextInt  (  )  ; 
final   String   val  =  timeVal  +  localHost  +  randVal  ; 
md  .  reset  (  )  ; 
md  .  update  (  val  .  getBytes  (  )  )  ; 
digest  =  toHexString  (  md  .  digest  (  )  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
println  (  "Warn: getUniqueKey() "  +  e  )  ; 
} 
return   digest  ; 
} 





public   static   void   println  (  final   String   message  )  { 
final   String   msg  =  "<XHTMLRenderer HTMLToPDF Message> ("  +  datestr  (  )  +  ") "  +  message  ; 
Print  .  println  (  msg  )  ; 
trace  (  msg  )  ; 
} 





public   static   void   trace  (  final   String   msg  )  { 
Print  .  println  (  "com.toolkit.util.pdf.XHTMLRendererBase"  +  msg  )  ; 
} 





public   static   String   datestr  (  )  { 
return  ""  +  (  new   Date  (  )  )  ; 
} 






public   void   setKeyValue  (  final   String   replKey  ,  final   String   val  )  { 
if  (  val  !=  null  )  { 
this  .  replKey  =  replKey  ; 
this  .  replVal  =  val  ; 
} 
} 





public   String   getReplKey  (  )  { 
return   replKey  ; 
} 





public   void   setReplKey  (  final   String   replKey  )  { 
this  .  replKey  =  replKey  ; 
} 





public   String   getReplVal  (  )  { 
return   replVal  ; 
} 





public   void   setReplVal  (  final   String   replVal  )  { 
this  .  replVal  =  replVal  ; 
} 




public   static   void   setXhtmlProperties  (  )  { 
System  .  setProperty  (  "xr.util-logging.loggingEnabled"  ,  "true"  )  ; 
System  .  setProperty  (  "xr.load.xml-reader"  ,  "org.ccil.cowan.tagsoup.Parser"  )  ; 
System  .  setProperty  (  "xr.util-logging.loggingEnabled"  ,  "true"  )  ; 
System  .  setProperty  (  "xr.util-logging.java.util.logging.ConsoleHandler.level"  ,  "INFO"  )  ; 
System  .  setProperty  (  "xr.util-logging.level"  ,  "INFO"  )  ; 
System  .  setProperty  (  "org.xhtmlrenderer.minium.quality"  ,  "highest"  )  ; 
XRLog  .  setLoggingEnabled  (  true  )  ; 
} 







public   static   String   dummyXhtmlDocument  (  final   String   msg  )  { 
final   StringBuffer   buf  =  new   StringBuffer  (  )  ; 
buf  .  append  (  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""  )  ; 
buf  .  append  (  "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"  )  ; 
buf  .  append  (  "<html xmlns=\"http://www.w3.org/1999/xhtml\">"  )  ; 
buf  .  append  (  "<head>"  )  ; 
buf  .  append  (  " <style type=\"text/css\">"  )  ; 
buf  .  append  (  "   .error_text {"  )  ; 
buf  .  append  (  "     	font-family: arial,helvetica,verdana,geneva,sans-serif; font-size: 11pt; color: #700; padding: 16px; margin-left: 20px; margin-top: 20px;"  )  ; 
buf  .  append  (  "     	border: 1px solid #888; background-color: #f8f8f8"  )  ; 
buf  .  append  (  "   }"  )  ; 
buf  .  append  (  " </style>"  )  ; 
buf  .  append  (  "</head><body>"  )  ; 
buf  .  append  (  "<div class=\"error_text\">"  )  ; 
buf  .  append  (  " A System Error Occurred While Trying to Create PDF Document. ("  +  datestr  (  )  +  ") <br /> ERR: "  +  msg  )  ; 
buf  .  append  (  "</div>"  )  ; 
buf  .  append  (  "</body>"  )  ; 
buf  .  append  (  "</html>"  )  ; 
return   buf  .  toString  (  )  ; 
} 









public   void   writeITextOutputStream  (  final   Document   doc  ,  final   OutputStream   os  )  throws   Exception  { 
final   ITextRenderer   renderer  =  new   ITextRenderer  (  )  ; 
this  .  writeITextOutputStream  (  doc  ,  os  ,  renderer  )  ; 
} 









public   void   writeITextOutputStream  (  final   Document   doc  ,  final   OutputStream   os  ,  final   ITextRenderer   itextRenderer  )  throws   Exception  { 
final   ITextRenderer   renderer  =  itextRenderer  ; 
renderer  .  setDocument  (  doc  ,  null  )  ; 
renderer  .  layout  (  )  ; 
println  (  "Sending output stream response to client"  )  ; 
renderer  .  createPDF  (  os  )  ; 
os  .  flush  (  )  ; 
os  .  close  (  )  ; 
} 








public   byte  [  ]  writeITextByteArray  (  final   Document   doc  )  throws   Exception  { 
final   ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
this  .  writeITextOutputStream  (  doc  ,  baos  )  ; 
return   baos  .  toByteArray  (  )  ; 
} 








private   void   writeITextFile  (  final   Document   doc  ,  final   String   filepath  )  throws   Exception  { 
final   FileOutputStream   stream  =  new   FileOutputStream  (  filepath  )  ; 
final   byte  [  ]  data  =  this  .  writeITextByteArray  (  doc  )  ; 
try  { 
if  (  stream  !=  null  )  { 
stream  .  write  (  data  )  ; 
} 
}  finally  { 
if  (  stream  !=  null  )  { 
stream  .  close  (  )  ; 
} 
} 
} 










public   void   parseDocumentStream  (  final   OutputStream   os  ,  final   XHTMLRendererText   parser  )  throws   Exception  { 
this  .  parseDocumentStream  (  os  ,  parser  ,  new   File  (  "."  )  .  toURI  (  )  .  toURL  (  )  .  toExternalForm  (  )  )  ; 
} 










public   void   parseDocumentStream  (  final   OutputStream   os  ,  final   XHTMLRendererText   parser  ,  final   String   baseURL  )  throws   Exception  { 
if  (  parser  !=  null  )  { 
parser  .  setBaseRenderer  (  this  )  ; 
} 
String   formattedHtmlDoc  =  ""  ; 
if  (  parser  !=  null  )  { 
println  (  "Renderer Parse Text Handler Found, attempting to parse document"  )  ; 
formattedHtmlDoc  =  parser  .  parse  (  )  ; 
}  else  { 
formattedHtmlDoc  =  this  .  getHtmlContent  (  )  ; 
} 
if  (  (  formattedHtmlDoc  ==  null  )  ||  (  formattedHtmlDoc  .  length  (  )  ==  0  )  )  { 
println  (  "<[ERROR] parseDocumentStream>: Renderer Parse Text Handler Failed, falling back to the dummy document"  )  ; 
formattedHtmlDoc  =  dummyXhtmlDocument  (  "[Invalid Document at parseDocumentStream]"  )  ; 
} 
if  (  (  formattedHtmlDoc  ==  null  )  ||  (  formattedHtmlDoc  .  length  (  )  ==  0  )  )  { 
println  (  "Invalid input document"  )  ; 
}  else  { 
println  (  "<[INFO] parseDocumentStream>: Renderer Parse Text Handler - document size : "  +  formattedHtmlDoc  .  length  (  )  )  ; 
} 
println  (  "<parseDocumentStream> Attempting to parse document from stream, len="  +  formattedHtmlDoc  .  length  (  )  )  ; 
final   XMLResource   xmlResouce  =  XMLResource  .  load  (  new   ByteArrayInputStream  (  formattedHtmlDoc  .  getBytes  (  )  )  )  ; 
final   Document   doc  =  xmlResouce  .  getDocument  (  )  ; 
this  .  writeITextOutputStream  (  doc  ,  os  ,  new   ITextRenderer  (  baseURL  )  )  ; 
println  (  "Done with xhtmlrenderer - parseDocumentStream"  )  ; 
} 






public   static   String   loadFile  (  final   String   path  )  { 
StringBuffer   html  =  new   StringBuffer  (  )  ; 
FileInputStream   stream  ; 
try  { 
stream  =  new   FileInputStream  (  path  )  ; 
}  catch  (  FileNotFoundException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
throw   new   RuntimeException  (  "<LoadFile> path="  +  path  +  " err="  +  e1  .  getMessage  (  )  )  ; 
} 
try  { 
stream  =  new   FileInputStream  (  path  )  ; 
final   BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  stream  )  )  ; 
String   data  =  ""  ; 
do  { 
data  =  reader  .  readLine  (  )  ; 
if  (  data  !=  null  )  { 
html  .  append  (  data  )  ; 
} 
}  while  (  data  !=  null  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
throw   new   RuntimeException  (  "<LoadFile> path="  +  path  +  " err="  +  e  .  getMessage  (  )  )  ; 
}  finally  { 
if  (  stream  !=  null  )  { 
try  { 
stream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   html  .  toString  (  )  ; 
} 







public   void   parseDocumentFile  (  final   String   path  ,  final   XHTMLRendererText   parser  )  throws   Exception  { 
final   FileOutputStream   stream  =  new   FileOutputStream  (  path  )  ; 
if  (  parser  !=  null  )  { 
parser  .  setBaseRenderer  (  this  )  ; 
} 
this  .  parseDocumentStream  (  stream  ,  parser  )  ; 
} 







public   void   parseDocumentFile  (  final   String   path  ,  final   XHTMLRendererText   parser  ,  final   String   baseURL  )  throws   Exception  { 
final   FileOutputStream   stream  =  new   FileOutputStream  (  path  )  ; 
if  (  parser  !=  null  )  { 
parser  .  setBaseRenderer  (  this  )  ; 
} 
this  .  parseDocumentStream  (  stream  ,  parser  ,  baseURL  )  ; 
} 
} 

