package   gate  .  jape  .  plus  ; 

import   gate  .  annotation  .  AnnotationSetImpl  ; 
import   gate  .  creole  .  *  ; 
import   gate  .  creole  .  ontology  .  OClass  ; 
import   gate  .  creole  .  ontology  .  OConstants  ; 
import   gate  .  creole  .  ontology  .  OConstants  .  Closure  ; 
import   gate  .  creole  .  ontology  .  OResource  ; 
import   gate  .  creole  .  ontology  .  Ontology  ; 
import   gate  .  jape  .  JapeException  ; 
import   gate  .  jape  .  Rule  ; 
import   gate  .  jape  .  DefaultActionContext  ; 
import   gate  .  jape  .  ActionContext  ; 
import   gate  .  *  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   com  .  ontotext  .  jape  .  pda  .  TransitionPDA  ; 
import   cern  .  colt  .  GenericSorting  ; 
import   cern  .  colt  .  Sorting  ; 
import   cern  .  colt  .  Swapper  ; 
import   cern  .  colt  .  bitvector  .  QuickBitVector  ; 
import   cern  .  colt  .  function  .  IntComparator  ; 
import   cern  .  colt  .  list  .  IntArrayList  ; 
import   gate  .  jape  .  ControllerEventBlocksAction  ; 
import   gate  .  jape  .  constraint  .  ConstraintPredicate  ; 




public   abstract   class   SPTBase   extends   AbstractLanguageAnalyser  { 

protected   ActionContext   actionContext  ; 









protected   abstract   boolean   advanceInstance  (  FSMInstance   instance  )  throws   JapeException  ; 





public   void   setActionContext  (  ActionContext   ac  )  { 
actionContext  =  ac  ; 
} 




protected   static   class   State  { 




protected   int   id  ; 




protected   Transition  [  ]  transitions  ; 





protected   int   rule  =  NOT_CALCULATED  ; 
} 




protected   static   class   Transition  { 




























protected   int  [  ]  [  ]  constraints  ; 




protected   int   nextState  ; 




protected   int   type  ; 
} 




protected   class   FSMInstance   implements   Comparable  <  FSMInstance  >  { 

public   FSMInstance  (  int   annotationIndex  ,  int   state  ,  Map  <  String  ,  IntArrayList  >  bindings  )  { 
this  (  annotationIndex  ,  state  ,  bindings  ,  new   IntArrayList  [  8  ]  )  ; 
} 

private   FSMInstance  (  int   annotationIndex  ,  int   state  ,  Map  <  String  ,  IntArrayList  >  bindings  ,  IntArrayList  [  ]  bindingStack  )  { 
super  (  )  ; 
this  .  annotationIndex  =  annotationIndex  ; 
this  .  state  =  state  ; 
this  .  bindings  =  bindings  ; 
this  .  bindingStack  =  bindingStack  ; 
} 

public   int   compareTo  (  FSMInstance   other  )  { 
int   res  =  other  .  annotationIndex  -  annotationIndex  ; 
if  (  res  ==  0  )  { 
if  (  rule  >=  0  &&  other  .  rule  >=  0  )  { 
res  =  rules  [  other  .  rule  ]  .  getPriority  (  )  -  rules  [  rule  ]  .  getPriority  (  )  ; 
if  (  res  ==  0  )  { 
res  =  rules  [  rule  ]  .  getPosition  (  )  -  rules  [  other  .  rule  ]  .  getPosition  (  )  ; 
} 
} 
if  (  res  ==  0  )  { 
int   thisSize  =  0  ; 
for  (  IntArrayList   binds  :  bindings  .  values  (  )  )  thisSize  +=  binds  .  size  (  )  ; 
int   thatSize  =  0  ; 
for  (  IntArrayList   binds  :  other  .  bindings  .  values  (  )  )  thatSize  +=  binds  .  size  (  )  ; 
res  =  thatSize  -  thisSize  ; 
} 
} 
return   res  ; 
} 

public   FSMInstance   clone  (  )  { 
Map  <  String  ,  IntArrayList  >  bindsClone  =  new   HashMap  <  String  ,  IntArrayList  >  (  bindings  .  size  (  )  )  ; 
for  (  Map  .  Entry  <  String  ,  IntArrayList  >  anEntry  :  bindings  .  entrySet  (  )  )  { 
bindsClone  .  put  (  anEntry  .  getKey  (  )  ,  anEntry  .  getValue  (  )  .  copy  (  )  )  ; 
} 
FSMInstance   newInstance  =  new   FSMInstance  (  annotationIndex  ,  state  ,  bindsClone  ,  null  )  ; 
newInstance  .  bindingStack  =  new   IntArrayList  [  bindingStack  .  length  ]  ; 
int   j  ,  length  ; 
for  (  int   i  =  0  ;  i  <  bindingStackStored  ;  i  ++  )  { 
if  (  bindingStack  [  i  ]  !=  null  )  { 
newInstance  .  bindingStack  [  i  ]  =  new   IntArrayList  (  )  ; 
length  =  bindingStack  [  i  ]  .  size  (  )  ; 
for  (  j  =  0  ;  j  <  length  ;  j  ++  )  { 
newInstance  .  bindingStack  [  i  ]  .  add  (  bindingStack  [  i  ]  .  get  (  j  )  )  ; 
} 
} 
} 
newInstance  .  bindingStackStored  =  bindingStackStored  ; 
return   newInstance  ; 
} 




public   int   annotationIndex  ; 




public   int   state  ; 






public   int   rule  =  -  1  ; 





protected   Map  <  String  ,  IntArrayList  >  bindings  ; 








private   IntArrayList  [  ]  bindingStack  ; 






private   int   bindingStackStored  ; 






public   void   pushNewEmptyBindingSet  (  )  { 
if  (  bindingStack  .  length  ==  bindingStackStored  )  { 
bindingStack  =  Arrays  .  copyOf  (  bindingStack  ,  2  *  bindingStackStored  )  ; 
} 
bindingStack  [  bindingStackStored  ]  =  null  ; 
bindingStackStored  ++  ; 
} 






public   void   popBindingSet  (  String   label  )  { 
bindingStackStored  --  ; 
IntArrayList   annotList  =  bindingStack  [  bindingStackStored  ]  ; 
if  (  annotList  !=  null  &&  !  annotList  .  isEmpty  (  )  )  { 
IntArrayList   annotSet  =  bindings  .  get  (  label  )  ; 
if  (  annotSet  ==  null  )  { 
annotSet  =  new   IntArrayList  (  )  ; 
} 
int   length  =  annotList  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
annotSet  .  add  (  annotList  .  get  (  i  )  )  ; 
} 
bindings  .  put  (  label  ,  annotSet  )  ; 
} 
} 




public   void   bindAnnotations  (  int  [  ]  aStep  )  { 
int   j  ; 
for  (  int   i  =  0  ;  i  <  bindingStackStored  ;  i  ++  )  { 
for  (  j  =  0  ;  j  <  aStep  .  length  ;  j  ++  )  { 
if  (  bindingStack  [  i  ]  ==  null  )  { 
bindingStack  [  i  ]  =  new   IntArrayList  (  )  ; 
} 
if  (  aStep  [  j  ]  >=  0  )  bindingStack  [  i  ]  .  add  (  aStep  [  j  ]  )  ; 
} 
} 
} 
} 




