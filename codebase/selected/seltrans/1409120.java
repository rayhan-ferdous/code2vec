package   cma  ; 

import   java  .  util  .  *  ; 


























































































































public   class   CMAEvolutionStrategy   implements   java  .  io  .  Serializable  { 




private   static   final   long   serialVersionUID  =  2918241762394253526L  ; 




public   final   String   versionNumber  =  new   String  (  "0.99.05"  )  ; 



public   class   StopCondition  { 

int   index  =  0  ; 

String  [  ]  messages  =  new   String  [  ]  {  ""  }  ; 

double   lastcounteval  ; 





public   boolean   isTrue  (  )  { 
return   test  (  )  >  0  ; 
} 




public   boolean   isFalse  (  )  { 
return  !  isTrue  (  )  ; 
} 




public   int   getNumber  (  )  { 
return   test  (  )  ; 
} 









public   String  [  ]  getMessages  (  )  { 
test  (  )  ; 
return   messages  ; 
} 



public   void   clear  (  )  { 
messages  =  new   String  [  ]  {  ""  }  ; 
index  =  0  ; 
} 

private   void   appendMessage  (  String   s  )  { 
String  [  ]  mold  =  messages  ; 
messages  =  new   String  [  index  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  index  ;  ++  i  )  messages  [  i  ]  =  mold  [  i  ]  ; 
messages  [  index  ++  ]  =  s  +  " (iter="  +  countiter  +  ",eval="  +  counteval  +  ")"  ; 
} 







int   test  (  )  { 
if  (  state  <  0  )  return   0  ; 
if  (  index  >  0  &&  (  counteval  ==  lastcounteval  ||  counteval  ==  lastcounteval  +  1  )  )  return   index  ; 
lastcounteval  =  counteval  ; 
if  (  (  countiter  >  1  ||  state  >=  3  )  &&  bestever_fit  <=  options  .  stopFitness  )  appendMessage  (  "Fitness: Objective function value dropped below the target function value "  +  options  .  stopFitness  )  ; 
if  (  counteval  >=  options  .  stopMaxFunEvals  )  appendMessage  (  "MaxFunEvals: maximum number of function evaluations "  +  options  .  stopMaxFunEvals  +  " reached"  )  ; 
if  (  countiter  >=  options  .  stopMaxIter  )  appendMessage  (  "MaxIter: maximum number of iterations reached"  )  ; 
if  (  (  countiter  >  1  ||  state  >=  3  )  &&  Math  .  max  (  math  .  max  (  fit  .  history  )  ,  fit  .  fitness  [  fit  .  fitness  .  length  -  1  ]  .  val  )  -  Math  .  min  (  math  .  min  (  fit  .  history  )  ,  fit  .  fitness  [  0  ]  .  val  )  <=  options  .  stopTolFun  )  appendMessage  (  "TolFun: function value changes below stopTolFun="  +  options  .  stopTolFun  )  ; 
if  (  options  .  stopTolFunHist  >=  0  &&  countiter  >  fit  .  history  .  length  )  { 
if  (  math  .  max  (  fit  .  history  )  -  math  .  min  (  fit  .  history  )  <=  options  .  stopTolFunHist  )  appendMessage  (  "TolFunHist: history of function value changes below stopTolFunHist="  +  options  .  stopTolFunHist  )  ; 
} 
double   tolx  =  Math  .  max  (  options  .  stopTolX  ,  options  .  stopTolXFactor  *  minstartsigma  )  ; 
if  (  sigma  *  maxsqrtdiagC  <  tolx  &&  sigma  *  math  .  max  (  math  .  abs  (  pc  )  )  <  tolx  )  appendMessage  (  "TolX or TolXFactor: standard deviation below "  +  tolx  )  ; 
if  (  sigma  *  maxsqrtdiagC  >  options  .  stopTolUpXFactor  *  maxstartsigma  )  appendMessage  (  "TolUpX: standard deviation increased by more than stopTolUpXFactor="  +  options  .  stopTolUpXFactor  +  ", larger initial standard deviation recommended"  )  ; 
if  (  options  .  stopnow  )  appendMessage  (  "Manual: flag Options.stopnow set or stop now in .properties file"  )  ; 
for  (  int   iAchse  =  0  ;  iAchse  <  N  ;  ++  iAchse  )  { 
int   iKoo  ; 
double   fac  =  0.1  *  sigma  *  diagD  [  iAchse  ]  ; 
for  (  iKoo  =  0  ;  iKoo  <  N  ;  ++  iKoo  )  { 
if  (  xmean  [  iKoo  ]  !=  xmean  [  iKoo  ]  +  fac  *  B  [  iKoo  ]  [  iAchse  ]  )  break  ; 
} 
if  (  iKoo  ==  N  )  appendMessage  (  "NoEffectAxis: Mutation "  +  0.1  *  sigma  *  diagD  [  iAchse  ]  +  " in a principal axis "  +  iAchse  +  " has no effect"  )  ; 
} 
for  (  int   iKoo  =  0  ;  iKoo  <  N  ;  ++  iKoo  )  { 
if  (  xmean  [  iKoo  ]  ==  xmean  [  iKoo  ]  +  0.2  *  sigma  *  Math  .  sqrt  (  C  [  iKoo  ]  [  iKoo  ]  )  )  appendMessage  (  "NoEffectCoordinate: Mutation of size "  +  0.2  *  sigma  *  Math  .  sqrt  (  C  [  iKoo  ]  [  iKoo  ]  )  +  " in coordinate "  +  iKoo  +  " has no effect"  )  ; 
} 
if  (  math  .  min  (  diagD  )  <=  0  )  appendMessage  (  "ConditionNumber: smallest eigenvalue smaller or equal zero"  )  ;  else   if  (  math  .  max  (  diagD  )  /  math  .  min  (  diagD  )  >  1e7  )  appendMessage  (  "ConditionNumber: condition number of the covariance matrix exceeds 1e14"  )  ; 
return   index  ; 
} 
} 

void   testAndCorrectNumerics  (  )  { 
if  (  getCountIter  (  )  >  1  ||  (  getCountIter  (  )  ==  1  &&  state  >=  3  )  )  if  (  fit  .  fitness  [  0  ]  .  val  ==  fit  .  fitness  [  Math  .  min  (  sp  .  getLambda  (  )  -  1  ,  sp  .  getLambda  (  )  /  2  +  1  )  -  1  ]  .  val  )  { 
warning  (  "flat fitness landscape, consider reformulation of fitness, step-size increased"  )  ; 
sigma  *=  Math  .  exp  (  0.2  +  sp  .  getCs  (  )  /  sp  .  getDamps  (  )  )  ; 
} 
double   fac  =  1  ; 
if  (  math  .  max  (  diagD  )  <  1e-6  )  fac  =  1.  /  math  .  max  (  diagD  )  ;  else   if  (  math  .  min  (  diagD  )  >  1e4  )  fac  =  1.  /  math  .  min  (  diagD  )  ; 
if  (  fac  !=  1.  )  { 
sigma  /=  fac  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  { 
pc  [  i  ]  *=  fac  ; 
diagD  [  i  ]  *=  fac  ; 
for  (  int   j  =  0  ;  j  <=  i  ;  ++  j  )  C  [  i  ]  [  j  ]  *=  fac  *  fac  ; 
} 
} 
} 




public   CMAOptions   options  =  new   CMAOptions  (  )  ; 

private   CMAParameters   sp  =  new   CMAParameters  (  )  ; 



public   CMAParameters   parameters  =  sp  ; 


public   StopCondition   stopConditions  =  new   StopCondition  (  )  ; 

int   N  ; 

long   seed  =  System  .  currentTimeMillis  (  )  ; 

Random   rand  =  new   Random  (  seed  )  ; 

final   MyMath   math  =  new   MyMath  (  )  ; 

double   axisratio  ; 

long   counteval  ; 

long   countiter  ; 

long   bestever_eval  ; 

double  [  ]  bestever_x  ; 

double   bestever_fit  =  Double  .  NaN  ; 

double   sigma  =  0.0  ; 

double  [  ]  typicalX  ; 

double  [  ]  initialX  ; 

double  [  ]  LBound  ,  UBound  ; 

double  [  ]  xmean  ; 

double   xmean_fit  =  Double  .  NaN  ; 

double  [  ]  pc  ; 

double  [  ]  ps  ; 

double  [  ]  [  ]  C  ; 

double   maxsqrtdiagC  ; 

double   minsqrtdiagC  ; 

double  [  ]  [  ]  B  ; 

double  [  ]  diagD  ; 

double  [  ]  startsigma  ; 

double   maxstartsigma  ; 

double   minstartsigma  ; 

boolean   iniphase  ; 










double   state  =  -  1  ; 

long   citerlastwritten  =  0  ; 

long   countwritten  =  0  ; 

int   lockDimension  =  0  ; 

int   mode  =  0  ; 

final   int   SINGLE_MODE  =  1  ; 

final   int   PARALLEL_MODE  =  2  ; 

long   countCupdatesSinceEigenupdate  ; 

class   FitnessCollector  { 

double   history  [  ]  ; 

IntDouble  [  ]  fitness  ; 

IntDouble  [  ]  raw  ; 



double  [  ]  deltaFitHist  =  new   double  [  5  ]  ; 

int   idxDeltaFitHist  =  0  ; 
} 

protected   FitnessCollector   fit  =  new   FitnessCollector  (  )  ; 

double   recentFunctionValue  ; 

double   recentMaxFunctionValue  ; 

double   recentMinFunctionValue  ; 

int   idxRecentOffspring  ; 

double  [  ]  [  ]  arx  ; 


public   double  [  ]  [  ]  population  ; 

double  [  ]  xold  ; 

double  [  ]  BDz  ; 

double  [  ]  artmp  ; 

String   propertiesFileName  =  new   String  (  "CMAEvolutionStrategy.properties"  )  ; 



public   CMAEvolutionStrategy  (  )  { 
state  =  -  1  ; 
} 



public   CMAEvolutionStrategy  (  Properties   properties  )  { 
setFromProperties  (  properties  )  ; 
state  =  -  1  ; 
} 




public   CMAEvolutionStrategy  (  String   propertiesFileName  )  { 
this  .  propertiesFileName  =  propertiesFileName  ; 
state  =  -  1  ; 
} 




public   CMAEvolutionStrategy  (  int   dimension  )  { 
setDimension  (  dimension  )  ; 
state  =  -  1  ; 
} 



































public   double  [  ]  init  (  int   dimension  ,  double  [  ]  initialX  ,  double  [  ]  initialStandardDeviations  )  { 
setInitialX  (  initialX  )  ; 
setInitialStandardDeviations  (  initialStandardDeviations  )  ; 
return   init  (  dimension  )  ; 
} 

private   double  [  ]  getArrayOf  (  double   x  ,  int   dim  )  { 
double  [  ]  res  =  new   double  [  dim  ]  ; 
for  (  int   i  =  0  ;  i  <  dim  ;  ++  i  )  res  [  i  ]  =  x  ; 
return   res  ; 
} 







private   double  [  ]  expandToDimension  (  double  [  ]  x  ,  int   dim  )  { 
if  (  x  ==  null  )  return   null  ; 
if  (  x  .  length  ==  dim  )  return   x  ; 
if  (  x  .  length  !=  1  )  error  (  "x must have length one or length dimension"  )  ; 
return   getArrayOf  (  x  [  0  ]  ,  dim  )  ; 
} 





public   double  [  ]  init  (  int   dimension  )  { 
setDimension  (  dimension  )  ; 
return   init  (  )  ; 
} 




public   double  [  ]  init  (  )  { 
int   i  ; 
if  (  N  <=  0  )  error  (  "dimension needs to be determined, use eg. setDimension() or setInitialX()"  )  ; 
if  (  state  >=  0  )  error  (  "init() cannot be called twice"  )  ; 
if  (  state  ==  0  )  return   new   double  [  sp  .  getLambda  (  )  ]  ; 
if  (  state  >  0  )  error  (  "init() cannot be called after the first population was sampled"  )  ; 
sp  =  parameters  ; 
if  (  sp  .  supplemented  ==  0  )  sp  .  supplementRemainders  (  N  ,  options  )  ; 
sp  .  locked  =  1  ; 
diagD  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  diagD  [  i  ]  =  1  ; 
LBound  =  expandToDimension  (  LBound  ,  N  )  ; 
if  (  LBound  ==  null  )  { 
LBound  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  LBound  [  i  ]  =  Double  .  NEGATIVE_INFINITY  ; 
} 
UBound  =  expandToDimension  (  UBound  ,  N  )  ; 
if  (  UBound  ==  null  )  { 
UBound  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  UBound  [  i  ]  =  Double  .  POSITIVE_INFINITY  ; 
} 
if  (  startsigma  !=  null  )  { 
if  (  startsigma  .  length  ==  1  )  { 
sigma  =  startsigma  [  0  ]  ; 
}  else   if  (  startsigma  .  length  ==  N  )  { 
sigma  =  math  .  max  (  startsigma  )  ; 
if  (  sigma  <=  0  )  error  (  "initial standard deviation sigma must be positive"  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
diagD  [  i  ]  =  startsigma  [  i  ]  /  sigma  ; 
} 
}  else   assert   false  ; 
}  else  { 
error  (  "no initial standard deviation specified, use setInitialStandardDeviations()"  )  ; 
sigma  =  0.5  ; 
} 
if  (  sigma  <=  0  ||  math  .  min  (  diagD  )  <=  0  )  { 
error  (  "initial standard deviations not specified or non-positive, "  +  "use setInitialStandarddeviations()"  )  ; 
sigma  =  1  ; 
} 
if  (  startsigma  ==  null  ||  startsigma  .  length  ==  1  )  { 
startsigma  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
startsigma  [  i  ]  =  sigma  *  diagD  [  i  ]  ; 
} 
} 
maxstartsigma  =  math  .  max  (  startsigma  )  ; 
minstartsigma  =  math  .  min  (  startsigma  )  ; 
axisratio  =  maxstartsigma  /  minstartsigma  ; 
typicalX  =  expandToDimension  (  typicalX  ,  N  )  ; 
xmean  =  expandToDimension  (  xmean  ,  N  )  ; 
if  (  xmean  ==  null  )  { 
if  (  typicalX  !=  null  )  { 
xmean  =  typicalX  .  clone  (  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  xmean  [  i  ]  +=  sigma  *  diagD  [  i  ]  *  rand  .  nextGaussian  (  )  ; 
}  else   if  (  math  .  max  (  UBound  )  <  Double  .  MAX_VALUE  &&  math  .  min  (  LBound  )  >  -  Double  .  MAX_VALUE  )  { 
error  (  "no initial search point (solution) X or typical X specified"  )  ; 
xmean  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
double   offset  =  sigma  *  diagD  [  i  ]  ; 
double   range  =  (  UBound  [  i  ]  -  LBound  [  i  ]  -  2  *  sigma  *  diagD  [  i  ]  )  ; 
if  (  offset  >  0.4  *  (  UBound  [  i  ]  -  LBound  [  i  ]  )  )  { 
offset  =  0.4  *  (  UBound  [  i  ]  -  LBound  [  i  ]  )  ; 
range  =  0.2  *  (  UBound  [  i  ]  -  LBound  [  i  ]  )  ; 
} 
xmean  [  i  ]  =  LBound  [  i  ]  +  offset  +  rand  .  nextDouble  (  )  *  range  ; 
} 
}  else  { 
error  (  "no initial search point (solution) X or typical X specified"  )  ; 
xmean  =  new   double  [  N  ]  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  xmean  [  i  ]  =  rand  .  nextDouble  (  )  ; 
} 
} 
assert   xmean  !=  null  ; 
assert   sigma  >  0  ; 
pc  =  new   double  [  N  ]  ; 
ps  =  new   double  [  N  ]  ; 
B  =  new   double  [  N  ]  [  N  ]  ; 
C  =  new   double  [  N  ]  [  N  ]  ; 
xold  =  new   double  [  N  ]  ; 
BDz  =  new   double  [  N  ]  ; 
bestever_x  =  xmean  .  clone  (  )  ; 
artmp  =  new   double  [  N  ]  ; 
fit  .  deltaFitHist  =  new   double  [  5  ]  ; 
fit  .  idxDeltaFitHist  =  -  1  ; 
for  (  i  =  0  ;  i  <  fit  .  deltaFitHist  .  length  ;  ++  i  )  fit  .  deltaFitHist  [  i  ]  =  1.  ; 
fit  .  fitness  =  new   IntDouble  [  sp  .  getLambda  (  )  ]  ; 
fit  .  raw  =  new   IntDouble  [  sp  .  getLambda  (  )  ]  ; 
fit  .  history  =  new   double  [  10  +  30  *  N  /  sp  .  getLambda  (  )  ]  ; 
arx  =  new   double  [  sp  .  getLambda  (  )  ]  [  N  ]  ; 
population  =  new   double  [  sp  .  getLambda  (  )  ]  [  N  ]  ; 
for  (  i  =  0  ;  i  <  sp  .  getLambda  (  )  ;  ++  i  )  { 
fit  .  fitness  [  i  ]  =  new   IntDouble  (  )  ; 
fit  .  raw  [  i  ]  =  new   IntDouble  (  )  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
pc  [  i  ]  =  0  ; 
ps  [  i  ]  =  0  ; 
for  (  int   j  =  0  ;  j  <  N  ;  ++  j  )  { 
B  [  i  ]  [  j  ]  =  0  ; 
} 
for  (  int   j  =  0  ;  j  <  i  ;  ++  j  )  { 
C  [  i  ]  [  j  ]  =  0  ; 
} 
B  [  i  ]  [  i  ]  =  1  ; 
C  [  i  ]  [  i  ]  =  diagD  [  i  ]  *  diagD  [  i  ]  ; 
} 
maxsqrtdiagC  =  Math  .  sqrt  (  math  .  max  (  math  .  diag  (  C  )  )  )  ; 
minsqrtdiagC  =  Math  .  sqrt  (  math  .  min  (  math  .  diag  (  C  )  )  )  ; 
countCupdatesSinceEigenupdate  =  0  ; 
iniphase  =  false  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
if  (  LBound  [  i  ]  >  UBound  [  i  ]  )  error  (  "lower bound is greater than upper bound"  )  ; 
if  (  typicalX  !=  null  )  { 
if  (  LBound  [  i  ]  >  typicalX  [  i  ]  )  error  (  "lower bound '"  +  LBound  [  i  ]  +  "'is greater than typicalX"  +  typicalX  [  i  ]  )  ; 
if  (  UBound  [  i  ]  <  typicalX  [  i  ]  )  error  (  "upper bound '"  +  UBound  [  i  ]  +  "' is smaller than typicalX "  +  typicalX  [  i  ]  )  ; 
} 
} 
String  [  ]  s  =  stopConditions  .  getMessages  (  )  ; 
if  (  !  s  [  0  ]  .  equals  (  ""  )  )  warning  (  "termination condition satisfied at initialization: \n  "  +  s  [  0  ]  )  ; 
initialX  =  xmean  .  clone  (  )  ; 
timings  .  start  =  System  .  currentTimeMillis  (  )  ; 
state  =  0  ; 
if  (  options  .  verbosity  >  -  1  )  printlnHelloWorld  (  )  ; 
return   new   double  [  sp  .  getLambda  (  )  ]  ; 
} 






