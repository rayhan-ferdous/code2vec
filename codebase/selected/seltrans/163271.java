package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   java  .  util  .  logging  .  Level  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  MeetMe  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 










































public   class   MeetMeImpl   extends   AsteriskActionStepImpl   implements   MeetMe  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   conferenceNumber  ; 









protected   DynamicValue   pin  ; 









protected   static   final   String   BACKGROUND_SCRIPT_AGI_EDEFAULT  =  null  ; 









protected   String   backgroundScriptAgi  =  BACKGROUND_SCRIPT_AGI_EDEFAULT  ; 









protected   static   final   String   RECORDING_FILENAME_EDEFAULT  =  null  ; 









protected   String   recordingFilename  =  RECORDING_FILENAME_EDEFAULT  ; 









protected   static   final   String   RECORDING_FORMAT_EDEFAULT  =  null  ; 









protected   String   recordingFormat  =  RECORDING_FORMAT_EDEFAULT  ; 









protected   static   final   boolean   ALONE_MESSAGE_ENABLED_EDEFAULT  =  true  ; 









protected   boolean   aloneMessageEnabled  =  ALONE_MESSAGE_ENABLED_EDEFAULT  ; 









protected   static   final   boolean   ADMIN_MODE_EDEFAULT  =  false  ; 









protected   boolean   adminMode  =  ADMIN_MODE_EDEFAULT  ; 









protected   static   final   boolean   USE_AGI_SCRIPT_EDEFAULT  =  false  ; 









protected   boolean   useAGIScript  =  USE_AGI_SCRIPT_EDEFAULT  ; 









protected   static   final   boolean   ANNOUNCE_COUNT_EDEFAULT  =  false  ; 









protected   boolean   announceCount  =  ANNOUNCE_COUNT_EDEFAULT  ; 









protected   static   final   boolean   DYNAMICALLY_ADD_CONFERENCE_EDEFAULT  =  true  ; 









protected   boolean   dynamicallyAddConference  =  DYNAMICALLY_ADD_CONFERENCE_EDEFAULT  ; 









protected   static   final   boolean   SELECT_EMPTY_CONFERENCE_EDEFAULT  =  false  ; 









protected   boolean   selectEmptyConference  =  SELECT_EMPTY_CONFERENCE_EDEFAULT  ; 









protected   static   final   boolean   SELECT_EMPTY_PINLESS_CONFERENCE_EDEFAULT  =  false  ; 









protected   boolean   selectEmptyPinlessConference  =  SELECT_EMPTY_PINLESS_CONFERENCE_EDEFAULT  ; 









protected   static   final   boolean   PASS_DTMF_EDEFAULT  =  false  ; 









protected   boolean   passDTMF  =  PASS_DTMF_EDEFAULT  ; 









protected   static   final   boolean   ANNOUNCE_JOIN_LEAVE_EDEFAULT  =  false  ; 









protected   boolean   announceJoinLeave  =  ANNOUNCE_JOIN_LEAVE_EDEFAULT  ; 









protected   static   final   boolean   ANNOUNCE_JOIN_LEAVE_NO_REVIEW_EDEFAULT  =  false  ; 









protected   boolean   announceJoinLeaveNoReview  =  ANNOUNCE_JOIN_LEAVE_NO_REVIEW_EDEFAULT  ; 









protected   static   final   boolean   USE_MUSIC_ON_HOLD_EDEFAULT  =  true  ; 









protected   boolean   useMusicOnHold  =  USE_MUSIC_ON_HOLD_EDEFAULT  ; 









protected   static   final   boolean   MONITOR_ONLY_MODE_EDEFAULT  =  false  ; 









protected   boolean   monitorOnlyMode  =  MONITOR_ONLY_MODE_EDEFAULT  ; 









protected   static   final   boolean   ALLOW_POUND_USER_EXIT_EDEFAULT  =  false  ; 









protected   boolean   allowPoundUserExit  =  ALLOW_POUND_USER_EXIT_EDEFAULT  ; 









protected   static   final   boolean   ALWAYS_PROMPT_FOR_PIN_EDEFAULT  =  false  ; 









protected   boolean   alwaysPromptForPin  =  ALWAYS_PROMPT_FOR_PIN_EDEFAULT  ; 









protected   static   final   boolean   QUIET_MODE_EDEFAULT  =  false  ; 









protected   boolean   quietMode  =  QUIET_MODE_EDEFAULT  ; 









protected   static   final   boolean   RECORD_CONFERENCE_EDEFAULT  =  false  ; 









protected   boolean   recordConference  =  RECORD_CONFERENCE_EDEFAULT  ; 









protected   static   final   boolean   PLAY_MENU_ON_STAR_EDEFAULT  =  false  ; 









protected   boolean   playMenuOnStar  =  PLAY_MENU_ON_STAR_EDEFAULT  ; 









protected   static   final   boolean   TALK_ONLY_MODE_EDEFAULT  =  false  ; 









protected   boolean   talkOnlyMode  =  TALK_ONLY_MODE_EDEFAULT  ; 









protected   static   final   boolean   TALKER_DETECTION_EDEFAULT  =  false  ; 









protected   boolean   talkerDetection  =  TALKER_DETECTION_EDEFAULT  ; 









protected   static   final   boolean   VIDEO_MODE_EDEFAULT  =  false  ; 









