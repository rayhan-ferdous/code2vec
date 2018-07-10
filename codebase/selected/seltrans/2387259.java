package   org  .  opencms  .  workplace  .  tools  .  database  ; 

import   org  .  opencms  .  db  .  CmsDbIoException  ; 
import   org  .  opencms  .  file  .  CmsFolder  ; 
import   org  .  opencms  .  file  .  CmsObject  ; 
import   org  .  opencms  .  file  .  CmsProperty  ; 
import   org  .  opencms  .  file  .  CmsPropertyDefinition  ; 
import   org  .  opencms  .  file  .  CmsResource  ; 
import   org  .  opencms  .  file  .  CmsResourceFilter  ; 
import   org  .  opencms  .  file  .  types  .  CmsResourceTypeFolder  ; 
import   org  .  opencms  .  file  .  types  .  CmsResourceTypeImage  ; 
import   org  .  opencms  .  file  .  types  .  CmsResourceTypePlain  ; 
import   org  .  opencms  .  file  .  types  .  CmsResourceTypePointer  ; 
import   org  .  opencms  .  file  .  types  .  CmsResourceTypeXmlPage  ; 
import   org  .  opencms  .  i18n  .  CmsEncoder  ; 
import   org  .  opencms  .  i18n  .  CmsMessageContainer  ; 
import   org  .  opencms  .  importexport  .  CmsImportExportException  ; 
import   org  .  opencms  .  loader  .  CmsResourceManager  ; 
import   org  .  opencms  .  lock  .  CmsLock  ; 
import   org  .  opencms  .  lock  .  CmsLockType  ; 
import   org  .  opencms  .  main  .  CmsException  ; 
import   org  .  opencms  .  main  .  CmsIllegalArgumentException  ; 
import   org  .  opencms  .  main  .  CmsLog  ; 
import   org  .  opencms  .  main  .  OpenCms  ; 
import   org  .  opencms  .  relations  .  CmsLink  ; 
import   org  .  opencms  .  report  .  I_CmsReport  ; 
import   org  .  opencms  .  staticexport  .  CmsLinkTable  ; 
import   org  .  opencms  .  util  .  CmsFileUtil  ; 
import   org  .  opencms  .  util  .  CmsStringUtil  ; 
import   org  .  opencms  .  xml  .  page  .  CmsXmlPage  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   org  .  apache  .  commons  .  collections  .  ExtendedProperties  ; 
import   org  .  apache  .  commons  .  fileupload  .  FileItem  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 


















