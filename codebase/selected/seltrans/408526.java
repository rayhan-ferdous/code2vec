package   net  .  suberic  .  util  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 












public   class   VariableBundle   extends   Object  { 

private   Properties   properties  ; 

private   Properties   writableProperties  ; 

private   Properties   temporaryProperties  =  new   Properties  (  )  ; 

private   ResourceBundle   resources  ; 

private   VariableBundle   parentProperties  ; 

private   File   mSaveFile  ; 

private   Vector   removeList  =  new   Vector  (  )  ; 

private   Hashtable   VCListeners  =  new   Hashtable  (  )  ; 

private   Hashtable   VCGlobListeners  =  new   Hashtable  (  )  ; 

public   VariableBundle  (  InputStream   propertiesFile  ,  String   resourceFile  ,  VariableBundle   newParentProperties  )  { 
configure  (  propertiesFile  ,  resourceFile  ,  newParentProperties  )  ; 
} 

public   VariableBundle  (  File   propertiesFile  ,  VariableBundle   newParentProperties  )  throws   java  .  io  .  FileNotFoundException  { 
FileInputStream   fis  =  new   FileInputStream  (  propertiesFile  )  ; 
configure  (  fis  ,  null  ,  newParentProperties  )  ; 
try  { 
fis  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   ioe  )  { 
} 
mSaveFile  =  propertiesFile  ; 
} 

public   VariableBundle  (  InputStream   propertiesFile  ,  String   resourceFile  )  { 
this  (  propertiesFile  ,  resourceFile  ,  null  )  ; 
} 

public   VariableBundle  (  InputStream   propertiesFile  ,  VariableBundle   newParentProperties  )  { 
this  (  propertiesFile  ,  null  ,  newParentProperties  )  ; 
} 

public   VariableBundle  (  Properties   editableProperties  ,  VariableBundle   newParentProperties  )  { 
writableProperties  =  editableProperties  ; 
parentProperties  =  newParentProperties  ; 
properties  =  new   Properties  (  )  ; 
resources  =  null  ; 
} 




protected   void   configure  (  InputStream   propertiesFile  ,  String   resourceFile  ,  VariableBundle   newParentProperties  )  { 
writableProperties  =  new   Properties  (  )  ; 
if  (  resourceFile  !=  null  )  try  { 
resources  =  ResourceBundle  .  getBundle  (  resourceFile  ,  Locale  .  getDefault  (  )  )  ; 
}  catch  (  MissingResourceException   mre  )  { 
System  .  err  .  println  (  "Error loading resource "  +  mre  .  getClassName  (  )  +  mre  .  getKey  (  )  +  ":  trying default locale."  )  ; 
try  { 
resources  =  ResourceBundle  .  getBundle  (  resourceFile  ,  Locale  .  US  )  ; 
}  catch  (  MissingResourceException   mreTwo  )  { 
System  .  err  .  println  (  "Unable to load default (US) resource bundle; exiting."  )  ; 
System  .  exit  (  1  )  ; 
} 
}  else   resources  =  null  ; 
properties  =  new   Properties  (  )  ; 
if  (  propertiesFile  !=  null  )  try  { 
properties  .  load  (  propertiesFile  )  ; 
}  catch  (  java  .  io  .  IOException   ioe  )  { 
System  .  err  .  println  (  ioe  .  getMessage  (  )  +  ":  "  +  propertiesFile  )  ; 
} 
List   includeStreams  =  getPropertyAsList  (  "VariableBundle.include"  ,  ""  )  ; 
if  (  includeStreams  !=  null  &&  includeStreams  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  includeStreams  .  size  (  )  ;  i  ++  )  { 
String   current  =  (  String  )  includeStreams  .  get  (  i  )  ; 
try  { 
if  (  current  !=  null  &&  !  current  .  equals  (  ""  )  )  { 
java  .  net  .  URL   url  =  this  .  getClass  (  )  .  getResource  (  current  )  ; 
java  .  io  .  InputStream   is  =  url  .  openStream  (  )  ; 
properties  .  load  (  is  )  ; 
} 
}  catch  (  java  .  io  .  IOException   ioe  )  { 
System  .  err  .  println  (  "error including file "  +  current  +  ":  "  +  ioe  .  getMessage  (  )  )  ; 
ioe  .  printStackTrace  (  )  ; 
} 
} 
} 
parentProperties  =  newParentProperties  ; 
} 

