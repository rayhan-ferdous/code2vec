package   verinec  .  gui  ; 

import   java  .  awt  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  logging  .  *  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  border  .  *  ; 
import   javax  .  swing  .  event  .  ChangeEvent  ; 
import   javax  .  swing  .  event  .  ChangeListener  ; 
import   org  .  jdom  .  Attribute  ; 
import   org  .  jdom  .  Document  ; 
import   org  .  jdom  .  Element  ; 
import   org  .  jdom  .  input  .  SAXBuilder  ; 
import   org  .  jdom  .  output  .  Format  ; 
import   org  .  jdom  .  output  .  XMLOutputter  ; 
import   clixml  .  JDomComparator  ; 
import   verinec  .  VerinecException  ; 
import   verinec  .  gui  .  core  .  *  ; 
import   verinec  .  data  .  repository  .  *  ; 
import   verinec  .  util  .  *  ; 






























public   class   VerinecStudio   extends   JFrame   implements   IConfigurable  { 

private   static   VerinecStudio   _instance  =  null  ; 

private   JMenuBar   menuBar  ; 

private   JMenu   menuNetwork  ; 

private   JMenu   menuPreferences  ; 

private   JMenu   menuAbout  ; 

private   JComboBox   moduleSelector  ; 

private   Action   moduleSelectAction  ; 

private   Action   newAction  ; 

private   Action   loadAction  ; 

private   Action   saveAction  ; 

private   Action   saveAsAction  ; 

private   StandardToolbar   stdToolbar  ; 

private   JPanel   toolbarPanel  ; 

private   JLabel   lblStatus  ; 

private   Logger   logger  =  Logger  .  getLogger  (  getClass  (  )  .  getName  (  )  )  ; 





public   DrawPanel   drawPanel  ; 







private   InfoSplitPane   infoSplitPane  ; 




private   JScrollPane   drawScrollPanel  ; 




private   JSplitPane   analyserSplitPane  ; 




private   IVerinecModule   currentModule  ; 





private   boolean   inTransition  =  true  ; 


private   Document   config  ; 


private   boolean   modifyAllowed  ; 





private   HashSet   nwComponentChangeListeners  ; 


private   Action   escKey  ; 


private   StudioRepository   currentState  ; 


private   IVerinecRepository   repo  ; 

private   Hashtable   nameToNode  ; 





private   static   final   File   storage  =  new   File  (  "./data"  )  ; 

private   final   String   title  =  "VeriNeC Studio"  ; 




private   static   final   String   LOAD_ICON  =  "/res/pictures/fileopen.png"  ; 




private   static   final   String   NEW_ICON  =  "/res/pictures/filenew.png"  ; 




private   static   final   String   SAVE_ICON  =  "/res/pictures/filesave.png"  ; 




private   static   final   String   SAVEAS_ICON  =  "/res/pictures/filesaveas.png"  ; 





private   static   final   String   CONFIGURE_ICON  =  "/res/pictures/configure.png"  ; 

private   static   final   String   CLOSE_ICON  =  "/res/pictures/close.png"  ; 





private   static   final   String   HELP_ICON  =  "/res/pictures/help.png"  ; 




private   static   final   String   configName  =  "verinec.gui.modules"  ; 















private   VerinecStudio  (  )  throws   VerinecException  { 
super  (  )  ; 
if  (  !  storage  .  exists  (  )  )  if  (  !  storage  .  mkdir  (  )  )  System  .  err  .  println  (  "Could not create data dir (this is bad)"  )  ; 
setTitle  (  title  )  ; 
NetworkTypes  .  initialize  (  this  )  ; 
newAction  =  new   NewAction  (  )  ; 
loadAction  =  new   LoadAction  (  )  ; 
saveAction  =  new   SaveAction  (  )  ; 
saveAsAction  =  new   SaveAsAction  (  )  ; 
menuBar  =  new   JMenuBar  (  )  ; 
menuNetwork  =  new   JMenu  (  "VeriNeC"  )  ; 
menuNetwork  .  add  (  newAction  )  ; 
menuNetwork  .  add  (  loadAction  )  ; 
menuNetwork  .  add  (  saveAction  )  ; 
menuNetwork  .  add  (  saveAsAction  )  ; 
menuNetwork  .  add  (  new   ExitAction  (  )  )  ; 
menuPreferences  =  new   JMenu  (  "Preferences"  )  ; 
menuPreferences  .  add  (  new   OptionsAction  (  )  )  ; 
menuAbout  =  new   JMenu  (  "Help"  )  ; 
JMenuItem   about  =  new   JMenuItem  (  new   AboutAction  (  )  )  ; 
menuAbout  .  add  (  about  )  ; 
escKey  =  new   EscKeyAction  (  )  ; 
config  =  getConfig  (  )  ; 
moduleSelector  =  new   JComboBox  (  )  ; 
resetModules  (  )  ; 
moduleSelectAction  =  new   ModuleSelectAction  (  )  ; 
moduleSelector  .  setAction  (  moduleSelectAction  )  ; 
toolbarPanel  =  new   JPanel  (  )  ; 
toolbarPanel  .  setLayout  (  new   FlowLayout  (  FlowLayout  .  LEFT  )  )  ; 
stdToolbar  =  new   StandardToolbar  (  )  ; 
stdToolbar  .  addSeparator  (  )  ; 
stdToolbar  .  add  (  moduleSelector  )  ; 
stdToolbar  .  addSeparator  (  new   Dimension  (  20  ,  20  )  )  ; 
stdToolbar  .  add  (  newAction  )  ; 
stdToolbar  .  add  (  loadAction  )  ; 
stdToolbar  .  add  (  saveAction  )  ; 
stdToolbar  .  add  (  saveAsAction  )  ; 
lblStatus  =  new   JLabel  (  "New Project"  )  ; 
Border   statusBorder  =  new   CompoundBorder  (  BorderFactory  .  createBevelBorder  (  BevelBorder  .  LOWERED  )  ,  BorderFactory  .  createEmptyBorder  (  2  ,  5  ,  2  ,  5  )  )  ; 
lblStatus  .  setBorder  (  statusBorder  )  ; 
infoSplitPane  =  new   InfoSplitPane  (  this  )  ; 
drawPanel  =  new   DrawPanel  (  this  ,  stdToolbar  )  ; 
drawScrollPanel  =  new   JScrollPane  (  drawPanel  )  ; 
drawScrollPanel  .  getVerticalScrollBar  (  )  .  setUnitIncrement  (  8  )  ; 
analyserSplitPane  =  new   JSplitPane  (  JSplitPane  .  HORIZONTAL_SPLIT  ,  drawScrollPanel  ,  infoSplitPane  )  ; 
analyserSplitPane  .  setResizeWeight  (  0.9  )  ; 
this  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
this  .  getContentPane  (  )  .  add  (  toolbarPanel  ,  BorderLayout  .  NORTH  )  ; 
this  .  getContentPane  (  )  .  add  (  analyserSplitPane  )  ; 
this  .  getContentPane  (  )  .  add  (  lblStatus  ,  BorderLayout  .  SOUTH  )  ; 
this  .  addWindowListener  (  new   WindowAdapter  (  )  { 

public   void   windowClosing  (  WindowEvent   e  )  { 
exit  (  )  ; 
} 
}  )  ; 
this  .  setDefaultCloseOperation  (  JFrame  .  DO_NOTHING_ON_CLOSE  )  ; 
this  .  setSize  (  Toolkit  .  getDefaultToolkit  (  )  .  getScreenSize  (  )  )  ; 
cleanUp  (  )  ; 
setDefaultModule  (  )  ; 
this  .  pack  (  )  ; 
} 










public   static   VerinecStudio   getInstance  (  )  throws   VerinecException  { 
if  (  _instance  ==  null  )  { 
_instance  =  new   VerinecStudio  (  )  ; 
} 
return   _instance  ; 
} 




private   void   initializeGui  (  )  { 
nwComponentChangeListeners  =  new   HashSet  (  )  ; 
this  .  getRootPane  (  )  .  getInputMap  (  JComponent  .  WHEN_ANCESTOR_OF_FOCUSED_COMPONENT  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ESCAPE"  )  ,  "escKey"  )  ; 
this  .  getRootPane  (  )  .  getActionMap  (  )  .  put  (  "escKey"  ,  escKey  )  ; 
modifyAllowed  =  true  ; 
toolbarPanel  .  removeAll  (  )  ; 
toolbarPanel  .  add  (  stdToolbar  )  ; 
menuBar  .  removeAll  (  )  ; 
menuBar  .  add  (  menuNetwork  )  ; 
menuBar  .  add  (  menuPreferences  )  ; 
menuBar  .  add  (  menuAbout  )  ; 
this  .  setJMenuBar  (  menuBar  )  ; 
setStatus  (  ""  )  ; 
analyserSplitPane  .  setLeftComponent  (  drawScrollPanel  )  ; 
drawPanel  .  initialize  (  )  ; 
Component  [  ]  components  =  getNetworkComponents  (  )  ; 
for  (  int   i  =  0  ;  i  <  components  .  length  ;  i  ++  )  { 
(  (  NwComponent  )  components  [  i  ]  )  .  initialize  (  )  ; 
} 
infoSplitPane  .  initialize  (  )  ; 
enableSaveFromMenu  (  )  ; 
} 


private   void   resetModules  (  )  { 
moduleSelector  .  removeAllItems  (  )  ; 
Iterator   iter  =  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Element   mod  =  (  Element  )  iter  .  next  (  )  ; 
try  { 
IVerinecModule   m  =  (  IVerinecModule  )  Class  .  forName  (  mod  .  getAttributeValue  (  "class"  )  )  .  newInstance  (  )  ; 
moduleSelector  .  addItem  (  m  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "Could not load module "  +  mod  .  getAttributeValue  (  "name"  )  +  ": "  +  t  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "resetModules"  ,  t  )  ; 
} 
} 
} 







public   void   addToolBar  (  JToolBar   bar  )  { 
toolbarPanel  .  add  (  bar  )  ; 
} 







public   void   addMenu  (  JMenu   menu  )  { 
menuBar  .  add  (  menu  )  ; 
} 








public   void   setTopComponent  (  Component   c  )  { 
infoSplitPane  .  setTopComponent  (  c  )  ; 
} 








public   void   setBottomComponent  (  Component   c  )  { 
infoSplitPane  .  setBottomComponent  (  c  )  ; 
} 





public   void   setResizeWeight  (  double   w  )  { 
infoSplitPane  .  setResizeWeight  (  w  )  ; 
} 










public   IVerinecRepository   getRepository  (  )  { 
return   currentState  ; 
} 






public   Component  [  ]  getNetworkComponents  (  )  { 
return   drawPanel  .  getComponents  (  )  ; 
} 








public   PCNode   getNodeByName  (  String   name  )  throws   VerinecException  { 
PCNode   n  =  (  PCNode  )  nameToNode  .  get  (  name  )  ; 
if  (  n  ==  null  )  throw   new   VerinecException  (  "No node with name "  +  name  +  " found"  )  ; 
return   n  ; 
} 










public   PCNode   addNode  (  int   x  ,  int   y  )  { 
PCNode   n  =  new   PCNode  (  x  ,  y  ,  this  )  ; 
try  { 
currentState  .  setNode  (  n  .  getConfig  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "Unexpected exception caught when adding node xml definition. "  +  t  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "addNode"  ,  t  )  ; 
} 
return   n  ; 
} 










public   void   addNetworkElement  (  Element   e  )  { 
currentState  .  addPhysicalNetwork  (  e  )  ; 
} 






public   void   setModifyAllowed  (  boolean   b  )  { 
modifyAllowed  =  b  ; 
} 






public   boolean   isModifyAllowed  (  )  { 
return   modifyAllowed  ; 
} 


public   void   disbleSaveFromMenu  (  )  { 
newAction  .  setEnabled  (  false  )  ; 
saveAction  .  setEnabled  (  false  )  ; 
saveAsAction  .  setEnabled  (  false  )  ; 
} 


public   void   enableSaveFromMenu  (  )  { 
newAction  .  setEnabled  (  true  )  ; 
saveAction  .  setEnabled  (  true  )  ; 
saveAsAction  .  setEnabled  (  true  )  ; 
} 


public   void   setDefaultModule  (  )  { 
setActiveModule  (  (  IVerinecModule  )  moduleSelector  .  getItemAt  (  0  )  )  ; 
} 









private   boolean   setActiveModule  (  IVerinecModule   newMod  )  { 
inTransition  =  true  ; 
if  (  currentModule  !=  null  )  currentModule  .  unload  (  )  ; 
initializeGui  (  )  ; 
if  (  newMod  !=  null  )  { 
try  { 
newMod  .  load  (  this  )  ; 
}  catch  (  Throwable   e  )  { 
setStatus  (  "Could not load new module "  +  newMod  .  toString  (  )  +  ". "  +  e  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "setActiveModule"  ,  e  )  ; 
try  { 
if  (  currentModule  !=  null  )  currentModule  .  load  (  this  )  ; 
return   false  ; 
}  catch  (  VerinecException   ex  )  { 
setStatus  (  "Man the lifeboats. Women and children first! (Could not load old module after failure with new module. Now you best restart the application.)"  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "setActiveModule"  ,  ex  )  ; 
return   false  ; 
} 
} 
setStatus  (  "Switched to module "  +  newMod  .  toString  (  )  )  ; 
} 
currentModule  =  newMod  ; 
inTransition  =  false  ; 
this  .  validate  (  )  ; 
return   true  ; 
} 






public   IVerinecModule   getActiveModule  (  )  { 
return   currentModule  ; 
} 






public   Vector   getModules  (  )  { 
Vector   v  =  new   Vector  (  moduleSelector  .  getItemCount  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  moduleSelector  .  getItemCount  (  )  ;  i  ++  )  v  .  add  (  moduleSelector  .  getItemAt  (  i  )  )  ; 
return   v  ; 
} 


private   void   cleanUp  (  )  { 
Component  [  ]  components  =  getNetworkComponents  (  )  ; 
for  (  int   i  =  0  ;  i  <  components  .  length  ;  i  ++  )  { 
try  { 
if  (  components  [  i  ]  instanceof   NwNode  )  (  (  NwComponent  )  components  [  i  ]  )  .  delete  (  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "Error during delete of a component: "  +  t  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "cleanUp"  ,  t  )  ; 
} 
} 
drawPanel  .  repaint  (  )  ; 
currentState  =  new   StudioRepository  (  "New Project"  )  ; 
repo  =  null  ; 
nameToNode  =  new   Hashtable  (  )  ; 
} 






public   boolean   isProjectModified  (  )  { 
if  (  repo  ==  null  )  { 
return  (  currentState  .  getNodes  (  )  .  getChildren  (  )  .  size  (  )  >  0  )  ; 
} 
JDomComparator   comparator  =  new   JDomComparator  (  )  ; 
try  { 
if  (  comparator  .  compare  (  repo  .  getNodes  (  )  ,  currentState  .  getNodes  (  )  )  !=  0  )  { 
return   true  ; 
}  else   if  (  comparator  .  compare  (  repo  .  getPhysicalNetworks  (  )  ,  currentState  .  getPhysicalNetworks  (  )  )  !=  0  )  { 
return   true  ; 
} 
}  catch  (  VerinecException   e  )  { 
return   true  ; 
} 
return   false  ; 
} 







private   boolean   unloadProject  (  )  { 
int   result  =  JOptionPane  .  OK_OPTION  ; 
if  (  isProjectModified  (  )  )  { 
result  =  JOptionPane  .  showConfirmDialog  (  this  ,  "Do you want to save the current Project?"  ,  "Project modified"  ,  JOptionPane  .  YES_NO_CANCEL_OPTION  )  ; 
if  (  result  ==  JOptionPane  .  OK_OPTION  )  { 
saveProject  (  )  ; 
} 
} 
if  (  result  ==  JOptionPane  .  OK_OPTION  ||  result  ==  JOptionPane  .  NO_OPTION  )  { 
IVerinecModule   mod  =  getActiveModule  (  )  ; 
setActiveModule  (  null  )  ; 
cleanUp  (  )  ; 
setActiveModule  (  mod  )  ; 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 





public   void   newProject  (  )  { 
if  (  unloadProject  (  )  )  { 
setTitle  (  title  )  ; 
setStatus  (  "New Project"  )  ; 
} 
} 






public   void   reloadProject  (  )  { 
try  { 
IVerinecRepository   theRepo  =  repo  ; 
cleanUp  (  )  ; 
if  (  theRepo  !=  null  )  { 
loadProject  (  theRepo  )  ; 
repo  =  theRepo  ; 
} 
}  catch  (  VerinecException   e  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "reloadNetwork"  ,  e  )  ; 
try  { 
setStatus  (  "Could not load project "  +  repo  .  getProjectName  (  )  +  ": "  +  e  .  getMessage  (  )  )  ; 
}  catch  (  VerinecException   ex  )  { 
} 
} 
} 








public   void   loadProject  (  )  { 
String  [  ]  repNames  ; 
try  { 
repNames  =  RepositoryFactory  .  getProjectNames  (  )  ; 
}  catch  (  VerinecException   e  )  { 
setStatus  (  "Could not get project names"  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "loadNetwork"  ,  e  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Could not get project names\n"  +  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
if  (  repNames  .  length  <  1  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  "There are no saved projects available."  ,  "No projects to open"  ,  JOptionPane  .  WARNING_MESSAGE  )  ; 
setStatus  (  "There are no saved projects available"  )  ; 
return  ; 
} 
if  (  !  unloadProject  (  )  )  { 
setStatus  (  "Load Network cancelled"  )  ; 
return  ; 
} 
Arrays  .  sort  (  repNames  )  ; 
String   choice  =  (  String  )  JOptionPane  .  showInputDialog  (  this  ,  "Choose a project:"  ,  "Available Projects"  ,  JOptionPane  .  QUESTION_MESSAGE  ,  null  ,  repNames  ,  null  )  ; 
if  (  choice  !=  null  )  { 
try  { 
setStatus  (  "Please wait..."  )  ; 
loadProject  (  RepositoryFactory  .  createRepository  (  choice  )  )  ; 
}  catch  (  VerinecException   e  )  { 
String   m  =  e  .  getMessage  (  )  ; 
if  (  e  .  getCause  (  )  !=  null  )  m  +=  "\n"  +  e  .  getCause  (  )  .  getMessage  (  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  m  +  "\nIf you have non-english letters in the path you installed the application, you will need Internet access for the XML schemas to be loaded."  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
repo  =  null  ; 
setStatus  (  "Could not load project "  +  choice  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "loadNetwork"  ,  e  )  ; 
} 
} 
} 








private   void   loadProject  (  IVerinecRepository   newRep  )  throws   VerinecException  { 
IVerinecModule   mod  =  null  ; 
try  { 
if  (  !  inTransition  )  { 
mod  =  getActiveModule  (  )  ; 
setActiveModule  (  null  )  ; 
} 
repo  =  newRep  ; 
currentState  =  new   StudioRepository  (  repo  .  getProjectName  (  )  )  ; 
currentState  .  setGlobals  (  repo  .  getGlobals  (  )  )  ; 
currentState  .  setNodes  (  repo  .  getNodes  (  )  )  ; 
createNodes  (  )  ; 
currentState  .  setPhysicalNetworks  (  repo  .  getPhysicalNetworks  (  )  )  ; 
createHubs  (  )  ; 
}  finally  { 
if  (  !  inTransition  )  setActiveModule  (  mod  )  ; 
} 
setTitle  (  title  +  ": "  +  repo  .  getProjectName  (  )  )  ; 
setStatus  (  "Project "  +  repo  .  getProjectName  (  )  +  " loaded."  )  ; 
} 


private   void   createNodes  (  )  { 
Iterator   iter  =  currentState  .  getNodes  (  )  .  getChildren  (  "node"  ,  VerinecNamespaces  .  NS_NODE  )  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Element   currentNode  =  (  Element  )  iter  .  next  (  )  ; 
PCNode   n  =  new   PCNode  (  currentNode  ,  this  )  ; 
nameToNode  .  put  (  n  .  getName  (  )  ,  n  )  ; 
} 
} 


private   void   createHubs  (  )  { 
Component  [  ]  components  =  getNetworkComponents  (  )  ; 
Iterator   hubs  =  currentState  .  getPhysicalNetworks  (  )  .  getChildren  (  "physical_network"  ,  VerinecNamespaces  .  NS_NETWORK  )  .  iterator  (  )  ; 
try  { 
while  (  hubs  .  hasNext  (  )  )  { 
Element   currentNetwork  =  (  Element  )  hubs  .  next  (  )  ; 
Element   child  =  currentNetwork  .  getChild  (  "connected"  ,  VerinecNamespaces  .  NS_NETWORK  )  ; 
String   idref  =  child  .  getAttributeValue  (  "binding"  )  ; 
for  (  int   k  =  0  ;  k  <  components  .  length  ;  k  ++  )  { 
if  (  components  [  k  ]  instanceof   NwBinding  )  { 
NwBinding   aBinding  =  (  NwBinding  )  components  [  k  ]  ; 
if  (  aBinding  .  getId  (  )  .  equals  (  idref  )  )  { 
new   NwHub  (  currentNetwork  ,  this  )  ; 
} 
} 
} 
} 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "Misteriously, an exception occured while creating the network connections."  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "createHubs"  ,  t  )  ; 
} 
} 





public   void   saveProject  (  )  { 
setStatus  (  "Please wait..."  )  ; 
if  (  repo  ==  null  )  { 
saveProjectAs  (  )  ; 
}  else  { 
try  { 
repo  .  setGlobals  (  currentState  .  getGlobals  (  )  )  ; 
repo  .  setNodes  (  currentState  .  getNodes  (  )  )  ; 
repo  .  setPhysicalNetworks  (  currentState  .  getPhysicalNetworks  (  )  )  ; 
setStatus  (  "Saved project "  +  repo  .  getProjectName  (  )  )  ; 
}  catch  (  Throwable   e  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "saveNetwork"  ,  e  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
LogUtil  .  logJdom  (  logger  ,  currentState  .  getPhysicalNetworks  (  )  )  ; 
} 
} 
} 




public   void   saveProjectAs  (  )  { 
setStatus  (  "Please wait..."  )  ; 
int   answer  =  JOptionPane  .  OK_OPTION  ; 
String   projectName  ; 
String  [  ]  existingProjects  ; 
try  { 
existingProjects  =  RepositoryFactory  .  getProjectNames  (  )  ; 
}  catch  (  VerinecException   e  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "saveAsNetwork"  ,  e  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Could not get existing project names\n"  +  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
while  (  (  projectName  =  JOptionPane  .  showInputDialog  (  this  ,  "Enter Project Name:"  )  )  !=  null  )  { 
if  (  projectName  .  length  (  )  ==  0  )  continue  ; 
for  (  int   i  =  0  ;  i  <  existingProjects  .  length  ;  i  ++  )  { 
if  (  projectName  .  equals  (  existingProjects  [  i  ]  )  )  { 
answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "The project '"  +  projectName  +  "' exists already\nYes to overwrite, no to enter other name, or cancel."  )  ; 
if  (  answer  ==  JOptionPane  .  CANCEL_OPTION  )  return  ; 
logger  .  fine  (  "Erasing project to overwrite: "  +  projectName  )  ; 
try  { 
repo  =  RepositoryFactory  .  createRepository  (  projectName  )  ; 
repo  .  drop  (  )  ; 
repo  =  null  ; 
}  catch  (  Throwable   e  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "saveAsNetwork"  ,  e  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
break  ; 
} 
} 
if  (  answer  ==  JOptionPane  .  OK_OPTION  )  { 
setTitle  (  title  +  " "  +  projectName  )  ; 
try  { 
repo  =  RepositoryFactory  .  createRepository  (  projectName  )  ; 
}  catch  (  Throwable   e  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "saveAsNetwork"  ,  e  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  e  .  getMessage  (  )  ,  "Internal Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
saveProject  (  )  ; 
break  ; 
} 
} 
} 










public   void   saveConfig  (  String   modulename  ,  Document   config  )  { 
try  { 
FileOutputStream   out  =  new   FileOutputStream  (  new   File  (  storage  ,  modulename  )  )  ; 
XMLOutputter   o  =  new   XMLOutputter  (  Format  .  getPrettyFormat  (  )  )  ; 
o  .  output  (  config  ,  out  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   e  )  { 
System  .  err  .  println  (  "could not create config file!"  )  ; 
} 
} 











public   Document   loadConfig  (  String   modulename  )  { 
File   f  =  new   File  (  storage  ,  modulename  )  ; 
if  (  !  f  .  exists  (  )  )  { 
logger  .  info  (  "Config file does not exist: "  +  modulename  )  ; 
return   null  ; 
} 
try  { 
SAXBuilder   parser  =  new   SAXBuilder  (  )  ; 
return   parser  .  build  (  f  )  ; 
}  catch  (  Exception   e  )  { 
logger  .  warning  (  "Could not read config file "  +  modulename  +  ".\n"  +  e  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "loadConfig"  ,  e  )  ; 
return   null  ; 
} 
} 







public   void   setStatus  (  String   status  )  { 
if  (  status  .  length  (  )  >  0  )  logger  .  finer  (  "GUI set to status: "  +  status  )  ; 
lblStatus  .  setText  (  status  )  ; 
} 







public   void   nwComponentStateChanged  (  NwComponent   c  )  { 
if  (  c   instanceof   PCNode  )  { 
if  (  nameToNode  .  containsValue  (  c  )  )  { 
Iterator   set  =  nameToNode  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  set  !=  null  &&  set  .  hasNext  (  )  )  { 
Map  .  Entry   e  =  (  Map  .  Entry  )  set  .  next  (  )  ; 
if  (  e  .  getValue  (  )  .  equals  (  c  )  )  { 
set  .  remove  (  )  ; 
set  =  null  ; 
} 
} 
} 
nameToNode  .  put  (  (  (  PCNode  )  c  )  .  getName  (  )  ,  c  )  ; 
} 
Iterator   iter  =  nwComponentChangeListeners  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
try  { 
(  (  ChangeListener  )  iter  .  next  (  )  )  .  stateChanged  (  new   ChangeEvent  (  c  )  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "ChangeListener has thrown exception, ignored."  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "nwComponentStateChanged"  ,  t  )  ; 
} 
} 
} 









public   void   addNwComponentStateListener  (  ChangeListener   l  )  { 
nwComponentChangeListeners  .  add  (  l  )  ; 
} 







public   void   removeNwComponentStateListener  (  ChangeListener   l  )  { 
nwComponentChangeListeners  .  remove  (  l  )  ; 
} 






public   boolean   exit  (  )  { 
if  (  isProjectModified  (  )  )  { 
if  (  !  unloadProject  (  )  )  return   false  ; 
} 
dispose  (  )  ; 
System  .  exit  (  0  )  ; 
return   true  ; 
} 











public   static   void   main  (  String  [  ]  args  )  { 
try  { 
System  .  setProperty  (  "apple.laf.useScreenMenuBar"  ,  "true"  )  ; 
UIManager  .  setLookAndFeel  (  UIManager  .  getSystemLookAndFeelClassName  (  )  )  ; 
VerinecStudio  .  getInstance  (  )  .  setVisible  (  true  )  ; 
}  catch  (  Exception   e  )  { 
System  .  err  .  println  (  "A problem occured while loading Verinec Studio: "  +  e  .  getMessage  (  )  )  ; 
e  .  printStackTrace  (  )  ; 
Logger  .  global  .  severe  (  "A problem occured while loading the UI Manager: "  +  e  .  getMessage  (  )  )  ; 
Logger  .  global  .  throwing  (  "VerinecStudio"  ,  "main"  ,  e  )  ; 
return  ; 
} 
} 









private   Document   getConfig  (  )  { 
Document   newconfig  =  loadConfig  (  configName  )  ; 
if  (  newconfig  ==  null  )  { 
Element   mods  =  new   Element  (  "modules"  )  ; 
Element   mod  =  new   Element  (  "module"  )  ; 
mod  .  setAttribute  (  "name"  ,  new   verinec  .  gui  .  configurator  .  Configurator  (  )  .  toString  (  )  )  ; 
mod  .  setAttribute  (  "class"  ,  "verinec.gui.configurator.Configurator"  )  ; 
mods  .  addContent  (  mod  )  ; 
newconfig  =  new   Document  (  mods  )  ; 
} 
return   newconfig  ; 
} 






public   ConfigPanel   getConfigPanel  (  )  { 
return   new   ModulesConfig  (  this  ,  config  )  ; 
} 






public   void   saveConfiguration  (  Document   config  )  { 
this  .  config  =  config  ; 
resetModules  (  )  ; 
saveConfig  (  configName  ,  config  )  ; 
} 


class   ExitAction   extends   AbstractAction  { 




public   ExitAction  (  )  throws   VerinecException  { 
super  (  "Exit VeriNeC"  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  CLOSE_ICON  )  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_F4  ,  KeyEvent  .  ALT_DOWN_MASK  )  )  ; 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
VerinecStudio  .  this  .  exit  (  )  ; 
} 
} 




class   NewAction   extends   AbstractAction  { 




public   NewAction  (  )  throws   VerinecException  { 
super  (  "New"  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  NEW_ICON  )  )  ; 
putValue  (  SHORT_DESCRIPTION  ,  "Create new network"  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_N  ,  KeyEvent  .  CTRL_DOWN_MASK  )  )  ; 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
newProject  (  )  ; 
} 
} 




