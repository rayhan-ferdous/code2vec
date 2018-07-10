package   com  .  cp  .  vaultclipse  ; 

import   java  .  io  .  File  ; 
import   org  .  eclipse  .  core  .  runtime  .  CoreException  ; 
import   org  .  eclipse  .  core  .  runtime  .  IPluginDescriptor  ; 
import   org  .  eclipse  .  jface  .  preference  .  IPreferenceStore  ; 
import   org  .  eclipse  .  ui  .  console  .  ConsolePlugin  ; 
import   org  .  eclipse  .  ui  .  console  .  IConsole  ; 
import   org  .  eclipse  .  ui  .  console  .  IConsoleManager  ; 
import   org  .  eclipse  .  ui  .  console  .  MessageConsole  ; 
import   org  .  eclipse  .  ui  .  plugin  .  AbstractUIPlugin  ; 
import   com  .  cp  .  vaultclipse  .  i18n  .  Localization  ; 
import   com  .  cp  .  vaultclipse  .  preferences  .  PreferencesMessages  ; 
import   com  .  cp  .  vaultclipse  .  svc  .  VaultSvc  ; 








@  SuppressWarnings  (  "deprecation"  ) 
public   class   VaultClipsePlugin   extends   AbstractUIPlugin  { 

private   static   VaultClipsePlugin   plugin  ; 






public   static   VaultClipsePlugin   getDefault  (  )  { 
return   plugin  ; 
} 

public   VaultClipsePlugin  (  IPluginDescriptor   descriptor  )  { 
super  (  descriptor  )  ; 
plugin  =  this  ; 
} 







protected   void   delete  (  File   file  )  { 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  children  =  file  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  length  ;  i  ++  )  { 
delete  (  children  [  i  ]  )  ; 
} 
} 
if  (  !  file  .  delete  (  )  )  { 
file  .  delete  (  )  ; 
} 
} 






public   MessageConsole   getConsole  (  )  { 
Localization   local  =  Localization  .  get  (  VaultClipsePlugin  .  class  )  ; 
ConsolePlugin   plugin  =  ConsolePlugin  .  getDefault  (  )  ; 
IConsoleManager   conMan  =  plugin  .  getConsoleManager  (  )  ; 
IConsole  [  ]  existing  =  conMan  .  getConsoles  (  )  ; 
for  (  int   i  =  0  ;  i  <  existing  .  length  ;  i  ++  )  { 
if  (  local  .  getString  (  GlobalMessages  .  VAULTCLIPSE_CONSOLE  .  getKey  (  )  )  .  equals  (  existing  [  i  ]  .  getName  (  )  )  )  { 
MessageConsole   console  =  (  MessageConsole  )  existing  [  i  ]  ; 
console  .  clearConsole  (  )  ; 
return   console  ; 
} 
} 
MessageConsole   myConsole  =  new   MessageConsole  (  local  .  getString  (  GlobalMessages  .  VAULTCLIPSE_CONSOLE  .  getKey  (  )  )  ,  null  )  ; 
conMan  .  addConsoles  (  new   IConsole  [  ]  {  myConsole  }  )  ; 
return   myConsole  ; 
} 

public   void   shutdown  (  )  throws   CoreException  { 
IPreferenceStore   preferenceStore  =  getPreferenceStore  (  )  ; 
String   tempPath  =  preferenceStore  .  getString  (  PreferencesMessages  .  TEMP_DIRECTORY  .  getKey  (  )  )  ; 
File   tempDir  =  new   File  (  tempPath  +  File  .  separator  +  VaultSvc  .  VAULTCLIPSE_TEMP_DIR  )  ; 
if  (  tempDir  .  exists  (  )  )  { 
File  [  ]  files  =  tempDir  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
delete  (  files  [  i  ]  )  ; 
} 
} 
super  .  shutdown  (  )  ; 
} 
} 

