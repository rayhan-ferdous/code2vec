package   br  .  ufmg  .  catustec  .  arangiSecurity  .  jaas  ; 

import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  Principal  ; 
import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  List  ; 
import   javax  .  naming  .  AuthenticationException  ; 
import   javax  .  naming  .  CommunicationException  ; 
import   javax  .  naming  .  Context  ; 
import   javax  .  naming  .  InvalidNameException  ; 
import   javax  .  naming  .  Name  ; 
import   javax  .  naming  .  NameNotFoundException  ; 
import   javax  .  naming  .  NameParser  ; 
import   javax  .  naming  .  NamingEnumeration  ; 
import   javax  .  naming  .  NamingException  ; 
import   javax  .  naming  .  directory  .  Attribute  ; 
import   javax  .  naming  .  directory  .  Attributes  ; 
import   javax  .  naming  .  directory  .  DirContext  ; 
import   javax  .  naming  .  directory  .  InitialDirContext  ; 
import   javax  .  naming  .  directory  .  SearchControls  ; 
import   javax  .  naming  .  directory  .  SearchResult  ; 
import   br  .  ufmg  .  catustec  .  arangi  .  auth  .  Base64  ; 
import   br  .  ufmg  .  catustec  .  arangi  .  auth  .  HexUtils  ; 











































































































