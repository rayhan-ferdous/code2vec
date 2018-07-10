package   preptool  .  view  ; 

import   java  .  awt  .  GridBagConstraints  ; 
import   java  .  awt  .  GridBagLayout  ; 
import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  awt  .  event  .  KeyEvent  ; 
import   java  .  awt  .  event  .  WindowAdapter  ; 
import   java  .  awt  .  event  .  WindowEvent  ; 
import   java  .  io  .  File  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Observable  ; 
import   java  .  util  .  Observer  ; 
import   javax  .  swing  .  AbstractAction  ; 
import   javax  .  swing  .  BorderFactory  ; 
import   javax  .  swing  .  DefaultListModel  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   javax  .  swing  .  JButton  ; 
import   javax  .  swing  .  JCheckBoxMenuItem  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JLabel  ; 
import   javax  .  swing  .  JList  ; 
import   javax  .  swing  .  JMenu  ; 
import   javax  .  swing  .  JMenuBar  ; 
import   javax  .  swing  .  JMenuItem  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JPopupMenu  ; 
import   javax  .  swing  .  JScrollPane  ; 
import   javax  .  swing  .  JSplitPane  ; 
import   javax  .  swing  .  JToolBar  ; 
import   javax  .  swing  .  KeyStroke  ; 
import   javax  .  swing  .  ListSelectionModel  ; 
import   javax  .  swing  .  UIManager  ; 
import   javax  .  swing  .  WindowConstants  ; 
import   javax  .  swing  .  border  .  BevelBorder  ; 
import   javax  .  swing  .  event  .  ListSelectionEvent  ; 
import   javax  .  swing  .  event  .  ListSelectionListener  ; 
import   javax  .  swing  .  filechooser  .  FileFilter  ; 
import   javax  .  swing  .  text  .  DefaultEditorKit  ; 
import   preptool  .  controller  .  exception  .  BallotExportException  ; 
import   preptool  .  controller  .  exception  .  BallotOpenException  ; 
import   preptool  .  controller  .  exception  .  BallotPreviewException  ; 
import   preptool  .  controller  .  exception  .  BallotSaveException  ; 
import   preptool  .  model  .  Model  ; 
import   preptool  .  model  .  Party  ; 
import   preptool  .  model  .  ballot  .  ICardFactory  ; 
import   preptool  .  model  .  language  .  Language  ; 
import   preptool  .  view  .  dialog  .  LanguagesDialog  ; 
import   preptool  .  view  .  dialog  .  PartiesDialog  ; 
import   preptool  .  view  .  dragndrop  .  ListTransferHandler  ; 
import   preptool  .  view  .  dragndrop  .  ListTransferListener  ; 







