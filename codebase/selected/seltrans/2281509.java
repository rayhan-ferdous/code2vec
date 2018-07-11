package   org  .  openscience  .  cdk  .  isomorphism  .  matchers  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IAtom  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IAtomContainer  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IBond  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IBond  .  Order  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IChemObjectChangeEvent  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IElectronContainer  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  ILonePair  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  ISingleElectron  ; 
import   org  .  openscience  .  cdk  .  interfaces  .  IStereoElement  ; 





public   class   QueryAtomContainer   extends   QueryChemObject   implements   IQueryAtomContainer  { 

private   static   final   long   serialVersionUID  =  -  1876912362585898476L  ; 

public   String   toString  (  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
s  .  append  (  "QueryAtomContainer("  )  ; 
s  .  append  (  this  .  hashCode  (  )  +  ", "  )  ; 
s  .  append  (  "#A:"  +  getAtomCount  (  )  +  ", "  )  ; 
s  .  append  (  "#EC:"  +  getElectronContainerCount  (  )  +  ", "  )  ; 
for  (  int   i  =  0  ;  i  <  getAtomCount  (  )  ;  i  ++  )  { 
s  .  append  (  getAtom  (  i  )  .  toString  (  )  +  ", "  )  ; 
} 
for  (  int   i  =  0  ;  i  <  getBondCount  (  )  ;  i  ++  )  { 
s  .  append  (  getBond  (  i  )  .  toString  (  )  +  ", "  )  ; 
} 
for  (  int   i  =  0  ;  i  <  getLonePairCount  (  )  ;  i  ++  )  { 
s  .  append  (  getLonePair  (  i  )  .  toString  (  )  +  ", "  )  ; 
} 
for  (  int   i  =  0  ;  i  <  getSingleElectronCount  (  )  ;  i  ++  )  { 
s  .  append  (  getSingleElectron  (  i  )  .  toString  (  )  +  ", "  )  ; 
} 
s  .  append  (  ")"  )  ; 
return   s  .  toString  (  )  ; 
} 




protected   int   atomCount  ; 




protected   int   bondCount  ; 




protected   int   lonePairCount  ; 




protected   int   singleElectronCount  ; 





protected   int   growArraySize  =  10  ; 




protected   IAtom  [  ]  atoms  ; 




protected   IBond  [  ]  bonds  ; 




protected   ILonePair  [  ]  lonePairs  ; 




protected   ISingleElectron  [  ]  singleElectrons  ; 




protected   List  <  IStereoElement  >  stereoElements  ; 




public   QueryAtomContainer  (  )  { 
this  (  10  ,  10  ,  0  ,  0  )  ; 
} 








public   QueryAtomContainer  (  IAtomContainer   container  )  { 
this  .  atomCount  =  container  .  getAtomCount  (  )  ; 
this  .  bondCount  =  container  .  getBondCount  (  )  ; 
this  .  lonePairCount  =  container  .  getLonePairCount  (  )  ; 
this  .  singleElectronCount  =  container  .  getSingleElectronCount  (  )  ; 
this  .  atoms  =  new   IAtom  [  this  .  atomCount  ]  ; 
this  .  bonds  =  new   IBond  [  this  .  bondCount  ]  ; 
this  .  lonePairs  =  new   ILonePair  [  this  .  lonePairCount  ]  ; 
this  .  singleElectrons  =  new   ISingleElectron  [  this  .  singleElectronCount  ]  ; 
stereoElements  =  new   ArrayList  <  IStereoElement  >  (  atomCount  /  2  )  ; 
for  (  int   f  =  0  ;  f  <  container  .  getAtomCount  (  )  ;  f  ++  )  { 
atoms  [  f  ]  =  container  .  getAtom  (  f  )  ; 
container  .  getAtom  (  f  )  .  addListener  (  this  )  ; 
} 
for  (  int   f  =  0  ;  f  <  this  .  bondCount  ;  f  ++  )  { 
bonds  [  f  ]  =  container  .  getBond  (  f  )  ; 
container  .  getBond  (  f  )  .  addListener  (  this  )  ; 
} 
for  (  int   f  =  0  ;  f  <  this  .  lonePairCount  ;  f  ++  )  { 
lonePairs  [  f  ]  =  container  .  getLonePair  (  f  )  ; 
container  .  getLonePair  (  f  )  .  addListener  (  this  )  ; 
} 
for  (  int   f  =  0  ;  f  <  this  .  singleElectronCount  ;  f  ++  )  { 
singleElectrons  [  f  ]  =  container  .  getSingleElectron  (  f  )  ; 
container  .  getSingleElectron  (  f  )  .  addListener  (  this  )  ; 
} 
} 












public   QueryAtomContainer  (  int   atomCount  ,  int   bondCount  ,  int   lpCount  ,  int   seCount  )  { 
this  .  atomCount  =  0  ; 
this  .  bondCount  =  0  ; 
this  .  lonePairCount  =  0  ; 
this  .  singleElectronCount  =  0  ; 
atoms  =  new   IAtom  [  atomCount  ]  ; 
bonds  =  new   IBond  [  bondCount  ]  ; 
lonePairs  =  new   ILonePair  [  lpCount  ]  ; 
singleElectrons  =  new   ISingleElectron  [  seCount  ]  ; 
stereoElements  =  new   ArrayList  <  IStereoElement  >  (  atomCount  /  2  )  ; 
} 


public   void   addStereoElement  (  IStereoElement   element  )  { 
stereoElements  .  add  (  element  )  ; 
} 


public   Iterable  <  IStereoElement  >  stereoElements  (  )  { 
return   new   Iterable  <  IStereoElement  >  (  )  { 

public   Iterator  <  IStereoElement  >  iterator  (  )  { 
return   stereoElements  .  iterator  (  )  ; 
} 
}  ; 
} 







public   void   setAtoms  (  IAtom  [  ]  atoms  )  { 
this  .  atoms  =  atoms  ; 
for  (  IAtom   atom  :  atoms  )  { 
atom  .  addListener  (  this  )  ; 
} 
this  .  atomCount  =  atoms  .  length  ; 
notifyChanged  (  )  ; 
} 








public   void   setBonds  (  IBond  [  ]  bonds  )  { 
this  .  bonds  =  bonds  ; 
for  (  IBond   bond  :  bonds  )  { 
bond  .  addListener  (  this  )  ; 
} 
this  .  bondCount  =  bonds  .  length  ; 
} 








public   void   setAtom  (  int   number  ,  IAtom   atom  )  { 
atom  .  addListener  (  this  )  ; 
atoms  [  number  ]  =  atom  ; 
notifyChanged  (  )  ; 
} 










public   IAtom   getAtom  (  int   number  )  { 
return   atoms  [  number  ]  ; 
} 







public   IBond   getBond  (  int   number  )  { 
return   bonds  [  number  ]  ; 
} 







public   ILonePair   getLonePair  (  int   number  )  { 
return   lonePairs  [  number  ]  ; 
} 







public   ISingleElectron   getSingleElectron  (  int   number  )  { 
return   singleElectrons  [  number  ]  ; 
} 






public   Iterable  <  IAtom  >  atoms  (  )  { 
return   new   Iterable  <  IAtom  >  (  )  { 

public   Iterator  <  IAtom  >  iterator  (  )  { 
return   new   AtomIterator  (  )  ; 
} 
}  ; 
} 





private   class   AtomIterator   implements   Iterator  <  IAtom  >  { 

private   int   pointer  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pointer  <  atomCount  ; 
} 

public   IAtom   next  (  )  { 
return   atoms  [  pointer  ++  ]  ; 
} 

public   void   remove  (  )  { 
removeAtom  (  --  pointer  )  ; 
} 
} 