public   class   JNDIRealm  { 




protected   MessageDigest   md  =  null  ; 

protected   String   digest  =  null  ; 

private   boolean   debug  =  false  ; 




protected   String   authentication  =  null  ; 




protected   String   connectionName  =  null  ; 




protected   String   connectionPassword  =  null  ; 




protected   String   connectionURL  =  null  ; 




protected   DirContext   context  =  null  ; 






protected   String   contextFactory  =  "com.sun.jndi.ldap.LdapCtxFactory"  ; 




protected   static   final   String   info  =  "org.apache.catalina.realm.JNDIRealm/1.0"  ; 




protected   static   final   String   name  =  "JNDIRealm"  ; 





protected   String   protocol  =  null  ; 






protected   String   referrals  =  null  ; 




protected   String   userBase  =  ""  ; 





protected   String   userSearch  =  null  ; 





protected   MessageFormat   userSearchFormat  =  null  ; 




protected   boolean   userSubtree  =  false  ; 




protected   String   userPassword  =  null  ; 









protected   String  [  ]  userPatternArray  =  null  ; 






protected   String   userPattern  =  null  ; 





protected   MessageFormat  [  ]  userPatternFormatArray  =  null  ; 




protected   String   roleBase  =  ""  ; 





protected   MessageFormat   roleFormat  =  null  ; 





protected   String   userRoleName  =  null  ; 




protected   String   roleName  =  null  ; 





protected   String   roleSearch  =  null  ; 




protected   boolean   roleSubtree  =  false  ; 




protected   String   alternateURL  ; 





protected   int   connectionAttempt  =  0  ; 




protected   int   curUserPattern  =  0  ; 




public   boolean   isDebug  (  )  { 
return   debug  ; 
} 




public   void   setDebug  (  boolean   debug  )  { 
this  .  debug  =  debug  ; 
} 




public   String   getAuthentication  (  )  { 
return   authentication  ; 
} 






public   void   setAuthentication  (  String   authentication  )  { 
this  .  authentication  =  authentication  ; 
} 




public   String   getConnectionName  (  )  { 
return  (  this  .  connectionName  )  ; 
} 






public   void   setConnectionName  (  String   connectionName  )  { 
this  .  connectionName  =  connectionName  ; 
} 




public   String   getConnectionPassword  (  )  { 
return  (  this  .  connectionPassword  )  ; 
} 






public   void   setConnectionPassword  (  String   connectionPassword  )  { 
this  .  connectionPassword  =  connectionPassword  ; 
} 




public   String   getConnectionURL  (  )  { 
return  (  this  .  connectionURL  )  ; 
} 






public   void   setConnectionURL  (  String   connectionURL  )  { 
this  .  connectionURL  =  connectionURL  ; 
} 




public   String   getContextFactory  (  )  { 
return  (  this  .  contextFactory  )  ; 
} 






public   void   setContextFactory  (  String   contextFactory  )  { 
this  .  contextFactory  =  contextFactory  ; 
} 




public   String   getProtocol  (  )  { 
return   protocol  ; 
} 






public   void   setProtocol  (  String   protocol  )  { 
this  .  protocol  =  protocol  ; 
} 




public   String   getReferrals  (  )  { 
return   referrals  ; 
} 





public   void   setReferrals  (  String   referrals  )  { 
this  .  referrals  =  referrals  ; 
} 




public   String   getUserBase  (  )  { 
return  (  this  .  userBase  )  ; 
} 






public   void   setUserBase  (  String   userBase  )  { 
this  .  userBase  =  userBase  ; 
} 




public   String   getUserSearch  (  )  { 
return  (  this  .  userSearch  )  ; 
} 






public   void   setUserSearch  (  String   userSearch  )  { 
this  .  userSearch  =  userSearch  ; 
if  (  userSearch  ==  null  )  userSearchFormat  =  null  ;  else   userSearchFormat  =  new   MessageFormat  (  userSearch  )  ; 
} 




public   boolean   getUserSubtree  (  )  { 
return  (  this  .  userSubtree  )  ; 
} 






public   void   setUserSubtree  (  boolean   userSubtree  )  { 
this  .  userSubtree  =  userSubtree  ; 
} 




public   String   getUserRoleName  (  )  { 
return   userRoleName  ; 
} 






public   void   setUserRoleName  (  String   userRoleName  )  { 
this  .  userRoleName  =  userRoleName  ; 
} 




public   String   getRoleBase  (  )  { 
return  (  this  .  roleBase  )  ; 
} 






public   void   setRoleBase  (  String   roleBase  )  { 
this  .  roleBase  =  roleBase  ; 
} 




public   String   getRoleName  (  )  { 
return  (  this  .  roleName  )  ; 
} 






public   void   setRoleName  (  String   roleName  )  { 
this  .  roleName  =  roleName  ; 
} 




public   String   getRoleSearch  (  )  { 
return  (  this  .  roleSearch  )  ; 
} 






public   void   setRoleSearch  (  String   roleSearch  )  { 
this  .  roleSearch  =  roleSearch  ; 
if  (  roleSearch  ==  null  )  roleFormat  =  null  ;  else   roleFormat  =  new   MessageFormat  (  roleSearch  )  ; 
} 




public   boolean   getRoleSubtree  (  )  { 
return  (  this  .  roleSubtree  )  ; 
} 






public   void   setRoleSubtree  (  boolean   roleSubtree  )  { 
this  .  roleSubtree  =  roleSubtree  ; 
} 




public   String   getUserPassword  (  )  { 
return  (  this  .  userPassword  )  ; 
} 






public   void   setUserPassword  (  String   userPassword  )  { 
this  .  userPassword  =  userPassword  ; 
} 




public   String   getUserPattern  (  )  { 
return  (  this  .  userPattern  )  ; 
} 











public   void   setUserPattern  (  String   userPattern  )  { 
this  .  userPattern  =  userPattern  ; 
if  (  userPattern  ==  null  )  userPatternArray  =  null  ;  else  { 
userPatternArray  =  parseUserPatternString  (  userPattern  )  ; 
int   len  =  this  .  userPatternArray  .  length  ; 
userPatternFormatArray  =  new   MessageFormat  [  len  ]  ; 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
userPatternFormatArray  [  i  ]  =  new   MessageFormat  (  userPatternArray  [  i  ]  )  ; 
} 
} 
} 






public   String   getAlternateURL  (  )  { 
return   this  .  alternateURL  ; 
} 






public   void   setAlternateURL  (  String   alternateURL  )  { 
this  .  alternateURL  =  alternateURL  ; 
} 














public   Principal   authenticate  (  String   username  ,  String   credentials  )  { 
DirContext   context  =  null  ; 
Principal   principal  =  null  ; 
try  { 
context  =  open  (  )  ; 
try  { 
principal  =  authenticate  (  context  ,  username  ,  credentials  )  ; 
}  catch  (  CommunicationException   e  )  { 
if  (  e  .  getMessage  (  )  !=  null  &&  e  .  getMessage  (  )  .  indexOf  (  "closed"  )  <  0  )  throw  (  e  )  ; 
if  (  context  !=  null  )  close  (  context  )  ; 
context  =  open  (  )  ; 
principal  =  authenticate  (  context  ,  username  ,  credentials  )  ; 
} 
release  (  context  )  ; 
return  (  principal  )  ; 
}  catch  (  NamingException   e  )  { 
if  (  context  !=  null  )  close  (  context  )  ; 
return  (  null  )  ; 
} 
} 












