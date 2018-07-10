package   com  .  tms  .  webservices  .  applications  ; 

import   java  .  awt  .  BorderLayout  ; 
import   java  .  awt  .  Cursor  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  FlowLayout  ; 
import   java  .  awt  .  GridLayout  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  KeyEvent  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PipedInputStream  ; 
import   java  .  io  .  PipedOutputStream  ; 
import   java  .  io  .  PipedReader  ; 
import   java  .  io  .  PipedWriter  ; 
import   java  .  io  .  Writer  ; 
import   javax  .  swing  .  AbstractAction  ; 
import   javax  .  swing  .  BorderFactory  ; 
import   javax  .  swing  .  KeyStroke  ; 
import   javax  .  swing  .  JButton  ; 
import   javax  .  swing  .  JComponent  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JInternalFrame  ; 
import   javax  .  swing  .  JLabel  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JPasswordField  ; 
import   javax  .  swing  .  JScrollPane  ; 
import   javax  .  swing  .  JTabbedPane  ; 
import   javax  .  swing  .  JTextArea  ; 
import   javax  .  swing  .  JTextField  ; 
import   javax  .  swing  .  Spring  ; 
import   javax  .  swing  .  SpringLayout  ; 
import   javax  .  swing  .  SwingUtilities  ; 


















