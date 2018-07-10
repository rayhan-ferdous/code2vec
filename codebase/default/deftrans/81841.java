import   java  .  awt  .  *  ; 
import   javax  .  swing  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  *  ; 
import   O2PlibSS  .  gui  .  *  ; 


















































































public   class   UtilCM  { 


public   static   int   defaultInt  =  0  ; 


public   static   float   defaultFloat  =  0.0F  ; 



public   static   boolean   loggingFlag  ; 


public   static   volatile   String   loggingStr  ; 




public   static   volatile   JTextArea   rptTextArea  =  null  ; 




public   static   volatile   PopupTextViewer   ptViewer  =  null  ; 




public   static   volatile   JTextField   rptStatusLineTextField  =  null  ; 




public   static   volatile   JLabel   rptStatusLineLabel  =  null  ; 


public   static   final   int   MAX_DEBUGS  =  10  ; 


public   static   int   nDebugPairs  =  0  ; 


public   static   String   debugName  [  ]  =  null  ; 


public   static   String   debugValue  [  ]  =  null  ; 








public   UtilCM  (  )  { 
if  (  debugName  ==  null  )  clearDebugNameValuePairs  (  )  ; 
} 





public   UtilCM  (  boolean   loggingFlag  )  { 
UtilCM  .  loggingFlag  =  loggingFlag  ; 
UtilCM  .  loggingStr  =  ""  ; 
if  (  debugName  ==  null  )  clearDebugNameValuePairs  (  )  ; 
} 




public   static   void   clearDebugNameValuePairs  (  )  { 
debugName  =  new   String  [  MAX_DEBUGS  ]  ; 
debugValue  =  new   String  [  MAX_DEBUGS  ]  ; 
nDebugPairs  =  0  ; 
} 








public   static   boolean   setDebugNameValue  (  String   name  ,  String   value  )  { 
int   n  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  nDebugPairs  ;  i  ++  )  if  (  name  .  equals  (  debugName  [  i  ]  )  )  { 
n  =  i  ; 
break  ; 
} 
if  (  n  ==  -  1  )  { 
if  (  nDebugPairs  <  MAX_DEBUGS  )  { 
n  =  nDebugPairs  ; 
nDebugPairs  ++  ; 
}  else   return  (  false  )  ; 
} 
debugName  [  n  ]  =  name  ; 
debugValue  [  n  ]  =  value  ; 
return  (  true  )  ; 
} 








public   static   String   getDebugValue  (  String   name  )  { 
for  (  int   i  =  0  ;  i  <  nDebugPairs  ;  i  ++  )  if  (  name  .  equals  (  debugName  [  i  ]  )  )  return  (  debugValue  [  i  ]  )  ; 
return  (  ""  )  ; 
} 










public   static   boolean   testDebugValue  (  String   dbugName  ,  String   testValue  )  { 
for  (  int   i  =  0  ;  i  <  nDebugPairs  ;  i  ++  )  if  (  dbugName  .  equals  (  debugName  [  i  ]  )  )  { 
String   dbugVal  =  debugValue  [  i  ]  ; 
boolean   flag  =  (  testValue  .  indexOf  (  dbugVal  )  !=  -  1  )  ; 
return  (  flag  )  ; 
} 
return  (  false  )  ; 
} 





public   static   void   setLogMsgs  (  boolean   loggingFlag  )  { 
UtilCM  .  loggingFlag  =  loggingFlag  ; 
UtilCM  .  loggingStr  =  ""  ; 
} 





public   static   String   getLogMsgs  (  )  { 
return  (  UtilCM  .  loggingStr  )  ; 
} 





public   static   void   logMsg  (  String   msg  )  { 
if  (  msg  ==  null  )  return  ; 
System  .  out  .  print  (  msg  )  ; 
if  (  loggingFlag  )  loggingStr  +=  msg  ; 
if  (  rptTextArea  !=  null  )  appendPRmsg  (  msg  )  ; 
} 







public   static   void   setReportTextArea  (  JTextArea   rptTxtArea  )  { 
rptTextArea  =  rptTxtArea  ; 
} 







public   static   void   setBigReportTextViewer  (  PopupTextViewer   pTxtViewer  )  { 
ptViewer  =  pTxtViewer  ; 
} 








public   static   void   setReportStatusLineTextField  (  JTextField   rptStatusLineTxtField  )  { 
rptStatusLineTextField  =  rptStatusLineTxtField  ; 
} 








public   static   void   setReportStatusLineLabel  (  JLabel   rptStatusLineLbl  )  { 
rptStatusLineLabel  =  rptStatusLineLbl  ; 
} 








public   static   void   appendPRmsg  (  String   msg  )  { 
if  (  rptTextArea  ==  null  ||  msg  ==  null  )  return  ; 
String   rptStr  =  ""  ; 
int   rptStrLth  =  0  ; 
if  (  rptTextArea  !=  null  )  { 
rptTextArea  .  append  (  msg  )  ; 
rptStr  =  rptTextArea  .  getText  (  )  ; 
rptStrLth  =  rptStr  .  length  (  )  ; 
rptTextArea  .  setCaretPosition  (  rptStrLth  )  ; 
} 
if  (  ptViewer  !=  null  )  { 
ptViewer  .  appendMsg  (  msg  )  ; 
} 
if  (  rptTextArea  ==  null  )  logMsg  (  msg  )  ; 
} 








public   static   void   updateReportStatusLine  (  String   msg  )  { 
if  (  rptStatusLineTextField  !=  null  &&  msg  !=  null  )  rptStatusLineTextField  .  setText  (  msg  )  ; 
if  (  rptStatusLineLabel  !=  null  &&  msg  !=  null  )  rptStatusLineLabel  .  setText  (  msg  )  ; 
} 





