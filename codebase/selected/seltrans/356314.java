package   com  .  xlg  .  common  .  utils  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  NetworkInterface  ; 
import   java  .  net  .  SocketException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  text  .  NumberFormat  ; 
import   java  .  util  .  Enumeration  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   android  .  app  .  Activity  ; 
import   android  .  app  .  AlertDialog  ; 
import   android  .  content  .  Context  ; 
import   android  .  content  .  DialogInterface  ; 
import   android  .  content  .  DialogInterface  .  OnClickListener  ; 
import   android  .  content  .  Intent  ; 
import   android  .  graphics  .  Color  ; 
import   android  .  view  .  Gravity  ; 
import   android  .  view  .  View  ; 
import   android  .  view  .  ViewGroup  ; 
import   android  .  widget  .  LinearLayout  ; 
import   android  .  widget  .  ListAdapter  ; 
import   android  .  widget  .  ListView  ; 
import   android  .  widget  .  ProgressBar  ; 
import   android  .  widget  .  RelativeLayout  ; 
import   android  .  widget  .  TextView  ; 
import   com  .  xlg  .  R  ; 
import   com  .  xlg  .  activity  .  LoginActivity  ; 
import   com  .  xlg  .  base  .  BaseActivity  ; 
import   com  .  xlg  .  base  .  BaseApp  ; 
import   com  .  xlg  .  beans  .  UserBean  ; 




