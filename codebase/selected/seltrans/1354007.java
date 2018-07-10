package   squirrel  .  DeaddropDroid  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  SortedMap  ; 
import   java  .  util  .  TreeMap  ; 
import   android  .  content  .  Context  ; 
import   android  .  content  .  Intent  ; 
import   android  .  content  .  SharedPreferences  ; 
import   android  .  content  .  pm  .  PackageManager  ; 
import   android  .  content  .  pm  .  ResolveInfo  ; 
import   android  .  database  .  Cursor  ; 
import   android  .  net  .  ConnectivityManager  ; 
import   android  .  net  .  NetworkInfo  ; 
import   android  .  os  .  Environment  ; 
import   android  .  preference  .  PreferenceManager  ; 
import   android  .  util  .  Log  ; 
import   android  .  view  .  animation  .  Animation  ; 
import   android  .  view  .  animation  .  LinearInterpolator  ; 
import   android  .  view  .  animation  .  RotateAnimation  ; 





public   class   DeaddropUtil  { 




protected   static   final   long   LOCATION_UPDATE_TIME  =  600000  ; 





protected   static   final   float   LOCATION_UPDATE_DISTANCE  =  100  ; 




protected   static   final   String   DOWNLOAD_PROGRESS_INTENT  =  "DeaddropUtil.getImage"  ; 

private   static   final   String   TAG  =  DeaddropDroid  .  TAG  ; 




















protected   static   File   getImage  (  final   Context   context  ,  final   String   name  ,  final   String   id  ,  final   boolean   fullsize  )  throws   IOException  { 
File   imageDir  ; 
if  (  fullsize  )  imageDir  =  getExternalFilesDir  (  id  +  "/fs/"  )  ;  else   imageDir  =  getExternalFilesDir  (  id  )  ; 
final   File   imageFile  =  new   File  (  imageDir  ,  name  )  ; 
if  (  imageFile  .  exists  (  )  &&  imageFile  .  length  (  )  >  0  )  return   imageFile  ; 
if  (  isOnline  (  context  )  )  { 
InputStream   is  =  null  ; 
final   URLConnection   ucon  =  getUrlConnection  (  context  ,  name  ,  id  ,  fullsize  )  ; 
try  { 
is  =  ucon  .  getInputStream  (  )  ; 
}  catch  (  final   IOException   e  )  { 
Log  .  e  (  TAG  ,  "Failed to create inputstream."  )  ; 
Log  .  e  (  TAG  ,  ""  +  e  )  ; 
return   null  ; 
} 
if  (  is  ==  null  )  return   null  ; 
FileOutputStream   fos  =  null  ; 
try  { 
fos  =  new   FileOutputStream  (  imageFile  )  ; 
}  catch  (  final   FileNotFoundException   e  )  { 
Log  .  e  (  TAG  ,  e  .  toString  (  )  ,  e  )  ; 
throw   e  ; 
} 
final   byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   len1  =  0  ; 
while  (  (  len1  =  is  .  read  (  buffer  )  )  >  0  )  fos  .  write  (  buffer  ,  0  ,  len1  )  ; 
fos  .  close  (  )  ; 
if  (  imageFile  .  exists  (  )  &&  imageFile  .  length  (  )  >  0  )  return   imageFile  ; 
imageFile  .  delete  (  )  ; 
Log  .  v  (  TAG  ,  "Geen data ontvangen uit de InputStream."  )  ; 
} 
return   null  ; 
} 













protected   static   URLConnection   getUrlConnection  (  final   Context   context  ,  final   String   name  ,  final   String   id  ,  final   boolean   fullsize  )  throws   IOException  { 
if  (  !  isOnline  (  context  )  )  return   null  ; 
String   imageUrl  =  ""  ; 
if  (  fullsize  )  { 
if  (  DeaddropDB  .  HTTPS  )  imageUrl  =  DeaddropDB  .  IMAGE_URL_FS_HTTPS  +  id  +  '/'  +  name  ;  else   imageUrl  =  DeaddropDB  .  IMAGE_URL_FS  +  id  +  '/'  +  name  ; 
}  else  { 
if  (  DeaddropDB  .  HTTPS  )  imageUrl  =  DeaddropDB  .  IMAGE_URL_HTTPS  +  id  +  '/'  +  name  ;  else   imageUrl  =  DeaddropDB  .  IMAGE_URL  +  id  +  '/'  +  name  ; 
} 
URL   url  ; 
URLConnection   ucon  ; 
try  { 
url  =  new   URL  (  imageUrl  )  ; 
ucon  =  url  .  openConnection  (  )  ; 
}  catch  (  final   MalformedURLException   e  )  { 
Log  .  v  (  TAG  ,  "Malformed URL "  +  imageUrl  )  ; 
Log  .  v  (  TAG  ,  ""  +  e  )  ; 
return   null  ; 
}  catch  (  final   IOException   e  )  { 
Log  .  v  (  TAG  ,  "Problem accessing url "  +  imageUrl  )  ; 
Log  .  v  (  TAG  ,  ""  +  e  )  ; 
return   null  ; 
} 
return   ucon  ; 
} 











public   static   void   copyFile  (  final   File   sourceFile  ,  final   File   destFile  )  throws   IOException  { 
if  (  !  destFile  .  exists  (  )  )  destFile  .  createNewFile  (  )  ; 
FileChannel   source  =  null  ; 
FileChannel   destination  =  null  ; 
try  { 
source  =  new   FileInputStream  (  sourceFile  )  .  getChannel  (  )  ; 
destination  =  new   FileOutputStream  (  destFile  )  .  getChannel  (  )  ; 
destination  .  transferFrom  (  source  ,  0  ,  source  .  size  (  )  )  ; 
}  finally  { 
if  (  source  !=  null  )  source  .  close  (  )  ; 
if  (  destination  !=  null  )  destination  .  close  (  )  ; 
} 
} 









public   static   String   getFormattedSize  (  int   size  )  { 
if  (  size  ==  0  )  return   null  ; 
String   unit  =  ""  ; 
if  (  size  >=  1024  *  1024  )  { 
size  =  size  /  (  1024  *  1024  )  ; 
unit  =  " GB"  ; 
}  else   if  (  size  >=  1024  )  { 
size  =  size  /  1024  ; 
unit  =  " MB"  ; 
}  else   unit  =  " kB"  ; 
return   size  +  unit  ; 
} 






public   static   boolean   isOnline  (  final   Context   context  )  { 
final   ConnectivityManager   cm  =  (  ConnectivityManager  )  context  .  getSystemService  (  Context  .  CONNECTIVITY_SERVICE  )  ; 
final   NetworkInfo   netInfo  =  cm  .  getActiveNetworkInfo  (  )  ; 
if  (  netInfo  !=  null  &&  netInfo  .  isConnectedOrConnecting  (  )  )  return   true  ; 
return   false  ; 
} 















public   static   double   distBetween  (  final   double   lat1  ,  final   double   lng1  ,  final   double   lat2  ,  final   double   lng2  )  { 
final   double   earthRadius  =  6369630  ; 
final   double   dLat  =  Math  .  toRadians  (  lat2  -  lat1  )  ; 
final   double   dLng  =  Math  .  toRadians  (  lng2  -  lng1  )  ; 
final   double   a  =  Math  .  sin  (  dLat  /  2  )  *  Math  .  sin  (  dLat  /  2  )  +  Math  .  cos  (  Math  .  toRadians  (  lat1  )  )  *  Math  .  cos  (  Math  .  toRadians  (  lat2  )  )  *  Math  .  sin  (  dLng  /  2  )  *  Math  .  sin  (  dLng  /  2  )  ; 
final   double   c  =  2  *  Math  .  atan2  (  Math  .  sqrt  (  a  )  ,  Math  .  sqrt  (  1  -  a  )  )  ; 
final   double   dist  =  earthRadius  *  c  ; 
return   dist  ; 
} 










public   static   String   hash  (  final   String   s  )  { 
if  (  s  ==  null  ||  s  .  length  (  )  ==  0  )  return   null  ; 
try  { 
final   MessageDigest   hashEngine  =  MessageDigest  .  getInstance  (  "SHA-1"  )  ; 
hashEngine  .  update  (  s  .  getBytes  (  "iso-8859-1"  )  ,  0  ,  s  .  length  (  )  )  ; 
return   convertToHex  (  hashEngine  .  digest  (  )  )  ; 
}  catch  (  final   Exception   e  )  { 
return   null  ; 
} 
} 










private   static   final   String   convertToHex  (  final   byte  [  ]  data  )  { 
if  (  data  ==  null  ||  data  .  length  ==  0  )  return   null  ; 
final   StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
for  (  int   byteIndex  =  0  ;  byteIndex  <  data  .  length  ;  byteIndex  ++  )  { 
int   halfbyte  =  (  data  [  byteIndex  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buffer  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buffer  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  byteIndex  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buffer  .  toString  (  )  ; 
} 






public   static   RotateAnimation   animation  (  )  { 
final   RotateAnimation   rotation  =  new   RotateAnimation  (  0f  ,  360f  ,  Animation  .  RELATIVE_TO_SELF  ,  0.5f  ,  Animation  .  RELATIVE_TO_SELF  ,  0.5f  )  ; 
rotation  .  setDuration  (  1200  )  ; 
rotation  .  setInterpolator  (  new   LinearInterpolator  (  )  )  ; 
rotation  .  setRepeatMode  (  Animation  .  RESTART  )  ; 
rotation  .  setRepeatCount  (  Animation  .  INFINITE  )  ; 
return   rotation  ; 
} 















public   static   String  [  ]  formatCoordinates  (  final   Context   context  ,  final   double   lat  ,  final   double   lon  ,  final   int   format  )  { 
final   String  [  ]  res  =  new   String  [  2  ]  ; 
String   ns  =  ""  ; 
String   ew  =  ""  ; 
String   d1  ,  d2  ,  m1  ,  m2  ,  s1  ,  s2  ; 
if  (  lat  >  0  )  ns  =  context  .  getString  (  R  .  string  .  north  )  ;  else   ns  =  context  .  getString  (  R  .  string  .  south  )  ; 
if  (  lon  >  0  )  ew  =  context  .  getString  (  R  .  string  .  east  )  ;  else   ew  =  context  .  getString  (  R  .  string  .  west  )  ; 
switch  (  format  )  { 
case   0  : 
d1  =  ""  +  getDeg  (  lat  )  ; 
d2  =  ""  +  getDeg  (  lon  )  ; 
m1  =  ""  +  (  int  )  getMin  (  lat  )  ; 
m2  =  ""  +  (  int  )  getMin  (  lon  )  ; 
s1  =  ""  +  (  (  int  )  (  getSec  (  lat  )  *  10  )  )  /  10.0  ; 
s2  =  ""  +  (  (  int  )  (  getSec  (  lon  )  *  10  )  )  /  10.0  ; 
res  [  0  ]  =  d1  +  "°"  +  m1  +  "'"  +  s1  +  "\""  +  ns  ; 
res  [  1  ]  =  d2  +  "°"  +  m2  +  "'"  +  s2  +  "\""  +  ew  ; 
break  ; 
case   1  : 
d1  =  ""  +  getDeg  (  lat  )  ; 
d2  =  ""  +  getDeg  (  lon  )  ; 
m1  =  ""  +  (  (  int  )  (  getMin  (  lat  )  *  1000  )  )  /  1000.0  ; 
m2  =  ""  +  (  (  int  )  (  getMin  (  lon  )  *  1000  )  )  /  1000.0  ; 
res  [  0  ]  =  d1  +  "°"  +  m1  +  "'"  +  ns  ; 
res  [  1  ]  =  d2  +  "°"  +  m2  +  "'"  +  ew  ; 
break  ; 
case   2  : 
res  [  0  ]  =  Double  .  toString  (  (  (  int  )  (  lat  *  1E7  )  )  /  1E7  )  ; 
res  [  1  ]  =  Double  .  toString  (  (  (  int  )  (  lon  *  1E7  )  )  /  1E7  )  ; 
break  ; 
} 
return   res  ; 
} 








private   static   int   getDeg  (  final   double   c  )  { 
return   Math  .  abs  (  (  int  )  c  )  ; 
} 








private   static   double   getMin  (  double   c  )  { 
c  =  Math  .  abs  (  c  )  ; 
return  (  c  -  getDeg  (  c  )  )  *  60.0  ; 
} 








private   static   double   getSec  (  double   c  )  { 
c  =  Math  .  abs  (  c  )  ; 
return  (  getMin  (  c  )  -  (  int  )  getMin  (  c  )  )  *  60.0  ; 
} 















protected   static   boolean   isIntentAvailable  (  final   Context   context  ,  final   String   action  )  { 
final   PackageManager   packageManager  =  context  .  getPackageManager  (  )  ; 
final   Intent   intent  =  new   Intent  (  action  )  ; 
final   List  <  ResolveInfo  >  list  =  packageManager  .  queryIntentActivities  (  intent  ,  PackageManager  .  MATCH_DEFAULT_ONLY  )  ; 
return   list  .  size  (  )  >  0  ; 
} 







protected   static   void   manageCache  (  Context   context  )  { 
DeaddropDB   db  =  new   DeaddropDB  (  context  )  ; 
Cursor   c  =  db  .  query  (  DeaddropDB  .  DEADDROPS_TABLE  ,  new   String  [  ]  {  DeaddropDB  .  KEY_ID  }  ,  DeaddropDB  .  KEY_DEADDROP_STORED  +  " > 0"  ,  null  ,  null  )  ; 
c  .  moveToFirst  (  )  ; 
ArrayList  <  String  >  stored  =  new   ArrayList  <  String  >  (  )  ; 
if  (  c  .  getCount  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  c  .  getCount  (  )  ;  i  ++  )  { 
stored  .  add  (  Integer  .  toString  (  c  .  getInt  (  c  .  getColumnIndex  (  DeaddropDB  .  KEY_ID  )  )  )  )  ; 
c  .  moveToNext  (  )  ; 
} 
} 
c  .  close  (  )  ; 
long   totalSize  =  0  ; 
final   Map  <  String  ,  Long  >  sizeMap  =  new   TreeMap  <  String  ,  Long  >  (  )  ; 
final   SortedMap  <  Long  ,  String  >  ageMap  =  new   TreeMap  <  Long  ,  String  >  (  )  ; 
long   size  ; 
File  [  ]  files  =  getExternalFilesDir  (  ""  )  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
String   name  =  file  .  getName  (  )  ; 
if  (  !  stored  .  contains  (  name  )  &&  file  .  isDirectory  (  )  )  { 
ageMap  .  put  (  file  .  lastModified  (  )  ,  name  )  ; 
size  =  getDirSize  (  file  )  ; 
totalSize  +=  size  ; 
sizeMap  .  put  (  name  ,  size  )  ; 
File   fs  =  new   File  (  file  .  getAbsolutePath  (  )  +  "/fs"  )  ; 
if  (  fs  .  isDirectory  (  )  )  { 
size  =  getDirSize  (  fs  )  ; 
totalSize  +=  size  ; 
sizeMap  .  put  (  name  ,  sizeMap  .  get  (  name  )  +  size  )  ; 
} 
} 
} 
SharedPreferences   preferences  =  PreferenceManager  .  getDefaultSharedPreferences  (  context  )  ; 
while  (  totalSize  >  Integer  .  parseInt  (  preferences  .  getString  (  Preferences  .  KEY_CACHE_SIZE  ,  "50"  )  )  *  1024  *  1024  )  { 
String   oldest  =  ageMap  .  get  (  ageMap  .  firstKey  (  )  )  ; 
db  .  delete  (  DeaddropDB  .  DEADDROPS_TABLE  ,  DeaddropDB  .  KEY_ID  +  " = ?"  ,  new   String  [  ]  {  oldest  }  )  ; 
ageMap  .  remove  (  ageMap  .  firstKey  (  )  )  ; 
totalSize  -=  sizeMap  .  get  (  oldest  )  ; 
String   deleteCmd  =  "rm -r "  +  getExternalFilesDir  (  oldest  )  ; 
Runtime   runtime  =  Runtime  .  getRuntime  (  )  ; 
try  { 
runtime  .  exec  (  deleteCmd  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 







protected   static   long   getCacheSize  (  Context   context  )  { 
DeaddropDB   db  =  new   DeaddropDB  (  context  )  ; 
Cursor   c  =  db  .  query  (  DeaddropDB  .  DEADDROPS_TABLE  ,  new   String  [  ]  {  DeaddropDB  .  KEY_ID  }  ,  DeaddropDB  .  KEY_DEADDROP_STORED  +  " > 0"  ,  null  ,  null  )  ; 
c  .  moveToFirst  (  )  ; 
ArrayList  <  String  >  stored  =  new   ArrayList  <  String  >  (  )  ; 
if  (  c  .  getCount  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  c  .  getCount  (  )  ;  i  ++  )  { 
stored  .  add  (  Integer  .  toString  (  c  .  getInt  (  c  .  getColumnIndex  (  DeaddropDB  .  KEY_ID  )  )  )  )  ; 
c  .  moveToNext  (  )  ; 
} 
} 
c  .  close  (  )  ; 
long   totalSize  =  0  ; 
File  [  ]  files  =  getExternalFilesDir  (  ""  )  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  { 
String   name  =  file  .  getName  (  )  ; 
if  (  !  stored  .  contains  (  name  )  &&  file  .  isDirectory  (  )  )  { 
totalSize  +=  getDirSize  (  file  )  ; 
File   fs  =  new   File  (  file  .  getAbsolutePath  (  )  +  "/fs"  )  ; 
if  (  fs  .  isDirectory  (  )  )  totalSize  +=  getDirSize  (  fs  )  ; 
} 
} 
return   totalSize  ; 
} 







private   static   long   getDirSize  (  final   File   dir  )  { 
long   size  =  0  ; 
final   File  [  ]  files  =  dir  .  listFiles  (  )  ; 
for  (  File   file  :  files  )  size  =  size  +  file  .  length  (  )  ; 
return   size  ; 
} 







protected   static   File   getExternalFilesDir  (  final   String   type  )  { 
final   File   dir  =  new   File  (  Environment  .  getExternalStorageDirectory  (  )  ,  "/Android/data/squirrel.DeaddropDroid/files/"  +  type  )  ; 
if  (  !  dir  .  isDirectory  (  )  )  dir  .  mkdirs  (  )  ; 
return   dir  ; 
} 
} 

