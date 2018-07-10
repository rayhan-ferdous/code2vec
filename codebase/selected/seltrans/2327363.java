package   utils  ; 

import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  logging  .  ConsoleHandler  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  apache  .  commons  .  io  .  IOUtils  ; 
import   com  .  google  .  gdata  .  client  .  GoogleService  ; 
import   com  .  google  .  gdata  .  client  .  Query  ; 
import   com  .  google  .  gdata  .  client  .  docs  .  DocsService  ; 
import   com  .  google  .  gdata  .  data  .  MediaContent  ; 
import   com  .  google  .  gdata  .  data  .  PlainTextConstruct  ; 
import   com  .  google  .  gdata  .  data  .  acl  .  AclEntry  ; 
import   com  .  google  .  gdata  .  data  .  acl  .  AclFeed  ; 
import   com  .  google  .  gdata  .  data  .  acl  .  AclRole  ; 
import   com  .  google  .  gdata  .  data  .  acl  .  AclScope  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  DocumentEntry  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  DocumentListEntry  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  DocumentListFeed  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  FolderEntry  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  PresentationEntry  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  RevisionFeed  ; 
import   com  .  google  .  gdata  .  data  .  docs  .  SpreadsheetEntry  ; 
import   com  .  google  .  gdata  .  data  .  media  .  MediaSource  ; 
import   com  .  google  .  gdata  .  data  .  media  .  MediaStreamSource  ; 
import   com  .  google  .  gdata  .  util  .  AuthenticationException  ; 
import   com  .  google  .  gdata  .  util  .  ServiceException  ; 

