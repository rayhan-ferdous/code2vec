package   ag  .  ion  .  bion  .  officelayer  .  internal  .  text  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   ag  .  ion  .  bion  .  officelayer  .  filter  .  IFilter  ; 
import   ag  .  ion  .  bion  .  officelayer  .  internal  .  document  .  ByteArrayXInputStreamAdapter  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  ICharacterProperties  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  IPageCursor  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  ITextCursor  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  ITextDocument  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  ITextRange  ; 
import   ag  .  ion  .  bion  .  officelayer  .  text  .  IViewCursor  ; 
import   ag  .  ion  .  noa  .  NOAException  ; 
import   ag  .  ion  .  noa  .  document  .  URLAdapter  ; 
import   com  .  sun  .  star  .  beans  .  PropertyValue  ; 
import   com  .  sun  .  star  .  beans  .  XPropertySet  ; 
import   com  .  sun  .  star  .  document  .  XDocumentInsertable  ; 
import   com  .  sun  .  star  .  style  .  BreakType  ; 
import   com  .  sun  .  star  .  table  .  XCell  ; 
import   com  .  sun  .  star  .  text  .  ControlCharacter  ; 
import   com  .  sun  .  star  .  text  .  XParagraphCursor  ; 
import   com  .  sun  .  star  .  text  .  XSentenceCursor  ; 
import   com  .  sun  .  star  .  text  .  XTextCursor  ; 
import   com  .  sun  .  star  .  text  .  XWordCursor  ; 
import   com  .  sun  .  star  .  uno  .  UnoRuntime  ; 









