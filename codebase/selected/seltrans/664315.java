package   jasmin  .  core  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  zip  .  *  ; 




public   class   DataSpace  { 




public   Fpu   fpu  ; 




public   Parser   parser  ; 







public   void   setParser  (  Parser   p  )  { 
parser  =  p  ; 
} 




private   static   String  [  ]  registers  =  {  "EAX"  ,  "AX"  ,  "AL"  ,  "AH"  ,  "EBX"  ,  "BX"  ,  "BL"  ,  "BH"  ,  "ECX"  ,  "CX"  ,  "CL"  ,  "CH"  ,  "EDX"  ,  "DX"  ,  "DL"  ,  "DH"  ,  "ESI"  ,  "SI"  ,  "EDI"  ,  "DI"  ,  "ESP"  ,  "SP"  ,  "EBP"  ,  "BP"  ,  "EIP"  }  ; 




public   static   String  [  ]  getRegisterList  (  )  { 
return   registers  ; 
} 




public   Address   EAX  ,  AX  ,  AH  ,  AL  ,  EBX  ,  BX  ,  BH  ,  BL  ,  ECX  ,  CX  ,  CH  ,  CL  ,  EDX  ,  DX  ,  DH  ,  DL  ,  ESI  ,  SI  ,  EDI  ,  DI  ,  ESP  ,  SP  ,  EBP  ,  BP  ,  EIP  ; 

private   RegisterSet  [  ]  registerSets  ; 






public   Address   Stack  (  int   size  )  { 
return   new   Address  (  Op  .  MEM  ,  size  ,  (  int  )  ESP  .  getShortcut  (  )  )  ; 
} 




private   static   String  [  ]  prefixes  =  {  "REP"  ,  "REPE"  ,  "REPZ"  ,  "REPNE"  ,  "REPNZ"  }  ; 

private   static   String   prefixesMatchingString  =  CalculatedAddress  .  createMatchingString  (  prefixes  )  ; 

public   static   Pattern   prefixesMatchingPattern  =  Pattern  .  compile  (  prefixesMatchingString  )  ; 

public   static   final   int   BIN  =  2  ; 

public   static   final   int   HEX  =  16  ; 

public   static   final   int   SIGNED  =  -  10  ; 

public   static   final   int   UNSIGNED  =  10  ; 




private   int   MEMSIZE  ; 




public   int   getMEMSIZE  (  )  { 
return   MEMSIZE  ; 
} 

private   int   memAddressStart  =  0x10000  ; 

public   int   getMemAddressStart  (  )  { 
return   memAddressStart  ; 
} 




private   Memory   memory  ; 

private   Registers   reg  ; 

private   TreeMap  <  Integer  ,  MemCellInfo  >  memInfo  ; 

private   TreeMap  <  Integer  ,  MemCellInfo  >  regInfo  ; 

private   int   nextReservableAddress  =  memAddressStart  ; 

private   boolean   addressOutOfRange  =  false  ; 








public   boolean   addressOutOfRange  (  )  { 
return   addressOutOfRange  ; 
} 




public   void   setAddressOutOfRange  (  )  { 
addressOutOfRange  =  true  ; 
} 




public   void   clearAddressOutOfRange  (  )  { 
addressOutOfRange  =  false  ; 
} 




public   boolean   fCarry  ,  fOverflow  ,  fSign  ,  fZero  ,  fParity  ,  fAuxiliary  ,  fTrap  ,  fDirection  ; 




private   int   REGSIZE  =  36  ; 




private   Hashtable  <  String  ,  Address  >  regtable  ; 








public   void   setInstructionPointer  (  int   ip  )  { 
putInteger  (  ip  ,  EIP  )  ; 
} 







public   int   getInstructionPointer  (  )  { 
return  (  int  )  EIP  .  getShortcut  (  )  ; 
} 




private   Hashtable  <  String  ,  Integer  >  variables  ; 

private   Hashtable  <  String  ,  Long  >  constants  ; 









public   DataSpace  (  int   size  ,  int   startAddress  )  { 
fpu  =  new   Fpu  (  )  ; 
initMem  (  size  ,  startAddress  )  ; 
initRegisters  (  )  ; 
clearDirty  (  )  ; 
clearFlags  (  )  ; 
} 









private   void   initMem  (  int   size  ,  int   startAddress  )  { 
variables  =  new   Hashtable  <  String  ,  Integer  >  (  )  ; 
constants  =  new   Hashtable  <  String  ,  Long  >  (  )  ; 
MEMSIZE  =  (  size  +  3  )  -  (  (  size  +  3  )  %  4  )  ; 
memory  =  new   Memory  (  MEMSIZE  ,  startAddress  )  ; 
memAddressStart  =  startAddress  ; 
nextReservableAddress  =  startAddress  ; 
memInfo  =  new   TreeMap  <  Integer  ,  MemCellInfo  >  (  )  ; 
} 





private   void   initRegisters  (  )  { 
regtable  =  new   Hashtable  <  String  ,  Address  >  (  )  ; 
reg  =  new   Registers  (  )  ; 
regInfo  =  new   TreeMap  <  Integer  ,  MemCellInfo  >  (  )  ; 
EAX  =  reg  .  constructAddress  (  "EAX"  ,  regtable  )  ; 
AX  =  reg  .  constructAddress  (  "AX"  ,  regtable  )  ; 
AH  =  reg  .  constructAddress  (  "AH"  ,  regtable  )  ; 
AL  =  reg  .  constructAddress  (  "AL"  ,  regtable  )  ; 
EBX  =  reg  .  constructAddress  (  "EBX"  ,  regtable  )  ; 
BX  =  reg  .  constructAddress  (  "BX"  ,  regtable  )  ; 
BH  =  reg  .  constructAddress  (  "BH"  ,  regtable  )  ; 
BL  =  reg  .  constructAddress  (  "BL"  ,  regtable  )  ; 
ECX  =  reg  .  constructAddress  (  "ECX"  ,  regtable  )  ; 
CX  =  reg  .  constructAddress  (  "CX"  ,  regtable  )  ; 
CH  =  reg  .  constructAddress  (  "CH"  ,  regtable  )  ; 
CL  =  reg  .  constructAddress  (  "CL"  ,  regtable  )  ; 
EDX  =  reg  .  constructAddress  (  "EDX"  ,  regtable  )  ; 
DX  =  reg  .  constructAddress  (  "DX"  ,  regtable  )  ; 
DH  =  reg  .  constructAddress  (  "DH"  ,  regtable  )  ; 
DL  =  reg  .  constructAddress  (  "DL"  ,  regtable  )  ; 
ESI  =  reg  .  constructAddress  (  "ESI"  ,  regtable  )  ; 
SI  =  reg  .  constructAddress  (  "SI"  ,  regtable  )  ; 
EDI  =  reg  .  constructAddress  (  "EDI"  ,  regtable  )  ; 
DI  =  reg  .  constructAddress  (  "DI"  ,  regtable  )  ; 
ESP  =  reg  .  constructAddress  (  "ESP"  ,  regtable  )  ; 
SP  =  reg  .  constructAddress  (  "SP"  ,  regtable  )  ; 
EBP  =  reg  .  constructAddress  (  "EBP"  ,  regtable  )  ; 
BP  =  reg  .  constructAddress  (  "BP"  ,  regtable  )  ; 
EIP  =  reg  .  constructAddress  (  "EIP"  ,  regtable  )  ; 
registerSets  =  new   RegisterSet  [  9  ]  ; 
registerSets  [  0  ]  =  new   RegisterSet  (  AL  ,  AH  ,  AX  ,  EAX  ,  "AL"  ,  "AH"  ,  "AX"  ,  "EAX"  )  ; 
registerSets  [  1  ]  =  new   RegisterSet  (  BL  ,  BH  ,  BX  ,  EBX  ,  "BL"  ,  "BH"  ,  "BX"  ,  "EBX"  )  ; 
registerSets  [  2  ]  =  new   RegisterSet  (  CL  ,  CH  ,  CX  ,  ECX  ,  "CL"  ,  "CH"  ,  "CX"  ,  "ECX"  )  ; 
registerSets  [  3  ]  =  new   RegisterSet  (  DL  ,  DH  ,  DX  ,  EDX  ,  "DL"  ,  "DH"  ,  "DX"  ,  "EDX"  )  ; 
registerSets  [  4  ]  =  new   RegisterSet  (  null  ,  null  ,  SI  ,  ESI  ,  ""  ,  ""  ,  "SI"  ,  "ESI"  )  ; 
registerSets  [  5  ]  =  new   RegisterSet  (  null  ,  null  ,  DI  ,  EDI  ,  ""  ,  ""  ,  "DI"  ,  "EDI"  )  ; 
registerSets  [  6  ]  =  new   RegisterSet  (  null  ,  null  ,  SP  ,  ESP  ,  ""  ,  ""  ,  "SP"  ,  "ESP"  )  ; 
registerSets  [  7  ]  =  new   RegisterSet  (  null  ,  null  ,  BP  ,  EBP  ,  ""  ,  ""  ,  "BP"  ,  "EBP"  )  ; 
registerSets  [  8  ]  =  new   RegisterSet  (  null  ,  null  ,  null  ,  EIP  ,  ""  ,  ""  ,  ""  ,  "EIP"  )  ; 
put  (  MEMSIZE  +  memAddressStart  ,  ESP  ,  null  )  ; 
put  (  MEMSIZE  +  memAddressStart  ,  EBP  ,  null  )  ; 
reg  .  clearDirty  (  )  ; 
} 








public   boolean   save  (  File   file  )  { 
ObjectOutputStream   oos  ; 
ZipOutputStream   zos  ; 
try  { 
if  (  !  file  .  getParentFile  (  )  .  exists  (  )  )  { 
file  .  getParentFile  (  )  .  mkdirs  (  )  ; 
} 
if  (  !  file  .  exists  (  )  )  { 
file  .  createNewFile  (  )  ; 
} 
zos  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  file  )  )  )  ; 
zos  .  putNextEntry  (  new   ZipEntry  (  "dataspace"  )  )  ; 
zos  .  setLevel  (  9  )  ; 
oos  =  new   ObjectOutputStream  (  zos  )  ; 
save  (  oos  )  ; 
oos  .  flush  (  )  ; 
oos  .  close  (  )  ; 
zos  .  closeEntry  (  )  ; 
zos  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
return   false  ; 
} 
return   true  ; 
} 








