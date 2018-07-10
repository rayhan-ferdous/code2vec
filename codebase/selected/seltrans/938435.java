package   org  .  grlea  .  log  .  rollover  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  nio  .  channels  .  Channels  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  text  .  Format  ; 
import   java  .  text  .  MessageFormat  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  TimerTask  ; 











public   class   RolloverManager   extends   Writer  { 


private   static   final   String   KEY_PREFIX  =  "simplelog."  ; 


private   static   final   String   KEY_ROLLOVER_STRATEGY  =  KEY_PREFIX  +  "rollover"  ; 


private   static   final   String   ROLLOVER_STRATEGY_FILESIZE  =  "fileSize"  ; 


private   static   final   String   ROLLOVER_STRATEGY_TIMEOFDAY  =  "timeOfDay"  ; 


private   static   final   String   KEY_ACTIVE_LOG_FILE  =  KEY_PREFIX  +  "logFile"  ; 


private   static   final   String   KEY_ROLLOVER_LOG_FILE  =  KEY_PREFIX  +  "rollover.filename"  ; 


private   static   final   String   KEY_ROLLOVER_DIRECTORY  =  KEY_PREFIX  +  "rollover.directory"  ; 


private   static   final   String   KEY_ROLLOVER_PERIOD  =  KEY_PREFIX  +  "rollover.period"  ; 


private   static   final   String   DEFAULT_ROLLOVER_PERIOD  =  "60"  ; 





private   final   Object   PRINTERS_SEMAPHORE  =  new   Object  (  )  ; 


private   int   printerCount  =  0  ; 






private   final   Object   WRITER_CHANGE_GATE  =  new   Object  (  )  ; 


private   Writer   writer  ; 


private   StringWriter   tempWriter  =  new   StringWriter  (  1024  )  ; 


private   RolloverStrategy   strategy  ; 


private   String   currentRolloverStrategyName  ; 


private   File   currentActiveLogFile  ; 


private   File   currentActiveLogFileDirectory  ; 


private   File   rolloverDirectory  ; 


private   MessageFormat   rolloverLogFileFormat  ; 


private   RandomAccessFile   fileOut  ; 


private   boolean   strategySetProgramatically  =  false  ; 


private   long   rolloverPeriod  ; 


private   Timer   timer  ; 


private   int   uniqueFileId  =  0  ; 


private   ErrorReporter   errorReporter  ; 
















public   RolloverManager  (  Properties   properties  ,  ErrorReporter   errorReporter  )  throws   IOException  { 
if  (  properties  ==  null  )  throw   new   IllegalArgumentException  (  "properties cannot be null."  )  ; 
this  .  errorReporter  =  errorReporter  ; 
configure  (  properties  )  ; 
} 









public   void   configure  (  Properties   properties  )  throws   IOException  { 
configureStrategy  (  properties  )  ; 
configureWriter  (  properties  )  ; 
} 




private   void   configureStrategy  (  Properties   properties  )  throws   IOException  { 
if  (  strategySetProgramatically  )  { 
return  ; 
} 
String   rolloverStrategyString  =  properties  .  getProperty  (  KEY_ROLLOVER_STRATEGY  )  ; 
if  (  rolloverStrategyString  ==  null  )  { 
throw   new   IOException  (  "RolloverManger created, but rollover property not specified."  )  ; 
} 
rolloverStrategyString  =  rolloverStrategyString  .  trim  (  )  ; 
if  (  rolloverStrategyString  .  length  (  )  ==  0  )  { 
throw   new   IOException  (  "RolloverManger created, but rollover property not specified."  )  ; 
} 
boolean   strategyChanged  =  !  rolloverStrategyString  .  equals  (  currentRolloverStrategyName  )  ; 
RolloverStrategy   newStrategy  ; 
if  (  strategyChanged  )  { 
try  { 
if  (  ROLLOVER_STRATEGY_FILESIZE  .  equals  (  rolloverStrategyString  )  )  { 
newStrategy  =  new   FileSizeRolloverStrategy  (  )  ; 
}  else   if  (  ROLLOVER_STRATEGY_TIMEOFDAY  .  equals  (  rolloverStrategyString  )  )  { 
newStrategy  =  new   TimeOfDayRolloverStrategy  (  )  ; 
}  else  { 
try  { 
Class   strategyClass  =  Class  .  forName  (  rolloverStrategyString  )  ; 
Object   strategyObject  =  strategyClass  .  newInstance  (  )  ; 
if  (  !  (  strategyObject   instanceof   RolloverStrategy  )  )  { 
throw   new   IOException  (  strategyClass  .  getName  (  )  +  " is not a RolloverStrategy"  )  ; 
} 
newStrategy  =  (  RolloverStrategy  )  strategyObject  ; 
}  catch  (  ClassNotFoundException   e  )  { 
throw   new   IOException  (  "Class '"  +  rolloverStrategyString  +  "' not found: "  +  e  )  ; 
}  catch  (  InstantiationException   e  )  { 
throw   new   IOException  (  "Failed to create an instance of "  +  rolloverStrategyString  +  ": "  +  e  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
throw   new   IOException  (  "Failed to create an instance of "  +  rolloverStrategyString  +  ": "  +  e  )  ; 
} 
} 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  "Error creating RolloverStrategy: "  +  e  )  ; 
} 
}  else  { 
newStrategy  =  strategy  ; 
} 
try  { 
newStrategy  .  configure  (  Collections  .  unmodifiableMap  (  properties  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  "Error configuring RolloverStrategy: "  +  e  )  ; 
} 
this  .  strategy  =  newStrategy  ; 
currentRolloverStrategyName  =  rolloverStrategyString  ; 
} 




private   void   configureWriter  (  Properties   properties  )  throws   IOException  { 
String   newActiveLogFileName  =  properties  .  getProperty  (  KEY_ACTIVE_LOG_FILE  )  ; 
if  (  newActiveLogFileName  ==  null  )  throw   new   IOException  (  "RolloverManager created but no active log file name specified."  )  ; 
File   newActiveLogFile  =  new   File  (  newActiveLogFileName  .  trim  (  )  )  .  getAbsoluteFile  (  )  ; 
if  (  newActiveLogFile  .  isDirectory  (  )  )  throw   new   IOException  (  "The specified active log file name already exists as a directory."  )  ; 
File   newActiveLogFileDirectory  =  newActiveLogFile  .  getParentFile  (  )  ; 
if  (  newActiveLogFileDirectory  !=  null  )  newActiveLogFileDirectory  .  mkdirs  (  )  ;  else   throw   new   IOException  (  "Caanot access the active log file's parent directory."  )  ; 
String   newRolloverLogFileNamePattern  =  properties  .  getProperty  (  KEY_ROLLOVER_LOG_FILE  )  ; 
if  (  newRolloverLogFileNamePattern  ==  null  )  newRolloverLogFileNamePattern  =  "{1}-"  +  newActiveLogFile  .  getName  (  )  ; 
newRolloverLogFileNamePattern  =  newRolloverLogFileNamePattern  .  trim  (  )  ; 
MessageFormat   newRolloverLogFileNameFormat  ; 
try  { 
newRolloverLogFileNameFormat  =  new   MessageFormat  (  newRolloverLogFileNamePattern  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
throw   new   IOException  (  "Illegal pattern provided for rollover log file name: "  +  e  .  getMessage  (  )  )  ; 
} 
String   newRolloverDirectoryName  =  properties  .  getProperty  (  KEY_ROLLOVER_DIRECTORY  )  ; 
File   newRolloverDirectory  ; 
if  (  newRolloverDirectoryName  !=  null  )  newRolloverDirectory  =  new   File  (  newRolloverDirectoryName  .  trim  (  )  )  .  getAbsoluteFile  (  )  ;  else   newRolloverDirectory  =  newActiveLogFileDirectory  ; 
if  (  newRolloverDirectory  .  exists  (  )  &&  !  newRolloverDirectory  .  isDirectory  (  )  )  { 
throw   new   IOException  (  "The location specified for storing rolled log files is not a directory: "  +  newRolloverDirectory  )  ; 
} 
String   rolloverPeriodString  =  (  String  )  properties  .  get  (  KEY_ROLLOVER_PERIOD  )  ; 
if  (  rolloverPeriodString  ==  null  )  rolloverPeriodString  =  DEFAULT_ROLLOVER_PERIOD  ; 
rolloverPeriodString  =  rolloverPeriodString  .  trim  (  )  ; 
if  (  rolloverPeriodString  .  length  (  )  ==  0  )  rolloverPeriodString  =  DEFAULT_ROLLOVER_PERIOD  ; 
long   newRolloverPeriod  ; 
try  { 
newRolloverPeriod  =  Long  .  parseLong  (  rolloverPeriodString  )  ; 
if  (  newRolloverPeriod  <  1  )  throw   new   NumberFormatException  (  "Must be greater than 0"  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   IOException  (  "Invalid rollover period specified: "  +  rolloverPeriodString  +  " ("  +  e  .  getMessage  (  )  +  ")"  )  ; 
} 
Format  [  ]  formats  =  newRolloverLogFileNameFormat  .  getFormatsByArgumentIndex  (  )  ; 
boolean   uniqueIdUsedInPattern  =  formats  .  length  >  1  ; 
if  (  uniqueIdUsedInPattern  )  { 
String  [  ]  filenames  =  newRolloverDirectory  .  list  (  )  ; 
int   maximumFileId  =  0  ; 
for  (  int   i  =  0  ;  filenames  !=  null  &&  i  <  filenames  .  length  ;  i  ++  )  { 
String   filename  =  filenames  [  i  ]  ; 
try  { 
Object  [  ]  parameters  =  newRolloverLogFileNameFormat  .  parse  (  filename  )  ; 
if  (  parameters  .  length  >  1  &&  parameters  [  1  ]  !=  null  )  { 
String   uniqueFileIdString  =  (  String  )  parameters  [  1  ]  ; 
int   parsedFileId  =  Integer  .  parseInt  (  uniqueFileIdString  ,  16  )  ; 
if  (  parsedFileId  >  maximumFileId  )  maximumFileId  =  parsedFileId  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
}  catch  (  ParseException   e  )  { 
} 
} 
uniqueFileId  =  maximumFileId  +  1  ; 
} 
rolloverDirectory  =  newRolloverDirectory  ; 
rolloverLogFileFormat  =  newRolloverLogFileNameFormat  ; 
if  (  !  newActiveLogFile  .  equals  (  currentActiveLogFile  )  )  { 
openWriter  (  newActiveLogFile  ,  newActiveLogFileDirectory  )  ; 
currentActiveLogFile  =  newActiveLogFile  ; 
currentActiveLogFileDirectory  =  newActiveLogFileDirectory  ; 
} 
if  (  timer  ==  null  ||  newRolloverPeriod  !=  rolloverPeriod  )  { 
if  (  timer  !=  null  )  timer  .  cancel  (  )  ; 
timer  =  new   Timer  (  true  )  ; 
timer  .  schedule  (  new   RolloverTask  (  )  ,  0  ,  newRolloverPeriod  *  1000L  )  ; 
rolloverPeriod  =  newRolloverPeriod  ; 
} 
} 









private   void   openWriter  (  File   newActiveLogFile  ,  File   activeLogFileDirectory  )  throws   IOException  { 
synchronized  (  WRITER_CHANGE_GATE  )  { 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
if  (  printerCount  !=  0  )  { 
try  { 
PRINTERS_SEMAPHORE  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
throw   new   IllegalStateException  (  "Interrupted while attempting to configure writer"  )  ; 
} 
} 
if  (  writer  !=  tempWriter  )  { 
if  (  writer  !=  null  )  { 
try  { 
writer  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  "Failed to close open file: "  +  e  )  ; 
} 
} 
RandomAccessFile   newFileOut  =  new   RandomAccessFile  (  newActiveLogFile  ,  "rws"  )  ; 
FileChannel   channel  =  newFileOut  .  getChannel  (  )  ; 
long   initialChannelSize  =  channel  .  size  (  )  ; 
newFileOut  .  seek  (  initialChannelSize  )  ; 
Writer   newFileWriter  =  Channels  .  newWriter  (  newFileOut  .  getChannel  (  )  ,  "UTF-8"  )  ; 
storeFileCreationTimeIfNecessary  (  activeLogFileDirectory  ,  newActiveLogFile  )  ; 
fileOut  =  newFileOut  ; 
writer  =  newFileWriter  ; 
} 
} 
} 
} 









private   void   storeFileCreationTimeIfNecessary  (  File   activeLogFileDirectory  ,  File   newActiveLogFile  )  throws   IOException  { 
long   creationTime  =  System  .  currentTimeMillis  (  )  ; 
try  { 
File   creationTimeFile  =  getCreationTimeFile  (  activeLogFileDirectory  ,  newActiveLogFile  )  ; 
boolean   recordCreationTime  =  !  creationTimeFile  .  exists  (  )  ; 
if  (  recordCreationTime  )  { 
FileWriter   creationTimeFileOut  =  new   FileWriter  (  creationTimeFile  )  ; 
creationTimeFileOut  .  write  (  String  .  valueOf  (  creationTime  )  )  ; 
creationTimeFileOut  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  "Failed to write file creation time: "  +  e  )  ; 
} 
} 











private   long   readCreationTime  (  )  throws   IOException  { 
try  { 
File   creationTimeFile  =  getCreationTimeFile  (  currentActiveLogFileDirectory  ,  currentActiveLogFile  )  ; 
if  (  !  creationTimeFile  .  exists  (  )  )  storeFileCreationTimeIfNecessary  (  currentActiveLogFileDirectory  ,  currentActiveLogFile  )  ; 
FileReader   fileIn  =  new   FileReader  (  creationTimeFile  )  ; 
BufferedReader   in  =  new   BufferedReader  (  fileIn  )  ; 
String   firstLine  =  in  .  readLine  (  )  ; 
in  .  close  (  )  ; 
return   Long  .  parseLong  (  firstLine  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IOException  (  "Error reading creation time file: "  +  e  )  ; 
}  catch  (  NumberFormatException   e  )  { 
throw   new   IOException  (  "Error reading creation time: "  +  e  )  ; 
} 
} 











private   File   getCreationTimeFile  (  File   activeLogFileDirectory  ,  File   newActiveLogFile  )  { 
return   new   File  (  activeLogFileDirectory  ,  newActiveLogFile  .  getName  (  )  +  "-CREATED"  )  ; 
} 






public   void   close  (  )  { 
synchronized  (  WRITER_CHANGE_GATE  )  { 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
if  (  printerCount  !=  0  )  { 
try  { 
PRINTERS_SEMAPHORE  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
throw   new   IllegalStateException  (  "Interrupted while attempting to configure writer"  )  ; 
} 
} 
writer  =  tempWriter  ; 
} 
timer  .  cancel  (  )  ; 
try  { 
writer  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
reportError  (  "Error closing RolloverManager's writer"  ,  e  ,  true  )  ; 
} 
try  { 
fileOut  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
reportError  (  "Error closing RolloverManager's file"  ,  e  ,  true  )  ; 
} 
} 
} 











private   void   reportError  (  String   description  ,  IOException   e  ,  boolean   printExceptionType  )  { 
if  (  errorReporter  !=  null  )  errorReporter  .  error  (  description  ,  e  ,  printExceptionType  )  ; 
} 




public   void   flush  (  )  { 
} 






public   void   write  (  char   cbuf  [  ]  ,  int   off  ,  int   len  )  throws   IOException  { 
synchronized  (  WRITER_CHANGE_GATE  )  { 
} 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
printerCount  ++  ; 
} 
try  { 
writer  .  write  (  cbuf  ,  off  ,  len  )  ; 
writer  .  flush  (  )  ; 
}  finally  { 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
printerCount  --  ; 
if  (  printerCount  ==  0  )  { 
PRINTERS_SEMAPHORE  .  notifyAll  (  )  ; 
} 
} 
} 
} 







public   void   rolloverIfNecessary  (  )  throws   IOException  { 
long   creationTime  =  readCreationTime  (  )  ; 
FileChannel   activeFileChannel  =  fileOut  .  getChannel  (  )  ; 
long   fileLength  =  activeFileChannel  .  size  (  )  ; 
Date   creationDate  =  new   Date  (  creationTime  )  ; 
boolean   rolloverNow  =  strategy  .  rolloverNow  (  creationDate  ,  fileLength  )  ; 
if  (  rolloverNow  )  { 
synchronized  (  WRITER_CHANGE_GATE  )  { 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
if  (  printerCount  !=  0  )  { 
try  { 
PRINTERS_SEMAPHORE  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
throw   new   IllegalStateException  (  "Interrupted while attempting to configure writer"  )  ; 
} 
} 
writer  =  tempWriter  ; 
} 
} 
int   fileIdNumber  =  uniqueFileId  ++  ; 
StringBuffer   fileIdString  =  new   StringBuffer  (  )  ; 
fileIdString  .  append  (  Integer  .  toHexString  (  fileIdNumber  )  .  toUpperCase  (  )  )  ; 
char  [  ]  zeroes  =  new   char  [  8  -  fileIdString  .  length  (  )  ]  ; 
Arrays  .  fill  (  zeroes  ,  '0'  )  ; 
fileIdString  .  insert  (  0  ,  zeroes  )  ; 
String   rolloverFileName  =  rolloverLogFileFormat  .  format  (  new   Object  [  ]  {  new   Date  (  )  ,  fileIdString  .  toString  (  )  }  )  ; 
if  (  !  rolloverDirectory  .  exists  (  )  )  rolloverDirectory  .  mkdirs  (  )  ; 
File   rolloverFile  =  new   File  (  rolloverDirectory  ,  rolloverFileName  )  ; 
FileOutputStream   rolloverOut  =  new   FileOutputStream  (  rolloverFile  ,  false  )  ; 
FileChannel   rolloverChannel  =  rolloverOut  .  getChannel  (  )  ; 
activeFileChannel  .  transferTo  (  0  ,  activeFileChannel  .  size  (  )  ,  rolloverChannel  )  ; 
rolloverOut  .  close  (  )  ; 
activeFileChannel  .  truncate  (  0  )  ; 
fileOut  .  seek  (  0  )  ; 
Writer   newFileWriter  =  Channels  .  newWriter  (  activeFileChannel  ,  "UTF-8"  )  ; 
File   creationTimeFile  =  getCreationTimeFile  (  currentActiveLogFileDirectory  ,  currentActiveLogFile  )  ; 
creationTimeFile  .  delete  (  )  ; 
storeFileCreationTimeIfNecessary  (  currentActiveLogFileDirectory  ,  currentActiveLogFile  )  ; 
synchronized  (  WRITER_CHANGE_GATE  )  { 
synchronized  (  PRINTERS_SEMAPHORE  )  { 
if  (  printerCount  !=  0  )  { 
try  { 
PRINTERS_SEMAPHORE  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
throw   new   IllegalStateException  (  "Interrupted while attempting to configure writer"  )  ; 
} 
} 
writer  =  newFileWriter  ; 
StringBuffer   tempBuffer  =  tempWriter  .  getBuffer  (  )  ; 
newFileWriter  .  write  (  tempBuffer  .  toString  (  )  )  ; 
tempBuffer  .  delete  (  0  ,  tempBuffer  .  length  (  )  )  ; 
} 
} 
} 
} 





public   RolloverStrategy   getStrategy  (  )  { 
return   strategy  ; 
} 










public   void   setStrategy  (  RolloverStrategy   strategy  )  { 
if  (  strategy  ==  null  )  { 
throw   new   IllegalArgumentException  (  "strategy cannot be null."  )  ; 
} 
this  .  strategy  =  strategy  ; 
strategySetProgramatically  =  true  ; 
} 




private   final   class   RolloverTask   extends   TimerTask  { 

public   void   run  (  )  { 
try  { 
rolloverIfNecessary  (  )  ; 
}  catch  (  IOException   e  )  { 
reportError  (  "SimpleLog ERROR: Failed to check or attempt rollover"  ,  e  ,  true  )  ; 
} 
} 
} 

public   static   Writer   createRolloverManager  (  Properties   properties  ,  ErrorReporter   errorReporter  )  throws   IOException  { 
return   new   RolloverManager  (  properties  ,  errorReporter  )  ; 
} 





public   static   interface   ErrorReporter  { 

public   void   error  (  String   description  ,  Throwable   t  ,  boolean   printExceptionType  )  ; 
} 
} 

