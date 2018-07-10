import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  util  .  GregorianCalendar  ; 

public   class   Test  { 









private   UnitSystem   createTemperaturUnit1  (  )  { 
try  { 
Auth   auth  =  new   Auth  (  )  ; 
auth  .  login  (  "sys"  ,  "12345"  )  ; 
UnitSystem   us  =  new   UnitSystem  (  "Temperature"  ,  new   Kelvin  (  )  ,  auth  )  ; 
us  .  getUnits  (  )  .  add  (  new   Celsius  (  )  ,  auth  )  ; 
us  .  getUnits  (  )  .  add  (  new   Fahreneinheit  (  )  ,  auth  )  ; 
us  .  setMin  (  new   Value  (  0  ,  new   Kelvin  (  )  )  ,  auth  )  ; 
us  .  setMax  (  new   Value  (  373.15  ,  new   Kelvin  (  )  )  ,  auth  )  ; 
us  .  setBorder  (  new   Value  (  313.15  ,  new   Kelvin  (  )  )  ,  auth  )  ; 
us  .  setRefborder  (  new   Value  (  273.15  ,  new   Kelvin  (  )  )  ,  auth  )  ; 
return   us  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 










private   Point   createPoint1  (  )  { 
return   new   Point  (  "TestPoint"  ,  "12345"  ,  this  .  createTemperaturUnit1  (  )  ,  new   GregorianCalendar  (  2000  ,  1  ,  1  )  ,  new   GregorianCalendar  (  2100  ,  1  ,  1  )  ,  1  )  ; 
} 










private   Measuring   createMeasuring1  (  double   val  ,  GregorianCalendar   time  )  { 
Value   value  =  new   Value  (  val  ,  new   Fahreneinheit  (  )  )  ; 
Point   point  =  this  .  createPoint1  (  )  ; 
return   new   Measuring  (  value  ,  point  ,  time  )  ; 
} 

private   Application   createApplication2  (  )  { 
try  { 
Application   app  =  new   Application  (  )  ; 
app  .  login  (  "sys"  ,  "12345"  )  ; 
Auth   auth  =  app  .  getAuth  (  )  ; 
UnitSystem   usathen  =  new   UnitSystem  (  "Temperature"  ,  new   Kelvin  (  )  ,  auth  )  ; 
usathen  .  getUnits  (  )  .  add  (  new   Celsius  (  )  ,  auth  )  ; 
usathen  .  getUnits  (  )  .  add  (  new   Fahreneinheit  (  )  ,  auth  )  ; 
usathen  .  setMin  (  new   Value  (  -  20  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usathen  .  setMax  (  new   Value  (  60  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usathen  .  setBorder  (  new   Value  (  40  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usathen  .  setRefborder  (  new   Value  (  35  ,  new   Celsius  (  )  )  ,  auth  )  ; 
UnitSystem   usbangladesch  =  new   UnitSystem  (  "Temperature"  ,  new   Kelvin  (  )  ,  auth  )  ; 
usbangladesch  .  getUnits  (  )  .  add  (  new   Celsius  (  )  ,  auth  )  ; 
usbangladesch  .  getUnits  (  )  .  add  (  new   Fahreneinheit  (  )  ,  auth  )  ; 
usbangladesch  .  setMin  (  new   Value  (  -  10  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usbangladesch  .  setMax  (  new   Value  (  100  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usbangladesch  .  setBorder  (  new   Value  (  45  ,  new   Celsius  (  )  )  ,  auth  )  ; 
usbangladesch  .  setRefborder  (  new   Value  (  40  ,  new   Celsius  (  )  )  ,  auth  )  ; 
UnitSystem   uschicago  =  new   UnitSystem  (  "Temperature"  ,  new   Kelvin  (  )  ,  auth  )  ; 
uschicago  .  getUnits  (  )  .  add  (  new   Celsius  (  )  ,  auth  )  ; 
uschicago  .  getUnits  (  )  .  add  (  new   Fahreneinheit  (  )  ,  auth  )  ; 
uschicago  .  setMin  (  new   Value  (  -  30  ,  new   Celsius  (  )  )  ,  auth  )  ; 
uschicago  .  setMax  (  new   Value  (  40  ,  new   Celsius  (  )  )  ,  auth  )  ; 
uschicago  .  setBorder  (  new   Value  (  25  ,  new   Celsius  (  )  )  ,  auth  )  ; 
uschicago  .  setRefborder  (  new   Value  (  30  ,  new   Celsius  (  )  )  ,  auth  )  ; 
Point   athen  =  new   Point  (  "Athen"  ,  "Athen"  ,  usathen  ,  new   GregorianCalendar  (  2000  ,  1  ,  1  )  ,  new   GregorianCalendar  (  2100  ,  1  ,  1  )  ,  1  )  ; 
Point   bangladesch  =  new   Point  (  "Bangladesch"  ,  "Bangladesch"  ,  usbangladesch  ,  new   GregorianCalendar  (  2000  ,  1  ,  1  )  ,  new   GregorianCalendar  (  2100  ,  1  ,  1  )  ,  1  )  ; 
Point   chicago  =  new   Point  (  "Chicago"  ,  "Chicago"  ,  uschicago  ,  new   GregorianCalendar  (  2000  ,  1  ,  1  )  ,  new   GregorianCalendar  (  2100  ,  1  ,  1  )  ,  1  )  ; 
app  .  getPoints  (  )  .  add  (  athen  ,  auth  )  ; 
app  .  getPoints  (  )  .  add  (  bangladesch  ,  auth  )  ; 
app  .  getPoints  (  )  .  add  (  chicago  ,  auth  )  ; 
return   app  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 




private   Application   createApplication1  (  )  { 
Application   app  =  this  .  createApplication2  (  )  ; 
try  { 
Measuring   ma1  =  new   Measuring  (  new   Value  (  5  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  8  ,  0  ,  0  )  )  ; 
Measuring   ma2  =  new   Measuring  (  new   Value  (  7  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  10  ,  0  ,  0  )  )  ; 
Measuring   ma3  =  new   Measuring  (  new   Value  (  9  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  12  ,  0  ,  0  )  )  ; 
Measuring   ma4  =  new   Measuring  (  new   Value  (  11  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  14  ,  0  ,  0  )  )  ; 
Measuring   ma5  =  new   Measuring  (  new   Value  (  9  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  16  ,  0  ,  0  )  )  ; 
Measuring   ma6  =  new   Measuring  (  new   Value  (  8  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  0  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  18  ,  0  ,  0  )  )  ; 
Measuring   mb1  =  new   Measuring  (  new   Value  (  12  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  8  ,  0  ,  0  )  )  ; 
Measuring   mb2  =  new   Measuring  (  new   Value  (  17  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  10  ,  0  ,  0  )  )  ; 
Measuring   mb3  =  new   Measuring  (  new   Value  (  24  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  12  ,  0  ,  0  )  )  ; 
Measuring   mb4  =  new   Measuring  (  new   Value  (  27  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  14  ,  0  ,  0  )  )  ; 
Measuring   mb5  =  new   Measuring  (  new   Value  (  27  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  16  ,  0  ,  0  )  )  ; 
Measuring   mb6  =  new   Measuring  (  new   Value  (  22  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  18  ,  0  ,  0  )  )  ; 
Measuring   mc1  =  new   Measuring  (  new   Value  (  6  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  8  ,  0  ,  0  )  )  ; 
Measuring   mc2  =  new   Measuring  (  new   Value  (  8  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  10  ,  0  ,  0  )  )  ; 
Measuring   mc3  =  new   Measuring  (  new   Value  (  10  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  12  ,  0  ,  0  )  )  ; 
Measuring   mc4  =  new   Measuring  (  new   Value  (  10  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  14  ,  0  ,  0  )  )  ; 
Measuring   mc5  =  new   Measuring  (  new   Value  (  12  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  16  ,  0  ,  0  )  )  ; 
Measuring   mc6  =  new   Measuring  (  new   Value  (  9  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  18  ,  0  ,  0  )  )  ; 
app  .  getDatasource  (  )  .  add  (  ma1  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  ma2  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  ma3  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  ma4  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  ma5  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  ma6  ,  "Athen"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb1  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb2  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb3  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb4  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb5  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mb6  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc1  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc2  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc3  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc4  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc5  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  mc6  ,  "Chicago"  )  ; 
return   app  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   null  ; 
} 
} 










public   boolean   Test1  (  String   methodName  )  { 
UnitSystem   tu  =  this  .  createTemperaturUnit1  (  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  313.15  ,  new   Kelvin  (  )  )  ,  tu  .  getBorder  (  )  )  ; 
} 










public   boolean   Test2  (  String   methodName  )  { 
UnitSystem   tu  =  this  .  createTemperaturUnit1  (  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  0.0  ,  new   Kelvin  (  )  )  ,  tu  .  getMin  (  )  )  ; 
} 










public   boolean   Test3  (  String   methodName  )  { 
UnitSystem   tu  =  this  .  createTemperaturUnit1  (  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  373.15  ,  new   Kelvin  (  )  )  ,  tu  .  getMax  (  )  )  ; 
} 








public   boolean   Test5  (  String   methodName  )  { 
UnitSystem   tp  =  this  .  createTemperaturUnit1  (  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  273.15  ,  new   Kelvin  (  )  )  ,  tp  .  getRefBorder  (  )  )  ; 
} 








public   boolean   Test6  (  String   methodName  )  { 
UnitSystem   tp  =  this  .  createTemperaturUnit1  (  )  ; 
return   this  .  matchResultToInteger  (  methodName  ,  3  ,  tp  .  countUnits  (  )  )  ; 
} 









public   boolean   Test7  (  String   methodName  )  { 
UnitSystem   tp  =  this  .  createTemperaturUnit1  (  )  ; 
Value   v  =  new   Value  (  0  ,  new   Celsius  (  )  )  ; 
try  { 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  273.15  ,  new   Kelvin  (  )  )  ,  tp  .  transform  (  v  ,  new   Kelvin  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 









public   boolean   Test8  (  String   methodName  )  { 
UnitSystem   tp  =  this  .  createTemperaturUnit1  (  )  ; 
Value   v  =  new   Value  (  0  ,  new   Fahreneinheit  (  )  )  ; 
try  { 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  255.37222222222223  ,  new   Kelvin  (  )  )  ,  tp  .  transform  (  v  ,  new   Kelvin  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 










public   boolean   Test9  (  String   methodName  )  { 
UnitSystem   tp  =  this  .  createTemperaturUnit1  (  )  ; 
Value   v  =  new   Value  (  0  ,  new   Fahreneinheit  (  )  )  ; 
try  { 
v  =  tp  .  transform  (  v  ,  new   Kelvin  (  )  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  -  17.777777777777743  ,  new   Celsius  (  )  )  ,  tp  .  transform  (  v  ,  new   Celsius  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 







public   boolean   Test11  (  String   methodName  )  { 
Value   v  =  new   Value  (  0  ,  new   Fahreneinheit  (  )  )  ; 
return   this  .  matchResultToString  (  methodName  ,  "F"  ,  v  .  getUnit  (  )  .  getName  (  )  )  ; 
} 







public   boolean   Test12  (  String   methodName  )  { 
Value   v  =  new   Value  (  5  ,  new   Fahreneinheit  (  )  )  ; 
return   this  .  matchResultToDouble  (  methodName  ,  5  ,  v  .  getValue  (  )  )  ; 
} 









public   boolean   Test14  (  String   methodName  )  { 
Point   p  =  this  .  createPoint1  (  )  ; 
return   this  .  matchResultToInteger  (  methodName  ,  1  ,  p  .  getId  (  )  )  ; 
} 









public   boolean   Test15  (  String   methodName  )  { 
Point   p  =  this  .  createPoint1  (  )  ; 
return   this  .  matchResultToInteger  (  methodName  ,  2  ,  p  .  getId  (  )  )  ; 
} 









public   boolean   Test16  (  String   methodName  )  { 
Point   p  =  this  .  createPoint1  (  )  ; 
return   this  .  matchResultToString  (  methodName  ,  "TestPoint"  ,  p  .  getName  (  )  )  ; 
} 






public   boolean   Test17  (  String   methodName  )  { 
Measuring   m  =  this  .  createMeasuring1  (  5  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  )  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  )  ,  m  .  getTime  (  )  )  ; 
} 






public   boolean   Test18  (  String   methodName  )  { 
Measuring   m  =  this  .  createMeasuring1  (  5  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  )  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  new   Value  (  5  ,  new   Fahreneinheit  (  )  )  ,  m  .  getValue  (  )  )  ; 
} 






public   boolean   Test19  (  String   methodName  )  { 
Point   p  =  new   Point  (  "Test"  ,  "12345"  ,  this  .  createTemperaturUnit1  (  )  ,  new   GregorianCalendar  (  2000  ,  1  ,  1  )  ,  new   GregorianCalendar  (  2100  ,  1  ,  1  )  ,  1  )  ; 
Measuring   m  =  new   Measuring  (  new   Value  (  5  ,  new   Fahreneinheit  (  )  )  ,  p  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  )  )  ; 
return   this  .  matchResultToObject  (  methodName  ,  p  ,  m  .  getPoint  (  )  )  ; 
} 






public   boolean   Test20  (  String   methodName  )  { 
Point   p  =  this  .  createPoint1  (  )  ; 
return   this  .  matchResultToBoolean  (  methodName  ,  true  ,  p  .  equalsPassword  (  "12345"  )  )  ; 
} 






public   boolean   Test21  (  String   methodName  )  { 
Point   p  =  this  .  createPoint1  (  )  ; 
return   this  .  matchResultToBoolean  (  methodName  ,  false  ,  p  .  equalsPassword  (  "asdfasfd"  )  )  ; 
} 








public   boolean   Test22  (  String   methodName  )  { 
Application   app1  =  this  .  createApplication1  (  )  ; 
Application   app2  =  new   Application  (  )  ; 
try  { 
app1  .  save  (  "app.txt"  )  ; 
app2  .  load  (  "app.txt"  )  ; 
return   this  .  matchResultToInteger  (  methodName  ,  3  ,  app2  .  getPoints  (  )  .  size  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 






public   boolean   Test23  (  String   methodName  )  { 
Application   app  =  new   Application  (  )  ; 
try  { 
app  .  load  (  "app2.txt"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchExecution  (  methodName  ,  true  )  ; 
} 
} 








public   boolean   Test24  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication1  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  23.5  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ,  "adfasfda"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Wrong Password"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test25  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication1  (  )  ; 
app  .  getDatasource  (  )  .  remove  (  1  ,  "adfasfd"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Wrong Password"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test26  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication1  (  )  ; 
app  .  getDatasource  (  )  .  remove  (  1  ,  "12345"  )  ; 
return   this  .  matchExecution  (  methodName  ,  true  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Wrong Password"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test27  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication1  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  23.5  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ,  "12345"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Wrong Password"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test28  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  0  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  45  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  1  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  2  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  45  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  3  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Average of the Values in the last hour over Border"  ,  e  .  getMessage  (  )  )  ; 
} 
} 









public   boolean   Test29  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Last two Values over Border\n"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test30  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  41  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  20  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  2  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Chicago"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  41  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "This time is earlier or equal than the last time"  ,  e  .  getMessage  (  )  )  ; 
} 
} 









public   boolean   Test31  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  -  100  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Value is not plausible"  ,  e  .  getLocalizedMessage  (  )  )  ; 
} 
} 









public   boolean   Test32  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  400  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Value is not plausible"  ,  e  .  getLocalizedMessage  (  )  )  ; 
} 
} 









public   boolean   Test33  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  20  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  1999  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Time is not plausible"  ,  e  .  getLocalizedMessage  (  )  )  ; 
} 
} 









public   boolean   Test34  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  20  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2200  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Time is not plausible"  ,  e  .  getLocalizedMessage  (  )  )  ; 
} 
} 






public   boolean   Test35  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setSelect  (  "ID"  ,  "MID"  ,  "POINT"  ,  "DATE"  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "ID: 45 MID: 1 Point: Athen Date: Thu Feb 01 08:00:00 CET 2001 Value: 5.0 Unit: C\n"  +  "ID: 45 MID: 2 Point: Athen Date: Thu Feb 01 10:00:00 CET 2001 Value: 7.0 Unit: C\n"  +  "ID: 45 MID: 3 Point: Athen Date: Thu Feb 01 12:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 45 MID: 4 Point: Athen Date: Thu Feb 01 14:00:00 CET 2001 Value: 11.0 Unit: C\n"  +  "ID: 45 MID: 5 Point: Athen Date: Thu Feb 01 16:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 45 MID: 6 Point: Athen Date: Thu Feb 01 18:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 46 MID: 1 Point: Bangladesch Date: Thu Feb 01 08:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "ID: 46 MID: 2 Point: Bangladesch Date: Thu Feb 01 10:00:00 CET 2001 Value: 17.0 Unit: C\n"  +  "ID: 46 MID: 3 Point: Bangladesch Date: Thu Feb 01 12:00:00 CET 2001 Value: 24.0 Unit: C\n"  +  "ID: 46 MID: 4 Point: Bangladesch Date: Thu Feb 01 14:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "ID: 46 MID: 5 Point: Bangladesch Date: Thu Feb 01 16:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "ID: 46 MID: 6 Point: Bangladesch Date: Thu Feb 01 18:00:00 CET 2001 Value: 22.0 Unit: C\n"  +  "ID: 47 MID: 1 Point: Chicago Date: Thu Feb 01 08:00:00 CET 2001 Value: 6.0 Unit: C\n"  +  "ID: 47 MID: 2 Point: Chicago Date: Thu Feb 01 10:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 47 MID: 3 Point: Chicago Date: Thu Feb 01 12:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 47 MID: 4 Point: Chicago Date: Thu Feb 01 14:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 47 MID: 5 Point: Chicago Date: Thu Feb 01 16:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "ID: 47 MID: 6 Point: Chicago Date: Thu Feb 01 18:00:00 CET 2001 Value: 9.0 Unit: C"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 





public   boolean   Test36  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setSelect  (  "POINT"  ,  "DATE"  )  ; 
q  .  setOrderBy  (  "DATE"  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "Point: Athen Date: Thu Feb 01 08:00:00 CET 2001 Value: 5.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 08:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 08:00:00 CET 2001 Value: 6.0 Unit: C\n"  +  "Point: Athen Date: Thu Feb 01 10:00:00 CET 2001 Value: 7.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 10:00:00 CET 2001 Value: 17.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 10:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "Point: Athen Date: Thu Feb 01 12:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 12:00:00 CET 2001 Value: 24.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 12:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "Point: Athen Date: Thu Feb 01 14:00:00 CET 2001 Value: 11.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 14:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 14:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "Point: Athen Date: Thu Feb 01 16:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 16:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 16:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "Point: Athen Date: Thu Feb 01 18:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "Point: Bangladesch Date: Thu Feb 01 18:00:00 CET 2001 Value: 22.0 Unit: C\n"  +  "Point: Chicago Date: Thu Feb 01 18:00:00 CET 2001 Value: 9.0 Unit: C"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 





public   boolean   Test37  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setFromValue  (  new   Value  (  0  ,  new   Celsius  (  )  )  )  ; 
q  .  setToValue  (  new   Value  (  15  ,  new   Celsius  (  )  )  )  ; 
q  .  setFromTime  (  new   GregorianCalendar  (  2000  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ; 
q  .  setToTime  (  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  10  ,  0  ,  0  )  )  ; 
q  .  setUnit  (  new   Kelvin  (  )  )  ; 
q  .  setSelect  (  "ID"  ,  "POINT"  ,  "DATE"  )  ; 
q  .  setOrderBy  (  "POINT"  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "ID: 51 Point: Athen Date: Thu Feb 01 08:00:00 CET 2001 Value: 278.15 Unit: K\n"  +  "ID: 51 Point: Athen Date: Thu Feb 01 10:00:00 CET 2001 Value: 280.15 Unit: K\n"  +  "ID: 52 Point: Bangladesch Date: Thu Feb 01 08:00:00 CET 2001 Value: 285.15 Unit: K\n"  +  "ID: 53 Point: Chicago Date: Thu Feb 01 08:00:00 CET 2001 Value: 279.15 Unit: K\n"  +  "ID: 53 Point: Chicago Date: Thu Feb 01 10:00:00 CET 2001 Value: 281.15 Unit: K"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 






public   boolean   Test38  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setFromValue  (  new   Value  (  0  ,  new   Celsius  (  )  )  )  ; 
q  .  setFromTime  (  new   GregorianCalendar  (  2000  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ; 
q  .  setUnit  (  new   Kelvin  (  )  )  ; 
try  { 
q  .  doQuery  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "One or several Parameters are invalid"  ,  e  .  getMessage  (  )  )  ; 
} 
} 




public   boolean   Test39  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "ID: 57 MID: 1 Point: Athen Date: Thu Feb 01 08:00:00 CET 2001 Value: 5.0 Unit: C\n"  +  "ID: 57 MID: 2 Point: Athen Date: Thu Feb 01 10:00:00 CET 2001 Value: 7.0 Unit: C\n"  +  "ID: 57 MID: 3 Point: Athen Date: Thu Feb 01 12:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 57 MID: 4 Point: Athen Date: Thu Feb 01 14:00:00 CET 2001 Value: 11.0 Unit: C\n"  +  "ID: 57 MID: 5 Point: Athen Date: Thu Feb 01 16:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 57 MID: 6 Point: Athen Date: Thu Feb 01 18:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 58 MID: 1 Point: Bangladesch Date: Thu Feb 01 08:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "ID: 58 MID: 2 Point: Bangladesch Date: Thu Feb 01 10:00:00 CET 2001 Value: 17.0 Unit: C\n"  +  "ID: 58 MID: 3 Point: Bangladesch Date: Thu Feb 01 12:00:00 CET 2001 Value: 24.0 Unit: C\n"  +  "ID: 58 MID: 4 Point: Bangladesch Date: Thu Feb 01 14:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "ID: 58 MID: 5 Point: Bangladesch Date: Thu Feb 01 16:00:00 CET 2001 Value: 27.0 Unit: C\n"  +  "ID: 58 MID: 6 Point: Bangladesch Date: Thu Feb 01 18:00:00 CET 2001 Value: 22.0 Unit: C\n"  +  "ID: 59 MID: 1 Point: Chicago Date: Thu Feb 01 08:00:00 CET 2001 Value: 6.0 Unit: C\n"  +  "ID: 59 MID: 2 Point: Chicago Date: Thu Feb 01 10:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 59 MID: 3 Point: Chicago Date: Thu Feb 01 12:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 59 MID: 4 Point: Chicago Date: Thu Feb 01 14:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 59 MID: 5 Point: Chicago Date: Thu Feb 01 16:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "ID: 59 MID: 6 Point: Chicago Date: Thu Feb 01 18:00:00 CET 2001 Value: 9.0 Unit: C"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 





public   boolean   Test40  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setSelectPoints  (  "Chicago"  ,  "Athen"  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "ID: 60 MID: 1 Point: Athen Date: Thu Feb 01 08:00:00 CET 2001 Value: 5.0 Unit: C\n"  +  "ID: 60 MID: 2 Point: Athen Date: Thu Feb 01 10:00:00 CET 2001 Value: 7.0 Unit: C\n"  +  "ID: 60 MID: 3 Point: Athen Date: Thu Feb 01 12:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 60 MID: 4 Point: Athen Date: Thu Feb 01 14:00:00 CET 2001 Value: 11.0 Unit: C\n"  +  "ID: 60 MID: 5 Point: Athen Date: Thu Feb 01 16:00:00 CET 2001 Value: 9.0 Unit: C\n"  +  "ID: 60 MID: 6 Point: Athen Date: Thu Feb 01 18:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 62 MID: 1 Point: Chicago Date: Thu Feb 01 08:00:00 CET 2001 Value: 6.0 Unit: C\n"  +  "ID: 62 MID: 2 Point: Chicago Date: Thu Feb 01 10:00:00 CET 2001 Value: 8.0 Unit: C\n"  +  "ID: 62 MID: 3 Point: Chicago Date: Thu Feb 01 12:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 62 MID: 4 Point: Chicago Date: Thu Feb 01 14:00:00 CET 2001 Value: 10.0 Unit: C\n"  +  "ID: 62 MID: 5 Point: Chicago Date: Thu Feb 01 16:00:00 CET 2001 Value: 12.0 Unit: C\n"  +  "ID: 62 MID: 6 Point: Chicago Date: Thu Feb 01 18:00:00 CET 2001 Value: 9.0 Unit: C"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 






public   boolean   Test41  (  String   methodName  )  { 
Application   app  =  this  .  createApplication1  (  )  ; 
Query   q  =  new   Query  (  app  .  getDatasource  (  )  )  ; 
q  .  setFromValue  (  new   Value  (  0  ,  new   Celsius  (  )  )  )  ; 
q  .  setToValue  (  new   Value  (  15  ,  new   Celsius  (  )  )  )  ; 
q  .  setFromTime  (  new   GregorianCalendar  (  2000  ,  1  ,  1  ,  0  ,  0  ,  0  )  )  ; 
q  .  setToTime  (  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  10  ,  0  ,  0  )  )  ; 
q  .  setUnit  (  new   Kelvin  (  )  )  ; 
q  .  setSelect  (  "ID"  ,  "POINT"  )  ; 
q  .  setOrderBy  (  "POINT"  )  ; 
q  .  setSelectPoints  (  "Bangladesch"  )  ; 
try  { 
return   this  .  matchResultToString  (  methodName  ,  "ID: 64 Point: Bangladesch Value: 285.15 Unit: K"  ,  q  .  doQuery  (  )  )  ; 
}  catch  (  ApplicationException   e  )  { 
e  .  printStackTrace  (  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
} 
} 








public   boolean   Test42  (  String   methodName  )  { 
try  { 
Application   app  =  new   Application  (  )  ; 
app  .  login  (  "sys"  ,  "12345"  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  ; 
return   matchExecution  (  methodName  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
return   matchExecution  (  methodName  ,  false  )  ; 
} 
} 









public   boolean   Test43  (  String   methodName  )  { 
try  { 
Application   app  =  new   Application  (  )  ; 
app  .  login  (  "sys"  ,  "12345"  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  .  add  (  new   Admin  (  "Peter"  ,  "0815"  )  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  .  add  (  new   Admin  (  "Horst"  ,  "1337"  )  )  ; 
app  .  login  (  "Horst"  ,  "1337"  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  ; 
return   matchExecution  (  methodName  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
return   matchExecution  (  methodName  ,  false  )  ; 
} 
} 









public   boolean   Test44  (  String   methodName  )  { 
try  { 
Application   app  =  new   Application  (  )  ; 
app  .  login  (  "sys"  ,  "12345"  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  .  add  (  new   Admin  (  "Peter"  ,  "0815"  )  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  .  add  (  new   Admin  (  "Horst"  ,  "1337"  )  )  ; 
app  .  login  (  "Horst"  ,  "1227"  )  ; 
app  .  getAuth  (  )  .  getAdmins  (  )  ; 
return   matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  Exception   e  )  { 
return   matchResultToString  (  methodName  ,  "Access Denied"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








public   boolean   Test45  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  20  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  0  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  99  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2001  ,  1  ,  1  ,  0  ,  0  ,  7  )  )  ,  "Bangladesch"  )  ; 
return   this  .  matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Value is twice Over Border\n"  ,  e  .  getMessage  (  )  )  ; 
} 
} 










public   boolean   Test46  (  String   methodName  )  { 
try  { 
Application   app  =  this  .  createApplication2  (  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  0  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  45  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  1  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  45  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  2  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  46  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  3  )  )  ,  "Bangladesch"  )  ; 
app  .  getDatasource  (  )  .  add  (  new   Measuring  (  new   Value  (  99  ,  new   Celsius  (  )  )  ,  app  .  getPoints  (  )  .  get  (  1  )  ,  new   GregorianCalendar  (  2002  ,  1  ,  1  ,  1  ,  0  ,  4  )  )  ,  "Bangladesch"  )  ; 
return   matchExecution  (  methodName  ,  false  )  ; 
}  catch  (  ApplicationException   e  )  { 
return   this  .  matchResultToString  (  methodName  ,  "Value is twice Over Border\n"  +  "Last two Values over Border\n"  +  "Average of the Values in the last hour over Border"  ,  e  .  getMessage  (  )  )  ; 
} 
} 








private   boolean   matchExecution  (  String   methodName  ,  boolean   expectedResult  )  { 
if  (  expectedResult  ==  true  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED EXECUTION\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "UNEXPECTED EXECUTION\n"  )  ; 
return   false  ; 
} 
} 

private   boolean   matchResultToBoolean  (  String   methodName  ,  Boolean   expectedResult  ,  Boolean   returnedResult  )  { 
if  (  expectedResult  ==  returnedResult  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   false  ; 
} 
} 

private   boolean   matchResultToObject  (  String   methodName  ,  Object   expectedResult  ,  Object   returnedResult  )  { 
if  (  expectedResult  .  equals  (  returnedResult  )  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   false  ; 
} 
} 

private   boolean   matchResultToInteger  (  String   methodName  ,  int   expectedResult  ,  int   returnedResult  )  { 
if  (  expectedResult  ==  returnedResult  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   false  ; 
} 
} 

private   boolean   matchResultToDouble  (  String   methodName  ,  double   expectedResult  ,  double   returnedResult  )  { 
if  (  expectedResult  ==  returnedResult  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   false  ; 
} 
} 








private   boolean   matchResultToString  (  String   methodName  ,  String   expectedResult  ,  String   returnedResult  )  { 
if  (  expectedResult  .  equals  (  returnedResult  )  )  { 
System  .  out  .  println  (  "SUCCESS@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   true  ; 
}  else  { 
System  .  out  .  println  (  "FAILURE@"  +  methodName  )  ; 
System  .  out  .  println  (  "EXPECTED"  )  ; 
System  .  out  .  println  (  expectedResult  )  ; 
System  .  out  .  println  (  "RETURNED"  )  ; 
System  .  out  .  println  (  returnedResult  +  "\n"  )  ; 
return   false  ; 
} 
} 

public   void   run  (  )  { 
Method  [  ]  methods  =  this  .  getClass  (  )  .  getMethods  (  )  ; 
int   countTestCases  =  0  ; 
int   countSuccessTestCases  =  0  ; 
for  (  int   i  =  0  ;  i  <  methods  .  length  ;  i  ++  )  { 
if  (  methods  [  i  ]  .  getName  (  )  .  length  (  )  >  4  )  { 
if  (  methods  [  i  ]  .  getName  (  )  .  substring  (  0  ,  4  )  .  equals  (  "Test"  )  )  { 
countTestCases  ++  ; 
try  { 
if  (  (  Boolean  )  (  methods  [  i  ]  .  invoke  (  this  ,  methods  [  i  ]  .  getName  (  )  .  substring  (  4  )  )  )  )  { 
countSuccessTestCases  ++  ; 
} 
}  catch  (  IllegalArgumentException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  IllegalAccessException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  InvocationTargetException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 
System  .  out  .  println  (  "------------------------"  )  ; 
System  .  out  .  println  (  "Testcases: "  +  countTestCases  )  ; 
System  .  out  .  println  (  "Success  : "  +  countSuccessTestCases  )  ; 
System  .  out  .  println  (  "Failure  : "  +  (  countTestCases  -  countSuccessTestCases  )  )  ; 
} 





public   static   void   main  (  String  [  ]  args  )  { 
Test   test  =  new   Test  (  )  ; 
test  .  run  (  )  ; 
} 
} 

