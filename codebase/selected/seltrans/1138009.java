package   jAudioFeatureExtractor  .  jAudioTools  ; 

import   java  .  nio  .  *  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Random  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 


















public   class   AudioMethodsSynthesis  { 

private   static   final   int   SINE_WAVE  =  1  ; 

private   static   final   int   BASIC_TONE  =  2  ; 

private   static   final   int   STEREO_PANNING  =  3  ; 

private   static   final   int   STEREO_PINPONG  =  4  ; 

private   static   final   int   FM_SWEEP  =  5  ; 

private   static   final   int   DECAY_PULSE  =  6  ; 

private   static   final   int   WHITE_NOISE  =  7  ; 

































































public   static   double  [  ]  [  ]  synthesizeAndWriteToBuffer  (  byte  [  ]  buffer  ,  double   duration  ,  AudioFormat   audio_format  ,  int   synthesis_type  ,  double   gain  ,  double   panning  ,  double   fundamental_frequency  ,  double   max_frac_samp_rate  ,  double   click_avoid_env_length  )  throws   Exception  { 
if  (  audio_format  ==  null  )  throw   new   Exception  (  "Null audio format provided."  )  ; 
if  (  (  audio_format  .  getSampleSizeInBits  (  )  !=  16  &&  audio_format  .  getSampleSizeInBits  (  )  !=  8  )  ||  !  audio_format  .  isBigEndian  (  )  ||  audio_format  .  getEncoding  (  )  !=  AudioFormat  .  Encoding  .  PCM_SIGNED  )  throw   new   Exception  (  "Only 8 or 16 bit signed PCM samples with a big-endian\n"  +  "byte order can be generated currently."  )  ; 
int   number_of_channels  =  audio_format  .  getChannels  (  )  ; 
float   sample_rate  =  audio_format  .  getSampleRate  (  )  ; 
int   bit_depth  =  audio_format  .  getSampleSizeInBits  (  )  ; 
int   total_number_of_samples_per_channel  =  0  ; 
if  (  buffer  !=  null  )  { 
int   bytes_per_sample  =  bit_depth  /  8  ; 
int   total_number_of_bytes  =  buffer  .  length  ; 
int   total_number_of_samples  =  total_number_of_bytes  /  bytes_per_sample  ; 
total_number_of_samples_per_channel  =  total_number_of_samples  /  number_of_channels  ; 
}  else   total_number_of_samples_per_channel  =  (  int  )  (  sample_rate  *  duration  )  ; 
double  [  ]  [  ]  sample_values  =  null  ; 
if  (  synthesis_type  ==  SINE_WAVE  )  { 
sample_values  =  generateSamplesSineWave  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  BASIC_TONE  )  { 
sample_values  =  generateSamplesBasicTone  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  STEREO_PANNING  )  { 
sample_values  =  generateSamplesStereoPanning  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  STEREO_PINPONG  )  { 
sample_values  =  generateSamplesStereoPingpong  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  FM_SWEEP  )  { 
sample_values  =  generateSamplesFMSweep  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  DECAY_PULSE  )  { 
sample_values  =  generateSamplesDecayPulse  (  fundamental_frequency  ,  number_of_channels  ,  sample_rate  ,  max_frac_samp_rate  ,  total_number_of_samples_per_channel  )  ; 
}  else   if  (  synthesis_type  ==  WHITE_NOISE  )  { 
sample_values  =  generateWhiteNoise  (  number_of_channels  ,  total_number_of_samples_per_channel  )  ; 
}  else   throw   new   Exception  (  "Invalid synthesis type specified."  )  ; 
applyGainAndPanning  (  sample_values  ,  gain  ,  panning  )  ; 
applyClickAvoidanceAttenuationEnvelope  (  sample_values  ,  click_avoid_env_length  ,  sample_rate  )  ; 
int   samples_per_channel  =  sample_values  [  0  ]  .  length  ; 
for  (  int   chan  =  0  ;  chan  <  sample_values  .  length  ;  chan  ++  )  if  (  sample_values  [  chan  ]  .  length  !=  samples_per_channel  )  throw   new   Exception  (  "Channels do not have equal number of samples."  )  ; 
if  (  buffer  !=  null  )  { 
writeSamplesToBuffer  (  sample_values  ,  bit_depth  ,  buffer  )  ; 
return   null  ; 
}  else   return   sample_values  ; 
} 


































public   static   void   applyGainAndPanning  (  double  [  ]  [  ]  samples_to_modify  ,  double   gain  ,  double   panning  )  throws   Exception  { 
if  (  gain  <  0.0  ||  gain  >  1.0  )  throw   new   Exception  (  "Gain of "  +  gain  +  " specified.\n"  +  "This value must be between 0.0 and 1.0."  )  ; 
if  (  panning  <  -  1.0  ||  panning  >  1.0  )  throw   new   Exception  (  "Panning of "  +  panning  +  " specified.\n"  +  "This value must be between -1.0 and 1.0."  )  ; 
if  (  samples_to_modify  ==  null  )  throw   new   Exception  (  "Empty set of samples provided."  )  ; 
for  (  int   chan  =  0  ;  chan  <  samples_to_modify  .  length  ;  chan  ++  )  if  (  samples_to_modify  [  chan  ]  ==  null  )  throw   new   Exception  (  "Channel "  +  chan  +  " is empty."  )  ; 
for  (  int   chan  =  0  ;  chan  <  samples_to_modify  .  length  ;  chan  ++  )  for  (  int   samp  =  0  ;  samp  <  samples_to_modify  [  chan  ]  .  length  ;  samp  ++  )  samples_to_modify  [  chan  ]  [  samp  ]  *=  gain  ; 
if  (  samples_to_modify  .  length  ==  2  &&  panning  !=  0.0  )  { 
if  (  panning  >  0.0  )  { 
double   left_multiplier  =  1.0  -  panning  ; 
for  (  int   samp  =  0  ;  samp  <  samples_to_modify  [  0  ]  .  length  ;  samp  ++  )  samples_to_modify  [  0  ]  [  samp  ]  *=  left_multiplier  ; 
} 
if  (  panning  <  0.0  )  { 
double   right_multiplier  =  panning  +  1.0  ; 
for  (  int   samp  =  0  ;  samp  <  samples_to_modify  [  1  ]  .  length  ;  samp  ++  )  samples_to_modify  [  1  ]  [  samp  ]  *=  right_multiplier  ; 
} 
} 
} 



















public   static   void   applyClickAvoidanceAttenuationEnvelope  (  double  [  ]  [  ]  sample_values  ,  double   click_avoid_env_length  ,  float   sample_rate  )  throws   Exception  { 
if  (  sample_values  ==  null  )  throw   new   Exception  (  "Empty set of samples provided."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Given sample rate is "  +  sample_rate  +  " Hz.\n"  +  "This value should be greater than zero."  )  ; 
if  (  click_avoid_env_length  <  0.0  )  throw   new   Exception  (  "Click avoidance envelope length is "  +  click_avoid_env_length  +  " seconds.\n"  +  "This value should be 0.0 seconds or higher."  )  ; 
double   duration_of_audio  =  sample_values  [  0  ]  .  length  /  sample_rate  ; 
if  (  (  2.0  *  click_avoid_env_length  )  >=  duration_of_audio  )  throw   new   Exception  (  "Click avoidance envelope length is "  +  click_avoid_env_length  +  " seconds.\n"  +  "This would lead to combined envelope lengths longer than the provided audio."  )  ; 
int   sample_duration  =  (  int  )  (  click_avoid_env_length  *  sample_rate  )  ; 
int   start_sample_1  =  0  ; 
int   end_sample_1  =  sample_duration  -  1  ; 
int   start_sample_2  =  sample_values  [  0  ]  .  length  -  1  -  sample_duration  ; 
int   end_sample_2  =  sample_values  [  0  ]  .  length  -  1  ; 
for  (  int   samp  =  start_sample_1  ;  samp  <=  end_sample_1  ;  samp  ++  )  { 
double   amplitude_multipler  =  (  double  )  samp  /  (  double  )  end_sample_1  ; 
for  (  int   chan  =  0  ;  chan  <  sample_values  .  length  ;  chan  ++  )  sample_values  [  chan  ]  [  samp  ]  *=  amplitude_multipler  ; 
} 
for  (  int   samp  =  start_sample_2  ;  samp  <=  end_sample_2  ;  samp  ++  )  { 
double   amplitude_multipler  =  1.0  -  (  (  double  )  (  samp  -  start_sample_2  )  /  (  double  )  (  end_sample_2  -  start_sample_2  )  )  ; 
for  (  int   chan  =  0  ;  chan  <  sample_values  .  length  ;  chan  ++  )  sample_values  [  chan  ]  [  samp  ]  *=  amplitude_multipler  ; 
} 
} 

















public   static   void   writeSamplesToBuffer  (  double  [  ]  [  ]  sample_values  ,  int   bit_depth  ,  byte  [  ]  buffer  )  throws   Exception  { 
if  (  sample_values  ==  null  )  throw   new   Exception  (  "Empty set of samples to write provided."  )  ; 
if  (  bit_depth  !=  8  &&  bit_depth  !=  16  )  throw   new   Exception  (  "Bit depth of "  +  bit_depth  +  " specified."  +  "Only bit depths of 8 or 16 currently accepted."  )  ; 
if  (  buffer  ==  null  )  throw   new   Exception  (  "Null buffer for storing samples provided."  )  ; 
double   max_sample_value  =  AudioMethods  .  findMaximumSampleValue  (  bit_depth  )  ; 
ByteBuffer   byte_buffer  =  ByteBuffer  .  wrap  (  buffer  )  ; 
if  (  bit_depth  ==  8  )  { 
for  (  int   samp  =  0  ;  samp  <  sample_values  [  0  ]  .  length  ;  samp  ++  )  for  (  int   chan  =  0  ;  chan  <  sample_values  .  length  ;  chan  ++  )  { 
double   sample_value  =  sample_values  [  chan  ]  [  samp  ]  *  max_sample_value  ; 
byte_buffer  .  put  (  (  byte  )  sample_value  )  ; 
} 
}  else   if  (  bit_depth  ==  16  )  { 
ShortBuffer   short_buffer  =  byte_buffer  .  asShortBuffer  (  )  ; 
for  (  int   samp  =  0  ;  samp  <  sample_values  [  0  ]  .  length  ;  samp  ++  )  for  (  int   chan  =  0  ;  chan  <  sample_values  .  length  ;  chan  ++  )  { 
double   sample_value  =  sample_values  [  chan  ]  [  samp  ]  *  max_sample_value  ; 
short_buffer  .  put  (  (  short  )  sample_value  )  ; 
} 
} 
} 











public   static   int   getSynthesisTypeCode  (  String   synthesis_type_name  )  throws   Exception  { 
if  (  synthesis_type_name  .  equals  (  "Sine Wave"  )  )  return   SINE_WAVE  ;  else   if  (  synthesis_type_name  .  equals  (  "Basic Tone"  )  )  return   BASIC_TONE  ;  else   if  (  synthesis_type_name  .  equals  (  "Stereo Panning"  )  )  return   STEREO_PANNING  ;  else   if  (  synthesis_type_name  .  equals  (  "Stereo Pingpong"  )  )  return   STEREO_PINPONG  ;  else   if  (  synthesis_type_name  .  equals  (  "FM Sweep"  )  )  return   FM_SWEEP  ;  else   if  (  synthesis_type_name  .  equals  (  "Decay Pulse"  )  )  return   DECAY_PULSE  ;  else   if  (  synthesis_type_name  .  equals  (  "White Noise"  )  )  return   WHITE_NOISE  ;  else   throw   new   Exception  (  "Unknown type of synthesis specified: "  +  synthesis_type_name  +  ".\n"  +  "Known types of synthesis are:\n"  +  "   Sine Wave, Basic Tone, Stereo Panning, Stereo Pingpong\n"  +  "   FM Sweep, White Noise and Decay Pulse."  )  ; 
} 







public   static   String  [  ]  getSynthesisNames  (  )  { 
String  [  ]  names  =  {  "Sine Wave"  ,  "Basic Tone"  ,  "Stereo Panning"  ,  "Stereo Pingpong"  ,  "FM Sweep"  ,  "Decay Pulse"  ,  "White Noise"  }  ; 
return   names  ; 
} 




































public   static   double  [  ]  [  ]  generateSamplesSineWave  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  <  1  )  throw   new   Exception  (  "There must be 1 or more channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
double   sample_value  =  (  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  time  )  )  ; 
for  (  int   chan  =  0  ;  chan  <  samples  .  length  ;  chan  ++  )  samples  [  chan  ]  [  samp  ]  =  sample_value  ; 
} 
return   samples  ; 
} 






































