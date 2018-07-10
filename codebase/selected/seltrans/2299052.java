package   com  .  generatescape  .  utils  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  util  .  Vector  ; 

public   class   FileUtils  { 





public   static   boolean   deleteDir  (  File   dir  )  { 
if  (  dir  .  isDirectory  (  )  )  { 
String  [  ]  children  =  dir  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
boolean   success  =  deleteDir  (  new   File  (  dir  ,  children  [  i  ]  )  )  ; 
if  (  !  success  )  { 
return   false  ; 
} 
} 
} 
return   dir  .  delete  (  )  ; 
} 





public   static   String   getBaseName  (  String   name  )  { 
String   base  =  new   File  (  name  )  .  getName  (  )  ; 
int   index  =  base  .  lastIndexOf  (  '.'  )  ; 
if  (  index  !=  -  1  )  base  =  base  .  substring  (  0  ,  index  )  ; 
return   base  ; 
} 






public   static   String   getFileAsString  (  String   name  )  throws   IOException  { 
File   file  =  new   File  (  name  )  ; 
return   getFileAsString  (  file  )  ; 
} 






public   static   String   getFileAsString  (  File   file  )  throws   IOException  { 
StringBuffer   text  =  new   StringBuffer  (  )  ; 
FileReader   in  =  new   FileReader  (  file  )  ; 
char   buffer  [  ]  =  new   char  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  in  .  read  (  buffer  )  )  !=  -  1  )  text  .  append  (  new   String  (  buffer  ,  0  ,  bytes_read  )  )  ; 
return   text  .  toString  (  )  ; 
} 





public   static   String   getFileExtension  (  String   filename  )  { 
String   extension  =  ""  ; 
int   index  =  filename  .  lastIndexOf  (  '.'  )  ; 
if  (  index  !=  -  1  )  extension  =  filename  .  substring  (  index  )  ; 
return   extension  ; 
} 





public   static   String   getFileExtension  (  File   file  )  { 
String   base  =  file  .  getName  (  )  ; 
String   extension  =  ""  ; 
int   index  =  base  .  lastIndexOf  (  '.'  )  ; 
if  (  index  !=  -  1  )  extension  =  base  .  substring  (  index  )  ; 
return   extension  ; 
} 






public   static   String   getFileExtension  (  String   filename  ,  boolean   keepDot  )  { 
filename  =  filename  .  replace  (  '\\'  ,  '/'  )  ; 
String   name  ; 
int   namePos  =  filename  .  lastIndexOf  (  '/'  )  ; 
if  (  namePos  !=  -  1  )  { 
name  =  filename  .  substring  (  namePos  +  1  )  ; 
}  else  { 
name  =  filename  ; 
} 
String   ext  ; 
int   extPos  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  extPos  !=  -  1  )  { 
if  (  keepDot  )  ext  =  name  .  substring  (  extPos  )  ;  else   ext  =  name  .  substring  (  extPos  +  1  )  ; 
}  else  { 
ext  =  ""  ; 
} 
return   ext  ; 
} 






public   static   String   getFileExtension  (  File   file  ,  boolean   keepDot  )  { 
String   base  =  file  .  getName  (  )  ; 
String   extension  =  ""  ; 
int   index  =  base  .  lastIndexOf  (  '.'  )  ; 
if  (  index  !=  -  1  )  { 
if  (  keepDot  )  extension  =  base  .  substring  (  index  )  ;  else   extension  =  base  .  substring  (  index  +  1  )  ; 
} 
return   extension  ; 
} 






public   static   String   getInputStreamAsString  (  InputStream   inStream  )  throws   IOException  { 
StringBuffer   text  =  new   StringBuffer  (  )  ; 
InputStreamReader   in  =  new   InputStreamReader  (  inStream  )  ; 
char   buffer  [  ]  =  new   char  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  in  .  read  (  buffer  )  )  !=  -  1  )  text  .  append  (  new   String  (  buffer  ,  0  ,  bytes_read  )  )  ; 
return   text  .  toString  (  )  ; 
} 





public   static   File   getRoot  (  File   file  )  { 
File   parent  =  file  ; 
while  (  parent  .  getParentFile  (  )  !=  null  )  parent  =  parent  .  getParentFile  (  )  ; 
return   parent  ; 
} 






