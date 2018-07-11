package   org  .  netlogo  .  extension  .  r  ; 

import   java  .  util  .  Properties  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  FileInputStream  ; 
import   org  .  nlogo  .  api  .  Argument  ; 
import   org  .  nlogo  .  api  .  Context  ; 
import   org  .  nlogo  .  api  .  DefaultCommand  ; 
import   org  .  nlogo  .  api  .  DefaultReporter  ; 
import   org  .  nlogo  .  api  .  ExtensionException  ; 
import   org  .  nlogo  .  api  .  LogoException  ; 
import   org  .  nlogo  .  api  .  Syntax  ; 
import   com  .  sun  .  org  .  apache  .  bcel  .  internal  .  generic  .  Type  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashMap  ; 
import   java  .  io  .  Console  ; 
import   java  .  lang  .  reflect  .  Method  ; 







public   class   Entry   extends   org  .  nlogo  .  api  .  DefaultClassManager  { 









private   static   org  .  nlogo  .  api  .  ExtensionManager   rConn  =  null  ; 






public   void   runOnce  (  org  .  nlogo  .  api  .  ExtensionManager   em  )  throws   ExtensionException  { 
try  { 
if  (  em  .  retrieveObject  (  )  ==  null  )  { 
final   String   filesep  =  System  .  getProperty  (  "file.separator"  )  ; 
String   filepath  =  System  .  getenv  (  "JRI_HOME"  )  ; 
JavaLibraryPath  .  addFile  (  filepath  +  filesep  +  "JRI.jar"  )  ; 
JavaLibraryPath  .  addLibraryPath  (  new   java  .  io  .  File  (  filepath  )  )  ; 
org  .  rosuda  .  JRI  .  Rengine   rToStore  =  new   org  .  rosuda  .  JRI  .  Rengine  (  new   String  [  ]  {  "--no-save"  }  ,  false  ,  null  )  ; 
if  (  !  rToStore  .  waitForR  (  )  )  { 
throw   new   ExtensionException  (  "Cannot load R!"  )  ; 
} 
if  (  !  rToStore  .  versionCheck  (  )  )  { 
throw   new   ExtensionException  (  "R-Extension: Verison mismatch!"  )  ; 
} 
HoldRengineX   htest  =  new   HoldRengineX  (  rToStore  )  ; 
em  .  storeObject  (  htest  )  ; 
} 
Object   obj  =  em  .  retrieveObject  (  )  ; 
rConn  =  (  org  .  nlogo  .  api  .  ExtensionManager  )  obj  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in runOnce: \n"  +  ex  )  ; 
} 
} 





public   void   load  (  org  .  nlogo  .  api  .  PrimitiveManager   primManager  )  { 
primManager  .  addPrimitive  (  "put"  ,  new   Put  (  )  )  ; 
primManager  .  addPrimitive  (  "putNamedList"  ,  new   PutNamedVector  (  )  )  ; 
primManager  .  addPrimitive  (  "putList"  ,  new   PutVector  (  )  )  ; 
primManager  .  addPrimitive  (  "putDataframe"  ,  new   PutDataframe  (  )  )  ; 
primManager  .  addPrimitive  (  "putAgent"  ,  new   PutAgent  (  )  )  ; 
primManager  .  addPrimitive  (  "putAgentDf"  ,  new   PutAgentDataFrame  (  )  )  ; 
primManager  .  addPrimitive  (  "eval"  ,  new   Eval  (  )  )  ; 
primManager  .  addPrimitive  (  "get"  ,  new   Get  (  )  )  ; 
primManager  .  addPrimitive  (  "clear"  ,  new   ClearWorkspace  (  )  )  ; 
primManager  .  addPrimitive  (  "interactiveShell"  ,  new   interactiveShell  (  )  )  ; 
primManager  .  addPrimitive  (  "messageWindow"  ,  new   messageWindow  (  )  )  ; 
primManager  .  addPrimitive  (  "startDebug"  ,  new   startDebug  (  )  )  ; 
primManager  .  addPrimitive  (  "stopDebug"  ,  new   stopDebug  (  )  )  ; 
primManager  .  addPrimitive  (  "startJRIDebug"  ,  new   startJRIDebug  (  )  )  ; 
primManager  .  addPrimitive  (  "stopJRIDebug"  ,  new   stopJRIDebug  (  )  )  ; 
} 





public   static   class   messageWindow   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  "windowConsole"  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in messageWindow: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   interactiveShell   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
Method   console_method  ; 
try  { 
Class   sys_class  =  Class  .  forName  (  "java.lang.System"  )  ; 
console_method  =  sys_class  .  getDeclaredMethod  (  "console"  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in interactiveShell: \n\n Are you running NetLogo from Console? \n If not, start it again from console! \n\n "  +  ex  )  ; 
} 
if  (  console_method  !=  null  )  { 
rConn  .  readExtensionObject  (  "interactiveShell"  ,  ""  ,  ""  )  ; 
}  else  { 
throw   new   ExtensionException  (  "Error in interactiveShell: \n No console available!. Start NetLogo from shell/console!"  )  ; 
} 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in interactiveShell: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   startDebug   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  "startDebug"  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in startDebug: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   stopDebug   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  "stopDebug"  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in stopDebug: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   startJRIDebug   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  "startJRIDebug"  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in startJRIDebug: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   stopJRIDebug   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  "stopJRIDebug"  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in stopJRIDebug: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   PutAgent   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_AGENTSET  |  Syntax  .  TYPE_AGENT  ,  Syntax  .  TYPE_STRING  |  Syntax  .  TYPE_REPEATABLE  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
AssignAgentsetorAgent  (  args  ,  "putnamedvector"  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutAgent: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   PutAgentDataFrame   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_AGENTSET  |  Syntax  .  TYPE_AGENT  ,  Syntax  .  TYPE_STRING  |  Syntax  .  TYPE_REPEATABLE  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
AssignAgentsetorAgent  (  args  ,  "putdataframe"  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutAgentDf: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   PutDataframe   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_WILDCARD  |  Syntax  .  TYPE_REPEATABLE  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   rname  =  args  [  0  ]  .  getString  (  )  ; 
Object  [  ]  input  =  new   Object  [  args  .  length  -  1  ]  ; 
LinkedHashMap  <  String  ,  Object  >  hm  =  new   LinkedHashMap  <  String  ,  Object  >  (  )  ; 
hm  .  put  (  rname  ,  null  )  ; 
hm  .  put  (  "putdataframe"  ,  null  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  -  2  ;  i  +=  2  )  { 
String   varname  =  args  [  i  +  1  ]  .  getString  (  )  ; 
input  [  i  ]  =  args  [  i  +  2  ]  .  get  (  )  ; 
hm  .  put  (  varname  ,  input  [  i  ]  )  ; 
} 
rConn  .  storeObject  (  hm  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutDataFrame: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   PutVector   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_WILDCARD  |  Syntax  .  TYPE_REPEATABLE  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   rname  =  args  [  0  ]  .  getString  (  )  ; 
Object  [  ]  input  =  new   Object  [  args  .  length  -  1  ]  ; 
LinkedHashMap  <  String  ,  Object  >  hm  =  new   LinkedHashMap  <  String  ,  Object  >  (  )  ; 
hm  .  put  (  rname  ,  null  )  ; 
hm  .  put  (  "putnamedvector"  ,  null  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  -  1  ;  i  ++  )  { 
String   varname  =  (  (  Integer  )  i  )  .  toString  (  )  ; 
input  [  i  ]  =  args  [  i  +  1  ]  .  get  (  )  ; 
hm  .  put  (  varname  ,  input  [  i  ]  )  ; 
} 
rConn  .  storeObject  (  hm  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutVector: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   PutNamedVector   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_WILDCARD  |  Syntax  .  TYPE_REPEATABLE  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   rname  =  args  [  0  ]  .  getString  (  )  ; 
Object  [  ]  input  =  new   Object  [  args  .  length  -  1  ]  ; 
LinkedHashMap  <  String  ,  Object  >  hm  =  new   LinkedHashMap  <  String  ,  Object  >  (  )  ; 
hm  .  put  (  rname  ,  null  )  ; 
if  (  args  [  1  ]  .  get  (  )  instanceof   String  )  { 
hm  .  put  (  "putnamedvector"  ,  null  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  -  2  ;  i  +=  2  )  { 
String   varname  =  args  [  i  +  1  ]  .  getString  (  )  ; 
input  [  i  ]  =  args  [  i  +  2  ]  .  get  (  )  ; 
hm  .  put  (  varname  ,  input  [  i  ]  )  ; 
} 
} 
rConn  .  storeObject  (  hm  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutNamedVector: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   Put   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  ,  Syntax  .  TYPE_WILDCARD  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   rname  =  args  [  0  ]  .  getString  (  )  ; 
Object   input  =  args  [  1  ]  .  get  (  )  ; 
LinkedHashMap  <  String  ,  Object  >  hm  =  new   LinkedHashMap  <  String  ,  Object  >  (  )  ; 
hm  .  put  (  "put"  ,  null  )  ; 
hm  .  put  (  rname  ,  input  )  ; 
rConn  .  storeObject  (  hm  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in perform. \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   Eval   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   command  =  args  [  0  ]  .  getString  (  )  ; 
rConn  .  readFromString  (  command  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in Eval: \n"  +  ex  )  ; 
} 
} 
} 




public   static   class   Get   extends   DefaultReporter  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  reporterSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  }  ,  Syntax  .  TYPE_WILDCARD  )  ; 
} 

private   Object   returnObject  (  Object   result  )  { 
if  (  result   instanceof   java  .  lang  .  String  )  { 
return  (  String  )  result  ; 
} 
if  (  result   instanceof   java  .  lang  .  Double  )  { 
return  (  Double  )  result  ; 
} 
if  (  result   instanceof   java  .  lang  .  Boolean  )  { 
return  (  Boolean  )  result  ; 
} 
if  (  result   instanceof   double  [  ]  )  { 
double  [  ]  dbarray  =  (  double  [  ]  )  result  ; 
if  (  dbarray  .  length  <  2  )  { 
return   dbarray  [  0  ]  ; 
} 
org  .  nlogo  .  api  .  LogoList   llist  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
for  (  int   i  =  0  ;  i  <  dbarray  .  length  ;  i  ++  )  { 
llist  .  add  (  (  Double  )  dbarray  [  i  ]  )  ; 
} 
return   llist  ; 
} 
if  (  result   instanceof   int  [  ]  )  { 
int  [  ]  intarray  =  (  int  [  ]  )  result  ; 
if  (  intarray  .  length  <  2  )  { 
return  (  Double  )  (  (  Integer  )  intarray  [  0  ]  )  .  doubleValue  (  )  ; 
} 
org  .  nlogo  .  api  .  LogoList   llist  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
for  (  int   i  =  0  ;  i  <  intarray  .  length  ;  i  ++  )  { 
llist  .  add  (  new   Double  (  (  (  Integer  )  intarray  [  i  ]  )  .  doubleValue  (  )  )  )  ; 
} 
return   llist  ; 
} 
if  (  result   instanceof   String  [  ]  )  { 
String  [  ]  strarray  =  (  String  [  ]  )  result  ; 
if  (  strarray  .  length  <  2  )  { 
return  (  String  )  strarray  [  0  ]  ; 
} 
org  .  nlogo  .  api  .  LogoList   llist  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
for  (  int   i  =  0  ;  i  <  strarray  .  length  ;  i  ++  )  { 
llist  .  add  (  (  String  )  strarray  [  i  ]  )  ; 
} 
return   llist  ; 
} 
if  (  result   instanceof   boolean  [  ]  )  { 
Boolean  [  ]  boolarray  =  (  Boolean  [  ]  )  result  ; 
if  (  boolarray  .  length  <  2  )  { 
return  (  (  Boolean  )  boolarray  [  0  ]  )  .  booleanValue  (  )  ; 
} 
org  .  nlogo  .  api  .  LogoList   llist  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
for  (  int   i  =  0  ;  i  <  boolarray  .  length  ;  i  ++  )  { 
llist  .  add  (  (  (  Boolean  )  boolarray  [  i  ]  )  .  booleanValue  (  )  )  ; 
} 
return   llist  ; 
} 
return   null  ; 
} 

public   Object   report  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
String   command  =  args  [  0  ]  .  getString  (  )  ; 
Object   result  =  rConn  .  readFromString  (  command  )  ; 
Object   singleobj  =  returnObject  (  result  )  ; 
if  (  singleobj  !=  null  )  { 
return   singleobj  ; 
} 
if  (  result   instanceof   org  .  rosuda  .  JRI  .  RVector  )  { 
org  .  nlogo  .  api  .  LogoList   list1  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
Iterator   it  =  (  (  org  .  rosuda  .  JRI  .  RVector  )  result  )  .  listIterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Object   obj  =  it  .  next  (  )  ; 
Object   objnow  =  returnObject  (  (  (  org  .  rosuda  .  JRI  .  REXP  )  obj  )  .  getContent  (  )  )  ; 
list1  .  add  (  objnow  )  ; 
} 
return   list1  ; 
} 
if  (  result   instanceof   org  .  rosuda  .  JRI  .  RBool  )  { 
int   bool  =  0  ; 
org  .  rosuda  .  JRI  .  RBool   boolhere  =  (  org  .  rosuda  .  JRI  .  RBool  )  result  ; 
if  (  boolhere  .  isTRUE  (  )  )  { 
bool  =  1  ; 
} 
if  (  boolhere  .  isNA  (  )  )  { 
bool  =  -  1  ; 
} 
return   bool  ; 
} 
if  (  result   instanceof   org  .  rosuda  .  JRI  .  RFactor  )  { 
org  .  nlogo  .  api  .  LogoList   list1  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
org  .  rosuda  .  JRI  .  RFactor   factorhere  =  (  org  .  rosuda  .  JRI  .  RFactor  )  result  ; 
for  (  int   i  =  0  ;  i  <  factorhere  .  size  (  )  ;  i  ++  )  { 
String   fac  =  factorhere  .  at  (  i  )  ; 
list1  .  add  (  fac  )  ; 
} 
return   list1  ; 
} 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in perform. \n"  +  ex  )  ; 
} 
return   null  ; 
} 
} 