protected   Ontology   ontology  ; 




protected   String   inputASName  ; 




protected   AnnotationSet   inputAS  ; 




protected   String   outputASName  ; 




protected   Transducer   owner  ; 





protected   String  [  ]  inputAnnotationTypes  ; 





protected   final   String  [  ]  annotationTypes  ; 






protected   final   Predicate  [  ]  [  ]  predicatesByType  ; 

protected   final   String   phaseName  ; 







protected   static   final   int   ANNOTATION_TYPE_OTHER  =  -  2  ; 




protected   static   final   int   NOT_CALCULATED  =  -  1  ; 

private   static   Logger   logger  =  Logger  .  getLogger  (  SPTBase  .  class  )  ; 




public   static   enum   MatchMode  { 

APPELT  ,  BRILL  ,  ALL  ,  FIRST  ,  ONCE 
} 






protected   Annotation  [  ]  annotation  ; 






protected   int  [  ]  annotationType  ; 






protected   int  [  ]  annotationNextOffset  ; 







protected   long  [  ]  [  ]  annotationPredicateComputed  ; 








protected   long  [  ]  [  ]  annotationPredicateValues  ; 





protected   int  [  ]  annotationFollowing  ; 






protected   final   String  [  ]  bindingNames  ; 





protected   long   predicateHits  ; 





protected   long   predicateMisses  ; 

protected   NumberFormat   percentFormat  ; 




protected   final   Rule  [  ]  rules  ; 




protected   final   MatchMode   matchMode  ; 





protected   final   boolean   debugMode  ; 





protected   final   boolean   groupMatchingMode  ; 




protected   LinkedList  <  FSMInstance  >  activeInstances  ; 




protected   List  <  FSMInstance  >  acceptingInstances  ; 

protected   SPTBase  (  String   phaseName  ,  String  [  ]  bindingNames  ,  String  [  ]  annotationTypes  ,  boolean   debugMode  ,  boolean   groupMatchingMode  ,  MatchMode   matchMode  ,  Rule  [  ]  rules  ,  Predicate  [  ]  [  ]  predicatesByType  )  { 
percentFormat  =  NumberFormat  .  getPercentInstance  (  )  ; 
percentFormat  .  setMinimumFractionDigits  (  3  )  ; 
this  .  phaseName  =  phaseName  ; 
this  .  bindingNames  =  bindingNames  ; 
this  .  annotationTypes  =  annotationTypes  ; 
this  .  debugMode  =  debugMode  ; 
this  .  groupMatchingMode  =  groupMatchingMode  ; 
this  .  matchMode  =  matchMode  ; 
this  .  rules  =  rules  ; 
this  .  predicatesByType  =  predicatesByType  ; 
} 




