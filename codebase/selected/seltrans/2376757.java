package   gov  .  sns  .  apps  .  pta  .  rscmgt  ; 

import   gov  .  sns  .  apps  .  pta  .  MainApplication  ; 
import   gov  .  sns  .  tools  .  apputils  .  iconlib  .  IconLib  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  prefs  .  Preferences  ; 
import   javax  .  swing  .  ImageIcon  ; 











public   class   ResourceManager  { 


private   static   final   Preferences   PREFS_CONFIG  ; 


public   static   String   STR_DIR_RESOURCES  =  "resources/"  ; 




static  { 
PREFS_CONFIG  =  Preferences  .  userNodeForPackage  (  ResourceManager  .  class  )  ; 
} 













public   static   Preferences   getPreferences  (  )  { 
return   PREFS_CONFIG  ; 
} 






















public   static   URL   getResourceUrl  (  String   strLocalName  )  { 
String   strPathRel  =  ResourceManager  .  STR_DIR_RESOURCES  +  strLocalName  ; 
URL   urlResrc  =  MainApplication  .  class  .  getResource  (  strPathRel  )  ; 
return   urlResrc  ; 
} 















public   static   InputStream   openResource  (  String   strRscName  )  throws   IOException  { 
URL   urlSrc  =  ResourceManager  .  getResourceUrl  (  strRscName  )  ; 
InputStream   isPath  =  urlSrc  .  openStream  (  )  ; 
return   isPath  ; 
} 


























public   static   Properties   getProperties  (  String   strMapFile  )  throws   IOException  { 
InputStream   is  =  openResource  (  strMapFile  )  ; 
Properties   map  =  new   Properties  (  )  ; 
map  .  load  (  is  )  ; 
is  .  close  (  )  ; 
return   map  ; 
} 



































public   static   ImageIcon   getImageIcon  (  String   strRscName  )  { 
if  (  strRscName  ==  null  )  return   null  ; 
if  (  strRscName  .  startsWith  (  "IconLib"  )  )  { 
String  [  ]  arrTokens  =  strRscName  .  split  (  ":"  )  ; 
ImageIcon   icon  =  IconLib  .  getImageIcon  (  arrTokens  [  1  ]  ,  arrTokens  [  2  ]  )  ; 
return   icon  ; 
} 
URL   urlIcon  =  ResourceManager  .  getResourceUrl  (  strRscName  )  ; 
ImageIcon   icnImage  =  new   ImageIcon  (  urlIcon  )  ; 
return   icnImage  ; 
} 









private   ResourceManager  (  )  { 
} 

; 
} 

