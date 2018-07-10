import   java  .  util  .  *  ; 
import   java  .  util  .  prefs  .  *  ; 
import   java  .  io  .  *  ; 

public   class   PrefsPanel   extends   javax  .  swing  .  JFrame  { 

protected   int   timeFormat  =  ClntComm  .  MINUTES  ; 

protected   boolean   animateIcons  =  true  ; 

protected   int   saveInterval  =  60  ; 

protected   int   allowedIdle  =  0  ,  idleAction  ; 

protected   long   countdown  ; 

protected   float   countpay  ; 

protected   String   idleProject  ; 

private   ClntComm   clntComm  ; 

private   String   themePack  ,  kdeTheme  ,  gtkTheme  ; 

private   static   boolean   timeoutLibrary  =  false  ; 

private   java  .  text  .  NumberFormat   dollarFormat  ; 

static  { 
try  { 
System  .  loadLibrary  (  "timeout"  )  ; 
timeoutLibrary  =  true  ; 
}  catch  (  UnsatisfiedLinkError   e  )  { 
timeoutLibrary  =  false  ; 
} 
} 


public   PrefsPanel  (  ClntComm   parent  )  { 
clntComm  =  parent  ; 
dollarFormat  =  java  .  text  .  NumberFormat  .  getInstance  (  )  ; 
dollarFormat  .  setMinimumFractionDigits  (  2  )  ; 
dollarFormat  .  setMaximumFractionDigits  (  2  )  ; 
readPrefs  (  )  ; 
initComponents  (  )  ; 
if  (  !  themePack  .  equals  (  ""  )  )  toggleThemePack  (  )  ; 
if  (  !  kdeTheme  .  equals  (  ""  )  )  toggleKDE  (  )  ; 
if  (  !  gtkTheme  .  equals  (  ""  )  )  toggleGTK  (  )  ; 
} 






private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
timeFormatGroup  =  new   javax  .  swing  .  ButtonGroup  (  )  ; 
idleGroup  =  new   javax  .  swing  .  ButtonGroup  (  )  ; 
tabbedPane  =  new   javax  .  swing  .  JTabbedPane  (  )  ; 
prefsPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
prefsInputPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
generalLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
timeFormatLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
minuteButton  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
secondButton  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
save1Label  =  new   javax  .  swing  .  JLabel  (  )  ; 
saveField  =  new   javax  .  swing  .  JTextField  (  )  ; 
save2Label  =  new   javax  .  swing  .  JLabel  (  )  ; 
showIconCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
prefsButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
prefsOKButton  =  new   javax  .  swing  .  JButton  (  )  ; 
prefsCancelButton  =  new   javax  .  swing  .  JButton  (  )  ; 
skinsPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
skinsInputPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
skinsLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
themeCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
themeField  =  new   javax  .  swing  .  JTextField  (  )  ; 
themeBrowse  =  new   javax  .  swing  .  JButton  (  )  ; 
gtkCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
gtkField  =  new   javax  .  swing  .  JTextField  (  )  ; 
gtkBrowse  =  new   javax  .  swing  .  JButton  (  )  ; 
kdeCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
kdeField  =  new   javax  .  swing  .  JTextField  (  )  ; 
kdeBrowse  =  new   javax  .  swing  .  JButton  (  )  ; 
skinsButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
skinsOKButton  =  new   javax  .  swing  .  JButton  (  )  ; 
skinsCancelButton  =  new   javax  .  swing  .  JButton  (  )  ; 
getContentPane  (  )  .  setLayout  (  new   java  .  awt  .  GridLayout  (  1  ,  0  )  )  ; 
addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

public   void   windowClosing  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
exitForm  (  evt  )  ; 
} 
}  )  ; 
prefsPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
prefsPanel  .  addComponentListener  (  new   java  .  awt  .  event  .  ComponentAdapter  (  )  { 

public   void   componentShown  (  java  .  awt  .  event  .  ComponentEvent   evt  )  { 
showPrefs  (  evt  )  ; 
} 
}  )  ; 
prefsInputPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
generalLabel  .  setHorizontalAlignment  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
generalLabel  .  setText  (  "General Properties"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  10  ,  0  ,  5  ,  0  )  ; 
prefsInputPanel  .  add  (  generalLabel  ,  gridBagConstraints  )  ; 
timeFormatLabel  .  setText  (  "Show time in:  "  )  ; 
prefsInputPanel  .  add  (  timeFormatLabel  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
minuteButton  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
minuteButton  .  setSelected  (  timeFormat  ==  ClntComm  .  MINUTES  )  ; 
minuteButton  .  setText  (  "Minutes"  )  ; 
timeFormatGroup  .  add  (  minuteButton  )  ; 
prefsInputPanel  .  add  (  minuteButton  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
secondButton  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
secondButton  .  setSelected  (  timeFormat  ==  ClntComm  .  SECONDS  )  ; 
secondButton  .  setText  (  "Seconds"  )  ; 
timeFormatGroup  .  add  (  secondButton  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
prefsInputPanel  .  add  (  secondButton  ,  gridBagConstraints  )  ; 
save1Label  .  setText  (  "Save info every "  )  ; 
prefsInputPanel  .  add  (  save1Label  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
saveField  .  setColumns  (  3  )  ; 
saveField  .  setText  (  Integer  .  toString  (  saveInterval  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
prefsInputPanel  .  add  (  saveField  ,  gridBagConstraints  )  ; 
save2Label  .  setText  (  " seconds"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
prefsInputPanel  .  add  (  save2Label  ,  gridBagConstraints  )  ; 
showIconCheckBox  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
showIconCheckBox  .  setSelected  (  animateIcons  )  ; 
showIconCheckBox  .  setText  (  "Show Animated Icons"  )  ; 
showIconCheckBox  .  setMargin  (  new   java  .  awt  .  Insets  (  6  ,  2  ,  2  ,  2  )  )  ; 
showIconCheckBox  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
prefsInputPanel  .  add  (  showIconCheckBox  ,  gridBagConstraints  )  ; 
prefsPanel  .  add  (  prefsInputPanel  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
prefsOKButton  .  setText  (  "OK"  )  ; 
prefsOKButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
savePrefs  (  evt  )  ; 
} 
}  )  ; 
prefsButtonPanel  .  add  (  prefsOKButton  )  ; 
prefsCancelButton  .  setText  (  "Cancel"  )  ; 
prefsCancelButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
cancel  (  evt  )  ; 
} 
}  )  ; 
prefsButtonPanel  .  add  (  prefsCancelButton  )  ; 
prefsPanel  .  add  (  prefsButtonPanel  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
tabbedPane  .  addTab  (  "General"  ,  null  ,  prefsPanel  ,  ""  )  ; 
skinsPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
skinsPanel  .  addComponentListener  (  new   java  .  awt  .  event  .  ComponentAdapter  (  )  { 

public   void   componentShown  (  java  .  awt  .  event  .  ComponentEvent   evt  )  { 
showSkins  (  evt  )  ; 
} 
}  )  ; 
skinsInputPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
skinsLabel  .  setText  (  "Load Themes/Skins"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
skinsInputPanel  .  add  (  skinsLabel  ,  gridBagConstraints  )  ; 
themeCheckBox  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
themeCheckBox  .  setSelected  (  !  themePack  .  equals  (  ""  )  )  ; 
themeCheckBox  .  setText  (  "Use Theme Pack: "  )  ; 
themeCheckBox  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleThemePack  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  themeCheckBox  ,  gridBagConstraints  )  ; 
themeField  .  setColumns  (  25  )  ; 
themeField  .  setText  (  themePack  )  ; 
themeField  .  setEnabled  (  false  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
skinsInputPanel  .  add  (  themeField  ,  gridBagConstraints  )  ; 
themeBrowse  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
themeBrowse  .  setText  (  "Browse..."  )  ; 
themeBrowse  .  setEnabled  (  false  )  ; 
themeBrowse  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
findTheme  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  themeBrowse  ,  gridBagConstraints  )  ; 
gtkCheckBox  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
gtkCheckBox  .  setSelected  (  !  gtkTheme  .  equals  (  ""  )  )  ; 
gtkCheckBox  .  setText  (  "Use GTK Theme: "  )  ; 
gtkCheckBox  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleGTK  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  gtkCheckBox  ,  gridBagConstraints  )  ; 
gtkField  .  setColumns  (  25  )  ; 
gtkField  .  setText  (  gtkTheme  )  ; 
gtkField  .  setEnabled  (  false  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
skinsInputPanel  .  add  (  gtkField  ,  gridBagConstraints  )  ; 
gtkBrowse  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
gtkBrowse  .  setText  (  "Browse..."  )  ; 
gtkBrowse  .  setEnabled  (  false  )  ; 
gtkBrowse  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
findGTK  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  gtkBrowse  ,  gridBagConstraints  )  ; 
kdeCheckBox  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
kdeCheckBox  .  setSelected  (  !  kdeTheme  .  equals  (  ""  )  )  ; 
kdeCheckBox  .  setText  (  "Use KDE Theme: "  )  ; 
kdeCheckBox  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleKDE  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  kdeCheckBox  ,  gridBagConstraints  )  ; 
kdeField  .  setColumns  (  25  )  ; 
kdeField  .  setText  (  kdeTheme  )  ; 
kdeField  .  setEnabled  (  false  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
skinsInputPanel  .  add  (  kdeField  ,  gridBagConstraints  )  ; 
kdeBrowse  .  setForeground  (  new   java  .  awt  .  Color  (  102  ,  102  ,  153  )  )  ; 
kdeBrowse  .  setText  (  "Browse..."  )  ; 
kdeBrowse  .  setEnabled  (  false  )  ; 
kdeBrowse  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
findKDE  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
skinsInputPanel  .  add  (  kdeBrowse  ,  gridBagConstraints  )  ; 
skinsPanel  .  add  (  skinsInputPanel  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
skinsOKButton  .  setText  (  "OK"  )  ; 
skinsOKButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
savePrefs  (  evt  )  ; 
} 
}  )  ; 
skinsButtonPanel  .  add  (  skinsOKButton  )  ; 
skinsCancelButton  .  setText  (  "Cancel"  )  ; 
skinsCancelButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
cancel  (  evt  )  ; 
} 
}  )  ; 
skinsButtonPanel  .  add  (  skinsCancelButton  )  ; 
skinsPanel  .  add  (  skinsButtonPanel  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
tabbedPane  .  addTab  (  "Skins"  ,  null  ,  skinsPanel  ,  ""  )  ; 
getContentPane  (  )  .  add  (  tabbedPane  )  ; 
pack  (  )  ; 
} 

