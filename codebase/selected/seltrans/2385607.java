package   net  .  maizegenetics  .  pal  .  tree  ; 

import   net  .  maizegenetics  .  pal  .  ids  .  Identifier  ; 
import   net  .  maizegenetics  .  pal  .  ids  .  LabelMapping  ; 
import   net  .  maizegenetics  .  pal  .  util  .  BranchLimits  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Hashtable  ; 










public   class   SimpleNode   implements   AttributeNode  { 


private   Node   parent  ; 


private   int   number  ; 


private   byte  [  ]  sequence  ; 


private   double   length  ; 


private   double   lengthSE  ; 


private   double   height  ; 


private   Identifier   identifier  ; 


private   Hashtable   attributes  =  null  ; 

private   Node  [  ]  child  ; 

static   final   long   serialVersionUID  =  3472432765038556717L  ; 


private   void   writeObject  (  java  .  io  .  ObjectOutputStream   out  )  throws   java  .  io  .  IOException  { 
out  .  writeByte  (  3  )  ; 
out  .  writeObject  (  parent  )  ; 
out  .  writeInt  (  number  )  ; 
out  .  writeObject  (  sequence  )  ; 
out  .  writeDouble  (  length  )  ; 
out  .  writeDouble  (  lengthSE  )  ; 
out  .  writeDouble  (  height  )  ; 
out  .  writeObject  (  identifier  )  ; 
out  .  writeObject  (  child  )  ; 
out  .  writeObject  (  attributes  )  ; 
} 

private   void   readObject  (  java  .  io  .  ObjectInputStream   in  )  throws   IOException  ,  ClassNotFoundException  { 
byte   version  =  in  .  readByte  (  )  ; 
switch  (  version  )  { 
case   1  : 
{ 
parent  =  (  Node  )  in  .  readObject  (  )  ; 
number  =  in  .  readInt  (  )  ; 
sequence  =  (  byte  [  ]  )  in  .  readObject  (  )  ; 
Object   partial  =  (  double  [  ]  [  ]  [  ]  )  in  .  readObject  (  )  ; 
length  =  in  .  readDouble  (  )  ; 
lengthSE  =  in  .  readDouble  (  )  ; 
height  =  in  .  readDouble  (  )  ; 
in  .  readDouble  (  )  ; 
identifier  =  (  Identifier  )  in  .  readObject  (  )  ; 
child  =  (  Node  [  ]  )  in  .  readObject  (  )  ; 
break  ; 
} 
case   2  : 
{ 
parent  =  (  Node  )  in  .  readObject  (  )  ; 
number  =  in  .  readInt  (  )  ; 
sequence  =  (  byte  [  ]  )  in  .  readObject  (  )  ; 
length  =  in  .  readDouble  (  )  ; 
lengthSE  =  in  .  readDouble  (  )  ; 
height  =  in  .  readDouble  (  )  ; 
identifier  =  (  Identifier  )  in  .  readObject  (  )  ; 
child  =  (  Node  [  ]  )  in  .  readObject  (  )  ; 
break  ; 
} 
default  : 
{ 
parent  =  (  Node  )  in  .  readObject  (  )  ; 
number  =  in  .  readInt  (  )  ; 
sequence  =  (  byte  [  ]  )  in  .  readObject  (  )  ; 
length  =  in  .  readDouble  (  )  ; 
lengthSE  =  in  .  readDouble  (  )  ; 
height  =  in  .  readDouble  (  )  ; 
identifier  =  (  Identifier  )  in  .  readObject  (  )  ; 
child  =  (  Node  [  ]  )  in  .  readObject  (  )  ; 
attributes  =  (  Hashtable  )  in  .  readObject  (  )  ; 
} 
} 
} 


public   SimpleNode  (  )  { 
parent  =  null  ; 
child  =  null  ; 
length  =  0.0  ; 
lengthSE  =  0.0  ; 
height  =  0.0  ; 
identifier  =  Identifier  .  ANONYMOUS  ; 
number  =  0  ; 
sequence  =  null  ; 
} 

public   SimpleNode  (  String   name  ,  double   branchLength  )  { 
this  (  )  ; 
identifier  =  new   Identifier  (  name  )  ; 
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

protected   SimpleNode  (  Node  [  ]  children  )  { 
this  (  children  ,  BranchLimits  .  MINARC  )  ; 
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
identifier  =  Identifier  .  ANONYMOUS  ; 
number  =  0  ; 
sequence  =  null  ; 
} 

public   SimpleNode  (  Node   n  ,  boolean   keepIds  )  { 
init  (  n  ,  keepIds  )  ; 
for  (  int   i  =  0  ;  i  <  n  .  getChildCount  (  )  ;  i  ++  )  { 
addChild  (  new   SimpleNode  (  n  .  getChild  (  i  )  ,  keepIds  )  )  ; 
} 
} 

public   SimpleNode  (  Node   n  ,  LabelMapping   lm  )  { 
init  (  n  ,  true  ,  lm  )  ; 
for  (  int   i  =  0  ;  i  <  n  .  getChildCount  (  )  ;  i  ++  )  { 
addChild  (  new   SimpleNode  (  n  .  getChild  (  i  )  ,  lm  )  )  ; 
} 
} 

protected   void   init  (  Node   n  )  { 
init  (  n  ,  true  )  ; 
} 





protected   void   init  (  Node   n  ,  boolean   keepId  )  { 
init  (  n  ,  keepId  ,  null  )  ; 
} 






protected   void   init  (  Node   n  ,  boolean   keepId  ,  LabelMapping   lm  )  { 
parent  =  null  ; 
length  =  n  .  getBranchLength  (  )  ; 
lengthSE  =  n  .  getBranchLengthSE  (  )  ; 
height  =  n  .  getNodeHeight  (  )  ; 
if  (  keepId  )  { 
if  (  lm  !=  null  )  { 
identifier  =  lm  .  getLabelIdentifier  (  n  .  getIdentifier  (  )  )  ; 
}  else  { 
identifier  =  n  .  getIdentifier  (  )  ; 
} 
}  else  { 
identifier  =  Identifier  .  ANONYMOUS  ; 
} 
number  =  n  .  getNumber  (  )  ; 
sequence  =  n  .  getSequence  (  )  ; 
if  (  n   instanceof   AttributeNode  )  { 
AttributeNode   attNode  =  (  AttributeNode  )  n  ; 
Enumeration   e  =  attNode  .  getAttributeNames  (  )  ; 
while  (  (  e  !=  null  )  &&  e  .  hasMoreElements  (  )  )  { 
String   name  =  (  String  )  e  .  nextElement  (  )  ; 
setAttribute  (  name  ,  attNode  .  getAttribute  (  name  )  )  ; 
} 
} 
child  =  null  ; 
} 




public   final   Node   getParent  (  )  { 
return   parent  ; 
} 


public   void   setParent  (  Node   node  )  { 
parent  =  node  ; 
} 




public   final   void   removeParent  (  )  { 
parent  =  null  ; 
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




public   final   Identifier   getIdentifier  (  )  { 
return   identifier  ; 
} 




public   final   void   setIdentifier  (  Identifier   id  )  { 
identifier  =  id  ; 
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






public   final   void   setAttribute  (  String   name  ,  Object   value  )  { 
if  (  attributes  ==  null  )  attributes  =  new   Hashtable  (  )  ; 
attributes  .  put  (  name  ,  value  )  ; 
} 





public   final   Object   getAttribute  (  String   name  )  { 
if  (  attributes  ==  null  )  return   null  ; 
return   attributes  .  get  (  name  )  ; 
} 





public   final   Enumeration   getAttributeNames  (  )  { 
if  (  attributes  ==  null  )  return   null  ; 
return   attributes  .  keys  (  )  ; 
} 




public   final   int   getChildCount  (  )  { 
if  (  child  ==  null  )  return   0  ; 
return   child  .  length  ; 
} 

public   String   toString  (  )  { 
StringWriter   sw  =  new   StringWriter  (  )  ; 
NodeUtils  .  printNH  (  new   PrintWriter  (  sw  )  ,  this  ,  true  ,  false  ,  0  ,  false  )  ; 
return   sw  .  toString  (  )  ; 
} 
} 

