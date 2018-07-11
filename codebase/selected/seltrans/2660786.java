package   javax  .  activation  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PipedInputStream  ; 
import   java  .  io  .  PipedOutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  net  .  URL  ; 
import   datatransfer  .  DataFlavor  ; 
import   datatransfer  .  Transferable  ; 
import   datatransfer  .  UnsupportedFlavorException  ; 




































public   class   DataHandler   implements   Transferable  { 

private   DataSource   dataSource  =  null  ; 

private   DataSource   objDataSource  =  null  ; 

private   Object   object  =  null  ; 

private   String   objectMimeType  =  null  ; 

private   CommandMap   currentCommandMap  =  null  ; 

private   static   final   DataFlavor   emptyFlavors  [  ]  =  new   DataFlavor  [  0  ]  ; 

private   DataFlavor   transferFlavors  [  ]  =  emptyFlavors  ; 

private   DataContentHandler   dataContentHandler  =  null  ; 

private   DataContentHandler   factoryDCH  =  null  ; 

private   static   DataContentHandlerFactory   factory  =  null  ; 

private   DataContentHandlerFactory   oldFactory  =  null  ; 

private   String   shortType  =  null  ; 








public   DataHandler  (  DataSource   ds  )  { 
dataSource  =  ds  ; 
oldFactory  =  factory  ; 
} 










public   DataHandler  (  Object   obj  ,  String   mimeType  )  { 
object  =  obj  ; 
objectMimeType  =  mimeType  ; 
oldFactory  =  factory  ; 
} 








public   DataHandler  (  URL   url  )  { 
dataSource  =  new   URLDataSource  (  url  )  ; 
oldFactory  =  factory  ; 
} 




private   synchronized   CommandMap   getCommandMap  (  )  { 
if  (  currentCommandMap  !=  null  )  return   currentCommandMap  ;  else   return   CommandMap  .  getDefaultCommandMap  (  )  ; 
} 















public   DataSource   getDataSource  (  )  { 
if  (  dataSource  ==  null  )  { 
if  (  objDataSource  ==  null  )  objDataSource  =  new   DataHandlerDataSource  (  this  )  ; 
return   objDataSource  ; 
} 
return   dataSource  ; 
} 









public   String   getName  (  )  { 
if  (  dataSource  !=  null  )  return   dataSource  .  getName  (  )  ;  else   return   null  ; 
} 








public   String   getContentType  (  )  { 
if  (  dataSource  !=  null  )  return   dataSource  .  getContentType  (  )  ;  else   return   objectMimeType  ; 
} 

























public   InputStream   getInputStream  (  )  throws   IOException  { 
InputStream   ins  =  null  ; 
if  (  dataSource  !=  null  )  { 
ins  =  dataSource  .  getInputStream  (  )  ; 
}  else  { 
DataContentHandler   dch  =  getDataContentHandler  (  )  ; 
if  (  dch  ==  null  )  throw   new   UnsupportedDataTypeException  (  "no DCH for MIME type "  +  getBaseType  (  )  )  ; 
if  (  dch   instanceof   ObjectDataContentHandler  )  { 
if  (  (  (  ObjectDataContentHandler  )  dch  )  .  getDCH  (  )  ==  null  )  throw   new   UnsupportedDataTypeException  (  "no object DCH for MIME type "  +  getBaseType  (  )  )  ; 
} 
final   DataContentHandler   fdch  =  dch  ; 
final   PipedOutputStream   pos  =  new   PipedOutputStream  (  )  ; 
PipedInputStream   pin  =  new   PipedInputStream  (  pos  )  ; 
new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
fdch  .  writeTo  (  object  ,  objectMimeType  ,  pos  )  ; 
}  catch  (  IOException   e  )  { 
}  finally  { 
try  { 
pos  .  close  (  )  ; 
}  catch  (  IOException   ie  )  { 
} 
} 
} 
}  ,  "DataHandler.getInputStream"  )  .  start  (  )  ; 
ins  =  pin  ; 
} 
return   ins  ; 
} 
















