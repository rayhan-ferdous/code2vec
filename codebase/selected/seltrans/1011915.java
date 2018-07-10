package   fd2  .  diagram  .  part  ; 

import   org  .  eclipse  .  core  .  resources  .  IFile  ; 
import   org  .  eclipse  .  core  .  resources  .  IMarker  ; 
import   org  .  eclipse  .  core  .  resources  .  IWorkspaceRoot  ; 
import   org  .  eclipse  .  core  .  resources  .  ResourcesPlugin  ; 
import   org  .  eclipse  .  core  .  runtime  .  CoreException  ; 
import   org  .  eclipse  .  core  .  runtime  .  IPath  ; 
import   org  .  eclipse  .  core  .  runtime  .  IProgressMonitor  ; 
import   org  .  eclipse  .  core  .  runtime  .  IStatus  ; 
import   org  .  eclipse  .  core  .  runtime  .  NullProgressMonitor  ; 
import   org  .  eclipse  .  emf  .  common  .  ui  .  URIEditorInput  ; 
import   org  .  eclipse  .  emf  .  transaction  .  TransactionalEditingDomain  ; 
import   org  .  eclipse  .  emf  .  workspace  .  util  .  WorkspaceSynchronizer  ; 
import   org  .  eclipse  .  gef  .  palette  .  PaletteRoot  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  common  .  ui  .  services  .  marker  .  MarkerNavigationService  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  core  .  preferences  .  PreferencesHint  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  actions  .  ActionIds  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDiagramDocument  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDocument  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDocumentProvider  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  parts  .  DiagramDocumentEditor  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  notation  .  Diagram  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  ErrorDialog  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  IMessageProvider  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  MessageDialog  ; 
import   org  .  eclipse  .  jface  .  viewers  .  ISelection  ; 
import   org  .  eclipse  .  jface  .  viewers  .  StructuredSelection  ; 
import   org  .  eclipse  .  jface  .  window  .  Window  ; 
import   org  .  eclipse  .  osgi  .  util  .  NLS  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Shell  ; 
import   org  .  eclipse  .  ui  .  IEditorInput  ; 
import   org  .  eclipse  .  ui  .  IEditorMatchingStrategy  ; 
import   org  .  eclipse  .  ui  .  IEditorReference  ; 
import   org  .  eclipse  .  ui  .  IFileEditorInput  ; 
import   org  .  eclipse  .  ui  .  PlatformUI  ; 
import   org  .  eclipse  .  ui  .  dialogs  .  SaveAsDialog  ; 
import   org  .  eclipse  .  ui  .  ide  .  IGotoMarker  ; 
import   org  .  eclipse  .  ui  .  navigator  .  resources  .  ProjectExplorer  ; 
import   org  .  eclipse  .  ui  .  part  .  FileEditorInput  ; 
import   org  .  eclipse  .  ui  .  part  .  IShowInTargetList  ; 
import   org  .  eclipse  .  ui  .  part  .  ShowInContext  ; 
import   fd2  .  diagram  .  navigator  .  Fd2NavigatorItem  ; 




