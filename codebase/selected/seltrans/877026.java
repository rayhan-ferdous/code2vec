package   org  .  TMSIM  .  TestData  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  TMSIM  .  Exceptions  .  TapeOutofBoundsException  ; 
import   org  .  TMSIM  .  Model  .  BaseConfiguration  ; 
import   org  .  TMSIM  .  Model  .  State  ; 
import   org  .  TMSIM  .  Model  .  Transition  ; 
import   org  .  TMSIM  .  Model  .  TransitionCollection  ; 
import   org  .  TMSIM  .  Model  .  TuringMachine  ; 







public   final   class   TestDataGenerator  { 

private   TestDataGenerator  (  )  { 
} 

private   static   boolean   debug  =  false  ; 

private   static   TransitionCollection   transitionCollection  ; 

private   static   ArrayList  <  State  >  states  ; 

private   static   ArrayList   marked  ; 

private   static   boolean   removed  =  false  ; 




















public   static   TuringMachine   generateTestTuringMachine  (  char   startChar  ,  Random   random  ,  int   maxCountArray  ,  int   maxCountTransition  ,  boolean   finiteAutomate  ,  String   alphabetIT  ,  String   alphabetWT  ,  int   maxLengthIT  ,  int   maxLengthWT  )  { 
int   lengthIT  =  random  .  nextInt  (  maxLengthIT  )  ; 
int   lengthWT  =  random  .  nextInt  (  maxLengthWT  )  ; 
int   ItHead  =  random  .  nextInt  (  lengthIT  +  2  )  ; 
int   WtHead  =  random  .  nextInt  (  lengthWT  +  1  )  ; 
return   generateTestTuringMachine  (  startChar  ,  new   Random  (  )  ,  maxCountArray  ,  maxCountTransition  ,  finiteAutomate  ,  alphabetIT  ,  alphabetWT  ,  lengthIT  ,  lengthWT  ,  ItHead  ,  WtHead  )  ; 
} 






















public   static   TuringMachine   generateTestTuringMachine  (  char   startChar  ,  Random   random  ,  int   maxCountArray  ,  int   maxCountTransition  ,  boolean   finiteAutomate  ,  String   alphabetIT  ,  String   alphabetWT  ,  int   LengthIT  ,  int   LengthWT  ,  int   ITHead  ,  int   WTHead  )  { 
TuringMachine   testTuringMachine  =  new   TuringMachine  (  )  ; 
int   statecount  =  1  +  random  .  nextInt  (  maxCountArray  +  1  )  ; 
states  =  generateTestStates  (  startChar  ,  statecount  )  ; 
ArrayList   testTransitionCollection  =  generateTestCollection  (  states  ,  maxCountTransition  ,  alphabetIT  ,  alphabetWT  )  ; 
transitionCollection  =  new   TransitionCollection  (  )  ; 
for  (  int   f  =  0  ;  f  <  testTransitionCollection  .  size  (  )  ;  f  ++  )  { 
for  (  int   g  =  0  ;  g  <  (  (  ArrayList  )  testTransitionCollection  .  get  (  f  )  )  .  size  (  )  ;  g  ++  )  { 
transitionCollection  .  add  (  (  (  ArrayList  )  testTransitionCollection  .  get  (  f  )  )  .  get  (  g  )  )  ; 
} 
} 
if  (  debug  )  { 
System  .  out  .  println  (  "collection generated"  )  ; 
printTestData  (  testTransitionCollection  )  ; 
System  .  out  .  println  (  "collection added"  )  ; 
transitionCollection  .  printCollection  (  )  ; 
} 
int   initalStateIndex  =  random  .  nextInt  (  states  .  size  (  )  )  ; 
State   initalState  =  states  .  get  (  initalStateIndex  )  ; 
marked  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  states  .  size  (  )  ;  i  ++  )  { 
marked  .  add  (  0  )  ; 
} 
DepthFirstSearch  (  initalStateIndex  )  ; 
if  (  debug  )  System  .  out  .  println  (  "InitialState "  +  initalState  .  getStateByFormat  (  State  .  statePrintFormat  )  )  ; 
if  (  debug  )  transitionCollection  .  printCollection  (  )  ; 
for  (  int   i  =  0  ;  i  <  marked  .  size  (  )  ;  i  ++  )  { 
if  (  !  marked  .  get  (  i  )  .  equals  (  1  )  )  { 
if  (  debug  )  System  .  out  .  println  (  states  .  get  (  i  )  .  getStateByFormat  (  State  .  statePrintFormat  )  +  " is not reached -> remove from collection and stateArray"  )  ; 
states  .  remove  (  i  )  ; 
marked  .  remove  (  i  )  ; 
removed  =  true  ; 
ArrayList  <  Transition  >  transitionsToRemove  =  (  ArrayList  <  Transition  >  )  testTransitionCollection  .  get  (  i  )  ; 
testTransitionCollection  .  remove  (  i  )  ; 
for  (  int   e  =  0  ;  e  <  transitionsToRemove  .  size  (  )  ;  e  ++  )  { 
transitionCollection  .  remove  (  transitionsToRemove  .  get  (  e  )  )  ; 
} 
i  --  ; 
} 
} 
if  (  removed  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  "cleaned collection"  )  ; 
transitionCollection  .  printCollection  (  )  ; 
} 
} 
ArrayList   finalStates  =  new   ArrayList  (  )  ; 
int   numFinalStates  ; 
if  (  states  .  size  (  )  >  1  )  { 
numFinalStates  =  1  +  random  .  nextInt  (  states  .  size  (  )  -  1  )  ; 
}  else  { 
numFinalStates  =  1  ; 
} 
int   generatedPos  =  random  .  nextInt  (  states  .  size  (  )  )  ; 
if  (  numFinalStates  !=  states  .  size  (  )  )  { 
for  (  int   i  =  0  ;  i  <  numFinalStates  ;  i  ++  )  { 
while  (  finalStates  .  contains  (  states  .  get  (  generatedPos  )  )  )  { 
generatedPos  =  random  .  nextInt  (  states  .  size  (  )  )  ; 
} 
finalStates  .  add  (  states  .  get  (  generatedPos  )  )  ; 
} 
}  else  { 
finalStates  =  states  ; 
} 
BaseConfiguration   testBaseConfiguration  =  new   BaseConfiguration  (  )  ; 
String   IT  =  ""  ; 
for  (  int   i  =  0  ;  i  <  LengthIT  ;  i  ++  )  { 
IT  +=  alphabetIT  .  charAt  (  random  .  nextInt  (  alphabetIT  .  length  (  )  )  )  ; 
} 
IT  =  TuringMachine  .  startSymbolIT  +  IT  +  TuringMachine  .  endSymbolIT  ; 
String   WT  =  ""  ; 
for  (  int   i  =  0  ;  i  <  LengthWT  ;  i  ++  )  { 
WT  +=  alphabetWT  .  charAt  (  random  .  nextInt  (  alphabetWT  .  length  (  )  )  )  ; 
} 
WT  =  TuringMachine  .  startSymbolWT  +  WT  ; 
testBaseConfiguration  .  setIT  (  IT  )  ; 
testBaseConfiguration  .  setWT  (  WT  )  ; 
try  { 
testBaseConfiguration  .  setITHead  (  ITHead  )  ; 
testBaseConfiguration  .  setWTHead  (  WTHead  )  ; 
}  catch  (  TapeOutofBoundsException   ex  )  { 
Logger  .  getLogger  (  TestDataGenerator  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
testTuringMachine  .  setStates  (  states  )  ; 
testTuringMachine  .  setAlphabetIT  (  alphabetIT  )  ; 
testTuringMachine  .  setAlphabetWT  (  alphabetWT  )  ; 
testTuringMachine  .  setTransitionFunction  (  transitionCollection  )  ; 
testTuringMachine  .  setInitalState  (  initalState  )  ; 
testTuringMachine  .  setFinalStates  (  finalStates  )  ; 
testTuringMachine  .  setBaseConfiguration  (  testBaseConfiguration  )  ; 
testTuringMachine  .  setFiniteAutomate  (  finiteAutomate  )  ; 
testTuringMachine  .  setBlankSymbol  (  '_'  )  ; 
return   testTuringMachine  ; 
} 






public   static   void   DepthFirstSearch  (  int   index  )  { 
marked  .  set  (  index  ,  1  )  ; 
if  (  debug  )  System  .  out  .  println  (  "currentState "  +  states  .  get  (  index  )  .  getStateByFormat  (  State  .  statePrintFormat  )  )  ; 
ArrayList  <  State  >  neighbours  =  getNeighbours  (  states  .  get  (  index  )  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "Neighbours "  +  neighbours  .  size  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  neighbours  .  size  (  )  ;  i  ++  )  { 
neighbours  .  get  (  i  )  .  printState  (  )  ; 
System  .  out  .  print  (  "-"  )  ; 
} 
System  .  out  .  println  (  ""  )  ; 
} 
for  (  int   i  =  0  ;  i  <  neighbours  .  size  (  )  ;  i  ++  )  { 
if  (  !  marked  .  get  (  states  .  indexOf  (  neighbours  .  get  (  i  )  )  )  .  equals  (  1  )  )  { 
DepthFirstSearch  (  states  .  indexOf  (  neighbours  .  get  (  i  )  )  )  ; 
} 
} 
} 






