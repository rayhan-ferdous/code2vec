package   it  .  greentone  ; 

import   it  .  greentone  .  persistence  .  JobStatus  ; 
import   it  .  greentone  .  persistence  .  OperationType  ; 
import   java  .  awt  .  Desktop  ; 
import   java  .  awt  .  Graphics  ; 
import   java  .  awt  .  GraphicsConfiguration  ; 
import   java  .  awt  .  GraphicsDevice  ; 
import   java  .  awt  .  GraphicsEnvironment  ; 
import   java  .  awt  .  HeadlessException  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  Transparency  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  awt  .  image  .  PixelGrabber  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  logging  .  Level  ; 
import   javax  .  inject  .  Inject  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   javax  .  swing  .  JTable  ; 
import   javax  .  swing  .  table  .  DefaultTableCellRenderer  ; 
import   javax  .  swing  .  text  .  JTextComponent  ; 
import   javax  .  swing  .  text  .  MaskFormatter  ; 
import   org  .  jdesktop  .  application  .  Application  ; 
import   org  .  jdesktop  .  application  .  ResourceMap  ; 
import   org  .  jdesktop  .  swingx  .  JXDatePicker  ; 
import   org  .  jdesktop  .  swingx  .  JXTable  ; 
import   org  .  joda  .  time  .  DateTime  ; 
import   org  .  joda  .  time  .  format  .  DateTimeFormat  ; 
import   org  .  joda  .  time  .  format  .  DateTimeFormatter  ; 
import   org  .  springframework  .  stereotype  .  Component  ; 




















@  Component 
public   class   GreenToneUtilities  { 

@  Inject 
private   GreenToneLogProvider   logger  ; 

private   static   final   String   UPDATE_URL  =  "http://greentone.googlecode.com/svn/trunk/installer/release.properties"  ; 

private   static   final   String   COMMENTS_CHAR  =  "#"  ; 

private   static   final   String   VERSION_SEPARATOR  =  "."  ; 

private   static   final   String   APP_NAME  =  "application.name"  ; 

private   static   final   String   APP_MAJOR_VERSION  =  "major.version.number"  ; 

private   static   final   String   APP_MINOR_VERSION  =  "minor.version.number"  ; 

private   static   final   String   APP_MINUS_VERSION  =  "minus.version.number"  ; 

private   static   final   DateTimeFormatter   dateTimeFormatter  =  DateTimeFormat  .  forPattern  (  "dd/MM/yyyy"  )  ; 












public   static   String   getText  (  JTextComponent   textField  )  { 
String   tmp  =  textField  .  getText  (  )  ; 
if  (  tmp  !=  null  )  { 
tmp  =  tmp  .  trim  (  )  ; 
if  (  tmp  .  length  (  )  ==  0  )  { 
tmp  =  null  ; 
} 
} 
return   tmp  ; 
} 








public   static   DateTime   getDateTime  (  JXDatePicker   datePicker  )  { 
Date   date  =  datePicker  .  getDate  (  )  ; 
return   date  !=  null  ?  new   DateTime  (  date  )  :  null  ; 
} 











public   static   MaskFormatter   createMaskFormatter  (  String   mask  )  { 
MaskFormatter   mf  =  null  ; 
try  { 
mf  =  new   MaskFormatter  (  mask  )  { 

private   static   final   long   serialVersionUID  =  1L  ; 

@  Override 
public   Object   stringToValue  (  String   value  )  throws   ParseException  { 
if  (  value  ==  null  ||  value  .  length  (  )  ==  0  ||  value  .  trim  (  )  .  isEmpty  (  )  )  { 
return   null  ; 
} 
return   super  .  stringToValue  (  value  )  ; 
} 
}  ; 
}  catch  (  ParseException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   mf  ; 
} 








public   String   checkUpdates  (  )  { 
ResourceMap   resourceMap  =  Application  .  getInstance  (  GreenTone  .  class  )  .  getContext  (  )  .  getResourceMap  (  )  ; 
String   currentVersion  =  resourceMap  .  getString  (  "Application.version"  )  ; 
String   remoteVersion  =  ""  ; 
try  { 
BufferedInputStream   in  =  new   BufferedInputStream  (  new   URL  (  UPDATE_URL  )  .  openStream  (  )  )  ; 
StringBuffer   strBuffer  =  new   StringBuffer  (  )  ; 
byte   data  [  ]  =  new   byte  [  1024  ]  ; 
while  (  in  .  read  (  data  ,  0  ,  1024  )  >=  0  )  { 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
strBuffer  .  append  (  (  char  )  data  [  i  ]  )  ; 
} 
} 
in  .  close  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   StringReader  (  strBuffer  .  toString  (  )  )  )  ; 
String   str  ; 
String   major  =  ""  ; 
String   minor  =  ""  ; 
String   minus  =  ""  ; 
while  (  (  str  =  reader  .  readLine  (  )  )  !=  null  )  { 
if  (  str  .  length  (  )  >  0  &&  !  str  .  startsWith  (  COMMENTS_CHAR  )  &&  !  str  .  startsWith  (  APP_NAME  )  )  { 
if  (  str  .  startsWith  (  APP_MAJOR_VERSION  )  )  { 
major  =  str  .  substring  (  str  .  length  (  )  -  1  )  +  VERSION_SEPARATOR  ; 
}  else   if  (  str  .  startsWith  (  APP_MINOR_VERSION  )  )  { 
minor  =  str  .  substring  (  str  .  length  (  )  -  1  )  +  VERSION_SEPARATOR  ; 
}  else   if  (  str  .  startsWith  (  APP_MINUS_VERSION  )  )  { 
minus  =  str  .  substring  (  str  .  length  (  )  -  1  )  ; 
} 
} 
} 
remoteVersion  =  major  +  minor  +  minus  ; 
}  catch  (  Exception   e  )  { 
logger  .  getLogger  (  )  .  info  (  Application  .  getInstance  (  GreenTone  .  class  )  .  getContext  (  )  .  getResourceMap  (  )  .  getString  (  "ErrorMessage.checkUpdate"  )  )  ; 
} 
if  (  remoteVersion  .  length  (  )  >  0  )  { 
return   remoteVersion  .  equals  (  currentVersion  )  ?  null  :  remoteVersion  ; 
} 
return   null  ; 
} 








