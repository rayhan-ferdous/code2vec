package   imp  .  gui  ; 

import   imp  .  Constants  ; 
import   imp  .  ImproVisor  ; 
import   imp  .  data  .  *  ; 
import   imp  .  util  .  LeadsheetFileView  ; 
import   imp  .  util  .  MidiFilter  ; 
import   java  .  io  .  File  ; 
import   java  .  util  .  LinkedList  ; 
import   javax  .  sound  .  midi  .  InvalidMidiDataException  ; 
import   javax  .  swing  .  DefaultListModel  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   jm  .  midi  .  MidiSynth  ; 





@  SuppressWarnings  (  "serial"  ) 
public   class   MidiImportFrame   extends   javax  .  swing  .  JFrame   implements   Constants  { 

Notate   notate  ; 

File   file  ; 

MidiImport   midiImport  ; 

DefaultListModel   trackListModel  ; 

private   LinkedList  <  MidiImportRecord  >  melodies  ; 

MelodyPart   selectedPart  =  null  ; 

String   filenameDisplay  ; 

private   JFileChooser   midiFileChooser  =  new   JFileChooser  (  )  ; 





MidiSynth   jmSynth  ; 

jm  .  music  .  data  .  Score   jmScore  ; 

int   INITIAL_RESOLUTION_COMBO  =  3  ; 





public   MidiImportFrame  (  Notate   notate  )  { 
trackListModel  =  new   DefaultListModel  (  )  ; 
initComponents  (  )  ; 
this  .  notate  =  notate  ; 
midiImport  =  new   MidiImport  (  )  ; 
initFileChooser  (  )  ; 
WindowRegistry  .  registerWindow  (  this  )  ; 
volumeSpinner  .  setVisible  (  false  )  ; 
noteResolutionComboBox  .  setSelectedIndex  (  INITIAL_RESOLUTION_COMBO  )  ; 
} 

public   void   loadFileAndMenu  (  )  { 
getFile  (  )  ; 
if  (  file  !=  null  )  { 
setTitle  (  "MIDI Import: "  +  getFilenameDisplay  (  )  )  ; 
loadFile  (  )  ; 
loadMenu  (  )  ; 
} 
} 

public   void   getFile  (  )  { 
file  =  null  ; 
try  { 
midiFileChooser  .  setCurrentDirectory  (  ImproVisor  .  getMidiDirectory  (  )  )  ; 
int   midiChoice  =  midiFileChooser  .  showOpenDialog  (  notate  )  ; 
if  (  midiChoice  ==  JFileChooser  .  CANCEL_OPTION  )  { 
return  ; 
} 
if  (  midiChoice  ==  JFileChooser  .  APPROVE_OPTION  )  { 
file  =  midiFileChooser  .  getSelectedFile  (  )  ; 
} 
filenameDisplay  =  file  .  getName  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 

public   void   loadFile  (  )  { 
midiImport  .  importMidi  (  file  )  ; 
} 

public   void   loadMenu  (  )  { 
melodies  =  midiImport  .  getMelodies  (  )  ; 
if  (  melodies  !=  null  )  { 
setResolution  (  )  ; 
trackListModel  .  clear  (  )  ; 
int   channelNumber  =  0  ; 
for  (  final   MidiImportRecord   record  :  melodies  )  { 
if  (  record  .  getChannel  (  )  >  channelNumber  )  { 
trackListModel  .  addElement  (  "-------------------------------------"  )  ; 
channelNumber  =  record  .  getChannel  (  )  ; 
} 
trackListModel  .  addElement  (  record  )  ; 
} 
selectTrack  (  0  )  ; 
} 
} 

private   void   reloadMenu  (  )  { 
setResolution  (  )  ; 
int   saveIndex  =  importedTrackList  .  getSelectedIndex  (  )  ; 
midiImport  .  scoreToMelodies  (  )  ; 
loadMenu  (  )  ; 
selectTrack  (  saveIndex  )  ; 
} 

private   void   initFileChooser  (  )  { 
LeadsheetFileView   fileView  =  new   LeadsheetFileView  (  )  ; 
midiFileChooser  .  setCurrentDirectory  (  ImproVisor  .  getUserDirectory  (  )  )  ; 
midiFileChooser  .  setDialogType  (  JFileChooser  .  OPEN_DIALOG  )  ; 
midiFileChooser  .  setDialogTitle  (  "Open MIDI file"  )  ; 
midiFileChooser  .  setFileSelectionMode  (  JFileChooser  .  FILES_ONLY  )  ; 
midiFileChooser  .  resetChoosableFileFilters  (  )  ; 
midiFileChooser  .  addChoosableFileFilter  (  new   MidiFilter  (  )  )  ; 
midiFileChooser  .  setFileView  (  fileView  )  ; 
} 

public   String   getFilenameDisplay  (  )  { 
return   filenameDisplay  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
midiImportTopPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
selectTracksLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
trackSelectScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
importedTrackList  =  new   javax  .  swing  .  JList  (  )  ; 
midiImportButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
playMIDIfile  =  new   javax  .  swing  .  JButton  (  )  ; 
playMIDIimportTrack  =  new   javax  .  swing  .  JButton  (  )  ; 
stopPlayingTrackButton  =  new   javax  .  swing  .  JButton  (  )  ; 
importTrackToLeadsheet  =  new   javax  .  swing  .  JButton  (  )  ; 
volumeSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
startBeatSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
endBeatSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
meterSpinner  =  new   javax  .  swing  .  JSpinner  (  )  ; 
noteResolutionComboBox  =  new   javax  .  swing  .  JComboBox  (  )  ; 
pickupResolutionComboBox  =  new   javax  .  swing  .  JComboBox  (  )  ; 
jMenuBar1  =  new   javax  .  swing  .  JMenuBar  (  )  ; 
MIDIimportFileMenu  =  new   javax  .  swing  .  JMenu  (  )  ; 
openAnotherFileMI  =  new   javax  .  swing  .  JMenuItem  (  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
setBackground  (  new   java  .  awt  .  Color  (  204  ,  204  ,  204  )  )  ; 
getContentPane  (  )  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
midiImportTopPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
selectTracksLabel  .  setBackground  (  new   java  .  awt  .  Color  (  153  ,  255  ,  0  )  )  ; 
selectTracksLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Lucida Grande"  ,  1  ,  14  )  )  ; 
selectTracksLabel  .  setHorizontalAlignment  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
selectTracksLabel  .  setText  (  "Please select the tracks to be imported one at a time. Each track will be put in a separate chorus in the leadsheet."  )  ; 
selectTracksLabel  .  setHorizontalTextPosition  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
selectTracksLabel  .  setOpaque  (  true  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  weighty  =  1.0  ; 
midiImportTopPanel  .  add  (  selectTracksLabel  ,  gridBagConstraints  )  ; 
getContentPane  (  )  .  add  (  midiImportTopPanel  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
trackSelectScrollPane  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  400  ,  100  )  )  ; 
trackSelectScrollPane  .  setOpaque  (  false  )  ; 
trackSelectScrollPane  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  600  ,  300  )  )  ; 
importedTrackList  .  setFont  (  new   java  .  awt  .  Font  (  "Lucida Grande"  ,  0  ,  14  )  )  ; 
importedTrackList  .  setModel  (  trackListModel  )  ; 
importedTrackList  .  setSelectionMode  (  javax  .  swing  .  ListSelectionModel  .  SINGLE_SELECTION  )  ; 
importedTrackList  .  setToolTipText  (  "These are all channels and tracks from the imported MIDI file. Select the one you wish to play or import to the leadsheet. Doube clicking imports the track, or use the button below."  )  ; 
importedTrackList  .  addMouseListener  (  new   java  .  awt  .  event  .  MouseAdapter  (  )  { 

public   void   mouseReleased  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
importTrackSelected  (  evt  )  ; 
} 

public   void   mouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
importedTrackListMouseClicked  (  evt  )  ; 
} 
}  )  ; 
trackSelectScrollPane  .  setViewportView  (  importedTrackList  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  weighty  =  0.8  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
getContentPane  (  )  .  add  (  trackSelectScrollPane  ,  gridBagConstraints  )  ; 
midiImportButtonPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
playMIDIfile  .  setText  (  "Play MIDI File"  )  ; 
playMIDIfile  .  setToolTipText  (  "CAUTION: This will play the complete MIDI file at full volume. Consider reducing your computer audio volume first."  )  ; 
playMIDIfile  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
playMIDIfileActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
midiImportButtonPanel  .  add  (  playMIDIfile  ,  gridBagConstraints  )  ; 
playMIDIimportTrack  .  setText  (  "Play Track"  )  ; 
playMIDIimportTrack  .  setToolTipText  (  "Plays the selected MIDI track individually, not in the leadsheet context."  )  ; 
playMIDIimportTrack  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
playMIDIimportTrackActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  0.5  ; 
midiImportButtonPanel  .  add  (  playMIDIimportTrack  ,  gridBagConstraints  )  ; 
stopPlayingTrackButton  .  setText  (  "Stop"  )  ; 
stopPlayingTrackButton  .  setToolTipText  (  "Stop playback of either full MIDI file or selected track, whichever is playing."  )  ; 
stopPlayingTrackButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
stopPlayingTrackButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  2  ; 
midiImportButtonPanel  .  add  (  stopPlayingTrackButton  ,  gridBagConstraints  )  ; 
importTrackToLeadsheet  .  setToolTipText  (  "Transfers the track selected  to the leadsheet as a new chorus. Alternatively, double click the entry. If Start Beat, +/-,  and End Beat are set, will import just the selected range of beats.\n"  )  ; 
importTrackToLeadsheet  .  setLabel  (  "Transfer Track (or double click)"  )  ; 
importTrackToLeadsheet  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
importTrackToLeadsheetActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  6  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  0.5  ; 
midiImportButtonPanel  .  add  (  importTrackToLeadsheet  ,  gridBagConstraints  )  ; 
volumeSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  70  ,  0  ,  127  ,  5  )  )  ; 
volumeSpinner  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Volume"  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_JUSTIFICATION  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Lucida Grande"  ,  0  ,  11  )  )  )  ; 
volumeSpinner  .  addChangeListener  (  new   javax  .  swing  .  event  .  ChangeListener  (  )  { 

public   void   stateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
volumeSpinnerChanged  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
midiImportButtonPanel  .  add  (  volumeSpinner  ,  gridBagConstraints  )  ; 
startBeatSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  Integer  .  valueOf  (  1  )  ,  Integer  .  valueOf  (  1  )  ,  null  ,  Integer  .  valueOf  (  1  )  )  )  ; 
startBeatSpinner  .  setToolTipText  (  "Sets the starting beat for playback or importing to the leadsheet."  )  ; 
startBeatSpinner  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  "Start Beat"  )  )  ; 
startBeatSpinner  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
startBeatSpinner  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  4  ; 
midiImportButtonPanel  .  add  (  startBeatSpinner  ,  gridBagConstraints  )  ; 
endBeatSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  Integer  .  valueOf  (  1  )  ,  Integer  .  valueOf  (  1  )  ,  null  ,  Integer  .  valueOf  (  1  )  )  )  ; 
endBeatSpinner  .  setToolTipText  (  "Sets the ending beat for playback or importing to the leadsheet."  )  ; 
endBeatSpinner  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "End Beat"  ,  javax  .  swing  .  border  .  TitledBorder  .  CENTER  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  )  )  ; 
endBeatSpinner  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
endBeatSpinner  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  5  ; 
midiImportButtonPanel  .  add  (  endBeatSpinner  ,  gridBagConstraints  )  ; 
meterSpinner  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  4  ,  1  ,  16  ,  1  )  )  ; 
meterSpinner  .  setToolTipText  (  ""  )  ; 
meterSpinner  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Meter"  ,  javax  .  swing  .  border  .  TitledBorder  .  CENTER  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  )  )  ; 
meterSpinner  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
meterSpinner  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  75  ,  56  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  7  ; 
midiImportButtonPanel  .  add  (  meterSpinner  ,  gridBagConstraints  )  ; 
noteResolutionComboBox  .  setMaximumRowCount  (  16  )  ; 
noteResolutionComboBox  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  NoteResolutionInfo  .  getNoteResolutions  (  )  )  )  ; 
noteResolutionComboBox  .  setSelectedItem  (  NoteResolutionInfo  .  getNoteResolutions  (  )  [  3  ]  )  ; 
noteResolutionComboBox  .  setToolTipText  (  "Sets the resolution with which MIDI tracks are converted to Impro-Visor notes. Select the highest number of slots that gives satisfactory results. Low numbers take more memory and may fail."  )  ; 
noteResolutionComboBox  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Note Resolution"  ,  javax  .  swing  .  border  .  TitledBorder  .  CENTER  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Lucida Grande"  ,  1  ,  13  )  )  )  ; 
noteResolutionComboBox  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  300  ,  50  )  )  ; 
noteResolutionComboBox  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  300  ,  50  )  )  ; 
noteResolutionComboBox  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
importMidiNoteResolutionChanged  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  10  ; 
gridBagConstraints  .  gridy  =  0  ; 
midiImportButtonPanel  .  add  (  noteResolutionComboBox  ,  gridBagConstraints  )  ; 
pickupResolutionComboBox  .  setMaximumRowCount  (  16  )  ; 
pickupResolutionComboBox  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  StartRoundingFactor  .  getFactors  (  )  )  )  ; 
pickupResolutionComboBox  .  setSelectedItem  (  StartRoundingFactor  .  getFactors  (  )  [  3  ]  )  ; 
pickupResolutionComboBox  .  setToolTipText  (  "Determines how to round off any pickup notes."  )  ; 
pickupResolutionComboBox  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Pickup Resolution"  ,  javax  .  swing  .  border  .  TitledBorder  .  CENTER  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Lucida Grande"  ,  1  ,  13  )  )  )  ; 
pickupResolutionComboBox  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  300  ,  50  )  )  ; 
pickupResolutionComboBox  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  300  ,  50  )  )  ; 
pickupResolutionComboBox  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
pickupResolutionComboBoximportMidiNoteResolutionChanged  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  11  ; 
gridBagConstraints  .  gridy  =  0  ; 
midiImportButtonPanel  .  add  (  pickupResolutionComboBox  ,  gridBagConstraints  )  ; 
pickupResolutionComboBox  .  getAccessibleContext  (  )  .  setAccessibleName  (  "Pickup Resolution"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  weighty  =  0.1  ; 
getContentPane  (  )  .  add  (  midiImportButtonPanel  ,  gridBagConstraints  )  ; 
MIDIimportFileMenu  .  setText  (  "File"  )  ; 
openAnotherFileMI  .  setText  (  "Open Another MIDI File"  )  ; 
openAnotherFileMI  .  setToolTipText  (  "Opens a MIDI File, usually a different one from the current."  )  ; 
openAnotherFileMI  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
openAnotherFileMIActionPerformed  (  evt  )  ; 
} 
}  )  ; 
MIDIimportFileMenu  .  add  (  openAnotherFileMI  )  ; 
jMenuBar1  .  add  (  MIDIimportFileMenu  )  ; 
setJMenuBar  (  jMenuBar1  )  ; 
pack  (  )  ; 
} 