public   class   GoogleDocService  { 

public   DocsService   service  ; 

public   GoogleService   spreadsheetsService  ; 

public   static   final   String   DEFAULT_AUTH_PROTOCOL  =  "https"  ; 

public   static   final   String   DEFAULT_AUTH_HOST  =  "docs.google.com"  ; 

public   static   final   String   DEFAULT_PROTOCOL  =  "http"  ; 

public   static   final   String   DEFAULT_HOST  =  "docs.google.com"  ; 

public   static   final   String   SPREADSHEETS_SERVICE_NAME  =  "wise"  ; 

public   static   final   String   SPREADSHEETS_HOST  =  "spreadsheets.google.com"  ; 

private   final   String   URL_FEED  =  "/feeds"  ; 

private   final   String   URL_DOWNLOAD  =  "/download"  ; 

private   final   String   URL_DOCLIST_FEED  =  "/private/full"  ; 

private   final   String   URL_DEFAULT  =  "/default"  ; 

private   final   String   URL_FOLDERS  =  "/contents"  ; 

private   final   String   URL_ACL  =  "/acl"  ; 

private   final   String   URL_REVISIONS  =  "/revisions"  ; 

private   final   String   URL_CATEGORY_DOCUMENT  =  "/-/document"  ; 

private   final   String   URL_CATEGORY_SPREADSHEET  =  "/-/spreadsheet"  ; 

private   final   String   URL_CATEGORY_PDF  =  "/-/pdf"  ; 

private   final   String   URL_CATEGORY_PRESENTATION  =  "/-/presentation"  ; 

private   final   String   URL_CATEGORY_STARRED  =  "/-/starred"  ; 

private   final   String   URL_CATEGORY_TRASHED  =  "/-/trashed"  ; 

private   final   String   URL_CATEGORY_FOLDER  =  "/-/folder"  ; 

private   final   String   URL_CATEGORY_EXPORT  =  "/Export"  ; 

private   final   String   PARAMETER_SHOW_FOLDERS  =  "showfolders=true"  ; 

private   String   applicationName  ; 

private   String   authProtocol  ; 

private   String   authHost  ; 

private   String   protocol  ; 

private   String   host  ; 

private   String   username  ; 

private   String   password  ; 

private   String   authSubToken  ; 

private   final   Map  <  String  ,  String  >  DOWNLOAD_DOCUMENT_FORMATS  ; 

{ 
DOWNLOAD_DOCUMENT_FORMATS  =  new   HashMap  <  String  ,  String  >  (  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "doc"  ,  "doc"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "txt"  ,  "txt"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "odt"  ,  "odt"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "pdf"  ,  "pdf"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "png"  ,  "png"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "rtf"  ,  "rtf"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "html"  ,  "html"  )  ; 
DOWNLOAD_DOCUMENT_FORMATS  .  put  (  "zip"  ,  "zip"  )  ; 
} 

private   final   Map  <  String  ,  String  >  DOWNLOAD_PRESENTATION_FORMATS  ; 

{ 
DOWNLOAD_PRESENTATION_FORMATS  =  new   HashMap  <  String  ,  String  >  (  )  ; 
DOWNLOAD_PRESENTATION_FORMATS  .  put  (  "pdf"  ,  "pdf"  )  ; 
DOWNLOAD_PRESENTATION_FORMATS  .  put  (  "png"  ,  "png"  )  ; 
DOWNLOAD_PRESENTATION_FORMATS  .  put  (  "ppt"  ,  "ppt"  )  ; 
DOWNLOAD_PRESENTATION_FORMATS  .  put  (  "swf"  ,  "swf"  )  ; 
DOWNLOAD_PRESENTATION_FORMATS  .  put  (  "txt"  ,  "txt"  )  ; 
} 

private   final   Map  <  String  ,  String  >  DOWNLOAD_SPREADSHEET_FORMATS  ; 

{ 
DOWNLOAD_SPREADSHEET_FORMATS  =  new   HashMap  <  String  ,  String  >  (  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "xls"  ,  "xls"  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "ods"  ,  "ods"  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "pdf"  ,  "pdf"  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "csv"  ,  "csv"  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "tsv"  ,  "tsv"  )  ; 
DOWNLOAD_SPREADSHEET_FORMATS  .  put  (  "html"  ,  "html"  )  ; 
} 








public   GoogleDocService  (  String   applicationName  )  throws   Exception  { 
this  (  applicationName  ,  DEFAULT_AUTH_PROTOCOL  ,  DEFAULT_AUTH_HOST  ,  DEFAULT_PROTOCOL  ,  DEFAULT_HOST  )  ; 
} 












public   GoogleDocService  (  String   applicationName  ,  String   authProtocol  ,  String   authHost  ,  String   protocol  ,  String   host  )  throws   RuntimeException  { 
if  (  authProtocol  ==  null  ||  authHost  ==  null  ||  protocol  ==  null  ||  host  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in required parameters"  )  ; 
} 
service  =  new   DocsService  (  applicationName  )  ; 
spreadsheetsService  =  new   GoogleService  (  SPREADSHEETS_SERVICE_NAME  ,  applicationName  )  ; 
this  .  applicationName  =  applicationName  ; 
this  .  authProtocol  =  authProtocol  ; 
this  .  authHost  =  authHost  ; 
this  .  protocol  =  protocol  ; 
this  .  host  =  host  ; 
} 










public   void   login  (  String   user  ,  String   pass  )  throws   AuthenticationException  ,  RuntimeException  { 
if  (  user  ==  null  ||  pass  ==  null  )  { 
throw   new   RuntimeException  (  "null login credentials"  )  ; 
} 
this  .  username  =  user  ; 
this  .  password  =  pass  ; 
this  .  authSubToken  =  ""  ; 
service  .  setUserCredentials  (  user  ,  pass  )  ; 
spreadsheetsService  .  setUserCredentials  (  user  ,  pass  )  ; 
} 









public   void   loginWithAuthSubToken  (  String   token  )  throws   AuthenticationException  ,  RuntimeException  { 
if  (  token  ==  null  )  { 
throw   new   RuntimeException  (  "null login credentials"  )  ; 
} 
this  .  authSubToken  =  token  ; 
this  .  username  =  ""  ; 
this  .  password  =  ""  ; 
service  .  setAuthSubToken  (  token  )  ; 
spreadsheetsService  .  setAuthSubToken  (  token  )  ; 
} 












public   DocumentListEntry   createNew  (  String   title  ,  String   type  )  throws   Exception  { 
if  (  title  ==  null  ||  type  ==  null  )  { 
throw   new   RuntimeException  (  "null title or type"  )  ; 
} 
DocumentListEntry   newEntry  =  null  ; 
if  (  type  .  equals  (  "document"  )  )  { 
newEntry  =  new   DocumentEntry  (  )  ; 
}  else   if  (  type  .  equals  (  "presentation"  )  )  { 
newEntry  =  new   PresentationEntry  (  )  ; 
}  else   if  (  type  .  equals  (  "spreadsheet"  )  )  { 
newEntry  =  new   SpreadsheetEntry  (  )  ; 
}  else   if  (  type  .  equals  (  "folder"  )  )  { 
newEntry  =  new   FolderEntry  (  )  ; 
} 
newEntry  .  setTitle  (  new   PlainTextConstruct  (  title  )  )  ; 
return   service  .  insert  (  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  )  ,  newEntry  )  ; 
} 













