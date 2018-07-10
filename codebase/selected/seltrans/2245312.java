package   com  .  sun  .  speech  .  freetts  .  clunits  ; 

import   com  .  sun  .  speech  .  freetts  .  cart  .  CART  ; 
import   com  .  sun  .  speech  .  freetts  .  cart  .  CARTImpl  ; 
import   com  .  sun  .  speech  .  freetts  .  util  .  Utilities  ; 
import   com  .  sun  .  speech  .  freetts  .  util  .  BulkTimer  ; 
import   com  .  sun  .  speech  .  freetts  .  relp  .  SampleSet  ; 
import   com  .  sun  .  speech  .  freetts  .  relp  .  Sample  ; 
import   com  .  sun  .  speech  .  freetts  .  relp  .  SampleInfo  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  MappedByteBuffer  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  NoSuchElementException  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Iterator  ; 












public   class   ClusterUnitDatabase  { 

static   final   int   CLUNIT_NONE  =  65535  ; 

private   DatabaseClusterUnit  [  ]  units  ; 

private   UnitType  [  ]  unitTypes  ; 

private   SampleSet   sts  ; 

private   SampleSet   mcep  ; 

private   int   continuityWeight  ; 

private   int   optimalCoupling  ; 

private   int   extendSelections  ; 

private   int   joinMethod  ; 

private   int  [  ]  joinWeights  ; 

private   int   joinWeightShift  ; 

private   Map   cartMap  =  new   HashMap  (  )  ; 

private   CART   defaultCart  =  null  ; 

private   transient   List   unitList  ; 

private   transient   int   lineCount  ; 

private   transient   List   unitTypesList  ; 

private   static   final   int   MAGIC  =  0xf0cacc1a  ; 

private   static   final   int   VERSION  =  0x1000  ; 









ClusterUnitDatabase  (  URL   url  ,  boolean   isBinary  )  throws   IOException  { 
BulkTimer  .  LOAD  .  start  (  "ClusterUnitDatabase"  )  ; 
InputStream   is  =  Utilities  .  getInputStream  (  url  )  ; 
if  (  isBinary  )  { 
loadBinary  (  is  )  ; 
}  else  { 
loadText  (  is  )  ; 
} 
is  .  close  (  )  ; 
BulkTimer  .  LOAD  .  stop  (  "ClusterUnitDatabase"  )  ; 
} 









int   getStart  (  int   unitEntry  )  { 
return   units  [  unitEntry  ]  .  start  ; 
} 









int   getEnd  (  int   unitEntry  )  { 
return   units  [  unitEntry  ]  .  end  ; 
} 








int   getPhone  (  int   unitEntry  )  { 
return   units  [  unitEntry  ]  .  phone  ; 
} 








CART   getTree  (  String   unitType  )  { 
CART   cart  =  (  CART  )  cartMap  .  get  (  unitType  )  ; 
if  (  cart  ==  null  )  { 
System  .  err  .  println  (  "ClusterUnitDatabase: can't find tree for "  +  unitType  )  ; 
return   defaultCart  ; 
} 
return   cart  ; 
} 

int   getUnitTypeIndex  (  String   name  )  { 
int   start  ,  end  ,  mid  ,  c  ; 
start  =  0  ; 
end  =  unitTypes  .  length  ; 
while  (  start  <  end  )  { 
mid  =  (  start  +  end  )  /  2  ; 
c  =  unitTypes  [  mid  ]  .  getName  (  )  .  compareTo  (  name  )  ; 
if  (  c  ==  0  )  { 
return   mid  ; 
}  else   if  (  c  >  0  )  { 
end  =  mid  ; 
}  else  { 
start  =  mid  +  1  ; 
} 
} 
return  -  1  ; 
} 









int   getUnitIndex  (  String   unitType  ,  int   instance  )  { 
int   i  =  getUnitTypeIndex  (  unitType  )  ; 
if  (  i  ==  -  1  )  { 
error  (  "getUnitIndex: can't find unit type "  +  unitType  )  ; 
i  =  0  ; 
} 
if  (  instance  >=  unitTypes  [  i  ]  .  getCount  (  )  )  { 
error  (  "getUnitIndex: can't find instance "  +  instance  +  " of "  +  unitType  )  ; 
instance  =  0  ; 
} 
return   unitTypes  [  i  ]  .  getStart  (  )  +  instance  ; 
} 








int   getUnitIndexName  (  String   name  )  { 
int   lastIndex  =  name  .  lastIndexOf  (  '_'  )  ; 
if  (  lastIndex  ==  -  1  )  { 
error  (  "getUnitIndexName: bad unit name "  +  name  )  ; 
return  -  1  ; 
} 
int   index  =  Integer  .  parseInt  (  name  .  substring  (  lastIndex  +  1  )  )  ; 
String   type  =  name  .  substring  (  0  ,  lastIndex  )  ; 
return   getUnitIndex  (  type  ,  index  )  ; 
} 






int   getExtendSelections  (  )  { 
return   extendSelections  ; 
} 






int   getNextUnit  (  int   which  )  { 
return   units  [  which  ]  .  next  ; 
} 








int   getPrevUnit  (  int   which  )  { 
return   units  [  which  ]  .  prev  ; 
} 










boolean   isUnitTypeEqual  (  int   unitA  ,  int   unitB  )  { 
return   units  [  unitA  ]  .  type  ==  units  [  unitB  ]  .  type  ; 
} 






int   getOptimalCoupling  (  )  { 
return   optimalCoupling  ; 
} 







int   getContinuityWeight  (  )  { 
return   continuityWeight  ; 
} 






int  [  ]  getJoinWeights  (  )  { 
return   joinWeights  ; 
} 








DatabaseClusterUnit   getUnit  (  String   unitName  )  { 
return   null  ; 
} 








DatabaseClusterUnit   getUnit  (  int   which  )  { 
return   units  [  which  ]  ; 
} 






String   getName  (  )  { 
return  "ClusterUnitDatabase"  ; 
} 






SampleInfo   getSampleInfo  (  )  { 
return   sts  .  getSampleInfo  (  )  ; 
} 






SampleSet   getSts  (  )  { 
return   sts  ; 
} 






SampleSet   getMcep  (  )  { 
return   mcep  ; 
} 








int   getJoinWeightShift  (  )  { 
return   joinWeightShift  ; 
} 








private   int   calcJoinWeightShift  (  int  [  ]  joinWeights  )  { 
int   first  =  joinWeights  [  0  ]  ; 
for  (  int   i  =  1  ;  i  <  joinWeights  .  length  ;  i  ++  )  { 
if  (  joinWeights  [  i  ]  !=  first  )  { 
return   0  ; 
} 
} 
int   divisor  =  65536  /  first  ; 
if  (  divisor  ==  2  )  { 
return   1  ; 
}  else   if  (  divisor  ==  4  )  { 
return   2  ; 
} 
return   0  ; 
} 






private   void   loadText  (  InputStream   is  )  { 
BufferedReader   reader  ; 
String   line  ; 
unitList  =  new   ArrayList  (  )  ; 
unitTypesList  =  new   ArrayList  (  )  ; 
if  (  is  ==  null  )  { 
throw   new   Error  (  "Can't load cluster db file."  )  ; 
} 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  is  )  )  ; 
try  { 
line  =  reader  .  readLine  (  )  ; 
lineCount  ++  ; 
while  (  line  !=  null  )  { 
if  (  !  line  .  startsWith  (  "***"  )  )  { 
parseAndAdd  (  line  ,  reader  )  ; 
} 
line  =  reader  .  readLine  (  )  ; 
} 
reader  .  close  (  )  ; 
units  =  new   DatabaseClusterUnit  [  unitList  .  size  (  )  ]  ; 
units  =  (  DatabaseClusterUnit  [  ]  )  unitList  .  toArray  (  units  )  ; 
unitList  =  null  ; 
unitTypes  =  new   UnitType  [  unitTypesList  .  size  (  )  ]  ; 
unitTypes  =  (  UnitType  [  ]  )  unitTypesList  .  toArray  (  unitTypes  )  ; 
unitTypesList  =  null  ; 
}  catch  (  IOException   e  )  { 
throw   new   Error  (  e  .  getMessage  (  )  +  " at line "  +  lineCount  )  ; 
}  finally  { 
} 
} 