public   Iterable  <  IBond  >  bonds  (  )  { 
return   new   Iterable  <  IBond  >  (  )  { 

public   Iterator  <  IBond  >  iterator  (  )  { 
return   new   BondIterator  (  )  ; 
} 
}  ; 
} 





private   class   BondIterator   implements   Iterator  <  IBond  >  { 

private   int   pointer  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pointer  <  bondCount  ; 
} 

public   IBond   next  (  )  { 
return   bonds  [  pointer  ++  ]  ; 
} 

public   void   remove  (  )  { 
removeBond  (  --  pointer  )  ; 
} 
} 






public   Iterable  <  ILonePair  >  lonePairs  (  )  { 
return   new   Iterable  <  ILonePair  >  (  )  { 

public   Iterator  <  ILonePair  >  iterator  (  )  { 
return   new   LonePairIterator  (  )  ; 
} 
}  ; 
} 





private   class   LonePairIterator   implements   Iterator  <  ILonePair  >  { 

private   int   pointer  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pointer  <  lonePairCount  ; 
} 

public   ILonePair   next  (  )  { 
return   lonePairs  [  pointer  ++  ]  ; 
} 

public   void   remove  (  )  { 
removeLonePair  (  --  pointer  )  ; 
} 
} 






public   Iterable  <  ISingleElectron  >  singleElectrons  (  )  { 
return   new   Iterable  <  ISingleElectron  >  (  )  { 

public   Iterator  <  ISingleElectron  >  iterator  (  )  { 
return   new   SingleElectronIterator  (  )  ; 
} 
}  ; 
} 





private   class   SingleElectronIterator   implements   Iterator  <  ISingleElectron  >  { 

private   int   pointer  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pointer  <  singleElectronCount  ; 
} 

public   ISingleElectron   next  (  )  { 
return   singleElectrons  [  pointer  ++  ]  ; 
} 

