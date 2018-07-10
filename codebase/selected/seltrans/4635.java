package   csa  .  jportal  .  cardset  ; 

import   csa  .  jportal  .  gui  .  Windowable  ; 
import   csa  .  jportal  .  card  .  *  ; 
import   csa  .  jportal  .  *  ; 
import   csa  .  jportal  .  ai  .  enhancedAI  .  enhancedHints  .  AIEnhancedCardHints  ; 
import   csa  .  jportal  .  ai  .  enhancedAI  .  enhancedHints  .  AIEnhancedHint  ; 
import   csa  .  jportal  .  ai  .  enhancedAI  .  enhancedHints  .  AIEnhancedHintData  ; 
import   csa  .  jportal  .  ai  .  standardAI  .  hints  .  AICardHints  ; 
import   csa  .  jportal  .  ai  .  standardAI  .  hints  .  AIHintsData  ; 
import   csa  .  jportal  .  config  .  *  ; 
import   csa  .  jportal  .  gui  .  StarterKit  ; 
import   csa  .  jportal  .  script  .  ScriptData  ; 
import   csa  .  jportal  .  script  .  ScriptDataPool  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  event  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  awt  .  *  ; 
import   java  .  awt  .  image  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 





public   class   CardSetGathererImportPanel   extends   javax  .  swing  .  JPanel   implements   Windowable  ,  Runnable  { 

private   JPortalView   mParent  =  null  ; 

private   javax  .  swing  .  JMenuItem   mParentMenuItem  =  null  ; 

public   static   final   Proxy  .  Type  [  ]  PROXY_TYPES  =  Proxy  .  Type  .  values  (  )  ; 

private   int   mProxyType  =  0  ; 

private   CardSet   mLoadedCardSet  =  new   CardSet  (  )  ; 

public   void   closing  (  )  { 
} 

public   void   setParentWindow  (  JPortalView   jpv  )  { 
mParent  =  jpv  ; 
} 

public   void   setMenuItem  (  javax  .  swing  .  JMenuItem   item  )  { 
mParentMenuItem  =  item  ; 
mParentMenuItem  .  setText  (  "Import Gatherer-Set"  )  ; 
} 

public   javax  .  swing  .  JMenuItem   getMenuItem  (  )  { 
return   mParentMenuItem  ; 
} 

public   javax  .  swing  .  JPanel   getPanel  (  )  { 
return   this  ; 
} 


public   CardSetGathererImportPanel  (  )  { 
if  (  mParentMenuItem  !=  null  )  mParentMenuItem  .  setText  (  "Import Gatherer-Set"  )  ; 
initComponents  (  )  ; 
jTextFieldBaseUrl  .  setText  (  Configuration  .  getConfiguration  (  )  .  getGathererBaseURL  (  )  )  ; 
String   klasse  =  "MagicSets"  ; 
Collection  <  CardSetData  >  colC  =  CardSet  .  getPool  (  )  .  getMapForKlasse  (  klasse  )  .  values  (  )  ; 
Iterator  <  CardSetData  >  iterC  =  colC  .  iterator  (  )  ; 
while  (  iterC  .  hasNext  (  )  )  { 
CardSetData   item  =  iterC  .  next  (  )  ; 
knownSets  .  put  (  item  .  mName  ,  item  .  mName  )  ; 
jTextAreaOut  .  insert  (  "\n Known set: "  +  item  .  mName  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
generateCardMapForSet  (  item  .  mName  )  ; 
} 
jPanel2  .  removeAll  (  )  ; 
String  [  ]  text  =  {  "No Proxy"  ,  "Http Proxy"  ,  "Socks Proxy"  }  ; 
for  (  int   i  =  0  ;  i  <  PROXY_TYPES  .  length  ;  i  ++  )  { 
JRadioButton   rb  =  new   JRadioButton  (  text  [  i  ]  )  ; 
final   int   t  =  i  ; 
rb  .  addChangeListener  (  new   ChangeListener  (  )  { 

@  Override 
public   void   stateChanged  (  ChangeEvent   e  )  { 
if  (  (  (  AbstractButton  )  e  .  getSource  (  )  )  .  isSelected  (  )  )  { 
mProxyType  =  t  ; 
jTextFieldProxyURL  .  setEnabled  (  mProxyType  !=  0  )  ; 
jTextFieldProxyPort  .  setEnabled  (  mProxyType  !=  0  )  ; 
} 
} 
}  )  ; 
buttonGroup1  .  add  (  rb  )  ; 
jPanel2  .  add  (  rb  )  ; 
if  (  i  ==  0  )  rb  .  setSelected  (  true  )  ; 
} 
} 

public   CardSet   getLoadedCardset  (  )  { 
return   mLoadedCardSet  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
buttonGroup1  =  new   javax  .  swing  .  ButtonGroup  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextFieldBaseUrl  =  new   javax  .  swing  .  JTextField  (  )  ; 
jComboBoxSetNames  =  new   javax  .  swing  .  JComboBox  (  )  ; 
jButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jTextFieldSetName  =  new   javax  .  swing  .  JTextField  (  )  ; 
jButtonStartImport  =  new   javax  .  swing  .  JButton  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextAreaOut  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
jButtonEditSet  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
jTextFieldProxyURL  =  new   javax  .  swing  .  JTextField  (  )  ; 
jTextFieldProxyPort  =  new   javax  .  swing  .  JTextField  (  )  ; 
jPanel2  =  new   javax  .  swing  .  JPanel  (  )  ; 
jRadioButtonPType  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
jLabelImage  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
jCheckBoxJoin  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jCheckBoxFindSimilar  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jLabel1  .  setText  (  "Gatherer base URL"  )  ; 
jLabel1  .  setName  (  "jLabel1"  )  ; 
jLabel2  .  setText  (  "Setname"  )  ; 
jLabel2  .  setName  (  "jLabel2"  )  ; 
jTextFieldBaseUrl  .  setToolTipText  (  "At the time of Developement it is: \"http://gatherer.wizards.com/Pages/\""  )  ; 
jTextFieldBaseUrl  .  setName  (  "jTextFieldBaseUrl"  )  ; 
jComboBoxSetNames  .  setName  (  "jComboBoxSetNames"  )  ; 
jComboBoxSetNames  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jComboBoxSetNamesActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jButton1  .  setText  (  "Get setnames from Gatherer"  )  ; 
jButton1  .  setName  (  "jButton1"  )  ; 
jButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jTextFieldSetName  .  setText  (  "Portal"  )  ; 
jTextFieldSetName  .  setToolTipText  (  "Setname as given on the Gatherer hompage"  )  ; 
jTextFieldSetName  .  setName  (  "jTextFieldSetName"  )  ; 
jButtonStartImport  .  setText  (  "Import"  )  ; 
jButtonStartImport  .  setToolTipText  (  "Imports card data and grafics"  )  ; 
jButtonStartImport  .  setName  (  "jButtonStartImport"  )  ; 
jButtonStartImport  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButtonStartImportActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jScrollPane1  .  setName  (  "jScrollPane1"  )  ; 
jTextAreaOut  .  setColumns  (  20  )  ; 
jTextAreaOut  .  setEditable  (  false  )  ; 
jTextAreaOut  .  setLineWrap  (  true  )  ; 
jTextAreaOut  .  setRows  (  5  )  ; 
jTextAreaOut  .  setWrapStyleWord  (  true  )  ; 
jTextAreaOut  .  setName  (  "jTextAreaOut"  )  ; 
jScrollPane1  .  setViewportView  (  jTextAreaOut  )  ; 
jLabel3  .  setText  (  "Import status"  )  ; 
jLabel3  .  setName  (  "jLabel3"  )  ; 
jButtonEditSet  .  setText  (  "Edit Set"  )  ; 
jButtonEditSet  .  setEnabled  (  false  )  ; 
jButtonEditSet  .  setName  (  "jButtonEditSet"  )  ; 
jButtonEditSet  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButtonEditSetActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel1  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Proxy Settings"  )  )  ; 
jPanel1  .  setName  (  "jPanel1"  )  ; 
jTextFieldProxyURL  .  setText  (  "Proxy Adress"  )  ; 
jTextFieldProxyURL  .  setToolTipText  (  "URL"  )  ; 
jTextFieldProxyURL  .  setName  (  "jTextFieldProxyURL"  )  ; 
jTextFieldProxyPort  .  setText  (  "Port Number"  )  ; 
jTextFieldProxyPort  .  setToolTipText  (  "Port"  )  ; 
jTextFieldProxyPort  .  setName  (  "jTextFieldProxyPort"  )  ; 
jPanel2  .  setName  (  "jPanel2"  )  ; 
jPanel2  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jPanel2  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
buttonGroup1  .  add  (  jRadioButtonPType  )  ; 
jRadioButtonPType  .  setText  (  "jRadioButton1"  )  ; 
jRadioButtonPType  .  setName  (  "jRadioButtonPType"  )  ; 
jPanel2  .  add  (  jRadioButtonPType  )  ; 
javax  .  swing  .  GroupLayout   jPanel1Layout  =  new   javax  .  swing  .  GroupLayout  (  jPanel1  )  ; 
jPanel1  .  setLayout  (  jPanel1Layout  )  ; 
jPanel1Layout  .  setHorizontalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addComponent  (  jPanel2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jTextFieldProxyURL  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  321  ,  Short  .  MAX_VALUE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jTextFieldProxyPort  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  48  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
jPanel1Layout  .  setVerticalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jPanel2  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  93  ,  Short  .  MAX_VALUE  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jTextFieldProxyURL  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jTextFieldProxyPort  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addContainerGap  (  )  )  )  ; 
jLabelImage  .  setText  (  "Image"  )  ; 
jLabelImage  .  setName  (  "jLabelImage"  )  ; 
jLabel4  .  setText  (  "Image cropped"  )  ; 
jLabel4  .  setName  (  "jLabel4"  )  ; 
jCheckBoxJoin  .  setText  (  "Join current JPortal implementations"  )  ; 
jCheckBoxJoin  .  setName  (  "jCheckBoxJoin"  )  ; 
jCheckBoxFindSimilar  .  setText  (  "Find similar Cards"  )  ; 
jCheckBoxFindSimilar  .  setEnabled  (  false  )  ; 
jCheckBoxFindSimilar  .  setName  (  "jCheckBoxFindSimilar"  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  this  )  ; 
this  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jLabel1  )  .  addComponent  (  jLabel2  )  )  .  addGap  (  36  ,  36  ,  36  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jTextFieldBaseUrl  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  658  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addComponent  (  jTextFieldSetName  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  204  ,  Short  .  MAX_VALUE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jComboBoxSetNames  ,  0  ,  204  ,  Short  .  MAX_VALUE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jButton1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  238  ,  Short  .  MAX_VALUE  )  )  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jButtonStartImport  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jButtonEditSet  )  )  .  addComponent  (  jCheckBoxFindSimilar  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabelImage  )  .  addGap  (  29  ,  29  ,  29  )  .  addComponent  (  jLabel4  )  )  .  addComponent  (  jLabel3  )  .  addComponent  (  jCheckBoxJoin  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jPanel1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  )  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  795  ,  Short  .  MAX_VALUE  )  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jTextFieldBaseUrl  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel1  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel2  )  .  addComponent  (  jComboBoxSetNames  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jButton1  )  .  addComponent  (  jTextFieldSetName  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGap  (  15  ,  15  ,  15  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabelImage  )  .  addComponent  (  jLabel4  )  )  .  addComponent  (  jCheckBoxFindSimilar  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jCheckBoxJoin  )  .  addGap  (  3  ,  3  ,  3  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jButtonStartImport  )  .  addComponent  (  jButtonEditSet  )  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  jLabel3  )  )  .  addComponent  (  jPanel1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  199  ,  Short  .  MAX_VALUE  )  )  )  ; 
} 