public   synchronized   Principal   authenticate  (  DirContext   context  ,  String   username  ,  String   credentials  )  throws   NamingException  { 
if  (  username  ==  null  ||  username  .  equals  (  ""  )  ||  credentials  ==  null  ||  credentials  .  equals  (  ""  )  )  return  (  null  )  ; 
if  (  userPatternArray  !=  null  )  { 
for  (  curUserPattern  =  0  ;  curUserPattern  <  userPatternFormatArray  .  length  ;  curUserPattern  ++  )  { 
User   user  =  getUser  (  context  ,  username  )  ; 
if  (  user  !=  null  )  { 
try  { 
if  (  checkCredentials  (  context  ,  user  ,  credentials  )  )  { 
List   roles  =  getRoles  (  context  ,  user  )  ; 
GenericPrincipal   gnPrincipal  =  new   JndiPrincipal  (  username  ,  credentials  ,  roles  )  ; 
return   gnPrincipal  ; 
} 
}  catch  (  InvalidNameException   ine  )  { 
} 
} 
} 
return   null  ; 
}  else  { 
User   user  =  getUser  (  context  ,  username  )  ; 
if  (  user  ==  null  )  return  (  null  )  ; 
if  (  !  checkCredentials  (  context  ,  user  ,  credentials  )  )  return  (  null  )  ; 
List   roles  =  getRoles  (  context  ,  user  )  ; 
return  (  new   JndiPrincipal  (  username  ,  credentials  ,  roles  )  )  ; 
} 
} 

















protected   User   getUser  (  DirContext   context  ,  String   username  )  throws   NamingException  { 
User   user  =  null  ; 
ArrayList   list  =  new   ArrayList  (  )  ; 
if  (  userPassword  !=  null  )  list  .  add  (  userPassword  )  ; 
if  (  userRoleName  !=  null  )  list  .  add  (  userRoleName  )  ; 
String  [  ]  attrIds  =  new   String  [  list  .  size  (  )  ]  ; 
list  .  toArray  (  attrIds  )  ; 
if  (  userPatternFormatArray  !=  null  )  { 
user  =  getUserByPattern  (  context  ,  username  ,  attrIds  )  ; 
}  else  { 
user  =  getUserBySearch  (  context  ,  username  ,  attrIds  )  ; 
} 
return   user  ; 
} 














protected   User   getUserByPattern  (  DirContext   context  ,  String   username  ,  String  [  ]  attrIds  )  throws   NamingException  { 
if  (  debug  )  { 
System  .  out  .  println  (  "lookupUser("  +  username  +  ")"  )  ; 
} 
if  (  username  ==  null  ||  userPatternFormatArray  [  curUserPattern  ]  ==  null  )  return  (  null  )  ; 
String   dn  =  userPatternFormatArray  [  curUserPattern  ]  .  format  (  new   String  [  ]  {  username  }  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "  dn="  +  dn  )  ; 
} 
if  (  attrIds  ==  null  ||  attrIds  .  length  ==  0  )  return   new   User  (  username  ,  dn  ,  null  ,  null  )  ; 
Attributes   attrs  =  null  ; 
try  { 
attrs  =  context  .  getAttributes  (  dn  ,  attrIds  )  ; 
}  catch  (  NameNotFoundException   e  )  { 
return  (  null  )  ; 
} 
if  (  attrs  ==  null  )  return  (  null  )  ; 
String   password  =  null  ; 
if  (  userPassword  !=  null  )  password  =  getAttributeValue  (  userPassword  ,  attrs  )  ; 
ArrayList   roles  =  null  ; 
if  (  userRoleName  !=  null  )  roles  =  addAttributeValues  (  userRoleName  ,  attrs  ,  roles  )  ; 
return   new   User  (  username  ,  dn  ,  password  ,  roles  )  ; 
} 












