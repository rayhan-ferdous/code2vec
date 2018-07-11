package   org  .  j3d  .  renderer  .  java3d  .  loaders  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  URL  ; 
import   com  .  sun  .  j3d  .  loaders  .  Scene  ; 
import   com  .  sun  .  j3d  .  loaders  .  SceneBase  ; 
import   com  .  sun  .  j3d  .  loaders  .  IncorrectFormatException  ; 
import   com  .  sun  .  j3d  .  loaders  .  ParsingErrorException  ; 
import   org  .  j3d  .  loaders  .  ac3d  .  parser  .  *  ; 







public   class   Ac3dLoader   implements   Loader  { 


private   LoaderTokenHandler   tokenHandler  ; 


private   int   flags  ; 


private   String   basePath  ; 


private   URL   baseUrl  ; 




public   Ac3dLoader  (  )  { 
tokenHandler  =  new   LoaderTokenHandler  (  )  ; 
flags  =  0  ; 
basePath  =  null  ; 
baseUrl  =  null  ; 
} 









public   Scene   load  (  String   fileName  )  throws   FileNotFoundException  ,  IncorrectFormatException  ,  ParsingErrorException  { 
Reader   reader  ; 
if  (  basePath  ==  null  )  tokenHandler  .  setBasePath  (  new   File  (  fileName  )  .  getParent  (  )  )  ;  else   tokenHandler  .  setBasePath  (  basePath  )  ; 
reader  =  new   FileReader  (  fileName  )  ; 
return   load  (  reader  )  ; 
} 









public   Scene   load  (  Reader   reader  )  throws   FileNotFoundException  ,  IncorrectFormatException  ,  ParsingErrorException  { 
Scene   rVal  =  null  ; 
BufferedReader   br  =  new   BufferedReader  (  reader  )  ; 
Ac3dParser   parser  =  new   Ac3dParser  (  )  ; 
try  { 
parser  .  setBufferedReader  (  br  )  ; 
tokenHandler  .  setBufferedReader  (  br  )  ; 
parser  .  setTokenHandler  (  tokenHandler  )  ; 
parser  .  parse  (  )  ; 
rVal  =  tokenHandler  .  getScene  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  err  .  println  (  "Exception during parse: "  +  e  .  getMessage  (  )  )  ; 
e  .  printStackTrace  (  System  .  err  )  ; 
} 
return   rVal  ; 
} 









public   Scene   load  (  URL   url  )  throws   FileNotFoundException  ,  IncorrectFormatException  ,  ParsingErrorException  { 
Reader   reader  ; 
if  (  baseUrl  ==  null  )  tokenHandler  .  setBaseUrl  (  url  )  ;  else   tokenHandler  .  setBaseUrl  (  baseUrl  )  ; 
try  { 
reader  =  new   InputStreamReader  (  url  .  openStream  (  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   FileNotFoundException  (  e  .  getMessage  (  )  )  ; 
} 
return   load  (  reader  )  ; 
} 



public   void   setFlags  (  int   flags  )  { 
this  .  flags  =  flags  ; 
} 






public   int   getFlags  (  )  { 
return   flags  ; 
} 




public   void   setBaseUrl  (  URL   baseUrl  )  { 
this  .  baseUrl  =  baseUrl  ; 
} 




public   URL   getBaseUrl  (  )  { 
return   baseUrl  ; 
} 







public   void   setBasePath  (  String   basePath  )  { 
this  .  basePath  =  basePath  ; 
} 






public   String   getBasePath  (  )  { 
return   basePath  ; 
} 
} 

