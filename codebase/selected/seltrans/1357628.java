package   espider  .  libs  .  helliker  .  id3  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 

































public   class   MP3File   implements   Comparable  { 





public   static   final   int   BOTH_TAGS  =  0  ; 





public   static   final   int   ID3V2_ONLY  =  1  ; 





public   static   final   int   ID3V1_ONLY  =  2  ; 




public   static   final   int   NO_TAGS  =  3  ; 





public   static   final   int   EXISTING_TAGS_ONLY  =  4  ; 




private   final   int   ID3V1  =  5  ; 




private   final   int   ID3V2  =  6  ; 




private   ID3v1Tag   id3v1  =  null  ; 




private   ID3v2Tag   id3v2  =  null  ; 




private   MPEGAudioFrameHeader   head  =  null  ; 




private   File   mp3  =  null  ; 




private   int   tagType  =  0  ; 













public   MP3File  (  String   fn  )  throws   FileNotFoundException  ,  NoMPEGFramesException  ,  IOException  ,  ID3v2FormatException  ,  CorruptHeaderException  { 
this  (  new   File  (  fn  )  )  ; 
} 













public   MP3File  (  File   mp3  )  throws   FileNotFoundException  ,  NoMPEGFramesException  ,  IOException  ,  ID3v2FormatException  ,  CorruptHeaderException  { 
this  (  mp3  ,  EXISTING_TAGS_ONLY  )  ; 
} 
















public   MP3File  (  String   fn  ,  int   tagType  )  throws   FileNotFoundException  ,  NoMPEGFramesException  ,  IOException  ,  ID3v2FormatException  ,  CorruptHeaderException  { 
this  (  new   File  (  fn  )  ,  tagType  )  ; 
} 
















public   MP3File  (  File   mp3  ,  int   tagType  )  throws   FileNotFoundException  ,  NoMPEGFramesException  ,  IOException  ,  ID3v2FormatException  ,  CorruptHeaderException  { 
this  .  mp3  =  mp3  ; 
this  .  tagType  =  tagType  ; 
head  =  new   MPEGAudioFrameHeader  (  mp3  ,  0  )  ; 
id3v1  =  new   ID3v1Tag  (  mp3  )  ; 
id3v2  =  new   ID3v2Tag  (  mp3  ,  head  .  getLocation  (  )  )  ; 
} 







public   long   getPlayingTime  (  )  { 
long   time  =  0  ; 
if  (  head  .  isVBR  (  )  )  { 
time  =  head  .  getVBRPlayingTime  (  )  ; 
}  else  { 
long   datasize  =  (  mp3  .  length  (  )  *  8  )  -  id3v2  .  getSize  (  )  -  id3v1  .  getSize  (  )  ; 
long   bps  =  head  .  getBitRate  (  )  *  1000  ; 
if  (  bps  ==  0  )  { 
time  =  0  ; 
}  else  { 
time  =  datasize  /  bps  ; 
} 
} 
return   time  ; 
} 







public   String   getPlayingTimeString  (  )  { 
String   str  ; 
long   time  =  getPlayingTime  (  )  ; 
long   mins  =  time  /  60  ; 
long   secs  =  Math  .  round  (  (  (  (  double  )  time  /  60  )  -  (  long  )  (  time  /  60  )  )  *  60  )  ; 
str  =  mins  +  ":"  ; 
if  (  secs  <  10  )  { 
str  +=  "0"  +  secs  ; 
}  else  { 
str  +=  ""  +  secs  ; 
} 
return   str  ; 
} 






public   String   getPath  (  )  { 
return   mp3  .  getAbsolutePath  (  )  ; 
} 






public   String   getParent  (  )  { 
return   mp3  .  getParent  (  )  ; 
} 






public   String   getFileName  (  )  { 
return   mp3  .  getName  (  )  ; 
} 






public   long   getFileSize  (  )  { 
return   mp3  .  length  (  )  ; 
} 






public   boolean   id3v2Exists  (  )  { 
return   id3v2  .  tagExists  (  )  ; 
} 






public   boolean   id3v1Exists  (  )  { 
return   id3v1  .  tagExists  (  )  ; 
} 







public   boolean   isMP3  (  )  { 
return   head  .  isMP3  (  )  ; 
} 






public   boolean   isVBR  (  )  { 
return   head  .  isVBR  (  )  ; 
} 







public   int   getBitRate  (  )  { 
return   head  .  getBitRate  (  )  ; 
} 






public   int   getSampleRate  (  )  { 
return   head  .  getSampleRate  (  )  ; 
} 






public   String   getMPEGEmphasis  (  )  { 
return   head  .  getEmphasis  (  )  ; 
} 






public   String   getMPEGLayer  (  )  { 
return   head  .  getLayer  (  )  ; 
} 







public   String   getMPEGVersion  (  )  { 
return   head  .  getVersion  (  )  ; 
} 






public   String   getMPEGChannelMode  (  )  { 
return   head  .  getChannelMode  (  )  ; 
} 






public   boolean   isMPEGCopyrighted  (  )  { 
return   head  .  isCopyrighted  (  )  ; 
} 






public   boolean   isMPEGOriginal  (  )  { 
return   head  .  isOriginal  (  )  ; 
} 






public   boolean   isMPEGProtected  (  )  { 
return   head  .  isProtected  (  )  ; 
} 






public   boolean   isMPEGPrivate  (  )  { 
return   head  .  privateBitSet  (  )  ; 
} 










public   void   removeTags  (  int   type  )  throws   FileNotFoundException  ,  IOException  { 
if  (  allow  (  ID3V1  ,  type  )  )  { 
id3v1  .  removeTag  (  )  ; 
} 
if  (  allow  (  ID3V2  ,  type  )  )  { 
id3v2  .  removeTag  (  )  ; 
} 
} 








public   void   writeTags  (  )  throws   FileNotFoundException  ,  IOException  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  writeTag  (  )  ; 
} 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  writeTag  (  )  ; 
} 
} 






