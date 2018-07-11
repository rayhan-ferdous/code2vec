package   mipt  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   mipt  .  common  .  Const  ; 




















public   class   BundleConverter   extends   AbstractUtil   implements   Const  { 

static  { 
clazz  =  BundleConverter  .  class  ; 
} 

protected   String   dir  ; 

protected   String   project  =  null  ; 

protected   String   file  =  "Bundle.*"  ; 

protected   String   superClass  =  null  ; 

protected   String   dest  =  null  ; 

protected   String   encoding  =  "UTF-16"  ; 




public   boolean   processOption  (  String   option  ,  String   value  )  { 
if  (  super  .  processOption  (  option  ,  value  )  )  { 
if  (  option  .  equals  (  "file"  )  )  { 
file  =  file  .  replace  (  "*"  ,  ".*"  )  ; 
} 
return   true  ; 
} 
if  (  option  .  equals  (  "super"  )  )  { 
superClass  =  value  ; 
}  else  { 
return   false  ; 
} 
return   true  ; 
} 




public   void   processArgument  (  String   arg  ,  int   index  )  { 
if  (  index  ==  0  )  dir  =  arg  ; 
} 




public   String  [  ]  getSupportedOptions  (  )  { 
return   new   String  [  ]  {  "?"  ,  "project"  ,  "file"  ,  "super"  ,  "dest"  ,  "encoding"  }  ; 
} 




protected   String   getUsageHelp  (  )  { 
return  "java "  +  getClass  (  )  .  getName  (  )  +  " dir [-option <arg>]\r\n"  +  "where dir is relative or absolute path to bundle(s)"  ; 
} 




public   void   showArgumentsHelp  (  PrintStream   out  )  { 
out  .  println  (  "**** Converts ResourceBundles: from *.java to *.properties ****"  )  ; 
out  .  println  (  " Usage: "  +  getUsageHelp  (  )  )  ; 
out  .  println  (  " Options:"  )  ; 
out  .  println  (  "  '-?': shows this help on command line arguments;"  )  ; 
out  .  println  (  "  '-project': uses '../project/src/dir' as dir"  )  ; 
out  .  println  (  "    (if it isn't set and if dir is relative, './src/dir' is used);"  )  ; 
out  .  println  (  "  '-file': sets file name (without extension!) or its pattern (with * as wildcard)"  )  ; 
out  .  println  (  "    (by default 'Bundle*' is used);"  )  ; 
out  .  println  (  "  '-super': all non-abstract subclasses of this class in project.dir are processed"  )  ; 
out  .  println  (  "     (if project is not set, ./src/dir is used as root to scan)"  )  ; 
out  .  println  (  "     (note that 'mipt' is assumed to be the root package to form class name from file path)"  )  ; 
out  .  println  (  "     (note that CLASSPATH should be set properly for this option to work);"  )  ; 
out  .  println  (  "  '-dest': sets destination directory path (absolute path or relative to project dir or to . dir)"  )  ; 
out  .  println  (  "    (if it isn't set the source dir is used; if it's set but doesn't exist in FS - created);"  )  ; 
out  .  println  (  "  '-encoding': sets encoding of destination file ('UTF-16' by default)."  )  ; 
out  .  println  (  " Example args: mipt\\crec\\lab\\compmath -project ALES\\NumLabs -super mipt.common.Bundle -dest labs\\strings"  )  ; 
} 





public   void   run  (  )  { 
java2properties  (  )  ; 
} 




public   void   java2properties  (  )  { 
convert  (  "new Object[][]"  ,  new   JavaReader  (  )  ,  new   PropertiesWriter  (  )  )  ; 
} 




public   void   convert  (  String   startWith  ,  BundleReader   reader  ,  BundleWriter   writer  )  { 
if  (  dir  ==  null  )  { 
showArgumentsHelp  (  System  .  err  )  ; 
return  ; 
} 
if  (  project  !=  null  )  { 
project  =  new   File  (  userDir  )  .  getParent  (  )  +  fileSep  +  project  ; 
dir  =  project  +  fileSep  +  "src"  +  fileSep  +  dir  ; 
} 
if  (  !  new   File  (  dir  )  .  isAbsolute  (  )  )  dir  =  userDir  +  fileSep  +  "src"  +  fileSep  +  dir  ; 
if  (  !  new   File  (  dir  )  .  exists  (  )  )  throw   new   IllegalStateException  (  dir  +  " does not exist"  )  ; 
if  (  dest  !=  null  &&  !  new   File  (  dest  )  .  isAbsolute  (  )  )  { 
dest  =  project  ==  null  ?  userDir  +  fileSep  +  dest  :  project  +  fileSep  +  dest  ; 
File   d  =  new   File  (  dest  )  ; 
if  (  !  d  .  exists  (  )  )  d  .  mkdir  (  )  ; 
} 
Iterator   files  =  getFiles  (  dir  )  .  iterator  (  )  ; 
int   i  =  0  ,  j  =  0  ; 
while  (  files  .  hasNext  (  )  )  { 
try  { 
file2file  (  files  .  next  (  )  ,  startWith  ,  reader  ,  writer  )  ; 
j  ++  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  System  .  err  )  ; 
} 
i  ++  ; 
} 
System  .  out  .  println  (  i  +  " classes found, "  +  j  +  " of them converted to properties"  )  ; 
} 




