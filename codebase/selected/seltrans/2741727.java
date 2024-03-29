package   reports  .  circulation  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  sql  .  Connection  ; 
import   java  .  sql  .  ResultSet  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  sql  .  Statement  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  swing  .  DefaultListModel  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  filechooser  .  FileFilter  ; 
import   javax  .  swing  .  table  .  DefaultTableModel  ; 
import   logUtil  .  SwingWorker  ; 
import   reports  .  utility  .  CSVWriter  ; 
import   reports  .  utility  .  StaticValues  ; 
import   reports  .  utility  .  database  .  ConnectionPoolFactory  ; 
import   tools  .  StringProcessor  ; 





public   class   StockVerification   extends   javax  .  swing  .  JInternalFrame  { 


private   javax  .  swing  .  DefaultListModel   dlm  =  null  ; 

private   javax  .  swing  .  table  .  DefaultTableModel   dtm  =  null  ; 

File   createFile  =  null  ; 

public   StockVerification  (  )  { 
initComponents  (  )  ; 
jProgressBar1  .  setStringPainted  (  true  )  ; 
setLegendTextPaneContent  (  )  ; 
dialogLegend  .  setSize  (  538  ,  420  )  ; 
dlm  =  new   DefaultListModel  (  )  ; 
jList1  .  setModel  (  dlm  )  ; 
String  [  ]  cols  =  {  "Accessionno/barcode"  ,  "Entrydate"  }  ; 
dtm  =  new   DefaultTableModel  (  cols  ,  0  )  ; 
jTable1  .  setModel  (  dtm  )  ; 
} 

public   void   setLegendTextPaneContent  (  )  { 
tpLegend  .  setContentType  (  "text/html"  )  ; 
tpLegend  .  setText  (  "<HTML><BODY><P>The files generated during StockVerification process are</P><OL>	<LI><b>Yet_To_Be_Verified.csv :</b> List of	items that are not in the Stock verification list. You need to	verify in the library whether these items are available physically.	<LI>Checked_Out.csv : List of all	items that are checked out on loan	<LI>At_Circulation_Desk.csv : These	items are reserved ones and are available at Circulation desk	<LI>Declared_Lost_By_Users.csv : List	of items declared lost by the patrons (Users)	<LI>Deleted.csv : Items deleted at	earlier occasions.	<LI>Found_Missing.csv : Items found	missing during earlier stock verifications	<LI>Invalid_Items.csv : The items	entered in the stock verification list are invalid. They do not have	any valid entry in the catalogue	<LI>Sent_For_Binding.csv : Items sent	for Binding	<LI>Separated_For_Binding.csv : Items	separated for binding	<LI>Weeded_Out.csv : Weeded out items	<LI>Consolidated_Report.csv	: All the above items with their status are included in this report</OL></BODY></HTML>"  )  ; 
} 

public   void   finishProcess  (  )  { 
jProgressBar1  .  setString  (  "Done"  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Stock verification process completed"  ,  "Stock verification process completed"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
} 

public   void   setProgressBarText  (  String   text  )  { 
jProgressBar1  .  setString  (  text  )  ; 
} 






public   void   addToMessages  (  String   message  )  { 
int   size  =  dlm  .  getSize  (  )  ; 
System  .  out  .  println  (  "DLM size is ::"  +  size  )  ; 
if  (  size  >  19  )  { 
dlm  .  removeElementAt  (  0  )  ; 
dlm  .  addElement  (  message  )  ; 
}  else  { 
dlm  .  addElement  (  message  )  ; 
} 
} 

private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
buttonGroup1  =  new   javax  .  swing  .  ButtonGroup  (  )  ; 
jDialog1  =  new   javax  .  swing  .  JDialog  (  )  ; 
jPanel11  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField3  =  new   javax  .  swing  .  JTextField  (  )  ; 
jBrowseButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel12  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel6  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel13  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLoadButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton11  =  new   javax  .  swing  .  JButton  (  )  ; 
jDialog2  =  new   javax  .  swing  .  JDialog  (  )  ; 
jPanel14  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel7  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField4  =  new   javax  .  swing  .  JTextField  (  )  ; 
jGoButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel15  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel8  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel16  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton12  =  new   javax  .  swing  .  JButton  (  )  ; 
jDialog3  =  new   javax  .  swing  .  JDialog  (  )  ; 
jPanel17  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel9  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField5  =  new   javax  .  swing  .  JTextField  (  )  ; 
jGoButton2  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel18  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel10  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel19  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton13  =  new   javax  .  swing  .  JButton  (  )  ; 
jDialog4  =  new   javax  .  swing  .  JDialog  (  )  ; 
jScrollPane2  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTable1  =  new   javax  .  swing  .  JTable  (  )  ; 
jPanel21  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton14  =  new   javax  .  swing  .  JButton  (  )  ; 
jDialog5  =  new   javax  .  swing  .  JDialog  (  )  ; 
jPanel20  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel11  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField6  =  new   javax  .  swing  .  JTextField  (  )  ; 
jBrowseButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel22  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel12  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel23  =  new   javax  .  swing  .  JPanel  (  )  ; 
jExportButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton16  =  new   javax  .  swing  .  JButton  (  )  ; 
dialogLegend  =  new   javax  .  swing  .  JDialog  (  )  ; 
jPanel7  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton18  =  new   javax  .  swing  .  JButton  (  )  ; 
jScrollPane3  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
tpLegend  =  new   javax  .  swing  .  JTextPane  (  )  ; 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanel4  =  new   javax  .  swing  .  JPanel  (  )  ; 
jRadioButton1  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
jRadioButton3  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
jPanel5  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanel6  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanel8  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanel25  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
tfClassificationNumber  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
cbShelvingLocation  =  new   javax  .  swing  .  JComboBox  (  )  ; 
jPanel2  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField1  =  new   javax  .  swing  .  JTextField  (  )  ; 
jButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jList1  =  new   javax  .  swing  .  JList  (  )  ; 
jButton2  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton3  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton4  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton5  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton6  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton7  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton15  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel3  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField2  =  new   javax  .  swing  .  JTextField  (  )  ; 
jButton8  =  new   javax  .  swing  .  JButton  (  )  ; 
jPanel9  =  new   javax  .  swing  .  JPanel  (  )  ; 
jProgressBar1  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
jPanel10  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton9  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton17  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton10  =  new   javax  .  swing  .  JButton  (  )  ; 
jDialog1  .  setTitle  (  "Load File"  )  ; 
jDialog1  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
jDialog1  .  getContentPane  (  )  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jDialog1  .  getContentPane  (  )  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel11  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  )  ; 
jPanel11  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  176  ,  40  )  )  ; 
jLabel5  .  setText  (  "File Location :"  )  ; 
jPanel11  .  add  (  jLabel5  )  ; 
jTextField3  .  setColumns  (  10  )  ; 
jPanel11  .  add  (  jTextField3  )  ; 
jBrowseButton  .  setText  (  "..."  )  ; 
jBrowseButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jBrowseButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel11  .  add  (  jBrowseButton  )  ; 
jDialog1  .  getContentPane  (  )  .  add  (  jPanel11  )  ; 
jPanel12  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  99  ,  40  )  )  ; 
jLabel6  .  setForeground  (  new   java  .  awt  .  Color  (  165  ,  0  ,  0  )  )  ; 
jPanel12  .  add  (  jLabel6  )  ; 
jDialog1  .  getContentPane  (  )  .  add  (  jPanel12  )  ; 
jPanel13  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  40  )  )  ; 
jLoadButton  .  setText  (  "LoadFile"  )  ; 
jLoadButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jLoadButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel13  .  add  (  jLoadButton  )  ; 
jButton11  .  setText  (  "Close"  )  ; 
jButton11  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton11ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel13  .  add  (  jButton11  )  ; 
jDialog1  .  getContentPane  (  )  .  add  (  jPanel13  )  ; 
jDialog2  .  setTitle  (  "Search"  )  ; 
jDialog2  .  getContentPane  (  )  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jDialog2  .  getContentPane  (  )  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel14  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  )  ; 
jLabel7  .  setText  (  "Accession number/barcode :"  )  ; 
jPanel14  .  add  (  jLabel7  )  ; 
jTextField4  .  setColumns  (  10  )  ; 
jTextField4  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jTextField4ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel14  .  add  (  jTextField4  )  ; 
jGoButton1  .  setText  (  "Go"  )  ; 
jGoButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jGoButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel14  .  add  (  jGoButton1  )  ; 
jDialog2  .  getContentPane  (  )  .  add  (  jPanel14  )  ; 
jPanel15  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  99  ,  40  )  )  ; 
jLabel8  .  setForeground  (  new   java  .  awt  .  Color  (  165  ,  0  ,  0  )  )  ; 
jPanel15  .  add  (  jLabel8  )  ; 
jDialog2  .  getContentPane  (  )  .  add  (  jPanel15  )  ; 
jButton12  .  setText  (  "Close"  )  ; 
jButton12  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton12ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel16  .  add  (  jButton12  )  ; 
jDialog2  .  getContentPane  (  )  .  add  (  jPanel16  )  ; 
jDialog3  .  setTitle  (  "Delete"  )  ; 
jDialog3  .  getContentPane  (  )  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jDialog3  .  getContentPane  (  )  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel17  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  )  ; 
jLabel9  .  setText  (  "Accession number/barcode :"  )  ; 
jPanel17  .  add  (  jLabel9  )  ; 
jTextField5  .  setColumns  (  10  )  ; 
jTextField5  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jTextField5ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel17  .  add  (  jTextField5  )  ; 
jGoButton2  .  setText  (  "Go"  )  ; 
jGoButton2  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jGoButton2ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel17  .  add  (  jGoButton2  )  ; 
jDialog3  .  getContentPane  (  )  .  add  (  jPanel17  )  ; 
jPanel18  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  99  ,  40  )  )  ; 
jLabel10  .  setForeground  (  new   java  .  awt  .  Color  (  165  ,  0  ,  0  )  )  ; 
jPanel18  .  add  (  jLabel10  )  ; 
jDialog3  .  getContentPane  (  )  .  add  (  jPanel18  )  ; 
jButton13  .  setText  (  "Close"  )  ; 
jButton13  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton13ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel19  .  add  (  jButton13  )  ; 
jDialog3  .  getContentPane  (  )  .  add  (  jPanel19  )  ; 
jDialog4  .  setTitle  (  "Last 20 entries"  )  ; 
jTable1  .  setModel  (  new   javax  .  swing  .  table  .  DefaultTableModel  (  new   Object  [  ]  [  ]  {  {  null  ,  null  ,  null  ,  null  }  ,  {  null  ,  null  ,  null  ,  null  }  ,  {  null  ,  null  ,  null  ,  null  }  ,  {  null  ,  null  ,  null  ,  null  }  }  ,  new   String  [  ]  {  "Title 1"  ,  "Title 2"  ,  "Title 3"  ,  "Title 4"  }  )  )  ; 
jScrollPane2  .  setViewportView  (  jTable1  )  ; 
jDialog4  .  getContentPane  (  )  .  add  (  jScrollPane2  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
jButton14  .  setText  (  "Close"  )  ; 
jButton14  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton14ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel21  .  add  (  jButton14  )  ; 
jDialog4  .  getContentPane  (  )  .  add  (  jPanel21  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
jDialog5  .  setTitle  (  "Load File"  )  ; 
jDialog5  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
jDialog5  .  getContentPane  (  )  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jDialog5  .  getContentPane  (  )  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel20  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  )  ; 
jPanel20  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  176  ,  40  )  )  ; 
jLabel11  .  setText  (  "Save list to file : "  )  ; 
jPanel20  .  add  (  jLabel11  )  ; 
jTextField6  .  setColumns  (  10  )  ; 
jPanel20  .  add  (  jTextField6  )  ; 
jBrowseButton1  .  setText  (  "..."  )  ; 
jBrowseButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jBrowseButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel20  .  add  (  jBrowseButton1  )  ; 
jDialog5  .  getContentPane  (  )  .  add  (  jPanel20  )  ; 
jPanel22  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  99  ,  40  )  )  ; 
jLabel12  .  setForeground  (  new   java  .  awt  .  Color  (  165  ,  0  ,  0  )  )  ; 
jPanel22  .  add  (  jLabel12  )  ; 
jDialog5  .  getContentPane  (  )  .  add  (  jPanel22  )  ; 
jPanel23  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  40  )  )  ; 
jExportButton1  .  setText  (  "Export"  )  ; 
jExportButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jExportButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel23  .  add  (  jExportButton1  )  ; 
jButton16  .  setText  (  "Close"  )  ; 
jButton16  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton16ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel23  .  add  (  jButton16  )  ; 
jDialog5  .  getContentPane  (  )  .  add  (  jPanel23  )  ; 
jButton18  .  setText  (  "Close"  )  ; 
jButton18  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton18ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel7  .  add  (  jButton18  )  ; 
dialogLegend  .  getContentPane  (  )  .  add  (  jPanel7  ,  java  .  awt  .  BorderLayout  .  PAGE_END  )  ; 
tpLegend  .  setEditable  (  false  )  ; 
jScrollPane3  .  setViewportView  (  tpLegend  )  ; 
dialogLegend  .  getContentPane  (  )  .  add  (  jScrollPane3  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
setClosable  (  true  )  ; 
setIconifiable  (  true  )  ; 
setMaximizable  (  true  )  ; 
setResizable  (  true  )  ; 
setTitle  (  "Stock verification tool"  )  ; 
getContentPane  (  )  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  getContentPane  (  )  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel1  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Stock verification mode"  )  )  ; 
jPanel1  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jPanel1  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
buttonGroup1  .  add  (  jRadioButton1  )  ; 
jRadioButton1  .  setSelected  (  true  )  ; 
jRadioButton1  .  setText  (  "Complete"  )  ; 
jRadioButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jRadioButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel4  .  add  (  jRadioButton1  )  ; 
buttonGroup1  .  add  (  jRadioButton3  )  ; 
jRadioButton3  .  setText  (  "Sectional"  )  ; 
jRadioButton3  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jRadioButton3ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel4  .  add  (  jRadioButton3  )  ; 
jPanel1  .  add  (  jPanel4  )  ; 
jPanel5  .  setLayout  (  new   java  .  awt  .  CardLayout  (  )  )  ; 
jPanel5  .  add  (  jPanel6  ,  "card2"  )  ; 
jPanel8  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jPanel8  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel25  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jLabel1  .  setText  (  "Initial characters of classification number"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  EAST  ; 
jPanel25  .  add  (  jLabel1  ,  gridBagConstraints  )  ; 
tfClassificationNumber  .  setColumns  (  15  )  ; 
jPanel25  .  add  (  tfClassificationNumber  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
jLabel2  .  setText  (  "Shelving location"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  EAST  ; 
jPanel25  .  add  (  jLabel2  ,  gridBagConstraints  )  ; 
cbShelvingLocation  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "Any"  }  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel25  .  add  (  cbShelvingLocation  ,  gridBagConstraints  )  ; 
jPanel8  .  add  (  jPanel25  )  ; 
jPanel5  .  add  (  jPanel8  ,  "sectional"  )  ; 
jPanel1  .  add  (  jPanel5  )  ; 
getContentPane  (  )  .  add  (  jPanel1  )  ; 
jPanel2  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Items added to Stock verification list and Utilities"  )  )  ; 
jPanel2  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jLabel3  .  setText  (  "Accession number/Barcode"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
jPanel2  .  add  (  jLabel3  ,  gridBagConstraints  )  ; 
jTextField1  .  setColumns  (  15  )  ; 
jTextField1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jTextField1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
jPanel2  .  add  (  jTextField1  ,  gridBagConstraints  )  ; 
jButton1  .  setText  (  "Add to list"  )  ; 
jButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  2  ; 
jPanel2  .  add  (  jButton1  ,  gridBagConstraints  )  ; 
jScrollPane1  .  setViewportView  (  jList1  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  gridwidth  =  3  ; 
gridBagConstraints  .  gridheight  =  7  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
jPanel2  .  add  (  jScrollPane1  ,  gridBagConstraints  )  ; 
jButton2  .  setText  (  "Load file"  )  ; 
jButton2  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton2ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton2  ,  gridBagConstraints  )  ; 
jButton3  .  setText  (  "Clear stored data"  )  ; 
jButton3  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton3ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton3  ,  gridBagConstraints  )  ; 
jButton4  .  setText  (  "Search for an item in the list"  )  ; 
jButton4  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton4ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  3  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton4  ,  gridBagConstraints  )  ; 
jButton5  .  setText  (  "Delete an item"  )  ; 
jButton5  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton5ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  4  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton5  ,  gridBagConstraints  )  ; 
jButton6  .  setText  (  "Show last 20 entries"  )  ; 
jButton6  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton6ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  5  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton6  ,  gridBagConstraints  )  ; 
jButton7  .  setText  (  "Total count of items in the list"  )  ; 
jButton7  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton7ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  6  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton7  ,  gridBagConstraints  )  ; 
jButton15  .  setText  (  "Export"  )  ; 
jButton15  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton15ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  7  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
jPanel2  .  add  (  jButton15  ,  gridBagConstraints  )  ; 
getContentPane  (  )  .  add  (  jPanel2  )  ; 
jPanel3  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  ,  "Target folder to generate report"  )  )  ; 
jLabel4  .  setText  (  "Select target folder to generate report"  )  ; 
jPanel3  .  add  (  jLabel4  )  ; 
jTextField2  .  setColumns  (  20  )  ; 
jPanel3  .  add  (  jTextField2  )  ; 
jButton8  .  setText  (  "..."  )  ; 
jButton8  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton8ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel3  .  add  (  jButton8  )  ; 
getContentPane  (  )  .  add  (  jPanel3  )  ; 
jPanel9  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
jPanel9  .  add  (  jProgressBar1  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
getContentPane  (  )  .  add  (  jPanel9  )  ; 
jPanel10  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createEtchedBorder  (  )  )  ; 
jButton9  .  setText  (  "Generate report"  )  ; 
jButton9  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton9ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel10  .  add  (  jButton9  )  ; 
jButton17  .  setText  (  "File names and their content"  )  ; 
jButton17  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton17ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel10  .  add  (  jButton17  )  ; 
jButton10  .  setText  (  "Close"  )  ; 
jButton10  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton10ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel10  .  add  (  jButton10  )  ; 
getContentPane  (  )  .  add  (  jPanel10  )  ; 
pack  (  )  ; 
} 

