package   freestyleLearningGroup  .  independent  .  util  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  tree  .  *  ; 






public   class   FLGFileUtility  { 

public   static   File   createNewFile  (  String   fileNamePrefix  ,  String   fileNameSuffix  ,  File   directory  )  { 
long   fileNameNumber  =  1  ; 
while  (  new   File  (  directory  ,  fileNamePrefix  +  fileNameNumber  +  fileNameSuffix  )  .  exists  (  )  )  fileNameNumber  ++  ; 
File   newFile  =  new   File  (  directory  ,  fileNamePrefix  +  fileNameNumber  +  fileNameSuffix  )  ; 
if  (  !  directory  .  exists  (  )  )  directory  .  mkdirs  (  )  ; 
return   newFile  ; 
} 

public   static   File   createNamedDirectory  (  String   directoryPrefix  ,  String   directorySuffix  ,  File   directory  )  { 
if  (  new   File  (  directory  ,  directoryPrefix  +  directorySuffix  )  .  exists  (  )  )  { 
return   createNewDirectory  (  directoryPrefix  ,  directorySuffix  ,  directory  )  ; 
} 
File   newFile  =  new   File  (  directory  ,  directoryPrefix  +  directorySuffix  )  ; 
if  (  !  directory  .  exists  (  )  )  directory  .  mkdirs  (  )  ; 
if  (  newFile  .  mkdirs  (  )  )  return   newFile  ; 
return   null  ; 
} 

public   static   File   createNewDirectory  (  String   directoryPrefix  ,  String   directorySuffix  ,  File   directory  )  { 
long   directoryNumber  =  1  ; 
while  (  new   File  (  directory  ,  directoryPrefix  +  directoryNumber  +  directorySuffix  )  .  exists  (  )  )  directoryNumber  ++  ; 
File   newFile  =  new   File  (  directory  ,  directoryPrefix  +  directoryNumber  +  directorySuffix  )  ; 
if  (  !  directory  .  exists  (  )  )  directory  .  mkdirs  (  )  ; 
if  (  newFile  .  mkdirs  (  )  )  return   newFile  ; 
return   null  ; 
} 


public   static   void   copyDirectoryContent  (  File   sourceDirectory  ,  File   destinationDirectory  )  { 
Vector   directoryContent  =  readDirectoryContent  (  sourceDirectory  )  ; 
for  (  int   i  =  0  ;  i  <  directoryContent  .  size  (  )  ;  i  ++  )  { 
String   sourcePath  =  directoryContent  .  elementAt  (  i  )  .  toString  (  )  ; 
int   sourceDirectoryPathLength  =  sourceDirectory  .  getPath  (  )  .  length  (  )  ; 
StringBuffer   relativePathBuffer  =  new   StringBuffer  (  )  ; 
for  (  int   j  =  sourceDirectoryPathLength  ;  j  <  sourcePath  .  length  (  )  ;  j  ++  )  { 
relativePathBuffer  .  append  (  sourcePath  .  charAt  (  j  )  )  ; 
} 
String   destinationPath  =  destinationDirectory  .  getPath  (  )  +  new   String  (  relativePathBuffer  )  ; 
File   sourceFile  =  new   File  (  sourcePath  )  ; 
File   destinationFile  =  new   File  (  destinationPath  )  ; 
if  (  !  sourceFile  .  isDirectory  (  )  )  copyFile  (  sourceFile  ,  destinationFile  )  ;  else  { 
if  (  !  destinationFile  .  exists  (  )  )  { 
destinationFile  .  mkdirs  (  )  ; 
destinationFile  .  mkdir  (  )  ; 
} 
} 
} 
} 

public   static   String   createNewRelativeFileName  (  String   fileNamePrefix  ,  String   fileNameSuffix  ,  File   directory  )  { 
long   fileNameNumber  =  1  ; 
while  (  new   File  (  directory  ,  fileNamePrefix  +  fileNameNumber  +  fileNameSuffix  )  .  exists  (  )  )  fileNameNumber  ++  ; 
return   fileNamePrefix  +  fileNameNumber  +  fileNameSuffix  ; 
} 

public   static   void   copyFile  (  File   source  ,  File   target  )  { 
try  { 
target  .  getParentFile  (  )  .  mkdirs  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   len  =  0  ; 
FileInputStream   in  =  new   FileInputStream  (  source  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  target  )  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  !=  -  1  )  out  .  write  (  buffer  ,  0  ,  len  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
} 
} 

public   static   String   readFileInString  (  File   file  )  { 
StringBuffer   stringBuffer  =  new   StringBuffer  (  )  ; 
try  { 
FileReader   fileReader  =  new   FileReader  (  file  )  ; 
BufferedReader   bufferedReader  =  new   BufferedReader  (  fileReader  )  ; 
String   newLine  =  null  ; 
while  (  (  newLine  =  bufferedReader  .  readLine  (  )  )  !=  null  )  { 
stringBuffer  .  append  (  newLine  +  "\n"  )  ; 
} 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
} 
return   stringBuffer  .  toString  (  )  ; 
} 

public   static   void   writeStringIntoFile  (  String   text  ,  File   file  )  { 
try  { 
PrintWriter   out  =  new   PrintWriter  (  new   BufferedWriter  (  new   FileWriter  (  file  )  )  )  ; 
out  .  println  (  text  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
} 
} 

public   static   boolean   directoryContains  (  File   directory  ,  String   fileName  ,  boolean   ignoreCase  )  { 
String  [  ]  fileNames  =  directory  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  fileNames  .  length  ;  i  ++  )  { 
if  (  ignoreCase  )  { 
if  (  fileNames  [  i  ]  .  equalsIgnoreCase  (  fileName  )  )  return   true  ; 
}  else  { 
if  (  fileNames  [  i  ]  .  equals  (  fileName  )  )  return   true  ; 
} 
} 
return   false  ; 
} 

public   static   void   cleanDirectory  (  File   directory  ,  File  [  ]  fileList  )  { 
if  (  directory  ==  null  ||  directory  .  getAbsolutePath  (  )  .  length  (  )  <  5  )  return  ; 
Vector   directoryContent  =  readDirectoryContent  (  directory  )  ; 
if  (  fileList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  fileList  .  length  ;  i  ++  )  { 
File   file  =  fileList  [  i  ]  ; 
File   parent  =  file  .  getParentFile  (  )  ; 
directoryContent  .  remove  (  file  .  getPath  (  )  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
Vector   subDirectoryContent  =  readDirectoryContent  (  file  )  ; 
for  (  int   j  =  0  ;  j  <  subDirectoryContent  .  size  (  )  ;  j  ++  )  { 
String   subDirectoryFileName  =  subDirectoryContent  .  elementAt  (  j  )  .  toString  (  )  ; 
directoryContent  .  remove  (  subDirectoryFileName  )  ; 
} 
} 
while  (  !  parent  .  getPath  (  )  .  equals  (  directory  .  getPath  (  )  )  )  { 
directoryContent  .  remove  (  parent  .  getPath  (  )  )  ; 
parent  =  parent  .  getParentFile  (  )  ; 
} 
} 
} 
for  (  int   i  =  0  ;  i  <  directoryContent  .  size  (  )  ;  i  ++  )  { 
String   fileName  =  (  String  )  directoryContent  .  elementAt  (  i  )  ; 
File   file  =  new   File  (  fileName  )  ; 
file  .  delete  (  )  ; 
} 
} 

