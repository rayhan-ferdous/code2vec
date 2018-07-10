package   com  .  iver  .  cit  .  gvsig  .  fmap  .  drivers  .  shp  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  nio  .  CharBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  charset  .  Charset  ; 
import   java  .  nio  .  charset  .  CharsetDecoder  ; 
import   java  .  nio  .  charset  .  UnsupportedCharsetException  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   com  .  iver  .  cit  .  gvsig  .  fmap  .  drivers  .  dbf  .  DbfEncodings  ; 
import   com  .  iver  .  utiles  .  bigfile  .  BigByteBuffer2  ; 





public   class   DbaseFileNIO  { 

private   static   Charset   defaultCharset  =  Charset  .  defaultCharset  (  )  ; 

private   DbaseFileHeaderNIO   myHeader  ; 

private   FileInputStream   fin  ; 

private   FileChannel   channel  ; 

private   BigByteBuffer2   buffer  ; 

CharBuffer   charBuffer  ; 

private   Charset   charSet  ; 






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










public   String   getStringFieldValue  (  int   rowIndex  ,  int   fieldId  )  throws   UnsupportedEncodingException  { 
int   recordOffset  =  (  myHeader  .  getRecordLength  (  )  *  rowIndex  )  +  myHeader  .  getHeaderLength  (  )  +  1  ; 
int   fieldOffset  =  0  ; 
for  (  int   i  =  0  ;  i  <  fieldId  ;  i  ++  )  { 
fieldOffset  +=  myHeader  .  getFieldLength  (  i  )  ; 
} 
buffer  .  position  (  recordOffset  +  fieldOffset  )  ; 
byte  [  ]  data  =  new   byte  [  myHeader  .  getFieldLength  (  fieldId  )  ]  ; 
buffer  .  get  (  data  )  ; 
return   new   String  (  data  ,  charSet  .  name  (  )  )  ; 
} 









public   Number   getNumberFieldValue  (  int   rowIndex  ,  int   fieldId  )  { 
int   recordOffset  =  (  myHeader  .  getRecordLength  (  )  *  rowIndex  )  +  myHeader  .  getHeaderLength  (  )  +  1  ; 
int   fieldOffset  =  0  ; 
for  (  int   i  =  0  ;  i  <  fieldId  ;  i  ++  )  { 
fieldOffset  +=  myHeader  .  getFieldLength  (  i  )  ; 
} 
buffer  .  position  (  recordOffset  +  fieldOffset  )  ; 
byte  [  ]  data  =  new   byte  [  myHeader  .  getFieldLength  (  fieldId  )  ]  ; 
buffer  .  get  (  data  )  ; 
String   s  =  new   String  (  data  )  ; 
s  =  s  .  trim  (  )  ; 
if  (  getFieldType  (  fieldId  )  ==  'N'  )  { 
Object   tempObject  =  Double  .  valueOf  (  s  )  ; 
return   new   Double  (  tempObject  .  toString  (  )  )  ; 
}  else  { 
Object   tempObject  =  Integer  .  valueOf  (  s  )  ; 
return   new   Integer  (  tempObject  .  toString  (  )  )  ; 
} 
} 








public   String   getFieldName  (  int   inIndex  )  { 
byte  [  ]  bytes  =  myHeader  .  getFieldName  (  inIndex  )  .  trim  (  )  .  getBytes  (  )  ; 
String   result  ; 
try  { 
result  =  new   String  (  bytes  ,  charSet  .  name  (  )  )  ; 
return   result  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
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
fin  =  new   FileInputStream  (  file  )  ; 
channel  =  fin  .  getChannel  (  )  ; 
buffer  =  new   BigByteBuffer2  (  channel  ,  FileChannel  .  MapMode  .  READ_ONLY  )  ; 
myHeader  =  new   DbaseFileHeaderNIO  (  )  ; 
myHeader  .  readHeader  (  buffer  )  ; 
charBuffer  =  CharBuffer  .  allocate  (  myHeader  .  getRecordLength  (  )  -  1  )  ; 
String   charSetName  =  DbfEncodings  .  getInstance  (  )  .  getCharsetForDbfId  (  myHeader  .  getLanguageID  (  )  )  ; 
if  (  charSetName  ==  null  )  { 
charSet  =  defaultCharset  ; 
}  else  { 
if  (  charSetName  .  equalsIgnoreCase  (  "UNKNOWN"  )  )  charSet  =  defaultCharset  ;  else  { 
try  { 
charSet  =  Charset  .  forName  (  charSetName  )  ; 
}  catch  (  UnsupportedCharsetException   e  )  { 
charSet  =  defaultCharset  ; 
} 
} 
} 
} 






public   void   close  (  )  throws   IOException  { 
fin  .  close  (  )  ; 
channel  .  close  (  )  ; 
} 

public   static   Charset   getDefaultCharset  (  )  { 
return   defaultCharset  ; 
} 

public   static   void   setDefaultCharset  (  Charset   defaultCharset  )  { 
DbaseFileNIO  .  defaultCharset  =  defaultCharset  ; 
} 

public   void   setCharSet  (  Charset   charSet  )  { 
this  .  charSet  =  charSet  ; 
} 

public   Charset   getCharSet  (  )  { 
return   charSet  ; 
} 




public   DbaseFileHeaderNIO   getDBaseHeader  (  )  { 
return   myHeader  ; 
} 
} 