private   void   jRadioButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
(  (  java  .  awt  .  CardLayout  )  jPanel5  .  getLayout  (  )  )  .  show  (  jPanel5  ,  "card2"  )  ; 
tfClassificationNumber  .  setText  (  ""  )  ; 
cbShelvingLocation  .  setSelectedItem  (  "Any"  )  ; 
} 

private   void   jRadioButton3ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
(  (  java  .  awt  .  CardLayout  )  jPanel5  .  getLayout  (  )  )  .  show  (  jPanel5  ,  "sectional"  )  ; 
cbShelvingLocation  .  removeAllItems  (  )  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select location_id, location from location where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rs  =  stmt  .  executeQuery  (  sql  )  ; 
cbShelvingLocation  .  addItem  (  "Any"  )  ; 
int   i  =  1  ; 
while  (  rs  .  next  (  )  )  { 
cbShelvingLocation  .  insertItemAt  (  rs  .  getString  (  "location"  )  ,  i  )  ; 
i  ++  ; 
} 
stmt  .  close  (  )  ; 
rs  .  close  (  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 

private   void   jButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   bCode  =  jTextField1  .  getText  (  )  ; 
Calendar   currentDate  =  Calendar  .  getInstance  (  )  ; 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "yyyy/MMM/dd HH:mm:ss"  )  ; 
String   dateNow  =  formatter  .  format  (  currentDate  .  getTime  (  )  )  ; 
String   add  =  "Item "  +  jTextField1  .  getText  (  )  +  " is added to the list on "  +  dateNow  ; 
String   available  =  "Item "  +  bCode  +  " is already available in the list"  ; 
if  (  !  bCode  .  equals  (  ""  )  )  { 
try  { 
int   count  =  0  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where barcode = '"  +  bCode  +  "' and library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rscount  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rscount  .  next  (  )  )  { 
count  +=  rscount  .  getInt  (  1  )  ; 
} 
System  .  out  .  println  (  "Barcode Count---"  +  sql  )  ; 
if  (  count  ==  0  )  { 
String   sql1  =  "insert into stock_verification_list (library_id, barcode, entry_id, entry_date) values ("  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  ", '"  +  bCode  +  "', '"  +  StaticValues  .  getInstance  (  )  .  getLoginPatronId  (  )  +  "', current_timestamp)"  ; 
java  .  sql  .  Statement   stmt1  =  con  .  createStatement  (  )  ; 
stmt1  .  executeUpdate  (  sql1  )  ; 
addToMessages  (  add  )  ; 
jTextField1  .  setText  (  ""  )  ; 
stmt1  .  close  (  )  ; 
}  else  { 
addToMessages  (  available  )  ; 
jTextField1  .  selectAll  (  )  ; 
} 
stmt  .  close  (  )  ; 
con  .  close  (  )  ; 
bCode  .  trim  (  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please Enter the text"  )  ; 
} 
} 

