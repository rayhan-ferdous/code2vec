package   org  .  processmining  .  framework  .  log  .  rfb  .  io  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  ArrayList  ; 
















public   class   ManagedRandomAccessFile   implements   RandomAccessStorage  { 

static  { 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   ShutdownHook  (  )  )  ; 
System  .  out  .  println  (  "shutdown hook installed"  )  ; 
} 




protected   static   final   String   TEMP_FILE_PREFIX  =  "PROM_ATERND"  ; 




protected   static   final   String   TEMP_FILE_SUFFIX  =  ".atedb"  ; 





protected   static   int   openFilesCounter  =  0  ; 





protected   static   ArrayList  <  ManagedRandomAccessFile  >  openFilesList  =  new   ArrayList  <  ManagedRandomAccessFile  >  (  )  ; 





protected   static   int   maxOpenFiles  =  250  ; 








protected   static   synchronized   void   retrieveOpenFileSlot  (  ManagedRandomAccessFile   customer  )  throws   IOException  { 
if  (  openFilesCounter  >=  maxOpenFiles  )  { 
ManagedRandomAccessFile   victim  =  (  ManagedRandomAccessFile  )  openFilesList  .  remove  (  0  )  ; 
victim  .  closeHandle  (  )  ; 
openFilesCounter  --  ; 
} 
openFilesList  .  add  (  customer  )  ; 
openFilesCounter  ++  ; 
} 







protected   static   synchronized   void   releaseOpenFileSlot  (  ManagedRandomAccessFile   customer  )  throws   IOException  { 
if  (  openFilesList  .  remove  (  customer  )  ==  true  )  { 
openFilesCounter  --  ; 
} 
} 

protected   File   file  =  null  ; 

protected   RandomAccessFile   raf  =  null  ; 

protected   long   currentFilePointer  =  0  ; 

protected   boolean   isOpen  =  false  ; 






public   ManagedRandomAccessFile  (  )  throws   IOException  { 
file  =  ManagedRandomAccessFile  .  createTempFile  (  )  ; 
raf  =  null  ; 
currentFilePointer  =  0  ; 
isOpen  =  false  ; 
} 











public   ManagedRandomAccessFile  (  ManagedRandomAccessFile   template  )  throws   IOException  { 
file  =  ManagedRandomAccessFile  .  createTempFile  (  )  ; 
raf  =  null  ; 
currentFilePointer  =  template  .  currentFilePointer  ; 
isOpen  =  false  ; 
copyFile  (  template  .  file  ,  file  )  ; 
} 










public   synchronized   boolean   delete  (  )  throws   IOException  { 
close  (  )  ; 
return   file  .  delete  (  )  ; 
} 











public   synchronized   void   deleteOnExit  (  )  throws   IOException  { 
close  (  )  ; 
file  .  deleteOnExit  (  )  ; 
} 





public   synchronized   void   close  (  )  throws   IOException  { 
if  (  isOpen  )  { 
raf  (  )  .  close  (  )  ; 
ManagedRandomAccessFile  .  releaseOpenFileSlot  (  this  )  ; 
isOpen  =  false  ; 
} 
} 






public   synchronized   long   getFilePointer  (  )  { 
return   currentFilePointer  ; 
} 






public   synchronized   long   length  (  )  throws   IOException  { 
return   raf  (  )  .  length  (  )  ; 
} 












public   synchronized   void   seek  (  long   position  )  { 
currentFilePointer  =  position  ; 
} 










protected   synchronized   void   closeHandle  (  )  throws   IOException  { 
raf  .  close  (  )  ; 
raf  =  null  ; 
isOpen  =  false  ; 
} 





protected   synchronized   void   reOpen  (  )  throws   IOException  { 
ManagedRandomAccessFile  .  retrieveOpenFileSlot  (  this  )  ; 
isOpen  =  true  ; 
raf  =  new   RandomAccessFile  (  file  ,  "rw"  )  ; 
} 











protected   synchronized   RandomAccessFile   raf  (  )  throws   IOException  { 
if  (  isOpen  ==  false  )  { 
reOpen  (  )  ; 
raf  .  seek  (  currentFilePointer  )  ; 
}  else  { 
if  (  raf  .  getFilePointer  (  )  !=  currentFilePointer  )  { 
raf  .  seek  (  currentFilePointer  )  ; 
} 
} 
return   raf  ; 
} 







