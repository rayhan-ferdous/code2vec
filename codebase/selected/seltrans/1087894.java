package   edu  .  cmu  .  sphinx  .  model  .  acoustic  .  plpsat50blF0M  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  LinkedHashMap  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  zip  .  ZipException  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  Context  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  HMM  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  HMMPosition  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  LeftRightContext  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  Unit  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  UnitManager  ; 
import   edu  .  cmu  .  sphinx  .  linguist  .  acoustic  .  tiedstate  .  *  ; 
import   edu  .  cmu  .  sphinx  .  util  .  ExtendedStreamTokenizer  ; 
import   edu  .  cmu  .  sphinx  .  util  .  LogMath  ; 
import   edu  .  cmu  .  sphinx  .  util  .  SphinxProperties  ; 
import   edu  .  cmu  .  sphinx  .  util  .  StreamFactory  ; 
import   edu  .  cmu  .  sphinx  .  util  .  Utilities  ; 
import   edu  .  cmu  .  sphinx  .  util  .  props  .  PropertyException  ; 
import   edu  .  cmu  .  sphinx  .  util  .  props  .  PropertySheet  ; 
import   edu  .  cmu  .  sphinx  .  util  .  props  .  PropertyType  ; 
import   edu  .  cmu  .  sphinx  .  util  .  props  .  Registry  ; 



































































































