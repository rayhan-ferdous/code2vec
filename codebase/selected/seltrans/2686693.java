package   org  .  mitre  .  bio  .  phylo  .  tree  ; 











public   class   SimpleNode   implements   Node  { 


private   Node   parent  ; 


private   int   number  ; 


private   byte  [  ]  sequence  ; 


private   double   length  ; 


private   double   lengthSE  ; 


private   double   height  ; 


private   String   identifier  ; 

private   Node  [  ]  child  ; 


public   SimpleNode  (  )  { 
parent  =  null  ; 
child  =  null  ; 
length  =  0.0  ; 
lengthSE  =  0.0  ; 
height  =  0.0  ; 
identifier  =  ""  ; 
number  =  0  ; 
sequence  =  null  ; 
} 

public   SimpleNode  (  String   name  ,  double   branchLength  )  { 
this  (  )  ; 
identifier  =  name  ; 
length  =  branchLength  ; 
} 







protected   SimpleNode  (  Node  [  ]  children  ,  double   branchLength  )  { 
this  (  )  ; 
this  .  child  =  children  ; 
if  (  children  .  length  ==  1  )  { 
throw   new   IllegalArgumentException  (  "Must have more than one child!"  )  ; 
} 
for  (  int   i  =  0  ;  i  <  child  .  length  ;  i  ++  )  { 
child  [  i  ]  .  setParent  (  this  )  ; 
} 
this  .  length  =  branchLength  ; 
} 


public   SimpleNode  (  Node   n  )  { 
this  (  n  ,  true  )  ; 
} 

public   void   reset  (  )  { 
parent  =  null  ; 
child  =  null  ; 
length  =  0.0  ; 
lengthSE  =  0.0  ; 
height  =  0.0  ; 
identifier  =  ""  ; 
number  =  0  ; 
sequence  =  null  ; 
} 

public   SimpleNode  (  Node   n  ,  boolean   keepIds  )  { 
init  (  n  ,  keepIds  )  ; 
for  (  int   i  =  0  ;  i  <  n  .  getChildCount  (  )  ;  i  ++  )  { 
addChild  (  new   SimpleNode  (  n  .  getChild  (  i  )  ,  keepIds  )  )  ; 
} 
} 

protected   void   init  (  Node   n  )  { 
init  (  n  ,  true  )  ; 
} 





private   void   init  (  Node   n  ,  boolean   keepId  )  { 
parent  =  null  ; 
length  =  n  .  getBranchLength  (  )  ; 
lengthSE  =  n  .  getBranchLengthSE  (  )  ; 
height  =  n  .  getNodeHeight  (  )  ; 
if  (  keepId  )  { 
identifier  =  n  .  getIdentifier  (  )  ; 
}  else  { 
identifier  =  ""  ; 
} 
number  =  n  .  getNumber  (  )  ; 
sequence  =  n  .  getSequence  (  )  ; 
child  =  null  ; 
} 




public   Node   getParent  (  )  { 
return   parent  ; 
} 


public   void   setParent  (  Node   node  )  { 
parent  =  node  ; 
} 




public   void   removeParent  (  )  { 
parent  =  null  ; 
} 

public   String   getIdentifier  (  )  { 
return   this  .  identifier  ; 
} 

public   void   setIdentifier  (  String   id  )  { 
this  .  identifier  =  id  ; 
} 




public   String   getSequenceString  (  )  { 
return   new   String  (  sequence  )  ; 
} 




public   byte  [  ]  getSequence  (  )  { 
return   sequence  ; 
} 




public   void   setSequence  (  byte  [  ]  s  )  { 
sequence  =  s  ; 
} 




public   final   double   getBranchLength  (  )  { 
return   length  ; 
} 




public   final   void   setBranchLength  (  double   value  )  { 
length  =  value  ; 
} 




public   final   double   getBranchLengthSE  (  )  { 
return   lengthSE  ; 
} 




public   final   void   setBranchLengthSE  (  double   value  )  { 
lengthSE  =  value  ; 
} 




public   final   double   getNodeHeight  (  )  { 
return   height  ; 
} 





public   final   void   setNodeHeight  (  double   value  )  { 
if  (  value  <  0  )  { 
height  =  -  value  ; 
}  else  { 
height  =  value  ; 
} 
} 





public   final   void   setNodeHeight  (  double   value  ,  boolean   adjustChildBranchLengths  )  { 
if  (  value  <  0  )  { 
height  =  -  value  ; 
}  else  { 
height  =  value  ; 
} 
if  (  adjustChildBranchLengths  &&  child  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  child  .  length  ;  i  ++  )  { 
child  [  i  ]  .  setBranchLength  (  height  -  child  [  i  ]  .  getNodeHeight  (  )  )  ; 
} 
} 
} 

