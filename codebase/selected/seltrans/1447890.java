package   jp  .  jparc  .  apps  .  iTuning  .  Scan2D  ; 

import   javax  .  swing  .  *  ; 
import   java  .  awt  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   javax  .  swing  .  event  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  border  .  *  ; 
import   gov  .  sns  .  tools  .  swing  .  *  ; 
import   gov  .  sns  .  tools  .  apputils  .  *  ; 







public   class   Scan2D  { 

private   String   title  =  "2D Scan Controller"  ; 

private   JPanel   controllerPanel  =  new   JPanel  (  )  ; 

private   Thread   measurementThread  =  null  ; 

private   ScanVariable   scanVariable  =  null  ; 

private   ScanVariable   paramVariable  =  null  ; 

private   Vector   measuredValuesV  =  new   Vector  (  )  ; 

private   Vector   validationValuesV  =  new   Vector  (  )  ; 

private   Vector   mpsStatusValidationValuesV  =  new   Vector  (  )  ; 

private   double   sleepTime  =  0.2  ; 

private   double   paramValue  =  0.0  ; 

private   double   paramValueRB  =  0.0  ; 

private   double   scanValueMem  =  0.  ; 

private   double   paramValueMem  =  0.  ; 

private   double   paramLowLim  =  0  ; 

private   double   paramUppLim  =  360  ; 

private   double   paramStep  =  5.0  ; 

private   double   scanValue  =  0.0  ; 

private   double   scanValueRB  =  0.0  ; 

private   double   lowLim  =  -  180  ; 

private   double   uppLim  =  180  ; 

private   double   step  =  5.0  ; 

private   AvgController   avgController  =  null  ; 

private   ChangeListener   avgParamChangeListener  =  null  ; 

private   volatile   double   avrgTime  =  0.0  ; 

private   volatile   int   nAveraging  =  1  ; 

private   ValidationController   validationController  =  null  ; 

private   ChangeListener   validationParamChangeListener  =  null  ; 

private   volatile   boolean   validateMeasurement  =  false  ; 

private   volatile   double   lowValidationLim  =  0.0  ; 

private   volatile   double   uppValidationLim  =  100.0  ; 

private   int   maxNumberBadMeasurements  =  10  ; 

private   MPSStatusValidator   mpsStatusValidator  =  null  ; 

private   ChangeListener   mpsStatusValidatorParamChangeListener  =  null  ; 

private   volatile   boolean   mpsStatusValidateMeasurement  =  false  ; 

private   JPanel   paramPhaseScanAndRB_Panel  =  new   JPanel  (  )  ; 

private   JLabel   paramRB_Label  =  new   JLabel  (  "Parameter PV RB value :  "  )  ; 

private   JLabel   paramScanStep_Label  =  new   JLabel  (  "Parameter SCAN step:  "  )  ; 

private   DoubleInputTextField   paramLowLimText  =  new   DoubleInputTextField  (  10  )  ; 

private   DoubleInputTextField   paramUppLimText  =  new   DoubleInputTextField  (  10  )  ; 

private   DoubleInputTextField   paramStepText  =  new   DoubleInputTextField  (  10  )  ; 

private   JTextField   paramText  =  new   JTextField  (  10  )  ; 

private   JTextField   paramTextRB  =  new   JTextField  (  10  )  ; 

private   JLabel   paramUnitsLabel  =  new   JLabel  (  "dim"  )  ; 

private   JScrollBar   paramScrollBar  =  new   JScrollBar  (  Scrollbar  .  HORIZONTAL  ,  0  ,  0  ,  0  ,  1000  )  ; 

private   boolean   paramScrollBarLocked  =  false  ; 

private   JPanel   valuePhaseScanAndRB_Panel  =  new   JPanel  (  )  ; 

private   JLabel   valueRB_Label  =  new   JLabel  (  "Scan PV RB value :  "  )  ; 

private   JLabel   scanStep_Label  =  new   JLabel  (  "SCAN with step:  "  )  ; 

private   DoubleInputTextField   lowLimText  =  new   DoubleInputTextField  (  10  )  ; 

private   DoubleInputTextField   uppLimText  =  new   DoubleInputTextField  (  10  )  ; 

private   DoubleInputTextField   stepText  =  new   DoubleInputTextField  (  10  )  ; 

private   JTextField   valueText  =  new   JTextField  (  10  )  ; 

private   JTextField   valueTextRB  =  new   JTextField  (  10  )  ; 

private   JLabel   unitsLabel  =  new   JLabel  (  "dim"  )  ; 

private   DoubleInputTextField   sleepTimeText  =  new   DoubleInputTextField  (  6  )  ; 

private   JLabel   sleepTimeLabel  =  new   JLabel  (  "Time delay after settings [sec]: "  ,  JLabel  .  CENTER  )  ; 

private   JButton   startButton  =  new   JButton  (  "START "  )  ; 

private   JButton   resumeButton  =  new   JButton  (  "RESUME"  )  ; 

private   JButton   stopButton  =  new   JButton  (  " STOP "  )  ; 

private   JScrollBar   scrollBar  =  new   JScrollBar  (  Scrollbar  .  HORIZONTAL  ,  0  ,  0  ,  0  ,  1000  )  ; 

private   boolean   scrollBarLocked  =  false  ; 

private   DecimalFormat   valueFormat  =  new   DecimalFormat  (  "####.####"  )  ; 

private   DecimalFormat   sleepTimeFormat  =  new   DecimalFormat  (  "##.##"  )  ; 

private   JTextField   messageText  =  new   JTextField  (  40  )  ; 

private   ActionEvent   newSetOfDataAction  =  null  ; 

private   ActionEvent   newPointOfDataAction  =  null  ; 

private   Vector   newSetOfDataListenersV  =  new   Vector  (  )  ; 

private   Vector   newPointOfDataListenersV  =  new   Vector  (  )  ; 

private   ActionEvent   startButtonAction  =  null  ; 

private   Vector   startButtonListenersV  =  new   Vector  (  )  ; 

private   ActionListener   stopScanListener  =  null  ; 

private   double  [  ]  paramVariableSet  =  new   double  [  100  ]  ; 

private   int   paramNPoints  =  0  ; 

private   int   paramPositionInd  =  0  ; 

private   double  [  ]  variableSet  =  new   double  [  100  ]  ; 

private   int   nPoints  =  0  ; 

private   int   positionInd  =  0  ; 

private   boolean   continueMode  =  false  ; 

private   volatile   boolean   scanOn  =  false  ; 

private   static   int   START_BUTTONS_STATE  =  0  ; 

private   static   int   RESUME_BUTTONS_STATE  =  1  ; 

private   static   int   SCAN_BUTTONS_STATE  =  2  ; 

private   int   CURRENT_BUTTONS_STATE  =  0  ; 

private   boolean   scanVarShouldBeRestored  =  true  ; 

private   boolean   scanVarShouldBeMemorized  =  true  ; 

private   Object   lockObj  =  new   Object  (  )  ; 







public   Scan2D  (  String   title  )  { 
this  .  title  =  title  ; 
paramLowLimText  .  setNormalBackground  (  Color  .  white  )  ; 
paramUppLimText  .  setNormalBackground  (  Color  .  white  )  ; 
paramStepText  .  setNormalBackground  (  Color  .  white  )  ; 
paramText  .  setBackground  (  Color  .  getHSBColor  (  0.5f  ,  0.5f  ,  1.0f  )  )  ; 
paramTextRB  .  setBackground  (  Color  .  getHSBColor  (  0.0f  ,  0.0f  ,  0.9f  )  )  ; 
paramTextRB  .  setEditable  (  false  )  ; 
paramLowLimText  .  setDecimalFormat  (  valueFormat  )  ; 
paramUppLimText  .  setDecimalFormat  (  valueFormat  )  ; 
paramStepText  .  setDecimalFormat  (  valueFormat  )  ; 
paramLowLimText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
paramUppLimText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
paramText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
paramStepText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
paramTextRB  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
paramText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  scanOn  ==  false  )  { 
try  { 
paramValue  =  Double  .  parseDouble  (  paramText  .  getText  (  )  )  ; 
}  catch  (  NumberFormatException   exc  )  { 
} 
setParamCurrentValue  (  paramValue  )  ; 
measure  (  paramValue  )  ; 
}  else  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
} 
} 
}  )  ; 
paramText  .  addMouseListener  (  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
if  (  e  .  getClickCount  (  )  ==  2  )  { 
if  (  scanOn  ==  false  )  { 
try  { 
paramValue  =  Double  .  parseDouble  (  paramText  .  getText  (  )  )  ; 
}  catch  (  NumberFormatException   exc  )  { 
} 
setParamCurrentValue  (  paramValue  )  ; 
measure  (  paramValue  )  ; 
}  else  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
} 
} 
} 
}  )  ; 
paramTextRB  .  addMouseListener  (  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
paramTextRB  .  setText  (  null  )  ; 
if  (  paramVariable  !=  null  &&  paramVariable  .  getChannelRB  (  )  !=  null  )  { 
paramValueRB  =  paramVariable  .  getValueRB  (  )  ; 
paramTextRB  .  setText  (  valueFormat  .  format  (  paramValueRB  )  )  ; 
} 
} 
}  )  ; 
paramLowLimText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
paramLowLim  =  paramLowLimText  .  getValue  (  )  ; 
setParamSliderValue  (  paramValue  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
paramUppLimText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
paramUppLim  =  paramUppLimText  .  getValue  (  )  ; 
setParamSliderValue  (  paramValue  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
paramStepText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
paramStep  =  paramStepText  .  getValue  (  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
paramScrollBar  .  setBlockIncrement  (  (  scrollBar  .  getMaximum  (  )  -  scrollBar  .  getMinimum  (  )  )  /  50  )  ; 
paramScrollBar  .  getModel  (  )  .  addChangeListener  (  new   ChangeListener  (  )  { 

public   void   stateChanged  (  ChangeEvent   e  )  { 
if  (  !  paramScrollBarLocked  )  { 
int   i_val  =  paramScrollBar  .  getValue  (  )  ; 
double   val  =  paramLowLim  +  i_val  *  (  paramUppLim  -  paramLowLim  )  /  (  paramScrollBar  .  getMaximum  (  )  -  paramScrollBar  .  getMinimum  (  )  )  ; 
paramText  .  setText  (  null  )  ; 
paramText  .  setText  (  valueFormat  .  format  (  val  )  )  ; 
} 
} 
}  )  ; 
lowLimText  .  setNormalBackground  (  Color  .  white  )  ; 
uppLimText  .  setNormalBackground  (  Color  .  white  )  ; 
stepText  .  setNormalBackground  (  Color  .  white  )  ; 
sleepTimeText  .  setNormalBackground  (  Color  .  white  )  ; 
valueText  .  setBackground  (  Color  .  getHSBColor  (  0.0f  ,  0.0f  ,  0.9f  )  )  ; 
valueTextRB  .  setBackground  (  Color  .  getHSBColor  (  0.0f  ,  0.0f  ,  0.9f  )  )  ; 
valueTextRB  .  setEditable  (  false  )  ; 
valueText  .  setEditable  (  false  )  ; 
lowLimText  .  setDecimalFormat  (  valueFormat  )  ; 
uppLimText  .  setDecimalFormat  (  valueFormat  )  ; 
stepText  .  setDecimalFormat  (  valueFormat  )  ; 
sleepTimeText  .  setDecimalFormat  (  sleepTimeFormat  )  ; 
lowLimText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
uppLimText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
valueText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
valueTextRB  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
stepText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
sleepTimeText  .  setHorizontalAlignment  (  JTextField  .  CENTER  )  ; 
valueText  .  addMouseListener  (  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
valueTextRB  .  setText  (  null  )  ; 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
if  (  scanVariable  .  getChannel  (  )  !=  null  )  { 
setCurrentValue  (  scanVariable  .  getValue  (  )  )  ; 
} 
if  (  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
setCurrentValueRB  (  scanVariable  .  getValueRB  (  )  )  ; 
} 
}  else  { 
try  { 
scanValue  =  Double  .  parseDouble  (  valueText  .  getText  (  )  )  ; 
}  catch  (  NumberFormatException   exc  )  { 
} 
; 
setCurrentValue  (  scanValue  )  ; 
} 
} 
}  )  ; 
valueTextRB  .  addMouseListener  (  new   MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  MouseEvent   e  )  { 
valueTextRB  .  setText  (  null  )  ; 
if  (  scanVariable  !=  null  &&  scanOn  ==  false  )  { 
scanValueRB  =  scanVariable  .  getValueRB  (  )  ; 
if  (  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
scanValueRB  =  scanVariable  .  getValueRB  (  )  ; 
valueTextRB  .  setText  (  valueFormat  .  format  (  scanValueRB  )  )  ; 
} 
} 
} 
}  )  ; 
lowLimText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
lowLim  =  lowLimText  .  getValue  (  )  ; 
setSliderValue  (  scanValue  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
uppLimText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
uppLim  =  uppLimText  .  getValue  (  )  ; 
setSliderValue  (  scanValue  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
stepText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
step  =  stepText  .  getValue  (  )  ; 
continueMode  =  false  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
}  )  ; 
sleepTimeText  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
sleepTime  =  sleepTimeText  .  getValue  (  )  ; 
} 
}  )  ; 
scrollBar  .  setBlockIncrement  (  (  scrollBar  .  getMaximum  (  )  -  scrollBar  .  getMinimum  (  )  )  /  50  )  ; 
scrollBar  .  getModel  (  )  .  addChangeListener  (  new   ChangeListener  (  )  { 

public   void   stateChanged  (  ChangeEvent   e  )  { 
if  (  !  scrollBarLocked  )  { 
int   i_val  =  scrollBar  .  getValue  (  )  ; 
double   val  =  lowLim  +  i_val  *  (  uppLim  -  lowLim  )  /  (  scrollBar  .  getMaximum  (  )  -  scrollBar  .  getMinimum  (  )  )  ; 
valueText  .  setText  (  null  )  ; 
valueText  .  setText  (  valueFormat  .  format  (  val  )  )  ; 
} 
} 
}  )  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
startButton  .  setHorizontalTextPosition  (  SwingConstants  .  CENTER  )  ; 
stopButton  .  setHorizontalTextPosition  (  SwingConstants  .  CENTER  )  ; 
resumeButton  .  setHorizontalTextPosition  (  SwingConstants  .  CENTER  )  ; 
startButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  scanOn  ==  true  )  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
return  ; 
} 
messageText  .  setText  (  null  )  ; 
continueMode  =  false  ; 
scanVarShouldBeRestored  =  true  ; 
if  (  CURRENT_BUTTONS_STATE  ==  START_BUTTONS_STATE  )  { 
scanVarShouldBeMemorized  =  true  ; 
}  else  { 
scanVarShouldBeMemorized  =  false  ; 
} 
setButtonsState  (  SCAN_BUTTONS_STATE  )  ; 
for  (  int   i  =  0  ,  n  =  startButtonListenersV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  ActionListener  )  startButtonListenersV  .  get  (  i  )  )  .  actionPerformed  (  startButtonAction  )  ; 
} 
measure  (  )  ; 
} 
}  )  ; 
resumeButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
scanVarShouldBeMemorized  =  false  ; 
if  (  CURRENT_BUTTONS_STATE  ==  RESUME_BUTTONS_STATE  )  { 
if  (  scanOn  ==  true  )  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
return  ; 
} 
messageText  .  setText  (  null  )  ; 
continueMode  =  true  ; 
scanVarShouldBeRestored  =  true  ; 
setButtonsState  (  SCAN_BUTTONS_STATE  )  ; 
measure  (  )  ; 
}  else  { 
if  (  scanOn  ==  false  )  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
return  ; 
} 
scanOn  =  false  ; 
scanVarShouldBeRestored  =  false  ; 
if  (  measurementThread  !=  null  &&  measurementThread  .  isAlive  (  )  )  { 
measurementThread  .  interrupt  (  )  ; 
} 
} 
} 
}  )  ; 
stopButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  scanOn  ==  false  )  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
return  ; 
} 
scanOn  =  false  ; 
scanVarShouldBeRestored  =  true  ; 
if  (  measurementThread  !=  null  &&  measurementThread  .  isAlive  (  )  )  { 
measurementThread  .  interrupt  (  )  ; 
} 
} 
}  )  ; 
stopScanListener  =  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
scanOn  =  false  ; 
if  (  measurementThread  !=  null  &&  measurementThread  .  isAlive  (  )  )  { 
measurementThread  .  interrupt  (  )  ; 
} 
} 
}  ; 
newSetOfDataAction  =  new   ActionEvent  (  this  ,  0  ,  "newSet"  )  ; 
newPointOfDataAction  =  new   ActionEvent  (  this  ,  0  ,  "newPoint"  )  ; 
startButtonAction  =  new   ActionEvent  (  this  ,  0  ,  "startButton"  )  ; 
setParamCurrentValueRB  (  paramValue  )  ; 
setParamCurrentValue  (  paramValueRB  )  ; 
setParamLowLimit  (  paramLowLim  )  ; 
setParamUppLimit  (  paramUppLim  )  ; 
setParamStep  (  paramStep  )  ; 
setCurrentValueRB  (  scanValue  )  ; 
setCurrentValue  (  scanValueRB  )  ; 
setLowLimit  (  lowLim  )  ; 
setUppLimit  (  uppLim  )  ; 
setStep  (  step  )  ; 
setSleepTime  (  sleepTime  )  ; 
controllerPanel  .  setLayout  (  new   BorderLayout  (  )  )  ; 
controllerPanel  .  setBorder  (  BorderFactory  .  createTitledBorder  (  BorderFactory  .  createEtchedBorder  (  )  ,  title  )  )  ; 
FlowLayout   flwC  =  new   FlowLayout  (  FlowLayout  .  LEFT  ,  1  ,  1  )  ; 
JPanel   panel_1_0  =  paramPhaseScanAndRB_Panel  ; 
panel_1_0  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_1_0  .  setLayout  (  flwC  )  ; 
panel_1_0  .  add  (  paramRB_Label  )  ; 
panel_1_0  .  add  (  paramTextRB  )  ; 
JPanel   panel_2_0  =  new   JPanel  (  )  ; 
panel_2_0  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_2_0  .  setLayout  (  new   GridLayout  (  1  ,  3  ,  1  ,  1  )  )  ; 
panel_2_0  .  add  (  paramLowLimText  )  ; 
panel_2_0  .  add  (  paramText  )  ; 
panel_2_0  .  add  (  paramUppLimText  )  ; 
JPanel   panel_3_0  =  new   JPanel  (  )  ; 
panel_3_0  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_3_0  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_3_0  .  add  (  paramScrollBar  ,  BorderLayout  .  NORTH  )  ; 
JPanel   panel_4_0  =  new   JPanel  (  )  ; 
panel_4_0  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_4_0  .  setLayout  (  flwC  )  ; 
panel_4_0  .  add  (  paramScanStep_Label  )  ; 
panel_4_0  .  add  (  paramStepText  )  ; 
panel_4_0  .  add  (  paramUnitsLabel  )  ; 
JPanel   panel_Group_0_0  =  new   JPanel  (  )  ; 
panel_Group_0_0  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_Group_0_0  .  add  (  panel_2_0  ,  BorderLayout  .  NORTH  )  ; 
panel_Group_0_0  .  add  (  panel_3_0  ,  BorderLayout  .  SOUTH  )  ; 
JPanel   panel_Group_0_1  =  new   JPanel  (  )  ; 
panel_Group_0_1  .  setBorder  (  BorderFactory  .  createEtchedBorder  (  )  )  ; 
panel_Group_0_1  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_Group_0_1  .  add  (  panel_1_0  ,  BorderLayout  .  NORTH  )  ; 
panel_Group_0_1  .  add  (  panel_4_0  ,  BorderLayout  .  SOUTH  )  ; 
panel_Group_0_1  .  add  (  panel_Group_0_0  ,  BorderLayout  .  CENTER  )  ; 
panel_Group_0_1  .  setBackground  (  Color  .  blue  )  ; 
JPanel   panel_1  =  valuePhaseScanAndRB_Panel  ; 
panel_1  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_1  .  setLayout  (  flwC  )  ; 
panel_1  .  add  (  valueRB_Label  )  ; 
panel_1  .  add  (  valueTextRB  )  ; 
JPanel   panel_2  =  new   JPanel  (  )  ; 
panel_2  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_2  .  setLayout  (  new   GridLayout  (  1  ,  3  ,  1  ,  1  )  )  ; 
panel_2  .  add  (  lowLimText  )  ; 
panel_2  .  add  (  valueText  )  ; 
panel_2  .  add  (  uppLimText  )  ; 
JPanel   panel_3  =  new   JPanel  (  )  ; 
panel_3  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_3  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_3  .  add  (  scrollBar  ,  BorderLayout  .  NORTH  )  ; 
JPanel   panel_4  =  new   JPanel  (  )  ; 
panel_4  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_4  .  setLayout  (  flwC  )  ; 
panel_4  .  add  (  scanStep_Label  )  ; 
panel_4  .  add  (  stepText  )  ; 
panel_4  .  add  (  unitsLabel  )  ; 
JPanel   panel_Group_1_0  =  new   JPanel  (  )  ; 
panel_Group_1_0  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_Group_1_0  .  add  (  panel_2  ,  BorderLayout  .  NORTH  )  ; 
panel_Group_1_0  .  add  (  panel_3  ,  BorderLayout  .  SOUTH  )  ; 
JPanel   panel_Group_1_1  =  new   JPanel  (  )  ; 
panel_Group_1_1  .  setBorder  (  BorderFactory  .  createEtchedBorder  (  )  )  ; 
panel_Group_1_1  .  setLayout  (  new   BorderLayout  (  )  )  ; 
panel_Group_1_1  .  add  (  panel_1  ,  BorderLayout  .  NORTH  )  ; 
panel_Group_1_1  .  add  (  panel_4  ,  BorderLayout  .  SOUTH  )  ; 
panel_Group_1_1  .  add  (  panel_Group_1_0  ,  BorderLayout  .  CENTER  )  ; 
panel_Group_1_1  .  setBackground  (  Color  .  blue  )  ; 
JPanel   panel_5  =  new   JPanel  (  )  ; 
panel_5  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_5  .  setLayout  (  flwC  )  ; 
panel_5  .  add  (  sleepTimeLabel  )  ; 
panel_5  .  add  (  sleepTimeText  )  ; 
JPanel   panel_6  =  new   JPanel  (  )  ; 
panel_6  .  setBorder  (  BorderFactory  .  createEmptyBorder  (  )  )  ; 
panel_6  .  setLayout  (  new   GridLayout  (  1  ,  3  ,  1  ,  1  )  )  ; 
panel_6  .  add  (  startButton  )  ; 
panel_6  .  add  (  resumeButton  )  ; 
panel_6  .  add  (  stopButton  )  ; 
JPanel   inner_panel  =  new   JPanel  (  )  ; 
inner_panel  .  setLayout  (  new   VerticalLayout  (  )  )  ; 
inner_panel  .  add  (  panel_Group_0_1  )  ; 
inner_panel  .  add  (  panel_Group_1_1  )  ; 
inner_panel  .  add  (  panel_5  )  ; 
inner_panel  .  add  (  panel_6  )  ; 
controllerPanel  .  add  (  inner_panel  ,  BorderLayout  .  WEST  )  ; 
setFontForAll  (  new   Font  (  "Monospaced"  ,  Font  .  PLAIN  ,  10  )  )  ; 
controllerPanel  .  setBackground  (  controllerPanel  .  getBackground  (  )  .  darker  (  )  )  ; 
} 







