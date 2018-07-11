package   net  .  tourbook  .  ui  ; 

import   org  .  eclipse  .  swt  .  SWT  ; 
import   org  .  eclipse  .  swt  .  SWTException  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  ACC  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  Accessible  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  AccessibleAdapter  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  AccessibleControlAdapter  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  AccessibleControlEvent  ; 
import   org  .  eclipse  .  swt  .  accessibility  .  AccessibleEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  DisposeEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  DisposeListener  ; 
import   org  .  eclipse  .  swt  .  events  .  PaintEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  PaintListener  ; 
import   org  .  eclipse  .  swt  .  events  .  TraverseEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  TraverseListener  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Color  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Font  ; 
import   org  .  eclipse  .  swt  .  graphics  .  GC  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Image  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Point  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Rectangle  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Canvas  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Composite  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Control  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Display  ; 

























public   class   ImageComboLabel   extends   Canvas  { 

private   static   final   int   GAP  =  3  ; 

private   static   final   int   INDENT  =  0  ; 


private   static   final   String   ELLIPSIS  =  "..."  ; 


private   int   align  =  SWT  .  LEFT  ; 

private   int   hIndent  =  INDENT  ; 

private   int   vIndent  =  INDENT  ; 


private   String   text  ; 


private   Image   image  ; 

private   String   appToolTipText  ; 

private   Image   backgroundImage  ; 

private   Color  [  ]  gradientColors  ; 

private   int  [  ]  gradientPercents  ; 

private   boolean   gradientVertical  ; 

private   Color   background  ; 

private   static   int   DRAW_FLAGS  =  SWT  .  DRAW_MNEMONIC  |  SWT  .  DRAW_TAB  |  SWT  .  DRAW_TRANSPARENT  |  SWT  .  DRAW_DELIMITER  ; 

































public   ImageComboLabel  (  final   Composite   parent  ,  int   style  )  { 
super  (  parent  ,  checkStyle  (  style  )  )  ; 
if  (  (  style  &  (  SWT  .  CENTER  |  SWT  .  RIGHT  )  )  ==  0  )  { 
style  |=  SWT  .  LEFT  ; 
} 
if  (  (  style  &  SWT  .  CENTER  )  !=  0  )  { 
align  =  SWT  .  CENTER  ; 
} 
if  (  (  style  &  SWT  .  RIGHT  )  !=  0  )  { 
align  =  SWT  .  RIGHT  ; 
} 
if  (  (  style  &  SWT  .  LEFT  )  !=  0  )  { 
align  =  SWT  .  LEFT  ; 
} 
addPaintListener  (  new   PaintListener  (  )  { 

public   void   paintControl  (  final   PaintEvent   event  )  { 
onPaint  (  event  )  ; 
} 
}  )  ; 
addDisposeListener  (  new   DisposeListener  (  )  { 

public   void   widgetDisposed  (  final   DisposeEvent   event  )  { 
onDispose  (  event  )  ; 
} 
}  )  ; 
addTraverseListener  (  new   TraverseListener  (  )  { 

public   void   keyTraversed  (  final   TraverseEvent   event  )  { 
if  (  event  .  detail  ==  SWT  .  TRAVERSE_MNEMONIC  )  { 
onMnemonic  (  event  )  ; 
} 
} 
}  )  ; 
initAccessible  (  )  ; 
} 




private   static   int   checkStyle  (  int   style  )  { 
if  (  (  style  &  SWT  .  BORDER  )  !=  0  )  { 
style  |=  SWT  .  SHADOW_IN  ; 
} 
final   int   mask  =  SWT  .  SHADOW_IN  |  SWT  .  SHADOW_OUT  |  SWT  .  SHADOW_NONE  |  SWT  .  LEFT_TO_RIGHT  |  SWT  .  RIGHT_TO_LEFT  ; 
style  =  style  &  mask  ; 
style  |=  SWT  .  NO_FOCUS  |  SWT  .  DOUBLE_BUFFERED  ; 
return   style  ; 
} 

char   _findMnemonic  (  final   String   string  )  { 
if  (  string  ==  null  )  { 
return  '\0'  ; 
} 
int   index  =  0  ; 
final   int   length  =  string  .  length  (  )  ; 
do  { 
while  (  index  <  length  &&  string  .  charAt  (  index  )  !=  '&'  )  { 
index  ++  ; 
} 
if  (  ++  index  >=  length  )  { 
return  '\0'  ; 
} 
if  (  string  .  charAt  (  index  )  !=  '&'  )  { 
return   Character  .  toLowerCase  (  string  .  charAt  (  index  )  )  ; 
} 
index  ++  ; 
}  while  (  index  <  length  )  ; 
return  '\0'  ; 
} 

@  Override 
public   Point   computeSize  (  final   int   wHint  ,  final   int   hHint  ,  final   boolean   changed  )  { 
checkWidget  (  )  ; 
final   Point   e  =  getTotalSize  (  image  ,  text  )  ; 
if  (  wHint  ==  SWT  .  DEFAULT  )  { 
e  .  x  +=  2  *  hIndent  ; 
}  else  { 
e  .  x  =  wHint  ; 
} 
if  (  hHint  ==  SWT  .  DEFAULT  )  { 
e  .  y  +=  2  *  vIndent  ; 
}  else  { 
e  .  y  =  hHint  ; 
} 
return   e  ; 
} 




private   void   drawBevelRect  (  final   GC   gc  ,  final   int   x  ,  final   int   y  ,  final   int   w  ,  final   int   h  ,  final   Color   topleft  ,  final   Color   bottomright  )  { 
gc  .  setForeground  (  bottomright  )  ; 
gc  .  drawLine  (  x  +  w  ,  y  ,  x  +  w  ,  y  +  h  )  ; 
gc  .  drawLine  (  x  ,  y  +  h  ,  x  +  w  ,  y  +  h  )  ; 
gc  .  setForeground  (  topleft  )  ; 
gc  .  drawLine  (  x  ,  y  ,  x  +  w  -  1  ,  y  )  ; 
gc  .  drawLine  (  x  ,  y  ,  x  ,  y  +  h  -  1  )  ; 
} 






public   int   getAlignment  (  )  { 
return   align  ; 
} 






public   Image   getImage  (  )  { 
return   image  ; 
} 

@  Override 
public   int   getStyle  (  )  { 
int   style  =  super  .  getStyle  (  )  ; 
switch  (  align  )  { 
case   SWT  .  RIGHT  : 
style  |=  SWT  .  RIGHT  ; 
break  ; 
case   SWT  .  CENTER  : 
style  |=  SWT  .  CENTER  ; 
break  ; 
case   SWT  .  LEFT  : 
style  |=  SWT  .  LEFT  ; 
break  ; 
} 
return   style  ; 
} 






public   String   getText  (  )  { 
return   text  ; 
} 

@  Override 
public   String   getToolTipText  (  )  { 
checkWidget  (  )  ; 
return   appToolTipText  ; 
} 




private   Point   getTotalSize  (  final   Image   image  ,  final   String   text  )  { 
final   Point   size  =  new   Point  (  0  ,  0  )  ; 
if  (  image  !=  null  )  { 
final   Rectangle   r  =  image  .  getBounds  (  )  ; 
size  .  x  +=  r  .  width  ; 
size  .  y  +=  r  .  height  ; 
} 
final   GC   gc  =  new   GC  (  this  )  ; 
if  (  text  !=  null  &&  text  .  length  (  )  >  0  )  { 
final   Point   e  =  gc  .  textExtent  (  text  ,  DRAW_FLAGS  )  ; 
size  .  x  +=  e  .  x  ; 
size  .  y  =  Math  .  max  (  size  .  y  ,  e  .  y  )  ; 
if  (  image  !=  null  )  { 
size  .  x  +=  GAP  ; 
} 
}  else  { 
size  .  y  =  Math  .  max  (  size  .  y  ,  gc  .  getFontMetrics  (  )  .  getHeight  (  )  )  ; 
} 
gc  .  dispose  (  )  ; 
return   size  ; 
} 

private   void   initAccessible  (  )  { 
final   Accessible   accessible  =  getAccessible  (  )  ; 
accessible  .  addAccessibleListener  (  new   AccessibleAdapter  (  )  { 

@  Override 
public   void   getHelp  (  final   AccessibleEvent   e  )  { 
e  .  result  =  getToolTipText  (  )  ; 
} 

@  Override 
public   void   getKeyboardShortcut  (  final   AccessibleEvent   e  )  { 
final   char   mnemonic  =  _findMnemonic  (  ImageComboLabel  .  this  .  text  )  ; 
if  (  mnemonic  !=  '\0'  )  { 
e  .  result  =  "Alt+"  +  mnemonic  ; 
} 
} 

@  Override 
public   void   getName  (  final   AccessibleEvent   e  )  { 
e  .  result  =  getText  (  )  ; 
} 
}  )  ; 
accessible  .  addAccessibleControlListener  (  new   AccessibleControlAdapter  (  )  { 

@  Override 
public   void   getChildAtPoint  (  final   AccessibleControlEvent   e  )  { 
e  .  childID  =  ACC  .  CHILDID_SELF  ; 
} 

@  Override 
public   void   getChildCount  (  final   AccessibleControlEvent   e  )  { 
e  .  detail  =  0  ; 
} 

@  Override 
public   void   getLocation  (  final   AccessibleControlEvent   e  )  { 
final   Rectangle   rect  =  getDisplay  (  )  .  map  (  getParent  (  )  ,  null  ,  getBounds  (  )  )  ; 
e  .  x  =  rect  .  x  ; 
e  .  y  =  rect  .  y  ; 
e  .  width  =  rect  .  width  ; 
e  .  height  =  rect  .  height  ; 
} 

@  Override 
public   void   getRole  (  final   AccessibleControlEvent   e  )  { 
e  .  detail  =  ACC  .  ROLE_LABEL  ; 
} 

@  Override 
public   void   getState  (  final   AccessibleControlEvent   e  )  { 
e  .  detail  =  ACC  .  STATE_READONLY  ; 
} 
}  )  ; 
} 

void   onDispose  (  final   DisposeEvent   event  )  { 
gradientColors  =  null  ; 
gradientPercents  =  null  ; 
backgroundImage  =  null  ; 
text  =  null  ; 
image  =  null  ; 
appToolTipText  =  null  ; 
} 

void   onMnemonic  (  final   TraverseEvent   event  )  { 
final   char   mnemonic  =  _findMnemonic  (  text  )  ; 
if  (  mnemonic  ==  '\0'  )  { 
return  ; 
} 
if  (  Character  .  toLowerCase  (  event  .  character  )  !=  mnemonic  )  { 
return  ; 
} 
Composite   control  =  this  .  getParent  (  )  ; 
while  (  control  !=  null  )  { 
final   Control  [  ]  children  =  control  .  getChildren  (  )  ; 
int   index  =  0  ; 
while  (  index  <  children  .  length  )  { 
if  (  children  [  index  ]  ==  this  )  { 
break  ; 
} 
index  ++  ; 
} 
index  ++  ; 
if  (  index  <  children  .  length  )  { 
if  (  children  [  index  ]  .  setFocus  (  )  )  { 
event  .  doit  =  true  ; 
event  .  detail  =  SWT  .  TRAVERSE_NONE  ; 
} 
} 
control  =  control  .  getParent  (  )  ; 
} 
} 

void   onPaint  (  final   PaintEvent   event  )  { 
final   Rectangle   rect  =  getClientArea  (  )  ; 
if  (  rect  .  width  ==  0  ||  rect  .  height  ==  0  )  { 
return  ; 
} 
boolean   shortenText  =  false  ; 
final   String   t  =  text  ; 
Image   img  =  image  ; 
final   int   availableWidth  =  Math  .  max  (  0  ,  rect  .  width  -  2  *  hIndent  )  ; 
Point   extent  =  getTotalSize  (  img  ,  t  )  ; 
if  (  extent  .  x  >  availableWidth  )  { 
img  =  null  ; 
extent  =  getTotalSize  (  img  ,  t  )  ; 
if  (  extent  .  x  >  availableWidth  )  { 
shortenText  =  true  ; 
} 
} 
final   GC   gc  =  event  .  gc  ; 
final   String  [  ]  lines  =  text  ==  null  ?  null  :  splitString  (  text  )  ; 
if  (  shortenText  )  { 
extent  .  x  =  0  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  i  ++  )  { 
final   Point   e  =  gc  .  textExtent  (  lines  [  i  ]  ,  DRAW_FLAGS  )  ; 
if  (  e  .  x  >  availableWidth  )  { 
lines  [  i  ]  =  shortenText  (  gc  ,  lines  [  i  ]  ,  availableWidth  )  ; 
extent  .  x  =  Math  .  max  (  extent  .  x  ,  getTotalSize  (  null  ,  lines  [  i  ]  )  .  x  )  ; 
}  else  { 
extent  .  x  =  Math  .  max  (  extent  .  x  ,  e  .  x  )  ; 
} 
} 
if  (  appToolTipText  ==  null  )  { 
super  .  setToolTipText  (  text  )  ; 
} 
}  else  { 
super  .  setToolTipText  (  appToolTipText  )  ; 
} 
int   x  =  rect  .  x  +  hIndent  ; 
if  (  align  ==  SWT  .  CENTER  )  { 
x  =  (  rect  .  width  -  extent  .  x  )  /  2  ; 
} 
if  (  align  ==  SWT  .  RIGHT  )  { 
x  =  rect  .  width  -  hIndent  -  extent  .  x  ; 
} 
try  { 
if  (  backgroundImage  !=  null  )  { 
final   Rectangle   imageRect  =  backgroundImage  .  getBounds  (  )  ; 
gc  .  setBackground  (  getBackground  (  )  )  ; 
gc  .  fillRectangle  (  rect  )  ; 
int   xPos  =  0  ; 
while  (  xPos  <  rect  .  width  )  { 
int   yPos  =  0  ; 
while  (  yPos  <  rect  .  height  )  { 
gc  .  drawImage  (  backgroundImage  ,  xPos  ,  yPos  )  ; 
yPos  +=  imageRect  .  height  ; 
} 
xPos  +=  imageRect  .  width  ; 
} 
}  else   if  (  gradientColors  !=  null  )  { 
final   Color   oldBackground  =  gc  .  getBackground  (  )  ; 
if  (  gradientColors  .  length  ==  1  )  { 
if  (  gradientColors  [  0  ]  !=  null  )  { 
gc  .  setBackground  (  gradientColors  [  0  ]  )  ; 
} 
gc  .  fillRectangle  (  0  ,  0  ,  rect  .  width  ,  rect  .  height  )  ; 
}  else  { 
final   Color   oldForeground  =  gc  .  getForeground  (  )  ; 
Color   lastColor  =  gradientColors  [  0  ]  ; 
if  (  lastColor  ==  null  )  { 
lastColor  =  oldBackground  ; 
} 
int   pos  =  0  ; 
for  (  int   i  =  0  ;  i  <  gradientPercents  .  length  ;  ++  i  )  { 
gc  .  setForeground  (  lastColor  )  ; 
lastColor  =  gradientColors  [  i  +  1  ]  ; 
if  (  lastColor  ==  null  )  { 
lastColor  =  oldBackground  ; 
} 
gc  .  setBackground  (  lastColor  )  ; 
if  (  gradientVertical  )  { 
final   int   gradientHeight  =  (  gradientPercents  [  i  ]  *  rect  .  height  /  100  )  -  pos  ; 
gc  .  fillGradientRectangle  (  0  ,  pos  ,  rect  .  width  ,  gradientHeight  ,  true  )  ; 
pos  +=  gradientHeight  ; 
}  else  { 
final   int   gradientWidth  =  (  gradientPercents  [  i  ]  *  rect  .  width  /  100  )  -  pos  ; 
gc  .  fillGradientRectangle  (  pos  ,  0  ,  gradientWidth  ,  rect  .  height  ,  false  )  ; 
pos  +=  gradientWidth  ; 
} 
} 
if  (  gradientVertical  &&  pos  <  rect  .  height  )  { 
gc  .  setBackground  (  getBackground  (  )  )  ; 
gc  .  fillRectangle  (  0  ,  pos  ,  rect  .  width  ,  rect  .  height  -  pos  )  ; 
} 
if  (  !  gradientVertical  &&  pos  <  rect  .  width  )  { 
gc  .  setBackground  (  getBackground  (  )  )  ; 
gc  .  fillRectangle  (  pos  ,  0  ,  rect  .  width  -  pos  ,  rect  .  height  )  ; 
} 
gc  .  setForeground  (  oldForeground  )  ; 
} 
gc  .  setBackground  (  oldBackground  )  ; 
}  else  { 
if  (  background  !=  null  ||  (  getStyle  (  )  &  SWT  .  DOUBLE_BUFFERED  )  ==  0  )  { 
gc  .  setBackground  (  getBackground  (  )  )  ; 
gc  .  fillRectangle  (  rect  )  ; 
} 
} 
}  catch  (  final   SWTException   e  )  { 
if  (  (  getStyle  (  )  &  SWT  .  DOUBLE_BUFFERED  )  ==  0  )  { 
gc  .  setBackground  (  getBackground  (  )  )  ; 
gc  .  fillRectangle  (  rect  )  ; 
} 
} 
final   int   style  =  getStyle  (  )  ; 
if  (  (  style  &  SWT  .  SHADOW_IN  )  !=  0  ||  (  style  &  SWT  .  SHADOW_OUT  )  !=  0  )  { 
paintBorder  (  gc  ,  rect  )  ; 
} 
if  (  img  !=  null  )  { 
final   Rectangle   imageRect  =  img  .  getBounds  (  )  ; 
gc  .  drawImage  (  img  ,  0  ,  0  ,  imageRect  .  width  ,  imageRect  .  height  ,  x  ,  (  rect  .  height  -  imageRect  .  height  )  /  2  ,  imageRect  .  width  ,  imageRect  .  height  )  ; 
x  +=  imageRect  .  width  +  GAP  ; 
extent  .  x  -=  imageRect  .  width  +  GAP  ; 
} 
if  (  lines  !=  null  )  { 
final   int   lineHeight  =  gc  .  getFontMetrics  (  )  .  getHeight  (  )  ; 
final   int   textHeight  =  lines  .  length  *  lineHeight  ; 
int   lineY  =  Math  .  max  (  vIndent  ,  rect  .  y  +  (  rect  .  height  -  textHeight  )  /  2  )  ; 
gc  .  setForeground  (  getForeground  (  )  )  ; 
for  (  final   String   line  :  lines  )  { 
int   lineX  =  x  ; 
if  (  lines  .  length  >  1  )  { 
if  (  align  ==  SWT  .  CENTER  )  { 
final   int   lineWidth  =  gc  .  textExtent  (  line  ,  DRAW_FLAGS  )  .  x  ; 
lineX  =  x  +  Math  .  max  (  0  ,  (  extent  .  x  -  lineWidth  )  /  2  )  ; 
} 
if  (  align  ==  SWT  .  RIGHT  )  { 
final   int   lineWidth  =  gc  .  textExtent  (  line  ,  DRAW_FLAGS  )  .  x  ; 
lineX  =  Math  .  max  (  x  ,  rect  .  x  +  rect  .  width  -  hIndent  -  lineWidth  )  ; 
} 
} 
gc  .  drawText  (  line  ,  lineX  ,  lineY  ,  DRAW_FLAGS  )  ; 
lineY  +=  lineHeight  ; 
} 
} 
} 




