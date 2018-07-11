package   com  .  limegroup  .  gnutella  .  util  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  NoSuchElementException  ; 
import   com  .  limegroup  .  gnutella  .  Assert  ; 
































public   class   StringTrie  <  V  >  { 































private   TrieNode  <  V  >  root  ; 





private   boolean   ignoreCase  ; 




public   StringTrie  (  boolean   ignoreCase  )  { 
this  .  ignoreCase  =  ignoreCase  ; 
clear  (  )  ; 
} 





public   void   clear  (  )  { 
this  .  root  =  new   TrieNode  <  V  >  (  )  ; 
} 















public   String   canonicalCase  (  final   String   s  )  { 
if  (  !  ignoreCase  )  return   s  ; 
return   s  .  toUpperCase  (  Locale  .  US  )  .  toLowerCase  (  Locale  .  US  )  ; 
} 

























private   final   int   match  (  String   a  ,  int   startOffset  ,  int   stopOffset  ,  String   b  )  { 
int   i  =  startOffset  ; 
for  (  int   j  =  0  ;  j  <  b  .  length  (  )  ;  j  ++  )  { 
if  (  i  >=  stopOffset  )  return   j  ; 
if  (  a  .  charAt  (  i  )  !=  b  .  charAt  (  j  )  )  return   j  ; 
i  ++  ; 
} 
return  -  1  ; 
} 








public   V   add  (  String   key  ,  V   value  )  { 
key  =  canonicalCase  (  key  )  ; 
TrieNode  <  V  >  node  =  root  ; 
int   i  =  0  ; 
while  (  i  <  key  .  length  (  )  )  { 
TrieEdge  <  V  >  edge  =  node  .  get  (  key  .  charAt  (  i  )  )  ; 
if  (  edge  ==  null  )  { 
TrieNode  <  V  >  newNode  =  new   TrieNode  <  V  >  (  value  )  ; 
node  .  put  (  key  .  substring  (  i  )  ,  newNode  )  ; 
return   null  ; 
} 
String   label  =  edge  .  getLabel  (  )  ; 
int   j  =  match  (  key  ,  i  ,  key  .  length  (  )  ,  label  )  ; 
Assert  .  that  (  j  !=  0  ,  "Label didn't start with prefix[0]."  )  ; 
if  (  j  >=  0  )  { 
TrieNode  <  V  >  child  =  edge  .  getChild  (  )  ; 
TrieNode  <  V  >  intermediate  =  new   TrieNode  <  V  >  (  )  ; 
String   a  =  label  .  substring  (  0  ,  j  )  ; 
String   b  =  label  .  substring  (  j  )  ; 
String   c  =  key  .  substring  (  i  +  j  )  ; 
if  (  c  .  length  (  )  >  0  )  { 
TrieNode  <  V  >  newNode  =  new   TrieNode  <  V  >  (  value  )  ; 
node  .  remove  (  label  .  charAt  (  0  )  )  ; 
node  .  put  (  a  ,  intermediate  )  ; 
intermediate  .  put  (  b  ,  child  )  ; 
intermediate  .  put  (  c  ,  newNode  )  ; 
}  else  { 
node  .  remove  (  label  .  charAt  (  0  )  )  ; 
node  .  put  (  a  ,  intermediate  )  ; 
intermediate  .  put  (  b  ,  child  )  ; 
intermediate  .  setValue  (  value  )  ; 
} 
return   null  ; 
} 
Assert  .  that  (  j  ==  -  1  ,  "Bad return value from match: "  +  i  )  ; 
node  =  edge  .  getChild  (  )  ; 
i  +=  label  .  length  (  )  ; 
} 
V   ret  =  node  .  getValue  (  )  ; 
node  .  setValue  (  value  )  ; 
return   ret  ; 
} 




private   TrieNode  <  V  >  fetch  (  String   prefix  )  { 
TrieNode  <  V  >  node  =  root  ; 
for  (  int   i  =  0  ;  i  <  prefix  .  length  (  )  ;  )  { 
TrieEdge  <  V  >  edge  =  node  .  get  (  prefix  .  charAt  (  i  )  )  ; 
if  (  edge  ==  null  )  return   null  ; 
String   label  =  edge  .  getLabel  (  )  ; 
int   j  =  match  (  prefix  ,  i  ,  prefix  .  length  (  )  ,  label  )  ; 
Assert  .  that  (  j  !=  0  ,  "Label didn't start with prefix[0]."  )  ; 
if  (  j  !=  -  1  )  return   null  ; 
i  +=  label  .  length  (  )  ; 
node  =  edge  .  getChild  (  )  ; 
} 
return   node  ; 
} 






public   V   get  (  String   key  )  { 
key  =  canonicalCase  (  key  )  ; 
TrieNode  <  V  >  node  =  fetch  (  key  )  ; 
if  (  node  ==  null  )  return   null  ; 
return   node  .  getValue  (  )  ; 
} 







public   boolean   remove  (  String   key  )  { 
key  =  canonicalCase  (  key  )  ; 
TrieNode  <  V  >  node  =  fetch  (  key  )  ; 
if  (  node  ==  null  )  return   false  ; 
boolean   ret  =  node  .  getValue  (  )  !=  null  ; 
node  .  setValue  (  null  )  ; 
return   ret  ; 
} 










public   Iterator  <  V  >  getPrefixedBy  (  String   prefix  )  { 
prefix  =  canonicalCase  (  prefix  )  ; 
return   getPrefixedBy  (  prefix  ,  0  ,  prefix  .  length  (  )  )  ; 
} 













public   Iterator  <  V  >  getPrefixedBy  (  String   prefix  ,  int   startOffset  ,  int   stopOffset  )  { 
TrieNode  <  V  >  node  =  root  ; 
for  (  int   i  =  startOffset  ;  i  <  stopOffset  ;  )  { 
TrieEdge  <  V  >  edge  =  node  .  get  (  prefix  .  charAt  (  i  )  )  ; 
if  (  edge  ==  null  )  { 
return   EmptyIterator  .  emptyIterator  (  )  ; 
} 
node  =  edge  .  getChild  (  )  ; 
String   label  =  edge  .  getLabel  (  )  ; 
int   j  =  match  (  prefix  ,  i  ,  stopOffset  ,  label  )  ; 
Assert  .  that  (  j  !=  0  ,  "Label didn't start with prefix[0]."  )  ; 
if  (  i  +  j  ==  stopOffset  )  { 
break  ; 
}  else   if  (  j  >=  0  )  { 
node  =  null  ; 
break  ; 
}  else  { 
Assert  .  that  (  j  ==  -  1  ,  "Bad return value from match: "  +  i  )  ; 
} 
i  +=  label  .  length  (  )  ; 
} 
if  (  node  ==  null  )  return   EmptyIterator  .  emptyIterator  (  )  ;  else   return   new   ValueIterator  (  node  )  ; 
} 




public   Iterator  <  V  >  getIterator  (  )  { 
return   new   ValueIterator  (  root  )  ; 
} 





private   class   ValueIterator   extends   UnmodifiableIterator  <  V  >  { 

private   NodeIterator   delegate  ; 

ValueIterator  (  TrieNode  <  V  >  start  )  { 
delegate  =  new   NodeIterator  (  start  ,  false  )  ; 
} 

public   V   next  (  )  { 
return   delegate  .  next  (  )  .  getValue  (  )  ; 
} 

public   boolean   hasNext  (  )  { 
return   delegate  .  hasNext  (  )  ; 
} 
} 












public   void   trim  (  Function  <  V  ,  ?  extends   V  >  valueCompactor  )  throws   IllegalArgumentException  ,  ClassCastException  { 
if  (  valueCompactor  !=  null  )  { 
for  (  Iterator  <  TrieNode  <  V  >  >  iter  =  new   NodeIterator  (  root  ,  true  )  ;  iter  .  hasNext  (  )  ;  )  { 
TrieNode  <  V  >  node  =  iter  .  next  (  )  ; 
node  .  trim  (  )  ; 
V   value  =  node  .  getValue  (  )  ; 
if  (  value  !=  null  )  node  .  setValue  (  valueCompactor  .  apply  (  value  )  )  ; 
} 
} 
} 

public   class   NodeIterator   extends   UnmodifiableIterator  <  TrieNode  <  V  >  >  { 


private   ArrayList  <  Iterator  <  TrieNode  <  V  >  >  >  stack  =  new   ArrayList  <  Iterator  <  TrieNode  <  V  >  >  >  (  )  ; 


private   TrieNode  <  V  >  nextNode  ; 

private   boolean   withNulls  ; 





public   NodeIterator  (  TrieNode  <  V  >  start  ,  boolean   withNulls  )  { 
this  .  withNulls  =  withNulls  ; 
if  (  withNulls  ||  start  .  getValue  (  )  !=  null  )  { 
nextNode  =  start  ; 
}  else  { 
nextNode  =  null  ; 
advance  (  start  )  ; 
} 
} 

public   boolean   hasNext  (  )  { 
return  !  stack  .  isEmpty  (  )  ||  nextNode  !=  null  ; 
} 

public   TrieNode  <  V  >  next  (  )  { 
if  (  nextNode  ==  null  )  { 
throw   new   NoSuchElementException  (  )  ; 
} 
TrieNode  <  V  >  node  =  nextNode  ; 
nextNode  =  null  ; 
advance  (  node  )  ; 
return   node  ; 
} 








private   void   advance  (  TrieNode  <  V  >  node  )  { 
Iterator  <  TrieNode  <  V  >  >  children  =  node  .  childrenForward  (  )  ; 
while  (  true  )  { 
int   size  ; 
if  (  children  .  hasNext  (  )  )  { 
node  =  children  .  next  (  )  ; 
if  (  children  .  hasNext  (  )  )  stack  .  add  (  children  )  ; 
if  (  withNulls  ||  node  .  getValue  (  )  ==  null  )  children  =  node  .  childrenForward  (  )  ;  else  { 
nextNode  =  node  ; 
return  ; 
} 
}  else   if  (  (  size  =  stack  .  size  (  )  )  ==  0  )  return  ;  else   children  =  stack  .  remove  (  size  -  1  )  ; 
} 
} 
} 






public   String   toString  (  )  { 
StringBuilder   buf  =  new   StringBuilder  (  )  ; 
buf  .  append  (  "<root>"  )  ; 
toStringHelper  (  root  ,  buf  ,  1  )  ; 
return   buf  .  toString  (  )  ; 
} 





private   void   toStringHelper  (  TrieNode   start  ,  StringBuilder   buf  ,  int   indent  )  { 
if  (  start  .  getValue  (  )  !=  null  )  { 
buf  .  append  (  " -> "  )  ; 
buf  .  append  (  start  .  getValue  (  )  .  toString  (  )  )  ; 
} 
buf  .  append  (  "\n"  )  ; 
for  (  Iterator   iter  =  start  .  labelsForward  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
for  (  int   i  =  0  ;  i  <  indent  ;  i  ++  )  buf  .  append  (  " "  )  ; 
String   label  =  (  String  )  iter  .  next  (  )  ; 
buf  .  append  (  label  )  ; 
TrieNode   child  =  start  .  get  (  label  .  charAt  (  0  )  )  .  getChild  (  )  ; 
toStringHelper  (  child  ,  buf  ,  indent  +  1  )  ; 
} 
} 
} 












