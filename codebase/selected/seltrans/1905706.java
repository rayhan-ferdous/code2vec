package   com  .  itextpdf  .  text  .  pdf  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  security  .  InvalidKeyException  ; 
import   java  .  security  .  KeyStore  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  NoSuchProviderException  ; 
import   java  .  security  .  PrivateKey  ; 
import   java  .  security  .  Signature  ; 
import   java  .  security  .  SignatureException  ; 
import   java  .  security  .  cert  .  CRL  ; 
import   java  .  security  .  cert  .  CRLException  ; 
import   java  .  security  .  cert  .  Certificate  ; 
import   java  .  security  .  cert  .  CertificateException  ; 
import   java  .  security  .  cert  .  CertificateFactory  ; 
import   java  .  security  .  cert  .  CertificateParsingException  ; 
import   java  .  security  .  cert  .  X509CRL  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Calendar  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  GregorianCalendar  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Set  ; 
import   org  .  bouncycastle  .  asn1  .  *  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  Attribute  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  AttributeTable  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  ContentInfo  ; 
import   org  .  bouncycastle  .  asn1  .  ocsp  .  BasicOCSPResponse  ; 
import   org  .  bouncycastle  .  asn1  .  ocsp  .  OCSPObjectIdentifiers  ; 
import   org  .  bouncycastle  .  asn1  .  pkcs  .  PKCSObjectIdentifiers  ; 
import   org  .  bouncycastle  .  asn1  .  tsp  .  MessageImprint  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  Extension  ; 
import   org  .  bouncycastle  .  jce  .  X509Principal  ; 
import   org  .  bouncycastle  .  jce  .  provider  .  X509CertParser  ; 
import   org  .  bouncycastle  .  cert  .  ocsp  .  BasicOCSPResp  ; 
import   org  .  bouncycastle  .  cert  .  ocsp  .  CertificateID  ; 
import   org  .  bouncycastle  .  cert  .  ocsp  .  SingleResp  ; 
import   org  .  bouncycastle  .  tsp  .  TimeStampToken  ; 
import   com  .  itextpdf  .  text  .  ExceptionConverter  ; 
import   com  .  itextpdf  .  text  .  error_messages  .  MessageLocalization  ; 
import   org  .  bouncycastle  .  asn1  .  DERIA5String  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  CRLDistPoint  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  DistributionPoint  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  DistributionPointName  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralName  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralNames  ; 
import   org  .  bouncycastle  .  jce  .  provider  .  CertPathValidatorUtilities  ; 
import   org  .  bouncycastle  .  jce  .  provider  .  RFC3280CertPathUtilities  ; 
import   org  .  bouncycastle  .  tsp  .  TimeStampTokenInfo  ; 
import   org  .  bouncycastle  .  operator  .  jcajce  .  JcaContentVerifierProviderBuilder  ; 
import   org  .  bouncycastle  .  operator  .  jcajce  .  JcaContentSignerBuilder  ; 
import   org  .  bouncycastle  .  cms  .  jcajce  .  JcaSimpleSignerInfoVerifierBuilder  ; 
import   org  .  bouncycastle  .  cert  .  jcajce  .  JcaX509CertificateHolder  ; 
import   org  .  bouncycastle  .  operator  .  jcajce  .  JcaDigestCalculatorProviderBuilder  ; 







