package   com  .  javable  .  dataview  ; 

import   javax  .  swing  .  tree  .  DefaultMutableTreeNode  ; 




public   class   DataGroup   extends   DefaultMutableTreeNode  { 

private   String   name  ; 

private   String   info  ; 

private   int   xChannel  ; 







public   DataGroup  (  String   name  ,  String   info  )  { 
super  (  )  ; 
this  .  name  =  name  ; 
this  .  info  =  info  ; 
} 






public   void   addChannel  (  DataChannel   c  )  { 
this  .  add  (  new   DefaultMutableTreeNode  (  c  )  )  ; 
} 








public   DataChannel   getChannel  (  int   c  )  throws   ClassCastException  { 
return  (  DataChannel  )  (  (  DefaultMutableTreeNode  )  this  .  getChildAt  (  c  )  )  .  getUserObject  (  )  ; 
} 






public   String   getName  (  )  { 
return   name  ; 
} 






public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 






public   String   getInfo  (  )  { 
return   info  ; 
} 






public   void   setInfo  (  String   info  )  { 
this  .  info  =  info  ; 
} 






public   int   getXChannel  (  )  { 
return   xChannel  ; 
} 






public   void   setXChannel  (  int   x  )  { 
this  .  xChannel  =  x  ; 
} 
} 

