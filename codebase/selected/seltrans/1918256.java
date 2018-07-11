package   com  .  aetrion  .  flickr  .  photos  ; 

import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  text  .  DateFormat  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   org  .  w3c  .  dom  .  Element  ; 
import   org  .  w3c  .  dom  .  NodeList  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   com  .  aetrion  .  zooomr  .  FlickrException  ; 
import   com  .  aetrion  .  zooomr  .  Parameter  ; 
import   com  .  aetrion  .  zooomr  .  REST  ; 
import   com  .  aetrion  .  zooomr  .  RequestContext  ; 
import   com  .  aetrion  .  zooomr  .  Response  ; 
import   com  .  aetrion  .  zooomr  .  Transport  ; 
import   com  .  aetrion  .  flickr  .  auth  .  AuthUtilities  ; 
import   com  .  aetrion  .  flickr  .  people  .  User  ; 
import   com  .  aetrion  .  flickr  .  photos  .  geo  .  GeoInterface  ; 
import   com  .  aetrion  .  flickr  .  util  .  IOUtilities  ; 
import   com  .  aetrion  .  flickr  .  util  .  StringUtilities  ; 
import   com  .  aetrion  .  flickr  .  util  .  XMLUtilities  ; 







public   class   PhotosInterface  { 

public   static   final   String   METHOD_ADD_TAGS  =  "flickr.photos.addTags"  ; 

public   static   final   String   METHOD_DELETE  =  "flickr.photos.delete"  ; 

public   static   final   String   METHOD_GET_ALL_CONTEXTS  =  "flickr.photos.getAllContexts"  ; 

public   static   final   String   METHOD_GET_CONTACTS_PHOTOS  =  "flickr.photos.getContactsPhotos"  ; 

public   static   final   String   METHOD_GET_CONTACTS_PUBLIC_PHOTOS  =  "flickr.photos.getContactsPublicPhotos"  ; 

public   static   final   String   METHOD_GET_CONTEXT  =  "flickr.photos.getContext"  ; 

public   static   final   String   METHOD_GET_COUNTS  =  "flickr.photos.getCounts"  ; 

public   static   final   String   METHOD_GET_EXIF  =  "flickr.photos.getExif"  ; 

public   static   final   String   METHOD_GET_FAVORITES  =  "flickr.photos.getFavorites"  ; 

public   static   final   String   METHOD_GET_INFO  =  "flickr.photos.getInfo"  ; 

public   static   final   String   METHOD_GET_NOT_IN_SET  =  "flickr.photos.getNotInSet"  ; 

public   static   final   String   METHOD_GET_PERMS  =  "flickr.photos.getPerms"  ; 

public   static   final   String   METHOD_GET_RECENT  =  "flickr.photos.getRecent"  ; 

public   static   final   String   METHOD_GET_SIZES  =  "flickr.photos.getSizes"  ; 

public   static   final   String   METHOD_GET_UNTAGGED  =  "flickr.photos.getUntagged"  ; 

public   static   final   String   METHOD_GET_WITH_GEO_DATA  =  "flickr.photos.getWithGeoData"  ; 

public   static   final   String   METHOD_GET_WITHOUT_GEO_DATA  =  "flickr.photos.getWithoutGeoData"  ; 

public   static   final   String   METHOD_RECENTLY_UPDATED  =  "flickr.photos.recentlyUpdated"  ; 

public   static   final   String   METHOD_REMOVE_TAG  =  "flickr.photos.removeTag"  ; 

public   static   final   String   METHOD_SEARCH  =  "flickr.photos.search"  ; 

public   static   final   String   METHOD_SET_CONTENTTYPE  =  "flickr.photos.setContentType"  ; 

public   static   final   String   METHOD_SET_DATES  =  "flickr.photos.setDates"  ; 

public   static   final   String   METHOD_SET_META  =  "flickr.photos.setMeta"  ; 

public   static   final   String   METHOD_SET_PERMS  =  "flickr.photos.setPerms"  ; 

public   static   final   String   METHOD_SET_SAFETYLEVEL  =  "flickr.photos.setSafetyLevel"  ; 

public   static   final   String   METHOD_SET_TAGS  =  "flickr.photos.setTags"  ; 

public   static   final   String   METHOD_GET_INTERESTINGNESS  =  "flickr.interestingness.getList"  ; 

private   static   final   ThreadLocal   DATE_FORMATS  =  new   ThreadLocal  (  )  { 

protected   synchronized   Object   initialValue  (  )  { 
return   new   SimpleDateFormat  (  "yyyy-MM-dd HH:mm:ss"  )  ; 
} 
}  ; 

private   GeoInterface   geoInterface  =  null  ; 

private   String   apiKey  ; 

private   String   sharedSecret  ; 

private   Transport   transport  ; 

public   PhotosInterface  (  String   apiKey  ,  String   sharedSecret  ,  Transport   transport  )  { 
this  .  apiKey  =  apiKey  ; 
this  .  sharedSecret  =  sharedSecret  ; 
this  .  transport  =  transport  ; 
} 





public   synchronized   GeoInterface   getGeoInterface  (  )  { 
if  (  geoInterface  ==  null  )  { 
geoInterface  =  new   GeoInterface  (  apiKey  ,  sharedSecret  ,  transport  )  ; 
} 
return   geoInterface  ; 
} 









public   void   delete  (  String   photoId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_DELETE  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 










public   List   getAllContexts  (  String   photoId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   list  =  new   ArrayList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_ALL_CONTEXTS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Collection   coll  =  response  .  getPayloadCollection  (  )  ; 
Iterator   it  =  coll  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Element   el  =  (  Element  )  it  .  next  (  )  ; 
String   id  =  el  .  getAttribute  (  "id"  )  ; 
String   title  =  el  .  getAttribute  (  "title"  )  ; 
String   kind  =  el  .  getTagName  (  )  ; 
list  .  add  (  new   PhotoPlace  (  kind  ,  id  ,  title  )  )  ; 
} 
return   list  ; 
} 










public   void   addTags  (  String   photoId  ,  String  [  ]  tags  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_ADD_TAGS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "tags"  ,  StringUtilities  .  join  (  tags  ,  " "  ,  true  )  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 













public   PhotoList   getContactsPhotos  (  int   count  ,  boolean   justFriends  ,  boolean   singlePhoto  ,  boolean   includeSelf  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
PhotoList   photos  =  new   PhotoList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_CONTACTS_PHOTOS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  count  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "count"  ,  count  )  )  ; 
} 
if  (  justFriends  )  { 
parameters  .  add  (  new   Parameter  (  "just_friends"  ,  "1"  )  )  ; 
} 
if  (  singlePhoto  )  { 
parameters  .  add  (  new   Parameter  (  "single_photo"  ,  "1"  )  )  ; 
} 
if  (  includeSelf  )  { 
parameters  .  add  (  new   Parameter  (  "include_self"  ,  "1"  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
NodeList   photoNodes  =  photosElement  .  getElementsByTagName  (  "photo"  )  ; 
photos  .  setPage  (  "1"  )  ; 
photos  .  setPages  (  "1"  )  ; 
photos  .  setPerPage  (  ""  +  photoNodes  .  getLength  (  )  )  ; 
photos  .  setTotal  (  ""  +  photoNodes  .  getLength  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  photoNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   photoElement  =  (  Element  )  photoNodes  .  item  (  i  )  ; 
photos  .  add  (  PhotoUtils  .  createPhoto  (  photoElement  )  )  ; 
} 
return   photos  ; 
} 















public   PhotoList   getContactsPublicPhotos  (  String   userId  ,  int   count  ,  boolean   justFriends  ,  boolean   singlePhoto  ,  boolean   includeSelf  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
return   getContactsPublicPhotos  (  userId  ,  Extras  .  MIN_EXTRAS  ,  count  ,  justFriends  ,  singlePhoto  ,  includeSelf  )  ; 
} 

public   PhotoList   getContactsPublicPhotos  (  String   userId  ,  Set   extras  ,  int   count  ,  boolean   justFriends  ,  boolean   singlePhoto  ,  boolean   includeSelf  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
PhotoList   photos  =  new   PhotoList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_CONTACTS_PUBLIC_PHOTOS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "user_id"  ,  userId  )  )  ; 
if  (  count  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "count"  ,  count  )  )  ; 
} 
if  (  justFriends  )  { 
parameters  .  add  (  new   Parameter  (  "just_friends"  ,  "1"  )  )  ; 
} 
if  (  singlePhoto  )  { 
parameters  .  add  (  new   Parameter  (  "single_photo"  ,  "1"  )  )  ; 
} 
if  (  includeSelf  )  { 
parameters  .  add  (  new   Parameter  (  "include_self"  ,  "1"  )  )  ; 
} 
if  (  extras  !=  null  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
Iterator   it  =  extras  .  iterator  (  )  ; 
for  (  int   i  =  0  ;  it  .  hasNext  (  )  ;  i  ++  )  { 
if  (  i  >  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  it  .  next  (  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  Extras  .  KEY_EXTRAS  ,  sb  .  toString  (  )  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
NodeList   photoNodes  =  photosElement  .  getElementsByTagName  (  "photo"  )  ; 
photos  .  setPage  (  "1"  )  ; 
photos  .  setPages  (  "1"  )  ; 
photos  .  setPerPage  (  ""  +  photoNodes  .  getLength  (  )  )  ; 
photos  .  setTotal  (  ""  +  photoNodes  .  getLength  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  photoNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   photoElement  =  (  Element  )  photoNodes  .  item  (  i  )  ; 
photos  .  add  (  PhotoUtils  .  createPhoto  (  photoElement  )  )  ; 
} 
return   photos  ; 
} 










public   PhotoContext   getContext  (  String   photoId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_CONTEXT  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
PhotoContext   photoContext  =  new   PhotoContext  (  )  ; 
Collection   payload  =  response  .  getPayloadCollection  (  )  ; 
Iterator   iter  =  payload  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
Element   payloadElement  =  (  Element  )  iter  .  next  (  )  ; 
String   tagName  =  payloadElement  .  getTagName  (  )  ; 
if  (  tagName  .  equals  (  "prevphoto"  )  )  { 
Photo   photo  =  new   Photo  (  )  ; 
photo  .  setId  (  payloadElement  .  getAttribute  (  "id"  )  )  ; 
photo  .  setSecret  (  payloadElement  .  getAttribute  (  "secret"  )  )  ; 
photo  .  setTitle  (  payloadElement  .  getAttribute  (  "title"  )  )  ; 
photo  .  setFarm  (  payloadElement  .  getAttribute  (  "farm"  )  )  ; 
photo  .  setUrl  (  payloadElement  .  getAttribute  (  "url"  )  )  ; 
photoContext  .  setPreviousPhoto  (  photo  )  ; 
}  else   if  (  tagName  .  equals  (  "nextphoto"  )  )  { 
Photo   photo  =  new   Photo  (  )  ; 
photo  .  setId  (  payloadElement  .  getAttribute  (  "id"  )  )  ; 
photo  .  setSecret  (  payloadElement  .  getAttribute  (  "secret"  )  )  ; 
photo  .  setTitle  (  payloadElement  .  getAttribute  (  "title"  )  )  ; 
photo  .  setFarm  (  payloadElement  .  getAttribute  (  "farm"  )  )  ; 
photo  .  setUrl  (  payloadElement  .  getAttribute  (  "url"  )  )  ; 
photoContext  .  setNextPhoto  (  photo  )  ; 
} 
} 
return   photoContext  ; 
} 










public   Collection   getCounts  (  Date  [  ]  dates  ,  Date  [  ]  takenDates  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   photocounts  =  new   ArrayList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_COUNTS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  dates  ==  null  &&  takenDates  ==  null  )  { 
throw   new   IllegalArgumentException  (  "You must provide a value for either dates or takenDates"  )  ; 
} 
if  (  dates  !=  null  )  { 
List   dateList  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  dates  .  length  ;  i  ++  )  { 
dateList  .  add  (  String  .  valueOf  (  dates  [  i  ]  .  getTime  (  )  /  1000L  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "dates"  ,  StringUtilities  .  join  (  dateList  ,  ","  )  )  )  ; 
} 
if  (  takenDates  !=  null  )  { 
List   takenDateList  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  takenDates  .  length  ;  i  ++  )  { 
takenDateList  .  add  (  String  .  valueOf  (  takenDates  [  i  ]  .  getTime  (  )  /  1000L  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "taken_dates"  ,  StringUtilities  .  join  (  takenDateList  ,  ","  )  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photocountsElement  =  response  .  getPayload  (  )  ; 
NodeList   photocountNodes  =  photocountsElement  .  getElementsByTagName  (  "photocount"  )  ; 
for  (  int   i  =  0  ;  i  <  photocountNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   photocountElement  =  (  Element  )  photocountNodes  .  item  (  i  )  ; 
Photocount   photocount  =  new   Photocount  (  )  ; 
photocount  .  setCount  (  photocountElement  .  getAttribute  (  "count"  )  )  ; 
photocount  .  setFromDate  (  photocountElement  .  getAttribute  (  "fromdate"  )  )  ; 
photocount  .  setToDate  (  photocountElement  .  getAttribute  (  "todate"  )  )  ; 
photocounts  .  add  (  photocount  )  ; 
} 
return   photocounts  ; 
} 









public   Collection   getFavorites  (  String   photoId  ,  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_FAVORITES  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
List   users  =  new   ArrayList  (  )  ; 
Element   userRoot  =  response  .  getPayload  (  )  ; 
NodeList   userNodes  =  userRoot  .  getElementsByTagName  (  "person"  )  ; 
for  (  int   i  =  0  ;  i  <  userNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   userElement  =  (  Element  )  userNodes  .  item  (  i  )  ; 
User   user  =  new   User  (  )  ; 
user  .  setId  (  userElement  .  getAttribute  (  "nsid"  )  )  ; 
user  .  setUsername  (  userElement  .  getAttribute  (  "username"  )  )  ; 
user  .  setFaveDate  (  userElement  .  getAttribute  (  "favedate"  )  )  ; 
users  .  add  (  user  )  ; 
} 
return   users  ; 
} 











public   Collection   getExif  (  String   photoId  ,  String   secret  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_EXIF  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
if  (  secret  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "secret"  ,  secret  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
List   exifs  =  new   ArrayList  (  )  ; 
Element   photoElement  =  response  .  getPayload  (  )  ; 
NodeList   exifElements  =  photoElement  .  getElementsByTagName  (  "exif"  )  ; 
for  (  int   i  =  0  ;  i  <  exifElements  .  getLength  (  )  ;  i  ++  )  { 
Element   exifElement  =  (  Element  )  exifElements  .  item  (  i  )  ; 
Exif   exif  =  new   Exif  (  )  ; 
exif  .  setTagspace  (  exifElement  .  getAttribute  (  "tagspace"  )  )  ; 
exif  .  setTagspaceId  (  exifElement  .  getAttribute  (  "tagspaceid"  )  )  ; 
exif  .  setTag  (  exifElement  .  getAttribute  (  "tag"  )  )  ; 
exif  .  setLabel  (  exifElement  .  getAttribute  (  "label"  )  )  ; 
exif  .  setRaw  (  XMLUtilities  .  getChildValue  (  exifElement  ,  "raw"  )  )  ; 
exif  .  setClean  (  XMLUtilities  .  getChildValue  (  exifElement  ,  "clean"  )  )  ; 
exifs  .  add  (  exif  )  ; 
} 
return   exifs  ; 
} 











public   Photo   getInfo  (  String   photoId  ,  String   secret  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_INFO  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
if  (  secret  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "secret"  ,  secret  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photoElement  =  (  Element  )  response  .  getPayload  (  )  ; 
return   PhotoUtils  .  createPhoto  (  photoElement  )  ; 
} 











public   PhotoList   getNotInSet  (  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
PhotoList   photos  =  new   PhotoList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  PhotosInterface  .  METHOD_GET_NOT_IN_SET  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
RequestContext   requestContext  =  RequestContext  .  getRequestContext  (  )  ; 
List   extras  =  requestContext  .  getExtras  (  )  ; 
if  (  extras  .  size  (  )  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "extras"  ,  StringUtilities  .  join  (  extras  ,  ","  )  )  )  ; 
} 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
photos  .  setPage  (  photosElement  .  getAttribute  (  "page"  )  )  ; 
photos  .  setPages  (  photosElement  .  getAttribute  (  "pages"  )  )  ; 
photos  .  setPerPage  (  photosElement  .  getAttribute  (  "perpage"  )  )  ; 
photos  .  setTotal  (  photosElement  .  getAttribute  (  "total"  )  )  ; 
NodeList   photoElements  =  photosElement  .  getElementsByTagName  (  "photo"  )  ; 
for  (  int   i  =  0  ;  i  <  photoElements  .  getLength  (  )  ;  i  ++  )  { 
Element   photoElement  =  (  Element  )  photoElements  .  item  (  i  )  ; 
photos  .  add  (  PhotoUtils  .  createPhoto  (  photoElement  )  )  ; 
} 
return   photos  ; 
} 










public   Permissions   getPerms  (  String   photoId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_PERMS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   permissionsElement  =  response  .  getPayload  (  )  ; 
Permissions   permissions  =  new   Permissions  (  )  ; 
permissions  .  setId  (  permissionsElement  .  getAttribute  (  "id"  )  )  ; 
permissions  .  setPublicFlag  (  "1"  .  equals  (  permissionsElement  .  getAttribute  (  "ispublic"  )  )  )  ; 
permissions  .  setFamilyFlag  (  "1"  .  equals  (  permissionsElement  .  getAttribute  (  "isfamily"  )  )  )  ; 
permissions  .  setComment  (  permissionsElement  .  getAttribute  (  "permcomment"  )  )  ; 
permissions  .  setAddmeta  (  permissionsElement  .  getAttribute  (  "permaddmeta"  )  )  ; 
return   permissions  ; 
} 











public   PhotoList   getRecent  (  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_RECENT  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
PhotoList   photos  =  PhotoUtils  .  createPhotoList  (  photosElement  )  ; 
return   photos  ; 
} 










public   Collection   getSizes  (  String   photoId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   sizes  =  new   ArrayList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_SIZES  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   sizesElement  =  response  .  getPayload  (  )  ; 
NodeList   sizeNodes  =  sizesElement  .  getElementsByTagName  (  "size"  )  ; 
for  (  int   i  =  0  ;  i  <  sizeNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   sizeElement  =  (  Element  )  sizeNodes  .  item  (  i  )  ; 
Size   size  =  new   Size  (  )  ; 
size  .  setLabel  (  sizeElement  .  getAttribute  (  "label"  )  )  ; 
size  .  setWidth  (  sizeElement  .  getAttribute  (  "width"  )  )  ; 
size  .  setHeight  (  sizeElement  .  getAttribute  (  "height"  )  )  ; 
size  .  setSource  (  sizeElement  .  getAttribute  (  "source"  )  )  ; 
size  .  setUrl  (  sizeElement  .  getAttribute  (  "url"  )  )  ; 
sizes  .  add  (  size  )  ; 
} 
return   sizes  ; 
} 











public   PhotoList   getUntagged  (  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_UNTAGGED  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
PhotoList   photos  =  PhotoUtils  .  createPhotoList  (  photosElement  )  ; 
return   photos  ; 
} 




























public   PhotoList   getWithGeoData  (  Date   minUploadDate  ,  Date   maxUploadDate  ,  Date   minTakenDate  ,  Date   maxTakenDate  ,  int   privacyFilter  ,  String   sort  ,  Set   extras  ,  int   perPage  ,  int   page  )  throws   FlickrException  ,  IOException  ,  SAXException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_WITH_GEO_DATA  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  minUploadDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "min_upload_date"  ,  minUploadDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  maxUploadDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "max_upload_date"  ,  maxUploadDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  minTakenDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "min_taken_date"  ,  minTakenDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  maxTakenDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "max_taken_date"  ,  maxTakenDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  privacyFilter  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "privacy_filter"  ,  privacyFilter  )  )  ; 
} 
if  (  sort  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "sort"  ,  sort  )  )  ; 
} 
if  (  extras  !=  null  &&  !  extras  .  isEmpty  (  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
Iterator   it  =  extras  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
if  (  sb  .  length  (  )  >  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  it  .  next  (  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "extras"  ,  sb  .  toString  (  )  )  )  ; 
} 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
PhotoList   photos  =  PhotoUtils  .  createPhotoList  (  photosElement  )  ; 
return   photos  ; 
} 




























