package   com  .  mindtree  .  techworks  .  insight  .  releng  .  mvn  .  nsis  .  actions  .  resolver  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  codehaus  .  plexus  .  util  .  FileUtils  ; 
import   com  .  mindtree  .  techworks  .  insight  .  releng  .  mvn  .  nsis  .  actions  .  MojoInfo  ; 
import   com  .  mindtree  .  techworks  .  insight  .  releng  .  mvn  .  nsis  .  actions  .  NsisActionExecutionException  ; 
import   com  .  mindtree  .  techworks  .  insight  .  releng  .  mvn  .  nsis  .  model  .  FileItem  ; 
import   com  .  mindtree  .  techworks  .  insight  .  releng  .  mvn  .  nsis  .  model  .  SetBase  ; 









public   class   FileItemResolver   implements   Resolver  { 

public   void   copyFiles  (  SetBase   setBase  ,  MojoInfo   mojoInfo  ,  File   archiveTempDir  )  throws   NsisActionExecutionException  { 
FileItem   fileItem  =  (  FileItem  )  setBase  ; 
File   sourceFile  =  getFile  (  fileItem  ,  mojoInfo  )  ; 
File   destinationDir  =  new   File  (  archiveTempDir  ,  (  (  null  ==  fileItem  .  getOutputDirectory  (  )  )  ?  ""  :  fileItem  .  getOutputDirectory  (  )  )  )  ; 
if  (  !  destinationDir  .  exists  (  )  )  { 
if  (  !  destinationDir  .  mkdirs  (  )  )  { 
throw   new   NsisActionExecutionException  (  "Could not create "  +  "destination directory: "  +  destinationDir  .  getAbsolutePath  (  )  )  ; 
} 
} 
File   destinationFile  =  new   File  (  destinationDir  ,  sourceFile  .  getName  (  )  )  ; 
try  { 
FileUtils  .  copyFile  (  sourceFile  ,  destinationFile  )  ; 
}  catch  (  IOException   e  )  { 
mojoInfo  .  getLog  (  )  .  error  (  "Error copying "  +  sourceFile  .  getAbsolutePath  (  )  ,  e  )  ; 
throw   new   NsisActionExecutionException  (  "Error copying "  +  sourceFile  .  getAbsolutePath  (  )  ,  e  )  ; 
} 
} 

public   List   getRelativeFilePath  (  SetBase   setBase  ,  MojoInfo   mojoInfo  )  throws   NsisActionExecutionException  { 
File   file  =  getFile  (  (  FileItem  )  setBase  ,  mojoInfo  )  ; 
List   relativePaths  =  new   ArrayList  (  1  )  ; 
String   relativeBase  =  (  null  ==  setBase  .  getOutputDirectory  (  )  )  ?  ""  :  setBase  .  getOutputDirectory  (  )  +  File  .  separator  ; 
mojoInfo  .  getLog  (  )  .  debug  (  "Adding: "  +  file  .  getAbsolutePath  (  )  )  ; 
relativePaths  .  add  (  relativeBase  +  file  .  getName  (  )  )  ; 
return   relativePaths  ; 
} 




protected   File   getFile  (  FileItem   fileItem  ,  MojoInfo   mojoInfo  )  { 
File   projectDir  =  mojoInfo  .  getProject  (  )  .  getBasedir  (  )  ; 
String   sourceName  =  fileItem  .  getSource  (  )  ; 
File   file  =  new   File  (  sourceName  )  ; 
if  (  !  file  .  exists  (  )  )  { 
file  =  new   File  (  projectDir  ,  sourceName  )  ; 
} 
return   file  ; 
} 
} 