public   void   remove  (  )  { 
removeSingleElectron  (  --  pointer  )  ; 
} 
} 






public   Iterable  <  IElectronContainer  >  electronContainers  (  )  { 
return   new   Iterable  <  IElectronContainer  >  (  )  { 

public   Iterator  <  IElectronContainer  >  iterator  (  )  { 
return   new   ElectronContainerIterator  (  )  ; 
} 
}  ; 
} 





private   class   ElectronContainerIterator   implements   Iterator  <  IElectronContainer  >  { 

private   int   pointer  =  0  ; 

public   boolean   hasNext  (  )  { 
return   pointer  <  (  bondCount  +  lonePairCount  +  singleElectronCount  )  ; 
} 

public   IElectronContainer   next  (  )  { 
if  (  pointer  <  bondCount  )  return   bonds  [  pointer  ++  ]  ;  else   if  (  pointer  <  bondCount  +  lonePairCount  )  return   lonePairs  [  (  pointer  ++  )  -  bondCount  ]  ;  else   if  (  pointer  <  bondCount  +  lonePairCount  +  singleElectronCount  )  return   singleElectrons  [  (  pointer  ++  )  -  bondCount  -  lonePairCount  ]  ; 
return   null  ; 
} 

public   void   remove  (  )  { 
if  (  pointer  <=  bondCount  )  removeBond  (  --  pointer  )  ;  else   if  (  pointer  <=  bondCount  +  lonePairCount  )  removeLonePair  (  (  --  pointer  )  -  bondCount  )  ;  else   if  (  pointer  <=  bondCount  +  lonePairCount  +  singleElectronCount  )  removeSingleElectron  (  (  --  pointer  )  -  bondCount  -  lonePairCount  )  ; 
} 
} 






public   IAtom   getFirstAtom  (  )  { 
return   atoms  [  0  ]  ; 
} 






public   IAtom   getLastAtom  (  )  { 
return   getAtomCount  (  )  >  0  ?  (  IAtom  )  atoms  [  getAtomCount  (  )  -  1  ]  :  null  ; 
} 








public   int   getAtomNumber  (  IAtom   atom  )  { 
for  (  int   f  =  0  ;  f  <  atomCount  ;  f  ++  )  { 
if  (  atoms  [  f  ]  ==  atom  )  return   f  ; 
} 
return  -  1  ; 
} 










public   int   getBondNumber  (  IAtom   atom1  ,  IAtom   atom2  )  { 
return  (  getBondNumber  (  getBond  (  atom1  ,  atom2  )  )  )  ; 
} 








public   int   getBondNumber  (  IBond   bond  )  { 
for  (  int   f  =  0  ;  f  <  bondCount  ;  f  ++  )  { 
if  (  bonds  [  f  ]  ==  bond  )  return   f  ; 
} 
return  -  1  ; 
} 








public   int   getLonePairNumber  (  ILonePair   lonePair  )  { 
for  (  int   f  =  0  ;  f  <  lonePairCount  ;  f  ++  )  { 
if  (  lonePairs  [  f  ]  ==  lonePair  )  return   f  ; 
} 
return  -  1  ; 
} 








public   int   getSingleElectronNumber  (  ISingleElectron   singleElectron  )  { 
for  (  int   f  =  0  ;  f  <  singleElectronCount  ;  f  ++  )  { 
if  (  singleElectrons  [  f  ]  ==  singleElectron  )  return   f  ; 
} 
return  -  1  ; 
} 








public   IElectronContainer   getElectronContainer  (  int   number  )  { 
if  (  number  <  this  .  bondCount  )  return   bonds  [  number  ]  ; 
number  -=  this  .  bondCount  ; 
if  (  number  <  this  .  lonePairCount  )  return   lonePairs  [  number  ]  ; 
number  -=  this  .  lonePairCount  ; 
if  (  number  <  this  .  singleElectronCount  )  return   singleElectrons  [  number  ]  ; 
return   null  ; 
} 








public   IBond   getBond  (  IAtom   atom1  ,  IAtom   atom2  )  { 
for  (  int   i  =  0  ;  i  <  getBondCount  (  )  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom1  )  &&  bonds  [  i  ]  .  getConnectedAtom  (  atom1  )  ==  atom2  )  { 
return   bonds  [  i  ]  ; 
} 
} 
return   null  ; 
} 






public   int   getAtomCount  (  )  { 
return   this  .  atomCount  ; 
} 






public   int   getBondCount  (  )  { 
return   this  .  bondCount  ; 
} 






public   int   getLonePairCount  (  )  { 
return   this  .  lonePairCount  ; 
} 






public   int   getSingleElectronCount  (  )  { 
return   this  .  singleElectronCount  ; 
} 






public   int   getElectronContainerCount  (  )  { 
return   this  .  bondCount  +  this  .  lonePairCount  +  this  .  singleElectronCount  ; 
} 