public   void   setNumber  (  int   n  )  { 
number  =  n  ; 
} 

public   int   getNumber  (  )  { 
return   number  ; 
} 








public   Node   getChild  (  int   n  )  { 
return   child  [  n  ]  ; 
} 







public   void   setChild  (  int   n  ,  Node   node  )  { 
child  [  n  ]  =  node  ; 
child  [  n  ]  .  setParent  (  this  )  ; 
} 






public   boolean   hasChildren  (  )  { 
return  !  isLeaf  (  )  ; 
} 






public   boolean   isLeaf  (  )  { 
return  (  getChildCount  (  )  ==  0  )  ; 
} 






public   boolean   isRoot  (  )  { 
if  (  parent  ==  null  )  { 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 






public   void   addChild  (  Node   n  )  { 
insertChild  (  n  ,  getChildCount  (  )  )  ; 
} 







public   void   insertChild  (  Node   n  ,  int   pos  )  { 
int   numChildren  =  getChildCount  (  )  ; 
Node  [  ]  newChild  =  new   Node  [  numChildren  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  pos  ;  i  ++  )  { 
newChild  [  i  ]  =  child  [  i  ]  ; 
} 
newChild  [  pos  ]  =  n  ; 
for  (  int   i  =  pos  ;  i  <  numChildren  ;  i  ++  )  { 
newChild  [  i  +  1  ]  =  child  [  i  ]  ; 
} 
child  =  newChild  ; 
n  .  setParent  (  this  )  ; 
} 






public   Node   removeChild  (  int   n  )  { 
int   numChildren  =  getChildCount  (  )  ; 
if  (  n  >=  numChildren  )  { 
throw   new   IllegalArgumentException  (  "Nonexistent child"  )  ; 
} 
Node  [  ]  newChild  =  new   Node  [  numChildren  -  1  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
newChild  [  i  ]  =  child  [  i  ]  ; 
} 
for  (  int   i  =  n  ;  i  <  numChildren  -  1  ;  i  ++  )  { 
newChild  [  i  ]  =  child  [  i  +  1  ]  ; 
} 
Node   removed  =  child  [  n  ]  ; 
removed  .  setParent  (  null  )  ; 
child  =  newChild  ; 
return   removed  ; 
} 





public   void   lengths2HeightsContemp  (  )  { 
double   largestHeight  =  0.0  ; 
if  (  !  isLeaf  (  )  )  { 
for  (  int   i  =  0  ;  i  <  getChildCount  (  )  ;  i  ++  )  { 
NodeUtils  .  lengths2Heights  (  getChild  (  i  )  )  ; 
double   newHeight  =  getChild  (  i  )  .  getNodeHeight  (  )  +  getChild  (  i  )  .  getBranchLength  (  )  ; 
if  (  newHeight  >  largestHeight  )  { 
largestHeight  =  newHeight  ; 
} 
} 
} 
setNodeHeight  (  largestHeight  )  ; 
} 




public   final   int   getChildCount  (  )  { 
if  (  child  ==  null  )  return   0  ; 
return   child  .  length  ; 
} 
} 