public   CMAParameters   getParameterDefaults  (  )  { 
return   sp  .  getDefaults  (  N  )  ; 
} 





public   CMAParameters   getParameterDefaults  (  int   N  )  { 
return   sp  .  getDefaults  (  N  )  ; 
} 






public   Properties   readProperties  (  )  { 
return   readProperties  (  propertiesFileName  )  ; 
} 

Properties   properties  =  new   Properties  (  )  ; 





public   Properties   readProperties  (  String   fileName  )  { 
this  .  propertiesFileName  =  fileName  ; 
try  { 
java  .  io  .  FileInputStream   fis  =  new   java  .  io  .  FileInputStream  (  fileName  )  ; 
properties  .  load  (  fis  )  ; 
fis  .  close  (  )  ; 
}  catch  (  java  .  io  .  IOException   e  )  { 
warning  (  "File '"  +  fileName  +  "' not found, no options read"  )  ; 
} 
setFromProperties  (  properties  )  ; 
return   properties  ; 
} 







public   void   setFromProperties  (  Properties   properties  )  { 
String   s  ; 
options  .  setOptions  (  properties  )  ; 
if  (  state  >=  0  )  return  ; 
if  (  (  s  =  properties  .  getProperty  (  "typicalX"  )  )  !=  null  )  { 
setTypicalX  (  options  .  parseDouble  (  options  .  getAllToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "initialX"  )  )  !=  null  )  { 
setInitialX  (  options  .  parseDouble  (  options  .  getAllToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "initialStandardDeviations"  )  )  !=  null  )  { 
setInitialStandardDeviations  (  options  .  parseDouble  (  options  .  getAllToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "dimension"  )  )  !=  null  )  { 
setDimension  (  Integer  .  parseInt  (  options  .  getFirstToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "randomSeed"  )  )  !=  null  )  { 
setSeed  (  Long  .  parseLong  (  options  .  getFirstToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "populationSize"  )  )  !=  null  )  { 
sp  .  setPopulationSize  (  Integer  .  parseInt  (  options  .  getFirstToken  (  s  )  )  )  ; 
} 
if  (  (  s  =  properties  .  getProperty  (  "cCov"  )  )  !=  null  )  { 
sp  .  setCcov  (  Integer  .  parseInt  (  options  .  getFirstToken  (  s  )  )  )  ; 
} 
} 