public   class   XTVDFrame   extends   JInternalFrame  { 





private   static   final   String   NEWLINE  =  "\n"  ; 





private   XTVDClient   xtvdClient  =  null  ; 





private   JTextField   userNameField  =  null  ; 





private   JPasswordField   passwordField  =  null  ; 





private   JTextField   startTimeField  =  null  ; 





private   JTextField   endTimeField  =  null  ; 





private   JTextField   webserviceURIField  =  null  ; 




private   JButton   sendButton  =  null  ; 





private   JButton   saveXTVDButton  =  null  ; 





private   JButton   savePropertiesButton  =  null  ; 





private   JTextArea   outputArea  =  null  ; 





private   JTextArea   logArea  =  null  ; 





private   JFileChooser   fileChooser  =  null  ; 





private   JLabel   messageLabel  =  null  ; 





private   volatile   boolean   successfulDownload  =  false  ; 














public   XTVDFrame  (  String   title  ,  XTVDClient   xtvdClient  )  { 
super  (  title  ,  true  ,  true  ,  true  ,  true  )  ; 
this  .  xtvdClient  =  xtvdClient  ; 
fileChooser  =  new   JFileChooser  (  )  ; 
fileChooser  .  setDialogTitle  (  "Save XTVD Document"  )  ; 
fileChooser  .  setMultiSelectionEnabled  (  false  )  ; 
buildInterface  (  )  ; 
pack  (  )  ; 
} 






private   void   buildInterface  (  )  { 
JTabbedPane   tabbedPane  =  new   JTabbedPane  (  )  ; 
tabbedPane  .  addTab  (  "Input Parameters"  ,  null  ,  createInputAreas  (  )  ,  "Input parameters area."  )  ; 
tabbedPane  .  setMnemonicAt  (  0  ,  KeyEvent  .  VK_I  )  ; 
tabbedPane  .  addTab  (  "XTVD Document"  ,  null  ,  createOutputArea  (  )  ,  "XTVD Response area."  )  ; 
tabbedPane  .  setMnemonicAt  (  1  ,  KeyEvent  .  VK_D  )  ; 
tabbedPane  .  addTab  (  "Server Messages"  ,  null  ,  createLogArea  (  )  ,  "XTVD log area."  )  ; 
tabbedPane  .  setMnemonicAt  (  2  ,  KeyEvent  .  VK_M  )  ; 
getContentPane  (  )  .  add  (  tabbedPane  ,  BorderLayout  .  CENTER  )  ; 
} 









private   JComponent   createInputAreas  (  )  { 
JPanel   contentPanel  =  new   JPanel  (  new   BorderLayout  (  )  )  ; 
JLabel   userNameLabel  =  new   JLabel  (  "User Name"  ,  JLabel  .  TRAILING  )  ; 
userNameField  =  new   JTextField  (  xtvdClient  .  userName  )  ; 
userNameLabel  .  setLabelFor  (  userNameField  )  ; 
JLabel   passwordLabel  =  new   JLabel  (  "Password"  ,  JLabel  .  TRAILING  )  ; 
passwordField  =  new   JPasswordField  (  xtvdClient  .  password  )  ; 
passwordLabel  .  setLabelFor  (  passwordField  )  ; 
JLabel   startTimeLabel  =  new   JLabel  (  "Start Time"  ,  JLabel  .  TRAILING  )  ; 
startTimeField  =  new   JTextField  (  xtvdClient  .  sdf  .  format  (  xtvdClient  .  start  .  getTime  (  )  )  )  ; 
startTimeLabel  .  setLabelFor  (  startTimeField  )  ; 
JLabel   endTimeLabel  =  new   JLabel  (  "End Time"  ,  JLabel  .  TRAILING  )  ; 
endTimeField  =  new   JTextField  (  xtvdClient  .  sdf  .  format  (  xtvdClient  .  end  .  getTime  (  )  )  )  ; 
endTimeLabel  .  setLabelFor  (  endTimeField  )  ; 
JLabel   uriLabel  =  new   JLabel  (  "Webservice URI"  ,  JLabel  .  TRAILING  )  ; 
webserviceURIField  =  new   JTextField  (  xtvdClient  .  webserviceURI  )  ; 
uriLabel  .  setLabelFor  (  webserviceURIField  )  ; 
JPanel   inputPanel  =  new   JPanel  (  new   SpringLayout  (  )  )  ; 
inputPanel  .  add  (  userNameLabel  )  ; 
inputPanel  .  add  (  userNameField  )  ; 
inputPanel  .  add  (  passwordLabel  )  ; 
inputPanel  .  add  (  passwordField  )  ; 
inputPanel  .  add  (  startTimeLabel  )  ; 
inputPanel  .  add  (  startTimeField  )  ; 
inputPanel  .  add  (  endTimeLabel  )  ; 
inputPanel  .  add  (  endTimeField  )  ; 
inputPanel  .  add  (  uriLabel  )  ; 
inputPanel  .  add  (  webserviceURIField  )  ; 
SpringUtilities  .  makeCompactGrid  (  inputPanel  ,  5  ,  2  ,  6  ,  6  ,  6  ,  6  )  ; 
contentPanel  .  add  (  inputPanel  ,  BorderLayout  .  NORTH  )  ; 
JPanel   messagePanel  =  new   JPanel  (  new   GridLayout  (  1  ,  1  )  )  ; 
messagePanel  .  setBorder  (  BorderFactory  .  createLoweredBevelBorder  (  )  )  ; 
messageLabel  =  new   JLabel  (  "Click \"Send Request\" to download data."  ,  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
messagePanel  .  add  (  messageLabel  )  ; 
contentPanel  .  add  (  messagePanel  ,  BorderLayout  .  CENTER  )  ; 
createButtons  (  contentPanel  )  ; 
return   contentPanel  ; 
} 













private   JComponent   createOutputArea  (  )  { 
JPanel   panel  =  new   JPanel  (  new   BorderLayout  (  )  )  ; 
outputArea  =  new   JTextArea  (  )  ; 
outputArea  .  setEditable  (  false  )  ; 
JScrollPane   scrollPane  =  new   JScrollPane  (  outputArea  )  ; 
scrollPane  .  setPreferredSize  (  new   Dimension  (  600  ,  400  )  )  ; 
panel  .  add  (  scrollPane  ,  BorderLayout  .  CENTER  )  ; 
JTextField   searchField  =  new   JTextField  (  25  )  ; 
FindAction   action  =  new   FindAction  (  searchField  ,  outputArea  )  ; 
FindNextAction   nextAction  =  new   FindNextAction  (  searchField  ,  outputArea  )  ; 
JButton   find  =  new   JButton  (  action  )  ; 
JButton   findNext  =  new   JButton  (  nextAction  )  ; 
searchField  .  setToolTipText  (  "Input text to search for."  )  ; 
searchField  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
searchField  .  getActionMap  (  )  .  put  (  "pressed"  ,  nextAction  )  ; 
find  .  setText  (  "Find"  )  ; 
find  .  setToolTipText  (  "Find first instance of specified text."  )  ; 
find  .  setMnemonic  (  KeyEvent  .  VK_F  )  ; 
find  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
find  .  getActionMap  (  )  .  put  (  "pressed"  ,  action  )  ; 
findNext  .  setText  (  "Find Next"  )  ; 
findNext  .  setToolTipText  (  "Find next instance of specified text from cursor location."  )  ; 
findNext  .  setMnemonic  (  KeyEvent  .  VK_N  )  ; 
findNext  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
findNext  .  getActionMap  (  )  .  put  (  "pressed"  ,  nextAction  )  ; 
JPanel   findPanel  =  new   JPanel  (  )  ; 
findPanel  .  add  (  searchField  )  ; 
findPanel  .  add  (  find  )  ; 
findPanel  .  add  (  findNext  )  ; 
panel  .  add  (  findPanel  ,  BorderLayout  .  SOUTH  )  ; 
return   panel  ; 
} 









private   JComponent   createLogArea  (  )  { 
logArea  =  new   JTextArea  (  )  ; 
logArea  .  setEditable  (  false  )  ; 
JScrollPane   scrollPane  =  new   JScrollPane  (  logArea  )  ; 
scrollPane  .  setPreferredSize  (  new   Dimension  (  600  ,  400  )  )  ; 
return   scrollPane  ; 
} 











private   void   createButtons  (  JPanel   panel  )  { 
JPanel   buttonPanel  =  new   JPanel  (  new   FlowLayout  (  )  )  ; 
createSendButton  (  )  ; 
buttonPanel  .  add  (  sendButton  )  ; 
createSaveXTVDButton  (  )  ; 
buttonPanel  .  add  (  saveXTVDButton  )  ; 
createSavePropertiesButton  (  )  ; 
buttonPanel  .  add  (  savePropertiesButton  )  ; 
panel  .  add  (  buttonPanel  ,  BorderLayout  .  SOUTH  )  ; 
} 







private   void   createSendButton  (  )  { 
RequestAction   action  =  new   RequestAction  (  )  ; 
sendButton  =  new   JButton  (  action  )  ; 
sendButton  .  setText  (  "Send Request"  )  ; 
sendButton  .  setToolTipText  (  "Send a new download request to the XTVD webservice."  )  ; 
sendButton  .  setMnemonic  (  KeyEvent  .  VK_R  )  ; 
sendButton  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
sendButton  .  getActionMap  (  )  .  put  (  "pressed"  ,  action  )  ; 
} 







private   void   createSaveXTVDButton  (  )  { 
SaveXTVDAction   action  =  new   SaveXTVDAction  (  )  ; 
saveXTVDButton  =  new   JButton  (  action  )  ; 
saveXTVDButton  .  setText  (  "Save XTVD"  )  ; 
saveXTVDButton  .  setToolTipText  (  "Save the downloaded XTVD document."  )  ; 
saveXTVDButton  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
saveXTVDButton  .  setEnabled  (  false  )  ; 
saveXTVDButton  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
saveXTVDButton  .  getActionMap  (  )  .  put  (  "pressed"  ,  action  )  ; 
} 







private   void   createSavePropertiesButton  (  )  { 
SavePropertiesAction   action  =  new   SavePropertiesAction  (  )  ; 
savePropertiesButton  =  new   JButton  (  action  )  ; 
savePropertiesButton  .  setText  (  "Save Properties"  )  ; 
savePropertiesButton  .  setToolTipText  (  "Save the current request parameters as default."  )  ; 
savePropertiesButton  .  setMnemonic  (  KeyEvent  .  VK_P  )  ; 
savePropertiesButton  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  "ENTER"  )  ,  "pressed"  )  ; 
savePropertiesButton  .  getActionMap  (  )  .  put  (  "pressed"  ,  action  )  ; 
} 