public   void   setMessage  (  String   message  )  { 
jLabel6  .  setText  (  message  )  ; 
} 

public   void   setExportMessage  (  String   message  )  { 
jLabel12  .  setText  (  message  )  ; 
} 

public   void   setButtonEnable  (  boolean   val  )  { 
jLoadButton  .  setEnabled  (  val  )  ; 
} 

public   String   getClassificationNumber  (  )  { 
return   tfClassificationNumber  .  getText  (  )  .  trim  (  )  ; 
} 

public   String   getShelvingLocation  (  )  { 
return   cbShelvingLocation  .  getSelectedItem  (  )  .  toString  (  )  ; 
} 

public   void   showMessage  (  String   message  )  { 
JOptionPane  .  showConfirmDialog  (  this  ,  message  ,  "Error"  ,  JOptionPane  .  WARNING_MESSAGE  )  ; 
} 

public   void   setProgressBarcMaxCount  (  int   maxval  )  { 
jProgressBar1  .  setMaximum  (  maxval  )  ; 
} 

public   void   setProgressBarcCurrentCount  (  int   maxval  )  { 
jProgressBar1  .  setValue  (  maxval  )  ; 
} 

public   void   setGenerateButtonEnable  (  boolean   value  )  { 
jButton9  .  setEnabled  (  value  )  ; 
} 

private   void   jTextField1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton1  .  doClick  (  )  ; 
} 