public   class   CommonUtil  { 




public   static   void   alterErr  (  Context   context  )  { 
dialog  (  context  ,  context  .  getString  (  R  .  string  .  request_response_fail  )  )  ; 
} 




public   static   void   dialog  (  Context   context  ,  String   message  )  { 
AlertDialog  .  Builder   tDialog  =  new   AlertDialog  .  Builder  (  context  )  ; 
tDialog  .  setTitle  (  R  .  string  .  dialogtitle  )  ; 
tDialog  .  setMessage  (  message  )  ; 
tDialog  .  setPositiveButton  (  R  .  string  .  ensure  ,  null  )  ; 
tDialog  .  show  (  )  ; 
} 




public   static   void   dialog  (  Context   context  ,  String   title  ,  String   message  ,  OnClickListener   pl  )  { 
AlertDialog  .  Builder   tDialog  =  new   AlertDialog  .  Builder  (  context  )  ; 
tDialog  .  setTitle  (  title  )  ; 
tDialog  .  setMessage  (  message  )  ; 
tDialog  .  setPositiveButton  (  R  .  string  .  ensure  ,  pl  )  ; 
tDialog  .  show  (  )  ; 
} 




public   static   void   dialog  (  Context   context  ,  int   title  ,  int   message  ,  OnClickListener   pl  )  { 
AlertDialog  .  Builder   tDialog  =  new   AlertDialog  .  Builder  (  context  )  ; 
tDialog  .  setTitle  (  title  )  ; 
tDialog  .  setMessage  (  message  )  ; 
tDialog  .  setPositiveButton  (  R  .  string  .  ensure  ,  pl  )  ; 
tDialog  .  show  (  )  ; 
} 




public   static   void   dialogNetError  (  Context   context  ,  OnClickListener   pl  )  { 
dialogConfirm  (  context  ,  R  .  string  .  dialogtitle  ,  R  .  string  .  net_error  ,  R  .  string  .  retry  ,  pl  ,  R  .  string  .  cancel  ,  null  )  ; 
} 




public   static   void   dialogExit  (  Context   context  ,  OnClickListener   pl  )  { 
dialogConfirm  (  context  ,  R  .  string  .  dialogtitle  ,  R  .  string  .  exit_app_msg  ,  R  .  string  .  ensure  ,  pl  ,  R  .  string  .  cancel  ,  null  )  ; 
} 




public   static   void   dialogConfirm  (  final   Context   context  ,  int   title  ,  int   message  ,  OnClickListener   pl  ,  OnClickListener   nl  )  { 
dialogConfirm  (  context  ,  title  ,  message  ,  R  .  string  .  ensure  ,  pl  ,  R  .  string  .  cancel  ,  nl  )  ; 
} 




public   static   void   dialogConfirm  (  final   Context   context  ,  int   title  ,  int   message  ,  int   pText  ,  OnClickListener   pl  ,  int   nText  ,  OnClickListener   nl  )  { 
AlertDialog  .  Builder   tDialog  =  new   AlertDialog  .  Builder  (  context  )  ; 
tDialog  .  setTitle  (  title  )  ; 
tDialog  .  setMessage  (  message  )  ; 
tDialog  .  setPositiveButton  (  pText  ,  pl  )  ; 
tDialog  .  setNegativeButton  (  nText  ,  nl  )  ; 
tDialog  .  show  (  )  ; 
} 






public   static   boolean   checkNoLoginDialog  (  final   BaseActivity   activity  )  { 
UserBean   userBean  =  CacheUtil  .  readLoginData  (  )  ; 
if  (  userBean  !=  null  )  return   false  ; 
dialogConfirm  (  activity  ,  R  .  string  .  dialogtitle  ,  R  .  string  .  please_login  ,  new   DialogInterface  .  OnClickListener  (  )  { 

public   void   onClick  (  DialogInterface   dialog  ,  int   which  )  { 
Intent   intent  =  new   Intent  (  activity  ,  LoginActivity  .  class  )  ; 
activity  .  parent  (  )  .  startSubActivity  (  intent  )  ; 
} 
}  ,  null  )  ; 
return   true  ; 
} 




public   static   void   setNoDataView  (  ListView   listView  ,  Activity   activity  )  { 
View   view  =  LayoutUtils  .  findLayoutById  (  R  .  layout  .  nodata  ,  activity  )  ; 
TextView   textView  =  (  TextView  )  view  .  findViewById  (  R  .  id  .  nodata  )  ; 
textView  .  setText  (  R  .  string  .  no_data  )  ; 
(  (  ViewGroup  )  listView  .  getParent  (  )  )  .  addView  (  view  ,  new   ViewGroup  .  LayoutParams  (  LinearLayout  .  LayoutParams  .  MATCH_PARENT  ,  LinearLayout  .  LayoutParams  .  WRAP_CONTENT  )  )  ; 
listView  .  setEmptyView  (  view  )  ; 
} 




public   static   LinearLayout   getLoading  (  Activity   activity  )  { 
LinearLayout   myLoadLayout  =  new   LinearLayout  (  activity  )  ; 
myLoadLayout  .  setMinimumHeight  (  60  )  ; 
myLoadLayout  .  setGravity  (  Gravity  .  CENTER  )  ; 
myLoadLayout  .  setOrientation  (  LinearLayout  .  HORIZONTAL  )  ; 
myLoadLayout  .  setBackgroundColor  (  Color  .  rgb  (  231  ,  231  ,  231  )  )  ; 
ProgressBar   pb  =  new   ProgressBar  (  activity  )  ; 
pb  .  setPadding  (  0  ,  0  ,  15  ,  0  )  ; 
myLoadLayout  .  addView  (  pb  ,  new   LinearLayout  .  LayoutParams  (  LinearLayout  .  LayoutParams  .  WRAP_CONTENT  ,  LinearLayout  .  LayoutParams  .  WRAP_CONTENT  )  )  ; 
TextView   textView  =  new   TextView  (  activity  )  ; 
textView  .  setText  (  R  .  string  .  loading  )  ; 
myLoadLayout  .  addView  (  textView  ,  new   LinearLayout  .  LayoutParams  (  LinearLayout  .  LayoutParams  .  WRAP_CONTENT  ,  LinearLayout  .  LayoutParams  .  WRAP_CONTENT  )  )  ; 
return   myLoadLayout  ; 
} 




public   static   RelativeLayout   getLoadMore  (  Activity   activity  ,  final   View  .  OnClickListener   loadButtonOnClickListener  )  { 
final   RelativeLayout   relativeLayout  =  LayoutUtils  .  findLayoutById  (  R  .  layout  .  loadmore  ,  activity  )  ; 
relativeLayout  .  setOnClickListener  (  new   View  .  OnClickListener  (  )  { 

public   void   onClick  (  View   v  )  { 
final   ProgressBar   progressBar  =  (  ProgressBar  )  relativeLayout  .  findViewById  (  R  .  id  .  load_more_progressbar  )  ; 
progressBar  .  setVisibility  (  View  .  VISIBLE  )  ; 
loadButtonOnClickListener  .  onClick  (  v  )  ; 
} 
}  )  ; 
return   relativeLayout  ; 
} 





public   static   void   clearLoading  (  Activity   activity  )  { 
ProgressBar   progressBar  =  (  ProgressBar  )  activity  .  findViewById  (  R  .  id  .  load_more_progressbar  )  ; 
if  (  progressBar  !=  null  )  { 
progressBar  .  setVisibility  (  View  .  INVISIBLE  )  ; 
} 
} 




public   static   void   setListViewHeight  (  ListView   lv  )  { 
ListAdapter   la  =  lv  .  getAdapter  (  )  ; 
if  (  null  ==  la  )  return  ; 
int   h  =  0  ; 
int   cnt  =  la  .  getCount  (  )  ; 
View   item  =  null  ; 
for  (  int   i  =  0  ;  i  <  cnt  ;  i  ++  )  { 
item  =  la  .  getView  (  i  ,  null  ,  lv  )  ; 
item  .  measure  (  0  ,  0  )  ; 
h  +=  item  .  getMeasuredHeight  (  )  ; 
} 
ViewGroup  .  LayoutParams   lp  =  lv  .  getLayoutParams  (  )  ; 
lp  .  height  =  h  +  (  lv  .  getDividerHeight  (  )  *  (  cnt  -  1  )  )  ; 
lv  .  setLayoutParams  (  lp  )  ; 
} 




public   static   String   getAddress  (  )  { 
try  { 
for  (  Enumeration  <  NetworkInterface  >  en  =  NetworkInterface  .  getNetworkInterfaces  (  )  ;  en  .  hasMoreElements  (  )  ;  )  { 
NetworkInterface   intf  =  en  .  nextElement  (  )  ; 
for  (  Enumeration  <  InetAddress  >  enumIpAddr  =  intf  .  getInetAddresses  (  )  ;  enumIpAddr  .  hasMoreElements  (  )  ;  )  { 
InetAddress   inetAddress  =  enumIpAddr  .  nextElement  (  )  ; 
if  (  !  inetAddress  .  isLoopbackAddress  (  )  )  { 
return   inetAddress  .  getHostAddress  (  )  .  toString  (  )  ; 
} 
} 
} 
}  catch  (  SocketException   ex  )  { 
LogUtil  .  error  (  ex  )  ; 
} 
return   StringUtils  .  EMPTY  ; 
} 




public   static   String   getMD5  (  String   value  )  { 
if  (  StringUtils  .  isBlank  (  value  )  )  return   null  ; 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md  .  update  (  value  .  getBytes  (  "UTF-8"  )  )  ; 
return   toHexString  (  md  .  digest  (  )  )  ; 
}  catch  (  Throwable   e  )  { 
return   null  ; 
} 
} 




