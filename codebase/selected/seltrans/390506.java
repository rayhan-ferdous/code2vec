package   util  ; 

import   java  .  io  .  *  ; 
import   java  .  nio  .  *  ; 
import   java  .  nio  .  channels  .  *  ; 
import   java  .  nio  .  charset  .  *  ; 
import   java  .  util  .  regex  .  *  ; 

public   class   Grep  { 

private   Charset   charset  =  Charset  .  forName  (  "ISO-8859-15"  )  ; 

private   CharsetDecoder   decoder  =  charset  .  newDecoder  (  )  ; 

private   Pattern   linePattern  =  Pattern  .  compile  (  ".*\r?\n"  )  ; 

private   Pattern   pattern  ; 








private   Pattern   compile  (  String   pat  )  throws   PatternSyntaxException  { 
return   pattern  =  Pattern  .  compile  (  pat  )  ; 
} 






public   Grep  (  String   pattern  )  throws   PatternSyntaxException  { 
compile  (  pattern  )  ; 
} 

private   boolean   grep  (  File   f  ,  CharBuffer   cb  )  { 
Matcher   lm  =  linePattern  .  matcher  (  cb  )  ; 
Matcher   pm  =  null  ; 
int   lines  =  0  ; 
while  (  lm  .  find  (  )  )  { 
lines  ++  ; 
CharSequence   cs  =  lm  .  group  (  )  ; 
if  (  pm  ==  null  )  pm  =  pattern  .  matcher  (  cs  )  ;  else   pm  .  reset  (  cs  )  ; 
if  (  pm  .  find  (  )  )  return   true  ; 
if  (  lm  .  end  (  )  ==  cb  .  limit  (  )  )  break  ; 
} 
return   false  ; 
} 








public   boolean   patternOccurrsOnce  (  File   f  )  throws   IOException  { 
FileInputStream   fis  =  new   FileInputStream  (  f  )  ; 
FileChannel   fc  =  fis  .  getChannel  (  )  ; 
int   fileSize  =  (  int  )  fc  .  size  (  )  ; 
MappedByteBuffer   bb  =  fc  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  fileSize  )  ; 
CharBuffer   cb  =  decoder  .  decode  (  bb  )  ; 
if  (  grep  (  f  ,  cb  )  )  { 
fc  .  close  (  )  ; 
return   true  ; 
}  else  { 
fc  .  close  (  )  ; 
return   false  ; 
} 
} 
} 