public   static   class   ClearWorkspace   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  ""  ,  ""  ,  "rm(list=ls())"  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in ClearWorkspace: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   LoadHistory   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  ""  ,  args  [  0  ]  .  getString  (  )  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in LoadHistory: \n"  +  ex  )  ; 
} 
} 
} 





public   static   class   SaveHistory   extends   DefaultCommand  { 

public   Syntax   getSyntax  (  )  { 
return   Syntax  .  commandSyntax  (  new   int  [  ]  {  Syntax  .  TYPE_STRING  }  )  ; 
} 

public   String   getAgentClassString  (  )  { 
return  "OTPL"  ; 
} 

public   void   perform  (  Argument   args  [  ]  ,  Context   context  )  throws   ExtensionException  ,  LogoException  { 
try  { 
rConn  .  readExtensionObject  (  args  [  0  ]  .  getString  (  )  ,  ""  ,  ""  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in SaveHistory: \n"  +  ex  )  ; 
} 
} 
} 





public   void   unload  (  )  throws   ExtensionException  { 
try  { 
rConn  .  readExtensionObject  (  ""  ,  ""  ,  "rm(list=ls())"  )  ; 
rConn  .  retrieveObject  (  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in unload: \n"  +  ex  )  ; 
} 
} 







private   static   void   AssignAgentsetorAgent  (  Argument   args  [  ]  ,  String   outtype  )  throws   ExtensionException  { 
try  { 
String   rname  =  args  [  0  ]  .  getString  (  )  ; 
Object   ag  =  args  [  1  ]  .  get  (  )  ; 
String  [  ]  varnames  =  new   String  [  args  .  length  -  2  ]  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  -  2  ;  i  ++  )  { 
varnames  [  i  ]  =  args  [  i  +  2  ]  .  getString  (  )  ; 
} 
LinkedHashMap  <  String  ,  Object  >  hm  =  new   LinkedHashMap  <  String  ,  Object  >  (  )  ; 
hm  .  put  (  rname  ,  null  )  ; 
hm  .  put  (  outtype  ,  null  )  ; 
if  (  ag   instanceof   org  .  nlogo  .  agent  .  AgentSet  )  { 
org  .  nlogo  .  agent  .  AgentSet   agentset  =  (  org  .  nlogo  .  agent  .  AgentSet  )  ag  ; 
for  (  int   j  =  0  ;  j  <  varnames  .  length  ;  j  ++  )  { 
org  .  nlogo  .  api  .  LogoList   varvalue  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
org  .  nlogo  .  agent  .  AgentSet  .  Iterator   it  =  agentset  .  iterator  (  )  ; 
org  .  nlogo  .  agent  .  Agent  [  ]  ags  =  new   org  .  nlogo  .  agent  .  Agent  [  agentset  .  count  (  )  ]  ; 
int   i  =  0  ; 
while  (  it  .  hasNext  (  )  )  { 
ags  [  i  ]  =  (  org  .  nlogo  .  agent  .  Agent  )  it  .  next  (  )  ; 
int   varindex  =  ags  [  i  ]  .  world  (  )  .  indexOfVariable  (  ags  [  i  ]  ,  varnames  [  j  ]  .  toUpperCase  (  )  )  ; 
if  (  ags  [  i  ]  instanceof   org  .  nlogo  .  agent  .  Turtle  )  { 
varvalue  .  add  (  ags  [  i  ]  .  getTurtleVariable  (  varindex  )  )  ; 
} 
if  (  ags  [  i  ]  instanceof   org  .  nlogo  .  agent  .  Link  )  { 
varvalue  .  add  (  ags  [  i  ]  .  getLinkVariable  (  varindex  )  )  ; 
} 
if  (  ags  [  i  ]  instanceof   org  .  nlogo  .  agent  .  Patch  )  { 
varvalue  .  add  (  ags  [  i  ]  .  getPatchVariable  (  varindex  )  )  ; 
} 
i  ++  ; 
} 
hm  .  put  (  varnames  [  j  ]  ,  varvalue  )  ; 
} 
} 
if  (  ag   instanceof   org  .  nlogo  .  agent  .  Agent  )  { 
org  .  nlogo  .  agent  .  Agent   agent  =  (  org  .  nlogo  .  agent  .  Agent  )  ag  ; 
for  (  int   j  =  0  ;  j  <  varnames  .  length  ;  j  ++  )  { 
int   varindex  =  agent  .  world  (  )  .  indexOfVariable  (  agent  ,  varnames  [  j  ]  .  toUpperCase  (  )  )  ; 
org  .  nlogo  .  api  .  LogoList   varvalue  =  new   org  .  nlogo  .  api  .  LogoList  (  )  ; 
if  (  agent   instanceof   org  .  nlogo  .  agent  .  Turtle  )  { 
varvalue  .  add  (  agent  .  getTurtleVariable  (  varindex  )  )  ; 
} 
if  (  agent   instanceof   org  .  nlogo  .  agent  .  Link  )  { 
varvalue  .  add  (  agent  .  getLinkVariable  (  varindex  )  )  ; 
} 
if  (  agent   instanceof   org  .  nlogo  .  agent  .  Patch  )  { 
varvalue  .  add  (  agent  .  getPatchVariable  (  varindex  )  )  ; 
} 
hm  .  put  (  varnames  [  j  ]  ,  varvalue  )  ; 
} 
} 
rConn  .  storeObject  (  hm  )  ; 
}  catch  (  Exception   ex  )  { 
throw   new   ExtensionException  (  "Error in PutAgentDf: \n"  +  ex  )  ; 
} 
} 
} 

