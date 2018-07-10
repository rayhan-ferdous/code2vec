package   org  .  jcompany  .  commons  .  io  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  jar  .  JarEntry  ; 
import   java  .  util  .  jar  .  JarOutputStream  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipException  ; 
import   java  .  util  .  zip  .  ZipFile  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   org  .  jcompany  .  commons  .  io  .  filters  .  IFilterUnzip  ; 
import   org  .  jcompany  .  commons  .  io  .  filters  .  IZipFileListerner  ; 








public   class   PlcZip   extends   File  { 

private   static   final   long   serialVersionUID  =  1L  ; 

protected   boolean   concluido  ; 

private   ZipFile   zip  ; 

private   HashMap  <  String  ,  ZipEntry  >  arquivosInternos  =  new   HashMap  <  String  ,  ZipEntry  >  (  )  ; 

private   IFilterUnzip  [  ]  filtros  =  null  ; 

private   IZipFileListerner   listener  ; 

public   PlcZip  (  String   arquivo  ,  boolean   descompactar  )  throws   ZipException  ,  IOException  { 
super  (  arquivo  )  ; 
if  (  descompactar  )  this  .  zip  =  abrirArquivo  (  )  ; 
} 

public   PlcZip  (  String   arquivo  )  throws   ZipException  ,  IOException  { 
super  (  arquivo  )  ; 
this  .  zip  =  abrirArquivo  (  )  ; 
} 

public   PlcZip  (  File   arquivo  )  throws   ZipException  ,  IOException  { 
super  (  arquivo  .  getAbsolutePath  (  )  )  ; 
this  .  zip  =  abrirArquivo  (  )  ; 
} 

public   void   close  (  )  { 
try  { 
zip  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 











public   boolean   descompactar  (  String   destino  )  throws   IOException  { 
Enumeration   entries  ; 
ZipEntry   entry  =  null  ; 
entries  =  zip  .  entries  (  )  ; 
int   total  =  zip  .  size  (  )  ; 
int   numeroEntrie  =  0  ; 
fora  :  while  (  entries  .  hasMoreElements  (  )  )  { 
numeroEntrie  ++  ; 
entry  =  (  ZipEntry  )  entries  .  nextElement  (  )  ; 
if  (  listener  !=  null  )  listener  .  unzipedInternalFile  (  total  ,  numeroEntrie  )  ; 
File   s  =  new   File  (  destino  +  File  .  separator  +  entry  .  getName  (  )  )  ; 
if  (  filtros  !=  null  )  for  (  IFilterUnzip   filtro  :  filtros  )  if  (  !  filtro  .  descompactar  (  entry  ,  s  )  )  continue   fora  ; 
if  (  entry  .  isDirectory  (  )  )  { 
if  (  s  .  isDirectory  (  )  &&  !  s  .  exists  (  )  )  s  .  mkdir  (  )  ; 
continue  ; 
}  else  { 
if  (  !  s  .  exists  (  )  )  { 
s  .  getParentFile  (  )  .  mkdirs  (  )  ; 
s  .  createNewFile  (  )  ; 
} 
} 
descompactarArquivo  (  entry  ,  destino  +  File  .  separator  +  entry  .  getName  (  )  )  ; 
} 
concluido  =  true  ; 
return   true  ; 
} 

public   boolean   compactar  (  String   origem  )  throws   IOException  { 
try  { 
BufferedInputStream   origin  =  null  ; 
FileOutputStream   dest  =  new   FileOutputStream  (  this  )  ; 
ZipOutputStream   out  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  dest  )  )  ; 
writeToZip  (  origem  ,  new   File  (  origem  )  ,  out  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
return   true  ; 
} 

private   void   descompactarArquivo  (  ZipEntry   entry  ,  String   destino  )  throws   FileNotFoundException  ,  IOException  { 
copyInputStream  (  zip  .  getInputStream  (  entry  )  ,  new   BufferedOutputStream  (  new   FileOutputStream  (  destino  )  )  )  ; 
} 











public   void   descompactarArquivoEspecifico  (  String   nome  ,  String   destino  )  throws   FileNotFoundException  ,  IOException  { 
ZipEntry   arquivo  =  (  ZipEntry  )  arquivosInternos  .  get  (  nome  )  ; 
descompactarArquivo  (  arquivo  ,  destino  )  ; 
} 








public   void   setListener  (  IZipFileListerner   listener  )  { 
this  .  listener  =  listener  ; 
} 










private   final   void   copyInputStream  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  >=  0  )  out  .  write  (  buffer  ,  0  ,  len  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 











private   ZipFile   abrirArquivo  (  )  throws   ZipException  ,  IOException  { 
ZipFile   file  =  null  ; 
file  =  new   ZipFile  (  this  )  ; 
Enumeration   entries  =  file  .  entries  (  )  ; 
while  (  entries  .  hasMoreElements  (  )  )  { 
ZipEntry   entry  =  (  ZipEntry  )  entries  .  nextElement  (  )  ; 
arquivosInternos  .  put  (  entry  .  getName  (  )  ,  entry  )  ; 
} 
return   file  ; 
} 

public   void   setFiltro  (  IFilterUnzip  ...  filtros  )  { 
this  .  filtros  =  filtros  ; 
} 








public   boolean   temArquivo  (  String   nome  )  { 
return   arquivosInternos  .  containsKey  (  nome  )  ; 
} 

public   void   fechar  (  )  throws   IOException  { 
zip  .  close  (  )  ; 
} 

public   boolean   isConcluido  (  )  { 
return   concluido  ; 
} 

private   void   writeToZip  (  String   dirIni  ,  File   file  ,  ZipOutputStream   zipOut  )  throws   IOException  ,  FileNotFoundException  { 
if  (  this  .  getName  (  )  .  equals  (  file  .  getName  (  )  )  )  return  ; 
if  (  file  .  isDirectory  (  )  )  { 
for  (  File   f  :  file  .  listFiles  (  )  )  { 
writeToZip  (  dirIni  ,  f  ,  zipOut  )  ; 
} 
return  ; 
} 
byte  [  ]  fileBytes  =  getFileBytes  (  file  )  ; 
dirIni  =  dirIni  .  replace  (  "\\"  ,  "/"  )  ; 
String   fileName  =  file  .  getPath  (  )  ; 
fileName  =  fileName  .  replace  (  dirIni  +  "\\"  ,  ""  )  .  replace  (  '\\'  ,  '/'  )  ; 
fileName  =  fileName  .  replace  (  dirIni  +  "/"  ,  ""  )  ; 
ZipEntry   entry  =  new   ZipEntry  (  fileName  )  ; 
zipOut  .  putNextEntry  (  entry  )  ; 
zipOut  .  write  (  fileBytes  )  ; 
zipOut  .  flush  (  )  ; 
} 

private   static   byte  [  ]  getFileBytes  (  File   file  )  throws   IOException  ,  FileNotFoundException  { 
long   fileSize  =  file  .  length  (  )  ; 
byte  [  ]  arr  =  new   byte  [  (  int  )  fileSize  ]  ; 
FileInputStream   fileIn  =  new   FileInputStream  (  file  )  ; 
fileIn  .  read  (  arr  )  ; 
fileIn  .  close  (  )  ; 
return   arr  ; 
} 
} 

