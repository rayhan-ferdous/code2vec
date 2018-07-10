package   co  .  edu  .  unal  .  ungrid  .  transformation  .  dft  ; 

import   co  .  edu  .  unal  .  ungrid  .  util  .  ConcurrencyHelper  ; 




public   class   BenchmarkDoubleFft  { 

private   BenchmarkDoubleFft  (  )  { 
} 

public   static   void   parseArguments  (  String  [  ]  args  )  { 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "args["  +  i  +  "]:"  +  args  [  i  ]  )  ; 
} 
if  (  (  args  ==  null  )  ||  (  args  .  length  !=  10  )  )  { 
System  .  out  .  println  (  "Parameters: <number of threads> <THREADS_BEGIN_N_2D> <THREADS_BEGIN_N_3D> <number of iterations> <perform warm-up> <perform scaling> <number of sizes> <initial exponent for 1D transforms> <initial exponent for 2D transforms> <initial exponent for 3D transforms>"  )  ; 
System  .  exit  (  -  1  )  ; 
} 
nthread  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
ConcurrencyHelper  .  setThreadsMinSize2D  (  Integer  .  parseInt  (  args  [  1  ]  )  )  ; 
ConcurrencyHelper  .  setThreadsMinSize3D  (  Integer  .  parseInt  (  args  [  2  ]  )  )  ; 
niter  =  Integer  .  parseInt  (  args  [  3  ]  )  ; 
doWarmup  =  Boolean  .  parseBoolean  (  args  [  4  ]  )  ; 
doScaling  =  Boolean  .  parseBoolean  (  args  [  5  ]  )  ; 
nsize  =  Integer  .  parseInt  (  args  [  6  ]  )  ; 
initialExponent1D  =  Integer  .  parseInt  (  args  [  7  ]  )  ; 
initialExponent2D  =  Integer  .  parseInt  (  args  [  8  ]  )  ; 
initialExponent3D  =  Integer  .  parseInt  (  args  [  9  ]  )  ; 
ConcurrencyHelper  .  setNumberOfProcessors  (  nthread  )  ; 
} 