public   List  <  IAtom  >  getConnectedAtomsList  (  IAtom   atom  )  { 
List  <  IAtom  >  atomsList  =  new   ArrayList  <  IAtom  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  atomsList  .  add  (  bonds  [  i  ]  .  getConnectedAtom  (  atom  )  )  ; 
} 
return   atomsList  ; 
} 







public   List  <  IBond  >  getConnectedBondsList  (  IAtom   atom  )  { 
List  <  IBond  >  bondsList  =  new   ArrayList  <  IBond  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  bondsList  .  add  (  bonds  [  i  ]  )  ; 
} 
return   bondsList  ; 
} 










public   List  <  ILonePair  >  getConnectedLonePairsList  (  IAtom   atom  )  { 
List  <  ILonePair  >  lps  =  new   ArrayList  <  ILonePair  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  lonePairCount  ;  i  ++  )  { 
if  (  lonePairs  [  i  ]  .  contains  (  atom  )  )  lps  .  add  (  lonePairs  [  i  ]  )  ; 
} 
return   lps  ; 
} 







public   List  <  ISingleElectron  >  getConnectedSingleElectronsList  (  IAtom   atom  )  { 
List  <  ISingleElectron  >  lps  =  new   ArrayList  <  ISingleElectron  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  singleElectronCount  ;  i  ++  )  { 
if  (  singleElectrons  [  i  ]  .  contains  (  atom  )  )  lps  .  add  (  singleElectrons  [  i  ]  )  ; 
} 
return   lps  ; 
} 







public   List  <  IElectronContainer  >  getConnectedElectronContainersList  (  IAtom   atom  )  { 
List  <  IElectronContainer  >  lps  =  new   ArrayList  <  IElectronContainer  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  lps  .  add  (  bonds  [  i  ]  )  ; 
} 
for  (  int   i  =  0  ;  i  <  lonePairCount  ;  i  ++  )  { 
if  (  lonePairs  [  i  ]  .  contains  (  atom  )  )  lps  .  add  (  lonePairs  [  i  ]  )  ; 
} 
for  (  int   i  =  0  ;  i  <  singleElectronCount  ;  i  ++  )  { 
if  (  singleElectrons  [  i  ]  .  contains  (  atom  )  )  lps  .  add  (  singleElectrons  [  i  ]  )  ; 
} 
return   lps  ; 
} 







public   int   getConnectedAtomsCount  (  IAtom   atom  )  { 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  ++  count  ; 
} 
return   count  ; 
} 







public   int   getConnectedBondsCount  (  IAtom   atom  )  { 
return   getConnectedAtomsCount  (  atom  )  ; 
} 







public   int   getConnectedBondsCount  (  int   atomNumber  )  { 
return   getConnectedAtomsCount  (  atoms  [  atomNumber  ]  )  ; 
} 







public   int   getConnectedLonePairsCount  (  IAtom   atom  )  { 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  lonePairCount  ;  i  ++  )  { 
if  (  lonePairs  [  i  ]  .  contains  (  atom  )  )  ++  count  ; 
} 
return   count  ; 
} 







public   int   getConnectedSingleElectronsCount  (  IAtom   atom  )  { 
int   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  singleElectronCount  ;  i  ++  )  { 
if  (  singleElectrons  [  i  ]  .  contains  (  atom  )  )  ++  count  ; 
} 
return   count  ; 
} 









public   double   getBondOrderSum  (  IAtom   atom  )  { 
double   count  =  0  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  { 
if  (  bonds  [  i  ]  .  getOrder  (  )  ==  IBond  .  Order  .  SINGLE  )  { 
count  +=  1  ; 
}  else   if  (  bonds  [  i  ]  .  getOrder  (  )  ==  IBond  .  Order  .  DOUBLE  )  { 
count  +=  2  ; 
}  else   if  (  bonds  [  i  ]  .  getOrder  (  )  ==  IBond  .  Order  .  TRIPLE  )  { 
count  +=  3  ; 
}  else   if  (  bonds  [  i  ]  .  getOrder  (  )  ==  IBond  .  Order  .  QUADRUPLE  )  { 
count  +=  4  ; 
} 
} 
} 
return   count  ; 
} 








public   Order   getMaximumBondOrder  (  IAtom   atom  )  { 
IBond  .  Order   max  =  IBond  .  Order  .  SINGLE  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  &&  bonds  [  i  ]  .  getOrder  (  )  .  ordinal  (  )  >  max  .  ordinal  (  )  )  { 
max  =  bonds  [  i  ]  .  getOrder  (  )  ; 
} 
} 
return   max  ; 
} 








public   Order   getMinimumBondOrder  (  IAtom   atom  )  { 
IBond  .  Order   min  =  IBond  .  Order  .  QUADRUPLE  ; 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  &&  bonds  [  i  ]  .  getOrder  (  )  .  ordinal  (  )  <  min  .  ordinal  (  )  )  { 
min  =  bonds  [  i  ]  .  getOrder  (  )  ; 
} 
} 
return   min  ; 
} 







