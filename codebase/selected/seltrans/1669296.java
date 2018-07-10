package   org  .  ops4j  .  pax  .  construct  .  archetype  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Set  ; 
import   org  .  apache  .  maven  .  archetype  .  Archetype  ; 
import   org  .  apache  .  maven  .  archetype  .  ArchetypeDescriptorException  ; 
import   org  .  apache  .  maven  .  archetype  .  ArchetypeNotFoundException  ; 
import   org  .  apache  .  maven  .  archetype  .  ArchetypeTemplateProcessingException  ; 
import   org  .  apache  .  maven  .  artifact  .  Artifact  ; 
import   org  .  apache  .  maven  .  artifact  .  ArtifactUtils  ; 
import   org  .  apache  .  maven  .  artifact  .  factory  .  ArtifactFactory  ; 
import   org  .  apache  .  maven  .  artifact  .  metadata  .  ArtifactMetadataSource  ; 
import   org  .  apache  .  maven  .  artifact  .  repository  .  ArtifactRepository  ; 
import   org  .  apache  .  maven  .  artifact  .  repository  .  ArtifactRepositoryFactory  ; 
import   org  .  apache  .  maven  .  artifact  .  repository  .  ArtifactRepositoryPolicy  ; 
import   org  .  apache  .  maven  .  artifact  .  repository  .  layout  .  ArtifactRepositoryLayout  ; 
import   org  .  apache  .  maven  .  artifact  .  resolver  .  ArtifactResolver  ; 
import   org  .  apache  .  maven  .  artifact  .  versioning  .  ArtifactVersion  ; 
import   org  .  apache  .  maven  .  artifact  .  versioning  .  DefaultArtifactVersion  ; 
import   org  .  apache  .  maven  .  artifact  .  versioning  .  InvalidVersionSpecificationException  ; 
import   org  .  apache  .  maven  .  artifact  .  versioning  .  VersionRange  ; 
import   org  .  apache  .  maven  .  plugin  .  AbstractMojo  ; 
import   org  .  apache  .  maven  .  plugin  .  MojoExecutionException  ; 
import   org  .  apache  .  maven  .  project  .  MavenProject  ; 
import   org  .  apache  .  maven  .  shared  .  model  .  fileset  .  FileSet  ; 
import   org  .  codehaus  .  plexus  .  util  .  DirectoryScanner  ; 
import   org  .  codehaus  .  plexus  .  util  .  FileUtils  ; 
import   org  .  ops4j  .  pax  .  construct  .  util  .  BndUtils  ; 
import   org  .  ops4j  .  pax  .  construct  .  util  .  BndUtils  .  Bnd  ; 
import   org  .  ops4j  .  pax  .  construct  .  util  .  DirUtils  ; 
import   org  .  ops4j  .  pax  .  construct  .  util  .  PomUtils  ; 
import   org  .  ops4j  .  pax  .  construct  .  util  .  PomUtils  .  Pom  ; 