public   static   String   dateStr  (  )  { 
Date   dateObj  =  new   Date  (  )  ; 
String   date  =  dateObj  .  toString  (  )  ; 
return  (  date  )  ; 
} 





public   static   String   timeStr  (  )  { 
Calendar   cal  =  Calendar  .  getInstance  (  )  ; 
int   hrs  =  cal  .  get  (  Calendar  .  HOUR_OF_DAY  )  ,  mins  =  cal  .  get  (  Calendar  .  MINUTE  )  ,  secs  =  cal  .  get  (  Calendar  .  SECOND  )  ; 
String   dayTime  =  hrs  +  ":"  +  mins  +  ":"  +  secs  ; 
return  (  dayTime  )  ; 
} 






public   static   String   getCurTimeStr  (  )  { 
GregorianCalendar   cal  =  new   GregorianCalendar  (  )  ; 
int   hh  =  cal  .  get  (  Calendar  .  HOUR_OF_DAY  )  ,  min  =  cal  .  get  (  Calendar  .  MINUTE  )  ,  ss  =  cal  .  get  (  Calendar  .  SECOND  )  ; 
String   sH  ,  sMin  ,  sS  ; 
Integer   i  ; 
if  (  hh  <  10  )  { 
i  =  new   Integer  (  hh  )  ; 
sH  =  "0"  +  i  .  toString  (  )  ; 
}  else  { 
i  =  new   Integer  (  hh  )  ; 
sH  =  i  .  toString  (  )  ; 
} 
if  (  min  <  10  )  { 
i  =  new   Integer  (  min  )  ; 
sMin  =  "0"  +  i  .  toString  (  )  ; 
}  else  { 
i  =  new   Integer  (  min  )  ; 
sMin  =  i  .  toString  (  )  ; 
} 
if  (  ss  <  10  )  { 
i  =  new   Integer  (  ss  )  ; 
sS  =  "0"  +  i  .  toString  (  )  ; 
}  else  { 
i  =  new   Integer  (  ss  )  ; 
sS  =  i  .  toString  (  )  ; 
} 
String   date  =  sH  +  ":"  +  sMin  +  ":"  +  sS  ; 
return  (  date  )  ; 
} 







public   static   String   mapCRLF2space  (  String   s  )  { 
if  (  s  ==  null  )  return  (  null  )  ; 
String   sR  =  ""  ; 
char   ch  ,  ch2  =  0  ,  sBuf  [  ]  =  s  .  toCharArray  (  )  ; 
int   sSize  =  s  .  length  (  )  ,  sCtr  =  0  ; 
while  (  sCtr  <  sSize  )  { 
ch  =  sBuf  [  sCtr  ++  ]  ; 
if  (  sCtr  <  sSize  )  ch2  =  sBuf  [  sCtr  ]  ;  else   ch2  =  0  ; 
if  (  (  ch  ==  '\r'  &&  ch2  ==  '\n'  )  ||  (  ch2  ==  '\r'  &&  ch  ==  '\n'  )  )  { 
ch  =  ' '  ; 
sCtr  ++  ; 
}  else   if  (  ch  ==  '\r'  ||  ch  ==  '\n'  )  ch  =  ' '  ; 
sR  +=  (  ""  +  ch  )  ; 
} 
return  (  sR  )  ; 
} 






public   static   String   mapCRLForCR2LF  (  String   rawData  )  { 
if  (  rawData  ==  null  ||  rawData  .  length  (  )  ==  0  )  return  (  rawData  )  ; 
char   ch  =  0  ,  chP1  =  0  ,  inputBuf  [  ]  =  rawData  .  toCharArray  (  )  ; 
int   xStart  =  0  ,  xOut  =  0  ,  bufSize  =  inputBuf  .  length  ; 
char   outBuf  [  ]  =  new   char  [  bufSize  ]  ; 
while  (  xStart  <  bufSize  )  { 
ch  =  inputBuf  [  xStart  ]  ; 
if  (  ch  ==  '\r'  &&  (  (  xStart  -  1  )  <  bufSize  )  )  { 
chP1  =  inputBuf  [  xStart  +  1  ]  ; 
if  (  chP1  ==  '\n'  )  { 
xStart  ++  ; 
} 
xStart  ++  ; 
outBuf  [  xOut  ++  ]  =  '\n'  ; 
}  else  { 
xStart  ++  ; 
outBuf  [  xOut  ++  ]  =  ch  ; 
} 
} 
String   sOut  =  new   String  (  outBuf  ,  0  ,  xOut  )  ; 
return  (  sOut  )  ; 
} 






public   static   String   mapComma2Tab  (  String   s  )  { 
if  (  s  ==  null  )  return  (  null  )  ; 
String   sR  =  ""  ; 
char   ch  ,  sBuf  [  ]  =  s  .  toCharArray  (  )  ; 
int   sSize  =  s  .  length  (  )  ,  sCtr  =  0  ; 
while  (  sCtr  <  sSize  )  { 
ch  =  sBuf  [  sCtr  ++  ]  ; 
if  (  ch  ==  ','  )  ch  =  '\t'  ; 
sR  +=  (  ""  +  ch  )  ; 
} 
return  (  sR  )  ; 
} 







static   String   mapIllegalChars  (  String   str  )  { 
String   sR  =  str  ; 
char   ch  ,  cBuf  [  ]  =  new   char  [  str  .  length  (  )  ]  ; 
for  (  int   i  =  str  .  length  (  )  -  1  ;  i  >=  0  ;  i  --  )  { 
ch  =  str  .  charAt  (  i  )  ; 
if  (  ch  ==  ' '  ||  ch  ==  '\t'  ||  ch  ==  ':'  ||  ch  ==  ';'  ||  ch  ==  '\"'  ||  ch  ==  ','  ||  ch  ==  '@'  ||  ch  ==  '*'  ||  ch  ==  '='  ||  ch  ==  '\''  )  ch  =  '_'  ; 
cBuf  [  i  ]  =  ch  ; 
} 
sR  =  new   String  (  cBuf  )  ; 
return  (  sR  )  ; 
} 






