package   org  .  dinopolis  .  timmon  .  frontend  .  treetable  ; 

import   java  .  awt  .  Component  ; 
import   java  .  awt  .  event  .  MouseAdapter  ; 
import   java  .  awt  .  event  .  MouseEvent  ; 
import   java  .  awt  .  event  .  InputEvent  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  JLabel  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  event  .  TableModelEvent  ; 
import   javax  .  swing  .  table  .  TableCellRenderer  ; 
import   javax  .  swing  .  table  .  TableColumnModel  ; 
import   javax  .  swing  .  table  .  TableModel  ; 
import   javax  .  swing  .  table  .  JTableHeader  ; 




public   class   TableSorter   extends   TableMap  { 




private   static   final   long   serialVersionUID  =  1L  ; 


private   int   indexes_  [  ]  ; 


private   Vector   sorting_columns_  =  new   Vector  (  )  ; 


private   boolean   ascending_  =  true  ; 


private   int   sorted_column_  =  -  1  ; 


private   int   compares_  ; 


private   JTable   table_  ; 




public   TableSorter  (  )  { 
indexes_  =  new   int  [  0  ]  ; 
} 







public   TableSorter  (  TableModel   model  )  { 
setModel  (  model  )  ; 
} 







public   void   setModel  (  TableModel   model  )  { 
super  .  setModel  (  model  )  ; 
reallocateIndexes  (  )  ; 
} 









public   int   compareRowsByColumn  (  int   row1  ,  int   row2  ,  int   column  )  { 
Class   type  =  model_  .  getColumnClass  (  column  )  ; 
TableModel   data  =  model_  ; 
Object   o1  =  data  .  getValueAt  (  row1  ,  column  )  ; 
Object   o2  =  data  .  getValueAt  (  row2  ,  column  )  ; 
if  (  o1  ==  null  &&  o2  ==  null  )  { 
return  (  0  )  ; 
}  else   if  (  o1  ==  null  )  { 
return  (  -  1  )  ; 
}  else   if  (  o2  ==  null  )  { 
return   1  ; 
} 
if  (  type  .  getSuperclass  (  )  ==  java  .  lang  .  Number  .  class  )  { 
Number   n1  =  (  Number  )  o1  ; 
double   d1  =  n1  .  doubleValue  (  )  ; 
Number   n2  =  (  Number  )  o2  ; 
double   d2  =  n2  .  doubleValue  (  )  ; 
if  (  d1  <  d2  )  { 
return  -  1  ; 
}  else   if  (  d1  >  d2  )  { 
return   1  ; 
}  else  { 
return   0  ; 
} 
}  else   if  (  type  ==  java  .  util  .  Date  .  class  )  { 
Date   d1  =  (  Date  )  o1  ; 
long   n1  =  d1  .  getTime  (  )  ; 
Date   d2  =  (  Date  )  o2  ; 
long   n2  =  d2  .  getTime  (  )  ; 
if  (  n1  <  n2  )  { 
return  (  -  1  )  ; 
}  else   if  (  n1  >  n2  )  { 
return  (  1  )  ; 
}  else  { 
return  (  0  )  ; 
} 
}  else   if  (  type  ==  String  .  class  )  { 
String   s1  =  (  String  )  o1  ; 
String   s2  =  (  String  )  o2  ; 
int   result  =  s1  .  compareTo  (  s2  )  ; 
if  (  result  <  0  )  { 
return  (  -  1  )  ; 
}  else   if  (  result  >  0  )  { 
return  (  1  )  ; 
}  else  { 
return  (  0  )  ; 
} 
}  else   if  (  type  ==  Boolean  .  class  )  { 
Boolean   bool1  =  (  Boolean  )  o1  ; 
boolean   b1  =  bool1  .  booleanValue  (  )  ; 
Boolean   bool2  =  (  Boolean  )  o2  ; 
boolean   b2  =  bool2  .  booleanValue  (  )  ; 
if  (  b1  ==  b2  )  { 
return  (  0  )  ; 
}  else   if  (  b1  )  { 
return  (  1  )  ; 
}  else  { 
return  (  -  1  )  ; 
} 
}  else  { 
String   s1  =  o1  .  toString  (  )  ; 
String   s2  =  o2  .  toString  (  )  ; 
int   result  =  s1  .  compareTo  (  s2  )  ; 
if  (  result  <  0  )  { 
return  (  -  1  )  ; 
}  else   if  (  result  >  0  )  { 
return  (  1  )  ; 
}  else  { 
return  (  0  )  ; 
} 
} 
} 








public   int   compare  (  int   row1  ,  int   row2  )  { 
compares_  ++  ; 
for  (  int   level  =  0  ;  level  <  sorting_columns_  .  size  (  )  ;  level  ++  )  { 
Integer   column  =  (  Integer  )  sorting_columns_  .  elementAt  (  level  )  ; 
int   result  =  compareRowsByColumn  (  row1  ,  row2  ,  column  .  intValue  (  )  )  ; 
if  (  result  !=  0  )  { 
return  (  ascending_  ?  result  :  -  result  )  ; 
} 
} 
return  (  0  )  ; 
} 




private   synchronized   void   reallocateIndexes  (  )  { 
int   row_count  =  model_  .  getRowCount  (  )  ; 
indexes_  =  new   int  [  row_count  ]  ; 
sorted_column_  =  -  1  ; 
for  (  int   row  =  0  ;  row  <  row_count  ;  row  ++  )  indexes_  [  row  ]  =  row  ; 
} 







public   void   tableChanged  (  TableModelEvent   event  )  { 
if  (  sorted_column_  <  0  )  { 
super  .  tableChanged  (  event  )  ; 
if  (  table_  !=  null  )  table_  .  getTableHeader  (  )  .  repaint  (  )  ; 
return  ; 
} 
int   first  =  event  .  getFirstRow  (  )  ; 
int   last  =  event  .  getLastRow  (  )  ; 
if  (  event  .  getType  (  )  !=  TableModelEvent  .  UPDATE  )  { 
reallocateIndexes  (  )  ; 
}  else  { 
synchronized  (  this  )  { 
if  (  indexes_  .  length  !=  model_  .  getRowCount  (  )  )  { 
int  [  ]  old  =  indexes_  ; 
indexes_  =  new   int  [  model_  .  getRowCount  (  )  ]  ; 
System  .  arraycopy  (  old  ,  0  ,  indexes_  ,  0  ,  Math  .  min  (  old  .  length  ,  indexes_  .  length  )  )  ; 
for  (  int   count  =  old  .  length  ;  count  <  indexes_  .  length  ;  count  ++  )  indexes_  [  count  ]  =  count  ; 
sortByColumn  (  sorted_column_  ,  ascending_  )  ; 
} 
if  (  first  <  indexes_  .  length  )  first  =  indexes_  [  first  ]  ; 
if  (  last  <  indexes_  .  length  )  last  =  indexes_  [  last  ]  ; 
} 
} 
super  .  tableChanged  (  new   TableModelEvent  (  this  ,  first  ,  last  ,  event  .  getColumn  (  )  ,  event  .  getType  (  )  )  )  ; 
if  (  table_  !=  null  )  table_  .  getTableHeader  (  )  .  repaint  (  )  ; 
} 






private   void   sort  (  Object   sender  )  { 
compares_  =  0  ; 
shuttlesort  (  (  int  [  ]  )  indexes_  .  clone  (  )  ,  indexes_  ,  0  ,  indexes_  .  length  )  ; 
} 









private   void   shuttlesort  (  int   from  [  ]  ,  int   to  [  ]  ,  int   low  ,  int   high  )  { 
if  (  high  -  low  <  2  )  return  ; 
int   middle  =  (  low  +  high  )  /  2  ; 
shuttlesort  (  to  ,  from  ,  low  ,  middle  )  ; 
shuttlesort  (  to  ,  from  ,  middle  ,  high  )  ; 
int   p  =  low  ; 
int   q  =  middle  ; 
if  (  high  -  low  >=  4  &&  compare  (  from  [  middle  -  1  ]  ,  from  [  middle  ]  )  <=  0  )  { 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  to  [  i  ]  =  from  [  i  ]  ; 
return  ; 
} 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  { 
if  (  q  >=  high  ||  (  p  <  middle  &&  compare  (  from  [  p  ]  ,  from  [  q  ]  )  <=  0  )  )  to  [  i  ]  =  from  [  p  ++  ]  ;  else   to  [  i  ]  =  from  [  q  ++  ]  ; 
} 
} 










public   synchronized   Object   getValueAt  (  int   row  ,  int   column  )  { 
if  (  sorted_column_  >=  0  )  return  (  model_  .  getValueAt  (  indexes_  [  row  ]  ,  column  )  )  ; 
return  (  model_  .  getValueAt  (  row  ,  column  )  )  ; 
} 












public   synchronized   void   setValueAt  (  Object   value  ,  int   row  ,  int   column  )  { 
if  (  sorted_column_  >=  0  )  { 
model_  .  setValueAt  (  value  ,  indexes_  [  row  ]  ,  column  )  ; 
return  ; 
} 
model_  .  setValueAt  (  value  ,  row  ,  column  )  ; 
} 








private   synchronized   void   sortByColumn  (  int   column  ,  boolean   ascending  )  { 
if  (  indexes_  .  length  !=  model_  .  getRowCount  (  )  )  reallocateIndexes  (  )  ; 
ascending_  =  ascending  ; 
sorted_column_  =  column  ; 
sorting_columns_  .  removeAllElements  (  )  ; 
sorting_columns_  .  addElement  (  new   Integer  (  column  )  )  ; 
sort  (  this  )  ; 
super  .  tableChanged  (  new   TableModelEvent  (  this  )  )  ; 
} 







public   void   addMouseListenerToHeaderInTable  (  JTable   table  )  { 
table_  =  table  ; 
table  .  getTableHeader  (  )  .  setDefaultRenderer  (  new   TableSorterCellRenderer  (  table  .  getTableHeader  (  )  .  getDefaultRenderer  (  )  )  )  ; 
final   TableSorter   sorter  =  this  ; 
final   JTable   tableView  =  table  ; 
tableView  .  setColumnSelectionAllowed  (  false  )  ; 
MouseAdapter   listMouseListener  =  new   MouseAdapter  (  )  { 

public   void   mouseClicked  (  MouseEvent   e  )  { 
TableColumnModel   columnModel  =  tableView  .  getColumnModel  (  )  ; 
int   viewColumn  =  columnModel  .  getColumnIndexAtX  (  e  .  getX  (  )  )  ; 
int   column  =  tableView  .  convertColumnIndexToModel  (  viewColumn  )  ; 
if  (  e  .  getClickCount  (  )  ==  1  &&  column  !=  -  1  )  { 
int   shiftPressed  =  e  .  getModifiers  (  )  &  InputEvent  .  SHIFT_MASK  ; 
boolean   ascending  =  (  shiftPressed  ==  0  )  ; 
if  (  (  ascending  )  &&  (  sorted_column_  ==  column  )  )  ascending  =  !  ascending_  ; 
sorter  .  sortByColumn  (  column  ,  ascending  )  ; 
} 
} 
}  ; 
JTableHeader   th  =  tableView  .  getTableHeader  (  )  ; 
th  .  addMouseListener  (  listMouseListener  )  ; 
} 




class   TableSorterCellRenderer   implements   TableCellRenderer  { 

private   TableCellRenderer   original_renderer_  ; 






TableSorterCellRenderer  (  TableCellRenderer   original_renderer  )  { 
original_renderer_  =  original_renderer  ; 
} 












public   Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   is_selected  ,  boolean   has_focus  ,  int   row  ,  int   column  )  { 
Component   original  =  original_renderer_  .  getTableCellRendererComponent  (  table  ,  value  ,  is_selected  ,  has_focus  ,  row  ,  column  )  ; 
if  (  original   instanceof   JLabel  )  { 
JLabel   label  =  (  JLabel  )  original  ; 
label  .  setIcon  (  null  )  ; 
if  (  sorted_column_  ==  table  .  convertColumnIndexToModel  (  column  )  )  { 
if  (  ascending_  )  label  .  setIcon  (  TreeTableConstants  .  UP_ICON  )  ;  else   label  .  setIcon  (  TreeTableConstants  .  DOWN_ICON  )  ; 
} 
} 
return  (  original  )  ; 
} 
} 
} 