private   void   importTrackSelected  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
setSelectedTrack  (  )  ; 
} 

private   void   playMIDIimportTrackActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
playSelectedTrack  (  )  ; 
} 

private   void   importTrackToLeadsheetActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
importSelectedTrack  (  )  ; 
} 

private   void   importMidiNoteResolutionChanged  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
reloadMenu  (  )  ; 
} 

private   void   importedTrackListMouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
setSelectedTrack  (  )  ; 
if  (  evt  .  getClickCount  (  )  >  1  )  { 
importSelectedTrack  (  )  ; 
} 
} 

private   void   stopPlayingTrackButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
stopPlaying  (  )  ; 
} 

private   void   playMIDIfileActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
try  { 
jmScore  =  midiImport  .  getScore  (  )  ; 
setJmVolume  (  )  ; 
if  (  jmSynth  !=  null  )  { 
jmSynth  .  stop  (  )  ; 
} 
jmSynth  =  new   jm  .  midi  .  MidiSynth  (  )  ; 
jmSynth  .  play  (  jmScore  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
} 
} 

private   void   volumeSpinnerChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
setJmVolume  (  )  ; 
} 

private   void   pickupResolutionComboBoximportMidiNoteResolutionChanged  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
reloadMenu  (  )  ; 
} 