protected   void   loadAnnotations  (  )  { 
inputAS  =  (  inputASName  ==  null  ||  inputASName  .  length  (  )  ==  0  )  ?  document  .  getAnnotations  (  )  :  document  .  getAnnotations  (  inputASName  )  ; 
if  (  inputAnnotationTypes  ==  null  ||  inputAnnotationTypes  .  length  ==  0  )  { 
Set  <  String  >  allTypes  =  inputAS  .  getAllTypes  (  )  ; 
inputAnnotationTypes  =  new   String  [  allTypes  .  size  (  )  ]  ; 
int   index  =  0  ; 
for  (  String   aType  :  allTypes  )  inputAnnotationTypes  [  index  ++  ]  =  aType  ; 
} 
int   annCount  =  0  ; 
final   Annotation  [  ]  [  ]  annotsByType  =  new   Annotation  [  inputAnnotationTypes  .  length  ]  [  ]  ; 
int   internalTypes  [  ]  =  new   int  [  inputAnnotationTypes  .  length  ]  ; 
for  (  int   type  =  0  ;  type  <  inputAnnotationTypes  .  length  ;  type  ++  )  { 
String   aType  =  inputAnnotationTypes  [  type  ]  ; 
internalTypes  [  type  ]  =  ANNOTATION_TYPE_OTHER  ; 
for  (  int   i  =  0  ;  i  <  annotationTypes  .  length  ;  i  ++  )  { 
if  (  annotationTypes  [  i  ]  .  equals  (  aType  )  )  { 
internalTypes  [  type  ]  =  i  ; 
break  ; 
} 
} 
Annotation  [  ]  ofOneType  =  owner  .  getSortedAnnotations  (  aType  )  ; 
annCount  +=  ofOneType  .  length  ; 
annotsByType  [  type  ]  =  ofOneType  ; 
} 
final   int  [  ]  typeIndex  =  new   int  [  annotsByType  .  length  ]  ; 
final   int  [  ]  typesOrder  =  new   int  [  annotsByType  .  length  ]  ; 
IntComparator   typeComparator  =  new   IntComparator  (  )  { 

public   int   compare  (  int   type1  ,  int   type2  )  { 
if  (  typeIndex  [  type1  ]  >=  annotsByType  [  type1  ]  .  length  )  { 
if  (  typeIndex  [  type2  ]  >=  annotsByType  [  type2  ]  .  length  )  { 
return   0  ; 
}  else  { 
return   1  ; 
} 
}  else   if  (  typeIndex  [  type2  ]  >=  annotsByType  [  type2  ]  .  length  )  { 
return  -  1  ; 
} 
Annotation   a1  =  annotsByType  [  type1  ]  [  typeIndex  [  type1  ]  ]  ; 
Annotation   a2  =  annotsByType  [  type2  ]  [  typeIndex  [  type2  ]  ]  ; 
return   owner  .  annotationComparator  .  compare  (  a1  ,  a2  )  ; 
} 
}  ; 
for  (  int   i  =  0  ;  i  <  typesOrder  .  length  ;  i  ++  )  { 
typesOrder  [  i  ]  =  i  ; 
} 
Sorting  .  quickSort  (  typesOrder  ,  0  ,  typesOrder  .  length  ,  typeComparator  )  ; 
annotation  =  new   Annotation  [  annCount  ]  ; 
annotationType  =  new   int  [  annCount  ]  ; 
int   annIdx  =  0  ; 
if  (  typesOrder  .  length  >  0  )  { 
while  (  true  )  { 
int   nextType  =  typesOrder  [  0  ]  ; 
if  (  typeIndex  [  nextType  ]  >=  annotsByType  [  nextType  ]  .  length  )  { 
if  (  annIdx  !=  annCount  )  { 
throw   new   RuntimeException  (  "Malfunction while building the annotation table: "  +  "sorted "  +  annIdx  +  " annotations instead of "  +  annCount  )  ; 
} 
break  ; 
} 
annotation  [  annIdx  ]  =  annotsByType  [  nextType  ]  [  typeIndex  [  nextType  ]  ]  ; 
typeIndex  [  nextType  ]  ++  ; 
annotationType  [  annIdx  ]  =  internalTypes  [  nextType  ]  ; 
annIdx  ++  ; 
int   insertPos  =  binarySearchFromTo  (  typesOrder  ,  1  ,  typesOrder  .  length  -  1  ,  typeComparator  )  ; 
if  (  insertPos  <  0  )  { 
insertPos  =  -  1  -  insertPos  ; 
} 
insertPos  -=  1  ; 
for  (  int   i  =  0  ;  i  <  insertPos  ;  i  ++  )  { 
typesOrder  [  i  ]  =  typesOrder  [  i  +  1  ]  ; 
} 
typesOrder  [  insertPos  ]  =  nextType  ; 
} 
} 
annotationFollowing  =  new   int  [  annCount  ]  ; 
for  (  int   i  =  0  ;  i  <  annotationFollowing  .  length  ;  i  ++  )  { 
annotationFollowing  [  i  ]  =  NOT_CALCULATED  ; 
} 
annotationNextOffset  =  new   int  [  annCount  ]  ; 
if  (  annCount  >  0  )  { 
long   currentOffset  =  annotation  [  0  ]  .  getStartNode  (  )  .  getOffset  (  )  ; 
int   currentOffsetStart  =  0  ; 
for  (  int   i  =  1  ;  i  <  annotation  .  length  ;  i  ++  )  { 
long   newOffset  =  annotation  [  i  ]  .  getStartNode  (  )  .  getOffset  (  )  ; 
if  (  newOffset  >  currentOffset  )  { 
for  (  int   j  =  currentOffsetStart  ;  j  <  i  ;  j  ++  )  { 
annotationNextOffset  [  j  ]  =  i  ; 
} 
currentOffset  =  newOffset  ; 
currentOffsetStart  =  i  ; 
} 
} 
for  (  int   j  =  currentOffsetStart  ;  j  <  annotation  .  length  ;  j  ++  )  { 
annotationNextOffset  [  j  ]  =  Integer  .  MAX_VALUE  ; 
} 
} 
annotationPredicateComputed  =  new   long  [  annCount  ]  [  ]  ; 
annotationPredicateValues  =  new   long  [  annCount  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  annotation  .  length  ;  i  ++  )  { 
if  (  annotationType  [  i  ]  >=  0  &&  predicatesByType  [  annotationType  [  i  ]  ]  !=  null  )  { 
annotationPredicateComputed  [  i  ]  =  QuickBitVector  .  makeBitVector  (  predicatesByType  [  annotationType  [  i  ]  ]  .  length  ,  1  )  ; 
annotationPredicateValues  [  i  ]  =  QuickBitVector  .  makeBitVector  (  predicatesByType  [  annotationType  [  i  ]  ]  .  length  ,  1  )  ; 
} 
} 
} 

