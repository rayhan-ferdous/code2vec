package   org  .  psu  .  newhall  .  ui  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  table  .  TableModel  ; 
import   org  .  psu  .  newhall  .  Newhall  ; 
import   org  .  psu  .  newhall  .  sim  .  BASICSimulationModel  ; 
import   org  .  psu  .  newhall  .  sim  .  NewhallDataset  ; 
import   org  .  psu  .  newhall  .  sim  .  NewhallResults  ; 
import   org  .  psu  .  newhall  .  util  .  CSVFileParser  ; 
import   org  .  psu  .  newhall  .  util  .  CSVResultsExporter  ; 
import   org  .  psu  .  newhall  .  util  .  XMLFileParser  ; 
import   org  .  psu  .  newhall  .  util  .  XMLResultsExporter  ; 
import   org  .  psu  .  newhall  .  util  .  XMLStringResultsExporter  ; 

public   class   DefaultNewhallFrame   extends   javax  .  swing  .  JFrame  { 

private   NewhallDataset   nd  ; 

private   NewhallResults   nr  ; 

private   boolean   inMetric  =  true  ; 

private   boolean   loaded  =  false  ; 

public   DefaultNewhallFrame  (  )  { 
initComponents  (  )  ; 
this  .  setTitle  (  "Newhall "  +  Newhall  .  NSM_VERSION  )  ; 
this  .  whcSpinner  .  setValue  (  200.0  )  ; 
this  .  whcSpinner  .  setEnabled  (  false  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
jSeparator1  =  new   javax  .  swing  .  JSeparator  (  )  ; 
datasetScrollPane2  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
rainfallTable1  =  new   javax  .  swing  .  JTable  (  )  ; 
tabPane  =  new   javax  .  swing  .  JTabbedPane  (  )  ; 
inputPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
datasetPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
datasetScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
rainfallTable  =  new   javax  .  swing  .  JTable  (  )  ; 
datasetScrollPane3  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
tempTable  =  new   javax  .  swing  .  JTable  (  )  ; 
stationLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
stationText  =  new   javax  .  swing  .  JLabel  (  )  ; 
elevationLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
elevationText  =  new   javax  .  swing  .  JLabel  (  )  ; 
latitudeLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
latitudeText  =  new   javax  .  swing  .  JLabel  (  )  ; 
longitudeLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
longitudeText  =  new   javax  .  swing  .  JLabel  (  )  ; 
countryLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
countryText  =  new   javax  .  swing  .  JLabel  (  )  ; 
startYearLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
startingYearText  =  new   javax  .  swing  .  JLabel  (  )  ; 
endingYearLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
endingYearText  =  new   javax  .  swing  .  JLabel  (  )  ; 
srcUnitSystemLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
srcUnitSystem  =  new   javax  .  swing  .  JLabel  (  )  ; 
jSeparator2  =  new   javax  .  swing  .  JSeparator  (  )  ; 
soilAirOffsetLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
soilAirOffset  =  new   javax  .  swing  .  JSpinner  (  )  ; 
soilAirOffsetUnits  =  new   javax  .  swing  .  JLabel  (  )  ; 
jSeparator3  =  new   javax  .  swing  .  JSeparator  (  )  ; 
jLabel21  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel23  =  new   javax  .  swing  .  JLabel  (  )  ; 
amplitudeSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
calendarPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
tempCalPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
jScrollPane4  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea3  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jScrollPane5  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea4  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jScrollPane6  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
temperatureCalendarText  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jLabel8  =  new   javax  .  swing  .  JLabel  (  )  ; 
moistCalPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
jScrollPane2  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea1  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jScrollPane3  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea2  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
moistureCalendarText  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jLabel7  =  new   javax  .  swing  .  JLabel  (  )  ; 
modelResultsPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
datasetScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mpeTable  =  new   javax  .  swing  .  JTable  (  )  ; 
moistureRegimeText  =  new   javax  .  swing  .  JLabel  (  )  ; 
temperatureRegimeText  =  new   javax  .  swing  .  JLabel  (  )  ; 
annualRainfallText  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel3  =  new   javax  .  swing  .  JPanel  (  )  ; 
jScrollPane7  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
statisticsText  =  new   javax  .  swing  .  JTextArea  (  )  ; 
subdivisionsLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
subdivisionsText  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
stationNameLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel9  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel10  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel11  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel12  =  new   javax  .  swing  .  JLabel  (  )  ; 
contributorPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel13  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel14  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel15  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel16  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel17  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel18  =  new   javax  .  swing  .  JLabel  (  )  ; 
stateProvLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
postalLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
contribCountryLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
emailLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
phoneLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
firstName  =  new   javax  .  swing  .  JLabel  (  )  ; 
lastName  =  new   javax  .  swing  .  JLabel  (  )  ; 
title  =  new   javax  .  swing  .  JLabel  (  )  ; 
orginization  =  new   javax  .  swing  .  JLabel  (  )  ; 
address  =  new   javax  .  swing  .  JLabel  (  )  ; 
city  =  new   javax  .  swing  .  JLabel  (  )  ; 
stateProv  =  new   javax  .  swing  .  JLabel  (  )  ; 
postal  =  new   javax  .  swing  .  JLabel  (  )  ; 
country  =  new   javax  .  swing  .  JLabel  (  )  ; 
email  =  new   javax  .  swing  .  JLabel  (  )  ; 
phone  =  new   javax  .  swing  .  JLabel  (  )  ; 
notesPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
jScrollPane8  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
notes  =  new   javax  .  swing  .  JTextArea  (  )  ; 
stationName  =  new   javax  .  swing  .  JLabel  (  )  ; 
stationId  =  new   javax  .  swing  .  JLabel  (  )  ; 
elevation  =  new   javax  .  swing  .  JLabel  (  )  ; 
stationStateProv  =  new   javax  .  swing  .  JLabel  (  )  ; 
stationCountry  =  new   javax  .  swing  .  JLabel  (  )  ; 
mlraName  =  new   javax  .  swing  .  JLabel  (  )  ; 
mlraId  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel6  =  new   javax  .  swing  .  JLabel  (  )  ; 
whcSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
whcUnitsText  =  new   javax  .  swing  .  JLabel  (  )  ; 
exportXmlButton  =  new   javax  .  swing  .  JButton  (  )  ; 
exportCsvButton  =  new   javax  .  swing  .  JButton  (  )  ; 
activeUnitSystemLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
activeUnitSystemText  =  new   javax  .  swing  .  JLabel  (  )  ; 
menuBar  =  new   javax  .  swing  .  JMenuBar  (  )  ; 
fileMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
openDatasetMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
exitMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
optionsMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
toggleUnitsMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
helpMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
aboutMenuItem  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
rainfallTable1  .  setFont  (  rainfallTable1  .  getFont  (  )  )  ; 
rainfallTable1  .  setModel  (  new   javax  .  swing  .  table  .  DefaultTableModel  (  new   Object  [  ]  [  ]  {  {  ""  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  }  ,  {  ""  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  }  }  ,  new   String  [  ]  {  " "  ,  "Jan"  ,  "Feb"  ,  "Mar"  ,  "Apr"  ,  "May"  ,  "Jun"  ,  "Jul"  ,  "Aug"  ,  "Sep"  ,  "Oct"  ,  "Nov"  ,  "Dec"  }  )  { 

boolean  [  ]  canEdit  =  new   boolean  [  ]  {  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  }  ; 

public   boolean   isCellEditable  (  int   rowIndex  ,  int   columnIndex  )  { 
return   canEdit  [  columnIndex  ]  ; 
} 
}  )  ; 
rainfallTable1  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
datasetScrollPane2  .  setViewportView  (  rainfallTable1  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  EXIT_ON_CLOSE  )  ; 
datasetPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Dataset"  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_JUSTIFICATION  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  )  )  ; 
datasetPanel  .  setFont  (  datasetPanel  .  getFont  (  )  )  ; 
rainfallTable  .  setFont  (  rainfallTable  .  getFont  (  )  )  ; 
rainfallTable  .  setModel  (  new   javax  .  swing  .  table  .  DefaultTableModel  (  new   Object  [  ]  [  ]  {  {  ""  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  }  }  ,  new   String  [  ]  {  " "  ,  "Jan"  ,  "Feb"  ,  "Mar"  ,  "Apr"  ,  "May"  ,  "Jun"  ,  "Jul"  ,  "Aug"  ,  "Sep"  ,  "Oct"  ,  "Nov"  ,  "Dec"  }  )  { 

boolean  [  ]  canEdit  =  new   boolean  [  ]  {  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  }  ; 

public   boolean   isCellEditable  (  int   rowIndex  ,  int   columnIndex  )  { 
return   canEdit  [  columnIndex  ]  ; 
} 
}  )  ; 
rainfallTable  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
datasetScrollPane  .  setViewportView  (  rainfallTable  )  ; 
tempTable  .  setFont  (  tempTable  .  getFont  (  )  )  ; 
tempTable  .  setModel  (  new   javax  .  swing  .  table  .  DefaultTableModel  (  new   Object  [  ]  [  ]  {  {  ""  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  }  }  ,  new   String  [  ]  {  " "  ,  "Jan"  ,  "Feb"  ,  "Mar"  ,  "Apr"  ,  "May"  ,  "Jun"  ,  "Jul"  ,  "Aug"  ,  "Sep"  ,  "Oct"  ,  "Nov"  ,  "Dec"  }  )  { 

boolean  [  ]  canEdit  =  new   boolean  [  ]  {  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  }  ; 

public   boolean   isCellEditable  (  int   rowIndex  ,  int   columnIndex  )  { 
return   canEdit  [  columnIndex  ]  ; 
} 
}  )  ; 
tempTable  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
datasetScrollPane3  .  setViewportView  (  tempTable  )  ; 
javax  .  swing  .  GroupLayout   datasetPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  datasetPanel  )  ; 
datasetPanel  .  setLayout  (  datasetPanelLayout  )  ; 
datasetPanelLayout  .  setHorizontalGroup  (  datasetPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  datasetPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  datasetPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  datasetScrollPane3  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  895  ,  Short  .  MAX_VALUE  )  .  addComponent  (  datasetScrollPane  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  895  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  )  ; 
datasetPanelLayout  .  setVerticalGroup  (  datasetPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  datasetPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  datasetScrollPane  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  85  ,  Short  .  MAX_VALUE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  datasetScrollPane3  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  86  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
stationLabel  .  setFont  (  stationLabel  .  getFont  (  )  )  ; 
stationLabel  .  setText  (  "Station:"  )  ; 
stationText  .  setFont  (  stationText  .  getFont  (  )  )  ; 
stationText  .  setText  (  " << No Dataset Loaded >>"  )  ; 
elevationLabel  .  setFont  (  elevationLabel  .  getFont  (  )  )  ; 
elevationLabel  .  setText  (  "Elevation:"  )  ; 
elevationText  .  setFont  (  elevationText  .  getFont  (  )  )  ; 
elevationText  .  setText  (  " "  )  ; 
latitudeLabel  .  setFont  (  latitudeLabel  .  getFont  (  )  )  ; 
latitudeLabel  .  setText  (  "Latitude:"  )  ; 
latitudeText  .  setFont  (  latitudeText  .  getFont  (  )  )  ; 
latitudeText  .  setText  (  " "  )  ; 
longitudeLabel  .  setFont  (  longitudeLabel  .  getFont  (  )  )  ; 
longitudeLabel  .  setText  (  "Longitude:"  )  ; 
longitudeText  .  setFont  (  longitudeText  .  getFont  (  )  )  ; 
longitudeText  .  setText  (  " "  )  ; 
countryLabel  .  setText  (  "Station Country:"  )  ; 
countryText  .  setText  (  " "  )  ; 
startYearLabel  .  setText  (  "Starting Year:"  )  ; 
startingYearText  .  setText  (  " "  )  ; 
endingYearLabel  .  setText  (  "Ending Year:"  )  ; 
endingYearText  .  setText  (  " "  )  ; 
srcUnitSystemLabel  .  setText  (  "Source Unit System:"  )  ; 
srcUnitSystem  .  setText  (  " "  )  ; 
soilAirOffsetLabel  .  setText  (  "Soil-Air Temp Offset:"  )  ; 
soilAirOffset  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  1.2d  ,  0.0d  ,  20.0d  ,  0.1d  )  )  ; 
soilAirOffset  .  setEnabled  (  false  )  ; 
soilAirOffset  .  setVerifyInputWhenFocusTarget  (  false  )  ; 
soilAirOffset  .  addChangeListener  (  new   javax  .  swing  .  event  .  ChangeListener  (  )  { 

public   void   stateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
soilAirOffsetStateChanged  (  evt  )  ; 
} 
}  )  ; 
soilAirOffsetUnits  .  setText  (  (  char  )  176  +  "C"  )  ; 
jLabel21  .  setText  (  "Amplitude:"  )  ; 
jLabel23  .  setText  (  "greater than air temperature"  )  ; 
amplitudeSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  0.66d  ,  0.0d  ,  1.0d  ,  0.01d  )  )  ; 
amplitudeSpinner  .  setEnabled  (  false  )  ; 
amplitudeSpinner  .  addChangeListener  (  new   javax  .  swing  .  event  .  ChangeListener  (  )  { 

public   void   stateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
amplitudeSpinnerStateChanged  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   inputPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  inputPanel  )  ; 
inputPanel  .  setLayout  (  inputPanelLayout  )  ; 
inputPanelLayout  .  setHorizontalGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  datasetPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  countryLabel  )  .  addComponent  (  stationLabel  )  .  addComponent  (  elevationLabel  )  .  addComponent  (  latitudeLabel  )  .  addComponent  (  longitudeLabel  )  )  .  addGap  (  18  ,  18  ,  18  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  false  )  .  addComponent  (  longitudeText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  latitudeText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  elevationText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  stationText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  countryText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  191  ,  Short  .  MAX_VALUE  )  )  .  addGap  (  18  ,  223  ,  Short  .  MAX_VALUE  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  srcUnitSystemLabel  )  .  addComponent  (  endingYearLabel  )  .  addComponent  (  startYearLabel  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  false  )  .  addComponent  (  startingYearText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  endingYearText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  49  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addComponent  (  srcUnitSystem  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  86  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  167  ,  167  ,  167  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jSeparator2  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  931  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  soilAirOffsetLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  soilAirOffset  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  57  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  soilAirOffsetUnits  )  .  addGap  (  5  ,  5  ,  5  )  .  addComponent  (  jLabel23  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  257  ,  Short  .  MAX_VALUE  )  .  addComponent  (  jLabel21  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  amplitudeSpinner  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  60  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jSeparator3  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  931  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  )  )  ; 
inputPanelLayout  .  setVerticalGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  inputPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  stationLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  14  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  countryLabel  )  .  addComponent  (  countryText  )  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  stationText  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  endingYearLabel  )  )  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  startingYearText  )  .  addComponent  (  startYearLabel  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  endingYearText  )  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  inputPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  elevationLabel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  elevationText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  srcUnitSystemLabel  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  latitudeLabel  )  .  addComponent  (  latitudeText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  14  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  longitudeLabel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  longitudeText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  14  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addComponent  (  srcUnitSystem  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jSeparator2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  10  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  inputPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  soilAirOffsetLabel  )  .  addComponent  (  soilAirOffset  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  soilAirOffsetUnits  )  .  addComponent  (  jLabel23  )  .  addComponent  (  jLabel21  )  .  addComponent  (  amplitudeSpinner  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jSeparator3  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  10  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  datasetPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
tabPane  .  addTab  (  "Input"  ,  inputPanel  )  ; 
tempCalPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Temperature Calendar"  )  )  ; 
jTextArea3  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
jTextArea3  .  setColumns  (  30  )  ; 
jTextArea3  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
jTextArea3  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
jTextArea3  .  setRows  (  1  )  ; 
jTextArea3  .  setText  (  "1''''''''''''15'''''''''''''30"  )  ; 
jTextArea3  .  setBorder  (  null  )  ; 
jScrollPane4  .  setViewportView  (  jTextArea3  )  ; 
jTextArea4  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
jTextArea4  .  setColumns  (  5  )  ; 
jTextArea4  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
jTextArea4  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
jTextArea4  .  setRows  (  12  )  ; 
jTextArea4  .  setText  (  " JAN\n FEB\n MAR\n APR\n MAY\n JUN\n JUL\n AUG\n SEP\n OCT\n NOV\n DEC"  )  ; 
jTextArea4  .  setBorder  (  null  )  ; 
jScrollPane5  .  setViewportView  (  jTextArea4  )  ; 
temperatureCalendarText  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
temperatureCalendarText  .  setColumns  (  30  )  ; 
temperatureCalendarText  .  setEditable  (  false  )  ; 
temperatureCalendarText  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
temperatureCalendarText  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
temperatureCalendarText  .  setRows  (  12  )  ; 
temperatureCalendarText  .  setBorder  (  null  )  ; 
jScrollPane6  .  setViewportView  (  temperatureCalendarText  )  ; 
jLabel8  .  setFont  (  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  )  ; 
jLabel8  .  setText  (  "- = Under 5"  +  (  char  )  176  +  "C, 5 = 5"  +  (  char  )  176  +  "C to 8"  +  (  char  )  176  +  "C, 8 = Excess of 8"  +  (  char  )  176  +  "C"  )  ; 
javax  .  swing  .  GroupLayout   tempCalPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  tempCalPanel  )  ; 
tempCalPanel  .  setLayout  (  tempCalPanelLayout  )  ; 
tempCalPanelLayout  .  setHorizontalGroup  (  tempCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  tempCalPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  tempCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  tempCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane5  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  tempCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jScrollPane6  )  .  addComponent  (  jScrollPane4  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  9  ,  9  ,  9  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  tempCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel8  )  .  addContainerGap  (  )  )  )  )  )  ; 
tempCalPanelLayout  .  setVerticalGroup  (  tempCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  tempCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane4  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  tempCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  false  )  .  addComponent  (  jScrollPane6  )  .  addComponent  (  jScrollPane5  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel8  )  )  )  ; 
moistCalPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Moisture Calendar"  )  )  ; 
jTextArea1  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
jTextArea1  .  setColumns  (  30  )  ; 
jTextArea1  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
jTextArea1  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
jTextArea1  .  setRows  (  1  )  ; 
jTextArea1  .  setText  (  "1''''''''''''15'''''''''''''30"  )  ; 
jTextArea1  .  setBorder  (  null  )  ; 
jScrollPane2  .  setViewportView  (  jTextArea1  )  ; 
jTextArea2  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
jTextArea2  .  setColumns  (  5  )  ; 
jTextArea2  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
jTextArea2  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
jTextArea2  .  setRows  (  12  )  ; 
jTextArea2  .  setText  (  " JAN\n FEB\n MAR\n APR\n MAY\n JUN\n JUL\n AUG\n SEP\n OCT\n NOV\n DEC"  )  ; 
jTextArea2  .  setBorder  (  null  )  ; 
jScrollPane3  .  setViewportView  (  jTextArea2  )  ; 
moistureCalendarText  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
moistureCalendarText  .  setColumns  (  30  )  ; 
moistureCalendarText  .  setEditable  (  false  )  ; 
moistureCalendarText  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  14  )  )  ; 
moistureCalendarText  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
moistureCalendarText  .  setRows  (  12  )  ; 
moistureCalendarText  .  setBorder  (  null  )  ; 
jScrollPane1  .  setViewportView  (  moistureCalendarText  )  ; 
jLabel7  .  setFont  (  new   java  .  awt  .  Font  (  "SansSerif"  ,  0  ,  11  )  )  ; 
jLabel7  .  setText  (  "1 = Dry, 2 = Moist/Dry, 3 = Moist"  )  ; 
javax  .  swing  .  GroupLayout   moistCalPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  moistCalPanel  )  ; 
moistCalPanel  .  setLayout  (  moistCalPanelLayout  )  ; 
moistCalPanelLayout  .  setHorizontalGroup  (  moistCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  moistCalPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  moistCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  moistCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  moistCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane3  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  moistCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  moistCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel7  )  .  addContainerGap  (  )  )  )  )  )  ; 
moistCalPanelLayout  .  setVerticalGroup  (  moistCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  moistCalPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  moistCalPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  false  )  .  addComponent  (  jScrollPane1  )  .  addComponent  (  jScrollPane3  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  jLabel7  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  17  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  ; 
javax  .  swing  .  GroupLayout   calendarPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  calendarPanel  )  ; 
calendarPanel  .  setLayout  (  calendarPanelLayout  )  ; 
calendarPanelLayout  .  setHorizontalGroup  (  calendarPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  calendarPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  tempCalPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  282  ,  Short  .  MAX_VALUE  )  .  addComponent  (  moistCalPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
calendarPanelLayout  .  setVerticalGroup  (  calendarPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  calendarPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  calendarPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addGroup  (  calendarPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  tempCalPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  calendarPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  moistCalPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addContainerGap  (  129  ,  Short  .  MAX_VALUE  )  )  )  ; 
tabPane  .  addTab  (  "Calendars"  ,  calendarPanel  )  ; 
modelResultsPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Model Results"  )  )  ; 
jLabel3  .  setText  (  "Annual Rainfall:"  )  ; 
jLabel4  .  setText  (  "Temperature Regime:"  )  ; 
jLabel5  .  setText  (  "Moisture Regime:"  )  ; 
mpeTable  .  setFont  (  mpeTable  .  getFont  (  )  )  ; 
mpeTable  .  setModel  (  new   javax  .  swing  .  table  .  DefaultTableModel  (  new   Object  [  ]  [  ]  {  {  ""  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  null  }  }  ,  new   String  [  ]  {  " "  ,  "Jan"  ,  "Feb"  ,  "Mar"  ,  "Apr"  ,  "May"  ,  "Jun"  ,  "Jul"  ,  "Aug"  ,  "Sep"  ,  "Oct"  ,  "Nov"  ,  "Dec"  }  )  { 

boolean  [  ]  canEdit  =  new   boolean  [  ]  {  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  ,  false  }  ; 

public   boolean   isCellEditable  (  int   rowIndex  ,  int   columnIndex  )  { 
return   canEdit  [  columnIndex  ]  ; 
} 
}  )  ; 
mpeTable  .  setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
datasetScrollPane1  .  setViewportView  (  mpeTable  )  ; 
moistureRegimeText  .  setText  (  " "  )  ; 
temperatureRegimeText  .  setText  (  " "  )  ; 
annualRainfallText  .  setText  (  " "  )  ; 
jPanel3  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Extended Statistics"  )  )  ; 
statisticsText  .  setBackground  (  new   java  .  awt  .  Color  (  0  ,  0  ,  0  )  )  ; 
statisticsText  .  setColumns  (  20  )  ; 
statisticsText  .  setEditable  (  false  )  ; 
statisticsText  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  11  )  )  ; 
statisticsText  .  setForeground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
statisticsText  .  setRows  (  15  )  ; 
jScrollPane7  .  setViewportView  (  statisticsText  )  ; 
javax  .  swing  .  GroupLayout   jPanel3Layout  =  new   javax  .  swing  .  GroupLayout  (  jPanel3  )  ; 
jPanel3  .  setLayout  (  jPanel3Layout  )  ; 
jPanel3Layout  .  setHorizontalGroup  (  jPanel3Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel3Layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  jScrollPane7  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  883  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
jPanel3Layout  .  setVerticalGroup  (  jPanel3Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel3Layout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane7  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  269  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
subdivisionsLabel  .  setText  (  "Subdivisions:"  )  ; 
subdivisionsText  .  setText  (  " "  )  ; 
javax  .  swing  .  GroupLayout   modelResultsPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  modelResultsPanel  )  ; 
modelResultsPanel  .  setLayout  (  modelResultsPanelLayout  )  ; 
modelResultsPanelLayout  .  setHorizontalGroup  (  modelResultsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  modelResultsPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  modelResultsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jPanel3  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  datasetScrollPane1  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  919  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  modelResultsPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel3  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  annualRainfallText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  78  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel4  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  temperatureRegimeText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  125  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel5  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  moistureRegimeText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  92  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  subdivisionsLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  subdivisionsText  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  113  ,  Short  .  MAX_VALUE  )  )  )  .  addContainerGap  (  )  )  )  ; 
modelResultsPanelLayout  .  setVerticalGroup  (  modelResultsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  modelResultsPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  modelResultsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel3  )  .  addComponent  (  temperatureRegimeText  )  .  addComponent  (  annualRainfallText  )  .  addComponent  (  subdivisionsText  )  .  addComponent  (  jLabel5  )  .  addComponent  (  moistureRegimeText  )  .  addComponent  (  subdivisionsLabel  )  .  addComponent  (  jLabel4  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  datasetScrollPane1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  45  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jPanel3  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
tabPane  .  addTab  (  "Results"  ,  modelResultsPanel  )  ; 
stationNameLabel  .  setText  (  "Station Name:"  )  ; 
jLabel1  .  setText  (  "Station ID:"  )  ; 
jLabel2  .  setText  (  "Elevation:"  )  ; 
jLabel9  .  setText  (  "State/Prov:"  )  ; 
jLabel10  .  setText  (  "Country:"  )  ; 
jLabel11  .  setText  (  "MLRA Name:"  )  ; 
jLabel12  .  setText  (  "MLRA ID:"  )  ; 
contributorPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Contributor"  )  )  ; 
jLabel13  .  setText  (  "First Name:"  )  ; 
jLabel14  .  setText  (  "Last Name:"  )  ; 
jLabel15  .  setText  (  "Title:"  )  ; 
jLabel16  .  setText  (  "Org:"  )  ; 
jLabel17  .  setText  (  "Address:"  )  ; 
jLabel18  .  setText  (  "City:"  )  ; 
stateProvLabel  .  setText  (  "State/Prov:"  )  ; 
postalLabel  .  setText  (  "Postal:"  )  ; 
contribCountryLabel  .  setText  (  "Country:"  )  ; 
emailLabel  .  setText  (  "Email:"  )  ; 
phoneLabel  .  setText  (  "Phone:"  )  ; 
firstName  .  setText  (  " "  )  ; 
lastName  .  setText  (  " "  )  ; 
title  .  setText  (  " "  )  ; 
orginization  .  setText  (  " "  )  ; 
address  .  setText  (  "  "  )  ; 
city  .  setText  (  " "  )  ; 
stateProv  .  setText  (  " "  )  ; 
postal  .  setText  (  " "  )  ; 
country  .  setText  (  " "  )  ; 
email  .  setText  (  " "  )  ; 
phone  .  setText  (  " "  )  ; 
javax  .  swing  .  GroupLayout   contributorPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  contributorPanel  )  ; 
contributorPanel  .  setLayout  (  contributorPanelLayout  )  ; 
contributorPanelLayout  .  setHorizontalGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  contributorPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  phoneLabel  )  .  addComponent  (  emailLabel  )  .  addComponent  (  contribCountryLabel  )  .  addComponent  (  postalLabel  )  .  addComponent  (  stateProvLabel  )  .  addComponent  (  jLabel18  )  .  addComponent  (  jLabel15  )  .  addComponent  (  jLabel14  )  .  addComponent  (  jLabel13  )  .  addComponent  (  jLabel16  )  .  addComponent  (  jLabel17  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  firstName  )  .  addComponent  (  lastName  )  .  addComponent  (  title  )  .  addComponent  (  orginization  )  .  addComponent  (  address  )  .  addComponent  (  city  )  .  addComponent  (  stateProv  )  .  addComponent  (  postal  )  .  addComponent  (  country  )  .  addComponent  (  email  )  .  addComponent  (  phone  )  )  .  addContainerGap  (  164  ,  Short  .  MAX_VALUE  )  )  )  ; 
contributorPanelLayout  .  setVerticalGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  contributorPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel13  )  .  addComponent  (  firstName  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel14  )  .  addComponent  (  lastName  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel15  )  .  addComponent  (  title  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel16  )  .  addComponent  (  orginization  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel17  )  .  addComponent  (  address  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel18  )  .  addComponent  (  city  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  stateProvLabel  )  .  addComponent  (  stateProv  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  postalLabel  )  .  addComponent  (  postal  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  contribCountryLabel  )  .  addComponent  (  country  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  emailLabel  )  .  addComponent  (  email  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  contributorPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  phoneLabel  )  .  addComponent  (  phone  )  )  .  addContainerGap  (  113  ,  Short  .  MAX_VALUE  )  )  )  ; 
notesPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Notes"  )  )  ; 
notes  .  setColumns  (  20  )  ; 
notes  .  setEditable  (  false  )  ; 
notes  .  setRows  (  5  )  ; 
jScrollPane8  .  setViewportView  (  notes  )  ; 
javax  .  swing  .  GroupLayout   notesPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  notesPanel  )  ; 
notesPanel  .  setLayout  (  notesPanelLayout  )  ; 
notesPanelLayout  .  setHorizontalGroup  (  notesPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  notesPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  jScrollPane8  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  602  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
notesPanelLayout  .  setVerticalGroup  (  notesPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  notesPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  jScrollPane8  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  191  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
stationName  .  setText  (  " "  )  ; 
stationId  .  setText  (  " "  )  ; 
elevation  .  setText  (  " "  )  ; 
stationStateProv  .  setText  (  " "  )  ; 
stationCountry  .  setText  (  " "  )  ; 
mlraName  .  setText  (  " "  )  ; 
mlraId  .  setText  (  " "  )  ; 
javax  .  swing  .  GroupLayout   jPanel1Layout  =  new   javax  .  swing  .  GroupLayout  (  jPanel1  )  ; 
jPanel1  .  setLayout  (  jPanel1Layout  )  ; 
jPanel1Layout  .  setHorizontalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jLabel12  )  .  addComponent  (  jLabel11  )  .  addComponent  (  jLabel10  )  .  addComponent  (  jLabel9  )  .  addComponent  (  jLabel2  )  .  addComponent  (  jLabel1  )  .  addComponent  (  stationNameLabel  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  mlraId  )  .  addComponent  (  mlraName  )  .  addComponent  (  stationCountry  )  .  addComponent  (  stationStateProv  )  .  addComponent  (  elevation  )  .  addComponent  (  stationId  )  .  addComponent  (  stationName  )  )  )  .  addComponent  (  notesPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  contributorPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
jPanel1Layout  .  setVerticalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  contributorPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  stationNameLabel  )  .  addComponent  (  stationName  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel1  )  .  addComponent  (  stationId  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel2  )  .  addComponent  (  elevation  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel9  )  .  addComponent  (  stationStateProv  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel10  )  .  addComponent  (  stationCountry  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel11  )  .  addComponent  (  mlraName  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel12  )  .  addComponent  (  mlraId  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  notesPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  )  .  addContainerGap  (  )  )  )  ; 
tabPane  .  addTab  (  "Metadata"  ,  jPanel1  )  ; 
jLabel6  .  setText  (  "Waterholding Capacity:"  )  ; 
whcSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  Double  .  valueOf  (  200.0d  )  ,  Double  .  valueOf  (  0.0d  )  ,  null  ,  Double  .  valueOf  (  0.1d  )  )  )  ; 
whcSpinner  .  addChangeListener  (  new   javax  .  swing  .  event  .  ChangeListener  (  )  { 

public   void   stateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
whcSpinnerStateChanged  (  evt  )  ; 
} 
}  )  ; 
whcUnitsText  .  setText  (  "mm"  )  ; 
exportXmlButton  .  setText  (  "Export to XML"  )  ; 
exportXmlButton  .  setEnabled  (  false  )  ; 
exportXmlButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
exportXmlButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
exportCsvButton  .  setText  (  "Export to FLX"  )  ; 
exportCsvButton  .  setEnabled  (  false  )  ; 
exportCsvButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
exportCsvButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
activeUnitSystemLabel  .  setText  (  "Display Unit System:"  )  ; 
activeUnitSystemText  .  setText  (  " "  )  ; 
menuBar  .  setFont  (  menuBar  .  getFont  (  )  )  ; 
fileMenu  .  setText  (  "File"  )  ; 
fileMenu  .  setFont  (  fileMenu  .  getFont  (  )  )  ; 
openDatasetMenuItem  .  setAccelerator  (  javax  .  swing  .  KeyStroke  .  getKeyStroke  (  java  .  awt  .  event  .  KeyEvent  .  VK_O  ,  java  .  awt  .  event  .  InputEvent  .  CTRL_MASK  )  )  ; 
openDatasetMenuItem  .  setFont  (  openDatasetMenuItem  .  getFont  (  )  )  ; 
openDatasetMenuItem  .  setText  (  "Open Dataset..."  )  ; 
openDatasetMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
openDatasetMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  add  (  openDatasetMenuItem  )  ; 
exitMenuItem  .  setFont  (  exitMenuItem  .  getFont  (  )  )  ; 
exitMenuItem  .  setText  (  "Exit"  )  ; 
exitMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
exitMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
fileMenu  .  add  (  exitMenuItem  )  ; 
menuBar  .  add  (  fileMenu  )  ; 
optionsMenu  .  setText  (  "Options"  )  ; 
optionsMenu  .  setFont  (  optionsMenu  .  getFont  (  )  )  ; 
toggleUnitsMenuItem  .  setFont  (  toggleUnitsMenuItem  .  getFont  (  )  )  ; 
toggleUnitsMenuItem  .  setText  (  "Toggle English/Metric Units"  )  ; 
toggleUnitsMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
toggleUnitsMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
optionsMenu  .  add  (  toggleUnitsMenuItem  )  ; 
menuBar  .  add  (  optionsMenu  )  ; 
helpMenu  .  setText  (  "Help"  )  ; 
helpMenu  .  setFont  (  helpMenu  .  getFont  (  )  )  ; 
aboutMenuItem  .  setFont  (  aboutMenuItem  .  getFont  (  )  )  ; 
aboutMenuItem  .  setText  (  "About"  )  ; 
aboutMenuItem  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
aboutMenuItemActionPerformed  (  evt  )  ; 
} 
}  )  ; 
helpMenu  .  add  (  aboutMenuItem  )  ; 
menuBar  .  add  (  helpMenu  )  ; 
setJMenuBar  (  menuBar  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  getContentPane  (  )  )  ; 
getContentPane  (  )  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  tabPane  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  963  ,  Short  .  MAX_VALUE  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel6  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  whcSpinner  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  57  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  whcUnitsText  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  31  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  58  ,  58  ,  58  )  .  addComponent  (  activeUnitSystemLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  activeUnitSystemText  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  258  ,  Short  .  MAX_VALUE  )  .  addComponent  (  exportXmlButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  exportCsvButton  )  )  )  .  addContainerGap  (  )  )  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  tabPane  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  460  ,  Short  .  MAX_VALUE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  exportCsvButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  exportXmlButton  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel6  )  .  addComponent  (  whcSpinner  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  whcUnitsText  )  .  addComponent  (  activeUnitSystemLabel  )  .  addComponent  (  activeUnitSystemText  )  )  )  .  addContainerGap  (  )  )  )  ; 
pack  (  )  ; 
} 

