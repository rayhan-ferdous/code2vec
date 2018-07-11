package   org  .  archive  .  crawler  .  framework  ; 

import   java  .  io  .  PrintWriter  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  TreeSet  ; 
import   org  .  archive  .  util  .  ArchiveUtils  ; 
import   org  .  archive  .  util  .  Histotable  ; 
import   org  .  archive  .  util  .  Reporter  ; 











public   class   ToePool   extends   ThreadGroup   implements   Reporter  { 


public   static   int   DEFAULT_TOE_PRIORITY  =  Thread  .  NORM_PRIORITY  -  1  ; 

protected   CrawlController   controller  ; 

protected   int   nextSerialNumber  =  1  ; 

protected   int   targetSize  =  0  ; 






public   ToePool  (  CrawlController   c  )  { 
super  (  "ToeThreads"  )  ; 
this  .  controller  =  c  ; 
} 

public   void   cleanup  (  )  { 
this  .  controller  =  null  ; 
} 




public   int   getActiveToeCount  (  )  { 
Thread  [  ]  toes  =  getToes  (  )  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  (  toes  [  i  ]  instanceof   ToeThread  )  &&  (  (  ToeThread  )  toes  [  i  ]  )  .  isActive  (  )  )  { 
count  ++  ; 
} 
} 
return   count  ; 
} 





public   int   getToeCount  (  )  { 
Thread  [  ]  toes  =  getToes  (  )  ; 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
count  ++  ; 
} 
} 
return   count  ; 
} 

private   Thread  [  ]  getToes  (  )  { 
Thread  [  ]  toes  =  new   Thread  [  activeCount  (  )  +  10  ]  ; 
this  .  enumerate  (  toes  )  ; 
return   toes  ; 
} 






public   void   setSize  (  int   newsize  )  { 
targetSize  =  newsize  ; 
int   difference  =  newsize  -  getToeCount  (  )  ; 
if  (  difference  >  0  )  { 
for  (  int   i  =  1  ;  i  <=  difference  ;  i  ++  )  { 
startNewThread  (  )  ; 
} 
}  else  { 
int   retainedToes  =  targetSize  ; 
Thread  [  ]  toes  =  this  .  getToes  (  )  ; 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  !  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
continue  ; 
} 
retainedToes  --  ; 
if  (  retainedToes  >=  0  )  { 
continue  ; 
} 
ToeThread   tt  =  (  ToeThread  )  toes  [  i  ]  ; 
tt  .  retire  (  )  ; 
} 
} 
} 













public   void   killThread  (  int   threadNumber  ,  boolean   replace  )  { 
Thread  [  ]  toes  =  getToes  (  )  ; 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  !  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
continue  ; 
} 
ToeThread   toe  =  (  ToeThread  )  toes  [  i  ]  ; 
if  (  toe  .  getSerialNumber  (  )  ==  threadNumber  )  { 
toe  .  kill  (  )  ; 
} 
} 
if  (  replace  )  { 
startNewThread  (  )  ; 
} 
} 

private   synchronized   void   startNewThread  (  )  { 
ToeThread   newThread  =  new   ToeThread  (  this  ,  nextSerialNumber  ++  )  ; 
newThread  .  setPriority  (  DEFAULT_TOE_PRIORITY  )  ; 
newThread  .  start  (  )  ; 
} 




public   CrawlController   getController  (  )  { 
return   controller  ; 
} 

public   static   String   STANDARD_REPORT  =  "standard"  ; 

public   static   String   COMPACT_REPORT  =  "compact"  ; 

protected   static   String  [  ]  REPORTS  =  {  STANDARD_REPORT  ,  COMPACT_REPORT  }  ; 

public   String  [  ]  getReports  (  )  { 
return   REPORTS  ; 
} 