protected   User   getUserBySearch  (  DirContext   context  ,  String   username  ,  String  [  ]  attrIds  )  throws   NamingException  { 
if  (  username  ==  null  ||  userSearchFormat  ==  null  )  return  (  null  )  ; 
String   filter  =  userSearchFormat  .  format  (  new   String  [  ]  {  username  }  )  ; 
SearchControls   constraints  =  new   SearchControls  (  )  ; 
if  (  userSubtree  )  { 
constraints  .  setSearchScope  (  SearchControls  .  SUBTREE_SCOPE  )  ; 
}  else  { 
constraints  .  setSearchScope  (  SearchControls  .  ONELEVEL_SCOPE  )  ; 
} 
if  (  attrIds  ==  null  )  attrIds  =  new   String  [  0  ]  ; 
constraints  .  setReturningAttributes  (  attrIds  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "  Searching for "  +  username  )  ; 
System  .  out  .  println  (  "  base: "  +  userBase  +  "  filter: "  +  filter  )  ; 
} 
NamingEnumeration   results  =  context  .  search  (  userBase  ,  filter  ,  constraints  )  ; 
if  (  results  ==  null  ||  !  results  .  hasMore  (  )  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  "  username not found"  )  ; 
} 
return  (  null  )  ; 
} 
SearchResult   result  =  (  SearchResult  )  results  .  next  (  )  ; 
if  (  results  .  hasMore  (  )  )  { 
return  (  null  )  ; 
} 
NameParser   parser  =  context  .  getNameParser  (  ""  )  ; 
Name   contextName  =  parser  .  parse  (  context  .  getNameInNamespace  (  )  )  ; 
Name   baseName  =  parser  .  parse  (  userBase  )  ; 
Name   entryName  =  parser  .  parse  (  result  .  getName  (  )  )  ; 
Name   name  =  contextName  .  addAll  (  baseName  )  ; 
name  =  name  .  addAll  (  entryName  )  ; 
String   dn  =  name  .  toString  (  )  ; 
Attributes   attrs  =  result  .  getAttributes  (  )  ; 
if  (  attrs  ==  null  )  return   null  ; 
String   password  =  null  ; 
if  (  userPassword  !=  null  )  password  =  getAttributeValue  (  userPassword  ,  attrs  )  ; 
ArrayList   roles  =  null  ; 
if  (  userRoleName  !=  null  )  roles  =  addAttributeValues  (  userRoleName  ,  attrs  ,  roles  )  ; 
return   new   User  (  username  ,  dn  ,  password  ,  roles  )  ; 
} 
















protected   boolean   checkCredentials  (  DirContext   context  ,  User   user  ,  String   credentials  )  throws   NamingException  { 
boolean   validated  =  false  ; 
if  (  userPassword  ==  null  )  { 
validated  =  bindAsUser  (  context  ,  user  ,  credentials  )  ; 
}  else  { 
validated  =  compareCredentials  (  context  ,  user  ,  credentials  )  ; 
} 
return  (  validated  )  ; 
} 











protected   boolean   compareCredentials  (  DirContext   context  ,  User   info  ,  String   credentials  )  throws   NamingException  { 
if  (  info  ==  null  ||  credentials  ==  null  )  return  (  false  )  ; 
String   password  =  info  .  password  ; 
if  (  password  ==  null  )  return  (  false  )  ; 
boolean   validated  =  false  ; 
if  (  hasMessageDigest  (  )  )  { 
if  (  password  .  startsWith  (  "{SHA}"  )  )  { 
synchronized  (  this  )  { 
password  =  password  .  substring  (  5  )  ; 
md  .  reset  (  )  ; 
md  .  update  (  credentials  .  getBytes  (  )  )  ; 
String   digestedPassword  =  new   String  (  Base64  .  encode  (  md  .  digest  (  )  )  )  ; 
validated  =  password  .  equals  (  digestedPassword  )  ; 
} 
}  else  { 
validated  =  (  digest  (  credentials  )  .  equalsIgnoreCase  (  password  )  )  ; 
} 
}  else   validated  =  (  digest  (  credentials  )  .  equals  (  password  )  )  ; 
return  (  validated  )  ; 
} 