private   void   parseAndAdd  (  String   line  ,  BufferedReader   reader  )  throws   IOException  { 
try  { 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  line  ,  " "  )  ; 
String   tag  =  tokenizer  .  nextToken  (  )  ; 
if  (  tag  .  equals  (  "CONTINUITY_WEIGHT"  )  )  { 
continuityWeight  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
}  else   if  (  tag  .  equals  (  "OPTIMAL_COUPLING"  )  )  { 
optimalCoupling  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
}  else   if  (  tag  .  equals  (  "EXTEND_SELECTIONS"  )  )  { 
extendSelections  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
}  else   if  (  tag  .  equals  (  "JOIN_METHOD"  )  )  { 
joinMethod  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
}  else   if  (  tag  .  equals  (  "JOIN_WEIGHTS"  )  )  { 
int   numWeights  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
joinWeights  =  new   int  [  numWeights  ]  ; 
for  (  int   i  =  0  ;  i  <  numWeights  ;  i  ++  )  { 
joinWeights  [  i  ]  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
} 
joinWeightShift  =  calcJoinWeightShift  (  joinWeights  )  ; 
}  else   if  (  tag  .  equals  (  "STS"  )  )  { 
String   name  =  tokenizer  .  nextToken  (  )  ; 
if  (  name  .  equals  (  "STS"  )  )  { 
sts  =  new   SampleSet  (  tokenizer  ,  reader  )  ; 
}  else  { 
mcep  =  new   SampleSet  (  tokenizer  ,  reader  )  ; 
} 
}  else   if  (  tag  .  equals  (  "UNITS"  )  )  { 
int   type  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   phone  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   start  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   end  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   prev  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   next  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
DatabaseClusterUnit   unit  =  new   DatabaseClusterUnit  (  type  ,  phone  ,  start  ,  end  ,  prev  ,  next  )  ; 
unitList  .  add  (  unit  )  ; 
}  else   if  (  tag  .  equals  (  "CART"  )  )  { 
String   name  =  tokenizer  .  nextToken  (  )  ; 
int   nodes  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
CART   cart  =  new   CARTImpl  (  reader  ,  nodes  )  ; 
cartMap  .  put  (  name  ,  cart  )  ; 
if  (  defaultCart  ==  null  )  { 
defaultCart  =  cart  ; 
} 
}  else   if  (  tag  .  equals  (  "UNIT_TYPE"  )  )  { 
String   name  =  tokenizer  .  nextToken  (  )  ; 
int   start  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
int   count  =  Integer  .  parseInt  (  tokenizer  .  nextToken  (  )  )  ; 
UnitType   unitType  =  new   UnitType  (  name  ,  start  ,  count  )  ; 
unitTypesList  .  add  (  unitType  )  ; 
}  else  { 
throw   new   Error  (  "Unsupported tag "  +  tag  +  " in db line `"  +  line  +  "'"  )  ; 
} 
}  catch  (  NoSuchElementException   nse  )  { 
throw   new   Error  (  "Error parsing db "  +  nse  .  getMessage  (  )  )  ; 
}  catch  (  NumberFormatException   nfe  )  { 
throw   new   Error  (  "Error parsing numbers in db line `"  +  line  +  "':"  +  nfe  .  getMessage  (  )  )  ; 
} 
} 









