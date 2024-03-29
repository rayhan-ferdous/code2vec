package   grafikon  ; 

import   java  .  awt  .  Color  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  swing  .  DefaultListModel  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 





public   class   Graph   extends   javax  .  swing  .  JFrame  { 

ArrayList  <  CoordsList  >  coordsList  =  new   ArrayList  <  CoordsList  >  (  )  ; 

CoordsList   currCoords  =  new   CoordsList  (  ""  ,  new   ArrayList  <  Coords  >  (  )  )  ; 

gFrame   graph  ; 

int   actList  =  0  ; 


public   Graph  (  )  { 
initComponents  (  )  ; 
setLocationRelativeTo  (  null  )  ; 
listFrame  .  setLocationRelativeTo  (  null  )  ; 
inputWarningDialog  .  setLocationRelativeTo  (  null  )  ; 
listNameDialog  .  setLocationRelativeTo  (  null  )  ; 
colorChooserDialog  .  setLocationRelativeTo  (  null  )  ; 
newCoordinateDialog  .  setLocationRelativeTo  (  null  )  ; 
loadListDialog  .  setLocationRelativeTo  (  null  )  ; 
qEDialog  .  setLocationRelativeTo  (  null  )  ; 
trigDialog  .  setLocationRelativeTo  (  null  )  ; 
expDialog  .  setLocationRelativeTo  (  null  )  ; 
absDialog  .  setLocationRelativeTo  (  null  )  ; 
if  (  !  (  new   File  (  System  .  getProperty  (  "user.dir"  )  +  File  .  separator  +  "SerializedCoords"  )  .  exists  (  )  )  )  { 
new   File  (  System  .  getProperty  (  "user.dir"  )  +  File  .  separator  +  "SerializedCoords"  )  .  mkdir  (  )  ; 
} 
loadListFileChooser  .  setCurrentDirectory  (  new   File  (  System  .  getProperty  (  "user.dir"  )  +  File  .  separator  +  "SerializedCoords"  )  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
listFrame  =  new   javax  .  swing  .  JFrame  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
listCoordsList  =  new   javax  .  swing  .  JList  (  )  ; 
deleteSelectedListButton  =  new   javax  .  swing  .  JButton  (  )  ; 
showSelectedList  =  new   javax  .  swing  .  JButton  (  )  ; 
backListButton  =  new   javax  .  swing  .  JButton  (  )  ; 
saveSelectedList  =  new   javax  .  swing  .  JButton  (  )  ; 
loadList  =  new   javax  .  swing  .  JButton  (  )  ; 
addColorToList  =  new   javax  .  swing  .  JButton  (  )  ; 
addCoordinateButton  =  new   javax  .  swing  .  JButton  (  )  ; 
graphSelectedListsButton  =  new   javax  .  swing  .  JButton  (  )  ; 
saveSelectedListsTxt  =  new   javax  .  swing  .  JButton  (  )  ; 
sortSelectedListButton  =  new   javax  .  swing  .  JButton  (  )  ; 
inputWarningDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
warningLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
warningButton  =  new   javax  .  swing  .  JButton  (  )  ; 
listNameDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
listNameLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
listNameTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
listNameApproveButton  =  new   javax  .  swing  .  JButton  (  )  ; 
colorChooserDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
colorChooser  =  new   javax  .  swing  .  JColorChooser  (  )  ; 
getColorButton  =  new   javax  .  swing  .  JButton  (  )  ; 
newCoordinateDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
addCoordinates  =  new   javax  .  swing  .  JButton  (  )  ; 
xCoordInDialog  =  new   javax  .  swing  .  JTextField  (  )  ; 
yCoordInDialog  =  new   javax  .  swing  .  JTextField  (  )  ; 
loadListDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
loadListFileChooser  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
qEDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
aTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
bTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
cTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
fNameTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
genListCoordsButton  =  new   javax  .  swing  .  JButton  (  )  ; 
trigDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
eTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
fTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
dTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
genTrigButton  =  new   javax  .  swing  .  JButton  (  )  ; 
trigNameTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel6  =  new   javax  .  swing  .  JLabel  (  )  ; 
expDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
expGenButton  =  new   javax  .  swing  .  JButton  (  )  ; 
expNameTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel7  =  new   javax  .  swing  .  JLabel  (  )  ; 
expTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel8  =  new   javax  .  swing  .  JLabel  (  )  ; 
absDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
jLabel9  =  new   javax  .  swing  .  JLabel  (  )  ; 
absNameTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
absGenButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jLabel10  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel11  =  new   javax  .  swing  .  JLabel  (  )  ; 
absTF  =  new   javax  .  swing  .  JTextField  (  )  ; 
addButton  =  new   javax  .  swing  .  JButton  (  )  ; 
xCoord  =  new   javax  .  swing  .  JTextField  (  )  ; 
yCoord  =  new   javax  .  swing  .  JTextField  (  )  ; 
endCurrentList  =  new   javax  .  swing  .  JButton  (  )  ; 
jMenuBar1  =  new   javax  .  swing  .  JMenuBar  (  )  ; 
fileMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
showListMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
showGraphMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
jSeparator1  =  new   javax  .  swing  .  JPopupMenu  .  Separator  (  )  ; 
exitMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
funcMI  =  new   javax  .  swing  .  JMenu  (  )  ; 
qEMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
trigMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
sinMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
cosMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
absMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
expMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
mainMenuBar  =  new   javax  .  swing  .  JMenu  (  )  ; 
usersGuideMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
listFrame  .  setTitle  (  "Edit lists"  )  ; 
listFrame  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  600  ,  375  )  )  ; 
listFrame  .  setResizable  (  false  )  ; 
listFrame  .  addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

public   void   windowActivated  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
listFrameWindowActivated  (  evt  )  ; 
} 
}  )  ; 
listCoordsList  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  400  ,  200  )  )  ; 
listCoordsList  .  addMouseListener  (  new   java  .  awt  .  event  .  MouseAdapter  (  )  { 

public   void   mouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
listCoordsListMouseClicked  (  evt  )  ; 
} 
}  )  ; 
jScrollPane1  .  setViewportView  (  listCoordsList  )  ; 
deleteSelectedListButton  .  setText  (  "Delete"  )  ; 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
deleteSelectedListButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
deleteSelectedListButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
showSelectedList  .  setText  (  "Show selected coordinates"  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
showSelectedList  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
showSelectedListActionPerformed  (  evt  )  ; 
} 
}  )  ; 
backListButton  .  setText  (  "Back"  )  ; 
backListButton  .  setEnabled  (  false  )  ; 
backListButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
backListButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
saveSelectedList  .  setText  (  "Save ser"  )  ; 
saveSelectedList  .  setToolTipText  (  "Kiválaszott lista mentése"  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedList  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
saveSelectedListActionPerformed  (  evt  )  ; 
} 
}  )  ; 
loadList  .  setText  (  "Load"  )  ; 
loadList  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
loadListActionPerformed  (  evt  )  ; 
} 
}  )  ; 
addColorToList  .  setText  (  "Select Color"  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addColorToList  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
addColorToListActionPerformed  (  evt  )  ; 
} 
}  )  ; 
addCoordinateButton  .  setText  (  "Edit name"  )  ; 
addCoordinateButton  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
addCoordinateButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
graphSelectedListsButton  .  setText  (  "Graph selected lists"  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
graphSelectedListsButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
graphSelectedListsButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
saveSelectedListsTxt  .  setText  (  "Save txt"  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
saveSelectedListsTxtActionPerformed  (  evt  )  ; 
} 
}  )  ; 
sortSelectedListButton  .  setText  (  "Sort"  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
sortSelectedListButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   listFrameLayout  =  new   javax  .  swing  .  GroupLayout  (  listFrame  .  getContentPane  (  )  )  ; 
listFrame  .  getContentPane  (  )  .  setLayout  (  listFrameLayout  )  ; 
listFrameLayout  .  setHorizontalGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  listFrameLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  268  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  sortSelectedListButton  )  .  addGap  (  16  ,  16  ,  16  )  .  addGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  backListButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addComponent  (  loadList  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addComponent  (  addColorToList  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addComponent  (  graphSelectedListsButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addComponent  (  deleteSelectedListButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addComponent  (  addCoordinateButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  .  addGroup  (  listFrameLayout  .  createSequentialGroup  (  )  .  addComponent  (  saveSelectedList  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  99  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  saveSelectedListsTxt  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  110  ,  Short  .  MAX_VALUE  )  )  .  addComponent  (  showSelectedList  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  215  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  )  ; 
listFrameLayout  .  setVerticalGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  listFrameLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  308  ,  Short  .  MAX_VALUE  )  .  addGroup  (  listFrameLayout  .  createSequentialGroup  (  )  .  addGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  showSelectedList  )  .  addComponent  (  sortSelectedListButton  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  addCoordinateButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  deleteSelectedListButton  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  graphSelectedListsButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  30  ,  Short  .  MAX_VALUE  )  .  addComponent  (  addColorToList  )  .  addGap  (  18  ,  18  ,  18  )  .  addGroup  (  listFrameLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  saveSelectedList  )  .  addComponent  (  saveSelectedListsTxt  )  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  loadList  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  backListButton  )  )  )  .  addContainerGap  (  )  )  )  ; 
inputWarningDialog  .  setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DO_NOTHING_ON_CLOSE  )  ; 
inputWarningDialog  .  setTitle  (  "ERROR"  )  ; 
inputWarningDialog  .  setAlwaysOnTop  (  true  )  ; 
inputWarningDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  400  ,  150  )  )  ; 
inputWarningDialog  .  setModal  (  true  )  ; 
inputWarningDialog  .  setResizable  (  false  )  ; 
warningLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  14  )  )  ; 
warningLabel  .  setText  (  "Wrong coordinate input!"  )  ; 
warningLabel  .  setHorizontalTextPosition  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
warningLabel  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  3000  ,  3000  )  )  ; 
warningLabel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  0  ,  0  )  )  ; 
warningLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  300  ,  300  )  )  ; 
warningButton  .  setText  (  "OK"  )  ; 
warningButton  .  setHorizontalTextPosition  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
warningButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
warningButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   inputWarningDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  inputWarningDialog  .  getContentPane  (  )  )  ; 
inputWarningDialog  .  getContentPane  (  )  .  setLayout  (  inputWarningDialogLayout  )  ; 
inputWarningDialogLayout  .  setHorizontalGroup  (  inputWarningDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputWarningDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  inputWarningDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  inputWarningDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  warningButton  )  .  addGap  (  153  ,  153  ,  153  )  )  .  addGroup  (  inputWarningDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  warningLabel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  372  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  )  )  ; 
inputWarningDialogLayout  .  setVerticalGroup  (  inputWarningDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputWarningDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  warningLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  58  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  warningButton  )  .  addContainerGap  (  40  ,  Short  .  MAX_VALUE  )  )  )  ; 
listNameDialog  .  setTitle  (  "List name"  )  ; 
listNameDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  250  ,  142  )  )  ; 
listNameDialog  .  setModal  (  true  )  ; 
listNameDialog  .  setResizable  (  false  )  ; 
listNameDialog  .  addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

