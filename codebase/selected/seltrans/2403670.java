package   org  .  apache  .  batik  .  bridge  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  TimerTask  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   java  .  util  .  zip  .  DeflaterOutputStream  ; 
import   org  .  apache  .  batik  .  dom  .  GenericDOMImplementation  ; 
import   org  .  apache  .  batik  .  dom  .  svg  .  SAXSVGDocumentFactory  ; 
import   org  .  apache  .  batik  .  dom  .  svg  .  SVGOMDocument  ; 
import   org  .  apache  .  batik  .  dom  .  util  .  SAXDocumentFactory  ; 
import   org  .  apache  .  batik  .  dom  .  util  .  XLinkSupport  ; 
import   org  .  apache  .  batik  .  script  .  Interpreter  ; 
import   org  .  apache  .  batik  .  script  .  InterpreterException  ; 
import   org  .  apache  .  batik  .  util  .  EncodingUtilities  ; 
import   org  .  apache  .  batik  .  util  .  ParsedURL  ; 
import   org  .  apache  .  batik  .  util  .  RunnableQueue  ; 
import   org  .  apache  .  batik  .  util  .  SVGConstants  ; 
import   org  .  apache  .  batik  .  util  .  XMLResourceDescriptor  ; 
import   org  .  w3c  .  dom  .  Document  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  Node  ; 
import   org  .  w3c  .  dom  .  events  .  Event  ; 
import   org  .  w3c  .  dom  .  events  .  EventListener  ; 
import   org  .  w3c  .  dom  .  events  .  EventTarget  ; 
import   org  .  w3c  .  dom  .  events  .  MutationEvent  ; 
import   org  .  w3c  .  dom  .  svg  .  SVGDocument  ; 