public   void   setFontForAll  (  Font   fnt  )  { 
paramRB_Label  .  setFont  (  fnt  )  ; 
paramScanStep_Label  .  setFont  (  fnt  )  ; 
paramLowLimText  .  setFont  (  fnt  )  ; 
paramUppLimText  .  setFont  (  fnt  )  ; 
paramStepText  .  setFont  (  fnt  )  ; 
paramText  .  setFont  (  fnt  )  ; 
paramTextRB  .  setFont  (  fnt  )  ; 
paramUnitsLabel  .  setFont  (  fnt  )  ; 
valueRB_Label  .  setFont  (  fnt  )  ; 
scanStep_Label  .  setFont  (  fnt  )  ; 
lowLimText  .  setFont  (  fnt  )  ; 
uppLimText  .  setFont  (  fnt  )  ; 
stepText  .  setFont  (  fnt  )  ; 
valueText  .  setFont  (  fnt  )  ; 
valueTextRB  .  setFont  (  fnt  )  ; 
sleepTimeText  .  setFont  (  fnt  )  ; 
sleepTimeLabel  .  setFont  (  fnt  )  ; 
unitsLabel  .  setFont  (  fnt  )  ; 
startButton  .  setFont  (  fnt  )  ; 
stopButton  .  setFont  (  fnt  )  ; 
resumeButton  .  setFont  (  fnt  )  ; 
sleepTimeLabel  .  setFont  (  fnt  )  ; 
TitledBorder   border  =  (  TitledBorder  )  controllerPanel  .  getBorder  (  )  ; 
border  .  setTitleFont  (  fnt  )  ; 
if  (  avgController  !=  null  )  { 
avgController  .  setFontForAll  (  fnt  )  ; 
} 
if  (  validationController  !=  null  )  { 
validationController  .  setFontForAll  (  fnt  )  ; 
} 
if  (  mpsStatusValidator  !=  null  )  { 
mpsStatusValidator  .  setFontForAll  (  fnt  )  ; 
} 
} 






