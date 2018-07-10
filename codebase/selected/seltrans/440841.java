package   org  .  germinus  .  telcoblocks  .  servicios  .  diagram  .  rcp  .  part  ; 

import   org  .  eclipse  .  emf  .  common  .  ui  .  URIEditorInput  ; 
import   org  .  eclipse  .  emf  .  transaction  .  TransactionalEditingDomain  ; 
import   org  .  eclipse  .  gef  .  palette  .  PaletteRoot  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  core  .  preferences  .  PreferencesHint  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  actions  .  ActionIds  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDiagramDocument  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDocument  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  document  .  IDocumentProvider  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  resources  .  editor  .  parts  .  DiagramDocumentEditor  ; 
import   org  .  eclipse  .  ui  .  IEditorInput  ; 




public   class   TelcoblocksDiagramEditor   extends   DiagramDocumentEditor  { 




public   static   final   String   ID  =  "org.germinus.telcoblocks.servicios.diagram.rcp.part.TelcoblocksDiagramEditorID"  ; 




public   static   final   String   CONTEXT_ID  =  "org.germinus.telcoblocks.servicios.diagram.rcp.ui.diagramContext"  ; 




public   TelcoblocksDiagramEditor  (  )  { 
super  (  true  )  ; 
} 




protected   String   getContextID  (  )  { 
return   CONTEXT_ID  ; 
} 




protected   PaletteRoot   createPaletteRoot  (  PaletteRoot   existingPaletteRoot  )  { 
PaletteRoot   root  =  super  .  createPaletteRoot  (  existingPaletteRoot  )  ; 
new   TelcoblocksPaletteFactory  (  )  .  fillPalette  (  root  )  ; 
return   root  ; 
} 




protected   PreferencesHint   getPreferencesHint  (  )  { 
return   TelcoblocksServiciosDiagramEditorPlugin  .  DIAGRAM_PREFERENCES_HINT  ; 
} 




public   String   getContributorId  (  )  { 
return   TelcoblocksServiciosDiagramEditorPlugin  .  ID  ; 
} 




protected   IDocumentProvider   getDocumentProvider  (  IEditorInput   input  )  { 
if  (  input   instanceof   URIEditorInput  )  { 
return   TelcoblocksServiciosDiagramEditorPlugin  .  getInstance  (  )  .  getDocumentProvider  (  )  ; 
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
if  (  input   instanceof   URIEditorInput  )  { 
setDocumentProvider  (  TelcoblocksServiciosDiagramEditorPlugin  .  getInstance  (  )  .  getDocumentProvider  (  )  )  ; 
}  else  { 
super  .  setDocumentProvider  (  input  )  ; 
} 
} 




protected   void   configureGraphicalViewer  (  )  { 
super  .  configureGraphicalViewer  (  )  ; 
DiagramEditorContextMenuProvider   provider  =  new   DiagramEditorContextMenuProvider  (  this  ,  getDiagramGraphicalViewer  (  )  )  ; 
getDiagramGraphicalViewer  (  )  .  setContextMenu  (  provider  )  ; 
getSite  (  )  .  registerContextMenu  (  ActionIds  .  DIAGRAM_EDITOR_CONTEXT_MENU  ,  provider  ,  getDiagramGraphicalViewer  (  )  )  ; 
} 
} 