private   void   openAnotherFileMIActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
loadFileAndMenu  (  )  ; 
} 

private   void   setJmVolume  (  )  { 
int   value  =  (  Integer  )  volumeSpinner  .  getValue  (  )  ; 
if  (  jmScore  !=  null  )  { 
jmScore  .  setVolume  (  value  )  ; 
} 
} 

private   void   setResolution  (  )  { 
int   newResolution  =  (  (  NoteResolutionInfo  )  noteResolutionComboBox  .  getSelectedItem  (  )  )  .  getSlots  (  )  ; 
midiImport  .  setResolution  (  newResolution  )  ; 
int   newRoundingFactor  =  (  (  StartRoundingFactor  )  pickupResolutionComboBox  .  getSelectedItem  (  )  )  .  getFactor  (  )  ; 
midiImport  .  setStartFactor  (  newRoundingFactor  )  ; 
} 





private   void   setSelectedTrack  (  )  { 
int   index  =  importedTrackList  .  getSelectedIndex  (  )  ; 
selectTrack  (  index  )  ; 
} 

private   void   selectTrack  (  int   index  )  { 
if  (  index  <  0  ||  index  >=  trackListModel  .  size  (  )  )  { 
return  ; 
} 
Object   ob  =  trackListModel  .  get  (  index  )  ; 
if  (  ob   instanceof   MidiImportRecord  )  { 
importedTrackList  .  setSelectedIndex  (  index  )  ; 
MidiImportRecord   record  =  (  MidiImportRecord  )  ob  ; 
selectedPart  =  record  .  getPart  (  )  ; 
int   initialRestSlots  =  record  .  getInitialRestSlots  (  )  ; 
int   beatsPerMeasure  =  (  Integer  )  meterSpinner  .  getValue  (  )  ; 
int   initialRestBeats  =  initialRestSlots  /  BEAT  ; 
int   initialRestMeasures  =  initialRestBeats  /  beatsPerMeasure  ; 
int   initialIntegralBeats  =  beatsPerMeasure  *  initialRestMeasures  ; 
int   startBeat  =  1  +  initialIntegralBeats  ; 
startBeatSpinner  .  setValue  (  startBeat  )  ; 
int   numBeats  =  record  .  getBeats  (  )  ; 
endBeatSpinner  .  setValue  (  numBeats  )  ; 
(  (  javax  .  swing  .  SpinnerNumberModel  )  endBeatSpinner  .  getModel  (  )  )  .  setMaximum  (  numBeats  )  ; 
} 
} 