public   void   add  (  IAtomContainer   atomContainer  )  { 
if  (  atomContainer   instanceof   QueryAtomContainer  )  { 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getAtomCount  (  )  ;  f  ++  )  { 
if  (  !  contains  (  atomContainer  .  getAtom  (  f  )  )  )  { 
addAtom  (  atomContainer  .  getAtom  (  f  )  )  ; 
} 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getBondCount  (  )  ;  f  ++  )  { 
if  (  !  contains  (  atomContainer  .  getBond  (  f  )  )  )  { 
addBond  (  atomContainer  .  getBond  (  f  )  )  ; 
} 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getLonePairCount  (  )  ;  f  ++  )  { 
if  (  !  contains  (  atomContainer  .  getLonePair  (  f  )  )  )  { 
addLonePair  (  atomContainer  .  getLonePair  (  f  )  )  ; 
} 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getSingleElectronCount  (  )  ;  f  ++  )  { 
if  (  !  contains  (  atomContainer  .  getSingleElectron  (  f  )  )  )  { 
addSingleElectron  (  atomContainer  .  getSingleElectron  (  f  )  )  ; 
} 
} 
notifyChanged  (  )  ; 
}  else  { 
throw   new   IllegalArgumentException  (  "AtomContainer is not of type QueryAtomContainer"  )  ; 
} 
} 






public   void   addAtom  (  IAtom   atom  )  { 
if  (  contains  (  atom  )  )  { 
return  ; 
} 
if  (  atomCount  +  1  >=  atoms  .  length  )  { 
growAtomArray  (  )  ; 
} 
atom  .  addListener  (  this  )  ; 
atoms  [  atomCount  ]  =  atom  ; 
atomCount  ++  ; 
notifyChanged  (  )  ; 
} 






public   void   addBond  (  IBond   bond  )  { 
if  (  bondCount  >=  bonds  .  length  )  growBondArray  (  )  ; 
bonds  [  bondCount  ]  =  bond  ; 
++  bondCount  ; 
notifyChanged  (  )  ; 
} 






public   void   addLonePair  (  ILonePair   lonePair  )  { 
if  (  lonePairCount  >=  lonePairs  .  length  )  growLonePairArray  (  )  ; 
lonePairs  [  lonePairCount  ]  =  lonePair  ; 
++  lonePairCount  ; 
notifyChanged  (  )  ; 
} 






public   void   addSingleElectron  (  ISingleElectron   singleElectron  )  { 
if  (  singleElectronCount  >=  singleElectrons  .  length  )  growSingleElectronArray  (  )  ; 
singleElectrons  [  singleElectronCount  ]  =  singleElectron  ; 
++  singleElectronCount  ; 
notifyChanged  (  )  ; 
} 






public   void   addElectronContainer  (  IElectronContainer   electronContainer  )  { 
if  (  electronContainer   instanceof   IBond  )  this  .  addBond  (  (  IBond  )  electronContainer  )  ; 
if  (  electronContainer   instanceof   ILonePair  )  this  .  addLonePair  (  (  ILonePair  )  electronContainer  )  ; 
if  (  electronContainer   instanceof   ISingleElectron  )  this  .  addSingleElectron  (  (  ISingleElectron  )  electronContainer  )  ; 
} 







public   void   remove  (  IAtomContainer   atomContainer  )  { 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getAtomCount  (  )  ;  f  ++  )  { 
removeAtom  (  atomContainer  .  getAtom  (  f  )  )  ; 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getBondCount  (  )  ;  f  ++  )  { 
removeBond  (  atomContainer  .  getBond  (  f  )  )  ; 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getLonePairCount  (  )  ;  f  ++  )  { 
removeLonePair  (  atomContainer  .  getLonePair  (  f  )  )  ; 
} 
for  (  int   f  =  0  ;  f  <  atomContainer  .  getSingleElectronCount  (  )  ;  f  ++  )  { 
removeSingleElectron  (  atomContainer  .  getSingleElectron  (  f  )  )  ; 
} 
} 








public   void   removeAtom  (  int   position  )  { 
atoms  [  position  ]  .  removeListener  (  this  )  ; 
for  (  int   i  =  position  ;  i  <  atomCount  -  1  ;  i  ++  )  { 
atoms  [  i  ]  =  atoms  [  i  +  1  ]  ; 
} 
atoms  [  atomCount  -  1  ]  =  null  ; 
atomCount  --  ; 
notifyChanged  (  )  ; 
} 








public   void   removeAtom  (  IAtom   atom  )  { 
int   position  =  getAtomNumber  (  atom  )  ; 
if  (  position  !=  -  1  )  { 
removeAtom  (  position  )  ; 
} 
} 






