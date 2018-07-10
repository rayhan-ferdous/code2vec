package   cwterm  .  service  .  rigctl  .  xsd  ; 




public   class   RigState   implements   org  .  apache  .  axis2  .  databinding  .  ADBBean  { 




protected   long   localAnnounces  ; 

protected   boolean   localAnnouncesTracker  =  false  ; 





protected   int  [  ]  localAttenuator  ; 

protected   boolean   localAttenuatorTracker  =  false  ; 





protected   cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  localChannelList  ; 

protected   boolean   localChannelListTracker  =  false  ; 





protected   cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  localFilters  ; 

protected   boolean   localFiltersTracker  =  false  ; 




protected   boolean   localHasGetFunc  ; 

protected   boolean   localHasGetFuncTracker  =  false  ; 




protected   boolean   localHasGetLevel  ; 

protected   boolean   localHasGetLevelTracker  =  false  ; 




protected   boolean   localHasGetParm  ; 

protected   boolean   localHasGetParmTracker  =  false  ; 




protected   boolean   localHasSetFunc  ; 

protected   boolean   localHasSetFuncTracker  =  false  ; 




protected   boolean   localHasSetLevel  ; 

protected   boolean   localHasSetLevelTracker  =  false  ; 




protected   boolean   localHasSetParm  ; 

protected   boolean   localHasSetParmTracker  =  false  ; 




protected   int   localItuRegion  ; 

protected   boolean   localItuRegionTracker  =  false  ; 




protected   long   localMaxIfShift  ; 

protected   boolean   localMaxIfShiftTracker  =  false  ; 




protected   long   localMaxRIT  ; 

protected   boolean   localMaxRITTracker  =  false  ; 




protected   long   localMaxXIT  ; 

protected   boolean   localMaxXITTracker  =  false  ; 





protected   int  [  ]  localPreamp  ; 

protected   boolean   localPreampTracker  =  false  ; 





protected   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  localRxRangeList  ; 

protected   boolean   localRxRangeListTracker  =  false  ; 





protected   cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  localTuningSteps  ; 

protected   boolean   localTuningStepsTracker  =  false  ; 





protected   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  localTxRangeList  ; 

protected   boolean   localTxRangeListTracker  =  false  ; 

private   static   java  .  lang  .  String   generatePrefix  (  java  .  lang  .  String   namespace  )  { 
if  (  namespace  .  equals  (  "http://rigctl.service.cwterm/xsd"  )  )  { 
return  "ns1"  ; 
} 
return   org  .  apache  .  axis2  .  databinding  .  utils  .  BeanUtil  .  getUniquePrefix  (  )  ; 
} 





public   long   getAnnounces  (  )  { 
return   localAnnounces  ; 
} 





public   void   setAnnounces  (  long   param  )  { 
if  (  param  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
localAnnouncesTracker  =  false  ; 
}  else  { 
localAnnouncesTracker  =  true  ; 
} 
this  .  localAnnounces  =  param  ; 
} 





public   int  [  ]  getAttenuator  (  )  { 
return   localAttenuator  ; 
} 




protected   void   validateAttenuator  (  int  [  ]  param  )  { 
} 





public   void   setAttenuator  (  int  [  ]  param  )  { 
validateAttenuator  (  param  )  ; 
if  (  param  !=  null  )  { 
localAttenuatorTracker  =  true  ; 
}  else  { 
localAttenuatorTracker  =  true  ; 
} 
this  .  localAttenuator  =  param  ; 
} 





public   cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  getChannelList  (  )  { 
return   localChannelList  ; 
} 




protected   void   validateChannelList  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  param  )  { 
} 





public   void   setChannelList  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  param  )  { 
validateChannelList  (  param  )  ; 
if  (  param  !=  null  )  { 
localChannelListTracker  =  true  ; 
}  else  { 
localChannelListTracker  =  true  ; 
} 
this  .  localChannelList  =  param  ; 
} 





public   void   addChannelList  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel   param  )  { 
if  (  localChannelList  ==  null  )  { 
localChannelList  =  new   cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  {  }  ; 
} 
localChannelListTracker  =  true  ; 
java  .  util  .  List   list  =  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  toList  (  localChannelList  )  ; 
list  .  add  (  param  )  ; 
this  .  localChannelList  =  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  )  list  .  toArray  (  new   cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  list  .  size  (  )  ]  )  ; 
} 





public   cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  getFilters  (  )  { 
return   localFilters  ; 
} 




protected   void   validateFilters  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  param  )  { 
} 





public   void   setFilters  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  param  )  { 
validateFilters  (  param  )  ; 
if  (  param  !=  null  )  { 
localFiltersTracker  =  true  ; 
}  else  { 
localFiltersTracker  =  true  ; 
} 
this  .  localFilters  =  param  ; 
} 





public   void   addFilters  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter   param  )  { 
if  (  localFilters  ==  null  )  { 
localFilters  =  new   cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  {  }  ; 
} 
localFiltersTracker  =  true  ; 
java  .  util  .  List   list  =  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  toList  (  localFilters  )  ; 
list  .  add  (  param  )  ; 
this  .  localFilters  =  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  )  list  .  toArray  (  new   cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  list  .  size  (  )  ]  )  ; 
} 





