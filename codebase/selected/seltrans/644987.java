package   werkzeugkasten  .  common  .  util  ; 

import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  List  ; 







public   class   ArrayUtil  { 








@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  add  (  T  [  ]  array  ,  T   obj  )  { 
if  (  array  ==  null  )  { 
throw   new   IllegalStateException  (  "array"  )  ; 
} 
T  [  ]  newArray  =  (  T  [  ]  )  Array  .  newInstance  (  array  .  getClass  (  )  .  getComponentType  (  )  ,  array  .  length  +  1  )  ; 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  array  .  length  )  ; 
newArray  [  array  .  length  ]  =  obj  ; 
return   newArray  ; 
} 








public   static   Object  [  ]  add  (  final   Object  [  ]  a  ,  final   Object  [  ]  b  )  { 
if  (  (  a  !=  null  )  &&  (  b  !=  null  )  )  { 
if  (  (  a  .  length  !=  0  )  &&  (  b  .  length  !=  0  )  )  { 
Object  [  ]  array  =  (  Object  [  ]  )  Array  .  newInstance  (  a  .  getClass  (  )  .  getComponentType  (  )  ,  a  .  length  +  b  .  length  )  ; 
System  .  arraycopy  (  a  ,  0  ,  array  ,  0  ,  a  .  length  )  ; 
System  .  arraycopy  (  b  ,  0  ,  array  ,  a  .  length  ,  b  .  length  )  ; 
return   array  ; 
}  else   if  (  b  .  length  ==  0  )  { 
return   a  ; 
}  else  { 
return   b  ; 
} 
}  else   if  (  b  ==  null  )  { 
return   a  ; 
}  else  { 
return   b  ; 
} 
} 








public   static   int   indexOf  (  Object  [  ]  array  ,  Object   obj  )  { 
if  (  array  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  ++  i  )  { 
Object   o  =  array  [  i  ]  ; 
if  (  o  !=  null  )  { 
if  (  o  .  equals  (  obj  )  )  { 
return   i  ; 
} 
}  else   if  (  obj  ==  null  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 









public   static   int   indexOf  (  char  [  ]  array  ,  char   ch  )  { 
if  (  array  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  ++  i  )  { 
char   c  =  array  [  i  ]  ; 
if  (  ch  ==  c  )  { 
return   i  ; 
} 
} 
} 
return  -  1  ; 
} 








public   static   Object  [  ]  remove  (  Object  [  ]  array  ,  Object   obj  )  { 
int   index  =  indexOf  (  array  ,  obj  )  ; 
if  (  index  <  0  )  { 
return   array  ; 
} 
Object  [  ]  newArray  =  (  Object  [  ]  )  Array  .  newInstance  (  array  .  getClass  (  )  .  getComponentType  (  )  ,  array  .  length  -  1  )  ; 
if  (  index  >  0  )  { 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  index  )  ; 
} 
if  (  index  <  array  .  length  -  1  )  { 
System  .  arraycopy  (  array  ,  index  +  1  ,  newArray  ,  index  ,  newArray  .  length  -  index  )  ; 
} 
return   newArray  ; 
} 







public   static   boolean   isEmpty  (  Object  [  ]  arrays  )  { 
return  (  (  arrays  ==  null  )  ||  (  arrays  .  length  ==  0  )  )  ; 
} 








public   static   boolean   contains  (  Object  [  ]  array  ,  Object   obj  )  { 
return  -  1  <  indexOf  (  array  ,  obj  )  ; 
} 








public   static   boolean   contains  (  char  [  ]  array  ,  char   ch  )  { 
return  -  1  <  indexOf  (  array  ,  ch  )  ; 
} 








public   static   boolean   equalsIgnoreSequence  (  Object  [  ]  array1  ,  Object  [  ]  array2  )  { 
if  (  (  array1  ==  null  )  &&  (  array2  ==  null  )  )  { 
return   true  ; 
}  else   if  (  (  array1  ==  null  )  ||  (  array2  ==  null  )  )  { 
return   false  ; 
} 
if  (  array1  .  length  !=  array2  .  length  )  { 
return   false  ; 
} 
List  <  Object  >  list  =  Arrays  .  asList  (  array2  )  ; 
for  (  int   i  =  0  ;  i  <  array1  .  length  ;  i  ++  )  { 
Object   o1  =  array1  [  i  ]  ; 
if  (  !  list  .  contains  (  o1  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 








@  Deprecated 
public   static   String   toString  (  Object  [  ]  array  )  { 
if  (  array  ==  null  )  { 
return  "null"  ; 
} 
if  (  array  .  length  ==  0  )  { 
return  "[]"  ; 
} 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  i  ==  0  )  { 
sb  .  append  (  '['  )  ; 
}  else  { 
sb  .  append  (  ", "  )  ; 
} 
sb  .  append  (  String  .  valueOf  (  array  [  i  ]  )  )  ; 
} 
sb  .  append  (  "]"  )  ; 
return   sb  .  toString  (  )  ; 
} 







public   static   Object  [  ]  toObjectArray  (  Object   obj  )  { 
int   length  =  Array  .  getLength  (  obj  )  ; 
Object  [  ]  array  =  new   Object  [  length  ]  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
array  [  i  ]  =  Array  .  get  (  obj  ,  i  )  ; 
} 
return   array  ; 
} 







public   static   int   compare  (  byte  [  ]  lefts  ,  byte  [  ]  rights  )  { 
if  (  lefts  ==  rights  )  { 
return   0  ; 
} 
if  (  (  lefts  !=  null  )  &&  (  rights  ==  null  )  )  { 
return   1  ; 
} 
if  (  (  lefts  ==  null  )  &&  (  rights  !=  null  )  )  { 
return  -  1  ; 
} 
int   ll  =  lefts  .  length  ; 
int   rl  =  rights  .  length  ; 
int   min  =  Math  .  min  (  ll  ,  rl  )  ; 
for  (  int   i  =  0  ;  i  <  min  ;  i  ++  )  { 
byte   lb  =  lefts  [  i  ]  ; 
byte   rb  =  rights  [  i  ]  ; 
if  (  lb  <  rb  )  { 
return  -  1  ; 
} 
if  (  lb  >  rb  )  { 
return   1  ; 
} 
} 
return   ll  -  rl  ; 
} 
} 

