package   org  .  jgap  .  xml  ; 

import   java  .  io  .  *  ; 
import   java  .  lang  .  reflect  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  xml  .  parsers  .  *  ; 
import   javax  .  xml  .  transform  .  *  ; 
import   javax  .  xml  .  transform  .  dom  .  *  ; 
import   javax  .  xml  .  transform  .  stream  .  *  ; 
import   junitx  .  util  .  PrivateAccessor  ; 
import   org  .  jgap  .  *  ; 
import   org  .  w3c  .  dom  .  *  ; 











public   class   XMLManager  { 


private   static   final   String   CVS_REVISION  =  "$Revision: 1.21 $"  ; 




private   static   final   String   GENOTYPE_TAG  =  "genotype"  ; 




private   static   final   String   CHROMOSOME_TAG  =  "chromosome"  ; 




private   static   final   String   GENES_TAG  =  "genes"  ; 




private   static   final   String   GENE_TAG  =  "gene"  ; 

private   static   final   String   ALLELE_TAG  =  "allele"  ; 





private   static   final   String   SIZE_ATTRIBUTE  =  "size"  ; 





private   static   final   String   CLASS_ATTRIBUTE  =  "class"  ; 





private   static   final   DocumentBuilder   m_documentCreator  ; 




private   static   final   Object   m_lock  =  new   Object  (  )  ; 





static  { 
try  { 
m_documentCreator  =  DocumentBuilderFactory  .  newInstance  (  )  .  newDocumentBuilder  (  )  ; 
}  catch  (  ParserConfigurationException   parserError  )  { 
throw   new   RuntimeException  (  "XMLManager: Unable to setup DocumentBuilder: "  +  parserError  .  getMessage  (  )  )  ; 
} 
} 





private   XMLManager  (  )  { 
} 












public   static   Document   representChromosomeAsDocument  (  final   IChromosome   a_subject  )  { 
Document   chromosomeDocument  ; 
synchronized  (  m_lock  )  { 
chromosomeDocument  =  m_documentCreator  .  newDocument  (  )  ; 
} 
Element   chromosomeElement  =  representChromosomeAsElement  (  a_subject  ,  chromosomeDocument  )  ; 
chromosomeDocument  .  appendChild  (  chromosomeElement  )  ; 
return   chromosomeDocument  ; 
} 












public   static   Document   representGenotypeAsDocument  (  final   Genotype   a_subject  )  { 
Document   genotypeDocument  ; 
synchronized  (  m_lock  )  { 
genotypeDocument  =  m_documentCreator  .  newDocument  (  )  ; 
} 
Element   genotypeElement  =  representGenotypeAsElement  (  a_subject  ,  genotypeDocument  )  ; 
genotypeDocument  .  appendChild  (  genotypeElement  )  ; 
return   genotypeDocument  ; 
} 















public   static   Element   representGenesAsElement  (  final   Gene  [  ]  a_geneValues  ,  final   Document   a_xmlDocument  )  { 
Element   genesElement  =  a_xmlDocument  .  createElement  (  GENES_TAG  )  ; 
Element   geneElement  ; 
for  (  int   i  =  0  ;  i  <  a_geneValues  .  length  ;  i  ++  )  { 
geneElement  =  a_xmlDocument  .  createElement  (  GENE_TAG  )  ; 
geneElement  .  setAttribute  (  CLASS_ATTRIBUTE  ,  a_geneValues  [  i  ]  .  getClass  (  )  .  getName  (  )  )  ; 
Element   alleleRepresentation  =  representAlleleAsElement  (  a_geneValues  [  i  ]  ,  a_xmlDocument  )  ; 
geneElement  .  appendChild  (  alleleRepresentation  )  ; 
genesElement  .  appendChild  (  geneElement  )  ; 
} 
return   genesElement  ; 
} 










private   static   Element   representAlleleAsElement  (  final   Gene   a_gene  ,  final   Document   a_xmlDocument  )  { 
Element   alleleElement  =  a_xmlDocument  .  createElement  (  ALLELE_TAG  )  ; 
alleleElement  .  setAttribute  (  "class"  ,  a_gene  .  getClass  (  )  .  getName  (  )  )  ; 
alleleElement  .  setAttribute  (  "value"  ,  a_gene  .  getPersistentRepresentation  (  )  )  ; 
return   alleleElement  ; 
} 


















public   static   Element   representChromosomeAsElement  (  final   IChromosome   a_subject  ,  final   Document   a_xmlDocument  )  { 
Element   chromosomeElement  =  a_xmlDocument  .  createElement  (  CHROMOSOME_TAG  )  ; 
chromosomeElement  .  setAttribute  (  SIZE_ATTRIBUTE  ,  Integer  .  toString  (  a_subject  .  size  (  )  )  )  ; 
Element   genesElement  =  representGenesAsElement  (  a_subject  .  getGenes  (  )  ,  a_xmlDocument  )  ; 
chromosomeElement  .  appendChild  (  genesElement  )  ; 
return   chromosomeElement  ; 
} 



















public   static   Element   representGenotypeAsElement  (  final   Genotype   a_subject  ,  final   Document   a_xmlDocument  )  { 
Population   population  =  a_subject  .  getPopulation  (  )  ; 
Element   genotypeTag  =  a_xmlDocument  .  createElement  (  GENOTYPE_TAG  )  ; 
genotypeTag  .  setAttribute  (  SIZE_ATTRIBUTE  ,  Integer  .  toString  (  population  .  size  (  )  )  )  ; 
for  (  int   i  =  0  ;  i  <  population  .  size  (  )  ;  i  ++  )  { 
Element   chromosomeElement  =  representChromosomeAsElement  (  population  .  getChromosome  (  i  )  ,  a_xmlDocument  )  ; 
genotypeTag  .  appendChild  (  chromosomeElement  )  ; 
} 
return   genotypeTag  ; 
} 





















public   static   Gene  [  ]  getGenesFromElement  (  Configuration   a_activeConfiguration  ,  Element   a_xmlElement  )  throws   ImproperXMLException  ,  UnsupportedRepresentationException  ,  GeneCreationException  { 
if  (  a_xmlElement  ==  null  ||  !  (  a_xmlElement  .  getTagName  (  )  .  equals  (  GENES_TAG  )  )  )  { 
throw   new   ImproperXMLException  (  "Unable to build Chromosome instance from XML Element: "  +  "given Element is not a 'genes' element."  )  ; 
} 
List   genes  =  Collections  .  synchronizedList  (  new   ArrayList  (  )  )  ; 
NodeList   geneElements  =  a_xmlElement  .  getElementsByTagName  (  GENE_TAG  )  ; 
if  (  geneElements  ==  null  )  { 
throw   new   ImproperXMLException  (  "Unable to build Gene instances from XML Element: "  +  "'"  +  GENE_TAG  +  "'"  +  " sub-elements not found."  )  ; 
} 
int   numberOfGeneNodes  =  geneElements  .  getLength  (  )  ; 
for  (  int   i  =  0  ;  i  <  numberOfGeneNodes  ;  i  ++  )  { 
Element   thisGeneElement  =  (  Element  )  geneElements  .  item  (  i  )  ; 
thisGeneElement  .  normalize  (  )  ; 
String   geneClassName  =  thisGeneElement  .  getAttribute  (  CLASS_ATTRIBUTE  )  ; 
Gene   thisGeneObject  ; 
Class   geneClass  =  null  ; 
try  { 
geneClass  =  Class  .  forName  (  geneClassName  )  ; 
try  { 
Constructor   constr  =  geneClass  .  getConstructor  (  new   Class  [  ]  {  Configuration  .  class  }  )  ; 
thisGeneObject  =  (  Gene  )  constr  .  newInstance  (  new   Object  [  ]  {  a_activeConfiguration  }  )  ; 
}  catch  (  NoSuchMethodException   nsme  )  { 
Constructor   constr  =  geneClass  .  getConstructor  (  new   Class  [  ]  {  }  )  ; 
thisGeneObject  =  (  Gene  )  constr  .  newInstance  (  new   Object  [  ]  {  }  )  ; 
thisGeneObject  =  (  Gene  )  PrivateAccessor  .  invoke  (  thisGeneObject  ,  "newGeneInternal"  ,  new   Class  [  ]  {  }  ,  new   Object  [  ]  {  }  )  ; 
} 
}  catch  (  Throwable   e  )  { 
throw   new   GeneCreationException  (  geneClass  ,  e  )  ; 
} 
NodeList   children  =  thisGeneElement  .  getChildNodes  (  )  ; 
int   childrenSize  =  children  .  getLength  (  )  ; 
String   alleleRepresentation  =  null  ; 
for  (  int   j  =  0  ;  j  <  childrenSize  ;  j  ++  )  { 
Element   alleleElem  =  (  Element  )  children  .  item  (  j  )  ; 
if  (  alleleElem  .  getTagName  (  )  .  equals  (  ALLELE_TAG  )  )  { 
alleleRepresentation  =  alleleElem  .  getAttribute  (  "value"  )  ; 
} 
if  (  children  .  item  (  j  )  .  getNodeType  (  )  ==  Node  .  TEXT_NODE  )  { 
alleleRepresentation  =  children  .  item  (  j  )  .  getNodeValue  (  )  ; 
break  ; 
} 
} 
if  (  alleleRepresentation  ==  null  )  { 
throw   new   ImproperXMLException  (  "Unable to build Gene instance from XML Element: "  +  "value (allele) is missing representation."  )  ; 
} 
try  { 
thisGeneObject  .  setValueFromPersistentRepresentation  (  alleleRepresentation  )  ; 
}  catch  (  UnsupportedOperationException   e  )  { 
throw   new   GeneCreationException  (  "Unable to build Gene because it does not support the "  +  "setValueFromPersistentRepresentation() method."  )  ; 
} 
genes  .  add  (  thisGeneObject  )  ; 
} 
return  (  Gene  [  ]  )  genes  .  toArray  (  new   Gene  [  genes  .  size  (  )  ]  )  ; 
} 
























public   static   Chromosome   getChromosomeFromElement  (  Configuration   a_activeConfiguration  ,  Element   a_xmlElement  )  throws   ImproperXMLException  ,  InvalidConfigurationException  ,  UnsupportedRepresentationException  ,  GeneCreationException  { 
if  (  a_xmlElement  ==  null  ||  !  (  a_xmlElement  .  getTagName  (  )  .  equals  (  CHROMOSOME_TAG  )  )  )  { 
throw   new   ImproperXMLException  (  "Unable to build Chromosome instance from XML Element: "  +  "given Element is not a 'chromosome' element."  )  ; 
} 
Element   genesElement  =  (  Element  )  a_xmlElement  .  getElementsByTagName  (  GENES_TAG  )  .  item  (  0  )  ; 
if  (  genesElement  ==  null  )  { 
throw   new   ImproperXMLException  (  "Unable to build Chromosome instance from XML Element: "  +  "'genes' sub-element not found."  )  ; 
} 
Gene  [  ]  geneAlleles  =  getGenesFromElement  (  a_activeConfiguration  ,  genesElement  )  ; 
return   new   Chromosome  (  a_activeConfiguration  ,  geneAlleles  )  ; 
} 



























public   static   Genotype   getGenotypeFromElement  (  Configuration   a_activeConfiguration  ,  Element   a_xmlElement  )  throws   ImproperXMLException  ,  InvalidConfigurationException  ,  UnsupportedRepresentationException  ,  GeneCreationException  { 
if  (  a_xmlElement  ==  null  ||  !  (  a_xmlElement  .  getTagName  (  )  .  equals  (  GENOTYPE_TAG  )  )  )  { 
throw   new   ImproperXMLException  (  "Unable to build Genotype instance from XML Element: "  +  "given Element is not a 'genotype' element."  )  ; 
} 
NodeList   chromosomes  =  a_xmlElement  .  getElementsByTagName  (  CHROMOSOME_TAG  )  ; 
int   numChromosomes  =  chromosomes  .  getLength  (  )  ; 
Population   population  =  new   Population  (  a_activeConfiguration  ,  numChromosomes  )  ; 
for  (  int   i  =  0  ;  i  <  numChromosomes  ;  i  ++  )  { 
population  .  addChromosome  (  getChromosomeFromElement  (  a_activeConfiguration  ,  (  Element  )  chromosomes  .  item  (  i  )  )  )  ; 
} 
return   new   Genotype  (  a_activeConfiguration  ,  population  )  ; 
} 


























public   static   Genotype   getGenotypeFromDocument  (  Configuration   a_activeConfiguration  ,  Document   a_xmlDocument  )  throws   ImproperXMLException  ,  InvalidConfigurationException  ,  UnsupportedRepresentationException  ,  GeneCreationException  { 
Element   rootElement  =  a_xmlDocument  .  getDocumentElement  (  )  ; 
if  (  rootElement  ==  null  ||  !  (  rootElement  .  getTagName  (  )  .  equals  (  GENOTYPE_TAG  )  )  )  { 
throw   new   ImproperXMLException  (  "Unable to build Genotype from XML Document: "  +  "'genotype' element must be at root of document."  )  ; 
} 
return   getGenotypeFromElement  (  a_activeConfiguration  ,  rootElement  )  ; 
} 


























public   static   Chromosome   getChromosomeFromDocument  (  Configuration   a_activeConfiguration  ,  Document   a_xmlDocument  )  throws   ImproperXMLException  ,  InvalidConfigurationException  ,  UnsupportedRepresentationException  ,  GeneCreationException  { 
Element   rootElement  =  a_xmlDocument  .  getDocumentElement  (  )  ; 
if  (  rootElement  ==  null  ||  !  (  rootElement  .  getTagName  (  )  .  equals  (  CHROMOSOME_TAG  )  )  )  { 
throw   new   ImproperXMLException  (  "Unable to build Chromosome instance from XML Document: "  +  "'chromosome' element must be at root of Document."  )  ; 
} 
return   getChromosomeFromElement  (  a_activeConfiguration  ,  rootElement  )  ; 
} 












public   static   Document   readFile  (  File   file  )  throws   IOException  ,  org  .  xml  .  sax  .  SAXException  { 
return   m_documentCreator  .  parse  (  file  )  ; 
} 











public   static   void   writeFile  (  Document   doc  ,  File   file  )  throws   IOException  { 
TransformerFactory   tFactory  =  TransformerFactory  .  newInstance  (  )  ; 
Transformer   transformer  ; 
try  { 
transformer  =  tFactory  .  newTransformer  (  )  ; 
}  catch  (  TransformerConfigurationException   tex  )  { 
throw   new   IOException  (  tex  .  getMessage  (  )  )  ; 
} 
DOMSource   source  =  new   DOMSource  (  doc  )  ; 
FileOutputStream   fos  =  new   FileOutputStream  (  file  )  ; 
StreamResult   result  =  new   StreamResult  (  fos  )  ; 
try  { 
transformer  .  transform  (  source  ,  result  )  ; 
fos  .  close  (  )  ; 
}  catch  (  TransformerException   tex  )  { 
throw   new   IOException  (  tex  .  getMessage  (  )  )  ; 
} 
} 
} 

