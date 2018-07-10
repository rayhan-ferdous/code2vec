package   jderead  ; 

import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 










public   class   DE423   extends   DEheader  { 




private   double  [  ]  startfiledates  =  {  2378480.5  ,  2396752.5  ,  2414992.5  ,  2433264.5  ,  2451536.5  ,  2469776.5  ,  2488048.5  ,  2506320.5  ,  2524624.5  }  ; 




private   String  [  ]  filenames  =  {  "ascp1800.423"  ,  "ascp1850.423"  ,  "ascp1900.423"  ,  "ascp1950.423"  ,  "ascp2000.423"  ,  "ascp2050.423"  ,  "ascp2100.423"  ,  "ascp2150.423"  }  ; 




public   DE423  (  )  { 
numbersperinterval  =  1016  ; 
denomber  =  423  ; 
startepoch  =  2378480.50  ; 
finalepoch  =  2524624.50  ; 
intervalduration  =  32  ; 
clight  =  0.299792458000000000e+06  ; 
au  =  0.149597870699626200e+09  ; 
emrat  =  0.813005694159985700e+02  ; 
gm1  =  0.491249717333700100e-10  ; 
gm2  =  0.724345233269844100e-09  ; 
gmb  =  0.899701140826804900e-09  ; 
gm4  =  0.954954869562239000e-10  ; 
gm5  =  0.282534584085505000e-06  ; 
gm6  =  0.845970607330847800e-07  ; 
gm7  =  0.129202482579265000e-07  ; 
gm8  =  0.152435910924974000e-07  ; 
gm9  =  0.217844105199052000e-11  ; 
gms  =  0.295912208285591100e-03  ; 
ephemeriscoefficients  =  new   double  [  581153  ]  ; 
} 













@  Override 
protected   boolean   readEphCoeff  (  double   jultime  )  { 
boolean   result  =  false  ; 
if  (  (  jultime  <  this  .  startepoch  )  ||  (  jultime  >=  this  .  finalepoch  )  )  { 
return   result  ; 
} 
if  (  (  jultime  <  this  .  ephemerisdates  [  1  ]  )  ||  (  jultime  >=  this  .  ephemerisdates  [  2  ]  )  )  { 
int   i  =  0  ; 
int   records  =  0  ; 
int   j  =  0  ; 
String   filename  =  " "  ; 
char  [  ]  cline  =  new   char  [  80  ]  ; 
try  { 
for  (  i  =  0  ;  i  <  startfiledates  .  length  -  1  ;  i  ++  )  { 
if  (  (  jultime  >=  startfiledates  [  i  ]  )  &&  (  jultime  <  startfiledates  [  i  +  1  ]  )  )  { 
ephemerisdates  [  1  ]  =  startfiledates  [  i  ]  ; 
ephemerisdates  [  2  ]  =  startfiledates  [  i  +  1  ]  ; 
filename  =  filenames  [  i  ]  ; 
records  =  (  int  )  (  (  ephemerisdates  [  2  ]  -  ephemerisdates  [  1  ]  )  /  intervalduration  )  ; 
} 
} 
filename  =  this  .  patheph  +  filename  ; 
FileReader   file  =  new   FileReader  (  filename  )  ; 
for  (  j  =  1  ;  j  <=  records  ;  j  ++  )  { 
file  .  read  (  cline  ,  0  ,  13  )  ; 
for  (  i  =  2  ;  i  <=  341  ;  i  ++  )  { 
file  .  read  (  cline  ,  0  ,  79  )  ; 
cline  [  22  ]  =  'e'  ; 
cline  [  48  ]  =  'e'  ; 
cline  [  74  ]  =  'e'  ; 
if  (  i  >  2  )  { 
ephemeriscoefficients  [  (  j  -  1  )  *  numbersperinterval  +  (  3  *  (  i  -  2  )  -  1  )  ]  =  Double  .  parseDouble  (  String  .  valueOf  (  cline  ,  1  ,  25  )  )  ; 
} 
if  (  (  i  >  2  )  &  (  i  <  341  )  )  { 
ephemeriscoefficients  [  (  j  -  1  )  *  numbersperinterval  +  3  *  (  i  -  2  )  ]  =  Double  .  parseDouble  (  String  .  valueOf  (  cline  ,  27  ,  25  )  )  ; 
} 
if  (  i  <  341  )  { 
ephemeriscoefficients  [  (  j  -  1  )  *  numbersperinterval  +  (  3  *  (  i  -  2  )  +  1  )  ]  =  Double  .  parseDouble  (  String  .  valueOf  (  cline  ,  53  ,  25  )  )  ; 
} 
} 
} 
file  .  close  (  )  ; 
result  =  true  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Error = "  +  e  .  toString  (  )  )  ; 
}  catch  (  StringIndexOutOfBoundsException   e  )  { 
System  .  out  .  println  (  "Error = "  +  e  .  toString  (  )  )  ; 
} 
}  else  { 
result  =  true  ; 
} 
return   result  ; 
} 
} 

