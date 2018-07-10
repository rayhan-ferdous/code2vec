package   javax  .  imageio  ; 

import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  awt  .  image  .  RenderedImage  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Iterator  ; 
import   javax  .  imageio  .  spi  .  IIORegistry  ; 
import   javax  .  imageio  .  spi  .  ImageInputStreamSpi  ; 
import   javax  .  imageio  .  spi  .  ImageOutputStreamSpi  ; 
import   javax  .  imageio  .  spi  .  ImageReaderSpi  ; 
import   javax  .  imageio  .  spi  .  ImageTranscoderSpi  ; 
import   javax  .  imageio  .  spi  .  ImageWriterSpi  ; 
import   javax  .  imageio  .  spi  .  ServiceRegistry  ; 
import   javax  .  imageio  .  stream  .  ImageInputStream  ; 
import   javax  .  imageio  .  stream  .  ImageOutputStream  ; 
import   javax  .  imageio  .  stream  .  MemoryCacheImageInputStream  ; 
import   javax  .  imageio  .  stream  .  MemoryCacheImageOutputStream  ; 





public   final   class   ImageIO  { 




private   ImageIO  (  )  { 
} 

private   static   final   class   ReaderFormatFilter   implements   ServiceRegistry  .  Filter  { 

private   String   formatName  ; 

public   ReaderFormatFilter  (  String   formatName  )  { 
this  .  formatName  =  formatName  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageReaderSpi  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  provider  ; 
String  [  ]  formatNames  =  spi  .  getFormatNames  (  )  ; 
for  (  int   i  =  formatNames  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  formatName  .  equals  (  formatNames  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   ReaderMIMETypeFilter   implements   ServiceRegistry  .  Filter  { 

private   String   MIMEType  ; 

public   ReaderMIMETypeFilter  (  String   MIMEType  )  { 
this  .  MIMEType  =  MIMEType  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageReaderSpi  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  provider  ; 
String  [  ]  mimetypes  =  spi  .  getMIMETypes  (  )  ; 
for  (  int   i  =  mimetypes  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  MIMEType  .  equals  (  mimetypes  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   ReaderObjectFilter   implements   ServiceRegistry  .  Filter  { 

private   Object   object  ; 

public   ReaderObjectFilter  (  Object   object  )  { 
this  .  object  =  object  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageReaderSpi  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  provider  ; 
try  { 
if  (  spi  .  canDecodeInput  (  object  )  )  return   true  ; 
}  catch  (  IOException   e  )  { 
} 
} 
return   false  ; 
} 
} 

private   static   final   class   ReaderSuffixFilter   implements   ServiceRegistry  .  Filter  { 

private   String   fileSuffix  ; 

public   ReaderSuffixFilter  (  String   fileSuffix  )  { 
this  .  fileSuffix  =  fileSuffix  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageReaderSpi  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  provider  ; 
String  [  ]  suffixes  =  spi  .  getFileSuffixes  (  )  ; 
for  (  int   i  =  suffixes  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  fileSuffix  .  equals  (  suffixes  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   WriterFormatFilter   implements   ServiceRegistry  .  Filter  { 

private   String   formatName  ; 

public   WriterFormatFilter  (  String   formatName  )  { 
this  .  formatName  =  formatName  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageWriterSpi  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  provider  ; 
String  [  ]  formatNames  =  spi  .  getFormatNames  (  )  ; 
for  (  int   i  =  formatNames  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  formatName  .  equals  (  formatNames  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   WriterMIMETypeFilter   implements   ServiceRegistry  .  Filter  { 

private   String   MIMEType  ; 

public   WriterMIMETypeFilter  (  String   MIMEType  )  { 
this  .  MIMEType  =  MIMEType  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageWriterSpi  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  provider  ; 
String  [  ]  mimetypes  =  spi  .  getMIMETypes  (  )  ; 
for  (  int   i  =  mimetypes  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  MIMEType  .  equals  (  mimetypes  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   WriterSuffixFilter   implements   ServiceRegistry  .  Filter  { 

private   String   fileSuffix  ; 

public   WriterSuffixFilter  (  String   fileSuffix  )  { 
this  .  fileSuffix  =  fileSuffix  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageWriterSpi  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  provider  ; 
String  [  ]  suffixes  =  spi  .  getFileSuffixes  (  )  ; 
for  (  int   i  =  suffixes  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  fileSuffix  .  equals  (  suffixes  [  i  ]  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   WriterObjectFilter   implements   ServiceRegistry  .  Filter  { 

private   ImageTypeSpecifier   type  ; 

private   String   formatName  ; 

public   WriterObjectFilter  (  ImageTypeSpecifier   type  ,  String   formatName  )  { 
this  .  type  =  type  ; 
this  .  formatName  =  formatName  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageWriterSpi  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  provider  ; 
if  (  spi  .  canEncodeImage  (  type  )  )  { 
String  [  ]  formatNames  =  spi  .  getFormatNames  (  )  ; 
for  (  int   i  =  formatNames  .  length  -  1  ;  i  >=  0  ;  --  i  )  if  (  formatName  .  equals  (  formatNames  [  i  ]  )  )  return   true  ; 
} 
} 
return   false  ; 
} 
} 

private   static   final   class   TranscoderFilter   implements   ServiceRegistry  .  Filter  { 

private   ImageReader   reader  ; 

private   ImageWriter   writer  ; 

public   TranscoderFilter  (  ImageReader   reader  ,  ImageWriter   writer  )  { 
this  .  reader  =  reader  ; 
this  .  writer  =  writer  ; 
} 

public   boolean   filter  (  Object   provider  )  { 
if  (  provider   instanceof   ImageTranscoderSpi  )  { 
ImageTranscoderSpi   spi  =  (  ImageTranscoderSpi  )  provider  ; 
if  (  spi  .  getReaderServiceProviderName  (  )  .  equals  (  reader  .  getOriginatingProvider  (  )  .  getClass  (  )  .  getName  (  )  )  &&  spi  .  getWriterServiceProviderName  (  )  .  equals  (  writer  .  getOriginatingProvider  (  )  .  getClass  (  )  .  getName  (  )  )  )  return   true  ; 
} 
return   false  ; 
} 
} 

private   static   final   class   ImageReaderIterator   implements   Iterator  { 

Iterator   it  ; 

Object   readerExtension  ; 

public   ImageReaderIterator  (  Iterator   it  ,  Object   readerExtension  )  { 
this  .  it  =  it  ; 
this  .  readerExtension  =  readerExtension  ; 
} 

public   boolean   hasNext  (  )  { 
return   it  .  hasNext  (  )  ; 
} 

public   Object   next  (  )  { 
try  { 
return  (  (  ImageReaderSpi  )  it  .  next  (  )  )  .  createReaderInstance  (  readerExtension  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

private   static   final   class   ImageWriterIterator   implements   Iterator  { 

Iterator   it  ; 

Object   writerExtension  ; 

public   ImageWriterIterator  (  Iterator   it  ,  Object   writerExtension  )  { 
this  .  it  =  it  ; 
this  .  writerExtension  =  writerExtension  ; 
} 

public   boolean   hasNext  (  )  { 
return   it  .  hasNext  (  )  ; 
} 

public   Object   next  (  )  { 
try  { 
return  (  (  ImageWriterSpi  )  it  .  next  (  )  )  .  createWriterInstance  (  writerExtension  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 

public   void   remove  (  )  { 
throw   new   UnsupportedOperationException  (  )  ; 
} 
} 

private   static   File   cacheDirectory  ; 

private   static   boolean   useCache  =  true  ; 

private   static   Iterator   getReadersByFilter  (  Class   type  ,  ServiceRegistry  .  Filter   filter  ,  Object   readerExtension  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  type  ,  filter  ,  true  )  ; 
return   new   ImageReaderIterator  (  it  ,  readerExtension  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   Collections  .  EMPTY_SET  .  iterator  (  )  ; 
} 
} 

private   static   Iterator   getWritersByFilter  (  Class   type  ,  ServiceRegistry  .  Filter   filter  ,  Object   writerExtension  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  type  ,  filter  ,  true  )  ; 
return   new   ImageWriterIterator  (  it  ,  writerExtension  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   Collections  .  EMPTY_SET  .  iterator  (  )  ; 
} 
} 






public   static   File   getCacheDirectory  (  )  { 
return   cacheDirectory  ; 
} 











public   static   Iterator   getImageReadersByFormatName  (  String   formatName  )  { 
if  (  formatName  ==  null  )  throw   new   IllegalArgumentException  (  "formatName may not be null"  )  ; 
return   getReadersByFilter  (  ImageReaderSpi  .  class  ,  new   ReaderFormatFilter  (  formatName  )  ,  formatName  )  ; 
} 












public   static   Iterator   getImageReadersByMIMEType  (  String   MIMEType  )  { 
if  (  MIMEType  ==  null  )  throw   new   IllegalArgumentException  (  "MIMEType may not be null"  )  ; 
return   getReadersByFilter  (  ImageReaderSpi  .  class  ,  new   ReaderMIMETypeFilter  (  MIMEType  )  ,  MIMEType  )  ; 
} 











public   static   Iterator   getImageReadersBySuffix  (  String   fileSuffix  )  { 
if  (  fileSuffix  ==  null  )  throw   new   IllegalArgumentException  (  "formatName may not be null"  )  ; 
return   getReadersByFilter  (  ImageReaderSpi  .  class  ,  new   ReaderSuffixFilter  (  fileSuffix  )  ,  fileSuffix  )  ; 
} 











public   static   Iterator   getImageWritersByFormatName  (  String   formatName  )  { 
if  (  formatName  ==  null  )  throw   new   IllegalArgumentException  (  "formatName may not be null"  )  ; 
return   getWritersByFilter  (  ImageWriterSpi  .  class  ,  new   WriterFormatFilter  (  formatName  )  ,  formatName  )  ; 
} 












public   static   Iterator   getImageWritersByMIMEType  (  String   MIMEType  )  { 
if  (  MIMEType  ==  null  )  throw   new   IllegalArgumentException  (  "MIMEType may not be null"  )  ; 
return   getWritersByFilter  (  ImageWriterSpi  .  class  ,  new   WriterMIMETypeFilter  (  MIMEType  )  ,  MIMEType  )  ; 
} 











public   static   Iterator   getImageWritersBySuffix  (  String   fileSuffix  )  { 
if  (  fileSuffix  ==  null  )  throw   new   IllegalArgumentException  (  "fileSuffix may not be null"  )  ; 
return   getWritersByFilter  (  ImageWriterSpi  .  class  ,  new   WriterSuffixFilter  (  fileSuffix  )  ,  fileSuffix  )  ; 
} 







public   static   String  [  ]  getReaderFormatNames  (  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  ImageReaderSpi  .  class  ,  true  )  ; 
ArrayList   result  =  new   ArrayList  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  it  .  next  (  )  ; 
String  [  ]  names  =  spi  .  getFormatNames  (  )  ; 
for  (  int   i  =  names  .  length  -  1  ;  i  >=  0  ;  --  i  )  result  .  add  (  names  [  i  ]  )  ; 
} 
return  (  String  [  ]  )  result  .  toArray  (  new   String  [  result  .  size  (  )  ]  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   new   String  [  0  ]  ; 
} 
} 







public   static   String  [  ]  getReaderMIMETypes  (  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  ImageReaderSpi  .  class  ,  true  )  ; 
ArrayList   result  =  new   ArrayList  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  it  .  next  (  )  ; 
String  [  ]  names  =  spi  .  getMIMETypes  (  )  ; 
for  (  int   i  =  names  .  length  -  1  ;  i  >=  0  ;  --  i  )  result  .  add  (  names  [  i  ]  )  ; 
} 
return  (  String  [  ]  )  result  .  toArray  (  new   String  [  result  .  size  (  )  ]  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   new   String  [  0  ]  ; 
} 
} 

private   static   IIORegistry   getRegistry  (  )  { 
return   IIORegistry  .  getDefaultInstance  (  )  ; 
} 







public   static   boolean   getUseCache  (  )  { 
return   useCache  ; 
} 







public   static   String  [  ]  getWriterFormatNames  (  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  ImageWriterSpi  .  class  ,  true  )  ; 
ArrayList   result  =  new   ArrayList  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  it  .  next  (  )  ; 
String  [  ]  names  =  spi  .  getFormatNames  (  )  ; 
for  (  int   i  =  names  .  length  -  1  ;  i  >=  0  ;  --  i  )  result  .  add  (  names  [  i  ]  )  ; 
} 
return  (  String  [  ]  )  result  .  toArray  (  new   String  [  result  .  size  (  )  ]  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   new   String  [  0  ]  ; 
} 
} 







public   static   String  [  ]  getWriterMIMETypes  (  )  { 
try  { 
Iterator   it  =  getRegistry  (  )  .  getServiceProviders  (  ImageWriterSpi  .  class  ,  true  )  ; 
ArrayList   result  =  new   ArrayList  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  it  .  next  (  )  ; 
String  [  ]  names  =  spi  .  getMIMETypes  (  )  ; 
for  (  int   i  =  names  .  length  -  1  ;  i  >=  0  ;  --  i  )  result  .  add  (  names  [  i  ]  )  ; 
} 
return  (  String  [  ]  )  result  .  toArray  (  new   String  [  result  .  size  (  )  ]  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
return   new   String  [  0  ]  ; 
} 
} 





public   static   void   scanForPlugins  (  )  { 
IIORegistry  .  getDefaultInstance  (  )  .  registerApplicationClasspathSpis  (  )  ; 
} 












public   static   void   setCacheDirectory  (  File   cacheDirectory  )  { 
if  (  cacheDirectory  !=  null  )  { 
if  (  !  cacheDirectory  .  isDirectory  (  )  )  throw   new   IllegalArgumentException  (  "cacheDirectory must be a directory"  )  ; 
cacheDirectory  .  canWrite  (  )  ; 
} 
ImageIO  .  cacheDirectory  =  cacheDirectory  ; 
} 












public   static   void   setUseCache  (  boolean   useCache  )  { 
ImageIO  .  useCache  =  useCache  ; 
} 















public   static   boolean   write  (  RenderedImage   im  ,  String   formatName  ,  File   output  )  throws   IOException  { 
if  (  im  ==  null  ||  formatName  ==  null  ||  output  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   write  (  im  ,  formatName  ,  new   FileOutputStream  (  output  )  )  ; 
} 
















public   static   boolean   write  (  RenderedImage   im  ,  String   formatName  ,  OutputStream   output  )  throws   IOException  { 
if  (  im  ==  null  ||  formatName  ==  null  ||  output  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   write  (  im  ,  formatName  ,  new   MemoryCacheImageOutputStream  (  output  )  )  ; 
} 


















public   static   boolean   write  (  RenderedImage   im  ,  String   formatName  ,  ImageOutputStream   output  )  throws   IOException  { 
if  (  im  ==  null  ||  formatName  ==  null  ||  output  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
Iterator   writers  =  getImageWritersByFormatName  (  formatName  )  ; 
IIOImage   img  =  new   IIOImage  (  im  ,  null  ,  null  )  ; 
while  (  writers  .  hasNext  (  )  )  { 
ImageWriter   w  =  (  ImageWriter  )  writers  .  next  (  )  ; 
try  { 
w  .  setOutput  (  output  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
continue  ; 
} 
w  .  write  (  null  ,  img  ,  null  )  ; 
output  .  close  (  )  ; 
return   true  ; 
} 
return   false  ; 
} 
















public   static   BufferedImage   read  (  ImageInputStream   stream  )  throws   IOException  { 
if  (  stream  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
Iterator   providers  =  getRegistry  (  )  .  getServiceProviders  (  ImageReaderSpi  .  class  ,  true  )  ; 
while  (  providers  .  hasNext  (  )  )  { 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  providers  .  next  (  )  ; 
if  (  spi  .  canDecodeInput  (  stream  )  )  { 
ImageReader   reader  =  spi  .  createReaderInstance  (  )  ; 
reader  .  setInput  (  stream  )  ; 
return   reader  .  read  (  0  ,  null  )  ; 
} 
} 
return   null  ; 
} 






















public   static   BufferedImage   read  (  URL   input  )  throws   IOException  { 
if  (  input  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   read  (  input  .  openStream  (  )  )  ; 
} 






















public   static   BufferedImage   read  (  InputStream   input  )  throws   IOException  { 
if  (  input  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   read  (  new   MemoryCacheImageInputStream  (  input  )  )  ; 
} 






















public   static   BufferedImage   read  (  File   input  )  throws   IOException  { 
if  (  input  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   read  (  new   FileInputStream  (  input  )  )  ; 
} 



















public   static   ImageInputStream   createImageInputStream  (  Object   input  )  throws   IOException  { 
if  (  input  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
Iterator   spis  =  getRegistry  (  )  .  getServiceProviders  (  ImageInputStreamSpi  .  class  ,  true  )  ; 
ImageInputStreamSpi   foundSpi  =  null  ; 
while  (  spis  .  hasNext  (  )  )  { 
ImageInputStreamSpi   spi  =  (  ImageInputStreamSpi  )  spis  .  next  (  )  ; 
if  (  input  .  getClass  (  )  .  equals  (  spi  .  getInputClass  (  )  )  )  { 
foundSpi  =  spi  ; 
break  ; 
} 
} 
return   foundSpi  ==  null  ?  null  :  foundSpi  .  createInputStreamInstance  (  input  ,  getUseCache  (  )  ,  getCacheDirectory  (  )  )  ; 
} 



















public   static   ImageOutputStream   createImageOutputStream  (  Object   output  )  throws   IOException  { 
if  (  output  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
Iterator   spis  =  getRegistry  (  )  .  getServiceProviders  (  ImageOutputStreamSpi  .  class  ,  true  )  ; 
ImageOutputStreamSpi   foundSpi  =  null  ; 
while  (  spis  .  hasNext  (  )  )  { 
ImageOutputStreamSpi   spi  =  (  ImageOutputStreamSpi  )  spis  .  next  (  )  ; 
if  (  output  .  getClass  (  )  .  equals  (  spi  .  getOutputClass  (  )  )  )  { 
foundSpi  =  spi  ; 
break  ; 
} 
} 
return   foundSpi  ==  null  ?  null  :  foundSpi  .  createOutputStreamInstance  (  output  ,  getUseCache  (  )  ,  getCacheDirectory  (  )  )  ; 
} 












public   static   ImageReader   getImageReader  (  ImageWriter   writer  )  { 
if  (  writer  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
ImageWriterSpi   spi  =  (  ImageWriterSpi  )  getRegistry  (  )  .  getServiceProviderByClass  (  writer  .  getClass  (  )  )  ; 
String  [  ]  readerSpiNames  =  spi  .  getImageReaderSpiNames  (  )  ; 
ImageReader   r  =  null  ; 
if  (  readerSpiNames  !=  null  )  { 
try  { 
Class   readerClass  =  Class  .  forName  (  readerSpiNames  [  0  ]  )  ; 
r  =  (  ImageReader  )  readerClass  .  newInstance  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
return   r  ; 
} 









public   static   Iterator   getImageReaders  (  Object   input  )  { 
if  (  input  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   getRegistry  (  )  .  getServiceProviders  (  ImageReaderSpi  .  class  ,  new   ReaderObjectFilter  (  input  )  ,  true  )  ; 
} 











public   static   Iterator   getImageWriters  (  ImageTypeSpecifier   type  ,  String   formatName  )  { 
if  (  type  ==  null  ||  formatName  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   getRegistry  (  )  .  getServiceProviders  (  ImageWriterSpi  .  class  ,  new   WriterObjectFilter  (  type  ,  formatName  )  ,  true  )  ; 
} 















public   static   ImageWriter   getImageWriter  (  ImageReader   reader  )  { 
if  (  reader  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
ImageReaderSpi   spi  =  (  ImageReaderSpi  )  getRegistry  (  )  .  getServiceProviderByClass  (  reader  .  getClass  (  )  )  ; 
String  [  ]  writerSpiNames  =  spi  .  getImageWriterSpiNames  (  )  ; 
ImageWriter   w  =  null  ; 
if  (  writerSpiNames  !=  null  )  { 
try  { 
Class   writerClass  =  Class  .  forName  (  writerSpiNames  [  0  ]  )  ; 
w  =  (  ImageWriter  )  writerClass  .  newInstance  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
return   w  ; 
} 














public   static   Iterator   getImageTranscoders  (  ImageReader   reader  ,  ImageWriter   writer  )  { 
if  (  reader  ==  null  ||  writer  ==  null  )  throw   new   IllegalArgumentException  (  "null argument"  )  ; 
return   getRegistry  (  )  .  getServiceProviders  (  ImageTranscoderSpi  .  class  ,  new   TranscoderFilter  (  reader  ,  writer  )  ,  true  )  ; 
} 
} 