public   static   void   deleteDirectory  (  File   directory  )  { 
if  (  (  directory  !=  null  )  &&  (  directory  .  isDirectory  (  )  )  )  { 
cleanDirectory  (  directory  ,  null  )  ; 
directory  .  delete  (  )  ; 
} 
} 


public   static   Vector   getSubdirectories  (  File   directory  )  { 
Vector   contentVector  =  new   Vector  (  )  ; 
File  [  ]  content  =  directory  .  listFiles  (  )  ; 
if  (  content  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  i  ++  )  { 
if  (  content  [  i  ]  .  isDirectory  (  )  )  { 
contentVector  .  addAll  (  getSubdirectories  (  content  [  i  ]  )  )  ; 
contentVector  .  add  (  content  [  i  ]  .  getPath  (  )  )  ; 
} 
} 
} 
return   contentVector  ; 
} 






public   static   Vector   readDirectoryContent  (  File   directory  )  { 
Vector   contentVector  =  new   Vector  (  )  ; 
File  [  ]  content  =  directory  .  listFiles  (  )  ; 
if  (  content  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  i  ++  )  { 
if  (  content  [  i  ]  .  isDirectory  (  )  )  { 
contentVector  .  addAll  (  readDirectoryContent  (  content  [  i  ]  )  )  ; 
} 
contentVector  .  add  (  content  [  i  ]  .  getPath  (  )  )  ; 
} 
} 
return   contentVector  ; 
} 










