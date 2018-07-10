package   org  .  firebirdsql  .  jdbc  ; 

import   java  .  io  .  *  ; 
import   java  .  sql  .  *  ; 
import   java  .  util  .  *  ; 
import   org  .  firebirdsql  .  gds  .  *  ; 
import   org  .  firebirdsql  .  gds  .  impl  .  GDSHelper  ; 



























public   class   FBBlob   implements   FirebirdBlob  ,  Synchronizable  { 

public   static   final   boolean   SEGMENTED  =  true  ; 

public   static   final   int   READ_FULLY_BUFFER_SIZE  =  16  *  1024  ; 






int   bufferlength  ; 

boolean   isNew  ; 

long   blob_id  ; 

GDSHelper   gdsHelper  ; 

private   FBObjectListener  .  BlobListener   blobListener  ; 

Collection   inputStreams  =  new   HashSet  (  )  ; 

private   FBBlobOutputStream   blobOut  =  null  ; 

private   FBBlob  (  GDSHelper   c  ,  boolean   isNew  ,  FBObjectListener  .  BlobListener   blobListener  )  { 
this  .  gdsHelper  =  c  ; 
this  .  isNew  =  isNew  ; 
this  .  bufferlength  =  c  .  getBlobBufferLength  (  )  ; 
this  .  blobListener  =  blobListener  ; 
} 







public   FBBlob  (  GDSHelper   c  ,  FBObjectListener  .  BlobListener   blobListener  )  { 
this  (  c  ,  true  ,  blobListener  )  ; 
} 

public   FBBlob  (  GDSHelper   c  )  { 
this  (  c  ,  null  )  ; 
} 








public   FBBlob  (  GDSHelper   c  ,  long   blob_id  ,  FBObjectListener  .  BlobListener   blobListener  )  { 
this  (  c  ,  false  ,  blobListener  )  ; 
this  .  blob_id  =  blob_id  ; 
} 

public   FBBlob  (  GDSHelper   c  ,  long   blob_id  )  { 
this  (  c  ,  blob_id  ,  null  )  ; 
} 







public   Object   getSynchronizationObject  (  )  { 
return   gdsHelper  ; 
} 







public   void   close  (  )  throws   IOException  { 
Object   syncObject  =  getSynchronizationObject  (  )  ; 
synchronized  (  syncObject  )  { 
IOException   error  =  null  ; 
Iterator   i  =  inputStreams  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
try  { 
(  (  FBBlobInputStream  )  i  .  next  (  )  )  .  close  (  )  ; 
}  catch  (  IOException   ex  )  { 
error  =  ex  ; 
} 
} 
inputStreams  .  clear  (  )  ; 
if  (  error  !=  null  )  throw   error  ; 
} 
} 


