public   IBond   removeBond  (  int   position  )  { 
IBond   bond  =  bonds  [  position  ]  ; 
bond  .  removeListener  (  this  )  ; 
for  (  int   i  =  position  ;  i  <  bondCount  -  1  ;  i  ++  )  { 
bonds  [  i  ]  =  bonds  [  i  +  1  ]  ; 
} 
bonds  [  bondCount  -  1  ]  =  null  ; 
bondCount  --  ; 
notifyChanged  (  )  ; 
return   bond  ; 
} 








public   IBond   removeBond  (  IAtom   atom1  ,  IAtom   atom2  )  { 
int   pos  =  getBondNumber  (  atom1  ,  atom2  )  ; 
IBond   bond  =  null  ; 
if  (  pos  !=  -  1  )  { 
bond  =  bonds  [  pos  ]  ; 
removeBond  (  pos  )  ; 
} 
return   bond  ; 
} 






public   void   removeBond  (  IBond   bond  )  { 
int   pos  =  getBondNumber  (  bond  )  ; 
if  (  pos  !=  -  1  )  removeBond  (  pos  )  ; 
} 






public   ILonePair   removeLonePair  (  int   position  )  { 
ILonePair   lp  =  lonePairs  [  position  ]  ; 
lp  .  removeListener  (  this  )  ; 
for  (  int   i  =  position  ;  i  <  lonePairCount  -  1  ;  i  ++  )  { 
lonePairs  [  i  ]  =  lonePairs  [  i  +  1  ]  ; 
} 
lonePairs  [  lonePairCount  -  1  ]  =  null  ; 
lonePairCount  --  ; 
notifyChanged  (  )  ; 
return   lp  ; 
} 






public   void   removeLonePair  (  ILonePair   lonePair  )  { 
int   pos  =  getLonePairNumber  (  lonePair  )  ; 
if  (  pos  !=  -  1  )  removeLonePair  (  pos  )  ; 
} 






public   ISingleElectron   removeSingleElectron  (  int   position  )  { 
ISingleElectron   se  =  singleElectrons  [  position  ]  ; 
se  .  removeListener  (  this  )  ; 
for  (  int   i  =  position  ;  i  <  singleElectronCount  -  1  ;  i  ++  )  { 
singleElectrons  [  i  ]  =  singleElectrons  [  i  +  1  ]  ; 
} 
singleElectrons  [  singleElectronCount  -  1  ]  =  null  ; 
singleElectronCount  --  ; 
notifyChanged  (  )  ; 
return   se  ; 
} 






public   void   removeSingleElectron  (  ISingleElectron   singleElectron  )  { 
int   pos  =  getSingleElectronNumber  (  singleElectron  )  ; 
if  (  pos  !=  -  1  )  removeSingleElectron  (  pos  )  ; 
} 







public   IElectronContainer   removeElectronContainer  (  int   number  )  { 
if  (  number  <  this  .  bondCount  )  return   removeBond  (  number  )  ; 
number  -=  this  .  bondCount  ; 
if  (  number  <  this  .  lonePairCount  )  return   removeLonePair  (  number  )  ; 
number  -=  this  .  lonePairCount  ; 
if  (  number  <  this  .  singleElectronCount  )  return   removeSingleElectron  (  number  )  ; 
return   null  ; 
} 






public   void   removeElectronContainer  (  IElectronContainer   electronContainer  )  { 
if  (  electronContainer   instanceof   IBond  )  removeBond  (  (  IBond  )  electronContainer  )  ;  else   if  (  electronContainer   instanceof   ILonePair  )  removeLonePair  (  (  ILonePair  )  electronContainer  )  ;  else   if  (  electronContainer   instanceof   ISingleElectron  )  removeSingleElectron  (  (  ISingleElectron  )  electronContainer  )  ; 
} 