public   abstract   class   AbstractPaxArchetypeMojo   extends   AbstractMojo  { 




public   static   final   String   PAX_CONSTRUCT_GROUP_ID  =  "org.ops4j.pax.construct"  ; 






private   Archetype   m_archetype  ; 






private   ArtifactFactory   m_factory  ; 






private   ArtifactResolver   m_resolver  ; 






private   ArtifactMetadataSource   m_source  ; 






private   ArtifactRepositoryFactory   m_repoFactory  ; 




private   ArtifactRepositoryLayout   m_defaultLayout  ; 








private   ArtifactRepository   m_localRepo  ; 








private   List   m_remoteRepos  ; 






private   String   remoteRepositories  ; 








private   String   pluginVersion  ; 






private   String   archetypeVersion  ; 






private   File   targetDirectory  ; 







private   String   contents  ; 






private   boolean   compactIds  ; 






private   boolean   attachPom  ; 






private   boolean   overwrite  ; 








private   MavenProject   m_project  ; 




private   File   m_pomFile  ; 




private   FileSet   m_tempFiles  ; 




private   List   m_customArchetypeIds  ; 




private   Pom   m_modulesPom  ; 




private   Pom   m_pom  ; 




private   Bnd   m_bnd  ; 




private   Properties   m_archetypeProperties  ; 




protected   final   ArtifactFactory   getFactory  (  )  { 
return   m_factory  ; 
} 




protected   final   ArtifactResolver   getResolver  (  )  { 
return   m_resolver  ; 
} 




protected   final   ArtifactMetadataSource   getSource  (  )  { 
return   m_source  ; 
} 




protected   final   ArtifactRepository   getLocalRepo  (  )  { 
return   m_localRepo  ; 
} 




protected   final   List   getRemoteRepos  (  )  { 
return   m_remoteRepos  ; 
} 




protected   final   boolean   hasCustomContent  (  )  { 
return   PomUtils  .  isNotEmpty  (  contents  )  ; 
} 





protected   final   String   getInternalGroupId  (  String   bundleGroupId  )  { 
if  (  PomUtils  .  isNotEmpty  (  bundleGroupId  )  )  { 
return   bundleGroupId  ; 
}  else   if  (  null  !=  m_modulesPom  )  { 
return   getCompoundId  (  m_modulesPom  .  getGroupId  (  )  ,  m_modulesPom  .  getArtifactId  (  )  )  ; 
}  else  { 
return  "examples"  ; 
} 
} 




protected   final   String   getPluginVersion  (  )  { 
return   pluginVersion  ; 
} 




protected   final   void   addTempFiles  (  String   pathExpression  )  { 
m_tempFiles  .  addInclude  (  pathExpression  )  ; 
} 




public   final   void   execute  (  )  throws   MojoExecutionException  { 
updateFields  (  )  ; 
createModuleTree  (  )  ; 
do  { 
scheduleCustomArchetypes  (  )  ; 
updateExtensionFields  (  )  ; 
prepareTarget  (  )  ; 
generateArchetype  (  )  ; 
cacheSettings  (  )  ; 
runCustomArchetypes  (  )  ; 
postProcess  (  )  ; 
cleanUp  (  )  ; 
}  while  (  createMoreArtifacts  (  )  )  ; 
} 




private   void   updateFields  (  )  { 
m_archetypeProperties  =  new   Properties  (  )  ; 
targetDirectory  =  DirUtils  .  resolveFile  (  targetDirectory  ,  true  )  ; 
setArchetypeProperty  (  "basedir"  ,  targetDirectory  .  getPath  (  )  )  ; 
if  (  PomUtils  .  isNotEmpty  (  remoteRepositories  )  )  { 
getLog  (  )  .  info  (  "We are using command-line specified remote repositories: "  +  remoteRepositories  )  ; 
m_remoteRepos  =  new   ArrayList  (  )  ; 
String  [  ]  s  =  remoteRepositories  .  split  (  ","  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  ;  i  ++  )  { 
m_remoteRepos  .  add  (  createRemoteRepository  (  "id"  +  i  ,  s  [  i  ]  )  )  ; 
} 
m_remoteRepos  .  add  (  createRemoteRepository  (  "central"  ,  "http://repo1.maven.org/maven2"  )  )  ; 
} 
} 






private   void   createModuleTree  (  )  throws   MojoExecutionException  { 
if  (  attachPom  )  { 
try  { 
m_modulesPom  =  DirUtils  .  createModuleTree  (  m_project  .  getBasedir  (  )  ,  targetDirectory  )  ; 
if  (  null  !=  m_modulesPom  &&  !  "pom"  .  equals  (  m_modulesPom  .  getPackaging  (  )  )  )  { 
throw   new   MojoExecutionException  (  "Containing project does not have packaging type 'pom'"  )  ; 
} 
}  catch  (  IOException   e  )  { 
getLog  (  )  .  warn  (  "Unable to create module tree"  )  ; 
} 
} 
} 






protected   abstract   void   updateExtensionFields  (  )  throws   MojoExecutionException  ; 




protected   abstract   String   getParentId  (  )  ; 






protected   void   cacheOriginalFiles  (  File   baseDir  )  { 
} 








protected   void   postProcess  (  Pom   pom  ,  Bnd   bnd  )  throws   MojoExecutionException  { 
} 




protected   boolean   createMoreArtifacts  (  )  { 
return   false  ; 
} 




private   VersionRange   getArchetypeVersionRange  (  )  { 
ArtifactVersion   version  =  new   DefaultArtifactVersion  (  pluginVersion  )  ; 
int   thisRelease  =  version  .  getMajorVersion  (  )  ; 
int   prevRelease  =  thisRelease  -  1  ; 
int   nextRelease  =  thisRelease  +  1  ; 
String   spec  ; 
if  (  false  ==  ArtifactUtils  .  isSnapshot  (  pluginVersion  )  )  { 
spec  =  "["  +  thisRelease  +  ','  +  nextRelease  +  ')'  ; 
}  else  { 
spec  =  "["  +  prevRelease  +  ','  +  nextRelease  +  ')'  ; 
} 
try  { 
return   VersionRange  .  createFromVersionSpec  (  spec  )  ; 
}  catch  (  InvalidVersionSpecificationException   e  )  { 
return   null  ; 
} 
} 








private   String   getArchetypeVersion  (  String   groupId  ,  String   artifactId  )  { 
Artifact   artifact  =  m_factory  .  createBuildArtifact  (  groupId  ,  artifactId  ,  pluginVersion  ,  "jar"  )  ; 
if  (  artifact  .  isSnapshot  (  )  &&  PomUtils  .  getFile  (  artifact  ,  m_resolver  ,  m_localRepo  )  )  { 
return   pluginVersion  ; 
} 
VersionRange   range  =  getArchetypeVersionRange  (  )  ; 
try  { 
getLog  (  )  .  info  (  "Selecting latest archetype release within version range "  +  range  )  ; 
return   PomUtils  .  getReleaseVersion  (  artifact  ,  m_source  ,  m_remoteRepos  ,  m_localRepo  ,  range  )  ; 
}  catch  (  MojoExecutionException   e  )  { 
return   pluginVersion  ; 
} 
} 






protected   final   void   setMainArchetype  (  String   archetypeArtifactId  )  { 
if  (  PomUtils  .  isEmpty  (  archetypeVersion  )  )  { 
archetypeVersion  =  getArchetypeVersion  (  PAX_CONSTRUCT_GROUP_ID  ,  archetypeArtifactId  )  ; 
} 
setArchetypeProperty  (  "archetypeGroupId"  ,  PAX_CONSTRUCT_GROUP_ID  )  ; 
setArchetypeProperty  (  "archetypeArtifactId"  ,  archetypeArtifactId  )  ; 
setArchetypeProperty  (  "archetypeVersion"  ,  archetypeVersion  )  ; 
} 






private   void   prepareTarget  (  )  throws   MojoExecutionException  { 
String   artifactId  =  getArchetypeProperty  (  "artifactId"  )  ; 
File   pomDirectory  =  new   File  (  targetDirectory  ,  artifactId  )  ; 
m_pomFile  =  new   File  (  pomDirectory  ,  "pom.xml"  )  ; 
if  (  m_pomFile  .  exists  (  )  )  { 
if  (  overwrite  )  { 
m_pomFile  .  delete  (  )  ; 
}  else  { 
throw   new   MojoExecutionException  (  "Project already exists, use -Doverwrite or -o to replace it"  )  ; 
} 
} 
m_tempFiles  =  new   FileSet  (  )  ; 
m_tempFiles  .  setDirectory  (  pomDirectory  .  getAbsolutePath  (  )  )  ; 
if  (  pomDirectory  .  exists  (  )  )  { 
preserveExistingFiles  (  pomDirectory  )  ; 
}  else  { 
pomDirectory  .  mkdirs  (  )  ; 
} 
if  (  null  !=  m_modulesPom  )  { 
setArchetypeProperty  (  "isMultiModuleProject"  ,  "true"  )  ; 
try  { 
m_modulesPom  .  addModule  (  pomDirectory  .  getName  (  )  ,  true  )  ; 
m_modulesPom  .  write  (  )  ; 
}  catch  (  IOException   e  )  { 
getLog  (  )  .  warn  (  "Unable to attach POM to existing project"  )  ; 
} 
} 
} 





private   void   preserveExistingFiles  (  File   baseDir  )  throws   MojoExecutionException  { 
try  { 
List   excludes  =  FileUtils  .  getFileNames  (  baseDir  ,  null  ,  null  ,  false  )  ; 
for  (  Iterator   i  =  excludes  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
getLog  (  )  .  debug  (  "Preserving "  +  i  .  next  (  )  )  ; 
} 
m_tempFiles  .  setExcludes  (  excludes  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   MojoExecutionException  (  "I/O error while protecting existing files from deletion"  ,  e  )  ; 
} 
} 






private   void   cacheSettings  (  )  throws   MojoExecutionException  { 
try  { 
if  (  null  !=  m_modulesPom  )  { 
DirUtils  .  updateLogicalParent  (  m_pomFile  ,  getParentId  (  )  )  ; 
} 
m_pom  =  PomUtils  .  readPom  (  m_pomFile  )  ; 
if  (  hasCustomContent  (  )  )  { 
m_pom  .  getFile  (  )  .  delete  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   MojoExecutionException  (  "I/O error reading generated Maven POM "  +  m_pomFile  ,  e  )  ; 
} 
try  { 
m_bnd  =  BndUtils  .  readBnd  (  m_pom  .  getBasedir  (  )  )  ; 
if  (  hasCustomContent  (  )  )  { 
m_bnd  .  getFile  (  )  .  delete  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
throw   new   MojoExecutionException  (  "I/O error reading generated Bnd instructions"  ,  e  )  ; 
} 
if  (  hasCustomContent  (  )  )  { 
cacheOriginalFiles  (  m_pom  .  getBasedir  (  )  )  ; 
} 
} 






private   void   runCustomArchetypes  (  )  throws   MojoExecutionException  { 
for  (  Iterator   i  =  m_customArchetypeIds  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
String  [  ]  fields  =  (  (  String  )  i  .  next  (  )  )  .  split  (  ":"  )  ; 
setArchetypeProperty  (  "archetypeGroupId"  ,  fields  [  0  ]  )  ; 
setArchetypeProperty  (  "archetypeArtifactId"  ,  fields  [  1  ]  )  ; 
setArchetypeProperty  (  "archetypeVersion"  ,  fields  [  2  ]  )  ; 
generateArchetype  (  )  ; 
} 
} 






private   void   postProcess  (  )  throws   MojoExecutionException  { 
postProcess  (  m_pom  ,  m_bnd  )  ; 
try  { 
saveProjectModel  (  m_pom  )  ; 
saveBndInstructions  (  m_bnd  )  ; 
}  catch  (  IOException   e  )  { 
getLog  (  )  .  error  (  "Unable to save customized settings"  )  ; 
} 
} 





protected   final   void   saveProjectModel  (  Pom   pom  )  throws   IOException  { 
if  (  hasCustomContent  (  )  &&  pom  .  getFile  (  )  .  exists  (  )  )  { 
Pom   customPom  =  PomUtils  .  readPom  (  pom  .  getBasedir  (  )  )  ; 
pom  .  overlayDetails  (  customPom  )  ; 
} 
pom  .  write  (  )  ; 
} 





protected   final   void   saveBndInstructions  (  Bnd   bnd  )  throws   IOException  { 
if  (  hasCustomContent  (  )  &&  bnd  .  getFile  (  )  .  exists  (  )  )  { 
Bnd   customBnd  =  BndUtils  .  readBnd  (  bnd  .  getBasedir  (  )  )  ; 
bnd  .  overlayInstructions  (  customBnd  )  ; 
} 
bnd  .  write  (  )  ; 
} 








protected   final   String   getCompoundId  (  String   groupId  ,  String   artifactId  )  { 
if  (  compactIds  )  { 
return   PomUtils  .  getCompoundId  (  groupId  ,  artifactId  )  ; 
} 
return   groupId  +  '.'  +  artifactId  ; 
} 




private   void   scheduleCustomArchetypes  (  )  { 
m_customArchetypeIds  =  new   ArrayList  (  )  ; 
if  (  !  hasCustomContent  (  )  )  { 
return  ; 
} 
String  [  ]  ids  =  contents  .  split  (  ","  )  ; 
for  (  int   i  =  0  ;  i  <  ids  .  length  ;  i  ++  )  { 
String   id  =  ids  [  i  ]  .  trim  (  )  ; 
String  [  ]  fields  =  id  .  split  (  ":"  )  ; 
if  (  fields  .  length  >  2  )  { 
scheduleArchetype  (  fields  [  0  ]  ,  fields  [  1  ]  ,  fields  [  2  ]  )  ; 
}  else   if  (  fields  .  length  >  1  )  { 
scheduleArchetype  (  fields  [  0  ]  ,  fields  [  0  ]  ,  fields  [  1  ]  )  ; 
}  else  { 
scheduleArchetype  (  PAX_CONSTRUCT_GROUP_ID  ,  fields  [  0  ]  ,  null  )  ; 
} 
} 
} 








protected   final   void   scheduleArchetype  (  String   groupId  ,  String   artifactId  ,  String   version  )  { 
if  (  PomUtils  .  isEmpty  (  version  )  )  { 
m_customArchetypeIds  .  add  (  groupId  +  ':'  +  artifactId  +  ':'  +  archetypeVersion  )  ; 
}  else  { 
m_customArchetypeIds  .  add  (  groupId  +  ':'  +  artifactId  +  ':'  +  version  )  ; 
} 
} 




protected   final   Set   getFinalFilenames  (  )  { 
Set   finalFiles  =  new   HashSet  (  )  ; 
DirectoryScanner   scanner  =  new   DirectoryScanner  (  )  ; 
scanner  .  setBasedir  (  m_tempFiles  .  getDirectory  (  )  )  ; 
scanner  .  setFollowSymlinks  (  false  )  ; 
scanner  .  addDefaultExcludes  (  )  ; 
scanner  .  setExcludes  (  m_tempFiles  .  getExcludesArray  (  )  )  ; 
scanner  .  setIncludes  (  m_tempFiles  .  getIncludesArray  (  )  )  ; 
scanner  .  scan  (  )  ; 
finalFiles  .  addAll  (  Arrays  .  asList  (  scanner  .  getNotIncludedFiles  (  )  )  )  ; 
finalFiles  .  addAll  (  Arrays  .  asList  (  scanner  .  getExcludedFiles  (  )  )  )  ; 
return   finalFiles  ; 
} 




private   void   cleanUp  (  )  { 
DirectoryScanner   scanner  =  new   DirectoryScanner  (  )  ; 
scanner  .  setBasedir  (  m_tempFiles  .  getDirectory  (  )  )  ; 
scanner  .  setFollowSymlinks  (  false  )  ; 
scanner  .  addDefaultExcludes  (  )  ; 
scanner  .  setExcludes  (  m_tempFiles  .  getExcludesArray  (  )  )  ; 
scanner  .  setIncludes  (  m_tempFiles  .  getIncludesArray  (  )  )  ; 
scanner  .  scan  (  )  ; 
String  [  ]  discardedFiles  =  scanner  .  getIncludedFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  discardedFiles  .  length  ;  i  ++  )  { 
String   filename  =  discardedFiles  [  i  ]  ; 
getLog  (  )  .  debug  (  "Discarding "  +  filename  )  ; 
new   File  (  scanner  .  getBasedir  (  )  ,  filename  )  .  delete  (  )  ; 
} 
DirUtils  .  pruneEmptyFolders  (  scanner  .  getBasedir  (  )  )  ; 
} 





protected   final   void   setArchetypeProperty  (  String   name  ,  String   value  )  { 
if  (  null  !=  value  )  { 
m_archetypeProperties  .  setProperty  (  name  ,  value  )  ; 
}  else  { 
m_archetypeProperties  .  remove  (  name  )  ; 
} 
if  (  "packageName"  .  equals  (  name  )  )  { 
m_archetypeProperties  .  setProperty  (  "package"  ,  value  )  ; 
} 
} 





protected   final   String   getArchetypeProperty  (  String   name  )  { 
return   m_archetypeProperties  .  getProperty  (  name  )  ; 
} 






private   void   generateArchetype  (  )  throws   MojoExecutionException  { 
try  { 
String   groupId  =  getArchetypeProperty  (  "archetypeGroupId"  )  ; 
String   artifactId  =  getArchetypeProperty  (  "archetypeArtifactId"  )  ; 
String   version  =  getArchetypeProperty  (  "archetypeVersion"  )  ; 
m_archetype  .  createArchetype  (  groupId  ,  artifactId  ,  version  ,  m_localRepo  ,  m_remoteRepos  ,  m_archetypeProperties  )  ; 
}  catch  (  ArchetypeNotFoundException   e  )  { 
throw   new   MojoExecutionException  (  "Error creating from archetype"  ,  e  )  ; 
}  catch  (  ArchetypeDescriptorException   e  )  { 
throw   new   MojoExecutionException  (  "Error creating from archetype"  ,  e  )  ; 
}  catch  (  ArchetypeTemplateProcessingException   e  )  { 
throw   new   MojoExecutionException  (  "Error creating from archetype"  ,  e  )  ; 
} 
} 






private   ArtifactRepository   createRemoteRepository  (  String   id  ,  String   url  )  { 
ArtifactRepositoryPolicy   snapshots  =  new   ArtifactRepositoryPolicy  (  true  ,  ArtifactRepositoryPolicy  .  UPDATE_POLICY_ALWAYS  ,  null  )  ; 
ArtifactRepositoryPolicy   releases  =  new   ArtifactRepositoryPolicy  (  true  ,  ArtifactRepositoryPolicy  .  UPDATE_POLICY_ALWAYS  ,  null  )  ; 
return   m_repoFactory  .  createArtifactRepository  (  id  ,  url  ,  m_defaultLayout  ,  snapshots  ,  releases  )  ; 
} 
} 