protected   String   digest  (  String   credentials  )  { 
if  (  hasMessageDigest  (  )  ==  false  )  return  (  credentials  )  ; 
synchronized  (  this  )  { 
try  { 
md  .  reset  (  )  ; 
md  .  update  (  credentials  .  getBytes  (  )  )  ; 
return  (  HexUtils  .  convert  (  md  .  digest  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
return  (  credentials  )  ; 
} 
} 
} 










protected   boolean   bindAsUser  (  DirContext   context  ,  User   user  ,  String   credentials  )  throws   NamingException  { 
Attributes   attr  ; 
if  (  credentials  ==  null  ||  user  ==  null  )  return  (  false  )  ; 
String   dn  =  user  .  dn  ; 
if  (  dn  ==  null  )  return  (  false  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "  validating credentials by binding as the user"  )  ; 
} 
context  .  addToEnvironment  (  Context  .  SECURITY_PRINCIPAL  ,  dn  )  ; 
context  .  addToEnvironment  (  Context  .  SECURITY_CREDENTIALS  ,  credentials  )  ; 
boolean   validated  =  false  ; 
try  { 
if  (  debug  )  { 
System  .  out  .  println  (  "  binding as "  +  dn  )  ; 
} 
attr  =  context  .  getAttributes  (  ""  ,  null  )  ; 
validated  =  true  ; 
}  catch  (  AuthenticationException   e  )  { 
if  (  debug  )  { 
System  .  out  .  println  (  "  bind attempt failed"  )  ; 
} 
} 
if  (  connectionName  !=  null  )  { 
context  .  addToEnvironment  (  Context  .  SECURITY_PRINCIPAL  ,  connectionName  )  ; 
}  else  { 
context  .  removeFromEnvironment  (  Context  .  SECURITY_PRINCIPAL  )  ; 
} 
if  (  connectionPassword  !=  null  )  { 
context  .  addToEnvironment  (  Context  .  SECURITY_CREDENTIALS  ,  connectionPassword  )  ; 
}  else  { 
context  .  removeFromEnvironment  (  Context  .  SECURITY_CREDENTIALS  )  ; 
} 
return  (  validated  )  ; 
} 












protected   List   getRoles  (  DirContext   context  ,  User   user  )  throws   NamingException  { 
if  (  user  ==  null  )  return  (  null  )  ; 
String   dn  =  user  .  dn  ; 
String   username  =  user  .  username  ; 
if  (  dn  ==  null  ||  username  ==  null  )  return  (  null  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "  getRoles("  +  dn  +  ")"  )  ; 
} 
ArrayList   list  =  user  .  roles  ; 
if  (  list  ==  null  )  { 
list  =  new   ArrayList  (  )  ; 
} 
if  (  (  roleFormat  ==  null  )  ||  (  roleName  ==  null  )  )  return  (  list  )  ; 
String   filter  =  roleFormat  .  format  (  new   String  [  ]  {  doRFC2254Encoding  (  dn  )  ,  username  }  )  ; 
SearchControls   controls  =  new   SearchControls  (  )  ; 
if  (  roleSubtree  )  { 
controls  .  setSearchScope  (  SearchControls  .  SUBTREE_SCOPE  )  ; 
}  else  { 
controls  .  setSearchScope  (  SearchControls  .  ONELEVEL_SCOPE  )  ; 
} 
controls  .  setReturningAttributes  (  new   String  [  ]  {  roleName  }  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  "  Searching role base '"  +  roleBase  +  "' for attribute '"  +  roleName  +  "'"  )  ; 
System  .  out  .  println  (  "  With filter expression '"  +  filter  +  "'"  )  ; 
} 
NamingEnumeration   results  =  context  .  search  (  roleBase  ,  filter  ,  controls  )  ; 
if  (  debug  )  { 
System  .  out  .  println  (  results  )  ; 
} 
if  (  results  ==  null  )  { 
return  (  list  )  ; 
} 
while  (  results  .  hasMore  (  )  )  { 
SearchResult   result  =  (  SearchResult  )  results  .  next  (  )  ; 
Attributes   attrs  =  result  .  getAttributes  (  )  ; 
if  (  attrs  ==  null  )  continue  ; 
list  =  addAttributeValues  (  roleName  ,  attrs  ,  list  )  ; 
} 
if  (  debug  )  { 
if  (  list  !=  null  )  { 
System  .  out  .  println  (  "  Returning "  +  list  .  size  (  )  +  " roles"  )  ; 
for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  System  .  out  .  println  (  "  Found role "  +  list  .  get  (  i  )  )  ; 
}  else  { 
System  .  out  .  println  (  "  getRoles about to return null "  )  ; 
} 
} 
return  (  list  )  ; 
} 