public   static   void   benchmarkComplexForward1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Complex forward FFT 1D of size 2^"  +  exponent  )  ; 
DoubleFft1D   fft  =  new   DoubleFft1D  (  N  )  ; 
x  =  new   double  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_1D  (  2  *  N  ,  x  )  ; 
fft  .  complexForward  (  x  )  ; 
IoUtils  .  fillMatrix_1D  (  2  *  N  ,  x  )  ; 
fft  .  complexForward  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_1D  (  2  *  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft  .  complexForward  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleComplexForwardFFT_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkRealForward1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Real forward FFT 1D of size 2^"  +  exponent  )  ; 
DoubleFft1D   fft  =  new   DoubleFft1D  (  N  )  ; 
x  =  new   double  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_1D  (  N  ,  x  )  ; 
fft  .  realForwardFull  (  x  )  ; 
IoUtils  .  fillMatrix_1D  (  N  ,  x  )  ; 
fft  .  realForwardFull  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_1D  (  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft  .  realForwardFull  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleRealForwardFFT_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkComplexForward2DInput1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Complex forward FFT 2D (input 1D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft2D   fft2  =  new   DoubleFft2D  (  N  ,  N  )  ; 
x  =  new   double  [  N  *  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
fft2  .  complexForward  (  x  )  ; 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
fft2  .  complexForward  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft2  .  complexForward  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft2  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleComplexForwardFFT_2D_input_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkComplexForward2DInput2D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Complex forward FFT 2D (input 2D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft2D   fft2  =  new   DoubleFft2D  (  N  ,  N  )  ; 
x  =  new   double  [  N  ]  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
fft2  .  complexForward  (  x  )  ; 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
fft2  .  complexForward  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  2  *  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft2  .  complexForward  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft2  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleComplexForwardFFT_2D_input_2D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkRealForward2DInput1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Real forward FFT 2D (input 1D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft2D   fft2  =  new   DoubleFft2D  (  N  ,  N  )  ; 
x  =  new   double  [  N  *  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft2  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleRealForwardFFT_2D_input_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkRealForward2DInput2D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Real forward FFT 2D (input 2D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft2D   fft2  =  new   DoubleFft2D  (  N  ,  N  )  ; 
x  =  new   double  [  N  ]  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_2D  (  N  ,  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft2  .  realForwardFull  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft2  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleRealForwardFFT_2D_input_2D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkComplexForward3DInput1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Complex forward FFT 3D (input 1D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft3D   fft3  =  new   DoubleFft3D  (  N  ,  N  ,  N  )  ; 
x  =  new   double  [  N  *  N  *  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
fft3  .  complexForward  (  x  )  ; 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
fft3  .  complexForward  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft3  .  complexForward  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft3  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleComplexForwardFFT_3D_input_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkComplexForward3DInput3D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  [  ]  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Complex forward FFT 3D (input 3D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft3D   fft3  =  new   DoubleFft3D  (  N  ,  N  ,  N  )  ; 
x  =  new   double  [  N  ]  [  N  ]  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
fft3  .  complexForward  (  x  )  ; 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
fft3  .  complexForward  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  2  *  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft3  .  complexForward  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft3  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleComplexForwardFFT_3D_input_3D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkRealForward3DInput1D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Real forward FFT 3D (input 1D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft3D   fft3  =  new   DoubleFft3D  (  N  ,  N  ,  N  )  ; 
x  =  new   double  [  N  *  N  *  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft3  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleRealForwardFFT_3D_input_1D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   benchmarkRealForward3DInput3D  (  int   init_exp  )  { 
int  [  ]  sizes  =  new   int  [  nsize  ]  ; 
double  [  ]  times  =  new   double  [  nsize  ]  ; 
double  [  ]  [  ]  [  ]  x  ; 
for  (  int   i  =  0  ;  i  <  nsize  ;  i  ++  )  { 
int   exponent  =  init_exp  +  i  ; 
int   N  =  (  int  )  Math  .  pow  (  2  ,  exponent  )  ; 
sizes  [  i  ]  =  N  ; 
System  .  out  .  println  (  "Real forward FFT 3D (input 3D) of size 2^"  +  exponent  +  " x 2^"  +  exponent  +  " x 2^"  +  exponent  )  ; 
DoubleFft3D   fft3  =  new   DoubleFft3D  (  N  ,  N  ,  N  )  ; 
x  =  new   double  [  N  ]  [  N  ]  [  2  *  N  ]  ; 
if  (  doWarmup  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
} 
double   av_time  =  0  ; 
long   elapsedTime  =  0  ; 
for  (  int   j  =  0  ;  j  <  niter  ;  j  ++  )  { 
IoUtils  .  fillMatrix_3D  (  N  ,  N  ,  N  ,  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  ; 
fft3  .  realForwardFull  (  x  )  ; 
elapsedTime  =  System  .  nanoTime  (  )  -  elapsedTime  ; 
av_time  =  av_time  +  elapsedTime  ; 
} 
times  [  i  ]  =  (  double  )  av_time  /  1000000.0  /  (  double  )  niter  ; 
System  .  out  .  println  (  "\tAverage execution time: "  +  String  .  format  (  "%.2f"  ,  av_time  /  1000000.0  /  (  double  )  niter  )  +  " msec"  )  ; 
x  =  null  ; 
fft3  =  null  ; 
System  .  gc  (  )  ; 
} 
IoUtils  .  writeFFTBenchmarkResultsToFile  (  "benchmarkDoubleRealForwardFFT_3D_input_3D.txt"  ,  nthread  ,  niter  ,  doWarmup  ,  doScaling  ,  sizes  ,  times  )  ; 
} 

public   static   void   main  (  String  [  ]  args  )  { 
parseArguments  (  args  )  ; 
benchmarkComplexForward1D  (  initialExponent1D  )  ; 
benchmarkRealForward1D  (  initialExponent1D  )  ; 
benchmarkComplexForward2DInput1D  (  initialExponent2D  )  ; 
benchmarkComplexForward2DInput2D  (  initialExponent2D  )  ; 
benchmarkRealForward2DInput1D  (  initialExponent2D  )  ; 
benchmarkRealForward2DInput2D  (  initialExponent2D  )  ; 
benchmarkComplexForward3DInput1D  (  initialExponent3D  )  ; 
benchmarkComplexForward3DInput3D  (  initialExponent3D  )  ; 
benchmarkRealForward3DInput1D  (  initialExponent3D  )  ; 
benchmarkRealForward3DInput3D  (  initialExponent3D  )  ; 
} 

private   static   int   nthread  =  2  ; 

private   static   int   nsize  =  6  ; 

private   static   int   niter  =  200  ; 

private   static   int   initialExponent1D  =  17  ; 

private   static   int   initialExponent2D  =  7  ; 

private   static   int   initialExponent3D  =  2  ; 

private   static   boolean   doWarmup  =  true  ; 

private   static   boolean   doScaling  =  false  ; 
} 