public   class   ModelLoader   implements   Loader  { 




public   static   final   String   PROP_LOG_MATH  =  "logMath"  ; 




public   static   final   String   PROP_UNIT_MANAGER  =  "unitManager"  ; 




public   static   final   String   PROP_IS_BINARY  =  "isBinary"  ; 




public   static   final   boolean   PROP_IS_BINARY_DEFAULT  =  true  ; 




public   static   final   String   PROP_MODEL  =  "modelDefinition"  ; 




public   static   final   String   PROP_MODEL_DEFAULT  =  "model.mdef"  ; 




public   static   final   String   PROP_DATA_LOCATION  =  "dataLocation"  ; 




public   static   final   String   PROP_DATA_LOCATION_DEFAULT  =  "data"  ; 




public   static   final   String   PROP_PROPERTIES_FILE  =  "propertiesFile"  ; 




public   static   final   String   PROP_PROPERTIES_FILE_DEFAULT  =  "model.props"  ; 




public   static   final   String   PROP_VECTOR_LENGTH  =  "vectorLength"  ; 




public   static   final   int   PROP_VECTOR_LENGTH_DEFAULT  =  39  ; 






public   static   final   String   PROP_SPARSE_FORM  =  "sparseForm"  ; 




public   static   final   boolean   PROP_SPARSE_FORM_DEFAULT  =  true  ; 





public   static   final   String   PROP_USE_CD_UNITS  =  "useCDUnits"  ; 




public   static   final   boolean   PROP_USE_CD_UNITS_DEFAULT  =  true  ; 




public   static   final   String   PROP_MC_FLOOR  =  "MixtureComponentScoreFloor"  ; 




public   static   final   float   PROP_MC_FLOOR_DEFAULT  =  0.0f  ; 




public   static   final   String   PROP_VARIANCE_FLOOR  =  "varianceFloor"  ; 




public   static   final   float   PROP_VARIANCE_FLOOR_DEFAULT  =  0.0001f  ; 




public   static   final   String   PROP_MW_FLOOR  =  "mixtureWeightFloor"  ; 




public   static   final   float   PROP_MW_FLOOR_DEFAULT  =  1e-7f  ; 

protected   static   final   String   NUM_SENONES  =  "num_senones"  ; 

protected   static   final   String   NUM_GAUSSIANS_PER_STATE  =  "num_gaussians"  ; 

protected   static   final   String   NUM_STREAMS  =  "num_streams"  ; 

protected   static   final   String   FILLER  =  "filler"  ; 

protected   static   final   String   SILENCE_CIPHONE  =  "SIL"  ; 

protected   static   final   int   BYTE_ORDER_MAGIC  =  0x11223344  ; 




public   static   final   String   MODEL_VERSION  =  "0.3"  ; 

protected   static   final   int   CONTEXT_SIZE  =  1  ; 

private   Pool   meansPool  ; 

private   Pool   variancePool  ; 

private   Pool   matrixPool  ; 

private   Pool   meanTransformationMatrixPool  ; 

private   Pool   meanTransformationVectorPool  ; 

private   Pool   varianceTransformationMatrixPool  ; 

private   Pool   varianceTransformationVectorPool  ; 

private   Pool   mixtureWeightsPool  ; 

private   Pool   senonePool  ; 

private   Map   contextIndependentUnits  ; 

private   HMMManager   hmmManager  ; 

private   LogMath   logMath  ; 

private   UnitManager   unitManager  ; 

private   Properties   properties  ; 

private   boolean   swap  ; 

protected   static   final   String   DENSITY_FILE_VERSION  =  "1.0"  ; 

protected   static   final   String   MIXW_FILE_VERSION  =  "1.0"  ; 

protected   static   final   String   TMAT_FILE_VERSION  =  "1.0"  ; 

private   String   name  ; 

private   Logger   logger  ; 

private   boolean   binary  ; 

private   boolean   sparseForm  ; 

private   int   vectorLength  ; 

private   String   location  ; 

private   String   model  ; 

private   String   dataDir  ; 

private   String   propsFile  ; 

private   float   distFloor  ; 

private   float   mixtureWeightFloor  ; 

private   float   varianceFloor  ; 

private   boolean   useCDUnits  ; 

public   void   register  (  String   name  ,  Registry   registry  )  throws   PropertyException  { 
this  .  name  =  name  ; 
registry  .  register  (  PROP_LOG_MATH  ,  PropertyType  .  COMPONENT  )  ; 
registry  .  register  (  PROP_UNIT_MANAGER  ,  PropertyType  .  COMPONENT  )  ; 
registry  .  register  (  PROP_IS_BINARY  ,  PropertyType  .  BOOLEAN  )  ; 
registry  .  register  (  PROP_SPARSE_FORM  ,  PropertyType  .  BOOLEAN  )  ; 
registry  .  register  (  PROP_VECTOR_LENGTH  ,  PropertyType  .  INT  )  ; 
registry  .  register  (  PROP_MODEL  ,  PropertyType  .  STRING  )  ; 
registry  .  register  (  PROP_DATA_LOCATION  ,  PropertyType  .  STRING  )  ; 
registry  .  register  (  PROP_PROPERTIES_FILE  ,  PropertyType  .  STRING  )  ; 
registry  .  register  (  PROP_MC_FLOOR  ,  PropertyType  .  FLOAT  )  ; 
registry  .  register  (  PROP_MW_FLOOR  ,  PropertyType  .  FLOAT  )  ; 
registry  .  register  (  PROP_VARIANCE_FLOOR  ,  PropertyType  .  FLOAT  )  ; 
registry  .  register  (  PROP_USE_CD_UNITS  ,  PropertyType  .  BOOLEAN  )  ; 
} 

public   void   newProperties  (  PropertySheet   ps  )  throws   PropertyException  { 
logger  =  ps  .  getLogger  (  )  ; 
propsFile  =  ps  .  getString  (  PROP_PROPERTIES_FILE  ,  PROP_PROPERTIES_FILE_DEFAULT  )  ; 
logMath  =  (  LogMath  )  ps  .  getComponent  (  PROP_LOG_MATH  ,  LogMath  .  class  )  ; 
unitManager  =  (  UnitManager  )  ps  .  getComponent  (  PROP_UNIT_MANAGER  ,  UnitManager  .  class  )  ; 
binary  =  ps  .  getBoolean  (  PROP_IS_BINARY  ,  getIsBinaryDefault  (  )  )  ; 
sparseForm  =  ps  .  getBoolean  (  PROP_SPARSE_FORM  ,  getSparseFormDefault  (  )  )  ; 
vectorLength  =  ps  .  getInt  (  PROP_VECTOR_LENGTH  ,  getVectorLengthDefault  (  )  )  ; 
model  =  ps  .  getString  (  PROP_MODEL  ,  getModelDefault  (  )  )  ; 
dataDir  =  ps  .  getString  (  PROP_DATA_LOCATION  ,  getDataLocationDefault  (  )  )  +  "/"  ; 
distFloor  =  ps  .  getFloat  (  PROP_MC_FLOOR  ,  PROP_MC_FLOOR_DEFAULT  )  ; 
mixtureWeightFloor  =  ps  .  getFloat  (  PROP_MW_FLOOR  ,  PROP_MW_FLOOR_DEFAULT  )  ; 
varianceFloor  =  ps  .  getFloat  (  PROP_VARIANCE_FLOOR  ,  PROP_VARIANCE_FLOOR_DEFAULT  )  ; 
useCDUnits  =  ps  .  getBoolean  (  PROP_USE_CD_UNITS  ,  PROP_USE_CD_UNITS_DEFAULT  )  ; 
} 

private   void   loadProperties  (  )  { 
if  (  properties  ==  null  )  { 
properties  =  new   Properties  (  )  ; 
try  { 
URL   url  =  getClass  (  )  .  getResource  (  propsFile  )  ; 
properties  .  load  (  url  .  openStream  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
} 






private   boolean   getIsBinaryDefault  (  )  { 
loadProperties  (  )  ; 
String   binary  =  (  String  )  properties  .  get  (  PROP_IS_BINARY  )  ; 
if  (  binary  !=  null  )  { 
return  (  Boolean  .  valueOf  (  binary  )  .  equals  (  Boolean  .  TRUE  )  )  ; 
}  else  { 
return   PROP_IS_BINARY_DEFAULT  ; 
} 
} 






private   boolean   getSparseFormDefault  (  )  { 
loadProperties  (  )  ; 
String   sparse  =  (  String  )  properties  .  get  (  PROP_SPARSE_FORM  )  ; 
if  (  sparse  !=  null  )  { 
return  (  Boolean  .  valueOf  (  binary  )  .  equals  (  Boolean  .  TRUE  )  )  ; 
}  else  { 
return   PROP_SPARSE_FORM_DEFAULT  ; 
} 
} 




private   int   getVectorLengthDefault  (  )  { 
loadProperties  (  )  ; 
String   length  =  (  String  )  properties  .  get  (  PROP_VECTOR_LENGTH  )  ; 
if  (  length  !=  null  )  { 
return   Integer  .  parseInt  (  length  )  ; 
}  else  { 
return   PROP_VECTOR_LENGTH_DEFAULT  ; 
} 
} 






private   String   getModelDefault  (  )  { 
loadProperties  (  )  ; 
String   mdef  =  (  String  )  properties  .  get  (  PROP_MODEL  )  ; 
if  (  mdef  !=  null  )  { 
return   mdef  ; 
}  else  { 
return   PROP_MODEL_DEFAULT  ; 
} 
} 






private   String   getDataLocationDefault  (  )  { 
loadProperties  (  )  ; 
String   location  =  (  String  )  properties  .  get  (  PROP_DATA_LOCATION  )  ; 
if  (  location  !=  null  )  { 
return   location  ; 
}  else  { 
return   PROP_DATA_LOCATION_DEFAULT  ; 
} 
} 

public   String   getName  (  )  { 
return   name  ; 
} 

public   void   load  (  )  throws   IOException  { 
hmmManager  =  new   HMMManager  (  )  ; 
contextIndependentUnits  =  new   LinkedHashMap  (  )  ; 
meanTransformationMatrixPool  =  createDummyMatrixPool  (  "meanTransformationMatrix"  )  ; 
meanTransformationVectorPool  =  createDummyVectorPool  (  "meanTransformationMatrix"  )  ; 
varianceTransformationMatrixPool  =  createDummyMatrixPool  (  "varianceTransformationMatrix"  )  ; 
varianceTransformationVectorPool  =  createDummyVectorPool  (  "varianceTransformationMatrix"  )  ; 
loadModelFiles  (  model  )  ; 
} 






protected   HMMManager   getHmmManager  (  )  { 
return   hmmManager  ; 
} 






protected   Pool   getMatrixPool  (  )  { 
return   matrixPool  ; 
} 






protected   Pool   getMixtureWeightsPool  (  )  { 
return   mixtureWeightsPool  ; 
} 






protected   Properties   getProperties  (  )  { 
if  (  properties  ==  null  )  { 
loadProperties  (  )  ; 
} 
return   properties  ; 
} 






protected   String   getLocation  (  )  { 
return   location  ; 
} 








private   void   loadModelFiles  (  String   modelName  )  throws   FileNotFoundException  ,  IOException  ,  ZipException  { 
logger  .  config  (  "Loading Sphinx3 acoustic model: "  +  modelName  )  ; 
logger  .  config  (  "    Path      : "  +  location  )  ; 
logger  .  config  (  "    modellName: "  +  model  )  ; 
logger  .  config  (  "    dataDir   : "  +  dataDir  )  ; 
if  (  binary  )  { 
meansPool  =  loadDensityFileBinary  (  dataDir  +  "means"  ,  -  Float  .  MAX_VALUE  )  ; 
variancePool  =  loadDensityFileBinary  (  dataDir  +  "variances"  ,  varianceFloor  )  ; 
mixtureWeightsPool  =  loadMixtureWeightsBinary  (  dataDir  +  "mixture_weights"  ,  mixtureWeightFloor  )  ; 
matrixPool  =  loadTransitionMatricesBinary  (  dataDir  +  "transition_matrices"  )  ; 
}  else  { 
meansPool  =  loadDensityFileAscii  (  dataDir  +  "means.ascii"  ,  -  Float  .  MAX_VALUE  )  ; 
variancePool  =  loadDensityFileAscii  (  dataDir  +  "variances.ascii"  ,  varianceFloor  )  ; 
mixtureWeightsPool  =  loadMixtureWeightsAscii  (  dataDir  +  "mixture_weights.ascii"  ,  mixtureWeightFloor  )  ; 
matrixPool  =  loadTransitionMatricesAscii  (  dataDir  +  "transition_matrices.ascii"  )  ; 
} 
senonePool  =  createSenonePool  (  distFloor  ,  varianceFloor  )  ; 
InputStream   modelStream  =  getClass  (  )  .  getResourceAsStream  (  model  )  ; 
if  (  modelStream  ==  null  )  { 
throw   new   IOException  (  "can't find model "  +  model  )  ; 
} 
loadHMMPool  (  useCDUnits  ,  modelStream  ,  location  +  File  .  separator  +  model  )  ; 
} 







public   Map   getContextIndependentUnits  (  )  { 
return   contextIndependentUnits  ; 
} 











private   Pool   createSenonePool  (  float   distFloor  ,  float   varianceFloor  )  { 
Pool   pool  =  new   Pool  (  "senones"  )  ; 
int   numMixtureWeights  =  mixtureWeightsPool  .  size  (  )  ; 
int   numMeans  =  meansPool  .  size  (  )  ; 
int   numVariances  =  variancePool  .  size  (  )  ; 
int   numGaussiansPerSenone  =  mixtureWeightsPool  .  getFeature  (  NUM_GAUSSIANS_PER_STATE  ,  0  )  ; 
int   numSenones  =  mixtureWeightsPool  .  getFeature  (  NUM_SENONES  ,  0  )  ; 
int   whichGaussian  =  0  ; 
logger  .  fine  (  "NG "  +  numGaussiansPerSenone  )  ; 
logger  .  fine  (  "NS "  +  numSenones  )  ; 
logger  .  fine  (  "NMIX "  +  numMixtureWeights  )  ; 
logger  .  fine  (  "NMNS "  +  numMeans  )  ; 
logger  .  fine  (  "NMNS "  +  numVariances  )  ; 
assert   numGaussiansPerSenone  >  0  ; 
assert   numMixtureWeights  ==  numSenones  ; 
assert   numVariances  ==  numSenones  *  numGaussiansPerSenone  ; 
assert   numMeans  ==  numSenones  *  numGaussiansPerSenone  ; 
for  (  int   i  =  0  ;  i  <  numSenones  ;  i  ++  )  { 
MixtureComponent  [  ]  mixtureComponents  =  new   MixtureComponent  [  numGaussiansPerSenone  ]  ; 
for  (  int   j  =  0  ;  j  <  numGaussiansPerSenone  ;  j  ++  )  { 
mixtureComponents  [  j  ]  =  new   MixtureComponent  (  logMath  ,  (  float  [  ]  )  meansPool  .  get  (  whichGaussian  )  ,  (  float  [  ]  [  ]  )  meanTransformationMatrixPool  .  get  (  0  )  ,  (  float  [  ]  )  meanTransformationVectorPool  .  get  (  0  )  ,  (  float  [  ]  )  variancePool  .  get  (  whichGaussian  )  ,  (  float  [  ]  [  ]  )  varianceTransformationMatrixPool  .  get  (  0  )  ,  (  float  [  ]  )  varianceTransformationVectorPool  .  get  (  0  )  ,  distFloor  ,  varianceFloor  )  ; 
whichGaussian  ++  ; 
} 
Senone   senone  =  new   GaussianMixture  (  logMath  ,  (  float  [  ]  )  mixtureWeightsPool  .  get  (  i  )  ,  mixtureComponents  ,  i  )  ; 
pool  .  put  (  i  ,  senone  )  ; 
} 
return   pool  ; 
} 
















private   SphinxProperties   loadAcousticPropertiesFile  (  URL   url  )  throws   FileNotFoundException  ,  IOException  { 
String   context  =  "acoustic."  +  getName  (  )  +  "."  +  url  ; 
SphinxProperties  .  initContext  (  context  ,  url  )  ; 
return  (  SphinxProperties  .  getSphinxProperties  (  context  )  )  ; 
} 

















private   Pool   loadDensityFileAscii  (  String   path  ,  float   floor  )  throws   FileNotFoundException  ,  IOException  { 
int   token_type  ; 
int   numStates  ; 
int   numStreams  ; 
int   numGaussiansPerState  ; 
InputStream   inputStream  =  getClass  (  )  .  getResourceAsStream  (  path  )  ; 
if  (  inputStream  ==  null  )  { 
throw   new   FileNotFoundException  (  "Error trying to read file "  +  location  +  path  )  ; 
} 
ExtendedStreamTokenizer   est  =  new   ExtendedStreamTokenizer  (  inputStream  ,  '#'  ,  false  )  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
logger  .  fine  (  "Loading density file from: "  +  path  )  ; 
est  .  expectString  (  "param"  )  ; 
numStates  =  est  .  getInt  (  "numStates"  )  ; 
numStreams  =  est  .  getInt  (  "numStreams"  )  ; 
numGaussiansPerState  =  est  .  getInt  (  "numGaussiansPerState"  )  ; 
pool  .  setFeature  (  NUM_SENONES  ,  numStates  )  ; 
pool  .  setFeature  (  NUM_STREAMS  ,  numStreams  )  ; 
pool  .  setFeature  (  NUM_GAUSSIANS_PER_STATE  ,  numGaussiansPerState  )  ; 
for  (  int   i  =  0  ;  i  <  numStates  ;  i  ++  )  { 
est  .  expectString  (  "mgau"  )  ; 
est  .  expectInt  (  "mgau index"  ,  i  )  ; 
est  .  expectString  (  "feat"  )  ; 
est  .  expectInt  (  "feat index"  ,  0  )  ; 
for  (  int   j  =  0  ;  j  <  numGaussiansPerState  ;  j  ++  )  { 
est  .  expectString  (  "density"  )  ; 
est  .  expectInt  (  "densityValue"  ,  j  )  ; 
float  [  ]  density  =  new   float  [  vectorLength  ]  ; 
for  (  int   k  =  0  ;  k  <  vectorLength  ;  k  ++  )  { 
density  [  k  ]  =  est  .  getFloat  (  "val"  )  ; 
if  (  density  [  k  ]  <  floor  )  { 
density  [  k  ]  =  floor  ; 
} 
} 
int   id  =  i  *  numGaussiansPerState  +  j  ; 
pool  .  put  (  id  ,  density  )  ; 
} 
} 
est  .  close  (  )  ; 
return   pool  ; 
} 

















private   Pool   loadDensityFileBinary  (  String   path  ,  float   floor  )  throws   FileNotFoundException  ,  IOException  { 
int   token_type  ; 
int   numStates  ; 
int   numStreams  ; 
int   numGaussiansPerState  ; 
Properties   props  =  new   Properties  (  )  ; 
int   blockSize  =  0  ; 
DataInputStream   dis  =  readS3BinaryHeader  (  location  ,  path  ,  props  )  ; 
String   version  =  props  .  getProperty  (  "version"  )  ; 
boolean   doCheckSum  ; 
if  (  version  ==  null  ||  !  version  .  equals  (  DENSITY_FILE_VERSION  )  )  { 
throw   new   IOException  (  "Unsupported version in "  +  path  )  ; 
} 
String   checksum  =  props  .  getProperty  (  "chksum0"  )  ; 
doCheckSum  =  (  checksum  !=  null  &&  checksum  .  equals  (  "yes"  )  )  ; 
numStates  =  readInt  (  dis  )  ; 
numStreams  =  readInt  (  dis  )  ; 
numGaussiansPerState  =  readInt  (  dis  )  ; 
int  [  ]  vectorLength  =  new   int  [  numStreams  ]  ; 
for  (  int   i  =  0  ;  i  <  numStreams  ;  i  ++  )  { 
vectorLength  [  i  ]  =  readInt  (  dis  )  ; 
} 
int   rawLength  =  readInt  (  dis  )  ; 
logger  .  fine  (  "Nstates "  +  numStates  )  ; 
logger  .  fine  (  "Nstreams "  +  numStreams  )  ; 
logger  .  fine  (  "NgaussiansPerState "  +  numGaussiansPerState  )  ; 
logger  .  fine  (  "vectorLength "  +  vectorLength  .  length  )  ; 
logger  .  fine  (  "rawLength "  +  rawLength  )  ; 
for  (  int   i  =  0  ;  i  <  numStreams  ;  i  ++  )  { 
blockSize  +=  vectorLength  [  i  ]  ; 
} 
assert   rawLength  ==  numGaussiansPerState  *  blockSize  *  numStates  ; 
assert   numStreams  ==  1  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
pool  .  setFeature  (  NUM_SENONES  ,  numStates  )  ; 
pool  .  setFeature  (  NUM_STREAMS  ,  numStreams  )  ; 
pool  .  setFeature  (  NUM_GAUSSIANS_PER_STATE  ,  numGaussiansPerState  )  ; 
int   r  =  0  ; 
for  (  int   i  =  0  ;  i  <  numStates  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  numStreams  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  numGaussiansPerState  ;  k  ++  )  { 
float  [  ]  density  =  readFloatArray  (  dis  ,  vectorLength  [  j  ]  )  ; 
floorData  (  density  ,  floor  )  ; 
pool  .  put  (  i  *  numGaussiansPerState  +  k  ,  density  )  ; 
} 
} 
} 
int   checkSum  =  readInt  (  dis  )  ; 
dis  .  close  (  )  ; 
return   pool  ; 
} 

















protected   DataInputStream   readS3BinaryHeader  (  String   location  ,  String   path  ,  Properties   props  )  throws   IOException  { 
InputStream   inputStream  =  getClass  (  )  .  getResourceAsStream  (  path  )  ; 
if  (  inputStream  ==  null  )  { 
throw   new   IOException  (  "Can't open "  +  path  )  ; 
} 
DataInputStream   dis  =  new   DataInputStream  (  new   BufferedInputStream  (  inputStream  )  )  ; 
String   id  =  readWord  (  dis  )  ; 
if  (  !  id  .  equals  (  "s3"  )  )  { 
throw   new   IOException  (  "Not proper s3 binary file "  +  location  +  path  )  ; 
} 
String   name  ; 
while  (  (  name  =  readWord  (  dis  )  )  !=  null  )  { 
if  (  !  name  .  equals  (  "endhdr"  )  )  { 
String   value  =  readWord  (  dis  )  ; 
props  .  setProperty  (  name  ,  value  )  ; 
}  else  { 
break  ; 
} 
} 
int   byteOrderMagic  =  dis  .  readInt  (  )  ; 
if  (  byteOrderMagic  ==  BYTE_ORDER_MAGIC  )  { 
swap  =  false  ; 
}  else   if  (  byteSwap  (  byteOrderMagic  )  ==  BYTE_ORDER_MAGIC  )  { 
swap  =  true  ; 
}  else  { 
throw   new   IOException  (  "Corrupt S3 file "  +  location  +  path  )  ; 
} 
return   dis  ; 
} 












String   readWord  (  DataInputStream   dis  )  throws   IOException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
char   c  ; 
do  { 
c  =  readChar  (  dis  )  ; 
}  while  (  Character  .  isWhitespace  (  c  )  )  ; 
do  { 
sb  .  append  (  c  )  ; 
c  =  readChar  (  dis  )  ; 
}  while  (  !  Character  .  isWhitespace  (  c  )  )  ; 
return   sb  .  toString  (  )  ; 
} 











private   char   readChar  (  DataInputStream   dis  )  throws   IOException  { 
return  (  char  )  dis  .  readByte  (  )  ; 
} 









private   int   byteSwap  (  int   val  )  { 
return  (  (  0xff  &  (  val  >  >  24  )  )  |  (  0xff00  &  (  val  >  >  8  )  )  |  (  0xff0000  &  (  val  <<  8  )  )  |  (  0xff000000  &  (  val  <<  24  )  )  )  ; 
} 












protected   int   readInt  (  DataInputStream   dis  )  throws   IOException  { 
if  (  swap  )  { 
return   Utilities  .  readLittleEndianInt  (  dis  )  ; 
}  else  { 
return   dis  .  readInt  (  )  ; 
} 
} 












protected   float   readFloat  (  DataInputStream   dis  )  throws   IOException  { 
float   val  ; 
if  (  swap  )  { 
val  =  Utilities  .  readLittleEndianFloat  (  dis  )  ; 
}  else  { 
val  =  dis  .  readFloat  (  )  ; 
} 
return   val  ; 
} 










protected   void   nonZeroFloor  (  float  [  ]  data  ,  float   floor  )  { 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
if  (  data  [  i  ]  !=  0.0  &&  data  [  i  ]  <  floor  )  { 
data  [  i  ]  =  floor  ; 
} 
} 
} 