private   void   exitMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
System  .  exit  (  0  )  ; 
} 

private   void   openDatasetMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
JFileChooser   jfc  =  new   JFileChooser  (  "."  )  ; 
int   returnCondition  =  jfc  .  showOpenDialog  (  this  )  ; 
if  (  returnCondition  ==  JFileChooser  .  APPROVE_OPTION  )  { 
System  .  out  .  println  (  "Attempting XML parsing."  )  ; 
File   selectedFile  =  jfc  .  getSelectedFile  (  )  ; 
NewhallDataset   newDataset  =  null  ; 
boolean   goodLoad  =  false  ; 
try  { 
XMLFileParser   xfp  =  new   XMLFileParser  (  selectedFile  )  ; 
newDataset  =  xfp  .  getDataset  (  )  ; 
goodLoad  =  true  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "Attempting CSV parsing."  )  ; 
try  { 
CSVFileParser   cfp  =  new   CSVFileParser  (  selectedFile  )  ; 
newDataset  =  cfp  .  getDatset  (  )  ; 
goodLoad  =  true  ; 
}  catch  (  Exception   ee  )  { 
System  .  out  .  println  (  "Unacceptable file detected."  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Selected file is not formatted as a Newhall CSV or XML document."  )  ; 
} 
} 
if  (  goodLoad  )  { 
System  .  out  .  println  (  "File acceptable, loading."  )  ; 
nd  =  newDataset  ; 
loaded  =  false  ; 
loadDataset  (  )  ; 
loaded  =  true  ; 
}  else  { 
System  .  out  .  println  (  "File unacceptable, unloading all active data."  )  ; 
loaded  =  false  ; 
unloadDataset  (  )  ; 
} 
} 
} 