public   boolean   getHasGetFunc  (  )  { 
return   localHasGetFunc  ; 
} 





public   void   setHasGetFunc  (  boolean   param  )  { 
if  (  false  )  { 
localHasGetFuncTracker  =  false  ; 
}  else  { 
localHasGetFuncTracker  =  true  ; 
} 
this  .  localHasGetFunc  =  param  ; 
} 





public   boolean   getHasGetLevel  (  )  { 
return   localHasGetLevel  ; 
} 





public   void   setHasGetLevel  (  boolean   param  )  { 
if  (  false  )  { 
localHasGetLevelTracker  =  false  ; 
}  else  { 
localHasGetLevelTracker  =  true  ; 
} 
this  .  localHasGetLevel  =  param  ; 
} 





public   boolean   getHasGetParm  (  )  { 
return   localHasGetParm  ; 
} 





public   void   setHasGetParm  (  boolean   param  )  { 
if  (  false  )  { 
localHasGetParmTracker  =  false  ; 
}  else  { 
localHasGetParmTracker  =  true  ; 
} 
this  .  localHasGetParm  =  param  ; 
} 





public   boolean   getHasSetFunc  (  )  { 
return   localHasSetFunc  ; 
} 





public   void   setHasSetFunc  (  boolean   param  )  { 
if  (  false  )  { 
localHasSetFuncTracker  =  false  ; 
}  else  { 
localHasSetFuncTracker  =  true  ; 
} 
this  .  localHasSetFunc  =  param  ; 
} 





public   boolean   getHasSetLevel  (  )  { 
return   localHasSetLevel  ; 
} 





public   void   setHasSetLevel  (  boolean   param  )  { 
if  (  false  )  { 
localHasSetLevelTracker  =  false  ; 
}  else  { 
localHasSetLevelTracker  =  true  ; 
} 
this  .  localHasSetLevel  =  param  ; 
} 





public   boolean   getHasSetParm  (  )  { 
return   localHasSetParm  ; 
} 





public   void   setHasSetParm  (  boolean   param  )  { 
if  (  false  )  { 
localHasSetParmTracker  =  false  ; 
}  else  { 
localHasSetParmTracker  =  true  ; 
} 
this  .  localHasSetParm  =  param  ; 
} 





public   int   getItuRegion  (  )  { 
return   localItuRegion  ; 
} 





public   void   setItuRegion  (  int   param  )  { 
if  (  param  ==  java  .  lang  .  Integer  .  MIN_VALUE  )  { 
localItuRegionTracker  =  false  ; 
}  else  { 
localItuRegionTracker  =  true  ; 
} 
this  .  localItuRegion  =  param  ; 
} 





public   long   getMaxIfShift  (  )  { 
return   localMaxIfShift  ; 
} 





public   void   setMaxIfShift  (  long   param  )  { 
if  (  param  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
localMaxIfShiftTracker  =  false  ; 
}  else  { 
localMaxIfShiftTracker  =  true  ; 
} 
this  .  localMaxIfShift  =  param  ; 
} 





public   long   getMaxRIT  (  )  { 
return   localMaxRIT  ; 
} 





public   void   setMaxRIT  (  long   param  )  { 
if  (  param  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
localMaxRITTracker  =  false  ; 
}  else  { 
localMaxRITTracker  =  true  ; 
} 
this  .  localMaxRIT  =  param  ; 
} 





public   long   getMaxXIT  (  )  { 
return   localMaxXIT  ; 
} 





public   void   setMaxXIT  (  long   param  )  { 
if  (  param  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
localMaxXITTracker  =  false  ; 
}  else  { 
localMaxXITTracker  =  true  ; 
} 
this  .  localMaxXIT  =  param  ; 
} 





public   int  [  ]  getPreamp  (  )  { 
return   localPreamp  ; 
} 




protected   void   validatePreamp  (  int  [  ]  param  )  { 
} 





public   void   setPreamp  (  int  [  ]  param  )  { 
validatePreamp  (  param  )  ; 
if  (  param  !=  null  )  { 
localPreampTracker  =  true  ; 
}  else  { 
localPreampTracker  =  true  ; 
} 
this  .  localPreamp  =  param  ; 
} 





public   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  getRxRangeList  (  )  { 
return   localRxRangeList  ; 
} 




protected   void   validateRxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  param  )  { 
} 





public   void   setRxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  param  )  { 
validateRxRangeList  (  param  )  ; 
if  (  param  !=  null  )  { 
localRxRangeListTracker  =  true  ; 
}  else  { 
localRxRangeListTracker  =  true  ; 
} 
this  .  localRxRangeList  =  param  ; 
} 





public   void   addRxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange   param  )  { 
if  (  localRxRangeList  ==  null  )  { 
localRxRangeList  =  new   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  {  }  ; 
} 
localRxRangeListTracker  =  true  ; 
java  .  util  .  List   list  =  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  toList  (  localRxRangeList  )  ; 
list  .  add  (  param  )  ; 
this  .  localRxRangeList  =  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  )  list  .  toArray  (  new   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  list  .  size  (  )  ]  )  ; 
} 





