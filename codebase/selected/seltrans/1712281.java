package   org  .  quartz  .  xml  ; 

import   java  .  beans  .  PropertyDescriptor  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  text  .  DateFormat  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  TimeZone  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   org  .  apache  .  commons  .  beanutils  .  ConversionException  ; 
import   org  .  apache  .  commons  .  beanutils  .  Converter  ; 
import   org  .  apache  .  commons  .  beanutils  .  DynaBean  ; 
import   org  .  apache  .  commons  .  beanutils  .  DynaProperty  ; 
import   org  .  apache  .  commons  .  beanutils  .  PropertyUtils  ; 
import   org  .  apache  .  commons  .  digester  .  BeanPropertySetterRule  ; 
import   org  .  apache  .  commons  .  digester  .  Digester  ; 
import   org  .  apache  .  commons  .  digester  .  RuleSetBase  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   org  .  quartz  .  CronTrigger  ; 
import   org  .  quartz  .  JobDataMap  ; 
import   org  .  quartz  .  JobDetail  ; 
import   org  .  quartz  .  JobListener  ; 
import   org  .  quartz  .  ObjectAlreadyExistsException  ; 
import   org  .  quartz  .  Scheduler  ; 
import   org  .  quartz  .  SchedulerException  ; 
import   org  .  quartz  .  SimpleTrigger  ; 
import   org  .  quartz  .  Trigger  ; 
import   org  .  quartz  .  spi  .  ClassLoadHelper  ; 
import   org  .  xml  .  sax  .  InputSource  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   org  .  xml  .  sax  .  SAXParseException  ; 
import   org  .  xml  .  sax  .  helpers  .  DefaultHandler  ; 






















