package   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  lexer  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  PushbackReader  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  EOF  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TAsA  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TEndl  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TGiven  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TIWant  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TScenarioKeyword  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TSoThat  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TSpace  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TThen  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TTitleKeyword  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TWhen  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TWord  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  Token  ; 

public   class   Lexer  { 

protected   Token   token  ; 

protected   State   state  =  State  .  INITIAL  ; 

private   PushbackReader   in  ; 

private   int   line  ; 

private   int   pos  ; 

private   boolean   cr  ; 

private   boolean   eof  ; 

private   final   StringBuffer   text  =  new   StringBuffer  (  )  ; 

protected   void   filter  (  )  throws   LexerException  ,  IOException  { 
} 

public   Lexer  (  PushbackReader   in  )  { 
this  .  in  =  in  ; 
} 

public   Token   peek  (  )  throws   LexerException  ,  IOException  { 
while  (  token  ==  null  )  { 
token  =  getToken  (  )  ; 
filter  (  )  ; 
} 
return   token  ; 
} 

public   Token   next  (  )  throws   LexerException  ,  IOException  { 
while  (  token  ==  null  )  { 
token  =  getToken  (  )  ; 
filter  (  )  ; 
} 
Token   result  =  token  ; 
token  =  null  ; 
return   result  ; 
} 

protected   Token   getToken  (  )  throws   IOException  ,  LexerException  { 
int   dfa_state  =  0  ; 
int   start_pos  =  pos  ; 
int   start_line  =  line  ; 
int   accept_state  =  -  1  ; 
int   accept_token  =  -  1  ; 
int   accept_length  =  -  1  ; 
int   accept_pos  =  -  1  ; 
int   accept_line  =  -  1  ; 
int  [  ]  [  ]  [  ]  gotoTable  =  this  .  gotoTable  [  state  .  id  (  )  ]  ; 
int  [  ]  accept  =  this  .  accept  [  state  .  id  (  )  ]  ; 
text  .  setLength  (  0  )  ; 
while  (  true  )  { 
int   c  =  getChar  (  )  ; 
if  (  c  !=  -  1  )  { 
switch  (  c  )  { 
case   10  : 
if  (  cr  )  { 
cr  =  false  ; 
}  else  { 
line  ++  ; 
pos  =  0  ; 
} 
break  ; 
case   13  : 
line  ++  ; 
pos  =  0  ; 
cr  =  true  ; 
break  ; 
default  : 
pos  ++  ; 
cr  =  false  ; 
break  ; 
} 
; 
text  .  append  (  (  char  )  c  )  ; 
do  { 
int   oldState  =  (  dfa_state  <  -  1  )  ?  (  -  2  -  dfa_state  )  :  dfa_state  ; 
dfa_state  =  -  1  ; 
int  [  ]  [  ]  tmp1  =  gotoTable  [  oldState  ]  ; 
int   low  =  0  ; 
int   high  =  tmp1  .  length  -  1  ; 
while  (  low  <=  high  )  { 
int   middle  =  (  low  +  high  )  /  2  ; 
int  [  ]  tmp2  =  tmp1  [  middle  ]  ; 
if  (  c  <  tmp2  [  0  ]  )  { 
high  =  middle  -  1  ; 
}  else   if  (  c  >  tmp2  [  1  ]  )  { 
low  =  middle  +  1  ; 
}  else  { 
dfa_state  =  tmp2  [  2  ]  ; 
break  ; 
} 
} 
}  while  (  dfa_state  <  -  1  )  ; 
}  else  { 
dfa_state  =  -  1  ; 
} 
if  (  dfa_state  >=  0  )  { 
if  (  accept  [  dfa_state  ]  !=  -  1  )  { 
accept_state  =  dfa_state  ; 
accept_token  =  accept  [  dfa_state  ]  ; 
accept_length  =  text  .  length  (  )  ; 
accept_pos  =  pos  ; 
accept_line  =  line  ; 
} 
}  else  { 
if  (  accept_state  !=  -  1  )  { 
switch  (  accept_token  )  { 
case   0  : 
{ 
Token   token  =  new0  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   1  : 
{ 
Token   token  =  new1  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   2  : 
{ 
Token   token  =  new2  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   3  : 
{ 
Token   token  =  new3  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   4  : 
{ 
Token   token  =  new4  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   5  : 
{ 
Token   token  =  new5  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   6  : 
{ 
Token   token  =  new6  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   7  : 
{ 
Token   token  =  new7  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   8  : 
{ 
Token   token  =  new8  (  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   9  : 
{ 
Token   token  =  new9  (  getText  (  accept_length  )  ,  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
case   10  : 
{ 
Token   token  =  new10  (  getText  (  accept_length  )  ,  start_line  +  1  ,  start_pos  +  1  )  ; 
pushBack  (  accept_length  )  ; 
pos  =  accept_pos  ; 
line  =  accept_line  ; 
return   token  ; 
} 
} 
}  else  { 
if  (  text  .  length  (  )  >  0  )  { 
throw   new   LexerException  (  "["  +  (  start_line  +  1  )  +  ","  +  (  start_pos  +  1  )  +  "]"  +  " Unknown token: "  +  text  )  ; 
}  else  { 
EOF   token  =  new   EOF  (  start_line  +  1  ,  start_pos  +  1  )  ; 
return   token  ; 
} 
} 
} 
} 
} 

Token   new0  (  int   line  ,  int   pos  )  { 
return   new   TTitleKeyword  (  line  ,  pos  )  ; 
} 

Token   new1  (  int   line  ,  int   pos  )  { 
return   new   TScenarioKeyword  (  line  ,  pos  )  ; 
} 

Token   new2  (  int   line  ,  int   pos  )  { 
return   new   TAsA  (  line  ,  pos  )  ; 
} 

Token   new3  (  int   line  ,  int   pos  )  { 
return   new   TIWant  (  line  ,  pos  )  ; 
} 

Token   new4  (  int   line  ,  int   pos  )  { 
return   new   TSoThat  (  line  ,  pos  )  ; 
} 

Token   new5  (  int   line  ,  int   pos  )  { 
return   new   TGiven  (  line  ,  pos  )  ; 
} 

Token   new6  (  int   line  ,  int   pos  )  { 
return   new   TWhen  (  line  ,  pos  )  ; 
} 

Token   new7  (  int   line  ,  int   pos  )  { 
return   new   TThen  (  line  ,  pos  )  ; 
} 

Token   new8  (  int   line  ,  int   pos  )  { 
return   new   TSpace  (  line  ,  pos  )  ; 
} 

Token   new9  (  String   text  ,  int   line  ,  int   pos  )  { 
return   new   TWord  (  text  ,  line  ,  pos  )  ; 
} 

Token   new10  (  String   text  ,  int   line  ,  int   pos  )  { 
return   new   TEndl  (  text  ,  line  ,  pos  )  ; 
} 

private   int   getChar  (  )  throws   IOException  { 
if  (  eof  )  { 
return  -  1  ; 
} 
int   result  =  in  .  read  (  )  ; 
if  (  result  ==  -  1  )  { 
eof  =  true  ; 
} 
return   result  ; 
} 

private   void   pushBack  (  int   acceptLength  )  throws   IOException  { 
int   length  =  text  .  length  (  )  ; 
for  (  int   i  =  length  -  1  ;  i  >=  acceptLength  ;  i  --  )  { 
eof  =  false  ; 
in  .  unread  (  text  .  charAt  (  i  )  )  ; 
} 
} 

protected   void   unread  (  Token   token  )  throws   IOException  { 
String   text  =  token  .  getText  (  )  ; 
int   length  =  text  .  length  (  )  ; 
for  (  int   i  =  length  -  1  ;  i  >=  0  ;  i  --  )  { 
eof  =  false  ; 
in  .  unread  (  text  .  charAt  (  i  )  )  ; 
} 
pos  =  token  .  getPos  (  )  -  1  ; 
line  =  token  .  getLine  (  )  -  1  ; 
} 

private   String   getText  (  int   acceptLength  )  { 
StringBuffer   s  =  new   StringBuffer  (  acceptLength  )  ; 
for  (  int   i  =  0  ;  i  <  acceptLength  ;  i  ++  )  { 
s  .  append  (  text  .  charAt  (  i  )  )  ; 
} 
return   s  .  toString  (  )  ; 
} 

private   static   int  [  ]  [  ]  [  ]  [  ]  gotoTable  ; 

private   static   int  [  ]  [  ]  accept  ; 

public   static   class   State  { 

public   static   final   State   INITIAL  =  new   State  (  0  )  ; 

private   int   id  ; 

private   State  (  int   id  )  { 
this  .  id  =  id  ; 
} 

public   int   id  (  )  { 
return   id  ; 
} 
} 

static  { 
try  { 
DataInputStream   s  =  new   DataInputStream  (  new   BufferedInputStream  (  Lexer  .  class  .  getResourceAsStream  (  "lexer.dat"  )  )  )  ; 
int   length  =  s  .  readInt  (  )  ; 
gotoTable  =  new   int  [  length  ]  [  ]  [  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  gotoTable  .  length  ;  i  ++  )  { 
length  =  s  .  readInt  (  )  ; 
gotoTable  [  i  ]  =  new   int  [  length  ]  [  ]  [  ]  ; 
for  (  int   j  =  0  ;  j  <  gotoTable  [  i  ]  .  length  ;  j  ++  )  { 
length  =  s  .  readInt  (  )  ; 
gotoTable  [  i  ]  [  j  ]  =  new   int  [  length  ]  [  3  ]  ; 
for  (  int   k  =  0  ;  k  <  gotoTable  [  i  ]  [  j  ]  .  length  ;  k  ++  )  { 
for  (  int   l  =  0  ;  l  <  3  ;  l  ++  )  { 
gotoTable  [  i  ]  [  j  ]  [  k  ]  [  l  ]  =  s  .  readInt  (  )  ; 
} 
} 
} 
} 
length  =  s  .  readInt  (  )  ; 
accept  =  new   int  [  length  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  accept  .  length  ;  i  ++  )  { 
length  =  s  .  readInt  (  )  ; 
accept  [  i  ]  =  new   int  [  length  ]  ; 
for  (  int   j  =  0  ;  j  <  accept  [  i  ]  .  length  ;  j  ++  )  { 
accept  [  i  ]  [  j  ]  =  s  .  readInt  (  )  ; 
} 
} 
s  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "The file \"lexer.dat\" is either missing or corrupted."  )  ; 
} 
} 
} 

