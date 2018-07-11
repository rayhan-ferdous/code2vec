package   quietcoffee  .  ssh  ; 

import   java  .  io  .  *  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  net  .  *  ; 
import   java  .  security  .  *  ; 
import   java  .  text  .  *  ; 








public   class   SSHConnection  { 


public   static   final   String   SSH_VERSION  =  "Quiet Coffee (SSH-Java) 0.5"  ; 


public   static   final   int   PROTOCOL_MAJOR_1  =  1  ; 


public   static   final   int   PROTOCOL_MINOR_1  =  5  ; 


public   static   final   int   PROTOCOL_MAJOR_2  =  2  ; 


public   static   final   int   PROTOCOL_MINOR_2  =  0  ; 

private   static   final   int   SSH_SMSG_SUCCESS  =  14  ; 

private   static   final   int   SSH_SMSG_PUBLIC_KEY  =  2  ; 

private   static   final   int   SSH_CMSG_SESSION_KEY  =  3  ; 





private   static   final   int   SSH_SESSION_KEY_LENGTH  =  32  ; 





private   static   final   int   SSH_KEY_BITS_RESERVED  =  128  ; 


private   static   final   int   SSH_PROTOFLAG_SCREEN_NUMBER  =  1  ; 


private   static   final   int   SSH_PROTOFLAG_HOST_IN_FWD_OPEN  =  2  ; 


private   static   final   int   SSH_DEFAULT_PORT  =  22  ; 


private   Socket   socket  =  null  ; 


private   Packet   packet  =  null  ; 


private   byte  [  ]  sessionId  =  null  ; 


private   int   supportedAuthentications  =  0  ; 


private   Options   options  =  null  ; 


private   Compatibility   compatibility  =  new   Compatibility  (  )  ; 


private   java  .  util  .  Random   random  =  null  ; 




public   SSHConnection  (  Options   options  )  { 
this  .  options  =  options  ; 
} 


















public   void   connect  (  String   host  ,  int   port  ,  int   connectionAttempts  ,  String   proxyCommand  )  throws   UnknownHostException  ,  ConnectionAbortedException  ,  ConnectionRefusedException  ,  SocketException  { 
boolean   fullFailure  =  true  ; 
InputStream   sockIn  =  null  ; 
OutputStream   sockOut  =  null  ; 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.connect(): unix guff"  )  ; 
if  (  port  ==  0  )  { 
port  =  SSH_DEFAULT_PORT  ; 
} 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.connect(): proxy command"  )  ; 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:connect() ipv6 support"  )  ; 
InetAddress   inetAddresses  [  ]  =  InetAddress  .  getAllByName  (  host  )  ; 
int   attempt  ; 
for  (  attempt  =  0  ;  ;  )  { 
if  (  attempt  >  0  )  { 
Log  .  getLogInstance  (  )  .  debug  (  "Trying again..."  )  ; 
} 
for  (  int   ai  =  0  ;  ai  <  inetAddresses  .  length  ;  ai  ++  )  { 
Log  .  getLogInstance  (  )  .  debug  (  "Connecting to "  +  host  +  " ["  +  inetAddresses  [  ai  ]  .  getHostAddress  (  )  +  "] port "  +  port  +  "."  )  ; 
try  { 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:connect() socket parameters"  )  ; 
socket  =  new   Socket  (  inetAddresses  [  ai  ]  ,  port  )  ; 
sockIn  =  socket  .  getInputStream  (  )  ; 
sockOut  =  socket  .  getOutputStream  (  )  ; 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:connect() extra connect to host"  )  ; 
}  catch  (  java  .  io  .  IOException   e  )  { 
continue  ; 
} 
} 
if  (  socket  !=  null  )  { 
break  ; 
} 
attempt  ++  ; 
if  (  attempt  >=  connectionAttempts  )  break  ; 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  attempt  >=  connectionAttempts  )  { 
if  (  fullFailure  )  { 
throw   new   ConnectionAbortedException  (  )  ; 
}  else  { 
throw   new   ConnectionRefusedException  (  )  ; 
} 
} 
Log  .  getLogInstance  (  )  .  debug  (  "Connection established."  )  ; 
socket  .  setSoLinger  (  true  ,  5  )  ; 
socket  .  setKeepAlive  (  options  .  isKeepalives  (  )  )  ; 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.java:connect() different random - java.security.SecureRandom?"  )  ; 
random  =  new   java  .  util  .  Random  (  )  ; 
packet  =  new   Packet  (  sockIn  ,  sockOut  ,  compatibility  ,  random  )  ; 
packet  .  setPacketDebug  (  options  .  isPacketDebug  (  )  )  ; 
} 




