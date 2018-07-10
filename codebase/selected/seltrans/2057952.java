package   org  .  jmol  .  adapter  .  readers  .  simple  ; 

import   java  .  util  .  BitSet  ; 
import   org  .  jmol  .  adapter  .  smarter  .  *  ; 
import   org  .  jmol  .  util  .  Logger  ; 
import   org  .  jmol  .  util  .  Parser  ; 






public   class   MopacReader   extends   AtomSetCollectionReader  { 

private   int   baseAtomIndex  ; 

private   boolean   chargesFound  =  false  ; 

private   boolean   haveHeader  ; 

private   int   mopacVersion  ; 

@  Override 
protected   void   initializeReader  (  )  throws   Exception  { 
while  (  mopacVersion  ==  0  )  { 
discardLinesUntilContains  (  "MOPAC"  )  ; 
if  (  line  .  indexOf  (  "2009"  )  >=  0  )  mopacVersion  =  2009  ;  else   if  (  line  .  indexOf  (  "6."  )  >=  0  )  mopacVersion  =  6  ;  else   if  (  line  .  indexOf  (  "7."  )  >=  0  )  mopacVersion  =  7  ;  else   if  (  line  .  indexOf  (  "93"  )  >=  0  )  mopacVersion  =  93  ;  else   if  (  line  .  indexOf  (  "2002"  )  >=  0  )  mopacVersion  =  2002  ; 
} 
Logger  .  info  (  "MOPAC version "  +  mopacVersion  )  ; 
} 

@  Override 
protected   boolean   checkLine  (  )  throws   Exception  { 
if  (  !  haveHeader  )  { 
if  (  line  .  trim  (  )  .  equals  (  "CARTESIAN COORDINATES"  )  )  { 
processCoordinates  (  )  ; 
atomSetCollection  .  setAtomSetName  (  "Input Structure"  )  ; 
return   true  ; 
} 
haveHeader  =  line  .  startsWith  (  " ---"  )  ; 
return   true  ; 
} 
if  (  line  .  indexOf  (  "TOTAL ENERGY"  )  >=  0  )  { 
processTotalEnergy  (  )  ; 
return   true  ; 
} 
if  (  line  .  indexOf  (  "ATOMIC CHARGES"  )  >=  0  )  { 
processAtomicCharges  (  )  ; 
return   true  ; 
} 
if  (  line  .  trim  (  )  .  equals  (  "CARTESIAN COORDINATES"  )  )  { 
processCoordinates  (  )  ; 
return   true  ; 
} 
if  (  line  .  indexOf  (  "ORIENTATION OF MOLECULE IN FORCE"  )  >=  0  )  { 
processCoordinates  (  )  ; 
atomSetCollection  .  setAtomSetName  (  "Orientation in Force Field"  )  ; 
return   true  ; 
} 
if  (  line  .  indexOf  (  "NORMAL COORDINATE ANALYSIS"  )  >=  0  )  { 
readFrequencies  (  )  ; 
return   true  ; 
} 
return   true  ; 
} 

void   processTotalEnergy  (  )  { 
} 

















void   processAtomicCharges  (  )  throws   Exception  { 
readLines  (  2  )  ; 
atomSetCollection  .  newAtomSet  (  )  ; 
baseAtomIndex  =  atomSetCollection  .  getAtomCount  (  )  ; 
int   expectedAtomNumber  =  0  ; 
while  (  readLine  (  )  !=  null  )  { 
int   atomNumber  =  parseInt  (  line  )  ; 
if  (  atomNumber  ==  Integer  .  MIN_VALUE  )  break  ; 
++  expectedAtomNumber  ; 
if  (  atomNumber  !=  expectedAtomNumber  )  throw   new   Exception  (  "unexpected atom number in atomic charges"  )  ; 
Atom   atom  =  atomSetCollection  .  addNewAtom  (  )  ; 
atom  .  elementSymbol  =  parseToken  (  )  ; 
atom  .  partialCharge  =  parseFloat  (  )  ; 
} 
chargesFound  =  true  ; 
} 


























void   processCoordinates  (  )  throws   Exception  { 
readLines  (  3  )  ; 
int   expectedAtomNumber  =  0  ; 
if  (  !  chargesFound  )  { 
atomSetCollection  .  newAtomSet  (  )  ; 
baseAtomIndex  =  atomSetCollection  .  getAtomCount  (  )  ; 
}  else  { 
chargesFound  =  false  ; 
} 
Atom  [  ]  atoms  =  atomSetCollection  .  getAtoms  (  )  ; 
while  (  readLine  (  )  !=  null  )  { 
int   atomNumber  =  parseInt  (  line  )  ; 
if  (  atomNumber  ==  Integer  .  MIN_VALUE  )  break  ; 
++  expectedAtomNumber  ; 
if  (  atomNumber  !=  expectedAtomNumber  )  throw   new   Exception  (  "unexpected atom number in coordinates"  )  ; 
String   elementSymbol  =  parseToken  (  )  ; 
Atom   atom  =  atoms  [  baseAtomIndex  +  atomNumber  -  1  ]  ; 
if  (  atom  ==  null  )  { 
atom  =  atomSetCollection  .  addNewAtom  (  )  ; 
} 
atom  .  atomSerial  =  atomNumber  ; 
setAtomCoord  (  atom  ,  parseFloat  (  )  ,  parseFloat  (  )  ,  parseFloat  (  )  )  ; 
int   atno  =  parseInt  (  elementSymbol  )  ; 
if  (  atno  !=  Integer  .  MIN_VALUE  )  elementSymbol  =  getElementSymbol  (  atno  )  ; 
atom  .  elementSymbol  =  elementSymbol  ; 
} 
} 






























private   void   readFrequencies  (  )  throws   Exception  { 
BitSet   bsOK  =  new   BitSet  (  )  ; 
int   n0  =  atomSetCollection  .  getCurrentAtomSetIndex  (  )  +  1  ; 
String  [  ]  tokens  ; 
boolean   done  =  false  ; 
while  (  !  done  &&  readLine  (  )  !=  null  &&  line  .  indexOf  (  "DESCRIPTION"  )  <  0  &&  line  .  indexOf  (  "MASS-WEIGHTED"  )  <  0  )  if  (  line  .  toUpperCase  (  )  .  indexOf  (  "ROOT"  )  >=  0  )  { 
discardLinesUntilNonBlank  (  )  ; 
tokens  =  getTokens  (  )  ; 
if  (  Float  .  isNaN  (  Parser  .  parseFloatStrict  (  tokens  [  tokens  .  length  -  1  ]  )  )  )  { 
discardLinesUntilNonBlank  (  )  ; 
tokens  =  getTokens  (  )  ; 
} 
int   frequencyCount  =  tokens  .  length  ; 
readLine  (  )  ; 
int   iAtom0  =  atomSetCollection  .  getAtomCount  (  )  ; 
int   atomCount  =  atomSetCollection  .  getLastAtomSetAtomCount  (  )  ; 
boolean  [  ]  ignore  =  new   boolean  [  frequencyCount  ]  ; 
for  (  int   i  =  0  ;  i  <  frequencyCount  ;  ++  i  )  { 
ignore  [  i  ]  =  done  ||  (  done  =  Parser  .  parseFloatStrict  (  tokens  [  i  ]  )  <  1  )  ||  !  doGetVibration  (  ++  vibrationNumber  )  ; 
if  (  ignore  [  i  ]  )  continue  ; 
bsOK  .  set  (  vibrationNumber  -  1  )  ; 
atomSetCollection  .  cloneLastAtomSet  (  )  ; 
} 
fillFrequencyData  (  iAtom0  ,  atomCount  ,  atomCount  ,  ignore  ,  false  ,  0  ,  0  ,  null  )  ; 
} 
String  [  ]  [  ]  info  =  new   String  [  vibrationNumber  ]  [  ]  ; 
if  (  line  .  indexOf  (  "DESCRIPTION"  )  <  0  )  discardLinesUntilContains  (  "DESCRIPTION"  )  ; 
while  (  discardLinesUntilContains  (  "VIBRATION"  )  !=  null  )  { 
tokens  =  getTokens  (  )  ; 
int   freqNo  =  parseInt  (  tokens  [  1  ]  )  ; 
tokens  [  0  ]  =  getTokens  (  readLine  (  )  )  [  1  ]  ; 
if  (  tokens  [  2  ]  .  equals  (  "ATOM"  )  )  tokens  [  2  ]  =  null  ; 
info  [  freqNo  -  1  ]  =  tokens  ; 
if  (  freqNo  ==  vibrationNumber  )  break  ; 
} 
for  (  int   i  =  vibrationNumber  -  1  ;  --  i  >=  0  ;  )  if  (  info  [  i  ]  ==  null  )  info  [  i  ]  =  info  [  i  +  1  ]  ; 
for  (  int   i  =  0  ,  n  =  n0  ;  i  <  vibrationNumber  ;  i  ++  )  { 
if  (  !  bsOK  .  get  (  i  )  )  continue  ; 
atomSetCollection  .  setCurrentAtomSetIndex  (  n  ++  )  ; 
atomSetCollection  .  setAtomSetFrequency  (  null  ,  info  [  i  ]  [  2  ]  ,  info  [  i  ]  [  0  ]  ,  null  )  ; 
} 
} 
} 