class   LoadAction   extends   AbstractAction  { 




public   LoadAction  (  )  throws   VerinecException  { 
super  (  "Open"  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  LOAD_ICON  )  )  ; 
putValue  (  SHORT_DESCRIPTION  ,  "Load a stored network"  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_O  ,  KeyEvent  .  CTRL_DOWN_MASK  )  )  ; 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
loadProject  (  )  ; 
} 
} 




class   SaveAction   extends   AbstractAction  { 




public   SaveAction  (  )  throws   VerinecException  { 
super  (  "Save"  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  SAVE_ICON  )  )  ; 
putValue  (  SHORT_DESCRIPTION  ,  "Save the network"  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_S  ,  KeyEvent  .  CTRL_DOWN_MASK  )  )  ; 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
saveProject  (  )  ; 
} 
} 




class   SaveAsAction   extends   AbstractAction  { 




public   SaveAsAction  (  )  throws   VerinecException  { 
super  (  "Save as..."  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  SAVEAS_ICON  )  )  ; 
putValue  (  SHORT_DESCRIPTION  ,  "Save the network under new name"  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_S  ,  KeyEvent  .  CTRL_DOWN_MASK  |  KeyEvent  .  SHIFT_DOWN_MASK  )  )  ; 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
saveProjectAs  (  )  ; 
} 
} 






