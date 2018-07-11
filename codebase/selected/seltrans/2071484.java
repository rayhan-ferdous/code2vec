package   navigators  .  smart  .  tom  .  leaderchange  ; 

import   java  .  io  .  Externalizable  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInput  ; 
import   java  .  io  .  ObjectOutput  ; 
import   java  .  util  .  HashSet  ; 
import   navigators  .  smart  .  paxosatwar  .  executionmanager  .  TimestampValuePair  ; 






public   class   CollectData   implements   Externalizable  { 

private   int   pid  ; 

private   int   eid  ; 

private   TimestampValuePair   quorumWeaks  ; 

private   HashSet  <  TimestampValuePair  >  writeSet  ; 




public   CollectData  (  )  { 
pid  =  -  1  ; 
eid  =  -  1  ; 
quorumWeaks  =  null  ; 
writeSet  =  null  ; 
} 









public   CollectData  (  int   pid  ,  int   eid  ,  TimestampValuePair   quorumWeaks  ,  HashSet  <  TimestampValuePair  >  writeSet  )  { 
this  .  pid  =  pid  ; 
this  .  eid  =  eid  ; 
this  .  quorumWeaks  =  quorumWeaks  ; 
this  .  writeSet  =  writeSet  ; 
} 





public   int   getEid  (  )  { 
return   eid  ; 
} 





public   int   getPid  (  )  { 
return   pid  ; 
} 





public   TimestampValuePair   getQuorumWeaks  (  )  { 
return   quorumWeaks  ; 
} 





public   HashSet  <  TimestampValuePair  >  getWriteSet  (  )  { 
return   writeSet  ; 
} 

public   boolean   equals  (  Object   obj  )  { 
if  (  obj   instanceof   CollectData  )  { 
CollectData   c  =  (  CollectData  )  obj  ; 
if  (  c  .  pid  ==  pid  )  return   true  ; 
} 
return   false  ; 
} 

public   int   hashCode  (  )  { 
return   pid  ; 
} 

public   void   writeExternal  (  ObjectOutput   out  )  throws   IOException  { 
out  .  writeInt  (  pid  )  ; 
out  .  writeInt  (  eid  )  ; 
out  .  writeObject  (  quorumWeaks  )  ; 
out  .  writeObject  (  writeSet  )  ; 
} 

public   void   readExternal  (  ObjectInput   in  )  throws   IOException  ,  ClassNotFoundException  { 
pid  =  in  .  readInt  (  )  ; 
eid  =  in  .  readInt  (  )  ; 
quorumWeaks  =  (  TimestampValuePair  )  in  .  readObject  (  )  ; 
writeSet  =  (  HashSet  <  TimestampValuePair  >  )  in  .  readObject  (  )  ; 
} 
} 

