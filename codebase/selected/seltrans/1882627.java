package   com  .  cidero  .  swing  .  table  ; 

import   java  .  awt  .  event  .  InputEvent  ; 
import   java  .  awt  .  event  .  MouseAdapter  ; 
import   java  .  awt  .  event  .  MouseEvent  ; 
import   java  .  util  .  Vector  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  event  .  TableModelEvent  ; 
import   javax  .  swing  .  table  .  JTableHeader  ; 
import   javax  .  swing  .  table  .  TableColumnModel  ; 
import   javax  .  swing  .  table  .  TableModel  ; 

public   class   TableSorter   extends   TableMap  { 

int   indexes  [  ]  ; 

Vector   sortingColumns  =  new   Vector  (  )  ; 

boolean   ascending  =  true  ; 

int   compares  ; 

public   TableSorter  (  )  { 
indexes  =  new   int  [  0  ]  ; 
} 

public   TableSorter  (  TableModel   model  )  { 
setModel  (  model  )  ; 
} 

public   void   setModel  (  TableModel   model  )  { 
super  .  setModel  (  model  )  ; 
reallocateIndexes  (  )  ; 
} 

public   int   compareRowsByColumn  (  int   row1  ,  int   row2  ,  int   column  )  { 
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
if  (  o1   instanceof   Comparable  )  { 
return  (  (  Comparable  )  o1  )  .  compareTo  (  o2  )  ; 
} 
return   0  ; 
} 

public   int   compare  (  int   row1  ,  int   row2  )  { 
compares  ++  ; 
for  (  int   level  =  0  ;  level  <  sortingColumns  .  size  (  )  ;  level  ++  )  { 
Integer   column  =  (  Integer  )  sortingColumns  .  elementAt  (  level  )  ; 
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

public   void   checkModel  (  )  { 
if  (  indexes  .  length  !=  model  .  getRowCount  (  )  )  { 
System  .  err  .  println  (  "Sorter not informed of a change in model."  )  ; 
} 
} 

public   void   sort  (  Object   sender  )  { 
checkModel  (  )  ; 
compares  =  0  ; 
shuttlesort  (  (  int  [  ]  )  indexes  .  clone  (  )  ,  indexes  ,  0  ,  indexes  .  length  )  ; 
} 

public   void   n2sort  (  )  { 
for  (  int   i  =  0  ;  i  <  getRowCount  (  )  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  getRowCount  (  )  ;  j  ++  )  { 
if  (  compare  (  indexes  [  i  ]  ,  indexes  [  j  ]  )  ==  -  1  )  { 
swap  (  i  ,  j  )  ; 
} 
} 
} 
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

public   void   swap  (  int   i  ,  int   j  )  { 
int   tmp  =  indexes  [  i  ]  ; 
indexes  [  i  ]  =  indexes  [  j  ]  ; 
indexes  [  j  ]  =  tmp  ; 
} 

public   Object   getValueAt  (  int   aRow  ,  int   aColumn  )  { 
checkModel  (  )  ; 
return   model  .  getValueAt  (  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 

public   void   setValueAt  (  Object   aValue  ,  int   aRow  ,  int   aColumn  )  { 
checkModel  (  )  ; 
model  .  setValueAt  (  aValue  ,  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 

public   void   sortByColumn  (  int   column  )  { 
sortByColumn  (  column  ,  true  )  ; 
} 

public   void   sortByColumn  (  int   column  ,  boolean   ascending  )  { 
this  .  ascending  =  ascending  ; 
sortingColumns  .  removeAllElements  (  )  ; 
sortingColumns  .  addElement  (  new   Integer  (  column  )  )  ; 
sort  (  this  )  ; 
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
int   column  =  tableView  .  convertColumnIndexToModel  (  viewColumn  )  ; 
if  (  e  .  getClickCount  (  )  ==  1  &&  column  !=  -  1  )  { 
int   shiftPressed  =  e  .  getModifiers  (  )  &  InputEvent  .  SHIFT_MASK  ; 
boolean   ascending  =  (  shiftPressed  ==  0  )  ; 
sorter  .  sortByColumn  (  column  ,  ascending  )  ; 
} 
} 
}  ; 
JTableHeader   th  =  tableView  .  getTableHeader  (  )  ; 
th  .  addMouseListener  (  listMouseListener  )  ; 
} 




public   int   getUnsortedRowIndex  (  int   sortedRowIndex  )  { 
return   indexes  [  sortedRowIndex  ]  ; 
} 
} 

