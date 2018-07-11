package   atnf  .  atoms  .  mon  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   javax  .  swing  .  event  .  *  ; 
import   atnf  .  atoms  .  time  .  *  ; 
import   atnf  .  atoms  .  util  .  *  ; 
import   atnf  .  atoms  .  mon  .  translation  .  *  ; 
import   atnf  .  atoms  .  mon  .  transaction  .  *  ; 
import   atnf  .  atoms  .  mon  .  externalsystem  .  *  ; 
import   atnf  .  atoms  .  mon  .  alarmcheck  .  *  ; 
import   atnf  .  atoms  .  mon  .  archivepolicy  .  *  ; 
import   atnf  .  atoms  .  mon  .  archiver  .  *  ; 
import   atnf  .  atoms  .  mon  .  util  .  *  ; 
import   org  .  apache  .  log4j  .  Logger  ; 







public   class   PointDescription   implements   ActionListener  ,  NamedObject  ,  Comparable  { 


protected   static   Logger   theirLogger  =  Logger  .  getLogger  (  PointDescription  .  class  .  getName  (  )  )  ; 


private   static   boolean   theirPointsCreated  =  false  ; 




protected   String  [  ]  itsNames  =  null  ; 


protected   String   itsSource  =  null  ; 


protected   String   itsLongDesc  =  ""  ; 


protected   String   itsShortDesc  =  ""  ; 




protected   static   final   int   theirMaxShortDescLen  =  10  ; 





protected   String   itsUnits  =  null  ; 




protected   boolean   itsEnabled  =  false  ; 


protected   Transaction  [  ]  itsInputTransactions  =  null  ; 


protected   String  [  ]  itsInputTransactionStrings  =  {  }  ; 


protected   String   itsInputTransactionString  =  ""  ; 


protected   Transaction  [  ]  itsOutputTransactions  =  null  ; 


protected   String  [  ]  itsOutputTransactionStrings  =  {  }  ; 


protected   String   itsOutputTransactionString  =  ""  ; 


protected   Translation  [  ]  itsTranslations  =  null  ; 


protected   String  [  ]  itsTranslationStrings  =  {  }  ; 


protected   String   itsTranslationString  =  ""  ; 


protected   AlarmCheck  [  ]  itsAlarmChecks  =  null  ; 


protected   String  [  ]  itsAlarmCheckStrings  =  {  }  ; 


protected   String   itsAlarmCheckString  =  ""  ; 


protected   ArchivePolicy  [  ]  itsArchive  =  null  ; 


protected   String  [  ]  itsArchiveStrings  =  {  }  ; 


protected   String   itsArchiveString  =  ""  ; 


protected   PointArchiver   itsArchiver  =  null  ; 


protected   int   itsArchiveLongevity  =  -  1  ; 


protected   EventListenerList   itsListenerList  =  new   EventListenerList  (  )  ; 




long   itsPeriod  =  0  ; 


boolean   itsCollecting  =  false  ; 


protected   transient   long   itsNextEpoch  =  0  ; 





public   long   getPeriod  (  )  { 
return   itsPeriod  ; 
} 


public   void   setPeriod  (  RelTime   newperiod  )  { 
itsPeriod  =  newperiod  .  getValue  (  )  ; 
} 


public   void   setPeriod  (  String   newperiod  )  { 
if  (  newperiod  .  equalsIgnoreCase  (  "null"  )  ||  newperiod  .  trim  (  )  .  equals  (  "-"  )  )  { 
itsPeriod  =  0  ; 
}  else  { 
try  { 
itsPeriod  =  Long  .  parseLong  (  newperiod  )  ; 
if  (  itsPeriod  <  0  )  { 
itsPeriod  =  0  ; 
} 
}  catch  (  Exception   e  )  { 
theirLogger  .  error  (  "("  +  getFullName  (  )  +  "): setPeriod: "  +  e  )  ; 
itsPeriod  =  0  ; 
} 
} 
} 




public   int   getArchiveLongevity  (  )  { 
return   itsArchiveLongevity  ; 
} 




public   void   setArchiveLongevity  (  int   length  )  { 
itsArchiveLongevity  =  length  ; 
} 




public   void   setArchiveLongevity  (  String   newperiod  )  { 
if  (  newperiod  .  equalsIgnoreCase  (  "null"  )  ||  newperiod  .  trim  (  )  .  equals  (  "-"  )  )  { 
itsArchiveLongevity  =  -  1  ; 
}  else  { 
try  { 
itsArchiveLongevity  =  Integer  .  parseInt  (  newperiod  )  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  error  (  "("  +  getFullName  (  )  +  "): setArchiveLongevity: "  +  e  )  ; 
itsPeriod  =  -  1  ; 
} 
} 
} 

public   Transaction  [  ]  getInputTransactions  (  )  { 
return   itsInputTransactions  ; 
} 

public   String   getInputTransactionString  (  )  { 
return   itsInputTransactionString  ; 
} 

public   String  [  ]  getInputTransactionsAsStrings  (  )  { 
return   itsInputTransactionStrings  ; 
} 

protected   void   setInputTransactionString  (  String  [  ]  transactions  )  { 
itsInputTransactionStrings  =  transactions  ; 
if  (  transactions  ==  null  ||  transactions  .  length  ==  0  )  { 
itsInputTransactionString  =  ""  ; 
}  else   if  (  transactions  .  length  ==  1  )  { 
itsInputTransactionString  =  transactions  [  0  ]  ; 
}  else  { 
itsInputTransactionString  =  "{"  ; 
for  (  int   i  =  0  ;  i  <  transactions  .  length  -  1  ;  i  ++  )  { 
itsInputTransactionString  +=  transactions  [  i  ]  +  ","  ; 
} 
itsInputTransactionString  +=  transactions  [  transactions  .  length  -  1  ]  +  "}"  ; 
} 
} 

public   Transaction  [  ]  getOutputTransactions  (  )  { 
return   itsOutputTransactions  ; 
} 

public   String   getOutputTransactionString  (  )  { 
return   itsOutputTransactionString  ; 
} 

public   String  [  ]  getOutputTransactionsAsStrings  (  )  { 
return   itsOutputTransactionStrings  ; 
} 

protected   void   setOutputTransactionString  (  String  [  ]  transactions  )  { 
itsOutputTransactionStrings  =  transactions  ; 
if  (  transactions  ==  null  ||  transactions  .  length  ==  0  )  { 
itsOutputTransactionString  =  null  ; 
}  else   if  (  transactions  .  length  ==  1  )  { 
itsOutputTransactionString  =  transactions  [  0  ]  ; 
}  else  { 
itsOutputTransactionString  =  "{"  ; 
for  (  int   i  =  0  ;  i  <  transactions  .  length  -  1  ;  i  ++  )  { 
itsOutputTransactionString  +=  transactions  [  i  ]  +  ","  ; 
} 
itsOutputTransactionString  +=  transactions  [  transactions  .  length  -  1  ]  +  "}"  ; 
} 
} 


protected   void   makeTransactions  (  )  { 
Transaction  [  ]  inputtrans  =  new   Transaction  [  itsInputTransactionStrings  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  itsInputTransactionStrings  .  length  ;  i  ++  )  { 
inputtrans  [  i  ]  =  (  Transaction  )  Factory  .  factory  (  this  ,  itsInputTransactionStrings  [  i  ]  ,  "atnf.atoms.mon.transaction.Transaction"  )  ; 
} 
Transaction  [  ]  outputtrans  =  new   Transaction  [  itsOutputTransactionStrings  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  itsOutputTransactionStrings  .  length  ;  i  ++  )  { 
outputtrans  [  i  ]  =  (  Transaction  )  Factory  .  factory  (  this  ,  itsOutputTransactionStrings  [  i  ]  ,  "atnf.atoms.mon.transaction.Transaction"  )  ; 
} 
itsInputTransactions  =  inputtrans  ; 
itsOutputTransactions  =  outputtrans  ; 
} 




public   Translation  [  ]  getTranslations  (  )  { 
return   itsTranslations  ; 
} 

public   String   getTranslationString  (  )  { 
return   itsTranslationString  ; 
} 

public   String  [  ]  getTranslationsAsStrings  (  )  { 
return   itsTranslationStrings  ; 
} 


protected   void   makeTranslations  (  )  { 
Translation  [  ]  translations  =  new   Translation  [  itsTranslationStrings  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  itsTranslationStrings  .  length  ;  i  ++  )  { 
translations  [  i  ]  =  (  Translation  )  Factory  .  factory  (  this  ,  itsTranslationStrings  [  i  ]  ,  "atnf.atoms.mon.translation.Translation"  )  ; 
} 
itsTranslations  =  translations  ; 
} 




protected   void   setTranslations  (  Translation  [  ]  t  )  { 
itsTranslations  =  t  ; 
} 

protected   void   setTranslationString  (  String  [  ]  translations  )  { 
itsTranslationStrings  =  translations  ; 
if  (  translations  ==  null  ||  translations  .  length  ==  0  )  { 
itsTranslationString  =  null  ; 
}  else   if  (  translations  .  length  ==  1  )  { 
itsTranslationString  =  translations  [  0  ]  ; 
}  else  { 
itsTranslationString  =  "{"  ; 
for  (  int   i  =  0  ;  i  <  translations  .  length  -  1  ;  i  ++  )  { 
itsTranslationString  +=  translations  [  i  ]  +  ","  ; 
} 
itsTranslationString  +=  translations  [  translations  .  length  -  1  ]  +  "}"  ; 
} 
} 





public   String   getSource  (  )  { 
return   itsSource  ; 
} 

public   void   setSource  (  String   source  )  { 
itsSource  =  source  ; 
} 





protected   void   setNames  (  String  [  ]  newnames  )  { 
itsNames  =  newnames  ; 
} 

public   String  [  ]  getAllNames  (  )  { 
return   itsNames  ; 
} 


public   int   getNumNames  (  )  { 
return   itsNames  .  length  ; 
} 


public   String   getName  (  int   i  )  { 
return   itsNames  [  i  ]  ; 
} 


public   String   getName  (  )  { 
return   itsNames  [  0  ]  ; 
} 


public   String   getLongName  (  )  { 
return   itsNames  [  0  ]  ; 
} 


public   String   getFullName  (  )  { 
return   itsSource  +  "."  +  itsNames  [  0  ]  ; 
} 


public   String  [  ]  getFullNames  (  )  { 
String  [  ]  res  =  new   String  [  itsNames  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  itsNames  .  length  ;  i  ++  )  { 
res  [  i  ]  =  itsSource  +  "."  +  itsNames  [  i  ]  ; 
} 
return   res  ; 
} 

public   int   getNumListeners  (  )  { 
return   itsListenerList  .  getListenerCount  (  )  ; 
} 

public   void   addPointListener  (  PointListener   listener  )  { 
itsListenerList  .  add  (  PointListener  .  class  ,  listener  )  ; 
} 

public   void   removePointListener  (  PointListener   listener  )  { 
itsListenerList  .  remove  (  PointListener  .  class  ,  listener  )  ; 
} 

public   void   actionPerformed  (  ActionEvent   e  )  { 
} 


public   AbsTime   getNextEpoch_AbsTime  (  )  { 
return   AbsTime  .  factory  (  getNextEpoch  (  )  )  ; 
} 

public   boolean   getEnabled  (  )  { 
return   itsEnabled  ; 
} 

public   void   setEnabled  (  boolean   enabled  )  { 
itsEnabled  =  enabled  ; 
} 




public   int   compareTo  (  Object   obj  )  { 
if  (  obj   instanceof   PointDescription  )  { 
if  (  (  (  PointDescription  )  obj  )  .  getNextEpoch  (  )  <  getNextEpoch  (  )  )  { 
return   1  ; 
} 
if  (  (  (  PointDescription  )  obj  )  .  getNextEpoch  (  )  >  getNextEpoch  (  )  )  { 
return  -  1  ; 
} 
return   0  ; 
}  else   if  (  obj   instanceof   AbsTime  )  { 
if  (  (  (  AbsTime  )  obj  )  .  getValue  (  )  <  getNextEpoch  (  )  )  { 
return   1  ; 
} 
if  (  (  (  AbsTime  )  obj  )  .  getValue  (  )  >  getNextEpoch  (  )  )  { 
return  -  1  ; 
} 
return   0  ; 
}  else  { 
System  .  err  .  println  (  "PointInteraction: compareTo: UNKNOWN TYPE!"  )  ; 
return  -  1  ; 
} 
} 


public   AlarmCheck  [  ]  getAlarmChecks  (  )  { 
return   itsAlarmChecks  ; 
} 

public   String   getAlarmCheckString  (  )  { 
return   itsAlarmCheckString  ; 
} 

public   String  [  ]  getAlarmChecksAsStrings  (  )  { 
return   itsAlarmCheckStrings  ; 
} 


public   void   setAlarmCheckString  (  String  [  ]  alarms  )  { 
itsAlarmCheckStrings  =  alarms  ; 
if  (  alarms  ==  null  ||  alarms  .  length  ==  0  )  { 
itsAlarmCheckString  =  "-"  ; 
}  else   if  (  alarms  .  length  ==  1  )  { 
itsAlarmCheckString  =  alarms  [  0  ]  ; 
}  else  { 
itsAlarmCheckString  =  "{"  ; 
for  (  int   i  =  0  ;  i  <  alarms  .  length  -  1  ;  i  ++  )  { 
itsAlarmCheckString  +=  alarms  [  i  ]  +  ","  ; 
} 
itsAlarmCheckString  +=  alarms  [  alarms  .  length  -  1  ]  +  "}"  ; 
} 
} 


protected   void   makeAlarmChecks  (  )  { 
try  { 
AlarmCheck  [  ]  alarms  =  new   AlarmCheck  [  itsAlarmCheckStrings  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  itsAlarmCheckStrings  .  length  ;  i  ++  )  { 
alarms  [  i  ]  =  (  AlarmCheck  )  Factory  .  factory  (  this  ,  itsAlarmCheckStrings  [  i  ]  ,  "atnf.atoms.mon.alarmcheck.AlarmCheck"  )  ; 
} 
itsAlarmChecks  =  alarms  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  error  (  "Encountered "  +  e  +  " while making AlarmCheck objects for point "  +  getFullName  (  )  )  ; 
} 
} 





public   void   evaluateAlarms  (  PointData   pd  )  { 
if  (  itsAlarmChecks  !=  null  &&  itsAlarmChecks  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  itsAlarmChecks  .  length  &&  pd  .  getAlarm  (  )  ==  false  ;  i  ++  )  { 
if  (  itsAlarmChecks  [  i  ]  !=  null  )  { 
itsAlarmChecks  [  i  ]  .  checkAlarm  (  pd  )  ; 
} 
} 
} 
} 


