package   org  .  statcato  .  spreadsheet  ; 

import   org  .  statcato  .  file  .  FileChooserUtils  ; 
import   org  .  statcato  .  file  .  ExtensionFileFilter  ; 
import   org  .  statcato  .  utils  .  *  ; 
import   org  .  statcato  .  statistics  .  BasicStatistics  ; 
import   org  .  statcato  .  file  .  FileOperations  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  table  .  *  ; 
import   javax  .  swing  .  ListSelectionModel  ; 
import   javax  .  swing  .  event  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  awt  .  Component  ; 
import   javax  .  swing  .  text  .  *  ; 
import   javax  .  swing  .  undo  .  *  ; 
import   org  .  apache  .  poi  .  hssf  .  usermodel  .  *  ; 
import   org  .  statcato  .  Statcato  ; 










public   class   Spreadsheet   extends   JTable   implements   StateEditable  { 

private   boolean   dragged  =  false  ; 

private   int   startDraggedColumn  =  -  1  ; 

private   int   lastSelectedColumn  =  -  1  ; 

private   File   savedFile  =  null  ; 

private   boolean   changed  =  false  ; 

private   Statcato   app  ; 

protected   UndoableEditSupport   undoableEditSupport  ; 

protected   static   final   String   MODEL  =  "MODEL"  ; 

protected   static   final   String   STATUS  =  "STATUS"  ; 

private   StateEdit   edit  ; 






public   Spreadsheet  (  Statcato   app  )  { 
super  (  new   SpreadsheetModel  (  app  )  )  ; 
this  .  app  =  app  ; 
initialize  (  )  ; 
} 

@  Override 
public   String   getToolTipText  (  MouseEvent   e  )  { 
java  .  awt  .  Point   p  =  e  .  getPoint  (  )  ; 
int   rowIndex  =  rowAtPoint  (  p  )  ; 
int   colIndex  =  columnAtPoint  (  p  )  ; 
int   realColumnIndex  =  convertColumnIndexToModel  (  colIndex  )  ; 
int   realRowIndex  =  convertRowIndexToModel  (  rowIndex  )  ; 
String   contents  =  getValueAt  (  realRowIndex  ,  realColumnIndex  )  .  getContents  (  )  ; 
return  (  contents  .  equals  (  ""  )  ?  null  :  contents  )  ; 
} 









public   Spreadsheet  (  Statcato   app  ,  int   numRows  ,  int   numColumns  )  { 
super  (  new   SpreadsheetModel  (  app  ,  numRows  ,  numColumns  )  )  ; 
this  .  app  =  app  ; 
initialize  (  )  ; 
} 

public   void   addUndoableEditListener  (  UndoableEditListener   undoableEditListener  )  { 
undoableEditSupport  .  addUndoableEditListener  (  undoableEditListener  )  ; 
} 

public   void   removeUndoableEditListener  (  UndoableEditListener   undoableEditListener  )  { 
undoableEditSupport  .  removeUndoableEditListener  (  undoableEditListener  )  ; 
} 

@  Override 
@  SuppressWarnings  (  "unchecked"  ) 
public   void   storeState  (  Hashtable   state  )  { 
state  .  put  (  MODEL  ,  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getTabDelimitedValues  (  )  )  ; 
state  .  put  (  STATUS  ,  new   Boolean  (  changed  )  )  ; 
} 

@  Override 
public   void   restoreState  (  Hashtable   state  )  { 
if  (  state  !=  null  )  { 
String   model  =  (  String  )  state  .  get  (  MODEL  )  ; 
if  (  model  !=  null  )  { 
clearAllCells  (  )  ; 
setData  (  model  )  ; 
} 
Object   changedStatus  =  state  .  get  (  STATUS  )  ; 
if  (  changedStatus  !=  null  )  { 
changed  =  (  Boolean  )  changedStatus  ; 
if  (  changed  )  setChangedStatus  (  )  ;  else   setUnchangedStatus  (  )  ; 
} 
}  else   System  .  out  .  println  (  "unexpected error: restore state is null"  )  ; 
} 






private   void   initialize  (  )  { 
undoableEditSupport  =  new   UndoableEditSupport  (  )  ; 
setAutoResizeMode  (  JTable  .  AUTO_RESIZE_OFF  )  ; 
setDefaultColumnWidths  (  )  ; 
setSelectionMode  (  ListSelectionModel  .  SINGLE_INTERVAL_SELECTION  )  ; 
setCellSelectionEnabled  (  true  )  ; 
setFillsViewportHeight  (  true  )  ; 
JTableHeader   atableHeader  =  getTableHeader  (  )  ; 
atableHeader  .  setReorderingAllowed  (  false  )  ; 
addColumnHeaderMouseListeners  (  )  ; 
getModel  (  )  .  addTableModelListener  (  this  )  ; 
setDragEnabled  (  true  )  ; 
addKeyListener  (  new   KeyAdapter  (  )  { 

@  Override 
public   void   keyPressed  (  KeyEvent   e  )  { 
if  (  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_BACK_SPACE  )  { 
clearSelectedCells  (  )  ; 
}  else   if  (  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_DELETE  )  { 
deleteSelectedCells  (  )  ; 
}  else   if  (  !  isEditing  (  )  &&  !  e  .  isActionKey  (  )  &&  !  e  .  isControlDown  (  )  &&  !  e  .  isAltDown  (  )  &&  e  .  getKeyCode  (  )  !=  KeyEvent  .  VK_SHIFT  )  { 
int   rowIndexStart  =  getSelectedRow  (  )  ; 
int   colIndexStart  =  getSelectedColumn  (  )  ; 
if  (  rowIndexStart  ==  -  1  ||  colIndexStart  ==  -  1  )  return  ; 
editCellAt  (  rowIndexStart  ,  colIndexStart  )  ; 
Component   c  =  getEditorComponent  (  )  ; 
if  (  c   instanceof   JTextComponent  )  (  (  JTextComponent  )  c  )  .  setText  (  ""  )  ; 
}  else   if  (  e  .  isControlDown  (  )  &&  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_V  )  { 
setChangedStatus  (  )  ; 
} 
} 

@  Override 
public   void   keyTyped  (  KeyEvent   e  )  { 
if  (  !  e  .  isControlDown  (  )  )  setChangedStatus  (  )  ; 
} 
}  )  ; 
ExcelAdapter   ea  =  new   ExcelAdapter  (  this  )  ; 
} 




public   void   setChangedStatus  (  )  { 
if  (  !  changed  )  { 
changed  =  true  ; 
app  .  setCurrentTabTitle  (  app  .  getCurrentTabTitle  (  )  +  "*"  )  ; 
} 
} 





public   void   setUnchangedStatus  (  )  { 
changed  =  false  ; 
String   title  =  app  .  getCurrentTabTitle  (  )  ; 
if  (  title  .  endsWith  (  "*"  )  )  app  .  setCurrentTabTitle  (  app  .  getCurrentTabTitle  (  )  .  substring  (  0  ,  title  .  length  (  )  -  1  )  )  ; 
} 






public   boolean   getChangedStatus  (  )  { 
return   changed  ; 
} 




public   void   closeFile  (  )  { 
savedFile  =  null  ; 
} 




public   void   setDefaultColumnWidths  (  )  { 
TableColumn   column  =  null  ; 
for  (  int   i  =  0  ;  i  <  getColumnCount  (  )  ;  ++  i  )  { 
column  =  this  .  getColumnModel  (  )  .  getColumn  (  i  )  ; 
column  .  setPreferredWidth  (  70  )  ; 
} 
} 

@  Override 
public   TableCellRenderer   getCellRenderer  (  int   row  ,  int   column  )  { 
if  (  row  ==  0  )  { 
return   new   VarHeaderRenderer  (  )  ; 
}  else   return   new   SpreadsheetCellRenderer  (  )  ; 
} 




private   void   addColumnHeaderMouseListeners  (  )  { 
MouseMotionAdapter   motionListener  =  new   MouseMotionAdapter  (  )  { 

@  Override 
public   void   mouseDragged  (  MouseEvent   e  )  { 
if  (  !  dragged  )  return  ; 
JTableHeader   header  =  getTableHeader  (  )  ; 
TableColumnModel   columns  =  header  .  getColumnModel  (  )  ; 
int   column  =  header  .  columnAtPoint  (  e  .  getPoint  (  )  )  ; 
ListSelectionModel   selection  =  columns  .  getSelectionModel  (  )  ; 
if  (  column  ==  -  1  )  { 
selection  .  clearSelection  (  )  ; 
return  ; 
} 
int   count  =  getRowCount  (  )  ; 
if  (  count  !=  0  )  setRowSelectionInterval  (  0  ,  count  -  1  )  ; 
if  (  column  <  startDraggedColumn  )  { 
selection  .  setSelectionInterval  (  column  ,  startDraggedColumn  )  ; 
}  else  { 
selection  .  setSelectionInterval  (  startDraggedColumn  ,  column  )  ; 
} 
} 
}  ; 
MouseAdapter   mouseAdapter  =  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseReleased  (  MouseEvent   e  )  { 
dragged  =  false  ; 
startDraggedColumn  =  -  1  ; 
} 

@  Override 
public   void   mousePressed  (  MouseEvent   e  )  { 
JTableHeader   header  =  getTableHeader  (  )  ; 
TableColumnModel   columns  =  header  .  getColumnModel  (  )  ; 
int   column  =  header  .  columnAtPoint  (  e  .  getPoint  (  )  )  ; 
ListSelectionModel   selection  =  columns  .  getSelectionModel  (  )  ; 
if  (  dragged  )  { 
selection  .  clearSelection  (  )  ; 
dragged  =  false  ; 
startDraggedColumn  =  -  1  ; 
} 
if  (  column  ==  -  1  )  return  ; 
dragged  =  true  ; 
int   count  =  getRowCount  (  )  ; 
if  (  count  !=  0  )  setRowSelectionInterval  (  0  ,  count  -  1  )  ; 
selection  .  addSelectionInterval  (  column  ,  column  )  ; 
startDraggedColumn  =  column  ; 
} 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
JTableHeader   header  =  getTableHeader  (  )  ; 
TableColumnModel   columns  =  header  .  getColumnModel  (  )  ; 
int   column  =  header  .  columnAtPoint  (  e  .  getPoint  (  )  )  ; 
if  (  column  ==  -  1  )  return  ; 
int   count  =  getRowCount  (  )  ; 
if  (  count  !=  0  )  setRowSelectionInterval  (  0  ,  count  -  1  )  ; 
ListSelectionModel   selection  =  columns  .  getSelectionModel  (  )  ; 
int   anchor  =  selection  .  getAnchorSelectionIndex  (  )  ; 
int   lead  =  selection  .  getLeadSelectionIndex  (  )  ; 
if  (  e  .  isShiftDown  (  )  )  { 
if  (  lastSelectedColumn  !=  -  1  )  { 
if  (  lastSelectedColumn  <=  column  )  selection  .  setSelectionInterval  (  lastSelectedColumn  ,  column  )  ;  else   selection  .  setSelectionInterval  (  column  ,  lastSelectedColumn  )  ; 
} 
}  else   if  (  e  .  isControlDown  (  )  )  { 
}  else   selection  .  setSelectionInterval  (  column  ,  column  )  ; 
lastSelectedColumn  =  column  ; 
} 
}  ; 
JTableHeader   header  =  getTableHeader  (  )  ; 
header  .  addMouseListener  (  mouseAdapter  )  ; 
header  .  addMouseMotionListener  (  motionListener  )  ; 
} 








