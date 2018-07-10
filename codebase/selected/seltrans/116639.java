package   ossobook  .  client  .  io  .  tunnel  ; 

import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  security  .  KeyManagementException  ; 
import   java  .  security  .  KeyStore  ; 
import   java  .  security  .  KeyStoreException  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  UnrecoverableKeyException  ; 
import   java  .  security  .  cert  .  CertificateException  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   javax  .  net  .  ServerSocketFactory  ; 
import   javax  .  net  .  ssl  .  KeyManagerFactory  ; 
import   javax  .  net  .  ssl  .  SSLContext  ; 
import   javax  .  net  .  ssl  .  SSLSocket  ; 
import   javax  .  net  .  ssl  .  SSLSocketFactory  ; 
import   javax  .  net  .  ssl  .  TrustManager  ; 
import   javax  .  net  .  ssl  .  X509TrustManager  ; 
import   ossobook  .  client  .  OssoBook  ; 
import   ossobook  .  client  .  config  .  ConfigurationOssobook  ; 



































public   class   Tunnel  { 




private   static   boolean   initialized  =  false  ; 




private   Tunnel  (  )  { 
} 












public   static   synchronized   boolean   init  (  )  { 
if  (  !  initialized  )  { 
try  { 
new   Acceptor  (  )  .  start  (  )  ; 
initialized  =  true  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "Failed starting tunnel."  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 
return   initialized  ; 
} 





private   static   class   Acceptor   extends   Thread  { 




private   ServerSocket   server  ; 





private   String   backendHost  ; 




private   int   backendPort  ; 







public   Acceptor  (  )  throws   IOException  { 
backendHost  =  OssoBook  .  config  .  getProperty  (  "global.host"  )  ; 
backendPort  =  Integer  .  parseInt  (  OssoBook  .  config  .  getProperty  (  "tunnel.backendport"  )  )  ; 
server  =  ServerSocketFactory  .  getDefault  (  )  .  createServerSocket  (  Integer  .  parseInt  (  OssoBook  .  config  .  getProperty  (  "tunnel.listenport"  )  )  )  ; 
} 




@  Override 
public   void   run  (  )  { 
try  { 
TrustManager  [  ]  trustAllCerts  =  new   TrustManager  [  ]  {  new   X509TrustManager  (  )  { 

public   X509Certificate  [  ]  getAcceptedIssuers  (  )  { 
return   null  ; 
} 

public   void   checkClientTrusted  (  X509Certificate  [  ]  certs  ,  String   authType  )  { 
} 

public   void   checkServerTrusted  (  X509Certificate  [  ]  certs  ,  String   authType  )  { 
} 
}  }  ; 
SSLContext   sc  =  SSLContext  .  getInstance  (  "SSLv3"  )  ; 
KeyManagerFactory   kmf  =  KeyManagerFactory  .  getInstance  (  "SunX509"  )  ; 
KeyStore   ks  =  KeyStore  .  getInstance  (  "JKS"  )  ; 
ks  .  load  (  new   FileInputStream  (  ConfigurationOssobook  .  CONFIG_DIR  +  "tunnel.jks"  )  ,  "ossobook"  .  toCharArray  (  )  )  ; 
kmf  .  init  (  ks  ,  "ossobook"  .  toCharArray  (  )  )  ; 
sc  .  init  (  kmf  .  getKeyManagers  (  )  ,  trustAllCerts  ,  null  )  ; 
SSLSocketFactory   sf  =  sc  .  getSocketFactory  (  )  ; 
while  (  true  )  { 
Socket   client  =  server  .  accept  (  )  ; 
try  { 
SSLSocket   backend  =  (  SSLSocket  )  sf  .  createSocket  (  backendHost  ,  backendPort  )  ; 
backend  .  startHandshake  (  )  ; 
String   hash  =  OssoBook  .  config  .  getProperty  (  "tunnel.authkey"  )  ; 
if  (  !  hash  .  equals  (  ""  )  )  { 
backend  .  getOutputStream  (  )  .  write  (  (  hash  +  "\n"  )  .  getBytes  (  )  )  ; 
} 
new   Forwarder  (  client  ,  backend  )  .  start  (  )  ; 
new   Forwarder  (  backend  ,  client  )  .  start  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
if  (  client  !=  null  &&  client  .  isConnected  (  )  )  { 
try  { 
client  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
} 
} 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  KeyStoreException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  CertificateException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  UnrecoverableKeyException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  catch  (  KeyManagementException   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
try  { 
server  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 





private   static   class   Forwarder   extends   Thread  { 




private   Socket   from  ; 




private   Socket   to  ; 












public   Forwarder  (  Socket   from  ,  Socket   to  )  { 
this  .  from  =  from  ; 
this  .  to  =  to  ; 
} 




@  Override 
public   void   run  (  )  { 
try  { 
while  (  true  )  { 
to  .  getOutputStream  (  )  .  write  (  from  .  getInputStream  (  )  .  read  (  )  )  ; 
} 
}  catch  (  IOException   e  )  { 
if  (  from  .  isConnected  (  )  )  { 
try  { 
from  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
} 
if  (  to  .  isConnected  (  )  )  { 
try  { 
to  .  close  (  )  ; 
}  catch  (  IOException   e1  )  { 
} 
} 
} 
} 
} 
} 