public   String   getTitle  (  )  { 
return   title  ; 
} 







public   void   setTitle  (  String   title  )  { 
this  .  title  =  title  ; 
controllerPanel  .  setBorder  (  BorderFactory  .  createTitledBorder  (  BorderFactory  .  createEtchedBorder  (  )  ,  title  )  )  ; 
controllerPanel  .  validate  (  )  ; 
controllerPanel  .  repaint  (  )  ; 
} 






public   JPanel   getJPanel  (  )  { 
return   controllerPanel  ; 
} 






public   boolean   isScanON  (  )  { 
return   scanOn  ; 
} 







public   void   setParamVariable  (  ScanVariable   paramVariable  )  { 
synchronized  (  lockObj  )  { 
if  (  this  .  paramVariable  !=  null  )  { 
this  .  paramVariable  .  setMessageTextField  (  null  )  ; 
this  .  paramVariable  .  setStopScanListener  (  null  )  ; 
this  .  paramVariable  .  setLockObject  (  new   Object  (  )  )  ; 
} 
this  .  paramVariable  =  paramVariable  ; 
if  (  paramVariable  ==  null  )  { 
return  ; 
} 
paramVariable  .  setStopScanListener  (  stopScanListener  )  ; 
paramVariable  .  setMessageTextField  (  messageText  )  ; 
paramVariable  .  setLockObject  (  lockObj  )  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
} 







public   void   setScanVariable  (  ScanVariable   scanVariable  )  { 
synchronized  (  lockObj  )  { 
if  (  this  .  scanVariable  !=  null  )  { 
this  .  scanVariable  .  setMessageTextField  (  null  )  ; 
this  .  scanVariable  .  setStopScanListener  (  null  )  ; 
this  .  scanVariable  .  setLockObject  (  new   Object  (  )  )  ; 
} 
this  .  scanVariable  =  scanVariable  ; 
if  (  scanVariable  ==  null  )  { 
return  ; 
} 
scanVariable  .  setStopScanListener  (  stopScanListener  )  ; 
scanVariable  .  setMessageTextField  (  messageText  )  ; 
scanVariable  .  setLockObject  (  lockObj  )  ; 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
} 







public   void   setAvgController  (  AvgController   avgController  )  { 
if  (  this  .  avgController  !=  null  )  { 
this  .  avgController  .  removeChangeListener  (  avgParamChangeListener  )  ; 
avrgTime  =  0.0  ; 
nAveraging  =  1  ; 
} 
this  .  avgController  =  avgController  ; 
if  (  avgController  !=  null  )  { 
if  (  avgParamChangeListener  ==  null  )  { 
avgParamChangeListener  =  new   ChangeListener  (  )  { 

public   void   stateChanged  (  ChangeEvent   changeEvent  )  { 
AvgController   avgCntr  =  (  AvgController  )  changeEvent  .  getSource  (  )  ; 
avrgTime  =  avgCntr  .  getTimeDelay  (  )  ; 
nAveraging  =  avgCntr  .  getAvgNumber  (  )  ; 
} 
}  ; 
} 
avgController  .  addChangeListener  (  avgParamChangeListener  )  ; 
avrgTime  =  avgController  .  getTimeDelay  (  )  ; 
nAveraging  =  avgController  .  getAvgNumber  (  )  ; 
} 
} 







public   void   setValidationController  (  ValidationController   validationController  )  { 
if  (  this  .  validationController  !=  null  )  { 
this  .  validationController  .  removeChangeListener  (  validationParamChangeListener  )  ; 
validateMeasurement  =  false  ; 
} 
this  .  validationController  =  validationController  ; 
if  (  validationController  !=  null  )  { 
if  (  validationParamChangeListener  ==  null  )  { 
validationParamChangeListener  =  new   ChangeListener  (  )  { 

public   void   stateChanged  (  ChangeEvent   changeEvent  )  { 
ValidationController   validCntr  =  (  ValidationController  )  changeEvent  .  getSource  (  )  ; 
validateMeasurement  =  validCntr  .  isOn  (  )  ; 
lowValidationLim  =  validCntr  .  getLowLim  (  )  ; 
uppValidationLim  =  validCntr  .  getUppLim  (  )  ; 
} 
}  ; 
} 
validationController  .  addChangeListener  (  validationParamChangeListener  )  ; 
validateMeasurement  =  validationController  .  isOn  (  )  ; 
lowValidationLim  =  validationController  .  getLowLim  (  )  ; 
uppValidationLim  =  validationController  .  getUppLim  (  )  ; 
} 
} 







public   void   setMPSStatusValidator  (  MPSStatusValidator   mpsStatusValidator  )  { 
if  (  this  .  mpsStatusValidator  !=  null  )  { 
this  .  mpsStatusValidator  .  removeChangeListener  (  mpsStatusValidatorParamChangeListener  )  ; 
mpsStatusValidateMeasurement  =  false  ; 
} 
this  .  mpsStatusValidator  =  mpsStatusValidator  ; 
if  (  mpsStatusValidator  !=  null  )  { 
if  (  mpsStatusValidatorParamChangeListener  ==  null  )  { 
mpsStatusValidatorParamChangeListener  =  new   ChangeListener  (  )  { 

public   void   stateChanged  (  ChangeEvent   changeEvent  )  { 
MPSStatusValidator   mpsValidCntr  =  (  MPSStatusValidator  )  changeEvent  .  getSource  (  )  ; 
mpsStatusValidateMeasurement  =  mpsValidCntr  .  isOn  (  )  ; 
} 
}  ; 
} 
mpsStatusValidator  .  addChangeListener  (  mpsStatusValidatorParamChangeListener  )  ; 
mpsStatusValidateMeasurement  =  mpsStatusValidator  .  isOn  (  )  ; 
} 
} 







public   void   setParamCurrentValue  (  double   paramValue  )  { 
paramText  .  setText  (  null  )  ; 
paramText  .  setText  (  valueFormat  .  format  (  paramValue  )  )  ; 
setParamSliderValue  (  paramValue  )  ; 
this  .  paramValue  =  paramValue  ; 
} 







public   void   setParamCurrentValueRB  (  double   paramValueRB  )  { 
this  .  paramValueRB  =  paramValueRB  ; 
paramTextRB  .  setText  (  null  )  ; 
paramTextRB  .  setText  (  valueFormat  .  format  (  paramValueRB  )  )  ; 
} 







public   void   setCurrentValue  (  double   scanValue  )  { 
valueText  .  setText  (  null  )  ; 
valueText  .  setText  (  valueFormat  .  format  (  scanValue  )  )  ; 
setSliderValue  (  scanValue  )  ; 
this  .  scanValue  =  scanValue  ; 
} 







public   void   setCurrentValueRB  (  double   scanValueRB  )  { 
this  .  scanValueRB  =  scanValueRB  ; 
valueTextRB  .  setText  (  null  )  ; 
valueTextRB  .  setText  (  valueFormat  .  format  (  scanValueRB  )  )  ; 
} 






public   double   getParamValue  (  )  { 
return   paramValue  ; 
} 






public   double   getParamValueRB  (  )  { 
return   paramValueRB  ; 
} 






public   double   getScanValue  (  )  { 
return   scanValue  ; 
} 






public   double   getScanValueRB  (  )  { 
return   scanValueRB  ; 
} 







public   void   setParamLowLimit  (  double   paramLowLim  )  { 
paramLowLimText  .  setValue  (  paramLowLim  )  ; 
} 







public   void   setParamUppLimit  (  double   paramUppLim  )  { 
paramUppLimText  .  setValue  (  paramUppLim  )  ; 
} 







public   void   setParamStep  (  double   paramStep  )  { 
paramStepText  .  setValue  (  paramStep  )  ; 
} 






public   double   getParamLowLimit  (  )  { 
return   paramLowLim  ; 
} 






public   double   getParamUppLimit  (  )  { 
return   paramUppLim  ; 
} 






public   double   getParamStep  (  )  { 
return   paramStep  ; 
} 







public   void   setLowLimit  (  double   lowLim  )  { 
lowLimText  .  setValue  (  lowLim  )  ; 
} 







public   void   setUppLimit  (  double   uppLim  )  { 
uppLimText  .  setValue  (  uppLim  )  ; 
} 







public   void   setStep  (  double   step  )  { 
stepText  .  setValue  (  step  )  ; 
} 






public   double   getLowLimit  (  )  { 
return   lowLim  ; 
} 






public   double   getUppLimit  (  )  { 
return   uppLim  ; 
} 






public   double   getStep  (  )  { 
return   step  ; 
} 







private   void   setParamSliderValue  (  double   val  )  { 
int   i_val  =  (  paramScrollBar  .  getMaximum  (  )  +  paramScrollBar  .  getMinimum  (  )  )  /  2  ; 
if  (  paramLowLim  <  paramUppLim  )  { 
i_val  =  (  int  )  (  (  (  val  -  paramLowLim  )  /  (  paramUppLim  -  paramLowLim  )  )  *  (  paramScrollBar  .  getMaximum  (  )  -  paramScrollBar  .  getMinimum  (  )  )  )  ; 
if  (  i_val  <  paramScrollBar  .  getMinimum  (  )  )  { 
i_val  =  paramScrollBar  .  getMinimum  (  )  ; 
} 
if  (  i_val  >  paramScrollBar  .  getMaximum  (  )  )  { 
i_val  =  paramScrollBar  .  getMaximum  (  )  ; 
} 
} 
paramScrollBarLocked  =  true  ; 
paramScrollBar  .  setValue  (  i_val  )  ; 
paramScrollBarLocked  =  false  ; 
} 







private   void   setSliderValue  (  double   val  )  { 
int   i_val  =  (  scrollBar  .  getMaximum  (  )  +  scrollBar  .  getMinimum  (  )  )  /  2  ; 
if  (  lowLim  <  uppLim  )  { 
i_val  =  (  int  )  (  (  (  val  -  lowLim  )  /  (  uppLim  -  lowLim  )  )  *  (  scrollBar  .  getMaximum  (  )  -  scrollBar  .  getMinimum  (  )  )  )  ; 
if  (  i_val  <  scrollBar  .  getMinimum  (  )  )  { 
i_val  =  scrollBar  .  getMinimum  (  )  ; 
} 
if  (  i_val  >  scrollBar  .  getMaximum  (  )  )  { 
i_val  =  scrollBar  .  getMaximum  (  )  ; 
} 
} 
scrollBarLocked  =  true  ; 
scrollBar  .  setValue  (  i_val  )  ; 
scrollBarLocked  =  false  ; 
} 







public   void   setSleepTime  (  double   sleepTimeIn  )  { 
sleepTimeText  .  setValue  (  sleepTimeIn  )  ; 
} 






public   double   getSleepTime  (  )  { 
return   sleepTime  ; 
} 






public   ScanVariable   getParamVariable  (  )  { 
return   paramVariable  ; 
} 






public   ScanVariable   getScanVariable  (  )  { 
return   scanVariable  ; 
} 







public   void   addMeasuredValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
if  (  mv  !=  null  )  { 
measuredValuesV  .  add  (  mv  )  ; 
} 
} 
} 






