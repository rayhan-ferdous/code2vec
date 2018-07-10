package   de  .  teamwork  .  jaicwain  .  gui  .  swing  ; 

import   java  .  awt  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  text  .  *  ; 
import   de  .  teamwork  .  ctcp  .  *  ; 
import   de  .  teamwork  .  ctcp  .  msgutils  .  *  ; 
import   de  .  teamwork  .  irc  .  *  ; 
import   de  .  teamwork  .  irc  .  msgutils  .  *  ; 
import   de  .  teamwork  .  jaicwain  .  App  ; 
import   de  .  teamwork  .  jaicwain  .  gui  .  *  ; 
import   de  .  teamwork  .  jaicwain  .  options  .  *  ; 
import   de  .  teamwork  .  jaicwain  .  session  .  irc  .  *  ; 
import   de  .  teamwork  .  util  .  swing  .  JHistoryTextField  ; 









public   class   IRCChatPanel   extends   ExtendedPanel   implements   ActionListener  ,  IRCChannelListener  ,  KeyListener  ,  OptionsChangedListener  ,  IRCSessionListener  { 




private   DefaultIRCChannel   channel  =  null  ; 




private   DefaultListModel   listModel  =  null  ; 




private   String  [  ]  users  =  new   String  [  0  ]  ; 




private   MutableAttributeSet   currStyle  =  new   SimpleAttributeSet  (  )  ; 




private   boolean   bold  =  false  ; 




private   boolean   italic  =  false  ; 




private   boolean   underline  =  false  ; 




public   static   final   Color  [  ]  IRC_TEXT_COLORS  =  {  Color  .  white  ,  Color  .  black  ,  new   Color  (  0x00007f  )  ,  new   Color  (  0x009300  )  ,  Color  .  red  ,  new   Color  (  0x7f0000  )  ,  new   Color  (  0x9c009c  )  ,  new   Color  (  0xfc7f00  )  ,  Color  .  yellow  ,  Color  .  green  ,  new   Color  (  0x009393  )  ,  Color  .  cyan  ,  Color  .  blue  ,  Color  .  magenta  ,  Color  .  darkGray  ,  Color  .  lightGray  }  ; 

private   JSplitPane   splitPane  ; 

private   JScrollPane   logScrollPane  ; 

private   JScrollPane   lstScrollPane  ; 

private   JTextPane   logTextPane  ; 

private   JList   userList  ; 

private   JHistoryTextField   commandField  ; 






public   IRCChatPanel  (  DefaultIRCChannel   channel  )  { 
super  (  )  ; 
if  (  channel  ==  null  )  throw   new   NullPointerException  (  "channel can't be null"  )  ; 
this  .  channel  =  channel  ; 
channel  .  addIRCChannelListener  (  this  )  ; 
channel  .  getParentSession  (  )  .  addIRCSessionListener  (  this  )  ; 
createUI  (  )  ; 
adaptUI  (  )  ; 
setPanelTitle  (  channel  .  getName  (  )  )  ; 
setPanelHeading  (  channel  .  getName  (  )  )  ; 
setPanelDescription  (  IRCUtils  .  getDecodedString  (  channel  .  getTopic  (  )  )  )  ; 
if  (  IRCUtils  .  isChannel  (  channel  .  getName  (  )  )  )  { 
}  else  { 
} 
App  .  options  .  addOptionsChangedListener  (  this  )  ; 
} 




private   void   createUI  (  )  { 
logTextPane  =  new   JTextPane  (  )  ; 
logTextPane  .  setEditable  (  false  )  ; 
logScrollPane  =  new   JScrollPane  (  logTextPane  ,  JScrollPane  .  VERTICAL_SCROLLBAR_ALWAYS  ,  JScrollPane  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
logScrollPane  .  setMinimumSize  (  new   Dimension  (  100  ,  50  )  )  ; 
userList  =  new   JList  (  listModel  =  new   DefaultListModel  (  )  )  ; 
userList  .  setSelectionMode  (  ListSelectionModel  .  SINGLE_SELECTION  )  ; 
lstScrollPane  =  new   JScrollPane  (  userList  ,  JScrollPane  .  VERTICAL_SCROLLBAR_AS_NEEDED  ,  JScrollPane  .  HORIZONTAL_SCROLLBAR_AS_NEEDED  )  ; 
lstScrollPane  .  setMinimumSize  (  new   Dimension  (  100  ,  50  )  )  ; 
commandField  =  new   JHistoryTextField  (  App  .  options  .  getIntOption  (  "gui"  ,  "historytextfield.size"  ,  25  )  )  ; 
commandField  .  setEditable  (  true  )  ; 
commandField  .  setName  (  "field"  )  ; 
commandField  .  setVisible  (  true  )  ; 
commandField  .  addActionListener  (  this  )  ; 
commandField  .  addKeyListener  (  this  )  ; 
TreeSet   keys  =  new   TreeSet  (  )  ; 
keys  .  add  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_TAB  ,  InputEvent  .  CTRL_MASK  )  )  ; 
commandField  .  setFocusTraversalKeys  (  KeyboardFocusManager  .  FORWARD_TRAVERSAL_KEYS  ,  keys  )  ; 
splitPane  =  new   JSplitPane  (  JSplitPane  .  HORIZONTAL_SPLIT  ,  false  )  ; 
splitPane  .  setBorder  (  null  )  ; 
splitPane  .  setDividerSize  (  4  )  ; 
splitPane  .  setOneTouchExpandable  (  false  )  ; 
int   location  =  0  ; 
if  (  App  .  options  .  getBooleanOption  (  "gui"  ,  "chatsplitpane.userlistleft"  ,  false  )  )  { 
location  =  150  ; 
}  else  { 
location  =  splitPane  .  getWidth  (  )  -  150  ; 
} 
splitPane  .  setDividerLocation  (  App  .  options  .  getIntOption  (  "gui"  ,  "chatsplitpane.divider.location"  ,  location  )  )  ; 
GridBagLayout   g  =  new   GridBagLayout  (  )  ; 
GridBagConstraints   c  =  null  ; 
setLayout  (  g  )  ; 
c  =  new   GridBagConstraints  (  )  ; 
c  .  weightx  =  1.0f  ; 
c  .  weighty  =  1.0f  ; 
c  .  gridx  =  0  ; 
c  .  gridy  =  0  ; 
c  .  anchor  =  GridBagConstraints  .  NORTHWEST  ; 
c  .  fill  =  GridBagConstraints  .  BOTH  ; 
g  .  setConstraints  (  splitPane  ,  c  )  ; 
add  (  splitPane  )  ; 
c  =  new   GridBagConstraints  (  )  ; 
c  .  weightx  =  1.0f  ; 
c  .  weighty  =  0.0f  ; 
c  .  gridx  =  0  ; 
c  .  gridy  =  1  ; 
c  .  anchor  =  GridBagConstraints  .  SOUTH  ; 
c  .  fill  =  GridBagConstraints  .  HORIZONTAL  ; 
g  .  setConstraints  (  commandField  ,  c  )  ; 
add  (  commandField  )  ; 
this  .  addKeyListener  (  this  )  ; 
} 





