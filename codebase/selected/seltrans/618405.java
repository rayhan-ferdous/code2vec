package   ch  .  laoe  .  clip  ; 

import   ch  .  laoe  .  operation  .  AOperation  ; 
import   ch  .  laoe  .  ui  .  GEditableArea  ; 
































public   class   AChannel2DSelection  { 




public   AChannel2DSelection  (  AChannel   ch  ,  GEditableArea   area  )  { 
this  .  channel  =  ch  ; 
this  .  area  =  area  ; 
} 

private   AChannel   channel  ; 

private   GEditableArea   area  ; 

public   GEditableArea   getArea  (  )  { 
return   area  ; 
} 

public   AChannel   getChannel  (  )  { 
return   channel  ; 
} 

public   boolean   isSelected  (  )  { 
return   area  .  isSomethingSelected  (  )  ; 
} 

public   boolean   isSelected  (  double   x  ,  double   y  )  { 
return   area  .  isSelected  (  x  ,  y  )  ; 
} 




public   void   operateChannel  (  AOperation   o  )  { 
o  .  startOperation  (  )  ; 
if  (  isSelected  (  )  )  { 
o  .  operate  (  this  )  ; 
} 
o  .  endOperation  (  )  ; 
System  .  gc  (  )  ; 
} 
} 

