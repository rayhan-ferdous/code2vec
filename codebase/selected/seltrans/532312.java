package   it  .  trento  .  comune  .  j4sign  .  cms  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  security  .  InvalidKeyException  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  NoSuchProviderException  ; 
import   java  .  security  .  SignatureException  ; 
import   java  .  security  .  cert  .  CertificateEncodingException  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Iterator  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1EncodableVector  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1InputStream  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1OctetString  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1Sequence  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1Set  ; 
import   org  .  bouncycastle  .  asn1  .  DEREncodableVector  ; 
import   org  .  bouncycastle  .  asn1  .  DERInteger  ; 
import   org  .  bouncycastle  .  asn1  .  DERNull  ; 
import   org  .  bouncycastle  .  asn1  .  DERObject  ; 
import   org  .  bouncycastle  .  asn1  .  DERObjectIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  DEROctetString  ; 
import   org  .  bouncycastle  .  asn1  .  DEROutputStream  ; 
import   org  .  bouncycastle  .  asn1  .  DERSet  ; 
import   org  .  bouncycastle  .  asn1  .  DERUTCTime  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  Attribute  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  AttributeTable  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  CMSAttributes  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  IssuerAndSerialNumber  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  SignerIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  SignerInfo  ; 
import   org  .  bouncycastle  .  asn1  .  cms  .  Time  ; 
import   org  .  bouncycastle  .  asn1  .  ess  .  ESSCertIDv2  ; 
import   org  .  bouncycastle  .  asn1  .  ess  .  SigningCertificateV2  ; 
import   org  .  bouncycastle  .  asn1  .  pkcs  .  PKCSObjectIdentifiers  ; 
import   org  .  bouncycastle  .  asn1  .  x500  .  X500Name  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  AlgorithmIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralName  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralNames  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  IssuerSerial  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  TBSCertificateStructure  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  X509CertificateStructure  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  X509Name  ; 
import   org  .  bouncycastle  .  cert  .  jcajce  .  JcaX509CertificateHolder  ; 
import   org  .  bouncycastle  .  cms  .  CMSException  ; 
import   org  .  bouncycastle  .  cms  .  CMSProcessable  ; 
import   org  .  bouncycastle  .  cms  .  CMSSignedDataGenerator  ; 






















public   class   ExternalSignatureSignerInfoGenerator  { 






X509Certificate   cert  ; 







byte  [  ]  signedBytes  ; 




String   digestOID  ; 




String   encOID  ; 






AttributeTable   sAttr  =  null  ; 





AttributeTable   unsAttr  =  null  ; 







ASN1Set   signedAttr  =  null  ; 







ASN1Set   unsignedAttr  =  null  ; 







static   class   DigOutputStream   extends   OutputStream  { 

MessageDigest   dig  ; 

public   DigOutputStream  (  MessageDigest   dig  )  { 
this  .  dig  =  dig  ; 
} 

public   void   write  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
dig  .  update  (  b  ,  off  ,  len  )  ; 
} 

public   void   write  (  int   b  )  throws   IOException  { 
dig  .  update  (  (  byte  )  b  )  ; 
} 
} 









public   ExternalSignatureSignerInfoGenerator  (  String   digestOID  ,  String   encOID  )  { 
this  .  cert  =  null  ; 
this  .  digestOID  =  digestOID  ; 
this  .  encOID  =  encOID  ; 
} 






public   X509Certificate   getCertificate  (  )  { 
return   cert  ; 
} 








public   void   setCertificate  (  X509Certificate   c  )  { 
cert  =  c  ; 
} 




String   getDigestAlgOID  (  )  { 
return   digestOID  ; 
} 




byte  [  ]  getDigestAlgParams  (  )  { 
return   null  ; 
} 




String   getEncryptionAlgOID  (  )  { 
return   encOID  ; 
} 




AttributeTable   getSignedAttributes  (  )  { 
return   sAttr  ; 
} 




AttributeTable   getUnsignedAttributes  (  )  { 
return   unsAttr  ; 
} 





