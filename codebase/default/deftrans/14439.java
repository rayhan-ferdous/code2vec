import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  lang  .  reflect  .  *  ; 
import   com  .  ibm  .  JikesRVM  .  *  ; 



























































public   class   GenerateAssembler  { 


static   final   boolean   DEBUG  =  false  ; 


static   FileWriter   out  ; 





private   static   void   emit  (  String   s  )  { 
try  { 
out  .  write  (  s  ,  0  ,  s  .  length  (  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 






private   static   void   emitTab  (  int   level  )  { 
for  (  int   i  =  0  ;  i  <  level  ;  i  ++  )  emit  (  "  "  )  ; 
} 









private   static   Class   formats  ; 





static  { 
try  { 
formats  =  Class  .  forName  (  "OPT_InstructionFormatTables"  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 








static   int  [  ]  currentOpcodeArgTable  ; 






static   String  [  ]  currentOpcodeSymbolicNames  ; 







static   String   currentOpcode  ; 






static   String   currentFormat  ; 








static   Hashtable   opcodeArgTables  ; 




static  { 
opcodeArgTables  =  new   Hashtable  (  )  ; 
opcodeArgTables  .  put  (  "CALL"  ,  new   int  [  ]  {  2  }  )  ; 
opcodeArgTables  .  put  (  "INT"  ,  new   int  [  ]  {  1  }  )  ; 
opcodeArgTables  .  put  (  "CDQ"  ,  new   int  [  ]  {  0  }  )  ; 
opcodeArgTables  .  put  (  "DIV"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "IDIV"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "MUL"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "IMUL1"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "DIV"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "IDIV"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "SET"  ,  new   int  [  ]  {  1  ,  0  }  )  ; 
opcodeArgTables  .  put  (  "CMPXCHG"  ,  new   int  [  ]  {  1  ,  2  }  )  ; 
opcodeArgTables  .  put  (  "FCMOV"  ,  new   int  [  ]  {  2  ,  0  ,  1  }  )  ; 
opcodeArgTables  .  put  (  "CMOV"  ,  new   int  [  ]  {  2  ,  0  ,  1  }  )  ; 
} 








static   void   setCurrentOpcode  (  String   opcode  )  { 
try  { 
currentOpcode  =  opcode  ; 
currentOpcodeArgTable  =  (  int  [  ]  )  opcodeArgTables  .  get  (  opcode  )  ; 
currentFormat  =  OPT_OperatorFormatTables  .  getFormat  (  opcode  )  ; 
Field   f  =  formats  .  getDeclaredField  (  currentFormat  +  "ParameterNames"  )  ; 
currentOpcodeSymbolicNames  =  (  String  [  ]  )  f  .  get  (  null  )  ; 
}  catch  (  Throwable   e  )  { 
System  .  err  .  println  (  "Cannot handle VM_Assembler opcode "  +  opcode  )  ; 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 




static   final   int   Immediate  =  0  ; 







static   final   int   Register  =  1  ; 







static   final   int   Condition  =  2  ; 






static   final   int   Absolute  =  3  ; 










static   final   int   RegisterDisplacement  =  4  ; 









static   final   int   RegisterOffset  =  5  ; 





static   final   int   RegisterIndexed  =  6  ; 






static   final   int   RegisterIndirect  =  7  ; 











static   final   int   Label  =  8  ; 







static   final   int   LabelOrImmediate  =  9  ; 





static   final   int   SIZES  =  3  ; 




static   final   int   Byte  =  10  ; 




static   final   int   Word  =  11  ; 




static   final   int   Quad  =  12  ; 

















static   final   String  [  ]  encoding  =  {  "Imm"  ,  "Reg"  ,  "Cond"  ,  "Abs"  ,  "RegDisp"  ,  "RegOff"  ,  "RegIdx"  ,  "RegInd"  ,  "Label"  ,  "ImmOrLabel"  ,  "Byte"  ,  "Word"  ,  "Quad"  }  ; 











private   static   int   getEncoding  (  String   str  )  { 
for  (  int   i  =  0  ;  i  <  encoding  .  length  -  SIZES  ;  i  ++  )  if  (  encoding  [  i  ]  .  equals  (  str  )  )  return   i  ; 
return  -  1  ; 
} 











private   static   int   getSize  (  String   str  )  { 
for  (  int   i  =  encoding  .  length  -  SIZES  ;  i  <  encoding  .  length  ;  i  ++  )  if  (  encoding  [  i  ]  .  equals  (  str  )  )  return   i  ; 
return  -  1  ; 
} 













private   static   String   getOperand  (  int   op  )  { 
try  { 
if  (  currentOpcodeArgTable  ==  null  )  return   currentFormat  +  ".get"  +  currentOpcodeSymbolicNames  [  op  ]  +  "(inst)"  ;  else   return   currentFormat  +  ".get"  +  currentOpcodeSymbolicNames  [  currentOpcodeArgTable  [  op  ]  ]  +  "(inst)"  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
System  .  err  .  println  (  currentOpcode  +  ": cannot access operand "  +  op  +  ":"  )  ; 
for  (  int   i  =  0  ;  i  <  currentOpcodeSymbolicNames  .  length  ;  i  ++  )  System  .  err  .  println  (  currentOpcodeSymbolicNames  [  i  ]  )  ; 
System  .  exit  (  -  1  )  ; 
return   null  ; 
} 
} 












private   static   void   emitTest  (  int   argNumber  ,  int   argEncoding  )  { 
if  (  argEncoding  <  encoding  .  length  -  SIZES  )  emit  (  "is"  +  encoding  [  argEncoding  ]  +  "("  +  getOperand  (  argNumber  )  +  ")"  )  ;  else   emit  (  "is"  +  encoding  [  argEncoding  ]  +  "(inst)"  )  ; 
} 


















private   static   void   emitVerify  (  int   argNumber  ,  int   argEncoding  ,  int   level  )  { 
emitTab  (  level  )  ; 
emit  (  "if (VM.VerifyAssertions && !"  )  ; 
emitTest  (  argNumber  ,  argEncoding  )  ; 
emit  (  ") VM._assert(false, inst.toString());\n"  )  ; 
} 















private   static   void   emitArgs  (  int   argNumber  ,  int   argEncoding  )  { 
String   op  =  getOperand  (  argNumber  )  ; 
if  (  argEncoding  ==  LabelOrImmediate  )  emit  (  "getImm("  +  op  +  "), getLabel("  +  op  +  ")"  )  ;  else   if  (  argEncoding  ==  RegisterDisplacement  )  emit  (  "getBase("  +  op  +  "), getDisp("  +  op  +  ")"  )  ;  else   if  (  argEncoding  ==  Absolute  )  emit  (  "getDisp("  +  op  +  ")"  )  ;  else   if  (  argEncoding  ==  RegisterOffset  )  emit  (  "getIndex("  +  op  +  "), getScale("  +  op  +  "), getDisp("  +  op  +  ")"  )  ;  else   if  (  argEncoding  ==  RegisterIndexed  )  emit  (  "getBase("  +  op  +  "), getIndex("  +  op  +  "), getScale("  +  op  +  "), getDisp("  +  op  +  ")"  )  ;  else   if  (  argEncoding  ==  RegisterIndirect  )  emit  (  "getBase("  +  op  +  ")"  )  ;  else   emit  (  "get"  +  encoding  [  argEncoding  ]  +  "("  +  op  +  ")"  )  ; 
} 














static   class   BadEmitMethod   extends   RuntimeException  { 









BadEmitMethod  (  String   methodName  ,  String   code  )  { 
super  (  "cannot interpret method "  +  methodName  +  "("  +  code  +  ")"  )  ; 
} 
} 









































static   class   EmitterDescriptor  { 

private   int   size  ; 

private   int   count  ; 

private   final   int   args  [  ]  ; 








EmitterDescriptor  (  String   methodName  )  { 
StringTokenizer   toks  =  new   StringTokenizer  (  methodName  ,  "_"  )  ; 
toks  .  nextElement  (  )  ; 
args  =  new   int  [  toks  .  countTokens  (  )  ]  ; 
this  .  size  =  0  ; 
this  .  count  =  0  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
String   cs  =  toks  .  nextToken  (  )  ; 
int   code  =  getEncoding  (  cs  )  ; 
int   size  =  GenerateAssembler  .  getSize  (  cs  )  ; 
if  (  DEBUG  )  { 
System  .  err  .  println  (  methodName  +  "["  +  i  +  "] is "  +  code  +  ","  +  size  +  " for "  +  cs  )  ; 
} 
if  (  code  !=  -  1  )  args  [  count  ++  ]  =  code  ;  else   if  (  size  !=  -  1  )  this  .  size  =  size  ;  else   throw   new   BadEmitMethod  (  methodName  ,  cs  )  ; 
} 
} 









































boolean   argMatchesEncoding  (  int   argument  ,  int   enc  )  { 
if  (  enc  <  encoding  .  length  -  SIZES  )  return  (  count  >  argument  )  &&  args  [  argument  ]  ==  enc  ;  else   return   size  ==  enc  ; 
} 







int  [  ]  getArgs  (  )  { 
return   args  ; 
} 







int   getSize  (  )  { 
return   size  ; 
} 







int   getCount  (  )  { 
return   count  ; 
} 

public   String   toString  (  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
s  .  append  (  "ed:"  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  s  .  append  (  " "  +  encoding  [  args  [  i  ]  ]  )  ; 
if  (  size  !=  0  )  s  .  append  (  " ("  +  encoding  [  size  ]  +  ")"  )  ; 
return   s  .  toString  (  )  ; 
} 
} 











static   class   EmitterSet  { 





private   final   Set   emitters  =  new   HashSet  (  )  ; 





public   String   toString  (  )  { 
StringBuffer   s  =  new   StringBuffer  (  )  ; 
s  .  append  (  "Emitter Set of:\n"  )  ; 
Iterator   i  =  emitters  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  s  .  append  (  i  .  next  (  )  .  toString  (  )  +  "\n"  )  ; 
s  .  append  (  "-------------\n"  )  ; 
return   s  .  toString  (  )  ; 
} 





boolean   isSingleton  (  )  { 
return  (  emitters  .  size  (  )  ==  1  )  ; 
} 





void   add  (  EmitterDescriptor   ed  )  { 
emitters  .  add  (  ed  )  ; 
} 














private   int   countEncoding  (  int   n  ,  int   code  )  { 
Iterator   i  =  emitters  .  iterator  (  )  ; 
int   count  =  0  ; 
while  (  i  .  hasNext  (  )  )  if  (  (  (  EmitterDescriptor  )  i  .  next  (  )  )  .  argMatchesEncoding  (  n  ,  code  )  )  count  ++  ; 
return   count  ; 
} 













private   int   getEncodingSplit  (  int   n  ,  int   code  )  { 
int   count  =  countEncoding  (  n  ,  code  )  ; 
return   Math  .  abs  (  (  emitters  .  size  (  )  -  count  )  -  count  )  ; 
} 












static   class   SplitRecord  { 




int   argument  ; 




int   test  ; 








SplitRecord  (  int   argument  ,  int   test  )  { 
this  .  argument  =  argument  ; 
this  .  test  =  test  ; 
} 
} 







private   EmitterSet  [  ]  makeSplit  (  SplitRecord   split  )  { 
int   arg  =  split  .  argument  ; 
int   test  =  split  .  test  ; 
EmitterSet   yes  =  new   EmitterSet  (  )  ; 
EmitterSet   no  =  new   EmitterSet  (  )  ; 
Iterator   i  =  emitters  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
EmitterDescriptor   ed  =  (  EmitterDescriptor  )  i  .  next  (  )  ; 
if  (  ed  .  argMatchesEncoding  (  arg  ,  test  )  )  yes  .  add  (  ed  )  ;  else   no  .  add  (  ed  )  ; 
} 
return   new   EmitterSet  [  ]  {  yes  ,  no  }  ; 
} 










SplitRecord   split  (  )  { 
int   splitArg  =  -  1  ; 
int   splitTest  =  -  1  ; 
int   splitDiff  =  1000  ; 
for  (  int   arg  =  0  ;  arg  <  4  ;  arg  ++  )  { 
for  (  int   test  =  0  ;  test  <  encoding  .  length  ;  test  ++  )  { 
int   c  =  getEncodingSplit  (  arg  ,  test  )  ; 
if  (  c  ==  0  )  return   new   SplitRecord  (  arg  ,  test  )  ;  else   if  (  c  <  splitDiff  )  { 
splitArg  =  arg  ; 
splitTest  =  test  ; 
splitDiff  =  c  ; 
} 
} 
} 
return   new   SplitRecord  (  splitArg  ,  splitTest  )  ; 
} 
















private   void   emitEmitCall  (  String   opcode  ,  int  [  ]  args  ,  int   count  ,  int   level  ,  int   size  )  { 
emitTab  (  level  )  ; 
emit  (  "emit"  +  opcode  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  emit  (  "_"  +  encoding  [  args  [  i  ]  ]  )  ; 
if  (  size  !=  0  )  emit  (  "_"  +  encoding  [  size  ]  )  ; 
if  (  count  ==  0  )  emit  (  "();\n"  )  ;  else  { 
emit  (  "("  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
emit  (  "\n"  )  ; 
emitTab  (  level  +  1  )  ; 
emitArgs  (  i  ,  args  [  i  ]  )  ; 
if  (  i  ==  count  -  1  )  emit  (  ");\n"  )  ;  else   emit  (  ","  )  ; 
} 
} 
} 





















private   void   emitSingleton  (  String   opcode  ,  boolean  [  ]  [  ]  testsPerformed  ,  int   level  )  { 
EmitterDescriptor   ed  =  (  EmitterDescriptor  )  emitters  .  iterator  (  )  .  next  (  )  ; 
int  [  ]  args  =  ed  .  getArgs  (  )  ; 
int   count  =  ed  .  getCount  (  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  if  (  !  testsPerformed  [  i  ]  [  args  [  i  ]  ]  )  emitVerify  (  i  ,  args  [  i  ]  ,  level  )  ; 
int   size  =  ed  .  getSize  (  )  ; 
if  (  size  !=  0  )  { 
boolean   needed  =  true  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  if  (  testsPerformed  [  i  ]  [  size  ]  )  needed  =  false  ; 
if  (  needed  )  emitVerify  (  0  ,  size  ,  level  )  ; 
if  (  size  ==  Byte  )  for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  if  (  args  [  i  ]  ==  Register  )  if  (  currentOpcode  .  indexOf  (  "MOVZX"  )  ==  -  1  &&  currentOpcode  .  indexOf  (  "MOVSX"  )  ==  -  1  )  { 
emitTab  (  level  )  ; 
emit  (  "if (VM.VerifyAssertions && !("  )  ; 
emitArgs  (  i  ,  Register  )  ; 
emit  (  " < 4)) VM._assert(false, inst.toString());\n"  )  ; 
} 
} 
emitEmitCall  (  opcode  ,  args  ,  count  ,  level  ,  ed  .  getSize  (  )  )  ; 
} 


























private   void   emitSet  (  String   opcode  ,  boolean  [  ]  [  ]  testsPerformed  ,  int   level  )  { 
if  (  emitters  .  isEmpty  (  )  )  { 
}  else   if  (  isSingleton  (  )  )  emitSingleton  (  opcode  ,  testsPerformed  ,  level  )  ;  else  { 
SplitRecord   rec  =  split  (  )  ; 
if  (  DEBUG  )  { 
for  (  int   i  =  0  ;  i  <  level  ;  i  ++  )  System  .  err  .  print  (  "  "  )  ; 
System  .  err  .  println  (  "split of "  +  opcode  +  "["  +  rec  .  argument  +  "] for "  +  encoding  [  rec  .  test  ]  )  ; 
} 
if  (  testsPerformed  [  rec  .  argument  ]  [  rec  .  test  ]  ==  true  )  { 
System  .  err  .  println  (  "repeated split of "  +  opcode  +  "["  +  rec  .  argument  +  "] for "  +  encoding  [  rec  .  test  ]  )  ; 
System  .  err  .  println  (  this  )  ; 
System  .  exit  (  -  1  )  ; 
} 
testsPerformed  [  rec  .  argument  ]  [  rec  .  test  ]  =  true  ; 
EmitterSet  [  ]  splits  =  makeSplit  (  rec  )  ; 
emitTab  (  level  )  ; 
emit  (  "if ("  )  ; 
emitTest  (  rec  .  argument  ,  rec  .  test  )  ; 
emit  (  ") {\n"  )  ; 
splits  [  0  ]  .  emitSet  (  opcode  ,  testsPerformed  ,  level  +  1  )  ; 
emit  (  "\n"  )  ; 
emitTab  (  level  )  ; 
emit  (  "} else {\n"  )  ; 
splits  [  1  ]  .  emitSet  (  opcode  ,  testsPerformed  ,  level  +  1  )  ; 
emitTab  (  level  )  ; 
emit  (  "}\n"  )  ; 
testsPerformed  [  rec  .  argument  ]  [  rec  .  test  ]  =  false  ; 
} 
} 
} 







static   Class   lowLevelAsm  ; 








private   static   EmitterSet   buildSetForOpcode  (  Method  [  ]  emitters  ,  String   opcode  )  { 
EmitterSet   s  =  new   EmitterSet  (  )  ; 
for  (  int   i  =  0  ;  i  <  emitters  .  length  ;  i  ++  )  { 
Method   m  =  emitters  [  i  ]  ; 
if  (  m  .  getName  (  )  .  startsWith  (  "emit"  +  opcode  +  "_"  )  ||  m  .  getName  (  )  .  equals  (  "emit"  +  opcode  )  )  { 
s  .  add  (  new   EmitterDescriptor  (  m  .  getName  (  )  )  )  ; 
} 
} 
return   s  ; 
} 







private   static   Set   excludedOpcodes  ; 






static  { 
excludedOpcodes  =  new   HashSet  (  )  ; 
excludedOpcodes  .  add  (  "FSAVE"  )  ; 
excludedOpcodes  .  add  (  "FNSTSW"  )  ; 
excludedOpcodes  .  add  (  "FUCOMPP"  )  ; 
excludedOpcodes  .  add  (  "SAHF"  )  ; 
excludedOpcodes  .  add  (  "NOP"  )  ; 
excludedOpcodes  .  add  (  "ENTER"  )  ; 
} 











private   static   Set   getOpcodes  (  Method  [  ]  emitters  )  { 
Set   s  =  new   HashSet  (  )  ; 
for  (  int   i  =  0  ;  i  <  emitters  .  length  ;  i  ++  )  { 
String   name  =  emitters  [  i  ]  .  getName  (  )  ; 
if  (  DEBUG  )  System  .  out  .  println  (  name  )  ; 
if  (  name  .  startsWith  (  "emit"  )  )  { 
int   posOf_  =  name  .  indexOf  (  '_'  )  ; 
if  (  posOf_  !=  -  1  )  { 
String   opcode  =  name  .  substring  (  4  ,  posOf_  )  ; 
if  (  !  excludedOpcodes  .  contains  (  opcode  )  )  s  .  add  (  opcode  )  ; 
}  else  { 
String   opcode  =  name  .  substring  (  4  )  ; 
if  (  opcode  .  equals  (  opcode  .  toUpperCase  (  Locale  .  getDefault  (  )  )  )  )  if  (  !  excludedOpcodes  .  contains  (  opcode  )  )  s  .  add  (  opcode  )  ; 
} 
} 
} 
return   s  ; 
} 














private   static   Set   getErrorOpcodes  (  Set   emittedOpcodes  )  { 
Iterator   e  =  OPT_OperatorFormatTables  .  getOpcodes  (  )  ; 
Set   errorOpcodes  =  new   HashSet  (  )  ; 
while  (  e  .  hasNext  (  )  )  { 
String   opcode  =  (  String  )  e  .  next  (  )  ; 
if  (  !  emittedOpcodes  .  contains  (  opcode  )  )  errorOpcodes  .  add  (  opcode  )  ; 
} 
return   errorOpcodes  ; 
} 










private   static   Set   getMatchingOperators  (  String   lowLevelOpcode  )  { 
Iterator   e  =  OPT_OperatorFormatTables  .  getOpcodes  (  )  ; 
Set   matchingOperators  =  new   HashSet  (  )  ; 
while  (  e  .  hasNext  (  )  )  { 
String   o  =  (  String  )  e  .  next  (  )  ; 
if  (  o  .  equals  (  lowLevelOpcode  )  ||  o  .  startsWith  (  lowLevelOpcode  +  "$"  )  )  matchingOperators  .  add  (  o  )  ; 
} 
return   matchingOperators  ; 
} 




public   static   void   main  (  String  [  ]  args  )  { 
try  { 
out  =  new   FileWriter  (  System  .  getProperty  (  "generateToDir"  )  +  "/OPT_Assembler.java"  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
try  { 
lowLevelAsm  =  Class  .  forName  (  "com.ibm.JikesRVM.VM_Assembler"  )  ; 
}  catch  (  ClassNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
emit  (  "package com.ibm.JikesRVM.opt;\n\n"  )  ; 
emit  (  "import com.ibm.JikesRVM.*;\n\n"  )  ; 
emit  (  "import com.ibm.JikesRVM.opt.ir.*;\n\n"  )  ; 
emit  (  "\n\n"  )  ; 
emit  (  "/**\n"  )  ; 
emit  (  " *  This class is the automatically-generated assembler for\n"  )  ; 
emit  (  " * the optimizing compiler.  It consists of methods that\n"  )  ; 
emit  (  " * understand the possible operand combinations of each\n"  )  ; 
emit  (  " * instruction type, and how to translate those operands to\n"  )  ; 
emit  (  " * calls to the VM_Assember low-level emit method\n"  )  ; 
emit  (  " *\n"  )  ; 
emit  (  " * @see GenerateAssembler\n"  )  ; 
emit  (  " *\n"  )  ; 
emit  (  " * @author Julian Dolby\n"  )  ; 
emit  (  " * @author {@link GenerateAssembler}\n"  )  ; 
emit  (  " */\n"  )  ; 
emit  (  "class OPT_Assembler extends OPT_AssemblerBase {\n\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "/**\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *  This class requires no special construction;\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * this constructor simply invokes the\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * constructor for VM_Assembler\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * @see VM_Assembler\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " */\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "OPT_Assembler(int bcSize, boolean print) {\n"  )  ; 
emitTab  (  2  )  ; 
emit  (  "super(bcSize, print);\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "}"  )  ; 
emit  (  "\n\n"  )  ; 
Method  [  ]  emitters  =  lowLevelAsm  .  getDeclaredMethods  (  )  ; 
Set   opcodes  =  getOpcodes  (  emitters  )  ; 
Iterator   i  =  opcodes  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
String   opcode  =  (  String  )  i  .  next  (  )  ; 
setCurrentOpcode  (  opcode  )  ; 
emitTab  (  1  )  ; 
emit  (  "/**\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *  Emit the given instruction, assuming that\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * it is a "  +  currentFormat  +  " instruction\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * and has a "  +  currentOpcode  +  " operator\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * @param inst the instruction to assemble\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " */\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "private void do"  +  opcode  +  "(OPT_Instruction inst) {\n"  )  ; 
EmitterSet   emitter  =  buildSetForOpcode  (  emitters  ,  opcode  )  ; 
boolean  [  ]  [  ]  tp  =  new   boolean  [  4  ]  [  encoding  .  length  ]  ; 
emitter  .  emitSet  (  opcode  ,  tp  ,  2  )  ; 
emitTab  (  1  )  ; 
emit  (  "}\n\n"  )  ; 
} 
emitTab  (  1  )  ; 
emit  (  "/**\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *  The number of instructions emitted so far\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " */\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "private int instructionCount = 0;\n\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "/**\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *  Assemble the given instruction\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " *\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " * @param inst the instruction to assemble\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  " */\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "void doInst(OPT_Instruction inst) {\n"  )  ; 
emitTab  (  2  )  ; 
emit  (  "resolveForwardReferences(++instructionCount);\n"  )  ; 
emitTab  (  2  )  ; 
emit  (  "switch (inst.getOpcode()) {\n"  )  ; 
Set   emittedOpcodes  =  new   HashSet  (  )  ; 
i  =  opcodes  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
String   opcode  =  (  String  )  i  .  next  (  )  ; 
Iterator   operators  =  getMatchingOperators  (  opcode  )  .  iterator  (  )  ; 
while  (  operators  .  hasNext  (  )  )  { 
Object   operator  =  operators  .  next  (  )  ; 
emitTab  (  3  )  ; 
emittedOpcodes  .  add  (  operator  )  ; 
emit  (  "case IA32_"  +  operator  +  "_opcode:\n"  )  ; 
} 
emitTab  (  4  )  ; 
emit  (  "do"  +  opcode  +  "(inst);\n"  )  ; 
emitTab  (  4  )  ; 
emit  (  "break;\n"  )  ; 
} 
emittedOpcodes  .  add  (  "LOCK"  )  ; 
emitTab  (  3  )  ; 
emit  (  "case IA32_LOCK_opcode:\n"  )  ; 
emitTab  (  4  )  ; 
emit  (  "emitLockNextInstruction();\n"  )  ; 
emitTab  (  4  )  ; 
emit  (  "break;\n"  )  ; 
emittedOpcodes  .  add  (  "LOCK"  )  ; 
emitTab  (  3  )  ; 
emit  (  "case IG_PATCH_POINT_opcode:\n"  )  ; 
emitTab  (  4  )  ; 
emit  (  "emitPatchPoint();\n"  )  ; 
emitTab  (  4  )  ; 
emit  (  "break;\n"  )  ; 
Set   errorOpcodes  =  getErrorOpcodes  (  emittedOpcodes  )  ; 
if  (  !  errorOpcodes  .  isEmpty  (  )  )  { 
i  =  errorOpcodes  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
emitTab  (  3  )  ; 
emit  (  "case IA32_"  +  i  .  next  (  )  +  "_opcode:\n"  )  ; 
} 
emitTab  (  4  )  ; 
emit  (  "throw new OPT_OptimizingCompilerException(inst + \" has unimplemented IA32 opcode (check excludedOpcodes)\");\n"  )  ; 
} 
emitTab  (  2  )  ; 
emit  (  "}\n"  )  ; 
emitTab  (  2  )  ; 
emit  (  "inst.setmcOffset( mi );\n"  )  ; 
emitTab  (  1  )  ; 
emit  (  "}\n\n"  )  ; 
emit  (  "\n}\n"  )  ; 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 
} 