private   void   toggleUnitsMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
double   originalWhcValue  =  0.0  ; 
double   originalSoilAirValue  =  0.0  ; 
if  (  whcSpinner  .  getValue  (  )  instanceof   Double  )  { 
originalWhcValue  =  (  Double  )  whcSpinner  .  getValue  (  )  ; 
}  else  { 
originalWhcValue  =  (  Integer  )  whcSpinner  .  getValue  (  )  ; 
} 
if  (  soilAirOffset  .  getValue  (  )  instanceof   Double  )  { 
originalSoilAirValue  =  (  Double  )  soilAirOffset  .  getValue  (  )  ; 
}  else  { 
originalSoilAirValue  =  (  Integer  )  soilAirOffset  .  getValue  (  )  ; 
} 
this  .  inMetric  =  !  this  .  inMetric  ; 
if  (  this  .  inMetric  )  { 
double   whcInMm  =  originalWhcValue  *  25.4  ; 
double   savInC  =  originalSoilAirValue  *  (  5.0  /  9.0  )  ; 
whcSpinner  .  setValue  (  whcInMm  )  ; 
soilAirOffset  .  setValue  (  savInC  )  ; 
whcUnitsText  .  setText  (  "mm"  )  ; 
soilAirOffsetUnits  .  setText  (  (  char  )  176  +  "C"  )  ; 
activeUnitSystemText  .  setText  (  "Metric"  )  ; 
}  else  { 
double   whcInInches  =  originalWhcValue  *  0.0393700787  ; 
double   savInF  =  (  originalSoilAirValue  *  (  9.0  /  5.0  )  )  ; 
whcSpinner  .  setValue  (  whcInInches  )  ; 
soilAirOffset  .  setValue  (  savInF  )  ; 
whcUnitsText  .  setText  (  "in"  )  ; 
soilAirOffsetUnits  .  setText  (  (  char  )  176  +  "F"  )  ; 
activeUnitSystemText  .  setText  (  "English"  )  ; 
} 
if  (  nd  !=  null  )  { 
loadDataset  (  )  ; 
} 
} 

