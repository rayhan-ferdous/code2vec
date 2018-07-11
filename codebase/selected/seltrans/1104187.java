package   com  .  progiweb  .  fbconnect  .  session  ; 

import   com  .  progiweb  .  fbconnect  .  FacebookConfig  ; 
import   atg  .  nucleus  .  GenericService  ; 
import   atg  .  nucleus  .  ServiceException  ; 
import   atg  .  servlet  .  DynamoHttpServletRequest  ; 
import   atg  .  servlet  .  DynamoHttpServletResponse  ; 
import   atg  .  userprofiling  .  Profile  ; 
import   atg  .  userprofiling  .  ProfileRequest  ; 
import   atg  .  userprofiling  .  ProfileRequestServlet  ; 
import   javax  .  servlet  .  http  .  Cookie  ; 
import   java  .  util  .  TreeSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 

public   class   FacebookConnector   extends   GenericService  { 

private   FacebookConfig   mFbConfig  ; 

private   String   mConnectContextPath  ; 

private   String   mProfilePath  ; 

private   String   mLogoutURL  ; 

private   String   mSiteHttpServerName  ; 

private   int   mSiteHttpServerPort  ; 

private   String   mApiKey  ,  mCookiePrefix  ,  mCookieUser  ,  mCookieLogout  ; 





public   void   doStartService  (  )  throws   ServiceException  { 
super  .  doStartService  (  )  ; 
mApiKey  =  mFbConfig  .  getApiKey  (  )  ; 
mCookiePrefix  =  mApiKey  +  '_'  ; 
mCookieUser  =  mCookiePrefix  +  FacebookConnectContext  .  USER_ID  ; 
mCookieLogout  =  mCookiePrefix  +  "logout"  ; 
} 







public   boolean   isLoggedInFacebook  (  DynamoHttpServletRequest   pRequest  ,  DynamoHttpServletResponse   pResponse  )  { 
String   ctx  =  "isLoggedInFacebook - "  ; 
Cookie  [  ]  cookies  =  pRequest  .  getCookies  (  )  ; 
if  (  cookies  ==  null  ||  cookies  .  length  ==  0  )  { 
return   false  ; 
} 
FacebookConnectContext   connectContext  =  (  FacebookConnectContext  )  pRequest  .  resolveName  (  getConnectContextPath  (  )  )  ; 
String   signature  =  ""  ; 
Cookie   logoutCookie  =  null  ; 
TreeSet   sortedFields  =  new   TreeSet  (  )  ; 
for  (  int   i  =  0  ;  i  <  cookies  .  length  ;  i  ++  )  { 
Cookie   cookie  =  cookies  [  i  ]  ; 
String   cookieName  =  cookie  .  getName  (  )  ; 
if  (  mApiKey  .  equals  (  cookieName  )  )  { 
signature  =  cookie  .  getValue  (  )  ; 
}  else   if  (  cookieName  .  startsWith  (  mApiKey  )  )  { 
String   field  =  cookieName  .  substring  (  mCookiePrefix  .  length  (  )  )  ; 
if  (  !  field  .  equals  (  mCookieLogout  )  )  { 
sortedFields  .  add  (  field  )  ; 
connectContext  .  setPropertyValue  (  field  ,  cookie  .  getValue  (  )  )  ; 
}  else  { 
logoutCookie  =  cookie  ; 
} 
} 
} 
if  (  signature  .  length  (  )  ==  0  ||  sortedFields  .  size  (  )  ==  0  ||  connectContext  .  getPropertyValue  (  FacebookConnectContext  .  USER_ID  )  ==  null  )  { 
return   false  ; 
} 
StringBuffer   base  =  new   StringBuffer  (  )  ; 
for  (  Iterator   it  =  sortedFields  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
String   sField  =  (  String  )  it  .  next  (  )  ; 
base  .  append  (  sField  )  .  append  (  '='  )  .  append  (  connectContext  .  getPropertyValue  (  sField  )  )  ; 
} 
base  .  append  (  mFbConfig  .  getSecretKey  (  )  )  ; 
try  { 
String   hash  =  md5  (  base  .  toString  (  )  )  ; 
if  (  hash  .  equals  (  signature  )  )  { 
if  (  logoutCookie  !=  null  )  { 
logoutCookie  .  setMaxAge  (  0  )  ; 
logoutCookie  .  setPath  (  "/"  )  ; 
pResponse  .  addCookie  (  logoutCookie  )  ; 
} 
Profile   profile  =  (  Profile  )  pRequest  .  resolveName  (  mProfilePath  )  ; 
profile  .  setPropertyValue  (  "facebookUserId"  ,  connectContext  .  getPropertyValue  (  FacebookConnectContext  .  USER_ID  )  )  ; 
return   true  ; 
} 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
if  (  isLoggingError  (  )  )  { 
logError  (  ctx  +  "could not make a MD5 digest"  ,  nsae  )  ; 
} 
} 
return   false  ; 
} 







public   String   getLogoutFacebookURL  (  DynamoHttpServletRequest   pRequest  ,  String   pLogoutSuccessURL  )  { 
String   ctx  =  "getLogoutFacebookURL - "  ; 
FacebookConnectContext   connectContext  =  (  FacebookConnectContext  )  pRequest  .  resolveName  (  getConnectContextPath  (  )  )  ; 
String   sessionKey  =  (  String  )  connectContext  .  getPropertyValue  (  FacebookConnectContext  .  SESSION_KEY  )  ; 
if  (  sessionKey  !=  null  &&  !  ""  .  equals  (  sessionKey  )  )  { 
String   absoluteSuccessURL  =  getAbsoluteURL  (  pRequest  ,  pLogoutSuccessURL  )  ; 
if  (  absoluteSuccessURL  .  indexOf  (  ProfileRequestServlet  .  LOGOUT_PARAM  )  ==  -  1  )  { 
absoluteSuccessURL  +=  "?"  +  ProfileRequestServlet  .  LOGOUT_PARAM  +  "=true"  ; 
} 
String   encodedLogoutSuccessURL  =  null  ; 
String   encodedSessionKey  =  null  ; 
try  { 
encodedLogoutSuccessURL  =  URLEncoder  .  encode  (  absoluteSuccessURL  ,  "UTF-8"  )  ; 
encodedSessionKey  =  URLEncoder  .  encode  (  sessionKey  ,  "UTF-8"  )  ; 
}  catch  (  UnsupportedEncodingException   uee  )  { 
if  (  isLoggingError  (  )  )  { 
logError  (  ctx  +  "could not encode URL"  ,  uee  )  ; 
} 
} 
StringBuffer   logoutFacebookURL  =  new   StringBuffer  (  mLogoutURL  )  ; 
logoutFacebookURL  .  append  (  "?app_key="  )  .  append  (  mFbConfig  .  getApiKey  (  )  )  ; 
logoutFacebookURL  .  append  (  "&session_key="  )  .  append  (  encodedSessionKey  )  ; 
logoutFacebookURL  .  append  (  "&next="  )  .  append  (  encodedLogoutSuccessURL  )  ; 
return   logoutFacebookURL  .  toString  (  )  ; 
} 
return   pLogoutSuccessURL  ; 
} 








private   String   getAbsoluteURL  (  DynamoHttpServletRequest   pRequest  ,  String   pOriginalURL  )  { 
String   ctx  =  "getAbsoluteURL - "  ; 
if  (  !  pOriginalURL  .  startsWith  (  "http://"  )  &&  !  pOriginalURL  .  startsWith  (  "https://"  )  )  { 
String   reqURL  =  pRequest  .  getRequestURL  (  )  .  toString  (  )  ; 
String   serverName  =  pRequest  .  getServerName  (  )  ; 
int   serverNamePos  =  reqURL  .  indexOf  (  serverName  )  ; 
String   serverPort  =  String  .  valueOf  (  pRequest  .  getServerPort  (  )  )  ; 
int   serverPortPos  =  reqURL  .  indexOf  (  serverPort  )  ; 
String   absoluteURL  =  reqURL  .  substring  (  0  ,  serverNamePos  )  +  getSiteHttpServerName  (  )  ; 
if  (  serverPortPos  !=  -  1  )  { 
absoluteURL  +=  ":"  +  getSiteHttpServerPort  (  )  +  reqURL  .  substring  (  serverPortPos  +  serverPort  .  length  (  )  )  ; 
}  else  { 
absoluteURL  +=  reqURL  .  substring  (  serverPortPos  )  ; 
} 
if  (  isLoggingDebug  (  )  )  { 
logDebug  (  ctx  +  "original URL = "  +  pOriginalURL  +  "  - absolute URL = "  +  absoluteURL  )  ; 
} 
return   absoluteURL  ; 
} 
return   pOriginalURL  ; 
} 






public   String   getUserId  (  DynamoHttpServletRequest   pRequest  )  { 
return   pRequest  .  getCookieParameter  (  mCookieUser  )  ; 
} 

public   String   getConnectContextPath  (  )  { 
return   mConnectContextPath  ; 
} 

public   void   setConnectContextPath  (  String   pConnectContextPath  )  { 
mConnectContextPath  =  pConnectContextPath  ; 
} 

public   String   getProfilePath  (  )  { 
return   mProfilePath  ; 
} 

public   void   setProfilePath  (  String   pProfilePath  )  { 
mProfilePath  =  pProfilePath  ; 
} 

public   String   getLogoutURL  (  )  { 
return   mLogoutURL  ; 
} 

public   void   setLogoutURL  (  String   pLogoutURL  )  { 
mLogoutURL  =  pLogoutURL  ; 
} 

public   FacebookConfig   getFbConfig  (  )  { 
return   mFbConfig  ; 
} 

public   void   setFbConfig  (  FacebookConfig   pFbConfig  )  { 
mFbConfig  =  pFbConfig  ; 
} 

public   String   getSiteHttpServerName  (  )  { 
return   mSiteHttpServerName  ; 
} 

public   void   setSiteHttpServerName  (  String   pSiteHttpServerName  )  { 
mSiteHttpServerName  =  pSiteHttpServerName  ; 
} 

public   int   getSiteHttpServerPort  (  )  { 
return   mSiteHttpServerPort  ; 
} 

public   void   setSiteHttpServerPort  (  int   pSiteHttpServerPort  )  { 
mSiteHttpServerPort  =  pSiteHttpServerPort  ; 
} 







private   static   String   md5  (  String   base  )  throws   NoSuchAlgorithmException  { 
MessageDigest   hash  ; 
hash  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte  [  ]  data  =  hash  .  digest  (  base  .  getBytes  (  )  )  ; 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  { 
buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ; 
}  else  { 
buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
} 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 
} 