class   OptionsAction   extends   AbstractAction  { 







public   OptionsAction  (  )  throws   VerinecException  { 
super  (  "Options..."  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  CONFIGURE_ICON  )  )  ; 
} 







public   void   actionPerformed  (  ActionEvent   e  )  { 
ConfigDialog  .  showDialog  (  VerinecStudio  .  this  )  ; 
} 
} 






class   AboutAction   extends   AbstractAction  { 






public   AboutAction  (  )  throws   VerinecException  { 
super  (  "About..."  ,  GuiUtil  .  loadIcon  (  VerinecStudio  .  this  ,  HELP_ICON  )  )  ; 
putValue  (  ACCELERATOR_KEY  ,  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_F1  ,  0  )  )  ; 
} 






public   void   actionPerformed  (  ActionEvent   e  )  { 
String   message  =  "<html><body>"  +  "<h1><font color=\"red\">VeriNeC Studio</font></h1>"  +  "<h2>Verified Network Configuration</h2>"  +  "<p>Prototype implementation of a network management system.<br>"  +  "The aim is to improve security and reliability in large, heterogenous<br>"  +  "networks, while at the same time facilitate their administration.<br>"  +  "Verinec uses a centralised database of the entire network and services <br>"  +  "configuration and validates correctness before configuring the devices.<br>"  +  "The configuration is represented in XML and abstracted from specific <br>"  +  "implementations. XML technologies are used to translate the abstracted <br>"  +  "configuration into implementation specific formats.<br>"  +  "The translated configuration is automatically distributed using existing<br>"  +  "remote administration protocols.</p>"  +  "<p><br>Dissertation by Dominik Jungo and David Buchmann</p>"  +  "<p>Contains work from our bachelor and master students: Patrick Aebischer,<br>"  +  "Geraldine Antener, Robert Awesso, Christoph Ehret, Jason Hug, Renato <br>"  +  "Loeffel, Martial Seifriz, Damian Vogel, Nadine Zurkinden.</p>"  +  "<p><br><br><font color =\"blue\">T</font>"  +  "<font color =\"green\">N</font>"  +  "<font color =\"red\">S</font> Group, University of Fribourg, 2003-2007<br>"  +  "<br>Project website: <a href=\"http://diuf.unifr.ch/tns/projects/verinec/\">diuf.unifr.ch/tns/projects/verinec/</a></p>"  +  "</body></html>"  ; 
JOptionPane  .  showMessageDialog  (  VerinecStudio  .  this  ,  message  ,  "About"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
} 
} 