private   void   floorData  (  float  [  ]  data  ,  float   floor  )  { 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
if  (  data  [  i  ]  <  floor  )  { 
data  [  i  ]  =  floor  ; 
} 
} 
} 







protected   void   normalize  (  float  [  ]  data  )  { 
float   sum  =  0  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
sum  +=  data  [  i  ]  ; 
} 
if  (  sum  !=  0.0f  )  { 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
data  [  i  ]  =  data  [  i  ]  /  sum  ; 
} 
} 
} 










private   void   dumpData  (  String   name  ,  float  [  ]  data  )  { 
System  .  out  .  println  (  " ----- "  +  name  +  " -----------"  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  name  +  " "  +  i  +  ": "  +  data  [  i  ]  )  ; 
} 
} 

protected   void   convertToLogMath  (  float  [  ]  data  )  { 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
data  [  i  ]  =  logMath  .  linearToLog  (  data  [  i  ]  )  ; 
} 
} 















protected   float  [  ]  readFloatArray  (  DataInputStream   dis  ,  int   size  )  throws   IOException  { 
float  [  ]  data  =  new   float  [  size  ]  ; 
for  (  int   i  =  0  ;  i  <  size  ;  i  ++  )  { 
data  [  i  ]  =  readFloat  (  dis  )  ; 
} 
return   data  ; 
} 



