private   void   whcSpinnerStateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
if  (  nd  !=  null  &&  loaded  )  { 
runModel  (  )  ; 
} 
} 

private   void   aboutMenuItemActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
AboutFrame   af  =  new   AboutFrame  (  )  ; 
af  .  setLocation  (  150  ,  150  )  ; 
af  .  setVisible  (  true  )  ; 
} 

private   void   exportCsvButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
JFileChooser   jfc  =  new   JFileChooser  (  "."  )  ; 
int   saveDialogResult  =  jfc  .  showSaveDialog  (  this  )  ; 
if  (  saveDialogResult  ==  JOptionPane  .  OK_OPTION  )  { 
if  (  jfc  .  getSelectedFile  (  )  !=  null  &&  jfc  .  getSelectedFile  (  )  .  exists  (  )  )  { 
int   result  =  JOptionPane  .  showConfirmDialog  (  null  ,  "The file "  +  jfc  .  getSelectedFile  (  )  .  getName  (  )  +  " already exists, overwrite it?"  ,  "Confirm Overwrite"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
if  (  result  ==  JOptionPane  .  CANCEL_OPTION  )  { 
return  ; 
} 
} 
CSVResultsExporter   cve  =  new   CSVResultsExporter  (  nr  ,  jfc  .  getSelectedFile  (  )  )  ; 
cve  .  save  (  )  ; 
} 
} 