public   class   View   extends   JFrame  { 

private   static   final   long   serialVersionUID  =  1L  ; 







private   JFileChooser   fileChooser  ; 




private   Model   model  ; 




private   JMenuBar   menuBar  ; 




private   JPopupMenu   addCardMenu  ; 




private   JPopupMenu   prefMenu  ; 




private   JToolBar   toolbar  ; 




private   JButton   newBallotButton  ; 




private   JButton   openBallotButton  ; 




private   JButton   saveBallotButton  ; 




private   JButton   exportBallotButton  ; 




private   JButton   previewButton  ; 




private   JButton   prefButton  ; 




private   JPanel   listPanel  ; 




private   JList   cardList  ; 




private   DefaultListModel   cardListModel  ; 




private   JToolBar   listToolbar  ; 




private   JButton   addCardButton  ; 




private   JButton   deleteCardButton  ; 




private   JButton   moveUpCardButton  ; 




private   JButton   moveDownCardButton  ; 




private   JPanel   cardPanel  ; 




private   CardView   currentCardView  ; 




private   PreviewPane   previewPanel  ; 




private   JPanel   noCardsPanel  ; 




private   LanguageBar   languageBar  ; 




private   ArrayList  <  LanguageChangeListener  >  languageChangeListeners  ; 

private   Observer   titleObserver  ; 









public   View  (  Model   m  )  { 
super  (  "VoteBox Preparation Tool"  )  ; 
model  =  m  ; 
languageChangeListeners  =  new   ArrayList  <  LanguageChangeListener  >  (  )  ; 
try  { 
UIManager  .  setLookAndFeel  (  UIManager  .  getSystemLookAndFeelClassName  (  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
System  .  setProperty  (  "apple.laf.useScreenMenuBar"  ,  "true"  )  ; 
addWindowListener  (  new   WindowAdapter  (  )  { 

@  Override 
public   void   windowClosing  (  WindowEvent   e  )  { 
exitButtonPressed  (  )  ; 
} 
}  )  ; 
setDefaultCloseOperation  (  WindowConstants  .  DO_NOTHING_ON_CLOSE  )  ; 
setSize  (  1000  ,  800  )  ; 
setLayout  (  new   GridBagLayout  (  )  )  ; 
GridBagConstraints   c  =  new   GridBagConstraints  (  )  ; 
initializePrefMenu  (  )  ; 
initializeToolbar  (  )  ; 
c  .  gridx  =  0  ; 
c  .  gridy  =  0  ; 
c  .  gridwidth  =  2  ; 
c  .  fill  =  GridBagConstraints  .  HORIZONTAL  ; 
c  .  anchor  =  GridBagConstraints  .  FIRST_LINE_START  ; 
add  (  toolbar  ,  c  )  ; 
initializeListPanel  (  )  ; 
c  .  gridy  =  1  ; 
c  .  weighty  =  0  ; 
c  .  fill  =  GridBagConstraints  .  HORIZONTAL  ; 
c  .  anchor  =  GridBagConstraints  .  LAST_LINE_START  ; 
listPanel  .  add  (  listToolbar  ,  c  )  ; 
listPanel  .  setBorder  (  BorderFactory  .  createBevelBorder  (  BevelBorder  .  LOWERED  )  )  ; 
initializeCardPanel  (  )  ; 
JSplitPane   cardSplitPane  =  new   JSplitPane  (  JSplitPane  .  VERTICAL_SPLIT  ,  true  ,  cardPanel  ,  previewPanel  )  ; 
cardSplitPane  .  setDividerLocation  (  400  )  ; 
JPanel   rightSide  =  new   JPanel  (  )  ; 
rightSide  .  setLayout  (  new   GridBagLayout  (  )  )  ; 
GridBagConstraints   c2  =  new   GridBagConstraints  (  )  ; 
c2  .  gridx  =  0  ; 
c2  .  gridy  =  0  ; 
c2  .  weightx  =  1  ; 
c2  .  weighty  =  1  ; 
c2  .  fill  =  GridBagConstraints  .  BOTH  ; 
rightSide  .  add  (  cardSplitPane  ,  c2  )  ; 
c2  .  gridy  =  1  ; 
c2  .  weighty  =  0  ; 
IMultiLanguageEditor   editor  =  new   IMultiLanguageEditor  (  )  { 

public   void   languageSelected  (  Language   lang  )  { 
if  (  currentCardView  !=  null  )  currentCardView  .  languageSelected  (  lang  )  ; 
} 

public   boolean   needsTranslation  (  Language   lang  )  { 
if  (  currentCardView  !=  null  )  return   currentCardView  .  needsTranslation  (  lang  )  ;  else   return   false  ; 
} 

public   void   updatePrimaryLanguage  (  Language   lang  )  { 
if  (  currentCardView  !=  null  )  currentCardView  .  updatePrimaryLanguage  (  lang  )  ; 
} 
}  ; 
languageBar  =  new   LanguageBar  (  this  ,  editor  ,  model  .  getLanguages  (  )  ,  model  .  getLanguages  (  )  .  get  (  0  )  )  ; 
languageBar  .  addLanguageSelectListener  (  new   Observer  (  )  { 

public   void   update  (  Observable   o  ,  Object   arg  )  { 
previewPanel  .  clear  (  )  ; 
} 
}  )  ; 
rightSide  .  add  (  languageBar  ,  c2  )  ; 
JSplitPane   splitPane  =  new   JSplitPane  (  JSplitPane  .  HORIZONTAL_SPLIT  ,  true  ,  listPanel  ,  rightSide  )  ; 
splitPane  .  setDividerLocation  (  300  )  ; 
c  .  gridx  =  0  ; 
c  .  gridy  =  1  ; 
c  .  gridwidth  =  1  ; 
c  .  gridheight  =  1  ; 
c  .  weighty  =  1  ; 
c  .  weightx  =  1  ; 
c  .  fill  =  GridBagConstraints  .  BOTH  ; 
add  (  splitPane  ,  c  )  ; 
initializeMenuBar  (  )  ; 
setJMenuBar  (  menuBar  )  ; 
initializePopupMenu  (  )  ; 
titleObserver  =  new   Observer  (  )  { 

public   void   update  (  Observable   o  ,  Object   arg  )  { 
if  (  languageBar  .  getCurrentLanguage  (  )  .  equals  (  model  .  getLanguages  (  )  .  get  (  0  )  )  )  { 
int   idx  =  cardList  .  getSelectedIndex  (  )  ; 
setCardTitle  (  model  .  getCardTitle  (  idx  )  ,  idx  )  ; 
validate  (  )  ; 
repaint  (  )  ; 
} 
} 
}  ; 
fileChooser  =  new   JFileChooser  (  )  ; 
} 





public   void   addCardButtonPressed  (  )  { 
addCardMenu  .  show  (  addCardButton  ,  0  ,  -  (  int  )  addCardMenu  .  getPreferredSize  (  )  .  getHeight  (  )  )  ; 
} 





public   void   prefButtonPressed  (  )  { 
prefMenu  .  show  (  prefButton  ,  0  ,  (  int  )  prefButton  .  getPreferredSize  (  )  .  getHeight  (  )  )  ; 
} 







public   void   addLanguageChangeListener  (  LanguageChangeListener   l  )  { 
languageChangeListeners  .  add  (  l  )  ; 
} 





public   void   cardListSelectionChanged  (  )  { 
int   idx  =  cardList  .  getSelectedIndex  (  )  ; 
if  (  idx  ==  -  1  )  { 
setCardPane  (  noCardsPanel  )  ; 
deleteCardButton  .  setEnabled  (  false  )  ; 
moveUpCardButton  .  setEnabled  (  false  )  ; 
moveDownCardButton  .  setEnabled  (  false  )  ; 
}  else  { 
setCardPane  (  new   CardView  (  this  ,  model  .  getCardType  (  idx  )  ,  model  .  getCardModules  (  idx  )  )  )  ; 
deleteCardButton  .  setEnabled  (  true  )  ; 
moveUpCardButton  .  setEnabled  (  (  idx  >  0  )  )  ; 
moveDownCardButton  .  setEnabled  (  (  idx  <  cardListModel  .  size  (  )  -  1  )  )  ; 
} 
} 





public   void   deleteCardButtonPressed  (  )  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "Are you sure you want to delete this card?"  ,  "Delete Card"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
int   idx  =  cardList  .  getSelectedIndex  (  )  ; 
int   newIdx  ; 
if  (  idx  ==  cardListModel  .  size  (  )  -  1  )  newIdx  =  idx  -  1  ;  else   newIdx  =  idx  +  1  ; 
cardList  .  setSelectedIndex  (  newIdx  )  ; 
model  .  deleteCard  (  idx  )  ; 
cardListModel  .  remove  (  idx  )  ; 
} 
} 






public   void   exitButtonPressed  (  )  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "If you exit, any changes you have made since the last time you saved the current ballot will be lost.  Save?"  ,  "Exit"  ,  JOptionPane  .  YES_NO_CANCEL_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
boolean   saved  =  saveBallotButtonPressed  (  )  ; 
if  (  saved  )  System  .  exit  (  0  )  ; 
}  else   if  (  answer  ==  JOptionPane  .  NO_OPTION  )  { 
System  .  exit  (  0  )  ; 
} 
} 






