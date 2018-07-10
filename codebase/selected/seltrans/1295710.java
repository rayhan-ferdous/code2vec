package   com  .  sleepsocial  .  authentication  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  util  .  Properties  ; 
import   javax  .  jdo  .  JDOException  ; 
import   javax  .  mail  .  Message  ; 
import   javax  .  mail  .  MessagingException  ; 
import   javax  .  mail  .  Session  ; 
import   javax  .  mail  .  Transport  ; 
import   javax  .  mail  .  internet  .  AddressException  ; 
import   javax  .  mail  .  internet  .  InternetAddress  ; 
import   javax  .  mail  .  internet  .  MimeMessage  ; 
import   javax  .  servlet  .  http  .  Cookie  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   org  .  mortbay  .  log  .  Log  ; 
import   com  .  google  .  appengine  .  api  .  datastore  .  Key  ; 
import   com  .  google  .  appengine  .  api  .  datastore  .  KeyFactory  ; 
import   com  .  sleepsocial  .  config  .  AppConfig  ; 
import   com  .  sleepsocial  .  logging  .  Logger  ; 
import   com  .  sleepsocial  .  persistency  .  PMF  ; 
import   com  .  sleepsocial  .  persistency  .  entity  .  user  .  UserProfile  ; 

public   class   AuthenticationManager  { 

private   static   Logger   logger  =  Logger  .  getInstance  (  AuthenticationManager  .  class  .  getSimpleName  (  )  )  ; 

private   static   final   String   HASH_METHOD  =  "SHA-1"  ; 

private   static   final   String   HASH_SALT  =  "DrJQ9Yl3NlAmp80rAeW5"  ; 

private   static   final   String   AUTH_ATTR  =  "user_key"  ; 




private   static   final   int   COOKIE_MAX_AGE  =  60  *  60  *  24  *  14  ; 

public   static   UserProfile   authenticate  (  String   email  ,  String   password  ,  HttpServletResponse   res  )  throws   AuthenticationException  { 
return   authenticateWithHash  (  email  ,  generatePasswordHash  (  password  )  ,  res  )  ; 
} 









public   static   UserProfile   authenticateWithHash  (  String   email  ,  String   hash  ,  HttpServletResponse   res  )  throws   AuthenticationException  { 
logger  .  i  (  "Authenticating user ["  +  email  +  "]"  )  ; 
UserProfile   profile  =  UserProfile  .  getUserByEmail  (  email  )  ; 
if  (  profile  ==  null  )  { 
logger  .  w  (  "Error authenticating user ["  +  email  +  "]"  )  ; 
throw   new   AuthenticationException  (  "user email does not exists"  )  ; 
} 
if  (  profile  .  getPasswordHash  (  )  .  equals  (  hash  )  )  { 
saveAuthenticatedUser  (  res  ,  profile  )  ; 
return   profile  ; 
}  else  { 
return   null  ; 
} 
} 







public   static   boolean   isUserAuthenticated  (  HttpServletRequest   req  )  { 
return   getAuthenticatedUser  (  req  )  !=  null  ; 
} 







public   static   Cookie   getAuthCookie  (  HttpServletRequest   req  )  { 
Cookie  [  ]  cookies  =  req  .  getCookies  (  )  ; 
if  (  cookies  !=  null  )  { 
for  (  Cookie   cookie  :  cookies  )  { 
if  (  cookie  .  getName  (  )  .  equals  (  AUTH_ATTR  )  &&  cookie  .  getValue  (  )  .  length  (  )  >=  10  )  { 
return   cookie  ; 
} 
} 
} 
return   null  ; 
} 








public   static   boolean   saveAuthenticatedUser  (  HttpServletResponse   res  ,  UserProfile   user  )  { 
try  { 
Cookie   cookie  =  new   Cookie  (  AUTH_ATTR  ,  KeyFactory  .  keyToString  (  user  .  getKey  (  )  )  )  ; 
cookie  .  setMaxAge  (  COOKIE_MAX_AGE  )  ; 
res  .  addCookie  (  cookie  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
logger  .  i  (  "Unable to save cookies. Are cookie senabled?"  )  ; 
return   false  ; 
} 
} 







public   static   boolean   logout  (  HttpServletResponse   res  )  { 
try  { 
Cookie   nullcookie  =  new   Cookie  (  AUTH_ATTR  ,  ""  )  ; 
nullcookie  .  setMaxAge  (  0  )  ; 
res  .  addCookie  (  nullcookie  )  ; 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 







public   static   UserProfile   getAuthenticatedUser  (  HttpServletRequest   req  )  { 
Cookie   cookie  =  getAuthCookie  (  req  )  ; 
if  (  cookie  !=  null  )  { 
String   userProfileKey  =  getAuthCookie  (  req  )  .  getValue  (  )  ; 
if  (  userProfileKey  ==  null  )  { 
return   null  ; 
}  else  { 
Key   key  =  KeyFactory  .  stringToKey  (  userProfileKey  )  ; 
try  { 
return   PMF  .  getPersistenceManager  (  )  .  getObjectById  (  UserProfile  .  class  ,  key  )  ; 
}  catch  (  JDOException   e  )  { 
logger  .  d  (  "Unable to locate user with given key in the datastore"  ,  e  )  ; 
return   null  ; 
} 
} 
}  else  { 
return   null  ; 
} 
} 









public   static   UserProfile   signup  (  String   email  ,  String   password  )  throws   SignupException  { 
logger  .  i  (  "signing up new user: "  +  email  )  ; 
UserProfile   profile  =  null  ; 
try  { 
profile  =  UserProfile  .  storeUserProfile  (  email  ,  generatePasswordHash  (  password  )  )  ; 
if  (  profile  !=  null  )  { 
sendSignupEmail  (  profile  ,  password  )  ; 
} 
}  catch  (  UserProfileException   e  )  { 
logger  .  w  (  "Error signing up new user ["  +  email  +  "]:"  +  e  )  ; 
throw   new   SignupException  (  e  .  getMessage  (  )  )  ; 
} 
return   profile  ; 
} 








public   static   String   generatePasswordHash  (  String   password  )  { 
String   hash  =  null  ; 
password  =  HASH_SALT  +  password  ; 
try  { 
MessageDigest   sha  =  MessageDigest  .  getInstance  (  HASH_METHOD  )  ; 
byte  [  ]  hashedBytes  =  sha  .  digest  (  password  .  getBytes  (  )  )  ; 
hash  =  convertToHex  (  hashedBytes  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
logger  .  w  (  "Error generating password hash for ["  +  password  +  "]"  ,  e  )  ; 
} 
return   hash  ; 
} 







private   static   String   convertToHex  (  byte  [  ]  data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 










private   static   boolean   sendSignupEmail  (  UserProfile   user  ,  String   password  )  { 
try  { 
final   InternetAddress   noreplyEmail  =  new   InternetAddress  (  "panthro.rafael@gmail.com"  ,  "SleepSocial Admin"  )  ; 
Properties   props  =  new   Properties  (  )  ; 
Session   session  =  Session  .  getDefaultInstance  (  props  ,  null  )  ; 
try  { 
StringBuilder   message  =  new   StringBuilder  (  )  ; 
message  .  append  (  "Wellcome to SleepSocial. <br />"  )  ; 
message  .  append  (  "Please activate your account at:<br />"  )  ; 
message  .  append  (  "<a href='"  +  AppConfig  .  APP_URL  +  "/web/auth/activate'<br /><br />"  )  ; 
message  .  append  (  "Your user email:"  +  user  .  getEmail  (  )  +  "<br />"  )  ; 
message  .  append  (  "Your user password:"  +  password  +  "<br /><br />"  )  ; 
message  .  append  (  "Thanks for joining Sleep Social"  )  ; 
String   subject  =  "Wellcome to SleepSocial"  ; 
Message   msg  =  new   MimeMessage  (  session  )  ; 
msg  .  setFrom  (  noreplyEmail  )  ; 
msg  .  addRecipient  (  Message  .  RecipientType  .  TO  ,  new   InternetAddress  (  user  .  getEmail  (  )  )  )  ; 
msg  .  setSubject  (  subject  )  ; 
msg  .  setContent  (  message  .  toString  (  )  ,  "text/html"  )  ; 
Transport  .  send  (  msg  )  ; 
}  catch  (  AddressException   e  )  { 
Log  .  warn  (  "Signup Email was not sent, not a valid addres"  ,  e  )  ; 
throw   e  ; 
}  catch  (  MessagingException   e  )  { 
Log  .  warn  (  "Signup Email was not sent, not a valid message"  ,  e  )  ; 
throw   e  ; 
} 
return   true  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 
} 