public   Vector   getMeasuredValuesV  (  )  { 
return   measuredValuesV  ; 
} 







public   void   removeMeasuredValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
measuredValuesV  .  remove  (  mv  )  ; 
} 
} 




public   void   removeAllMeasuredValues  (  )  { 
synchronized  (  lockObj  )  { 
measuredValuesV  .  clear  (  )  ; 
} 
} 







public   void   addValidationValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
if  (  mv  !=  null  )  { 
validationValuesV  .  add  (  mv  )  ; 
} 
} 
} 






public   Vector   getValidationValuesV  (  )  { 
return   validationValuesV  ; 
} 







public   void   removeValidationValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
validationValuesV  .  remove  (  mv  )  ; 
} 
} 




public   void   removeAllValidationValues  (  )  { 
synchronized  (  lockObj  )  { 
validationValuesV  .  clear  (  )  ; 
} 
} 







public   void   addMpsValidationValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
if  (  mv  !=  null  )  { 
mpsStatusValidationValuesV  .  add  (  mv  )  ; 
} 
} 
} 






public   Vector   getMpsValidationValuesV  (  )  { 
return   mpsStatusValidationValuesV  ; 
} 







public   void   removeMpsValidationValue  (  MeasuredValue   mv  )  { 
synchronized  (  lockObj  )  { 
mpsStatusValidationValuesV  .  remove  (  mv  )  ; 
} 
} 