protected   static   int   binarySearchFromTo  (  int  [  ]  array  ,  int   from  ,  int   to  ,  IntComparator   comp  )  { 
final   int   key  =  0  ; 
while  (  from  <=  to  )  { 
int   mid  =  (  from  +  to  )  /  2  ; 
int   comparison  =  comp  .  compare  (  array  [  mid  ]  ,  array  [  key  ]  )  ; 
if  (  comparison  <  0  )  from  =  mid  +  1  ;  else   if  (  comparison  >  0  )  to  =  mid  -  1  ;  else   return   mid  ; 
} 
return  -  (  from  +  1  )  ; 
} 














protected   int   followingAnnotation  (  int   idx  )  { 
if  (  annotationFollowing  [  idx  ]  ==  NOT_CALCULATED  )  { 
long   endOffset  =  annotation  [  idx  ]  .  getEndNode  (  )  .  getOffset  (  )  ; 
int   from  =  idx  ; 
int   to  =  annotation  .  length  -  1  ; 
while  (  from  <=  to  )  { 
int   mid  =  (  from  +  to  )  /  2  ; 
long   midStartOffset  =  annotation  [  mid  ]  .  getStartNode  (  )  .  getOffset  (  )  ; 
if  (  midStartOffset  >  endOffset  )  { 
while  (  mid  >  from  &&  annotationNextOffset  [  mid  -  1  ]  ==  annotationNextOffset  [  mid  ]  )  { 
mid  --  ; 
} 
if  (  annotation  [  mid  -  1  ]  .  getStartNode  (  )  .  getOffset  (  )  <  endOffset  )  { 
annotationFollowing  [  idx  ]  =  mid  ; 
break  ; 
}  else  { 
to  =  mid  -  1  ; 
} 
}  else   if  (  midStartOffset  <  endOffset  )  { 
mid  =  annotationNextOffset  [  mid  ]  ; 
if  (  mid  >  annotation  .  length  )  { 
annotationFollowing  [  idx  ]  =  Integer  .  MAX_VALUE  ; 
break  ; 
}  else   if  (  annotation  [  mid  ]  .  getStartNode  (  )  .  getOffset  (  )  >  endOffset  )  { 
annotationFollowing  [  idx  ]  =  mid  ; 
break  ; 
}  else  { 
from  =  mid  ; 
} 
}  else  { 
while  (  mid  >  from  &&  annotation  [  mid  -  1  ]  .  getStartNode  (  )  .  getOffset  (  )  ==  midStartOffset  )  { 
mid  --  ; 
} 
annotationFollowing  [  idx  ]  =  mid  ; 
break  ; 
} 
} 
if  (  annotationFollowing  [  idx  ]  ==  NOT_CALCULATED  )  { 
if  (  from  <  annotation  .  length  )  { 
annotationFollowing  [  idx  ]  =  from  ; 
}  else  { 
annotationFollowing  [  idx  ]  =  Integer  .  MAX_VALUE  ; 
} 
} 
} 
return   annotationFollowing  [  idx  ]  ; 
} 
















protected   boolean   checkPredicate  (  int   annotationId  ,  int   predicateId  )  throws   JapeException  { 
if  (  !  QuickBitVector  .  get  (  annotationPredicateComputed  [  annotationId  ]  ,  predicateId  )  )  { 
predicateMisses  ++  ; 
Predicate   predicate  =  predicatesByType  [  annotationType  [  annotationId  ]  ]  [  predicateId  ]  ; 
boolean   result  =  calculatePredicateValue  (  annotationId  ,  predicateId  )  ; 
QuickBitVector  .  set  (  annotationPredicateComputed  [  annotationId  ]  ,  predicateId  )  ; 
if  (  result  )  { 
QuickBitVector  .  set  (  annotationPredicateValues  [  annotationId  ]  ,  predicateId  )  ; 
for  (  int   otherPred  :  predicate  .  alsoTrue  )  { 
QuickBitVector  .  set  (  annotationPredicateComputed  [  annotationId  ]  ,  otherPred  )  ; 
QuickBitVector  .  set  (  annotationPredicateValues  [  annotationId  ]  ,  otherPred  )  ; 
} 
for  (  int   otherPred  :  predicate  .  converselyFalse  )  { 
QuickBitVector  .  set  (  annotationPredicateComputed  [  annotationId  ]  ,  otherPred  )  ; 
} 
}  else  { 
for  (  int   otherPred  :  predicate  .  alsoFalse  )  { 
QuickBitVector  .  set  (  annotationPredicateComputed  [  annotationId  ]  ,  otherPred  )  ; 
} 
for  (  int   otherPred  :  predicate  .  converselyTrue  )  { 
QuickBitVector  .  set  (  annotationPredicateComputed  [  annotationId  ]  ,  otherPred  )  ; 
QuickBitVector  .  set  (  annotationPredicateValues  [  annotationId  ]  ,  otherPred  )  ; 
} 
} 
}  else  { 
predicateHits  ++  ; 
} 
return   QuickBitVector  .  get  (  annotationPredicateValues  [  annotationId  ]  ,  predicateId  )  ; 
} 

