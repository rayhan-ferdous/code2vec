package   org  .  neurpheus  .  nlp  .  morphology  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  neurpheus  .  nlp  .  morphology  .  baseimpl  .  TagsetImpl  ; 
import   org  .  neurpheus  .  nlp  .  morphology  .  tagset  .  Tagset  ; 
















public   class   DefaultMorphologyFactory   implements   MorphologyFactory  { 


private   static   Logger   logger  =  Logger  .  getLogger  (  DefaultMorphologyFactory  .  class  .  getName  (  )  )  ; 


private   static   DefaultMorphologyFactory   instance  =  new   DefaultMorphologyFactory  (  )  ; 





private   Map   stemmers  ; 





private   Map   mostAccurateStemmers  ; 





private   Map   fastestStemmers  ; 





private   Map   lemmatizers  ; 





private   Map   mostAccurateLemmatizers  ; 





private   Map   fastestLemmatizers  ; 





private   Map   analysiers  ; 





private   Map   mostAccurateAnalysiers  ; 





private   Map   fastestAnalysiers  ; 





private   Map   generators  ; 





private   Map   mostAccurateGenerators  ; 





private   Map   fastestGenerators  ; 






public   static   DefaultMorphologyFactory   getInstance  (  )  { 
if  (  instance  ==  null  )  { 
createInstance  (  )  ; 
} 
return   instance  ; 
} 




private   static   synchronized   void   createInstance  (  )  { 
if  (  instance  ==  null  )  { 
instance  =  new   DefaultMorphologyFactory  (  )  ; 
} 
} 




private   static   final   int   MORE_IMPORTANT  =  1000  ; 




private   static   final   int   LES_IMPORTANT  =  1  ; 




private   DefaultMorphologyFactory  (  )  { 
stemmers  =  lookupForMorphologyComponents  (  Stemmer  .  class  )  ; 
mostAccurateStemmers  =  createBestComponentsMap  (  stemmers  ,  MORE_IMPORTANT  ,  LES_IMPORTANT  )  ; 
fastestStemmers  =  createBestComponentsMap  (  stemmers  ,  LES_IMPORTANT  ,  MORE_IMPORTANT  )  ; 
lemmatizers  =  lookupForMorphologyComponents  (  Lemmatizer  .  class  )  ; 
mostAccurateLemmatizers  =  createBestComponentsMap  (  lemmatizers  ,  MORE_IMPORTANT  ,  LES_IMPORTANT  )  ; 
fastestLemmatizers  =  createBestComponentsMap  (  lemmatizers  ,  LES_IMPORTANT  ,  MORE_IMPORTANT  )  ; 
analysiers  =  lookupForMorphologyComponents  (  MorphologicalAnalyser  .  class  )  ; 
mostAccurateAnalysiers  =  createBestComponentsMap  (  analysiers  ,  MORE_IMPORTANT  ,  LES_IMPORTANT  )  ; 
fastestAnalysiers  =  createBestComponentsMap  (  analysiers  ,  LES_IMPORTANT  ,  MORE_IMPORTANT  )  ; 
generators  =  lookupForMorphologyComponents  (  WordFormGenerator  .  class  )  ; 
mostAccurateGenerators  =  createBestComponentsMap  (  generators  ,  MORE_IMPORTANT  ,  LES_IMPORTANT  )  ; 
fastestGenerators  =  createBestComponentsMap  (  generators  ,  LES_IMPORTANT  ,  MORE_IMPORTANT  )  ; 
} 














private   MorphologicalComponent   findComponent  (  final   Map   componentsMap  ,  final   Locale   loc  ,  final   double   qualityImportance  ,  final   double   speedImportance  ,  final   String   propertyName  ,  final   Object   propertyValue  )  { 
List   list  =  (  List  )  componentsMap  .  get  (  loc  )  ; 
if  (  list  ==  null  )  { 
return   null  ; 
} 
MorphologicalComponent   result  =  null  ; 
double   bestValue  =  -  1  ; 
for  (  final   Iterator   it  =  list  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
MorphologicalComponent   comp  =  (  MorphologicalComponent  )  it  .  next  (  )  ; 
double   value  =  comp  .  getQuality  (  )  *  qualityImportance  +  comp  .  getSpeed  (  )  *  speedImportance  ; 
if  (  value  >  bestValue  )  { 
boolean   match  =  true  ; 
if  (  propertyName  !=  null  )  { 
Object   v  =  comp  .  getProperty  (  propertyName  )  ; 
match  =  v  ==  propertyValue  ||  (  v  !=  null  &&  propertyValue  !=  null  &&  v  .  equals  (  propertyValue  )  )  ; 
} 
if  (  match  )  { 
result  =  comp  ; 
bestValue  =  value  ; 
} 
} 
} 
return   result  ; 
} 










private   Map   createBestComponentsMap  (  final   Map   components  ,  final   double   qualityImportance  ,  final   double   speedImportance  )  { 
Map   result  =  new   HashMap  (  )  ; 
for  (  final   Iterator   it  =  components  .  keySet  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
Locale   loc  =  (  Locale  )  it  .  next  (  )  ; 
MorphologicalComponent   component  =  findComponent  (  components  ,  loc  ,  qualityImportance  ,  speedImportance  ,  null  ,  null  )  ; 
if  (  component  !=  null  )  { 
result  .  put  (  loc  ,  component  )  ; 
} 
} 
return   result  ; 
} 








private   Map   lookupForMorphologyComponents  (  final   Class   componentInterface  )  { 
Map   componentsMap  =  new   HashMap  (  )  ; 
try  { 
List   implementations  =  lookupForImplementations  (  componentInterface  ,  null  ,  null  ,  false  ,  true  )  ; 
for  (  final   Iterator   it  =  implementations  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
MorphologicalComponent   comp  =  (  MorphologicalComponent  )  it  .  next  (  )  ; 
Collection   locales  =  comp  .  getSupportedLocales  (  )  ; 
if  (  locales  !=  null  )  { 
for  (  final   Iterator   lit  =  locales  .  iterator  (  )  ;  lit  .  hasNext  (  )  ;  )  { 
Object   obj  =  lit  .  next  (  )  ; 
if  (  obj  !=  null  &&  obj   instanceof   Locale  )  { 
Locale   loc  =  (  Locale  )  obj  ; 
try  { 
MorphologicalComponent   mc  =  comp  .  getInstance  (  loc  )  ; 
List   list  =  (  List  )  componentsMap  .  get  (  loc  )  ; 
if  (  list  ==  null  )  { 
list  =  new   ArrayList  (  )  ; 
componentsMap  .  put  (  loc  ,  list  )  ; 
} 
list  .  add  (  mc  )  ; 
}  catch  (  MorphologyException   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Cannot create instance of component "  +  comp  .  getClass  (  )  .  getName  (  )  +  " for locale "  +  loc  .  toString  (  )  ,  e  )  ; 
} 
}  else  { 
logger  .  warning  (  "The morphology component "  +  comp  .  getClass  (  )  .  getName  (  )  +  " has returned invalid object as locale."  )  ; 
} 
} 
} 
} 
}  catch  (  ClassNotFoundException   e  )  { 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  log  (  Level  .  FINE  ,  "Cannot find any implementation of "  +  componentInterface  .  getName  (  )  )  ; 
} 
} 
return   componentsMap  ; 
} 












































