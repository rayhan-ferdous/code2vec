package   org  .  tanso  .  fountain  .  util  .  net  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   org  .  apache  .  commons  .  net  .  ftp  .  FTPClient  ; 
import   org  .  apache  .  commons  .  net  .  ftp  .  FTPFile  ; 
import   org  .  apache  .  commons  .  net  .  ftp  .  FTPReply  ; 








public   class   FTPClientUtil  { 

public   static   class   Configuration  { 

public   static   String   RemoteDownPath  =  ""  ; 

public   static   String   RemoteUpPath  =  ""  ; 

public   static   String   LocalDownPath  =  ""  ; 

public   static   String   LocalUpPath  =  ""  ; 

public   static   String   FtpServer  =  ""  ; 

public   static   String   FtpUser  =  ""  ; 

public   static   String   FtpPassword  =  ""  ; 
} 

private   String   server  ; 

private   String   username  ; 

private   String   password  ; 

private   int   port  =  21  ; 

private   FTPClient   ftp  ; 

private   boolean   binaryTransfer  =  true  ; 











public   FTPClientUtil  (  String   server  ,  int   port  ,  String   username  ,  String   password  )  { 
this  .  server  =  server  ; 
this  .  username  =  username  ; 
this  .  password  =  password  ; 
this  .  port  =  port  ; 
ftp  =  new   FTPClient  (  )  ; 
} 









public   FTPClientUtil  (  String   server  ,  String   username  ,  String   password  )  { 
this  (  server  ,  21  ,  username  ,  password  )  ; 
} 




public   FTPClientUtil  (  )  { 
this  (  Configuration  .  FtpServer  ,  Configuration  .  FtpUser  ,  Configuration  .  FtpPassword  )  ; 
} 






public   boolean   connect  (  )  { 
try  { 
int   reply  ; 
ftp  .  connect  (  server  ,  port  )  ; 
reply  =  ftp  .  getReplyCode  (  )  ; 
if  (  FTPReply  .  isPositiveCompletion  (  reply  )  )  { 
if  (  ftp  .  login  (  username  ,  password  )  )  { 
ftp  .  enterLocalPassiveMode  (  )  ; 
return   true  ; 
} 
}  else  { 
ftp  .  disconnect  (  )  ; 
System  .  out  .  println  (  "FTP server refused connection."  )  ; 
} 
}  catch  (  IOException   e  )  { 
if  (  ftp  .  isConnected  (  )  )  { 
try  { 
ftp  .  disconnect  (  )  ; 
}  catch  (  IOException   f  )  { 
} 
} 
System  .  out  .  println  (  "Could not connect to server."  )  ; 
} 
return   false  ; 
} 










public   boolean   get  (  String   fileName  ,  boolean   delFile  )  { 
String   remote  =  Configuration  .  RemoteDownPath  +  fileName  ; 
String   local  =  Configuration  .  LocalDownPath  +  fileName  ; 
return   get  (  remote  ,  local  ,  delFile  )  ; 
} 










public   boolean   put  (  String   fileName  ,  boolean   delFile  )  { 
String   remote  =  Configuration  .  RemoteUpPath  +  fileName  ; 
String   local  =  Configuration  .  LocalUpPath  +  fileName  ; 
return   put  (  remote  ,  local  ,  delFile  )  ; 
} 










public   boolean  [  ]  put  (  String  [  ]  fileNames  ,  boolean   delFile  )  { 
boolean  [  ]  result  =  new   boolean  [  fileNames  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  result  .  length  ;  j  ++  )  { 
result  [  j  ]  =  false  ; 
} 
String   localFile  ; 
for  (  int   i  =  0  ;  i  <  fileNames  .  length  ;  i  ++  )  { 
localFile  =  fileNames  [  i  ]  ; 
result  [  i  ]  =  put  (  localFile  ,  delFile  )  ; 
} 
return   result  ; 
} 












public   boolean   put  (  String   remoteAbsoluteFile  ,  String   localAbsoluteFile  ,  boolean   delFile  )  { 
InputStream   input  =  null  ; 
try  { 
if  (  binaryTransfer  )  { 
ftp  .  setFileType  (  FTPClient  .  BINARY_FILE_TYPE  )  ; 
}  else  { 
ftp  .  setFileType  (  FTPClient  .  ASCII_FILE_TYPE  )  ; 
} 
input  =  new   FileInputStream  (  localAbsoluteFile  )  ; 
ftp  .  mkd  (  retrivePath  (  remoteAbsoluteFile  )  )  ; 
if  (  ftp  .  storeFile  (  remoteAbsoluteFile  ,  input  )  )  { 
System  .  out  .  println  (  "put "  +  localAbsoluteFile  )  ; 
input  .  close  (  )  ; 
if  (  delFile  )  { 
(  new   File  (  localAbsoluteFile  )  )  .  delete  (  )  ; 
System  .  out  .  println  (  "delete "  +  localAbsoluteFile  )  ; 
} 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
System  .  out  .  println  (  "local file not found."  )  ; 
}  catch  (  IOException   e1  )  { 
System  .  out  .  println  (  "Could put file to server."  )  ; 
}  finally  { 
try  { 
if  (  input  !=  null  )  { 
input  .  close  (  )  ; 
} 
}  catch  (  Exception   e2  )  { 
} 
} 
return   false  ; 
} 












public   boolean   get  (  String   remoteAbsoluteFile  ,  String   localAbsoluteFile  ,  boolean   delFile  )  { 
OutputStream   output  =  null  ; 
try  { 
if  (  binaryTransfer  )  { 
ftp  .  setFileType  (  FTPClient  .  BINARY_FILE_TYPE  )  ; 
}  else  { 
ftp  .  setFileType  (  FTPClient  .  ASCII_FILE_TYPE  )  ; 
} 
output  =  new   FileOutputStream  (  localAbsoluteFile  )  ; 
if  (  ftp  .  retrieveFile  (  remoteAbsoluteFile  ,  output  )  )  { 
output  .  close  (  )  ; 
if  (  delFile  )  { 
ftp  .  deleteFile  (  remoteAbsoluteFile  )  ; 
} 
return   true  ; 
}  else  { 
return   false  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
System  .  out  .  println  (  "local file not found."  )  ; 
return   false  ; 
}  catch  (  IOException   e1  )  { 
System  .  out  .  println  (  "Could get file from server."  )  ; 
return   false  ; 
}  finally  { 
try  { 
if  (  output  !=  null  )  { 
output  .  close  (  )  ; 
} 
}  catch  (  IOException   e2  )  { 
} 
} 
} 









public   String  [  ]  listNames  (  String   remotePath  )  { 
String  [  ]  fileNames  =  null  ; 
try  { 
FTPFile  [  ]  remotefiles  =  ftp  .  listFiles  (  remotePath  )  ; 
fileNames  =  new   String  [  remotefiles  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  remotefiles  .  length  ;  i  ++  )  { 
fileNames  [  i  ]  =  remotefiles  [  i  ]  .  getName  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Could not list file from server."  )  ; 
} 
return   fileNames  ; 
} 




public   void   disconnect  (  )  { 
try  { 
ftp  .  logout  (  )  ; 
if  (  ftp  .  isConnected  (  )  )  { 
ftp  .  disconnect  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Could not disconnect from server."  )  ; 
} 
} 




public   boolean   isBinaryTransfer  (  )  { 
return   binaryTransfer  ; 
} 





public   void   setBinaryTransfer  (  boolean   binaryTransfer  )  { 
this  .  binaryTransfer  =  binaryTransfer  ; 
} 








private   String   retrivePath  (  String   filename  )  { 
int   pathIndex  =  filename  .  lastIndexOf  (  '/'  )  ; 
if  (  pathIndex  !=  -  1  )  { 
return   filename  .  substring  (  0  ,  pathIndex  )  ; 
}  else  { 
return  ""  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
FTPClientUtil   ftp  =  new   FTPClientUtil  (  "127.0.0.1"  ,  "admin"  ,  "admin"  )  ; 
ftp  .  connect  (  )  ; 
String  [  ]  temp  =  ftp  .  listNames  (  "/"  )  ; 
System  .  out  .  println  (  "connect sucess"  )  ; 
System  .  out  .  println  (  temp  .  length  )  ; 
for  (  int   i  =  0  ,  len  =  temp  .  length  ;  i  <  len  ;  i  ++  )  { 
System  .  out  .  println  (  temp  [  i  ]  )  ; 
} 
System  .  out  .  println  (  "Upload "  +  ftp  .  put  (  "/bupt/loopftp.bat"  ,  "loop.bat"  ,  false  )  )  ; 
ftp  .  disconnect  (  )  ; 
} 
} 