protected   boolean   calculatePredicateValue  (  int   annotationId  ,  int   predicateId  )  throws   JapeException  { 
Predicate   predicate  =  predicatesByType  [  annotationType  [  annotationId  ]  ]  [  predicateId  ]  ; 
boolean   result  =  false  ; 
Object   actualValue  =  null  ; 
if  (  predicate  .  annotationAccessor  !=  null  )  { 
actualValue  =  predicate  .  annotationAccessor  .  getValue  (  annotation  [  annotationId  ]  ,  inputAS  )  ; 
if  (  actualValue  !=  null  )  { 
if  (  predicate  .  featureValue   instanceof   String  )  { 
if  (  !  (  actualValue   instanceof   String  )  )  { 
actualValue  =  actualValue  .  toString  (  )  ; 
} 
}  else   if  (  predicate  .  featureValue   instanceof   Long  )  { 
if  (  !  (  actualValue   instanceof   Long  )  )  { 
try  { 
actualValue  =  new   Long  (  actualValue  .  toString  (  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
logger  .  warn  (  "Could not convert value \""  +  actualValue  .  toString  (  )  +  "\" of feature \""  +  predicate  .  annotationAccessor  +  "\" to a Long. All constraint "  +  "checks will use \"null\" instead."  )  ; 
actualValue  =  null  ; 
} 
} 
}  else   if  (  predicate  .  featureValue   instanceof   Double  )  { 
if  (  !  (  actualValue   instanceof   Double  )  )  { 
try  { 
actualValue  =  new   Double  (  actualValue  .  toString  (  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
logger  .  warn  (  "Could not convert value \""  +  actualValue  .  toString  (  )  +  "\" of feature \""  +  predicate  .  annotationAccessor  +  "\" to a Double. All constraint "  +  "checks will use \"null\" instead."  )  ; 
actualValue  =  null  ; 
} 
} 
} 
} 
} 
predtype  :  switch  (  predicate  .  type  )  { 
case   EQ  : 
if  (  ontology  !=  null  &&  actualValue  !=  null  &&  predicate  .  annotationAccessor  .  getKey  (  )  .  toString  (  )  .  equals  (  ANNIEConstants  .  LOOKUP_CLASS_FEATURE_NAME  )  )  { 
OResource   superClass  =  null  ; 
List  <  OResource  >  classes  =  ontology  .  getOResourcesByName  (  predicate  .  featureValue  .  toString  (  )  )  ; 
if  (  classes  .  isEmpty  (  )  )  { 
superClass  =  null  ; 
}  else  { 
superClass  =  classes  .  get  (  0  )  ; 
if  (  classes  .  size  (  )  >  1  )  { 
StringBuilder   str  =  new   StringBuilder  (  "Ontology class name \""  +  predicate  .  featureValue  .  toString  (  )  +  "\" is ambiguous: ["  )  ; 
boolean   first  =  true  ; 
for  (  OResource   aClass  :  classes  )  { 
if  (  first  )  { 
first  =  false  ; 
}  else  { 
str  .  append  (  ", "  )  ; 
} 
str  .  append  (  aClass  .  getONodeID  (  )  .  getNameSpace  (  )  )  ; 
str  .  append  (  aClass  .  getONodeID  (  )  .  getResourceName  (  )  )  ; 
} 
str  .  append  (  "]."  )  ; 
logger  .  warn  (  str  .  toString  (  )  )  ; 
} 
} 
OResource   subClass  =  null  ; 
classes  =  ontology  .  getOResourcesByName  (  actualValue  .  toString  (  )  )  ; 
if  (  classes  .  isEmpty  (  )  )  { 
subClass  =  null  ; 
}  else  { 
subClass  =  classes  .  get  (  0  )  ; 
if  (  classes  .  size  (  )  >  1  )  { 
StringBuilder   str  =  new   StringBuilder  (  "Ontology class name \""  +  predicate  .  featureValue  .  toString  (  )  +  "\" is ambiguous: ["  )  ; 
boolean   first  =  true  ; 
for  (  OResource   aClass  :  classes  )  { 
if  (  first  )  { 
first  =  false  ; 
}  else  { 
str  .  append  (  ", "  )  ; 
} 
str  .  append  (  aClass  .  getONodeID  (  )  .  getNameSpace  (  )  )  ; 
str  .  append  (  aClass  .  getONodeID  (  )  .  getResourceName  (  )  )  ; 
} 
str  .  append  (  "]."  )  ; 
logger  .  warn  (  str  .  toString  (  )  )  ; 
} 
} 
if  (  superClass  ==  null  ||  !  (  superClass   instanceof   OClass  )  ||  subClass  ==  null  ||  !  (  subClass   instanceof   OClass  )  )  { 
result  =  false  ; 
}  else  { 
result  =  subClass  ==  superClass  ||  (  (  OClass  )  subClass  )  .  isSubClassOf  (  (  OClass  )  superClass  ,  Closure  .  TRANSITIVE_CLOSURE  )  ; 
} 
}  else  { 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  ==  0  ; 
} 
} 
break  ; 
case   NOT_EQ  : 
if  (  actualValue  ==  null  )  { 
result  =  true  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  !=  0  ; 
} 
break  ; 
case   LT  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  >  0  ; 
} 
break  ; 
case   GT  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  <  0  ; 
} 
break  ; 
case   LE  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  >=  0  ; 
} 
break  ; 
case   GE  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Comparable  )  predicate  .  featureValue  )  .  compareTo  (  actualValue  )  <=  0  ; 
} 
break  ; 
case   REGEX_FIND  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Pattern  )  predicate  .  featureValue  )  .  matcher  (  (  String  )  actualValue  )  .  find  (  )  ; 
} 
break  ; 
case   REGEX_MATCH  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  (  (  Pattern  )  predicate  .  featureValue  )  .  matcher  (  (  String  )  actualValue  )  .  matches  (  )  ; 
} 
break  ; 
case   REGEX_NOT_FIND  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  !  (  (  Pattern  )  predicate  .  featureValue  )  .  matcher  (  (  String  )  actualValue  )  .  find  (  )  ; 
} 
break  ; 
case   REGEX_NOT_MATCH  : 
if  (  actualValue  ==  null  )  { 
result  =  false  ; 
}  else  { 
result  =  !  (  (  Pattern  )  predicate  .  featureValue  )  .  matcher  (  (  String  )  actualValue  )  .  matches  (  )  ; 
} 
break  ; 
case   CONTAINS  : 
{ 
int  [  ]  constraints  =  (  int  [  ]  )  predicate  .  featureValue  ; 
int   currAnnIdx  =  annotationId  ; 
long   startOffset  =  annotation  [  annotationId  ]  .  getStartNode  (  )  .  getOffset  (  )  ; 
long   endOffset  =  annotation  [  annotationId  ]  .  getEndNode  (  )  .  getOffset  (  )  ; 
while  (  currAnnIdx  >  0  &&  annotation  [  currAnnIdx  -  1  ]  .  getStartNode  (  )  .  getOffset  (  )  ==  startOffset  )  { 
currAnnIdx  --  ; 
} 
while  (  currAnnIdx  <  annotation  .  length  &&  annotation  [  currAnnIdx  ]  .  getStartNode  (  )  .  getOffset  (  )  <=  endOffset  )  { 
if  (  annotationType  [  currAnnIdx  ]  ==  constraints  [  0  ]  &&  annotation  [  currAnnIdx  ]  .  getEndNode  (  )  .  getOffset  (  )  <=  endOffset  )  { 
boolean   predicatesHappy  =  true  ; 
for  (  int   predIdx  =  2  ;  predIdx  <  constraints  .  length  &&  predicatesHappy  ;  predIdx  ++  )  { 
predicatesHappy  &=  (  checkPredicate  (  currAnnIdx  ,  constraints  [  predIdx  ]  )  )  ; 
} 
if  (  (  constraints  [  1  ]  >=  0  &&  predicatesHappy  )  ||  (  constraints  [  1  ]  <  0  &&  !  predicatesHappy  )  )  { 
result  =  true  ; 
break   predtype  ; 
} 
} 
currAnnIdx  ++  ; 
} 
} 
break  ; 
case   WITHIN  : 
{ 
int  [  ]  constraints  =  (  int  [  ]  )  predicate  .  featureValue  ; 
int   currAnnIdx  =  0  ; 
int   maxCurrAnn  =  annotationNextOffset  [  annotationId  ]  ; 
if  (  maxCurrAnn  >=  annotation  .  length  )  maxCurrAnn  =  annotation  .  length  ; 
long   endOffset  =  annotation  [  annotationId  ]  .  getEndNode  (  )  .  getOffset  (  )  ; 
while  (  currAnnIdx  <  maxCurrAnn  )  { 
if  (  annotationType  [  currAnnIdx  ]  ==  constraints  [  0  ]  &&  annotation  [  currAnnIdx  ]  .  getEndNode  (  )  .  getOffset  (  )  >=  endOffset  )  { 
boolean   predicatesHappy  =  true  ; 
for  (  int   predIdx  =  2  ;  predIdx  <  constraints  .  length  &&  predicatesHappy  ;  predIdx  ++  )  { 
predicatesHappy  &=  (  checkPredicate  (  currAnnIdx  ,  constraints  [  predIdx  ]  )  )  ; 
} 
if  (  (  constraints  [  1  ]  >=  0  &&  predicatesHappy  )  ||  (  constraints  [  1  ]  <  0  &&  !  predicatesHappy  )  )  { 
result  =  true  ; 
break   predtype  ; 
} 
} 
currAnnIdx  ++  ; 
} 
} 
break  ; 
case   CUSTOM  : 
ConstraintPredicate   actualConstraint  =  (  ConstraintPredicate  )  predicate  .  featureValue  ; 
result  =  actualConstraint  .  matches  (  annotation  [  annotationId  ]  ,  inputAS  )  ; 
break  ; 
default  : 
throw   new   IllegalArgumentException  (  "Predicate type "  +  predicate  .  type  +  " not implemented"  )  ; 
} 
return   result  ; 
} 