protected   Pool   loadHMMPool  (  boolean   useCDUnits  ,  InputStream   inputStream  ,  String   path  )  throws   FileNotFoundException  ,  IOException  { 
int   token_type  ; 
int   numBase  ; 
int   numTri  ; 
int   numStateMap  ; 
int   numTiedState  ; 
int   numStatePerHMM  ; 
int   numContextIndependentTiedState  ; 
int   numTiedTransitionMatrices  ; 
ExtendedStreamTokenizer   est  =  new   ExtendedStreamTokenizer  (  inputStream  ,  '#'  ,  false  )  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
logger  .  fine  (  "Loading HMM file from: "  +  path  )  ; 
est  .  expectString  (  MODEL_VERSION  )  ; 
numBase  =  est  .  getInt  (  "numBase"  )  ; 
est  .  expectString  (  "n_base"  )  ; 
numTri  =  est  .  getInt  (  "numTri"  )  ; 
est  .  expectString  (  "n_tri"  )  ; 
numStateMap  =  est  .  getInt  (  "numStateMap"  )  ; 
est  .  expectString  (  "n_state_map"  )  ; 
numTiedState  =  est  .  getInt  (  "numTiedState"  )  ; 
est  .  expectString  (  "n_tied_state"  )  ; 
numContextIndependentTiedState  =  est  .  getInt  (  "numContextIndependentTiedState"  )  ; 
est  .  expectString  (  "n_tied_ci_state"  )  ; 
numTiedTransitionMatrices  =  est  .  getInt  (  "numTiedTransitionMatrices"  )  ; 
est  .  expectString  (  "n_tied_tmat"  )  ; 
numStatePerHMM  =  numStateMap  /  (  numTri  +  numBase  )  ; 
assert   numTiedState  ==  mixtureWeightsPool  .  getFeature  (  NUM_SENONES  ,  0  )  ; 
assert   numTiedTransitionMatrices  ==  matrixPool  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  numBase  ;  i  ++  )  { 
String   name  =  est  .  getString  (  )  ; 
String   left  =  est  .  getString  (  )  ; 
String   right  =  est  .  getString  (  )  ; 
String   position  =  est  .  getString  (  )  ; 
String   attribute  =  est  .  getString  (  )  ; 
int   tmat  =  est  .  getInt  (  "tmat"  )  ; 
int  [  ]  stid  =  new   int  [  numStatePerHMM  -  1  ]  ; 
for  (  int   j  =  0  ;  j  <  numStatePerHMM  -  1  ;  j  ++  )  { 
stid  [  j  ]  =  est  .  getInt  (  "j"  )  ; 
assert   stid  [  j  ]  >=  0  &&  stid  [  j  ]  <  numContextIndependentTiedState  ; 
} 
est  .  expectString  (  "N"  )  ; 
assert   left  .  equals  (  "-"  )  ; 
assert   right  .  equals  (  "-"  )  ; 
assert   position  .  equals  (  "-"  )  ; 
assert   tmat  <  numTiedTransitionMatrices  ; 
Unit   unit  =  unitManager  .  getUnit  (  name  ,  attribute  .  equals  (  FILLER  )  )  ; 
contextIndependentUnits  .  put  (  unit  .  getName  (  )  ,  unit  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  fine  (  "Loaded "  +  unit  )  ; 
} 
if  (  unit  .  isFiller  (  )  &&  unit  .  getName  (  )  .  equals  (  SILENCE_CIPHONE  )  )  { 
unit  =  UnitManager  .  SILENCE  ; 
} 
float  [  ]  [  ]  transitionMatrix  =  (  float  [  ]  [  ]  )  matrixPool  .  get  (  tmat  )  ; 
SenoneSequence   ss  =  getSenoneSequence  (  stid  )  ; 
HMM   hmm  =  new   SenoneHMM  (  unit  ,  ss  ,  transitionMatrix  ,  HMMPosition  .  lookup  (  position  )  )  ; 
hmmManager  .  put  (  hmm  )  ; 
} 
String   lastUnitName  =  ""  ; 
Unit   lastUnit  =  null  ; 
int  [  ]  lastStid  =  null  ; 
SenoneSequence   lastSenoneSequence  =  null  ; 
for  (  int   i  =  0  ;  i  <  numTri  ;  i  ++  )  { 
String   name  =  est  .  getString  (  )  ; 
String   left  =  est  .  getString  (  )  ; 
String   right  =  est  .  getString  (  )  ; 
String   position  =  est  .  getString  (  )  ; 
String   attribute  =  est  .  getString  (  )  ; 
int   tmat  =  est  .  getInt  (  "tmat"  )  ; 
int  [  ]  stid  =  new   int  [  numStatePerHMM  -  1  ]  ; 
for  (  int   j  =  0  ;  j  <  numStatePerHMM  -  1  ;  j  ++  )  { 
stid  [  j  ]  =  est  .  getInt  (  "j"  )  ; 
assert   stid  [  j  ]  >=  numContextIndependentTiedState  &&  stid  [  j  ]  <  numTiedState  ; 
} 
est  .  expectString  (  "N"  )  ; 
assert  !  left  .  equals  (  "-"  )  ; 
assert  !  right  .  equals  (  "-"  )  ; 
assert  !  position  .  equals  (  "-"  )  ; 
assert   attribute  .  equals  (  "n/a"  )  ; 
assert   tmat  <  numTiedTransitionMatrices  ; 
if  (  useCDUnits  )  { 
Unit   unit  =  null  ; 
String   unitName  =  (  name  +  " "  +  left  +  " "  +  right  )  ; 
if  (  unitName  .  equals  (  lastUnitName  )  )  { 
unit  =  lastUnit  ; 
}  else  { 
Unit  [  ]  leftContext  =  new   Unit  [  1  ]  ; 
leftContext  [  0  ]  =  (  Unit  )  contextIndependentUnits  .  get  (  left  )  ; 
Unit  [  ]  rightContext  =  new   Unit  [  1  ]  ; 
rightContext  [  0  ]  =  (  Unit  )  contextIndependentUnits  .  get  (  right  )  ; 
Context   context  =  LeftRightContext  .  get  (  leftContext  ,  rightContext  )  ; 
unit  =  unitManager  .  getUnit  (  name  ,  false  ,  context  )  ; 
} 
lastUnitName  =  unitName  ; 
lastUnit  =  unit  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  fine  (  "Loaded "  +  unit  )  ; 
} 
float  [  ]  [  ]  transitionMatrix  =  (  float  [  ]  [  ]  )  matrixPool  .  get  (  tmat  )  ; 
SenoneSequence   ss  =  lastSenoneSequence  ; 
if  (  ss  ==  null  ||  !  sameSenoneSequence  (  stid  ,  lastStid  )  )  { 
ss  =  getSenoneSequence  (  stid  )  ; 
} 
lastSenoneSequence  =  ss  ; 
lastStid  =  stid  ; 
HMM   hmm  =  new   SenoneHMM  (  unit  ,  ss  ,  transitionMatrix  ,  HMMPosition  .  lookup  (  position  )  )  ; 
hmmManager  .  put  (  hmm  )  ; 
} 
} 
est  .  close  (  )  ; 
return   pool  ; 
} 







