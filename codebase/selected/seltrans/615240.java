package   uk  .  ac  .  ebi  .  interpro  .  scan  .  io  .  model  ; 

import   uk  .  ac  .  ebi  .  interpro  .  scan  .  io  .  ParseException  ; 
import   java  .  io  .  *  ; 
import   java  .  nio  .  channels  .  FileLock  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 






















public   class   GaValuesRetriever   implements   Serializable  { 




private   static   final   Pattern   GA_LINE_PATTERN  =  Pattern  .  compile  (  "^GA\\s+(.+);?\\s*$"  )  ; 

private   static   final   Pattern   ACCESSION_PATTERN  =  Pattern  .  compile  (  "^ACC\\s+([A-Z0-9]+)\\.?.*$"  )  ; 

private   static   final   String   END_OF_RECORD  =  "//"  ; 

private   Properties   accessionToGAProps  =  null  ; 

private   static   final   String   MAP_FILE_EXTENSION  =  ".accession_to_ga_map"  ; 

private   static   final   Map  <  String  ,  Double  >  ACC_TO_SEQUENCE_GA  =  new   HashMap  <  String  ,  Double  >  (  )  ; 

private   static   final   Map  <  String  ,  Double  >  ACC_TO_DOMAIN_GA  =  new   HashMap  <  String  ,  Double  >  (  )  ; 

private   boolean   initialised  =  false  ; 

private   final   String   modelFileAbsolutePath  ; 










public   GaValuesRetriever  (  String   modelFileAbsolutePath  )  throws   IOException  { 
this  .  modelFileAbsolutePath  =  modelFileAbsolutePath  ; 
} 

private   File   createMapFileObject  (  String   modelFileAbsolutePath  )  throws   IOException  { 
return   new   File  (  modelFileAbsolutePath  +  MAP_FILE_EXTENSION  )  ; 
} 














private   synchronized   void   storeMappingToPropertiesFile  (  String   modelFileAbsolutePath  )  throws   IOException  { 
File   mapFile  =  createMapFileObject  (  modelFileAbsolutePath  )  ; 
if  (  mapFile  .  exists  (  )  )  { 
return  ; 
} 
FileOutputStream   fos  =  null  ; 
try  { 
fos  =  new   FileOutputStream  (  mapFile  )  ; 
FileLock   lock  =  fos  .  getChannel  (  )  .  lock  (  )  ; 
accessionToGAProps  .  store  (  fos  ,  "Mapping of model accessions to GA values."  )  ; 
lock  .  release  (  )  ; 
}  catch  (  Exception   ioe  )  { 
mapFile  .  delete  (  )  ; 
}  finally  { 
if  (  fos  !=  null  )  { 
fos  .  close  (  )  ; 
} 
} 
} 













private   synchronized   void   loadMappingFromPropertiesFile  (  File   mapFile  )  throws   IOException  { 
FileInputStream   fis  =  null  ; 
try  { 
fis  =  new   FileInputStream  (  mapFile  )  ; 
FileLock   lock  =  fis  .  getChannel  (  )  .  lock  (  0  ,  Long  .  MAX_VALUE  ,  true  )  ; 
accessionToGAProps  =  new   Properties  (  )  ; 
accessionToGAProps  .  load  (  fis  )  ; 
lock  .  release  (  )  ; 
}  finally  { 
if  (  fis  !=  null  )  { 
fis  .  close  (  )  ; 
} 
} 
} 

public   void   parseModelFile  (  String   modelFileAbsolutePath  )  throws   IOException  { 
BufferedReader   reader  =  null  ; 
accessionToGAProps  =  new   Properties  (  )  ; 
try  { 
reader  =  new   BufferedReader  (  new   FileReader  (  modelFileAbsolutePath  )  )  ; 
String   accession  =  null  ,  gaValues  =  null  ; 
int   lineNumber  =  0  ; 
String   line  ; 
while  (  (  line  =  reader  .  readLine  (  )  )  !=  null  )  { 
lineNumber  ++  ; 
char   firstChar  =  line  .  charAt  (  0  )  ; 
if  (  firstChar  ==  '/'  ||  firstChar  ==  'G'  ||  firstChar  ==  'A'  )  { 
if  (  line  .  startsWith  (  END_OF_RECORD  )  )  { 
if  (  accession  ==  null  ||  gaValues  ==  null  )  { 
throw   new   IllegalStateException  (  "The HMMER3 model file '"  +  modelFileAbsolutePath  +  "' contains a record that does not contain a accession and GA row.  PfamModel ending on line number "  +  lineNumber  )  ; 
} 
if  (  accessionToGAProps  .  containsKey  (  accession  )  )  { 
throw   new   IllegalStateException  (  "The HMMER3 model file '"  +  modelFileAbsolutePath  +  "' contains a duplicated model accession "  +  accession  )  ; 
} 
accessionToGAProps  .  put  (  accession  ,  gaValues  )  ; 
accession  =  null  ; 
gaValues  =  null  ; 
}  else   if  (  line  .  startsWith  (  "ACC"  )  )  { 
Matcher   nameMatcher  =  ACCESSION_PATTERN  .  matcher  (  line  )  ; 
if  (  nameMatcher  .  matches  (  )  )  { 
if  (  accession  !=  null  )  { 
throw   new   IllegalStateException  (  "The HMMER3 model file '"  +  modelFileAbsolutePath  +  "' contains a record that appears to contain more than one accession row.  PfamModel ending on line number "  +  lineNumber  )  ; 
} 
accession  =  nameMatcher  .  group  (  1  )  ; 
} 
}  else   if  (  line  .  startsWith  (  "GA"  )  )  { 
Matcher   accessionMatcher  =  GA_LINE_PATTERN  .  matcher  (  line  )  ; 
if  (  accessionMatcher  .  matches  (  )  )  { 
if  (  gaValues  !=  null  )  { 
throw   new   IllegalStateException  (  "The HMMER3 model file '"  +  modelFileAbsolutePath  +  "' contains a record that appears to contain more than one GA row.  PfamModel ending on line number "  +  lineNumber  )  ; 
} 
gaValues  =  accessionMatcher  .  group  (  1  )  ; 
} 
} 
} 
} 
}  finally  { 
if  (  reader  !=  null  )  { 
reader  .  close  (  )  ; 
} 
} 
} 

public   double   getSequenceGAForAccession  (  String   modelAccession  )  throws   IOException  { 
lazyInitialise  (  )  ; 
return   ACC_TO_SEQUENCE_GA  .  get  (  modelAccession  )  ; 
} 

public   double   getDomainGAForAccession  (  String   modelAccession  )  throws   IOException  { 
lazyInitialise  (  )  ; 
return   ACC_TO_DOMAIN_GA  .  get  (  modelAccession  )  ; 
} 

private   void   lazyInitialise  (  )  throws   IOException  { 
if  (  !  initialised  )  { 
File   mapFile  =  createMapFileObject  (  modelFileAbsolutePath  )  ; 
if  (  mapFile  .  exists  (  )  )  { 
loadMappingFromPropertiesFile  (  mapFile  )  ; 
}  else  { 
parseModelFile  (  modelFileAbsolutePath  )  ; 
storeMappingToPropertiesFile  (  modelFileAbsolutePath  )  ; 
} 
for  (  Object   accessionObject  :  accessionToGAProps  .  keySet  (  )  )  { 
String   accession  =  (  String  )  accessionObject  ; 
String   gaString  =  (  String  )  accessionToGAProps  .  get  (  accessionObject  )  ; 
gaString  =  gaString  .  trim  (  )  ; 
if  (  gaString  .  endsWith  (  ";"  )  )  { 
gaString  =  gaString  .  substring  (  0  ,  gaString  .  length  (  )  -  1  )  ; 
} 
String   values  [  ]  =  gaString  .  split  (  "\\s"  )  ; 
if  (  values  .  length  <  2  )  { 
throw   new   ParseException  (  "The GA line format is not as expected (was expecting at least two floating point numbers separated by a space)."  ,  "NOT_A_FILE"  ,  gaString  ,  1  )  ; 
} 
ACC_TO_SEQUENCE_GA  .  put  (  accession  ,  new   Double  (  values  [  0  ]  )  )  ; 
ACC_TO_DOMAIN_GA  .  put  (  accession  ,  new   Double  (  values  [  1  ]  )  )  ; 
} 
initialised  =  true  ; 
} 
} 
} 

