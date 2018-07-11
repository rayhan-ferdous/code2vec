package   com  .  globant  .  google  .  mendoza  .  malbec  .  transport  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   javax  .  net  .  ServerSocketFactory  ; 
import   javax  .  net  .  ssl  .  HostnameVerifier  ; 
import   javax  .  net  .  ssl  .  HttpsURLConnection  ; 
import   javax  .  net  .  ssl  .  SSLSession  ; 
import   javax  .  net  .  ssl  .  SSLSocketFactory  ; 
import   org  .  apache  .  commons  .  codec  .  binary  .  Base64  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 









public   final   class   StandAlone   implements   Transport  { 



private   static   final   int   HTTPS_PORT  =  443  ; 



private   static   final   int   READ_BUFFER_SIZE  =  1024  ; 



private   static   Log   log  =  LogFactory  .  getLog  (  StandAlone  .  class  )  ; 



private   String   serverURL  =  "https://localhost:10001"  ; 



private   int   port  =  HTTPS_PORT  ; 



private   SSLSocketFactory   clientSocketFactory  ; 



private   ServerSocketFactory   serverSocketFactory  ; 



private   String   merchantId  ; 



private   String   merchantKey  ; 



private   Receiver   receiver  =  null  ; 



private   NanoHTTPD   server  =  null  ; 



private   String   sendErrorMessage  =  null  ; 




private   boolean   acceptAllCertificates  ; 



















public   StandAlone  (  final   SSLSocketFactory   theClientSocketFactory  ,  final   ServerSocketFactory   theServerSocketFactory  ,  final   String   theMerchantId  ,  final   String   theMerchantKey  ,  final   String   url  )  { 
if  (  theMerchantId  ==  null  )  { 
throw   new   IllegalArgumentException  (  "the merchant id cannot be null"  )  ; 
} 
if  (  theMerchantKey  ==  null  )  { 
throw   new   IllegalArgumentException  (  "the merchant key cannot be null"  )  ; 
} 
if  (  url  ==  null  )  { 
throw   new   IllegalArgumentException  (  "the server url cannot be null"  )  ; 
} 
serverURL  =  url  ; 
merchantId  =  theMerchantId  ; 
merchantKey  =  theMerchantKey  ; 
clientSocketFactory  =  theClientSocketFactory  ; 
serverSocketFactory  =  theServerSocketFactory  ; 
} 



















public   StandAlone  (  final   SSLSocketFactory   theClientSocketFactory  ,  final   ServerSocketFactory   theServerSocketFactory  ,  final   String   theMerchantId  ,  final   String   theMerchantKey  ,  final   int   thePort  )  { 
if  (  theMerchantId  ==  null  )  { 
throw   new   IllegalArgumentException  (  "the merchant id cannot be null"  )  ; 
} 
if  (  theMerchantKey  ==  null  )  { 
throw   new   IllegalArgumentException  (  "the merchant key cannot be null"  )  ; 
} 
serverURL  =  null  ; 
merchantId  =  theMerchantId  ; 
merchantKey  =  theMerchantKey  ; 
clientSocketFactory  =  theClientSocketFactory  ; 
serverSocketFactory  =  theServerSocketFactory  ; 
port  =  thePort  ; 
} 






















public   StandAlone  (  final   SSLSocketFactory   theClientSocketFactory  ,  final   ServerSocketFactory   theServerSocketFactory  ,  final   String   theMerchantId  ,  final   String   theMerchantKey  ,  final   String   url  ,  final   int   thePort  ,  final   boolean   isAcceptAllCertificates  )  { 
this  (  theClientSocketFactory  ,  theServerSocketFactory  ,  theMerchantId  ,  theMerchantKey  ,  url  )  ; 
port  =  thePort  ; 
acceptAllCertificates  =  isAcceptAllCertificates  ; 
} 









public   int   getPort  (  )  { 
if  (  server  ==  null  )  { 
throw   new   IllegalStateException  (  "You must start the transport first"  )  ; 
} 
return   server  .  getPort  (  )  ; 
} 





public   void   setServerURL  (  final   String   url  )  { 
if  (  url  ==  null  )  { 
throw   new   IllegalArgumentException  (  "url cannot be null"  )  ; 
} 
serverURL  =  url  ; 
} 








public   String   send  (  final   String   message  )  { 
log  .  trace  (  "Entering send"  )  ; 
HttpURLConnection   connection  =  getConnection  (  "application/xml"  )  ; 
String   response  =  null  ; 
log  .  debug  (  "Writing message:\n"  +  message  )  ; 
try  { 
writeRequest  (  connection  ,  message  )  ; 
response  =  readResponse  (  connection  )  ; 
}  finally  { 
connection  .  disconnect  (  )  ; 
} 
if  (  response  ==  null  )  { 
throw   new   RuntimeException  (  sendErrorMessage  )  ; 
} 
log  .  debug  (  "Received response:\n"  +  response  )  ; 
log  .  trace  (  "Leaving send"  )  ; 
return   response  ; 
} 











public   void   registerReceiver  (  final   Receiver   theReceiver  )  { 
log  .  trace  (  "Entering registerReceiver"  )  ; 
if  (  theReceiver  ==  null  )  { 
throw   new   IllegalArgumentException  (  "receiver cannot be null"  )  ; 
} 
receiver  =  theReceiver  ; 
log  .  trace  (  "Leaving registerReceiver"  )  ; 
} 





public   void   start  (  )  { 
log  .  trace  (  "Entering start"  )  ; 
if  (  receiver  ==  null  )  { 
throw   new   IllegalStateException  (  "Must register a receiver before"  +  " starting the server"  )  ; 
} 
server  =  new   NanoHTTPD  (  port  ,  receiver  ,  serverSocketFactory  ,  merchantId  ,  merchantKey  )  ; 
log  .  trace  (  "Leaving start"  )  ; 
} 





public   void   stop  (  )  { 
if  (  server  ==  null  )  { 
throw   new   RuntimeException  (  "You must call start before stopping the"  +  " transport"  )  ; 
} 
if  (  server  !=  null  )  { 
server  .  stop  (  )  ; 
server  =  null  ; 
} 
} 








private   HttpURLConnection   getConnection  (  final   String   contentType  )  { 
log  .  trace  (  "Entering getConnection"  )  ; 
if  (  serverURL  ==  null  )  { 
throw   new   IllegalStateException  (  "Must set the server url first"  )  ; 
} 
HttpURLConnection   connection  =  null  ; 
try  { 
URL   url  =  new   URL  (  serverURL  )  ; 
connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
String   encoding  =  new   String  (  Base64  .  encodeBase64  (  (  merchantId  +  ":"  +  merchantKey  )  .  getBytes  (  )  )  )  ; 
connection  .  setRequestProperty  (  "Authorization"  ,  "Basic "  +  encoding  )  ; 
if  (  acceptAllCertificates  )  { 
if  (  connection   instanceof   HttpsURLConnection  )  { 
HttpsURLConnection   sslConnection  =  (  HttpsURLConnection  )  connection  ; 
sslConnection  .  setHostnameVerifier  (  new   HostnameVerifier  (  )  { 

public   boolean   verify  (  final   String   hostName  ,  final   SSLSession   session  )  { 
return   true  ; 
} 
}  )  ; 
} 
}  else  { 
if  (  clientSocketFactory  !=  null  )  { 
if  (  connection   instanceof   HttpsURLConnection  )  { 
HttpsURLConnection   sslConnection  =  (  HttpsURLConnection  )  connection  ; 
sslConnection  .  setSSLSocketFactory  (  clientSocketFactory  )  ; 
} 
} 
} 
connection  .  setDoOutput  (  true  )  ; 
if  (  contentType  !=  null  )  { 
connection  .  setRequestProperty  (  "Content-Type"  ,  contentType  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Unable to connect to server"  ,  e  )  ; 
} 
log  .  trace  (  "Leaving getConnection"  )  ; 
if  (  connection  ==  null  )  { 
throw   new   RuntimeException  (  "Attempted to return a null connection"  )  ; 
} 
return   connection  ; 
} 








private   String   readResponse  (  final   HttpURLConnection   connection  )  { 
log  .  trace  (  "Entering readResponse"  )  ; 
BufferedReader   in  =  null  ; 
String   response  =  null  ; 
try  { 
in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
}  catch  (  IOException   e  )  { 
in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getErrorStream  (  )  )  )  ; 
if  (  in  !=  null  )  { 
sendErrorMessage  =  readResponse  (  in  )  ; 
} 
log  .  trace  (  "Leaving readResponse with null"  )  ; 
return   null  ; 
} 
try  { 
response  =  readResponse  (  in  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Error closing connection"  ,  e  )  ; 
} 
} 
} 
log  .  trace  (  "Leaving readResponse"  )  ; 
return   response  ; 
} 








private   String   readResponse  (  final   BufferedReader   in  )  { 
StringBuffer   response  =  new   StringBuffer  (  )  ; 
try  { 
char  [  ]  line  =  new   char  [  READ_BUFFER_SIZE  ]  ; 
int   read  =  0  ; 
while  (  (  read  =  in  .  read  (  line  )  )  !=  -  1  )  { 
response  .  append  (  line  ,  0  ,  read  )  ; 
} 
}  catch  (  IOException   e  )  { 
log  .  error  (  "Error reading data from the server"  ,  e  )  ; 
throw   new   RuntimeException  (  "Unable to read response"  ,  e  )  ; 
} 
return   response  .  toString  (  )  ; 
} 







private   void   writeRequest  (  final   HttpURLConnection   connection  ,  final   String   message  )  { 
PrintWriter   out  =  null  ; 
try  { 
out  =  new   PrintWriter  (  connection  .  getOutputStream  (  )  )  ; 
out  .  write  (  message  )  ; 
out  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error sending data to the client"  ,  e  )  ; 
throw   new   RuntimeException  (  "Unable to send message"  ,  e  )  ; 
}  finally  { 
if  (  out  !=  null  )  { 
out  .  close  (  )  ; 
} 
} 
} 
} 