private   Vector  <  String  >  convertColumnNumbersToDescriptions  (  Vector  <  Integer  >  ColumnsNumbers  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
Vector  <  String  >  ColumnsDesc  =  new   Vector  <  String  >  (  )  ; 
for  (  Enumeration   e  =  ColumnsNumbers  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
int   col  =  (  (  Integer  )  e  .  nextElement  (  )  )  .  intValue  (  )  ; 
String   desc  =  DataSpreadsheetModel  .  getColumnName  (  col  )  ; 
String   variable  =  DataSpreadsheetModel  .  getVariableName  (  col  )  ; 
desc  +=  "  "  +  (  variable  .  length  (  )  >  20  ?  variable  .  substring  (  0  ,  16  )  +  "..."  :  variable  )  ; 
ColumnsDesc  .  addElement  (  desc  )  ; 
} 
return   ColumnsDesc  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
public   void   populateColumnsList  (  JList   List  )  { 
List  .  setSelectionMode  (  ListSelectionModel  .  MULTIPLE_INTERVAL_SELECTION  )  ; 
List  .  setLayoutOrientation  (  JList  .  VERTICAL_WRAP  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
Vector  <  Integer  >  ColumnsNumbers  =  (  Vector  <  Integer  >  )  DataSpreadsheetModel  .  getColumnsWithData  (  )  ; 
List  .  setListData  (  convertColumnNumbersToDescriptions  (  ColumnsNumbers  )  )  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   void   populateMutableColumnsList  (  JList   List  )  { 
DefaultListModel   listModel  =  (  DefaultListModel  )  List  .  getModel  (  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
Vector  <  Integer  >  ColumnsNumbers  =  (  Vector  <  Integer  >  )  DataSpreadsheetModel  .  getColumnsWithData  (  )  ; 
Vector  <  String  >  ColumnsLabels  =  convertColumnNumbersToDescriptions  (  ColumnsNumbers  )  ; 
for  (  Enumeration   e  =  ColumnsLabels  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  listModel  .  addElement  (  e  .  nextElement  (  )  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
public   void   populateAllColumnsList  (  JList   List  )  { 
List  .  setSelectionMode  (  ListSelectionModel  .  MULTIPLE_INTERVAL_SELECTION  )  ; 
List  .  setLayoutOrientation  (  JList  .  VERTICAL_WRAP  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
Vector  <  Integer  >  ColumnsNumbers  =  (  Vector  <  Integer  >  )  DataSpreadsheetModel  .  getAllColumnNumbers  (  )  ; 
List  .  setListData  (  convertColumnNumbersToDescriptions  (  ColumnsNumbers  )  )  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   void   populateComboBox  (  JComboBox   Combo  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
Vector  <  Integer  >  ColumnsNumbers  =  (  Vector  <  Integer  >  )  DataSpreadsheetModel  .  getColumnsWithData  (  )  ; 
Vector  <  String  >  ColumnsDesc  =  convertColumnNumbersToDescriptions  (  ColumnsNumbers  )  ; 
Combo  .  removeAllItems  (  )  ; 
Combo  .  addItem  (  ""  )  ; 
Combo  .  setSelectedItem  (  ""  )  ; 
for  (  Enumeration   e  =  ColumnsDesc  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  Combo  .  addItem  (  e  .  nextElement  (  )  )  ; 
} 








public   int   parseColumnNumber  (  String   str  )  { 
String  [  ]  items  =  str  .  split  (  " "  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
return   DataSpreadsheetModel  .  getColumnNumber  (  items  [  0  ]  )  ; 
} 







public   Vector  <  Cell  >  getColumn  (  int   col  )  { 
return  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getColumn  (  col  )  ; 
} 







public   Vector  <  Cell  >  getRow  (  int   row  )  { 
return  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getRow  (  row  )  ; 
} 







public   int   getColumnNumber  (  String   str  )  { 
return  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getColumnNumber  (  str  )  ; 
} 








public   String   getColumnFullLabel  (  int   column  )  { 
SpreadsheetModel   model  =  (  SpreadsheetModel  )  getModel  (  )  ; 
return   model  .  getColumnLabel  (  column  )  +  " "  +  model  .  getVariableName  (  column  )  ; 
} 









public   Vector  <  Integer  >  getColumnNumbers  (  String   str  )  { 
int   dash  =  str  .  indexOf  (  '-'  )  ; 
Vector  <  Integer  >  columns  =  new   Vector  <  Integer  >  (  )  ; 
if  (  dash  ==  -  1  )  { 
int   col  =  getColumnNumber  (  str  )  ; 
if  (  col  !=  -  1  )  columns  .  addElement  (  new   Integer  (  col  )  )  ; 
}  else  { 
int   min  =  getColumnNumber  (  str  .  substring  (  0  ,  dash  )  )  ; 
int   max  =  getColumnNumber  (  str  .  substring  (  dash  +  1  )  )  ; 
if  (  min  !=  -  1  &&  max  !=  -  1  )  { 
if  (  min  >  max  )  { 
int   temp  =  max  ; 
max  =  min  ; 
min  =  temp  ; 
} 
for  (  int   i  =  min  ;  i  <=  max  ;  ++  i  )  { 
columns  .  addElement  (  new   Integer  (  i  )  )  ; 
} 
} 
} 
return   columns  ; 
} 










public   Vector  <  Integer  >  getColumnNumbersFromString  (  String   str  )  { 
Object  [  ]  columns  =  HelperFunctions  .  parseString  (  str  )  .  toArray  (  )  ; 
Vector  <  Integer  >  nums  =  new   Vector  <  Integer  >  (  )  ; 
for  (  int   x  =  0  ;  x  <  columns  .  length  ;  x  ++  )  { 
String   label  =  (  String  )  columns  [  x  ]  ; 
if  (  label  .  equals  (  ""  )  )  { 
continue  ; 
}  else  { 
Vector  <  Integer  >  ColumnNums  =  getColumnNumbers  (  label  )  ; 
if  (  ColumnNums  .  size  (  )  ==  0  )  { 
return   null  ; 
}  else  { 
for  (  int   i  =  0  ;  i  <  ColumnNums  .  size  (  )  ;  ++  i  )  { 
nums  .  addElement  (  ColumnNums  .  elementAt  (  i  )  )  ; 
} 
} 
} 
} 
return   nums  ; 
} 

public   int  [  ]  getColumnNumbersArrayFromString  (  String   str  )  { 
Vector  <  Integer  >  v  =  getColumnNumbersFromString  (  str  )  ; 
return   HelperFunctions  .  ConvertIntegerVectorToArray  (  v  )  ; 
} 







public   int   getRowNumber  (  String   str  )  { 
return  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getRowNumber  (  str  )  ; 
} 








@  Override 
public   Cell   getValueAt  (  int   row  ,  int   col  )  { 
return  (  Cell  )  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getValueAt  (  row  ,  col  )  ; 
} 






public   int   getLastRowNumber  (  )  { 
int   lastRow  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
for  (  int   r  =  1  ;  r  <  DataSpreadsheetModel  .  getRowCount  (  )  ;  ++  r  )  { 
boolean   hasData  =  false  ; 
for  (  int   c  =  0  ;  c  <  DataSpreadsheetModel  .  getColumnCount  (  )  ;  ++  c  )  { 
Cell   data  =  (  Cell  )  DataSpreadsheetModel  .  getValueAt  (  r  ,  c  )  ; 
if  (  SpreadsheetModel  .  hasData  (  data  )  )  hasData  =  true  ; 
} 
if  (  hasData  )  { 
lastRow  =  r  ; 
} 
} 
return   lastRow  ; 
} 








public   void   setColumn  (  int   column  ,  Vector  <  String  >  data  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "set column "  +  DataSpreadsheetModel  .  getColumnLabel  (  column  )  )  ; 
if  (  column  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  column  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
if  (  data  .  size  (  )  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  data  .  size  (  )  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
DataSpreadsheetModel  .  setColumn  (  column  ,  data  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 








public   void   setCellColumn  (  int   column  ,  Vector  <  Cell  >  data  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "set column "  +  DataSpreadsheetModel  .  getColumnLabel  (  column  )  )  ; 
if  (  column  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  column  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
if  (  data  .  size  (  )  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  data  .  size  (  )  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
DataSpreadsheetModel  .  setCellColumn  (  column  ,  data  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 







public   void   setRow  (  int   row  ,  Vector  <  String  >  data  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "set row "  +  row  )  ; 
if  (  row  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  row  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
if  (  data  .  size  (  )  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  data  .  size  (  )  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
DataSpreadsheetModel  .  setRow  (  row  ,  data  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 







public   void   setCellRow  (  int   row  ,  Vector  <  Cell  >  data  )  { 
setRow  (  row  ,  HelperFunctions  .  ConvertCellVectorToStringVector  (  data  )  )  ; 
} 






public   void   setVariablesRow  (  Vector  <  String  >  data  )  { 
setRow  (  0  ,  data  )  ; 
} 








public   void   setVariableLabel  (  int   column  ,  String   name  )  { 
if  (  !  getValueAt  (  0  ,  column  )  .  hasData  (  )  )  setValueAt  (  name  ,  0  ,  column  )  ; 
} 








public   int  [  ]  convertColumnLabelsToNumbers  (  Object  [  ]  Labels  )  { 
int  [  ]  columnNumbers  =  new   int  [  Labels  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  Labels  .  length  ;  ++  i  )  { 
String   value  =  Labels  [  i  ]  .  toString  (  )  ; 
columnNumbers  [  i  ]  =  parseColumnNumber  (  value  )  ; 
} 
return   columnNumbers  ; 
} 







public   Vector  <  Vector  <  Cell  >  >  getRowsWithDataAtGivenColumns  (  int  [  ]  columns  )  { 
Vector  <  Vector  <  Cell  >  >  Rows  =  new   Vector  <  Vector  <  Cell  >  >  (  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
int   numEmptyRowsSinceLastNonEmpty  =  0  ; 
for  (  int   r  =  1  ;  r  <  DataSpreadsheetModel  .  getRowCount  (  )  ;  ++  r  )  { 
boolean   hasData  =  false  ; 
Vector  <  Cell  >  Row  =  new   Vector  <  Cell  >  (  0  )  ; 
for  (  int   c  =  0  ;  c  <  columns  .  length  ;  ++  c  )  { 
Cell   data  =  (  Cell  )  DataSpreadsheetModel  .  getValueAt  (  r  ,  columns  [  c  ]  )  ; 
if  (  SpreadsheetModel  .  hasData  (  data  )  )  hasData  =  true  ; 
Row  .  addElement  (  data  )  ; 
} 
if  (  hasData  )  { 
for  (  int   i  =  0  ;  i  <  numEmptyRowsSinceLastNonEmpty  ;  ++  i  )  { 
Rows  .  addElement  (  new   Vector  <  Cell  >  (  0  )  )  ; 
} 
Rows  .  addElement  (  Row  )  ; 
numEmptyRowsSinceLastNonEmpty  =  0  ; 
}  else   numEmptyRowsSinceLastNonEmpty  ++  ; 
} 
return   Rows  ; 
} 






public   int   getLastNonEmptyRow  (  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
return   DataSpreadsheetModel  .  getLastNonEmptyRow  (  )  ; 
} 






public   int   getLastNonEmptyColumn  (  int   row  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
return   DataSpreadsheetModel  .  getLastNonEmptyColumn  (  row  )  ; 
} 






public   int   getLastNonEmptyColumn  (  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
return   DataSpreadsheetModel  .  getLastNonEmptyColumn  (  )  ; 
} 







public   void   setData  (  String   data  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
String  [  ]  lines  =  data  .  split  (  "\\n"  )  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  ++  i  )  { 
String  [  ]  items  =  lines  [  i  ]  .  split  (  "\\t"  )  ; 
for  (  int   j  =  0  ;  j  <  items  .  length  ;  ++  j  )  { 
setStringValueAt  (  items  [  j  ]  ,  i  ,  j  )  ; 
} 
} 
setChangedStatus  (  )  ; 
} 

@  Override 
public   void   setValueAt  (  Object   value  ,  int   row  ,  int   col  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
if  (  value  .  getClass  (  )  !=  String  .  class  ||  !  (  (  String  )  value  )  .  equals  (  ""  )  )  edit  =  new   StateEdit  (  this  ,  "set cell R"  +  row  +  ", "  +  DataSpreadsheetModel  .  getColumnLabel  (  col  )  )  ; 
if  (  row  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  row  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
if  (  col  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  col  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
DataSpreadsheetModel  .  setValueAt  (  value  ,  row  ,  col  )  ; 
if  (  value  .  getClass  (  )  !=  String  .  class  ||  !  (  (  String  )  value  )  .  equals  (  ""  )  )  { 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 
} 








public   void   setStringValueAt  (  String   value  ,  int   row  ,  int   col  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
if  (  row  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  row  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
if  (  col  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  col  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
DataSpreadsheetModel  .  setStringValueAt  (  value  ,  row  ,  col  )  ; 
} 







public   void   setDataUnchangedStatus  (  String   data  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
String  [  ]  lines  =  data  .  split  (  "\\n"  )  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  ++  i  )  { 
String  [  ]  items  =  lines  [  i  ]  .  split  (  "\\t"  )  ; 
for  (  int   j  =  0  ;  j  <  items  .  length  ;  ++  j  )  { 
setStringValueAt  (  items  [  j  ]  ,  i  ,  j  )  ; 
} 
} 
app  .  undoManager  .  discardAllEdits  (  )  ; 
} 







public   void   setData  (  Vector  <  Vector  <  String  >  >  data  )  { 
int   row  =  data  .  size  (  )  ; 
int   col  =  data  .  elementAt  (  0  )  .  size  (  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
if  (  row  >=  getRowCount  (  )  )  { 
for  (  int   r  =  getRowCount  (  )  ;  r  <=  row  ;  ++  r  )  { 
DataSpreadsheetModel  .  insertRow  (  r  )  ; 
updateRowHeader  (  )  ; 
} 
} 
if  (  col  >=  getColumnCount  (  )  )  { 
for  (  int   c  =  getColumnCount  (  )  ;  c  <=  col  ;  ++  c  )  { 
DataSpreadsheetModel  .  insertColumn  (  c  )  ; 
} 
} 
(  (  SpreadsheetModel  )  getModel  (  )  )  .  setData  (  data  )  ; 
setChangedStatus  (  )  ; 
app  .  undoManager  .  discardAllEdits  (  )  ; 
} 











public   Spreadsheet   getSubTable  (  int   minRow  ,  int   minCol  ,  int   maxRow  ,  int   maxCol  )  { 
Spreadsheet   NewSS  =  new   Spreadsheet  (  app  ,  maxRow  -  minRow  +  2  ,  maxCol  -  minCol  +  1  )  ; 
SpreadsheetModel   DataSSModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
SpreadsheetModel   NewDataSSModel  =  (  SpreadsheetModel  )  NewSS  .  getModel  (  )  ; 
Object   value  ; 
int   row  ,  col  ; 
for  (  int   i  =  minRow  ;  i  <=  maxRow  ;  ++  i  )  { 
for  (  int   j  =  minCol  ;  j  <=  maxCol  ;  ++  j  )  { 
value  =  DataSSModel  .  getValueAt  (  i  ,  j  )  ; 
row  =  i  -  minRow  +  1  ; 
col  =  j  -  minCol  ; 
NewDataSSModel  .  setValueAt  (  value  ,  row  ,  col  )  ; 
} 
} 
return   NewSS  ; 
} 




public   void   display  (  )  { 
(  (  SpreadsheetModel  )  getModel  (  )  )  .  printDebugData  (  )  ; 
} 












public   File   writeToFile  (  JFrame   frame  ,  boolean   saveAs  )  { 
String   path  =  ""  ; 
String   extension  =  ""  ; 
if  (  savedFile  !=  null  &&  !  saveAs  )  { 
path  =  savedFile  .  getPath  (  )  ; 
extension  =  FileChooserUtils  .  getExtension  (  savedFile  )  ; 
writeFileHelper  (  frame  ,  path  ,  extension  )  ; 
return   savedFile  ; 
}  else  { 
JFileChooser   fc  =  new   JFileChooser  (  FileOperations  .  getRecentDatasheet  (  )  ==  null  ?  null  :  FileOperations  .  getRecentDatasheet  (  )  .  getParentFile  (  )  )  ; 
ExtensionFileFilter   ExcelFilter  =  new   ExtensionFileFilter  (  "Excel (*.xls)"  ,  "xls"  )  ; 
ExtensionFileFilter   CSVFilter  =  new   ExtensionFileFilter  (  "Comma-separated values(*.csv)"  ,  "csv"  )  ; 
ExtensionFileFilter   TDVFilter  =  new   ExtensionFileFilter  (  "Tab-delimited values (*.txt)"  ,  "txt"  )  ; 
fc  .  addChoosableFileFilter  (  ExcelFilter  )  ; 
fc  .  addChoosableFileFilter  (  CSVFilter  )  ; 
fc  .  addChoosableFileFilter  (  TDVFilter  )  ; 
fc  .  setAcceptAllFileFilterUsed  (  false  )  ; 
int   returnValue  =  fc  .  showSaveDialog  (  frame  )  ; 
if  (  returnValue  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
path  =  file  .  getPath  (  )  ; 
extension  =  ""  ; 
javax  .  swing  .  filechooser  .  FileFilter   filter  =  fc  .  getFileFilter  (  )  ; 
if  (  filter  .  equals  (  ExcelFilter  )  )  { 
extension  =  "xls"  ; 
}  else   if  (  filter  .  equals  (  CSVFilter  )  )  { 
extension  =  "csv"  ; 
}  else  { 
extension  =  "txt"  ; 
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
writeFileHelper  (  frame  ,  path  ,  extension  )  ; 
savedFile  =  file  ; 
return   file  ; 
} 
return   null  ; 
} 
} 









private   void   writeFileHelper  (  JFrame   frame  ,  String   path  ,  String   extension  )  { 
try  { 
BufferedWriter   Writer  =  new   BufferedWriter  (  new   FileWriter  (  path  )  )  ; 
String   contents  =  ""  ; 
if  (  extension  !=  null  &&  extension  .  equals  (  "xls"  )  )  { 
System  .  out  .  println  (  "write excel"  )  ; 
HSSFWorkbook   wb  =  new   HSSFWorkbook  (  )  ; 
HSSFSheet   sheet  =  wb  .  createSheet  (  "new sheet"  )  ; 
HSSFRow   row  ; 
SpreadsheetModel   sm  =  (  SpreadsheetModel  )  getModel  (  )  ; 
int   lastNonEmptyRow  =  getLastNonEmptyRow  (  )  ; 
int   lastNonEmptyCol  ; 
for  (  int   i  =  0  ;  i  <=  lastNonEmptyRow  ;  i  ++  )  { 
row  =  sheet  .  createRow  (  (  short  )  i  )  ; 
lastNonEmptyCol  =  getLastNonEmptyColumn  (  i  )  ; 
for  (  int   j  =  0  ;  j  <=  lastNonEmptyCol  ;  j  ++  )  { 
Cell   cell  =  (  Cell  )  sm  .  getValueAt  (  i  ,  j  )  ; 
if  (  cell  .  isNumeric  (  )  )  row  .  createCell  (  (  short  )  j  )  .  setCellValue  (  cell  .  getNumValue  (  )  .  doubleValue  (  )  )  ;  else   row  .  createCell  (  (  short  )  j  )  .  setCellValue  (  cell  .  getContents  (  )  )  ; 
} 
} 
FileOutputStream   fileOut  =  new   FileOutputStream  (  path  )  ; 
wb  .  write  (  fileOut  )  ; 
fileOut  .  close  (  )  ; 
setUnchangedStatus  (  )  ; 
return  ; 
} 
if  (  extension  !=  null  &&  extension  .  equals  (  "csv"  )  )  { 
System  .  out  .  println  (  "write csv"  )  ; 
contents  =  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getCommaSeparatedValues  (  )  ; 
}  else  { 
System  .  out  .  println  (  "write txt"  )  ; 
contents  =  (  (  SpreadsheetModel  )  getModel  (  )  )  .  getTabDelimitedValues  (  )  ; 
} 
String  [  ]  lines  =  contents  .  split  (  "\n"  )  ; 
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






public   void   setFile  (  File   file  )  { 
savedFile  =  file  ; 
} 




public   void   clearSelectedCells  (  )  { 
int  [  ]  selectedColumns  =  getSelectedColumns  (  )  ; 
int  [  ]  selectedRows  =  getSelectedRows  (  )  ; 
int   minRow  =  BasicStatistics  .  min  (  selectedRows  )  ; 
int   minCol  =  BasicStatistics  .  min  (  selectedColumns  )  ; 
int   maxRow  =  BasicStatistics  .  max  (  selectedRows  )  ; 
int   maxCol  =  BasicStatistics  .  max  (  selectedColumns  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "clear cells"  )  ; 
for  (  int   row  =  minRow  ;  row  <=  maxRow  ;  ++  row  )  { 
for  (  int   col  =  minCol  ;  col  <=  maxCol  ;  ++  col  )  { 
DataSpreadsheetModel  .  clearCell  (  row  ,  col  )  ; 
} 
} 
setRowSelectionInterval  (  minRow  ,  minRow  )  ; 
setColumnSelectionInterval  (  minCol  ,  minCol  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   clearAllCells  (  )  { 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
for  (  int   i  =  0  ;  i  <  getRowCount  (  )  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  getColumnCount  (  )  ;  j  ++  )  { 
DataSpreadsheetModel  .  clearCell  (  i  ,  j  )  ; 
} 
} 
setChangedStatus  (  )  ; 
} 




public   void   deleteSelectedCells  (  )  { 
int  [  ]  selectedColumns  =  getSelectedColumns  (  )  ; 
int  [  ]  selectedRows  =  getSelectedRows  (  )  ; 
int   minRow  =  BasicStatistics  .  min  (  selectedRows  )  ; 
int   minCol  =  BasicStatistics  .  min  (  selectedColumns  )  ; 
int   maxRow  =  BasicStatistics  .  max  (  selectedRows  )  ; 
int   maxCol  =  BasicStatistics  .  max  (  selectedColumns  )  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "delete cells"  )  ; 
if  (  (  maxRow  -  minRow  )  ==  (  DataSpreadsheetModel  .  getRowCount  (  )  -  1  )  )  { 
for  (  int   col  =  minCol  ;  col  <=  maxCol  ;  ++  col  )  { 
DataSpreadsheetModel  .  deleteColumn  (  minCol  )  ; 
} 
}  else   if  (  (  maxCol  -  minCol  )  ==  (  DataSpreadsheetModel  .  getColumnCount  (  )  -  1  )  )  { 
if  (  minRow  ==  0  )  { 
DataSpreadsheetModel  .  deleteRow  (  0  )  ; 
minRow  =  1  ; 
} 
for  (  int   row  =  minRow  ;  row  <=  maxRow  ;  ++  row  )  { 
DataSpreadsheetModel  .  deleteRow  (  minRow  )  ; 
} 
}  else  { 
DataSpreadsheetModel  .  deleteCells  (  minRow  ,  maxRow  ,  minCol  ,  maxCol  )  ; 
} 
setRowSelectionInterval  (  minRow  ,  minRow  )  ; 
setColumnSelectionInterval  (  minCol  ,  minCol  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertRow  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
DataSpreadsheetModel  .  insertRow  (  getRowCount  (  )  )  ; 
updateRowHeader  (  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
} 




public   void   insertRowAbove  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert row above"  )  ; 
DataSpreadsheetModel  .  insertRow  (  row  )  ; 
updateRowHeader  (  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertRowBelow  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert row below"  )  ; 
DataSpreadsheetModel  .  insertRow  (  row  +  1  )  ; 
updateRowHeader  (  )  ; 
setRowSelectionInterval  (  row  +  1  ,  row  +  1  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertColumn  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
DataSpreadsheetModel  .  insertColumn  (  getColumnCount  (  )  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
} 




public   void   insertColumnLeft  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert column left"  )  ; 
DataSpreadsheetModel  .  insertColumn  (  col  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertColumnRight  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert column right"  )  ; 
DataSpreadsheetModel  .  insertColumn  (  col  +  1  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  +  1  ,  col  +  1  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertCellAbove  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert cell above"  )  ; 
DataSpreadsheetModel  .  insertCell  (  row  ,  col  )  ; 
updateRowHeader  (  )  ; 
setRowSelectionInterval  (  row  ,  row  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




public   void   insertCellBelow  (  )  { 
int   row  =  getSelectedRow  (  )  ; 
int   col  =  getSelectedColumn  (  )  ; 
if  (  row  ==  -  1  )  row  =  1  ; 
if  (  col  ==  -  1  )  col  =  0  ; 
SpreadsheetModel   DataSpreadsheetModel  =  (  SpreadsheetModel  )  getModel  (  )  ; 
edit  =  new   StateEdit  (  this  ,  "insert cell below"  )  ; 
DataSpreadsheetModel  .  insertCell  (  row  +  1  ,  col  )  ; 
updateRowHeader  (  )  ; 
setRowSelectionInterval  (  row  +  1  ,  row  +  1  )  ; 
setColumnSelectionInterval  (  col  ,  col  )  ; 
setChangedStatus  (  )  ; 
edit  .  end  (  )  ; 
undoableEditSupport  .  postEdit  (  edit  )  ; 
} 




private   void   updateRowHeader  (  )  { 
RowHeaderTable   rowHeader  =  (  RowHeaderTable  )  (  (  SpreadsheetScrollPane  )  app  .  getDatasheetTabbedPane  (  )  .  getSelectedComponent  (  )  )  .  getRowHeaderTable  (  )  ; 
rowHeader  .  addHeaderRow  (  )  ; 
} 




public   void   selectAllCells  (  )  { 
selectAll  (  )  ; 
} 
} 

