package   org  .  smartgrape  .  cofiltering  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   gnu  .  trove  .  *  ; 
import   org  .  smartgrape  .  tools  .  *  ; 





public   class   SGSlopeOneRecommender  { 

public   static   char   fSep  =  File  .  separatorChar  ; 

static   String   CustomerRatingFileName  =  "CustomerRatingBinaryFile.txt"  ; 

static   String   MovieIndexFileName  =  "MovieIndex.txt"  ; 

static   String   MovieRatingFileName  =  "MovieRatingBinaryFile.txt"  ; 

static   String   CustIndexFileName  =  "CustomerIndex.txt"  ; 

static   TShortObjectHashMap   MovieLimitsTHash  ; 

static   TIntObjectHashMap   CustomerLimitsTHash  =  null  ; 

static   TShortObjectHashMap   CustomersAndRatingsPerMovie  ; 

static   TIntObjectHashMap   MoviesAndRatingsPerCustomer  ; 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
String   completePath  =  null  ; 
String   predictionFileName  =  null  ; 
String   slopeOneStatsFolderName  =  null  ; 
String   submissionFileName  =  null  ; 
if  (  args  .  length  ==  4  )  { 
completePath  =  args  [  0  ]  ; 
predictionFileName  =  args  [  1  ]  ; 
slopeOneStatsFolderName  =  args  [  2  ]  ; 
submissionFileName  =  args  [  3  ]  ; 
}  else  { 
System  .  out  .  println  (  "Please provide complete path to training_set parent folder as an argument. EXITING"  )  ; 
System  .  exit  (  0  )  ; 
} 
File   inputFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  MovieIndexFileName  )  ; 
FileChannel   inC  =  new   FileInputStream  (  inputFile  )  .  getChannel  (  )  ; 
int   filesize  =  (  int  )  inC  .  size  (  )  ; 
ByteBuffer   mappedfile  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  filesize  )  ; 
MovieLimitsTHash  =  new   TShortObjectHashMap  (  17770  ,  1  )  ; 
int   i  =  0  ,  totalcount  =  0  ; 
short   movie  ; 
int   startIndex  ,  endIndex  ; 
TIntArrayList   a  ; 
while  (  mappedfile  .  hasRemaining  (  )  )  { 
movie  =  mappedfile  .  getShort  (  )  ; 
startIndex  =  mappedfile  .  getInt  (  )  ; 
endIndex  =  mappedfile  .  getInt  (  )  ; 
a  =  new   TIntArrayList  (  2  )  ; 
a  .  add  (  startIndex  )  ; 
a  .  add  (  endIndex  )  ; 
MovieLimitsTHash  .  put  (  movie  ,  a  )  ; 
} 
inC  .  close  (  )  ; 
mappedfile  =  null  ; 
System  .  out  .  println  (  "Loaded movie index hash"  )  ; 
if  (  (  CustomersAndRatingsPerMovie  ==  null  )  ||  CustomersAndRatingsPerMovie  .  size  (  )  <=  0  )  { 
CustomersAndRatingsPerMovie  =  InitializeCustomerRatingsForMovieHashMap  (  completePath  ,  MovieLimitsTHash  )  ; 
System  .  out  .  println  (  "Populated CustomersAndRatingsPerMovie hashmap"  )  ; 
} 
buildDiffStatistics  (  completePath  ,  "SlopeOneData"  ,  "Movie2MovieSlopeOneData.txt"  )  ; 
buildPerMovieDiffBinary  (  completePath  ,  "SlopeOneData"  ,  "Movie2MovieSlopeOneData.txt"  )  ; 
CustomersAndRatingsPerMovie  .  clear  (  )  ; 
CustomersAndRatingsPerMovie  =  null  ; 
MovieLimitsTHash  .  clear  (  )  ; 
MovieLimitsTHash  =  null  ; 
inputFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  CustIndexFileName  )  ; 
inC  =  new   FileInputStream  (  inputFile  )  .  getChannel  (  )  ; 
filesize  =  (  int  )  inC  .  size  (  )  ; 
mappedfile  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  filesize  )  ; 
CustomerLimitsTHash  =  new   TIntObjectHashMap  (  480189  ,  1  )  ; 
int   custid  ; 
while  (  mappedfile  .  hasRemaining  (  )  )  { 
custid  =  mappedfile  .  getInt  (  )  ; 
startIndex  =  mappedfile  .  getInt  (  )  ; 
endIndex  =  mappedfile  .  getInt  (  )  ; 
a  =  new   TIntArrayList  (  2  )  ; 
a  .  add  (  startIndex  )  ; 
a  .  add  (  endIndex  )  ; 
CustomerLimitsTHash  .  put  (  custid  ,  a  )  ; 
} 
inC  .  close  (  )  ; 
mappedfile  =  null  ; 
System  .  out  .  println  (  "Loaded customer index hash"  )  ; 
if  (  (  MoviesAndRatingsPerCustomer  ==  null  )  ||  MoviesAndRatingsPerCustomer  .  size  (  )  <=  0  )  { 
MoviesAndRatingsPerCustomer  =  InitializeMovieRatingsForCustomerHashMap  (  completePath  ,  CustomerLimitsTHash  )  ; 
System  .  out  .  println  (  "Populated CustomersAndRatingsPerMovie hashmap: "  )  ; 
} 
boolean   success  =  true  ; 
success  =  predictDataSet  (  completePath  ,  "Qualifying"  ,  predictionFileName  ,  slopeOneStatsFolderName  )  ; 
if  (  success  )  { 
System  .  out  .  println  (  "Binary prediction file successfully created"  )  ; 
}  else  { 
System  .  out  .  println  (  "Binary prediction file creation failed"  )  ; 
} 
success  =  DataUtilities  .  prepareSubmissionFile  (  completePath  ,  predictionFileName  ,  submissionFileName  )  ; 
if  (  success  )  { 
System  .  out  .  println  (  "Prediction file for submission to Netflix successfully created"  )  ; 
}  else  { 
System  .  out  .  println  (  "Prediction file creation for submission to Netflix failed"  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 


























public   static   void   buildDiffStatistics  (  String   completePath  ,  String   slopeOneDataFolderName  ,  String   slopeOneDataFileName  )  { 
try  { 
File   outfile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  slopeOneDataFolderName  +  fSep  +  slopeOneDataFileName  )  ; 
FileChannel   outC  =  new   FileOutputStream  (  outfile  ,  true  )  .  getChannel  (  )  ; 
short  [  ]  movies  =  CustomersAndRatingsPerMovie  .  keys  (  )  ; 
Arrays  .  sort  (  movies  )  ; 
int   noMovies  =  movies  .  length  ; 
for  (  int   i  =  0  ;  i  <  noMovies  -  1  ;  i  ++  )  { 
short   movie1  =  movies  [  i  ]  ; 
System  .  out  .  println  (  "Processing movie: "  +  movie1  )  ; 
TIntByteHashMap   testMovieCustAndRatingsMap  =  (  TIntByteHashMap  )  CustomersAndRatingsPerMovie  .  get  (  movie1  )  ; 
int  [  ]  customers1  =  testMovieCustAndRatingsMap  .  keys  (  )  ; 
Arrays  .  sort  (  customers1  )  ; 
for  (  int   j  =  i  +  1  ;  j  <  noMovies  ;  j  ++  )  { 
float   diffRating  =  0  ; 
int   count  =  0  ; 
short   movie2  =  movies  [  j  ]  ; 
TIntByteHashMap   otherMovieCustAndRatingsMap  =  (  TIntByteHashMap  )  CustomersAndRatingsPerMovie  .  get  (  movie2  )  ; 
int  [  ]  customers2  =  otherMovieCustAndRatingsMap  .  keys  (  )  ; 
TIntArrayList   intersectSet  =  CustOverLapForTwoMoviesCustom  (  customers1  ,  customers2  )  ; 
if  (  (  intersectSet  .  size  (  )  ==  0  )  ||  (  intersectSet  ==  null  )  )  { 
count  =  0  ; 
diffRating  =  0  ; 
}  else  { 
count  =  intersectSet  .  size  (  )  ; 
for  (  int   l  =  0  ;  l  <  count  ;  l  ++  )  { 
int   commonCust  =  intersectSet  .  getQuick  (  l  )  ; 
diffRating  +=  testMovieCustAndRatingsMap  .  get  (  commonCust  )  ; 
diffRating  -=  otherMovieCustAndRatingsMap  .  get  (  commonCust  )  ; 
} 
} 
ByteBuffer   buf  =  ByteBuffer  .  allocate  (  12  )  ; 
buf  .  putShort  (  movie1  )  ; 
buf  .  putShort  (  movie2  )  ; 
buf  .  putInt  (  count  )  ; 
buf  .  putFloat  (  diffRating  )  ; 
buf  .  flip  (  )  ; 
outC  .  write  (  buf  )  ; 
buf  .  clear  (  )  ; 
} 
} 
outC  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 


























public   static   void   buildPerMovieDiffBinary  (  String   completePath  ,  String   slopeOneDataFolderName  ,  String   slopeOneDataFileName  )  { 
try  { 
File   inFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  slopeOneDataFolderName  +  fSep  +  slopeOneDataFileName  )  ; 
FileChannel   inC  =  new   FileInputStream  (  inFile  )  .  getChannel  (  )  ; 
for  (  int   i  =  1  ;  i  <=  17770  ;  i  ++  )  { 
File   outFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  slopeOneDataFolderName  +  fSep  +  "Movie-"  +  i  +  "-SlopeOneData.txt"  )  ; 
FileChannel   outC  =  new   FileOutputStream  (  outFile  )  .  getChannel  (  )  ; 
ByteBuffer   buf  =  ByteBuffer  .  allocate  (  17770  *  10  )  ; 
for  (  int   j  =  1  ;  j  <  i  ;  j  ++  )  { 
ByteBuffer   bbuf  =  ByteBuffer  .  allocate  (  12  )  ; 
inC  .  position  (  (  17769  *  17770  *  6  )  -  (  (  17769  -  (  j  -  1  )  )  *  (  17770  -  (  j  -  1  )  )  *  6  )  +  (  i  -  j  -  1  )  *  12  )  ; 
inC  .  read  (  bbuf  )  ; 
bbuf  .  flip  (  )  ; 
buf  .  putShort  (  bbuf  .  getShort  (  )  )  ; 
bbuf  .  getShort  (  )  ; 
buf  .  putInt  (  bbuf  .  getInt  (  )  )  ; 
buf  .  putFloat  (  -  bbuf  .  getFloat  (  )  )  ; 
} 
buf  .  putShort  (  new   Integer  (  i  )  .  shortValue  (  )  )  ; 
buf  .  putInt  (  0  )  ; 
buf  .  putFloat  (  0.0f  )  ; 
ByteBuffer   remainingBuf  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  (  17769  *  17770  *  6  )  -  (  (  17769  -  (  i  -  1  )  )  *  (  17770  -  (  i  -  1  )  )  *  6  )  ,  (  17770  -  i  )  *  12  )  ; 
while  (  remainingBuf  .  hasRemaining  (  )  )  { 
remainingBuf  .  getShort  (  )  ; 
buf  .  putShort  (  remainingBuf  .  getShort  (  )  )  ; 
buf  .  putInt  (  remainingBuf  .  getInt  (  )  )  ; 
buf  .  putFloat  (  remainingBuf  .  getFloat  (  )  )  ; 
} 
buf  .  flip  (  )  ; 
outC  .  write  (  buf  )  ; 
buf  .  clear  (  )  ; 
outC  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 










public   static   TIntArrayList   CustOverLapForTwoMoviesCustom  (  int  [  ]  Customers1  ,  int  [  ]  Customers2  )  { 
try  { 
Arrays  .  sort  (  Customers2  )  ; 
TIntArrayList   returnVal  =  new   TIntArrayList  (  )  ; 
int   size1  =  Customers1  .  length  ; 
int   size2  =  Customers2  .  length  ; 
if  (  size1  >  size2  )  { 
for  (  int   i  =  0  ;  i  <  size1  ;  i  ++  )  { 
if  (  Arrays  .  binarySearch  (  Customers2  ,  Customers1  [  i  ]  )  >  0  )  { 
returnVal  .  add  (  Customers1  [  i  ]  )  ; 
} 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  size2  ;  i  ++  )  { 
if  (  Arrays  .  binarySearch  (  Customers1  ,  Customers2  [  i  ]  )  >  0  )  { 
returnVal  .  add  (  Customers2  [  i  ]  )  ; 
} 
} 
} 
return   returnVal  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 















public   static   boolean   predictDataSet  (  String   completePath  ,  String   Type  ,  String   predictionOutputFileName  ,  String   slopeOneDataFolderName  )  { 
try  { 
if  (  Type  .  equalsIgnoreCase  (  "Qualifying"  )  )  { 
File   inputFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  "CompleteQualifyingDataInByteFormat.txt"  )  ; 
FileChannel   inC  =  new   FileInputStream  (  inputFile  )  .  getChannel  (  )  ; 
int   filesize  =  (  int  )  inC  .  size  (  )  ; 
TShortObjectHashMap   qualMap  =  new   TShortObjectHashMap  (  17770  ,  1  )  ; 
ByteBuffer   qualmappedfile  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  filesize  )  ; 
while  (  qualmappedfile  .  hasRemaining  (  )  )  { 
short   movie  =  qualmappedfile  .  getShort  (  )  ; 
int   customer  =  qualmappedfile  .  getInt  (  )  ; 
if  (  qualMap  .  containsKey  (  movie  )  )  { 
TIntArrayList   arr  =  (  TIntArrayList  )  qualMap  .  get  (  movie  )  ; 
arr  .  add  (  customer  )  ; 
qualMap  .  put  (  movie  ,  arr  )  ; 
}  else  { 
TIntArrayList   arr  =  new   TIntArrayList  (  )  ; 
arr  .  add  (  customer  )  ; 
qualMap  .  put  (  movie  ,  arr  )  ; 
} 
} 
System  .  out  .  println  (  "Populated qualifying hashmap"  )  ; 
File   outFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  predictionOutputFileName  )  ; 
FileChannel   outC  =  new   FileOutputStream  (  outFile  )  .  getChannel  (  )  ; 
ByteBuffer   buf  ; 
TShortObjectHashMap   movieDiffStats  ; 
double   finalPrediction  ; 
short  [  ]  movies  =  qualMap  .  keys  (  )  ; 
Arrays  .  sort  (  movies  )  ; 
for  (  int   i  =  0  ;  i  <  movies  .  length  ;  i  ++  )  { 
short   movieToProcess  =  movies  [  i  ]  ; 
movieDiffStats  =  loadMovieDiffStats  (  completePath  ,  movieToProcess  ,  slopeOneDataFolderName  )  ; 
System  .  out  .  println  (  movieDiffStats  .  size  (  )  )  ; 
TIntArrayList   customersToProcess  =  (  TIntArrayList  )  qualMap  .  get  (  movieToProcess  )  ; 
for  (  int   j  =  0  ;  j  <  customersToProcess  .  size  (  )  ;  j  ++  )  { 
int   customerToProcess  =  customersToProcess  .  getQuick  (  j  )  ; 
finalPrediction  =  predictSlopeOneRating  (  customerToProcess  ,  movieDiffStats  )  ; 
if  (  finalPrediction  ==  finalPrediction  )  { 
if  (  finalPrediction  <  1.0  )  finalPrediction  =  1.0  ;  else   if  (  finalPrediction  >  5.0  )  finalPrediction  =  5.0  ; 
}  else   finalPrediction  =  GetAveragePrediction  (  movieToProcess  )  ; 
buf  =  ByteBuffer  .  allocate  (  10  )  ; 
buf  .  putShort  (  movieToProcess  )  ; 
buf  .  putInt  (  customerToProcess  )  ; 
buf  .  putFloat  (  new   Double  (  finalPrediction  )  .  floatValue  (  )  )  ; 
buf  .  flip  (  )  ; 
outC  .  write  (  buf  )  ; 
} 
} 
outC  .  close  (  )  ; 
return   true  ; 
}  else   if  (  Type  .  equalsIgnoreCase  (  "Probe"  )  )  { 
File   inputFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  "CompleteProbeDataInByteFormat.txt"  )  ; 
FileChannel   inC  =  new   FileInputStream  (  inputFile  )  .  getChannel  (  )  ; 
int   filesize  =  (  int  )  inC  .  size  (  )  ; 
TShortObjectHashMap   probeMap  =  new   TShortObjectHashMap  (  17770  ,  1  )  ; 
ByteBuffer   probemappedfile  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  filesize  )  ; 
while  (  probemappedfile  .  hasRemaining  (  )  )  { 
short   movie  =  probemappedfile  .  getShort  (  )  ; 
int   customer  =  probemappedfile  .  getInt  (  )  ; 
byte   rating  =  probemappedfile  .  get  (  )  ; 
if  (  probeMap  .  containsKey  (  movie  )  )  { 
TIntByteHashMap   actualRatings  =  (  TIntByteHashMap  )  probeMap  .  get  (  movie  )  ; 
actualRatings  .  put  (  customer  ,  rating  )  ; 
probeMap  .  put  (  movie  ,  actualRatings  )  ; 
}  else  { 
TIntByteHashMap   actualRatings  =  new   TIntByteHashMap  (  )  ; 
actualRatings  .  put  (  customer  ,  rating  )  ; 
probeMap  .  put  (  movie  ,  actualRatings  )  ; 
} 
} 
System  .  out  .  println  (  "Populated probe hashmap"  )  ; 
File   outFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  predictionOutputFileName  )  ; 
FileChannel   outC  =  new   FileOutputStream  (  outFile  )  .  getChannel  (  )  ; 
ByteBuffer   buf  ; 
double   finalPrediction  ; 
TShortObjectHashMap   movieDiffStats  ; 
short  [  ]  movies  =  probeMap  .  keys  (  )  ; 
Arrays  .  sort  (  movies  )  ; 
for  (  int   i  =  0  ;  i  <  movies  .  length  ;  i  ++  )  { 
short   movieToProcess  =  movies  [  i  ]  ; 
movieDiffStats  =  loadMovieDiffStats  (  completePath  ,  movieToProcess  ,  slopeOneDataFolderName  )  ; 
TIntByteHashMap   custRatingsToProcess  =  (  TIntByteHashMap  )  probeMap  .  get  (  movieToProcess  )  ; 
TIntArrayList   customersToProcess  =  new   TIntArrayList  (  custRatingsToProcess  .  keys  (  )  )  ; 
for  (  int   j  =  0  ;  j  <  customersToProcess  .  size  (  )  ;  j  ++  )  { 
int   customerToProcess  =  customersToProcess  .  getQuick  (  j  )  ; 
byte   rating  =  custRatingsToProcess  .  get  (  customerToProcess  )  ; 
finalPrediction  =  predictSlopeOneRating  (  customerToProcess  ,  movieDiffStats  )  ; 
if  (  finalPrediction  ==  finalPrediction  )  { 
if  (  finalPrediction  <  1.0  )  finalPrediction  =  1.0  ;  else   if  (  finalPrediction  >  5.0  )  finalPrediction  =  5.0  ; 
}  else   finalPrediction  =  GetAveragePrediction  (  movieToProcess  )  ; 
buf  =  ByteBuffer  .  allocate  (  11  )  ; 
buf  .  putShort  (  movieToProcess  )  ; 
buf  .  putInt  (  customerToProcess  )  ; 
buf  .  put  (  rating  )  ; 
buf  .  putFloat  (  new   Double  (  finalPrediction  )  .  floatValue  (  )  )  ; 
buf  .  flip  (  )  ; 
outC  .  write  (  buf  )  ; 
} 
} 
outC  .  close  (  )  ; 
return   true  ; 
}  else   return   false  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
} 