public   static   double   roundTwoDecimals  (  double   value  )  { 
double   result  =  value  *  100  ; 
result  =  Math  .  round  (  result  )  ; 
result  =  result  /  100  ; 
return   result  ; 
} 











public   void   copyFile  (  File   input  ,  File   output  )  throws   IOException  { 
InputStream   in  =  null  ; 
OutputStream   out  =  null  ; 
try  { 
in  =  new   FileInputStream  (  input  )  ; 
out  =  new   FileOutputStream  (  output  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
}  catch  (  Exception   e  )  { 
logger  .  getLogger  (  )  .  log  (  Level  .  WARNING  ,  Application  .  getInstance  (  GreenTone  .  class  )  .  getContext  (  )  .  getResourceMap  (  )  .  getString  (  "ErrorMessage.copyingFile"  )  +  " "  +  input  .  getPath  (  )  ,  e  )  ; 
}  finally  { 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 
} 








public   void   open  (  File   file  )  { 
if  (  Desktop  .  isDesktopSupported  (  )  )  { 
Desktop   desktop  =  Desktop  .  getDesktop  (  )  ; 
try  { 
desktop  .  open  (  file  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  getLogger  (  )  .  log  (  Level  .  WARNING  ,  Application  .  getInstance  (  GreenTone  .  class  )  .  getContext  (  )  .  getResourceMap  (  )  .  getString  (  "ErrorMessage.cannotOpenURL"  )  +  " "  +  file  .  getPath  (  )  ,  e  )  ; 
} 
} 
} 







public   void   browse  (  URI   uri  )  { 
if  (  Desktop  .  isDesktopSupported  (  )  )  { 
Desktop   desktop  =  Desktop  .  getDesktop  (  )  ; 
try  { 
desktop  .  browse  (  uri  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  getLogger  (  )  .  log  (  Level  .  WARNING  ,  Application  .  getInstance  (  GreenTone  .  class  )  .  getContext  (  )  .  getResourceMap  (  )  .  getString  (  "ErrorMessage.cannotOpenURL"  )  +  " "  +  uri  .  getPath  (  )  ,  e  )  ; 
} 
} 
} 














public   static   String   leftPadding  (  String   toPad  ,  char   paddingChar  ,  int   length  )  { 
int   toPadLength  =  toPad  .  length  (  )  ; 
for  (  int   y  =  0  ;  y  <  length  -  toPadLength  ;  y  ++  )  { 
toPad  =  paddingChar  +  toPad  ; 
} 
return   toPad  ; 
} 







public   static   BufferedImage   toBufferedImage  (  Image   image  )  { 
if  (  image   instanceof   BufferedImage  )  { 
return  (  BufferedImage  )  image  ; 
} 
image  =  new   ImageIcon  (  image  )  .  getImage  (  )  ; 
boolean   hasAlpha  =  hasAlpha  (  image  )  ; 
BufferedImage   bimage  =  null  ; 
GraphicsEnvironment   ge  =  GraphicsEnvironment  .  getLocalGraphicsEnvironment  (  )  ; 
try  { 
int   transparency  =  Transparency  .  OPAQUE  ; 
if  (  hasAlpha  ==  true  )  { 
transparency  =  Transparency  .  BITMASK  ; 
} 
GraphicsDevice   gs  =  ge  .  getDefaultScreenDevice  (  )  ; 
GraphicsConfiguration   gc  =  gs  .  getDefaultConfiguration  (  )  ; 
bimage  =  gc  .  createCompatibleImage  (  image  .  getWidth  (  null  )  ,  image  .  getHeight  (  null  )  ,  transparency  )  ; 
}  catch  (  HeadlessException   e  )  { 
} 
if  (  bimage  ==  null  )  { 
int   type  =  BufferedImage  .  TYPE_INT_RGB  ; 
if  (  hasAlpha  ==  true  )  { 
type  =  BufferedImage  .  TYPE_INT_ARGB  ; 
} 
bimage  =  new   BufferedImage  (  image  .  getWidth  (  null  )  ,  image  .  getHeight  (  null  )  ,  type  )  ; 
} 
Graphics   g  =  bimage  .  createGraphics  (  )  ; 
g  .  drawImage  (  image  ,  0  ,  0  ,  null  )  ; 
g  .  dispose  (  )  ; 
return   bimage  ; 
} 








public   static   boolean   hasAlpha  (  Image   image  )  { 
if  (  image   instanceof   BufferedImage  )  { 
return  (  (  BufferedImage  )  image  )  .  getColorModel  (  )  .  hasAlpha  (  )  ; 
} 
PixelGrabber   pg  =  new   PixelGrabber  (  image  ,  0  ,  0  ,  1  ,  1  ,  false  )  ; 
try  { 
pg  .  grabPixels  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
return   pg  .  getColorModel  (  )  .  hasAlpha  (  )  ; 
} 








public   static   String   formatDateTime  (  DateTime   date  )  { 
if  (  date  !=  null  )  { 
return   dateTimeFormatter  .  print  (  date  )  ; 
} 
return  ""  ; 
} 






@  SuppressWarnings  (  "serial"  ) 
public   static   DefaultTableCellRenderer   getJobStatusTableCellRenderer  (  )  { 
return   new   DefaultTableCellRenderer  (  )  { 

@  Override 
public   java  .  awt  .  Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   isSelected  ,  boolean   hasFocus  ,  int   row  ,  int   column  )  { 
JobStatus   status  =  (  JobStatus  )  value  ; 
return   super  .  getTableCellRendererComponent  (  table  ,  status  !=  null  ?  status  .  getLocalizedName  (  )  :  null  ,  isSelected  ,  hasFocus  ,  row  ,  column  )  ; 
} 
}  ; 
} 






@  SuppressWarnings  (  "serial"  ) 
public   static   DefaultTableCellRenderer   getOperationTypeTableCellRenderer  (  )  { 
return   new   DefaultTableCellRenderer  (  )  { 

@  Override 
public   java  .  awt  .  Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   isSelected  ,  boolean   hasFocus  ,  int   row  ,  int   column  )  { 
OperationType   type  =  (  OperationType  )  value  ; 
return   super  .  getTableCellRendererComponent  (  table  ,  type  !=  null  ?  type  .  getLocalizedName  (  )  :  null  ,  isSelected  ,  hasFocus  ,  row  ,  column  )  ; 
} 
}  ; 
} 






@  SuppressWarnings  (  "serial"  ) 
public   static   DefaultTableCellRenderer   getDateTableCellRenderer  (  )  { 
return   new   DefaultTableCellRenderer  (  )  { 

@  Override 
public   java  .  awt  .  Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   isSelected  ,  boolean   hasFocus  ,  int   row  ,  int   column  )  { 
DateTime   date  =  (  DateTime  )  value  ; 
return   super  .  getTableCellRendererComponent  (  table  ,  formatDateTime  (  date  )  ,  isSelected  ,  hasFocus  ,  row  ,  column  )  ; 
} 
}  ; 
} 






@  SuppressWarnings  (  "serial"  ) 
public   static   DefaultTableCellRenderer   getDoubleTableCellRenderer  (  )  { 
return   new   DefaultTableCellRenderer  (  )  { 

@  Override 
public   java  .  awt  .  Component   getTableCellRendererComponent  (  JTable   table  ,  Object   value  ,  boolean   isSelected  ,  boolean   hasFocus  ,  int   row  ,  int   column  )  { 
Double   amount  =  (  Double  )  value  ; 
DecimalFormat   decimalFormat  =  (  DecimalFormat  )  DecimalFormat  .  getInstance  (  )  ; 
decimalFormat  .  setMinimumFractionDigits  (  2  )  ; 
decimalFormat  .  setMaximumFractionDigits  (  2  )  ; 
return   super  .  getTableCellRendererComponent  (  table  ,  amount  !=  null  ?  decimalFormat  .  format  (  amount  )  :  null  ,  isSelected  ,  hasFocus  ,  row  ,  column  )  ; 
} 
}  ; 
} 







public   static   JXTable   createJXTable  (  )  { 
JXTable   table  =  new   JXTable  (  )  ; 
table  .  setColumnControlVisible  (  true  )  ; 
table  .  setDefaultRenderer  (  JobStatus  .  class  ,  getJobStatusTableCellRenderer  (  )  )  ; 
table  .  setDefaultRenderer  (  OperationType  .  class  ,  getOperationTypeTableCellRenderer  (  )  )  ; 
table  .  setDefaultRenderer  (  DateTime  .  class  ,  getDateTableCellRenderer  (  )  )  ; 
table  .  setDefaultRenderer  (  Double  .  class  ,  getDoubleTableCellRenderer  (  )  )  ; 
return   table  ; 
} 
} 