public   void   execute  (  )  throws   ExecutionException  { 
fireProgressChanged  (  0  )  ; 
int   lastProgressReportAnnIdx  =  0  ; 
loadAnnotations  (  )  ; 
predicateHits  =  0  ; 
predicateMisses  =  0  ; 
activeInstances  =  new   LinkedList  <  FSMInstance  >  (  )  ; 
acceptingInstances  =  new   ArrayList  <  FSMInstance  >  (  )  ; 
int   currentAnnotation  =  0  ; 
topWhile  :  while  (  currentAnnotation  <  annotation  .  length  )  { 
activeInstances  .  add  (  new   FSMInstance  (  currentAnnotation  ,  0  ,  new   HashMap  <  String  ,  IntArrayList  >  (  )  )  )  ; 
instances  :  while  (  activeInstances  .  size  (  )  >  0  )  { 
if  (  owner  .  isInterrupted  (  )  )  throw   new   ExecutionInterruptedException  (  "The execution of the \""  +  getName  (  )  +  "\" JAPE Plus transducer has been interrupted!"  )  ; 
FSMInstance   fsmInstance  =  activeInstances  .  removeFirst  (  )  ; 
try  { 
if  (  advanceInstance  (  fsmInstance  )  )  break   instances  ; 
}  catch  (  JapeException   e  )  { 
throw   new   ExecutionException  (  e  )  ; 
} 
} 
int   oldCurrAnn  =  currentAnnotation  ; 
if  (  acceptingInstances  .  size  (  )  >  0  )  { 
try  { 
switch  (  matchMode  )  { 
case   APPELT  : 
Collections  .  sort  (  acceptingInstances  )  ; 
FSMInstance   topInstance  =  acceptingInstances  .  get  (  0  )  ; 
currentAnnotation  =  topInstance  .  annotationIndex  ; 
applyRule  (  topInstance  )  ; 
if  (  debugMode  ||  groupMatchingMode  )  { 
List  <  FSMInstance  >  otherEqualInstances  =  new   LinkedList  <  FSMInstance  >  (  )  ; 
int   i  =  1  ; 
while  (  i  <  acceptingInstances  .  size  (  )  &&  topInstance  .  compareTo  (  acceptingInstances  .  get  (  i  )  )  ==  0  )  { 
otherEqualInstances  .  add  (  acceptingInstances  .  get  (  i  ++  )  )  ; 
} 
if  (  otherEqualInstances  .  size  (  )  >  0  )  { 
if  (  debugMode  )  { 
logger  .  warn  (  "Multiple equivalent matches in Appelt mode!"  )  ; 
} 
if  (  groupMatchingMode  )  { 
for  (  FSMInstance   anInstance  :  otherEqualInstances  )  { 
applyRule  (  anInstance  )  ; 
} 
} 
} 
} 
break  ; 
case   BRILL  : 
int   maxNext  =  Integer  .  MIN_VALUE  ; 
for  (  FSMInstance   anInstance  :  acceptingInstances  )  { 
applyRule  (  anInstance  )  ; 
if  (  anInstance  .  annotationIndex  >  maxNext  )  { 
maxNext  =  anInstance  .  annotationIndex  ; 
} 
} 
currentAnnotation  =  maxNext  ; 
break  ; 
case   ALL  : 
for  (  FSMInstance   anInstance  :  acceptingInstances  )  { 
applyRule  (  anInstance  )  ; 
} 
currentAnnotation  =  annotationNextOffset  [  currentAnnotation  ]  ; 
break  ; 
case   FIRST  : 
FSMInstance   anInstance  =  acceptingInstances  .  get  (  0  )  ; 
applyRule  (  anInstance  )  ; 
currentAnnotation  =  anInstance  .  annotationIndex  ; 
activeInstances  .  clear  (  )  ; 
break  ; 
case   ONCE  : 
applyRule  (  acceptingInstances  .  get  (  0  )  )  ; 
break   topWhile  ; 
} 
acceptingInstances  .  clear  (  )  ; 
if  (  currentAnnotation  !=  Integer  .  MAX_VALUE  &&  annotation  [  oldCurrAnn  ]  .  getStartNode  (  )  .  getOffset  (  )  ==  annotation  [  currentAnnotation  ]  .  getStartNode  (  )  .  getOffset  (  )  )  { 
currentAnnotation  =  annotationNextOffset  [  currentAnnotation  ]  ; 
} 
}  catch  (  JapeException   e  )  { 
throw   new   ExecutionException  (  e  )  ; 
} 
}  else  { 
currentAnnotation  =  annotationNextOffset  [  currentAnnotation  ]  ; 
} 
if  (  (  (  DefaultActionContext  )  actionContext  )  .  isPhaseEnded  (  )  )  { 
(  (  DefaultActionContext  )  actionContext  )  .  setPhaseEnded  (  false  )  ; 
logger  .  debug  (  "Phase \""  +  phaseName  +  "\" terminated prematurely by a RHS action."  )  ; 
break   topWhile  ; 
} 
if  (  currentAnnotation  -  lastProgressReportAnnIdx  >  annotation  .  length  /  10  )  { 
fireProgressChanged  (  currentAnnotation  *  100  /  annotation  .  length  )  ; 
lastProgressReportAnnIdx  =  currentAnnotation  ; 
} 
} 
fireProcessFinished  (  )  ; 
annotation  =  null  ; 
annotationFollowing  =  null  ; 
annotationNextOffset  =  null  ; 
annotationPredicateComputed  =  null  ; 
annotationPredicateValues  =  null  ; 
annotationType  =  null  ; 
acceptingInstances  =  null  ; 
activeInstances  =  null  ; 
ontology  =  null  ; 
} 

