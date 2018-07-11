package   org  .  tigr  .  seq  .  util  ; 

import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Component  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  *  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  border  .  *  ; 
import   javax  .  swing  .  event  .  *  ; 
import   javax  .  swing  .  table  .  *  ; 
import   java  .  util  .  *  ; 
import   org  .  tigr  .  seq  .  log  .  *  ; 

public   class   TableSorter   extends   TableMap  { 




private   static   final   long   serialVersionUID  =  -  4319913935109457316L  ; 




private   int   sortingColumn  =  -  1  ; 




private   int  [  ]  indexes  ; 




private   boolean   sorted  ; 




Vector  <  Integer  >  sortingColumns  =  new   Vector  <  Integer  >  (  )  ; 




boolean   ascending  =  true  ; 




int   compares  ; 




public   TableSorter  (  )  { 
indexes  =  new   int  [  0  ]  ; 
} 






public   TableSorter  (  TableModel   model  )  { 
super  .  setModel  (  model  )  ; 
this  .  reallocateIndexes  (  )  ; 
} 








public   int   compareRowsByColumn  (  int   row1  ,  int   row2  ,  int   column  )  { 
Class   type  =  model  .  getColumnClass  (  column  )  ; 
TableModel   data  =  model  ; 
Object   o1  =  data  .  getValueAt  (  row1  ,  column  )  ; 
Object   o2  =  data  .  getValueAt  (  row2  ,  column  )  ; 
if  (  o1  ==  null  &&  o2  ==  null  )  { 
return   0  ; 
}  else   if  (  o1  ==  null  )  { 
return  -  1  ; 
}  else   if  (  o2  ==  null  )  { 
return   1  ; 
} 
if  (  type  .  getSuperclass  (  )  ==  java  .  lang  .  Number  .  class  )  { 
Number   n1  =  (  Number  )  data  .  getValueAt  (  row1  ,  column  )  ; 
double   d1  =  n1  .  doubleValue  (  )  ; 
Number   n2  =  (  Number  )  data  .  getValueAt  (  row2  ,  column  )  ; 
double   d2  =  n2  .  doubleValue  (  )  ; 
if  (  d1  <  d2  )  { 
return  -  1  ; 
}  else   if  (  d1  >  d2  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else   if  (  type  ==  java  .  util  .  Date  .  class  )  { 
Date   d1  =  (  Date  )  data  .  getValueAt  (  row1  ,  column  )  ; 
long   n1  =  d1  .  getTime  (  )  ; 
Date   d2  =  (  Date  )  data  .  getValueAt  (  row2  ,  column  )  ; 
long   n2  =  d2  .  getTime  (  )  ; 
if  (  n1  <  n2  )  { 
return  -  1  ; 
}  else   if  (  n1  >  n2  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else   if  (  type  ==  String  .  class  )  { 
String   s1  =  (  String  )  data  .  getValueAt  (  row1  ,  column  )  ; 
String   s2  =  (  String  )  data  .  getValueAt  (  row2  ,  column  )  ; 
int   result  =  s1  .  compareTo  (  s2  )  ; 
if  (  result  <  0  )  { 
return  -  1  ; 
}  else   if  (  result  >  0  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else   if  (  type  ==  Boolean  .  class  )  { 
Boolean   bool1  =  (  Boolean  )  data  .  getValueAt  (  row1  ,  column  )  ; 
boolean   b1  =  bool1  .  booleanValue  (  )  ; 
Boolean   bool2  =  (  Boolean  )  data  .  getValueAt  (  row2  ,  column  )  ; 
boolean   b2  =  bool2  .  booleanValue  (  )  ; 
if  (  b1  ==  b2  )  { 
return   0  ; 
}  else   if  (  b1  )  { 
return   1  ; 
}  else  { 
return  -  1  ; 
} 
}  else  { 
Object   v1  =  data  .  getValueAt  (  row1  ,  column  )  ; 
String   s1  =  v1  .  toString  (  )  ; 
Object   v2  =  data  .  getValueAt  (  row2  ,  column  )  ; 
String   s2  =  v2  .  toString  (  )  ; 
int   result  =  s1  .  compareTo  (  s2  )  ; 
if  (  result  <  0  )  { 
return  -  1  ; 
}  else   if  (  result  >  0  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
} 
} 







public   int   compare  (  int   row1  ,  int   row2  )  { 
compares  ++  ; 
for  (  int   level  =  0  ;  level  <  sortingColumns  .  size  (  )  ;  level  ++  )  { 
Integer   column  =  sortingColumns  .  elementAt  (  level  )  ; 
int   result  =  compareRowsByColumn  (  row1  ,  row2  ,  column  .  intValue  (  )  )  ; 
if  (  result  !=  0  )  { 
return   ascending  ?  result  :  -  result  ; 
} 
} 
return   0  ; 
} 





public   void   reallocateIndexes  (  )  { 
int   rowCount  =  model  .  getRowCount  (  )  ; 
indexes  =  new   int  [  rowCount  ]  ; 
for  (  int   row  =  0  ;  row  <  rowCount  ;  row  ++  )  { 
indexes  [  row  ]  =  row  ; 
} 
} 






public   void   tableChanged  (  TableModelEvent   e  )  { 
reallocateIndexes  (  )  ; 
super  .  tableChanged  (  e  )  ; 
} 






public   void   sort  (  )  { 
compares  =  0  ; 
shuttlesort  (  (  int  [  ]  )  indexes  .  clone  (  )  ,  indexes  ,  0  ,  indexes  .  length  )  ; 
} 
















public   void   shuttlesort  (  int   from  [  ]  ,  int   to  [  ]  ,  int   low  ,  int   high  )  { 
if  (  high  -  low  <  2  )  { 
return  ; 
} 
int   middle  =  (  low  +  high  )  /  2  ; 
shuttlesort  (  to  ,  from  ,  low  ,  middle  )  ; 
shuttlesort  (  to  ,  from  ,  middle  ,  high  )  ; 
int   p  =  low  ; 
int   q  =  middle  ; 
if  (  high  -  low  >=  4  &&  compare  (  from  [  middle  -  1  ]  ,  from  [  middle  ]  )  <=  0  )  { 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  { 
to  [  i  ]  =  from  [  i  ]  ; 
} 
return  ; 
} 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  { 
if  (  q  >=  high  ||  (  p  <  middle  &&  compare  (  from  [  p  ]  ,  from  [  q  ]  )  <=  0  )  )  { 
to  [  i  ]  =  from  [  p  ++  ]  ; 
}  else  { 
to  [  i  ]  =  from  [  q  ++  ]  ; 
} 
} 
} 











public   Object   getValueAt  (  int   aRow  ,  int   aColumn  )  { 
return   model  .  getValueAt  (  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 




public   String   getColumnName  (  int   theCol  )  { 
return   this  .  model  .  getColumnName  (  theCol  )  ; 
} 








public   void   setValueAt  (  Object   aValue  ,  int   aRow  ,  int   aColumn  )  { 
model  .  setValueAt  (  aValue  ,  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 







public   void   sortByColumn  (  int   column  ,  boolean   ascending  )  { 
this  .  ascending  =  ascending  ; 
sortingColumns  .  removeAllElements  (  )  ; 
sortingColumns  .  addElement  (  new   Integer  (  column  )  )  ; 
sort  (  )  ; 
super  .  tableChanged  (  new   TableModelEvent  (  this  )  )  ; 
} 












public   void   addMouseListenerToHeaderInTable  (  JTable   table  )  { 
final   TableSorter   sorter  =  this  ; 
final   JTable   tableView  =  table  ; 
tableView  .  setColumnSelectionAllowed  (  false  )  ; 
MouseAdapter   listMouseListener  =  new   MouseAdapter  (  )  { 

public   void   mouseClicked  (  MouseEvent   e  )  { 
TableColumnModel   columnModel  =  tableView  .  getColumnModel  (  )  ; 
int   viewColumn  =  columnModel  .  getColumnIndexAtX  (  e  .  getX  (  )  )  ; 
int   clickedColumn  =  tableView  .  convertColumnIndexToModel  (  viewColumn  )  ; 
if  (  e  .  getClickCount  (  )  ==  1  &&  clickedColumn  !=  -  1  )  { 
TableSorter  .  this  .  sortingColumn  =  clickedColumn  ; 
int   shiftPressed  =  e  .  getModifiers  (  )  &  InputEvent  .  SHIFT_MASK  ; 
boolean   ascending  =  (  shiftPressed  ==  0  )  ; 
sorter  .  sortByColumn  (  clickedColumn  ,  ascending  )  ; 
TableSorter  .  this  .  sorted  =  true  ; 
int   colIndex  =  tableView  .  getColumnCount  (  )  ; 
javax  .  swing  .  table  .  TableColumn   tc  ; 
for  (  int   i  =  0  ;  i  <  colIndex  ;  i  ++  )  { 
tc  =  columnModel  .  getColumn  (  i  )  ; 
if  (  i  !=  viewColumn  )  { 
tc  .  setHeaderRenderer  (  null  )  ; 
} 
} 
tc  =  columnModel  .  getColumn  (  viewColumn  )  ; 
System  .  out  .  println  (  " the clicked column name is "  +  tableView  .  getColumnName  (  viewColumn  )  )  ; 
DefaultTableCellRenderer   headerRenderer  =  TableSorter  .  this  .  createTableCellRenderer  (  ascending  )  ; 
tc  .  setHeaderRenderer  (  headerRenderer  )  ; 
} 
} 
}  ; 
JTableHeader   th  =  tableView  .  getTableHeader  (  )  ; 
th  .  addMouseListener  (  listMouseListener  )  ; 
} 






public   boolean   getSortingStatus  (  )  { 
return   this  .  sorted  ; 
} 






public   boolean   isAscendingSorting  (  )  { 
return   this  .  ascending  ; 
} 







public   void   setSortingStatus  (  boolean   sorted  )  { 
this  .  sorted  =  sorted  ; 
} 





public   int  [  ]  getIndexes  (  )  { 
return   this  .  indexes  ; 
} 






public   void   setIndexes  (  int  [  ]  pIndexes  )  { 
this  .  indexes  =  pIndexes  ; 
} 






public   int   getSortingColumn  (  )  { 
return   this  .  sortingColumn  ; 
} 









@  SuppressWarnings  (  "serial"  ) 
public   DefaultTableCellRenderer   createTableCellRenderer  (  boolean   ascending  )  { 
final   boolean   isAscending  =  ascending  ; 
DefaultTableCellRenderer   tableHeaderRenderer  =  new   DefaultTableCellRenderer  (  )  { 

public   Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   isSelected  ,  boolean   hasFocus  ,  int   row  ,  int   column  )  { 
ImageIcon   up  =  new   ImageIcon  (  )  ; 
ImageIcon   down  =  new   ImageIcon  (  )  ; 
String   path  =  ResourceUtil  .  getResource  (  TableSorter  .  class  ,  "image.path"  )  ; 
String   upArrow  =  ResourceUtil  .  getResource  (  TableSorter  .  class  ,  "image.file.upArrow"  )  ; 
String   downArrow  =  ResourceUtil  .  getResource  (  TableSorter  .  class  ,  "image.file.downArrow"  )  ; 
try  { 
up  =  new   ImageIcon  (  Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  ClassLoader  .  getSystemResource  (  path  +  upArrow  )  )  )  ; 
down  =  new   ImageIcon  (  Toolkit  .  getDefaultToolkit  (  )  .  getImage  (  ClassLoader  .  getSystemResource  (  path  +  downArrow  )  )  )  ; 
}  catch  (  Exception   ex  )  { 
System  .  out  .  println  (  " Image fetching exception :"  +  ex  )  ; 
Log  .  log  (  Log  .  ERROR  ,  new   Throwable  (  )  ,  ex  ,  ResourceUtil  .  getMessage  (  TableSorter  .  class  ,  "image_fetching_exception"  )  )  ; 
} 
JButton   headerCellRenderer  =  new   JButton  (  )  ; 
headerCellRenderer  .  setText  (  (  value  ==  null  )  ?  ""  :  value  .  toString  (  )  )  ; 
headerCellRenderer  .  setBackground  (  UIManager  .  getColor  (  "Button.pressed"  )  )  ; 
headerCellRenderer  .  setBorder  (  new   CompoundBorder  (  new   BevelBorder  (  BevelBorder  .  RAISED  ,  Color  .  gray  ,  Color  .  white  )  ,  new   EmptyBorder  (  2  ,  2  ,  2  ,  2  )  )  )  ; 
if  (  isAscending  )  { 
headerCellRenderer  .  setIcon  (  up  )  ; 
}  else  { 
headerCellRenderer  .  setIcon  (  down  )  ; 
} 
return   headerCellRenderer  ; 
} 
}  ; 
return   tableHeaderRenderer  ; 
} 
} 

