package   eu  .  planets_project  .  pp  .  plato  .  application  ; 

import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  List  ; 
import   javax  .  ejb  .  Remove  ; 
import   javax  .  ejb  .  Stateful  ; 
import   javax  .  faces  .  application  .  FacesMessage  ; 
import   javax  .  faces  .  context  .  FacesContext  ; 
import   javax  .  persistence  .  EntityManager  ; 
import   javax  .  persistence  .  PersistenceContext  ; 
import   javax  .  persistence  .  Query  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  dom4j  .  Document  ; 
import   org  .  dom4j  .  io  .  XMLWriter  ; 
import   org  .  jboss  .  annotation  .  ejb  .  cache  .  Cache  ; 
import   org  .  jboss  .  seam  .  ScopeType  ; 
import   org  .  jboss  .  seam  .  annotations  .  Destroy  ; 
import   org  .  jboss  .  seam  .  annotations  .  In  ; 
import   org  .  jboss  .  seam  .  annotations  .  Name  ; 
import   org  .  jboss  .  seam  .  annotations  .  Out  ; 
import   org  .  jboss  .  seam  .  annotations  .  Scope  ; 
import   org  .  jboss  .  seam  .  faces  .  FacesMessages  ; 
import   org  .  junit  .  runner  .  Computer  ; 
import   eu  .  planets_project  .  pp  .  plato  .  action  .  IProjectCleaner  ; 
import   eu  .  planets_project  .  pp  .  plato  .  action  .  IProjectExport  ; 
import   eu  .  planets_project  .  pp  .  plato  .  action  .  interfaces  .  IAdmin  ; 
import   eu  .  planets_project  .  pp  .  plato  .  action  .  interfaces  .  IMessages  ; 
import   eu  .  planets_project  .  pp  .  plato  .  action  .  project  .  LoadPlanAction  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  Alternative  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  Plan  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  PlanProperties  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  Values  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  tree  .  Leaf  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  tree  .  TemplateTree  ; 
import   eu  .  planets_project  .  pp  .  plato  .  model  .  values  .  Value  ; 
import   eu  .  planets_project  .  pp  .  plato  .  util  .  MemoryTest  ; 
import   eu  .  planets_project  .  pp  .  plato  .  util  .  PlatoLogger  ; 
import   eu  .  planets_project  .  pp  .  plato  .  xml  .  ProjectExporter  ; 