String   getDigestAlgName  (  )  { 
String   digestAlgOID  =  this  .  getDigestAlgOID  (  )  ; 
if  (  CMSSignedDataGenerator  .  DIGEST_MD5  .  equals  (  digestAlgOID  )  )  { 
return  "MD5"  ; 
}  else   if  (  CMSSignedDataGenerator  .  DIGEST_SHA1  .  equals  (  digestAlgOID  )  )  { 
return  "SHA1"  ; 
}  else   if  (  CMSSignedDataGenerator  .  DIGEST_SHA224  .  equals  (  digestAlgOID  )  )  { 
return  "SHA224"  ; 
}  else  { 
return   digestAlgOID  ; 
} 
} 





String   getEncryptionAlgName  (  )  { 
String   encryptionAlgOID  =  this  .  getEncryptionAlgOID  (  )  ; 
if  (  CMSSignedDataGenerator  .  ENCRYPTION_DSA  .  equals  (  encryptionAlgOID  )  )  { 
return  "DSA"  ; 
}  else   if  (  CMSSignedDataGenerator  .  ENCRYPTION_RSA  .  equals  (  encryptionAlgOID  )  )  { 
return  "RSA"  ; 
}  else  { 
return   encryptionAlgOID  ; 
} 
} 











SignerInfo   generate  (  )  throws   CertificateEncodingException  ,  IOException  { 
AlgorithmIdentifier   digAlgId  =  null  ; 
AlgorithmIdentifier   encAlgId  =  null  ; 
digAlgId  =  new   AlgorithmIdentifier  (  new   DERObjectIdentifier  (  this  .  getDigestAlgOID  (  )  )  ,  new   DERNull  (  )  )  ; 
if  (  this  .  getEncryptionAlgOID  (  )  .  equals  (  CMSSignedDataGenerator  .  ENCRYPTION_DSA  )  )  { 
encAlgId  =  new   AlgorithmIdentifier  (  new   DERObjectIdentifier  (  this  .  getEncryptionAlgOID  (  )  )  )  ; 
}  else  { 
encAlgId  =  new   AlgorithmIdentifier  (  new   DERObjectIdentifier  (  this  .  getEncryptionAlgOID  (  )  )  ,  new   DERNull  (  )  )  ; 
} 
ASN1OctetString   encDigest  =  new   DEROctetString  (  this  .  signedBytes  )  ; 
X509Certificate   cert  =  this  .  getCertificate  (  )  ; 
ByteArrayInputStream   bIn  =  new   ByteArrayInputStream  (  cert  .  getTBSCertificate  (  )  )  ; 
ASN1InputStream   aIn  =  new   ASN1InputStream  (  bIn  )  ; 
TBSCertificateStructure   tbs  =  TBSCertificateStructure  .  getInstance  (  aIn  .  readObject  (  )  )  ; 
IssuerAndSerialNumber   encSid  =  new   IssuerAndSerialNumber  (  tbs  .  getIssuer  (  )  ,  cert  .  getSerialNumber  (  )  )  ; 
return   new   SignerInfo  (  new   SignerIdentifier  (  encSid  )  ,  digAlgId  ,  signedAttr  ,  encAlgId  ,  encDigest  ,  unsignedAttr  )  ; 
} 








