public   void   windowActivated  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
listNameDialogWindowActivated  (  evt  )  ; 
} 
}  )  ; 
listNameLabel  .  setText  (  "Please enter a list name:"  )  ; 
listNameApproveButton  .  setText  (  "OK"  )  ; 
listNameApproveButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
listNameApproveButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   listNameDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  listNameDialog  .  getContentPane  (  )  )  ; 
listNameDialog  .  getContentPane  (  )  .  setLayout  (  listNameDialogLayout  )  ; 
listNameDialogLayout  .  setHorizontalGroup  (  listNameDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  listNameDialogLayout  .  createSequentialGroup  (  )  .  addGroup  (  listNameDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  listNameDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  67  ,  67  ,  67  )  .  addComponent  (  listNameApproveButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  100  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  listNameDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  20  ,  20  ,  20  )  .  addComponent  (  listNameTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  193  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  listNameDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  41  ,  41  ,  41  )  .  addComponent  (  listNameLabel  )  )  )  .  addContainerGap  (  24  ,  Short  .  MAX_VALUE  )  )  )  ; 
listNameDialogLayout  .  setVerticalGroup  (  listNameDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  listNameDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  33  ,  Short  .  MAX_VALUE  )  .  addComponent  (  listNameLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  listNameTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  listNameApproveButton  )  .  addGap  (  23  ,  23  ,  23  )  )  )  ; 
colorChooserDialog  .  setTitle  (  "Color chooser"  )  ; 
colorChooserDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  670  ,  420  )  )  ; 
colorChooserDialog  .  setModal  (  true  )  ; 
colorChooserDialog  .  setResizable  (  false  )  ; 
getColorButton  .  setText  (  "Select"  )  ; 
getColorButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
getColorButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   colorChooserDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  colorChooserDialog  .  getContentPane  (  )  )  ; 
colorChooserDialog  .  getContentPane  (  )  .  setLayout  (  colorChooserDialogLayout  )  ; 
colorChooserDialogLayout  .  setHorizontalGroup  (  colorChooserDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  colorChooserDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  colorChooser  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  601  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  56  ,  Short  .  MAX_VALUE  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  colorChooserDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  596  ,  Short  .  MAX_VALUE  )  .  addComponent  (  getColorButton  )  .  addContainerGap  (  )  )  )  ; 
colorChooserDialogLayout  .  setVerticalGroup  (  colorChooserDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  colorChooserDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  colorChooser  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  316  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  getColorButton  )  .  addContainerGap  (  63  ,  Short  .  MAX_VALUE  )  )  )  ; 
newCoordinateDialog  .  setTitle  (  "New coordinate"  )  ; 
newCoordinateDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  330  ,  130  )  )  ; 
newCoordinateDialog  .  setModal  (  true  )  ; 
newCoordinateDialog  .  setResizable  (  false  )  ; 
addCoordinates  .  setText  (  "Add"  )  ; 
addCoordinates  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
addCoordinatesActionPerformed  (  evt  )  ; 
} 
}  )  ; 
xCoordInDialog  .  setColumns  (  10  )  ; 
xCoordInDialog  .  setText  (  "X coordinate"  )  ; 
xCoordInDialog  .  addFocusListener  (  new   java  .  awt  .  event  .  FocusAdapter  (  )  { 

public   void   focusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordInDialogFocusGained  (  evt  )  ; 
} 

public   void   focusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordInDialogFocusLost  (  evt  )  ; 
} 
}  )  ; 
yCoordInDialog  .  setColumns  (  10  )  ; 
yCoordInDialog  .  setText  (  "Y coordinate"  )  ; 
yCoordInDialog  .  addFocusListener  (  new   java  .  awt  .  event  .  FocusAdapter  (  )  { 

public   void   focusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordInDialogFocusGained  (  evt  )  ; 
} 

public   void   focusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordInDialogFocusLost  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   newCoordinateDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  newCoordinateDialog  .  getContentPane  (  )  )  ; 
newCoordinateDialog  .  getContentPane  (  )  .  setLayout  (  newCoordinateDialogLayout  )  ; 
newCoordinateDialogLayout  .  setHorizontalGroup  (  newCoordinateDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  newCoordinateDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  18  ,  18  ,  18  )  .  addGroup  (  newCoordinateDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  xCoordInDialog  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  yCoordInDialog  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  addCoordinates  )  .  addContainerGap  (  157  ,  Short  .  MAX_VALUE  )  )  )  ; 
newCoordinateDialogLayout  .  setVerticalGroup  (  newCoordinateDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  newCoordinateDialogLayout  .  createSequentialGroup  (  )  .  addGroup  (  newCoordinateDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  newCoordinateDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  xCoordInDialog  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  yCoordInDialog  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  newCoordinateDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  29  ,  29  ,  29  )  .  addComponent  (  addCoordinates  )  )  )  .  addContainerGap  (  61  ,  Short  .  MAX_VALUE  )  )  )  ; 
loadListFileChooser  .  setMultiSelectionEnabled  (  true  )  ; 
javax  .  swing  .  GroupLayout   loadListDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  loadListDialog  .  getContentPane  (  )  )  ; 
loadListDialog  .  getContentPane  (  )  .  setLayout  (  loadListDialogLayout  )  ; 
loadListDialogLayout  .  setHorizontalGroup  (  loadListDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  loadListDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  loadListFileChooser  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  )  ; 
loadListDialogLayout  .  setVerticalGroup  (  loadListDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  loadListDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  loadListFileChooser  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
qEDialog  .  setTitle  (  "Quadratic equation"  )  ; 
qEDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  400  ,  200  )  )  ; 
qEDialog  .  setResizable  (  false  )  ; 
aTextField  .  setColumns  (  1  )  ; 
aTextField  .  setText  (  "0"  )  ; 
bTextField  .  setColumns  (  1  )  ; 
bTextField  .  setText  (  "0"  )  ; 
cTextField  .  setColumns  (  1  )  ; 
cTextField  .  setText  (  "0"  )  ; 
jLabel1  .  setText  (  "x^2 + "  )  ; 
jLabel2  .  setText  (  "x + "  )  ; 
jLabel3  .  setText  (  "Name:"  )  ; 
fNameTextField  .  setColumns  (  10  )  ; 
genListCoordsButton  .  setText  (  "Generate"  )  ; 
genListCoordsButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
genListCoordsButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   qEDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  qEDialog  .  getContentPane  (  )  )  ; 
qEDialog  .  getContentPane  (  )  .  setLayout  (  qEDialogLayout  )  ; 
qEDialogLayout  .  setHorizontalGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  37  ,  37  ,  37  )  .  addComponent  (  aTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  36  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  23  ,  23  ,  23  )  .  addComponent  (  jLabel3  )  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  fNameTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel1  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  bTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  36  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jLabel2  )  )  )  .  addGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  cTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  30  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  64  ,  64  ,  64  )  .  addComponent  (  genListCoordsButton  )  )  )  .  addGap  (  80  ,  80  ,  80  )  )  )  ; 
qEDialogLayout  .  setVerticalGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  qEDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  33  ,  33  ,  33  )  .  addGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  bTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel1  )  .  addComponent  (  aTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel2  )  .  addComponent  (  cTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  44  ,  44  ,  44  )  .  addGroup  (  qEDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  fNameTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel3  )  .  addComponent  (  genListCoordsButton  )  )  .  addContainerGap  (  77  ,  Short  .  MAX_VALUE  )  )  )  ; 
trigDialog  .  setTitle  (  "Trigonometric function"  )  ; 
trigDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  237  ,  171  )  )  ; 
trigDialog  .  setResizable  (  false  )  ; 
jLabel4  .  setText  (  "sin("  )  ; 
jLabel5  .  setText  (  "x) +"  )  ; 
eTF  .  setColumns  (  1  )  ; 
eTF  .  setText  (  "0"  )  ; 
fTF  .  setColumns  (  1  )  ; 
fTF  .  setText  (  "0"  )  ; 
dTF  .  setColumns  (  1  )  ; 
dTF  .  setText  (  "0"  )  ; 
genTrigButton  .  setText  (  "Generate"  )  ; 
genTrigButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
genTrigButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
trigNameTF  .  setColumns  (  10  )  ; 
jLabel6  .  setText  (  "Name:"  )  ; 
javax  .  swing  .  GroupLayout   trigDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  trigDialog  .  getContentPane  (  )  )  ; 
trigDialog  .  getContentPane  (  )  .  setLayout  (  trigDialogLayout  )  ; 
trigDialogLayout  .  setHorizontalGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  trigDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  genTrigButton  )  .  addGroup  (  trigDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel6  )  .  addGap  (  34  ,  34  ,  34  )  .  addGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  trigNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGroup  (  trigDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  dTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel4  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  eTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel5  )  )  )  )  )  .  addGap  (  12  ,  12  ,  12  )  .  addComponent  (  fTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  50  ,  Short  .  MAX_VALUE  )  )  )  ; 
trigDialogLayout  .  setVerticalGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  trigDialogLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  trigNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel6  )  )  .  addGap  (  24  ,  24  ,  24  )  .  addGroup  (  trigDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel4  )  .  addComponent  (  dTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  eTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  fTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel5  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  48  ,  Short  .  MAX_VALUE  )  .  addComponent  (  genTrigButton  )  .  addGap  (  25  ,  25  ,  25  )  )  )  ; 
expDialog  .  setTitle  (  "Exponential function"  )  ; 
expDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  302  ,  188  )  )  ; 
expGenButton  .  setText  (  "Generate"  )  ; 
expGenButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
expGenButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
expNameTF  .  setColumns  (  10  )  ; 
jLabel7  .  setText  (  "Name:"  )  ; 
expTF  .  setColumns  (  1  )  ; 
jLabel8  .  setText  (  "x"  )  ; 
javax  .  swing  .  GroupLayout   expDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  expDialog  .  getContentPane  (  )  )  ; 
expDialog  .  getContentPane  (  )  .  setLayout  (  expDialogLayout  )  ; 
expDialogLayout  .  setHorizontalGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  expDialogLayout  .  createSequentialGroup  (  )  .  addGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  expDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  34  ,  34  ,  34  )  .  addComponent  (  jLabel7  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  expNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  expGenButton  )  )  )  .  addGroup  (  expDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  83  ,  83  ,  83  )  .  addComponent  (  expTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel8  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  15  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addContainerGap  (  141  ,  Short  .  MAX_VALUE  )  )  )  ; 
expDialogLayout  .  setVerticalGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  expDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  19  ,  19  ,  19  )  .  addGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  expNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel7  )  )  .  addGroup  (  expDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  expDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  27  ,  27  ,  27  )  .  addComponent  (  expTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  expDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  jLabel8  )  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  27  ,  Short  .  MAX_VALUE  )  .  addComponent  (  expGenButton  )  .  addGap  (  52  ,  52  ,  52  )  )  )  ; 
absDialog  .  setTitle  (  "Absolute value"  )  ; 
absDialog  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  400  ,  300  )  )  ; 
jLabel9  .  setText  (  "Name:"  )  ; 
absNameTF  .  setColumns  (  10  )  ; 
absGenButton  .  setText  (  "Generate"  )  ; 
absGenButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
absGenButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jLabel10  .  setText  (  "|"  )  ; 
jLabel11  .  setText  (  "x|"  )  ; 
absTF  .  setColumns  (  1  )  ; 
absTF  .  setText  (  "0"  )  ; 
javax  .  swing  .  GroupLayout   absDialogLayout  =  new   javax  .  swing  .  GroupLayout  (  absDialog  .  getContentPane  (  )  )  ; 
absDialog  .  getContentPane  (  )  .  setLayout  (  absDialogLayout  )  ; 
absDialogLayout  .  setHorizontalGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  absDialogLayout  .  createSequentialGroup  (  )  .  addGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  absDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  132  ,  132  ,  132  )  .  addComponent  (  absGenButton  )  )  .  addGroup  (  absDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  25  ,  25  ,  25  )  .  addGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jLabel10  )  .  addGroup  (  absDialogLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel9  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  absNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  absTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel11  )  )  )  .  addContainerGap  (  191  ,  Short  .  MAX_VALUE  )  )  )  ; 
absDialogLayout  .  setVerticalGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  absDialogLayout  .  createSequentialGroup  (  )  .  addGap  (  28  ,  28  ,  28  )  .  addGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel9  )  .  addComponent  (  absNameTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  45  ,  45  ,  45  )  .  addGroup  (  absDialogLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel10  )  .  addComponent  (  absTF  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel11  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  65  ,  Short  .  MAX_VALUE  )  .  addComponent  (  absGenButton  )  .  addGap  (  99  ,  99  ,  99  )  )  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  EXIT_ON_CLOSE  )  ; 
setTitle  (  "GraphER"  )  ; 
setMinimumSize  (  new   java  .  awt  .  Dimension  (  337  ,  150  )  )  ; 
setResizable  (  false  )  ; 
addButton  .  setText  (  "Add coordinate"  )  ; 
addButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
addButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
xCoord  .  setText  (  "X coordinate"  )  ; 
xCoord  .  setNextFocusableComponent  (  yCoord  )  ; 
xCoord  .  addFocusListener  (  new   java  .  awt  .  event  .  FocusAdapter  (  )  { 

public   void   focusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordFocusGained  (  evt  )  ; 
} 

public   void   focusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordFocusLost  (  evt  )  ; 
} 
}  )  ; 
yCoord  .  setText  (  "Y coordinate"  )  ; 
yCoord  .  setNextFocusableComponent  (  addButton  )  ; 
yCoord  .  addFocusListener  (  new   java  .  awt  .  event  .  FocusAdapter  (  )  { 

public   void   focusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordFocusGained  (  evt  )  ; 
} 

public   void   focusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordFocusLost  (  evt  )  ; 
} 
}  )  ; 
endCurrentList  .  setText  (  "Add list"  )  ; 
endCurrentList  .  setEnabled  (  false  )  ; 
endCurrentList  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
endCurrentListActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  setText  (  "File"  )  ; 
showListMenuItem  .  setText  (  "Show list"  )  ; 
showListMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
showListMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  add  (  showListMenuItem  )  ; 
showGraphMenuItem  .  setText  (  "Show graph"  )  ; 
showGraphMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
showGraphMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  add  (  showGraphMenuItem  )  ; 
fileMenu  .  add  (  jSeparator1  )  ; 
exitMenuItem  .  setText  (  "Exit"  )  ; 
exitMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
exitMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  add  (  exitMenuItem  )  ; 
jMenuBar1  .  add  (  fileMenu  )  ; 
funcMI  .  setText  (  "Functions"  )  ; 
qEMI  .  setText  (  "Quadratic equation"  )  ; 
qEMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
qEMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
funcMI  .  add  (  qEMI  )  ; 
trigMenu  .  setText  (  "Trigonometric"  )  ; 
sinMI  .  setText  (  "Sinus"  )  ; 
sinMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
sinMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
trigMenu  .  add  (  sinMI  )  ; 
cosMI  .  setText  (  "Cosinus"  )  ; 
cosMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
cosMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
trigMenu  .  add  (  cosMI  )  ; 
funcMI  .  add  (  trigMenu  )  ; 
absMI  .  setText  (  "Absolute value"  )  ; 
absMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
absMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
funcMI  .  add  (  absMI  )  ; 
expMI  .  setText  (  "Exponential function"  )  ; 
expMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
expMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
funcMI  .  add  (  expMI  )  ; 
jMenuBar1  .  add  (  funcMI  )  ; 
mainMenuBar  .  setText  (  "Help"  )  ; 
usersGuideMenuItem  .  setText  (  "User's Manual"  )  ; 
usersGuideMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
usersGuideMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mainMenuBar  .  add  (  usersGuideMenuItem  )  ; 
jMenuBar1  .  add  (  mainMenuBar  )  ; 
setJMenuBar  (  jMenuBar1  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  getContentPane  (  )  )  ; 
getContentPane  (  )  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGap  (  31  ,  31  ,  31  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  false  )  .  addComponent  (  yCoord  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  xCoord  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  135  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  71  ,  Short  .  MAX_VALUE  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  false  )  .  addComponent  (  addButton  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  endCurrentList  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  .  addGap  (  51  ,  51  ,  51  )  )  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  addButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  endCurrentList  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  xCoord  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  yCoord  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addContainerGap  (  70  ,  Short  .  MAX_VALUE  )  )  )  ; 
pack  (  )  ; 
} 

