package   goldengate  .  ftp  .  core  .  data  ; 

import   goldengate  .  common  .  command  .  ReplyCode  ; 
import   goldengate  .  common  .  command  .  exception  .  CommandAbstractException  ; 
import   goldengate  .  common  .  command  .  exception  .  Reply425Exception  ; 
import   goldengate  .  common  .  future  .  GgChannelFuture  ; 
import   goldengate  .  common  .  future  .  GgFuture  ; 
import   goldengate  .  common  .  logging  .  GgInternalLogger  ; 
import   goldengate  .  common  .  logging  .  GgInternalLoggerFactory  ; 
import   goldengate  .  ftp  .  core  .  command  .  FtpCommandCode  ; 
import   goldengate  .  ftp  .  core  .  command  .  service  .  ABOR  ; 
import   goldengate  .  ftp  .  core  .  config  .  FtpInternalConfiguration  ; 
import   goldengate  .  ftp  .  core  .  control  .  NetworkHandler  ; 
import   goldengate  .  ftp  .  core  .  data  .  handler  .  DataNetworkHandler  ; 
import   goldengate  .  ftp  .  core  .  exception  .  FtpNoConnectionException  ; 
import   goldengate  .  ftp  .  core  .  exception  .  FtpNoFileException  ; 
import   goldengate  .  ftp  .  core  .  exception  .  FtpNoTransferException  ; 
import   goldengate  .  ftp  .  core  .  file  .  FtpFile  ; 
import   goldengate  .  ftp  .  core  .  session  .  FtpSession  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   java  .  util  .  concurrent  .  TimeUnit  ; 
import   org  .  jboss  .  netty  .  bootstrap  .  ClientBootstrap  ; 
import   org  .  jboss  .  netty  .  channel  .  Channel  ; 
import   org  .  jboss  .  netty  .  channel  .  ChannelFuture  ; 
import   org  .  jboss  .  netty  .  channel  .  Channels  ; 