public   void   removeMpsAllValidationValues  (  )  { 
synchronized  (  lockObj  )  { 
mpsStatusValidationValuesV  .  clear  (  )  ; 
} 
} 







public   void   addNewSetOfDataListener  (  ActionListener   newSetListener  )  { 
if  (  newSetListener  ==  null  )  { 
return  ; 
} 
newSetOfDataListenersV  .  add  (  newSetListener  )  ; 
} 









public   void   addNewPointOfDataListener  (  ActionListener   newPointListener  )  { 
if  (  newPointListener  ==  null  )  { 
return  ; 
} 
newPointOfDataListenersV  .  add  (  newPointListener  )  ; 
} 




public   void   removeAllNewSetOfDataListeners  (  )  { 
newSetOfDataListenersV  .  clear  (  )  ; 
} 




public   void   removeAllNewPointOfDataListeners  (  )  { 
newPointOfDataListenersV  .  clear  (  )  ; 
} 







public   void   addStartButtonListener  (  ActionListener   newStartButtonListener  )  { 
startButtonListenersV  .  add  (  newStartButtonListener  )  ; 
} 







public   void   removeStartButtonListener  (  ActionListener   newStartButtonListener  )  { 
startButtonListenersV  .  remove  (  newStartButtonListener  )  ; 
} 




