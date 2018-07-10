package   src  .  projects  .  findPeaks  ; 

import   java  .  util  .  Vector  ; 
import   src  .  lib  .  FloatingPoint  ; 
import   src  .  lib  .  Error_handling  .  UnexpectedResultException  ; 
import   src  .  lib  .  objects  .  Tuple  ; 
import   src  .  projects  .  findPeaks  .  objects  .  Map_f  ; 
import   src  .  projects  .  findPeaks  .  objects  .  Map_i  ; 





public   class   Functions  { 

private   Functions  (  )  { 
} 










public   static   Tuple  <  Float  ,  Integer  >  [  ]  all_maxima_and_location  (  float  [  ]  R  ,  float   min_valley  )  throws   UnexpectedResultException  { 
Vector  <  Tuple  <  Float  ,  Integer  >  >  filtered  =  new   Vector  <  Tuple  <  Float  ,  Integer  >  >  (  )  ; 
int   h  =  1  ; 
while  (  h  <  R  .  length  -  1  )  { 
if  (  R  [  h  ]  >  R  [  h  -  1  ]  ||  h  ==  1  )  { 
int   pk  =  h  ; 
while  (  h  +  2  <  R  .  length  &&  FloatingPoint  .  are_values_equal  (  R  [  h  ]  ,  R  [  h  +  1  ]  )  )  { 
h  ++  ; 
} 
if  (  h  !=  pk  )  { 
if  (  R  [  h  ]  >  R  [  h  +  1  ]  )  { 
if  (  pk  ==  1  &&  FloatingPoint  .  are_values_equal  (  R  [  0  ]  ,  R  [  1  ]  )  )  { 
pk  =  Math  .  round  (  (  float  )  (  pk  -  1  +  h  )  /  2  )  ; 
}  else  { 
pk  =  (  pk  +  h  )  /  2  ; 
} 
filtered  .  add  (  new   Tuple  <  Float  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
}  else   if  (  h  +  2  ==  R  .  length  )  { 
if  (  R  [  h  ]  <  R  [  h  +  1  ]  )  { 
pk  =  h  +  1  ; 
}  else   if  (  FloatingPoint  .  are_values_equal  (  R  [  h  ]  ,  R  [  h  +  1  ]  )  )  { 
pk  =  (  int  )  (  (  float  )  (  pk  +  h  +  1  )  /  2  )  ; 
}  else  { 
pk  =  (  pk  +  h  )  /  2  ; 
} 
filtered  .  add  (  new   Tuple  <  Float  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
} 
}  else  { 
if  (  R  [  h  ]  >  R  [  h  +  1  ]  )  { 
filtered  .  add  (  new   Tuple  <  Float  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
} 
} 
} 
h  ++  ; 
} 
if  (  filtered  .  size  (  )  <  1  )  { 
filtered  .  add  (  new   Tuple  <  Float  ,  Integer  >  (  R  [  R  .  length  -  1  ]  ,  R  .  length  -  1  )  )  ; 
} 
Tuple  <  Float  ,  Integer  >  [  ]  X  =  separate_peaks  (  filtered  ,  R  ,  min_valley  )  ; 
return   X  ; 
} 










public   static   Tuple  <  Integer  ,  Integer  >  [  ]  all_maxima_and_location  (  int  [  ]  R  ,  float   min_valley  )  throws   UnexpectedResultException  { 
Vector  <  Tuple  <  Integer  ,  Integer  >  >  filtered  =  new   Vector  <  Tuple  <  Integer  ,  Integer  >  >  (  )  ; 
int   h  =  1  ; 
while  (  h  <  R  .  length  -  1  )  { 
if  (  R  [  h  ]  >  R  [  h  -  1  ]  ||  h  ==  1  )  { 
int   pk  =  h  ; 
while  (  h  +  2  <  R  .  length  &&  R  [  h  ]  ==  R  [  h  +  1  ]  )  { 
h  ++  ; 
} 
if  (  h  !=  pk  )  { 
if  (  R  [  h  ]  >  R  [  h  +  1  ]  )  { 
if  (  pk  ==  1  &&  R  [  0  ]  ==  R  [  1  ]  )  { 
pk  =  Math  .  round  (  (  float  )  (  pk  -  1  +  h  )  /  2  )  ; 
}  else  { 
pk  =  (  pk  +  h  )  /  2  ; 
} 
filtered  .  add  (  new   Tuple  <  Integer  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
}  else   if  (  h  +  2  ==  R  .  length  )  { 
if  (  R  [  h  ]  <  R  [  h  +  1  ]  )  { 
pk  =  h  +  1  ; 
}  else   if  (  R  [  h  ]  ==  R  [  h  +  1  ]  )  { 
pk  =  (  int  )  (  (  float  )  (  pk  +  h  +  1  )  /  2  )  ; 
}  else  { 
pk  =  (  pk  +  h  )  /  2  ; 
} 
filtered  .  add  (  new   Tuple  <  Integer  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
} 
}  else  { 
if  (  R  [  h  ]  >  R  [  h  +  1  ]  )  { 
filtered  .  add  (  new   Tuple  <  Integer  ,  Integer  >  (  R  [  pk  ]  ,  pk  )  )  ; 
} 
} 
} 
h  ++  ; 
} 
if  (  filtered  .  size  (  )  <  1  )  { 
filtered  .  add  (  new   Tuple  <  Integer  ,  Integer  >  (  R  [  R  .  length  -  1  ]  ,  R  .  length  -  1  )  )  ; 
} 
Tuple  <  Integer  ,  Integer  >  [  ]  X  =  separate_peaks  (  filtered  ,  R  ,  min_valley  )  ; 
return   X  ; 
} 







public   static   float   maximum  (  float  [  ]  list  )  { 
float   max  =  -  1  ; 
for  (  float   u  :  list  )  { 
if  (  u  >  max  )  { 
max  =  u  ; 
} 
} 
return   max  ; 
} 






public   static   float   area  (  float  [  ]  list  )  { 
float   area  =  0  ; 
for  (  float   u  :  list  )  { 
area  +=  (  u  >  0  )  ?  u  :  -  u  ; 
} 
return   area  ; 
} 






public   static   int   area  (  int  [  ]  list  )  { 
int   area  =  0  ; 
for  (  int   u  :  list  )  { 
area  +=  (  u  >  0  )  ?  u  :  -  u  ; 
} 
return   area  ; 
} 








public   static   float   area  (  float  [  ]  list  ,  int   start  ,  int   end  )  { 
float   area  =  0  ; 
for  (  int   u  =  start  ;  u  <=  end  ;  u  ++  )  { 
area  +=  (  list  [  u  ]  >  0  )  ?  list  [  u  ]  :  -  list  [  u  ]  ; 
} 
return   area  ; 
} 








public   static   int   area  (  int  [  ]  list  ,  int   start  ,  int   end  )  { 
int   area  =  0  ; 
for  (  int   u  =  start  ;  u  <=  end  ;  u  ++  )  { 
area  +=  (  list  [  u  ]  >  0  )  ?  list  [  u  ]  :  -  list  [  u  ]  ; 
} 
return   area  ; 
} 







public   static   int   maximum  (  int  [  ]  list  )  { 
int   max  =  -  1  ; 
for  (  int   u  :  list  )  { 
if  (  u  >  max  )  { 
max  =  u  ; 
} 
} 
return   max  ; 
} 









public   static   Tuple  <  Integer  ,  Integer  >  maximum_and_location  (  Map_i   map  )  { 
Tuple  <  Integer  ,  Integer  >  max  =  new   Tuple  <  Integer  ,  Integer  >  (  -  1  ,  -  1  )  ; 
int  [  ]  list  =  map  .  get_map  (  )  ; 
for  (  int   u  =  0  ;  u  <  list  .  length  ;  u  ++  )  { 
if  (  list  [  u  ]  >  max  .  get_first  (  )  )  { 
max  .  set_first  (  list  [  u  ]  )  ; 
max  .  set_second  (  u  )  ; 
} 
} 
return   max  ; 
} 







public   static   Tuple  <  Float  ,  Integer  >  maximum_and_location  (  Map_f   map  )  { 
Tuple  <  Float  ,  Integer  >  max  =  new   Tuple  <  Float  ,  Integer  >  (  -  1F  ,  -  1  )  ; 
float  [  ]  list  =  map  .  get_map  (  )  ; 
for  (  int   u  =  0  ;  u  <  list  .  length  ;  u  ++  )  { 
if  (  list  [  u  ]  >  max  .  get_first  (  )  )  { 
max  .  set_first  (  list  [  u  ]  )  ; 
max  .  set_second  (  u  )  ; 
} 
} 
return   max  ; 
} 









public   static   Tuple  <  Integer  ,  Integer  >  maximum_and_location  (  int  [  ]  list  )  { 
Tuple  <  Integer  ,  Integer  >  max  =  new   Tuple  <  Integer  ,  Integer  >  (  -  1  ,  -  1  )  ; 
for  (  int   u  =  0  ;  u  <  list  .  length  ;  u  ++  )  { 
if  (  list  [  u  ]  >  max  .  get_first  (  )  )  { 
max  .  set_first  (  list  [  u  ]  )  ; 
max  .  set_second  (  u  )  ; 
} 
} 
return   max  ; 
} 







public   static   Tuple  <  Float  ,  Integer  >  maximum_and_location  (  float  [  ]  list  )  { 
Tuple  <  Float  ,  Integer  >  max  =  new   Tuple  <  Float  ,  Integer  >  (  -  1F  ,  -  1  )  ; 
for  (  int   u  =  0  ;  u  <  list  .  length  ;  u  ++  )  { 
if  (  list  [  u  ]  >  max  .  get_first  (  )  )  { 
max  .  set_first  (  list  [  u  ]  )  ; 
max  .  set_second  (  u  )  ; 
} 
} 
return   max  ; 
} 











@  SuppressWarnings  (  "unchecked"  ) 
private   static   Tuple  <  Integer  ,  Integer  >  [  ]  separate_peaks  (  Vector  <  Tuple  <  Integer  ,  Integer  >  >  vi  ,  int  [  ]  R  ,  float   min_valley  )  throws   UnexpectedResultException  { 
int   A  =  -  1  ; 
int   B  =  -  1  ; 
boolean   found  =  false  ; 
int   p1  =  0  ; 
while  (  p1  <  vi  .  size  (  )  -  1  )  { 
Tuple  <  Integer  ,  Integer  >  aa  =  vi  .  get  (  p1  )  ; 
Tuple  <  Integer  ,  Integer  >  bb  =  vi  .  get  (  p1  +  1  )  ; 
A  =  aa  .  get_second  (  )  ; 
B  =  bb  .  get_second  (  )  ; 
int   small  =  (  aa  .  get_first  (  )  <  bb  .  get_first  (  )  )  ?  aa  .  get_first  (  )  :  bb  .  get_first  (  )  ; 
int   w  =  A  +  1  ; 
while  (  !  found  &&  w  <  B  )  { 
if  (  R  [  w  ]  <  small  *  min_valley  )  { 
found  =  true  ; 
p1  ++  ; 
} 
w  ++  ; 
} 
if  (  !  found  )  { 
if  (  R  [  A  ]  ==  small  )  { 
vi  .  remove  (  p1  )  ; 
}  else  { 
vi  .  remove  (  p1  +  1  )  ; 
} 
} 
found  =  false  ; 
} 
if  (  vi  .  size  (  )  <  1  )  { 
throw   new   UnexpectedResultException  (  "Warning: No maxima found for all_maxima()"  )  ; 
} 
return   vi  .  toArray  (  new   Tuple  [  vi  .  size  (  )  ]  )  ; 
} 











@  SuppressWarnings  (  "unchecked"  ) 
private   static   Tuple  <  Float  ,  Integer  >  [  ]  separate_peaks  (  Vector  <  Tuple  <  Float  ,  Integer  >  >  vi  ,  float  [  ]  R  ,  float   min_valley  )  throws   UnexpectedResultException  { 
int   A  =  -  1  ; 
int   B  =  -  1  ; 
boolean   found  =  false  ; 
int   p1  =  0  ; 
while  (  p1  <  vi  .  size  (  )  -  1  )  { 
Tuple  <  Float  ,  Integer  >  aa  =  vi  .  get  (  p1  )  ; 
Tuple  <  Float  ,  Integer  >  bb  =  vi  .  get  (  p1  +  1  )  ; 
A  =  aa  .  get_second  (  )  ; 
B  =  bb  .  get_second  (  )  ; 
float   small  =  FloatingPoint  .  smallest  (  aa  .  get_first  (  )  ,  bb  .  get_first  (  )  )  ; 
int   w  =  A  +  1  ; 
while  (  !  found  &&  w  <  B  )  { 
if  (  R  [  w  ]  <  small  *  min_valley  )  { 
found  =  true  ; 
p1  ++  ; 
} 
w  ++  ; 
} 
if  (  !  found  )  { 
if  (  FloatingPoint  .  are_values_equal  (  aa  .  get_first  (  )  ,  small  )  )  { 
vi  .  remove  (  p1  )  ; 
}  else  { 
vi  .  remove  (  p1  +  1  )  ; 
} 
} 
found  =  false  ; 
} 
if  (  vi  .  size  (  )  <  1  )  { 
throw   new   UnexpectedResultException  (  "Warning: No maxima found for all_maxima()"  )  ; 
} 
return   vi  .  toArray  (  new   Tuple  [  vi  .  size  (  )  ]  )  ; 
} 
} 