public   class   Fd2DiagramEditor   extends   DiagramDocumentEditor   implements   IGotoMarker  { 




public   static   final   String   ID  =  "fd2.diagram.part.Fd2DiagramEditorID"  ; 




public   static   final   String   CONTEXT_ID  =  "fd2.diagram.ui.diagramContext"  ; 




public   Fd2DiagramEditor  (  )  { 
super  (  true  )  ; 
} 




protected   String   getContextID  (  )  { 
return   CONTEXT_ID  ; 
} 




protected   PaletteRoot   createPaletteRoot  (  PaletteRoot   existingPaletteRoot  )  { 
PaletteRoot   root  =  super  .  createPaletteRoot  (  existingPaletteRoot  )  ; 
new   Fd2PaletteFactory  (  )  .  fillPalette  (  root  )  ; 
return   root  ; 
} 




protected   PreferencesHint   getPreferencesHint  (  )  { 
return   Fd2DiagramEditorPlugin  .  DIAGRAM_PREFERENCES_HINT  ; 
} 




public   String   getContributorId  (  )  { 
return   Fd2DiagramEditorPlugin  .  ID  ; 
} 




public   Object   getAdapter  (  Class   type  )  { 
if  (  type  ==  IShowInTargetList  .  class  )  { 
return   new   IShowInTargetList  (  )  { 

public   String  [  ]  getShowInTargetIds  (  )  { 
return   new   String  [  ]  {  ProjectExplorer  .  VIEW_ID  }  ; 
} 
}  ; 
} 
return   super  .  getAdapter  (  type  )  ; 
} 




protected   IDocumentProvider   getDocumentProvider  (  IEditorInput   input  )  { 
if  (  input   instanceof   IFileEditorInput  ||  input   instanceof   URIEditorInput  )  { 
return   Fd2DiagramEditorPlugin  .  getInstance  (  )  .  getDocumentProvider  (  )  ; 
} 
return   super  .  getDocumentProvider  (  input  )  ; 
} 




public   TransactionalEditingDomain   getEditingDomain  (  )  { 
IDocument   document  =  getEditorInput  (  )  !=  null  ?  getDocumentProvider  (  )  .  getDocument  (  getEditorInput  (  )  )  :  null  ; 
if  (  document   instanceof   IDiagramDocument  )  { 
return  (  (  IDiagramDocument  )  document  )  .  getEditingDomain  (  )  ; 
} 
return   super  .  getEditingDomain  (  )  ; 
} 




protected   void   setDocumentProvider  (  IEditorInput   input  )  { 
if  (  input   instanceof   IFileEditorInput  ||  input   instanceof   URIEditorInput  )  { 
setDocumentProvider  (  Fd2DiagramEditorPlugin  .  getInstance  (  )  .  getDocumentProvider  (  )  )  ; 
}  else  { 
super  .  setDocumentProvider  (  input  )  ; 
} 
} 




public   void   gotoMarker  (  IMarker   marker  )  { 
MarkerNavigationService  .  getInstance  (  )  .  gotoMarker  (  this  ,  marker  )  ; 
} 




public   boolean   isSaveAsAllowed  (  )  { 
return   true  ; 
} 




public   void   doSaveAs  (  )  { 
performSaveAs  (  new   NullProgressMonitor  (  )  )  ; 
} 




protected   void   performSaveAs  (  IProgressMonitor   progressMonitor  )  { 
Shell   shell  =  getSite  (  )  .  getShell  (  )  ; 
IEditorInput   input  =  getEditorInput  (  )  ; 
SaveAsDialog   dialog  =  new   SaveAsDialog  (  shell  )  ; 
IFile   original  =  input   instanceof   IFileEditorInput  ?  (  (  IFileEditorInput  )  input  )  .  getFile  (  )  :  null  ; 
if  (  original  !=  null  )  { 
dialog  .  setOriginalFile  (  original  )  ; 
} 
dialog  .  create  (  )  ; 
IDocumentProvider   provider  =  getDocumentProvider  (  )  ; 
if  (  provider  ==  null  )  { 
return  ; 
} 
if  (  provider  .  isDeleted  (  input  )  &&  original  !=  null  )  { 
String   message  =  NLS  .  bind  (  Messages  .  Fd2DiagramEditor_SavingDeletedFile  ,  original  .  getName  (  )  )  ; 
dialog  .  setErrorMessage  (  null  )  ; 
dialog  .  setMessage  (  message  ,  IMessageProvider  .  WARNING  )  ; 
} 
if  (  dialog  .  open  (  )  ==  Window  .  CANCEL  )  { 
if  (  progressMonitor  !=  null  )  { 
progressMonitor  .  setCanceled  (  true  )  ; 
} 
return  ; 
} 
IPath   filePath  =  dialog  .  getResult  (  )  ; 
if  (  filePath  ==  null  )  { 
if  (  progressMonitor  !=  null  )  { 
progressMonitor  .  setCanceled  (  true  )  ; 
} 
return  ; 
} 
IWorkspaceRoot   workspaceRoot  =  ResourcesPlugin  .  getWorkspace  (  )  .  getRoot  (  )  ; 
IFile   file  =  workspaceRoot  .  getFile  (  filePath  )  ; 
final   IEditorInput   newInput  =  new   FileEditorInput  (  file  )  ; 
IEditorMatchingStrategy   matchingStrategy  =  getEditorDescriptor  (  )  .  getEditorMatchingStrategy  (  )  ; 
IEditorReference  [  ]  editorRefs  =  PlatformUI  .  getWorkbench  (  )  .  getActiveWorkbenchWindow  (  )  .  getActivePage  (  )  .  getEditorReferences  (  )  ; 
for  (  int   i  =  0  ;  i  <  editorRefs  .  length  ;  i  ++  )  { 
if  (  matchingStrategy  .  matches  (  editorRefs  [  i  ]  ,  newInput  )  )  { 
MessageDialog  .  openWarning  (  shell  ,  Messages  .  Fd2DiagramEditor_SaveAsErrorTitle  ,  Messages  .  Fd2DiagramEditor_SaveAsErrorMessage  )  ; 
return  ; 
} 
} 
boolean   success  =  false  ; 
try  { 
provider  .  aboutToChange  (  newInput  )  ; 
getDocumentProvider  (  newInput  )  .  saveDocument  (  progressMonitor  ,  newInput  ,  getDocumentProvider  (  )  .  getDocument  (  getEditorInput  (  )  )  ,  true  )  ; 
success  =  true  ; 
}  catch  (  CoreException   x  )  { 
IStatus   status  =  x  .  getStatus  (  )  ; 
if  (  status  ==  null  ||  status  .  getSeverity  (  )  !=  IStatus  .  CANCEL  )  { 
ErrorDialog  .  openError  (  shell  ,  Messages  .  Fd2DiagramEditor_SaveErrorTitle  ,  Messages  .  Fd2DiagramEditor_SaveErrorMessage  ,  x  .  getStatus  (  )  )  ; 
} 
}  finally  { 
provider  .  changed  (  newInput  )  ; 
if  (  success  )  { 
setInput  (  newInput  )  ; 
} 
} 
if  (  progressMonitor  !=  null  )  { 
progressMonitor  .  setCanceled  (  !  success  )  ; 
} 
} 




public   ShowInContext   getShowInContext  (  )  { 
return   new   ShowInContext  (  getEditorInput  (  )  ,  getNavigatorSelection  (  )  )  ; 
} 




private   ISelection   getNavigatorSelection  (  )  { 
IDiagramDocument   document  =  getDiagramDocument  (  )  ; 
if  (  document  ==  null  )  { 
return   StructuredSelection  .  EMPTY  ; 
} 
Diagram   diagram  =  document  .  getDiagram  (  )  ; 
IFile   file  =  WorkspaceSynchronizer  .  getFile  (  diagram  .  eResource  (  )  )  ; 
if  (  file  !=  null  )  { 
Fd2NavigatorItem   item  =  new   Fd2NavigatorItem  (  diagram  ,  file  ,  false  )  ; 
return   new   StructuredSelection  (  item  )  ; 
} 
return   StructuredSelection  .  EMPTY  ; 
} 




protected   void   configureGraphicalViewer  (  )  { 
super  .  configureGraphicalViewer  (  )  ; 
DiagramEditorContextMenuProvider   provider  =  new   DiagramEditorContextMenuProvider  (  this  ,  getDiagramGraphicalViewer  (  )  )  ; 
getDiagramGraphicalViewer  (  )  .  setContextMenu  (  provider  )  ; 
getSite  (  )  .  registerContextMenu  (  ActionIds  .  DIAGRAM_EDITOR_CONTEXT_MENU  ,  provider  ,  getDiagramGraphicalViewer  (  )  )  ; 
} 
} 