public   class   CmsHtmlImport  { 


public   static   final   String   META_PROPERTIES  =  "meta.properties"  ; 


private   static   final   Log   LOG  =  CmsLog  .  getLog  (  CmsHtmlImport  .  class  )  ; 










public   static   File   createTempFolder  (  String   name  )  throws   Exception  { 
File   folder  =  null  ; 
folder  =  File  .  createTempFile  (  name  ,  ""  ,  null  )  ; 
folder  .  delete  (  )  ; 
folder  .  mkdirs  (  )  ; 
folder  .  deleteOnExit  (  )  ; 
return   folder  ; 
} 


private   CmsObject   m_cmsObject  ; 


private   String   m_destinationDir  ; 


private   String   m_downloadGallery  ; 


private   String   m_element  ; 


private   String   m_endPattern  ; 


private   Map   m_extensions  ; 


private   HashSet   m_externalLinks  ; 


private   HashMap   m_fileIndex  ; 


private   CmsHtmlImportConverter   m_htmlConverter  ; 


private   String   m_httpDir  ; 


private   String   m_imageGallery  ; 


private   HashMap   m_imageInfo  ; 


private   String   m_inputDir  ; 


private   String   m_inputEncoding  ; 


private   boolean   m_keepBrokenLinks  ; 


private   String   m_linkGallery  ; 


private   String   m_locale  ; 


private   boolean   m_overwrite  ; 


private   HashMap   m_parents  ; 


private   I_CmsReport   m_report  ; 


private   String   m_startPattern  ; 


private   String   m_template  ; 




public   CmsHtmlImport  (  )  { 
m_overwrite  =  true  ; 
m_extensions  =  OpenCms  .  getResourceManager  (  )  .  getExtensionMapping  (  )  ; 
m_fileIndex  =  new   HashMap  (  )  ; 
m_parents  =  new   HashMap  (  )  ; 
m_imageInfo  =  new   HashMap  (  )  ; 
m_externalLinks  =  new   HashSet  (  )  ; 
m_htmlConverter  =  new   CmsHtmlImportConverter  (  this  ,  false  )  ; 
} 






public   CmsHtmlImport  (  CmsObject   cms  )  { 
this  (  )  ; 
m_cmsObject  =  cms  ; 
} 











public   String   getAbsoluteUri  (  String   relativeUri  ,  String   baseUri  )  { 
if  (  (  relativeUri  ==  null  )  ||  (  relativeUri  .  charAt  (  0  )  ==  '/'  )  ||  (  relativeUri  .  startsWith  (  "#"  )  )  )  { 
return   relativeUri  ; 
} 
String   windowsAddition  =  ""  ; 
if  (  File  .  separator  .  equals  (  "\\"  )  )  { 
windowsAddition  =  ":"  ; 
} 
try  { 
URL   baseUrl  =  new   URL  (  "file://"  )  ; 
URL   url  =  new   URL  (  new   URL  (  baseUrl  ,  "file://"  +  baseUri  )  ,  relativeUri  )  ; 
if  (  url  .  getQuery  (  )  ==  null  )  { 
if  (  url  .  getRef  (  )  ==  null  )  { 
return   url  .  getHost  (  )  +  windowsAddition  +  url  .  getPath  (  )  ; 
}  else  { 
return   url  .  getHost  (  )  +  windowsAddition  +  url  .  getPath  (  )  +  "#"  +  url  .  getRef  (  )  ; 
} 
}  else  { 
return   url  .  getHost  (  )  +  windowsAddition  +  url  .  getPath  (  )  +  "?"  +  url  .  getQuery  (  )  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
return   relativeUri  ; 
} 
} 






public   String   getDestinationDir  (  )  { 
return   m_destinationDir  ; 
} 






public   String   getDownloadGallery  (  )  { 
return   m_downloadGallery  ; 
} 






public   String   getElement  (  )  { 
return   m_element  ; 
} 






public   String   getEndPattern  (  )  { 
return   m_endPattern  ; 
} 






public   String   getHttpDir  (  )  { 
return   m_httpDir  ; 
} 






public   String   getImageGallery  (  )  { 
return   m_imageGallery  ; 
} 






public   String   getInputDir  (  )  { 
return   m_inputDir  ; 
} 






public   String   getInputEncoding  (  )  { 
return   m_inputEncoding  ; 
} 






public   String   getLinkGallery  (  )  { 
return   m_linkGallery  ; 
} 






public   String   getLocale  (  )  { 
return   m_locale  ; 
} 






public   String   getStartPattern  (  )  { 
return   m_startPattern  ; 
} 






public   String   getTemplate  (  )  { 
return   m_template  ; 
} 






public   boolean   isKeepBrokenLinks  (  )  { 
return   m_keepBrokenLinks  ; 
} 






public   boolean   isOverwrite  (  )  { 
return   m_overwrite  ; 
} 






public   void   setCmsObject  (  CmsObject   cmsObject  )  { 
m_cmsObject  =  cmsObject  ; 
} 






public   void   setDestinationDir  (  String   destinationDir  )  { 
m_destinationDir  =  destinationDir  ; 
} 






public   void   setDownloadGallery  (  String   downloadGallery  )  { 
m_downloadGallery  =  downloadGallery  ; 
} 






public   void   setElement  (  String   element  )  { 
m_element  =  element  ; 
} 






public   void   setEndPattern  (  String   endPattern  )  { 
m_endPattern  =  endPattern  ; 
} 






public   void   setHttpDir  (  String   httpDir  )  { 
m_httpDir  =  httpDir  ; 
} 






public   void   setImageGallery  (  String   imageGallery  )  { 
m_imageGallery  =  imageGallery  ; 
} 






public   void   setInputDir  (  String   inputDir  )  { 
m_inputDir  =  inputDir  ; 
} 






public   void   setInputEncoding  (  String   inputEncoding  )  { 
m_inputEncoding  =  inputEncoding  ; 
} 






public   void   setKeepBrokenLinks  (  boolean   keepBrokenLinks  )  { 
m_keepBrokenLinks  =  keepBrokenLinks  ; 
} 






public   void   setLinkGallery  (  String   linkGallery  )  { 
m_linkGallery  =  linkGallery  ; 
} 






public   void   setLocale  (  String   locale  )  { 
m_locale  =  locale  ; 
} 






public   void   setOverwrite  (  boolean   overwrite  )  { 
m_overwrite  =  overwrite  ; 
} 






public   void   setStartPattern  (  String   startPattern  )  { 
m_startPattern  =  startPattern  ; 
} 






public   void   setTemplate  (  String   template  )  { 
m_template  =  template  ; 
} 










public   void   startImport  (  I_CmsReport   report  )  throws   Exception  { 
try  { 
m_report  =  report  ; 
m_report  .  println  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_HTML_IMPORT_BEGIN_0  )  ,  I_CmsReport  .  FORMAT_HEADLINE  )  ; 
boolean   isStream  =  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_httpDir  )  ; 
File   streamFolder  =  null  ; 
if  (  isStream  )  { 
streamFolder  =  unzipStream  (  )  ; 
m_inputDir  =  streamFolder  .  getAbsolutePath  (  )  ; 
} 
buildIndex  (  m_inputDir  )  ; 
buildParentPath  (  )  ; 
copyHtmlFiles  (  m_inputDir  )  ; 
copyOtherFiles  (  m_inputDir  )  ; 
createExternalLinks  (  )  ; 
if  (  isStream  &&  streamFolder  !=  null  )  { 
m_report  .  println  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_HTML_DELETE_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
CmsFileUtil  .  purgeDirectory  (  streamFolder  )  ; 
File   file  =  new   File  (  m_httpDir  )  ; 
if  (  file  .  exists  (  )  &&  file  .  canWrite  (  )  )  { 
file  .  delete  (  )  ; 
} 
} 
m_report  .  println  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_HTML_IMPORT_END_0  )  ,  I_CmsReport  .  FORMAT_HEADLINE  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 










public   String   storeExternalLink  (  String   externalLink  )  { 
if  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_linkGallery  )  )  { 
m_externalLinks  .  add  (  externalLink  )  ; 
return   getExternalLinkFile  (  externalLink  )  ; 
} 
return   null  ; 
} 









public   void   storeImageInfo  (  String   image  ,  String   altText  )  { 
m_imageInfo  .  put  (  image  ,  altText  )  ; 
} 










