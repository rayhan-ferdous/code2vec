package   org  .  archive  .  io  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  SequenceInputStream  ; 
import   java  .  io  .  Serializable  ; 
import   org  .  archive  .  crawler  .  checkpoint  .  ObjectPlusFilesInputStream  ; 
import   org  .  archive  .  crawler  .  checkpoint  .  ObjectPlusFilesOutputStream  ; 
















public   class   DiskByteQueue   implements   Serializable  { 

private   static   final   String   IN_FILE_EXTENSION  =  ".qin"  ; 

private   static   final   String   OUT_FILE_EXTENSION  =  ".qout"  ; 

File   tempDir  ; 

String   backingFilenamePrefix  ; 

File   inFile  ; 

File   outFile  ; 

transient   FlipFileInputStream   headStream  ; 

long   rememberedPosition  =  0  ; 

transient   FlipFileOutputStream   tailStream  ; 









public   DiskByteQueue  (  File   tempDir  ,  String   backingFilenamePrefix  ,  boolean   reuse  )  { 
super  (  )  ; 
this  .  tempDir  =  tempDir  ; 
this  .  backingFilenamePrefix  =  backingFilenamePrefix  ; 
tempDir  .  mkdirs  (  )  ; 
String   pathPrefix  =  tempDir  .  getPath  (  )  +  File  .  separatorChar  +  backingFilenamePrefix  ; 
inFile  =  new   File  (  pathPrefix  +  IN_FILE_EXTENSION  )  ; 
outFile  =  new   File  (  pathPrefix  +  OUT_FILE_EXTENSION  )  ; 
if  (  reuse  ==  false  )  { 
if  (  inFile  .  exists  (  )  )  { 
inFile  .  delete  (  )  ; 
} 
if  (  outFile  .  exists  (  )  )  { 
outFile  .  delete  (  )  ; 
} 
} 
} 







public   InputStream   getHeadStream  (  )  throws   IOException  { 
if  (  headStream  ==  null  )  { 
headStream  =  new   FlipFileInputStream  (  rememberedPosition  )  ; 
} 
return   headStream  ; 
} 







public   OutputStream   getTailStream  (  )  throws   FileNotFoundException  { 
if  (  tailStream  ==  null  )  { 
tailStream  =  new   FlipFileOutputStream  (  )  ; 
} 
return   tailStream  ; 
} 






void   flip  (  )  throws   IOException  { 
inFile  .  delete  (  )  ; 
tailStream  .  flush  (  )  ; 
tailStream  .  close  (  )  ; 
outFile  .  renameTo  (  inFile  )  ; 
tailStream  .  setupStreams  (  )  ; 
} 









public   InputStream   getReadAllInputStream  (  )  throws   IOException  { 
tailStream  .  flush  (  )  ; 
BufferedInputStream   inStream1  ; 
if  (  inFile  ==  null  )  { 
inStream1  =  null  ; 
}  else  { 
FileInputStream   tmpFileStream1  =  new   FileInputStream  (  inFile  )  ; 
tmpFileStream1  .  getChannel  (  )  .  position  (  headStream  .  getReadPosition  (  )  )  ; 
inStream1  =  new   BufferedInputStream  (  tmpFileStream1  ,  4096  )  ; 
} 
tailStream  .  flush  (  )  ; 
FileInputStream   tmpFileStream2  =  new   FileInputStream  (  outFile  )  ; 
BufferedInputStream   inStream2  =  new   BufferedInputStream  (  tmpFileStream2  ,  4096  )  ; 
ByteArrayOutputStream   baOutStream  =  new   ByteArrayOutputStream  (  )  ; 
new   ObjectOutputStream  (  baOutStream  )  ; 
ByteArrayInputStream   baInStream  =  new   ByteArrayInputStream  (  baOutStream  .  toByteArray  (  )  )  ; 
return   new   SequenceInputStream  (  (  inStream1  ==  null  ?  (  InputStream  )  baInStream  :  (  InputStream  )  new   SequenceInputStream  (  baInStream  ,  inStream1  )  )  ,  inStream2  )  ; 
} 




public   void   close  (  )  throws   IOException  { 
if  (  headStream  !=  null  )  { 
rememberedPosition  =  headStream  .  position  ; 
headStream  .  close  (  )  ; 
headStream  =  null  ; 
} 
if  (  tailStream  !=  null  )  { 
tailStream  .  close  (  )  ; 
tailStream  =  null  ; 
} 
} 





public   void   discard  (  )  throws   IOException  { 
close  (  )  ; 
if  (  !  inFile  .  delete  (  )  )  { 
throw   new   IOException  (  "unable to delete "  +  inFile  )  ; 
} 
if  (  !  outFile  .  delete  (  )  )  { 
throw   new   IOException  (  "unable to delete "  +  outFile  )  ; 
} 
} 




public   void   disconnect  (  )  throws   IOException  { 
close  (  )  ; 
} 




public   void   connect  (  )  throws   IOException  { 
} 

private   void   writeObject  (  ObjectOutputStream   stream  )  throws   IOException  { 
tailStream  .  flush  (  )  ; 
rememberedPosition  =  headStream  .  getReadPosition  (  )  ; 
stream  .  defaultWriteObject  (  )  ; 
ObjectPlusFilesOutputStream   coostream  =  (  ObjectPlusFilesOutputStream  )  stream  ; 
coostream  .  snapshotAppendOnlyFile  (  outFile  )  ; 
coostream  .  snapshotAppendOnlyFile  (  inFile  )  ; 
} 

private   void   readObject  (  ObjectInputStream   stream  )  throws   IOException  ,  ClassNotFoundException  { 
stream  .  defaultReadObject  (  )  ; 
ObjectPlusFilesInputStream   coistream  =  (  ObjectPlusFilesInputStream  )  stream  ; 
coistream  .  restoreFile  (  outFile  )  ; 
coistream  .  restoreFile  (  inFile  )  ; 
} 







class   FlipFileOutputStream   extends   OutputStream  { 

BufferedOutputStream   outStream  ; 

FileOutputStream   fileStream  ; 





public   FlipFileOutputStream  (  )  throws   FileNotFoundException  { 
setupStreams  (  )  ; 
} 

protected   void   setupStreams  (  )  throws   FileNotFoundException  { 
fileStream  =  new   FileOutputStream  (  outFile  ,  true  )  ; 
outStream  =  new   BufferedOutputStream  (  fileStream  ,  4096  )  ; 
} 




public   void   write  (  int   b  )  throws   IOException  { 
outStream  .  write  (  b  )  ; 
} 




public   void   write  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
outStream  .  write  (  b  ,  off  ,  len  )  ; 
} 




public   void   write  (  byte  [  ]  b  )  throws   IOException  { 
outStream  .  write  (  b  )  ; 
} 




public   void   close  (  )  throws   IOException  { 
super  .  close  (  )  ; 
outStream  .  close  (  )  ; 
} 




public   void   flush  (  )  throws   IOException  { 
outStream  .  flush  (  )  ; 
} 
} 








