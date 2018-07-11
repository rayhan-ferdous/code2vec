package   uk  .  ac  .  ebi  .  pride  .  gui  .  utils  ; 

import   org  .  jdom  .  Document  ; 
import   org  .  jdom  .  Element  ; 
import   org  .  jdom  .  input  .  SAXBuilder  ; 
import   org  .  slf4j  .  Logger  ; 
import   org  .  slf4j  .  LoggerFactory  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  regex  .  Matcher  ; 
import   java  .  util  .  regex  .  Pattern  ; 

public   class   ProteinNameFetcher  { 

private   Logger   logger  =  LoggerFactory  .  getLogger  (  ProteinNameFetcher  .  class  )  ; 

private   final   String   ESUMMARY_QUERY_STRING  =  "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=protein&id=%s"  ; 

private   final   String   ESEARCH_QUERY_STRING  =  "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=protein&term=%s"  ; 

private   final   String   UNIPROT_ACC_QUERY_STRING  =  "http://www.uniprot.org/uniprot/?query=accession:%s&format=tab&columns=protein%%20names"  ; 

private   final   String   UNIPROT_ENTRY_NAME_QUERY_STRING  =  "http://www.uniprot.org/uniprot/?query=mnemonic:%s&format=tab&columns=protein%%20names"  ; 

private   final   String   UNIPROT_ENTRY_QUERY_STRING  =  "http://www.uniprot.org/uniprot/%s"  ; 

private   final   String   UNIPROT_ACC_CONVERSION_QUERY_STRING  =  "http://www.uniprot.org/uniprot/?query=%s&format=tab&columns=id,reviewed"  ; 

private   final   String   UNIPROT_FOREIGN_NAME_QUERY_STRING  =  "http://www.uniprot.org/uniprot/?query=%s&format=tab&columns=protein%%20names,reviewed"  ; 

private   final   String   IPI_FASTA_QUERY_STRING  =  "http://www.ebi.ac.uk/cgi-bin/dbfetch?db=IPI&id=%s&format=fasta&style=raw"  ; 

private   HashMap  <  String  ,  String  >  pageBuffer  =  new   HashMap  <  String  ,  String  >  (  )  ; 








public   String   getProteinName  (  String   accession  )  throws   Exception  { 
if  (  ProteinAccessionPattern  .  isSwissprotAccession  (  accession  )  ||  ProteinAccessionPattern  .  isSwissprotEntryName  (  accession  )  )  { 
String  [  ]  parts  =  accession  .  split  (  "-"  )  ; 
return   getUniprotName  (  parts  [  0  ]  )  ; 
} 
if  (  ProteinAccessionPattern  .  isUniparcAccession  (  accession  )  )  { 
return   getUniprotName  (  accession  )  ; 
} 
if  (  ProteinAccessionPattern  .  isIPIAccession  (  accession  )  )  { 
return   getIpiName  (  accession  )  ; 
} 
if  (  ProteinAccessionPattern  .  isEnsemblAccession  (  accession  )  )  { 
return   getEnsemblName  (  accession  )  ; 
} 
if  (  ProteinAccessionPattern  .  isRefseqAccession  (  accession  )  )  { 
String   ncbiId  =  getNcbiId  (  accession  )  ; 
return   getNcbiName  (  ncbiId  )  ; 
} 
if  (  ProteinAccessionPattern  .  isGIAccession  (  accession  )  )  { 
return   getNcbiName  (  accession  )  ; 
} 
return   null  ; 
} 








private   String   getIpiName  (  String   accession  )  throws   Exception  { 
if  (  !  accession  .  startsWith  (  "IPI"  )  )  throw   new   Exception  (  "Malformatted IPI accession"  )  ; 
String   fasta  =  getPage  (  String  .  format  (  IPI_FASTA_QUERY_STRING  ,  accession  )  )  ; 
fasta  =  fasta  .  substring  (  0  ,  fasta  .  indexOf  (  '\n'  )  )  ; 
Pattern   pat  =  Pattern  .  compile  (  "IPI[\\d\\.]+ (.*)$"  )  ; 
Matcher   matcher  =  pat  .  matcher  (  fasta  )  ; 
if  (  !  matcher  .  find  (  )  )  throw   new   Exception  (  "Unexpected fasta format encountered"  )  ; 
return   matcher  .  group  (  1  )  ; 
} 










private   String   getEnsemblName  (  String   accession  )  throws   Exception  { 
if  (  !  accession  .  startsWith  (  "ENS"  )  )  throw   new   Exception  (  "Malformatted ENSEMBL accession"  )  ; 
return   getForeignUniprotName  (  accession  )  ; 
} 












private   String   getForeignUniprotName  (  String   accession  )  throws   Exception  { 
String   page  =  getPage  (  String  .  format  (  UNIPROT_FOREIGN_NAME_QUERY_STRING  ,  accession  )  )  ; 
String  [  ]  lines  =  page  .  split  (  "\n"  )  ; 
if  (  page  .  equals  (  ""  )  ||  lines  .  length  <  2  )  throw   new   Exception  (  "No UniProt accession available for "  +  accession  )  ; 
ArrayList  <  String  >  swissprot  =  new   ArrayList  <  String  >  (  )  ; 
ArrayList  <  String  >  trembl  =  new   ArrayList  <  String  >  (  )  ; 
for  (  int   i  =  1  ;  i  <  lines  .  length  ;  i  ++  )  { 
if  (  lines  [  i  ]  .  contains  (  "unreviewed"  )  )  trembl  .  add  (  lines  [  i  ]  .  substring  (  0  ,  lines  [  i  ]  .  indexOf  (  '\t'  )  )  )  ;  else   swissprot  .  add  (  lines  [  i  ]  .  substring  (  0  ,  lines  [  i  ]  .  indexOf  (  '\t'  )  )  )  ; 
} 
if  (  swissprot  .  size  (  )  >  0  )  return   swissprot  .  get  (  0  )  ;  else   return   trembl  .  get  (  0  )  ; 
} 











private   String   convertToUniprotAccession  (  String   accession  )  throws   Exception  { 
String   page  =  getPage  (  String  .  format  (  UNIPROT_ACC_CONVERSION_QUERY_STRING  ,  accession  )  )  ; 
String  [  ]  lines  =  page  .  split  (  "\n"  )  ; 
if  (  page  .  equals  (  ""  )  ||  lines  .  length  <  2  )  throw   new   Exception  (  "No UniProt accession available for "  +  accession  )  ; 
ArrayList  <  String  >  swissprot  =  new   ArrayList  <  String  >  (  )  ; 
ArrayList  <  String  >  trembl  =  new   ArrayList  <  String  >  (  )  ; 
for  (  int   i  =  1  ;  i  <  lines  .  length  ;  i  ++  )  { 
if  (  lines  [  i  ]  .  contains  (  "unreviewed"  )  )  trembl  .  add  (  lines  [  i  ]  .  substring  (  0  ,  lines  [  i  ]  .  indexOf  (  '\t'  )  )  )  ;  else   swissprot  .  add  (  lines  [  i  ]  .  substring  (  0  ,  lines  [  i  ]  .  indexOf  (  '\t'  )  )  )  ; 
} 
if  (  swissprot  .  size  (  )  >  0  )  return   swissprot  .  get  (  0  )  ;  else   return   trembl  .  get  (  0  )  ; 
} 










private   String   getUniprotName  (  String   accession  )  throws   Exception  { 
String   page  =  getPage  (  String  .  format  (  UNIPROT_ACC_QUERY_STRING  ,  accession  )  )  ; 
if  (  ""  .  equals  (  page  .  trim  (  )  )  )  { 
page  =  getPage  (  String  .  format  (  UNIPROT_ENTRY_NAME_QUERY_STRING  ,  accession  )  )  ; 
} 
String  [  ]  lines  =  page  .  split  (  "\n"  )  ; 
if  (  page  .  equals  (  ""  )  ||  lines  .  length  <  2  )  { 
return   null  ; 
} 
return   lines  [  1  ]  .  trim  (  )  ; 
} 









private   String   getCurrentUniprotAccession  (  String   accession  )  throws   Exception  { 
URL   url  =  new   URL  (  String  .  format  (  UNIPROT_ENTRY_QUERY_STRING  ,  accession  )  )  ; 
HttpURLConnection   connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
HttpURLConnection  .  setFollowRedirects  (  true  )  ; 
connection  .  setRequestMethod  (  "HEAD"  )  ; 
connection  .  connect  (  )  ; 
if  (  connection  .  getResponseCode  (  )  !=  200  )  { 
logger  .  debug  (  "{} seems to be no UniProt accession"  ,  accession  )  ; 
throw   new   Exception  (  "Missing UniProt entry for "  +  accession  )  ; 
} 
String   effectiveUrl  =  connection  .  getURL  (  )  .  toString  (  )  ; 
String   confirmedAccession  =  effectiveUrl  .  substring  (  effectiveUrl  .  lastIndexOf  (  '/'  )  +  1  )  ; 
logger  .  debug  (  "getCurrentUniprotAccession: {} -> {}"  ,  new   Object  [  ]  {  accession  ,  confirmedAccession  }  )  ; 
return   confirmedAccession  ; 
} 








private   String   getNcbiName  (  String   accession  )  throws   Exception  { 
String   ncbiId  =  getNcbiId  (  accession  )  ; 
if  (  ncbiId  ==  null  )  throw   new   Exception  (  accession  +  " is not a valid NCBI accession"  )  ; 
HashMap  <  String  ,  String  >  properties  =  getNcbiProperties  (  ncbiId  )  ; 
String   name  =  properties  .  get  (  "Title"  )  ; 
if  (  name  .  contains  (  "AltName"  )  ||  name  .  contains  (  "Short"  )  )  name  +=  ")"  ; 
name  =  name  .  replace  (  "RecName: "  ,  ""  )  ; 
name  =  name  .  replace  (  "Full="  ,  ""  )  ; 
name  =  name  .  replace  (  "AltName: "  ,  "("  )  ; 
name  =  name  .  replace  (  "; Short="  ,  ", "  )  ; 
name  =  name  .  replace  (  ";"  ,  ")"  )  ; 
if  (  name  .  contains  (  ")"  )  )  name  =  name  .  substring  (  0  ,  name  .  indexOf  (  ')'  )  )  +  name  .  substring  (  name  .  indexOf  (  ')'  )  +  1  )  ; 
return   name  ; 
} 











private   String   getNcbiId  (  String   accession  )  throws   Exception  { 
URL   url  =  new   URL  (  String  .  format  (  ESEARCH_QUERY_STRING  ,  accession  )  )  ; 
SAXBuilder   builder  =  new   SAXBuilder  (  )  ; 
Document   doc  =  builder  .  build  (  url  )  ; 
Element   root  =  doc  .  getRootElement  (  )  ; 
if  (  root  ==  null  )  throw   new   Exception  (  "Failed to retrieve NCBI search result for "  +  accession  )  ; 
Element   idList  =  root  .  getChild  (  "IdList"  )  ; 
if  (  idList  ==  null  )  throw   new   Exception  (  "Failed to retrieve id list for "  +  accession  )  ; 
List  <  Element  >  idElements  =  idList  .  getChildren  (  "Id"  )  ; 
String   ids  [  ]  =  new   String  [  idElements  .  size  (  )  ]  ; 
int   counter  =  0  ; 
for  (  Element   idElement  :  idElements  )  { 
ids  [  counter  ++  ]  =  idElement  .  getValue  (  )  ; 
} 
if  (  ids  .  length  <  1  )  return   null  ; 
if  (  ids  .  length  >  1  )  logger  .  warn  (  "More than one id found for {} ({})"  ,  new   Object  [  ]  {  accession  ,  ids  .  toString  (  )  }  )  ; 
return   ids  [  0  ]  ; 
} 









private   HashMap  <  String  ,  String  >  getNcbiProperties  (  String   ncbiId  )  throws   Exception  { 
URL   url  =  new   URL  (  String  .  format  (  ESUMMARY_QUERY_STRING  ,  ncbiId  )  )  ; 
SAXBuilder   builder  =  new   SAXBuilder  (  )  ; 
Document   doc  =  builder  .  build  (  url  )  ; 
Element   root  =  doc  .  getRootElement  (  )  ; 
if  (  root  ==  null  )  throw   new   Exception  (  "Failed to retrieve xml summary for "  +  ncbiId  )  ; 
Element   docSum  =  root  .  getChild  (  "DocSum"  )  ; 
if  (  docSum  ==  null  )  throw   new   Exception  (  "Failed to retrieve xml summary for "  +  ncbiId  )  ; 
List  <  Element  >  items  =  docSum  .  getChildren  (  "Item"  )  ; 
HashMap  <  String  ,  String  >  properties  =  new   HashMap  <  String  ,  String  >  (  )  ; 
for  (  Element   item  :  items  )  { 
properties  .  put  (  item  .  getAttributeValue  (  "Name"  )  ,  item  .  getValue  (  )  )  ; 
} 
return   properties  ; 
} 









private   String   getPage  (  String   urlString  )  throws   Exception  { 
if  (  pageBuffer  .  containsKey  (  urlString  )  )  return   pageBuffer  .  get  (  urlString  )  ; 
URL   url  =  new   URL  (  urlString  )  ; 
HttpURLConnection   connection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
connection  .  connect  (  )  ; 
BufferedReader   in  =  null  ; 
StringBuilder   page  =  new   StringBuilder  (  )  ; 
try  { 
in  =  new   BufferedReader  (  new   InputStreamReader  (  connection  .  getInputStream  (  )  )  )  ; 
String   line  ; 
while  (  (  line  =  in  .  readLine  (  )  )  !=  null  )  { 
page  .  append  (  line  )  ; 
page  .  append  (  "\n"  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
logger  .  warn  (  "Failed to read web page"  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
} 
return   page  .  toString  (  )  ; 
} 
} 

