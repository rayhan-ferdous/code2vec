package   weka  .  core  .  converters  ; 

import   weka  .  core  .  Attribute  ; 
import   weka  .  core  .  FastVector  ; 
import   weka  .  core  .  Instance  ; 
import   weka  .  core  .  Instances  ; 
import   weka  .  core  .  RevisionUtils  ; 
import   weka  .  core  .  SparseInstance  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  Reader  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  Vector  ; 















public   class   LibSVMLoader   extends   AbstractFileLoader   implements   BatchConverter  ,  URLSourcedLoader  { 


private   static   final   long   serialVersionUID  =  4988360125354664417L  ; 


public   static   String   FILE_EXTENSION  =  ".libsvm"  ; 


protected   String   m_URL  =  "http://"  ; 


protected   transient   Reader   m_sourceReader  =  null  ; 


protected   Vector   m_Buffer  =  null  ; 







public   String   globalInfo  (  )  { 
return  "Reads a source that is in libsvm format.\n\n"  +  "For more information about libsvm see:\n\n"  +  "http://www.csie.ntu.edu.tw/~cjlin/libsvm/"  ; 
} 






public   String   getFileExtension  (  )  { 
return   FILE_EXTENSION  ; 
} 






public   String  [  ]  getFileExtensions  (  )  { 
return   new   String  [  ]  {  getFileExtension  (  )  }  ; 
} 






public   String   getFileDescription  (  )  { 
return  "libsvm data files"  ; 
} 






public   void   reset  (  )  throws   IOException  { 
m_structure  =  null  ; 
m_Buffer  =  null  ; 
setRetrieval  (  NONE  )  ; 
if  (  (  m_File  !=  null  )  &&  (  new   File  (  m_File  )  )  .  isFile  (  )  )  { 
setFile  (  new   File  (  m_File  )  )  ; 
}  else   if  (  (  m_URL  !=  null  )  &&  !  m_URL  .  equals  (  "http://"  )  )  { 
setURL  (  m_URL  )  ; 
} 
} 








public   void   setSource  (  URL   url  )  throws   IOException  { 
m_structure  =  null  ; 
m_Buffer  =  null  ; 
setRetrieval  (  NONE  )  ; 
setSource  (  url  .  openStream  (  )  )  ; 
m_URL  =  url  .  toString  (  )  ; 
} 







public   void   setURL  (  String   url  )  throws   IOException  { 
m_URL  =  url  ; 
setSource  (  new   URL  (  url  )  )  ; 
} 






public   String   retrieveURL  (  )  { 
return   m_URL  ; 
} 








public   void   setSource  (  InputStream   in  )  throws   IOException  { 
m_File  =  (  new   File  (  System  .  getProperty  (  "user.dir"  )  )  )  .  getAbsolutePath  (  )  ; 
m_URL  =  "http://"  ; 
m_sourceReader  =  new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
} 








protected   double  [  ]  libsvmToArray  (  String   row  )  { 
double  [  ]  result  ; 
StringTokenizer   tok  ; 
int   index  ; 
int   max  ; 
String   col  ; 
double   value  ; 
max  =  0  ; 
tok  =  new   StringTokenizer  (  row  ,  " \t"  )  ; 
tok  .  nextToken  (  )  ; 
while  (  tok  .  hasMoreTokens  (  )  )  { 
col  =  tok  .  nextToken  (  )  ; 
index  =  Integer  .  parseInt  (  col  .  substring  (  0  ,  col  .  indexOf  (  ":"  )  )  )  ; 
if  (  index  >  max  )  max  =  index  ; 
} 
tok  =  new   StringTokenizer  (  row  ,  " \t"  )  ; 
result  =  new   double  [  max  +  1  ]  ; 
result  [  result  .  length  -  1  ]  =  Double  .  parseDouble  (  tok  .  nextToken  (  )  )  ; 
while  (  tok  .  hasMoreTokens  (  )  )  { 
col  =  tok  .  nextToken  (  )  ; 
index  =  Integer  .  parseInt  (  col  .  substring  (  0  ,  col  .  indexOf  (  ":"  )  )  )  ; 
value  =  Double  .  parseDouble  (  col  .  substring  (  col  .  indexOf  (  ":"  )  +  1  )  )  ; 
result  [  index  -  1  ]  =  value  ; 
} 
return   result  ; 
} 










protected   int   determineNumAttributes  (  String   row  ,  int   num  )  { 
int   result  ; 
int   count  ; 
result  =  num  ; 
count  =  libsvmToArray  (  row  )  .  length  ; 
if  (  count  >  result  )  result  =  count  ; 
return   result  ; 
} 









public   Instances   getStructure  (  )  throws   IOException  { 
StringBuffer   line  ; 
int   cInt  ; 
char   c  ; 
int   numAtt  ; 
FastVector   atts  ; 
int   i  ; 
String   relName  ; 
if  (  m_sourceReader  ==  null  )  throw   new   IOException  (  "No source has been specified"  )  ; 
if  (  m_structure  ==  null  )  { 
m_Buffer  =  new   Vector  (  )  ; 
try  { 
numAtt  =  0  ; 
line  =  new   StringBuffer  (  )  ; 
while  (  (  cInt  =  m_sourceReader  .  read  (  )  )  !=  -  1  )  { 
c  =  (  char  )  cInt  ; 
if  (  (  c  ==  '\n'  )  ||  (  c  ==  '\r'  )  )  { 
if  (  line  .  length  (  )  >  0  )  { 
m_Buffer  .  add  (  libsvmToArray  (  line  .  toString  (  )  )  )  ; 
numAtt  =  determineNumAttributes  (  line  .  toString  (  )  ,  numAtt  )  ; 
} 
line  =  new   StringBuffer  (  )  ; 
}  else  { 
line  .  append  (  c  )  ; 
} 
} 
if  (  line  .  length  (  )  !=  0  )  { 
m_Buffer  .  add  (  libsvmToArray  (  line  .  toString  (  )  )  )  ; 
numAtt  =  determineNumAttributes  (  line  .  toString  (  )  ,  numAtt  )  ; 
} 
atts  =  new   FastVector  (  numAtt  )  ; 
for  (  i  =  0  ;  i  <  numAtt  -  1  ;  i  ++  )  atts  .  addElement  (  new   Attribute  (  "att_"  +  (  i  +  1  )  )  )  ; 
atts  .  addElement  (  new   Attribute  (  "class"  )  )  ; 
if  (  !  m_URL  .  equals  (  "http://"  )  )  relName  =  m_URL  ;  else   relName  =  m_File  ; 
m_structure  =  new   Instances  (  relName  ,  atts  ,  0  )  ; 
m_structure  .  setClassIndex  (  m_structure  .  numAttributes  (  )  -  1  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
throw   new   IOException  (  "Unable to determine structure as libsvm: "  +  ex  )  ; 
} 
} 
return   new   Instances  (  m_structure  ,  0  )  ; 
} 










public   Instances   getDataSet  (  )  throws   IOException  { 
Instances   result  ; 
double  [  ]  sparse  ; 
double  [  ]  data  ; 
int   i  ; 
if  (  m_sourceReader  ==  null  )  throw   new   IOException  (  "No source has been specified"  )  ; 
if  (  getRetrieval  (  )  ==  INCREMENTAL  )  throw   new   IOException  (  "Cannot mix getting Instances in both incremental and batch modes"  )  ; 
setRetrieval  (  BATCH  )  ; 
if  (  m_structure  ==  null  )  getStructure  (  )  ; 
result  =  new   Instances  (  m_structure  ,  0  )  ; 
for  (  i  =  0  ;  i  <  m_Buffer  .  size  (  )  ;  i  ++  )  { 
sparse  =  (  double  [  ]  )  m_Buffer  .  get  (  i  )  ; 
if  (  sparse  .  length  !=  m_structure  .  numAttributes  (  )  )  { 
data  =  new   double  [  m_structure  .  numAttributes  (  )  ]  ; 
System  .  arraycopy  (  sparse  ,  0  ,  data  ,  0  ,  sparse  .  length  -  1  )  ; 
data  [  data  .  length  -  1  ]  =  sparse  [  sparse  .  length  -  1  ]  ; 
}  else  { 
data  =  sparse  ; 
} 
result  .  add  (  new   SparseInstance  (  1  ,  data  )  )  ; 
} 
try  { 
m_sourceReader  .  close  (  )  ; 
}  catch  (  Exception   ex  )  { 
} 
return   result  ; 
} 









public   Instance   getNextInstance  (  Instances   structure  )  throws   IOException  { 
throw   new   IOException  (  "LibSVMLoader can't read data sets incrementally."  )  ; 
} 






public   String   getRevision  (  )  { 
return   RevisionUtils  .  extract  (  "$Revision: 4853 $"  )  ; 
} 






public   static   void   main  (  String  [  ]  args  )  { 
runFileLoader  (  new   LibSVMLoader  (  )  ,  args  )  ; 
} 
} 

