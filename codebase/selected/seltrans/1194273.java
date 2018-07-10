package   com  .  ondelette  .  servlet  .  webforum  ; 

import   java  .  awt  .  image  .  *  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  event  .  *  ; 
import   javax  .  swing  .  border  .  *  ; 
import   javax  .  swing  .  tree  .  *  ; 
import   javax  .  swing  .  text  .  *  ; 
import   javax  .  swing  .  text  .  html  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  awt  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 
import   com  .  ondelette  .  servlet  .  *  ; 
import   com  .  ondelette  .  servlet  .  laf  .  *  ; 
import   javax  .  swing  .  plaf  .  metal  .  *  ; 








public   final   class   SwingForumApplet   extends   JApplet   implements   ParameterNames  { 






protected   AppletLocale   mAppletLocale  =  new   AppletLocale  (  "/appletlocale.properties"  )  ; 

private   MessageDisplay   mMessageDisplay  ; 

private   ForumBrowser   mForumBrowser  ; 

private   String   mCopyrightString  =  "(c) 2000 Daniel Lemire (http://www.ondelette.com/)"  ; 

private   JLabel   mStatus  ; 

private   Hashtable   mMessageHolderTable  =  new   Hashtable  (  )  ; 

private   MessageHolder   mMessageHolder  ; 

private   String   mServletURLString  ; 

private   String   mForumFileString  ; 

private   URL   mForumURL  =  null  ; 

private   int   mImageIndex  =  0  ; 







public   static   void   main  (  String  [  ]  arg  )  { 
} 







public   void   setEnabled  (  boolean   b  )  { 
mMessageDisplay  .  setEnabled  (  b  )  ; 
mForumBrowser  .  setEnabled  (  b  )  ; 
} 







public   MessageHolder   getMessageHolder  (  )  { 
MessageHolder   mh  =  (  MessageHolder  )  mMessageHolderTable  .  get  (  mForumURL  )  ; 
if  (  mh  ==  null  )  { 
mh  =  new   MessageHolder  (  mForumURL  ,  mForumFileString  ,  this  ,  mAppletLocale  )  ; 
mMessageHolderTable  .  put  (  mForumURL  ,  mh  )  ; 
} 
return  (  mh  )  ; 
} 






public   void   init  (  )  { 
Point   middle  =  new   Point  (  this  .  getLocation  (  )  .  x  +  this  .  getWidth  (  )  /  2  ,  this  .  getLocation  (  )  .  y  +  this  .  getHeight  (  )  /  2  )  ; 
LoadingMessageWindow   lm  =  new   LoadingMessageWindow  (  mAppletLocale  .  getLoadingText  (  )  ,  middle  )  ; 
setupGUI  (  )  ; 
wireEvents  (  )  ; 
lm  .  dispose  (  )  ; 
} 






public   void   start  (  )  { 
mServletURLString  =  this  .  getParameter  (  "ServletURL"  )  ; 
mForumFileString  =  this  .  getParameter  (  "ForumFile"  )  ; 
try  { 
mForumURL  =  new   URL  (  mServletURLString  )  ; 
}  catch  (  MalformedURLException   murle  )  { 
murle  .  printStackTrace  (  )  ; 
} 
mStatus  .  setText  (  mAppletLocale  .  getLoadingText  (  )  )  ; 
mMessageHolder  =  getMessageHolder  (  )  ; 
mMessageHolder  .  getTree  (  0  )  ; 
} 







public   void   gotTree  (  TreeModel   tree  )  { 
mForumBrowser  .  setTreeModel  (  tree  )  ; 
mStatus  .  setText  (  mCopyrightString  )  ; 
mForumBrowser  .  setEnabled  (  true  )  ; 
} 








public   void   selected  (  MessageReference   mr  ,  Message   m  )  { 
mImageIndex  =  0  ; 
mMessageDisplay  .  setMessage  (  mr  ,  m  )  ; 
mMessageDisplay  .  setEnabled  (  true  )  ; 
} 






public   void   stop  (  )  { 
mMessageHolder  .  stop  (  )  ; 
} 






public   void   destroy  (  )  { 
mMessageHolder  .  destroy  (  )  ; 
} 







public   void   postForm  (  MessageReference   mr  )  { 
new   PostForm  (  mr  ,  this  )  ; 
} 







public   void   showPage  (  URL   url  )  { 
if  (  url  ==  null  )  { 
return  ; 
} 
System  .  out  .  println  (  "should show "  +  url  .  toString  (  )  )  ; 
this  .  getAppletContext  (  )  .  showDocument  (  url  ,  "_new"  )  ; 
} 







public   void   showImage  (  URL   url  )  { 
if  (  url  ==  null  )  { 
return  ; 
} 
mImageIndex  ++  ; 
mImageIndex  =  mImageIndex  %  10  ; 
System  .  out  .  println  (  "should show image "  +  url  .  toString  (  )  )  ; 
this  .  getAppletContext  (  )  .  showDocument  (  url  ,  "_image"  +  mImageIndex  )  ; 
} 







public   void   gotValidUser  (  User   u  )  { 
mMessageHolder  .  setUser  (  u  )  ; 
} 






private   void   setupGUI  (  )  { 
mStatus  =  new   JLabel  (  mCopyrightString  )  ; 
mStatus  .  setBorder  (  new   BevelBorder  (  BevelBorder  .  RAISED  )  )  ; 
mMessageDisplay  =  new   MessageDisplay  (  this  ,  new   MessageReference  (  -  1  )  ,  new   Message  (  ""  ,  ""  ,  ""  ,  ""  ,  null  ,  null  )  )  ; 
mMessageDisplay  .  setEnabled  (  false  )  ; 
mForumBrowser  =  new   ForumBrowser  (  this  )  ; 
mForumBrowser  .  setEnabled  (  false  )  ; 
this  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
JPanel   panel  =  new   JPanel  (  )  ; 
panel  .  setBorder  (  new   BevelBorder  (  BevelBorder  .  RAISED  )  )  ; 
panel  .  setLayout  (  new   GridLayout  (  0  ,  2  ,  4  ,  4  )  )  ; 
panel  .  add  (  mForumBrowser  )  ; 
panel  .  add  (  mMessageDisplay  )  ; 
this  .  getContentPane  (  )  .  add  (  panel  ,  BorderLayout  .  CENTER  )  ; 
this  .  getContentPane  (  )  .  add  (  mStatus  ,  BorderLayout  .  SOUTH  )  ; 
mMessageDisplay  .  setDoubleBuffered  (  false  )  ; 
mForumBrowser  .  setDoubleBuffered  (  false  )  ; 
mStatus  .  setDoubleBuffered  (  false  )  ; 
} 







private   void   loadTheme  (  String   themefile  )  { 
PropertiesMetalTheme   myTheme  =  null  ; 
URL   url  =  this  .  getClass  (  )  .  getResource  (  themefile  )  ; 
if  (  url  !=  null  )  { 
try  { 
InputStream   is  =  url  .  openStream  (  )  ; 
try  { 
myTheme  =  new   PropertiesMetalTheme  (  is  )  ; 
}  finally  { 
is  .  close  (  )  ; 
} 
if  (  myTheme  !=  null  )  { 
MetalLookAndFeel  .  setCurrentTheme  (  myTheme  )  ; 
} 
UIManager  .  setLookAndFeel  (  "javax.swing.plaf.metal.MetalLookAndFeel"  )  ; 
}  catch  (  Exception   ex  )  { 
System  .  out  .  println  (  "Failed loading Metal"  )  ; 
System  .  out  .  println  (  ex  )  ; 
} 
} 
} 






private   void   wireEvents  (  )  { 
} 








public   class   NoImageHTMLFactory   implements   ViewFactory  { 

private   HTMLEditorKit  .  HTMLFactory   mHtmlfact  =  new   HTMLEditorKit  .  HTMLFactory  (  )  ; 








public   View   create  (  Element   elem  )  { 
Object   o  =  elem  .  getAttributes  (  )  .  getAttribute  (  StyleConstants  .  NameAttribute  )  ; 
if  (  o   instanceof   HTML  .  Tag  )  { 
HTML  .  Tag   kind  =  (  HTML  .  Tag  )  o  ; 
if  (  kind  ==  HTML  .  Tag  .  IMG  )  { 
URL   url  ; 
if  (  (  url  =  getSourceURL  (  elem  )  )  !=  null  )  { 
SwingForumApplet  .  this  .  showImage  (  url  )  ; 
} 
return  (  new   ObjectView  (  elem  )  )  ; 
} 
} 
return  (  mHtmlfact  .  create  (  elem  )  )  ; 
} 








private   URL   getSourceURL  (  Element   elem  )  { 
String   src  =  (  String  )  elem  .  getAttributes  (  )  .  getAttribute  (  HTML  .  Attribute  .  SRC  )  ; 
if  (  src  ==  null  )  { 
return   null  ; 
} 
URL   reference  =  (  (  HTMLDocument  )  elem  .  getDocument  (  )  )  .  getBase  (  )  ; 
try  { 
URL   u  =  new   URL  (  reference  ,  src  )  ; 
return   u  ; 
}  catch  (  MalformedURLException   e  )  { 
return   null  ; 
} 
} 
} 







final   class   LoadingMessageWindow   extends   JWindow  { 








public   LoadingMessageWindow  (  String   message  ,  Point   p  )  { 
this  .  setBackground  (  Color  .  white  )  ; 
this  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
URL   logourl  =  this  .  getClass  (  )  .  getResource  (  "/logo_blanc_moyen.gif"  )  ; 
JLabel   mainlabel  =  null  ; 
if  (  logourl  !=  null  )  { 
mainlabel  =  new   JLabel  (  message  ,  new   ImageIcon  (  logourl  )  ,  SwingConstants  .  CENTER  )  ; 
}  else  { 
mainlabel  =  new   JLabel  (  message  ,  SwingConstants  .  CENTER  )  ; 
} 
mainlabel  .  setVerticalTextPosition  (  JLabel  .  BOTTOM  )  ; 
mainlabel  .  setHorizontalTextPosition  (  JLabel  .  CENTER  )  ; 
mainlabel  .  setBorder  (  new   BevelBorder  (  2  )  )  ; 
mainlabel  .  setBackground  (  Color  .  white  )  ; 
this  .  getContentPane  (  )  .  add  (  mainlabel  ,  BorderLayout  .  CENTER  )  ; 
this  .  pack  (  )  ; 
this  .  setLocation  (  p  )  ; 
mainlabel  .  setDoubleBuffered  (  false  )  ; 
this  .  show  (  )  ; 
} 
} 







final   class   MessageDisplay   extends   JComponent   implements   ActionListener  { 

private   Message   mMessage  ; 

private   MessageReference   mMessageReference  ; 

private   MessageHeader   mHeader  ; 

private   JEditorPane   mPane  ; 

private   SwingForumApplet   mOwner  ; 

private   JButton   mReplyButton  ; 









public   MessageDisplay  (  SwingForumApplet   owner  ,  MessageReference   mr  ,  Message   m  )  { 
mMessage  =  m  ; 
mMessageReference  =  mr  ; 
mOwner  =  owner  ; 
setupGUI  (  )  ; 
} 







public   void   setEnabled  (  boolean   b  )  { 
mReplyButton  .  setEnabled  (  b  )  ; 
} 








public   void   setMessage  (  MessageReference   mr  ,  Message   m  )  { 
mMessage  =  m  ; 
mMessageReference  =  mr  ; 
updateGUI  (  )  ; 
mHeader  .  setMessage  (  m  )  ; 
repaint  (  )  ; 
} 







public   boolean   isEnabled  (  )  { 
return  (  mReplyButton  .  isEnabled  (  )  )  ; 
} 







public   void   actionPerformed  (  ActionEvent   e  )  { 
mOwner  .  postForm  (  mMessageReference  )  ; 
} 






private   void   setupGUI  (  )  { 
mHeader  =  new   MessageHeader  (  mMessage  ,  mAppletLocale  )  ; 
mPane  =  new   JEditorPane  (  )  ; 
mPane  .  setContentType  (  "text/html"  )  ; 
mPane  .  setEditorKit  (  new   NoImageHTMLEditorKit  (  )  )  ; 
mPane  .  setEditable  (  false  )  ; 
if  (  mMessage  .  getMessage  (  )  !=  null  )  { 
mPane  .  setText  (  "<HTML><BODY>"  +  mMessage  .  getFormattedMessage  (  )  .  trim  (  )  +  "</BODY></HTML>"  )  ; 
}  else  { 
mPane  .  setText  (  ""  )  ; 
} 
LayoutManager   lm  =  new   BorderLayout  (  )  ; 
this  .  setLayout  (  lm  )  ; 
add  (  mHeader  ,  BorderLayout  .  NORTH  )  ; 
add  (  new   JScrollPane  (  mPane  )  ,  BorderLayout  .  CENTER  )  ; 
add  (  mReplyButton  =  new   JButton  (  mAppletLocale  .  getPostANewReplyText  (  )  )  ,  BorderLayout  .  SOUTH  )  ; 
mReplyButton  .  addActionListener  (  this  )  ; 
mPane  .  addHyperlinkListener  (  new   HyperlinkListener  (  )  { 

public   void   hyperlinkUpdate  (  HyperlinkEvent   e  )  { 
if  (  e  .  getEventType  (  )  ==  HyperlinkEvent  .  EventType  .  ACTIVATED  )  { 
mOwner  .  showPage  (  e  .  getURL  (  )  )  ; 
} 
} 
}  )  ; 
mPane  .  setDoubleBuffered  (  false  )  ; 
mReplyButton  .  setDoubleBuffered  (  false  )  ; 
} 






private   void   updateGUI  (  )  { 
if  (  mMessage  .  getMessage  (  )  !=  null  )  { 
mPane  .  setText  (  "<HTML><BODY>"  +  mMessage  .  getFormattedMessage  (  )  .  trim  (  )  +  "</BODY></HTML>"  )  ; 
}  else  { 
mPane  .  setText  (  ""  )  ; 
} 
} 







final   class   MessageHeader   extends   JComponent   implements   AutorizationConstants  { 

private   Message   mMessage  ; 

private   JLabel   mAuthorLabel  ,  mSubjectLabel  ; 

private   AppletLocale   mAppletLocale  ; 








public   MessageHeader  (  Message   m  ,  AppletLocale   al  )  { 
mAppletLocale  =  al  ; 
mMessage  =  m  ; 
setupGUI  (  )  ; 
} 







public   void   setMessage  (  Message   m  )  { 
mMessage  =  m  ; 
updateGUI  (  )  ; 
} 







public   Dimension   getMinimumSize  (  )  { 
return  (  getLayout  (  )  .  minimumLayoutSize  (  this  )  )  ; 
} 







public   Dimension   getPreferredSize  (  )  { 
return  (  getLayout  (  )  .  preferredLayoutSize  (  this  )  )  ; 
} 







public   Dimension   getMaximumSize  (  )  { 
return  (  getLayout  (  )  .  preferredLayoutSize  (  this  )  )  ; 
} 






private   void   setupGUI  (  )  { 
LayoutManager   lm  =  new   GridLayout  (  0  ,  1  ,  5  ,  5  )  ; 
this  .  setLayout  (  lm  )  ; 
add  (  mSubjectLabel  =  new   JLabel  (  )  )  ; 
this  .  add  (  mAuthorLabel  =  new   JLabel  (  )  )  ; 
updateGUI  (  )  ; 
this  .  setDoubleBuffered  (  false  )  ; 
mAuthorLabel  .  setDoubleBuffered  (  false  )  ; 
mSubjectLabel  .  setDoubleBuffered  (  false  )  ; 
} 






private   void   updateGUI  (  )  { 
if  (  mMessage  .  getSubject  (  )  !=  null  )  { 
mSubjectLabel  .  setText  (  mMessage  .  getSubject  (  )  .  trim  (  )  )  ; 
mSubjectLabel  .  setToolTipText  (  mMessage  .  getSubject  (  )  .  trim  (  )  )  ; 
}  else  { 
mSubjectLabel  .  setText  (  ""  )  ; 
mSubjectLabel  .  setToolTipText  (  ""  )  ; 
} 
mSubjectLabel  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getDateFormat  (  )  .  format  (  mMessage  .  getDate  (  )  )  )  )  ; 
if  (  mMessage  .  getAuthor  (  )  !=  null  )  { 
mAuthorLabel  .  setText  (  mMessage  .  getAuthor  (  )  .  trim  (  )  )  ; 
}  else  { 
mAuthorLabel  .  setText  (  ""  )  ; 
} 
if  (  mMessage  .  getAuthorEmail  (  )  !=  null  )  { 
mAuthorLabel  .  setBorder  (  new   TitledBorder  (  mMessage  .  getAuthorEmail  (  )  .  trim  (  )  )  )  ; 
mAuthorLabel  .  setToolTipText  (  mMessage  .  getAuthorEmail  (  )  )  ; 
}  else  { 
mAuthorLabel  .  setBorder  (  new   TitledBorder  (  ""  )  )  ; 
mAuthorLabel  .  setToolTipText  (  ""  )  ; 
} 
} 
} 
} 