public   class   JobSchedulingDataProcessor   extends   DefaultHandler  { 

public   static   final   String   QUARTZ_PUBLIC_ID  =  "-//Quartz Enterprise Job Scheduler//DTD Job Scheduling Data 1.5//EN"  ; 

public   static   final   String   QUARTZ_SYSTEM_ID  =  "http://www.opensymphony.com/quartz/xml/job_scheduling_data_1_5.dtd"  ; 

public   static   final   String   QUARTZ_DTD  =  "/org/quartz/xml/job_scheduling_data_1_5.dtd"  ; 

public   static   final   String   QUARTZ_NS  =  "http://www.opensymphony.com/quartz/JobSchedulingData"  ; 

public   static   final   String   QUARTZ_SCHEMA  =  "http://www.opensymphony.com/quartz/xml/job_scheduling_data_1_5.xsd"  ; 

public   static   final   String   QUARTZ_XSD  =  "/org/quartz/xml/job_scheduling_data_1_5.xsd"  ; 

public   static   final   String   QUARTZ_SYSTEM_ID_DIR_PROP  =  "quartz.system.id.dir"  ; 

public   static   final   String   QUARTZ_XML_FILE_NAME  =  "quartz_jobs.xml"  ; 

public   static   final   String   QUARTZ_SYSTEM_ID_PREFIX  =  "jar:"  ; 

protected   static   final   String   TAG_QUARTZ  =  "quartz"  ; 

protected   static   final   String   TAG_OVERWRITE_EXISTING_JOBS  =  "overwrite-existing-jobs"  ; 

protected   static   final   String   TAG_JOB_LISTENER  =  "job-listener"  ; 

protected   static   final   String   TAG_CALENDAR  =  "calendar"  ; 

protected   static   final   String   TAG_CLASS_NAME  =  "class-name"  ; 

protected   static   final   String   TAG_DESCRIPTION  =  "description"  ; 

protected   static   final   String   TAG_BASE_CALENDAR  =  "base-calendar"  ; 

protected   static   final   String   TAG_MISFIRE_INSTRUCTION  =  "misfire-instruction"  ; 

protected   static   final   String   TAG_CALENDAR_NAME  =  "calendar-name"  ; 

protected   static   final   String   TAG_JOB  =  "job"  ; 

protected   static   final   String   TAG_JOB_DETAIL  =  "job-detail"  ; 

protected   static   final   String   TAG_NAME  =  "name"  ; 

protected   static   final   String   TAG_GROUP  =  "group"  ; 

protected   static   final   String   TAG_JOB_CLASS  =  "job-class"  ; 

protected   static   final   String   TAG_JOB_LISTENER_REF  =  "job-listener-ref"  ; 

protected   static   final   String   TAG_VOLATILITY  =  "volatility"  ; 

protected   static   final   String   TAG_DURABILITY  =  "durability"  ; 

protected   static   final   String   TAG_RECOVER  =  "recover"  ; 

protected   static   final   String   TAG_JOB_DATA_MAP  =  "job-data-map"  ; 

protected   static   final   String   TAG_ENTRY  =  "entry"  ; 

protected   static   final   String   TAG_KEY  =  "key"  ; 

protected   static   final   String   TAG_ALLOWS_TRANSIENT_DATA  =  "allows-transient-data"  ; 

protected   static   final   String   TAG_VALUE  =  "value"  ; 

protected   static   final   String   TAG_TRIGGER  =  "trigger"  ; 

protected   static   final   String   TAG_SIMPLE  =  "simple"  ; 

protected   static   final   String   TAG_CRON  =  "cron"  ; 

protected   static   final   String   TAG_JOB_NAME  =  "job-name"  ; 

protected   static   final   String   TAG_JOB_GROUP  =  "job-group"  ; 

protected   static   final   String   TAG_START_TIME  =  "start-time"  ; 

protected   static   final   String   TAG_END_TIME  =  "end-time"  ; 

protected   static   final   String   TAG_REPEAT_COUNT  =  "repeat-count"  ; 

protected   static   final   String   TAG_REPEAT_INTERVAL  =  "repeat-interval"  ; 

protected   static   final   String   TAG_CRON_EXPRESSION  =  "cron-expression"  ; 

protected   static   final   String   TAG_TIME_ZONE  =  "time-zone"  ; 







protected   static   final   String   XSD_DATE_FORMAT  =  "yyyy-MM-dd'T'hh:mm:ss"  ; 




protected   static   final   String   DTD_DATE_FORMAT  =  "yyyy-MM-dd hh:mm:ss a"  ; 

protected   Map   scheduledJobs  =  new   HashMap  (  )  ; 

protected   List   jobsToSchedule  =  new   LinkedList  (  )  ; 

protected   List   calsToSchedule  =  new   LinkedList  (  )  ; 

protected   List   listenersToSchedule  =  new   LinkedList  (  )  ; 

protected   Collection   validationExceptions  =  new   ArrayList  (  )  ; 

protected   ClassLoadHelper   classLoadHelper  ; 

protected   Digester   digester  ; 

private   boolean   overWriteExistingJobs  =  true  ; 

private   ThreadLocal   schedLocal  =  new   ThreadLocal  (  )  ; 

private   final   Log   log  =  LogFactory  .  getLog  (  getClass  (  )  )  ; 




private   JobSchedulingDataProcessor  (  )  { 
} 








public   JobSchedulingDataProcessor  (  ClassLoadHelper   clh  ,  boolean   validating  ,  boolean   validatingSchema  )  { 
this  .  classLoadHelper  =  clh  ; 
initDigester  (  validating  ,  validatingSchema  )  ; 
} 







protected   void   initDigester  (  boolean   validating  ,  boolean   validatingSchema  )  { 
digester  =  new   Digester  (  )  ; 
digester  .  setNamespaceAware  (  true  )  ; 
digester  .  setClassLoader  (  this  .  classLoadHelper  .  getClassLoader  (  )  )  ; 
digester  .  setValidating  (  validating  )  ; 
initSchemaValidation  (  validatingSchema  )  ; 
digester  .  setEntityResolver  (  this  )  ; 
digester  .  setErrorHandler  (  this  )  ; 
if  (  addCustomDigesterRules  (  digester  )  )  { 
addDefaultDigesterRules  (  digester  )  ; 
} 
} 




protected   void   addDefaultDigesterRules  (  Digester   digester  )  { 
digester  .  addSetProperties  (  TAG_QUARTZ  ,  TAG_OVERWRITE_EXISTING_JOBS  ,  "overWriteExistingJobs"  )  ; 
digester  .  addObjectCreate  (  TAG_QUARTZ  +  "/"  +  TAG_JOB_LISTENER  ,  "jobListener"  ,  "class-name"  )  ; 
digester  .  addCallMethod  (  TAG_QUARTZ  +  "/"  +  TAG_JOB_LISTENER  ,  "setName"  ,  1  )  ; 
digester  .  addCallParam  (  TAG_QUARTZ  +  "/"  +  TAG_JOB_LISTENER  ,  0  ,  "name"  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB_LISTENER  ,  "addListenerToSchedule"  )  ; 
digester  .  addRuleSet  (  new   CalendarRuleSet  (  TAG_QUARTZ  +  "/"  +  TAG_CALENDAR  ,  "addCalendarToSchedule"  )  )  ; 
digester  .  addRuleSet  (  new   CalendarRuleSet  (  "*/"  +  TAG_BASE_CALENDAR  ,  "setBaseCalendar"  )  )  ; 
digester  .  addObjectCreate  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  ,  JobSchedulingBundle  .  class  )  ; 
digester  .  addObjectCreate  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  ,  JobDetail  .  class  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_NAME  ,  "name"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_GROUP  ,  "group"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_DESCRIPTION  ,  "description"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_CLASS  ,  "jobClass"  )  ; 
digester  .  addCallMethod  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_LISTENER_REF  ,  "addJobListener"  ,  0  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_VOLATILITY  ,  "volatility"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_DURABILITY  ,  "durability"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_RECOVER  ,  "requestsRecovery"  )  ; 
digester  .  addObjectCreate  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_DATA_MAP  ,  JobDataMap  .  class  )  ; 
digester  .  addCallMethod  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  ,  "put"  ,  2  ,  new   Class  [  ]  {  Object  .  class  ,  Object  .  class  }  )  ; 
digester  .  addCallParam  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  +  "/"  +  TAG_KEY  ,  0  )  ; 
digester  .  addCallParam  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  +  "/"  +  TAG_VALUE  ,  1  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  +  "/"  +  TAG_JOB_DATA_MAP  ,  "setJobDataMap"  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_JOB_DETAIL  ,  "setJobDetail"  )  ; 
digester  .  addRuleSet  (  new   TriggerRuleSet  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_SIMPLE  ,  SimpleTrigger  .  class  )  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_SIMPLE  +  "/"  +  TAG_REPEAT_COUNT  ,  "repeatCount"  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_SIMPLE  +  "/"  +  TAG_REPEAT_INTERVAL  ,  "repeatInterval"  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_SIMPLE  ,  "addTrigger"  )  ; 
digester  .  addRuleSet  (  new   TriggerRuleSet  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_CRON  ,  CronTrigger  .  class  )  )  ; 
digester  .  addBeanPropertySetter  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_CRON  +  "/"  +  TAG_CRON_EXPRESSION  ,  "cronExpression"  )  ; 
digester  .  addRule  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_CRON  +  "/"  +  TAG_TIME_ZONE  ,  new   SimpleConverterRule  (  "timeZone"  ,  new   TimeZoneConverter  (  )  ,  TimeZone  .  class  )  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  +  "/"  +  TAG_TRIGGER  +  "/"  +  TAG_CRON  ,  "addTrigger"  )  ; 
digester  .  addSetNext  (  TAG_QUARTZ  +  "/"  +  TAG_JOB  ,  "addJobToSchedule"  )  ; 
} 