private   void   jComboBoxSetNamesActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
Object   o  =  jComboBoxSetNames  .  getSelectedItem  (  )  ; 
if  (  o  !=  null  )  jTextFieldSetName  .  setText  (  o  .  toString  (  )  )  ; 
} 

private   void   jButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
Vector  <  String  >  name  =  getSetNames  (  )  ; 
jComboBoxSetNames  .  removeAllItems  (  )  ; 
for  (  int   i  =  0  ;  i  <  name  .  size  (  )  ;  i  ++  )  { 
String   n  =  name  .  elementAt  (  i  )  ; 
jComboBoxSetNames  .  addItem  (  n  )  ; 
} 
} 

private   void   jButtonStartImportActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mLoadedCardSet  =  new   CardSet  (  )  ; 
new   Thread  (  CardSetGathererImportPanel  .  this  )  .  start  (  )  ; 
jButtonStartImport  .  setEnabled  (  false  )  ; 
if  (  msk  !=  null  )  { 
msk  .  importStarting  (  )  ; 
} 
} 

StarterKit   msk  =  null  ; 

public   void   startImport  (  )  { 
jButtonStartImportActionPerformed  (  null  )  ; 
} 

public   void   setStarterKit  (  StarterKit   sk  )  { 
msk  =  sk  ; 
} 