private   void   jButton2ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog1  .  setSize  (  444  ,  130  )  ; 
jDialog1  .  setLocation  (  200  ,  50  )  ; 
jDialog1  .  setVisible  (  true  )  ; 
} 

private   void   jBrowseButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jTextField3  .  setEditable  (  true  )  ; 
javax  .  swing  .  JFileChooser   filechoose  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
filechoose  .  setFileSelectionMode  (  javax  .  swing  .  JFileChooser  .  FILES_ONLY  )  ; 
filechoose  .  showOpenDialog  (  this  )  ; 
jTextField3  .  setText  (  filechoose  .  getSelectedFile  (  )  .  getAbsolutePath  (  )  )  ; 
} 

private   void   jLoadButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  jTextField3  .  getText  (  )  .  equals  (  ""  )  )  { 
LoadFile   lf  =  new   LoadFile  (  new   File  (  jTextField3  .  getText  (  )  )  ,  this  )  ; 
lf  .  start  (  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select the file"  )  ; 
} 
} 

private   void   jButton10ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
this  .  dispose  (  )  ; 
} 

private   void   jButton11ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog1  .  dispose  (  )  ; 
} 

private   void   jButton3ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
int   count  =  0  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rscount  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rscount  .  next  (  )  )  { 
count  +=  rscount  .  getInt  (  1  )  ; 
} 
int   reply  =  JOptionPane  .  showConfirmDialog  (  this  ,  "This will delete all the data from stock verification list. Are you sure you want to do this? "  ,  ""  ,  JOptionPane  .  YES_NO_OPTION  ,  JOptionPane  .  PLAIN_MESSAGE  )  ; 
if  (  count  !=  0  )  { 
if  (  reply  ==  JOptionPane  .  YES_OPTION  )  { 
String   sql1  =  "delete from stock_verification_list where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt1  =  con  .  createStatement  (  )  ; 
stmt1  .  executeUpdate  (  sql1  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Deleted "  +  count  +  " items from the list"  )  ; 
stmt1  .  close  (  )  ; 
} 
rscount  .  close  (  )  ; 
stmt  .  close  (  )  ; 
con  .  close  (  )  ; 
addToMessages  (  count  +  " Items removed from the list"  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  this  ,  "There are no items in the list to delete"  )  ; 
} 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 

private   void   jGoButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  jTextField4  .  getText  (  )  .  equals  (  ""  )  )  { 
try  { 
int   count  =  0  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where barcode = '"  +  jTextField4  .  getText  (  )  +  "' and library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rscount  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rscount  .  next  (  )  )  { 
count  +=  rscount  .  getInt  (  1  )  ; 
} 
if  (  count  ==  1  )  { 
jLabel8  .  setText  (  "Item "  +  jTextField4  .  getText  (  )  +  " is already added to the list"  )  ; 
}  else  { 
jLabel8  .  setText  (  "Item "  +  jTextField4  .  getText  (  )  +  " is not in the list"  )  ; 
} 
stmt  .  close  (  )  ; 
con  .  close  (  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please enter the text"  )  ; 
} 
} 

private   void   jButton12ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog2  .  dispose  (  )  ; 
} 

private   void   jButton4ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog2  .  setSize  (  444  ,  130  )  ; 
jDialog2  .  setLocation  (  200  ,  150  )  ; 
jDialog2  .  setVisible  (  true  )  ; 
} 

private   void   jGoButton2ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  jTextField5  .  getText  (  )  .  equals  (  ""  )  )  { 
try  { 
int   count  =  0  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where barcode = '"  +  jTextField5  .  getText  (  )  +  "' and library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rscount  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rscount  .  next  (  )  )  { 
count  +=  rscount  .  getInt  (  1  )  ; 
} 
if  (  count  ==  1  )  { 
String   sql1  =  "delete from stock_verification_list where barcode = '"  +  jTextField5  .  getText  (  )  +  "' and library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt1  =  con  .  createStatement  (  )  ; 
stmt  .  executeUpdate  (  sql1  )  ; 
jLabel10  .  setText  (  "Item "  +  jTextField5  .  getText  (  )  +  " is deleted from the list"  )  ; 
}  else  { 
jLabel10  .  setText  (  "Item "  +  jTextField5  .  getText  (  )  +  " is not in the list"  )  ; 
} 
stmt  .  close  (  )  ; 
con  .  close  (  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please enter the text"  )  ; 
} 
} 

private   void   jButton13ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog3  .  dispose  (  )  ; 
} 

private   void   jButton5ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog3  .  setSize  (  444  ,  130  )  ; 
jDialog3  .  setLocation  (  200  ,  150  )  ; 
jDialog3  .  setVisible  (  true  )  ; 
} 

private   void   jTextField4ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jGoButton1  .  doClick  (  )  ; 
} 

private   void   jTextField5ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jGoButton2  .  doClick  (  )  ; 
} 

private   void   jButton6ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "SELECT barcode, entry_date FROM stock_verification_list WHERE library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  " ORDER BY entry_date DESC LIMIT 20"  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rs  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rs  .  next  (  )  )  { 
Object  [  ]  rows  =  {  rs  .  getString  (  1  )  ,  rs  .  getString  (  2  )  }  ; 
dtm  .  addRow  (  rows  )  ; 
} 
jDialog4  .  setSize  (  500  ,  250  )  ; 
jDialog4  .  setLocation  (  200  ,  150  )  ; 
jDialog4  .  setVisible  (  true  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 

private   void   jButton7ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
int   count  =  0  ; 
String   entrydate  =  null  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rscount  =  stmt  .  executeQuery  (  sql  )  ; 
while  (  rscount  .  next  (  )  )  { 
count  +=  rscount  .  getInt  (  1  )  ; 
} 
rscount  .  close  (  )  ; 
stmt  .  close  (  )  ; 
String   sql1  =  "select max(entry_date) from stock_verification_list where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
Statement   stmt1  =  con  .  createStatement  (  )  ; 
ResultSet   rs  =  stmt1  .  executeQuery  (  sql1  )  ; 
while  (  rs  .  next  (  )  )  { 
entrydate  =  rs  .  getString  (  1  )  ; 
} 
JOptionPane  .  showMessageDialog  (  null  ,  "There are "  +  count  +  " items in the list. The last entry was made on "  +  entrydate  )  ; 
rs  .  close  (  )  ; 
stmt1  .  close  (  )  ; 
con  .  close  (  )  ; 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 

private   void   jButton14ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog4  .  dispose  (  )  ; 
} 

private   void   jButton15ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog5  .  setSize  (  444  ,  130  )  ; 
jDialog5  .  setLocation  (  200  ,  50  )  ; 
jDialog5  .  setVisible  (  true  )  ; 
} 