public   String   getProperty  (  String   key  ,  String   defaultValue  )  { 
String   returnValue  ; 
returnValue  =  temporaryProperties  .  getProperty  (  key  ,  ""  )  ; 
if  (  returnValue  ==  ""  )  { 
returnValue  =  writableProperties  .  getProperty  (  key  ,  ""  )  ; 
if  (  returnValue  ==  ""  )  { 
returnValue  =  properties  .  getProperty  (  key  ,  ""  )  ; 
if  (  returnValue  ==  ""  )  { 
returnValue  =  getParentProperty  (  key  ,  ""  )  ; 
if  (  returnValue  ==  ""  )  { 
if  (  resources  !=  null  )  try  { 
returnValue  =  resources  .  getString  (  key  )  ; 
}  catch  (  MissingResourceException   mre  )  { 
returnValue  =  defaultValue  ; 
}  else   returnValue  =  defaultValue  ; 
} 
} 
} 
} 
return   returnValue  ; 
} 

public   String   getProperty  (  String   key  )  throws   MissingResourceException  { 
String   returnValue  ; 
returnValue  =  getProperty  (  key  ,  ""  )  ; 
if  (  returnValue  ==  ""  )  { 
throw   new   MissingResourceException  (  key  ,  ""  ,  key  )  ; 
} 
return   returnValue  ; 
} 

private   String   getParentProperty  (  String   key  ,  String   defaultValue  )  { 
if  (  parentProperties  ==  null  )  { 
return   defaultValue  ; 
}  else  { 
return   parentProperties  .  getProperty  (  key  ,  defaultValue  )  ; 
} 
} 

public   ResourceBundle   getResources  (  )  { 
return   resources  ; 
} 

public   void   setResourceBundle  (  ResourceBundle   newResources  )  { 
resources  =  newResources  ; 
} 

public   Properties   getProperties  (  )  { 
return   properties  ; 
} 

public   void   setProperties  (  Properties   newProperties  )  { 
properties  =  newProperties  ; 
} 

public   VariableBundle   getParentProperties  (  )  { 
return   parentProperties  ; 
} 

public   Properties   getWritableProperties  (  )  { 
return   writableProperties  ; 
} 

public   void   setProperty  (  String   propertyName  ,  String   propertyValue  )  { 
temporaryProperties  .  remove  (  propertyName  )  ; 
writableProperties  .  setProperty  (  propertyName  ,  propertyValue  )  ; 
if  (  propertyValue  ==  null  ||  propertyValue  .  equalsIgnoreCase  (  ""  )  )  { 
removeProperty  (  propertyName  )  ; 
}  else  { 
unRemoveProperty  (  propertyName  )  ; 
} 
fireValueChanged  (  propertyName  )  ; 
} 




public   void   setProperty  (  String   propertyName  ,  String   propertyValue  ,  boolean   temporary  )  { 
if  (  temporary  )  { 
temporaryProperties  .  setProperty  (  propertyName  ,  propertyValue  )  ; 
fireValueChanged  (  propertyName  )  ; 
}  else  { 
setProperty  (  propertyName  ,  propertyValue  )  ; 
} 
} 





public   Enumeration   getPropertyAsEnumeration  (  String   propertyName  ,  String   defaultValue  )  { 
StringTokenizer   tokens  =  new   StringTokenizer  (  getProperty  (  propertyName  ,  defaultValue  )  ,  ":"  )  ; 
return   tokens  ; 
} 





public   static   Vector   convertToVector  (  String   value  )  { 
Vector   returnValue  =  new   Vector  (  )  ; 
StringTokenizer   tokens  =  new   StringTokenizer  (  value  ,  ":"  )  ; 
while  (  tokens  .  hasMoreElements  (  )  )  returnValue  .  add  (  tokens  .  nextElement  (  )  )  ; 
return   returnValue  ; 
} 