public   static   File  [  ]  bubbleSortFiles  (  File  [  ]  unsortedFiles  ,  boolean   ascending  )  { 
if  (  unsortedFiles  .  length  <  2  )  return   unsortedFiles  ; 
if  (  ascending  )  { 
for  (  int   i  =  0  ;  i  <  unsortedFiles  .  length  -  1  ;  i  ++  )  { 
for  (  int   j  =  1  ;  j  <  unsortedFiles  .  length  -  1  -  i  ;  j  ++  )  if  (  (  unsortedFiles  [  j  +  1  ]  .  getName  (  )  )  .  compareToIgnoreCase  (  unsortedFiles  [  j  ]  .  getName  (  )  )  <  0  )  { 
File   swap  =  unsortedFiles  [  j  ]  ; 
unsortedFiles  [  j  ]  =  unsortedFiles  [  j  +  1  ]  ; 
unsortedFiles  [  j  +  1  ]  =  swap  ; 
} 
} 
}  else  { 
for  (  int   i  =  unsortedFiles  .  length  -  2  ;  i  >=  0  ;  i  --  )  { 
for  (  int   j  =  unsortedFiles  .  length  -  2  -  i  ;  j  >=  0  ;  j  --  )  if  (  (  unsortedFiles  [  j  +  1  ]  .  getName  (  )  )  .  compareToIgnoreCase  (  unsortedFiles  [  j  ]  .  getName  (  )  )  >  0  )  { 
File   swap  =  unsortedFiles  [  j  ]  ; 
unsortedFiles  [  j  ]  =  unsortedFiles  [  j  +  1  ]  ; 
unsortedFiles  [  j  +  1  ]  =  swap  ; 
} 
} 
} 
return   unsortedFiles  ; 
} 







public   static   DefaultTreeModel   loadDirectoryContent  (  File   directory  )  { 
DefaultTreeModel   directoryTreeModel  ; 
DefaultMutableTreeNode   root  =  new   DefaultMutableTreeNode  (  directory  )  ; 
directoryTreeModel  =  new   DefaultTreeModel  (  root  )  ; 
File  [  ]  content  =  directory  .  listFiles  (  )  ; 
content  =  FLGFileUtility  .  bubbleSortFiles  (  content  ,  true  )  ; 
if  (  content  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  i  ++  )  { 
if  (  content  [  i  ]  .  isDirectory  (  )  )  { 
loadDirectoryContent  (  content  [  i  ]  ,  root  ,  directoryTreeModel  )  ; 
}  else  { 
directoryTreeModel  .  insertNodeInto  (  new   DefaultMutableTreeNode  (  content  [  i  ]  )  ,  root  ,  root  .  getChildCount  (  )  )  ; 
} 
} 
} 
return   directoryTreeModel  ; 
} 

private   static   DefaultTreeModel   loadDirectoryContent  (  File   directory  ,  DefaultMutableTreeNode   parent  ,  DefaultTreeModel   model  )  { 
if  (  directory  !=  null  )  { 
DefaultMutableTreeNode   currentRoot  =  new   DefaultMutableTreeNode  (  directory  )  ; 
model  .  insertNodeInto  (  currentRoot  ,  parent  ,  parent  .  getChildCount  (  )  )  ; 
File  [  ]  content  =  directory  .  listFiles  (  )  ; 
if  (  content  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  content  .  length  ;  i  ++  )  { 
if  (  content  [  i  ]  .  isDirectory  (  )  )  { 
loadDirectoryContent  (  content  [  i  ]  ,  currentRoot  ,  model  )  ; 
}  else  { 
model  .  insertNodeInto  (  new   DefaultMutableTreeNode  (  content  [  i  ]  )  ,  currentRoot  ,  currentRoot  .  getChildCount  (  )  )  ; 
} 
} 
} 
} 
return   model  ; 
} 


