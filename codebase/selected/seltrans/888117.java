package   com  .  lemu  .  music  .  morph  .  transearch  ; 

import   jm  .  music  .  tools  .  Mod  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   com  .  lemu  .  music  .  LPart  ; 
import   ren  .  gui  .  ParameterMap  ; 
import   ren  .  io  .  Domable  ; 
import   ren  .  io  .  Domc  ; 
import   ren  .  util  .  PO  ; 
import   ren  .  util  .  RMath  ; 
import   ai  .  MTConverter  ; 















public   abstract   class   TransSearch   implements   Domable  { 

public   int   MIDV  ; 

protected   ParameterMap   dev  ; 

protected   ParameterMap   devDec  ; 

protected   boolean   bypass  =  false  ; 

protected   ParameterMap   traspee  ; 

protected   ParameterMap   trackConsistence  ; 

protected   Metric   metric  =  new   Metric  (  )  ; 

protected   MTConverter   mtconv  =  new   MTConverter  (  )  ; 

protected   double  [  ]  w  ; 

private   double  [  ]  [  ]  wi  ; 

protected   LPart  [  ]  op  ; 

protected   ParameterMap  [  ]  co  ; 

protected   ParameterMap  [  ]  otherParams  ; 

private   int   rsel  =  0  ; 

private   boolean   VB  =  false  ; 

public   TransSearch  (  )  { 
super  (  )  ; 
bypass  =  false  ; 
this  .  initParams  (  )  ; 
} 









public   abstract   int   opn  (  )  ; 














protected   LPart   findClosest  (  int   steps  ,  LPart   f  ,  LPart   t  ,  double  [  ]  glopar  )  throws   InterruptedException  { 
if  (  Thread  .  interrupted  (  )  )  throw   new   InterruptedException  (  )  ; 
if  (  w  .  length  !=  op  .  length  )  { 
try  { 
Exception   e  =  new   Exception  (  "weights and "  +  "options must me the same length"  )  ; 
e  .  fillInStackTrace  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
wi  =  new   double  [  opn  (  )  ]  [  5  ]  ; 
for  (  int   i  =  0  ;  i  <  opn  (  )  ;  i  ++  )  { 
if  (  i  ==  this  .  MIDV  )  { 
wi  [  i  ]  [  0  ]  =  (  1  -  (  1  -  w  [  i  ]  )  *  costOfIndex  (  i  )  *  glopar  [  2  ]  )  ; 
}  else  { 
wi  [  i  ]  [  0  ]  =  (  1  -  (  1  -  w  [  i  ]  )  *  costOfIndex  (  i  )  )  ; 
} 
wi  [  i  ]  [  1  ]  =  i  ; 
} 
double   midw  =  wi  [  MIDV  ]  [  0  ]  ; 
quickSort  (  wi  ,  0  ,  wi  .  length  -  1  ,  0  )  ; 
if  (  wi  [  0  ]  [  0  ]  ==  midw  )  { 
int   fwi  =  (  int  )  wi  [  0  ]  [  1  ]  ; 
for  (  int   i  =  0  ;  i  <  wi  .  length  ;  i  ++  )  { 
if  (  wi  [  i  ]  [  1  ]  ==  MIDV  )  { 
wi  [  i  ]  [  1  ]  =  fwi  ; 
wi  [  0  ]  [  1  ]  =  MIDV  ; 
break  ; 
} 
} 
} 
int   sel  =  0  ; 
if  (  this  .  dev  .  getValue  (  )  >  0  )  { 
PO  .  p  (  "dev > 0 = "  +  dev  .  getValue  (  )  )  ; 
sel  =  (  int  )  (  (  this  .  dev  .  getValue  (  )  *  glopar  [  0  ]  )  -  (  steps  -  1  )  *  (  this  .  devDec  .  getValue  (  )  *  glopar  [  1  ]  )  )  ; 
sel  =  Math  .  max  (  Math  .  min  (  sel  ,  wi  .  length  -  1  )  ,  0  )  ; 
if  (  sel  >=  wi  .  length  )  { 
PO  .  p  (  this  .  getType  (  )  )  ; 
PO  .  p  (  "out of wi, which = "  ,  wi  )  ; 
} 
}  else  { 
double   targdif  =  (  midw  -  wi  [  0  ]  [  0  ]  )  *  (  1  -  Math  .  min  (  this  .  traspee  .  getValue  (  )  *  glopar  [  3  ]  ,  1.0  )  )  +  wi  [  0  ]  [  0  ]  ; 
targdif  =  (  midw  -  (  midw  -  targdif  )  *  (  steps  +  1  )  )  ; 
targdif  =  RMath  .  boundHard  (  targdif  ,  wi  [  0  ]  [  0  ]  ,  midw  )  ; 
double   ditadi  =  -  1  ; 
sel  =  selectMorph  (  wi  ,  midw  ,  targdif  ,  t  ,  f  )  ; 
} 
rsel  =  (  int  )  wi  [  sel  ]  [  1  ]  ; 
quickSort  (  wi  ,  0  ,  wi  .  length  -  1  ,  4  )  ; 
if  (  this  .  getClass  (  )  .  getName  (  )  .  equalsIgnoreCase  (  "keymosearch"  )  )  { 
System  .  out  .  print  (  "wi = "  +  "\n {"  )  ; 
for  (  int   i  =  0  ;  i  <  wi  .  length  ;  i  ++  )  { 
System  .  out  .  print  (  " "  +  i  +  "["  )  ; 
for  (  int   j  =  0  ;  j  <  wi  [  i  ]  .  length  ;  j  ++  )  { 
System  .  out  .  print  (  ","  +  wi  [  i  ]  [  j  ]  +  " "  )  ; 
} 
System  .  out  .  print  (  "],\n"  )  ; 
PO  .  p  (  "op root = "  +  op  [  (  int  )  wi  [  i  ]  [  1  ]  ]  .  getTonalManager  (  )  .  getRoot  (  )  +  " scale = "  +  op  [  (  int  )  wi  [  i  ]  [  1  ]  ]  .  getTonalManager  (  )  .  getScaleName  (  )  )  ; 
} 
System  .  out  .  print  (  "}"  )  ; 
}  else   if  (  this  .  getType  (  )  .  equalsIgnoreCase  (  "add remove"  )  )  { 
} 
if  (  VB  )  { 
PO  .  p  (  "\n\ntransearch "  +  this  .  getType  (  )  )  ; 
PO  .  p  (  " selected = "  +  rsel  +  " .  mid = "  +  this  .  MIDV  +  "  result = "  )  ; 
PO  .  p  (  "   "  ,  op  [  rsel  ]  .  getPart  (  )  ,  2  )  ; 
} 
return   op  [  rsel  ]  ; 
} 

























private   int   selectMorph  (  double  [  ]  [  ]  wi  ,  double   midw  ,  double   targdif  ,  LPart   t  ,  LPart   f  )  { 
double  [  ]  min  =  new   double  [  2  ]  ; 
min  [  0  ]  =  Double  .  MAX_VALUE  ; 
min  [  1  ]  =  -  1  ; 
targdif  =  situationNowhereCheck  (  wi  ,  targdif  ,  midw  )  ; 
for  (  int   i  =  0  ;  i  <  wi  .  length  ;  i  ++  )  { 
wi  [  i  ]  [  2  ]  =  miss  (  wi  [  i  ]  [  0  ]  ,  targdif  ,  midw  ,  wi  [  0  ]  [  0  ]  )  ; 
wi  [  i  ]  [  3  ]  =  differenceFunction  (  op  [  (  int  )  wi  [  i  ]  [  1  ]  ]  ,  f  )  ; 
wi  [  i  ]  [  4  ]  =  wi  [  i  ]  [  2  ]  *  (  1  -  this  .  trackConsistence  .  getValue  (  )  )  +  wi  [  i  ]  [  3  ]  *  (  this  .  trackConsistence  .  getValue  (  )  )  ; 
if  (  wi  [  i  ]  [  4  ]  <  min  [  0  ]  )  { 
min  [  0  ]  =  wi  [  i  ]  [  4  ]  ; 
min  [  1  ]  =  i  ; 
} 
} 
return  (  int  )  min  [  1  ]  ; 
} 












protected   double   situationNowhereCheck  (  double  [  ]  [  ]  wi2  ,  double   targdif  ,  double   midw  )  { 
return   targdif  ; 
} 









private   double   cons  (  double   mds  ,  double   mdt  )  { 
double   co  =  (  mds  )  +  (  mdt  )  ; 
co  =  co  /  2.0  ; 
return   co  ; 
} 










private   double   miss  (  double   m  ,  double   t  ,  double   f  ,  double   lop  )  { 
double   miss  =  Math  .  abs  (  m  -  t  )  ; 
double   deno  =  (  Math  .  max  (  t  -  lop  ,  f  -  t  )  )  ; 
if  (  deno  ==  0  &&  miss  ==  0  )  { 
}  else  { 
miss  =  miss  /  deno  ; 
} 
if  (  miss  >  1.0  )  { 
miss  =  1.0  ; 
} 
return   miss  ; 
} 






public   boolean   isChanged  (  )  { 
if  (  MIDV  ==  rsel  )  return   false  ;  else   return   true  ; 
} 


protected   double   costOfIndex  (  int   i  )  { 
return   co  [  i  ]  .  getValue  (  )  ; 
} 










protected   void   resetOps  (  LPart   f  )  { 
Mod  .  quickSort  (  f  .  getPart  (  )  )  ; 
if  (  op  .  length  <  opn  (  )  )  { 
op  =  new   LPart  [  opn  (  )  +  10  ]  ; 
w  =  new   double  [  opn  (  )  +  10  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  opn  (  )  ;  i  ++  )  { 
op  [  i  ]  =  f  .  copy  (  )  ; 
w  [  i  ]  =  Double  .  NaN  ; 
} 
} 








protected   void   initParams  (  )  { 
initOPN  (  )  ; 
op  =  new   LPart  [  this  .  opn  (  )  ]  ; 
w  =  new   double  [  opn  (  )  ]  ; 
this  .  dev  =  (  new   ParameterMap  (  )  )  .  construct  (  0  ,  opn  (  )  ,  0  ,  opn  (  )  ,  0.0  ,  "deviation"  )  ; 
this  .  devDec  =  (  new   ParameterMap  (  )  )  .  construct  (  0  ,  100  ,  0  ,  1.0  ,  0.5  ,  "deviation decay"  )  ; 
this  .  traspee  =  (  new   ParameterMap  (  )  )  .  construct  (  0  ,  100  ,  0  ,  1.0  ,  0.5  ,  "transform spd"  )  ; 
this  .  trackConsistence  =  (  new   ParameterMap  (  )  )  .  construct  (  0  ,  100  ,  0  ,  1.0  ,  0.5  ,  "tra v cons"  )  ; 
initCostParams  (  opn  (  )  )  ; 
setInitialCostValues  (  )  ; 
} 

protected   abstract   void   initOPN  (  )  ; 

protected   void   initCostParams  (  int   num  )  { 
co  =  new   ParameterMap  [  num  ]  ; 
for  (  int   i  =  0  ;  i  <  num  ;  i  ++  )  { 
co  [  i  ]  =  new   ParameterMap  (  )  ; 
co  [  i  ]  .  construct  (  0  ,  100  ,  0  ,  1  ,  1.0  )  ; 
} 
} 




protected   abstract   void   setInitialCostValues  (  )  ; 






public   ParameterMap  [  ]  getCostParams  (  )  { 
return   this  .  co  ; 
} 

public   ParameterMap  [  ]  getOtherParams  (  )  { 
return   this  .  otherParams  ; 
} 









public   abstract   double   differenceFunction  (  LPart   f  ,  LPart   t  )  ; 

public   LPart   find  (  final   LPart   f  ,  final   LPart   t  ,  int   steps  ,  double  [  ]  glopar  )  throws   InterruptedException  { 
if  (  bypass  ==  true  )  { 
this  .  rsel  =  MIDV  ; 
return   f  .  copy  (  )  ; 
}  else   this  .  findb  (  f  ,  t  ,  steps  )  ; 
return   this  .  findClosest  (  steps  ,  f  ,  t  ,  glopar  )  ; 
} 













protected   abstract   void   findb  (  final   LPart   f  ,  final   LPart   t  ,  int   steps  )  throws   InterruptedException  ; 

public   void   checkError  (  LPart   f  ,  LPart   t  )  { 
if  (  f  .  size  (  )  ==  0  ||  t  .  size  (  )  ==  0  )  return  ; 
try  { 
if  (  f  .  getPart  (  )  .  getPhrase  (  f  .  size  (  )  -  1  )  .  getStartTime  (  )  >=  f  .  getScope  (  )  .  getValue  (  )  ||  t  .  getPart  (  )  .  getPhrase  (  t  .  size  (  )  -  1  )  .  getStartTime  (  )  >=  t  .  getScope  (  )  .  getValue  (  )  )  { 
PO  .  p  (  "f =  "  ,  f  .  getPart  (  )  ,  1  )  ; 
PO  .  p  (  "t = "  ,  t  .  getPart  (  )  ,  1  )  ; 
Exception   e  =  new   Exception  (  "note phrases within f and t must"  +  " be within bounds of scope,  f "  +  f  .  getScope  (  )  .  getValue  (  )  +  "  t "  +  t  .  getScope  (  )  .  getValue  (  )  )  ; 
e  .  fillInStackTrace  (  )  ; 
throw   e  ; 
} 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 






protected   double  [  ]  initVAL  (  int   ex  )  { 
double  [  ]  val  =  new   double  [  ex  *  2  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  val  .  length  ;  i  ++  )  { 
val  [  i  ]  =  i  -  ex  ; 
} 
return   val  ; 
} 

private   static   void   quickSort  (  double  [  ]  [  ]  wi  ,  int   lowIndex  ,  int   highIndex  ,  int   sobi  )  { 
int   lowToHighIndex  ,  highToLowIndex  ,  pivotIndex  ; 
double   pivotValue  ,  lowToHighValue  ,  highToLowValue  ; 
double  [  ]  parking  ; 
int   newLowIndex  ,  newHighIndex  ,  compareResult  ; 
lowToHighIndex  =  lowIndex  ; 
highToLowIndex  =  highIndex  ; 
pivotIndex  =  (  lowToHighIndex  +  highToLowIndex  )  /  2  ; 
pivotValue  =  wi  [  pivotIndex  ]  [  sobi  ]  ; 
newLowIndex  =  highIndex  +  1  ; 
newHighIndex  =  lowIndex  -  1  ; 
while  (  (  newHighIndex  +  1  )  <  newLowIndex  )  { 
lowToHighValue  =  wi  [  lowToHighIndex  ]  [  sobi  ]  ; 
while  (  lowToHighIndex  <  newLowIndex  &&  lowToHighValue  <  pivotValue  )  { 
newHighIndex  =  lowToHighIndex  ; 
lowToHighIndex  ++  ; 
lowToHighValue  =  wi  [  lowToHighIndex  ]  [  sobi  ]  ; 
} 
highToLowValue  =  wi  [  highToLowIndex  ]  [  sobi  ]  ; 
while  (  newHighIndex  <=  highToLowIndex  &&  highToLowValue  >  pivotValue  )  { 
newLowIndex  =  highToLowIndex  ; 
highToLowIndex  --  ; 
highToLowValue  =  wi  [  highToLowIndex  ]  [  sobi  ]  ; 
} 
if  (  lowToHighIndex  ==  highToLowIndex  )  { 
newHighIndex  =  lowToHighIndex  ; 
}  else   if  (  lowToHighIndex  <  highToLowIndex  )  { 
if  (  lowToHighValue  >=  highToLowValue  )  { 
parking  =  wi  [  lowToHighIndex  ]  ; 
wi  [  lowToHighIndex  ]  =  wi  [  highToLowIndex  ]  ; 
wi  [  highToLowIndex  ]  =  parking  ; 
newLowIndex  =  highToLowIndex  ; 
newHighIndex  =  lowToHighIndex  ; 
lowToHighIndex  ++  ; 
highToLowIndex  --  ; 
} 
} 
} 
if  (  lowIndex  <  newHighIndex  )  { 
quickSort  (  wi  ,  lowIndex  ,  newHighIndex  ,  sobi  )  ; 
} 
if  (  newLowIndex  <  highIndex  )  { 
quickSort  (  wi  ,  newLowIndex  ,  highIndex  ,  sobi  )  ; 
} 
} 

public   abstract   String   getType  (  )  ; 

public   void   dload  (  Element   e  )  { 
if  (  e  .  hasAttribute  (  "dev"  )  )  this  .  dev  .  setValue  (  e  .  getAttribute  (  "dev"  )  )  ; 
if  (  e  .  hasAttribute  (  "devDec"  )  )  this  .  devDec  .  setValue  (  e  .  getAttribute  (  "devDec"  )  )  ; 
if  (  e  .  hasAttribute  (  "traspee"  )  )  this  .  traspee  .  setValue  (  e  .  getAttribute  (  "traspee"  )  )  ; 
if  (  e  .  hasAttribute  (  BYP  )  )  this  .  bypass  =  Boolean  .  valueOf  (  e  .  getAttribute  (  BYP  )  )  .  booleanValue  (  )  ; 
if  (  e  .  hasAttribute  (  TRACON  )  )  this  .  trackConsistence  .  setValue  (  e  .  getAttribute  (  TRACON  )  )  ; 
NodeList   nl  =  e  .  getElementsByTagName  (  "cost"  )  ; 
for  (  int   i  =  0  ;  i  <  nl  .  getLength  (  )  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  co  .  length  ;  j  ++  )  { 
if  (  (  (  Element  )  nl  .  item  (  i  )  )  .  getAttribute  (  ParameterMap  .  PARAM_NAME  )  .  equalsIgnoreCase  (  co  [  j  ]  .  getName  (  )  )  )  { 
co  [  j  ]  .  setClosestValue  (  Double  .  parseDouble  (  (  (  Element  )  nl  .  item  (  i  )  )  .  getAttribute  (  ParameterMap  .  VALUE  )  )  )  ; 
} 
} 
} 
} 

private   static   final   String   BYP  =  "bypass"  ; 

private   static   final   String   TRACON  =  "tracon"  ; 

public   void   dsave  (  Element   e  )  { 
if  (  this  .  getType  (  )  ==  null  )  { 
PO  .  p  (  "getType() method needs to be properly filled in for the "  +  "subclass of transearch "  +  this  .  getClass  (  )  .  getName  (  )  )  ; 
return  ; 
} 
e  .  setAttribute  (  "type"  ,  this  .  getType  (  )  )  ; 
e  .  setAttribute  (  "dev"  ,  dev  .  getValueStr  (  )  )  ; 
e  .  setAttribute  (  "devDec"  ,  devDec  .  getValueStr  (  )  )  ; 
e  .  setAttribute  (  "traspee"  ,  traspee  .  getValueStr  (  )  )  ; 
e  .  setAttribute  (  "bypass"  ,  String  .  valueOf  (  bypass  )  )  ; 
e  .  setAttribute  (  TRACON  ,  this  .  trackConsistence  .  getValueStr  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  this  .  co  .  length  ;  i  ++  )  { 
e  .  appendChild  (  Domc  .  sa  (  co  [  i  ]  ,  "cost"  ,  e  .  getOwnerDocument  (  )  )  )  ; 
} 
} 

public   ParameterMap   getDev  (  )  { 
return   dev  ; 
} 

public   ParameterMap   getDevDec  (  )  { 
return   devDec  ; 
} 

public   ParameterMap   getTransformSpeed  (  )  { 
return   this  .  traspee  ; 
} 

public   boolean   getBypass  (  )  { 
return   this  .  bypass  ; 
} 

public   ParameterMap   getTrackVsConsistence  (  )  { 
return   this  .  trackConsistence  ; 
} 

public   void   setBypass  (  boolean   b  )  { 
this  .  bypass  =  b  ; 
} 

public   void   printW  (  )  { 
PO  .  p  (  "wi = \n"  ,  wi  )  ; 
} 

public   void   printSWO  (  )  { 
PO  .  p  (  "---- "  +  this  .  getType  (  )  +  " -------\n"  )  ; 
PO  .  p  (  " sel = "  +  rsel  +  " midv = "  +  this  .  MIDV  )  ; 
PO  .  p  (  "op = \n"  ,  op  )  ; 
PO  .  p  (  " wi = \n"  ,  wi  )  ; 
} 

public   void   config  (  int   i  )  { 
for  (  int   j  =  0  ;  j  <  co  .  length  ;  j  ++  )  { 
co  [  j  ]  .  setValue  (  1.0  )  ; 
} 
} 
} 

