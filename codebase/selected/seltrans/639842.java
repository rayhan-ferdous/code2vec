package   api  .  utils  ; 

import   com  .  sun  .  image  .  codec  .  jpeg  .  ImageFormatException  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  JPEGCodec  ; 
import   com  .  sun  .  image  .  codec  .  jpeg  .  JPEGImageEncoder  ; 
import   java  .  awt  .  Graphics  ; 
import   java  .  awt  .  Graphics2D  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   javax  .  imageio  .  ImageReadParam  ; 
import   javax  .  imageio  .  ImageReader  ; 
import   javax  .  imageio  .  stream  .  ImageInputStream  ; 
import   javax  .  swing  .  ImageIcon  ; 







public   class   ImageBytes  { 







public   static   byte  [  ]  ImageToByte  (  File   filePath  )  throws   FileNotFoundException  { 
FileInputStream   fis  =  new   FileInputStream  (  filePath  )  ; 
ByteArrayOutputStream   bos  =  new   ByteArrayOutputStream  (  )  ; 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
try  { 
for  (  int   readNum  ;  (  readNum  =  fis  .  read  (  buf  )  )  !=  -  1  ;  )  { 
bos  .  write  (  buf  ,  0  ,  readNum  )  ; 
} 
}  catch  (  IOException   ex  )  { 
} 
byte  [  ]  bytes  =  bos  .  toByteArray  (  )  ; 
return   bytes  ; 
} 







public   static   void   byteToImage  (  byte  [  ]  bytes  ,  File   imageFile  )  throws   IOException  { 
ByteArrayInputStream   bis  =  new   ByteArrayInputStream  (  bytes  )  ; 
Iterator  <  ?  >  readers  =  ImageIO  .  getImageReadersByFormatName  (  "jpeg"  )  ; 
ImageReader   reader  =  (  ImageReader  )  readers  .  next  (  )  ; 
Object   source  =  bis  ; 
ImageInputStream   iis  =  ImageIO  .  createImageInputStream  (  source  )  ; 
reader  .  setInput  (  iis  ,  true  )  ; 
ImageReadParam   param  =  reader  .  getDefaultReadParam  (  )  ; 
Image   image  =  reader  .  read  (  0  ,  param  )  ; 
BufferedImage   bufferedImage  =  new   BufferedImage  (  image  .  getWidth  (  null  )  ,  image  .  getHeight  (  null  )  ,  BufferedImage  .  TYPE_INT_RGB  )  ; 
Graphics2D   g2  =  bufferedImage  .  createGraphics  (  )  ; 
g2  .  drawImage  (  image  ,  null  ,  null  )  ; 
ImageIO  .  write  (  bufferedImage  ,  "jpeg"  ,  imageFile  )  ; 
} 








public   static   byte  [  ]  bufferedImageToByteArray  (  BufferedImage   img  )  throws   ImageFormatException  ,  IOException  { 
ByteArrayOutputStream   os  =  new   ByteArrayOutputStream  (  )  ; 
JPEGImageEncoder   encoder  =  JPEGCodec  .  createJPEGEncoder  (  os  )  ; 
encoder  .  encode  (  img  )  ; 
return   os  .  toByteArray  (  )  ; 
} 






public   static   void   createFileFromBytes  (  byte  [  ]  bA  ,  File   f  )  { 
FileOutputStream   fos  ; 
try  { 
fos  =  new   FileOutputStream  (  f  )  ; 
fos  .  write  (  bA  )  ; 
fos  .  close  (  )  ; 
}  catch  (  FileNotFoundException   ex  )  { 
Logger  .  getLogger  (  ImageBytes  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  ImageBytes  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 







public   static   byte  [  ]  getBytesFromFile  (  File   file  )  throws   IOException  { 
InputStream   is  =  new   FileInputStream  (  file  )  ; 
long   length  =  file  .  length  (  )  ; 
if  (  length  >  Integer  .  MAX_VALUE  )  { 
} 
byte  [  ]  bytes  =  new   byte  [  (  int  )  length  ]  ; 
int   offset  =  0  ; 
int   numRead  =  0  ; 
while  (  offset  <  bytes  .  length  &&  (  numRead  =  is  .  read  (  bytes  ,  offset  ,  bytes  .  length  -  offset  )  )  >=  0  )  { 
offset  +=  numRead  ; 
} 
if  (  offset  <  bytes  .  length  )  { 
throw   new   IOException  (  "Could not completely read file "  +  file  .  getName  (  )  )  ; 
} 
is  .  close  (  )  ; 
return   bytes  ; 
} 






public   static   BufferedImage   FileToBufferedImage  (  String   file  )  { 
ImageIcon   icon  =  new   ImageIcon  (  file  )  ; 
Image   imageG  =  icon  .  getImage  (  )  ; 
BufferedImage   bimg  =  new   BufferedImage  (  imageG  .  getWidth  (  null  )  ,  imageG  .  getHeight  (  null  )  ,  BufferedImage  .  TYPE_INT_ARGB  )  ; 
Graphics   g  =  bimg  .  getGraphics  (  )  ; 
g  .  drawImage  (  imageG  ,  0  ,  0  ,  null  )  ; 
g  .  dispose  (  )  ; 
return   bimg  ; 
} 
} 