private   void   exportXmlButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
JFileChooser   jfc  =  new   JFileChooser  (  "."  )  ; 
int   saveDialogResult  =  jfc  .  showSaveDialog  (  this  )  ; 
if  (  saveDialogResult  ==  JOptionPane  .  OK_OPTION  )  { 
if  (  jfc  .  getSelectedFile  (  )  !=  null  &&  jfc  .  getSelectedFile  (  )  .  exists  (  )  )  { 
int   result  =  JOptionPane  .  showConfirmDialog  (  null  ,  "The file "  +  jfc  .  getSelectedFile  (  )  .  getName  (  )  +  " already exists, overwrite it?"  ,  "Confirm Overwrite"  ,  JOptionPane  .  OK_CANCEL_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
if  (  result  ==  JOptionPane  .  CANCEL_OPTION  )  { 
return  ; 
} 
} 
try  { 
XMLResultsExporter   xre  =  new   XMLResultsExporter  (  jfc  .  getSelectedFile  (  )  )  ; 
xre  .  export  (  nr  ,  nd  )  ; 
System  .  out  .  println  (  "Exported XML:\n------------------------"  )  ; 
System  .  out  .  print  (  XMLStringResultsExporter  .  export  (  nr  ,  nd  )  )  ; 
System  .  out  .  println  (  "------------------------"  )  ; 
}  catch  (  IOException   e  )  { 
System  .  out  .  println  (  "XML Export failed: "  +  e  .  getMessage  (  )  )  ; 
} 
} 
} 