private   void   infoVerbose  (  String   s  )  { 
println  (  " CMA-ES info: "  +  s  )  ; 
} 

private   void   warning  (  String   s  )  { 
println  (  " CMA-ES warning: "  +  s  )  ; 
} 

private   void   error  (  String   s  )  { 
println  (  " CMA-ES error: "  +  s  )  ; 
throw   new   CMAException  (  " CMA-ES error: "  +  s  )  ; 
} 


class   MyMath  { 

int   itest  ; 

double   square  (  double   d  )  { 
return   d  *  d  ; 
} 

double   prod  (  double  [  ]  ar  )  { 
double   res  =  1.0  ; 
for  (  int   i  =  0  ;  i  <  ar  .  length  ;  ++  i  )  res  *=  ar  [  i  ]  ; 
return   res  ; 
} 

public   double   median  (  double   ar  [  ]  )  { 
double  [  ]  ar2  =  new   double  [  ar  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  ar  .  length  ;  ++  i  )  ar2  [  i  ]  =  ar  [  i  ]  ; 
Arrays  .  sort  (  ar2  )  ; 
if  (  ar2  .  length  %  2  ==  0  )  return  (  ar2  [  ar  .  length  /  2  ]  +  ar2  [  ar  .  length  /  2  -  1  ]  )  /  2.  ;  else   return   ar2  [  ar  .  length  /  2  ]  ; 
} 


public   double   max  (  double   ar  [  ]  )  { 
int   i  ; 
double   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  m  <  ar  [  i  ]  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 


public   double   hypot  (  double   a  ,  double   b  )  { 
double   r  =  0  ; 
if  (  Math  .  abs  (  a  )  >  Math  .  abs  (  b  )  )  { 
r  =  b  /  a  ; 
r  =  Math  .  abs  (  a  )  *  Math  .  sqrt  (  1  +  r  *  r  )  ; 
}  else   if  (  b  !=  0  )  { 
r  =  a  /  b  ; 
r  =  Math  .  abs  (  b  )  *  Math  .  sqrt  (  1  +  r  *  r  )  ; 
} 
return   r  ; 
} 


public   int   minidx  (  double   ar  [  ]  )  { 
return   minidx  (  ar  ,  ar  .  length  -  1  )  ; 
} 





public   int   minidx  (  double  [  ]  ar  ,  int   maxidx  )  { 
int   i  ,  idx  ; 
idx  =  0  ; 
for  (  i  =  1  ;  i  <  maxidx  ;  ++  i  )  { 
if  (  ar  [  idx  ]  >  ar  [  i  ]  )  idx  =  i  ; 
} 
return   idx  ; 
} 





protected   int   minidx  (  IntDouble  [  ]  ar  ,  int   maxidx  )  { 
int   i  ,  idx  ; 
idx  =  0  ; 
for  (  i  =  1  ;  i  <  maxidx  ;  ++  i  )  { 
if  (  ar  [  idx  ]  .  val  >  ar  [  i  ]  .  val  )  idx  =  i  ; 
} 
return   idx  ; 
} 


public   int   maxidx  (  double   ar  [  ]  )  { 
int   i  ,  idx  ; 
idx  =  0  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  ar  [  idx  ]  <  ar  [  i  ]  )  idx  =  i  ; 
} 
return   idx  ; 
} 


