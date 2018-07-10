package   com  .  pallas  .  unicore  .  security  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  net  .  URL  ; 
import   java  .  security  .  InvalidKeyException  ; 
import   java  .  security  .  KeyPair  ; 
import   java  .  security  .  KeyPairGenerator  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  Principal  ; 
import   java  .  security  .  PrivateKey  ; 
import   java  .  security  .  PublicKey  ; 
import   java  .  security  .  SecureRandom  ; 
import   java  .  security  .  SignatureException  ; 
import   java  .  security  .  cert  .  CRLException  ; 
import   java  .  security  .  cert  .  Certificate  ; 
import   java  .  security  .  cert  .  CertificateEncodingException  ; 
import   java  .  security  .  cert  .  CertificateException  ; 
import   java  .  security  .  cert  .  CertificateFactory  ; 
import   java  .  security  .  cert  .  X509CRL  ; 
import   java  .  security  .  cert  .  X509CRLEntry  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashSet  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   org  .  bouncycastle  .  asn1  .  DERInputStream  ; 
import   org  .  bouncycastle  .  asn1  .  DERObjectIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  DEROctetString  ; 
import   org  .  bouncycastle  .  asn1  .  DERSequence  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  AuthorityKeyIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  BasicConstraints  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  SubjectKeyIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  SubjectPublicKeyInfo  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  X509Extensions  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  X509Name  ; 
import   org  .  bouncycastle  .  jce  .  X509V3CertificateGenerator  ; 
import   org  .  bouncycastle  .  util  .  encoders  .  Base64  ; 
import   com  .  pallas  .  unicore  .  connection  .  URLReader  ; 
import   com  .  pallas  .  unicore  .  utility  .  Hex  ; 
import   com  .  pallas  .  unicore  .  utility  .  UserMessages  ; 