private   void   jBrowseButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
JFileChooser   chooser  =  new   JFileChooser  (  new   File  (  System  .  getProperty  (  "user.home"  )  )  )  ; 
chooser  .  setFileFilter  (  new   FileFilter  (  )  { 

public   String   getDescription  (  )  { 
return  ".txt"  ; 
} 

public   boolean   accept  (  File   file  )  { 
boolean   status  =  false  ; 
try  { 
String   fileName  =  file  .  getName  (  )  .  toLowerCase  (  )  ; 
status  =  fileName  .  endsWith  (  ".txt"  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   status  ; 
} 
}  )  ; 
int   i  =  chooser  .  showSaveDialog  (  this  )  ; 
chooser  .  setFileSelectionMode  (  JFileChooser  .  FILES_ONLY  )  ; 
if  (  i  ==  JFileChooser  .  APPROVE_OPTION  )  { 
String   file  =  chooser  .  getSelectedFile  (  )  .  toString  (  )  ; 
StringTokenizer   str  =  new   StringTokenizer  (  file  ,  "."  )  ; 
if  (  str  .  countTokens  (  )  <=  2  )  { 
if  (  str  .  countTokens  (  )  ==  1  )  { 
createFile  =  new   File  (  chooser  .  getSelectedFile  (  )  .  toString  (  )  +  ".txt"  )  ; 
if  (  createFile  .  exists  (  )  )  { 
int   cnt  =  JOptionPane  .  showConfirmDialog  (  this  ,  "This file already exists ! Are you sure \n you want to over write it."  ,  "check"  ,  JOptionPane  .  OK_CANCEL_OPTION  )  ; 
if  (  cnt  ==  0  )  { 
jTextField6  .  setText  (  createFile  .  toString  (  )  )  ; 
System  .  out  .  println  (  "override"  )  ; 
}  else  { 
createFile  =  null  ; 
jTextField6  .  setText  (  ""  )  ; 
} 
}  else  { 
jTextField6  .  setText  (  createFile  .  toString  (  )  )  ; 
} 
}  else  { 
str  .  nextToken  (  )  ; 
String   s1  =  str  .  nextToken  (  "."  )  ; 
if  (  s1  .  equalsIgnoreCase  (  "txt"  )  )  { 
createFile  =  new   File  (  chooser  .  getSelectedFile  (  )  .  toString  (  )  )  ; 
if  (  createFile  .  exists  (  )  )  { 
int   cnt  =  JOptionPane  .  showConfirmDialog  (  this  ,  "This file already exists ! Are you sure \n you want to over write it."  ,  "check"  ,  JOptionPane  .  OK_CANCEL_OPTION  )  ; 
if  (  cnt  ==  0  )  { 
jTextField6  .  setText  (  createFile  .  toString  (  )  )  ; 
System  .  out  .  println  (  "override"  )  ; 
}  else  { 
createFile  =  null  ; 
jTextField6  .  setText  (  ""  )  ; 
} 
}  else  { 
jTextField6  .  setText  (  createFile  .  toString  (  )  )  ; 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  this  ,  "The given file is not in .txt format \n Please create .csv extension."  ,  "check"  ,  JOptionPane  .  CANCEL_OPTION  )  ; 
} 
} 
}  else  { 
JOptionPane  .  showMessageDialog  (  this  ,  "The given file name is not correct \n Please create a new file."  ,  "check"  ,  JOptionPane  .  YES_OPTION  )  ; 
} 
}  else  { 
jTextField6  .  setText  (  ""  )  ; 
} 
try  { 
if  (  createFile  !=  null  )  { 
OutputStreamWriter   osw  =  new   OutputStreamWriter  (  new   FileOutputStream  (  jTextField6  .  getText  (  )  )  )  ; 
osw  .  flush  (  )  ; 
osw  .  close  (  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select the file to export the data."  ,  "check"  ,  JOptionPane  .  YES_OPTION  )  ; 
} 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 

private   void   jExportButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ISO2709DataExport  .  SwingWorker   sw  =  new   ISO2709DataExport  .  SwingWorker  (  )  { 

public   Object   construct  (  )  { 
try  { 
FileOutputStream   pw  =  null  ; 
final   int   count  ; 
pw  =  new   FileOutputStream  (  new   File  (  jTextField6  .  getText  (  )  )  )  ; 
System  .  out  .  println  (  "Absolute path : "  +  jTextField6  .  getText  (  )  )  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "select count(*) from stock_verification_list where library_id = "  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
java  .  sql  .  Statement   stmt  =  con  .  createStatement  (  )  ; 
java  .  sql  .  ResultSet   rs  =  stmt  .  executeQuery  (  sql  )  ; 
int   count1  =  0  ; 
while  (  rs  .  next  (  )  )  { 
count1  =  rs  .  getInt  (  1  )  ; 
} 
count  =  count1  ; 
rs  .  close  (  )  ; 
stmt  .  execute  (  "begin work;"  )  ; 
stmt  .  execute  (  "BEGIN;"  )  ; 
stmt  .  execute  (  "DECLARE crsr CURSOR FOR SELECT barcode FROM stock_verification_list where library_id="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  ";"  )  ; 
int   looptimes  =  count  /  500  ; 
if  (  count  %  500  !=  0  )  { 
looptimes  ++  ; 
} 
for  (  int   i  =  0  ;  i  <  looptimes  ;  i  ++  )  { 
rs  =  stmt  .  executeQuery  (  "FETCH FORWARD 500 FROM crsr;"  )  ; 
while  (  rs  .  next  (  )  )  { 
CSVWriter  .  writeString  (  pw  ,  rs  .  getString  (  1  )  )  ; 
pw  .  write  (  '\n'  )  ; 
jLabel12  .  setText  (  "Exported "  +  rs  .  getString  (  1  )  +  " item"  )  ; 
} 
} 
jLabel12  .  setText  (  "Exported "  +  count  +  " itmes to the file"  )  ; 
stmt  .  execute  (  "close crsr;"  )  ; 
stmt  .  execute  (  "commit work;"  )  ; 
stmt  .  close  (  )  ; 
con  .  close  (  )  ; 
pw  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
return  "Hello"  ; 
} 
}  ; 
sw  .  start  (  )  ; 
} 

private   void   jButton16ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jDialog5  .  dispose  (  )  ; 
} 

private   void   jButton8ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jTextField2  .  setEditable  (  true  )  ; 
JFileChooser   chooser  =  new   JFileChooser  (  new   File  (  System  .  getProperty  (  "user.home"  )  )  )  ; 
chooser  .  setFileSelectionMode  (  javax  .  swing  .  JFileChooser  .  DIRECTORIES_ONLY  )  ; 
chooser  .  showOpenDialog  (  this  )  ; 
Calendar   currentDate  =  Calendar  .  getInstance  (  )  ; 
SimpleDateFormat   formatter  =  new   SimpleDateFormat  (  "dd-MMM-yyyy"  )  ; 
String   dateNow  =  formatter  .  format  (  currentDate  .  getTime  (  )  )  ; 
System  .  out  .  println  (  chooser  .  getSelectedFile  (  )  )  ; 
String   mkfolder  =  chooser  .  getSelectedFile  (  )  +  "/"  +  dateNow  ; 
boolean   success  =  (  new   File  (  mkfolder  )  )  .  mkdir  (  )  ; 
if  (  success  )  { 
System  .  out  .  println  (  "Directory: "  +  mkfolder  +  " created"  )  ; 
} 
jTextField2  .  setText  (  mkfolder  )  ; 
chooser  .  cancelSelection  (  )  ; 
} 

private   void   jButton9ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  !  jTextField2  .  getText  (  )  .  equals  (  ""  )  )  { 
GenerateStockReport   gsr  =  new   GenerateStockReport  (  new   File  (  jTextField2  .  getText  (  )  )  ,  this  )  ; 
gsr  .  start  (  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select the folder"  )  ; 
} 
} 

private   void   jButton17ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
dialogLegend  .  setVisible  (  true  )  ; 
} 

private   void   jButton18ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
dialogLegend  .  dispose  (  )  ; 
} 

private   javax  .  swing  .  ButtonGroup   buttonGroup1  ; 

private   javax  .  swing  .  JComboBox   cbShelvingLocation  ; 

private   javax  .  swing  .  JDialog   dialogLegend  ; 

private   javax  .  swing  .  JButton   jBrowseButton  ; 

private   javax  .  swing  .  JButton   jBrowseButton1  ; 

private   javax  .  swing  .  JButton   jButton1  ; 

private   javax  .  swing  .  JButton   jButton10  ; 

private   javax  .  swing  .  JButton   jButton11  ; 