public   void   writeTo  (  OutputStream   os  )  throws   IOException  { 
if  (  dataSource  !=  null  )  { 
InputStream   is  =  null  ; 
byte   data  [  ]  =  new   byte  [  8  *  1024  ]  ; 
int   bytes_read  ; 
is  =  dataSource  .  getInputStream  (  )  ; 
try  { 
while  (  (  bytes_read  =  is  .  read  (  data  )  )  >  0  )  { 
os  .  write  (  data  ,  0  ,  bytes_read  )  ; 
} 
}  finally  { 
is  .  close  (  )  ; 
is  =  null  ; 
} 
}  else  { 
DataContentHandler   dch  =  getDataContentHandler  (  )  ; 
dch  .  writeTo  (  object  ,  objectMimeType  ,  os  )  ; 
} 
} 













public   OutputStream   getOutputStream  (  )  throws   IOException  { 
if  (  dataSource  !=  null  )  return   dataSource  .  getOutputStream  (  )  ;  else   return   null  ; 
} 

























public   synchronized   DataFlavor  [  ]  getTransferDataFlavors  (  )  { 
if  (  factory  !=  oldFactory  )  transferFlavors  =  emptyFlavors  ; 
if  (  transferFlavors  ==  emptyFlavors  )  transferFlavors  =  getDataContentHandler  (  )  .  getTransferDataFlavors  (  )  ; 
return   transferFlavors  ; 
} 













public   boolean   isDataFlavorSupported  (  DataFlavor   flavor  )  { 
DataFlavor  [  ]  lFlavors  =  getTransferDataFlavors  (  )  ; 
for  (  int   i  =  0  ;  i  <  lFlavors  .  length  ;  i  ++  )  { 
if  (  lFlavors  [  i  ]  .  equals  (  flavor  )  )  return   true  ; 
} 
return   false  ; 
} 


































public   Object   getTransferData  (  DataFlavor   flavor  )  throws   UnsupportedFlavorException  ,  IOException  { 
return   getDataContentHandler  (  )  .  getTransferData  (  flavor  ,  dataSource  )  ; 
} 













public   synchronized   void   setCommandMap  (  CommandMap   commandMap  )  { 
if  (  commandMap  !=  currentCommandMap  ||  commandMap  ==  null  )  { 
transferFlavors  =  emptyFlavors  ; 
dataContentHandler  =  null  ; 
currentCommandMap  =  commandMap  ; 
} 
} 














public   CommandInfo  [  ]  getPreferredCommands  (  )  { 
if  (  dataSource  !=  null  )  return   getCommandMap  (  )  .  getPreferredCommands  (  getBaseType  (  )  ,  dataSource  )  ;  else   return   getCommandMap  (  )  .  getPreferredCommands  (  getBaseType  (  )  )  ; 
} 













public   CommandInfo  [  ]  getAllCommands  (  )  { 
if  (  dataSource  !=  null  )  return   getCommandMap  (  )  .  getAllCommands  (  getBaseType  (  )  ,  dataSource  )  ;  else   return   getCommandMap  (  )  .  getAllCommands  (  getBaseType  (  )  )  ; 
} 













public   CommandInfo   getCommand  (  String   cmdName  )  { 
if  (  dataSource  !=  null  )  return   getCommandMap  (  )  .  getCommand  (  getBaseType  (  )  ,  cmdName  ,  dataSource  )  ;  else   return   getCommandMap  (  )  .  getCommand  (  getBaseType  (  )  ,  cmdName  )  ; 
} 


















public   Object   getContent  (  )  throws   IOException  { 
if  (  object  !=  null  )  return   object  ;  else   return   getDataContentHandler  (  )  .  getContent  (  getDataSource  (  )  )  ; 
} 













public   Object   getBean  (  CommandInfo   cmdinfo  )  { 
Object   bean  =  null  ; 
try  { 
ClassLoader   cld  =  null  ; 
cld  =  SecuritySupport  .  getContextClassLoader  (  )  ; 
if  (  cld  ==  null  )  cld  =  this  .  getClass  (  )  .  getClassLoader  (  )  ; 
bean  =  cmdinfo  .  getCommandObject  (  this  ,  cld  )  ; 
}  catch  (  IOException   e  )  { 
}  catch  (  ClassNotFoundException   e  )  { 
} 
return   bean  ; 
} 


















