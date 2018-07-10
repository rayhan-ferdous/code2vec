package   jssh  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  security  .  *  ; 
import   de  .  mud  .  ssh  .  Cipher  ; 








public   class   ClientProtocolHandler   implements   Runnable  ,  IProtocolHandler  ,  IPacketConstants  { 

public   ClientProtocolHandler  (  Socket   socket_  ,  Options   options_  )  throws   IOException  { 
_socket  =  socket_  ; 
_instream  =  new   BufferedInputStream  (  socket_  .  getInputStream  (  )  )  ; 
_outstream  =  new   BufferedOutputStream  (  socket_  .  getOutputStream  (  )  )  ; 
_options  =  options_  ; 
} 



public   void   enqueueToRemote  (  Packet   packet_  )  { 
_clientqueue  .  enqueue  (  packet_  )  ; 
} 



public   void   enqueueToStdout  (  Packet   packet_  )  { 
_stdout  .  enqueue  (  packet_  )  ; 
} 




public   STDOUT_InputStream   getSTDOUT  (  )  { 
return   _stdout  ; 
} 




public   STDIN_OutputStream   getSTDIN  (  )  { 
if  (  _stdin  ==  null  )  _stdin  =  new   STDIN_OutputStream  (  this  )  ; 
return   _stdin  ; 
} 






public   void   exchangeIdStrings  (  )  throws   IOException  ,  SSHSetupException  { 
if  (  _phase  !=  PHASE_EXCHANGE_ID_STRINGS  )  { 
throw   new   RuntimeException  (  "SSH protocol already in packet mode"  )  ; 
} 
for  (  ;  ;  )  { 
int   b  =  _instream  .  read  (  )  ; 
if  (  b  ==  '\n'  )  { 
debug  (  "Remote version: "  +  _id_string_received  .  toString  (  )  )  ; 
debug  (  "Local version string: "  +  ID_STRING_SENT  )  ; 
_outstream  .  write  (  (  ID_STRING_SENT  +  "\n"  )  .  getBytes  (  )  )  ; 
_outstream  .  flush  (  )  ; 
if  (  _id_string_received  .  toString  (  )  .  startsWith  (  "SSH-1"  )  ==  false  )  { 
throw   new   SSHSetupException  (  "incompatible server version: "  +  _id_string_received  .  toString  (  )  )  ; 
} 
_phase  =  PHASE_RECEIVE_SERVER_KEY  ; 
return  ; 
} 
_id_string_received  .  append  (  (  char  )  b  )  ; 
} 
} 





public   void   receiveServerKey  (  )  throws   IOException  ,  SSHSetupException  ,  SSHProtocolException  { 
if  (  _phase  !=  PHASE_RECEIVE_SERVER_KEY  )  { 
throw   new   RuntimeException  (  "SSH protocol not in packet mode yet"  )  ; 
} 
_ssh_in  =  new   SSHInputStream  (  _instream  )  ; 
_ssh_out  =  new   SSHOutputStream  (  _outstream  )  ; 
debug  (  "Waiting for server public key."  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
if  (  packet  .  getPacketType  (  )  !=  SSH_SMSG_PUBLIC_KEY  )  { 
throw   new   SSHProtocolException  (  "Received packet type "  +  packet  .  getPacketType  (  )  +  ", expected SSH_SMSG_PUBLIC_KEY"  )  ; 
} 
_server_public_key_packet  =  (  SMSG_PUBLIC_KEY  )  packet  ; 
debug  (  "Received server public key ("  +  (  8  *  _server_public_key_packet  .  getServerKeyPublicModulus  (  )  .  length  )  +  " bits) and host key ("  +  (  8  *  _server_public_key_packet  .  getHostKeyPublicModulus  (  )  .  length  )  +  " bits)."  )  ; 
_phase  =  PHASE_SEND_SESSION_KEY  ; 
return  ; 
} 


















public   void   sendSessionKey  (  ITrueRandom   trueRandom_  )  throws   SSHProtocolException  ,  SSHSetupException  ,  IOException  { 
if  (  _phase  !=  PHASE_SEND_SESSION_KEY  )  { 
throw   new   RuntimeException  (  "SSH protocol not in valid state for sending session key"  )  ; 
} 
CMSG_SESSION_KEY   session_key_packet  =  new   CMSG_SESSION_KEY  (  trueRandom_  ,  _server_public_key_packet  )  ; 
_session_id  =  session_key_packet  .  getSessionID  (  )  ; 
_ssh_out  .  writePacket  (  session_key_packet  )  ; 
debug  (  "Sent encrypted session key."  )  ; 
String   cipherName  =  session_key_packet  .  getCipherName  (  )  ; 
Cipher   sendCipher  =  Cipher  .  getInstance  (  cipherName  )  ; 
if  (  sendCipher  ==  null  )  { 
throw   new   SSHSetupException  (  cipherName  +  " algorithm not supported"  )  ; 
} 
debug  (  "Encryption type: "  +  cipherName  )  ; 
sendCipher  .  setKey  (  session_key_packet  .  getSessionKey  (  )  )  ; 
_ssh_out  .  setCipher  (  sendCipher  )  ; 
Cipher   receiveCipher  =  Cipher  .  getInstance  (  cipherName  )  ; 
receiveCipher  .  setKey  (  session_key_packet  .  getSessionKey  (  )  )  ; 
_ssh_in  .  setCipher  (  receiveCipher  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
if  (  packet  .  getPacketType  (  )  !=  SSH_SMSG_SUCCESS  )  { 
throw   new   SSHProtocolException  (  "Received packet type "  +  packet  .  getPacketType  (  )  +  ", expected SSH_SMSG_SUCCESS"  )  ; 
} 
debug  (  "Received encrypted confirmation."  )  ; 
_phase  =  PHASE_DECLARE_USER  ; 
} 






public   boolean   declareUser  (  String   username_  )  throws   IOException  ,  SSHProtocolException  { 
if  (  _phase  !=  PHASE_DECLARE_USER  )  { 
throw   new   RuntimeException  (  "SSH protocol in invalid state for declaring user"  )  ; 
} 
CMSG_USER   user_packet  =  new   CMSG_USER  (  username_  )  ; 
_ssh_out  .  writePacket  (  user_packet  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
if  (  packet  .  getPacketType  (  )  ==  SSH_SMSG_SUCCESS  )  { 
_phase  =  PHASE_PREPARATORY  ; 
return   false  ; 
} 
if  (  packet  .  getPacketType  (  )  !=  SSH_SMSG_FAILURE  )  { 
throw   new   SSHProtocolException  (  "Received packet type "  +  packet  .  getPacketType  (  )  +  ", expected SSH_SMSG_SUCCESS or SSH_SMSG_FAILURE"  )  ; 
} 
_phase  =  PHASE_AUTHENTICATE  ; 
return   true  ; 
} 











public   boolean   authenticateUser  (  String   user_  ,  String   password_  )  throws   IOException  ,  SSHProtocolException  { 
if  (  _phase  !=  PHASE_AUTHENTICATE  )  { 
throw   new   RuntimeException  (  "SSH protocol in invalid state for user authentication"  )  ; 
} 
debug  (  "Doing password authentication."  )  ; 
CMSG_AUTH_PASSWORD   password_packet  =  new   CMSG_AUTH_PASSWORD  (  password_  )  ; 
_ssh_out  .  writePacket  (  password_packet  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
if  (  packet  .  getPacketType  (  )  !=  SSH_SMSG_SUCCESS  )  { 
debug  (  "User authentication failure"  )  ; 
return   false  ; 
} 
_phase  =  PHASE_PREPARATORY  ; 
return   true  ; 
} 










public   boolean   authenticateUser  (  RSAPrivateKey   privateKey_  )  throws   IOException  ,  SSHProtocolException  ,  SSHAuthFailedException  { 
if  (  _phase  !=  PHASE_AUTHENTICATE  )  { 
throw   new   RuntimeException  (  "SSH protocol in invalid state for user authentication"  )  ; 
} 
debug  (  "Trying RSA authentication with key '"  +  privateKey_  .  getComment  (  )  +  "'"  )  ; 
byte  [  ]  modulus  =  privateKey_  .  getModulus  (  )  ; 
CMSG_AUTH_RSA   auth_rsa  =  new   CMSG_AUTH_RSA  (  modulus  )  ; 
_ssh_out  .  writePacket  (  auth_rsa  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
int   packet_type  =  packet  .  getPacketType  (  )  ; 
if  (  packet_type  ==  SSH_SMSG_FAILURE  )  { 
debug  (  "Server refused our key"  )  ; 
return   false  ; 
}  else   if  (  packet_type  !=  SSH_SMSG_AUTH_RSA_CHALLENGE  )  { 
throw   new   SSHProtocolException  (  "Received packet type "  +  packet_type  +  ", expected SSH_SMSG_AUTH_RSA_CHALLENGE or SSH_SMSG_FAILURE"  )  ; 
} 
debug  (  "Received RSA challenge from server."  )  ; 
SMSG_AUTH_RSA_CHALLENGE   challenge_packet  =  (  SMSG_AUTH_RSA_CHALLENGE  )  packet  ; 
byte  [  ]  challenge  =  challenge_packet  .  getChallenge  (  )  ; 
byte  [  ]  serverRandomBytes  =  RSAAlgorithm  .  encrypt  (  challenge  ,  privateKey_  .  getExponent  (  )  ,  privateKey_  .  getModulus  (  )  )  ; 
serverRandomBytes  =  RSAAlgorithm  .  stripPKCSPadding  (  serverRandomBytes  )  ; 
byte  [  ]  toBeHashed  =  SSHMisc  .  concatenate  (  serverRandomBytes  ,  _session_id  )  ; 
byte  [  ]  response_bytes  ; 
try  { 
MessageDigest   md5  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
response_bytes  =  md5  .  digest  (  toBeHashed  )  ; 
}  catch  (  NoSuchAlgorithmException   e  )  { 
throw   new   SSHProtocolException  (  "MD5 algorithm not supported"  )  ; 
} 
CMSG_AUTH_RSA_RESPONSE   response_packet  =  new   CMSG_AUTH_RSA_RESPONSE  (  response_bytes  )  ; 
_ssh_out  .  writePacket  (  response_packet  )  ; 
packet  =  readNextPacket  (  )  ; 
packet_type  =  packet  .  getPacketType  (  )  ; 
if  (  packet_type  ==  SSH_SMSG_FAILURE  )  { 
debug  (  "RSA authentication failed"  )  ; 
return   false  ; 
}  else   if  (  packet_type  !=  SSH_SMSG_SUCCESS  )  { 
throw   new   SSHProtocolException  (  "Received packet type "  +  packet_type  +  ", expected SSH_MSG_SUCCESS or SSH_SMSG_FAILURE"  )  ; 
} 
_phase  =  PHASE_PREPARATORY  ; 
return   true  ; 
} 





public   void   preparatoryOperations  (  )  throws   IOException  ,  SSHProtocolException  ,  SSHSetupException  { 
if  (  _phase  !=  PHASE_PREPARATORY  )  { 
throw   new   RuntimeException  (  "Protocol not in preparatory phase"  )  ; 
} 
byte  [  ]  terminalModes  =  {  CMSG_REQUEST_PTY  .  ICRNL  ,  1  ,  CMSG_REQUEST_PTY  .  ISIG  ,  1  ,  CMSG_REQUEST_PTY  .  ICANON  ,  1  ,  CMSG_REQUEST_PTY  .  ECHO  ,  1  ,  CMSG_REQUEST_PTY  .  ECHOE  ,  1  ,  CMSG_REQUEST_PTY  .  ECHOK  ,  1  ,  CMSG_REQUEST_PTY  .  ECHONL  ,  1  ,  CMSG_REQUEST_PTY  .  OPOST  ,  1  ,  CMSG_REQUEST_PTY  .  ONLCR  ,  1  ,  CMSG_REQUEST_PTY  .  CS8  ,  1  ,  CMSG_REQUEST_PTY  .  TTY_OP_END  }  ; 
int   columns  =  _options  .  getTerminalSize  (  )  .  width  ; 
int   rows  =  _options  .  getTerminalSize  (  )  .  height  ; 
CMSG_REQUEST_PTY   pty_packet  =  new   CMSG_REQUEST_PTY  (  _options  .  getTerminalType  (  )  ,  rows  ,  columns  ,  terminalModes  )  ; 
debug  (  "Requesting pty."  )  ; 
_ssh_out  .  writePacket  (  pty_packet  )  ; 
Packet   packet  =  readNextPacket  (  )  ; 
if  (  packet  .  getPacketType  (  )  !=  SSH_SMSG_SUCCESS  )  { 
throw   new   SSHProtocolException  (  "server refused to allocate pseudo-tty"  )  ; 
} 
Iterator   iter  =  _options  .  getLocalForwardings  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
PortForwarding   pf  =  (  PortForwarding  )  iter  .  next  (  )  ; 
Channel   channel  =  null  ; 
try  { 
channel  =  new   Channel  (  this  ,  pf  .  getListenPort  (  )  ,  pf  .  getHostname  (  )  ,  pf  .  getHostPort  (  )  )  ; 
}  catch  (  java  .  net  .  BindException   e  )  { 
throw   new   java  .  net  .  BindException  (  "Cannot bind to port "  +  pf  .  getListenPort  (  )  +  "; "  +  e  .  getMessage  (  )  )  ; 
} 
channel  .  setDaemon  (  true  )  ; 
channel  .  start  (  )  ; 
} 
iter  =  _options  .  getRemoteForwardings  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
PortForwarding   pf  =  (  PortForwarding  )  iter  .  next  (  )  ; 
debug  (  "Requesting forwarding of remote port "  +  pf  .  getListenPort  (  )  +  " to "  +  pf  .  getHostname  (  )  +  ":"  +  pf  .  getHostPort  (  )  )  ; 
CMSG_PORT_FORWARD_REQUEST   forward_request  =  new   CMSG_PORT_FORWARD_REQUEST  (  pf  .  getListenPort  (  )  ,  pf  .  getHostname  (  )  ,  pf  .  getHostPort  (  )  )  ; 
_ssh_out  .  writePacket  (  forward_request  )  ; 
if  (  readNextPacket  (  )  .  getPacketType  (  )  !=  SSH_SMSG_SUCCESS  )  { 
throw   new   SSHSetupException  (  "Server refused to forward port "  +  "(listen-port="  +  pf  .  getListenPort  (  )  +  " hostname="  +  pf  .  getHostname  (  )  +  " host-port="  +  pf  .  getHostPort  (  )  +  ")"  )  ; 
} 
} 
if  (  _options  .  compressionEnabled  (  )  )  { 
debug  (  "Requesting compression."  )  ; 
CMSG_REQUEST_COMPRESSION   compression_request  =  new   CMSG_REQUEST_COMPRESSION  (  6  )  ; 
_ssh_out  .  writePacket  (  compression_request  )  ; 
if  (  readNextPacket  (  )  .  getPacketType  (  )  ==  SSH_SMSG_SUCCESS  )  { 
_ssh_out  .  setCompression  (  6  )  ; 
_ssh_in  .  setCompression  (  )  ; 
} 
} 
_phase  =  PHASE_READY  ; 
return  ; 
} 





public   void   execShell  (  )  throws   IOException  ,  SSHProtocolException  { 
if  (  _phase  !=  PHASE_READY  )  { 
throw   new   RuntimeException  (  "Protocol not in correct state"  )  ; 
} 
debug  (  "Requesting shell."  )  ; 
CMSG_EXEC_SHELL   shell_packet  =  new   CMSG_EXEC_SHELL  (  )  ; 
_ssh_out  .  writePacket  (  shell_packet  )  ; 
Thread   sendThread  =  new   Thread  (  this  )  ; 
sendThread  .  setDaemon  (  true  )  ; 
sendThread  .  start  (  )  ; 
int   timeout  =  _options  .  getKeepaliveTimeout  (  )  ; 
if  (  timeout  !=  0  )  { 
_keepaliveThread  =  new   KeepaliveThread  (  timeout  )  ; 
_keepaliveThread  .  setDaemon  (  true  )  ; 
_keepaliveThread  .  start  (  )  ; 
} 
debug  (  "Entering interactive session."  )  ; 
for  (  ;  ;  )  { 
Packet   packet  =  null  ; 
try  { 
packet  =  readNextPacket  (  )  ; 
}  catch  (  EOFException   e  )  { 
break  ; 
} 
if  (  packet   instanceof   IInteractivePacket  ==  false  )  { 
throw   new   SSHProtocolException  (  "packet type "  +  packet  .  getPacketType  (  )  +  " received in interactive mode"  )  ; 
} 
(  (  IInteractivePacket  )  packet  )  .  processPacket  (  this  )  ; 
if  (  packet  .  getPacketType  (  )  ==  SSH_SMSG_EXITSTATUS  )  { 
SMSG_EXITSTATUS   status_packet  =  (  SMSG_EXITSTATUS  )  packet  ; 
debug  (  "Exit status "  +  status_packet  .  getExitStatus  (  )  )  ; 
} 
} 
return  ; 
} 











public   void   execCmd  (  String   command_  )  throws   IOException  ,  SSHProtocolException  { 
if  (  _phase  !=  PHASE_READY  )  { 
throw   new   RuntimeException  (  "Protocol not in correct state"  )  ; 
} 
debug  (  "Sending command: "  +  command_  )  ; 
CMSG_EXEC_CMD   cmd_packet  =  new   CMSG_EXEC_CMD  (  command_  )  ; 
_ssh_out  .  writePacket  (  cmd_packet  )  ; 
debug  (  "Entering interactive session."  )  ; 
for  (  ;  ;  )  { 
Packet   packet  =  null  ; 
try  { 
packet  =  readNextPacket  (  )  ; 
}  catch  (  EOFException   e  )  { 
break  ; 
}  catch  (  SocketException   e  )  { 
break  ; 
} 
if  (  packet   instanceof   IInteractivePacket  ==  false  )  { 
throw   new   SSHProtocolException  (  "packet type "  +  packet  .  getPacketType  (  )  +  " received in interactive mode"  )  ; 
} 
(  (  IInteractivePacket  )  packet  )  .  processPacket  (  this  )  ; 
if  (  packet  .  getPacketType  (  )  ==  SSH_SMSG_EXITSTATUS  )  { 
SMSG_EXITSTATUS   status_packet  =  (  SMSG_EXITSTATUS  )  packet  ; 
debug  (  "Exit status "  +  status_packet  .  getExitStatus  (  )  )  ; 
_ssh_out  .  writePacket  (  new   CMSG_EXIT_CONFIRMATION  (  )  )  ; 
} 
} 
} 





public   void   run  (  )  { 
for  (  ;  ;  )  { 
Packet   packet  =  _clientqueue  .  getNextPacket  (  )  ; 
try  { 
_ssh_out  .  writePacket  (  packet  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  0  )  ; 
} 
} 
} 



public   void   registerOpenChannel  (  OpenChannel   channel_  )  { 
_channelManager  .  registerOpenChannel  (  channel_  )  ; 
} 



public   void   removeOpenChannel  (  OpenChannel   channel_  )  { 
_channelManager  .  removeOpenChannel  (  channel_  )  ; 
} 



public   OpenChannel   findOpenChannel  (  int   channel_number_  )  { 
return   _channelManager  .  findOpenChannel  (  channel_number_  )  ; 
} 





public   boolean   isPortOpenAllowed  (  String   hostname_  ,  int   port_  )  { 
return   _options  .  isPortOpenAllowed  (  hostname_  ,  port_  )  ; 
} 




public   SMSG_PUBLIC_KEY   getServerKeyPacket  (  )  { 
return   _server_public_key_packet  ; 
} 




private   Packet   readNextPacket  (  )  throws   IOException  ,  SSHProtocolException  { 
Packet   packet  =  null  ; 
while  (  true  )  { 
packet  =  _ssh_in  .  readPacket  (  )  ; 
if  (  _keepaliveThread  !=  null  )  { 
_keepaliveThread  .  interrupt  (  )  ; 
} 
int   packet_type  =  packet  .  getPacketType  (  )  ; 
if  (  packet_type  ==  SSH_MSG_DEBUG  )  { 
debug  (  (  (  MSG_DEBUG  )  packet  )  .  getMessage  (  )  )  ; 
}  else   if  (  packet_type  !=  SSH_MSG_IGNORE  )  break  ; 
} 
return   packet  ; 
} 



public   void   debug  (  String   string_  )  { 
if  (  _options  .  getDebug  (  )  )  enqueueToStdout  (  new   SMSG_STDERR_DATA  (  "debug: "  +  string_  +  "\r\n"  )  )  ; 
} 




private   class   KeepaliveThread   extends   Thread  { 

private   int   _seconds  ; 

public   KeepaliveThread  (  int   seconds_  )  { 
_seconds  =  seconds_  ; 
} 

public   void   run  (  )  { 
for  (  ;  ;  )  { 
try  { 
Thread  .  sleep  (  _seconds  *  1000L  )  ; 
}  catch  (  InterruptedException   e  )  { 
continue  ; 
} 
_clientqueue  .  enqueue  (  new   MSG_IGNORE  (  )  )  ; 
} 
} 
} 



private   PacketQueue   _clientqueue  =  new   PacketQueue  (  )  ; 



private   Socket   _socket  ; 



private   Options   _options  ; 



private   BufferedInputStream   _instream  ; 



private   BufferedOutputStream   _outstream  ; 




private   SMSG_PUBLIC_KEY   _server_public_key_packet  ; 




private   byte  [  ]  _session_id  ; 

private   SSHInputStream   _ssh_in  ; 

private   SSHOutputStream   _ssh_out  ; 

private   STDOUT_InputStream   _stdout  =  new   STDOUT_InputStream  (  )  ; 

private   STDIN_OutputStream   _stdin  =  null  ; 

private   StringBuffer   _id_string_received  =  new   StringBuffer  (  )  ; 

private   static   String   ID_STRING_SENT  =  "SSH-1.5-JSSH 0.9.5 (2002/10/05)"  ; 

private   ChannelManager   _channelManager  =  new   ChannelManager  (  )  ; 

private   KeepaliveThread   _keepaliveThread  =  null  ; 

private   int   _phase  =  PHASE_EXCHANGE_ID_STRINGS  ; 

private   static   final   int   PHASE_EXCHANGE_ID_STRINGS  =  1  ; 

private   static   final   int   PHASE_RECEIVE_SERVER_KEY  =  2  ; 

private   static   final   int   PHASE_SEND_SESSION_KEY  =  3  ; 

private   static   final   int   PHASE_DECLARE_USER  =  4  ; 

private   static   final   int   PHASE_AUTHENTICATE  =  5  ; 

private   static   final   int   PHASE_PREPARATORY  =  6  ; 

private   static   final   int   PHASE_READY  =  7  ; 
} 