public   void   exportButtonPressed  (  )  { 
try  { 
String  [  ]  cardsNeedTranslation  =  model  .  checkTranslations  (  )  ; 
if  (  cardsNeedTranslation  .  length  >  0  )  { 
String   body  =  "You have not entered translations for all text in this ballot.\n\nThe following cards are missing translations:\n"  ; 
for  (  String   s  :  cardsNeedTranslation  )  body  +=  s  +  "\n"  ; 
body  +=  "\nContinue exporting?"  ; 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  body  ,  "Export"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  NO_OPTION  )  return  ; 
} 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "Would you like to export the ballot as a ZIP file?"  ,  "Export as ZIP"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
JFileChooser   fc  =  fileChooser  ; 
fc  .  setFileFilter  (  new   FileFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   f  )  { 
String   path  =  f  .  getAbsolutePath  (  )  ; 
return  (  f  .  isDirectory  (  )  ||  path  .  length  (  )  >  4  &&  path  .  substring  (  path  .  length  (  )  -  4  )  .  equals  (  ".zip"  )  )  ; 
} 

@  Override 
public   String   getDescription  (  )  { 
return  "Ballot export files"  ; 
} 
}  )  ; 
answer  =  fc  .  showDialog  (  this  ,  "Export"  )  ; 
if  (  answer  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
model  .  exportAsZip  (  this  ,  file  .  getAbsolutePath  (  )  )  ; 
} 
}  else  { 
JFileChooser   fc  =  fileChooser  ; 
fc  .  setFileSelectionMode  (  JFileChooser  .  DIRECTORIES_ONLY  )  ; 
answer  =  fc  .  showDialog  (  this  ,  "Export"  )  ; 
if  (  answer  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
file  .  mkdirs  (  )  ; 
model  .  export  (  this  ,  file  .  getAbsolutePath  (  )  )  ; 
} 
} 
}  catch  (  BallotExportException   e  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  "An error occurred while exporting the ballot.\nPlease verify the directory is writable, and try again."  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 




public   void   fireLanguagesChanged  (  ArrayList  <  Language  >  languages  )  { 
model  .  setLanguages  (  languages  )  ; 
for  (  int   i  =  0  ;  i  <  model  .  getNumCards  (  )  ;  i  ++  )  setCardTitle  (  model  .  getCardTitle  (  i  )  ,  i  )  ; 
for  (  LanguageChangeListener   l  :  languageChangeListeners  )  l  .  languagesChanged  (  languages  )  ; 
} 





public   void   moveDownCardButtonPressed  (  )  { 
int   idx  =  cardList  .  getSelectedIndex  (  )  ; 
if  (  idx  <  cardListModel  .  size  (  )  -  1  )  { 
int   newIdx  =  idx  +  1  ; 
model  .  moveCard  (  idx  ,  newIdx  )  ; 
invalidate  (  )  ; 
String   element  =  (  String  )  cardListModel  .  remove  (  idx  )  ; 
cardListModel  .  add  (  newIdx  ,  element  )  ; 
cardList  .  setSelectedIndex  (  newIdx  )  ; 
validate  (  )  ; 
} 
} 





public   void   moveUpCardButtonPressed  (  )  { 
int   idx  =  cardList  .  getSelectedIndex  (  )  ; 
if  (  idx  >  0  )  { 
int   newIdx  =  idx  -  1  ; 
model  .  moveCard  (  idx  ,  newIdx  )  ; 
invalidate  (  )  ; 
String   element  =  (  String  )  cardListModel  .  remove  (  idx  )  ; 
cardListModel  .  add  (  newIdx  ,  element  )  ; 
cardList  .  setSelectedIndex  (  newIdx  )  ; 
validate  (  )  ; 
} 
} 





public   void   newBallotButtonPressed  (  )  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "If you start a new ballot, any changes you have made since the last time you saved the current ballot will be lost.  Continue?"  ,  "New Ballot"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
model  .  newBallot  (  )  ; 
cardListModel  .  removeAllElements  (  )  ; 
setCardPane  (  noCardsPanel  )  ; 
fireLanguagesChanged  (  model  .  getLanguages  (  )  )  ; 
} 
} 