public   static   float   predictSlopeOneRating  (  int   customerToProcess  ,  TShortObjectHashMap   movieDiffStats  )  { 
try  { 
float   prediction  =  0.0f  ; 
int   overlapCount  =  0  ; 
short   movie  ; 
TShortByteHashMap   custMovieAndRatingsMap  =  (  TShortByteHashMap  )  MoviesAndRatingsPerCustomer  .  get  (  customerToProcess  )  ; 
TShortByteIterator   itr  =  custMovieAndRatingsMap  .  iterator  (  )  ; 
int   noMoviesRated  =  custMovieAndRatingsMap  .  size  (  )  ; 
float   numerator  =  0  ,  denominator  =  0  ,  count  =  0  ,  diffRating  =  0  ; 
while  (  itr  .  hasNext  (  )  )  { 
itr  .  advance  (  )  ; 
movie  =  itr  .  key  (  )  ; 
slopeOneStats   SOS  =  (  slopeOneStats  )  movieDiffStats  .  get  (  movie  )  ; 
count  =  SOS  .  getCount  (  )  ; 
diffRating  =  SOS  .  getDiffRating  (  )  ; 
numerator  +=  (  itr  .  value  (  )  *  count  )  +  diffRating  ; 
denominator  +=  count  ; 
} 
return   numerator  /  denominator  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   Float  .  NaN  ; 
} 
} 












