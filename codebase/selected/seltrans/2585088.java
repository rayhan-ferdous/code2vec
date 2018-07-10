package   edu  .  univalle  .  lingweb  .  model  ; 

import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   javax  .  persistence  .  PersistenceException  ; 
import   javax  .  persistence  .  Query  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  log4j  .  xml  .  DOMConfigurator  ; 
import   org  .  eclipse  .  persistence  .  config  .  HintValues  ; 
import   org  .  eclipse  .  persistence  .  config  .  QueryHints  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoMultipleChoiceE3  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoMultipleChoiceE3DAO  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoQuestion  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  CoQuestionDAO  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  EntityManagerHelper  ; 
import   edu  .  univalle  .  lingweb  .  rest  .  RestServiceResult  ; 







public   class   DataManagerMultipleChoiceE3   extends   DataManager  { 






private   Logger   log  =  Logger  .  getLogger  (  DataManagerCourse  .  class  )  ; 




public   DataManagerMultipleChoiceE3  (  )  { 
super  (  )  ; 
DOMConfigurator  .  configure  (  DataManagerCourse  .  class  .  getResource  (  "/log4j.xml"  )  )  ; 
} 












public   RestServiceResult   create  (  RestServiceResult   serviceResult  ,  CoMultipleChoiceE3   coMultipleChoiceE3  )  { 
CoMultipleChoiceE3DAO   coMultipleChoiceE3DAO  =  new   CoMultipleChoiceE3DAO  (  )  ; 
try  { 
coMultipleChoiceE3  .  setMultipleChoiceE3Id  (  getSequence  (  "sq_co_multiple_choice_question"  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
coMultipleChoiceE3DAO  .  save  (  coMultipleChoiceE3  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  coMultipleChoiceE3  )  ; 
log  .  info  (  "Pregunta de selecci�n creada con �xito: "  +  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  )  ; 
Object  [  ]  arrayParam  =  {  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.create.success"  )  ,  arrayParam  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al guardar el curso: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.create.error"  )  ,  e  .  getMessage  (  )  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   update  (  RestServiceResult   serviceResult  ,  CoMultipleChoiceE3   coMultipleChoiceE3  )  { 
CoMultipleChoiceE3DAO   coMultipleChoiceE3DAO  =  new   CoMultipleChoiceE3DAO  (  )  ; 
try  { 
EntityManagerHelper  .  beginTransaction  (  )  ; 
coMultipleChoiceE3DAO  .  update  (  coMultipleChoiceE3  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  coMultipleChoiceE3  )  ; 
Object  [  ]  args  =  {  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.update.success"  )  ,  args  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al guardar el curso: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.update.error"  )  ,  e  .  getMessage  (  )  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   delete  (  RestServiceResult   serviceResult  ,  CoMultipleChoiceE3   coMultipleChoiceE3  )  { 
String   sMultipleChoiceE3Name  =  null  ; 
try  { 
sMultipleChoiceE3Name  =  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  ; 
log  .  error  (  "Eliminando la pregunta de seleccion: "  +  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  Statements  .  DELETE_CO_MULTIPLE_CHOICE_E3  )  ; 
query  .  setParameter  (  1  ,  coMultipleChoiceE3  .  getMultipleChoiceE3Id  (  )  )  ; 
query  .  executeUpdate  (  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  coMultipleChoiceE3  )  ; 
Object  [  ]  arrayParam  =  {  sMultipleChoiceE3Name  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.delete.success"  )  ,  arrayParam  )  )  ; 
log  .  info  (  "Eliminando la pregunta de seleccion: "  +  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al actualizar el curso: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
Object  [  ]  arrayParam  =  {  coMultipleChoiceE3  .  getMultipleChoiceName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.delete.error"  )  +  e  .  getMessage  (  )  ,  arrayParam  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  String   sMultipleChoiceName  )  { 
List  <  CoMultipleChoiceE3  >  list  =  new   CoMultipleChoiceE3DAO  (  )  .  findByMultipleChoiceName  (  sMultipleChoiceName  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.search.notFound"  )  )  ; 
}  else  { 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  Long   nMultipleChoiceE3Id  )  { 
CoMultipleChoiceE3   coMultipleChoiceE3  =  new   CoMultipleChoiceE3DAO  (  )  .  findById  (  nMultipleChoiceE3Id  )  ; 
if  (  coMultipleChoiceE3  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.search.notFound"  )  )  ; 
}  else  { 
List  <  CoMultipleChoiceE3  >  list  =  new   ArrayList  <  CoMultipleChoiceE3  >  (  )  ; 
EntityManagerHelper  .  refresh  (  coMultipleChoiceE3  )  ; 
list  .  add  (  coMultipleChoiceE3  )  ; 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   result  )  { 
return   list  (  result  ,  0  ,  0  )  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   serviceResult  ,  int   nRowStart  ,  int   nMaxResults  )  { 
CoMultipleChoiceE3DAO   coMultipleChoiceE3DAO  =  new   CoMultipleChoiceE3DAO  (  )  ; 
List  <  CoMultipleChoiceE3  >  list  =  coMultipleChoiceE3DAO  .  findAll  (  nRowStart  ,  nMaxResults  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.list.notFound"  )  )  ; 
}  else  { 
Object  [  ]  array  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.list.success"  )  ,  array  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  serviceResult  .  setNumResult  (  coMultipleChoiceE3DAO  .  findAll  (  )  .  size  (  )  )  ;  else   serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
return   serviceResult  ; 
} 

public   RestServiceResult   listMChoiceForExercise3  (  RestServiceResult   serviceResult  ,  Long   nExerciseId  )  { 
CoQuestion   coQuestion  =  new   CoQuestionDAO  (  )  .  findById  (  nExerciseId  )  ; 
EntityManagerHelper  .  refresh  (  coQuestion  )  ; 
if  (  coQuestion  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.search.notFound"  )  )  ; 
}  else  { 
List  <  CoMultipleChoiceE3  >  list  =  new   ArrayList  <  CoMultipleChoiceE3  >  (  coQuestion  .  getCoMultipleChoiceE3s  (  )  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
Object  [  ]  arrayParam  =  {  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.notFound"  )  ,  arrayParam  )  )  ; 
}  else  { 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  ,  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   listPageMChoiceForExercise3  (  RestServiceResult   result  ,  Long   nQuestionId  )  { 
return   listPageMChoiceForExercise3  (  result  ,  nQuestionId  ,  0  ,  0  )  ; 
} 















@  SuppressWarnings  (  "unchecked"  ) 
public   RestServiceResult   listPageMChoiceForExercise3  (  RestServiceResult   serviceResult  ,  Long   nQuestionId  ,  int   nRowStart  ,  int   nMaxResults  )  { 
CoQuestion   coQuestion  =  new   CoQuestionDAO  (  )  .  findById  (  nQuestionId  )  ; 
log  .  info  (  "Entro en el datamanager y el id de la pregunta es: "  +  nQuestionId  )  ; 
EntityManagerHelper  .  refresh  (  coQuestion  )  ; 
if  (  coQuestion  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.search.notFound"  )  )  ; 
}  else  { 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  Statements  .  SELECT_MULTIPLE_CHOICE_IN_QUESTION  ,  CoMultipleChoiceE3  .  class  )  ; 
query  .  setHint  (  QueryHints  .  REFRESH  ,  HintValues  .  TRUE  )  ; 
query  .  setParameter  (  1  ,  nQuestionId  )  ; 
if  (  nRowStart  >  0  )  query  .  setFirstResult  (  nRowStart  )  ; 
if  (  nMaxResults  >  0  )  query  .  setMaxResults  (  nMaxResults  )  ; 
List  <  CoMultipleChoiceE3  >  list  =  query  .  getResultList  (  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
Object  [  ]  arrayParam  =  {  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.notFound"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
}  else  { 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  ,  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  { 
RestServiceResult   serviceResult2  =  listPageMChoiceForExercise3  (  new   RestServiceResult  (  )  ,  nQuestionId  )  ; 
int   nNumMultipleChoice  =  serviceResult2  .  getNumResult  (  )  ; 
serviceResult  .  setNumResult  (  nNumMultipleChoice  )  ; 
}  else   serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   listPageNoSolveMChoiceForExercise3  (  RestServiceResult   result  ,  Long   nQuestionId  ,  Long   nUserId  )  { 
return   listPageNoSolveMChoiceForExercise3  (  result  ,  nQuestionId  ,  nUserId  ,  0  ,  0  )  ; 
} 















@  SuppressWarnings  (  "unchecked"  ) 
public   RestServiceResult   listPageNoSolveMChoiceForExercise3  (  RestServiceResult   serviceResult  ,  Long   nQuestionId  ,  Long   nUserId  ,  int   nRowStart  ,  int   nMaxResults  )  { 
CoQuestion   coQuestion  =  new   CoQuestionDAO  (  )  .  findById  (  nQuestionId  )  ; 
log  .  info  (  "Entro en el datamanager y el id de la pregunta es: "  +  nQuestionId  )  ; 
EntityManagerHelper  .  refresh  (  coQuestion  )  ; 
if  (  coQuestion  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "multipleChoice.search.notFound"  )  )  ; 
}  else  { 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  Statements  .  SELECT_MULTIPLE_CHOICE_IN_QUESTION_2  ,  CoMultipleChoiceE3  .  class  )  ; 
query  .  setHint  (  QueryHints  .  REFRESH  ,  HintValues  .  TRUE  )  ; 
query  .  setParameter  (  1  ,  nQuestionId  )  ; 
query  .  setParameter  (  2  ,  nUserId  )  ; 
if  (  nRowStart  >  0  )  query  .  setFirstResult  (  nRowStart  )  ; 
if  (  nMaxResults  >  0  )  query  .  setMaxResults  (  nMaxResults  )  ; 
List  <  CoMultipleChoiceE3  >  list  =  query  .  getResultList  (  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
Object  [  ]  arrayParam  =  {  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.notFound"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
}  else  { 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  ,  coQuestion  .  getQuestionName  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "multipleChoice.listMultipleChoiceForExerciseE3.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  { 
RestServiceResult   serviceResult2  =  listPageNoSolveMChoiceForExercise3  (  new   RestServiceResult  (  )  ,  nQuestionId  ,  nUserId  )  ; 
int   nNumMultipleChoice  =  serviceResult2  .  getNumResult  (  )  ; 
serviceResult  .  setNumResult  (  nNumMultipleChoice  )  ; 
}  else   serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
} 
return   serviceResult  ; 
} 
} 