public   void   openBallotButtonPressed  (  )  { 
try  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "If you open a ballot, any changes you have made since the last time you saved the current ballot will be lost.  Continue?"  ,  "Open Ballot"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
JFileChooser   fc  =  fileChooser  ; 
fc  .  setFileFilter  (  new   FileFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   f  )  { 
String   path  =  f  .  getAbsolutePath  (  )  ; 
return  (  f  .  isDirectory  (  )  ||  path  .  length  (  )  >  4  &&  path  .  substring  (  path  .  length  (  )  -  4  )  .  equals  (  ".bal"  )  )  ; 
} 

@  Override 
public   String   getDescription  (  )  { 
return  "Ballot files"  ; 
} 
}  )  ; 
answer  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  answer  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
model  .  open  (  file  .  getAbsolutePath  (  )  )  ; 
cardListModel  .  removeAllElements  (  )  ; 
for  (  int   i  =  0  ;  i  <  model  .  getNumCards  (  )  ;  i  ++  )  { 
cardListModel  .  addElement  (  model  .  getCardTitle  (  i  )  )  ; 
} 
if  (  cardListModel  .  size  (  )  >  0  )  cardList  .  setSelectedIndex  (  0  )  ;  else   cardList  .  setSelectedIndex  (  -  1  )  ; 
} 
fireLanguagesChanged  (  model  .  getLanguages  (  )  )  ; 
} 
}  catch  (  BallotOpenException   e  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  "An error occurred while opening the ballot.\nPlease verify the file is not corrupt, and try again."  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 





public   void   previewButtonPressed  (  )  { 
try  { 
model  .  previewBallot  (  this  )  ; 
}  catch  (  BallotPreviewException   e  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  "An error occurred while previewing the ballot."  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 







public   void   removeLanguageChangeListener  (  LanguageChangeListener   l  )  { 
languageChangeListeners  .  remove  (  l  )  ; 
} 





public   boolean   saveBallotButtonPressed  (  )  { 
try  { 
JFileChooser   fc  =  fileChooser  ; 
fc  .  setFileFilter  (  new   FileFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   f  )  { 
String   path  =  f  .  getAbsolutePath  (  )  ; 
return  (  f  .  isDirectory  (  )  ||  path  .  length  (  )  >  4  &&  path  .  substring  (  path  .  length  (  )  -  4  )  .  equals  (  ".bal"  )  )  ; 
} 

@  Override 
public   String   getDescription  (  )  { 
return  "Ballot files"  ; 
} 
}  )  ; 
int   answer  =  fc  .  showSaveDialog  (  this  )  ; 
if  (  answer  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
if  (  file  .  exists  (  )  )  { 
answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  "The file you selected already exists. Overwrite?"  ,  "Overwrite Saved Ballot"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
model  .  saveAs  (  file  .  getAbsolutePath  (  )  )  ; 
return   true  ; 
} 
}  else  { 
model  .  saveAs  (  file  .  getAbsolutePath  (  )  )  ; 
return   true  ; 
} 
} 
}  catch  (  BallotSaveException   e  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  "An error occurred while saving the ballot.\nPlease verify the directory is writable, and try again."  ,  "Error"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
return   false  ; 
} 







public   void   setCardPane  (  CardView   panel  )  { 
setCardPane  (  (  JPanel  )  panel  )  ; 
currentCardView  =  panel  ; 
languageBar  .  refreshEditorLanguage  (  )  ; 
model  .  getBallot  (  )  .  getCards  (  )  .  get  (  cardList  .  getSelectedIndex  (  )  )  .  addModuleObserver  (  "Title"  ,  titleObserver  )  ; 
} 







public   void   setCardPane  (  JPanel   panel  )  { 
currentCardView  =  null  ; 
cardPanel  .  removeAll  (  )  ; 
GridBagConstraints   c  =  new   GridBagConstraints  (  )  ; 
c  .  weightx  =  1  ; 
c  .  weighty  =  1  ; 
c  .  fill  =  GridBagConstraints  .  BOTH  ; 
cardPanel  .  add  (  panel  ,  c  )  ; 
if  (  previewPanel  !=  null  )  previewPanel  .  clear  (  )  ; 
validate  (  )  ; 
repaint  (  )  ; 
} 









public   void   setCardTitle  (  String   title  ,  int   idx  )  { 
if  (  title  .  equals  (  ""  )  )  cardListModel  .  setElementAt  (  "<untitled>"  ,  idx  )  ;  else   cardListModel  .  setElementAt  (  title  ,  idx  )  ; 
} 






public   void   showLanguageDialog  (  )  { 
LanguagesDialog   dialog  =  new   LanguagesDialog  (  this  ,  Language  .  getAllLanguages  (  )  ,  model  .  getLanguages  (  )  )  ; 
dialog  .  setVisible  (  true  )  ; 
if  (  dialog  .  okButtonWasPressed  (  )  )  { 
fireLanguagesChanged  (  dialog  .  getSelectedLanguages  (  )  )  ; 
} 
} 





public   ArrayList  <  Party  >  showPartiesDialog  (  )  { 
PartiesDialog   dialog  =  new   PartiesDialog  (  this  ,  model  .  getParties  (  )  ,  model  .  getLanguages  (  )  ,  languageBar  .  getCurrentLanguage  (  )  )  ; 
dialog  .  setVisible  (  true  )  ; 
return   model  .  getParties  (  )  ; 
} 




private   void   initializeCardPanel  (  )  { 
cardPanel  =  new   JPanel  (  )  ; 
cardPanel  .  setBorder  (  BorderFactory  .  createBevelBorder  (  BevelBorder  .  LOWERED  )  )  ; 
cardPanel  .  setLayout  (  new   GridBagLayout  (  )  )  ; 
noCardsPanel  =  new   JPanel  (  )  ; 
noCardsPanel  .  setLayout  (  new   GridBagLayout  (  )  )  ; 
GridBagConstraints   c2  =  new   GridBagConstraints  (  )  ; 
c2  .  gridx  =  0  ; 
c2  .  gridy  =  0  ; 
noCardsPanel  .  add  (  new   JLabel  (  "This ballot is currently empty (it does not contain"  +  " any races)."  )  ,  c2  )  ; 
c2  .  gridy  =  1  ; 
noCardsPanel  .  add  (  new   JLabel  (  "To get started, click on the '+' button in the lower left corner"  +  " of the screen."  )  ,  c2  )  ; 
setCardPane  (  noCardsPanel  )  ; 
previewPanel  =  new   PreviewPane  (  new   IPreviewPaneGenerator  (  )  { 

public   ArrayList  <  JPanel  >  getPreviewPanels  (  )  { 
if  (  cardList  .  getSelectedIndex  (  )  !=  -  1  )  return   model  .  previewCard  (  cardList  .  getSelectedIndex  (  )  ,  languageBar  .  getCurrentLanguage  (  )  )  ;  else   return   new   ArrayList  <  JPanel  >  (  )  ; 
} 
}  )  ; 
previewPanel  .  setBorder  (  BorderFactory  .  createBevelBorder  (  BevelBorder  .  LOWERED  )  )  ; 
} 





private   void   initializeListPanel  (  )  { 
listPanel  =  new   JPanel  (  )  ; 
listPanel  .  setLayout  (  new   GridBagLayout  (  )  )  ; 
cardListModel  =  new   DefaultListModel  (  )  ; 
cardList  =  new   JList  (  cardListModel  )  ; 
cardList  .  setDragEnabled  (  true  )  ; 
ListTransferHandler   lth  =  new   ListTransferHandler  (  )  ; 
lth  .  addListTransferListener  (  new   ListTransferListener  (  )  { 

public   void   listItemMoved  (  int   from  ,  int   to  )  { 
model  .  moveCard  (  from  ,  to  )  ; 
} 
}  )  ; 
cardList  .  setTransferHandler  (  lth  )  ; 
cardList  .  getSelectionModel  (  )  .  setSelectionMode  (  ListSelectionModel  .  SINGLE_SELECTION  )  ; 
cardList  .  addListSelectionListener  (  new   ListSelectionListener  (  )  { 

public   void   valueChanged  (  ListSelectionEvent   e  )  { 
cardListSelectionChanged  (  )  ; 
} 
}  )  ; 
JScrollPane   cardListScrollPane  =  new   JScrollPane  (  cardList  )  ; 
GridBagConstraints   c  =  new   GridBagConstraints  (  )  ; 
c  .  gridx  =  0  ; 
c  .  gridy  =  0  ; 
c  .  gridwidth  =  1  ; 
c  .  weightx  =  1  ; 
c  .  weighty  =  1  ; 
c  .  fill  =  GridBagConstraints  .  BOTH  ; 
listPanel  .  add  (  cardListScrollPane  ,  c  )  ; 
listToolbar  =  new   JToolBar  (  )  ; 
listToolbar  .  setFloatable  (  false  )  ; 
ImageIcon   icon  ; 
String   text  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/list-add.png"  )  )  ; 
text  =  ""  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
text  =  "Add"  ; 
} 
addCardButton  =  new   JButton  (  new   AbstractAction  (  text  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
addCardButtonPressed  (  )  ; 
} 
}  )  ; 
listToolbar  .  add  (  addCardButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/list-remove.png"  )  )  ; 
text  =  ""  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
text  =  "Remove"  ; 
} 
deleteCardButton  =  new   JButton  (  new   AbstractAction  (  text  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
deleteCardButtonPressed  (  )  ; 
} 
}  )  ; 
deleteCardButton  .  setEnabled  (  false  )  ; 
listToolbar  .  add  (  deleteCardButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/go-up.png"  )  )  ; 
text  =  ""  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
text  =  "Up"  ; 
} 
moveUpCardButton  =  new   JButton  (  new   AbstractAction  (  text  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
moveUpCardButtonPressed  (  )  ; 
} 
}  )  ; 
moveUpCardButton  .  setEnabled  (  false  )  ; 
listToolbar  .  add  (  moveUpCardButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/go-down.png"  )  )  ; 
text  =  ""  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
text  =  "Down"  ; 
} 
moveDownCardButton  =  new   JButton  (  new   AbstractAction  (  text  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
moveDownCardButtonPressed  (  )  ; 
} 
}  )  ; 
moveDownCardButton  .  setEnabled  (  false  )  ; 
listToolbar  .  add  (  moveDownCardButton  )  ; 
} 