protected   LinkedList   getFiles  (  String   dir  )  { 
LinkedList   files  =  new   LinkedList  (  )  ; 
if  (  superClass  ==  null  )  { 
if  (  !  file  .  contains  (  "*"  )  )  addFile  (  files  ,  dir  ,  file  )  ;  else   addFiles  (  files  ,  dir  ,  false  )  ; 
}  else  { 
try  { 
scanDir  (  files  ,  dir  ,  getRootDir  (  dir  )  ,  Class  .  forName  (  superClass  )  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  System  .  err  )  ; 
} 
} 
return   files  ; 
} 






protected   final   String   getRootDir  (  String   dir  )  { 
String   last  ; 
do  { 
if  (  dir  .  endsWith  (  fileSep  )  )  dir  =  dir  .  substring  (  0  ,  dir  .  length  (  )  -  1  )  ; 
int   j  =  dir  .  lastIndexOf  (  fileSep  )  +  1  ; 
if  (  j  ==  0  )  break  ; 
last  =  dir  .  substring  (  j  )  ; 
dir  =  dir  .  substring  (  0  ,  j  )  ; 
}  while  (  !  isRootDir  (  dir  ,  last  )  )  ; 
return   dir  ; 
} 




protected   boolean   isRootDir  (  String   path  ,  String   removedDir  )  { 
return   removedDir  .  equals  (  "mipt"  )  ; 
} 








protected   void   scanDir  (  LinkedList   files  ,  String   dir  ,  String   rootDir  ,  Class   supClass  )  throws   ClassNotFoundException  { 
LinkedList   current  =  new   LinkedList  (  )  ; 
addFiles  (  current  ,  dir  ,  true  )  ; 
String   className  =  dir  .  substring  (  rootDir  .  length  (  )  )  ; 
className  =  className  .  replace  (  fileSep  .  charAt  (  0  )  ,  '.'  )  ; 
if  (  !  className  .  endsWith  (  "."  )  )  className  +=  "."  ; 
Iterator   iter  =  current  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
File   file  =  (  File  )  iter  .  next  (  )  ; 
if  (  file  .  isDirectory  (  )  )  scanDir  (  files  ,  file  .  getPath  (  )  ,  rootDir  ,  supClass  )  ;  else  { 
String   name  =  file  .  getName  (  )  ; 
String   shortName  =  name  .  substring  (  0  ,  name  .  length  (  )  -  ".java"  .  length  (  )  )  ; 
Class   cls  =  Class  .  forName  (  className  +  shortName  )  ; 
if  (  supClass  .  isAssignableFrom  (  cls  )  &&  !  Modifier  .  isAbstract  (  cls  .  getModifiers  (  )  )  )  { 
if  (  hasContents  (  cls  )  )  files  .  add  (  file  )  ;  else  { 
int   j  =  shortName  .  indexOf  (  '_'  )  ; 
if  (  j  <  0  )  continue  ; 
shortName  =  shortName  .  substring  (  0  ,  j  )  ; 
File   sourceFile  =  new   File  (  file  .  getParent  (  )  ,  shortName  +  ".java"  )  ; 
files  .  add  (  new   File  [  ]  {  sourceFile  ,  file  }  )  ; 
} 
} 
} 
} 
} 






protected   boolean   hasContents  (  Class   cls  )  { 
try  { 
cls  .  getDeclaredMethod  (  "initContents"  ,  new   Class  [  0  ]  )  ; 
return   true  ; 
}  catch  (  SecurityException   e  )  { 
return   true  ; 
}  catch  (  NoSuchMethodException   e  )  { 
return   false  ; 
} 
} 






