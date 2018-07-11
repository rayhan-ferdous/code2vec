package   ces  .  platform  .  infoplat  .  core  ; 

import   java  .  io  .  File  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  List  ; 
import   ces  .  coral  .  lang  .  StringUtil  ; 
import   ces  .  platform  .  infoplat  .  core  .  base  .  Const  ; 
import   ces  .  platform  .  infoplat  .  core  .  dao  .  DocumentCBFDAO  ; 
import   ces  .  platform  .  infoplat  .  core  .  dao  .  DocumentPublishDAO  ; 
import   ces  .  platform  .  infoplat  .  core  .  tree  .  TreeNode  ; 
import   ces  .  platform  .  infoplat  .  service  .  indexserver  .  parser  .  html  .  HtmlParser  ; 
import   ces  .  platform  .  infoplat  .  utils  .  Function  ; 
import   ces  .  platform  .  system  .  facade  .  OrgUser  ; 


















public   class   DocumentPublish   extends   Document  { 

private   int   publisher  ; 

private   String   publisherName  ; 

private   Date   publishDate  ; 

private   int   orderNo  ; 

private   Date   validStartDate  ; 

private   Date   validEndDate  ; 

private   boolean   synStatus  =  false  ; 

private   String   siteAsciiName  ; 

private   String   channelAsciiName  ; 

private   String   docTypeName  ; 

private   String   published_channel_count  ; 

private   String   published_channel  ; 

private   String   full_path  ; 

public   DocumentPublish  (  )  { 
} 






public   DocumentPublish  (  DocumentCBF   doc  ,  String   publishPath  ,  int   publisher  ,  Date   publishDate  ,  Date   validStartDate  ,  Date   validEndDate  ,  int   orderNo  )  { 
this  .  id  =  doc  .  id  ; 
this  .  abstractWords  =  doc  .  getAbstractWords  (  )  ; 
this  .  actyInstId  =  doc  .  getActyInstId  (  )  ; 
this  .  attachStatus  =  doc  .  getAttachStatus  (  )  ; 
this  .  author  =  doc  .  getAuthor  (  )  ; 
this  .  channelPath  =  publishPath  ; 
this  .  contentFile  =  doc  .  getContentFile  (  )  ; 
this  .  createDate  =  doc  .  getCreateDate  (  )  ; 
this  .  creater  =  doc  .  getCreater  (  )  ; 
this  .  doctypePath  =  doc  .  getDoctypePath  (  )  ; 
this  .  editorRemark  =  doc  .  getEditorRemark  (  )  ; 
this  .  emitDate  =  doc  .  getEmitDate  (  )  ; 
this  .  emitUnit  =  doc  .  getEmitUnit  (  )  ; 
this  .  hyperlink  =  doc  .  getHyperlink  (  )  ; 
this  .  keywords  =  doc  .  getKeywords  (  )  ; 
this  .  notes  =  doc  .  getNotes  (  )  ; 
this  .  orderNo  =  orderNo  ; 
this  .  periodicalNo  =  doc  .  getPeriodicalNo  (  )  ; 
this  .  pertinentWords  =  doc  .  getPertinentWords  (  )  ; 
this  .  publishDate  =  publishDate  ; 
this  .  publisher  =  publisher  ; 
this  .  remarkProp  =  doc  .  getRemarkProp  (  )  ; 
this  .  remarkPropName  =  doc  .  getRemarkPropName  (  )  ; 
this  .  reservation1  =  doc  .  getReservation1  (  )  ; 
this  .  reservation2  =  doc  .  getReservation2  (  )  ; 
this  .  reservation3  =  doc  .  getReservation3  (  )  ; 
this  .  reservation4  =  doc  .  getReservation4  (  )  ; 
this  .  reservation5  =  doc  .  getReservation5  (  )  ; 
this  .  reservation6  =  doc  .  getReservation6  (  )  ; 
this  .  securityLevelId  =  doc  .  getSecurityLevelId  (  )  ; 
this  .  securityLevelName  =  doc  .  getSecurityLevelName  (  )  ; 
this  .  sourceId  =  doc  .  getSourceId  (  )  ; 
this  .  subTitle  =  doc  .  getSubTitle  (  )  ; 
this  .  title  =  doc  .  getTitle  (  )  ; 
this  .  titleColor  =  doc  .  getTitleColor  (  )  ; 
this  .  validEndDate  =  validEndDate  ; 
this  .  validStartDate  =  validStartDate  ; 
this  .  wordNo  =  doc  .  getWordNo  (  )  ; 
this  .  yearNo  =  doc  .  getYearNo  (  )  ; 
this  .  createrName  =  doc  .  getCreaterName  (  )  ; 
} 









public   static   DocumentPublish   getInstance  (  String   channelPath  ,  int   docId  )  throws   Exception  { 
return   new   DocumentPublishDAO  (  )  .  getInstance  (  docId  ,  channelPath  )  ; 
} 




public   void   add  (  )  throws   Exception  { 
new   DocumentPublishDAO  (  )  .  add  (  this  )  ; 
} 






public   String   getContentLength  (  )  { 
String   docContent  =  ""  ; 
String   strFileName  =  ""  ; 
File   file  =  null  ; 
if  (  strFileName  .  equals  (  ""  )  )  { 
strFileName  =  new   DocumentPublishDAO  (  )  .  getContentName  (  this  )  ; 
} 
try  { 
file  =  new   File  (  strFileName  )  ; 
if  (  file  .  exists  (  )  )  { 
HtmlParser   parser  =  new   HtmlParser  (  )  ; 
docContent  =  parser  .  getFileContent  (  strFileName  )  ; 
if  (  docContent  ==  null  )  { 
docContent  =  ""  ; 
} 
} 
docContent  =  StringUtil  .  replaceAll  (  docContent  ,  " "  ,  ""  )  ; 
return  ""  +  docContent  .  length  (  )  ; 
}  catch  (  Exception   ex3  )  { 
log  .  error  (  "�����ļ���ֵ"  ,  ex3  )  ; 
return   null  ; 
} 
} 




public   void   delete  (  )  throws   Exception  { 
new   DocumentPublishDAO  (  )  .  delete  (  this  )  ; 
} 




public   DocAffix  [  ]  getAffixList  (  )  throws   Exception  { 
return   new   DocumentCBFDAO  (  )  .  getAffixList  (  this  .  id  )  ; 
} 






public   DocAffix   getAffix  (  int   affixId  )  throws   Exception  { 
return  (  DocAffix  )  DocResource  .  getInstance  (  affixId  )  ; 
} 




public   DocPicture  [  ]  getPictureList  (  )  throws   Exception  { 
return   new   DocumentCBFDAO  (  )  .  getPictureList  (  this  .  id  )  ; 
} 






public   DocPicture   getPicture  (  int   pictureId  )  throws   Exception  { 
return  (  DocPicture  )  DocResource  .  getInstance  (  pictureId  )  ; 
} 




public   DocContentResource  [  ]  getDocContentResourceList  (  )  throws   Exception  { 
return   new   DocumentCBFDAO  (  )  .  getDocContentResourceList  (  this  .  id  )  ; 
} 




public   Document  [  ]  getCorrelationDocList  (  )  throws   Exception  { 
return   new   DocumentPublishDAO  (  )  .  getCorrelationDocList  (  this  .  id  ,  this  .  siteAsciiName  )  ; 
} 





public   Document  [  ]  getCorrelationList  (  int  [  ]  selectionDocIds  )  throws   Exception  { 
return   Documents  .  getMainList  (  this  .  getPertinentWords  (  )  ,  selectionDocIds  )  ; 
} 







public   DocumentPublish  [  ]  getMagazineList  (  )  throws   Exception  { 
DocumentCBF  [  ]  docCBF  =  new   DocumentCBFDAO  (  )  .  getMagazineList  (  id  ,  null  )  ; 
if  (  docCBF  ==  null  ||  docCBF  .  length  ==  0  )  { 
return   null  ; 
} 
DocumentPublish  [  ]  result  =  new   DocumentPublish  [  docCBF  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  docCBF  .  length  ;  i  ++  )  { 
result  [  i  ]  =  new   DocumentPublish  (  docCBF  [  i  ]  ,  this  .  getChannelPath  (  )  ,  this  .  getPublisher  (  )  ,  this  .  publishDate  ,  this  .  validStartDate  ,  this  .  validEndDate  ,  this  .  orderNo  )  ; 
} 
return   result  ; 
} 





private   void   getMagazineTree  (  DocumentPublish   doc  ,  List   treeList  )  throws   Exception  { 
DocumentCBF  [  ]  magazine  =  DocumentCBF  .  getInstance  (  doc  .  id  )  .  getMagazineList  (  null  )  ; 
for  (  int   i  =  0  ;  magazine  !=  null  &&  magazine  .  length  >  0  &&  i  <  magazine  .  length  ;  i  ++  )  { 
DocumentPublish   tmp  =  new   DocumentPublish  (  magazine  [  i  ]  ,  doc  .  getChannelPath  (  )  ,  doc  .  getPublisher  (  )  ,  doc  .  getPublishDate  (  )  ,  doc  .  getValidStartDate  (  )  ,  doc  .  getValidEndDate  (  )  ,  doc  .  getOrderNo  (  )  )  ; 
treeList  .  add  (  tmp  )  ; 
} 
treeList  .  add  (  this  )  ; 
} 






public   List   getMagazineTree  (  )  throws   Exception  { 
List   result  =  new   ArrayList  (  )  ; 
getMagazineTree  (  this  ,  result  )  ; 
return   result  ; 
} 








public   void   unPublish  (  String   userId  ,  boolean   isBackProcess  )  throws   Exception  { 
new   DocumentPublishDAO  (  )  .  unPublish  (  this  ,  userId  ,  isBackProcess  )  ; 
} 







public   void   rePublish  (  )  throws   Exception  { 
if  (  this  .  getDoctypePath  (  )  .  startsWith  (  Const  .  DOCTYPE_PATH_MAGAZINE_HEAD  )  )  { 
DocumentPublish  [  ]  adp  =  getMagazineList  (  )  ; 
if  (  adp  !=  null  &&  adp  .  length  >  0  )  { 
for  (  int   i  =  0  ;  i  <  adp  .  length  ;  i  ++  )  adp  [  i  ]  .  rePublish  (  )  ; 
} 
} 
new   DocumentPublishDAO  (  )  .  rePublish  (  this  )  ; 
} 




public   DocPicture  [  ]  getBreviaryImageList  (  )  throws   Exception  { 
return   new   DocumentCBFDAO  (  )  .  getBreviaryImageList  (  this  .  id  )  ; 
} 

public   String   getPublisherName  (  )  { 
if  (  publisherName  ==  null  )  { 
try  { 
publisherName  =  new   OrgUser  (  )  .  getUser  (  publisher  )  .  getUserName  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "�õ�publisherName���?"  )  ; 
} 
} 
return   this  .  publisherName  ; 
} 




