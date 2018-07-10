package   verjinxer  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  ByteOrder  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  Arrays  ; 










public   class   HugeByteArray  { 


private   static   final   int   BYTESPERELEMENT  =  Byte  .  SIZE  /  8  ; 


public   final   long   length  ; 

private   static   final   int   BITS  =  30  ; 

private   static   final   int   BINSIZE  =  1  <<  BITS  ; 

private   static   final   int   BITMASK_HIGH  =  (  -  1  )  <<  BITS  ; 

private   static   final   int   BITMASK_LOW  =  ~  BITMASK_HIGH  ; 


private   final   byte  [  ]  [  ]  arrays  ; 

private   final   int   bins  ; 







public   HugeByteArray  (  final   long   length  )  { 
this  .  length  =  length  ; 
bins  =  (  int  )  (  (  length  -  1  )  >  >  BITS  )  +  1  ; 
arrays  =  new   byte  [  bins  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  bins  -  1  ;  ++  i  )  { 
arrays  [  i  ]  =  new   byte  [  BINSIZE  ]  ; 
} 
if  (  bins  >  0  )  { 
if  (  (  length  &  BITMASK_LOW  )  >  0  )  arrays  [  bins  -  1  ]  =  new   byte  [  (  int  )  (  length  &  BITMASK_LOW  )  ]  ;  else   arrays  [  bins  -  1  ]  =  new   byte  [  BINSIZE  ]  ; 
assert   arrays  [  bins  -  1  ]  .  length  >  0  ; 
} 
} 







public   HugeByteArray  (  final   HugeByteArray   other  )  { 
length  =  other  .  length  ; 
bins  =  other  .  bins  ; 
arrays  =  new   byte  [  bins  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  bins  ;  i  ++  )  { 
arrays  [  i  ]  =  new   byte  [  other  .  arrays  [  i  ]  .  length  ]  ; 
assert   other  .  arrays  [  i  ]  !=  null  :  String  .  format  (  "HugeByteArray copy constructor: other.arrays[%d] is null"  ,  i  )  ; 
assert   this  .  arrays  [  i  ]  !=  null  :  String  .  format  (  "HugeByteArray copy constructor: this.arrays[%d] is null"  ,  i  )  ; 
for  (  int   j  =  0  ;  j  <  arrays  [  i  ]  .  length  ;  j  ++  )  { 
arrays  [  i  ]  =  other  .  arrays  [  i  ]  ; 
} 
} 
} 







public   HugeByteArray  (  final   byte  [  ]  other  )  { 
length  =  other  .  length  ; 
bins  =  (  int  )  (  (  length  -  1  )  >  >  BITS  )  +  1  ; 
arrays  =  new   byte  [  bins  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  bins  -  1  ;  ++  i  )  { 
arrays  [  i  ]  =  new   byte  [  BINSIZE  ]  ; 
} 
if  (  bins  >  0  )  { 
if  (  (  length  &  BITMASK_LOW  )  >  0  )  arrays  [  bins  -  1  ]  =  new   byte  [  (  int  )  (  length  &  BITMASK_LOW  )  ]  ;  else   arrays  [  bins  -  1  ]  =  new   byte  [  BINSIZE  ]  ; 
assert   arrays  [  bins  -  1  ]  .  length  >  0  ; 
} 
int   otherPos  =  0  ; 
for  (  int   bin  =  0  ;  bin  <  bins  ;  bin  ++  )  { 
for  (  int   binPos  =  0  ;  binPos  <  arrays  [  bin  ]  .  length  ;  binPos  ++  )  { 
arrays  [  bin  ]  [  binPos  ]  =  other  [  otherPos  ++  ]  ; 
} 
} 
assert   otherPos  ==  other  .  length  ; 
} 








public   final   byte   get  (  final   long   i  )  { 
return   arrays  [  (  int  )  (  i  >  >  BITS  )  ]  [  (  int  )  (  i  &  BITMASK_LOW  )  ]  ; 
} 









public   final   void   set  (  final   long   i  ,  final   byte   value  )  { 
arrays  [  (  int  )  (  i  >  >  BITS  )  ]  [  (  int  )  (  i  &  BITMASK_LOW  )  ]  =  value  ; 
} 




public   int   getBins  (  )  { 
return   bins  ; 
} 







public   final   void   fill  (  final   byte   value  )  { 
for  (  int   i  =  0  ;  i  <  bins  ;  i  ++  )  { 
Arrays  .  fill  (  arrays  [  i  ]  ,  value  )  ; 
} 
} 

@  Override 
public   boolean   equals  (  Object   other  )  { 
if  (  !  (  other   instanceof   HugeByteArray  )  )  { 
return   false  ; 
} 
HugeByteArray   o  =  (  HugeByteArray  )  other  ; 
if  (  length  !=  o  .  length  )  { 
return   false  ; 
} 
if  (  bins  !=  o  .  bins  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  o  .  bins  ;  i  ++  )  { 
if  (  !  Arrays  .  equals  (  arrays  [  i  ]  ,  o  .  arrays  [  i  ]  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 

@  Override 
public   int   hashCode  (  )  { 
int   hash  =  5  ; 
hash  =  53  *  hash  +  (  int  )  (  this  .  length  ^  (  this  .  length  >  >  >  32  )  )  ; 
hash  =  53  *  hash  +  this  .  bins  ; 
for  (  long   i  =  0  ;  i  <  length  ;  i  ++  )  { 
hash  =  53  *  hash  +  (  int  )  get  (  i  )  ; 
} 
return   hash  ; 
} 












public   long   binarySearch  (  final   byte   key  ,  final   long   fromIndex  ,  final   long   toIndex  )  { 
long   low  =  fromIndex  ; 
long   high  =  toIndex  -  1  ; 
while  (  low  <=  high  )  { 
long   mid  =  (  low  +  high  )  >  >  >  1  ; 
final   byte   midVal  =  get  (  mid  )  ; 
if  (  midVal  <  key  )  { 
low  =  mid  +  1  ; 
}  else   if  (  midVal  >  key  )  { 
high  =  mid  -  1  ; 
}  else  { 
return   mid  ; 
} 
} 
return  -  (  low  +  1  )  ; 
} 










public   long   binarySearch  (  final   byte   key  )  { 
return   binarySearch  (  key  ,  0  ,  length  )  ; 
} 


public   void   sort  (  )  { 
sort  (  0  ,  length  )  ; 
} 







public   void   sort  (  final   long   off  ,  final   long   len  )  { 
if  (  len  <  7  )  { 
for  (  long   i  =  off  ;  i  <  len  +  off  ;  i  ++  )  { 
for  (  long   j  =  i  ;  j  >  off  &&  get  (  j  -  1  )  >  get  (  j  )  ;  j  --  )  { 
swap  (  j  ,  j  -  1  )  ; 
} 
} 
return  ; 
} 
long   m  =  off  +  (  len  >  >  1  )  ; 
if  (  len  >  7  )  { 
long   l  =  off  ; 
long   n  =  off  +  len  -  1  ; 
if  (  len  >  40  )  { 
long   s  =  len  /  8  ; 
l  =  med3  (  l  ,  l  +  s  ,  l  +  2  *  s  )  ; 
m  =  med3  (  m  -  s  ,  m  ,  m  +  s  )  ; 
n  =  med3  (  n  -  2  *  s  ,  n  -  s  ,  n  )  ; 
} 
m  =  med3  (  l  ,  m  ,  n  )  ; 
} 
final   byte   v  =  get  (  m  )  ; 
long   a  =  off  ,  b  =  a  ,  c  =  off  +  len  -  1  ,  d  =  c  ; 
while  (  true  )  { 
while  (  b  <=  c  &&  get  (  b  )  <=  v  )  { 
if  (  get  (  b  )  ==  v  )  { 
swap  (  a  ++  ,  b  )  ; 
} 
b  ++  ; 
} 
while  (  c  >=  b  &&  get  (  c  )  >=  v  )  { 
if  (  get  (  c  )  ==  v  )  { 
swap  (  c  ,  d  --  )  ; 
} 
c  --  ; 
} 
if  (  b  >  c  )  { 
break  ; 
} 
swap  (  b  ++  ,  c  --  )  ; 
} 
long   s  ,  n  =  off  +  len  ; 
s  =  Math  .  min  (  a  -  off  ,  b  -  a  )  ; 
vecswap  (  off  ,  b  -  s  ,  s  )  ; 
s  =  Math  .  min  (  d  -  c  ,  n  -  d  -  1  )  ; 
vecswap  (  b  ,  n  -  s  ,  s  )  ; 
if  (  (  s  =  b  -  a  )  >  1  )  { 
sort  (  off  ,  s  )  ; 
} 
if  (  (  s  =  d  -  c  )  >  1  )  { 
sort  (  n  -  s  ,  s  )  ; 
} 
} 







public   void   swap  (  final   long   i  ,  final   long   j  )  { 
final   byte   t  =  get  (  i  )  ; 
set  (  i  ,  get  (  j  )  )  ; 
set  (  j  ,  t  )  ; 
} 









private   void   vecswap  (  long   i  ,  long   j  ,  final   long   n  )  { 
for  (  long   t  =  0  ;  t  <  n  ;  t  ++  ,  i  ++  ,  j  ++  )  swap  (  i  ,  j  )  ; 
} 




private   long   med3  (  final   long   a  ,  final   long   b  ,  final   long   c  )  { 
return  (  get  (  a  )  <  get  (  b  )  ?  (  get  (  b  )  <  get  (  c  )  ?  b  :  get  (  a  )  <  get  (  c  )  ?  c  :  a  )  :  (  get  (  b  )  >  get  (  c  )  ?  b  :  get  (  a  )  >  get  (  c  )  ?  c  :  a  )  )  ; 
} 




public   HugeByteArray   copy  (  )  { 
return   new   HugeByteArray  (  this  )  ; 
} 











public   HugeByteArray   copyRange  (  final   long   from  ,  final   long   to  )  { 
final   long   newsize  =  to  -  from  ; 
final   HugeByteArray   c  =  new   HugeByteArray  (  newsize  )  ; 
for  (  long   i  =  0  ;  i  <  newsize  ;  i  ++  )  { 
c  .  set  (  i  ,  get  (  i  +  from  )  )  ; 
} 
return   c  ; 
} 







@  Override 
public   String   toString  (  )  { 
if  (  length  ==  0  )  { 
return  "[]"  ; 
} 
if  (  length  <=  20  )  { 
return  (  Arrays  .  toString  (  arrays  [  0  ]  )  )  ; 
} 
StringBuilder   sb  =  new   StringBuilder  (  1024  )  ; 
sb  .  append  (  "["  )  ; 
for  (  int   i  =  0  ;  i  <  9  ;  i  ++  )  { 
sb  .  append  (  arrays  [  0  ]  [  i  ]  )  ; 
sb  .  append  (  ", "  )  ; 
} 
sb  .  append  (  String  .  format  (  "...(%d numbers total)..., "  ,  length  )  )  ; 
for  (  long   i  =  0  ;  i  <  8  ;  i  ++  )  { 
sb  .  append  (  get  (  length  -  10  +  i  )  )  ; 
sb  .  append  (  ", "  )  ; 
} 
sb  .  append  (  get  (  length  -  1  )  )  ; 
sb  .  append  (  "]"  )  ; 
return   sb  .  toString  (  )  ; 
} 

private   static   final   long   BUFSIZE  =  1024  *  1024  ; 

private   final   ByteBuffer   _bb  =  ByteBuffer  .  allocateDirect  (  (  int  )  BUFSIZE  )  .  order  (  ByteOrder  .  nativeOrder  (  )  )  ; 


















public   final   void   read  (  final   File   file  ,  long   start  ,  long   nItems  ,  final   long   fpos  )  throws   IOException  { 
final   FileChannel   channel  =  new   FileInputStream  (  file  )  .  getChannel  (  )  ; 
if  (  fpos  >=  0  )  channel  .  position  (  BYTESPERELEMENT  *  fpos  )  ; 
final   ByteBuffer   ib  =  _bb  ; 
if  (  nItems  <  0  )  nItems  =  (  channel  .  size  (  )  -  channel  .  position  (  )  )  /  BYTESPERELEMENT  ; 
while  (  nItems  >  0  )  { 
int   bytestoread  =  (  int  )  (  (  nItems  *  BYTESPERELEMENT  <  BUFSIZE  )  ?  nItems  *  BYTESPERELEMENT  :  BUFSIZE  )  ; 
final   int   itemstoread  =  bytestoread  /  BYTESPERELEMENT  ; 
_bb  .  position  (  0  )  .  limit  (  bytestoread  )  ; 
while  (  (  bytestoread  -=  channel  .  read  (  _bb  )  )  >  0  )  { 
} 
ib  .  position  (  0  )  .  limit  (  itemstoread  )  ; 
final   int   movi  =  (  int  )  (  start  /  BINSIZE  )  ; 
final   int   movj  =  (  int  )  (  start  %  BINSIZE  )  ; 
final   int   movs  =  arrays  [  movi  ]  .  length  -  movj  ; 
final   int   tomove  =  (  itemstoread  >  movs  )  ?  movs  :  itemstoread  ; 
ib  .  get  (  arrays  [  movi  ]  ,  movj  ,  tomove  )  ; 
if  (  tomove  <  itemstoread  )  ib  .  get  (  arrays  [  movi  +  1  ]  ,  0  ,  itemstoread  -  tomove  )  ; 
start  +=  itemstoread  ; 
nItems  -=  itemstoread  ; 
} 
channel  .  close  (  )  ; 
} 









public   static   final   HugeByteArray   fromFile  (  final   File   file  )  throws   IOException  { 
final   FileChannel   channel  =  new   FileInputStream  (  file  )  .  getChannel  (  )  ; 
final   long   flen  =  channel  .  size  (  )  ; 
if  (  flen  %  BYTESPERELEMENT  !=  0  )  throw   new   IOException  (  "File size not compatible with HugeByteArray"  )  ; 
final   HugeByteArray   a  =  new   HugeByteArray  (  flen  /  BYTESPERELEMENT  )  ; 
a  .  read  (  file  ,  0  ,  flen  /  BYTESPERELEMENT  ,  0  )  ; 
return   a  ; 
} 
} 

