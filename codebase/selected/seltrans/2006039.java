package   com  .  sleepycat  .  je  ; 




public   class   TransactionConfig   implements   Cloneable  { 





public   static   final   TransactionConfig   DEFAULT  =  new   TransactionConfig  (  )  ; 

private   boolean   sync  =  false  ; 

private   boolean   noSync  =  false  ; 

private   boolean   writeNoSync  =  false  ; 

private   Durability   durability  =  null  ; 

private   ReplicaConsistencyPolicy   consistencyPolicy  ; 

private   boolean   noWait  =  false  ; 

private   boolean   readUncommitted  =  false  ; 

private   boolean   readCommitted  =  false  ; 

private   boolean   serializableIsolation  =  false  ; 





public   TransactionConfig  (  )  { 
} 






















public   Durability   getDurabilityFromSync  (  )  { 
if  (  sync  )  { 
return   Durability  .  COMMIT_SYNC  ; 
}  else   if  (  writeNoSync  )  { 
return   Durability  .  COMMIT_WRITE_NO_SYNC  ; 
}  else   if  (  noSync  )  { 
return   Durability  .  COMMIT_NO_SYNC  ; 
} 
return   Durability  .  COMMIT_SYNC  ; 
} 




















public   TransactionConfig   setSync  (  boolean   sync  )  { 
checkMixedMode  (  sync  ,  noSync  ,  writeNoSync  ,  durability  )  ; 
this  .  sync  =  sync  ; 
return   this  ; 
} 








public   boolean   getSync  (  )  { 
return   sync  ; 
} 
























public   TransactionConfig   setNoSync  (  boolean   noSync  )  { 
checkMixedMode  (  sync  ,  noSync  ,  writeNoSync  ,  durability  )  ; 
this  .  noSync  =  noSync  ; 
return   this  ; 
} 










public   boolean   getNoSync  (  )  { 
return   noSync  ; 
} 























public   TransactionConfig   setWriteNoSync  (  boolean   writeNoSync  )  { 
checkMixedMode  (  sync  ,  noSync  ,  writeNoSync  ,  durability  )  ; 
this  .  writeNoSync  =  writeNoSync  ; 
return   this  ; 
} 










public   boolean   getWriteNoSync  (  )  { 
return   writeNoSync  ; 
} 













public   TransactionConfig   setDurability  (  Durability   durability  )  { 
checkMixedMode  (  sync  ,  noSync  ,  writeNoSync  ,  durability  )  ; 
this  .  durability  =  durability  ; 
return   this  ; 
} 









public   Durability   getDurability  (  )  { 
return   durability  ; 
} 






void   overrideDurability  (  Durability   durability  )  { 
sync  =  false  ; 
noSync  =  false  ; 
writeNoSync  =  false  ; 
this  .  durability  =  durability  ; 
} 








public   TransactionConfig   setConsistencyPolicy  (  ReplicaConsistencyPolicy   consistencyPolicy  )  { 
this  .  consistencyPolicy  =  consistencyPolicy  ; 
return   this  ; 
} 






public   ReplicaConsistencyPolicy   getConsistencyPolicy  (  )  { 
return   consistencyPolicy  ; 
} 














public   TransactionConfig   setNoWait  (  boolean   noWait  )  { 
this  .  noWait  =  noWait  ; 
return   this  ; 
} 








public   boolean   getNoWait  (  )  { 
return   noWait  ; 
} 












public   TransactionConfig   setReadUncommitted  (  boolean   readUncommitted  )  { 
this  .  readUncommitted  =  readUncommitted  ; 
return   this  ; 
} 










public   boolean   getReadUncommitted  (  )  { 
return   readUncommitted  ; 
} 















public   TransactionConfig   setReadCommitted  (  boolean   readCommitted  )  { 
this  .  readCommitted  =  readCommitted  ; 
return   this  ; 
} 










public   boolean   getReadCommitted  (  )  { 
return   readCommitted  ; 
} 


















public   TransactionConfig   setSerializableIsolation  (  boolean   serializableIsolation  )  { 
this  .  serializableIsolation  =  serializableIsolation  ; 
return   this  ; 
} 










public   boolean   getSerializableIsolation  (  )  { 
return   serializableIsolation  ; 
} 




@  Override 
public   TransactionConfig   clone  (  )  { 
try  { 
return  (  TransactionConfig  )  super  .  clone  (  )  ; 
}  catch  (  CloneNotSupportedException   willNeverOccur  )  { 
return   null  ; 
} 
} 











static   void   checkMixedMode  (  boolean   sync  ,  boolean   noSync  ,  boolean   writeNoSync  ,  Durability   durability  )  throws   IllegalArgumentException  { 
if  (  (  sync  ||  noSync  ||  writeNoSync  )  &&  (  durability  !=  null  )  )  { 
throw   new   IllegalArgumentException  (  "Mixed use of deprecated and current durability APIs is "  +  "not supported"  )  ; 
} 
if  (  (  sync  &&  noSync  )  ||  (  sync  &&  writeNoSync  )  ||  (  noSync  &&  writeNoSync  )  )  { 
throw   new   IllegalArgumentException  (  "Only one of TxnSync, TxnNoSync, and TxnWriteNoSync "  +  "can be set."  )  ; 
} 
} 






@  Override 
public   String   toString  (  )  { 
return  "sync="  +  sync  +  "\nnoSync="  +  noSync  +  "\nwriteNoSync="  +  writeNoSync  +  "\ndurability="  +  durability  +  "\nconsistencyPolicy="  +  consistencyPolicy  +  "\nnoWait="  +  noWait  +  "\nreadUncommitted="  +  readUncommitted  +  "\nreadCommitted="  +  readCommitted  +  "\nSerializableIsolation="  +  serializableIsolation  +  "\n"  ; 
} 
} 