protected   boolean   videoMode  =  VIDEO_MODE_EDEFAULT  ; 









protected   static   final   boolean   WAIT_FOR_MARKED_USER_EDEFAULT  =  false  ; 









protected   boolean   waitForMarkedUser  =  WAIT_FOR_MARKED_USER_EDEFAULT  ; 









protected   static   final   boolean   EXIT_ON_EXTENSION_ENTERED_EDEFAULT  =  false  ; 









protected   boolean   exitOnExtensionEntered  =  EXIT_ON_EXTENSION_ENTERED_EDEFAULT  ; 









protected   static   final   boolean   CLOSE_ON_LAST_MARKED_USER_EXIT_EDEFAULT  =  false  ; 









protected   boolean   closeOnLastMarkedUserExit  =  CLOSE_ON_LAST_MARKED_USER_EXIT_EDEFAULT  ; 






protected   MeetMeImpl  (  )  { 
super  (  )  ; 
} 

@  Override 
public   void   beginProcessing  (  SafletContext   context  )  throws   ActionStepException  { 
super  .  beginProcessing  (  context  )  ; 
Exception   exception  =  null  ; 
if  (  call1  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No current call found"  )  )  ; 
return  ; 
}  else   if  (  !  (  call1   instanceof   Call  )  )  { 
handleException  (  context  ,  new   ActionStepException  (  "Call isn't isn't an Asterisk call: "  +  call1  .  getClass  (  )  .  getName  (  )  )  )  ; 
return  ; 
} 
if  (  (  (  Call  )  call1  )  .  getChannel  (  )  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No channel found in current context"  )  )  ; 
return  ; 
} 
AgiChannel   channel  =  (  (  Call  )  call1  )  .  getChannel  (  )  ; 
try  { 
String   meetmeStr  =  null  ; 
if  (  conferenceNumber  !=  null  )  { 
Object   dynValue  =  resolveDynamicValue  (  conferenceNumber  ,  context  )  ; 
meetmeStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
} 
if  (  StringUtils  .  isBlank  (  meetmeStr  )  )  { 
exception  =  new   ActionStepException  (  "Conference number is required"  )  ; 
}  else  { 
if  (  StringUtils  .  isNotBlank  (  recordingFilename  )  )  { 
channel  .  setVariable  (  "MEETME_RECORDINGFILE"  ,  recordingFilename  )  ; 
} 
if  (  StringUtils  .  isNotBlank  (  recordingFormat  )  )  { 
channel  .  setVariable  (  "RECORDINGFORMAT"  ,  recordingFormat  )  ; 
} 
if  (  StringUtils  .  isNotBlank  (  backgroundScriptAgi  )  )  { 
channel  .  setVariable  (  "MEETME_AGI_BACKGROUND"  ,  backgroundScriptAgi  )  ; 
} 
StringBuffer   args  =  new   StringBuffer  (  meetmeStr  )  ; 
args  .  append  (  '|'  )  ; 
if  (  adminMode  )  args  .  append  (  'a'  )  ; 
if  (  allowPoundUserExit  )  args  .  append  (  'p'  )  ; 
if  (  !  aloneMessageEnabled  )  args  .  append  (  '1'  )  ; 
if  (  alwaysPromptForPin  )  args  .  append  (  'P'  )  ; 
if  (  announceCount  )  args  .  append  (  'c'  )  ; 
if  (  announceJoinLeaveNoReview  )  args  .  append  (  'I'  )  ;  else   if  (  announceJoinLeave  )  args  .  append  (  'i'  )  ; 
if  (  closeOnLastMarkedUserExit  )  args  .  append  (  'x'  )  ; 
String   pinStr  =  null  ; 
if  (  pin  !=  null  )  { 
Object   dynValue  =  resolveDynamicValue  (  pin  ,  context  )  ; 
pinStr  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  dynValue  )  ; 
} 
if  (  dynamicallyAddConference  )  args  .  append  (  StringUtils  .  isNotBlank  (  pinStr  )  ?  'D'  :  'd'  )  ; 
if  (  exitOnExtensionEntered  )  args  .  append  (  'x'  )  ; 
if  (  monitorOnlyMode  )  args  .  append  (  'm'  )  ; 
if  (  passDTMF  )  args  .  append  (  'F'  )  ; 
if  (  playMenuOnStar  )  args  .  append  (  's'  )  ; 
if  (  quietMode  )  args  .  append  (  'q'  )  ; 
if  (  recordConference  )  args  .  append  (  'r'  )  ; 
if  (  selectEmptyPinlessConference  )  args  .  append  (  'E'  )  ;  else   if  (  selectEmptyConference  )  args  .  append  (  'e'  )  ; 
if  (  talkerDetection  )  args  .  append  (  'T'  )  ; 
if  (  talkOnlyMode  )  args  .  append  (  't'  )  ; 
if  (  useMusicOnHold  )  args  .  append  (  'M'  )  ; 
if  (  videoMode  )  args  .  append  (  'v'  )  ; 
if  (  waitForMarkedUser  )  args  .  append  (  'w'  )  ; 
if  (  StringUtils  .  isNotBlank  (  pinStr  )  )  args  .  append  (  ','  )  .  append  (  pinStr  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Executing MeetMe app with args "  +  args  )  ; 
int   result  =  channel  .  exec  (  "MeetMe"  ,  args  .  toString  (  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "MeetMe return value was "  +  translateAppReturnValue  (  result  )  )  ; 
if  (  result  ==  -  1  )  { 
exception  =  new   ActionStepException  (  "Channel was hung up"  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
exception  =  e  ; 
} 
if  (  exception  !=  null  )  { 
handleException  (  context  ,  exception  )  ; 
return  ; 
} 
handleSuccess  (  context  )  ; 
} 






@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  MEET_ME  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  MEET_ME__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 
} 
return   call1  ; 
} 






public   SafiCall   basicGetCall1  (  )  { 
return   call1  ; 
} 






public   void   setCall1  (  SafiCall   newCall1  )  { 
SafiCall   oldCall1  =  call1  ; 
call1  =  newCall1  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getConferenceNumber  (  )  { 
return   conferenceNumber  ; 
} 






public   NotificationChain   basicSetConferenceNumber  (  DynamicValue   newConferenceNumber  ,  NotificationChain   msgs  )  { 
DynamicValue   oldConferenceNumber  =  conferenceNumber  ; 
conferenceNumber  =  newConferenceNumber  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  ,  oldConferenceNumber  ,  newConferenceNumber  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setConferenceNumber  (  DynamicValue   newConferenceNumber  )  { 
if  (  newConferenceNumber  !=  conferenceNumber  )  { 
NotificationChain   msgs  =  null  ; 
if  (  conferenceNumber  !=  null  )  msgs  =  (  (  InternalEObject  )  conferenceNumber  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  ,  null  ,  msgs  )  ; 
if  (  newConferenceNumber  !=  null  )  msgs  =  (  (  InternalEObject  )  newConferenceNumber  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  ,  null  ,  msgs  )  ; 
msgs  =  basicSetConferenceNumber  (  newConferenceNumber  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  ,  newConferenceNumber  ,  newConferenceNumber  )  )  ; 
} 






public   DynamicValue   getPin  (  )  { 
return   pin  ; 
} 






public   NotificationChain   basicSetPin  (  DynamicValue   newPin  ,  NotificationChain   msgs  )  { 
DynamicValue   oldPin  =  pin  ; 
pin  =  newPin  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__PIN  ,  oldPin  ,  newPin  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setPin  (  DynamicValue   newPin  )  { 
if  (  newPin  !=  pin  )  { 
NotificationChain   msgs  =  null  ; 
if  (  pin  !=  null  )  msgs  =  (  (  InternalEObject  )  pin  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  MEET_ME__PIN  ,  null  ,  msgs  )  ; 
if  (  newPin  !=  null  )  msgs  =  (  (  InternalEObject  )  newPin  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  MEET_ME__PIN  ,  null  ,  msgs  )  ; 
msgs  =  basicSetPin  (  newPin  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__PIN  ,  newPin  ,  newPin  )  )  ; 
} 






public   String   getBackgroundScriptAgi  (  )  { 
return   backgroundScriptAgi  ; 
} 






public   void   setBackgroundScriptAgi  (  String   newBackgroundScriptAgi  )  { 
String   oldBackgroundScriptAgi  =  backgroundScriptAgi  ; 
backgroundScriptAgi  =  newBackgroundScriptAgi  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__BACKGROUND_SCRIPT_AGI  ,  oldBackgroundScriptAgi  ,  backgroundScriptAgi  )  )  ; 
} 






public   String   getRecordingFilename  (  )  { 
return   recordingFilename  ; 
} 






public   void   setRecordingFilename  (  String   newRecordingFilename  )  { 
String   oldRecordingFilename  =  recordingFilename  ; 
recordingFilename  =  newRecordingFilename  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__RECORDING_FILENAME  ,  oldRecordingFilename  ,  recordingFilename  )  )  ; 
} 






public   String   getRecordingFormat  (  )  { 
return   recordingFormat  ; 
} 






public   void   setRecordingFormat  (  String   newRecordingFormat  )  { 
String   oldRecordingFormat  =  recordingFormat  ; 
recordingFormat  =  newRecordingFormat  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__RECORDING_FORMAT  ,  oldRecordingFormat  ,  recordingFormat  )  )  ; 
} 