private   void   soilAirOffsetStateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
double   newOffset  =  0.0  ; 
if  (  soilAirOffset  .  getValue  (  )  instanceof   Integer  )  { 
newOffset  =  (  Integer  )  soilAirOffset  .  getValue  (  )  ; 
}  else  { 
newOffset  =  (  Double  )  soilAirOffset  .  getValue  (  )  ; 
} 
if  (  this  .  inMetric  &&  !  nd  .  isMetric  (  )  )  { 
nd  .  getMetadata  (  )  .  setSoilAirOffset  (  newOffset  *  (  9.0  /  5.0  )  )  ; 
}  else   if  (  !  this  .  inMetric  &&  nd  .  isMetric  (  )  )  { 
nd  .  getMetadata  (  )  .  setSoilAirOffset  (  newOffset  *  (  5.0  /  9.0  )  )  ; 
}  else  { 
nd  .  getMetadata  (  )  .  setSoilAirOffset  (  newOffset  )  ; 
} 
if  (  nd  !=  null  &&  loaded  )  { 
runModel  (  )  ; 
} 
} 

private   void   amplitudeSpinnerStateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
double   newAmplitude  =  0.0  ; 
if  (  amplitudeSpinner  .  getValue  (  )  instanceof   Integer  )  { 
newAmplitude  =  (  Integer  )  amplitudeSpinner  .  getValue  (  )  ; 
}  else  { 
newAmplitude  =  (  Double  )  amplitudeSpinner  .  getValue  (  )  ; 
} 
if  (  nd  !=  null  &&  loaded  )  { 
nd  .  getMetadata  (  )  .  setAmplitude  (  newAmplitude  )  ; 
runModel  (  )  ; 
} 
} 

public   void   loadDataset  (  )  { 
List  <  Double  >  properTemp  =  new   ArrayList  <  Double  >  (  12  )  ; 
List  <  Double  >  properPrecip  =  new   ArrayList  <  Double  >  (  12  )  ; 
double   properElevation  =  0.0  ; 
if  (  this  .  inMetric  &&  !  nd  .  isMetric  (  )  )  { 
for  (  int   i  =  0  ;  i  <  12  ;  i  ++  )  { 
double   tempInC  =  (  nd  .  getTemperature  (  )  .  get  (  i  )  -  32  )  *  (  5.0  /  9.0  )  ; 
properTemp  .  add  (  tempInC  )  ; 
double   precipInMm  =  nd  .  getPrecipitation  (  )  .  get  (  i  )  *  25.4  ; 
properPrecip  .  add  (  precipInMm  )  ; 
} 
double   offsetInC  =  nd  .  getMetadata  (  )  .  getSoilAirOffset  (  )  *  (  5.0  /  9.0  )  ; 
soilAirOffset  .  setValue  (  offsetInC  )  ; 
properElevation  =  nd  .  getElevation  (  )  *  0.3048  ; 
}  else   if  (  !  this  .  inMetric  &&  nd  .  isMetric  (  )  )  { 
for  (  int   i  =  0  ;  i  <  12  ;  i  ++  )  { 
double   tempInF  =  (  nd  .  getTemperature  (  )  .  get  (  i  )  *  (  9.0  /  5.0  )  )  +  32  ; 
properTemp  .  add  (  tempInF  )  ; 
double   precipInInches  =  nd  .  getPrecipitation  (  )  .  get  (  i  )  /  25.4  ; 
properPrecip  .  add  (  precipInInches  )  ; 
} 
double   whcInInches  =  nd  .  getWaterholdingCapacity  (  )  *  0.0393700787  ; 
whcSpinner  .  setValue  (  whcInInches  )  ; 
double   offsetInF  =  nd  .  getMetadata  (  )  .  getSoilAirOffset  (  )  *  (  9.0  /  5.0  )  ; 
soilAirOffset  .  setValue  (  offsetInF  )  ; 
properElevation  =  nd  .  getElevation  (  )  *  3.2808399  ; 
}  else  { 
properTemp  =  nd  .  getTemperature  (  )  ; 
properPrecip  =  nd  .  getPrecipitation  (  )  ; 
properElevation  =  nd  .  getElevation  (  )  ; 
whcSpinner  .  setValue  (  nd  .  getWaterholdingCapacity  (  )  )  ; 
soilAirOffset  .  setValue  (  nd  .  getMetadata  (  )  .  getSoilAirOffset  (  )  )  ; 
} 
TableModel   tempTableModel  =  this  .  tempTable  .  getModel  (  )  ; 
TableModel   rainfallTableModel  =  this  .  rainfallTable  .  getModel  (  )  ; 
if  (  this  .  inMetric  )  { 
tempTableModel  .  setValueAt  (  "Air Temp ("  +  (  char  )  176  +  "C)"  ,  0  ,  0  )  ; 
rainfallTableModel  .  setValueAt  (  "Rainfall (mm)"  ,  0  ,  0  )  ; 
elevationText  .  setText  (  roundForDisplay  (  properElevation  )  +  " meters"  )  ; 
elevation  .  setText  (  roundForDisplay  (  properElevation  )  +  " meters"  )  ; 
soilAirOffsetUnits  .  setText  (  (  char  )  176  +  "C"  )  ; 
activeUnitSystemText  .  setText  (  "Metric"  )  ; 
}  else  { 
tempTableModel  .  setValueAt  (  "Air Temp ("  +  (  char  )  176  +  "F)"  ,  0  ,  0  )  ; 
rainfallTableModel  .  setValueAt  (  "Rainfall (in)"  ,  0  ,  0  )  ; 
elevationText  .  setText  (  roundForDisplay  (  properElevation  )  +  " feet"  )  ; 
elevation  .  setText  (  roundForDisplay  (  properElevation  )  +  " feet"  )  ; 
soilAirOffsetUnits  .  setText  (  (  char  )  176  +  "F"  )  ; 
activeUnitSystemText  .  setText  (  "English"  )  ; 
} 
tempTable  .  getColumnModel  (  )  .  getColumn  (  0  )  .  setPreferredWidth  (  150  )  ; 
rainfallTable  .  getColumnModel  (  )  .  getColumn  (  0  )  .  setPreferredWidth  (  150  )  ; 
for  (  int   i  =  0  ;  i  <  12  ;  i  ++  )  { 
rainfallTableModel  .  setValueAt  (  roundForDisplay  (  properPrecip  .  get  (  i  )  )  ,  0  ,  i  +  1  )  ; 
tempTableModel  .  setValueAt  (  roundForDisplay  (  properTemp  .  get  (  i  )  )  ,  0  ,  i  +  1  )  ; 
} 
srcUnitSystem  .  setText  (  nd  .  getMetadata  (  )  .  getUnitSystem  (  )  )  ; 
amplitudeSpinner  .  setValue  (  nd  .  getMetadata  (  )  .  getAmplitude  (  )  )  ; 
stationText  .  setText  (  nd  .  getName  (  )  )  ; 
latitudeText  .  setText  (  roundDegreesForDisplay  (  nd  .  getLatitude  (  )  )  +  " "  +  (  char  )  176  +  nd  .  getNsHemisphere  (  )  )  ; 
longitudeText  .  setText  (  roundDegreesForDisplay  (  nd  .  getLongitude  (  )  )  +  " "  +  (  char  )  176  +  nd  .  getEwHemisphere  (  )  )  ; 
countryText  .  setText  (  nd  .  getCountry  (  )  )  ; 
startingYearText  .  setText  (  nd  .  getStartYear  (  )  +  ""  )  ; 
endingYearText  .  setText  (  nd  .  getEndYear  (  )  +  ""  )  ; 
whcSpinner  .  setEnabled  (  true  )  ; 
soilAirOffset  .  setEnabled  (  true  )  ; 
amplitudeSpinner  .  setEnabled  (  true  )  ; 
exportCsvButton  .  setEnabled  (  true  )  ; 
exportXmlButton  .  setEnabled  (  true  )  ; 
stationName  .  setText  (  nd  .  getMetadata  (  )  .  getStationName  (  )  )  ; 
stationId  .  setText  (  nd  .  getMetadata  (  )  .  getStationId  (  )  )  ; 
stationStateProv  .  setText  (  nd  .  getMetadata  (  )  .  getStationStateProvidence  (  )  )  ; 
stationCountry  .  setText  (  nd  .  getMetadata  (  )  .  getStationCountry  (  )  )  ; 
mlraName  .  setText  (  nd  .  getMetadata  (  )  .  getMlraName  (  )  )  ; 
mlraId  .  setText  (  Integer  .  toString  (  nd  .  getMetadata  (  )  .  getMlraId  (  )  )  )  ; 
firstName  .  setText  (  nd  .  getMetadata  (  )  .  getContribFirstName  (  )  )  ; 
lastName  .  setText  (  nd  .  getMetadata  (  )  .  getContribLastName  (  )  )  ; 
title  .  setText  (  nd  .  getMetadata  (  )  .  getContribTitle  (  )  )  ; 
orginization  .  setText  (  nd  .  getMetadata  (  )  .  getContribOrg  (  )  )  ; 
address  .  setText  (  nd  .  getMetadata  (  )  .  getContribAddress  (  )  )  ; 
city  .  setText  (  nd  .  getMetadata  (  )  .  getContribCity  (  )  )  ; 
stateProv  .  setText  (  nd  .  getMetadata  (  )  .  getContribStateProvidence  (  )  )  ; 
postal  .  setText  (  nd  .  getMetadata  (  )  .  getContribPostal  (  )  )  ; 
country  .  setText  (  nd  .  getMetadata  (  )  .  getContribCountry  (  )  )  ; 
email  .  setText  (  nd  .  getMetadata  (  )  .  getContribEmail  (  )  )  ; 
phone  .  setText  (  nd  .  getMetadata  (  )  .  getContribPhone  (  )  )  ; 
String   totalNotes  =  ""  ; 
for  (  String   note  :  nd  .  getMetadata  (  )  .  getNotes  (  )  )  { 
totalNotes  +=  note  +  "\n\n"  ; 
} 
notes  .  setText  (  totalNotes  )  ; 
runModel  (  )  ; 
} 