@  Stateful 
@  Scope  (  ScopeType  .  APPLICATION  ) 
@  Name  (  "admin"  ) 
@  Cache  (  org  .  jboss  .  ejb3  .  cache  .  NoPassivationCache  .  class  ) 
public   class   AdminAction   implements   IAdmin  ,  Serializable  { 




private   static   final   long   serialVersionUID  =  8226431137018309922L  ; 

private   static   final   Log   log  =  PlatoLogger  .  getLogger  (  AdminAction  .  class  )  ; 

@  PersistenceContext 
EntityManager   em  ; 




@  In  (  required  =  false  ,  create  =  true  ) 
@  Out 
IMessages   allmessages  ; 

@  In  (  create  =  true  ) 
private   LoadPlanAction   loadPlan  ; 




@  In  (  required  =  false  ) 
@  Out 
private   String   news  =  ""  ; 




@  In  (  required  =  false  ) 
@  Out 
private   String   author  =  ""  ; 




private   String   importance  =  "Info"  ; 




@  Out 
boolean   showNews  =  true  ; 

@  Out  (  required  =  false  ) 
private   String   exportDir  =  ""  ; 

private   MemoryTest   memTest  =  new   MemoryTest  (  )  ; 

private   ProjectExporter   exporter  =  new   ProjectExporter  (  )  ; 

public   void   throwException  (  )  { 
throw   new   RuntimeException  (  "Test Exception"  )  ; 
} 




@  In  (  required  =  false  ) 
String   password  ; 




private   String   code  =  "d1f686a6914ac3925ba26732abc96d8878465746"  ; 






public   boolean   check  (  )  { 
log  .  info  (  computeSHA  (  password  )  )  ; 
boolean   allow  =  (  password  !=  null  &&  (  computeSHA  (  password  )  .  equals  (  code  )  )  )  ; 
if  (  !  allow  )  { 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Sorry, wrong passcode."  )  ; 
} 
return   allow  ; 
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

private   static   String   computeSHA  (  String   input  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "SHA-1"  )  ; 
md  .  update  (  input  .  getBytes  (  "UTF-8"  )  )  ; 
byte  [  ]  code  =  md  .  digest  (  )  ; 
return   convertToHex  (  code  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
log  .  error  (  "Algorithm SHA-1 not found!"  ,  e  )  ; 
e  .  printStackTrace  (  )  ; 
return   null  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
log  .  error  (  "Encoding problem: UTF-8 not supported!"  ,  e  )  ; 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 






public   String   unlockAll  (  )  { 
if  (  check  (  )  )  { 
loadPlan  .  unlockAll  (  )  ; 
} 
return   null  ; 
} 





public   String   unlockUsingProjectID  (  )  { 
Query   q  =  em  .  createQuery  (  "update PlanProperties pp set pp.openHandle = 0 where pp.id = "  +  projectID  )  ; 
if  (  q  .  executeUpdate  (  )  <  1  )  { 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_ERROR  ,  "Unlocking project "  +  projectID  +  " failed."  )  ; 
log  .  info  (  "Unlocking project "  +  projectID  +  " failed."  )  ; 
}  else   FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_ERROR  ,  "Unlocked project "  +  this  .  projectID  )  ; 
log  .  info  (  "Unlocked project "  +  projectID  )  ; 
return   null  ; 
} 





public   void   addNews  (  )  { 
if  (  check  (  )  )  { 
allmessages  .  addNewsMessage  (  new   NewsClass  (  news  ,  importance  ,  author  )  )  ; 
news  =  ""  ; 
} 
System  .  gc  (  )  ; 
} 




public   void   clearErrors  (  )  { 
if  (  check  (  )  )  { 
allmessages  .  clearErrors  (  )  ; 
} 
return  ; 
} 




public   void   clearNews  (  )  { 
if  (  check  (  )  )  { 
allmessages  .  clearNews  (  )  ; 
} 
} 

@  Remove 
@  Destroy 
public   void   destroy  (  )  { 
} 

@  In  (  create  =  true  ) 
IProjectExport   projectExport  ; 

@  In  (  create  =  true  ) 
IProjectCleaner   projectCleaner  ; 

public   String   cleanupValues  (  )  { 
List  <  PlanProperties  >  ppList  =  em  .  createQuery  (  "select p from PlanProperties p"  )  .  getResultList  (  )  ; 
int   total  =  0  ; 
int   i  =  0  ; 
for  (  PlanProperties   pp  :  ppList  )  { 
int   number  =  projectCleaner  .  cleanupProject  (  pp  .  getId  (  )  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Plan "  +  pp  .  getName  (  )  +  ": removed "  +  number  +  " values."  )  ; 
total  +=  number  ; 
i  ++  ; 
if  (  (  i  %  5  )  ==  0  )  { 
System  .  gc  (  )  ; 
} 
} 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "All projects cleaned up, removed "  +  total  +  " Values objects in total"  )  ; 
return   null  ; 
} 





public   String   clearData  (  )  { 
if  (  check  (  )  )  { 
List  <  Plan  >  projectList  =  em  .  createQuery  (  "select p from Plan p"  )  .  getResultList  (  )  ; 
for  (  Plan   p  :  projectList  )  { 
log  .  info  (  "deleting plan "  +  p  .  getPlanProperties  (  )  .  getName  (  )  )  ; 
log  .  debug  (  "removing value scale linkage..."  )  ; 
for  (  Leaf   l  :  p  .  getTree  (  )  .  getRoot  (  )  .  getAllLeaves  (  )  )  { 
for  (  Alternative   a  :  p  .  getAlternativesDefinition  (  )  .  getAlternatives  (  )  )  { 
Values   values  =  l  .  getValues  (  a  .  getName  (  )  )  ; 
if  (  values  !=  null  )  { 
for  (  Value   v  :  values  .  getList  (  )  )  { 
if  (  v  !=  null  )  { 
v  .  setScale  (  null  )  ; 
} 
} 
} 
} 
} 
log  .  debug  (  "removing entity... "  )  ; 
em  .  remove  (  p  )  ; 
log  .  debug  (  "plan removed"  )  ; 
} 
em  .  flush  (  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "All projects deleted!"  )  ; 
} 
return   null  ; 
} 




@  In  (  required  =  false  ) 
private   String   projectID  ; 





public   String   clearDataUsingProjectID  (  )  { 
if  (  check  (  )  )  { 
List  <  Plan  >  projectList  =  em  .  createQuery  (  "select p from Plan p where p.planProperties.id = "  +  this  .  projectID  )  .  getResultList  (  )  ; 
if  (  !  projectList  .  isEmpty  (  )  )  { 
Plan   p  =  projectList  .  get  (  0  )  ; 
log  .  info  (  "Deleting project "  +  p  .  getPlanProperties  (  )  .  getId  (  )  )  ; 
em  .  remove  (  p  )  ; 
em  .  flush  (  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Plan "  +  this  .  projectID  +  " deleted!"  )  ; 
}  else  { 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Plan "  +  this  .  projectID  +  " not found!"  )  ; 
} 
} 
return   null  ; 
} 

public   String   getPassword  (  )  { 
return   password  ; 
} 

public   void   setPassword  (  String   password  )  { 
this  .  password  =  password  ; 
} 





public   String   clearAllData  (  )  { 
if  (  check  (  )  )  { 
this  .  clearData  (  )  ; 
this  .  resetPublicLibraries  (  )  ; 
this  .  deleteUserLibraries  (  )  ; 
} 
return   null  ; 
} 





public   String   clearKB  (  )  { 
if  (  check  (  )  )  { 
this  .  resetPublicLibraries  (  )  ; 
this  .  deleteUserLibraries  (  )  ; 
} 
return   null  ; 
} 





public   String   exportAllProjectsToZip  (  )  { 
if  (  check  (  )  )  { 
return   projectExport  .  exportAllProjectsToZip  (  )  ; 
} 
return   null  ; 
} 





public   String   exportPrivateTemplates  (  )  { 
if  (  check  (  )  )  { 
List  <  TemplateTree  >  templates  =  em  .  createQuery  (  "select n from TemplateTree n where owner is not null"  )  .  getResultList  (  )  ; 
if  (  !  templates  .  isEmpty  (  )  )  { 
this  .  returnXMLExport  (  exporter  .  exportTemplates  (  templates  )  )  ; 
}  else   FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_ERROR  ,  "No Templates found!"  )  ; 
} 
return   null  ; 
} 




private   String   returnXMLExport  (  Document   doc  )  { 
SimpleDateFormat   format  =  new   SimpleDateFormat  (  "yyyyMMdd_kkmmss"  )  ; 
String   timestamp  =  format  .  format  (  new   Date  (  System  .  currentTimeMillis  (  )  )  )  ; 
String   filename  =  "export_"  +  timestamp  +  ".xml"  ; 
HttpServletResponse   response  =  (  HttpServletResponse  )  FacesContext  .  getCurrentInstance  (  )  .  getExternalContext  (  )  .  getResponse  (  )  ; 
response  .  setContentType  (  "application/x-download"  )  ; 
response  .  setHeader  (  "Content-Disposition"  ,  "attachement; filename=\""  +  filename  +  "\""  )  ; 
try  { 
BufferedOutputStream   out  =  new   BufferedOutputStream  (  response  .  getOutputStream  (  )  )  ; 
XMLWriter   writer  =  new   XMLWriter  (  out  ,  ProjectExporter  .  prettyFormat  )  ; 
writer  .  write  (  doc  )  ; 
writer  .  flush  (  )  ; 
writer  .  close  (  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_ERROR  ,  "An error occured while generating the export file."  )  ; 
log  .  error  (  "Could not open response-outputstream: "  ,  e  )  ; 
} 
FacesContext  .  getCurrentInstance  (  )  .  responseComplete  (  )  ; 
return   null  ; 
} 





public   String   exportAllTemplates  (  )  { 
if  (  check  (  )  )  { 
List  <  TemplateTree  >  templates  =  em  .  createQuery  (  "select n from TemplateTree n"  )  .  getResultList  (  )  ; 
if  (  !  templates  .  isEmpty  (  )  )  { 
this  .  returnXMLExport  (  exporter  .  exportTemplates  (  templates  )  )  ; 
}  else   FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_ERROR  ,  "No Templates found!"  )  ; 
} 
return   null  ; 
} 