protected   boolean   addCustomDigesterRules  (  Digester   digester  )  { 
return   true  ; 
} 






protected   void   initSchemaValidation  (  boolean   validatingSchema  )  { 
if  (  validatingSchema  )  { 
String   schemaUri  =  null  ; 
URL   url  =  getClass  (  )  .  getResource  (  QUARTZ_XSD  )  ; 
if  (  url  !=  null  )  { 
schemaUri  =  url  .  toExternalForm  (  )  ; 
}  else  { 
schemaUri  =  QUARTZ_SCHEMA  ; 
} 
digester  .  setSchema  (  schemaUri  )  ; 
} 
} 

protected   Log   getLog  (  )  { 
return   log  ; 
} 






public   boolean   getUseContextClassLoader  (  )  { 
return   digester  .  getUseContextClassLoader  (  )  ; 
} 






public   void   setUseContextClassLoader  (  boolean   useContextClassLoader  )  { 
digester  .  setUseContextClassLoader  (  useContextClassLoader  )  ; 
} 






public   boolean   getOverWriteExistingJobs  (  )  { 
return   overWriteExistingJobs  ; 
} 






public   void   setOverWriteExistingJobs  (  boolean   overWriteExistingJobs  )  { 
this  .  overWriteExistingJobs  =  overWriteExistingJobs  ; 
} 






public   void   processFile  (  )  throws   Exception  { 
processFile  (  QUARTZ_XML_FILE_NAME  )  ; 
} 







public   void   processFile  (  String   fileName  )  throws   Exception  { 
processFile  (  fileName  ,  getSystemIdForFileName  (  fileName  )  )  ; 
} 











