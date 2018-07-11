package   com  .  jeecms  .  cms  .  entity  .  main  .  base  ; 

import   java  .  io  .  Serializable  ; 









public   abstract   class   BaseContent   implements   Serializable  { 

public   static   String   REF  =  "Content"  ; 

public   static   String   PROP_STATUS  =  "status"  ; 

public   static   String   PROP_TYPE  =  "type"  ; 

public   static   String   PROP_RECOMMEND  =  "recommend"  ; 

public   static   String   PROP_SITE  =  "site"  ; 

public   static   String   PROP_USER  =  "user"  ; 

public   static   String   PROP_HAS_TITLE_IMG  =  "hasTitleImg"  ; 

public   static   String   PROP_SORT_DATE  =  "sortDate"  ; 

public   static   String   PROP_TOP_LEVEL  =  "topLevel"  ; 

public   static   String   PROP_COMMENTS_DAY  =  "commentsDay"  ; 

public   static   String   PROP_CONTENT_EXT  =  "contentExt"  ; 

public   static   String   PROP_VIEWS_DAY  =  "viewsDay"  ; 

public   static   String   PROP_UPS_DAY  =  "upsDay"  ; 

public   static   String   PROP_CHANNEL  =  "channel"  ; 

public   static   String   PROP_CONTENT_COUNT  =  "contentCount"  ; 

public   static   String   PROP_ID  =  "id"  ; 

public   static   String   PROP_DOWNLOADS_DAY  =  "downloadsDay"  ; 

public   BaseContent  (  )  { 
initialize  (  )  ; 
} 




public   BaseContent  (  java  .  lang  .  Integer   id  )  { 
this  .  setId  (  id  )  ; 
initialize  (  )  ; 
} 




public   BaseContent  (  java  .  lang  .  Integer   id  ,  com  .  jeecms  .  cms  .  entity  .  main  .  CmsSite   site  ,  java  .  util  .  Date   sortDate  ,  java  .  lang  .  Byte   topLevel  ,  java  .  lang  .  Boolean   hasTitleImg  ,  java  .  lang  .  Boolean   recommend  ,  java  .  lang  .  Byte   status  ,  java  .  lang  .  Integer   viewsDay  ,  java  .  lang  .  Short   commentsDay  ,  java  .  lang  .  Short   downloadsDay  ,  java  .  lang  .  Short   upsDay  )  { 
this  .  setId  (  id  )  ; 
this  .  setSite  (  site  )  ; 
this  .  setSortDate  (  sortDate  )  ; 
this  .  setTopLevel  (  topLevel  )  ; 
this  .  setHasTitleImg  (  hasTitleImg  )  ; 
this  .  setRecommend  (  recommend  )  ; 
this  .  setStatus  (  status  )  ; 
this  .  setViewsDay  (  viewsDay  )  ; 
this  .  setCommentsDay  (  commentsDay  )  ; 
this  .  setDownloadsDay  (  downloadsDay  )  ; 
this  .  setUpsDay  (  upsDay  )  ; 
initialize  (  )  ; 
} 

protected   void   initialize  (  )  { 
} 

private   int   hashCode  =  Integer  .  MIN_VALUE  ; 

private   java  .  lang  .  Integer   id  ; 

private   java  .  util  .  Date   sortDate  ; 

private   java  .  lang  .  Byte   topLevel  ; 

private   java  .  lang  .  Boolean   hasTitleImg  ; 

private   java  .  lang  .  Boolean   recommend  ; 

private   java  .  lang  .  Byte   status  ; 

private   java  .  lang  .  Integer   viewsDay  ; 

private   java  .  lang  .  Short   commentsDay  ; 

private   java  .  lang  .  Short   downloadsDay  ; 

private   java  .  lang  .  Short   upsDay  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  ContentExt   contentExt  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  ContentCount   contentCount  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  ContentType   type  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  CmsSite   site  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser   user  ; 

private   com  .  jeecms  .  cms  .  entity  .  main  .  Channel   channel  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  Channel  >  channels  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsTopic  >  topics  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsGroup  >  viewGroups  ; 

private   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTag  >  tags  ; 

private   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentPicture  >  pictures  ; 

private   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentAttachment  >  attachments  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTxt  >  contentTxtSet  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentCheck  >  contentCheckSet  ; 

private   java  .  util  .  Map  <  java  .  lang  .  String  ,  java  .  lang  .  String  >  attr  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser  >  collectUsers  ; 







public   java  .  lang  .  Integer   getId  (  )  { 
return   id  ; 
} 





public   void   setId  (  java  .  lang  .  Integer   id  )  { 
this  .  id  =  id  ; 
this  .  hashCode  =  Integer  .  MIN_VALUE  ; 
} 




public   java  .  util  .  Date   getSortDate  (  )  { 
return   sortDate  ; 
} 





public   void   setSortDate  (  java  .  util  .  Date   sortDate  )  { 
this  .  sortDate  =  sortDate  ; 
} 




public   java  .  lang  .  Byte   getTopLevel  (  )  { 
return   topLevel  ; 
} 





public   void   setTopLevel  (  java  .  lang  .  Byte   topLevel  )  { 
this  .  topLevel  =  topLevel  ; 
} 




public   java  .  lang  .  Boolean   getHasTitleImg  (  )  { 
return   hasTitleImg  ; 
} 





public   void   setHasTitleImg  (  java  .  lang  .  Boolean   hasTitleImg  )  { 
this  .  hasTitleImg  =  hasTitleImg  ; 
} 




public   java  .  lang  .  Boolean   getRecommend  (  )  { 
return   recommend  ; 
} 





public   void   setRecommend  (  java  .  lang  .  Boolean   recommend  )  { 
this  .  recommend  =  recommend  ; 
} 




public   java  .  lang  .  Byte   getStatus  (  )  { 
return   status  ; 
} 





public   void   setStatus  (  java  .  lang  .  Byte   status  )  { 
this  .  status  =  status  ; 
} 




public   java  .  lang  .  Integer   getViewsDay  (  )  { 
return   viewsDay  ; 
} 





public   void   setViewsDay  (  java  .  lang  .  Integer   viewsDay  )  { 
this  .  viewsDay  =  viewsDay  ; 
} 




public   java  .  lang  .  Short   getCommentsDay  (  )  { 
return   commentsDay  ; 
} 





public   void   setCommentsDay  (  java  .  lang  .  Short   commentsDay  )  { 
this  .  commentsDay  =  commentsDay  ; 
} 




public   java  .  lang  .  Short   getDownloadsDay  (  )  { 
return   downloadsDay  ; 
} 





public   void   setDownloadsDay  (  java  .  lang  .  Short   downloadsDay  )  { 
this  .  downloadsDay  =  downloadsDay  ; 
} 




public   java  .  lang  .  Short   getUpsDay  (  )  { 
return   upsDay  ; 
} 





public   void   setUpsDay  (  java  .  lang  .  Short   upsDay  )  { 
this  .  upsDay  =  upsDay  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  ContentExt   getContentExt  (  )  { 
return   contentExt  ; 
} 





public   void   setContentExt  (  com  .  jeecms  .  cms  .  entity  .  main  .  ContentExt   contentExt  )  { 
this  .  contentExt  =  contentExt  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  ContentCount   getContentCount  (  )  { 
return   contentCount  ; 
} 





public   void   setContentCount  (  com  .  jeecms  .  cms  .  entity  .  main  .  ContentCount   contentCount  )  { 
this  .  contentCount  =  contentCount  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  ContentType   getType  (  )  { 
return   type  ; 
} 





public   void   setType  (  com  .  jeecms  .  cms  .  entity  .  main  .  ContentType   type  )  { 
this  .  type  =  type  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  CmsSite   getSite  (  )  { 
return   site  ; 
} 





public   void   setSite  (  com  .  jeecms  .  cms  .  entity  .  main  .  CmsSite   site  )  { 
this  .  site  =  site  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser   getUser  (  )  { 
return   user  ; 
} 





public   void   setUser  (  com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser   user  )  { 
this  .  user  =  user  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  main  .  Channel   getChannel  (  )  { 
return   channel  ; 
} 





public   void   setChannel  (  com  .  jeecms  .  cms  .  entity  .  main  .  Channel   channel  )  { 
this  .  channel  =  channel  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  Channel  >  getChannels  (  )  { 
return   channels  ; 
} 





public   void   setChannels  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  Channel  >  channels  )  { 
this  .  channels  =  channels  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsTopic  >  getTopics  (  )  { 
return   topics  ; 
} 





public   void   setTopics  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsTopic  >  topics  )  { 
this  .  topics  =  topics  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsGroup  >  getViewGroups  (  )  { 
return   viewGroups  ; 
} 





public   void   setViewGroups  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsGroup  >  viewGroups  )  { 
this  .  viewGroups  =  viewGroups  ; 
} 




public   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTag  >  getTags  (  )  { 
return   tags  ; 
} 





public   void   setTags  (  java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTag  >  tags  )  { 
this  .  tags  =  tags  ; 
} 




public   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentPicture  >  getPictures  (  )  { 
return   pictures  ; 
} 





public   void   setPictures  (  java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentPicture  >  pictures  )  { 
this  .  pictures  =  pictures  ; 
} 




public   java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentAttachment  >  getAttachments  (  )  { 
return   attachments  ; 
} 





public   void   setAttachments  (  java  .  util  .  List  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentAttachment  >  attachments  )  { 
this  .  attachments  =  attachments  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTxt  >  getContentTxtSet  (  )  { 
return   contentTxtSet  ; 
} 





public   void   setContentTxtSet  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentTxt  >  contentTxtSet  )  { 
this  .  contentTxtSet  =  contentTxtSet  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentCheck  >  getContentCheckSet  (  )  { 
return   contentCheckSet  ; 
} 





public   void   setContentCheckSet  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  ContentCheck  >  contentCheckSet  )  { 
this  .  contentCheckSet  =  contentCheckSet  ; 
} 




public   java  .  util  .  Map  <  java  .  lang  .  String  ,  java  .  lang  .  String  >  getAttr  (  )  { 
return   attr  ; 
} 

public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser  >  getCollectUsers  (  )  { 
return   collectUsers  ; 
} 

public   void   setCollectUsers  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  main  .  CmsUser  >  collectUsers  )  { 
this  .  collectUsers  =  collectUsers  ; 
} 





public   void   setAttr  (  java  .  util  .  Map  <  java  .  lang  .  String  ,  java  .  lang  .  String  >  attr  )  { 
this  .  attr  =  attr  ; 
} 

public   boolean   equals  (  Object   obj  )  { 
if  (  null  ==  obj  )  return   false  ; 
if  (  !  (  obj   instanceof   com  .  jeecms  .  cms  .  entity  .  main  .  Content  )  )  return   false  ;  else  { 
com  .  jeecms  .  cms  .  entity  .  main  .  Content   content  =  (  com  .  jeecms  .  cms  .  entity  .  main  .  Content  )  obj  ; 
if  (  null  ==  this  .  getId  (  )  ||  null  ==  content  .  getId  (  )  )  return   false  ;  else   return  (  this  .  getId  (  )  .  equals  (  content  .  getId  (  )  )  )  ; 
} 
} 

public   int   hashCode  (  )  { 
if  (  Integer  .  MIN_VALUE  ==  this  .  hashCode  )  { 
if  (  null  ==  this  .  getId  (  )  )  return   super  .  hashCode  (  )  ;  else  { 
String   hashStr  =  this  .  getClass  (  )  .  getName  (  )  +  ":"  +  this  .  getId  (  )  .  hashCode  (  )  ; 
this  .  hashCode  =  hashStr  .  hashCode  (  )  ; 
} 
} 
return   this  .  hashCode  ; 
} 

public   String   toString  (  )  { 
return   super  .  toString  (  )  ; 
} 
} 