public   void   transferPublish  (  )  { 
} 




public   Date   getPublishDate  (  )  { 
return   publishDate  ; 
} 




public   int   getPublisher  (  )  { 
return   publisher  ; 
} 




public   void   setPublishDate  (  Date   publishDate  )  { 
this  .  publishDate  =  publishDate  ; 
} 




public   void   setPublisher  (  int   publisher  )  { 
this  .  publisher  =  publisher  ; 
} 




public   int   getOrderNo  (  )  { 
return   orderNo  ; 
} 




public   void   setOrderNo  (  int   orderNo  )  { 
this  .  orderNo  =  orderNo  ; 
} 




public   Date   getValidEndDate  (  )  { 
return   validEndDate  ; 
} 




public   Date   getValidStartDate  (  )  { 
return   validStartDate  ; 
} 




public   void   setValidEndDate  (  Date   validEndDate  )  { 
this  .  validEndDate  =  validEndDate  ; 
} 




public   void   setValidStartDate  (  Date   validStartDate  )  { 
this  .  validStartDate  =  validStartDate  ; 
} 




public   boolean   isSynStatus  (  )  { 
return   synStatus  ; 
} 




public   void   setSynStatus  (  boolean   synStatus  )  { 
this  .  synStatus  =  synStatus  ; 
} 







