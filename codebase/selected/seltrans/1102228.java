package   org  .  ascape  .  model  ; 

import   java  .  awt  .  GraphicsEnvironment  ; 
import   java  .  awt  .  HeadlessException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ListIterator  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  zip  .  GZIPInputStream  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   org  .  ascape  .  explorer  .  ModelApplet  ; 
import   org  .  ascape  .  explorer  .  RuntimeEnvironment  ; 
import   org  .  ascape  .  explorer  .  UserEnvironment  ; 
import   org  .  ascape  .  model  .  event  .  ControlEvent  ; 
import   org  .  ascape  .  model  .  event  .  DefaultScapeListener  ; 
import   org  .  ascape  .  model  .  event  .  ScapeEvent  ; 
import   org  .  ascape  .  model  .  rule  .  NotifyViews  ; 
import   org  .  ascape  .  model  .  space  .  SpatialTemporalException  ; 
import   org  .  ascape  .  util  .  data  .  DataGroup  ; 









public   class   ModelRoot   implements   Serializable  ,  Runnable  { 

private   Scape   scape  ; 




private   DataGroup   dataGroup  ; 





private   static   transient   RuntimeEnvironment   environment  ; 




private   String   periodName  =  "Iteration"  ; 





private   String   description  ; 





private   String   HTMLDescription  ; 




private   int   startPeriod  =  0  ; 





private   boolean   startOnOpen  =  true  ; 




private   int   stopPeriod  =  Integer  .  MAX_VALUE  ; 




private   int   pausePeriod  =  Integer  .  MAX_VALUE  ; 





private   String   home  ; 




private   int   earliestPeriod  ; 




private   int   latestPeriod  =  Integer  .  MAX_VALUE  ; 

private   List   restartingViews  =  new   Vector  (  )  ; 




private   int   iteration  ; 




private   int   period  ; 




private   boolean   paused  =  false  ; 




private   boolean   running  =  false  ; 




private   boolean   step  =  false  ; 




private   boolean   closeAndOpenNewRequested  =  false  ; 




private   boolean   closeAndOpenSavedRequested  =  false  ; 




private   boolean   restartRequested  =  false  ; 




private   boolean   closeRequested  =  false  ; 




private   boolean   quitRequested  =  false  ; 




private   boolean   openRequested  =  false  ; 




private   boolean   saveRequested  =  false  ; 




private   boolean   inMainLoop  =  false  ; 

private   boolean   beginningDeserializedRun  =  false  ; 




private   boolean   autoRestart  =  true  ; 





private   static   boolean   displayGraphics  =  true  ; 




private   static   boolean   serveGraphics  =  false  ; 





private   static   boolean   muiltWinEnvironment  ; 

public   ModelRoot  (  Scape   scape  )  { 
this  .  scape  =  scape  ; 
dataGroup  =  new   DataGroup  (  )  ; 
dataGroup  .  setScape  (  scape  )  ; 
} 

protected   void   initialize  (  )  { 
setInternalRunning  (  false  )  ; 
getData  (  )  .  clear  (  )  ; 
scape  .  reseed  (  )  ; 
scape  .  execute  (  new   NotifyViews  (  ScapeEvent  .  REQUEST_SETUP  )  )  ; 
waitForViewsUpdate  (  )  ; 
setIteration  (  0  )  ; 
setPeriod  (  getStartPeriod  (  )  )  ; 
scape  .  execute  (  Scape  .  INITIALIZE_RULE  )  ; 
scape  .  execute  (  new   NotifyViews  (  ScapeEvent  .  REPORT_INITIALIZED  )  )  ; 
waitForViewsUpdate  (  )  ; 
scape  .  execute  (  Scape  .  INITIAL_RULES_RULE  )  ; 
setInternalRunning  (  true  )  ; 
} 





protected   void   runMainLoop  (  )  { 
inMainLoop  =  true  ; 
if  (  beginningDeserializedRun  )  { 
beginningDeserializedRun  =  false  ; 
saveRequested  =  false  ; 
initialize  (  )  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REPORT_DESERIALIZED  )  )  ; 
waitForViewsUpdate  (  )  ; 
scape  .  reseed  (  )  ; 
System  .  out  .  println  (  "\nNew Random Seed: "  +  scape  .  getRandomSeed  (  )  +  "\n"  )  ; 
}  else  { 
scape  .  executeOnRoot  (  Scape  .  CLEAR_STATS_RULE  )  ; 
initialize  (  )  ; 
scape  .  executeOnRoot  (  Scape  .  COLLECT_STATS_RULE  )  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REPORT_START  )  )  ; 
waitForViewsUpdate  (  )  ; 
} 
while  (  running  )  { 
if  (  scape  .  isListenersAndMembersCurrent  (  )  &&  (  !  paused  ||  step  )  )  { 
scape  .  executeOnRoot  (  Scape  .  CLEAR_STATS_RULE  )  ; 
iteration  ++  ; 
period  ++  ; 
if  (  period  ==  getPausePeriod  (  )  &&  !  paused  )  { 
pause  (  )  ; 
} 
scape  .  executeOnRoot  (  Scape  .  EXECUTE_RULES_RULE  )  ; 
scape  .  executeOnRoot  (  Scape  .  COLLECT_STATS_RULE  )  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REPORT_ITERATE  )  )  ; 
step  =  false  ; 
}  else  { 
if  (  paused  )  { 
waitForViewsUpdate  (  )  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  TICK  )  )  ; 
try  { 
Thread  .  sleep  (  100  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
}  else  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 
if  (  period  >=  getStopPeriod  (  )  )  { 
waitForViewsUpdate  (  )  ; 
if  (  !  isAutoRestart  (  )  )  { 
scape  .  respondControl  (  new   ControlEvent  (  this  ,  ControlEvent  .  REQUEST_STOP  )  )  ; 
}  else  { 
scape  .  respondControl  (  new   ControlEvent  (  this  ,  ControlEvent  .  REQUEST_RESTART  )  )  ; 
} 
} 
if  (  closeAndOpenNewRequested  )  { 
(  new   Thread  (  this  ,  "Ascape Main Execution Loop"  )  { 

public   void   run  (  )  { 
closeAndOpenNewFinally  (  scape  )  ; 
} 
}  )  .  start  (  )  ; 
closeAndOpenNewRequested  =  false  ; 
} 
if  (  closeAndOpenSavedRequested  )  { 
(  new   Thread  (  this  ,  "Ascape Main Execution Loop"  )  { 

public   void   run  (  )  { 
closeAndOpenSavedFinally  (  scape  )  ; 
} 
}  )  .  start  (  )  ; 
closeAndOpenSavedRequested  =  false  ; 
} 
if  (  saveRequested  )  { 
waitForViewsUpdate  (  )  ; 
saveChoose  (  )  ; 
saveRequested  =  false  ; 
} 
if  (  openRequested  )  { 
waitForViewsUpdate  (  )  ; 
openChoose  (  )  ; 
openRequested  =  false  ; 
} 
} 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REPORT_STOP  )  )  ; 
waitForViewsUpdate  (  )  ; 
if  (  restartRequested  )  { 
restartRequested  =  false  ; 
scape  .  respondControl  (  new   ControlEvent  (  this  ,  ControlEvent  .  REQUEST_START  )  )  ; 
} 
if  (  closeRequested  )  { 
closeFinally  (  )  ; 
closeRequested  =  false  ; 
} 
if  (  quitRequested  )  { 
quitFinally  (  )  ; 
} 
inMainLoop  =  false  ; 
} 








