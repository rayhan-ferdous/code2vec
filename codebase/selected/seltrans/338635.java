package   be  .  pendragon  .  net  .  pac  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  Reader  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  UnknownHostException  ; 
import   java  .  util  .  Arrays  ; 
import   org  .  mozilla  .  javascript  .  Context  ; 
import   org  .  mozilla  .  javascript  .  ContextFactory  ; 
import   org  .  mozilla  .  javascript  .  Function  ; 
import   org  .  mozilla  .  javascript  .  ScriptableObject  ; 

public   final   class   ProxyAutoConfiguration  { 

private   URI   pacFileUri  ; 

public   URI   getPacFileUri  (  )  { 
return   pacFileUri  ; 
} 

private   Context   jsContext  ; 

private   ScriptableObject   scope  ; 

private   Function   findProxyForURLFunction  ; 

private   ProxyAutoConfiguration  (  )  { 
} 

public   static   ProxyAutoConfiguration   build  (  URI   pacFileUri  )  throws   IOException  { 
return   new   ProxyAutoConfiguration  (  )  .  init  (  pacFileUri  )  ; 
} 

public   static   ProxyAutoConfiguration   build  (  File   pacFile  )  throws   IOException  { 
return   build  (  pacFile  .  toURI  (  )  )  ; 
} 

public   static   ProxyAutoConfiguration   build  (  String   pacFilePath  )  throws   IOException  { 
return   build  (  new   File  (  pacFilePath  )  )  ; 
} 

private   ProxyAutoConfiguration   init  (  URI   pacFileUri  )  throws   IOException  { 
this  .  pacFileUri  =  pacFileUri  ; 
jsContext  =  ContextFactory  .  getGlobal  (  )  .  enterContext  (  )  ; 
scope  =  jsContext  .  initStandardObjects  (  )  ; 
scope  .  defineFunctionProperties  (  new   String  [  ]  {  "isPlainHostName"  ,  "dnsDomainIs"  ,  "localHostOrDomainIs"  ,  "isResolvable"  ,  "isInNet"  ,  "dnsResolve"  ,  "myIpAddress"  ,  "dnsDomainLevels"  ,  "shExpMatch"  ,  "weekdayRange"  ,  "dateRange"  ,  "timeRange"  }  ,  ProxyAutoConfiguration  .  class  ,  ScriptableObject  .  DONTENUM  )  ; 
URL   url  =  pacFileUri  .  toURL  (  )  ; 
InputStream   bytesStream  =  url  .  openStream  (  )  ; 
Reader   charStream  =  new   InputStreamReader  (  bytesStream  )  ; 
jsContext  .  evaluateReader  (  scope  ,  charStream  ,  pacFileUri  .  toString  (  )  ,  1  ,  null  )  ; 
findProxyForURLFunction  =  (  Function  )  scope  .  get  (  "FindProxyForURL"  ,  scope  )  ; 
return   this  ; 
} 









public   static   boolean   isPlainHostName  (  String   host  )  { 
if  (  null  ==  host  )  return   false  ; 
return  (  host  .  indexOf  (  '.'  )  <  0  )  ; 
} 










public   static   boolean   dnsDomainIs  (  String   host  ,  String   domain  )  { 
if  (  (  null  ==  host  )  ||  (  domain  ==  null  )  )  return   false  ; 
return   host  .  endsWith  (  domain  )  ; 
} 












public   static   boolean   localHostOrDomainIs  (  String   host  ,  String   hostdom  )  { 
if  (  (  null  ==  host  )  ||  (  hostdom  ==  null  )  )  return   false  ; 
if  (  host  .  equals  (  hostdom  )  )  return   true  ; 
if  (  !  isPlainHostName  (  host  )  )  return   false  ; 
return   host  .  equals  (  hostdom  .  split  (  "\\."  )  [  0  ]  )  ; 
} 









public   static   boolean   isResolvable  (  String   host  )  { 
try  { 
InetAddress  .  getByName  (  host  )  ; 
return   true  ; 
}  catch  (  UnknownHostException   e  )  { 
return   false  ; 
} 
} 













public   static   boolean   isInNet  (  String   host  ,  String   pattern  ,  String   mask  )  throws   UnknownHostException  { 
int   hostAddress  =  ipAddressToInteger  (  InetAddress  .  getByName  (  host  )  .  getHostAddress  (  )  )  ; 
int   patternAsInt  =  ipAddressToInteger  (  pattern  )  ; 
int   maskAsInt  =  ipAddressToInteger  (  mask  )  ; 
return  (  hostAddress  &  maskAsInt  )  ==  patternAsInt  ; 
} 

private   static   int   ipAddressToInteger  (  String   ipAddress  )  { 
String  [  ]  parts  =  ipAddress  .  split  (  "\\."  )  ; 
if  (  parts  .  length  !=  4  )  throw   new   IllegalArgumentException  (  "Invalid IP adress '"  +  ipAddress  +  "'"  )  ; 
return  (  (  toUByte  (  parts  [  0  ]  )  <<  8  |  toUByte  (  parts  [  1  ]  )  )  <<  8  |  toUByte  (  parts  [  2  ]  )  )  <<  8  |  toUByte  (  parts  [  3  ]  )  ; 
} 

private   static   byte   toUByte  (  String   value  )  { 
Integer   integer  =  Integer  .  valueOf  (  value  )  ; 
if  (  (  ~  0xff  &  integer  )  !=  0  )  throw   new   IllegalArgumentException  (  "Not a byte: '"  +  value  +  "'"  )  ; 
return   integer  .  byteValue  (  )  ; 
} 









public   static   String   dnsResolve  (  String   host  )  throws   UnknownHostException  { 
return   InetAddress  .  getByName  (  host  )  .  getHostAddress  (  )  ; 
} 









public   static   String   myIpAddress  (  )  throws   SocketException  ,  UnknownHostException  { 
return   InetAddress  .  getLocalHost  (  )  .  getHostAddress  (  )  ; 
} 










public   static   int   dnsDomainLevels  (  String   host  )  { 
return   host  .  split  (  "\\."  )  .  length  -  1  ; 
} 











public   boolean   shExpMatch  (  String   str  ,  String   shexp  )  { 
String   regex  =  "\\Q"  +  shexp  .  replaceAll  (  "\\*"  ,  "\\\\E.*\\\\Q"  )  +  "\\E"  ; 
return   str  .  matches  (  regex  )  ; 
} 




public   static   boolean   weekdayRange  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 




public   static   boolean   dateRange  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 




public   static   boolean   timeRange  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 

public   void   close  (  )  { 
Context  .  exit  (  )  ; 
} 

@  Override 
protected   void   finalize  (  )  throws   Throwable  { 
this  .  close  (  )  ; 
super  .  finalize  (  )  ; 
} 













public   Connection  [  ]  findProxyForURL  (  URL   url  ,  String   host  )  { 
Object   functionArgs  [  ]  =  {  url  .  toString  (  )  ,  host  }  ; 
Object   result  =  findProxyForURLFunction  .  call  (  jsContext  ,  scope  ,  scope  ,  functionArgs  )  ; 
String  [  ]  connectionsDescriptions  =  Context  .  toString  (  result  )  .  trim  (  )  .  split  (  "\\s*;\\s*"  )  ; 
Connection  [  ]  connections  =  new   Connection  [  connectionsDescriptions  .  length  ]  ; 
for  (  int   idx  =  0  ;  idx  <  connections  .  length  ;  idx  ++  )  { 
String  [  ]  parts  =  connectionsDescriptions  [  idx  ]  .  split  (  "\\s+"  )  ; 
if  (  parts  .  length  <  1  )  throw   new   IllegalArgumentException  (  "'"  +  connectionsDescriptions  [  idx  ]  +  "' is not a connection description"  )  ; 
if  (  parts  [  0  ]  .  equals  (  DirectConnection  .  PREFIX  )  )  { 
if  (  parts  .  length  !=  1  )  throw   new   IllegalArgumentException  (  "'"  +  connectionsDescriptions  [  idx  ]  +  "' is not a valid DIRECT connection description"  )  ; 
connections  [  idx  ]  =  DirectConnection  .  DIRECT  ; 
}  else   if  (  parts  [  0  ]  .  equals  (  ProxyConnection  .  PREFIX  )  )  { 
if  (  parts  .  length  !=  2  )  throw   new   IllegalArgumentException  (  "'"  +  connectionsDescriptions  [  idx  ]  +  "' is missing host an port"  )  ; 
connections  [  idx  ]  =  new   ProxyConnection  (  parts  [  1  ]  )  ; 
}  else   if  (  parts  [  0  ]  .  equals  (  ProxyConnection  .  PREFIX  )  )  { 
if  (  parts  .  length  !=  2  )  throw   new   IllegalArgumentException  (  "'"  +  connectionsDescriptions  [  idx  ]  +  "' is missing host an port"  )  ; 
connections  [  idx  ]  =  new   SocksConnection  (  parts  [  1  ]  )  ; 
}  else   throw   new   IllegalArgumentException  (  "'"  +  connectionsDescriptions  [  idx  ]  +  "' is not a valid connection description"  )  ; 
} 
return   connections  ; 
} 

public   Connection  [  ]  findProxyForURL  (  URL   url  )  { 
return   findProxyForURL  (  url  ,  null  )  ; 
} 




public   static   void   main  (  String  [  ]  args  )  { 
try  { 
if  (  (  args  .  length  <  2  )  ||  (  args  .  length  >  3  )  )  { 
usage  (  )  ; 
throw   new   IllegalArgumentException  (  "Incorrect arguments count"  )  ; 
} 
ProxyAutoConfiguration   pac  =  ProxyAutoConfiguration  .  build  (  args  [  0  ]  )  ; 
Connection  [  ]  proxies  =  (  args  .  length  ==  2  )  ?  pac  .  findProxyForURL  (  new   URL  (  args  [  1  ]  )  )  :  pac  .  findProxyForURL  (  new   URL  (  args  [  1  ]  )  ,  args  [  2  ]  )  ; 
System  .  out  .  println  (  Arrays  .  toString  (  proxies  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

private   static   void   usage  (  )  { 
System  .  out  .  println  (  "Usage: java "  +  ProxyAutoConfiguration  .  class  .  getName  (  )  +  "<proxy file> <url> [<host>]"  )  ; 
} 
} 

