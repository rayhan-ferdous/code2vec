import   com  .  ibm  .  JikesRVM  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 

public   class   JDPServer   implements   JDPCommandInterface  { 




private   ServerSocket   serverSocket  ; 




private   Socket   clientSocket  ; 




private   ObjectOutputStream   out  ; 




private   BufferedReader   in  ; 




int   port  ; 





public   JDPServer  (  int   initialPort  )  { 
port  =  initialPort  ; 
boolean   socketOpened  =  false  ; 
while  (  !  socketOpened  )  { 
try  { 
serverSocket  =  new   ServerSocket  (  port  )  ; 
System  .  out  .  println  (  "JDPServer running on port "  +  port  )  ; 
socketOpened  =  true  ; 
}  catch  (  IOException   e  )  { 
if  (  ++  port  >  65536  )  { 
System  .  err  .  println  (  "No available port"  )  ; 
System  .  exit  (  1  )  ; 
} 
} 
} 
} 




public   void   acceptConnection  (  )  { 
System  .  out  .  println  (  "Waiting for connection..."  )  ; 
try  { 
clientSocket  =  serverSocket  .  accept  (  )  ; 
System  .  out  .  println  (  "Connection detected!"  )  ; 
out  =  new   ObjectOutputStream  (  clientSocket  .  getOutputStream  (  )  )  ; 
in  =  new   BufferedReader  (  new   InputStreamReader  (  clientSocket  .  getInputStream  (  )  )  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "Accept failed."  )  ; 
System  .  exit  (  1  )  ; 
} 
} 




public   void   closeConnection  (  )  { 
try  { 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
clientSocket  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "Close failed."  )  ; 
} 
} 




public   void   writeCommand  (  String   command  )  { 
} 




private   String   cmd  =  "?"  ; 




private   String   args  [  ]  =  new   String  [  0  ]  ; 

public   void   readCommand  (  OsProcess   proc  )  { 
String   input  =  null  ; 
try  { 
while  (  (  input  =  in  .  readLine  (  )  )  ==  null  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "readCommand failed."  )  ; 
System  .  exit  (  1  )  ; 
} 
String  [  ]  words  =  toArgs  (  input  )  ; 
if  (  words  !=  null  )  { 
if  (  words  .  length  !=  0  )  { 
cmd  =  words  [  0  ]  ; 
args  =  new   String  [  words  .  length  -  1  ]  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  ++  i  )  args  [  i  ]  =  words  [  i  +  1  ]  ; 
} 
} 
} 







private   String  [  ]  toArgs  (  String   line  )  { 
String   lineargs  [  ]  ; 
String   str  =  new   String  (  line  )  ; 
Vector   vec  =  new   Vector  (  )  ; 
for  (  ;  ;  )  { 
while  (  str  .  startsWith  (  " "  )  )  str  =  str  .  substring  (  1  )  ; 
if  (  str  .  length  (  )  ==  0  )  break  ; 
int   end  =  str  .  indexOf  (  " "  )  ; 
if  (  end  ==  -  1  )  { 
vec  .  addElement  (  str  )  ; 
str  =  ""  ; 
}  else  { 
vec  .  addElement  (  str  .  substring  (  0  ,  end  )  )  ; 
str  =  str  .  substring  (  end  )  ; 
} 
} 
if  (  vec  .  size  (  )  !=  0  )  { 
lineargs  =  new   String  [  vec  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  lineargs  .  length  ;  ++  i  )  lineargs  [  i  ]  =  (  String  )  vec  .  elementAt  (  i  )  ; 
return   lineargs  ; 
} 
return   null  ; 
} 

public   String   cmd  (  )  { 
return   cmd  ; 
} 

public   String  [  ]  args  (  )  { 
return   args  ; 
} 





public   void   writeOutput  (  Object   output  )  { 
try  { 
out  .  writeObject  (  output  )  ; 
}  catch  (  IOException   e  )  { 
System  .  err  .  println  (  "error writing object "  +  output  )  ; 
System  .  exit  (  1  )  ; 
} 
} 
} 