public   double   min  (  double   ar  [  ]  )  { 
int   i  ; 
double   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  m  >  ar  [  i  ]  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 



public   Double   max  (  Double   ar  [  ]  ,  Comparator  <  Double  >  c  )  { 
int   i  ; 
Double   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  c  .  compare  (  m  ,  ar  [  i  ]  )  >  0  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 


public   IntDouble   max  (  IntDouble   ar  [  ]  )  { 
int   i  ; 
IntDouble   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  m  .  compare  (  m  ,  ar  [  i  ]  )  <  0  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 


public   IntDouble   min  (  IntDouble   ar  [  ]  )  { 
int   i  ; 
IntDouble   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  m  .  compare  (  m  ,  ar  [  i  ]  )  >  0  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 


public   Double   min  (  Double   ar  [  ]  ,  Comparator  <  Double  >  c  )  { 
int   i  ; 
Double   m  ; 
m  =  ar  [  0  ]  ; 
for  (  i  =  1  ;  i  <  ar  .  length  ;  ++  i  )  { 
if  (  c  .  compare  (  m  ,  ar  [  i  ]  )  <  0  )  m  =  ar  [  i  ]  ; 
} 
return   m  ; 
} 




public   double  [  ]  diag  (  double   ar  [  ]  [  ]  )  { 
int   i  ; 
double  [  ]  diag  =  new   double  [  ar  .  length  ]  ; 
for  (  i  =  0  ;  i  <  ar  .  length  &&  i  <  ar  [  i  ]  .  length  ;  ++  i  )  diag  [  i  ]  =  ar  [  i  ]  [  i  ]  ; 
return   diag  ; 
} 




public   double  [  ]  abs  (  double   v  [  ]  )  { 
double   res  [  ]  =  new   double  [  v  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  v  .  length  ;  ++  i  )  res  [  i  ]  =  Math  .  abs  (  v  [  i  ]  )  ; 
return   res  ; 
} 
} 

class   Timing  { 

Timing  (  )  { 
birth  =  System  .  currentTimeMillis  (  )  ; 
start  =  birth  ; 
} 

long   birth  ; 

long   start  ; 

long   eigendecomposition  =  0  ; 

long   writedefaultfiles  =  0  ; 
} 

Timing   timings  =  new   Timing  (  )  ; 

void   eigendecomposition  (  int   flgforce  )  { 
int   i  ,  j  ; 
if  (  countCupdatesSinceEigenupdate  ==  0  &&  flgforce  <  2  )  return  ; 
if  (  flgforce  >  0  ||  (  timings  .  eigendecomposition  <  1000  +  options  .  maxTimeFractionForEigendecomposition  *  (  System  .  currentTimeMillis  (  )  -  timings  .  start  )  &&  countCupdatesSinceEigenupdate  >  1.  /  sp  .  getCcov  (  )  /  N  /  5.  )  )  { 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  for  (  j  =  0  ;  j  <=  i  ;  ++  j  )  B  [  i  ]  [  j  ]  =  B  [  j  ]  [  i  ]  =  C  [  i  ]  [  j  ]  ; 
double  [  ]  offdiag  =  new   double  [  N  ]  ; 
long   firsttime  =  System  .  currentTimeMillis  (  )  ; 
tred2  (  N  ,  B  ,  diagD  ,  offdiag  )  ; 
tql2  (  N  ,  diagD  ,  offdiag  ,  B  )  ; 
timings  .  eigendecomposition  +=  System  .  currentTimeMillis  (  )  -  firsttime  ; 
if  (  options  .  checkEigenSystem  >  0  )  checkEigenSystem  (  N  ,  C  ,  diagD  ,  B  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
if  (  diagD  [  i  ]  <  0  )  error  (  "an eigenvalue has become negative"  )  ; 
diagD  [  i  ]  =  Math  .  sqrt  (  diagD  [  i  ]  )  ; 
} 
if  (  math  .  min  (  diagD  )  ==  0  )  axisratio  =  Double  .  POSITIVE_INFINITY  ;  else   axisratio  =  math  .  max  (  diagD  )  /  math  .  min  (  diagD  )  ; 
countCupdatesSinceEigenupdate  =  0  ; 
} 
} 

int   checkEigenSystem  (  int   N  ,  double   C  [  ]  [  ]  ,  double   diag  [  ]  ,  double   Q  [  ]  [  ]  )  { 
int   i  ,  j  ,  k  ,  res  =  0  ; 
double   cc  ,  dd  ; 
String   s  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  for  (  j  =  0  ;  j  <  N  ;  ++  j  )  { 
for  (  cc  =  0.  ,  dd  =  0.  ,  k  =  0  ;  k  <  N  ;  ++  k  )  { 
cc  +=  diag  [  k  ]  *  Q  [  i  ]  [  k  ]  *  Q  [  j  ]  [  k  ]  ; 
dd  +=  Q  [  i  ]  [  k  ]  *  Q  [  j  ]  [  k  ]  ; 
} 
if  (  Math  .  abs  (  cc  -  C  [  i  >  j  ?  i  :  j  ]  [  i  >  j  ?  j  :  i  ]  )  /  Math  .  sqrt  (  C  [  i  ]  [  i  ]  *  C  [  j  ]  [  j  ]  )  >  1e-10  &&  Math  .  abs  (  cc  -  C  [  i  >  j  ?  i  :  j  ]  [  i  >  j  ?  j  :  i  ]  )  >  1e-9  )  { 
s  =  " "  +  i  +  " "  +  j  +  " "  +  cc  +  " "  +  C  [  i  >  j  ?  i  :  j  ]  [  i  >  j  ?  j  :  i  ]  +  " "  +  (  cc  -  C  [  i  >  j  ?  i  :  j  ]  [  i  >  j  ?  j  :  i  ]  )  ; 
warning  (  "cmaes_t:Eigen(): imprecise result detected "  +  s  )  ; 
++  res  ; 
} 
if  (  Math  .  abs  (  dd  -  (  i  ==  j  ?  1  :  0  )  )  >  1e-10  )  { 
s  =  i  +  " "  +  j  +  " "  +  dd  ; 
warning  (  "cmaes_t:Eigen(): imprecise result detected (Q not orthog.) "  +  s  )  ; 
++  res  ; 
} 
} 
return   res  ; 
} 

private   void   tred2  (  int   n  ,  double   V  [  ]  [  ]  ,  double   d  [  ]  ,  double   e  [  ]  )  { 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
d  [  j  ]  =  V  [  n  -  1  ]  [  j  ]  ; 
} 
for  (  int   i  =  n  -  1  ;  i  >  0  ;  i  --  )  { 
double   scale  =  0.0  ; 
double   h  =  0.0  ; 
for  (  int   k  =  0  ;  k  <  i  ;  k  ++  )  { 
scale  =  scale  +  Math  .  abs  (  d  [  k  ]  )  ; 
} 
if  (  scale  ==  0.0  )  { 
e  [  i  ]  =  d  [  i  -  1  ]  ; 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
d  [  j  ]  =  V  [  i  -  1  ]  [  j  ]  ; 
V  [  i  ]  [  j  ]  =  0.0  ; 
V  [  j  ]  [  i  ]  =  0.0  ; 
} 
}  else  { 
for  (  int   k  =  0  ;  k  <  i  ;  k  ++  )  { 
d  [  k  ]  /=  scale  ; 
h  +=  d  [  k  ]  *  d  [  k  ]  ; 
} 
double   f  =  d  [  i  -  1  ]  ; 
double   g  =  Math  .  sqrt  (  h  )  ; 
if  (  f  >  0  )  { 
g  =  -  g  ; 
} 
e  [  i  ]  =  scale  *  g  ; 
h  =  h  -  f  *  g  ; 
d  [  i  -  1  ]  =  f  -  g  ; 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
e  [  j  ]  =  0.0  ; 
} 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
f  =  d  [  j  ]  ; 
V  [  j  ]  [  i  ]  =  f  ; 
g  =  e  [  j  ]  +  V  [  j  ]  [  j  ]  *  f  ; 
for  (  int   k  =  j  +  1  ;  k  <=  i  -  1  ;  k  ++  )  { 
g  +=  V  [  k  ]  [  j  ]  *  d  [  k  ]  ; 
e  [  k  ]  +=  V  [  k  ]  [  j  ]  *  f  ; 
} 
e  [  j  ]  =  g  ; 
} 
f  =  0.0  ; 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
e  [  j  ]  /=  h  ; 
f  +=  e  [  j  ]  *  d  [  j  ]  ; 
} 
double   hh  =  f  /  (  h  +  h  )  ; 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
e  [  j  ]  -=  hh  *  d  [  j  ]  ; 
} 
for  (  int   j  =  0  ;  j  <  i  ;  j  ++  )  { 
f  =  d  [  j  ]  ; 
g  =  e  [  j  ]  ; 
for  (  int   k  =  j  ;  k  <=  i  -  1  ;  k  ++  )  { 
V  [  k  ]  [  j  ]  -=  (  f  *  e  [  k  ]  +  g  *  d  [  k  ]  )  ; 
} 
d  [  j  ]  =  V  [  i  -  1  ]  [  j  ]  ; 
V  [  i  ]  [  j  ]  =  0.0  ; 
} 
} 
d  [  i  ]  =  h  ; 
} 
for  (  int   i  =  0  ;  i  <  n  -  1  ;  i  ++  )  { 
V  [  n  -  1  ]  [  i  ]  =  V  [  i  ]  [  i  ]  ; 
V  [  i  ]  [  i  ]  =  1.0  ; 
double   h  =  d  [  i  +  1  ]  ; 
if  (  h  !=  0.0  )  { 
for  (  int   k  =  0  ;  k  <=  i  ;  k  ++  )  { 
d  [  k  ]  =  V  [  k  ]  [  i  +  1  ]  /  h  ; 
} 
for  (  int   j  =  0  ;  j  <=  i  ;  j  ++  )  { 
double   g  =  0.0  ; 
for  (  int   k  =  0  ;  k  <=  i  ;  k  ++  )  { 
g  +=  V  [  k  ]  [  i  +  1  ]  *  V  [  k  ]  [  j  ]  ; 
} 
for  (  int   k  =  0  ;  k  <=  i  ;  k  ++  )  { 
V  [  k  ]  [  j  ]  -=  g  *  d  [  k  ]  ; 
} 
} 
} 
for  (  int   k  =  0  ;  k  <=  i  ;  k  ++  )  { 
V  [  k  ]  [  i  +  1  ]  =  0.0  ; 
} 
} 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
d  [  j  ]  =  V  [  n  -  1  ]  [  j  ]  ; 
V  [  n  -  1  ]  [  j  ]  =  0.0  ; 
} 
V  [  n  -  1  ]  [  n  -  1  ]  =  1.0  ; 
e  [  0  ]  =  0.0  ; 
} 