private   void   loadBinary  (  InputStream   is  )  throws   IOException  { 
if  (  is   instanceof   FileInputStream  )  { 
FileInputStream   fis  =  (  FileInputStream  )  is  ; 
FileChannel   fc  =  fis  .  getChannel  (  )  ; 
MappedByteBuffer   bb  =  fc  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  (  int  )  fc  .  size  (  )  )  ; 
bb  .  load  (  )  ; 
loadBinary  (  bb  )  ; 
is  .  close  (  )  ; 
}  else  { 
loadBinary  (  new   DataInputStream  (  is  )  )  ; 
} 
} 








private   void   loadBinary  (  ByteBuffer   bb  )  throws   IOException  { 
if  (  bb  .  getInt  (  )  !=  MAGIC  )  { 
throw   new   Error  (  "Bad magic in db"  )  ; 
} 
if  (  bb  .  getInt  (  )  !=  VERSION  )  { 
throw   new   Error  (  "Bad VERSION in db"  )  ; 
} 
continuityWeight  =  bb  .  getInt  (  )  ; 
optimalCoupling  =  bb  .  getInt  (  )  ; 
extendSelections  =  bb  .  getInt  (  )  ; 
joinMethod  =  bb  .  getInt  (  )  ; 
joinWeightShift  =  bb  .  getInt  (  )  ; 
int   weightLength  =  bb  .  getInt  (  )  ; 
joinWeights  =  new   int  [  weightLength  ]  ; 
for  (  int   i  =  0  ;  i  <  joinWeights  .  length  ;  i  ++  )  { 
joinWeights  [  i  ]  =  bb  .  getInt  (  )  ; 
} 
int   unitsLength  =  bb  .  getInt  (  )  ; 
units  =  new   DatabaseClusterUnit  [  unitsLength  ]  ; 
for  (  int   i  =  0  ;  i  <  units  .  length  ;  i  ++  )  { 
units  [  i  ]  =  new   DatabaseClusterUnit  (  bb  )  ; 
} 
int   unitTypesLength  =  bb  .  getInt  (  )  ; 
unitTypes  =  new   UnitType  [  unitTypesLength  ]  ; 
for  (  int   i  =  0  ;  i  <  unitTypes  .  length  ;  i  ++  )  { 
unitTypes  [  i  ]  =  new   UnitType  (  bb  )  ; 
} 
sts  =  new   SampleSet  (  bb  )  ; 
mcep  =  new   SampleSet  (  bb  )  ; 
int   numCarts  =  bb  .  getInt  (  )  ; 
cartMap  =  new   HashMap  (  )  ; 
for  (  int   i  =  0  ;  i  <  numCarts  ;  i  ++  )  { 
String   name  =  Utilities  .  getString  (  bb  )  ; 
CART   cart  =  CARTImpl  .  loadBinary  (  bb  )  ; 
cartMap  .  put  (  name  ,  cart  )  ; 
if  (  defaultCart  ==  null  )  { 
defaultCart  =  cart  ; 
} 
} 
} 