public   void   removeAtomAndConnectedElectronContainers  (  IAtom   atom  )  { 
int   position  =  getAtomNumber  (  atom  )  ; 
if  (  position  !=  -  1  )  { 
for  (  int   i  =  0  ;  i  <  bondCount  ;  i  ++  )  { 
if  (  bonds  [  i  ]  .  contains  (  atom  )  )  { 
removeBond  (  i  )  ; 
--  i  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  lonePairCount  ;  i  ++  )  { 
if  (  lonePairs  [  i  ]  .  contains  (  atom  )  )  { 
removeLonePair  (  i  )  ; 
--  i  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  singleElectronCount  ;  i  ++  )  { 
if  (  singleElectrons  [  i  ]  .  contains  (  atom  )  )  { 
removeSingleElectron  (  i  )  ; 
--  i  ; 
} 
} 
removeAtom  (  position  )  ; 
} 
notifyChanged  (  )  ; 
} 




public   void   removeAllElements  (  )  { 
removeAllElectronContainers  (  )  ; 
for  (  int   f  =  0  ;  f  <  getAtomCount  (  )  ;  f  ++  )  { 
getAtom  (  f  )  .  removeListener  (  this  )  ; 
} 
atoms  =  new   IAtom  [  growArraySize  ]  ; 
atomCount  =  0  ; 
notifyChanged  (  )  ; 
} 




public   void   removeAllElectronContainers  (  )  { 
removeAllBonds  (  )  ; 
for  (  int   f  =  0  ;  f  <  getLonePairCount  (  )  ;  f  ++  )  { 
getLonePair  (  f  )  .  removeListener  (  this  )  ; 
} 
for  (  int   f  =  0  ;  f  <  getSingleElectronCount  (  )  ;  f  ++  )  { 
getSingleElectron  (  f  )  .  removeListener  (  this  )  ; 
} 
lonePairs  =  new   ILonePair  [  growArraySize  ]  ; 
singleElectrons  =  new   ISingleElectron  [  growArraySize  ]  ; 
lonePairCount  =  0  ; 
singleElectronCount  =  0  ; 
notifyChanged  (  )  ; 
} 




public   void   removeAllBonds  (  )  { 
for  (  int   f  =  0  ;  f  <  getBondCount  (  )  ;  f  ++  )  { 
getBond  (  f  )  .  removeListener  (  this  )  ; 
} 
bonds  =  new   IBond  [  growArraySize  ]  ; 
bondCount  =  0  ; 
notifyChanged  (  )  ; 
} 









public   void   addBond  (  int   atom1  ,  int   atom2  ,  IBond  .  Order   order  ,  IBond  .  Stereo   stereo  )  { 
IBond   bond  =  getBuilder  (  )  .  newInstance  (  IBond  .  class  ,  getAtom  (  atom1  )  ,  getAtom  (  atom2  )  ,  order  ,  stereo  )  ; 
if  (  contains  (  bond  )  )  { 
return  ; 
} 
if  (  bondCount  >=  bonds  .  length  )  { 
growBondArray  (  )  ; 
} 
addBond  (  bond  )  ; 
} 








public   void   addBond  (  int   atom1  ,  int   atom2  ,  IBond  .  Order   order  )  { 
IBond   bond  =  getBuilder  (  )  .  newInstance  (  IBond  .  class  ,  getAtom  (  atom1  )  ,  getAtom  (  atom2  )  ,  order  )  ; 
if  (  bondCount  >=  bonds  .  length  )  { 
growBondArray  (  )  ; 
} 
addBond  (  bond  )  ; 
} 






public   void   addLonePair  (  int   atomID  )  { 
ILonePair   lonePair  =  getBuilder  (  )  .  newInstance  (  ILonePair  .  class  ,  atoms  [  atomID  ]  )  ; 
lonePair  .  addListener  (  this  )  ; 
addLonePair  (  lonePair  )  ; 
} 






public   void   addSingleElectron  (  int   atomID  )  { 
ISingleElectron   singleElectron  =  getBuilder  (  )  .  newInstance  (  ISingleElectron  .  class  ,  atoms  [  atomID  ]  )  ; 
singleElectron  .  addListener  (  this  )  ; 
addSingleElectron  (  singleElectron  )  ; 
} 







public   boolean   contains  (  IAtom   atom  )  { 
for  (  int   i  =  0  ;  i  <  getAtomCount  (  )  ;  i  ++  )  { 
if  (  atom  ==  atoms  [  i  ]  )  return   true  ; 
} 
return   false  ; 
} 







public   boolean   contains  (  IBond   bond  )  { 
for  (  int   i  =  0  ;  i  <  getBondCount  (  )  ;  i  ++  )  { 
if  (  bond  ==  bonds  [  i  ]  )  return   true  ; 
} 
return   false  ; 
} 







public   boolean   contains  (  ILonePair   lonePair  )  { 
for  (  int   i  =  0  ;  i  <  getLonePairCount  (  )  ;  i  ++  )  { 
if  (  lonePair  ==  lonePairs  [  i  ]  )  return   true  ; 
} 
return   false  ; 
} 







public   boolean   contains  (  ISingleElectron   singleElectron  )  { 
for  (  int   i  =  0  ;  i  <  getSingleElectronCount  (  )  ;  i  ++  )  { 
if  (  singleElectron  ==  singleElectrons  [  i  ]  )  return   true  ; 
} 
return   false  ; 
} 







public   boolean   contains  (  IElectronContainer   electronContainer  )  { 
if  (  electronContainer   instanceof   IBond  )  return   contains  (  (  IBond  )  electronContainer  )  ; 
if  (  electronContainer   instanceof   ILonePair  )  return   contains  (  (  ILonePair  )  electronContainer  )  ; 
if  (  electronContainer   instanceof   ISingleElectron  )  return   contains  (  (  ISingleElectron  )  electronContainer  )  ; 
return   false  ; 
} 







public   Object   clone  (  )  throws   CloneNotSupportedException  { 
IAtom  [  ]  newAtoms  ; 
IAtomContainer   clone  =  (  IAtomContainer  )  super  .  clone  (  )  ; 
clone  .  removeAllElements  (  )  ; 
for  (  int   f  =  0  ;  f  <  getAtomCount  (  )  ;  f  ++  )  { 
clone  .  addAtom  (  (  IAtom  )  getAtom  (  f  )  .  clone  (  )  )  ; 
} 
IBond   bond  ; 
IBond   newBond  ; 
for  (  int   i  =  0  ;  i  <  getBondCount  (  )  ;  ++  i  )  { 
bond  =  getBond  (  i  )  ; 
newBond  =  (  IBond  )  bond  .  clone  (  )  ; 
newAtoms  =  new   IAtom  [  bond  .  getAtomCount  (  )  ]  ; 
for  (  int   j  =  0  ;  j  <  bond  .  getAtomCount  (  )  ;  ++  j  )  { 
newAtoms  [  j  ]  =  clone  .  getAtom  (  getAtomNumber  (  bond  .  getAtom  (  j  )  )  )  ; 
} 
newBond  .  setAtoms  (  newAtoms  )  ; 
clone  .  addBond  (  newBond  )  ; 
} 
ILonePair   lp  ; 
ILonePair   newLp  ; 
for  (  int   i  =  0  ;  i  <  getLonePairCount  (  )  ;  ++  i  )  { 
lp  =  getLonePair  (  i  )  ; 
newLp  =  (  ILonePair  )  lp  .  clone  (  )  ; 
if  (  lp  .  getAtom  (  )  !=  null  )  { 
newLp  .  setAtom  (  clone  .  getAtom  (  getAtomNumber  (  lp  .  getAtom  (  )  )  )  )  ; 
} 
clone  .  addLonePair  (  newLp  )  ; 
} 
ISingleElectron   se  ; 
ISingleElectron   newSe  ; 
for  (  int   i  =  0  ;  i  <  getSingleElectronCount  (  )  ;  ++  i  )  { 
se  =  getSingleElectron  (  i  )  ; 
newSe  =  (  ISingleElectron  )  se  .  clone  (  )  ; 
if  (  se  .  getAtom  (  )  !=  null  )  { 
newSe  .  setAtom  (  clone  .  getAtom  (  getAtomNumber  (  se  .  getAtom  (  )  )  )  )  ; 
} 
clone  .  addSingleElectron  (  newSe  )  ; 
} 
return   clone  ; 
} 






private   void   growAtomArray  (  )  { 
growArraySize  =  (  atoms  .  length  <  growArraySize  )  ?  growArraySize  :  atoms  .  length  ; 
IAtom  [  ]  newatoms  =  new   IAtom  [  atoms  .  length  +  growArraySize  ]  ; 
System  .  arraycopy  (  atoms  ,  0  ,  newatoms  ,  0  ,  atoms  .  length  )  ; 
atoms  =  newatoms  ; 
} 






private   void   growBondArray  (  )  { 
growArraySize  =  (  bonds  .  length  <  growArraySize  )  ?  growArraySize  :  bonds  .  length  ; 
IBond  [  ]  newBonds  =  new   IBond  [  bonds  .  length  +  growArraySize  ]  ; 
System  .  arraycopy  (  bonds  ,  0  ,  newBonds  ,  0  ,  bonds  .  length  )  ; 
bonds  =  newBonds  ; 
} 






private   void   growLonePairArray  (  )  { 
growArraySize  =  (  lonePairs  .  length  <  growArraySize  )  ?  growArraySize  :  lonePairs  .  length  ; 
ILonePair  [  ]  newLonePairs  =  new   ILonePair  [  lonePairs  .  length  +  growArraySize  ]  ; 
System  .  arraycopy  (  lonePairs  ,  0  ,  newLonePairs  ,  0  ,  lonePairs  .  length  )  ; 
lonePairs  =  newLonePairs  ; 
} 






private   void   growSingleElectronArray  (  )  { 
growArraySize  =  (  singleElectrons  .  length  <  growArraySize  )  ?  growArraySize  :  singleElectrons  .  length  ; 
ISingleElectron  [  ]  newSingleElectrons  =  new   ISingleElectron  [  singleElectrons  .  length  +  growArraySize  ]  ; 
System  .  arraycopy  (  singleElectrons  ,  0  ,  newSingleElectrons  ,  0  ,  singleElectrons  .  length  )  ; 
singleElectrons  =  newSingleElectrons  ; 
} 







public   void   stateChanged  (  IChemObjectChangeEvent   event  )  { 
notifyChanged  (  event  )  ; 
} 
} 

