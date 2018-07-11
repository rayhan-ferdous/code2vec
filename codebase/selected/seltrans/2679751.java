package   jAudioFeatureExtractor  ; 

import   java  .  awt  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   javax  .  swing  .  *  ; 
import   java  .  io  .  *  ; 
import   javax  .  sound  .  sampled  .  *  ; 
import   jAudioFeatureExtractor  .  jAudioTools  .  *  ; 
import   jAudioFeatureExtractor  .  GeneralTools  .  StringMethods  ; 
























public   class   RecordingFrame   extends   JFrame   implements   ActionListener  { 

static   final   long   serialVersionUID  =  1  ; 




private   AudioInputStream   last_recorded_audio  ; 




private   AudioMethodsRecording  .  RecordThread   record_thread  ; 




private   AudioMethodsPlayback  .  PlayThread   playback_thread  ; 




Controller   controller  ; 




private   JFileChooser   save_file_chooser  ; 




private   AudioFormatJFrame   audio_format_selector  ; 




private   JButton   choose_encoding_format_button  ; 

private   JButton   display_current_audio_format_button  ; 

private   JButton   record_button  ; 

private   JButton   stop_recording_button  ; 

private   JButton   play_recording_button  ; 

private   JButton   stop_playback_button  ; 

private   JButton   cancel_button  ; 

private   JButton   save_button  ; 




private   JComboBox   choose_file_format_combo_box  ; 






public   RecordingFrame  (  Controller   c  )  { 
setTitle  (  "Record Audio"  )  ; 
Color   blue  =  new   Color  (  (  float  )  0.75  ,  (  float  )  0.85  ,  (  float  )  1.0  )  ; 
getContentPane  (  )  .  setBackground  (  blue  )  ; 
addWindowListener  (  new   WindowAdapter  (  )  { 

public   void   windowClosing  (  WindowEvent   e  )  { 
cancel  (  )  ; 
} 
}  )  ; 
controller  =  c  ; 
record_thread  =  null  ; 
last_recorded_audio  =  null  ; 
playback_thread  =  null  ; 
save_file_chooser  =  null  ; 
audio_format_selector  =  new   AudioFormatJFrame  (  )  ; 
AudioFormat   default_format  =  AudioFormatJFrame  .  getStandardMidQualityRecordAudioFormat  (  )  ; 
audio_format_selector  .  setAudioFormat  (  default_format  )  ; 
int   horizontal_gap  =  6  ; 
int   vertical_gap  =  11  ; 
setLayout  (  new   GridLayout  (  6  ,  2  ,  horizontal_gap  ,  vertical_gap  )  )  ; 
choose_encoding_format_button  =  new   JButton  (  "Change Encoding Format"  )  ; 
choose_encoding_format_button  .  addActionListener  (  this  )  ; 
add  (  choose_encoding_format_button  )  ; 
display_current_audio_format_button  =  new   JButton  (  "Display Current Encoding"  )  ; 
display_current_audio_format_button  .  addActionListener  (  this  )  ; 
add  (  display_current_audio_format_button  )  ; 
record_button  =  new   JButton  (  "Record"  )  ; 
record_button  .  addActionListener  (  this  )  ; 
add  (  record_button  )  ; 
stop_recording_button  =  new   JButton  (  "Stop Recording"  )  ; 
stop_recording_button  .  addActionListener  (  this  )  ; 
add  (  stop_recording_button  )  ; 
play_recording_button  =  new   JButton  (  "Play Last Recording"  )  ; 
play_recording_button  .  addActionListener  (  this  )  ; 
add  (  play_recording_button  )  ; 
stop_playback_button  =  new   JButton  (  "Stop Playback"  )  ; 
stop_playback_button  .  addActionListener  (  this  )  ; 
add  (  stop_playback_button  )  ; 
choose_file_format_combo_box  =  new   JComboBox  (  )  ; 
String   file_types  [  ]  =  AudioMethods  .  getAvailableFileFormatTypes  (  )  ; 
for  (  int   i  =  0  ;  i  <  file_types  .  length  ;  i  ++  )  choose_file_format_combo_box  .  addItem  (  file_types  [  i  ]  )  ; 
choose_file_format_combo_box  .  setBackground  (  this  .  getContentPane  (  )  .  getBackground  (  )  )  ; 
add  (  new   JLabel  (  "File Format For Saving:"  )  )  ; 
add  (  choose_file_format_combo_box  )  ; 
add  (  new   JLabel  (  ""  )  )  ; 
add  (  new   JLabel  (  ""  )  )  ; 
cancel_button  =  new   JButton  (  "Cancel"  )  ; 
cancel_button  .  addActionListener  (  this  )  ; 
add  (  cancel_button  )  ; 
save_button  =  new   JButton  (  "Save"  )  ; 
save_button  .  addActionListener  (  this  )  ; 
add  (  save_button  )  ; 
pack  (  )  ; 
setVisible  (  true  )  ; 
} 






public   void   actionPerformed  (  ActionEvent   event  )  { 
if  (  event  .  getSource  (  )  .  equals  (  choose_encoding_format_button  )  )  chooseEncodingFormt  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  display_current_audio_format_button  )  )  displayCurrentAudioFormat  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  record_button  )  )  record  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  stop_recording_button  )  )  stopRecording  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  play_recording_button  )  )  play  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  stop_playback_button  )  )  stopPlayback  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  cancel_button  )  )  cancel  (  )  ;  else   if  (  event  .  getSource  (  )  .  equals  (  save_button  )  )  save  (  )  ; 
} 