private   void   tql2  (  int   n  ,  double   d  [  ]  ,  double   e  [  ]  ,  double   V  [  ]  [  ]  )  { 
for  (  int   i  =  1  ;  i  <  n  ;  i  ++  )  { 
e  [  i  -  1  ]  =  e  [  i  ]  ; 
} 
e  [  n  -  1  ]  =  0.0  ; 
double   f  =  0.0  ; 
double   tst1  =  0.0  ; 
double   eps  =  Math  .  pow  (  2.0  ,  -  52.0  )  ; 
for  (  int   l  =  0  ;  l  <  n  ;  l  ++  )  { 
tst1  =  Math  .  max  (  tst1  ,  Math  .  abs  (  d  [  l  ]  )  +  Math  .  abs  (  e  [  l  ]  )  )  ; 
int   m  =  l  ; 
while  (  m  <  n  )  { 
if  (  Math  .  abs  (  e  [  m  ]  )  <=  eps  *  tst1  )  { 
break  ; 
} 
m  ++  ; 
} 
if  (  m  >  l  )  { 
int   iter  =  0  ; 
do  { 
iter  =  iter  +  1  ; 
double   g  =  d  [  l  ]  ; 
double   p  =  (  d  [  l  +  1  ]  -  g  )  /  (  2.0  *  e  [  l  ]  )  ; 
double   r  =  math  .  hypot  (  p  ,  1.0  )  ; 
if  (  p  <  0  )  { 
r  =  -  r  ; 
} 
d  [  l  ]  =  e  [  l  ]  /  (  p  +  r  )  ; 
d  [  l  +  1  ]  =  e  [  l  ]  *  (  p  +  r  )  ; 
double   dl1  =  d  [  l  +  1  ]  ; 
double   h  =  g  -  d  [  l  ]  ; 
for  (  int   i  =  l  +  2  ;  i  <  n  ;  i  ++  )  { 
d  [  i  ]  -=  h  ; 
} 
f  =  f  +  h  ; 
p  =  d  [  m  ]  ; 
double   c  =  1.0  ; 
double   c2  =  c  ; 
double   c3  =  c  ; 
double   el1  =  e  [  l  +  1  ]  ; 
double   s  =  0.0  ; 
double   s2  =  0.0  ; 
for  (  int   i  =  m  -  1  ;  i  >=  l  ;  i  --  )  { 
c3  =  c2  ; 
c2  =  c  ; 
s2  =  s  ; 
g  =  c  *  e  [  i  ]  ; 
h  =  c  *  p  ; 
r  =  math  .  hypot  (  p  ,  e  [  i  ]  )  ; 
e  [  i  +  1  ]  =  s  *  r  ; 
s  =  e  [  i  ]  /  r  ; 
c  =  p  /  r  ; 
p  =  c  *  d  [  i  ]  -  s  *  g  ; 
d  [  i  +  1  ]  =  h  +  s  *  (  c  *  g  +  s  *  d  [  i  ]  )  ; 
for  (  int   k  =  0  ;  k  <  n  ;  k  ++  )  { 
h  =  V  [  k  ]  [  i  +  1  ]  ; 
V  [  k  ]  [  i  +  1  ]  =  s  *  V  [  k  ]  [  i  ]  +  c  *  h  ; 
V  [  k  ]  [  i  ]  =  c  *  V  [  k  ]  [  i  ]  -  s  *  h  ; 
} 
} 
p  =  -  s  *  s2  *  c3  *  el1  *  e  [  l  ]  /  dl1  ; 
e  [  l  ]  =  s  *  p  ; 
d  [  l  ]  =  c  *  p  ; 
}  while  (  Math  .  abs  (  e  [  l  ]  )  >  eps  *  tst1  )  ; 
} 
d  [  l  ]  =  d  [  l  ]  +  f  ; 
e  [  l  ]  =  0.0  ; 
} 
for  (  int   i  =  0  ;  i  <  n  -  1  ;  i  ++  )  { 
int   k  =  i  ; 
double   p  =  d  [  i  ]  ; 
for  (  int   j  =  i  +  1  ;  j  <  n  ;  j  ++  )  { 
if  (  d  [  j  ]  <  p  )  { 
k  =  j  ; 
p  =  d  [  j  ]  ; 
} 
} 
if  (  k  !=  i  )  { 
d  [  k  ]  =  d  [  i  ]  ; 
d  [  i  ]  =  p  ; 
for  (  int   j  =  0  ;  j  <  n  ;  j  ++  )  { 
p  =  V  [  j  ]  [  i  ]  ; 
V  [  j  ]  [  i  ]  =  V  [  j  ]  [  k  ]  ; 
V  [  j  ]  [  k  ]  =  p  ; 
} 
} 
} 
} 







double  [  ]  [  ]  genoPhenoTransformation  (  double  [  ]  [  ]  popx  ,  double  [  ]  [  ]  popy  )  { 
if  (  popy  ==  null  ||  popy  ==  popx  ||  popy  .  length  !=  popx  .  length  )  popy  =  new   double  [  popx  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  popy  .  length  ;  ++  i  )  popy  [  i  ]  =  genoPhenoTransformation  (  popx  [  i  ]  ,  popy  [  i  ]  )  ; 
return   popy  ; 
} 







double  [  ]  [  ]  phenoGenoTransformation  (  double  [  ]  [  ]  popx  ,  double  [  ]  [  ]  popy  )  { 
if  (  popy  ==  null  ||  popy  ==  popx  ||  popy  .  length  !=  popx  .  length  )  popy  =  new   double  [  popx  .  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  popy  .  length  ;  ++  i  )  popy  [  i  ]  =  phenoGenoTransformation  (  popx  [  i  ]  ,  popy  [  i  ]  )  ; 
return   popy  ; 
} 







double  [  ]  genoPhenoTransformation  (  double  [  ]  x  ,  double  [  ]  y  )  { 
if  (  y  ==  null  ||  y  ==  x  ||  y  .  length  !=  x  .  length  )  { 
y  =  x  .  clone  (  )  ; 
return   y  ; 
} 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  y  [  i  ]  =  x  [  i  ]  ; 
return   y  ; 
} 







double  [  ]  phenoGenoTransformation  (  double  [  ]  x  ,  double  [  ]  y  )  { 
if  (  y  ==  null  ||  y  ==  x  ||  y  .  length  !=  x  .  length  )  { 
y  =  x  .  clone  (  )  ; 
return   y  ; 
} 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  y  [  i  ]  =  x  [  i  ]  ; 
return   y  ; 
} 









public   double  [  ]  [  ]  samplePopulation  (  )  { 
int   i  ,  j  ,  iNk  ; 
double   sum  ; 
if  (  state  <  0  )  init  (  )  ;  else   if  (  state  <  3  &&  state  >  2  )  error  (  "mixing of calls to updateSingle() and samplePopulation() is not possible"  )  ;  else   eigendecomposition  (  0  )  ; 
if  (  state  !=  1  )  ++  countiter  ; 
state  =  1  ; 
idxRecentOffspring  =  sp  .  getLambda  (  )  -  1  ; 
if  (  options  .  lowerStandardDeviations  !=  null  &&  options  .  lowerStandardDeviations  .  length  >  0  )  for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
double   d  =  options  .  lowerStandardDeviations  [  Math  .  min  (  i  ,  options  .  lowerStandardDeviations  .  length  -  1  )  ]  ; 
if  (  d  >  sigma  *  minsqrtdiagC  )  sigma  =  d  /  minsqrtdiagC  ; 
} 
if  (  options  .  upperStandardDeviations  !=  null  &&  options  .  upperStandardDeviations  .  length  >  0  )  for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
double   d  =  options  .  upperStandardDeviations  [  Math  .  min  (  i  ,  options  .  upperStandardDeviations  .  length  -  1  )  ]  ; 
if  (  d  <  sigma  *  maxsqrtdiagC  )  sigma  =  d  /  maxsqrtdiagC  ; 
} 
testAndCorrectNumerics  (  )  ; 
for  (  iNk  =  0  ;  iNk  <  sp  .  getLambda  (  )  ;  ++  iNk  )  { 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
artmp  [  i  ]  =  diagD  [  i  ]  *  rand  .  nextGaussian  (  )  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
for  (  j  =  0  ,  sum  =  0  ;  j  <  N  ;  ++  j  )  sum  +=  B  [  i  ]  [  j  ]  *  artmp  [  j  ]  ; 
arx  [  iNk  ]  [  i  ]  =  xmean  [  i  ]  +  sigma  *  sum  ; 
} 
} 
return   population  =  genoPhenoTransformation  (  arx  ,  population  )  ; 
} 















public   double  [  ]  resampleSingle  (  int   index  )  { 
int   i  ,  j  ; 
double   sum  ; 
if  (  state  !=  1  )  error  (  "call samplePopulation before calling resampleSingle(int index)"  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
artmp  [  i  ]  =  diagD  [  i  ]  *  rand  .  nextGaussian  (  )  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
for  (  j  =  0  ,  sum  =  0.0  ;  j  <  N  ;  ++  j  )  sum  +=  B  [  i  ]  [  j  ]  *  artmp  [  j  ]  ; 
arx  [  index  ]  [  i  ]  =  xmean  [  i  ]  +  sigma  *  sum  ; 
} 
return   population  [  index  ]  =  genoPhenoTransformation  (  arx  [  index  ]  ,  population  [  index  ]  )  ; 
} 










private   void   updateDistribution  (  double  [  ]  [  ]  population  ,  double  [  ]  functionValues  )  { 
arx  =  phenoGenoTransformation  (  population  ,  null  )  ; 
updateDistribution  (  functionValues  )  ; 
} 



public   void   updateDistribution  (  double  [  ]  functionValues  )  { 
if  (  state  ==  3  )  { 
error  (  "updateDistribution() was already called"  )  ; 
} 
if  (  functionValues  .  length  !=  sp  .  getLambda  (  )  )  error  (  "argument double[] funcionValues.length="  +  functionValues  .  length  +  "!="  +  "lambda="  +  sp  .  getLambda  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  sp  .  getLambda  (  )  ;  ++  i  )  { 
fit  .  raw  [  i  ]  .  val  =  functionValues  [  i  ]  ; 
fit  .  raw  [  i  ]  .  i  =  i  ; 
} 
counteval  +=  sp  .  getLambda  (  )  ; 
recentFunctionValue  =  math  .  min  (  fit  .  raw  )  .  val  ; 
recentMaxFunctionValue  =  math  .  max  (  fit  .  raw  )  .  val  ; 
recentMinFunctionValue  =  math  .  min  (  fit  .  raw  )  .  val  ; 
updateDistribution  (  )  ; 
} 