public   void   close  (  )  { 
try  { 
if  (  packet  !=  null  )  { 
packet  .  close  (  )  ; 
} 
if  (  socket  !=  null  )  { 
socket  .  close  (  )  ; 
} 
}  catch  (  java  .  io  .  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 












public   void   login  (  String   origHost  ,  String   serverUser  )  throws   SSHException  ,  IOException  { 
String   localUser  =  System  .  getProperty  (  "user.name"  )  ; 
String   host  =  origHost  .  toLowerCase  (  )  ; 
exchangeIdentification  (  )  ; 
if  (  compatibility  .  isCompat20  (  )  )  { 
keyExchange2  (  host  )  ; 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.login() user auth"  )  ; 
}  else  { 
keyExchange1  (  host  )  ; 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.login() params to auth"  )  ; 
userAuth1  (  localUser  ,  serverUser  ,  host  )  ; 
} 
} 







private   void   exchangeIdentification  (  )  throws   SSHException  ,  IOException  { 
byte  [  ]  buf  =  new   byte  [  256  ]  ; 
int   minor1  =  PROTOCOL_MINOR_1  ; 
for  (  ;  ;  )  { 
for  (  int   i  =  0  ;  i  <  buf  .  length  -  1  ;  i  ++  )  { 
int   len  =  packet  .  atomicRead  (  buf  ,  i  ,  1  )  ; 
if  (  len  !=  1  )  { 
throw   new   SSHException  (  "SSHConnection:exchangeIdentification(): Connection closed by remote host"  )  ; 
} 
if  (  buf  [  i  ]  ==  '\r'  )  { 
buf  [  i  ]  =  '\n'  ; 
buf  [  i  +  1  ]  =  0  ; 
continue  ; 
} 
if  (  buf  [  i  ]  ==  '\n'  )  { 
buf  [  i  +  1  ]  =  0  ; 
break  ; 
} 
} 
buf  [  buf  .  length  -  1  ]  =  0  ; 
String   str  =  new   String  (  buf  ,  0  ,  4  )  ; 
if  (  str  .  equals  (  "SSH-"  )  )  { 
break  ; 
} 
Log  .  getLogInstance  (  )  .  debug  (  "SSHConnection:exchangeIdentification(): "  +  new   String  (  buf  )  )  ; 
} 
String   serverVersionString  =  new   String  (  buf  )  ; 
int   remoteMajor  =  0  ,  remoteMinor  =  0  ; 
String   remoteVersion  =  ""  ; 
try  { 
MessageFormat   fmt  =  new   MessageFormat  (  "SSH-{0,number,integer}.{1,number,integer}-{2}\n"  )  ; 
Object  [  ]  params  =  fmt  .  parse  (  serverVersionString  )  ; 
remoteMajor  =  (  (  Long  )  params  [  0  ]  )  .  intValue  (  )  ; 
remoteMinor  =  (  (  Long  )  params  [  1  ]  )  .  intValue  (  )  ; 
remoteVersion  =  (  String  )  params  [  2  ]  ; 
}  catch  (  ParseException   e  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "Bad remote protocol version identification: "  +  serverVersionString  )  ; 
} 
Log  .  getLogInstance  (  )  .  debug  (  "Remote protocol version "  +  remoteMajor  +  "."  +  remoteMinor  +  ", remote software version "  +  remoteVersion  )  ; 
compatibility  .  dataFellows  (  remoteVersion  )  ; 
boolean   mismatch  =  false  ; 
switch  (  remoteMajor  )  { 
case   1  : 
if  (  remoteMinor  ==  99  &&  (  options  .  getProtocol  (  )  &  Compatibility  .  SSH_PROTO_2  )  !=  0  &&  (  options  .  getProtocol  (  )  &  Compatibility  .  SSH_PROTO_1_PREFERRED  )  ==  0  )  { 
compatibility  .  setCompat20  (  true  )  ; 
break  ; 
} 
if  (  (  options  .  getProtocol  (  )  &  Compatibility  .  SSH_PROTO_1  )  ==  0  )  { 
mismatch  =  true  ; 
break  ; 
} 
if  (  remoteMinor  <  3  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "Remote machine has too old SSH software version."  )  ; 
}  else   if  (  remoteMinor  ==  3  ||  remoteMinor  ==  4  )  { 
compatibility  .  setCompat13  (  true  )  ; 
minor1  =  3  ; 
if  (  options  .  isForwardAgent  (  )  )  { 
Log  .  getLogInstance  (  )  .  log  (  "Agent forwarding disabled for protocol 1.3"  )  ; 
options  .  setForwardAgent  (  false  )  ; 
} 
} 
break  ; 
case   2  : 
if  (  (  options  .  getProtocol  (  )  &  Compatibility  .  SSH_PROTO_2  )  !=  0  )  { 
compatibility  .  setCompat20  (  true  )  ; 
break  ; 
} 
default  : 
mismatch  =  true  ; 
break  ; 
} 
if  (  mismatch  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "Protocol major versions differ: "  +  (  (  options  .  getProtocol  (  )  &  Compatibility  .  SSH_PROTO_2  )  !=  0  ?  PROTOCOL_MAJOR_2  :  PROTOCOL_MAJOR_1  )  +  " vs. "  +  remoteMajor  )  ; 
} 
String   clientVersionString  =  "SSH-"  +  (  compatibility  .  isCompat20  (  )  ?  PROTOCOL_MAJOR_2  :  PROTOCOL_MAJOR_1  )  +  "."  +  (  compatibility  .  isCompat20  (  )  ?  PROTOCOL_MINOR_2  :  minor1  )  +  "-"  +  SSH_VERSION  +  "\n"  ; 
packet  .  atomicWrite  (  clientVersionString  .  getBytes  (  )  ,  0  ,  clientVersionString  .  length  (  )  )  ; 
int   index  =  clientVersionString  .  indexOf  (  "\n"  )  ; 
if  (  index  >=  0  )  { 
clientVersionString  =  clientVersionString  .  substring  (  0  ,  index  )  ; 
} 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:exchangeIdentification() chop server string"  )  ; 
Log  .  getLogInstance  (  )  .  debug  (  "Local version string "  +  clientVersionString  )  ; 
} 