private   void   adaptUI  (  )  { 
Font   font  =  new   Font  (  App  .  options  .  getStringOption  (  "gui"  ,  "chattextarea.font.name"  ,  "Monospaced"  )  ,  App  .  options  .  getIntOption  (  "gui"  ,  "chattextarea.font.style"  ,  0  )  ,  App  .  options  .  getIntOption  (  "gui"  ,  "chattextarea.font.size"  ,  12  )  )  ; 
logTextPane  .  setFont  (  font  )  ; 
font  =  new   Font  (  App  .  options  .  getStringOption  (  "gui"  ,  "chattextfield.font.name"  ,  "Monospaced"  )  ,  App  .  options  .  getIntOption  (  "gui"  ,  "chattextfield.font.style"  ,  0  )  ,  App  .  options  .  getIntOption  (  "gui"  ,  "chattextfield.font.size"  ,  12  )  )  ; 
commandField  .  setFont  (  font  )  ; 
commandField  .  setHistorySize  (  App  .  options  .  getIntOption  (  "gui"  ,  "historytextfield.size"  ,  25  )  )  ; 
if  (  App  .  options  .  getBooleanOption  (  "gui"  ,  "chatsplitpane.userlistleft"  ,  false  )  )  { 
splitPane  .  setLeftComponent  (  lstScrollPane  )  ; 
splitPane  .  setRightComponent  (  logScrollPane  )  ; 
splitPane  .  setResizeWeight  (  0  )  ; 
}  else  { 
splitPane  .  setLeftComponent  (  logScrollPane  )  ; 
splitPane  .  setRightComponent  (  lstScrollPane  )  ; 
splitPane  .  setResizeWeight  (  1  )  ; 
} 
} 






