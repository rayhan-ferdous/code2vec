package   org  .  apache  .  axis2  .  maven2  .  mar  ; 

import   org  .  apache  .  maven  .  artifact  .  Artifact  ; 
import   org  .  apache  .  maven  .  artifact  .  resolver  .  filter  .  ScopeArtifactFilter  ; 
import   org  .  apache  .  maven  .  plugin  .  AbstractMojo  ; 
import   org  .  apache  .  maven  .  plugin  .  MojoExecutionException  ; 
import   org  .  apache  .  maven  .  project  .  MavenProject  ; 
import   org  .  codehaus  .  plexus  .  util  .  DirectoryScanner  ; 
import   org  .  codehaus  .  plexus  .  util  .  FileUtils  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 




public   abstract   class   AbstractMarMojo   extends   AbstractMojo  { 








protected   File   baseDir  ; 








protected   MavenProject   project  ; 







private   File   classesDirectory  ; 







protected   File   marDirectory  ; 








private   File   moduleXmlFile  ; 






private   FileSet  [  ]  fileSets  ; 






private   boolean   includeDependencies  ; 





protected   void   buildExplodedMar  (  )  throws   MojoExecutionException  { 
getLog  (  )  .  debug  (  "Exploding mar..."  )  ; 
marDirectory  .  mkdirs  (  )  ; 
getLog  (  )  .  debug  (  "Assembling mar "  +  project  .  getArtifactId  (  )  +  " in "  +  marDirectory  )  ; 
try  { 
final   File   metaInfDir  =  new   File  (  marDirectory  ,  "META-INF"  )  ; 
final   File   libDir  =  new   File  (  marDirectory  ,  "lib"  )  ; 
final   File   moduleFileTarget  =  new   File  (  metaInfDir  ,  "module.xml"  )  ; 
boolean   existsBeforeCopyingClasses  =  moduleFileTarget  .  exists  (  )  ; 
if  (  classesDirectory  .  exists  (  )  &&  (  !  classesDirectory  .  equals  (  marDirectory  )  )  )  { 
FileUtils  .  copyDirectoryStructure  (  classesDirectory  ,  marDirectory  )  ; 
} 
if  (  fileSets  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  fileSets  .  length  ;  i  ++  )  { 
FileSet   fileSet  =  fileSets  [  i  ]  ; 
copyFileSet  (  fileSet  ,  marDirectory  )  ; 
} 
} 
copyMetaInfFile  (  moduleXmlFile  ,  moduleFileTarget  ,  existsBeforeCopyingClasses  ,  "module.xml file"  )  ; 
if  (  includeDependencies  )  { 
Set   artifacts  =  project  .  getArtifacts  (  )  ; 
List   duplicates  =  findDuplicates  (  artifacts  )  ; 
for  (  Iterator   iter  =  artifacts  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Artifact   artifact  =  (  Artifact  )  iter  .  next  (  )  ; 
String   targetFileName  =  getDefaultFinalName  (  artifact  )  ; 
getLog  (  )  .  debug  (  "Processing: "  +  targetFileName  )  ; 
if  (  duplicates  .  contains  (  targetFileName  )  )  { 
getLog  (  )  .  debug  (  "Duplicate found: "  +  targetFileName  )  ; 
targetFileName  =  artifact  .  getGroupId  (  )  +  "-"  +  targetFileName  ; 
getLog  (  )  .  debug  (  "Renamed to: "  +  targetFileName  )  ; 
} 
ScopeArtifactFilter   filter  =  new   ScopeArtifactFilter  (  Artifact  .  SCOPE_RUNTIME  )  ; 
if  (  !  artifact  .  isOptional  (  )  &&  filter  .  include  (  artifact  )  )  { 
String   type  =  artifact  .  getType  (  )  ; 
if  (  "jar"  .  equals  (  type  )  )  { 
copyFileIfModified  (  artifact  .  getFile  (  )  ,  new   File  (  libDir  ,  targetFileName  )  )  ; 
} 
} 
} 
} 
}  catch  (  IOException   e  )  { 
throw   new   MojoExecutionException  (  "Could not explode mar..."  ,  e  )  ; 
} 
} 







