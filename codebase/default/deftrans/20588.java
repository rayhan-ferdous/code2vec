import   javax  .  sound  .  sampled  .  *  ; 
import   java  .  io  .  *  ; 

public   class   Audio  { 























public   static   synchronized   void   playSound  (  final   String   soundFile  )  { 
Thread   soundThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
String   url  =  Config  .  SOUNDS_DIR  +  soundFile  ; 
AudioInputStream   inputStream  =  AudioSystem  .  getAudioInputStream  (  new   File  (  url  )  )  ; 
Clip   clip  =  AudioSystem  .  getClip  (  )  ; 
clip  .  open  (  inputStream  )  ; 
clip  .  stop  (  )  ; 
clip  .  setFramePosition  (  0  )  ; 
clip  .  start  (  )  ; 
url  =  null  ; 
inputStream  =  null  ; 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  ex  .  getMessage  (  )  )  ; 
} 
} 
}  )  ; 
soundThread  .  setName  (  "Sound: "  +  soundFile  )  ; 
soundThread  .  start  (  )  ; 
} 



















public   static   Clip   loadSound  (  final   String   soundFile  )  { 
Clip   clip  =  null  ; 
try  { 
String   url  =  Config  .  SOUNDS_DIR  +  soundFile  ; 
AudioInputStream   inputStream  =  AudioSystem  .  getAudioInputStream  (  new   File  (  url  )  )  ; 
clip  =  AudioSystem  .  getClip  (  )  ; 
clip  .  open  (  inputStream  )  ; 
url  =  null  ; 
inputStream  =  null  ; 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  ex  .  getMessage  (  )  )  ; 
} 
return   clip  ; 
} 
















public   static   synchronized   void   playSound  (  final   Clip   clip  )  { 
Thread   soundThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
clip  .  stop  (  )  ; 
clip  .  setFramePosition  (  0  )  ; 
clip  .  start  (  )  ; 
} 
}  )  ; 
soundThread  .  setName  (  "Sound"  )  ; 
soundThread  .  start  (  )  ; 
} 
} 