public   class   CertificateUtility  { 

private   static   final   String   CRL_DISTRIBUTION_POINT_OID  =  "2.5.29.31"  ; 

private   static   Logger   logger  =  Logger  .  getLogger  (  "com.pallas.unicore.security"  )  ; 

static   ResourceBundle   res  =  ResourceBundle  .  getBundle  (  "com.pallas.unicore.security.ResourceStrings"  )  ; 

private   static   String   checkForCRLExtensions  (  X509Certificate  [  ]  chain  )  { 
for  (  int   i  =  0  ;  i  <  chain  .  length  ;  i  ++  )  { 
System  .  err  .  println  (  chain  [  i  ]  )  ; 
byte  [  ]  extension  =  chain  [  i  ]  .  getExtensionValue  (  CRL_DISTRIBUTION_POINT_OID  )  ; 
if  (  extension  !=  null  )  { 
String   crlPoint  =  new   String  (  extension  )  ; 
int   index  =  crlPoint  .  indexOf  (  "http"  )  ; 
if  (  index  <  0  )  { 
index  =  crlPoint  .  indexOf  (  "HTTP"  )  ; 
if  (  index  >=  0  )  { 
return   crlPoint  .  substring  (  index  )  ; 
} 
}  else  { 
return   crlPoint  .  substring  (  index  )  ; 
} 
} 
} 
return   null  ; 
} 








public   static   boolean   checkOrder  (  X509Certificate  [  ]  chain  )  { 
boolean   check  =  true  ; 
for  (  int   i  =  0  ;  i  <  chain  .  length  -  1  ;  i  ++  )  { 
if  (  !  chain  [  i  ]  .  getIssuerDN  (  )  .  equals  (  chain  [  i  +  1  ]  .  getSubjectDN  (  )  )  )  { 
check  =  false  ; 
UserMessages  .  error  (  "Broken certificate chain at index "  +  i  )  ; 
} 
} 
return   check  ; 
} 





public   static   boolean   checkRevokedCertificate  (  Certificate  [  ]  userCert  )  throws   Exception  { 
return   checkRevokedCertificate  (  toX509  (  userCert  )  )  ; 
} 





public   static   boolean   checkRevokedCertificate  (  X509Certificate  [  ]  userCert  )  throws   Exception  { 
String   crlPoint  =  checkForCRLExtensions  (  userCert  )  ; 
if  (  crlPoint  ==  null  )  { 
throw   new   Exception  (  "CRL cannot be read.\n"  +  "No X509V3 extension in certificate"  )  ; 
} 
System  .  err  .  println  (  "Reading CRL from: "  +  crlPoint  )  ; 
URLReader   urlReader  =  new   URLReader  (  )  ; 
URL   url  =  new   URL  (  crlPoint  )  ; 
byte  [  ]  crlBytes  =  urlReader  .  readURL  (  url  )  .  getBytes  (  )  ; 
ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  crlBytes  )  ; 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
X509CRL   crl  =  (  X509CRL  )  cf  .  generateCRL  (  bais  )  ; 
System  .  out  .  println  (  "CRL issued by: "  +  crl  .  getIssuerDN  (  )  .  getName  (  )  +  " valid till: "  +  crl  .  getNextUpdate  (  )  )  ; 
Object  [  ]  crlSet  =  crl  .  getRevokedCertificates  (  )  .  toArray  (  )  ; 
for  (  int   i  =  0  ;  i  <  crlSet  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  "Revoked: "  +  (  X509CRLEntry  )  crlSet  [  i  ]  )  ; 
} 
Date   now  =  new   Date  (  )  ; 
if  (  now  .  after  (  crl  .  getNextUpdate  (  )  )  )  { 
throw   new   Exception  (  res  .  getString  (  "CRL_OUTDATED"  )  )  ; 
} 
Principal   crlIssuer  =  crl  .  getIssuerDN  (  )  ; 
X509Certificate   user  =  null  ; 
for  (  int   i  =  0  ;  i  <  userCert  .  length  ;  i  ++  )  { 
if  (  !  userCert  [  i  ]  .  getIssuerDN  (  )  .  equals  (  userCert  [  i  ]  .  getSubjectDN  (  )  )  )  { 
user  =  userCert  [  i  ]  ; 
break  ; 
} 
} 
if  (  user  ==  null  ||  !  user  .  getIssuerDN  (  )  .  equals  (  crlIssuer  )  )  { 
throw   new   Exception  (  "CRL does not belong to the signer of the certificate.\n"  +  "Select a valid URL in the user defaults."  )  ; 
} 
return   crl  .  isRevoked  (  user  )  ; 
} 








public   static   byte  [  ]  generateMD5Fingerprint  (  byte  [  ]  ba  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
return   md  .  digest  (  ba  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
System  .  err  .  println  (  "MD5 algorithm not supported"  +  nsae  .  getLocalizedMessage  (  )  )  ; 
} 
return   null  ; 
} 










public   static   KeyPair   generateNewKeys  (  String   algorithm  ,  int   length  )  throws   Exception  { 
KeyPairGenerator   kpg  =  KeyPairGenerator  .  getInstance  (  algorithm  )  ; 
kpg  .  initialize  (  length  )  ; 
return   kpg  .  generateKeyPair  (  )  ; 
} 











public   static   KeyPair   generateNewKeys  (  String   algorithm  ,  String   provider  ,  int   length  )  throws   Exception  { 
KeyPairGenerator   kpg  =  KeyPairGenerator  .  getInstance  (  algorithm  ,  provider  )  ; 
kpg  .  initialize  (  length  )  ; 
return   kpg  .  generateKeyPair  (  )  ; 
} 








public   static   byte  [  ]  generateSHA1Fingerprint  (  byte  [  ]  ba  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "SHA1"  )  ; 
return   md  .  digest  (  ba  )  ; 
}  catch  (  NoSuchAlgorithmException   nsae  )  { 
System  .  err  .  println  (  "SHA1 algorithm not supported"  +  nsae  .  getLocalizedMessage  (  )  )  ; 
} 
return   null  ; 
} 