class   EscKeyAction   extends   AbstractAction  { 




public   void   actionPerformed  (  ActionEvent   e  )  { 
drawPanel  .  unselectAllComponents  (  )  ; 
} 
} 






class   ModuleSelectAction   extends   AbstractAction  { 


public   ModuleSelectAction  (  )  { 
super  (  "Select Mode"  )  ; 
putValue  (  SHORT_DESCRIPTION  ,  "Select module"  )  ; 
} 







public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  getActiveModule  (  )  !=  moduleSelector  .  getSelectedItem  (  )  )  { 
if  (  !  setActiveModule  (  (  IVerinecModule  )  moduleSelector  .  getSelectedItem  (  )  )  )  { 
moduleSelector  .  setSelectedItem  (  getActiveModule  (  )  )  ; 
} 
} 
} 
} 




class   ModulesConfig   extends   ConfigPanel   implements   ActionListener  { 

private   JComboBox   cboModules  ; 

private   JTextField   txtClass  ; 

private   JButton   btnNew  ; 

private   JButton   btnUpdate  ; 

private   JButton   btnRemove  ; 

private   int   oldSelection  ; 





public   ModulesConfig  (  VerinecStudio   analyser  ,  Document   config  )  { 
super  (  "Modules"  ,  analyser  ,  config  ,  new   BorderLayout  (  )  )  ; 
JPanel   center  =  new   JPanel  (  new   BorderLayout  (  )  )  ; 
txtClass  =  new   JTextField  (  30  )  ; 
JPanel   brpfs  =  new   JPanel  (  )  ; 
brpfs  .  add  (  txtClass  )  ; 
center  .  add  (  brpfs  ,  BorderLayout  .  WEST  )  ; 
center  .  add  (  new   JLabel  (  "Class (including package name):"  )  ,  BorderLayout  .  NORTH  )  ; 
add  (  center  ,  BorderLayout  .  CENTER  )  ; 
JPanel   south  =  new   JPanel  (  new   BorderLayout  (  )  )  ; 
btnNew  =  new   JButton  (  "New"  )  ; 
btnNew  .  addActionListener  (  this  )  ; 
south  .  add  (  btnNew  ,  BorderLayout  .  WEST  )  ; 
btnUpdate  =  new   JButton  (  "Find all Modules in classpath"  )  ; 
btnUpdate  .  addActionListener  (  this  )  ; 
south  .  add  (  btnUpdate  ,  BorderLayout  .  CENTER  )  ; 
btnRemove  =  new   JButton  (  "Remove"  )  ; 
btnRemove  .  addActionListener  (  this  )  ; 
south  .  add  (  btnRemove  ,  BorderLayout  .  EAST  )  ; 
add  (  south  ,  BorderLayout  .  SOUTH  )  ; 
cboModules  =  new   JComboBox  (  )  ; 
reset  (  )  ; 
JPanel   west  =  new   JPanel  (  )  ; 
west  .  add  (  cboModules  )  ; 
add  (  west  ,  BorderLayout  .  WEST  )  ; 
} 


public   void   updateConfig  (  )  { 
try  { 
if  (  oldSelection  <  0  )  return  ; 
Element   mod  =  createModuleElement  (  txtClass  .  getText  (  )  )  ; 
Element   oldMod  =  (  Element  )  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  .  get  (  oldSelection  )  ; 
if  (  !  (  oldMod  .  getAttributeValue  (  "name"  )  .  equals  (  mod  .  getAttributeValue  (  "name"  )  )  &&  oldMod  .  getAttributeValue  (  "class"  )  .  equals  (  mod  .  getAttributeValue  (  "class"  )  )  )  )  { 
oldMod  .  setAttribute  (  (  Attribute  )  mod  .  getAttribute  (  "name"  )  .  clone  (  )  )  ; 
oldMod  .  setAttribute  (  (  Attribute  )  mod  .  getAttribute  (  "class"  )  .  clone  (  )  )  ; 
int   pos  =  cboModules  .  getSelectedIndex  (  )  ; 
cboModules  .  removeItemAt  (  oldSelection  )  ; 
cboModules  .  insertItemAt  (  mod  .  getAttributeValue  (  "name"  )  ,  oldSelection  )  ; 
cboModules  .  setSelectedIndex  (  pos  )  ; 
} 
}  catch  (  Throwable   t  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "updateConfig"  ,  t  )  ; 
JOptionPane  .  showMessageDialog  (  VerinecStudio  .  this  ,  "Could not load specified module class.\nSetting not changed.\n"  +  t  .  getMessage  (  )  )  ; 
} 
} 


