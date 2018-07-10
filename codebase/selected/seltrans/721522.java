package   net  .  sourceforge  .  pebble  .  util  ; 

import   net  .  sourceforge  .  pebble  .  domain  .  Blog  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   net  .  sourceforge  .  pebble  .  web  .  validation  .  ValidationContext  ; 
import   net  .  sourceforge  .  pebble  .  PebbleContext  ; 
import   javax  .  mail  .  Message  ; 
import   javax  .  mail  .  Session  ; 
import   javax  .  mail  .  Transport  ; 
import   javax  .  mail  .  internet  .  AddressException  ; 
import   javax  .  mail  .  internet  .  InternetAddress  ; 
import   javax  .  mail  .  internet  .  MimeMessage  ; 
import   javax  .  naming  .  Context  ; 
import   javax  .  naming  .  InitialContext  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 






public   class   MailUtils  { 


private   static   Log   log  =  LogFactory  .  getLog  (  MailUtils  .  class  )  ; 


private   static   ExecutorService   pool  =  Executors  .  newFixedThreadPool  (  1  )  ; 









public   static   void   sendMail  (  Session   session  ,  Blog   blog  ,  String   to  ,  String   subject  ,  String   message  )  { 
Collection   set  =  new   HashSet  (  )  ; 
set  .  add  (  to  )  ; 
sendMail  (  session  ,  blog  ,  set  ,  new   HashSet  (  )  ,  new   HashSet  (  )  ,  subject  ,  message  )  ; 
} 









public   static   void   sendMail  (  Session   session  ,  Blog   blog  ,  Collection   to  ,  String   subject  ,  String   message  )  { 
sendMail  (  session  ,  blog  ,  to  ,  new   HashSet  (  )  ,  new   HashSet  (  )  ,  subject  ,  message  )  ; 
} 










public   static   void   sendMail  (  Session   session  ,  Blog   blog  ,  Collection   to  ,  Collection   cc  ,  String   subject  ,  String   message  )  { 
sendMail  (  session  ,  blog  ,  to  ,  cc  ,  new   HashSet  (  )  ,  subject  ,  message  )  ; 
} 











public   static   void   sendMail  (  Session   session  ,  Blog   blog  ,  Collection   to  ,  Collection   cc  ,  Collection   bcc  ,  String   subject  ,  String   message  )  { 
Runnable   r  =  new   SendMailRunnable  (  session  ,  blog  ,  to  ,  cc  ,  bcc  ,  subject  ,  message  )  ; 
pool  .  execute  (  r  )  ; 
} 





static   class   SendMailRunnable   implements   Runnable  { 


private   Session   session  ; 


private   Blog   blog  ; 


private   Collection   to  ; 


private   Collection   cc  ; 


private   Collection   bcc  ; 


private   String   subject  ; 


private   String   message  ; 












public   SendMailRunnable  (  Session   session  ,  Blog   blog  ,  Collection   to  ,  Collection   cc  ,  Collection   bcc  ,  String   subject  ,  String   message  )  { 
this  .  session  =  session  ; 
this  .  blog  =  blog  ; 
this  .  to  =  to  ; 
this  .  cc  =  cc  ; 
this  .  bcc  =  bcc  ; 
this  .  subject  =  subject  ; 
this  .  message  =  message  ; 
} 




public   void   run  (  )  { 
try  { 
Message   msg  =  new   MimeMessage  (  session  )  ; 
msg  .  setFrom  (  new   InternetAddress  (  blog  .  getFirstEmailAddress  (  )  ,  blog  .  getName  (  )  )  )  ; 
Collection   internetAddresses  =  new   HashSet  (  )  ; 
Iterator   it  =  to  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
internetAddresses  .  add  (  new   InternetAddress  (  it  .  next  (  )  .  toString  (  )  )  )  ; 
} 
msg  .  addRecipients  (  Message  .  RecipientType  .  TO  ,  (  InternetAddress  [  ]  )  internetAddresses  .  toArray  (  new   InternetAddress  [  ]  {  }  )  )  ; 
internetAddresses  =  new   HashSet  (  )  ; 
it  =  cc  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
internetAddresses  .  add  (  new   InternetAddress  (  it  .  next  (  )  .  toString  (  )  )  )  ; 
} 
msg  .  addRecipients  (  Message  .  RecipientType  .  CC  ,  (  InternetAddress  [  ]  )  internetAddresses  .  toArray  (  new   InternetAddress  [  ]  {  }  )  )  ; 
internetAddresses  =  new   HashSet  (  )  ; 
it  =  bcc  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
internetAddresses  .  add  (  new   InternetAddress  (  it  .  next  (  )  .  toString  (  )  )  )  ; 
} 
msg  .  addRecipients  (  Message  .  RecipientType  .  BCC  ,  (  InternetAddress  [  ]  )  internetAddresses  .  toArray  (  new   InternetAddress  [  ]  {  }  )  )  ; 
msg  .  setSubject  (  subject  )  ; 
msg  .  setSentDate  (  new   Date  (  )  )  ; 
msg  .  setContent  (  message  ,  "text/html"  )  ; 
log  .  debug  (  "From : "  +  blog  .  getName  (  )  +  " ("  +  blog  .  getFirstEmailAddress  (  )  +  ")"  )  ; 
log  .  debug  (  "Subject : "  +  subject  )  ; 
log  .  debug  (  "Message : "  +  message  )  ; 
Transport  .  send  (  msg  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Notification e-mail could not be sent"  ,  e  )  ; 
} 
} 
} 







public   static   Session   createSession  (  )  throws   Exception  { 
String   ref  =  PebbleContext  .  getInstance  (  )  .  getConfiguration  (  )  .  getSmtpHost  (  )  ; 
if  (  ref  .  startsWith  (  "java:comp/env"  )  )  { 
Context   ctx  =  new   InitialContext  (  )  ; 
return  (  Session  )  ctx  .  lookup  (  ref  )  ; 
}  else  { 
Properties   props  =  new   Properties  (  )  ; 
props  .  put  (  "mail.smtp.host"  ,  ref  )  ; 
return   Session  .  getDefaultInstance  (  props  ,  null  )  ; 
} 
} 







public   static   void   validate  (  String   email  ,  ValidationContext   context  )  { 
if  (  email  !=  null  )  { 
try  { 
InternetAddress   ia  =  new   InternetAddress  (  email  ,  true  )  ; 
ia  .  validate  (  )  ; 
}  catch  (  AddressException   aex  )  { 
context  .  addError  (  aex  .  getMessage  (  )  +  ": "  +  email  )  ; 
} 
} 
} 
} 