protected   void   generateAllNewInstances  (  FSMInstance   instance  ,  int   nextState  ,  IntArrayList  [  ]  annotsForConstraints  )  { 
List  <  int  [  ]  >  nextSteps  =  enumerateCombinations  (  annotsForConstraints  )  ; 
for  (  int  [  ]  aStep  :  nextSteps  )  { 
FSMInstance   nextInstance  =  instance  .  clone  (  )  ; 
nextInstance  .  state  =  nextState  ; 
int   nextAnnotationForInstance  =  instance  .  annotationIndex  ; 
int   maxNextStep  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  aStep  .  length  ;  i  ++  )  { 
if  (  aStep  [  i  ]  >=  0  )  { 
if  (  maxNextStep  <  aStep  [  i  ]  )  maxNextStep  =  aStep  [  i  ]  ; 
if  (  nextAnnotationForInstance  <  followingAnnotation  (  aStep  [  i  ]  )  )  { 
nextAnnotationForInstance  =  followingAnnotation  (  aStep  [  i  ]  )  ; 
} 
} 
} 
if  (  nextAnnotationForInstance  <=  maxNextStep  )  { 
if  (  maxNextStep  <  annotation  .  length  -  2  )  { 
nextAnnotationForInstance  =  maxNextStep  +  1  ; 
}  else  { 
nextAnnotationForInstance  =  Integer  .  MAX_VALUE  ; 
} 
} 
nextInstance  .  annotationIndex  =  nextAnnotationForInstance  ; 
nextInstance  .  bindAnnotations  (  aStep  )  ; 
activeInstances  .  addLast  (  nextInstance  )  ; 
} 
} 

protected   void   applyRule  (  FSMInstance   instance  )  throws   JapeException  { 
Map  <  String  ,  AnnotationSet  >  newBindings  =  new   HashMap  <  String  ,  AnnotationSet  >  (  instance  .  bindings  .  size  (  )  )  ; 
for  (  Map  .  Entry  <  String  ,  IntArrayList  >  entry  :  instance  .  bindings  .  entrySet  (  )  )  { 
AnnotationSet   boundAnnots  =  new   AnnotationSetImpl  (  document  )  ; 
for  (  int   i  =  0  ;  i  <  entry  .  getValue  (  )  .  size  (  )  ;  i  ++  )  { 
boundAnnots  .  add  (  annotation  [  entry  .  getValue  (  )  .  getQuick  (  i  )  ]  )  ; 
} 
newBindings  .  put  (  entry  .  getKey  (  )  ,  boundAnnots  )  ; 
} 
rules  [  instance  .  rule  ]  .  getRHS  (  )  .  transduce  (  document  ,  newBindings  ,  document  .  getAnnotations  (  inputASName  )  ,  document  .  getAnnotations  (  outputASName  )  ,  ontology  ,  actionContext  )  ; 
} 