private   MelodyPart   getSelectedTrackMelody  (  )  { 
if  (  selectedPart  !=  null  )  { 
int   startSlot  =  BEAT  *  (  (  Integer  )  startBeatSpinner  .  getValue  (  )  -  1  )  ; 
int   endSlot  =  Math  .  min  (  selectedPart  .  getSize  (  )  -  1  ,  BEAT  *  (  (  Integer  )  endBeatSpinner  .  getValue  (  )  )  )  ; 
return   selectedPart  .  copy  (  startSlot  ,  endSlot  )  ; 
} 
return   null  ; 
} 

private   void   importSelectedTrack  (  )  { 
MelodyPart   part  =  getSelectedTrackMelody  (  )  ; 
if  (  part  !=  null  )  { 
notate  .  addChorus  (  part  )  ; 
notate  .  requestFocusInWindow  (  )  ; 
} 
} 

private   void   playSelectedTrack  (  )  { 
MelodyPart   part  =  getSelectedTrackMelody  (  )  ; 
if  (  part  !=  null  )  { 
stopPlaying  (  )  ; 
Score   score  =  new   Score  (  )  ; 
score  .  addPart  (  part  )  ; 
notate  .  playAscore  (  score  )  ; 
} 
} 

private   void   stopPlaying  (  )  { 
notate  .  stopPlayAscore  (  )  ; 
if  (  jmSynth  !=  null  )  { 
jmSynth  .  stop  (  )  ; 
} 
} 