public   void   save  (  ObjectOutputStream   oos  )  throws   IOException  { 
oos  .  writeInt  (  MEMSIZE  )  ; 
oos  .  writeInt  (  REGSIZE  )  ; 
oos  .  writeObject  (  reg  )  ; 
oos  .  writeObject  (  regInfo  )  ; 
oos  .  writeObject  (  memory  )  ; 
oos  .  writeObject  (  memInfo  )  ; 
oos  .  writeObject  (  variables  )  ; 
oos  .  writeObject  (  constants  )  ; 
oos  .  writeBoolean  (  fCarry  )  ; 
oos  .  writeBoolean  (  fOverflow  )  ; 
oos  .  writeBoolean  (  fSign  )  ; 
oos  .  writeBoolean  (  fZero  )  ; 
oos  .  writeBoolean  (  fParity  )  ; 
oos  .  writeBoolean  (  fAuxiliary  )  ; 
oos  .  writeBoolean  (  fTrap  )  ; 
oos  .  writeBoolean  (  fDirection  )  ; 
oos  .  writeInt  (  nextReservableAddress  )  ; 
} 








public   boolean   load  (  File   file  )  { 
ObjectInputStream   ois  ; 
ZipInputStream   zis  ; 
if  (  !  file  .  exists  (  )  )  { 
return   false  ; 
} 
try  { 
zis  =  new   ZipInputStream  (  new   BufferedInputStream  (  new   FileInputStream  (  file  )  )  )  ; 
zis  .  getNextEntry  (  )  ; 
ois  =  new   ObjectInputStream  (  zis  )  ; 
load  (  ois  )  ; 
ois  .  close  (  )  ; 
zis  .  closeEntry  (  )  ; 
zis  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
return   true  ; 
} 









@  SuppressWarnings  (  "unchecked"  ) 
public   void   load  (  ObjectInputStream   ois  )  throws   IOException  ,  ClassNotFoundException  { 
MEMSIZE  =  ois  .  readInt  (  )  ; 
REGSIZE  =  ois  .  readInt  (  )  ; 
reg  =  (  Registers  )  ois  .  readObject  (  )  ; 
regInfo  =  (  TreeMap  <  Integer  ,  MemCellInfo  >  )  ois  .  readObject  (  )  ; 
memory  =  (  Memory  )  ois  .  readObject  (  )  ; 
memInfo  =  (  TreeMap  <  Integer  ,  MemCellInfo  >  )  ois  .  readObject  (  )  ; 
variables  =  (  Hashtable  <  String  ,  Integer  >  )  ois  .  readObject  (  )  ; 
constants  =  (  Hashtable  <  String  ,  Long  >  )  ois  .  readObject  (  )  ; 
fCarry  =  ois  .  readBoolean  (  )  ; 
fOverflow  =  ois  .  readBoolean  (  )  ; 
fSign  =  ois  .  readBoolean  (  )  ; 
fZero  =  ois  .  readBoolean  (  )  ; 
fParity  =  ois  .  readBoolean  (  )  ; 
fAuxiliary  =  ois  .  readBoolean  (  )  ; 
fTrap  =  ois  .  readBoolean  (  )  ; 
fDirection  =  ois  .  readBoolean  (  )  ; 
nextReservableAddress  =  ois  .  readInt  (  )  ; 
} 









private   void   putInteger  (  long   value  ,  Address   address  )  { 
if  (  (  address  .  type  &  Op  .  REG  )  !=  0  )  { 
reg  .  set  (  address  ,  value  )  ; 
}  else   if  (  (  address  .  type  &  Op  .  MEM  )  !=  0  )  { 
if  (  (  address  .  address  <  memAddressStart  )  ||  (  (  address  .  address  +  address  .  size  )  >  (  MEMSIZE  +  memAddressStart  )  )  )  { 
addressOutOfRange  =  true  ; 
return  ; 
} 
long   mask  =  255  ; 
int   bytebuffer  =  0  ; 
for  (  int   i  =  0  ;  i  <  address  .  size  ;  i  ++  )  { 
bytebuffer  =  (  int  )  (  value  &  mask  )  ; 
memory  .  set  (  address  .  address  +  i  ,  bytebuffer  )  ; 
value  =  value  >  >  8  ; 
} 
}  else  { 
return  ; 
} 
} 












public   void   putString  (  String   numberString  ,  Address   address  ,  int   type  )  { 
numberString  =  numberString  .  toUpperCase  (  )  ; 
if  (  type  ==  BIN  )  { 
if  (  !  numberString  .  endsWith  (  "B"  )  )  { 
numberString  +=  "B"  ; 
} 
}  else   if  (  type  ==  HEX  )  { 
if  (  !  (  numberString  .  startsWith  (  "0X"  )  ||  numberString  .  endsWith  (  "H"  )  )  )  { 
numberString  =  "0X"  +  numberString  ; 
} 
} 
numberString  =  Parser  .  hex2dec  (  numberString  )  ; 
if  (  !  Op  .  matches  (  parser  .  getOperandType  (  numberString  )  ,  Op  .  IMM  )  )  { 
System  .  out  .  println  (  "DataSpace.putString: invalid OperandType!"  )  ; 
return  ; 
} 
putInteger  (  Long  .  parseLong  (  numberString  )  ,  address  )  ; 
} 










public   long   getUnsignedMemory  (  int   address  ,  int   size  )  { 
long   result  =  0  ; 
long   bitmask  =  255  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
result  =  result  |  (  (  bitmask  &  memory  .  get  (  address  +  i  )  )  <<  (  8  *  i  )  )  ; 
} 
return   result  ; 
} 