public   static   String   mapSpaceToMinus  (  String   str  )  { 
if  (  str  ==  null  )  return  (  null  )  ; 
String   sR  =  ""  ; 
char   ch  ,  sBuf  [  ]  =  str  .  toCharArray  (  )  ; 
int   sSize  =  str  .  length  (  )  ,  sCtr  =  0  ; 
while  (  sCtr  <  sSize  )  { 
ch  =  sBuf  [  sCtr  ++  ]  ; 
if  (  ch  ==  ' '  )  ch  =  '-'  ; 
sR  +=  (  ""  +  ch  )  ; 
} 
return  (  sR  )  ; 
} 





public   static   String   removeQuotes  (  String   line  )  { 
if  (  line  .  indexOf  (  '\"'  )  ==  -  1  )  return  (  line  )  ; 
int   sSize  =  line  .  length  (  )  ,  sCtr  =  0  ,  oCnt  =  0  ; 
char   ch  ,  sInBuf  [  ]  =  line  .  toCharArray  (  )  ,  sOutBuf  [  ]  =  new   char  [  sSize  ]  ; 
while  (  sCtr  <  sSize  )  { 
ch  =  sInBuf  [  sCtr  ++  ]  ; 
if  (  ch  !=  '\"'  )  sOutBuf  [  oCnt  ++  ]  =  ch  ; 
} 
line  =  new   String  (  sOutBuf  ,  0  ,  oCnt  )  ; 
return  (  line  )  ; 
} 







public   static   boolean   isEmptyArray  (  String   sList  [  ]  )  { 
if  (  sList  ==  null  )  return  (  true  )  ; 
int   lth  =  sList  .  length  ; 
for  (  int   i  =  0  ;  i  <  lth  ;  i  ++  )  if  (  sList  [  i  ]  !=  null  &&  sList  [  i  ]  .  length  (  )  >  0  )  return  (  false  )  ; 
return  (  true  )  ; 
} 






public   static   boolean   isNumber  (  String   str  )  { 
double   d  ; 
try  { 
Double   D  =  new   Double  (  str  )  ; 
d  =  D  .  doubleValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
return  (  false  )  ; 
} 
return  (  true  )  ; 
} 








public   static   String   cvf2s  (  float   v  ,  int   precision  )  { 
NumberFormat   nf  =  NumberFormat  .  getInstance  (  )  ; 
nf  .  setMaximumFractionDigits  (  precision  )  ; 
nf  .  setGroupingUsed  (  false  )  ; 
String   s  =  nf  .  format  (  v  )  ; 
return  (  s  )  ; 
} 








public   static   String   cvd2s  (  double   v  ,  int   precision  )  { 
NumberFormat   nf  =  NumberFormat  .  getInstance  (  )  ; 
nf  .  setMaximumFractionDigits  (  precision  )  ; 
nf  .  setGroupingUsed  (  false  )  ; 
String   s  =  nf  .  format  (  v  )  ; 
return  (  s  )  ; 
} 






public   static   double   cvs2d  (  String   str  )  { 
double   d  ; 
try  { 
Double   D  =  new   Double  (  str  )  ; 
d  =  D  .  doubleValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
d  =  (  double  )  defaultFloat  ; 
} 
return  (  d  )  ; 
} 






public   static   float   cvs2f  (  String   str  )  { 
float   f  ; 
try  { 
Float   F  =  new   Float  (  str  )  ; 
f  =  F  .  floatValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
f  =  defaultFloat  ; 
} 
return  (  f  )  ; 
} 







public   static   float   cvs2f  (  String   str  ,  float   defVal  )  { 
float   f  ; 
try  { 
Float   F  =  new   Float  (  str  )  ; 
f  =  F  .  floatValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
f  =  defVal  ; 
} 
return  (  f  )  ; 
} 






public   static   int   cvs2i  (  String   str  )  { 
int   i  ; 
try  { 
i  =  java  .  lang  .  Integer  .  parseInt  (  str  )  ; 
}  catch  (  NumberFormatException   e  )  { 
i  =  defaultInt  ; 
} 
return  (  i  )  ; 
} 







public   static   int   cvs2i  (  String   str  ,  int   defVal  )  { 
int   i  ; 
try  { 
i  =  java  .  lang  .  Integer  .  parseInt  (  str  )  ; 
}  catch  (  NumberFormatException   e  )  { 
i  =  defVal  ; 
} 
return  (  i  )  ; 
} 






public   static   long   cvs2long  (  String   str  )  { 
long   i  ; 
try  { 
i  =  java  .  lang  .  Long  .  parseLong  (  str  )  ; 
}  catch  (  NumberFormatException   e  )  { 
i  =  defaultInt  ; 
} 
return  (  i  )  ; 
} 






public   static   String   cvb2s  (  boolean   b  )  { 
if  (  b  )  return  (  "true"  )  ;  else   return  (  "false"  )  ; 
} 






public   static   long   cvs2l  (  String   str  ,  long   defaultLong  )  { 
if  (  str  ==  null  )  return  (  defaultLong  )  ; 
long   lV  =  defaultLong  ; 
if  (  str  !=  null  )  { 
try  { 
Long   L  =  new   Long  (  str  )  ; 
lV  =  L  .  longValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
lV  =  defaultLong  ; 
} 
return  (  lV  )  ; 
} 
return  (  lV  )  ; 
} 