class   RequestThread   extends   Thread  { 





private   Writer   out  ; 








public   RequestThread  (  Writer   writer  )  { 
out  =  writer  ; 
} 







public   void   run  (  )  { 
try  { 
xtvdClient  .  soapRequest  .  getData  (  xtvdClient  .  start  ,  xtvdClient  .  end  ,  out  )  ; 
out  .  close  (  )  ; 
successfulDownload  =  true  ; 
}  catch  (  Throwable   t  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  t  )  )  ; 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   ioex  )  { 
} 
successfulDownload  =  false  ; 
} 
} 
} 






class   ResponseThread   extends   Thread  { 





private   BufferedReader   reader  ; 





public   ResponseThread  (  BufferedReader   reader  )  { 
this  .  reader  =  reader  ; 
} 







public   void   run  (  )  { 
displayDownloadingMessage  (  )  ; 
try  { 
String   line  =  ""  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
outputArea  .  append  (  line  )  ; 
outputArea  .  append  (  XTVDFrame  .  NEWLINE  )  ; 
} 
reader  .  close  (  )  ; 
}  catch  (  Throwable   t  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  t  )  )  ; 
successfulDownload  =  false  ; 
} 
displayDownloadStatus  (  )  ; 
} 





private   void   displayDownloadingMessage  (  )  { 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
messageLabel  .  setText  (  "Downloading data."  )  ; 
outputArea  .  setCursor  (  new   Cursor  (  Cursor  .  WAIT_CURSOR  )  )  ; 
outputArea  .  setText  (  ""  )  ; 
} 
}  )  ; 
} 





private   void   displayDownloadStatus  (  )  { 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  successfulDownload  )  { 
messageLabel  .  setText  (  "Finished! Click \"Save XTVD\" to save the downloaded document."  )  ; 
saveXTVDButton  .  setEnabled  (  true  )  ; 
}  else  { 
messageLabel  .  setText  (  "Failed! View the \"Server Messages\" area for error information."  )  ; 
} 
sendButton  .  setEnabled  (  true  )  ; 
outputArea  .  setCursor  (  new   Cursor  (  Cursor  .  DEFAULT_CURSOR  )  )  ; 
} 
}  )  ; 
} 
} 