private   void   showSkins  (  java  .  awt  .  event  .  ComponentEvent   evt  )  { 
getRootPane  (  )  .  setDefaultButton  (  skinsOKButton  )  ; 
} 

private   void   findKDE  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
final   javax  .  swing  .  JFileChooser   fc  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
File   theme  =  fc  .  getSelectedFile  (  )  ; 
kdeTheme  =  theme  .  toString  (  )  ; 
kdeField  .  setText  (  themePack  )  ; 
} 
} 

private   void   findGTK  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
final   javax  .  swing  .  JFileChooser   fc  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
File   theme  =  fc  .  getSelectedFile  (  )  ; 
gtkTheme  =  theme  .  toString  (  )  ; 
gtkField  .  setText  (  themePack  )  ; 
} 
} 

private   void   findTheme  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
final   javax  .  swing  .  JFileChooser   fc  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
File   theme  =  fc  .  getSelectedFile  (  )  ; 
themePack  =  theme  .  toString  (  )  ; 
themeField  .  setText  (  themePack  )  ; 
} 
} 

private   void   toggleKDE  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleKDE  (  )  ; 
} 

private   void   toggleKDE  (  )  { 
boolean   kdetoggle  =  kdeCheckBox  .  isSelected  (  )  ; 
boolean   themetoggle  =  kdetoggle  ||  gtkCheckBox  .  isSelected  (  )  ; 
kdeField  .  setEnabled  (  kdetoggle  )  ; 
kdeBrowse  .  setEnabled  (  kdetoggle  )  ; 
themeCheckBox  .  setEnabled  (  !  themetoggle  )  ; 
if  (  themetoggle  )  themeField  .  setEnabled  (  false  )  ; 
if  (  themetoggle  )  themeBrowse  .  setEnabled  (  false  )  ; 
} 