public   static   boolean   cvs2b  (  String   str  ,  boolean   defaultBool  )  { 
if  (  str  ==  null  )  return  (  defaultBool  )  ; 
boolean   b  =  defaultBool  ; 
if  (  str  .  equals  (  "TRUE"  )  ||  str  .  equals  (  "true"  )  ||  str  .  equals  (  "T"  )  ||  str  .  equals  (  "t"  )  ||  str  .  equals  (  "1"  )  )  b  =  true  ; 
return  (  b  )  ; 
} 















public   static   String  [  ]  cvs2ArrayNullFill  (  String   str  ,  String   delimChr  ,  String   nullFillStr  )  { 
String   tokArray  [  ]  =  cvs2Array  (  str  ,  delimChr  )  ; 
if  (  tokArray  ==  null  ||  nullFillStr  ==  null  )  return  (  tokArray  )  ; 
int   nTokens  =  tokArray  .  length  ; 
for  (  int   i  =  0  ;  i  <  nTokens  ;  i  ++  )  if  (  tokArray  [  i  ]  ==  null  )  tokArray  [  i  ]  =  nullFillStr  ; 
return  (  tokArray  )  ; 
} 











public   static   String  [  ]  cvs2Array  (  String   str  ,  String   delimChr  )  { 
if  (  str  ==  null  ||  delimChr  ==  null  )  return  (  null  )  ; 
if  (  !  delimChr  .  equals  (  "\r"  )  )  str  =  rmvSpecifiedChar  (  str  ,  '\r'  )  ; 
if  (  !  delimChr  .  equals  (  "\n"  )  )  str  =  rmvSpecifiedChar  (  str  ,  '\n'  )  ; 
int   delimCnt  =  0  ,  count  =  0  ,  strLen  =  str  .  length  (  )  ; 
char   delim  =  delimChr  .  charAt  (  0  )  ,  searchArray  [  ]  =  str  .  toCharArray  (  )  ; 
boolean   insideQuoteFlag  =  false  ; 
while  (  count  <  strLen  )  { 
if  (  searchArray  [  count  ++  ]  ==  delim  )  delimCnt  ++  ; 
} 
delimCnt  ++  ; 
String   token  ,  tokArray  [  ]  =  new   String  [  delimCnt  ]  ; 
char   ch  ,  lineBuf  [  ]  =  str  .  toCharArray  (  )  ,  tokBuf  [  ]  =  new   char  [  500  ]  ; 
int   tokBufSize  =  tokBuf  .  length  ,  bufSize  =  strLen  ,  bufCtr  =  0  ; 
int   nTokens  =  0  ; 
for  (  int   c  =  0  ;  c  <  delimCnt  ;  c  ++  )  { 
int   lastNonSpaceTokCtr  =  0  ,  tokCtr  =  0  ; 
while  (  bufCtr  <  bufSize  &&  (  lineBuf  [  bufCtr  ]  !=  delim  ||  insideQuoteFlag  )  )  { 
ch  =  lineBuf  [  bufCtr  ++  ]  ; 
if  (  ch  ==  '\"'  )  insideQuoteFlag  =  !  insideQuoteFlag  ;  else  { 
if  (  tokCtr  >=  (  tokBufSize  -  1  )  )  { 
int   newTokBufSize  =  (  int  )  (  2.0  *  tokBufSize  )  ; 
char   newTokBuf  [  ]  =  new   char  [  newTokBufSize  ]  ; 
for  (  int   k  =  0  ;  k  <  tokCtr  ;  k  ++  )  newTokBuf  [  k  ]  =  tokBuf  [  k  ]  ; 
tokBuf  =  newTokBuf  ; 
tokBufSize  =  newTokBufSize  ; 
} 
tokBuf  [  tokCtr  ++  ]  =  ch  ; 
} 
lastNonSpaceTokCtr  =  tokCtr  ; 
} 
tokBuf  [  tokCtr  ]  =  '\0'  ; 
token  =  new   String  (  tokBuf  ,  0  ,  tokCtr  )  ; 
token  =  token  .  substring  (  0  ,  lastNonSpaceTokCtr  )  ; 
tokArray  [  nTokens  ++  ]  =  token  ; 
if  (  bufCtr  <  bufSize  &&  lineBuf  [  bufCtr  ]  ==  delim  )  bufCtr  ++  ; 
} 
if  (  tokArray  .  length  >  nTokens  )  { 
String   newTokArray  [  ]  =  new   String  [  nTokens  ]  ; 
for  (  int   i  =  0  ;  i  <  nTokens  ;  i  ++  )  newTokArray  [  i  ]  =  tokArray  [  i  ]  ; 
tokArray  =  newTokArray  ; 
} 
return  (  tokArray  )  ; 
} 












public   static   String  [  ]  rightSizeArray  (  String   strArray  [  ]  ,  int   requiredSize  ,  String   fillStr  )  { 
if  (  strArray  ==  null  ||  requiredSize  <=  0  )  return  (  null  )  ; 
if  (  strArray  .  length  ==  requiredSize  )  return  (  strArray  )  ; 
String   sRtn  [  ]  =  new   String  [  requiredSize  ]  ; 
for  (  int   i  =  0  ;  i  <  requiredSize  ;  i  ++  )  sRtn  [  i  ]  =  (  i  <  strArray  .  length  )  ?  strArray  [  i  ]  :  fillStr  ; 
return  (  sRtn  )  ; 
} 






public   static   String  [  ]  cloneString  (  String   strArray  [  ]  )  { 
if  (  strArray  ==  null  )  return  (  null  )  ; 
int   lth  =  strArray  .  length  ; 
String   sRtn  [  ]  =  new   String  [  lth  ]  ; 
for  (  int   i  =  0  ;  i  <  lth  ;  i  ++  )  sRtn  [  i  ]  =  strArray  [  i  ]  ; 
return  (  sRtn  )  ; 
} 










