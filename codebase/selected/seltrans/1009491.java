package   org  .  aitools  .  programd  .  interfaces  .  graphical  ; 

import   java  .  awt  .  event  .  InputEvent  ; 
import   java  .  awt  .  event  .  MouseAdapter  ; 
import   java  .  awt  .  event  .  MouseEvent  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Vector  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  event  .  TableModelEvent  ; 
import   javax  .  swing  .  table  .  JTableHeader  ; 
import   javax  .  swing  .  table  .  TableColumnModel  ; 
import   javax  .  swing  .  table  .  TableModel  ; 
import   org  .  aitools  .  programd  .  util  .  DeveloperError  ; 


















public   class   TableSorter   extends   TableMap  { 

int   indexes  [  ]  ; 

Vector  <  Integer  >  sortingColumns  =  new   Vector  <  Integer  >  (  )  ; 

boolean   ascending  =  true  ; 

int   compares  ; 




public   TableSorter  (  )  { 
this  .  indexes  =  new   int  [  0  ]  ; 
} 




public   TableSorter  (  TableModel   modelToSet  )  { 
setModel  (  modelToSet  )  ; 
} 




@  Override 
public   void   setModel  (  TableModel   modelToSet  )  { 
super  .  setModel  (  modelToSet  )  ; 
reallocateIndexes  (  )  ; 
} 









public   int   compareRowsByColumn  (  int   row1  ,  int   row2  ,  int   column  )  { 
Class  <  ?  >  type  =  this  .  model  .  getColumnClass  (  column  )  ; 
TableModel   data  =  this  .  model  ; 
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
this  .  compares  ++  ; 
for  (  int   level  =  0  ;  level  <  this  .  sortingColumns  .  size  (  )  ;  level  ++  )  { 
Integer   column  =  this  .  sortingColumns  .  elementAt  (  level  )  ; 
int   result  =  compareRowsByColumn  (  row1  ,  row2  ,  column  .  intValue  (  )  )  ; 
if  (  result  !=  0  )  { 
return   this  .  ascending  ?  result  :  -  result  ; 
} 
} 
return   0  ; 
} 





public   void   reallocateIndexes  (  )  { 
int   rowCount  =  this  .  model  .  getRowCount  (  )  ; 
this  .  indexes  =  new   int  [  rowCount  ]  ; 
for  (  int   row  =  0  ;  row  <  rowCount  ;  row  ++  )  { 
this  .  indexes  [  row  ]  =  row  ; 
} 
} 




@  Override 
public   void   tableChanged  (  TableModelEvent   e  )  { 
reallocateIndexes  (  )  ; 
super  .  tableChanged  (  e  )  ; 
} 





public   void   checkModel  (  )  { 
if  (  this  .  indexes  .  length  !=  this  .  model  .  getRowCount  (  )  )  { 
throw   new   DeveloperError  (  "TableSorter model is not valid."  ,  new   IllegalStateException  (  "Sorter not informed of a change in model."  )  )  ; 
} 
} 




public   void   sort  (  )  { 
checkModel  (  )  ; 
this  .  compares  =  0  ; 
shuttlesort  (  this  .  indexes  .  clone  (  )  ,  this  .  indexes  ,  0  ,  this  .  indexes  .  length  )  ; 
} 




public   void   n2sort  (  )  { 
for  (  int   i  =  0  ;  i  <  getRowCount  (  )  ;  i  ++  )  { 
for  (  int   j  =  i  +  1  ;  j  <  getRowCount  (  )  ;  j  ++  )  { 
if  (  compare  (  this  .  indexes  [  i  ]  ,  this  .  indexes  [  j  ]  )  ==  -  1  )  { 
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
int   tmp  =  this  .  indexes  [  i  ]  ; 
this  .  indexes  [  i  ]  =  this  .  indexes  [  j  ]  ; 
this  .  indexes  [  j  ]  =  tmp  ; 
} 





@  Override 
public   Object   getValueAt  (  int   aRow  ,  int   aColumn  )  { 
checkModel  (  )  ; 
return   this  .  model  .  getValueAt  (  this  .  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 





@  Override 
public   void   setValueAt  (  Object   aValue  ,  int   aRow  ,  int   aColumn  )  { 
checkModel  (  )  ; 
this  .  model  .  setValueAt  (  aValue  ,  this  .  indexes  [  aRow  ]  ,  aColumn  )  ; 
} 






public   void   sortByColumn  (  int   column  )  { 
sortByColumn  (  column  ,  true  )  ; 
} 







public   void   sortByColumn  (  int   column  ,  boolean   ascendingSetting  )  { 
this  .  ascending  =  ascendingSetting  ; 
this  .  sortingColumns  .  removeAllElements  (  )  ; 
this  .  sortingColumns  .  addElement  (  new   Integer  (  column  )  )  ; 
sort  (  )  ; 
super  .  tableChanged  (  new   TableModelEvent  (  this  )  )  ; 
} 







public   void   addMouseListenerToHeaderInTable  (  JTable   table  )  { 
final   TableSorter   sorter  =  this  ; 
final   JTable   tableView  =  table  ; 
tableView  .  setColumnSelectionAllowed  (  false  )  ; 
MouseAdapter   listMouseListener  =  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
TableColumnModel   columnModel  =  tableView  .  getColumnModel  (  )  ; 
int   viewColumn  =  columnModel  .  getColumnIndexAtX  (  e  .  getX  (  )  )  ; 
int   column  =  tableView  .  convertColumnIndexToModel  (  viewColumn  )  ; 
if  (  e  .  getClickCount  (  )  ==  1  &&  column  !=  -  1  )  { 
int   shiftPressed  =  e  .  getModifiers  (  )  &  InputEvent  .  SHIFT_MASK  ; 
sorter  .  sortByColumn  (  column  ,  (  shiftPressed  ==  0  )  )  ; 
} 
} 
}  ; 
JTableHeader   th  =  tableView  .  getTableHeader  (  )  ; 
th  .  addMouseListener  (  listMouseListener  )  ; 
} 
} 

