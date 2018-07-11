package   org  .  loon  .  framework  .  android  .  game  .  utils  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  HashMap  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  LSystem  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  geom  .  RectBox  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  geom  .  Rectangle  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  geom  .  Shape  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  graphics  .  LColor  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  graphics  .  LImage  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  graphics  .  device  .  LGraphics  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  resource  .  Resources  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  store  .  InvalidRecordIDException  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  store  .  RecordEnumeration  ; 
import   org  .  loon  .  framework  .  android  .  game  .  core  .  store  .  RecordStore  ; 
import   org  .  loon  .  framework  .  android  .  game  .  utils  .  collection  .  ArrayByte  ; 
import   android  .  graphics  .  Bitmap  ; 
import   android  .  graphics  .  BitmapFactory  ; 
import   android  .  graphics  .  Canvas  ; 
import   android  .  graphics  .  Matrix  ; 
import   android  .  graphics  .  Rect  ; 
import   android  .  graphics  .  Bitmap  .  Config  ; 






















public   class   GraphicsUtils  { 

public   static   final   Matrix   matrix  =  new   Matrix  (  )  ; 

public   static   final   Canvas   canvas  =  new   Canvas  (  )  ; 

private   static   final   HashMap  <  String  ,  LImage  >  lazyImages  =  new   HashMap  <  String  ,  LImage  >  (  LSystem  .  DEFAULT_MAX_CACHE_SIZE  )  ; 

private   static   final   HashMap  <  String  ,  LImage  [  ]  [  ]  >  lazySplitMap  =  new   HashMap  <  String  ,  LImage  [  ]  [  ]  >  (  LSystem  .  DEFAULT_MAX_CACHE_SIZE  )  ; 

public   static   final   BitmapFactory  .  Options   ARGB4444options  =  new   BitmapFactory  .  Options  (  )  ; 

public   static   final   BitmapFactory  .  Options   ARGB8888options  =  new   BitmapFactory  .  Options  (  )  ; 

public   static   final   BitmapFactory  .  Options   RGB565options  =  new   BitmapFactory  .  Options  (  )  ; 

static  { 
ARGB8888options  .  inDither  =  false  ; 
ARGB8888options  .  inJustDecodeBounds  =  false  ; 
ARGB8888options  .  inPreferredConfig  =  Bitmap  .  Config  .  ARGB_8888  ; 
ARGB4444options  .  inDither  =  false  ; 
ARGB4444options  .  inJustDecodeBounds  =  false  ; 
ARGB4444options  .  inPreferredConfig  =  Bitmap  .  Config  .  ARGB_4444  ; 
RGB565options  .  inDither  =  false  ; 
RGB565options  .  inJustDecodeBounds  =  false  ; 
RGB565options  .  inPreferredConfig  =  Bitmap  .  Config  .  RGB_565  ; 
try  { 
BitmapFactory  .  Options  .  class  .  getField  (  "inPurgeable"  )  .  set  (  ARGB8888options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inPurgeable"  )  .  set  (  ARGB4444options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inPurgeable"  )  .  set  (  RGB565options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inInputShareable"  )  .  set  (  ARGB8888options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inInputShareable"  )  .  set  (  ARGB4444options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inInputShareable"  )  .  set  (  RGB565options  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
} 
try  { 
BitmapFactory  .  Options  .  class  .  getField  (  "inScaled"  )  .  set  (  ARGB8888options  ,  false  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inScaled"  )  .  set  (  ARGB4444options  ,  false  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inScaled"  )  .  set  (  RGB565options  ,  false  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 

public   static   LImage   createImage  (  int   width  ,  int   height  ,  LColor   c  )  { 
LImage   image  =  new   LImage  (  width  ,  height  ,  false  )  ; 
LGraphics   g  =  image  .  getLGraphics  (  )  ; 
g  .  setColor  (  c  )  ; 
g  .  fillRect  (  0  ,  0  ,  width  ,  height  )  ; 
g  .  dispose  (  )  ; 
return   image  ; 
} 








public   static   final   Bitmap   loadBitmap  (  InputStream   in  ,  boolean   transparency  )  { 
if  (  LSystem  .  IMAGE_SIZE  !=  0  )  { 
return   loadSizeBitmap  (  in  ,  LSystem  .  IMAGE_SIZE  ,  transparency  )  ; 
}  else  { 
return   BitmapFactory  .  decodeStream  (  in  ,  null  ,  transparency  ?  ARGB4444options  :  RGB565options  )  ; 
} 
} 








public   static   final   Bitmap   loadBitmap  (  String   resName  ,  boolean   transparency  )  { 
if  (  LSystem  .  IMAGE_SIZE  !=  0  )  { 
return   loadSizeBitmap  (  resName  ,  LSystem  .  IMAGE_SIZE  ,  transparency  )  ; 
}  else  { 
try  { 
return   loadBitmap  (  Resources  .  openResource  (  resName  )  ,  transparency  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
} 
} 
} 









public   static   final   Bitmap   loadScaleBitmap  (  String   resName  ,  int   width  ,  int   height  )  { 
try  { 
BitmapFactory  .  Options   opts  =  new   BitmapFactory  .  Options  (  )  ; 
opts  .  inJustDecodeBounds  =  true  ; 
BitmapFactory  .  decodeStream  (  Resources  .  openResource  (  resName  )  ,  null  ,  opts  )  ; 
int   scaleWidth  =  (  int  )  Math  .  floor  (  (  double  )  opts  .  outWidth  /  width  )  ; 
int   scaleHeight  =  (  int  )  Math  .  floor  (  (  double  )  opts  .  outHeight  /  height  )  ; 
opts  .  inJustDecodeBounds  =  false  ; 
opts  .  inSampleSize  =  Math  .  min  (  scaleWidth  ,  scaleHeight  )  ; 
return   BitmapFactory  .  decodeStream  (  Resources  .  openResource  (  resName  )  ,  null  ,  opts  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
} 
} 







public   static   final   Bitmap   loadBitmap  (  String   resName  )  { 
if  (  LSystem  .  IMAGE_SIZE  !=  0  )  { 
return   loadSizeBitmap  (  resName  ,  LSystem  .  IMAGE_SIZE  ,  true  )  ; 
}  else  { 
try  { 
BitmapFactory  .  Options   opts  =  new   BitmapFactory  .  Options  (  )  ; 
return   BitmapFactory  .  decodeStream  (  Resources  .  openResource  (  resName  )  ,  null  ,  opts  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
} 
} 
} 









public   static   final   LImage   loadScaleImage  (  String   resName  ,  int   width  ,  int   height  )  { 
return   new   LImage  (  loadScaleBitmap  (  resName  ,  width  ,  height  )  )  ; 
} 







public   static   final   LImage   loadImage  (  InputStream   in  ,  boolean   transparency  )  { 
if  (  LSystem  .  IMAGE_SIZE  !=  0  )  { 
return   loadPoorImage  (  in  ,  LSystem  .  IMAGE_SIZE  ,  transparency  )  ; 
}  else  { 
Bitmap   bitmap  =  BitmapFactory  .  decodeStream  (  in  ,  null  ,  transparency  ?  ARGB4444options  :  RGB565options  )  ; 
int   w  =  bitmap  .  getWidth  (  )  ; 
int   h  =  bitmap  .  getHeight  (  )  ; 
if  (  (  w  <  16  ||  w  >  128  )  &&  (  h  <  16  ||  h  >  128  )  )  { 
return   new   LImage  (  bitmap  )  ; 
} 
return   filterBitmapTo565  (  bitmap  ,  w  ,  h  )  ; 
} 
} 









public   static   final   LImage   loadPoorImage  (  String   resName  ,  int   sampleSize  ,  boolean   transparency  )  { 
return   new   LImage  (  loadSizeBitmap  (  resName  ,  sampleSize  ,  transparency  )  )  ; 
} 








public   static   final   Bitmap   loadSizeBitmap  (  String   resName  ,  int   sampleSize  ,  boolean   transparency  )  { 
try  { 
return   loadSizeBitmap  (  Resources  .  openResource  (  resName  )  ,  sampleSize  ,  transparency  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
} 
} 









public   static   final   LImage   loadPoorImage  (  InputStream   in  ,  int   sampleSize  ,  boolean   transparency  )  { 
return   new   LImage  (  loadSizeBitmap  (  in  ,  sampleSize  ,  transparency  )  )  ; 
} 









public   static   final   Bitmap   loadSizeBitmap  (  InputStream   in  ,  int   sampleSize  ,  boolean   transparency  )  { 
ArrayByte   byteArray  =  new   ArrayByte  (  )  ; 
try  { 
byteArray  .  write  (  in  )  ; 
byteArray  .  reset  (  )  ; 
return   loadSizeBitmap  (  byteArray  .  getData  (  )  ,  sampleSize  ,  transparency  )  ; 
}  catch  (  IOException   ex  )  { 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
in  =  null  ; 
} 
return   BitmapFactory  .  decodeStream  (  in  ,  null  ,  transparency  ?  ARGB4444options  :  RGB565options  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "Image not found!"  )  ; 
} 
}  finally  { 
byteArray  =  null  ; 
} 
} 









public   static   final   LImage   loadPoorImage  (  byte  [  ]  bytes  ,  int   sampleSize  ,  boolean   transparency  )  { 
return   new   LImage  (  loadSizeBitmap  (  bytes  ,  sampleSize  ,  transparency  )  )  ; 
} 









public   static   final   Bitmap   loadSizeBitmap  (  byte  [  ]  bytes  ,  int   sampleSize  ,  boolean   transparency  )  { 
BitmapFactory  .  Options   options  =  new   BitmapFactory  .  Options  (  )  ; 
options  .  inDither  =  false  ; 
options  .  inSampleSize  =  sampleSize  ; 
options  .  inJustDecodeBounds  =  false  ; 
options  .  inPreferredConfig  =  transparency  ?  Bitmap  .  Config  .  ARGB_4444  :  Bitmap  .  Config  .  RGB_565  ; 
try  { 
BitmapFactory  .  Options  .  class  .  getField  (  "inPurgeable"  )  .  set  (  options  ,  true  )  ; 
BitmapFactory  .  Options  .  class  .  getField  (  "inInputShareable"  )  .  set  (  options  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
} 
return   BitmapFactory  .  decodeByteArray  (  bytes  ,  0  ,  bytes  .  length  ,  options  )  ; 
} 









public   static   final   LImage   filterBitmapTo565  (  Bitmap   src  ,  int   w  ,  int   h  )  { 
return   new   LImage  (  filterBitmapTo565Bitmap  (  src  ,  w  ,  h  )  )  ; 
} 









public   static   final   Bitmap   filterBitmapTo565Bitmap  (  Bitmap   src  ,  int   w  ,  int   h  )  { 
Config   config  =  src  .  getConfig  (  )  ; 
if  (  config  !=  Config  .  RGB_565  &&  config  !=  Config  .  ARGB_4444  &&  config  !=  Config  .  ALPHA_8  )  { 
boolean   isOpaque  =  true  ; 
int   pixel  ; 
int   size  =  w  *  h  ; 
int  [  ]  pixels  =  new   int  [  size  ]  ; 
src  .  getPixels  (  pixels  ,  0  ,  w  ,  0  ,  0  ,  w  ,  h  )  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
pixel  =  LColor  .  premultiply  (  pixels  [  i  ]  )  ; 
if  (  isOpaque  &&  (  pixel  >  >  >  24  )  <  255  )  { 
isOpaque  =  false  ; 
break  ; 
} 
} 
pixels  =  null  ; 
if  (  isOpaque  )  { 
Bitmap   newBitmap  =  src  .  copy  (  Config  .  RGB_565  ,  false  )  ; 
src  .  recycle  (  )  ; 
src  =  null  ; 
return   newBitmap  ; 
} 
} 
return   src  ; 
} 








public   static   final   LImage   load8888Image  (  InputStream   in  )  { 
return   new   LImage  (  load8888Bitmap  (  in  )  )  ; 
} 







public   static   final   LImage   load8888Image  (  String   fileName  )  { 
return   new   LImage  (  load8888Bitmap  (  fileName  )  )  ; 
} 







public   static   final   LImage   load8888Image  (  byte  [  ]  buffer  )  { 
return   new   LImage  (  load8888Bitmap  (  buffer  )  )  ; 
} 







public   static   final   Bitmap   load8888Bitmap  (  InputStream   in  )  { 
Bitmap   bitmap  =  BitmapFactory  .  decodeStream  (  in  ,  null  ,  ARGB8888options  )  ; 
if  (  bitmap  .  getConfig  (  )  !=  Config  .  ARGB_8888  )  { 
Bitmap   newBitmap  =  bitmap  .  copy  (  Config  .  ARGB_8888  ,  false  )  ; 
bitmap  .  recycle  (  )  ; 
bitmap  =  null  ; 
return   newBitmap  ; 
} 
return   bitmap  ; 
} 







public   static   final   Bitmap   load8888Bitmap  (  byte  [  ]  buffer  )  { 
return   BitmapFactory  .  decodeByteArray  (  buffer  ,  0  ,  buffer  .  length  ,  ARGB8888options  )  ; 
} 







public   static   final   Bitmap   load8888Bitmap  (  String   fileName  )  { 
try  { 
Bitmap   bitmap  =  BitmapFactory  .  decodeStream  (  Resources  .  openResource  (  fileName  )  ,  null  ,  ARGB8888options  )  ; 
if  (  bitmap  .  getConfig  (  )  !=  Config  .  ARGB_8888  )  { 
Bitmap   newBitmap  =  bitmap  .  copy  (  Config  .  ARGB_8888  ,  false  )  ; 
bitmap  .  recycle  (  )  ; 
bitmap  =  null  ; 
return   newBitmap  ; 
} 
return   bitmap  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  (  "File not found. ( "  +  fileName  +  " )"  )  .  intern  (  )  )  ; 
} 
} 







public   static   final   LImage   loadImage  (  byte  [  ]  buffer  ,  boolean   transparency  )  { 
return   new   LImage  (  loadBitmap  (  buffer  ,  transparency  )  )  ; 
} 







public   static   final   Bitmap   loadBitmap  (  byte  [  ]  buffer  ,  boolean   transparency  )  { 
if  (  LSystem  .  IMAGE_SIZE  !=  0  )  { 
return   loadSizeBitmap  (  buffer  ,  LSystem  .  IMAGE_SIZE  ,  transparency  )  ; 
}  else  { 
return   BitmapFactory  .  decodeByteArray  (  buffer  ,  0  ,  buffer  .  length  ,  transparency  ?  ARGB4444options  :  RGB565options  )  ; 
} 
} 









public   static   final   LImage   loadImage  (  byte  [  ]  imageData  ,  int   imageOffset  ,  int   imageLength  ,  boolean   transparency  )  { 
return   new   LImage  (  loadBitmap  (  imageData  ,  imageOffset  ,  imageLength  ,  transparency  )  )  ; 
} 









public   static   final   Bitmap   loadBitmap  (  byte  [  ]  imageData  ,  int   imageOffset  ,  int   imageLength  ,  boolean   transparency  )  { 
return   BitmapFactory  .  decodeByteArray  (  imageData  ,  imageOffset  ,  imageLength  ,  transparency  ?  ARGB4444options  :  RGB565options  )  ; 
} 







public   static   final   LImage   loadImage  (  final   String   resName  ,  boolean   transparency  )  { 
if  (  resName  ==  null  )  { 
return   null  ; 
} 
String   keyName  =  resName  .  toLowerCase  (  )  ; 
LImage   image  =  (  LImage  )  lazyImages  .  get  (  keyName  )  ; 
if  (  image  !=  null  &&  !  image  .  isClose  (  )  )  { 
return   image  ; 
}  else  { 
InputStream   in  =  null  ; 
try  { 
in  =  Resources  .  openResource  (  resName  )  ; 
image  =  loadImage  (  in  ,  transparency  )  ; 
lazyImages  .  put  (  keyName  ,  image  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
}  finally  { 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
in  =  null  ; 
} 
}  catch  (  IOException   e  )  { 
LSystem  .  gc  (  )  ; 
} 
} 
} 
if  (  image  ==  null  )  { 
throw   new   RuntimeException  (  (  "File not found. ( "  +  resName  +  " )"  )  .  intern  (  )  )  ; 
} 
return   image  ; 
} 

public   static   final   LImage   loadImage  (  final   String   resName  )  { 
return   GraphicsUtils  .  loadImage  (  resName  ,  false  )  ; 
} 

public   static   final   LImage   loadNotCacheImage  (  final   String   resName  ,  boolean   transparency  )  { 
if  (  resName  ==  null  )  { 
return   null  ; 
} 
InputStream   in  =  null  ; 
try  { 
in  =  Resources  .  openResource  (  resName  )  ; 
return   loadImage  (  in  ,  transparency  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  resName  +  " not found!"  )  ; 
}  finally  { 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
in  =  null  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
} 

public   static   final   LImage   loadNotCacheImage  (  final   String   resName  )  { 
return   GraphicsUtils  .  loadNotCacheImage  (  resName  ,  false  )  ; 
} 







public   static   LImage   loadWebImage  (  String   string  ,  boolean   transparency  )  { 
LImage   img  =  null  ; 
try  { 
java  .  net  .  URL   url  =  new   java  .  net  .  URL  (  string  )  ; 
java  .  net  .  HttpURLConnection   http  =  (  java  .  net  .  HttpURLConnection  )  url  .  openConnection  (  )  ; 
http  .  setRequestMethod  (  "GET"  )  ; 
http  .  connect  (  )  ; 
InputStream   is  =  http  .  getInputStream  (  )  ; 
img  =  GraphicsUtils  .  loadImage  (  is  ,  transparency  )  ; 
if  (  img  .  getWidth  (  )  ==  0  ||  img  .  getHeight  (  )  ==  0  )  { 
img  =  null  ; 
} 
is  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  (  "File not found. ( "  +  string  +  " )"  )  .  intern  (  )  )  ; 
} 
return   img  ; 
} 









public   static   LImage  [  ]  loadSequenceImages  (  String   fileName  ,  String   range  ,  boolean   transparency  )  { 
try  { 
int   start_range  =  -  1  ; 
int   end_range  =  -  1  ; 
int   images_count  =  1  ; 
int   minusIndex  =  range  .  indexOf  (  '-'  )  ; 
if  (  (  minusIndex  >  0  )  &&  (  minusIndex  <  (  range  .  length  (  )  -  1  )  )  )  { 
try  { 
start_range  =  Integer  .  parseInt  (  range  .  substring  (  0  ,  minusIndex  )  )  ; 
end_range  =  Integer  .  parseInt  (  range  .  substring  (  minusIndex  +  1  )  )  ; 
if  (  start_range  <  end_range  )  { 
images_count  =  end_range  -  start_range  +  1  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 
LImage  [  ]  images  =  new   LImage  [  images_count  ]  ; 
for  (  int   i  =  0  ;  i  <  images_count  ;  i  ++  )  { 
String   imageName  =  fileName  ; 
if  (  images_count  >  1  )  { 
int   dotIndex  =  fileName  .  lastIndexOf  (  '.'  )  ; 
if  (  dotIndex  >=  0  )  { 
imageName  =  fileName  .  substring  (  0  ,  dotIndex  )  +  (  start_range  +  i  )  +  fileName  .  substring  (  dotIndex  )  ; 
} 
} 
images  [  i  ]  =  GraphicsUtils  .  loadImage  (  imageName  ,  transparency  )  ; 
} 
return   images  ; 
}  catch  (  Exception   ex  )  { 
} 
return   null  ; 
} 







public   static   LImage  [  ]  [  ]  getFlipHorizintalImage2D  (  LImage  [  ]  [  ]  pixels  )  { 
int   w  =  pixels  .  length  ; 
int   h  =  pixels  [  0  ]  .  length  ; 
LImage   pixel  [  ]  [  ]  =  new   LImage  [  h  ]  [  w  ]  ; 
for  (  int   i  =  0  ;  i  <  h  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  w  ;  j  ++  )  { 
pixel  [  i  ]  [  j  ]  =  pixels  [  j  ]  [  i  ]  ; 
} 
} 
return   pixel  ; 
} 






public   static   LImage   rotateImage  (  final   LImage   image  )  { 
return   rotate  (  image  ,  180  )  ; 
} 







public   static   LImage   rotateImage  (  final   LImage   image  ,  final   int   angdeg  ,  final   boolean   d  )  { 
int   w  =  image  .  getWidth  (  )  ; 
int   h  =  image  .  getHeight  (  )  ; 
LImage   img  =  LImage  .  createImage  (  w  ,  h  ,  image  .  getConfig  (  )  )  ; 
LGraphics   g  =  img  .  getLGraphics  (  )  ; 
g  .  setAntiAlias  (  true  )  ; 
g  .  rotate  (  d  ?  (  float  )  -  Math  .  toRadians  (  angdeg  )  :  (  float  )  Math  .  toRadians  (  angdeg  )  ,  w  /  2  ,  h  /  2  )  ; 
g  .  drawImage  (  image  ,  0  ,  0  )  ; 
g  .  setAntiAlias  (  false  )  ; 
g  .  dispose  (  )  ; 
return   img  ; 
} 









public   static   LImage   createShapeImage  (  Shape   shape  ,  LColor   c1  ,  LColor   c2  )  { 
Rectangle   rect  =  shape  .  getBounds  (  )  ; 
LImage   image  =  LImage  .  createImage  (  rect  .  width  ,  rect  .  height  ,  false  )  ; 
LGraphics   g  =  image  .  getLGraphics  (  )  ; 
g  .  setColor  (  c1  )  ; 
g  .  fill  (  shape  )  ; 
g  .  setColor  (  c2  )  ; 
g  .  draw  (  shape  )  ; 
g  .  dispose  (  )  ; 
return   image  ; 
} 













public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x1  ,  int   y1  ,  int   x2  ,  int   y2  ,  Config   config  )  { 
if  (  image  ==  null  )  { 
return   null  ; 
} 
if  (  objectWidth  >  image  .  getWidth  (  )  )  { 
objectWidth  =  image  .  getWidth  (  )  ; 
} 
if  (  objectHeight  >  image  .  getHeight  (  )  )  { 
objectHeight  =  image  .  getHeight  (  )  ; 
} 
Bitmap   bitmap  =  Bitmap  .  createBitmap  (  objectWidth  ,  objectHeight  ,  config  )  ; 
canvas  .  setBitmap  (  bitmap  )  ; 
canvas  .  drawBitmap  (  image  .  getBitmap  (  )  ,  new   Rect  (  x1  ,  y1  ,  x2  ,  y2  )  ,  new   Rect  (  0  ,  0  ,  objectWidth  ,  objectHeight  )  ,  null  )  ; 
if  (  objectWidth  ==  objectHeight  &&  objectWidth  <=  48  &&  objectHeight  <=  48  )  { 
return   filterBitmapTo565  (  bitmap  ,  objectWidth  ,  objectHeight  )  ; 
} 
return   new   LImage  (  bitmap  )  ; 
} 













public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x1  ,  int   y1  ,  int   x2  ,  int   y2  )  { 
return   drawClipImage  (  image  ,  objectWidth  ,  objectHeight  ,  x1  ,  y1  ,  x2  ,  y2  ,  image  .  getConfig  (  )  )  ; 
} 














public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x1  ,  int   y1  ,  int   x2  ,  int   y2  ,  boolean   flag  )  { 
return   drawClipImage  (  image  ,  objectWidth  ,  objectHeight  ,  x1  ,  y1  ,  x2  ,  y2  ,  flag  ?  image  .  getConfig  (  )  :  Config  .  RGB_565  )  ; 
} 












public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x  ,  int   y  ,  boolean   flag  )  { 
return   drawClipImage  (  image  ,  objectWidth  ,  objectHeight  ,  x  ,  y  ,  flag  ?  image  .  getConfig  (  )  :  Config  .  RGB_565  )  ; 
} 












public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x  ,  int   y  ,  Config   config  )  { 
if  (  image  .  getWidth  (  )  ==  objectWidth  &&  image  .  getHeight  (  )  ==  objectHeight  )  { 
return   image  ; 
} 
Bitmap   bitmap  =  Bitmap  .  createBitmap  (  objectWidth  ,  objectHeight  ,  config  )  ; 
canvas  .  setBitmap  (  bitmap  )  ; 
canvas  .  drawBitmap  (  image  .  getBitmap  (  )  ,  new   Rect  (  x  ,  y  ,  x  +  objectWidth  ,  objectHeight  +  y  )  ,  new   Rect  (  0  ,  0  ,  objectWidth  ,  objectHeight  )  ,  null  )  ; 
if  (  objectWidth  ==  objectHeight  &&  objectWidth  <=  48  &&  objectHeight  <=  48  )  { 
return   filterBitmapTo565  (  bitmap  ,  objectWidth  ,  objectHeight  )  ; 
} 
return   new   LImage  (  bitmap  )  ; 
} 











public   static   LImage   drawCropImage  (  final   LImage   image  ,  int   x  ,  int   y  ,  int   objectWidth  ,  int   objectHeight  )  { 
return   GraphicsUtils  .  drawClipImage  (  image  ,  objectWidth  ,  objectHeight  ,  x  ,  y  ,  image  .  getConfig  (  )  )  ; 
} 











public   static   LImage   drawClipImage  (  final   LImage   image  ,  int   objectWidth  ,  int   objectHeight  ,  int   x  ,  int   y  )  { 
return   GraphicsUtils  .  drawClipImage  (  image  ,  objectWidth  ,  objectHeight  ,  x  ,  y  ,  image  .  getConfig  (  )  )  ; 
} 









public   static   LImage  [  ]  [  ]  getSplit2Images  (  String   fileName  ,  int   row  ,  int   col  ,  boolean   isFiltrate  ,  boolean   transparency  )  { 
String   keyName  =  (  fileName  +  row  +  col  +  isFiltrate  )  .  intern  (  )  .  toLowerCase  (  )  .  trim  (  )  ; 
if  (  lazySplitMap  .  size  (  )  >  LSystem  .  DEFAULT_MAX_CACHE_SIZE  /  3  )  { 
lazySplitMap  .  clear  (  )  ; 
System  .  gc  (  )  ; 
} 
LImage  [  ]  [  ]  objs  =  lazySplitMap  .  get  (  keyName  )  ; 
if  (  objs  ==  null  )  { 
LImage   image  =  GraphicsUtils  .  loadNotCacheImage  (  fileName  ,  transparency  )  ; 
objs  =  getSplit2Images  (  image  ,  row  ,  col  ,  isFiltrate  )  ; 
lazySplitMap  .  put  (  keyName  ,  objs  )  ; 
} 
return  (  LImage  [  ]  [  ]  )  objs  ; 
} 









public   static   LImage  [  ]  [  ]  getSplit2Images  (  String   fileName  ,  int   row  ,  int   col  ,  boolean   transparency  )  { 
return   getSplit2Images  (  fileName  ,  row  ,  col  ,  false  ,  transparency  )  ; 
} 









public   static   LImage  [  ]  [  ]  getSplit2Images  (  LImage   image  ,  int   row  ,  int   col  ,  boolean   isFiltrate  )  { 
int   wlength  =  image  .  getWidth  (  )  /  row  ; 
int   hlength  =  image  .  getHeight  (  )  /  col  ; 
LImage  [  ]  [  ]  abufferedimage  =  new   LImage  [  wlength  ]  [  hlength  ]  ; 
Rect   srcR  =  new   Rect  (  )  ; 
Rect   dstR  =  new   Rect  (  )  ; 
for  (  int   y  =  0  ;  y  <  hlength  ;  y  ++  )  { 
for  (  int   x  =  0  ;  x  <  wlength  ;  x  ++  )  { 
Bitmap   bitmap  =  Bitmap  .  createBitmap  (  row  ,  col  ,  image  .  getConfig  (  )  )  ; 
srcR  .  set  (  (  x  *  row  )  ,  (  y  *  col  )  ,  row  +  (  x  *  row  )  ,  col  +  (  y  *  col  )  )  ; 
dstR  .  set  (  0  ,  0  ,  row  ,  col  )  ; 
canvas  .  setBitmap  (  bitmap  )  ; 
canvas  .  drawBitmap  (  image  .  getBitmap  (  )  ,  srcR  ,  dstR  ,  null  )  ; 
if  (  row  ==  col  &&  row  <=  48  &&  col  <=  48  )  { 
abufferedimage  [  x  ]  [  y  ]  =  filterBitmapTo565  (  bitmap  ,  row  ,  col  )  ; 
}  else  { 
abufferedimage  [  x  ]  [  y  ]  =  new   LImage  (  bitmap  )  ; 
} 
if  (  isFiltrate  )  { 
LImage   tmp  =  abufferedimage  [  x  ]  [  y  ]  ; 
int   pixels  [  ]  =  tmp  .  getPixels  (  )  ; 
for  (  int   i  =  0  ;  i  <  pixels  .  length  ;  i  ++  )  { 
LColor   c  =  new   LColor  (  pixels  [  i  ]  )  ; 
if  (  (  c  .  getBlue  (  )  ==  247  &&  c  .  getGreen  (  )  ==  0  &&  c  .  getBlue  (  )  ==  255  )  ||  (  c  .  getBlue  (  )  ==  255  &&  c  .  getGreen  (  )  ==  0  &&  c  .  getBlue  (  )  ==  255  )  ||  (  c  .  getBlue  (  )  ==  0  &&  c  .  getGreen  (  )  ==  0  &&  c  .  getBlue  (  )  ==  0  )  )  { 
pixels  [  i  ]  =  0xffffff  ; 
} 
} 
tmp  .  setPixels  (  pixels  ,  tmp  .  getWidth  (  )  ,  tmp  .  getHeight  (  )  )  ; 
} 
} 
} 
return   abufferedimage  ; 
} 









public   static   LImage   getResize  (  LImage   image  ,  int   w  ,  int   h  )  { 
return   new   LImage  (  GraphicsUtils  .  getResize  (  image  .  getBitmap  (  )  ,  w  ,  h  )  )  ; 
} 









public   static   Bitmap   getResize  (  Bitmap   image  ,  int   w  ,  int   h  ,  boolean   flag  )  { 
int   width  =  image  .  getWidth  (  )  ; 
int   height  =  image  .  getHeight  (  )  ; 
if  (  width  ==  w  &&  height  ==  h  )  { 
return   image  ; 
} 
int   newWidth  =  w  ; 
int   newHeight  =  h  ; 
float   scaleWidth  =  (  (  float  )  newWidth  )  /  width  ; 
float   scaleHeight  =  (  (  float  )  newHeight  )  /  height  ; 
matrix  .  reset  (  )  ; 
matrix  .  postScale  (  scaleWidth  ,  scaleHeight  )  ; 
Bitmap   resizedBitmap  =  Bitmap  .  createBitmap  (  image  ,  0  ,  0  ,  width  ,  height  ,  matrix  ,  flag  )  ; 
return   resizedBitmap  ; 
} 









public   static   Bitmap   getResize  (  Bitmap   image  ,  int   w  ,  int   h  )  { 
return   getResize  (  image  ,  w  ,  h  ,  true  )  ; 
} 






public   static   Matrix   getMatrix  (  )  { 
matrix  .  reset  (  )  ; 
return   matrix  ; 
} 









public   static   LImage  [  ]  getSplitImages  (  String   fileName  ,  int   row  ,  int   col  ,  boolean   transparency  )  { 
return   getSplitImages  (  GraphicsUtils  .  loadImage  (  fileName  ,  transparency  )  ,  row  ,  col  )  ; 
} 









public   static   LImage  [  ]  getSplitImages  (  LImage   image  ,  int   row  ,  int   col  )  { 
int   frame  =  0  ; 
int   wlength  =  image  .  getWidth  (  )  /  row  ; 
int   hlength  =  image  .  getHeight  (  )  /  col  ; 
int   total  =  wlength  *  hlength  ; 
Rect   srcR  =  new   Rect  (  )  ; 
Rect   dstR  =  new   Rect  (  )  ; 
LImage  [  ]  images  =  new   LImage  [  total  ]  ; 
for  (  int   y  =  0  ;  y  <  hlength  ;  y  ++  )  { 
for  (  int   x  =  0  ;  x  <  wlength  ;  x  ++  )  { 
Bitmap   bitmap  =  Bitmap  .  createBitmap  (  row  ,  col  ,  image  .  getConfig  (  )  )  ; 
srcR  .  set  (  (  x  *  row  )  ,  (  y  *  col  )  ,  row  +  (  x  *  row  )  ,  col  +  (  y  *  col  )  )  ; 
dstR  .  set  (  0  ,  0  ,  row  ,  col  )  ; 
canvas  .  setBitmap  (  bitmap  )  ; 
canvas  .  drawBitmap  (  image  .  getBitmap  (  )  ,  srcR  ,  dstR  ,  null  )  ; 
if  (  row  ==  col  &&  row  <=  48  &&  col  <=  48  )  { 
images  [  frame  ]  =  filterBitmapTo565  (  bitmap  ,  row  ,  col  )  ; 
}  else  { 
images  [  frame  ]  =  new   LImage  (  bitmap  )  ; 
} 
frame  ++  ; 
} 
} 
return   images  ; 
} 








public   static   LImage   copy  (  LImage   target  ,  LImage   source  )  { 
LGraphics   g  =  target  .  getLGraphics  (  )  ; 
g  .  drawImage  (  source  ,  0  ,  0  )  ; 
g  .  dispose  (  )  ; 
return   target  ; 
} 








public   static   Bitmap   rotate  (  Bitmap   bit  ,  float   degrees  )  { 
if  (  bit  ==  null  )  { 
return   bit  ; 
} 
if  (  degrees  %  360  !=  0  )  { 
int   width  =  bit  .  getWidth  (  )  ; 
int   height  =  bit  .  getHeight  (  )  ; 
int   nx  =  width  /  2  ; 
int   ny  =  height  /  2  ; 
matrix  .  reset  (  )  ; 
matrix  .  preTranslate  (  -  nx  ,  -  ny  )  ; 
matrix  .  postRotate  (  degrees  )  ; 
matrix  .  postTranslate  (  nx  ,  ny  )  ; 
Bitmap   dst  =  Bitmap  .  createBitmap  (  bit  ,  0  ,  0  ,  width  ,  height  ,  matrix  ,  false  )  ; 
return   dst  ; 
}  else  { 
return   bit  ; 
} 
} 








public   static   LImage   rotate  (  LImage   img  ,  float   degrees  )  { 
return   new   LImage  (  rotate  (  img  .  getBitmap  (  )  ,  degrees  )  )  ; 
} 









public   static   Bitmap   fitBitmap  (  Bitmap   baseImage  ,  int   width  ,  int   height  )  { 
RectBox   rect  =  calculateFitBitmap  (  baseImage  ,  width  ,  height  )  ; 
Bitmap   resizedBitmap  =  Bitmap  .  createScaledBitmap  (  baseImage  ,  rect  .  width  ,  rect  .  height  ,  true  )  ; 
return   resizedBitmap  ; 
} 









public   static   LImage   fitImage  (  LImage   image  ,  int   width  ,  int   height  )  { 
Bitmap   bitmap  =  image  .  getBitmap  (  )  ; 
RectBox   rect  =  calculateFitBitmap  (  bitmap  ,  width  ,  height  )  ; 
Bitmap   resizedBitmap  =  Bitmap  .  createScaledBitmap  (  bitmap  ,  rect  .  width  ,  rect  .  height  ,  true  )  ; 
return   new   LImage  (  resizedBitmap  )  ; 
} 











public   static   final   RectBox   calculateFitBitmap  (  Bitmap   baseImage  ,  int   width  ,  int   height  )  { 
if  (  baseImage  ==  null  )  { 
throw   new   RuntimeException  (  "Image is null"  )  ; 
} 
return   fitLimitSize  (  baseImage  .  getWidth  (  )  ,  baseImage  .  getHeight  (  )  ,  width  ,  height  )  ; 
} 










public   static   final   RectBox   fitLimitSize  (  int   srcWidth  ,  int   srcHeight  ,  int   dstWidth  ,  int   dstHeight  )  { 
int   dw  =  dstWidth  ; 
int   dh  =  dstHeight  ; 
if  (  dw  !=  0  &&  dh  !=  0  )  { 
double   waspect  =  (  double  )  dw  /  srcWidth  ; 
double   haspect  =  (  double  )  dh  /  srcHeight  ; 
if  (  waspect  >  haspect  )  { 
dw  =  (  int  )  (  srcWidth  *  haspect  )  ; 
}  else  { 
dh  =  (  int  )  (  srcHeight  *  waspect  )  ; 
} 
} 
return   new   RectBox  (  0  ,  0  ,  dw  ,  dh  )  ; 
} 








public   static   LImage   loadAsPNG  (  String   recordStore  ,  String   resourceName  )  { 
RecordStore   imagesRS  =  null  ; 
LImage   img  =  null  ; 
try  { 
imagesRS  =  RecordStore  .  openRecordStore  (  recordStore  ,  true  )  ; 
RecordEnumeration   re  =  imagesRS  .  enumerateRecords  (  null  ,  null  ,  true  )  ; 
int   numRecs  =  re  .  numRecords  (  )  ; 
for  (  int   i  =  1  ;  i  <  numRecs  ;  i  ++  )  { 
int   recId  =  re  .  nextRecordId  (  )  ; 
byte  [  ]  rec  =  imagesRS  .  getRecord  (  recId  )  ; 
ByteArrayInputStream   bin  =  new   ByteArrayInputStream  (  rec  )  ; 
DataInputStream   din  =  new   DataInputStream  (  bin  )  ; 
String   name  =  din  .  readUTF  (  )  ; 
if  (  name  .  equals  (  resourceName  )  ==  false  )  { 
continue  ; 
} 
int   width  =  din  .  readInt  (  )  ; 
int   height  =  din  .  readInt  (  )  ; 
din  .  readLong  (  )  ; 
int   length  =  din  .  readInt  (  )  ; 
int  [  ]  rawImg  =  new   int  [  width  *  height  ]  ; 
for  (  i  =  0  ;  i  <  length  ;  i  ++  )  { 
rawImg  [  i  ]  =  din  .  readInt  (  )  ; 
} 
img  =  LImage  .  createRGBImage  (  rawImg  ,  width  ,  height  ,  false  )  ; 
din  .  close  (  )  ; 
bin  .  close  (  )  ; 
} 
}  catch  (  InvalidRecordIDException   ignore  )  { 
}  catch  (  Exception   e  )  { 
}  finally  { 
try  { 
if  (  imagesRS  !=  null  )  imagesRS  .  closeRecordStore  (  )  ; 
}  catch  (  Exception   ignore  )  { 
} 
} 
return   img  ; 
} 









public   static   int   saveAsPNG  (  String   recordStore  ,  String   resourceName  ,  LImage   image  )  { 
RecordStore   imagesRS  =  null  ; 
if  (  resourceName  ==  null  )  { 
return  -  1  ; 
} 
try  { 
int  [  ]  buffer  =  image  .  getPixels  (  )  ; 
imagesRS  =  RecordStore  .  openRecordStore  (  recordStore  ,  true  )  ; 
ByteArrayOutputStream   bout  =  new   ByteArrayOutputStream  (  )  ; 
DataOutputStream   dout  =  new   DataOutputStream  (  bout  )  ; 
dout  .  writeUTF  (  resourceName  )  ; 
dout  .  writeInt  (  image  .  getWidth  (  )  )  ; 
dout  .  writeInt  (  image  .  getHeight  (  )  )  ; 
dout  .  writeLong  (  System  .  currentTimeMillis  (  )  )  ; 
dout  .  writeInt  (  buffer  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  buffer  .  length  ;  i  ++  )  { 
dout  .  writeInt  (  buffer  [  i  ]  )  ; 
} 
dout  .  flush  (  )  ; 
dout  .  close  (  )  ; 
byte  [  ]  data  =  bout  .  toByteArray  (  )  ; 
return   imagesRS  .  addRecord  (  data  ,  0  ,  data  .  length  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Save the image ["  +  resourceName  +  "] to RecordStore ["  +  recordStore  +  "] failed!"  )  ; 
}  finally  { 
try  { 
if  (  imagesRS  !=  null  )  imagesRS  .  closeRecordStore  (  )  ; 
}  catch  (  Exception   ignore  )  { 
} 
} 
} 









public   static   boolean   saveAsPNG  (  Bitmap   bitmap  ,  String   fileName  )  throws   FileNotFoundException  { 
return   bitmap  .  compress  (  Bitmap  .  CompressFormat  .  PNG  ,  1  ,  new   FileOutputStream  (  fileName  )  )  ; 
} 









public   static   boolean   saveAsPNG  (  LImage   image  ,  String   fileName  )  throws   FileNotFoundException  { 
return   image  .  getBitmap  (  )  .  compress  (  Bitmap  .  CompressFormat  .  PNG  ,  1  ,  new   FileOutputStream  (  fileName  )  )  ; 
} 







public   static   int   hashBitmap  (  Bitmap   bitmap  )  { 
int   hash_result  =  0  ; 
int   w  =  bitmap  .  getWidth  (  )  ; 
int   h  =  bitmap  .  getHeight  (  )  ; 
hash_result  =  (  hash_result  <<  7  )  ^  h  ; 
hash_result  =  (  hash_result  <<  7  )  ^  w  ; 
for  (  int   pixel  =  0  ;  pixel  <  20  ;  ++  pixel  )  { 
int   x  =  (  pixel  *  50  )  %  w  ; 
int   y  =  (  pixel  *  100  )  %  h  ; 
hash_result  =  (  hash_result  <<  7  )  ^  bitmap  .  getPixel  (  x  ,  y  )  ; 
} 
return   hash_result  ; 
} 







public   static   int  [  ]  getPixels  (  Bitmap   bit  )  { 
int   w  =  bit  .  getWidth  (  )  ,  h  =  bit  .  getHeight  (  )  ; 
int   pixels  [  ]  =  new   int  [  w  *  h  ]  ; 
bit  .  getPixels  (  pixels  ,  0  ,  w  ,  0  ,  0  ,  w  ,  h  )  ; 
return   pixels  ; 
} 




public   static   void   destroy  (  )  { 
for  (  LImage   img  :  lazyImages  .  values  (  )  )  { 
if  (  img  !=  null  )  { 
img  .  dispose  (  )  ; 
img  =  null  ; 
} 
} 
lazyImages  .  clear  (  )  ; 
for  (  LImage  [  ]  [  ]  img  :  lazySplitMap  .  values  (  )  )  { 
if  (  img  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  img  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  >  img  [  i  ]  .  length  ;  j  ++  )  { 
if  (  img  [  i  ]  [  j  ]  !=  null  )  { 
img  [  i  ]  [  j  ]  .  dispose  (  )  ; 
img  [  i  ]  [  j  ]  =  null  ; 
} 
} 
} 
} 
} 
lazySplitMap  .  clear  (  )  ; 
} 
} 