public   static   int   lookupDataIdx  (  String   field  ,  String   strData  [  ]  ,  int   strDataLth  )  { 
if  (  field  ==  null  ||  strData  ==  null  )  return  (  -  1  )  ; 
if  (  strDataLth  <=  0  )  strDataLth  =  strData  .  length  ; 
int   idx  =  -  1  ; 
for  (  int   i  =  0  ;  i  <  strDataLth  ;  i  ++  )  if  (  field  .  equalsIgnoreCase  (  strData  [  i  ]  )  )  { 
idx  =  i  ; 
break  ; 
} 
return  (  idx  )  ; 
} 










public   static   boolean   isListInAnotherList  (  String   listA  [  ]  ,  int   lthA  ,  String   listB  [  ]  ,  int   lthB  )  { 
if  (  listA  ==  null  ||  listB  ==  null  )  return  (  false  )  ; 
if  (  lthA  <=  0  )  lthA  =  listA  .  length  ; 
if  (  lthB  <=  0  )  lthB  =  listB  .  length  ; 
boolean   flag  =  true  ; 
if  (  lthA  >  0  )  { 
for  (  int   i  =  0  ;  i  <  lthA  ;  i  ++  )  { 
String   strA  =  listA  [  i  ]  ; 
if  (  lookupDataIdx  (  strA  ,  listB  ,  lthB  )  ==  -  1  )  { 
flag  =  false  ; 
break  ; 
} 
} 
} 
return  (  flag  )  ; 
} 










public   static   String   mapAllkeywords  (  String   str  ,  String   keyList  [  ]  ,  String   toStrList  [  ]  ,  int   nMaps  )  { 
if  (  str  ==  null  ||  nMaps  ==  0  )  return  (  str  )  ; 
String   sPreface  =  null  ,  sKeyword  =  null  ,  sRest  =  null  ,  sToString  =  null  ,  sOld  =  ""  ,  sR  =  str  ; 
int   keywordLth  =  0  ,  prefaceIdx  =  -  1  ,  restIdx  =  -  1  ; 
while  (  !  sOld  .  equals  (  sR  )  )  { 
sOld  =  sR  ; 
for  (  int   i  =  0  ;  i  <  nMaps  ;  i  ++  )  { 
sKeyword  =  keyList  [  i  ]  ; 
prefaceIdx  =  sR  .  indexOf  (  sKeyword  )  ; 
if  (  prefaceIdx  ==  -  1  )  continue  ; 
keywordLth  =  sKeyword  .  length  (  )  ; 
restIdx  =  prefaceIdx  +  keywordLth  ; 
if  (  restIdx  >=  sR  .  length  (  )  )  sRest  =  ""  ;  else   sRest  =  sR  .  substring  (  restIdx  )  ; 
sPreface  =  sR  .  substring  (  0  ,  prefaceIdx  )  ; 
sToString  =  toStrList  [  i  ]  ; 
sR  =  sPreface  +  sToString  +  sRest  ; 
} 
} 
return  (  sR  )  ; 
} 






public   static   final   String   rmvRtnChars  (  String   str  )  { 
if  (  str  ==  null  )  return  (  null  )  ; 
int   lthOut  =  0  ,  lthIn  =  str  .  length  (  )  ,  lastIdx  =  lthIn  -  1  ; 
char   ch  ,  chLA  ,  cOut  [  ]  =  new   char  [  lthIn  ]  ,  cIn  [  ]  =  str  .  toCharArray  (  )  ; 
int   i  =  0  ; 
while  (  i  <  lthIn  )  { 
ch  =  cIn  [  i  ++  ]  ; 
if  (  ch  !=  '\r'  )  cOut  [  lthOut  ++  ]  =  ch  ;  else  { 
chLA  =  (  i  ==  lastIdx  )  ?  0  :  cIn  [  i  +  1  ]  ; 
if  (  chLA  ==  '\n'  )  i  +=  1  ; 
cOut  [  lthOut  ++  ]  =  '\n'  ; 
} 
} 
String   sR  =  new   String  (  cOut  ,  0  ,  lthOut  )  ; 
return  (  sR  )  ; 
} 







public   static   final   String   rmvSpecifiedChar  (  String   str  ,  char   rmvChar  )  { 
if  (  str  ==  null  )  return  (  null  )  ; 
if  (  rmvChar  ==  '\0'  )  return  (  str  )  ; 
int   lthOut  =  0  ,  lthIn  =  str  .  length  (  )  ; 
char   ch  ,  cOut  [  ]  =  new   char  [  lthIn  ]  ,  cIn  [  ]  =  str  .  toCharArray  (  )  ; 
int   i  =  0  ; 
while  (  i  <  lthIn  )  { 
ch  =  cIn  [  i  ++  ]  ; 
if  (  ch  !=  rmvChar  )  cOut  [  lthOut  ++  ]  =  ch  ; 
} 
String   sR  =  new   String  (  cOut  ,  0  ,  lthOut  )  ; 
return  (  sR  )  ; 
} 










public   static   final   String   rmvEnclosingWhitespace  (  String   str  )  { 
if  (  str  ==  null  ||  str  .  equals  (  ""  )  ||  str  .  equals  (  " "  )  )  return  (  str  )  ; 
int   lthIn  =  str  .  length  (  )  ,  leadingFirstNonBlankIdx  =  -  1  ,  endingFirstNonBlankIdx  =  -  1  ; 
char   cIn  [  ]  =  str  .  toCharArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  lthIn  ;  i  ++  )  { 
if  (  cIn  [  i  ]  ==  ' '  )  continue  ; 
leadingFirstNonBlankIdx  =  i  ; 
break  ; 
} 
for  (  int   i  =  lthIn  -  1  ;  i  >  -  1  ;  i  --  )  { 
if  (  cIn  [  i  ]  ==  ' '  )  continue  ; 
endingFirstNonBlankIdx  =  i  +  1  ; 
break  ; 
} 
String   sR  =  str  ; 
if  (  leadingFirstNonBlankIdx  !=  -  1  &&  endingFirstNonBlankIdx  !=  -  1  )  sR  =  str  .  substring  (  leadingFirstNonBlankIdx  ,  endingFirstNonBlankIdx  )  ; 
return  (  sR  )  ; 
} 









