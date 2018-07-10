package   com  .  sodad  .  weka  .  experiment  ; 

import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  util  .  zip  .  GZIPOutputStream  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   com  .  sodad  .  weka  .  core  .  RevisionHandler  ; 
import   com  .  sodad  .  weka  .  core  .  RevisionUtils  ; 












public   class   OutputZipper   implements   RevisionHandler  { 

File   m_destination  ; 

DataOutputStream   m_zipOut  =  null  ; 

ZipOutputStream   m_zs  =  null  ; 







public   OutputZipper  (  File   destination  )  throws   Exception  { 
m_destination  =  destination  ; 
if  (  !  m_destination  .  isDirectory  (  )  )  { 
m_zs  =  new   ZipOutputStream  (  new   FileOutputStream  (  m_destination  )  )  ; 
m_zipOut  =  new   DataOutputStream  (  m_zs  )  ; 
} 
} 









public   void   zipit  (  String   outString  ,  String   name  )  throws   Exception  { 
File   saveFile  ; 
ZipEntry   ze  ; 
if  (  m_zipOut  ==  null  )  { 
saveFile  =  new   File  (  m_destination  ,  name  +  ".gz"  )  ; 
DataOutputStream   dout  =  new   DataOutputStream  (  new   GZIPOutputStream  (  new   FileOutputStream  (  saveFile  )  )  )  ; 
dout  .  writeBytes  (  outString  )  ; 
dout  .  close  (  )  ; 
}  else  { 
ze  =  new   ZipEntry  (  name  )  ; 
m_zs  .  putNextEntry  (  ze  )  ; 
m_zipOut  .  writeBytes  (  outString  )  ; 
m_zs  .  closeEntry  (  )  ; 
} 
} 






public   void   finished  (  )  throws   Exception  { 
if  (  m_zipOut  !=  null  )  { 
m_zipOut  .  close  (  )  ; 
} 
} 






public   String   getRevision  (  )  { 
return   RevisionUtils  .  extract  (  "$Revision: 1.8 $"  )  ; 
} 




public   static   void   main  (  String  [  ]  args  )  { 
try  { 
File   testF  =  new   File  (  new   File  (  System  .  getProperty  (  "user.dir"  )  )  ,  "testOut.zip"  )  ; 
OutputZipper   oz  =  new   OutputZipper  (  testF  )  ; 
oz  .  zipit  (  "Here is some test text to be zipped"  ,  "testzip"  )  ; 
oz  .  zipit  (  "Here is a second entry to be zipped"  ,  "testzip2"  )  ; 
oz  .  finished  (  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
System  .  err  .  println  (  ex  .  getMessage  (  )  )  ; 
} 
} 
} 