private   static   String   toHexString  (  byte  [  ]  bytes  )  { 
StringBuffer   sb  =  new   StringBuffer  (  bytes  .  length  *  2  )  ; 
for  (  int   i  =  0  ;  i  <  bytes  .  length  ;  i  ++  )  { 
sb  .  append  (  Character  .  forDigit  (  (  bytes  [  i  ]  &  0XF0  )  >  >  4  ,  16  )  )  ; 
sb  .  append  (  Character  .  forDigit  (  bytes  [  i  ]  &  0X0F  ,  16  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 




public   static   int   dip2px  (  float   dipValue  )  { 
final   float   scale  =  BaseApp  .  getBaseApp  (  )  .  getResources  (  )  .  getDisplayMetrics  (  )  .  density  ; 
return  (  int  )  (  dipValue  *  scale  +  0.5f  )  ; 
} 




public   static   String   formatDouble  (  double   d  )  { 
NumberFormat   nf  =  NumberFormat  .  getInstance  (  )  ; 
nf  .  setMaximumFractionDigits  (  2  )  ; 
nf  .  setMinimumFractionDigits  (  2  )  ; 
return   nf  .  format  (  d  )  ; 
} 




public   static   void   closeInputStream  (  InputStream   is  )  { 
if  (  is  ==  null  )  return  ; 
try  { 
is  .  close  (  )  ; 
}  catch  (  Throwable   e  )  { 
LogUtil  .  error  (  e  )  ; 
} 
} 




public   static   void   closeOutputStream  (  OutputStream   os  )  { 
if  (  os  ==  null  )  return  ; 
try  { 
os  .  close  (  )  ; 
}  catch  (  Throwable   e  )  { 
LogUtil  .  error  (  e  )  ; 
} 
} 
} 

