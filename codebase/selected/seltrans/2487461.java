package   jsslib  .  shell  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Hashtable  ; 





public   class   Exec  { 




private   static   Hashtable  <  String  ,  String  >  env_hash  =  new   Hashtable  <  String  ,  String  >  (  )  ; 






public   static   void   setEnv  (  String   var  ,  String   value  )  { 
env_hash  .  put  (  var  ,  value  )  ; 
} 






public   static   String   getEnv  (  String   var  )  { 
String   result  =  env_hash  .  get  (  var  )  ; 
return   result  ; 
} 




public   static   void   inheritBashEnv  (  )  throws   IOException  { 
String   env_out  =  runToString  (  "bash -l -c env"  ,  null  )  ; 
String  [  ]  lines  =  env_out  .  split  (  "\n"  )  ; 
for  (  int   i  =  0  ;  i  <  lines  .  length  ;  i  ++  )  { 
String  [  ]  nameValue  =  lines  [  i  ]  .  split  (  "="  )  ; 
if  (  nameValue  .  length  ==  2  )  setEnv  (  nameValue  [  0  ]  ,  nameValue  [  1  ]  )  ; 
} 
} 






public   static   String   which  (  String   command  )  throws   IOException  { 
String   result  =  runToString  (  "which "  +  command  ,  null  )  ; 
result  =  result  .  replace  (  "\n"  ,  ""  )  ; 
return   result  ; 
} 




public   static   String  [  ]  getEnvStrArr  (  )  { 
Enumeration  <  String  >  vars  =  env_hash  .  keys  (  )  ; 
ArrayList  <  String  >  all  =  new   ArrayList  <  String  >  (  )  ; 
while  (  vars  .  hasMoreElements  (  )  )  { 
String   key  =  vars  .  nextElement  (  )  ; 
all  .  add  (  key  +  "="  +  env_hash  .  get  (  key  )  )  ; 
} 
Object  [  ]  all_obj  =  all  .  toArray  (  )  ; 
if  (  all_obj  .  length  ==  0  )  return   null  ; 
String  [  ]  result  =  new   String  [  all_obj  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  result  .  length  ;  i  ++  )  result  [  i  ]  =  (  String  )  all_obj  [  i  ]  ; 
return   result  ; 
} 







public   static   void   runAndWait  (  String   command  ,  String   dir  )  throws   IOException  { 
try  { 
Process   pro  =  Runtime  .  getRuntime  (  )  .  exec  (  command  ,  getEnvStrArr  (  )  ,  new   File  (  dir  )  )  ; 
pro  .  waitFor  (  )  ; 
pro  .  getInputStream  (  )  .  close  (  )  ; 
pro  .  getOutputStream  (  )  .  close  (  )  ; 
pro  .  getErrorStream  (  )  .  close  (  )  ; 
}  catch  (  InterruptedException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 








public   static   String   runToString  (  String   command  ,  String   dir  )  throws   IOException  { 
StringBuilder   output  =  new   StringBuilder  (  )  ; 
char  [  ]  puffer  =  new   char  [  1000  ]  ; 
File   fdir  =  null  ; 
if  (  dir  !=  null  )  fdir  =  new   File  (  dir  )  ; 
Process   prozess  =  Runtime  .  getRuntime  (  )  .  exec  (  command  ,  getEnvStrArr  (  )  ,  fdir  )  ; 
BufferedReader   ausgabe  =  new   BufferedReader  (  new   InputStreamReader  (  prozess  .  getInputStream  (  )  )  )  ; 
BufferedReader   error  =  new   BufferedReader  (  new   InputStreamReader  (  prozess  .  getErrorStream  (  )  )  )  ; 
while  (  true  )  { 
while  (  ausgabe  .  ready  (  )  )  { 
int   anzahl  =  ausgabe  .  read  (  puffer  ,  0  ,  1000  )  ; 
for  (  int   x  =  0  ;  x  <  anzahl  ;  x  ++  )  output  .  append  (  puffer  [  x  ]  )  ; 
} 
while  (  error  .  ready  (  )  )  { 
int   anzahl  =  error  .  read  (  puffer  ,  0  ,  1000  )  ; 
for  (  int   x  =  0  ;  x  <  anzahl  ;  x  ++  )  output  .  append  (  puffer  [  x  ]  )  ; 
} 
try  { 
int   wert  =  prozess  .  exitValue  (  )  ; 
break  ; 
}  catch  (  IllegalThreadStateException   ex  )  { 
} 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   ex  )  { 
} 
} 
ausgabe  .  close  (  )  ; 
error  .  close  (  )  ; 
prozess  .  getOutputStream  (  )  .  close  (  )  ; 
return   output  .  toString  (  )  ; 
} 








public   static   void   runToFile  (  String   command  ,  String   dir  ,  String   outputfile  )  throws   IOException  { 
byte  [  ]  puffer  =  new   byte  [  10000  ]  ; 
File   fdir  =  null  ; 
if  (  dir  !=  null  )  fdir  =  new   File  (  dir  )  ; 
Process   prozess  =  Runtime  .  getRuntime  (  )  .  exec  (  command  ,  getEnvStrArr  (  )  ,  fdir  )  ; 
BufferedOutputStream   output  =  new   BufferedOutputStream  (  new   FileOutputStream  (  outputfile  )  )  ; 
int   anzahl  =  -  1  ; 
BufferedInputStream   input  =  new   BufferedInputStream  (  prozess  .  getInputStream  (  )  )  ; 
BufferedInputStream   error  =  new   BufferedInputStream  (  prozess  .  getErrorStream  (  )  )  ; 
while  (  true  )  { 
while  (  input  .  available  (  )  >  0  )  { 
output  .  write  (  input  .  read  (  )  )  ; 
} 
while  (  error  .  available  (  )  >  0  )  { 
output  .  write  (  error  .  read  (  )  )  ; 
} 
try  { 
int   wert  =  prozess  .  exitValue  (  )  ; 
break  ; 
}  catch  (  IllegalThreadStateException   ex  )  { 
} 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   ex  )  { 
} 
} 
output  .  flush  (  )  ; 
output  .  close  (  )  ; 
input  .  close  (  )  ; 
error  .  close  (  )  ; 
prozess  .  getOutputStream  (  )  .  close  (  )  ; 
} 
} 