public   boolean   isAloneMessageEnabled  (  )  { 
return   aloneMessageEnabled  ; 
} 






public   void   setAloneMessageEnabled  (  boolean   newAloneMessageEnabled  )  { 
boolean   oldAloneMessageEnabled  =  aloneMessageEnabled  ; 
aloneMessageEnabled  =  newAloneMessageEnabled  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ALONE_MESSAGE_ENABLED  ,  oldAloneMessageEnabled  ,  aloneMessageEnabled  )  )  ; 
} 






public   boolean   isAdminMode  (  )  { 
return   adminMode  ; 
} 






public   void   setAdminMode  (  boolean   newAdminMode  )  { 
boolean   oldAdminMode  =  adminMode  ; 
adminMode  =  newAdminMode  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ADMIN_MODE  ,  oldAdminMode  ,  adminMode  )  )  ; 
} 






public   boolean   isUseAGIScript  (  )  { 
return   useAGIScript  ; 
} 






public   void   setUseAGIScript  (  boolean   newUseAGIScript  )  { 
boolean   oldUseAGIScript  =  useAGIScript  ; 
useAGIScript  =  newUseAGIScript  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__USE_AGI_SCRIPT  ,  oldUseAGIScript  ,  useAGIScript  )  )  ; 
} 






public   boolean   isAnnounceCount  (  )  { 
return   announceCount  ; 
} 