public   static   ArrayList  <  State  >  getNeighbours  (  State   state  )  { 
ArrayList  <  State  >  neighbours  =  new   ArrayList  (  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "size "  +  transitionCollection  .  size  (  )  )  ; 
state  .  printState  (  )  ; 
System  .  out  .  println  (  ""  )  ; 
} 
for  (  int   i  =  0  ;  i  <  transitionCollection  .  size  (  )  ;  i  ++  )  { 
ArrayList  <  Transition  >  currentTransitionArray  =  (  (  ArrayList  )  transitionCollection  .  getSortedTransitions  (  )  .  get  (  i  )  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "size2 "  +  currentTransitionArray  .  size  (  )  )  ; 
currentTransitionArray  .  get  (  0  )  .  getState  (  )  .  printState  (  )  ; 
System  .  out  .  println  (  ""  )  ; 
} 
if  (  currentTransitionArray  .  get  (  0  )  .  getState  (  )  .  equals  (  state  )  )  { 
if  (  debug  )  System  .  out  .  println  (  "index "  +  i  )  ; 
for  (  int   e  =  0  ;  e  <  currentTransitionArray  .  size  (  )  ;  e  ++  )  { 
Transition   currentTransition  =  currentTransitionArray  .  get  (  e  )  ; 
if  (  !  neighbours  .  contains  (  currentTransition  .  getTargetState  (  )  )  )  { 
neighbours  .  add  (  currentTransition  .  getTargetState  (  )  )  ; 
} 
} 
break  ; 
} 
} 
return   neighbours  ; 
} 