protected   boolean   sameSenoneSequence  (  int  [  ]  ssid1  ,  int  [  ]  ssid2  )  { 
if  (  ssid1  .  length  ==  ssid2  .  length  )  { 
for  (  int   i  =  0  ;  i  <  ssid1  .  length  ;  i  ++  )  { 
if  (  ssid1  [  i  ]  !=  ssid2  [  i  ]  )  { 
return   false  ; 
} 
} 
return   true  ; 
}  else  { 
return   false  ; 
} 
} 









protected   SenoneSequence   getSenoneSequence  (  int  [  ]  stateid  )  { 
Senone  [  ]  senones  =  new   Senone  [  stateid  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  stateid  .  length  ;  i  ++  )  { 
senones  [  i  ]  =  (  Senone  )  senonePool  .  get  (  stateid  [  i  ]  )  ; 
} 
return   new   SenoneSequence  (  senones  )  ; 
} 
















private   Pool   loadMixtureWeightsAscii  (  String   path  ,  float   floor  )  throws   FileNotFoundException  ,  IOException  { 
logger  .  fine  (  "Loading mixture weights from: "  +  path  )  ; 
int   numStates  ; 
int   numStreams  ; 
int   numGaussiansPerState  ; 
InputStream   inputStream  =  StreamFactory  .  getInputStream  (  location  ,  path  )  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
ExtendedStreamTokenizer   est  =  new   ExtendedStreamTokenizer  (  inputStream  ,  '#'  ,  false  )  ; 
est  .  expectString  (  "mixw"  )  ; 
numStates  =  est  .  getInt  (  "numStates"  )  ; 
numStreams  =  est  .  getInt  (  "numStreams"  )  ; 
numGaussiansPerState  =  est  .  getInt  (  "numGaussiansPerState"  )  ; 
pool  .  setFeature  (  NUM_SENONES  ,  numStates  )  ; 
pool  .  setFeature  (  NUM_STREAMS  ,  numStreams  )  ; 
pool  .  setFeature  (  NUM_GAUSSIANS_PER_STATE  ,  numGaussiansPerState  )  ; 
for  (  int   i  =  0  ;  i  <  numStates  ;  i  ++  )  { 
est  .  expectString  (  "mixw"  )  ; 
est  .  expectString  (  "["  +  i  )  ; 
est  .  expectString  (  "0]"  )  ; 
float   total  =  est  .  getFloat  (  "total"  )  ; 
float  [  ]  logMixtureWeight  =  new   float  [  numGaussiansPerState  ]  ; 
for  (  int   j  =  0  ;  j  <  numGaussiansPerState  ;  j  ++  )  { 
float   val  =  est  .  getFloat  (  "mixwVal"  )  ; 
if  (  val  <  floor  )  { 
val  =  floor  ; 
} 
logMixtureWeight  [  j  ]  =  val  ; 
} 
convertToLogMath  (  logMixtureWeight  )  ; 
pool  .  put  (  i  ,  logMixtureWeight  )  ; 
} 
est  .  close  (  )  ; 
return   pool  ; 
} 
