public   void   removeAllStartButtonListeners  (  )  { 
startButtonListenersV  .  clear  (  )  ; 
} 




private   void   setParamVariableSet  (  )  { 
paramPositionInd  =  0  ; 
paramNPoints  =  1  ; 
double   paramStepLocal  =  paramStep  ; 
if  (  paramStepLocal  >  0.  )  { 
paramStepLocal  *=  1.000000001  ; 
} 
if  (  paramStepLocal  <  0.  )  { 
paramStepLocal  *=  0.999999999  ; 
} 
if  (  paramStepLocal  !=  0.  )  { 
paramNPoints  =  (  int  )  (  (  paramUppLim  -  paramLowLim  )  /  paramStepLocal  )  ; 
if  (  paramLowLim  +  paramNPoints  *  paramStepLocal  <  paramUppLim  )  { 
paramNPoints  ++  ; 
} 
paramNPoints  ++  ; 
} 
if  (  paramVariableSet  .  length  <  paramNPoints  )  { 
paramVariableSet  =  new   double  [  paramNPoints  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  paramNPoints  ;  i  ++  )  { 
paramVariableSet  [  i  ]  =  paramLowLim  +  i  *  paramStepLocal  ; 
} 
if  (  paramVariableSet  [  paramNPoints  -  1  ]  >  paramUppLim  )  { 
paramVariableSet  [  paramNPoints  -  1  ]  =  paramUppLim  ; 
} 
} 




private   void   setVariableSet  (  )  { 
positionInd  =  0  ; 
nPoints  =  1  ; 
if  (  step  !=  0.  )  { 
nPoints  =  (  int  )  (  (  uppLim  -  lowLim  )  /  step  )  ; 
if  (  lowLim  +  nPoints  *  step  <  uppLim  )  { 
nPoints  ++  ; 
} 
nPoints  ++  ; 
} 
if  (  variableSet  .  length  <  nPoints  )  { 
variableSet  =  new   double  [  nPoints  ]  ; 
} 
for  (  int   i  =  0  ;  i  <  nPoints  ;  i  ++  )  { 
variableSet  [  i  ]  =  lowLim  +  i  *  step  ; 
} 
if  (  variableSet  [  nPoints  -  1  ]  >  uppLim  )  { 
variableSet  [  nPoints  -  1  ]  =  uppLim  ; 
} 
} 







private   void   setButtonsState  (  int   BUTTONS_STATE  )  { 
int   OLD_BUTTONS_STATE  =  CURRENT_BUTTONS_STATE  ; 
CURRENT_BUTTONS_STATE  =  BUTTONS_STATE  ; 
if  (  CURRENT_BUTTONS_STATE  ==  START_BUTTONS_STATE  )  { 
startButton  .  setEnabled  (  true  )  ; 
resumeButton  .  setEnabled  (  false  )  ; 
stopButton  .  setEnabled  (  false  )  ; 
resumeButton  .  setText  (  "PAUSE"  )  ; 
}  else  { 
if  (  CURRENT_BUTTONS_STATE  ==  RESUME_BUTTONS_STATE  )  { 
startButton  .  setEnabled  (  true  )  ; 
resumeButton  .  setEnabled  (  true  )  ; 
stopButton  .  setEnabled  (  false  )  ; 
resumeButton  .  setText  (  "RESUME"  )  ; 
}  else  { 
startButton  .  setEnabled  (  false  )  ; 
resumeButton  .  setEnabled  (  true  )  ; 
stopButton  .  setEnabled  (  true  )  ; 
resumeButton  .  setText  (  "PAUSE"  )  ; 
} 
} 
boolean   startB  =  startButton  .  isEnabled  (  )  ; 
boolean   resumeB  =  resumeButton  .  isEnabled  (  )  ; 
boolean   stopB  =  stopButton  .  isEnabled  (  )  ; 
if  (  startB  )  { 
startButton  .  setBackground  (  Color  .  red  )  ; 
}  else  { 
startButton  .  setBackground  (  Color  .  lightGray  )  ; 
} 
if  (  resumeB  )  { 
resumeButton  .  setBackground  (  Color  .  red  )  ; 
}  else  { 
resumeButton  .  setBackground  (  Color  .  lightGray  )  ; 
} 
if  (  stopB  )  { 
stopButton  .  setBackground  (  Color  .  red  )  ; 
lowLimText  .  setEditable  (  false  )  ; 
uppLimText  .  setEditable  (  false  )  ; 
stepText  .  setEditable  (  false  )  ; 
sleepTimeText  .  setEditable  (  false  )  ; 
}  else  { 
stopButton  .  setBackground  (  Color  .  lightGray  )  ; 
lowLimText  .  setEditable  (  true  )  ; 
uppLimText  .  setEditable  (  true  )  ; 
stepText  .  setEditable  (  true  )  ; 
sleepTimeText  .  setEditable  (  true  )  ; 
} 
} 




public   void   measure  (  )  { 
Runnable   runMeasure  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
synchronized  (  lockObj  )  { 
scanOn  =  true  ; 
measurementThread  =  Thread  .  currentThread  (  )  ; 
if  (  scanVarShouldBeMemorized  ==  true  )  { 
if  (  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
paramVariable  .  memorizeValue  (  )  ; 
} 
paramValueMem  =  paramValue  ; 
} 
if  (  scanVarShouldBeMemorized  ==  true  )  { 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
scanVariable  .  memorizeValue  (  )  ; 
} 
scanValueMem  =  scanValue  ; 
} 
if  (  continueMode  ==  false  )  { 
setVariableSet  (  )  ; 
setParamVariableSet  (  )  ; 
setParamPV  (  paramVariableSet  [  paramPositionInd  ]  )  ; 
startNewSetOfData  (  )  ; 
} 
for  (  int   pS  =  paramPositionInd  ;  pS  <  paramNPoints  ;  pS  ++  )  { 
setParamPV  (  paramVariableSet  [  pS  ]  )  ; 
if  (  positionInd  ==  nPoints  )  { 
positionInd  =  0  ; 
startNewSetOfData  (  )  ; 
} 
paramPositionInd  =  pS  ; 
if  (  !  trueMeasure  (  )  )  { 
continueMode  =  true  ; 
break  ; 
}  else  { 
continueMode  =  false  ; 
paramPositionInd  ++  ; 
} 
} 
if  (  scanVarShouldBeRestored  ==  true  &&  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
paramVariable  .  restoreFromMemory  (  )  ; 
} 
if  (  scanVarShouldBeRestored  ==  true  &&  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
scanVariable  .  restoreFromMemory  (  )  ; 
} 
if  (  scanOn  !=  false  &&  sleepTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  sleepTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  scanVarShouldBeRestored  ==  true  )  { 
valueTextRB  .  setText  (  null  )  ; 
paramTextRB  .  setText  (  null  )  ; 
} 
setCurrentValue  (  scanValueMem  )  ; 
setParamCurrentValue  (  paramValueMem  )  ; 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
if  (  scanVariable  .  getChannel  (  )  !=  null  )  { 
Thread   localUpDateThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
setCurrentValue  (  scanVariable  .  getValue  (  )  )  ; 
if  (  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
setCurrentValueRB  (  scanVariable  .  getValueRB  (  )  )  ; 
} 
} 
}  )  ; 
localUpDateThread  .  start  (  )  ; 
} 
} 
if  (  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
if  (  paramVariable  .  getChannel  (  )  !=  null  )  { 
Thread   localUpDateThread  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
Thread  .  sleep  (  1000  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
setParamCurrentValue  (  paramVariable  .  getValue  (  )  )  ; 
if  (  paramVariable  .  getChannelRB  (  )  !=  null  )  { 
setParamCurrentValueRB  (  paramVariable  .  getValueRB  (  )  )  ; 
} 
} 
}  )  ; 
localUpDateThread  .  start  (  )  ; 
} 
} 
if  (  continueMode  )  { 
setButtonsState  (  RESUME_BUTTONS_STATE  )  ; 
}  else  { 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
} 
scanOn  =  false  ; 
} 
} 
}  ; 
Thread   mThread  =  new   Thread  (  runMeasure  )  ; 
mThread  .  start  (  )  ; 
} 