public   long   getSignedMemory  (  int   address  ,  int   size  )  { 
if  (  size  ==  8  )  { 
long   value  =  0  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
value  =  value  |  (  (  (  long  )  memory  .  get  (  address  +  i  )  )  <<  (  8  *  i  )  )  ; 
} 
return   value  ; 
} 
if  (  size  ==  4  )  { 
int   value  =  0  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
value  =  (  value  |  (  memory  .  get  (  address  +  i  )  <<  (  8  *  i  )  )  )  ; 
} 
return   value  ; 
} 
if  (  size  ==  2  )  { 
short   value  =  0  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
value  =  (  short  )  (  value  |  (  memory  .  get  (  address  +  i  )  <<  (  8  *  i  )  )  )  ; 
} 
return   value  ; 
} 
if  (  size  ==  1  )  { 
return  (  byte  )  (  memory  .  get  (  address  )  )  ; 
} 
throw   new   RuntimeException  (  "getSignedMemory called with invalid size!"  )  ; 
} 









private   long   getSignedRegister  (  Address   address  )  { 
if  (  address  .  size  ==  8  )  { 
return   address  .  getShortcut  (  )  ; 
} 
if  (  address  .  size  ==  4  )  { 
return  (  int  )  address  .  getShortcut  (  )  ; 
} 
if  (  address  .  size  ==  2  )  { 
return  (  short  )  address  .  getShortcut  (  )  ; 
} 
if  (  address  .  size  ==  1  )  { 
return  (  byte  )  address  .  getShortcut  (  )  ; 
} 
throw   new   RuntimeException  (  "getSignedRegister called with invalid size. This can't happen. ;-)"  )  ; 
} 