private   void   jButtonEditSetActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mParent  .  removePanel  (  this  )  ; 
CardSetPanel   p  =  CardSetPanel  .  getCardSetPanel  (  )  ; 
p  .  setCardSet  (  mLoadedCardSet  )  ; 
mParent  .  addPanel  (  p  )  ; 
mParent  .  setMainPanel  (  p  )  ; 
} 

Vector  <  String  >  getSetNames  (  )  { 
Vector  <  String  >  ret  =  new   Vector  <  String  >  (  )  ; 
Proxy   p  =  null  ; 
if  (  mProxyType  ==  0  )  p  =  Proxy  .  NO_PROXY  ;  else  { 
try  { 
p  =  new   Proxy  (  PROXY_TYPES  [  mProxyType  ]  ,  new   InetSocketAddress  (  jTextFieldProxyURL  .  getText  (  )  ,  Integer  .  parseInt  (  jTextFieldProxyPort  .  getText  (  )  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
return   ret  ; 
} 
} 
try  { 
setCursor  (  Cursor  .  getPredefinedCursor  (  Cursor  .  WAIT_CURSOR  )  )  ; 
try  { 
String   baseUrl  =  jTextFieldBaseUrl  .  getText  (  )  ; 
URL   url  =  new   URL  (  baseUrl  )  ; 
URLConnection   conn  =  url  .  openConnection  (  p  )  ; 
conn  .  setDoOutput  (  true  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  ; 
boolean   save  =  false  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  indexOf  (  "Filter Card Set"  )  !=  -  1  )  save  =  true  ; 
if  (  line  .  indexOf  (  "Filter Card Type"  )  !=  -  1  )  save  =  false  ; 
if  (  save  )  { 
if  (  line  .  indexOf  (  "<option value=\""  )  !=  -  1  )  { 
String   name  =  line  .  substring  (  line  .  indexOf  (  "\""  )  +  1  ,  line  .  indexOf  (  "\">"  )  )  ; 
name  =  csa  .  util  .  UtilityString  .  fromXML  (  name  )  ; 
ret  .  addElement  (  name  )  ; 
} 
} 
} 
rd  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
} 
}  finally  { 
setCursor  (  Cursor  .  getDefaultCursor  (  )  )  ; 
} 
return   ret  ; 
} 

public   void   run  (  )  { 
Proxy   p  =  null  ; 
if  (  mProxyType  ==  0  )  p  =  Proxy  .  NO_PROXY  ;  else  { 
try  { 
p  =  new   Proxy  (  PROXY_TYPES  [  mProxyType  ]  ,  new   InetSocketAddress  (  jTextFieldProxyURL  .  getText  (  )  ,  Integer  .  parseInt  (  jTextFieldProxyPort  .  getText  (  )  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
return  ; 
} 
} 
String   setName  =  jTextFieldSetName  .  getText  (  )  ; 
String   setNameUrl  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  " "  ,  "%20"  )  ; 
setName  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  "\""  ,  ""  )  ; 
setName  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  "'"  ,  ""  )  ; 
setName  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  "´"  ,  ""  )  ; 
setName  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  "`"  ,  ""  )  ; 
setName  =  csa  .  util  .  UtilityString  .  replace  (  setName  ,  ":"  ,  ""  )  ; 
String   base  =  jTextFieldBaseUrl  .  getText  (  )  ; 
mLoadedCardSet  .  getData  (  )  .  mOrigin  =  CardSet  .  GATHERER_IMPORTED  ; 
mLoadedCardSet  .  getData  (  )  .  mSetName  =  setName  ; 
try  { 
String   searcher  =  "Search/Default.aspx?output=spoiler&method=text&set=[%22"  +  setNameUrl  +  "%22] "  ; 
URL   url  =  new   URL  (  base  +  searcher  )  ; 
URLConnection   conn  =  url  .  openConnection  (  p  )  ; 
conn  .  setDoOutput  (  true  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  ; 
boolean   save  =  false  ; 
String  [  ]  dontLike  =  {  "<tr>"  ,  "</tr>"  ,  "<td"  ,  "</td>"  ,  "<table>"  ,  "</table>"  }  ; 
int   NAME  =  0  ; 
int   COST  =  1  ; 
int   TYPE  =  2  ; 
int   POW  =  3  ; 
int   TEXT  =  4  ; 
int   RARITY  =  5  ; 
String  [  ]  textTags  =  {  "Name:"  ,  "Cost:"  ,  "Type:"  ,  "Pow/Tgh:"  ,  "Rules Text:"  ,  "Set/Rarity:"  }  ; 
Card   card  =  new   Card  (  )  ; 
int   currentTag  =  -  1  ; 
String   dbg  =  ""  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  indexOf  (  "class=\"textspoiler\""  )  !=  -  1  )  save  =  true  ; 
if  (  line  .  indexOf  (  "</div>"  )  !=  -  1  )  save  =  false  ; 
if  (  save  )  { 
boolean   goOn  =  true  ; 
for  (  int   i  =  0  ;  i  <  dontLike  .  length  ;  i  ++  )  { 
if  (  line  .  indexOf  (  dontLike  [  i  ]  )  !=  -  1  )  { 
goOn  =  false  ; 
break  ; 
} 
} 
if  (  goOn  )  { 
for  (  int   i  =  0  ;  i  <  textTags  .  length  ;  i  ++  )  { 
if  (  line  .  indexOf  (  textTags  [  i  ]  )  !=  -  1  )  { 
currentTag  =  i  ; 
goOn  =  false  ; 
break  ; 
} 
} 
} 
if  (  goOn  )  { 
if  (  NAME  ==  currentTag  )  { 
dbg  =  ""  ; 
String   id  =  line  .  substring  (  line  .  indexOf  (  "multiverseid="  )  +  13  )  ; 
id  =  id  .  substring  (  0  ,  id  .  indexOf  (  "\""  )  )  ; 
String   name  =  line  .  substring  (  line  .  indexOf  (  ">"  )  +  1  )  ; 
name  =  name  .  substring  (  0  ,  name  .  indexOf  (  "<"  )  )  ; 
card  =  new   Card  (  )  ; 
card  .  getData  (  )  .  setId  (  id  )  ; 
card  .  getData  (  )  .  setCardName  (  name  )  ; 
card  .  getData  (  )  .  setSetName  (  setName  )  ; 
dbg  +=  "Card: "  +  name  +  "("  +  id  +  ")\n"  ; 
currentTag  =  -  1  ; 
} 
if  (  COST  ==  currentTag  )  { 
String   cost  =  line  .  trim  (  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "G"  ,  "{G}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "B"  ,  "{B}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "W"  ,  "{W}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "R"  ,  "{R}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "U"  ,  "{U}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "X"  ,  "{X}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "1"  ,  "{1}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "2"  ,  "{2}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "3"  ,  "{3}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "4"  ,  "{4}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "5"  ,  "{5}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "6"  ,  "{6}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "7"  ,  "{7}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "8"  ,  "{8}"  )  ; 
cost  =  csa  .  util  .  UtilityString  .  replace  (  cost  ,  "9"  ,  "{9}"  )  ; 
card  .  getData  (  )  .  setMana  (  cost  )  ; 
dbg  +=  "Cost: "  +  cost  +  "\n"  ; 
currentTag  =  -  1  ; 
} 
if  (  TYPE  ==  currentTag  )  { 
String   type  =  line  .  trim  (  )  ; 
int   typeEnd  =  -  1  ; 
int   subtypeStart  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  type  .  length  (  )  ;  i  ++  )  { 
if  (  (  (  int  )  (  type  .  charAt  (  i  )  )  )  >  250  )  { 
typeEnd  =  i  -  1  ; 
for  (  int   c  =  i  ;  c  <  type  .  length  (  )  ;  c  ++  )  { 
if  (  (  (  int  )  (  type  .  charAt  (  c  )  )  )  ==  32  )  { 
subtypeStart  =  c  ; 
break  ; 
} 
} 
break  ; 
} 
} 
if  (  typeEnd  !=  -  1  )  { 
card  .  getData  (  )  .  setType  (  type  .  substring  (  0  ,  typeEnd  )  .  trim  (  )  )  ; 
dbg  +=  "Type: "  +  type  .  substring  (  0  ,  typeEnd  )  .  trim  (  )  +  " "  ; 
}  else  { 
card  .  getData  (  )  .  setType  (  type  )  ; 
dbg  +=  "Type: "  +  type  +  " "  ; 
} 
if  (  subtypeStart  !=  -  1  )  { 
String   subType  =  type  .  substring  (  subtypeStart  )  .  trim  (  )  ; 
card  .  getData  (  )  .  setSubtype  (  subType  .  trim  (  )  )  ; 
dbg  +=  "SubType: "  +  subType  .  trim  (  )  +  "\n"  ; 
} 
currentTag  =  -  1  ; 
} 
if  (  POW  ==  currentTag  )  { 
String   powerToughness  =  line  .  trim  (  )  ; 
if  (  powerToughness  .  length  (  )  ==  0  )  { 
card  .  getData  (  )  .  setPower  (  ""  )  ; 
card  .  getData  (  )  .  setToughness  (  ""  )  ; 
}  else  { 
powerToughness  =  csa  .  util  .  UtilityString  .  replace  (  powerToughness  ,  "("  ,  ""  )  ; 
powerToughness  =  csa  .  util  .  UtilityString  .  replace  (  powerToughness  ,  ")"  ,  ""  )  ; 
String   power  =  powerToughness  .  substring  (  0  ,  powerToughness  .  indexOf  (  "/"  )  )  ; 
String   toughness  =  powerToughness  .  substring  (  powerToughness  .  indexOf  (  "/"  )  +  1  )  ; 
card  .  getData  (  )  .  setPower  (  power  )  ; 
card  .  getData  (  )  .  setToughness  (  toughness  )  ; 
} 
dbg  +=  "Pow/Tough: "  +  card  .  getData  (  )  .  getPower  (  )  +  "/"  +  card  .  getData  (  )  .  getToughness  (  )  +  "\n"  ; 
currentTag  =  -  1  ; 
} 
if  (  TEXT  ==  currentTag  )  { 
String   text  =  line  .  trim  (  )  ; 
text  =  csa  .  util  .  UtilityString  .  replace  (  text  ,  "<br />"  ,  " "  )  ; 
card  .  addText  (  text  )  ; 
} 
if  (  RARITY  ==  currentTag  )  { 
dbg  +=  "Text: "  +  card  .  getText  (  )  +  "\n"  ; 
String   r  =  ""  ; 
String   rarity  =  line  .  substring  (  line  .  indexOf  (  setName  )  +  setName  .  length  (  )  )  ; 
int   l  =  rarity  .  length  (  )  ; 
if  (  rarity  .  indexOf  (  ","  )  !=  -  1  )  l  =  rarity  .  indexOf  (  ","  )  ; 
rarity  =  rarity  .  substring  (  0  ,  l  )  ; 
if  (  rarity  .  indexOf  (  "Common"  )  !=  -  1  )  r  =  "C"  ; 
if  (  rarity  .  indexOf  (  "Uncommon"  )  !=  -  1  )  r  =  "U"  ; 
if  (  rarity  .  indexOf  (  "Rare"  )  !=  -  1  )  r  =  "R"  ; 
if  (  rarity  .  indexOf  (  "Land"  )  !=  -  1  )  r  =  "L"  ; 
card  .  getData  (  )  .  setRarity  (  r  )  ; 
dbg  +=  "Rarity: "  +  r  +  "\n"  ; 
dbg  +=  "----\n"  ; 
if  (  card  .  getType  (  )  .  equalsIgnoreCase  (  "Basic Land"  )  )  { 
if  (  card  .  getText  (  )  .  indexOf  (  "{T}"  )  ==  -  1  )  card  .  addfText  (  "{T}:"  )  ; 
} 
mLoadedCardSet  .  addCard  (  card  )  ; 
currentTag  =  -  1  ; 
synchronized  (  this  )  { 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
Configuration  .  getConfiguration  (  )  .  getLogEntity  (  )  .  addLog  (  dbg  )  ; 
} 
} 
} 
} 
} 
rd  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
return  ; 
} 
String   dirName  =  "sets"  +  File  .  separator  +  setName  ; 
File   dir  =  new   File  (  dirName  )  ; 
if  (  !  dir  .  exists  (  )  )  { 
try  { 
dir  .  mkdir  (  )  ; 
}  catch  (  Throwable   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
} 
} 
mLoadedCardSet  .  getData  (  )  .  mImagePath  =  dirName  ; 
HashMap  <  String  ,  String  >  imageUrls  =  new   HashMap  <  String  ,  String  >  (  )  ; 
try  { 
String   secondBase  =  "Search/"  ; 
String   searcher  =  new   String  (  "Default.aspx?output=spoiler&method=visual&set=[%22"  +  setNameUrl  +  "%22] "  )  ; 
URL   url  =  new   URL  (  base  +  secondBase  +  searcher  )  ; 
URLConnection   conn  =  url  .  openConnection  (  p  )  ; 
conn  .  setDoOutput  (  true  )  ; 
BufferedReader   rd  =  new   BufferedReader  (  new   InputStreamReader  (  conn  .  getInputStream  (  )  )  )  ; 
String   line  ; 
boolean   save  =  false  ; 
while  (  (  line  =  rd  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  indexOf  (  "class=\"visualspoiler\""  )  !=  -  1  )  save  =  true  ; 
if  (  line  .  indexOf  (  "</div>"  )  !=  -  1  )  save  =  false  ; 
if  (  save  )  { 
if  (  line  .  indexOf  (  "<img src="  )  !=  -  1  )  { 
line  =  line  .  substring  (  line  .  indexOf  (  "<img src="  )  +  "<img src="  .  length  (  )  +  1  )  ; 
line  =  line  .  substring  (  0  ,  line  .  indexOf  (  "\""  )  )  ; 
String   id  =  line  .  substring  (  line  .  indexOf  (  "="  )  +  1  )  ; 
id  =  id  .  substring  (  0  ,  id  .  indexOf  (  "&"  )  )  ; 
String   imageUrl  =  csa  .  util  .  UtilityString  .  fromXML  (  line  )  ; 
if  (  imageUrl  .  startsWith  (  "."  )  )  { 
imageUrl  =  base  +  secondBase  +  imageUrl  ; 
} 
imageUrls  .  put  (  id  ,  imageUrl  )  ; 
synchronized  (  this  )  { 
String   dbg  =  "ID: "  +  id  +  " ImageUrl: "  +  imageUrl  +  " got!\n"  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  -  dbg  .  length  (  )  )  ; 
Configuration  .  getConfiguration  (  )  .  getLogEntity  (  )  .  addLog  (  dbg  )  ; 
} 
} 
} 
} 
rd  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
} 
BufferedInputStream   in  ; 
BufferedOutputStream   out  ; 
Set   entries  =  imageUrls  .  entrySet  (  )  ; 
Iterator   it  =  entries  .  iterator  (  )  ; 
int   c  =  0  ; 
while  (  it  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  it  .  next  (  )  ; 
String   imageUrl  =  (  String  )  entry  .  getValue  (  )  ; 
String   id  =  (  String  )  entry  .  getKey  (  )  ; 
String   imagePath  =  dirName  +  File  .  separator  +  id  +  ".jpg"  ; 
File   f  =  new   File  (  imagePath  )  ; 
if  (  f  .  exists  (  )  )  continue  ; 
try  { 
in  =  new   BufferedInputStream  (  new   URL  (  imageUrl  )  .  openConnection  (  p  )  .  getInputStream  (  )  )  ; 
Image   image  =  javax  .  imageio  .  ImageIO  .  read  (  in  )  ; 
Icon   i  =  new   ImageIcon  (  image  )  ; 
jLabelImage  .  setIcon  (  i  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  f  )  )  ; 
BufferedImage   bimage  =  csa  .  util  .  UtilityImage  .  toBufferedImage  (  image  )  ; 
if  (  bimage  .  getHeight  (  )  !=  285  )  { 
BufferedImage   bimageCropped  =  bimage  .  getSubimage  (  11  ,  12  ,  200  ,  285  )  ; 
Icon   iCropped  =  new   ImageIcon  (  bimageCropped  )  ; 
jLabel4  .  setIcon  (  iCropped  )  ; 
javax  .  imageio  .  ImageIO  .  write  (  bimageCropped  ,  "jpg"  ,  f  )  ; 
}  else   javax  .  imageio  .  ImageIO  .  write  (  bimage  ,  "jpg"  ,  f  )  ; 
in  .  close  (  )  ; 
c  ++  ; 
synchronized  (  this  )  { 
String   dbg  =  "("  +  c  +  "/"  +  imageUrls  .  size  (  )  +  ")Image successfully saved to: "  +  imagePath  +  "\n"  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  -  dbg  .  length  (  )  )  ; 
Configuration  .  getConfiguration  (  )  .  getLogEntity  (  )  .  addLog  (  dbg  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
Configuration  .  getConfiguration  (  )  .  getDebugEntity  (  )  .  addLog  (  e  ,  Logable  .  LOG_ERROR  )  ; 
} 
} 
jButtonStartImport  .  setEnabled  (  true  )  ; 
jButtonEditSet  .  setEnabled  (  true  )  ; 
Card  .  addImagePathForSet  (  setName  ,  dirName  +  File  .  separator  )  ; 
mLoadedCardSet  .  getData  (  )  .  mClass  =  "MagicSets"  ; 
mLoadedCardSet  .  getData  (  )  .  mName  =  setName  ; 
mLoadedCardSet  .  joinedSave  (  )  ; 
if  (  jCheckBoxJoin  .  isSelected  (  )  )  { 
jTextAreaOut  .  insert  (  "\nLooking if there is anything to clone..."  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
int   s  =  seekForKnown  (  setName  )  ; 
String   dbg  =  "\n"  +  setName  +  " ->"  +  "Supported Cards found: "  +  s  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
System  .  out  .  println  (  dbg  )  ; 
} 
if  (  jCheckBoxFindSimilar  .  isSelected  (  )  )  { 
jTextAreaOut  .  insert  (  "\nLooking if there is anything to convert and clone..."  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
int   s  =  seekForSimilar  (  setName  )  ; 
String   dbg  =  "\n"  +  setName  +  " ->"  +  "Supported Cards found: "  +  s  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
System  .  out  .  println  (  dbg  )  ; 
} 
String   dbg  =  "Import finished!\n"  ; 
dbg  =  "\nImages and set is saved, press above \"Edit Set\" button to enter set editing."  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
Configuration  .  getConfiguration  (  )  .  getLogEntity  (  )  .  addLog  (  dbg  )  ; 
if  (  msk  !=  null  )  { 
msk  .  importFinished  (  )  ; 
} 
} 

private   javax  .  swing  .  ButtonGroup   buttonGroup1  ; 

private   javax  .  swing  .  JButton   jButton1  ; 

private   javax  .  swing  .  JButton   jButtonEditSet  ; 

private   javax  .  swing  .  JButton   jButtonStartImport  ; 

private   javax  .  swing  .  JCheckBox   jCheckBoxFindSimilar  ; 

private   javax  .  swing  .  JCheckBox   jCheckBoxJoin  ; 

private   javax  .  swing  .  JComboBox   jComboBoxSetNames  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabelImage  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JPanel   jPanel2  ; 

private   javax  .  swing  .  JRadioButton   jRadioButtonPType  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JTextArea   jTextAreaOut  ; 

private   javax  .  swing  .  JTextField   jTextFieldBaseUrl  ; 

private   javax  .  swing  .  JTextField   jTextFieldProxyPort  ; 

private   javax  .  swing  .  JTextField   jTextFieldProxyURL  ; 

private   javax  .  swing  .  JTextField   jTextFieldSetName  ; 

String   getCleanText  (  String   text  )  { 
return   excludeAbilities  (  excludeBrackets  (  text  )  )  .  trim  (  )  ; 
} 

private   String   excludeBrackets  (  String   t  )  { 
while  (  t  .  indexOf  (  "("  )  !=  -  1  )  { 
int   start  =  t  .  indexOf  (  "("  )  ; 
int   end  =  t  .  indexOf  (  ")"  )  ; 
int   start2  =  t  .  indexOf  (  "("  ,  start  +  1  )  ; 
while  (  (  start2  !=  -  1  )  &&  (  start2  <  end  )  )  { 
start  =  start2  ; 
start2  =  t  .  indexOf  (  "("  ,  start  +  1  )  ; 
} 
String   t1  =  ""  ; 
String   t2  =  ""  ; 
if  (  start  !=  0  )  t1  =  t  .  substring  (  0  ,  start  )  ; 
if  (  end  <  t  .  length  (  )  -  1  )  t2  =  t  .  substring  (  end  +  1  )  ; 
t  =  t1  +  t2  ; 
} 
return   t  ; 
} 

private   String   excludeAbilities  (  String   t  )  { 
String  [  ]  abilities  =  {  "Flying"  ,  "Haste"  ,  "Vigilance"  ,  "Defender"  ,  "Reach"  ,  "Mountainwalk"  ,  "Swampwalk"  ,  "Forestwalk"  ,  "Islandwalk"  ,  "Horsemanship"  }  ; 
boolean   found  ; 
t  =  t  .  trim  (  )  ; 
do  { 
found  =  false  ; 
for  (  int   i  =  0  ;  i  <  abilities  .  length  ;  i  ++  )  { 
String   ability  =  abilities  [  i  ]  .  toUpperCase  (  )  ; 
if  (  t  .  toUpperCase  (  )  .  indexOf  (  ability  )  ==  0  )  { 
t  =  t  .  substring  (  ability  .  length  (  )  )  .  trim  (  )  ; 
if  (  t  .  startsWith  (  ","  )  )  t  =  t  .  substring  (  1  )  .  trim  (  )  ; 
if  (  t  .  startsWith  (  ":"  )  )  t  =  t  .  substring  (  1  )  .  trim  (  )  ; 
found  =  true  ; 
break  ; 
} 
} 
}  while  (  found  )  ; 
return   t  ; 
} 

void   cloneCard  (  Card   otherCard  ,  Card   mCurrentCard  )  { 
AIEnhancedCardHints  .  resetHintPool  (  )  ; 
if  (  mCurrentCard  !=  null  )  { 
String   basedir  =  mCurrentCard  .  getScriptPoolBaseDir  (  )  ; 
File   f  =  new   File  (  csa  .  Global  .  mBaseDir  +  File  .  separator  +  basedir  )  ; 
if  (  !  f  .  exists  (  )  )  { 
boolean   b  =  f  .  mkdir  (  )  ; 
if  (  !  b  )  System  .  out  .  println  (  "FALSE: "  +  f  .  toString  (  )  )  ; 
} 
} 
ScriptDataPool   mScriptDataPool  =  new   ScriptDataPool  (  mCurrentCard  .  getScriptPoolBaseName  (  )  +  ".xml"  )  ; 
for  (  int   i  =  0  ;  i  <  CardSituation  .  KEY  .  length  ;  i  ++  )  { 
String   scriptForKey  =  otherCard  .  getPureScript  (  CardSituation  .  KEY  [  i  ]  )  ; 
ScriptData   data  =  otherCard  .  getPureScriptData  (  CardSituation  .  KEY  [  i  ]  )  ; 
if  (  data  !=  null  )  { 
String   script  =  data  .  getScript  (  )  ; 
script  =  csa  .  util  .  UtilityString  .  replace  (  script  ,  otherCard  .  getId  (  )  ,  mCurrentCard  .  getId  (  )  )  ; 
script  =  csa  .  util  .  UtilityString  .  replace  (  script  ,  otherCard  .  getName  (  )  ,  mCurrentCard  .  getName  (  )  )  ; 
data  .  setScript  (  script  )  ; 
mScriptDataPool  .  put  (  data  )  ; 
} 
} 
mScriptDataPool  .  save  (  )  ; 
Configuration  .  getConfiguration  (  )  .  resetScriptCaching  (  )  ; 
AICardHints   otherHints  =  new   AICardHints  (  otherCard  )  ; 
AICardHints   myHints  =  new   AICardHints  (  mCurrentCard  )  ; 
for  (  int   i  =  0  ;  i  <  otherHints  .  size  (  )  ;  i  ++  )  { 
AIHintsData   otherHint  =  otherHints  .  getHintByRow  (  i  )  ; 
AIHintsData   aHint  =  AICardHints  .  buildNewHint  (  mCurrentCard  )  ; 
aHint  .  setHintComment  (  otherHint  .  getHintComment  (  )  )  ; 
aHint  .  setHintName  (  otherHint  .  getHintName  (  )  )  ; 
aHint  .  setHintValue  (  otherHint  .  getHintValue  (  )  )  ; 
aHint  .  setSituation  (  otherHint  .  getSituation  (  )  )  ; 
aHint  .  setHintVarName  (  otherHint  .  getHintVarName  (  )  )  ; 
aHint  .  setHintVarType  (  otherHint  .  getHintVarType  (  )  )  ; 
aHint  .  setIsTemplate  (  false  )  ; 
aHint  .  setCardID  (  mCurrentCard  .  getId  (  )  )  ; 
aHint  .  setCardSet  (  mCurrentCard  .  getSet  (  )  )  ; 
myHints  .  addHint  (  aHint  )  ; 
} 
myHints  .  save  (  )  ; 
AIEnhancedCardHints   eotherHints  =  AIEnhancedCardHints  .  getHints  (  otherCard  )  ; 
AIEnhancedCardHints   emyHints  =  AIEnhancedCardHints  .  getHints  (  mCurrentCard  )  ; 
for  (  int   i  =  0  ;  i  <  eotherHints  .  size  (  )  ;  i  ++  )  { 
AIEnhancedHintData   otherHint  =  eotherHints  .  getHintByRow  (  i  )  ; 
AIEnhancedHintData   aHint  =  AIEnhancedHint  .  buildNewHint  (  mCurrentCard  )  ; 
aHint  .  setHintKey  (  otherHint  .  getHintKey  (  )  )  ; 
aHint  .  setHintNumber  (  otherHint  .  getHintNumber  (  )  )  ; 
aHint  .  setHintName  (  otherHint  .  getHintName  (  )  )  ; 
aHint  .  setHintType  (  otherHint  .  getHintType  (  )  )  ; 
aHint  .  setHintValue  (  otherHint  .  getHintValue  (  )  )  ; 
aHint  .  setCardID  (  mCurrentCard  .  getId  (  )  )  ; 
aHint  .  setCardSet  (  mCurrentCard  .  getSet  (  )  )  ; 
emyHints  .  addHint  (  aHint  )  ; 
} 
emyHints  .  save  (  )  ; 
} 

Vector  <  String  >  setNamesToImport  ; 

HashMap  <  String  ,  Card  >  map  =  new   HashMap  <  String  ,  Card  >  (  )  ; 

HashMap  <  String  ,  String  >  knownSets  =  new   HashMap  <  String  ,  String  >  (  )  ; 

private   void   generateCardMapForSet  (  String   setName  )  { 
CardSet   cardset  =  new   CardSet  (  )  ; 
cardset  .  setData  (  CardSet  .  getPool  (  )  .  get  (  setName  )  )  ; 
Vector  <  Card  >  cards  =  cardset  .  getCards  (  )  ; 
for  (  int   i  =  0  ;  i  <  cards  .  size  (  )  ;  i  ++  )  { 
Card   card  =  cards  .  elementAt  (  i  )  ; 
map  .  put  (  card  .  getName  (  )  ,  card  )  ; 
} 
} 

public   int   seekForKnown  (  String   newSetName  )  { 
CardSet   cardset  =  new   CardSet  (  )  ; 
cardset  .  setData  (  CardSet  .  getPool  (  )  .  get  (  newSetName  )  )  ; 
Vector  <  Card  >  cards  =  cardset  .  getCards  (  )  ; 
int   supportedCard  =  0  ; 
for  (  int   i  =  0  ;  i  <  cards  .  size  (  )  ;  i  ++  )  { 
Card   newCard  =  cards  .  elementAt  (  i  )  ; 
Card   knownCard  =  map  .  get  (  newCard  .  getName  (  )  )  ; 
if  (  knownCard  ==  null  )  { 
if  (  newCard  .  isCreature  (  )  )  { 
String   text  =  getCleanText  (  newCard  .  getText  (  )  )  ; 
if  (  text  .  length  (  )  ==  0  )  { 
String   dbg  =  "\n"  +  newCard  +  " "  +  newCard  .  getId  (  )  +  " as new simple creature supported."  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
newCard  .  setOk  (  "Y"  )  ; 
supportedCard  ++  ; 
continue  ; 
} 
} 
}  else  { 
String   dbg  =  "\n"  +  "Knowncard: "  +  knownCard  +  " "  +  knownCard  .  getId  (  )  +  " -> cloning to "  +  newCard  +  " "  +  newCard  .  getId  (  )  ; 
jTextAreaOut  .  insert  (  dbg  ,  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
jTextAreaOut  .  setCaretPosition  (  jTextAreaOut  .  getText  (  )  .  length  (  )  )  ; 
if  (  knownCard  .  getType  (  )  .  equals  (  newCard  .  getType  (  )  )  )  { 
cloneCard  (  knownCard  ,  newCard  )  ; 
newCard  .  setOk  (  knownCard  .  getOk  (  )  )  ; 
if  (  knownCard  .  getOk  (  )  .  equalsIgnoreCase  (  "Y"  )  )  supportedCard  ++  ; 
continue  ; 
} 
} 
newCard  .  setOk  (  "N"  )  ; 
} 
cardset  .  save  (  )  ; 
return   supportedCard  ; 
} 

class   CardTemplateSetting  { 

String   orgText  =  ""  ; 

String   name  =  ""  ; 

String  [  ]  ability  =  new   String  [  1  ]  ; 

String   powerMod  =  ""  ; 

String   toughnessMod  =  ""  ; 

String   color  =  ""  ; 

String   type  =  ""  ; 

String   subtype  =  ""  ; 
} 

public   int   seekForSimilar  (  String   newSetName  )  { 
return   0  ; 
} 
} 