public   PhotoList   getWithoutGeoData  (  Date   minUploadDate  ,  Date   maxUploadDate  ,  Date   minTakenDate  ,  Date   maxTakenDate  ,  int   privacyFilter  ,  String   sort  ,  Set   extras  ,  int   perPage  ,  int   page  )  throws   FlickrException  ,  IOException  ,  SAXException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_WITHOUT_GEO_DATA  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
if  (  minUploadDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "min_upload_date"  ,  minUploadDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  maxUploadDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "max_upload_date"  ,  maxUploadDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  minTakenDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "min_taken_date"  ,  minTakenDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  maxTakenDate  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "max_taken_date"  ,  maxTakenDate  .  getTime  (  )  /  1000L  )  )  ; 
} 
if  (  privacyFilter  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "privacy_filter"  ,  privacyFilter  )  )  ; 
} 
if  (  sort  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "sort"  ,  sort  )  )  ; 
} 
if  (  extras  !=  null  &&  !  extras  .  isEmpty  (  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
Iterator   it  =  extras  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
if  (  sb  .  length  (  )  >  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  it  .  next  (  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "extras"  ,  sb  .  toString  (  )  )  )  ; 
} 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
PhotoList   photos  =  PhotoUtils  .  createPhotoList  (  photosElement  )  ; 
return   photos  ; 
} 