protected   String   getSystemIdForFileName  (  String   fileName  )  { 
InputStream   fileInputStream  =  null  ; 
try  { 
String   urlPath  =  null  ; 
File   file  =  new   File  (  fileName  )  ; 
if  (  !  file  .  exists  (  )  )  { 
URL   url  =  getURL  (  fileName  )  ; 
if  (  url  !=  null  )  { 
urlPath  =  URLDecoder  .  decode  (  url  .  getPath  (  )  )  ; 
try  { 
fileInputStream  =  url  .  openStream  (  )  ; 
}  catch  (  IOException   ignore  )  { 
} 
} 
}  else  { 
try  { 
fileInputStream  =  new   FileInputStream  (  file  )  ; 
}  catch  (  FileNotFoundException   ignore  )  { 
} 
} 
if  (  fileInputStream  ==  null  )  { 
getLog  (  )  .  debug  (  "Unable to resolve '"  +  fileName  +  "' to full path, so using it as is for system id."  )  ; 
return   fileName  ; 
}  else  { 
return  (  urlPath  !=  null  )  ?  urlPath  :  file  .  getAbsolutePath  (  )  ; 
} 
}  finally  { 
try  { 
if  (  fileInputStream  !=  null  )  { 
fileInputStream  .  close  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
getLog  (  )  .  warn  (  "Error closing jobs file: "  +  fileName  ,  ioe  )  ; 
} 
} 
} 








protected   URL   getURL  (  String   fileName  )  { 
return   Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  .  getResource  (  fileName  )  ; 
} 










public   void   processFile  (  String   fileName  ,  String   systemId  )  throws   ValidationException  ,  ParserConfigurationException  ,  SAXException  ,  IOException  ,  SchedulerException  ,  ClassNotFoundException  ,  ParseException  { 
clearValidationExceptions  (  )  ; 
scheduledJobs  .  clear  (  )  ; 
jobsToSchedule  .  clear  (  )  ; 
calsToSchedule  .  clear  (  )  ; 
getLog  (  )  .  info  (  "Parsing XML file: "  +  fileName  +  " with systemId: "  +  systemId  +  " validating: "  +  digester  .  getValidating  (  )  +  " validating schema: "  +  digester  .  getSchema  (  )  )  ; 
InputSource   is  =  new   InputSource  (  getInputStream  (  fileName  )  )  ; 
is  .  setSystemId  (  systemId  )  ; 
digester  .  push  (  this  )  ; 
digester  .  parse  (  is  )  ; 
maybeThrowValidationException  (  )  ; 
} 










public   void   processStream  (  InputStream   stream  ,  String   systemId  )  throws   ValidationException  ,  ParserConfigurationException  ,  SAXException  ,  IOException  ,  SchedulerException  ,  ClassNotFoundException  ,  ParseException  { 
clearValidationExceptions  (  )  ; 
scheduledJobs  .  clear  (  )  ; 
jobsToSchedule  .  clear  (  )  ; 
calsToSchedule  .  clear  (  )  ; 
getLog  (  )  .  info  (  "Parsing XML from stream with systemId: "  +  systemId  +  " validating: "  +  digester  .  getValidating  (  )  +  " validating schema: "  +  digester  .  getSchema  (  )  )  ; 
InputSource   is  =  new   InputSource  (  stream  )  ; 
is  .  setSystemId  (  systemId  )  ; 
digester  .  push  (  this  )  ; 
digester  .  parse  (  is  )  ; 
maybeThrowValidationException  (  )  ; 
} 






public   void   processFileAndScheduleJobs  (  Scheduler   sched  ,  boolean   overWriteExistingJobs  )  throws   SchedulerException  ,  Exception  { 
processFileAndScheduleJobs  (  QUARTZ_XML_FILE_NAME  ,  sched  ,  overWriteExistingJobs  )  ; 
} 








public   void   processFileAndScheduleJobs  (  String   fileName  ,  Scheduler   sched  ,  boolean   overWriteExistingJobs  )  throws   Exception  { 
processFileAndScheduleJobs  (  fileName  ,  getSystemIdForFileName  (  fileName  )  ,  sched  ,  overWriteExistingJobs  )  ; 
} 








public   void   processFileAndScheduleJobs  (  String   fileName  ,  String   systemId  ,  Scheduler   sched  ,  boolean   overWriteExistingJobs  )  throws   Exception  { 
schedLocal  .  set  (  sched  )  ; 
try  { 
processFile  (  fileName  ,  systemId  )  ; 
scheduleJobs  (  getScheduledJobs  (  )  ,  sched  ,  overWriteExistingJobs  )  ; 
}  finally  { 
schedLocal  .  set  (  null  )  ; 
} 
} 










public   void   scheduleJobs  (  Map   jobBundles  ,  Scheduler   sched  ,  boolean   overWriteExistingJobs  )  throws   Exception  { 
getLog  (  )  .  info  (  "Scheduling "  +  jobsToSchedule  .  size  (  )  +  " parsed jobs."  )  ; 
Iterator   itr  =  calsToSchedule  .  iterator  (  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
CalendarBundle   bndle  =  (  CalendarBundle  )  itr  .  next  (  )  ; 
addCalendar  (  sched  ,  bndle  )  ; 
} 
itr  =  jobsToSchedule  .  iterator  (  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
JobSchedulingBundle   bndle  =  (  JobSchedulingBundle  )  itr  .  next  (  )  ; 
scheduleJob  (  bndle  ,  sched  ,  overWriteExistingJobs  )  ; 
} 
itr  =  listenersToSchedule  .  iterator  (  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
JobListener   listener  =  (  JobListener  )  itr  .  next  (  )  ; 
getLog  (  )  .  info  (  "adding listener "  +  listener  .  getName  (  )  +  " of class "  +  listener  .  getClass  (  )  .  getName  (  )  )  ; 
sched  .  addJobListener  (  listener  )  ; 
} 
getLog  (  )  .  info  (  jobBundles  .  size  (  )  +  " scheduled jobs."  )  ; 
} 









public   Map   getScheduledJobs  (  )  { 
return   Collections  .  unmodifiableMap  (  scheduledJobs  )  ; 
} 








public   JobSchedulingBundle   getScheduledJob  (  String   name  )  { 
return  (  JobSchedulingBundle  )  getScheduledJobs  (  )  .  get  (  name  )  ; 
} 








protected   InputStream   getInputStream  (  String   fileName  )  { 
return   this  .  classLoadHelper  .  getResourceAsStream  (  fileName  )  ; 
} 










public   void   scheduleJob  (  JobSchedulingBundle   job  )  throws   SchedulerException  { 
scheduleJob  (  job  ,  (  Scheduler  )  schedLocal  .  get  (  )  ,  getOverWriteExistingJobs  (  )  )  ; 
} 

public   void   addJobToSchedule  (  JobSchedulingBundle   job  )  { 
jobsToSchedule  .  add  (  job  )  ; 
} 

public   void   addCalendarToSchedule  (  CalendarBundle   cal  )  { 
calsToSchedule  .  add  (  cal  )  ; 
} 

public   void   addListenerToSchedule  (  JobListener   listener  )  { 
listenersToSchedule  .  add  (  listener  )  ; 
} 














public   void   scheduleJob  (  JobSchedulingBundle   job  ,  Scheduler   sched  ,  boolean   localOverWriteExistingJobs  )  throws   SchedulerException  { 
if  (  (  job  !=  null  )  &&  job  .  isValid  (  )  )  { 
JobDetail   detail  =  job  .  getJobDetail  (  )  ; 
JobDetail   dupeJ  =  sched  .  getJobDetail  (  detail  .  getName  (  )  ,  detail  .  getGroup  (  )  )  ; 
if  (  (  dupeJ  !=  null  )  &&  !  localOverWriteExistingJobs  )  { 
getLog  (  )  .  info  (  "Not overwriting existing job: "  +  dupeJ  .  getFullName  (  )  )  ; 
return  ; 
} 
if  (  dupeJ  !=  null  )  { 
getLog  (  )  .  info  (  "Replacing job: "  +  detail  .  getFullName  (  )  )  ; 
}  else  { 
getLog  (  )  .  info  (  "Adding job: "  +  detail  .  getFullName  (  )  )  ; 
} 
if  (  job  .  getTriggers  (  )  .  size  (  )  ==  0  &&  !  job  .  getJobDetail  (  )  .  isDurable  (  )  )  { 
if  (  dupeJ  ==  null  )  { 
throw   new   SchedulerException  (  "A new job defined without any triggers must be durable: "  +  detail  .  getFullName  (  )  )  ; 
} 
if  (  (  dupeJ  .  isDurable  (  )  &&  (  sched  .  getTriggersOfJob  (  detail  .  getName  (  )  ,  detail  .  getGroup  (  )  )  .  length  ==  0  )  )  )  { 
throw   new   SchedulerException  (  "Can't make a durable job without triggers non-durable: "  +  detail  .  getFullName  (  )  )  ; 
} 
} 
sched  .  addJob  (  detail  ,  true  )  ; 
for  (  Iterator   iter  =  job  .  getTriggers  (  )  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Trigger   trigger  =  (  Trigger  )  iter  .  next  (  )  ; 
trigger  .  setJobName  (  detail  .  getName  (  )  )  ; 
trigger  .  setJobGroup  (  detail  .  getGroup  (  )  )  ; 
if  (  trigger  .  getStartTime  (  )  ==  null  )  { 
trigger  .  setStartTime  (  new   Date  (  )  )  ; 
} 
boolean   addedTrigger  =  false  ; 
while  (  addedTrigger  ==  false  )  { 
Trigger   dupeT  =  sched  .  getTrigger  (  trigger  .  getName  (  )  ,  trigger  .  getGroup  (  )  )  ; 
if  (  dupeT  !=  null  )  { 
if  (  getLog  (  )  .  isDebugEnabled  (  )  )  { 
getLog  (  )  .  debug  (  "Rescheduling job: "  +  detail  .  getFullName  (  )  +  " with updated trigger: "  +  trigger  .  getFullName  (  )  )  ; 
} 
if  (  !  dupeT  .  getJobGroup  (  )  .  equals  (  trigger  .  getJobGroup  (  )  )  ||  !  dupeT  .  getJobName  (  )  .  equals  (  trigger  .  getJobName  (  )  )  )  { 
getLog  (  )  .  warn  (  "Possibly duplicately named triggers in jobs xml file!"  )  ; 
} 
sched  .  rescheduleJob  (  trigger  .  getName  (  )  ,  trigger  .  getGroup  (  )  ,  trigger  )  ; 
}  else  { 
if  (  getLog  (  )  .  isDebugEnabled  (  )  )  { 
getLog  (  )  .  debug  (  "Scheduling job: "  +  detail  .  getFullName  (  )  +  " with trigger: "  +  trigger  .  getFullName  (  )  )  ; 
} 
try  { 
sched  .  scheduleJob  (  trigger  )  ; 
}  catch  (  ObjectAlreadyExistsException   e  )  { 
if  (  getLog  (  )  .  isDebugEnabled  (  )  )  { 
getLog  (  )  .  debug  (  "Adding trigger: "  +  trigger  .  getFullName  (  )  +  " for job: "  +  detail  .  getFullName  (  )  +  " failed because the trigger already existed.  "  +  "This is likely due to a race condition between multiple instances "  +  "in the cluster.  Will try to reschedule instead."  )  ; 
} 
continue  ; 
} 
} 
addedTrigger  =  true  ; 
} 
} 
addScheduledJob  (  job  )  ; 
} 
} 







protected   void   addScheduledJob  (  JobSchedulingBundle   job  )  { 
scheduledJobs  .  put  (  job  .  getFullName  (  )  ,  job  )  ; 
} 








public   void   addCalendar  (  Scheduler   sched  ,  CalendarBundle   calendarBundle  )  throws   SchedulerException  { 
sched  .  addCalendar  (  calendarBundle  .  getCalendarName  (  )  ,  calendarBundle  .  getCalendar  (  )  ,  calendarBundle  .  getReplace  (  )  ,  true  )  ; 
} 








































public   InputSource   resolveEntity  (  String   publicId  ,  String   systemId  )  { 
InputSource   inputSource  =  null  ; 
InputStream   is  =  null  ; 
URL   url  =  null  ; 
try  { 
if  (  publicId  ==  null  )  { 
if  (  systemId  !=  null  )  { 
if  (  QUARTZ_SCHEMA  .  equals  (  systemId  )  )  { 
is  =  getClass  (  )  .  getResourceAsStream  (  QUARTZ_XSD  )  ; 
}  else  { 
is  =  getInputStream  (  systemId  )  ; 
if  (  is  ==  null  )  { 
int   start  =  systemId  .  indexOf  (  QUARTZ_SYSTEM_ID_PREFIX  )  ; 
if  (  start  >  -  1  )  { 
String   fileName  =  systemId  .  substring  (  QUARTZ_SYSTEM_ID_PREFIX  .  length  (  )  )  ; 
is  =  getInputStream  (  fileName  )  ; 
}  else  { 
if  (  systemId  .  indexOf  (  ':'  )  ==  -  1  )  { 
File   file  =  new   java  .  io  .  File  (  systemId  )  ; 
url  =  file  .  toURL  (  )  ; 
}  else  { 
url  =  new   URL  (  systemId  )  ; 
} 
is  =  url  .  openStream  (  )  ; 
} 
} 
} 
} 
}  else  { 
if  (  QUARTZ_PUBLIC_ID  .  equals  (  publicId  )  )  { 
is  =  getClass  (  )  .  getResourceAsStream  (  QUARTZ_DTD  )  ; 
}  else  { 
url  =  new   URL  (  systemId  )  ; 
is  =  url  .  openStream  (  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  is  !=  null  )  { 
inputSource  =  new   InputSource  (  is  )  ; 
inputSource  .  setPublicId  (  publicId  )  ; 
inputSource  .  setSystemId  (  systemId  )  ; 
} 
} 
return   inputSource  ; 
} 











public   void   warning  (  SAXParseException   e  )  throws   SAXException  { 
addValidationException  (  e  )  ; 
} 











public   void   error  (  SAXParseException   e  )  throws   SAXException  { 
addValidationException  (  e  )  ; 
} 











public   void   fatalError  (  SAXParseException   e  )  throws   SAXException  { 
addValidationException  (  e  )  ; 
} 







protected   void   addValidationException  (  SAXException   e  )  { 
validationExceptions  .  add  (  e  )  ; 
} 




protected   void   clearValidationExceptions  (  )  { 
validationExceptions  .  clear  (  )  ; 
} 








protected   void   maybeThrowValidationException  (  )  throws   ValidationException  { 
if  (  validationExceptions  .  size  (  )  >  0  )  { 
throw   new   ValidationException  (  validationExceptions  )  ; 
} 
} 






public   class   CalendarRuleSet   extends   RuleSetBase  { 

protected   String   prefix  ; 

protected   String   setNextMethodName  ; 

public   CalendarRuleSet  (  String   prefix  ,  String   setNextMethodName  )  { 
super  (  )  ; 
this  .  prefix  =  prefix  ; 
this  .  setNextMethodName  =  setNextMethodName  ; 
} 

public   void   addRuleInstances  (  Digester   digester  )  { 
digester  .  addObjectCreate  (  prefix  ,  CalendarBundle  .  class  )  ; 
digester  .  addSetProperties  (  prefix  ,  TAG_CLASS_NAME  ,  "className"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_NAME  ,  "calendarName"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_DESCRIPTION  ,  "description"  )  ; 
digester  .  addSetNext  (  prefix  ,  setNextMethodName  )  ; 
} 
} 






public   class   TriggerRuleSet   extends   RuleSetBase  { 

protected   String   prefix  ; 

protected   Class   clazz  ; 

public   TriggerRuleSet  (  String   prefix  ,  Class   clazz  )  { 
super  (  )  ; 
this  .  prefix  =  prefix  ; 
if  (  !  Trigger  .  class  .  isAssignableFrom  (  clazz  )  )  { 
throw   new   IllegalArgumentException  (  "Class must be an instance of Trigger"  )  ; 
} 
this  .  clazz  =  clazz  ; 
} 

public   void   addRuleInstances  (  Digester   digester  )  { 
digester  .  addObjectCreate  (  prefix  ,  clazz  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_NAME  ,  "name"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_GROUP  ,  "group"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_DESCRIPTION  ,  "description"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_VOLATILITY  ,  "volatility"  )  ; 
digester  .  addRule  (  prefix  +  "/"  +  TAG_MISFIRE_INSTRUCTION  ,  new   MisfireInstructionRule  (  "misfireInstruction"  )  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_CALENDAR_NAME  ,  "calendarName"  )  ; 
digester  .  addObjectCreate  (  prefix  +  "/"  +  TAG_JOB_DATA_MAP  ,  JobDataMap  .  class  )  ; 
digester  .  addCallMethod  (  prefix  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  ,  "put"  ,  2  ,  new   Class  [  ]  {  Object  .  class  ,  Object  .  class  }  )  ; 
digester  .  addCallParam  (  prefix  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  +  "/"  +  TAG_KEY  ,  0  )  ; 
digester  .  addCallParam  (  prefix  +  "/"  +  TAG_JOB_DATA_MAP  +  "/"  +  TAG_ENTRY  +  "/"  +  TAG_VALUE  ,  1  )  ; 
digester  .  addSetNext  (  prefix  +  "/"  +  TAG_JOB_DATA_MAP  ,  "setJobDataMap"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_JOB_NAME  ,  "jobName"  )  ; 
digester  .  addBeanPropertySetter  (  prefix  +  "/"  +  TAG_JOB_GROUP  ,  "jobGroup"  )  ; 
Converter   converter  =  new   DateConverter  (  new   String  [  ]  {  XSD_DATE_FORMAT  ,  DTD_DATE_FORMAT  }  )  ; 
digester  .  addRule  (  prefix  +  "/"  +  TAG_START_TIME  ,  new   SimpleConverterRule  (  "startTime"  ,  converter  ,  Date  .  class  )  )  ; 
digester  .  addRule  (  prefix  +  "/"  +  TAG_END_TIME  ,  new   SimpleConverterRule  (  "endTime"  ,  converter  ,  Date  .  class  )  )  ; 
} 
} 























public   class   SimpleConverterRule   extends   BeanPropertySetterRule  { 

private   Converter   converter  ; 

private   Class   clazz  ; 








public   SimpleConverterRule  (  String   propertyName  ,  Converter   converter  ,  Class   clazz  )  { 
this  .  propertyName  =  propertyName  ; 
if  (  converter  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Converter must not be null"  )  ; 
} 
this  .  converter  =  converter  ; 
if  (  clazz  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Class must not be null"  )  ; 
} 
this  .  clazz  =  clazz  ; 
} 













public   void   end  (  String   namespace  ,  String   name  )  throws   Exception  { 
String   property  =  propertyName  ; 
if  (  property  ==  null  )  { 
property  =  name  ; 
} 
Object   top  =  this  .  digester  .  peek  (  )  ; 
if  (  getDigester  (  )  .  getLogger  (  )  .  isDebugEnabled  (  )  )  { 
getDigester  (  )  .  getLogger  (  )  .  debug  (  "[BeanPropertySetterRule]{"  +  getDigester  (  )  .  getMatch  (  )  +  "} Set "  +  top  .  getClass  (  )  .  getName  (  )  +  " property "  +  property  +  " with text "  +  bodyText  )  ; 
} 
if  (  top   instanceof   DynaBean  )  { 
DynaProperty   desc  =  (  (  DynaBean  )  top  )  .  getDynaClass  (  )  .  getDynaProperty  (  property  )  ; 
if  (  desc  ==  null  )  { 
throw   new   NoSuchMethodException  (  "Bean has no property named "  +  property  )  ; 
} 
}  else  { 
PropertyDescriptor   desc  =  PropertyUtils  .  getPropertyDescriptor  (  top  ,  property  )  ; 
if  (  desc  ==  null  )  { 
throw   new   NoSuchMethodException  (  "Bean has no property named "  +  property  )  ; 
} 
} 
Object   value  =  converter  .  convert  (  clazz  ,  bodyText  )  ; 
PropertyUtils  .  setProperty  (  top  ,  property  ,  value  )  ; 
} 
} 













public   class   MisfireInstructionRule   extends   BeanPropertySetterRule  { 






public   MisfireInstructionRule  (  String   propertyName  )  { 
this  .  propertyName  =  propertyName  ; 
} 











public   void   body  (  String   namespace  ,  String   name  ,  String   text  )  throws   Exception  { 
super  .  body  (  namespace  ,  name  ,  text  )  ; 
this  .  bodyText  =  getConstantValue  (  bodyText  )  ; 
} 









private   String   getConstantValue  (  String   constantName  )  { 
String   value  =  "0"  ; 
Object   top  =  this  .  digester  .  peek  (  )  ; 
if  (  top  !=  null  )  { 
Class   clazz  =  top  .  getClass  (  )  ; 
try  { 
java  .  lang  .  reflect  .  Field   field  =  clazz  .  getField  (  constantName  )  ; 
Object   fieldValue  =  field  .  get  (  top  )  ; 
if  (  fieldValue  !=  null  )  { 
value  =  fieldValue  .  toString  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
} 
return   value  ; 
} 
} 







public   final   class   DateConverter   implements   Converter  { 





public   DateConverter  (  )  { 
this  .  defaultValue  =  null  ; 
this  .  useDefault  =  false  ; 
} 







public   DateConverter  (  Object   defaultValue  )  { 
this  .  defaultValue  =  defaultValue  ; 
this  .  useDefault  =  true  ; 
} 

public   DateConverter  (  String  [  ]  formats  )  { 
this  (  )  ; 
int   len  =  formats  .  length  ; 
dateFormats  =  new   DateFormat  [  len  ]  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
dateFormats  [  i  ]  =  new   SimpleDateFormat  (  formats  [  i  ]  )  ; 
} 
} 




private   Object   defaultValue  =  null  ; 




private   boolean   useDefault  =  true  ; 

private   DateFormat  [  ]  dateFormats  ; 











public   Object   convert  (  Class   type  ,  Object   value  )  { 
if  (  value  ==  null  )  { 
if  (  useDefault  )  { 
return  (  defaultValue  )  ; 
}  else  { 
return  (  null  )  ; 
} 
} 
if  (  String  .  class  .  equals  (  type  )  )  { 
if  (  (  value   instanceof   Date  )  &&  (  dateFormats  !=  null  )  )  { 
return  (  dateFormats  [  0  ]  .  format  (  (  Date  )  value  )  )  ; 
}  else  { 
return  (  value  .  toString  (  )  )  ; 
} 
} 
if  (  value   instanceof   Date  )  { 
return  (  value  )  ; 
} 
try  { 
if  (  Date  .  class  .  isAssignableFrom  (  type  )  &&  dateFormats  !=  null  )  { 
return   parseDate  (  value  )  ; 
}  else  { 
return  (  value  .  toString  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
if  (  useDefault  )  { 
return  (  defaultValue  )  ; 
}  else  { 
throw   new   ConversionException  (  e  )  ; 
} 
} 
} 

protected   Date   parseDate  (  Object   value  )  throws   ParseException  { 
Date   date  =  null  ; 
int   len  =  dateFormats  .  length  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
try  { 
date  =  (  dateFormats  [  i  ]  .  parse  (  value  .  toString  (  )  )  )  ; 
break  ; 
}  catch  (  ParseException   e  )  { 
if  (  i  ==  (  len  -  1  )  )  { 
throw   e  ; 
} 
} 
} 
return   date  ; 
} 
} 






public   final   class   TimeZoneConverter   implements   Converter  { 





public   TimeZoneConverter  (  )  { 
} 











public   Object   convert  (  Class   type  ,  Object   value  )  { 
if  (  value  ==  null  )  { 
return  (  null  )  ; 
} 
if  (  value   instanceof   TimeZone  )  { 
return  (  value  )  ; 
} 
try  { 
if  (  String  .  class  .  equals  (  value  .  getClass  (  )  )  )  { 
return  (  TimeZone  .  getTimeZone  (  (  String  )  value  )  )  ; 
}  else  { 
return  (  value  .  toString  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
throw   new   ConversionException  (  e  )  ; 
} 
} 
} 
} 

