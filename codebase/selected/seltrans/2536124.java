package   com  .  ibm  .  tuningfork  .  infra  .  stream  .  core  ; 

import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  zip  .  CRC32  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  Logging  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  data  .  TimeInterval  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  dogfooder  .  DogFooder  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  event  .  EventType  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  event  .  IEvent  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  event  .  TypedEvent  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  feed  .  Feed  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  feed  .  IDataSource  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  feed  .  IFeeder  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  feed  .  StreamManager  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  filter  .  Filter  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  sharing  .  ISharingConvertible  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  sharing  .  ISharingConvertibleCallback  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  sharing  .  UnimplementedConversionException  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  expression  .  Operand  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  expression  .  Operator  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  expression  .  StreamOperand  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  expression  .  base  .  StreamContext  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  expression  .  base  .  TupleExpression  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  stream  .  precise  .  PreciseStream  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  units  .  ITimeConverter  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  units  .  SimpleTimeConverter  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  units  .  Unit  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  util  .  MiscUtils  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  util  .  Semaphore  ; 
import   com  .  ibm  .  tuningfork  .  infra  .  util  .  StringUtils  ; 
import   com  .  ibm  .  tuningfork  .  tracegen  .  IFeedlet  ; 
import   com  .  ibm  .  tuningfork  .  tracegen  .  ITimerEvent  ; 
















