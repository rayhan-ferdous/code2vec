package   jreader  ; 

import   java  .  io  .  IOException  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   jreader  .  gui  .  GUI  ; 







public   class   JReader  { 


public   static   final   int   HISTORY_SIZE  =  25  ; 




private   static   Config   config  =  new   Config  (  )  ; 




private   static   Channels   channels  =  new   Channels  (  )  ; 




private   static   List  <  Channel  >  visibleChannels  =  new   ArrayList  <  Channel  >  (  )  ; 




private   static   List  <  Item  >  items  =  new   ArrayList  <  Item  >  (  )  ; 





private   static   List  <  HistoryList  <  Preview  >  >  previewTabs  =  new   ArrayList  <  HistoryList  <  Preview  >  >  (  HISTORY_SIZE  )  ; 




private   static   List  <  String  >  tags  =  new   LinkedList  <  String  >  (  )  ; 

private   static   ChannelComparator   channelComparator  =  new   ChannelComparator  (  )  ; 

private   static   ItemComparator   itemComparator  =  new   ItemComparator  (  )  ; 







private   static   String   currentFilter  =  "unread"  ; 


private   JReader  (  )  { 
} 




public   static   void   main  (  String  [  ]  args  )  { 
addNewPreviewTab  (  )  ; 
channels  .  removeItems  (  )  ; 
updateTagsList  (  )  ; 
selectTag  (  "all"  )  ; 
selectUnread  (  )  ; 
if  (  args  .  length  >  0  &&  args  [  0  ]  .  equals  (  "-t"  )  )  { 
TextUI  .  run  (  )  ; 
}  else  { 
GUI  .  run  (  )  ; 
} 
channels  .  write  (  )  ; 
} 




public   static   Config   getConfig  (  )  { 
return   config  ; 
} 




public   static   Channel   getChannel  (  String   channelId  )  { 
return   channels  .  getChannel  (  channelId  )  ; 
} 




public   static   Channel   getChannel  (  int   index  )  { 
return   visibleChannels  .  get  (  index  )  ; 
} 




public   static   List  <  Channel  >  getVisibleChannels  (  )  { 
return   visibleChannels  ; 
} 




public   static   List  <  Item  >  getItems  (  )  { 
return   items  ; 
} 




public   static   HistoryList  <  Preview  >  getPreview  (  int   tabIndex  )  { 
return   previewTabs  .  get  (  tabIndex  )  ; 
} 




public   static   List  <  String  >  getTags  (  )  { 
return   tags  ; 
} 















public   static   void   addChannel  (  String   siteURL  ,  String   channelTags  )  throws   LinkNotFoundException  ,  MalformedURLException  ,  SAXException  ,  IOException  { 
Channel   newChannel  =  ChannelFactory  .  getChannelFromSite  (  siteURL  )  ; 
newChannel  .  setTags  (  parseTags  (  channelTags  )  )  ; 
channels  .  add  (  newChannel  ,  ChannelFactory  .  getDownloadedItems  (  )  )  ; 
visibleChannels  .  add  (  newChannel  )  ; 
Collections  .  sort  (  visibleChannels  ,  channelComparator  )  ; 
for  (  String   tag  :  newChannel  .  getTags  (  )  )  { 
if  (  !  tags  .  contains  (  tag  )  )  { 
tags  .  add  (  tag  )  ; 
} 
} 
Collections  .  sort  (  tags  )  ; 
channels  .  write  (  )  ; 
} 








public   static   Preview   previousItem  (  int   tabIndex  )  { 
return   previewTabs  .  get  (  tabIndex  )  .  previous  (  )  ; 
} 







public   static   Preview   nextItem  (  int   tabIndex  )  { 
return   previewTabs  .  get  (  tabIndex  )  .  next  (  )  ; 
} 









public   static   boolean   nextUnread  (  int   tabIndex  )  { 
Item   nextUnreadItem  =  new   Item  (  ""  ,  ""  )  ; 
Date   beginningOfTime  =  new   Date  (  0  )  ; 
Date   endOfTime  =  new   Date  (  )  ; 
try  { 
endOfTime  =  new   SimpleDateFormat  (  "yyyy"  )  .  parse  (  "9999"  )  ; 
}  catch  (  ParseException   pe  )  { 
} 
if  (  config  .  getSortByNewest  (  )  )  { 
nextUnreadItem  .  setDate  (  beginningOfTime  )  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
if  (  channel  .  getUnreadItemsCount  (  )  >  0  )  { 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
if  (  !  item  .  isRead  (  )  )  { 
if  (  item  .  getDate  (  )  .  after  (  nextUnreadItem  .  getDate  (  )  )  )  { 
nextUnreadItem  =  item  ; 
} 
} 
} 
} 
} 
}  else  { 
nextUnreadItem  .  setDate  (  endOfTime  )  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
if  (  channel  .  getUnreadItemsCount  (  )  >  0  )  { 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
if  (  !  item  .  isRead  (  )  )  { 
if  (  item  .  getDate  (  )  .  before  (  nextUnreadItem  .  getDate  (  )  )  )  { 
nextUnreadItem  =  item  ; 
} 
} 
} 
} 
} 
} 
if  (  nextUnreadItem  .  getDate  (  )  .  equals  (  beginningOfTime  )  ||  nextUnreadItem  .  getDate  (  )  .  equals  (  endOfTime  )  )  { 
return   false  ; 
} 
nextUnreadItem  .  markAsRead  (  )  ; 
updateUnreadItemsCount  (  channels  .  getChannel  (  nextUnreadItem  .  getChannelId  (  )  )  )  ; 
previewTabs  .  get  (  tabIndex  )  .  setCurrent  (  new   Preview  (  nextUnreadItem  )  )  ; 
channels  .  write  (  )  ; 
return   true  ; 
} 






