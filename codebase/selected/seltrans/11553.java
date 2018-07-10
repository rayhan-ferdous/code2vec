package   org  .  statcato  ; 

import   org  .  statcato  .  file  .  FileChooserUtils  ; 
import   org  .  statcato  .  file  .  ExtensionFileFilter  ; 
import   javax  .  swing  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   javax  .  swing  .  text  .  html  .  *  ; 
import   javax  .  swing  .  text  .  *  ; 
import   java  .  io  .  *  ; 
import   org  .  statcato  .  utils  .  *  ; 
import   org  .  statcato  .  Statcato  .  *  ; 








public   class   LogWindow   extends   JTextPane   implements   Serializable  { 

File   savedFile  =  null  ; 

boolean   changed  =  false  ; 

transient   Statcato   app  =  null  ; 






public   LogWindow  (  Statcato   mTab  )  { 
super  (  )  ; 
app  =  mTab  ; 
initialize  (  )  ; 
addKeyListener  (  new   KeyAdapter  (  )  { 

@  Override 
public   void   keyTyped  (  KeyEvent   e  )  { 
if  (  e  .  getKeyChar  (  )  ==  '\n'  )  { 
HTMLDocument   doc  =  (  HTMLDocument  )  getDocument  (  )  ; 
HTMLEditorKit   editorKit  =  (  HTMLEditorKit  )  getEditorKit  (  )  ; 
try  { 
editorKit  .  insertHTML  (  doc  ,  getCaretPosition  (  )  ,  "<br>"  ,  0  ,  0  ,  null  )  ; 
int   pos  =  getCaretPosition  (  )  ; 
setChangedStatus  (  )  ; 
String   txt  =  getText  (  )  ; 
setText  (  ""  )  ; 
setText  (  txt  )  ; 
setCaretPosition  (  pos  -  2  )  ; 
}  catch  (  BadLocationException   ex  )  { 
System  .  out  .  println  (  ex  )  ; 
}  catch  (  IOException   ex  )  { 
System  .  out  .  println  (  ex  )  ; 
} 
} 
if  (  !  e  .  isControlDown  (  )  )  setChangedStatus  (  )  ; 
} 

@  Override 
public   void   keyPressed  (  KeyEvent   e  )  { 
if  (  e  .  isControlDown  (  )  &&  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_V  )  { 
setChangedStatus  (  )  ; 
} 
} 

@  Override 
public   void   keyReleased  (  KeyEvent   e  )  { 
} 
}  )  ; 
} 





private   void   initialize  (  )  { 
setContentType  (  "text/html"  )  ; 
setText  (  ""  )  ; 
String   WelcomeText  =  "Welcome to Statcato!<br> "  ; 
WelcomeText  +=  HelperFunctions  .  getDateTime  (  )  +  "<hr size='1'>"  ; 
setText  (  WelcomeText  )  ; 
int   pos  =  getCaretPosition  (  )  ; 
addUnformattedText  (  ""  )  ; 
setCaretPosition  (  pos  )  ; 
setUnchangedStatus  (  )  ; 
setEditable  (  true  )  ; 
StyleSheet   sheet  =  new   HTMLEditorKit  (  )  .  getStyleSheet  (  )  ; 
sheet  .  addRule  (  "body { font-family: san-serif;}"  )  ; 
} 









public   void   addParagraph  (  String   heading  ,  String   message  )  { 
message  =  "<br><b>"  +  heading  +  "</b><br>"  +  message  .  replace  (  "\n"  ,  ""  )  +  "<br>"  ; 
HTMLDocument   doc  =  (  HTMLDocument  )  getDocument  (  )  ; 
HTMLEditorKit   editorKit  =  (  HTMLEditorKit  )  getEditorKit  (  )  ; 
try  { 
editorKit  .  insertHTML  (  doc  ,  doc  .  getLength  (  )  ,  message  ,  0  ,  0  ,  null  )  ; 
setChangedStatus  (  )  ; 
}  catch  (  BadLocationException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
} 







public   void   addText  (  String   message  )  { 
message  =  message  .  replace  (  "\n"  ,  ""  )  +  "<br>"  ; 
HTMLDocument   doc  =  (  HTMLDocument  )  getDocument  (  )  ; 
HTMLEditorKit   editorKit  =  (  HTMLEditorKit  )  getEditorKit  (  )  ; 
try  { 
editorKit  .  insertHTML  (  doc  ,  doc  .  getLength  (  )  ,  message  ,  0  ,  0  ,  null  )  ; 
setChangedStatus  (  )  ; 
}  catch  (  BadLocationException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
} 







public   void   addUnformattedText  (  String   message  )  { 
HTMLDocument   doc  =  (  HTMLDocument  )  getDocument  (  )  ; 
HTMLEditorKit   editorKit  =  (  HTMLEditorKit  )  getEditorKit  (  )  ; 
try  { 
editorKit  .  insertHTML  (  doc  ,  doc  .  getLength  (  )  ,  message  .  replace  (  "\n"  ,  ""  )  ,  0  ,  0  ,  null  )  ; 
setChangedStatus  (  )  ; 
}  catch  (  BadLocationException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
} 







public   void   addHeading  (  String   message  )  { 
message  =  "<br><b>"  +  message  +  "</b><br>"  ; 
HTMLDocument   doc  =  (  HTMLDocument  )  getDocument  (  )  ; 
HTMLEditorKit   editorKit  =  (  HTMLEditorKit  )  getEditorKit  (  )  ; 
try  { 
editorKit  .  insertHTML  (  doc  ,  doc  .  getLength  (  )  ,  message  ,  0  ,  0  ,  null  )  ; 
setChangedStatus  (  )  ; 
}  catch  (  BadLocationException   e  )  { 
}  catch  (  IOException   e  )  { 
} 
} 






public   void   setChangedStatus  (  )  { 
if  (  !  changed  )  { 
changed  =  true  ; 
app  .  setLogTitle  (  app  .  getLogTitle  (  )  +  "*"  )  ; 
} 
} 






public   void   setUnchangedStatus  (  )  { 
changed  =  false  ; 
String   title  =  app  .  getLogTitle  (  )  ; 
if  (  title  .  endsWith  (  "*"  )  )  app  .  setLogTitle  (  app  .  getLogTitle  (  )  .  substring  (  0  ,  title  .  length  (  )  -  1  )  )  ; 
} 






public   boolean   getChangedStatus  (  )  { 
return   changed  ; 
} 











public   File   writeToFile  (  JFrame   frame  ,  boolean   saveAs  )  { 
String   path  =  ""  ; 
String   extension  =  ""  ; 
if  (  savedFile  !=  null  &&  !  saveAs  )  { 
path  =  savedFile  .  getPath  (  )  ; 
extension  =  FileChooserUtils  .  getExtension  (  savedFile  )  ; 
writeFileHelper  (  frame  ,  path  )  ; 
return   savedFile  ; 
}  else  { 
JFileChooser   fc  =  new   JFileChooser  (  )  ; 
ExtensionFileFilter   HTMLFilter  =  new   ExtensionFileFilter  (  "HTML (*.html)"  ,  "html"  )  ; 
fc  .  addChoosableFileFilter  (  HTMLFilter  )  ; 
fc  .  setAcceptAllFileFilterUsed  (  false  )  ; 
int   returnValue  =  fc  .  showSaveDialog  (  frame  )  ; 
if  (  returnValue  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
path  =  file  .  getPath  (  )  ; 
extension  =  ""  ; 
javax  .  swing  .  filechooser  .  FileFilter   filter  =  fc  .  getFileFilter  (  )  ; 
if  (  filter  .  equals  (  HTMLFilter  )  )  { 
extension  =  "html"  ; 
} 
if  (  !  path  .  toLowerCase  (  )  .  endsWith  (  "."  +  extension  )  )  { 
path  +=  "."  +  extension  ; 
file  =  new   File  (  path  )  ; 
} 
if  (  file  .  exists  (  )  )  { 
System  .  out  .  println  (  "file exists already"  )  ; 
Object  [  ]  options  =  {  "Overwrite file"  ,  "Cancel"  }  ; 
int   choice  =  JOptionPane  .  showOptionDialog  (  frame  ,  "The specified file already exists.  Overwrite existing file?"  ,  "Overwrite file?"  ,  JOptionPane  .  YES_NO_OPTION  ,  JOptionPane  .  WARNING_MESSAGE  ,  null  ,  options  ,  options  [  1  ]  )  ; 
if  (  choice  !=  0  )  return   null  ; 
} 
writeFileHelper  (  frame  ,  path  )  ; 
savedFile  =  file  ; 
return   file  ; 
} 
return   null  ; 
} 
} 








private   void   writeFileHelper  (  JFrame   frame  ,  String   path  )  { 
try  { 
String   htmlSource  =  getText  (  )  ; 
BufferedWriter   Writer  =  new   BufferedWriter  (  new   FileWriter  (  path  )  )  ; 
String  [  ]  lines  =  htmlSource  .  split  (  "\n"  )  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  ++  i  )  { 
Writer  .  write  (  lines  [  i  ]  )  ; 
Writer  .  newLine  (  )  ; 
} 
Writer  .  close  (  )  ; 
setUnchangedStatus  (  )  ; 
}  catch  (  IOException   e  )  { 
HelperFunctions  .  showErrorDialog  (  frame  ,  "Write file failed!"  )  ; 
} 
} 




public   void   clear  (  )  { 
app  .  compoundEdit  =  new   DialogEdit  (  "clear log"  )  ; 
initialize  (  )  ; 
setChangedStatus  (  )  ; 
app  .  compoundEdit  .  end  (  )  ; 
app  .  addCompoundEdit  (  app  .  compoundEdit  )  ; 
} 




public   void   overwrite  (  String   txt  )  { 
app  .  compoundEdit  =  new   DialogEdit  (  "load log"  )  ; 
setText  (  ""  )  ; 
addText  (  txt  )  ; 
setChangedStatus  (  )  ; 
app  .  compoundEdit  .  end  (  )  ; 
app  .  addCompoundEdit  (  app  .  compoundEdit  )  ; 
} 

public   void   appendLog  (  String   log  )  { 
app  .  compoundEdit  =  new   DialogEdit  (  "Append log"  )  ; 
addUnformattedText  (  log  )  ; 
setUnchangedStatus  (  )  ; 
app  .  compoundEdit  .  end  (  )  ; 
app  .  addCompoundEdit  (  app  .  compoundEdit  )  ; 
} 
} 