private   String   getAttributeValue  (  String   attrId  ,  Attributes   attrs  )  throws   NamingException  { 
if  (  debug  )  { 
System  .  out  .  println  (  "  retrieving attribute "  +  attrId  )  ; 
} 
if  (  attrId  ==  null  ||  attrs  ==  null  )  return   null  ; 
Attribute   attr  =  attrs  .  get  (  attrId  )  ; 
if  (  attr  ==  null  )  { 
return  (  null  )  ; 
} 
Object   value  =  attr  .  get  (  )  ; 
if  (  value  ==  null  )  { 
return  (  null  )  ; 
} 
String   valueString  =  null  ; 
if  (  value   instanceof   byte  [  ]  )  { 
valueString  =  new   String  (  (  byte  [  ]  )  value  )  ; 
}  else  { 
valueString  =  value  .  toString  (  )  ; 
} 
return   valueString  ; 
} 










private   ArrayList   addAttributeValues  (  String   attrId  ,  Attributes   attrs  ,  ArrayList   values  )  throws   NamingException  { 
if  (  debug  )  { 
System  .  out  .  println  (  "retrieving values for attribute "  +  attrId  )  ; 
} 
if  (  attrId  ==  null  ||  attrs  ==  null  )  { 
return   values  ; 
} 
if  (  values  ==  null  )  { 
values  =  new   ArrayList  (  )  ; 
} 
Attribute   attr  =  attrs  .  get  (  attrId  )  ; 
if  (  attr  ==  null  )  { 
return  (  values  )  ; 
} 
NamingEnumeration   e  =  attr  .  getAll  (  )  ; 
while  (  e  .  hasMore  (  )  )  { 
String   value  =  (  String  )  e  .  next  (  )  ; 
values  .  add  (  value  )  ; 
} 
return   values  ; 
} 






protected   void   close  (  DirContext   context  )  { 
if  (  context  ==  null  )  return  ; 
try  { 
if  (  debug  )  { 
System  .  out  .  println  (  "Closing directory context"  )  ; 
} 
context  .  close  (  )  ; 
}  catch  (  NamingException   e  )  { 
System  .  out  .  println  (  "jndiRealm.close: "  +  e  .  getClass  (  )  .  getName  (  )  +  ": "  +  e  .  getMessage  (  )  )  ; 
} 
this  .  context  =  null  ; 
} 




protected   String   getName  (  )  { 
return  (  name  )  ; 
} 




protected   String   getPassword  (  String   username  )  { 
return  (  null  )  ; 
} 




protected   Principal   getPrincipal  (  String   username  )  { 
return  (  null  )  ; 
} 







protected   DirContext   open  (  )  throws   NamingException  { 
if  (  context  !=  null  )  return  (  context  )  ; 
try  { 
context  =  new   InitialDirContext  (  getDirectoryContextEnvironment  (  )  )  ; 
}  catch  (  NamingException   e  )  { 
connectionAttempt  =  1  ; 
System  .  out  .  println  (  "jndiRealm.exception"  )  ; 
e  .  printStackTrace  (  )  ; 
context  =  new   InitialDirContext  (  getDirectoryContextEnvironment  (  )  )  ; 
}  finally  { 
connectionAttempt  =  0  ; 
} 
return  (  context  )  ; 
} 