public   void   updateDialog  (  )  { 
cboModules  .  removeActionListener  (  this  )  ; 
cboModules  .  removeAllItems  (  )  ; 
Iterator   iter  =  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Element   mod  =  (  Element  )  iter  .  next  (  )  ; 
try  { 
IVerinecModule   m  =  (  IVerinecModule  )  Class  .  forName  (  mod  .  getAttributeValue  (  "class"  )  )  .  newInstance  (  )  ; 
cboModules  .  addItem  (  mod  .  getAttributeValue  (  "name"  )  +  (  mod  .  getAttributeValue  (  "name"  )  .  equals  (  m  .  toString  (  )  )  ?  ""  :  "("  +  m  .  toString  (  )  +  ")"  )  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  warning  (  "Could not load module "  +  mod  .  getAttributeValue  (  "name"  )  +  ": "  +  t  .  getMessage  (  )  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "updateDialog"  ,  t  )  ; 
cboModules  .  addItem  (  mod  .  getAttributeValue  (  "name"  )  +  " (invalid)"  )  ; 
} 
} 
cboModules  .  addActionListener  (  this  )  ; 
oldSelection  =  -  1  ; 
cboModules  .  setSelectedIndex  (  0  )  ; 
} 






public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  e  .  getSource  (  )  ==  btnNew  )  { 
String   classname  ; 
while  (  (  classname  =  JOptionPane  .  showInputDialog  (  VerinecStudio  .  this  ,  "Enter full class name (with package):"  )  )  !=  null  )  { 
try  { 
Element   mod  =  createModuleElement  (  classname  )  ; 
config  .  getRootElement  (  )  .  addContent  (  mod  )  ; 
cboModules  .  addItem  (  mod  .  getAttributeValue  (  "name"  )  )  ; 
cboModules  .  setSelectedIndex  (  cboModules  .  getItemCount  (  )  -  1  )  ; 
return  ; 
}  catch  (  Throwable   t  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "actionPerformed"  ,  t  )  ; 
JOptionPane  .  showMessageDialog  (  VerinecStudio  .  this  ,  "Could not load specified module class.\nPlease try again or cancel input prompt.\n"  +  t  .  getMessage  (  )  )  ; 
} 
} 
}  else   if  (  e  .  getSource  (  )  ==  btnRemove  )  { 
int   pos  =  cboModules  .  getSelectedIndex  (  )  ; 
cboModules  .  removeItemAt  (  pos  )  ; 
(  (  Element  )  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  .  get  (  pos  )  )  .  detach  (  )  ; 
cboModules  .  setSelectedIndex  (  (  pos  >  0  )  ?  pos  -  1  :  0  )  ; 
}  else   if  (  e  .  getSource  (  )  ==  cboModules  )  { 
updateConfig  (  )  ; 
int   pos  =  cboModules  .  getSelectedIndex  (  )  ; 
List   l  =  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  ; 
if  (  pos  <  l  .  size  (  )  )  { 
Element   mod  =  (  Element  )  l  .  get  (  pos  )  ; 
txtClass  .  setText  (  mod  .  getAttributeValue  (  "class"  )  )  ; 
oldSelection  =  pos  ; 
} 
}  else   if  (  e  .  getSource  (  )  ==  btnUpdate  )  { 
FindClassThread   myThread  =  new   FindClassThread  (  this  )  ; 
SearchingDialog   sd  =  new   SearchingDialog  (  (  Dialog  )  getTopLevelAncestor  (  )  ,  myThread  )  ; 
myThread  .  setDialog  (  sd  )  ; 
myThread  .  start  (  )  ; 
btnUpdate  .  setText  (  "...please wait. searching..."  )  ; 
sd  .  setVisible  (  true  )  ; 
} 
} 