public   Vector   getPropertyAsVector  (  String   propertyName  ,  String   defaultValue  )  { 
return   convertToVector  (  getProperty  (  propertyName  ,  defaultValue  )  )  ; 
} 





public   static   List   convertToList  (  String   value  )  { 
List   returnValue  =  new   ArrayList  (  )  ; 
StringTokenizer   tokens  =  new   StringTokenizer  (  value  ,  ":"  )  ; 
while  (  tokens  .  hasMoreElements  (  )  )  returnValue  .  add  (  tokens  .  nextElement  (  )  )  ; 
return   returnValue  ; 
} 





public   List   getPropertyAsList  (  String   propertyName  ,  String   defaultValue  )  { 
return   convertToList  (  getProperty  (  propertyName  ,  defaultValue  )  )  ; 
} 




public   static   String   convertToString  (  List   pValue  )  { 
if  (  pValue  ==  null  ||  pValue  .  size  (  )  ==  0  )  return  ""  ;  else  { 
StringBuffer   returnBuffer  =  new   StringBuffer  (  )  ; 
Iterator   it  =  pValue  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
returnBuffer  .  append  (  (  String  )  it  .  next  (  )  )  ; 
if  (  it  .  hasNext  (  )  )  { 
returnBuffer  .  append  (  ":"  )  ; 
} 
} 
return   returnBuffer  .  toString  (  )  ; 
} 
} 






public   void   saveProperties  (  )  { 
if  (  mSaveFile  !=  null  )  { 
saveProperties  (  mSaveFile  )  ; 
} 
} 






public   void   saveProperties  (  File   pSaveFile  )  { 
if  (  pSaveFile  ==  null  )  return  ; 
synchronized  (  this  )  { 
if  (  writableProperties  .  size  (  )  >  0  )  { 
File   outputFile  ; 
String   currentLine  ,  key  ; 
int   equalsLoc  ; 
try  { 
if  (  !  pSaveFile  .  exists  (  )  )  pSaveFile  .  createNewFile  (  )  ; 
outputFile  =  pSaveFile  .  createTempFile  (  pSaveFile  .  getName  (  )  ,  ".tmp"  ,  pSaveFile  .  getParentFile  (  )  )  ; 
BufferedReader   readSaveFile  =  new   BufferedReader  (  new   FileReader  (  pSaveFile  )  )  ; 
BufferedWriter   writeSaveFile  =  new   BufferedWriter  (  new   FileWriter  (  outputFile  )  )  ; 
currentLine  =  readSaveFile  .  readLine  (  )  ; 
while  (  currentLine  !=  null  )  { 
equalsLoc  =  currentLine  .  indexOf  (  '='  )  ; 
if  (  equalsLoc  !=  -  1  )  { 
String   rawKey  =  currentLine  .  substring  (  0  ,  equalsLoc  )  ; 
key  =  unEscapeString  (  rawKey  )  ; 
if  (  !  propertyIsRemoved  (  key  )  )  { 
if  (  writableProperties  .  getProperty  (  key  ,  ""  )  .  equals  (  ""  )  )  { 
writeSaveFile  .  write  (  currentLine  )  ; 
writeSaveFile  .  newLine  (  )  ; 
}  else  { 
writeSaveFile  .  write  (  rawKey  +  "="  +  escapeWhiteSpace  (  writableProperties  .  getProperty  (  key  ,  ""  )  )  )  ; 
writeSaveFile  .  newLine  (  )  ; 
properties  .  setProperty  (  key  ,  writableProperties  .  getProperty  (  key  ,  ""  )  )  ; 
writableProperties  .  remove  (  key  )  ; 
} 
removeProperty  (  key  )  ; 
} 
}  else  { 
writeSaveFile  .  write  (  currentLine  )  ; 
writeSaveFile  .  newLine  (  )  ; 
} 
currentLine  =  readSaveFile  .  readLine  (  )  ; 
} 
Enumeration   propsLeft  =  writableProperties  .  keys  (  )  ; 
while  (  propsLeft  .  hasMoreElements  (  )  )  { 
String   nextKey  =  (  String  )  propsLeft  .  nextElement  (  )  ; 
String   nextKeyEscaped  =  escapeWhiteSpace  (  nextKey  )  ; 
String   nextValueEscaped  =  escapeWhiteSpace  (  writableProperties  .  getProperty  (  nextKey  ,  ""  )  )  ; 
writeSaveFile  .  write  (  nextKeyEscaped  +  "="  +  nextValueEscaped  )  ; 
writeSaveFile  .  newLine  (  )  ; 
properties  .  setProperty  (  nextKey  ,  writableProperties  .  getProperty  (  nextKey  ,  ""  )  )  ; 
writableProperties  .  remove  (  nextKey  )  ; 
} 
clearRemoveList  (  )  ; 
readSaveFile  .  close  (  )  ; 
writeSaveFile  .  flush  (  )  ; 
writeSaveFile  .  close  (  )  ; 
String   oldSaveName  =  pSaveFile  .  getAbsolutePath  (  )  +  ".old"  ; 
File   oldSave  =  new   File  (  oldSaveName  )  ; 
if  (  oldSave  .  exists  (  )  )  oldSave  .  delete  (  )  ; 
String   fileName  =  new   String  (  pSaveFile  .  getAbsolutePath  (  )  )  ; 
pSaveFile  .  renameTo  (  oldSave  )  ; 
outputFile  .  renameTo  (  new   File  (  fileName  )  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  getProperty  (  "VariableBundle.saveError"  ,  "Error saving properties file: "  +  pSaveFile  .  getName  (  )  +  ": "  +  e  .  getMessage  (  )  )  )  ; 
e  .  printStackTrace  (  System  .  err  )  ; 
} 
} 
} 
} 