public   String   resetPublicLibraries  (  )  { 
if  (  check  (  )  )  { 
List  <  TemplateTree  >  templates  =  em  .  createQuery  (  "select n from TemplateTree n where owner is null"  )  .  getResultList  (  )  ; 
for  (  TemplateTree   t  :  templates  )  { 
log  .  info  (  "Deleting Template Tree "  +  t  .  getName  (  )  )  ; 
em  .  remove  (  t  )  ; 
} 
em  .  flush  (  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Public Templates removed!"  )  ; 
} 
return   null  ; 
} 

public   String   deleteUserLibraries  (  )  { 
if  (  check  (  )  )  { 
List  <  TemplateTree  >  templates  =  em  .  createQuery  (  "select n from TemplateTree n where owner is not null"  )  .  getResultList  (  )  ; 
for  (  TemplateTree   t  :  templates  )  { 
log  .  info  (  "Deleting Template Tree "  +  t  .  getName  (  )  +  " of user "  +  t  .  getOwner  (  )  )  ; 
em  .  remove  (  t  )  ; 
} 
em  .  flush  (  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Private Templates removed!"  )  ; 
} 
return   null  ; 
} 

public   String   refresh  (  )  { 
if  (  exportDir  ==  null  )  return   exportDir  ;  else   return   null  ; 
} 

public   void   munchMem  (  int   mb  )  { 
log  .  info  (  "Munching "  +  mb  +  " MB"  )  ; 
memTest  .  munchMem  (  mb  )  ; 
FacesMessages  .  instance  (  )  .  add  (  FacesMessage  .  SEVERITY_INFO  ,  "Munched "  +  mb  +  " MB"  )  ; 
} 

public   void   releaseMem  (  )  { 
log  .  info  (  "Releasing memory..."  )  ; 
memTest  .  releaseMem  (  )  ; 
} 







public   static   void   main  (  String  [  ]  args  )  { 
System  .  out  .  println  (  computeSHA  (  args  [  0  ]  )  )  ; 
} 
} 