public   Packet   getPacketHandler  (  )  { 
return   packet  ; 
} 







private   void   keyExchange1  (  String   host  )  throws   IOException  ,  SSHException  { 
byte  [  ]  sessionKey  =  new   byte  [  SSH_SESSION_KEY_LENGTH  ]  ; 
int   sshCipherDefault  =  CipherDetails  .  SSH_CIPHER_3DES  ; 
Log  .  getLogInstance  (  )  .  debug  (  "Waiting for server public key."  )  ; 
packet  .  readExpect  (  SSH_SMSG_PUBLIC_KEY  )  ; 
byte  [  ]  cookie  =  new   byte  [  8  ]  ; 
for  (  int   i  =  0  ;  i  <  8  ;  i  ++  )  { 
cookie  [  i  ]  =  packet  .  getChar  (  )  ; 
} 
int   bits  =  packet  .  getInt  (  )  ; 
int   startLength  =  packet  .  getIncomingPacket  (  )  .  getLength  (  )  ; 
PublicKey   publicKey  =  new   PublicKey  (  )  ; 
publicKey  .  setExponent  (  packet  .  getBigNum  (  )  )  ; 
publicKey  .  setModulus  (  packet  .  getBigNum  (  )  )  ; 
int   sumLen  =  startLength  -  packet  .  getIncomingPacket  (  )  .  getLength  (  )  ; 
int   pbits  =  publicKey  .  getModulus  (  )  .  bitLength  (  )  ; 
if  (  bits  !=  pbits  )  { 
Log  .  getLogInstance  (  )  .  log  (  "Warning: Server lies about size of server public key: "  +  "actual size is "  +  pbits  +  " bits vs. announced "  +  bits  +  "."  )  ; 
Log  .  getLogInstance  (  )  .  log  (  "Warning: This may be due to an old implementation of ssh."  )  ; 
} 
PublicKey   hostKey  =  new   PublicKey  (  )  ; 
bits  =  packet  .  getInt  (  )  ; 
startLength  =  packet  .  getIncomingPacket  (  )  .  getLength  (  )  ; 
hostKey  .  setExponent  (  packet  .  getBigNum  (  )  )  ; 
hostKey  .  setModulus  (  packet  .  getBigNum  (  )  )  ; 
sumLen  +=  (  startLength  -  packet  .  getIncomingPacket  (  )  .  getLength  (  )  )  ; 
int   hbits  =  hostKey  .  getModulus  (  )  .  bitLength  (  )  ; 
if  (  bits  !=  hbits  )  { 
Log  .  getLogInstance  (  )  .  log  (  "Warning: Server lies about size of server host key: "  +  "actual size is "  +  hbits  +  " bits vs. announced "  +  bits  +  "."  )  ; 
Log  .  getLogInstance  (  )  .  log  (  "Warning: This may be due to an old implementation of ssh."  )  ; 
} 
int   serverFlags  =  packet  .  getInt  (  )  ; 
packet  .  setProtocolFlags  (  serverFlags  )  ; 
int   supportedCiphers  =  packet  .  getInt  (  )  ; 
supportedAuthentications  =  packet  .  getInt  (  )  ; 
packet  .  checkEOM  (  )  ; 
Log  .  getLogInstance  (  )  .  debug  (  "Received server public key ("  +  pbits  +  " bits) and host key ("  +  hbits  +  " bits)."  )  ; 
if  (  verifyHostKey  (  host  ,  hostKey  )  ==  false  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "Host key verification failed"  )  ; 
} 
int   clientFlags  =  SSH_PROTOFLAG_SCREEN_NUMBER  |  SSH_PROTOFLAG_HOST_IN_FWD_OPEN  ; 
sessionId  =  computeSessionId  (  cookie  ,  hostKey  .  getModulus  (  )  ,  publicKey  .  getModulus  (  )  )  ; 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:keyExchange() arc4random stir"  )  ; 
long   rand  =  0  ; 
for  (  int   i  =  0  ;  i  <  32  ;  i  ++  )  { 
if  (  i  %  4  ==  0  )  { 
rand  =  random  .  nextInt  (  )  ; 
} 
sessionKey  [  i  ]  =  (  byte  )  (  rand  &  0xFF  )  ; 
rand  >  >=  8  ; 
} 
byte  [  ]  newKey  =  new   byte  [  SSH_SESSION_KEY_LENGTH  ]  ; 
for  (  int   i  =  0  ;  i  <  SSH_SESSION_KEY_LENGTH  ;  i  ++  )  { 
if  (  i  <  16  )  { 
newKey  [  i  ]  =  (  byte  )  (  (  sessionKey  [  i  ]  ^  sessionId  [  i  ]  )  &  0xFF  )  ; 
}  else  { 
newKey  [  i  ]  =  sessionKey  [  i  ]  ; 
} 
} 
BigInteger   key  =  new   BigInteger  (  1  ,  newKey  )  ; 
if  (  publicKey  .  getModulus  (  )  .  compareTo  (  hostKey  .  getModulus  (  )  )  <  0  )  { 
if  (  hostKey  .  getModulus  (  )  .  bitLength  (  )  <  publicKey  .  getModulus  (  )  .  bitLength  (  )  +  SSH_KEY_BITS_RESERVED  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "SSHConnection:keyExchange1(): host_key "  +  hostKey  .  getModulus  (  )  .  bitLength  (  )  +  " < public_key "  +  publicKey  .  getModulus  (  )  .  bitLength  (  )  +  " + SSH_KEY_BITS_RESERVED "  +  SSH_KEY_BITS_RESERVED  )  ; 
} 
key  =  publicKey  .  rsaPublicEncrypt  (  key  ,  random  )  ; 
key  =  hostKey  .  rsaPublicEncrypt  (  key  ,  random  )  ; 
}  else  { 
if  (  publicKey  .  getModulus  (  )  .  bitLength  (  )  <  hostKey  .  getModulus  (  )  .  bitLength  (  )  +  SSH_KEY_BITS_RESERVED  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "SSHConncetion:keyExchange1(): public_key "  +  publicKey  .  getModulus  (  )  .  bitLength  (  )  +  " < host_key "  +  hostKey  .  getModulus  (  )  .  bitLength  (  )  +  " + SSH_KEY_BITS_RESERVED "  +  SSH_KEY_BITS_RESERVED  )  ; 
} 
key  =  hostKey  .  rsaPublicEncrypt  (  key  ,  random  )  ; 
key  =  publicKey  .  rsaPublicEncrypt  (  key  ,  random  )  ; 
} 
publicKey  =  hostKey  =  null  ; 
if  (  options  .  getCipher  (  )  ==  CipherDetails  .  SSH_CIPHER_NOT_SET  )  { 
if  (  (  CipherDetails  .  maskSsh1  (  true  )  &  supportedCiphers  &  (  1  <<  sshCipherDefault  )  )  !=  0  )  { 
options  .  setCipher  (  sshCipherDefault  )  ; 
} 
}  else   if  (  options  .  getCipher  (  )  ==  CipherDetails  .  SSH_CIPHER_ILLEGAL  ||  (  CipherDetails  .  maskSsh1  (  true  )  &  (  1  <<  options  .  getCipher  (  )  )  )  ==  0  )  { 
Log  .  getLogInstance  (  )  .  log  (  "No valid SSH1 cipher, using "  +  CipherFactory  .  getCipherName  (  sshCipherDefault  )  +  " instead."  )  ; 
options  .  setCipher  (  sshCipherDefault  )  ; 
} 
if  (  (  supportedCiphers  &  (  1  <<  options  .  getCipher  (  )  )  )  ==  0  )  { 
Log  .  getLogInstance  (  )  .  fatal  (  "Selected cipher type "  +  CipherFactory  .  getCipherName  (  options  .  getCipher  (  )  )  +  " not supported by server."  )  ; 
} 
Log  .  getLogInstance  (  )  .  debug  (  "Encryption type: "  +  CipherFactory  .  getCipherName  (  options  .  getCipher  (  )  )  )  ; 
packet  .  start  (  SSH_CMSG_SESSION_KEY  )  ; 
packet  .  putChar  (  (  byte  )  options  .  getCipher  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  8  ;  i  ++  )  { 
packet  .  putChar  (  cookie  [  i  ]  )  ; 
} 
packet  .  putBigNum  (  key  )  ; 
key  =  null  ; 
packet  .  putInt  (  clientFlags  )  ; 
packet  .  send  (  )  ; 
packet  .  writeWait  (  )  ; 
Log  .  getLogInstance  (  )  .  debug  (  "Sent encrypted session key."  )  ; 
packet  .  setEncryptionKey  (  sessionKey  ,  SSH_SESSION_KEY_LENGTH  ,  options  .  getCipher  (  )  )  ; 
sessionKey  =  null  ; 
packet  .  readExpect  (  SSH_SMSG_SUCCESS  )  ; 
Log  .  getLogInstance  (  )  .  debug  (  "Received encrypted confirmation."  )  ; 
} 







