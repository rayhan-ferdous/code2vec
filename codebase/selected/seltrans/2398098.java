package   org  .  hironico  .  gui  .  table  .  export  ; 

import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  awt  .  event  .  ItemEvent  ; 
import   java  .  awt  .  event  .  ItemListener  ; 
import   java  .  io  .  File  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   javax  .  swing  .  BorderFactory  ; 
import   javax  .  swing  .  ButtonGroup  ; 
import   javax  .  swing  .  JButton  ; 
import   javax  .  swing  .  JCheckBox  ; 
import   javax  .  swing  .  JComboBox  ; 
import   javax  .  swing  .  JDialog  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JInternalFrame  ; 
import   javax  .  swing  .  JLabel  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JProgressBar  ; 
import   javax  .  swing  .  JRadioButton  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  JTextField  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   org  .  apache  .  log4j  .  ConsoleAppender  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  log4j  .  PatternLayout  ; 


















public   class   TableExporterPanel   extends   JPanel  { 

private   static   final   long   serialVersionUID  =  -  682921064684519244L  ; 






public   TableExporterPanel  (  )  { 
this  .  parentInternalFrame  =  null  ; 
this  .  parentFrame  =  null  ; 
this  .  parentDialog  =  null  ; 
jbInit  (  )  ; 
additionalInit  (  )  ; 
} 








public   TableExporterPanel  (  JInternalFrame   parentInternalFrame  )  { 
this  .  parentInternalFrame  =  parentInternalFrame  ; 
this  .  parentFrame  =  null  ; 
this  .  parentDialog  =  null  ; 
jbInit  (  )  ; 
additionalInit  (  )  ; 
} 








public   TableExporterPanel  (  JFrame   parentFrame  )  { 
this  .  parentInternalFrame  =  null  ; 
this  .  parentFrame  =  parentFrame  ; 
this  .  parentDialog  =  null  ; 
jbInit  (  )  ; 
additionalInit  (  )  ; 
} 

public   TableExporterPanel  (  JDialog   dlg  )  { 
this  .  parentFrame  =  null  ; 
this  .  parentInternalFrame  =  null  ; 
this  .  parentDialog  =  dlg  ; 
jbInit  (  )  ; 
additionalInit  (  )  ; 
} 

private   void   additionalInit  (  )  { 
radioButtonGroup  .  add  (  radioAllRows  )  ; 
radioButtonGroup  .  add  (  radioOnlySelectedRows  )  ; 
if  (  !  logger  .  getAllAppenders  (  )  .  hasMoreElements  (  )  )  { 
PatternLayout   layout  =  new   PatternLayout  (  "%-5p [%t]: %m%n"  )  ; 
ConsoleAppender   appender  =  new   ConsoleAppender  (  layout  )  ; 
logger  .  addAppender  (  appender  )  ; 
} 
cmbEncoding  .  removeAllItems  (  )  ; 
Object  [  ]  charsetNames  =  Charset  .  availableCharsets  (  )  .  keySet  (  )  .  toArray  (  )  ; 
for  (  int   cpt  =  0  ;  cpt  <  charsetNames  .  length  ;  cpt  ++  )  cmbEncoding  .  addItem  (  charsetNames  [  cpt  ]  )  ; 
} 




private   void   jbInit  (  )  { 
chkExportToClipboard  .  setText  (  "Export to clipboard"  )  ; 
btnBrowse  .  setText  (  "Browse..."  )  ; 
btnBrowse  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
btnBrowseActionPerformed  (  e  )  ; 
} 
}  )  ; 
txtFileName  .  setText  (  ""  )  ; 
txtFileName  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  150  ,  20  )  )  ; 
txtFileName  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  150  ,  20  )  )  ; 
chkExportToFile  .  setText  (  "Export to file :"  )  ; 
chkExportToFile  .  addItemListener  (  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
chkExportToFileItemStateChanged  (  e  )  ; 
} 
}  )  ; 
radioAllRows  .  setText  (  "Export all rows"  )  ; 
radioAllRows  .  setSelected  (  true  )  ; 
radioOnlySelectedRows  .  setText  (  "Export only selected rows"  )  ; 
lblEncoding  .  setText  (  "Choose the caracter encoding :"  )  ; 
pnlEncoding  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "File format options :"  ,  javax  .  swing  .  border  .  TitledBorder  .  LEADING  ,  javax  .  swing  .  border  .  TitledBorder  .  TOP  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  ,  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  )  ; 
pnlEncoding  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
pnlEncoding  .  add  (  btnScanEncoding  ,  new   java  .  awt  .  GridBagConstraints  (  1  ,  2  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  5  ,  5  )  ,  0  ,  0  )  )  ; 
pnlEncoding  .  add  (  cmbEncoding  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  2  ,  1  ,  1  ,  1.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  5  ,  5  )  ,  0  ,  0  )  )  ; 
pnlEncoding  .  add  (  lblEncoding  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlEncoding  .  add  (  chkUseExcelFormat  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
btnScanEncoding  .  setText  (  "Scan..."  )  ; 
cmbEncoding  .  setEditable  (  true  )  ; 
setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
setBorder  (  BorderFactory  .  createEmptyBorder  (  0  ,  0  ,  0  ,  0  )  )  ; 
setBounds  (  new   java  .  awt  .  Rectangle  (  0  ,  0  ,  485  ,  567  )  )  ; 
add  (  pnlCommands  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  5  ,  1  ,  1  ,  1.0  ,  1.0  ,  java  .  awt  .  GridBagConstraints  .  SOUTH  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  5  ,  5  )  ,  0  ,  0  )  )  ; 
add  (  pnlExportationOptions  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  3  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  BOTH  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
add  (  pnlProgress  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  4  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
add  (  pnlEncoding  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  2  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
add  (  pnlSource  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
add  (  pnlDestination  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Text formatting options :"  ,  javax  .  swing  .  border  .  TitledBorder  .  LEADING  ,  javax  .  swing  .  border  .  TitledBorder  .  TOP  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  ,  new   java  .  awt  .  Color  (  60  ,  60  ,  60  )  )  )  ; 
pnlExportationOptions  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
pnlExportationOptions  .  add  (  chkIncludeHeaders  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  4  ,  2  ,  1  ,  1.0  ,  1.0  ,  java  .  awt  .  GridBagConstraints  .  NORTH  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  5  ,  5  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  chkNewLineForEachRow  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  3  ,  2  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  txtRowSeparator  ,  new   java  .  awt  .  GridBagConstraints  (  1  ,  2  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  WEST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  txtCellSeparator  ,  new   java  .  awt  .  GridBagConstraints  (  1  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  WEST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  chkCellSeparator  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  WEST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  chkNewLineForEachCell  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  2  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlExportationOptions  .  add  (  chkRowSeparator  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  2  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  WEST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
chkCellSeparator  .  setText  (  "Use cell separator :"  )  ; 
chkCellSeparator  .  setSelected  (  true  )  ; 
txtCellSeparator  .  setText  (  ";"  )  ; 
txtCellSeparator  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  50  ,  20  )  )  ; 
txtCellSeparator  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  50  ,  20  )  )  ; 
txtCellSeparator  .  setHorizontalAlignment  (  javax  .  swing  .  JTextField  .  CENTER  )  ; 
chkNewLineForEachCell  .  setText  (  "New line for each cell"  )  ; 
chkRowSeparator  .  setText  (  "Use row separator :"  )  ; 
txtRowSeparator  .  setText  (  ""  )  ; 
txtRowSeparator  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  50  ,  20  )  )  ; 
txtRowSeparator  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  50  ,  20  )  )  ; 
txtRowSeparator  .  setEnabled  (  false  )  ; 
txtRowSeparator  .  setHorizontalAlignment  (  javax  .  swing  .  JTextField  .  CENTER  )  ; 
pnlCommands  .  setBorder  (  null  )  ; 
pnlCommands  .  setLayout  (  new   java  .  awt  .  FlowLayout  (  java  .  awt  .  FlowLayout  .  TRAILING  )  )  ; 
pnlCommands  .  add  (  btnExport  )  ; 
pnlCommands  .  add  (  btnClose  )  ; 
btnExport  .  setText  (  "Export"  )  ; 
btnExport  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  110  ,  27  )  )  ; 
btnClose  .  setText  (  "Close"  )  ; 
btnClose  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  110  ,  27  )  )  ; 
chkCellSeparator  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
chkCellSeparatorActionPerformed  (  e  )  ; 
} 
}  )  ; 
chkRowSeparator  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
chkRowSeparatorActionPerformed  (  e  )  ; 
} 
}  )  ; 
btnExport  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
btnExportActionPerformed  (  e  )  ; 
} 
}  )  ; 
btnClose  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
btnCloseActionPerformed  (  e  )  ; 
} 
}  )  ; 
pnlProgress  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Progress :"  ,  javax  .  swing  .  border  .  TitledBorder  .  LEADING  ,  javax  .  swing  .  border  .  TitledBorder  .  TOP  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  ,  new   java  .  awt  .  Color  (  60  ,  60  ,  60  )  )  )  ; 
pnlProgress  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
pnlProgress  .  add  (  progressBar  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  1.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  5  ,  10  )  ,  0  ,  0  )  )  ; 
pnlProgress  .  add  (  btnCancelCurrentJob  ,  new   java  .  awt  .  GridBagConstraints  (  1  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  WEST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  5  ,  5  )  ,  0  ,  0  )  )  ; 
pnlProgress  .  add  (  chkCloseWhenFinished  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  2  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  EAST  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
btnCancelCurrentJob  .  setText  (  "Stop"  )  ; 
progressBar  .  setStringPainted  (  true  )  ; 
btnCancelCurrentJob  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
btnCancelCurrentJobActionPerformed  (  e  )  ; 
} 
}  )  ; 
chkNewLineForEachRow  .  setText  (  "New line for each row"  )  ; 
chkNewLineForEachRow  .  setSelected  (  true  )  ; 
chkIncludeHeaders  .  setText  (  "Include columns headers"  )  ; 
chkCloseWhenFinished  .  setText  (  "Close this window when export is finished"  )  ; 
chkCloseWhenFinished  .  setHorizontalTextPosition  (  javax  .  swing  .  SwingConstants  .  LEADING  )  ; 
chkCloseWhenFinished  .  setSelected  (  true  )  ; 
pnlSource  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
pnlSource  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Source rows selection :"  ,  javax  .  swing  .  border  .  TitledBorder  .  LEADING  ,  javax  .  swing  .  border  .  TitledBorder  .  TOP  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  ,  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  )  ; 
pnlSource  .  add  (  radioOnlySelectedRows  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlSource  .  add  (  radioAllRows  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  1  ,  1  ,  1.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  5  ,  0  )  ,  0  ,  0  )  )  ; 
chkUseExcelFormat  .  setText  (  "Use EXCEL workbook format"  )  ; 
pnlDestination  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Destination types :"  ,  javax  .  swing  .  border  .  TitledBorder  .  LEADING  ,  javax  .  swing  .  border  .  TitledBorder  .  TOP  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  ,  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  )  ; 
pnlDestination  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
pnlDestination  .  add  (  chkExportToFile  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  0  ,  0  )  ,  0  ,  0  )  )  ; 
pnlDestination  .  add  (  txtFileName  ,  new   java  .  awt  .  GridBagConstraints  (  1  ,  0  ,  1  ,  1  ,  1.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
pnlDestination  .  add  (  btnBrowse  ,  new   java  .  awt  .  GridBagConstraints  (  2  ,  0  ,  1  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  NONE  ,  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ,  0  ,  0  )  )  ; 
pnlDestination  .  add  (  chkExportToClipboard  ,  new   java  .  awt  .  GridBagConstraints  (  0  ,  1  ,  3  ,  1  ,  0.0  ,  0.0  ,  java  .  awt  .  GridBagConstraints  .  CENTER  ,  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ,  new   java  .  awt  .  Insets  (  0  ,  0  ,  5  ,  0  )  ,  0  ,  0  )  )  ; 
chkUseExcelFormat  .  addItemListener  (  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
chkUseExcelFormatItemStateChanged  (  e  )  ; 
} 
}  )  ; 
} 









