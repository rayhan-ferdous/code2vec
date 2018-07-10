package   games  .  midhedava  .  client  .  soundreview  ; 

import   games  .  midhedava  .  client  .  sprite  .  SpriteStore  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Properties  ; 
import   marauroa  .  common  .  Log4J  ; 

public   class   SoundFileReader  { 


public   static   final   String   STORE_PROPERTYFILE  =  "data/sounds/"  ; 

static   Properties   soundprops  ; 

public   SoundFileReader  (  )  { 
} 

public   void   init  (  )  { 
init  (  STORE_PROPERTYFILE  +  "stensounds.properties"  )  ; 
} 

private   void   init  (  String   propertyfile  )  { 
soundprops  =  loadSoundProperties  (  soundprops  ,  propertyfile  )  ; 
} 

public   void   initWithXml  (  )  { 
} 









public   static   InputStream   getResourceStream  (  String   name  )  throws   IOException  { 
URL   url  =  SpriteStore  .  get  (  )  .  getResourceURL  (  name  )  ; 
if  (  url  ==  null  )  { 
return   null  ; 
} 
return   url  .  openStream  (  )  ; 
} 







public   static   Properties   loadSoundProperties  (  Properties   prop  ,  String   url  )  { 
InputStream   in1  ; 
if  (  prop  ==  null  )  { 
prop  =  new   Properties  (  )  ; 
} 
try  { 
in1  =  getResourceStream  (  url  )  ; 
prop  .  load  (  in1  )  ; 
in1  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   prop  ; 
} 

byte  [  ]  getData  (  String   soundname  )  { 
byte  [  ]  data  ; 
String   soundbase  =  SoundFileReader  .  soundprops  .  getProperty  (  "soundbase"  )  ; 
if  (  soundbase  ==  null  )  return   null  ; 
if  (  !  soundbase  .  endsWith  (  "/"  )  )  { 
soundbase  =  soundbase  +  "/"  ; 
} 
String   filename  =  soundbase  +  soundname  ; 
InputStream   in  ; 
ByteArrayOutputStream   bout  ; 
bout  =  new   ByteArrayOutputStream  (  )  ; 
try  { 
in  =  getResourceStream  (  filename  )  ; 
if  (  in  ==  null  )  { 
return   null  ; 
} 
transferData  (  in  ,  bout  ,  4096  )  ; 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
Log4J  .  getLogger  (  SoundFileReader  .  class  )  .  error  (  "could not open soundfile "  +  filename  )  ; 
return   null  ; 
} 
data  =  bout  .  toByteArray  (  )  ; 
return   data  ; 
} 










static   void   transferData  (  InputStream   input  ,  OutputStream   output  ,  int   bufferSize  )  throws   java  .  io  .  IOException  { 
byte  [  ]  buffer  =  new   byte  [  bufferSize  ]  ; 
int   len  ; 
while  (  (  len  =  input  .  read  (  buffer  )  )  >  0  )  { 
output  .  write  (  buffer  ,  0  ,  len  )  ; 
} 
} 
} 

