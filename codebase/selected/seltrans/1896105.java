package   net  .  sf  .  gateway  .  mef  .  pdf  ; 

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
import   java  .  util  .  UUID  ; 




public   class   TY2009_VT_IN111  { 








public   void   fill  (  String   src  ,  String   dest  ,  String   user  )  throws   IOException  ,  DocumentException  { 
PdfReader   reader  =  new   PdfReader  (  src  )  ; 
FileOutputStream   writer  =  new   FileOutputStream  (  dest  )  ; 
PdfStamper   stamper  =  new   PdfStamper  (  reader  ,  writer  )  ; 
stamper  .  setEncryption  (  true  ,  ""  ,  UUID  .  randomUUID  (  )  .  toString  (  )  ,  0  )  ; 
AcroFields   fields  =  stamper  .  getAcroFields  (  )  ; 
Font   font  =  FontFactory  .  getFont  (  FontFactory  .  COURIER_BOLD  )  ; 
font  .  setSize  (  (  float  )  20.2  )  ; 
BaseFont   baseFont  =  font  .  getBaseFont  (  )  ; 
fields  .  setFieldProperty  (  "VTTaxAfterCredits"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VTTaxAfterCredits"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VTTaxAfterCredits"  ,  this  .  get_VTTaxAfterCredits  (  )  )  ; 
fields  .  setFieldProperty  (  "VermontCampaignFund"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VermontCampaignFund"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VermontCampaignFund"  ,  this  .  get_VermontCampaignFund  (  )  )  ; 
fields  .  setFieldProperty  (  "Exemptions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Exemptions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Exemptions"  ,  this  .  get_Exemptions  (  )  )  ; 
fields  .  setFieldProperty  (  "FederalAdjustedGrossIncome"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FederalAdjustedGrossIncome"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FederalAdjustedGrossIncome"  ,  this  .  get_FederalAdjustedGrossIncome  (  )  )  ; 
fields  .  setFieldProperty  (  "BalanceDueWithReturn"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BalanceDueWithReturn"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BalanceDueWithReturn"  ,  this  .  get_BalanceDueWithReturn  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary FirstName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary FirstName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary FirstName"  ,  this  .  get_Secondary_FirstName  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalDonations"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalDonations"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalDonations"  ,  this  .  get_TotalDonations  (  )  )  ; 
fields  .  setFieldProperty  (  "LastName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "LastName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "LastName"  ,  this  .  get_LastName  (  )  )  ; 
fields  .  setFieldProperty  (  "Check Box1"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Check Box1"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Check Box1"  ,  this  .  get_Check_Box1  (  )  )  ; 
fields  .  setFieldProperty  (  "Check Box2"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Check Box2"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Check Box2"  ,  this  .  get_Check_Box2  (  )  )  ; 
fields  .  setFieldProperty  (  "AdjustedVTIncomeTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "AdjustedVTIncomeTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "AdjustedVTIncomeTax"  ,  this  .  get_AdjustedVTIncomeTax  (  )  )  ; 
fields  .  setFieldProperty  (  "SpouseCUPartnerName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SpouseCUPartnerName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SpouseCUPartnerName"  ,  this  .  get_SpouseCUPartnerName  (  )  )  ; 
fields  .  setFieldProperty  (  "BonusDepreciation"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BonusDepreciation"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BonusDepreciation"  ,  this  .  get_BonusDepreciation  (  )  )  ; 
fields  .  setFieldProperty  (  "ChildrensTrustFund"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ChildrensTrustFund"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ChildrensTrustFund"  ,  this  .  get_ChildrensTrustFund  (  )  )  ; 
fields  .  setFieldProperty  (  "VTTaxableIncome"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VTTaxableIncome"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VTTaxableIncome"  ,  this  .  get_VTTaxableIncome  (  )  )  ; 
fields  .  setFieldProperty  (  "RealEstateWithholding"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "RealEstateWithholding"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "RealEstateWithholding"  ,  this  .  get_RealEstateWithholding  (  )  )  ; 
fields  .  setFieldProperty  (  "SchoolDistrict"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SchoolDistrict"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SchoolDistrict"  ,  this  .  get_SchoolDistrict  (  )  )  ; 
fields  .  setFieldProperty  (  "FedTaxableWithAdditions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FedTaxableWithAdditions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FedTaxableWithAdditions"  ,  this  .  get_FedTaxableWithAdditions  (  )  )  ; 
fields  .  setFieldProperty  (  "StateofLegalResidence"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "StateofLegalResidence"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "StateofLegalResidence"  ,  this  .  get_StateofLegalResidence  (  )  )  ; 
fields  .  setFieldProperty  (  "UseTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "UseTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "UseTax"  ,  this  .  get_UseTax  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalVTCredits"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalVTCredits"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalVTCredits"  ,  this  .  get_TotalVTCredits  (  )  )  ; 
fields  .  setFieldProperty  (  "TaxWithheld"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TaxWithheld"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TaxWithheld"  ,  this  .  get_TaxWithheld  (  )  )  ; 
fields  .  setFieldProperty  (  "FederalTaxableIncome"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FederalTaxableIncome"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FederalTaxableIncome"  ,  this  .  get_FederalTaxableIncome  (  )  )  ; 
fields  .  setFieldProperty  (  "StateIncomeTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "StateIncomeTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "StateIncomeTax"  ,  this  .  get_StateIncomeTax  (  )  )  ; 
fields  .  setFieldProperty  (  "BusEntityPayments"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BusEntityPayments"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BusEntityPayments"  ,  this  .  get_BusEntityPayments  (  )  )  ; 
fields  .  setFieldProperty  (  "LowIncChildDepCare"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "LowIncChildDepCare"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "LowIncChildDepCare"  ,  this  .  get_LowIncChildDepCare  (  )  )  ; 
fields  .  setFieldProperty  (  "Preparer SSN PTIN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Preparer SSN PTIN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Preparer SSN PTIN"  ,  this  .  get_Preparer_SSN_PTIN  (  )  )  ; 
fields  .  setFieldProperty  (  "Check Box9"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Check Box9"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Check Box9"  ,  this  .  get_Check_Box9  (  )  )  ; 
fields  .  setFieldProperty  (  "CapitalGain"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "CapitalGain"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "CapitalGain"  ,  this  .  get_CapitalGain  (  )  )  ; 
fields  .  setFieldProperty  (  "IncomeAdjustment"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "IncomeAdjustment"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "IncomeAdjustment"  ,  this  .  get_IncomeAdjustment  (  )  )  ; 
fields  .  setFieldProperty  (  "Preparer Phone"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Preparer Phone"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Preparer Phone"  ,  this  .  get_Preparer_Phone  (  )  )  ; 
fields  .  setFieldProperty  (  "ZIPCode"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ZIPCode"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ZIPCode"  ,  this  .  get_ZIPCode  (  )  )  ; 
fields  .  setFieldProperty  (  "AddressLine1"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "AddressLine1"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "AddressLine1"  ,  this  .  get_AddressLine1  (  )  )  ; 
fields  .  setFieldProperty  (  "UnderpaymentPenaltyInterest"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "UnderpaymentPenaltyInterest"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "UnderpaymentPenaltyInterest"  ,  this  .  get_UnderpaymentPenaltyInterest  (  )  )  ; 
fields  .  setFieldProperty  (  "OverpaymentCreditedPropTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OverpaymentCreditedPropTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OverpaymentCreditedPropTax"  ,  this  .  get_OverpaymentCreditedPropTax  (  )  )  ; 
fields  .  setFieldProperty  (  "OverpaymentAmount"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OverpaymentAmount"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OverpaymentAmount"  ,  this  .  get_OverpaymentAmount  (  )  )  ; 
fields  .  setFieldProperty  (  "TownCityLegalResidence"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TownCityLegalResidence"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TownCityLegalResidence"  ,  this  .  get_TownCityLegalResidence  (  )  )  ; 
fields  .  setFieldProperty  (  "TaxpayerSSN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TaxpayerSSN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TaxpayerSSN"  ,  this  .  get_TaxpayerSSN  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary TaxpayerSSN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary TaxpayerSSN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary TaxpayerSSN"  ,  this  .  get_Secondary_TaxpayerSSN  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalBalanceDue"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalBalanceDue"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalBalanceDue"  ,  this  .  get_TotalBalanceDue  (  )  )  ; 
fields  .  setFieldProperty  (  "BonusDepreciationAdj"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "BonusDepreciationAdj"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "BonusDepreciationAdj"  ,  this  .  get_BonusDepreciationAdj  (  )  )  ; 
fields  .  setFieldProperty  (  "VTIncomeTaxCredits"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VTIncomeTaxCredits"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VTIncomeTaxCredits"  ,  this  .  get_VTIncomeTaxCredits  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalVTTaxes"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalVTTaxes"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalVTTaxes"  ,  this  .  get_TotalVTTaxes  (  )  )  ; 
fields  .  setFieldProperty  (  "OtherAdditions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OtherAdditions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OtherAdditions"  ,  this  .  get_OtherAdditions  (  )  )  ; 
fields  .  setFieldProperty  (  "EIC"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "EIC"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "EIC"  ,  this  .  get_EIC  (  )  )  ; 
fields  .  setFieldProperty  (  "Secondary LastName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Secondary LastName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Secondary LastName"  ,  this  .  get_Secondary_LastName  (  )  )  ; 
fields  .  setFieldProperty  (  "FirstName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FirstName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FirstName"  ,  this  .  get_FirstName  (  )  )  ; 
fields  .  setFieldProperty  (  "FilingStatus"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "FilingStatus"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "FilingStatus"  ,  this  .  get_FilingStatus  (  )  )  ; 
fields  .  setFieldProperty  (  "NonGameWildlife"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "NonGameWildlife"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "NonGameWildlife"  ,  this  .  get_NonGameWildlife  (  )  )  ; 
fields  .  setFieldProperty  (  "StateMuniInterest"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "StateMuniInterest"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "StateMuniInterest"  ,  this  .  get_StateMuniInterest  (  )  )  ; 
fields  .  setFieldProperty  (  "RefundAmount"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "RefundAmount"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "RefundAmount"  ,  this  .  get_RefundAmount  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalVTTaxesAndContributions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalVTTaxesAndContributions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalVTTaxesAndContributions"  ,  this  .  get_TotalVTTaxesAndContributions  (  )  )  ; 
fields  .  setFieldProperty  (  "EstimatedPaymentTotal"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "EstimatedPaymentTotal"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "EstimatedPaymentTotal"  ,  this  .  get_EstimatedPaymentTotal  (  )  )  ; 
fields  .  setFieldProperty  (  "TaxesPaidOtherStateCreditAmt"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TaxesPaidOtherStateCreditAmt"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TaxesPaidOtherStateCreditAmt"  ,  this  .  get_TaxesPaidOtherStateCreditAmt  (  )  )  ; 
fields  .  setFieldProperty  (  "SpouseCUPartnerSSN"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "SpouseCUPartnerSSN"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "SpouseCUPartnerSSN"  ,  this  .  get_SpouseCUPartnerSSN  (  )  )  ; 
fields  .  setFieldProperty  (  "ItemizedDeductionAddBack"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "ItemizedDeductionAddBack"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "ItemizedDeductionAddBack"  ,  this  .  get_ItemizedDeductionAddBack  (  )  )  ; 
fields  .  setFieldProperty  (  "VTIncomeWithAdditions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VTIncomeWithAdditions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VTIncomeWithAdditions"  ,  this  .  get_VTIncomeWithAdditions  (  )  )  ; 
fields  .  setFieldProperty  (  "PreparerBusinessName"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "PreparerBusinessName"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "PreparerBusinessName"  ,  this  .  get_PreparerBusinessName  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalPaymentsAndCredits"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalPaymentsAndCredits"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalPaymentsAndCredits"  ,  this  .  get_TotalPaymentsAndCredits  (  )  )  ; 
fields  .  setFieldProperty  (  "City"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "City"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "City"  ,  this  .  get_City  (  )  )  ; 
fields  .  setFieldProperty  (  "RenterRebate"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "RenterRebate"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "RenterRebate"  ,  this  .  get_RenterRebate  (  )  )  ; 
fields  .  setFieldProperty  (  "Text16"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "Text16"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "Text16"  ,  this  .  get_Text16  (  )  )  ; 
fields  .  setFieldProperty  (  "InterestUSObligations"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "InterestUSObligations"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "InterestUSObligations"  ,  this  .  get_InterestUSObligations  (  )  )  ; 
fields  .  setFieldProperty  (  "State"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "State"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "State"  ,  this  .  get_State  (  )  )  ; 
fields  .  setFieldProperty  (  "VTIncomeTax"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "VTIncomeTax"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "VTIncomeTax"  ,  this  .  get_VTIncomeTax  (  )  )  ; 
fields  .  setFieldProperty  (  "OverpaymentCreditedNxtYr"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OverpaymentCreditedNxtYr"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OverpaymentCreditedNxtYr"  ,  this  .  get_OverpaymentCreditedNxtYr  (  )  )  ; 
fields  .  setFieldProperty  (  "TotalSubtractions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "TotalSubtractions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "TotalSubtractions"  ,  this  .  get_TotalSubtractions  (  )  )  ; 
fields  .  setFieldProperty  (  "OtherSubtractions"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "OtherSubtractions"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "OtherSubtractions"  ,  this  .  get_OtherSubtractions  (  )  )  ; 
fields  .  setFieldProperty  (  "PreparerFirmIDNumber"  ,  "textsize"  ,  new   Float  (  20.2  )  ,  null  )  ; 
fields  .  setFieldProperty  (  "PreparerFirmIDNumber"  ,  "textfont"  ,  baseFont  ,  null  )  ; 
fields  .  setField  (  "PreparerFirmIDNumber"  ,  this  .  get_PreparerFirmIDNumber  (  )  )  ; 
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




private   String   _VTTaxAfterCredits  =  ""  ; 





public   void   set_VTTaxAfterCredits  (  String   _VTTaxAfterCredits  )  { 
this  .  _VTTaxAfterCredits  =  _VTTaxAfterCredits  ; 
} 





public   String   get_VTTaxAfterCredits  (  )  { 
return   this  .  _VTTaxAfterCredits  ; 
} 




private   String   _VermontCampaignFund  =  ""  ; 





public   void   set_VermontCampaignFund  (  String   _VermontCampaignFund  )  { 
this  .  _VermontCampaignFund  =  _VermontCampaignFund  ; 
} 





public   String   get_VermontCampaignFund  (  )  { 
return   this  .  _VermontCampaignFund  ; 
} 




private   String   _Exemptions  =  ""  ; 





public   void   set_Exemptions  (  String   _Exemptions  )  { 
this  .  _Exemptions  =  _Exemptions  ; 
} 





public   String   get_Exemptions  (  )  { 
return   this  .  _Exemptions  ; 
} 




private   String   _FederalAdjustedGrossIncome  =  ""  ; 





public   void   set_FederalAdjustedGrossIncome  (  String   _FederalAdjustedGrossIncome  )  { 
this  .  _FederalAdjustedGrossIncome  =  _FederalAdjustedGrossIncome  ; 
} 





public   String   get_FederalAdjustedGrossIncome  (  )  { 
return   this  .  _FederalAdjustedGrossIncome  ; 
} 




private   String   _BalanceDueWithReturn  =  ""  ; 





public   void   set_BalanceDueWithReturn  (  String   _BalanceDueWithReturn  )  { 
this  .  _BalanceDueWithReturn  =  _BalanceDueWithReturn  ; 
} 





public   String   get_BalanceDueWithReturn  (  )  { 
return   this  .  _BalanceDueWithReturn  ; 
} 




private   String   _Secondary_FirstName  =  ""  ; 





public   void   set_Secondary_FirstName  (  String   _Secondary_FirstName  )  { 
this  .  _Secondary_FirstName  =  _Secondary_FirstName  ; 
} 





public   String   get_Secondary_FirstName  (  )  { 
return   this  .  _Secondary_FirstName  ; 
} 




private   String   _TotalDonations  =  ""  ; 





public   void   set_TotalDonations  (  String   _TotalDonations  )  { 
this  .  _TotalDonations  =  _TotalDonations  ; 
} 





public   String   get_TotalDonations  (  )  { 
return   this  .  _TotalDonations  ; 
} 




private   String   _LastName  =  ""  ; 





public   void   set_LastName  (  String   _LastName  )  { 
this  .  _LastName  =  _LastName  ; 
} 





public   String   get_LastName  (  )  { 
return   this  .  _LastName  ; 
} 




private   String   _Check_Box1  =  ""  ; 





public   void   set_Check_Box1  (  String   _Check_Box1  )  { 
this  .  _Check_Box1  =  _Check_Box1  ; 
} 





public   String   get_Check_Box1  (  )  { 
return   this  .  _Check_Box1  ; 
} 




private   String   _Check_Box2  =  ""  ; 





public   void   set_Check_Box2  (  String   _Check_Box2  )  { 
this  .  _Check_Box2  =  _Check_Box2  ; 
} 





public   String   get_Check_Box2  (  )  { 
return   this  .  _Check_Box2  ; 
} 




private   String   _AdjustedVTIncomeTax  =  ""  ; 





public   void   set_AdjustedVTIncomeTax  (  String   _AdjustedVTIncomeTax  )  { 
this  .  _AdjustedVTIncomeTax  =  _AdjustedVTIncomeTax  ; 
} 





public   String   get_AdjustedVTIncomeTax  (  )  { 
return   this  .  _AdjustedVTIncomeTax  ; 
} 




private   String   _SpouseCUPartnerName  =  ""  ; 





public   void   set_SpouseCUPartnerName  (  String   _SpouseCUPartnerName  )  { 
this  .  _SpouseCUPartnerName  =  _SpouseCUPartnerName  ; 
} 





public   String   get_SpouseCUPartnerName  (  )  { 
return   this  .  _SpouseCUPartnerName  ; 
} 




private   String   _BonusDepreciation  =  ""  ; 





public   void   set_BonusDepreciation  (  String   _BonusDepreciation  )  { 
this  .  _BonusDepreciation  =  _BonusDepreciation  ; 
} 





public   String   get_BonusDepreciation  (  )  { 
return   this  .  _BonusDepreciation  ; 
} 




private   String   _ChildrensTrustFund  =  ""  ; 





public   void   set_ChildrensTrustFund  (  String   _ChildrensTrustFund  )  { 
this  .  _ChildrensTrustFund  =  _ChildrensTrustFund  ; 
} 





public   String   get_ChildrensTrustFund  (  )  { 
return   this  .  _ChildrensTrustFund  ; 
} 




private   String   _VTTaxableIncome  =  ""  ; 





public   void   set_VTTaxableIncome  (  String   _VTTaxableIncome  )  { 
this  .  _VTTaxableIncome  =  _VTTaxableIncome  ; 
} 





public   String   get_VTTaxableIncome  (  )  { 
return   this  .  _VTTaxableIncome  ; 
} 




private   String   _RealEstateWithholding  =  ""  ; 





public   void   set_RealEstateWithholding  (  String   _RealEstateWithholding  )  { 
this  .  _RealEstateWithholding  =  _RealEstateWithholding  ; 
} 





public   String   get_RealEstateWithholding  (  )  { 
return   this  .  _RealEstateWithholding  ; 
} 




private   String   _SchoolDistrict  =  ""  ; 





public   void   set_SchoolDistrict  (  String   _SchoolDistrict  )  { 
this  .  _SchoolDistrict  =  _SchoolDistrict  ; 
} 





public   String   get_SchoolDistrict  (  )  { 
return   this  .  _SchoolDistrict  ; 
} 




private   String   _FedTaxableWithAdditions  =  ""  ; 





public   void   set_FedTaxableWithAdditions  (  String   _FedTaxableWithAdditions  )  { 
this  .  _FedTaxableWithAdditions  =  _FedTaxableWithAdditions  ; 
} 





public   String   get_FedTaxableWithAdditions  (  )  { 
return   this  .  _FedTaxableWithAdditions  ; 
} 




private   String   _StateofLegalResidence  =  ""  ; 





public   void   set_StateofLegalResidence  (  String   _StateofLegalResidence  )  { 
this  .  _StateofLegalResidence  =  _StateofLegalResidence  ; 
} 





public   String   get_StateofLegalResidence  (  )  { 
return   this  .  _StateofLegalResidence  ; 
} 




private   String   _UseTax  =  ""  ; 





public   void   set_UseTax  (  String   _UseTax  )  { 
this  .  _UseTax  =  _UseTax  ; 
} 





public   String   get_UseTax  (  )  { 
return   this  .  _UseTax  ; 
} 




private   String   _TotalVTCredits  =  ""  ; 





public   void   set_TotalVTCredits  (  String   _TotalVTCredits  )  { 
this  .  _TotalVTCredits  =  _TotalVTCredits  ; 
} 





public   String   get_TotalVTCredits  (  )  { 
return   this  .  _TotalVTCredits  ; 
} 




private   String   _TaxWithheld  =  ""  ; 





public   void   set_TaxWithheld  (  String   _TaxWithheld  )  { 
this  .  _TaxWithheld  =  _TaxWithheld  ; 
} 





public   String   get_TaxWithheld  (  )  { 
return   this  .  _TaxWithheld  ; 
} 




private   String   _FederalTaxableIncome  =  ""  ; 





public   void   set_FederalTaxableIncome  (  String   _FederalTaxableIncome  )  { 
this  .  _FederalTaxableIncome  =  _FederalTaxableIncome  ; 
} 





public   String   get_FederalTaxableIncome  (  )  { 
return   this  .  _FederalTaxableIncome  ; 
} 




private   String   _StateIncomeTax  =  ""  ; 





public   void   set_StateIncomeTax  (  String   _StateIncomeTax  )  { 
this  .  _StateIncomeTax  =  _StateIncomeTax  ; 
} 





public   String   get_StateIncomeTax  (  )  { 
return   this  .  _StateIncomeTax  ; 
} 




private   String   _BusEntityPayments  =  ""  ; 





public   void   set_BusEntityPayments  (  String   _BusEntityPayments  )  { 
this  .  _BusEntityPayments  =  _BusEntityPayments  ; 
} 





public   String   get_BusEntityPayments  (  )  { 
return   this  .  _BusEntityPayments  ; 
} 




private   String   _LowIncChildDepCare  =  ""  ; 





public   void   set_LowIncChildDepCare  (  String   _LowIncChildDepCare  )  { 
this  .  _LowIncChildDepCare  =  _LowIncChildDepCare  ; 
} 





public   String   get_LowIncChildDepCare  (  )  { 
return   this  .  _LowIncChildDepCare  ; 
} 




private   String   _Preparer_SSN_PTIN  =  ""  ; 





public   void   set_Preparer_SSN_PTIN  (  String   _Preparer_SSN_PTIN  )  { 
this  .  _Preparer_SSN_PTIN  =  _Preparer_SSN_PTIN  ; 
} 





public   String   get_Preparer_SSN_PTIN  (  )  { 
return   this  .  _Preparer_SSN_PTIN  ; 
} 




private   String   _Check_Box9  =  ""  ; 





public   void   set_Check_Box9  (  String   _Check_Box9  )  { 
this  .  _Check_Box9  =  _Check_Box9  ; 
} 





public   String   get_Check_Box9  (  )  { 
return   this  .  _Check_Box9  ; 
} 




private   String   _CapitalGain  =  ""  ; 





public   void   set_CapitalGain  (  String   _CapitalGain  )  { 
this  .  _CapitalGain  =  _CapitalGain  ; 
} 





public   String   get_CapitalGain  (  )  { 
return   this  .  _CapitalGain  ; 
} 




private   String   _IncomeAdjustment  =  ""  ; 





public   void   set_IncomeAdjustment  (  String   _IncomeAdjustment  )  { 
this  .  _IncomeAdjustment  =  _IncomeAdjustment  ; 
} 





public   String   get_IncomeAdjustment  (  )  { 
return   this  .  _IncomeAdjustment  ; 
} 




private   String   _Preparer_Phone  =  ""  ; 





public   void   set_Preparer_Phone  (  String   _Preparer_Phone  )  { 
this  .  _Preparer_Phone  =  _Preparer_Phone  ; 
} 





public   String   get_Preparer_Phone  (  )  { 
return   this  .  _Preparer_Phone  ; 
} 




private   String   _ZIPCode  =  ""  ; 





public   void   set_ZIPCode  (  String   _ZIPCode  )  { 
this  .  _ZIPCode  =  _ZIPCode  ; 
} 





public   String   get_ZIPCode  (  )  { 
return   this  .  _ZIPCode  ; 
} 




private   String   _AddressLine1  =  ""  ; 





public   void   set_AddressLine1  (  String   _AddressLine1  )  { 
this  .  _AddressLine1  =  _AddressLine1  ; 
} 





public   String   get_AddressLine1  (  )  { 
return   this  .  _AddressLine1  ; 
} 




private   String   _UnderpaymentPenaltyInterest  =  ""  ; 





public   void   set_UnderpaymentPenaltyInterest  (  String   _UnderpaymentPenaltyInterest  )  { 
this  .  _UnderpaymentPenaltyInterest  =  _UnderpaymentPenaltyInterest  ; 
} 





public   String   get_UnderpaymentPenaltyInterest  (  )  { 
return   this  .  _UnderpaymentPenaltyInterest  ; 
} 




private   String   _OverpaymentCreditedPropTax  =  ""  ; 





public   void   set_OverpaymentCreditedPropTax  (  String   _OverpaymentCreditedPropTax  )  { 
this  .  _OverpaymentCreditedPropTax  =  _OverpaymentCreditedPropTax  ; 
} 





public   String   get_OverpaymentCreditedPropTax  (  )  { 
return   this  .  _OverpaymentCreditedPropTax  ; 
} 




private   String   _OverpaymentAmount  =  ""  ; 





public   void   set_OverpaymentAmount  (  String   _OverpaymentAmount  )  { 
this  .  _OverpaymentAmount  =  _OverpaymentAmount  ; 
} 





public   String   get_OverpaymentAmount  (  )  { 
return   this  .  _OverpaymentAmount  ; 
} 




private   String   _TownCityLegalResidence  =  ""  ; 





public   void   set_TownCityLegalResidence  (  String   _TownCityLegalResidence  )  { 
this  .  _TownCityLegalResidence  =  _TownCityLegalResidence  ; 
} 





public   String   get_TownCityLegalResidence  (  )  { 
return   this  .  _TownCityLegalResidence  ; 
} 




private   String   _TaxpayerSSN  =  ""  ; 





public   void   set_TaxpayerSSN  (  String   _TaxpayerSSN  )  { 
this  .  _TaxpayerSSN  =  _TaxpayerSSN  ; 
} 





public   String   get_TaxpayerSSN  (  )  { 
return   this  .  _TaxpayerSSN  ; 
} 




private   String   _Secondary_TaxpayerSSN  =  ""  ; 





public   void   set_Secondary_TaxpayerSSN  (  String   _Secondary_TaxpayerSSN  )  { 
this  .  _Secondary_TaxpayerSSN  =  _Secondary_TaxpayerSSN  ; 
} 





public   String   get_Secondary_TaxpayerSSN  (  )  { 
return   this  .  _Secondary_TaxpayerSSN  ; 
} 




private   String   _TotalBalanceDue  =  ""  ; 





public   void   set_TotalBalanceDue  (  String   _TotalBalanceDue  )  { 
this  .  _TotalBalanceDue  =  _TotalBalanceDue  ; 
} 





public   String   get_TotalBalanceDue  (  )  { 
return   this  .  _TotalBalanceDue  ; 
} 




private   String   _BonusDepreciationAdj  =  ""  ; 





public   void   set_BonusDepreciationAdj  (  String   _BonusDepreciationAdj  )  { 
this  .  _BonusDepreciationAdj  =  _BonusDepreciationAdj  ; 
} 





public   String   get_BonusDepreciationAdj  (  )  { 
return   this  .  _BonusDepreciationAdj  ; 
} 




private   String   _VTIncomeTaxCredits  =  ""  ; 





public   void   set_VTIncomeTaxCredits  (  String   _VTIncomeTaxCredits  )  { 
this  .  _VTIncomeTaxCredits  =  _VTIncomeTaxCredits  ; 
} 





public   String   get_VTIncomeTaxCredits  (  )  { 
return   this  .  _VTIncomeTaxCredits  ; 
} 




private   String   _TotalVTTaxes  =  ""  ; 





public   void   set_TotalVTTaxes  (  String   _TotalVTTaxes  )  { 
this  .  _TotalVTTaxes  =  _TotalVTTaxes  ; 
} 





public   String   get_TotalVTTaxes  (  )  { 
return   this  .  _TotalVTTaxes  ; 
} 




private   String   _OtherAdditions  =  ""  ; 





public   void   set_OtherAdditions  (  String   _OtherAdditions  )  { 
this  .  _OtherAdditions  =  _OtherAdditions  ; 
} 





public   String   get_OtherAdditions  (  )  { 
return   this  .  _OtherAdditions  ; 
} 




private   String   _EIC  =  ""  ; 





public   void   set_EIC  (  String   _EIC  )  { 
this  .  _EIC  =  _EIC  ; 
} 





public   String   get_EIC  (  )  { 
return   this  .  _EIC  ; 
} 




private   String   _Secondary_LastName  =  ""  ; 





public   void   set_Secondary_LastName  (  String   _Secondary_LastName  )  { 
this  .  _Secondary_LastName  =  _Secondary_LastName  ; 
} 





public   String   get_Secondary_LastName  (  )  { 
return   this  .  _Secondary_LastName  ; 
} 




private   String   _FirstName  =  ""  ; 





public   void   set_FirstName  (  String   _FirstName  )  { 
this  .  _FirstName  =  _FirstName  ; 
} 





public   String   get_FirstName  (  )  { 
return   this  .  _FirstName  ; 
} 




private   String   _FilingStatus  =  ""  ; 





public   void   set_FilingStatus  (  String   _FilingStatus  )  { 
this  .  _FilingStatus  =  _FilingStatus  ; 
} 





public   String   get_FilingStatus  (  )  { 
return   this  .  _FilingStatus  ; 
} 




private   String   _NonGameWildlife  =  ""  ; 





public   void   set_NonGameWildlife  (  String   _NonGameWildlife  )  { 
this  .  _NonGameWildlife  =  _NonGameWildlife  ; 
} 





public   String   get_NonGameWildlife  (  )  { 
return   this  .  _NonGameWildlife  ; 
} 




private   String   _StateMuniInterest  =  ""  ; 





public   void   set_StateMuniInterest  (  String   _StateMuniInterest  )  { 
this  .  _StateMuniInterest  =  _StateMuniInterest  ; 
} 





public   String   get_StateMuniInterest  (  )  { 
return   this  .  _StateMuniInterest  ; 
} 




private   String   _RefundAmount  =  ""  ; 





public   void   set_RefundAmount  (  String   _RefundAmount  )  { 
this  .  _RefundAmount  =  _RefundAmount  ; 
} 





public   String   get_RefundAmount  (  )  { 
return   this  .  _RefundAmount  ; 
} 




private   String   _TotalVTTaxesAndContributions  =  ""  ; 





public   void   set_TotalVTTaxesAndContributions  (  String   _TotalVTTaxesAndContributions  )  { 
this  .  _TotalVTTaxesAndContributions  =  _TotalVTTaxesAndContributions  ; 
} 





public   String   get_TotalVTTaxesAndContributions  (  )  { 
return   this  .  _TotalVTTaxesAndContributions  ; 
} 




private   String   _EstimatedPaymentTotal  =  ""  ; 





public   void   set_EstimatedPaymentTotal  (  String   _EstimatedPaymentTotal  )  { 
this  .  _EstimatedPaymentTotal  =  _EstimatedPaymentTotal  ; 
} 





public   String   get_EstimatedPaymentTotal  (  )  { 
return   this  .  _EstimatedPaymentTotal  ; 
} 




private   String   _TaxesPaidOtherStateCreditAmt  =  ""  ; 





public   void   set_TaxesPaidOtherStateCreditAmt  (  String   _TaxesPaidOtherStateCreditAmt  )  { 
this  .  _TaxesPaidOtherStateCreditAmt  =  _TaxesPaidOtherStateCreditAmt  ; 
} 





public   String   get_TaxesPaidOtherStateCreditAmt  (  )  { 
return   this  .  _TaxesPaidOtherStateCreditAmt  ; 
} 




private   String   _SpouseCUPartnerSSN  =  ""  ; 





public   void   set_SpouseCUPartnerSSN  (  String   _SpouseCUPartnerSSN  )  { 
this  .  _SpouseCUPartnerSSN  =  _SpouseCUPartnerSSN  ; 
} 





public   String   get_SpouseCUPartnerSSN  (  )  { 
return   this  .  _SpouseCUPartnerSSN  ; 
} 




private   String   _ItemizedDeductionAddBack  =  ""  ; 





public   void   set_ItemizedDeductionAddBack  (  String   _ItemizedDeductionAddBack  )  { 
this  .  _ItemizedDeductionAddBack  =  _ItemizedDeductionAddBack  ; 
} 





public   String   get_ItemizedDeductionAddBack  (  )  { 
return   this  .  _ItemizedDeductionAddBack  ; 
} 




private   String   _VTIncomeWithAdditions  =  ""  ; 





public   void   set_VTIncomeWithAdditions  (  String   _VTIncomeWithAdditions  )  { 
this  .  _VTIncomeWithAdditions  =  _VTIncomeWithAdditions  ; 
} 





public   String   get_VTIncomeWithAdditions  (  )  { 
return   this  .  _VTIncomeWithAdditions  ; 
} 




private   String   _PreparerBusinessName  =  ""  ; 





public   void   set_PreparerBusinessName  (  String   _PreparerBusinessName  )  { 
this  .  _PreparerBusinessName  =  _PreparerBusinessName  ; 
} 





public   String   get_PreparerBusinessName  (  )  { 
return   this  .  _PreparerBusinessName  ; 
} 




private   String   _TotalPaymentsAndCredits  =  ""  ; 





public   void   set_TotalPaymentsAndCredits  (  String   _TotalPaymentsAndCredits  )  { 
this  .  _TotalPaymentsAndCredits  =  _TotalPaymentsAndCredits  ; 
} 





public   String   get_TotalPaymentsAndCredits  (  )  { 
return   this  .  _TotalPaymentsAndCredits  ; 
} 




private   String   _City  =  ""  ; 





public   void   set_City  (  String   _City  )  { 
this  .  _City  =  _City  ; 
} 





public   String   get_City  (  )  { 
return   this  .  _City  ; 
} 




private   String   _RenterRebate  =  ""  ; 





public   void   set_RenterRebate  (  String   _RenterRebate  )  { 
this  .  _RenterRebate  =  _RenterRebate  ; 
} 





public   String   get_RenterRebate  (  )  { 
return   this  .  _RenterRebate  ; 
} 




private   String   _Text16  =  ""  ; 





public   void   set_Text16  (  String   _Text16  )  { 
this  .  _Text16  =  _Text16  ; 
} 





public   String   get_Text16  (  )  { 
return   this  .  _Text16  ; 
} 




private   String   _InterestUSObligations  =  ""  ; 





public   void   set_InterestUSObligations  (  String   _InterestUSObligations  )  { 
this  .  _InterestUSObligations  =  _InterestUSObligations  ; 
} 





public   String   get_InterestUSObligations  (  )  { 
return   this  .  _InterestUSObligations  ; 
} 




private   String   _State  =  ""  ; 





public   void   set_State  (  String   _State  )  { 
this  .  _State  =  _State  ; 
} 





public   String   get_State  (  )  { 
return   this  .  _State  ; 
} 




private   String   _VTIncomeTax  =  ""  ; 





public   void   set_VTIncomeTax  (  String   _VTIncomeTax  )  { 
this  .  _VTIncomeTax  =  _VTIncomeTax  ; 
} 





public   String   get_VTIncomeTax  (  )  { 
return   this  .  _VTIncomeTax  ; 
} 




private   String   _OverpaymentCreditedNxtYr  =  ""  ; 





public   void   set_OverpaymentCreditedNxtYr  (  String   _OverpaymentCreditedNxtYr  )  { 
this  .  _OverpaymentCreditedNxtYr  =  _OverpaymentCreditedNxtYr  ; 
} 





public   String   get_OverpaymentCreditedNxtYr  (  )  { 
return   this  .  _OverpaymentCreditedNxtYr  ; 
} 




private   String   _TotalSubtractions  =  ""  ; 





public   void   set_TotalSubtractions  (  String   _TotalSubtractions  )  { 
this  .  _TotalSubtractions  =  _TotalSubtractions  ; 
} 





public   String   get_TotalSubtractions  (  )  { 
return   this  .  _TotalSubtractions  ; 
} 




private   String   _OtherSubtractions  =  ""  ; 





public   void   set_OtherSubtractions  (  String   _OtherSubtractions  )  { 
this  .  _OtherSubtractions  =  _OtherSubtractions  ; 
} 





public   String   get_OtherSubtractions  (  )  { 
return   this  .  _OtherSubtractions  ; 
} 




private   String   _PreparerFirmIDNumber  =  ""  ; 





public   void   set_PreparerFirmIDNumber  (  String   _PreparerFirmIDNumber  )  { 
this  .  _PreparerFirmIDNumber  =  _PreparerFirmIDNumber  ; 
} 





public   String   get_PreparerFirmIDNumber  (  )  { 
return   this  .  _PreparerFirmIDNumber  ; 
} 
} 