public   cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  getTuningSteps  (  )  { 
return   localTuningSteps  ; 
} 




protected   void   validateTuningSteps  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  param  )  { 
} 





public   void   setTuningSteps  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  param  )  { 
validateTuningSteps  (  param  )  ; 
if  (  param  !=  null  )  { 
localTuningStepsTracker  =  true  ; 
}  else  { 
localTuningStepsTracker  =  true  ; 
} 
this  .  localTuningSteps  =  param  ; 
} 





public   void   addTuningSteps  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep   param  )  { 
if  (  localTuningSteps  ==  null  )  { 
localTuningSteps  =  new   cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  {  }  ; 
} 
localTuningStepsTracker  =  true  ; 
java  .  util  .  List   list  =  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  toList  (  localTuningSteps  )  ; 
list  .  add  (  param  )  ; 
this  .  localTuningSteps  =  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  )  list  .  toArray  (  new   cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  list  .  size  (  )  ]  )  ; 
} 





public   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  getTxRangeList  (  )  { 
return   localTxRangeList  ; 
} 




protected   void   validateTxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  param  )  { 
} 





public   void   setTxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  param  )  { 
validateTxRangeList  (  param  )  ; 
if  (  param  !=  null  )  { 
localTxRangeListTracker  =  true  ; 
}  else  { 
localTxRangeListTracker  =  true  ; 
} 
this  .  localTxRangeList  =  param  ; 
} 





public   void   addTxRangeList  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange   param  )  { 
if  (  localTxRangeList  ==  null  )  { 
localTxRangeList  =  new   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  {  }  ; 
} 
localTxRangeListTracker  =  true  ; 
java  .  util  .  List   list  =  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  toList  (  localTxRangeList  )  ; 
list  .  add  (  param  )  ; 
this  .  localTxRangeList  =  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  )  list  .  toArray  (  new   cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  list  .  size  (  )  ]  )  ; 
} 





public   static   boolean   isReaderMTOMAware  (  javax  .  xml  .  stream  .  XMLStreamReader   reader  )  { 
boolean   isReaderMTOMAware  =  false  ; 
try  { 
isReaderMTOMAware  =  java  .  lang  .  Boolean  .  TRUE  .  equals  (  reader  .  getProperty  (  org  .  apache  .  axiom  .  om  .  OMConstants  .  IS_DATA_HANDLERS_AWARE  )  )  ; 
}  catch  (  java  .  lang  .  IllegalArgumentException   e  )  { 
isReaderMTOMAware  =  false  ; 
} 
return   isReaderMTOMAware  ; 
} 







public   org  .  apache  .  axiom  .  om  .  OMElement   getOMElement  (  final   javax  .  xml  .  namespace  .  QName   parentQName  ,  final   org  .  apache  .  axiom  .  om  .  OMFactory   factory  )  throws   org  .  apache  .  axis2  .  databinding  .  ADBException  { 
org  .  apache  .  axiom  .  om  .  OMDataSource   dataSource  =  new   org  .  apache  .  axis2  .  databinding  .  ADBDataSource  (  this  ,  parentQName  )  { 

public   void   serialize  (  org  .  apache  .  axis2  .  databinding  .  utils  .  writer  .  MTOMAwareXMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
RigState  .  this  .  serialize  (  parentQName  ,  factory  ,  xmlWriter  )  ; 
} 
}  ; 
return   new   org  .  apache  .  axiom  .  om  .  impl  .  llom  .  OMSourcedElementImpl  (  parentQName  ,  factory  ,  dataSource  )  ; 
} 