public   void   setTitle  (  String   title  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setTitle  (  title  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  TITLE  ,  title  )  ; 
} 
} 






public   void   setAlbum  (  String   album  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setAlbum  (  album  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  ALBUM  ,  album  )  ; 
} 
} 






public   void   setArtist  (  String   artist  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setArtist  (  artist  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  LEAD_PERFORMERS  ,  artist  )  ; 
} 
} 






public   void   setComment  (  String   comment  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setComment  (  comment  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setCommentFrame  (  ""  ,  comment  )  ; 
} 
} 






public   void   setGenre  (  String   genre  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setGenreString  (  genre  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  CONTENT_TYPE  ,  genre  )  ; 
} 
} 






public   void   setYear  (  String   year  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setYear  (  year  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  YEAR  ,  year  )  ; 
} 
} 






public   void   setTrack  (  int   track  )  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setTrack  (  track  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  TRACK_NUMBER  ,  String  .  valueOf  (  track  )  )  ; 
} 
} 








public   void   setTrack  (  String   track  )  throws   NumberFormatException  { 
if  (  allow  (  ID3V1  )  )  { 
id3v1  .  setTrack  (  Integer  .  parseInt  (  track  )  )  ; 
} 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  TRACK_NUMBER  ,  track  )  ; 
} 
} 






public   void   setComposer  (  String   composer  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  COMPOSER  ,  composer  )  ; 
} 
} 






public   void   setOriginalArtist  (  String   artist  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  ORIGINAL_ARTIST  ,  artist  )  ; 
} 
} 






public   void   setCopyrightInfo  (  String   copyright  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  COPYRIGHT_MESSAGE  ,  copyright  )  ; 
} 
} 








public   void   setUserDefinedURL  (  String   desc  ,  String   url  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setUserDefinedURLFrame  (  desc  ,  url  )  ; 
} 
} 








public   void   setUserDefinedText  (  String   desc  ,  String   text  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setUserDefinedTextFrame  (  desc  ,  text  )  ; 
} 
} 






public   void   setEncodedBy  (  String   encBy  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  ID3v2Frames  .  ENCODED_BY  ,  encBy  )  ; 
} 
} 









public   void   setTextFrame  (  String   id  ,  String   data  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  setTextFrame  (  id  ,  data  )  ; 
} 
} 








public   void   setFrameData  (  String   id  ,  byte  [  ]  data  )  { 
if  (  allow  (  ID3V2  )  )  { 
id3v2  .  updateFrameData  (  id  ,  data  )  ; 
} 
} 







public   String   getArtist  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  LEAD_PERFORMERS  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getArtist  (  )  ; 
} 
return   str  ; 
} 







public   String   getAlbum  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  ALBUM  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getAlbum  (  )  ; 
} 
return   str  ; 
} 







public   String   getComment  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  COMMENTS  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getComment  (  )  ; 
} 
return   str  ; 
} 







public   String   getGenre  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  CONTENT_TYPE  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getGenreString  (  )  ; 
} 
return   str  ; 
} 







public   String   getTitle  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  TITLE  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getTitle  (  )  ; 
} 
return   str  ; 
} 







public   String   getTrackString  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  TRACK_NUMBER  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  String  .  valueOf  (  id3v1  .  getTrack  (  )  )  ; 
} 
return   str  ; 
} 