public   static   double  [  ]  [  ]  generateSamplesBasicTone  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  /  1.8  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  /  1.8  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  <  1  )  throw   new   Exception  (  "There must be 1 or more channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
double   sample_value  =  (  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  time  )  +  Math  .  sin  (  2  *  Math  .  PI  *  (  1.2  )  *  fund_freq  *  time  )  +  Math  .  sin  (  2  *  Math  .  PI  *  (  1.8  )  *  fund_freq  *  time  )  )  ; 
sample_value  =  sample_value  /  3.0  ; 
for  (  int   chan  =  0  ;  chan  <  samples  .  length  ;  chan  ++  )  samples  [  chan  ]  [  samp  ]  =  sample_value  ; 
} 
return   samples  ; 
} 





































public   static   double  [  ]  [  ]  generateSamplesStereoPanning  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  /  1.8  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  !=  2  )  throw   new   Exception  (  "There must be 2 channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
double   right_gain  =  (  double  )  samp  /  (  double  )  total_samples_per_chan  ; 
double   left_gain  =  1.0  -  right_gain  ; 
double   original_left_sample_value  =  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  time  )  ; 
double   original_right_sample_value  =  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  /  2  *  time  )  ; 
samples  [  0  ]  [  samp  ]  =  left_gain  *  original_left_sample_value  ; 
samples  [  1  ]  [  samp  ]  =  right_gain  *  original_right_sample_value  ; 
} 
return   samples  ; 
} 





