private   void   updateDistribution  (  )  { 
int   i  ,  j  ,  k  ,  iNk  ,  hsig  ; 
double   sum  ,  tfac  ; 
double   psxps  ; 
if  (  state  ==  3  )  { 
error  (  "updateDistribution() was already called"  )  ; 
} 
Arrays  .  sort  (  fit  .  raw  ,  fit  .  raw  [  0  ]  )  ; 
for  (  iNk  =  0  ;  iNk  <  sp  .  getLambda  (  )  ;  ++  iNk  )  { 
fit  .  fitness  [  iNk  ]  .  val  =  fit  .  raw  [  iNk  ]  .  val  ; 
fit  .  fitness  [  iNk  ]  .  i  =  fit  .  raw  [  iNk  ]  .  i  ; 
} 
for  (  i  =  fit  .  history  .  length  -  1  ;  i  >  0  ;  --  i  )  fit  .  history  [  i  ]  =  fit  .  history  [  i  -  1  ]  ; 
fit  .  history  [  0  ]  =  fit  .  raw  [  0  ]  .  val  ; 
updateBestEver  (  arx  [  fit  .  raw  [  0  ]  .  i  ]  ,  fit  .  raw  [  0  ]  .  val  ,  counteval  -  sp  .  getLambda  (  )  +  fit  .  raw  [  0  ]  .  i  +  1  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
xold  [  i  ]  =  xmean  [  i  ]  ; 
xmean  [  i  ]  =  0.  ; 
for  (  iNk  =  0  ;  iNk  <  sp  .  getMu  (  )  ;  ++  iNk  )  xmean  [  i  ]  +=  sp  .  getWeights  (  )  [  iNk  ]  *  arx  [  fit  .  fitness  [  iNk  ]  .  i  ]  [  i  ]  ; 
BDz  [  i  ]  =  Math  .  sqrt  (  sp  .  getMueff  (  )  )  *  (  xmean  [  i  ]  -  xold  [  i  ]  )  /  sigma  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
for  (  j  =  0  ,  sum  =  0.  ;  j  <  N  ;  ++  j  )  sum  +=  B  [  j  ]  [  i  ]  *  BDz  [  j  ]  ; 
artmp  [  i  ]  =  sum  /  diagD  [  i  ]  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
for  (  j  =  0  ,  sum  =  0.  ;  j  <  N  ;  ++  j  )  sum  +=  B  [  i  ]  [  j  ]  *  artmp  [  j  ]  ; 
ps  [  i  ]  =  (  1.  -  sp  .  getCs  (  )  )  *  ps  [  i  ]  +  Math  .  sqrt  (  sp  .  getCs  (  )  *  (  2.  -  sp  .  getCs  (  )  )  )  *  sum  ; 
} 
psxps  =  0  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  psxps  +=  ps  [  i  ]  *  ps  [  i  ]  ; 
hsig  =  0  ; 
if  (  Math  .  sqrt  (  psxps  )  /  Math  .  sqrt  (  1.  -  Math  .  pow  (  1.  -  sp  .  getCs  (  )  ,  2.  *  countiter  )  )  /  sp  .  chiN  <  1.4  +  2.  /  (  N  +  1.  )  )  { 
hsig  =  1  ; 
} 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
pc  [  i  ]  =  (  1.  -  sp  .  getCc  (  )  )  *  pc  [  i  ]  +  hsig  *  Math  .  sqrt  (  sp  .  getCc  (  )  *  (  2.  -  sp  .  getCc  (  )  )  )  *  BDz  [  i  ]  ; 
} 
if  (  iniphase  &&  countiter  >  Math  .  min  (  1  /  sp  .  getCs  (  )  ,  1  +  N  /  sp  .  getMucov  (  )  )  )  if  (  psxps  /  sp  .  getDamps  (  )  /  (  1.  -  Math  .  pow  (  (  1.  -  sp  .  getCs  (  )  )  ,  countiter  )  )  <  N  *  1.05  )  iniphase  =  false  ; 
if  (  11  <  3  &&  psxps  /  N  >  1.5  +  10  *  Math  .  sqrt  (  2.  /  N  )  &&  fit  .  history  [  0  ]  >  fit  .  history  [  1  ]  &&  fit  .  history  [  0  ]  >  fit  .  history  [  2  ]  )  { 
infoVerbose  (  countiter  +  ": remove momentum "  +  psxps  /  N  +  " "  +  ps  [  0  ]  +  " "  +  sigma  )  ; 
tfac  =  Math  .  sqrt  (  (  1  +  Math  .  max  (  0  ,  Math  .  log  (  psxps  /  N  )  )  )  *  N  /  psxps  )  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  ps  [  i  ]  *=  tfac  ; 
psxps  *=  tfac  *  tfac  ; 
} 
if  (  sp  .  getCcov  (  )  >  0  &&  iniphase  ==  false  )  { 
++  countCupdatesSinceEigenupdate  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  for  (  j  =  0  ;  j  <=  i  ;  ++  j  )  { 
C  [  i  ]  [  j  ]  =  (  1  -  sp  .  getCcov  (  )  )  *  C  [  i  ]  [  j  ]  +  sp  .  getCcov  (  )  *  (  1.  /  sp  .  getMucov  (  )  )  *  (  pc  [  i  ]  *  pc  [  j  ]  +  (  1  -  hsig  )  *  sp  .  getCc  (  )  *  (  2.  -  sp  .  getCc  (  )  )  *  C  [  i  ]  [  j  ]  )  ; 
for  (  k  =  0  ;  k  <  sp  .  getMu  (  )  ;  ++  k  )  { 
C  [  i  ]  [  j  ]  +=  sp  .  getCcov  (  )  *  (  1  -  1.  /  sp  .  getMucov  (  )  )  *  sp  .  getWeights  (  )  [  k  ]  *  (  arx  [  fit  .  fitness  [  k  ]  .  i  ]  [  i  ]  -  xold  [  i  ]  )  *  (  arx  [  fit  .  fitness  [  k  ]  .  i  ]  [  j  ]  -  xold  [  j  ]  )  /  sigma  /  sigma  ; 
} 
} 
maxsqrtdiagC  =  Math  .  sqrt  (  math  .  max  (  math  .  diag  (  C  )  )  )  ; 
minsqrtdiagC  =  Math  .  sqrt  (  math  .  min  (  math  .  diag  (  C  )  )  )  ; 
} 
sigma  *=  Math  .  exp  (  (  (  Math  .  sqrt  (  psxps  )  /  sp  .  chiN  )  -  1  )  *  sp  .  getCs  (  )  /  sp  .  getDamps  (  )  )  ; 
state  =  3  ; 
} 








double  [  ]  assignNew  (  double  [  ]  rhs  ,  double  [  ]  lhs  )  { 
assert   rhs  !=  null  ; 
if  (  lhs  !=  null  &&  lhs  !=  rhs  &&  lhs  .  length  ==  rhs  .  length  )  for  (  int   i  =  0  ;  i  <  lhs  .  length  ;  ++  i  )  lhs  [  i  ]  =  rhs  [  i  ]  ;  else   lhs  =  rhs  .  clone  (  )  ; 
return   lhs  ; 
} 

void   updateBestEver  (  double  [  ]  x  ,  double   fitness  ,  long   eval  )  { 
if  (  fitness  <  bestever_fit  ||  Double  .  isNaN  (  bestever_fit  )  )  { 
bestever_fit  =  fitness  ; 
bestever_eval  =  eval  ; 
bestever_x  =  assignNew  (  x  ,  bestever_x  )  ; 
} 
} 





public   double   getAxisRatio  (  )  { 
return   axisratio  ; 
} 









public   CMASolution   getBestSolution  (  )  { 
return   new   CMASolution  (  bestever_x  ,  bestever_fit  ,  bestever_eval  )  ; 
} 






public   CMASolution   setFitnessOfMeanX  (  double   fitness  )  { 
xmean_fit  =  fitness  ; 
++  counteval  ; 
updateBestEver  (  xmean  ,  fitness  ,  counteval  )  ; 
return   new   CMASolution  (  bestever_x  ,  bestever_fit  ,  bestever_eval  )  ; 
} 






public   double  [  ]  getBestX  (  )  { 
if  (  state  <  0  )  return   null  ; 
return   bestever_x  .  clone  (  )  ; 
} 





public   double   getBestFunctionValue  (  )  { 
if  (  state  <  0  )  return   Double  .  NaN  ; 
return   bestever_fit  ; 
} 

public   long   getBestEvaluationNumber  (  )  { 
return   bestever_eval  ; 
} 











public   ISolutionPoint   getBestRecentSolution  (  )  { 
return   new   CMASolution  (  genoPhenoTransformation  (  arx  [  fit  .  raw  [  0  ]  .  i  ]  ,  null  )  ,  fit  .  raw  [  0  ]  .  val  ,  counteval  -  sp  .  getLambda  (  )  +  fit  .  raw  [  0  ]  .  i  +  1  )  ; 
} 





public   double  [  ]  getBestRecentX  (  )  { 
return   genoPhenoTransformation  (  arx  [  fit  .  raw  [  0  ]  .  i  ]  ,  null  )  ; 
} 








public   double   getBestRecentFunctionValue  (  )  { 
return   recentMinFunctionValue  ; 
} 





public   double   getWorstRecentFunctionValue  (  )  { 
return   recentMaxFunctionValue  ; 
} 












public   double  [  ]  getMeanX  (  )  { 
return   xmean  .  clone  (  )  ; 
} 

public   int   getDimension  (  )  { 
return   N  ; 
} 




public   long   getCountEval  (  )  { 
return   counteval  ; 
} 




public   long   getCountIter  (  )  { 
return   countiter  ; 
} 







public   double  [  ]  getInitialX  (  )  { 
if  (  state  <  0  )  error  (  "initiaX not yet available, init() must be called first"  )  ; 
return   initialX  .  clone  (  )  ; 
} 


public   Random   getRand  (  )  { 
return   rand  ; 
} 






public   Properties   getProperties  (  )  { 
return   properties  ; 
} 


public   long   getSeed  (  )  { 
return   seed  ; 
} 




public   long   setCountEval  (  long   c  )  { 
return   counteval  =  c  ; 
} 


public   void   setDimension  (  int   n  )  { 
if  (  (  lockDimension  >  0  ||  state  >=  0  )  &&  N  !=  n  )  error  (  "dimension cannot be changed anymore or contradicts to initialX"  )  ; 
N  =  n  ; 
} 




public   void   setTypicalX  (  double   x  )  { 
if  (  state  >=  0  )  error  (  "typical x cannot be set anymore"  )  ; 
typicalX  =  new   double  [  ]  {  x  }  ; 
} 