private   Pool   loadMixtureWeightsBinary  (  String   path  ,  float   floor  )  throws   FileNotFoundException  ,  IOException  { 
logger  .  fine  (  "Loading mixture weights from: "  +  path  )  ; 
int   numStates  ; 
int   numStreams  ; 
int   numGaussiansPerState  ; 
int   numValues  ; 
Properties   props  =  new   Properties  (  )  ; 
DataInputStream   dis  =  readS3BinaryHeader  (  location  ,  path  ,  props  )  ; 
String   version  =  props  .  getProperty  (  "version"  )  ; 
boolean   doCheckSum  ; 
if  (  version  ==  null  ||  !  version  .  equals  (  MIXW_FILE_VERSION  )  )  { 
throw   new   IOException  (  "Unsupported version in "  +  path  )  ; 
} 
String   checksum  =  props  .  getProperty  (  "chksum0"  )  ; 
doCheckSum  =  (  checksum  !=  null  &&  checksum  .  equals  (  "yes"  )  )  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
numStates  =  readInt  (  dis  )  ; 
numStreams  =  readInt  (  dis  )  ; 
numGaussiansPerState  =  readInt  (  dis  )  ; 
numValues  =  readInt  (  dis  )  ; 
assert   numValues  ==  numStates  *  numStreams  *  numGaussiansPerState  ; 
assert   numStreams  ==  1  ; 
pool  .  setFeature  (  NUM_SENONES  ,  numStates  )  ; 
pool  .  setFeature  (  NUM_STREAMS  ,  numStreams  )  ; 
pool  .  setFeature  (  NUM_GAUSSIANS_PER_STATE  ,  numGaussiansPerState  )  ; 
for  (  int   i  =  0  ;  i  <  numStates  ;  i  ++  )  { 
float  [  ]  logMixtureWeight  =  readFloatArray  (  dis  ,  numGaussiansPerState  )  ; 
nonZeroFloor  (  logMixtureWeight  ,  floor  )  ; 
normalize  (  logMixtureWeight  )  ; 
convertToLogMath  (  logMixtureWeight  )  ; 
pool  .  put  (  i  ,  logMixtureWeight  )  ; 
} 
dis  .  close  (  )  ; 
return   pool  ; 
} 