public   String   getContent  (  )  { 
return   new   DocumentPublishDAO  (  )  .  getContent  (  this  )  ; 
} 







public   String   getContentAtChannel  (  )  { 
String   str  =  new   DocumentPublishDAO  (  )  .  getContent  (  this  )  ; 
String   imageFile  =  "d_"  +  this  .  id  +  ".files"  ; 
String   year  =  String  .  valueOf  (  this  .  getCreateDate  (  )  .  getYear  (  )  +  1900  )  ; 
String   month  =  String  .  valueOf  (  this  .  getCreateDate  (  )  .  getMonth  (  )  +  1  )  ; 
if  (  month  .  length  (  )  ==  1  )  { 
month  =  "0"  +  month  ; 
} 
String   yyyyMM  =  year  +  month  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "href=\""  +  imageFile  +  "/"  ,  "href=\"../docs/"  +  yyyyMM  +  "/"  +  imageFile  +  "/"  )  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "href="  +  imageFile  +  "/"  ,  "href=../docs/"  +  yyyyMM  +  "/"  +  imageFile  +  "/"  )  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "src=\""  +  imageFile  +  "/"  ,  "src=\"../docs/"  +  yyyyMM  +  "/"  +  imageFile  +  "/"  )  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "src="  +  imageFile  +  "/"  ,  "src=../docs/"  +  yyyyMM  +  "/"  +  imageFile  +  "/"  )  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "src=\"res/"  ,  "src=\"../docs/"  +  yyyyMM  +  "/res/"  )  ; 
str  =  StringUtil  .  replaceAll  (  str  ,  "src=res/"  ,  "src=../docs/"  +  yyyyMM  +  "/res/"  )  ; 
return   str  ; 
} 