private   static   List   lookupForImplementations  (  final   Class   clazz  ,  final   ClassLoader   loader  ,  final   String  [  ]  defaultImplementations  ,  final   boolean   onlyFirst  ,  final   boolean   returnInstances  )  throws   ClassNotFoundException  { 
if  (  clazz  ==  null  )  { 
throw   new   IllegalArgumentException  (  "Argument 'clazz' cannot be null!"  )  ; 
} 
ClassLoader   classLoader  =  loader  ; 
if  (  classLoader  ==  null  )  { 
classLoader  =  clazz  .  getClassLoader  (  )  ; 
} 
String   interfaceName  =  clazz  .  getName  (  )  ; 
ArrayList   tmp  =  new   ArrayList  (  )  ; 
ArrayList   toRemove  =  new   ArrayList  (  )  ; 
String   className  =  System  .  getProperty  (  interfaceName  )  ; 
if  (  className  !=  null  &&  className  .  trim  (  )  .  length  (  )  >  0  )  { 
tmp  .  add  (  className  .  trim  (  )  )  ; 
} 
Enumeration   en  =  null  ; 
try  { 
en  =  classLoader  .  getResources  (  "META-INF/services/"  +  clazz  .  getName  (  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
while  (  en  !=  null  &&  en  .  hasMoreElements  (  )  )  { 
URL   url  =  (  URL  )  en  .  nextElement  (  )  ; 
InputStream   is  =  null  ; 
try  { 
is  =  url  .  openStream  (  )  ; 
BufferedReader   reader  =  new   BufferedReader  (  new   InputStreamReader  (  is  ,  "UTF-8"  )  )  ; 
String   line  ; 
do  { 
line  =  reader  .  readLine  (  )  ; 
boolean   remove  =  false  ; 
if  (  line  !=  null  )  { 
if  (  line  .  startsWith  (  "#-"  )  )  { 
remove  =  true  ; 
line  =  line  .  substring  (  2  )  ; 
} 
int   pos  =  line  .  indexOf  (  '#'  )  ; 
if  (  pos  >=  0  )  { 
line  =  line  .  substring  (  0  ,  pos  )  ; 
} 
line  =  line  .  trim  (  )  ; 
if  (  line  .  length  (  )  >  0  )  { 
if  (  remove  )  { 
toRemove  .  add  (  line  )  ; 
}  else  { 
tmp  .  add  (  line  )  ; 
} 
} 
} 
}  while  (  line  !=  null  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
if  (  is  !=  null  )  { 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
if  (  defaultImplementations  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  defaultImplementations  .  length  ;  i  ++  )  { 
tmp  .  add  (  defaultImplementations  [  i  ]  .  trim  (  )  )  ; 
} 
} 
if  (  !  clazz  .  isInterface  (  )  )  { 
int   m  =  clazz  .  getModifiers  (  )  ; 
if  (  !  Modifier  .  isAbstract  (  m  )  &&  Modifier  .  isPublic  (  m  )  &&  !  Modifier  .  isStatic  (  m  )  )  { 
tmp  .  add  (  interfaceName  )  ; 
} 
} 
tmp  .  removeAll  (  toRemove  )  ; 
ArrayList   res  =  new   ArrayList  (  )  ; 
for  (  Iterator   it  =  tmp  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
className  =  (  String  )  it  .  next  (  )  ; 
try  { 
Class   c  =  Class  .  forName  (  className  ,  false  ,  classLoader  )  ; 
if  (  c  !=  null  )  { 
if  (  clazz  .  isAssignableFrom  (  c  )  )  { 
if  (  returnInstances  )  { 
Object   o  =  null  ; 
try  { 
o  =  c  .  newInstance  (  )  ; 
}  catch  (  Throwable   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
if  (  o  !=  null  )  { 
res  .  add  (  o  )  ; 
if  (  onlyFirst  )  { 
return   res  ; 
} 
} 
}  else  { 
res  .  add  (  c  )  ; 
if  (  onlyFirst  )  { 
return   res  ; 
} 
} 
}  else  { 
logger  .  warning  (  "MetaInfLookup: Class '"  +  className  +  "' is not a subclass of class : "  +  interfaceName  )  ; 
} 
} 
}  catch  (  ClassNotFoundException   e  )  { 
logger  .  log  (  Level  .  WARNING  ,  "Cannot create implementation of interface: "  +  interfaceName  ,  e  )  ; 
} 
} 
if  (  res  .  size  (  )  ==  0  )  { 
throw   new   ClassNotFoundException  (  "Cannot find any implemnetation of class "  +  interfaceName  )  ; 
} 
return   res  ; 
} 








public   List   getAllStemmers  (  final   Locale   locale  )  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'locale' argument cannot be null."  )  ; 
} 
List   result  =  (  List  )  stemmers  .  get  (  locale  )  ; 
if  (  result  ==  null  )  { 
result  =  Collections  .  EMPTY_LIST  ; 
} 
return   result  ; 
} 












public   Stemmer   getStemmer  (  final   Locale   locale  ,  final   String   propertyName  ,  final   Object   propertyValue  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
if  (  propertyName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'propertyName' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  stemmers  ,  locale  ,  1  ,  1  ,  propertyName  ,  propertyValue  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find stemmer for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  Stemmer  )  component  ; 
} 
} 










public   Stemmer   getMostAccurateStemmer  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
Stemmer   component  =  (  Stemmer  )  mostAccurateStemmers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find stemmer for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 










public   Stemmer   getFastestStemmer  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
Stemmer   component  =  (  Stemmer  )  fastestStemmers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find stemmer for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 














public   Stemmer   getStemmer  (  final   Locale   locale  ,  final   double   qualityImportance  ,  final   double   speedImportance  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  stemmers  ,  locale  ,  qualityImportance  ,  speedImportance  ,  null  ,  null  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find stemmer for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  Stemmer  )  component  ; 
} 
} 








public   List   getAllLemmatizers  (  final   Locale   locale  )  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'locale' argument cannot be null."  )  ; 
} 
List   result  =  (  List  )  lemmatizers  .  get  (  locale  )  ; 
if  (  result  ==  null  )  { 
result  =  Collections  .  EMPTY_LIST  ; 
} 
return   result  ; 
} 












