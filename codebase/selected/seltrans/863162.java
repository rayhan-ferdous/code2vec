package   org  .  smartgrape  .  statistics  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   gnu  .  trove  .  *  ; 
import   org  .  apache  .  commons  .  math  .  stat  .  regression  .  SimpleRegression  ; 





public   class   SGBasicRegresser  { 

public   static   char   fSep  =  File  .  separatorChar  ; 

static   String   CustomerRatingFileName  =  "CustomerRatingBinaryFile.txt"  ; 

static   String   MovieIndexFileName  =  "MovieIndex.txt"  ; 

static   String   MovieRatingFileName  =  "MovieRatingBinaryFile.txt"  ; 

static   String   CustIndexFileName  =  "CustomerIndex.txt"  ; 

static   TShortObjectHashMap   MovieLimitsTHash  ; 

static   TShortObjectHashMap   CustomersAndRatingsPerMovie  ; 

static   TIntObjectHashMap   MoviesAndRatingsPerCustomer  ; 

static   TIntObjectHashMap   CustomerLimitsTHash  ; 

public   static   void   main  (  String  [  ]  args  )  { 
try  { 
String   completePath  =  null  ; 
String   predictionFileName  =  null  ; 
if  (  args  .  length  ==  2  )  { 
completePath  =  args  [  0  ]  ; 
predictionFileName  =  args  [  1  ]  ; 
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
MoviesAndRatingsPerCustomer  =  InitializeMovieRatingsForCustomerHashMap  (  completePath  ,  CustomerLimitsTHash  )  ; 
System  .  out  .  println  (  "Populated MoviesAndRatingsPerCustomer hashmap"  )  ; 
File   outfile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  predictionFileName  )  ; 
FileChannel   out  =  new   FileOutputStream  (  outfile  ,  true  )  .  getChannel  (  )  ; 
inputFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  "formattedProbeData.txt"  )  ; 
inC  =  new   FileInputStream  (  inputFile  )  .  getChannel  (  )  ; 
filesize  =  (  int  )  inC  .  size  (  )  ; 
ByteBuffer   probemappedfile  =  inC  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  filesize  )  ; 
int   custAndRatingSize  =  0  ; 
TIntByteHashMap   custsandratings  =  new   TIntByteHashMap  (  )  ; 
int   ignoreProcessedRows  =  0  ; 
int   movieViewershipSize  =  0  ; 
while  (  probemappedfile  .  hasRemaining  (  )  )  { 
short   testmovie  =  probemappedfile  .  getShort  (  )  ; 
int   testCustomer  =  probemappedfile  .  getInt  (  )  ; 
if  (  (  CustomersAndRatingsPerMovie  !=  null  )  &&  (  CustomersAndRatingsPerMovie  .  containsKey  (  testmovie  )  )  )  { 
}  else  { 
CustomersAndRatingsPerMovie  =  InitializeCustomerRatingsForMovieHashMap  (  completePath  ,  testmovie  )  ; 
custsandratings  =  (  TIntByteHashMap  )  CustomersAndRatingsPerMovie  .  get  (  testmovie  )  ; 
custAndRatingSize  =  custsandratings  .  size  (  )  ; 
} 
TShortByteHashMap   testCustMovieAndRatingsMap  =  (  TShortByteHashMap  )  MoviesAndRatingsPerCustomer  .  get  (  testCustomer  )  ; 
short  [  ]  testCustMovies  =  testCustMovieAndRatingsMap  .  keys  (  )  ; 
float   finalPrediction  =  0  ; 
finalPrediction  =  predictRating  (  testCustomer  ,  testmovie  ,  custsandratings  ,  custAndRatingSize  ,  testCustMovies  ,  testCustMovieAndRatingsMap  )  ; 
System  .  out  .  println  (  "prediction for movie: "  +  testmovie  +  " for customer "  +  testCustomer  +  " is "  +  finalPrediction  )  ; 
ByteBuffer   buf  =  ByteBuffer  .  allocate  (  11  )  ; 
buf  .  putShort  (  testmovie  )  ; 
buf  .  putInt  (  testCustomer  )  ; 
buf  .  putFloat  (  finalPrediction  )  ; 
buf  .  flip  (  )  ; 
out  .  write  (  buf  )  ; 
buf  =  null  ; 
testCustMovieAndRatingsMap  =  null  ; 
testCustMovies  =  null  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 










public   static   float   predictRating  (  int   testCustomer  ,  short   testmovie  ,  TIntByteHashMap   custsandratings  ,  int   custAndRatingSize  ,  short  [  ]  testCustMovies  ,  TShortByteHashMap   testCustMovieAndRatingsMap  )  { 
int   totalPredictions  =  0  ; 
double   sumOfPredictions  =  0  ; 
float   totalweight  =  0  ; 
int   otherCust  ; 
byte   otherRating  ; 
int   othercustAndRatingSize  =  0  ,  othermoviesAndRatingSize  =  0  ; 
TShortHashSet   otherCustMovies  ; 
TShortByteHashMap   otherCustMovieAndRatingsMap  ; 
TIntByteIterator   ibit  =  custsandratings  .  iterator  (  )  ; 
int   NoOfRegressions  =  0  ; 
for  (  int   p  =  0  ;  p  <  custAndRatingSize  ;  p  ++  )  { 
ibit  .  advance  (  )  ; 
otherCust  =  ibit  .  key  (  )  ; 
otherRating  =  ibit  .  value  (  )  ; 
otherCustMovieAndRatingsMap  =  (  TShortByteHashMap  )  MoviesAndRatingsPerCustomer  .  get  (  otherCust  )  ; 
otherCustMovies  =  new   TShortHashSet  (  otherCustMovieAndRatingsMap  .  keys  (  )  )  ; 
MovieOverLapForTwoCusts  (  testCustMovies  ,  otherCustMovies  )  ; 
if  (  (  otherCustMovies  .  size  (  )  ==  0  )  ||  (  testCustomer  ==  otherCust  )  ||  (  otherCustMovies  ==  null  )  )  { 
}  else  { 
double  [  ]  [  ]  d  =  prepareData  (  otherCustMovies  ,  testCustMovieAndRatingsMap  ,  otherCustMovieAndRatingsMap  ,  otherRating  )  ; 
SimpleRegression   r  =  getRegression  (  d  )  ; 
double   prediction  =  r  .  predict  (  new   Byte  (  otherRating  )  .  doubleValue  (  )  )  ; 
double   weight  =  r  .  getRSquare  (  )  ; 
d  =  null  ; 
if  (  new   Double  (  prediction  )  .  isNaN  (  )  ||  new   Double  (  weight  )  .  isNaN  (  )  )  { 
}  else  { 
totalPredictions  +=  1  ; 
sumOfPredictions  +=  prediction  *  weight  ; 
totalweight  +=  weight  ; 
} 
otherCustMovieAndRatingsMap  =  null  ; 
otherCustMovies  .  clear  (  )  ; 
otherCustMovies  =  null  ; 
r  =  null  ; 
} 
if  (  totalPredictions  >=  250  )  break  ; 
} 
float   finalPrediction  =  0  ; 
if  (  totalPredictions  ==  0  )  { 
finalPrediction  =  GetAveragePrediction  (  testmovie  )  ; 
}  else  { 
finalPrediction  =  new   Float  (  sumOfPredictions  /  (  totalweight  )  )  .  floatValue  (  )  ; 
} 
if  (  finalPrediction  >  5  )  finalPrediction  =  5  ; 
if  (  finalPrediction  <  1  )  finalPrediction  =  1  ; 
return   finalPrediction  ; 
} 







public   static   double  [  ]  [  ]  prepareData  (  TShortHashSet   overLapMovies  ,  TShortByteHashMap   testmoviesAndRateMap  ,  TShortByteHashMap   othermoviesAndRateMap  ,  short   testmovie  )  { 
double  [  ]  [  ]  d  =  new   double  [  overLapMovies  .  size  (  )  ]  [  2  ]  ; 
TShortIterator   itr  =  overLapMovies  .  iterator  (  )  ; 
int   i  =  0  ; 
int   intersectsize  =  overLapMovies  .  size  (  )  ; 
for  (  int   j  =  0  ;  j  <  intersectsize  ;  j  ++  )  { 
short   movie  =  itr  .  next  (  )  ; 
if  (  movie  !=  testmovie  )  { 
d  [  i  ]  [  0  ]  =  new   Byte  (  othermoviesAndRateMap  .  get  (  movie  )  )  .  doubleValue  (  )  ; 
d  [  i  ]  [  1  ]  =  new   Byte  (  testmoviesAndRateMap  .  get  (  movie  )  )  .  doubleValue  (  )  ; 
} 
i  ++  ; 
} 
itr  =  null  ; 
return   d  ; 
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

public   static   Set   GetMoviesAloneForCust  (  Map   MovieRatings  )  { 
return   MovieRatings  .  keySet  (  )  ; 
} 












public   static   void   MovieOverLapForTwoCusts  (  short  [  ]  testCustMovies  ,  TShortHashSet   otherCustMovies  )  { 
try  { 
otherCustMovies  .  retainAll  (  testCustMovies  )  ; 
if  (  otherCustMovies  .  size  (  )  >  3  )  { 
}  else  { 
otherCustMovies  .  clear  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 







public   static   SimpleRegression   getRegression  (  double  [  ]  [  ]  data  )  { 
SimpleRegression   regression  =  new   SimpleRegression  (  )  ; 
regression  .  addData  (  data  )  ; 
return   regression  ; 
} 












public   static   TIntObjectHashMap   InitializeMovieRatingsForCustomerHashMap  (  String   completePath  ,  TIntObjectHashMap   custList  )  { 
try  { 
TIntObjectHashMap   returnMap  =  new   TIntObjectHashMap  (  custList  .  size  (  )  ,  1  )  ; 
int   totalCusts  =  custList  .  size  (  )  ; 
File   movieMMAPDATAFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  "MovieRatingBinaryFile.txt"  )  ; 
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












public   static   TShortObjectHashMap   InitializeCustomerRatingsForMovieHashMap  (  String   completePath  ,  short   movie  )  { 
try  { 
TShortObjectHashMap   returnMap  =  new   TShortObjectHashMap  (  MovieLimitsTHash  .  size  (  )  ,  1  )  ; 
File   movieMMAPDATAFile  =  new   File  (  completePath  +  fSep  +  "SmartGRAPE"  +  fSep  +  "CustomerRatingBinaryFile.txt"  )  ; 
FileChannel   inC  =  new   FileInputStream  (  movieMMAPDATAFile  )  .  getChannel  (  )  ; 
TIntByteHashMap   result  ; 
ByteBuffer   buf  ; 
TIntArrayList   a  =  (  TIntArrayList  )  MovieLimitsTHash  .  get  (  movie  )  ; 
int   startIndex  =  a  .  get  (  0  )  ; 
int   endIndex  =  a  .  get  (  1  )  ; 
if  (  endIndex  >  startIndex  )  { 
result  =  new   TIntByteHashMap  (  endIndex  -  startIndex  +  1  ,  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  (  endIndex  -  startIndex  +  1  )  *  5  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  5  )  ; 
}  else  { 
result  =  new   TIntByteHashMap  (  1  )  ; 
buf  =  ByteBuffer  .  allocate  (  5  )  ; 
inC  .  read  (  buf  ,  (  startIndex  -  1  )  *  5  )  ; 
} 
buf  .  flip  (  )  ; 
int   bufsize  =  buf  .  capacity  (  )  /  5  ; 
for  (  int   q  =  0  ;  q  <  bufsize  ;  q  ++  )  { 
result  .  put  (  buf  .  getInt  (  )  ,  buf  .  get  (  )  )  ; 
} 
returnMap  .  put  (  movie  ,  result  )  ; 
buf  .  clear  (  )  ; 
buf  =  null  ; 
a  .  clear  (  )  ; 
a  =  null  ; 
inC  .  close  (  )  ; 
return   returnMap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 
} 