private   void   updateFoundClasses  (  ArrayList   modules  )  { 
Iterator   iter  =  modules  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
boolean   found  =  false  ; 
String   modClass  =  (  (  String  )  iter  .  next  (  )  )  ; 
Iterator   iter2  =  config  .  getRootElement  (  )  .  getChildren  (  "module"  )  .  iterator  (  )  ; 
while  (  iter2  .  hasNext  (  )  )  { 
if  (  modClass  .  equals  (  (  (  Element  )  iter2  .  next  (  )  )  .  getAttributeValue  (  "class"  )  )  )  { 
found  =  true  ; 
} 
} 
if  (  !  found  )  { 
try  { 
Element   mod  =  createModuleElement  (  modClass  )  ; 
config  .  getRootElement  (  )  .  addContent  (  mod  )  ; 
cboModules  .  addItem  (  mod  .  getAttributeValue  (  "name"  )  )  ; 
cboModules  .  setSelectedIndex  (  cboModules  .  getItemCount  (  )  -  1  )  ; 
}  catch  (  Throwable   t  )  { 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "updateFoundClasses"  ,  t  )  ; 
JOptionPane  .  showMessageDialog  (  VerinecStudio  .  this  ,  "Could not load specified module class.\nPlease try again or cancel input prompt.\n"  +  t  .  getMessage  (  )  )  ; 
} 
} 
} 
btnUpdate  .  setText  (  "Find all Modules in classpath"  )  ; 
} 

