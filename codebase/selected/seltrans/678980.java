package   net  .  sf  .  jplist  .  input  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   net  .  sf  .  jplist  .  core  .  Document  ; 
import   net  .  sf  .  jplist  .  core  .  JPListException  ; 
import   net  .  sf  .  jplist  .  input  .  ascii  .  AsciiPListParser  ; 
import   net  .  sf  .  jplist  .  input  .  ascii  .  ParseException  ; 








public   class   AsciiBuilder  { 




public   AsciiBuilder  (  )  { 
super  (  )  ; 
} 






public   Document   build  (  File   file  )  throws   JPListException  { 
FileReader   reader  =  null  ; 
try  { 
reader  =  new   FileReader  (  file  )  ; 
return   AsciiPListParser  .  parse  (  reader  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
throw   new   JPListException  (  "File not found:"  ,  e  )  ; 
}  catch  (  ParseException   e  )  { 
throw   new   JPListException  (  "Unrecognized format"  ,  e  )  ; 
}  finally  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 







public   Document   build  (  InputStream   istream  )  throws   JPListException  { 
BufferedReader   reader  =  null  ; 
try  { 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  istream  )  )  ; 
return   AsciiPListParser  .  parse  (  reader  )  ; 
}  catch  (  ParseException   e  )  { 
throw   new   JPListException  (  "Unrecognized format"  ,  e  )  ; 
}  finally  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 







public   Document   build  (  Reader   reader  )  throws   JPListException  { 
try  { 
return   AsciiPListParser  .  parse  (  reader  )  ; 
}  catch  (  ParseException   e  )  { 
throw   new   JPListException  (  "Unrecognized format"  ,  e  )  ; 
}  finally  { 
if  (  reader  !=  null  )  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 







public   Document   build  (  URL   url  )  throws   JPListException  { 
URLConnection   urlconn  =  null  ; 
InputStream   istream  =  null  ; 
try  { 
urlconn  =  url  .  openConnection  (  )  ; 
istream  =  urlconn  .  getInputStream  (  )  ; 
return   build  (  istream  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   JPListException  (  "IO Exception"  ,  e  )  ; 
}  finally  { 
if  (  istream  !=  null  )  { 
try  { 
istream  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
} 







public   Document   build  (  String   input  )  throws   JPListException  { 
StringReader   reader  =  null  ; 
reader  =  new   StringReader  (  input  )  ; 
return   build  (  reader  )  ; 
} 
} 

