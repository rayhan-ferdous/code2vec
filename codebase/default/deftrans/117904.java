import   com  .  sun  .  tools  .  doclets  .  *  ; 
import   com  .  sun  .  javadoc  .  *  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 











public   class   Extern  { 




private   static   Map   packageMap  ; 




final   String   packageName  ; 





final   String   path  ; 




final   boolean   relative  ; 









Extern  (  String   packageName  ,  String   path  ,  boolean   relative  )  { 
this  .  packageName  =  packageName  ; 
this  .  path  =  path  ; 
this  .  relative  =  relative  ; 
if  (  packageMap  ==  null  )  { 
packageMap  =  new   HashMap  (  )  ; 
} 
packageMap  .  put  (  packageName  ,  this  )  ; 
} 






public   static   Extern   findPackage  (  String   pkgName  )  { 
if  (  packageMap  ==  null  )  { 
return   null  ; 
} 
return  (  Extern  )  packageMap  .  get  (  pkgName  )  ; 
} 









public   static   boolean   url  (  String   url  ,  String   pkglisturl  ,  DocErrorReporter   reporter  )  { 
if  (  packageMap  !=  null  )  { 
reporter  .  printError  (  getText  (  "doclet.link_option_twice"  )  )  ; 
return   false  ; 
} 
String   errMsg  =  composeExternPackageList  (  url  ,  pkglisturl  )  ; 
if  (  errMsg  !=  null  )  { 
reporter  .  printError  (  errMsg  )  ; 
return   false  ; 
}  else  { 
return   true  ; 
} 
} 










static   String   composeExternPackageList  (  String   url  ,  String   pkglisturl  )  { 
url  =  adjustEndFileSeparator  (  url  )  ; 
pkglisturl  =  adjustEndFileSeparator  (  pkglisturl  )  ; 
if  (  pkglisturl  .  startsWith  (  "http://"  )  ||  pkglisturl  .  startsWith  (  "file:"  )  )  { 
return   fetchURLComposeExternPackageList  (  url  ,  pkglisturl  )  ; 
}  else  { 
return   readFileComposeExternPackageList  (  url  ,  pkglisturl  )  ; 
} 
} 




static   String   adjustEndFileSeparator  (  String   url  )  { 
String   filesep  =  isRelativePath  (  url  )  ?  File  .  separator  :  "/"  ; 
if  (  !  url  .  endsWith  (  filesep  )  )  { 
url  +=  filesep  ; 
} 
return   url  ; 
} 





static   boolean   isRelativePath  (  String   url  )  { 
return  !  (  url  .  startsWith  (  "http://"  )  ||  url  .  startsWith  (  "file:"  )  )  ; 
} 







private   static   String   getText  (  String   prop  ,  String   link  )  { 
return   Standard  .  configuration  (  )  .  standardmessage  .  getText  (  prop  ,  link  )  ; 
} 






private   static   String   getText  (  String   msg  )  { 
return   Standard  .  configuration  (  )  .  standardmessage  .  getText  (  msg  )  ; 
} 







static   String   fetchURLComposeExternPackageList  (  String   urlpath  ,  String   pkglisturlpath  )  { 
String   link  =  pkglisturlpath  +  "package-list"  ; 
try  { 
boolean   relative  =  isRelativePath  (  urlpath  )  ; 
readPackageList  (  (  new   URL  (  link  )  )  .  openStream  (  )  ,  urlpath  ,  relative  )  ; 
}  catch  (  MalformedURLException   exc  )  { 
return   getText  (  "doclet.MalformedURL"  ,  link  )  ; 
}  catch  (  IOException   exc  )  { 
return   getText  (  "doclet.URL_error"  ,  link  )  ; 
} 
return   null  ; 
} 







static   String   readFileComposeExternPackageList  (  String   urlpath  ,  String   relpath  )  { 
String   link  =  relpath  +  "package-list"  ; 
try  { 
File   file  =  new   File  (  link  )  ; 
if  (  file  .  exists  (  )  &&  file  .  canRead  (  )  )  { 
boolean   relative  =  isRelativePath  (  urlpath  )  ; 
readPackageList  (  new   FileInputStream  (  file  )  ,  urlpath  ,  relative  )  ; 
}  else  { 
return   getText  (  "doclet.File_error"  ,  link  )  ; 
} 
}  catch  (  FileNotFoundException   exc  )  { 
return   getText  (  "doclet.File_error"  ,  link  )  ; 
}  catch  (  IOException   exc  )  { 
return   getText  (  "doclet.File_error"  ,  link  )  ; 
} 
return   null  ; 
} 









static   void   readPackageList  (  InputStream   input  ,  String   path  ,  boolean   relative  )  throws   IOException  { 
InputStreamReader   in  =  new   InputStreamReader  (  input  )  ; 
StringBuffer   strbuf  =  new   StringBuffer  (  )  ; 
try  { 
int   c  ; 
while  (  (  c  =  in  .  read  (  )  )  >=  0  )  { 
char   ch  =  (  char  )  c  ; 
if  (  ch  ==  '\n'  ||  ch  ==  '\r'  )  { 
if  (  strbuf  .  length  (  )  >  0  )  { 
String   packname  =  strbuf  .  toString  (  )  ; 
String   packpath  =  path  +  packname  .  replace  (  '.'  ,  '/'  )  +  '/'  ; 
new   Extern  (  packname  ,  packpath  ,  relative  )  ; 
strbuf  .  setLength  (  0  )  ; 
} 
}  else  { 
strbuf  .  append  (  ch  )  ; 
} 
} 
}  finally  { 
input  .  close  (  )  ; 
} 
} 




public   String   toString  (  )  { 
return   packageName  +  (  relative  ?  " -> "  :  " => "  )  +  path  ; 
} 
} 