private   void   paintBorder  (  final   GC   gc  ,  final   Rectangle   r  )  { 
final   Display   disp  =  getDisplay  (  )  ; 
Color   c1  =  null  ; 
Color   c2  =  null  ; 
final   int   style  =  getStyle  (  )  ; 
if  (  (  style  &  SWT  .  SHADOW_IN  )  !=  0  )  { 
c1  =  disp  .  getSystemColor  (  SWT  .  COLOR_WIDGET_NORMAL_SHADOW  )  ; 
c2  =  disp  .  getSystemColor  (  SWT  .  COLOR_WIDGET_HIGHLIGHT_SHADOW  )  ; 
} 
if  (  (  style  &  SWT  .  SHADOW_OUT  )  !=  0  )  { 
c1  =  disp  .  getSystemColor  (  SWT  .  COLOR_WIDGET_LIGHT_SHADOW  )  ; 
c2  =  disp  .  getSystemColor  (  SWT  .  COLOR_WIDGET_NORMAL_SHADOW  )  ; 
} 
if  (  c1  !=  null  &&  c2  !=  null  )  { 
gc  .  setLineWidth  (  1  )  ; 
drawBevelRect  (  gc  ,  r  .  x  ,  r  .  y  ,  r  .  width  -  1  ,  r  .  height  -  1  ,  c1  ,  c2  )  ; 
} 
} 
