public   ArchivePolicy  [  ]  getArchivePolicies  (  )  { 
return   itsArchive  ; 
} 


public   void   setArchiveString  (  String  [  ]  archive  )  { 
itsArchiveStrings  =  archive  ; 
if  (  archive  ==  null  ||  archive  .  length  ==  0  )  { 
itsArchiveString  =  null  ; 
}  else   if  (  archive  .  length  ==  1  )  { 
itsArchiveString  =  archive  [  0  ]  ; 
}  else  { 
itsArchiveString  =  "{"  ; 
for  (  int   i  =  0  ;  i  <  archive  .  length  -  1  ;  i  ++  )  { 
itsArchiveString  +=  archive  [  i  ]  +  ","  ; 
} 
itsArchiveString  +=  archive  [  archive  .  length  -  1  ]  +  "}"  ; 
} 
} 


public   String   getArchivePolicyString  (  )  { 
return   itsArchiveString  ; 
} 


public   String  [  ]  getArchivePoliciesAsStrings  (  )  { 
return   itsArchiveStrings  ; 
} 


protected   void   makeArchivePolicies  (  )  { 
ArchivePolicy  [  ]  archives  =  new   ArchivePolicy  [  itsArchiveStrings  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  archives  .  length  ;  i  ++  )  { 
archives  [  i  ]  =  (  ArchivePolicy  )  Factory  .  factory  (  this  ,  itsArchiveStrings  [  i  ]  ,  "atnf.atoms.mon.archivepolicy.ArchivePolicy"  )  ; 
} 
itsArchive  =  archives  ; 
} 




