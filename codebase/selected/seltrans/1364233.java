package   edu  .  mit  .  osidimpl  .  id  .  local  ; 

import   edu  .  mit  .  osidimpl  .  manager  .  *  ; 
import   edu  .  mit  .  osidimpl  .  id  .  shared  .  *  ; 
































public   class   IdManager   extends   OsidManagerWithCascadingPropertiesAndLogging   implements   org  .  osid  .  id  .  IdManager  { 

OsidLogger   logger  ; 

private   String   file  =  null  ; 

private   String   prefix  =  null  ; 

private   long   max  =  0  ; 




public   IdManager  (  )  { 
super  (  )  ; 
} 




public   void   initialize  (  )  { 
String   property  ; 
logger  =  getLogger  (  )  ; 
logger  .  logDebug  (  "initializing IdManager"  )  ; 
property  =  getConfiguration  (  "file"  )  ; 
if  (  property  !=  null  )  { 
this  .  file  =  property  ; 
logger  .  logTrace  (  "file="  +  this  .  file  )  ; 
} 
property  =  getConfiguration  (  "prefix"  )  ; 
if  (  property  !=  null  )  { 
this  .  prefix  =  property  ; 
logger  .  logTrace  (  "prefix="  +  this  .  prefix  )  ; 
} 
property  =  getConfiguration  (  "max"  )  ; 
if  (  property  !=  null  )  { 
try  { 
this  .  max  =  Long  .  parseLong  (  property  )  ; 
}  catch  (  NumberFormatException   nfe  )  { 
logger  .  logError  (  "unable to parse max value"  )  ; 
} 
logger  .  logTrace  (  "max="  +  this  .  max  )  ; 
} 
if  (  this  .  max  ==  0  )  { 
this  .  max  =  Long  .  MAX_VALUE  ; 
} 
return  ; 
} 
















public   org  .  osid  .  shared  .  Id   createId  (  )  throws   org  .  osid  .  id  .  IdException  { 
long   seq  ; 
if  (  this  .  file  ==  null  )  { 
logger  .  logCritical  (  "cache file not set"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  CONFIGURATION_ERROR  )  ; 
} 
try  { 
seq  =  getSequence  (  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  logCritical  (  "cannot get sequence number: "  +  e  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
++  seq  ; 
if  (  seq  <=  0  )  { 
logger  .  logCritical  (  "sequence number wrapped"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
if  (  seq  >  max  )  { 
logger  .  logCritical  (  "sequence number exceeds specified limit"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
try  { 
setSequence  (  seq  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  logCritical  (  "cannot set sequence numb er: "  +  e  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
String   id  ; 
if  (  this  .  prefix  ==  null  )  { 
id  =  Long  .  toString  (  seq  )  ; 
}  else  { 
id  =  this  .  prefix  +  Long  .  toString  (  seq  )  ; 
} 
logger  .  logInfo  (  "assigned id "  +  seq  )  ; 
return  (  getId  (  id  )  )  ; 
} 














public   org  .  osid  .  shared  .  Id   getId  (  String   idString  )  throws   org  .  osid  .  id  .  IdException  { 
return  (  new   Id  (  idString  )  )  ; 
} 

private   long   getSequence  (  )  throws   org  .  osid  .  id  .  IdException  { 
if  (  this  .  file  ==  null  )  { 
logger  .  logError  (  "no sequence file specified"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  CONFIGURATION_ERROR  )  ; 
} 
java  .  io  .  File   file  ; 
java  .  nio  .  channels  .  FileChannel   channel  ; 
java  .  nio  .  channels  .  FileLock   lock  ; 
try  { 
file  =  new   java  .  io  .  File  (  this  .  file  )  ; 
channel  =  new   java  .  io  .  RandomAccessFile  (  file  ,  "r"  )  .  getChannel  (  )  ; 
lock  =  channel  .  lock  (  0  ,  Long  .  MAX_VALUE  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  logError  (  "unable to lock file "  +  this  .  file  +  ": "  +  e  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
org  .  apache  .  xerces  .  parsers  .  DOMParser   parser  =  new   org  .  apache  .  xerces  .  parsers  .  DOMParser  (  )  ; 
try  { 
parser  .  parse  (  this  .  file  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  logError  (  "cannot parse sequence file "  +  this  .  file  +  ": "  +  e  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
}  finally  { 
try  { 
lock  .  release  (  )  ; 
channel  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   ie  )  { 
logger  .  logError  (  "cannot release lock on file "  +  this  .  file  +  ": "  +  ie  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
} 
org  .  w3c  .  dom  .  Document   doc  =  parser  .  getDocument  (  )  ; 
org  .  w3c  .  dom  .  NodeList   nodes  =  doc  .  getElementsByTagName  (  "sequence"  )  ; 
if  (  nodes  ==  null  )  { 
logger  .  logError  (  "no id node, error in parsing "  +  this  .  file  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
if  (  nodes  .  getLength  (  )  !=  1  )  { 
logger  .  logError  (  "multiple nodes, error in parsing "  +  this  .  file  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
org  .  w3c  .  dom  .  Element   element  =  (  org  .  w3c  .  dom  .  Element  )  nodes  .  item  (  0  )  ; 
String   value  =  element  .  getFirstChild  (  )  .  getNodeValue  (  )  ; 
long   ret  ; 
try  { 
ret  =  Long  .  parseLong  (  value  .  trim  (  )  )  ; 
}  catch  (  NumberFormatException   nfe  )  { 
logger  .  logError  (  "unable to parse: "  +  value  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
if  (  ret  <  0  )  { 
logger  .  logError  (  "bad number: "  +  ret  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
return  (  ret  )  ; 
} 

private   void   setSequence  (  long   sequence  )  throws   org  .  osid  .  id  .  IdException  { 
if  (  this  .  file  ==  null  )  { 
logger  .  logError  (  "no sequence file specified"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  CONFIGURATION_ERROR  )  ; 
} 
java  .  io  .  File   file  ; 
java  .  nio  .  channels  .  FileChannel   channel  ; 
java  .  nio  .  channels  .  FileLock   lock  ; 
try  { 
file  =  new   java  .  io  .  File  (  this  .  file  )  ; 
channel  =  new   java  .  io  .  RandomAccessFile  (  file  ,  "r"  )  .  getChannel  (  )  ; 
lock  =  channel  .  lock  (  0  ,  Long  .  MAX_VALUE  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  logError  (  "unable to lock file "  +  this  .  file  +  ": "  +  e  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
java  .  text  .  SimpleDateFormat   sdf  =  new   java  .  text  .  SimpleDateFormat  (  "EEE, dd MMM yyy HH:mm:ss Z"  )  ; 
java  .  util  .  Date   date  =  new   java  .  util  .  Date  (  )  ; 
try  { 
java  .  io  .  BufferedWriter   out  =  new   java  .  io  .  BufferedWriter  (  new   java  .  io  .  FileWriter  (  file  )  )  ; 
out  .  write  (  "<?xml version=\"1.0\"?>\n"  )  ; 
out  .  write  (  "<IdManager impl=\"edu.mit.osidimpl.id.local\">\n"  )  ; 
out  .  write  (  "    <id assignedDate=\""  +  sdf  .  format  (  date  )  +  "\">\n"  )  ; 
if  (  this  .  prefix  !=  null  )  { 
out  .  write  (  "        <prefix>\n"  )  ; 
out  .  write  (  "            "  +  this  .  prefix  +  "\n"  )  ; 
out  .  write  (  "        </prefix>\n"  )  ; 
} 
out  .  write  (  "        <sequence>\n"  )  ; 
out  .  write  (  "            "  +  sequence  +  "\n"  )  ; 
out  .  write  (  "        </sequence>\n"  )  ; 
out  .  write  (  "    </id>\n"  )  ; 
out  .  write  (  "</IdManager>\n"  )  ; 
out  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   ie  )  { 
logger  .  logError  (  "unable to write identifier to "  +  this  .  file  +  ": "  +  ie  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
try  { 
lock  .  release  (  )  ; 
channel  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   ie  )  { 
logger  .  logError  (  "cannot release lock on file "  +  this  .  file  +  ": "  +  ie  .  getMessage  (  )  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
return  ; 
} 
} 