public   class   FtpTransferControl  { 




private   static   final   GgInternalLogger   logger  =  GgInternalLoggerFactory  .  getLogger  (  FtpTransferControl  .  class  )  ; 




private   final   FtpSession   session  ; 




private   volatile   boolean   isDataNetworkHandlerReady  =  false  ; 




private   volatile   Channel   dataChannel  =  null  ; 




private   volatile   GgChannelFuture   waitForOpenedDataChannel  =  new   GgChannelFuture  (  true  )  ; 




private   volatile   GgFuture   closedDataChannel  =  null  ; 




private   volatile   boolean   isExecutingCommandFinished  =  true  ; 




private   volatile   GgFuture   commandFinishing  =  null  ; 




private   FtpTransfer   executingCommand  =  null  ; 




private   ExecutorService   executorService  =  null  ; 





private   volatile   GgFuture   endOfCommand  =  null  ; 




private   volatile   boolean   isCheckAlreadyCalled  =  false  ; 





public   FtpTransferControl  (  FtpSession   session  )  { 
this  .  session  =  session  ; 
endOfCommand  =  null  ; 
} 





private   void   setDataNetworkHandlerReady  (  )  { 
isCheckAlreadyCalled  =  false  ; 
if  (  isDataNetworkHandlerReady  )  { 
return  ; 
} 
isDataNetworkHandlerReady  =  true  ; 
} 








public   void   waitForDataNetworkHandlerReady  (  )  throws   InterruptedException  { 
if  (  !  isDataNetworkHandlerReady  )  { 
throw   new   InterruptedException  (  "Bad initialization"  )  ; 
} 
} 








public   void   setOpenedDataChannel  (  Channel   channel  ,  DataNetworkHandler   dataNetworkHandler  )  { 
if  (  channel  !=  null  )  { 
session  .  getDataConn  (  )  .  setDataNetworkHandler  (  dataNetworkHandler  )  ; 
waitForOpenedDataChannel  .  setChannel  (  channel  )  ; 
waitForOpenedDataChannel  .  setSuccess  (  )  ; 
}  else  { 
waitForOpenedDataChannel  .  cancel  (  )  ; 
} 
} 








public   Channel   waitForOpenedDataChannel  (  )  throws   InterruptedException  { 
Channel   channel  =  null  ; 
if  (  waitForOpenedDataChannel  .  await  (  session  .  getConfiguration  (  )  .  TIMEOUTCON  +  1000  ,  TimeUnit  .  MILLISECONDS  )  )  { 
if  (  waitForOpenedDataChannel  .  isSuccess  (  )  )  { 
channel  =  waitForOpenedDataChannel  .  getChannel  (  )  ; 
}  else  { 
logger  .  warn  (  "data connection is in error"  )  ; 
} 
}  else  { 
logger  .  warn  (  "Timeout occurs during data connection"  )  ; 
} 
waitForOpenedDataChannel  =  new   GgChannelFuture  (  true  )  ; 
return   channel  ; 
} 




public   void   setClosedDataChannel  (  )  { 
if  (  closedDataChannel  !=  null  )  { 
closedDataChannel  .  setSuccess  (  )  ; 
} 
} 








public   boolean   openDataConnection  (  )  throws   Reply425Exception  { 
closedDataChannel  =  new   GgFuture  (  true  )  ; 
FtpDataAsyncConn   dataAsyncConn  =  session  .  getDataConn  (  )  ; 
if  (  !  dataAsyncConn  .  isStreamFile  (  )  )  { 
if  (  dataAsyncConn  .  isConnected  (  )  )  { 
session  .  setReplyCode  (  ReplyCode  .  REPLY_125_DATA_CONNECTION_ALREADY_OPEN  ,  dataAsyncConn  .  getType  (  )  .  name  (  )  +  " mode data connection already open"  )  ; 
return   true  ; 
} 
}  else  { 
if  (  dataAsyncConn  .  isConnected  (  )  )  { 
logger  .  error  (  "Connection already open but should not since in Stream mode"  )  ; 
setTransferAbortedFromInternal  (  false  )  ; 
throw   new   Reply425Exception  (  "Connection already open but should not since in Stream mode"  )  ; 
} 
} 
session  .  setReplyCode  (  ReplyCode  .  REPLY_150_FILE_STATUS_OKAY  ,  "Opening "  +  dataAsyncConn  .  getType  (  )  .  name  (  )  +  " mode data connection"  )  ; 
if  (  dataAsyncConn  .  isPassiveMode  (  )  )  { 
if  (  !  dataAsyncConn  .  isBind  (  )  )  { 
throw   new   Reply425Exception  (  "No passive data connection prepared"  )  ; 
} 
try  { 
dataChannel  =  waitForOpenedDataChannel  (  )  ; 
dataAsyncConn  .  setNewOpenedDataChannel  (  dataChannel  )  ; 
}  catch  (  InterruptedException   e  )  { 
logger  .  warn  (  "Connection abort in passive mode"  ,  e  )  ; 
throw   new   Reply425Exception  (  "Cannot open passive data connection"  )  ; 
} 
}  else  { 
InetAddress   inetAddress  =  dataAsyncConn  .  getLocalAddress  (  )  .  getAddress  (  )  ; 
InetSocketAddress   inetSocketAddress  =  dataAsyncConn  .  getRemoteAddress  (  )  ; 
if  (  session  .  getConfiguration  (  )  .  getFtpInternalConfiguration  (  )  .  hasFtpSession  (  inetAddress  ,  inetSocketAddress  )  )  { 
throw   new   Reply425Exception  (  "Cannot open active data connection since remote address is already in use: "  +  inetSocketAddress  )  ; 
} 
ClientBootstrap   clientBootstrap  =  session  .  getConfiguration  (  )  .  getFtpInternalConfiguration  (  )  .  getActiveBootstrap  (  )  ; 
session  .  getConfiguration  (  )  .  setNewFtpSession  (  inetAddress  ,  inetSocketAddress  ,  session  )  ; 
String   mylog  =  session  .  toString  (  )  ; 
logger  .  debug  (  "DataConn for: "  +  session  .  getCurrentCommand  (  )  .  getCommand  (  )  +  " to "  +  inetSocketAddress  .  toString  (  )  )  ; 
ChannelFuture   future  =  clientBootstrap  .  connect  (  inetSocketAddress  ,  dataAsyncConn  .  getLocalAddress  (  )  )  ; 
try  { 
future  .  await  (  )  ; 
}  catch  (  InterruptedException   e1  )  { 
} 
if  (  !  future  .  isSuccess  (  )  )  { 
logger  .  warn  (  "Connection abort in active mode from future while session: "  +  session  .  toString  (  )  +  "\nTrying connect to: "  +  inetSocketAddress  .  toString  (  )  +  "\nWas: "  +  mylog  ,  future  .  getCause  (  )  )  ; 
session  .  getConfiguration  (  )  .  delFtpSession  (  inetAddress  ,  inetSocketAddress  )  ; 
throw   new   Reply425Exception  (  "Cannot open active data connection"  )  ; 
} 
try  { 
dataChannel  =  waitForOpenedDataChannel  (  )  ; 
dataAsyncConn  .  setNewOpenedDataChannel  (  dataChannel  )  ; 
}  catch  (  InterruptedException   e  )  { 
logger  .  warn  (  "Connection abort in active mode"  ,  e  )  ; 
session  .  getConfiguration  (  )  .  delFtpSession  (  inetAddress  ,  inetSocketAddress  )  ; 
throw   new   Reply425Exception  (  "Cannot open active data connection"  )  ; 
} 
} 
if  (  dataChannel  ==  null  )  { 
if  (  !  dataAsyncConn  .  isPassiveMode  (  )  )  { 
session  .  getConfiguration  (  )  .  getFtpInternalConfiguration  (  )  .  delFtpSession  (  dataAsyncConn  .  getLocalAddress  (  )  .  getAddress  (  )  ,  dataAsyncConn  .  getRemoteAddress  (  )  )  ; 
} 
throw   new   Reply425Exception  (  "Cannot open data connection, shuting down"  )  ; 
} 
return   true  ; 
} 




private   void   runExecutor  (  )  { 
try  { 
session  .  getDataConn  (  )  .  getDataNetworkHandler  (  )  .  unlockModeCodec  (  )  ; 
}  catch  (  FtpNoConnectionException   e  )  { 
setTransferAbortedFromInternal  (  false  )  ; 
return  ; 
} 
if  (  executorService  ==  null  )  { 
executorService  =  Executors  .  newSingleThreadExecutor  (  )  ; 
} 
endOfCommand  =  new   GgFuture  (  true  )  ; 
executorService  .  execute  (  new   FtpTransferExecutor  (  session  ,  executingCommand  )  )  ; 
try  { 
commandFinishing  .  await  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 









public   void   setNewFtpTransfer  (  FtpCommandCode   command  ,  FtpFile   file  )  { 
isExecutingCommandFinished  =  false  ; 
commandFinishing  =  new   GgFuture  (  true  )  ; 
setDataNetworkHandlerReady  (  )  ; 
executingCommand  =  new   FtpTransfer  (  command  ,  file  )  ; 
runExecutor  (  )  ; 
commandFinishing  =  null  ; 
} 











public   void   setNewFtpTransfer  (  FtpCommandCode   command  ,  List  <  String  >  list  ,  String   path  )  { 
isExecutingCommandFinished  =  false  ; 
commandFinishing  =  new   GgFuture  (  true  )  ; 
setDataNetworkHandlerReady  (  )  ; 
executingCommand  =  new   FtpTransfer  (  command  ,  list  ,  path  )  ; 
runExecutor  (  )  ; 
commandFinishing  =  null  ; 
} 








public   boolean   isFtpTransferExecuting  (  )  { 
return  !  isExecutingCommandFinished  ; 
} 






public   FtpTransfer   getExecutingFtpTransfer  (  )  throws   FtpNoTransferException  { 
if  (  executingCommand  !=  null  )  { 
return   executingCommand  ; 
} 
throw   new   FtpNoTransferException  (  "No Command currently running"  )  ; 
} 








private   boolean   isExecutingRetrLikeTransfer  (  )  throws   FtpNoTransferException  ,  CommandAbstractException  ,  FtpNoFileException  { 
return   FtpCommandCode  .  isRetrLikeCommand  (  getExecutingFtpTransfer  (  )  .  getCommand  (  )  )  &&  getExecutingFtpTransfer  (  )  .  getFtpFile  (  )  .  isInReading  (  )  ; 
} 





public   void   runTrueRetrieve  (  )  { 
try  { 
if  (  isExecutingRetrLikeTransfer  (  )  )  { 
getExecutingFtpTransfer  (  )  .  getFtpFile  (  )  .  trueRetrieve  (  )  ; 
} 
}  catch  (  CommandAbstractException   e  )  { 
}  catch  (  FtpNoTransferException   e  )  { 
}  catch  (  FtpNoFileException   e  )  { 
} 
} 







private   boolean   checkFtpTransferStatus  (  )  throws   FtpNoTransferException  { 
if  (  isCheckAlreadyCalled  )  { 
logger  .  warn  (  "Check: ALREADY CALLED"  )  ; 
return   true  ; 
} 
if  (  isExecutingCommandFinished  )  { 
logger  .  warn  (  "Check: already Finished"  )  ; 
if  (  commandFinishing  !=  null  )  { 
commandFinishing  .  cancel  (  )  ; 
} 
throw   new   FtpNoTransferException  (  "No transfer running"  )  ; 
} 
if  (  !  isDataNetworkHandlerReady  )  { 
logger  .  warn  (  "Check: already DNH not ready"  )  ; 
throw   new   FtpNoTransferException  (  "No connection"  )  ; 
} 
isCheckAlreadyCalled  =  true  ; 
FtpTransfer   executedTransfer  =  getExecutingFtpTransfer  (  )  ; 
if  (  FtpCommandCode  .  isListLikeCommand  (  executedTransfer  .  getCommand  (  )  )  )  { 
if  (  executedTransfer  .  getStatus  (  )  )  { 
closeTransfer  (  )  ; 
return   false  ; 
} 
abortTransfer  (  )  ; 
return   false  ; 
}  else   if  (  FtpCommandCode  .  isRetrLikeCommand  (  executedTransfer  .  getCommand  (  )  )  )  { 
FtpFile   file  =  null  ; 
try  { 
file  =  executedTransfer  .  getFtpFile  (  )  ; 
}  catch  (  FtpNoFileException   e  )  { 
abortTransfer  (  )  ; 
return   false  ; 
} 
try  { 
if  (  file  .  isInReading  (  )  )  { 
logger  .  debug  (  "Check: Retr FtpFile still in reading KO"  )  ; 
abortTransfer  (  )  ; 
}  else  { 
logger  .  debug  (  "Check: Retr FtpFile no more in reading OK"  )  ; 
closeTransfer  (  )  ; 
} 
}  catch  (  CommandAbstractException   e  )  { 
logger  .  warn  (  "Retr Test is in Reading problem"  ,  e  )  ; 
closeTransfer  (  )  ; 
} 
return   false  ; 
}  else   if  (  FtpCommandCode  .  isStoreLikeCommand  (  executedTransfer  .  getCommand  (  )  )  )  { 
closeTransfer  (  )  ; 
return   false  ; 
}  else  { 
logger  .  warn  (  "Check: Unknown command"  )  ; 
abortTransfer  (  )  ; 
} 
return   false  ; 
} 




private   void   abortTransfer  (  )  { 
FtpFile   file  =  null  ; 
FtpTransfer   current  =  null  ; 
try  { 
current  =  getExecutingFtpTransfer  (  )  ; 
file  =  current  .  getFtpFile  (  )  ; 
file  .  abortFile  (  )  ; 
}  catch  (  FtpNoTransferException   e  )  { 
logger  .  warn  (  "Abort problem"  ,  e  )  ; 
}  catch  (  FtpNoFileException   e  )  { 
}  catch  (  CommandAbstractException   e  )  { 
logger  .  warn  (  "Abort problem"  ,  e  )  ; 
} 
if  (  current  !=  null  )  { 
current  .  setStatus  (  false  )  ; 
} 
endDataConnection  (  )  ; 
session  .  setReplyCode  (  ReplyCode  .  REPLY_426_CONNECTION_CLOSED_TRANSFER_ABORTED  ,  "Transfer aborted for "  +  (  current  ==  null  ?  "Unknown command"  :  current  .  toString  (  )  )  )  ; 
if  (  current  !=  null  )  { 
if  (  !  FtpCommandCode  .  isListLikeCommand  (  current  .  getCommand  (  )  )  )  { 
try  { 
session  .  getBusinessHandler  (  )  .  afterTransferDoneBeforeAnswer  (  current  )  ; 
}  catch  (  CommandAbstractException   e  )  { 
session  .  setReplyCode  (  e  )  ; 
} 
} 
} 
finalizeExecution  (  )  ; 
} 





private   void   closeTransfer  (  )  { 
FtpFile   file  =  null  ; 
FtpTransfer   current  =  null  ; 
try  { 
current  =  getExecutingFtpTransfer  (  )  ; 
file  =  current  .  getFtpFile  (  )  ; 
file  .  closeFile  (  )  ; 
}  catch  (  FtpNoTransferException   e  )  { 
logger  .  warn  (  "Close problem"  ,  e  )  ; 
}  catch  (  FtpNoFileException   e  )  { 
}  catch  (  CommandAbstractException   e  )  { 
logger  .  warn  (  "Close problem"  ,  e  )  ; 
} 
if  (  current  !=  null  )  { 
current  .  setStatus  (  true  )  ; 
} 
if  (  session  .  getDataConn  (  )  .  isStreamFile  (  )  )  { 
endDataConnection  (  )  ; 
} 
session  .  setReplyCode  (  ReplyCode  .  REPLY_250_REQUESTED_FILE_ACTION_OKAY  ,  "Transfer correctly finished for "  +  (  current  ==  null  ?  "Unknown command"  :  current  .  toString  (  )  )  )  ; 
if  (  current  !=  null  )  { 
if  (  !  FtpCommandCode  .  isListLikeCommand  (  current  .  getCommand  (  )  )  )  { 
try  { 
session  .  getBusinessHandler  (  )  .  afterTransferDoneBeforeAnswer  (  current  )  ; 
}  catch  (  CommandAbstractException   e  )  { 
session  .  setReplyCode  (  e  )  ; 
} 
}  else  { 
try  { 
Thread  .  sleep  (  FtpInternalConfiguration  .  RETRYINMS  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 
finalizeExecution  (  )  ; 
} 






public   void   setEndOfTransfer  (  )  { 
try  { 
checkFtpTransferStatus  (  )  ; 
}  catch  (  FtpNoTransferException   e  )  { 
return  ; 
} 
} 








public   void   setTransferAbortedFromInternal  (  boolean   write  )  { 
abortTransfer  (  )  ; 
if  (  write  )  { 
session  .  getNetworkHandler  (  )  .  writeIntermediateAnswer  (  )  ; 
} 
if  (  endOfCommand  !=  null  )  { 
endOfCommand  .  cancel  (  )  ; 
} 
} 






public   void   setPreEndOfTransfer  (  )  { 
if  (  endOfCommand  !=  null  )  { 
endOfCommand  .  setSuccess  (  )  ; 
} 
} 







public   void   waitForEndOfTransfer  (  )  throws   InterruptedException  { 
if  (  endOfCommand  !=  null  )  { 
endOfCommand  .  await  (  )  ; 
if  (  endOfCommand  .  isFailed  (  )  )  { 
throw   new   InterruptedException  (  "Transfer aborted"  )  ; 
} 
} 
} 





private   void   finalizeExecution  (  )  { 
isExecutingCommandFinished  =  true  ; 
if  (  commandFinishing  !=  null  )  { 
commandFinishing  .  setSuccess  (  )  ; 
} 
executingCommand  =  null  ; 
} 




private   void   endDataConnection  (  )  { 
if  (  isDataNetworkHandlerReady  )  { 
isDataNetworkHandlerReady  =  false  ; 
Channels  .  close  (  dataChannel  )  ; 
if  (  closedDataChannel  !=  null  )  { 
try  { 
closedDataChannel  .  await  (  session  .  getConfiguration  (  )  .  TIMEOUTCON  ,  TimeUnit  .  MILLISECONDS  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
dataChannel  =  null  ; 
} 
} 







public   void   clear  (  )  { 
endDataConnection  (  )  ; 
finalizeExecution  (  )  ; 
if  (  closedDataChannel  !=  null  )  { 
closedDataChannel  .  cancel  (  )  ; 
} 
if  (  endOfCommand  !=  null  )  { 
endOfCommand  .  cancel  (  )  ; 
} 
if  (  waitForOpenedDataChannel  !=  null  )  { 
waitForOpenedDataChannel  .  cancel  (  )  ; 
} 
if  (  executorService  !=  null  )  { 
executorService  .  shutdownNow  (  )  ; 
executorService  =  null  ; 
} 
} 
} 