private   synchronized   DataContentHandler   getDataContentHandler  (  )  { 
if  (  factory  !=  oldFactory  )  { 
oldFactory  =  factory  ; 
factoryDCH  =  null  ; 
dataContentHandler  =  null  ; 
transferFlavors  =  emptyFlavors  ; 
} 
if  (  dataContentHandler  !=  null  )  return   dataContentHandler  ; 
String   simpleMT  =  getBaseType  (  )  ; 
if  (  factoryDCH  ==  null  &&  factory  !=  null  )  factoryDCH  =  factory  .  createDataContentHandler  (  simpleMT  )  ; 
if  (  factoryDCH  !=  null  )  dataContentHandler  =  factoryDCH  ; 
if  (  dataContentHandler  ==  null  )  { 
if  (  dataSource  !=  null  )  dataContentHandler  =  getCommandMap  (  )  .  createDataContentHandler  (  simpleMT  ,  dataSource  )  ;  else   dataContentHandler  =  getCommandMap  (  )  .  createDataContentHandler  (  simpleMT  )  ; 
} 
if  (  dataSource  !=  null  )  dataContentHandler  =  new   DataSourceDataContentHandler  (  dataContentHandler  ,  dataSource  )  ;  else   dataContentHandler  =  new   ObjectDataContentHandler  (  dataContentHandler  ,  object  ,  objectMimeType  )  ; 
return   dataContentHandler  ; 
} 





private   synchronized   String   getBaseType  (  )  { 
if  (  shortType  ==  null  )  { 
String   ct  =  getContentType  (  )  ; 
try  { 
MimeType   mt  =  new   MimeType  (  ct  )  ; 
shortType  =  mt  .  getBaseType  (  )  ; 
}  catch  (  MimeTypeParseException   e  )  { 
shortType  =  ct  ; 
} 
} 
return   shortType  ; 
} 














public   static   synchronized   void   setDataContentHandlerFactory  (  DataContentHandlerFactory   newFactory  )  { 
if  (  factory  !=  null  )  throw   new   Error  (  "DataContentHandlerFactory already defined"  )  ; 
SecurityManager   security  =  System  .  getSecurityManager  (  )  ; 
if  (  security  !=  null  )  { 
try  { 
security  .  checkSetFactory  (  )  ; 
}  catch  (  SecurityException   ex  )  { 
if  (  DataHandler  .  class  .  getClassLoader  (  )  !=  newFactory  .  getClass  (  )  .  getClassLoader  (  )  )  throw   ex  ; 
} 
} 
factory  =  newFactory  ; 
} 
} 






class   DataHandlerDataSource   implements   DataSource  { 

DataHandler   dataHandler  =  null  ; 




public   DataHandlerDataSource  (  DataHandler   dh  )  { 
this  .  dataHandler  =  dh  ; 
} 





public   InputStream   getInputStream  (  )  throws   IOException  { 
return   dataHandler  .  getInputStream  (  )  ; 
} 





public   OutputStream   getOutputStream  (  )  throws   IOException  { 
return   dataHandler  .  getOutputStream  (  )  ; 
} 





public   String   getContentType  (  )  { 
return   dataHandler  .  getContentType  (  )  ; 
} 





public   String   getName  (  )  { 
return   dataHandler  .  getName  (  )  ; 
} 
} 