public   static   String   getString  (  long   value  ,  int   size  ,  int   type  )  { 
if  (  type  ==  SIGNED  )  { 
switch  (  size  )  { 
case   1  : 
return   String  .  valueOf  (  (  byte  )  value  )  ; 
case   2  : 
return   String  .  valueOf  (  (  short  )  value  )  ; 
case   4  : 
return   String  .  valueOf  (  (  int  )  value  )  ; 
} 
}  else   if  (  type  ==  UNSIGNED  )  { 
return   String  .  valueOf  (  value  )  ; 
}  else   if  (  type  ==  BIN  )  { 
return   Integer  .  toBinaryString  (  (  int  )  value  )  ; 
}  else   if  (  type  ==  HEX  )  { 
String   temp  =  Integer  .  toHexString  (  (  int  )  value  )  .  toUpperCase  (  )  ; 
while  (  temp  .  length  (  )  <  (  size  *  2  )  )  { 
temp  =  "0"  +  temp  ; 
} 
temp  =  "0x"  +  temp  ; 
return   temp  ; 
} 
return  ""  ; 
} 




public   void   updateDirty  (  )  { 
memory  .  updateDirty  (  )  ; 
reg  .  updateDirty  (  )  ; 
} 








public   void   setDirty  (  Address   address  )  { 
if  (  (  address  .  type  &  Op  .  REG  )  !=  0  )  { 
reg  .  setDirty  (  address  )  ; 
}  else   if  (  (  address  .  type  &  Op  .  MEM  )  !=  0  )  { 
for  (  int   i  =  0  ;  i  <  address  .  size  ;  i  ++  )  { 
memory  .  setDirty  (  i  +  address  .  address  )  ; 
} 
} 
} 