private   void   chooseEncodingFormt  (  )  { 
audio_format_selector  .  setVisible  (  true  )  ; 
} 




private   void   displayCurrentAudioFormat  (  )  { 
if  (  last_recorded_audio  !=  null  )  { 
String   data  =  AudioMethods  .  getAudioFormatData  (  last_recorded_audio  .  getFormat  (  )  )  ; 
JOptionPane  .  showMessageDialog  (  null  ,  data  ,  "Current Audio Encoding"  ,  JOptionPane  .  INFORMATION_MESSAGE  )  ; 
}  else   JOptionPane  .  showMessageDialog  (  null  ,  "No audio has been stored."  ,  "WARNING"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 












private   void   record  (  )  { 
try  { 
stopRecording  (  )  ; 
stopPlayback  (  )  ; 
AudioFormat   audio_format  =  audio_format_selector  .  getAudioFormat  (  true  )  ; 
TargetDataLine   target_data_line  =  AudioMethods  .  getTargetDataLine  (  audio_format  ,  null  )  ; 
record_thread  =  AudioMethodsRecording  .  recordByteArrayOutputStream  (  target_data_line  )  ; 
}  catch  (  Exception   e  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Could not record because:\n"  +  e  .  getMessage  (  )  ,  "ERROR"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 





private   void   stopRecording  (  )  { 
if  (  record_thread  !=  null  )  { 
record_thread  .  stopRecording  (  )  ; 
ByteArrayOutputStream   audio_buffer  =  record_thread  .  getRecordedData  (  )  ; 
AudioFormat   audio_buffer_format  =  record_thread  .  getFormatUsedForRecording  (  )  ; 
last_recorded_audio  =  AudioMethods  .  getInputStream  (  audio_buffer  ,  audio_buffer_format  )  ; 
record_thread  =  null  ; 
} 
} 





private   void   play  (  )  { 
if  (  last_recorded_audio  !=  null  )  { 
stopRecording  (  )  ; 
stopPlayback  (  )  ; 
SourceDataLine   source_data_line  =  AudioMethods  .  getSourceDataLine  (  last_recorded_audio  .  getFormat  (  )  ,  null  )  ; 
try  { 
playback_thread  =  AudioMethodsPlayback  .  playAudioInputStreamInterruptible  (  last_recorded_audio  ,  source_data_line  )  ; 
}  catch  (  Exception   e  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Could not play because:\n"  +  e  .  getMessage  (  )  ,  "ERROR"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 
} 






private   void   stopPlayback  (  )  { 
if  (  playback_thread  !=  null  )  { 
playback_thread  .  stopPlaying  (  )  ; 
try  { 
last_recorded_audio  .  reset  (  )  ; 
}  catch  (  Exception   e  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Could not reset playback position:\n"  +  e  .  getMessage  (  )  ,  "ERROR"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 
playback_thread  =  null  ; 
} 





private   void   cancel  (  )  { 
stopRecording  (  )  ; 
stopPlayback  (  )  ; 
last_recorded_audio  =  null  ; 
this  .  setVisible  (  false  )  ; 
} 








private   void   save  (  )  { 
if  (  last_recorded_audio  ==  null  )  { 
int   end  =  JOptionPane  .  showConfirmDialog  (  null  ,  "No recording has been made.\nDo you wish to make a recording?"  ,  "WARNING"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  end  ==  JOptionPane  .  NO_OPTION  )  cancel  (  )  ; 
}  else  { 
stopRecording  (  )  ; 
stopPlayback  (  )  ; 
if  (  save_file_chooser  ==  null  )  { 
save_file_chooser  =  new   JFileChooser  (  )  ; 
save_file_chooser  .  setCurrentDirectory  (  new   File  (  "."  )  )  ; 
save_file_chooser  .  setFileFilter  (  new   FileFilterAudio  (  )  )  ; 
} 
int   dialog_result  =  save_file_chooser  .  showSaveDialog  (  RecordingFrame  .  this  )  ; 
if  (  dialog_result  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   save_file  =  save_file_chooser  .  getSelectedFile  (  )  ; 
boolean   proceed  =  true  ; 
String   correct_format_name  =  (  String  )  choose_file_format_combo_box  .  getSelectedItem  (  )  ; 
AudioFileFormat  .  Type   correct_format  =  AudioMethods  .  getAudioFileFormatType  (  correct_format_name  )  ; 
save_file  =  ensureCorrectExtension  (  save_file  ,  correct_format  )  ; 
if  (  save_file  .  exists  (  )  )  { 
int   overwrite  =  JOptionPane  .  showConfirmDialog  (  null  ,  "This file already exists.\nDo you wish to overwrite it?"  ,  "WARNING"  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  overwrite  !=  JOptionPane  .  YES_OPTION  )  proceed  =  false  ; 
} 
if  (  proceed  )  { 
try  { 
AudioMethods  .  saveToFile  (  last_recorded_audio  ,  save_file  ,  correct_format  )  ; 
File  [  ]  to_add_to_table  =  new   File  [  1  ]  ; 
to_add_to_table  [  0  ]  =  save_file  ; 
controller  .  addRecordingsAction  .  addRecording  (  to_add_to_table  )  ; 
cancel  (  )  ; 
}  catch  (  Exception   e  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  e  .  getMessage  (  )  ,  "ERROR"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
} 
} 
} 
} 













private   File   ensureCorrectExtension  (  File   file_to_verify  ,  AudioFileFormat  .  Type   file_format_type  )  { 
String   correct_extension  =  "."  +  file_format_type  .  getExtension  (  )  ; 
String   path  =  file_to_verify  .  getAbsolutePath  (  )  ; 
String   ext  =  StringMethods  .  getExtension  (  path  )  ; 
if  (  ext  ==  null  )  path  +=  correct_extension  ;  else   if  (  !  ext  .  equals  (  correct_extension  )  )  path  =  StringMethods  .  removeExtension  (  path  )  +  correct_extension  ;  else   return   file_to_verify  ; 
JOptionPane  .  showMessageDialog  (  null  ,  "Incorrect file extension specified.\nChanged from "  +  ext  +  " to "  +  correct_extension  +  "."  ,  "WARNING"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
return   new   File  (  path  )  ; 
} 
} 