private   void   toggleGTK  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleGTK  (  )  ; 
} 

private   void   toggleGTK  (  )  { 
boolean   gtktoggle  =  gtkCheckBox  .  isSelected  (  )  ; 
boolean   themetoggle  =  gtktoggle  ||  kdeCheckBox  .  isSelected  (  )  ; 
gtkField  .  setEnabled  (  gtktoggle  )  ; 
gtkBrowse  .  setEnabled  (  gtktoggle  )  ; 
themeCheckBox  .  setEnabled  (  !  themetoggle  )  ; 
if  (  themetoggle  )  themeField  .  setEnabled  (  false  )  ; 
if  (  themetoggle  )  themeBrowse  .  setEnabled  (  false  )  ; 
} 

private   void   toggleThemePack  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleThemePack  (  )  ; 
} 

private   void   toggleThemePack  (  )  { 
boolean   toggle  =  themeCheckBox  .  isSelected  (  )  ; 
themeField  .  setEnabled  (  toggle  )  ; 
themeBrowse  .  setEnabled  (  toggle  )  ; 
kdeCheckBox  .  setEnabled  (  !  toggle  )  ; 
if  (  toggle  )  kdeField  .  setEnabled  (  false  )  ; 
if  (  toggle  )  kdeBrowse  .  setEnabled  (  false  )  ; 
gtkCheckBox  .  setEnabled  (  !  toggle  )  ; 
if  (  toggle  )  gtkField  .  setEnabled  (  false  )  ; 
if  (  toggle  )  gtkBrowse  .  setEnabled  (  false  )  ; 
} 

private   void   showPrefs  (  java  .  awt  .  event  .  ComponentEvent   evt  )  { 
getRootPane  (  )  .  setDefaultButton  (  prefsOKButton  )  ; 
} 