public   class   PdfPKCS7  { 

private   byte   sigAttr  [  ]  ; 

private   byte   digestAttr  [  ]  ; 

private   int   version  ,  signerversion  ; 

private   Set  <  String  >  digestalgos  ; 

private   Collection  <  Certificate  >  certs  ; 

private   Collection  <  CRL  >  crls  ; 

private   Collection  <  Certificate  >  signCerts  ; 

private   X509Certificate   signCert  ; 

private   byte  [  ]  digest  ; 

private   MessageDigest   messageDigest  ; 

private   MessageDigest   encContDigest  ; 

private   String   digestAlgorithm  ,  digestEncryptionAlgorithm  ; 

private   Signature   sig  ; 

private   transient   PrivateKey   privKey  ; 

private   byte   RSAdata  [  ]  ; 

private   boolean   verified  ; 

private   boolean   verifyResult  ; 

private   byte   externalDigest  [  ]  ; 

private   byte   externalRSAdata  [  ]  ; 

private   String   provider  ; 

private   boolean   isTsp  ; 

private   static   final   String   ID_PKCS7_DATA  =  "1.2.840.113549.1.7.1"  ; 

private   static   final   String   ID_PKCS7_SIGNED_DATA  =  "1.2.840.113549.1.7.2"  ; 

private   static   final   String   ID_RSA  =  "1.2.840.113549.1.1.1"  ; 

private   static   final   String   ID_DSA  =  "1.2.840.10040.4.1"  ; 

private   static   final   String   ID_CONTENT_TYPE  =  "1.2.840.113549.1.9.3"  ; 

private   static   final   String   ID_MESSAGE_DIGEST  =  "1.2.840.113549.1.9.4"  ; 

private   static   final   String   ID_SIGNING_TIME  =  "1.2.840.113549.1.9.5"  ; 

private   static   final   String   ID_ADBE_REVOCATION  =  "1.2.840.113583.1.1.8"  ; 




private   String   reason  ; 




private   String   location  ; 




private   Calendar   signDate  ; 




private   String   signName  ; 

private   TimeStampToken   timeStampToken  ; 

private   static   final   HashMap  <  String  ,  String  >  digestNames  =  new   HashMap  <  String  ,  String  >  (  )  ; 

private   static   final   HashMap  <  String  ,  String  >  algorithmNames  =  new   HashMap  <  String  ,  String  >  (  )  ; 

private   static   final   HashMap  <  String  ,  String  >  allowedDigests  =  new   HashMap  <  String  ,  String  >  (  )  ; 

static  { 
digestNames  .  put  (  "1.2.840.113549.2.5"  ,  "MD5"  )  ; 
digestNames  .  put  (  "1.2.840.113549.2.2"  ,  "MD2"  )  ; 
digestNames  .  put  (  "1.3.14.3.2.26"  ,  "SHA1"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.2.4"  ,  "SHA224"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.2.1"  ,  "SHA256"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.2.2"  ,  "SHA384"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.2.3"  ,  "SHA512"  )  ; 
digestNames  .  put  (  "1.3.36.3.2.2"  ,  "RIPEMD128"  )  ; 
digestNames  .  put  (  "1.3.36.3.2.1"  ,  "RIPEMD160"  )  ; 
digestNames  .  put  (  "1.3.36.3.2.3"  ,  "RIPEMD256"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.4"  ,  "MD5"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.2"  ,  "MD2"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.5"  ,  "SHA1"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.14"  ,  "SHA224"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.11"  ,  "SHA256"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.12"  ,  "SHA384"  )  ; 
digestNames  .  put  (  "1.2.840.113549.1.1.13"  ,  "SHA512"  )  ; 
digestNames  .  put  (  "1.2.840.113549.2.5"  ,  "MD5"  )  ; 
digestNames  .  put  (  "1.2.840.113549.2.2"  ,  "MD2"  )  ; 
digestNames  .  put  (  "1.2.840.10040.4.3"  ,  "SHA1"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.3.1"  ,  "SHA224"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.3.2"  ,  "SHA256"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.3.3"  ,  "SHA384"  )  ; 
digestNames  .  put  (  "2.16.840.1.101.3.4.3.4"  ,  "SHA512"  )  ; 
digestNames  .  put  (  "1.3.36.3.3.1.3"  ,  "RIPEMD128"  )  ; 
digestNames  .  put  (  "1.3.36.3.3.1.2"  ,  "RIPEMD160"  )  ; 
digestNames  .  put  (  "1.3.36.3.3.1.4"  ,  "RIPEMD256"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.1"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.10040.4.1"  ,  "DSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.2"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.4"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.5"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.14"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.11"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.12"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.113549.1.1.13"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.2.840.10040.4.3"  ,  "DSA"  )  ; 
algorithmNames  .  put  (  "2.16.840.1.101.3.4.3.1"  ,  "DSA"  )  ; 
algorithmNames  .  put  (  "2.16.840.1.101.3.4.3.2"  ,  "DSA"  )  ; 
algorithmNames  .  put  (  "1.3.36.3.3.1.3"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.3.36.3.3.1.2"  ,  "RSA"  )  ; 
algorithmNames  .  put  (  "1.3.36.3.3.1.4"  ,  "RSA"  )  ; 
allowedDigests  .  put  (  "MD5"  ,  "1.2.840.113549.2.5"  )  ; 
allowedDigests  .  put  (  "MD2"  ,  "1.2.840.113549.2.2"  )  ; 
allowedDigests  .  put  (  "SHA1"  ,  "1.3.14.3.2.26"  )  ; 
allowedDigests  .  put  (  "SHA224"  ,  "2.16.840.1.101.3.4.2.4"  )  ; 
allowedDigests  .  put  (  "SHA256"  ,  "2.16.840.1.101.3.4.2.1"  )  ; 
allowedDigests  .  put  (  "SHA384"  ,  "2.16.840.1.101.3.4.2.2"  )  ; 
allowedDigests  .  put  (  "SHA512"  ,  "2.16.840.1.101.3.4.2.3"  )  ; 
allowedDigests  .  put  (  "MD-5"  ,  "1.2.840.113549.2.5"  )  ; 
allowedDigests  .  put  (  "MD-2"  ,  "1.2.840.113549.2.2"  )  ; 
allowedDigests  .  put  (  "SHA-1"  ,  "1.3.14.3.2.26"  )  ; 
allowedDigests  .  put  (  "SHA-224"  ,  "2.16.840.1.101.3.4.2.4"  )  ; 
allowedDigests  .  put  (  "SHA-256"  ,  "2.16.840.1.101.3.4.2.1"  )  ; 
allowedDigests  .  put  (  "SHA-384"  ,  "2.16.840.1.101.3.4.2.2"  )  ; 
allowedDigests  .  put  (  "SHA-512"  ,  "2.16.840.1.101.3.4.2.3"  )  ; 
allowedDigests  .  put  (  "RIPEMD128"  ,  "1.3.36.3.2.2"  )  ; 
allowedDigests  .  put  (  "RIPEMD-128"  ,  "1.3.36.3.2.2"  )  ; 
allowedDigests  .  put  (  "RIPEMD160"  ,  "1.3.36.3.2.1"  )  ; 
allowedDigests  .  put  (  "RIPEMD-160"  ,  "1.3.36.3.2.1"  )  ; 
allowedDigests  .  put  (  "RIPEMD256"  ,  "1.3.36.3.2.3"  )  ; 
allowedDigests  .  put  (  "RIPEMD-256"  ,  "1.3.36.3.2.3"  )  ; 
} 







public   static   String   getDigest  (  String   oid  )  { 
String   ret  =  digestNames  .  get  (  oid  )  ; 
if  (  ret  ==  null  )  return   oid  ;  else   return   ret  ; 
} 







public   static   String   getAlgorithm  (  String   oid  )  { 
String   ret  =  algorithmNames  .  get  (  oid  )  ; 
if  (  ret  ==  null  )  return   oid  ;  else   return   ret  ; 
} 

public   static   String   getAllowedDigests  (  String   name  )  { 
return   allowedDigests  .  get  (  name  .  toUpperCase  (  )  )  ; 
} 






public   TimeStampToken   getTimeStampToken  (  )  { 
return   timeStampToken  ; 
} 






public   Calendar   getTimeStampDate  (  )  { 
if  (  timeStampToken  ==  null  )  return   null  ; 
Calendar   cal  =  new   GregorianCalendar  (  )  ; 
Date   date  =  timeStampToken  .  getTimeStampInfo  (  )  .  getGenTime  (  )  ; 
cal  .  setTime  (  date  )  ; 
return   cal  ; 
} 







@  SuppressWarnings  (  "unchecked"  ) 
public   PdfPKCS7  (  byte  [  ]  contentsKey  ,  byte  [  ]  certsKey  ,  String   provider  )  { 
try  { 
this  .  provider  =  provider  ; 
X509CertParser   cr  =  new   X509CertParser  (  )  ; 
cr  .  engineInit  (  new   ByteArrayInputStream  (  certsKey  )  )  ; 
certs  =  cr  .  engineReadAll  (  )  ; 
signCerts  =  certs  ; 
signCert  =  (  X509Certificate  )  certs  .  iterator  (  )  .  next  (  )  ; 
crls  =  new   ArrayList  <  CRL  >  (  )  ; 
ASN1InputStream   in  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  contentsKey  )  )  ; 
digest  =  (  (  DEROctetString  )  in  .  readObject  (  )  )  .  getOctets  (  )  ; 
if  (  provider  ==  null  )  sig  =  Signature  .  getInstance  (  "SHA1withRSA"  )  ;  else   sig  =  Signature  .  getInstance  (  "SHA1withRSA"  ,  provider  )  ; 
sig  .  initVerify  (  signCert  .  getPublicKey  (  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 





public   boolean   isTsp  (  )  { 
return   isTsp  ; 
} 

private   BasicOCSPResp   basicResp  ; 






public   BasicOCSPResp   getOcsp  (  )  { 
return   basicResp  ; 
} 

private   void   findCRL  (  ASN1Sequence   seq  )  throws   IOException  ,  CertificateException  ,  CRLException  { 
try  { 
crls  =  new   ArrayList  <  CRL  >  (  )  ; 
for  (  int   k  =  0  ;  k  <  seq  .  size  (  )  ;  ++  k  )  { 
ByteArrayInputStream   ar  =  new   ByteArrayInputStream  (  seq  .  getObjectAt  (  k  )  .  toASN1Primitive  (  )  .  getEncoded  (  ASN1Encoding  .  DER  )  )  ; 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  )  ; 
X509CRL   crl  =  (  X509CRL  )  cf  .  generateCRL  (  ar  )  ; 
crls  .  add  (  crl  )  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 

private   void   findOcsp  (  ASN1Sequence   seq  )  throws   IOException  { 
basicResp  =  null  ; 
boolean   ret  =  false  ; 
while  (  true  )  { 
if  (  seq  .  getObjectAt  (  0  )  instanceof   ASN1ObjectIdentifier  &&  (  (  ASN1ObjectIdentifier  )  seq  .  getObjectAt  (  0  )  )  .  getId  (  )  .  equals  (  OCSPObjectIdentifiers  .  id_pkix_ocsp_basic  .  getId  (  )  )  )  { 
break  ; 
} 
ret  =  true  ; 
for  (  int   k  =  0  ;  k  <  seq  .  size  (  )  ;  ++  k  )  { 
if  (  seq  .  getObjectAt  (  k  )  instanceof   ASN1Sequence  )  { 
seq  =  (  ASN1Sequence  )  seq  .  getObjectAt  (  0  )  ; 
ret  =  false  ; 
break  ; 
} 
if  (  seq  .  getObjectAt  (  k  )  instanceof   ASN1TaggedObject  )  { 
ASN1TaggedObject   tag  =  (  ASN1TaggedObject  )  seq  .  getObjectAt  (  k  )  ; 
if  (  tag  .  getObject  (  )  instanceof   ASN1Sequence  )  { 
seq  =  (  ASN1Sequence  )  tag  .  getObject  (  )  ; 
ret  =  false  ; 
break  ; 
}  else   return  ; 
} 
} 
if  (  ret  )  return  ; 
} 
DEROctetString   os  =  (  DEROctetString  )  seq  .  getObjectAt  (  1  )  ; 
ASN1InputStream   inp  =  new   ASN1InputStream  (  os  .  getOctets  (  )  )  ; 
BasicOCSPResponse   resp  =  BasicOCSPResponse  .  getInstance  (  inp  .  readObject  (  )  )  ; 
basicResp  =  new   BasicOCSPResp  (  resp  )  ; 
} 







public   PdfPKCS7  (  byte  [  ]  contentsKey  ,  String   provider  )  { 
this  (  contentsKey  ,  false  ,  provider  )  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
public   PdfPKCS7  (  byte  [  ]  contentsKey  ,  boolean   tsp  ,  String   provider  )  { 
isTsp  =  tsp  ; 
try  { 
this  .  provider  =  provider  ; 
ASN1InputStream   din  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  contentsKey  )  )  ; 
ASN1Primitive   pkcs  ; 
try  { 
pkcs  =  din  .  readObject  (  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "can.t.decode.pkcs7signeddata.object"  )  )  ; 
} 
if  (  !  (  pkcs   instanceof   ASN1Sequence  )  )  { 
throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "not.a.valid.pkcs.7.object.not.a.sequence"  )  )  ; 
} 
ASN1Sequence   signedData  =  (  ASN1Sequence  )  pkcs  ; 
ASN1ObjectIdentifier   objId  =  (  ASN1ObjectIdentifier  )  signedData  .  getObjectAt  (  0  )  ; 
if  (  !  objId  .  getId  (  )  .  equals  (  ID_PKCS7_SIGNED_DATA  )  )  throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "not.a.valid.pkcs.7.object.not.signed.data"  )  )  ; 
ASN1Sequence   content  =  (  ASN1Sequence  )  (  (  DERTaggedObject  )  signedData  .  getObjectAt  (  1  )  )  .  getObject  (  )  ; 
version  =  (  (  ASN1Integer  )  content  .  getObjectAt  (  0  )  )  .  getValue  (  )  .  intValue  (  )  ; 
digestalgos  =  new   HashSet  <  String  >  (  )  ; 
Enumeration  <  ASN1Sequence  >  e  =  (  (  ASN1Set  )  content  .  getObjectAt  (  1  )  )  .  getObjects  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
ASN1Sequence   s  =  e  .  nextElement  (  )  ; 
ASN1ObjectIdentifier   o  =  (  ASN1ObjectIdentifier  )  s  .  getObjectAt  (  0  )  ; 
digestalgos  .  add  (  o  .  getId  (  )  )  ; 
} 
X509CertParser   cr  =  new   X509CertParser  (  )  ; 
cr  .  engineInit  (  new   ByteArrayInputStream  (  contentsKey  )  )  ; 
certs  =  cr  .  engineReadAll  (  )  ; 
ASN1Sequence   rsaData  =  (  ASN1Sequence  )  content  .  getObjectAt  (  2  )  ; 
if  (  rsaData  .  size  (  )  >  1  )  { 
DEROctetString   rsaDataContent  =  (  DEROctetString  )  (  (  DERTaggedObject  )  rsaData  .  getObjectAt  (  1  )  )  .  getObject  (  )  ; 
RSAdata  =  rsaDataContent  .  getOctets  (  )  ; 
} 
int   next  =  3  ; 
while  (  content  .  getObjectAt  (  next  )  instanceof   DERTaggedObject  )  ++  next  ; 
ASN1Set   signerInfos  =  (  ASN1Set  )  content  .  getObjectAt  (  next  )  ; 
if  (  signerInfos  .  size  (  )  !=  1  )  throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "this.pkcs.7.object.has.multiple.signerinfos.only.one.is.supported.at.this.time"  )  )  ; 
ASN1Sequence   signerInfo  =  (  ASN1Sequence  )  signerInfos  .  getObjectAt  (  0  )  ; 
signerversion  =  (  (  ASN1Integer  )  signerInfo  .  getObjectAt  (  0  )  )  .  getValue  (  )  .  intValue  (  )  ; 
ASN1Sequence   issuerAndSerialNumber  =  (  ASN1Sequence  )  signerInfo  .  getObjectAt  (  1  )  ; 
X509Principal   issuer  =  new   X509Principal  (  issuerAndSerialNumber  .  getObjectAt  (  0  )  .  toASN1Primitive  (  )  .  getEncoded  (  )  )  ; 
BigInteger   serialNumber  =  (  (  ASN1Integer  )  issuerAndSerialNumber  .  getObjectAt  (  1  )  )  .  getValue  (  )  ; 
for  (  Object   element  :  certs  )  { 
X509Certificate   cert  =  (  X509Certificate  )  element  ; 
if  (  issuer  .  equals  (  cert  .  getIssuerDN  (  )  )  &&  serialNumber  .  equals  (  cert  .  getSerialNumber  (  )  )  )  { 
signCert  =  cert  ; 
break  ; 
} 
} 
if  (  signCert  ==  null  )  { 
throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "can.t.find.signing.certificate.with.serial.1"  ,  issuer  .  getName  (  )  +  " / "  +  serialNumber  .  toString  (  16  )  )  )  ; 
} 
signCertificateChain  (  )  ; 
digestAlgorithm  =  (  (  ASN1ObjectIdentifier  )  (  (  ASN1Sequence  )  signerInfo  .  getObjectAt  (  2  )  )  .  getObjectAt  (  0  )  )  .  getId  (  )  ; 
next  =  3  ; 
if  (  signerInfo  .  getObjectAt  (  next  )  instanceof   ASN1TaggedObject  )  { 
ASN1TaggedObject   tagsig  =  (  ASN1TaggedObject  )  signerInfo  .  getObjectAt  (  next  )  ; 
ASN1Set   sseq  =  ASN1Set  .  getInstance  (  tagsig  ,  false  )  ; 
sigAttr  =  sseq  .  getEncoded  (  ASN1Encoding  .  DER  )  ; 
for  (  int   k  =  0  ;  k  <  sseq  .  size  (  )  ;  ++  k  )  { 
ASN1Sequence   seq2  =  (  ASN1Sequence  )  sseq  .  getObjectAt  (  k  )  ; 
if  (  (  (  ASN1ObjectIdentifier  )  seq2  .  getObjectAt  (  0  )  )  .  getId  (  )  .  equals  (  ID_MESSAGE_DIGEST  )  )  { 
ASN1Set   set  =  (  ASN1Set  )  seq2  .  getObjectAt  (  1  )  ; 
digestAttr  =  (  (  DEROctetString  )  set  .  getObjectAt  (  0  )  )  .  getOctets  (  )  ; 
}  else   if  (  (  (  ASN1ObjectIdentifier  )  seq2  .  getObjectAt  (  0  )  )  .  getId  (  )  .  equals  (  ID_ADBE_REVOCATION  )  )  { 
ASN1Set   setout  =  (  ASN1Set  )  seq2  .  getObjectAt  (  1  )  ; 
ASN1Sequence   seqout  =  (  ASN1Sequence  )  setout  .  getObjectAt  (  0  )  ; 
for  (  int   j  =  0  ;  j  <  seqout  .  size  (  )  ;  ++  j  )  { 
ASN1TaggedObject   tg  =  (  ASN1TaggedObject  )  seqout  .  getObjectAt  (  j  )  ; 
if  (  tg  .  getTagNo  (  )  ==  0  )  { 
ASN1Sequence   seqin  =  (  ASN1Sequence  )  tg  .  getObject  (  )  ; 
findCRL  (  seqin  )  ; 
} 
if  (  tg  .  getTagNo  (  )  ==  1  )  { 
ASN1Sequence   seqin  =  (  ASN1Sequence  )  tg  .  getObject  (  )  ; 
findOcsp  (  seqin  )  ; 
} 
} 
} 
} 
if  (  digestAttr  ==  null  )  throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "authenticated.attribute.is.missing.the.digest"  )  )  ; 
++  next  ; 
} 
digestEncryptionAlgorithm  =  (  (  ASN1ObjectIdentifier  )  (  (  ASN1Sequence  )  signerInfo  .  getObjectAt  (  next  ++  )  )  .  getObjectAt  (  0  )  )  .  getId  (  )  ; 
digest  =  (  (  DEROctetString  )  signerInfo  .  getObjectAt  (  next  ++  )  )  .  getOctets  (  )  ; 
if  (  next  <  signerInfo  .  size  (  )  &&  signerInfo  .  getObjectAt  (  next  )  instanceof   DERTaggedObject  )  { 
DERTaggedObject   taggedObject  =  (  DERTaggedObject  )  signerInfo  .  getObjectAt  (  next  )  ; 
ASN1Set   unat  =  ASN1Set  .  getInstance  (  taggedObject  ,  false  )  ; 
AttributeTable   attble  =  new   AttributeTable  (  unat  )  ; 
Attribute   ts  =  attble  .  get  (  PKCSObjectIdentifiers  .  id_aa_signatureTimeStampToken  )  ; 
if  (  ts  !=  null  &&  ts  .  getAttrValues  (  )  .  size  (  )  >  0  )  { 
ASN1Set   attributeValues  =  ts  .  getAttrValues  (  )  ; 
ASN1Sequence   tokenSequence  =  ASN1Sequence  .  getInstance  (  attributeValues  .  getObjectAt  (  0  )  )  ; 
ContentInfo   contentInfo  =  new   ContentInfo  (  tokenSequence  )  ; 
this  .  timeStampToken  =  new   TimeStampToken  (  contentInfo  )  ; 
} 
} 
if  (  isTsp  )  { 
ContentInfo   contentInfoTsp  =  new   ContentInfo  (  signedData  )  ; 
this  .  timeStampToken  =  new   TimeStampToken  (  contentInfoTsp  )  ; 
TimeStampTokenInfo   info  =  timeStampToken  .  getTimeStampInfo  (  )  ; 
String   algOID  =  info  .  getMessageImprintAlgOID  (  )  .  getId  (  )  ; 
messageDigest  =  MessageDigest  .  getInstance  (  algOID  )  ; 
}  else  { 
if  (  RSAdata  !=  null  ||  digestAttr  !=  null  )  { 
if  (  provider  ==  null  ||  provider  .  startsWith  (  "SunPKCS11"  )  )  { 
messageDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  )  ; 
encContDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  )  ; 
}  else  { 
messageDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  ,  provider  )  ; 
encContDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  ,  provider  )  ; 
} 
} 
if  (  provider  ==  null  )  sig  =  Signature  .  getInstance  (  getDigestAlgorithm  (  )  )  ;  else   sig  =  Signature  .  getInstance  (  getDigestAlgorithm  (  )  ,  provider  )  ; 
sig  .  initVerify  (  signCert  .  getPublicKey  (  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 













public   PdfPKCS7  (  PrivateKey   privKey  ,  Certificate  [  ]  certChain  ,  CRL  [  ]  crlList  ,  String   hashAlgorithm  ,  String   provider  ,  boolean   hasRSAdata  )  throws   InvalidKeyException  ,  NoSuchProviderException  ,  NoSuchAlgorithmException  { 
this  .  privKey  =  privKey  ; 
this  .  provider  =  provider  ; 
digestAlgorithm  =  getAllowedDigests  (  hashAlgorithm  )  ; 
if  (  digestAlgorithm  ==  null  )  throw   new   NoSuchAlgorithmException  (  MessageLocalization  .  getComposedMessage  (  "unknown.hash.algorithm.1"  ,  hashAlgorithm  )  )  ; 
version  =  signerversion  =  1  ; 
certs  =  new   ArrayList  <  Certificate  >  (  )  ; 
crls  =  new   ArrayList  <  CRL  >  (  )  ; 
digestalgos  =  new   HashSet  <  String  >  (  )  ; 
digestalgos  .  add  (  digestAlgorithm  )  ; 
signCert  =  (  X509Certificate  )  certChain  [  0  ]  ; 
for  (  Certificate   element  :  certChain  )  { 
certs  .  add  (  element  )  ; 
} 
if  (  crlList  !=  null  )  { 
for  (  CRL   element  :  crlList  )  { 
crls  .  add  (  element  )  ; 
} 
} 
if  (  privKey  !=  null  )  { 
digestEncryptionAlgorithm  =  privKey  .  getAlgorithm  (  )  ; 
if  (  digestEncryptionAlgorithm  .  equals  (  "RSA"  )  )  { 
digestEncryptionAlgorithm  =  ID_RSA  ; 
}  else   if  (  digestEncryptionAlgorithm  .  equals  (  "DSA"  )  )  { 
digestEncryptionAlgorithm  =  ID_DSA  ; 
}  else  { 
throw   new   NoSuchAlgorithmException  (  MessageLocalization  .  getComposedMessage  (  "unknown.key.algorithm.1"  ,  digestEncryptionAlgorithm  )  )  ; 
} 
} 
if  (  hasRSAdata  )  { 
RSAdata  =  new   byte  [  0  ]  ; 
if  (  provider  ==  null  ||  provider  .  startsWith  (  "SunPKCS11"  )  )  messageDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  )  ;  else   messageDigest  =  MessageDigest  .  getInstance  (  getHashAlgorithm  (  )  ,  provider  )  ; 
} 
if  (  privKey  !=  null  )  { 
if  (  provider  ==  null  )  sig  =  Signature  .  getInstance  (  getDigestAlgorithm  (  )  )  ;  else   sig  =  Signature  .  getInstance  (  getDigestAlgorithm  (  )  ,  provider  )  ; 
sig  .  initSign  (  privKey  )  ; 
} 
} 








public   void   update  (  byte  [  ]  buf  ,  int   off  ,  int   len  )  throws   SignatureException  { 
if  (  RSAdata  !=  null  ||  digestAttr  !=  null  ||  isTsp  )  messageDigest  .  update  (  buf  ,  off  ,  len  )  ;  else   sig  .  update  (  buf  ,  off  ,  len  )  ; 
} 






public   boolean   verify  (  )  throws   SignatureException  { 
if  (  verified  )  return   verifyResult  ; 
if  (  isTsp  )  { 
TimeStampTokenInfo   info  =  timeStampToken  .  getTimeStampInfo  (  )  ; 
MessageImprint   imprint  =  info  .  toASN1Structure  (  )  .  getMessageImprint  (  )  ; 
byte  [  ]  md  =  messageDigest  .  digest  (  )  ; 
byte  [  ]  imphashed  =  imprint  .  getHashedMessage  (  )  ; 
verifyResult  =  Arrays  .  equals  (  md  ,  imphashed  )  ; 
}  else  { 
if  (  sigAttr  !=  null  )  { 
final   byte  [  ]  msgDigestBytes  =  messageDigest  .  digest  (  )  ; 
boolean   verifyRSAdata  =  true  ; 
sig  .  update  (  sigAttr  )  ; 
boolean   encContDigestCompare  =  false  ; 
if  (  RSAdata  !=  null  )  { 
verifyRSAdata  =  Arrays  .  equals  (  msgDigestBytes  ,  RSAdata  )  ; 
encContDigest  .  update  (  RSAdata  )  ; 
encContDigestCompare  =  Arrays  .  equals  (  encContDigest  .  digest  (  )  ,  digestAttr  )  ; 
} 
boolean   absentEncContDigestCompare  =  Arrays  .  equals  (  msgDigestBytes  ,  digestAttr  )  ; 
boolean   concludingDigestCompare  =  absentEncContDigestCompare  ||  encContDigestCompare  ; 
boolean   sigVerify  =  sig  .  verify  (  digest  )  ; 
verifyResult  =  concludingDigestCompare  &&  sigVerify  &&  verifyRSAdata  ; 
}  else  { 
if  (  RSAdata  !=  null  )  sig  .  update  (  messageDigest  .  digest  (  )  )  ; 
verifyResult  =  sig  .  verify  (  digest  )  ; 
} 
} 
verified  =  true  ; 
return   verifyResult  ; 
} 







public   boolean   verifyTimestampImprint  (  )  throws   NoSuchAlgorithmException  { 
if  (  timeStampToken  ==  null  )  return   false  ; 
TimeStampTokenInfo   info  =  timeStampToken  .  getTimeStampInfo  (  )  ; 
MessageImprint   imprint  =  info  .  toASN1Structure  (  )  .  getMessageImprint  (  )  ; 
String   algOID  =  info  .  getMessageImprintAlgOID  (  )  .  getId  (  )  ; 
byte  [  ]  md  =  MessageDigest  .  getInstance  (  algOID  )  .  digest  (  digest  )  ; 
byte  [  ]  imphashed  =  imprint  .  getHashedMessage  (  )  ; 
boolean   res  =  Arrays  .  equals  (  md  ,  imphashed  )  ; 
return   res  ; 
} 






public   Certificate  [  ]  getCertificates  (  )  { 
return   certs  .  toArray  (  new   X509Certificate  [  certs  .  size  (  )  ]  )  ; 
} 








public   Certificate  [  ]  getSignCertificateChain  (  )  { 
return   signCerts  .  toArray  (  new   X509Certificate  [  signCerts  .  size  (  )  ]  )  ; 
} 

private   void   signCertificateChain  (  )  { 
ArrayList  <  Certificate  >  cc  =  new   ArrayList  <  Certificate  >  (  )  ; 
cc  .  add  (  signCert  )  ; 
ArrayList  <  Certificate  >  oc  =  new   ArrayList  <  Certificate  >  (  certs  )  ; 
for  (  int   k  =  0  ;  k  <  oc  .  size  (  )  ;  ++  k  )  { 
if  (  signCert  .  equals  (  oc  .  get  (  k  )  )  )  { 
oc  .  remove  (  k  )  ; 
--  k  ; 
continue  ; 
} 
} 
boolean   found  =  true  ; 
while  (  found  )  { 
X509Certificate   v  =  (  X509Certificate  )  cc  .  get  (  cc  .  size  (  )  -  1  )  ; 
found  =  false  ; 
for  (  int   k  =  0  ;  k  <  oc  .  size  (  )  ;  ++  k  )  { 
try  { 
if  (  provider  ==  null  )  v  .  verify  (  (  (  X509Certificate  )  oc  .  get  (  k  )  )  .  getPublicKey  (  )  )  ;  else   v  .  verify  (  (  (  X509Certificate  )  oc  .  get  (  k  )  )  .  getPublicKey  (  )  ,  provider  )  ; 
found  =  true  ; 
cc  .  add  (  oc  .  get  (  k  )  )  ; 
oc  .  remove  (  k  )  ; 
break  ; 
}  catch  (  Exception   e  )  { 
} 
} 
} 
signCerts  =  cc  ; 
} 





public   Collection  <  CRL  >  getCRLs  (  )  { 
return   crls  ; 
} 





public   X509Certificate   getSigningCertificate  (  )  { 
return   signCert  ; 
} 





public   int   getVersion  (  )  { 
return   version  ; 
} 





public   int   getSigningInfoVersion  (  )  { 
return   signerversion  ; 
} 




public   String   getDigestEncryptionAlgorithmOid  (  )  { 
return   digestEncryptionAlgorithm  ; 
} 




public   String   getDigestAlgorithmOid  (  )  { 
return   digestAlgorithm  ; 
} 





public   String   getDigestAlgorithm  (  )  { 
String   dea  =  getAlgorithm  (  digestEncryptionAlgorithm  )  ; 
if  (  dea  ==  null  )  dea  =  digestEncryptionAlgorithm  ; 
return   getHashAlgorithm  (  )  +  "with"  +  dea  ; 
} 





public   String   getHashAlgorithm  (  )  { 
return   getDigest  (  digestAlgorithm  )  ; 
} 






public   static   KeyStore   loadCacertsKeyStore  (  )  { 
return   loadCacertsKeyStore  (  null  )  ; 
} 






public   static   KeyStore   loadCacertsKeyStore  (  String   provider  )  { 
File   file  =  new   File  (  System  .  getProperty  (  "java.home"  )  ,  "lib"  )  ; 
file  =  new   File  (  file  ,  "security"  )  ; 
file  =  new   File  (  file  ,  "cacerts"  )  ; 
FileInputStream   fin  =  null  ; 
try  { 
fin  =  new   FileInputStream  (  file  )  ; 
KeyStore   k  ; 
if  (  provider  ==  null  )  k  =  KeyStore  .  getInstance  (  "JKS"  )  ;  else   k  =  KeyStore  .  getInstance  (  "JKS"  ,  provider  )  ; 
k  .  load  (  fin  ,  null  )  ; 
return   k  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
}  finally  { 
try  { 
if  (  fin  !=  null  )  { 
fin  .  close  (  )  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 
} 









public   static   String   verifyCertificate  (  X509Certificate   cert  ,  Collection  <  CRL  >  crls  ,  Calendar   calendar  )  { 
if  (  calendar  ==  null  )  calendar  =  new   GregorianCalendar  (  )  ; 
if  (  cert  .  hasUnsupportedCriticalExtension  (  )  )  return  "Has unsupported critical extension"  ; 
try  { 
cert  .  checkValidity  (  calendar  .  getTime  (  )  )  ; 
}  catch  (  Exception   e  )  { 
return   e  .  getMessage  (  )  ; 
} 
if  (  crls  !=  null  )  { 
for  (  CRL   crl  :  crls  )  { 
if  (  crl  .  isRevoked  (  cert  )  )  return  "Certificate revoked"  ; 
} 
} 
return   null  ; 
} 











public   static   Object  [  ]  verifyCertificates  (  Certificate   certs  [  ]  ,  KeyStore   keystore  ,  Collection  <  CRL  >  crls  ,  Calendar   calendar  )  { 
if  (  calendar  ==  null  )  calendar  =  new   GregorianCalendar  (  )  ; 
for  (  int   k  =  0  ;  k  <  certs  .  length  ;  ++  k  )  { 
X509Certificate   cert  =  (  X509Certificate  )  certs  [  k  ]  ; 
String   err  =  verifyCertificate  (  cert  ,  crls  ,  calendar  )  ; 
if  (  err  !=  null  )  return   new   Object  [  ]  {  cert  ,  err  }  ; 
try  { 
for  (  Enumeration  <  String  >  aliases  =  keystore  .  aliases  (  )  ;  aliases  .  hasMoreElements  (  )  ;  )  { 
try  { 
String   alias  =  aliases  .  nextElement  (  )  ; 
if  (  !  keystore  .  isCertificateEntry  (  alias  )  )  continue  ; 
X509Certificate   certStoreX509  =  (  X509Certificate  )  keystore  .  getCertificate  (  alias  )  ; 
if  (  verifyCertificate  (  certStoreX509  ,  crls  ,  calendar  )  !=  null  )  continue  ; 
try  { 
cert  .  verify  (  certStoreX509  .  getPublicKey  (  )  )  ; 
return   null  ; 
}  catch  (  Exception   e  )  { 
continue  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 
}  catch  (  Exception   e  )  { 
} 
int   j  ; 
for  (  j  =  0  ;  j  <  certs  .  length  ;  ++  j  )  { 
if  (  j  ==  k  )  continue  ; 
X509Certificate   certNext  =  (  X509Certificate  )  certs  [  j  ]  ; 
try  { 
cert  .  verify  (  certNext  .  getPublicKey  (  )  )  ; 
break  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  j  ==  certs  .  length  )  return   new   Object  [  ]  {  cert  ,  "Cannot be verified against the KeyStore or the certificate chain"  }  ; 
} 
return   new   Object  [  ]  {  null  ,  "Invalid state. Possible circular certificate chain"  }  ; 
} 









public   static   boolean   verifyOcspCertificates  (  BasicOCSPResp   ocsp  ,  KeyStore   keystore  ,  String   provider  )  { 
if  (  provider  ==  null  )  provider  =  "BC"  ; 
try  { 
for  (  Enumeration  <  String  >  aliases  =  keystore  .  aliases  (  )  ;  aliases  .  hasMoreElements  (  )  ;  )  { 
try  { 
String   alias  =  aliases  .  nextElement  (  )  ; 
if  (  !  keystore  .  isCertificateEntry  (  alias  )  )  continue  ; 
X509Certificate   certStoreX509  =  (  X509Certificate  )  keystore  .  getCertificate  (  alias  )  ; 
if  (  ocsp  .  isSignatureValid  (  new   JcaContentVerifierProviderBuilder  (  )  .  setProvider  (  provider  )  .  build  (  certStoreX509  .  getPublicKey  (  )  )  )  )  return   true  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
}  catch  (  Exception   e  )  { 
} 
return   false  ; 
} 









public   static   boolean   verifyTimestampCertificates  (  TimeStampToken   ts  ,  KeyStore   keystore  ,  String   provider  )  { 
if  (  provider  ==  null  )  provider  =  "BC"  ; 
try  { 
for  (  Enumeration  <  String  >  aliases  =  keystore  .  aliases  (  )  ;  aliases  .  hasMoreElements  (  )  ;  )  { 
try  { 
String   alias  =  aliases  .  nextElement  (  )  ; 
if  (  !  keystore  .  isCertificateEntry  (  alias  )  )  continue  ; 
X509Certificate   certStoreX509  =  (  X509Certificate  )  keystore  .  getCertificate  (  alias  )  ; 
ts  .  isSignatureValid  (  new   JcaSimpleSignerInfoVerifierBuilder  (  )  .  setProvider  (  provider  )  .  build  (  certStoreX509  )  )  ; 
return   true  ; 
}  catch  (  Exception   ex  )  { 
} 
} 
}  catch  (  Exception   e  )  { 
} 
return   false  ; 
} 








public   static   String   getOCSPURL  (  X509Certificate   certificate  )  throws   CertificateParsingException  { 
try  { 
ASN1Primitive   obj  =  getExtensionValue  (  certificate  ,  Extension  .  authorityInfoAccess  .  getId  (  )  )  ; 
if  (  obj  ==  null  )  { 
return   null  ; 
} 
ASN1Sequence   AccessDescriptions  =  (  ASN1Sequence  )  obj  ; 
for  (  int   i  =  0  ;  i  <  AccessDescriptions  .  size  (  )  ;  i  ++  )  { 
ASN1Sequence   AccessDescription  =  (  ASN1Sequence  )  AccessDescriptions  .  getObjectAt  (  i  )  ; 
if  (  AccessDescription  .  size  (  )  !=  2  )  { 
continue  ; 
}  else  { 
if  (  AccessDescription  .  getObjectAt  (  0  )  instanceof   ASN1ObjectIdentifier  &&  (  (  ASN1ObjectIdentifier  )  AccessDescription  .  getObjectAt  (  0  )  )  .  getId  (  )  .  equals  (  "1.3.6.1.5.5.7.48.1"  )  )  { 
String   AccessLocation  =  getStringFromGeneralName  (  (  ASN1Primitive  )  AccessDescription  .  getObjectAt  (  1  )  )  ; 
if  (  AccessLocation  ==  null  )  { 
return  ""  ; 
}  else  { 
return   AccessLocation  ; 
} 
} 
} 
} 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 

public   static   String   getCrlUrl  (  X509Certificate   certificate  )  throws   CertificateParsingException  { 
try  { 
ASN1Primitive   obj  =  getExtensionValue  (  certificate  ,  Extension  .  cRLDistributionPoints  .  getId  (  )  )  ; 
if  (  obj  ==  null  )  { 
return   null  ; 
} 
CRLDistPoint   dist  =  CRLDistPoint  .  getInstance  (  obj  )  ; 
DistributionPoint  [  ]  dists  =  dist  .  getDistributionPoints  (  )  ; 
for  (  DistributionPoint   p  :  dists  )  { 
DistributionPointName   distributionPointName  =  p  .  getDistributionPoint  (  )  ; 
if  (  DistributionPointName  .  FULL_NAME  !=  distributionPointName  .  getType  (  )  )  { 
continue  ; 
} 
GeneralNames   generalNames  =  (  GeneralNames  )  distributionPointName  .  getName  (  )  ; 
GeneralName  [  ]  names  =  generalNames  .  getNames  (  )  ; 
for  (  GeneralName   name  :  names  )  { 
if  (  name  .  getTagNo  (  )  !=  GeneralName  .  uniformResourceIdentifier  )  { 
continue  ; 
} 
DERIA5String   derStr  =  DERIA5String  .  getInstance  (  (  ASN1TaggedObject  )  name  .  toASN1Primitive  (  )  ,  false  )  ; 
return   derStr  .  getString  (  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
} 
return   null  ; 
} 






public   boolean   isRevocationValid  (  )  { 
if  (  basicResp  ==  null  )  return   false  ; 
if  (  signCerts  .  size  (  )  <  2  )  return   false  ; 
try  { 
X509Certificate  [  ]  cs  =  (  X509Certificate  [  ]  )  getSignCertificateChain  (  )  ; 
SingleResp   sr  =  basicResp  .  getResponses  (  )  [  0  ]  ; 
CertificateID   cid  =  sr  .  getCertID  (  )  ; 
X509Certificate   sigcer  =  getSigningCertificate  (  )  ; 
X509Certificate   isscer  =  cs  [  1  ]  ; 
CertificateID   tis  =  new   CertificateID  (  new   JcaDigestCalculatorProviderBuilder  (  )  .  build  (  )  .  get  (  CertificateID  .  HASH_SHA1  )  ,  new   JcaX509CertificateHolder  (  isscer  )  ,  sigcer  .  getSerialNumber  (  )  )  ; 
return   tis  .  equals  (  cid  )  ; 
}  catch  (  Exception   ex  )  { 
} 
return   false  ; 
} 

private   static   ASN1Primitive   getExtensionValue  (  X509Certificate   cert  ,  String   oid  )  throws   IOException  { 
byte  [  ]  bytes  =  cert  .  getExtensionValue  (  oid  )  ; 
if  (  bytes  ==  null  )  { 
return   null  ; 
} 
ASN1InputStream   aIn  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  bytes  )  )  ; 
ASN1OctetString   octs  =  (  ASN1OctetString  )  aIn  .  readObject  (  )  ; 
aIn  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  octs  .  getOctets  (  )  )  )  ; 
return   aIn  .  readObject  (  )  ; 
} 

private   static   String   getStringFromGeneralName  (  ASN1Primitive   names  )  throws   IOException  { 
DERTaggedObject   taggedObject  =  (  DERTaggedObject  )  names  ; 
return   new   String  (  ASN1OctetString  .  getInstance  (  taggedObject  ,  false  )  .  getOctets  (  )  ,  "ISO-8859-1"  )  ; 
} 






private   static   ASN1Primitive   getIssuer  (  byte  [  ]  enc  )  { 
try  { 
ASN1InputStream   in  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  enc  )  )  ; 
ASN1Sequence   seq  =  (  ASN1Sequence  )  in  .  readObject  (  )  ; 
return  (  ASN1Primitive  )  seq  .  getObjectAt  (  seq  .  getObjectAt  (  0  )  instanceof   DERTaggedObject  ?  3  :  2  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 






private   static   ASN1Primitive   getSubject  (  byte  [  ]  enc  )  { 
try  { 
ASN1InputStream   in  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  enc  )  )  ; 
ASN1Sequence   seq  =  (  ASN1Sequence  )  in  .  readObject  (  )  ; 
return  (  ASN1Primitive  )  seq  .  getObjectAt  (  seq  .  getObjectAt  (  0  )  instanceof   DERTaggedObject  ?  5  :  4  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 






public   static   X500Name   getIssuerFields  (  X509Certificate   cert  )  { 
try  { 
return   new   X500Name  (  (  ASN1Sequence  )  getIssuer  (  cert  .  getTBSCertificate  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 






public   static   X500Name   getSubjectFields  (  X509Certificate   cert  )  { 
try  { 
return   new   X500Name  (  (  ASN1Sequence  )  getSubject  (  cert  .  getTBSCertificate  (  )  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 





public   byte  [  ]  getEncodedPKCS1  (  )  { 
try  { 
if  (  externalDigest  !=  null  )  digest  =  externalDigest  ;  else   digest  =  sig  .  sign  (  )  ; 
ByteArrayOutputStream   bOut  =  new   ByteArrayOutputStream  (  )  ; 
ASN1OutputStream   dout  =  new   ASN1OutputStream  (  bOut  )  ; 
dout  .  writeObject  (  new   DEROctetString  (  digest  )  )  ; 
dout  .  close  (  )  ; 
return   bOut  .  toByteArray  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 









public   void   setExternalDigest  (  byte   digest  [  ]  ,  byte   RSAdata  [  ]  ,  String   digestEncryptionAlgorithm  )  { 
externalDigest  =  digest  ; 
externalRSAdata  =  RSAdata  ; 
if  (  digestEncryptionAlgorithm  !=  null  )  { 
if  (  digestEncryptionAlgorithm  .  equals  (  "RSA"  )  )  { 
this  .  digestEncryptionAlgorithm  =  ID_RSA  ; 
}  else   if  (  digestEncryptionAlgorithm  .  equals  (  "DSA"  )  )  { 
this  .  digestEncryptionAlgorithm  =  ID_DSA  ; 
}  else   throw   new   ExceptionConverter  (  new   NoSuchAlgorithmException  (  MessageLocalization  .  getComposedMessage  (  "unknown.key.algorithm.1"  ,  digestEncryptionAlgorithm  )  )  )  ; 
} 
} 





public   byte  [  ]  getEncodedPKCS7  (  )  { 
return   getEncodedPKCS7  (  null  ,  null  ,  null  ,  null  )  ; 
} 








public   byte  [  ]  getEncodedPKCS7  (  byte   secondDigest  [  ]  ,  Calendar   signingTime  )  { 
return   getEncodedPKCS7  (  secondDigest  ,  signingTime  ,  null  ,  null  )  ; 
} 











public   byte  [  ]  getEncodedPKCS7  (  byte   secondDigest  [  ]  ,  Calendar   signingTime  ,  TSAClient   tsaClient  ,  byte  [  ]  ocsp  )  { 
try  { 
if  (  externalDigest  !=  null  )  { 
digest  =  externalDigest  ; 
if  (  RSAdata  !=  null  )  RSAdata  =  externalRSAdata  ; 
}  else   if  (  externalRSAdata  !=  null  &&  RSAdata  !=  null  )  { 
RSAdata  =  externalRSAdata  ; 
sig  .  update  (  RSAdata  )  ; 
digest  =  sig  .  sign  (  )  ; 
}  else  { 
if  (  RSAdata  !=  null  )  { 
RSAdata  =  messageDigest  .  digest  (  )  ; 
sig  .  update  (  RSAdata  )  ; 
} 
digest  =  sig  .  sign  (  )  ; 
} 
ASN1EncodableVector   digestAlgorithms  =  new   ASN1EncodableVector  (  )  ; 
for  (  Object   element  :  digestalgos  )  { 
ASN1EncodableVector   algos  =  new   ASN1EncodableVector  (  )  ; 
algos  .  add  (  new   ASN1ObjectIdentifier  (  (  String  )  element  )  )  ; 
algos  .  add  (  DERNull  .  INSTANCE  )  ; 
digestAlgorithms  .  add  (  new   DERSequence  (  algos  )  )  ; 
} 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_PKCS7_DATA  )  )  ; 
if  (  RSAdata  !=  null  )  v  .  add  (  new   DERTaggedObject  (  0  ,  new   DEROctetString  (  RSAdata  )  )  )  ; 
DERSequence   contentinfo  =  new   DERSequence  (  v  )  ; 
v  =  new   ASN1EncodableVector  (  )  ; 
for  (  Object   element  :  certs  )  { 
ASN1InputStream   tempstream  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  (  (  X509Certificate  )  element  )  .  getEncoded  (  )  )  )  ; 
v  .  add  (  tempstream  .  readObject  (  )  )  ; 
} 
DERSet   dercertificates  =  new   DERSet  (  v  )  ; 
ASN1EncodableVector   signerinfo  =  new   ASN1EncodableVector  (  )  ; 
signerinfo  .  add  (  new   ASN1Integer  (  signerversion  )  )  ; 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  getIssuer  (  signCert  .  getTBSCertificate  (  )  )  )  ; 
v  .  add  (  new   ASN1Integer  (  signCert  .  getSerialNumber  (  )  )  )  ; 
signerinfo  .  add  (  new   DERSequence  (  v  )  )  ; 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  digestAlgorithm  )  )  ; 
v  .  add  (  new   DERNull  (  )  )  ; 
signerinfo  .  add  (  new   DERSequence  (  v  )  )  ; 
if  (  secondDigest  !=  null  &&  signingTime  !=  null  )  { 
signerinfo  .  add  (  new   DERTaggedObject  (  false  ,  0  ,  getAuthenticatedAttributeSet  (  secondDigest  ,  signingTime  ,  ocsp  )  )  )  ; 
} 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  digestEncryptionAlgorithm  )  )  ; 
v  .  add  (  new   DERNull  (  )  )  ; 
signerinfo  .  add  (  new   DERSequence  (  v  )  )  ; 
signerinfo  .  add  (  new   DEROctetString  (  digest  )  )  ; 
if  (  tsaClient  !=  null  )  { 
byte  [  ]  tsImprint  =  MessageDigest  .  getInstance  (  tsaClient  .  getDigestAlgorithm  (  )  )  .  digest  (  digest  )  ; 
byte  [  ]  tsToken  =  tsaClient  .  getTimeStampToken  (  tsImprint  )  ; 
if  (  tsToken  !=  null  )  { 
ASN1EncodableVector   unauthAttributes  =  buildUnauthenticatedAttributes  (  tsToken  )  ; 
if  (  unauthAttributes  !=  null  )  { 
signerinfo  .  add  (  new   DERTaggedObject  (  false  ,  1  ,  new   DERSet  (  unauthAttributes  )  )  )  ; 
} 
} 
} 
ASN1EncodableVector   body  =  new   ASN1EncodableVector  (  )  ; 
body  .  add  (  new   ASN1Integer  (  version  )  )  ; 
body  .  add  (  new   DERSet  (  digestAlgorithms  )  )  ; 
body  .  add  (  contentinfo  )  ; 
body  .  add  (  new   DERTaggedObject  (  false  ,  0  ,  dercertificates  )  )  ; 
body  .  add  (  new   DERSet  (  new   DERSequence  (  signerinfo  )  )  )  ; 
ASN1EncodableVector   whole  =  new   ASN1EncodableVector  (  )  ; 
whole  .  add  (  new   ASN1ObjectIdentifier  (  ID_PKCS7_SIGNED_DATA  )  )  ; 
whole  .  add  (  new   DERTaggedObject  (  0  ,  new   DERSequence  (  body  )  )  )  ; 
ByteArrayOutputStream   bOut  =  new   ByteArrayOutputStream  (  )  ; 
ASN1OutputStream   dout  =  new   ASN1OutputStream  (  bOut  )  ; 
dout  .  writeObject  (  new   DERSequence  (  whole  )  )  ; 
dout  .  close  (  )  ; 
return   bOut  .  toByteArray  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 










private   ASN1EncodableVector   buildUnauthenticatedAttributes  (  byte  [  ]  timeStampToken  )  throws   IOException  { 
if  (  timeStampToken  ==  null  )  return   null  ; 
String   ID_TIME_STAMP_TOKEN  =  "1.2.840.113549.1.9.16.2.14"  ; 
ASN1InputStream   tempstream  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  timeStampToken  )  )  ; 
ASN1EncodableVector   unauthAttributes  =  new   ASN1EncodableVector  (  )  ; 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_TIME_STAMP_TOKEN  )  )  ; 
ASN1Sequence   seq  =  (  ASN1Sequence  )  tempstream  .  readObject  (  )  ; 
v  .  add  (  new   DERSet  (  seq  )  )  ; 
unauthAttributes  .  add  (  new   DERSequence  (  v  )  )  ; 
return   unauthAttributes  ; 
} 




























public   byte  [  ]  getAuthenticatedAttributeBytes  (  byte   secondDigest  [  ]  ,  Calendar   signingTime  ,  byte  [  ]  ocsp  )  { 
try  { 
return   getAuthenticatedAttributeSet  (  secondDigest  ,  signingTime  ,  ocsp  )  .  getEncoded  (  ASN1Encoding  .  DER  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 

private   DERSet   getAuthenticatedAttributeSet  (  byte   secondDigest  [  ]  ,  Calendar   signingTime  ,  byte  [  ]  ocsp  )  { 
try  { 
ASN1EncodableVector   attribute  =  new   ASN1EncodableVector  (  )  ; 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_CONTENT_TYPE  )  )  ; 
v  .  add  (  new   DERSet  (  new   ASN1ObjectIdentifier  (  ID_PKCS7_DATA  )  )  )  ; 
attribute  .  add  (  new   DERSequence  (  v  )  )  ; 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_SIGNING_TIME  )  )  ; 
v  .  add  (  new   DERSet  (  new   DERUTCTime  (  signingTime  .  getTime  (  )  )  )  )  ; 
attribute  .  add  (  new   DERSequence  (  v  )  )  ; 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_MESSAGE_DIGEST  )  )  ; 
v  .  add  (  new   DERSet  (  new   DEROctetString  (  secondDigest  )  )  )  ; 
attribute  .  add  (  new   DERSequence  (  v  )  )  ; 
if  (  ocsp  !=  null  ||  !  crls  .  isEmpty  (  )  )  { 
v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   ASN1ObjectIdentifier  (  ID_ADBE_REVOCATION  )  )  ; 
ASN1EncodableVector   revocationV  =  new   ASN1EncodableVector  (  )  ; 
if  (  !  crls  .  isEmpty  (  )  )  { 
ASN1EncodableVector   v2  =  new   ASN1EncodableVector  (  )  ; 
for  (  Object   element  :  crls  )  { 
ASN1InputStream   t  =  new   ASN1InputStream  (  new   ByteArrayInputStream  (  (  (  X509CRL  )  element  )  .  getEncoded  (  )  )  )  ; 
v2  .  add  (  t  .  readObject  (  )  )  ; 
} 
revocationV  .  add  (  new   DERTaggedObject  (  true  ,  0  ,  new   DERSequence  (  v2  )  )  )  ; 
} 
if  (  ocsp  !=  null  )  { 
DEROctetString   doctet  =  new   DEROctetString  (  ocsp  )  ; 
ASN1EncodableVector   vo1  =  new   ASN1EncodableVector  (  )  ; 
ASN1EncodableVector   v2  =  new   ASN1EncodableVector  (  )  ; 
v2  .  add  (  OCSPObjectIdentifiers  .  id_pkix_ocsp_basic  )  ; 
v2  .  add  (  doctet  )  ; 
ASN1Enumerated   den  =  new   ASN1Enumerated  (  0  )  ; 
ASN1EncodableVector   v3  =  new   ASN1EncodableVector  (  )  ; 
v3  .  add  (  den  )  ; 
v3  .  add  (  new   DERTaggedObject  (  true  ,  0  ,  new   DERSequence  (  v2  )  )  )  ; 
vo1  .  add  (  new   DERSequence  (  v3  )  )  ; 
revocationV  .  add  (  new   DERTaggedObject  (  true  ,  1  ,  new   DERSequence  (  vo1  )  )  )  ; 
} 
v  .  add  (  new   DERSet  (  new   DERSequence  (  revocationV  )  )  )  ; 
attribute  .  add  (  new   DERSequence  (  v  )  )  ; 
} 
return   new   DERSet  (  attribute  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExceptionConverter  (  e  )  ; 
} 
} 





public   String   getReason  (  )  { 
return   this  .  reason  ; 
} 





public   void   setReason  (  String   reason  )  { 
this  .  reason  =  reason  ; 
} 





public   String   getLocation  (  )  { 
return   this  .  location  ; 
} 





public   void   setLocation  (  String   location  )  { 
this  .  location  =  location  ; 
} 





public   Calendar   getSignDate  (  )  { 
return   this  .  signDate  ; 
} 





public   void   setSignDate  (  Calendar   signDate  )  { 
this  .  signDate  =  signDate  ; 
} 





public   String   getSignName  (  )  { 
return   this  .  signName  ; 
} 





public   void   setSignName  (  String   signName  )  { 
this  .  signName  =  signName  ; 
} 




public   static   class   X500Name  { 




public   static   final   ASN1ObjectIdentifier   C  =  new   ASN1ObjectIdentifier  (  "2.5.4.6"  )  ; 




public   static   final   ASN1ObjectIdentifier   O  =  new   ASN1ObjectIdentifier  (  "2.5.4.10"  )  ; 




public   static   final   ASN1ObjectIdentifier   OU  =  new   ASN1ObjectIdentifier  (  "2.5.4.11"  )  ; 




public   static   final   ASN1ObjectIdentifier   T  =  new   ASN1ObjectIdentifier  (  "2.5.4.12"  )  ; 




public   static   final   ASN1ObjectIdentifier   CN  =  new   ASN1ObjectIdentifier  (  "2.5.4.3"  )  ; 




public   static   final   ASN1ObjectIdentifier   SN  =  new   ASN1ObjectIdentifier  (  "2.5.4.5"  )  ; 




public   static   final   ASN1ObjectIdentifier   L  =  new   ASN1ObjectIdentifier  (  "2.5.4.7"  )  ; 




public   static   final   ASN1ObjectIdentifier   ST  =  new   ASN1ObjectIdentifier  (  "2.5.4.8"  )  ; 


public   static   final   ASN1ObjectIdentifier   SURNAME  =  new   ASN1ObjectIdentifier  (  "2.5.4.4"  )  ; 


public   static   final   ASN1ObjectIdentifier   GIVENNAME  =  new   ASN1ObjectIdentifier  (  "2.5.4.42"  )  ; 


public   static   final   ASN1ObjectIdentifier   INITIALS  =  new   ASN1ObjectIdentifier  (  "2.5.4.43"  )  ; 


public   static   final   ASN1ObjectIdentifier   GENERATION  =  new   ASN1ObjectIdentifier  (  "2.5.4.44"  )  ; 


public   static   final   ASN1ObjectIdentifier   UNIQUE_IDENTIFIER  =  new   ASN1ObjectIdentifier  (  "2.5.4.45"  )  ; 





public   static   final   ASN1ObjectIdentifier   EmailAddress  =  new   ASN1ObjectIdentifier  (  "1.2.840.113549.1.9.1"  )  ; 




public   static   final   ASN1ObjectIdentifier   E  =  EmailAddress  ; 


public   static   final   ASN1ObjectIdentifier   DC  =  new   ASN1ObjectIdentifier  (  "0.9.2342.19200300.100.1.25"  )  ; 


public   static   final   ASN1ObjectIdentifier   UID  =  new   ASN1ObjectIdentifier  (  "0.9.2342.19200300.100.1.1"  )  ; 


public   static   HashMap  <  ASN1ObjectIdentifier  ,  String  >  DefaultSymbols  =  new   HashMap  <  ASN1ObjectIdentifier  ,  String  >  (  )  ; 

static  { 
DefaultSymbols  .  put  (  C  ,  "C"  )  ; 
DefaultSymbols  .  put  (  O  ,  "O"  )  ; 
DefaultSymbols  .  put  (  T  ,  "T"  )  ; 
DefaultSymbols  .  put  (  OU  ,  "OU"  )  ; 
DefaultSymbols  .  put  (  CN  ,  "CN"  )  ; 
DefaultSymbols  .  put  (  L  ,  "L"  )  ; 
DefaultSymbols  .  put  (  ST  ,  "ST"  )  ; 
DefaultSymbols  .  put  (  SN  ,  "SN"  )  ; 
DefaultSymbols  .  put  (  EmailAddress  ,  "E"  )  ; 
DefaultSymbols  .  put  (  DC  ,  "DC"  )  ; 
DefaultSymbols  .  put  (  UID  ,  "UID"  )  ; 
DefaultSymbols  .  put  (  SURNAME  ,  "SURNAME"  )  ; 
DefaultSymbols  .  put  (  GIVENNAME  ,  "GIVENNAME"  )  ; 
DefaultSymbols  .  put  (  INITIALS  ,  "INITIALS"  )  ; 
DefaultSymbols  .  put  (  GENERATION  ,  "GENERATION"  )  ; 
} 


public   HashMap  <  String  ,  ArrayList  <  String  >  >  values  =  new   HashMap  <  String  ,  ArrayList  <  String  >  >  (  )  ; 





@  SuppressWarnings  (  "unchecked"  ) 
public   X500Name  (  ASN1Sequence   seq  )  { 
Enumeration  <  ASN1Set  >  e  =  seq  .  getObjects  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
ASN1Set   set  =  e  .  nextElement  (  )  ; 
for  (  int   i  =  0  ;  i  <  set  .  size  (  )  ;  i  ++  )  { 
ASN1Sequence   s  =  (  ASN1Sequence  )  set  .  getObjectAt  (  i  )  ; 
String   id  =  DefaultSymbols  .  get  (  s  .  getObjectAt  (  0  )  )  ; 
if  (  id  ==  null  )  continue  ; 
ArrayList  <  String  >  vs  =  values  .  get  (  id  )  ; 
if  (  vs  ==  null  )  { 
vs  =  new   ArrayList  <  String  >  (  )  ; 
values  .  put  (  id  ,  vs  )  ; 
} 
vs  .  add  (  (  (  ASN1String  )  s  .  getObjectAt  (  1  )  )  .  getString  (  )  )  ; 
} 
} 
} 





public   X500Name  (  String   dirName  )  { 
X509NameTokenizer   nTok  =  new   X509NameTokenizer  (  dirName  )  ; 
while  (  nTok  .  hasMoreTokens  (  )  )  { 
String   token  =  nTok  .  nextToken  (  )  ; 
int   index  =  token  .  indexOf  (  '='  )  ; 
if  (  index  ==  -  1  )  { 
throw   new   IllegalArgumentException  (  MessageLocalization  .  getComposedMessage  (  "badly.formated.directory.string"  )  )  ; 
} 
String   id  =  token  .  substring  (  0  ,  index  )  .  toUpperCase  (  )  ; 
String   value  =  token  .  substring  (  index  +  1  )  ; 
ArrayList  <  String  >  vs  =  values  .  get  (  id  )  ; 
if  (  vs  ==  null  )  { 
vs  =  new   ArrayList  <  String  >  (  )  ; 
values  .  put  (  id  ,  vs  )  ; 
} 
vs  .  add  (  value  )  ; 
} 
} 

public   String   getField  (  String   name  )  { 
ArrayList  <  String  >  vs  =  values  .  get  (  name  )  ; 
return   vs  ==  null  ?  null  :  (  String  )  vs  .  get  (  0  )  ; 
} 






public   ArrayList  <  String  >  getFieldArray  (  String   name  )  { 
ArrayList  <  String  >  vs  =  values  .  get  (  name  )  ; 
return   vs  ==  null  ?  null  :  vs  ; 
} 





public   HashMap  <  String  ,  ArrayList  <  String  >  >  getFields  (  )  { 
return   values  ; 
} 




@  Override 
public   String   toString  (  )  { 
return   values  .  toString  (  )  ; 
} 
} 







public   static   class   X509NameTokenizer  { 

private   String   oid  ; 

private   int   index  ; 

private   StringBuffer   buf  =  new   StringBuffer  (  )  ; 

public   X509NameTokenizer  (  String   oid  )  { 
this  .  oid  =  oid  ; 
this  .  index  =  -  1  ; 
} 

public   boolean   hasMoreTokens  (  )  { 
return   index  !=  oid  .  length  (  )  ; 
} 

public   String   nextToken  (  )  { 
if  (  index  ==  oid  .  length  (  )  )  { 
return   null  ; 
} 
int   end  =  index  +  1  ; 
boolean   quoted  =  false  ; 
boolean   escaped  =  false  ; 
buf  .  setLength  (  0  )  ; 
while  (  end  !=  oid  .  length  (  )  )  { 
char   c  =  oid  .  charAt  (  end  )  ; 
if  (  c  ==  '"'  )  { 
if  (  !  escaped  )  { 
quoted  =  !  quoted  ; 
}  else  { 
buf  .  append  (  c  )  ; 
} 
escaped  =  false  ; 
}  else  { 
if  (  escaped  ||  quoted  )  { 
buf  .  append  (  c  )  ; 
escaped  =  false  ; 
}  else   if  (  c  ==  '\\'  )  { 
escaped  =  true  ; 
}  else   if  (  c  ==  ','  )  { 
break  ; 
}  else  { 
buf  .  append  (  c  )  ; 
} 
} 
end  ++  ; 
} 
index  =  end  ; 
return   buf  .  toString  (  )  .  trim  (  )  ; 
} 
} 
} 

