package   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  parser  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  ListIterator  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  analysis  .  Analysis  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  analysis  .  AnalysisAdapter  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  lexer  .  Lexer  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  lexer  .  LexerException  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  ABenefit  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AContext  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AEvent  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AFeature  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AOutcome  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  APhrase  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  ARole  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AScenario  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AScenarioTitle  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  ASpaceWordOrSpace  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AStory  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  ATitle  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  AWordWordOrSpace  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  EOF  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  NodeCast  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PBenefit  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PContext  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PEvent  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PFeature  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  POutcome  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PPhrase  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PRole  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PScenario  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PScenarioTitle  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PStory  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PTitle  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  PWordOrSpace  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  Start  ; 
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  Switchable  ; 
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
import   org  .  jbehave  .  core  .  story  .  codegen  .  sablecc  .  node  .  TypedLinkedList  ; 

public   class   Parser  { 

public   final   Analysis   ignoredTokens  =  new   AnalysisAdapter  (  )  ; 

protected   ArrayList   nodeList  ; 

private   final   Lexer   lexer  ; 

private   final   ListIterator   stack  =  new   LinkedList  (  )  .  listIterator  (  )  ; 

private   int   last_shift  ; 

private   int   last_pos  ; 

private   int   last_line  ; 

private   Token   last_token  ; 

private   final   TokenIndex   converter  =  new   TokenIndex  (  )  ; 

private   final   int  [  ]  action  =  new   int  [  2  ]  ; 

private   static   final   int   SHIFT  =  0  ; 

private   static   final   int   REDUCE  =  1  ; 

private   static   final   int   ACCEPT  =  2  ; 

private   static   final   int   ERROR  =  3  ; 

public   Parser  (  Lexer   lexer  )  { 
this  .  lexer  =  lexer  ; 
} 

protected   void   filter  (  )  throws   ParserException  ,  LexerException  ,  IOException  { 
} 

private   void   push  (  int   numstate  ,  ArrayList   listNode  ,  boolean   hidden  )  throws   ParserException  ,  LexerException  ,  IOException  { 
this  .  nodeList  =  listNode  ; 
if  (  !  hidden  )  { 
filter  (  )  ; 
} 
if  (  !  stack  .  hasNext  (  )  )  { 
stack  .  add  (  new   State  (  numstate  ,  this  .  nodeList  )  )  ; 
return  ; 
} 
State   s  =  (  State  )  stack  .  next  (  )  ; 
s  .  state  =  numstate  ; 
s  .  nodes  =  this  .  nodeList  ; 
} 

private   int   goTo  (  int   index  )  { 
int   state  =  state  (  )  ; 
int   low  =  1  ; 
int   high  =  gotoTable  [  index  ]  .  length  -  1  ; 
int   value  =  gotoTable  [  index  ]  [  0  ]  [  1  ]  ; 
while  (  low  <=  high  )  { 
int   middle  =  (  low  +  high  )  /  2  ; 
if  (  state  <  gotoTable  [  index  ]  [  middle  ]  [  0  ]  )  { 
high  =  middle  -  1  ; 
}  else   if  (  state  >  gotoTable  [  index  ]  [  middle  ]  [  0  ]  )  { 
low  =  middle  +  1  ; 
}  else  { 
value  =  gotoTable  [  index  ]  [  middle  ]  [  1  ]  ; 
break  ; 
} 
} 
return   value  ; 
} 

private   int   state  (  )  { 
State   s  =  (  State  )  stack  .  previous  (  )  ; 
stack  .  next  (  )  ; 
return   s  .  state  ; 
} 

private   ArrayList   pop  (  )  { 
return  (  ArrayList  )  (  (  State  )  stack  .  previous  (  )  )  .  nodes  ; 
} 

private   int   index  (  Switchable   token  )  { 
converter  .  index  =  -  1  ; 
token  .  apply  (  converter  )  ; 
return   converter  .  index  ; 
} 

public   Start   parse  (  )  throws   ParserException  ,  LexerException  ,  IOException  { 
push  (  0  ,  null  ,  true  )  ; 
List   ign  =  null  ; 
while  (  true  )  { 
while  (  index  (  lexer  .  peek  (  )  )  ==  -  1  )  { 
if  (  ign  ==  null  )  { 
ign  =  new   TypedLinkedList  (  NodeCast  .  instance  )  ; 
} 
ign  .  add  (  lexer  .  next  (  )  )  ; 
} 
if  (  ign  !=  null  )  { 
ignoredTokens  .  setIn  (  lexer  .  peek  (  )  ,  ign  )  ; 
ign  =  null  ; 
} 
last_pos  =  lexer  .  peek  (  )  .  getPos  (  )  ; 
last_line  =  lexer  .  peek  (  )  .  getLine  (  )  ; 
last_token  =  lexer  .  peek  (  )  ; 
int   index  =  index  (  lexer  .  peek  (  )  )  ; 
action  [  0  ]  =  actionTable  [  state  (  )  ]  [  0  ]  [  1  ]  ; 
action  [  1  ]  =  actionTable  [  state  (  )  ]  [  0  ]  [  2  ]  ; 
int   low  =  1  ; 
int   high  =  actionTable  [  state  (  )  ]  .  length  -  1  ; 
while  (  low  <=  high  )  { 
int   middle  =  (  low  +  high  )  /  2  ; 
if  (  index  <  actionTable  [  state  (  )  ]  [  middle  ]  [  0  ]  )  { 
high  =  middle  -  1  ; 
}  else   if  (  index  >  actionTable  [  state  (  )  ]  [  middle  ]  [  0  ]  )  { 
low  =  middle  +  1  ; 
}  else  { 
action  [  0  ]  =  actionTable  [  state  (  )  ]  [  middle  ]  [  1  ]  ; 
action  [  1  ]  =  actionTable  [  state  (  )  ]  [  middle  ]  [  2  ]  ; 
break  ; 
} 
} 
switch  (  action  [  0  ]  )  { 
case   SHIFT  : 
{ 
ArrayList   list  =  new   ArrayList  (  )  ; 
list  .  add  (  lexer  .  next  (  )  )  ; 
push  (  action  [  1  ]  ,  list  ,  false  )  ; 
last_shift  =  action  [  1  ]  ; 
} 
break  ; 
case   REDUCE  : 
switch  (  action  [  1  ]  )  { 
case   0  : 
{ 
ArrayList   list  =  new0  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   1  : 
{ 
ArrayList   list  =  new1  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   2  : 
{ 
ArrayList   list  =  new2  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   3  : 
{ 
ArrayList   list  =  new3  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   4  : 
{ 
ArrayList   list  =  new4  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   5  : 
{ 
ArrayList   list  =  new5  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   6  : 
{ 
ArrayList   list  =  new6  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   7  : 
{ 
ArrayList   list  =  new7  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   8  : 
{ 
ArrayList   list  =  new8  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   9  : 
{ 
ArrayList   list  =  new9  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   10  : 
{ 
ArrayList   list  =  new10  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   11  : 
{ 
ArrayList   list  =  new11  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   12  : 
{ 
ArrayList   list  =  new12  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   13  : 
{ 
ArrayList   list  =  new13  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   14  : 
{ 
ArrayList   list  =  new14  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   15  : 
{ 
ArrayList   list  =  new15  (  )  ; 
push  (  goTo  (  0  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   16  : 
{ 
ArrayList   list  =  new16  (  )  ; 
push  (  goTo  (  1  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   17  : 
{ 
ArrayList   list  =  new17  (  )  ; 
push  (  goTo  (  2  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   18  : 
{ 
ArrayList   list  =  new18  (  )  ; 
push  (  goTo  (  3  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   19  : 
{ 
ArrayList   list  =  new19  (  )  ; 
push  (  goTo  (  4  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   20  : 
{ 
ArrayList   list  =  new20  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   21  : 
{ 
ArrayList   list  =  new21  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   22  : 
{ 
ArrayList   list  =  new22  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   23  : 
{ 
ArrayList   list  =  new23  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   24  : 
{ 
ArrayList   list  =  new24  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   25  : 
{ 
ArrayList   list  =  new25  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   26  : 
{ 
ArrayList   list  =  new26  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   27  : 
{ 
ArrayList   list  =  new27  (  )  ; 
push  (  goTo  (  5  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   28  : 
{ 
ArrayList   list  =  new28  (  )  ; 
push  (  goTo  (  6  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   29  : 
{ 
ArrayList   list  =  new29  (  )  ; 
push  (  goTo  (  7  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   30  : 
{ 
ArrayList   list  =  new30  (  )  ; 
push  (  goTo  (  8  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   31  : 
{ 
ArrayList   list  =  new31  (  )  ; 
push  (  goTo  (  9  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   32  : 
{ 
ArrayList   list  =  new32  (  )  ; 
push  (  goTo  (  10  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   33  : 
{ 
ArrayList   list  =  new33  (  )  ; 
push  (  goTo  (  11  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   34  : 
{ 
ArrayList   list  =  new34  (  )  ; 
push  (  goTo  (  11  )  ,  list  ,  false  )  ; 
} 
break  ; 
case   35  : 
{ 
ArrayList   list  =  new35  (  )  ; 
push  (  goTo  (  12  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   36  : 
{ 
ArrayList   list  =  new36  (  )  ; 
push  (  goTo  (  12  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   37  : 
{ 
ArrayList   list  =  new37  (  )  ; 
push  (  goTo  (  13  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   38  : 
{ 
ArrayList   list  =  new38  (  )  ; 
push  (  goTo  (  13  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   39  : 
{ 
ArrayList   list  =  new39  (  )  ; 
push  (  goTo  (  14  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   40  : 
{ 
ArrayList   list  =  new40  (  )  ; 
push  (  goTo  (  14  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   41  : 
{ 
ArrayList   list  =  new41  (  )  ; 
push  (  goTo  (  15  )  ,  list  ,  true  )  ; 
} 
break  ; 
case   42  : 
{ 
ArrayList   list  =  new42  (  )  ; 
push  (  goTo  (  15  )  ,  list  ,  true  )  ; 
} 
break  ; 
} 
break  ; 
case   ACCEPT  : 
{ 
EOF   node2  =  (  EOF  )  lexer  .  next  (  )  ; 
PStory   node1  =  (  PStory  )  (  (  ArrayList  )  pop  (  )  )  .  get  (  0  )  ; 
Start   node  =  new   Start  (  node1  ,  node2  )  ; 
return   node  ; 
} 
case   ERROR  : 
throw   new   ParserException  (  last_token  ,  "["  +  last_line  +  ","  +  last_pos  +  "] "  +  errorMessages  [  errors  [  action  [  1  ]  ]  ]  )  ; 
} 
} 
} 

ArrayList   new0  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
Object   nullNode4  =  null  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  null  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new1  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
Object   nullNode4  =  null  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  null  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new2  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
PFeature   pfeatureNode4  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  pfeatureNode4  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new3  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
PFeature   pfeatureNode4  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  pfeatureNode4  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new4  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
Object   nullNode4  =  null  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  null  ,  pbenefitNode5  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new5  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
Object   nullNode4  =  null  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  null  ,  pbenefitNode5  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new6  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
PFeature   pfeatureNode4  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList2  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  pfeatureNode4  ,  pbenefitNode5  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new7  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
PFeature   pfeatureNode4  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList3  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList4  .  get  (  0  )  ; 
{ 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  pfeatureNode4  ,  pbenefitNode5  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new8  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
Object   nullNode4  =  null  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  null  ,  null  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new9  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
Object   nullNode4  =  null  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList3  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  null  ,  null  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new10  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
PFeature   pfeatureNode4  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList3  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  pfeatureNode4  ,  null  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new11  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
PFeature   pfeatureNode4  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList4  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  pfeatureNode4  ,  null  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new12  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
Object   nullNode4  =  null  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList3  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  null  ,  pbenefitNode5  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new13  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
Object   nullNode4  =  null  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList4  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  null  ,  pbenefitNode5  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new14  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
Object   nullNode3  =  null  ; 
PFeature   pfeatureNode4  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList2  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList4  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  null  ,  pfeatureNode4  ,  pbenefitNode5  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new15  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList5  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PStory   pstoryNode1  ; 
{ 
PTitle   ptitleNode2  ; 
PRole   proleNode3  ; 
PFeature   pfeatureNode4  ; 
PBenefit   pbenefitNode5  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
ptitleNode2  =  (  PTitle  )  nodeArrayList1  .  get  (  0  )  ; 
proleNode3  =  (  PRole  )  nodeArrayList2  .  get  (  0  )  ; 
pfeatureNode4  =  (  PFeature  )  nodeArrayList3  .  get  (  0  )  ; 
pbenefitNode5  =  (  PBenefit  )  nodeArrayList4  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList5  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pstoryNode1  =  new   AStory  (  ptitleNode2  ,  proleNode3  ,  pfeatureNode4  ,  pbenefitNode5  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pstoryNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new16  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PTitle   ptitleNode1  ; 
{ 
TTitleKeyword   ttitlekeywordNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
ttitlekeywordNode2  =  (  TTitleKeyword  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
ptitleNode1  =  new   ATitle  (  ttitlekeywordNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  ptitleNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new17  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PRole   proleNode1  ; 
{ 
TAsA   tasaNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tasaNode2  =  (  TAsA  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
proleNode1  =  new   ARole  (  tasaNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  proleNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new18  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PFeature   pfeatureNode1  ; 
{ 
TIWant   tiwantNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tiwantNode2  =  (  TIWant  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
pfeatureNode1  =  new   AFeature  (  tiwantNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  pfeatureNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new19  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PBenefit   pbenefitNode1  ; 
{ 
TSoThat   tsothatNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tsothatNode2  =  (  TSoThat  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
pbenefitNode1  =  new   ABenefit  (  tsothatNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  pbenefitNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new20  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
Object   nullNode4  =  null  ; 
TypedLinkedList   listNode5  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
} 
{ 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode3  ,  null  ,  listNode5  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new21  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode4  =  new   TypedLinkedList  (  )  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
listNode3  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode3  !=  null  )  { 
listNode4  .  addAll  (  listNode3  )  ; 
} 
} 
{ 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode4  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new22  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
PEvent   peventNode4  ; 
TypedLinkedList   listNode5  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
} 
peventNode4  =  (  PEvent  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode3  ,  peventNode4  ,  listNode5  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new23  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode4  =  new   TypedLinkedList  (  )  ; 
PEvent   peventNode5  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
listNode3  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode3  !=  null  )  { 
listNode4  .  addAll  (  listNode3  )  ; 
} 
} 
peventNode5  =  (  PEvent  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode4  ,  peventNode5  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new24  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
Object   nullNode4  =  null  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
} 
{ 
TypedLinkedList   listNode5  =  new   TypedLinkedList  (  )  ; 
listNode5  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode5  !=  null  )  { 
listNode6  .  addAll  (  listNode5  )  ; 
} 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode3  ,  null  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new25  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode4  =  new   TypedLinkedList  (  )  ; 
Object   nullNode5  =  null  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
listNode3  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode3  !=  null  )  { 
listNode4  .  addAll  (  listNode3  )  ; 
} 
} 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList3  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode4  ,  null  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new26  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
PEvent   peventNode4  ; 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
} 
peventNode4  =  (  PEvent  )  nodeArrayList2  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode5  =  new   TypedLinkedList  (  )  ; 
listNode5  =  (  TypedLinkedList  )  nodeArrayList3  .  get  (  0  )  ; 
if  (  listNode5  !=  null  )  { 
listNode6  .  addAll  (  listNode5  )  ; 
} 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode3  ,  peventNode4  ,  listNode6  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new27  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenario   pscenarioNode1  ; 
{ 
PScenarioTitle   pscenariotitleNode2  ; 
TypedLinkedList   listNode4  =  new   TypedLinkedList  (  )  ; 
PEvent   peventNode5  ; 
TypedLinkedList   listNode7  =  new   TypedLinkedList  (  )  ; 
pscenariotitleNode2  =  (  PScenarioTitle  )  nodeArrayList1  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
listNode3  =  (  TypedLinkedList  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode3  !=  null  )  { 
listNode4  .  addAll  (  listNode3  )  ; 
} 
} 
peventNode5  =  (  PEvent  )  nodeArrayList3  .  get  (  0  )  ; 
{ 
TypedLinkedList   listNode6  =  new   TypedLinkedList  (  )  ; 
listNode6  =  (  TypedLinkedList  )  nodeArrayList4  .  get  (  0  )  ; 
if  (  listNode6  !=  null  )  { 
listNode7  .  addAll  (  listNode6  )  ; 
} 
} 
pscenarioNode1  =  new   AScenario  (  pscenariotitleNode2  ,  listNode4  ,  peventNode5  ,  listNode7  )  ; 
} 
nodeList  .  add  (  pscenarioNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new28  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PScenarioTitle   pscenariotitleNode1  ; 
{ 
TScenarioKeyword   tscenariokeywordNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tscenariokeywordNode2  =  (  TScenarioKeyword  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
pscenariotitleNode1  =  new   AScenarioTitle  (  tscenariokeywordNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  pscenariotitleNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new29  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PContext   pcontextNode1  ; 
{ 
TGiven   tgivenNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tgivenNode2  =  (  TGiven  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
pcontextNode1  =  new   AContext  (  tgivenNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  pcontextNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new30  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PEvent   peventNode1  ; 
{ 
TWhen   twhenNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
twhenNode2  =  (  TWhen  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
peventNode1  =  new   AEvent  (  twhenNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  peventNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new31  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList4  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList3  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
POutcome   poutcomeNode1  ; 
{ 
TThen   tthenNode2  ; 
TSpace   tspaceNode3  ; 
PPhrase   pphraseNode4  ; 
TEndl   tendlNode5  ; 
tthenNode2  =  (  TThen  )  nodeArrayList1  .  get  (  0  )  ; 
tspaceNode3  =  (  TSpace  )  nodeArrayList2  .  get  (  0  )  ; 
pphraseNode4  =  (  PPhrase  )  nodeArrayList3  .  get  (  0  )  ; 
tendlNode5  =  (  TEndl  )  nodeArrayList4  .  get  (  0  )  ; 
poutcomeNode1  =  new   AOutcome  (  tthenNode2  ,  tspaceNode3  ,  pphraseNode4  ,  tendlNode5  )  ; 
} 
nodeList  .  add  (  poutcomeNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new32  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PPhrase   pphraseNode1  ; 
{ 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
{ 
TypedLinkedList   listNode2  =  new   TypedLinkedList  (  )  ; 
listNode2  =  (  TypedLinkedList  )  nodeArrayList1  .  get  (  0  )  ; 
if  (  listNode2  !=  null  )  { 
listNode3  .  addAll  (  listNode2  )  ; 
} 
} 
pphraseNode1  =  new   APhrase  (  listNode3  )  ; 
} 
nodeList  .  add  (  pphraseNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new33  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PWordOrSpace   pwordorspaceNode1  ; 
{ 
TWord   twordNode2  ; 
twordNode2  =  (  TWord  )  nodeArrayList1  .  get  (  0  )  ; 
pwordorspaceNode1  =  new   AWordWordOrSpace  (  twordNode2  )  ; 
} 
nodeList  .  add  (  pwordorspaceNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new34  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
PWordOrSpace   pwordorspaceNode1  ; 
{ 
TSpace   tspaceNode2  ; 
tspaceNode2  =  (  TSpace  )  nodeArrayList1  .  get  (  0  )  ; 
pwordorspaceNode1  =  new   ASpaceWordOrSpace  (  tspaceNode2  )  ; 
} 
nodeList  .  add  (  pwordorspaceNode1  )  ; 
return   nodeList  ; 
} 

ArrayList   new35  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode2  =  new   TypedLinkedList  (  )  ; 
{ 
PScenario   pscenarioNode1  ; 
pscenarioNode1  =  (  PScenario  )  nodeArrayList1  .  get  (  0  )  ; 
if  (  pscenarioNode1  !=  null  )  { 
listNode2  .  add  (  pscenarioNode1  )  ; 
} 
} 
nodeList  .  add  (  listNode2  )  ; 
return   nodeList  ; 
} 

ArrayList   new36  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
{ 
TypedLinkedList   listNode1  =  new   TypedLinkedList  (  )  ; 
PScenario   pscenarioNode2  ; 
listNode1  =  (  TypedLinkedList  )  nodeArrayList1  .  get  (  0  )  ; 
pscenarioNode2  =  (  PScenario  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode1  !=  null  )  { 
listNode3  .  addAll  (  listNode1  )  ; 
} 
if  (  pscenarioNode2  !=  null  )  { 
listNode3  .  add  (  pscenarioNode2  )  ; 
} 
} 
nodeList  .  add  (  listNode3  )  ; 
return   nodeList  ; 
} 

ArrayList   new37  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode2  =  new   TypedLinkedList  (  )  ; 
{ 
PContext   pcontextNode1  ; 
pcontextNode1  =  (  PContext  )  nodeArrayList1  .  get  (  0  )  ; 
if  (  pcontextNode1  !=  null  )  { 
listNode2  .  add  (  pcontextNode1  )  ; 
} 
} 
nodeList  .  add  (  listNode2  )  ; 
return   nodeList  ; 
} 

ArrayList   new38  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
{ 
TypedLinkedList   listNode1  =  new   TypedLinkedList  (  )  ; 
PContext   pcontextNode2  ; 
listNode1  =  (  TypedLinkedList  )  nodeArrayList1  .  get  (  0  )  ; 
pcontextNode2  =  (  PContext  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode1  !=  null  )  { 
listNode3  .  addAll  (  listNode1  )  ; 
} 
if  (  pcontextNode2  !=  null  )  { 
listNode3  .  add  (  pcontextNode2  )  ; 
} 
} 
nodeList  .  add  (  listNode3  )  ; 
return   nodeList  ; 
} 

ArrayList   new39  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode2  =  new   TypedLinkedList  (  )  ; 
{ 
POutcome   poutcomeNode1  ; 
poutcomeNode1  =  (  POutcome  )  nodeArrayList1  .  get  (  0  )  ; 
if  (  poutcomeNode1  !=  null  )  { 
listNode2  .  add  (  poutcomeNode1  )  ; 
} 
} 
nodeList  .  add  (  listNode2  )  ; 
return   nodeList  ; 
} 

ArrayList   new40  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
{ 
TypedLinkedList   listNode1  =  new   TypedLinkedList  (  )  ; 
POutcome   poutcomeNode2  ; 
listNode1  =  (  TypedLinkedList  )  nodeArrayList1  .  get  (  0  )  ; 
poutcomeNode2  =  (  POutcome  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode1  !=  null  )  { 
listNode3  .  addAll  (  listNode1  )  ; 
} 
if  (  poutcomeNode2  !=  null  )  { 
listNode3  .  add  (  poutcomeNode2  )  ; 
} 
} 
nodeList  .  add  (  listNode3  )  ; 
return   nodeList  ; 
} 

ArrayList   new41  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode2  =  new   TypedLinkedList  (  )  ; 
{ 
PWordOrSpace   pwordorspaceNode1  ; 
pwordorspaceNode1  =  (  PWordOrSpace  )  nodeArrayList1  .  get  (  0  )  ; 
if  (  pwordorspaceNode1  !=  null  )  { 
listNode2  .  add  (  pwordorspaceNode1  )  ; 
} 
} 
nodeList  .  add  (  listNode2  )  ; 
return   nodeList  ; 
} 

ArrayList   new42  (  )  { 
ArrayList   nodeList  =  new   ArrayList  (  )  ; 
ArrayList   nodeArrayList2  =  (  ArrayList  )  pop  (  )  ; 
ArrayList   nodeArrayList1  =  (  ArrayList  )  pop  (  )  ; 
TypedLinkedList   listNode3  =  new   TypedLinkedList  (  )  ; 
{ 
TypedLinkedList   listNode1  =  new   TypedLinkedList  (  )  ; 
PWordOrSpace   pwordorspaceNode2  ; 
listNode1  =  (  TypedLinkedList  )  nodeArrayList1  .  get  (  0  )  ; 
pwordorspaceNode2  =  (  PWordOrSpace  )  nodeArrayList2  .  get  (  0  )  ; 
if  (  listNode1  !=  null  )  { 
listNode3  .  addAll  (  listNode1  )  ; 
} 
if  (  pwordorspaceNode2  !=  null  )  { 
listNode3  .  add  (  pwordorspaceNode2  )  ; 
} 
} 
nodeList  .  add  (  listNode3  )  ; 
return   nodeList  ; 
} 

private   static   int  [  ]  [  ]  [  ]  actionTable  ; 

private   static   int  [  ]  [  ]  [  ]  gotoTable  ; 

private   static   String  [  ]  errorMessages  ; 

private   static   int  [  ]  errors  ; 

static  { 
try  { 
DataInputStream   s  =  new   DataInputStream  (  new   BufferedInputStream  (  Parser  .  class  .  getResourceAsStream  (  "parser.dat"  )  )  )  ; 
int   length  =  s  .  readInt  (  )  ; 
actionTable  =  new   int  [  length  ]  [  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  actionTable  .  length  ;  i  ++  )  { 
length  =  s  .  readInt  (  )  ; 
actionTable  [  i  ]  =  new   int  [  length  ]  [  3  ]  ; 
for  (  int   j  =  0  ;  j  <  actionTable  [  i  ]  .  length  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  3  ;  k  ++  )  { 
actionTable  [  i  ]  [  j  ]  [  k  ]  =  s  .  readInt  (  )  ; 
} 
} 
} 
length  =  s  .  readInt  (  )  ; 
gotoTable  =  new   int  [  length  ]  [  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  gotoTable  .  length  ;  i  ++  )  { 
length  =  s  .  readInt  (  )  ; 
gotoTable  [  i  ]  =  new   int  [  length  ]  [  2  ]  ; 
for  (  int   j  =  0  ;  j  <  gotoTable  [  i  ]  .  length  ;  j  ++  )  { 
for  (  int   k  =  0  ;  k  <  2  ;  k  ++  )  { 
gotoTable  [  i  ]  [  j  ]  [  k  ]  =  s  .  readInt  (  )  ; 
} 
} 
} 
length  =  s  .  readInt  (  )  ; 
errorMessages  =  new   String  [  length  ]  ; 
for  (  int   i  =  0  ;  i  <  errorMessages  .  length  ;  i  ++  )  { 
length  =  s  .  readInt  (  )  ; 
StringBuffer   buffer  =  new   StringBuffer  (  )  ; 
for  (  int   j  =  0  ;  j  <  length  ;  j  ++  )  { 
buffer  .  append  (  s  .  readChar  (  )  )  ; 
} 
errorMessages  [  i  ]  =  buffer  .  toString  (  )  ; 
} 
length  =  s  .  readInt  (  )  ; 
errors  =  new   int  [  length  ]  ; 
for  (  int   i  =  0  ;  i  <  errors  .  length  ;  i  ++  )  { 
errors  [  i  ]  =  s  .  readInt  (  )  ; 
} 
s  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "The file \"parser.dat\" is either missing or corrupted."  )  ; 
} 
} 
} 