private   void   measure  (  final   double   paramVal  )  { 
Runnable   runMeasure  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
synchronized  (  lockObj  )  { 
setButtonsState  (  SCAN_BUTTONS_STATE  )  ; 
scanOn  =  true  ; 
measurementThread  =  Thread  .  currentThread  (  )  ; 
scanValueMem  =  scanValue  ; 
paramValueMem  =  paramValue  ; 
if  (  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
paramVariable  .  memorizeValue  (  )  ; 
} 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
scanVariable  .  memorizeValue  (  )  ; 
} 
setVariableSet  (  )  ; 
setParamPV  (  paramVal  )  ; 
startNewSetOfData  (  )  ; 
continueMode  =  false  ; 
trueMeasure  (  )  ; 
if  (  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
paramVariable  .  restoreFromMemory  (  )  ; 
} 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
scanVariable  .  restoreFromMemory  (  )  ; 
} 
if  (  scanOn  !=  false  &&  sleepTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  sleepTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
valueTextRB  .  setText  (  null  )  ; 
paramTextRB  .  setText  (  null  )  ; 
setCurrentValue  (  scanValueMem  )  ; 
setParamCurrentValue  (  paramValueMem  )  ; 
if  (  scanVariable  !=  null  &&  scanVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
if  (  scanVariable  .  getChannel  (  )  !=  null  )  { 
setCurrentValue  (  scanVariable  .  getValue  (  )  )  ; 
if  (  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
setCurrentValueRB  (  scanVariable  .  getValueRB  (  )  )  ; 
} 
} 
} 
if  (  paramVariable  !=  null  &&  paramVariable  .  getMonitoredPV  (  )  .  isGood  (  )  )  { 
if  (  paramVariable  .  getChannel  (  )  !=  null  )  { 
setParamCurrentValue  (  paramVariable  .  getValue  (  )  )  ; 
if  (  paramVariable  .  getChannelRB  (  )  !=  null  )  { 
setParamCurrentValueRB  (  paramVariable  .  getValueRB  (  )  )  ; 
} 
} 
} 
setButtonsState  (  START_BUTTONS_STATE  )  ; 
scanOn  =  false  ; 
} 
} 
}  ; 
Thread   mThread  =  new   Thread  (  runMeasure  )  ; 
mThread  .  start  (  )  ; 
} 







private   void   setParamPV  (  double   paramVal  )  { 
setParamCurrentValue  (  paramVal  )  ; 
paramValueRB  =  paramValue  ; 
if  (  paramVariable  !=  null  &&  paramVariable  .  getChannel  (  )  !=  null  )  { 
paramVariable  .  setValue  (  paramVal  )  ; 
} 
if  (  scanOn  !=  false  &&  sleepTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  sleepTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  paramVariable  !=  null  &&  paramVariable  .  getChannelRB  (  )  !=  null  &&  paramVariable  .  getMonitoredPV_RB  (  )  .  isGood  (  )  )  { 
setParamCurrentValueRB  (  paramVariable  .  getValueRB  (  )  )  ; 
}  else  { 
paramTextRB  .  setText  (  null  )  ; 
} 
} 