public   static   final   String  [  ]  trimArrayEnclWhitespace  (  String   rowData  [  ]  )  { 
if  (  rowData  !=  null  )  { 
int   lth  =  rowData  .  length  ; 
for  (  int   c  =  0  ;  c  <  lth  ;  c  ++  )  { 
if  (  rowData  [  c  ]  !=  null  )  rowData  [  c  ]  =  rowData  [  c  ]  .  trim  (  )  ; 
} 
} 
return  (  rowData  )  ; 
} 







public   static   final   String  [  ]  removeEmptyStringsFromArray  (  String   rowData  [  ]  )  { 
if  (  rowData  ==  null  )  return  (  null  )  ; 
int   lth  =  rowData  .  length  ; 
String   sTmp  ,  tmpList  [  ]  =  new   String  [  rowData  .  length  ]  ; 
int   nFound  =  0  ; 
for  (  int   c  =  0  ;  c  <  lth  ;  c  ++  )  if  (  rowData  [  c  ]  !=  null  )  { 
sTmp  =  rowData  [  c  ]  ; 
if  (  sTmp  ==  null  )  continue  ; 
sTmp  =  sTmp  .  trim  (  )  ; 
if  (  sTmp  .  equals  (  ""  )  )  continue  ; 
tmpList  [  nFound  ++  ]  =  sTmp  ; 
} 
String   trimmedData  [  ]  =  new   String  [  nFound  ]  ; 
for  (  int   c  =  0  ;  c  <  nFound  ;  c  ++  )  trimmedData  [  c  ]  =  tmpList  [  c  ]  ; 
return  (  trimmedData  )  ; 
} 







public   static   final   String  [  ]  getCleanArgList  (  String   strArg  )  { 
String   str  =  UtilCM  .  mapCRLF2space  (  strArg  )  ,  sCommaList  =  UtilCM  .  replaceSubstrInString  (  str  ,  " "  ,  ","  )  ,  sArgList  [  ]  =  cvs2Array  (  sCommaList  ,  ","  )  ,  sR  [  ]  =  removeEmptyStringsFromArray  (  sArgList  )  ; 
return  (  sR  )  ; 
} 








public   static   final   String   getCleanSpaceList  (  String   strArg  )  { 
String   str  =  UtilCM  .  mapCRLF2space  (  strArg  )  ,  str1  =  UtilCM  .  replaceSubstrInString  (  str  ,  ","  ,  " "  )  ; 
str  =  UtilCM  .  replaceSubstrInString  (  str1  ,  "\t"  ,  " "  )  ; 
String   sR  =  str  ; 
str  =  UtilCM  .  replaceSubstrInString  (  str  ,  "  "  ,  " "  )  ; 
while  (  sR  .  length  (  )  !=  str  .  length  (  )  )  { 
str  =  UtilCM  .  replaceSubstrInString  (  str1  ,  "  "  ,  " "  )  ; 
sR  =  str  ; 
} 
return  (  sR  )  ; 
} 











public   static   final   String   replaceSpaceWithCommaSpaceDelim  (  String   str  )  { 
String   sTmp  =  UtilCM  .  replaceSubstrInString  (  str  ,  " "  ,  "@#$*"  )  ,  sR  =  UtilCM  .  replaceSubstrInString  (  sTmp  ,  "@#$*"  ,  ", "  )  ; 
return  (  sR  )  ; 
} 









public   static   final   String   replaceSubstrInString  (  String   str  ,  String   oldToken  ,  String   newToken  )  { 
if  (  str  ==  null  ||  oldToken  ==  null  ||  newToken  ==  null  )  return  (  str  )  ; 
String   sR  =  str  ,  frontOfStr  =  null  ,  restOfStr  =  null  ; 
if  (  oldToken  !=  null  &&  oldToken  .  length  (  )  >  0  )  { 
int   idxOldToken  =  str  .  indexOf  (  oldToken  )  ; 
if  (  idxOldToken  !=  -  1  )  { 
int   idxRestOfStr  =  idxOldToken  +  oldToken  .  length  (  )  ; 
frontOfStr  =  str  .  substring  (  0  ,  idxOldToken  )  ; 
restOfStr  =  str  .  substring  (  idxRestOfStr  )  ; 
sR  =  frontOfStr  +  newToken  +  restOfStr  ; 
sR  =  replaceSubstrInString  (  sR  ,  oldToken  ,  newToken  )  ; 
} 
} 
return  (  sR  )  ; 
} 







public   static   String   changeWorkingDirectory  (  String   newDir  )  { 
try  { 
String   oldDir  =  System  .  getProperty  (  "user.dir"  )  ; 
System  .  setProperty  (  "user.dir"  ,  newDir  )  ; 
File   cur  =  new   File  (  "."  )  ; 
String   newDirCanonical  =  cur  .  getCanonicalPath  (  )  ; 
String   curDir  =  System  .  getProperty  (  "user.dir"  )  ; 
return  (  newDirCanonical  )  ; 
}  catch  (  Exception   e  )  { 
return  (  null  )  ; 
} 
} 











