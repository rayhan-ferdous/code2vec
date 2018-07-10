package   org  .  apache  .  axis  .  client  ; 

import   org  .  apache  .  axis  .  utils  .  Messages  ; 
import   org  .  apache  .  axis  .  utils  .  ClassUtils  ; 
import   javax  .  xml  .  parsers  .  SAXParser  ; 
import   javax  .  xml  .  parsers  .  SAXParserFactory  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PrintStream  ; 




public   class   HappyClient  { 

PrintStream   out  ; 

public   HappyClient  (  PrintStream   out  )  { 
this  .  out  =  out  ; 
} 






Class   classExists  (  String   classname  )  { 
try  { 
return   Class  .  forName  (  classname  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
return   null  ; 
} 
} 






boolean   resourceExists  (  String   resource  )  { 
boolean   found  ; 
InputStream   instream  =  ClassUtils  .  getResourceAsStream  (  this  .  getClass  (  )  ,  resource  )  ; 
found  =  instream  !=  null  ; 
if  (  instream  !=  null  )  { 
try  { 
instream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   found  ; 
} 











int   probeClass  (  String   category  ,  String   classname  ,  String   jarFile  ,  String   description  ,  String   errorText  ,  String   homePage  )  throws   IOException  { 
String   url  =  ""  ; 
if  (  homePage  !=  null  )  { 
url  =  Messages  .  getMessage  (  "happyClientHomepage"  ,  homePage  )  ; 
} 
String   errorLine  =  ""  ; 
if  (  errorText  !=  null  )  { 
errorLine  =  Messages  .  getMessage  (  errorText  )  ; 
} 
try  { 
Class   clazz  =  classExists  (  classname  )  ; 
if  (  clazz  ==  null  )  { 
String   text  ; 
text  =  Messages  .  getMessage  (  "happyClientMissingClass"  ,  category  ,  classname  ,  jarFile  )  ; 
out  .  println  (  text  )  ; 
out  .  println  (  url  )  ; 
return   1  ; 
}  else  { 
String   location  =  getLocation  (  clazz  )  ; 
String   text  ; 
if  (  location  ==  null  )  { 
text  =  Messages  .  getMessage  (  "happyClientFoundDescriptionClass"  ,  description  ,  classname  )  ; 
}  else  { 
text  =  Messages  .  getMessage  (  "happyClientFoundDescriptionClassLocation"  ,  description  ,  classname  ,  location  )  ; 
} 
out  .  println  (  text  )  ; 
return   0  ; 
} 
}  catch  (  NoClassDefFoundError   ncdfe  )  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientNoDependency"  ,  category  ,  classname  ,  jarFile  )  )  ; 
out  .  println  (  errorLine  )  ; 
out  .  println  (  url  )  ; 
out  .  println  (  ncdfe  .  getMessage  (  )  )  ; 
return   1  ; 
} 
} 






String   getLocation  (  Class   clazz  )  { 
try  { 
java  .  net  .  URL   url  =  clazz  .  getProtectionDomain  (  )  .  getCodeSource  (  )  .  getLocation  (  )  ; 
String   location  =  url  .  toString  (  )  ; 
if  (  location  .  startsWith  (  "jar"  )  )  { 
url  =  (  (  java  .  net  .  JarURLConnection  )  url  .  openConnection  (  )  )  .  getJarFileURL  (  )  ; 
location  =  url  .  toString  (  )  ; 
} 
if  (  location  .  startsWith  (  "file"  )  )  { 
java  .  io  .  File   file  =  new   java  .  io  .  File  (  url  .  getFile  (  )  )  ; 
return   file  .  getAbsolutePath  (  )  ; 
}  else  { 
return   url  .  toString  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
} 
return   Messages  .  getMessage  (  "happyClientUnknownLocation"  )  ; 
} 










int   needClass  (  String   classname  ,  String   jarFile  ,  String   description  ,  String   errorText  ,  String   homePage  )  throws   IOException  { 
return   probeClass  (  Messages  .  getMessage  (  "happyClientError"  )  ,  classname  ,  jarFile  ,  description  ,  errorText  ,  homePage  )  ; 
} 










int   wantClass  (  String   classname  ,  String   jarFile  ,  String   description  ,  String   errorText  ,  String   homePage  )  throws   IOException  { 
return   probeClass  (  Messages  .  getMessage  (  "happyClientWarning"  )  ,  classname  ,  jarFile  ,  description  ,  errorText  ,  homePage  )  ; 
} 







int   wantResource  (  String   resource  ,  String   errorText  )  throws   Exception  { 
if  (  !  resourceExists  (  resource  )  )  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientNoResource"  ,  resource  )  )  ; 
out  .  println  (  errorText  )  ; 
return   0  ; 
}  else  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientFoundResource"  ,  resource  )  )  ; 
return   1  ; 
} 
} 





private   String   getParserName  (  )  { 
SAXParser   saxParser  =  getSAXParser  (  )  ; 
if  (  saxParser  ==  null  )  { 
return   Messages  .  getMessage  (  "happyClientNoParser"  )  ; 
} 
String   saxParserName  =  saxParser  .  getClass  (  )  .  getName  (  )  ; 
return   saxParserName  ; 
} 





private   SAXParser   getSAXParser  (  )  { 
SAXParserFactory   saxParserFactory  =  SAXParserFactory  .  newInstance  (  )  ; 
if  (  saxParserFactory  ==  null  )  { 
return   null  ; 
} 
SAXParser   saxParser  =  null  ; 
try  { 
saxParser  =  saxParserFactory  .  newSAXParser  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   saxParser  ; 
} 





private   String   getParserLocation  (  )  { 
SAXParser   saxParser  =  getSAXParser  (  )  ; 
if  (  saxParser  ==  null  )  { 
return   null  ; 
} 
String   location  =  getLocation  (  saxParser  .  getClass  (  )  )  ; 
return   location  ; 
} 






public   int   getJavaVersionNumber  (  )  { 
int   javaVersionNumber  =  10  ; 
try  { 
Class  .  forName  (  "java.lang.Void"  )  ; 
javaVersionNumber  ++  ; 
Class  .  forName  (  "java.lang.ThreadLocal"  )  ; 
javaVersionNumber  ++  ; 
Class  .  forName  (  "java.lang.StrictMath"  )  ; 
javaVersionNumber  ++  ; 
Class  .  forName  (  "java.lang.CharSequence"  )  ; 
javaVersionNumber  ++  ; 
}  catch  (  Throwable   t  )  { 
} 
return   javaVersionNumber  ; 
} 

private   void   title  (  String   title  )  { 
out  .  println  (  )  ; 
String   message  =  Messages  .  getMessage  (  title  )  ; 
out  .  println  (  message  )  ; 
for  (  int   i  =  0  ;  i  <  message  .  length  (  )  ;  i  ++  )  { 
out  .  print  (  "="  )  ; 
} 
out  .  println  (  )  ; 
} 







public   boolean   verifyClientIsHappy  (  boolean   warningsAsErrors  )  throws   IOException  { 
int   needed  =  0  ,  wanted  =  0  ; 
out  .  println  (  )  ; 
title  (  "happyClientTitle"  )  ; 
title  (  "happyClientNeeded"  )  ; 
needed  =  needClass  (  "javax.xml.soap.SOAPMessage"  ,  "saaj.jar"  ,  "SAAJ"  ,  "happyClientNoAxis"  ,  "http://xml.apache.org/axis/"  )  ; 
needed  +=  needClass  (  "javax.xml.rpc.Service"  ,  "jaxrpc.jar"  ,  "JAX-RPC"  ,  "happyClientNoAxis"  ,  "http://xml.apache.org/axis/"  )  ; 
needed  +=  needClass  (  "org.apache.commons.discovery.Resource"  ,  "commons-discovery.jar"  ,  "Jakarta-Commons Discovery"  ,  "happyClientNoAxis"  ,  "http://jakarta.apache.org/commons/discovery.html"  )  ; 
needed  +=  needClass  (  "org.apache.commons.logging.Log"  ,  "commons-logging.jar"  ,  "Jakarta-Commons Logging"  ,  "happyClientNoAxis"  ,  "http://jakarta.apache.org/commons/logging.html"  )  ; 
needed  +=  needClass  (  "org.apache"  +  ".log"  +  "4j"  +  ".Layout"  ,  "log4"  +  "j-1.2.4.jar"  ,  "Log4"  +  "j"  ,  "happyClientNoLog4J"  ,  "http://jakarta.apache.org/log"  +  "4j"  )  ; 
needed  +=  needClass  (  "com.ibm.wsdl.factory.WSDLFactoryImpl"  ,  "wsdl4j.jar"  ,  "WSDL4Java"  ,  "happyClientNoAxis"  ,  null  )  ; 
needed  +=  needClass  (  "javax.xml.parsers.SAXParserFactory"  ,  "xerces.jar"  ,  "JAXP"  ,  "happyClientNoAxis"  ,  "http://xml.apache.org/xerces-j/"  )  ; 
title  (  "happyClientOptional"  )  ; 
wanted  +=  wantClass  (  "javax.mail.internet.MimeMessage"  ,  "mail.jar"  ,  "Mail"  ,  "happyClientNoAttachments"  ,  "http://java.sun.com/products/javamail/"  )  ; 
wanted  +=  wantClass  (  "javax.activation.DataHandler"  ,  "activation.jar"  ,  "Activation"  ,  "happyClientNoAttachments"  ,  "http://java.sun.com/products/javabeans/glasgow/jaf.html"  )  ; 
wanted  +=  wantClass  (  "org.apache.xml.security.Init"  ,  "xmlsec.jar"  ,  "XML Security"  ,  "happyClientNoSecurity"  ,  "http://xml.apache.org/security/"  )  ; 
wanted  +=  wantClass  (  "javax.net.ssl.SSLSocketFactory"  ,  Messages  .  getMessage  (  "happyClientJSSEsources"  )  ,  "Java Secure Socket Extension"  ,  "happyClientNoHTTPS"  ,  "http://java.sun.com/products/jsse/"  )  ; 
int   warningMessages  =  0  ; 
String   xmlParser  =  getParserName  (  )  ; 
String   xmlParserLocation  =  getParserLocation  (  )  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientXMLinfo"  ,  xmlParser  ,  xmlParserLocation  )  )  ; 
if  (  xmlParser  .  indexOf  (  "xerces"  )  <=  0  )  { 
warningMessages  ++  ; 
out  .  println  (  )  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientRecommendXerces"  )  )  ; 
} 
if  (  getJavaVersionNumber  (  )  <  13  )  { 
warningMessages  ++  ; 
out  .  println  (  )  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientUnsupportedJVM"  )  )  ; 
} 
boolean   happy  ; 
title  (  "happyClientSummary"  )  ; 
if  (  needed  ==  0  )  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientCorePresent"  )  )  ; 
happy  =  true  ; 
}  else  { 
happy  =  false  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientCoreMissing"  ,  Integer  .  toString  (  needed  )  )  )  ; 
} 
if  (  wanted  >  0  )  { 
out  .  println  (  )  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientOptionalMissing"  ,  Integer  .  toString  (  wanted  )  )  )  ; 
out  .  println  (  Messages  .  getMessage  (  "happyClientOptionalOK"  )  )  ; 
if  (  warningsAsErrors  )  { 
happy  =  false  ; 
} 
}  else  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientOptionalPresent"  )  )  ; 
} 
if  (  warningMessages  >  0  )  { 
out  .  println  (  Messages  .  getMessage  (  "happyClientWarningMessageCount"  ,  Integer  .  toString  (  warningMessages  )  )  )  ; 
if  (  warningsAsErrors  )  { 
happy  =  false  ; 
} 
} 
return   happy  ; 
} 






public   static   void   main  (  String   args  [  ]  )  { 
boolean   isHappy  =  isClientHappy  (  args  )  ; 
System  .  exit  (  isHappy  ?  0  :  -  1  )  ; 
} 







private   static   boolean   isClientHappy  (  String  [  ]  args  )  { 
HappyClient   happy  =  new   HappyClient  (  System  .  out  )  ; 
boolean   isHappy  ; 
int   missing  =  0  ; 
try  { 
isHappy  =  happy  .  verifyClientIsHappy  (  false  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
missing  +=  happy  .  probeClass  (  "argument"  ,  args  [  i  ]  ,  null  ,  null  ,  null  ,  null  )  ; 
} 
if  (  missing  >  0  )  { 
isHappy  =  false  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
isHappy  =  false  ; 
} 
return   isHappy  ; 
} 
} 

