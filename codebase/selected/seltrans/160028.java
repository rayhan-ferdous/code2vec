package   com  .  salas  .  bb  .  channelguide  ; 

import   java  .  net  .  *  ; 
import   java  .  net  .  URL  ; 
import   java  .  sql  .  Connection  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   net  .  sf  .  hibernate  .  HibernateException  ; 
import   com  .  salas  .  bb  .  core  .  *  ; 
import   de  .  nava  .  informa  .  core  .  *  ; 
import   de  .  nava  .  informa  .  impl  .  hibernate  .  *  ; 
import   de  .  nava  .  informa  .  impl  .  hibernate  .  SessionHandler  ; 
import   de  .  nava  .  informa  .  utils  .  *  ; 
import   de  .  nava  .  informa  .  utils  .  ChannelRegistry  ; 








public   class   InformaBackEnd   extends   RSSBackEnd   implements   ChannelObserverIF  { 

private   Logger   log  =  Logger  .  getLogger  (  this  .  getClass  (  )  .  getName  (  )  )  ; 

private   SessionHandler   handler  ; 

private   Connection   jdbcConnection  ; 

private   static   final   int   DEFAULT_MEMCHAN_UPDATE_MS  =  60000  ; 

private   static   final   int   DBG_MEMCHAN_UPDATE_MS  =  4000  ; 

private   ChannelBuilderIF   transientChanBuilder  ; 

private   ChannelRegistry   channelRegistry  ; 

int   defaultPollingInterval  =  180  ; 

public   InformaBackEnd  (  )  { 
super  (  )  ; 
jdbcConnection  =  null  ; 
transientChanBuilder  =  new   de  .  nava  .  informa  .  impl  .  basic  .  ChannelBuilder  (  )  ; 
channelRegistry  =  new   ChannelRegistry  (  transientChanBuilder  )  ; 
} 







protected   ChannelGuideEntry   mapChannel2CGE  (  Object   theChan  )  { 
return   ChannelGuideSet  .  SINGLETON  .  mapRSSChan2CGE  (  (  ChannelIF  )  theChan  )  ; 
} 







public   void   itemAdded  (  final   ItemIF   informaItem  )  { 
log  .  fine  (  "itemAdded called"  +  informaItem  )  ; 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
InformaCGE   theCGE  ; 
try  { 
theCGE  =  (  InformaCGE  )  mapChannel2CGE  (  informaItem  .  getChannel  (  )  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
log  .  fine  (  "Item Retrieved via Hibernate for which there is no CGE."  +  informaItem  )  ; 
return  ; 
} 
theCGE  .  addItem  (  informaItem  )  ; 
ArticleListModel   aListModl  =  ArticleListModel  .  SINGLETON  ; 
ChannelGuideEntry   selCGE  =  GlobalModel  .  SINGLETON  .  getSelectedCGE  (  )  ; 
log  .  finer  (  "itemAdded: cge = "  +  theCGE  +  ", sel CGE = "  +  selCGE  )  ; 
if  (  selCGE  ==  theCGE  &&  theCGE  .  getArticleCount  (  )  .  intValue  (  )  ==  1  )  GlobalController  .  SINGLETON  .  selectArticle  (  0  )  ; 
theCGE  .  reSortItems  (  )  ; 
theCGE  .  reloadCachedValues  (  )  ; 
} 
}  )  ; 
} 







