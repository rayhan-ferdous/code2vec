package   randoop  .  util  ; 

import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashMap  ; 
import   java  .  util  .  LinkedHashSet  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  SortedSet  ; 
import   java  .  util  .  TreeSet  ; 
import   randoop  .  Globals  ; 
import   plume  .  Hasher  ; 

public   final   class   CollectionsExt  { 

private   CollectionsExt  (  )  { 
throw   new   IllegalStateException  (  "no instances"  )  ; 
} 





public   static  <  T  >  List  <  T  >  unique  (  List  <  T  >  lst  )  { 
return   new   ArrayList  <  T  >  (  new   LinkedHashSet  <  T  >  (  lst  )  )  ; 
} 




public   static  <  T  >  List  <  T  >  roCopyList  (  Collection  <  T  >  l  )  { 
return   Collections  .  unmodifiableList  (  new   ArrayList  <  T  >  (  l  )  )  ; 
} 

public   static  <  T  >  Set  <  T  >  intersection  (  Set  <  ?  extends   T  >  ...  sets  )  { 
if  (  sets  .  length  ==  0  )  return   Collections  .  emptySet  (  )  ; 
Set  <  T  >  result  =  new   LinkedHashSet  <  T  >  (  )  ; 
result  .  addAll  (  sets  [  0  ]  )  ; 
for  (  Set  <  ?  extends   T  >  s  :  sets  )  { 
result  .  retainAll  (  s  )  ; 
} 
return   result  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  Set  <  T  >  xor  (  Set  <  ?  extends   T  >  s1  ,  Set  <  ?  extends   T  >  s2  )  { 
return   union  (  diff  (  s1  ,  s2  )  ,  diff  (  s2  ,  s1  )  )  ; 
} 

public   static  <  T  >  Set  <  T  >  diff  (  Set  <  ?  extends   T  >  s1  ,  Set  <  ?  extends   T  >  s2  )  { 
Set  <  T  >  result  =  new   LinkedHashSet  <  T  >  (  )  ; 
result  .  addAll  (  s1  )  ; 
result  .  removeAll  (  s2  )  ; 
return   result  ; 
} 

public   static  <  T  >  Set  <  T  >  union  (  Set  <  ?  extends   T  >  ...  sets  )  { 
Set  <  T  >  result  =  new   LinkedHashSet  <  T  >  (  )  ; 
for  (  Set  <  ?  extends   T  >  s  :  sets  )  { 
result  .  addAll  (  s  )  ; 
} 
return   result  ; 
} 





public   static  <  T  >  List  <  T  >  concat  (  List  <  ?  extends   T  >  ...  lists  )  { 
List  <  T  >  result  =  new   ArrayList  <  T  >  (  )  ; 
for  (  List  <  ?  extends   T  >  list  :  lists  )  { 
result  .  addAll  (  list  )  ; 
} 
return   result  ; 
} 





public   static  <  T  >  List  <  T  >  concatAll  (  Collection  <  ?  extends   List  <  ?  extends   T  >  >  lists  )  { 
List  <  T  >  result  =  new   ArrayList  <  T  >  (  )  ; 
for  (  List  <  ?  extends   T  >  list  :  lists  )  { 
result  .  addAll  (  list  )  ; 
} 
return   result  ; 
} 

public   static  <  T  >  T   getNthIteratedElement  (  Collection  <  ?  extends   T  >  s  ,  int   index  )  { 
if  (  s  ==  null  )  throw   new   IllegalArgumentException  (  "s cannot be null."  )  ; 
if  (  s  .  isEmpty  (  )  )  throw   new   IllegalArgumentException  (  "s cannot be empty."  )  ; 
if  (  index  >=  s  .  size  (  )  )  throw   new   IllegalArgumentException  (  "Index "  +  index  +  " invalid for set of size "  +  s  .  size  (  )  )  ; 
return   getNthIteratedElement  (  s  .  iterator  (  )  ,  index  )  ; 
} 

public   static  <  T  >  T   getNthIteratedElement  (  Iterator  <  ?  extends   T  >  iter  ,  int   index  )  { 
if  (  index  <  0  )  throw   new   IllegalArgumentException  (  "Index "  +  index  +  " invalid"  )  ; 
int   counter  =  0  ; 
for  (  Iterator  <  ?  extends   T  >  i  =  iter  ;  i  .  hasNext  (  )  ;  )  { 
if  (  counter  ==  index  )  { 
return   i  .  next  (  )  ; 
} 
i  .  next  (  )  ; 
counter  ++  ; 
} 
throw   new   IllegalArgumentException  (  "invalid index:"  +  index  +  " size:"  +  counter  )  ; 
} 

public   static   SortedSet  <  Integer  >  findAll  (  List  <  ?  >  list  ,  Object   elem  )  { 
if  (  list  ==  null  )  throw   new   IllegalArgumentException  (  "list cannot be null."  )  ; 
SortedSet  <  Integer  >  result  =  new   TreeSet  <  Integer  >  (  )  ; 
for  (  int   i  =  0  ,  n  =  list  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
if  (  list  .  get  (  i  )  .  equals  (  elem  )  )  { 
result  .  add  (  i  )  ; 
} 
} 
return   Collections  .  unmodifiableSortedSet  (  result  )  ; 
} 





public   static   String   toStringInLines  (  Collection  <  ?  >  c  )  { 
if  (  c  .  isEmpty  (  )  )  return  ""  ; 
return   join  (  Globals  .  lineSep  ,  toStringLines  (  c  )  )  +  Globals  .  lineSep  ; 
} 





public   static   String   toStringInSortedLines  (  Collection  <  ?  >  c  )  { 
if  (  c  .  isEmpty  (  )  )  return  ""  ; 
return   join  (  Globals  .  lineSep  ,  sort  (  toStringLines  (  c  )  )  )  +  Globals  .  lineSep  ; 
} 





public   static   List  <  String  >  toStringLines  (  Collection  <  ?  >  c  )  { 
List  <  String  >  lines  =  new   ArrayList  <  String  >  (  c  .  size  (  )  )  ; 
for  (  Object   each  :  c  )  { 
lines  .  add  (  String  .  valueOf  (  each  )  )  ; 
} 
return   lines  ; 
} 





public   static   List  <  String  >  sort  (  List  <  String  >  strings  )  { 
Collections  .  sort  (  strings  )  ; 
return   strings  ; 
} 









public   static  <  T  >  List  <  List  <  T  >  >  chunkUp  (  List  <  T  >  list  ,  int   maxLength  )  { 
if  (  maxLength  <=  0  )  throw   new   IllegalArgumentException  (  "maxLength must be > 0 but was "  +  maxLength  )  ; 
int   fullChunks  =  list  .  size  (  )  /  maxLength  ; 
List  <  List  <  T  >  >  result  =  new   ArrayList  <  List  <  T  >  >  (  fullChunks  +  1  )  ; 
for  (  int   i  =  0  ;  i  <  fullChunks  ;  i  ++  )  { 
List  <  T  >  subList  =  list  .  subList  (  i  *  maxLength  ,  (  i  +  1  )  *  maxLength  )  ; 
if  (  subList  .  size  (  )  !=  maxLength  )  throw   new   IllegalStateException  (  "bogus length:"  +  subList  .  size  (  )  +  " not "  +  maxLength  )  ; 
result  .  add  (  subList  )  ; 
} 
List  <  T  >  lastChunk  =  list  .  subList  (  fullChunks  *  maxLength  ,  list  .  size  (  )  )  ; 
if  (  !  lastChunk  .  isEmpty  (  )  )  result  .  add  (  lastChunk  )  ; 
return   Collections  .  unmodifiableList  (  result  )  ; 
} 

public   static  <  T  >  Set  <  T  >  getAll  (  Enumeration  <  T  >  e  )  { 
Set  <  T  >  result  =  new   LinkedHashSet  <  T  >  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  result  .  add  (  e  .  nextElement  (  )  )  ; 
return   result  ; 
} 





public   static  <  T   extends   Collection  <  String  >  >  T   removeMatching  (  String   pattern  ,  T   strings  )  { 
for  (  Iterator  <  String  >  iter  =  strings  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
String   s  =  iter  .  next  (  )  ; 
if  (  s  .  matches  (  pattern  )  )  iter  .  remove  (  )  ; 
} 
return   strings  ; 
} 





public   static   String   join  (  String   separator  ,  List  <  String  >  strings  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
for  (  Iterator  <  String  >  iter  =  strings  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
String   s  =  iter  .  next  (  )  ; 
sb  .  append  (  s  )  ; 
if  (  iter  .  hasNext  (  )  )  sb  .  append  (  separator  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   List  <  String  >  prefix  (  String   prefix  ,  List  <  String  >  lines  )  { 
List  <  String  >  result  =  new   ArrayList  <  String  >  (  lines  .  size  (  )  )  ; 
for  (  String   line  :  lines  )  { 
result  .  add  (  prefix  +  line  )  ; 
} 
return   result  ; 
} 






public   static   boolean   areDisjoint  (  Set  <  ?  >  ...  ss  )  { 
if  (  ss  ==  null  ||  ss  .  length  ==  0  )  return   true  ; 
int   elementCount  =  0  ; 
for  (  Set  <  ?  >  set  :  ss  )  { 
elementCount  +=  set  .  size  (  )  ; 
} 
return   CollectionsExt  .  union  (  ss  )  .  size  (  )  ==  elementCount  ; 
} 










public   static   Map  <  Integer  ,  Set  <  Object  [  ]  >  >  createPerArityGroups  (  Object  [  ]  objects  ,  int   max  )  { 
Map  <  Integer  ,  Set  <  Object  [  ]  >  >  result  =  new   LinkedHashMap  <  Integer  ,  Set  <  Object  [  ]  >  >  (  )  ; 
result  .  put  (  0  ,  Collections  .  singleton  (  new   Object  [  0  ]  )  )  ; 
for  (  int   i  =  1  ;  i  <=  max  ;  i  ++  )  { 
Set  <  Object  [  ]  >  newSet  =  new   LinkedHashSet  <  Object  [  ]  >  (  )  ; 
for  (  Object  [  ]  oneSmaller  :  result  .  get  (  i  -  1  )  )  { 
for  (  Object   object  :  objects  )  { 
newSet  .  add  (  CollectionsExt  .  addToArray  (  oneSmaller  ,  object  )  )  ; 
} 
} 
result  .  put  (  i  ,  newSet  )  ; 
} 
return   result  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  addToArray  (  T  [  ]  array  ,  T   el  )  { 
T  [  ]  newArray  =  (  T  [  ]  )  Array  .  newInstance  (  array  .  getClass  (  )  .  getComponentType  (  )  ,  array  .  length  +  1  )  ; 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  array  .  length  )  ; 
newArray  [  array  .  length  ]  =  el  ; 
return   newArray  ; 
} 




public   static   boolean   allInstancesOf  (  Class  <  ?  >  clazz  ,  Object  [  ]  objs  )  { 
for  (  Object   obj  :  objs  )  { 
if  (  !  clazz  .  isInstance  (  obj  )  )  return   false  ; 
} 
return   true  ; 
} 










public   static  <  T  >  List  <  List  <  T  >  >  allCombinations  (  List  <  List  <  T  >  >  inputs  )  { 
if  (  inputs  .  isEmpty  (  )  )  { 
ArrayList  <  List  <  T  >  >  result  =  new   ArrayList  <  List  <  T  >  >  (  1  )  ; 
result  .  add  (  new   ArrayList  <  T  >  (  1  )  )  ; 
return   result  ; 
} 
List  <  T  >  lastList  =  inputs  .  get  (  inputs  .  size  (  )  -  1  )  ; 
List  <  List  <  T  >  >  result  =  new   ArrayList  <  List  <  T  >  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  lastList  .  size  (  )  ;  i  ++  )  { 
List  <  List  <  T  >  >  tails  =  allCombinations  (  inputs  .  subList  (  0  ,  inputs  .  size  (  )  -  1  )  )  ; 
T   x  =  lastList  .  get  (  i  )  ; 
for  (  List  <  T  >  tail  :  tails  )  { 
tail  .  add  (  x  )  ; 
} 
result  .  addAll  (  tails  )  ; 
} 
return   result  ; 
} 





public   static  <  T  >  boolean   containsIdentical  (  Collection  <  T  >  c  ,  Object   o  )  { 
for  (  T   t  :  c  )  { 
if  (  t  ==  o  )  return   true  ; 
} 
return   false  ; 
} 





public   static  <  T  >  boolean   containsEquivalent  (  Collection  <  T  >  c  ,  Object   o  ,  Hasher   h  )  { 
for  (  T   t  :  c  )  { 
if  (  h  .  equals  (  t  ,  o  )  )  return   true  ; 
} 
return   false  ; 
} 





public   static  <  T  >  List  <  T  >  map  (  List  <  T  >  values  ,  Map  <  T  ,  T  >  map  )  { 
List  <  T  >  result  =  new   ArrayList  <  T  >  (  values  .  size  (  )  )  ; 
for  (  T   t  :  values  )  { 
result  .  add  (  map  .  get  (  t  )  )  ; 
} 
return   result  ; 
} 
} 