public   void   setTypicalX  (  double  [  ]  x  )  { 
if  (  state  >=  0  )  error  (  "typical x cannot be set anymore"  )  ; 
if  (  x  .  length  ==  1  )  { 
setTypicalX  (  x  [  0  ]  )  ; 
return  ; 
} 
if  (  N  <  1  )  setDimension  (  x  .  length  )  ; 
if  (  N  !=  x  .  length  )  error  (  "dimensions N="  +  N  +  " and input x.length="  +  x  .  length  +  "do not agree"  )  ; 
typicalX  =  new   double  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  typicalX  [  i  ]  =  x  [  i  ]  ; 
lockDimension  =  1  ; 
} 

public   void   setInitialStandardDeviation  (  double   startsigma  )  { 
if  (  state  >=  0  )  error  (  "standard deviations cannot be set anymore"  )  ; 
this  .  startsigma  =  new   double  [  ]  {  startsigma  }  ; 
} 

public   void   setInitialStandardDeviations  (  double  [  ]  startsigma  )  { 
if  (  state  >=  0  )  error  (  "standard deviations cannot be set anymore"  )  ; 
if  (  startsigma  .  length  ==  1  )  { 
setInitialStandardDeviation  (  startsigma  [  0  ]  )  ; 
return  ; 
} 
if  (  N  >  0  &&  N  !=  startsigma  .  length  )  error  (  "dimensions N="  +  N  +  " and input startsigma.length="  +  startsigma  .  length  +  "do not agree"  )  ; 
if  (  N  ==  0  )  setDimension  (  startsigma  .  length  )  ; 
assert   N  ==  startsigma  .  length  ; 
this  .  startsigma  =  startsigma  .  clone  (  )  ; 
lockDimension  =  1  ; 
} 






public   void   setInitialX  (  double   x  )  { 
if  (  state  >=  0  )  error  (  "initial x cannot be set anymore"  )  ; 
xmean  =  new   double  [  ]  {  x  }  ; 
} 










public   void   setInitialX  (  double   l  ,  double   u  )  { 
if  (  state  >=  0  )  error  (  "initial x cannot be set anymore"  )  ; 
if  (  N  <  1  )  error  (  "dimension must have been specified before"  )  ; 
xmean  =  new   double  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  xmean  .  length  ;  ++  i  )  xmean  [  i  ]  =  l  +  (  u  -  l  )  *  rand  .  nextDouble  (  )  ; 
lockDimension  =  1  ; 
} 






public   void   setInitialX  (  double  [  ]  l  ,  double  [  ]  u  )  { 
if  (  state  >=  0  )  error  (  "initial x cannot be set anymore"  )  ; 
if  (  l  .  length  !=  u  .  length  )  error  (  "length of lower and upper values disagree"  )  ; 
setDimension  (  l  .  length  )  ; 
xmean  =  new   double  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  xmean  .  length  ;  ++  i  )  xmean  [  i  ]  =  l  [  i  ]  +  (  u  [  i  ]  -  l  [  i  ]  )  *  rand  .  nextDouble  (  )  ; 
lockDimension  =  1  ; 
} 








public   void   setInitialX  (  double  [  ]  x  )  { 
if  (  state  >=  0  )  error  (  "initial x cannot be set anymore"  )  ; 
if  (  x  .  length  ==  1  )  { 
setInitialX  (  x  [  0  ]  )  ; 
return  ; 
} 
if  (  N  >  0  &&  N  !=  x  .  length  )  error  (  "dimensions do not match"  )  ; 
if  (  N  ==  0  )  setDimension  (  x  .  length  )  ; 
assert   N  ==  x  .  length  ; 
xmean  =  new   double  [  N  ]  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  xmean  [  i  ]  =  x  [  i  ]  ; 
lockDimension  =  1  ; 
} 

public   void   setRand  (  Random   rand  )  { 
this  .  rand  =  rand  ; 
} 






public   void   setSeed  (  long   seed  )  { 
if  (  state  >=  0  )  warning  (  "setting seed has no effect at this point"  )  ;  else  { 
if  (  seed  <=  0  )  seed  =  System  .  currentTimeMillis  (  )  ; 
this  .  seed  =  seed  ; 
rand  .  setSeed  (  seed  )  ; 
} 
} 




























public   String   getPrintLine  (  )  { 
String   s  ; 
if  (  state  <  0  )  s  =  new   String  (  new   PrintfFormat  (  Locale  .  US  ,  " %4d"  )  .  sprintf  (  countiter  )  +  new   PrintfFormat  (  Locale  .  US  ,  "(%2d), "  )  .  sprintf  (  0  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%6.0d: "  )  .  sprintf  (  counteval  )  )  ;  else   s  =  new   String  (  new   PrintfFormat  (  Locale  .  US  ,  " %4d"  )  .  sprintf  (  countiter  )  +  new   PrintfFormat  (  Locale  .  US  ,  "(%2d), "  )  .  sprintf  (  idxRecentOffspring  +  1  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%6.0d: "  )  .  sprintf  (  counteval  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%.16e "  )  .  sprintf  (  recentFunctionValue  )  +  new   PrintfFormat  (  Locale  .  US  ,  "(%+.0e,"  )  .  sprintf  (  getBestFunctionValue  (  )  -  recentFunctionValue  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%.0e) | "  )  .  sprintf  (  recentMaxFunctionValue  -  recentFunctionValue  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%2d:"  )  .  sprintf  (  math  .  maxidx  (  math  .  diag  (  C  )  )  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%8.1e "  )  .  sprintf  (  sigma  *  maxsqrtdiagC  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%2d:"  )  .  sprintf  (  math  .  minidx  (  math  .  diag  (  C  )  )  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%8.1e "  )  .  sprintf  (  sigma  *  minsqrtdiagC  )  +  new   PrintfFormat  (  Locale  .  US  ,  "| %6.1e "  )  .  sprintf  (  sigma  *  math  .  min  (  diagD  )  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%6.1e "  )  .  sprintf  (  sigma  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%6.2f"  )  .  sprintf  (  axisratio  )  +  new   PrintfFormat  (  Locale  .  US  ,  "   | %4.1f "  )  .  sprintf  (  (  System  .  currentTimeMillis  (  )  -  timings  .  start  )  /  1000.  )  +  new   PrintfFormat  (  Locale  .  US  ,  "%4.1f "  )  .  sprintf  (  timings  .  eigendecomposition  /  1000.  )  )  ; 
return   s  ; 
} 


public   String   getPrintAnnotation  (  )  { 
String   s  =  new   String  (  "Iteration,#Fevals: rb Function Value Delta( best ,worst) |idx: Max SD idx: Min SD  | minsigD  sigma Axisratio | time, in eig"  )  ; 
return   s  ; 
} 


public   String   helloWorld  (  )  { 
String   s  =  new   String  (  "("  +  sp  .  getMu  (  )  +  ","  +  sp  .  getLambda  (  )  +  ")-CMA-ES(mu_eff="  +  Math  .  round  (  10.  *  sp  .  getMueff  (  )  )  /  10.  +  "), Ver=\""  +  versionNumber  +  "\", dimension="  +  N  +  ", randomSeed="  +  seed  +  " ("  +  new   Date  (  )  .  toString  (  )  +  ")"  )  ; 
return   s  ; 
} 





public   void   println  (  String   s  )  { 
System  .  out  .  println  (  s  )  ; 
if  (  options  .  writeDisplayToFile  >  0  )  writeToFile  (  options  .  outputFileNamesPrefix  +  "disp"  +  ".dat"  ,  s  ,  1  )  ; 
} 




public   void   println  (  )  { 
println  (  getPrintLine  (  )  )  ; 
} 


public   void   printlnAnnotation  (  )  { 
println  (  getPrintAnnotation  (  )  )  ; 
} 





public   void   printlnHelloWorld  (  )  { 
println  (  helloWorld  (  )  )  ; 
} 

public   String   getDataRowFitness  (  )  { 
String   s  =  new   String  (  )  ; 
s  =  countiter  +  " "  +  counteval  +  " "  +  sigma  +  " "  +  axisratio  +  " "  +  bestever_fit  +  " "  ; 
if  (  mode  ==  SINGLE_MODE  )  s  +=  recentFunctionValue  +  " "  ;  else  { 
s  +=  fit  .  raw  [  0  ]  .  val  +  " "  ; 
s  +=  fit  .  raw  [  sp  .  getLambda  (  )  /  2  ]  .  val  +  " "  ; 
s  +=  fit  .  raw  [  sp  .  getLambda  (  )  -  1  ]  .  val  +  " "  ; 
s  +=  math  .  min  (  diagD  )  +  " "  +  (  math  .  maxidx  (  math  .  diag  (  C  )  )  +  1  )  +  " "  +  sigma  *  maxsqrtdiagC  +  " "  +  (  math  .  minidx  (  math  .  diag  (  C  )  )  +  1  )  +  " "  +  sigma  *  minsqrtdiagC  ; 
} 
return   s  ; 
} 

public   String   getDataRowXRecentBest  (  )  { 
int   idx  =  0  ; 
if  (  mode  ==  SINGLE_MODE  )  idx  =  idxRecentOffspring  ; 
String   s  =  new   String  (  )  ; 
s  =  countiter  +  " "  +  counteval  +  " "  +  sigma  +  " 0 "  +  (  state  ==  1  ?  Double  .  NaN  :  fit  .  raw  [  idx  ]  .  val  )  +  " "  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  { 
s  +=  arx  [  fit  .  raw  [  idx  ]  .  i  ]  [  i  ]  +  " "  ; 
} 
return   s  ; 
} 

public   String   getDataRowXMean  (  )  { 
String   s  =  new   String  (  )  ; 
s  =  countiter  +  " "  +  counteval  +  " "  +  sigma  +  " 0 0 "  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  { 
s  +=  xmean  [  i  ]  +  " "  ; 
} 
return   s  ; 
} 


public   String   getDataRowAxlen  (  )  { 
String   s  =  new   String  (  )  ; 
s  =  countiter  +  " "  +  counteval  +  " "  +  sigma  +  " "  +  axisratio  +  " "  +  maxsqrtdiagC  /  minsqrtdiagC  +  " "  ; 
double  [  ]  tmp  =  (  double  [  ]  )  diagD  .  clone  (  )  ; 
java  .  util  .  Arrays  .  sort  (  tmp  )  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  { 
s  +=  tmp  [  i  ]  +  " "  ; 
} 
return   s  ; 
} 

public   String   getDataRowStddev  (  )  { 
String   s  =  new   String  (  )  ; 
s  =  countiter  +  " "  +  counteval  +  " "  +  sigma  +  " "  +  (  1  +  math  .  maxidx  (  math  .  diag  (  C  )  )  )  +  " "  +  (  1  +  math  .  minidx  (  math  .  diag  (  C  )  )  )  +  " "  ; 
for  (  int   i  =  0  ;  i  <  N  ;  ++  i  )  { 
s  +=  sigma  *  Math  .  sqrt  (  C  [  i  ]  [  i  ]  )  +  " "  ; 
} 
return   s  ; 
} 







public   String   getDataC  (  )  { 
int   i  ,  j  ; 
String   s  =  new   String  (  )  ; 
s  =  "%# "  +  countiter  +  " "  +  counteval  +  " "  +  sigma  +  "\n"  ; 
for  (  i  =  0  ;  i  <  N  ;  ++  i  )  { 
for  (  j  =  0  ;  j  <  i  ;  ++  j  )  s  +=  C  [  i  ]  [  j  ]  /  Math  .  sqrt  (  C  [  i  ]  [  i  ]  *  C  [  j  ]  [  j  ]  )  +  " "  ; 
for  (  j  =  i  ;  j  <  N  ;  ++  j  )  s  +=  sigma  *  sigma  *  C  [  i  ]  [  j  ]  +  " "  ; 
s  +=  "\n"  ; 
} 
return   s  ; 
} 

private   String  [  ]  fileswritten  =  new   String  [  ]  {  ""  }  ; 







public   void   writeToFile  (  String   filename  ,  String   data  ,  int   flgAppend  )  { 
boolean   appendflag  =  flgAppend  >  0  ; 
for  (  int   i  =  0  ;  !  appendflag  &&  i  <  fileswritten  .  length  ;  ++  i  )  if  (  filename  .  equals  (  fileswritten  [  i  ]  )  )  { 
appendflag  =  true  ; 
} 
java  .  io  .  PrintWriter   out  =  null  ; 
try  { 
out  =  new   java  .  io  .  PrintWriter  (  new   java  .  io  .  FileWriter  (  filename  ,  appendflag  )  )  ; 
out  .  println  (  data  )  ; 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  java  .  io  .  FileNotFoundException   e  )  { 
warning  (  "Could not find file '"  +  filename  +  "'(FileNotFoundException)"  )  ; 
}  catch  (  java  .  io  .  IOException   e  )  { 
warning  (  "Could not open/write to file "  +  filename  )  ; 
}  finally  { 
if  (  out  !=  null  )  out  .  close  (  )  ; 
} 
if  (  appendflag  ==  false  )  { 
String   s  [  ]  =  fileswritten  ; 
fileswritten  =  new   String  [  fileswritten  .  length  +  1  ]  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  ;  ++  i  )  fileswritten  [  i  ]  =  s  [  i  ]  ; 
fileswritten  [  fileswritten  .  length  -  1  ]  =  new   String  (  filename  )  ; 
} 
} 
