private   void   xCoordFocusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoord  .  setText  (  (  xCoord  .  getText  (  )  .  equals  (  "X coordinate"  )  )  ?  ""  :  xCoord  .  getText  (  )  )  ; 
} 

private   void   xCoordFocusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoord  .  setText  (  (  xCoord  .  getText  (  )  .  equals  (  ""  )  )  ?  "X coordinate"  :  xCoord  .  getText  (  )  )  ; 
} 

private   void   yCoordFocusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoord  .  setText  (  (  yCoord  .  getText  (  )  .  equals  (  "Y coordinate"  )  )  ?  ""  :  yCoord  .  getText  (  )  )  ; 
} 

private   void   yCoordFocusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoord  .  setText  (  (  yCoord  .  getText  (  )  .  equals  (  ""  )  )  ?  "Y coordinate"  :  yCoord  .  getText  (  )  )  ; 
} 

private   void   addButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
currCoords  .  coords  .  add  (  new   Coords  (  Double  .  parseDouble  (  xCoord  .  getText  (  )  )  ,  Double  .  parseDouble  (  yCoord  .  getText  (  )  )  )  )  ; 
endCurrentList  .  setEnabled  (  true  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong coordinate input!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
xCoord  .  setText  (  "X coordinate"  )  ; 
yCoord  .  setText  (  "Y coordinate"  )  ; 
xCoord  .  requestFocus  (  )  ; 
} 