public   void   reportTo  (  String   name  ,  PrintWriter   writer  )  { 
if  (  COMPACT_REPORT  .  equals  (  name  )  )  { 
compactReportTo  (  writer  )  ; 
return  ; 
} 
if  (  name  !=  null  &&  !  STANDARD_REPORT  .  equals  (  name  )  )  { 
writer  .  print  (  name  )  ; 
writer  .  print  (  " not recognized: giving standard report/n"  )  ; 
} 
standardReportTo  (  writer  )  ; 
} 

protected   void   standardReportTo  (  PrintWriter   writer  )  { 
writer  .  print  (  "Toe threads report - "  +  ArchiveUtils  .  get12DigitDate  (  )  +  "\n"  )  ; 
writer  .  print  (  " Job being crawled: "  +  this  .  controller  .  getOrder  (  )  .  getCrawlOrderName  (  )  +  "\n"  )  ; 
writer  .  print  (  " Number of toe threads in pool: "  +  getToeCount  (  )  +  " ("  +  getActiveToeCount  (  )  +  " active)\n"  )  ; 
Thread  [  ]  toes  =  this  .  getToes  (  )  ; 
synchronized  (  toes  )  { 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  !  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
continue  ; 
} 
ToeThread   tt  =  (  ToeThread  )  toes  [  i  ]  ; 
if  (  tt  !=  null  )  { 
writer  .  print  (  "   ToeThread #"  +  tt  .  getSerialNumber  (  )  +  "\n"  )  ; 
tt  .  reportTo  (  writer  )  ; 
} 
} 
} 
} 

protected   void   compactReportTo  (  PrintWriter   writer  )  { 
writer  .  print  (  getToeCount  (  )  +  " threads ("  +  getActiveToeCount  (  )  +  " active)\n"  )  ; 
Thread  [  ]  toes  =  this  .  getToes  (  )  ; 
boolean   legendWritten  =  false  ; 
synchronized  (  toes  )  { 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  !  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
continue  ; 
} 
ToeThread   tt  =  (  ToeThread  )  toes  [  i  ]  ; 
if  (  tt  !=  null  )  { 
if  (  !  legendWritten  )  { 
writer  .  println  (  tt  .  singleLineLegend  (  )  )  ; 
legendWritten  =  true  ; 
} 
tt  .  singleLineReportTo  (  writer  )  ; 
} 
} 
} 
} 

public   void   singleLineReportTo  (  PrintWriter   w  )  { 
Histotable   ht  =  new   Histotable  (  )  ; 
Thread  [  ]  toes  =  getToes  (  )  ; 
for  (  int   i  =  0  ;  i  <  toes  .  length  ;  i  ++  )  { 
if  (  !  (  toes  [  i  ]  instanceof   ToeThread  )  )  { 
continue  ; 
} 
ToeThread   tt  =  (  ToeThread  )  toes  [  i  ]  ; 
if  (  tt  !=  null  )  { 
ht  .  tally  (  tt  .  getStep  (  )  )  ; 
} 
} 
TreeSet   sorted  =  ht  .  getSorted  (  )  ; 
w  .  print  (  getToeCount  (  )  )  ; 
w  .  print  (  " threads: "  )  ; 
w  .  print  (  Histotable  .  entryString  (  sorted  .  first  (  )  )  )  ; 
if  (  sorted  .  size  (  )  >  1  )  { 
Iterator   iter  =  sorted  .  iterator  (  )  ; 
iter  .  next  (  )  ; 
w  .  print  (  "; "  )  ; 
w  .  print  (  Histotable  .  entryString  (  iter  .  next  (  )  )  )  ; 
} 
if  (  sorted  .  size  (  )  >  2  )  { 
w  .  print  (  "; etc..."  )  ; 
} 
} 

public   String   singleLineLegend  (  )  { 
return  "total: mostCommonStateTotal secondMostCommonStateTotal"  ; 
} 

public   String   singleLineReport  (  )  { 
return   ArchiveUtils  .  singleLineReport  (  this  )  ; 
} 

public   void   reportTo  (  PrintWriter   writer  )  { 
reportTo  (  null  ,  writer  )  ; 
} 
} 