private   void   savePrefs  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
savePrefs  (  )  ; 
clntComm  .  reload  (  )  ; 
exitForm  (  )  ; 
} 

private   void   cancel  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
exitForm  (  )  ; 
} 


private   void   exitForm  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
exitForm  (  )  ; 
} 

private   void   exitForm  (  )  { 
setVisible  (  false  )  ; 
dispose  (  )  ; 
} 




private   void   readPrefs  (  )  { 
Preferences   prefs  =  Preferences  .  userRoot  (  )  .  node  (  "CsltComm"  )  ; 
timeFormat  =  prefs  .  getInt  (  "timeFormat"  ,  ClntComm  .  MINUTES  )  ; 
animateIcons  =  prefs  .  getBoolean  (  "animations"  ,  true  )  ; 
saveInterval  =  prefs  .  getInt  (  "saveInterval"  ,  60  )  ; 
themePack  =  prefs  .  get  (  "theme"  ,  ""  )  ; 
kdeTheme  =  prefs  .  get  (  "kde"  ,  ""  )  ; 
gtkTheme  =  prefs  .  get  (  "gtk"  ,  ""  )  ; 
} 

private   void   savePrefs  (  )  { 
try  { 
Preferences   prefs  =  Preferences  .  userRoot  (  )  .  node  (  "CsltComm"  )  ; 
prefs  .  putInt  (  "timeFormat"  ,  secondButton  .  isSelected  (  )  ?  ClntComm  .  SECONDS  :  ClntComm  .  MINUTES  )  ; 
prefs  .  putBoolean  (  "animations"  ,  showIconCheckBox  .  isSelected  (  )  )  ; 
prefs  .  putInt  (  "saveInterval"  ,  Integer  .  parseInt  (  saveField  .  getText  (  )  )  )  ; 
if  (  themeCheckBox  .  isSelected  (  )  )  prefs  .  put  (  "theme"  ,  themeField  .  getText  (  )  )  ;  else   prefs  .  put  (  "theme"  ,  ""  )  ; 
if  (  kdeCheckBox  .  isSelected  (  )  )  prefs  .  put  (  "kde"  ,  kdeField  .  getText  (  )  )  ;  else   prefs  .  put  (  "kde"  ,  ""  )  ; 
if  (  gtkCheckBox  .  isSelected  (  )  )  prefs  .  put  (  "gtk"  ,  gtkField  .  getText  (  )  )  ;  else   prefs  .  put  (  "gtk"  ,  ""  )  ; 
prefs  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  err  .  println  (  "Problem saving prefs file..."  +  e  )  ; 
} 
} 

private   javax  .  swing  .  JLabel   generalLabel  ; 

private   javax  .  swing  .  JButton   gtkBrowse  ; 

private   javax  .  swing  .  JCheckBox   gtkCheckBox  ; 

private   javax  .  swing  .  JTextField   gtkField  ; 

private   javax  .  swing  .  ButtonGroup   idleGroup  ; 

private   javax  .  swing  .  JButton   kdeBrowse  ; 

private   javax  .  swing  .  JCheckBox   kdeCheckBox  ; 

private   javax  .  swing  .  JTextField   kdeField  ; 

private   javax  .  swing  .  JRadioButton   minuteButton  ; 

private   javax  .  swing  .  JPanel   prefsButtonPanel  ; 

private   javax  .  swing  .  JButton   prefsCancelButton  ; 

private   javax  .  swing  .  JPanel   prefsInputPanel  ; 

private   javax  .  swing  .  JButton   prefsOKButton  ; 

private   javax  .  swing  .  JPanel   prefsPanel  ; 

private   javax  .  swing  .  JLabel   save1Label  ; 

private   javax  .  swing  .  JLabel   save2Label  ; 

private   javax  .  swing  .  JTextField   saveField  ; 

private   javax  .  swing  .  JRadioButton   secondButton  ; 

private   javax  .  swing  .  JCheckBox   showIconCheckBox  ; 

private   javax  .  swing  .  JPanel   skinsButtonPanel  ; 

private   javax  .  swing  .  JButton   skinsCancelButton  ; 

private   javax  .  swing  .  JPanel   skinsInputPanel  ; 

private   javax  .  swing  .  JLabel   skinsLabel  ; 

private   javax  .  swing  .  JButton   skinsOKButton  ; 

private   javax  .  swing  .  JPanel   skinsPanel  ; 

private   javax  .  swing  .  JTabbedPane   tabbedPane  ; 

private   javax  .  swing  .  JButton   themeBrowse  ; 

private   javax  .  swing  .  JCheckBox   themeCheckBox  ; 

private   javax  .  swing  .  JTextField   themeField  ; 

private   javax  .  swing  .  ButtonGroup   timeFormatGroup  ; 

private   javax  .  swing  .  JLabel   timeFormatLabel  ; 
} 

