package   com  .  handy  .  util  ; 

import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Font  ; 
import   java  .  awt  .  Graphics  ; 
import   java  .  awt  .  Graphics2D  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  RenderingHints  ; 
import   java  .  awt  .  geom  .  AffineTransform  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Random  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   com  .  handy  .  util  .  gifencoder  .  AnimatedGifEncoder  ; 
import   com  .  handy  .  util  .  gifencoder  .  GifDecoder  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  ImageFormatException  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  JPEGCodec  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  JPEGEncodeParam  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  JPEGImageEncoder  ; 













public   class   ImageTool  { 









public   static   void   jpgToGif  (  String   pic  [  ]  ,  String   newPic  )  { 
try  { 
AnimatedGifEncoder   e  =  new   AnimatedGifEncoder  (  )  ; 
e  .  setRepeat  (  0  )  ; 
e  .  start  (  newPic  )  ; 
BufferedImage   src  [  ]  =  new   BufferedImage  [  pic  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  src  .  length  ;  i  ++  )  { 
e  .  setDelay  (  200  )  ; 
src  [  i  ]  =  ImageIO  .  read  (  new   File  (  pic  [  i  ]  )  )  ; 
e  .  addFrame  (  src  [  i  ]  )  ; 
} 
e  .  finish  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "jpgToGif Failed:"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 










public   static   String  [  ]  splitGif  (  String   gifName  ,  String   path  )  { 
try  { 
GifDecoder   decoder  =  new   GifDecoder  (  )  ; 
decoder  .  read  (  gifName  )  ; 
int   n  =  decoder  .  getFrameCount  (  )  ; 
String  [  ]  subPic  =  new   String  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
BufferedImage   frame  =  decoder  .  getFrame  (  i  )  ; 
subPic  [  i  ]  =  path  +  String  .  valueOf  (  i  )  +  ".jpg"  ; 
FileOutputStream   out  =  new   FileOutputStream  (  subPic  [  i  ]  )  ; 
ImageIO  .  write  (  frame  ,  "jpeg"  ,  out  )  ; 
JPEGImageEncoder   encoder  =  JPEGCodec  .  createJPEGEncoder  (  out  )  ; 
encoder  .  encode  (  frame  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
} 
return   subPic  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "splitGif Failed!"  )  ; 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 

public   static   void   createJpgByFont  (  String   s  ,  String   jpgname  )  { 
createJpgByFont  (  s  ,  null  ,  null  ,  null  ,  null  ,  jpgname  )  ; 
} 

















public   static   void   createJpgByFont  (  String   s  ,  Integer   fontSize  ,  Color   bgcolor  ,  Color   fontcolor  ,  String   fontPath  ,  String   jpgname  )  { 
if  (  fontSize  ==  null  )  fontSize  =  20  ; 
if  (  bgcolor  ==  null  ||  ""  .  equals  (  bgcolor  )  )  bgcolor  =  Color  .  WHITE  ; 
if  (  fontcolor  ==  null  ||  ""  .  equals  (  fontcolor  )  )  fontcolor  =  Color  .  BLACK  ; 
try  { 
int   bWidth  =  s  .  length  (  )  *  fontSize  +  10  ; 
int   bHeight  =  fontSize  +  5  ; 
BufferedImage   bimage  =  new   BufferedImage  (  bWidth  ,  bHeight  ,  BufferedImage  .  TYPE_INT_RGB  )  ; 
Graphics2D   g  =  bimage  .  createGraphics  (  )  ; 
g  .  setColor  (  bgcolor  )  ; 
g  .  fillRect  (  0  ,  0  ,  bWidth  ,  bHeight  )  ; 
g  .  setRenderingHint  (  RenderingHints  .  KEY_ANTIALIASING  ,  RenderingHints  .  VALUE_ANTIALIAS_ON  )  ; 
Font   font  =  null  ; 
if  (  fontPath  ==  null  ||  ""  .  equals  (  fontPath  )  )  { 
font  =  new   Font  (  "Serif"  ,  Font  .  PLAIN  ,  fontSize  )  ; 
}  else  { 
File   file  =  new   File  (  fontPath  )  ; 
font  =  Font  .  createFont  (  Font  .  TRUETYPE_FONT  ,  file  )  ; 
} 
g  .  setFont  (  font  .  deriveFont  (  (  float  )  fontSize  )  )  ; 
g  .  setColor  (  fontcolor  )  ; 
g  .  drawString  (  s  ,  5  ,  fontSize  )  ; 
AffineTransform   at  =  new   AffineTransform  (  )  ; 
at  .  rotate  (  25  )  ; 
g  .  setTransform  (  at  )  ; 
g  .  dispose  (  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  jpgname  )  ; 
JPEGImageEncoder   encoder  =  JPEGCodec  .  createJPEGEncoder  (  out  )  ; 
JPEGEncodeParam   param  =  encoder  .  getDefaultJPEGEncodeParam  (  bimage  )  ; 
param  .  setQuality  (  50f  ,  true  )  ; 
encoder  .  encode  (  bimage  ,  param  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "createJpgByFont Failed!"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 

public   static   Color   getRandColor  (  int   fc  ,  int   bc  )  { 
Random   random  =  new   Random  (  )  ; 
if  (  fc  >  255  )  fc  =  255  ; 
if  (  bc  >  255  )  bc  =  255  ; 
int   r  =  fc  +  random  .  nextInt  (  bc  -  fc  )  ; 
int   g  =  fc  +  random  .  nextInt  (  bc  -  fc  )  ; 
int   b  =  fc  +  random  .  nextInt  (  bc  -  fc  )  ; 
return   new   Color  (  r  ,  g  ,  b  )  ; 
} 

















public   static   void   createBigJPG  (  ArrayList  <  String  >  smalls  ,  int   smallWidth  ,  int   smallHeight  ,  int   cols  ,  Color   bgColor  ,  String   picName  )  { 
if  (  null  ==  picName  ||  ""  .  equals  (  picName  )  )  { 
log  .  info  (  "合成大图时，大图文件名要指定。"  )  ; 
} 
try  { 
FileOutputStream   out  =  new   FileOutputStream  (  picName  )  ; 
createBigJPG  (  smalls  ,  smallWidth  ,  smallHeight  ,  cols  ,  bgColor  ,  out  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
log  .  error  (  e  .  getMessage  (  )  )  ; 
} 
} 










public   static   void   createBigJPG  (  ArrayList  <  String  >  smalls  ,  int   smallWidth  ,  int   smallHeight  ,  int   cols  ,  Color   bgColor  ,  OutputStream   out  )  { 
if  (  null  ==  smalls  ||  smalls  .  isEmpty  (  )  )  { 
log  .  info  (  "没有小图，放弃合成大图"  )  ; 
return  ; 
} 
int   scount  =  smalls  .  size  (  )  ; 
int   rows  =  0  ; 
if  (  scount  %  cols  ==  0  )  { 
rows  =  smalls  .  size  (  )  /  cols  ; 
}  else  { 
rows  =  smalls  .  size  (  )  /  cols  +  1  ; 
} 
int   bigWidth  =  cols  *  smallWidth  ; 
int   bigHeight  =  rows  *  smallHeight  ; 
BufferedImage   bufImage  =  new   BufferedImage  (  bigWidth  ,  bigHeight  ,  BufferedImage  .  TYPE_INT_RGB  )  ; 
Graphics2D   g  =  bufImage  .  createGraphics  (  )  ; 
g  .  setColor  (  bgColor  )  ; 
g  .  fillRect  (  0  ,  0  ,  smallWidth  ,  smallHeight  )  ; 
int   row  =  0  ; 
int   col  =  0  ; 
int   x  =  0  ; 
int   y  =  0  ; 
for  (  String   small  :  smalls  )  { 
ImageIcon   icon  =  new   ImageIcon  (  small  )  ; 
Image   img  =  icon  .  getImage  (  )  ; 
x  =  col  *  smallWidth  ; 
y  =  row  *  smallHeight  ; 
g  .  drawImage  (  img  ,  x  ,  y  ,  null  )  ; 
col  ++  ; 
if  (  log  .  isDebugEnabled  (  )  )  { 
log  .  debug  (  "x="  +  x  +  ";y="  +  y  )  ; 
log  .  debug  (  col  )  ; 
} 
if  (  col  ==  cols  )  { 
col  =  0  ; 
row  ++  ; 
} 
} 
g  .  dispose  (  )  ; 
try  { 
JPEGImageEncoder   encoder  =  JPEGCodec  .  createJPEGEncoder  (  out  )  ; 
JPEGEncodeParam   param  =  encoder  .  getDefaultJPEGEncodeParam  (  bufImage  )  ; 
param  .  setQuality  (  10f  ,  true  )  ; 
encoder  .  encode  (  bufImage  ,  param  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
log  .  error  (  e  .  getMessage  (  )  )  ; 
}  catch  (  ImageFormatException   e  )  { 
log  .  error  (  e  .  getMessage  (  )  )  ; 
}  catch  (  IOException   e  )  { 
log  .  error  (  e  .  getMessage  (  )  )  ; 
} 
} 









public   void   waterMark  (  String   strOriginalFileName  ,  String   strWaterMarkFileName  )  { 
try  { 
File   fileOriginal  =  new   File  (  strOriginalFileName  )  ; 
Image   imageOriginal  =  ImageIO  .  read  (  fileOriginal  )  ; 
int   widthOriginal  =  imageOriginal  .  getWidth  (  null  )  ; 
int   heightOriginal  =  imageOriginal  .  getHeight  (  null  )  ; 
System  .  out  .  println  (  "widthOriginal:"  +  widthOriginal  +  "theightOriginal:"  +  heightOriginal  )  ; 
BufferedImage   bufImage  =  new   BufferedImage  (  widthOriginal  ,  heightOriginal  ,  BufferedImage  .  TYPE_INT_RGB  )  ; 
Graphics   g  =  bufImage  .  createGraphics  (  )  ; 
g  .  drawImage  (  imageOriginal  ,  0  ,  0  ,  widthOriginal  ,  heightOriginal  ,  null  )  ; 
File   fileWaterMark  =  new   File  (  strWaterMarkFileName  )  ; 
Image   imageWaterMark  =  ImageIO  .  read  (  fileWaterMark  )  ; 
int   widthWaterMark  =  imageWaterMark  .  getWidth  (  null  )  ; 
int   heightWaterMark  =  imageWaterMark  .  getHeight  (  null  )  ; 
System  .  out  .  println  (  "widthWaterMark:"  +  widthWaterMark  +  "theightWaterMark:"  +  heightWaterMark  )  ; 
g  .  drawImage  (  imageWaterMark  ,  widthOriginal  -  widthWaterMark  ,  heightOriginal  -  heightWaterMark  ,  widthWaterMark  ,  heightWaterMark  ,  null  )  ; 
g  .  dispose  (  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  strOriginalFileName  )  ; 
JPEGImageEncoder   encoder  =  JPEGCodec  .  createJPEGEncoder  (  fos  )  ; 
encoder  .  encode  (  bufImage  )  ; 
fos  .  flush  (  )  ; 
fos  .  close  (  )  ; 
fos  =  null  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

private   static   Log   log  =  LogFactory  .  getLog  (  ImageTool  .  class  )  ; 

public   static   void   main  (  String  [  ]  args  )  { 
String   s  =  "aebp"  ; 
String   jpgname  =  "d://test.jpg"  ; 
ImageTool  .  createJpgByFont  (  s  ,  jpgname  )  ; 
} 
} 

