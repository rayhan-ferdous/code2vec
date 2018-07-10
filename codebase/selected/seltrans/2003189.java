package   core  ; 

import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Component  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  GridBagConstraints  ; 
import   java  .  awt  .  GridBagLayout  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  MouseAdapter  ; 
import   java  .  awt  .  event  .  MouseEvent  ; 
import   java  .  beans  .  PropertyVetoException  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  util  .  ArrayList  ; 
import   javax  .  sound  .  midi  .  MidiMessage  ; 
import   javax  .  sound  .  midi  .  Receiver  ; 
import   javax  .  sound  .  midi  .  ShortMessage  ; 
import   javax  .  sound  .  midi  .  Transmitter  ; 
import   javax  .  swing  .  JComponent  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JScrollPane  ; 
import   javax  .  swing  .  UIManager  ; 







public   class   PatchEditorFrame   extends   Actions  .  MenuFrame   implements   PatchBasket  { 


protected   ISinglePatch   p  ; 


protected   JPanel   scrollPane  ; 


protected   GridBagConstraints   gbc  =  new   GridBagConstraints  (  )  ; 


protected   ArrayList   widgetList  =  new   ArrayList  (  )  ; 


protected   int   forceLabelWidth  =  0  ; 






protected   BankEditorFrame   bankFrame  =  null  ; 


private   int   faderBank  ; 


private   int   numFaderBanks  ; 


private   static   int   nFrame  =  0  ; 


private   static   boolean   sendPatchOnActivated  =  true  ; 





private   final   IPatch   originalPatch  ; 


private   int   patchRow  ; 

private   int   patchCol  ; 


private   SysexWidget   recentWidget  ; 

private   int   lastFader  ; 

private   JScrollPane   scroller  ; 










protected   PatchEditorFrame  (  String   name  ,  ISinglePatch   patch  )  { 
this  (  name  ,  patch  ,  new   JPanel  (  new   GridBagLayout  (  )  )  )  ; 
} 

protected   PatchEditorFrame  (  String   name  ,  ISinglePatch   patch  ,  JPanel   panel  )  { 
super  (  PatchEdit  .  getDesktop  (  )  ,  name  )  ; 
nFrame  ++  ; 
p  =  patch  ; 
originalPatch  =  (  IPatch  )  p  .  clone  (  )  ; 
scrollPane  =  panel  ; 
scroller  =  new   JScrollPane  (  scrollPane  )  ; 
getContentPane  (  )  .  add  (  scroller  )  ; 
faderInEnable  (  AppConfig  .  getFaderEnable  (  )  )  ; 
scroller  .  getVerticalScrollBar  (  )  .  addMouseListener  (  new   MouseAdapter  (  )  { 

public   void   mousePressed  (  MouseEvent   e  )  { 
} 

public   void   mouseReleased  (  MouseEvent   e  )  { 
repaint  (  )  ; 
} 
}  )  ; 
scroller  .  getHorizontalScrollBar  (  )  .  addMouseListener  (  new   MouseAdapter  (  )  { 

public   void   mousePressed  (  MouseEvent   e  )  { 
} 

public   void   mouseReleased  (  MouseEvent   e  )  { 
repaint  (  )  ; 
} 
}  )  ; 
addJSLFrameListener  (  new   JSLFrameListener  (  )  { 

public   void   JSLFrameClosing  (  JSLFrameEvent   e  )  { 
frameClosing  (  )  ; 
} 

public   void   JSLFrameOpened  (  JSLFrameEvent   e  )  { 
frameOpened  (  )  ; 
} 

public   void   JSLFrameActivated  (  JSLFrameEvent   e  )  { 
frameActivated  (  )  ; 
} 

public   void   JSLFrameClosed  (  JSLFrameEvent   e  )  { 
} 

public   void   JSLFrameDeactivated  (  JSLFrameEvent   e  )  { 
frameDeactivated  (  )  ; 
} 

public   void   JSLFrameDeiconified  (  JSLFrameEvent   e  )  { 
} 

public   void   JSLFrameIconified  (  JSLFrameEvent   e  )  { 
} 
}  )  ; 
} 

public   void   setVisible  (  boolean   b  )  { 
if  (  b  )  { 
numFaderBanks  =  getNumFaderBank  (  )  ; 
ErrorMsg  .  reportStatus  (  "PatchEditorFrame.show(): Num Fader Banks = "  +  numFaderBanks  )  ; 
faderHighlight  (  )  ; 
pack  (  )  ; 
Dimension   screenSize  =  PatchEdit  .  getDesktop  (  )  .  getSize  (  )  ; 
Dimension   frameSize  =  this  .  getSize  (  )  ; 
ErrorMsg  .  reportStatus  (  "PatchEditorFrame.setVisible(): scrollPane size = "  +  scrollPane  .  getSize  (  )  +  ", frame size = "  +  frameSize  )  ; 
if  (  frameSize  .  height  >  screenSize  .  height  )  { 
frameSize  .  width  +=  scroller  .  getVerticalScrollBar  (  )  .  getPreferredSize  (  )  .  width  ; 
this  .  setSize  (  frameSize  .  width  ,  screenSize  .  height  )  ; 
} 
if  (  frameSize  .  width  >  screenSize  .  width  )  { 
frameSize  .  height  +=  scroller  .  getHorizontalScrollBar  (  )  .  getPreferredSize  (  )  .  height  ; 
if  (  frameSize  .  height  >  screenSize  .  height  )  frameSize  .  height  =  screenSize  .  height  ; 
this  .  setSize  (  screenSize  .  width  ,  frameSize  .  height  )  ; 
} 
} 
super  .  setVisible  (  b  )  ; 
} 





protected   void   frameClosing  (  )  { 
ErrorMsg  .  reportStatus  (  "###PE.FrameCloseing: nFrame = "  +  nFrame  )  ; 
nFrame  --  ; 
sendPatchOnActivated  =  (  nFrame  <  2  )  ; 
String  [  ]  choices  =  new   String  [  ]  {  "Keep Changes"  ,  "Revert to Original"  ,  "Place Changed Version on Clipboard"  }  ; 
int   choice  ; 
do  { 
choice  =  JOptionPane  .  showOptionDialog  (  (  Component  )  null  ,  "What do you wish to do with the changed copy of the Patch?"  ,  "Save Changes?"  ,  JOptionPane  .  OK_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  ,  null  ,  choices  ,  choices  [  0  ]  )  ; 
}  while  (  choice  ==  JOptionPane  .  CLOSED_OPTION  )  ; 
if  (  choice  ==  0  )  { 
if  (  bankFrame  !=  null  )  bankFrame  .  setPatchAt  (  p  ,  patchRow  ,  patchCol  )  ; 
}  else  { 
if  (  choice  ==  2  )  copySelectedPatch  (  )  ; 
p  .  useSysexFromPatch  (  originalPatch  )  ; 
} 
} 





protected   void   frameOpened  (  )  { 
} 




protected   void   frameActivated  (  )  { 
ErrorMsg  .  reportStatus  (  "###PE.FrameActivated: nFrame = "  +  nFrame  )  ; 
if  (  sendPatchOnActivated  ||  nFrame  >  1  )  { 
sendPatchOnActivated  =  false  ; 
p  .  send  (  )  ; 
} 
Actions  .  setEnabled  (  false  ,  Actions  .  EN_ALL  )  ; 
Actions  .  setEnabled  (  true  ,  Actions  .  EN_COPY  |  Actions  .  EN_PLAY  |  Actions  .  EN_SEND  |  Actions  .  EN_SEND_TO  |  Actions  .  EN_REASSIGN  )  ; 
Actions  .  setEnabled  (  Toolkit  .  getDefaultToolkit  (  )  .  getSystemClipboard  (  )  .  getContents  (  this  )  !=  null  ,  Actions  .  EN_PASTE  )  ; 
} 




protected   void   frameDeactivated  (  )  { 
Actions  .  setEnabled  (  false  ,  Actions  .  EN_ALL  )  ; 
} 

public   ArrayList   getPatchCollection  (  )  { 
return   null  ; 
} 

public   void   importPatch  (  File   file  )  throws   FileNotFoundException  { 
} 

public   void   exportPatch  (  File   file  )  throws   FileNotFoundException  { 
} 

public   void   deleteSelectedPatch  (  )  { 
} 

public   void   copySelectedPatch  (  )  { 
ClipboardUtil  .  storePatch  (  p  )  ; 
} 

public   IPatch   getSelectedPatch  (  )  { 
return   p  ; 
} 

public   void   sendSelectedPatch  (  )  { 
p  .  send  (  )  ; 
} 

public   void   sendToSelectedPatch  (  )  { 
new   SysexSendToDialog  (  p  )  ; 
} 

public   void   reassignSelectedPatch  (  )  { 
new   ReassignPatchDialog  (  p  )  ; 
} 

public   void   playSelectedPatch  (  )  { 
p  .  send  (  )  ; 
p  .  play  (  )  ; 
} 

public   void   storeSelectedPatch  (  )  { 
} 

public   JSLFrame   editSelectedPatch  (  )  { 
return   null  ; 
} 

public   void   pastePatch  (  )  { 
} 

public   void   pastePatch  (  IPatch   _p  )  { 
} 

public   void   pastePatch  (  IPatch   _p  ,  int   bankNum  ,  int   patchNum  )  { 
} 


















protected   void   addWidget  (  JComponent   parent  ,  SysexWidget   widget  ,  int   gridx  ,  int   gridy  ,  int   gridwidth  ,  int   gridheight  ,  int   anchor  ,  int   fill  ,  int   slidernum  )  { 
try  { 
gbc  .  gridx  =  gridx  ; 
gbc  .  gridy  =  gridy  ; 
gbc  .  gridwidth  =  gridwidth  ; 
gbc  .  gridheight  =  gridheight  ; 
gbc  .  anchor  =  anchor  ; 
gbc  .  fill  =  fill  ; 
parent  .  add  (  widget  ,  gbc  )  ; 
widgetList  .  add  (  widget  )  ; 
widget  .  setSliderNum  (  slidernum  )  ; 
}  catch  (  Exception   e  )  { 
ErrorMsg  .  reportStatus  (  e  )  ; 
} 
} 










protected   void   addWidget  (  JComponent   parent  ,  SysexWidget   widget  ,  int   gridx  ,  int   gridy  ,  int   gridwidth  ,  int   gridheight  ,  int   slidernum  )  { 
this  .  addWidget  (  parent  ,  widget  ,  gridx  ,  gridy  ,  gridwidth  ,  gridheight  ,  GridBagConstraints  .  NORTHEAST  ,  GridBagConstraints  .  HORIZONTAL  ,  slidernum  )  ; 
} 










protected   void   addWidget  (  SysexWidget   widget  ,  int   gridx  ,  int   gridy  ,  int   gridwidth  ,  int   gridheight  ,  int   slidernum  )  { 
this  .  addWidget  (  scrollPane  ,  widget  ,  gridx  ,  gridy  ,  gridwidth  ,  gridheight  ,  GridBagConstraints  .  EAST  ,  GridBagConstraints  .  BOTH  ,  slidernum  )  ; 
} 

private   Transmitter   trns  ; 

private   Receiver   rcvr  ; 

private   void   faderInEnable  (  boolean   enable  )  { 
if  (  enable  )  { 
trns  =  MidiUtil  .  getTransmitter  (  AppConfig  .  getFaderPort  (  )  )  ; 
rcvr  =  new   FaderReceiver  (  )  ; 
trns  .  setReceiver  (  rcvr  )  ; 
}  else  { 
if  (  trns  !=  null  )  trns  .  close  (  )  ; 
if  (  rcvr  !=  null  )  rcvr  .  close  (  )  ; 
} 
} 

private   class   FaderReceiver   implements   Receiver  { 

public   void   close  (  )  { 
} 














public   void   send  (  MidiMessage   message  ,  long   timeStamp  )  { 
if  (  !  isSelected  (  )  )  return  ; 
ShortMessage   msg  =  (  ShortMessage  )  message  ; 
if  (  msg  .  getCommand  (  )  ==  ShortMessage  .  CONTROL_CHANGE  )  { 
int   channel  =  msg  .  getChannel  (  )  ; 
int   controller  =  msg  .  getData1  (  )  ; 
ErrorMsg  .  reportStatus  (  "FaderReceiver: channel: "  +  channel  +  ", control: "  +  controller  +  ", value: "  +  msg  .  getData2  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  Constants  .  NUM_FADERS  ;  i  ++  )  { 
if  (  (  AppConfig  .  getFaderChannel  (  i  )  ==  channel  )  &&  (  AppConfig  .  getFaderControl  (  i  )  ==  controller  )  )  { 
faderMoved  (  i  ,  msg  .  getData2  (  )  )  ; 
break  ; 
} 
} 
} 
} 
} 












private   void   faderMoved  (  int   fader  ,  int   value  )  { 
ErrorMsg  .  reportStatus  (  "FaderMoved: fader: "  +  fader  +  ", value: "  +  value  )  ; 
if  (  fader  ==  32  )  { 
nextFader  (  )  ; 
return  ; 
}  else   if  (  fader  ==  31  )  { 
prevFader  (  )  ; 
return  ; 
}  else   if  (  fader  >  16  )  fader  =  (  byte  )  (  0  -  (  fader  -  16  )  -  (  faderBank  *  16  )  )  ;  else   fader  +=  (  faderBank  *  16  )  ; 
if  (  recentWidget  !=  null  )  { 
SysexWidget   w  =  recentWidget  ; 
if  (  fader  ==  faderBank  *  16  )  fader  =  lastFader  ; 
if  (  w  .  getSliderNum  (  )  ==  fader  &&  w  .  isShowing  (  )  )  { 
if  (  w  .  getNumFaders  (  )  ==  1  )  w  .  setValue  (  (  int  )  (  w  .  getValueMin  (  )  +  (  (  float  )  (  value  )  /  127  *  (  w  .  getValueMax  (  )  -  w  .  getValueMin  (  )  )  )  )  )  ;  else   w  .  setFaderValue  (  fader  ,  value  )  ; 
w  .  repaint  (  )  ; 
return  ; 
} 
} 
lastFader  =  fader  ; 
for  (  int   i  =  0  ;  i  <  widgetList  .  size  (  )  ;  i  ++  )  { 
SysexWidget   w  =  (  SysexWidget  )  widgetList  .  get  (  i  )  ; 
if  (  (  w  .  getSliderNum  (  )  ==  fader  ||  (  w  .  getSliderNum  (  )  <  fader  &&  w  .  getSliderNum  (  )  +  w  .  getNumFaders  (  )  >  fader  )  )  &&  w  .  isShowing  (  )  )  { 
if  (  w  .  getNumFaders  (  )  ==  1  )  w  .  setValue  (  (  int  )  (  w  .  getValueMin  (  )  +  (  (  float  )  (  value  )  /  127  *  (  w  .  getValueMax  (  )  -  w  .  getValueMin  (  )  )  )  )  )  ;  else   w  .  setFaderValue  (  fader  ,  value  )  ; 
w  .  repaint  (  )  ; 
recentWidget  =  w  ; 
return  ; 
} 
} 
} 

private   static   Color   activeColor  ; 

private   static   Color   inactiveColor  ; 

static  { 
activeColor  =  UIManager  .  getColor  (  "controlText"  )  ; 
if  (  activeColor  ==  null  )  activeColor  =  new   Color  (  75  ,  75  ,  100  )  ; 
inactiveColor  =  UIManager  .  getColor  (  "textInactiveText"  )  ; 
if  (  inactiveColor  ==  null  )  inactiveColor  =  new   Color  (  102  ,  102  ,  153  )  ; 
} 














private   void   faderHighlight  (  )  { 
for  (  int   i  =  0  ;  i  <  widgetList  .  size  (  )  ;  i  ++  )  { 
SysexWidget   w  =  (  SysexWidget  )  widgetList  .  get  (  i  )  ; 
if  (  w  .  getLabel  (  )  !=  null  )  { 
if  (  (  (  Math  .  abs  (  w  .  getSliderNum  (  )  -  1  )  &  0xf0  )  )  ==  faderBank  *  16  )  { 
w  .  getJLabel  (  )  .  setForeground  (  activeColor  )  ; 
}  else  { 
w  .  getJLabel  (  )  .  setForeground  (  inactiveColor  )  ; 
} 
w  .  getJLabel  (  )  .  repaint  (  )  ; 
} 
} 
} 

void   nextFader  (  )  { 
faderBank  =  (  faderBank  +  1  )  %  numFaderBanks  ; 
faderHighlight  (  )  ; 
} 

void   prevFader  (  )  { 
faderBank  =  faderBank  -  1  ; 
if  (  faderBank  <  0  )  faderBank  =  numFaderBanks  -  1  ; 
faderHighlight  (  )  ; 
} 





private   int   getNumFaderBank  (  )  { 
int   high  =  0  ; 
for  (  int   i  =  0  ;  i  <  widgetList  .  size  (  )  ;  i  ++  )  { 
SysexWidget   w  =  (  SysexWidget  )  widgetList  .  get  (  i  )  ; 
if  (  (  w  .  getSliderNum  (  )  +  w  .  getNumFaders  (  )  -  1  )  >  high  )  high  =  w  .  getSliderNum  (  )  +  w  .  getNumFaders  (  )  -  1  ; 
} 
return  (  high  /  16  )  +  1  ; 
} 





public   void   setBankEditorInformation  (  BankEditorFrame   bf  ,  int   row  ,  int   col  )  { 
bankFrame  =  bf  ; 
patchRow  =  row  ; 
patchCol  =  col  ; 
} 

void   revalidateDriver  (  )  { 
p  .  setDriver  (  )  ; 
if  (  p  .  hasNullDriver  (  )  )  { 
try  { 
setClosed  (  true  )  ; 
}  catch  (  PropertyVetoException   e  )  { 
ErrorMsg  .  reportStatus  (  e  )  ; 
} 
return  ; 
} 
} 




public   IPatch   getPatch  (  )  { 
return   p  ; 
} 
} 

