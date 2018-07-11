package   rath  .  msnm  .  ftp  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  ServerSocket  ; 
import   rath  .  msnm  .  MSNMessenger  ; 
import   rath  .  msnm  .  msg  .  FileTransferMessage  ; 








public   class   VolatileTransferServer   extends   Thread   implements   VolatileTransfer  { 

String   cookie  ; 

private   final   MSNMessenger   msn  ; 

private   boolean   isLive  =  true  ; 

private   int   port  ; 

private   String   authCookie  =  null  ; 

private   String   peerLoginName  =  null  ; 

private   File   file  =  null  ; 

private   int   filesize  =  0  ; 

private   volatile   int   offset  =  0  ; 

private   ServerSocket   serverSocket  =  null  ; 

private   OutputStream   out  =  null  ; 

private   Socket   socket  =  null  ; 

private   BufferedReader   in  =  null  ; 

private   Thread   binaryThread  =  null  ; 

private   VolatileTransferServer  (  MSNMessenger   msn  )  { 
this  .  msn  =  msn  ; 
} 




public   static   VolatileTransferServer   getInstance  (  MSNMessenger   msn  ,  ToSendFile   tosend  ,  FileTransferMessage   msg  )  throws   FileNotFoundException  { 
int   port  =  Integer  .  parseInt  (  msg  .  getProperty  (  "Port"  )  )  ; 
String   authCookie  =  msg  .  getProperty  (  "AuthCookie"  )  ; 
String   loginName  =  tosend  .  getReceiverName  (  )  ; 
File   file  =  tosend  .  getFile  (  )  ; 
if  (  !  file  .  exists  (  )  )  throw   new   FileNotFoundException  (  file  .  getAbsolutePath  (  )  )  ; 
VolatileTransferServer   vts  =  new   VolatileTransferServer  (  msn  )  ; 
vts  .  port  =  port  ; 
vts  .  authCookie  =  authCookie  ; 
vts  .  peerLoginName  =  loginName  ; 
vts  .  file  =  file  ; 
vts  .  filesize  =  (  int  )  file  .  length  (  )  ; 
return   vts  ; 
} 




public   int   getPort  (  )  { 
return   this  .  port  ; 
} 




public   String   getReceiverName  (  )  { 
return   this  .  peerLoginName  ; 
} 




public   String   getAuthCookie  (  )  { 
return   this  .  authCookie  ; 
} 

public   String   getCookie  (  )  { 
return   this  .  cookie  ; 
} 




public   File   getFile  (  )  { 
return   this  .  file  ; 
} 

public   String   getFilename  (  )  { 
return   this  .  file  .  getName  (  )  ; 
} 




public   int   getPostedLength  (  )  { 
return   this  .  offset  ; 
} 

public   int   getCommitPercent  (  )  { 
return  (  int  )  (  (  (  double  )  offset  /  (  double  )  filesize  )  *  100.0D  )  ; 
} 




public   final   void   run  (  )  { 
try  { 
fireStart  (  )  ; 
makeConnection  (  )  ; 
while  (  isLive  )  { 
String   line  =  in  .  readLine  (  )  ; 
if  (  line  ==  null  )  break  ; 
String   header  =  line  .  substring  (  0  ,  3  )  ; 
String   body  =  ""  ; 
if  (  line  .  length  (  )  >  4  )  body  =  line  .  substring  (  4  )  ; 
processMessage  (  header  ,  body  )  ; 
} 
}  catch  (  Throwable   e  )  { 
fireError  (  e  )  ; 
}  finally  { 
if  (  binaryThread  !=  null  )  binaryThread  .  interrupt  (  )  ; 
close  (  )  ; 
fireEnd  (  )  ; 
} 
} 

public   void   processMessage  (  String   header  ,  String   body  )  throws   Throwable  { 
if  (  header  .  equals  (  "VER"  )  )  { 
sendMessage  (  header  ,  body  )  ; 
}  else   if  (  header  .  equals  (  "USR"  )  )  { 
int   i0  =  body  .  indexOf  (  ' '  )  ; 
if  (  i0  ==  -  1  )  { 
close  (  )  ; 
return  ; 
} 
String   loginName  =  body  .  substring  (  0  ,  i0  )  ; 
String   authCookie  =  body  .  substring  (  i0  +  1  )  ; 
if  (  !  loginName  .  equals  (  peerLoginName  )  ||  !  authCookie  .  equals  (  this  .  authCookie  )  )  { 
close  (  )  ; 
return  ; 
} 
sendMessage  (  "FIL"  ,  String  .  valueOf  (  file  .  length  (  )  )  )  ; 
}  else   if  (  header  .  equals  (  "TFR"  )  )  { 
binaryThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
sendFileContent  (  )  ; 
}  catch  (  Throwable   e  )  { 
fireError  (  e  )  ; 
} 
} 
}  )  ; 
binaryThread  .  start  (  )  ; 
}  else   if  (  header  .  equals  (  "CCL"  )  )  { 
if  (  binaryThread  !=  null  )  { 
binaryThread  .  interrupt  (  )  ; 
binaryThread  =  null  ; 
isLive  =  false  ; 
} 
}  else   if  (  header  .  equals  (  "BYE"  )  )  { 
isLive  =  false  ; 
} 
} 

