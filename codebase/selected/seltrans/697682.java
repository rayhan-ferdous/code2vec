package   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  views  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   org  .  eclipse  .  core  .  runtime  .  IProgressMonitor  ; 
import   org  .  eclipse  .  core  .  runtime  .  IStatus  ; 
import   org  .  eclipse  .  core  .  runtime  .  Status  ; 
import   org  .  eclipse  .  jface  .  action  .  Action  ; 
import   org  .  eclipse  .  jface  .  action  .  IAction  ; 
import   org  .  eclipse  .  jface  .  action  .  MenuManager  ; 
import   org  .  eclipse  .  jface  .  action  .  Separator  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  ErrorDialog  ; 
import   org  .  eclipse  .  jface  .  viewers  .  DoubleClickEvent  ; 
import   org  .  eclipse  .  jface  .  viewers  .  IDoubleClickListener  ; 
import   org  .  eclipse  .  jface  .  viewers  .  IStructuredSelection  ; 
import   org  .  eclipse  .  jface  .  viewers  .  TreeViewer  ; 
import   org  .  eclipse  .  swt  .  SWT  ; 
import   org  .  eclipse  .  swt  .  dnd  .  DND  ; 
import   org  .  eclipse  .  swt  .  dnd  .  DragSourceEvent  ; 
import   org  .  eclipse  .  swt  .  dnd  .  DragSourceListener  ; 
import   org  .  eclipse  .  swt  .  dnd  .  TextTransfer  ; 
import   org  .  eclipse  .  swt  .  dnd  .  Transfer  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Composite  ; 
import   org  .  eclipse  .  swt  .  widgets  .  FileDialog  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Menu  ; 
import   org  .  eclipse  .  swt  .  widgets  .  MessageBox  ; 
import   org  .  eclipse  .  ui  .  ISaveablePart  ; 
import   org  .  eclipse  .  ui  .  IWorkbenchActionConstants  ; 
import   org  .  eclipse  .  ui  .  IWorkbenchPartConstants  ; 
import   org  .  eclipse  .  ui  .  PartInitException  ; 
import   org  .  eclipse  .  ui  .  PlatformUI  ; 
import   org  .  eclipse  .  ui  .  model  .  WorkbenchLabelProvider  ; 
import   org  .  eclipse  .  ui  .  part  .  ViewPart  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  axlang  .  model  .  api  .  IAXLangElement  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  axlang  .  model  .  api  .  IAXLangListener  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  axlang  .  model  .  api  .  converter  .  IAXLangWriter  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  axlang  .  model  .  converter  .  writer  .  TextWriter  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  axlang  .  model  .  elements  .  Model  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  AXBench  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  RCAConstants  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  Session  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  adapters  .  AXLElementAdapter  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  adapters  .  AXLElementWorkbenchContentProvider  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  advisors  .  AXBenchActionBarAdvisor  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  editors  .  AXLElementEditor  ; 
import   de  .  fraunhofer  .  isst  .  axbench  .  rca  .  editors  .  AXLElementEditorInput  ; 


