private   void   initializeMenuBar  (  )  { 
menuBar  =  new   JMenuBar  (  )  ; 
ImageIcon   icon  ; 
int   shortcutKeyMask  =  java  .  awt  .  Toolkit  .  getDefaultToolkit  (  )  .  getMenuShortcutKeyMask  (  )  ; 
JMenu   fileMenu  =  new   JMenu  (  "File"  )  ; 
JMenuItem   newBallotMenuItem  =  new   JMenuItem  (  newBallotButton  .  getAction  (  )  )  ; 
newBallotMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_N  ,  shortcutKeyMask  )  )  ; 
fileMenu  .  add  (  newBallotMenuItem  )  ; 
JMenuItem   openBallotMenuItem  =  new   JMenuItem  (  openBallotButton  .  getAction  (  )  )  ; 
openBallotMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_O  ,  shortcutKeyMask  )  )  ; 
fileMenu  .  add  (  openBallotMenuItem  )  ; 
JMenuItem   saveBallotMenuItem  =  new   JMenuItem  (  saveBallotButton  .  getAction  (  )  )  ; 
saveBallotMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_S  ,  shortcutKeyMask  )  )  ; 
fileMenu  .  add  (  saveBallotMenuItem  )  ; 
fileMenu  .  addSeparator  (  )  ; 
JMenuItem   exportBallotMenuItem  =  new   JMenuItem  (  exportBallotButton  .  getAction  (  )  )  ; 
fileMenu  .  add  (  exportBallotMenuItem  )  ; 
JMenuItem   previewMenuItem  =  new   JMenuItem  (  previewButton  .  getAction  (  )  )  ; 
fileMenu  .  add  (  previewMenuItem  )  ; 
fileMenu  .  addSeparator  (  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/system-log-out.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
JMenuItem   quitMenuItem  =  new   JMenuItem  (  "Quit"  ,  icon  )  ; 
quitMenuItem  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
exitButtonPressed  (  )  ; 
} 
}  )  ; 
quitMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_Q  ,  shortcutKeyMask  )  )  ; 
fileMenu  .  add  (  quitMenuItem  )  ; 
menuBar  .  add  (  fileMenu  )  ; 
JMenu   editMenu  =  new   JMenu  (  "Edit"  )  ; 
JMenuItem   cutMenuItem  =  new   JMenuItem  (  new   DefaultEditorKit  .  CutAction  (  )  )  ; 
cutMenuItem  .  setText  (  "Cut"  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/edit-cut.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
cutMenuItem  .  setIcon  (  icon  )  ; 
cutMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_X  ,  shortcutKeyMask  )  )  ; 
editMenu  .  add  (  cutMenuItem  )  ; 
JMenuItem   copyMenuItem  =  new   JMenuItem  (  new   DefaultEditorKit  .  CopyAction  (  )  )  ; 
copyMenuItem  .  setText  (  "Copy"  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/edit-copy.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
copyMenuItem  .  setIcon  (  icon  )  ; 
copyMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_C  ,  shortcutKeyMask  )  )  ; 
editMenu  .  add  (  copyMenuItem  )  ; 
JMenuItem   pasteMenuItem  =  new   JMenuItem  (  new   DefaultEditorKit  .  PasteAction  (  )  )  ; 
pasteMenuItem  .  setText  (  "Paste"  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/edit-paste.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
pasteMenuItem  .  setIcon  (  icon  )  ; 
pasteMenuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_V  ,  shortcutKeyMask  )  )  ; 
editMenu  .  add  (  pasteMenuItem  )  ; 
menuBar  .  add  (  editMenu  )  ; 
} 