private   void   loadBinary  (  DataInputStream   is  )  throws   IOException  { 
if  (  is  .  readInt  (  )  !=  MAGIC  )  { 
throw   new   Error  (  "Bad magic in db"  )  ; 
} 
if  (  is  .  readInt  (  )  !=  VERSION  )  { 
throw   new   Error  (  "Bad VERSION in db"  )  ; 
} 
continuityWeight  =  is  .  readInt  (  )  ; 
optimalCoupling  =  is  .  readInt  (  )  ; 
extendSelections  =  is  .  readInt  (  )  ; 
joinMethod  =  is  .  readInt  (  )  ; 
joinWeightShift  =  is  .  readInt  (  )  ; 
int   weightLength  =  is  .  readInt  (  )  ; 
joinWeights  =  new   int  [  weightLength  ]  ; 
for  (  int   i  =  0  ;  i  <  joinWeights  .  length  ;  i  ++  )  { 
joinWeights  [  i  ]  =  is  .  readInt  (  )  ; 
} 
int   unitsLength  =  is  .  readInt  (  )  ; 
units  =  new   DatabaseClusterUnit  [  unitsLength  ]  ; 
for  (  int   i  =  0  ;  i  <  units  .  length  ;  i  ++  )  { 
units  [  i  ]  =  new   DatabaseClusterUnit  (  is  )  ; 
} 
int   unitTypesLength  =  is  .  readInt  (  )  ; 
unitTypes  =  new   UnitType  [  unitTypesLength  ]  ; 
for  (  int   i  =  0  ;  i  <  unitTypes  .  length  ;  i  ++  )  { 
unitTypes  [  i  ]  =  new   UnitType  (  is  )  ; 
} 
sts  =  new   SampleSet  (  is  )  ; 
mcep  =  new   SampleSet  (  is  )  ; 
int   numCarts  =  is  .  readInt  (  )  ; 
cartMap  =  new   HashMap  (  )  ; 
for  (  int   i  =  0  ;  i  <  numCarts  ;  i  ++  )  { 
String   name  =  Utilities  .  getString  (  is  )  ; 
CART   cart  =  CARTImpl  .  loadBinary  (  is  )  ; 
cartMap  .  put  (  name  ,  cart  )  ; 
if  (  defaultCart  ==  null  )  { 
defaultCart  =  cart  ; 
} 
} 
} 