protected   Hashtable   getDirectoryContextEnvironment  (  )  { 
Hashtable   env  =  new   Hashtable  (  )  ; 
if  (  debug  &&  connectionAttempt  ==  0  )  { 
System  .  out  .  println  (  "Connecting to URL "  +  connectionURL  )  ; 
}  else   if  (  debug  &&  connectionAttempt  >  0  )  { 
System  .  out  .  println  (  "Connecting to URL "  +  alternateURL  )  ; 
} 
env  .  put  (  Context  .  INITIAL_CONTEXT_FACTORY  ,  contextFactory  )  ; 
if  (  connectionName  !=  null  )  env  .  put  (  Context  .  SECURITY_PRINCIPAL  ,  connectionName  )  ; 
if  (  connectionPassword  !=  null  )  env  .  put  (  Context  .  SECURITY_CREDENTIALS  ,  connectionPassword  )  ; 
if  (  connectionURL  !=  null  &&  connectionAttempt  ==  0  )  env  .  put  (  Context  .  PROVIDER_URL  ,  connectionURL  )  ;  else   if  (  alternateURL  !=  null  &&  connectionAttempt  >  0  )  env  .  put  (  Context  .  PROVIDER_URL  ,  alternateURL  )  ; 
if  (  authentication  !=  null  )  env  .  put  (  Context  .  SECURITY_AUTHENTICATION  ,  authentication  )  ; 
if  (  protocol  !=  null  )  env  .  put  (  Context  .  SECURITY_PROTOCOL  ,  protocol  )  ; 
if  (  referrals  !=  null  )  env  .  put  (  Context  .  REFERRAL  ,  referrals  )  ; 
return   env  ; 
} 






protected   void   release  (  DirContext   context  )  { 
; 
} 










protected   String  [  ]  parseUserPatternString  (  String   userPatternString  )  { 
if  (  userPatternString  !=  null  )  { 
ArrayList   pathList  =  new   ArrayList  (  )  ; 
int   startParenLoc  =  userPatternString  .  indexOf  (  '('  )  ; 
if  (  startParenLoc  ==  -  1  )  { 
return   new   String  [  ]  {  userPatternString  }  ; 
} 
int   startingPoint  =  0  ; 
while  (  startParenLoc  >  -  1  )  { 
int   endParenLoc  =  0  ; 
while  (  (  userPatternString  .  charAt  (  startParenLoc  +  1  )  ==  '|'  )  ||  (  startParenLoc  !=  0  &&  userPatternString  .  charAt  (  startParenLoc  -  1  )  ==  '\\'  )  )  { 
startParenLoc  =  userPatternString  .  indexOf  (  "("  ,  startParenLoc  +  1  )  ; 
} 
endParenLoc  =  userPatternString  .  indexOf  (  ")"  ,  startParenLoc  +  1  )  ; 
while  (  userPatternString  .  charAt  (  endParenLoc  -  1  )  ==  '\\'  )  { 
endParenLoc  =  userPatternString  .  indexOf  (  ")"  ,  endParenLoc  +  1  )  ; 
} 
String   nextPathPart  =  userPatternString  .  substring  (  startParenLoc  +  1  ,  endParenLoc  )  ; 
pathList  .  add  (  nextPathPart  )  ; 
startingPoint  =  endParenLoc  +  1  ; 
startParenLoc  =  userPatternString  .  indexOf  (  '('  ,  startingPoint  )  ; 
} 
return  (  String  [  ]  )  pathList  .  toArray  (  new   String  [  ]  {  }  )  ; 
} 
return   null  ; 
} 















protected   String   doRFC2254Encoding  (  String   inString  )  { 
StringBuffer   buf  =  new   StringBuffer  (  inString  .  length  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  inString  .  length  (  )  ;  i  ++  )  { 
char   c  =  inString  .  charAt  (  i  )  ; 
switch  (  c  )  { 
case  '\\'  : 
buf  .  append  (  "\\5c"  )  ; 
break  ; 
case  '*'  : 
buf  .  append  (  "\\2a"  )  ; 
break  ; 
case  '('  : 
buf  .  append  (  "\\28"  )  ; 
break  ; 
case  ')'  : 
buf  .  append  (  "\\29"  )  ; 
break  ; 
case  '\0'  : 
buf  .  append  (  "\\00"  )  ; 
break  ; 
default  : 
buf  .  append  (  c  )  ; 
break  ; 
} 
} 
return   buf  .  toString  (  )  ; 
} 

protected   boolean   hasMessageDigest  (  )  { 
return  !  (  md  ==  null  )  ; 
} 
} 




class   User  { 

String   username  =  null  ; 

String   dn  =  null  ; 

String   password  =  null  ; 

ArrayList   roles  =  null  ; 

User  (  String   username  ,  String   dn  ,  String   password  ,  ArrayList   roles  )  { 
this  .  username  =  username  ; 
this  .  dn  =  dn  ; 
this  .  password  =  password  ; 
this  .  roles  =  roles  ; 
} 
} 

