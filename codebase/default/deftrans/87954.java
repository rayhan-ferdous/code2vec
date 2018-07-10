import   java  .  io  .  *  ; 







public   class   NarrWriter  { 

private   BufferedWriter   output  ; 




private   StringBuffer   allText  =  new   StringBuffer  (  )  ; 





public   NarrWriter  (  File   output  )  { 
try  { 
this  .  output  =  new   BufferedWriter  (  new   FileWriter  (  output  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   void   print  (  String   s  )  { 
try  { 
output  .  write  (  s  )  ; 
allText  .  append  (  s  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 





public   void   println  (  String   s  )  { 
try  { 
output  .  write  (  s  )  ; 
output  .  newLine  (  )  ; 
allText  .  append  (  s  +  "\n"  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   void   println  (  )  { 
try  { 
output  .  newLine  (  )  ; 
allText  .  append  (  "\n"  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 




public   void   close  (  )  { 
try  { 
output  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 








public   void   writeInput  (  File   inputFile  )  { 
BufferedReader   input  ; 
try  { 
input  =  new   BufferedReader  (  new   FileReader  (  inputFile  )  )  ; 
String   nextLine  =  input  .  readLine  (  )  ; 
while  (  nextLine  !=  null  )  { 
println  (  nextLine  )  ; 
nextLine  =  input  .  readLine  (  )  ; 
} 
input  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   String   getText  (  )  { 
return   allText  .  toString  (  )  ; 
} 
} 