private   String   loadConvert  (  String   theString  )  { 
char   aChar  ; 
int   len  =  theString  .  length  (  )  ; 
StringBuffer   outBuffer  =  new   StringBuffer  (  len  )  ; 
for  (  int   x  =  0  ;  x  <  len  ;  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
if  (  aChar  ==  '\\'  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
if  (  aChar  ==  'u'  )  { 
int   value  =  0  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
aChar  =  theString  .  charAt  (  x  ++  )  ; 
switch  (  aChar  )  { 
case  '0'  : 
case  '1'  : 
case  '2'  : 
case  '3'  : 
case  '4'  : 
case  '5'  : 
case  '6'  : 
case  '7'  : 
case  '8'  : 
case  '9'  : 
value  =  (  value  <<  4  )  +  aChar  -  '0'  ; 
break  ; 
case  'a'  : 
case  'b'  : 
case  'c'  : 
case  'd'  : 
case  'e'  : 
case  'f'  : 
value  =  (  value  <<  4  )  +  10  +  aChar  -  'a'  ; 
break  ; 
case  'A'  : 
case  'B'  : 
case  'C'  : 
case  'D'  : 
case  'E'  : 
case  'F'  : 
value  =  (  value  <<  4  )  +  10  +  aChar  -  'A'  ; 
break  ; 
default  : 
throw   new   IllegalArgumentException  (  "Malformed \\uxxxx encoding."  )  ; 
} 
} 
outBuffer  .  append  (  (  char  )  value  )  ; 
}  else  { 
if  (  aChar  ==  't'  )  aChar  =  '\t'  ;  else   if  (  aChar  ==  'r'  )  aChar  =  '\r'  ;  else   if  (  aChar  ==  'n'  )  aChar  =  '\n'  ;  else   if  (  aChar  ==  'f'  )  aChar  =  '\f'  ; 
outBuffer  .  append  (  aChar  )  ; 
} 
}  else   outBuffer  .  append  (  aChar  )  ; 
} 
return   outBuffer  .  toString  (  )  ; 
} 

