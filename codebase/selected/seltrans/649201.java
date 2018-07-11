package   jp  .  locky  .  stumbler  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  net  .  ConnectException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Vector  ; 

import   org  .  apache  .  commons  .  httpclient  .  Header  ; 
import   org  .  apache  .  commons  .  httpclient  .  HttpClient  ; 
import   org  .  apache  .  commons  .  httpclient  .  HttpStatus  ; 
import   org  .  apache  .  commons  .  httpclient  .  URI  ; 
import   org  .  apache  .  commons  .  httpclient  .  cookie  .  CookiePolicy  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  GetMethod  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  PostMethod  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  multipart  .  FilePart  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  multipart  .  MultipartRequestEntity  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  multipart  .  Part  ; 
import   org  .  apache  .  commons  .  httpclient  .  methods  .  multipart  .  StringPart  ; 
import   org  .  apache  .  commons  .  httpclient  .  params  .  HttpMethodParams  ; 
import   org  .  eclipse  .  swt  .  SWT  ; 
import   org  .  eclipse  .  swt  .  events  .  SelectionAdapter  ; 
import   org  .  eclipse  .  swt  .  events  .  SelectionEvent  ; 
import   org  .  eclipse  .  swt  .  layout  .  GridData  ; 
import   org  .  eclipse  .  swt  .  layout  .  GridLayout  ; 
import   org  .  eclipse  .  swt  .  layout  .  RowData  ; 
import   org  .  eclipse  .  swt  .  layout  .  RowLayout  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Button  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Combo  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Composite  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Display  ; 
import   org  .  eclipse  .  swt  .  widgets  .  FileDialog  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Label  ; 
import   org  .  eclipse  .  swt  .  widgets  .  MessageBox  ; 
import   org  .  eclipse  .  swt  .  widgets  .  ProgressBar  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Shell  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Text  ; 