public   void   runModel  (  )  { 
double   inputWhc  =  0.0  ; 
double   inputOffset  =  nd  .  getMetadata  (  )  .  getSoilAirOffset  (  )  ; 
if  (  whcSpinner  .  getValue  (  )  instanceof   Integer  )  { 
inputWhc  =  (  Integer  )  whcSpinner  .  getValue  (  )  ; 
}  else  { 
inputWhc  =  (  Double  )  whcSpinner  .  getValue  (  )  ; 
} 
if  (  !  this  .  inMetric  )  { 
inputWhc  *=  25.4  ; 
} 
if  (  !  nd  .  isMetric  (  )  )  { 
inputOffset  *=  5.0  /  9.0  ; 
} 
try  { 
nr  =  null  ; 
System  .  out  .  println  (  "Calling model: "  +  nd  .  getName  (  )  +  ", "  +  inputWhc  +  ", "  +  inputOffset  +  ", "  +  nd  .  getMetadata  (  )  .  getAmplitude  (  )  )  ; 
nr  =  BASICSimulationModel  .  runSimulation  (  nd  ,  inputWhc  ,  inputOffset  ,  nd  .  getMetadata  (  )  .  getAmplitude  (  )  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
unloadDataset  (  )  ; 
return  ; 
} 
if  (  nr  !=  null  )  { 
TableModel   mpeTableModel  =  this  .  mpeTable  .  getModel  (  )  ; 
mpeTable  .  getColumnModel  (  )  .  getColumn  (  0  )  .  setPreferredWidth  (  260  )  ; 
String   properAnnualRainfall  ; 
if  (  !  this  .  inMetric  )  { 
properAnnualRainfall  =  roundForDisplay  (  nr  .  getAnnualRainfall  (  )  *  0.0393700787  )  +  " in"  ; 
whcUnitsText  .  setText  (  "in"  )  ; 
mpeTableModel  .  setValueAt  (  "Evapotranspiration (in)"  ,  0  ,  0  )  ; 
for  (  int   i  =  0  ;  i  <  12  ;  i  ++  )  { 
double   mpeValueForDisplay  =  roundForDisplay  (  nr  .  getMeanPotentialEvapotranspiration  (  )  .  get  (  i  )  *  0.0393700787  )  ; 
mpeTableModel  .  setValueAt  (  mpeValueForDisplay  ,  0  ,  i  +  1  )  ; 
} 
}  else  { 
properAnnualRainfall  =  roundForDisplay  (  nr  .  getAnnualRainfall  (  )  )  +  " mm"  ; 
whcUnitsText  .  setText  (  "mm"  )  ; 
mpeTableModel  .  setValueAt  (  "Evapotranspiration (mm)"  ,  0  ,  0  )  ; 
for  (  int   i  =  0  ;  i  <  12  ;  i  ++  )  { 
double   mpeValueForDisplay  =  roundForDisplay  (  nr  .  getMeanPotentialEvapotranspiration  (  )  .  get  (  i  )  )  ; 
mpeTableModel  .  setValueAt  (  mpeValueForDisplay  ,  0  ,  i  +  1  )  ; 
} 
} 
annualRainfallText  .  setText  (  properAnnualRainfall  )  ; 
temperatureRegimeText  .  setText  (  nr  .  getTemperatureRegime  (  )  )  ; 
moistureRegimeText  .  setText  (  nr  .  getMoistureRegime  (  )  )  ; 
subdivisionsText  .  setText  (  nr  .  getRegimeSubdivision1  (  )  +  " "  +  nr  .  getRegimeSubdivision2  (  )  )  ; 
moistureCalendarText  .  setText  (  nr  .  getFormattedMoistureCalendar  (  )  )  ; 
temperatureCalendarText  .  setText  (  nr  .  getFormattedTemperatureCalendar  (  )  )  ; 
statisticsText  .  setText  (  nr  .  getFormattedStatistics  (  )  )  ; 
} 
} 

public   void   unloadDataset  (  )  { 
stationText  .  setText  (  " << No Dataset Loaded >>"  )  ; 
elevationText  .  setText  (  ""  )  ; 
latitudeText  .  setText  (  ""  )  ; 
longitudeText  .  setText  (  ""  )  ; 
countryText  .  setText  (  ""  )  ; 
startingYearText  .  setText  (  ""  )  ; 
endingYearText  .  setText  (  ""  )  ; 
TableModel   mpeTableModel  =  this  .  mpeTable  .  getModel  (  )  ; 
TableModel   rainfallTableModel  =  this  .  rainfallTable  .  getModel  (  )  ; 
TableModel   tempTableModel  =  this  .  tempTable  .  getModel  (  )  ; 
for  (  int   i  =  0  ;  i  <=  12  ;  i  ++  )  { 
rainfallTableModel  .  setValueAt  (  ""  ,  0  ,  i  )  ; 
tempTableModel  .  setValueAt  (  ""  ,  0  ,  i  )  ; 
} 
for  (  int   i  =  0  ;  i  <=  12  ;  i  ++  )  { 
mpeTableModel  .  setValueAt  (  ""  ,  0  ,  i  )  ; 
} 
annualRainfallText  .  setText  (  ""  )  ; 
temperatureRegimeText  .  setText  (  ""  )  ; 
moistureRegimeText  .  setText  (  ""  )  ; 
subdivisionsText  .  setText  (  ""  )  ; 
temperatureCalendarText  .  setText  (  ""  )  ; 
moistureCalendarText  .  setText  (  ""  )  ; 
statisticsText  .  setText  (  ""  )  ; 
whcSpinner  .  setValue  (  200.0  )  ; 
whcSpinner  .  setEnabled  (  false  )  ; 
soilAirOffset  .  setValue  (  1.2  )  ; 
soilAirOffset  .  setEnabled  (  false  )  ; 
amplitudeSpinner  .  setValue  (  0.66  )  ; 
amplitudeSpinner  .  setEnabled  (  false  )  ; 
exportCsvButton  .  setEnabled  (  false  )  ; 
exportXmlButton  .  setEnabled  (  false  )  ; 
stationName  .  setText  (  ""  )  ; 
stationId  .  setText  (  ""  )  ; 
elevation  .  setText  (  ""  )  ; 
stationStateProv  .  setText  (  ""  )  ; 
stationCountry  .  setText  (  ""  )  ; 
mlraName  .  setText  (  ""  )  ; 
mlraId  .  setText  (  ""  )  ; 
firstName  .  setText  (  ""  )  ; 
lastName  .  setText  (  ""  )  ; 
title  .  setText  (  ""  )  ; 
orginization  .  setText  (  ""  )  ; 
address  .  setText  (  ""  )  ; 
city  .  setText  (  ""  )  ; 
stateProv  .  setText  (  ""  )  ; 
postal  .  setText  (  ""  )  ; 
country  .  setText  (  ""  )  ; 
email  .  setText  (  ""  )  ; 
phone  .  setText  (  ""  )  ; 
notes  .  setText  (  ""  )  ; 
activeUnitSystemText  .  setText  (  ""  )  ; 
this  .  inMetric  =  true  ; 
} 

public   double   roundForDisplay  (  Double   value  )  { 
return   Double  .  valueOf  (  new   DecimalFormat  (  "##.##"  )  .  format  (  value  )  )  ; 
} 

public   double   roundDegreesForDisplay  (  Double   degrees  )  { 
return   Double  .  valueOf  (  new   DecimalFormat  (  "##.####"  )  .  format  (  degrees  )  )  ; 
} 

private   javax  .  swing  .  JMenuItem   aboutMenuItem  ; 

private   javax  .  swing  .  JLabel   activeUnitSystemLabel  ; 

private   javax  .  swing  .  JLabel   activeUnitSystemText  ; 

private   javax  .  swing  .  JLabel   address  ; 

private   javax  .  swing  .  JSpinner   amplitudeSpinner  ; 

private   javax  .  swing  .  JLabel   annualRainfallText  ; 

private   javax  .  swing  .  JPanel   calendarPanel  ; 

private   javax  .  swing  .  JLabel   city  ; 

private   javax  .  swing  .  JLabel   contribCountryLabel  ; 

private   javax  .  swing  .  JPanel   contributorPanel  ; 

private   javax  .  swing  .  JLabel   country  ; 

private   javax  .  swing  .  JLabel   countryLabel  ; 

private   javax  .  swing  .  JLabel   countryText  ; 

private   javax  .  swing  .  JPanel   datasetPanel  ; 

private   javax  .  swing  .  JScrollPane   datasetScrollPane  ; 

private   javax  .  swing  .  JScrollPane   datasetScrollPane1  ; 

private   javax  .  swing  .  JScrollPane   datasetScrollPane2  ; 

private   javax  .  swing  .  JScrollPane   datasetScrollPane3  ; 

private   javax  .  swing  .  JLabel   elevation  ; 

private   javax  .  swing  .  JLabel   elevationLabel  ; 

private   javax  .  swing  .  JLabel   elevationText  ; 

private   javax  .  swing  .  JLabel   email  ; 

private   javax  .  swing  .  JLabel   emailLabel  ; 

private   javax  .  swing  .  JLabel   endingYearLabel  ; 

private   javax  .  swing  .  JLabel   endingYearText  ; 

private   javax  .  swing  .  JMenuItem   exitMenuItem  ; 

private   javax  .  swing  .  JButton   exportCsvButton  ; 

private   javax  .  swing  .  JButton   exportXmlButton  ; 

private   javax  .  swing  .  JMenu   fileMenu  ; 

private   javax  .  swing  .  JLabel   firstName  ; 

private   javax  .  swing  .  JMenu   helpMenu  ; 

private   javax  .  swing  .  JPanel   inputPanel  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel10  ; 

private   javax  .  swing  .  JLabel   jLabel11  ; 

private   javax  .  swing  .  JLabel   jLabel12  ; 

private   javax  .  swing  .  JLabel   jLabel13  ; 

private   javax  .  swing  .  JLabel   jLabel14  ; 

private   javax  .  swing  .  JLabel   jLabel15  ; 

private   javax  .  swing  .  JLabel   jLabel16  ; 

private   javax  .  swing  .  JLabel   jLabel17  ; 

private   javax  .  swing  .  JLabel   jLabel18  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel21  ; 

private   javax  .  swing  .  JLabel   jLabel23  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabel5  ; 

private   javax  .  swing  .  JLabel   jLabel6  ; 

private   javax  .  swing  .  JLabel   jLabel7  ; 

private   javax  .  swing  .  JLabel   jLabel8  ; 

private   javax  .  swing  .  JLabel   jLabel9  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JPanel   jPanel3  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane2  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane3  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane4  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane5  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane6  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane7  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane8  ; 

private   javax  .  swing  .  JSeparator   jSeparator1  ; 

private   javax  .  swing  .  JSeparator   jSeparator2  ; 

private   javax  .  swing  .  JSeparator   jSeparator3  ; 

private   javax  .  swing  .  JTextArea   jTextArea1  ; 

private   javax  .  swing  .  JTextArea   jTextArea2  ; 

private   javax  .  swing  .  JTextArea   jTextArea3  ; 

private   javax  .  swing  .  JTextArea   jTextArea4  ; 

private   javax  .  swing  .  JLabel   lastName  ; 

private   javax  .  swing  .  JLabel   latitudeLabel  ; 

private   javax  .  swing  .  JLabel   latitudeText  ; 

private   javax  .  swing  .  JLabel   longitudeLabel  ; 

private   javax  .  swing  .  JLabel   longitudeText  ; 

private   javax  .  swing  .  JMenuBar   menuBar  ; 

private   javax  .  swing  .  JLabel   mlraId  ; 

private   javax  .  swing  .  JLabel   mlraName  ; 

private   javax  .  swing  .  JPanel   modelResultsPanel  ; 

private   javax  .  swing  .  JPanel   moistCalPanel  ; 

private   javax  .  swing  .  JTextArea   moistureCalendarText  ; 

private   javax  .  swing  .  JLabel   moistureRegimeText  ; 

private   javax  .  swing  .  JTable   mpeTable  ; 

private   javax  .  swing  .  JTextArea   notes  ; 

private   javax  .  swing  .  JPanel   notesPanel  ; 

private   javax  .  swing  .  JMenuItem   openDatasetMenuItem  ; 

private   javax  .  swing  .  JMenu   optionsMenu  ; 

private   javax  .  swing  .  JLabel   orginization  ; 

private   javax  .  swing  .  JLabel   phone  ; 

private   javax  .  swing  .  JLabel   phoneLabel  ; 

private   javax  .  swing  .  JLabel   postal  ; 

private   javax  .  swing  .  JLabel   postalLabel  ; 

private   javax  .  swing  .  JTable   rainfallTable  ; 

private   javax  .  swing  .  JTable   rainfallTable1  ; 

private   javax  .  swing  .  JSpinner   soilAirOffset  ; 

private   javax  .  swing  .  JLabel   soilAirOffsetLabel  ; 

private   javax  .  swing  .  JLabel   soilAirOffsetUnits  ; 

private   javax  .  swing  .  JLabel   srcUnitSystem  ; 

private   javax  .  swing  .  JLabel   srcUnitSystemLabel  ; 

private   javax  .  swing  .  JLabel   startYearLabel  ; 

private   javax  .  swing  .  JLabel   startingYearText  ; 

private   javax  .  swing  .  JLabel   stateProv  ; 

private   javax  .  swing  .  JLabel   stateProvLabel  ; 

private   javax  .  swing  .  JLabel   stationCountry  ; 

private   javax  .  swing  .  JLabel   stationId  ; 

private   javax  .  swing  .  JLabel   stationLabel  ; 

private   javax  .  swing  .  JLabel   stationName  ; 

private   javax  .  swing  .  JLabel   stationNameLabel  ; 

private   javax  .  swing  .  JLabel   stationStateProv  ; 

private   javax  .  swing  .  JLabel   stationText  ; 

private   javax  .  swing  .  JTextArea   statisticsText  ; 

private   javax  .  swing  .  JLabel   subdivisionsLabel  ; 

private   javax  .  swing  .  JLabel   subdivisionsText  ; 

private   javax  .  swing  .  JTabbedPane   tabPane  ; 

private   javax  .  swing  .  JPanel   tempCalPanel  ; 

private   javax  .  swing  .  JTable   tempTable  ; 

private   javax  .  swing  .  JTextArea   temperatureCalendarText  ; 

private   javax  .  swing  .  JLabel   temperatureRegimeText  ; 

private   javax  .  swing  .  JLabel   title  ; 

private   javax  .  swing  .  JMenuItem   toggleUnitsMenuItem  ; 

private   javax  .  swing  .  JSpinner   whcSpinner  ; 

private   javax  .  swing  .  JLabel   whcUnitsText  ; 
} 