public   static   void   selectItem  (  Item   item  ,  int   tabIndex  )  { 
item  .  markAsRead  (  )  ; 
previewTabs  .  get  (  tabIndex  )  .  setCurrent  (  new   Preview  (  item  )  )  ; 
if  (  updateUnreadItemsCount  (  channels  .  getChannel  (  item  .  getChannelId  (  )  )  )  )  { 
channels  .  write  (  )  ; 
} 
} 







public   static   void   selectChannel  (  int   index  ,  int   tabIndex  )  { 
items  =  channels  .  getItems  (  visibleChannels  .  get  (  index  )  .  getId  (  )  )  ; 
Collections  .  sort  (  items  ,  itemComparator  )  ; 
previewTabs  .  get  (  tabIndex  )  .  setCurrent  (  new   Preview  (  visibleChannels  .  get  (  index  )  )  )  ; 
currentFilter  =  visibleChannels  .  get  (  index  )  .  getId  (  )  ; 
} 




public   static   void   markChannelAsRead  (  Channel   channel  )  { 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
item  .  markAsRead  (  )  ; 
} 
if  (  updateUnreadItemsCount  (  channel  )  )  { 
channels  .  write  (  )  ; 
} 
} 




public   static   void   selectAll  (  )  { 
items  =  new   ArrayList  <  Item  >  (  )  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
items  .  add  (  item  )  ; 
} 
} 
Collections  .  sort  (  items  ,  itemComparator  )  ; 
currentFilter  =  "all"  ; 
} 




public   static   void   selectUnread  (  )  { 
items  =  new   ArrayList  <  Item  >  (  )  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
if  (  channel  .  getUnreadItemsCount  (  )  >  0  )  { 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
if  (  !  item  .  isRead  (  )  )  { 
items  .  add  (  item  )  ; 
} 
} 
} 
} 
Collections  .  sort  (  items  ,  itemComparator  )  ; 
currentFilter  =  "unread"  ; 
} 









public   static   void   selectTag  (  String   tag  )  { 
tag  =  tag  .  trim  (  )  ; 
visibleChannels  =  new   ArrayList  <  Channel  >  (  )  ; 
if  (  tag  .  equals  (  "all"  )  )  { 
visibleChannels  =  channels  .  getChannels  (  )  ; 
}  else   if  (  tag  .  equals  (  ""  )  ||  tag  ==  null  )  { 
for  (  Channel   channel  :  channels  .  getChannels  (  )  )  { 
if  (  channel  .  getTags  (  )  .  size  (  )  ==  0  )  { 
visibleChannels  .  add  (  channel  )  ; 
} 
} 
}  else  { 
for  (  Channel   channel  :  channels  .  getChannels  (  )  )  { 
if  (  channel  .  containsTag  (  tag  )  )  { 
visibleChannels  .  add  (  channel  )  ; 
} 
} 
} 
Collections  .  sort  (  visibleChannels  ,  channelComparator  )  ; 
} 