public   static   X509Certificate   genSelfCert  (  String   dn  ,  long   validity  ,  PrivateKey   privKey  ,  PublicKey   pubKey  ,  boolean   isCA  )  throws   NoSuchAlgorithmException  ,  SignatureException  ,  InvalidKeyException  { 
String   sigAlg  =  "SHA1WithRSA"  ; 
Date   firstDate  =  new   Date  (  )  ; 
firstDate  .  setTime  (  firstDate  .  getTime  (  )  -  10  *  60  *  1000  )  ; 
Date   lastDate  =  new   Date  (  )  ; 
lastDate  .  setTime  (  lastDate  .  getTime  (  )  +  (  validity  *  (  24  *  60  *  60  *  1000  )  )  )  ; 
X509V3CertificateGenerator   certgen  =  new   X509V3CertificateGenerator  (  )  ; 
byte  [  ]  serno  =  new   byte  [  8  ]  ; 
SecureRandom   random  =  SecureRandom  .  getInstance  (  "SHA1PRNG"  )  ; 
random  .  setSeed  (  (  long  )  (  new   Date  (  )  .  getTime  (  )  )  )  ; 
random  .  nextBytes  (  serno  )  ; 
certgen  .  setSerialNumber  (  (  new   java  .  math  .  BigInteger  (  serno  )  )  .  abs  (  )  )  ; 
certgen  .  setNotBefore  (  firstDate  )  ; 
certgen  .  setNotAfter  (  lastDate  )  ; 
certgen  .  setSignatureAlgorithm  (  sigAlg  )  ; 
certgen  .  setSubjectDN  (  CertificateUtility  .  stringToBcX509Name  (  dn  )  )  ; 
certgen  .  setIssuerDN  (  CertificateUtility  .  stringToBcX509Name  (  dn  )  )  ; 
certgen  .  setPublicKey  (  pubKey  )  ; 
BasicConstraints   bc  =  new   BasicConstraints  (  isCA  )  ; 
certgen  .  addExtension  (  X509Extensions  .  BasicConstraints  .  getId  (  )  ,  true  ,  bc  )  ; 
try  { 
if  (  isCA  ==  true  )  { 
SubjectPublicKeyInfo   spki  =  new   SubjectPublicKeyInfo  (  (  DERSequence  )  new   DERInputStream  (  new   ByteArrayInputStream  (  pubKey  .  getEncoded  (  )  )  )  .  readObject  (  )  )  ; 
SubjectKeyIdentifier   ski  =  new   SubjectKeyIdentifier  (  spki  )  ; 
SubjectPublicKeyInfo   apki  =  new   SubjectPublicKeyInfo  (  (  DERSequence  )  new   DERInputStream  (  new   ByteArrayInputStream  (  pubKey  .  getEncoded  (  )  )  )  .  readObject  (  )  )  ; 
AuthorityKeyIdentifier   aki  =  new   AuthorityKeyIdentifier  (  apki  )  ; 
certgen  .  addExtension  (  X509Extensions  .  SubjectKeyIdentifier  .  getId  (  )  ,  false  ,  ski  )  ; 
certgen  .  addExtension  (  X509Extensions  .  AuthorityKeyIdentifier  .  getId  (  )  ,  false  ,  aki  )  ; 
} 
}  catch  (  IOException   e  )  { 
} 
X509Certificate   selfcert  =  certgen  .  generateX509Certificate  (  privKey  )  ; 
return   selfcert  ; 
} 





public   static   byte  [  ]  getAuthorityKeyId  (  X509Certificate   cert  )  throws   IOException  { 
byte  [  ]  extvalue  =  cert  .  getExtensionValue  (  "2.5.29.35"  )  ; 
if  (  extvalue  ==  null  )  { 
return   null  ; 
} 
DEROctetString   oct  =  (  DEROctetString  )  (  new   DERInputStream  (  new   ByteArrayInputStream  (  extvalue  )  )  .  readObject  (  )  )  ; 
AuthorityKeyIdentifier   keyId  =  new   AuthorityKeyIdentifier  (  (  DERSequence  )  new   DERInputStream  (  new   ByteArrayInputStream  (  oct  .  getOctets  (  )  )  )  .  readObject  (  )  )  ; 
return   keyId  .  getKeyIdentifier  (  )  ; 
} 