public   boolean   isDirty  (  Address   a  ,  int   steps  )  { 
if  (  (  a  .  type  &  Op  .  MEM  )  !=  0  )  { 
for  (  int   i  =  0  ;  i  <  a  .  size  ;  i  ++  )  { 
if  (  memory  .  isDirty  (  i  +  a  .  address  ,  steps  )  )  { 
return   true  ; 
} 
} 
}  else   if  (  (  a  .  type  &  Op  .  REG  )  !=  0  )  { 
return   reg  .  isDirty  (  a  ,  steps  )  ; 
} 
return   false  ; 
} 




private   void   clearReg  (  )  { 
reg  .  reset  (  )  ; 
put  (  MEMSIZE  +  memAddressStart  ,  ESP  ,  null  )  ; 
put  (  MEMSIZE  +  memAddressStart  ,  EBP  ,  null  )  ; 
reg  .  clearDirty  (  )  ; 
} 




private   void   clearMem  (  )  { 
memory  .  reset  (  )  ; 
memInfo  =  new   TreeMap  <  Integer  ,  MemCellInfo  >  (  )  ; 
nextReservableAddress  =  memAddressStart  ; 
variables  .  clear  (  )  ; 
constants  .  clear  (  )  ; 
} 




private   void   clearDirty  (  )  { 
memory  .  clearDirty  (  )  ; 
reg  .  clearDirty  (  )  ; 
} 




