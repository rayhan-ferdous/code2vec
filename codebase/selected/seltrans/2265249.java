package   doors  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  logging  .  ConsoleHandler  ; 
import   java  .  util  .  logging  .  Handler  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 






public   class   Util  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  "Util"  )  ; 




public   static   String  [  ]  parseList  (  String   string  )  { 
List   list  =  new   Vector  (  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  string  ,  ","  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
list  .  add  (  st  .  nextToken  (  )  )  ; 
} 
return  (  String  [  ]  )  list  .  toArray  (  new   String  [  0  ]  )  ; 
} 






public   static   void   typeToExit  (  String   msg  ,  String   keyword  )  { 
System  .  out  .  println  (  msg  )  ; 
java  .  io  .  BufferedReader   kb  =  new   java  .  io  .  BufferedReader  (  new   java  .  io  .  InputStreamReader  (  System  .  in  )  )  ; 
try  { 
while  (  kb  .  readLine  (  )  .  equalsIgnoreCase  (  keyword  )  ==  false  )  { 
System  .  out  .  println  (  msg  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 





public   static   String   getLocalHostName  (  )  { 
String   name  =  null  ; 
try  { 
name  =  java  .  net  .  InetAddress  .  getLocalHost  (  )  .  getHostName  (  )  ; 
}  catch  (  java  .  net  .  UnknownHostException   e  )  { 
logger  .  log  (  Level  .  SEVERE  ,  "Cannot get name of localhost"  ,  e  )  ; 
int   randomNumber  =  new   Random  (  )  .  nextInt  (  )  %  99  ; 
name  =  "localhost"  +  randomNumber  ; 
} 
return   name  ; 
} 




public   static   String   stripTrailingDigits  (  String   str  )  { 
int   crop  =  str  .  length  (  )  -  1  ; 
while  (  str  .  charAt  (  crop  )  >=  48  &&  str  .  charAt  (  crop  )  <=  57  )  crop  --  ; 
return   str  .  substring  (  0  ,  crop  +  1  )  ; 
} 








public   static   String   getThrowableDetail  (  Throwable   e  ,  boolean   extensive  )  { 
StringBuffer   sb  =  new   StringBuffer  (  "getThrowableDetail(e): "  )  ; 
if  (  e  ==  null  )  { 
sb  .  append  (  "Exception was null!"  )  ; 
}  else  { 
sb  .  append  (  "\n"  )  ; 
if  (  extensive  )  if  (  e  .  getLocalizedMessage  (  )  !=  null  )  { 
sb  .  append  (  "getLocalizedMessage(): "  )  ; 
sb  .  append  (  e  .  getLocalizedMessage  (  )  +  "\n"  )  ; 
} 
if  (  e  .  getMessage  (  )  !=  null  )  { 
sb  .  append  (  "getMessage(): "  )  ; 
sb  .  append  (  e  .  getMessage  (  )  +  "\n"  )  ; 
} 
sb  .  append  (  "toString(): "  )  ; 
sb  .  append  (  e  .  toString  (  )  +  "\n"  )  ; 
if  (  extensive  )  { 
sb  .  append  (  "printStackTrace(): "  )  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
e  .  printStackTrace  (  new   PrintStream  (  baos  )  )  ; 
sb  .  append  (  baos  .  toString  (  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   String   readUrl  (  URL   url  )  throws   IOException  { 
java  .  io  .  BufferedReader   is  =  new   java  .  io  .  BufferedReader  (  new   java  .  io  .  InputStreamReader  (  url  .  openStream  (  )  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
int   c  =  is  .  read  (  )  ; 
while  (  c  !=  -  1  )  { 
sb  .  append  (  (  char  )  c  )  ; 
c  =  is  .  read  (  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   void   writeFile  (  File   file  ,  String   str  )  throws   java  .  io  .  IOException  { 
FileWriter   fw  =  new   FileWriter  (  file  )  ; 
fw  .  write  (  str  )  ; 
fw  .  flush  (  )  ; 
fw  .  close  (  )  ; 
} 







public   static   String   stringToDec  (  String   str  )  { 
StringBuffer   r  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
r  .  append  (  (  int  )  str  .  charAt  (  i  )  )  ; 
if  (  i  !=  (  str  .  length  (  )  -  1  )  )  r  .  append  (  ", "  )  ; 
} 
return   r  .  toString  (  )  ; 
} 







public   static   String   stringToHex  (  String   str  )  { 
StringBuffer   r  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  str  .  length  (  )  ;  i  ++  )  { 
r  .  append  (  Integer  .  toHexString  (  str  .  charAt  (  i  )  )  )  ; 
if  (  i  !=  (  str  .  length  (  )  -  1  )  )  r  .  append  (  ", "  )  ; 
} 
return   r  .  toString  (  )  ; 
} 






public   static   String   formatAbsoluteTime  (  long   t  )  { 
return   java  .  text  .  NumberFormat  .  getInstance  (  )  .  format  (  t  )  ; 
} 




public   static   double   toOneDP  (  double   d  )  { 
long   r  =  Math  .  round  (  d  *  10  )  ; 
return  (  double  )  r  /  10  ; 
} 











public   static   String   decodeUrlCharacters  (  String   str  )  { 
StringBuffer   r  =  new   StringBuffer  (  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  str  ,  "%"  )  ; 
String   token  ; 
boolean   first  =  true  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
token  =  st  .  nextToken  (  )  ; 
if  (  first  &&  (  !  str  .  startsWith  (  "%"  )  )  )  { 
first  =  false  ; 
r  .  append  (  token  )  ; 
}  else  { 
try  { 
int   i  =  Integer  .  valueOf  (  token  .  substring  (  0  ,  2  )  ,  16  )  .  intValue  (  )  ; 
r  .  append  (  (  char  )  i  )  ; 
r  .  append  (  token  .  substring  (  2  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
r  .  append  (  "%"  +  token  )  ; 
} 
} 
} 
return   r  .  toString  (  )  ; 
} 




public   static   void   printOut  (  InputStream   inputStream  )  throws   IOException  { 
BufferedReader   br  =  new   BufferedReader  (  new   InputStreamReader  (  inputStream  )  )  ; 
String   line  =  br  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
System  .  out  .  println  (  line  )  ; 
line  =  br  .  readLine  (  )  ; 
} 
} 

public   static   String   readPropertyOrExit  (  Properties   properties  ,  String   name  )  { 
String   value  =  properties  .  getProperty  (  name  )  ; 
if  (  value  ==  null  )  { 
logger  .  severe  (  "Error: Cannot read parameter "  +  name  +  " in config file!"  )  ; 
System  .  exit  (  1  )  ; 
} 
return   value  ; 
} 




public   static   void   initialiseLogger  (  )  { 
Handler  [  ]  handlers  =  Logger  .  getLogger  (  ""  )  .  getHandlers  (  )  ; 
for  (  int   i  =  0  ;  i  <  handlers  .  length  ;  i  ++  )  { 
Handler   handler  =  handlers  [  i  ]  ; 
if  (  handler   instanceof   ConsoleHandler  )  { 
handler  .  setFormatter  (  new   SuccinctlLogFormatter  (  )  )  ; 
} 
} 
} 




public   static   boolean   isWin32  (  )  { 
return   System  .  getProperty  (  "os.name"  ,  "unknown"  )  .  toLowerCase  (  )  .  indexOf  (  "win"  )  !=  -  1  ; 
} 
} 

