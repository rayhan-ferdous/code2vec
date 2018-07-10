package   de  .  str  .  prettysource  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  io  .  StringWriter  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Stack  ; 













public   class   SourceFormat  { 

protected   int   lineCount  =  0  ; 

protected   String   sourceLine  =  null  ; 

protected   String   remainingLine  =  null  ; 

protected   StringBuilder   targetLine  =  null  ; 

protected   long   startTime  =  0  ; 

protected   Stack  <  SourceSelectionProcessor  >  selectionStack  =  null  ; 

private   SourceSelection   rootSelection  =  null  ; 

public   SourceFormat  (  SourceSelection   selections  )  { 
this  .  rootSelection  =  selections  ; 
SourceSelectionProcessor   root  =  createProcessors  (  this  .  rootSelection  )  ; 
this  .  selectionStack  =  new   Stack  <  SourceSelectionProcessor  >  (  )  ; 
this  .  selectionStack  .  push  (  root  )  ; 
} 

public   static   SourceSelection   createRootSelection  (  )  { 
return   new   SourceSelection  (  null  ,  null  )  ; 
} 







public   SourceSelection   getSourceSelection  (  )  { 
return   this  .  rootSelection  ; 
} 











private   SourceSelectionProcessor   createProcessors  (  SourceSelection   sourceSelection  )  { 
SourceSelectionProcessor   result  =  null  ; 
if  (  sourceSelection  .  getAllChilds  (  )  .  isEmpty  (  )  )  { 
result  =  new   SourceSelectionProcessor  (  sourceSelection  )  ; 
}  else  { 
List  <  SourceSelectionProcessor  >  subElements  =  new   ArrayList  <  SourceSelectionProcessor  >  (  )  ; 
for  (  SourceSelection   subnode  :  sourceSelection  .  getAllChilds  (  )  )  { 
SourceSelectionProcessor   se  =  createProcessors  (  subnode  )  ; 
subElements  .  add  (  se  )  ; 
} 
result  =  new   ParentSourceSelectionProcessor  (  sourceSelection  ,  subElements  )  ; 
} 
return   result  ; 
} 









public   void   format  (  File   source  ,  File   target  )  { 
if  (  !  source  .  exists  (  )  )  { 
throw   new   IllegalArgumentException  (  "Source '"  +  source  +  " doesn't exist"  )  ; 
} 
if  (  !  source  .  isFile  (  )  )  { 
throw   new   IllegalArgumentException  (  "Source '"  +  source  +  " is not a file"  )  ; 
} 
target  .  mkdirs  (  )  ; 
String   fileExtension  =  source  .  getName  (  )  .  substring  (  source  .  getName  (  )  .  lastIndexOf  (  "."  )  +  1  )  ; 
String   _target  =  source  .  getName  (  )  .  replace  (  fileExtension  ,  "html"  )  ; 
target  =  new   File  (  target  .  getPath  (  )  +  "/"  +  _target  )  ; 
try  { 
Reader   reader  =  new   FileReader  (  source  )  ; 
Writer   writer  =  new   FileWriter  (  target  )  ; 
this  .  format  (  reader  ,  writer  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 









public   void   format  (  Reader   source  ,  Writer   target  )  { 
this  .  startTime  =  System  .  currentTimeMillis  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  source  )  ; 
PrintWriter   printer  =  new   PrintWriter  (  new   BufferedWriter  (  target  )  )  ; 
this  .  lineCount  =  0  ; 
this  .  beforeFormatingSource  (  printer  )  ; 
try  { 
while  (  reader  .  ready  (  )  )  { 
String   line  =  reader  .  readLine  (  )  ; 
if  (  line  ==  null  )  { 
break  ; 
} 
this  .  sourceLine  =  line  ; 
this  .  remainingLine  =  line  ; 
this  .  targetLine  =  new   StringBuilder  (  )  ; 
this  .  lineCount  ++  ; 
this  .  beforeFormatingLine  (  printer  )  ; 
while  (  this  .  remainingLine  !=  null  &&  this  .  remainingLine  .  length  (  )  >  0  )  { 
SourceSelectionProcessor   foMode  =  this  .  selectionStack  .  peek  (  )  ; 
foMode  .  process  (  this  ,  null  )  ; 
} 
printer  .  println  (  this  .  targetLine  .  toString  (  )  )  ; 
} 
this  .  afterFormatingSource  (  printer  )  ; 
printer  .  flush  (  )  ; 
printer  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 








public   String   format  (  String   input  )  { 
Reader   source  =  new   StringReader  (  input  )  ; 
Writer   target  =  new   StringWriter  (  )  ; 
this  .  format  (  source  ,  target  )  ; 
return   target  .  toString  (  )  ; 
} 







protected   void   beforeFormatingSource  (  PrintWriter   printer  )  { 
} 







protected   void   afterFormatingSource  (  PrintWriter   printer  )  { 
} 







protected   void   beforeFormatingLine  (  PrintWriter   printer  )  { 
} 
} 

