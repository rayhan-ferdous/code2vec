import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 








public   class   BinningFasta  { 

public   BinningFasta  (  )  { 
} 












public   static   int   readRGlength  (  File   data  )  throws   IOException  { 
BufferedReader   input  ; 
try  { 
input  =  new   BufferedReader  (  new   FileReader  (  data  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
return  -  1  ; 
} 
String   nextLine  =  input  .  readLine  (  )  ; 
StringTokenizer   a  =  new   StringTokenizer  (  nextLine  )  ; 
a  .  nextToken  (  )  ; 
if  (  a  ==  null  )  return  -  1  ; 
String   b  =  a  .  nextToken  (  )  ; 
if  (  b  ==  null  )  return  -  1  ; 
input  .  close  (  )  ; 
return  (  new   Integer  (  b  )  )  .  intValue  (  )  ; 
} 
















public   static   int  [  ]  readFile  (  File   fasta  ,  File   outFile  ,  File   numbers  )  throws   IOException  { 
BufferedReader   input  ; 
try  { 
input  =  new   BufferedReader  (  new   FileReader  (  fasta  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Input file not found."  )  ; 
return   null  ; 
} 
BufferedWriter   out  ; 
try  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  outFile  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Error in writing output file."  )  ; 
return   null  ; 
} 
ArrayList   data  =  new   ArrayList  (  )  ; 
int   numSequences  =  0  ; 
int   lenSequence  =  0  ; 
String   nextLine  =  input  .  readLine  (  )  ; 
if  (  nextLine  ==  null  )  { 
System  .  out  .  println  (  "Empty Input File"  )  ; 
return   null  ; 
} 
String   gene  =  new   String  (  ""  )  ; 
while  (  nextLine  !=  null  )  { 
if  (  nextLine  .  charAt  (  0  )  !=  '>'  )  { 
System  .  out  .  println  (  "Invalid input file."  )  ; 
return   null  ; 
} 
data  .  add  (  nextLine  )  ; 
nextLine  =  input  .  readLine  (  )  ; 
if  (  nextLine  ==  null  )  { 
System  .  out  .  println  (  "Invalid input file. There is a gene name "  +  "with no corresponding gene sequence"  )  ; 
return   null  ; 
} 
gene  =  nextLine  ; 
numSequences  ++  ; 
data  .  add  (  nextLine  )  ; 
nextLine  =  input  .  readLine  (  )  ; 
} 
lenSequence  =  gene  .  length  (  )  ; 
input  .  close  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  size  (  )  ;  i  ++  )  { 
out  .  write  (  (  String  )  data  .  get  (  i  )  )  ; 
out  .  newLine  (  )  ; 
} 
out  .  close  (  )  ; 
int  [  ]  retArray  =  {  numSequences  ,  lenSequence  }  ; 
writeNumbersDat  (  numbers  ,  retArray  )  ; 
return   retArray  ; 
} 











public   static   void   writeNumbersDat  (  File   numbers  ,  int  [  ]  values  )  throws   IOException  { 
BufferedWriter   out  ; 
try  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  numbers  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Invalid output file!!"  )  ; 
return  ; 
} 
out  .  write  (  ""  +  values  [  0  ]  +  ", "  +  values  [  1  ]  )  ; 
out  .  newLine  (  )  ; 
out  .  newLine  (  )  ; 
out  .  write  (  "numbers.dat"  )  ; 
out  .  close  (  )  ; 
} 














public   static   ArrayList   readBins  (  File   data  ,  NarrWriter   narr  )  throws   IOException  { 
BufferedReader   input  ; 
try  { 
input  =  new   BufferedReader  (  new   FileReader  (  data  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Invalid input file file won't open"  )  ; 
return   null  ; 
} 
String   nextLine  =  input  .  readLine  (  )  ; 
if  (  nextLine  ==  null  )  { 
System  .  out  .  println  (  "Invalid input file first line null"  )  ; 
return   null  ; 
} 
String   storage  =  nextLine  ; 
StringTokenizer   a  ; 
String   x  ; 
int   value  ; 
while  (  nextLine  !=  null  )  { 
a  =  new   StringTokenizer  (  nextLine  )  ; 
a  .  nextToken  (  )  ; 
if  (  !  a  .  hasMoreTokens  (  )  )  { 
System  .  out  .  println  (  "Invalid input file one line doesn't have "  +  "2 tokens"  )  ; 
return   null  ; 
} 
value  =  (  new   Integer  (  a  .  nextToken  (  )  )  )  .  intValue  (  )  ; 
if  (  value  >  1  )  break  ; 
narr  .  println  (  nextLine  )  ; 
storage  =  nextLine  ; 
nextLine  =  input  .  readLine  (  )  ; 
} 
ArrayList   output  =  new   ArrayList  (  )  ; 
output  .  add  (  storage  )  ; 
while  (  nextLine  !=  null  )  { 
output  .  add  (  nextLine  )  ; 
narr  .  println  (  nextLine  )  ; 
nextLine  =  input  .  readLine  (  )  ; 
} 
input  .  close  (  )  ; 
return   output  ; 
} 

















public   static   FredOutVal   chooseBest  (  FredOutVal  [  ]  values  ,  File   debug  )  throws   IOException  { 
BufferedWriter   out  ; 
try  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  debug  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Error creating output file."  )  ; 
return   null  ; 
} 
int   startPer  =  MasterVariables  .  getSortPercentage  (  )  ; 
double  [  ]  storage  ; 
int   i  ; 
double   twoHits  =  (  double  )  2  /  MasterVariables  .  getnrep  (  )  ; 
for  (  startPer  =  startPer  ;  startPer  >  0  ;  startPer  --  )  { 
for  (  i  =  0  ;  i  <  values  .  length  ;  i  ++  )  { 
storage  =  values  [  i  ]  .  getPercentages  (  )  ; 
if  (  storage  [  startPer  ]  >  twoHits  ||  storage  [  startPer  ]  ==  twoHits  )  break  ; 
} 
if  (  i  !=  values  .  length  )  break  ; 
} 
MasterVariables  .  setSortPercentage  (  startPer  )  ; 
Heapsorter  .  heapSort  (  values  )  ; 
for  (  int   j  =  0  ;  j  <  values  .  length  ;  j  ++  )  { 
out  .  write  (  values  [  j  ]  .  toString  (  )  )  ; 
out  .  newLine  (  )  ; 
} 
out  .  close  (  )  ; 
return   values  [  0  ]  ; 
} 





public   static   boolean   verifyInputFile  (  File   file  )  { 
try  { 
BufferedReader   input  =  new   BufferedReader  (  new   FileReader  (  file  )  )  ; 
String   line  =  input  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
if  (  line  .  charAt  (  0  )  !=  '>'  )  { 
input  .  close  (  )  ; 
return   false  ; 
} 
line  =  input  .  readLine  (  )  ; 
if  (  line  ==  null  )  { 
input  .  close  (  )  ; 
return   false  ; 
} 
if  (  line  .  charAt  (  0  )  ==  '>'  )  { 
input  .  close  (  )  ; 
return   false  ; 
} 
line  =  input  .  readLine  (  )  ; 
} 
input  .  close  (  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 
} 

