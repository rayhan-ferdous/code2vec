package   com  .  hifi  .  core  .  utils  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  Locale  ; 

public   class   OSUtils  { 




private   static   boolean   _isWindows  =  false  ; 




private   static   boolean   _isWindowsNT  =  false  ; 




private   static   boolean   _isWindowsXP  =  false  ; 




private   static   boolean   _isWindowsNTor2000orXP  =  false  ; 




private   static   boolean   _isWindows2000orXP  =  false  ; 




private   static   boolean   _isWindows95  =  false  ; 




private   static   boolean   _isWindows98  =  false  ; 




private   static   boolean   _isWindowsMe  =  false  ; 





private   static   boolean   _supportsTray  =  false  ; 




private   static   boolean   _isMacOSX  =  false  ; 




private   static   boolean   _isLinux  =  false  ; 




private   static   boolean   _isSolaris  =  false  ; 




private   static   boolean   _isOS2  =  false  ; 





private   static   final   char  [  ]  ILLEGAL_CHARS_ANY_OS  =  {  '/'  ,  '\n'  ,  '\r'  ,  '\t'  ,  '\0'  ,  '\f'  }  ; 

private   static   final   char  [  ]  ILLEGAL_CHARS_UNIX  =  {  '`'  }  ; 

private   static   final   char  [  ]  ILLEGAL_CHARS_WINDOWS  =  {  '?'  ,  '*'  ,  '\\'  ,  '<'  ,  '>'  ,  '|'  ,  '\"'  ,  ':'  }  ; 

private   static   final   char  [  ]  ILLEGAL_CHARS_MACOS  =  {  ':'  }  ; 




private   static   final   File   CURRENT_DIRECTORY  =  new   File  (  System  .  getProperty  (  "user.dir"  )  )  ; 




static   File   SETTINGS_DIRECTORY  =  null  ; 




private   OSUtils  (  )  { 
} 




static  { 
setOperatingSystems  (  )  ; 
} 




private   static   void   setOperatingSystems  (  )  { 
_isWindows  =  false  ; 
_isWindowsNTor2000orXP  =  false  ; 
_isWindows2000orXP  =  false  ; 
_isWindowsNT  =  false  ; 
_isWindowsXP  =  false  ; 
_isWindows95  =  false  ; 
_isWindows98  =  false  ; 
_isWindowsMe  =  false  ; 
_isSolaris  =  false  ; 
_isLinux  =  false  ; 
_isOS2  =  false  ; 
_isMacOSX  =  false  ; 
String   os  =  System  .  getProperty  (  "os.name"  )  .  toLowerCase  (  Locale  .  US  )  ; 
_isWindows  =  os  .  indexOf  (  "windows"  )  !=  -  1  ; 
if  (  os  .  indexOf  (  "windows nt"  )  !=  -  1  ||  os  .  indexOf  (  "windows 2000"  )  !=  -  1  ||  os  .  indexOf  (  "windows xp"  )  !=  -  1  )  _isWindowsNTor2000orXP  =  true  ; 
if  (  os  .  indexOf  (  "windows 2000"  )  !=  -  1  ||  os  .  indexOf  (  "windows xp"  )  !=  -  1  )  _isWindows2000orXP  =  true  ; 
if  (  os  .  indexOf  (  "windows nt"  )  !=  -  1  )  _isWindowsNT  =  true  ; 
if  (  os  .  indexOf  (  "windows xp"  )  !=  -  1  )  _isWindowsXP  =  true  ; 
if  (  os  .  indexOf  (  "windows 95"  )  !=  -  1  )  _isWindows95  =  true  ; 
if  (  os  .  indexOf  (  "windows 98"  )  !=  -  1  )  _isWindows98  =  true  ; 
if  (  os  .  indexOf  (  "windows me"  )  !=  -  1  )  _isWindowsMe  =  true  ; 
_isSolaris  =  os  .  indexOf  (  "solaris"  )  !=  -  1  ; 
_isLinux  =  os  .  indexOf  (  "linux"  )  !=  -  1  ; 
_isOS2  =  os  .  indexOf  (  "os/2"  )  !=  -  1  ; 
if  (  _isWindows  ||  _isLinux  )  _supportsTray  =  true  ; 
if  (  os  .  startsWith  (  "mac os"  )  )  { 
if  (  os  .  endsWith  (  "x"  )  )  { 
_isMacOSX  =  true  ; 
} 
} 
} 




public   static   String   getJavaVersion  (  )  { 
return   System  .  getProperty  (  "java.version"  )  ; 
} 




public   static   String   getOS  (  )  { 
return   System  .  getProperty  (  "os.name"  )  ; 
} 




public   static   String   getOSVersion  (  )  { 
return   System  .  getProperty  (  "os.version"  )  ; 
} 








public   static   File   getCurrentDirectory  (  )  { 
return   CURRENT_DIRECTORY  ; 
} 





public   static   boolean   supportsTray  (  )  { 
if  (  isLinux  (  )  )  return   false  ; 
return   _supportsTray  ; 
} 








public   static   boolean   isUltrapeerOS  (  )  { 
return  !  (  _isWindows98  ||  _isWindows95  ||  _isWindowsMe  ||  _isWindowsNT  )  ; 
} 





public   static   boolean   isGoodWindows  (  )  { 
return   isWindows  (  )  &&  isUltrapeerOS  (  )  ; 
} 







public   static   boolean   isWindows  (  )  { 
return   _isWindows  ; 
} 







public   static   boolean   isWindowsNTor2000orXP  (  )  { 
return   _isWindowsNTor2000orXP  ; 
} 







public   static   boolean   isWindows2000orXP  (  )  { 
return   _isWindows2000orXP  ; 
} 







public   static   boolean   isWindowsXP  (  )  { 
return   _isWindowsXP  ; 
} 







public   static   boolean   isOS2  (  )  { 
return   _isOS2  ; 
} 







public   static   boolean   isMacOSX  (  )  { 
return   _isMacOSX  ; 
} 




public   static   boolean   isCocoaFoundationAvailable  (  )  { 
if  (  !  isMacOSX  (  )  )  return   false  ; 
try  { 
Class  .  forName  (  "com.apple.cocoa.foundation.NSUserDefaults"  )  ; 
Class  .  forName  (  "com.apple.cocoa.foundation.NSMutableDictionary"  )  ; 
Class  .  forName  (  "com.apple.cocoa.foundation.NSMutableArray"  )  ; 
Class  .  forName  (  "com.apple.cocoa.foundation.NSObject"  )  ; 
Class  .  forName  (  "com.apple.cocoa.foundation.NSSystem"  )  ; 
return   true  ; 
}  catch  (  ClassNotFoundException   error  )  { 
return   false  ; 
}  catch  (  NoClassDefFoundError   error  )  { 
return   false  ; 
} 
} 







public   static   boolean   isAnyMac  (  )  { 
return   _isMacOSX  ; 
} 







public   static   boolean   isSolaris  (  )  { 
return   _isSolaris  ; 
} 







public   static   boolean   isLinux  (  )  { 
return   _isLinux  ; 
} 





public   static   boolean   isUnix  (  )  { 
return   _isLinux  ||  _isSolaris  ; 
} 




public   static   boolean   isPOSIX  (  )  { 
return   _isLinux  ||  _isSolaris  ||  _isMacOSX  ; 
} 







public   static   boolean   isJava14OrLater  (  )  { 
String   version  =  getJavaVersion  (  )  ; 
return  !  version  .  startsWith  (  "1.3"  )  &&  !  version  .  startsWith  (  "1.2"  )  &&  !  version  .  startsWith  (  "1.1"  )  &&  !  version  .  startsWith  (  "1.0"  )  ; 
} 







public   static   boolean   isJava142OrLater  (  )  { 
String   version  =  getJavaVersion  (  )  ; 
return  !  version  .  startsWith  (  "1.4.1"  )  &&  !  version  .  startsWith  (  "1.4.0"  )  &&  isJava14OrLater  (  )  ; 
} 




public   static   boolean   isJava15OrLater  (  )  { 
String   version  =  getJavaVersion  (  )  ; 
return  !  version  .  startsWith  (  "1.4"  )  &&  !  version  .  startsWith  (  "1.3"  )  &&  !  version  .  startsWith  (  "1.2"  )  &&  !  version  .  startsWith  (  "1.1"  )  &&  !  version  .  startsWith  (  "1.0"  )  ; 
} 




public   static   boolean   isJava16OrLater  (  )  { 
String   version  =  getJavaVersion  (  )  ; 
return  !  version  .  startsWith  (  "1.5"  )  &&  !  version  .  startsWith  (  "1.4"  )  &&  !  version  .  startsWith  (  "1.3"  )  &&  !  version  .  startsWith  (  "1.2"  )  &&  !  version  .  startsWith  (  "1.1"  )  &&  !  version  .  startsWith  (  "1.0"  )  ; 
} 




public   static   boolean   isJavaOutOfDate  (  )  { 
return   isWindows  (  )  &&  !  isSpecificJRE  (  )  &&  (  getJavaVersion  (  )  .  startsWith  (  "1.3"  )  ||  getJavaVersion  (  )  .  startsWith  (  "1.4.0"  )  )  ; 
} 




public   static   boolean   isSpecificJRE  (  )  { 
return   new   File  (  "."  ,  "jre"  )  .  isDirectory  (  )  ; 
} 












public   static   int   copy  (  File   src  ,  int   amount  ,  File   dst  )  { 
final   int   BUFFER_SIZE  =  1024  ; 
int   amountToRead  =  amount  ; 
InputStream   in  =  null  ; 
OutputStream   out  =  null  ; 
try  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  src  )  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  dst  )  )  ; 
byte  [  ]  buf  =  new   byte  [  BUFFER_SIZE  ]  ; 
while  (  amountToRead  >  0  )  { 
int   read  =  in  .  read  (  buf  ,  0  ,  Math  .  min  (  BUFFER_SIZE  ,  amountToRead  )  )  ; 
if  (  read  ==  -  1  )  break  ; 
amountToRead  -=  read  ; 
out  .  write  (  buf  ,  0  ,  read  )  ; 
} 
}  catch  (  IOException   e  )  { 
}  finally  { 
if  (  in  !=  null  )  try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
if  (  out  !=  null  )  { 
try  { 
out  .  flush  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   amount  -  amountToRead  ; 
} 






public   static   boolean   copy  (  File   src  ,  File   dst  )  { 
long   length  =  src  .  length  (  )  ; 
return   copy  (  src  ,  (  int  )  length  ,  dst  )  ==  length  ; 
} 








public   static   File   getUserHomeDir  (  )  { 
return   new   File  (  System  .  getProperty  (  "user.home"  )  )  ; 
} 






public   static   String   getUserName  (  )  { 
return   System  .  getProperty  (  "user.name"  )  ; 
} 









public   static   String   convertFileName  (  String   name  )  { 
if  (  name  .  length  (  )  >  180  )  { 
int   extStart  =  name  .  lastIndexOf  (  '.'  )  ; 
if  (  extStart  ==  -  1  )  { 
name  =  name  .  substring  (  0  ,  180  )  ; 
}  else  { 
int   extLength  =  name  .  length  (  )  -  extStart  ; 
int   extEnd  =  extLength  >  11  ?  extStart  +  11  :  name  .  length  (  )  ; 
name  =  name  .  substring  (  0  ,  180  -  extLength  )  +  name  .  substring  (  extStart  ,  extEnd  )  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  ILLEGAL_CHARS_ANY_OS  .  length  ;  i  ++  )  name  =  name  .  replace  (  ILLEGAL_CHARS_ANY_OS  [  i  ]  ,  '_'  )  ; 
if  (  _isWindows  ||  _isOS2  )  { 
for  (  int   i  =  0  ;  i  <  ILLEGAL_CHARS_WINDOWS  .  length  ;  i  ++  )  name  =  name  .  replace  (  ILLEGAL_CHARS_WINDOWS  [  i  ]  ,  '_'  )  ; 
}  else   if  (  _isLinux  ||  _isSolaris  )  { 
for  (  int   i  =  0  ;  i  <  ILLEGAL_CHARS_UNIX  .  length  ;  i  ++  )  name  =  name  .  replace  (  ILLEGAL_CHARS_UNIX  [  i  ]  ,  '_'  )  ; 
}  else   if  (  _isMacOSX  )  { 
for  (  int   i  =  0  ;  i  <  ILLEGAL_CHARS_MACOS  .  length  ;  i  ++  )  name  =  name  .  replace  (  ILLEGAL_CHARS_MACOS  [  i  ]  ,  '_'  )  ; 
} 
return   name  ; 
} 







public   static   String   seconds2time  (  int   seconds  )  { 
int   minutes  =  seconds  /  60  ; 
seconds  =  seconds  -  minutes  *  60  ; 
int   hours  =  minutes  /  60  ; 
minutes  =  minutes  -  hours  *  60  ; 
int   days  =  hours  /  24  ; 
hours  =  hours  -  days  *  24  ; 
StringBuilder   time  =  new   StringBuilder  (  )  ; 
if  (  days  !=  0  )  { 
time  .  append  (  Integer  .  toString  (  days  )  )  ; 
time  .  append  (  ":"  )  ; 
if  (  hours  <  10  )  time  .  append  (  "0"  )  ; 
} 
if  (  days  !=  0  ||  hours  !=  0  )  { 
time  .  append  (  Integer  .  toString  (  hours  )  )  ; 
time  .  append  (  ":"  )  ; 
if  (  minutes  <  10  )  time  .  append  (  "0"  )  ; 
} 
time  .  append  (  Integer  .  toString  (  minutes  )  )  ; 
time  .  append  (  ":"  )  ; 
if  (  seconds  <  10  )  time  .  append  (  "0"  )  ; 
time  .  append  (  Integer  .  toString  (  seconds  )  )  ; 
return   time  .  toString  (  )  ; 
} 
} 