public   PhotoList   recentlyUpdated  (  Date   minDate  ,  Set   extras  ,  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_RECENTLY_UPDATED  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "min_date"  ,  minDate  .  getTime  (  )  /  1000L  )  )  ; 
if  (  extras  !=  null  &&  !  extras  .  isEmpty  (  )  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
Iterator   it  =  extras  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
if  (  sb  .  length  (  )  >  0  )  { 
sb  .  append  (  ","  )  ; 
} 
sb  .  append  (  it  .  next  (  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "extras"  ,  sb  .  toString  (  )  )  )  ; 
} 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
PhotoList   photos  =  PhotoUtils  .  createPhotoList  (  photosElement  )  ; 
return   photos  ; 
} 









public   void   removeTag  (  String   tagId  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_REMOVE_TAG  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "tag_id"  ,  tagId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 












public   PhotoList   search  (  SearchParameters   params  ,  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
PhotoList   photos  =  new   PhotoList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SEARCH  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  addAll  (  params  .  getAsParameters  (  )  )  ; 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  ""  +  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  ""  +  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
photos  .  setPage  (  photosElement  .  getAttribute  (  "page"  )  )  ; 
photos  .  setPages  (  photosElement  .  getAttribute  (  "pages"  )  )  ; 
photos  .  setPerPage  (  photosElement  .  getAttribute  (  "perpage"  )  )  ; 
photos  .  setTotal  (  photosElement  .  getAttribute  (  "total"  )  )  ; 
NodeList   photoNodes  =  photosElement  .  getElementsByTagName  (  "photo"  )  ; 
for  (  int   i  =  0  ;  i  <  photoNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   photoElement  =  (  Element  )  photoNodes  .  item  (  i  )  ; 
photos  .  add  (  PhotoUtils  .  createPhoto  (  photoElement  )  )  ; 
} 
return   photos  ; 
} 












public   PhotoList   searchInterestingness  (  SearchParameters   params  ,  int   perPage  ,  int   page  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
PhotoList   photos  =  new   PhotoList  (  )  ; 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_GET_INTERESTINGNESS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  addAll  (  params  .  getAsParameters  (  )  )  ; 
if  (  perPage  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "per_page"  ,  perPage  )  )  ; 
} 
if  (  page  >  0  )  { 
parameters  .  add  (  new   Parameter  (  "page"  ,  page  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  get  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
Element   photosElement  =  response  .  getPayload  (  )  ; 
photos  .  setPage  (  photosElement  .  getAttribute  (  "page"  )  )  ; 
photos  .  setPages  (  photosElement  .  getAttribute  (  "pages"  )  )  ; 
photos  .  setPerPage  (  photosElement  .  getAttribute  (  "perpage"  )  )  ; 
photos  .  setTotal  (  photosElement  .  getAttribute  (  "total"  )  )  ; 
NodeList   photoNodes  =  photosElement  .  getElementsByTagName  (  "photo"  )  ; 
for  (  int   i  =  0  ;  i  <  photoNodes  .  getLength  (  )  ;  i  ++  )  { 
Element   photoElement  =  (  Element  )  photoNodes  .  item  (  i  )  ; 
Photo   photo  =  new   Photo  (  )  ; 
photo  .  setId  (  photoElement  .  getAttribute  (  "id"  )  )  ; 
User   owner  =  new   User  (  )  ; 
owner  .  setId  (  photoElement  .  getAttribute  (  "owner"  )  )  ; 
photo  .  setOwner  (  owner  )  ; 
photo  .  setSecret  (  photoElement  .  getAttribute  (  "secret"  )  )  ; 
photo  .  setServer  (  photoElement  .  getAttribute  (  "server"  )  )  ; 
photo  .  setFarm  (  photoElement  .  getAttribute  (  "farm"  )  )  ; 
photo  .  setTitle  (  photoElement  .  getAttribute  (  "title"  )  )  ; 
photo  .  setPublicFlag  (  "1"  .  equals  (  photoElement  .  getAttribute  (  "ispublic"  )  )  )  ; 
photo  .  setFriendFlag  (  "1"  .  equals  (  photoElement  .  getAttribute  (  "isfriend"  )  )  )  ; 
photo  .  setFamilyFlag  (  "1"  .  equals  (  photoElement  .  getAttribute  (  "isfamily"  )  )  )  ; 
photos  .  add  (  photo  )  ; 
} 
return   photos  ; 
} 













public   void   setContentType  (  String   photoId  ,  String   contentType  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_CONTENTTYPE  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "content_type"  ,  contentType  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 












public   void   setDates  (  String   photoId  ,  Date   datePosted  ,  Date   dateTaken  ,  String   dateTakenGranularity  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_DATES  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
if  (  datePosted  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "date_posted"  ,  new   Long  (  datePosted  .  getTime  (  )  /  1000  )  )  )  ; 
} 
if  (  dateTaken  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "date_taken"  ,  (  (  DateFormat  )  DATE_FORMATS  .  get  (  )  )  .  format  (  dateTaken  )  )  )  ; 
} 
if  (  dateTakenGranularity  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "date_taken_granularity"  ,  dateTakenGranularity  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 











public   void   setMeta  (  String   photoId  ,  String   title  ,  String   description  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_META  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "title"  ,  title  )  )  ; 
parameters  .  add  (  new   Parameter  (  "description"  ,  description  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 










public   void   setPerms  (  String   photoId  ,  Permissions   permissions  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_PERMS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "is_public"  ,  permissions  .  isPublicFlag  (  )  ?  "1"  :  "0"  )  )  ; 
parameters  .  add  (  new   Parameter  (  "is_friend"  ,  permissions  .  isFriendFlag  (  )  ?  "1"  :  "0"  )  )  ; 
parameters  .  add  (  new   Parameter  (  "is_family"  ,  permissions  .  isFamilyFlag  (  )  ?  "1"  :  "0"  )  )  ; 
parameters  .  add  (  new   Parameter  (  "perm_comment"  ,  permissions  .  getComment  (  )  )  )  ; 
parameters  .  add  (  new   Parameter  (  "perm_addmeta"  ,  permissions  .  getAddmeta  (  )  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 














public   void   setSafetyLevel  (  String   photoId  ,  String   safetyLevel  ,  Boolean   hidden  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_SAFETYLEVEL  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
if  (  safetyLevel  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "safety_level"  ,  safetyLevel  )  )  ; 
} 
if  (  hidden  !=  null  )  { 
parameters  .  add  (  new   Parameter  (  "hidden"  ,  hidden  .  booleanValue  (  )  ?  "1"  :  "0"  )  )  ; 
} 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 










public   void   setTags  (  String   photoId  ,  String  [  ]  tags  )  throws   IOException  ,  SAXException  ,  FlickrException  { 
List   parameters  =  new   ArrayList  (  )  ; 
parameters  .  add  (  new   Parameter  (  "method"  ,  METHOD_SET_TAGS  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_key"  ,  apiKey  )  )  ; 
parameters  .  add  (  new   Parameter  (  "photo_id"  ,  photoId  )  )  ; 
parameters  .  add  (  new   Parameter  (  "tags"  ,  StringUtilities  .  join  (  tags  ,  " "  ,  true  )  )  )  ; 
parameters  .  add  (  new   Parameter  (  "api_sig"  ,  AuthUtilities  .  getSignature  (  sharedSecret  ,  parameters  )  )  )  ; 
Response   response  =  transport  .  post  (  transport  .  getPath  (  )  ,  parameters  )  ; 
if  (  response  .  isError  (  )  )  { 
throw   new   FlickrException  (  response  .  getErrorCode  (  )  ,  response  .  getErrorMessage  (  )  )  ; 
} 
} 











public   Photo   getPhoto  (  String   id  )  throws   IOException  ,  FlickrException  ,  SAXException  { 
return   getPhoto  (  id  ,  null  )  ; 
} 












public   Photo   getPhoto  (  String   id  ,  String   secret  )  throws   IOException  ,  FlickrException  ,  SAXException  { 
return   getInfo  (  id  ,  secret  )  ; 
} 













public   InputStream   getImageAsStream  (  Photo   photo  ,  int   size  )  throws   IOException  ,  FlickrException  { 
String   urlStr  =  ""  ; 
if  (  size  ==  Size  .  SQUARE  )  { 
urlStr  =  photo  .  getSmallSquareUrl  (  )  ; 
}  else   if  (  size  ==  Size  .  THUMB  )  { 
urlStr  =  photo  .  getThumbnailUrl  (  )  ; 
}  else   if  (  size  ==  Size  .  SMALL  )  { 
urlStr  =  photo  .  getSmallUrl  (  )  ; 
}  else   if  (  size  ==  Size  .  MEDIUM  )  { 
urlStr  =  photo  .  getMediumUrl  (  )  ; 
}  else   if  (  size  ==  Size  .  LARGE  )  { 
urlStr  =  photo  .  getLargeUrl  (  )  ; 
}  else   if  (  size  ==  Size  .  ORIGINAL  )  { 
urlStr  =  photo  .  getOriginalUrl  (  )  ; 
}  else  { 
throw   new   FlickrException  (  "0"  ,  "Unknown Photo-size"  )  ; 
} 
URL   url  =  new   URL  (  urlStr  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
if  (  transport   instanceof   REST  )  { 
if  (  (  (  REST  )  transport  )  .  isProxyAuth  (  )  )  { 
conn  .  setRequestProperty  (  "Proxy-Authorization"  ,  "Basic "  +  (  (  REST  )  transport  )  .  getProxyCredentials  (  )  )  ; 
} 
} 
conn  .  connect  (  )  ; 
return   conn  .  getInputStream  (  )  ; 
} 












public   BufferedImage   getImage  (  Photo   photo  ,  int   size  )  throws   IOException  ,  FlickrException  { 
return   ImageIO  .  read  (  getImageAsStream  (  photo  ,  size  )  )  ; 
} 








public   BufferedImage   getImage  (  String   urlStr  )  throws   IOException  { 
URL   url  =  new   URL  (  urlStr  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
if  (  transport   instanceof   REST  )  { 
if  (  (  (  REST  )  transport  )  .  isProxyAuth  (  )  )  { 
conn  .  setRequestProperty  (  "Proxy-Authorization"  ,  "Basic "  +  (  (  REST  )  transport  )  .  getProxyCredentials  (  )  )  ; 
} 
} 
conn  .  connect  (  )  ; 
InputStream   in  =  null  ; 
try  { 
in  =  conn  .  getInputStream  (  )  ; 
return   ImageIO  .  read  (  in  )  ; 
}  finally  { 
IOUtilities  .  close  (  in  )  ; 
} 
} 
} 