private   String   saveConvert  (  String   theString  ,  boolean   escapeSpace  )  { 
int   len  =  theString  .  length  (  )  ; 
StringBuffer   outBuffer  =  new   StringBuffer  (  len  *  2  )  ; 
for  (  int   x  =  0  ;  x  <  len  ;  x  ++  )  { 
char   aChar  =  theString  .  charAt  (  x  )  ; 
switch  (  aChar  )  { 
case  ' '  : 
if  (  x  ==  0  ||  escapeSpace  )  outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  ' '  )  ; 
break  ; 
case  '\\'  : 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  '\\'  )  ; 
break  ; 
case  '\t'  : 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  't'  )  ; 
break  ; 
case  '\n'  : 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  'n'  )  ; 
break  ; 
case  '\r'  : 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  'r'  )  ; 
break  ; 
case  '\f'  : 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  'f'  )  ; 
break  ; 
default  : 
if  (  (  aChar  <  0x0020  )  ||  (  aChar  >  0x007e  )  )  { 
outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  'u'  )  ; 
outBuffer  .  append  (  toHex  (  (  aChar  >  >  12  )  &  0xF  )  )  ; 
outBuffer  .  append  (  toHex  (  (  aChar  >  >  8  )  &  0xF  )  )  ; 
outBuffer  .  append  (  toHex  (  (  aChar  >  >  4  )  &  0xF  )  )  ; 
outBuffer  .  append  (  toHex  (  aChar  &  0xF  )  )  ; 
}  else  { 
if  (  specialSaveChars  .  indexOf  (  aChar  )  !=  -  1  )  outBuffer  .  append  (  '\\'  )  ; 
outBuffer  .  append  (  aChar  )  ; 
} 
} 
} 
return   outBuffer  .  toString  (  )  ; 
} 





public   String   escapeWhiteSpace  (  String   sourceString  )  { 
return   saveConvert  (  sourceString  ,  true  )  ; 
} 




public   String   unEscapeString  (  String   sourceString  )  { 
return   loadConvert  (  sourceString  )  ; 
} 





public   void   clearRemoveList  (  )  { 
removeList  .  clear  (  )  ; 
} 






public   void   removeProperty  (  String   remProp  )  { 
if  (  !  propertyIsRemoved  (  remProp  )  )  removeList  .  add  (  remProp  )  ; 
} 







public   void   unRemoveProperty  (  String   unRemProp  )  { 
for  (  int   i  =  removeList  .  size  (  )  -  1  ;  i  >=  0  ;  i  --  )  { 
if  (  (  (  String  )  removeList  .  elementAt  (  i  )  )  .  equals  (  unRemProp  )  )  removeList  .  removeElementAt  (  i  )  ; 
} 
} 





public   boolean   propertyIsRemoved  (  String   prop  )  { 
if  (  removeList  .  size  (  )  <  1  )  return   false  ; 
for  (  int   i  =  0  ;  i  <  removeList  .  size  (  )  ;  i  ++  )  { 
if  (  (  (  String  )  removeList  .  elementAt  (  i  )  )  .  equals  (  prop  )  )  return   true  ; 
} 
return   false  ; 
} 





public   void   fireValueChanged  (  String   changedValue  )  { 
Set   notified  =  new   HashSet  (  )  ; 
Vector   listeners  =  (  Vector  )  VCListeners  .  get  (  changedValue  )  ; 
if  (  listeners  !=  null  &&  listeners  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  listeners  .  size  (  )  ;  i  ++  )  { 
(  (  ValueChangeListener  )  listeners  .  elementAt  (  i  )  )  .  valueChanged  (  changedValue  )  ; 
notified  .  add  (  listeners  .  elementAt  (  i  )  )  ; 
} 
} 
Enumeration   keys  =  VCGlobListeners  .  keys  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
String   currentPattern  =  (  String  )  keys  .  nextElement  (  )  ; 
if  (  changedValue  .  startsWith  (  currentPattern  )  )  { 
Vector   globListeners  =  (  Vector  )  VCGlobListeners  .  get  (  currentPattern  )  ; 
if  (  globListeners  !=  null  &&  globListeners  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  globListeners  .  size  (  )  ;  i  ++  )  { 
ValueChangeListener   currentListener  =  (  (  ValueChangeListener  )  globListeners  .  elementAt  (  i  )  )  ; 
if  (  !  notified  .  contains  (  currentListener  )  )  { 
currentListener  .  valueChanged  (  changedValue  )  ; 
notified  .  add  (  currentListener  )  ; 
} 
} 
} 
} 
} 
} 