public   static   TShortObjectHashMap   loadMovieDiffStats  (  String   completePath  ,  short   movieToProcess  ,  String   slopeOneDataFolderName  )  { 
try  { 
File   inFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  slopeOneDataFolderName  +  fSep  +  "Movie-"  +  movieToProcess  +  "-MatrixData.txt"  )  ; 
FileChannel   inC  =  new   FileInputStream  (  inFile  )  .  getChannel  (  )  ; 
TShortObjectHashMap   returnMap  =  new   TShortObjectHashMap  (  17770  ,  1  )  ; 
int   size  =  (  int  )  inC  .  size  (  )  ; 
ByteBuffer   mapped  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  size  )  ; 
short   otherMovie  ; 
int   count  ; 
float   diffRating  ; 
while  (  mapped  .  hasRemaining  (  )  )  { 
otherMovie  =  mapped  .  getShort  (  )  ; 
count  =  mapped  .  getInt  (  )  ; 
diffRating  =  mapped  .  getFloat  (  )  ; 
slopeOneStats   newSOS  =  new   slopeOneStats  (  count  ,  diffRating  )  ; 
returnMap  .  put  (  otherMovie  ,  newSOS  )  ; 
} 
return   returnMap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 







public   static   float   GetAveragePrediction  (  short   movie  )  { 
TIntByteHashMap   custratings  =  (  TIntByteHashMap  )  CustomersAndRatingsPerMovie  .  get  (  movie  )  ; 
int   noofrecords  =  custratings  .  size  (  )  ; 
int   sumOfPredictions  =  0  ; 
int   totalPredictions  =  0  ; 
TIntByteIterator   itr  =  custratings  .  iterator  (  )  ; 
for  (  int   l  =  0  ;  l  <  noofrecords  ;  l  ++  )  { 
itr  .  advance  (  )  ; 
sumOfPredictions  +=  new   Byte  (  itr  .  value  (  )  )  .  intValue  (  )  ; 
totalPredictions  +=  1  ; 
} 
return   new   Float  (  sumOfPredictions  /  totalPredictions  )  .  floatValue  (  )  ; 
} 













