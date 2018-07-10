package   com  .  rhythm  .  commons  .  collections  ; 

import   static   com  .  rhythm  .  base  .  Preconditions  .  assertNotNull  ; 
import   static   com  .  rhythm  .  base  .  Preconditions  .  assertArgument  ; 
import   com  .  rhythm  .  base  .  Nulls  ; 
import   com  .  rhythm  .  base  .  Numbers  ; 
import   com  .  rhythm  .  base  .  Strings  ; 
import   java  .  lang  .  reflect  .  Array  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  NoSuchElementException  ; 





public   class   Arrays  { 

public   static   final   int   INDEX_NOT_FOUND  =  -  1  ; 


public   static   final   Object  [  ]  EMPTY_ARRAY  =  new   Object  [  0  ]  ; 















@  SuppressWarnings  (  "unchecked"  ) 
public   static  <  T  >  T  [  ]  newArray  (  Class  <  T  >  type  ,  int   length  )  { 
assertArgument  (  length  >=  0  ,  "Length of an array cannot be negative."  )  ; 
return  (  T  [  ]  )  Array  .  newInstance  (  type  ,  length  )  ; 
} 








public   static  <  T  >  T  [  ]  newArray  (  T  [  ]  reference  ,  int   length  )  { 
Class  <  ?  >  type  =  reference  .  getClass  (  )  .  getComponentType  (  )  ; 
@  SuppressWarnings  (  "unchecked"  )  T  [  ]  result  =  (  T  [  ]  )  Array  .  newInstance  (  type  ,  length  )  ; 
return   result  ; 
} 






public   static  <  T  >  T  [  ]  emptyArray  (  T  [  ]  array  )  { 
return  (  array  .  length  ==  0  )  ?  array  :  newArray  (  array  ,  0  )  ; 
} 







public   static   boolean   isEmpty  (  Object  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  long  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  int  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  short  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  char  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  byte  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  double  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  float  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 







public   static   boolean   isEmpty  (  boolean  [  ]  array  )  { 
if  (  array  ==  null  ||  array  .  length  ==  0  )  { 
return   true  ; 
} 
return   false  ; 
} 

public   static  <  T  >  T  [  ]  add  (  T  [  ]  array  ,  T   element  )  { 
Nulls  .  failIfNull  (  array  ,  "Cannot add to a null array"  )  ; 
T  [  ]  copy  =  expand  (  array  ,  1  )  ; 
copy  [  copy  .  length  -  1  ]  =  element  ; 
return   copy  ; 
} 











public   static   int   indexOf  (  Object  [  ]  array  ,  Object   objectToFind  )  { 
return   indexOf  (  array  ,  objectToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  Object  [  ]  array  ,  Object   objectToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
if  (  objectToFind  ==  null  )  { 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  array  [  i  ]  ==  null  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  objectToFind  .  equals  (  array  [  i  ]  )  )  { 
return   i  ; 
} 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  Object  [  ]  array  ,  Object   objectToFind  )  { 
return   lastIndexOf  (  array  ,  objectToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  Object  [  ]  array  ,  Object   objectToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
if  (  objectToFind  ==  null  )  { 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  array  [  i  ]  ==  null  )  { 
return   i  ; 
} 
} 
}  else  { 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  objectToFind  .  equals  (  array  [  i  ]  )  )  { 
return   i  ; 
} 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  Object  [  ]  array  ,  Object   objectToFind  )  { 
return   indexOf  (  array  ,  objectToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  long  [  ]  array  ,  long   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  long  [  ]  array  ,  long   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  long  [  ]  array  ,  long   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  long  [  ]  array  ,  long   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  long  [  ]  array  ,  long   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  int  [  ]  array  ,  int   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  int  [  ]  array  ,  int   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  int  [  ]  array  ,  int   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  int  [  ]  array  ,  int   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  int  [  ]  array  ,  int   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  short  [  ]  array  ,  short   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  short  [  ]  array  ,  short   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  short  [  ]  array  ,  short   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  short  [  ]  array  ,  short   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  short  [  ]  array  ,  short   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 












public   static   int   indexOf  (  char  [  ]  array  ,  char   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 
















public   static   int   indexOf  (  char  [  ]  array  ,  char   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 












public   static   int   lastIndexOf  (  char  [  ]  array  ,  char   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 
















public   static   int   lastIndexOf  (  char  [  ]  array  ,  char   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   boolean   contains  (  char  [  ]  array  ,  char   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  byte  [  ]  array  ,  byte   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  byte  [  ]  array  ,  byte   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  byte  [  ]  array  ,  byte   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  byte  [  ]  array  ,  byte   valueToFind  ,  int   startIndex  )  { 
if  (  array  ==  null  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  byte  [  ]  array  ,  byte   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  double  [  ]  array  ,  double   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 














public   static   int   indexOf  (  double  [  ]  array  ,  double   valueToFind  ,  double   tolerance  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  ,  tolerance  )  ; 
} 















public   static   int   indexOf  (  double  [  ]  array  ,  double   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 


















public   static   int   indexOf  (  double  [  ]  array  ,  double   valueToFind  ,  int   startIndex  ,  double   tolerance  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
double   min  =  valueToFind  -  tolerance  ; 
double   max  =  valueToFind  +  tolerance  ; 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  array  [  i  ]  >=  min  &&  array  [  i  ]  <=  max  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  double  [  ]  array  ,  double   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 














public   static   int   lastIndexOf  (  double  [  ]  array  ,  double   valueToFind  ,  double   tolerance  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  ,  tolerance  )  ; 
} 















public   static   int   lastIndexOf  (  double  [  ]  array  ,  double   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 


















public   static   int   lastIndexOf  (  double  [  ]  array  ,  double   valueToFind  ,  int   startIndex  ,  double   tolerance  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
double   min  =  valueToFind  -  tolerance  ; 
double   max  =  valueToFind  +  tolerance  ; 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  array  [  i  ]  >=  min  &&  array  [  i  ]  <=  max  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  double  [  ]  array  ,  double   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 














public   static   boolean   contains  (  double  [  ]  array  ,  double   valueToFind  ,  double   tolerance  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  ,  tolerance  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  float  [  ]  array  ,  float   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 















public   static   int   indexOf  (  float  [  ]  array  ,  float   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 











public   static   int   lastIndexOf  (  float  [  ]  array  ,  float   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  float  [  ]  array  ,  float   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  float  [  ]  array  ,  float   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 











public   static   int   indexOf  (  boolean  [  ]  array  ,  boolean   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  ,  0  )  ; 
} 
















public   static   int   indexOf  (  boolean  [  ]  array  ,  boolean   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
startIndex  =  0  ; 
} 
for  (  int   i  =  startIndex  ;  i  <  array  .  length  ;  i  ++  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 












public   static   int   lastIndexOf  (  boolean  [  ]  array  ,  boolean   valueToFind  )  { 
return   lastIndexOf  (  array  ,  valueToFind  ,  Integer  .  MAX_VALUE  )  ; 
} 















public   static   int   lastIndexOf  (  boolean  [  ]  array  ,  boolean   valueToFind  ,  int   startIndex  )  { 
if  (  Arrays  .  isEmpty  (  array  )  )  { 
return   INDEX_NOT_FOUND  ; 
} 
if  (  startIndex  <  0  )  { 
return   INDEX_NOT_FOUND  ; 
}  else   if  (  startIndex  >=  array  .  length  )  { 
startIndex  =  array  .  length  -  1  ; 
} 
for  (  int   i  =  startIndex  ;  i  >=  0  ;  i  --  )  { 
if  (  valueToFind  ==  array  [  i  ]  )  { 
return   i  ; 
} 
} 
return   INDEX_NOT_FOUND  ; 
} 










public   static   boolean   contains  (  boolean  [  ]  array  ,  boolean   valueToFind  )  { 
return   indexOf  (  array  ,  valueToFind  )  !=  INDEX_NOT_FOUND  ; 
} 










public   static  <  T  >  T  [  ]  expand  (  T  [  ]  array  ,  int   amount  )  { 
Nulls  .  failIfNull  (  array  ,  "Cannot expand a null array"  )  ; 
int   arrayLength  =  Array  .  getLength  (  array  )  ; 
@  SuppressWarnings  (  "unchecked"  )  T  [  ]  newArray  =  (  T  [  ]  )  Array  .  newInstance  (  array  .  getClass  (  )  .  getComponentType  (  )  ,  arrayLength  +  amount  )  ; 
System  .  arraycopy  (  array  ,  0  ,  newArray  ,  0  ,  arrayLength  )  ; 
return   newArray  ; 
} 







public   static  <  T  >  T  [  ]  of  (  T  ...  elements  )  { 
return   elements  ; 
} 













public   static  <  T  >  boolean   isLastItem  (  int   i  ,  T  [  ]  array  )  { 
Nulls  .  failIfNull  (  array  ,  "Cannot check the size of a null array."  )  ; 
if  (  i  ==  (  array  .  length  -  1  )  )  { 
return   true  ; 
} 
return   false  ; 
} 














public   static   boolean   isLastItem  (  Object   obj  ,  Object  [  ]  array  )  { 
Nulls  .  failIfNull  (  array  ,  "Cannot check the size of a null array."  )  ; 
int   index  =  Arrays  .  indexOf  (  array  ,  obj  )  ; 
return  (  (  index  ==  INDEX_NOT_FOUND  )  ?  false  :  isLastItem  (  index  ,  array  )  )  ; 
} 











public   static   String   toString  (  String  [  ]  strs  )  { 
if  (  strs  ==  null  )  { 
return   Strings  .  valueOf  (  strs  )  ; 
} 
StringBuilder   buffer  =  new   StringBuilder  (  strs  .  length  )  ; 
for  (  String   string  :  strs  )  { 
buffer  .  append  (  string  )  ; 
} 
return   buffer  .  toString  (  )  ; 
} 












public   static   String   toString  (  String  [  ]  strs  ,  String   delimiter  )  { 
if  (  strs  ==  null  )  { 
return   Strings  .  valueOf  (  strs  )  ; 
} 
StringBuilder   buffer  =  new   StringBuilder  (  strs  .  length  )  ; 
String   str  =  null  ; 
for  (  int   i  =  0  ;  i  <  strs  .  length  ;  i  ++  )  { 
str  =  strs  [  i  ]  ; 
buffer  .  append  (  str  )  ; 
if  (  !  Arrays  .  isLastItem  (  i  ,  strs  )  )  { 
buffer  .  append  (  delimiter  )  ; 
} 
} 
return   buffer  .  toString  (  )  ; 
} 
















public   static  <  T  >  List  <  T  >  toList  (  T  ...  array  )  { 
return   java  .  util  .  Arrays  .  asList  (  array  )  ; 
} 










public   static   synchronized  <  T  >  T   random  (  T  [  ]  array  )  { 
return  (  (  isEmpty  (  array  )  )  ?  null  :  array  [  Numbers  .  random  (  0  ,  array  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   int   random  (  int  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   char   random  (  char  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   boolean   random  (  boolean  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   long   random  (  long  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   byte   random  (  byte  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   float   random  (  float  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   short   random  (  short  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized   double   random  (  double  [  ]  ref  )  { 
return  (  (  isEmpty  (  ref  )  )  ?  null  :  ref  [  Numbers  .  random  (  0  ,  ref  .  length  -  1  )  ]  )  ; 
} 









public   static   synchronized  <  T  >  T  [  ]  concat  (  T  [  ]  a  ,  T  [  ]  b  )  { 
assertNotNull  (  a  ,  "Cannot concatenate a null array reference"  )  ; 
assertNotNull  (  b  ,  "Cannot concatenate a null array reference"  )  ; 
final   int   alen  =  a  .  length  ; 
final   int   blen  =  b  .  length  ; 
if  (  alen  ==  0  )  { 
return   b  ; 
} 
if  (  blen  ==  0  )  { 
return   a  ; 
} 
final   T  [  ]  result  =  (  T  [  ]  )  java  .  lang  .  reflect  .  Array  .  newInstance  (  a  .  getClass  (  )  .  getComponentType  (  )  ,  alen  +  blen  )  ; 
System  .  arraycopy  (  a  ,  0  ,  result  ,  0  ,  alen  )  ; 
System  .  arraycopy  (  b  ,  0  ,  result  ,  alen  ,  blen  )  ; 
return   result  ; 
} 

public   static   synchronized   char  [  ]  concat  (  char  [  ]  a  ,  char  [  ]  b  )  { 
assertNotNull  (  a  ,  "Cannot concatenate a null array reference"  )  ; 
assertNotNull  (  b  ,  "Cannot concatenate a null array reference"  )  ; 
final   int   alen  =  a  .  length  ; 
final   int   blen  =  b  .  length  ; 
if  (  alen  ==  0  )  { 
return   b  ; 
} 
if  (  blen  ==  0  )  { 
return   a  ; 
} 
final   char  [  ]  result  =  new   char  [  alen  +  blen  ]  ; 
System  .  arraycopy  (  a  ,  0  ,  result  ,  0  ,  alen  )  ; 
System  .  arraycopy  (  b  ,  0  ,  result  ,  alen  ,  blen  )  ; 
return   result  ; 
} 

public   static   synchronized   int  [  ]  concat  (  int  [  ]  a  ,  int  [  ]  b  )  { 
assertNotNull  (  a  ,  "Cannot concatenate a null array reference"  )  ; 
assertNotNull  (  b  ,  "Cannot concatenate a null array reference"  )  ; 
final   int   alen  =  a  .  length  ; 
final   int   blen  =  b  .  length  ; 
if  (  alen  ==  0  )  { 
return   b  ; 
} 
if  (  blen  ==  0  )  { 
return   a  ; 
} 
final   int  [  ]  result  =  new   int  [  alen  +  blen  ]  ; 
System  .  arraycopy  (  a  ,  0  ,  result  ,  0  ,  alen  )  ; 
System  .  arraycopy  (  b  ,  0  ,  result  ,  alen  ,  blen  )  ; 
return   result  ; 
} 








public   static   synchronized  <  T   extends   Object  >  T  [  ]  copy  (  T  [  ]  ref  )  { 
if  (  ref  ==  null  )  { 
return   null  ; 
} 
T  [  ]  copy  =  Arrays  .  newArray  (  ref  ,  ref  .  length  )  ; 
System  .  arraycopy  (  ref  ,  0  ,  copy  ,  0  ,  ref  .  length  )  ; 
return   copy  ; 
} 








@  SuppressWarnings  (  "unchecked"  ) 
public   static   synchronized  <  T  >  Iterator  <  T  >  toIterator  (  final   T  [  ]  ref  )  { 
assertNotNull  (  ref  ,  "null array not allowed"  )  ; 
return   new   Iterator  (  )  { 

private   T  [  ]  ary  =  ref  ; 

private   int   position  =  0  ; 

public   boolean   hasNext  (  )  { 
return  (  position  <  ary  .  length  )  ; 
} 

public   T   next  (  )  { 
if  (  hasNext  (  )  )  { 
return   ary  [  position  ++  ]  ; 
}  else  { 
throw   new   NoSuchElementException  (  )  ; 
} 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
}  ; 
} 








@  SuppressWarnings  (  "unchecked"  ) 
public   static   synchronized   Iterator   toIterator  (  final   int  [  ]  ref  )  { 
assertNotNull  (  ref  ,  "null array not allowed"  )  ; 
return   new   Iterator  (  )  { 

private   int  [  ]  ary  =  ref  ; 

private   int   position  =  0  ; 

public   boolean   hasNext  (  )  { 
return  (  position  <  ary  .  length  )  ; 
} 

public   Object   next  (  )  { 
if  (  hasNext  (  )  )  { 
return   ary  [  position  ++  ]  ; 
}  else  { 
throw   new   NoSuchElementException  (  )  ; 
} 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
}  ; 
} 







public   static   synchronized  <  T  >  T  [  ]  invert  (  T  [  ]  ref  )  { 
assertNotNull  (  "Cannot invert a null array"  )  ; 
T  [  ]  copy  =  newArray  (  ref  ,  ref  .  length  )  ; 
int   index  =  0  ; 
for  (  int   i  =  ref  .  length  ;  i  >  0  ;  i  --  )  { 
copy  [  index  ++  ]  =  ref  [  i  -  1  ]  ; 
} 
return   copy  ; 
} 

private   Arrays  (  )  { 
} 
} 

