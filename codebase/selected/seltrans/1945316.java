package   com  .  hardcode  .  gdbms  .  driver  .  dbf  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   com  .  iver  .  utiles  .  bigfile  .  BigByteBuffer  ; 





public   class   DbaseFile  { 

private   DbaseFileHeader   myHeader  ; 

private   RandomAccessFile   raf  ; 

private   FileChannel   channel  ; 

private   BigByteBuffer   buffer  ; 

private   FileChannel  .  MapMode   mode  ; 

public   int   getRecordCount  (  )  { 
return   myHeader  .  getNumRecords  (  )  ; 
} 






public   int   getFieldCount  (  )  { 
return   myHeader  .  getNumFields  (  )  ; 
} 









public   boolean   getBooleanFieldValue  (  int   rowIndex  ,  int   fieldId  )  { 
int   recordOffset  =  (  myHeader  .  getRecordLength  (  )  *  rowIndex  )  +  myHeader  .  getHeaderLength  (  )  +  1  ; 
int   fieldOffset  =  0  ; 
for  (  int   i  =  0  ;  i  <  (  fieldId  -  1  )  ;  i  ++  )  { 
fieldOffset  +=  myHeader  .  getFieldLength  (  i  )  ; 
} 
buffer  .  position  (  recordOffset  +  fieldOffset  )  ; 
char   bool  =  (  char  )  buffer  .  get  (  )  ; 
return  (  (  bool  ==  't'  )  ||  (  bool  ==  'T'  )  ||  (  bool  ==  'Y'  )  ||  (  bool  ==  'y'  )  )  ; 
} 









public   String   getStringFieldValue  (  int   rowIndex  ,  int   fieldId  )  { 
int   recordOffset  =  (  myHeader  .  getRecordLength  (  )  *  rowIndex  )  +  myHeader  .  getHeaderLength  (  )  +  1  ; 
int   fieldOffset  =  myHeader  .  getFieldDescription  (  fieldId  )  .  myFieldDataAddress  ; 
buffer  .  position  (  recordOffset  +  fieldOffset  )  ; 
byte  [  ]  data  =  new   byte  [  myHeader  .  getFieldLength  (  fieldId  )  ]  ; 
buffer  .  get  (  data  )  ; 
return   new   String  (  data  )  ; 
} 








public   String   getFieldName  (  int   inIndex  )  { 
return   myHeader  .  getFieldName  (  inIndex  )  .  trim  (  )  ; 
} 








public   char   getFieldType  (  int   inIndex  )  { 
return   myHeader  .  getFieldType  (  inIndex  )  ; 
} 








public   int   getFieldLength  (  int   inIndex  )  { 
return   myHeader  .  getFieldLength  (  inIndex  )  ; 
} 








public   int   getFieldDecimalLength  (  int   inIndex  )  { 
return   myHeader  .  getFieldDecimalCount  (  inIndex  )  ; 
} 








public   void   open  (  File   file  )  throws   IOException  { 
if  (  file  .  canWrite  (  )  )  { 
try  { 
raf  =  new   RandomAccessFile  (  file  ,  "rw"  )  ; 
mode  =  FileChannel  .  MapMode  .  READ_WRITE  ; 
}  catch  (  FileNotFoundException   e  )  { 
raf  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
mode  =  FileChannel  .  MapMode  .  READ_ONLY  ; 
} 
}  else  { 
raf  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
mode  =  FileChannel  .  MapMode  .  READ_ONLY  ; 
} 
channel  =  raf  .  getChannel  (  )  ; 
buffer  =  new   BigByteBuffer  (  channel  ,  mode  )  ; 
myHeader  =  new   DbaseFileHeader  (  )  ; 
myHeader  .  readHeader  (  buffer  )  ; 
} 






public   void   close  (  )  throws   IOException  { 
raf  .  close  (  )  ; 
channel  .  close  (  )  ; 
buffer  =  null  ; 
} 

public   FileChannel   getWriteChannel  (  )  { 
return   channel  ; 
} 
} 