final   class   TrieNode  <  E  >  { 




private   E   value  =  null  ; 











private   ArrayList  <  TrieEdge  <  E  >  >  children  =  new   ArrayList  <  TrieEdge  <  E  >  >  (  0  )  ; 




public   TrieNode  (  )  { 
} 




public   TrieNode  (  E   value  )  { 
this  .  value  =  value  ; 
} 




public   E   getValue  (  )  { 
return   value  ; 
} 




public   void   setValue  (  E   value  )  { 
this  .  value  =  value  ; 
} 






private   final   TrieEdge  <  E  >  get  (  int   i  )  { 
return   children  .  get  (  i  )  ; 
} 






































private   final   int   search  (  char   c  ,  boolean   exact  )  { 
int   low  =  0  ; 
int   high  =  children  .  size  (  )  -  1  ; 
while  (  low  <=  high  )  { 
int   middle  =  (  low  +  high  )  /  2  ; 
char   cmiddle  =  get  (  middle  )  .  getLabelStart  (  )  ; 
if  (  cmiddle  <  c  )  low  =  middle  +  1  ;  else   if  (  c  <  cmiddle  )  high  =  middle  -  1  ;  else   return   middle  ; 
} 
if  (  exact  )  return  -  1  ; 
return   high  ; 
} 





public   TrieEdge  <  E  >  get  (  char   labelStart  )  { 
int   i  =  search  (  labelStart  ,  true  )  ; 
if  (  i  <  0  )  return   null  ; 
TrieEdge  <  E  >  ret  =  get  (  i  )  ; 
Assert  .  that  (  ret  .  getLabelStart  (  )  ==  labelStart  )  ; 
return   ret  ; 
} 










public   void   put  (  String   label  ,  TrieNode  <  E  >  child  )  { 
char   labelStart  ; 
int   i  ; 
if  (  (  i  =  search  (  labelStart  =  label  .  charAt  (  0  )  ,  false  )  )  >=  0  )  { 
Assert  .  that  (  get  (  i  )  .  getLabelStart  (  )  !=  labelStart  ,  "Precondition of TrieNode.put violated."  )  ; 
} 
children  .  add  (  i  +  1  ,  new   TrieEdge  <  E  >  (  label  ,  child  )  )  ; 
} 





public   boolean   remove  (  char   labelStart  )  { 
int   i  ; 
if  (  (  i  =  search  (  labelStart  ,  true  )  )  <  0  )  return   false  ; 
Assert  .  that  (  get  (  i  )  .  getLabelStart  (  )  ==  labelStart  )  ; 
children  .  remove  (  i  )  ; 
return   true  ; 
} 







public   void   trim  (  )  { 
children  .  trimToSize  (  )  ; 
} 





public   Iterator  <  TrieNode  <  E  >  >  childrenForward  (  )  { 
return   new   ChildrenForwardIterator  (  )  ; 
} 




private   class   ChildrenForwardIterator   extends   UnmodifiableIterator  <  TrieNode  <  E  >  >  { 

int   i  =  0  ; 

public   boolean   hasNext  (  )  { 
return   i  <  children  .  size  (  )  ; 
} 

public   TrieNode  <  E  >  next  (  )  { 
if  (  i  <  children  .  size  (  )  )  return   get  (  i  ++  )  .  getChild  (  )  ; 
throw   new   NoSuchElementException  (  )  ; 
} 
} 





public   Iterator  <  String  >  labelsForward  (  )  { 
return   new   LabelForwardIterator  (  )  ; 
} 




private   class   LabelForwardIterator   extends   UnmodifiableIterator  <  String  >  { 

int   i  =  0  ; 

public   boolean   hasNext  (  )  { 
return   i  <  children  .  size  (  )  ; 
} 

public   String   next  (  )  { 
if  (  i  <  children  .  size  (  )  )  return   get  (  i  ++  )  .  getLabel  (  )  ; 
throw   new   NoSuchElementException  (  )  ; 
} 
} 

public   String   toString  (  )  { 
Object   val  =  getValue  (  )  ; 
if  (  val  !=  null  )  return   val  .  toString  (  )  ; 
return  "NULL"  ; 
} 
} 




final   class   TrieEdge  <  E  >  { 

private   String   label  ; 

private   TrieNode  <  E  >  child  ; 





TrieEdge  (  String   label  ,  TrieNode  <  E  >  child  )  { 
this  .  label  =  label  ; 
this  .  child  =  child  ; 
} 

public   String   getLabel  (  )  { 
return   label  ; 
} 




public   char   getLabelStart  (  )  { 
return   label  .  charAt  (  0  )  ; 
} 

public   TrieNode  <  E  >  getChild  (  )  { 
return   child  ; 
} 
} 