private   void   resetUpdateBtn  (  )  { 
btnUpdate  .  setText  (  "Find all Modules in classpath"  )  ; 
} 









private   Element   createModuleElement  (  String   classname  )  throws   Throwable  { 
Object   o  =  Class  .  forName  (  classname  )  .  newInstance  (  )  ; 
if  (  !  (  o   instanceof   IVerinecModule  )  )  { 
throw   new   VerinecException  (  "Specified class does not implement IVerinecModule."  )  ; 
} 
IVerinecModule   m  =  (  IVerinecModule  )  o  ; 
Element   mod  =  new   Element  (  "module"  )  ; 
mod  .  setAttribute  (  "name"  ,  m  .  toString  (  )  )  ; 
mod  .  setAttribute  (  "class"  ,  classname  )  ; 
return   mod  ; 
} 
} 






class   FindClassThread   extends   Thread  { 

private   ModulesConfig   moduleC  ; 

private   boolean   search  ; 

private   SearchingDialog   sdialog  ; 







public   FindClassThread  (  ModulesConfig   moduleC  )  { 
setName  (  "FindClassThread"  )  ; 
this  .  moduleC  =  moduleC  ; 
} 

private   void   cancelSearch  (  )  { 
search  =  false  ; 
moduleC  .  resetUpdateBtn  (  )  ; 
} 

private   void   setDialog  (  SearchingDialog   sdialog  )  { 
this  .  sdialog  =  sdialog  ; 
} 











public   void   run  (  )  { 
search  =  true  ; 
try  { 
ArrayList   modules  =  InterfaceUtility  .  getClassResources  (  "verinec.gui.IVerinecModule"  )  ; 
if  (  search  )  { 
moduleC  .  updateFoundClasses  (  modules  )  ; 
} 
sdialog  .  setVisible  (  false  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 






class   SearchingDialog   extends   PleaseWaitDialog  { 

FindClassThread   thread  ; 









public   SearchingDialog  (  Dialog   parent  ,  FindClassThread   thread  )  { 
super  (  parent  ,  "Searching"  ,  "Searching, please wait..."  )  ; 
this  .  thread  =  thread  ; 
} 




protected   boolean   cancel  (  )  { 
thread  .  cancelSearch  (  )  ; 
return   true  ; 
} 
} 










class   StudioRepository   extends   VerinecRepository  { 




public   StudioRepository  (  String   name  )  { 
super  (  name  )  ; 
} 











public   String  [  ]  getProjectNames  (  )  throws   VerinecException  { 
return   RepositoryFactory  .  getProjectNames  (  )  ; 
} 






public   void   drop  (  )  throws   VerinecException  { 
newProject  (  )  ; 
} 








public   void   setNode  (  Element   node  )  throws   VerinecException  { 
super  .  setNode  (  node  )  ; 
} 





public   Element   getNodes  (  )  { 
try  { 
return   super  .  getNodes  (  )  ; 
}  catch  (  VerinecException   e  )  { 
logger  .  warning  (  "Unexpected exception, returning null"  )  ; 
logger  .  throwing  (  getClass  (  )  .  getName  (  )  ,  "getNodes"  ,  e  )  ; 
return   null  ; 
} 
} 








public   Element   getPhysicalNetworks  (  )  { 
try  { 
return   super  .  getPhysicalNetworks  (  )  ; 
}  catch  (  VerinecException   e  )  { 
throw   new   InternalError  (  "Could not get physical networks "  +  e  .  toString  (  )  )  ; 
} 
} 





public   void   addPhysicalNetwork  (  Element   e  )  { 
networks  .  getChild  (  "physical"  ,  VerinecNamespaces  .  NS_NETWORK  )  .  addContent  (  e  )  ; 
} 
} 
} 