private   javax  .  swing  .  JButton   jButton12  ; 

private   javax  .  swing  .  JButton   jButton13  ; 

private   javax  .  swing  .  JButton   jButton14  ; 

private   javax  .  swing  .  JButton   jButton15  ; 

private   javax  .  swing  .  JButton   jButton16  ; 

private   javax  .  swing  .  JButton   jButton17  ; 

private   javax  .  swing  .  JButton   jButton18  ; 

private   javax  .  swing  .  JButton   jButton2  ; 

private   javax  .  swing  .  JButton   jButton3  ; 

private   javax  .  swing  .  JButton   jButton4  ; 

private   javax  .  swing  .  JButton   jButton5  ; 

private   javax  .  swing  .  JButton   jButton6  ; 

private   javax  .  swing  .  JButton   jButton7  ; 

private   javax  .  swing  .  JButton   jButton8  ; 

private   javax  .  swing  .  JButton   jButton9  ; 

private   javax  .  swing  .  JDialog   jDialog1  ; 

private   javax  .  swing  .  JDialog   jDialog2  ; 

private   javax  .  swing  .  JDialog   jDialog3  ; 

private   javax  .  swing  .  JDialog   jDialog4  ; 

private   javax  .  swing  .  JDialog   jDialog5  ; 

private   javax  .  swing  .  JButton   jExportButton1  ; 

private   javax  .  swing  .  JButton   jGoButton1  ; 

private   javax  .  swing  .  JButton   jGoButton2  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel10  ; 

private   javax  .  swing  .  JLabel   jLabel11  ; 

private   javax  .  swing  .  JLabel   jLabel12  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabel5  ; 

private   javax  .  swing  .  JLabel   jLabel6  ; 

private   javax  .  swing  .  JLabel   jLabel7  ; 

private   javax  .  swing  .  JLabel   jLabel8  ; 

private   javax  .  swing  .  JLabel   jLabel9  ; 

private   javax  .  swing  .  JList   jList1  ; 

private   javax  .  swing  .  JButton   jLoadButton  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JPanel   jPanel10  ; 

private   javax  .  swing  .  JPanel   jPanel11  ; 

private   javax  .  swing  .  JPanel   jPanel12  ; 

private   javax  .  swing  .  JPanel   jPanel13  ; 

private   javax  .  swing  .  JPanel   jPanel14  ; 

private   javax  .  swing  .  JPanel   jPanel15  ; 

private   javax  .  swing  .  JPanel   jPanel16  ; 

private   javax  .  swing  .  JPanel   jPanel17  ; 

private   javax  .  swing  .  JPanel   jPanel18  ; 

private   javax  .  swing  .  JPanel   jPanel19  ; 

private   javax  .  swing  .  JPanel   jPanel2  ; 

private   javax  .  swing  .  JPanel   jPanel20  ; 

private   javax  .  swing  .  JPanel   jPanel21  ; 

private   javax  .  swing  .  JPanel   jPanel22  ; 

private   javax  .  swing  .  JPanel   jPanel23  ; 

private   javax  .  swing  .  JPanel   jPanel25  ; 

private   javax  .  swing  .  JPanel   jPanel3  ; 

private   javax  .  swing  .  JPanel   jPanel4  ; 

private   javax  .  swing  .  JPanel   jPanel5  ; 

private   javax  .  swing  .  JPanel   jPanel6  ; 

private   javax  .  swing  .  JPanel   jPanel7  ; 

private   javax  .  swing  .  JPanel   jPanel8  ; 

private   javax  .  swing  .  JPanel   jPanel9  ; 

private   javax  .  swing  .  JProgressBar   jProgressBar1  ; 

private   javax  .  swing  .  JRadioButton   jRadioButton1  ; 

private   javax  .  swing  .  JRadioButton   jRadioButton3  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane2  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane3  ; 

private   javax  .  swing  .  JTable   jTable1  ; 

private   javax  .  swing  .  JTextField   jTextField1  ; 

private   javax  .  swing  .  JTextField   jTextField2  ; 

private   javax  .  swing  .  JTextField   jTextField3  ; 

private   javax  .  swing  .  JTextField   jTextField4  ; 

private   javax  .  swing  .  JTextField   jTextField5  ; 

private   javax  .  swing  .  JTextField   jTextField6  ; 

private   javax  .  swing  .  JTextField   tfClassificationNumber  ; 

private   javax  .  swing  .  JTextPane   tpLegend  ; 
} 