class   DataSourceDataContentHandler   implements   DataContentHandler  { 

private   DataSource   ds  =  null  ; 

private   DataFlavor   transferFlavors  [  ]  =  null  ; 

private   DataContentHandler   dch  =  null  ; 




public   DataSourceDataContentHandler  (  DataContentHandler   dch  ,  DataSource   ds  )  { 
this  .  ds  =  ds  ; 
this  .  dch  =  dch  ; 
} 





public   DataFlavor  [  ]  getTransferDataFlavors  (  )  { 
if  (  transferFlavors  ==  null  )  { 
if  (  dch  !=  null  )  { 
transferFlavors  =  dch  .  getTransferDataFlavors  (  )  ; 
}  else  { 
transferFlavors  =  new   DataFlavor  [  1  ]  ; 
transferFlavors  [  0  ]  =  new   ActivationDataFlavor  (  ds  .  getContentType  (  )  ,  ds  .  getContentType  (  )  )  ; 
} 
} 
return   transferFlavors  ; 
} 







public   Object   getTransferData  (  DataFlavor   df  ,  DataSource   ds  )  throws   UnsupportedFlavorException  ,  IOException  { 
if  (  dch  !=  null  )  return   dch  .  getTransferData  (  df  ,  ds  )  ;  else   if  (  df  .  equals  (  getTransferDataFlavors  (  )  [  0  ]  )  )  return   ds  .  getInputStream  (  )  ;  else   throw   new   UnsupportedFlavorException  (  df  )  ; 
} 

public   Object   getContent  (  DataSource   ds  )  throws   IOException  { 
if  (  dch  !=  null  )  return   dch  .  getContent  (  ds  )  ;  else   return   ds  .  getInputStream  (  )  ; 
} 




public   void   writeTo  (  Object   obj  ,  String   mimeType  ,  OutputStream   os  )  throws   IOException  { 
if  (  dch  !=  null  )  dch  .  writeTo  (  obj  ,  mimeType  ,  os  )  ;  else   throw   new   UnsupportedDataTypeException  (  "no DCH for content type "  +  ds  .  getContentType  (  )  )  ; 
} 
} 

class   ObjectDataContentHandler   implements   DataContentHandler  { 

private   DataFlavor   transferFlavors  [  ]  =  null  ; 

private   Object   obj  ; 

private   String   mimeType  ; 

private   DataContentHandler   dch  =  null  ; 




public   ObjectDataContentHandler  (  DataContentHandler   dch  ,  Object   obj  ,  String   mimeType  )  { 
this  .  obj  =  obj  ; 
this  .  mimeType  =  mimeType  ; 
this  .  dch  =  dch  ; 
} 





public   DataContentHandler   getDCH  (  )  { 
return   dch  ; 
} 





public   synchronized   DataFlavor  [  ]  getTransferDataFlavors  (  )  { 
if  (  transferFlavors  ==  null  )  { 
if  (  dch  !=  null  )  { 
transferFlavors  =  dch  .  getTransferDataFlavors  (  )  ; 
}  else  { 
transferFlavors  =  new   DataFlavor  [  1  ]  ; 
transferFlavors  [  0  ]  =  new   ActivationDataFlavor  (  obj  .  getClass  (  )  ,  mimeType  ,  mimeType  )  ; 
} 
} 
return   transferFlavors  ; 
} 







public   Object   getTransferData  (  DataFlavor   df  ,  DataSource   ds  )  throws   UnsupportedFlavorException  ,  IOException  { 
if  (  dch  !=  null  )  return   dch  .  getTransferData  (  df  ,  ds  )  ;  else   if  (  df  .  equals  (  getTransferDataFlavors  (  )  [  0  ]  )  )  return   obj  ;  else   throw   new   UnsupportedFlavorException  (  df  )  ; 
} 

public   Object   getContent  (  DataSource   ds  )  { 
return   obj  ; 
} 




public   void   writeTo  (  Object   obj  ,  String   mimeType  ,  OutputStream   os  )  throws   IOException  { 
if  (  dch  !=  null  )  dch  .  writeTo  (  obj  ,  mimeType  ,  os  )  ;  else   if  (  obj   instanceof   byte  [  ]  )  os  .  write  (  (  byte  [  ]  )  obj  )  ;  else   if  (  obj   instanceof   String  )  { 
OutputStreamWriter   osw  =  new   OutputStreamWriter  (  os  )  ; 
osw  .  write  (  (  String  )  obj  )  ; 
osw  .  flush  (  )  ; 
}  else   throw   new   UnsupportedDataTypeException  (  "no object DCH for MIME type "  +  this  .  mimeType  )  ; 
} 
} 