public   void   setPublisherName  (  String   publisherName  )  { 
this  .  publisherName  =  publisherName  ; 
} 






public   String   getPublishedURL  (  )  { 
String   url  =  new   StringBuffer  (  "../docs/"  )  .  append  (  Function  .  getNYofDate  (  this  .  getCreateDate  (  )  )  )  .  append  (  "/d_"  )  .  append  (  this  .  id  )  .  append  (  ".html"  )  .  toString  (  )  ; 
return   url  ; 
} 






public   String   getPublishedURLForDocTemplate  (  )  { 
String   url  =  new   StringBuffer  (  "../"  )  .  append  (  Function  .  getNYofDate  (  this  .  getCreateDate  (  )  )  )  .  append  (  "/d_"  )  .  append  (  this  .  id  )  .  append  (  ".html"  )  .  toString  (  )  ; 
return   url  ; 
} 






public   String   getPreviewPublishedURL  (  )  { 
String   url  =  new   StringBuffer  (  "../../../../platformData/infoplat/pub/"  )  .  append  (  this  .  siteAsciiName  )  .  append  (  "/docs/"  )  .  append  (  Function  .  getNYofDate  (  this  .  getCreateDate  (  )  )  )  .  append  (  "/d_"  )  .  append  (  this  .  id  )  .  append  (  ".html"  )  .  toString  (  )  ; 
return   url  ; 
} 






public   String   getPublishedResURL  (  )  { 
String   url  =  new   StringBuffer  (  "../"  )  .  append  (  "docs/"  )  .  append  (  Function  .  getNYofDate  (  this  .  getCreateDate  (  )  )  )  .  append  (  "/res/"  )  .  toString  (  )  ; 
return   url  ; 
} 






public   String   getPublishedResURLForDocTemplate  (  )  { 
String   url  =  new   StringBuffer  (  "./"  )  .  append  (  "res/"  )  .  toString  (  )  ; 
return   url  ; 
} 






public   String   getChannelAsciiName  (  )  { 
return   channelAsciiName  ; 
} 






public   String   getSiteAsciiName  (  )  { 
return   siteAsciiName  ; 
} 






public   void   setChannelAsciiName  (  String   string  )  { 
channelAsciiName  =  string  ; 
} 






public   void   setSiteAsciiName  (  String   string  )  { 
siteAsciiName  =  string  ; 
} 







public   Channel  [  ]  getPublishedPosition  (  )  { 
Channel  [  ]  channels  =  null  ; 
String   channelPath  =  this  .  getChannelPath  (  )  ; 
if  (  channelPath  ==  null  )  { 
return   null  ; 
} 
try  { 
Channel   channel  =  (  Channel  )  Channel  .  getInstance  (  channelPath  )  ; 
channels  =  channel  .  getSelfAndAncestors  (  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  " �õ��ĵ�������λ�ó���"  ,  e  )  ; 
} 
return   channels  ; 
} 






