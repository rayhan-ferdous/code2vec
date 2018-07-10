package   gridsim  .  auction  ; 

import   eduni  .  simjava  .  Sim_port  ; 
import   gridsim  .  auction  .  AuctionTags  ; 
import   gridsim  .  auction  .  DoubleAuction  ; 
import   gridsim  .  auction  .  MessageAsk  ; 
import   gridsim  .  auction  .  MessageBid  ; 
import   gridsim  .  auction  .  MessageCallForBids  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  LinkedList  ; 




























public   class   ContinuousDoubleAuction   extends   DoubleAuction  { 

private   LinkedList   asks  ; 

private   LinkedList   bids  ; 

private   Comparator   compAsks  ; 

private   Comparator   compBids  ; 

private   Object   syncObj  =  new   Object  (  )  ; 









public   ContinuousDoubleAuction  (  String   auctionName  ,  int   auctioneerID  ,  double   durationOfAuction  ,  Sim_port   output  )  throws   Exception  { 
super  (  auctionName  ,  auctioneerID  ,  AuctionTags  .  CONTINUOUS_DOUBLE_AUCTION  ,  durationOfAuction  ,  output  )  ; 
compAsks  =  new   OrderAsksByPriceAsc  (  )  ; 
compBids  =  new   OrderBidsByPriceDesc  (  )  ; 
asks  =  new   LinkedList  (  )  ; 
bids  =  new   LinkedList  (  )  ; 
} 







public   ContinuousDoubleAuction  (  String   auctionName  ,  double   durationOfAuction  )  throws   Exception  { 
super  (  auctionName  ,  AuctionTags  .  CONTINUOUS_DOUBLE_AUCTION  ,  durationOfAuction  )  ; 
compAsks  =  new   OrderAsksByPriceAsc  (  )  ; 
compBids  =  new   OrderBidsByPriceDesc  (  )  ; 
asks  =  new   LinkedList  (  )  ; 
bids  =  new   LinkedList  (  )  ; 
} 




public   void   onStart  (  )  { 
MessageCallForBids   msg  =  new   MessageCallForBids  (  super  .  getAuctionID  (  )  ,  super  .  getAuctionProtocol  (  )  ,  0D  ,  1  )  ; 
super  .  broadcastMessage  (  msg  )  ; 
} 




public   void   onStop  (  )  { 
synchronized  (  syncObj  )  { 
for  (  int   i  =  0  ;  i  <  asks  .  size  (  )  ;  i  ++  )  { 
super  .  match  (  (  MessageAsk  )  asks  .  get  (  i  )  ,  null  ,  0  )  ; 
} 
for  (  int   i  =  0  ;  i  <  bids  .  size  (  )  ;  i  ++  )  { 
super  .  match  (  null  ,  (  MessageBid  )  bids  .  get  (  i  )  ,  0  )  ; 
} 
asks  .  clear  (  )  ; 
bids  .  clear  (  )  ; 
} 
} 





public   void   onReceiveAsk  (  MessageAsk   ask  )  { 
synchronized  (  syncObj  )  { 
Collections  .  sort  (  bids  ,  compBids  )  ; 
if  (  bids  .  size  (  )  >  0  )  { 
MessageBid   bid  =  (  MessageBid  )  bids  .  getFirst  (  )  ; 
double   priceAsk  =  ask  .  getPrice  (  )  ; 
double   priceBid  =  bid  .  getPrice  (  )  ; 
if  (  priceBid  >=  priceAsk  )  { 
double   finalPrice  =  (  priceAsk  +  priceBid  )  /  2  ; 
super  .  match  (  ask  ,  bid  ,  finalPrice  )  ; 
bids  .  remove  (  bid  )  ; 
}  else  { 
asks  .  add  (  ask  )  ; 
} 
}  else  { 
asks  .  add  (  ask  )  ; 
} 
} 
} 





public   void   onReceiveBid  (  MessageBid   bid  )  { 
synchronized  (  syncObj  )  { 
Collections  .  sort  (  asks  ,  compAsks  )  ; 
if  (  asks  .  size  (  )  >  0  )  { 
MessageAsk   ask  =  (  MessageAsk  )  asks  .  getFirst  (  )  ; 
double   priceAsk  =  ask  .  getPrice  (  )  ; 
double   priceBid  =  bid  .  getPrice  (  )  ; 
if  (  priceBid  >=  priceAsk  )  { 
double   finalPrice  =  (  priceAsk  +  priceBid  )  /  2  ; 
super  .  match  (  ask  ,  bid  ,  finalPrice  )  ; 
asks  .  remove  (  ask  )  ; 
}  else  { 
bids  .  add  (  bid  )  ; 
} 
}  else  { 
bids  .  add  (  bid  )  ; 
} 
} 
} 








class   OrderAsksByPriceAsc   implements   Comparator  { 




public   OrderAsksByPriceAsc  (  )  { 
super  (  )  ; 
} 

public   int   compare  (  Object   a  ,  Object   b  )  throws   ClassCastException  { 
if  (  a  ==  null  )  { 
return  -  1  ; 
}  else   if  (  b  ==  null  )  { 
return   1  ; 
}  else   if  (  a  ==  null  &&  b  ==  null  )  { 
return   0  ; 
}  else  { 
MessageAsk   aska  =  (  MessageAsk  )  a  ; 
MessageAsk   askb  =  (  MessageAsk  )  b  ; 
Double   d_c1  =  new   Double  (  aska  .  getPrice  (  )  )  ; 
Double   d_c2  =  new   Double  (  askb  .  getPrice  (  )  )  ; 
return   d_c1  .  compareTo  (  d_c2  )  ; 
} 
} 
} 








class   OrderBidsByPriceDesc   implements   Comparator  { 




public   OrderBidsByPriceDesc  (  )  { 
super  (  )  ; 
} 

public   int   compare  (  Object   a  ,  Object   b  )  throws   ClassCastException  { 
if  (  a  ==  null  )  { 
return  -  1  ; 
}  else   if  (  b  ==  null  )  { 
return   1  ; 
}  else   if  (  a  ==  null  &&  b  ==  null  )  { 
return   0  ; 
}  else  { 
MessageBid   bida  =  (  MessageBid  )  a  ; 
MessageBid   bidb  =  (  MessageBid  )  b  ; 
Double   d_c1  =  new   Double  (  bida  .  getPrice  (  )  )  ; 
Double   d_c2  =  new   Double  (  bidb  .  getPrice  (  )  )  ; 
return   d_c2  .  compareTo  (  d_c1  )  ; 
} 
} 
} 
} 