public   static   File  [  ]  listDirectories  (  File   parentDirectory  )  { 
Vector   subDirs  =  new   Vector  (  )  ; 
File  [  ]  subFiles  =  parentDirectory  .  listFiles  (  )  ; 
File  [  ]  subDirectories  =  new   File  [  0  ]  ; 
if  (  subFiles  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  subFiles  .  length  ;  i  ++  )  { 
if  (  subFiles  [  i  ]  .  isDirectory  (  )  )  subDirs  .  add  (  subFiles  [  i  ]  )  ; 
} 
subDirectories  =  new   File  [  subDirs  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  subDirs  .  size  (  )  ;  i  ++  )  { 
subDirectories  [  i  ]  =  (  File  )  subDirs  .  get  (  i  )  ; 
} 
} 
return   subDirectories  ; 
} 






public   static   boolean   isDirectoryWritable  (  File   directory  )  { 
boolean   result  =  true  ; 
File   tmpFile  =  new   File  (  directory  ,  "test.tmp"  )  ; 
try  { 
BufferedOutputStream   bos  =  new   BufferedOutputStream  (  new   FileOutputStream  (  tmpFile  )  )  ; 
for  (  int   i  =  0  ;  i  <  10  ;  i  ++  )  { 
bos  .  write  (  0  )  ; 
} 
bos  .  close  (  )  ; 
tmpFile  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
result  =  false  ; 
} 
return   result  ; 
} 












public   static   File   getNextDirectoryName  (  File   parent  ,  String   prefix  )  { 
File   result  ; 
int   counter  =  0  ; 
result  =  new   File  (  parent  ,  prefix  +  counter  )  ; 
while  (  result  .  exists  (  )  )  { 
counter  ++  ; 
result  =  new   File  (  parent  ,  prefix  +  counter  )  ; 
} 
return   result  ; 
} 







public   static   String   getExtension  (  String   filename  )  { 
String   ext  =  ""  ; 
int   i  =  filename  .  lastIndexOf  (  '.'  )  ; 
if  (  i  >  0  &&  i  <  filename  .  length  (  )  -  1  )  { 
ext  =  filename  .  substring  (  i  +  1  )  .  toLowerCase  (  )  ; 
} 
return   ext  ; 
} 







public   static   String   removeExtension  (  String   fileName  )  { 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  fileName  .  length  (  )  ;  i  ++  )  { 
if  (  fileName  .  charAt  (  i  )  ==  '.'  )  { 
break  ; 
}  else  { 
buffer  .  append  (  fileName  .  charAt  (  i  )  )  ; 
} 
} 
return   buffer  .  toString  (  )  ; 
} 







public   URL   getAbsolutePath  (  Object   obj  ,  String   relativePath  )  { 
return   obj  .  getClass  (  )  .  getClassLoader  (  )  .  getResource  (  relativePath  )  ; 
} 







public   static   boolean   copy  (  File   sourceFile  ,  File   destinationFile  )  { 
return   hardCopy  (  sourceFile  ,  destinationFile  ,  null  )  ; 
} 









public   static   boolean   copy  (  File   sourceFile  ,  File   destinationFile  ,  StringBuffer   errorLog  )  { 
return   hardCopy  (  sourceFile  ,  destinationFile  ,  errorLog  )  ; 
} 







public   static   boolean   copy  (  String   source  ,  String   destination  )  { 
return   copy  (  source  ,  destination  ,  null  )  ; 
} 









public   static   boolean   copy  (  String   source  ,  String   destination  ,  StringBuffer   errorLog  )  { 
File   sourceFile  =  new   File  (  source  )  ; 
File   destinationFile  =  new   File  (  destination  )  ; 
return   hardCopy  (  sourceFile  ,  destinationFile  ,  errorLog  )  ; 
} 







public   static   long   fileCount  (  File   directory  ,  String  [  ]  fileFilter  )  { 
long   result  =  0  ; 
if  (  directory  .  isDirectory  (  )  )  { 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
int   length  =  files  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  result  +=  fileCount  (  files  [  i  ]  ,  fileFilter  )  ;  else  { 
for  (  int   j  =  0  ;  j  <  fileFilter  .  length  ;  j  ++  )  { 
if  (  files  [  i  ]  .  getName  (  )  .  endsWith  (  fileFilter  [  j  ]  )  )  { 
result  ++  ; 
break  ; 
} 
} 
} 
} 
} 
return   result  ; 
} 






