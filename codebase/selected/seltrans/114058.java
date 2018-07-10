package   csimage  .  util  ; 

import   java  .  awt  .  Color  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Arrays  ; 
import   javax  .  swing  .  JColorChooser  ; 
import   javax  .  swing  .  JFileChooser  ; 

public   class   EasyInput  { 







public   static   String   input  (  )  { 
try  { 
return   rawInput  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "An IOException has occurred inside the input() method."  )  ; 
} 
} 










public   static   String   input  (  String   prompt  )  { 
try  { 
return   rawInput  (  prompt  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "An IOException has occurred inside the input(String prompt) method."  )  ; 
} 
} 









public   static   String   rawInput  (  )  throws   IOException  { 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  System  .  in  )  )  ; 
String   message  =  in  .  readLine  (  )  ; 
return   message  ; 
} 












public   static   String   rawInput  (  String   prompt  )  throws   IOException  { 
System  .  out  .  print  (  prompt  )  ; 
return   rawInput  (  )  ; 
} 







public   static   Color   chooseColor  (  )  { 
Color   c  =  JColorChooser  .  showDialog  (  null  ,  "Color Chooser"  ,  Color  .  RED  )  ; 
if  (  c  ==  null  )  { 
throw   new   Error  (  "No color chosen."  )  ; 
}  else  { 
return   c  ; 
} 
} 







public   static   String   getcwd  (  )  { 
String   cwd  =  System  .  getProperty  (  "user.dir"  )  ; 
return   cwd  ; 
} 





public   static   void   printCwd  (  )  { 
System  .  out  .  println  (  "Current working directory: "  +  getcwd  (  )  )  ; 
} 



















public   static   String  [  ]  listdir  (  )  { 
return   listdir  (  getcwd  (  )  )  ; 
} 





















public   static   String  [  ]  listdir  (  String   path  )  { 
return   listdir  (  new   File  (  path  )  )  ; 
} 

public   static   String  [  ]  listdir  (  File   dir  )  { 
return   dir  .  list  (  )  ; 
} 








public   static   String   openFileDialog  (  )  { 
JFileChooser   fc  =  new   JFileChooser  (  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  null  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fc  .  getSelectedFile  (  )  ; 
return   file  .  getPath  (  )  ; 
}  else  { 
throw   new   Error  (  "openFileDialog error: No file selected."  )  ; 
} 
} 










public   static   String   rawReadfile  (  String   path  )  throws   IOException  { 
FileReader   in  =  new   FileReader  (  new   File  (  path  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   c  =  -  1  ;  (  c  =  in  .  read  (  )  )  !=  -  1  ;  )  { 
sb  .  append  (  c  )  ; 
} 
in  .  close  (  )  ; 
return   sb  .  toString  (  )  ; 
} 









public   static   String   readfile  (  String   path  )  { 
try  { 
FileReader   in  =  new   FileReader  (  new   File  (  path  )  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   c  =  -  1  ;  (  c  =  in  .  read  (  )  )  !=  -  1  ;  )  { 
sb  .  append  (  (  char  )  c  )  ; 
} 
in  .  close  (  )  ; 
return   sb  .  toString  (  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   Error  (  "\nThe file "  +  dbg  .  quote  (  path  )  +  " could not be found in the directory:\n"  +  dbg  .  quote  (  getcwd  (  )  )  +  "\n"  +  "Check that the file you want to read is in the directory\n"  +  "you think it's in, and check that all names are spelled\n"  +  "correctly, including any spaces or punctuation. The case\n"  +  "of a file name matters, e.g. \"MyFile.txt\" is different\n"  +  "than \"myfile.txt\""  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "An IOException has been thrown by the readFile method:\n"  +  e  )  ; 
} 
} 








public   static   String   getWebPage  (  String   url  )  { 
try  { 
return   getWebPage  (  new   URL  (  url  )  )  ; 
}  catch  (  MalformedURLException   urlEx  )  { 
throw   new   Error  (  "URL creation failed:\n"  +  dbg  .  quote  (  url  )  +  "\nis not a validly formatted URL."  )  ; 
} 
} 








public   static   String   getWebPage  (  URL   urlObj  )  { 
try  { 
String   content  =  ""  ; 
InputStreamReader   is  =  new   InputStreamReader  (  urlObj  .  openStream  (  )  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  is  )  ; 
String   line  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
content  +=  line  ; 
} 
return   content  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  "The page "  +  dbg  .  quote  (  urlObj  .  toString  (  )  )  +  "could not be retrieved."  +  "\nThis is could be caused by a number of things:"  +  "\n"  +  "\n  - the computer hosting the web page you want is down, or has returned an error"  +  "\n  - your computer does not have Internet access"  +  "\n  - the heat death of the universe has occurred, taking down all web servers with it"  )  ; 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
System  .  out  .  println  (  "This is a test of the EasyInput class ...\n"  )  ; 
String   name  =  input  (  "What is your name? "  )  ; 
String   age  =  input  (  "What is your age?"  )  ; 
System  .  out  .  println  (  "name = "  +  dbg  .  quote  (  name  )  )  ; 
System  .  out  .  println  (  "age = "  +  dbg  .  quote  (  age  )  )  ; 
System  .  out  .  println  (  "Current working directory = "  +  dbg  .  quote  (  getcwd  (  )  )  )  ; 
System  .  out  .  println  (  "Files and folders: "  +  Arrays  .  asList  (  listdir  (  )  )  )  ; 
String   fname  =  openFileDialog  (  )  ; 
System  .  out  .  println  (  fname  )  ; 
String   file  =  readfile  (  fname  )  ; 
System  .  out  .  println  (  file  )  ; 
} 
} 