public   static   double  [  ]  [  ]  generateSamplesStereoPingpong  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  /  1.8  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  !=  2  )  throw   new   Exception  (  "There must be 2 channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
double   number_of_times_a_sec_switches_occur  =  4.0  ; 
double   switch_time_interval  =  1  /  number_of_times_a_sec_switches_occur  ; 
double   time_of_last_switch  =  0.0  ; 
double   right_gain  =  0.0  ; 
double   left_gain  =  1.0  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
if  (  time  -  time_of_last_switch  >  switch_time_interval  )  { 
double   temp  =  left_gain  ; 
left_gain  =  right_gain  ; 
right_gain  =  temp  ; 
time_of_last_switch  =  time  ; 
} 
double   original_left_sample_value  =  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  time  )  ; 
double   original_right_sample_value  =  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  0.8  *  time  )  ; 
samples  [  0  ]  [  samp  ]  =  left_gain  *  original_left_sample_value  ; 
samples  [  1  ]  [  samp  ]  =  right_gain  *  original_right_sample_value  ; 
} 
return   samples  ; 
} 







































public   static   double  [  ]  [  ]  generateSamplesFMSweep  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  <  1  )  throw   new   Exception  (  "There must be 1 or more channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
double   high_freq  =  fund_freq  ; 
double   low_freq  =  high_freq  /  10.0  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
double   fraction_done  =  (  double  )  samp  /  (  double  )  total_samples_per_chan  ; 
double   freq  =  low_freq  +  (  high_freq  -  low_freq  )  *  fraction_done  ; 
double   sample_value  =  (  Math  .  sin  (  2  *  Math  .  PI  *  freq  *  time  )  )  ; 
for  (  int   chan  =  0  ;  chan  <  samples  .  length  ;  chan  ++  )  samples  [  chan  ]  [  samp  ]  =  sample_value  ; 
} 
return   samples  ; 
} 





