public   static   long   fileCount  (  File   directory  )  { 
long   result  =  0  ; 
if  (  directory  .  isDirectory  (  )  )  { 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
int   length  =  files  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  result  +=  fileCount  (  files  [  i  ]  )  ;  else  { 
result  ++  ; 
} 
} 
} 
return   result  ; 
} 







public   static   long   size  (  File   directory  )  { 
long   result  =  0  ; 
if  (  directory  .  isDirectory  (  )  )  { 
File  [  ]  files  =  directory  .  listFiles  (  )  ; 
int   length  =  files  .  length  ; 
for  (  int   i  =  0  ;  i  <  length  ;  i  ++  )  { 
if  (  files  [  i  ]  .  isDirectory  (  )  )  result  +=  size  (  files  [  i  ]  )  ;  else   result  +=  files  [  i  ]  .  length  (  )  ; 
} 
}  else  { 
result  =  directory  .  length  (  )  ; 
} 
return   result  ; 
} 







public   static   boolean   copy2Directory  (  File   sourceFile  ,  File   targetDirectory  )  { 
return   copy2Directory  (  sourceFile  ,  targetDirectory  ,  null  )  ; 
} 









public   static   boolean   copy2Directory  (  File   sourceFile  ,  File   targetDirectory  ,  StringBuffer   errorLog  )  { 
boolean   result  ; 
result  =  hardCopy  (  sourceFile  ,  new   File  (  targetDirectory  ,  sourceFile  .  getName  (  )  )  ,  errorLog  )  ; 
return   result  ; 
} 


















public   static   boolean   copyFileStructure  (  File   sourceFile  ,  File   targetDirectory  ,  boolean   withSubDirectories  )  { 
return   copyFileStructure  (  sourceFile  ,  targetDirectory  ,  withSubDirectories  ,  null  )  ; 
} 









public   static   boolean   copyFileStructure  (  File   sourceDirectory  ,  File   targetDirectory  ,  boolean   withSubDirectories  ,  StringBuffer   errorLog  )  { 
if  (  !  sourceDirectory  .  isDirectory  (  )  )  return   false  ; 
if  (  !  targetDirectory  .  isDirectory  (  )  )  return   false  ; 
abortCopy  =  false  ; 
abortSuccessful  =  false  ; 
boolean   result  =  true  ; 
File  [  ]  srcDirContent  =  sourceDirectory  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  srcDirContent  .  length  ;  i  ++  )  { 
if  (  abortCopy  )  return   false  ; 
if  (  !  result  )  return   false  ; 
result  =  result  &  copyFileStructureRecursive  (  srcDirContent  [  i  ]  ,  targetDirectory  ,  withSubDirectories  ,  errorLog  )  ; 
} 
abortSuccessful  =  true  ; 
return   result  ; 
} 










private   static   boolean   copyFileStructureRecursive  (  File   sourceFile  ,  File   targetDirectory  ,  boolean   withSubDirectories  ,  StringBuffer   errorLog  )  { 
boolean   result  =  true  ; 
if  (  abortCopy  )  return   false  ; 
if  (  sourceFile  .  isDirectory  (  )  )  { 
if  (  withSubDirectories  )  { 
File   newDir  =  new   File  (  targetDirectory  ,  sourceFile  .  getName  (  )  )  ; 
newDir  .  mkdirs  (  )  ; 
if  (  sourceFile  .  list  (  )  !=  null  &&  sourceFile  .  list  (  )  .  length  >  0  )  { 
File  [  ]  srcDirContent  =  sourceFile  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  srcDirContent  .  length  ;  i  ++  )  { 
if  (  abortCopy  )  return   false  ; 
if  (  !  result  )  return   result  ; 
result  =  result  &  copyFileStructureRecursive  (  srcDirContent  [  i  ]  ,  newDir  ,  withSubDirectories  ,  errorLog  )  ; 
} 
} 
}  else  { 
if  (  sourceFile  .  list  (  )  !=  null  &&  sourceFile  .  list  (  )  .  length  >  0  )  { 
File  [  ]  srcDirContent  =  sourceFile  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  srcDirContent  .  length  ;  i  ++  )  { 
if  (  abortCopy  )  return   false  ; 
if  (  !  result  )  return   result  ; 
result  =  result  &  copyFileStructure  (  srcDirContent  [  i  ]  ,  targetDirectory  ,  withSubDirectories  ,  errorLog  )  ; 
} 
} 
} 
}  else  { 
result  =  result  &  copy2Directory  (  sourceFile  ,  targetDirectory  ,  errorLog  )  ; 
} 
return   result  ; 
} 