public   DocumentListFeed   getDocsListFeed  (  String   category  )  throws   Exception  { 
if  (  category  ==  null  )  { 
throw   new   RuntimeException  (  "null category"  )  ; 
} 
URL   url  ; 
if  (  category  .  equals  (  "all"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  )  ; 
}  else   if  (  category  .  equals  (  "folders"  )  )  { 
String  [  ]  parameters  =  {  PARAMETER_SHOW_FOLDERS  }  ; 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_FOLDER  ,  parameters  )  ; 
}  else   if  (  category  .  equals  (  "documents"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_DOCUMENT  )  ; 
}  else   if  (  category  .  equals  (  "spreadsheets"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_SPREADSHEET  )  ; 
}  else   if  (  category  .  equals  (  "pdfs"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_PDF  )  ; 
}  else   if  (  category  .  equals  (  "presentations"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_PRESENTATION  )  ; 
}  else   if  (  category  .  equals  (  "starred"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_STARRED  )  ; 
}  else   if  (  category  .  equals  (  "trashed"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_TRASHED  )  ; 
}  else  { 
return   null  ; 
} 
return   service  .  getFeed  (  url  ,  DocumentListFeed  .  class  )  ; 
} 











public   DocumentListEntry   getDocsListEntry  (  String   resourceId  )  throws   Exception  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  )  ; 
return   service  .  getEntry  (  url  ,  DocumentListEntry  .  class  )  ; 
} 











public   DocumentListFeed   getFolderDocsListFeed  (  String   folderResourceId  )  throws   Exception  { 
if  (  folderResourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null folderResourceId"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  folderResourceId  +  URL_FOLDERS  )  ; 
return   service  .  getFeed  (  url  ,  DocumentListFeed  .  class  )  ; 
} 











public   RevisionFeed   getRevisionsFeed  (  String   resourceId  )  throws   Exception  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  +  URL_REVISIONS  )  ; 
return   service  .  getFeed  (  url  ,  RevisionFeed  .  class  )  ; 
} 











public   DocumentListFeed   search  (  Map  <  String  ,  String  >  searchParameters  )  throws   Exception  { 
return   search  (  searchParameters  ,  null  )  ; 
} 





