final   class   UserIdentificationForm   extends   JDialog   implements   AutorizationConstants  { 

private   JTextField   mAuthorTextField  ,  mAuthorEmailTextField  ; 

private   JButton   mOKButton  ,  mCancelButton  ; 

private   PostForm   mPostForm  ; 

private   SwingForumApplet   mOwner  ; 







public   UserIdentificationForm  (  PostForm   sfa  )  { 
super  (  sfa  ,  true  )  ; 
mPostForm  =  sfa  ; 
mOwner  =  sfa  .  mOwner  ; 
mOwner  .  setEnabled  (  false  )  ; 
setupGUI  (  )  ; 
this  .  pack  (  )  ; 
Point   middle  =  new   Point  (  sfa  .  getX  (  )  +  sfa  .  getWidth  (  )  /  2  ,  sfa  .  getY  (  )  +  sfa  .  getHeight  (  )  /  2  )  ; 
this  .  setLocation  (  middle  )  ; 
setDefaultCloseOperation  (  WindowConstants  .  DO_NOTHING_ON_CLOSE  )  ; 
wireEvents  (  )  ; 
show  (  )  ; 
} 







public   boolean   registrer  (  )  { 
String   author  =  mAuthorTextField  .  getText  (  )  ; 
if  (  author  ==  null  )  { 
return   false  ; 
} 
author  =  author  .  trim  (  )  ; 
if  (  author  .  length  (  )  ==  0  )  { 
return   false  ; 
} 
String   email  =  mAuthorEmailTextField  .  getText  (  )  ; 
if  (  email  !=  null  )  { 
email  =  email  .  trim  (  )  ; 
} 
User   u  =  new   User  (  author  ,  null  ,  email  ,  AutorizationConstants  .  NORMAL  )  ; 
mOwner  .  gotValidUser  (  u  )  ; 
mOwner  .  setEnabled  (  true  )  ; 
return  (  true  )  ; 
} 






private   void   setupGUI  (  )  { 
JPanel   fieldpanel  =  new   JPanel  (  )  ; 
fieldpanel  .  setLayout  (  new   GridLayout  (  0  ,  1  ,  2  ,  2  )  )  ; 
fieldpanel  .  add  (  mAuthorTextField  =  new   JTextField  (  )  )  ; 
mAuthorTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getAuthorText  (  )  )  )  ; 
fieldpanel  .  add  (  mAuthorEmailTextField  =  new   JTextField  (  )  )  ; 
mAuthorEmailTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getAuthorEmailText  (  )  )  )  ; 
JPanel   buttonpanel  =  new   JPanel  (  )  ; 
buttonpanel  .  setLayout  (  new   GridLayout  (  1  ,  0  ,  2  ,  2  )  )  ; 
buttonpanel  .  add  (  mOKButton  =  new   JButton  (  mAppletLocale  .  getOKButtonText  (  )  )  )  ; 
buttonpanel  .  add  (  mCancelButton  =  new   JButton  (  mAppletLocale  .  getCancelButtonText  (  )  )  )  ; 
this  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
this  .  getContentPane  (  )  .  add  (  buttonpanel  ,  BorderLayout  .  SOUTH  )  ; 
this  .  getContentPane  (  )  .  add  (  fieldpanel  ,  BorderLayout  .  CENTER  )  ; 
mAuthorTextField  .  setDoubleBuffered  (  false  )  ; 
mAuthorEmailTextField  .  setDoubleBuffered  (  false  )  ; 
} 