public   void   respondControl  (  ControlEvent   control  )  { 
switch  (  control  .  getID  (  )  )  { 
case   ControlEvent  .  REQUEST_CLOSE  : 
close  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_OPEN  : 
closeAndOpenNew  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_OPEN_SAVED  : 
closeAndOpenSaved  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_SAVE  : 
save  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_START  : 
start  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_STOP  : 
stop  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_STEP  : 
setStep  (  true  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_RESTART  : 
restart  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_PAUSE  : 
pause  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_RESUME  : 
resume  (  )  ; 
break  ; 
case   ControlEvent  .  REQUEST_QUIT  : 
quit  (  )  ; 
break  ; 
default  : 
throw   new   RuntimeException  (  "Unknown control event sent to Agent scape: "  +  control  +  " ["  +  control  .  getID  (  )  +  "]"  )  ; 
} 
} 





public   void   waitForViewsUpdate  (  )  { 
while  (  !  scape  .  isAllViewsUpdated  (  )  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
} 






public   void   closeAndOpenNew  (  )  { 
if  (  running  )  { 
closeAndOpenNewRequested  =  true  ; 
}  else  { 
closeAndOpenNewFinally  (  scape  )  ; 
} 
} 





public   static   void   closeAndOpenNewFinally  (  final   Scape   oldScape  )  { 
boolean   oldWasPaused  =  oldScape  .  isPaused  (  )  ; 
if  (  !  oldWasPaused  )  { 
oldScape  .  getModel  (  )  .  pause  (  )  ; 
} 
final   String   modelName  =  UserEnvironment  .  openDialog  (  )  ; 
if  (  !  oldWasPaused  )  { 
oldScape  .  getModel  (  )  .  resume  (  )  ; 
} 
if  (  modelName  !=  null  )  { 
oldScape  .  addView  (  new   DefaultScapeListener  (  )  { 

public   void   scapeClosing  (  ScapeEvent   scapeEvent  )  { 
open  (  modelName  )  ; 
} 
}  )  ; 
oldScape  .  getModel  (  )  .  close  (  )  ; 
} 
} 






public   void   closeAndOpenSaved  (  )  { 
if  (  running  )  { 
closeAndOpenSavedRequested  =  true  ; 
}  else  { 
(  new   Thread  (  this  )  { 

public   void   run  (  )  { 
closeAndOpenSavedFinally  (  scape  )  ; 
} 
}  )  .  start  (  )  ; 
} 
} 




public   static   void   openSavedChoose  (  )  { 
closeAndOpenSavedFinally  (  null  )  ; 
} 






public   static   void   closeAndOpenSavedFinally  (  Scape   oldScape  )  { 
boolean   exit  =  false  ; 
ModelRoot   model  =  null  ; 
if  (  oldScape  !=  null  )  { 
if  (  !  oldScape  .  isPaused  (  )  )  { 
oldScape  .  getModel  (  )  .  pause  (  )  ; 
model  =  oldScape  .  getModel  (  )  ; 
} 
} 
while  (  !  exit  )  { 
JFileChooser   chooser  =  new   JFileChooser  (  )  ; 
int   option  =  chooser  .  showOpenDialog  (  null  )  ; 
if  (  option  ==  JFileChooser  .  APPROVE_OPTION  )  { 
if  (  chooser  .  getSelectedFile  (  )  !=  null  )  { 
final   Scape   newScape  =  openSavedRun  (  chooser  .  getSelectedFile  (  )  )  ; 
if  (  newScape  !=  null  &&  oldScape  !=  null  )  { 
oldScape  .  addView  (  new   DefaultScapeListener  (  )  { 

public   void   scapeClosing  (  ScapeEvent   scapeEvent  )  { 
newScape  .  getModel  (  )  .  start  (  )  ; 
} 
}  )  ; 
oldScape  .  getModel  (  )  .  close  (  )  ; 
exit  =  true  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "You must enter a file name or cancel."  ,  "Message"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
closeAndOpenSavedFinally  (  oldScape  )  ; 
} 
}  else  { 
exit  =  true  ; 
} 
} 
} 

public   static   Scape   openSavedRun  (  File   savedRunFile  )  { 
Scape   newScape  =  null  ; 
try  { 
InputStream   is  =  new   FileInputStream  (  savedRunFile  )  ; 
newScape  =  openSavedRun  (  is  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Sorry, could not find the file you specified:\n"  +  savedRunFile  ,  "Error"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
}  catch  (  IOException   e  )  { 
String   msg  =  "Sorry, couldn't open model because a file exception occured:"  ; 
System  .  err  .  println  (  msg  )  ; 
e  .  printStackTrace  (  )  ; 
JOptionPane  .  showMessageDialog  (  null  ,  msg  +  "\n"  +  e  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
return   newScape  ; 
} 

public   static   Scape   openSavedRun  (  InputStream   is  )  throws   IOException  { 
Scape   newScape  =  null  ; 
GZIPInputStream   gis  =  new   GZIPInputStream  (  is  )  ; 
ObjectInputStream   ois  =  new   ObjectInputStream  (  gis  )  ; 
try  { 
newScape  =  (  Scape  )  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
try  { 
newScape  .  setStartPeriod  (  newScape  .  getPeriod  (  )  +  1  )  ; 
}  catch  (  SpatialTemporalException   e  )  { 
try  { 
newScape  .  setStartPeriod  (  newScape  .  getPeriod  (  )  )  ; 
}  catch  (  SpatialTemporalException   e1  )  { 
try  { 
newScape  .  setStartPeriod  (  newScape  .  getPeriod  (  )  )  ; 
}  catch  (  SpatialTemporalException   e2  )  { 
throw   new   RuntimeException  (  "Internal Error"  )  ; 
} 
} 
} 
newScape  .  getModel  (  )  .  beginningDeserializedRun  =  true  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   newScape  ; 
} 

public   static   Scape   openSavedRun  (  String   fileName  ,  String  [  ]  args  )  { 
Scape   newScape  =  openSavedRun  (  new   File  (  fileName  )  )  ; 
if  (  newScape  !=  null  )  { 
if  (  args  .  length  >  0  )  { 
newScape  .  assignParameters  (  args  ,  true  )  ; 
} 
newScape  .  getModel  (  )  .  createEnvironment  (  )  ; 
if  (  newScape  .  isPaused  (  )  &&  newScape  .  isStartOnOpen  (  )  )  { 
newScape  .  getModel  (  )  .  resume  (  )  ; 
} 
newScape  .  getModel  (  )  .  runMainLoop  (  )  ; 
} 
return   newScape  ; 
} 

public   void   createNewModel  (  )  { 
createNewModel  (  null  ,  new   String  [  0  ]  )  ; 
} 

public   void   createNewModel  (  ModelApplet   applet  ,  String  [  ]  args  )  { 
try  { 
if  (  applet  !=  null  )  { 
scape  .  getUserEnvironment  (  )  .  setApplet  (  applet  )  ; 
applet  .  setScape  (  scape  )  ; 
} 
if  (  args  !=  null  )  { 
scape  .  assignParameters  (  args  ,  false  )  ; 
} 
scape  .  executeOnRoot  (  Scape  .  CREATE_RULE  )  ; 
if  (  args  !=  null  )  { 
scape  .  assignParameters  (  args  ,  true  )  ; 
} 
scape  .  executeOnRoot  (  Scape  .  CREATE_VIEW_RULE  )  ; 
if  (  scape  .  getModel  (  )  .  isStartOnOpen  (  )  )  { 
start  (  )  ; 
} 
}  catch  (  RuntimeException   e  )  { 
if  (  scape  .  getUserEnvironment  (  )  !=  null  )  { 
scape  .  getUserEnvironment  (  )  .  showErrorDialog  (  scape  ,  e  )  ; 
}  else  { 
throw  (  e  )  ; 
} 
} 
} 





public   void   testRun  (  )  { 
try  { 
scape  .  executeOnRoot  (  Scape  .  CREATE_RULE  )  ; 
run  (  )  ; 
}  catch  (  RuntimeException   e  )  { 
if  (  scape  .  getUserEnvironment  (  )  !=  null  )  { 
scape  .  getUserEnvironment  (  )  .  showErrorDialog  (  scape  ,  e  )  ; 
}  else  { 
throw  (  e  )  ; 
} 
} 
} 





public   void   save  (  )  { 
if  (  scape  .  isRoot  (  )  )  { 
saveRequested  =  true  ; 
}  else  { 
save  (  )  ; 
} 
} 

















public   void   run  (  )  { 
run  (  false  )  ; 
} 












public   void   run  (  boolean   singlethread  )  { 
if  (  (  scape  .  getUserEnvironment  (  )  !=  null  )  &&  (  scape  .  getUserEnvironment  (  )  .  getRuntimeMode  (  )  ==  UserEnvironment  .  RELEASE_RUNTIME_MODE  )  )  { 
try  { 
runMainLoop  (  )  ; 
}  catch  (  RuntimeException   e  )  { 
scape  .  getUserEnvironment  (  )  .  showErrorDialog  (  scape  ,  e  )  ; 
} 
}  else  { 
runMainLoop  (  )  ; 
} 
} 







public   void   start  (  )  { 
if  (  !  isRunning  (  )  )  { 
(  new   Thread  (  this  ,  "Ascape Main Execution Loop"  )  )  .  start  (  )  ; 
}  else  { 
System  .  out  .  println  (  "Warning: Tried to start an already running scape."  )  ; 
} 
} 







public   void   stop  (  )  { 
setInternalRunning  (  false  )  ; 
} 






public   void   pause  (  )  { 
setPaused  (  true  )  ; 
} 






public   void   resume  (  )  { 
setPaused  (  false  )  ; 
} 




public   void   requestRestart  (  )  { 
restartRequested  =  true  ; 
} 






public   void   restart  (  )  { 
if  (  running  )  { 
stop  (  )  ; 
restartRequested  =  true  ; 
}  else  { 
start  (  )  ; 
} 
} 





private   void   openImplementation  (  ModelApplet   applet  ,  String  [  ]  args  ,  boolean   block  )  { 
try  { 
if  (  applet  !=  null  )  { 
scape  .  getUserEnvironment  (  )  .  setApplet  (  applet  )  ; 
applet  .  setScape  (  scape  )  ; 
} 
if  (  args  !=  null  )  { 
scape  .  assignParameters  (  args  ,  false  )  ; 
} 
createEnvironment  (  )  ; 
scape  .  executeOnRoot  (  Scape  .  CREATE_RULE  )  ; 
if  (  args  !=  null  )  { 
scape  .  assignParameters  (  args  ,  true  )  ; 
} 
if  (  scape  .  getRuntimeEnvironment  (  )  !=  null  )  { 
scape  .  addView  (  scape  .  getRuntimeEnvironment  (  )  )  ; 
} 
scape  .  executeOnRoot  (  Scape  .  CREATE_VIEW_RULE  )  ; 
if  (  scape  .  getModel  (  )  .  isStartOnOpen  (  )  )  { 
if  (  !  block  )  { 
start  (  )  ; 
}  else  { 
run  (  )  ; 
} 
} 
}  catch  (  RuntimeException   e  )  { 
if  (  scape  .  getUserEnvironment  (  )  !=  null  )  { 
scape  .  getUserEnvironment  (  )  .  showErrorDialog  (  scape  ,  e  )  ; 
}  else  { 
throw  (  e  )  ; 
} 
} 
} 











public   void   open  (  ModelApplet   applet  ,  String  [  ]  args  ,  boolean   block  )  { 
openImplementation  (  applet  ,  args  ,  block  )  ; 
} 









public   void   open  (  ModelApplet   applet  ,  String  [  ]  args  )  { 
openImplementation  (  applet  ,  args  ,  false  )  ; 
} 









public   void   open  (  String  [  ]  args  ,  boolean   block  )  { 
openImplementation  (  null  ,  args  ,  block  )  ; 
} 







public   void   open  (  String  [  ]  args  )  { 
openImplementation  (  null  ,  args  ,  false  )  ; 
} 




public   void   open  (  )  { 
openImplementation  (  null  ,  null  ,  false  )  ; 
} 







public   void   open  (  boolean   block  )  { 
openImplementation  (  null  ,  null  ,  block  )  ; 
} 















public   static   Scape   open  (  String   modelName  ,  ModelApplet   applet  ,  String  [  ]  args  ,  boolean   block  )  { 
try  { 
Class   c  =  Class  .  forName  (  modelName  )  ; 
try  { 
Scape   scape  =  (  Scape  )  c  .  newInstance  (  )  ; 
scape  .  setModel  (  new   ModelRoot  (  scape  )  )  ; 
scape  .  getModel  (  )  .  open  (  applet  ,  args  ,  block  )  ; 
return   scape  ; 
}  catch  (  InstantiationException   e  )  { 
throw   new   RuntimeException  (  "An internal Ascape or vm error ocurred: "  +  e  .  getMessage  (  )  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
throw   new   RuntimeException  (  "An internal Ascape or vm error ocurred: "  +  e  .  getMessage  (  )  )  ; 
} 
}  catch  (  ClassNotFoundException   e  )  { 
throw   new   RuntimeException  (  "The class \""  +  modelName  +  " could not be found."  )  ; 
} 
} 










public   static   Scape   open  (  String   modelName  ,  String  [  ]  args  )  { 
return   open  (  modelName  ,  null  ,  args  ,  false  )  ; 
} 












public   static   Scape   open  (  String   modelName  ,  String  [  ]  args  ,  boolean   block  )  { 
return   open  (  modelName  ,  null  ,  args  ,  block  )  ; 
} 










public   static   Scape   open  (  String   modelName  ,  ModelApplet   applet  )  { 
return   open  (  modelName  ,  applet  ,  null  ,  false  )  ; 
} 








public   static   Scape   open  (  String   modelName  ,  boolean   block  )  { 
return   open  (  modelName  ,  null  ,  null  ,  block  )  ; 
} 








public   static   Scape   open  (  String   modelName  )  { 
return   open  (  modelName  ,  null  ,  null  ,  false  )  ; 
} 




public   static   Scape   openChoose  (  )  { 
return   openChoose  (  null  )  ; 
} 




public   static   Scape   openChoose  (  String  [  ]  args  )  { 
String   modelName  =  UserEnvironment  .  openDialog  (  )  ; 
Scape   scape  =  null  ; 
if  (  modelName  !=  null  )  { 
scape  =  open  (  modelName  ,  args  )  ; 
} 
return   scape  ; 
} 




public   void   saveChoose  (  )  { 
JFileChooser   chooser  =  null  ; 
boolean   overwrite  =  false  ; 
File   savedFile  ; 
while  (  !  overwrite  )  { 
chooser  =  new   JFileChooser  (  )  ; 
int   option  =  chooser  .  showSaveDialog  (  null  )  ; 
if  (  option  ==  JFileChooser  .  APPROVE_OPTION  )  { 
savedFile  =  chooser  .  getSelectedFile  (  )  ; 
}  else  { 
return  ; 
} 
if  (  savedFile  .  exists  (  )  )  { 
int   n  =  JOptionPane  .  showConfirmDialog  (  chooser  ,  "Warning - A file already exists by this name!\n"  +  "Do you want to overwrite it?\n"  ,  "Save Confirmation"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  n  ==  JOptionPane  .  YES_OPTION  )  { 
overwrite  =  true  ; 
}  else   if  (  n  ==  JOptionPane  .  CANCEL_OPTION  )  { 
chooser  .  cancelSelection  (  )  ; 
scape  .  getModel  (  )  .  resume  (  )  ; 
} 
}  else  { 
overwrite  =  true  ; 
} 
} 
if  (  chooser  .  getSelectedFile  (  )  !=  null  )  { 
try  { 
scape  .  save  (  chooser  .  getSelectedFile  (  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
JOptionPane  .  showMessageDialog  (  null  ,  "Sorry, couldn't save model because an input/output exception occured:\n"  +  e  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "You must enter a file name or cancel."  ,  "Message"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
saveChoose  (  )  ; 
} 
} 

public   void   close  (  )  { 
if  (  running  )  { 
stop  (  )  ; 
closeRequested  =  true  ; 
}  else  { 
closeFinally  (  )  ; 
} 
} 










public   void   closeFinally  (  )  { 
dataGroup  =  null  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REQUEST_CLOSE  )  )  ; 
waitForViewsUpdate  (  )  ; 
} 








public   void   quit  (  )  { 
if  (  inMainLoop  )  { 
quitRequested  =  true  ; 
stop  (  )  ; 
}  else   if  (  !  quitRequested  )  { 
quitFinally  (  )  ; 
} 
} 










public   void   quitFinally  (  )  { 
closeFinally  (  )  ; 
scape  .  executeOnRoot  (  new   NotifyViews  (  ScapeEvent  .  REQUEST_QUIT  )  )  ; 
waitForViewsUpdate  (  )  ; 
exit  (  )  ; 
} 





public   static   void   exit  (  )  { 
try  { 
System  .  exit  (  0  )  ; 
}  catch  (  SecurityException   e  )  { 
System  .  out  .  println  (  "Can't quit in this security context. (Scape is probably running in browser or viewer; quit or change that.)"  )  ; 
} 
} 























public   static   void   main  (  String  [  ]  args  )  { 
Scape   scape  ; 
if  (  args  .  length  >  0  &&  args  [  0  ]  .  indexOf  (  "="  )  ==  -  1  )  { 
String  [  ]  argsRem  =  new   String  [  args  .  length  -  1  ]  ; 
System  .  arraycopy  (  args  ,  1  ,  argsRem  ,  0  ,  argsRem  .  length  )  ; 
scape  =  open  (  args  [  0  ]  ,  argsRem  )  ; 
}  else  { 
String   fileName  =  null  ; 
List   argsList  =  new   LinkedList  (  Arrays  .  asList  (  args  )  )  ; 
for  (  ListIterator   li  =  argsList  .  listIterator  (  )  ;  li  .  hasNext  (  )  ;  )  { 
String   arg  =  (  String  )  li  .  next  (  )  ; 
int   equalAt  =  arg  .  lastIndexOf  (  "="  )  ; 
if  (  equalAt  <  1  )  { 
System  .  err  .  println  (  "Syntax error in command line: "  +  arg  )  ; 
}  else  { 
String   paramName  =  arg  .  substring  (  0  ,  equalAt  )  ; 
if  (  paramName  .  equalsIgnoreCase  (  "SavedRun"  )  )  { 
fileName  =  arg  .  substring  (  equalAt  +  1  )  ; 
li  .  remove  (  )  ; 
} 
} 
} 
if  (  fileName  !=  null  )  { 
scape  =  openSavedRun  (  fileName  ,  (  String  [  ]  )  argsList  .  toArray  (  new   String  [  0  ]  )  )  ; 
}  else  { 
environment  =  new   UserEnvironment  (  )  ; 
UserEnvironment  .  checkForLicenseAgreement  (  )  ; 
scape  =  openChoose  (  args  )  ; 
if  (  scape  !=  null  )  { 
scape  .  getModel  (  )  .  createEnvironment  (  )  ; 
}  else  { 
System  .  exit  (  0  )  ; 
} 
} 
} 
} 

public   static   void   createEnvironment  (  )  { 
if  (  environment  ==  null  )  { 
environment  =  UserEnvironment  .  getDefaultEnvironment  (  )  ; 
if  (  isDisplayGraphics  (  )  )  { 
if  (  !  isMultiWinEnvironment  (  )  )  { 
if  (  !  (  environment   instanceof   UserEnvironment  )  )  { 
environment  =  new   UserEnvironment  (  )  ; 
} 
} 
}  else  { 
environment  =  new   RuntimeEnvironment  (  )  ; 
} 
} 
} 

public   static   boolean   isDisplayGraphics  (  )  { 
try  { 
return   displayGraphics  &&  !  GraphicsEnvironment  .  isHeadless  (  )  ; 
}  catch  (  HeadlessException   e  )  { 
return   false  ; 
} 
} 

public   static   void   setDisplayGraphics  (  boolean   displayGraphics  )  { 
ModelRoot  .  displayGraphics  =  displayGraphics  ; 
} 

public   static   boolean   isServeGraphics  (  )  { 
return   serveGraphics  ; 
} 

public   static   void   setServeGraphics  (  boolean   serveGraphics  )  { 
ModelRoot  .  serveGraphics  =  serveGraphics  ; 
} 

public   static   boolean   isMultiWinEnvironment  (  )  { 
return   muiltWinEnvironment  ; 
} 

public   static   void   setMultiWinEnvironment  (  boolean   muiltWinEnvironment  )  { 
ModelRoot  .  muiltWinEnvironment  =  muiltWinEnvironment  ; 
} 






public   String   getPeriodName  (  )  { 
return   periodName  ; 
} 







public   void   setPeriodName  (  String   periodName  )  { 
this  .  periodName  =  periodName  ; 
} 






public   String   getDescription  (  )  { 
return   description  ; 
} 







public   void   setDescription  (  String   description  )  { 
this  .  description  =  description  ; 
} 







public   String   getHTMLDescription  (  )  { 
return   HTMLDescription  ; 
} 







public   void   setHTMLDescription  (  String   HTMLdescription  )  { 
this  .  HTMLDescription  =  HTMLdescription  ; 
} 






public   int   getStartPeriod  (  )  { 
return   startPeriod  ; 
} 






public   boolean   isStartOnOpen  (  )  { 
return   startOnOpen  ; 
} 







public   void   setStartOnOpen  (  boolean   startOnOpen  )  { 
this  .  startOnOpen  =  startOnOpen  ; 
} 










public   void   setStartPeriod  (  int   startPeriod  )  throws   SpatialTemporalException  { 
if  (  startPeriod  >=  earliestPeriod  )  { 
this  .  startPeriod  =  startPeriod  ; 
}  else  { 
throw   new   SpatialTemporalException  (  "Tried to set start period before earliest period"  )  ; 
} 
} 







public   int   getStopPeriod  (  )  { 
return   stopPeriod  ; 
} 












public   void   setStopPeriod  (  int   stopPeriod  )  throws   SpatialTemporalException  { 
if  (  stopPeriod  <=  latestPeriod  )  { 
this  .  stopPeriod  =  stopPeriod  ; 
}  else  { 
throw   new   SpatialTemporalException  (  "Tried to set stop period after latest period"  )  ; 
} 
} 






public   int   getPausePeriod  (  )  { 
return   pausePeriod  ; 
} 







public   void   setPausePeriod  (  int   pausePeriod  )  { 
this  .  pausePeriod  =  pausePeriod  ; 
} 






public   int   getEarliestPeriod  (  )  { 
return   earliestPeriod  ; 
} 








public   void   setEarliestPeriod  (  int   earliestPeriod  )  { 
this  .  earliestPeriod  =  earliestPeriod  ; 
if  (  startPeriod  <  earliestPeriod  )  { 
try  { 
setStartPeriod  (  earliestPeriod  )  ; 
}  catch  (  SpatialTemporalException   e  )  { 
throw   new   RuntimeException  (  "Internal Logic Error"  )  ; 
} 
} 
} 






public   int   getLatestPeriod  (  )  { 
return   latestPeriod  ; 
} 








public   void   setLatestPeriod  (  int   latestPeriod  )  { 
this  .  latestPeriod  =  latestPeriod  ; 
if  (  stopPeriod  >  latestPeriod  )  { 
try  { 
setStopPeriod  (  latestPeriod  )  ; 
}  catch  (  SpatialTemporalException   e  )  { 
throw   new   RuntimeException  (  "Internal Logic Error"  )  ; 
} 
} 
} 






public   List   getRestartingViews  (  )  { 
return   restartingViews  ; 
} 







public   void   setRestartingViews  (  List   restartingViews  )  { 
this  .  restartingViews  =  restartingViews  ; 
} 






public   boolean   isAutoRestart  (  )  { 
return   autoRestart  ; 
} 







public   void   setAutoRestart  (  boolean   autoRestart  )  { 
this  .  autoRestart  =  autoRestart  ; 
} 








public   boolean   isValidPeriod  (  int   period  )  { 
return  (  (  period  >=  earliestPeriod  )  &&  (  period  <=  latestPeriod  )  )  ; 
} 










public   String   getHome  (  )  { 
if  (  home  ==  null  )  { 
home  =  "./"  ; 
try  { 
home  =  System  .  getProperty  (  "ascape.home"  ,  home  )  ; 
}  catch  (  SecurityException   e  )  { 
} 
} 
return   home  ; 
} 









public   void   setHome  (  String   home  )  { 
this  .  home  =  home  ; 
} 

public   static   RuntimeEnvironment   getEnvironment  (  )  { 
return   environment  ; 
} 

public   boolean   isBeginningDeserializedRun  (  )  { 
return   beginningDeserializedRun  ; 
} 

public   void   setBeginningDeserializedRun  (  boolean   beginningDeserializedRun  )  { 
this  .  beginningDeserializedRun  =  beginningDeserializedRun  ; 
} 

public   boolean   isCloseAndOpenNewRequested  (  )  { 
return   closeAndOpenNewRequested  ; 
} 

public   void   setCloseAndOpenNewRequested  (  boolean   closeAndOpenNewRequested  )  { 
this  .  closeAndOpenNewRequested  =  closeAndOpenNewRequested  ; 
} 

public   boolean   isCloseAndOpenSavedRequested  (  )  { 
return   closeAndOpenSavedRequested  ; 
} 

public   void   setCloseAndOpenSavedRequested  (  boolean   closeAndOpenSavedRequested  )  { 
this  .  closeAndOpenSavedRequested  =  closeAndOpenSavedRequested  ; 
} 

public   boolean   isCloseRequested  (  )  { 
return   closeRequested  ; 
} 

public   void   setCloseRequested  (  boolean   closeRequested  )  { 
this  .  closeRequested  =  closeRequested  ; 
} 

public   boolean   isInMainLoop  (  )  { 
return   inMainLoop  ; 
} 

public   void   setInMainLoop  (  boolean   inMainLoop  )  { 
this  .  inMainLoop  =  inMainLoop  ; 
} 

public   int   getIteration  (  )  { 
return   iteration  ; 
} 

public   void   setIteration  (  int   iteration  )  { 
this  .  iteration  =  iteration  ; 
} 

public   boolean   isOpenRequested  (  )  { 
return   openRequested  ; 
} 

public   void   setOpenRequested  (  boolean   openRequested  )  { 
this  .  openRequested  =  openRequested  ; 
} 

public   boolean   isPaused  (  )  { 
return   paused  ; 
} 

public   void   setPaused  (  boolean   paused  )  { 
this  .  paused  =  paused  ; 
} 

public   int   getPeriod  (  )  { 
return   period  ; 
} 

public   void   setPeriod  (  int   period  )  { 
this  .  period  =  period  ; 
} 

public   boolean   isQuitRequested  (  )  { 
return   quitRequested  ; 
} 

public   void   setQuitRequested  (  boolean   quitRequested  )  { 
this  .  quitRequested  =  quitRequested  ; 
} 

public   boolean   isRestartRequested  (  )  { 
return   restartRequested  ; 
} 

public   void   setRestartRequested  (  boolean   restartRequested  )  { 
this  .  restartRequested  =  restartRequested  ; 
} 

public   boolean   isRunning  (  )  { 
return   running  ; 
} 

public   void   setRunning  (  boolean   running  )  { 
if  (  running  )  { 
start  (  )  ; 
}  else  { 
stop  (  )  ; 
} 
} 

public   void   setInternalRunning  (  boolean   running  )  { 
this  .  running  =  running  ; 
} 

public   boolean   isSaveRequested  (  )  { 
return   saveRequested  ; 
} 

public   void   setSaveRequested  (  boolean   saveRequested  )  { 
this  .  saveRequested  =  saveRequested  ; 
} 

public   boolean   isStep  (  )  { 
return   step  ; 
} 

public   void   setStep  (  boolean   step  )  { 
this  .  step  =  step  ; 
} 

public   DataGroup   getData  (  )  { 
return   dataGroup  ; 
} 

public   void   setScape  (  Scape   scape  )  { 
this  .  scape  =  scape  ; 
} 
} 