public   void   addValueChangeListener  (  ValueChangeListener   vcl  ,  String   property  )  { 
if  (  property  .  endsWith  (  "*"  )  )  { 
String   startProperty  =  property  .  substring  (  0  ,  property  .  length  (  )  -  1  )  ; 
Vector   listeners  =  (  Vector  )  VCGlobListeners  .  get  (  startProperty  )  ; 
if  (  listeners  ==  null  )  { 
listeners  =  new   Vector  (  )  ; 
listeners  .  add  (  vcl  )  ; 
VCGlobListeners  .  put  (  startProperty  ,  listeners  )  ; 
}  else  { 
if  (  !  listeners  .  contains  (  vcl  )  )  listeners  .  add  (  vcl  )  ; 
} 
}  else  { 
Vector   listeners  =  (  Vector  )  VCListeners  .  get  (  property  )  ; 
if  (  listeners  ==  null  )  { 
listeners  =  new   Vector  (  )  ; 
listeners  .  add  (  vcl  )  ; 
VCListeners  .  put  (  property  ,  listeners  )  ; 
}  else  { 
if  (  !  listeners  .  contains  (  vcl  )  )  listeners  .  add  (  vcl  )  ; 
} 
} 
} 





public   void   removeValueChangeListener  (  ValueChangeListener   vcl  )  { 
Enumeration   keys  =  VCListeners  .  keys  (  )  ; 
Vector   currentListenerList  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
currentListenerList  =  (  Vector  )  VCListeners  .  get  (  keys  .  nextElement  (  )  )  ; 
while  (  currentListenerList  !=  null  &&  currentListenerList  .  contains  (  vcl  )  )  currentListenerList  .  remove  (  vcl  )  ; 
} 
keys  =  VCGlobListeners  .  keys  (  )  ; 
while  (  keys  .  hasMoreElements  (  )  )  { 
currentListenerList  =  (  Vector  )  VCGlobListeners  .  get  (  keys  .  nextElement  (  )  )  ; 
while  (  currentListenerList  !=  null  &&  currentListenerList  .  contains  (  vcl  )  )  currentListenerList  .  remove  (  vcl  )  ; 
} 
} 





public   void   removeValueChangeListener  (  ValueChangeListener   vcl  ,  String   property  )  { 
Vector   currentListenerList  ; 
currentListenerList  =  (  Vector  )  VCListeners  .  get  (  property  )  ; 
while  (  currentListenerList  !=  null  &&  currentListenerList  .  contains  (  vcl  )  )  currentListenerList  .  remove  (  vcl  )  ; 
currentListenerList  =  (  Vector  )  VCGlobListeners  .  get  (  property  )  ; 
while  (  currentListenerList  !=  null  &&  currentListenerList  .  contains  (  vcl  )  )  currentListenerList  .  remove  (  vcl  )  ; 
} 




public   Map   getAllListeners  (  )  { 
HashMap   returnValue  =  new   HashMap  (  VCListeners  )  ; 
returnValue  .  putAll  (  VCGlobListeners  )  ; 
return   returnValue  ; 
} 





private   static   char   toHex  (  int   nibble  )  { 
return   hexDigit  [  (  nibble  &  0xF  )  ]  ; 
} 


private   static   final   char  [  ]  hexDigit  =  {  '0'  ,  '1'  ,  '2'  ,  '3'  ,  '4'  ,  '5'  ,  '6'  ,  '7'  ,  '8'  ,  '9'  ,  'A'  ,  'B'  ,  'C'  ,  'D'  ,  'E'  ,  'F'  }  ; 

private   static   final   String   keyValueSeparators  =  "=: \t\r\n\f"  ; 

private   static   final   String   strictKeyValueSeparators  =  "=:"  ; 

private   static   final   String   specialSaveChars  =  "=: \t\r\n\f#!"  ; 

private   static   final   String   whiteSpaceChars  =  " \t\r\n\f"  ; 




public   File   getSaveFile  (  )  { 
return   mSaveFile  ; 
} 




public   void   setSaveFile  (  File   newFile  )  { 
mSaveFile  =  newFile  ; 
} 
} 