private   void   wireEvents  (  )  { 
mOKButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  UserIdentificationForm  .  this  .  registrer  (  )  )  { 
mOwner  .  setEnabled  (  true  )  ; 
UserIdentificationForm  .  this  .  dispose  (  )  ; 
} 
} 
}  )  ; 
mCancelButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
mOwner  .  setEnabled  (  true  )  ; 
UserIdentificationForm  .  this  .  dispose  (  )  ; 
UserIdentificationForm  .  this  .  mPostForm  .  cancel  (  )  ; 
} 
}  )  ; 
} 
} 







final   class   PostForm   extends   JFrame  { 

private   MessageReference   mMessageReference  ; 

private   Message   mParentMessage  =  null  ; 

private   SwingForumApplet   mOwner  ; 

private   JTextField   mSubjectTextField  ,  mURLTextField  ,  mURLTitleTextField  ,  mImageTextField  ; 

private   JTextArea   mTextArea  =  new   JTextArea  (  10  ,  40  )  ; 

private   AppletLocale   mAppletLocale  ; 

private   JButton   mPostButton  ,  mCancelButton  ; 

private   final   String   HTTPSTRING  =  "http://"  ; 

private   boolean   mCancel  =  false  ; 








public   PostForm  (  MessageReference   mr  ,  SwingForumApplet   owner  )  { 
mOwner  =  owner  ; 
mMessageReference  =  mr  ; 
mAppletLocale  =  owner  .  mAppletLocale  ; 
setupGUI  (  )  ; 
if  (  mr  .  getID  (  )  >  0  )  { 
mParentMessage  =  mMessageHolder  .  getMessage  (  mr  )  ; 
} 
if  (  mParentMessage  !=  null  )  { 
setTitle  (  mParentMessage  .  toString  (  )  )  ; 
} 
this  .  pack  (  )  ; 
Point   middle  =  new   Point  (  mOwner  .  getX  (  )  +  mOwner  .  getWidth  (  )  /  2  ,  mOwner  .  getY  (  )  +  mOwner  .  getHeight  (  )  /  2  )  ; 
this  .  setLocation  (  middle  )  ; 
setDefaultCloseOperation  (  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
wireEvents  (  )  ; 
if  (  mOwner  .  mMessageHolder  .  getUser  (  )  ==  null  )  { 
new   UserIdentificationForm  (  this  )  ; 
if  (  mCancel  )  { 
dispose  (  )  ; 
}  else  { 
show  (  )  ; 
} 
}  else  { 
show  (  )  ; 
} 
} 







public   Message   getMessage  (  )  { 
StringBuffer   content  =  new   StringBuffer  (  mTextArea  .  getText  (  )  )  ; 
if  (  mImageTextField  .  getText  (  )  !=  null  )  { 
if  (  (  !  mImageTextField  .  getText  (  )  .  equals  (  HTTPSTRING  )  )  &&  (  mImageTextField  .  getText  (  )  .  length  (  )  >  0  )  )  { 
content  .  append  (  "<IMG SRC=\""  )  ; 
content  .  append  (  mImageTextField  .  getText  (  )  )  ; 
content  .  append  (  "\" align=center>"  )  ; 
} 
} 
if  (  mURLTextField  .  getText  (  )  !=  null  )  { 
if  (  (  !  mURLTextField  .  getText  (  )  .  equals  (  HTTPSTRING  )  )  &&  (  mURLTextField  .  getText  (  )  .  length  (  )  >  0  )  )  { 
content  .  append  (  "<DIV align=center><A HREF=\""  )  ; 
content  .  append  (  mURLTextField  .  getText  (  )  )  ; 
content  .  append  (  "\">"  )  ; 
if  (  mURLTitleTextField  .  getText  (  )  !=  null  )  { 
if  (  mURLTitleTextField  .  getText  (  )  .  length  (  )  >  0  )  { 
content  .  append  (  mURLTitleTextField  .  getText  (  )  )  ; 
}  else  { 
content  .  append  (  mURLTextField  .  getText  (  )  )  ; 
} 
}  else  { 
content  .  append  (  mURLTextField  .  getText  (  )  )  ; 
} 
content  .  append  (  "</A></DIV>\n"  )  ; 
} 
} 
Message   newmessage  =  new   Message  (  null  ,  null  ,  mSubjectTextField  .  getText  (  )  ,  content  .  toString  (  )  ,  null  ,  null  )  ; 
newmessage  .  setInReplyTo  (  mMessageReference  .  getID  (  )  )  ; 
return  (  newmessage  )  ; 
} 






public   void   cancel  (  )  { 
mCancel  =  true  ; 
} 






public   void   post  (  )  { 
mMessageHolder  .  sendMessage  (  getMessage  (  )  )  ; 
this  .  dispose  (  )  ; 
} 






private   void   setupGUI  (  )  { 
this  .  getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
JPanel   centerpanel  =  new   JPanel  (  )  ; 
centerpanel  .  setLayout  (  new   BorderLayout  (  )  )  ; 
JPanel   variousfieldspanel  =  new   JPanel  (  )  ; 
variousfieldspanel  .  setLayout  (  new   GridLayout  (  0  ,  1  ,  2  ,  2  )  )  ; 
variousfieldspanel  .  add  (  mURLTextField  =  new   JTextField  (  HTTPSTRING  )  )  ; 
mURLTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getURLText  (  )  )  )  ; 
variousfieldspanel  .  add  (  mURLTitleTextField  =  new   JTextField  (  )  )  ; 
mURLTitleTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getURLTitleText  (  )  )  )  ; 
variousfieldspanel  .  add  (  mImageTextField  =  new   JTextField  (  HTTPSTRING  )  )  ; 
mImageTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getImageText  (  )  )  )  ; 
centerpanel  .  add  (  variousfieldspanel  ,  BorderLayout  .  SOUTH  )  ; 
centerpanel  .  add  (  new   JScrollPane  (  mTextArea  )  )  ; 
mTextArea  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getMessageText  (  )  )  )  ; 
centerpanel  .  add  (  mSubjectTextField  =  new   JTextField  (  )  ,  BorderLayout  .  NORTH  )  ; 
mSubjectTextField  .  setBorder  (  new   TitledBorder  (  mAppletLocale  .  getSubjectText  (  )  )  )  ; 
this  .  getContentPane  (  )  .  add  (  centerpanel  ,  BorderLayout  .  CENTER  )  ; 
JPanel   buttonpanel  =  new   JPanel  (  )  ; 
buttonpanel  .  setLayout  (  new   GridLayout  (  1  ,  0  ,  5  ,  5  )  )  ; 
buttonpanel  .  add  (  mPostButton  =  new   JButton  (  mAppletLocale  .  getPostButtonText  (  )  )  )  ; 
buttonpanel  .  add  (  mCancelButton  =  new   JButton  (  mAppletLocale  .  getCancelButtonText  (  )  )  )  ; 
this  .  getContentPane  (  )  .  add  (  buttonpanel  ,  BorderLayout  .  SOUTH  )  ; 
mTextArea  .  setDoubleBuffered  (  false  )  ; 
mSubjectTextField  .  setDoubleBuffered  (  false  )  ; 
mURLTextField  .  setDoubleBuffered  (  false  )  ; 
mURLTitleTextField  .  setDoubleBuffered  (  false  )  ; 
mImageTextField  .  setDoubleBuffered  (  false  )  ; 
mPostButton  .  setDoubleBuffered  (  false  )  ; 
mCancelButton  .  setDoubleBuffered  (  false  )  ; 
} 