public   static   int   updateChannel  (  Channel   channel  )  throws   SAXException  ,  IOException  { 
Channel   newChannel  =  ChannelFactory  .  getChannelFromXML  (  channel  .  getChannelURL  (  )  )  ; 
if  (  channel  .  getIconPath  (  )  ==  null  )  { 
String   iconPath  ; 
try  { 
iconPath  =  ChannelFactory  .  extractIconPath  (  channel  .  getLink  (  )  )  ; 
channel  .  setIconPath  (  iconPath  )  ; 
}  catch  (  IOException   ioe  )  { 
} 
} 
channel  .  setTitle  (  newChannel  .  getTitle  (  )  )  ; 
channel  .  setLink  (  newChannel  .  getLink  (  )  )  ; 
channel  .  setDescription  (  newChannel  .  getDescription  (  )  )  ; 
channel  .  setImageURL  (  newChannel  .  getImageURL  (  )  )  ; 
channel  .  setImageTitle  (  newChannel  .  getImageTitle  (  )  )  ; 
channel  .  setImageLink  (  newChannel  .  getImageLink  (  )  )  ; 
int   newItemsCount  =  0  ; 
for  (  Item   updatedItem  :  ChannelFactory  .  getDownloadedItems  (  )  )  { 
boolean   itemAlreadyExists  =  false  ; 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
if  (  updatedItem  .  equals  (  item  )  )  { 
itemAlreadyExists  =  true  ; 
break  ; 
} 
} 
if  (  !  itemAlreadyExists  )  { 
channels  .  addItem  (  updatedItem  )  ; 
channel  .  addItem  (  updatedItem  .  getId  (  )  )  ; 
updateItemsList  (  updatedItem  )  ; 
newItemsCount  ++  ; 
} 
} 
if  (  updateUnreadItemsCount  (  channel  )  )  { 
channels  .  write  (  )  ; 
} 
return   newItemsCount  ; 
} 




public   static   void   editTags  (  Channel   channel  ,  String   channelTags  )  { 
channel  .  setTags  (  parseTags  (  channelTags  )  )  ; 
updateTagsList  (  )  ; 
Collections  .  sort  (  tags  )  ; 
channels  .  write  (  )  ; 
} 






public   static   void   removeChannel  (  int   index  )  { 
List  <  Integer  >  indToRemove  =  new   ArrayList  <  Integer  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  items  .  size  (  )  ;  i  ++  )  { 
for  (  Item   channelItem  :  channels  .  getItems  (  visibleChannels  .  get  (  index  )  .  getId  (  )  )  )  { 
if  (  items  .  get  (  i  )  .  equals  (  channelItem  )  )  { 
indToRemove  .  add  (  i  )  ; 
} 
} 
} 
for  (  int   i  =  0  ;  i  <  indToRemove  .  size  (  )  ;  i  ++  )  { 
items  .  remove  (  (  int  )  indToRemove  .  get  (  i  )  )  ; 
for  (  int   j  =  i  ;  j  <  indToRemove  .  size  (  )  ;  j  ++  )  { 
indToRemove  .  set  (  j  ,  indToRemove  .  get  (  j  )  -  1  )  ; 
} 
} 
for  (  String   itemId  :  visibleChannels  .  get  (  index  )  .  getItems  (  )  )  { 
channels  .  removeItem  (  itemId  )  ; 
} 
channels  .  removeChannel  (  visibleChannels  .  get  (  index  )  .  getId  (  )  )  ; 
visibleChannels  .  remove  (  index  )  ; 
updateTagsList  (  )  ; 
channels  .  write  (  )  ; 
} 







public   static   void   removeItem  (  Item   item  )  { 
items  .  remove  (  item  )  ; 
getChannel  (  item  .  getChannelId  (  )  )  .  getItems  (  )  .  remove  (  item  .  getId  (  )  )  ; 
channels  .  removeItem  (  item  .  getId  (  )  )  ; 
} 












public   static   int   importChannelList  (  String   fileLocation  )  throws   IOException  ,  SAXException  { 
List  <  Channel  >  importedChannels  =  ImportExport  .  getChannelsFromFile  (  fileLocation  )  ; 
for  (  Channel   channel  :  importedChannels  )  { 
if  (  !  channels  .  containsChannel  (  channel  .  getId  (  )  )  )  { 
channels  .  add  (  channel  )  ; 
visibleChannels  .  add  (  channel  )  ; 
} 
for  (  String   tag  :  channel  .  getTags  (  )  )  { 
if  (  !  tags  .  contains  (  tag  )  )  { 
tags  .  add  (  tag  )  ; 
} 
} 
} 
Collections  .  sort  (  tags  )  ; 
channels  .  write  (  )  ; 
return   importedChannels  .  size  (  )  ; 
} 






public   static   void   exportChannelList  (  String   fileLocation  )  throws   IOException  { 
ImportExport  .  writeChannelsToFile  (  new   LinkedList  <  Channel  >  (  channels  .  getChannels  (  )  )  ,  fileLocation  )  ; 
} 

















