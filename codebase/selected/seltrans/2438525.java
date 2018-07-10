package   phex  .  common  ; 

import   java  .  awt  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  lang  .  reflect  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  *  ; 
import   phex  .  *  ; 
import   phex  .  msg  .  *  ; 
import   phex  .  utils  .  *  ; 

public   class   Cfg  { 

public   static   final   int   DEFAULT_SOCKS5_PORT  =  1080  ; 

public   static   final   int   DEFAULT_HTTP_PORT  =  80  ; 

public   static   final   int   DEFAULT_MAX_MESSAGE_LENGTH  =  65536  ; 

public   static   final   short   DEFAULT_LOGGER_VERBOSE_LEVEL  =  6  ; 

public   static   final   boolean   DEFAULT_ENABLE_HIT_SNOOPING  =  true  ; 

public   static   final   boolean   DEFAULT_IS_CHAT_ENABLED  =  true  ; 

public   static   final   boolean   DEFAULT_ALLOW_TO_BECOME_LEAF  =  true  ; 

public   static   final   boolean   DEFAULT_ALLOW_TO_BECOME_ULTRAPEER  =  false  ; 

public   static   final   boolean   DEFAULT_FORCE_UP_CONNECTIONS  =  false  ; 

public   static   final   boolean   DEFAULT_IS_NOVENDOR_NODE_DISCONNECTED  =  false  ; 

public   static   final   int   DEFAULT_FREELOADER_FILES  =  0  ; 

public   static   final   int   DEFAULT_FREELOADER_SHARE_SIZE  =  0  ; 

public   static   final   int   DEFAULT_HOST_ERROR_DISPLAY_TIME  =  1000  ; 

public   static   final   int   DEFAULT_TTL  =  7  ; 

public   static   final   int   DEFAULT_MAX_NETWORK_TTL  =  7  ; 

public   static   final   int   DEFAULT_UP_2_UP_CONNECTIONS  =  16  ; 

public   static   final   int   DEFAULT_UP_2_LEAF_CONNECTIONS  =  15  ; 

public   static   final   int   DEFAULT_UP_2_PEER_CONNECTIONS  =  4  ; 

public   static   final   int   DEFAULT_LEAF_2_UP_CONNECTIONS  =  5  ; 

public   static   final   int   DEFAULT_LEAF_2_PEER_CONNECTIONS  =  2  ; 

public   static   final   int   DEFAULT_PEER_CONNECTIONS  =  4  ; 

public   static   final   int   DEFAULT_MAX_CONNECTTO_HISTORY_SIZE  =  10  ; 

public   static   final   int   DEFAULT_MAX_SEARCHTERM_HISTORY_SIZE  =  10  ; 

public   static   final   boolean   DEFAULT_ARE_PARTIAL_FILES_SHARED  =  true  ; 




public   static   final   int   DEFAULT_DYNAMIC_QUERY_MAX_TTL  =  3  ; 




public   static   final   boolean   DEFAULT_FORCE_TOBE_ULTRAPEER  =  false  ; 




public   static   final   int   DEFAULT_MAX_DOWNLOADS_PER_IP  =  1  ; 




public   static   final   boolean   DEFAULT_ALLOW_UPLOAD_QUEUING  =  true  ; 




public   static   final   int   DEFAULT_MAX_UPLOAD_QUEUE_SIZE  =  100  ; 




public   static   final   int   DEFAULT_MIN_UPLOAD_QUEUE_POLL_TIME  =  45  ; 




public   static   final   int   DEFAULT_MAX_UPLOAD_QUEUE_POLL_TIME  =  120  ; 




public   static   final   boolean   DEFAULT_IS_DEFLATE_CONNECTION_ACCEPTED  =  true  ; 

public   static   final   int   UNLIMITED_BANDWIDTH  =  Integer  .  MAX_VALUE  ; 

public   static   int   MIN_SEARCH_TERM_LENGTH  =  2  ; 

public   static   final   String   GENERAL_GNUTELLA_NETWORK  =  "<General Gnutella Network>"  ; 

public   GUID   mProgramClientID  =  new   GUID  (  )  ; 

public   String   mMyIP  =  ""  ; 

public   int   mListeningPort  =  -  1  ; 

public   int   mMaxUpload  =  4  ; 

public   int   mMaxUploadPerIP  =  1  ; 

public   int   mUploadMaxBandwidth  =  102400  ; 

public   int   mNetMaxHostToCatch  =  1000  ; 

public   int   mNetMaxSendQueue  =  500  ; 

public   int   mSearchMaxConcurrent  =  10  ; 

public   int   mNetMaxRate  =  50000  ; 

public   int   mDownloadMaxBandwidth  =  102400  ; 

public   boolean   mDownloadAutoRemoveCompleted  =  false  ; 

public   String   mDownloadDir  =  "."  ; 

public   int   mDownloadMaxRetry  =  999  ; 

public   int   mDownloadRetryWait  =  30  *  1000  ; 

public   boolean   mAutoConnect  =  true  ; 





public   int   mNetMinConn  =  4  ; 

public   boolean   mAutoCleanup  =  true  ; 





public   int   mUploadMaxSearch  =  64  ; 

public   boolean   mShareBrowseDir  =  true  ; 

public   int   mPushTransferTimeout  =  30  *  1000  ; 




public   ArrayList   mNetIgnoredHosts  ; 




public   Vector   mNetInvalidHosts  =  new   Vector  (  )  ; 




public   Vector   mFilteredSearchHosts  =  new   Vector  (  )  ; 

public   String   mCurrentNetwork  =  GENERAL_GNUTELLA_NETWORK  ; 

public   Vector   mNetNetworkHistory  =  new   Vector  (  )  ; 

public   boolean   mAutoJoin  =  true  ; 

public   boolean   mDisconnectApplyPolicy  =  true  ; 

public   int   mDisconnectDropRatio  =  70  ; 

public   boolean   mProxyUse  =  false  ; 

public   String   mProxyHost  =  ""  ; 

public   int   mProxyPort  =  DEFAULT_SOCKS5_PORT  ; 

public   boolean   useProxyAuthentication  =  false  ; 

public   String   mProxyUserName  =  ""  ; 

public   String   mProxyPassword  =  ""  ; 

public   Font   mFontMenu  =  new   Font  (  "Dialog"  ,  Font  .  PLAIN  ,  11  )  ; 

public   Font   mFontLabel  =  new   Font  (  "Dialog"  ,  Font  .  PLAIN  ,  11  )  ; 

public   Font   mFontTable  =  new   Font  (  "Dialog"  ,  Font  .  PLAIN  ,  11  )  ; 

public   String   mFindText  =  ""  ; 

public   boolean   mFindMatchCase  =  false  ; 

public   boolean   mFindDown  =  true  ; 

public   boolean   mUIDisplayTooltip  =  true  ; 

public   String   mLFClassName  ; 

public   String   mUploadDir  =  ""  ; 

public   String   mUploadFileExclusions  =  ""  ; 

public   String   mUploadFileInclusions  =  "*"  ; 

public   boolean   mUploadScanRecursively  =  true  ; 

public   boolean   mUploadAutoRemoveCompleted  =  false  ; 

public   boolean   mPhexPingResponse  =  true  ; 

public   boolean   monitorSearchHistory  =  false  ; 




public   String   searchMonitorFile  =  ""  ; 

public   int   searchHistoryLength  =  10  ; 




public   boolean   connectedToLAN  =  true  ; 






public   boolean   minimizeToBackground  =  true  ; 




public   boolean   showCloseOptionsDialog  =  true  ; 




public   String   incompleteDir  =  "."  ; 





public   ArrayList   filteredCatcherPorts  =  new   ArrayList  (  )  ; 




public   short   maxWorkerPerDownload  =  3  ; 




public   short   maxTotalDownloadWorker  =  6  ; 




public   boolean   allowUploadQueuing  ; 




public   int   maxUploadQueueSize  ; 




public   int   minUploadQueuePollTime  ; 




public   int   maxUploadQueuePollTime  ; 




public   int   maxDownloadsPerIP  ; 





public   short   maxFailedConnectionsInARow  =  10  ; 







public   int   networkSpeedKbps  =  256  ; 







public   int   maxTotalBandwidth  =  16384  ; 







public   String   runningPhexVersion  =  ""  ; 







public   String   runningBuildNumber  =  ""  ; 





public   boolean   isBehindFirewall  =  false  ; 





public   boolean   isHttpProxyUsed  =  false  ; 




public   String   httpProxyHost  =  ""  ; 




public   int   httpProxyPort  =  DEFAULT_HTTP_PORT  ; 




public   String   lastUpdateCheckVersion  =  "0"  ; 




public   String   lastBetaUpdateCheckVersion  =  "0"  ; 




public   long   lastUpdateCheckTime  =  0  ; 




public   boolean   showUpdateNotification  =  true  ; 




public   boolean   showBetaUpdateNotification  =  false  ; 

public   int   mSocketTimeout  =  60  *  1000  ; 

public   int   privateSocketTimeout  =  2000  ; 




public   int   mNetConnectionTimeout  =  4  *  1000  ; 




public   int   searchRetryTimeout  =  30000  ; 






public   short   loggerVerboseLevel  ; 




public   short   logType  =  0x01  ; 




public   boolean   logToConsole  =  false  ; 




public   long   maxLogFileLength  =  512  *  1024  ; 




public   boolean   enableHitSnooping  ; 




public   int   maxMessageLength  ; 




public   boolean   isChatEnabled  ; 





public   boolean   allowUPConnections  ; 




public   boolean   allowToBecomeLeaf  ; 







public   boolean   forceUPConnections  ; 




public   boolean   allowToBecomeUP  ; 







public   boolean   forceToBeUltrapeer  ; 








public   int   up2upConnections  ; 





public   int   up2leafConnections  ; 





public   int   up2peerConnections  ; 





public   int   leaf2upConnections  ; 





public   int   leaf2peerConnections  ; 




public   int   peerConnections  ; 




public   boolean   isNoVendorNodeDisconnected  ; 




public   int   freeloaderFiles  ; 




public   int   freeloaderShareSize  ; 




public   int   hostErrorDisplayTime  ; 




public   int   ttl  ; 






public   int   maxNetworkTTL  ; 




public   ArrayList   connectToHistory  ; 




public   int   maxConnectToHistorySize  ; 




public   ArrayList   searchTermHistory  ; 




public   int   maxSearchTermHistorySize  ; 




public   boolean   arePartialFilesShared  ; 




public   long   movingTotalUptime  ; 




public   int   movingTotalUptimeCount  ; 




public   long   maximalUptime  ; 




public   boolean   isDeflateConnectionAccepted  ; 

private   File   configFile  ; 

private   Properties   mSetting  ; 

public   Cfg  (  File   cfgFile  )  { 
configFile  =  cfgFile  ; 
mSetting  =  new   Properties  (  )  ; 
} 

public   void   load  (  )  { 
loadDefaultValues  (  )  ; 
try  { 
FileInputStream   is  =  new   FileInputStream  (  configFile  )  ; 
mSetting  .  load  (  is  )  ; 
is  .  close  (  )  ; 
}  catch  (  FileNotFoundException   exp  )  { 
}  catch  (  Exception   exp  )  { 
Logger  .  logError  (  exp  )  ; 
} 
deserializeSimpleFields  (  )  ; 
deserializeComplexFields  (  )  ; 
handlePhexVersionAdjustments  (  )  ; 
if  (  mListeningPort  ==  -  1  )  { 
Random   random  =  new   Random  (  System  .  currentTimeMillis  (  )  )  ; 
mListeningPort  =  random  .  nextInt  (  )  ; 
mListeningPort  =  mListeningPort  <  0  ?  -  mListeningPort  :  mListeningPort  ; 
mListeningPort  %=  20000  ; 
mListeningPort  +=  4000  ; 
} 
updateHTTPProxySettings  (  )  ; 
if  (  mLFClassName  ==  null  )  { 
if  (  Environment  .  getInstance  (  )  .  isMacOSX  (  )  )  { 
mLFClassName  =  UIManager  .  getSystemLookAndFeelClassName  (  )  ; 
}  else  { 
mLFClassName  =  UIManager  .  getCrossPlatformLookAndFeelClassName  (  )  ; 
} 
} 
File   dir  =  new   File  (  mDownloadDir  )  ; 
dir  .  mkdirs  (  )  ; 
dir  =  new   File  (  incompleteDir  )  ; 
dir  .  mkdirs  (  )  ; 
} 

public   void   save  (  )  { 
Logger  .  logMessage  (  Logger  .  FINEST  ,  Logger  .  GLOBAL  ,  "Saving configuration."  )  ; 
mSetting  .  clear  (  )  ; 
serializeSimpleFields  (  )  ; 
serializeComplexField  (  )  ; 
try  { 
FileOutputStream   os  =  new   FileOutputStream  (  configFile  )  ; 
mSetting  .  store  (  os  ,  "PHEX Config Values"  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   exp  )  { 
Logger  .  logError  (  exp  )  ; 
} 
} 

private   void   loadDefaultValues  (  )  { 
maxDownloadsPerIP  =  DEFAULT_MAX_DOWNLOADS_PER_IP  ; 
enableHitSnooping  =  DEFAULT_ENABLE_HIT_SNOOPING  ; 
maxMessageLength  =  DEFAULT_MAX_MESSAGE_LENGTH  ; 
isChatEnabled  =  DEFAULT_IS_CHAT_ENABLED  ; 
allowToBecomeLeaf  =  DEFAULT_ALLOW_TO_BECOME_LEAF  ; 
forceUPConnections  =  DEFAULT_FORCE_UP_CONNECTIONS  ; 
forceToBeUltrapeer  =  DEFAULT_FORCE_TOBE_ULTRAPEER  ; 
allowToBecomeUP  =  DEFAULT_ALLOW_TO_BECOME_ULTRAPEER  ; 
isNoVendorNodeDisconnected  =  DEFAULT_IS_NOVENDOR_NODE_DISCONNECTED  ; 
freeloaderFiles  =  DEFAULT_FREELOADER_FILES  ; 
freeloaderShareSize  =  DEFAULT_FREELOADER_SHARE_SIZE  ; 
hostErrorDisplayTime  =  DEFAULT_HOST_ERROR_DISPLAY_TIME  ; 
ttl  =  DEFAULT_TTL  ; 
maxNetworkTTL  =  DEFAULT_MAX_NETWORK_TTL  ; 
up2upConnections  =  DEFAULT_UP_2_UP_CONNECTIONS  ; 
up2leafConnections  =  DEFAULT_UP_2_LEAF_CONNECTIONS  ; 
up2peerConnections  =  DEFAULT_UP_2_PEER_CONNECTIONS  ; 
leaf2upConnections  =  DEFAULT_LEAF_2_UP_CONNECTIONS  ; 
leaf2peerConnections  =  DEFAULT_LEAF_2_PEER_CONNECTIONS  ; 
peerConnections  =  DEFAULT_PEER_CONNECTIONS  ; 
arePartialFilesShared  =  DEFAULT_ARE_PARTIAL_FILES_SHARED  ; 
allowUploadQueuing  =  DEFAULT_ALLOW_UPLOAD_QUEUING  ; 
maxUploadQueueSize  =  DEFAULT_MAX_UPLOAD_QUEUE_SIZE  ; 
minUploadQueuePollTime  =  DEFAULT_MIN_UPLOAD_QUEUE_POLL_TIME  ; 
maxUploadQueuePollTime  =  DEFAULT_MAX_UPLOAD_QUEUE_POLL_TIME  ; 
isDeflateConnectionAccepted  =  DEFAULT_IS_DEFLATE_CONNECTION_ACCEPTED  ; 
loggerVerboseLevel  =  DEFAULT_LOGGER_VERBOSE_LEVEL  ; 
connectToHistory  =  new   ArrayList  (  )  ; 
maxConnectToHistorySize  =  DEFAULT_MAX_CONNECTTO_HISTORY_SIZE  ; 
searchTermHistory  =  new   ArrayList  (  )  ; 
maxSearchTermHistorySize  =  DEFAULT_MAX_SEARCHTERM_HISTORY_SIZE  ; 
} 

private   String   get  (  String   key  )  { 
String   value  =  (  String  )  mSetting  .  get  (  key  )  ; 
if  (  value  !=  null  )  value  =  value  .  trim  (  )  ; 
return   value  ; 
} 

private   String   get  (  String   key  ,  String   defaultVal  )  { 
String   value  =  get  (  key  )  ; 
if  (  value  ==  null  )  return   defaultVal  ; 
return   value  ; 
} 

private   void   set  (  String   key  ,  String   value  )  { 
if  (  value  !=  null  )  { 
mSetting  .  put  (  key  ,  value  )  ; 
} 
} 

private   void   set  (  String   key  ,  long   value  )  { 
mSetting  .  put  (  key  ,  String  .  valueOf  (  value  )  )  ; 
} 

private   void   set  (  String   key  ,  boolean   value  )  { 
mSetting  .  put  (  key  ,  value  ?  "true"  :  "false"  )  ; 
} 

private   void   serializeSimpleFields  (  )  { 
Field  [  ]  fields  =  this  .  getClass  (  )  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
String   name  =  fields  [  i  ]  .  getName  (  )  ; 
int   modifiers  =  fields  [  i  ]  .  getModifiers  (  )  ; 
Class   type  =  fields  [  i  ]  .  getType  (  )  ; 
if  (  !  Modifier  .  isPublic  (  modifiers  )  ||  Modifier  .  isTransient  (  modifiers  )  ||  Modifier  .  isStatic  (  modifiers  )  )  { 
continue  ; 
} 
try  { 
if  (  type  .  getName  (  )  .  equals  (  "int"  )  )  { 
set  (  name  ,  fields  [  i  ]  .  getInt  (  this  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "short"  )  )  { 
set  (  name  ,  fields  [  i  ]  .  getShort  (  this  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "long"  )  )  { 
set  (  name  ,  fields  [  i  ]  .  getLong  (  this  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "boolean"  )  )  { 
set  (  name  ,  fields  [  i  ]  .  getBoolean  (  this  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "java.lang.String"  )  )  { 
set  (  name  ,  (  String  )  fields  [  i  ]  .  get  (  this  )  )  ; 
} 
}  catch  (  Exception   exp  )  { 
Logger  .  logError  (  exp  ,  "Error in field: "  +  name  )  ; 
} 
} 
} 

private   void   serializeComplexField  (  )  { 
try  { 
set  (  "mProgramClientID"  ,  mProgramClientID  .  toHexString  (  )  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  16  *  mNetIgnoredHosts  .  size  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  mNetIgnoredHosts  .  size  (  )  ;  i  ++  )  { 
String  [  ]  parts  =  (  String  [  ]  )  mNetIgnoredHosts  .  get  (  i  )  ; 
buffer  .  append  (  parts  [  0  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  1  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  2  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  3  ]  )  ; 
buffer  .  append  (  ' '  )  ; 
} 
set  (  "mNetIgnoredHosts"  ,  buffer  .  toString  (  )  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  mFilteredSearchHosts  .  size  (  )  ;  i  ++  )  { 
String  [  ]  parts  =  (  String  [  ]  )  mFilteredSearchHosts  .  elementAt  (  i  )  ; 
buffer  .  append  (  parts  [  0  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  1  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  2  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  3  ]  )  ; 
buffer  .  append  (  ' '  )  ; 
} 
set  (  "mFilteredSearchHosts"  ,  buffer  .  toString  (  )  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  mNetInvalidHosts  .  size  (  )  ;  i  ++  )  { 
String  [  ]  parts  =  (  String  [  ]  )  mNetInvalidHosts  .  elementAt  (  i  )  ; 
buffer  .  append  (  parts  [  0  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  1  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  2  ]  )  ; 
buffer  .  append  (  '.'  )  ; 
buffer  .  append  (  parts  [  3  ]  )  ; 
buffer  .  append  (  ' '  )  ; 
} 
set  (  "mNetInvalidHosts"  ,  buffer  .  toString  (  )  )  ; 
int   size  =  filteredCatcherPorts  .  size  (  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
buffer  .  append  (  (  String  )  filteredCatcherPorts  .  get  (  i  )  )  ; 
buffer  .  append  (  " "  )  ; 
} 
set  (  "filteredCatcherPorts"  ,  buffer  .  toString  (  )  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  mNetNetworkHistory  .  size  (  )  ;  i  ++  )  { 
buffer  .  append  (  mNetNetworkHistory  .  elementAt  (  i  )  )  ; 
buffer  .  append  (  " "  )  ; 
} 
set  (  "mNetNetworkHistory"  ,  buffer  .  toString  (  )  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  connectToHistory  .  size  (  )  ;  i  ++  )  { 
buffer  .  append  (  connectToHistory  .  get  (  i  )  )  ; 
buffer  .  append  (  " "  )  ; 
} 
set  (  "connectToHistory"  ,  buffer  .  toString  (  )  )  ; 
buffer  .  setLength  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  searchTermHistory  .  size  (  )  ;  i  ++  )  { 
buffer  .  append  (  searchTermHistory  .  get  (  i  )  )  ; 
buffer  .  append  (  ','  )  ; 
} 
set  (  "searchTermHistory"  ,  buffer  .  toString  (  )  )  ; 
set  (  "mFontMenu"  ,  mFontMenu  .  getName  (  )  +  ";"  +  mFontMenu  .  getStyle  (  )  +  ";"  +  mFontMenu  .  getSize  (  )  )  ; 
set  (  "mFontLabel"  ,  mFontLabel  .  getName  (  )  +  ";"  +  mFontLabel  .  getStyle  (  )  +  ";"  +  mFontLabel  .  getSize  (  )  )  ; 
set  (  "mFontTable"  ,  mFontTable  .  getName  (  )  +  ";"  +  mFontTable  .  getStyle  (  )  +  ";"  +  mFontTable  .  getSize  (  )  )  ; 
}  catch  (  Exception   e  )  { 
Logger  .  logError  (  e  )  ; 
} 
} 

private   void   deserializeSimpleFields  (  )  { 
Field  [  ]  fields  =  this  .  getClass  (  )  .  getDeclaredFields  (  )  ; 
for  (  int   i  =  0  ;  i  <  fields  .  length  ;  i  ++  )  { 
String   name  =  fields  [  i  ]  .  getName  (  )  ; 
int   modifiers  =  fields  [  i  ]  .  getModifiers  (  )  ; 
Class   type  =  fields  [  i  ]  .  getType  (  )  ; 
String   value  =  ""  ; 
if  (  !  Modifier  .  isPublic  (  modifiers  )  ||  Modifier  .  isTransient  (  modifiers  )  ||  Modifier  .  isStatic  (  modifiers  )  )  { 
continue  ; 
} 
try  { 
value  =  get  (  name  )  ; 
if  (  value  ==  null  )  { 
continue  ; 
} 
if  (  type  .  getName  (  )  .  equals  (  "int"  )  )  { 
fields  [  i  ]  .  setInt  (  this  ,  Integer  .  parseInt  (  value  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "short"  )  )  { 
fields  [  i  ]  .  setShort  (  this  ,  Short  .  parseShort  (  value  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "long"  )  )  { 
fields  [  i  ]  .  setLong  (  this  ,  Long  .  parseLong  (  value  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "boolean"  )  )  { 
fields  [  i  ]  .  setBoolean  (  this  ,  value  .  equals  (  "true"  )  )  ; 
}  else   if  (  type  .  getName  (  )  .  equals  (  "java.lang.String"  )  )  { 
fields  [  i  ]  .  set  (  this  ,  value  )  ; 
} 
}  catch  (  Exception   exp  )  { 
Logger  .  logError  (  exp  ,  "Error in field: "  +  name  +  ", value: "  +  value  )  ; 
} 
} 
} 

private   void   deserializeComplexFields  (  )  { 
try  { 
try  { 
mProgramClientID  .  fromHexString  (  get  (  "mProgramClientID"  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
String   ignoredHosts  =  get  (  "mNetIgnoredHosts"  ,  ""  )  ; 
StringTokenizer   tokens  =  new   StringTokenizer  (  ignoredHosts  )  ; 
if  (  mNetIgnoredHosts  ==  null  )  { 
mNetIgnoredHosts  =  new   ArrayList  (  tokens  .  countTokens  (  )  )  ; 
}  else  { 
mNetIgnoredHosts  .  clear  (  )  ; 
mNetInvalidHosts  .  ensureCapacity  (  tokens  .  countTokens  (  )  )  ; 
} 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
mNetIgnoredHosts  .  add  (  IPUtils  .  splitIP2Parts  (  tokens  .  nextToken  (  )  )  )  ; 
} 
mNetInvalidHosts  .  trimToSize  (  )  ; 
String   filteredSearchHosts  =  get  (  "mFilteredSearchHosts"  ,  ""  )  ; 
if  (  filteredSearchHosts  !=  null  )  { 
tokens  =  new   StringTokenizer  (  filteredSearchHosts  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
mFilteredSearchHosts  .  addElement  (  IPUtils  .  splitIP2Parts  (  tokens  .  nextToken  (  )  )  )  ; 
} 
} 
String   invalidHosts  =  get  (  "mNetInvalidHosts"  )  ; 
if  (  invalidHosts  !=  null  )  { 
tokens  =  new   StringTokenizer  (  invalidHosts  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
mNetInvalidHosts  .  addElement  (  IPUtils  .  splitIP2Parts  (  tokens  .  nextToken  (  )  )  )  ; 
} 
} 
String   invalidPorts  =  get  (  "filteredCatcherPorts"  )  ; 
if  (  invalidPorts  !=  null  )  { 
tokens  =  new   StringTokenizer  (  invalidPorts  ,  " "  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
filteredCatcherPorts  .  add  (  tokens  .  nextToken  (  )  )  ; 
} 
} 
String   networkHistory  =  get  (  "mNetNetworkHistory"  ,  ""  )  ; 
{ 
tokens  =  new   StringTokenizer  (  networkHistory  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
mNetNetworkHistory  .  addElement  (  tokens  .  nextToken  (  )  )  ; 
} 
} 
String   connectToList  =  get  (  "connectToHistory"  ,  ""  )  ; 
if  (  connectToList  !=  null  )  { 
tokens  =  new   StringTokenizer  (  connectToList  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
connectToHistory  .  add  (  tokens  .  nextToken  (  )  )  ; 
} 
} 
String   searchTermList  =  get  (  "searchTermHistory"  ,  ""  )  ; 
if  (  searchTermList  !=  null  )  { 
tokens  =  new   StringTokenizer  (  searchTermList  ,  ","  )  ; 
while  (  tokens  .  hasMoreTokens  (  )  )  { 
searchTermHistory  .  add  (  tokens  .  nextToken  (  )  .  trim  (  )  )  ; 
} 
} 
String   font  ; 
font  =  get  (  "mFontMenu"  )  ; 
if  (  font  !=  null  )  { 
try  { 
tokens  =  new   StringTokenizer  (  font  ,  ";"  )  ; 
mFontMenu  =  new   Font  (  tokens  .  nextToken  (  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
font  =  get  (  "mFontLabel"  )  ; 
if  (  font  !=  null  )  { 
try  { 
tokens  =  new   StringTokenizer  (  font  ,  ";"  )  ; 
mFontLabel  =  new   Font  (  tokens  .  nextToken  (  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
font  =  get  (  "mFontTable"  )  ; 
if  (  font  !=  null  )  { 
try  { 
tokens  =  new   StringTokenizer  (  font  ,  ";"  )  ; 
mFontTable  =  new   Font  (  tokens  .  nextToken  (  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  ,  Integer  .  parseInt  (  tokens  .  nextToken  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
}  catch  (  Exception   exp  )  { 
Logger  .  logError  (  exp  )  ; 
} 
} 




public   void   updateHTTPProxySettings  (  )  { 
System  .  setProperty  (  "http.agent"  ,  Environment  .  getPhexVendor  (  )  )  ; 
if  (  isHttpProxyUsed  )  { 
System  .  setProperty  (  "http.proxyHost"  ,  httpProxyHost  )  ; 
System  .  setProperty  (  "http.proxyPort"  ,  String  .  valueOf  (  httpProxyPort  )  )  ; 
}  else  { 
System  .  setProperty  (  "http.proxyHost"  ,  ""  )  ; 
System  .  setProperty  (  "http.proxyPort"  ,  ""  )  ; 
} 
} 

private   void   handlePhexVersionAdjustments  (  )  { 
if  (  (  runningPhexVersion  ==  null  ||  runningPhexVersion  .  length  (  )  ==  0  )  &&  (  runningBuildNumber  ==  null  ||  runningBuildNumber  .  length  (  )  ==  0  )  )  { 
return  ; 
} 
if  (  runningPhexVersion  ==  null  ||  runningPhexVersion  .  length  (  )  ==  0  )  { 
runningPhexVersion  =  "0.6"  ; 
} 
if  (  VersionUtils  .  compare  (  "0.7"  ,  runningPhexVersion  )  >  0  )  { 
updatesFor0_7  (  )  ; 
} 
if  (  VersionUtils  .  compare  (  "0.8"  ,  runningPhexVersion  )  >  0  )  { 
updatesFor0_8  (  )  ; 
} 
if  (  runningBuildNumber  ==  null  ||  runningBuildNumber  .  length  (  )  ==  0  )  { 
runningBuildNumber  =  "35"  ; 
} 
if  (  VersionUtils  .  compare  (  "36"  ,  runningBuildNumber  )  >  0  )  { 
updatesForBuild36  (  )  ; 
} 
if  (  VersionUtils  .  compare  (  "42"  ,  runningBuildNumber  )  >  0  )  { 
updatesForBuild42  (  )  ; 
} 
runningBuildNumber  =  Environment  .  getInstance  (  )  .  getProperty  (  "build.number"  )  ; 
runningPhexVersion  =  Res  .  getStr  (  "Program.Version"  )  ; 
save  (  )  ; 
} 

private   void   updatesFor0_7  (  )  { 
try  { 
File   downloadListFile  =  Environment  .  getInstance  (  )  .  getPhexConfigFile  (  EnvironmentConstants  .  XML_DOWNLOAD_FILE_NAME  )  ; 
if  (  downloadListFile  .  exists  (  )  )  { 
FileUtils  .  copyFile  (  downloadListFile  ,  new   File  (  downloadListFile  .  getAbsolutePath  (  )  +  ".v0.6.4"  )  )  ; 
} 
}  catch  (  IOException   exp  )  { 
Logger  .  logError  (  exp  )  ; 
} 
runningPhexVersion  =  "0.7"  ; 
} 

private   void   updatesFor0_8  (  )  { 
peerConnections  =  mNetMinConn  ; 
runningPhexVersion  =  "0.8"  ; 
mUploadMaxSearch  =  64  ; 
mNetMaxHostToCatch  =  1000  ; 
} 

private   void   updatesForBuild36  (  )  { 
runningBuildNumber  =  "36"  ; 
allowToBecomeLeaf  =  allowUPConnections  ; 
} 

private   void   updatesForBuild42  (  )  { 
runningBuildNumber  =  "42"  ; 
if  (  up2upConnections  <  DEFAULT_UP_2_UP_CONNECTIONS  )  { 
up2upConnections  =  DEFAULT_UP_2_UP_CONNECTIONS  ; 
} 
} 
} 