private   void   unPublishMagazine  (  String   userId  ,  boolean   isBackProcess  ,  DocumentPublish   doc  )  throws   Exception  { 
DocumentCBF  [  ]  magazine  =  DocumentCBF  .  getInstance  (  doc  .  id  )  .  getMagazineList  (  null  )  ; 
for  (  int   i  =  0  ;  magazine  !=  null  &&  magazine  .  length  >  0  &&  i  <  magazine  .  length  ;  i  ++  )  { 
DocumentPublish   tmp  =  new   DocumentPublish  (  magazine  [  i  ]  ,  doc  .  getChannelPath  (  )  ,  doc  .  getPublisher  (  )  ,  doc  .  getPublishDate  (  )  ,  doc  .  getValidStartDate  (  )  ,  doc  .  getValidEndDate  (  )  ,  doc  .  getOrderNo  (  )  )  ; 
tmp  .  unPublishMagazine  (  userId  ,  isBackProcess  ,  tmp  )  ; 
} 
boolean   bool  =  isBackProcess  &&  doc  .  getDoctypePath  (  )  .  startsWith  (  Const  .  DOCTYPE_PATH_MAGAZINE_HEAD  )  &&  doc  .  getDoctypePath  (  )  .  length  (  )  ==  15  ; 
unPublish  (  userId  ,  bool  )  ; 
} 








public   void   unPublishMagazine  (  String   userId  ,  boolean   isBackProcess  )  throws   Exception  { 
if  (  new   DocumentPublishDAO  (  )  .  isMoreChannel  (  this  )  )  { 
boolean   bool  =  isBackProcess  &&  getDoctypePath  (  )  .  startsWith  (  Const  .  DOCTYPE_PATH_MAGAZINE_HEAD  )  &&  getDoctypePath  (  )  .  length  (  )  ==  15  ; 
unPublish  (  userId  ,  bool  )  ; 
}  else  { 
unPublishMagazine  (  userId  ,  isBackProcess  ,  this  )  ; 
} 
} 





public   String   getCBFChannelName  (  )  { 
String   result  =  ""  ; 
try  { 
DocumentCBF   cbf  =  DocumentCBF  .  getInstance  (  this  .  id  )  ; 
if  (  cbf  !=  null  )  { 
String   channelPath  =  cbf  .  getChannelPath  (  )  ; 
Channel   channel  =  (  Channel  )  TreeNode  .  getInstance  (  channelPath  )  ; 
if  (  channel  !=  null  )  { 
result  =  channel  .  getName  (  )  ; 
} 
} 
}  catch  (  Exception   ex  )  { 
log  .  error  (  "�õ��÷����ĵ��ĳ�ʼƵ�������ʧ�ܣ�"  )  ; 
} 
return   result  ; 
} 







public   String   limitString  (  String   str  ,  int   length  )  { 
return   Function  .  limitString  (  str  ,  length  )  ; 
} 








public   String   limitString  (  String   str  ,  int   length  ,  String   endStr  )  { 
return   Function  .  limitString  (  str  ,  length  ,  endStr  )  ; 
} 




public   String   getDocTypeName  (  )  { 
return   docTypeName  ; 
} 




public   void   setDocTypeName  (  String   docTypeName  )  { 
this  .  docTypeName  =  docTypeName  ; 
} 




public   String   getFull_path  (  )  { 
return   full_path  ; 
} 




public   void   setFull_path  (  String   full_path  )  { 
this  .  full_path  =  full_path  ; 
} 

public   void   setPublished_channel  (  String   published_channel  )  { 
this  .  published_channel  =  published_channel  ; 
} 

public   void   setPublished_channel_count  (  String   published_channel_count  )  { 
this  .  published_channel_count  =  published_channel_count  ; 
} 

public   String   getPublished_channel  (  )  { 
if  (  null  ==  published_channel  ||  ""  .  equals  (  published_channel  )  ||  "null"  .  equalsIgnoreCase  (  published_channel  )  )  new   DocumentPublishDAO  (  )  .  setPublish_channel  (  this  )  ; 
return   published_channel  ; 
} 

public   String   getPublished_channel_count  (  )  { 
if  (  null  ==  published_channel_count  ||  ""  .  equals  (  published_channel_count  )  ||  "null"  .  equalsIgnoreCase  (  published_channel_count  )  )  new   DocumentPublishDAO  (  )  .  setPublish_channel  (  this  )  ; 
return   published_channel_count  ; 
} 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
DocumentPublish   object  =  DocumentPublish  .  getInstance  (  "000000070201802"  ,  29002  )  ; 
object  .  rePublish  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 

