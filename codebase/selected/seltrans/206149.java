package   gnu  .  javax  .  crypto  .  prng  ; 

import   gnu  .  java  .  security  .  Registry  ; 
import   gnu  .  java  .  security  .  hash  .  HashFactory  ; 
import   gnu  .  java  .  security  .  hash  .  IMessageDigest  ; 
import   gnu  .  java  .  security  .  prng  .  BasePRNG  ; 
import   gnu  .  java  .  security  .  prng  .  LimitReachedException  ; 
import   gnu  .  java  .  security  .  prng  .  RandomEvent  ; 
import   gnu  .  java  .  security  .  prng  .  RandomEventListener  ; 
import   gnu  .  javax  .  crypto  .  cipher  .  CipherFactory  ; 
import   gnu  .  javax  .  crypto  .  cipher  .  IBlockCipher  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  security  .  InvalidKeyException  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 































public   class   Fortuna   extends   BasePRNG   implements   Serializable  ,  RandomEventListener  { 

private   static   final   long   serialVersionUID  =  0xFACADE  ; 

private   static   final   int   SEED_FILE_SIZE  =  64  ; 

private   static   final   int   NUM_POOLS  =  32  ; 

private   static   final   int   MIN_POOL_SIZE  =  64  ; 

private   final   Generator   generator  ; 

private   final   IMessageDigest  [  ]  pools  ; 

private   long   lastReseed  ; 

private   int   pool  ; 

private   int   pool0Count  ; 

private   int   reseedCount  ; 

public   static   final   String   SEED  =  "gnu.crypto.prng.fortuna.seed"  ; 

public   Fortuna  (  )  { 
super  (  Registry  .  FORTUNA_PRNG  )  ; 
generator  =  new   Generator  (  CipherFactory  .  getInstance  (  Registry  .  RIJNDAEL_CIPHER  )  ,  HashFactory  .  getInstance  (  Registry  .  SHA256_HASH  )  )  ; 
pools  =  new   IMessageDigest  [  NUM_POOLS  ]  ; 
for  (  int   i  =  0  ;  i  <  NUM_POOLS  ;  i  ++  )  pools  [  i  ]  =  HashFactory  .  getInstance  (  Registry  .  SHA256_HASH  )  ; 
lastReseed  =  0  ; 
pool  =  0  ; 
pool0Count  =  0  ; 
buffer  =  new   byte  [  256  ]  ; 
} 

public   void   setup  (  Map   attributes  )  { 
lastReseed  =  0  ; 
reseedCount  =  0  ; 
pool  =  0  ; 
pool0Count  =  0  ; 
generator  .  init  (  attributes  )  ; 
try  { 
fillBlock  (  )  ; 
}  catch  (  LimitReachedException   shouldNotHappen  )  { 
throw   new   RuntimeException  (  shouldNotHappen  )  ; 
} 
} 

public   void   fillBlock  (  )  throws   LimitReachedException  { 
if  (  pool0Count  >=  MIN_POOL_SIZE  &&  System  .  currentTimeMillis  (  )  -  lastReseed  >  100  )  { 
reseedCount  ++  ; 
byte  [  ]  seed  =  new   byte  [  0  ]  ; 
for  (  int   i  =  0  ;  i  <  NUM_POOLS  ;  i  ++  )  if  (  reseedCount  %  (  1  <<  i  )  ==  0  )  generator  .  addRandomBytes  (  pools  [  i  ]  .  digest  (  )  )  ; 
lastReseed  =  System  .  currentTimeMillis  (  )  ; 
pool0Count  =  0  ; 
} 
generator  .  nextBytes  (  buffer  )  ; 
} 

public   void   addRandomByte  (  byte   b  )  { 
pools  [  pool  ]  .  update  (  b  )  ; 
if  (  pool  ==  0  )  pool0Count  ++  ; 
pool  =  (  pool  +  1  )  %  NUM_POOLS  ; 
} 

public   void   addRandomBytes  (  byte  [  ]  buf  ,  int   offset  ,  int   length  )  { 
pools  [  pool  ]  .  update  (  buf  ,  offset  ,  length  )  ; 
if  (  pool  ==  0  )  pool0Count  +=  length  ; 
pool  =  (  pool  +  1  )  %  NUM_POOLS  ; 
} 

public   void   addRandomEvent  (  RandomEvent   event  )  { 
if  (  event  .  getPoolNumber  (  )  <  0  ||  event  .  getPoolNumber  (  )  >=  pools  .  length  )  throw   new   IllegalArgumentException  (  "pool number out of range: "  +  event  .  getPoolNumber  (  )  )  ; 
pools  [  event  .  getPoolNumber  (  )  ]  .  update  (  event  .  getSourceNumber  (  )  )  ; 
pools  [  event  .  getPoolNumber  (  )  ]  .  update  (  (  byte  )  event  .  getData  (  )  .  length  )  ; 
pools  [  event  .  getPoolNumber  (  )  ]  .  update  (  event  .  getData  (  )  )  ; 
if  (  event  .  getPoolNumber  (  )  ==  0  )  pool0Count  +=  event  .  getData  (  )  .  length  ; 
} 

private   void   writeObject  (  ObjectOutputStream   out  )  throws   IOException  { 
byte  [  ]  seed  =  new   byte  [  SEED_FILE_SIZE  ]  ; 
try  { 
generator  .  nextBytes  (  seed  )  ; 
}  catch  (  LimitReachedException   shouldNeverHappen  )  { 
throw   new   Error  (  shouldNeverHappen  )  ; 
} 
out  .  write  (  seed  )  ; 
} 

private   void   readObject  (  ObjectInputStream   in  )  throws   IOException  { 
byte  [  ]  seed  =  new   byte  [  SEED_FILE_SIZE  ]  ; 
in  .  readFully  (  seed  )  ; 
generator  .  addRandomBytes  (  seed  )  ; 
} 






public   static   class   Generator   extends   BasePRNG   implements   Cloneable  { 

private   static   final   int   LIMIT  =  1  <<  20  ; 

private   final   IBlockCipher   cipher  ; 

private   final   IMessageDigest   hash  ; 

private   final   byte  [  ]  counter  ; 

private   final   byte  [  ]  key  ; 

private   boolean   seeded  ; 

public   Generator  (  final   IBlockCipher   cipher  ,  final   IMessageDigest   hash  )  { 
super  (  Registry  .  FORTUNA_GENERATOR_PRNG  )  ; 
this  .  cipher  =  cipher  ; 
this  .  hash  =  hash  ; 
counter  =  new   byte  [  cipher  .  defaultBlockSize  (  )  ]  ; 
buffer  =  new   byte  [  cipher  .  defaultBlockSize  (  )  ]  ; 
int   keysize  =  0  ; 
for  (  Iterator   it  =  cipher  .  keySizes  (  )  ;  it  .  hasNext  (  )  ;  )  { 
int   ks  =  (  (  Integer  )  it  .  next  (  )  )  .  intValue  (  )  ; 
if  (  ks  >  keysize  )  keysize  =  ks  ; 
if  (  keysize  >=  32  )  break  ; 
} 
key  =  new   byte  [  keysize  ]  ; 
} 

public   byte   nextByte  (  )  { 
byte  [  ]  b  =  new   byte  [  1  ]  ; 
nextBytes  (  b  ,  0  ,  1  )  ; 
return   b  [  0  ]  ; 
} 

public   void   nextBytes  (  byte  [  ]  out  ,  int   offset  ,  int   length  )  { 
if  (  !  seeded  )  throw   new   IllegalStateException  (  "generator not seeded"  )  ; 
int   count  =  0  ; 
do  { 
int   amount  =  Math  .  min  (  LIMIT  ,  length  -  count  )  ; 
try  { 
super  .  nextBytes  (  out  ,  offset  +  count  ,  amount  )  ; 
}  catch  (  LimitReachedException   shouldNeverHappen  )  { 
throw   new   Error  (  shouldNeverHappen  )  ; 
} 
count  +=  amount  ; 
for  (  int   i  =  0  ;  i  <  key  .  length  ;  i  +=  counter  .  length  )  { 
fillBlock  (  )  ; 
int   l  =  Math  .  min  (  key  .  length  -  i  ,  cipher  .  currentBlockSize  (  )  )  ; 
System  .  arraycopy  (  buffer  ,  0  ,  key  ,  i  ,  l  )  ; 
} 
resetKey  (  )  ; 
}  while  (  count  <  length  )  ; 
fillBlock  (  )  ; 
ndx  =  0  ; 
} 

public   void   addRandomByte  (  byte   b  )  { 
addRandomBytes  (  new   byte  [  ]  {  b  }  )  ; 
} 

public   void   addRandomBytes  (  byte  [  ]  seed  ,  int   offset  ,  int   length  )  { 
hash  .  update  (  key  )  ; 
hash  .  update  (  seed  ,  offset  ,  length  )  ; 
byte  [  ]  newkey  =  hash  .  digest  (  )  ; 
System  .  arraycopy  (  newkey  ,  0  ,  key  ,  0  ,  Math  .  min  (  key  .  length  ,  newkey  .  length  )  )  ; 
resetKey  (  )  ; 
incrementCounter  (  )  ; 
seeded  =  true  ; 
} 

public   void   fillBlock  (  )  { 
if  (  !  seeded  )  throw   new   IllegalStateException  (  "generator not seeded"  )  ; 
cipher  .  encryptBlock  (  counter  ,  0  ,  buffer  ,  0  )  ; 
incrementCounter  (  )  ; 
} 

public   void   setup  (  Map   attributes  )  { 
seeded  =  false  ; 
Arrays  .  fill  (  key  ,  (  byte  )  0  )  ; 
Arrays  .  fill  (  counter  ,  (  byte  )  0  )  ; 
byte  [  ]  seed  =  (  byte  [  ]  )  attributes  .  get  (  SEED  )  ; 
if  (  seed  !=  null  )  addRandomBytes  (  seed  )  ; 
fillBlock  (  )  ; 
} 





private   void   resetKey  (  )  { 
try  { 
cipher  .  reset  (  )  ; 
cipher  .  init  (  Collections  .  singletonMap  (  IBlockCipher  .  KEY_MATERIAL  ,  key  )  )  ; 
}  catch  (  InvalidKeyException   ike  )  { 
throw   new   Error  (  ike  )  ; 
}  catch  (  IllegalArgumentException   iae  )  { 
throw   new   Error  (  iae  )  ; 
} 
} 





private   void   incrementCounter  (  )  { 
for  (  int   i  =  0  ;  i  <  counter  .  length  ;  i  ++  )  { 
counter  [  i  ]  ++  ; 
if  (  counter  [  i  ]  !=  0  )  break  ; 
} 
} 
} 
} 