public   void   chkExportToFileItemStateChanged  (  ItemEvent   e  )  { 
txtFileName  .  setEnabled  (  chkExportToFile  .  isSelected  (  )  )  ; 
btnBrowse  .  setEnabled  (  chkExportToFile  .  isSelected  (  )  )  ; 
cmbEncoding  .  setEnabled  (  !  chkUseExcelFormat  .  isSelected  (  )  &&  chkExportToFile  .  isSelected  (  )  )  ; 
btnScanEncoding  .  setEnabled  (  !  chkUseExcelFormat  .  isSelected  (  )  &&  chkExportToFile  .  isSelected  (  )  )  ; 
chkUseExcelFormat  .  setEnabled  (  chkExportToFile  .  isSelected  (  )  )  ; 
} 









public   void   btnBrowseActionPerformed  (  ActionEvent   e  )  { 
JFileChooser   chooser  ; 
if  (  lastUsedDirectory  !=  null  )  chooser  =  new   JFileChooser  (  lastUsedDirectory  )  ;  else   chooser  =  new   JFileChooser  (  )  ; 
int   retVal  =  chooser  .  showSaveDialog  (  this  )  ; 
if  (  retVal  !=  JFileChooser  .  APPROVE_OPTION  )  return  ; 
lastUsedDirectory  =  chooser  .  getSelectedFile  (  )  .  getAbsolutePath  (  )  ; 
txtFileName  .  setText  (  lastUsedDirectory  )  ; 
} 