protected   Pool   loadTransitionMatricesAscii  (  String   path  )  throws   FileNotFoundException  ,  IOException  { 
InputStream   inputStream  =  StreamFactory  .  getInputStream  (  location  ,  path  )  ; 
logger  .  fine  (  "Loading transition matrices from: "  +  path  )  ; 
int   numMatrices  ; 
int   numStates  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
ExtendedStreamTokenizer   est  =  new   ExtendedStreamTokenizer  (  inputStream  ,  '#'  ,  false  )  ; 
est  .  expectString  (  "tmat"  )  ; 
numMatrices  =  est  .  getInt  (  "numMatrices"  )  ; 
numStates  =  est  .  getInt  (  "numStates"  )  ; 
logger  .  fine  (  "with "  +  numMatrices  +  " and "  +  numStates  +  " states, in "  +  (  sparseForm  ?  "sparse"  :  "dense"  )  +  " form"  )  ; 
for  (  int   i  =  0  ;  i  <  numMatrices  ;  i  ++  )  { 
est  .  expectString  (  "tmat"  )  ; 
est  .  expectString  (  "["  +  i  +  "]"  )  ; 
float  [  ]  [  ]  tmat  =  new   float  [  numStates  ]  [  numStates  ]  ; 
for  (  int   j  =  0  ;  j  <  numStates  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  numStates  ;  k  ++  )  { 
if  (  j  <  numStates  -  1  )  { 
if  (  sparseForm  )  { 
if  (  k  ==  j  ||  k  ==  j  +  1  )  { 
tmat  [  j  ]  [  k  ]  =  est  .  getFloat  (  "tmat value"  )  ; 
} 
}  else  { 
tmat  [  j  ]  [  k  ]  =  est  .  getFloat  (  "tmat value"  )  ; 
} 
} 
tmat  [  j  ]  [  k  ]  =  logMath  .  linearToLog  (  tmat  [  j  ]  [  k  ]  )  ; 
if  (  logger  .  isLoggable  (  Level  .  FINE  )  )  { 
logger  .  fine  (  "tmat j "  +  j  +  " k "  +  k  +  " tm "  +  tmat  [  j  ]  [  k  ]  )  ; 
} 
} 
} 
pool  .  put  (  i  ,  tmat  )  ; 
} 
est  .  close  (  )  ; 
return   pool  ; 
} 