private   void   clearFlags  (  )  { 
fCarry  =  fOverflow  =  fSign  =  fZero  =  fParity  =  fAuxiliary  =  fTrap  =  fDirection  =  false  ; 
} 




public   void   clear  (  )  { 
clearFlags  (  )  ; 
clearMem  (  )  ; 
addressOutOfRange  =  false  ; 
clearReg  (  )  ; 
fpu  .  clear  (  )  ; 
} 








public   Address   getRegisterArgument  (  String   s  )  { 
return   regtable  .  get  (  s  )  ; 
} 








public   MemCellInfo   memInfo  (  Address   a  )  { 
Integer   index  =  new   Integer  (  a  .  address  )  ; 
if  (  Op  .  matches  (  a  .  type  ,  Op  .  MEM  )  )  { 
return   memInfo  .  get  (  index  )  ; 
}  else   if  (  Op  .  matches  (  a  .  type  ,  Op  .  REG  )  )  { 
return   regInfo  .  get  (  index  )  ; 
} 
return   null  ; 
} 

private   void   memInfoPut  (  Address   a  ,  MemCellInfo   info  )  { 
Integer   index  =  new   Integer  (  a  .  address  )  ; 
if  (  Op  .  matches  (  a  .  type  ,  Op  .  MEM  )  )  { 
if  (  (  a  .  address  >=  (  MEMSIZE  +  memAddressStart  )  )  ||  (  a  .  address  <  memAddressStart  )  )  { 
return  ; 
} 
memInfo  .  put  (  index  ,  info  )  ; 
}  else   if  (  Op  .  matches  (  a  .  type  ,  Op  .  REG  )  )  { 
regInfo  .  put  (  index  ,  info  )  ; 
} 
} 

private   void   memInfoDelete  (  Address   a  )  { 
Integer   index  =  new   Integer  (  a  .  address  )  ; 
if  (  Op  .  matches  (  a  .  type  ,  Op  .  MEM  )  )  { 
if  (  (  a  .  address  >=  (  MEMSIZE  +  memAddressStart  )  )  ||  (  a  .  address  <  memAddressStart  )  )  { 
return  ; 
} 
memInfo  .  remove  (  index  )  ; 
}  else   if  (  Op  .  matches  (  a  .  type  ,  Op  .  REG  )  )  { 
regInfo  .  remove  (  index  )  ; 
} 
} 











public   Address   getMatchingRegister  (  Address   wantedBigRegister  ,  int   size  )  { 
for  (  RegisterSet   rs  :  registerSets  )  { 
if  (  rs  .  aE  ==  wantedBigRegister  )  { 
if  (  size  ==  1  )  { 
return   rs  .  aL  ; 
} 
if  (  size  ==  2  )  { 
return   rs  .  aX  ; 
} 
if  (  size  ==  4  )  { 
return   rs  .  aE  ; 
} 
} 
} 
return   null  ; 
} 






public   int   getRegisterSize  (  String   register  )  { 
Address   reg  =  regtable  .  get  (  register  )  ; 
if  (  reg  !=  null  )  { 
return   reg  .  size  ; 
} 
return   0  ; 
} 





public   RegisterSet  [  ]  getRegisterSets  (  )  { 
return   registerSets  ; 
} 











public   void   put  (  long   value  ,  Address   a  ,  MemCellInfo   memCellInfo  )  { 
if  (  a  ==  null  )  { 
return  ; 
} 
if  (  (  a  .  type  &  (  Op  .  MEM  |  Op  .  REG  )  )  !=  0  )  { 
putInteger  (  value  ,  a  )  ; 
if  (  memCellInfo  ==  null  )  { 
memInfoDelete  (  a  )  ; 
}  else  { 
memInfoPut  (  a  ,  memCellInfo  )  ; 
} 
return  ; 
} 
if  (  (  a  .  type  &  Op  .  FPUREG  )  !=  0  )  { 
fpu  .  put  (  a  ,  value  )  ; 
return  ; 
} 
} 