public   int   getTrack  (  )  throws   ID3v2FormatException  { 
String   str  =  getTrackString  (  )  ; 
int   track  =  -  1  ; 
int   loc  =  str  .  indexOf  (  "/"  )  ; 
try  { 
if  (  loc  !=  -  1  )  { 
str  =  str  .  substring  (  0  ,  loc  )  ; 
} 
track  =  Integer  .  parseInt  (  str  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
return   track  ; 
} 











public   int   getNumTracks  (  )  throws   ID3v2FormatException  { 
String   str  =  getTrackString  (  )  ; 
int   track  =  -  1  ; 
int   loc  =  str  .  indexOf  (  "/"  )  ; 
try  { 
if  (  loc  !=  -  1  )  { 
str  =  str  .  substring  (  loc  +  1  ,  str  .  length  (  )  )  ; 
track  =  Integer  .  parseInt  (  str  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
} 
return   track  ; 
} 







public   String   getYear  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  YEAR  )  ; 
}  else   if  (  allow  (  ID3V1  )  )  { 
str  =  id3v1  .  getYear  (  )  ; 
} 
return   str  ; 
} 








public   String   getComposer  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  COMPOSER  )  ; 
} 
return   str  ; 
} 








public   String   getOriginalArtist  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  ORIGINAL_ARTIST  )  ; 
} 
return   str  ; 
} 








public   String   getCopyrightInfo  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  COPYRIGHT_MESSAGE  )  ; 
} 
return   str  ; 
} 








public   String   getUserDefinedURL  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  USER_DEFINED_URL  )  ; 
} 
return   str  ; 
} 








public   String   getEncodedBy  (  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  ID3v2Frames  .  ENCODED_BY  )  ; 
} 
return   str  ; 
} 











public   String   getFrameDataString  (  String   id  )  throws   ID3v2FormatException  { 
String   str  =  new   String  (  )  ; 
if  (  allow  (  ID3V2  )  )  { 
str  =  id3v2  .  getFrameDataString  (  id  )  ; 
} 
return   str  ; 
} 









public   byte  [  ]  getFrameDataBytes  (  String   id  )  { 
byte  [  ]  b  =  new   byte  [  0  ]  ; 
if  (  allow  (  ID3V2  )  )  { 
b  =  id3v2  .  getFrameData  (  id  )  ; 
} 
return   b  ; 
} 






public   int   getTaggingType  (  )  { 
return   tagType  ; 
} 








public   void   setTaggingType  (  int   newType  )  { 
tagType  =  newType  ; 
} 







public   int   getTagSize  (  )  { 
return   getTagSize  (  tagType  )  ; 
} 











public   int   getTagSize  (  int   type  )  { 
int   size  =  0  ; 
if  (  allow  (  ID3V1  ,  type  )  )  { 
size  +=  id3v1  .  getSize  (  )  ; 
} 
if  (  allow  (  ID3V2  ,  type  )  )  { 
if  (  id3v2  .  tagExists  (  )  )  { 
size  +=  id3v2  .  getSize  (  )  ; 
} 
} 
return   size  ; 
} 









private   boolean   allow  (  int   tagVersion  )  { 
return   this  .  allow  (  tagVersion  ,  tagType  )  ; 
} 











private   boolean   allow  (  int   tagVersion  ,  int   type  )  { 
boolean   retval  =  false  ; 
if  (  tagVersion  ==  ID3V1  )  { 
retval  =  (  (  type  ==  EXISTING_TAGS_ONLY  )  &&  id3v1  .  tagExists  (  )  )  ||  (  type  ==  ID3V1_ONLY  )  ||  (  type  ==  BOTH_TAGS  )  ; 
}  else   if  (  tagVersion  ==  ID3V2  )  { 
retval  =  (  (  type  ==  EXISTING_TAGS_ONLY  )  &&  id3v2  .  tagExists  (  )  )  ||  (  type  ==  ID3V2_ONLY  )  ||  (  type  ==  BOTH_TAGS  )  ; 
} 
return   retval  ; 
} 








public   String   toString  (  )  { 
return  "MP3File"  +  "\nPath:\t\t\t\t"  +  mp3  .  getAbsolutePath  (  )  +  "\nFileSize:\t\t\t"  +  mp3  .  length  (  )  +  " bytes\nPlayingTime:\t\t\t"  +  getPlayingTimeString  (  )  +  "\n"  +  head  +  "\n"  +  id3v1  +  "\n"  +  id3v2  ; 
} 








public   boolean   equals  (  Object   o  )  { 
boolean   equal  =  false  ; 
if  (  o   instanceof   MP3File  )  { 
equal  =  this  .  getPath  (  )  .  equals  (  (  (  MP3File  )  o  )  .  getPath  (  )  )  ; 
} 
return   equal  ; 
} 











public   int   compareTo  (  Object   o  )  { 
int   cmp  =  1  ; 
if  (  o   instanceof   MP3File  )  { 
cmp  =  this  .  getPath  (  )  .  compareTo  (  (  (  MP3File  )  o  )  .  getPath  (  )  )  ; 
} 
return   cmp  ; 
} 
} 