public   long   getLastEpoch  (  )  { 
PointData   data  =  PointBuffer  .  getPointData  (  this  )  ; 
if  (  data  ==  null  )  { 
return  -  1  ; 
}  else  { 
return   data  .  getTimestamp  (  )  .  getValue  (  )  ; 
} 
} 





public   long   getNextEpoch  (  )  { 
return   itsNextEpoch  ; 
} 


public   void   setNextEpoch  (  long   nextEpoch  )  { 
itsNextEpoch  =  nextEpoch  ; 
} 


public   void   setNextEpoch  (  AbsTime   nextEpoch  )  { 
itsNextEpoch  =  nextEpoch  .  getValue  (  )  ; 
} 





public   void   isCollecting  (  boolean   collecting  )  { 
itsCollecting  =  collecting  ; 
} 


public   boolean   isCollecting  (  )  { 
return   itsCollecting  ; 
} 


public   void   setLongDesc  (  String   desc  )  { 
itsLongDesc  =  desc  .  replace  (  '\"'  ,  '\0'  )  ; 
} 


public   String   getLongDesc  (  )  { 
return   itsLongDesc  ; 
} 


public   void   setShortDesc  (  String   desc  )  { 
itsShortDesc  =  desc  .  replace  (  '\"'  ,  '\0'  )  ; 
if  (  itsShortDesc  .  length  (  )  >  theirMaxShortDescLen  )  { 
itsShortDesc  =  itsShortDesc  .  substring  (  theirMaxShortDescLen  )  ; 
} 
} 