public   void   free  (  )  throws   SQLException  { 
try  { 
close  (  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   FBSQLException  (  ex  )  ; 
} 
} 

















public   InputStream   getBinaryStream  (  long   pos  ,  long   length  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 













public   byte  [  ]  getInfo  (  byte  [  ]  items  ,  int   buffer_length  )  throws   SQLException  { 
Object   syncObject  =  getSynchronizationObject  (  )  ; 
synchronized  (  syncObject  )  { 
try  { 
if  (  blobListener  !=  null  )  blobListener  .  executionStarted  (  this  )  ; 
IscBlobHandle   blob  =  gdsHelper  .  openBlob  (  blob_id  ,  SEGMENTED  )  ; 
try  { 
return   gdsHelper  .  getBlobInfo  (  blob  ,  items  ,  buffer_length  )  ; 
}  finally  { 
gdsHelper  .  closeBlob  (  blob  )  ; 
} 
}  catch  (  GDSException   ex  )  { 
throw   new   FBSQLException  (  ex  )  ; 
}  finally  { 
if  (  blobListener  !=  null  )  blobListener  .  executionCompleted  (  this  )  ; 
} 
} 
} 

public   static   final   byte  [  ]  BLOB_LENGTH_REQUEST  =  new   byte  [  ]  {  ISCConstants  .  isc_info_blob_total_length  }  ; 










public   long   length  (  )  throws   SQLException  { 
byte  [  ]  info  =  getInfo  (  BLOB_LENGTH_REQUEST  ,  20  )  ; 
return   interpretLength  (  info  ,  0  )  ; 
} 











public   static   long   interpretLength  (  GDSHelper   gdsHelper  ,  byte  [  ]  info  ,  int   position  )  throws   SQLException  { 
if  (  info  [  position  ]  !=  ISCConstants  .  isc_info_blob_total_length  )  throw   new   FBSQLException  (  "Length is not available."  )  ; 
int   dataLength  =  gdsHelper  .  iscVaxInteger  (  info  ,  position  +  1  ,  2  )  ; 
return   gdsHelper  .  iscVaxInteger  (  info  ,  position  +  3  ,  dataLength  )  ; 
} 











long   interpretLength  (  byte  [  ]  info  ,  int   position  )  throws   SQLException  { 
return   interpretLength  (  gdsHelper  ,  info  ,  position  )  ; 
} 









public   boolean   isSegmented  (  )  throws   SQLException  { 
byte  [  ]  info  =  getInfo  (  new   byte  [  ]  {  ISCConstants  .  isc_info_blob_type  }  ,  20  )  ; 
if  (  info  [  0  ]  !=  ISCConstants  .  isc_info_blob_type  )  throw   new   FBSQLException  (  "Cannot determine BLOB type"  )  ; 
int   dataLength  =  gdsHelper  .  iscVaxInteger  (  info  ,  1  ,  2  )  ; 
int   type  =  gdsHelper  .  iscVaxInteger  (  info  ,  3  ,  dataLength  )  ; 
return   type  ==  ISCConstants  .  isc_bpb_type_segmented  ; 
} 

















public   FirebirdBlob   detach  (  )  throws   SQLException  { 
return   new   FBBlob  (  gdsHelper  ,  blob_id  ,  blobListener  )  ; 
} 



















public   byte  [  ]  getBytes  (  long   pos  ,  int   length  )  throws   SQLException  { 
if  (  pos  <  1  )  throw   new   FBSQLException  (  "Blob position should be >= 1"  )  ; 
if  (  pos  >  Integer  .  MAX_VALUE  )  throw   new   FBSQLException  (  "Blob position is limited to 2^31 - 1 "  +  "due to isc_seek_blob limitations."  ,  FBSQLException  .  SQL_STATE_INVALID_ARG_VALUE  )  ; 
Object   syncObject  =  getSynchronizationObject  (  )  ; 
synchronized  (  syncObject  )  { 
if  (  blobListener  !=  null  )  blobListener  .  executionStarted  (  this  )  ; 
try  { 
FirebirdBlob  .  BlobInputStream   in  =  (  FirebirdBlob  .  BlobInputStream  )  getBinaryStream  (  )  ; 
try  { 
byte  [  ]  result  =  new   byte  [  length  ]  ; 
if  (  pos  !=  1  )  in  .  seek  (  (  int  )  pos  -  1  )  ; 
in  .  readFully  (  result  )  ; 
return   result  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
throw   new   FBSQLException  (  ex  )  ; 
}  finally  { 
if  (  blobListener  !=  null  )  blobListener  .  executionCompleted  (  this  )  ; 
} 
} 
} 










public   InputStream   getBinaryStream  (  )  throws   SQLException  { 
Object   syncObject  =  getSynchronizationObject  (  )  ; 
synchronized  (  syncObject  )  { 
FBBlobInputStream   blobstream  =  new   FBBlobInputStream  (  this  )  ; 
inputStreams  .  add  (  blobstream  )  ; 
return   blobstream  ; 
} 
} 
















public   long   position  (  byte   pattern  [  ]  ,  long   start  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 
















public   long   position  (  Blob   pattern  ,  long   start  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 








public   void   truncate  (  long   param1  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 












public   int   setBytes  (  long   param1  ,  byte  [  ]  param2  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 
















public   int   setBytes  (  long   param1  ,  byte  [  ]  param2  ,  int   param3  ,  int   param4  )  throws   SQLException  { 
throw   new   FBDriverNotCapableException  (  )  ; 
} 









public   OutputStream   setBinaryStream  (  long   pos  )  throws   SQLException  { 
if  (  blobListener  !=  null  )  blobListener  .  executionStarted  (  this  )  ; 
if  (  blobOut  !=  null  )  throw   new   FBSQLException  (  "Only one blob output stream open at a time!"  )  ; 
if  (  pos  <  1  )  throw   new   FBSQLException  (  "You can't start before the beginning of the blob"  ,  FBSQLException  .  SQL_STATE_INVALID_ARG_VALUE  )  ; 
if  (  (  isNew  )  &&  (  pos  >  1  )  )  throw   new   FBSQLException  (  "Previous value was null, you must start at position 1"  ,  FBSQLException  .  SQL_STATE_INVALID_ARG_VALUE  )  ; 
blobOut  =  new   FBBlobOutputStream  (  this  )  ; 
if  (  pos  >  1  )  { 
throw   new   FBDriverNotCapableException  (  "Offset start positions are not yet supported."  )  ; 
} 
return   blobOut  ; 
} 







public   long   getBlobId  (  )  throws   SQLException  { 
if  (  isNew  )  throw   new   FBSQLException  (  "No Blob ID is available in new Blob object."  )  ; 
return   blob_id  ; 
} 

void   setBlobId  (  long   blob_id  )  { 
this  .  blob_id  =  blob_id  ; 
this  .  isNew  =  false  ; 
} 

public   void   copyBytes  (  byte  [  ]  bytes  ,  int   pos  ,  int   len  )  throws   SQLException  { 
OutputStream   out  =  setBinaryStream  (  1  )  ; 
try  { 
try  { 
out  .  write  (  bytes  ,  pos  ,  len  )  ; 
}  finally  { 
out  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
throw   new   FBSQLException  (  ex  )  ; 
} 
} 








public   void   copyStream  (  InputStream   inputStream  ,  int   length  )  throws   SQLException  { 
OutputStream   os  =  setBinaryStream  (  1  )  ; 
byte  [  ]  buffer  =  new   byte  [  Math  .  min  (  bufferlength  ,  length  )  ]  ; 
int   chunk  ; 
try  { 
while  (  length  >  0  )  { 
chunk  =  inputStream  .  read  (  buffer  ,  0  ,  (  (  length  <  bufferlength  )  ?  length  :  bufferlength  )  )  ; 
if  (  chunk  ==  -  1  )  break  ; 
os  .  write  (  buffer  ,  0  ,  chunk  )  ; 
length  -=  chunk  ; 
} 
os  .  flush  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   FBSQLException  (  ioe  )  ; 
} 
} 









public   void   copyStream  (  InputStream   inputStream  )  throws   SQLException  { 
OutputStream   os  =  setBinaryStream  (  1  )  ; 
try  { 
int   chunk  =  0  ; 
byte  [  ]  buffer  =  new   byte  [  bufferlength  ]  ; 
while  (  (  chunk  =  inputStream  .  read  (  buffer  )  )  !=  -  1  )  os  .  write  (  buffer  ,  0  ,  chunk  )  ; 
os  .  flush  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   FBSQLException  (  ioe  )  ; 
} 
} 








public   void   copyCharacterStream  (  Reader   inputStream  ,  int   length  ,  String   encoding  )  throws   SQLException  { 
OutputStream   os  =  setBinaryStream  (  1  )  ; 
try  { 
OutputStreamWriter   osw  ; 
if  (  encoding  !=  null  )  osw  =  new   OutputStreamWriter  (  os  ,  encoding  )  ;  else   osw  =  new   OutputStreamWriter  (  os  )  ; 
char  [  ]  buffer  =  new   char  [  Math  .  min  (  bufferlength  ,  length  )  ]  ; 
int   chunk  ; 
try  { 
while  (  length  >  0  )  { 
chunk  =  inputStream  .  read  (  buffer  ,  0  ,  (  (  length  <  bufferlength  )  ?  length  :  bufferlength  )  )  ; 
if  (  chunk  ==  -  1  )  break  ; 
osw  .  write  (  buffer  ,  0  ,  chunk  )  ; 
length  -=  chunk  ; 
} 
osw  .  flush  (  )  ; 
os  .  flush  (  )  ; 
os  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   FBSQLException  (  ioe  )  ; 
} 
}  catch  (  UnsupportedEncodingException   ex  )  { 
throw   new   FBSQLException  (  "Cannot set character stream because "  +  "the unsupported encoding is detected in the JVM: "  +  encoding  +  ". Please report this to the driver developers."  )  ; 
} 
} 
} 