public   static   String   cvDeltaTime2str  (  long   deltaTimeMsec  ,  boolean   fullCvtFlag  )  { 
int   run  =  (  int  )  (  deltaTimeMsec  /  1000  )  ,  Rhours  =  run  /  3600  ,  Rminutes  =  (  run  -  (  Rhours  *  3600  )  )  /  60  ,  Rseconds  =  (  run  -  (  Rhours  *  3600  )  -  (  Rminutes  *  60  )  )  ; 
float   runMsec  =  (  float  )  deltaTimeMsec  ,  totRseconds  =  runMsec  /  1000.0F  ; 
String   secR  =  cvf2s  (  totRseconds  ,  1  )  +  " seconds"  ,  sR  =  secR  ; 
if  (  fullCvtFlag  )  sR  =  Rhours  +  ":"  +  Rminutes  +  ":"  +  Rseconds  +  " (H:M:S) or "  +  secR  ; 
return  (  sR  )  ; 
} 










public   static   String   off_timer  (  long   startT  )  { 
long   endTime  =  on_timer  (  )  ,  deltaTimeMsec  =  (  endTime  -  startT  )  ; 
String   sR  =  "Run time ="  +  cvDeltaTime2str  (  deltaTimeMsec  ,  true  )  +  "\n"  ; 
return  (  sR  )  ; 
} 





public   static   long   on_timer  (  )  { 
Date   dateObj  =  new   Date  (  )  ; 
long   time  =  dateObj  .  getTime  (  )  ; 
return  (  time  )  ; 
} 





public   static   synchronized   void   sleepMsec  (  int   mSec  )  { 
try  { 
Thread  .  sleep  (  mSec  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 








public   static   String  [  ]  appendElementToArray  (  String   sArray  [  ]  ,  String   sElement  )  { 
int   n  ; 
if  (  sArray  ==  null  )  { 
sArray  =  new   String  [  1  ]  ; 
n  =  1  ; 
}  else  { 
n  =  sArray  .  length  ; 
String   tmp  [  ]  =  new   String  [  n  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  tmp  [  i  ]  =  sArray  [  i  ]  ; 
sArray  =  tmp  ; 
n  ++  ; 
} 
sArray  [  n  -  1  ]  =  sElement  ; 
return  (  sArray  )  ; 
} 







public   static   boolean   startsWithIgnoreCase  (  String   str  ,  String   startStr  )  { 
String   strUC  =  str  .  toUpperCase  (  )  ,  startStrUC  =  startStr  .  toUpperCase  (  )  ; 
return  (  strUC  .  startsWith  (  startStrUC  )  )  ; 
} 







public   static   boolean   endsWithIgnoreCase  (  String   str  ,  String   endsStr  )  { 
String   strUC  =  str  .  toUpperCase  (  )  ,  endsStrUC  =  endsStr  .  toUpperCase  (  )  ; 
return  (  strUC  .  endsWith  (  endsStrUC  )  )  ; 
} 







public   static   int   indexOfIgnoreCase  (  String   str  ,  String   subStr  )  { 
String   strUC  =  str  .  toUpperCase  (  )  ,  subStrUC  =  subStr  .  toUpperCase  (  )  ; 
return  (  strUC  .  indexOf  (  subStrUC  )  )  ; 
} 







public   static   String   cvtHTMLcolorNameToHex  (  String   cName  )  { 
if  (  cName  .  equalsIgnoreCase  (  "black"  )  )  return  (  "#000000"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "sliver"  )  )  return  (  "#C0C0C0"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "gray"  )  )  return  (  "#808080"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "white"  )  )  return  (  "#FFFFFF"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "maroon"  )  )  return  (  "#800000"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "red"  )  )  return  (  "#FF0000"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "purple"  )  )  return  (  "#800080"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "fuscia"  )  )  return  (  "#FF00FF"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "green"  )  )  return  (  "#008000"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "lime"  )  )  return  (  "#00FF00"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "olive"  )  )  return  (  "#808000"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "yellow"  )  )  return  (  "#FFFF00"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "navy"  )  )  return  (  "#000080"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "blue"  )  )  return  (  "#0000FF"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "teal"  )  )  return  (  "#008080"  )  ;  else   if  (  cName  .  equalsIgnoreCase  (  "aqua"  )  )  return  (  "#00FFFF"  )  ; 
return  (  "cName"  )  ; 
} 


















public   static   boolean   updateJarFile  (  String   pgmName  ,  String   urlJarFile  ,  String   localJarFilePath  )  { 
String   fileSep  =  System  .  getProperty  (  "file.separator"  )  ,  userDir  =  System  .  getProperty  (  "user.dir"  )  +  fileSep  ,  localJarFile  =  userDir  +  pgmName  +  ".jar"  ,  localJarFileBkup  =  userDir  +  pgmName  +  ".jar.bkup"  ,  localJarFileTmp  =  userDir  +  pgmName  +  ".jar.tmp"  ; 
deleteLocalFile  (  localJarFileBkup  )  ; 
boolean   ok  =  copyFile  (  localJarFile  ,  localJarFileBkup  ,  null  ,  0  )  ; 
if  (  !  ok  )  return  (  false  )  ; 
String   updateMsg  =  "Updating your "  +  pgmName  +  ".jar file from "  +  urlJarFile  +  " server.\n"  ; 
File   f  =  new   File  (  localJarFileBkup  )  ; 
int   estInputFileLth  =  (  f  !=  null  )  ?  (  int  )  f  .  length  (  )  :  0  ; 
if  (  !  copyFile  (  urlJarFile  ,  localJarFileTmp  ,  updateMsg  ,  estInputFileLth  )  )  return  (  false  )  ; 
if  (  !  deleteLocalFile  (  localJarFile  )  )  return  (  false  )  ; 
if  (  !  copyFile  (  localJarFileTmp  ,  localJarFile  ,  null  ,  0  )  )  return  (  false  )  ; 
return  (  true  )  ; 
} 













