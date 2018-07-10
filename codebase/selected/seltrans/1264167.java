package   de  .  juwimm  .  util  .  mail  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Properties  ; 
import   javax  .  mail  .  Address  ; 
import   javax  .  mail  .  BodyPart  ; 
import   javax  .  mail  .  MessagingException  ; 
import   javax  .  mail  .  Session  ; 
import   javax  .  mail  .  Transport  ; 
import   javax  .  mail  .  Message  .  RecipientType  ; 
import   javax  .  mail  .  internet  .  InternetAddress  ; 
import   javax  .  mail  .  internet  .  MimeBodyPart  ; 
import   javax  .  mail  .  internet  .  MimeMessage  ; 
import   javax  .  mail  .  internet  .  MimeMultipart  ; 
import   javax  .  naming  .  InitialContext  ; 
import   javax  .  naming  .  NamingException  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 





public   class   Mail  { 

private   static   Log   log  =  LogFactory  .  getLog  (  Mail  .  class  )  ; 

private   MimeMessage   message  =  null  ; 

private   String   encoding  =  "UTF-8"  ; 

private   String   messageText  =  null  ; 

private   ArrayList  <  MimeBodyPart  >  attachments  =  null  ; 

private   Hashtable  <  String  ,  String  >  tempFileNameMappings  =  null  ; 






public   Mail  (  String   mailDS  )  { 
try  { 
Session   session  =  (  Session  )  new   InitialContext  (  )  .  lookup  (  mailDS  )  ; 
if  (  session  ==  null  )  { 
throw   new   IllegalArgumentException  (  "session could not be initialized with mailDS '"  +  mailDS  +  "'"  )  ; 
} 
initializeMail  (  session  )  ; 
}  catch  (  NamingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 







public   Mail  (  Properties   testProperties  )  { 
Session   session  =  Session  .  getDefaultInstance  (  testProperties  )  ; 
if  (  session  ==  null  )  { 
throw   new   IllegalArgumentException  (  "session could not be initialized with specified properties"  )  ; 
} 
initializeMail  (  session  )  ; 
} 

private   void   initializeMail  (  Session   session  )  { 
this  .  message  =  new   MimeMessage  (  session  )  ; 
this  .  attachments  =  new   ArrayList  <  MimeBodyPart  >  (  )  ; 
this  .  tempFileNameMappings  =  new   Hashtable  <  String  ,  String  >  (  )  ; 
} 




public   void   setEncodingToISO  (  )  { 
this  .  encoding  =  "ISO-8859-1"  ; 
} 






public   void   setFrom  (  String   from  )  { 
try  { 
this  .  message  .  setFrom  (  new   InternetAddress  (  from  )  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 






public   String   getFrom  (  )  { 
try  { 
return   this  .  message  .  getFrom  (  )  [  0  ]  .  toString  (  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
return  ""  ; 
} 
} 






public   void   setTo  (  String  [  ]  to  )  { 
if  (  to  !=  null  )  { 
try  { 
for  (  int   i  =  0  ;  i  <  to  .  length  ;  i  ++  )  { 
this  .  message  .  addRecipient  (  RecipientType  .  TO  ,  new   InternetAddress  (  to  [  i  ]  )  )  ; 
} 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 






public   void   addTo  (  String   to  )  { 
if  (  to  !=  null  )  { 
try  { 
this  .  message  .  addRecipient  (  RecipientType  .  TO  ,  new   InternetAddress  (  to  )  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 

public   String  [  ]  getTo  (  )  { 
return   getRecipients  (  RecipientType  .  TO  )  ; 
} 






public   void   setCc  (  String  [  ]  cc  )  { 
if  (  cc  !=  null  )  { 
try  { 
for  (  int   i  =  0  ;  i  <  cc  .  length  ;  i  ++  )  { 
this  .  message  .  addRecipient  (  RecipientType  .  CC  ,  new   InternetAddress  (  cc  [  i  ]  )  )  ; 
} 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 






public   void   addCc  (  String   cc  )  { 
if  (  cc  !=  null  )  { 
try  { 
this  .  message  .  addRecipient  (  RecipientType  .  CC  ,  new   InternetAddress  (  cc  )  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 






public   String  [  ]  getCc  (  )  { 
return   getRecipients  (  RecipientType  .  CC  )  ; 
} 






public   void   setBcc  (  String  [  ]  bcc  )  { 
if  (  bcc  !=  null  )  { 
try  { 
for  (  int   i  =  0  ;  i  <  bcc  .  length  ;  i  ++  )  { 
this  .  message  .  addRecipient  (  RecipientType  .  BCC  ,  new   InternetAddress  (  bcc  [  i  ]  )  )  ; 
} 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 






public   void   addBcc  (  String   bcc  )  { 
if  (  bcc  !=  null  )  { 
try  { 
this  .  message  .  addRecipient  (  RecipientType  .  BCC  ,  new   InternetAddress  (  bcc  )  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 
} 






public   String  [  ]  getBcc  (  )  { 
return   getRecipients  (  RecipientType  .  BCC  )  ; 
} 






public   void   setSubject  (  String   subject  )  { 
try  { 
this  .  message  .  setSubject  (  subject  ,  this  .  encoding  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
} 






public   String   getSubject  (  )  { 
String   result  =  ""  ; 
try  { 
result  =  this  .  message  .  getSubject  (  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
} 
return   result  ; 
} 










public   void   setBody  (  String   bodyText  )  { 
this  .  messageText  =  bodyText  ; 
} 

public   void   appendBody  (  String   bodyText  )  { 
this  .  messageText  =  this  .  messageText  +  bodyText  ; 
} 






public   boolean   isMailSendable  (  )  { 
try  { 
Address  [  ]  fromArray  =  this  .  message  .  getFrom  (  )  ; 
return  (  this  .  message  .  getSubject  (  )  !=  null  &&  getRecipients  (  RecipientType  .  TO  )  .  length  >  0  &&  fromArray  !=  null  &&  fromArray  .  length  >  0  &&  fromArray  [  0  ]  !=  null  &&  fromArray  [  0  ]  .  toString  (  )  .  length  (  )  >  0  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
return   false  ; 
} 
} 












public   void   addAttachmentFromInputStream  (  InputStream   inputStream  ,  String   fileName  ,  String   mimeType  )  { 
FileOutputStream   fileOutputStream  =  null  ; 
try  { 
String   oldFileName  =  fileName  ; 
String   suffix  =  null  ; 
int   dotIndex  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  dotIndex  >=  0  )  { 
suffix  =  fileName  .  substring  (  dotIndex  )  ; 
fileName  =  fileName  .  substring  (  0  ,  dotIndex  )  ; 
} 
while  (  fileName  .  length  (  )  <  3  )  { 
fileName  =  "0"  +  fileName  ; 
} 
File   tempFile  =  File  .  createTempFile  (  fileName  ,  suffix  )  ; 
tempFile  .  deleteOnExit  (  )  ; 
this  .  tempFileNameMappings  .  put  (  tempFile  .  getAbsolutePath  (  )  ,  oldFileName  )  ; 
fileOutputStream  =  new   FileOutputStream  (  tempFile  )  ; 
byte  [  ]  buffer  =  new   byte  [  512  ]  ; 
for  (  int   length  =  0  ;  (  length  =  inputStream  .  read  (  buffer  )  )  !=  -  1  ;  )  { 
fileOutputStream  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
this  .  addAttachmentFromFile  (  tempFile  .  getAbsolutePath  (  )  )  ; 
}  catch  (  IOException   exception  )  { 
log  .  error  (  "Error creating attachment "  +  fileName  +  " from inputStream"  ,  exception  )  ; 
}  finally  { 
try  { 
if  (  fileOutputStream  !=  null  )  { 
fileOutputStream  .  close  (  )  ; 
} 
if  (  inputStream  !=  null  )  { 
inputStream  .  close  (  )  ; 
} 
}  catch  (  IOException   exception  )  { 
} 
} 
} 






public   void   addAttachmentFromFile  (  String   fileNameWithPath  )  { 
try  { 
File   file  =  new   File  (  fileNameWithPath  )  ; 
MimeBodyPart   bodyPart  =  new   MimeBodyPart  (  )  ; 
bodyPart  .  attachFile  (  file  )  ; 
if  (  this  .  tempFileNameMappings  .  containsKey  (  fileNameWithPath  )  )  { 
bodyPart  .  setFileName  (  this  .  tempFileNameMappings  .  get  (  fileNameWithPath  )  )  ; 
} 
this  .  attachments  .  add  (  bodyPart  )  ; 
}  catch  (  IOException   exception  )  { 
log  .  error  (  "Error opening file "  +  fileNameWithPath  ,  exception  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  "Error creating attachment comprising file "  +  fileNameWithPath  )  ; 
} 
} 











public   void   addAttachmentFromUrl  (  String   httpUrl  )  { 
try  { 
URL   url  =  new   URL  (  httpUrl  )  ; 
String   fileName  =  url  .  getPath  (  )  ; 
int   slashIndex  =  fileName  .  lastIndexOf  (  "/"  )  ; 
if  (  slashIndex  >=  0  )  { 
fileName  =  fileName  .  substring  (  slashIndex  +  1  )  ; 
} 
String   oldFileName  =  fileName  ; 
String   suffix  =  null  ; 
int   dotIndex  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  dotIndex  >=  0  )  { 
suffix  =  fileName  .  substring  (  dotIndex  )  ; 
fileName  =  fileName  .  substring  (  0  ,  dotIndex  )  ; 
} 
while  (  fileName  .  length  (  )  <  3  )  { 
fileName  =  "0"  +  fileName  ; 
} 
File   tempFile  =  File  .  createTempFile  (  fileName  ,  suffix  )  ; 
tempFile  .  deleteOnExit  (  )  ; 
this  .  tempFileNameMappings  .  put  (  tempFile  .  getAbsolutePath  (  )  ,  oldFileName  )  ; 
InputStream   inputStream  =  url  .  openStream  (  )  ; 
FileOutputStream   fileOutputStream  =  new   FileOutputStream  (  tempFile  )  ; 
byte  [  ]  buffer  =  new   byte  [  512  ]  ; 
for  (  int   length  =  0  ;  (  length  =  inputStream  .  read  (  buffer  )  )  !=  -  1  ;  )  { 
fileOutputStream  .  write  (  buffer  ,  0  ,  length  )  ; 
} 
fileOutputStream  .  close  (  )  ; 
inputStream  .  close  (  )  ; 
this  .  addAttachmentFromFile  (  tempFile  .  getAbsolutePath  (  )  )  ; 
}  catch  (  MalformedURLException   exception  )  { 
log  .  error  (  "The URL is invalid: "  +  httpUrl  ,  exception  )  ; 
}  catch  (  IOException   exception  )  { 
log  .  error  (  "Error opening the URL "  +  httpUrl  ,  exception  )  ; 
} 
} 

public   boolean   sendPlaintextMail  (  )  { 
try  { 
if  (  this  .  attachments  .  size  (  )  >  0  )  { 
MimeMultipart   multiPart  =  new   MimeMultipart  (  "mixed"  )  ; 
BodyPart   plainTextBodyPart  =  new   MimeBodyPart  (  )  ; 
plainTextBodyPart  .  setText  (  this  .  messageText  )  ; 
multiPart  .  addBodyPart  (  plainTextBodyPart  )  ; 
for  (  int   i  =  0  ;  i  <  this  .  attachments  .  size  (  )  ;  i  ++  )  { 
multiPart  .  addBodyPart  (  this  .  attachments  .  get  (  i  )  )  ; 
} 
this  .  message  .  setContent  (  multiPart  )  ; 
}  else  { 
this  .  message  .  setText  (  this  .  messageText  ,  this  .  encoding  ,  "plain"  )  ; 
} 
this  .  message  .  saveChanges  (  )  ; 
doSend  (  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  "Error sending plain text mail"  ,  exception  )  ; 
return   false  ; 
} 
return   true  ; 
} 

public   boolean   sendHtmlMail  (  String   alternativePlaintextBody  )  { 
try  { 
if  (  this  .  attachments  .  size  (  )  >  0  )  { 
MimeMultipart   mainMultiPart  =  new   MimeMultipart  (  "mixed"  )  ; 
if  (  alternativePlaintextBody  !=  null  )  { 
MimeMultipart   alternativeMultiPart  =  new   MimeMultipart  (  "alternative"  )  ; 
MimeBodyPart   plainTextBodyPart  =  new   MimeBodyPart  (  )  ; 
MimeBodyPart   htmlBodyPart  =  new   MimeBodyPart  (  )  ; 
plainTextBodyPart  .  setText  (  alternativePlaintextBody  ,  this  .  encoding  ,  "plain"  )  ; 
htmlBodyPart  .  setText  (  this  .  messageText  ,  this  .  encoding  ,  "html"  )  ; 
alternativeMultiPart  .  addBodyPart  (  plainTextBodyPart  )  ; 
alternativeMultiPart  .  addBodyPart  (  htmlBodyPart  )  ; 
MimeBodyPart   containerBodyPart  =  new   MimeBodyPart  (  )  ; 
containerBodyPart  .  setContent  (  alternativeMultiPart  )  ; 
mainMultiPart  .  addBodyPart  (  containerBodyPart  )  ; 
}  else  { 
MimeBodyPart   htmlBodyPart  =  new   MimeBodyPart  (  )  ; 
htmlBodyPart  .  setText  (  this  .  messageText  ,  this  .  encoding  ,  "html"  )  ; 
mainMultiPart  .  addBodyPart  (  htmlBodyPart  )  ; 
} 
for  (  int   i  =  0  ;  i  <  this  .  attachments  .  size  (  )  ;  i  ++  )  { 
mainMultiPart  .  addBodyPart  (  this  .  attachments  .  get  (  i  )  )  ; 
} 
this  .  message  .  setContent  (  mainMultiPart  )  ; 
}  else  { 
if  (  alternativePlaintextBody  !=  null  )  { 
MimeMultipart   mainMultipart  =  new   MimeMultipart  (  "alternative"  )  ; 
MimeBodyPart   plainTextBodyPart  =  new   MimeBodyPart  (  )  ; 
MimeBodyPart   htmlBodyPart  =  new   MimeBodyPart  (  )  ; 
plainTextBodyPart  .  setText  (  alternativePlaintextBody  ,  this  .  encoding  ,  "plain"  )  ; 
htmlBodyPart  .  setText  (  this  .  messageText  ,  this  .  encoding  ,  "html"  )  ; 
mainMultipart  .  addBodyPart  (  plainTextBodyPart  )  ; 
mainMultipart  .  addBodyPart  (  htmlBodyPart  )  ; 
this  .  message  .  setContent  (  mainMultipart  )  ; 
}  else  { 
this  .  message  .  setContent  (  this  .  messageText  ,  "text/html"  )  ; 
} 
} 
this  .  message  .  saveChanges  (  )  ; 
doSend  (  )  ; 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  "Error sending HTML mail"  ,  exception  )  ; 
return   false  ; 
} 
return   true  ; 
} 







public   void   addNameToFileNameMappings  (  String   absoluteFileName  ,  String   displayName  )  { 
this  .  tempFileNameMappings  .  put  (  absoluteFileName  ,  displayName  )  ; 
} 







private   String  [  ]  getRecipients  (  RecipientType   type  )  { 
String  [  ]  result  ; 
try  { 
Address  [  ]  addresses  =  this  .  message  .  getRecipients  (  type  )  ; 
result  =  new   String  [  addresses  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  addresses  .  length  ;  i  ++  )  { 
result  [  i  ]  =  addresses  [  i  ]  .  toString  (  )  ; 
} 
}  catch  (  MessagingException   exception  )  { 
log  .  error  (  exception  )  ; 
result  =  new   String  [  0  ]  ; 
} 
return   result  ; 
} 






private   void   doSend  (  )  throws   MessagingException  { 
this  .  message  .  setSentDate  (  new   Date  (  )  )  ; 
Transport  .  send  (  this  .  message  )  ; 
if  (  this  .  attachments  .  size  (  )  >  0  )  { 
Enumeration  <  String  >  enumeration  =  this  .  tempFileNameMappings  .  keys  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
File   file  =  new   File  (  enumeration  .  nextElement  (  )  )  ; 
if  (  file  .  exists  (  )  )  { 
if  (  !  file  .  delete  (  )  )  { 
log  .  error  (  "Temp file "  +  file  .  getAbsolutePath  (  )  +  " could not be deleted!"  )  ; 
} 
} 
} 
this  .  attachments  .  clear  (  )  ; 
this  .  tempFileNameMappings  .  clear  (  )  ; 
} 
} 

public   void   clearTempFiles  (  )  { 
if  (  this  .  attachments  .  size  (  )  >  0  )  { 
Enumeration  <  String  >  enumeration  =  this  .  tempFileNameMappings  .  keys  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
File   file  =  new   File  (  enumeration  .  nextElement  (  )  )  ; 
if  (  file  .  exists  (  )  )  { 
if  (  !  file  .  delete  (  )  )  { 
log  .  error  (  "Temp file "  +  file  .  getAbsolutePath  (  )  +  " could not be deleted!"  )  ; 
} 
} 
} 
this  .  attachments  .  clear  (  )  ; 
this  .  tempFileNameMappings  .  clear  (  )  ; 
} 
} 
} 