public   DefaultIRCChannel   getChannel  (  )  { 
return   channel  ; 
} 







public   JMenuItem  [  ]  getPanelMenuItems  (  )  { 
return   null  ; 
} 












public   synchronized   void   append  (  String   message  ,  String   type  ,  boolean   flag  )  { 
if  (  !  type  .  equals  (  "PRIVMSG"  )  )  { 
message  =  "* "  +  message  ; 
} 
if  (  type  .  equals  (  "NOTICE"  )  ||  type  .  equals  (  "NOTIFY"  )  )  { 
message  =  "**"  +  message  ; 
} 
if  (  flag  )  { 
setStyleBold  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles.FLAGGED.bold"  ,  false  )  )  ; 
setStyleItalic  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles.FLAGGED.italic"  ,  false  )  )  ; 
setStyleUnderline  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles.FLAGGED.underline"  ,  false  )  )  ; 
setStyleForeground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles.FLAGGED.foreground"  ,  Color  .  BLACK  )  )  ; 
setStyleBackground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles.FLAGGED.background"  ,  new   Color  (  -  921360  )  )  )  ; 
decode  (  message  ,  type  )  ; 
panelContainer  .  flagContentChange  (  this  ,  true  )  ; 
return  ; 
} 
setStyleBold  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles."  +  type  +  ".bold"  ,  false  )  )  ; 
setStyleItalic  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles."  +  type  +  ".italic"  ,  false  )  )  ; 
setStyleUnderline  (  App  .  options  .  getBooleanOption  (  "irc"  ,  "styles."  +  type  +  ".underline"  ,  false  )  )  ; 
setStyleForeground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles."  +  type  +  ".foreground"  ,  Color  .  BLACK  )  )  ; 
setStyleBackground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles."  +  type  +  ".background"  ,  Color  .  WHITE  )  )  ; 
decode  (  message  ,  type  )  ; 
panelContainer  .  flagContentChange  (  this  ,  false  )  ; 
} 






private   void   write  (  String   message  )  { 
int   position  =  logScrollPane  .  getVerticalScrollBar  (  )  .  getValue  (  )  ; 
try  { 
logTextPane  .  getDocument  (  )  .  insertString  (  logTextPane  .  getDocument  (  )  .  getLength  (  )  ,  message  ,  currStyle  )  ; 
}  catch  (  BadLocationException   e  )  { 
} 
logTextPane  .  setCaretPosition  (  logTextPane  .  getDocument  (  )  .  getLength  (  )  )  ; 
} 





protected   void   writeDecodedMessage  (  StringBuffer   buf  )  { 
write  (  buf  .  toString  (  )  )  ; 
buf  .  setLength  (  0  )  ; 
} 







