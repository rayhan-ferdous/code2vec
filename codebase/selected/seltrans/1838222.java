package   net  .  sf  .  gateway  .  mef  .  pdf  .  ty2009  ; 

import   com  .  itextpdf  .  text  .  Element  ; 
import   com  .  itextpdf  .  text  .  pdf  .  PdfContentByte  ; 
import   com  .  itextpdf  .  text  .  DocumentException  ; 
import   com  .  itextpdf  .  text  .  pdf  .  AcroFields  ; 
import   com  .  itextpdf  .  text  .  pdf  .  BaseFont  ; 
import   com  .  itextpdf  .  text  .  Font  ; 
import   com  .  itextpdf  .  text  .  FontFactory  ; 
import   com  .  itextpdf  .  text  .  pdf  .  PdfReader  ; 
import   com  .  itextpdf  .  text  .  pdf  .  PdfStamper  ; 
import   com  .  itextpdf  .  text  .  BaseColor  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  Date  ; 




public   class   HS122  { 








public   void   fill  (  String   src  ,  String   dest  ,  String   user  )  throws   IOException  ,  DocumentException  { 
PdfReader   reader  =  new   PdfReader  (  src  )  ; 
FileOutputStream   writer  =  new   FileOutputStream  (  dest  )  ; 
PdfStamper   stamper  =  new   PdfStamper  (  reader  ,  writer  )  ; 
stamper  .  setEncryption  (  true  ,  ""  ,  "Gu7ruc*YAWaStEbr"  ,  0  )  ; 
AcroFields   fields  =  stamper  .  getAcroFields  (  )  ; 
Font   font  =  FontFactory  .  getFont  (  FontFactory  .  COURIER_BOLD  )  ; 
font  .  setSize  (  (  float  )  20.2  )  ; 
BaseFont   baseFont  =  font  .  getBaseFont  (  )  ; 
fields  .  setFieldProperty  (  "RentalUse"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "RentalUse"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "RentalUse"  ,  this  .  get_RentalUse  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary FirstName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary FirstName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary FirstName"  ,  this  .  get_Secondary_FirstName  (  )  )  ; 
fields  .  setFieldProperty  (  "HousesiteEducationTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "HousesiteEducationTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "HousesiteEducationTax"  ,  this  .  get_HousesiteEducationTax  (  )  )  ; 
fields  .  setFieldProperty  (  "TownCityLegalResidence"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TownCityLegalResidence"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TownCityLegalResidence"  ,  this  .  get_TownCityLegalResidence  (  )  )  ; 
fields  .  setFieldProperty  (  "PreparerFirmIDNumber"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "PreparerFirmIDNumber"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "PreparerFirmIDNumber"  ,  this  .  get_PreparerFirmIDNumber  (  )  )  ; 
fields  .  setFieldProperty  (  "OwnedByRelatedFarmer"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OwnedByRelatedFarmer"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OwnedByRelatedFarmer"  ,  this  .  get_OwnedByRelatedFarmer  (  )  )  ; 
fields  .  setFieldProperty  (  "AllocatedEducationTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "AllocatedEducationTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "AllocatedEducationTax"  ,  this  .  get_AllocatedEducationTax  (  )  )  ; 
fields  .  setFieldProperty  (  "LifeEstateHolder"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "LifeEstateHolder"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "LifeEstateHolder"  ,  this  .  get_LifeEstateHolder  (  )  )  ; 
fields  .  setFieldProperty  (  "City"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "City"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "City"  ,  this  .  get_City  (  )  )  ; 
fields  .  setFieldProperty  (  "FullYearResident"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FullYearResident"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FullYearResident"  ,  this  .  get_FullYearResident  (  )  )  ; 
fields  .  setFieldProperty  (  "HousesiteValue"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "HousesiteValue"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "HousesiteValue"  ,  this  .  get_HousesiteValue  (  )  )  ; 
fields  .  setFieldProperty  (  "Preparer Phone"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Preparer Phone"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Preparer Phone"  ,  this  .  get_Preparer_Phone  (  )  )  ; 
fields  .  setFieldProperty  (  "PreparerBusinessName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "PreparerBusinessName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "PreparerBusinessName"  ,  this  .  get_PreparerBusinessName  (  )  )  ; 
fields  .  setFieldProperty  (  "CrossingTownBoundaries"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "CrossingTownBoundaries"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "CrossingTownBoundaries"  ,  this  .  get_CrossingTownBoundaries  (  )  )  ; 
fields  .  setFieldProperty  (  "LotRent"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "LotRent"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "LotRent"  ,  this  .  get_LotRent  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary TaxpayerSSN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary TaxpayerSSN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary TaxpayerSSN"  ,  this  .  get_Secondary_TaxpayerSSN  (  )  )  ; 
fields  .  setFieldProperty  (  "Preparer SSN PTIN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Preparer SSN PTIN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Preparer SSN PTIN"  ,  this  .  get_Preparer_SSN_PTIN  (  )  )  ; 
fields  .  setFieldProperty  (  "FirstName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FirstName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FirstName"  ,  this  .  get_FirstName  (  )  )  ; 
fields  .  setFieldProperty  (  "AddressLine1"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "AddressLine1"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "AddressLine1"  ,  this  .  get_AddressLine1  (  )  )  ; 
fields  .  setFieldProperty  (  "HousesiteMunicipalTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "HousesiteMunicipalTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "HousesiteMunicipalTax"  ,  this  .  get_HousesiteMunicipalTax  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalParcelAcres"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalParcelAcres"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalParcelAcres"  ,  this  .  get_TotalParcelAcres  (  )  )  ; 
fields  .  setFieldProperty  (  "RevocableTrust"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "RevocableTrust"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "RevocableTrust"  ,  this  .  get_RevocableTrust  (  )  )  ; 
fields  .  setFieldProperty  (  "TaxpayerSSN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TaxpayerSSN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TaxpayerSSN"  ,  this  .  get_TaxpayerSSN  (  )  )  ; 
fields  .  setFieldProperty  (  "OwnershipInterest"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OwnershipInterest"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OwnershipInterest"  ,  this  .  get_OwnershipInterest  (  )  )  ; 
fields  .  setFieldProperty  (  "SellHousesite"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SellHousesite"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SellHousesite"  ,  this  .  get_SellHousesite  (  )  )  ; 
fields  .  setFieldProperty  (  "ClaimedAsDependent"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ClaimedAsDependent"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ClaimedAsDependent"  ,  this  .  get_ClaimedAsDependent  (  )  )  ; 
fields  .  setFieldProperty  (  "State"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "State"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "State"  ,  this  .  get_State  (  )  )  ; 
fields  .  setFieldProperty  (  "ZIPCode"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ZIPCode"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ZIPCode"  ,  this  .  get_ZIPCode  (  )  )  ; 
fields  .  setFieldProperty  (  "BusinessUse"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BusinessUse"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BusinessUse"  ,  this  .  get_BusinessUse  (  )  )  ; 
fields  .  setFieldProperty  (  "AllocatedMunicipalTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "AllocatedMunicipalTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "AllocatedMunicipalTax"  ,  this  .  get_AllocatedMunicipalTax  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary LastName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary LastName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary LastName"  ,  this  .  get_Secondary_LastName  (  )  )  ; 
fields  .  setFieldProperty  (  "SpanNumber"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SpanNumber"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SpanNumber"  ,  this  .  get_SpanNumber  (  )  )  ; 
fields  .  setFieldProperty  (  "ClaimantDateOfBirth"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ClaimantDateOfBirth"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ClaimantDateOfBirth"  ,  this  .  get_ClaimantDateOfBirth  (  )  )  ; 
fields  .  setFieldProperty  (  "HouseholdIncome"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "HouseholdIncome"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "HouseholdIncome"  ,  this  .  get_HouseholdIncome  (  )  )  ; 
fields  .  setFieldProperty  (  "SchoolDistrict"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SchoolDistrict"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SchoolDistrict"  ,  this  .  get_SchoolDistrict  (  )  )  ; 
fields  .  setFieldProperty  (  "StateofLegalResidence"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "StateofLegalResidence"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "StateofLegalResidence"  ,  this  .  get_StateofLegalResidence  (  )  )  ; 
fields  .  setFieldProperty  (  "BusinessRentUseImprovements"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BusinessRentUseImprovements"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BusinessRentUseImprovements"  ,  this  .  get_BusinessRentUseImprovements  (  )  )  ; 
fields  .  setFieldProperty  (  "HomesteadLocation"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "HomesteadLocation"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "HomesteadLocation"  ,  this  .  get_HomesteadLocation  (  )  )  ; 
fields  .  setFieldProperty  (  "LastName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "LastName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "LastName"  ,  this  .  get_LastName  (  )  )  ; 
stamper  .  setFormFlattening  (  true  )  ; 
stamper  .  setFullCompression  (  )  ; 
for  (  int   i  =  0  ;  i  <  reader  .  getNumberOfPages  (  )  +  1  ;  i  ++  )  { 
PdfContentByte   overContent  =  stamper  .  getOverContent  (  i  )  ; 
if  (  overContent  !=  null  )  { 
overContent  .  beginText  (  )  ; 
font  =  FontFactory  .  getFont  (  FontFactory  .  TIMES_ITALIC  )  ; 
font  .  setColor  (  BaseColor  .  BLUE  )  ; 
baseFont  =  font  .  getBaseFont  (  )  ; 
overContent  .  setColorFill  (  BaseColor  .  BLUE  )  ; 
overContent  .  setFontAndSize  (  baseFont  ,  24  )  ; 
overContent  .  showTextAligned  (  Element  .  ALIGN_RIGHT  |  Element  .  ALIGN_TOP  ,  "Electronically filed via Modernized eFile"  ,  20  ,  175  ,  90  )  ; 
overContent  .  endText  (  )  ; 
overContent  .  beginText  (  )  ; 
font  =  FontFactory  .  getFont  (  FontFactory  .  TIMES  )  ; 
font  .  setColor  (  BaseColor  .  RED  )  ; 
baseFont  =  font  .  getBaseFont  (  )  ; 
overContent  .  setColorFill  (  BaseColor  .  RED  )  ; 
overContent  .  setFontAndSize  (  baseFont  ,  8  )  ; 
overContent  .  showTextAligned  (  Element  .  ALIGN_CENTER  |  Element  .  ALIGN_BOTTOM  ,  "Retrieved by "  +  user  +  " on "  +  new   Date  (  )  .  toString  (  )  ,  220  ,  3  ,  0  )  ; 
overContent  .  endText  (  )  ; 
} 
} 
stamper  .  close  (  )  ; 
reader  .  close  (  )  ; 
} 




private   String   _RentalUse  =  ""  ; 





public   void   set_RentalUse  (  String   _RentalUse  )  { 
this  .  _RentalUse  =  _RentalUse  ; 
} 





public   String   get_RentalUse  (  )  { 
return   this  .  _RentalUse  ; 
} 




private   String   _Secondary_FirstName  =  ""  ; 





public   void   set_Secondary_FirstName  (  String   _Secondary_FirstName  )  { 
this  .  _Secondary_FirstName  =  _Secondary_FirstName  ; 
} 





public   String   get_Secondary_FirstName  (  )  { 
return   this  .  _Secondary_FirstName  ; 
} 




private   String   _HousesiteEducationTax  =  ""  ; 





public   void   set_HousesiteEducationTax  (  String   _HousesiteEducationTax  )  { 
this  .  _HousesiteEducationTax  =  _HousesiteEducationTax  ; 
} 





public   String   get_HousesiteEducationTax  (  )  { 
return   this  .  _HousesiteEducationTax  ; 
} 




private   String   _TownCityLegalResidence  =  ""  ; 





public   void   set_TownCityLegalResidence  (  String   _TownCityLegalResidence  )  { 
this  .  _TownCityLegalResidence  =  _TownCityLegalResidence  ; 
} 





public   String   get_TownCityLegalResidence  (  )  { 
return   this  .  _TownCityLegalResidence  ; 
} 




private   String   _PreparerFirmIDNumber  =  ""  ; 





public   void   set_PreparerFirmIDNumber  (  String   _PreparerFirmIDNumber  )  { 
this  .  _PreparerFirmIDNumber  =  _PreparerFirmIDNumber  ; 
} 





public   String   get_PreparerFirmIDNumber  (  )  { 
return   this  .  _PreparerFirmIDNumber  ; 
} 




private   String   _OwnedByRelatedFarmer  =  ""  ; 





public   void   set_OwnedByRelatedFarmer  (  String   _OwnedByRelatedFarmer  )  { 
this  .  _OwnedByRelatedFarmer  =  _OwnedByRelatedFarmer  ; 
} 





public   String   get_OwnedByRelatedFarmer  (  )  { 
return   this  .  _OwnedByRelatedFarmer  ; 
} 




private   String   _AllocatedEducationTax  =  ""  ; 





public   void   set_AllocatedEducationTax  (  String   _AllocatedEducationTax  )  { 
this  .  _AllocatedEducationTax  =  _AllocatedEducationTax  ; 
} 





public   String   get_AllocatedEducationTax  (  )  { 
return   this  .  _AllocatedEducationTax  ; 
} 




private   String   _LifeEstateHolder  =  ""  ; 





public   void   set_LifeEstateHolder  (  String   _LifeEstateHolder  )  { 
this  .  _LifeEstateHolder  =  _LifeEstateHolder  ; 
} 





public   String   get_LifeEstateHolder  (  )  { 
return   this  .  _LifeEstateHolder  ; 
} 




private   String   _City  =  ""  ; 





public   void   set_City  (  String   _City  )  { 
this  .  _City  =  _City  ; 
} 





public   String   get_City  (  )  { 
return   this  .  _City  ; 
} 




private   String   _FullYearResident  =  ""  ; 





public   void   set_FullYearResident  (  String   _FullYearResident  )  { 
this  .  _FullYearResident  =  _FullYearResident  ; 
} 





public   String   get_FullYearResident  (  )  { 
return   this  .  _FullYearResident  ; 
} 




private   String   _HousesiteValue  =  ""  ; 





public   void   set_HousesiteValue  (  String   _HousesiteValue  )  { 
this  .  _HousesiteValue  =  _HousesiteValue  ; 
} 





public   String   get_HousesiteValue  (  )  { 
return   this  .  _HousesiteValue  ; 
} 




private   String   _Preparer_Phone  =  ""  ; 





public   void   set_Preparer_Phone  (  String   _Preparer_Phone  )  { 
this  .  _Preparer_Phone  =  _Preparer_Phone  ; 
} 





public   String   get_Preparer_Phone  (  )  { 
return   this  .  _Preparer_Phone  ; 
} 




private   String   _PreparerBusinessName  =  ""  ; 





public   void   set_PreparerBusinessName  (  String   _PreparerBusinessName  )  { 
this  .  _PreparerBusinessName  =  _PreparerBusinessName  ; 
} 





public   String   get_PreparerBusinessName  (  )  { 
return   this  .  _PreparerBusinessName  ; 
} 




private   String   _CrossingTownBoundaries  =  ""  ; 





public   void   set_CrossingTownBoundaries  (  String   _CrossingTownBoundaries  )  { 
this  .  _CrossingTownBoundaries  =  _CrossingTownBoundaries  ; 
} 





public   String   get_CrossingTownBoundaries  (  )  { 
return   this  .  _CrossingTownBoundaries  ; 
} 




private   String   _LotRent  =  ""  ; 





public   void   set_LotRent  (  String   _LotRent  )  { 
this  .  _LotRent  =  _LotRent  ; 
} 





public   String   get_LotRent  (  )  { 
return   this  .  _LotRent  ; 
} 




private   String   _Secondary_TaxpayerSSN  =  ""  ; 





public   void   set_Secondary_TaxpayerSSN  (  String   _Secondary_TaxpayerSSN  )  { 
this  .  _Secondary_TaxpayerSSN  =  _Secondary_TaxpayerSSN  ; 
} 





public   String   get_Secondary_TaxpayerSSN  (  )  { 
return   this  .  _Secondary_TaxpayerSSN  ; 
} 




private   String   _Preparer_SSN_PTIN  =  ""  ; 





public   void   set_Preparer_SSN_PTIN  (  String   _Preparer_SSN_PTIN  )  { 
this  .  _Preparer_SSN_PTIN  =  _Preparer_SSN_PTIN  ; 
} 





public   String   get_Preparer_SSN_PTIN  (  )  { 
return   this  .  _Preparer_SSN_PTIN  ; 
} 




private   String   _FirstName  =  ""  ; 





public   void   set_FirstName  (  String   _FirstName  )  { 
this  .  _FirstName  =  _FirstName  ; 
} 





public   String   get_FirstName  (  )  { 
return   this  .  _FirstName  ; 
} 




private   String   _AddressLine1  =  ""  ; 





public   void   set_AddressLine1  (  String   _AddressLine1  )  { 
this  .  _AddressLine1  =  _AddressLine1  ; 
} 





public   String   get_AddressLine1  (  )  { 
return   this  .  _AddressLine1  ; 
} 




private   String   _HousesiteMunicipalTax  =  ""  ; 





public   void   set_HousesiteMunicipalTax  (  String   _HousesiteMunicipalTax  )  { 
this  .  _HousesiteMunicipalTax  =  _HousesiteMunicipalTax  ; 
} 





public   String   get_HousesiteMunicipalTax  (  )  { 
return   this  .  _HousesiteMunicipalTax  ; 
} 




private   String   _TotalParcelAcres  =  ""  ; 





public   void   set_TotalParcelAcres  (  String   _TotalParcelAcres  )  { 
this  .  _TotalParcelAcres  =  _TotalParcelAcres  ; 
} 





public   String   get_TotalParcelAcres  (  )  { 
return   this  .  _TotalParcelAcres  ; 
} 




private   String   _RevocableTrust  =  ""  ; 





public   void   set_RevocableTrust  (  String   _RevocableTrust  )  { 
this  .  _RevocableTrust  =  _RevocableTrust  ; 
} 





public   String   get_RevocableTrust  (  )  { 
return   this  .  _RevocableTrust  ; 
} 




private   String   _TaxpayerSSN  =  ""  ; 





public   void   set_TaxpayerSSN  (  String   _TaxpayerSSN  )  { 
this  .  _TaxpayerSSN  =  _TaxpayerSSN  ; 
} 





public   String   get_TaxpayerSSN  (  )  { 
return   this  .  _TaxpayerSSN  ; 
} 




private   String   _OwnershipInterest  =  ""  ; 





public   void   set_OwnershipInterest  (  String   _OwnershipInterest  )  { 
this  .  _OwnershipInterest  =  _OwnershipInterest  ; 
} 





public   String   get_OwnershipInterest  (  )  { 
return   this  .  _OwnershipInterest  ; 
} 




private   String   _SellHousesite  =  ""  ; 





public   void   set_SellHousesite  (  String   _SellHousesite  )  { 
this  .  _SellHousesite  =  _SellHousesite  ; 
} 





public   String   get_SellHousesite  (  )  { 
return   this  .  _SellHousesite  ; 
} 




private   String   _ClaimedAsDependent  =  ""  ; 





public   void   set_ClaimedAsDependent  (  String   _ClaimedAsDependent  )  { 
this  .  _ClaimedAsDependent  =  _ClaimedAsDependent  ; 
} 





public   String   get_ClaimedAsDependent  (  )  { 
return   this  .  _ClaimedAsDependent  ; 
} 




private   String   _State  =  ""  ; 





public   void   set_State  (  String   _State  )  { 
this  .  _State  =  _State  ; 
} 





public   String   get_State  (  )  { 
return   this  .  _State  ; 
} 




private   String   _ZIPCode  =  ""  ; 





public   void   set_ZIPCode  (  String   _ZIPCode  )  { 
this  .  _ZIPCode  =  _ZIPCode  ; 
} 





public   String   get_ZIPCode  (  )  { 
return   this  .  _ZIPCode  ; 
} 




private   String   _BusinessUse  =  ""  ; 





public   void   set_BusinessUse  (  String   _BusinessUse  )  { 
this  .  _BusinessUse  =  _BusinessUse  ; 
} 





public   String   get_BusinessUse  (  )  { 
return   this  .  _BusinessUse  ; 
} 




private   String   _AllocatedMunicipalTax  =  ""  ; 





public   void   set_AllocatedMunicipalTax  (  String   _AllocatedMunicipalTax  )  { 
this  .  _AllocatedMunicipalTax  =  _AllocatedMunicipalTax  ; 
} 





public   String   get_AllocatedMunicipalTax  (  )  { 
return   this  .  _AllocatedMunicipalTax  ; 
} 




private   String   _Secondary_LastName  =  ""  ; 





public   void   set_Secondary_LastName  (  String   _Secondary_LastName  )  { 
this  .  _Secondary_LastName  =  _Secondary_LastName  ; 
} 





public   String   get_Secondary_LastName  (  )  { 
return   this  .  _Secondary_LastName  ; 
} 




private   String   _SpanNumber  =  ""  ; 





public   void   set_SpanNumber  (  String   _SpanNumber  )  { 
this  .  _SpanNumber  =  _SpanNumber  ; 
} 





public   String   get_SpanNumber  (  )  { 
return   this  .  _SpanNumber  ; 
} 




private   String   _ClaimantDateOfBirth  =  ""  ; 





public   void   set_ClaimantDateOfBirth  (  String   _ClaimantDateOfBirth  )  { 
this  .  _ClaimantDateOfBirth  =  _ClaimantDateOfBirth  ; 
} 





public   String   get_ClaimantDateOfBirth  (  )  { 
return   this  .  _ClaimantDateOfBirth  ; 
} 




private   String   _HouseholdIncome  =  ""  ; 





public   void   set_HouseholdIncome  (  String   _HouseholdIncome  )  { 
this  .  _HouseholdIncome  =  _HouseholdIncome  ; 
} 





public   String   get_HouseholdIncome  (  )  { 
return   this  .  _HouseholdIncome  ; 
} 




private   String   _SchoolDistrict  =  ""  ; 





public   void   set_SchoolDistrict  (  String   _SchoolDistrict  )  { 
this  .  _SchoolDistrict  =  _SchoolDistrict  ; 
} 





public   String   get_SchoolDistrict  (  )  { 
return   this  .  _SchoolDistrict  ; 
} 




private   String   _StateofLegalResidence  =  ""  ; 





public   void   set_StateofLegalResidence  (  String   _StateofLegalResidence  )  { 
this  .  _StateofLegalResidence  =  _StateofLegalResidence  ; 
} 





public   String   get_StateofLegalResidence  (  )  { 
return   this  .  _StateofLegalResidence  ; 
} 




private   String   _BusinessRentUseImprovements  =  ""  ; 





public   void   set_BusinessRentUseImprovements  (  String   _BusinessRentUseImprovements  )  { 
this  .  _BusinessRentUseImprovements  =  _BusinessRentUseImprovements  ; 
} 





public   String   get_BusinessRentUseImprovements  (  )  { 
return   this  .  _BusinessRentUseImprovements  ; 
} 




private   String   _HomesteadLocation  =  ""  ; 





public   void   set_HomesteadLocation  (  String   _HomesteadLocation  )  { 
this  .  _HomesteadLocation  =  _HomesteadLocation  ; 
} 





public   String   get_HomesteadLocation  (  )  { 
return   this  .  _HomesteadLocation  ; 
} 




private   String   _LastName  =  ""  ; 





public   void   set_LastName  (  String   _LastName  )  { 
this  .  _LastName  =  _LastName  ; 
} 





public   String   get_LastName  (  )  { 
return   this  .  _LastName  ; 
} 
} 