public   DocumentListFeed   search  (  Map  <  String  ,  String  >  searchParameters  ,  String   category  )  throws   Exception  { 
if  (  searchParameters  ==  null  )  { 
throw   new   RuntimeException  (  "searchParameters null"  )  ; 
} 
URL   url  ; 
if  (  category  ==  null  ||  category  .  equals  (  ""  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  )  ; 
}  else   if  (  category  .  equals  (  "documents"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_DOCUMENT  )  ; 
}  else   if  (  category  .  equals  (  "spreadsheets"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_SPREADSHEET  )  ; 
}  else   if  (  category  .  equals  (  "presentations"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_PRESENTATION  )  ; 
}  else   if  (  category  .  equals  (  "starred"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_STARRED  )  ; 
}  else   if  (  category  .  equals  (  "trashed"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_TRASHED  )  ; 
}  else   if  (  category  .  equals  (  "folders"  )  )  { 
url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  URL_CATEGORY_FOLDER  )  ; 
}  else  { 
throw   new   RuntimeException  (  "invaild category"  )  ; 
} 
Query   qry  =  new   Query  (  url  )  ; 
for  (  String   key  :  searchParameters  .  keySet  (  )  )  { 
qry  .  setStringCustomParameter  (  key  ,  searchParameters  .  get  (  key  )  )  ; 
} 
return   service  .  query  (  qry  ,  DocumentListFeed  .  class  )  ; 
} 

public   DocumentListEntry   upload  (  InputStream   input  ,  String   mediaType  ,  String   title  )  throws   Exception  ,  ServiceException  ,  RuntimeException  { 
if  (  input  ==  null  ||  title  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
DocumentEntry   newDocument  =  new   DocumentEntry  (  )  ; 
MediaSource   source  =  new   MediaStreamSource  (  input  ,  mediaType  )  ; 
newDocument  .  setMediaSource  (  source  )  ; 
newDocument  .  setTitle  (  new   PlainTextConstruct  (  title  )  )  ; 
return   service  .  insert  (  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  )  ,  newDocument  )  ; 
} 












public   void   trashObject  (  String   resourceId  ,  boolean   delete  )  throws   Exception  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
String   feedUrl  =  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  ; 
if  (  delete  )  { 
feedUrl  +=  "?delete=true"  ; 
} 
service  .  delete  (  buildUrl  (  feedUrl  )  ,  getDocsListEntry  (  resourceId  )  .  getEtag  (  )  )  ; 
} 












public   void   removeFromFolder  (  String   resourceId  ,  String   folderResourceId  )  throws   Exception  { 
if  (  resourceId  ==  null  ||  folderResourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  folderResourceId  +  URL_FOLDERS  +  "/"  +  resourceId  )  ; 
service  .  delete  (  url  ,  getDocsListEntry  (  resourceId  )  .  getEtag  (  )  )  ; 
} 

public   void   write  (  URL   exportUrl  ,  OutputStream   output  )  throws   Exception  { 
if  (  exportUrl  ==  null  ||  output  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
MediaContent   mc  =  new   MediaContent  (  )  ; 
mc  .  setUri  (  exportUrl  .  toString  (  )  )  ; 
MediaSource   ms  =  service  .  getMedia  (  mc  )  ; 
InputStream   input  =  ms  .  getInputStream  (  )  ; 
IOUtils  .  copy  (  input  ,  output  )  ; 
} 

public   void   downloadPresentation  (  String   resourceId  ,  OutputStream   outputStream  ,  String   format  )  throws   Exception  { 
if  (  resourceId  ==  null  ||  outputStream  ==  null  ||  format  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
String  [  ]  parameters  =  {  "docID="  +  resourceId  ,  "exportFormat="  +  format  }  ; 
URL   url  =  buildUrl  (  URL_DOWNLOAD  +  "/presentations"  +  URL_CATEGORY_EXPORT  ,  parameters  )  ; 
write  (  url  ,  outputStream  )  ; 
} 












public   DocumentListEntry   moveObjectToFolder  (  String   resourceId  ,  String   folderId  )  throws   Exception  { 
if  (  resourceId  ==  null  ||  folderId  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
DocumentListEntry   doc  =  new   DocumentListEntry  (  )  ; 
doc  .  setId  (  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  )  .  toString  (  )  )  ; 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  folderId  +  URL_FOLDERS  )  ; 
return   service  .  insert  (  url  ,  doc  )  ; 
} 











public   AclFeed   getAclFeed  (  String   resourceId  )  throws   Exception  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  +  URL_ACL  )  ; 
return   service  .  getFeed  (  url  ,  AclFeed  .  class  )  ; 
} 