public   class   LogUploader   implements   Parameter  ,  Runnable  { 


public   static   final   String   LOCKY_JP_CONTRACT  =  "以下では、Locky.jpにおいて、あなたの提供された情報がどのように利用されるか、また、情報提供の際に注意すべき点について説明しています。\n"  + 
"情報提供者は、以下の１から４の規約に同意したものとみなします。\n\n"  + 
"注意深くお読みいただいて、今後も同意していただける場合は「はい」を、\n"  + 
"今回だけ同意していただける場合は「いいえ」を、\n"  + 
"同意していただけない場合は「キャンセル」を押してください。\n\n\n"  + 
"１．提供された情報の利用について\n\n"  + 
"提供された情報（以下、提供データ）は、Locky.jpにおいて、位置推定のために利用されます。\n"  + 
"提供データは、以後の利用において、提供者がわからない形で利用されます。\n"  + 
"提供データは、他の収集データと合わせ、Locky.jpにおいて配布されることがあります。\n"  + 
"また、今後の位置推定手法に適切な形式に変換され、配布・利用されることがあります。\n"  + 
"また、アプリケーションやCD-ROM等に含まれて配布・利用されることがあります。\n"  + 
"Locky.jpは、提供データが提供された時点でこれらをLocky.jp運営のため無償で自由に利用し、\n"  + 
"また第三者に対して同様の利用権を許諾する権利を有するものとします。\n\n"  + 
"２．情報提供とランキングについて\n\n"  + 
"Locky.jpに情報提供を行うためには、アカウントの登録が必要です。また提供されたデータの個数等に応じて、\n"  + 
"ランキング等が表示される仕組みが準備されています。\n"  + 
"アカウント名がランキングに表示されますので、アカウント作成の際には、注意してください。\n\n"  + 
"３．情報提供の際に注意すべき点について\n\n"  + 
"無線LAN基地局の位置情報は、基地局が移動したり、変更されてしまうと意味がありません。\n"  + 
"提供データには、可能な限り、長期的に利用可能な無線LAN基地局のデータが含まれるようにお願いします。\n"  + 
"イベント等の際に利用される短期的なデータについては、別の受付手法をとる場合があります。\n\n"  + 
"４．免責事項\n\n"  + 
"Locky.jpは、提供データの内容については、原則として関知いたしません。\n"  + 
"情報提供者は、自身の責任において提供データの作成を行ってください。\n"  + 
"また、Locky.jpが配布する、位置推定システム用のデータは、提供データにもとづいて作成されるため、その正しさは保障されません。\n"  + 
"また、Locky.jpは、任意の理由で提供データの削除・修正することがあります。\n"  + 
"Locky.jpが提供するシステムやデータを用いて、何らかの損害や障害が生じたとしても、Locky.jpは一切の責任を負いません。\n"  + 
"また、Locky.jpは情報収集や位置推定用のデータの配布等のサービスを任意の理由で中断することができます。\n"  + 
"サービスの中断に関して、Locky.jpは、一切の責任を負いません。\n\n"  + 
"以上"  ; 


private   LogUploader   uploader_  ; 
private   Display   display_  ; 


private   File  [  ]  logFiles_  ; 


private   String   accountName_  ; 
private   String   accountPassword_  ; 


private   final   Text   log  ; 


private   ProgressBar   filesProgress_  ; 


private   Label   progressLabel_  ; 


private   Button   uploadButton_  ; 
private   Button   closeButton_  ; 


private   boolean   isRunning_  =  false  ; 







public   LogUploader  (  Display   display  ,  File  [  ]  files  )  { 
logFiles_  =  files  ; 
uploader_  =  this  ; 
display_  =  display  ; 

GridLayout   gridLayout  =  new   GridLayout  (  )  ; 
gridLayout  .  numColumns  =  2  ; 
GridData   gridData  ; 

final   Shell   shell  =  new   Shell  (  display_  ,  SWT  .  APPLICATION_MODAL  |  SWT  .  DIALOG_TRIM  )  ; 
shell  .  setText  (  "Log File Upload"  )  ; 
shell  .  setLayout  (  gridLayout  )  ; 


Composite   accountComposite  =  new   Composite  (  shell  ,  SWT  .  NULL  )  ; 
gridLayout  =  new   GridLayout  (  )  ; 
gridLayout  .  numColumns  =  3  ; 
accountComposite  .  setLayout  (  gridLayout  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
accountComposite  .  setLayoutData  (  gridData  )  ; 


Label   nameLabel  =  new   Label  (  accountComposite  ,  SWT  .  NULL  )  ; 
nameLabel  .  setText  (  "Name"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL  |  GridData  .  HORIZONTAL_ALIGN_END  )  ; 
nameLabel  .  setLayoutData  (  gridData  )  ; 

final   Text   nameText  =  new   Text  (  accountComposite  ,  SWT  .  SINGLE  |  SWT  .  BORDER  )  ; 
nameText  .  setText  (  readParameter  (  ACCOUNT_NAME  )  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
nameText  .  setLayoutData  (  gridData  )  ; 

final   Button   nameSave  =  new   Button  (  accountComposite  ,  SWT  .  CHECK  )  ; 
nameSave  .  setText  (  "Save"  )  ; 
nameSave  .  setLayoutData  (  gridData  )  ; 
if  (  readParameter  (  SAVE_ACCOUNT_NAME  )  .  equals  (  "true"  )  )  { 
nameSave  .  setSelection  (  true  )  ; 
} 


Label   passwordLabel  =  new   Label  (  accountComposite  ,  SWT  .  NULL  )  ; 
passwordLabel  .  setText  (  "Password"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL  |  GridData  .  HORIZONTAL_ALIGN_END  )  ; 
passwordLabel  .  setLayoutData  (  gridData  )  ; 

final   Text   passwordText  =  new   Text  (  accountComposite  ,  SWT  .  SINGLE  |  SWT  .  BORDER  |  SWT  .  PASSWORD  )  ; 
passwordText  .  setText  (  readParameter  (  ACCOUNT_PASSWORD  )  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
passwordText  .  setLayoutData  (  gridData  )  ; 

final   Button   passwordSave  =  new   Button  (  accountComposite  ,  SWT  .  CHECK  )  ; 
passwordSave  .  setText  (  "Save"  )  ; 
passwordSave  .  setLayoutData  (  gridData  )  ; 
if  (  readParameter  (  SAVE_ACCOUNT_PASSWORD  )  .  equals  (  "true"  )  )  { 
passwordSave  .  setSelection  (  true  )  ; 
} 


Label   webBrowserLabel  =  new   Label  (  accountComposite  ,  SWT  .  NULL  )  ; 
webBrowserLabel  .  setText  (  "Web Browser"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL  |  GridData  .  HORIZONTAL_ALIGN_END  )  ; 
webBrowserLabel  .  setLayoutData  (  gridData  )  ; 

final   Text   webBrowser  =  new   Text  (  accountComposite  ,  SWT  .  SINGLE  |  SWT  .  BORDER  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
gridData  .  widthHint  =  200  ; 
webBrowser  .  setText  (  readParameter  (  WEB_BROWSER  )  )  ; 
webBrowser  .  setLayoutData  (  gridData  )  ; 

Button   selectBrowserButton  =  new   Button  (  accountComposite  ,  SWT  .  PUSH  )  ; 
selectBrowserButton  .  setText  (  "Select"  )  ; 
selectBrowserButton  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 
public   void   widgetSelected  (  SelectionEvent   e  )  { 
FileDialog   dialog  =  new   FileDialog  (  shell  )  ; 
dialog  .  setFilterPath  (  WEB_BROWSER  )  ; 
String   browserAddress  =  dialog  .  open  (  )  ; 
if  (  browserAddress  !=  null  )  { 
webBrowser  .  setText  (  browserAddress  )  ; 
} 
} 
}  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL  )  ; 
selectBrowserButton  .  setLayoutData  (  gridData  )  ; 


Label   uploadResultLabel  =  new   Label  (  accountComposite  ,  SWT  .  NULL  )  ; 
uploadResultLabel  .  setText  (  "Upload Result"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL  |  GridData  .  HORIZONTAL_ALIGN_END  )  ; 
uploadResultLabel  .  setLayoutData  (  gridData  )  ; 

final   Combo   uploadResult  =  new   Combo  (  accountComposite  ,  SWT  .  READ_ONLY  )  ; 
uploadResult  .  add  (  "MAP"  )  ; 
uploadResult  .  add  (  "TEXT"  )  ; 
uploadResult  .  add  (  "NONE"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
gridData  .  horizontalSpan  =  2  ; 
uploadResult  .  setLayoutData  (  gridData  )  ; 

String   result  =  readParameter  (  UPLOAD_RESULT  )  ; 
if  (  result  .  equals  (  "MAP"  )  )  { 
uploadResult  .  select  (  0  )  ; 
} 
else   if  (  result  .  equals  (  "TEXT"  )  )  { 
uploadResult  .  select  (  1  )  ; 
} 
else  { 
uploadResult  .  select  (  2  )  ; 
} 


Label   uploadFileLabel  =  new   Label  (  accountComposite  ,  SWT  .  NULL  )  ; 
uploadFileLabel  .  setText  (  "Upload Files"  )  ; 
gridData  =  new   GridData  (  GridData  .  HORIZONTAL_ALIGN_END  )  ; 
uploadFileLabel  .  setLayoutData  (  gridData  )  ; 

Text   uploadFiles  =  new   Text  (  accountComposite  ,  SWT  .  BORDER  |  SWT  .  MULTI  |  SWT  .  V_SCROLL  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
gridData  .  horizontalSpan  =  2  ; 
uploadFiles  .  setLayoutData  (  gridData  )  ; 



log  =  new   Text  (  shell  ,  SWT  .  BORDER  |  SWT  .  MULTI  |  SWT  .  V_SCROLL  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
gridData  .  minimumHeight  =  200  ; 
gridData  .  minimumWidth  =  300  ; 
log  .  setLayoutData  (  gridData  )  ; 



filesProgress_  =  new   ProgressBar  (  shell  ,  SWT  .  SMOOTH  )  ; 
int   totalSize  =  0  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
totalSize  +=  files  [  i  ]  .  length  (  )  ; 
} 
filesProgress_  .  setMaximum  (  totalSize  )  ; 
filesProgress_  .  setMinimum  (  0  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
gridData  .  horizontalSpan  =  2  ; 
filesProgress_  .  setLayoutData  (  gridData  )  ; 



progressLabel_  =  new   Label  (  shell  ,  SWT  .  NULL  )  ; 
progressLabel_  .  setText  (  "0 / 0"  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
progressLabel_  .  setLayoutData  (  gridData  )  ; 



RowLayout   rowLayout  =  new   RowLayout  (  SWT  .  HORIZONTAL  )  ; 
rowLayout  .  justify  =  true  ; 
rowLayout  .  marginHeight  =  0  ; 

Composite   buttonComposite  =  new   Composite  (  shell  ,  SWT  .  NULL  )  ; 
buttonComposite  .  setLayout  (  rowLayout  )  ; 
gridData  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
buttonComposite  .  setLayoutData  (  gridData  )  ; 

uploadButton_  =  new   Button  (  buttonComposite  ,  SWT  .  NULL  )  ; 
uploadButton_  .  setText  (  "Upload"  )  ; 
uploadButton_  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 
public   void   widgetSelected  (  SelectionEvent   e  )  { 

if  (  (  nameText  .  getText  (  )  .  equals  (  ""  )  )  ||  (  passwordText  .  getText  (  )  .  equals  (  ""  )  )  )  { 
MessageBox   msg  =  new   MessageBox  (  shell  ,  SWT  .  OK  |  SWT  .  ICON_WARNING  )  ; 
msg  .  setMessage  (  "アカウント情報を入力して下さい"  )  ; 
msg  .  open  (  )  ; 
return  ; 
} 


if  (  !  readParameter  (  UPLOAD_AGREEMENT  )  .  equals  (  "true"  )  )  { 
MessageBox   msg  =  new   MessageBox  (  shell  ,  SWT  .  YES  |  SWT  .  NO  |  SWT  .  CANCEL  )  ; 
msg  .  setMessage  (  LOCKY_JP_CONTRACT  )  ; 
msg  .  setText  (  "Locky.jp 情報提供に関する規約"  )  ; 
int   result  =  msg  .  open  (  )  ; 
if  (  result  ==  SWT  .  YES  )  { 

saveParameter  (  UPLOAD_AGREEMENT  ,  "true"  )  ; 
} 
else   if  (  result  ==  SWT  .  NO  )  { 

} 
else   if  (  result  ==  SWT  .  CANCEL  )  { 

return  ; 
} 
} 


accountName_  =  nameText  .  getText  (  )  ; 
accountPassword_  =  passwordText  .  getText  (  )  ; 


if  (  nameSave  .  getSelection  (  )  )  { 
saveParameter  (  ACCOUNT_NAME  ,  nameText  .  getText  (  )  )  ; 
saveParameter  (  SAVE_ACCOUNT_NAME  ,  "true"  )  ; 
} 
else  { 
saveParameter  (  ACCOUNT_NAME  ,  ""  )  ; 
saveParameter  (  SAVE_ACCOUNT_NAME  ,  "false"  )  ; 
} 

if  (  passwordSave  .  getSelection  (  )  )  { 
saveParameter  (  ACCOUNT_PASSWORD  ,  passwordText  .  getText  (  )  )  ; 
saveParameter  (  SAVE_ACCOUNT_PASSWORD  ,  "true"  )  ; 
} 
else  { 
saveParameter  (  ACCOUNT_PASSWORD  ,  ""  )  ; 
saveParameter  (  SAVE_ACCOUNT_PASSWORD  ,  "false"  )  ; 
} 

saveParameter  (  WEB_BROWSER  ,  webBrowser  .  getText  (  )  )  ; 
saveParameter  (  UPLOAD_RESULT  ,  uploadResult  .  getItem  (  uploadResult  .  getSelectionIndex  (  )  )  )  ; 


isRunning_  =  true  ; 
uploadButton_  .  setEnabled  (  false  )  ; 

new   Thread  (  uploader_  )  .  start  (  )  ; 

} 
}  )  ; 
uploadButton_  .  setLayoutData  (  new   RowData  (  80  ,  25  )  )  ; 

closeButton_  =  new   Button  (  buttonComposite  ,  SWT  .  NULL  )  ; 
closeButton_  .  setText  (  "Cancel"  )  ; 
closeButton_  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 
public   void   widgetSelected  (  SelectionEvent   e  )  { 


if  (  !  isRunning_  )  { 
shell  .  dispose  (  )  ; 

return  ; 
} 


if  (  isRunning_  )  { 

isRunning_  =  false  ; 
} 

for  (  int   i  =  0  ;  (  (  i  <  5  )  &&  (  !  isRunning_  )  )  ;  i  ++  )  { 
try  { 

Thread  .  sleep  (  1000  )  ; 
} 
catch  (  Exception   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
} 

shell  .  dispose  (  )  ; 
} 
}  )  ; 
closeButton_  .  setLayoutData  (  new   RowData  (  80  ,  25  )  )  ; 


shell  .  pack  (  )  ; 


String   fileNames  =  ""  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
fileNames  +=  files  [  i  ]  .  getName  (  )  +  "\n"  ; 
} 
uploadFiles  .  setText  (  fileNames  )  ; 


shell  .  open  (  )  ; 
} 




public   void   enableClose  (  )  { 
if  (  !  display_  .  isDisposed  (  )  )  { 
display_  .  asyncExec  (  new   Runnable  (  )  { 
public   void   run  (  )  { 
closeButton_  .  setText  (  "Close"  )  ; 
} 
}  )  ; 
} 
} 




public   void   enableUpload  (  )  { 
if  (  !  display_  .  isDisposed  (  )  )  { 
display_  .  asyncExec  (  new   Runnable  (  )  { 
public   void   run  (  )  { 
uploadButton_  .  setEnabled  (  true  )  ; 
} 
}  )  ; 
} 
} 





protected   HttpClient   establishConnection  (  )  { 
try  { 
String   hostName  =  readParameter  (  WEB_HOST  )  ; 


setLog  (  "サーバへのアクセスを開始します"  )  ; 
HttpClient   client  =  new   HttpClient  (  )  ; 
client  .  getHostConfiguration  (  )  .  setHost  (  hostName  ,  80  ,  "http"  )  ; 
client  .  getParams  (  )  .  setCookiePolicy  (  CookiePolicy  .  BROWSER_COMPATIBILITY  )  ; 


GetMethod   getMehod  =  new   GetMethod  (  "/member/index.jsp"  )  ; 
getMehod  .  setRequestHeader  (  "Keep-Alive"  ,  "300"  )  ; 
getMehod  .  setRequestHeader  (  "Connection"  ,  "keep-alive"  )  ; 


setLog  (  "サーバからの応答を待っています"  )  ; 
int   statusCode  =  client  .  executeMethod  (  getMehod  )  ; 
if  (  statusCode  ==  200  )  { 
setLog  (  "アクセス成功"  )  ; 
} 
else   if  (  statusCode  <  400  )  { 

} 
else   if  (  statusCode  <  500  )  { 
switch  (  statusCode  )  { 
case   400  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Bad Request"  )  ;  break  ; 
case   401  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Unauthorized"  )  ;  break  ; 
case   402  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Payment Required"  )  ;  break  ; 
case   403  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Forbidden"  )  ;  break  ; 
case   404  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Found"  )  ;  break  ; 
case   405  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Method Not Allowed"  )  ;  break  ; 
case   406  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Acceptable"  )  ;  break  ; 
case   407  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Proxy Authentication Required"  )  ;  break  ; 
case   408  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request Time-out"  )  ;  break  ; 
case   409  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Conflict"  )  ;  break  ; 
case   410  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Gone"  )  ;  break  ; 
case   411  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Length Required"  )  ;  break  ; 
case   412  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Precondition Failed"  )  ;  break  ; 
case   413  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request Entity Too Large"  )  ;  break  ; 
case   414  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request-URI Too Large"  )  ;  break  ; 
case   415  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Unsupported Media Type"  )  ;  break  ; 
} 
setLog  (  "サーバに接続できません"  )  ; 
getMehod  .  releaseConnection  (  )  ; 
isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 
else   if  (  statusCode  <  600  )  { 
switch  (  statusCode  )  { 
case   500  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Internal Server Error"  )  ;  break  ; 
case   501  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Implemented"  )  ;  break  ; 
case   502  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Bad Gateway"  )  ;  break  ; 
case   503  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Service Unavailable"  )  ;  break  ; 
case   504  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Gateway Time-out"  )  ;  break  ; 
case   505  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : HTTP Version not supported"  )  ;  break  ; 
} 
setLog  (  "サーバに接続できません"  )  ; 
getMehod  .  releaseConnection  (  )  ; 
isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 
Md5   md5  =  new   Md5  (  )  ; 

setLog  (  "ログインを開始します"  )  ; 
PostMethod   loginMethod  =  new   PostMethod  (  "/member/j_security_check"  )  ; 
loginMethod  .  setRequestHeader  (  "Keep-Alive"  ,  "300"  )  ; 
loginMethod  .  setRequestHeader  (  "Connection"  ,  "keep-alive"  )  ; 
loginMethod  .  addParameter  (  "j_username"  ,  accountName_  )  ; 
loginMethod  .  addParameter  (  "j_password"  ,  md5  .  toDigest  (  accountPassword_  )  )  ; 
loginMethod  .  addParameter  (  "submit"  ,  "Login"  )  ; 

statusCode  =  client  .  executeMethod  (  loginMethod  )  ; 
loginMethod  .  releaseConnection  (  )  ; 


Header   locationHeader  =  loginMethod  .  getResponseHeader  (  "Location"  )  ; 
if  (  locationHeader  !=  null  )  { 
String   url  =  locationHeader  .  getValue  (  )  ; 
if  (  url  .  endsWith  (  "error.html"  )  )  { 
setLog  (  "入力されたアカウントかパスワードに誤りがあります\r\nログイン失敗"  )  ; 
isRunning_  =  false  ; 
return   null  ; 
} 
else   if  (  (  url  ==  null  )  ||  (  url  .  equals  (  ""  )  )  )  { 
setLog  (  "ログイン失敗"  )  ; 
isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 

getMehod  .  setURI  (  new   URI  (  url  ,  false  )  )  ; 
statusCode  =  client  .  executeMethod  (  getMehod  )  ; 
if  (  statusCode  ==  200  )  { 
setLog  (  "ログイン成功"  )  ; 
return   client  ; 
} 
else   if  (  statusCode  <  400  )  { 

} 
else   if  (  statusCode  <  500  )  { 
switch  (  statusCode  )  { 
case   400  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Bad Request"  )  ;  break  ; 
case   401  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Unauthorized"  )  ;  break  ; 
case   402  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Payment Required"  )  ;  break  ; 
case   403  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Forbidden"  )  ;  break  ; 
case   404  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Found"  )  ;  break  ; 
case   405  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Method Not Allowed"  )  ;  break  ; 
case   406  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Acceptable"  )  ;  break  ; 
case   407  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Proxy Authentication Required"  )  ;  break  ; 
case   408  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request Time-out"  )  ;  break  ; 
case   409  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Conflict"  )  ;  break  ; 
case   410  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Gone"  )  ;  break  ; 
case   411  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Length Required"  )  ;  break  ; 
case   412  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Precondition Failed"  )  ;  break  ; 
case   413  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request Entity Too Large"  )  ;  break  ; 
case   414  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Request-URI Too Large"  )  ;  break  ; 
case   415  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Unsupported Media Type"  )  ;  break  ; 
} 
setLog  (  "サーバに接続できません"  )  ; 
getMehod  .  releaseConnection  (  )  ; 
isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 
else   if  (  statusCode  <  600  )  { 
switch  (  statusCode  )  { 
case   500  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Internal Server Error"  )  ;  break  ; 
case   501  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Not Implemented"  )  ;  break  ; 
case   502  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Bad Gateway"  )  ;  break  ; 
case   503  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Service Unavailable"  )  ;  break  ; 
case   504  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : Gateway Time-out"  )  ;  break  ; 
case   505  : 
setLog  (  "HTTP Status Code "  +  String  .  valueOf  (  statusCode  )  +  " : HTTP Version not supported"  )  ;  break  ; 
} 
setLog  (  "サーバに接続できません"  )  ; 
getMehod  .  releaseConnection  (  )  ; 
isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 
getMehod  .  releaseConnection  (  )  ; 
} 
else  { 
setLog  (  "ログイン失敗"  )  ; 
} 

isRunning_  =  false  ; 
enableUpload  (  )  ; 
return   null  ; 
} 
catch  (  ConnectException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
setLog  (  "サーバとの接続を拒否されました"  )  ; 
isRunning_  =  false  ; 
return   null  ; 
} 
catch  (  IOException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
isRunning_  =  false  ; 
return   null  ; 
} 
} 

public   String   readParameter  (  String   key  )  { 
try  { 
FileInputStream   fis  =  new   FileInputStream  (  CONFIG_FILE  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  fis  )  ; 
BufferedReader   br  =  new   BufferedReader  (  isr  )  ; 

String   line  =  ""  ; 
while  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  startsWith  (  key  )  )  { 
return   line  .  substring  (  key  .  length  (  )  +  1  )  ; 
} 
} 

fis  .  close  (  )  ; 
isr  .  close  (  )  ; 
br  .  close  (  )  ; 

return  ""  ; 
} 
catch  (  Exception   exception  )  { 
exception  .  printStackTrace  (  )  ; 

return  ""  ; 
} 
} 





protected   void   releaseConnection  (  HttpClient   client  )  { 
try  { 
GetMethod   getMehod  =  new   GetMethod  (  "/member/logout.jsp"  )  ; 
getMehod  .  setRequestHeader  (  "Keep-Alive"  ,  "300"  )  ; 
getMehod  .  setRequestHeader  (  "Connection"  ,  "keep-alive"  )  ; 


client  .  executeMethod  (  getMehod  )  ; 
} 
catch  (  IOException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
} 





public   void   run  (  )  { 
upload  (  )  ; 
} 

public   void   saveParameter  (  String   key  ,  String   value  )  { 
try  { 

ArrayList  <  String  >  parameters  =  new   ArrayList  <  String  >  (  10  )  ; 

FileInputStream   fis  =  new   FileInputStream  (  CONFIG_FILE  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  fis  )  ; 
BufferedReader   br  =  new   BufferedReader  (  isr  )  ; 

boolean   regist  =  false  ; 
String   line  =  ""  ; 
while  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  startsWith  (  key  )  )  { 
parameters  .  add  (  key  +  "="  +  value  )  ; 
regist  =  true  ; 
} 
else  { 
parameters  .  add  (  line  )  ; 
} 
} 


if  (  regist  ==  false  )  { 
parameters  .  add  (  key  +  "="  +  value  )  ; 
} 

fis  .  close  (  )  ; 
isr  .  close  (  )  ; 
br  .  close  (  )  ; 


FileOutputStream   fos  =  new   FileOutputStream  (  CONFIG_FILE  ,  false  )  ; 
OutputStreamWriter   osw  =  new   OutputStreamWriter  (  fos  )  ; 
BufferedWriter   bw  =  new   BufferedWriter  (  osw  )  ; 

for  (  int   i  =  0  ;  i  <  parameters  .  size  (  )  ;  i  ++  )  { 
bw  .  write  (  parameters  .  get  (  i  )  )  ; 
bw  .  newLine  (  )  ; 
} 
bw  .  flush  (  )  ; 

fos  .  close  (  )  ; 
osw  .  close  (  )  ; 
bw  .  close  (  )  ; 
} 
catch  (  Exception   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
} 





public   void   setLog  (  final   String   msg  )  { 
if  (  !  display_  .  isDisposed  (  )  )  { 
display_  .  asyncExec  (  new   Runnable  (  )  { 
public   void   run  (  )  { 
String   logs  =  log  .  getText  (  )  ; 
logs  +=  msg  +  "\r\n"  ; 
log  .  setText  (  logs  )  ; 
} 
}  )  ; 
} 
} 





public   void   setProgress  (  final   int   number  )  { 
if  (  !  display_  .  isDisposed  (  )  )  { 
display_  .  asyncExec  (  new   Runnable  (  )  { 
public   void   run  (  )  { 
int   finishSize  =  0  ; 
int   totalSize  =  0  ; 
for  (  int   i  =  0  ;  i  <  logFiles_  .  length  ;  i  ++  )  { 
totalSize  +=  logFiles_  [  i  ]  .  length  (  )  ; 
if  (  i  <  number  )  { 
finishSize  =  totalSize  ; 
} 
} 

String   percent  =  String  .  valueOf  (  (  double  )  finishSize  /  (  double  )  totalSize  *  100  )  +  "000"  ; 
percent  =  percent  .  substring  (  0  ,  percent  .  indexOf  (  "."  )  +  3  )  ; 

filesProgress_  .  setSelection  (  finishSize  )  ; 
progressLabel_  .  setText  (  String  .  valueOf  (  finishSize  )  +  " / "  +  String  .  valueOf  (  totalSize  )  +  "\n"  +  percent  +  "%"  )  ; 
} 
}  )  ; 
} 
} 




public   void   upload  (  )  { 
try  { 
HttpClient   client  =  establishConnection  (  )  ; 

if  (  client  ==  null  )  { 
return  ; 
} 

Vector  <  File  >  successFiles  =  new   Vector  <  File  >  (  10  )  ; 
String   startDB  =  ""  ; 
String   endDB  =  ""  ; 


setLog  (  "アップロード開始"  )  ; 
for  (  int   i  =  0  ;  i  <  logFiles_  .  length  ;  i  ++  )  { 
if  (  !  isRunning_  )  { 

releaseConnection  (  client  )  ; 

break  ; 
} 

if  (  logFiles_  [  i  ]  .  exists  (  )  )  { 

FileInputStream   fis  =  new   FileInputStream  (  logFiles_  [  i  ]  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  fis  )  ; 
BufferedReader   br  =  new   BufferedReader  (  isr  )  ; 

String   comment  =  ""  ; 
String   line  =  null  ; 
if  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  startsWith  (  "#LockyStumbler Log"  )  )  { 

if  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  startsWith  (  "#"  )  )  { 
comment  =  line  .  substring  (  1  )  ; 


while  (  comment  .  endsWith  (  " "  )  )  { 
comment  =  comment  .  substring  (  0  ,  comment  .  length  (  )  -  1  )  ; 
} 
} 
} 
} 
} 

fis  .  close  (  )  ; 
isr  .  close  (  )  ; 
br  .  close  (  )  ; 


PostMethod   uploadMethod  =  new   PostMethod  (  "/member/result.html"  )  ; 

uploadMethod  .  getParams  (  )  .  setBooleanParameter  (  HttpMethodParams  .  USE_EXPECT_CONTINUE  ,  true  )  ; 
Part  [  ]  parts  =  {  new   StringPart  (  "from"  ,  "logbrowser"  )  ,  new   StringPart  (  "comment"  ,  comment  )  ,  new   FilePart  (  "fileName"  ,  logFiles_  [  i  ]  ,  "text/plain"  ,  null  )  }  ; 
uploadMethod  .  setRequestEntity  (  new   MultipartRequestEntity  (  parts  ,  uploadMethod  .  getParams  (  )  )  )  ; 


client  .  getHttpConnectionManager  (  )  .  getParams  (  )  .  setConnectionTimeout  (  5000  )  ; 


int   statusCode  =  client  .  executeMethod  (  uploadMethod  )  ; 

if  (  statusCode  ==  HttpStatus  .  SC_OK  )  { 

String   response  =  uploadMethod  .  getResponseBodyAsString  (  )  ; 

String   start  =  response  .  substring  (  0  ,  response  .  indexOf  (  "\t"  )  )  ; 
String   end  =  response  .  substring  (  response  .  indexOf  (  "\t"  )  +  1  )  ; 


if  (  startDB  .  equals  (  ""  )  )  { 
startDB  =  start  ; 
} 


if  (  endDB  .  equals  (  ""  )  )  { 
endDB  =  end  ; 
} 


if  (  Integer  .  parseInt  (  endDB  )  <  Integer  .  parseInt  (  end  )  )  { 
endDB  =  end  ; 
} 


successFiles  .  add  (  logFiles_  [  i  ]  )  ; 


if  (  isRunning_  )  { 
setLog  (  logFiles_  [  i  ]  .  getName  (  )  +  "\t[ SUCCESS ]"  )  ; 
} 
} 

uploadMethod  .  releaseConnection  (  )  ; 

setProgress  (  i  +  1  )  ; 
} 
} 
if  (  isRunning_  )  { 
setLog  (  "アップロード終了"  )  ; 
} 




String   view  =  readParameter  (  UPLOAD_RESULT  )  ; 
if  (  !  isRunning_  )  { 

} 
else   if  (  view  .  equals  (  "MAP"  )  )  { 

MessageDigest   md5  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md5  .  update  (  accountName_  .  getBytes  (  )  )  ; 
byte  [  ]  digest  =  md5  .  digest  (  )  ; 


String   userNameDigest  =  ""  ; 
for  (  int   i  =  0  ;  i  <  digest  .  length  ;  i  ++  )  { 
int   d  =  digest  [  i  ]  ; 
if  (  d  <  0  )  { 

d  +=  256  ; 
} 
if  (  d  <  16  )  { 

userNameDigest  +=  "0"  ; 
} 


userNameDigest  +=  Integer  .  toString  (  d  ,  16  )  ; 
} 


if  (  startDB  .  equals  (  ""  )  )  { 
startDB  =  "0"  ; 
} 
if  (  endDB  .  equals  (  ""  )  )  { 
endDB  =  "0"  ; 
} 


if  (  startDB  .  equals  (  "0"  )  &&  endDB  .  equals  (  "0"  )  )  { 
setLog  (  "新規発見数： 0"  )  ; 
} 
else  { 
ProcessBuilder   process  =  new   ProcessBuilder  (  readParameter  (  WEB_BROWSER  )  ,  "http://"  +  readParameter  (  WEB_HOST  )  +  "/service/logviewer.html?user="  +  userNameDigest  +  "&start="  +  startDB  +  "&end="  +  endDB  )  ; 
process  .  start  (  )  ; 
} 
} 
else   if  (  view  .  equals  (  "TEXT"  )  )  { 
if  (  startDB  .  equals  (  ""  )  ||  endDB  .  equals  (  ""  )  )  { 

setLog  (  "受信情報が欠けているため表示できません"  )  ; 
} 
else  { 
int   newCount  =  Integer  .  parseInt  (  endDB  )  -  Integer  .  parseInt  (  startDB  )  ; 
setLog  (  "新規発見数： "  +  String  .  valueOf  (  newCount  )  )  ; 
} 
} 



for  (  int   i  =  0  ;  i  <  successFiles  .  size  (  )  ;  i  ++  )  { 
try  { 
RandomAccessFile   file  =  new   RandomAccessFile  (  successFiles  .  get  (  i  )  ,  "rw"  )  ; 


String   line  ; 
String   seekString  =  ""  ; 
while  (  (  line  =  file  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  startsWith  (  "#LockyStumbler Log"  )  )  { 


int   version  =  Integer  .  parseInt  (  line  .  substring  (  "#LockyStumbler Log Version "  .  length  (  )  )  )  ; 
if  (  version  <  2  )  { 
return  ; 
} 




file  .  readLine  (  )  ; 
long   pos  =  file  .  getFilePointer  (  )  ; 


line  =  file  .  readLine  (  )  ; 
String  [  ]  token  =  line  .  substring  (  1  )  .  split  (  "[|]"  )  ; 
for  (  int   j  =  0  ;  j  <  token  .  length  ;  j  ++  )  { 
if  (  token  [  j  ]  .  startsWith  (  "UPLOAD="  )  )  { 


file  .  seek  (  pos  +  "|UPLOAD="  .  length  (  )  )  ; 
file  .  write  (  "T"  .  getBytes  (  )  )  ; 
} 
else  { 

pos  +=  (  "|"  +  token  [  j  ]  )  .  length  (  )  ; 
} 
} 
} 
} 

file  .  close  (  )  ; 
} 
catch  (  FileNotFoundException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
catch  (  IOException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
} 


if  (  !  isRunning_  )  { 

isRunning_  =  true  ; 
return  ; 
} 


isRunning_  =  false  ; 
enableClose  (  )  ; 
releaseConnection  (  client  )  ; 
} 
catch  (  IOException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
} 
catch  (  NoSuchAlgorithmException   exception  )  { 
exception  .  printStackTrace  (  )  ; 
setLog  (  "JREのバージョンが古いため表示できませんでした"  )  ; 
} 
} 
} 