public   Lemmatizer   getLemmatizer  (  final   Locale   locale  ,  final   String   propertyName  ,  final   Object   propertyValue  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
if  (  propertyName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'propertyName' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  lemmatizers  ,  locale  ,  1  ,  1  ,  propertyName  ,  propertyValue  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find lemmatizer for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  Lemmatizer  )  component  ; 
} 
} 










public   Lemmatizer   getMostAccurateLemmatizer  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
Lemmatizer   component  =  (  Lemmatizer  )  mostAccurateLemmatizers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find lemmatizer for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 










public   Lemmatizer   getFastestLemmatizer  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
Lemmatizer   component  =  (  Lemmatizer  )  fastestLemmatizers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find lemmatizer for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 














public   Lemmatizer   getLemmatizer  (  final   Locale   locale  ,  final   double   qualityImportance  ,  final   double   speedImportance  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  lemmatizers  ,  locale  ,  qualityImportance  ,  speedImportance  ,  null  ,  null  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find lemmatizer for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  Lemmatizer  )  component  ; 
} 
} 








public   List   getAllMorphologicalAnalysers  (  final   Locale   locale  )  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'locale' argument cannot be null."  )  ; 
} 
List   result  =  (  List  )  analysiers  .  get  (  locale  )  ; 
if  (  result  ==  null  )  { 
result  =  Collections  .  EMPTY_LIST  ; 
} 
return   result  ; 
} 