public   synchronized   void   write  (  int   val  )  throws   IOException  { 
raf  (  )  .  write  (  val  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   write  (  byte  [  ]  arg  )  throws   IOException  { 
raf  (  )  .  write  (  arg  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   write  (  byte  [  ]  arg  ,  int   arg1  ,  int   arg2  )  throws   IOException  { 
raf  (  )  .  write  (  arg  ,  arg1  ,  arg2  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeBoolean  (  boolean   bool  )  throws   IOException  { 
raf  (  )  .  writeBoolean  (  bool  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeByte  (  int   arg0  )  throws   IOException  { 
raf  (  )  .  writeByte  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeShort  (  int   arg0  )  throws   IOException  { 
raf  (  )  .  writeShort  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeChar  (  int   arg0  )  throws   IOException  { 
raf  (  )  .  writeChar  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeInt  (  int   arg0  )  throws   IOException  { 
raf  (  )  .  writeInt  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeLong  (  long   arg0  )  throws   IOException  { 
raf  (  )  .  writeLong  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeFloat  (  float   arg0  )  throws   IOException  { 
raf  (  )  .  writeFloat  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeDouble  (  double   arg0  )  throws   IOException  { 
raf  (  )  .  writeDouble  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeBytes  (  String   arg0  )  throws   IOException  { 
raf  (  )  .  writeBytes  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   writeChars  (  String   arg0  )  throws   IOException  { 
raf  (  )  .  writeChars  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 








public   synchronized   void   writeUTF  (  String   arg0  )  throws   IOException  { 
byte  [  ]  content  =  arg0  .  getBytes  (  "UTF-8"  )  ; 
raf  (  )  .  writeInt  (  content  .  length  )  ; 
raf  (  )  .  write  (  content  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   readFully  (  byte  [  ]  arg0  )  throws   IOException  { 
raf  (  )  .  readFully  (  arg0  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   void   readFully  (  byte  [  ]  arg0  ,  int   arg1  ,  int   arg2  )  throws   IOException  { 
raf  (  )  .  readFully  (  arg0  ,  arg1  ,  arg2  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
} 







public   synchronized   int   skipBytes  (  int   arg0  )  throws   IOException  { 
int   skipped  =  raf  (  )  .  skipBytes  (  arg0  )  ; 
currentFilePointer  +=  skipped  ; 
return   skipped  ; 
} 







public   synchronized   boolean   readBoolean  (  )  throws   IOException  { 
boolean   result  =  raf  (  )  .  readBoolean  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   byte   readByte  (  )  throws   IOException  { 
byte   result  =  raf  (  )  .  readByte  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   int   readUnsignedByte  (  )  throws   IOException  { 
int   result  =  raf  (  )  .  readUnsignedByte  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   short   readShort  (  )  throws   IOException  { 
short   result  =  raf  (  )  .  readShort  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   int   readUnsignedShort  (  )  throws   IOException  { 
int   result  =  raf  (  )  .  readUnsignedShort  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   char   readChar  (  )  throws   IOException  { 
char   result  =  raf  (  )  .  readChar  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   int   readInt  (  )  throws   IOException  { 
int   result  =  raf  (  )  .  readInt  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   long   readLong  (  )  throws   IOException  { 
long   result  =  raf  (  )  .  readLong  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   float   readFloat  (  )  throws   IOException  { 
float   result  =  raf  (  )  .  readFloat  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   double   readDouble  (  )  throws   IOException  { 
double   result  =  raf  (  )  .  readDouble  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 







public   synchronized   String   readLine  (  )  throws   IOException  { 
String   result  =  raf  (  )  .  readLine  (  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 









public   synchronized   String   readUTF  (  )  throws   IOException  { 
byte  [  ]  content  =  new   byte  [  raf  (  )  .  readInt  (  )  ]  ; 
raf  (  )  .  readFully  (  content  )  ; 
String   result  =  new   String  (  content  ,  "UTF-8"  )  ; 
currentFilePointer  =  raf  .  getFilePointer  (  )  ; 
return   result  ; 
} 




protected   void   finalize  (  )  throws   Throwable  { 
if  (  delete  (  )  ==  false  )  { 
deleteOnExit  (  )  ; 
} 
} 














public   static   void   copyFile  (  File   source  ,  File   destination  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  source  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  destination  )  ; 
FileChannel   inCh  =  fis  .  getChannel  (  )  ; 
FileChannel   outCh  =  fos  .  getChannel  (  )  ; 
inCh  .  transferTo  (  0  ,  inCh  .  size  (  )  ,  outCh  )  ; 
inCh  .  close  (  )  ; 
fis  .  close  (  )  ; 
outCh  .  close  (  )  ; 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
} 

public   RandomAccessStorage   copy  (  )  throws   IOException  { 
return   new   ManagedRandomAccessFile  (  this  )  ; 
} 







protected   static   File   createTempFile  (  )  throws   IOException  { 
return   File  .  createTempFile  (  ManagedRandomAccessFile  .  TEMP_FILE_PREFIX  ,  ManagedRandomAccessFile  .  TEMP_FILE_SUFFIX  )  ; 
} 








protected   static   class   ShutdownHook   extends   Thread  { 




protected   String   tmpFileRegEx  =  ManagedRandomAccessFile  .  TEMP_FILE_PREFIX  +  "(.*)"  +  ManagedRandomAccessFile  .  TEMP_FILE_SUFFIX  ; 







public   void   run  (  )  { 
System  .  out  .  print  (  "ManagedRandomAccessFile.ShutdownHook invoked.. "  )  ; 
int   cleaned  =  0  ; 
File  [  ]  tmpFiles  =  (  new   File  (  System  .  getProperty  (  "java.io.tmpdir"  )  )  )  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  tmpFiles  .  length  ;  i  ++  )  { 
if  (  tmpFiles  [  i  ]  .  getName  (  )  .  matches  (  tmpFileRegEx  )  )  { 
if  (  tmpFiles  [  i  ]  .  delete  (  )  ==  false  )  { 
tmpFiles  [  i  ]  .  deleteOnExit  (  )  ; 
} 
cleaned  ++  ; 
} 
} 
System  .  out  .  print  (  "cleaned "  +  cleaned  +  " stale files."  )  ; 
} 
} 
} 