public   static   boolean   search  (  boolean   caseSensitive  ,  boolean   titlesOnly  ,  int   scope  ,  String   needle  )  { 
List  <  Item  >  haystack  =  new   LinkedList  <  Item  >  (  )  ; 
List  <  Item  >  matchingItems  =  new   LinkedList  <  Item  >  (  )  ; 
String   itemContents  =  ""  ; 
if  (  scope  ==  0  )  { 
haystack  .  addAll  (  items  )  ; 
}  else   if  (  scope  ==  1  )  { 
for  (  Channel   channel  :  visibleChannels  )  { 
haystack  .  addAll  (  channels  .  getItems  (  channel  .  getId  (  )  )  )  ; 
} 
}  else   if  (  scope  ==  2  )  { 
for  (  Channel   channel  :  channels  .  getChannels  (  )  )  { 
haystack  .  addAll  (  channels  .  getItems  (  channel  .  getId  (  )  )  )  ; 
} 
} 
for  (  Item   item  :  haystack  )  { 
itemContents  =  item  .  getTitle  (  )  ; 
if  (  !  titlesOnly  )  { 
if  (  item  .  getDescription  (  )  !=  null  )  { 
itemContents  =  itemContents  .  concat  (  item  .  getDescription  (  )  )  ; 
} 
} 
if  (  !  caseSensitive  )  { 
itemContents  =  itemContents  .  toLowerCase  (  )  ; 
} 
if  (  itemContents  .  contains  (  needle  )  )  { 
matchingItems  .  add  (  item  )  ; 
} 
} 
if  (  matchingItems  .  size  (  )  >  0  )  { 
items  =  matchingItems  ; 
return   true  ; 
} 
return   false  ; 
} 




public   static   int   getUnreadItemsCount  (  )  { 
int   unreadItemsCount  =  0  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
unreadItemsCount  +=  channel  .  getUnreadItemsCount  (  )  ; 
} 
return   unreadItemsCount  ; 
} 




public   static   int   getItemsCount  (  )  { 
int   itemsCount  =  0  ; 
for  (  Channel   channel  :  visibleChannels  )  { 
itemsCount  +=  channel  .  getItems  (  )  .  size  (  )  ; 
} 
return   itemsCount  ; 
} 

public   static   void   addNewPreviewTab  (  )  { 
previewTabs  .  add  (  new   HistoryList  <  Preview  >  (  HISTORY_SIZE  )  )  ; 
} 

public   static   void   removePreviewTab  (  int   tabIndex  )  { 
previewTabs  .  remove  (  tabIndex  )  ; 
} 








public   static   List  <  String  >  parseTags  (  String   tagsAsString  )  { 
List  <  String  >  tagsAsList  =  new   LinkedList  <  String  >  (  )  ; 
if  (  tagsAsString  !=  null  )  { 
tagsAsString  =  tagsAsString  .  trim  (  )  .  toLowerCase  (  )  ; 
if  (  !  ""  .  equals  (  tagsAsString  )  )  { 
tagsAsString  =  tagsAsString  .  replace  (  ", "  ,  ","  )  ; 
tagsAsString  =  tagsAsString  .  replace  (  ","  ,  " "  )  ; 
String  [  ]  tagsAsStringArray  =  tagsAsString  .  split  (  " "  )  ; 
for  (  String   tag  :  tagsAsStringArray  )  { 
tagsAsList  .  add  (  tag  )  ; 
} 
} 
} 
return   tagsAsList  ; 
} 







private   static   boolean   updateUnreadItemsCount  (  Channel   channel  )  { 
int   oldCount  =  channel  .  getUnreadItemsCount  (  )  ; 
channel  .  setUnreadItemsCount  (  0  )  ; 
for  (  Item   item  :  channels  .  getItems  (  channel  .  getId  (  )  )  )  { 
if  (  !  item  .  isRead  (  )  )  { 
channel  .  setUnreadItemsCount  (  channel  .  getUnreadItemsCount  (  )  +  1  )  ; 
} 
} 
if  (  oldCount  ==  channel  .  getUnreadItemsCount  (  )  )  { 
return   false  ; 
}  else  { 
return   true  ; 
} 
} 




private   static   void   updateTagsList  (  )  { 
tags  .  clear  (  )  ; 
for  (  Channel   channel  :  channels  .  getChannels  (  )  )  { 
for  (  String   tag  :  channel  .  getTags  (  )  )  { 
if  (  !  tags  .  contains  (  tag  )  )  { 
tags  .  add  (  tag  )  ; 
} 
} 
} 
Collections  .  sort  (  tags  )  ; 
} 









private   static   void   updateItemsList  (  Item   item  )  { 
if  (  currentFilter  .  equals  (  "unread"  )  )  { 
if  (  !  item  .  isRead  (  )  )  { 
items  .  add  (  item  )  ; 
} 
}  else   if  (  currentFilter  .  equals  (  "all"  )  )  { 
items  .  add  (  item  )  ; 
}  else  { 
if  (  currentFilter  .  equals  (  item  .  getChannelId  (  )  )  )  { 
items  .  add  (  item  )  ; 
} 
} 
Collections  .  sort  (  items  ,  itemComparator  )  ; 
} 
} 

