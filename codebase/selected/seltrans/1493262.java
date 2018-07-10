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
import   edu  .  univalle  .  lingweb  .  persistence  .  EntityManagerHelper  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToAssistance  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToAssistanceDAO  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToAssistenceMetadata  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToAssistenceMetadataDAO  ; 
import   edu  .  univalle  .  lingweb  .  rest  .  RestServiceResult  ; 






public   class   DataManagerAssistance   extends   DataManager  { 






private   Logger   log  =  Logger  .  getLogger  (  DataManagerAssistance  .  class  )  ; 




public   DataManagerAssistance  (  )  { 
super  (  )  ; 
DOMConfigurator  .  configure  (  DataManagerAssistance  .  class  .  getResource  (  "/log4j.xml"  )  )  ; 
} 













public   RestServiceResult   create  (  RestServiceResult   result  ,  ToAssistance   toAssistance  )  { 
ToAssistanceDAO   maAssistanceDAO  =  new   ToAssistanceDAO  (  )  ; 
try  { 
toAssistance  .  setAssistanceId  (  getSequence  (  "sq_to_assistance"  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
maAssistanceDAO  .  save  (  toAssistance  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  toAssistance  )  ; 
log  .  info  (  "Se cre� la ayuda con �xito: "  +  toAssistance  .  getAssistanceId  (  )  )  ; 
Object  [  ]  args  =  {  toAssistance  .  getAssistanceId  (  )  }  ; 
result  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.create.success"  )  ,  args  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
e  .  printStackTrace  (  )  ; 
log  .  error  (  "Error al guardar la Ayuda: "  +  e  .  getMessage  (  )  )  ; 
result  .  setError  (  true  )  ; 
result  .  setMessage  (  e  .  getMessage  (  )  )  ; 
} 
return   result  ; 
} 









public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  Long   nAssistanceId  )  { 
ToAssistance   maAssistance  =  new   ToAssistanceDAO  (  )  .  findById  (  nAssistanceId  )  ; 
if  (  maAssistance  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "assistance.search.notFound"  )  )  ; 
}  else  { 
List  <  ToAssistance  >  list  =  new   ArrayList  <  ToAssistance  >  (  )  ; 
EntityManagerHelper  .  refresh  (  maAssistance  )  ; 
list  .  add  (  maAssistance  )  ; 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
log  .  info  (  "Se encontro "  +  list  .  size  (  )  +  " configuraci�n para el asistente "  +  nAssistanceId  )  ; 
} 
return   serviceResult  ; 
} 