public   void   serialize  (  final   javax  .  xml  .  namespace  .  QName   parentQName  ,  final   org  .  apache  .  axiom  .  om  .  OMFactory   factory  ,  org  .  apache  .  axis2  .  databinding  .  utils  .  writer  .  MTOMAwareXMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  ,  org  .  apache  .  axis2  .  databinding  .  ADBException  { 
java  .  lang  .  String   prefix  =  null  ; 
java  .  lang  .  String   namespace  =  null  ; 
prefix  =  parentQName  .  getPrefix  (  )  ; 
namespace  =  parentQName  .  getNamespaceURI  (  )  ; 
if  (  namespace  !=  null  )  { 
java  .  lang  .  String   writerPrefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  writerPrefix  !=  null  )  { 
xmlWriter  .  writeStartElement  (  namespace  ,  parentQName  .  getLocalPart  (  )  )  ; 
}  else  { 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
} 
xmlWriter  .  writeStartElement  (  prefix  ,  parentQName  .  getLocalPart  (  )  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  parentQName  .  getLocalPart  (  )  )  ; 
} 
if  (  localAnnouncesTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "announces"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "announces"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "announces"  )  ; 
} 
if  (  localAnnounces  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "announces cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localAnnounces  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localAttenuatorTracker  )  { 
if  (  localAttenuator  !=  null  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
boolean   emptyNamespace  =  (  namespace  ==  null  )  ||  (  namespace  .  length  (  )  ==  0  )  ; 
prefix  =  emptyNamespace  ?  null  :  xmlWriter  .  getPrefix  (  namespace  )  ; 
for  (  int   i  =  0  ;  i  <  localAttenuator  .  length  ;  i  ++  )  { 
if  (  localAttenuator  [  i  ]  !=  java  .  lang  .  Integer  .  MIN_VALUE  )  { 
if  (  !  emptyNamespace  )  { 
if  (  prefix  ==  null  )  { 
java  .  lang  .  String   prefix2  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "attenuator"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "attenuator"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "attenuator"  )  ; 
} 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localAttenuator  [  i  ]  )  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
}  else  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "attenuator"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "attenuator"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "attenuator"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "attenuator"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "attenuator"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "attenuator"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localChannelListTracker  )  { 
if  (  localChannelList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localChannelList  .  length  ;  i  ++  )  { 
if  (  localChannelList  [  i  ]  !=  null  )  { 
localChannelList  [  i  ]  .  serialize  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  ,  factory  ,  xmlWriter  )  ; 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "channelList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "channelList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "channelList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "channelList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "channelList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "channelList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localFiltersTracker  )  { 
if  (  localFilters  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localFilters  .  length  ;  i  ++  )  { 
if  (  localFilters  [  i  ]  !=  null  )  { 
localFilters  [  i  ]  .  serialize  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  ,  factory  ,  xmlWriter  )  ; 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "filters"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "filters"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "filters"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "filters"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "filters"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "filters"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localHasGetFuncTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasGetFunc"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasGetFunc"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasGetFunc"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasGetFunc cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetFunc  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localHasGetLevelTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasGetLevel"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasGetLevel"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasGetLevel"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasGetLevel cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetLevel  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localHasGetParmTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasGetParm"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasGetParm"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasGetParm"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasGetParm cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetParm  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localHasSetFuncTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasSetFunc"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasSetFunc"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasSetFunc"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasSetFunc cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetFunc  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localHasSetLevelTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasSetLevel"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasSetLevel"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasSetLevel"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasSetLevel cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetLevel  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localHasSetParmTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "hasSetParm"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "hasSetParm"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "hasSetParm"  )  ; 
} 
if  (  false  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "hasSetParm cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetParm  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localItuRegionTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "ituRegion"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "ituRegion"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "ituRegion"  )  ; 
} 
if  (  localItuRegion  ==  java  .  lang  .  Integer  .  MIN_VALUE  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "ituRegion cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localItuRegion  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localMaxIfShiftTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "maxIfShift"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "maxIfShift"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "maxIfShift"  )  ; 
} 
if  (  localMaxIfShift  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "maxIfShift cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxIfShift  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localMaxRITTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "maxRIT"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "maxRIT"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "maxRIT"  )  ; 
} 
if  (  localMaxRIT  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "maxRIT cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxRIT  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localMaxXITTracker  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "maxXIT"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "maxXIT"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "maxXIT"  )  ; 
} 
if  (  localMaxXIT  ==  java  .  lang  .  Long  .  MIN_VALUE  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "maxXIT cannot be null!!"  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxXIT  )  )  ; 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 
if  (  localPreampTracker  )  { 
if  (  localPreamp  !=  null  )  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
boolean   emptyNamespace  =  (  namespace  ==  null  )  ||  (  namespace  .  length  (  )  ==  0  )  ; 
prefix  =  emptyNamespace  ?  null  :  xmlWriter  .  getPrefix  (  namespace  )  ; 
for  (  int   i  =  0  ;  i  <  localPreamp  .  length  ;  i  ++  )  { 
if  (  localPreamp  [  i  ]  !=  java  .  lang  .  Integer  .  MIN_VALUE  )  { 
if  (  !  emptyNamespace  )  { 
if  (  prefix  ==  null  )  { 
java  .  lang  .  String   prefix2  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "preamp"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "preamp"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "preamp"  )  ; 
} 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localPreamp  [  i  ]  )  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
}  else  { 
namespace  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace  .  equals  (  ""  )  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
xmlWriter  .  writeStartElement  (  prefix  ,  "preamp"  ,  namespace  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace  ,  "preamp"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "preamp"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "preamp"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "preamp"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "preamp"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localRxRangeListTracker  )  { 
if  (  localRxRangeList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localRxRangeList  .  length  ;  i  ++  )  { 
if  (  localRxRangeList  [  i  ]  !=  null  )  { 
localRxRangeList  [  i  ]  .  serialize  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  ,  factory  ,  xmlWriter  )  ; 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "rxRangeList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "rxRangeList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "rxRangeList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "rxRangeList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "rxRangeList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "rxRangeList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localTuningStepsTracker  )  { 
if  (  localTuningSteps  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localTuningSteps  .  length  ;  i  ++  )  { 
if  (  localTuningSteps  [  i  ]  !=  null  )  { 
localTuningSteps  [  i  ]  .  serialize  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  ,  factory  ,  xmlWriter  )  ; 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "tuningSteps"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "tuningSteps"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "tuningSteps"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "tuningSteps"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "tuningSteps"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "tuningSteps"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
if  (  localTxRangeListTracker  )  { 
if  (  localTxRangeList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localTxRangeList  .  length  ;  i  ++  )  { 
if  (  localTxRangeList  [  i  ]  !=  null  )  { 
localTxRangeList  [  i  ]  .  serialize  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  ,  factory  ,  xmlWriter  )  ; 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "txRangeList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "txRangeList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "txRangeList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
}  else  { 
java  .  lang  .  String   namespace2  =  "http://rigctl.service.cwterm/xsd"  ; 
if  (  !  namespace2  .  equals  (  ""  )  )  { 
java  .  lang  .  String   prefix2  =  xmlWriter  .  getPrefix  (  namespace2  )  ; 
if  (  prefix2  ==  null  )  { 
prefix2  =  generatePrefix  (  namespace2  )  ; 
xmlWriter  .  writeStartElement  (  prefix2  ,  "txRangeList"  ,  namespace2  )  ; 
xmlWriter  .  writeNamespace  (  prefix2  ,  namespace2  )  ; 
xmlWriter  .  setPrefix  (  prefix2  ,  namespace2  )  ; 
}  else  { 
xmlWriter  .  writeStartElement  (  namespace2  ,  "txRangeList"  )  ; 
} 
}  else  { 
xmlWriter  .  writeStartElement  (  "txRangeList"  )  ; 
} 
writeAttribute  (  "xsi"  ,  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  ,  "1"  ,  xmlWriter  )  ; 
xmlWriter  .  writeEndElement  (  )  ; 
} 
} 
xmlWriter  .  writeEndElement  (  )  ; 
} 




private   void   writeAttribute  (  java  .  lang  .  String   prefix  ,  java  .  lang  .  String   namespace  ,  java  .  lang  .  String   attName  ,  java  .  lang  .  String   attValue  ,  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
if  (  xmlWriter  .  getPrefix  (  namespace  )  ==  null  )  { 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
} 
xmlWriter  .  writeAttribute  (  namespace  ,  attName  ,  attValue  )  ; 
} 




private   void   writeAttribute  (  java  .  lang  .  String   namespace  ,  java  .  lang  .  String   attName  ,  java  .  lang  .  String   attValue  ,  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
if  (  namespace  .  equals  (  ""  )  )  { 
xmlWriter  .  writeAttribute  (  attName  ,  attValue  )  ; 
}  else  { 
registerPrefix  (  xmlWriter  ,  namespace  )  ; 
xmlWriter  .  writeAttribute  (  namespace  ,  attName  ,  attValue  )  ; 
} 
} 




private   void   writeQNameAttribute  (  java  .  lang  .  String   namespace  ,  java  .  lang  .  String   attName  ,  javax  .  xml  .  namespace  .  QName   qname  ,  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
java  .  lang  .  String   attributeNamespace  =  qname  .  getNamespaceURI  (  )  ; 
java  .  lang  .  String   attributePrefix  =  xmlWriter  .  getPrefix  (  attributeNamespace  )  ; 
if  (  attributePrefix  ==  null  )  { 
attributePrefix  =  registerPrefix  (  xmlWriter  ,  attributeNamespace  )  ; 
} 
java  .  lang  .  String   attributeValue  ; 
if  (  attributePrefix  .  trim  (  )  .  length  (  )  >  0  )  { 
attributeValue  =  attributePrefix  +  ":"  +  qname  .  getLocalPart  (  )  ; 
}  else  { 
attributeValue  =  qname  .  getLocalPart  (  )  ; 
} 
if  (  namespace  .  equals  (  ""  )  )  { 
xmlWriter  .  writeAttribute  (  attName  ,  attributeValue  )  ; 
}  else  { 
registerPrefix  (  xmlWriter  ,  namespace  )  ; 
xmlWriter  .  writeAttribute  (  namespace  ,  attName  ,  attributeValue  )  ; 
} 
} 




private   void   writeQName  (  javax  .  xml  .  namespace  .  QName   qname  ,  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
java  .  lang  .  String   namespaceURI  =  qname  .  getNamespaceURI  (  )  ; 
if  (  namespaceURI  !=  null  )  { 
java  .  lang  .  String   prefix  =  xmlWriter  .  getPrefix  (  namespaceURI  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespaceURI  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespaceURI  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespaceURI  )  ; 
} 
if  (  prefix  .  trim  (  )  .  length  (  )  >  0  )  { 
xmlWriter  .  writeCharacters  (  prefix  +  ":"  +  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qname  )  )  ; 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qname  )  )  ; 
} 
}  else  { 
xmlWriter  .  writeCharacters  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qname  )  )  ; 
} 
} 

