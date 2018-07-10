package   edu  .  univalle  .  lingweb  .  model  ; 

import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   javax  .  persistence  .  PersistenceException  ; 
import   javax  .  persistence  .  Query  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  log4j  .  xml  .  DOMConfigurator  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoSingleTextCheckList3  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoSingleTextCheckList3DAO  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  EntityManagerHelper  ; 
import   edu  .  univalle  .  lingweb  .  rest  .  RestServiceResult  ; 







public   class   DataManagerCheckListStudent3   extends   DataManager  { 






private   Logger   log  =  Logger  .  getLogger  (  DataManagerCourse  .  class  )  ; 




public   DataManagerCheckListStudent3  (  )  { 
super  (  )  ; 
DOMConfigurator  .  configure  (  DataManagerCourse  .  class  .  getResource  (  "/log4j.xml"  )  )  ; 
} 













public   RestServiceResult   create  (  RestServiceResult   result  ,  CoSingleTextCheckList3   coSingleTextCheckList  )  { 
CoSingleTextCheckList3DAO   coSingleTextCheckListDAO  =  new   CoSingleTextCheckList3DAO  (  )  ; 
log  .  info  (  "estoy en el create del data manager"  )  ; 
try  { 
coSingleTextCheckList  .  setCheckListId  (  getSequence  (  "sq_co_single_text_check_1"  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
coSingleTextCheckListDAO  .  save  (  coSingleTextCheckList  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  coSingleTextCheckList  )  ; 
log  .  info  (  "Pregunta de check list creada con �xito: "  +  coSingleTextCheckList  .  getCheckListId  (  )  )  ; 
Object  [  ]  args  =  {  coSingleTextCheckList  .  getCheckListId  (  )  ,  coSingleTextCheckList  .  getTitle  (  )  }  ; 
result  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.create.success"  )  ,  args  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
e  .  printStackTrace  (  )  ; 
log  .  error  (  "Error al guardar la lista de chequeo: "  +  e  .  getMessage  (  )  )  ; 
result  .  setError  (  true  )  ; 
result  .  setMessage  (  e  .  getMessage  (  )  )  ; 
} 
return   result  ; 
} 














public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  Long   nCheckListId  )  { 
CoSingleTextCheckList3   coSingleTextCheckList  =  new   CoSingleTextCheckList3DAO  (  )  .  findById  (  nCheckListId  )  ; 
if  (  coSingleTextCheckList  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "checkList.search.notFound"  )  )  ; 
}  else  { 
List  <  CoSingleTextCheckList3  >  list  =  new   ArrayList  <  CoSingleTextCheckList3  >  (  )  ; 
EntityManagerHelper  .  refresh  (  coSingleTextCheckList  )  ; 
list  .  add  (  coSingleTextCheckList  )  ; 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
return   serviceResult  ; 
} 















public   RestServiceResult   update  (  RestServiceResult   serviceResult  ,  CoSingleTextCheckList3   coSingleTextCheckList  )  { 
CoSingleTextCheckList3DAO   coSingleTextCheckListDAO  =  new   CoSingleTextCheckList3DAO  (  )  ; 
try  { 
log  .  info  (  "Actualizando la t�cnica: "  +  coSingleTextCheckList  .  getTitle  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
coSingleTextCheckListDAO  .  update  (  coSingleTextCheckList  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  coSingleTextCheckList  )  ; 
Object  [  ]  arrayParam  =  {  coSingleTextCheckList  .  getTitle  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.update.success"  )  ,  arrayParam  )  )  ; 
log  .  info  (  "Se actualizo la t�cnica con �xito: "  +  coSingleTextCheckList  .  getTitle  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al actualizar la t�cnica: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  e  .  getMessage  (  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   delete  (  RestServiceResult   serviceResult  ,  CoSingleTextCheckList3   coSingleTextCheckList  )  { 
String   sTitle  =  null  ; 
try  { 
sTitle  =  coSingleTextCheckList  .  getTitle  (  )  ; 
log  .  error  (  "Eliminando la lista de chequeo: "  +  coSingleTextCheckList  .  getTitle  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
Query   query  =  EntityManagerHelper  .  getEntityManager  (  )  .  createNativeQuery  (  Statements  .  DELETE_CHECK_LIST_STUDENT  )  ; 
query  .  setParameter  (  1  ,  coSingleTextCheckList  .  getCheckListId  (  )  )  ; 
query  .  executeUpdate  (  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
Object  [  ]  arrayParam  =  {  sTitle  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.delete.success"  )  ,  arrayParam  )  )  ; 
log  .  info  (  "Eliminando el curso: "  +  coSingleTextCheckList  .  getTitle  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al actualizar el curso: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
Object  [  ]  args  =  {  coSingleTextCheckList  .  getTitle  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.delete.error"  )  +  e  .  getMessage  (  )  ,  args  )  )  ; 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   result  )  { 
return   list  (  result  ,  0  ,  0  )  ; 
} 















public   RestServiceResult   list  (  RestServiceResult   result  ,  int   nRowStart  ,  int   nMaxResults  )  { 
CoSingleTextCheckList3DAO   coSingleTextCheckListDAO  =  new   CoSingleTextCheckList3DAO  (  )  ; 
List  <  CoSingleTextCheckList3  >  list  =  coSingleTextCheckListDAO  .  findAll  (  nRowStart  ,  nMaxResults  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
result  .  setError  (  true  )  ; 
result  .  setMessage  (  bundle  .  getString  (  "checkListStudent.list.notFound"  )  )  ; 
}  else  { 
Object  [  ]  array  =  {  list  .  size  (  )  }  ; 
result  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "checkListStudent.list.success"  )  ,  array  )  )  ; 
result  .  setObjResult  (  list  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  result  .  setNumResult  (  coSingleTextCheckListDAO  .  findAll  (  )  .  size  (  )  )  ;  else   result  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
return   result  ; 
} 
} 