public   class   TreeView   extends   ViewPart   implements   ISaveablePart  { 


public   static   final   String   ID  =  RCAConstants  .  ID_VIEW_TREE  ; 

protected   TreeViewer   jfTreeViewer  =  null  ; 

private   boolean   bDirty  =  false  ; 










@  Override 
public   void   createPartControl  (  Composite   theParent  )  { 
jfTreeViewer  =  new   TreeViewer  (  theParent  )  ; 
jfTreeViewer  .  setLabelProvider  (  new   WorkbenchLabelProvider  (  )  )  ; 
jfTreeViewer  .  setContentProvider  (  new   AXLElementWorkbenchContentProvider  (  AXLElementAdapter  .  class  )  )  ; 
getSite  (  )  .  setSelectionProvider  (  jfTreeViewer  )  ; 
initContextMenu  (  )  ; 
initOpenAction  (  )  ; 
initDoubleClick  (  )  ; 
initDragAndDrop  (  )  ; 
initContextHelp  (  )  ; 
setModel  (  Session  .  getAXLangModel  (  )  )  ; 
} 





@  Override 
public   void   setFocus  (  )  { 
jfTreeViewer  .  getControl  (  )  .  setFocus  (  )  ; 
} 






private   void   setModel  (  Model   axlModel  )  { 
if  (  axlModel  ==  null  )  { 
setPartName  (  "No model"  )  ; 
}  else  { 
jfTreeViewer  .  setInput  (  axlModel  )  ; 
jfTreeViewer  .  expandToLevel  (  2  )  ; 
axlModel  .  addAXLangListener  (  new   IAXLangListener  (  )  { 

public   void   elementAdded  (  IAXLangElement   axlElement  )  { 
jfTreeViewer  .  refresh  (  axlElement  .  getModel  (  )  )  ; 
if  (  !  bDirty  )  { 
bDirty  =  true  ; 
firePropertyChange  (  PROP_DIRTY  )  ; 
} 
} 

public   void   elementChanged  (  IAXLangElement   axlElement  )  { 
jfTreeViewer  .  refresh  (  axlElement  .  getParent  (  )  )  ; 
if  (  !  bDirty  )  { 
bDirty  =  true  ; 
firePropertyChange  (  PROP_DIRTY  )  ; 
} 
} 
}  )  ; 
String   sTitle  =  "Model '"  +  axlModel  .  getIdentifier  (  )  +  "'"  ; 
if  (  Session  .  getFile  (  )  !=  null  )  { 
sTitle  +=  " ("  +  Session  .  getFile  (  )  .  getName  (  )  +  ")"  ; 
} 
setPartName  (  sTitle  )  ; 
} 
} 




private   void   initContextMenu  (  )  { 
MenuManager   theManager  =  new   MenuManager  (  )  ; 
theManager  .  add  (  new   Separator  (  IWorkbenchActionConstants  .  MB_ADDITIONS  )  )  ; 
Menu   theContextMenu  =  theManager  .  createContextMenu  (  jfTreeViewer  .  getControl  (  )  )  ; 
jfTreeViewer  .  getControl  (  )  .  setMenu  (  theContextMenu  )  ; 
getSite  (  )  .  registerContextMenu  (  theManager  ,  jfTreeViewer  )  ; 
} 








private   void   initOpenAction  (  )  { 
IAction   actOpen  =  new   Action  (  )  { 

@  Override 
public   void   run  (  )  { 
if  (  isDirty  (  )  )  { 
MessageBox   dlgSave  =  new   MessageBox  (  getSite  (  )  .  getShell  (  )  ,  SWT  .  ICON_WARNING  |  SWT  .  YES  |  SWT  .  NO  |  SWT  .  CANCEL  )  ; 
dlgSave  .  setMessage  (  "Current file modified. Save?"  )  ; 
dlgSave  .  setText  (  "Unsaved File"  )  ; 
int   iResult  =  dlgSave  .  open  (  )  ; 
if  (  iResult  ==  SWT  .  YES  )  { 
doSave  (  null  )  ; 
} 
if  (  iResult  ==  SWT  .  CANCEL  )  { 
return  ; 
} 
} 
FileDialog   dlgOpen  =  new   FileDialog  (  getSite  (  )  .  getShell  (  )  ,  SWT  .  OPEN  )  ; 
dlgOpen  .  setText  (  "Open aXLang model"  )  ; 
if  (  Session  .  getFile  (  )  !=  null  )  { 
dlgOpen  .  setFilterPath  (  Session  .  getFile  (  )  .  getPath  (  )  )  ; 
} 
String  [  ]  sFilters  =  {  "*"  +  RCAConstants  .  EXT_AXL  ,  "*"  +  RCAConstants  .  EXT_XML  }  ; 
dlgOpen  .  setFilterExtensions  (  sFilters  )  ; 
String   sFileName  =  dlgOpen  .  open  (  )  ; 
if  (  sFileName  ==  null  )  { 
return  ; 
} 
File   fleOpen  =  new   File  (  sFileName  )  ; 
try  { 
Session  .  loadFile  (  fleOpen  )  ; 
setModel  (  Session  .  getAXLangModel  (  )  )  ; 
bDirty  =  false  ; 
TreeView  .  this  .  firePropertyChange  (  IWorkbenchPartConstants  .  PROP_DIRTY  )  ; 
}  catch  (  IOException   e  )  { 
IStatus   theStatus  =  new   Status  (  IStatus  .  ERROR  ,  AXBench  .  class  .  getCanonicalName  (  )  ,  "Error while loading"  ,  e  )  ; 
ErrorDialog   dlgError  =  new   ErrorDialog  (  getSite  (  )  .  getShell  (  )  ,  "Load Error"  ,  "Could not load file."  ,  theStatus  ,  IStatus  .  ERROR  )  ; 
dlgError  .  open  (  )  ; 
} 
} 
}  ; 
actOpen  .  setEnabled  (  true  )  ; 
getViewSite  (  )  .  getActionBars  (  )  .  setGlobalActionHandler  (  RCAConstants  .  ID_ACTION_OPEN  ,  actOpen  )  ; 
} 







public   boolean   isDirty  (  )  { 
return   bDirty  ; 
} 





public   void   doSave  (  IProgressMonitor   theMonitor  )  { 
if  (  Session  .  getFile  (  )  ==  null  )  { 
doSaveAs  (  )  ; 
return  ; 
} 
try  { 
Session  .  writeFile  (  )  ; 
bDirty  =  false  ; 
firePropertyChange  (  PROP_DIRTY  )  ; 
}  catch  (  IOException   e  )  { 
IStatus   theStatus  =  new   Status  (  IStatus  .  ERROR  ,  AXBench  .  class  .  getCanonicalName  (  )  ,  "Error while saving"  ,  e  )  ; 
ErrorDialog   dlgError  =  new   ErrorDialog  (  getSite  (  )  .  getShell  (  )  ,  "Save Error"  ,  "Could not save file."  ,  theStatus  ,  IStatus  .  ERROR  )  ; 
dlgError  .  open  (  )  ; 
} 
} 




public   void   doSaveAs  (  )  { 
FileDialog   dlgSave  =  new   FileDialog  (  getSite  (  )  .  getShell  (  )  ,  SWT  .  SAVE  )  ; 
dlgSave  .  setText  (  "Save aXLang model as..."  )  ; 
if  (  Session  .  getFile  (  )  !=  null  )  { 
dlgSave  .  setFilterPath  (  Session  .  getFile  (  )  .  getPath  (  )  )  ; 
} 
String  [  ]  sFilters  =  {  "*"  +  RCAConstants  .  EXT_AXL  ,  "*"  +  RCAConstants  .  EXT_XML  }  ; 
dlgSave  .  setFilterExtensions  (  sFilters  )  ; 
String   sFileName  =  dlgSave  .  open  (  )  ; 
if  (  sFileName  ==  null  )  { 
return  ; 
} 
File   theFile  =  new   File  (  sFileName  )  ; 
if  (  theFile  .  exists  (  )  )  { 
MessageBox   msgOverwrite  =  new   MessageBox  (  getSite  (  )  .  getShell  (  )  ,  SWT  .  ICON_WARNING  |  SWT  .  YES  |  SWT  .  NO  )  ; 
msgOverwrite  .  setMessage  (  "File "  +  sFileName  +  " already exists.  Overwrite?"  )  ; 
msgOverwrite  .  setText  (  "File exists."  )  ; 
int   iResult  =  msgOverwrite  .  open  (  )  ; 
if  (  iResult  !=  SWT  .  YES  )  { 
return  ; 
} 
} 
Session  .  setFile  (  theFile  )  ; 
doSave  (  null  )  ; 
} 







public   boolean   isSaveAsAllowed  (  )  { 
return  (  Session  .  getAXLangModel  (  )  !=  null  )  ; 
} 







public   boolean   isSaveOnCloseNeeded  (  )  { 
return   true  ; 
} 






private   void   initDoubleClick  (  )  { 
jfTreeViewer  .  addDoubleClickListener  (  new   IDoubleClickListener  (  )  { 

public   void   doubleClick  (  DoubleClickEvent   theEvent  )  { 
IAXLangElement   axlElement  =  getSelectedElement  (  )  ; 
AXLElementEditorInput   theInput  =  new   AXLElementEditorInput  (  axlElement  .  getUID  (  )  ,  axlElement  .  getIdentifier  (  )  ,  Session  .  getImageDescriptor  (  axlElement  )  )  ; 
try  { 
getSite  (  )  .  getPage  (  )  .  openEditor  (  theInput  ,  AXLElementEditor  .  ID  )  ; 
}  catch  (  PartInitException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
}  )  ; 
} 





private   IAXLangElement   getSelectedElement  (  )  { 
IStructuredSelection   selection  =  (  IStructuredSelection  )  jfTreeViewer  .  getSelection  (  )  ; 
return  (  IAXLangElement  )  selection  .  getFirstElement  (  )  ; 
} 





private   void   initDragAndDrop  (  )  { 
Transfer  [  ]  arrDragTypes  =  new   Transfer  [  ]  {  TextTransfer  .  getInstance  (  )  }  ; 
jfTreeViewer  .  addDragSupport  (  DND  .  DROP_MOVE  ,  arrDragTypes  ,  new   DragSourceListener  (  )  { 

public   void   dragStart  (  DragSourceEvent   event  )  { 
} 

public   void   dragFinished  (  DragSourceEvent   event  )  { 
} 

public   void   dragSetData  (  DragSourceEvent   event  )  { 
IStructuredSelection   theSelection  =  (  IStructuredSelection  )  jfTreeViewer  .  getSelection  (  )  ; 
IAXLangElement   axlElement  =  (  IAXLangElement  )  theSelection  .  getFirstElement  (  )  ; 
if  (  TextTransfer  .  getInstance  (  )  .  isSupportedType  (  event  .  dataType  )  )  { 
ByteArrayOutputStream   stmOut  =  new   ByteArrayOutputStream  (  )  ; 
IAXLangWriter   theWriter  =  new   TextWriter  (  )  ; 
try  { 
theWriter  .  writeAXLangElement  (  axlElement  ,  null  ,  stmOut  )  ; 
stmOut  .  close  (  )  ; 
event  .  data  =  stmOut  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
event  .  data  =  "error converting '"  +  axlElement  .  getIdentifier  (  )  +  "'\n"  ; 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
}  )  ; 
} 




private   void   initContextHelp  (  )  { 
PlatformUI  .  getWorkbench  (  )  .  getHelpSystem  (  )  .  setHelp  (  jfTreeViewer  .  getTree  (  )  ,  RCAConstants  .  ID_AXBENCH  +  ".treeview"  )  ; 
} 
} 

