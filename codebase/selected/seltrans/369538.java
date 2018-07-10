package   org  .  amiwall  .  feedback  ; 

import   java  .  util  .  Map  ; 
import   org  .  amiwall  .  user  .  User  ; 
import   org  .  amiwall  .  plugin  .  AbstractPlugin  ; 
import   org  .  amiwall  .  delivery  .  Delivery  ; 
import   org  .  amiwall  .  policy  .  Policy  ; 
import   org  .  jdom  .  Element  ; 
import   org  .  amiwall  .  delivery  .  EmailDelivery  ; 
import   java  .  util  .  Iterator  ; 






public   abstract   class   AbstractFeedback   extends   AbstractPlugin   implements   Feedback  { 




protected   Delivery   delivery  =  null  ; 

protected   Policy   policy  =  null  ; 






public   void   setDelivery  (  Delivery   delivery  )  { 
this  .  delivery  =  delivery  ; 
} 

public   void   setPolicy  (  Policy   policy  )  { 
this  .  policy  =  policy  ; 
} 







public   void   digest  (  Element   root  )  { 
digestDelivery  (  root  )  ; 
} 

void   digestDelivery  (  Element   root  )  { 
for  (  Iterator   i  =  root  .  getChildren  (  )  .  iterator  (  )  ;  i  .  hasNext  (  )  ;  )  { 
Element   child  =  (  Element  )  i  .  next  (  )  ; 
Delivery   delivery  =  getDelivery  (  child  .  getName  (  )  )  ; 
if  (  delivery  !=  null  )  { 
delivery  .  digest  (  child  )  ; 
setDelivery  (  delivery  )  ; 
break  ; 
} 
} 
} 

protected   Delivery   getDelivery  (  String   name  )  { 
if  (  name  .  equals  (  "EmailDelivery"  )  )  { 
return   new   EmailDelivery  (  )  ; 
} 
return   null  ; 
} 






public   void   activate  (  )  throws   Exception  { 
if  (  delivery  ==  null  )  { 
throw   new   NullPointerException  (  "delivery is NULL, this needs to be configured."  )  ; 
} 
delivery  .  activate  (  )  ; 
if  (  policy  ==  null  )  { 
throw   new   NullPointerException  (  "policy is NULL, this needs to be configured."  )  ; 
} 
} 




public   void   deactivate  (  )  { 
delivery  .  deactivate  (  )  ; 
} 
} 