public   void   channelRetrieved  (  final   ChannelIF   theChan  )  { 
InformaCGE   theCGE  ; 
try  { 
theCGE  =  (  InformaCGE  )  mapChannel2CGE  (  theChan  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
log  .  severe  (  "Channel Retrieved via Hibernate for which there is no CGE:"  +  theChan  )  ; 
return  ; 
} 
URL   siteURL  =  theChan  .  getSite  (  )  ; 
if  (  siteURL  !=  null  )  theCGE  .  updateHtmlURL  (  siteURL  .  toString  (  )  )  ; 
URL   xmlURL  =  theChan  .  getLocation  (  )  ; 
if  (  xmlURL  !=  null  )  theCGE  .  setXmlURL  (  xmlURL  .  toString  (  )  )  ; 
theCGE  .  getCGEContentHolder  (  )  .  setDescription  (  theChan  .  getDescription  (  )  )  ; 
log  .  config  (  "channelRetrieved called"  +  theChan  )  ; 
SwingUtilities  .  invokeLater  (  new   UpdateCgeUiThread  (  theChan  ,  theCGE  )  )  ; 
} 

private   final   class   UpdateCgeUiThread   extends   Thread  { 

private   InformaCGE   theCGE  ; 

private   ChannelIF   theChan  ; 

public   UpdateCgeUiThread  (  ChannelIF   aChan  ,  InformaCGE   aCGE  )  { 
super  (  )  ; 
theCGE  =  aCGE  ; 
theChan  =  aChan  ; 
} 

public   void   run  (  )  { 
theCGE  .  setTitle  (  theChan  .  getTitle  (  )  )  ; 
theCGE  .  incRetrievals  (  )  ; 
theCGE  .  reloadCachedValues  (  )  ; 
} 
} 







public   Object   addNewPersistentChannel  (  PersistentInformaChannelGuide   cg  ,  String   xmlURL  )  { 
Channel   achannel  ; 
PersistChanGrpMgr   chanGrpMgr  =  cg  .  getPersistChanGrpMgr  (  )  ; 
chanGrpMgr  .  deActivate  (  )  ; 
achannel  =  chanGrpMgr  .  addChannel  (  xmlURL  )  ; 
chanGrpMgr  .  activate  (  )  ; 
return   achannel  ; 
} 






public   void   activatePersistentChannels4CG  (  PersistentInformaChannelGuide   cg  )  { 
cg  .  chanGrpMgr  .  activate  (  )  ; 
} 

public   void   deActivatePersistenChannels4CG  (  PersistentInformaChannelGuide   cg  )  { 
cg  .  chanGrpMgr  .  deActivate  (  )  ; 
} 






public   void   connect  (  Connection   connection  )  { 
jdbcConnection  =  connection  ; 
try  { 
handler  =  SessionHandler  .  getInstance  (  )  ; 
handler  .  setConnection  (  jdbcConnection  )  ; 
}  catch  (  HibernateException   e  )  { 
handler  =  null  ; 
e  .  printStackTrace  (  )  ; 
} 
} 








public   Object   addNewMemoryChannel  (  String   xmlURL  ,  boolean   activate  )  { 
URL   theXML  =  null  ; 
ChannelIF   aChannel  ; 
try  { 
theXML  =  new   URL  (  xmlURL  )  ; 
aChannel  =  (  ChannelIF  )  channelRegistry  .  addChannel  (  theXML  ,  defaultPollingInterval  ,  activate  )  ; 
aChannel  .  addObserver  (  this  )  ; 
return   aChannel  ; 
}  catch  (  MalformedURLException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 






public   void   activateMemoryChannels4CG  (  ChannelGuide   cg  )  { 
Iterator   it  =  cg  .  getChannels  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
ChannelGuideEntry   entry  =  (  ChannelGuideEntry  )  it  .  next  (  )  ; 
ChannelIF   channel  =  (  ChannelIF  )  entry  .  rssBackEndhandle  (  )  ; 
int   chan_upd_ms  =  GlobalModel  .  SINGLETON  .  isDebugMode  (  )  ?  DBG_MEMCHAN_UPDATE_MS  :  DEFAULT_MEMCHAN_UPDATE_MS  ; 
if  (  channel   instanceof   de  .  nava  .  informa  .  impl  .  basic  .  Channel  )  channelRegistry  .  activateChannel  (  channel  ,  chan_upd_ms  )  ; 
} 
} 

public   SessionHandler   getSessionHandler  (  )  { 
return   handler  ; 
} 

public   Connection   getJDBCConnection  (  )  { 
return   jdbcConnection  ; 
} 
} 