private   List   findDuplicates  (  Set   artifacts  )  { 
List   duplicates  =  new   ArrayList  (  )  ; 
List   identifiers  =  new   ArrayList  (  )  ; 
for  (  Iterator   iter  =  artifacts  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Artifact   artifact  =  (  Artifact  )  iter  .  next  (  )  ; 
String   candidate  =  getDefaultFinalName  (  artifact  )  ; 
if  (  identifiers  .  contains  (  candidate  )  )  { 
duplicates  .  add  (  candidate  )  ; 
}  else  { 
identifiers  .  add  (  candidate  )  ; 
} 
} 
return   duplicates  ; 
} 







private   String   getDefaultFinalName  (  Artifact   artifact  )  { 
return   artifact  .  getArtifactId  (  )  +  "-"  +  artifact  .  getVersion  (  )  +  "."  +  artifact  .  getArtifactHandler  (  )  .  getExtension  (  )  ; 
} 















private   void   copyFileIfModified  (  File   source  ,  File   destination  )  throws   IOException  { 
if  (  destination  .  lastModified  (  )  <  source  .  lastModified  (  )  )  { 
FileUtils  .  copyFile  (  source  .  getCanonicalFile  (  )  ,  destination  )  ; 
destination  .  setLastModified  (  source  .  lastModified  (  )  )  ; 
} 
} 

private   void   copyFileSet  (  FileSet   fileSet  ,  File   targetDirectory  )  throws   IOException  { 
File   dir  =  fileSet  .  getDirectory  (  )  ; 
if  (  dir  ==  null  )  { 
dir  =  baseDir  ; 
} 
File   targetDir  =  targetDirectory  ; 
if  (  fileSet  .  getOutputDirectory  (  )  !=  null  )  { 
targetDir  =  new   File  (  targetDir  ,  fileSet  .  getOutputDirectory  (  )  )  ; 
} 
if  (  targetDir  .  equals  (  dir  )  )  { 
return  ; 
} 
DirectoryScanner   ds  =  new   DirectoryScanner  (  )  ; 
ds  .  setBasedir  (  dir  )  ; 
if  (  !  fileSet  .  isSkipDefaultExcludes  (  )  )  { 
ds  .  addDefaultExcludes  (  )  ; 
} 
final   String  [  ]  excludes  =  fileSet  .  getExcludes  (  )  ; 
if  (  excludes  !=  null  )  { 
ds  .  setExcludes  (  excludes  )  ; 
} 
final   String  [  ]  includes  =  fileSet  .  getIncludes  (  )  ; 
if  (  includes  !=  null  )  { 
ds  .  setIncludes  (  includes  )  ; 
} 
ds  .  scan  (  )  ; 
String  [  ]  files  =  ds  .  getIncludedFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   sourceFile  =  new   File  (  dir  ,  files  [  i  ]  )  ; 
File   targetFile  =  new   File  (  targetDir  ,  files  [  i  ]  )  ; 
FileUtils  .  copyFile  (  sourceFile  ,  targetFile  )  ; 
} 
} 

private   void   copyMetaInfFile  (  final   File   pSource  ,  final   File   pTarget  ,  final   boolean   pExistsBeforeCopying  ,  final   String   pDescription  )  throws   MojoExecutionException  ,  IOException  { 
if  (  pSource  !=  null  &&  pTarget  !=  null  )  { 
if  (  !  pSource  .  exists  (  )  )  { 
throw   new   MojoExecutionException  (  "The configured "  +  pDescription  +  " could not be found at "  +  pSource  )  ; 
} 
if  (  !  pExistsBeforeCopying  &&  pTarget  .  exists  (  )  )  { 
getLog  (  )  .  warn  (  "The configured "  +  pDescription  +  " overwrites another file from the classpath."  )  ; 
} 
FileUtils  .  copyFile  (  pSource  ,  pTarget  )  ; 
} 
} 
} 

