package   hypercast  ; 

import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 












public   class   HTTP_ServerUtility  { 


public   static   final   int   MAX_SERVER_RESPONSE_SIZE  =  8192  ; 



public   static   String   query  (  String   urlString  )  throws   MalformedURLException  ,  IOException  { 
URL   url  =  new   URL  (  urlString  )  ; 
String   result  =  null  ; 
HttpURLConnection   connection  =  null  ; 
try  { 
connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
result  =  firstLineFromStream  (  connection  .  getInputStream  (  )  )  ; 
}  finally  { 
connection  .  disconnect  (  )  ; 
} 
return   result  ; 
} 



public   static   byte  [  ]  query  (  String   server_query_prefix  ,  String   overlayID  ,  byte  [  ]  input_message  )  throws   MalformedURLException  ,  IOException  { 
String   urlString  =  server_query_prefix  +  "?cmd=msg&OverlayID="  +  encodeURLString  (  overlayID  )  +  "&msg="  +  toHexString  (  input_message  )  ; 
URL   url  =  new   URL  (  urlString  )  ; 
String   result  =  null  ; 
HttpURLConnection   connection  =  null  ; 
try  { 
connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
result  =  firstLineFromStream  (  connection  .  getInputStream  (  )  )  ; 
}  finally  { 
connection  .  disconnect  (  )  ; 
} 
return   toByteArray  (  result  )  ; 
} 


private   static   String   firstLineFromStream  (  InputStream   response_is  )  throws   IOException  { 
InputStreamReader   response_isr  =  new   InputStreamReader  (  response_is  )  ; 
BufferedReader   response  =  new   BufferedReader  (  response_isr  ,  MAX_SERVER_RESPONSE_SIZE  )  ; 
try  { 
String   s  =  response  .  readLine  (  )  ; 
response  .  close  (  )  ; 
return   s  ; 
}  finally  { 
response  .  close  (  )  ; 
} 
} 

public   static   String   toHexString  (  byte  [  ]  a  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  a  .  length  ;  i  ++  )  { 
final   int   high_order_nibble  =  (  a  [  i  ]  >  >  >  4  )  &  0xF  ; 
if  (  high_order_nibble  <  10  )  sb  .  append  (  (  char  )  (  '0'  +  high_order_nibble  )  )  ;  else   if  (  high_order_nibble  >=  10  &&  high_order_nibble  <  16  )  sb  .  append  (  (  char  )  (  'A'  +  (  high_order_nibble  -  10  )  )  )  ;  else  { 
throw   new   HyperCastFatalRuntimeException  (  "ERROR translating high order nibble:"  +  high_order_nibble  )  ; 
} 
final   int   low_order_nibble  =  a  [  i  ]  &  0xF  ; 
if  (  low_order_nibble  <  10  )  sb  .  append  (  (  char  )  (  '0'  +  low_order_nibble  )  )  ;  else   if  (  low_order_nibble  >=  10  &&  low_order_nibble  <  16  )  sb  .  append  (  (  char  )  (  'A'  +  (  low_order_nibble  -  10  )  )  )  ;  else  { 
throw   new   HyperCastFatalRuntimeException  (  "ERROR translating low order nibble:"  +  low_order_nibble  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   byte  [  ]  toByteArray  (  String   s  )  { 
byte  [  ]  buffer  =  new   byte  [  s  .  length  (  )  /  2  ]  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  s  .  length  (  )  ;  i  +=  2  )  { 
byte   output_byte  =  0  ; 
char   first_char  =  s  .  charAt  (  i  )  ; 
if  (  first_char  ==  '\n'  ||  first_char  ==  '\r'  )  break  ;  else   if  (  first_char  >=  'A'  &&  first_char  <=  'F'  )  output_byte  =  (  byte  )  (  (  (  byte  )  (  first_char  -  'A'  +  10  )  )  <<  4  )  ;  else   if  (  first_char  >=  '0'  &&  first_char  <=  '9'  )  output_byte  =  (  byte  )  (  (  (  byte  )  (  first_char  -  '0'  )  )  <<  4  )  ;  else  { 
System  .  err  .  println  (  "Error at webserver server.  Received msg: \""  +  s  +  "\""  )  ; 
return   null  ; 
} 
char   second_char  =  s  .  charAt  (  i  +  1  )  ; 
if  (  second_char  ==  '\n'  ||  second_char  ==  '\r'  )  break  ;  else   if  (  second_char  >=  'A'  &&  second_char  <=  'F'  )  output_byte  |=  (  (  byte  )  (  second_char  -  'A'  +  10  )  )  ;  else   if  (  second_char  >=  '0'  &&  second_char  <=  '9'  )  output_byte  |=  (  (  byte  )  (  second_char  -  '0'  )  )  ;  else  { 
System  .  err  .  println  (  "Error at webserver server.  Received msg: \""  +  s  +  "\""  )  ; 
return   null  ; 
} 
buffer  [  i  /  2  ]  =  output_byte  ; 
} 
byte  [  ]  output_buffer  =  new   byte  [  i  /  2  ]  ; 
System  .  arraycopy  (  buffer  ,  0  ,  output_buffer  ,  0  ,  i  /  2  )  ; 
return   output_buffer  ; 
} 








public   static   String   decodeURLString  (  String   s  )  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  ++  )  { 
if  (  s  .  charAt  (  i  )  ==  '+'  )  sb  .  append  (  " "  )  ;  else   if  (  s  .  charAt  (  i  )  ==  '%'  )  { 
if  (  i  +  2  >=  s  .  length  (  )  )  throw   new   IllegalArgumentException  (  "URL String was poorly formatted!"  )  ; 
char   first_char  =  s  .  charAt  (  i  +  1  )  ; 
char   second_char  =  s  .  charAt  (  i  +  2  )  ; 
byte   output_byte  =  0  ; 
if  (  first_char  >=  'A'  &&  first_char  <=  'F'  )  output_byte  =  (  byte  )  (  (  (  byte  )  (  first_char  -  'A'  +  10  )  )  <<  4  )  ;  else   if  (  first_char  >=  '0'  &&  first_char  <=  '9'  )  output_byte  =  (  byte  )  (  (  (  byte  )  (  first_char  -  '0'  )  )  <<  4  )  ;  else   throw   new   IllegalArgumentException  (  "URL String was poorly formatted!"  )  ; 
if  (  second_char  >=  'A'  &&  second_char  <=  'F'  )  output_byte  |=  (  (  byte  )  (  second_char  -  'A'  +  10  )  )  ;  else   if  (  second_char  >=  '0'  &&  second_char  <=  '9'  )  output_byte  |=  (  (  byte  )  (  second_char  -  '0'  )  )  ;  else   throw   new   IllegalArgumentException  (  "URL String was poorly formatted!"  )  ; 
sb  .  append  (  (  char  )  output_byte  )  ; 
i  +=  2  ; 
}  else  { 
sb  .  append  (  s  .  charAt  (  i  )  )  ; 
} 
} 
return   sb  .  toString  (  )  ; 
} 







public   static   String   encodeURLString  (  String   s  )  { 
return   URLEncoder  .  encode  (  s  )  ; 
} 
} 