private   void   wireEvents  (  )  { 
mCancelButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
mOwner  .  setEnabled  (  true  )  ; 
PostForm  .  this  .  dispose  (  )  ; 
} 
}  )  ; 
mPostButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
PostForm  .  this  .  post  (  )  ; 
} 
}  )  ; 
} 
} 







final   class   ForumBrowser   extends   JComponent  { 

private   JButton   mPostButton  ; 

private   JButton   mExpandButton  ; 

private   JTree   mTree  ; 

private   SwingForumApplet   mOwner  ; 







public   ForumBrowser  (  SwingForumApplet   msl  )  { 
mOwner  =  msl  ; 
setupTree  (  )  ; 
setupGUI  (  )  ; 
wireEvents  (  )  ; 
} 







public   void   setEnabled  (  boolean   b  )  { 
mPostButton  .  setEnabled  (  b  )  ; 
} 







public   void   setTreeModel  (  TreeModel   tree  )  { 
mTree  .  setModel  (  tree  )  ; 
repaint  (  )  ; 
} 







public   boolean   isEnabled  (  )  { 
return  (  mPostButton  .  isEnabled  (  )  )  ; 
} 







public   Dimension   getMinimumSize  (  )  { 
return  (  getLayout  (  )  .  minimumLayoutSize  (  this  )  )  ; 
} 







public   Dimension   getPreferredSize  (  )  { 
return  (  getLayout  (  )  .  preferredLayoutSize  (  this  )  )  ; 
} 






private   void   setupGUI  (  )  { 
this  .  setLayout  (  new   BorderLayout  (  )  )  ; 
this  .  add  (  new   JScrollPane  (  mTree  )  ,  BorderLayout  .  CENTER  )  ; 
JPanel   buttonpanel  =  new   JPanel  (  )  ; 
buttonpanel  .  setDoubleBuffered  (  false  )  ; 
buttonpanel  .  add  (  mPostButton  =  new   JButton  (  mOwner  .  mAppletLocale  .  getPostANewMessageText  (  )  )  )  ; 
buttonpanel  .  add  (  mExpandButton  =  new   JButton  (  mOwner  .  mAppletLocale  .  getExpandText  (  )  )  )  ; 
this  .  add  (  buttonpanel  ,  BorderLayout  .  SOUTH  )  ; 
this  .  setDoubleBuffered  (  false  )  ; 
mTree  .  setDoubleBuffered  (  false  )  ; 
mPostButton  .  setDoubleBuffered  (  false  )  ; 
mExpandButton  .  setDoubleBuffered  (  false  )  ; 
} 






private   void   setupTree  (  )  { 
DefaultMutableTreeNode   parent  =  new   DefaultMutableTreeNode  (  "messages"  )  ; 
parent  .  add  (  new   DefaultMutableTreeNode  (  mOwner  .  mAppletLocale  .  getLoadingText  (  )  )  )  ; 
mTree  =  new   JTree  (  parent  )  ; 
mTree  .  getSelectionModel  (  )  .  setSelectionMode  (  TreeSelectionModel  .  SINGLE_TREE_SELECTION  )  ; 
mTree  .  setCellRenderer  (  new   MessageFormattingRenderer  (  mAppletLocale  .  getDateFormat  (  )  )  )  ; 
mTree  .  putClientProperty  (  "JTree.lineStyle"  ,  "Angled"  )  ; 
mTree  .  addTreeSelectionListener  (  new   TreeSelectionListener  (  )  { 

public   void   valueChanged  (  TreeSelectionEvent   e  )  { 
TreePath   path  =  e  .  getNewLeadSelectionPath  (  )  ; 
if  (  path  ==  null  )  { 
return  ; 
} 
DefaultMutableTreeNode   node  =  (  DefaultMutableTreeNode  )  path  .  getLastPathComponent  (  )  ; 
if  (  node  ==  null  )  { 
return  ; 
} 
if  (  node   instanceof   MessageNode  )  { 
Message   m  =  (  Message  )  node  .  getUserObject  (  )  ; 
MessageNode   mn  =  (  MessageNode  )  node  ; 
mOwner  .  selected  (  mn  .  getMessageReference  (  )  ,  m  )  ; 
mn  .  setHasBeenRead  (  true  )  ; 
} 
} 
}  )  ; 
mTree  .  addTreeExpansionListener  (  new   MessageExplorer  (  (  DefaultTreeModel  )  mTree  .  getModel  (  )  )  )  ; 
} 






private   void   wireEvents  (  )  { 
mPostButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
mOwner  .  postForm  (  new   MessageReference  (  -  1  )  )  ; 
} 
}  )  ; 
mExpandButton  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
if  (  mTree  ==  null  )  { 
return  ; 
} 
synchronized  (  this  )  { 
for  (  int   k  =  0  ;  k  <  mTree  .  getRowCount  (  )  ;  k  ++  )  { 
mTree  .  expandRow  (  k  )  ; 
} 
} 
} 
}  )  ; 
} 
} 







