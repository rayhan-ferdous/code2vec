package   gate  .  yam  .  depend  ; 

import   gate  .  persist  .  PersistenceException  ; 
import   gate  .  util  .  GateException  ; 
import   gate  .  yam  .  YamFile  ; 
import   junit  .  framework  .  Test  ; 
import   junit  .  framework  .  TestCase  ; 
import   junit  .  framework  .  TestSuite  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  springframework  .  core  .  io  .  FileSystemResource  ; 
import   java  .  io  .  *  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 





public   class   DependenciesTests   extends   TestCase  { 


static   Logger   log  =  Logger  .  getLogger  (  "gate.yam.depend.DependenciesTests"  )  ; 







private   static   Pattern   testFilePattern  =  Pattern  .  compile  (  "yam-depends-(\\d+).yam"  )  ; 






private   static   Pattern   originalTestFilePattern  =  Pattern  .  compile  (  "(yam-depends-)ORIGINAL-(\\d+.yam)"  )  ; 





class   DependenciesTestsFileFilter   implements   FilenameFilter  { 


public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   testFilePattern  .  matcher  (  name  )  .  matches  (  )  ; 
} 
} 






class   DependenciesTestsOriginalFileFilter   implements   FilenameFilter  { 




public   boolean   accept  (  File   dir  ,  String   name  )  { 
return   originalTestFilePattern  .  matcher  (  name  )  .  matches  (  )  ; 
} 
} 




private   static   File   yamDir  ; 







Map  <  String  ,  String  >  yamFileNameMap  =  new   HashMap  <  String  ,  String  >  (  )  ; 





Map  <  String  ,  YamFile  >  yamFileMap  =  new   HashMap  <  String  ,  YamFile  >  (  )  ; 




private   List  <  YamFile  >  wiki1  =  new   ArrayList  <  YamFile  >  (  )  ; 




private   Dependencies   dep1  ; 





public   DependenciesTests  (  String   testName  )  { 
super  (  testName  )  ; 
} 




protected   void   setUp  (  )  { 
String   testDirName  =  System  .  getProperty  (  "gate.yam.depend.test.dir"  )  ; 
if  (  testDirName  ==  null  )  { 
testDirName  =  "test/scratch/dependencies"  ; 
} 
File   testDirFile  =  new   File  (  testDirName  )  ; 
testDirFile  .  mkdirs  (  )  ; 
Dependencies  .  setSerializationDirectory  (  testDirFile  )  ; 
String   yamDirName  =  System  .  getProperty  (  "java.yam.resources.dir"  )  ; 
if  (  yamDirName  ==  null  )  { 
yamDirName  =  this  .  getClass  (  )  .  getResource  (  "/gate/yam/resources"  )  .  getFile  (  )  ; 
} 
log  .  info  (  "Getting test yam files from: "  +  yamDirName  )  ; 
yamDir  =  new   File  (  yamDirName  )  ; 
File  [  ]  originalVersions  =  yamDir  .  listFiles  (  new   DependenciesTestsOriginalFileFilter  (  )  )  ; 
for  (  File   origFile  :  originalVersions  )  { 
Matcher   matcher  =  originalTestFilePattern  .  matcher  (  origFile  .  getName  (  )  )  ; 
if  (  !  matcher  .  matches  (  )  )  { 
fail  (  "Failed to setup - filename not conventional: "  +  origFile  .  getName  (  )  )  ; 
} 
String   newName  =  matcher  .  group  (  1  )  +  matcher  .  group  (  2  )  ; 
File   newFile  =  new   File  (  yamDir  ,  newName  )  ; 
copy  (  origFile  ,  newFile  )  ; 
} 
File  [  ]  testFiles  =  yamDir  .  listFiles  (  new   DependenciesTestsFileFilter  (  )  )  ; 
for  (  File   file  :  testFiles  )  { 
YamFile   yamFile  =  YamFile  .  get  (  new   FileSystemResource  (  file  )  )  ; 
try  { 
String   canPath  =  yamFile  .  getCanonicalPath  (  )  ; 
log  .  info  (  "Getting test file: "  +  canPath  )  ; 
Matcher   matcher  =  testFilePattern  .  matcher  (  file  .  getName  (  )  )  ; 
if  (  !  matcher  .  matches  (  )  )  { 
fail  (  "Failed to setup - filename not conventional: "  +  file  .  getName  (  )  )  ; 
} 
String   number  =  matcher  .  group  (  1  )  ; 
yamFile  .  generate  (  )  ; 
yamFileNameMap  .  put  (  number  ,  canPath  )  ; 
yamFileMap  .  put  (  number  ,  yamFile  )  ; 
}  catch  (  GateException   ge  )  { 
fail  (  "Failed to setup: "  +  ge  .  getMessage  (  )  )  ; 
} 
} 
wiki1  .  add  (  yamFileMap  .  get  (  "1"  )  )  ; 
wiki1  .  add  (  yamFileMap  .  get  (  "2"  )  )  ; 
wiki1  .  add  (  yamFileMap  .  get  (  "3"  )  )  ; 
wiki1  .  add  (  yamFileMap  .  get  (  "4"  )  )  ; 
wiki1  .  add  (  yamFileMap  .  get  (  "5"  )  )  ; 
try  { 
Dependencies  .  remove  (  "1"  )  ; 
dep1  =  Dependencies  .  get  (  "1"  )  ; 
}  catch  (  PersistenceException   pe  )  { 
fail  (  "Failed to set up: "  +  pe  .  getMessage  (  )  )  ; 
} 
assertTrue  (  "New Dependencies not empty"  ,  dep1  .  isEmpty  (  )  )  ; 
for  (  YamFile   yam  :  wiki1  )  { 
dep1  .  created  (  yam  )  ; 
} 
assertFalse  (  "Dependencies is empty after adding links"  ,  dep1  .  isEmpty  (  )  )  ; 
} 