public   class   TextCursor   implements   ITextCursor  { 

private   ITextDocument   textDocument  =  null  ; 

private   XTextCursor   xTextCursor  =  null  ; 

private   XWordCursor   xWordCursor  =  null  ; 

private   XSentenceCursor   xSentenceCursor  =  null  ; 

private   XParagraphCursor   xParagraphCursor  =  null  ; 















public   TextCursor  (  ITextDocument   textDocument  ,  XTextCursor   xTextCursor  )  { 
if  (  xTextCursor  ==  null  )  throw   new   IllegalArgumentException  (  "The submitted OpenOffice.org XTextCursor interface is not valid."  )  ; 
this  .  xTextCursor  =  xTextCursor  ; 
if  (  textDocument  ==  null  )  throw   new   IllegalArgumentException  (  "The submitted text document is not valid."  )  ; 
this  .  textDocument  =  textDocument  ; 
} 








public   ICharacterProperties   getCharacterProperties  (  )  { 
XPropertySet   xPropertySet  =  (  XPropertySet  )  UnoRuntime  .  queryInterface  (  XPropertySet  .  class  ,  xTextCursor  )  ; 
return   new   CharacterProperties  (  xPropertySet  )  ; 
} 










public   void   gotoEnd  (  boolean   mark  )  { 
xTextCursor  .  gotoEnd  (  mark  )  ; 
} 










public   void   gotoStart  (  boolean   mark  )  { 
xTextCursor  .  gotoStart  (  mark  )  ; 
} 












public   void   gotoRange  (  ITextRange   range  ,  boolean   mark  )  { 
xTextCursor  .  gotoRange  (  range  .  getXTextRange  (  )  ,  mark  )  ; 
} 









public   void   setString  (  String   content  )  { 
if  (  content  !=  null  )  xTextCursor  .  setString  (  content  )  ; 
} 








public   String   getString  (  )  { 
return   xTextCursor  .  getString  (  )  ; 
} 












public   void   goLeft  (  short   stepNumber  ,  boolean   mark  )  { 
xTextCursor  .  goLeft  (  stepNumber  ,  mark  )  ; 
} 












public   void   goRight  (  short   stepNumber  ,  boolean   mark  )  { 
xTextCursor  .  goRight  (  stepNumber  ,  mark  )  ; 
} 








public   ITextRange   getStart  (  )  { 
return   new   TextRange  (  textDocument  ,  xTextCursor  .  getStart  (  )  )  ; 
} 








public   ITextRange   getEnd  (  )  { 
return   new   TextRange  (  textDocument  ,  xTextCursor  .  getEnd  (  )  )  ; 
} 










public   short   getStartPageNumber  (  )  { 
IViewCursor   viewCursor  =  textDocument  .  getViewCursorService  (  )  .  getViewCursor  (  )  ; 
IPageCursor   pageCursor  =  viewCursor  .  getPageCursor  (  )  ; 
if  (  pageCursor  !=  null  )  { 
viewCursor  .  goToRange  (  getStart  (  )  ,  false  )  ; 
return   pageCursor  .  getPage  (  )  ; 
} 
return  -  1  ; 
} 










public   short   getEndPageNumber  (  )  { 
IViewCursor   viewCursor  =  textDocument  .  getViewCursorService  (  )  .  getViewCursor  (  )  ; 
IPageCursor   pageCursor  =  viewCursor  .  getPageCursor  (  )  ; 
if  (  pageCursor  !=  null  )  { 
viewCursor  .  goToRange  (  getEnd  (  )  ,  false  )  ; 
return   pageCursor  .  getPage  (  )  ; 
} 
return  -  1  ; 
} 










public   void   insertPageBreak  (  )  throws   NOAException  { 
try  { 
XCell   xCell  =  (  XCell  )  UnoRuntime  .  queryInterface  (  XCell  .  class  ,  xTextCursor  .  getText  (  )  )  ; 
XPropertySet   propertySet  =  (  XPropertySet  )  UnoRuntime  .  queryInterface  (  XPropertySet  .  class  ,  xTextCursor  )  ; 
propertySet  .  setPropertyValue  (  "BreakType"  ,  BreakType  .  PAGE_AFTER  )  ; 
if  (  xCell  ==  null  )  { 
xTextCursor  .  getText  (  )  .  insertControlCharacter  (  xTextCursor  ,  ControlCharacter  .  PARAGRAPH_BREAK  ,  false  )  ; 
} 
}  catch  (  Throwable   throwable  )  { 
throw   new   NOAException  (  "Error inserting page break."  ,  throwable  )  ; 
} 
} 













public   void   insertDocument  (  String   url  )  throws   NOAException  { 
if  (  url  ==  null  )  return  ; 
try  { 
XDocumentInsertable   xDocumentInsertable  =  (  XDocumentInsertable  )  UnoRuntime  .  queryInterface  (  XDocumentInsertable  .  class  ,  xTextCursor  )  ; 
if  (  xDocumentInsertable  !=  null  )  xDocumentInsertable  .  insertDocumentFromURL  (  URLAdapter  .  adaptURL  (  url  )  ,  new   PropertyValue  [  0  ]  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   NOAException  (  throwable  )  ; 
} 
} 















public   void   insertDocument  (  InputStream   inputStream  ,  IFilter   filter  )  throws   NOAException  { 
if  (  inputStream  ==  null  ||  filter  ==  null  )  return  ; 
FileOutputStream   outputStream  =  null  ; 
File   tempFile  =  null  ; 
try  { 
XDocumentInsertable   xDocumentInsertable  =  (  XDocumentInsertable  )  UnoRuntime  .  queryInterface  (  XDocumentInsertable  .  class  ,  xTextCursor  )  ; 
if  (  xDocumentInsertable  !=  null  )  { 
boolean   useOld  =  true  ; 
if  (  useOld  )  { 
byte   buffer  [  ]  =  new   byte  [  0xffff  ]  ; 
int   bytes  =  -  1  ; 
tempFile  =  File  .  createTempFile  (  "noatemp"  +  System  .  currentTimeMillis  (  )  ,  "tmp"  )  ; 
tempFile  .  deleteOnExit  (  )  ; 
outputStream  =  new   FileOutputStream  (  tempFile  )  ; 
while  (  (  bytes  =  inputStream  .  read  (  buffer  )  )  !=  -  1  )  outputStream  .  write  (  buffer  ,  0  ,  bytes  )  ; 
insertDocument  (  tempFile  .  getAbsolutePath  (  )  )  ; 
}  else  { 
PropertyValue  [  ]  loadProps  =  new   PropertyValue  [  2  ]  ; 
loadProps  [  0  ]  =  new   PropertyValue  (  )  ; 
loadProps  [  0  ]  .  Name  =  "InputStream"  ; 
loadProps  [  0  ]  .  Value  =  new   ByteArrayXInputStreamAdapter  (  inputStream  ,  null  )  ; 
loadProps  [  1  ]  =  new   PropertyValue  (  )  ; 
loadProps  [  1  ]  .  Name  =  "FilterName"  ; 
loadProps  [  1  ]  .  Value  =  filter  .  getFilterDefinition  (  textDocument  )  ; 
xDocumentInsertable  .  insertDocumentFromURL  (  "private:stream"  ,  loadProps  )  ; 
} 
} 
}  catch  (  Throwable   throwable  )  { 
throw   new   NOAException  (  throwable  )  ; 
}  finally  { 
if  (  inputStream  !=  null  )  { 
try  { 
inputStream  .  close  (  )  ; 
}  catch  (  IOException   ioException  )  { 
} 
} 
if  (  outputStream  !=  null  )  { 
try  { 
outputStream  .  close  (  )  ; 
}  catch  (  IOException   ioException  )  { 
} 
} 
if  (  tempFile  !=  null  )  tempFile  .  delete  (  )  ; 
} 
} 









public   boolean   supportsWordCursor  (  )  { 
if  (  this  .  xWordCursor  !=  null  )  return   true  ; 
XWordCursor   xWordCursor  =  (  XWordCursor  )  UnoRuntime  .  queryInterface  (  XWordCursor  .  class  ,  xTextCursor  )  ; 
if  (  xWordCursor  !=  null  )  { 
this  .  xWordCursor  =  xWordCursor  ; 
return   true  ; 
} 
return   false  ; 
} 












public   boolean   isStartOfWord  (  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  isStartOfWord  (  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 












public   boolean   isEndOfWord  (  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  isEndOfWord  (  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 
















public   boolean   gotoNextWord  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  gotoNextWord  (  mark  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 
















public   boolean   gotoPreviousWord  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  gotoPreviousWord  (  mark  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 
















public   boolean   gotoEndOfWord  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  gotoEndOfWord  (  mark  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 
















public   boolean   gotoStartOfWord  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsWordCursor  (  )  )  return   xWordCursor  .  gotoStartOfWord  (  mark  )  ; 
throw   new   NOAException  (  "Word cursor operations not supported"  )  ; 
} 









public   boolean   supportsSentenceCursor  (  )  { 
if  (  this  .  xSentenceCursor  !=  null  )  return   true  ; 
XSentenceCursor   xSentenceCursor  =  (  XSentenceCursor  )  UnoRuntime  .  queryInterface  (  XSentenceCursor  .  class  ,  xTextCursor  )  ; 
if  (  xSentenceCursor  !=  null  )  { 
this  .  xSentenceCursor  =  xSentenceCursor  ; 
return   true  ; 
} 
return   false  ; 
} 












public   boolean   isStartOfSentence  (  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  return   xSentenceCursor  .  isStartOfSentence  (  )  ; 
throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 












public   boolean   isEndOfSentence  (  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  return   xSentenceCursor  .  isEndOfSentence  (  )  ; 
throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 














public   void   gotoNextSentence  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  xSentenceCursor  .  gotoNextSentence  (  mark  )  ;  else   throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 














public   void   gotoPreviousSentence  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  xSentenceCursor  .  gotoPreviousSentence  (  mark  )  ;  else   throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 














public   void   gotoEndOfSentence  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  xSentenceCursor  .  gotoEndOfSentence  (  mark  )  ;  else   throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 














public   void   gotoStartOfSentence  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsSentenceCursor  (  )  )  xSentenceCursor  .  gotoStartOfSentence  (  mark  )  ;  else   throw   new   NOAException  (  "Sentence cursor operations not supported"  )  ; 
} 









public   boolean   supportsParagraphCursor  (  )  { 
if  (  this  .  xParagraphCursor  !=  null  )  return   true  ; 
XParagraphCursor   xParagraphCursor  =  (  XParagraphCursor  )  UnoRuntime  .  queryInterface  (  XParagraphCursor  .  class  ,  xTextCursor  )  ; 
if  (  xParagraphCursor  !=  null  )  { 
this  .  xParagraphCursor  =  xParagraphCursor  ; 
return   true  ; 
} 
return   false  ; 
} 












public   boolean   isStartOfParagraph  (  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  return   xParagraphCursor  .  isStartOfParagraph  (  )  ; 
throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 












public   boolean   isEndOfParagraph  (  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  return   xParagraphCursor  .  isEndOfParagraph  (  )  ; 
throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 














public   void   gotoNextParagraph  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  xParagraphCursor  .  gotoNextParagraph  (  mark  )  ;  else   throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 














public   void   gotoPreviousParagraph  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  xParagraphCursor  .  gotoPreviousParagraph  (  mark  )  ;  else   throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 














public   void   gotoEndOfParagraph  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  xParagraphCursor  .  gotoEndOfParagraph  (  mark  )  ;  else   throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 














public   void   gotoStartOfParagraph  (  boolean   mark  )  throws   NOAException  { 
if  (  supportsParagraphCursor  (  )  )  xParagraphCursor  .  gotoStartOfParagraph  (  mark  )  ;  else   throw   new   NOAException  (  "Paragraph cursor operations not supported"  )  ; 
} 
} 