public   MorphologicalAnalyser   getMorphologicalAnalyser  (  final   Locale   locale  ,  final   String   propertyName  ,  final   Object   propertyValue  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
if  (  propertyName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'propertyName' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  analysiers  ,  locale  ,  1  ,  1  ,  propertyName  ,  propertyValue  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find morphological analyser for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  MorphologicalAnalyser  )  component  ; 
} 
} 











public   MorphologicalAnalyser   getMostAccurateMorphologicalAnalyser  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalAnalyser   component  =  (  MorphologicalAnalyser  )  mostAccurateAnalysiers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find morphological analyser for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 











public   MorphologicalAnalyser   getFastestMorphologicalAnalyser  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalAnalyser   component  =  (  MorphologicalAnalyser  )  fastestAnalysiers  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find morphological analyser for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 















public   MorphologicalAnalyser   getMorphologicalAnalyser  (  final   Locale   locale  ,  final   double   qualityImportance  ,  final   double   speedImportance  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  analysiers  ,  locale  ,  qualityImportance  ,  speedImportance  ,  null  ,  null  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find morphological analyser for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  MorphologicalAnalyser  )  component  ; 
} 
} 








public   List   getAllGenerators  (  final   Locale   locale  )  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'locale' argument cannot be null."  )  ; 
} 
List   result  =  (  List  )  generators  .  get  (  locale  )  ; 
if  (  result  ==  null  )  { 
result  =  Collections  .  EMPTY_LIST  ; 
} 
return   result  ; 
} 












public   WordFormGenerator   getGenerator  (  final   Locale   locale  ,  final   String   propertyName  ,  final   Object   propertyValue  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
if  (  propertyName  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'propertyName' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  generators  ,  locale  ,  1  ,  1  ,  propertyName  ,  propertyValue  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find generator for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  WordFormGenerator  )  component  ; 
} 
} 










public   WordFormGenerator   getMostAccurateGenerator  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
WordFormGenerator   component  =  (  WordFormGenerator  )  mostAccurateGenerators  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find generator for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 










public   WordFormGenerator   getFastestGenerator  (  final   Locale   locale  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
WordFormGenerator   component  =  (  WordFormGenerator  )  fastestGenerators  .  get  (  locale  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find generator for locales : "  +  locale  .  toString  (  )  )  ; 
} 
return   component  ; 
} 














public   WordFormGenerator   getGenerator  (  final   Locale   locale  ,  final   double   qualityImportance  ,  final   double   speedImportance  )  throws   MorphologyException  { 
if  (  locale  ==  null  )  { 
throw   new   IllegalArgumentException  (  "The 'loc' argument cannot be null."  )  ; 
} 
MorphologicalComponent   component  =  findComponent  (  generators  ,  locale  ,  qualityImportance  ,  speedImportance  ,  null  ,  null  )  ; 
if  (  component  ==  null  )  { 
throw   new   MorphologyException  (  "Cannot find generator for locales : "  +  locale  .  toString  (  )  )  ; 
}  else  { 
return  (  WordFormGenerator  )  component  ; 
} 
} 






public   Tagset   createTagset  (  )  { 
return   new   TagsetImpl  (  )  ; 
} 
} 