public   AclEntry   addAclRole  (  AclRole   role  ,  AclScope   scope  ,  String   resourceId  )  throws   Exception  { 
if  (  role  ==  null  ||  scope  ==  null  ||  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
AclEntry   entry  =  new   AclEntry  (  )  ; 
entry  .  setRole  (  role  )  ; 
entry  .  setScope  (  scope  )  ; 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  +  URL_ACL  )  ; 
return   service  .  insert  (  url  ,  entry  )  ; 
} 













public   AclEntry   changeAclRole  (  AclRole   role  ,  AclScope   scope  ,  String   resourceId  )  throws   Exception  { 
if  (  role  ==  null  ||  scope  ==  null  ||  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  +  URL_ACL  )  ; 
return   service  .  update  (  url  ,  scope  ,  role  )  ; 
} 













public   void   removeAclRole  (  String   scope  ,  String   email  ,  String   resourceId  )  throws   Exception  { 
if  (  scope  ==  null  ||  email  ==  null  ||  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
URL   url  =  buildUrl  (  URL_DEFAULT  +  URL_DOCLIST_FEED  +  "/"  +  resourceId  +  URL_ACL  +  "/"  +  scope  +  "%3A"  +  email  )  ; 
service  .  delete  (  url  )  ; 
} 









public   String   getDownloadFormat  (  String   resourceId  ,  String   ext  )  throws   RuntimeException  { 
if  (  resourceId  ==  null  ||  ext  ==  null  )  { 
throw   new   RuntimeException  (  "null passed in for required parameters"  )  ; 
} 
if  (  resourceId  .  indexOf  (  "document"  )  ==  0  )  { 
if  (  DOWNLOAD_DOCUMENT_FORMATS  .  containsKey  (  ext  )  )  { 
return   DOWNLOAD_DOCUMENT_FORMATS  .  get  (  ext  )  ; 
} 
}  else   if  (  resourceId  .  indexOf  (  "presentation"  )  ==  0  )  { 
if  (  DOWNLOAD_PRESENTATION_FORMATS  .  containsKey  (  ext  )  )  { 
return   DOWNLOAD_PRESENTATION_FORMATS  .  get  (  ext  )  ; 
} 
}  else   if  (  resourceId  .  indexOf  (  "spreadsheet"  )  ==  0  )  { 
if  (  DOWNLOAD_SPREADSHEET_FORMATS  .  containsKey  (  ext  )  )  { 
return   DOWNLOAD_SPREADSHEET_FORMATS  .  get  (  ext  )  ; 
} 
} 
throw   new   RuntimeException  (  "invalid document type"  )  ; 
} 








public   String   getResourceIdSuffix  (  String   resourceId  )  throws   RuntimeException  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
if  (  resourceId  .  indexOf  (  "%3A"  )  !=  -  1  )  { 
return   resourceId  .  substring  (  resourceId  .  lastIndexOf  (  "%3A"  )  +  3  )  ; 
}  else   if  (  resourceId  .  indexOf  (  ":"  )  !=  -  1  )  { 
return   resourceId  .  substring  (  resourceId  .  lastIndexOf  (  ":"  )  +  1  )  ; 
} 
throw   new   RuntimeException  (  "Bad resourceId"  )  ; 
} 








public   String   getResourceIdPrefix  (  String   resourceId  )  throws   RuntimeException  { 
if  (  resourceId  ==  null  )  { 
throw   new   RuntimeException  (  "null resourceId"  )  ; 
} 
if  (  resourceId  .  indexOf  (  "%3A"  )  !=  -  1  )  { 
return   resourceId  .  substring  (  0  ,  resourceId  .  indexOf  (  "%3A"  )  )  ; 
}  else   if  (  resourceId  .  indexOf  (  ":"  )  !=  -  1  )  { 
return   resourceId  .  substring  (  0  ,  resourceId  .  indexOf  (  ":"  )  )  ; 
}  else  { 
throw   new   RuntimeException  (  "Bad resourceId"  )  ; 
} 
} 









