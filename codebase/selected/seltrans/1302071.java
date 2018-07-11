package   org  .  omegat  .  util  .  logging  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  logging  .  ErrorManager  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  LogManager  ; 
import   java  .  util  .  logging  .  LogRecord  ; 
import   java  .  util  .  logging  .  StreamHandler  ; 
import   org  .  omegat  .  util  .  OConsts  ; 
import   org  .  omegat  .  util  .  StaticUtils  ; 
















public   class   OmegaTFileHandler   extends   StreamHandler  { 

private   File   lockFile  ; 

private   FileOutputStream   lockStream  ; 

private   final   long   maxSize  ; 

private   final   int   count  ; 

public   OmegaTFileHandler  (  )  throws   IOException  { 
LogManager   manager  =  LogManager  .  getLogManager  (  )  ; 
String   cname  =  getClass  (  )  .  getName  (  )  ; 
String   level  =  manager  .  getProperty  (  cname  +  ".level"  )  ; 
if  (  level  !=  null  )  { 
setLevel  (  Level  .  parse  (  level  .  trim  (  )  )  )  ; 
} 
String   maxSizeStr  =  manager  .  getProperty  (  cname  +  ".size"  )  ; 
if  (  maxSizeStr  !=  null  )  { 
maxSize  =  Long  .  parseLong  (  maxSizeStr  )  ; 
}  else  { 
maxSize  =  1024  *  1024  ; 
} 
String   countStr  =  manager  .  getProperty  (  cname  +  ".count"  )  ; 
if  (  countStr  !=  null  )  { 
count  =  Integer  .  parseInt  (  countStr  )  ; 
}  else  { 
count  =  10  ; 
} 
openFiles  (  new   File  (  StaticUtils  .  getConfigDir  (  )  ,  "logs"  )  )  ; 
} 




private   void   openFiles  (  final   File   dir  )  throws   IOException  { 
dir  .  mkdirs  (  )  ; 
for  (  int   instanceIndex  =  0  ;  instanceIndex  <  100  ;  instanceIndex  ++  )  { 
String   fileName  =  "OmegaT"  +  (  instanceIndex  >  0  ?  (  "-"  +  instanceIndex  )  :  ""  )  ; 
lockFile  =  new   File  (  dir  ,  fileName  +  ".log.lck"  )  ; 
lockStream  =  new   FileOutputStream  (  lockFile  )  ; 
if  (  lockStream  .  getChannel  (  )  .  tryLock  (  )  !=  null  )  { 
rotate  (  dir  ,  fileName  )  ; 
setEncoding  (  OConsts  .  UTF8  )  ; 
setOutputStream  (  new   FileOutputStream  (  new   File  (  dir  ,  fileName  +  ".log"  )  ,  true  )  )  ; 
break  ; 
} 
} 
setErrorManager  (  new   ErrorManager  (  )  )  ; 
} 

@  Override 
public   synchronized   void   close  (  )  throws   SecurityException  { 
try  { 
lockStream  .  close  (  )  ; 
lockFile  .  delete  (  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 




private   void   rotate  (  final   File   dir  ,  final   String   fileName  )  { 
File   logFile  =  new   File  (  dir  ,  fileName  +  ".log"  )  ; 
if  (  !  logFile  .  exists  (  )  ||  logFile  .  length  (  )  <  maxSize  )  { 
return  ; 
} 
String   suffix  =  new   SimpleDateFormat  (  "yyyyMMdd.HHmm"  )  .  format  (  new   Date  (  )  )  ; 
File   destFile  =  new   File  (  dir  ,  fileName  +  '.'  +  suffix  +  ".log"  )  ; 
logFile  .  renameTo  (  destFile  )  ; 
File  [  ]  oldLogs  =  dir  .  listFiles  (  new   FileFilter  (  )  { 

public   boolean   accept  (  File   pathname  )  { 
return   pathname  .  getName  (  )  .  startsWith  (  fileName  +  '.'  )  &&  pathname  .  getName  (  )  .  endsWith  (  ".log"  )  ; 
} 
}  )  ; 
if  (  oldLogs  !=  null  )  { 
Arrays  .  sort  (  oldLogs  ,  new   Comparator  <  File  >  (  )  { 

public   int   compare  (  File   o1  ,  File   o2  )  { 
return   o2  .  getName  (  )  .  compareToIgnoreCase  (  o1  .  getName  (  )  )  ; 
} 
}  )  ; 
for  (  int   i  =  count  ;  i  <  oldLogs  .  length  ;  i  ++  )  { 
oldLogs  [  i  ]  .  delete  (  )  ; 
} 
} 
} 

@  Override 
public   synchronized   void   publish  (  LogRecord   record  )  { 
if  (  isLoggable  (  record  )  )  { 
super  .  publish  (  record  )  ; 
flush  (  )  ; 
} 
} 
} 

