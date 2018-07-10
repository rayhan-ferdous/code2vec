package   org  .  xaware  .  server  .  engine  .  channel  .  smtp  ; 

import   java  .  util  .  Properties  ; 
import   javax  .  mail  .  MessagingException  ; 
import   javax  .  mail  .  NoSuchProviderException  ; 
import   javax  .  mail  .  Session  ; 
import   javax  .  mail  .  Transport  ; 
import   org  .  xaware  .  server  .  engine  .  IScopedChannel  ; 
import   org  .  xaware  .  shared  .  util  .  XAwareConstants  ; 
import   org  .  xaware  .  shared  .  util  .  XAwareException  ; 
import   org  .  xaware  .  shared  .  util  .  logging  .  XAwareLogger  ; 








public   class   SmtpTemplate   implements   IScopedChannel  { 

private   static   String   className  =  SmtpTemplate  .  class  .  getName  (  )  ; 

private   static   XAwareLogger   lf  =  XAwareLogger  .  getXAwareLogger  (  className  )  ; 


SmtpChannelSpecification   channelObject  =  null  ; 


boolean   initialized  =  false  ; 


Transport   m_mailTransport  ; 




private   SmtpTemplate  (  )  { 
} 




public   SmtpTemplate  (  SmtpBizDriver   driver  )  throws   XAwareException  { 
super  (  )  ; 
channelObject  =  (  SmtpChannelSpecification  )  driver  .  createChannelObject  (  )  ; 
} 







public   Type   getScopedChannelType  (  )  { 
return   Type  .  SMTP  ; 
} 





public   void   close  (  boolean   success  )  { 
if  (  m_mailTransport  !=  null  )  { 
try  { 
m_mailTransport  .  close  (  )  ; 
}  catch  (  MessagingException   e  )  { 
lf  .  debug  (  "Transport Close failed with exception: "  +  e  .  getMessage  (  )  )  ; 
} 
} 
initialized  =  false  ; 
} 






public   void   initResources  (  )  throws   XAwareException  { 
if  (  !  initialized  )  { 
final   Properties   props  =  new   Properties  (  )  ; 
props  .  setProperty  (  SmtpChannelSpecification  .  AUTH_KEY  ,  channelObject  .  getProperty  (  SmtpChannelSpecification  .  AUTH_KEY  )  )  ; 
final   Session   session  =  Session  .  getDefaultInstance  (  props  ,  null  )  ; 
String   sServer  =  channelObject  .  getProperty  (  XAwareConstants  .  BIZDRIVER_SERVER  )  ; 
int   port  =  Integer  .  parseInt  (  channelObject  .  getProperty  (  XAwareConstants  .  BIZDRIVER_PORT  )  )  ; 
String   sUserId  =  channelObject  .  getProperty  (  XAwareConstants  .  BIZDRIVER_USER  )  ; 
String   sPassword  =  channelObject  .  getProperty  (  XAwareConstants  .  BIZDRIVER_PWD  )  ; 
try  { 
m_mailTransport  =  session  .  getTransport  (  "smtp"  )  ; 
m_mailTransport  .  connect  (  sServer  ,  port  ,  sUserId  ,  sPassword  )  ; 
}  catch  (  NoSuchProviderException   e  )  { 
String   msg  =  "Exception initializing transport: "  +  e  .  getMessage  (  )  ; 
lf  .  debug  (  msg  )  ; 
throw   new   XAwareException  (  msg  )  ; 
}  catch  (  MessagingException   e  )  { 
String   msg  =  "Exception initializing transport: "  +  e  .  getMessage  (  )  ; 
lf  .  debug  (  msg  )  ; 
throw   new   XAwareException  (  msg  )  ; 
} 
initialized  =  true  ; 
} 
} 




public   SmtpChannelSpecification   getChannelObject  (  )  { 
return   channelObject  ; 
} 




public   Transport   getMailTransport  (  )  throws   XAwareException  { 
if  (  !  initialized  )  { 
initResources  (  )  ; 
} 
return   m_mailTransport  ; 
} 
} 