private   boolean   trueMeasure  (  double   val  )  { 
scanValue  =  val  ; 
if  (  scanVariable  !=  null  )  { 
scanVariable  .  setValue  (  scanValue  )  ; 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
if  (  sleepTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  sleepTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
for  (  int   k  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  k  <  n  ;  k  ++  )  { 
if  (  !  scanOn  )  { 
return   false  ; 
} 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  k  )  )  .  restoreIniState  (  )  ; 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
int   j  =  0  ; 
int   badCount  =  0  ; 
while  (  j  <  nAveraging  )  { 
badCount  =  0  ; 
while  (  true  )  { 
if  (  validateMeasurements  (  )  &&  mpsStatusValidateMeasurements  (  )  )  { 
for  (  int   k  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  k  <  n  ;  k  ++  )  { 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  k  )  )  .  measure  (  )  ; 
if  (  !  scanOn  )  { 
return   false  ; 
} 
} 
j  ++  ; 
break  ; 
}  else   if  (  mpsStatusValidateMeasurements  (  )  )  { 
if  (  !  scanOn  )  { 
Toolkit  .  getDefaultToolkit  (  )  .  beep  (  )  ; 
return   false  ; 
} 
scanOn  =  false  ; 
scanVarShouldBeRestored  =  false  ; 
if  (  measurementThread  !=  null  &&  measurementThread  .  isAlive  (  )  )  { 
measurementThread  .  interrupt  (  )  ; 
} 
JOptionPane  .  showMessageDialog  (  controllerPanel  .  getRootPane  (  )  ,  "The MPS has been masked."  )  ; 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
if  (  nAveraging  >  1  &&  badCount  >  0  &&  avrgTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  avrgTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  nAveraging  ==  1  &&  badCount  >  0  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  Math  .  max  (  sleepTime  ,  avrgTime  )  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
badCount  ++  ; 
if  (  badCount  >  maxNumberBadMeasurements  )  { 
scanOn  =  false  ; 
messageText  .  setText  (  "Cannot validate measurements."  )  ; 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
if  (  nAveraging  >  1  &&  j  !=  nAveraging  &&  avrgTime  >  0.  )  { 
try  { 
lockObj  .  wait  (  (  long  )  (  1000.0  *  avrgTime  )  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  !  scanOn  )  { 
return   false  ; 
} 
} 
setCurrentValue  (  scanValue  )  ; 
if  (  scanVariable  !=  null  &&  scanVariable  .  getChannelRB  (  )  !=  null  &&  scanVariable  .  getMonitoredPV_RB  (  )  .  isGood  (  )  )  { 
setCurrentValueRB  (  scanVariable  .  getValueRB  (  )  )  ; 
}  else  { 
scanValueRB  =  scanValue  ; 
valueTextRB  .  setText  (  null  )  ; 
} 
accountNewDataPoint  (  )  ; 
return   true  ; 
} 






private   boolean   trueMeasure  (  )  { 
int   newPositionInd  =  positionInd  ; 
for  (  int   i  =  positionInd  ;  i  <  nPoints  ;  i  ++  )  { 
if  (  !  trueMeasure  (  variableSet  [  i  ]  )  )  { 
break  ; 
} 
newPositionInd  =  i  +  1  ; 
} 
positionInd  =  newPositionInd  ; 
if  (  positionInd  !=  nPoints  )  { 
return   false  ; 
} 
return   true  ; 
} 






private   boolean   validateMeasurements  (  )  { 
if  (  validateMeasurement  &&  validationController  !=  null  )  { 
double   validVal  =  0.  ; 
for  (  int   i  =  0  ,  n  =  validationValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
MeasuredValue   mv  =  (  MeasuredValue  )  validationValuesV  .  get  (  i  )  ; 
validVal  =  mv  .  getValue  (  )  ; 
if  (  validVal  <  lowValidationLim  ||  validVal  >  uppValidationLim  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 






private   boolean   mpsStatusValidateMeasurements  (  )  { 
if  (  mpsStatusValidateMeasurement  &&  mpsStatusValidator  !=  null  )  { 
int   validVal  =  0  ; 
for  (  int   i  =  0  ,  n  =  mpsStatusValidationValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
MeasuredValue   mv  =  (  MeasuredValue  )  mpsStatusValidationValuesV  .  get  (  i  )  ; 
validVal  =  (  int  )  mv  .  getValue  (  )  ; 
if  (  (  validVal  &  0x2  )  ==  1  &&  (  validVal  &  0x5  )  !=  5  )  { 
return   false  ; 
} 
} 
} 
return   true  ; 
} 




private   void   startNewSetOfData  (  )  { 
for  (  int   i  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  i  )  )  .  createNewDataContainer  (  )  ; 
} 
if  (  scanVariable  !=  null  &&  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
for  (  int   i  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  i  )  )  .  createNewDataContainerRB  (  )  ; 
} 
} 
for  (  int   i  =  0  ,  n  =  newSetOfDataListenersV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  ActionListener  )  newSetOfDataListenersV  .  get  (  i  )  )  .  actionPerformed  (  newSetOfDataAction  )  ; 
} 
} 




private   void   accountNewDataPoint  (  )  { 
for  (  int   i  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  i  )  )  .  consumeData  (  scanValue  )  ; 
} 
if  (  scanVariable  !=  null  &&  scanVariable  .  getChannelRB  (  )  !=  null  )  { 
for  (  int   i  =  0  ,  n  =  measuredValuesV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  MeasuredValue  )  measuredValuesV  .  get  (  i  )  )  .  consumeDataRB  (  scanValueRB  )  ; 
} 
} 
for  (  int   i  =  0  ,  n  =  newPointOfDataListenersV  .  size  (  )  ;  i  <  n  ;  i  ++  )  { 
(  (  ActionListener  )  newPointOfDataListenersV  .  get  (  i  )  )  .  actionPerformed  (  newPointOfDataAction  )  ; 
} 
} 






public   boolean   getParamPhaseScanButtonVizible  (  )  { 
if  (  paramPhaseScanAndRB_Panel  .  getComponentCount  (  )  ==  2  )  { 
return   false  ; 
} 
return   true  ; 
} 






public   boolean   getValuePhaseScanButtonVizible  (  )  { 
if  (  valuePhaseScanAndRB_Panel  .  getComponentCount  (  )  ==  2  )  { 
return   false  ; 
} 
return   true  ; 
} 






public   JLabel   getValueRB_Label  (  )  { 
return   valueRB_Label  ; 
} 






public   JLabel   getScanStep_Label  (  )  { 
return   scanStep_Label  ; 
} 






public   JLabel   getUnitsLabel  (  )  { 
return   unitsLabel  ; 
} 






public   JTextField   getValueText  (  )  { 
return   valueText  ; 
} 






public   JTextField   getValueTextRB  (  )  { 
return   valueTextRB  ; 
} 






public   JTextField   getMessageText  (  )  { 
return   messageText  ; 
} 






public   JLabel   getParamRB_Label  (  )  { 
return   paramRB_Label  ; 
} 






public   JLabel   getParamScanStep_Label  (  )  { 
return   paramScanStep_Label  ; 
} 






public   JLabel   getParamUnitsLabel  (  )  { 
return   paramUnitsLabel  ; 
} 

public   void   setStartButtonEnabled  (  boolean   onOff  )  { 
startButton  .  setEnabled  (  onOff  )  ; 
} 

public   void   setResumeButtonEnabled  (  boolean   onOff  )  { 
resumeButton  =  new   JButton  (  "RESUME"  )  ; 
} 

public   void   setStopButtonEnabled  (  boolean   onOff  )  { 
stopButton  =  new   JButton  (  " STOP "  )  ; 
} 






public   static   void   main  (  String   args  [  ]  )  { 
JFrame   mainFrame  =  new   JFrame  (  "Test of the IndependentValueRange class"  )  ; 
mainFrame  .  addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

@  Override 
public   void   windowClosing  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
System  .  exit  (  0  )  ; 
} 
}  )  ; 
mainFrame  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
JPanel   tmp_p  =  new   JPanel  (  )  ; 
tmp_p  .  setLayout  (  new   BorderLayout  (  )  )  ; 
mainFrame  .  getContentPane  (  )  .  add  (  tmp_p  ,  BorderLayout  .  WEST  )  ; 
final   Scan2D   iRange  =  new   Scan2D  (  "SCAN CONTROL PANEL"  )  ; 
iRange  .  getUnitsLabel  (  )  .  setText  (  " kV "  )  ; 
iRange  .  setLowLimit  (  -  10.0  )  ; 
iRange  .  setUppLimit  (  10.0  )  ; 
iRange  .  setStep  (  5.0  )  ; 
iRange  .  getParamUnitsLabel  (  )  .  setText  (  " Amp "  )  ; 
iRange  .  setParamLowLimit  (  -  1.0  )  ; 
iRange  .  setParamUppLimit  (  1.0  )  ; 
iRange  .  setParamStep  (  0.1  )  ; 
tmp_p  .  add  (  iRange  .  getJPanel  (  )  ,  BorderLayout  .  NORTH  )  ; 
iRange  .  addNewSetOfDataListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
System  .  out  .  println  (  "debug param="  +  iRange  .  getParamValue  (  )  )  ; 
} 
}  )  ; 
mainFrame  .  pack  (  )  ; 
mainFrame  .  setSize  (  new   Dimension  (  300  ,  430  )  )  ; 
mainFrame  .  setVisible  (  true  )  ; 
} 
} 

