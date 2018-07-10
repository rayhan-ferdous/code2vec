package   com  .  koutra  .  dist  .  proc  .  designer  .  editor  ; 

import   java  .  io  .  IOException  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  util  .  EventObject  ; 
import   java  .  util  .  List  ; 
import   org  .  eclipse  .  core  .  resources  .  IFile  ; 
import   org  .  eclipse  .  core  .  resources  .  ResourcesPlugin  ; 
import   org  .  eclipse  .  core  .  runtime  .  CoreException  ; 
import   org  .  eclipse  .  core  .  runtime  .  IPath  ; 
import   org  .  eclipse  .  core  .  runtime  .  IProgressMonitor  ; 
import   org  .  eclipse  .  gef  .  ContextMenuProvider  ; 
import   org  .  eclipse  .  gef  .  DefaultEditDomain  ; 
import   org  .  eclipse  .  gef  .  EditPart  ; 
import   org  .  eclipse  .  gef  .  EditPartViewer  ; 
import   org  .  eclipse  .  gef  .  GraphicalViewer  ; 
import   org  .  eclipse  .  gef  .  dnd  .  TemplateTransferDragSourceListener  ; 
import   org  .  eclipse  .  gef  .  dnd  .  TemplateTransferDropTargetListener  ; 
import   org  .  eclipse  .  gef  .  editparts  .  ScalableFreeformRootEditPart  ; 
import   org  .  eclipse  .  gef  .  palette  .  PaletteRoot  ; 
import   org  .  eclipse  .  gef  .  requests  .  CreationFactory  ; 
import   org  .  eclipse  .  gef  .  requests  .  SimpleFactory  ; 
import   org  .  eclipse  .  gef  .  ui  .  actions  .  ActionRegistry  ; 
import   org  .  eclipse  .  gef  .  ui  .  palette  .  PaletteViewer  ; 
import   org  .  eclipse  .  gef  .  ui  .  palette  .  PaletteViewerProvider  ; 
import   org  .  eclipse  .  gef  .  ui  .  parts  .  ContentOutlinePage  ; 
import   org  .  eclipse  .  gef  .  ui  .  parts  .  GraphicalEditorWithFlyoutPalette  ; 
import   org  .  eclipse  .  gef  .  ui  .  parts  .  GraphicalViewerKeyHandler  ; 
import   org  .  eclipse  .  gef  .  ui  .  parts  .  TreeViewer  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  ProgressMonitorDialog  ; 
import   org  .  eclipse  .  jface  .  util  .  TransferDropTargetListener  ; 
import   org  .  eclipse  .  jface  .  viewers  .  StructuredSelection  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Composite  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Control  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Shell  ; 
import   org  .  eclipse  .  ui  .  IActionBars  ; 
import   org  .  eclipse  .  ui  .  IEditorInput  ; 
import   org  .  eclipse  .  ui  .  IEditorPart  ; 
import   org  .  eclipse  .  ui  .  IFileEditorInput  ; 
import   org  .  eclipse  .  ui  .  actions  .  ActionFactory  ; 
import   org  .  eclipse  .  ui  .  actions  .  WorkspaceModifyOperation  ; 
import   org  .  eclipse  .  ui  .  dialogs  .  SaveAsDialog  ; 
import   org  .  eclipse  .  ui  .  part  .  FileEditorInput  ; 
import   org  .  eclipse  .  ui  .  part  .  IPageSite  ; 
import   org  .  eclipse  .  ui  .  views  .  contentoutline  .  IContentOutlinePage  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  Messages  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  editor  .  parts  .  ProcessEditPartFactory  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  editor  .  parts  .  ProcessTreeEditPartFactory  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  model  .  Process  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  model  .  ProcessStep  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  model  .  ValidationError  ; 
import   com  .  koutra  .  dist  .  proc  .  designer  .  utils  .  DesignerLog  ; 