public   String   translateLink  (  String   link  )  { 
String   translatedLink  =  null  ; 
translatedLink  =  (  String  )  m_fileIndex  .  get  (  link  .  replace  (  '\\'  ,  '/'  )  )  ; 
if  (  translatedLink  ==  null  )  { 
if  (  link  .  startsWith  (  "#"  )  )  { 
translatedLink  =  link  ; 
}  else   if  (  link  .  startsWith  (  "/"  )  )  { 
if  (  link  .  startsWith  (  OpenCms  .  getSystemInfo  (  )  .  getOpenCmsContext  (  )  )  )  { 
link  =  link  .  substring  (  OpenCms  .  getSystemInfo  (  )  .  getOpenCmsContext  (  )  .  length  (  )  )  ; 
} 
if  (  (  m_keepBrokenLinks  )  ||  (  m_cmsObject  .  existsResource  (  link  )  )  )  { 
translatedLink  =  link  ; 
} 
}  else  { 
String   fileBase  =  getBasePath  (  m_inputDir  ,  link  )  ; 
String   cmsBase  =  (  String  )  m_parents  .  get  (  fileBase  )  ; 
if  (  cmsBase  !=  null  )  { 
String   outLink  =  cmsBase  +  link  .  substring  (  fileBase  .  length  (  )  )  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  (  m_keepBrokenLinks  )  ||  (  m_cmsObject  .  existsResource  (  outLink  )  )  )  { 
translatedLink  =  outLink  ; 
} 
} 
} 
} 
if  (  (  translatedLink  !=  null  )  &&  translatedLink  .  endsWith  (  "/"  )  )  { 
translatedLink  +=  "index.html"  ; 
} 
if  (  translatedLink  ==  null  )  { 
translatedLink  =  "#"  ; 
} 
return   translatedLink  ; 
} 










public   void   validate  (  FileItem   fi  ,  boolean   isdefault  )  throws   CmsIllegalArgumentException  { 
if  (  fi  ==  null  )  { 
if  (  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_inputDir  )  &&  !  isdefault  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_INPUTDIR_1  ,  m_inputDir  )  )  ; 
}  else   if  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_inputDir  )  )  { 
File   inputDir  =  new   File  (  m_inputDir  )  ; 
if  (  !  inputDir  .  exists  (  )  ||  inputDir  .  isFile  (  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_INPUTDIR_1  ,  m_inputDir  )  )  ; 
} 
} 
} 
try  { 
if  (  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_destinationDir  )  &&  !  isdefault  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_DESTDIR_1  ,  m_destinationDir  )  )  ; 
}  else   if  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_destinationDir  )  )  { 
m_cmsObject  .  readFolder  (  m_destinationDir  )  ; 
} 
}  catch  (  CmsException   e  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_DESTDIR_1  ,  m_destinationDir  )  ,  e  )  ; 
} 
if  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_imageGallery  )  )  { 
try  { 
CmsFolder   folder  =  m_cmsObject  .  readFolder  (  m_imageGallery  )  ; 
String   name  =  OpenCms  .  getResourceManager  (  )  .  getResourceType  (  folder  .  getTypeId  (  )  )  .  getTypeName  (  )  ; 
if  (  !  name  .  equals  (  "imagegallery"  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_IMGGALLERY_INVALID_1  ,  m_imageGallery  )  )  ; 
} 
}  catch  (  CmsException   e  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_IMGGALLERY_1  ,  m_imageGallery  )  ,  e  )  ; 
} 
} 
if  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_linkGallery  )  )  { 
try  { 
CmsFolder   folder  =  m_cmsObject  .  readFolder  (  m_linkGallery  )  ; 
String   name  =  OpenCms  .  getResourceManager  (  )  .  getResourceType  (  folder  .  getTypeId  (  )  )  .  getTypeName  (  )  ; 
if  (  !  name  .  equals  (  "linkgallery"  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_LINKGALLERY_INVALID_1  ,  m_linkGallery  )  )  ; 
} 
}  catch  (  CmsException   e  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_LINKGALLERY_1  ,  m_linkGallery  )  ,  e  )  ; 
} 
} 
if  (  (  !  isExternal  (  m_downloadGallery  )  )  &&  (  !  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_downloadGallery  )  )  )  { 
try  { 
CmsFolder   folder  =  m_cmsObject  .  readFolder  (  m_downloadGallery  )  ; 
String   name  =  OpenCms  .  getResourceManager  (  )  .  getResourceType  (  folder  .  getTypeId  (  )  )  .  getTypeName  (  )  ; 
if  (  !  name  .  equals  (  "downloadgallery"  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_DOWNGALLERY_INVALID_1  ,  m_downloadGallery  )  )  ; 
} 
}  catch  (  CmsException   e  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_DOWNGALLERY_1  ,  m_downloadGallery  )  ,  e  )  ; 
} 
} 
try  { 
m_cmsObject  .  readResource  (  m_template  ,  CmsResourceFilter  .  ALL  )  ; 
}  catch  (  CmsException   e  )  { 
if  (  !  isValidElement  (  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_TEMPLATE_1  ,  m_template  )  ,  e  )  ; 
} 
} 
if  (  !  isValidElement  (  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_INVALID_ELEM_2  ,  m_element  ,  m_template  )  )  ; 
} 
if  (  m_cmsObject  .  getRequestContext  (  )  .  currentProject  (  )  .  isOnlineProject  (  )  )  { 
throw   new   CmsIllegalArgumentException  (  Messages  .  get  (  )  .  container  (  Messages  .  GUI_HTMLIMPORT_CONSTRAINT_OFFLINE_0  )  )  ; 
} 
} 








private   void   buildIndex  (  String   startfolder  )  throws   Exception  { 
File   folder  =  new   File  (  startfolder  )  ; 
File  [  ]  subresources  =  folder  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  subresources  .  length  ;  i  ++  )  { 
try  { 
String   relativeFSName  =  subresources  [  i  ]  .  getAbsolutePath  (  )  .  substring  (  m_inputDir  .  length  (  )  +  1  )  ; 
String   absoluteVFSName  =  getVfsName  (  relativeFSName  ,  subresources  [  i  ]  .  getName  (  )  ,  subresources  [  i  ]  .  isFile  (  )  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_CREATE_INDEX_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  relativeFSName  .  replace  (  '\\'  ,  '/'  )  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_ARROW_RIGHT_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  absoluteVFSName  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
m_fileIndex  .  put  (  subresources  [  i  ]  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  ,  absoluteVFSName  )  ; 
if  (  subresources  [  i  ]  .  isDirectory  (  )  )  { 
buildIndex  (  subresources  [  i  ]  .  getAbsolutePath  (  )  )  ; 
} 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
}  catch  (  Exception   e  )  { 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
m_report  .  println  (  e  )  ; 
} 
} 
} 