public   static   String   getCertFingerprintAsString  (  byte  [  ]  ba  )  { 
try  { 
X509Certificate   cert  =  getCertfromByteArray  (  ba  )  ; 
byte  [  ]  res  =  generateSHA1Fingerprint  (  cert  .  getEncoded  (  )  )  ; 
return   Hex  .  encode  (  res  )  ; 
}  catch  (  CertificateEncodingException   cee  )  { 
System  .  err  .  println  (  "Error encoding X509 certificate."  +  cee  .  getLocalizedMessage  (  )  )  ; 
}  catch  (  CertificateException   cee  )  { 
System  .  err  .  println  (  "Error decoding X509 certificate."  +  cee  .  getLocalizedMessage  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
System  .  err  .  println  (  "Error reading byte array for X509 certificate."  +  ioe  .  getLocalizedMessage  (  )  )  ; 
} 
return   null  ; 
} 











public   static   X509Certificate   getCertfromByteArray  (  byte  [  ]  cert  )  throws   IOException  ,  CertificateException  { 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
X509Certificate   x509cert  =  (  X509Certificate  )  cf  .  generateCertificate  (  new   ByteArrayInputStream  (  cert  )  )  ; 
return   x509cert  ; 
} 











public   static   X509Certificate   getCertfromPEM  (  InputStream   certstream  )  throws   IOException  ,  CertificateException  { 
String   beginKey  =  "-----BEGIN CERTIFICATE-----"  ; 
String   endKey  =  "-----END CERTIFICATE-----"  ; 
BufferedReader   bufRdr  =  new   BufferedReader  (  new   InputStreamReader  (  certstream  )  )  ; 
ByteArrayOutputStream   ostr  =  new   ByteArrayOutputStream  (  )  ; 
PrintStream   opstr  =  new   PrintStream  (  ostr  )  ; 
String   temp  ; 
while  (  (  temp  =  bufRdr  .  readLine  (  )  )  !=  null  &&  !  temp  .  equals  (  beginKey  )  )  { 
continue  ; 
} 
if  (  temp  ==  null  )  { 
throw   new   IOException  (  "Error in "  +  certstream  .  toString  (  )  +  ", missing "  +  beginKey  +  " boundary"  )  ; 
} 
while  (  (  temp  =  bufRdr  .  readLine  (  )  )  !=  null  &&  !  temp  .  equals  (  endKey  )  )  { 
opstr  .  print  (  temp  )  ; 
} 
if  (  temp  ==  null  )  { 
throw   new   IOException  (  "Error in "  +  certstream  .  toString  (  )  +  ", missing "  +  endKey  +  " boundary"  )  ; 
} 
opstr  .  close  (  )  ; 
byte  [  ]  certbuf  =  Base64  .  decode  (  ostr  .  toByteArray  (  )  )  ; 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
X509Certificate   x509cert  =  (  X509Certificate  )  cf  .  generateCertificate  (  new   ByteArrayInputStream  (  certbuf  )  )  ; 
return   x509cert  ; 
} 












public   static   X509Certificate   getCertfromPEM  (  String   certFile  )  throws   IOException  ,  CertificateException  { 
InputStream   inStrm  =  new   FileInputStream  (  certFile  )  ; 
X509Certificate   cert  =  getCertfromPEM  (  inStrm  )  ; 
return   cert  ; 
} 







public   static   String   getCommonName  (  X509Certificate   x509  )  { 
if  (  x509  ==  null  )  { 
return   null  ; 
} 
String   principal  =  x509  .  getSubjectDN  (  )  .  toString  (  )  ; 
int   index1  =  0  ; 
int   index2  =  0  ; 
index1  =  principal  .  indexOf  (  "CN="  )  ; 
index2  =  principal  .  indexOf  (  ","  ,  index1  )  ; 
String   cn  =  null  ; 
if  (  index2  >  0  )  { 
cn  =  principal  .  substring  (  index1  +  3  ,  index2  )  ; 
}  else  { 
cn  =  principal  .  substring  (  index1  +  3  ,  principal  .  length  (  )  -  1  )  ; 
} 
return   cn  ; 
} 












public   static   X509CRL   getCRLfromByteArray  (  byte  [  ]  crl  )  throws   IOException  ,  CertificateException  ,  CRLException  { 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
X509CRL   x509crl  =  (  X509CRL  )  cf  .  generateCRL  (  new   ByteArrayInputStream  (  crl  )  )  ; 
return   x509crl  ; 
} 







public   static   String   getFingerprintAsString  (  X509Certificate   cert  )  { 
try  { 
byte  [  ]  res  =  generateSHA1Fingerprint  (  cert  .  getEncoded  (  )  )  ; 
return   Hex  .  encode  (  res  )  ; 
}  catch  (  CertificateEncodingException   cee  )  { 
System  .  err  .  println  (  "Error encoding X509 certificate."  +  cee  .  getLocalizedMessage  (  )  )  ; 
} 
return   null  ; 
} 







public   static   String   getFingerprintAsString  (  X509CRL   crl  )  { 
try  { 
byte  [  ]  res  =  generateSHA1Fingerprint  (  crl  .  getEncoded  (  )  )  ; 
return   Hex  .  encode  (  res  )  ; 
}  catch  (  CRLException   ce  )  { 
System  .  err  .  println  (  "Error encoding X509 CRL."  +  ce  .  getLocalizedMessage  (  )  )  ; 
} 
return   null  ; 
} 










public   static   String   getPartFromDN  (  String   dn  ,  String   dnpart  )  { 
String   trimmeddn  =  dn  .  trim  (  )  ; 
String   part  =  null  ; 
String   o  =  null  ; 
StringTokenizer   st  =  new   StringTokenizer  (  trimmeddn  ,  ",="  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
o  =  st  .  nextToken  (  )  ; 
if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  dnpart  )  )  { 
part  =  st  .  nextToken  (  )  ; 
} 
} 
return   part  ; 
} 










