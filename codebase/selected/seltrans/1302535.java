package   org  .  nees  .  rbnb  ; 

import   com  .  rbnb  .  sapi  .  Source  ; 
import   com  .  rbnb  .  sapi  .  ChannelMap  ; 
import   com  .  rbnb  .  sapi  .  SAPIException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   org  .  apache  .  commons  .  cli  .  *  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 

public   class   DataTurbine  { 

static   Log   log  =  LogFactory  .  getLog  (  DataTurbine  .  class  .  getName  (  )  )  ; 

private   static   final   String   SERVER_NAME  =  "localhost:3333"  ; 

private   static   final   String   CHANNEL_NAME  =  "image"  ; 

private   static   final   String   PICTURE_PATH  =  "./image/"  ; 

private   String   serverName  =  SERVER_NAME  ; 

private   String   channelName  =  CHANNEL_NAME  ; 

private   String   picturePath  =  PICTURE_PATH  ; 

private   boolean   debugging  =  false  ; 

private   String   name  ; 

private   Source   source  ; 

private   byte  [  ]  fileBuffer  ; 

private   int   channelId  =  -  1  ; 

private   ChannelMap   cmap  ; 

public   static   int   RBNB_CACHE_SIZE  =  5000  ; 

public   static   int   RBNB_ARCHIVE_SIZE  =  10000  ; 

public   boolean   keepRbnbDataArchive  =  true  ; 

public   boolean   keepRbnbDataCache  =  true  ; 







public   static   String   RBNB_ARCHIVE_MODE  =  "append"  ; 

public   DataTurbine  (  String   name  )  { 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   Thread  (  )  { 

public   void   run  (  )  { 
try  { 
if  (  keepRbnbDataArchive  ||  keepRbnbDataCache  )  { 
closeAndKeep  (  )  ; 
}  else  { 
close  (  )  ; 
} 
log  .  debug  (  "Shutdown hook."  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Unexpected error closing DataTurbine."  )  ; 
} 
} 
}  )  ; 
this  .  name  =  name  ; 
} 



protected   void   finalize  (  )  { 
try  { 
if  (  keepRbnbDataArchive  ||  keepRbnbDataCache  )  { 
this  .  closeAndKeep  (  )  ; 
}  else  { 
this  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Unexpected error closing DataTurbine."  )  ; 
} 
} 



public   void   setServerName  (  String   newServerName  )  { 
this  .  serverName  =  newServerName  ; 
} 



public   void   setName  (  String   newName  )  { 
this  .  name  =  newName  ; 
} 


public   Source   getSource  (  )  { 
return   this  .  source  ; 
} 

public   ChannelMap   getChannelMap  (  )  { 
return   this  .  cmap  ; 
} 



public   String   getDebugString  (  )  { 
String   retVal  =  "serverName:\t"  +  serverName  +  "\n"  +  "channelName:\t"  +  channelName  +  "\n"  +  "picturePath:\t"  +  picturePath  +  "\n"  +  "name:\t\t"  +  name  +  "\n"  +  "source:\t\t"  +  source  +  "\n"  +  "channelId:\t"  +  channelId  +  "\n"  +  "cmap:\t\t"  +  cmap  ; 
return   retVal  ; 
} 



public   void   open  (  )  throws   SAPIException  { 
try  { 
log  .  debug  (  "Turbine server: "  +  this  .  serverName  )  ; 
log  .  debug  (  "Opening turbine: "  +  this  .  name  )  ; 
this  .  source  =  new   Source  (  RBNB_CACHE_SIZE  ,  RBNB_ARCHIVE_MODE  ,  RBNB_ARCHIVE_SIZE  )  ; 
this  .  source  .  OpenRBNBConnection  (  this  .  serverName  ,  this  .  name  )  ; 
}  catch  (  SAPIException   sapie  )  { 
log  .  error  (  "Error opening turbine source: "  +  sapie  )  ; 
throw   sapie  ; 
} 
log  .  debug  (  "Opening turbine channel"  )  ; 
this  .  cmap  =  new   ChannelMap  (  )  ; 
this  .  cmap  .  PutTimeAuto  (  "timeofday"  )  ; 
} 



public   boolean   isConnected  (  )  { 
if  (  this  .  source  ==  null  )  { 
return   false  ; 
}  else  { 
return   this  .  source  .  VerifyConnection  (  )  ; 
} 
} 




public   void   upload  (  String   file  )  { 
log  .  debug  (  "Uploading file: "  +  file  +  " to "  +  serverName  )  ; 
loadImageFile  (  file  )  ; 
try  { 
log  .  debug  (  "Adding turbine channel \""  +  channelName  +  "\" on "  +  serverName  )  ; 
channelId  =  cmap  .  Add  (  channelName  )  ; 
this  .  cmap  .  PutTimeAuto  (  "timeofday"  )  ; 
this  .  cmap  .  PutMime  (  channelId  ,  "image/jpeg"  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error adding turbine channel: "  +  e  )  ; 
} 
try  { 
log  .  debug  (  "Uploading size: "  +  fileBuffer  .  length  +  " bytes to "  +  serverName  )  ; 
this  .  cmap  .  PutDataAsByteArray  (  channelId  ,  fileBuffer  )  ; 
this  .  source  .  Flush  (  this  .  cmap  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error writing to turbine: "  +  e  )  ; 
} 
} 




private   void   loadImageFile  (  String   file  )  { 
try  { 
File   f  =  new   File  (  picturePath  +  file  )  ; 
FileInputStream   in  =  new   FileInputStream  (  f  )  ; 
int   fileLength  =  (  int  )  f  .  length  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  fileLength  ]  ; 
in  .  read  (  buffer  )  ; 
fileBuffer  =  buffer  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error loading image file: "  +  e  )  ; 
} 
} 





public   int   putString  (  String   theString  ,  String   targetChannel  )  { 
try  { 
log  .  debug  (  "Putting string\n"  +  theString  +  "\ninto channel \""  +  targetChannel  +  "\""  +  " on "  +  serverName  )  ; 
try  { 
this  .  channelId  =  this  .  cmap  .  Add  (  targetChannel  )  ; 
this  .  cmap  .  PutTimeAuto  (  "timeofday"  )  ; 
this  .  cmap  .  PutMime  (  this  .  channelId  ,  "text/plain"  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error adding turbine channel: "  +  e  )  ; 
} 
this  .  cmap  .  PutDataAsString  (  this  .  channelId  ,  theString  )  ; 
this  .  source  .  Flush  (  this  .  cmap  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error writing to turbine: "  +  e  )  ; 
} 
return   this  .  channelId  ; 
} 






public   void   close  (  )  throws   Exception  { 
try  { 
log  .  debug  (  "Closing turbine "  +  serverName  )  ; 
source  .  CloseRBNBConnection  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error closing turbine: "  +  e  )  ; 
throw   e  ; 
} 
} 




public   void   closeAndKeep  (  )  throws   Exception  { 
try  { 
log  .  debug  (  "Closing turbine "  +  serverName  +  " with cache and archive."  )  ; 
this  .  source  .  Detach  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Error closing turbine: "  +  e  )  ; 
throw   e  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
DataTurbine   app  =  new   DataTurbine  (  "NEESit_DataTurbine_Wrapper"  )  ; 
Options   opts  =  new   Options  (  )  ; 
CommandLineParser   parser  =  new   BasicParser  (  )  ; 
CommandLine   cmd  =  null  ; 
opts  .  addOption  (  "a"  ,  false  ,  "about"  )  ; 
opts  .  addOption  (  "d"  ,  false  ,  "enable debug output"  )  ; 
opts  .  addOption  (  "n"  ,  true  ,  "DataTurbine source name to register"  )  ; 
opts  .  addOption  (  "r"  ,  true  ,  "DataTurbine server name to connect to as a source."  +  " Default is to use a fake channel to act as a source for."  )  ; 
try  { 
cmd  =  parser  .  parse  (  opts  ,  args  )  ; 
}  catch  (  ParseException   pe  )  { 
HelpFormatter   formatter  =  new   HelpFormatter  (  )  ; 
formatter  .  printHelp  (  "DataTurbine"  ,  opts  )  ; 
System  .  exit  (  0  )  ; 
} 
if  (  cmd  .  hasOption  (  "a"  )  )  { 
System  .  out  .  println  (  "About: This is a program that will manage"  +  "DataTurbine server connections."  )  ; 
System  .  exit  (  0  )  ; 
} 
if  (  cmd  .  hasOption  (  "d"  )  )  { 
app  .  debugging  =  true  ; 
} 
if  (  cmd  .  hasOption  (  "n"  )  )  { 
app  .  setName  (  cmd  .  getOptionValue  (  "n"  )  )  ; 
} 
if  (  cmd  .  hasOption  (  "r"  )  )  { 
app  .  setServerName  (  cmd  .  getOptionValue  (  "r"  )  )  ; 
} 
try  { 
app  .  open  (  )  ; 
}  catch  (  SAPIException   sae  )  { 
log  .  error  (  "Couldn't open the turbine "  +  sae  )  ; 
} 
app  .  putString  (  "DataTurbine.java_test"  ,  "_DataTurbine.java_test"  )  ; 
if  (  app  .  debugging  )  System  .  out  .  println  (  app  .  getDebugString  (  )  )  ; 
} 
} 