public   void   setAlignment  (  final   int   align  )  { 
checkWidget  (  )  ; 
if  (  align  !=  SWT  .  LEFT  &&  align  !=  SWT  .  RIGHT  &&  align  !=  SWT  .  CENTER  )  { 
SWT  .  error  (  SWT  .  ERROR_INVALID_ARGUMENT  )  ; 
} 
if  (  this  .  align  !=  align  )  { 
this  .  align  =  align  ; 
redraw  (  )  ; 
} 
} 

@  Override 
public   void   setBackground  (  final   Color   color  )  { 
super  .  setBackground  (  color  )  ; 
if  (  backgroundImage  ==  null  &&  gradientColors  ==  null  &&  gradientPercents  ==  null  )  { 
if  (  color  ==  null  )  { 
if  (  background  ==  null  )  { 
return  ; 
} 
}  else  { 
if  (  color  .  equals  (  background  )  )  { 
return  ; 
} 
} 
} 
background  =  color  ; 
backgroundImage  =  null  ; 
gradientColors  =  null  ; 
gradientPercents  =  null  ; 
redraw  (  )  ; 
} 


































public   void   setBackground  (  final   Color  [  ]  colors  ,  final   int  [  ]  percents  )  { 
setBackground  (  colors  ,  percents  ,  false  )  ; 
} 



