private   void   deleteSelectedListButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  listCoordsList  .  isSelectionEmpty  (  )  )  { 
if  (  !  backListButton  .  isEnabled  (  )  )  { 
coordsList  .  remove  (  listCoordsList  .  getSelectedIndex  (  )  )  ; 
DefaultListModel   lm  =  (  DefaultListModel  )  listCoordsList  .  getModel  (  )  ; 
lm  .  removeElementAt  (  listCoordsList  .  getSelectedIndex  (  )  )  ; 
listCoordsList  .  setModel  (  lm  )  ; 
}  else  { 
coordsList  .  get  (  actList  )  .  coords  .  remove  (  listCoordsList  .  getSelectedIndex  (  )  )  ; 
DefaultListModel   lm  =  (  DefaultListModel  )  listCoordsList  .  getModel  (  )  ; 
lm  .  removeElementAt  (  listCoordsList  .  getSelectedIndex  (  )  )  ; 
listCoordsList  .  setModel  (  lm  )  ; 
} 
} 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  backListButton  .  isEnabled  (  )  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  !  backListButton  .  isEnabled  (  )  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setEnabled  (  (  backListButton  .  isEnabled  (  )  )  ?  true  :  false  )  ; 
} 

private   void   warningButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
inputWarningDialog  .  setVisible  (  false  )  ; 
} 