public   static   void   copy  (  File   from_file  ,  File   to_file  )  throws   IOException  { 
if  (  !  from_file  .  exists  (  )  )  abort  (  "FileCopy: no such source file: "  +  from_file  .  getName  (  )  )  ; 
if  (  !  from_file  .  isFile  (  )  )  abort  (  "FileCopy: can't copy directory: "  +  from_file  .  getName  (  )  )  ; 
if  (  !  from_file  .  canRead  (  )  )  abort  (  "FileCopy: source file is unreadable: "  +  from_file  .  getName  (  )  )  ; 
if  (  to_file  .  isDirectory  (  )  )  to_file  =  new   File  (  to_file  ,  from_file  .  getName  (  )  )  ; 
if  (  to_file  .  exists  (  )  )  { 
if  (  !  to_file  .  canWrite  (  )  )  abort  (  "FileCopy: destination file is unwriteable: "  +  to_file  .  getName  (  )  )  ; 
}  else  { 
String   parent  =  to_file  .  getParent  (  )  ; 
if  (  parent  ==  null  )  parent  =  System  .  getProperty  (  "user.dir"  )  ; 
File   dir  =  new   File  (  parent  )  ; 
if  (  !  dir  .  exists  (  )  )  abort  (  "FileCopy: destination directory doesn't exist: "  +  parent  )  ; 
if  (  dir  .  isFile  (  )  )  abort  (  "FileCopy: destination is not a directory: "  +  parent  )  ; 
if  (  !  dir  .  canWrite  (  )  )  abort  (  "FileCopy: destination directory is unwriteable: "  +  parent  )  ; 
} 
FileInputStream   from  =  null  ; 
FileOutputStream   to  =  null  ; 
try  { 
from  =  new   FileInputStream  (  from_file  )  ; 
to  =  new   FileOutputStream  (  to_file  )  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  from  .  read  (  buffer  )  )  !=  -  1  )  { 
to  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
}  finally  { 
if  (  from  !=  null  )  { 
try  { 
from  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
if  (  to  !=  null  )  { 
try  { 
to  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 






public   static   void   copy  (  String   from_name  ,  String   to_name  )  throws   IOException  { 
File   from_file  =  new   File  (  from_name  )  ; 
File   to_file  =  new   File  (  to_name  )  ; 
if  (  !  from_file  .  exists  (  )  )  abort  (  "FileCopy: no such source file: "  +  from_name  )  ; 
if  (  !  from_file  .  isFile  (  )  )  abort  (  "FileCopy: can't copy directory: "  +  from_name  )  ; 
if  (  !  from_file  .  canRead  (  )  )  abort  (  "FileCopy: source file is unreadable: "  +  from_name  )  ; 
if  (  to_file  .  isDirectory  (  )  )  to_file  =  new   File  (  to_file  ,  from_file  .  getName  (  )  )  ; 
if  (  to_file  .  exists  (  )  )  { 
if  (  !  to_file  .  canWrite  (  )  )  abort  (  "FileCopy: destination file is unwriteable: "  +  to_name  )  ; 
}  else  { 
String   parent  =  to_file  .  getParent  (  )  ; 
if  (  parent  ==  null  )  parent  =  System  .  getProperty  (  "user.dir"  )  ; 
File   dir  =  new   File  (  parent  )  ; 
if  (  !  dir  .  exists  (  )  )  abort  (  "FileCopy: destination directory doesn't exist: "  +  parent  )  ; 
if  (  dir  .  isFile  (  )  )  abort  (  "FileCopy: destination is not a directory: "  +  parent  )  ; 
if  (  !  dir  .  canWrite  (  )  )  abort  (  "FileCopy: destination directory is unwriteable: "  +  parent  )  ; 
} 
FileInputStream   from  =  null  ; 
FileOutputStream   to  =  null  ; 
try  { 
from  =  new   FileInputStream  (  from_file  )  ; 
to  =  new   FileOutputStream  (  to_file  )  ; 
byte  [  ]  buffer  =  new   byte  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  from  .  read  (  buffer  )  )  !=  -  1  )  { 
to  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
}  finally  { 
if  (  from  !=  null  )  { 
try  { 
from  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
if  (  to  !=  null  )  { 
try  { 
to  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 






public   static   File  [  ]  findByExt  (  File   base  ,  String   ext  )  { 
Vector   result  =  new   Vector  (  )  ; 
findByExtWorker  (  result  ,  base  ,  ext  )  ; 
Object   objs  [  ]  =  result  .  toArray  (  )  ; 
File   files  [  ]  =  new   File  [  objs  .  length  ]  ; 
System  .  arraycopy  (  objs  ,  0  ,  files  ,  0  ,  objs  .  length  )  ; 
return   files  ; 
} 





public   static   String   prettyPrintFileSize  (  File   file  )  { 
long   size  =  file  .  length  (  )  ; 
if  (  size  <  1028  )  return  "1 k"  ; 
size  =  size  /  1028  ; 
return   size  +  " k"  ; 
} 

public   static   void   saveStreamToFile  (  InputStream   in  ,  File   outFile  )  throws   IOException  { 
FileOutputStream   out  =  null  ; 
try  { 
out  =  new   FileOutputStream  (  outFile  )  ; 
byte  [  ]  buf  =  new   byte  [  4096  ]  ; 
int   bytes_read  ; 
while  (  (  bytes_read  =  in  .  read  (  buf  )  )  !=  -  1  )  out  .  write  (  buf  ,  0  ,  bytes_read  )  ; 
}  finally  { 
if  (  in  !=  null  )  try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
if  (  out  !=  null  )  try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 




private   static   void   abort  (  String   msg  )  throws   IOException  { 
throw   new   IOException  (  msg  )  ; 
} 






private   static   void   findByExtWorker  (  Vector   result  ,  File   base  ,  String   ext  )  { 
File   files  [  ]  =  base  .  listFiles  (  )  ; 
if  (  files  ==  null  )  { 
return  ; 
} 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   file  =  files  [  i  ]  ; 
if  (  !  file  .  isDirectory  (  )  )  { 
if  (  ext  .  equals  (  "*"  )  )  { 
result  .  add  (  file  )  ; 
}  else  { 
String   currentExt  =  getFileExtension  (  file  )  ; 
if  (  currentExt  .  equalsIgnoreCase  (  ext  )  )  { 
result  .  add  (  file  )  ; 
} 
} 
}  else  { 
findByExtWorker  (  result  ,  file  ,  ext  )  ; 
} 
} 
} 
} 