public   void   chkCellSeparatorActionPerformed  (  ActionEvent   e  )  { 
txtCellSeparator  .  setEnabled  (  chkCellSeparator  .  isSelected  (  )  )  ; 
} 








public   void   chkRowSeparatorActionPerformed  (  ActionEvent   e  )  { 
txtRowSeparator  .  setEnabled  (  chkRowSeparator  .  isSelected  (  )  )  ; 
} 












public   void   btnExportActionPerformed  (  ActionEvent   e  )  { 
String   msg  ; 
String   title  ; 
if  (  !  chkExportToClipboard  .  isSelected  (  )  &&  !  chkExportToFile  .  isSelected  (  )  )  { 
msg  =  "You should choose at least one export mode :\n"  +  "-export to file\n"  +  "-export to clipboard"  ; 
title  =  "Hey !!!"  ; 
if  (  parentInternalFrame  !=  null  )  JOptionPane  .  showInternalMessageDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ;  else   JOptionPane  .  showMessageDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
if  (  chkCellSeparator  .  isSelected  (  )  &&  txtCellSeparator  .  getText  (  )  .  equals  (  ""  )  )  { 
msg  =  "Empty cell separator !"  ; 
title  =  "Cannot export !"  ; 
if  (  parentInternalFrame  !=  null  )  JOptionPane  .  showInternalMessageDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ;  else   JOptionPane  .  showMessageDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
if  (  chkRowSeparator  .  isSelected  (  )  &&  txtRowSeparator  .  getText  (  )  .  equals  (  ""  )  )  { 
msg  =  "Empty row separator !"  ; 
title  =  "Cannot export !"  ; 
if  (  parentInternalFrame  !=  null  )  JOptionPane  .  showInternalMessageDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ;  else   JOptionPane  .  showMessageDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
if  (  chkCellSeparator  .  isSelected  (  )  &&  chkRowSeparator  .  isSelected  (  )  &&  txtCellSeparator  .  getText  (  )  .  equals  (  chkRowSeparator  .  getText  (  )  )  )  { 
msg  =  "Row and Cell separators are identical !"  ; 
title  =  "Ohoh..."  ; 
if  (  parentInternalFrame  !=  null  )  JOptionPane  .  showInternalMessageDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ;  else   JOptionPane  .  showMessageDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
if  (  chkExportToFile  .  isSelected  (  )  &&  txtFileName  .  getText  (  )  .  equals  (  ""  )  )  { 
msg  =  "You should select a file using the browse button..."  ; 
title  =  "Hey !!!!"  ; 
if  (  parentInternalFrame  !=  null  )  JOptionPane  .  showInternalMessageDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ;  else   JOptionPane  .  showMessageDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return  ; 
} 
File   fichierExport  =  new   File  (  txtFileName  .  getText  (  )  )  ; 
if  (  fichierExport  .  exists  (  )  &&  chkExportToFile  .  isSelected  (  )  )  { 
msg  =  "The file you specifed already exists.\n"  +  "Do you want to overwrite it ?"  ; 
title  =  "Hey !!!"  ; 
int   retValue  ; 
if  (  parentInternalFrame  !=  null  )  retValue  =  JOptionPane  .  showInternalConfirmDialog  (  parentInternalFrame  .  getDesktopPane  (  )  ,  msg  ,  title  ,  JOptionPane  .  YES_NO_OPTION  )  ;  else   retValue  =  JOptionPane  .  showConfirmDialog  (  this  ,  msg  ,  title  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  retValue  !=  JOptionPane  .  YES_OPTION  )  return  ; 
} 
TableExporterThread   thread  =  new   TableExporterThread  (  this  ,  tableToExport  )  ; 
progressBar  .  setValue  (  0  )  ; 
int   nbreCellules  =  tableToExport  .  getRowCount  (  )  *  tableToExport  .  getColumnCount  (  )  ; 
if  (  chkExportToClipboard  .  isSelected  (  )  &&  chkExportToFile  .  isSelected  (  )  )  progressBar  .  setMaximum  (  nbreCellules  *  2  )  ;  else   progressBar  .  setMaximum  (  nbreCellules  )  ; 
cancelAsked  =  false  ; 
thread  .  start  (  )  ; 
} 












public   void   btnCloseActionPerformed  (  ActionEvent   e  )  { 
if  (  parentInternalFrame  !=  null  )  { 
parentInternalFrame  .  dispose  (  )  ; 
return  ; 
} 
if  (  parentFrame  !=  null  )  { 
parentFrame  .  dispose  (  )  ; 
return  ; 
} 
if  (  parentDialog  !=  null  )  { 
parentDialog  .  dispose  (  )  ; 
return  ; 
} 
this  .  setVisible  (  false  )  ; 
} 





public   JTable   getTableToExport  (  )  { 
return   tableToExport  ; 
} 








public   void   setTableToExport  (  JTable   tableToExport  )  { 
this  .  tableToExport  =  tableToExport  ; 
} 






public   boolean   isExportToFile  (  )  { 
return   chkExportToFile  .  isSelected  (  )  ; 
} 





public   void   setExportToFile  (  boolean   exportToFile  )  { 
chkExportToFile  .  setSelected  (  exportToFile  )  ; 
chkExportToFileItemStateChanged  (  null  )  ; 
} 






public   String   getExportFileName  (  )  { 
return   txtFileName  .  getText  (  )  ; 
} 





public   String   getCellSeparator  (  )  { 
return   txtCellSeparator  .  getText  (  )  ; 
} 





public   boolean   isCellSeparated  (  )  { 
return   chkCellSeparator  .  isSelected  (  )  ; 
} 






public   boolean   isNewLineForEachCell  (  )  { 
return   chkNewLineForEachCell  .  isSelected  (  )  ; 
} 






public   boolean   isNewLineForEachRow  (  )  { 
return   chkNewLineForEachRow  .  isSelected  (  )  ; 
} 





public   String   getRowSeparator  (  )  { 
return   txtRowSeparator  .  getText  (  )  ; 
} 





public   boolean   isRowSeparated  (  )  { 
return   chkRowSeparator  .  isSelected  (  )  ; 
} 





public   boolean   isExportToClipboard  (  )  { 
return   chkExportToClipboard  .  isSelected  (  )  ; 
} 





public   void   setExportToClipboard  (  boolean   exportToClipboard  )  { 
chkExportToClipboard  .  setSelected  (  exportToClipboard  )  ; 
} 





public   boolean   isCancelAsked  (  )  { 
return   cancelAsked  ; 
} 









public   void   btnCancelCurrentJobActionPerformed  (  ActionEvent   e  )  { 
cancelAsked  =  true  ; 
} 









public   void   setProgress  (  int   progress  )  { 
progressBar  .  setValue  (  progress  )  ; 
} 





public   JInternalFrame   getParentInternalFrame  (  )  { 
return   parentInternalFrame  ; 
} 








public   void   setParentInternalFrame  (  JInternalFrame   parentInternalFrame  )  { 
this  .  parentInternalFrame  =  parentInternalFrame  ; 
} 





public   JFrame   getParentFrame  (  )  { 
return   parentFrame  ; 
} 





public   void   setParentFrame  (  JFrame   parentFrame  )  { 
this  .  parentFrame  =  parentFrame  ; 
} 





public   String   getCharsetName  (  )  { 
return   cmbEncoding  .  getSelectedItem  (  )  .  toString  (  )  ; 
} 






public   boolean   isOnlySelectedRows  (  )  { 
return   radioOnlySelectedRows  .  isSelected  (  )  ; 
} 











public   void   setOnlySelectedRows  (  boolean   onlySelectedRows  )  { 
radioOnlySelectedRows  .  setSelected  (  onlySelectedRows  )  ; 
} 





public   boolean   isIncludeHeaders  (  )  { 
return   chkIncludeHeaders  .  isSelected  (  )  ; 
} 






public   void   exportLaunched  (  )  { 
btnBrowse  .  setEnabled  (  false  )  ; 
btnClose  .  setEnabled  (  false  )  ; 
btnExport  .  setEnabled  (  false  )  ; 
btnScanEncoding  .  setEnabled  (  false  )  ; 
txtCellSeparator  .  setEnabled  (  false  )  ; 
txtFileName  .  setEnabled  (  false  )  ; 
txtRowSeparator  .  setEnabled  (  false  )  ; 
chkCellSeparator  .  setEnabled  (  false  )  ; 
chkExportToClipboard  .  setEnabled  (  false  )  ; 
chkExportToFile  .  setEnabled  (  false  )  ; 
chkIncludeHeaders  .  setEnabled  (  false  )  ; 
chkNewLineForEachCell  .  setEnabled  (  false  )  ; 
chkNewLineForEachRow  .  setEnabled  (  false  )  ; 
chkRowSeparator  .  setEnabled  (  false  )  ; 
cmbEncoding  .  setEnabled  (  false  )  ; 
btnCancelCurrentJob  .  setEnabled  (  true  )  ; 
} 






public   void   exportEnded  (  )  { 
btnBrowse  .  setEnabled  (  true  )  ; 
btnClose  .  setEnabled  (  true  )  ; 
btnExport  .  setEnabled  (  true  )  ; 
btnScanEncoding  .  setEnabled  (  true  )  ; 
txtCellSeparator  .  setEnabled  (  true  )  ; 
txtFileName  .  setEnabled  (  true  )  ; 
txtRowSeparator  .  setEnabled  (  true  )  ; 
chkCellSeparator  .  setEnabled  (  true  )  ; 
chkExportToClipboard  .  setEnabled  (  true  )  ; 
chkExportToFile  .  setEnabled  (  true  )  ; 
chkIncludeHeaders  .  setEnabled  (  true  )  ; 
chkNewLineForEachCell  .  setEnabled  (  true  )  ; 
chkNewLineForEachRow  .  setEnabled  (  true  )  ; 
chkRowSeparator  .  setEnabled  (  true  )  ; 
cmbEncoding  .  setEnabled  (  true  )  ; 
btnCancelCurrentJob  .  setEnabled  (  false  )  ; 
if  (  chkCloseWhenFinished  .  isSelected  (  )  )  { 
btnCloseActionPerformed  (  null  )  ; 
} 
} 









public   void   chkUseExcelFormatItemStateChanged  (  ItemEvent   e  )  { 
if  (  chkUseExcelFormat  .  isSelected  (  )  )  chkExportToFile  .  setSelected  (  true  )  ; 
cmbEncoding  .  setEnabled  (  !  chkUseExcelFormat  .  isSelected  (  )  &&  chkExportToFile  .  isSelected  (  )  )  ; 
btnScanEncoding  .  setEnabled  (  !  chkUseExcelFormat  .  isSelected  (  )  &&  chkExportToFile  .  isSelected  (  )  )  ; 
} 







public   boolean   isExportToExcel  (  )  { 
return   chkUseExcelFormat  .  isSelected  (  )  ; 
} 

private   JCheckBox   chkExportToFile  =  new   JCheckBox  (  )  ; 

private   JCheckBox   chkExportToClipboard  =  new   JCheckBox  (  )  ; 

private   JTextField   txtFileName  =  new   JTextField  (  )  ; 

private   JButton   btnBrowse  =  new   JButton  (  )  ; 

private   JPanel   pnlExportationOptions  =  new   JPanel  (  )  ; 

private   JCheckBox   chkCellSeparator  =  new   JCheckBox  (  )  ; 

private   JTextField   txtCellSeparator  =  new   JTextField  (  )  ; 

private   JCheckBox   chkNewLineForEachCell  =  new   JCheckBox  (  )  ; 

private   JCheckBox   chkRowSeparator  =  new   JCheckBox  (  )  ; 

private   JTextField   txtRowSeparator  =  new   JTextField  (  )  ; 

private   JPanel   pnlCommands  =  new   JPanel  (  )  ; 

private   JButton   btnExport  =  new   JButton  (  )  ; 

private   JButton   btnClose  =  new   JButton  (  )  ; 

private   JPanel   pnlProgress  =  new   JPanel  (  )  ; 

private   JTable   tableToExport  ; 

private   JProgressBar   progressBar  =  new   JProgressBar  (  )  ; 

private   JButton   btnCancelCurrentJob  =  new   JButton  (  )  ; 

private   boolean   cancelAsked  ; 

private   JInternalFrame   parentInternalFrame  =  null  ; 

private   JFrame   parentFrame  =  null  ; 

private   JDialog   parentDialog  =  null  ; 

private   JPanel   pnlEncoding  =  new   JPanel  (  )  ; 

private   JComboBox   cmbEncoding  =  new   JComboBox  (  )  ; 

private   JButton   btnScanEncoding  =  new   JButton  (  )  ; 

private   JCheckBox   chkNewLineForEachRow  =  new   JCheckBox  (  )  ; 

private   JRadioButton   radioOnlySelectedRows  =  new   JRadioButton  (  )  ; 

private   JRadioButton   radioAllRows  =  new   JRadioButton  (  )  ; 

private   ButtonGroup   radioButtonGroup  =  new   ButtonGroup  (  )  ; 


private   static   Logger   logger  =  Logger  .  getLogger  (  "com.caindosuez.dsi.emc.murex.common.gui"  )  ; 

private   JCheckBox   chkIncludeHeaders  =  new   JCheckBox  (  )  ; 

private   String   lastUsedDirectory  ; 

private   JCheckBox   chkCloseWhenFinished  =  new   JCheckBox  (  )  ; 

private   JLabel   lblEncoding  =  new   JLabel  (  )  ; 

private   JPanel   pnlSource  =  new   JPanel  (  )  ; 

private   JCheckBox   chkUseExcelFormat  =  new   JCheckBox  (  )  ; 

private   JPanel   pnlDestination  =  new   JPanel  (  )  ; 
} 