public   static   boolean   copyFile  (  String   srcName  ,  String   dstName  ,  String   optUpdateMsg  ,  int   optEstInputFileLth  )  { 
try  { 
FileOutputStream   dstFOS  =  new   FileOutputStream  (  new   File  (  dstName  )  )  ; 
FileInputStream   srcFIS  =  null  ; 
int   bufSize  =  100000  ,  nBytesRead  =  0  ,  nBytesWritten  =  0  ; 
byte   buf  [  ]  =  new   byte  [  bufSize  ]  ; 
boolean   isURL  =  (  srcName  .  startsWith  (  "http://"  )  ||  srcName  .  startsWith  (  "file://"  )  )  ; 
if  (  isURL  )  { 
if  (  optUpdateMsg  !=  null  )  logMsg  (  optUpdateMsg  )  ; 
String   sDots  =  ""  ; 
URL   url  =  new   URL  (  srcName  )  ; 
InputStream   urlIS  =  url  .  openStream  (  )  ; 
int   nTotBytesRead  =  0  ; 
while  (  true  )  { 
if  (  optUpdateMsg  !=  null  )  { 
sDots  +=  "."  ; 
String   sPct  =  (  optEstInputFileLth  >  0  )  ?  (  (  int  )  (  (  100  *  nTotBytesRead  )  /  optEstInputFileLth  )  )  +  "% "  :  ""  ,  sProgress  =  "Copying "  +  sPct  +  sDots  +  "\n"  ; 
logMsg  (  sProgress  )  ; 
} 
nBytesRead  =  urlIS  .  read  (  buf  )  ; 
nTotBytesRead  +=  nBytesRead  ; 
if  (  nBytesRead  ==  -  1  )  break  ;  else  { 
dstFOS  .  write  (  buf  ,  0  ,  nBytesRead  )  ; 
nBytesWritten  +=  nBytesRead  ; 
} 
} 
dstFOS  .  close  (  )  ; 
if  (  optUpdateMsg  !=  null  )  { 
logMsg  (  "\n"  )  ; 
} 
}  else  { 
srcFIS  =  new   FileInputStream  (  new   File  (  srcName  )  )  ; 
while  (  true  )  { 
nBytesRead  =  srcFIS  .  read  (  buf  )  ; 
if  (  nBytesRead  ==  -  1  )  break  ;  else  { 
dstFOS  .  write  (  buf  ,  0  ,  nBytesRead  )  ; 
nBytesWritten  +=  nBytesRead  ; 
} 
} 
srcFIS  .  close  (  )  ; 
dstFOS  .  close  (  )  ; 
} 
}  catch  (  Exception   e1  )  { 
return  (  false  )  ; 
} 
return  (  true  )  ; 
} 










public   static   byte  [  ]  readBytesFromURL  (  String   srcName  ,  String   optUpdateMsg  )  { 
if  (  !  srcName  .  startsWith  (  "http://"  )  &&  !  srcName  .  startsWith  (  "file://"  )  )  return  (  null  )  ; 
int   bufSize  =  20000  ,  nBytesRead  =  0  ,  nBytesWritten  =  0  ,  oByteSize  =  bufSize  ; 
byte   buf  [  ]  =  null  ,  oBuf  [  ]  =  null  ; 
try  { 
buf  =  new   byte  [  bufSize  ]  ; 
oBuf  =  new   byte  [  bufSize  ]  ; 
if  (  optUpdateMsg  !=  null  )  logMsg  (  optUpdateMsg  )  ; 
String   sDots  =  ""  ; 
URL   url  =  new   URL  (  srcName  )  ; 
InputStream   urlIS  =  url  .  openStream  (  )  ; 
logMsg  (  "Reading "  +  sDots  )  ; 
while  (  true  )  { 
if  (  optUpdateMsg  !=  null  )  { 
logMsg  (  "."  )  ; 
} 
nBytesRead  =  urlIS  .  read  (  buf  )  ; 
if  (  nBytesRead  ==  -  1  )  break  ;  else  { 
if  (  nBytesRead  +  nBytesWritten  >=  oByteSize  )  { 
byte   tmp  [  ]  =  new   byte  [  oByteSize  +  bufSize  ]  ; 
for  (  int   i  =  0  ;  i  <  nBytesWritten  ;  i  ++  )  tmp  [  i  ]  =  oBuf  [  i  ]  ; 
oBuf  =  tmp  ; 
oByteSize  +=  bufSize  ; 
} 
for  (  int   i  =  0  ;  i  <  nBytesRead  ;  i  ++  )  oBuf  [  nBytesWritten  ++  ]  =  buf  [  i  ]  ; 
} 
} 
byte   tmp  [  ]  =  new   byte  [  nBytesWritten  ]  ; 
for  (  int   i  =  0  ;  i  <  nBytesWritten  ;  i  ++  )  tmp  [  i  ]  =  oBuf  [  i  ]  ; 
oBuf  =  tmp  ; 
if  (  optUpdateMsg  !=  null  )  { 
logMsg  (  "\n"  )  ; 
} 
}  catch  (  Exception   e  )  { 
logMsg  (  "Problem can't readBytesFromURL() e="  +  e  +  "\n"  )  ; 
return  (  null  )  ; 
} 
return  (  oBuf  )  ; 
} 






public   static   boolean   deleteLocalFile  (  String   fileName  )  { 
try  { 
File   srcF  =  new   File  (  fileName  )  ; 
if  (  srcF  .  exists  (  )  )  srcF  .  delete  (  )  ; 
}  catch  (  Exception   e  )  { 
return  (  false  )  ; 
} 
return  (  true  )  ; 
} 
} 