public   class   ScriptingEnvironment   extends   BaseScriptingEnvironment  { 




protected   static   final   String   FRAGMENT_PREFIX  =  "<svg xmlns='"  +  SVGConstants  .  SVG_NAMESPACE_URI  +  "' xmlns:xlink='"  +  XLinkSupport  .  XLINK_NAMESPACE_URI  +  "'>"  ; 

protected   static   final   String   FRAGMENT_SUFFIX  =  "</svg>"  ; 

public   static   final   String  [  ]  SVG_EVENT_ATTRS  =  {  "onabort"  ,  "onerror"  ,  "onresize"  ,  "onscroll"  ,  "onunload"  ,  "onzoom"  ,  "onbegin"  ,  "onend"  ,  "onrepeat"  ,  "onfocusin"  ,  "onfocusout"  ,  "onactivate"  ,  "onclick"  ,  "onmousedown"  ,  "onmouseup"  ,  "onmouseover"  ,  "onmouseout"  ,  "onmousemove"  ,  "onkeypress"  ,  "onkeydown"  ,  "onkeyup"  }  ; 

public   static   final   String  [  ]  SVG_DOM_EVENT  =  {  "SVGAbort"  ,  "SVGError"  ,  "SVGResize"  ,  "SVGScroll"  ,  "SVGUnload"  ,  "SVGZoom"  ,  "beginEvent"  ,  "endEvent"  ,  "repeatEvent"  ,  "DOMFocusIn"  ,  "DOMFocusOut"  ,  "DOMActivate"  ,  "click"  ,  "mousedown"  ,  "mouseup"  ,  "mouseover"  ,  "mouseout"  ,  "mousemove"  ,  "keypress"  ,  "keydown"  ,  "keyup"  }  ; 




protected   Timer   timer  =  new   Timer  (  true  )  ; 




protected   UpdateManager   updateManager  ; 




protected   RunnableQueue   updateRunnableQueue  ; 




protected   EventListener   domNodeInsertedListener  =  new   DOMNodeInsertedListener  (  )  ; 




protected   EventListener   domNodeRemovedListener  =  new   DOMNodeRemovedListener  (  )  ; 




protected   EventListener   domAttrModifiedListener  =  new   DOMAttrModifiedListener  (  )  ; 




protected   EventListener   svgAbortListener  =  new   ScriptingEventListener  (  "onabort"  )  ; 




protected   EventListener   svgErrorListener  =  new   ScriptingEventListener  (  "onerror"  )  ; 




protected   EventListener   svgResizeListener  =  new   ScriptingEventListener  (  "onresize"  )  ; 




protected   EventListener   svgScrollListener  =  new   ScriptingEventListener  (  "onscroll"  )  ; 




protected   EventListener   svgUnloadListener  =  new   ScriptingEventListener  (  "onunload"  )  ; 




protected   EventListener   svgZoomListener  =  new   ScriptingEventListener  (  "onzoom"  )  ; 




protected   EventListener   beginListener  =  new   ScriptingEventListener  (  "onbegin"  )  ; 




protected   EventListener   endListener  =  new   ScriptingEventListener  (  "onend"  )  ; 




protected   EventListener   repeatListener  =  new   ScriptingEventListener  (  "onrepeat"  )  ; 




protected   EventListener   focusinListener  =  new   ScriptingEventListener  (  "onfocusin"  )  ; 




protected   EventListener   focusoutListener  =  new   ScriptingEventListener  (  "onfocusout"  )  ; 




protected   EventListener   activateListener  =  new   ScriptingEventListener  (  "onactivate"  )  ; 




protected   EventListener   clickListener  =  new   ScriptingEventListener  (  "onclick"  )  ; 




protected   EventListener   mousedownListener  =  new   ScriptingEventListener  (  "onmousedown"  )  ; 




protected   EventListener   mouseupListener  =  new   ScriptingEventListener  (  "onmouseup"  )  ; 




protected   EventListener   mouseoverListener  =  new   ScriptingEventListener  (  "onmouseover"  )  ; 




protected   EventListener   mouseoutListener  =  new   ScriptingEventListener  (  "onmouseout"  )  ; 




protected   EventListener   mousemoveListener  =  new   ScriptingEventListener  (  "onmousemove"  )  ; 




protected   EventListener   keypressListener  =  new   ScriptingEventListener  (  "onkeypress"  )  ; 




protected   EventListener   keydownListener  =  new   ScriptingEventListener  (  "onkeydown"  )  ; 




protected   EventListener   keyupListener  =  new   ScriptingEventListener  (  "onkeyup"  )  ; 

protected   EventListener  [  ]  listeners  =  {  svgAbortListener  ,  svgErrorListener  ,  svgResizeListener  ,  svgScrollListener  ,  svgUnloadListener  ,  svgZoomListener  ,  beginListener  ,  endListener  ,  repeatListener  ,  focusinListener  ,  focusoutListener  ,  activateListener  ,  clickListener  ,  mousedownListener  ,  mouseupListener  ,  mouseoverListener  ,  mouseoutListener  ,  mousemoveListener  ,  keypressListener  ,  keydownListener  ,  keyupListener  }  ; 

Map   attrToDOMEvent  =  new   HashMap  (  SVG_EVENT_ATTRS  .  length  )  ; 

Map   attrToListener  =  new   HashMap  (  SVG_EVENT_ATTRS  .  length  )  ; 

{ 
for  (  int   i  =  0  ;  i  <  SVG_EVENT_ATTRS  .  length  ;  i  ++  )  { 
attrToDOMEvent  .  put  (  SVG_EVENT_ATTRS  [  i  ]  ,  SVG_DOM_EVENT  [  i  ]  )  ; 
attrToListener  .  put  (  SVG_EVENT_ATTRS  [  i  ]  ,  listeners  [  i  ]  )  ; 
} 
} 





public   ScriptingEnvironment  (  BridgeContext   ctx  )  { 
super  (  ctx  )  ; 
updateManager  =  ctx  .  getUpdateManager  (  )  ; 
updateRunnableQueue  =  updateManager  .  getUpdateRunnableQueue  (  )  ; 
addScriptingListeners  (  document  .  getDocumentElement  (  )  )  ; 
EventTarget   et  =  (  EventTarget  )  document  ; 
et  .  addEventListener  (  "DOMNodeInserted"  ,  domNodeInsertedListener  ,  false  )  ; 
et  .  addEventListener  (  "DOMNodeRemoved"  ,  domNodeRemovedListener  ,  false  )  ; 
et  .  addEventListener  (  "DOMAttrModified"  ,  domAttrModifiedListener  ,  false  )  ; 
} 




public   org  .  apache  .  batik  .  script  .  Window   createWindow  (  Interpreter   interp  ,  String   lang  )  { 
return   new   Window  (  interp  ,  lang  )  ; 
} 




public   void   runEventHandler  (  String   script  ,  Event   evt  ,  String   lang  ,  String   desc  )  { 
Interpreter   interpreter  =  getInterpreter  (  lang  )  ; 
if  (  interpreter  ==  null  )  return  ; 
try  { 
checkCompatibleScriptURL  (  lang  ,  docPURL  )  ; 
interpreter  .  bindObject  (  EVENT_NAME  ,  evt  )  ; 
interpreter  .  bindObject  (  ALTERNATE_EVENT_NAME  ,  evt  )  ; 
interpreter  .  evaluate  (  new   StringReader  (  script  )  ,  desc  )  ; 
}  catch  (  IOException   ioe  )  { 
}  catch  (  InterpreterException   ie  )  { 
handleInterpreterException  (  ie  )  ; 
}  catch  (  SecurityException   se  )  { 
handleSecurityException  (  se  )  ; 
} 
} 




public   void   interrupt  (  )  { 
timer  .  cancel  (  )  ; 
removeScriptingListeners  (  document  .  getDocumentElement  (  )  )  ; 
EventTarget   et  =  (  EventTarget  )  document  ; 
et  .  removeEventListener  (  "DOMNodeInserted"  ,  domNodeInsertedListener  ,  false  )  ; 
et  .  removeEventListener  (  "DOMNodeRemoved"  ,  domNodeRemovedListener  ,  false  )  ; 
et  .  removeEventListener  (  "DOMAttrModified"  ,  domAttrModifiedListener  ,  false  )  ; 
} 




protected   void   addScriptingListeners  (  Node   node  )  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   elt  =  (  Element  )  node  ; 
EventTarget   target  =  (  EventTarget  )  elt  ; 
if  (  SVGConstants  .  SVG_NAMESPACE_URI  .  equals  (  elt  .  getNamespaceURI  (  )  )  )  { 
if  (  SVGConstants  .  SVG_SVG_TAG  .  equals  (  elt  .  getLocalName  (  )  )  )  { 
if  (  elt  .  hasAttributeNS  (  null  ,  "onabort"  )  )  { 
target  .  addEventListener  (  "SVGAbort"  ,  svgAbortListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onerror"  )  )  { 
target  .  addEventListener  (  "SVGError"  ,  svgErrorListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onresize"  )  )  { 
target  .  addEventListener  (  "SVGResize"  ,  svgResizeListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onscroll"  )  )  { 
target  .  addEventListener  (  "SVGScroll"  ,  svgScrollListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onunload"  )  )  { 
target  .  addEventListener  (  "SVGUnload"  ,  svgUnloadListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onzoom"  )  )  { 
target  .  addEventListener  (  "SVGZoom"  ,  svgZoomListener  ,  false  )  ; 
} 
}  else  { 
String   name  =  elt  .  getLocalName  (  )  ; 
if  (  name  .  equals  (  SVGConstants  .  SVG_SET_TAG  )  ||  name  .  startsWith  (  "animate"  )  )  { 
if  (  elt  .  hasAttributeNS  (  null  ,  "onbegin"  )  )  { 
target  .  addEventListener  (  "beginEvent"  ,  beginListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onend"  )  )  { 
target  .  addEventListener  (  "endEvent"  ,  endListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onrepeat"  )  )  { 
target  .  addEventListener  (  "repeatEvent"  ,  repeatListener  ,  false  )  ; 
} 
return  ; 
} 
} 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onfocusin"  )  )  { 
target  .  addEventListener  (  "DOMFocusIn"  ,  focusinListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onfocusout"  )  )  { 
target  .  addEventListener  (  "DOMFocusOut"  ,  focusoutListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onactivate"  )  )  { 
target  .  addEventListener  (  "DOMActivate"  ,  activateListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onclick"  )  )  { 
target  .  addEventListener  (  "click"  ,  clickListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onmousedown"  )  )  { 
target  .  addEventListener  (  "mousedown"  ,  mousedownListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onmouseup"  )  )  { 
target  .  addEventListener  (  "mouseup"  ,  mouseupListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onmouseover"  )  )  { 
target  .  addEventListener  (  "mouseover"  ,  mouseoverListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onmouseout"  )  )  { 
target  .  addEventListener  (  "mouseout"  ,  mouseoutListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onmousemove"  )  )  { 
target  .  addEventListener  (  "mousemove"  ,  mousemoveListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onkeypress"  )  )  { 
target  .  addEventListener  (  "keypress"  ,  keypressListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onkeydown"  )  )  { 
target  .  addEventListener  (  "keydown"  ,  keydownListener  ,  false  )  ; 
} 
if  (  elt  .  hasAttributeNS  (  null  ,  "onkeyup"  )  )  { 
target  .  addEventListener  (  "keyup"  ,  keyupListener  ,  false  )  ; 
} 
} 
for  (  Node   n  =  node  .  getFirstChild  (  )  ;  n  !=  null  ;  n  =  n  .  getNextSibling  (  )  )  { 
addScriptingListeners  (  n  )  ; 
} 
} 




protected   void   removeScriptingListeners  (  Node   node  )  { 
if  (  node  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
Element   elt  =  (  Element  )  node  ; 
EventTarget   target  =  (  EventTarget  )  elt  ; 
if  (  SVGConstants  .  SVG_NAMESPACE_URI  .  equals  (  elt  .  getNamespaceURI  (  )  )  )  { 
if  (  SVGConstants  .  SVG_SVG_TAG  .  equals  (  elt  .  getLocalName  (  )  )  )  { 
target  .  removeEventListener  (  "SVGAbort"  ,  svgAbortListener  ,  false  )  ; 
target  .  removeEventListener  (  "SVGError"  ,  svgErrorListener  ,  false  )  ; 
target  .  removeEventListener  (  "SVGResize"  ,  svgResizeListener  ,  false  )  ; 
target  .  removeEventListener  (  "SVGScroll"  ,  svgScrollListener  ,  false  )  ; 
target  .  removeEventListener  (  "SVGUnload"  ,  svgUnloadListener  ,  false  )  ; 
target  .  removeEventListener  (  "SVGZoom"  ,  svgZoomListener  ,  false  )  ; 
}  else  { 
String   name  =  elt  .  getLocalName  (  )  ; 
if  (  name  .  equals  (  SVGConstants  .  SVG_SET_TAG  )  ||  name  .  startsWith  (  "animate"  )  )  { 
target  .  removeEventListener  (  "beginEvent"  ,  beginListener  ,  false  )  ; 
target  .  removeEventListener  (  "endEvent"  ,  endListener  ,  false  )  ; 
target  .  removeEventListener  (  "repeatEvent"  ,  repeatListener  ,  false  )  ; 
return  ; 
} 
} 
} 
target  .  removeEventListener  (  "DOMFocusIn"  ,  focusinListener  ,  false  )  ; 
target  .  removeEventListener  (  "DOMFocusOut"  ,  focusoutListener  ,  false  )  ; 
target  .  removeEventListener  (  "DOMActivate"  ,  activateListener  ,  false  )  ; 
target  .  removeEventListener  (  "click"  ,  clickListener  ,  false  )  ; 
target  .  removeEventListener  (  "mousedown"  ,  mousedownListener  ,  false  )  ; 
target  .  removeEventListener  (  "mouseup"  ,  mouseupListener  ,  false  )  ; 
target  .  removeEventListener  (  "mouseover"  ,  mouseoverListener  ,  false  )  ; 
target  .  removeEventListener  (  "mouseout"  ,  mouseoutListener  ,  false  )  ; 
target  .  removeEventListener  (  "mousemove"  ,  mousemoveListener  ,  false  )  ; 
target  .  removeEventListener  (  "keypress"  ,  keypressListener  ,  false  )  ; 
target  .  removeEventListener  (  "keydown"  ,  keydownListener  ,  false  )  ; 
target  .  removeEventListener  (  "keyup"  ,  keyupListener  ,  false  )  ; 
} 
for  (  Node   n  =  node  .  getFirstChild  (  )  ;  n  !=  null  ;  n  =  n  .  getNextSibling  (  )  )  { 
removeScriptingListeners  (  n  )  ; 
} 
} 




protected   void   updateScriptingListeners  (  Element   elt  ,  String   attr  )  { 
String   domEvt  =  (  String  )  attrToDOMEvent  .  get  (  attr  )  ; 
if  (  domEvt  ==  null  )  return  ; 
EventListener   listener  =  (  EventListener  )  attrToListener  .  get  (  attr  )  ; 
EventTarget   target  =  (  EventTarget  )  elt  ; 
if  (  elt  .  hasAttributeNS  (  null  ,  attr  )  )  target  .  addEventListener  (  domEvt  ,  listener  ,  false  )  ;  else   target  .  removeEventListener  (  domEvt  ,  listener  ,  false  )  ; 
} 




protected   class   EvaluateRunnable   implements   Runnable  { 

protected   Interpreter   interpreter  ; 

protected   String   script  ; 

public   EvaluateRunnable  (  String   s  ,  Interpreter   interp  )  { 
interpreter  =  interp  ; 
script  =  s  ; 
} 

public   void   run  (  )  { 
try  { 
interpreter  .  evaluate  (  script  )  ; 
}  catch  (  InterpreterException   ie  )  { 
handleInterpreterException  (  ie  )  ; 
} 
} 
} 




protected   class   EvaluateIntervalRunnable   implements   Runnable  { 




public   int   count  ; 

public   boolean   error  ; 

protected   Interpreter   interpreter  ; 

protected   String   script  ; 

public   EvaluateIntervalRunnable  (  String   s  ,  Interpreter   interp  )  { 
interpreter  =  interp  ; 
script  =  s  ; 
} 

public   void   run  (  )  { 
synchronized  (  this  )  { 
if  (  error  )  return  ; 
count  --  ; 
} 
try  { 
interpreter  .  evaluate  (  script  )  ; 
}  catch  (  InterpreterException   ie  )  { 
handleInterpreterException  (  ie  )  ; 
synchronized  (  this  )  { 
error  =  true  ; 
} 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
}  else  { 
e  .  printStackTrace  (  )  ; 
} 
synchronized  (  this  )  { 
error  =  true  ; 
} 
} 
} 
} 




protected   class   EvaluateRunnableRunnable   implements   Runnable  { 




public   int   count  ; 

public   boolean   error  ; 

protected   Runnable   runnable  ; 

public   EvaluateRunnableRunnable  (  Runnable   r  )  { 
runnable  =  r  ; 
} 

public   void   run  (  )  { 
synchronized  (  this  )  { 
if  (  error  )  return  ; 
count  --  ; 
} 
try  { 
runnable  .  run  (  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
}  else  { 
e  .  printStackTrace  (  )  ; 
} 
synchronized  (  this  )  { 
error  =  true  ; 
} 
} 
} 
} 




protected   class   Window   implements   org  .  apache  .  batik  .  script  .  Window  { 




protected   Interpreter   interpreter  ; 




protected   String   language  ; 




public   Window  (  Interpreter   interp  ,  String   lang  )  { 
interpreter  =  interp  ; 
language  =  lang  ; 
} 





public   Object   setInterval  (  final   String   script  ,  long   interval  )  { 
TimerTask   tt  =  new   TimerTask  (  )  { 

EvaluateIntervalRunnable   eir  =  new   EvaluateIntervalRunnable  (  script  ,  interpreter  )  ; 

public   void   run  (  )  { 
synchronized  (  eir  )  { 
if  (  eir  .  count  >  1  )  return  ; 
eir  .  count  ++  ; 
} 
synchronized  (  updateRunnableQueue  .  getIteratorLock  (  )  )  { 
if  (  updateRunnableQueue  .  getThread  (  )  ==  null  )  { 
cancel  (  )  ; 
return  ; 
} 
updateRunnableQueue  .  invokeLater  (  eir  )  ; 
} 
synchronized  (  eir  )  { 
if  (  eir  .  error  )  cancel  (  )  ; 
} 
} 
}  ; 
timer  .  schedule  (  tt  ,  interval  ,  interval  )  ; 
return   tt  ; 
} 





public   Object   setInterval  (  final   Runnable   r  ,  long   interval  )  { 
TimerTask   tt  =  new   TimerTask  (  )  { 

EvaluateRunnableRunnable   eihr  =  new   EvaluateRunnableRunnable  (  r  )  ; 

public   void   run  (  )  { 
synchronized  (  eihr  )  { 
if  (  eihr  .  count  >  1  )  return  ; 
eihr  .  count  ++  ; 
} 
updateRunnableQueue  .  invokeLater  (  eihr  )  ; 
synchronized  (  eihr  )  { 
if  (  eihr  .  error  )  cancel  (  )  ; 
} 
} 
}  ; 
timer  .  schedule  (  tt  ,  interval  ,  interval  )  ; 
return   tt  ; 
} 





public   void   clearInterval  (  Object   interval  )  { 
if  (  interval  ==  null  )  return  ; 
(  (  TimerTask  )  interval  )  .  cancel  (  )  ; 
} 





public   Object   setTimeout  (  final   String   script  ,  long   timeout  )  { 
TimerTask   tt  =  new   TimerTask  (  )  { 

public   void   run  (  )  { 
updateRunnableQueue  .  invokeLater  (  new   EvaluateRunnable  (  script  ,  interpreter  )  )  ; 
} 
}  ; 
timer  .  schedule  (  tt  ,  timeout  )  ; 
return   tt  ; 
} 





public   Object   setTimeout  (  final   Runnable   r  ,  long   timeout  )  { 
TimerTask   tt  =  new   TimerTask  (  )  { 

public   void   run  (  )  { 
updateRunnableQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
r  .  run  (  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
} 
} 
}  )  ; 
} 
}  ; 
timer  .  schedule  (  tt  ,  timeout  )  ; 
return   tt  ; 
} 





public   void   clearTimeout  (  Object   timeout  )  { 
if  (  timeout  ==  null  )  return  ; 
(  (  TimerTask  )  timeout  )  .  cancel  (  )  ; 
} 





public   Node   parseXML  (  String   text  ,  Document   doc  )  { 
SAXSVGDocumentFactory   df  =  new   SAXSVGDocumentFactory  (  XMLResourceDescriptor  .  getXMLParserClassName  (  )  )  ; 
URL   urlObj  =  null  ; 
if  (  (  doc  !=  null  )  &&  (  doc   instanceof   SVGOMDocument  )  )  urlObj  =  (  (  SVGOMDocument  )  doc  )  .  getURLObject  (  )  ; 
if  (  urlObj  ==  null  )  { 
urlObj  =  (  (  SVGOMDocument  )  bridgeContext  .  getDocument  (  )  )  .  getURLObject  (  )  ; 
} 
String   uri  =  (  urlObj  ==  null  )  ?  ""  :  urlObj  .  toString  (  )  ; 
try  { 
Document   d  =  df  .  createDocument  (  uri  ,  new   StringReader  (  text  )  )  ; 
if  (  doc  ==  null  )  return   d  ; 
Node   result  =  doc  .  createDocumentFragment  (  )  ; 
result  .  appendChild  (  doc  .  importNode  (  d  .  getDocumentElement  (  )  ,  true  )  )  ; 
return   result  ; 
}  catch  (  Exception   ex  )  { 
} 
if  (  (  doc  !=  null  )  &&  (  doc   instanceof   SVGOMDocument  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  FRAGMENT_PREFIX  .  length  (  )  +  text  .  length  (  )  +  FRAGMENT_SUFFIX  .  length  (  )  )  ; 
sb  .  append  (  FRAGMENT_PREFIX  )  ; 
sb  .  append  (  text  )  ; 
sb  .  append  (  FRAGMENT_SUFFIX  )  ; 
String   newText  =  sb  .  toString  (  )  ; 
try  { 
Document   d  =  df  .  createDocument  (  uri  ,  new   StringReader  (  newText  )  )  ; 
if  (  doc  ==  null  )  doc  =  d  ; 
for  (  Node   n  =  d  .  getDocumentElement  (  )  .  getFirstChild  (  )  ;  n  !=  null  ;  n  =  n  .  getNextSibling  (  )  )  { 
if  (  n  .  getNodeType  (  )  ==  Node  .  ELEMENT_NODE  )  { 
n  =  doc  .  importNode  (  n  ,  true  )  ; 
Node   result  =  doc  .  createDocumentFragment  (  )  ; 
result  .  appendChild  (  n  )  ; 
return   result  ; 
} 
} 
}  catch  (  Exception   exc  )  { 
} 
} 
SAXDocumentFactory   sdf  ; 
if  (  doc  !=  null  )  { 
sdf  =  new   SAXDocumentFactory  (  doc  .  getImplementation  (  )  ,  XMLResourceDescriptor  .  getXMLParserClassName  (  )  )  ; 
}  else  { 
sdf  =  new   SAXDocumentFactory  (  new   GenericDOMImplementation  (  )  ,  XMLResourceDescriptor  .  getXMLParserClassName  (  )  )  ; 
} 
try  { 
Document   d  =  sdf  .  createDocument  (  uri  ,  new   StringReader  (  text  )  )  ; 
if  (  doc  ==  null  )  return   d  ; 
Node   result  =  doc  .  createDocumentFragment  (  )  ; 
result  .  appendChild  (  doc  .  importNode  (  d  .  getDocumentElement  (  )  ,  true  )  )  ; 
return   result  ; 
}  catch  (  Exception   ext  )  { 
if  (  userAgent  !=  null  )  userAgent  .  displayError  (  ext  )  ; 
} 
return   null  ; 
} 





public   void   getURL  (  String   uri  ,  org  .  apache  .  batik  .  script  .  Window  .  URLResponseHandler   h  )  { 
getURL  (  uri  ,  h  ,  null  )  ; 
} 

static   final   String   DEFLATE  =  "deflate"  ; 

static   final   String   GZIP  =  "gzip"  ; 

static   final   String   UTF_8  =  "UTF-8"  ; 





public   void   getURL  (  final   String   uri  ,  final   org  .  apache  .  batik  .  script  .  Window  .  URLResponseHandler   h  ,  final   String   enc  )  { 
Thread   t  =  new   Thread  (  )  { 

public   void   run  (  )  { 
try  { 
URL   burl  ; 
burl  =  (  (  SVGOMDocument  )  document  )  .  getURLObject  (  )  ; 
final   ParsedURL   purl  =  new   ParsedURL  (  burl  ,  uri  )  ; 
String   e  =  null  ; 
if  (  enc  !=  null  )  { 
e  =  EncodingUtilities  .  javaEncoding  (  enc  )  ; 
e  =  (  (  e  ==  null  )  ?  enc  :  e  )  ; 
} 
InputStream   is  =  purl  .  openStream  (  )  ; 
Reader   r  ; 
if  (  e  ==  null  )  { 
r  =  new   InputStreamReader  (  is  )  ; 
}  else  { 
try  { 
r  =  new   InputStreamReader  (  is  ,  e  )  ; 
}  catch  (  UnsupportedEncodingException   uee  )  { 
r  =  new   InputStreamReader  (  is  )  ; 
} 
} 
r  =  new   BufferedReader  (  r  )  ; 
final   StringBuffer   sb  =  new   StringBuffer  (  )  ; 
int   read  ; 
char  [  ]  buf  =  new   char  [  4096  ]  ; 
while  (  (  read  =  r  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  )  { 
sb  .  append  (  buf  ,  0  ,  read  )  ; 
} 
r  .  close  (  )  ; 
updateRunnableQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
h  .  getURLDone  (  true  ,  purl  .  getContentType  (  )  ,  sb  .  toString  (  )  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
} 
} 
}  )  ; 
}  catch  (  Exception   e  )  { 
if  (  e   instanceof   SecurityException  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
updateRunnableQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
h  .  getURLDone  (  false  ,  null  ,  null  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
} 
} 
}  )  ; 
} 
} 
}  ; 
t  .  setPriority  (  Thread  .  MIN_PRIORITY  )  ; 
t  .  start  (  )  ; 
} 

public   void   postURL  (  String   uri  ,  String   content  ,  org  .  apache  .  batik  .  script  .  Window  .  URLResponseHandler   h  )  { 
postURL  (  uri  ,  content  ,  h  ,  "text/plain"  ,  null  )  ; 
} 

public   void   postURL  (  String   uri  ,  String   content  ,  org  .  apache  .  batik  .  script  .  Window  .  URLResponseHandler   h  ,  String   mimeType  )  { 
postURL  (  uri  ,  content  ,  h  ,  mimeType  ,  null  )  ; 
} 

public   void   postURL  (  final   String   uri  ,  final   String   content  ,  final   org  .  apache  .  batik  .  script  .  Window  .  URLResponseHandler   h  ,  final   String   mimeType  ,  final   String   fEnc  )  { 
Thread   t  =  new   Thread  (  )  { 

public   void   run  (  )  { 
try  { 
URL   burl  ; 
burl  =  (  (  SVGOMDocument  )  document  )  .  getURLObject  (  )  ; 
URL   url  ; 
if  (  burl  !=  null  )  url  =  new   URL  (  burl  ,  uri  )  ;  else   url  =  new   URL  (  uri  )  ; 
final   URLConnection   conn  =  url  .  openConnection  (  )  ; 
conn  .  setDoOutput  (  true  )  ; 
conn  .  setDoInput  (  true  )  ; 
conn  .  setUseCaches  (  false  )  ; 
conn  .  setRequestProperty  (  "Content-Type"  ,  mimeType  )  ; 
OutputStream   os  =  conn  .  getOutputStream  (  )  ; 
String   e  =  null  ,  enc  =  fEnc  ; 
if  (  enc  !=  null  )  { 
if  (  enc  .  startsWith  (  DEFLATE  )  )  { 
os  =  new   DeflaterOutputStream  (  os  )  ; 
if  (  enc  .  length  (  )  >  DEFLATE  .  length  (  )  )  enc  =  enc  .  substring  (  DEFLATE  .  length  (  )  +  1  )  ;  else   enc  =  ""  ; 
conn  .  setRequestProperty  (  "Content-Encoding"  ,  DEFLATE  )  ; 
} 
if  (  enc  .  startsWith  (  GZIP  )  )  { 
os  =  new   GZIPOutputStream  (  os  )  ; 
if  (  enc  .  length  (  )  >  GZIP  .  length  (  )  )  enc  =  enc  .  substring  (  GZIP  .  length  (  )  +  1  )  ;  else   enc  =  ""  ; 
conn  .  setRequestProperty  (  "Content-Encoding"  ,  DEFLATE  )  ; 
} 
if  (  enc  .  length  (  )  !=  0  )  { 
e  =  EncodingUtilities  .  javaEncoding  (  enc  )  ; 
if  (  e  ==  null  )  e  =  UTF_8  ; 
}  else  { 
e  =  UTF_8  ; 
} 
} 
Writer   w  ; 
if  (  e  ==  null  )  w  =  new   OutputStreamWriter  (  os  )  ;  else   w  =  new   OutputStreamWriter  (  os  ,  e  )  ; 
w  .  write  (  content  )  ; 
w  .  flush  (  )  ; 
w  .  close  (  )  ; 
os  .  close  (  )  ; 
InputStream   is  =  conn  .  getInputStream  (  )  ; 
Reader   r  ; 
e  =  UTF_8  ; 
if  (  e  ==  null  )  r  =  new   InputStreamReader  (  is  )  ;  else   r  =  new   InputStreamReader  (  is  ,  e  )  ; 
r  =  new   BufferedReader  (  r  )  ; 
final   StringBuffer   sb  =  new   StringBuffer  (  )  ; 
int   read  ; 
char  [  ]  buf  =  new   char  [  4096  ]  ; 
while  (  (  read  =  r  .  read  (  buf  ,  0  ,  buf  .  length  )  )  !=  -  1  )  { 
sb  .  append  (  buf  ,  0  ,  read  )  ; 
} 
r  .  close  (  )  ; 
updateRunnableQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
h  .  getURLDone  (  true  ,  conn  .  getContentType  (  )  ,  sb  .  toString  (  )  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
} 
} 
}  )  ; 
}  catch  (  Exception   e  )  { 
if  (  e   instanceof   SecurityException  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
updateRunnableQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
h  .  getURLDone  (  false  ,  null  ,  null  )  ; 
}  catch  (  Exception   e  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  displayError  (  e  )  ; 
} 
} 
} 
}  )  ; 
} 
} 
}  ; 
t  .  setPriority  (  Thread  .  MIN_PRIORITY  )  ; 
t  .  start  (  )  ; 
} 




public   void   alert  (  String   message  )  { 
if  (  userAgent  !=  null  )  { 
userAgent  .  showAlert  (  message  )  ; 
} 
} 




public   boolean   confirm  (  String   message  )  { 
if  (  userAgent  !=  null  )  { 
return   userAgent  .  showConfirm  (  message  )  ; 
} 
return   false  ; 
} 




public   String   prompt  (  String   message  )  { 
if  (  userAgent  !=  null  )  { 
return   userAgent  .  showPrompt  (  message  )  ; 
} 
return   null  ; 
} 




public   String   prompt  (  String   message  ,  String   defVal  )  { 
if  (  userAgent  !=  null  )  { 
return   userAgent  .  showPrompt  (  message  ,  defVal  )  ; 
} 
return   null  ; 
} 




public   BridgeContext   getBridgeContext  (  )  { 
return   bridgeContext  ; 
} 




public   Interpreter   getInterpreter  (  )  { 
return   interpreter  ; 
} 
} 




protected   class   DOMNodeInsertedListener   implements   EventListener  { 

public   void   handleEvent  (  Event   evt  )  { 
addScriptingListeners  (  (  Node  )  evt  .  getTarget  (  )  )  ; 
} 
} 




protected   class   DOMNodeRemovedListener   implements   EventListener  { 

public   void   handleEvent  (  Event   evt  )  { 
removeScriptingListeners  (  (  Node  )  evt  .  getTarget  (  )  )  ; 
} 
} 

protected   class   DOMAttrModifiedListener   implements   EventListener  { 

public   void   handleEvent  (  Event   evt  )  { 
MutationEvent   me  =  (  MutationEvent  )  evt  ; 
if  (  me  .  getAttrChange  (  )  !=  MutationEvent  .  MODIFICATION  )  updateScriptingListeners  (  (  Element  )  me  .  getTarget  (  )  ,  me  .  getAttrName  (  )  )  ; 
} 
} 




protected   class   ScriptingEventListener   implements   EventListener  { 




protected   String   attribute  ; 




public   ScriptingEventListener  (  String   attr  )  { 
attribute  =  attr  ; 
} 




public   void   handleEvent  (  Event   evt  )  { 
Element   elt  =  (  Element  )  evt  .  getCurrentTarget  (  )  ; 
String   script  =  elt  .  getAttributeNS  (  null  ,  attribute  )  ; 
if  (  script  .  length  (  )  ==  0  )  return  ; 
DocumentLoader   dl  =  bridgeContext  .  getDocumentLoader  (  )  ; 
SVGDocument   d  =  (  SVGDocument  )  elt  .  getOwnerDocument  (  )  ; 
int   line  =  dl  .  getLineNumber  (  elt  )  ; 
final   String   desc  =  Messages  .  formatMessage  (  EVENT_SCRIPT_DESCRIPTION  ,  new   Object  [  ]  {  d  .  getURL  (  )  ,  attribute  ,  new   Integer  (  line  )  }  )  ; 
Element   e  =  elt  ; 
while  (  e  !=  null  &&  (  !  SVGConstants  .  SVG_NAMESPACE_URI  .  equals  (  e  .  getNamespaceURI  (  )  )  ||  !  SVGConstants  .  SVG_SVG_TAG  .  equals  (  e  .  getLocalName  (  )  )  )  )  { 
e  =  SVGUtilities  .  getParentElement  (  e  )  ; 
} 
if  (  e  ==  null  )  return  ; 
String   lang  =  e  .  getAttributeNS  (  null  ,  SVGConstants  .  SVG_CONTENT_SCRIPT_TYPE_ATTRIBUTE  )  ; 
runEventHandler  (  script  ,  evt  ,  lang  ,  desc  )  ; 
} 
} 
} 