class   LoadFile   extends   SwingWorker  { 

private   File   loadFile  ; 

private   StockVerification   hostClass  ; 

public   LoadFile  (  File   loadFile  ,  StockVerification   hostClass  )  { 
this  .  loadFile  =  loadFile  ; 
this  .  hostClass  =  hostClass  ; 
} 

public   Object   construct  (  )  { 
try  { 
File   file  =  loadFile  ; 
BufferedReader   br  =  null  ; 
FileReader   fr  =  null  ; 
hostClass  .  setButtonEnable  (  false  )  ; 
try  { 
String   thisLine  ; 
fr  =  new   FileReader  (  file  )  ; 
java  .  sql  .  Connection   con  =  reports  .  utility  .  database  .  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
String   sql  =  "insert into stock_verification_list (library_id, barcode, entry_id, entry_date) values (?,?,?,current_timestamp)"  ; 
java  .  sql  .  PreparedStatement   pstmt  =  con  .  prepareStatement  (  sql  )  ; 
try  { 
br  =  new   BufferedReader  (  new   FileReader  (  file  )  )  ; 
int   count  =  0  ; 
while  (  (  thisLine  =  br  .  readLine  (  )  )  !=  null  )  { 
pstmt  .  setInt  (  1  ,  Integer  .  parseInt  (  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  )  )  ; 
pstmt  .  setString  (  2  ,  thisLine  )  ; 
pstmt  .  setString  (  3  ,  StaticValues  .  getInstance  (  )  .  getLoginPatronId  (  )  )  ; 
try  { 
pstmt  .  executeUpdate  (  )  ; 
}  catch  (  Exception   exp  )  { 
} 
hostClass  .  setMessage  (  thisLine  +  " Item is being processed"  )  ; 
count  ++  ; 
} 
hostClass  .  setMessage  (  "Total "  +  count  +  " items are completed"  )  ; 
pstmt  .  close  (  )  ; 
con  .  close  (  )  ; 
hostClass  .  addToMessages  (  count  +  " Items loaded from "  +  file  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "Error: "  +  e  )  ; 
} 
}  catch  (  SQLException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
br  .  close  (  )  ; 
fr  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
hostClass  .  setButtonEnable  (  true  )  ; 
return  "Hello"  ; 
} 
} 

class   GenerateStockReport   extends   SwingWorker  { 

newgenlib  .  marccomponent  .  conversion  .  Converter   converter  =  new   newgenlib  .  marccomponent  .  conversion  .  Converter  (  )  ; 

private   File   targetDirectory  ; 

private   StockVerification   hostClass  ; 

public   GenerateStockReport  (  File   targetDirectory  ,  StockVerification   hostClass  )  { 
this  .  targetDirectory  =  targetDirectory  ; 
this  .  hostClass  =  hostClass  ; 
} 

public   Object   construct  (  )  { 
hostClass  .  setGenerateButtonEnable  (  false  )  ; 
Object  [  ]  hd  =  new   Object  [  11  ]  ; 
hd  [  0  ]  =  "Serial no"  ; 
hd  [  1  ]  =  "Accession no"  ; 
hd  [  2  ]  =  "Barcode"  ; 
hd  [  3  ]  =  "Title"  ; 
hd  [  4  ]  =  "Author"  ; 
hd  [  5  ]  =  "Publisher"  ; 
hd  [  6  ]  =  "Edition"  ; 
hd  [  7  ]  =  "ISBN"  ; 
hd  [  8  ]  =  "Classification no"  ; 
hd  [  9  ]  =  "Book no"  ; 
hd  [  10  ]  =  "Status"  ; 
FileOutputStream   pw1  =  null  ; 
try  { 
pw1  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Consolidated_Report.csv"  )  )  ; 
FileOutputStream   pw2  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Yet_To_Be_Verified.csv"  )  )  ; 
FileOutputStream   pw3  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Invalid_Items.csv"  )  )  ; 
FileOutputStream   pw4  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Checked_Out.csv"  )  )  ; 
FileOutputStream   pw5  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/At_Circulation_Desk.csv"  )  )  ; 
FileOutputStream   pw6  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Separated_For_Binding.csv"  )  )  ; 
FileOutputStream   pw7  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Declared_Lost_By_Users.csv"  )  )  ; 
FileOutputStream   pw8  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Deleted.csv"  )  )  ; 
FileOutputStream   pw9  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Found_Missing.csv"  )  )  ; 
FileOutputStream   pw10  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Sent_For_Binding.csv"  )  )  ; 
FileOutputStream   pw11  =  new   FileOutputStream  (  new   File  (  targetDirectory  +  "/Weeded_Out.csv"  )  )  ; 
CSVWriter  .  writeRow  (  pw1  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw2  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw4  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw5  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw6  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw7  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw8  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw9  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw10  ,  hd  )  ; 
CSVWriter  .  writeRow  (  pw11  ,  hd  )  ; 
String   classificatioNo  =  hostClass  .  getClassificationNumber  (  )  ; 
String   shelLoc  =  hostClass  .  getShelvingLocation  (  )  ; 
String   shellocId  =  ""  ; 
try  { 
Connection   con  =  ConnectionPoolFactory  .  getInstance  (  )  .  getConnectionPool  (  )  .  getConnection  (  )  ; 
Statement   stmtStock  =  con  .  createStatement  (  )  ; 
try  { 
stmtStock  .  executeQuery  (  "select count(*) from stock_verification_list"  )  ; 
}  catch  (  SQLException   sex  )  { 
try  { 
stmtStock  .  execute  (  "CREATE TABLE stock_verification_list(library_id integer NOT NULL,barcode character varying(1000) NOT NULL,entry_id character varying(500),entry_date timestamp without time zone,CONSTRAINT pk_stockverification_list PRIMARY KEY (library_id, barcode))"  )  ; 
}  catch  (  Exception   ex  )  { 
} 
}  finally  { 
stmtStock  .  close  (  )  ; 
} 
if  (  !  shelLoc  .  equals  (  "Any"  )  )  { 
Statement   stat  =  con  .  createStatement  (  )  ; 
ResultSet   rs  =  stat  .  executeQuery  (  "select location_id from location where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  " and location='"  +  shelLoc  +  "'"  )  ; 
while  (  rs  .  next  (  )  )  { 
shellocId  =  rs  .  getString  (  1  )  ; 
} 
rs  .  close  (  )  ; 
stat  .  close  (  )  ; 
} 
String   whereCondition  =  ""  ; 
if  (  classificatioNo  .  equals  (  ""  )  &&  !  shelLoc  .  equals  (  "Any"  )  )  { 
whereCondition  =  " location_id="  +  shellocId  ; 
System  .  out  .  println  (  "---------Shelving location is selected---------"  )  ; 
}  else   if  (  !  classificatioNo  .  equals  (  ""  )  &&  shelLoc  .  equals  (  "Any"  )  )  { 
whereCondition  =  " classification_number like '"  +  classificatioNo  +  "%'"  ; 
System  .  out  .  println  (  "---------Classification no. is selected---------"  )  ; 
}  else   if  (  !  classificatioNo  .  equals  (  ""  )  &&  !  shelLoc  .  equals  (  "Any"  )  )  { 
whereCondition  =  " classification_number like '"  +  classificatioNo  +  "%' and location_id="  +  shellocId  ; 
System  .  out  .  println  (  "---------Both are selected---------"  )  ; 
}  else  { 
whereCondition  =  " "  ; 
System  .  out  .  println  (  "---------Neither is selected---------"  )  ; 
} 
String   sqlCount  =  ""  ; 
String   sqlCursor  =  ""  ; 
if  (  !  classificatioNo  .  equals  (  ""  )  ||  !  shelLoc  .  equals  (  "Any"  )  )  { 
sqlCount  =  "select count(*) from document where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  " and "  +  whereCondition  ; 
sqlCursor  =  "DECLARE crsr CURSOR FOR select accession_number, library_id, volume_id, status, barcode, book_number, classification_number from document where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  " and "  +  whereCondition  +  ";"  ; 
}  else  { 
sqlCount  =  "select count(*) from document where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  ; 
sqlCursor  =  "DECLARE crsr CURSOR FOR select accession_number, library_id, volume_id, status, barcode, book_number, classification_number from document where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  ";"  ; 
} 
Statement   stat  =  con  .  createStatement  (  )  ; 
ResultSet   rs  =  stat  .  executeQuery  (  sqlCount  )  ; 
int   count  =  0  ; 
while  (  rs  .  next  (  )  )  { 
count  =  rs  .  getInt  (  1  )  ; 
} 
hostClass  .  setProgressBarcMaxCount  (  count  )  ; 
rs  .  close  (  )  ; 
int   count1  =  count  ; 
stat  .  execute  (  "begin work;"  )  ; 
stat  .  execute  (  "BEGIN;"  )  ; 
stat  .  execute  (  sqlCursor  )  ; 
int   looptimes  =  count1  /  500  ; 
if  (  count1  %  500  !=  0  )  { 
looptimes  ++  ; 
} 
String   vol_id  =  null  ; 
String   status  =  null  ; 
String   accessionno  =  null  ; 
String   barcode  =  null  ; 
String   bookno  =  null  ; 
String   classificationno  =  null  ; 
Hashtable   ht  =  null  ; 
int   cnt  =  1  ; 
System  .  out  .  println  (  "ocunt is: "  +  count  )  ; 
System  .  out  .  println  (  "loop is: "  +  looptimes  )  ; 
int   completeCount  =  0  ; 
for  (  int   i  =  0  ;  i  <  looptimes  ;  i  ++  )  { 
rs  =  stat  .  executeQuery  (  "FETCH FORWARD 500 FROM crsr;"  )  ; 
while  (  rs  .  next  (  )  )  { 
accessionno  =  rs  .  getString  (  1  )  ; 
vol_id  =  rs  .  getString  (  3  )  ; 
status  =  rs  .  getString  (  4  )  ; 
barcode  =  rs  .  getString  (  5  )  ; 
bookno  =  rs  .  getString  (  6  )  ; 
classificationno  =  rs  .  getString  (  7  )  ; 
Statement   stat2  =  con  .  createStatement  (  )  ; 
ResultSet   rs2  =  stat2  .  executeQuery  (  "select b.wholecataloguerecord from cat_volume a, searchable_cataloguerecord b where a.volume_id = "  +  vol_id  +  " and a.cataloguerecordid = b.cataloguerecordid and a.owner_library_id = b.owner_library_id"  )  ; 
while  (  rs2  .  next  (  )  )  { 
String   abc  =  rs2  .  getString  (  1  )  ; 
if  (  abc  !=  null  )  { 
try  { 
ht  =  converter  .  getDetails  (  abc  )  ; 
}  catch  (  Exception   exp  )  { 
} 
} 
} 
if  (  ht  ==  null  )  { 
ht  =  new   Hashtable  (  )  ; 
} 
rs2  .  close  (  )  ; 
stat2  .  close  (  )  ; 
int   cnt1  =  0  ; 
Object  [  ]  row  =  new   Object  [  11  ]  ; 
row  [  0  ]  =  ""  +  cnt  ++  ; 
row  [  1  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  accessionno  )  ; 
row  [  2  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  barcode  )  ; 
if  (  ht  !=  null  )  { 
row  [  3  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  ht  .  get  (  "TITLE"  )  )  ; 
row  [  4  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  ht  .  get  (  "AUTHOR"  )  )  ; 
row  [  5  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  ht  .  get  (  "PUBLISHER"  )  )  ; 
row  [  6  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  ht  .  get  (  "EDITION"  )  )  ; 
row  [  7  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  ht  .  get  (  "ISBN"  )  )  ; 
} 
row  [  8  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  classificationno  )  ; 
row  [  9  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  bookno  )  ; 
row  [  10  ]  =  StringProcessor  .  getInstance  (  )  .  verifyString  (  status  )  ; 
if  (  status  .  equals  (  "B"  )  )  { 
stat2  =  con  .  createStatement  (  )  ; 
rs2  =  stat2  .  executeQuery  (  "select count(*) from stock_verification_list where barcode = '"  +  accessionno  +  "' and library_Id="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  )  ; 
while  (  rs2  .  next  (  )  )  { 
cnt1  =  rs2  .  getInt  (  1  )  ; 
} 
rs2  .  close  (  )  ; 
stat2  .  close  (  )  ; 
if  (  cnt1  >  0  )  { 
row  [  10  ]  =  "Available"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
}  else  { 
row  [  10  ]  =  "Yet to be Verified"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw2  ,  row  )  ; 
} 
}  else   if  (  status  .  equals  (  "A"  )  )  { 
row  [  10  ]  =  "Checked Out"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw4  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "C"  )  )  { 
row  [  10  ]  =  "Separated for binding"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw6  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "D"  )  )  { 
row  [  10  ]  =  "Sent for binding"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw10  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "E"  )  )  { 
row  [  10  ]  =  "Declared lost"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw7  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "F"  )  )  { 
row  [  10  ]  =  "At circulation desk"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw5  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "G"  )  )  { 
row  [  10  ]  =  "Deleted"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw8  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "H"  )  )  { 
row  [  10  ]  =  "Found Missing"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw9  ,  row  )  ; 
}  else   if  (  status  .  equals  (  "I"  )  )  { 
row  [  10  ]  =  "Weeded out"  ; 
CSVWriter  .  writeRow  (  pw1  ,  row  )  ; 
CSVWriter  .  writeRow  (  pw11  ,  row  )  ; 
} 
completeCount  ++  ; 
hostClass  .  setProgressBarcCurrentCount  (  completeCount  )  ; 
hostClass  .  setProgressBarText  (  completeCount  +  " of "  +  count  )  ; 
} 
} 
stat  .  execute  (  "close crsr;"  )  ; 
stat  .  execute  (  "commit work;"  )  ; 
rs  .  close  (  )  ; 
stat  .  close  (  )  ; 
pw1  .  flush  (  )  ; 
pw2  .  flush  (  )  ; 
pw4  .  flush  (  )  ; 
pw5  .  flush  (  )  ; 
pw6  .  flush  (  )  ; 
pw7  .  flush  (  )  ; 
pw8  .  flush  (  )  ; 
pw9  .  flush  (  )  ; 
pw10  .  flush  (  )  ; 
pw11  .  flush  (  )  ; 
pw1  .  close  (  )  ; 
pw2  .  close  (  )  ; 
pw4  .  close  (  )  ; 
pw5  .  close  (  )  ; 
pw6  .  close  (  )  ; 
pw7  .  close  (  )  ; 
pw8  .  close  (  )  ; 
pw9  .  close  (  )  ; 
pw10  .  close  (  )  ; 
pw11  .  close  (  )  ; 
JOptionPane  .  showMessageDialog  (  null  ,  "Please check the Stock Verification Information in the "  +  targetDirectory  +  " directory. Now processing for Invalid Items"  )  ; 
stat  =  con  .  createStatement  (  )  ; 
rs  =  stat  .  executeQuery  (  "select count(*) from stock_verification_list where library_id="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  )  ; 
int   fic  =  0  ; 
while  (  rs  .  next  (  )  )  { 
fic  =  rs  .  getInt  (  1  )  ; 
} 
hostClass  .  setProgressBarcMaxCount  (  fic  )  ; 
System  .  out  .  println  (  "Count ###"  +  fic  )  ; 
rs  .  close  (  )  ; 
stat  .  close  (  )  ; 
stat  =  con  .  createStatement  (  )  ; 
int   fic1  =  fic  ; 
stat  .  execute  (  "begin work;"  )  ; 
stat  .  execute  (  "BEGIN;"  )  ; 
stat  .  execute  (  "DECLARE crsr1 CURSOR FOR select barcode from stock_verification_list where library_id ="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  +  ";"  )  ; 
int   looptimes1  =  fic1  /  500  ; 
if  (  fic1  %  500  !=  0  )  { 
looptimes1  ++  ; 
} 
System  .  out  .  println  (  "LoopCount ###"  +  looptimes1  )  ; 
int   completeCount1  =  0  ; 
Object  [  ]  headRow  =  new   Object  [  2  ]  ; 
headRow  [  0  ]  =  "Barcode/Accession number"  ; 
headRow  [  1  ]  =  "Status"  ; 
CSVWriter  .  writeRow  (  pw3  ,  headRow  )  ; 
Object  [  ]  row  =  new   Object  [  2  ]  ; 
for  (  int   i  =  0  ;  i  <  looptimes1  ;  i  ++  )  { 
rs  =  stat  .  executeQuery  (  "FETCH FORWARD 500 FROM crsr1;"  )  ; 
while  (  rs  .  next  (  )  )  { 
String   accno  =  rs  .  getString  (  1  )  ; 
Statement   stat2  =  con  .  createStatement  (  )  ; 
ResultSet   rs2  =  stat2  .  executeQuery  (  "select count(*) from document where accession_number='"  +  accno  +  "' and library_id="  +  StaticValues  .  getInstance  (  )  .  getLoginLibraryId  (  )  )  ; 
int   fic2  =  0  ; 
while  (  rs2  .  next  (  )  )  { 
fic2  =  rs2  .  getInt  (  1  )  ; 
} 
rs2  .  close  (  )  ; 
stat2  .  close  (  )  ; 
if  (  fic2  ==  0  )  { 
row  [  0  ]  =  accno  ; 
row  [  1  ]  =  "Invalid item entered"  ; 
CSVWriter  .  writeRow  (  pw3  ,  row  )  ; 
}  else  { 
} 
completeCount1  ++  ; 
hostClass  .  setProgressBarcCurrentCount  (  completeCount1  )  ; 
hostClass  .  setProgressBarText  (  "Processing database for invalid items"  )  ; 
} 
} 
stat  .  execute  (  "close crsr1;"  )  ; 
stat  .  execute  (  "commit work;"  )  ; 
rs  .  close  (  )  ; 
stat  .  close  (  )  ; 
pw3  .  flush  (  )  ; 
pw3  .  close  (  )  ; 
con  .  close  (  )  ; 
hostClass  .  setProgressBarText  (  "Done"  )  ; 
JOptionPane  .  showMessageDialog  (  hostClass  ,  "Stock verification completed"  ,  "Stock verification completed"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
}  catch  (  Exception   exp  )  { 
exp  .  printStackTrace  (  )  ; 
} 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
pw1  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 
hostClass  .  setGenerateButtonEnable  (  true  )  ; 
return  "Done"  ; 
} 
} 