public   long   getUpdate  (  Address   a  ,  boolean   signed  )  { 
if  (  a  ==  null  )  { 
return   0  ; 
} 
if  (  (  a  .  type  &  Op  .  MEM  )  !=  0  )  { 
if  (  (  a  .  address  <  memAddressStart  )  ||  (  (  a  .  address  +  a  .  size  )  >  (  MEMSIZE  +  memAddressStart  )  )  )  { 
addressOutOfRange  =  true  ; 
return   0  ; 
} 
MemCellInfo   info  =  memInfo  .  get  (  a  .  address  )  ; 
if  (  info  !=  null  )  { 
if  (  info  .  type  ==  Op  .  LABEL  )  { 
long   currentLabelLine  =  getInitial  (  info  .  value  ,  Op  .  LABEL  ,  info  .  size  ,  false  )  ; 
long   storedLabelLine  =  getUnsignedMemory  (  a  .  address  ,  a  .  size  )  ; 
if  (  currentLabelLine  !=  storedLabelLine  )  { 
put  (  currentLabelLine  ,  a  ,  info  )  ; 
} 
return   currentLabelLine  ; 
} 
} 
if  (  signed  )  { 
return   getSignedMemory  (  a  .  address  ,  a  .  size  )  ; 
}  else  { 
return   getUnsignedMemory  (  a  .  address  ,  a  .  size  )  ; 
} 
} 
if  (  (  a  .  type  &  Op  .  REG  )  !=  0  )  { 
MemCellInfo   info  =  regInfo  .  get  (  a  .  address  )  ; 
if  (  info  !=  null  )  { 
if  (  info  .  type  ==  Op  .  LABEL  )  { 
long   currentLabelLine  =  getInitial  (  info  .  value  ,  Op  .  LABEL  ,  info  .  size  ,  false  )  ; 
long   storedLabelLine  =  a  .  getShortcut  (  )  ; 
if  (  currentLabelLine  !=  storedLabelLine  )  { 
put  (  currentLabelLine  ,  a  ,  info  )  ; 
} 
return   currentLabelLine  ; 
} 
} 
if  (  signed  )  { 
return   getSignedRegister  (  a  )  ; 
}  else  { 
return   a  .  getShortcut  (  )  ; 
} 
} 
if  (  (  a  .  type  &  Op  .  FPUREG  )  !=  0  )  { 
return   fpu  .  get  (  a  )  ; 
} 
return   a  .  value  ; 
} 










public   long   getInitial  (  FullArgument   a  ,  boolean   signed  )  { 
return   getInitial  (  a  .  arg  ,  a  .  address  .  type  ,  a  .  address  .  size  ,  signed  )  ; 
} 

private   long   getInitial  (  String   src  ,  int   type  ,  int   size  ,  boolean   signed  )  { 
if  (  (  type  &  Op  .  IMM  )  !=  0  )  { 
long   value  =  Long  .  valueOf  (  src  )  ; 
if  (  signed  )  { 
return   value  ; 
}  else  { 
value  =  (  value  &  (  (  (  long  )  1  <<  (  size  *  8  )  )  -  1  )  )  ; 
return   value  ; 
} 
}  else   if  (  type  ==  Op  .  CHARS  )  { 
return   Parser  .  getCharsAsNumber  (  src  )  ; 
}  else   if  (  type  ==  Op  .  LABEL  )  { 
return   parser  .  doc  .  getLabelLine  (  src  )  ; 
}  else   if  (  type  ==  Op  .  VARIABLE  )  { 
return   getVariable  (  src  )  ; 
}  else   if  (  type  ==  Op  .  CONST  )  { 
return   getConstant  (  src  )  ; 
}  else   if  (  type  ==  Op  .  FLOAT  )  { 
if  (  size  ==  8  )  { 
return   Double  .  doubleToRawLongBits  (  Double  .  parseDouble  (  src  )  )  ; 
}  else   if  (  size  ==  4  )  { 
return   Float  .  floatToRawIntBits  (  Float  .  parseFloat  (  src  )  )  ; 
} 
} 
return   0  ; 
} 