public   void   testCreateAndBasics  (  )  throws   Exception  { 
log  .  info  (  "========== DependenciesTests.tesCreateAndBasics() =============="  )  ; 
log  .  info  (  "testing dependencies.created(YamFile) and equality methods"  )  ; 
Dependencies   dep2  =  Dependencies  .  get  (  "2"  )  ; 
for  (  YamFile   yam  :  wiki1  )  { 
dep2  .  created  (  yam  )  ; 
} 
assertEquals  (  "Dependencies linksTo not correct after create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep2  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep2  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep2  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep2  .  includedByAsString  (  )  )  ; 
assertEquals  (  "Same Dependencies not equal"  ,  dep1  ,  dep1  )  ; 
assertEquals  (  "Dependencies not equal after get"  ,  dep1  ,  Dependencies  .  get  (  "1"  )  )  ; 
assertEquals  (  "Identical Dependencies not equal"  ,  dep1  ,  dep2  )  ; 
assertEquals  (  "Identical Dependencies with different hash codes"  ,  dep1  .  hashCode  (  )  ,  dep2  .  hashCode  (  )  )  ; 
Dependencies  .  remove  (  "2"  )  ; 
assertFalse  (  "Dependencies still exists after removal"  ,  Dependencies  .  exists  (  "2"  )  )  ; 
} 





public   void   testDelete  (  )  throws   Exception  { 
log  .  info  (  "============== DependenciesTests.testDelete() =================="  )  ; 
log  .  info  (  "testing dependencies.deleted(YamFile)"  )  ; 
Set  <  String  >  toRegenerate  ; 
toRegenerate  =  dep1  .  deleted  (  yamFileMap  .  get  (  "1"  )  )  ; 
assertEquals  (  "Regenerate set not correct after delete"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after delete"  ,  shorthandToPaths  (  "2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after delete"  ,  shorthandToPaths  (  "4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after delete"  ,  shorthandToPaths  (  "2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after delete"  ,  shorthandToPaths  (  "4:[2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
toRegenerate  =  dep1  .  created  (  yamFileMap  .  get  (  "1"  )  )  ; 
assertEquals  (  "Regenerate set not correct after create"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
} 





public   void   testRename  (  )  throws   Exception  { 
log  .  info  (  "============== DependenciesTests.testRename() =================="  )  ; 
log  .  info  (  "testing dependencies.renamed(YamFile)"  )  ; 
Set  <  String  >  toRegenerate  ; 
List  <  String  >  toRegenerateSorted  ; 
toRegenerate  =  dep1  .  renamed  (  yamFileMap  .  get  (  "2"  )  ,  yamFileMap  .  get  (  "6"  )  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after rename"  ,  numbersToList  (  "1"  )  ,  toRegenerateSorted  )  ; 
assertEquals  (  "Dependencies linksTo not correct after rename"  ,  shorthandToPaths  (  "1:[3,6];3:[5];6:[4,5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after rename"  ,  shorthandToPaths  (  "3:[1];4:[6];5:[3,6];6:[1]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after rename"  ,  shorthandToPaths  (  "1:[3,4,6];6:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after rename"  ,  shorthandToPaths  (  "3:[1];4:[1,6];6:[1]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
dep1  .  renamed  (  yamFileMap  .  get  (  "6"  )  ,  yamFileMap  .  get  (  "2"  )  )  ; 
} 





public   void   testDelete2  (  )  throws   Exception  { 
log  .  info  (  "============== DependenciesTests.testDelete2() ================="  )  ; 
log  .  info  (  "testing dependencies.deleted(YamFile)"  )  ; 
Set  <  String  >  toRegenerate  ; 
List  <  String  >  toRegenerateSorted  ; 
toRegenerate  =  dep1  .  deleted  (  yamFileMap  .  get  (  "4"  )  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after delete"  ,  numbersToList  (  "1,2"  )  ,  toRegenerateSorted  )  ; 
toRegenerate  =  dep1  .  deleted  (  yamFileMap  .  get  (  "5"  )  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after delete"  ,  numbersToList  (  "2,3"  )  ,  toRegenerateSorted  )  ; 
dep1  .  created  (  yamFileMap  .  get  (  "4"  )  )  ; 
dep1  .  created  (  yamFileMap  .  get  (  "5"  )  )  ; 
assertEquals  (  "Dependencies linksTo not correct after delete and create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after delete and create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after delete and create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after delete and create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
} 





public   void   testModify  (  )  throws   Exception  { 
log  .  info  (  "============== DependenciesTests.testModify() =================="  )  ; 
log  .  info  (  "testing dependencies.modified(YamFile)"  )  ; 
Set  <  String  >  toRegenerate  ; 
List  <  String  >  toRegenerateSorted  ; 
File   currentFile  =  new   File  (  yamDir  ,  "yam-depends-2.yam"  )  ; 
File   modFile  =  new   File  (  yamDir  ,  "yam-depends-MODIFIED-2.yam"  )  ; 
copy  (  modFile  ,  currentFile  )  ; 
yamFileMap  .  get  (  "2"  )  .  generate  (  )  ; 
toRegenerate  =  dep1  .  modified  (  yamFileMap  .  get  (  "2"  )  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after modify"  ,  numbersToList  (  "1"  )  ,  toRegenerateSorted  )  ; 
assertEquals  (  "Dependencies linksTo not correct after modify"  ,  shorthandToPaths  (  "1:[2,3];2:[4,7];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after modify"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[3];7:[2]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after modify"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after modify"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
currentFile  =  new   File  (  yamDir  ,  "yam-depends-1.yam"  )  ; 
modFile  =  new   File  (  yamDir  ,  "yam-depends-MODIFIED-1.yam"  )  ; 
copy  (  modFile  ,  currentFile  )  ; 
yamFileMap  .  get  (  "1"  )  .  generate  (  )  ; 
toRegenerate  =  dep1  .  modified  (  yamFileMap  .  get  (  "1"  )  )  ; 
assertEquals  (  "Regenerate set not correct after modify"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after modify"  ,  shorthandToPaths  (  "1:[2,3];2:[4,7];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after modify"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[3];7:[2]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after modify"  ,  shorthandToPaths  (  "1:[3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after modify"  ,  shorthandToPaths  (  "3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
copy  (  new   File  (  yamDir  ,  "yam-depends-ORIGINAL-1.yam"  )  ,  new   File  (  yamDir  ,  "yam-depends-1.yam"  )  )  ; 
copy  (  new   File  (  yamDir  ,  "yam-depends-ORIGINAL-2.yam"  )  ,  new   File  (  yamDir  ,  "yam-depends-2.yam"  )  )  ; 
yamFileMap  .  get  (  "1"  )  .  generate  (  )  ; 
yamFileMap  .  get  (  "2"  )  .  generate  (  )  ; 
} 





public   void   testNonYamLinks  (  )  throws   Exception  { 
log  .  info  (  "============ DependenciesTests.testNonYamLinks() ==============="  )  ; 
log  .  info  (  "testing links to non yam files and urls"  )  ; 
YamFile   yf  =  yamFileMap  .  get  (  "8"  )  ; 
wiki1  .  add  (  yf  )  ; 
dep1  .  created  (  yf  )  ; 
assertEquals  (  "Dependencies linksTo not correct after create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5];"  +  "8:[../../non-existent.html,non-existent.html]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after create"  ,  shorthandToPaths  (  "../../non-existent.html:[8];"  +  ""  +  "non-existent.html:[8];2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
dep1  .  deleted  (  yf  )  ; 
assertEquals  (  "Dependencies linksTo not correct after delete"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after delete"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after delete"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after delete"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
} 





public   void   testNonYamChanges  (  )  throws   Exception  { 
log  .  info  (  "========= DependenciesTests.testNonYamChanges() ============="  )  ; 
Set  <  String  >  toRegenerate  ; 
List  <  String  >  toRegenerateSorted  ; 
log  .  info  (  "testing dependencies.created(File)"  )  ; 
File   nonYam  =  new   File  (  yamDir  ,  "nonYamFile.abc"  )  ; 
toRegenerate  =  dep1  .  created  (  nonYam  )  ; 
assertEquals  (  "Regenerate set not correct after created(File)"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
toRegenerate  =  dep1  .  deleted  (  nonYam  )  ; 
assertEquals  (  "Regenerate set not correct after deleted(File)"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
YamFile   linkingFile  =  yamFileMap  .  get  (  "9"  )  ; 
wiki1  .  add  (  linkingFile  )  ; 
toRegenerate  =  dep1  .  created  (  linkingFile  )  ; 
assertEquals  (  "Regenerate set not correct after created(YamFile)"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after create"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5];9:[nonYamFile.abc]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after create"  ,  shorthandToPaths  (  "nonYamFile.abc:[9];2:[1];3:[1];4:[2];5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after create"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after create"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
toRegenerate  =  dep1  .  created  (  nonYam  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after created(File)"  ,  numbersToList  (  "9"  )  ,  toRegenerateSorted  )  ; 
log  .  info  (  "testing dependencies.renamed(File, File)"  )  ; 
File   nonYamRenamed  =  new   File  (  yamDir  ,  "nonYamFileRenamed.abc"  )  ; 
toRegenerate  =  dep1  .  renamed  (  nonYam  ,  nonYamRenamed  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after renamed(File)"  ,  numbersToList  (  "9"  )  ,  toRegenerateSorted  )  ; 
assertEquals  (  "Dependencies linksTo not correct after rename"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5];9:[nonYamFileRenamed.abc]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after rename"  ,  shorthandToPaths  (  "nonYamFileRenamed.abc:[9];2:[1];3:[1];4:[2]"  +  ";5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after rename"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after rename"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
log  .  info  (  "testing dependencies.deleted(File)"  )  ; 
toRegenerate  =  dep1  .  deleted  (  nonYamRenamed  )  ; 
toRegenerateSorted  =  new   ArrayList  <  String  >  (  toRegenerate  )  ; 
Collections  .  sort  (  toRegenerateSorted  )  ; 
assertEquals  (  "Regenerate set not correct after deleted(File)"  ,  numbersToList  (  "9"  )  ,  toRegenerateSorted  )  ; 
assertEquals  (  "Dependencies linksTo not correct after delete"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5];9:[nonYamFileRenamed.abc]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after delete"  ,  shorthandToPaths  (  "nonYamFileRenamed.abc:[9];2:[1];3:[1];4:[2]"  +  ";5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after delete"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after delete"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
toRegenerate  =  dep1  .  deleted  (  linkingFile  )  ; 
assertEquals  (  "Regenerate set not correct after deleted(YamFile)"  ,  new   HashSet  <  String  >  (  )  ,  toRegenerate  )  ; 
assertEquals  (  "Dependencies linksTo not correct after delete"  ,  shorthandToPaths  (  "1:[2,3];2:[4,5];3:[5]"  )  ,  dep1  .  linksToAsString  (  )  )  ; 
assertEquals  (  "Dependencies linkedBy not correct after delete"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[2]"  +  ";5:[2,3]"  )  ,  dep1  .  linkedByAsString  (  )  )  ; 
assertEquals  (  "Dependencies includes not correct after delete"  ,  shorthandToPaths  (  "1:[2,3,4];2:[4]"  )  ,  dep1  .  includesAsString  (  )  )  ; 
assertEquals  (  "Dependencies includedBy not correct after delete"  ,  shorthandToPaths  (  "2:[1];3:[1];4:[1,2]"  )  ,  dep1  .  includedByAsString  (  )  )  ; 
} 





public   void   testSerialization  (  )  throws   Exception  { 
log  .  info  (  "============= DependenciesTests.testSerialization() ============"  )  ; 
log  .  info  (  "testing serialization and deserialization of Dependencies"  )  ; 
Dependencies  .  remove  (  "1"  )  ; 
Dependencies   dep1  =  Dependencies  .  get  (  "1"  )  ; 
for  (  YamFile   yam  :  wiki1  )  { 
dep1  .  created  (  yam  )  ; 
} 
Dependencies  .  serialize  (  )  ; 
Dependencies  .  clear  (  )  ; 
assertTrue  (  "Dependencies removed"  ,  Dependencies  .  exists  (  "1"  )  )  ; 
Dependencies   dep1Reloaded  =  Dependencies  .  get  (  "1"  )  ; 
assertEquals  (  "Dependencies not consistently serialized / deserialized"  ,  dep1  ,  dep1Reloaded  )  ; 
Dependencies  .  remove  (  "1"  )  ; 
} 





public   static   Test   suite  (  )  { 
TestSuite   suite  =  new   TestSuite  (  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testCreateAndBasics"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testDelete"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testRename"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testDelete2"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testModify"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testNonYamLinks"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testNonYamChanges"  )  )  ; 
suite  .  addTest  (  new   DependenciesTests  (  "testSerialization"  )  )  ; 
return   suite  ; 
} 




















public   String   shorthandToPaths  (  String   str  )  { 
StringBuilder   bldr  =  new   StringBuilder  (  )  ; 
for  (  String   nodeAndArcs  :  str  .  split  (  "(\\]\\;)|(\\])"  )  )  { 
String  [  ]  nodeAndArcsSplit  =  nodeAndArcs  .  split  (  "\\:\\["  )  ; 
String   node  =  nodeAndArcsSplit  [  0  ]  ; 
String   arcs  =  nodeAndArcsSplit  [  1  ]  ; 
String   nodePath  =  null  ; 
if  (  node  .  matches  (  "\\d+"  )  )  { 
nodePath  =  yamFileNameMap  .  get  (  node  )  ; 
}  else  { 
File   yamParent  =  new   File  (  yamFileNameMap  .  get  (  arcs  )  )  .  getParentFile  (  )  ; 
try  { 
nodePath  =  new   File  (  yamParent  ,  node  )  .  getCanonicalPath  (  )  ; 
}  catch  (  IOException   ioe  )  { 
fail  (  "Couldn't resolve link canonical path: "  +  ioe  .  getMessage  (  )  )  ; 
} 
} 
bldr  .  append  (  nodePath  )  ; 
bldr  .  append  (  ":["  )  ; 
for  (  String   arc  :  arcs  .  split  (  "\\,"  )  )  { 
if  (  arc  .  matches  (  "\\d+"  )  )  { 
bldr  .  append  (  yamFileNameMap  .  get  (  arc  )  )  ; 
}  else  { 
File   yamParent  =  new   File  (  nodePath  )  .  getParentFile  (  )  ; 
File   link  =  new   File  (  yamParent  ,  arc  )  ; 
try  { 
bldr  .  append  (  link  .  getCanonicalPath  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
fail  (  "Couldn't resolve link canonical path: "  +  ioe  .  getMessage  (  )  )  ; 
} 
} 
bldr  .  append  (  ","  )  ; 
} 
bldr  .  deleteCharAt  (  bldr  .  length  (  )  -  1  )  ; 
bldr  .  append  (  "];"  )  ; 
} 
bldr  .  deleteCharAt  (  bldr  .  length  (  )  -  1  )  ; 
return   bldr  .  toString  (  )  ; 
} 









private   List  <  String  >  numbersToList  (  String   str  )  { 
List  <  String  >  pathList  =  new   ArrayList  <  String  >  (  )  ; 
for  (  String   number  :  str  .  split  (  ","  )  )  { 
pathList  .  add  (  yamFileNameMap  .  get  (  number  )  )  ; 
} 
return   pathList  ; 
} 






private   void   copy  (  File   in  ,  File   out  )  { 
log  .  info  (  "Copying yam file from: "  +  in  .  getName  (  )  +  " to: "  +  out  .  getName  (  )  )  ; 
try  { 
FileChannel   ic  =  new   FileInputStream  (  in  )  .  getChannel  (  )  ; 
FileChannel   oc  =  new   FileOutputStream  (  out  )  .  getChannel  (  )  ; 
ic  .  transferTo  (  0  ,  ic  .  size  (  )  ,  oc  )  ; 
ic  .  close  (  )  ; 
oc  .  close  (  )  ; 
}  catch  (  IOException   ioe  )  { 
fail  (  "Failed testing while copying modified file: "  +  ioe  .  getMessage  (  )  )  ; 
} 
} 
} 