public   void   setAnnounceCount  (  boolean   newAnnounceCount  )  { 
boolean   oldAnnounceCount  =  announceCount  ; 
announceCount  =  newAnnounceCount  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ANNOUNCE_COUNT  ,  oldAnnounceCount  ,  announceCount  )  )  ; 
} 






public   boolean   isDynamicallyAddConference  (  )  { 
return   dynamicallyAddConference  ; 
} 






public   void   setDynamicallyAddConference  (  boolean   newDynamicallyAddConference  )  { 
boolean   oldDynamicallyAddConference  =  dynamicallyAddConference  ; 
dynamicallyAddConference  =  newDynamicallyAddConference  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__DYNAMICALLY_ADD_CONFERENCE  ,  oldDynamicallyAddConference  ,  dynamicallyAddConference  )  )  ; 
} 






public   boolean   isSelectEmptyConference  (  )  { 
return   selectEmptyConference  ; 
} 






public   void   setSelectEmptyConference  (  boolean   newSelectEmptyConference  )  { 
boolean   oldSelectEmptyConference  =  selectEmptyConference  ; 
selectEmptyConference  =  newSelectEmptyConference  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__SELECT_EMPTY_CONFERENCE  ,  oldSelectEmptyConference  ,  selectEmptyConference  )  )  ; 
} 






public   boolean   isSelectEmptyPinlessConference  (  )  { 
return   selectEmptyPinlessConference  ; 
} 






public   void   setSelectEmptyPinlessConference  (  boolean   newSelectEmptyPinlessConference  )  { 
boolean   oldSelectEmptyPinlessConference  =  selectEmptyPinlessConference  ; 
selectEmptyPinlessConference  =  newSelectEmptyPinlessConference  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__SELECT_EMPTY_PINLESS_CONFERENCE  ,  oldSelectEmptyPinlessConference  ,  selectEmptyPinlessConference  )  )  ; 
} 






public   boolean   isPassDTMF  (  )  { 
return   passDTMF  ; 
} 






public   void   setPassDTMF  (  boolean   newPassDTMF  )  { 
boolean   oldPassDTMF  =  passDTMF  ; 
passDTMF  =  newPassDTMF  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__PASS_DTMF  ,  oldPassDTMF  ,  passDTMF  )  )  ; 
} 






public   boolean   isAnnounceJoinLeave  (  )  { 
return   announceJoinLeave  ; 
} 






public   void   setAnnounceJoinLeave  (  boolean   newAnnounceJoinLeave  )  { 
boolean   oldAnnounceJoinLeave  =  announceJoinLeave  ; 
announceJoinLeave  =  newAnnounceJoinLeave  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE  ,  oldAnnounceJoinLeave  ,  announceJoinLeave  )  )  ; 
} 






public   boolean   isAnnounceJoinLeaveNoReview  (  )  { 
return   announceJoinLeaveNoReview  ; 
} 






public   void   setAnnounceJoinLeaveNoReview  (  boolean   newAnnounceJoinLeaveNoReview  )  { 
boolean   oldAnnounceJoinLeaveNoReview  =  announceJoinLeaveNoReview  ; 
announceJoinLeaveNoReview  =  newAnnounceJoinLeaveNoReview  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE_NO_REVIEW  ,  oldAnnounceJoinLeaveNoReview  ,  announceJoinLeaveNoReview  )  )  ; 
} 






public   boolean   isUseMusicOnHold  (  )  { 
return   useMusicOnHold  ; 
} 






public   void   setUseMusicOnHold  (  boolean   newUseMusicOnHold  )  { 
boolean   oldUseMusicOnHold  =  useMusicOnHold  ; 
useMusicOnHold  =  newUseMusicOnHold  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__USE_MUSIC_ON_HOLD  ,  oldUseMusicOnHold  ,  useMusicOnHold  )  )  ; 
} 






public   boolean   isMonitorOnlyMode  (  )  { 
return   monitorOnlyMode  ; 
} 






public   void   setMonitorOnlyMode  (  boolean   newMonitorOnlyMode  )  { 
boolean   oldMonitorOnlyMode  =  monitorOnlyMode  ; 
monitorOnlyMode  =  newMonitorOnlyMode  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__MONITOR_ONLY_MODE  ,  oldMonitorOnlyMode  ,  monitorOnlyMode  )  )  ; 
} 






public   boolean   isAllowPoundUserExit  (  )  { 
return   allowPoundUserExit  ; 
} 






public   void   setAllowPoundUserExit  (  boolean   newAllowPoundUserExit  )  { 
boolean   oldAllowPoundUserExit  =  allowPoundUserExit  ; 
allowPoundUserExit  =  newAllowPoundUserExit  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ALLOW_POUND_USER_EXIT  ,  oldAllowPoundUserExit  ,  allowPoundUserExit  )  )  ; 
} 






public   boolean   isAlwaysPromptForPin  (  )  { 
return   alwaysPromptForPin  ; 
} 