public   static   TShortObjectHashMap   InitializeCustomerRatingsForMovieHashMap  (  String   completePath  ,  TShortObjectHashMap   MovieLimitsTHash  )  { 
try  { 
TShortObjectHashMap   returnMap  =  new   TShortObjectHashMap  (  MovieLimitsTHash  .  size  (  )  ,  1  )  ; 
int   totalMovies  =  MovieLimitsTHash  .  size  (  )  ; 
File   movieMMAPDATAFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  CustomerRatingFileName  )  ; 
FileChannel   inC  =  new   FileInputStream  (  movieMMAPDATAFile  )  .  getChannel  (  )  ; 
short  [  ]  itr  =  MovieLimitsTHash  .  keys  (  )  ; 
int   startIndex  =  0  ; 
int   endIndex  =  0  ; 
TIntArrayList   a  =  null  ; 
TIntByteHashMap   result  ; 
ByteBuffer   buf  ; 
for  (  int   i  =  0  ;  i  <  totalMovies  ;  i  ++  )  { 
short   currentMovie  =  itr  [  i  ]  ; 
a  =  (  TIntArrayList  )  MovieLimitsTHash  .  get  (  currentMovie  )  ; 
startIndex  =  a  .  get  (  0  )  ; 
endIndex  =  a  .  get  (  1  )  ; 
if  (  endIndex  >  startIndex  )  { 
result  =  new   TIntByteHashMap  (  endIndex  -  startIndex  +  1  ,  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  (  endIndex  -  startIndex  +  1  )  *  5  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  5  )  ; 
}  else  { 
result  =  new   TIntByteHashMap  (  1  ,  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  5  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  5  )  ; 
} 
buf  .  flip  (  )  ; 
int   bufsize  =  buf  .  capacity  (  )  /  5  ; 
for  (  int   q  =  0  ;  q  <  bufsize  ;  q  ++  )  { 
result  .  put  (  buf  .  getInt  (  )  ,  buf  .  get  (  )  )  ; 
} 
returnMap  .  put  (  currentMovie  ,  result  .  clone  (  )  )  ; 
buf  .  clear  (  )  ; 
buf  =  null  ; 
a  .  clear  (  )  ; 
a  =  null  ; 
} 
inC  .  close  (  )  ; 
return   returnMap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 












public   static   TIntObjectHashMap   InitializeMovieRatingsForCustomerHashMap  (  String   completePath  ,  TIntObjectHashMap   custList  )  { 
try  { 
TIntObjectHashMap   returnMap  =  new   TIntObjectHashMap  (  custList  .  size  (  )  ,  1  )  ; 
int   totalCusts  =  custList  .  size  (  )  ; 
File   movieMMAPDATAFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  MovieRatingFileName  )  ; 
FileChannel   inC  =  new   FileInputStream  (  movieMMAPDATAFile  )  .  getChannel  (  )  ; 
int  [  ]  itr  =  custList  .  keys  (  )  ; 
int   startIndex  =  0  ; 
int   endIndex  =  0  ; 
TIntArrayList   a  =  null  ; 
TShortByteHashMap   result  ; 
ByteBuffer   buf  ; 
for  (  int   i  =  0  ;  i  <  totalCusts  ;  i  ++  )  { 
int   currentCust  =  itr  [  i  ]  ; 
a  =  (  TIntArrayList  )  custList  .  get  (  currentCust  )  ; 
startIndex  =  a  .  get  (  0  )  ; 
endIndex  =  a  .  get  (  1  )  ; 
if  (  endIndex  >  startIndex  )  { 
result  =  new   TShortByteHashMap  (  endIndex  -  startIndex  +  1  ,  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  (  endIndex  -  startIndex  +  1  )  *  3  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  3  )  ; 
}  else  { 
result  =  new   TShortByteHashMap  (  1  ,  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  3  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  3  )  ; 
} 
buf  .  flip  (  )  ; 
int   bufsize  =  buf  .  capacity  (  )  /  3  ; 
for  (  int   q  =  0  ;  q  <  bufsize  ;  q  ++  )  { 
result  .  put  (  buf  .  getShort  (  )  ,  buf  .  get  (  )  )  ; 
} 
returnMap  .  put  (  currentCust  ,  result  .  clone  (  )  )  ; 
buf  .  clear  (  )  ; 
buf  =  null  ; 
a  .  clear  (  )  ; 
a  =  null  ; 
} 
return   returnMap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 
} 

class   slopeOneStats  { 

int   count  ; 

float   diffRating  ; 

public   slopeOneStats  (  )  { 
count  =  0  ; 
diffRating  =  0  ; 
} 

public   slopeOneStats  (  int   cnt  ,  float   dif  )  { 
count  =  cnt  ; 
diffRating  =  dif  ; 
} 

public   int   getCount  (  )  { 
return   count  ; 
} 

public   float   getDiffRating  (  )  { 
return   diffRating  ; 
} 
} 