private   void   writeQNames  (  javax  .  xml  .  namespace  .  QName  [  ]  qnames  ,  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
if  (  qnames  !=  null  )  { 
java  .  lang  .  StringBuffer   stringToWrite  =  new   java  .  lang  .  StringBuffer  (  )  ; 
java  .  lang  .  String   namespaceURI  =  null  ; 
java  .  lang  .  String   prefix  =  null  ; 
for  (  int   i  =  0  ;  i  <  qnames  .  length  ;  i  ++  )  { 
if  (  i  >  0  )  { 
stringToWrite  .  append  (  " "  )  ; 
} 
namespaceURI  =  qnames  [  i  ]  .  getNamespaceURI  (  )  ; 
if  (  namespaceURI  !=  null  )  { 
prefix  =  xmlWriter  .  getPrefix  (  namespaceURI  )  ; 
if  (  (  prefix  ==  null  )  ||  (  prefix  .  length  (  )  ==  0  )  )  { 
prefix  =  generatePrefix  (  namespaceURI  )  ; 
xmlWriter  .  writeNamespace  (  prefix  ,  namespaceURI  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespaceURI  )  ; 
} 
if  (  prefix  .  trim  (  )  .  length  (  )  >  0  )  { 
stringToWrite  .  append  (  prefix  )  .  append  (  ":"  )  .  append  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qnames  [  i  ]  )  )  ; 
}  else  { 
stringToWrite  .  append  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qnames  [  i  ]  )  )  ; 
} 
}  else  { 
stringToWrite  .  append  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  qnames  [  i  ]  )  )  ; 
} 
} 
xmlWriter  .  writeCharacters  (  stringToWrite  .  toString  (  )  )  ; 
} 
} 




