package   org  .  virbo  .  jythonsupport  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 
import   java  .  nio  .  channels  .  Channels  ; 
import   java  .  nio  .  channels  .  ReadableByteChannel  ; 
import   java  .  nio  .  channels  .  WritableByteChannel  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Map  ; 
import   org  .  das2  .  datum  .  DatumRange  ; 
import   org  .  das2  .  datum  .  DatumRangeUtil  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  das2  .  util  .  filesystem  .  FileSystem  ; 
import   org  .  das2  .  util  .  filesystem  .  Glob  ; 
import   org  .  das2  .  util  .  monitor  .  NullProgressMonitor  ; 
import   org  .  das2  .  util  .  monitor  .  ProgressMonitor  ; 
import   org  .  virbo  .  dataset  .  DDataSet  ; 
import   org  .  virbo  .  dataset  .  DataSetUtil  ; 
import   org  .  virbo  .  dataset  .  QDataSet  ; 
import   org  .  virbo  .  dataset  .  WritableDataSet  ; 
import   org  .  virbo  .  datasource  .  DataSetURI  ; 
import   org  .  virbo  .  datasource  .  DataSource  ; 
import   org  .  virbo  .  datasource  .  DataSourceFactory  ; 
import   org  .  virbo  .  datasource  .  DataSourceUtil  ; 
import   org  .  virbo  .  datasource  .  capability  .  TimeSeriesBrowse  ; 





public   class   Util  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  "virbo.jython"  )  ; 