public   void   sendMessage  (  String   header  ,  String   body  )  throws   IOException  { 
StringBuffer   sb  =  new   StringBuffer  (  40  )  ; 
sb  .  append  (  header  )  ; 
sb  .  append  (  ' '  )  ; 
sb  .  append  (  body  )  ; 
sb  .  append  (  "\r\n"  )  ; 
out  .  write  (  sb  .  toString  (  )  .  getBytes  (  )  )  ; 
out  .  flush  (  )  ; 
} 

public   void   sendFileContent  (  )  throws   IOException  ,  InterruptedException  { 
int   filesize  =  (  int  )  file  .  length  (  )  ; 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
InputStream   in  =  null  ; 
if  (  this  .  file   instanceof   StreamingFile  )  in  =  (  (  StreamingFile  )  this  .  file  )  .  getInputStream  (  )  ;  else   in  =  new   FileInputStream  (  this  .  file  )  ; 
Thread   currentThread  =  Thread  .  currentThread  (  )  ; 
int   readlen  ; 
while  (  (  readlen  =  in  .  read  (  buf  ,  3  ,  2045  )  )  >  0  )  { 
buf  [  0  ]  =  0  ; 
buf  [  1  ]  =  (  byte  )  (  (  readlen  >  >  0  )  &  0xff  )  ; 
buf  [  2  ]  =  (  byte  )  (  (  readlen  >  >  8  )  &  0xff  )  ; 
out  .  write  (  buf  ,  0  ,  readlen  +  3  )  ; 
offset  +=  readlen  ; 
out  .  flush  (  )  ; 
if  (  currentThread  .  isInterrupted  (  )  )  { 
in  .  close  (  )  ; 
throw   new   InterruptedException  (  "thread interrupted"  )  ; 
} 
} 
in  .  close  (  )  ; 
out  .  write  (  0  )  ; 
out  .  write  (  0  )  ; 
out  .  write  (  0  )  ; 
} 

public   void   close  (  )  { 
isLive  =  false  ; 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  out  !=  null  )  { 
try  { 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  socket  !=  null  )  { 
try  { 
socket  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  serverSocket  !=  null  )  { 
try  { 
serverSocket  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 

protected   void   fireStart  (  )  { 
msn  .  fireFileSendStartedEvent  (  this  )  ; 
} 





protected   void   fireError  (  Throwable   e  )  { 
msn  .  fireFileSendErrorEvent  (  this  ,  e  )  ; 
} 

protected   void   fireEnd  (  )  { 
msn  .  fireFileSendEndedEvent  (  this  )  ; 
} 





protected   void   makeConnection  (  )  throws   IOException  { 
this  .  serverSocket  =  new   ServerSocket  (  this  .  port  ,  1  )  ; 
this  .  serverSocket  .  setSoTimeout  (  30000  )  ; 
this  .  socket  =  serverSocket  .  accept  (  )  ; 
this  .  out  =  socket  .  getOutputStream  (  )  ; 
this  .  in  =  new   BufferedReader  (  new   InputStreamReader  (  socket  .  getInputStream  (  )  )  )  ; 
} 
} 