public   void   setAlwaysPromptForPin  (  boolean   newAlwaysPromptForPin  )  { 
boolean   oldAlwaysPromptForPin  =  alwaysPromptForPin  ; 
alwaysPromptForPin  =  newAlwaysPromptForPin  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__ALWAYS_PROMPT_FOR_PIN  ,  oldAlwaysPromptForPin  ,  alwaysPromptForPin  )  )  ; 
} 






public   boolean   isQuietMode  (  )  { 
return   quietMode  ; 
} 






public   void   setQuietMode  (  boolean   newQuietMode  )  { 
boolean   oldQuietMode  =  quietMode  ; 
quietMode  =  newQuietMode  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__QUIET_MODE  ,  oldQuietMode  ,  quietMode  )  )  ; 
} 






public   boolean   isRecordConference  (  )  { 
return   recordConference  ; 
} 






public   void   setRecordConference  (  boolean   newRecordConference  )  { 
boolean   oldRecordConference  =  recordConference  ; 
recordConference  =  newRecordConference  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__RECORD_CONFERENCE  ,  oldRecordConference  ,  recordConference  )  )  ; 
} 






public   boolean   isPlayMenuOnStar  (  )  { 
return   playMenuOnStar  ; 
} 






public   void   setPlayMenuOnStar  (  boolean   newPlayMenuOnStar  )  { 
boolean   oldPlayMenuOnStar  =  playMenuOnStar  ; 
playMenuOnStar  =  newPlayMenuOnStar  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__PLAY_MENU_ON_STAR  ,  oldPlayMenuOnStar  ,  playMenuOnStar  )  )  ; 
} 






public   boolean   isTalkOnlyMode  (  )  { 
return   talkOnlyMode  ; 
} 






public   void   setTalkOnlyMode  (  boolean   newTalkOnlyMode  )  { 
boolean   oldTalkOnlyMode  =  talkOnlyMode  ; 
talkOnlyMode  =  newTalkOnlyMode  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__TALK_ONLY_MODE  ,  oldTalkOnlyMode  ,  talkOnlyMode  )  )  ; 
} 






public   boolean   isTalkerDetection  (  )  { 
return   talkerDetection  ; 
} 






public   void   setTalkerDetection  (  boolean   newTalkerDetection  )  { 
boolean   oldTalkerDetection  =  talkerDetection  ; 
talkerDetection  =  newTalkerDetection  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__TALKER_DETECTION  ,  oldTalkerDetection  ,  talkerDetection  )  )  ; 
} 






public   boolean   isVideoMode  (  )  { 
return   videoMode  ; 
} 






public   void   setVideoMode  (  boolean   newVideoMode  )  { 
boolean   oldVideoMode  =  videoMode  ; 
videoMode  =  newVideoMode  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__VIDEO_MODE  ,  oldVideoMode  ,  videoMode  )  )  ; 
} 






public   boolean   isWaitForMarkedUser  (  )  { 
return   waitForMarkedUser  ; 
} 






public   void   setWaitForMarkedUser  (  boolean   newWaitForMarkedUser  )  { 
boolean   oldWaitForMarkedUser  =  waitForMarkedUser  ; 
waitForMarkedUser  =  newWaitForMarkedUser  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__WAIT_FOR_MARKED_USER  ,  oldWaitForMarkedUser  ,  waitForMarkedUser  )  )  ; 
} 






public   boolean   isExitOnExtensionEntered  (  )  { 
return   exitOnExtensionEntered  ; 
} 






public   void   setExitOnExtensionEntered  (  boolean   newExitOnExtensionEntered  )  { 
boolean   oldExitOnExtensionEntered  =  exitOnExtensionEntered  ; 
exitOnExtensionEntered  =  newExitOnExtensionEntered  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__EXIT_ON_EXTENSION_ENTERED  ,  oldExitOnExtensionEntered  ,  exitOnExtensionEntered  )  )  ; 
} 






public   boolean   isCloseOnLastMarkedUserExit  (  )  { 
return   closeOnLastMarkedUserExit  ; 
} 