public   class   ProcessEditor   extends   GraphicalEditorWithFlyoutPalette  { 


private   com  .  koutra  .  dist  .  proc  .  designer  .  model  .  Process   process  ; 


private   static   PaletteRoot   PALETTE_MODEL  ; 


public   ProcessEditor  (  )  { 
setEditDomain  (  new   DefaultEditDomain  (  this  )  )  ; 
} 









protected   void   configureGraphicalViewer  (  )  { 
super  .  configureGraphicalViewer  (  )  ; 
GraphicalViewer   viewer  =  getGraphicalViewer  (  )  ; 
viewer  .  setEditPartFactory  (  new   ProcessEditPartFactory  (  )  )  ; 
viewer  .  setRootEditPart  (  new   ScalableFreeformRootEditPart  (  )  )  ; 
viewer  .  setKeyHandler  (  new   GraphicalViewerKeyHandler  (  viewer  )  )  ; 
ContextMenuProvider   cmProvider  =  new   ProcessEditorContextMenuProvider  (  viewer  ,  getActionRegistry  (  )  )  ; 
viewer  .  setContextMenu  (  cmProvider  )  ; 
getSite  (  )  .  registerContextMenu  (  cmProvider  ,  viewer  )  ; 
} 

public   void   commandStackChanged  (  EventObject   event  )  { 
firePropertyChange  (  IEditorPart  .  PROP_DIRTY  )  ; 
super  .  commandStackChanged  (  event  )  ; 
} 

protected   PaletteViewerProvider   createPaletteViewerProvider  (  )  { 
return   new   PaletteViewerProvider  (  getEditDomain  (  )  )  { 

protected   void   configurePaletteViewer  (  PaletteViewer   viewer  )  { 
super  .  configurePaletteViewer  (  viewer  )  ; 
viewer  .  addDragSourceListener  (  new   TemplateTransferDragSourceListener  (  viewer  )  )  ; 
} 
}  ; 
} 






private   TransferDropTargetListener   createTransferDropTargetListener  (  )  { 
return   new   TemplateTransferDropTargetListener  (  getGraphicalViewer  (  )  )  { 

@  SuppressWarnings  (  "unchecked"  ) 
protected   CreationFactory   getFactory  (  Object   template  )  { 
return   new   SimpleFactory  (  (  Class  )  template  )  ; 
} 
}  ; 
} 

public   void   doSave  (  IProgressMonitor   monitor  )  { 
try  { 
IFile   file  =  (  (  IFileEditorInput  )  getEditorInput  (  )  )  .  getFile  (  )  ; 
Process  .  dehydrate  (  file  ,  process  ,  monitor  )  ; 
getCommandStack  (  )  .  markSaveLocation  (  )  ; 
}  catch  (  CoreException   ce  )  { 
ce  .  printStackTrace  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 

public   void   doSaveAs  (  )  { 
Shell   shell  =  getSite  (  )  .  getWorkbenchWindow  (  )  .  getShell  (  )  ; 
SaveAsDialog   dialog  =  new   SaveAsDialog  (  shell  )  ; 
dialog  .  setOriginalFile  (  (  (  IFileEditorInput  )  getEditorInput  (  )  )  .  getFile  (  )  )  ; 
dialog  .  open  (  )  ; 
IPath   path  =  dialog  .  getResult  (  )  ; 
if  (  path  !=  null  )  { 
final   IFile   file  =  ResourcesPlugin  .  getWorkspace  (  )  .  getRoot  (  )  .  getFile  (  path  )  ; 
try  { 
new   ProgressMonitorDialog  (  shell  )  .  run  (  false  ,  false  ,  new   WorkspaceModifyOperation  (  )  { 

public   void   execute  (  final   IProgressMonitor   monitor  )  { 
try  { 
Process  .  dehydrate  (  file  ,  process  ,  monitor  )  ; 
}  catch  (  CoreException   ce  )  { 
ce  .  printStackTrace  (  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
}  )  ; 
setInput  (  new   FileEditorInput  (  file  )  )  ; 
getCommandStack  (  )  .  markSaveLocation  (  )  ; 
}  catch  (  InterruptedException   ie  )  { 
ie  .  printStackTrace  (  )  ; 
}  catch  (  InvocationTargetException   ite  )  { 
ite  .  printStackTrace  (  )  ; 
} 
} 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   Object   getAdapter  (  Class   type  )  { 
if  (  type  ==  IContentOutlinePage  .  class  )  return   new   ShapesOutlinePage  (  new   TreeViewer  (  )  )  ; 
return   super  .  getAdapter  (  type  )  ; 
} 

public   com  .  koutra  .  dist  .  proc  .  designer  .  model  .  Process   getModel  (  )  { 
return   process  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
protected   EditPart   getPartFor  (  ProcessStep   step  )  { 
List   steps  =  getGraphicalViewer  (  )  .  getContents  (  )  .  getChildren  (  )  ; 
for  (  Object   s  :  steps  )  { 
EditPart   editPart  =  (  EditPart  )  s  ; 
if  (  editPart  .  getModel  (  )  ==  step  )  return   editPart  ; 
} 
return   null  ; 
} 

public   String   validate  (  )  { 
List  <  ValidationError  >  validationErrors  =  process  .  validate  (  )  ; 
if  (  validationErrors  .  size  (  )  ==  0  )  return   null  ; 
ValidationError   error  =  validationErrors  .  get  (  0  )  ; 
EditPart   childEditPart  =  getPartFor  (  (  ProcessStep  )  error  .  getElement  (  )  )  ; 
getGraphicalViewer  (  )  .  setSelection  (  new   StructuredSelection  (  childEditPart  )  )  ; 
return   Messages  .  ProcessEditor_ValidationErrorPreamble  +  error  .  getErrorMessage  (  )  ; 
} 

protected   PaletteRoot   getPaletteRoot  (  )  { 
if  (  PALETTE_MODEL  ==  null  )  PALETTE_MODEL  =  ProcessEditorPaletteFactory  .  createPalette  (  )  ; 
return   PALETTE_MODEL  ; 
} 





protected   void   initializeGraphicalViewer  (  )  { 
super  .  initializeGraphicalViewer  (  )  ; 
GraphicalViewer   viewer  =  getGraphicalViewer  (  )  ; 
viewer  .  setContents  (  getModel  (  )  )  ; 
viewer  .  addDropTargetListener  (  createTransferDropTargetListener  (  )  )  ; 
} 

public   boolean   isSaveAsAllowed  (  )  { 
return   true  ; 
} 

protected   void   setInput  (  IEditorInput   input  )  { 
super  .  setInput  (  input  )  ; 
try  { 
IFile   file  =  (  (  IFileEditorInput  )  input  )  .  getFile  (  )  ; 
process  =  Process  .  hydrate  (  file  )  ; 
setPartName  (  file  .  getName  (  )  )  ; 
}  catch  (  Exception   e  )  { 
DesignerLog  .  logError  (  "Unable to load input"  ,  e  )  ; 
} 
} 




public   class   ShapesOutlinePage   extends   ContentOutlinePage  { 






public   ShapesOutlinePage  (  EditPartViewer   viewer  )  { 
super  (  viewer  )  ; 
} 

public   void   createControl  (  Composite   parent  )  { 
getViewer  (  )  .  createControl  (  parent  )  ; 
getViewer  (  )  .  setEditDomain  (  getEditDomain  (  )  )  ; 
getViewer  (  )  .  setEditPartFactory  (  new   ProcessTreeEditPartFactory  (  )  )  ; 
ContextMenuProvider   cmProvider  =  new   ProcessEditorContextMenuProvider  (  getViewer  (  )  ,  getActionRegistry  (  )  )  ; 
getViewer  (  )  .  setContextMenu  (  cmProvider  )  ; 
getSite  (  )  .  registerContextMenu  (  "org.eclipse.gef.examples.shapes.outline.contextmenu"  ,  cmProvider  ,  getSite  (  )  .  getSelectionProvider  (  )  )  ; 
getSelectionSynchronizer  (  )  .  addViewer  (  getViewer  (  )  )  ; 
getViewer  (  )  .  setContents  (  getModel  (  )  )  ; 
} 

public   void   dispose  (  )  { 
getSelectionSynchronizer  (  )  .  removeViewer  (  getViewer  (  )  )  ; 
super  .  dispose  (  )  ; 
} 

public   Control   getControl  (  )  { 
return   getViewer  (  )  .  getControl  (  )  ; 
} 




public   void   init  (  IPageSite   pageSite  )  { 
super  .  init  (  pageSite  )  ; 
ActionRegistry   registry  =  getActionRegistry  (  )  ; 
IActionBars   bars  =  pageSite  .  getActionBars  (  )  ; 
String   id  =  ActionFactory  .  UNDO  .  getId  (  )  ; 
bars  .  setGlobalActionHandler  (  id  ,  registry  .  getAction  (  id  )  )  ; 
id  =  ActionFactory  .  REDO  .  getId  (  )  ; 
bars  .  setGlobalActionHandler  (  id  ,  registry  .  getAction  (  id  )  )  ; 
id  =  ActionFactory  .  DELETE  .  getId  (  )  ; 
bars  .  setGlobalActionHandler  (  id  ,  registry  .  getAction  (  id  )  )  ; 
} 
} 
} 