private   static   void   printTestData  (  ArrayList   printdata  )  { 
for  (  int   i  =  0  ;  i  <  printdata  .  size  (  )  ;  i  ++  )  { 
System  .  out  .  print  (  "|"  )  ; 
for  (  int   e  =  0  ;  e  <  (  (  ArrayList  )  printdata  .  get  (  i  )  )  .  size  (  )  ;  e  ++  )  { 
Transition   printTransition  =  (  (  Transition  )  (  (  ArrayList  )  printdata  .  get  (  i  )  )  .  get  (  e  )  )  ; 
System  .  out  .  print  (  " - "  )  ; 
printTransition  .  printTransition  (  Transition  .  transitionPrintFormat  ,  State  .  statePrintFormat  )  ; 
} 
System  .  out  .  println  (  ""  )  ; 
} 
} 















public   static   ArrayList   generateTestCollection  (  char   startChar  ,  Random   random  ,  int   maxCountArray  ,  int   maxCountTransition  ,  String   alphabetIT  ,  String   alphabetWT  )  { 
int   localcountArray  =  1  +  random  .  nextInt  (  maxCountArray  +  1  )  ; 
ArrayList   stateArray  =  generateTestStates  (  startChar  ,  localcountArray  )  ; 
return   generateTestCollection  (  stateArray  ,  maxCountTransition  ,  alphabetIT  ,  alphabetWT  )  ; 
} 












public   static   ArrayList   generateTestCollection  (  ArrayList   stateArray  ,  int   maxCountTransition  ,  String   alphabetIT  ,  String   alphabetWT  )  { 
ArrayList   collectionArray  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  stateArray  .  size  (  )  ;  i  ++  )  { 
collectionArray  .  add  (  generateTestTransitions  (  stateArray  ,  i  ,  new   Random  (  )  ,  maxCountTransition  ,  alphabetIT  ,  alphabetWT  )  )  ; 
} 
return   collectionArray  ; 
} 











public   static   ArrayList   generateTestTransitions  (  ArrayList   stateArray  ,  int   index  ,  Random   random  ,  int   maxCountTransition  ,  String   alphabetIT  ,  String   alphabetWT  )  { 
int   countTransition  =  1  +  random  .  nextInt  (  maxCountTransition  +  1  )  ; 
ArrayList   transitionArray  =  new   ArrayList  (  )  ; 
State   indexState  =  (  State  )  stateArray  .  get  (  index  )  ; 
char   randomChar  =  indexState  .  getName  (  )  .  charAt  (  0  )  ; 
for  (  int   i  =  0  ;  i  <  countTransition  ;  i  ++  )  { 
transitionArray  .  add  (  generateTestTransition  (  indexState  ,  stateArray  ,  new   Random  (  )  ,  alphabetIT  ,  alphabetWT  )  )  ; 
} 
return   transitionArray  ; 
} 