public   static   Vector   getPartsFromDN  (  String   dn  ,  String   dnpart  )  { 
String   trimmeddn  =  dn  .  trim  (  )  ; 
Vector   part  =  new   Vector  (  )  ; 
String   o  =  null  ; 
StringTokenizer   st  =  new   StringTokenizer  (  trimmeddn  ,  ",="  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
o  =  st  .  nextToken  (  )  ; 
if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  dnpart  )  )  { 
part  .  addElement  (  st  .  nextToken  (  )  )  ; 
} 
} 
return   part  ; 
} 





public   static   byte  [  ]  getSubjectKeyId  (  X509Certificate   cert  )  throws   IOException  { 
byte  [  ]  extvalue  =  cert  .  getExtensionValue  (  "2.5.29.14"  )  ; 
if  (  extvalue  ==  null  )  { 
return   null  ; 
} 
DEROctetString   oct  =  (  DEROctetString  )  (  new   DERInputStream  (  new   ByteArrayInputStream  (  extvalue  )  )  .  readObject  (  )  )  ; 
SubjectKeyIdentifier   keyId  =  new   SubjectKeyIdentifier  (  oct  )  ; 
return   keyId  .  getKeyIdentifier  (  )  ; 
} 






public   static   Certificate   importTrustedCertifcate  (  File   filename  )  throws   Exception  { 
DataInputStream   dis  =  new   DataInputStream  (  new   FileInputStream  (  filename  )  )  ; 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
byte  [  ]  bytes  =  new   byte  [  dis  .  available  (  )  ]  ; 
dis  .  readFully  (  bytes  )  ; 
return   importTrustedCertificate  (  bytes  )  ; 
} 