public   String   getShortDesc  (  )  { 
return   itsShortDesc  ; 
} 




public   String   getUnits  (  )  { 
return   itsUnits  ; 
} 


public   void   setUnits  (  String   units  )  { 
itsUnits  =  units  ; 
} 




public   void   populateServerFields  (  )  { 
makeTransactions  (  )  ; 
makeTranslations  (  )  ; 
makeArchivePolicies  (  )  ; 
makeAlarmChecks  (  )  ; 
for  (  int   i  =  0  ;  i  <  itsInputTransactions  .  length  ;  i  ++  )  { 
Transaction   thistrans  =  itsInputTransactions  [  i  ]  ; 
if  (  thistrans  !=  null  &&  thistrans  .  getChannel  (  )  !=  null  &&  !  thistrans  .  getChannel  (  )  .  equals  (  "NONE"  )  )  { 
ExternalSystem   ds  =  ExternalSystem  .  getExternalSystem  (  thistrans  .  getChannel  (  )  )  ; 
if  (  ds  !=  null  )  { 
ds  .  addPoint  (  this  )  ; 
}  else  { 
theirLogger  .  warn  (  "("  +  getFullName  (  )  +  ") No ExternalSystem found for Channel: "  +  thistrans  .  getChannel  (  )  )  ; 
} 
} 
} 
setArchiver  (  PointArchiver  .  getPointArchiver  (  )  )  ; 
} 