protected   void   addFiles  (  LinkedList   files  ,  String   dir  ,  final   boolean   acceptDir  )  { 
File   dirFile  =  new   File  (  dir  )  ; 
File  [  ]  f  =  dirFile  .  listFiles  (  new   FileFilter  (  )  { 

public   boolean   accept  (  File   f  )  { 
if  (  f  .  isDirectory  (  )  )  return   acceptDir  &&  !  f  .  isHidden  (  )  ; 
String   name  =  f  .  getName  (  )  ; 
if  (  !  name  .  endsWith  (  ".java"  )  )  return   false  ; 
Pattern   p  =  Pattern  .  compile  (  file  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
return   p  .  matcher  (  name  )  .  matches  (  )  ; 
} 
}  )  ; 
for  (  int   i  =  0  ;  i  <  f  .  length  ;  i  ++  )  files  .  add  (  f  [  i  ]  )  ; 
} 






protected   final   void   addFile  (  LinkedList   files  ,  String   dir  ,  String   file  )  { 
files  .  add  (  new   File  (  dir  ,  file  +  ".java"  )  )  ; 
} 





protected   String   getPropertiesPath  (  File   source  )  { 
String   destination  =  dest  ==  null  ?  source  .  getParent  (  )  :  dest  ; 
return   destination  +  fileSep  +  getPropertiesFileName  (  source  )  +  ".properties"  ; 
} 







protected   String   getPropertiesFileName  (  File   source  )  { 
File   pack  =  source  .  getParentFile  (  )  ; 
if  (  pack  .  getName  (  )  .  equals  (  "gui"  )  )  pack  =  pack  .  getParentFile  (  )  ; 
String   p  =  pack  .  getName  (  )  ,  name  =  source  .  getName  (  )  ; 
int   j  =  name  .  lastIndexOf  (  '_'  )  ; 
if  (  j  >=  0  )  p  +=  name  .  substring  (  j  ,  j  +  3  )  ; 
return   p  ; 
} 





public   void   file2file  (  Object   file  ,  String   startWith  ,  BundleReader   reader  ,  BundleWriter   writer  )  throws   IOException  { 
File   source  ,  dest  ; 
if  (  file   instanceof   File  )  source  =  dest  =  (  File  )  file  ;  else  { 
File  [  ]  arr  =  (  File  [  ]  )  file  ; 
source  =  arr  [  0  ]  ; 
dest  =  arr  [  1  ]  ; 
} 
BufferedReader   br  =  new   BufferedReader  (  new   FileReader  (  source  )  )  ; 
try  { 
writer  .  createWriter  (  getPropertiesPath  (  dest  )  ,  encoding  )  ; 
boolean   header  =  true  ; 
Entry   entry  =  new   Entry  (  )  ; 
while  (  br  .  ready  (  )  )  { 
String   line  =  br  .  readLine  (  )  ; 
if  (  line  ==  null  )  break  ; 
if  (  header  )  { 
header  =  reader  .  isHeader  (  line  ,  startWith  )  ; 
continue  ; 
} 
if  (  header  )  continue  ; 
if  (  !  reader  .  parseEntry  (  br  ,  line  ,  entry  )  )  break  ; 
writer  .  write  (  entry  )  ; 
} 
}  finally  { 
br  .  close  (  )  ; 
writer  .  close  (  )  ; 
} 
} 





public   interface   BundleReader  { 





boolean   isHeader  (  String   line  ,  String   startWith  )  ; 








boolean   parseEntry  (  BufferedReader   br  ,  String   line  ,  Entry   entry  )  throws   IOException  ; 
} 





public   interface   BundleWriter  { 




void   createWriter  (  String   path  ,  String   encoding  )  throws   IOException  ; 




void   write  (  Entry   entry  )  throws   IOException  ; 




void   close  (  )  throws   IOException  ; 
} 








public   static   class   JavaReader   implements   BundleReader  { 




public   boolean   isHeader  (  String   line  ,  String   startWith  )  { 
return   line  .  lastIndexOf  (  startWith  )  <  0  ; 
} 









public   boolean   parseEntry  (  BufferedReader   br  ,  String   line  ,  Entry   entry  )  throws   IOException  { 
int   jK  =  line  .  indexOf  (  "{\""  )  ,  jC  =  line  .  lastIndexOf  (  "//"  )  ,  jEnd  =  line  .  lastIndexOf  (  "};"  )  ; 
while  (  jK  <  0  &&  jC  <  0  &&  jEnd  <  0  &&  br  .  ready  (  )  )  { 
line  =  br  .  readLine  (  )  ; 
jK  =  line  .  indexOf  (  "{\""  )  ; 
jC  =  line  .  lastIndexOf  (  "//"  )  ; 
jEnd  =  line  .  lastIndexOf  (  "};"  )  ; 
} 
if  (  jK  <  0  &&  jC  <  0  )  return   false  ; 
if  (  jC  >=  0  &&  jK  >=  0  )  jC  =  checkComment  (  jC  ,  line  )  ; 
if  (  jC  <  0  )  { 
entry  .  comment  =  null  ; 
}  else  { 
entry  .  comment  =  trim  (  line  .  substring  (  jC  +  2  )  )  ; 
line  =  line  .  substring  (  0  ,  jC  )  ; 
jK  =  line  .  indexOf  (  "{\""  )  ; 
} 
if  (  jK  <  0  )  entry  .  key  =  null  ;  else  { 
jK  +=  2  ; 
int   j  =  line  .  indexOf  (  '"'  ,  jK  )  ; 
if  (  j  <  0  )  throw   new   IOException  (  "Wrong source format: unclosed \" in key"  )  ; 
entry  .  key  =  line  .  substring  (  jK  ,  j  )  ; 
parseValue  (  br  ,  line  .  substring  (  j  +  1  )  ,  entry  )  ; 
} 
return   true  ; 
} 

private   int   checkComment  (  int   jC  ,  String   line  )  { 
int   jV  =  line  .  lastIndexOf  (  '"'  )  ; 
if  (  jC  <  jV  &&  jC  >  line  .  lastIndexOf  (  '"'  ,  jV  -  1  )  )  return  -  1  ; 
int   jC2  =  line  .  lastIndexOf  (  "//"  ,  jC  -  1  )  ; 
if  (  jC2  >=  0  )  jC2  =  checkComment  (  jC2  ,  line  )  ; 
if  (  jC2  >=  0  )  return   jC2  ; 
return   jC  ; 
} 









protected   void   parseValue  (  BufferedReader   br  ,  String   line  ,  Entry   entry  )  throws   IOException  { 
String   element  [  ]  =  new   String  [  1  ]  ; 
int   j  =  line  .  lastIndexOf  (  "String[]"  )  ; 
if  (  j  >=  0  )  { 
LinkedList   list  =  new   LinkedList  (  )  ; 
while  (  true  )  { 
boolean   end  =  !  parseValue  (  element  ,  br  ,  line  ,  entry  ,  true  )  ; 
if  (  element  [  0  ]  ==  null  )  break  ; 
list  .  add  (  element  [  0  ]  )  ; 
if  (  end  )  break  ; 
line  =  br  .  readLine  (  )  ; 
} 
entry  .  array  =  new   String  [  list  .  size  (  )  ]  ; 
list  .  toArray  (  entry  .  array  )  ; 
entry  .  value  =  null  ; 
}  else  { 
parseValue  (  element  ,  br  ,  line  ,  entry  ,  false  )  ; 
if  (  element  [  0  ]  ==  null  )  throw   new   IOException  (  "Wrong source format: "  +  line  +  " is neither String (constant) nor String[] (array of constants)"  )  ; 
entry  .  value  =  element  [  0  ]  ; 
entry  .  array  =  null  ; 
} 
} 








protected   boolean   parseValue  (  String  [  ]  result  ,  BufferedReader   br  ,  String   line  ,  Entry   entry  ,  boolean   inLoop  )  throws   IOException  { 
int   jC  =  line  .  indexOf  (  "//"  )  ; 
if  (  jC  >=  0  )  jC  =  checkComment  (  jC  ,  line  )  ; 
if  (  jC  >=  0  )  { 
String   comment  =  line  .  substring  (  jC  +  2  )  ; 
comment  =  trim  (  comment  )  ; 
if  (  entry  .  comment  ==  null  )  entry  .  comment  =  comment  ;  else  { 
if  (  comment  .  length  (  )  >  1  )  comment  =  Character  .  toUpperCase  (  comment  .  charAt  (  0  )  )  +  comment  .  substring  (  1  )  ; 
entry  .  comment  +=  ". "  +  comment  ; 
} 
line  =  line  .  substring  (  0  ,  jC  )  ; 
} 
line  =  trim  (  line  ,  false  )  ; 
int   j1  =  line  .  indexOf  (  '"'  )  ,  jEndValue  =  line  .  lastIndexOf  (  '}'  )  ; 
if  (  j1  <  0  )  { 
if  (  jEndValue  <  0  )  return   parseValue  (  result  ,  br  ,  br  .  readLine  (  )  ,  entry  ,  inLoop  )  ; 
result  [  0  ]  =  null  ; 
return   false  ; 
} 
int   j2  =  line  .  lastIndexOf  (  '"'  )  ; 
if  (  j2  ==  j1  )  throw   new   IOException  (  "Wrong source format: unclosed \" in value"  )  ; 
boolean   continued  =  line  .  charAt  (  line  .  length  (  )  -  1  )  ==  '+'  ; 
result  [  0  ]  =  correctValue  (  line  .  substring  (  j1  +  1  ,  j2  )  )  ; 
if  (  continued  )  { 
String  [  ]  end  =  new   String  [  1  ]  ; 
boolean   res  =  parseValue  (  end  ,  br  ,  br  .  readLine  (  )  ,  entry  ,  inLoop  )  ; 
if  (  end  [  0  ]  !=  null  )  result  [  0  ]  +=  end  [  0  ]  ; 
return   res  ; 
} 
if  (  jEndValue  >=  0  &&  jEndValue  >  j2  )  return   false  ; 
if  (  inLoop  )  return   true  ; 
return   parseValue  (  result  ,  br  ,  br  .  readLine  (  )  ,  entry  ,  false  )  ; 
} 





protected   String   correctValue  (  String   value  )  { 
int   j  =  value  .  indexOf  (  "\\u"  )  ; 
while  (  j  >=  0  )  { 
int   ch  =  Integer  .  parseInt  (  value  .  substring  (  j  +  2  ,  j  +  6  )  ,  16  )  ; 
value  =  value  .  substring  (  0  ,  j  )  +  (  char  )  ch  +  value  .  substring  (  j  +  6  )  ; 
j  =  value  .  indexOf  (  "\\u"  ,  j  +  1  )  ; 
} 
value  =  value  .  replace  (  "\r"  ,  ""  )  ; 
return   value  .  replace  (  "\n"  ,  "\\n"  )  ; 
} 




public   final   String   trim  (  String   s  )  { 
s  =  trim  (  s  ,  true  )  ; 
return   trim  (  s  ,  false  )  ; 
} 





public   String   trim  (  String   s  ,  boolean   leading  )  { 
int   n  =  leading  ?  s  .  length  (  )  -  1  :  0  ,  m  =  s  .  length  (  )  -  1  -  n  ,  j  ,  i  =  leading  ?  1  :  -  1  ; 
for  (  j  =  m  ;  j  !=  n  ;  j  +=  i  )  { 
char   c  =  s  .  charAt  (  j  )  ; 
if  (  c  !=  ' '  &&  c  !=  '\t'  &&  c  !=  '/'  )  break  ; 
} 
if  (  j  ==  m  )  return   s  ; 
return   leading  ?  s  .  substring  (  j  )  :  s  .  substring  (  0  ,  j  +  1  )  ; 
} 
} 





public   static   class   PropertiesWriter   implements   BundleWriter  { 

protected   BufferedWriter   bw  ; 




public   void   createWriter  (  String   path  ,  String   encoding  )  throws   IOException  { 
bw  =  new   BufferedWriter  (  new   OutputStreamWriter  (  new   FileOutputStream  (  path  )  ,  encoding  )  )  ; 
} 




public   void   write  (  Entry   entry  )  throws   IOException  { 
bw  .  write  (  entry2property  (  entry  )  )  ; 
bw  .  write  (  lineSep  )  ; 
bw  .  flush  (  )  ; 
} 




public   void   close  (  )  throws   IOException  { 
if  (  bw  !=  null  )  bw  .  close  (  )  ; 
} 




protected   String   entry2property  (  Entry   entry  )  { 
String   line  =  ""  ; 
if  (  entry  .  key  !=  null  )  { 
if  (  entry  .  value  !=  null  )  line  =  entry  .  value  ;  else   for  (  int   i  =  0  ;  i  <  entry  .  array  .  length  ;  i  ++  )  { 
line  =  line  +  "[["  +  entry  .  array  [  i  ]  ; 
} 
line  =  entry  .  key  +  "="  +  line  ; 
} 
if  (  entry  .  comment  ==  null  )  return   line  ;  else   if  (  line  ==  ""  )  return  "# "  +  entry  .  comment  ;  else   return  "# "  +  entry  .  comment  +  lineSep  +  line  ; 
} 
} 





public   static   class   Entry  { 

public   String   key  ,  value  ,  array  [  ]  ,  comment  ; 
} 
} 