private   void   decode  (  String   message  ,  String   type  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
int   len  =  message  .  length  (  )  ; 
int   i  ; 
char   c  ; 
for  (  i  =  0  ;  i  <  len  ;  i  ++  )  { 
c  =  message  .  charAt  (  i  )  ; 
switch  (  c  )  { 
case  '\002'  : 
writeDecodedMessage  (  buf  )  ; 
toggleStyleBold  (  )  ; 
break  ; 
case  '\003'  : 
boolean   comma  =  false  ; 
char   tmp  ; 
int   j  =  1  ; 
StringBuffer   fgCol  =  new   StringBuffer  (  ""  )  ; 
StringBuffer   bgCol  =  new   StringBuffer  (  ""  )  ; 
for  (  j  =  1  ;  j  <=  5  ;  j  ++  )  { 
if  (  i  +  j  <  len  )  { 
tmp  =  message  .  charAt  (  i  +  j  )  ; 
if  (  (  tmp  >=  '0'  )  &&  (  tmp  <=  '9'  )  )  { 
if  (  comma  )  { 
if  (  bgCol  .  length  (  )  ==  2  )  { 
break  ; 
}  else  { 
bgCol  .  append  (  tmp  )  ; 
} 
}  else  { 
if  (  fgCol  .  length  (  )  ==  2  )  { 
break  ; 
}  else  { 
fgCol  .  append  (  tmp  )  ; 
} 
} 
}  else   if  (  tmp  ==  ','  )  { 
comma  =  true  ; 
}  else  { 
break  ; 
} 
} 
} 
i  +=  j  -  1  ; 
writeDecodedMessage  (  buf  )  ; 
setStyleForeground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles."  +  type  +  ".foreground"  ,  Color  .  BLACK  )  )  ; 
setStyleBackground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles."  +  type  +  ".background"  ,  Color  .  WHITE  )  )  ; 
break  ; 
case  '\026'  : 
writeDecodedMessage  (  buf  )  ; 
setStyleForeground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles.PRIVMSG.background"  ,  Color  .  WHITE  )  )  ; 
setStyleBackground  (  App  .  options  .  getColorOption  (  "irc"  ,  "styles.PRIVMSG.foreground"  ,  Color  .  BLACK  )  )  ; 
break  ; 
case  '\037'  : 
writeDecodedMessage  (  buf  )  ; 
toggleStyleUnderline  (  )  ; 
break  ; 
default  : 
buf  .  append  (  c  )  ; 
break  ; 
} 
} 
writeDecodedMessage  (  buf  )  ; 
writeDecodedMessage  (  new   StringBuffer  (  "\n"  )  )  ; 
} 




protected   synchronized   void   setStyleBold  (  boolean   bold  )  { 
currStyle  .  removeAttribute  (  StyleConstants  .  Bold  )  ; 
if  (  bold  )  currStyle  .  addAttribute  (  StyleConstants  .  Bold  ,  Boolean  .  TRUE  )  ; 
this  .  bold  =  bold  ; 
} 




protected   void   toggleStyleBold  (  )  { 
setStyleBold  (  !  bold  )  ; 
} 




protected   synchronized   void   setStyleItalic  (  boolean   italic  )  { 
currStyle  .  removeAttribute  (  StyleConstants  .  Italic  )  ; 
if  (  italic  )  currStyle  .  addAttribute  (  StyleConstants  .  Italic  ,  Boolean  .  TRUE  )  ; 
this  .  italic  =  italic  ; 
} 




protected   void   toggleStyleItalic  (  )  { 
setStyleItalic  (  !  italic  )  ; 
} 




protected   synchronized   void   setStyleUnderline  (  boolean   underline  )  { 
currStyle  .  removeAttribute  (  StyleConstants  .  Underline  )  ; 
if  (  underline  )  currStyle  .  addAttribute  (  StyleConstants  .  Underline  ,  Boolean  .  TRUE  )  ; 
this  .  underline  =  underline  ; 
} 




protected   void   toggleStyleUnderline  (  )  { 
setStyleUnderline  (  !  underline  )  ; 
} 




void   setStyleForeground  (  int   index  )  { 
setStyleForeground  (  IRC_TEXT_COLORS  [  index  ]  )  ; 
} 




protected   void   setStyleForeground  (  Color   col  )  { 
currStyle  .  removeAttribute  (  StyleConstants  .  Foreground  )  ; 
currStyle  .  addAttribute  (  StyleConstants  .  Foreground  ,  col  )  ; 
} 




protected   void   setStyleBackground  (  int   index  )  { 
setStyleBackground  (  IRC_TEXT_COLORS  [  index  ]  )  ; 
} 