protected   Pool   loadTransitionMatricesBinary  (  String   path  )  throws   FileNotFoundException  ,  IOException  { 
logger  .  fine  (  "Loading transition matrices from: "  +  path  )  ; 
int   numMatrices  ; 
int   numStates  ; 
int   numRows  ; 
int   numValues  ; 
Properties   props  =  new   Properties  (  )  ; 
DataInputStream   dis  =  readS3BinaryHeader  (  location  ,  path  ,  props  )  ; 
String   version  =  props  .  getProperty  (  "version"  )  ; 
boolean   doCheckSum  ; 
if  (  version  ==  null  ||  !  version  .  equals  (  TMAT_FILE_VERSION  )  )  { 
throw   new   IOException  (  "Unsupported version in "  +  path  )  ; 
} 
String   checksum  =  props  .  getProperty  (  "chksum0"  )  ; 
doCheckSum  =  (  checksum  !=  null  &&  checksum  .  equals  (  "yes"  )  )  ; 
Pool   pool  =  new   Pool  (  path  )  ; 
numMatrices  =  readInt  (  dis  )  ; 
numRows  =  readInt  (  dis  )  ; 
numStates  =  readInt  (  dis  )  ; 
numValues  =  readInt  (  dis  )  ; 
assert   numValues  ==  numStates  *  numRows  *  numMatrices  ; 
for  (  int   i  =  0  ;  i  <  numMatrices  ;  i  ++  )  { 
float  [  ]  [  ]  tmat  =  new   float  [  numStates  ]  [  ]  ; 
tmat  [  numStates  -  1  ]  =  new   float  [  numStates  ]  ; 
convertToLogMath  (  tmat  [  numStates  -  1  ]  )  ; 
for  (  int   j  =  0  ;  j  <  numRows  ;  j  ++  )  { 
tmat  [  j  ]  =  readFloatArray  (  dis  ,  numStates  )  ; 
nonZeroFloor  (  tmat  [  j  ]  ,  0.00001f  )  ; 
normalize  (  tmat  [  j  ]  )  ; 
convertToLogMath  (  tmat  [  j  ]  )  ; 
} 
pool  .  put  (  i  ,  tmat  )  ; 
} 
dis  .  close  (  )  ; 
return   pool  ; 
} 









private   Pool   createDummyMatrixPool  (  String   name  )  { 
Pool   pool  =  new   Pool  (  name  )  ; 
float  [  ]  [  ]  matrix  =  new   float  [  vectorLength  ]  [  vectorLength  ]  ; 
logger  .  fine  (  "creating dummy matrix pool "  +  name  )  ; 
for  (  int   i  =  0  ;  i  <  vectorLength  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  vectorLength  ;  j  ++  )  { 
if  (  i  ==  j  )  { 
matrix  [  i  ]  [  j  ]  =  1.0F  ; 
}  else  { 
matrix  [  i  ]  [  j  ]  =  0.0F  ; 
} 
} 
} 
pool  .  put  (  0  ,  matrix  )  ; 
return   pool  ; 
} 









private   Pool   createDummyVectorPool  (  String   name  )  { 
logger  .  fine  (  "creating dummy vector pool "  +  name  )  ; 
Pool   pool  =  new   Pool  (  name  )  ; 
float  [  ]  vector  =  new   float  [  vectorLength  ]  ; 
for  (  int   i  =  0  ;  i  <  vectorLength  ;  i  ++  )  { 
vector  [  i  ]  =  0.0f  ; 
} 
pool  .  put  (  0  ,  vector  )  ; 
return   pool  ; 
} 






public   Pool   getMeansPool  (  )  { 
return   meansPool  ; 
} 






public   Pool   getMeansTransformationMatrixPool  (  )  { 
return   meanTransformationMatrixPool  ; 
} 






public   Pool   getMeansTransformationVectorPool  (  )  { 
return   meanTransformationVectorPool  ; 
} 

public   Pool   getVariancePool  (  )  { 
return   variancePool  ; 
} 






public   Pool   getVarianceTransformationMatrixPool  (  )  { 
return   varianceTransformationMatrixPool  ; 
} 






public   Pool   getVarianceTransformationVectorPool  (  )  { 
return   varianceTransformationVectorPool  ; 
} 

public   Pool   getMixtureWeightPool  (  )  { 
return   mixtureWeightsPool  ; 
} 

public   Pool   getTransitionMatrixPool  (  )  { 
return   matrixPool  ; 
} 

public   Pool   getSenonePool  (  )  { 
return   senonePool  ; 
} 






public   int   getLeftContextSize  (  )  { 
return   CONTEXT_SIZE  ; 
} 






public   int   getRightContextSize  (  )  { 
return   CONTEXT_SIZE  ; 
} 






public   HMMManager   getHMMManager  (  )  { 
return   hmmManager  ; 
} 




public   void   logInfo  (  )  { 
logger  .  info  (  "ModelLoader"  )  ; 
meansPool  .  logInfo  (  logger  )  ; 
variancePool  .  logInfo  (  logger  )  ; 
matrixPool  .  logInfo  (  logger  )  ; 
senonePool  .  logInfo  (  logger  )  ; 
meanTransformationMatrixPool  .  logInfo  (  logger  )  ; 
meanTransformationVectorPool  .  logInfo  (  logger  )  ; 
varianceTransformationMatrixPool  .  logInfo  (  logger  )  ; 
varianceTransformationVectorPool  .  logInfo  (  logger  )  ; 
mixtureWeightsPool  .  logInfo  (  logger  )  ; 
senonePool  .  logInfo  (  logger  )  ; 
logger  .  info  (  "Context Independent Unit Entries: "  +  contextIndependentUnits  .  size  (  )  )  ; 
hmmManager  .  logInfo  (  logger  )  ; 
} 
} 