public   static   Certificate   importTrustedCertificate  (  byte  [  ]  bytes  )  throws   Exception  { 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
ByteArrayInputStream   bais  =  new   ByteArrayInputStream  (  bytes  )  ; 
Certificate   cert  =  cf  .  generateCertificate  (  bais  )  ; 
return   cert  ; 
} 









public   static   boolean   isSelfSigned  (  X509Certificate   cert  )  { 
boolean   ret  =  cert  .  getSubjectDN  (  )  .  equals  (  cert  .  getIssuerDN  (  )  )  ; 
return   ret  ; 
} 







public   static   X509Certificate  [  ]  sort  (  X509Certificate   in  [  ]  )  { 
X509Certificate   x509Cert  [  ]  =  null  ; 
if  (  in  ==  null  )  { 
return   null  ; 
}  else   if  (  in  .  length  ==  0  )  { 
x509Cert  =  new   X509Certificate  [  0  ]  ; 
return   x509Cert  ; 
} 
LinkedList   list  =  new   LinkedList  (  )  ; 
LinkedHashSet   inSet  =  new   LinkedHashSet  (  )  ; 
for  (  int   i  =  0  ;  i  <  in  .  length  ;  i  ++  )  { 
inSet  .  add  (  in  [  i  ]  )  ; 
} 
int   setSize  =  inSet  .  size  (  )  ; 
if  (  setSize  !=  in  .  length  )  { 
UserMessages  .  error  (  "Size "  +  setSize  +  " of certificate set different from size "  +  in  .  length  +  " of certificate array"  )  ; 
} 
list  .  addLast  (  in  [  0  ]  )  ; 
inSet  .  remove  (  in  [  0  ]  )  ; 
while  (  !  inSet  .  isEmpty  (  )  )  { 
int   size  =  inSet  .  size  (  )  ; 
Iterator   iter  =  inSet  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
X509Certificate   next  =  (  X509Certificate  )  iter  .  next  (  )  ; 
if  (  (  (  X509Certificate  )  list  .  getLast  (  )  )  .  getIssuerDN  (  )  .  equals  (  next  .  getSubjectDN  (  )  )  )  { 
list  .  addLast  (  next  )  ; 
inSet  .  remove  (  next  )  ; 
}  else   if  (  next  .  getIssuerDN  (  )  .  equals  (  (  (  X509Certificate  )  list  .  getFirst  (  )  )  .  getSubjectDN  (  )  )  )  { 
list  .  addFirst  (  next  )  ; 
inSet  .  remove  (  next  )  ; 
} 
} 
if  (  inSet  .  size  (  )  ==  size  )  { 
break  ; 
} 
} 
Object   obj  [  ]  =  list  .  toArray  (  )  ; 
x509Cert  =  new   X509Certificate  [  obj  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  obj  .  length  ;  i  ++  )  { 
x509Cert  [  i  ]  =  (  X509Certificate  )  obj  [  i  ]  ; 
} 
if  (  x509Cert  .  length  !=  setSize  )  { 
UserMessages  .  error  (  "Size "  +  setSize  +  " of certificate set different from size "  +  x509Cert  .  length  +  " of SORTED certificate array"  )  ; 
return   null  ; 
} 
if  (  !  checkOrder  (  x509Cert  )  )  { 
UserMessages  .  error  (  "Sorting of certificates failed"  )  ; 
return   null  ; 
} 
return   x509Cert  ; 
} 








public   static   String   stringToBCDNString  (  String   dn  )  { 
String   name  =  stringToBcX509Name  (  dn  )  .  toString  (  )  ; 
return   name  ; 
} 
