private   static   boolean   hardCopy  (  File   sourceFile  ,  File   destinationFile  ,  StringBuffer   errorLog  )  { 
boolean   result  =  true  ; 
try  { 
notifyCopyStart  (  destinationFile  )  ; 
destinationFile  .  getParentFile  (  )  .  mkdirs  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   len  =  0  ; 
FileInputStream   in  =  new   FileInputStream  (  sourceFile  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  destinationFile  )  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  !=  -  1  )  out  .  write  (  buffer  ,  0  ,  len  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
result  =  false  ; 
handleException  (  "\n Error in method: copy!\n"  ,  e  ,  errorLog  )  ; 
}  finally  { 
notifyCopyEnd  (  destinationFile  )  ; 
} 
return   result  ; 
} 


public   static   void   abortCopy  (  )  { 
abortCopy  =  true  ; 
} 


public   static   void   waitForAbort  (  )  { 
abortCopy  (  )  ; 
while  (  !  abortSuccessful  )  { 
} 
} 





public   static   boolean   delete  (  File   file  )  { 
return   delete  (  file  ,  null  )  ; 
} 







public   static   boolean   delete  (  File   file  ,  StringBuffer   errorLog  )  { 
boolean   result  =  true  ; 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  fileDirContent  =  file  .  listFiles  (  )  ; 
if  (  file  .  list  (  )  !=  null  &&  file  .  list  (  )  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  fileDirContent  .  length  ;  i  ++  )  result  =  result  &  delete  (  fileDirContent  [  i  ]  ,  errorLog  )  ; 
} 
result  =  file  .  delete  (  )  ; 
}  else  { 
result  =  file  .  delete  (  )  ; 
} 
return   result  ; 
} 

private   static   void   handleException  (  String   errorMessage  ,  Exception   e  ,  StringBuffer   errorLog  )  { 
if  (  errorLog  !=  null  )  { 
errorLog  .  append  (  "\n Error in method: copy!\n"  )  ; 
errorLog  .  append  (  e  )  ; 
errorLog  .  append  (  "\n"  )  ; 
}  else  { 
System  .  out  .  println  (  "\n Error in method: copy!\n"  )  ; 
System  .  out  .  println  (  e  )  ; 
System  .  out  .  println  (  "\n"  )  ; 
} 
} 


public   static   void   addFileCopyListener  (  FLGFileCopyListener   listener  )  { 
if  (  fileCopyListeners  ==  null  )  fileCopyListeners  =  new   Vector  (  )  ; 
fileCopyListeners  .  add  (  listener  )  ; 
} 


public   static   boolean   removeFileCopyListener  (  FLGFileCopyListener   listener  )  { 
return   fileCopyListeners  .  remove  (  listener  )  ; 
} 


private   static   void   notifyCopyStart  (  File   file  )  { 
FLGFileCopyListener   listener  ; 
Iterator   iterator  =  fileCopyListeners  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
listener  =  (  FLGFileCopyListener  )  iterator  .  next  (  )  ; 
listener  .  copyStarted  (  new   FLGFileCopyEvent  (  file  )  )  ; 
} 
} 


private   static   void   notifyCopyEnd  (  File   file  )  { 
FLGFileCopyListener   listener  ; 
Iterator   iterator  =  fileCopyListeners  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  )  { 
listener  =  (  FLGFileCopyListener  )  iterator  .  next  (  )  ; 
listener  .  copyEnded  (  new   FLGFileCopyEvent  (  file  )  )  ; 
} 
} 

protected   static   Vector   fileCopyListeners  =  new   Vector  (  )  ; 

private   static   boolean   abortCopy  ; 

private   static   boolean   abortSuccessful  ; 
} 