void   dumpBinary  (  String   path  )  { 
try  { 
FileOutputStream   fos  =  new   FileOutputStream  (  path  )  ; 
DataOutputStream   os  =  new   DataOutputStream  (  new   BufferedOutputStream  (  fos  )  )  ; 
os  .  writeInt  (  MAGIC  )  ; 
os  .  writeInt  (  VERSION  )  ; 
os  .  writeInt  (  continuityWeight  )  ; 
os  .  writeInt  (  optimalCoupling  )  ; 
os  .  writeInt  (  extendSelections  )  ; 
os  .  writeInt  (  joinMethod  )  ; 
os  .  writeInt  (  joinWeightShift  )  ; 
os  .  writeInt  (  joinWeights  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  joinWeights  .  length  ;  i  ++  )  { 
os  .  writeInt  (  joinWeights  [  i  ]  )  ; 
} 
os  .  writeInt  (  units  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  units  .  length  ;  i  ++  )  { 
units  [  i  ]  .  dumpBinary  (  os  )  ; 
} 
os  .  writeInt  (  unitTypes  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  unitTypes  .  length  ;  i  ++  )  { 
unitTypes  [  i  ]  .  dumpBinary  (  os  )  ; 
} 
sts  .  dumpBinary  (  os  )  ; 
mcep  .  dumpBinary  (  os  )  ; 
os  .  writeInt  (  cartMap  .  size  (  )  )  ; 
for  (  Iterator   i  =  cartMap  .  keySet  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   name  =  (  String  )  i  .  next  (  )  ; 
CART   cart  =  (  CART  )  cartMap  .  get  (  name  )  ; 
Utilities  .  outString  (  os  ,  name  )  ; 
cart  .  dumpBinary  (  os  )  ; 
} 
os  .  close  (  )  ; 
}  catch  (  FileNotFoundException   fe  )  { 
throw   new   Error  (  "Can't dump binary database "  +  fe  .  getMessage  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   Error  (  "Can't write binary database "  +  ioe  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   compare  (  ClusterUnitDatabase   other  )  { 
System  .  out  .  println  (  "Warning: Compare not implemented yet"  )  ; 
return   false  ; 
} 




























public   static   void   main  (  String  [  ]  args  )  { 
boolean   showTimes  =  false  ; 
String   srcPath  =  "."  ; 
String   destPath  =  "."  ; 
try  { 
if  (  args  .  length  >  0  )  { 
BulkTimer   timer  =  new   BulkTimer  (  )  ; 
timer  .  start  (  )  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  equals  (  "-src"  )  )  { 
srcPath  =  args  [  ++  i  ]  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-dest"  )  )  { 
destPath  =  args  [  ++  i  ]  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-generate_binary"  )  )  { 
String   name  =  "clunits.txt"  ; 
if  (  i  +  1  <  args  .  length  )  { 
String   nameArg  =  args  [  ++  i  ]  ; 
if  (  !  nameArg  .  startsWith  (  "-"  )  )  { 
name  =  nameArg  ; 
} 
} 
int   suffixPos  =  name  .  lastIndexOf  (  ".txt"  )  ; 
String   binaryName  =  "clunits.bin"  ; 
if  (  suffixPos  !=  -  1  )  { 
binaryName  =  name  .  substring  (  0  ,  suffixPos  )  +  ".bin"  ; 
} 
System  .  out  .  println  (  "Loading "  +  name  )  ; 
timer  .  start  (  "load_text"  )  ; 
ClusterUnitDatabase   udb  =  new   ClusterUnitDatabase  (  new   URL  (  "file:"  +  srcPath  +  "/"  +  name  )  ,  false  )  ; 
timer  .  stop  (  "load_text"  )  ; 
System  .  out  .  println  (  "Dumping "  +  binaryName  )  ; 
timer  .  start  (  "dump_binary"  )  ; 
udb  .  dumpBinary  (  destPath  +  "/"  +  binaryName  )  ; 
timer  .  stop  (  "dump_binary"  )  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-compare"  )  )  { 
timer  .  start  (  "load_text"  )  ; 
ClusterUnitDatabase   udb  =  new   ClusterUnitDatabase  (  new   URL  (  "file:./cmu_time_awb.txt"  )  ,  false  )  ; 
timer  .  stop  (  "load_text"  )  ; 
timer  .  start  (  "load_binary"  )  ; 
ClusterUnitDatabase   budb  =  new   ClusterUnitDatabase  (  new   URL  (  "file:./cmu_time_awb.bin"  )  ,  true  )  ; 
timer  .  stop  (  "load_binary"  )  ; 
timer  .  start  (  "compare"  )  ; 
if  (  udb  .  compare  (  budb  )  )  { 
System  .  out  .  println  (  "other compare ok"  )  ; 
}  else  { 
System  .  out  .  println  (  "other compare different"  )  ; 
} 
timer  .  stop  (  "compare"  )  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-showtimes"  )  )  { 
showTimes  =  true  ; 
}  else  { 
System  .  out  .  println  (  "Unknown option "  +  args  [  i  ]  )  ; 
} 
} 
timer  .  stop  (  )  ; 
if  (  showTimes  )  { 
timer  .  show  (  "ClusterUnitDatabase"  )  ; 
} 
}  else  { 
System  .  out  .  println  (  "Options: "  )  ; 
System  .  out  .  println  (  "    -src path"  )  ; 
System  .  out  .  println  (  "    -dest path"  )  ; 
System  .  out  .  println  (  "    -compare"  )  ; 
System  .  out  .  println  (  "    -generate_binary"  )  ; 
System  .  out  .  println  (  "    -showTimes"  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
System  .  err  .  println  (  ioe  )  ; 
} 
} 




class   DatabaseClusterUnit  { 

int   type  ; 

int   phone  ; 

int   start  ; 

int   end  ; 

int   prev  ; 

int   next  ; 











DatabaseClusterUnit  (  int   type  ,  int   phone  ,  int   start  ,  int   end  ,  int   prev  ,  int   next  )  { 
this  .  type  =  type  ; 
this  .  phone  =  phone  ; 
this  .  start  =  start  ; 
this  .  end  =  end  ; 
this  .  prev  =  prev  ; 
this  .  next  =  next  ; 
} 








DatabaseClusterUnit  (  ByteBuffer   bb  )  throws   IOException  { 
this  .  type  =  bb  .  getInt  (  )  ; 
this  .  phone  =  bb  .  getInt  (  )  ; 
this  .  start  =  bb  .  getInt  (  )  ; 
this  .  end  =  bb  .  getInt  (  )  ; 
this  .  prev  =  bb  .  getInt  (  )  ; 
this  .  next  =  bb  .  getInt  (  )  ; 
} 








DatabaseClusterUnit  (  DataInputStream   is  )  throws   IOException  { 
this  .  type  =  is  .  readInt  (  )  ; 
this  .  phone  =  is  .  readInt  (  )  ; 
this  .  start  =  is  .  readInt  (  )  ; 
this  .  end  =  is  .  readInt  (  )  ; 
this  .  prev  =  is  .  readInt  (  )  ; 
this  .  next  =  is  .  readInt  (  )  ; 
} 






String   getName  (  )  { 
return   unitTypes  [  type  ]  .  getName  (  )  ; 
} 








void   dumpBinary  (  DataOutputStream   os  )  throws   IOException  { 
os  .  writeInt  (  type  )  ; 
os  .  writeInt  (  phone  )  ; 
os  .  writeInt  (  start  )  ; 
os  .  writeInt  (  end  )  ; 
os  .  writeInt  (  prev  )  ; 
os  .  writeInt  (  next  )  ; 
} 
} 






private   void   error  (  String   s  )  { 
System  .  out  .  println  (  "ClusterUnitDatabase Error: "  +  s  )  ; 
} 
} 




class   UnitType  { 

private   String   name  ; 

private   int   start  ; 

private   int   count  ; 








UnitType  (  String   name  ,  int   start  ,  int   count  )  { 
this  .  name  =  name  ; 
this  .  start  =  start  ; 
this  .  count  =  count  ; 
} 








UnitType  (  DataInputStream   is  )  throws   IOException  { 
this  .  name  =  Utilities  .  getString  (  is  )  ; 
this  .  start  =  is  .  readInt  (  )  ; 
this  .  count  =  is  .  readInt  (  )  ; 
} 








UnitType  (  ByteBuffer   bb  )  throws   IOException  { 
this  .  name  =  Utilities  .  getString  (  bb  )  ; 
this  .  start  =  bb  .  getInt  (  )  ; 
this  .  count  =  bb  .  getInt  (  )  ; 
} 






String   getName  (  )  { 
return   name  ; 
} 






int   getStart  (  )  { 
return   start  ; 
} 






int   getCount  (  )  { 
return   count  ; 
} 








void   dumpBinary  (  DataOutputStream   os  )  throws   IOException  { 
Utilities  .  outString  (  os  ,  name  )  ; 
os  .  writeInt  (  start  )  ; 
os  .  writeInt  (  count  )  ; 
} 
} 