private   void   endCurrentListActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
listNameLabel  .  setText  (  "Please enter a list name: "  )  ; 
listNameDialog  .  setVisible  (  true  )  ; 
xCoord  .  setText  (  "X coordinate"  )  ; 
yCoord  .  setText  (  "Y coordinate"  )  ; 
xCoord  .  requestFocus  (  )  ; 
endCurrentList  .  setEnabled  (  false  )  ; 
} 

private   void   showSelectedListActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
actList  =  listCoordsList  .  getSelectedIndex  (  )  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  coords  .  size  (  )  ;  i  ++  )  { 
lm  .  addElement  (  "X: "  +  coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  coords  .  get  (  i  )  .  x  +  " Y: "  +  coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  coords  .  get  (  i  )  .  y  )  ; 
} 
listCoordsList  .  setModel  (  lm  )  ; 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  true  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  false  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setText  (  "Add new"  )  ; 
addCoordinateButton  .  setEnabled  (  true  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
} 

private   void   backListButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  !  coordsList  .  get  (  i  )  .  coords  .  isEmpty  (  )  )  { 
lm  .  addElement  (  coordsList  .  get  (  i  )  .  name  )  ; 
} 
} 
listCoordsList  .  setModel  (  lm  )  ; 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  false  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  true  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setText  (  "Edit name"  )  ; 
addCoordinateButton  .  setEnabled  (  false  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
} 

private   void   listCoordsListMouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
if  (  listCoordsList  .  isSelectionEmpty  (  )  )  { 
if  (  !  backListButton  .  isEnabled  (  )  )  { 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  false  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  true  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setEnabled  (  false  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
}  else  { 
deleteSelectedListButton  .  setEnabled  (  false  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  true  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  false  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setEnabled  (  true  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
} 
}  else  { 
if  (  !  backListButton  .  isEnabled  (  )  )  { 
deleteSelectedListButton  .  setEnabled  (  true  )  ; 
showSelectedList  .  setEnabled  (  true  )  ; 
backListButton  .  setEnabled  (  false  )  ; 
saveSelectedList  .  setEnabled  (  true  )  ; 
saveSelectedListsTxt  .  setEnabled  (  true  )  ; 
loadList  .  setEnabled  (  true  )  ; 
addColorToList  .  setEnabled  (  true  )  ; 
addCoordinateButton  .  setEnabled  (  true  )  ; 
graphSelectedListsButton  .  setEnabled  (  true  )  ; 
sortSelectedListButton  .  setEnabled  (  true  )  ; 
}  else  { 
deleteSelectedListButton  .  setEnabled  (  true  )  ; 
showSelectedList  .  setEnabled  (  false  )  ; 
backListButton  .  setEnabled  (  true  )  ; 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
loadList  .  setEnabled  (  false  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
addCoordinateButton  .  setEnabled  (  true  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
sortSelectedListButton  .  setEnabled  (  false  )  ; 
} 
} 
} 

private   void   listNameApproveButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
boolean   nameEquals  =  false  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  listNameTextField  .  getText  (  )  .  equals  (  coordsList  .  get  (  i  )  .  name  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  !  nameEquals  &&  !  listNameTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
if  (  !  listNameLabel  .  getText  (  )  .  equals  (  "Please add a new list name:"  )  )  { 
currCoords  .  name  =  listNameTextField  .  getText  (  )  ; 
coordsList  .  add  (  new   CoordsList  (  currCoords  .  name  ,  currCoords  .  coords  )  )  ; 
currCoords  .  coords  .  clear  (  )  ; 
}  else  { 
coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  name  =  listNameTextField  .  getText  (  )  ; 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  !  coordsList  .  get  (  i  )  .  coords  .  isEmpty  (  )  )  { 
lm  .  addElement  (  coordsList  .  get  (  i  )  .  name  )  ; 
} 
} 
listCoordsList  .  setModel  (  lm  )  ; 
} 
listNameDialog  .  setVisible  (  false  )  ; 
}  else  { 
warningLabel  .  setText  (  "There is already a list with the same name: "  +  listNameTextField  .  getText  (  )  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
listNameTextField  .  setText  (  ""  )  ; 
} 
} 

private   void   getColorButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
colorChooserDialog  .  setVisible  (  false  )  ; 
coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  color  =  (  !  colorChooser  .  isBackgroundSet  (  )  )  ?  Color  .  BLACK  :  colorChooser  .  getColor  (  )  ; 
} 

private   void   addColorToListActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
colorChooserDialog  .  setVisible  (  true  )  ; 
} 

private   void   listFrameWindowActivated  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
saveSelectedList  .  setEnabled  (  false  )  ; 
saveSelectedListsTxt  .  setEnabled  (  false  )  ; 
addColorToList  .  setEnabled  (  false  )  ; 
} 

private   void   listNameDialogWindowActivated  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
listNameTextField  .  setText  (  ""  )  ; 
} 

private   void   addCoordinateButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  backListButton  .  isEnabled  (  )  )  { 
newCoordinateDialog  .  setVisible  (  true  )  ; 
xCoordInDialog  .  setText  (  "X koordináta"  )  ; 
yCoordInDialog  .  setText  (  "Y koordináta"  )  ; 
}  else  { 
listNameLabel  .  setText  (  "Please add a new list name:"  )  ; 
listNameDialog  .  setVisible  (  true  )  ; 
} 
} 

private   void   xCoordInDialogFocusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordInDialog  .  setText  (  (  xCoordInDialog  .  getText  (  )  .  equals  (  "X coordinate"  )  )  ?  ""  :  xCoordInDialog  .  getText  (  )  )  ; 
} 

private   void   xCoordInDialogFocusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
xCoordInDialog  .  setText  (  (  xCoordInDialog  .  getText  (  )  .  equals  (  ""  )  )  ?  "X cooordinate"  :  xCoordInDialog  .  getText  (  )  )  ; 
} 

private   void   yCoordInDialogFocusLost  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordInDialog  .  setText  (  (  yCoordInDialog  .  getText  (  )  .  equals  (  ""  )  )  ?  "Y coordinate"  :  yCoordInDialog  .  getText  (  )  )  ; 
} 

private   void   yCoordInDialogFocusGained  (  java  .  awt  .  event  .  FocusEvent   evt  )  { 
yCoordInDialog  .  setText  (  (  yCoordInDialog  .  getText  (  )  .  equals  (  "Y coordinate"  )  )  ?  ""  :  yCoordInDialog  .  getText  (  )  )  ; 
} 

private   void   addCoordinatesActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
coordsList  .  get  (  actList  )  .  add  (  new   Coords  (  Double  .  parseDouble  (  xCoordInDialog  .  getText  (  )  )  ,  Double  .  parseDouble  (  yCoordInDialog  .  getText  (  )  )  )  )  ; 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  get  (  actList  )  .  coords  .  size  (  )  ;  i  ++  )  { 
lm  .  addElement  (  "X: "  +  coordsList  .  get  (  actList  )  .  coords  .  get  (  i  )  .  x  +  "Y: "  +  coordsList  .  get  (  actList  )  .  coords  .  get  (  i  )  .  y  )  ; 
} 
listCoordsList  .  setModel  (  lm  )  ; 
xCoordInDialog  .  setText  (  ""  )  ; 
xCoordInDialog  .  requestFocus  (  )  ; 
yCoordInDialog  .  setText  (  "Y coordinate"  )  ; 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong coordin!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
} 

