package   com  .  flagstone  .  transform  ; 

import   java  .  util  .  *  ; 

































































































































































































public   class   FSDefineTextField   extends   FSDefineObject  { 


public   static   final   String   WordWrapped  =  "WordWrapped"  ; 


public   static   final   String   Multiline  =  "Multiline"  ; 


public   static   final   String   Password  =  "Password"  ; 


public   static   final   String   ReadOnly  =  "ReadOnly"  ; 


public   static   final   String   Selectable  =  "Selectable"  ; 


public   static   final   String   Bordered  =  "Bordered"  ; 


public   static   final   String   HTML  =  "HTML"  ; 


public   static   final   String   UseFontGlyphs  =  "UseFontGlyphs"  ; 


public   static   final   String   AutoSize  =  "AutoSize"  ; 


public   static   final   String   FontIdentifier  =  "FontIdentifier"  ; 


public   static   final   String   FontHeight  =  "FontHeight"  ; 


public   static   final   String   Color  =  "Color"  ; 


public   static   final   String   MaxLength  =  "MaxLength"  ; 


public   static   final   String   LeftMargin  =  "LeftMargin"  ; 


public   static   final   String   RightMargin  =  "RightMargin"  ; 


public   static   final   String   Indent  =  "Indent"  ; 


public   static   final   String   Leading  =  "Leading"  ; 


public   static   final   String   VariableName  =  "VariableName"  ; 


public   static   final   String   InitialText  =  "InitialText"  ; 


public   static   final   int   AlignLeft  =  0  ; 


public   static   final   int   AlignRight  =  1  ; 


public   static   final   int   AlignCenter  =  2  ; 


public   static   final   int   AlignJustify  =  3  ; 

private   FSBounds   bounds  =  null  ; 

private   boolean   wordWrapped  =  false  ; 

private   boolean   multiline  =  false  ; 

private   boolean   password  =  false  ; 

private   boolean   readOnly  =  false  ; 

private   int   reserved1  =  0  ; 

private   boolean   selectable  =  false  ; 

private   boolean   bordered  =  false  ; 

private   boolean   reserved2  =  false  ; 

private   boolean   html  =  false  ; 

private   boolean   useFontGlyphs  =  false  ; 

private   boolean   autoSize  =  false  ; 

private   int   fontIdentifier  =  0  ; 

private   int   fontHeight  =  0  ; 

private   FSColor   color  =  null  ; 

private   int   maxLength  =  0  ; 

private   int   alignment  =  Transform  .  VALUE_NOT_SET  ; 

private   int   leftMargin  =  Transform  .  VALUE_NOT_SET  ; 

private   int   rightMargin  =  Transform  .  VALUE_NOT_SET  ; 

private   int   indent  =  Transform  .  VALUE_NOT_SET  ; 

private   int   leading  =  Transform  .  VALUE_NOT_SET  ; 

private   String   variableName  =  ""  ; 

private   String   initialText  =  ""  ; 







public   FSDefineTextField  (  FSCoder   coder  )  { 
super  (  DefineTextField  ,  0  )  ; 
decode  (  coder  )  ; 
} 






public   FSDefineTextField  (  int   anIdentifier  ,  FSBounds   aBounds  )  { 
super  (  DefineTextField  ,  anIdentifier  )  ; 
setBounds  (  aBounds  )  ; 
} 







public   FSDefineTextField  (  int   anIdentifier  ,  FSBounds   aBounds  ,  Hashtable   attributes  )  { 
super  (  DefineTextField  ,  anIdentifier  )  ; 
setBounds  (  aBounds  )  ; 
setAttributes  (  attributes  )  ; 
} 







public   FSDefineTextField  (  FSDefineTextField   obj  )  { 
super  (  obj  )  ; 
bounds  =  new   FSBounds  (  obj  .  bounds  )  ; 
wordWrapped  =  obj  .  wordWrapped  ; 
multiline  =  obj  .  multiline  ; 
password  =  obj  .  password  ; 
readOnly  =  obj  .  readOnly  ; 
reserved1  =  obj  .  reserved1  ; 
selectable  =  obj  .  selectable  ; 
bordered  =  obj  .  bordered  ; 
reserved2  =  obj  .  reserved2  ; 
html  =  obj  .  html  ; 
useFontGlyphs  =  obj  .  useFontGlyphs  ; 
autoSize  =  obj  .  autoSize  ; 
fontIdentifier  =  obj  .  fontIdentifier  ; 
fontHeight  =  obj  .  fontHeight  ; 
color  =  new   FSColor  (  obj  .  color  )  ; 
maxLength  =  obj  .  maxLength  ; 
alignment  =  obj  .  alignment  ; 
leftMargin  =  obj  .  leftMargin  ; 
rightMargin  =  obj  .  rightMargin  ; 
indent  =  obj  .  indent  ; 
leading  =  obj  .  leading  ; 
variableName  =  new   String  (  obj  .  variableName  )  ; 
initialText  =  new   String  (  obj  .  initialText  )  ; 
} 





public   FSBounds   getBounds  (  )  { 
return   bounds  ; 
} 





public   boolean   isWordWrapped  (  )  { 
return   wordWrapped  ; 
} 





public   boolean   isMultiline  (  )  { 
return   multiline  ; 
} 





public   boolean   isPassword  (  )  { 
return   password  ; 
} 





public   boolean   isReadOnly  (  )  { 
return   readOnly  ; 
} 





public   boolean   isSelectable  (  )  { 
return   selectable  ; 
} 





public   boolean   isBordered  (  )  { 
return   bordered  ; 
} 





public   boolean   isHTML  (  )  { 
return   html  ; 
} 





public   boolean   isAutoSize  (  )  { 
return   autoSize  ; 
} 





public   void   setAutoSize  (  boolean   aFlag  )  { 
autoSize  =  aFlag  ; 
} 









public   boolean   useFontGlyphs  (  )  { 
return   useFontGlyphs  ; 
} 





public   int   getFontIdentifier  (  )  { 
return   fontIdentifier  ; 
} 





public   int   getFontHeight  (  )  { 
return   fontHeight  ; 
} 





public   FSColor   getColor  (  )  { 
return   color  ; 
} 





public   int   getMaxLength  (  )  { 
return   maxLength  ; 
} 





public   int   getAlignment  (  )  { 
return   alignment  ; 
} 





public   int   getLeftMargin  (  )  { 
return   leftMargin  ; 
} 





public   int   getRightMargin  (  )  { 
return   rightMargin  ; 
} 





public   int   getIndent  (  )  { 
return   indent  ; 
} 





public   int   getLeading  (  )  { 
return   leading  ; 
} 





public   String   getVariableName  (  )  { 
return   variableName  ; 
} 





public   String   getInitialText  (  )  { 
return   initialText  ; 
} 





public   Hashtable   getAttributes  (  )  { 
Hashtable   attributes  =  new   Hashtable  (  )  ; 
attributes  .  put  (  WordWrapped  ,  new   Boolean  (  isWordWrapped  (  )  )  )  ; 
attributes  .  put  (  Multiline  ,  new   Boolean  (  isMultiline  (  )  )  )  ; 
attributes  .  put  (  Password  ,  new   Boolean  (  isPassword  (  )  )  )  ; 
attributes  .  put  (  ReadOnly  ,  new   Boolean  (  isReadOnly  (  )  )  )  ; 
attributes  .  put  (  AutoSize  ,  new   Boolean  (  isAutoSize  (  )  )  )  ; 
attributes  .  put  (  Selectable  ,  new   Boolean  (  isSelectable  (  )  )  )  ; 
attributes  .  put  (  Bordered  ,  new   Boolean  (  isBordered  (  )  )  )  ; 
attributes  .  put  (  HTML  ,  new   Boolean  (  isHTML  (  )  )  )  ; 
attributes  .  put  (  UseFontGlyphs  ,  new   Boolean  (  useFontGlyphs  (  )  )  )  ; 
attributes  .  put  (  FontIdentifier  ,  new   Integer  (  getFontIdentifier  (  )  )  )  ; 
attributes  .  put  (  FontHeight  ,  new   Integer  (  getFontHeight  (  )  )  )  ; 
attributes  .  put  (  Color  ,  getColor  (  )  )  ; 
attributes  .  put  (  MaxLength  ,  new   Integer  (  getMaxLength  (  )  )  )  ; 
attributes  .  put  (  LeftMargin  ,  new   Integer  (  getLeftMargin  (  )  )  )  ; 
attributes  .  put  (  RightMargin  ,  new   Integer  (  getRightMargin  (  )  )  )  ; 
attributes  .  put  (  Indent  ,  new   Integer  (  getIndent  (  )  )  )  ; 
attributes  .  put  (  Leading  ,  new   Integer  (  getLeading  (  )  )  )  ; 
attributes  .  put  (  VariableName  ,  getVariableName  (  )  )  ; 
attributes  .  put  (  InitialText  ,  getInitialText  (  )  )  ; 
return   attributes  ; 
} 





public   void   setBounds  (  FSBounds   aBounds  )  { 
bounds  =  aBounds  ; 
} 





public   void   setWordWrapped  (  boolean   aFlag  )  { 
wordWrapped  =  aFlag  ; 
} 





public   void   setMultiline  (  boolean   aFlag  )  { 
multiline  =  aFlag  ; 
} 





public   void   setPassword  (  boolean   aFlag  )  { 
password  =  aFlag  ; 
} 





public   void   setReadOnly  (  boolean   aFlag  )  { 
readOnly  =  aFlag  ; 
} 





public   void   setSelectable  (  boolean   aFlag  )  { 
selectable  =  !  aFlag  ; 
} 





public   void   setBordered  (  boolean   aFlag  )  { 
bordered  =  aFlag  ; 
} 





public   void   setHTML  (  boolean   aFlag  )  { 
html  =  aFlag  ; 
} 









public   void   setUseFontGlyphs  (  boolean   aFlag  )  { 
useFontGlyphs  =  aFlag  ; 
} 





public   void   setFontIdentifier  (  int   anIdentifier  )  { 
fontIdentifier  =  anIdentifier  ; 
} 





public   void   setFontHeight  (  int   aNumber  )  { 
fontHeight  =  aNumber  ; 
} 





public   void   setColor  (  FSColor   aColor  )  { 
color  =  aColor  ; 
} 





public   void   setMaxLength  (  int   aNumber  )  { 
maxLength  =  aNumber  ; 
} 





public   void   setAlignment  (  int   aType  )  { 
alignment  =  aType  ; 
} 





public   void   setLeftMargin  (  int   aNumber  )  { 
leftMargin  =  aNumber  ; 
} 





public   void   setRightMargin  (  int   aNumber  )  { 
rightMargin  =  aNumber  ; 
} 





public   void   setIndent  (  int   aNumber  )  { 
indent  =  aNumber  ; 
} 





public   void   setLeading  (  int   aNumber  )  { 
leading  =  aNumber  ; 
} 





public   void   setVariableName  (  String   aString  )  { 
variableName  =  aString  ; 
} 





public   void   setInitialText  (  String   aString  )  { 
initialText  =  aString  ; 
} 





public   void   setAttributes  (  Hashtable   attributes  )  { 
if  (  attributes  .  get  (  WordWrapped  )  !=  null  )  setWordWrapped  (  (  (  Boolean  )  attributes  .  get  (  WordWrapped  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  Multiline  )  !=  null  )  setMultiline  (  (  (  Boolean  )  attributes  .  get  (  Multiline  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  Password  )  !=  null  )  setPassword  (  (  (  Boolean  )  attributes  .  get  (  Password  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  ReadOnly  )  !=  null  )  setReadOnly  (  (  (  Boolean  )  attributes  .  get  (  ReadOnly  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  Selectable  )  !=  null  )  setSelectable  (  (  (  Boolean  )  attributes  .  get  (  Selectable  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  Bordered  )  !=  null  )  setBordered  (  (  (  Boolean  )  attributes  .  get  (  Bordered  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  HTML  )  !=  null  )  setHTML  (  (  (  Boolean  )  attributes  .  get  (  HTML  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  AutoSize  )  !=  null  )  setAutoSize  (  (  (  Boolean  )  attributes  .  get  (  AutoSize  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  UseFontGlyphs  )  !=  null  )  setUseFontGlyphs  (  (  (  Boolean  )  attributes  .  get  (  UseFontGlyphs  )  )  .  booleanValue  (  )  )  ; 
if  (  attributes  .  get  (  FontIdentifier  )  !=  null  )  setFontIdentifier  (  (  (  Integer  )  attributes  .  get  (  FontIdentifier  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  FontHeight  )  !=  null  )  setFontHeight  (  (  (  Integer  )  attributes  .  get  (  FontHeight  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  Color  )  !=  null  )  setColor  (  (  FSColor  )  attributes  .  get  (  Color  )  )  ; 
if  (  attributes  .  get  (  MaxLength  )  !=  null  )  setMaxLength  (  (  (  Integer  )  attributes  .  get  (  MaxLength  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  LeftMargin  )  !=  null  )  setLeftMargin  (  (  (  Integer  )  attributes  .  get  (  LeftMargin  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  RightMargin  )  !=  null  )  setRightMargin  (  (  (  Integer  )  attributes  .  get  (  RightMargin  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  Indent  )  !=  null  )  setIndent  (  (  (  Integer  )  attributes  .  get  (  Indent  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  Leading  )  !=  null  )  setLeading  (  (  (  Integer  )  attributes  .  get  (  Leading  )  )  .  intValue  (  )  )  ; 
if  (  attributes  .  get  (  VariableName  )  !=  null  )  setVariableName  (  (  String  )  attributes  .  get  (  VariableName  )  )  ; 
if  (  attributes  .  get  (  InitialText  )  !=  null  )  setInitialText  (  (  String  )  attributes  .  get  (  InitialText  )  )  ; 
} 

public   Object   clone  (  )  { 
FSDefineTextField   anObject  =  (  FSDefineTextField  )  super  .  clone  (  )  ; 
anObject  .  bounds  =  (  bounds  !=  null  )  ?  (  FSBounds  )  bounds  .  clone  (  )  :  null  ; 
anObject  .  color  =  (  color  !=  null  )  ?  (  FSColor  )  color  .  clone  (  )  :  null  ; 
return   anObject  ; 
} 

public   boolean   equals  (  Object   anObject  )  { 
boolean   result  =  false  ; 
if  (  super  .  equals  (  anObject  )  )  { 
FSDefineTextField   typedObject  =  (  FSDefineTextField  )  anObject  ; 
if  (  bounds  !=  null  )  result  =  bounds  .  equals  (  typedObject  .  bounds  )  ;  else   result  =  bounds  ==  typedObject  .  bounds  ; 
result  =  result  &&  wordWrapped  ==  typedObject  .  wordWrapped  ; 
result  =  result  &&  multiline  ==  typedObject  .  multiline  ; 
result  =  result  &&  password  ==  typedObject  .  password  ; 
result  =  result  &&  readOnly  ==  typedObject  .  readOnly  ; 
result  =  result  &&  reserved1  ==  typedObject  .  reserved1  ; 
result  =  result  &&  autoSize  ==  typedObject  .  autoSize  ; 
result  =  result  &&  selectable  ==  typedObject  .  selectable  ; 
result  =  result  &&  bordered  ==  typedObject  .  bordered  ; 
result  =  result  &&  reserved2  ==  typedObject  .  reserved2  ; 
result  =  result  &&  html  ==  typedObject  .  html  ; 
result  =  result  &&  useFontGlyphs  ==  typedObject  .  useFontGlyphs  ; 
result  =  result  &&  fontIdentifier  ==  typedObject  .  fontIdentifier  ; 
result  =  result  &&  fontHeight  ==  typedObject  .  fontHeight  ; 
if  (  color  !=  null  )  result  =  result  &&  color  .  equals  (  typedObject  .  color  )  ;  else   result  =  result  &&  color  ==  typedObject  .  color  ; 
result  =  result  &&  maxLength  ==  typedObject  .  maxLength  ; 
if  (  containsLayoutInfo  (  )  )  { 
result  =  result  &&  alignment  ==  typedObject  .  alignment  ; 
result  =  result  &&  leftMargin  ==  typedObject  .  leftMargin  ; 
result  =  result  &&  rightMargin  ==  typedObject  .  rightMargin  ; 
result  =  result  &&  indent  ==  typedObject  .  indent  ; 
result  =  result  &&  leading  ==  typedObject  .  leading  ; 
} 
if  (  variableName  !=  null  )  result  =  result  &&  variableName  .  equals  (  typedObject  .  variableName  )  ;  else   result  =  result  &&  variableName  ==  typedObject  .  variableName  ; 
if  (  initialText  !=  null  )  result  =  result  &&  initialText  .  equals  (  typedObject  .  initialText  )  ;  else   result  =  result  &&  initialText  ==  typedObject  .  initialText  ; 
} 
return   result  ; 
} 

public   void   appendDescription  (  StringBuffer   buffer  ,  int   depth  )  { 
buffer  .  append  (  name  (  )  )  ; 
if  (  depth  >  0  )  { 
buffer  .  append  (  ": { "  )  ; 
Transform  .  append  (  buffer  ,  "bounds"  ,  bounds  ,  depth  )  ; 
Transform  .  append  (  buffer  ,  "wordWrapped"  ,  wordWrapped  )  ; 
Transform  .  append  (  buffer  ,  "multiline"  ,  multiline  )  ; 
Transform  .  append  (  buffer  ,  "password"  ,  password  )  ; 
Transform  .  append  (  buffer  ,  "readOnly"  ,  readOnly  )  ; 
Transform  .  append  (  buffer  ,  "autoSize"  ,  autoSize  )  ; 
Transform  .  append  (  buffer  ,  "selectable"  ,  selectable  )  ; 
Transform  .  append  (  buffer  ,  "bordered"  ,  bordered  )  ; 
Transform  .  append  (  buffer  ,  "HTML"  ,  html  )  ; 
Transform  .  append  (  buffer  ,  "useFontGlyphs"  ,  useFontGlyphs  )  ; 
Transform  .  append  (  buffer  ,  "fontIdentifier"  ,  fontIdentifier  )  ; 
Transform  .  append  (  buffer  ,  "fontHeight"  ,  fontHeight  )  ; 
Transform  .  append  (  buffer  ,  "color"  ,  color  ,  depth  )  ; 
Transform  .  append  (  buffer  ,  "maxLength"  ,  maxLength  )  ; 
Transform  .  append  (  buffer  ,  "alignment"  ,  alignment  )  ; 
Transform  .  append  (  buffer  ,  "leftMargin"  ,  leftMargin  )  ; 
Transform  .  append  (  buffer  ,  "rightMargin"  ,  rightMargin  )  ; 
Transform  .  append  (  buffer  ,  "indent"  ,  indent  )  ; 
Transform  .  append  (  buffer  ,  "leading"  ,  leading  )  ; 
Transform  .  append  (  buffer  ,  "variableName"  ,  variableName  )  ; 
Transform  .  append  (  buffer  ,  "initalText"  ,  initialText  )  ; 
buffer  .  append  (  "}"  )  ; 
} 
} 

public   int   length  (  FSCoder   coder  )  { 
boolean   _containsFont  =  containsFont  (  )  ; 
boolean   _containsColor  =  containsColor  (  )  ; 
boolean   _containsMaxLength  =  containsMaxLength  (  )  ; 
boolean   _containsText  =  containsText  (  )  ; 
super  .  length  (  coder  )  ; 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  1  ; 
length  +=  bounds  .  length  (  coder  )  ; 
length  +=  2  ; 
length  +=  (  _containsFont  )  ?  4  :  0  ; 
length  +=  (  _containsColor  )  ?  color  .  length  (  coder  )  :  0  ; 
length  +=  (  _containsMaxLength  )  ?  2  :  0  ; 
length  +=  (  containsLayoutInfo  (  )  )  ?  9  :  0  ; 
length  +=  coder  .  strlen  (  variableName  ,  true  )  ; 
length  +=  (  _containsText  )  ?  coder  .  strlen  (  initialText  ,  true  )  :  0  ; 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  0  ; 
return   length  ; 
} 

public   void   encode  (  FSCoder   coder  )  { 
boolean   _containsFont  =  containsFont  (  )  ; 
boolean   _containsColor  =  containsColor  (  )  ; 
boolean   _containsMaxLength  =  containsMaxLength  (  )  ; 
boolean   _containsText  =  containsText  (  )  ; 
super  .  encode  (  coder  )  ; 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  1  ; 
bounds  .  encode  (  coder  )  ; 
coder  .  writeBits  (  _containsText  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  wordWrapped  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  multiline  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  password  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  readOnly  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  _containsColor  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  _containsMaxLength  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  _containsFont  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  0  ,  1  )  ; 
coder  .  writeBits  (  autoSize  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  containsLayoutInfo  (  )  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  selectable  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  bordered  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  0  ,  1  )  ; 
coder  .  writeBits  (  html  ?  1  :  0  ,  1  )  ; 
coder  .  writeBits  (  useFontGlyphs  ?  1  :  0  ,  1  )  ; 
if  (  _containsFont  )  { 
coder  .  writeWord  (  fontIdentifier  ,  2  )  ; 
coder  .  writeWord  (  fontHeight  ,  2  )  ; 
} 
if  (  _containsColor  )  color  .  encode  (  coder  )  ; 
if  (  _containsMaxLength  )  coder  .  writeWord  (  maxLength  ,  2  )  ; 
if  (  containsLayoutInfo  (  )  )  { 
coder  .  writeWord  (  (  alignment  !=  Transform  .  VALUE_NOT_SET  )  ?  alignment  :  0  ,  1  )  ; 
coder  .  writeWord  (  (  leftMargin  !=  Transform  .  VALUE_NOT_SET  )  ?  leftMargin  :  0  ,  2  )  ; 
coder  .  writeWord  (  (  rightMargin  !=  Transform  .  VALUE_NOT_SET  )  ?  rightMargin  :  0  ,  2  )  ; 
coder  .  writeWord  (  (  indent  !=  Transform  .  VALUE_NOT_SET  )  ?  indent  :  0  ,  2  )  ; 
coder  .  writeWord  (  (  leading  !=  Transform  .  VALUE_NOT_SET  )  ?  leading  :  0  ,  2  )  ; 
} 
coder  .  writeString  (  variableName  )  ; 
coder  .  writeWord  (  0  ,  1  )  ; 
if  (  _containsText  )  { 
coder  .  writeString  (  initialText  )  ; 
coder  .  writeWord  (  0  ,  1  )  ; 
} 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  0  ; 
coder  .  endObject  (  name  (  )  )  ; 
} 

public   void   decode  (  FSCoder   coder  )  { 
boolean   _containsFont  =  false  ; 
boolean   _containsColor  =  false  ; 
boolean   _containsMaxLength  =  false  ; 
boolean   _containsText  =  false  ; 
boolean   _containsLayout  =  false  ; 
super  .  decode  (  coder  )  ; 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  1  ; 
bounds  =  new   FSBounds  (  coder  )  ; 
_containsText  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
wordWrapped  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
multiline  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
password  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
readOnly  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
_containsColor  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
_containsMaxLength  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
_containsFont  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
reserved1  =  coder  .  readBits  (  1  ,  false  )  ; 
autoSize  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
_containsLayout  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
selectable  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
bordered  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
reserved2  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
html  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
useFontGlyphs  =  coder  .  readBits  (  1  ,  false  )  !=  0  ?  true  :  false  ; 
if  (  _containsFont  )  { 
fontIdentifier  =  coder  .  readWord  (  2  ,  false  )  ; 
fontHeight  =  coder  .  readWord  (  2  ,  false  )  ; 
} 
if  (  _containsColor  )  color  =  new   FSColor  (  coder  )  ; 
if  (  _containsMaxLength  )  maxLength  =  coder  .  readWord  (  2  ,  false  )  ; 
if  (  _containsLayout  )  { 
alignment  =  coder  .  readWord  (  1  ,  false  )  ; 
leftMargin  =  coder  .  readWord  (  2  ,  false  )  ; 
rightMargin  =  coder  .  readWord  (  2  ,  false  )  ; 
indent  =  coder  .  readWord  (  2  ,  false  )  ; 
leading  =  coder  .  readWord  (  2  ,  true  )  ; 
} 
variableName  =  coder  .  readString  (  )  ; 
if  (  _containsText  )  initialText  =  coder  .  readString  (  )  ; 
coder  .  context  [  FSCoder  .  TransparentColors  ]  =  0  ; 
coder  .  endObject  (  name  (  )  )  ; 
} 

private   boolean   containsColor  (  )  { 
return   color  !=  null  ; 
} 

private   boolean   containsFont  (  )  { 
return   fontIdentifier  !=  0  &&  fontHeight  !=  0  ; 
} 

private   boolean   containsMaxLength  (  )  { 
return   maxLength  >  0  ; 
} 

private   boolean   containsLayoutInfo  (  )  { 
boolean   layout  =  false  ; 
layout  =  alignment  !=  Transform  .  VALUE_NOT_SET  ; 
layout  =  layout  ||  leftMargin  !=  Transform  .  VALUE_NOT_SET  ; 
layout  =  layout  ||  rightMargin  !=  Transform  .  VALUE_NOT_SET  ; 
layout  =  layout  ||  indent  !=  Transform  .  VALUE_NOT_SET  ; 
layout  =  layout  ||  leading  !=  Transform  .  VALUE_NOT_SET  ; 
return   layout  ; 
} 

private   boolean   containsText  (  )  { 
return   initialText  !=  null  &&  initialText  .  length  (  )  >  0  ; 
} 
} 