class   RequestAction   extends   AbstractAction  { 








public   void   actionPerformed  (  ActionEvent   event  )  { 
try  { 
String   userName  =  userNameField  .  getText  (  )  ; 
String   password  =  new   String  (  passwordField  .  getPassword  (  )  )  ; 
if  (  userName  ==  null  ||  userName  .  equals  (  ""  )  ||  password  ==  null  ||  password  .  equals  (  ""  )  )  { 
JOptionPane  .  showInternalMessageDialog  (  XTVDFrame  .  this  ,  "Username or password not specified"  ,  "Missing Information"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
}  else  { 
sendButton  .  setEnabled  (  false  )  ; 
saveXTVDButton  .  setEnabled  (  false  )  ; 
xtvdClient  .  soapRequest  .  setUserName  (  userName  )  ; 
xtvdClient  .  soapRequest  .  setPassword  (  password  )  ; 
xtvdClient  .  soapRequest  .  setWebserviceURI  (  webserviceURIField  .  getText  (  )  )  ; 
xtvdClient  .  start  .  setTime  (  xtvdClient  .  sdf  .  parse  (  startTimeField  .  getText  (  )  )  )  ; 
xtvdClient  .  end  .  setTime  (  xtvdClient  .  sdf  .  parse  (  endTimeField  .  getText  (  )  )  )  ; 
try  { 
PipedWriter   out  =  new   PipedWriter  (  )  ; 
PipedReader   in  =  new   PipedReader  (  out  )  ; 
xtvdClient  .  soapRequest  .  setLog  (  out  )  ; 
new   LogReaderThread  (  new   BufferedReader  (  in  )  ,  logArea  )  .  start  (  )  ; 
}  catch  (  IOException   ioex  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  ioex  )  )  ; 
} 
PipedOutputStream   pipedOutputStream  =  new   PipedOutputStream  (  )  ; 
BufferedWriter   out  =  new   BufferedWriter  (  new   OutputStreamWriter  (  pipedOutputStream  ,  "UTF-8"  )  )  ; 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  new   PipedInputStream  (  pipedOutputStream  )  ,  "UTF-8"  )  )  ; 
new   RequestThread  (  out  )  .  start  (  )  ; 
new   ResponseThread  (  in  )  .  start  (  )  ; 
} 
}  catch  (  Throwable   t  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  t  )  )  ; 
} 
} 
} 










class   SavePropertiesAction   extends   AbstractAction  { 







public   void   actionPerformed  (  ActionEvent   event  )  { 
Object  [  ]  values  =  {  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  ,  "10"  ,  "11"  ,  "12"  ,  "13"  }  ; 
int   numberOfDays  =  xtvdClient  .  xtvdProperties  .  getNumberOfDays  (  )  ; 
String   days  =  (  String  )  JOptionPane  .  showInternalInputDialog  (  XTVDFrame  .  this  ,  "Enter the default number\nof days of data to download."  ,  "Number of Days"  ,  JOptionPane  .  PLAIN_MESSAGE  ,  null  ,  values  ,  String  .  valueOf  (  numberOfDays  )  )  ; 
if  (  days  !=  null  &&  days  .  length  (  )  >  0  )  { 
numberOfDays  =  Integer  .  parseInt  (  days  )  ; 
} 
try  { 
xtvdClient  .  xtvdProperties  .  saveProperties  (  userNameField  .  getText  (  )  ,  new   String  (  passwordField  .  getPassword  (  )  )  ,  numberOfDays  ,  webserviceURIField  .  getText  (  )  )  ; 
}  catch  (  DataDirectException   ddex  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  ddex  )  )  ; 
} 
} 
} 








class   SaveXTVDAction   extends   AbstractAction  { 






public   void   actionPerformed  (  ActionEvent   event  )  { 
sendButton  .  setEnabled  (  false  )  ; 
saveXTVDButton  .  setEnabled  (  false  )  ; 
int   value  =  fileChooser  .  showSaveDialog  (  XTVDFrame  .  this  )  ; 
if  (  value  ==  JFileChooser  .  APPROVE_OPTION  )  { 
try  { 
BufferedWriter   writer  =  new   BufferedWriter  (  new   OutputStreamWriter  (  new   FileOutputStream  (  fileChooser  .  getSelectedFile  (  )  )  ,  "UTF-8"  )  )  ; 
writer  .  write  (  outputArea  .  getText  (  )  )  ; 
writer  .  flush  (  )  ; 
writer  .  close  (  )  ; 
}  catch  (  IOException   ioex  )  { 
logArea  .  append  (  DataDirectException  .  getStackTraceString  (  ioex  )  )  ; 
} 
} 
sendButton  .  setEnabled  (  true  )  ; 
saveXTVDButton  .  setEnabled  (  true  )  ; 
} 
} 
} 