private   void   saveSelectedListActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ArrayList  <  CoordsList  >  list  =  new   ArrayList  <  CoordsList  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  listCoordsList  .  getSelectedIndices  (  )  .  length  ;  i  ++  )  { 
list  .  add  (  coordsList  .  get  (  listCoordsList  .  getSelectedIndices  (  )  [  i  ]  )  )  ; 
} 
if  (  !  list  .  isEmpty  (  )  )  { 
for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  { 
File   f  =  new   File  (  "SerializedCoords"  +  File  .  separator  +  list  .  get  (  i  )  .  name  +  ".ser"  )  ; 
Boolean   overWriteExistingSerFile  =  true  ; 
if  (  f  .  exists  (  )  )  { 
int   response  =  0  ; 
try  { 
response  =  JOptionPane  .  showConfirmDialog  (  null  ,  "Overwrite existing file("  +  f  .  getCanonicalPath  (  )  +  ")?"  ,  "The file already exists"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
if  (  response  ==  JOptionPane  .  CANCEL_OPTION  )  { 
overWriteExistingSerFile  =  false  ; 
} 
} 
if  (  overWriteExistingSerFile  )  { 
try  { 
ObjectOutputStream   oos  =  new   ObjectOutputStream  (  new   FileOutputStream  (  f  .  getAbsolutePath  (  )  )  )  ; 
oos  .  writeObject  (  list  .  get  (  i  )  )  ; 
oos  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
} 

private   void   loadListActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
CoordsList   newList  ; 
Coords   newCoords  ; 
int   returnVal  =  loadListFileChooser  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
String   loadListName  ; 
for  (  int   i  =  0  ;  i  <  loadListFileChooser  .  getSelectedFiles  (  )  .  length  ;  i  ++  )  { 
if  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getAbsolutePath  (  )  .  endsWith  (  ".ser"  )  )  { 
loadListName  =  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getPath  (  )  .  split  (  "\\\\"  )  [  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getPath  (  )  .  split  (  "\\\\"  )  .  length  )  -  1  ]  .  substring  (  0  ,  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getPath  (  )  .  split  (  "\\\\"  )  [  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getPath  (  )  .  split  (  "\\\\"  )  .  length  )  -  1  ]  .  length  (  )  -  4  )  ; 
boolean   nameEquals  =  false  ,  overWriteEnabled  =  true  ; 
for  (  int   j  =  0  ;  j  <  coordsList  .  size  (  )  ;  j  ++  )  { 
if  (  loadListName  .  equals  (  coordsList  .  get  (  j  )  .  name  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  nameEquals  )  { 
int   response  =  0  ; 
response  =  JOptionPane  .  showConfirmDialog  (  null  ,  "Overwrite existing file("  +  loadListName  +  ")?"  ,  "The file already exists"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
if  (  response  ==  JOptionPane  .  CANCEL_OPTION  )  { 
overWriteEnabled  =  false  ; 
} 
} 
if  (  overWriteEnabled  )  { 
try  { 
ObjectInputStream   ois  =  new   ObjectInputStream  (  new   FileInputStream  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  )  )  ; 
newList  =  (  CoordsList  )  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
for  (  int   j  =  0  ;  j  <  coordsList  .  size  (  )  ;  j  ++  )  { 
if  (  loadListName  .  equals  (  coordsList  .  get  (  j  )  .  name  )  )  { 
coordsList  .  set  (  j  ,  newList  )  ; 
} 
} 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
if  (  !  nameEquals  )  { 
try  { 
ObjectInputStream   ois  =  new   ObjectInputStream  (  new   FileInputStream  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  )  )  ; 
newList  =  (  CoordsList  )  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
coordsList  .  add  (  newList  )  ; 
DefaultListModel   lm  =  (  DefaultListModel  )  listCoordsList  .  getModel  (  )  ; 
lm  .  addElement  (  coordsList  .  get  (  coordsList  .  size  (  )  -  1  )  .  name  )  ; 
listCoordsList  .  setModel  (  lm  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
if  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  .  getAbsolutePath  (  )  .  endsWith  (  ".txt"  )  )  { 
try  { 
ArrayList  <  Coords  >  txtCoords  =  new   ArrayList  <  Coords  >  (  )  ; 
BufferedReader   br  =  new   BufferedReader  (  new   FileReader  (  loadListFileChooser  .  getSelectedFiles  (  )  [  i  ]  )  )  ; 
String   txtListName  =  br  .  readLine  (  )  ; 
while  (  br  .  ready  (  )  )  { 
String   tmpLine  =  br  .  readLine  (  )  ; 
txtCoords  .  add  (  new   Coords  (  Double  .  parseDouble  (  tmpLine  .  split  (  " "  )  [  0  ]  )  ,  Double  .  parseDouble  (  tmpLine  .  split  (  " "  )  [  1  ]  )  )  )  ; 
} 
br  .  close  (  )  ; 
boolean   overWriteEnabled  =  true  ,  nameEquals  =  false  ; 
for  (  int   j  =  0  ;  j  <  coordsList  .  size  (  )  ;  j  ++  )  { 
if  (  txtListName  .  equals  (  coordsList  .  get  (  j  )  .  name  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  nameEquals  )  { 
int   response  =  0  ; 
response  =  JOptionPane  .  showConfirmDialog  (  null  ,  "Overwrite existing file("  +  txtListName  +  ")?"  ,  "The list already exists!"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
if  (  response  ==  JOptionPane  .  CANCEL_OPTION  )  { 
overWriteEnabled  =  false  ; 
} 
} 
if  (  overWriteEnabled  )  { 
for  (  int   j  =  0  ;  j  <  coordsList  .  size  (  )  ;  j  ++  )  { 
if  (  txtListName  .  equals  (  coordsList  .  get  (  j  )  .  name  )  )  { 
coordsList  .  set  (  j  ,  new   CoordsList  (  txtListName  ,  txtCoords  )  )  ; 
} 
} 
} 
if  (  !  nameEquals  )  { 
coordsList  .  add  (  new   CoordsList  (  txtListName  ,  txtCoords  )  )  ; 
} 
; 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
for  (  int   j  =  0  ;  j  <  coordsList  .  size  (  )  ;  j  ++  )  { 
if  (  !  coordsList  .  get  (  j  )  .  coords  .  isEmpty  (  )  )  { 
lm  .  addElement  (  coordsList  .  get  (  j  )  .  name  )  ; 
} 
} 
listCoordsList  .  setModel  (  lm  )  ; 
}  catch  (  Exception   ex  )  { 
Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 
} 
} 
} 

private   void   graphSelectedListsButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ArrayList  <  CoordsList  >  graphList  =  new   ArrayList  <  CoordsList  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  listCoordsList  .  getSelectedIndices  (  )  .  length  ;  i  ++  )  { 
graphList  .  add  (  coordsList  .  get  (  listCoordsList  .  getSelectedIndices  (  )  [  i  ]  )  )  ; 
} 
new   gFrame  (  graphList  )  ; 
} 

private   void   saveSelectedListsTxtActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ArrayList  <  CoordsList  >  list  =  new   ArrayList  <  CoordsList  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  listCoordsList  .  getSelectedIndices  (  )  .  length  ;  i  ++  )  { 
list  .  add  (  coordsList  .  get  (  listCoordsList  .  getSelectedIndices  (  )  [  i  ]  )  )  ; 
} 
if  (  !  list  .  isEmpty  (  )  )  { 
for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  { 
File   f  =  new   File  (  "SerializedCoords"  +  File  .  separator  +  "Saved TXT"  +  File  .  separator  +  list  .  get  (  i  )  .  name  +  ".txt"  )  ; 
Boolean   overWriteExistingSerFile  =  true  ; 
if  (  f  .  exists  (  )  )  { 
int   response  =  0  ; 
try  { 
response  =  JOptionPane  .  showConfirmDialog  (  null  ,  "Overwriting existing file: ("  +  f  .  getCanonicalPath  (  )  +  ")?"  ,  "The file already exists"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
if  (  response  ==  JOptionPane  .  CANCEL_OPTION  )  { 
overWriteExistingSerFile  =  false  ; 
} 
} 
if  (  overWriteExistingSerFile  )  { 
try  { 
if  (  !  new   File  (  "SerializedCoords"  +  File  .  separator  +  "Saved TXT"  )  .  exists  (  )  )  { 
new   File  (  "SerializedCoords"  +  File  .  separator  +  "Saved TXT"  )  .  mkdir  (  )  ; 
} 
PrintWriter   pw  =  new   PrintWriter  (  new   FileWriter  (  f  )  )  ; 
pw  .  println  (  list  .  get  (  i  )  .  name  )  ; 
for  (  int   j  =  0  ;  j  <  list  .  get  (  i  )  .  coords  .  size  (  )  ;  j  ++  )  { 
pw  .  println  (  list  .  get  (  i  )  .  coords  .  get  (  j  )  .  x  +  " "  +  list  .  get  (  i  )  .  coords  .  get  (  j  )  .  y  +  " "  +  list  .  get  (  i  )  .  color  )  ; 
} 
pw  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
} 

private   void   sortSelectedListButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
Collections  .  sort  (  coordsList  .  get  (  listCoordsList  .  getSelectedIndex  (  )  )  .  coords  )  ; 
} 

private   void   exitMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
dispose  (  )  ; 
} 

private   void   showGraphMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
graph  =  new   gFrame  (  coordsList  )  ; 
} 

private   void   showListMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
DefaultListModel   lm  =  new   DefaultListModel  (  )  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  !  coordsList  .  get  (  i  )  .  coords  .  isEmpty  (  )  )  { 
lm  .  addElement  (  coordsList  .  get  (  i  )  .  name  )  ; 
} 
} 
listCoordsList  .  setModel  (  lm  )  ; 
listFrame  .  setVisible  (  true  )  ; 
backListButton  .  setEnabled  (  false  )  ; 
graphSelectedListsButton  .  setEnabled  (  false  )  ; 
} 

private   void   qEMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
qEDialog  .  setVisible  (  true  )  ; 
} 

private   void   genListCoordsButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  aTextField  .  getText  (  )  .  equals  (  ""  )  &&  !  bTextField  .  getText  (  )  .  equals  (  ""  )  &&  !  cTextField  .  getText  (  )  .  equals  (  ""  )  &&  !  fNameTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
CoordsList   qECoordsList  ; 
ArrayList  <  Coords  >  qECoords  =  new   ArrayList  <  Coords  >  (  )  ; 
try  { 
for  (  double   x  =  -  10  ;  x  <  11  ;  x  ++  )  { 
qECoords  .  add  (  new   Coords  (  x  ,  (  x  *  x  *  Double  .  parseDouble  (  aTextField  .  getText  (  )  )  +  x  *  Double  .  parseDouble  (  bTextField  .  getText  (  )  )  +  Double  .  parseDouble  (  cTextField  .  getText  (  )  )  )  )  )  ; 
} 
boolean   nameEquals  =  false  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  coordsList  .  get  (  i  )  .  name  .  equals  (  fNameTextField  .  getText  (  )  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  !  nameEquals  )  { 
qECoordsList  =  new   CoordsList  (  fNameTextField  .  getText  (  )  ,  qECoords  )  ; 
if  (  !  qECoords  .  isEmpty  (  )  )  { 
coordsList  .  add  (  qECoordsList  )  ; 
} 
qEDialog  .  setVisible  (  false  )  ; 
}  else  { 
warningLabel  .  setText  (  "The entered name already exists!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong input!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
} 
} 

private   void   sinMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jLabel4  .  setText  (  "sin("  )  ; 
trigDialog  .  setVisible  (  true  )  ; 
} 

private   void   genTrigButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  trigNameTF  .  getText  (  )  .  equals  (  ""  )  &&  !  dTF  .  getText  (  )  .  equals  (  ""  )  &&  !  eTF  .  getText  (  )  .  equals  (  ""  )  &&  !  fTF  .  getText  (  )  .  equals  (  ""  )  )  { 
CoordsList   trigCoordsList  ; 
ArrayList  <  Coords  >  trigCoords  =  new   ArrayList  <  Coords  >  (  )  ; 
try  { 
for  (  double   x  =  -  300  ;  x  <  301  ;  x  ++  )  { 
if  (  jLabel4  .  getText  (  )  .  equals  (  "sin("  )  )  { 
trigCoords  .  add  (  new   Coords  (  x  ,  Double  .  parseDouble  (  dTF  .  getText  (  )  )  *  (  Math  .  sin  (  Math  .  toRadians  (  x  *  Double  .  parseDouble  (  eTF  .  getText  (  )  )  )  )  )  +  Double  .  parseDouble  (  fTF  .  getText  (  )  )  )  )  ; 
}  else   if  (  jLabel4  .  getText  (  )  .  equals  (  "cos("  )  )  { 
trigCoords  .  add  (  new   Coords  (  x  ,  Double  .  parseDouble  (  dTF  .  getText  (  )  )  *  (  Math  .  cos  (  Math  .  toRadians  (  x  *  Double  .  parseDouble  (  eTF  .  getText  (  )  )  )  )  )  +  Double  .  parseDouble  (  fTF  .  getText  (  )  )  )  )  ; 
} 
} 
boolean   nameEquals  =  false  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  coordsList  .  get  (  i  )  .  name  .  equals  (  trigNameTF  .  getText  (  )  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  !  nameEquals  )  { 
trigCoordsList  =  new   CoordsList  (  trigNameTF  .  getText  (  )  ,  trigCoords  )  ; 
if  (  !  trigCoords  .  isEmpty  (  )  )  { 
coordsList  .  add  (  trigCoordsList  )  ; 
} 
trigDialog  .  setVisible  (  false  )  ; 
}  else  { 
warningLabel  .  setText  (  "The entered name already exists!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong input!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
} 
} 

private   void   cosMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jLabel4  .  setText  (  "cos("  )  ; 
trigDialog  .  setVisible  (  true  )  ; 
} 

private   void   expMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
expDialog  .  setVisible  (  true  )  ; 
} 

private   void   expGenButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  expTF  .  getText  (  )  .  equals  (  ""  )  &&  !  expNameTF  .  getText  (  )  .  equals  (  ""  )  )  { 
CoordsList   expCoordsList  ; 
ArrayList  <  Coords  >  expCoords  =  new   ArrayList  <  Coords  >  (  )  ; 
try  { 
for  (  double   x  =  -  10  ;  x  <  11  ;  x  ++  )  { 
expCoords  .  add  (  new   Coords  (  x  ,  Math  .  pow  (  Double  .  parseDouble  (  expTF  .  getText  (  )  )  ,  x  )  )  )  ; 
} 
boolean   nameEquals  =  false  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  coordsList  .  get  (  i  )  .  name  .  equals  (  trigNameTF  .  getText  (  )  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  !  nameEquals  )  { 
expCoordsList  =  new   CoordsList  (  expNameTF  .  getText  (  )  ,  expCoords  )  ; 
if  (  !  expCoords  .  isEmpty  (  )  )  { 
coordsList  .  add  (  expCoordsList  )  ; 
} 
expDialog  .  setVisible  (  false  )  ; 
}  else  { 
warningLabel  .  setText  (  "The entered name already exists!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong input!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
} 
} 

private   void   absGenButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  absTF  .  getText  (  )  .  equals  (  ""  )  &&  !  absNameTF  .  getText  (  )  .  equals  (  ""  )  )  { 
CoordsList   absCoordsList  ; 
ArrayList  <  Coords  >  absCoords  =  new   ArrayList  <  Coords  >  (  )  ; 
try  { 
for  (  double   x  =  -  10  ;  x  <  11  ;  x  ++  )  { 
absCoords  .  add  (  new   Coords  (  x  ,  Math  .  abs  (  x  *  Double  .  parseDouble  (  absTF  .  getText  (  )  )  )  )  )  ; 
} 
boolean   nameEquals  =  false  ; 
for  (  int   i  =  0  ;  i  <  coordsList  .  size  (  )  ;  i  ++  )  { 
if  (  coordsList  .  get  (  i  )  .  name  .  equals  (  trigNameTF  .  getText  (  )  )  )  { 
nameEquals  =  true  ; 
} 
} 
if  (  !  nameEquals  )  { 
absCoordsList  =  new   CoordsList  (  absNameTF  .  getText  (  )  ,  absCoords  )  ; 
if  (  !  absCoords  .  isEmpty  (  )  )  { 
coordsList  .  add  (  absCoordsList  )  ; 
} 
absDialog  .  setVisible  (  false  )  ; 
}  else  { 
warningLabel  .  setText  (  "The entered name already exists!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
}  catch  (  NumberFormatException   ex  )  { 
warningLabel  .  setText  (  "Wrong input!"  )  ; 
inputWarningDialog  .  setVisible  (  true  )  ; 
} 
} 
} 

private   void   absMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
absDialog  .  setVisible  (  true  )  ; 
} 

private   void   usersGuideMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
File   uM  =  new   File  (  "lib/User's Manual.pdf"  )  ; 
Process   p  =  Runtime  .  getRuntime  (  )  .  exec  (  "rundll32 url.dll,FileProtocolHandler "  +  uM  .  getAbsolutePath  (  )  )  ; 
p  .  waitFor  (  )  ; 
}  catch  (  Exception   ex  )  { 
Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 




public   static   void   main  (  String   args  [  ]  )  { 
try  { 
for  (  javax  .  swing  .  UIManager  .  LookAndFeelInfo   info  :  javax  .  swing  .  UIManager  .  getInstalledLookAndFeels  (  )  )  { 
if  (  "Nimbus"  .  equals  (  info  .  getName  (  )  )  )  { 
javax  .  swing  .  UIManager  .  setLookAndFeel  (  info  .  getClassName  (  )  )  ; 
break  ; 
} 
} 
}  catch  (  ClassNotFoundException   ex  )  { 
java  .  util  .  logging  .  Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  InstantiationException   ex  )  { 
java  .  util  .  logging  .  Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IllegalAccessException   ex  )  { 
java  .  util  .  logging  .  Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  javax  .  swing  .  UnsupportedLookAndFeelException   ex  )  { 
java  .  util  .  logging  .  Logger  .  getLogger  (  Graph  .  class  .  getName  (  )  )  .  log  (  java  .  util  .  logging  .  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
java  .  awt  .  EventQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
new   Graph  (  )  .  setVisible  (  true  )  ; 
} 
}  )  ; 
} 

private   javax  .  swing  .  JTextField   aTextField  ; 

private   javax  .  swing  .  JDialog   absDialog  ; 

private   javax  .  swing  .  JButton   absGenButton  ; 

private   javax  .  swing  .  JMenuItem   absMI  ; 

private   javax  .  swing  .  JTextField   absNameTF  ; 

private   javax  .  swing  .  JTextField   absTF  ; 

private   javax  .  swing  .  JButton   addButton  ; 

private   javax  .  swing  .  JButton   addColorToList  ; 

private   javax  .  swing  .  JButton   addCoordinateButton  ; 

private   javax  .  swing  .  JButton   addCoordinates  ; 

private   javax  .  swing  .  JTextField   bTextField  ; 

private   javax  .  swing  .  JButton   backListButton  ; 

private   javax  .  swing  .  JTextField   cTextField  ; 

private   javax  .  swing  .  JColorChooser   colorChooser  ; 

private   javax  .  swing  .  JDialog   colorChooserDialog  ; 

private   javax  .  swing  .  JMenuItem   cosMI  ; 

private   javax  .  swing  .  JTextField   dTF  ; 

private   javax  .  swing  .  JButton   deleteSelectedListButton  ; 

private   javax  .  swing  .  JTextField   eTF  ; 

private   javax  .  swing  .  JButton   endCurrentList  ; 

private   javax  .  swing  .  JMenuItem   exitMenuItem  ; 

private   javax  .  swing  .  JDialog   expDialog  ; 

private   javax  .  swing  .  JButton   expGenButton  ; 

private   javax  .  swing  .  JMenuItem   expMI  ; 

private   javax  .  swing  .  JTextField   expNameTF  ; 

private   javax  .  swing  .  JTextField   expTF  ; 

private   javax  .  swing  .  JTextField   fNameTextField  ; 

private   javax  .  swing  .  JTextField   fTF  ; 

private   javax  .  swing  .  JMenu   fileMenu  ; 

private   javax  .  swing  .  JMenu   funcMI  ; 

private   javax  .  swing  .  JButton   genListCoordsButton  ; 

private   javax  .  swing  .  JButton   genTrigButton  ; 

private   javax  .  swing  .  JButton   getColorButton  ; 

private   javax  .  swing  .  JButton   graphSelectedListsButton  ; 

private   javax  .  swing  .  JDialog   inputWarningDialog  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel10  ; 

private   javax  .  swing  .  JLabel   jLabel11  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabel5  ; 

private   javax  .  swing  .  JLabel   jLabel6  ; 

private   javax  .  swing  .  JLabel   jLabel7  ; 

private   javax  .  swing  .  JLabel   jLabel8  ; 

private   javax  .  swing  .  JLabel   jLabel9  ; 

private   javax  .  swing  .  JMenuBar   jMenuBar1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JPopupMenu  .  Separator   jSeparator1  ; 

private   javax  .  swing  .  JList   listCoordsList  ; 

private   javax  .  swing  .  JFrame   listFrame  ; 

private   javax  .  swing  .  JButton   listNameApproveButton  ; 

private   javax  .  swing  .  JDialog   listNameDialog  ; 

private   javax  .  swing  .  JLabel   listNameLabel  ; 

private   javax  .  swing  .  JTextField   listNameTextField  ; 

private   javax  .  swing  .  JButton   loadList  ; 

private   javax  .  swing  .  JDialog   loadListDialog  ; 

private   javax  .  swing  .  JFileChooser   loadListFileChooser  ; 

private   javax  .  swing  .  JMenu   mainMenuBar  ; 

private   javax  .  swing  .  JDialog   newCoordinateDialog  ; 

private   javax  .  swing  .  JDialog   qEDialog  ; 

private   javax  .  swing  .  JMenuItem   qEMI  ; 

private   javax  .  swing  .  JButton   saveSelectedList  ; 

private   javax  .  swing  .  JButton   saveSelectedListsTxt  ; 

private   javax  .  swing  .  JMenuItem   showGraphMenuItem  ; 

private   javax  .  swing  .  JMenuItem   showListMenuItem  ; 

private   javax  .  swing  .  JButton   showSelectedList  ; 

private   javax  .  swing  .  JMenuItem   sinMI  ; 

private   javax  .  swing  .  JButton   sortSelectedListButton  ; 

private   javax  .  swing  .  JDialog   trigDialog  ; 

private   javax  .  swing  .  JMenu   trigMenu  ; 

private   javax  .  swing  .  JTextField   trigNameTF  ; 

private   javax  .  swing  .  JMenuItem   usersGuideMenuItem  ; 

private   javax  .  swing  .  JButton   warningButton  ; 

private   javax  .  swing  .  JLabel   warningLabel  ; 

private   javax  .  swing  .  JTextField   xCoord  ; 

private   javax  .  swing  .  JTextField   xCoordInDialog  ; 

private   javax  .  swing  .  JTextField   yCoord  ; 

private   javax  .  swing  .  JTextField   yCoordInDialog  ; 
} 