private   void   buildParentPath  (  )  { 
String   destFolder  =  m_destinationDir  ; 
String   inputDir  =  m_inputDir  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  !  inputDir  .  endsWith  (  "/"  )  )  { 
inputDir  +=  "/"  ; 
} 
int   pos  =  inputDir  .  lastIndexOf  (  "/"  )  ; 
while  (  (  pos  >  0  )  &&  (  destFolder  !=  null  )  )  { 
inputDir  =  inputDir  .  substring  (  0  ,  pos  )  ; 
m_parents  .  put  (  inputDir  +  "/"  ,  destFolder  )  ; 
pos  =  inputDir  .  lastIndexOf  (  "/"  ,  pos  -  1  )  ; 
destFolder  =  CmsResource  .  getParentFolder  (  destFolder  )  ; 
} 
} 






private   void   closeStream  (  InputStream   stream  )  { 
if  (  stream  !=  null  )  { 
try  { 
stream  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
LOG  .  error  (  ex  .  getLocalizedMessage  (  )  ,  ex  )  ; 
} 
} 
} 








private   void   copyHtmlFiles  (  String   startfolder  )  throws   Exception  { 
try  { 
File   folder  =  new   File  (  startfolder  )  ; 
File  [  ]  subresources  =  folder  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  subresources  .  length  ;  i  ++  )  { 
if  (  subresources  [  i  ]  .  isDirectory  (  )  )  { 
Hashtable   properties  =  new   Hashtable  (  )  ; 
createFolder  (  subresources  [  i  ]  .  getAbsolutePath  (  )  ,  i  ,  properties  )  ; 
copyHtmlFiles  (  subresources  [  i  ]  .  getAbsolutePath  (  )  )  ; 
}  else  { 
String   vfsFileName  =  (  String  )  m_fileIndex  .  get  (  subresources  [  i  ]  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  )  ; 
int   type  =  getFileType  (  vfsFileName  )  ; 
if  (  CmsResourceTypePlain  .  getStaticTypeId  (  )  ==  type  )  { 
Hashtable   properties  =  new   Hashtable  (  )  ; 
String   content  =  ""  ; 
try  { 
content  =  parseHtmlFile  (  subresources  [  i  ]  ,  properties  )  ; 
}  catch  (  CmsException   e  )  { 
m_report  .  println  (  e  )  ; 
} 
properties  .  put  (  "template"  ,  m_template  )  ; 
createFile  (  subresources  [  i  ]  .  getAbsolutePath  (  )  ,  i  ,  content  ,  properties  )  ; 
} 
} 
} 
}  catch  (  Exception   e  )  { 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
} 
} 






private   void   copyOtherFiles  (  String   startfolder  )  { 
try  { 
File   folder  =  new   File  (  startfolder  )  ; 
File  [  ]  subresources  =  folder  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  subresources  .  length  ;  i  ++  )  { 
if  (  subresources  [  i  ]  .  isDirectory  (  )  )  { 
copyOtherFiles  (  subresources  [  i  ]  .  getAbsolutePath  (  )  )  ; 
}  else  { 
if  (  !  subresources  [  i  ]  .  getName  (  )  .  equals  (  META_PROPERTIES  )  )  { 
String   vfsFileName  =  (  String  )  m_fileIndex  .  get  (  subresources  [  i  ]  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  )  ; 
int   type  =  getFileType  (  vfsFileName  )  ; 
if  (  CmsResourceTypePlain  .  getStaticTypeId  (  )  !=  type  )  { 
if  (  isExternal  (  vfsFileName  )  )  { 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_SKIP_EXTERNAL_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  subresources  [  i  ]  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_ARROW_RIGHT_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  vfsFileName  )  )  ; 
}  else  { 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_IMPORT_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  vfsFileName  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
byte  [  ]  content  =  getFileBytes  (  subresources  [  i  ]  )  ; 
List   properties  =  new   ArrayList  (  )  ; 
String   altText  =  (  String  )  m_imageInfo  .  get  (  subresources  [  i  ]  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  )  ; 
CmsProperty   property1  =  new   CmsProperty  (  CmsPropertyDefinition  .  PROPERTY_DESCRIPTION  ,  altText  ,  altText  )  ; 
CmsProperty   property2  =  new   CmsProperty  (  CmsPropertyDefinition  .  PROPERTY_TITLE  ,  altText  ,  altText  )  ; 
if  (  altText  !=  null  )  { 
properties  .  add  (  property1  )  ; 
properties  .  add  (  property2  )  ; 
} 
if  (  !  m_overwrite  )  { 
m_cmsObject  .  createResource  (  vfsFileName  ,  type  ,  content  ,  properties  )  ; 
}  else  { 
try  { 
CmsLock   lock  =  m_cmsObject  .  getLock  (  vfsFileName  )  ; 
if  (  lock  .  getType  (  )  !=  CmsLockType  .  EXCLUSIVE  )  { 
m_cmsObject  .  lockResource  (  vfsFileName  )  ; 
} 
m_cmsObject  .  deleteResource  (  vfsFileName  ,  CmsResource  .  DELETE_PRESERVE_SIBLINGS  )  ; 
}  catch  (  CmsException   e  )  { 
}  finally  { 
m_cmsObject  .  createResource  (  vfsFileName  ,  type  ,  content  ,  properties  )  ; 
} 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_OVERWRITE_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
} 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
} 
} 
} 
} 
} 
}  catch  (  Exception   e  )  { 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
m_report  .  println  (  e  )  ; 
} 
} 





