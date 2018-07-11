package   uk  .  ac  .  ed  .  ph  .  aardvark  .  commons  .  util  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  Closeable  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  Reader  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 









public   final   class   IOUtilities  { 


public   static   int   BUFFER_SIZE  =  32  *  1024  ; 


public   static   int   MAX_TEXT_STREAM_SIZE  =  1024  *  1024  ; 







public   static   void   ensureDirectoryCreated  (  File   directory  )  throws   IOException  { 
if  (  !  directory  .  isDirectory  (  )  )  { 
if  (  !  directory  .  mkdirs  (  )  )  { 
throw   new   IOException  (  "Could not create directory "  +  directory  )  ; 
} 
} 
} 








public   static   void   ensureFileCreated  (  File   file  )  throws   IOException  { 
File   parentDirectory  =  file  .  getParentFile  (  )  ; 
if  (  parentDirectory  !=  null  )  { 
ensureDirectoryCreated  (  parentDirectory  )  ; 
} 
if  (  !  file  .  isFile  (  )  )  { 
if  (  !  file  .  createNewFile  (  )  )  { 
throw   new   IOException  (  "Could not create file "  +  file  )  ; 
} 
} 
} 










public   static   void   ensureClose  (  Closeable  ...  streams  )  throws   IOException  { 
IOException   firstException  =  null  ; 
for  (  Closeable   stream  :  streams  )  { 
if  (  stream  !=  null  )  { 
try  { 
stream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
firstException  =  e  ; 
} 
} 
} 
if  (  firstException  !=  null  )  { 
throw   firstException  ; 
} 
} 












public   static   void   transfer  (  InputStream   inStream  ,  OutputStream   outStream  )  throws   IOException  { 
if  (  inStream   instanceof   FileInputStream  &&  outStream   instanceof   FileOutputStream  )  { 
transfer  (  (  FileInputStream  )  inStream  ,  (  FileOutputStream  )  outStream  )  ; 
}  else  { 
transfer  (  inStream  ,  outStream  ,  true  )  ; 
} 
} 





public   static   void   transfer  (  FileInputStream   fileInStream  ,  FileOutputStream   fileOutStream  )  throws   IOException  { 
FileChannel   fileInChannel  =  fileInStream  .  getChannel  (  )  ; 
FileChannel   fileOutChannel  =  fileOutStream  .  getChannel  (  )  ; 
long   fileInSize  =  fileInChannel  .  size  (  )  ; 
try  { 
long   transferred  =  fileInChannel  .  transferTo  (  0  ,  fileInSize  ,  fileOutChannel  )  ; 
if  (  transferred  !=  fileInSize  )  { 
throw   new   IOException  (  "transfer() did not complete"  )  ; 
} 
}  finally  { 
ensureClose  (  fileInChannel  ,  fileOutChannel  )  ; 
} 
} 










public   static   void   transfer  (  InputStream   inStream  ,  OutputStream   outStream  ,  boolean   closeOutputStream  )  throws   IOException  { 
transfer  (  inStream  ,  outStream  ,  true  ,  closeOutputStream  )  ; 
} 











public   static   void   transfer  (  InputStream   inStream  ,  OutputStream   outStream  ,  boolean   closeInputStream  ,  boolean   closeOutputStream  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   count  ; 
try  { 
while  (  (  count  =  inStream  .  read  (  buffer  )  )  !=  -  1  )  { 
outStream  .  write  (  buffer  ,  0  ,  count  )  ; 
} 
}  finally  { 
if  (  closeInputStream  )  { 
inStream  .  close  (  )  ; 
} 
if  (  closeOutputStream  )  { 
outStream  .  close  (  )  ; 
}  else  { 
outStream  .  flush  (  )  ; 
} 
} 
} 

public   static   byte  [  ]  readBinaryStream  (  InputStream   stream  )  throws   IOException  { 
ByteArrayOutputStream   outStream  =  new   ByteArrayOutputStream  (  )  ; 
transfer  (  stream  ,  outStream  )  ; 
return   outStream  .  toByteArray  (  )  ; 
} 












public   static   String   readCharacterStream  (  Reader   reader  )  throws   IOException  { 
BufferedReader   bufferedReader  =  new   BufferedReader  (  reader  )  ; 
String   line  ; 
int   size  =  0  ; 
StringBuilder   result  =  new   StringBuilder  (  )  ; 
while  (  (  line  =  bufferedReader  .  readLine  (  )  )  !=  null  )  { 
size  +=  line  .  length  (  )  +  1  ; 
if  (  size  >  MAX_TEXT_STREAM_SIZE  )  { 
throw   new   IOException  (  "String data exceeds current maximum safe size ("  +  MAX_TEXT_STREAM_SIZE  +  ")"  )  ; 
} 
result  .  append  (  line  )  .  append  (  "\n"  )  ; 
} 
bufferedReader  .  close  (  )  ; 
return   result  .  toString  (  )  ; 
} 









public   static   String   readUnicodeStream  (  InputStream   in  )  throws   IOException  { 
return   readCharacterStream  (  new   InputStreamReader  (  in  ,  "UTF-8"  )  )  ; 
} 









public   static   String   readUnicodeFile  (  File   file  )  throws   IOException  { 
InputStream   inStream  =  new   FileInputStream  (  file  )  ; 
try  { 
return   readUnicodeStream  (  inStream  )  ; 
}  finally  { 
inStream  .  close  (  )  ; 
} 
} 










public   static   void   writeUnicodeFile  (  File   outputFile  ,  String   data  )  throws   IOException  { 
writeFile  (  outputFile  ,  data  ,  "UTF-8"  )  ; 
} 












public   static   void   writeFile  (  File   outputFile  ,  String   data  ,  String   encoding  )  throws   IOException  { 
FileOutputStream   outStream  =  new   FileOutputStream  (  outputFile  )  ; 
OutputStreamWriter   writer  =  null  ; 
try  { 
writer  =  new   OutputStreamWriter  (  outStream  ,  encoding  )  ; 
writer  .  write  (  data  )  ; 
}  finally  { 
if  (  writer  !=  null  )  { 
writer  .  close  (  )  ; 
}  else  { 
outStream  .  close  (  )  ; 
} 
} 
} 












public   static   void   recursivelyDelete  (  File   root  ,  boolean   deleteRoot  )  throws   IOException  { 
if  (  root  .  isDirectory  (  )  )  { 
File  [  ]  contents  =  root  .  listFiles  (  )  ; 
for  (  File   child  :  contents  )  { 
recursivelyDelete  (  child  ,  true  )  ; 
} 
} 
if  (  deleteRoot  )  { 
if  (  !  root  .  delete  (  )  )  { 
throw   new   IOException  (  "Could not delete directory "  +  root  )  ; 
} 
} 
} 





public   static   void   recursivelyDelete  (  File   root  )  throws   IOException  { 
recursivelyDelete  (  root  ,  true  )  ; 
} 
} 

