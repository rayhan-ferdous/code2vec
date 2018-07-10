package   edu  .  mit  .  osidimpl  .  id  .  unix  ; 

import   edu  .  mit  .  osidimpl  .  manager  .  *  ; 
import   edu  .  mit  .  osidimpl  .  id  .  shared  .  *  ; 


































public   class   IdManager   extends   OsidManagerWithCascadingPropertiesAndLogging   implements   org  .  osid  .  id  .  IdManager  { 

OsidLogger   logger  ; 

private   String   file  =  null  ; 

private   String   prefix  =  null  ; 

private   String   passwd_file  =  null  ; 

private   String   group_file  =  null  ; 

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
property  =  getConfiguration  (  "passwd_file"  )  ; 
if  (  property  !=  null  )  { 
this  .  passwd_file  =  property  ; 
logger  .  logTrace  (  "passwd_file="  +  this  .  passwd_file  )  ; 
} 
property  =  getConfiguration  (  "group_file"  )  ; 
if  (  property  !=  null  )  { 
this  .  group_file  =  property  ; 
logger  .  logTrace  (  "group_file="  +  this  .  group_file  )  ; 
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
int   seq  ; 
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
while  (  true  )  { 
edu  .  mit  .  osidutil  .  contrivance  .  PasswdFile   pf  ; 
try  { 
pf  =  new   edu  .  mit  .  osidutil  .  contrivance  .  PasswdFile  (  this  .  passwd_file  )  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  PasswdFileException   pfe  )  { 
logger  .  logError  (  "unable to open passwd file: "  +  this  .  passwd_file  +  "("  +  pfe  .  getMessage  (  )  +  ")"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
try  { 
edu  .  mit  .  osidutil  .  contrivance  .  PasswdEntry   entry  =  pf  .  lookupEntry  (  seq  )  ; 
++  seq  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  PasswdFileUserNotFoundException   pnfe  )  { 
break  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  PasswdFileException   pfe  )  { 
logger  .  logError  (  "unable to read passwd file: "  +  this  .  passwd_file  +  "("  +  pfe  .  getMessage  (  )  +  ")"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
} 
while  (  true  )  { 
edu  .  mit  .  osidutil  .  contrivance  .  GroupFile   gf  ; 
try  { 
gf  =  new   edu  .  mit  .  osidutil  .  contrivance  .  GroupFile  (  this  .  group_file  )  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  GroupFileException   gfe  )  { 
logger  .  logError  (  "unable to open group file: "  +  this  .  group_file  +  "("  +  gfe  .  getMessage  (  )  +  ")"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
try  { 
edu  .  mit  .  osidutil  .  contrivance  .  GroupEntry   entry  =  gf  .  lookupEntry  (  seq  )  ; 
++  seq  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  GroupFileGroupNotFoundException   gnfe  )  { 
break  ; 
}  catch  (  edu  .  mit  .  osidutil  .  contrivance  .  GroupFileException   gfe  )  { 
logger  .  logError  (  "unable to read group file: "  +  this  .  passwd_file  +  "("  +  gfe  .  getMessage  (  )  +  ")"  )  ; 
throw   new   org  .  osid  .  id  .  IdException  (  org  .  osid  .  id  .  IdException  .  OPERATION_FAILED  )  ; 
} 
} 
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
logger  .  logCritical  (  "cannot set sequence number: "  +  e  .  getMessage  (  )  )  ; 
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

private   int   getSequence  (  )  throws   org  .  osid  .  id  .  IdException  { 
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
int   ret  ; 
try  { 
ret  =  Integer  .  parseInt  (  value  .  trim  (  )  )  ; 
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
lock  =  channel  .  lock  (  0  ,  Integer  .  MAX_VALUE  ,  true  )  ; 
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