private   void   createExternalLinks  (  )  { 
Iterator   i  =  m_externalLinks  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
String   linkUrl  =  (  String  )  i  .  next  (  )  ; 
String   filename  =  getExternalLinkFile  (  linkUrl  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_CREATE_EXTERNAL_LINK_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  filename  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
List   properties  =  new   ArrayList  (  )  ; 
CmsProperty   property1  =  new   CmsProperty  (  CmsPropertyDefinition  .  PROPERTY_TITLE  ,  "Link to "  +  linkUrl  ,  "Link to "  +  linkUrl  )  ; 
properties  .  add  (  property1  )  ; 
try  { 
m_cmsObject  .  createResource  (  m_linkGallery  +  filename  ,  CmsResourceTypePointer  .  getStaticTypeId  (  )  ,  linkUrl  .  getBytes  (  )  ,  properties  )  ; 
}  catch  (  CmsException   e  )  { 
} 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
} 
} 









private   void   createFile  (  String   filename  ,  int   position  ,  String   content  ,  Hashtable   properties  )  { 
String   vfsFileName  =  (  String  )  m_fileIndex  .  get  (  filename  .  replace  (  '\\'  ,  '/'  )  )  ; 
if  (  vfsFileName  !=  null  )  { 
try  { 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_CREATE_FILE_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  vfsFileName  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
if  (  (  properties  .  get  (  CmsPropertyDefinition  .  PROPERTY_NAVPOS  )  ==  null  )  &&  (  properties  .  get  (  CmsPropertyDefinition  .  PROPERTY_NAVTEXT  )  !=  null  )  )  { 
properties  .  put  (  CmsPropertyDefinition  .  PROPERTY_NAVPOS  ,  (  position  +  1  )  +  ""  )  ; 
} 
Locale   locale  =  new   Locale  (  m_locale  )  ; 
CmsXmlPage   page  =  new   CmsXmlPage  (  locale  ,  OpenCms  .  getSystemInfo  (  )  .  getDefaultEncoding  (  )  )  ; 
page  .  addValue  (  m_element  ,  locale  )  ; 
page  .  setStringValue  (  m_cmsObject  ,  m_element  ,  locale  ,  content  )  ; 
CmsLinkTable   linkTable  =  page  .  getLinkTable  (  m_element  ,  locale  )  ; 
Iterator   i  =  linkTable  .  iterator  (  )  ; 
while  (  i  .  hasNext  (  )  )  { 
CmsLink   link  =  (  CmsLink  )  i  .  next  (  )  ; 
String   target  =  link  .  getTarget  (  )  ; 
if  (  link  .  isInternal  (  )  )  { 
target  =  m_cmsObject  .  getRequestContext  (  )  .  getFileTranslator  (  )  .  translateResource  (  target  )  ; 
link  .  updateLink  (  target  ,  link  .  getAnchor  (  )  ,  link  .  getQuery  (  )  )  ; 
link  .  checkConsistency  (  m_cmsObject  )  ; 
} 
} 
byte  [  ]  contentByteArray  =  page  .  marshal  (  )  ; 
List   oldProperties  =  new   ArrayList  (  )  ; 
if  (  !  m_overwrite  )  { 
m_cmsObject  .  createResource  (  vfsFileName  ,  CmsResourceTypeXmlPage  .  getStaticTypeId  (  )  ,  contentByteArray  ,  new   ArrayList  (  )  )  ; 
}  else  { 
try  { 
oldProperties  =  m_cmsObject  .  readPropertyObjects  (  vfsFileName  ,  false  )  ; 
CmsLock   lock  =  m_cmsObject  .  getLock  (  vfsFileName  )  ; 
if  (  lock  .  getType  (  )  !=  CmsLockType  .  EXCLUSIVE  )  { 
m_cmsObject  .  lockResource  (  vfsFileName  )  ; 
} 
m_cmsObject  .  deleteResource  (  vfsFileName  ,  CmsResource  .  DELETE_PRESERVE_SIBLINGS  )  ; 
}  catch  (  CmsException   e  )  { 
}  finally  { 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_OVERWRITE_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
m_cmsObject  .  createResource  (  vfsFileName  ,  CmsResourceTypeXmlPage  .  getStaticTypeId  (  )  ,  contentByteArray  ,  new   ArrayList  (  )  )  ; 
} 
} 
Iterator   it  =  properties  .  entrySet  (  )  .  iterator  (  )  ; 
List   propertyList  =  new   ArrayList  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  it  .  next  (  )  ; 
String   propertyKey  =  (  String  )  entry  .  getKey  (  )  ; 
String   propertyVal  =  (  String  )  entry  .  getValue  (  )  ; 
CmsProperty   property  =  new   CmsProperty  (  propertyKey  ,  propertyVal  ,  propertyVal  )  ; 
property  .  setAutoCreatePropertyDefinition  (  true  )  ; 
propertyList  .  add  (  property  )  ; 
} 
try  { 
m_cmsObject  .  writePropertyObjects  (  vfsFileName  ,  propertyList  )  ; 
m_cmsObject  .  writePropertyObjects  (  vfsFileName  ,  oldProperties  )  ; 
}  catch  (  CmsException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
}  catch  (  CmsException   e  )  { 
m_report  .  println  (  e  )  ; 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
} 
} 
} 