public   static   QDataSet   getDataSet  (  String   surl  ,  String   stimeRange  ,  ProgressMonitor   mon  )  throws   Exception  { 
logger  .  log  (  Level  .  FINE  ,  "getDataSet({0},{1})"  ,  new   Object  [  ]  {  surl  ,  stimeRange  }  )  ; 
URI   uri  =  DataSetURI  .  getURI  (  surl  )  ; 
DataSourceFactory   factory  =  DataSetURI  .  getDataSourceFactory  (  uri  ,  new   NullProgressMonitor  (  )  )  ; 
DataSource   result  =  factory  .  getDataSource  (  uri  )  ; 
if  (  mon  ==  null  )  { 
mon  =  new   NullProgressMonitor  (  )  ; 
} 
TimeSeriesBrowse   tsb  =  result  .  getCapability  (  TimeSeriesBrowse  .  class  )  ; 
if  (  tsb  !=  null  )  { 
DatumRange   timeRange  =  DatumRangeUtil  .  parseTimeRange  (  stimeRange  )  ; 
tsb  .  setTimeRange  (  timeRange  )  ; 
}  else  { 
System  .  err  .  println  (  "Warning: TimeSeriesBrowse capability not found, simply returning dataset."  )  ; 
} 
QDataSet   rds  =  result  .  getDataSet  (  mon  )  ; 
try  { 
metadata  =  result  .  getMetadata  (  new   NullProgressMonitor  (  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
metadataSurl  =  surl  ; 
if  (  rds  ==  null  )  return   null  ; 
if  (  rds   instanceof   WritableDataSet  &&  DataSetUtil  .  isQube  (  rds  )  )  { 
return   rds  ; 
}  else  { 
if  (  DataSetUtil  .  isQube  (  rds  )  )  { 
return   DDataSet  .  copy  (  rds  )  ; 
}  else  { 
System  .  err  .  println  (  "unable to copy read-only dataset, which may cause problems elsewhere."  )  ; 
return   rds  ; 
} 
} 
} 







public   static   QDataSet   getDataSet  (  String   surl  ,  ProgressMonitor   mon  )  throws   Exception  { 
logger  .  log  (  Level  .  FINE  ,  "getDataSet({0})"  ,  surl  )  ; 
URI   uri  =  DataSetURI  .  getURIValid  (  surl  )  ; 
DataSourceFactory   factory  =  DataSetURI  .  getDataSourceFactory  (  uri  ,  new   NullProgressMonitor  (  )  )  ; 
DataSource   result  =  factory  .  getDataSource  (  uri  )  ; 
if  (  mon  ==  null  )  { 
mon  =  new   NullProgressMonitor  (  )  ; 
} 
QDataSet   rds  =  result  .  getDataSet  (  mon  )  ; 
try  { 
metadata  =  result  .  getMetadata  (  new   NullProgressMonitor  (  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
metadataSurl  =  surl  ; 
if  (  rds  ==  null  )  return   null  ; 
if  (  rds   instanceof   WritableDataSet  &&  DataSetUtil  .  isQube  (  rds  )  )  { 
return   rds  ; 
}  else  { 
if  (  DataSetUtil  .  isQube  (  rds  )  )  { 
return   DDataSet  .  copy  (  rds  )  ; 
}  else  { 
System  .  err  .  println  (  "unable to copy read-only dataset, which may cause problems elsewhere."  )  ; 
return   rds  ; 
} 
} 
} 

private   static   Map  <  String  ,  Object  >  metadata  ; 

private   static   String   metadataSurl  ; 




public   static   Map  <  String  ,  Object  >  getMetaData  (  String   surl  ,  ProgressMonitor   mon  )  throws   Exception  { 
return   getMetadata  (  surl  ,  mon  )  ; 
} 










public   static   Map  <  String  ,  Object  >  getMetadata  (  String   surl  ,  ProgressMonitor   mon  )  throws   Exception  { 
logger  .  log  (  Level  .  FINE  ,  "getMetadata({0})"  ,  surl  )  ; 
if  (  surl  .  equals  (  metadataSurl  )  )  { 
return   metadata  ; 
}  else  { 
URI   url  =  DataSetURI  .  getURIValid  (  surl  )  ; 
DataSourceFactory   factory  =  DataSetURI  .  getDataSourceFactory  (  url  ,  new   NullProgressMonitor  (  )  )  ; 
DataSource   result  =  factory  .  getDataSource  (  url  )  ; 
if  (  mon  ==  null  )  { 
mon  =  new   NullProgressMonitor  (  )  ; 
} 
return   result  .  getMetadata  (  mon  )  ; 
} 
} 








public   static   QDataSet   getDataSet  (  String   surl  )  throws   Exception  { 
return   getDataSet  (  surl  ,  null  )  ; 
} 













public   static   QDataSet   getDataSet  (  String   spec  ,  InputStream   in  ,  ProgressMonitor   mon  )  throws   Exception  { 
logger  .  log  (  Level  .  FINE  ,  "getDataSet({0},InputStream)"  ,  new   Object  [  ]  {  spec  }  )  ; 
String  [  ]  ss  =  spec  .  split  (  ":"  ,  -  2  )  ; 
String   ext  ; 
int   i  =  ss  [  0  ]  .  indexOf  (  "+"  )  ; 
ext  =  (  i  ==  -  1  )  ?  ss  [  0  ]  :  ss  [  0  ]  .  substring  (  i  +  1  )  ; 
File   f  =  File  .  createTempFile  (  "autoplot"  ,  "."  +  ext  )  ; 
ReadableByteChannel   chin  =  Channels  .  newChannel  (  in  )  ; 
WritableByteChannel   chout  =  new   FileOutputStream  (  f  )  .  getChannel  (  )  ; 
DataSourceUtil  .  transfer  (  chin  ,  chout  )  ; 
String   virtUrl  =  ss  [  0  ]  +  ":"  +  f  .  toURI  (  )  .  toString  (  )  +  ss  [  1  ]  ; 
QDataSet   ds  =  getDataSet  (  virtUrl  ,  mon  )  ; 
return   ds  ; 
} 













public   static   String  [  ]  listDirectory  (  String   surl  )  throws   IOException  ,  URISyntaxException  { 
logger  .  log  (  Level  .  FINE  ,  "listDirectory({0})"  ,  surl  )  ; 
String  [  ]  ss  =  FileSystem  .  splitUrl  (  surl  )  ; 
FileSystem   fs  =  FileSystem  .  create  (  DataSetURI  .  toUri  (  ss  [  2  ]  )  )  ; 
String   glob  =  ss  [  3  ]  .  substring  (  ss  [  2  ]  .  length  (  )  )  ; 
String  [  ]  result  ; 
if  (  glob  .  length  (  )  ==  0  )  { 
result  =  fs  .  listDirectory  (  "/"  )  ; 
}  else  { 
result  =  fs  .  listDirectory  (  "/"  ,  Glob  .  getRegex  (  glob  )  )  ; 
} 
Arrays  .  sort  (  result  )  ; 
return   result  ; 
} 
} 