public   void   writeToDefaultFiles  (  )  { 
writeToDefaultFiles  (  options  .  outputFileNamesPrefix  )  ; 
} 










public   void   writeToDefaultFiles  (  int   flgForce  )  { 
if  (  flgForce  >  0  &&  countiter  !=  citerlastwritten  )  citerlastwritten  =  -  1  ; 
if  (  flgForce  >=  2  )  citerlastwritten  =  -  1  ; 
writeToDefaultFiles  (  options  .  outputFileNamesPrefix  )  ; 
} 






public   void   writeToDefaultFiles  (  String   fileNamePrefix  )  { 
if  (  options  .  maxTimeFractionForWriteToDefaultFiles  <  0  )  return  ; 
if  (  citerlastwritten  >=  0  )  { 
if  (  state  <  1  )  return  ; 
if  (  countiter  ==  citerlastwritten  )  return  ; 
if  (  options  .  maxTimeFractionForWriteToDefaultFiles  <=  0  )  return  ; 
if  (  countiter  >  4  &&  stopConditions  .  index  ==  0  &&  countiter  -  citerlastwritten  -  1  <  2.  *  (  countiter  -  countwritten  +  1.  )  /  (  countwritten  +  1.  )  &&  timings  .  writedefaultfiles  >  options  .  maxTimeFractionForWriteToDefaultFiles  *  (  System  .  currentTimeMillis  (  )  -  timings  .  start  )  )  return  ; 
} 
long   firsttime  =  System  .  currentTimeMillis  (  )  ; 
writeToFile  (  fileNamePrefix  +  "fit.dat"  ,  getDataRowFitness  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "xmean.dat"  ,  getDataRowXMean  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "xrecentbest.dat"  ,  getDataRowXRecentBest  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "stddev.dat"  ,  getDataRowStddev  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "axlen.dat"  ,  getDataRowAxlen  (  )  ,  1  )  ; 
timings  .  writedefaultfiles  +=  System  .  currentTimeMillis  (  )  -  firsttime  ; 
if  (  countiter  <  3  )  timings  .  writedefaultfiles  =  0  ; 
++  countwritten  ; 
citerlastwritten  =  countiter  ; 
} 





public   void   writeToDefaultFilesHeaders  (  int   flgAppend  )  { 
writeToDefaultFilesHeaders  (  options  .  outputFileNamesPrefix  ,  flgAppend  )  ; 
} 






public   void   writeToDefaultFilesHeaders  (  String   fileNamePrefix  ,  int   flgAppend  )  { 
if  (  options  .  maxTimeFractionForWriteToDefaultFiles  <  0  )  return  ; 
String   s  =  "(randomSeed="  +  seed  +  ", "  +  new   Date  (  )  .  toString  (  )  +  ")\n"  ; 
writeToFile  (  fileNamePrefix  +  "fit.dat"  ,  "%# iteration evaluations sigma axisratio fitness_of(bestever best median worst) mindii "  +  "idxmaxSD maxSD idxminSD minSD "  +  s  ,  flgAppend  )  ; 
writeToFile  (  fileNamePrefix  +  "xmean.dat"  ,  "%# iteration evaluations sigma void void mean(1...dimension) "  +  s  ,  flgAppend  )  ; 
if  (  state  ==  0  )  writeToFile  (  fileNamePrefix  +  "xmean.dat"  ,  getDataRowXMean  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "xrecentbest.dat"  ,  "%# iteration evaluations sigma void fitness_of_recent_best x_of_recent_best(1...dimension) "  +  s  ,  flgAppend  )  ; 
writeToFile  (  fileNamePrefix  +  "stddev.dat"  ,  "%# iteration evaluations sigma idxmaxSD idxminSD SDs=sigma*sqrt(diag(C)) "  +  s  ,  flgAppend  )  ; 
if  (  state  ==  0  )  writeToFile  (  fileNamePrefix  +  "stddev.dat"  ,  getDataRowStddev  (  )  ,  1  )  ; 
writeToFile  (  fileNamePrefix  +  "axlen.dat"  ,  "%# iteration evaluations sigma axisratio stddevratio sort(diag(D)) (square roots of eigenvalues of C) "  +  s  ,  flgAppend  )  ; 
if  (  state  ==  0  )  writeToFile  (  fileNamePrefix  +  "axlen.dat"  ,  getDataRowAxlen  (  )  ,  1  )  ; 
} 





public   class   CMAException   extends   RuntimeException  { 

private   static   final   long   serialVersionUID  =  1L  ; 

CMAException  (  String   s  )  { 
super  (  s  )  ; 
} 
} 
} 

class   IntDouble   implements   Comparator  <  IntDouble  >  { 

int   i  ; 

double   val  ; 

public   IntDouble  (  double   d  ,  int   i  )  { 
this  .  val  =  d  ; 
this  .  i  =  i  ; 
} 

public   IntDouble  (  double   d  )  { 
this  .  val  =  d  ; 
} 

public   IntDouble  (  )  { 
} 

public   int   compare  (  IntDouble   o1  ,  IntDouble   o2  )  { 
if  (  o1  .  val  <  o2  .  val  )  return  -  1  ; 
if  (  o1  .  val  >  o2  .  val  )  return   1  ; 
if  (  o1  .  i  <  o2  .  i  )  return  -  1  ; 
if  (  o1  .  i  >  o2  .  i  )  return   1  ; 
return   0  ; 
} 

public   boolean   equals  (  IntDouble   o1  ,  IntDouble   o2  )  { 
if  (  o1  .  compare  (  o1  ,  o2  )  ==  0  )  return   true  ; 
return   false  ; 
} 
} 