public   abstract   class   Stream   implements   ISharingConvertible  { 

public   enum   StreamMode  { 

DERIVED_FROM_FEED  ,  DERIVED_FROM_STREAMS  ,  DERIVED_FROM_OTHER  ,  MANUAL 
} 

; 

public   static   final   int   STREAM_TIMEOUT_NEVER  =  Integer  .  MAX_VALUE  ; 

protected   static   final   int   DEFAULT_YIELD_FREQUENCY  =  50000  ; 

protected   static   final   int   POLLING_INTERVAL_FOR_COMPLETED_STREAM_IN_MS  =  50  ; 

public   static   int   MAXIMUM_CONCURRENT_STREAMS  =  100  ; 

protected   static   final   Semaphore   threadCreationController  =  new   Semaphore  (  "Stream Thread Creation Controller"  ,  MAXIMUM_CONCURRENT_STREAMS  )  ; 

protected   StreamMode   streamMode  ; 

private   long   startTimeMillis  =  0  ; 

private   final   Object   notifier  =  new   Object  (  )  ; 

protected   int   yieldFrequency  =  DEFAULT_YIELD_FREQUENCY  ; 

protected   int   totalYields  ; 

protected   int   yieldCount  ; 

private   Operator   operator  ; 

private   Operand  [  ]  operands  ; 

private   String   displayName  ; 

private   String   canonicalName  ; 

private   long   hashedId  ; 

protected   IDataSource  [  ]  dataSources  ; 

protected   ITimeConverter   timeConverter  ; 

protected   long  [  ]  cursors  ; 

protected   Unit   unit  ; 

protected   final   EventType   eventType  ; 

protected   TypedEvent   lastSeenEvent  =  null  ; 

protected   boolean   started  =  false  ; 

private   boolean   isClosed  =  false  ; 

protected   boolean   modified  =  false  ; 

private   boolean   invisible  =  false  ; 

protected   IFeedlet   selfProfilingFeedlet  ; 

protected   ITimerEvent   streamRunningTimer  ; 

protected   ITimerEvent   streamYieldedTimer  ; 

protected   NumberFormat   timeFormatter  =  new   DecimalFormat  (  "#.###"  )  ; 

protected   StreamContext   streamContext  ; 

protected   TupleExpression   expression  ; 

public   Stream  (  String   name  ,  Feed   feed  ,  Unit   unit  ,  EventType   type  )  { 
this  (  name  ,  new   IDataSource  [  ]  {  feed  }  ,  unit  ,  type  )  ; 
} 

public   Stream  (  String   name  ,  IDataSource  [  ]  dataSource  ,  Unit   unit  ,  EventType   type  )  { 
this  .  streamMode  =  StreamMode  .  DERIVED_FROM_FEED  ; 
this  .  timeConverter  =  dataSource  [  0  ]  .  getTimeConverter  (  )  ; 
if  (  !  Filter  .  checkConsistency  (  dataSource  )  )  { 
Logging  .  errorln  (  "Creation of stream with inconsistent time converters"  )  ; 
} 
this  .  dataSources  =  dataSource  ; 
this  .  operator  =  new   Operator  (  "Base"  )  ; 
this  .  operands  =  StreamOperand  .  EMPTY  ; 
this  .  canonicalName  =  "Base("  ; 
for  (  IDataSource   source  :  dataSource  )  { 
long   id  =  source  .  getCanonicalId  (  )  ; 
this  .  canonicalName  +=  (  id  +  ","  )  ; 
} 
this  .  canonicalName  +=  name  +  ")"  ; 
this  .  eventType  =  type  ; 
commonInit  (  name  ,  dataSource  ,  unit  )  ; 
} 









protected   Stream  (  String   name  ,  StreamContext   context  ,  TupleExpression   expression  ,  Unit   unit  ,  EventType   eventType  )  { 
this  .  streamMode  =  StreamMode  .  DERIVED_FROM_STREAMS  ; 
canonicalName  =  expression  .  toString  (  )  ; 
streamContext  =  context  ; 
this  .  eventType  =  eventType  ; 
this  .  expression  =  expression  ; 
operator  =  new   Operator  (  "ForkTalk"  )  ; 
Stream  [  ]  streams  =  context  .  getStreams  (  )  ; 
operands  =  new   Operand  [  streams  .  length  ]  ; 
ArrayList  <  IDataSource  >  feedList  =  new   ArrayList  <  IDataSource  >  (  )  ; 
int   index  =  0  ; 
for  (  Stream   stream  :  streams  )  { 
timeConverter  =  stream  .  getTimeConverter  (  )  ; 
IDataSource  [  ]  sources  =  stream  .  dataSources  ; 
for  (  IDataSource   ds  :  sources  )  { 
if  (  !  feedList  .  contains  (  ds  )  )  { 
feedList  .  add  (  ds  )  ; 
} 
} 
operands  [  index  ++  ]  =  new   StreamOperand  (  stream  .  getName  (  )  ,  stream  )  ; 
} 
if  (  feedList  .  size  (  )  >  0  )  { 
dataSources  =  new   IDataSource  [  feedList  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  dataSources  .  length  ;  i  ++  )  { 
this  .  dataSources  [  i  ]  =  feedList  .  get  (  i  )  ; 
} 
}  else  { 
IDataSource   defaultSource  =  streamContext  .  getDataSource  (  )  ; 
if  (  defaultSource  ==  null  )  { 
throw   new   IllegalStateException  (  "No data source available for stream construction"  )  ; 
} 
if  (  timeConverter  ==  null  )  { 
timeConverter  =  defaultSource  .  getTimeConverter  (  )  ; 
if  (  timeConverter  ==  null  )  { 
timeConverter  =  new   SimpleTimeConverter  (  1e9  )  ; 
} 
} 
dataSources  =  new   IDataSource  [  ]  {  defaultSource  }  ; 
} 
commonInit  (  name  ,  dataSources  ,  unit  )  ; 
} 











public   Stream  (  String   name  ,  String   operatorName  ,  Operand  [  ]  operands  ,  Unit   unit  ,  EventType   type  )  { 
this  .  streamMode  =  StreamMode  .  DERIVED_FROM_STREAMS  ; 
this  .  timeConverter  =  null  ; 
for  (  Operand   op  :  operands  )  { 
if  (  op   instanceof   StreamOperand  )  { 
timeConverter  =  (  (  StreamOperand  )  op  )  .  stream  .  getTimeConverter  (  )  ; 
} 
} 
this  .  operands  =  operands  ; 
this  .  operator  =  new   Operator  (  operatorName  )  ; 
ArrayList  <  IDataSource  >  feedList  =  new   ArrayList  <  IDataSource  >  (  )  ; 
for  (  Operand   op  :  operands  )  { 
if  (  op   instanceof   StreamOperand  )  { 
IDataSource  [  ]  sources  =  (  (  StreamOperand  )  op  )  .  stream  .  dataSources  ; 
for  (  IDataSource   ds  :  sources  )  { 
if  (  !  feedList  .  contains  (  ds  )  )  { 
feedList  .  add  (  ds  )  ; 
} 
} 
} 
} 
this  .  dataSources  =  new   IDataSource  [  feedList  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  dataSources  .  length  ;  i  ++  )  { 
this  .  dataSources  [  i  ]  =  feedList  .  get  (  i  )  ; 
} 
canonicalName  =  operator  .  getCanonicalId  (  )  +  "("  ; 
for  (  int   i  =  0  ;  i  <  operands  .  length  ;  i  ++  )  { 
canonicalName  +=  operands  [  i  ]  .  getCanonicalName  (  )  +  (  i  !=  operands  .  length  -  1  ?  ","  :  ""  )  ; 
} 
canonicalName  +=  ")"  ; 
this  .  eventType  =  type  ; 
commonInit  (  name  ,  dataSources  ,  unit  )  ; 
} 

protected   void   commonInit  (  String   displayName  ,  IDataSource  [  ]  dataSource  ,  Unit   unit  )  { 
this  .  displayName  =  displayName  ; 
CRC32   crc  =  new   CRC32  (  )  ; 
crc  .  update  (  canonicalName  .  getBytes  (  )  )  ; 
this  .  hashedId  =  crc  .  getValue  (  )  ; 
this  .  unit  =  unit  ; 
this  .  cursors  =  new   long  [  this  .  dataSources  .  length  ]  ; 
getStreamManager  (  )  .  addStream  (  this  )  ; 
} 

public   static   Stream   reconstituteFromExisting  (  String   canonicalName  ,  IDataSource   dataSources  [  ]  )  throws   UnimplementedConversionException  { 
if  (  dataSources  ==  null  )  return   null  ; 
for  (  IDataSource   ds  :  dataSources  )  for  (  Stream   s  :  ds  .  getAllStreams  (  )  )  if  (  canonicalName  .  equals  (  s  .  canonicalName  )  )  return   s  ; 
throw   new   UnimplementedConversionException  (  "Stream: "  +  canonicalName  +  " does not exist"  )  ; 
} 

public   final   void   collectReconstructionArguments  (  ISharingConvertibleCallback   cb  )  throws   Exception  { 
cb  .  setStaticMethod  (  Stream  .  class  ,  "reconstituteFromExisting"  )  ; 
cb  .  convert  (  canonicalName  )  ; 
cb  .  convert  (  getDataSources  (  )  )  ; 
collectSpecificReconstructionArguments  (  cb  .  nextTry  (  )  )  ; 
} 

public   void   collectSpecificReconstructionArguments  (  ISharingConvertibleCallback   cb  )  throws   Exception  { 
collectAllButLastReconstructionArguments  (  cb  )  ; 
cb  .  convert  (  unit  )  ; 
} 

protected   void   collectAllButLastReconstructionArguments  (  ISharingConvertibleCallback   cb  )  throws   Exception  { 
if  (  streamMode  ==  StreamMode  .  DERIVED_FROM_FEED  ||  streamMode  ==  StreamMode  .  MANUAL  )  { 
cb  .  convert  (  displayName  )  ; 
cb  .  convert  (  dataSources  )  ; 
}  else   if  (  streamMode  ==  StreamMode  .  DERIVED_FROM_STREAMS  )  { 
cb  .  convert  (  displayName  )  ; 
cb  .  convert  (  operator  .  name  )  ; 
cb  .  convert  (  operands  )  ; 
}  else   throw   new   Exception  (  "unimplemented streamMode: "  +  streamMode  )  ; 
} 

public   StreamMode   getStreamMode  (  )  { 
return   streamMode  ; 
} 

public   Unit   getUnit  (  )  { 
return   unit  ; 
} 

public   String   getCanonicalName  (  )  { 
return   canonicalName  ; 
} 

public   abstract   long   getLength  (  )  ; 

public   abstract   IStreamCursor   newCursor  (  long   startTime  ,  long   endTime  )  ; 

public   IStreamCursor   newCursor  (  )  { 
return   newCursor  (  TimeInterval  .  ALL_TIME  )  ; 
} 

public   IStreamCursor   newCursor  (  TimeInterval   range  )  { 
return   newCursor  (  range  .  getStart  (  )  ,  range  .  getEnd  (  )  )  ; 
} 




public   boolean   isReady  (  )  { 
for  (  IDataSource   dataSource  :  dataSources  )  { 
if  (  !  dataSource  .  isReady  (  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 




public   boolean   isFinite  (  )  { 
for  (  IDataSource   f  :  dataSources  )  { 
if  (  !  (  f  .  isFinite  (  )  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 

public   IDataSource  [  ]  getDataSources  (  )  { 
return   dataSources  ; 
} 

public   String   getName  (  )  { 
return   displayName  ; 
} 

public   String   getId  (  )  { 
return   canonicalName  ; 
} 

public   Operator   getOperator  (  )  { 
return   operator  ; 
} 

public   Operand  [  ]  getOperands  (  )  { 
return   operands  ; 
} 

public   List  <  Stream  >  getStreamOperands  (  )  { 
List  <  Stream  >  streams  =  new   ArrayList  <  Stream  >  (  )  ; 
for  (  Operand   operand  :  getOperands  (  )  )  { 
if  (  operand   instanceof   StreamOperand  )  { 
streams  .  add  (  (  (  StreamOperand  )  operand  )  .  stream  )  ; 
} 
} 
return   streams  ; 
} 

public   boolean   supportsDrilldown  (  )  { 
return   false  ; 
} 

public   List  <  Stream  >  getDrilldownStreams  (  )  { 
return   null  ; 
} 

public   void   delete  (  )  { 
unregister  (  )  ; 
Logging  .  verboseln  (  1  ,  "Attempted to delete stream '"  +  getName  (  )  +  "' but we just unregistered it and made it invisible"  )  ; 
} 

public   Stream   makeInvisible  (  )  { 
return   setInvisible  (  true  )  ; 
} 

public   Stream   setInvisible  (  boolean   b  )  { 
if  (  invisible  !=  b  )  { 
invisible  =  b  ; 
getStreamManager  (  )  .  streamVisibilityChanged  (  this  )  ; 
} 
return   this  ; 
} 

public   Stream   makeVisible  (  )  { 
return   setInvisible  (  false  )  ; 
} 

public   boolean   isInvisible  (  )  { 
return   invisible  ; 
} 

public   boolean   isVisible  (  )  { 
return  !  invisible  ; 
} 





public   void   unregister  (  )  { 
getStreamManager  (  )  .  removeStream  (  this  )  ; 
} 

public   void   waitForMore  (  )  { 
if  (  isClosed  (  )  )  { 
return  ; 
} 
synchronized  (  notifier  )  { 
try  { 
notifier  .  wait  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 

public   String   getRelativeTimeLabel  (  long   time  )  { 
long   startTime  =  getTimeRange  (  )  .  getStart  (  )  ; 
ITimeConverter   timeConverter  =  getTimeConverter  (  )  ; 
String   timeString  =  timeFormatter  .  format  (  timeConverter  .  tickToSecond  (  time  -  startTime  )  )  +  " sec"  ; 
return   timeString  ; 
} 

public   TimeInterval   getTimeRange  (  )  { 
TimeInterval   range  =  null  ; 
for  (  int   i  =  0  ;  i  <  dataSources  .  length  ;  i  ++  )  { 
TimeInterval   frange  =  dataSources  [  i  ]  .  getTimeRange  (  )  ; 
if  (  range  ==  null  )  { 
range  =  frange  ; 
}  else  { 
range  =  range  .  expand  (  frange  )  ; 
} 
} 
if  (  range  ==  null  )  { 
range  =  new   TimeInterval  (  0  ,  0  )  ; 
} 
return   range  ; 
} 

public   ITimeConverter   getTimeConverter  (  )  { 
return   timeConverter  ; 
} 

public   int   getCategoryCount  (  )  { 
return   1  ; 
} 

public   String   getCategoryName  (  int   index  )  { 
return   displayName  ; 
} 

public   boolean   hasInfo  (  )  { 
return   false  ; 
} 

public   String  [  ]  getInfo  (  )  { 
return   null  ; 
} 

public   IStreamStatistics   getStatistics  (  )  { 
return   null  ; 
} 

public   void   derivedRun  (  )  { 
} 

public   boolean   isClosed  (  )  { 
return   isClosed  ; 
} 

protected   void   close  (  )  { 
close  (  null  )  ; 
} 

protected   void   close  (  long   numSamples  )  { 
close  (  "  There were "  +  numSamples  +  " samples."  )  ; 
} 

protected   void   close  (  long   numSamples  ,  long   numSummaries  )  { 
close  (  "  There were "  +  numSamples  +  " samples and "  +  numSummaries  +  " summaries."  )  ; 
} 

protected   void   close  (  long   numSamples  ,  long  [  ]  numSummaries  )  { 
close  (  "  There were "  +  numSamples  +  " samples and ["  +  StringUtils  .  arrayToString  (  numSummaries  ,  ", "  )  +  "] summaries."  )  ; 
} 

protected   void   close  (  PreciseStream   unsummarized  ,  PreciseStream  [  ]  summaries  )  { 
long  [  ]  summaryLengths  =  new   long  [  summaries  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  summaries  .  length  ;  i  ++  )  { 
summaryLengths  [  i  ]  =  summaries  [  i  ]  .  length  (  )  ; 
} 
close  (  "  There were "  +  unsummarized  .  length  (  )  +  " samples and ["  +  StringUtils  .  arrayToString  (  summaryLengths  ,  ", "  )  +  "] summaries."  )  ; 
} 

protected   void   close  (  String   info  )  { 
if  (  isClosed  )  { 
Logging  .  verboseln  (  2  ,  "Calling Stream.close on stream that is already closed.  Name = "  +  displayName  )  ; 
return  ; 
} 
isClosed  =  true  ; 
long   durationMs  =  System  .  currentTimeMillis  (  )  -  startTimeMillis  ; 
Logging  .  verboseln  (  1  ,  "Stream '"  +  getName  (  )  +  "' closed.  Took "  +  (  durationMs  /  1e3  )  +  " sec with "  +  totalYields  +  " yields."  )  ; 
if  (  info  !=  null  )  { 
Logging  .  verboseln  (  1  ,  info  )  ; 
}  else  { 
Logging  .  verboseln  (  1  ,  ""  )  ; 
} 
Logging  .  verboseln  (  1  ,  "    hashedId =  "  +  hashedId  +  "    canonicalName =  "  +  canonicalName  )  ; 
modifyCheck  (  )  ; 
} 





protected   void   modify  (  )  { 
modified  =  true  ; 
modifyCheckInternal  (  false  )  ; 
} 




public   void   modifyCheck  (  )  { 
modifyCheckInternal  (  true  )  ; 
} 

private   void   modifyCheckInternal  (  boolean   force  )  { 
if  (  force  ||  yieldCheck  (  )  )  { 
synchronized  (  notifier  )  { 
notifier  .  notifyAll  (  )  ; 
} 
} 
} 

private   boolean   yieldCheck  (  )  { 
if  (  --  yieldCount  <=  0  )  { 
yieldCount  =  yieldFrequency  ; 
if  (  DogFooder  .  EAT_DOGFOOD  )  { 
streamYieldedTimer  .  start  (  )  ; 
} 
MiscUtils  .  milliSleep  (  10  )  ; 
totalYields  ++  ; 
if  (  DogFooder  .  EAT_DOGFOOD  )  { 
streamYieldedTimer  .  stop  (  )  ; 
} 
return   true  ; 
} 
return   false  ; 
} 

public   boolean   isRunning  (  )  { 
return   started  ; 
} 

protected   void   derivedFromFeedRun  (  )  { 
IFeeder   source  =  (  IFeeder  )  dataSources  [  0  ]  ; 
while  (  true  )  { 
TypedEvent   e  =  source  .  getEvent  (  cursors  [  0  ]  )  ; 
if  (  e  !=  null  )  { 
cursors  [  0  ]  ++  ; 
addEvent  (  e  )  ; 
lastSeenEvent  =  e  ; 
modify  (  )  ; 
}  else   if  (  source  .  isClosed  (  )  )  { 
if  (  cursors  [  0  ]  >=  source  .  numberOfEvents  (  )  )  { 
break  ; 
}  else  { 
Logging  .  errorln  (  "missing event "  +  cursors  [  0  ]  +  " from feed"  )  ; 
break  ; 
} 
}  else  { 
modifyCheck  (  )  ; 
MiscUtils  .  milliSleep  (  100  )  ; 
} 
} 
} 

public   synchronized   void   start  (  )  { 
if  (  streamMode  ==  StreamMode  .  MANUAL  )  { 
return  ; 
} 
if  (  started  )  { 
return  ; 
} 
threadCreationController  .  acquireUninterruptibly  (  )  ; 
if  (  DogFooder  .  EAT_DOGFOOD  )  { 
selfProfilingFeedlet  =  DogFooder  .  logger  .  makeFeedlet  (  getName  (  )  ,  "Events from Thread for Stream "  +  getName  (  )  )  ; 
streamRunningTimer  =  DogFooder  .  logger  .  makeTimerEvent  (  "Stream Run - "  +  getName  (  )  )  ; 
streamYieldedTimer  =  DogFooder  .  logger  .  makeTimerEvent  (  "Stream Yield - "  +  getName  (  )  )  ; 
} 
Thread   t  =  new   Thread  (  getName  (  )  )  { 

public   void   run  (  )  { 
try  { 
if  (  DogFooder  .  EAT_DOGFOOD  )  { 
selfProfilingFeedlet  .  bindToCurrentThread  (  )  ; 
streamRunningTimer  .  start  (  )  ; 
} 
setPriority  (  MIN_PRIORITY  )  ; 
started  =  true  ; 
startTimeMillis  =  System  .  currentTimeMillis  (  )  ; 
if  (  streamMode  ==  StreamMode  .  DERIVED_FROM_FEED  )  { 
derivedFromFeedRun  (  )  ; 
}  else   if  (  expression  !=  null  )  { 
forkTalkRun  (  )  ; 
}  else  { 
derivedRun  (  )  ; 
} 
close  (  )  ; 
if  (  DogFooder  .  EAT_DOGFOOD  )  { 
streamRunningTimer  .  stop  (  )  ; 
} 
}  catch  (  Throwable   e  )  { 
Logging  .  errorln  (  "Stream "  +  getName  (  )  +  " died with exception "  +  e  .  getMessage  (  )  )  ; 
e  .  printStackTrace  (  )  ; 
}  finally  { 
threadCreationController  .  release  (  )  ; 
} 
} 
}  ; 
t  .  start  (  )  ; 
while  (  !  started  )  { 
MiscUtils  .  milliSleep  (  10  )  ; 
} 
} 




private   void   forkTalkRun  (  )  { 
streamContext  .  reset  (  this  )  ; 
while  (  streamContext  .  hasMore  (  )  )  { 
streamContext  .  advance  (  )  ; 
IEvent   value  =  (  IEvent  )  expression  .  getTupleValue  (  streamContext  )  ; 
if  (  value  !=  null  )  { 
streamContext  .  addEvent  (  value  )  ; 
} 
} 
} 









public   void   addEvent  (  IEvent   e  )  { 
throw   new   RuntimeException  (  "Stream.addEvent has been called.  Improperly implemented stream."  )  ; 
} 







public   EventType   getEventType  (  )  { 
return   eventType  ; 
} 

public   abstract   void   clear  (  )  ; 

public   String   toString  (  )  { 
return   getName  (  )  +  "["  +  getOperator  (  )  +  "]"  ; 
} 

public   Stream   waitUntilComplete  (  )  { 
return   waitUntilComplete  (  STREAM_TIMEOUT_NEVER  )  ; 
} 

public   Stream   waitUntilComplete  (  int   timeoutInMS  )  { 
if  (  !  isFinite  (  )  )  { 
throw   new   RuntimeException  (  "Attempt to wait for completion of a non-finite stream"  )  ; 
} 
if  (  !  isRunning  (  )  )  { 
start  (  )  ; 
} 
long   started  =  System  .  currentTimeMillis  (  )  ; 
long   reported  =  0  ; 
while  (  (  !  isClosed  (  )  )  )  { 
long   timeWaited  =  System  .  currentTimeMillis  (  )  -  started  ; 
if  (  timeWaited  >=  timeoutInMS  )  { 
String   reason  =  "Timed out waiting for completion of stream "  +  getName  (  )  +  " after "  +  timeWaited  +  "ms"  ; 
Logging  .  errorln  (  reason  )  ; 
return   null  ; 
} 
try  { 
if  (  timeWaited  -  reported  >  5000  )  { 
Logging  .  msgln  (  "Sleeping on "  +  getName  (  )  +  "; so far waited "  +  timeWaited  +  " ms"  )  ; 
reported  =  timeWaited  ; 
} 
Thread  .  sleep  (  POLLING_INTERVAL_FOR_COMPLETED_STREAM_IN_MS  )  ; 
}  catch  (  InterruptedException   e  )  { 
return   null  ; 
} 
} 
return   this  ; 
} 

public   static   boolean   allStreamsClosed  (  Stream  [  ]  streams  )  { 
for  (  int   i  =  0  ;  i  <  streams  .  length  ;  i  ++  )  { 
if  (  !  streams  [  i  ]  .  isClosed  (  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 

protected   StreamManager   getStreamManager  (  )  { 
StreamManager   mgr  =  null  ; 
for  (  IDataSource   dataSource  :  dataSources  )  { 
StreamManager   tmp  =  dataSource  .  getStreamManager  (  )  ; 
if  (  mgr  !=  null  &&  mgr  !=  tmp  )  { 
return   dataSource  .  getFeedGroup  (  )  .  getStreamManager  (  )  ; 
}  else  { 
mgr  =  tmp  ; 
} 
} 
return   mgr  ; 
} 
} 