private   URL   buildUrl  (  String   path  )  throws   MalformedURLException  ,  RuntimeException  { 
if  (  path  ==  null  )  { 
throw   new   RuntimeException  (  "null path"  )  ; 
} 
return   buildUrl  (  path  ,  null  )  ; 
} 










private   URL   buildUrl  (  String   path  ,  String  [  ]  parameters  )  throws   MalformedURLException  ,  RuntimeException  { 
if  (  path  ==  null  )  { 
throw   new   RuntimeException  (  "null path"  )  ; 
} 
return   buildUrl  (  host  ,  path  ,  parameters  )  ; 
} 











private   URL   buildUrl  (  String   domain  ,  String   path  ,  String  [  ]  parameters  )  throws   MalformedURLException  ,  RuntimeException  { 
if  (  path  ==  null  )  { 
throw   new   RuntimeException  (  "null path"  )  ; 
} 
StringBuffer   url  =  new   StringBuffer  (  )  ; 
url  .  append  (  protocol  +  "://"  +  domain  +  URL_FEED  +  path  )  ; 
if  (  parameters  !=  null  &&  parameters  .  length  >  0  )  { 
url  .  append  (  "?"  )  ; 
for  (  int   i  =  0  ;  i  <  parameters  .  length  ;  i  ++  )  { 
url  .  append  (  parameters  [  i  ]  )  ; 
if  (  i  !=  (  parameters  .  length  -  1  )  )  { 
url  .  append  (  "&"  )  ; 
} 
} 
} 
return   new   URL  (  url  .  toString  (  )  )  ; 
} 











private   URL   buildUrl  (  String   domain  ,  String   path  ,  Map  <  String  ,  String  >  parameters  )  throws   MalformedURLException  ,  RuntimeException  { 
if  (  path  ==  null  )  { 
throw   new   RuntimeException  (  "null path"  )  ; 
} 
StringBuffer   url  =  new   StringBuffer  (  )  ; 
url  .  append  (  protocol  +  "://"  +  domain  +  URL_FEED  +  path  )  ; 
if  (  parameters  !=  null  &&  parameters  .  size  (  )  >  0  )  { 
Set  <  Map  .  Entry  <  String  ,  String  >  >  params  =  parameters  .  entrySet  (  )  ; 
Iterator  <  Map  .  Entry  <  String  ,  String  >  >  itr  =  params  .  iterator  (  )  ; 
url  .  append  (  "?"  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
Map  .  Entry  <  String  ,  String  >  entry  =  itr  .  next  (  )  ; 
url  .  append  (  entry  .  getKey  (  )  +  "="  +  entry  .  getValue  (  )  )  ; 
if  (  itr  .  hasNext  (  )  )  { 
url  .  append  (  "&"  )  ; 
} 
} 
} 
return   new   URL  (  url  .  toString  (  )  )  ; 
} 

public   static   void   turnOnLogging  (  )  { 
Logger   httpLogger  =  Logger  .  getLogger  (  "com.google.gdata.client.http.HttpGDataRequest"  )  ; 
httpLogger  .  setLevel  (  Level  .  ALL  )  ; 
Logger   xmlLogger  =  Logger  .  getLogger  (  "com.google.gdata.util.XmlParser"  )  ; 
xmlLogger  .  setLevel  (  Level  .  ALL  )  ; 
ConsoleHandler   logHandler  =  new   ConsoleHandler  (  )  ; 
logHandler  .  setLevel  (  Level  .  ALL  )  ; 
httpLogger  .  addHandler  (  logHandler  )  ; 
xmlLogger  .  addHandler  (  logHandler  )  ; 
} 
} 