public   static   Transition   generateTestTransition  (  State   indexState  ,  ArrayList   stateArray  ,  Random   random  ,  String   alphabetIT  ,  String   alphabetWT  )  { 
char   a  =  alphabetIT  .  charAt  (  random  .  nextInt  (  alphabetIT  .  length  (  )  )  )  ; 
char   X  =  alphabetIT  .  charAt  (  random  .  nextInt  (  alphabetIT  .  length  (  )  )  )  ; 
char   Y  =  alphabetWT  .  charAt  (  random  .  nextInt  (  alphabetWT  .  length  (  )  )  )  ; 
int   randomState  =  random  .  nextInt  (  stateArray  .  size  (  )  )  ; 
return   new   Transition  (  indexState  ,  a  ,  X  ,  (  State  )  stateArray  .  get  (  randomState  )  ,  Y  ,  (  1  +  random  .  nextInt  (  3  )  )  ,  (  1  +  random  .  nextInt  (  3  )  )  )  ; 
} 








public   static   ArrayList   generateTestStates  (  char   stateName  ,  int   countState  )  { 
ArrayList   stateArray  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  countState  ;  i  ++  )  { 
stateArray  .  add  (  new   State  (  ""  +  (  (  char  )  stateName  )  )  )  ; 
stateName  ++  ; 
} 
return   stateArray  ; 
} 






public   static   Transition   fetchRandomTransitionFromCollection  (  ArrayList   collection  )  { 
Random   random  =  new   Random  (  )  ; 
int   arrayPos  =  random  .  nextInt  (  collection  .  size  (  )  )  ; 
int   transitionPos  =  random  .  nextInt  (  (  (  ArrayList  )  collection  .  get  (  arrayPos  )  )  .  size  (  )  )  ; 
return  (  Transition  )  (  (  ArrayList  )  collection  .  get  (  arrayPos  )  )  .  get  (  transitionPos  )  ; 
} 









public   static   Transition   fetchRandomTransitionFromCollectionIndex  (  ArrayList   collection  ,  int   index  )  { 
Random   random  =  new   Random  (  )  ; 
int   randomPos  =  random  .  nextInt  (  (  (  ArrayList  )  collection  .  get  (  index  )  )  .  size  (  )  )  ; 
return  (  Transition  )  (  (  ArrayList  )  collection  .  get  (  index  )  )  .  get  (  randomPos  )  ; 
} 










public   static   ArrayList   fetchAllTransitions  (  ArrayList   collection  ,  int   index  ,  Transition   transition  )  { 
ArrayList   expectedTransitions  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  (  (  ArrayList  )  collection  .  get  (  index  )  )  .  size  (  )  ;  i  ++  )  { 
Transition   tempTransition  =  (  Transition  )  (  (  ArrayList  )  (  collection  .  get  (  index  )  )  )  .  get  (  i  )  ; 
if  (  transition  .  getState  (  )  .  equals  (  tempTransition  .  getState  (  )  )  &&  transition  .  getInSymbolIT  (  )  ==  tempTransition  .  getInSymbolIT  (  )  &&  transition  .  getInSymbolWT  (  )  ==  tempTransition  .  getInSymbolWT  (  )  )  { 
expectedTransitions  .  add  (  tempTransition  )  ; 
} 
} 
return   expectedTransitions  ; 
} 









public   static   int   fetchIndexOfTransition  (  ArrayList   collection  ,  Transition   transition  )  { 
int   index  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  collection  .  size  (  )  ;  i  ++  )  { 
Transition   tempTransition  =  (  (  Transition  )  (  (  ArrayList  )  collection  .  get  (  i  )  )  .  get  (  0  )  )  ; 
if  (  transition  .  getState  (  )  .  equals  (  tempTransition  .  getState  (  )  )  )  { 
index  =  i  ; 
} 
} 
return   index  ; 
} 








public   static   ArrayList   fetchRandomTransitionSet  (  ArrayList   collection  )  { 
Transition   randomTransition  =  fetchRandomTransitionFromCollection  (  collection  )  ; 
int   arrayPos  =  fetchIndexOfTransition  (  collection  ,  randomTransition  )  ; 
return   fetchAllTransitions  (  collection  ,  arrayPos  ,  randomTransition  )  ; 
} 
} 