public   void   setCloseOnLastMarkedUserExit  (  boolean   newCloseOnLastMarkedUserExit  )  { 
boolean   oldCloseOnLastMarkedUserExit  =  closeOnLastMarkedUserExit  ; 
closeOnLastMarkedUserExit  =  newCloseOnLastMarkedUserExit  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  MEET_ME__CLOSE_ON_LAST_MARKED_USER_EXIT  ,  oldCloseOnLastMarkedUserExit  ,  closeOnLastMarkedUserExit  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  : 
return   basicSetConferenceNumber  (  null  ,  msgs  )  ; 
case   ActionstepPackage  .  MEET_ME__PIN  : 
return   basicSetPin  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  MEET_ME__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  : 
return   getConferenceNumber  (  )  ; 
case   ActionstepPackage  .  MEET_ME__PIN  : 
return   getPin  (  )  ; 
case   ActionstepPackage  .  MEET_ME__BACKGROUND_SCRIPT_AGI  : 
return   getBackgroundScriptAgi  (  )  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FILENAME  : 
return   getRecordingFilename  (  )  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FORMAT  : 
return   getRecordingFormat  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ALONE_MESSAGE_ENABLED  : 
return   isAloneMessageEnabled  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ADMIN_MODE  : 
return   isAdminMode  (  )  ; 
case   ActionstepPackage  .  MEET_ME__USE_AGI_SCRIPT  : 
return   isUseAGIScript  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_COUNT  : 
return   isAnnounceCount  (  )  ; 
case   ActionstepPackage  .  MEET_ME__DYNAMICALLY_ADD_CONFERENCE  : 
return   isDynamicallyAddConference  (  )  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_CONFERENCE  : 
return   isSelectEmptyConference  (  )  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_PINLESS_CONFERENCE  : 
return   isSelectEmptyPinlessConference  (  )  ; 
case   ActionstepPackage  .  MEET_ME__PASS_DTMF  : 
return   isPassDTMF  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE  : 
return   isAnnounceJoinLeave  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE_NO_REVIEW  : 
return   isAnnounceJoinLeaveNoReview  (  )  ; 
case   ActionstepPackage  .  MEET_ME__USE_MUSIC_ON_HOLD  : 
return   isUseMusicOnHold  (  )  ; 
case   ActionstepPackage  .  MEET_ME__MONITOR_ONLY_MODE  : 
return   isMonitorOnlyMode  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ALLOW_POUND_USER_EXIT  : 
return   isAllowPoundUserExit  (  )  ; 
case   ActionstepPackage  .  MEET_ME__ALWAYS_PROMPT_FOR_PIN  : 
return   isAlwaysPromptForPin  (  )  ; 
case   ActionstepPackage  .  MEET_ME__QUIET_MODE  : 
return   isQuietMode  (  )  ; 
case   ActionstepPackage  .  MEET_ME__RECORD_CONFERENCE  : 
return   isRecordConference  (  )  ; 
case   ActionstepPackage  .  MEET_ME__PLAY_MENU_ON_STAR  : 
return   isPlayMenuOnStar  (  )  ; 
case   ActionstepPackage  .  MEET_ME__TALK_ONLY_MODE  : 
return   isTalkOnlyMode  (  )  ; 
case   ActionstepPackage  .  MEET_ME__TALKER_DETECTION  : 
return   isTalkerDetection  (  )  ; 
case   ActionstepPackage  .  MEET_ME__VIDEO_MODE  : 
return   isVideoMode  (  )  ; 
case   ActionstepPackage  .  MEET_ME__WAIT_FOR_MARKED_USER  : 
return   isWaitForMarkedUser  (  )  ; 
case   ActionstepPackage  .  MEET_ME__EXIT_ON_EXTENSION_ENTERED  : 
return   isExitOnExtensionEntered  (  )  ; 
case   ActionstepPackage  .  MEET_ME__CLOSE_ON_LAST_MARKED_USER_EXIT  : 
return   isCloseOnLastMarkedUserExit  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  MEET_ME__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  : 
setConferenceNumber  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PIN  : 
setPin  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__BACKGROUND_SCRIPT_AGI  : 
setBackgroundScriptAgi  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FILENAME  : 
setRecordingFilename  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FORMAT  : 
setRecordingFormat  (  (  String  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALONE_MESSAGE_ENABLED  : 
setAloneMessageEnabled  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ADMIN_MODE  : 
setAdminMode  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__USE_AGI_SCRIPT  : 
setUseAGIScript  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_COUNT  : 
setAnnounceCount  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__DYNAMICALLY_ADD_CONFERENCE  : 
setDynamicallyAddConference  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_CONFERENCE  : 
setSelectEmptyConference  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_PINLESS_CONFERENCE  : 
setSelectEmptyPinlessConference  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PASS_DTMF  : 
setPassDTMF  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE  : 
setAnnounceJoinLeave  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE_NO_REVIEW  : 
setAnnounceJoinLeaveNoReview  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__USE_MUSIC_ON_HOLD  : 
setUseMusicOnHold  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__MONITOR_ONLY_MODE  : 
setMonitorOnlyMode  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALLOW_POUND_USER_EXIT  : 
setAllowPoundUserExit  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALWAYS_PROMPT_FOR_PIN  : 
setAlwaysPromptForPin  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__QUIET_MODE  : 
setQuietMode  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORD_CONFERENCE  : 
setRecordConference  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PLAY_MENU_ON_STAR  : 
setPlayMenuOnStar  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__TALK_ONLY_MODE  : 
setTalkOnlyMode  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__TALKER_DETECTION  : 
setTalkerDetection  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__VIDEO_MODE  : 
setVideoMode  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__WAIT_FOR_MARKED_USER  : 
setWaitForMarkedUser  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__EXIT_ON_EXTENSION_ENTERED  : 
setExitOnExtensionEntered  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__CLOSE_ON_LAST_MARKED_USER_EXIT  : 
setCloseOnLastMarkedUserExit  (  (  Boolean  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  MEET_ME__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  : 
setConferenceNumber  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PIN  : 
setPin  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__BACKGROUND_SCRIPT_AGI  : 
setBackgroundScriptAgi  (  BACKGROUND_SCRIPT_AGI_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FILENAME  : 
setRecordingFilename  (  RECORDING_FILENAME_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FORMAT  : 
setRecordingFormat  (  RECORDING_FORMAT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALONE_MESSAGE_ENABLED  : 
setAloneMessageEnabled  (  ALONE_MESSAGE_ENABLED_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ADMIN_MODE  : 
setAdminMode  (  ADMIN_MODE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__USE_AGI_SCRIPT  : 
setUseAGIScript  (  USE_AGI_SCRIPT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_COUNT  : 
setAnnounceCount  (  ANNOUNCE_COUNT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__DYNAMICALLY_ADD_CONFERENCE  : 
setDynamicallyAddConference  (  DYNAMICALLY_ADD_CONFERENCE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_CONFERENCE  : 
setSelectEmptyConference  (  SELECT_EMPTY_CONFERENCE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_PINLESS_CONFERENCE  : 
setSelectEmptyPinlessConference  (  SELECT_EMPTY_PINLESS_CONFERENCE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PASS_DTMF  : 
setPassDTMF  (  PASS_DTMF_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE  : 
setAnnounceJoinLeave  (  ANNOUNCE_JOIN_LEAVE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE_NO_REVIEW  : 
setAnnounceJoinLeaveNoReview  (  ANNOUNCE_JOIN_LEAVE_NO_REVIEW_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__USE_MUSIC_ON_HOLD  : 
setUseMusicOnHold  (  USE_MUSIC_ON_HOLD_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__MONITOR_ONLY_MODE  : 
setMonitorOnlyMode  (  MONITOR_ONLY_MODE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALLOW_POUND_USER_EXIT  : 
setAllowPoundUserExit  (  ALLOW_POUND_USER_EXIT_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__ALWAYS_PROMPT_FOR_PIN  : 
setAlwaysPromptForPin  (  ALWAYS_PROMPT_FOR_PIN_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__QUIET_MODE  : 
setQuietMode  (  QUIET_MODE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__RECORD_CONFERENCE  : 
setRecordConference  (  RECORD_CONFERENCE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__PLAY_MENU_ON_STAR  : 
setPlayMenuOnStar  (  PLAY_MENU_ON_STAR_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__TALK_ONLY_MODE  : 
setTalkOnlyMode  (  TALK_ONLY_MODE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__TALKER_DETECTION  : 
setTalkerDetection  (  TALKER_DETECTION_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__VIDEO_MODE  : 
setVideoMode  (  VIDEO_MODE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__WAIT_FOR_MARKED_USER  : 
setWaitForMarkedUser  (  WAIT_FOR_MARKED_USER_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__EXIT_ON_EXTENSION_ENTERED  : 
setExitOnExtensionEntered  (  EXIT_ON_EXTENSION_ENTERED_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  MEET_ME__CLOSE_ON_LAST_MARKED_USER_EXIT  : 
setCloseOnLastMarkedUserExit  (  CLOSE_ON_LAST_MARKED_USER_EXIT_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  MEET_ME__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  MEET_ME__CONFERENCE_NUMBER  : 
return   conferenceNumber  !=  null  ; 
case   ActionstepPackage  .  MEET_ME__PIN  : 
return   pin  !=  null  ; 
case   ActionstepPackage  .  MEET_ME__BACKGROUND_SCRIPT_AGI  : 
return   BACKGROUND_SCRIPT_AGI_EDEFAULT  ==  null  ?  backgroundScriptAgi  !=  null  :  !  BACKGROUND_SCRIPT_AGI_EDEFAULT  .  equals  (  backgroundScriptAgi  )  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FILENAME  : 
return   RECORDING_FILENAME_EDEFAULT  ==  null  ?  recordingFilename  !=  null  :  !  RECORDING_FILENAME_EDEFAULT  .  equals  (  recordingFilename  )  ; 
case   ActionstepPackage  .  MEET_ME__RECORDING_FORMAT  : 
return   RECORDING_FORMAT_EDEFAULT  ==  null  ?  recordingFormat  !=  null  :  !  RECORDING_FORMAT_EDEFAULT  .  equals  (  recordingFormat  )  ; 
case   ActionstepPackage  .  MEET_ME__ALONE_MESSAGE_ENABLED  : 
return   aloneMessageEnabled  !=  ALONE_MESSAGE_ENABLED_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ADMIN_MODE  : 
return   adminMode  !=  ADMIN_MODE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__USE_AGI_SCRIPT  : 
return   useAGIScript  !=  USE_AGI_SCRIPT_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_COUNT  : 
return   announceCount  !=  ANNOUNCE_COUNT_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__DYNAMICALLY_ADD_CONFERENCE  : 
return   dynamicallyAddConference  !=  DYNAMICALLY_ADD_CONFERENCE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_CONFERENCE  : 
return   selectEmptyConference  !=  SELECT_EMPTY_CONFERENCE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__SELECT_EMPTY_PINLESS_CONFERENCE  : 
return   selectEmptyPinlessConference  !=  SELECT_EMPTY_PINLESS_CONFERENCE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__PASS_DTMF  : 
return   passDTMF  !=  PASS_DTMF_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE  : 
return   announceJoinLeave  !=  ANNOUNCE_JOIN_LEAVE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ANNOUNCE_JOIN_LEAVE_NO_REVIEW  : 
return   announceJoinLeaveNoReview  !=  ANNOUNCE_JOIN_LEAVE_NO_REVIEW_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__USE_MUSIC_ON_HOLD  : 
return   useMusicOnHold  !=  USE_MUSIC_ON_HOLD_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__MONITOR_ONLY_MODE  : 
return   monitorOnlyMode  !=  MONITOR_ONLY_MODE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ALLOW_POUND_USER_EXIT  : 
return   allowPoundUserExit  !=  ALLOW_POUND_USER_EXIT_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__ALWAYS_PROMPT_FOR_PIN  : 
return   alwaysPromptForPin  !=  ALWAYS_PROMPT_FOR_PIN_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__QUIET_MODE  : 
return   quietMode  !=  QUIET_MODE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__RECORD_CONFERENCE  : 
return   recordConference  !=  RECORD_CONFERENCE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__PLAY_MENU_ON_STAR  : 
return   playMenuOnStar  !=  PLAY_MENU_ON_STAR_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__TALK_ONLY_MODE  : 
return   talkOnlyMode  !=  TALK_ONLY_MODE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__TALKER_DETECTION  : 
return   talkerDetection  !=  TALKER_DETECTION_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__VIDEO_MODE  : 
return   videoMode  !=  VIDEO_MODE_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__WAIT_FOR_MARKED_USER  : 
return   waitForMarkedUser  !=  WAIT_FOR_MARKED_USER_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__EXIT_ON_EXTENSION_ENTERED  : 
return   exitOnExtensionEntered  !=  EXIT_ON_EXTENSION_ENTERED_EDEFAULT  ; 
case   ActionstepPackage  .  MEET_ME__CLOSE_ON_LAST_MARKED_USER_EXIT  : 
return   closeOnLastMarkedUserExit  !=  CLOSE_ON_LAST_MARKED_USER_EXIT_EDEFAULT  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  MEET_ME__CALL1  : 
return   CallPackage  .  CALL_CONSUMER1__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eBaseStructuralFeatureID  (  derivedFeatureID  ,  baseClass  )  ; 
} 






@  Override 
public   int   eDerivedStructuralFeatureID  (  int   baseFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  baseFeatureID  )  { 
case   CallPackage  .  CALL_CONSUMER1__CALL1  : 
return   ActionstepPackage  .  MEET_ME__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eDerivedStructuralFeatureID  (  baseFeatureID  ,  baseClass  )  ; 
} 






@  Override 
public   String   toString  (  )  { 
if  (  eIsProxy  (  )  )  return   super  .  toString  (  )  ; 
StringBuffer   result  =  new   StringBuffer  (  super  .  toString  (  )  )  ; 
result  .  append  (  " (backgroundScriptAgi: "  )  ; 
result  .  append  (  backgroundScriptAgi  )  ; 
result  .  append  (  ", recordingFilename: "  )  ; 
result  .  append  (  recordingFilename  )  ; 
result  .  append  (  ", recordingFormat: "  )  ; 
result  .  append  (  recordingFormat  )  ; 
result  .  append  (  ", aloneMessageEnabled: "  )  ; 
result  .  append  (  aloneMessageEnabled  )  ; 
result  .  append  (  ", adminMode: "  )  ; 
result  .  append  (  adminMode  )  ; 
result  .  append  (  ", useAGIScript: "  )  ; 
result  .  append  (  useAGIScript  )  ; 
result  .  append  (  ", announceCount: "  )  ; 
result  .  append  (  announceCount  )  ; 
result  .  append  (  ", dynamicallyAddConference: "  )  ; 
result  .  append  (  dynamicallyAddConference  )  ; 
result  .  append  (  ", selectEmptyConference: "  )  ; 
result  .  append  (  selectEmptyConference  )  ; 
result  .  append  (  ", selectEmptyPinlessConference: "  )  ; 
result  .  append  (  selectEmptyPinlessConference  )  ; 
result  .  append  (  ", passDTMF: "  )  ; 
result  .  append  (  passDTMF  )  ; 
result  .  append  (  ", announceJoinLeave: "  )  ; 
result  .  append  (  announceJoinLeave  )  ; 
result  .  append  (  ", announceJoinLeaveNoReview: "  )  ; 
result  .  append  (  announceJoinLeaveNoReview  )  ; 
result  .  append  (  ", useMusicOnHold: "  )  ; 
result  .  append  (  useMusicOnHold  )  ; 
result  .  append  (  ", monitorOnlyMode: "  )  ; 
result  .  append  (  monitorOnlyMode  )  ; 
result  .  append  (  ", allowPoundUserExit: "  )  ; 
result  .  append  (  allowPoundUserExit  )  ; 
result  .  append  (  ", alwaysPromptForPin: "  )  ; 
result  .  append  (  alwaysPromptForPin  )  ; 
result  .  append  (  ", quietMode: "  )  ; 
result  .  append  (  quietMode  )  ; 
result  .  append  (  ", recordConference: "  )  ; 
result  .  append  (  recordConference  )  ; 
result  .  append  (  ", playMenuOnStar: "  )  ; 
result  .  append  (  playMenuOnStar  )  ; 
result  .  append  (  ", talkOnlyMode: "  )  ; 
result  .  append  (  talkOnlyMode  )  ; 
result  .  append  (  ", talkerDetection: "  )  ; 
result  .  append  (  talkerDetection  )  ; 
result  .  append  (  ", videoMode: "  )  ; 
result  .  append  (  videoMode  )  ; 
result  .  append  (  ", waitForMarkedUser: "  )  ; 
result  .  append  (  waitForMarkedUser  )  ; 
result  .  append  (  ", exitOnExtensionEntered: "  )  ; 
result  .  append  (  exitOnExtensionEntered  )  ; 
result  .  append  (  ", closeOnLastMarkedUserExit: "  )  ; 
result  .  append  (  closeOnLastMarkedUserExit  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