protected   static   final   List  <  int  [  ]  >  enumerateCombinations  (  IntArrayList  [  ]  candidates  )  { 
List  <  int  [  ]  >  combinations  =  new   ArrayList  <  int  [  ]  >  (  )  ; 
int  [  ]  indexes  =  new   int  [  candidates  .  length  ]  ; 
int   currentIndex  =  indexes  .  length  ; 
while  (  true  )  { 
if  (  currentIndex  ==  indexes  .  length  )  { 
int  [  ]  aSolution  =  new   int  [  indexes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  indexes  .  length  ;  i  ++  )  { 
aSolution  [  i  ]  =  candidates  [  i  ]  .  get  (  indexes  [  i  ]  )  ; 
} 
combinations  .  add  (  aSolution  )  ; 
currentIndex  --  ; 
}  else   if  (  currentIndex  <  0  )  { 
break  ; 
}  else  { 
if  (  indexes  [  currentIndex  ]  <  candidates  [  currentIndex  ]  .  size  (  )  -  1  )  { 
indexes  [  currentIndex  ]  ++  ; 
currentIndex  ++  ; 
}  else  { 
indexes  [  currentIndex  ]  =  -  1  ; 
currentIndex  --  ; 
} 
} 
} 
return   combinations  ; 
} 




public   String   getInputASName  (  )  { 
return   inputASName  ; 
} 




public   void   setInputASName  (  String   inputASName  )  { 
this  .  inputASName  =  inputASName  ; 
} 




public   String   getOutputASName  (  )  { 
return   outputASName  ; 
} 




public   void   setOutputASName  (  String   outputASName  )  { 
this  .  outputASName  =  outputASName  ; 
} 

public   void   setOwner  (  Transducer   owner  )  { 
this  .  owner  =  owner  ; 
} 

public   void   setOntology  (  Ontology   onto  )  { 
ontology  =  onto  ; 
} 

ControllerEventBlocksAction   actionblocks  ; 

public   void   setControllerEventBlocksAction  (  ControllerEventBlocksAction   abs  )  { 
this  .  actionblocks  =  abs  ; 
} 

protected   void   prepareControllerEventBlocksAction  (  ActionContext   ac  ,  Controller   c  ,  Ontology   o  )  { 
if  (  actionblocks  ==  null  )  { 
return  ; 
} 
actionblocks  .  setController  (  c  )  ; 
actionblocks  .  setOntology  (  null  )  ; 
actionblocks  .  setActionContext  (  ac  )  ; 
if  (  c   instanceof   CorpusController  )  { 
Corpus   corpus  =  (  (  CorpusController  )  c  )  .  getCorpus  (  )  ; 
actionblocks  .  setCorpus  (  corpus  )  ; 
}  else  { 
actionblocks  .  setCorpus  (  null  )  ; 
} 
actionblocks  .  setThrowable  (  null  )  ; 
} 

protected   void   runControllerExecutionStartedBlock  (  ActionContext   ac  ,  Controller   c  ,  Ontology   o  )  throws   ExecutionException  { 
if  (  actionblocks  ==  null  )  { 
return  ; 
} 
prepareControllerEventBlocksAction  (  ac  ,  c  ,  o  )  ; 
try  { 
actionblocks  .  controllerExecutionStarted  (  )  ; 
}  catch  (  Throwable   e  )  { 
if  (  e   instanceof   Error  )  { 
throw  (  Error  )  e  ; 
} 
if  (  e   instanceof   RuntimeException  )  { 
throw  (  RuntimeException  )  e  ; 
} 
throw   new   ExecutionException  (  "Couldn't run controller started action"  ,  e  )  ; 
} 
} 

protected   void   runControllerExecutionFinishedBlock  (  ActionContext   ac  ,  Controller   c  ,  Ontology   o  )  throws   ExecutionException  { 
if  (  actionblocks  ==  null  )  { 
return  ; 
} 
prepareControllerEventBlocksAction  (  ac  ,  c  ,  o  )  ; 
try  { 
actionblocks  .  controllerExecutionFinished  (  )  ; 
}  catch  (  Throwable   e  )  { 
if  (  e   instanceof   Error  )  { 
throw  (  Error  )  e  ; 
} 
if  (  e   instanceof   RuntimeException  )  { 
throw  (  RuntimeException  )  e  ; 
} 
throw   new   ExecutionException  (  "Couldn't run controller finished action"  ,  e  )  ; 
} 
} 

protected   void   runControllerExecutionAbortedBlock  (  ActionContext   ac  ,  Controller   c  ,  Throwable   t  ,  Ontology   o  )  throws   ExecutionException  { 
if  (  actionblocks  ==  null  )  { 
return  ; 
} 
prepareControllerEventBlocksAction  (  ac  ,  c  ,  o  )  ; 
actionblocks  .  setThrowable  (  t  )  ; 
try  { 
actionblocks  .  controllerExecutionFinished  (  )  ; 
}  catch  (  Throwable   e  )  { 
if  (  e   instanceof   Error  )  { 
throw  (  Error  )  e  ; 
} 
if  (  e   instanceof   RuntimeException  )  { 
throw  (  RuntimeException  )  e  ; 
} 
throw   new   ExecutionException  (  "Couldn't run controller aborted action"  ,  e  )  ; 
} 
} 
} 