public   static   double  [  ]  [  ]  generateSamplesDecayPulse  (  double   fund_freq  ,  int   number_of_channels  ,  float   sample_rate  ,  double   max_frac_samp_rate  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  max_frac_samp_rate  <=  0.0  )  throw   new   Exception  (  "Invalid maximum allowable fraction of sampling rate of "  +  max_frac_samp_rate  +  " specified.\n"  +  "This value must be above 0."  )  ; 
if  (  fund_freq  <=  0.0  )  throw   new   Exception  (  "Invalid fundamental frequence of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be above 0 Hz."  )  ; 
if  (  fund_freq  >=  (  max_frac_samp_rate  *  sample_rate  )  )  throw   new   Exception  (  "Invalid fundamental frequency of "  +  fund_freq  +  " Hz specified.\n"  +  "Frequency must be below "  +  (  max_frac_samp_rate  *  sample_rate  )  +  " Hz\n"  +  "under current settings. This is done in order to avoid aliasing at this\n"  +  "sampling rate of "  +  sample_rate  +  " Hz for this type of synthesis."  )  ; 
if  (  number_of_channels  <  1  )  throw   new   Exception  (  "There must be 1 or more channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  sample_rate  <=  0.0F  )  throw   new   Exception  (  "Invalid sampling rate of "  +  sample_rate  +  " Hz specified.\n"  +  "Must be greater than 0."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  { 
double   time  =  samp  /  sample_rate  ; 
double   fraction_done  =  (  double  )  samp  /  (  double  )  total_samples_per_chan  ; 
double   amplitude_coef  =  1.0  -  fraction_done  ; 
double   sample_value  =  amplitude_coef  *  (  Math  .  sin  (  2  *  Math  .  PI  *  fund_freq  *  time  )  )  ; 
for  (  int   chan  =  0  ;  chan  <  samples  .  length  ;  chan  ++  )  samples  [  chan  ]  [  samp  ]  =  sample_value  ; 
} 
return   samples  ; 
} 






















public   static   double  [  ]  [  ]  generateWhiteNoise  (  int   number_of_channels  ,  int   total_samples_per_chan  )  throws   Exception  { 
if  (  number_of_channels  <  1  )  throw   new   Exception  (  "There must be 1 or more channels. You specified "  +  number_of_channels  +  "."  )  ; 
if  (  total_samples_per_chan  <=  0  )  throw   new   Exception  (  "Invalid total number of samples per channel of "  +  total_samples_per_chan  +  " specified.\n"  +  "Must be greater than 0."  )  ; 
double  [  ]  [  ]  samples  =  new   double  [  number_of_channels  ]  [  total_samples_per_chan  ]  ; 
Random   generator  =  new   Random  (  new   Date  (  )  .  getTime  (  )  )  ; 
for  (  int   samp  =  0  ;  samp  <  total_samples_per_chan  ;  samp  ++  )  for  (  int   chan  =  0  ;  chan  <  number_of_channels  ;  chan  ++  )  samples  [  chan  ]  [  samp  ]  =  (  2.0  *  generator  .  nextDouble  (  )  )  -  1.0  ; 
return   samples  ; 
} 
} 