private   void   createFolder  (  String   foldername  ,  int   position  ,  Hashtable   properties  )  { 
String   vfsFolderName  =  (  String  )  m_fileIndex  .  get  (  foldername  .  replace  (  '\\'  ,  '/'  )  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_CREATE_FOLDER_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  vfsFolderName  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
if  (  vfsFolderName  !=  null  )  { 
String   path  =  vfsFolderName  .  substring  (  0  ,  vfsFolderName  .  substring  (  0  ,  vfsFolderName  .  length  (  )  -  1  )  .  lastIndexOf  (  "/"  )  )  ; 
String   folder  =  vfsFolderName  .  substring  (  path  .  length  (  )  ,  vfsFolderName  .  length  (  )  )  ; 
try  { 
String   propertyFileName  =  foldername  +  File  .  separator  +  META_PROPERTIES  ; 
boolean   metaPropertiesFound  =  false  ; 
ExtendedProperties   propertyFile  =  new   ExtendedProperties  (  )  ; 
try  { 
propertyFile  .  load  (  new   FileInputStream  (  new   File  (  propertyFileName  )  )  )  ; 
metaPropertiesFound  =  true  ; 
}  catch  (  Exception   e1  )  { 
} 
if  (  metaPropertiesFound  )  { 
Enumeration   enu  =  propertyFile  .  keys  (  )  ; 
String   property  =  ""  ; 
while  (  enu  .  hasMoreElements  (  )  )  { 
try  { 
property  =  (  String  )  enu  .  nextElement  (  )  ; 
String   propertyvalue  =  (  String  )  propertyFile  .  get  (  property  )  ; 
properties  .  put  (  property  ,  propertyvalue  )  ; 
}  catch  (  Exception   e2  )  { 
e2  .  printStackTrace  (  )  ; 
} 
} 
if  (  properties  .  get  (  CmsPropertyDefinition  .  PROPERTY_NAVPOS  )  ==  null  )  { 
properties  .  put  (  CmsPropertyDefinition  .  PROPERTY_NAVPOS  ,  (  position  +  1  )  +  ""  )  ; 
} 
if  (  properties  .  get  (  CmsPropertyDefinition  .  PROPERTY_NAVTEXT  )  ==  null  )  { 
String   navtext  =  folder  .  substring  (  1  ,  2  )  .  toUpperCase  (  )  +  folder  .  substring  (  2  ,  folder  .  length  (  )  -  1  )  ; 
properties  .  put  (  CmsPropertyDefinition  .  PROPERTY_NAVTEXT  ,  navtext  )  ; 
} 
}  else  { 
properties  =  new   Hashtable  (  )  ; 
} 
try  { 
m_cmsObject  .  readFolder  (  path  +  folder  )  ; 
m_cmsObject  .  lockResource  (  path  +  folder  )  ; 
}  catch  (  CmsException   e1  )  { 
m_cmsObject  .  createResource  (  path  +  folder  ,  CmsResourceTypeFolder  .  getStaticTypeId  (  )  )  ; 
} 
Enumeration   enu  =  properties  .  keys  (  )  ; 
List   propertyList  =  new   ArrayList  (  )  ; 
while  (  enu  .  hasMoreElements  (  )  )  { 
String   propertyKey  =  (  String  )  enu  .  nextElement  (  )  ; 
String   propertyVal  =  (  String  )  properties  .  get  (  propertyKey  )  ; 
CmsProperty   property  =  new   CmsProperty  (  propertyKey  ,  propertyVal  ,  propertyVal  )  ; 
property  .  setAutoCreatePropertyDefinition  (  true  )  ; 
propertyList  .  add  (  property  )  ; 
} 
try  { 
m_cmsObject  .  writePropertyObjects  (  path  +  folder  ,  propertyList  )  ; 
}  catch  (  CmsException   e1  )  { 
e1  .  printStackTrace  (  )  ; 
} 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
}  catch  (  CmsException   e  )  { 
m_report  .  println  (  e  )  ; 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
} 
} 
} 









private   String   getBasePath  (  String   path1  ,  String   path2  )  { 
StringBuffer   base  =  new   StringBuffer  (  )  ; 
path1  =  path1  .  replace  (  '\\'  ,  '/'  )  ; 
path2  =  path2  .  replace  (  '\\'  ,  '/'  )  ; 
String  [  ]  parts1  =  path1  .  split  (  "/"  )  ; 
String  [  ]  parts2  =  path2  .  split  (  "/"  )  ; 
for  (  int   i  =  0  ;  i  <  parts1  .  length  ;  i  ++  )  { 
if  (  i  >=  parts2  .  length  )  { 
break  ; 
} 
if  (  parts1  [  i  ]  .  equals  (  parts2  [  i  ]  )  )  { 
base  .  append  (  parts1  [  i  ]  +  "/"  )  ; 
} 
} 
return   base  .  toString  (  )  ; 
} 








private   String   getExternalLinkFile  (  String   link  )  { 
String   filename  =  link  .  substring  (  link  .  indexOf  (  "://"  )  +  3  ,  link  .  length  (  )  )  ; 
filename  =  m_cmsObject  .  getRequestContext  (  )  .  getFileTranslator  (  )  .  translateResource  (  filename  .  replace  (  '/'  ,  '-'  )  )  ; 
return   filename  ; 
} 










private   byte  [  ]  getFileBytes  (  File   file  )  throws   CmsException  { 
byte  [  ]  buffer  =  null  ; 
FileInputStream   fileStream  =  null  ; 
int   charsRead  ; 
int   size  ; 
try  { 
fileStream  =  new   FileInputStream  (  file  )  ; 
charsRead  =  0  ; 
size  =  new   Long  (  file  .  length  (  )  )  .  intValue  (  )  ; 
buffer  =  new   byte  [  size  ]  ; 
while  (  charsRead  <  size  )  { 
charsRead  +=  fileStream  .  read  (  buffer  ,  charsRead  ,  size  -  charsRead  )  ; 
} 
return   buffer  ; 
}  catch  (  IOException   e  )  { 
throw   new   CmsDbIoException  (  Messages  .  get  (  )  .  container  (  Messages  .  ERR_GET_FILE_BYTES_1  ,  file  .  getAbsolutePath  (  )  )  ,  e  )  ; 
}  finally  { 
closeStream  (  fileStream  )  ; 
} 
} 











private   int   getFileType  (  String   filename  )  throws   Exception  { 
String   extension  =  ""  ; 
if  (  filename  .  indexOf  (  "."  )  >  -  1  )  { 
extension  =  filename  .  substring  (  (  filename  .  lastIndexOf  (  "."  )  )  )  ; 
} 
String   typename  =  (  String  )  m_extensions  .  get  (  extension  .  toLowerCase  (  )  )  ; 
if  (  typename  ==  null  )  { 
typename  =  "binary"  ; 
} 
CmsResourceManager   resourceManager  =  OpenCms  .  getResourceManager  (  )  ; 
return   resourceManager  .  getResourceType  (  typename  )  .  getTypeId  (  )  ; 
} 














private   String   getVfsName  (  String   relativeName  ,  String   name  ,  boolean   isFile  )  throws   Exception  { 
String   vfsName  =  relativeName  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  isFile  )  { 
int   filetype  =  getFileType  (  name  )  ; 
if  (  name  .  indexOf  (  "."  )  ==  0  )  { 
name  =  "unknown"  +  name  ; 
int   dot  =  relativeName  .  lastIndexOf  (  "."  )  ; 
relativeName  =  relativeName  .  substring  (  0  ,  dot  )  +  name  ; 
} 
boolean   leaveImages  =  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_imageGallery  )  ; 
boolean   leaveDownload  =  CmsStringUtil  .  isEmptyOrWhitespaceOnly  (  m_downloadGallery  )  ; 
if  (  (  CmsResourceTypeImage  .  getStaticTypeId  (  )  ==  filetype  )  &&  (  !  leaveImages  )  )  { 
vfsName  =  m_imageGallery  +  name  ; 
}  else   if  (  (  CmsResourceTypePlain  .  getStaticTypeId  (  )  ==  filetype  )  ||  (  leaveImages  )  ||  (  leaveDownload  )  )  { 
String   folderName  =  relativeName  ; 
if  (  folderName  .  indexOf  (  "."  )  >  0  )  { 
folderName  =  folderName  .  substring  (  0  ,  folderName  .  indexOf  (  "."  )  )  ; 
} 
folderName  =  m_inputDir  +  "\\"  +  folderName  ; 
File   folder  =  new   File  (  folderName  )  ; 
if  (  folder  .  isDirectory  (  )  )  { 
vfsName  =  m_destinationDir  +  relativeName  .  substring  (  0  ,  relativeName  .  indexOf  (  "."  )  )  +  "/index.html"  ; 
}  else  { 
vfsName  =  m_destinationDir  +  relativeName  ; 
} 
}  else  { 
vfsName  =  m_downloadGallery  +  name  ; 
} 
return   validateFilename  (  vfsName  )  ; 
}  else  { 
vfsName  =  m_destinationDir  +  vfsName  +  "/"  ; 
return   vfsName  ; 
} 
} 









