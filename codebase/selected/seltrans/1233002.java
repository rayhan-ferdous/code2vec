package   jragonsoft  .  javautil  .  util  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   jragonsoft  .  javautil  .  support  .  RegExFilenameFilter  ; 









public   class   FileUtils  { 


public   static   final   int   KB  =  1024  ; 


public   static   int   MAX_READ  =  KB  *  64  ; 


public   static   String   LINE_SEP  =  System  .  getProperty  (  "line.separator"  )  ; 


public   static   String   FILE_SEP  =  System  .  getProperty  (  "file.separator"  )  ; 


public   static   String   PATH_SEP  =  System  .  getProperty  (  "path.separator"  )  ; 









public   static   String   getPathname  (  String   filename  )  { 
int   pos  =  filename  .  lastIndexOf  (  FILE_SEP  )  ; 
if  (  pos  >  0  )  { 
return   filename  .  substring  (  0  ,  pos  )  ; 
}  else  { 
return  ""  ; 
} 
} 









public   static   String   getBasename  (  String   filename  )  { 
int   pos  =  filename  .  lastIndexOf  (  FILE_SEP  )  ; 
if  (  pos  >=  0  )  { 
return   filename  .  substring  (  pos  +  1  )  ; 
}  else  { 
return   filename  ; 
} 
} 








public   static   String   getFilenameExt  (  String   filename  )  { 
String  [  ]  parts  =  StringUtils  .  split  (  filename  ,  "."  )  ; 
if  (  parts  .  length  <  2  )  { 
return  ""  ; 
} 
return   parts  [  parts  .  length  -  1  ]  ; 
} 








public   static   String   getFilenameWithoutExt  (  String   filename  )  { 
int   pos  =  filename  .  lastIndexOf  (  "."  )  ; 
if  (  pos  ==  -  1  )  { 
return   filename  ; 
} 
return   filename  .  substring  (  0  ,  pos  )  ; 
} 









public   static   String   joinPath  (  String  [  ]  paths  )  { 
return   StringUtils  .  join  (  FILE_SEP  ,  paths  )  ; 
} 








public   static   String   getAbsPath  (  String   filename  )  { 
try  { 
return   new   File  (  filename  )  .  getCanonicalPath  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 










public   static   boolean   isSamePath  (  File   left  ,  File   right  )  { 
try  { 
return   left  .  getCanonicalPath  (  )  .  equals  (  right  .  getCanonicalPath  (  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 










public   static   String   appendBasename  (  String   filename  ,  String   appendText  )  { 
String   extname  =  getFilenameExt  (  filename  )  ; 
String   basename  =  getFilenameWithoutExt  (  filename  )  ; 
if  (  !  extname  .  equals  (  ""  )  )  { 
extname  =  "."  +  extname  ; 
} 
return   basename  +  appendText  +  extname  ; 
} 








public   static   boolean   isText  (  File   file  )  { 
return   isText  (  file  ,  0  )  ; 
} 










public   static   boolean   isText  (  File   file  ,  int   readSize  )  { 
try  { 
if  (  getLength  (  file  )  ==  0  )  { 
return   true  ; 
}  else  { 
return   isText  (  new   FileInputStream  (  file  )  ,  readSize  )  ; 
} 
}  catch  (  FileNotFoundException   e  )  { 
return   false  ; 
} 
} 














public   static   boolean   isText  (  InputStream   inputstream  ,  int   readSize  )  { 
int   MAX_READ  =  FileUtils  .  MAX_READ  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  inputstream  )  ; 
try  { 
byte   buffer  [  ]  =  new   byte  [  MAX_READ  ]  ; 
int   len  =  in  .  read  (  buffer  ,  0  ,  1  )  ; 
if  (  len  ==  -  1  )  { 
throw   new   RuntimeException  (  "Can't read file!"  )  ; 
} 
if  (  buffer  [  0  ]  ==  0  )  { 
return   false  ; 
} 
int   readLen  =  1  ; 
while  (  (  (  len  =  in  .  read  (  buffer  ,  0  ,  MAX_READ  )  )  !=  -  1  )  )  { 
for  (  int   i  =  len  -  1  ;  i  >=  0  ;  i  --  )  { 
byte   b  =  buffer  [  i  ]  ; 
if  (  !  (  (  b  >=  32  &&  b  <=  127  )  ||  (  b  ==  '\n'  )  ||  (  b  ==  '\r'  )  ||  (  b  ==  '\t'  )  ||  (  b  ==  '\b'  )  )  )  { 
return   false  ; 
} 
readLen  ++  ; 
if  (  readSize  >  0  &&  readLen  >=  readSize  )  { 
break  ; 
} 
} 
} 
return   true  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 
} 







public   static   void   touch  (  File   file  )  { 
try  { 
FileOutputStream   out  =  new   FileOutputStream  (  file  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Failed to create empty file: "  +  file  .  getAbsolutePath  (  )  ,  e  )  ; 
} 
} 








public   static   void   delete  (  File   file  )  { 
if  (  !  file  .  exists  (  )  )  { 
return  ; 
} 
if  (  !  file  .  delete  (  )  )  { 
throw   new   RuntimeException  (  "Failed to delete file from "  +  file  .  getAbsolutePath  (  )  )  ; 
} 
} 










public   static   void   move  (  File   srcFile  ,  File   destFile  )  { 
if  (  !  srcFile  .  renameTo  (  destFile  )  )  { 
throw   new   RuntimeException  (  "Failed to move file from "  +  srcFile  .  getAbsolutePath  (  )  +  " to "  +  destFile  .  getAbsolutePath  (  )  )  ; 
} 
} 









public   static   void   copy  (  File   srcFile  ,  File   destFile  )  { 
try  { 
FileInputStream   fin  =  new   FileInputStream  (  srcFile  )  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  fin  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  destFile  )  ; 
byte  [  ]  buffer  =  new   byte  [  MAX_READ  ]  ; 
int   len  =  -  1  ; 
while  (  (  len  =  in  .  read  (  buffer  ,  0  ,  MAX_READ  )  )  !=  -  1  )  { 
out  .  write  (  buffer  ,  0  ,  len  )  ; 
out  .  flush  (  )  ; 
} 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Failed to copy file from "  +  srcFile  .  getAbsolutePath  (  )  +  " to "  +  destFile  .  getAbsolutePath  (  )  ,  e  )  ; 
} 
} 









public   static   void   writeLine  (  File   file  ,  String   line  )  { 
try  { 
boolean   isAppendMode  =  false  ; 
PrintWriter   out  =  new   PrintWriter  (  new   BufferedWriter  (  new   FileWriter  (  file  ,  isAppendMode  )  )  )  ; 
out  .  println  (  line  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 









public   static   void   appendLine  (  File   file  ,  String   line  )  { 
try  { 
boolean   isAppendMode  =  true  ; 
PrintWriter   out  =  new   PrintWriter  (  new   BufferedWriter  (  new   FileWriter  (  file  ,  isAppendMode  )  )  )  ; 
out  .  println  (  line  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   List   getLinesAsList  (  File   file  )  { 
try  { 
return   getLinesAsList  (  new   FileInputStream  (  file  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   List   getLinesAsList  (  InputStream   in  )  { 
try  { 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
List   lines  =  new   ArrayList  (  )  ; 
String   line  =  null  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
lines  .  add  (  line  )  ; 
} 
reader  .  close  (  )  ; 
return   lines  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   String  [  ]  getLines  (  File   file  )  { 
List   lines  =  getLinesAsList  (  file  )  ; 
return   ArrayUtils  .  toStringArray  (  lines  )  ; 
} 










public   static   String   getLine  (  File   file  ,  int   lineNum  )  { 
try  { 
return   getLine  (  new   FileInputStream  (  file  )  ,  lineNum  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 










public   static   String   getLine  (  InputStream   in  ,  int   lineNum  )  { 
if  (  lineNum  <=  0  )  { 
throw   new   RuntimeException  (  "lineNum must be one or greater index of line number."  )  ; 
} 
try  { 
String   ret  =  null  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
String   line  =  null  ; 
int   currentLineNum  =  0  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
currentLineNum  ++  ; 
if  (  currentLineNum  !=  lineNum  )  { 
continue  ; 
} 
ret  =  line  ; 
break  ; 
} 
reader  .  close  (  )  ; 
if  (  ret  ==  null  )  { 
throw   new   RuntimeException  (  "lineNum is set higher than max line number."  )  ; 
} 
return   ret  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 










public   static   int   getInt  (  File   file  ,  int   lineNum  )  { 
return   Integer  .  parseInt  (  getLine  (  file  ,  lineNum  )  )  ; 
} 










public   static   double   getDouble  (  File   file  ,  int   lineNum  )  { 
return   Double  .  parseDouble  (  getLine  (  file  ,  lineNum  )  )  ; 
} 










public   static   boolean   getBoolean  (  File   file  ,  int   lineNum  )  { 
return   Boolean  .  valueOf  (  getLine  (  file  ,  lineNum  )  )  .  booleanValue  (  )  ; 
} 









public   static   void   writeString  (  File   file  ,  String   text  )  { 
try  { 
boolean   isAppendMode  =  false  ; 
PrintWriter   out  =  new   PrintWriter  (  new   BufferedWriter  (  new   FileWriter  (  file  ,  isAppendMode  )  )  )  ; 
out  .  print  (  text  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   String   getString  (  File   file  )  { 
try  { 
return   getString  (  new   FileInputStream  (  file  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   String   getString  (  InputStream   in  )  { 
try  { 
StringBuffer   ret  =  new   StringBuffer  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
char   buffer  [  ]  =  new   char  [  MAX_READ  ]  ; 
int   len  ; 
while  (  (  len  =  reader  .  read  (  buffer  ,  0  ,  MAX_READ  )  )  !=  -  1  )  { 
ret  .  append  (  buffer  ,  0  ,  len  )  ; 
} 
reader  .  close  (  )  ; 
return   ret  .  toString  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   byte  [  ]  getBytes  (  File   file  )  { 
try  { 
FileInputStream   fin  =  new   FileInputStream  (  file  )  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  fin  )  ; 
int   length  =  (  int  )  getLength  (  file  )  ; 
byte  [  ]  buffer  =  new   byte  [  length  ]  ; 
in  .  read  (  buffer  ,  0  ,  length  )  ; 
in  .  close  (  )  ; 
return   buffer  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   long   getLength  (  File   file  )  { 
try  { 
RandomAccessFile   rafile  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
long   len  =  rafile  .  length  (  )  ; 
rafile  .  close  (  )  ; 
return   len  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   InputStream   getInputStream  (  String   filename  )  { 
try  { 
InputStream   in  =  null  ; 
java  .  net  .  URL   url  =  null  ; 
try  { 
url  =  new   java  .  net  .  URL  (  filename  )  ; 
in  =  url  .  openStream  (  )  ; 
}  catch  (  java  .  net  .  MalformedURLException   e  )  { 
in  =  new   FileInputStream  (  filename  )  ; 
} 
return   in  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   InputStreamReader   getInputStreamReader  (  String   filename  )  { 
return   new   InputStreamReader  (  getInputStream  (  filename  )  )  ; 
} 









public   static   File  [  ]  globHiddenFiles  (  File   dir  )  { 
ArrayList   subdirs  =  new   ArrayList  (  )  ; 
File  [  ]  childs  =  dir  .  listFiles  (  )  ; 
if  (  childs  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
for  (  int   i  =  childs  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   child  =  childs  [  i  ]  ; 
if  (  child  .  isFile  (  )  &&  child  .  isHidden  (  )  )  { 
subdirs  .  add  (  child  )  ; 
} 
} 
return   toFileArray  (  subdirs  )  ; 
} 








public   static   File  [  ]  globFiles  (  File   dir  )  { 
ArrayList   subdirs  =  new   ArrayList  (  )  ; 
File  [  ]  childs  =  dir  .  listFiles  (  )  ; 
if  (  childs  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
for  (  int   i  =  childs  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   child  =  childs  [  i  ]  ; 
if  (  child  .  isFile  (  )  )  { 
subdirs  .  add  (  child  )  ; 
} 
} 
return   toFileArray  (  subdirs  )  ; 
} 











public   static   File  [  ]  globFiles  (  File   dir  ,  String   regex  )  { 
boolean   isRecursive  =  false  ; 
return   globFiles  (  dir  ,  regex  ,  isRecursive  )  ; 
} 













public   static   File  [  ]  globFiles  (  File   dir  ,  String   regex  ,  boolean   isRecursive  )  { 
return   globFiles  (  dir  ,  Pattern  .  compile  (  regex  )  ,  isRecursive  )  ; 
} 













public   static   File  [  ]  globFiles  (  File   dir  ,  Pattern   pattern  ,  boolean   isRecursive  )  { 
RegExFilenameFilter   filter  =  new   RegExFilenameFilter  (  pattern  )  ; 
ArrayList   files  =  new   ArrayList  (  )  ; 
File  [  ]  childs  =  dir  .  listFiles  (  filter  )  ; 
if  (  childs  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
files  .  addAll  (  Arrays  .  asList  (  childs  )  )  ; 
if  (  isRecursive  )  { 
File  [  ]  subdirs  =  globDirs  (  dir  )  ; 
for  (  int   i  =  subdirs  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   subdir  =  subdirs  [  i  ]  ; 
File  [  ]  results  =  globFiles  (  subdir  ,  pattern  ,  isRecursive  )  ; 
files  .  addAll  (  Arrays  .  asList  (  results  )  )  ; 
} 
} 
return   toFileArray  (  files  )  ; 
} 








public   static   File  [  ]  globDirs  (  File   dir  )  { 
ArrayList   subdirs  =  new   ArrayList  (  )  ; 
File  [  ]  childs  =  dir  .  listFiles  (  )  ; 
if  (  childs  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
for  (  int   i  =  childs  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   child  =  childs  [  i  ]  ; 
if  (  child  .  isDirectory  (  )  )  { 
subdirs  .  add  (  child  )  ; 
} 
} 
return   toFileArray  (  subdirs  )  ; 
} 










public   static   File  [  ]  globDirs  (  File   dir  ,  String   regex  )  { 
boolean   isRecursive  =  false  ; 
return   globDirs  (  dir  ,  regex  ,  isRecursive  )  ; 
} 













public   static   File  [  ]  globDirs  (  File   dir  ,  String   regex  ,  boolean   isRecursive  )  { 
Pattern   pattern  =  Pattern  .  compile  (  regex  )  ; 
ArrayList   subdirs  =  new   ArrayList  (  )  ; 
File  [  ]  childs  =  dir  .  listFiles  (  )  ; 
if  (  childs  ==  null  )  { 
return   new   File  [  0  ]  ; 
} 
for  (  int   i  =  childs  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   child  =  childs  [  i  ]  ; 
Matcher   matcher  =  pattern  .  matcher  (  child  .  getName  (  )  )  ; 
if  (  child  .  isDirectory  (  )  &&  matcher  .  find  (  )  )  { 
subdirs  .  add  (  child  )  ; 
} 
} 
if  (  isRecursive  )  { 
File  [  ]  subchilds  =  (  File  [  ]  )  ArrayUtils  .  copy  (  childs  )  ; 
for  (  int   i  =  subchilds  .  length  -  1  ;  i  >=  0  ;  i  --  )  { 
File   subdir  =  subchilds  [  i  ]  ; 
File  [  ]  results  =  globDirs  (  subdir  ,  regex  ,  isRecursive  )  ; 
subdirs  .  addAll  (  Arrays  .  asList  (  results  )  )  ; 
} 
} 
return   toFileArray  (  subdirs  )  ; 
} 









public   static   void   copyStream  (  InputStream   inputstream  ,  OutputStream   outstream  )  { 
try  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  inputstream  )  ; 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  outstream  )  ; 
byte  [  ]  buf  =  new   byte  [  MAX_READ  ]  ; 
int   len  =  -  1  ; 
while  (  (  len  =  in  .  read  (  buf  ,  0  ,  MAX_READ  )  )  !=  -  1  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
out  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 

public   static   void   copyFilteredStream  (  InputStream   inputstream  ,  OutputStream   outstream  ,  Properties   dict  )  { 
copyFilteredStream  (  inputstream  ,  outstream  ,  dict  ,  LINE_SEP  )  ; 
} 

public   static   void   copyFilteredStream  (  InputStream   inputstream  ,  OutputStream   outstream  ,  Properties   dict  ,  String   lineSep  )  { 
try  { 
BufferedReader   in  =  new   BufferedReader  (  new   InputStreamReader  (  inputstream  )  )  ; 
BufferedWriter   out  =  new   BufferedWriter  (  new   OutputStreamWriter  (  outstream  )  )  ; 
String   line  ; 
while  (  (  line  =  in  .  readLine  (  )  )  !=  null  )  { 
line  =  RegexUtils  .  expandSubstitutions  (  line  ,  dict  )  ; 
out  .  write  (  line  ,  0  ,  line  .  length  (  )  )  ; 
out  .  write  (  lineSep  ,  0  ,  lineSep  .  length  (  )  )  ; 
} 
out  .  flush  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  e  )  ; 
} 
} 








public   static   File  [  ]  toFileArray  (  List   list  )  { 
Object  [  ]  array  =  list  .  toArray  (  )  ; 
File  [  ]  castedArray  =  new   File  [  array  .  length  ]  ; 
System  .  arraycopy  (  array  ,  0  ,  castedArray  ,  0  ,  array  .  length  )  ; 
return   castedArray  ; 
} 
} 

