package   org  .  apache  .  wicket  .  examples  .  stockquote  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  ProtocolException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 





public   class   StockQuote  { 





private   static   final   String   serviceUrl  =  "http://www.webservicex.net/stockquote.asmx"  ; 


private   String   symbol  ; 




public   StockQuote  (  )  { 
} 







public   StockQuote  (  String   symbol  )  { 
this  .  symbol  =  symbol  ; 
} 






public   String   getSymbol  (  )  { 
return   symbol  ; 
} 






public   void   setSymbol  (  String   symbol  )  { 
this  .  symbol  =  symbol  ; 
} 






public   String   getQuote  (  )  { 
final   String   response  =  getSOAPQuote  (  symbol  )  ; 
int   start  =  response  .  indexOf  (  "&lt;Last&gt;"  )  +  "&lt;Last&gt;"  .  length  (  )  ; 
int   end  =  response  .  indexOf  (  "&lt;/Last&gt;"  )  ; 
if  (  start  <  "&lt;Last&gt;"  .  length  (  )  )  { 
return  "(unknown)"  ; 
} 
String   result  =  response  .  substring  (  start  ,  end  )  ; 
return   result  .  equals  (  "0.00"  )  ?  "(unknown)"  :  result  ; 
} 








private   String   getSOAPQuote  (  String   symbol  )  { 
String   response  =  ""  ; 
try  { 
final   URL   url  =  new   URL  (  serviceUrl  )  ; 
final   String   message  =  createMessage  (  symbol  )  ; 
HttpURLConnection   httpConn  =  setUpHttpConnection  (  url  ,  message  .  length  (  )  )  ; 
writeRequest  (  message  ,  httpConn  )  ; 
response  =  readResult  (  httpConn  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   response  ; 
} 










private   void   writeRequest  (  String   message  ,  HttpURLConnection   httpConn  )  throws   IOException  { 
OutputStream   out  =  httpConn  .  getOutputStream  (  )  ; 
out  .  write  (  message  .  getBytes  (  )  )  ; 
out  .  close  (  )  ; 
} 












private   HttpURLConnection   setUpHttpConnection  (  URL   url  ,  int   length  )  throws   IOException  ,  ProtocolException  { 
URLConnection   connection  =  url  .  openConnection  (  )  ; 
HttpURLConnection   httpConn  =  (  HttpURLConnection  )  connection  ; 
httpConn  .  setRequestProperty  (  "Content-Length"  ,  String  .  valueOf  (  length  )  )  ; 
httpConn  .  setRequestProperty  (  "Content-Type"  ,  "text/xml; charset=utf-8"  )  ; 
httpConn  .  setRequestProperty  (  "SOAPAction"  ,  "\"http://www.webserviceX.NET/GetQuote\""  )  ; 
httpConn  .  setRequestMethod  (  "POST"  )  ; 
httpConn  .  setDoOutput  (  true  )  ; 
httpConn  .  setDoInput  (  true  )  ; 
return   httpConn  ; 
} 









private   String   readResult  (  HttpURLConnection   connection  )  throws   IOException  { 
InputStream   inputStream  =  connection  .  getInputStream  (  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  inputStream  )  ; 
BufferedReader   in  =  new   BufferedReader  (  isr  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
String   inputLine  ; 
while  (  (  inputLine  =  in  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  inputLine  )  ; 
} 
in  .  close  (  )  ; 
return   sb  .  toString  (  )  ; 
} 








private   String   createMessage  (  String   symbol  )  { 
StringBuffer   message  =  new   StringBuffer  (  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  )  ; 
message  .  append  (  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"  )  ; 
message  .  append  (  "  <soap:Body>"  )  ; 
message  .  append  (  "    <GetQuote xmlns=\"http://www.webserviceX.NET/\">"  )  ; 
message  .  append  (  "      <symbol>"  )  .  append  (  symbol  )  .  append  (  "</symbol>"  )  ; 
message  .  append  (  "    </GetQuote>"  )  ; 
message  .  append  (  "  </soap:Body>"  )  ; 
message  .  append  (  "</soap:Envelope>"  )  ; 
return   message  .  toString  (  )  ; 
} 
} 