public   static   X509Name   stringToBcX509Name  (  String   dn  )  { 
String   trimmeddn  =  dn  .  trim  (  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  trimmeddn  ,  ",="  )  ; 
Hashtable   dntable  =  new   Hashtable  (  )  ; 
String   o  =  null  ; 
DERObjectIdentifier   oid  =  null  ; 
Collection   coll  =  new   ArrayList  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
o  =  st  .  nextToken  (  )  ; 
if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "C"  )  )  { 
oid  =  X509Name  .  C  ; 
coll  .  add  (  X509Name  .  C  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "DC"  )  )  { 
oid  =  X509Name  .  DC  ; 
coll  .  add  (  X509Name  .  DC  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "ST"  )  )  { 
oid  =  X509Name  .  ST  ; 
coll  .  add  (  X509Name  .  ST  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "L"  )  )  { 
oid  =  X509Name  .  L  ; 
coll  .  add  (  X509Name  .  L  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "O"  )  )  { 
oid  =  X509Name  .  O  ; 
coll  .  add  (  X509Name  .  O  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "OU"  )  )  { 
oid  =  X509Name  .  OU  ; 
coll  .  add  (  X509Name  .  OU  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "SN"  )  )  { 
oid  =  X509Name  .  SN  ; 
coll  .  add  (  X509Name  .  SN  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "CN"  )  )  { 
oid  =  X509Name  .  CN  ; 
coll  .  add  (  X509Name  .  CN  )  ; 
}  else   if  (  o  .  trim  (  )  .  equalsIgnoreCase  (  "EmailAddress"  )  )  { 
oid  =  X509Name  .  EmailAddress  ; 
coll  .  add  (  X509Name  .  EmailAddress  )  ; 
}  else  { 
oid  =  null  ; 
} 
if  (  oid  !=  null  )  { 
dntable  .  put  (  oid  ,  st  .  nextToken  (  )  )  ; 
} 
} 
Vector   order  =  new   Vector  (  )  ; 
order  .  add  (  X509Name  .  EmailAddress  )  ; 
order  .  add  (  X509Name  .  CN  )  ; 
order  .  add  (  X509Name  .  SN  )  ; 
order  .  add  (  X509Name  .  OU  )  ; 
order  .  add  (  X509Name  .  O  )  ; 
order  .  add  (  X509Name  .  L  )  ; 
order  .  add  (  X509Name  .  ST  )  ; 
order  .  add  (  X509Name  .  DC  )  ; 
order  .  add  (  X509Name  .  C  )  ; 
order  .  retainAll  (  coll  )  ; 
return   new   X509Name  (  order  ,  dntable  )  ; 
} 








public   static   Vector   tokenizeDN  (  String   dn  )  { 
String   trimmeddn  =  dn  .  trim  (  )  ; 
Vector   part  =  new   Vector  (  )  ; 
String   o  =  null  ; 
StringTokenizer   st  =  new   StringTokenizer  (  trimmeddn  ,  ",="  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
o  =  st  .  nextToken  (  )  ; 
part  .  addElement  (  st  .  nextToken  (  )  )  ; 
} 
return   part  ; 
} 







public   static   X509Certificate  [  ]  toX509  (  Certificate   in  [  ]  )  { 
X509Certificate   x509Cert  [  ]  =  null  ; 
if  (  in  !=  null  )  { 
x509Cert  =  new   X509Certificate  [  in  .  length  ]  ; 
for  (  int   j  =  0  ;  j  <  in  .  length  ;  j  ++  )  { 
if  (  in  [  j  ]  instanceof   X509Certificate  )  { 
x509Cert  [  j  ]  =  (  X509Certificate  )  in  [  j  ]  ; 
} 
} 
} 
return   x509Cert  ; 
} 







public   static   X509Certificate  [  ]  toX509Chain  (  Collection   coll  )  { 
Iterator   iter  =  coll  .  iterator  (  )  ; 
X509Certificate   unsortedCerts  [  ]  =  new   X509Certificate  [  coll  .  size  (  )  ]  ; 
int   k  =  0  ; 
while  (  iter  .  hasNext  (  )  )  { 
unsortedCerts  [  k  ]  =  (  X509Certificate  )  iter  .  next  (  )  ; 
k  ++  ; 
} 
X509Certificate   x509Cert  [  ]  =  sort  (  unsortedCerts  )  ; 
return   x509Cert  ; 
} 




private   CertificateUtility  (  )  { 
} 
} 