public   static   ArrayList   parseFile  (  String   fname  )  { 
try  { 
return   parseFile  (  new   FileReader  (  fname  )  )  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  error  (  "While parsing point definition file: "  +  e  )  ; 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 




public   static   ArrayList  <  PointDescription  >  parseFile  (  Reader   pointsfile  )  { 
ArrayList  <  PointDescription  >  result  =  new   ArrayList  <  PointDescription  >  (  )  ; 
String  [  ]  lines  =  MonitorUtils  .  parseFile  (  pointsfile  )  ; 
if  (  lines  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  i  ++  )  { 
ArrayList  <  PointDescription  >  al  =  null  ; 
try  { 
al  =  parseLine  (  lines  [  i  ]  )  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  error  (  "Exception \""  +  e  +  "\" while parsing point definition line "  +  i  +  ": "  +  lines  [  i  ]  )  ; 
} 
if  (  al  !=  null  )  { 
result  .  addAll  (  al  )  ; 
} 
} 
} 
return   result  ; 
} 

public   static   ArrayList  <  PointDescription  >  parseLine  (  String   line  )  throws   Exception  { 
final   int   NUMTOKENS  =  13  ; 
ArrayList  <  PointDescription  >  result  =  new   ArrayList  <  PointDescription  >  (  )  ; 
String  [  ]  toks  =  MonitorUtils  .  getTokens  (  line  )  ; 
if  (  toks  .  length  !=  NUMTOKENS  )  { 
throw   new   Exception  (  "Expect "  +  NUMTOKENS  +  " tokens, found "  +  toks  .  length  )  ; 
} 
String  [  ]  pointNameArray  =  MonitorUtils  .  getTokens  (  toks  [  0  ]  )  ; 
String   pointLongDesc  =  toks  [  1  ]  ; 
String   pointShortDesc  =  toks  [  2  ]  ; 
String   pointUnits  =  toks  [  3  ]  ; 
String  [  ]  pointSourceArray  =  MonitorUtils  .  getTokens  (  toks  [  4  ]  )  ; 
String   pointEnabled  =  toks  [  5  ]  ; 
String  [  ]  pointInputArray  =  MonitorUtils  .  getTokens  (  toks  [  6  ]  )  ; 
String  [  ]  pointOutputArray  =  MonitorUtils  .  getTokens  (  toks  [  7  ]  )  ; 
String  [  ]  pointTranslateArray  =  MonitorUtils  .  getTokens  (  toks  [  8  ]  )  ; 
String  [  ]  pointLimitsArray  =  MonitorUtils  .  getTokens  (  toks  [  9  ]  )  ; 
String  [  ]  pointArchiveArray  =  MonitorUtils  .  getTokens  (  toks  [  10  ]  )  ; 
String   pointPeriod  =  toks  [  11  ]  ; 
String   archiveLife  =  toks  [  12  ]  ; 
boolean  [  ]  pointEnabledArray  =  parseBoolean  (  pointEnabled  )  ; 
if  (  pointEnabled  .  length  (  )  <  pointSourceArray  .  length  )  { 
boolean  [  ]  temp  =  new   boolean  [  pointSourceArray  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  pointEnabledArray  [  0  ]  ; 
} 
pointEnabledArray  =  temp  ; 
} 
for  (  int   i  =  0  ;  i  <  pointSourceArray  .  length  ;  i  ++  )  { 
result  .  add  (  PointDescription  .  factory  (  pointNameArray  ,  pointLongDesc  ,  pointShortDesc  ,  pointUnits  ,  pointSourceArray  [  i  ]  ,  pointInputArray  ,  pointOutputArray  ,  pointTranslateArray  ,  pointLimitsArray  ,  pointArchiveArray  ,  pointPeriod  ,  archiveLife  ,  pointEnabledArray  [  i  ]  )  )  ; 
} 
return   result  ; 
} 


public   static   boolean  [  ]  parseBoolean  (  String   token  )  { 
boolean  [  ]  res  =  new   boolean  [  token  .  length  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  res  .  length  ;  i  ++  )  { 
if  (  token  .  charAt  (  i  )  ==  't'  ||  token  .  charAt  (  i  )  ==  'T'  )  { 
res  [  i  ]  =  true  ; 
}  else  { 
res  [  i  ]  =  false  ; 
} 
} 
return   res  ; 
} 


public   static   PointDescription   factory  (  String  [  ]  names  ,  String   longdesc  ,  String   shortdesc  ,  String   units  ,  String   source  ,  String  [  ]  inputs  ,  String  [  ]  outputs  ,  String  [  ]  translate  ,  String  [  ]  limits  ,  String  [  ]  archive  ,  String   period  ,  String   archivelife  ,  boolean   enabled  )  { 
PointDescription   result  =  new   PointDescription  (  )  ; 
result  .  setNames  (  names  )  ; 
result  .  setLongDesc  (  longdesc  )  ; 
result  .  setShortDesc  (  shortdesc  )  ; 
result  .  setUnits  (  units  )  ; 
result  .  setSource  (  source  )  ; 
result  .  setInputTransactionString  (  inputs  )  ; 
result  .  setOutputTransactionString  (  outputs  )  ; 
result  .  setTranslationString  (  translate  )  ; 
result  .  setAlarmCheckString  (  limits  )  ; 
result  .  setArchiveString  (  archive  )  ; 
result  .  setArchiveLongevity  (  archivelife  )  ; 
result  .  setPeriod  (  period  )  ; 
result  .  setEnabled  (  enabled  )  ; 
addPoint  (  result  )  ; 
return   result  ; 
} 


public   synchronized   void   distributeData  (  PointEvent   pe  )  { 
Object  [  ]  listeners  =  itsListenerList  .  getListenerList  (  )  ; 
for  (  int   i  =  0  ;  i  <  listeners  .  length  ;  i  +=  2  )  { 
if  (  listeners  [  i  ]  ==  PointListener  .  class  )  { 
try  { 
(  (  PointListener  )  listeners  [  i  +  1  ]  )  .  onPointEvent  (  this  ,  pe  )  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  warn  (  getFullName  (  )  +  ": Error distributing data to listener, class "  +  listeners  [  i  +  1  ]  .  getClass  (  )  .  getCanonicalName  (  )  +  " ("  +  e  +  ")"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 


public   synchronized   void   firePointEvent  (  PointEvent   pe  )  { 
PointData   data  =  pe  .  getPointData  (  )  ; 
if  (  pe  .  isRaw  (  )  )  { 
if  (  data  !=  null  &&  itsTranslations  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  itsTranslations  .  length  ;  i  ++  )  { 
try  { 
if  (  itsTranslations  [  i  ]  !=  null  )  { 
data  =  itsTranslations  [  i  ]  .  translate  (  data  )  ; 
} 
}  catch  (  Throwable   e  )  { 
theirLogger  .  error  (  "("  +  getFullName  (  )  +  ") Error on Translation "  +  (  i  +  1  )  +  "/"  +  itsTranslations  .  length  +  ": "  +  e  )  ; 
data  =  null  ; 
} 
if  (  data  ==  null  )  { 
break  ; 
} 
} 
} 
if  (  data  !=  null  &&  !  data  .  getName  (  )  .  equals  (  getFullName  (  )  )  )  { 
data  =  new   PointData  (  data  )  ; 
data  .  setName  (  getFullName  (  )  )  ; 
} 
pe  =  new   PointEvent  (  this  ,  data  ,  false  )  ; 
} 
if  (  data  !=  null  &&  data  .  isValid  (  )  )  { 
evaluateAlarms  (  data  )  ; 
if  (  getEnabled  (  )  &&  itsOutputTransactions  !=  null  &&  itsOutputTransactions  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  itsOutputTransactions  .  length  ;  i  ++  )  { 
Transaction   thistrans  =  itsOutputTransactions  [  i  ]  ; 
if  (  thistrans  !=  null  )  { 
ExternalSystem   ds  =  ExternalSystem  .  getExternalSystem  (  thistrans  .  getChannel  (  )  )  ; 
if  (  ds  ==  null  )  { 
theirLogger  .  warn  (  "("  +  getFullName  (  )  +  ") No ExternalSystem for output Transaction channel "  +  thistrans  .  getChannel  (  )  )  ; 
}  else   if  (  !  ds  .  isConnected  (  )  )  { 
theirLogger  .  warn  (  "("  +  getFullName  (  )  +  ") While writing output data: ExternalSystem "  +  thistrans  .  getChannel  (  )  +  " is not connected"  )  ; 
}  else  { 
try  { 
ds  .  putData  (  this  ,  data  )  ; 
}  catch  (  Exception   e  )  { 
theirLogger  .  warn  (  "("  +  getFullName  (  )  +  ") while writing output data, ExternalSystem "  +  ds  .  getName  (  )  +  " threw exception \""  +  e  +  "\""  )  ; 
} 
} 
} 
} 
} 
if  (  itsArchiver  !=  null  &&  itsEnabled  )  { 
for  (  int   i  =  0  ;  i  <  itsArchive  .  length  ;  i  ++  )  { 
if  (  itsArchive  [  i  ]  !=  null  &&  itsArchive  [  i  ]  .  checkArchiveThis  (  data  )  )  { 
itsArchiver  .  archiveData  (  this  ,  data  )  ; 
break  ; 
} 
} 
} 
} 
if  (  data  !=  null  )  { 
PointBuffer  .  updateData  (  this  ,  data  )  ; 
} 
distributeData  (  pe  )  ; 
if  (  itsPeriod  >  0  )  { 
if  (  data  !=  null  &&  data  .  isValid  (  )  )  { 
itsNextEpoch  =  data  .  getTimestamp  (  )  .  getValue  (  )  +  itsPeriod  ; 
}  else  { 
itsNextEpoch  =  (  new   AbsTime  (  )  )  .  getValue  (  )  +  itsPeriod  ; 
} 
} 
} 







public   void   setArchiver  (  PointArchiver   archiver  )  { 
itsArchiver  =  archiver  ; 
} 




public   PointArchiver   getArchiver  (  )  { 
return   itsArchiver  ; 
} 




public   String   getStringEquiv  (  )  { 
StringBuffer   res  =  new   StringBuffer  (  )  ; 
if  (  itsNames  .  length  >  1  )  { 
res  .  append  (  '{'  )  ; 
for  (  int   i  =  0  ;  i  <  itsNames  .  length  -  1  ;  i  ++  )  { 
res  .  append  (  itsNames  [  i  ]  +  ","  )  ; 
} 
res  .  append  (  itsNames  [  itsNames  .  length  -  1  ]  +  "}"  )  ; 
}  else  { 
res  .  append  (  itsNames  [  0  ]  )  ; 
} 
res  .  append  (  ' '  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  itsLongDesc  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  itsShortDesc  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  itsUnits  )  ; 
res  .  append  (  '"'  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsSource  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsEnabled  ?  'T'  :  'F'  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsInputTransactionString  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsOutputTransactionString  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsTranslationString  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsAlarmCheckString  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsArchiveString  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsPeriod  )  ; 
res  .  append  (  ' '  )  ; 
res  .  append  (  itsArchiveLongevity  )  ; 
return   res  .  toString  (  )  ; 
} 


public   String   toString  (  )  { 
return  "{"  +  itsSource  +  "."  +  itsNames  [  0  ]  +  " "  +  getNextEpoch_AbsTime  (  )  .  toString  (  AbsTime  .  Format  .  UTC_STRING  )  +  "}"  ; 
} 


private   static   TreeMap  <  String  ,  PointDescription  >  theirPoints  =  new   TreeMap  <  String  ,  PointDescription  >  (  )  ; 


private   static   TreeMap  <  String  ,  PointDescription  >  theirUniquePoints  =  new   TreeMap  <  String  ,  PointDescription  >  (  )  ; 


public   static   synchronized   void   addPoint  (  PointDescription   pm  )  { 
String  [  ]  names  =  pm  .  getFullNames  (  )  ; 
if  (  !  theirUniquePoints  .  containsKey  (  names  [  0  ]  )  )  { 
theirUniquePoints  .  put  (  names  [  0  ]  ,  pm  )  ; 
} 
for  (  int   i  =  0  ;  i  <  names  .  length  ;  i  ++  )  { 
if  (  !  theirPoints  .  containsKey  (  names  [  i  ]  )  )  { 
theirPoints  .  put  (  names  [  i  ]  ,  pm  )  ; 
} 
} 
} 


public   static   synchronized   String  [  ]  getAllPointNames  (  )  { 
return   MonitorUtils  .  toStringArray  (  theirPoints  .  keySet  (  )  .  toArray  (  )  )  ; 
} 


public   static   synchronized   String  [  ]  getAllUniqueNames  (  )  { 
return   MonitorUtils  .  toStringArray  (  theirUniquePoints  .  keySet  (  )  .  toArray  (  )  )  ; 
} 


public   static   synchronized   PointDescription   getPoint  (  String   name  )  { 
return   theirPoints  .  get  (  name  )  ; 
} 


public   static   synchronized   Collection  <  PointDescription  >  getAllPoints  (  )  { 
return   theirPoints  .  values  (  )  ; 
} 


public   static   synchronized   PointDescription  [  ]  getAllUniquePoints  (  )  { 
Object  [  ]  pointobjarr  =  theirUniquePoints  .  values  (  )  .  toArray  (  )  ; 
return   Arrays  .  copyOf  (  pointobjarr  ,  pointobjarr  .  length  ,  PointDescription  [  ]  .  class  )  ; 
} 


public   static   boolean   checkPointName  (  String   name  )  { 
if  (  theirPoints  .  containsKey  (  name  )  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 


public   static   void   setPointsCreated  (  )  { 
theirPointsCreated  =  true  ; 
} 


public   static   boolean   getPointsCreated  (  )  { 
return   theirPointsCreated  ; 
} 
} 