private   void   initializePopupMenu  (  )  { 
addCardMenu  =  new   JPopupMenu  (  )  ; 
for  (  final   ICardFactory   fac  :  model  .  getCardFactories  (  )  )  { 
JMenuItem   item  =  new   JMenuItem  (  fac  .  getMenuString  (  )  )  ; 
item  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
model  .  addCard  (  fac  .  makeCard  (  )  )  ; 
int   idx  =  model  .  getNumCards  (  )  -  1  ; 
cardListModel  .  addElement  (  ""  )  ; 
setCardTitle  (  model  .  getCardTitle  (  idx  )  ,  idx  )  ; 
cardList  .  setSelectedIndex  (  idx  )  ; 
} 
}  )  ; 
addCardMenu  .  add  (  item  )  ; 
} 
} 




private   void   initializeToolbar  (  )  { 
toolbar  =  new   JToolBar  (  )  ; 
toolbar  .  setFloatable  (  false  )  ; 
ImageIcon   icon  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/document-new.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
newBallotButton  =  new   JButton  (  new   AbstractAction  (  "New Ballot"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
newBallotButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  newBallotButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/document-open.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
openBallotButton  =  new   JButton  (  new   AbstractAction  (  "Open Ballot"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
openBallotButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  openBallotButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/document-save.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
saveBallotButton  =  new   JButton  (  new   AbstractAction  (  "Save Ballot"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
saveBallotButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  saveBallotButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/media-flash.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
exportBallotButton  =  new   JButton  (  new   AbstractAction  (  "Export to VoteBox"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
exportButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  exportBallotButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/system-search.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
previewButton  =  new   JButton  (  new   AbstractAction  (  "Preview in VoteBox"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
previewButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  previewButton  )  ; 
try  { 
icon  =  new   ImageIcon  (  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "images/system-options.png"  )  )  ; 
}  catch  (  Exception   e  )  { 
icon  =  null  ; 
} 
prefButton  =  new   JButton  (  new   AbstractAction  (  "Preferences"  ,  icon  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

public   void   actionPerformed  (  ActionEvent   e  )  { 
prefButtonPressed  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  prefButton  )  ; 
} 




public   ArrayList  <  Party  >  getParties  (  )  { 
return   model  .  getParties  (  )  ; 
} 

private   void   initializePrefMenu  (  )  { 
final   JFrame   frame  =  this  ; 
prefMenu  =  new   JPopupMenu  (  )  ; 
final   JMenuItem   races  =  new   JMenuItem  (  "Number of Races per Review Card (Current: "  +  model  .  getCardsPerReviewPage  (  )  +  ")"  )  ; 
races  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
String   temp  =  (  String  )  JOptionPane  .  showInputDialog  (  frame  ,  "How many races would you like "  +  "displayed on each review page?"  ,  ""  ,  JOptionPane  .  PLAIN_MESSAGE  ,  null  ,  null  ,  "10"  )  ; 
if  (  temp  !=  null  )  model  .  setCardsPerReviewPage  (  Integer  .  parseInt  (  temp  )  )  ;  else   return  ; 
races  .  setText  (  "Number of Races per Review Card (Current: "  +  model  .  getCardsPerReviewPage  (  )  +  ")"  )  ; 
} 
}  )  ; 
prefMenu  .  add  (  races  )  ; 
final   JMenuItem   font  =  new   JMenuItem  (  "Base Font Size (Current: "  +  model  .  getBaseFontSize  (  )  +  ")"  )  ; 
font  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
String   temp  =  (  String  )  JOptionPane  .  showInputDialog  (  frame  ,  "Please enter your desired font size:"  ,  ""  ,  JOptionPane  .  PLAIN_MESSAGE  ,  null  ,  null  ,  "8"  )  ; 
if  (  temp  !=  null  )  model  .  setFontSize  (  Integer  .  parseInt  (  temp  )  )  ;  else   return  ; 
font  .  setText  (  "Base Font Size (Current: "  +  model  .  getBaseFontSize  (  )  +  ")"  )  ; 
} 
}  )  ; 
prefMenu  .  add  (  font  )  ; 
final   JCheckBoxMenuItem   challenge  =  new   JCheckBoxMenuItem  (  "Use Commit-Challenge Model"  )  ; 
challenge  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
model  .  setCommitChallenge  (  challenge  .  getState  (  )  )  ; 
} 
}  )  ; 
prefMenu  .  add  (  challenge  )  ; 
} 
} 