private   void   userAuth1  (  String   localUser  ,  String   serverUser  ,  String   host  )  { 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:userAuth() authenticate user"  )  ; 
} 







private   boolean   verifyHostKey  (  String   host  ,  PublicKey   key  )  { 
Log  .  getLogInstance  (  )  .  log  (  "TODO: SSHConnection.java:verifyHostKey() verify key"  )  ; 
return   true  ; 
} 








private   byte  [  ]  computeSessionId  (  byte  [  ]  cookie  ,  BigInteger   hostKeyN  ,  BigInteger   sessionKeyN  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md  .  update  (  hostKeyN  .  abs  (  )  .  toByteArray  (  )  )  ; 
md  .  update  (  sessionKeyN  .  abs  (  )  .  toByteArray  (  )  )  ; 
md  .  update  (  cookie  ,  0  ,  8  )  ; 
return   md  .  digest  (  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
e  .  printStackTrace  (  )  ; 
Log  .  getLogInstance  (  )  .  fatal  (  "MD5 not supported by security provider."  )  ; 
return   null  ; 
} 
} 







private   void   keyExchange2  (  String   host  )  throws   IOException  ,  SSHException  { 
if  (  options  .  getCiphers  (  )  ==  null  )  { 
Log  .  getLogInstance  (  )  .  log  (  "No valid ciphers for protocol version 2 given, using defaults."  )  ; 
} 
Log  .  getLogInstance  (  )  .  TODO  (  "SSHConnection.keyExhcange2() do key exchange"  )  ; 
} 
} 

