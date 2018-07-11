package   com  .  bocoon  .  entity  .  cms  .  main  ; 

import   static   com  .  bocoon  .  common  .  web  .  Constants  .  INDEX  ; 
import   static   com  .  bocoon  .  common  .  web  .  Constants  .  SPT  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  TreeSet  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   com  .  bocoon  .  app  .  cms  .  admin  .  staticpage  .  StaticPageUtils  ; 
import   com  .  bocoon  .  common  .  hibernate3  .  HibernateTree  ; 
import   com  .  bocoon  .  common  .  hibernate3  .  PriorityComparator  ; 
import   com  .  bocoon  .  common  .  hibernate3  .  PriorityInterface  ; 
import   com  .  bocoon  .  entity  .  cms  .  main  .  base  .  BaseChannel  ; 
import   com  .  bocoon  .  entity  .  cms  .  main  .  Channel  ; 
import   com  .  bocoon  .  entity  .  cms  .  main  .  ChannelExt  ; 
import   com  .  bocoon  .  entity  .  cms  .  main  .  ChannelTxt  ; 
import   com  .  bocoon  .  entity  .  cms  .  main  .  CmsModel  ; 







public   class   Channel   extends   BaseChannel   implements   HibernateTree  <  Integer  >  ,  PriorityInterface  { 

private   static   final   long   serialVersionUID  =  1L  ; 







public   static   enum   AfterCheckEnum  { 




CANNOT_UPDATE  , 


BACK_UPDATE  , 


KEEP_UPDATE 
} 






public   String   getUrl  (  )  { 
if  (  !  StringUtils  .  isBlank  (  getLink  (  )  )  )  { 
return   getLink  (  )  ; 
} 
if  (  getStaticChannel  (  )  )  { 
return   getUrlStatic  (  false  ,  1  )  ; 
}  else  { 
return   getUrlDynamic  (  null  )  ; 
} 
} 

public   String   getUrlWhole  (  )  { 
if  (  !  StringUtils  .  isBlank  (  getLink  (  )  )  )  { 
return   getLink  (  )  ; 
} 
if  (  getStaticChannel  (  )  )  { 
return   getUrlStatic  (  true  ,  1  )  ; 
}  else  { 
return   getUrlDynamic  (  true  )  ; 
} 
} 






public   String   getUrlStatic  (  )  { 
return   getUrlStatic  (  null  ,  1  )  ; 
} 

public   String   getUrlStatic  (  int   pageNo  )  { 
return   getUrlStatic  (  null  ,  pageNo  )  ; 
} 

public   String   getUrlStatic  (  Boolean   whole  ,  int   pageNo  )  { 
if  (  !  StringUtils  .  isBlank  (  getLink  (  )  )  )  { 
return   getLink  (  )  ; 
} 
CmsSite   site  =  getSite  (  )  ; 
StringBuilder   url  =  site  .  getUrlBuffer  (  false  ,  whole  ,  false  )  ; 
String   filename  =  getStaticFilenameByRule  (  )  ; 
if  (  !  StringUtils  .  isBlank  (  filename  )  )  { 
if  (  pageNo  >  1  )  { 
int   index  =  filename  .  indexOf  (  "."  ,  filename  .  lastIndexOf  (  "/"  )  )  ; 
if  (  index  !=  -  1  )  { 
url  .  append  (  filename  .  substring  (  0  ,  index  )  )  ; 
url  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
url  .  append  (  filename  .  substring  (  index  )  )  ; 
}  else  { 
url  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
} 
}  else  { 
if  (  getAccessByDir  (  )  )  { 
url  .  append  (  filename  .  substring  (  0  ,  filename  .  lastIndexOf  (  "/"  )  +  1  )  )  ; 
}  else  { 
url  .  append  (  filename  )  ; 
} 
} 
}  else  { 
url  .  append  (  SPT  )  .  append  (  getPath  (  )  )  ; 
if  (  pageNo  >  1  )  { 
url  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
url  .  append  (  site  .  getStaticSuffix  (  )  )  ; 
}  else  { 
if  (  getHasContent  (  )  )  { 
url  .  append  (  SPT  )  ; 
}  else  { 
url  .  append  (  site  .  getStaticSuffix  (  )  )  ; 
} 
} 
} 
return   url  .  toString  (  )  ; 
} 

public   String   getStaticFilename  (  int   pageNo  )  { 
CmsSite   site  =  getSite  (  )  ; 
StringBuilder   url  =  new   StringBuilder  (  )  ; 
String   staticDir  =  site  .  getStaticDir  (  )  ; 
if  (  !  StringUtils  .  isBlank  (  staticDir  )  )  { 
url  .  append  (  staticDir  )  ; 
} 
String   filename  =  getStaticFilenameByRule  (  )  ; 
if  (  !  StringUtils  .  isBlank  (  filename  )  )  { 
int   index  =  filename  .  indexOf  (  "."  ,  filename  .  lastIndexOf  (  "/"  )  )  ; 
if  (  pageNo  >  1  )  { 
if  (  index  !=  -  1  )  { 
url  .  append  (  filename  .  substring  (  0  ,  index  )  )  .  append  (  "_"  )  .  append  (  pageNo  )  .  append  (  filename  .  substring  (  index  )  )  ; 
}  else  { 
url  .  append  (  filename  )  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
} 
}  else  { 
url  .  append  (  filename  )  ; 
} 
}  else  { 
url  .  append  (  SPT  )  .  append  (  getPath  (  )  )  ; 
String   suffix  =  site  .  getStaticSuffix  (  )  ; 
if  (  getHasContent  (  )  )  { 
url  .  append  (  SPT  )  .  append  (  INDEX  )  ; 
if  (  pageNo  >  1  )  { 
url  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
} 
url  .  append  (  suffix  )  ; 
}  else  { 
if  (  pageNo  >  1  )  { 
url  .  append  (  "_"  )  .  append  (  pageNo  )  ; 
} 
url  .  append  (  suffix  )  ; 
} 
} 
return   url  .  toString  (  )  ; 
} 

public   String   getStaticFilenameByRule  (  )  { 
String   rule  =  getChannelRule  (  )  ; 
if  (  StringUtils  .  isBlank  (  rule  )  )  { 
return   null  ; 
} 
CmsModel   model  =  getModel  (  )  ; 
String   url  =  StaticPageUtils  .  staticUrlRule  (  rule  ,  model  .  getId  (  )  ,  model  .  getPath  (  )  ,  getId  (  )  ,  getPath  (  )  ,  null  ,  null  )  ; 
return   url  ; 
} 






public   String   getUrlDynamic  (  )  { 
return   getUrlDynamic  (  null  )  ; 
} 

public   String   getUrlDynamic  (  Boolean   whole  )  { 
if  (  !  StringUtils  .  isBlank  (  getLink  (  )  )  )  { 
return   getLink  (  )  ; 
} 
CmsSite   site  =  getSite  (  )  ; 
StringBuilder   url  =  site  .  getUrlBuffer  (  true  ,  whole  ,  false  )  ; 
url  .  append  (  SPT  )  .  append  (  getPath  (  )  )  ; 
if  (  getHasContent  (  )  )  { 
url  .  append  (  SPT  )  .  append  (  INDEX  )  ; 
} 
url  .  append  (  site  .  getDynamicSuffix  (  )  )  ; 
return   url  .  toString  (  )  ; 
} 






public   List  <  Channel  >  getNodeList  (  )  { 
LinkedList  <  Channel  >  list  =  new   LinkedList  <  Channel  >  (  )  ; 
Channel   node  =  this  ; 
while  (  node  !=  null  )  { 
list  .  addFirst  (  node  )  ; 
node  =  node  .  getParent  (  )  ; 
} 
return   list  ; 
} 






public   Integer  [  ]  getNodeIds  (  )  { 
List  <  Channel  >  channels  =  getNodeList  (  )  ; 
Integer  [  ]  ids  =  new   Integer  [  channels  .  size  (  )  ]  ; 
int   i  =  0  ; 
for  (  Channel   c  :  channels  )  { 
ids  [  i  ++  ]  =  c  .  getId  (  )  ; 
} 
return   ids  ; 
} 






public   int   getDeep  (  )  { 
int   deep  =  0  ; 
Channel   parent  =  getParent  (  )  ; 
while  (  parent  !=  null  )  { 
deep  ++  ; 
parent  =  parent  .  getParent  (  )  ; 
} 
return   deep  ; 
} 






public   Byte   getFinalStepExtends  (  )  { 
Byte   step  =  getFinalStep  (  )  ; 
if  (  step  ==  null  )  { 
Channel   parent  =  getParent  (  )  ; 
if  (  parent  ==  null  )  { 
return   getSite  (  )  .  getFinalStep  (  )  ; 
}  else  { 
return   parent  .  getFinalStepExtends  (  )  ; 
} 
}  else  { 
return   step  ; 
} 
} 

public   Byte   getAfterCheck  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getAfterCheck  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   AfterCheckEnum   getAfterCheckEnum  (  )  { 
Byte   after  =  getChannelExt  (  )  .  getAfterCheck  (  )  ; 
Channel   channel  =  getParent  (  )  ; 
while  (  after  ==  null  &&  channel  !=  null  )  { 
after  =  channel  .  getAfterCheck  (  )  ; 
channel  =  channel  .  getParent  (  )  ; 
} 
if  (  after  ==  null  )  { 
after  =  getSite  (  )  .  getAfterCheck  (  )  ; 
} 
if  (  after  ==  1  )  { 
return   AfterCheckEnum  .  CANNOT_UPDATE  ; 
}  else   if  (  after  ==  2  )  { 
return   AfterCheckEnum  .  BACK_UPDATE  ; 
}  else   if  (  after  ==  3  )  { 
return   AfterCheckEnum  .  KEEP_UPDATE  ; 
}  else  { 
return   AfterCheckEnum  .  CANNOT_UPDATE  ; 
} 
} 






public   List  <  Channel  >  getListForSelect  (  Set  <  Channel  >  rights  ,  boolean   hasContentOnly  )  { 
return   getListForSelect  (  rights  ,  null  ,  hasContentOnly  )  ; 
} 

public   List  <  Channel  >  getListForSelect  (  Set  <  Channel  >  rights  ,  Channel   exclude  ,  boolean   hasContentOnly  )  { 
List  <  Channel  >  list  =  new   ArrayList  <  Channel  >  (  (  getRgt  (  )  -  getLft  (  )  )  /  2  )  ; 
addChildToList  (  list  ,  this  ,  rights  ,  exclude  ,  hasContentOnly  )  ; 
return   list  ; 
} 








public   static   List  <  Channel  >  getListForSelect  (  List  <  Channel  >  topList  ,  Set  <  Channel  >  rights  ,  boolean   hasContentOnly  )  { 
return   getListForSelect  (  topList  ,  rights  ,  null  ,  hasContentOnly  )  ; 
} 

public   static   List  <  Channel  >  getListForSelect  (  List  <  Channel  >  topList  ,  Set  <  Channel  >  rights  ,  Channel   exclude  ,  boolean   hasContentOnly  )  { 
List  <  Channel  >  list  =  new   ArrayList  <  Channel  >  (  )  ; 
for  (  Channel   c  :  topList  )  { 
addChildToList  (  list  ,  c  ,  rights  ,  exclude  ,  hasContentOnly  )  ; 
} 
return   list  ; 
} 











private   static   void   addChildToList  (  List  <  Channel  >  list  ,  Channel   channel  ,  Set  <  Channel  >  rights  ,  Channel   exclude  ,  boolean   hasContentOnly  )  { 
if  (  (  rights  !=  null  &&  !  rights  .  contains  (  channel  )  )  ||  (  exclude  !=  null  &&  exclude  .  equals  (  channel  )  )  )  { 
return  ; 
} 
list  .  add  (  channel  )  ; 
Set  <  Channel  >  child  =  channel  .  getChild  (  )  ; 
for  (  Channel   c  :  child  )  { 
if  (  hasContentOnly  )  { 
if  (  c  .  getHasContent  (  )  )  { 
addChildToList  (  list  ,  c  ,  rights  ,  exclude  ,  hasContentOnly  )  ; 
} 
}  else  { 
addChildToList  (  list  ,  c  ,  rights  ,  exclude  ,  hasContentOnly  )  ; 
} 
} 
} 

public   String   getTplChannelOrDef  (  )  { 
String   tpl  =  getTplChannel  (  )  ; 
if  (  !  StringUtils  .  isBlank  (  tpl  )  )  { 
return   tpl  ; 
}  else  { 
String   sol  =  getSite  (  )  .  getSolutionPath  (  )  ; 
return   getModel  (  )  .  getTplChannel  (  sol  ,  true  )  ; 
} 
} 

public   String   getTplContentOrDef  (  )  { 
String   tpl  =  getTplContent  (  )  ; 
if  (  !  StringUtils  .  isBlank  (  tpl  )  )  { 
return   tpl  ; 
}  else  { 
String   sol  =  getSite  (  )  .  getSolutionPath  (  )  ; 
return   getModel  (  )  .  getTplContent  (  sol  ,  true  )  ; 
} 
} 

public   Integer  [  ]  getUserIds  (  )  { 
Set  <  CmsUser  >  users  =  getUsers  (  )  ; 
return   CmsUser  .  fetchIds  (  users  )  ; 
} 

public   void   addToViewGroups  (  CmsGroup   group  )  { 
Set  <  CmsGroup  >  groups  =  getViewGroups  (  )  ; 
if  (  groups  ==  null  )  { 
groups  =  new   TreeSet  <  CmsGroup  >  (  new   PriorityComparator  (  )  )  ; 
setViewGroups  (  groups  )  ; 
} 
groups  .  add  (  group  )  ; 
group  .  getViewChannels  (  )  .  add  (  this  )  ; 
} 

public   void   addToContriGroups  (  CmsGroup   group  )  { 
Set  <  CmsGroup  >  groups  =  getContriGroups  (  )  ; 
if  (  groups  ==  null  )  { 
groups  =  new   TreeSet  <  CmsGroup  >  (  new   PriorityComparator  (  )  )  ; 
setContriGroups  (  groups  )  ; 
} 
groups  .  add  (  group  )  ; 
group  .  getContriChannels  (  )  .  add  (  this  )  ; 
} 

public   void   addToUsers  (  CmsUser   user  )  { 
Set  <  CmsUser  >  set  =  getUsers  (  )  ; 
if  (  set  ==  null  )  { 
set  =  new   TreeSet  <  CmsUser  >  (  new   PriorityComparator  (  )  )  ; 
setUsers  (  set  )  ; 
} 
set  .  add  (  user  )  ; 
user  .  addToChannels  (  this  )  ; 
} 

public   void   init  (  )  { 
if  (  getPriority  (  )  ==  null  )  { 
setPriority  (  10  )  ; 
} 
if  (  getDisplay  (  )  ==  null  )  { 
setDisplay  (  true  )  ; 
} 
} 

public   String   getName  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getName  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getStaticChannel  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getStaticChannel  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getStaticContent  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getStaticContent  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getAccessByDir  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getAccessByDir  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getListChild  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getListChild  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getPageSize  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getPageSize  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getChannelRule  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getChannelRule  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getContentRule  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getContentRule  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Byte   getFinalStep  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getFinalStep  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getLink  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getLink  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getTplChannel  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTplChannel  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getTplContent  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTplContent  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getHasTitleImg  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getHasTitleImg  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getHasContentImg  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getHasContentImg  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getTitleImgWidth  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTitleImgWidth  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getTitleImgHeight  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTitleImgHeight  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getContentImgWidth  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getContentImgWidth  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getContentImgHeight  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getContentImgHeight  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getTitleImg  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTitleImg  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getContentImg  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getContentImg  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getTitle  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getTitle  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getKeywords  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getKeywords  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   String   getDescription  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getDescription  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Integer   getCommentControl  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getCommentControl  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getAllowUpdown  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getAllowUpdown  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   Boolean   getBlank  (  )  { 
ChannelExt   ext  =  getChannelExt  (  )  ; 
if  (  ext  !=  null  )  { 
return   ext  .  getBlank  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   String   getTxt  (  )  { 
ChannelTxt   txt  =  getChannelTxt  (  )  ; 
if  (  txt  !=  null  )  { 
return   txt  .  getTxt  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   String   getTxt1  (  )  { 
ChannelTxt   txt  =  getChannelTxt  (  )  ; 
if  (  txt  !=  null  )  { 
return   txt  .  getTxt1  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   String   getTxt2  (  )  { 
ChannelTxt   txt  =  getChannelTxt  (  )  ; 
if  (  txt  !=  null  )  { 
return   txt  .  getTxt2  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   String   getTxt3  (  )  { 
ChannelTxt   txt  =  getChannelTxt  (  )  ; 
if  (  txt  !=  null  )  { 
return   txt  .  getTxt3  (  )  ; 
}  else  { 
return   null  ; 
} 
} 

public   ChannelTxt   getChannelTxt  (  )  { 
Set  <  ChannelTxt  >  set  =  getChannelTxtSet  (  )  ; 
if  (  set  !=  null  &&  set  .  size  (  )  >  0  )  { 
return   set  .  iterator  (  )  .  next  (  )  ; 
}  else  { 
return   null  ; 
} 
} 






public   String   getTreeCondition  (  )  { 
return  "bean.site.id="  +  getSite  (  )  .  getId  (  )  ; 
} 




public   Integer   getParentId  (  )  { 
Channel   parent  =  getParent  (  )  ; 
if  (  parent  !=  null  )  { 
return   parent  .  getId  (  )  ; 
}  else  { 
return   null  ; 
} 
} 




public   String   getLftName  (  )  { 
return   DEF_LEFT_NAME  ; 
} 




public   String   getParentName  (  )  { 
return   DEF_PARENT_NAME  ; 
} 




public   String   getRgtName  (  )  { 
return   DEF_RIGHT_NAME  ; 
} 

public   static   Integer  [  ]  fetchIds  (  Collection  <  Channel  >  channels  )  { 
if  (  channels  ==  null  )  { 
return   null  ; 
} 
Integer  [  ]  ids  =  new   Integer  [  channels  .  size  (  )  ]  ; 
int   i  =  0  ; 
for  (  Channel   c  :  channels  )  { 
ids  [  i  ++  ]  =  c  .  getId  (  )  ; 
} 
return   ids  ; 
} 

public   Channel  (  )  { 
super  (  )  ; 
} 




public   Channel  (  java  .  lang  .  Integer   id  )  { 
super  (  id  )  ; 
} 




public   Channel  (  java  .  lang  .  Integer   id  ,  com  .  bocoon  .  entity  .  cms  .  main  .  CmsSite   site  ,  com  .  bocoon  .  entity  .  cms  .  main  .  CmsModel   model  ,  java  .  lang  .  Integer   lft  ,  java  .  lang  .  Integer   rgt  ,  java  .  lang  .  Integer   priority  ,  java  .  lang  .  Boolean   hasContent  ,  java  .  lang  .  Boolean   display  )  { 
super  (  id  ,  site  ,  model  ,  lft  ,  rgt  ,  priority  ,  hasContent  ,  display  )  ; 
} 
} 