public   Address   malloc  (  int   size  ,  int   howmany  )  { 
int   result  =  nextReservableAddress  ; 
nextReservableAddress  +=  size  *  howmany  ; 
if  (  nextReservableAddress  >  (  MEMSIZE  +  memAddressStart  )  )  { 
addressOutOfRange  =  true  ; 
return   null  ; 
} 
Address   a  =  new   Address  (  Op  .  MEM  ,  size  ,  result  )  ; 
return   a  ; 
} 







public   void   registerVariable  (  String   label  )  { 
if  (  !  variables  .  containsKey  (  label  )  )  { 
variables  .  put  (  label  ,  nextReservableAddress  )  ; 
} 
if  (  constants  .  containsKey  (  label  )  )  { 
System  .  out  .  println  (  label  +  " is now a variable!"  )  ; 
constants  .  remove  (  label  )  ; 
} 
} 

public   void   setVariableAddress  (  String   variable  ,  int   address  )  { 
variables  .  put  (  variable  ,  address  )  ; 
} 







public   void   registerConstant  (  String   label  )  { 
if  (  !  constants  .  containsKey  (  label  )  )  { 
constants  .  put  (  label  ,  0L  )  ; 
} 
if  (  variables  .  containsKey  (  label  )  )  { 
System  .  out  .  println  (  label  +  " is now a constant!"  )  ; 
variables  .  remove  (  label  )  ; 
} 
} 

public   void   setConstantValue  (  String   label  ,  long   value  )  { 
constants  .  put  (  label  ,  value  )  ; 
if  (  variables  .  containsKey  (  label  )  )  { 
variables  .  remove  (  label  )  ; 
} 
} 







public   void   unregisterVariable  (  String   label  )  { 
if  (  label  !=  null  )  { 
variables  .  remove  (  label  )  ; 
} 
} 

public   void   unregisterConstant  (  String   constant  )  { 
if  (  constant  !=  null  )  { 
constants  .  remove  (  constant  )  ; 
} 
} 






public   int   getVariable  (  String   symbol  )  { 
return   variables  .  get  (  symbol  )  ; 
} 

public   long   getConstant  (  String   constant  )  { 
if  (  constants  ==  null  )  { 
return   0L  ; 
} 
return   constants  .  get  (  constant  )  ; 
} 




public   String  [  ]  getVariableList  (  )  { 
Enumeration  <  String  >  enumeration  =  variables  .  keys  (  )  ; 
String  [  ]  result  =  new   String  [  variables  .  size  (  )  ]  ; 
int   i  =  0  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
result  [  i  ++  ]  =  enumeration  .  nextElement  (  )  ; 
} 
return   result  ; 
} 

public   String  [  ]  getConstantList  (  )  { 
Enumeration  <  String  >  enumeration  =  constants  .  keys  (  )  ; 
String  [  ]  result  =  new   String  [  constants  .  size  (  )  ]  ; 
int   i  =  0  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
result  [  i  ++  ]  =  enumeration  .  nextElement  (  )  ; 
} 
return   result  ; 
} 








public   boolean   isVariable  (  String   symbol  )  { 
return  (  variables  .  containsKey  (  symbol  )  )  ; 
} 

public   boolean   isConstant  (  String   symbol  )  { 
return  (  constants  .  containsKey  (  symbol  )  )  ; 
} 

public   void   addMemoryListener  (  IListener   l  ,  int   address  )  { 
memory  .  addListener  (  l  ,  address  )  ; 
} 

public   void   addMemoryListener  (  IListener   l  )  { 
memory  .  addListener  (  l  )  ; 
} 

public   void   removeMemoryListener  (  IListener   l  ,  int   address  )  { 
memory  .  removeListener  (  l  ,  address  )  ; 
} 

public   void   removeMemoryListener  (  IListener   l  )  { 
memory  .  removeListener  (  l  )  ; 
} 
} 

