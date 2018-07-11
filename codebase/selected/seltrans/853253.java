package   cn  .  com  .  once  .  deploytool  .  unit  .  repository  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  Calendar  ; 
import   org  .  apache  .  commons  .  io  .  FileUtils  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   cn  .  com  .  once  .  deploytool  .  Configuration  ; 
import   cn  .  com  .  once  .  deploytool  .  Constants  ; 
import   cn  .  com  .  once  .  deploytool  .  model  .  unit  .  Repository  ; 








public   class   RepositoryHelper  { 

private   String   baseUrl  ; 

private   static   Logger   logger  =  Logger  .  getLogger  (  RepositoryHelper  .  class  )  ; 

private   static   final   String   COMMON  =  "common"  ; 

public   RepositoryHelper  (  Repository   repository  )  { 
if  (  repository  ==  null  ||  StringUtils  .  isEmpty  (  repository  .  getBaseUrl  (  )  )  )  this  .  baseUrl  =  Configuration  .  getProperty  (  Constants  .  LOCALREPOSITORY  )  ;  else   this  .  baseUrl  =  repository  .  getBaseUrl  (  )  ; 
if  (  baseUrl  .  charAt  (  baseUrl  .  length  (  )  -  1  )  ==  File  .  separatorChar  )  baseUrl  =  baseUrl  .  substring  (  0  ,  baseUrl  .  length  (  )  -  1  )  ; 
logger  .  info  (  String  .  format  (  "Use [%s] as local repository url"  ,  baseUrl  )  )  ; 
File   file  =  new   File  (  baseUrl  )  ; 
if  (  !  file  .  exists  (  )  ||  file  .  isFile  (  )  )  { 
logger  .  info  (  String  .  format  (  "[%s] don't exist. Create it."  ,  baseUrl  )  )  ; 
if  (  !  file  .  mkdirs  (  )  )  { 
file  .  delete  (  )  ; 
logger  .  info  (  String  .  format  (  "Can't create [%s] as local repository."  ,  baseUrl  )  )  ; 
} 
} 
} 

public   String   getBaseUrl  (  )  { 
return   baseUrl  ; 
} 

public   void   setBaseUrl  (  String   baseUrl  )  { 
this  .  baseUrl  =  baseUrl  ; 
} 






















public   OriginalDeployUnitDescription   saveFileToRepository  (  File   file  ,  String   type  ,  String   target  ,  String   provider  ,  String   url  )  throws   UnitRepositoryException  { 
if  (  StringUtils  .  isEmpty  (  target  )  )  target  =  COMMON  ; 
if  (  StringUtils  .  isEmpty  (  type  )  )  type  =  COMMON  ; 
if  (  file  ==  null  ||  !  file  .  exists  (  )  )  { 
String   m  =  String  .  format  (  "The file [%s] don't exsit!"  ,  file  .  getAbsoluteFile  (  )  )  ; 
logger  .  error  (  m  )  ; 
throw   new   UnitRepositoryException  (  m  )  ; 
} 
String   targetFileString  =  this  .  baseUrl  +  File  .  separator  +  type  +  File  .  separator  +  target  ; 
try  { 
FileUtils  .  copyFileToDirectory  (  file  ,  new   File  (  targetFileString  )  ,  true  )  ; 
}  catch  (  IOException   e  )  { 
String   m  =  String  .  format  (  "Can't move [%s] to [%s]"  ,  file  .  getAbsoluteFile  (  )  ,  targetFileString  )  ; 
logger  .  error  (  m  )  ; 
throw   new   UnitRepositoryException  (  m  ,  e  )  ; 
} 
OriginalDeployUnitDescription   ouD  =  new   OriginalDeployUnitDescription  (  url  )  ; 
ouD  .  setDate  (  Calendar  .  getInstance  (  )  .  getTime  (  )  )  ; 
ouD  .  setUrl  (  url  )  ; 
ouD  .  setFilename  (  file  .  getName  (  )  )  ; 
ouD  .  setStatus  (  OriginalDeployUnitDescription  .  INVALIDATED  )  ; 
ouD  .  setRepositoryUrl  (  this  .  baseUrl  )  ; 
ouD  .  setType  (  type  )  ; 
ouD  .  setTarget  (  target  )  ; 
ouD  .  setProvider  (  provider  )  ; 
ouD  .  save  (  )  ; 
return   ouD  ; 
} 









public   OriginalDeployUnitDescription   getOriginalDeployUnitDescriptionFromRepository  (  String   type  ,  String   target  ,  String   name  )  { 
if  (  StringUtils  .  isEmpty  (  target  )  )  target  =  COMMON  ; 
if  (  StringUtils  .  isEmpty  (  type  )  )  type  =  COMMON  ; 
String   fileStr  =  this  .  baseUrl  +  File  .  separator  +  type  +  File  .  separator  +  target  +  File  .  separator  +  name  +  ".xml"  ; 
File   file  =  new   File  (  fileStr  )  ; 
return   OriginalDeployUnitDescription  .  load  (  file  )  ; 
} 

public   void   removOriginalDeployUnit  (  String   type  ,  String   target  ,  String   name  )  { 
if  (  StringUtils  .  isEmpty  (  target  )  )  target  =  COMMON  ; 
if  (  StringUtils  .  isEmpty  (  type  )  )  type  =  COMMON  ; 
String   fileStr  =  this  .  baseUrl  +  File  .  separator  +  type  +  File  .  separator  +  target  +  File  .  separator  +  name  ; 
boolean   ret1  =  FileUtils  .  deleteQuietly  (  new   File  (  fileStr  )  )  ; 
if  (  !  ret1  )  logger  .  error  (  String  .  format  (  "Can't delete file [%s]"  ,  fileStr  )  )  ; 
boolean   ret2  =  FileUtils  .  deleteQuietly  (  new   File  (  fileStr  +  ".xml"  )  )  ; 
if  (  !  ret2  )  logger  .  error  (  String  .  format  (  "Can't delete file [%s]"  ,  fileStr  +  ".xml"  )  )  ; 
} 





public   void   cleanRepository  (  )  { 
try  { 
FileUtils  .  cleanDirectory  (  new   File  (  getBaseUrl  (  )  )  )  ; 
}  catch  (  IOException   e  )  { 
logger  .  error  (  String  .  format  (  "Error when clean repository [%s]"  ,  getBaseUrl  (  )  )  )  ; 
} 
} 
} 