private   java  .  lang  .  String   registerPrefix  (  javax  .  xml  .  stream  .  XMLStreamWriter   xmlWriter  ,  java  .  lang  .  String   namespace  )  throws   javax  .  xml  .  stream  .  XMLStreamException  { 
java  .  lang  .  String   prefix  =  xmlWriter  .  getPrefix  (  namespace  )  ; 
if  (  prefix  ==  null  )  { 
prefix  =  generatePrefix  (  namespace  )  ; 
while  (  xmlWriter  .  getNamespaceContext  (  )  .  getNamespaceURI  (  prefix  )  !=  null  )  { 
prefix  =  org  .  apache  .  axis2  .  databinding  .  utils  .  BeanUtil  .  getUniquePrefix  (  )  ; 
} 
xmlWriter  .  writeNamespace  (  prefix  ,  namespace  )  ; 
xmlWriter  .  setPrefix  (  prefix  ,  namespace  )  ; 
} 
return   prefix  ; 
} 





public   javax  .  xml  .  stream  .  XMLStreamReader   getPullParser  (  javax  .  xml  .  namespace  .  QName   qName  )  throws   org  .  apache  .  axis2  .  databinding  .  ADBException  { 
java  .  util  .  ArrayList   elementList  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   attribList  =  new   java  .  util  .  ArrayList  (  )  ; 
if  (  localAnnouncesTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "announces"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localAnnounces  )  )  ; 
} 
if  (  localAttenuatorTracker  )  { 
if  (  localAttenuator  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localAttenuator  .  length  ;  i  ++  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "attenuator"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localAttenuator  [  i  ]  )  )  ; 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "attenuator"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
if  (  localChannelListTracker  )  { 
if  (  localChannelList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localChannelList  .  length  ;  i  ++  )  { 
if  (  localChannelList  [  i  ]  !=  null  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  )  ; 
elementList  .  add  (  localChannelList  [  i  ]  )  ; 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  )  ; 
elementList  .  add  (  localChannelList  )  ; 
} 
} 
if  (  localFiltersTracker  )  { 
if  (  localFilters  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localFilters  .  length  ;  i  ++  )  { 
if  (  localFilters  [  i  ]  !=  null  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  )  ; 
elementList  .  add  (  localFilters  [  i  ]  )  ; 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  )  ; 
elementList  .  add  (  localFilters  )  ; 
} 
} 
if  (  localHasGetFuncTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetFunc"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetFunc  )  )  ; 
} 
if  (  localHasGetLevelTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetLevel"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetLevel  )  )  ; 
} 
if  (  localHasGetParmTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetParm"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasGetParm  )  )  ; 
} 
if  (  localHasSetFuncTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetFunc"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetFunc  )  )  ; 
} 
if  (  localHasSetLevelTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetLevel"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetLevel  )  )  ; 
} 
if  (  localHasSetParmTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetParm"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localHasSetParm  )  )  ; 
} 
if  (  localItuRegionTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "ituRegion"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localItuRegion  )  )  ; 
} 
if  (  localMaxIfShiftTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxIfShift"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxIfShift  )  )  ; 
} 
if  (  localMaxRITTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxRIT"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxRIT  )  )  ; 
} 
if  (  localMaxXITTracker  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxXIT"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localMaxXIT  )  )  ; 
} 
if  (  localPreampTracker  )  { 
if  (  localPreamp  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localPreamp  .  length  ;  i  ++  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "preamp"  )  )  ; 
elementList  .  add  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToString  (  localPreamp  [  i  ]  )  )  ; 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "preamp"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
if  (  localRxRangeListTracker  )  { 
if  (  localRxRangeList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localRxRangeList  .  length  ;  i  ++  )  { 
if  (  localRxRangeList  [  i  ]  !=  null  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  )  ; 
elementList  .  add  (  localRxRangeList  [  i  ]  )  ; 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  )  ; 
elementList  .  add  (  localRxRangeList  )  ; 
} 
} 
if  (  localTuningStepsTracker  )  { 
if  (  localTuningSteps  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localTuningSteps  .  length  ;  i  ++  )  { 
if  (  localTuningSteps  [  i  ]  !=  null  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  )  ; 
elementList  .  add  (  localTuningSteps  [  i  ]  )  ; 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  )  ; 
elementList  .  add  (  localTuningSteps  )  ; 
} 
} 
if  (  localTxRangeListTracker  )  { 
if  (  localTxRangeList  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  localTxRangeList  .  length  ;  i  ++  )  { 
if  (  localTxRangeList  [  i  ]  !=  null  )  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  )  ; 
elementList  .  add  (  localTxRangeList  [  i  ]  )  ; 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  )  ; 
elementList  .  add  (  null  )  ; 
} 
} 
}  else  { 
elementList  .  add  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  )  ; 
elementList  .  add  (  localTxRangeList  )  ; 
} 
} 
return   new   org  .  apache  .  axis2  .  databinding  .  utils  .  reader  .  ADBXMLStreamReaderImpl  (  qName  ,  elementList  .  toArray  (  )  ,  attribList  .  toArray  (  )  )  ; 
} 