public   void   setBackground  (  Color  [  ]  colors  ,  int  [  ]  percents  ,  final   boolean   vertical  )  { 
checkWidget  (  )  ; 
if  (  colors  !=  null  )  { 
if  (  percents  ==  null  ||  percents  .  length  !=  colors  .  length  -  1  )  { 
SWT  .  error  (  SWT  .  ERROR_INVALID_ARGUMENT  )  ; 
} 
if  (  getDisplay  (  )  .  getDepth  (  )  <  15  )  { 
colors  =  new   Color  [  ]  {  colors  [  colors  .  length  -  1  ]  }  ; 
percents  =  new   int  [  ]  {  }  ; 
} 
for  (  int   i  =  0  ;  i  <  percents  .  length  ;  i  ++  )  { 
if  (  percents  [  i  ]  <  0  ||  percents  [  i  ]  >  100  )  { 
SWT  .  error  (  SWT  .  ERROR_INVALID_ARGUMENT  )  ; 
} 
if  (  i  >  0  &&  percents  [  i  ]  <  percents  [  i  -  1  ]  )  { 
SWT  .  error  (  SWT  .  ERROR_INVALID_ARGUMENT  )  ; 
} 
} 
} 
final   Color   background  =  getBackground  (  )  ; 
if  (  backgroundImage  ==  null  )  { 
if  (  (  gradientColors  !=  null  )  &&  (  colors  !=  null  )  &&  (  gradientColors  .  length  ==  colors  .  length  )  )  { 
boolean   same  =  false  ; 
for  (  int   i  =  0  ;  i  <  gradientColors  .  length  ;  i  ++  )  { 
same  =  (  gradientColors  [  i  ]  ==  colors  [  i  ]  )  ||  (  (  gradientColors  [  i  ]  ==  null  )  &&  (  colors  [  i  ]  ==  background  )  )  ||  (  (  gradientColors  [  i  ]  ==  background  )  &&  (  colors  [  i  ]  ==  null  )  )  ; 
if  (  !  same  )  { 
break  ; 
} 
} 
if  (  same  )  { 
for  (  int   i  =  0  ;  i  <  gradientPercents  .  length  ;  i  ++  )  { 
same  =  gradientPercents  [  i  ]  ==  percents  [  i  ]  ; 
if  (  !  same  )  { 
break  ; 
} 
} 
} 
if  (  same  &&  this  .  gradientVertical  ==  vertical  )  { 
return  ; 
} 
} 
}  else  { 
backgroundImage  =  null  ; 
} 
if  (  colors  ==  null  )  { 
gradientColors  =  null  ; 
gradientPercents  =  null  ; 
gradientVertical  =  false  ; 
}  else  { 
gradientColors  =  new   Color  [  colors  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  colors  .  length  ;  ++  i  )  { 
gradientColors  [  i  ]  =  (  colors  [  i  ]  !=  null  )  ?  colors  [  i  ]  :  background  ; 
} 
gradientPercents  =  new   int  [  percents  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  percents  .  length  ;  ++  i  )  { 
gradientPercents  [  i  ]  =  percents  [  i  ]  ; 
} 
gradientVertical  =  vertical  ; 
} 
redraw  (  )  ; 
} 













