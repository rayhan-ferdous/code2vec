package   com  .  debitors  .  table  ; 

import   java  .  util  .  *  ; 
import   com  .  debitors  .  vo  .  PaymentVO  ; 
import   com  .  util  .  comparator  .  *  ; 
import   com  .  util  .  table  .  *  ; 

public   class   PaymentModel   implements   ITableModel  { 

protected   PaymentVO   fTotal  ; 

protected   PaymentVO  [  ]  fTableData  ; 

protected   String   fSortedColumn  ; 

protected   int  [  ]  fSortOrder  ; 

protected   Comparator   fComparator  ; 

public   PaymentModel  (  )  { 
super  (  )  ; 
} 

public   void   setData  (  Object  [  ]  data  )  { 
fTableData  =  (  PaymentVO  [  ]  )  data  ; 
fSortOrder  =  new   int  [  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ]  ; 
for  (  int   i  =  0  ;  i  <  fSortOrder  .  length  ;  i  ++  )  { 
fSortOrder  [  i  ]  =  i  ; 
} 
} 

public   Object   getValueAt  (  int   pRow  ,  String   pColumn  )  { 
PaymentVO   vo  =  fTableData  [  fSortOrder  [  pRow  ]  ]  ; 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getId  (  )  )  ; 
if  (  "type"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getType  (  )  )  ; 
if  (  "customerID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getCustomerID  (  )  )  ; 
if  (  "amount"  .  equals  (  pColumn  )  )  return   vo  .  getAmount  (  )  ; 
if  (  "currencyID"  .  equals  (  pColumn  )  )  return   new   Short  (  vo  .  getCurrencyID  (  )  )  ; 
if  (  "creationDate"  .  equals  (  pColumn  )  )  return   vo  .  getCreationDate  (  )  ; 
if  (  "valueDate"  .  equals  (  pColumn  )  )  return   vo  .  getValueDate  (  )  ; 
if  (  "bookText"  .  equals  (  pColumn  )  )  return   vo  .  getBookText  (  )  ; 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  return   vo  .  getReferenceNumber  (  )  ; 
if  (  "modified"  .  equals  (  pColumn  )  )  return   vo  .  getModified  (  )  ; 
return   null  ; 
} 

public   Object   getTotalValueAt  (  String   pColumn  )  { 
if  (  fTotal  !=  null  )  { 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getId  (  )  )  ; 
if  (  "type"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getType  (  )  )  ; 
if  (  "customerID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getCustomerID  (  )  )  ; 
if  (  "amount"  .  equals  (  pColumn  )  )  return   fTotal  .  getAmount  (  )  ; 
if  (  "currencyID"  .  equals  (  pColumn  )  )  return   new   Short  (  fTotal  .  getCurrencyID  (  )  )  ; 
if  (  "creationDate"  .  equals  (  pColumn  )  )  return   fTotal  .  getCreationDate  (  )  ; 
if  (  "valueDate"  .  equals  (  pColumn  )  )  return   fTotal  .  getValueDate  (  )  ; 
if  (  "bookText"  .  equals  (  pColumn  )  )  return   fTotal  .  getBookText  (  )  ; 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  return   fTotal  .  getReferenceNumber  (  )  ; 
if  (  "modified"  .  equals  (  pColumn  )  )  return   fTotal  .  getModified  (  )  ; 
} 
return   null  ; 
} 

public   void   sort  (  String   pColumn  ,  String   pSortDirection  )  { 
boolean   up  =  true  ; 
if  (  !  "u"  .  equals  (  pSortDirection  )  )  { 
up  =  false  ; 
} 
if  (  "id"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getId  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "type"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getType  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "customerID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCustomerID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "amount"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   BigDecimalComparator  (  )  ; 
java  .  math  .  BigDecimal  [  ]  temp  =  new   java  .  math  .  BigDecimal  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getAmount  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "currencyID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
short  [  ]  temp  =  new   short  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCurrencyID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortShort  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "creationDate"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCreationDate  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "valueDate"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getValueDate  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "bookText"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getBookText  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getReferenceNumber  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "modified"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   TimestampComparator  (  )  ; 
java  .  sql  .  Timestamp  [  ]  temp  =  new   java  .  sql  .  Timestamp  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getModified  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
fSortedColumn  =  pColumn  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
private   void   sort  (  Object  [  ]  a  ,  int   lo0  ,  int   hi0  ,  boolean   up  )  { 
int   lo  =  lo0  ; 
int   hi  =  hi0  ; 
if  (  lo  >=  hi  )  { 
return  ; 
} 
int   mid  =  (  lo  +  hi  )  /  2  ; 
sort  (  a  ,  lo  ,  mid  ,  up  )  ; 
sort  (  a  ,  mid  +  1  ,  hi  ,  up  )  ; 
int   end_lo  =  mid  ; 
int   start_hi  =  mid  +  1  ; 
while  (  (  lo  <=  end_lo  )  &&  (  start_hi  <=  hi  )  )  { 
boolean   isChange  ; 
if  (  up  )  { 
isChange  =  (  fComparator  .  compare  (  a  [  fSortOrder  [  lo  ]  ]  ,  a  [  fSortOrder  [  start_hi  ]  ]  )  <=  0  )  ; 
}  else  { 
isChange  =  (  fComparator  .  compare  (  a  [  fSortOrder  [  lo  ]  ]  ,  a  [  fSortOrder  [  start_hi  ]  ]  )  >=  0  )  ; 
} 
if  (  isChange  )  { 
lo  ++  ; 
}  else  { 
int   T  =  fSortOrder  [  start_hi  ]  ; 
for  (  int   k  =  start_hi  -  1  ;  k  >=  lo  ;  k  --  )  { 
fSortOrder  [  k  +  1  ]  =  fSortOrder  [  k  ]  ; 
} 
fSortOrder  [  lo  ]  =  T  ; 
lo  ++  ; 
end_lo  ++  ; 
start_hi  ++  ; 
} 
} 
} 

public   String  [  ]  getSortedColumns  (  )  { 
if  (  fSortedColumn  !=  null  )  { 
return   new   String  [  ]  {  fSortedColumn  }  ; 
}  else  { 
return   null  ; 
} 
} 

public   Object   getRowAt  (  int   index  )  { 
return  (  fTableData  !=  null  ?  fTableData  [  fSortOrder  [  index  ]  ]  :  null  )  ; 
} 

public   Object   getTotalRow  (  )  { 
return   fTotal  ; 
} 

public   int   getRowCount  (  )  { 
return  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ; 
} 
} 