public   RestServiceResult   searchAssistantByMet  (  RestServiceResult   serviceResult  ,  String   sTableName  ,  String   sColumnName  )  { 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  Statements  .  SELECT_TO_ASSISTANCE_BY_METADATA  ,  ToAssistenceMetadata  .  class  )  ; 
query  .  setParameter  (  1  ,  sTableName  )  ; 
query  .  setParameter  (  2  ,  sColumnName  )  ; 
query  .  setHint  (  QueryHints  .  REFRESH  ,  HintValues  .  TRUE  )  ; 
List  <  ToAssistenceMetadata  >  list  =  query  .  getResultList  (  )  ; 
ToAssistenceMetadata   toAssistanceAssistenceMetadata  =  null  ; 
Long   nAssistanceId  =  null  ; 
if  (  list  !=  null  )  for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  { 
toAssistanceAssistenceMetadata  =  (  ToAssistenceMetadata  )  list  .  get  (  i  )  ; 
} 
if  (  toAssistanceAssistenceMetadata  !=  null  )  { 
nAssistanceId  =  toAssistanceAssistenceMetadata  .  getToAssistance  (  )  .  getAssistanceId  (  )  ; 
serviceResult  =  search  (  serviceResult  ,  nAssistanceId  )  ; 
}  else  { 
serviceResult  .  setMessage  (  bundle  .  getString  (  "assistance.search.notFound"  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
return   serviceResult  ; 
} 









public   RestServiceResult   delete  (  RestServiceResult   serviceResult  ,  ToAssistance   maAssistance  )  { 
try  { 
EntityManagerHelper  .  beginTransaction  (  )  ; 
Query   query  =  EntityManagerHelper  .  getEntityManager  (  )  .  createNativeQuery  (  Statements  .  DELETE_TO_ASSISTANCE  )  ; 
query  .  setParameter  (  1  ,  maAssistance  .  getAssistanceId  (  )  )  ; 
query  .  executeUpdate  (  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  maAssistance  )  ; 
Object  [  ]  arrayParam  =  {  maAssistance  .  getAssistanceId  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.delete.success"  )  ,  arrayParam  )  )  ; 
log  .  info  (  "Eliminando la configuraci�n del asistente: "  +  maAssistance  .  getAssistanceId  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al eliminar la configuraci�n del asistente: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
Object  [  ]  arrayParam  =  {  maAssistance  .  getAssistanceId  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.delete.error"  )  +  e  .  getMessage  (  )  ,  arrayParam  )  )  ; 
} 
return   serviceResult  ; 
} 















public   RestServiceResult   update  (  RestServiceResult   serviceResult  ,  ToAssistance   assistance  )  { 
ToAssistanceDAO   assistanceDAO  =  new   ToAssistanceDAO  (  )  ; 
try  { 
log  .  info  (  "Actualizando la Ayuda: "  +  assistance  .  getAssistanceId  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
assistanceDAO  .  update  (  assistance  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  assistance  )  ; 
Object  [  ]  arrayParam  =  {  assistance  .  getAssistanceId  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.update.success"  )  ,  arrayParam  )  )  ; 
log  .  info  (  "Se actualizo la Ayuda con �xito: "  +  assistance  .  getAssistanceId  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al actualizar la Ayuda: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  e  .  getMessage  (  )  )  ; 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   result  )  { 
return   list  (  result  ,  0  ,  0  )  ; 
} 















public   RestServiceResult   list  (  RestServiceResult   result  ,  int   nRowStart  ,  int   nMaxResults  )  { 
ToAssistanceDAO   assistanceDAO  =  new   ToAssistanceDAO  (  )  ; 
List  <  ToAssistance  >  list  =  assistanceDAO  .  findAll  (  nRowStart  ,  nMaxResults  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
result  .  setError  (  true  )  ; 
result  .  setMessage  (  bundle  .  getString  (  "assistance.list.notFound"  )  )  ; 
}  else  { 
Object  [  ]  array  =  {  list  .  size  (  )  }  ; 
result  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "assistance.list.success"  )  ,  array  )  )  ; 
result  .  setObjResult  (  list  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  result  .  setNumResult  (  assistanceDAO  .  findAll  (  )  .  size  (  )  )  ;  else   result  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
return   result  ; 
} 








private   void   addMetadata  (  ToAssistenceMetadata   toAssistenceMetadata  ,  ToAssistance   toAssistance  )  { 
try  { 
toAssistenceMetadata  .  setAssistanceMetadataId  (  getSequence  (  "sq_to_assistence_metadata"  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
new   ToAssistenceMetadataDAO  (  )  .  save  (  toAssistenceMetadata  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  toAssistenceMetadata  )  ; 
log  .  info  (  "Se asocio la getAssistanceMetadataId "  +  toAssistenceMetadata  .  getAssistanceMetadataId  (  )  )  ; 
log  .  info  (  "Se asocio la getTableName  "  +  toAssistenceMetadata  .  getTableName  (  )  )  ; 
log  .  info  (  "Se asocio la getColumnName  "  +  toAssistenceMetadata  .  getColumnName  (  )  )  ; 
log  .  info  (  "Se asocio la getMetadataTypeId  "  +  toAssistenceMetadata  .  getToMetadataType  (  )  .  getMetadataTypeId  (  )  )  ; 
log  .  info  (  "Se asocio la getAssistanceId  "  +  toAssistenceMetadata  .  getToAssistance  (  )  .  getAssistanceId  (  )  )  ; 
log  .  info  (  "Se asocio la metadata '"  +  toAssistenceMetadata  .  getAssistanceMetadataId  (  )  +  "' con �xito al asistente '"  +  toAssistance  .  getAssistanceId  (  )  +  "' "  )  ; 
}  catch  (  PersistenceException   e  )  { 
log  .  info  (  "La metadata '"  +  toAssistenceMetadata  .  getAssistanceMetadataId  (  )  +  "' ya esta asociada a al asistente '"  +  toAssistance  .  getAssistanceId  (  )  +  "' "  +  "OMITIR EXCEPCION PRIMARY KEY"  )  ; 
} 
return  ; 
} 











public   RestServiceResult   addMetadata  (  RestServiceResult   serviceResult  ,  ToAssistenceMetadata   toAssistenceMetadata  ,  ToAssistance   toAssistance  )  { 
log  .  info  (  "METADATA ASOCIADA AL ASISTENTE"  )  ; 
Long   nAssistanceId  =  toAssistance  .  getAssistanceId  (  )  ; 
log  .  info  (  "Agregando Metadata al Asistente: "  +  nAssistanceId  )  ; 
addMetadata  (  toAssistenceMetadata  ,  toAssistance  )  ; 
log  .  info  (  "TERMININA ASOCIACION DE LA METADATA"  )  ; 
return   serviceResult  ; 
} 
} 

