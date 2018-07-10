package   com  .  jeecms  .  article  .  entity  .  base  ; 

import   java  .  io  .  Serializable  ; 









public   abstract   class   BaseArticle   implements   Serializable  { 

public   static   String   REF  =  "Article"  ; 

public   static   String   PROP_ADMIN_DISABLE  =  "adminDisable"  ; 

public   static   String   PROP_CHECK  =  "check"  ; 

public   static   String   PROP_DEF_STR9  =  "defStr9"  ; 

public   static   String   PROP_DEF_MONEY2  =  "defMoney2"  ; 

public   static   String   PROP_VISIT_WEEK  =  "visitWeek"  ; 

public   static   String   PROP_DISABLED  =  "disabled"  ; 

public   static   String   PROP_TAGS  =  "tags"  ; 

public   static   String   PROP_TITLE_COLOR  =  "titleColor"  ; 

public   static   String   PROP_CONTENT_RES_PATH  =  "contentResPath"  ; 

public   static   String   PROP_VISIT_TOTAL  =  "visitTotal"  ; 

public   static   String   PROP_CONFIG  =  "config"  ; 

public   static   String   PROP_GROUP  =  "group"  ; 

public   static   String   PROP_DEF_LONG4  =  "defLong4"  ; 

public   static   String   PROP_ORIGIN  =  "origin"  ; 

public   static   String   PROP_CONTENT_CTG  =  "contentCtg"  ; 

public   static   String   PROP_RELEASE_SYS_DATE  =  "releaseSysDate"  ; 

public   static   String   PROP_CHANNEL  =  "channel"  ; 

public   static   String   PROP_PARAM1  =  "param1"  ; 

public   static   String   PROP_DEF_STR5  =  "defStr5"  ; 

public   static   String   PROP_DEF_STR2  =  "defStr2"  ; 

public   static   String   PROP_VISIT_YEAR  =  "visitYear"  ; 

public   static   String   PROP_DEF_BOOL1  =  "defBool1"  ; 

public   static   String   PROP_VISIT_MONTH  =  "visitMonth"  ; 

public   static   String   PROP_PAGE_COUNT  =  "pageCount"  ; 

public   static   String   PROP_SHORT_TITLE  =  "shortTitle"  ; 

public   static   String   PROP_CHECK_OPINION  =  "checkOpinion"  ; 

public   static   String   PROP_ALLOW_COMMENT  =  "allowComment"  ; 

public   static   String   PROP_SORT_DATE  =  "sortDate"  ; 

public   static   String   PROP_TOP_LEVEL  =  "topLevel"  ; 

public   static   String   PROP_DEF_DATE2  =  "defDate2"  ; 

public   static   String   PROP_RELEASE_DATE  =  "releaseDate"  ; 

public   static   String   PROP_TITLE  =  "title"  ; 

public   static   String   PROP_PARAM2  =  "param2"  ; 

public   static   String   PROP_DEF_STR6  =  "defStr6"  ; 

public   static   String   PROP_DEF_DATE3  =  "defDate3"  ; 

public   static   String   PROP_DEF_STR1  =  "defStr1"  ; 

public   static   String   PROP_DEF_STR4  =  "defStr4"  ; 

public   static   String   PROP_DISABLE_TIME  =  "disableTime"  ; 

public   static   String   PROP_ADMIN_CHECK  =  "adminCheck"  ; 

public   static   String   PROP_AUTHOR  =  "author"  ; 

public   static   String   PROP_VISIT_QUARTER  =  "visitQuarter"  ; 

public   static   String   PROP_DRAFT  =  "draft"  ; 

public   static   String   PROP_DEF_MONEY3  =  "defMoney3"  ; 

public   static   String   PROP_STAT_DATE  =  "statDate"  ; 

public   static   String   PROP_DESCRIPTION  =  "description"  ; 

public   static   String   PROP_PRE  =  "pre"  ; 

public   static   String   PROP_PARAM3  =  "param3"  ; 

public   static   String   PROP_DEF_BOOL3  =  "defBool3"  ; 

public   static   String   PROP_DEF_STR3  =  "defStr3"  ; 

public   static   String   PROP_CHECK_TIME  =  "checkTime"  ; 

public   static   String   PROP_NEXT  =  "next"  ; 

public   static   String   PROP_DEF_LONG5  =  "defLong5"  ; 

public   static   String   PROP_RECOMMEND  =  "recommend"  ; 

public   static   String   PROP_DEF_BOOL2  =  "defBool2"  ; 

public   static   String   PROP_COMMENT_COUNT  =  "commentCount"  ; 

public   static   String   PROP_DEF_LONG2  =  "defLong2"  ; 

public   static   String   PROP_WEBSITE  =  "website"  ; 

public   static   String   PROP_VISIT_TODAY  =  "visitToday"  ; 

public   static   String   PROP_ADMIN_INPUT  =  "adminInput"  ; 

public   static   String   PROP_DEF_DATE1  =  "defDate1"  ; 

public   static   String   PROP_DEF_STR8  =  "defStr8"  ; 

public   static   String   PROP_DEF_MONEY1  =  "defMoney1"  ; 

public   static   String   PROP_TPL_CONTENT  =  "tplContent"  ; 

public   static   String   PROP_HAS_TITLE_IMG  =  "hasTitleImg"  ; 

public   static   String   PROP_OUTER_URL  =  "outerUrl"  ; 

public   static   String   PROP_DEF_LONG1  =  "defLong1"  ; 

public   static   String   PROP_DEF_STR7  =  "defStr7"  ; 

public   static   String   PROP_TITLE_IMG  =  "titleImg"  ; 

public   static   String   PROP_BOLD  =  "bold"  ; 

public   static   String   PROP_CHECK_STEP  =  "checkStep"  ; 

public   static   String   PROP_MEMBER  =  "member"  ; 

public   static   String   PROP_DEF_LONG3  =  "defLong3"  ; 

public   static   String   PROP_ID  =  "id"  ; 

public   static   String   PROP_RELATED_IDS  =  "relatedIds"  ; 

public   static   String   PROP_REJECT  =  "reject"  ; 

public   static   String   PROP_CONTENT_IMG  =  "contentImg"  ; 

public   BaseArticle  (  )  { 
initialize  (  )  ; 
} 




public   BaseArticle  (  java  .  lang  .  Long   id  )  { 
this  .  setId  (  id  )  ; 
initialize  (  )  ; 
} 




public   BaseArticle  (  java  .  lang  .  Long   id  ,  com  .  jeecms  .  cms  .  entity  .  ContentCtg   contentCtg  ,  com  .  jeecms  .  cms  .  entity  .  CmsChannel   channel  ,  com  .  jeecms  .  core  .  entity  .  Website   website  ,  com  .  jeecms  .  cms  .  entity  .  CmsConfig   config  ,  java  .  util  .  Date   sortDate  ,  java  .  util  .  Date   releaseDate  ,  java  .  util  .  Date   releaseSysDate  ,  java  .  lang  .  Long   visitTotal  ,  java  .  lang  .  Long   visitToday  ,  java  .  lang  .  Long   visitWeek  ,  java  .  lang  .  Long   visitMonth  ,  java  .  lang  .  Long   visitQuarter  ,  java  .  lang  .  Long   visitYear  ,  java  .  lang  .  Integer   checkStep  ,  java  .  lang  .  Integer   topLevel  ,  java  .  lang  .  Integer   commentCount  ,  java  .  lang  .  Boolean   hasTitleImg  ,  java  .  lang  .  Boolean   allowComment  ,  java  .  lang  .  Boolean   bold  ,  java  .  lang  .  Boolean   draft  ,  java  .  lang  .  Boolean   recommend  ,  java  .  lang  .  Boolean   check  ,  java  .  lang  .  Boolean   disabled  ,  java  .  lang  .  Boolean   reject  )  { 
this  .  setId  (  id  )  ; 
this  .  setContentCtg  (  contentCtg  )  ; 
this  .  setChannel  (  channel  )  ; 
this  .  setWebsite  (  website  )  ; 
this  .  setConfig  (  config  )  ; 
this  .  setSortDate  (  sortDate  )  ; 
this  .  setReleaseDate  (  releaseDate  )  ; 
this  .  setReleaseSysDate  (  releaseSysDate  )  ; 
this  .  setVisitTotal  (  visitTotal  )  ; 
this  .  setVisitToday  (  visitToday  )  ; 
this  .  setVisitWeek  (  visitWeek  )  ; 
this  .  setVisitMonth  (  visitMonth  )  ; 
this  .  setVisitQuarter  (  visitQuarter  )  ; 
this  .  setVisitYear  (  visitYear  )  ; 
this  .  setCheckStep  (  checkStep  )  ; 
this  .  setTopLevel  (  topLevel  )  ; 
this  .  setCommentCount  (  commentCount  )  ; 
this  .  setHasTitleImg  (  hasTitleImg  )  ; 
this  .  setAllowComment  (  allowComment  )  ; 
this  .  setBold  (  bold  )  ; 
this  .  setDraft  (  draft  )  ; 
this  .  setRecommend  (  recommend  )  ; 
this  .  setCheck  (  check  )  ; 
this  .  setDisabled  (  disabled  )  ; 
this  .  setReject  (  reject  )  ; 
initialize  (  )  ; 
} 

protected   void   initialize  (  )  { 
} 

private   int   hashCode  =  Integer  .  MIN_VALUE  ; 

private   java  .  lang  .  Long   id  ; 

private   java  .  lang  .  String   title  ; 

private   java  .  lang  .  String   shortTitle  ; 

private   java  .  lang  .  String   titleImg  ; 

private   java  .  lang  .  String   contentImg  ; 

private   java  .  lang  .  String   titleColor  ; 

private   java  .  lang  .  String   description  ; 

private   java  .  lang  .  String   tags  ; 

private   java  .  lang  .  String   author  ; 

private   java  .  lang  .  String   origin  ; 

private   java  .  util  .  Date   sortDate  ; 

private   java  .  util  .  Date   releaseDate  ; 

private   java  .  util  .  Date   releaseSysDate  ; 

private   java  .  util  .  Date   checkTime  ; 

private   java  .  util  .  Date   disableTime  ; 

private   java  .  lang  .  Long   visitTotal  ; 

private   java  .  lang  .  Long   visitToday  ; 

private   java  .  lang  .  Long   visitWeek  ; 

private   java  .  lang  .  Long   visitMonth  ; 

private   java  .  lang  .  Long   visitQuarter  ; 

private   java  .  lang  .  Long   visitYear  ; 

private   java  .  util  .  Date   statDate  ; 

private   java  .  lang  .  String   outerUrl  ; 

private   java  .  lang  .  String   contentResPath  ; 

private   java  .  lang  .  Integer   pageCount  ; 

private   java  .  lang  .  String   tplContent  ; 

private   java  .  lang  .  Integer   checkStep  ; 

private   java  .  lang  .  Integer   topLevel  ; 

private   java  .  lang  .  Integer   commentCount  ; 

private   java  .  lang  .  String   checkOpinion  ; 

private   java  .  lang  .  String   relatedIds  ; 

private   java  .  lang  .  Boolean   hasTitleImg  ; 

private   java  .  lang  .  Boolean   allowComment  ; 

private   java  .  lang  .  Boolean   bold  ; 

private   java  .  lang  .  Boolean   draft  ; 

private   java  .  lang  .  Boolean   recommend  ; 

private   java  .  lang  .  Boolean   check  ; 

private   java  .  lang  .  Boolean   disabled  ; 

private   java  .  lang  .  Boolean   reject  ; 

private   java  .  lang  .  String   param1  ; 

private   java  .  lang  .  String   param2  ; 

private   java  .  lang  .  String   param3  ; 

private   java  .  lang  .  String   defStr1  ; 

private   java  .  lang  .  String   defStr2  ; 

private   java  .  lang  .  String   defStr3  ; 

private   java  .  lang  .  String   defStr4  ; 

private   java  .  lang  .  String   defStr5  ; 

private   java  .  lang  .  String   defStr6  ; 

private   java  .  lang  .  String   defStr7  ; 

private   java  .  lang  .  String   defStr8  ; 

private   java  .  lang  .  String   defStr9  ; 

private   java  .  lang  .  Long   defLong1  ; 

private   java  .  lang  .  Long   defLong2  ; 

private   java  .  lang  .  Long   defLong3  ; 

private   java  .  lang  .  Long   defLong4  ; 

private   java  .  lang  .  Long   defLong5  ; 

private   java  .  math  .  BigDecimal   defMoney1  ; 

private   java  .  math  .  BigDecimal   defMoney2  ; 

private   java  .  math  .  BigDecimal   defMoney3  ; 

private   java  .  util  .  Date   defDate1  ; 

private   java  .  util  .  Date   defDate2  ; 

private   java  .  util  .  Date   defDate3  ; 

private   java  .  lang  .  Boolean   defBool1  ; 

private   java  .  lang  .  Boolean   defBool2  ; 

private   java  .  lang  .  Boolean   defBool3  ; 

private   com  .  jeecms  .  article  .  entity  .  Article   next  ; 

private   com  .  jeecms  .  article  .  entity  .  Article   pre  ; 

private   com  .  jeecms  .  cms  .  entity  .  ContentCtg   contentCtg  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminDisable  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsMemberGroup   group  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsChannel   channel  ; 

private   com  .  jeecms  .  core  .  entity  .  Website   website  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsConfig   config  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminCheck  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminInput  ; 

private   com  .  jeecms  .  cms  .  entity  .  CmsMember   member  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  core  .  entity  .  Attachment  >  attachments  ; 

private   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  CmsTopic  >  topics  ; 







public   java  .  lang  .  Long   getId  (  )  { 
return   id  ; 
} 





public   void   setId  (  java  .  lang  .  Long   id  )  { 
this  .  id  =  id  ; 
this  .  hashCode  =  Integer  .  MIN_VALUE  ; 
} 




public   java  .  lang  .  String   getTitle  (  )  { 
return   title  ; 
} 





public   void   setTitle  (  java  .  lang  .  String   title  )  { 
this  .  title  =  title  ; 
} 




public   java  .  lang  .  String   getShortTitle  (  )  { 
return   shortTitle  ; 
} 





public   void   setShortTitle  (  java  .  lang  .  String   shortTitle  )  { 
this  .  shortTitle  =  shortTitle  ; 
} 




public   java  .  lang  .  String   getTitleImg  (  )  { 
return   titleImg  ; 
} 





public   void   setTitleImg  (  java  .  lang  .  String   titleImg  )  { 
this  .  titleImg  =  titleImg  ; 
} 




public   java  .  lang  .  String   getContentImg  (  )  { 
return   contentImg  ; 
} 





public   void   setContentImg  (  java  .  lang  .  String   contentImg  )  { 
this  .  contentImg  =  contentImg  ; 
} 




public   java  .  lang  .  String   getTitleColor  (  )  { 
return   titleColor  ; 
} 





public   void   setTitleColor  (  java  .  lang  .  String   titleColor  )  { 
this  .  titleColor  =  titleColor  ; 
} 




public   java  .  lang  .  String   getDescription  (  )  { 
return   description  ; 
} 





public   void   setDescription  (  java  .  lang  .  String   description  )  { 
this  .  description  =  description  ; 
} 




public   java  .  lang  .  String   getTags  (  )  { 
return   tags  ; 
} 





public   void   setTags  (  java  .  lang  .  String   tags  )  { 
this  .  tags  =  tags  ; 
} 




public   java  .  lang  .  String   getAuthor  (  )  { 
return   author  ; 
} 





public   void   setAuthor  (  java  .  lang  .  String   author  )  { 
this  .  author  =  author  ; 
} 




public   java  .  lang  .  String   getOrigin  (  )  { 
return   origin  ; 
} 





public   void   setOrigin  (  java  .  lang  .  String   origin  )  { 
this  .  origin  =  origin  ; 
} 




public   java  .  util  .  Date   getSortDate  (  )  { 
return   sortDate  ; 
} 





public   void   setSortDate  (  java  .  util  .  Date   sortDate  )  { 
this  .  sortDate  =  sortDate  ; 
} 




public   java  .  util  .  Date   getReleaseDate  (  )  { 
return   releaseDate  ; 
} 





public   void   setReleaseDate  (  java  .  util  .  Date   releaseDate  )  { 
this  .  releaseDate  =  releaseDate  ; 
} 




public   java  .  util  .  Date   getReleaseSysDate  (  )  { 
return   releaseSysDate  ; 
} 





public   void   setReleaseSysDate  (  java  .  util  .  Date   releaseSysDate  )  { 
this  .  releaseSysDate  =  releaseSysDate  ; 
} 




public   java  .  util  .  Date   getCheckTime  (  )  { 
return   checkTime  ; 
} 





public   void   setCheckTime  (  java  .  util  .  Date   checkTime  )  { 
this  .  checkTime  =  checkTime  ; 
} 




public   java  .  util  .  Date   getDisableTime  (  )  { 
return   disableTime  ; 
} 





public   void   setDisableTime  (  java  .  util  .  Date   disableTime  )  { 
this  .  disableTime  =  disableTime  ; 
} 




public   java  .  lang  .  Long   getVisitTotal  (  )  { 
return   visitTotal  ; 
} 





public   void   setVisitTotal  (  java  .  lang  .  Long   visitTotal  )  { 
this  .  visitTotal  =  visitTotal  ; 
} 




public   java  .  lang  .  Long   getVisitToday  (  )  { 
return   visitToday  ; 
} 





public   void   setVisitToday  (  java  .  lang  .  Long   visitToday  )  { 
this  .  visitToday  =  visitToday  ; 
} 




public   java  .  lang  .  Long   getVisitWeek  (  )  { 
return   visitWeek  ; 
} 





public   void   setVisitWeek  (  java  .  lang  .  Long   visitWeek  )  { 
this  .  visitWeek  =  visitWeek  ; 
} 




public   java  .  lang  .  Long   getVisitMonth  (  )  { 
return   visitMonth  ; 
} 





public   void   setVisitMonth  (  java  .  lang  .  Long   visitMonth  )  { 
this  .  visitMonth  =  visitMonth  ; 
} 




public   java  .  lang  .  Long   getVisitQuarter  (  )  { 
return   visitQuarter  ; 
} 





public   void   setVisitQuarter  (  java  .  lang  .  Long   visitQuarter  )  { 
this  .  visitQuarter  =  visitQuarter  ; 
} 




public   java  .  lang  .  Long   getVisitYear  (  )  { 
return   visitYear  ; 
} 





public   void   setVisitYear  (  java  .  lang  .  Long   visitYear  )  { 
this  .  visitYear  =  visitYear  ; 
} 




public   java  .  util  .  Date   getStatDate  (  )  { 
return   statDate  ; 
} 





public   void   setStatDate  (  java  .  util  .  Date   statDate  )  { 
this  .  statDate  =  statDate  ; 
} 




public   java  .  lang  .  String   getOuterUrl  (  )  { 
return   outerUrl  ; 
} 





public   void   setOuterUrl  (  java  .  lang  .  String   outerUrl  )  { 
this  .  outerUrl  =  outerUrl  ; 
} 




public   java  .  lang  .  String   getContentResPath  (  )  { 
return   contentResPath  ; 
} 





public   void   setContentResPath  (  java  .  lang  .  String   contentResPath  )  { 
this  .  contentResPath  =  contentResPath  ; 
} 




public   java  .  lang  .  Integer   getPageCount  (  )  { 
return   pageCount  ; 
} 





public   void   setPageCount  (  java  .  lang  .  Integer   pageCount  )  { 
this  .  pageCount  =  pageCount  ; 
} 




public   java  .  lang  .  String   getTplContent  (  )  { 
return   tplContent  ; 
} 





public   void   setTplContent  (  java  .  lang  .  String   tplContent  )  { 
this  .  tplContent  =  tplContent  ; 
} 




public   java  .  lang  .  Integer   getCheckStep  (  )  { 
return   checkStep  ; 
} 





public   void   setCheckStep  (  java  .  lang  .  Integer   checkStep  )  { 
this  .  checkStep  =  checkStep  ; 
} 




public   java  .  lang  .  Integer   getTopLevel  (  )  { 
return   topLevel  ; 
} 





public   void   setTopLevel  (  java  .  lang  .  Integer   topLevel  )  { 
this  .  topLevel  =  topLevel  ; 
} 




public   java  .  lang  .  Integer   getCommentCount  (  )  { 
return   commentCount  ; 
} 





public   void   setCommentCount  (  java  .  lang  .  Integer   commentCount  )  { 
this  .  commentCount  =  commentCount  ; 
} 




public   java  .  lang  .  String   getCheckOpinion  (  )  { 
return   checkOpinion  ; 
} 





public   void   setCheckOpinion  (  java  .  lang  .  String   checkOpinion  )  { 
this  .  checkOpinion  =  checkOpinion  ; 
} 




public   java  .  lang  .  String   getRelatedIds  (  )  { 
return   relatedIds  ; 
} 





public   void   setRelatedIds  (  java  .  lang  .  String   relatedIds  )  { 
this  .  relatedIds  =  relatedIds  ; 
} 




public   java  .  lang  .  Boolean   getHasTitleImg  (  )  { 
return   hasTitleImg  ; 
} 





public   void   setHasTitleImg  (  java  .  lang  .  Boolean   hasTitleImg  )  { 
this  .  hasTitleImg  =  hasTitleImg  ; 
} 




public   java  .  lang  .  Boolean   getAllowComment  (  )  { 
return   allowComment  ; 
} 





public   void   setAllowComment  (  java  .  lang  .  Boolean   allowComment  )  { 
this  .  allowComment  =  allowComment  ; 
} 




public   java  .  lang  .  Boolean   getBold  (  )  { 
return   bold  ; 
} 





public   void   setBold  (  java  .  lang  .  Boolean   bold  )  { 
this  .  bold  =  bold  ; 
} 




public   java  .  lang  .  Boolean   getDraft  (  )  { 
return   draft  ; 
} 





public   void   setDraft  (  java  .  lang  .  Boolean   draft  )  { 
this  .  draft  =  draft  ; 
} 




public   java  .  lang  .  Boolean   getRecommend  (  )  { 
return   recommend  ; 
} 





public   void   setRecommend  (  java  .  lang  .  Boolean   recommend  )  { 
this  .  recommend  =  recommend  ; 
} 




public   java  .  lang  .  Boolean   getCheck  (  )  { 
return   check  ; 
} 





public   void   setCheck  (  java  .  lang  .  Boolean   check  )  { 
this  .  check  =  check  ; 
} 




public   java  .  lang  .  Boolean   getDisabled  (  )  { 
return   disabled  ; 
} 





public   void   setDisabled  (  java  .  lang  .  Boolean   disabled  )  { 
this  .  disabled  =  disabled  ; 
} 




public   java  .  lang  .  Boolean   getReject  (  )  { 
return   reject  ; 
} 





public   void   setReject  (  java  .  lang  .  Boolean   reject  )  { 
this  .  reject  =  reject  ; 
} 




public   java  .  lang  .  String   getParam1  (  )  { 
return   param1  ; 
} 





public   void   setParam1  (  java  .  lang  .  String   param1  )  { 
this  .  param1  =  param1  ; 
} 




public   java  .  lang  .  String   getParam2  (  )  { 
return   param2  ; 
} 





public   void   setParam2  (  java  .  lang  .  String   param2  )  { 
this  .  param2  =  param2  ; 
} 




public   java  .  lang  .  String   getParam3  (  )  { 
return   param3  ; 
} 





public   void   setParam3  (  java  .  lang  .  String   param3  )  { 
this  .  param3  =  param3  ; 
} 




public   java  .  lang  .  String   getDefStr1  (  )  { 
return   defStr1  ; 
} 





public   void   setDefStr1  (  java  .  lang  .  String   defStr1  )  { 
this  .  defStr1  =  defStr1  ; 
} 




public   java  .  lang  .  String   getDefStr2  (  )  { 
return   defStr2  ; 
} 





public   void   setDefStr2  (  java  .  lang  .  String   defStr2  )  { 
this  .  defStr2  =  defStr2  ; 
} 




public   java  .  lang  .  String   getDefStr3  (  )  { 
return   defStr3  ; 
} 





public   void   setDefStr3  (  java  .  lang  .  String   defStr3  )  { 
this  .  defStr3  =  defStr3  ; 
} 




public   java  .  lang  .  String   getDefStr4  (  )  { 
return   defStr4  ; 
} 





public   void   setDefStr4  (  java  .  lang  .  String   defStr4  )  { 
this  .  defStr4  =  defStr4  ; 
} 




public   java  .  lang  .  String   getDefStr5  (  )  { 
return   defStr5  ; 
} 





public   void   setDefStr5  (  java  .  lang  .  String   defStr5  )  { 
this  .  defStr5  =  defStr5  ; 
} 




public   java  .  lang  .  String   getDefStr6  (  )  { 
return   defStr6  ; 
} 





public   void   setDefStr6  (  java  .  lang  .  String   defStr6  )  { 
this  .  defStr6  =  defStr6  ; 
} 




public   java  .  lang  .  String   getDefStr7  (  )  { 
return   defStr7  ; 
} 





public   void   setDefStr7  (  java  .  lang  .  String   defStr7  )  { 
this  .  defStr7  =  defStr7  ; 
} 




public   java  .  lang  .  String   getDefStr8  (  )  { 
return   defStr8  ; 
} 





public   void   setDefStr8  (  java  .  lang  .  String   defStr8  )  { 
this  .  defStr8  =  defStr8  ; 
} 




public   java  .  lang  .  String   getDefStr9  (  )  { 
return   defStr9  ; 
} 





public   void   setDefStr9  (  java  .  lang  .  String   defStr9  )  { 
this  .  defStr9  =  defStr9  ; 
} 




public   java  .  lang  .  Long   getDefLong1  (  )  { 
return   defLong1  ; 
} 





public   void   setDefLong1  (  java  .  lang  .  Long   defLong1  )  { 
this  .  defLong1  =  defLong1  ; 
} 




public   java  .  lang  .  Long   getDefLong2  (  )  { 
return   defLong2  ; 
} 





public   void   setDefLong2  (  java  .  lang  .  Long   defLong2  )  { 
this  .  defLong2  =  defLong2  ; 
} 




public   java  .  lang  .  Long   getDefLong3  (  )  { 
return   defLong3  ; 
} 





public   void   setDefLong3  (  java  .  lang  .  Long   defLong3  )  { 
this  .  defLong3  =  defLong3  ; 
} 




public   java  .  lang  .  Long   getDefLong4  (  )  { 
return   defLong4  ; 
} 





public   void   setDefLong4  (  java  .  lang  .  Long   defLong4  )  { 
this  .  defLong4  =  defLong4  ; 
} 




public   java  .  lang  .  Long   getDefLong5  (  )  { 
return   defLong5  ; 
} 





public   void   setDefLong5  (  java  .  lang  .  Long   defLong5  )  { 
this  .  defLong5  =  defLong5  ; 
} 




public   java  .  math  .  BigDecimal   getDefMoney1  (  )  { 
return   defMoney1  ; 
} 





public   void   setDefMoney1  (  java  .  math  .  BigDecimal   defMoney1  )  { 
this  .  defMoney1  =  defMoney1  ; 
} 




public   java  .  math  .  BigDecimal   getDefMoney2  (  )  { 
return   defMoney2  ; 
} 





public   void   setDefMoney2  (  java  .  math  .  BigDecimal   defMoney2  )  { 
this  .  defMoney2  =  defMoney2  ; 
} 




public   java  .  math  .  BigDecimal   getDefMoney3  (  )  { 
return   defMoney3  ; 
} 





public   void   setDefMoney3  (  java  .  math  .  BigDecimal   defMoney3  )  { 
this  .  defMoney3  =  defMoney3  ; 
} 




public   java  .  util  .  Date   getDefDate1  (  )  { 
return   defDate1  ; 
} 





public   void   setDefDate1  (  java  .  util  .  Date   defDate1  )  { 
this  .  defDate1  =  defDate1  ; 
} 




public   java  .  util  .  Date   getDefDate2  (  )  { 
return   defDate2  ; 
} 





public   void   setDefDate2  (  java  .  util  .  Date   defDate2  )  { 
this  .  defDate2  =  defDate2  ; 
} 




public   java  .  util  .  Date   getDefDate3  (  )  { 
return   defDate3  ; 
} 





public   void   setDefDate3  (  java  .  util  .  Date   defDate3  )  { 
this  .  defDate3  =  defDate3  ; 
} 




public   java  .  lang  .  Boolean   getDefBool1  (  )  { 
return   defBool1  ; 
} 





public   void   setDefBool1  (  java  .  lang  .  Boolean   defBool1  )  { 
this  .  defBool1  =  defBool1  ; 
} 




public   java  .  lang  .  Boolean   getDefBool2  (  )  { 
return   defBool2  ; 
} 





public   void   setDefBool2  (  java  .  lang  .  Boolean   defBool2  )  { 
this  .  defBool2  =  defBool2  ; 
} 




public   java  .  lang  .  Boolean   getDefBool3  (  )  { 
return   defBool3  ; 
} 





public   void   setDefBool3  (  java  .  lang  .  Boolean   defBool3  )  { 
this  .  defBool3  =  defBool3  ; 
} 




public   com  .  jeecms  .  article  .  entity  .  Article   getNext  (  )  { 
return   next  ; 
} 





public   void   setNext  (  com  .  jeecms  .  article  .  entity  .  Article   next  )  { 
this  .  next  =  next  ; 
} 




public   com  .  jeecms  .  article  .  entity  .  Article   getPre  (  )  { 
return   pre  ; 
} 





public   void   setPre  (  com  .  jeecms  .  article  .  entity  .  Article   pre  )  { 
this  .  pre  =  pre  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  ContentCtg   getContentCtg  (  )  { 
return   contentCtg  ; 
} 





public   void   setContentCtg  (  com  .  jeecms  .  cms  .  entity  .  ContentCtg   contentCtg  )  { 
this  .  contentCtg  =  contentCtg  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   getAdminDisable  (  )  { 
return   adminDisable  ; 
} 





public   void   setAdminDisable  (  com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminDisable  )  { 
this  .  adminDisable  =  adminDisable  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsMemberGroup   getGroup  (  )  { 
return   group  ; 
} 





public   void   setGroup  (  com  .  jeecms  .  cms  .  entity  .  CmsMemberGroup   group  )  { 
this  .  group  =  group  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsChannel   getChannel  (  )  { 
return   channel  ; 
} 





public   void   setChannel  (  com  .  jeecms  .  cms  .  entity  .  CmsChannel   channel  )  { 
this  .  channel  =  channel  ; 
} 




public   com  .  jeecms  .  core  .  entity  .  Website   getWebsite  (  )  { 
return   website  ; 
} 





public   void   setWebsite  (  com  .  jeecms  .  core  .  entity  .  Website   website  )  { 
this  .  website  =  website  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsConfig   getConfig  (  )  { 
return   config  ; 
} 





public   void   setConfig  (  com  .  jeecms  .  cms  .  entity  .  CmsConfig   config  )  { 
this  .  config  =  config  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   getAdminCheck  (  )  { 
return   adminCheck  ; 
} 





public   void   setAdminCheck  (  com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminCheck  )  { 
this  .  adminCheck  =  adminCheck  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsAdmin   getAdminInput  (  )  { 
return   adminInput  ; 
} 





public   void   setAdminInput  (  com  .  jeecms  .  cms  .  entity  .  CmsAdmin   adminInput  )  { 
this  .  adminInput  =  adminInput  ; 
} 




public   com  .  jeecms  .  cms  .  entity  .  CmsMember   getMember  (  )  { 
return   member  ; 
} 





public   void   setMember  (  com  .  jeecms  .  cms  .  entity  .  CmsMember   member  )  { 
this  .  member  =  member  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  core  .  entity  .  Attachment  >  getAttachments  (  )  { 
return   attachments  ; 
} 





public   void   setAttachments  (  java  .  util  .  Set  <  com  .  jeecms  .  core  .  entity  .  Attachment  >  attachments  )  { 
this  .  attachments  =  attachments  ; 
} 




public   java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  CmsTopic  >  getTopics  (  )  { 
return   topics  ; 
} 





public   void   setTopics  (  java  .  util  .  Set  <  com  .  jeecms  .  cms  .  entity  .  CmsTopic  >  topics  )  { 
this  .  topics  =  topics  ; 
} 

public   boolean   equals  (  Object   obj  )  { 
if  (  null  ==  obj  )  return   false  ; 
if  (  !  (  obj   instanceof   com  .  jeecms  .  article  .  entity  .  Article  )  )  return   false  ;  else  { 
com  .  jeecms  .  article  .  entity  .  Article   article  =  (  com  .  jeecms  .  article  .  entity  .  Article  )  obj  ; 
if  (  null  ==  this  .  getId  (  )  ||  null  ==  article  .  getId  (  )  )  return   false  ;  else   return  (  this  .  getId  (  )  .  equals  (  article  .  getId  (  )  )  )  ; 
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