private   boolean   isExternal  (  String   filename  )  { 
boolean   external  =  false  ; 
if  (  filename  .  indexOf  (  "://"  )  >  0  )  { 
external  =  true  ; 
} 
return   external  ; 
} 






private   boolean   isValidElement  (  )  { 
boolean   validElement  =  false  ; 
List   elementList  =  new   ArrayList  (  )  ; 
try  { 
String   elements  =  m_cmsObject  .  readPropertyObject  (  m_template  ,  CmsPropertyDefinition  .  PROPERTY_TEMPLATE_ELEMENTS  ,  false  )  .  getValue  (  )  ; 
if  (  elements  !=  null  )  { 
StringTokenizer   T  =  new   StringTokenizer  (  elements  ,  ","  )  ; 
while  (  T  .  hasMoreTokens  (  )  )  { 
String   currentElement  =  T  .  nextToken  (  )  ; 
int   sepIndex  =  currentElement  .  indexOf  (  "|"  )  ; 
if  (  sepIndex  !=  -  1  )  { 
currentElement  =  currentElement  .  substring  (  0  ,  sepIndex  )  ; 
} 
if  (  currentElement  .  endsWith  (  "*"  )  )  { 
currentElement  =  currentElement  .  substring  (  0  ,  currentElement  .  length  (  )  -  1  )  ; 
} 
elementList  .  add  (  currentElement  )  ; 
} 
} 
if  (  elementList  .  contains  (  m_element  )  )  { 
validElement  =  true  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   validElement  ; 
} 












private   String   parseHtmlFile  (  File   file  ,  Hashtable   properties  )  throws   CmsException  { 
String   parsedHtml  =  ""  ; 
try  { 
byte  [  ]  content  =  getFileBytes  (  file  )  ; 
String   contentString  =  new   String  (  content  ,  m_inputEncoding  )  ; 
contentString  =  CmsEncoder  .  escapeNonAscii  (  contentString  )  ; 
contentString  =  CmsStringUtil  .  substitute  (  contentString  ,  "&#"  ,  "{subst}"  )  ; 
parsedHtml  =  m_htmlConverter  .  convertHTML  (  file  .  getAbsolutePath  (  )  ,  contentString  ,  m_startPattern  ,  m_endPattern  ,  properties  )  ; 
parsedHtml  =  CmsStringUtil  .  substitute  (  parsedHtml  ,  "{subst}"  ,  "&#"  )  ; 
}  catch  (  Exception   e  )  { 
CmsMessageContainer   message  =  Messages  .  get  (  )  .  container  (  Messages  .  ERR_HTMLIMPORT_PARSE_1  ,  file  .  getAbsolutePath  (  )  )  ; 
LOG  .  error  (  e  .  getLocalizedMessage  (  )  ,  e  )  ; 
throw   new   CmsImportExportException  (  message  ,  e  )  ; 
} 
return   parsedHtml  ; 
} 







private   File   unzipStream  (  )  { 
ZipInputStream   importZip  =  null  ; 
File   folder  =  null  ; 
try  { 
importZip  =  new   ZipInputStream  (  new   FileInputStream  (  m_httpDir  )  )  ; 
folder  =  createTempFolder  (  "import_html"  )  ; 
ZipEntry   entry  =  null  ; 
byte  [  ]  buffer  =  null  ; 
while  (  true  )  { 
try  { 
entry  =  importZip  .  getNextEntry  (  )  ; 
if  (  entry  ==  null  )  { 
break  ; 
} 
String   name  =  entry  .  getName  (  )  ; 
m_report  .  print  (  Messages  .  get  (  )  .  container  (  Messages  .  RPT_HTML_UNZIP_0  )  ,  I_CmsReport  .  FORMAT_NOTE  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_ARGUMENT_1  ,  name  )  )  ; 
m_report  .  print  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_DOTS_0  )  )  ; 
name  =  name  .  replace  (  '/'  ,  File  .  separatorChar  )  ; 
String   path  =  folder  +  File  .  separator  +  name  ; 
if  (  entry  .  isDirectory  (  )  )  { 
File   importFile  =  new   File  (  path  )  ; 
importFile  .  mkdirs  (  )  ; 
}  else  { 
int   size  =  new   Long  (  entry  .  getSize  (  )  )  .  intValue  (  )  ; 
if  (  size  ==  -  1  )  { 
buffer  =  CmsFileUtil  .  readFully  (  importZip  ,  false  )  ; 
}  else  { 
buffer  =  CmsFileUtil  .  readFully  (  importZip  ,  size  ,  false  )  ; 
} 
File   importFile  =  new   File  (  path  )  ; 
File   parent  =  importFile  .  getParentFile  (  )  ; 
if  (  parent  !=  null  )  { 
parent  .  mkdirs  (  )  ; 
} 
importFile  .  createNewFile  (  )  ; 
FileOutputStream   fileOutput  =  new   FileOutputStream  (  importFile  .  getAbsoluteFile  (  )  )  ; 
fileOutput  .  write  (  buffer  )  ; 
fileOutput  .  close  (  )  ; 
} 
importZip  .  closeEntry  (  )  ; 
m_report  .  println  (  org  .  opencms  .  report  .  Messages  .  get  (  )  .  container  (  org  .  opencms  .  report  .  Messages  .  RPT_OK_0  )  ,  I_CmsReport  .  FORMAT_OK  )  ; 
}  catch  (  Exception   ex  )  { 
String   name  =  (  entry  !=  null  ?  entry  .  getName  (  )  :  ""  )  ; 
if  (  LOG  .  isErrorEnabled  (  )  )  { 
LOG  .  error  (  Messages  .  get  (  )  .  getBundle  (  )  .  key  (  Messages  .  ERR_ZIPFILE_UNZIP_1  ,  name  )  ,  ex  )  ; 
} 
m_report  .  println  (  Messages  .  get  (  )  .  container  (  Messages  .  ERR_ZIPFILE_UNZIP_1  ,  name  )  ,  I_CmsReport  .  FORMAT_ERROR  )  ; 
} 
entry  =  null  ; 
} 
}  catch  (  Exception   ex  )  { 
if  (  LOG  .  isErrorEnabled  (  )  )  { 
LOG  .  error  (  Messages  .  get  (  )  .  getBundle  (  )  .  key  (  Messages  .  ERR_ZIPFILE_READ_1  ,  m_httpDir  )  ,  ex  )  ; 
} 
m_report  .  println  (  Messages  .  get  (  )  .  container  (  Messages  .  ERR_ZIPFILE_READ_1  ,  m_httpDir  )  ,  I_CmsReport  .  FORMAT_ERROR  )  ; 
}  finally  { 
closeStream  (  importZip  )  ; 
} 
return   folder  ; 
} 











