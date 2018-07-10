package   edu  .  lcmi  .  grouppac  .  http  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  ProtocolException  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  util  .  StringTokenizer  ; 
import   edu  .  lcmi  .  grouppac  .  GroupPacComponent  ; 
import   edu  .  lcmi  .  grouppac  .  util  .  Debug  ; 














public   class   WebServer   implements   GroupPacComponent  { 

private   int   backlog  =  5  ; 

private   String   htdocs  =  "."  ; 

private   int   port  =  80  ; 

private   volatile   boolean   running  =  true  ; 

class   RequestThread   implements   Runnable  { 

private   Socket   clientSocket  ; 






public   RequestThread  (  Socket   s  )  { 
clientSocket  =  s  ; 
} 




public   void   run  (  )  { 
try  { 
HTTPrequest   req  =  getRequest  (  clientSocket  )  ; 
implementMethod  (  req  )  ; 
}  catch  (  IOException   ioe  )  { 
Debug  .  output  (  3  ,  ioe  )  ; 
} 
} 
} 







public   WebServer  (  String  [  ]  args  )  { 
parseArgs  (  args  )  ; 
} 









public   HTTPrequest   getRequest  (  Socket   client  )  throws   IOException  ,  ProtocolException  { 
BufferedReader   inbound  =  null  ; 
HTTPrequest   request  =  null  ; 
try  { 
inbound  =  new   BufferedReader  (  new   InputStreamReader  (  client  .  getInputStream  (  )  )  )  ; 
String   reqhdr  =  readHeader  (  inbound  )  ; 
request  =  parseReqHdr  (  reqhdr  )  ; 
request  .  clientSocket  =  client  ; 
request  .  inbound  =  inbound  ; 
}  catch  (  ProtocolException   pe  )  { 
if  (  inbound  !=  null  )  inbound  .  close  (  )  ; 
throw   pe  ; 
}  catch  (  IOException   ioe  )  { 
if  (  inbound  !=  null  )  inbound  .  close  (  )  ; 
throw   ioe  ; 
} 
return   request  ; 
} 






public   void   run  (  Object   notificator  )  { 
ServerSocket   serverSocket  =  null  ; 
Socket   clientSocket  =  null  ; 
try  { 
serverSocket  =  new   ServerSocket  (  port  ,  backlog  )  ; 
Debug  .  output  (  1  ,  "HTTP Server up"  )  ; 
synchronized  (  notificator  )  { 
notificator  .  notifyAll  (  )  ; 
} 
while  (  running  )  { 
try  { 
clientSocket  =  serverSocket  .  accept  (  )  ; 
RequestThread   rt  =  new   RequestThread  (  clientSocket  )  ; 
Thread   t  =  new   Thread  (  rt  ,  "HTTP Request Processor"  )  ; 
t  .  start  (  )  ; 
}  catch  (  Exception   ioe  )  { 
Debug  .  output  (  1  ,  ioe  )  ; 
} 
} 
serverSocket  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
Debug  .  output  (  1  ,  ioe  )  ; 
Debug  .  output  (  1  ,  "HTTP: IOException: Server port could not be opened."  )  ; 
}  finally  { 
synchronized  (  notificator  )  { 
notificator  .  notifyAll  (  )  ; 
} 
} 
} 







protected   void   implementMethod  (  HTTPrequest   request  )  throws   ProtocolException  { 
try  { 
edu  .  lcmi  .  grouppac  .  util  .  Debug  .  output  (  4  ,  "HTTP: Servicing: "  +  request  )  ; 
if  (  (  request  .  method  .  equals  (  "GET"  )  )  ||  (  request  .  method  .  equals  (  "HEAD"  )  )  )  serviceGetRequest  (  request  )  ;  else   throw   new   ProtocolException  (  "Unimplemented method: "  +  request  .  method  )  ; 
}  catch  (  ProtocolException   pe  )  { 
sendNegativeResponse  (  request  ,  pe  )  ; 
} 
} 









protected   HTTPrequest   parseReqHdr  (  String   reqhdr  )  throws   IOException  ,  ProtocolException  { 
HTTPrequest   req  =  new   HTTPrequest  (  )  ; 
StringTokenizer   lines  =  new   StringTokenizer  (  reqhdr  ,  "\r\n"  )  ; 
String   currentLine  =  lines  .  nextToken  (  )  ; 
StringTokenizer   members  =  new   StringTokenizer  (  currentLine  ,  " \t"  )  ; 
req  .  method  =  members  .  nextToken  (  )  ; 
req  .  file  =  members  .  nextToken  (  )  ; 
if  (  req  .  file  .  equals  (  "/"  )  )  req  .  file  =  "/index.html"  ; 
req  .  version  =  members  .  nextToken  (  )  ; 
while  (  lines  .  hasMoreTokens  (  )  )  { 
String   line  =  lines  .  nextToken  (  )  ; 
int   slice  =  line  .  indexOf  (  ':'  )  ; 
if  (  slice  ==  -  1  )  throw   new   ProtocolException  (  "Invalid HTTP header: "  +  line  )  ;  else  { 
String   name  =  line  .  substring  (  0  ,  slice  )  .  trim  (  )  ; 
String   value  =  line  .  substring  (  slice  +  1  )  .  trim  (  )  ; 
req  .  addNameValue  (  name  ,  value  )  ; 
} 
} 
return   req  ; 
} 









protected   String   readHeader  (  BufferedReader   is  )  throws   IOException  ,  ProtocolException  { 
String   command  ; 
String   line  ; 
if  (  (  command  =  is  .  readLine  (  )  )  ==  null  )  command  =  ""  ; 
command  +=  "\n"  ; 
if  (  command  .  indexOf  (  "HTTP/"  )  !=  -  1  )  { 
while  (  (  (  line  =  is  .  readLine  (  )  )  !=  null  )  &&  !  line  .  equals  (  ""  )  )  command  +=  (  line  +  "\n"  )  ; 
}  else   throw   new   ProtocolException  (  "Pre HTTP/1.0 request"  )  ; 
return   command  ; 
} 






protected   void   sendNegativeResponse  (  HTTPrequest   request  )  { 
sendNegativeResponse  (  request  ,  null  )  ; 
} 







protected   void   sendNegativeResponse  (  HTTPrequest   request  ,  Exception   pe  )  { 
PrintStream   outbound  =  null  ; 
try  { 
outbound  =  new   PrintStream  (  request  .  clientSocket  .  getOutputStream  (  )  ,  true  )  ; 
outbound  .  print  (  "HTTP/1.0 "  )  ; 
outbound  .  print  (  "404 NOT_FOUND\r\n"  )  ; 
outbound  .  print  (  "\r\n"  )  ; 
outbound  .  print  (  "404 - File not found."  )  ; 
if  (  pe  !=  null  )  { 
outbound  .  print  (  "<br>"  )  ; 
pe  .  printStackTrace  (  outbound  )  ; 
} 
outbound  .  close  (  )  ; 
request  .  inbound  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
edu  .  lcmi  .  grouppac  .  util  .  Debug  .  output  (  3  ,  ioe  )  ; 
} 
} 








protected   void   serviceGetRequest  (  HTTPrequest   request  )  throws   ProtocolException  { 
try  { 
if  (  request  .  file  .  indexOf  (  ".."  )  !=  -  1  )  throw   new   ProtocolException  (  "Relative paths not supported"  )  ; 
String   fileToGet  =  htdocs  +  request  .  file  ; 
FileInputStream   inFile  =  new   FileInputStream  (  fileToGet  )  ; 
Debug  .  output  (  4  ,  "HTTP: Sending file "  +  fileToGet  +  " "  +  inFile  .  available  (  )  +  " Bytes"  )  ; 
sendFile  (  request  ,  inFile  )  ; 
inFile  .  close  (  )  ; 
}  catch  (  FileNotFoundException   fnf  )  { 
sendNegativeResponse  (  request  ,  fnf  )  ; 
}  catch  (  ProtocolException   pe  )  { 
sendNegativeResponse  (  request  ,  pe  )  ; 
}  catch  (  IOException   ioe  )  { 
sendNegativeResponse  (  request  ,  ioe  )  ; 
} 
} 




private   void   help  (  )  { 
String   a  =  "WebServer is a basic http server\n\n"  ; 
a  +=  " -htport    <int>     Set the server port.\n"  ; 
a  +=  " -htbacklog <int>     Set the number of connections to hold before refusing new ones.\n"  ; 
a  +=  " -htdocs    <folder>  Document root.\n"  ; 
System  .  out  .  println  (  a  )  ; 
} 






private   void   parseArgs  (  String  [  ]  args  )  { 
if  (  args  ==  null  )  return  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  toLowerCase  (  )  .  equals  (  "-htport"  )  )  { 
try  { 
port  =  Integer  .  parseInt  (  args  [  i  +  1  ]  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
}  else   if  (  args  [  i  ]  .  toLowerCase  (  )  .  equals  (  "-htbacklog"  )  )  { 
try  { 
backlog  =  Integer  .  parseInt  (  args  [  i  +  1  ]  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
}  else   if  (  args  [  i  ]  .  toLowerCase  (  )  .  equals  (  "-htdocs"  )  )  htdocs  =  args  [  i  +  1  ]  ;  else   if  (  args  [  i  ]  .  toLowerCase  (  )  .  equals  (  "-h"  )  )  help  (  )  ; 
} 
} 







private   void   sendFile  (  HTTPrequest   request  ,  InputStream   inFile  )  { 
PrintStream   outbound  =  null  ; 
try  { 
outbound  =  new   PrintStream  (  request  .  clientSocket  .  getOutputStream  (  )  ,  true  )  ; 
outbound  .  print  (  "HTTP/1.0 200 OK\r\n"  )  ; 
outbound  .  print  (  "Content-type: text/html\r\n"  )  ; 
outbound  .  print  (  "Content-Length: "  +  inFile  .  available  (  )  +  "\r\n"  )  ; 
outbound  .  print  (  "\r\n"  )  ; 
try  { 
Thread  .  sleep  (  500  )  ; 
}  catch  (  InterruptedException   ie  )  { 
} 
if  (  !  request  .  method  .  equals  (  "HEAD"  )  )  { 
byte  [  ]  dataBody  =  new   byte  [  1024  ]  ; 
while  (  inFile  .  read  (  dataBody  )  !=  -  1  )  outbound  .  write  (  dataBody  )  ; 
} 
outbound  .  close  (  )  ; 
request  .  inbound  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
Debug  .  output  (  3  ,  ioe  )  ; 
} 
} 
} 