public   void   setBackground  (  final   Image   image  )  { 
checkWidget  (  )  ; 
if  (  image  ==  backgroundImage  )  { 
return  ; 
} 
if  (  image  !=  null  )  { 
gradientColors  =  null  ; 
gradientPercents  =  null  ; 
} 
backgroundImage  =  image  ; 
redraw  (  )  ; 
} 

@  Override 
public   void   setFont  (  final   Font   font  )  { 
super  .  setFont  (  font  )  ; 
redraw  (  )  ; 
} 













public   void   setImage  (  final   Image   image  )  { 
checkWidget  (  )  ; 
if  (  image  !=  this  .  image  )  { 
this  .  image  =  image  ; 
redraw  (  )  ; 
} 
} 













public   void   setText  (  String   text  )  { 
checkWidget  (  )  ; 
if  (  text  ==  null  )  { 
text  =  UI  .  EMPTY_STRING  ; 
} 
if  (  !  text  .  equals  (  this  .  text  )  )  { 
this  .  text  =  text  ; 
redraw  (  )  ; 
} 
} 

@  Override 
public   void   setToolTipText  (  final   String   string  )  { 
super  .  setToolTipText  (  string  )  ; 
appToolTipText  =  super  .  getToolTipText  (  )  ; 
} 














protected   String   shortenText  (  final   GC   gc  ,  final   String   t  ,  final   int   width  )  { 
if  (  t  ==  null  )  { 
return   null  ; 
} 
final   int   w  =  gc  .  textExtent  (  ELLIPSIS  ,  DRAW_FLAGS  )  .  x  ; 
if  (  width  <=  w  )  { 
return   t  ; 
} 
final   int   l  =  t  .  length  (  )  ; 
int   max  =  l  /  2  ; 
int   min  =  0  ; 
int   mid  =  (  max  +  min  )  /  2  -  1  ; 
if  (  mid  <=  0  )  { 
return   t  ; 
} 
while  (  min  <  mid  &&  mid  <  max  )  { 
final   String   s1  =  t  .  substring  (  0  ,  mid  )  ; 
final   String   s2  =  t  .  substring  (  l  -  mid  ,  l  )  ; 
final   int   l1  =  gc  .  textExtent  (  s1  ,  DRAW_FLAGS  )  .  x  ; 
final   int   l2  =  gc  .  textExtent  (  s2  ,  DRAW_FLAGS  )  .  x  ; 
if  (  l1  +  w  +  l2  >  width  )  { 
max  =  mid  ; 
mid  =  (  max  +  min  )  /  2  ; 
}  else   if  (  l1  +  w  +  l2  <  width  )  { 
min  =  mid  ; 
mid  =  (  max  +  min  )  /  2  ; 
}  else  { 
min  =  max  ; 
} 
} 
if  (  mid  ==  0  )  { 
return   t  ; 
} 
return   t  .  substring  (  0  ,  mid  )  +  ELLIPSIS  +  t  .  substring  (  l  -  mid  ,  l  )  ; 
} 

private   String  [  ]  splitString  (  final   String   text  )  { 
String  [  ]  lines  =  new   String  [  1  ]  ; 
int   start  =  0  ,  pos  ; 
do  { 
pos  =  text  .  indexOf  (  '\n'  ,  start  )  ; 
if  (  pos  ==  -  1  )  { 
lines  [  lines  .  length  -  1  ]  =  text  .  substring  (  start  )  ; 
}  else  { 
final   boolean   crlf  =  (  pos  >  0  )  &&  (  text  .  charAt  (  pos  -  1  )  ==  '\r'  )  ; 
lines  [  lines  .  length  -  1  ]  =  text  .  substring  (  start  ,  pos  -  (  crlf  ?  1  :  0  )  )  ; 
start  =  pos  +  1  ; 
final   String  [  ]  newLines  =  new   String  [  lines  .  length  +  1  ]  ; 
System  .  arraycopy  (  lines  ,  0  ,  newLines  ,  0  ,  lines  .  length  )  ; 
lines  =  newLines  ; 
} 
}  while  (  pos  !=  -  1  )  ; 
return   lines  ; 
} 
} 