public   class   FlipFileInputStream   extends   InputStream  { 

FileInputStream   fileStream  ; 

InputStream   inStream  ; 

long   position  ; 






public   FlipFileInputStream  (  long   readPosition  )  throws   IOException  { 
setupStreams  (  readPosition  )  ; 
} 




public   int   read  (  )  throws   IOException  { 
int   c  ; 
if  (  inStream  ==  null  ||  (  c  =  inStream  .  read  (  )  )  ==  -  1  )  { 
getNewInStream  (  )  ; 
if  (  (  c  =  inStream  .  read  (  )  )  ==  -  1  )  { 
return  -  1  ; 
} 
} 
if  (  c  !=  -  1  )  { 
position  ++  ; 
} 
return   c  ; 
} 




public   int   read  (  byte  [  ]  b  )  throws   IOException  { 
return   read  (  b  ,  0  ,  b  .  length  )  ; 
} 




public   int   read  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
int   count  ; 
if  (  inStream  ==  null  ||  (  count  =  inStream  .  read  (  b  ,  off  ,  len  )  )  ==  -  1  )  { 
getNewInStream  (  )  ; 
if  (  (  count  =  inStream  .  read  (  b  ,  off  ,  len  )  )  ==  -  1  )  { 
return  -  1  ; 
} 
} 
if  (  count  !=  -  1  )  { 
position  +=  count  ; 
} 
return   count  ; 
} 







private   void   getNewInStream  (  )  throws   FileNotFoundException  ,  IOException  { 
if  (  inStream  !=  null  )  { 
inStream  .  close  (  )  ; 
} 
DiskByteQueue  .  this  .  flip  (  )  ; 
setupStreams  (  0  )  ; 
} 

private   void   setupStreams  (  long   readPosition  )  throws   IOException  { 
inFile  .  createNewFile  (  )  ; 
fileStream  =  new   FileInputStream  (  inFile  )  ; 
inStream  =  new   BufferedInputStream  (  fileStream  ,  4096  )  ; 
inStream  .  skip  (  readPosition  )  ; 
position  =  readPosition  ; 
} 




public   void   close  (  )  throws   IOException  { 
super  .  close  (  )  ; 
if  (  inStream  !=  null  )  { 
inStream  .  close  (  )  ; 
} 
} 






public   long   getReadPosition  (  )  { 
return   position  ; 
} 
} 
} 

