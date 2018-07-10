package   net  .  sf  .  ncsimulator  .  models  .  network  ; 

import   org  .  eclipse  .  emf  .  ecore  .  EAttribute  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EPackage  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EReference  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  EPackageImpl  ; 















public   class   NetworkPackage   extends   EPackageImpl  { 







public   static   final   String   eNAME  =  "network"  ; 







public   static   final   String   eNS_URI  =  "http://ncsimulator.sf.net/models/network/"  ; 







public   static   final   String   eNS_PREFIX  =  "network"  ; 







public   static   final   NetworkPackage   eINSTANCE  =  net  .  sf  .  ncsimulator  .  models  .  network  .  NetworkPackage  .  init  (  )  ; 









public   static   final   int   NODE  =  0  ; 








public   static   final   int   NODE__NAME  =  0  ; 








public   static   final   int   NODE__NETWORK  =  1  ; 








public   static   final   int   NODE__OUTPUT_CHANNELS  =  2  ; 








public   static   final   int   NODE__INPUT_CHANNELS  =  3  ; 








public   static   final   int   NODE__BEHAVIOUR_INSTANCE  =  4  ; 








public   static   final   int   NODE_FEATURE_COUNT  =  5  ; 









public   static   final   int   NETWORK  =  1  ; 








public   static   final   int   NETWORK__NODES  =  0  ; 








public   static   final   int   NETWORK__NAME  =  1  ; 








public   static   final   int   NETWORK__CHANNELS  =  2  ; 








public   static   final   int   NETWORK_FEATURE_COUNT  =  3  ; 









public   static   final   int   CHANNEL  =  2  ; 








public   static   final   int   CHANNEL__SOURCE  =  0  ; 








public   static   final   int   CHANNEL__TARGET  =  1  ; 








public   static   final   int   CHANNEL__NETWORK  =  2  ; 








public   static   final   int   CHANNEL__BEHAVIOUR  =  3  ; 








public   static   final   int   CHANNEL__TITLE  =  4  ; 








public   static   final   int   CHANNEL__BEHAVIOUR_INSTANCE  =  5  ; 








public   static   final   int   CHANNEL_FEATURE_COUNT  =  6  ; 









public   static   final   int   CHANNEL_BEHAVIOR  =  3  ; 








public   static   final   int   CHANNEL_BEHAVIOR_FEATURE_COUNT  =  0  ; 









public   static   final   int   NODE_BEHAVIOR  =  4  ; 








public   static   final   int   NODE_BEHAVIOR_FEATURE_COUNT  =  0  ; 






private   EClass   nodeEClass  =  null  ; 






private   EClass   networkEClass  =  null  ; 






private   EClass   channelEClass  =  null  ; 






private   EClass   channelBehaviorEClass  =  null  ; 






private   EClass   nodeBehaviorEClass  =  null  ; 
















private   NetworkPackage  (  )  { 
super  (  eNS_URI  ,  NetworkFactory  .  INSTANCE  )  ; 
} 






private   static   boolean   isInited  =  false  ; 













public   static   NetworkPackage   init  (  )  { 
if  (  isInited  )  return  (  NetworkPackage  )  EPackage  .  Registry  .  INSTANCE  .  getEPackage  (  NetworkPackage  .  eNS_URI  )  ; 
NetworkPackage   theNetworkPackage  =  (  NetworkPackage  )  (  EPackage  .  Registry  .  INSTANCE  .  get  (  eNS_URI  )  instanceof   NetworkPackage  ?  EPackage  .  Registry  .  INSTANCE  .  get  (  eNS_URI  )  :  new   NetworkPackage  (  )  )  ; 
isInited  =  true  ; 
theNetworkPackage  .  createPackageContents  (  )  ; 
theNetworkPackage  .  initializePackageContents  (  )  ; 
theNetworkPackage  .  freeze  (  )  ; 
EPackage  .  Registry  .  INSTANCE  .  put  (  NetworkPackage  .  eNS_URI  ,  theNetworkPackage  )  ; 
return   theNetworkPackage  ; 
} 









public   EClass   getNode  (  )  { 
return   nodeEClass  ; 
} 










public   EAttribute   getNode_Name  (  )  { 
return  (  EAttribute  )  nodeEClass  .  getEStructuralFeatures  (  )  .  get  (  0  )  ; 
} 










public   EReference   getNode_Network  (  )  { 
return  (  EReference  )  nodeEClass  .  getEStructuralFeatures  (  )  .  get  (  1  )  ; 
} 










public   EReference   getNode_OutputChannels  (  )  { 
return  (  EReference  )  nodeEClass  .  getEStructuralFeatures  (  )  .  get  (  2  )  ; 
} 










public   EReference   getNode_InputChannels  (  )  { 
return  (  EReference  )  nodeEClass  .  getEStructuralFeatures  (  )  .  get  (  3  )  ; 
} 










public   EReference   getNode_BehaviourInstance  (  )  { 
return  (  EReference  )  nodeEClass  .  getEStructuralFeatures  (  )  .  get  (  4  )  ; 
} 









public   EClass   getNetwork  (  )  { 
return   networkEClass  ; 
} 










public   EReference   getNetwork_Nodes  (  )  { 
return  (  EReference  )  networkEClass  .  getEStructuralFeatures  (  )  .  get  (  0  )  ; 
} 










public   EAttribute   getNetwork_Name  (  )  { 
return  (  EAttribute  )  networkEClass  .  getEStructuralFeatures  (  )  .  get  (  1  )  ; 
} 










public   EReference   getNetwork_Channels  (  )  { 
return  (  EReference  )  networkEClass  .  getEStructuralFeatures  (  )  .  get  (  2  )  ; 
} 









public   EClass   getChannel  (  )  { 
return   channelEClass  ; 
} 










public   EReference   getChannel_Source  (  )  { 
return  (  EReference  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  0  )  ; 
} 










public   EReference   getChannel_Target  (  )  { 
return  (  EReference  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  1  )  ; 
} 










public   EReference   getChannel_Network  (  )  { 
return  (  EReference  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  2  )  ; 
} 










public   EAttribute   getChannel_Behaviour  (  )  { 
return  (  EAttribute  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  3  )  ; 
} 










public   EAttribute   getChannel_Title  (  )  { 
return  (  EAttribute  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  4  )  ; 
} 










public   EReference   getChannel_BehaviourInstance  (  )  { 
return  (  EReference  )  channelEClass  .  getEStructuralFeatures  (  )  .  get  (  5  )  ; 
} 









public   EClass   getChannelBehavior  (  )  { 
return   channelBehaviorEClass  ; 
} 









public   EClass   getNodeBehavior  (  )  { 
return   nodeBehaviorEClass  ; 
} 








public   NetworkFactory   getNetworkFactory  (  )  { 
return  (  NetworkFactory  )  getEFactoryInstance  (  )  ; 
} 






private   boolean   isCreated  =  false  ; 








public   void   createPackageContents  (  )  { 
if  (  isCreated  )  return  ; 
isCreated  =  true  ; 
nodeEClass  =  createEClass  (  NODE  )  ; 
createEAttribute  (  nodeEClass  ,  NODE__NAME  )  ; 
createEReference  (  nodeEClass  ,  NODE__NETWORK  )  ; 
createEReference  (  nodeEClass  ,  NODE__OUTPUT_CHANNELS  )  ; 
createEReference  (  nodeEClass  ,  NODE__INPUT_CHANNELS  )  ; 
createEReference  (  nodeEClass  ,  NODE__BEHAVIOUR_INSTANCE  )  ; 
networkEClass  =  createEClass  (  NETWORK  )  ; 
createEReference  (  networkEClass  ,  NETWORK__NODES  )  ; 
createEAttribute  (  networkEClass  ,  NETWORK__NAME  )  ; 
createEReference  (  networkEClass  ,  NETWORK__CHANNELS  )  ; 
channelEClass  =  createEClass  (  CHANNEL  )  ; 
createEReference  (  channelEClass  ,  CHANNEL__SOURCE  )  ; 
createEReference  (  channelEClass  ,  CHANNEL__TARGET  )  ; 
createEReference  (  channelEClass  ,  CHANNEL__NETWORK  )  ; 
createEAttribute  (  channelEClass  ,  CHANNEL__BEHAVIOUR  )  ; 
createEAttribute  (  channelEClass  ,  CHANNEL__TITLE  )  ; 
createEReference  (  channelEClass  ,  CHANNEL__BEHAVIOUR_INSTANCE  )  ; 
channelBehaviorEClass  =  createEClass  (  CHANNEL_BEHAVIOR  )  ; 
nodeBehaviorEClass  =  createEClass  (  NODE_BEHAVIOR  )  ; 
} 






private   boolean   isInitialized  =  false  ; 








public   void   initializePackageContents  (  )  { 
if  (  isInitialized  )  return  ; 
isInitialized  =  true  ; 
setName  (  eNAME  )  ; 
setNsPrefix  (  eNS_PREFIX  )  ; 
setNsURI  (  eNS_URI  )  ; 
initEClass  (  nodeEClass  ,  Node  .  class  ,  "Node"  ,  !  IS_ABSTRACT  ,  !  IS_INTERFACE  ,  IS_GENERATED_INSTANCE_CLASS  )  ; 
initEAttribute  (  getNode_Name  (  )  ,  ecorePackage  .  getEString  (  )  ,  "Name"  ,  ""  ,  1  ,  1  ,  Node  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_UNSETTABLE  ,  !  IS_ID  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getNode_Network  (  )  ,  this  .  getNetwork  (  )  ,  this  .  getNetwork_Nodes  (  )  ,  "Network"  ,  null  ,  1  ,  1  ,  Node  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  !  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getNode_OutputChannels  (  )  ,  this  .  getChannel  (  )  ,  this  .  getChannel_Source  (  )  ,  "OutputChannels"  ,  null  ,  0  ,  -  1  ,  Node  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  !  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  !  IS_ORDERED  )  ; 
initEReference  (  getNode_InputChannels  (  )  ,  this  .  getChannel  (  )  ,  this  .  getChannel_Target  (  )  ,  "InputChannels"  ,  null  ,  0  ,  -  1  ,  Node  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  !  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  !  IS_ORDERED  )  ; 
initEReference  (  getNode_BehaviourInstance  (  )  ,  this  .  getNodeBehavior  (  )  ,  null  ,  "BehaviourInstance"  ,  null  ,  0  ,  1  ,  Node  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEClass  (  networkEClass  ,  Network  .  class  ,  "Network"  ,  !  IS_ABSTRACT  ,  !  IS_INTERFACE  ,  IS_GENERATED_INSTANCE_CLASS  )  ; 
initEReference  (  getNetwork_Nodes  (  )  ,  this  .  getNode  (  )  ,  this  .  getNode_Network  (  )  ,  "Nodes"  ,  null  ,  0  ,  -  1  ,  Network  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
getNetwork_Nodes  (  )  .  getEKeys  (  )  .  add  (  this  .  getNode_Name  (  )  )  ; 
initEAttribute  (  getNetwork_Name  (  )  ,  ecorePackage  .  getEString  (  )  ,  "Name"  ,  null  ,  0  ,  1  ,  Network  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_UNSETTABLE  ,  !  IS_ID  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getNetwork_Channels  (  )  ,  this  .  getChannel  (  )  ,  this  .  getChannel_Network  (  )  ,  "Channels"  ,  null  ,  0  ,  -  1  ,  Network  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEClass  (  channelEClass  ,  Channel  .  class  ,  "Channel"  ,  !  IS_ABSTRACT  ,  !  IS_INTERFACE  ,  IS_GENERATED_INSTANCE_CLASS  )  ; 
initEReference  (  getChannel_Source  (  )  ,  this  .  getNode  (  )  ,  this  .  getNode_OutputChannels  (  )  ,  "Source"  ,  null  ,  1  ,  1  ,  Channel  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getChannel_Target  (  )  ,  this  .  getNode  (  )  ,  this  .  getNode_InputChannels  (  )  ,  "Target"  ,  null  ,  1  ,  1  ,  Channel  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getChannel_Network  (  )  ,  this  .  getNetwork  (  )  ,  this  .  getNetwork_Channels  (  )  ,  "Network"  ,  null  ,  1  ,  1  ,  Channel  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEAttribute  (  getChannel_Behaviour  (  )  ,  ecorePackage  .  getEString  (  )  ,  "Behaviour"  ,  "net.sf.ncsimulator.channels.ideal.IdealChannelFactory"  ,  1  ,  1  ,  Channel  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  !  IS_UNSETTABLE  ,  !  IS_ID  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEAttribute  (  getChannel_Title  (  )  ,  ecorePackage  .  getEString  (  )  ,  "Title"  ,  ""  ,  0  ,  1  ,  Channel  .  class  ,  IS_TRANSIENT  ,  IS_VOLATILE  ,  !  IS_CHANGEABLE  ,  !  IS_UNSETTABLE  ,  !  IS_ID  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEReference  (  getChannel_BehaviourInstance  (  )  ,  this  .  getChannelBehavior  (  )  ,  null  ,  "BehaviourInstance"  ,  null  ,  0  ,  1  ,  Channel  .  class  ,  !  IS_TRANSIENT  ,  !  IS_VOLATILE  ,  IS_CHANGEABLE  ,  IS_COMPOSITE  ,  !  IS_RESOLVE_PROXIES  ,  !  IS_UNSETTABLE  ,  IS_UNIQUE  ,  !  IS_DERIVED  ,  IS_ORDERED  )  ; 
initEClass  (  channelBehaviorEClass  ,  ChannelBehavior  .  class  ,  "ChannelBehavior"  ,  IS_ABSTRACT  ,  IS_INTERFACE  ,  IS_GENERATED_INSTANCE_CLASS  )  ; 
initEClass  (  nodeBehaviorEClass  ,  NodeBehavior  .  class  ,  "NodeBehavior"  ,  IS_ABSTRACT  ,  IS_INTERFACE  ,  IS_GENERATED_INSTANCE_CLASS  )  ; 
createResource  (  eNS_URI  )  ; 
} 













public   interface   Literals  { 









public   static   final   EClass   NODE  =  eINSTANCE  .  getNode  (  )  ; 







public   static   final   EAttribute   NODE__NAME  =  eINSTANCE  .  getNode_Name  (  )  ; 







public   static   final   EReference   NODE__NETWORK  =  eINSTANCE  .  getNode_Network  (  )  ; 







public   static   final   EReference   NODE__OUTPUT_CHANNELS  =  eINSTANCE  .  getNode_OutputChannels  (  )  ; 







public   static   final   EReference   NODE__INPUT_CHANNELS  =  eINSTANCE  .  getNode_InputChannels  (  )  ; 







public   static   final   EReference   NODE__BEHAVIOUR_INSTANCE  =  eINSTANCE  .  getNode_BehaviourInstance  (  )  ; 









public   static   final   EClass   NETWORK  =  eINSTANCE  .  getNetwork  (  )  ; 







public   static   final   EReference   NETWORK__NODES  =  eINSTANCE  .  getNetwork_Nodes  (  )  ; 







public   static   final   EAttribute   NETWORK__NAME  =  eINSTANCE  .  getNetwork_Name  (  )  ; 







public   static   final   EReference   NETWORK__CHANNELS  =  eINSTANCE  .  getNetwork_Channels  (  )  ; 









public   static   final   EClass   CHANNEL  =  eINSTANCE  .  getChannel  (  )  ; 







public   static   final   EReference   CHANNEL__SOURCE  =  eINSTANCE  .  getChannel_Source  (  )  ; 







public   static   final   EReference   CHANNEL__TARGET  =  eINSTANCE  .  getChannel_Target  (  )  ; 







public   static   final   EReference   CHANNEL__NETWORK  =  eINSTANCE  .  getChannel_Network  (  )  ; 







public   static   final   EAttribute   CHANNEL__BEHAVIOUR  =  eINSTANCE  .  getChannel_Behaviour  (  )  ; 







public   static   final   EAttribute   CHANNEL__TITLE  =  eINSTANCE  .  getChannel_Title  (  )  ; 







public   static   final   EReference   CHANNEL__BEHAVIOUR_INSTANCE  =  eINSTANCE  .  getChannel_BehaviourInstance  (  )  ; 









public   static   final   EClass   CHANNEL_BEHAVIOR  =  eINSTANCE  .  getChannelBehavior  (  )  ; 









public   static   final   EClass   NODE_BEHAVIOR  =  eINSTANCE  .  getNodeBehavior  (  )  ; 
} 
} 