@  Override 
public   void   dispose  (  )  { 
WindowRegistry  .  unregisterWindow  (  this  )  ; 
super  .  dispose  (  )  ; 
} 

private   javax  .  swing  .  JMenu   MIDIimportFileMenu  ; 

private   javax  .  swing  .  JSpinner   endBeatSpinner  ; 

private   javax  .  swing  .  JButton   importTrackToLeadsheet  ; 

private   javax  .  swing  .  JList   importedTrackList  ; 

private   javax  .  swing  .  JMenuBar   jMenuBar1  ; 

private   javax  .  swing  .  JSpinner   meterSpinner  ; 

private   javax  .  swing  .  JPanel   midiImportButtonPanel  ; 

private   javax  .  swing  .  JPanel   midiImportTopPanel  ; 

private   javax  .  swing  .  JComboBox   noteResolutionComboBox  ; 

private   javax  .  swing  .  JMenuItem   openAnotherFileMI  ; 

private   javax  .  swing  .  JComboBox   pickupResolutionComboBox  ; 

private   javax  .  swing  .  JButton   playMIDIfile  ; 

private   javax  .  swing  .  JButton   playMIDIimportTrack  ; 

private   javax  .  swing  .  JLabel   selectTracksLabel  ; 

private   javax  .  swing  .  JSpinner   startBeatSpinner  ; 

private   javax  .  swing  .  JButton   stopPlayingTrackButton  ; 

private   javax  .  swing  .  JScrollPane   trackSelectScrollPane  ; 

private   javax  .  swing  .  JSpinner   volumeSpinner  ; 
} 

