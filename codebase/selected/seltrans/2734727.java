package   org  .  expasy  .  jpl  .  insilico  .  ms  .  peak  ; 

public   class   JPLMSPeakQuickSorter  { 

public   static   final   void   sort  (  JPLITheoMSPeak  [  ]  peaks  )  { 
if  (  peaks  .  length  >  0  )  { 
sort  (  peaks  ,  0  ,  peaks  .  length  -  1  )  ; 
} 
} 









public   static   final   void   sort  (  JPLITheoMSPeak  [  ]  peaks  ,  int   from  ,  int   to  )  { 
if  (  from  >=  to  )  { 
return  ; 
} 
int   mid  =  (  from  +  to  )  /  2  ; 
JPLITheoMSPeak   middle  =  peaks  [  mid  ]  ; 
JPLITheoMSPeak   tmp  ; 
if  (  peaks  [  from  ]  .  compareTo  (  middle  )  >  0  )  { 
peaks  [  mid  ]  =  peaks  [  from  ]  ; 
peaks  [  from  ]  =  middle  ; 
middle  =  peaks  [  mid  ]  ; 
} 
if  (  middle  .  compareTo  (  peaks  [  to  ]  )  >  0  )  { 
peaks  [  mid  ]  =  peaks  [  to  ]  ; 
peaks  [  to  ]  =  middle  ; 
middle  =  peaks  [  mid  ]  ; 
if  (  peaks  [  from  ]  .  compareTo  (  middle  )  >  0  )  { 
peaks  [  mid  ]  =  peaks  [  from  ]  ; 
peaks  [  from  ]  =  middle  ; 
middle  =  peaks  [  mid  ]  ; 
} 
} 
int   left  =  from  +  1  ; 
int   right  =  to  -  1  ; 
if  (  left  >=  right  )  { 
return  ; 
} 
for  (  ;  ;  )  { 
while  (  peaks  [  right  ]  .  compareTo  (  middle  )  >  0  )  { 
right  --  ; 
} 
while  (  left  <  right  &&  peaks  [  left  ]  .  compareTo  (  middle  )  <=  0  )  { 
left  ++  ; 
} 
if  (  left  <  right  )  { 
tmp  =  peaks  [  left  ]  ; 
peaks  [  left  ]  =  peaks  [  right  ]  ; 
peaks  [  right  ]  =  tmp  ; 
right  --  ; 
}  else  { 
break  ; 
} 
} 
sort  (  peaks  ,  from  ,  left  )  ; 
sort  (  peaks  ,  left  +  1  ,  to  )  ; 
} 



public   static   final   void   quickSort  (  JPLITheoMSPeak  [  ]  peaks  )  { 
if  (  peaks  .  length  >  0  )  { 
quickSort  (  peaks  ,  0  ,  peaks  .  length  -  1  )  ; 
} 
} 


























public   static   final   void   quickSort  (  JPLITheoMSPeak  [  ]  peaks  ,  int   lowIndex  ,  int   highIndex  )  { 
int   lowToHighIndex  ; 
int   highToLowIndex  ; 
int   pivotIndex  ; 
JPLITheoMSPeak   pivotValue  ; 
JPLITheoMSPeak   lowToHighValue  ; 
JPLITheoMSPeak   highToLowValue  ; 
JPLITheoMSPeak   parking  ; 
int   newLowIndex  ; 
int   newHighIndex  ; 
int   compareResult  ; 
lowToHighIndex  =  lowIndex  ; 
highToLowIndex  =  highIndex  ; 
pivotIndex  =  (  lowToHighIndex  +  highToLowIndex  )  /  2  ; 
pivotValue  =  peaks  [  pivotIndex  ]  ; 
newLowIndex  =  highIndex  +  1  ; 
newHighIndex  =  lowIndex  -  1  ; 
while  (  (  newHighIndex  +  1  )  <  newLowIndex  )  { 
lowToHighValue  =  peaks  [  lowToHighIndex  ]  ; 
while  (  lowToHighIndex  <  newLowIndex  &  lowToHighValue  .  compareTo  (  pivotValue  )  <  0  )  { 
newHighIndex  =  lowToHighIndex  ; 
lowToHighIndex  ++  ; 
lowToHighValue  =  peaks  [  lowToHighIndex  ]  ; 
} 
highToLowValue  =  peaks  [  highToLowIndex  ]  ; 
while  (  newHighIndex  <=  highToLowIndex  &  (  highToLowValue  .  compareTo  (  pivotValue  )  >  0  )  )  { 
newLowIndex  =  highToLowIndex  ; 
highToLowIndex  --  ; 
highToLowValue  =  peaks  [  highToLowIndex  ]  ; 
} 
if  (  lowToHighIndex  ==  highToLowIndex  )  { 
newHighIndex  =  lowToHighIndex  ; 
}  else   if  (  lowToHighIndex  <  highToLowIndex  )  { 
compareResult  =  lowToHighValue  .  compareTo  (  highToLowValue  )  ; 
if  (  compareResult  >=  0  )  { 
parking  =  lowToHighValue  ; 
peaks  [  lowToHighIndex  ]  =  highToLowValue  ; 
peaks  [  highToLowIndex  ]  =  parking  ; 
newLowIndex  =  highToLowIndex  ; 
newHighIndex  =  lowToHighIndex  ; 
lowToHighIndex  ++  ; 
highToLowIndex  --  ; 
} 
} 
} 
if  (  lowIndex  <  newHighIndex  )  { 
quickSort  (  peaks  ,  lowIndex  ,  newHighIndex  )  ; 
} 
if  (  newLowIndex  <  highIndex  )  { 
quickSort  (  peaks  ,  newLowIndex  ,  highIndex  )  ; 
} 
} 
} 