public   static   class   Factory  { 








public   static   RigState   parse  (  javax  .  xml  .  stream  .  XMLStreamReader   reader  )  throws   java  .  lang  .  Exception  { 
RigState   object  =  new   RigState  (  )  ; 
int   event  ; 
java  .  lang  .  String   nillableValue  =  null  ; 
java  .  lang  .  String   prefix  =  ""  ; 
java  .  lang  .  String   namespaceuri  =  ""  ; 
try  { 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "type"  )  !=  null  )  { 
java  .  lang  .  String   fullTypeName  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "type"  )  ; 
if  (  fullTypeName  !=  null  )  { 
java  .  lang  .  String   nsPrefix  =  null  ; 
if  (  fullTypeName  .  indexOf  (  ":"  )  >  -  1  )  { 
nsPrefix  =  fullTypeName  .  substring  (  0  ,  fullTypeName  .  indexOf  (  ":"  )  )  ; 
} 
nsPrefix  =  (  nsPrefix  ==  null  )  ?  ""  :  nsPrefix  ; 
java  .  lang  .  String   type  =  fullTypeName  .  substring  (  fullTypeName  .  indexOf  (  ":"  )  +  1  )  ; 
if  (  !  "RigState"  .  equals  (  type  )  )  { 
java  .  lang  .  String   nsUri  =  reader  .  getNamespaceContext  (  )  .  getNamespaceURI  (  nsPrefix  )  ; 
return  (  RigState  )  cwterm  .  service  .  rigctl  .  ExtensionMapper  .  getTypeObject  (  nsUri  ,  type  ,  reader  )  ; 
} 
} 
} 
java  .  util  .  Vector   handledAttributes  =  new   java  .  util  .  Vector  (  )  ; 
reader  .  next  (  )  ; 
java  .  util  .  ArrayList   list2  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list3  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list4  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list15  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list16  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list17  =  new   java  .  util  .  ArrayList  (  )  ; 
java  .  util  .  ArrayList   list18  =  new   java  .  util  .  ArrayList  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "announces"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setAnnounces  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToLong  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
object  .  setAnnounces  (  java  .  lang  .  Long  .  MIN_VALUE  )  ; 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "attenuator"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list2  .  add  (  String  .  valueOf  (  java  .  lang  .  Integer  .  MIN_VALUE  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list2  .  add  (  reader  .  getElementText  (  )  )  ; 
} 
boolean   loopDone2  =  false  ; 
while  (  !  loopDone2  )  { 
while  (  !  reader  .  isEndElement  (  )  )  { 
reader  .  next  (  )  ; 
} 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone2  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "attenuator"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list2  .  add  (  String  .  valueOf  (  java  .  lang  .  Integer  .  MIN_VALUE  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list2  .  add  (  reader  .  getElementText  (  )  )  ; 
} 
}  else  { 
loopDone2  =  true  ; 
} 
} 
} 
object  .  setAttenuator  (  (  int  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  int  .  class  ,  list2  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list3  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list3  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  .  Factory  .  parse  (  reader  )  )  ; 
} 
boolean   loopDone3  =  false  ; 
while  (  !  loopDone3  )  { 
while  (  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone3  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "channelList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list3  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list3  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  .  Factory  .  parse  (  reader  )  )  ; 
} 
}  else  { 
loopDone3  =  true  ; 
} 
} 
} 
object  .  setChannelList  (  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  cwterm  .  service  .  rigctl  .  xsd  .  Channel  .  class  ,  list3  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list4  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list4  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  .  Factory  .  parse  (  reader  )  )  ; 
} 
boolean   loopDone4  =  false  ; 
while  (  !  loopDone4  )  { 
while  (  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone4  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "filters"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list4  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list4  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  .  Factory  .  parse  (  reader  )  )  ; 
} 
}  else  { 
loopDone4  =  true  ; 
} 
} 
} 
object  .  setFilters  (  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  cwterm  .  service  .  rigctl  .  xsd  .  Filter  .  class  ,  list4  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetFunc"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasGetFunc  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetLevel"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasGetLevel  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasGetParm"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasGetParm  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetFunc"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasSetFunc  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetLevel"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasSetLevel  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "hasSetParm"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setHasSetParm  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToBoolean  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "ituRegion"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setItuRegion  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToInt  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
object  .  setItuRegion  (  java  .  lang  .  Integer  .  MIN_VALUE  )  ; 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxIfShift"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setMaxIfShift  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToLong  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
object  .  setMaxIfShift  (  java  .  lang  .  Long  .  MIN_VALUE  )  ; 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxRIT"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setMaxRIT  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToLong  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
object  .  setMaxRIT  (  java  .  lang  .  Long  .  MIN_VALUE  )  ; 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "maxXIT"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
java  .  lang  .  String   content  =  reader  .  getElementText  (  )  ; 
object  .  setMaxXIT  (  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToLong  (  content  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
object  .  setMaxXIT  (  java  .  lang  .  Long  .  MIN_VALUE  )  ; 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "preamp"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list15  .  add  (  String  .  valueOf  (  java  .  lang  .  Integer  .  MIN_VALUE  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list15  .  add  (  reader  .  getElementText  (  )  )  ; 
} 
boolean   loopDone15  =  false  ; 
while  (  !  loopDone15  )  { 
while  (  !  reader  .  isEndElement  (  )  )  { 
reader  .  next  (  )  ; 
} 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone15  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "preamp"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list15  .  add  (  String  .  valueOf  (  java  .  lang  .  Integer  .  MIN_VALUE  )  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list15  .  add  (  reader  .  getElementText  (  )  )  ; 
} 
}  else  { 
loopDone15  =  true  ; 
} 
} 
} 
object  .  setPreamp  (  (  int  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  int  .  class  ,  list15  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list16  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list16  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  Factory  .  parse  (  reader  )  )  ; 
} 
boolean   loopDone16  =  false  ; 
while  (  !  loopDone16  )  { 
while  (  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone16  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "rxRangeList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list16  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list16  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  Factory  .  parse  (  reader  )  )  ; 
} 
}  else  { 
loopDone16  =  true  ; 
} 
} 
} 
object  .  setRxRangeList  (  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  class  ,  list16  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list17  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list17  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  .  Factory  .  parse  (  reader  )  )  ; 
} 
boolean   loopDone17  =  false  ; 
while  (  !  loopDone17  )  { 
while  (  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone17  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "tuningSteps"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list17  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list17  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  .  Factory  .  parse  (  reader  )  )  ; 
} 
}  else  { 
loopDone17  =  true  ; 
} 
} 
} 
object  .  setTuningSteps  (  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  cwterm  .  service  .  rigctl  .  xsd  .  TuningStep  .  class  ,  list17  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  &&  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list18  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list18  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  Factory  .  parse  (  reader  )  )  ; 
} 
boolean   loopDone18  =  false  ; 
while  (  !  loopDone18  )  { 
while  (  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
reader  .  next  (  )  ; 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isEndElement  (  )  )  { 
loopDone18  =  true  ; 
}  else  { 
if  (  new   javax  .  xml  .  namespace  .  QName  (  "http://rigctl.service.cwterm/xsd"  ,  "txRangeList"  )  .  equals  (  reader  .  getName  (  )  )  )  { 
nillableValue  =  reader  .  getAttributeValue  (  "http://www.w3.org/2001/XMLSchema-instance"  ,  "nil"  )  ; 
if  (  "true"  .  equals  (  nillableValue  )  ||  "1"  .  equals  (  nillableValue  )  )  { 
list18  .  add  (  null  )  ; 
reader  .  next  (  )  ; 
}  else  { 
list18  .  add  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  Factory  .  parse  (  reader  )  )  ; 
} 
}  else  { 
loopDone18  =  true  ; 
} 
} 
} 
object  .  setTxRangeList  (  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  [  ]  )  org  .  apache  .  axis2  .  databinding  .  utils  .  ConverterUtil  .  convertToArray  (  cwterm  .  service  .  rigctl  .  xsd  .  FreqRange  .  class  ,  list18  )  )  ; 
}  else  { 
} 
while  (  !  reader  .  isStartElement  (  )  &&  !  reader  .  isEndElement  (  )  )  reader  .  next  (  )  ; 
if  (  reader  .  isStartElement  (  )  )  { 
throw   new   org  .  apache  .  axis2  .  databinding  .  ADBException  (  "Unexpected subelement "  +  reader  .  getLocalName  (  )  )  ; 
} 
}  catch  (  javax  .  xml  .  stream  .  XMLStreamException   e  )  { 
throw   new   java  .  lang  .  Exception  (  e  )  ; 
} 
return   object  ; 
} 
} 
} 