private   String   validateFilename  (  String   filename  )  { 
if  (  isExternal  (  filename  )  )  { 
return   filename  ; 
} 
int   postfix  =  1  ; 
boolean   found  =  true  ; 
String   validFilename  =  filename  ; 
if  (  !  m_overwrite  )  { 
while  (  found  )  { 
try  { 
validFilename  =  m_cmsObject  .  getRequestContext  (  )  .  getFileTranslator  (  )  .  translateResource  (  validFilename  )  ; 
found  =  true  ; 
if  (  !  m_fileIndex  .  containsValue  (  validFilename  .  replace  (  '\\'  ,  '/'  )  )  )  { 
found  =  false  ; 
} 
if  (  !  found  )  { 
found  =  true  ; 
m_cmsObject  .  readResource  (  validFilename  ,  CmsResourceFilter  .  ALL  )  ; 
} 
String   path  =  filename  .  substring  (  0  ,  filename  .  lastIndexOf  (  "/"  )  +  1  )  ; 
String   name  =  filename  .  substring  (  filename  .  lastIndexOf  (  "/"  )  +  1  ,  filename  .  length  (  )  )  ; 
validFilename  =  path  ; 
if  (  name  .  lastIndexOf  (  "."  )  >  0  )  { 
validFilename  +=  name  .  substring  (  0  ,  name  .  lastIndexOf  (  "."  )  )  ; 
}  else  { 
validFilename  +=  name  ; 
} 
validFilename  +=  "_"  +  postfix  ; 
if  (  name  .  lastIndexOf  (  "."  )  >  0  )  { 
validFilename  +=  name  .  substring  (  name  .  lastIndexOf  (  "."  )  ,  name  .  length  (  )  )  ; 
} 
postfix  ++  ; 
}  catch  (  CmsException   e  )  { 
found  =  false  ; 
} 
} 
}  else  { 
validFilename  =  validFilename  .  replace  (  '\\'  ,  '/'  )  ; 
} 
return   OpenCms  .  getResourceManager  (  )  .  getFileTranslator  (  )  .  translateResource  (  validFilename  )  ; 
} 
} 