final   class   MessageHolder   implements   Runnable  ,  GenericServletInterface  { 

private   Hashtable   mMessageTable  =  new   Hashtable  (  )  ; 

private   Hashtable   mMessageNodeTable  =  new   Hashtable  (  )  ; 

private   URL   mForumURL  ; 

private   String   mForumFileString  ; 

private   JTree   mTree  ; 

private   Thread   mLoadTreeThread  =  null  ; 

private   SwingForumApplet   mTreeListener  ; 

private   int   mCurrentPage  =  -  1  ; 

private   int   mCurrentMaxIndex  =  -  1  ; 

private   AppletLocale   mAppletLocale  ; 

private   User   mUser  ; 

private   DefaultTreeModel   mTreeModel  ; 










public   MessageHolder  (  URL   forumurl  ,  String   forumfile  ,  SwingForumApplet   treelistener  ,  AppletLocale   al  )  { 
mAppletLocale  =  al  ; 
mForumURL  =  forumurl  ; 
mForumFileString  =  forumfile  ; 
mTreeListener  =  treelistener  ; 
} 







public   void   setUser  (  User   u  )  { 
mUser  =  u  ; 
} 







public   User   getUser  (  )  { 
return  (  mUser  )  ; 
} 







public   void   getTree  (  final   int   pagenumber  )  { 
if  (  mLoadTreeThread  !=  null  )  { 
try  { 
mLoadTreeThread  .  join  (  )  ; 
}  catch  (  InterruptedException   ie  )  { 
ie  .  printStackTrace  (  )  ; 
} 
mLoadTreeThread  =  null  ; 
} 
mCurrentPage  =  pagenumber  ; 
mLoadTreeThread  =  new   Thread  (  this  )  ; 
mLoadTreeThread  .  start  (  )  ; 
} 








public   Message   getMessage  (  MessageReference   mr  )  { 
return  (  (  Message  )  mMessageTable  .  get  (  mr  )  )  ; 
} 








public   MessageNode   getMessageNode  (  MessageReference   mr  )  { 
MessageNode   mn  =  (  MessageNode  )  mMessageNodeTable  .  get  (  mr  )  ; 
if  (  mn  ==  null  )  { 
Message   m  =  getMessage  (  mr  )  ; 
if  (  m  !=  null  )  { 
mn  =  new   MessageNode  (  mr  ,  m  ,  this  )  ; 
mMessageNodeTable  .  put  (  mr  ,  mn  )  ; 
} 
} 
return  (  mn  )  ; 
} 








public   boolean   sendMessage  (  Message   m  )  { 
if  (  mUser  ==  null  )  { 
System  .  err  .  println  (  "No user defined. Command ignored."  )  ; 
return  (  false  )  ; 
} 
m  .  setAutorizationLevel  (  mUser  .  getAutorizationLevel  (  )  )  ; 
m  .  setAuthor  (  mUser  .  getLogin  (  )  )  ; 
m  .  setAuthorEmail  (  mUser  .  getEmail  (  )  )  ; 
try  { 
URLConnection   con  =  connect  (  )  ; 
DataOutputStream   dos  =  new   DataOutputStream  (  con  .  getOutputStream  (  )  )  ; 
try  { 
sendStandardHeader  (  dos  )  ; 
dos  .  writeChar  (  PUT  )  ; 
dos  .  writeChar  (  MESSAGE  )  ; 
dos  .  writeInt  (  m  .  getInReplyTo  (  )  )  ; 
dos  .  flush  (  )  ; 
ObjectOutputStream   oos  =  new   ObjectOutputStream  (  dos  )  ; 
oos  .  writeObject  (  m  )  ; 
oos  .  flush  (  )  ; 
}  catch  (  IOException   ioe  )  { 
dos  .  close  (  )  ; 
ioe  .  printStackTrace  (  )  ; 
return  (  false  )  ; 
} 
dos  .  flush  (  )  ; 
dos  .  close  (  )  ; 
dos  =  null  ; 
InputStream   is  =  con  .  getInputStream  (  )  ; 
DataInputStream   dis  =  new   DataInputStream  (  is  )  ; 
int   newmesssageid  ; 
try  { 
readStandardHeader  (  dis  )  ; 
char   tc  ; 
if  (  (  tc  =  dis  .  readChar  (  )  )  !=  GET  )  { 
throw   new   IOException  (  "No GET found where expected! Instead I found : "  +  tc  )  ; 
} 
if  (  (  tc  =  dis  .  readChar  (  )  )  !=  MESSAGE  )  { 
throw   new   IOException  (  "No MESSAGE found where expected! Instead I found :  "  +  tc  )  ; 
} 
int   readparentmr  =  dis  .  readInt  (  )  ; 
if  (  readparentmr  !=  m  .  getInReplyTo  (  )  )  { 
throw   new   IOException  (  "Bad parent message id "  +  readparentmr  +  " != "  +  m  .  getInReplyTo  (  )  )  ; 
} 
newmesssageid  =  dis  .  readInt  (  )  ; 
if  (  newmesssageid  <  0  )  { 
throw   new   IOException  (  "Negative message id "  +  newmesssageid  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
dis  .  close  (  )  ; 
ioe  .  printStackTrace  (  )  ; 
return  (  false  )  ; 
}  finally  { 
dis  .  close  (  )  ; 
} 
addNewMessage  (  new   MessageReference  (  newmesssageid  )  ,  m  )  ; 
return  (  true  )  ; 
}  catch  (  IOException   ioe2  )  { 
ioe2  .  printStackTrace  (  )  ; 
return  (  false  )  ; 
} 
} 






public   void   run  (  )  { 
try  { 
URLConnection   con  =  connect  (  )  ; 
DataOutputStream   dos  =  new   DataOutputStream  (  con  .  getOutputStream  (  )  )  ; 
try  { 
sendStandardHeader  (  dos  )  ; 
dos  .  writeChar  (  GET  )  ; 
dos  .  writeChar  (  PAGE  )  ; 
dos  .  writeInt  (  mCurrentPage  )  ; 
dos  .  flush  (  )  ; 
}  catch  (  IOException   ioe  )  { 
dos  .  close  (  )  ; 
ioe  .  printStackTrace  (  )  ; 
return  ; 
} 
dos  .  flush  (  )  ; 
dos  .  close  (  )  ; 
dos  =  null  ; 
InputStream   is  =  con  .  getInputStream  (  )  ; 
DataInputStream   dis  =  new   DataInputStream  (  is  )  ; 
try  { 
readStandardHeader  (  dis  )  ; 
char   tc  ; 
if  (  (  tc  =  dis  .  readChar  (  )  )  !=  PUT  )  { 
throw   new   IOException  (  "No PUT found where expected! Instead I found : "  +  tc  )  ; 
} 
if  (  (  tc  =  dis  .  readChar  (  )  )  !=  PAGE  )  { 
throw   new   IOException  (  "No PAGE found where expected! Instead I found :  "  +  tc  )  ; 
} 
mCurrentPage  =  dis  .  readInt  (  )  ; 
try  { 
ObjectInputStream   ois  =  new   ObjectInputStream  (  is  )  ; 
int  [  ]  pageheader  =  (  int  [  ]  )  ois  .  readObject  (  )  ; 
receiveMessages  (  is  )  ; 
mTreeListener  .  gotTree  (  buildTree  (  mCurrentPage  ,  pageheader  )  )  ; 
}  catch  (  ClassNotFoundException   cnfe  )  { 
cnfe  .  printStackTrace  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
}  finally  { 
dis  .  close  (  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
mCurrentPage  =  -  1  ; 
} 






public   void   stop  (  )  { 
if  (  mLoadTreeThread  !=  null  )  { 
try  { 
mLoadTreeThread  .  join  (  )  ; 
}  catch  (  InterruptedException   ie  )  { 
ie  .  printStackTrace  (  )  ; 
} 
mLoadTreeThread  =  null  ; 
} 
} 






public   void   destroy  (  )  { 
stop  (  )  ; 
} 








private   void   addNewMessage  (  MessageReference   mr  ,  Message   m  )  { 
mMessageTable  .  put  (  mr  ,  m  )  ; 
if  (  m  .  getInReplyTo  (  )  <  0  )  { 
DefaultMutableTreeNode   root  =  (  DefaultMutableTreeNode  )  mTreeModel  .  getRoot  (  )  ; 
root  .  insert  (  getMessageNode  (  mr  )  ,  0  )  ; 
mTreeModel  .  nodeStructureChanged  (  root  )  ; 
return  ; 
} 
MessageReference   parentmr  =  new   MessageReference  (  m  .  getInReplyTo  (  )  )  ; 
MessageNode   parentmn  =  getMessageNode  (  parentmr  )  ; 
Message   parentmessage  =  (  Message  )  parentmn  .  getUserObject  (  )  ; 
if  (  !  parentmessage  .  contains  (  mr  )  )  { 
parentmessage  .  addReply  (  mr  )  ; 
mMessageTable  .  put  (  parentmn  ,  parentmessage  )  ; 
} 
parentmn  .  insert  (  getMessageNode  (  mr  )  ,  0  )  ; 
mTreeModel  .  nodeStructureChanged  (  parentmn  )  ; 
} 








private   URLConnection   connect  (  )  throws   IOException  { 
URLConnection   con  =  mForumURL  .  openConnection  (  )  ; 
con  .  setUseCaches  (  false  )  ; 
con  .  setRequestProperty  (  "CONTENT_TYPE"  ,  "application/octet-stream"  )  ; 
con  .  setDoInput  (  true  )  ; 
con  .  setDoOutput  (  true  )  ; 
return  (  con  )  ; 
} 








private   void   sendStandardHeader  (  DataOutputStream   dos  )  throws   IOException  { 
dos  .  writeInt  (  mCurrentMaxIndex  )  ; 
dos  .  writeInt  (  mForumFileString  .  length  (  )  )  ; 
for  (  int   k  =  0  ;  k  <  mForumFileString  .  length  (  )  ;  k  ++  )  { 
dos  .  writeChar  (  mForumFileString  .  charAt  (  k  )  )  ; 
} 
} 








private   void   readStandardHeader  (  DataInputStream   dis  )  throws   IOException  { 
int   CurrentMaxIndex  =  dis  .  readInt  (  )  ; 
if  (  CurrentMaxIndex  <  0  )  { 
throw   new   IOException  (  "No such forum!"  )  ; 
} 
if  (  CurrentMaxIndex  <  mCurrentMaxIndex  )  { 
launchUpdate  (  )  ; 
} 
final   int   length  =  dis  .  readInt  (  )  ; 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  int   k  =  0  ;  k  <  length  ;  k  ++  )  { 
sb  .  append  (  dis  .  readChar  (  )  )  ; 
} 
String   s  =  sb  .  toString  (  )  ; 
if  (  !  s  .  equals  (  mForumFileString  )  )  { 
throw   new   IOException  (  "Bad forum file : "  +  s  +  " != "  +  mForumFileString  )  ; 
} 
} 






private   void   launchUpdate  (  )  { 
System  .  out  .  println  (  "Normally, this should launch an update request in a new thread"  )  ; 
} 







private   void   receiveMessages  (  InputStream   is  )  { 
try  { 
DataInputStream   dis  =  new   DataInputStream  (  is  )  ; 
while  (  true  )  { 
char   tc  =  dis  .  readChar  (  )  ; 
if  (  (  tc  !=  PUT  )  &&  (  tc  !=  END  )  )  { 
throw   new   IOException  (  "No PUT or END found where expected! Instead I found : "  +  tc  )  ; 
} 
if  (  tc  ==  END  )  { 
System  .  out  .  println  (  "End of stream"  )  ; 
break  ; 
} 
if  (  (  tc  =  dis  .  readChar  (  )  )  !=  MESSAGE  )  { 
throw   new   IOException  (  "No MESSAGE found where expected! Instead I found :  "  +  tc  )  ; 
} 
try  { 
final   int   index  =  dis  .  readInt  (  )  ; 
try  { 
ObjectInputStream   ois  =  new   ObjectInputStream  (  is  )  ; 
Message   m  =  (  Message  )  ois  .  readObject  (  )  ; 
if  (  m  !=  null  )  { 
mMessageTable  .  put  (  new   MessageReference  (  index  )  ,  m  )  ; 
}  else  { 
System  .  out  .  println  (  "Got a null object! "  )  ; 
} 
}  catch  (  ClassNotFoundException   cnfe  )  { 
cnfe  .  printStackTrace  (  )  ; 
} 
}  catch  (  IOException   ioe1  )  { 
ioe1  .  printStackTrace  (  )  ; 
break  ; 
} 
} 
}  catch  (  IOException   ioe2  )  { 
ioe2  .  printStackTrace  (  )  ; 
} 
} 









private   TreeModel   buildTree  (  final   int   pagenumber  ,  int  [  ]  pageheader  )  { 
PageNode   pn  =  new   PageNode  (  pagenumber  ,  pageheader  ,  this  )  ; 
pn  .  explore  (  )  ; 
return  (  mTreeModel  =  new   DefaultTreeModel  (  pn  )  )  ; 
} 
} 







class   MessageFormattingRenderer   extends   DefaultTreeCellRenderer   implements   AutorizationConstants  { 

private   Font   mUnreadFont  =  new   Font  (  "Serif"  ,  Font  .  PLAIN  ,  12  )  ; 

private   Font   mReadFont  =  new   Font  (  "Serif"  ,  Font  .  ITALIC  ,  12  )  ; 

private   DateFormat   mDateFormat  ; 

private   Color   mTextColor  ,  mSelectedTextColor  ,  mBrightTextColor  ,  mBrightSelectedTextColor  ; 







public   MessageFormattingRenderer  (  DateFormat   df  )  { 
super  (  )  ; 
mDateFormat  =  df  ; 
mTextColor  =  this  .  getTextNonSelectionColor  (  )  ; 
mSelectedTextColor  =  this  .  getTextSelectionColor  (  )  ; 
mBrightTextColor  =  mTextColor  .  brighter  (  )  ; 
mBrightSelectedTextColor  =  mSelectedTextColor  .  brighter  (  )  ; 
} 















public   Component   getTreeCellRendererComponent  (  JTree   tree  ,  Object   value  ,  boolean   selected  ,  boolean   expanded  ,  boolean   leaf  ,  int   row  ,  boolean   hasFocus  )  { 
super  .  getTreeCellRendererComponent  (  tree  ,  value  ,  selected  ,  expanded  ,  leaf  ,  row  ,  hasFocus  )  ; 
DefaultMutableTreeNode   n  =  (  DefaultMutableTreeNode  )  value  ; 
Object   userObject  =  n  .  getUserObject  (  )  ; 
if  (  userObject   instanceof   Message  )  { 
Message   m  =  (  Message  )  userObject  ; 
setText  (  m  .  toString  (  )  )  ; 
if  (  n   instanceof   MessageNode  )  { 
MessageNode   mn  =  (  MessageNode  )  n  ; 
if  (  mn  .  hasBeenRead  (  )  )  { 
setFont  (  mReadFont  )  ; 
}  else  { 
setFont  (  mUnreadFont  )  ; 
} 
if  (  m  .  getAutorizationLevel  (  )  >  MEMBER  )  { 
this  .  setForeground  (  Color  .  red  )  ; 
}  else   if  (  m  .  getAutorizationLevel  (  )  >  NORMAL  )  { 
this  .  setForeground  (  Color  .  blue  )  ; 
} 
} 
} 
return  (  this  )  ; 
} 
} 







final   class   MessageNode   extends   DefaultMutableTreeNode  { 

private   MessageHolder   mMessageHolder  ; 

private   MessageReference   mMessageReference  ; 

private   boolean   mHasBeenRead  =  false  ; 









public   MessageNode  (  MessageReference   mr  ,  Message   m  ,  MessageHolder   mh  )  { 
mMessageHolder  =  mh  ; 
mMessageReference  =  mr  ; 
setUserObject  (  m  )  ; 
explore  (  )  ; 
} 







public   void   setHasBeenRead  (  boolean   b  )  { 
mHasBeenRead  =  b  ; 
} 







public   MessageReference   getMessageReference  (  )  { 
return  (  mMessageReference  )  ; 
} 







public   boolean   isLeaf  (  )  { 
Message   m  =  (  Message  )  getUserObject  (  )  ; 
return  (  !  m  .  getReplies  (  )  .  hasMoreElements  (  )  )  ; 
} 







public   boolean   getAllowsChildren  (  )  { 
return  (  true  )  ; 
} 







public   String   toString  (  )  { 
return  (  getUserObject  (  )  .  toString  (  )  )  ; 
} 







public   boolean   hasBeenRead  (  )  { 
return  (  mHasBeenRead  )  ; 
} 






public   void   explore  (  )  { 
Message   m  =  (  Message  )  getUserObject  (  )  ; 
Enumeration   enume  =  m  .  getReplies  (  )  ; 
while  (  enume  .  hasMoreElements  (  )  )  { 
MessageReference   mr  =  (  MessageReference  )  enume  .  nextElement  (  )  ; 
MessageNode   mn  =  mMessageHolder  .  getMessageNode  (  mr  )  ; 
if  (  mn  !=  null  )  { 
add  (  mn  )  ; 
}  else  { 
} 
} 
} 
} 







final   class   PageNode   extends   DefaultMutableTreeNode  { 

private   int   mPageNumber  ; 

private   MessageHolder   mMessageHolder  ; 

private   boolean   mIsExplored  ; 









public   PageNode  (  final   int   PageNumber  ,  int  [  ]  Headers  ,  MessageHolder   mh  )  { 
mMessageHolder  =  mh  ; 
mPageNumber  =  PageNumber  ; 
setUserObject  (  Headers  )  ; 
} 







public   boolean   isLeaf  (  )  { 
int  [  ]  array  =  (  int  [  ]  )  getUserObject  (  )  ; 
if  (  array  ==  null  )  { 
return  (  true  )  ; 
} 
return  (  array  .  length  ==  0  )  ; 
} 







public   boolean   getAllowsChildren  (  )  { 
return  (  true  )  ; 
} 







public   boolean   isExplored  (  )  { 
return  (  mIsExplored  )  ; 
} 







public   String   toString  (  )  { 
return  (  "page"  )  ; 
} 






public   void   explore  (  )  { 
int  [  ]  array  =  (  int  [  ]  )  getUserObject  (  )  ; 
if  (  array  ==  null  )  { 
return  ; 
} 
for  (  int   k  =  0  ;  k  <  array  .  length  ;  k  ++  )  { 
MessageNode   mn  =  mMessageHolder  .  getMessageNode  (  new   MessageReference  (  array  [  k  ]  )  )  ; 
if  (  mn  !=  null  )  { 
add  (  mn  )  ; 
}  else  { 
} 
} 
mIsExplored  =  true  ; 
} 
} 







final   class   AppletLocale  { 

private   Locale   mCurrentLocale  ; 

private   DateFormat   mDateFormat  ; 







private   AppletLocale  (  String   ConfigurationFile  )  { 
mCurrentLocale  =  new   Locale  (  "en"  ,  "US"  ,  ""  )  ; 
mDateFormat  =  DateFormat  .  getDateTimeInstance  (  DateFormat  .  SHORT  ,  DateFormat  .  SHORT  ,  mCurrentLocale  )  ; 
} 







public   DateFormat   getDateFormat  (  )  { 
return  (  mDateFormat  )  ; 
} 







public   String   getImageText  (  )  { 
return  (  "image"  )  ; 
} 







public   String   getPostButtonText  (  )  { 
return  (  "post"  )  ; 
} 







public   String   getCancelButtonText  (  )  { 
return  (  "cancel"  )  ; 
} 







public   String   getAuthorText  (  )  { 
return  (  "author"  )  ; 
} 







public   String   getAuthorEmailText  (  )  { 
return  (  "email"  )  ; 
} 







public   String   getURLText  (  )  { 
return  (  "URL"  )  ; 
} 







public   String   getURLTitleText  (  )  { 
return  (  "URL title"  )  ; 
} 







public   String   getSubjectText  (  )  { 
return  (  "subject"  )  ; 
} 







public   String   getOKButtonText  (  )  { 
return  (  "Ok"  )  ; 
} 







public   String   getMessageText  (  )  { 
return  (  "Message"  )  ; 
} 







public   String   getPostANewMessageText  (  )  { 
return  (  "Post a new message"  )  ; 
} 







public   String   getPostANewReplyText  (  )  { 
return  (  "Post a new reply"  )  ; 
} 







public   String   getExpandText  (  )  { 
return  (  "Expand"  )  ; 
} 







public   String   getLoadingText  (  )  { 
return  (  "Loading... please wait..."  )  ; 
} 
} 







final   class   MessageExplorer   implements   TreeExpansionListener  { 

private   DefaultTreeModel   mTreeModel  ; 







public   MessageExplorer  (  DefaultTreeModel   tm  )  { 
mTreeModel  =  tm  ; 
} 







public   void   treeCollapsed  (  TreeExpansionEvent   e  )  { 
} 







public   void   treeExpanded  (  TreeExpansionEvent   e  )  { 
Object   o  =  e  .  getPath  (  )  .  getLastPathComponent  (  )  ; 
if  (  o   instanceof   PageNode  )  { 
PageNode   pn  =  (  PageNode  )  o  ; 
if  (  !  pn  .  isExplored  (  )  )  { 
pn  .  explore  (  )  ; 
mTreeModel  .  nodeStructureChanged  (  pn  )  ; 
} 
} 
} 
} 







class   NoImageHTMLEditorKit   extends   HTMLEditorKit  { 






public   NoImageHTMLEditorKit  (  )  { 
super  (  )  ; 
} 







public   ViewFactory   getViewFactory  (  )  { 
return   new   NoImageHTMLFactory  (  )  ; 
} 
} 
} 