protected   void   setStyleBackground  (  Color   col  )  { 
currStyle  .  removeAttribute  (  StyleConstants  .  Background  )  ; 
currStyle  .  addAttribute  (  StyleConstants  .  Background  ,  col  )  ; 
} 









private   Color   parseColorString  (  String   col  ,  Color   defaultColor  )  { 
try  { 
int   color  =  Integer  .  parseInt  (  col  )  ; 
while  (  color  >  16  )  color  -=  16  ; 
return   IRC_TEXT_COLORS  [  color  ]  ; 
}  catch  (  NumberFormatException   e  )  { 
return   defaultColor  ; 
} 
} 




public   void   actionPerformed  (  ActionEvent   e  )  { 
channel  .  processUserCommand  (  commandField  .  getText  (  )  )  ; 
commandField  .  setText  (  ""  )  ; 
} 

public   void   messageProcessed  (  AbstractIRCChannel   channel  ,  IRCMessage   msg  )  { 
if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_JOIN  )  )  { 
String   name  =  msg  .  getNick  (  )  ; 
users  =  channel  .  getUsers  (  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  users  [  i  ]  .  equals  (  name  )  )  listModel  .  insertElementAt  (  name  ,  i  )  ; 
if  (  msg  .  getNick  (  )  .  equals  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  )  { 
Object   args  [  ]  =  new   Object  [  ]  {  new   Date  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_join.self"  ,  "Joined the channel on {0,date,full} at {0,time,full}"  ,  args  )  ; 
append  (  txt  ,  "JOIN"  ,  false  )  ; 
}  else  { 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_join"  ,  "{0} has joined the channel"  ,  args  )  ; 
append  (  txt  ,  "JOIN"  ,  false  )  ; 
} 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_KICK  )  )  { 
String   name  =  KickMessage  .  getUser  (  msg  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  IRCUtils  .  stripNickStatus  (  users  [  i  ]  )  .  equals  (  name  )  )  listModel  .  removeElementAt  (  i  )  ; 
users  =  channel  .  getUsers  (  )  ; 
String   args  [  ]  =  new   String  [  ]  {  KickMessage  .  getUser  (  msg  )  ,  msg  .  getNick  (  )  ,  KickMessage  .  getComment  (  msg  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_kick"  ,  "{0} has been kicked by {1}. Reason: {2}"  ,  args  )  ; 
append  (  txt  ,  "KICK"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_MODE  )  )  { 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_NICK  )  )  { 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  IRCUtils  .  stripNickStatus  (  users  [  i  ]  )  .  equals  (  msg  .  getNick  (  )  )  )  { 
listModel  .  removeElementAt  (  i  )  ; 
break  ; 
} 
users  =  channel  .  getUsers  (  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  IRCUtils  .  stripNickStatus  (  users  [  i  ]  )  .  equals  (  NickMessage  .  getNickname  (  msg  )  )  )  { 
listModel  .  insertElementAt  (  users  [  i  ]  ,  i  )  ; 
break  ; 
} 
if  (  NickMessage  .  getNickname  (  msg  )  .  equals  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  )  { 
return  ; 
} 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  NickMessage  .  getNickname  (  msg  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_nick"  ,  "{0} changed his nick to {1}"  ,  args  )  ; 
append  (  txt  ,  "NICK"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_NOTICE  )  )  { 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  NoticeMessage  .  getText  (  msg  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_notice"  ,  "{0}: {1}"  ,  args  )  ; 
append  (  txt  ,  "NOTICE"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_PART  )  )  { 
if  (  msg  .  getNick  (  )  .  equals  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  )  { 
Object   args  [  ]  =  new   Object  [  ]  {  new   Date  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_part.self"  ,  "Left the channel on {0,date,full} at {0,time,full}"  ,  args  )  ; 
append  (  txt  ,  "PART"  ,  false  )  ; 
} 
String   name  =  msg  .  getNick  (  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  IRCUtils  .  stripNickStatus  (  users  [  i  ]  )  .  equals  (  name  )  )  listModel  .  removeElementAt  (  i  )  ; 
users  =  channel  .  getUsers  (  )  ; 
String   txt  =  PartMessage  .  getMessage  (  msg  )  ; 
if  (  txt  .  equals  (  ""  )  )  txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_part.nopartmessage"  ,  "No part message"  )  ; 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  txt  }  ; 
txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_part"  ,  "{0} has left the channel ({1})"  ,  args  )  ; 
append  (  txt  ,  "PART"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_PRIVMSG  )  )  { 
if  (  CTCPMessage  .  isCTCPMessage  (  PrivateMessage  .  getText  (  msg  )  )  )  { 
CTCPMessage   ctcp  =  CTCPMessage  .  parseMessageString  (  PrivateMessage  .  getText  (  msg  )  )  ; 
if  (  ctcp  .  getType  (  )  .  equals  (  CTCPMessageTypes  .  CTCP_ACTION  )  )  { 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  ActionMessage  .  getText  (  ctcp  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_privmsg.ctcp_action"  ,  "{0} {1}"  ,  args  )  ; 
boolean   flag  =  (  ActionMessage  .  getText  (  ctcp  )  .  indexOf  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  !=  -  1  )  ; 
append  (  txt  ,  "ACTION"  ,  flag  )  ; 
} 
}  else  { 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  PrivateMessage  .  getText  (  msg  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_privmsg"  ,  "<{0}> {1}"  ,  args  )  ; 
boolean   flag  =  (  PrivateMessage  .  getText  (  msg  )  .  indexOf  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  !=  -  1  )  ; 
append  (  txt  ,  "PRIVMSG"  ,  flag  )  ; 
} 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_QUIT  )  )  { 
if  (  msg  .  getNick  (  )  .  equals  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  )  { 
Object   args  [  ]  =  new   Object  [  ]  {  new   Date  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_quit.self"  ,  "Left IRC on {0,date,full} at {0,time,full}"  ,  args  )  ; 
append  (  txt  ,  "QUIT"  ,  false  )  ; 
panelContainer  .  removeExtendedPanel  (  this  )  ; 
} 
String   name  =  msg  .  getNick  (  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  if  (  IRCUtils  .  stripNickStatus  (  users  [  i  ]  )  .  equals  (  name  )  )  listModel  .  removeElementAt  (  i  )  ; 
users  =  channel  .  getUsers  (  )  ; 
String   txt  =  QuitMessage  .  getQuitMessage  (  msg  )  ; 
if  (  txt  .  equals  (  ""  )  )  txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_quit.noquitmessage"  ,  "No quit message"  )  ; 
String   args  [  ]  =  new   String  [  ]  {  msg  .  getNick  (  )  ,  txt  }  ; 
txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_quit"  ,  "{0} has quit IRC ({1})"  ,  args  )  ; 
append  (  txt  ,  "QUIT"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  MSG_TOPIC  )  )  { 
setPanelDescription  (  IRCUtils  .  getDecodedString  (  TopicMessage  .  getTopic  (  msg  )  )  )  ; 
String   args  [  ]  =  new   String  [  ]  {  channel  .  getTopic  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.msg_topic"  ,  "The channel''s topic has been changed to: {0}"  ,  args  )  ; 
append  (  txt  ,  "TOPIC"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  ERR_CANNOTSENDTOCHAN  )  )  { 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.err_cannotsendtochan"  ,  "The message couldn't be sent to the channel"  )  ; 
append  (  txt  ,  "ERROR"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  RPL_AWAY  )  )  { 
String   args  [  ]  =  new   String  [  ]  {  AwayReply  .  getNick  (  msg  )  ,  AwayReply  .  getMessage  (  msg  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.rpl_away"  ,  "{0} is away ({1})"  ,  args  )  ; 
append  (  txt  ,  "REPLY"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  RPL_NOTOPIC  )  )  { 
setPanelDescription  (  ""  )  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.rpl_notopic"  ,  "Nobody dared to set a channel topic yet"  )  ; 
append  (  txt  ,  "REPLY"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  RPL_TOPIC  )  )  { 
setPanelDescription  (  IRCUtils  .  getDecodedString  (  TopicMessage  .  getTopic  (  msg  )  )  )  ; 
String   args  [  ]  =  new   String  [  ]  {  channel  .  getTopic  (  )  }  ; 
String   txt  =  App  .  localization  .  localize  (  "app"  ,  "irc.rpl_topic"  ,  "The channel''s topic is {0}"  ,  args  )  ; 
append  (  txt  ,  "REPLY"  ,  false  )  ; 
}  else   if  (  msg  .  getType  (  )  .  equals  (  IRCMessageTypes  .  RPL_ENDOFNAMES  )  )  { 
int   length  =  users  .  length  ; 
users  =  new   String  [  0  ]  ; 
listModel  .  clear  (  )  ; 
users  =  channel  .  getUsers  (  )  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  listModel  .  insertElementAt  (  users  [  i  ]  ,  i  )  ; 
} 
} 

public   void   messageProcessed  (  AbstractIRCSession   session  ,  IRCMessage   msg  )  { 
} 

public   void   channelCreated  (  AbstractIRCSession   session  ,  AbstractIRCChannel   channel  )  { 
} 

public   void   channelRemoved  (  AbstractIRCSession   session  ,  AbstractIRCChannel   channel  )  { 
if  (  channel  ==  this  .  channel  )  { 
channel  .  getParentSession  (  )  .  removeIRCSessionListener  (  this  )  ; 
panelContainer  .  removeExtendedPanel  (  this  )  ; 
} 
} 






public   void   keyTyped  (  KeyEvent   e  )  { 
} 






public   void   keyPressed  (  KeyEvent   e  )  { 
if  (  (  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_U  )  &&  e  .  isControlDown  (  )  )  { 
commandField  .  setText  (  ""  )  ; 
}  else   if  (  e  .  getKeyCode  (  )  ==  KeyEvent  .  VK_TAB  )  { 
int   offs  =  0  ; 
int   begOffs  =  0  ; 
int   endOffs  =  0  ; 
try  { 
offs  =  commandField  .  getCaretPosition  (  )  ; 
begOffs  =  Utilities  .  getWordStart  (  commandField  ,  offs  )  ; 
endOffs  =  Utilities  .  getWordEnd  (  commandField  ,  offs  )  ; 
commandField  .  moveCaretPosition  (  begOffs  )  ; 
commandField  .  setSelectionStart  (  begOffs  )  ; 
commandField  .  setSelectionEnd  (  endOffs  )  ; 
}  catch  (  BadLocationException   bl  )  { 
return  ; 
} 
String  [  ]  users  =  channel  .  getUsers  (  )  ; 
String   text  =  commandField  .  getSelectedText  (  )  ; 
ArrayList   matches  =  new   ArrayList  (  1  )  ; 
String   item  =  ""  ; 
for  (  int   i  =  0  ;  i  <  users  .  length  ;  i  ++  )  { 
item  =  users  [  i  ]  ; 
if  (  item  .  equals  (  channel  .  getParentSession  (  )  .  getUser  (  )  .  getNick  (  )  )  )  continue  ; 
if  (  item  .  startsWith  (  text  )  )  matches  .  add  (  item  )  ; 
} 
if  (  matches  .  size  (  )  ==  1  )  { 
if  (  begOffs  ==  0  )  commandField  .  replaceSelection  (  (  String  )  matches  .  get  (  0  )  +  ": "  )  ;  else   commandField  .  replaceSelection  (  (  String  )  matches  .  get  (  0  )  )  ; 
}  else   if  (  matches  .  size  (  )  >  1  )  { 
commandField  .  setCaretPosition  (  endOffs  )  ; 
} 
} 
commandField  .  requestFocusInWindow  (  )  ; 
} 






public   void   keyReleased  (  KeyEvent   e  )  { 
} 

public   void   optionsChanged  (  String   set  )  { 
if  (  set  .  equals  (  "gui"  )  )  adaptUI  (  )  ; 
} 
} 

