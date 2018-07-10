package   org  .  amiwall  .  delivery  ; 

import   org  .  amiwall  .  plugin  .  AbstractPlugin  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  amiwall  .  user  .  User  ; 
import   org  .  apache  .  commons  .  net  .  io  .  Util  ; 
import   org  .  apache  .  commons  .  net  .  smtp  .  SMTPClient  ; 
import   org  .  apache  .  commons  .  net  .  smtp  .  SMTPReply  ; 
import   org  .  apache  .  commons  .  net  .  smtp  .  SimpleSMTPHeader  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  io  .  IOException  ; 
import   org  .  amiwall  .  user  .  BasicUser  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Calendar  ; 
import   org  .  jdom  .  Element  ; 
import   java  .  util  .  Iterator  ; 






public   class   EmailDelivery   extends   AbstractPlugin   implements   Delivery  { 

private   static   Logger   log  =  Logger  .  getLogger  (  "org.amiwall.delivery.EmailDelivery"  )  ; 




protected   String   server  =  null  ; 




protected   String   from  =  null  ; 




protected   Set   ccList  =  new   HashSet  (  )  ; 






public   String   getName  (  )  { 
return  "EmailDelivery"  ; 
} 







public   void   digest  (  Element   root  )  { 
setServer  (  root  .  getChildTextTrim  (  "server"  )  )  ; 
setFrom  (  root  .  getChildTextTrim  (  "from"  )  )  ; 
for  (  Iterator   i  =  root  .  getChildren  (  "cc"  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Element   child  =  (  Element  )  i  .  next  (  )  ; 
addCc  (  child  .  getTextTrim  (  )  )  ; 
} 
} 






public   void   activate  (  )  throws   Exception  { 
super  .  activate  (  )  ; 
if  (  from  ==  null  )  { 
throw   new   NullPointerException  (  "from is NULL, this needs to be configured"  )  ; 
} 
if  (  server  ==  null  )  { 
throw   new   NullPointerException  (  "server is NULL, this needs to be configured"  )  ; 
} 
} 






public   void   setFrom  (  String   from  )  { 
this  .  from  =  from  ; 
} 






public   void   setServer  (  String   server  )  { 
this  .  server  =  server  ; 
} 






public   void   addCc  (  String   cc  )  { 
ccList  .  add  (  cc  )  ; 
} 








public   void   deliver  (  User   user  ,  String   subject  ,  String   message  )  { 
if  (  user   instanceof   BasicUser  )  { 
BasicUser   basicUser  =  (  BasicUser  )  user  ; 
String   recipient  =  basicUser  .  getEmail  (  )  ; 
if  (  recipient  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "delivery To:"  +  basicUser  .  getName  (  )  +  " Sub:"  +  subject  )  ; 
try  { 
SimpleSMTPHeader   header  =  getHeader  (  recipient  ,  subject  )  ; 
SMTPClient   client  =  new   SMTPClient  (  )  ; 
client  .  connect  (  server  )  ; 
if  (  !  SMTPReply  .  isPositiveCompletion  (  client  .  getReplyCode  (  )  )  )  { 
client  .  disconnect  (  )  ; 
System  .  err  .  println  (  "SMTP server refused connection."  )  ; 
System  .  exit  (  1  )  ; 
} 
client  .  login  (  )  ; 
setClientHeaders  (  client  ,  recipient  )  ; 
writeMessage  (  client  ,  header  ,  message  )  ; 
client  .  logout  (  )  ; 
client  .  disconnect  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Sent sucessfully"  )  ; 
}  catch  (  IOException   e  )  { 
log  .  error  (  "Failed to send email to "  +  user  .  getName  (  )  ,  e  )  ; 
} 
}  else  { 
log  .  error  (  "Cant send email to "  +  basicUser  .  getName  (  )  +  " - dont have an email address"  )  ; 
} 
}  else  { 
log  .  error  (  "Cant send email to "  +  user  .  getName  (  )  +  " - cant work out his email address."  )  ; 
} 
} 








protected   SimpleSMTPHeader   getHeader  (  String   recipient  ,  String   subject  )  { 
SimpleSMTPHeader   header  =  new   SimpleSMTPHeader  (  from  ,  recipient  ,  subject  )  ; 
for  (  Iterator   i  =  ccList  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   cc  =  (  String  )  i  .  next  (  )  ; 
header  .  addCC  (  cc  )  ; 
} 
SimpleDateFormat   df  =  new   SimpleDateFormat  (  "EEE, dd MMM yyyy HH:mm:ss Z"  )  ; 
String   date  =  df  .  format  (  Calendar  .  getInstance  (  )  .  getTime  (  )  )  ; 
header  .  addHeaderField  (  "Date"  ,  date  )  ; 
return   header  ; 
} 






protected   void   setClientHeaders  (  SMTPClient   client  ,  String   recipient  )  throws   IOException  { 
client  .  setSender  (  from  )  ; 
client  .  addRecipient  (  recipient  )  ; 
for  (  Iterator   i  =  ccList  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String   cc  =  (  String  )  i  .  next  (  )  ; 
client  .  addRecipient  (  cc  )  ; 
} 
} 








protected   void   writeMessage  (  SMTPClient   client  ,  SimpleSMTPHeader   header  ,  String   message  )  throws   IOException  { 
Writer   writer  =  client  .  sendMessageData  (  )  ; 
if  (  writer  !=  null  )  { 
writer  .  write  (  header  .  toString  (  )  )  ; 
writer  .  write  (  message  )  ; 
writer  .  close  (  )  ; 
client  .  completePendingCommand  (  )  ; 
} 
} 
} 