public   byte  [  ]  getBytesToSign  (  DERObjectIdentifier   contentType  ,  CMSProcessable   content  ,  String   sigProvider  )  throws   IOException  ,  SignatureException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  NoSuchAlgorithmException  ,  CertificateEncodingException  ,  CMSException  { 
MessageDigest   dig  =  MessageDigest  .  getInstance  (  this  .  getDigestAlgOID  (  )  ,  sigProvider  )  ; 
content  .  write  (  new   DigOutputStream  (  dig  )  )  ; 
byte  [  ]  hash  =  dig  .  digest  (  )  ; 
AttributeTable   attr  =  this  .  getSignedAttributes  (  )  ; 
if  (  attr  !=  null  )  { 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
if  (  attr  .  get  (  CMSAttributes  .  contentType  )  ==  null  )  { 
v  .  add  (  new   Attribute  (  CMSAttributes  .  contentType  ,  new   DERSet  (  contentType  )  )  )  ; 
}  else  { 
v  .  add  (  attr  .  get  (  CMSAttributes  .  contentType  )  )  ; 
} 
if  (  attr  .  get  (  CMSAttributes  .  signingTime  )  ==  null  )  { 
v  .  add  (  new   Attribute  (  CMSAttributes  .  signingTime  ,  new   DERSet  (  new   Time  (  new   Date  (  )  )  )  )  )  ; 
}  else  { 
v  .  add  (  attr  .  get  (  CMSAttributes  .  signingTime  )  )  ; 
} 
v  .  add  (  new   Attribute  (  CMSAttributes  .  messageDigest  ,  new   DERSet  (  new   DEROctetString  (  hash  )  )  )  )  ; 
v  .  add  (  buildSigningCertificateV2Attribute  (  sigProvider  )  )  ; 
Hashtable   ats  =  attr  .  toHashtable  (  )  ; 
ats  .  remove  (  CMSAttributes  .  contentType  )  ; 
ats  .  remove  (  CMSAttributes  .  signingTime  )  ; 
ats  .  remove  (  CMSAttributes  .  messageDigest  )  ; 
ats  .  remove  (  PKCSObjectIdentifiers  .  id_aa_signingCertificateV2  )  ; 
Iterator   it  =  ats  .  values  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
v  .  add  (  Attribute  .  getInstance  (  it  .  next  (  )  )  )  ; 
} 
signedAttr  =  new   DERSet  (  v  )  ; 
}  else  { 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
v  .  add  (  new   Attribute  (  CMSAttributes  .  contentType  ,  new   DERSet  (  contentType  )  )  )  ; 
v  .  add  (  new   Attribute  (  CMSAttributes  .  signingTime  ,  new   DERSet  (  new   DERUTCTime  (  new   Date  (  )  )  )  )  )  ; 
v  .  add  (  new   Attribute  (  CMSAttributes  .  messageDigest  ,  new   DERSet  (  new   DEROctetString  (  hash  )  )  )  )  ; 
v  .  add  (  buildSigningCertificateV2Attribute  (  sigProvider  )  )  ; 
signedAttr  =  new   DERSet  (  v  )  ; 
} 
attr  =  this  .  getUnsignedAttributes  (  )  ; 
if  (  attr  !=  null  )  { 
Hashtable   ats  =  attr  .  toHashtable  (  )  ; 
Iterator   it  =  ats  .  values  (  )  .  iterator  (  )  ; 
ASN1EncodableVector   v  =  new   ASN1EncodableVector  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
v  .  add  (  Attribute  .  getInstance  (  it  .  next  (  )  )  )  ; 
} 
unsignedAttr  =  new   DERSet  (  v  )  ; 
} 
ByteArrayOutputStream   bOut  =  new   ByteArrayOutputStream  (  )  ; 
DEROutputStream   dOut  =  new   DEROutputStream  (  bOut  )  ; 
dOut  .  writeObject  (  signedAttr  )  ; 
return   bOut  .  toByteArray  (  )  ; 
} 

















private   Attribute   buildSigningCertificateV2Attribute  (  String   sigProvider  )  throws   NoSuchAlgorithmException  ,  NoSuchProviderException  ,  CertificateEncodingException  ,  IOException  { 
X509Certificate   cert  =  this  .  getCertificate  (  )  ; 
MessageDigest   dig  =  MessageDigest  .  getInstance  (  this  .  getDigestAlgOID  (  )  ,  sigProvider  )  ; 
byte  [  ]  certHash  =  dig  .  digest  (  cert  .  getEncoded  (  )  )  ; 
JcaX509CertificateHolder   holder  =  new   JcaX509CertificateHolder  (  cert  )  ; 
X500Name   x500name  =  holder  .  getIssuer  (  )  ; 
GeneralName   generalName  =  new   GeneralName  (  x500name  )  ; 
GeneralNames   generalNames  =  new   GeneralNames  (  generalName  )  ; 
DERInteger   serialNum  =  new   DERInteger  (  holder  .  getSerialNumber  (  )  )  ; 
IssuerSerial   issuerserial  =  new   IssuerSerial  (  generalNames  ,  serialNum  )  ; 
ESSCertIDv2   essCert  =  new   ESSCertIDv2  (  new   AlgorithmIdentifier  (  getDigestAlgOID  (  )  )  ,  certHash  ,  issuerserial  )  ; 
SigningCertificateV2   scv2  =  new   SigningCertificateV2  (  new   ESSCertIDv2  [  ]  {  essCert  }  )  ; 
return   new   Attribute  (  PKCSObjectIdentifiers  .  id_aa_signingCertificateV2  ,  new   DERSet  (  scv2  )  )  ; 
} 





public   void   setSignedBytes  (  byte  [  ]  signedBytes  )  { 
this  .  signedBytes  =  signedBytes  ; 
} 
} 

