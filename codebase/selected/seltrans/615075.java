package   de  .  grogra  .  imp3d  .  gl20  ; 

import   de  .  grogra  .  math  .  ChannelMap  ; 

public   abstract   class   GL20ResourceShaderFragment   extends   GL20Resource  { 




public   static   final   int   GL20CHANNEL_POSITION_X  =  0  ; 




public   static   final   int   GL20CHANNEL_POSITION_Y  =  1  ; 




public   static   final   int   GL20CHANNEL_POSITION_Z  =  2  ; 




public   static   final   int   GL20CHANNEL_NORMAL_X  =  4  ; 




public   static   final   int   GL20CHANNEL_NORMAL_Y  =  5  ; 




public   static   final   int   GL20CHANNEL_NORMAL_Z  =  6  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_X_DU  =  8  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Y_DU  =  9  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Z_DU  =  10  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_X_DV  =  12  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Y_DV  =  13  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Z_DV  =  14  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_MIN  =  GL20CHANNEL_DERIVATE_X_DU  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_MAX  =  GL20CHANNEL_DERIVATE_Z_DV  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_U  =  16  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_V  =  17  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_W  =  18  ; 




public   static   final   int   GL20CHANNEL_X  =  20  ; 




public   static   final   int   GL20CHANNEL_Y  =  21  ; 




public   static   final   int   GL20CHANNEL_Z  =  22  ; 




public   static   final   int   GL20CHANNEL_R  =  24  ; 




public   static   final   int   GL20CHANNEL_G  =  25  ; 




public   static   final   int   GL20CHANNEL_B  =  26  ; 




public   static   final   int   GL20CHANNEL_A  =  27  ; 




public   static   final   int   GL20CHANNEL_POSITION_X_BIT  =  (  1  <<  GL20CHANNEL_POSITION_X  )  ; 




public   static   final   int   GL20CHANNEL_POSITION_Y_BIT  =  (  1  <<  GL20CHANNEL_POSITION_Y  )  ; 




public   static   final   int   GL20CHANNEL_POSITION_Z_BIT  =  (  1  <<  GL20CHANNEL_POSITION_Z  )  ; 




public   static   final   int   GL20CHANNEL_POSITION_BITS  =  (  GL20CHANNEL_POSITION_X_BIT  |  GL20CHANNEL_POSITION_Y_BIT  |  GL20CHANNEL_POSITION_Z_BIT  )  ; 




public   static   final   int   GL20CHANNEL_NORMAL_X_BIT  =  (  1  <<  GL20CHANNEL_NORMAL_X  )  ; 




public   static   final   int   GL20CHANNEL_NORMAL_Y_BIT  =  (  1  <<  GL20CHANNEL_NORMAL_Y  )  ; 




public   static   final   int   GL20CHANNEL_NORMAL_Z_BIT  =  (  1  <<  GL20CHANNEL_NORMAL_Z  )  ; 




public   static   final   int   GL20CHANNEL_NORMAL_BITS  =  (  GL20CHANNEL_NORMAL_X_BIT  |  GL20CHANNEL_NORMAL_Y_BIT  |  GL20CHANNEL_NORMAL_Z_BIT  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_X_DU_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_X_DU  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Y_DU_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_Y_DU  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Z_DU_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_Z_DU  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_DU_BITS  =  (  GL20CHANNEL_DERIVATE_X_DU_BIT  |  GL20CHANNEL_DERIVATE_Y_DU_BIT  |  GL20CHANNEL_DERIVATE_Z_DU_BIT  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_X_DV_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_X_DV  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Y_DV_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_Y_DV  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_Z_DV_BIT  =  (  1  <<  GL20CHANNEL_DERIVATE_Z_DV  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_DV_BITS  =  (  GL20CHANNEL_DERIVATE_X_DV_BIT  |  GL20CHANNEL_DERIVATE_Y_DV_BIT  |  GL20CHANNEL_DERIVATE_Z_DV_BIT  )  ; 




public   static   final   int   GL20CHANNEL_DERIVATE_BITS  =  (  GL20CHANNEL_DERIVATE_DU_BITS  |  GL20CHANNEL_DERIVATE_DV_BITS  )  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_U_BIT  =  (  1  <<  GL20CHANNEL_TEXCOORD_U  )  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_V_BIT  =  (  1  <<  GL20CHANNEL_TEXCOORD_V  )  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_W_BIT  =  (  1  <<  GL20CHANNEL_TEXCOORD_W  )  ; 




public   static   final   int   GL20CHANNEL_TEXCOORD_BITS  =  (  GL20CHANNEL_TEXCOORD_U_BIT  |  GL20CHANNEL_TEXCOORD_V_BIT  |  GL20CHANNEL_TEXCOORD_W_BIT  )  ; 




public   static   final   int   GL20CHANNEL_X_BIT  =  (  1  <<  GL20CHANNEL_X  )  ; 




public   static   final   int   GL20CHANNEL_Y_BIT  =  (  1  <<  GL20CHANNEL_Y  )  ; 




public   static   final   int   GL20CHANNEL_Z_BIT  =  (  1  <<  GL20CHANNEL_Z  )  ; 




public   static   final   int   GL20CHANNEL_XYZ_BITS  =  (  GL20CHANNEL_X_BIT  |  GL20CHANNEL_Y_BIT  |  GL20CHANNEL_Z_BIT  )  ; 




public   static   final   int   GL20CHANNEL_R_BIT  =  (  1  <<  GL20CHANNEL_R  )  ; 




public   static   final   int   GL20CHANNEL_G_BIT  =  (  1  <<  GL20CHANNEL_G  )  ; 




public   static   final   int   GL20CHANNEL_B_BIT  =  (  1  <<  GL20CHANNEL_B  )  ; 




public   static   final   int   GL20CHANNEL_A_BIT  =  (  1  <<  GL20CHANNEL_A  )  ; 




public   static   final   int   GL20CHANNEL_RGB_BITS  =  (  GL20CHANNEL_R_BIT  |  GL20CHANNEL_G_BIT  |  GL20CHANNEL_B_BIT  )  ; 




public   static   final   int   GL20CHANNEL_RGBA_BITS  =  (  GL20CHANNEL_RGB_BITS  |  GL20CHANNEL_A_BIT  )  ; 




private   static   final   int   CHANNEL_MAP  =  0x1  ; 




private   int   changeMask  =  GL20Const  .  ALL_CHANGED  ; 




private   ChannelMap   channelMap  =  null  ; 




private   int   channelMapStamp  =  -  1  ; 




private   int   userCount  =  0  ; 

protected   GL20ResourceShaderFragment  (  int   resourceClassType  )  { 
super  (  resourceClassType  )  ; 
assert  (  (  resourceClassType  &  GL20Resource  .  GL20RESOURCE_CLASS_MASK  )  ==  GL20Resource  .  GL20RESOURCE_CLASS_SHADER  )  ; 
} 







public   boolean   setChannelMap  (  ChannelMap   channelMap  )  { 
boolean   returnValue  =  false  ; 
if  (  this  .  channelMap  !=  channelMap  )  { 
this  .  channelMap  =  channelMap  ; 
changeMask  |=  CHANNEL_MAP  ; 
returnValue  =  true  ; 
}  else   returnValue  =  true  ; 
return   returnValue  ; 
} 






public   ChannelMap   getChannelMap  (  )  { 
return   channelMap  ; 
} 










public   int   getScalarIndex  (  GL20GLSLCode   code  ,  int   channel  )  { 
return  -  1  ; 
} 










public   int   getVector3Index  (  GL20GLSLCode   code  ,  int   startChannel  )  { 
return  -  1  ; 
} 










public   int   getVector4Index  (  GL20GLSLCode   code  ,  int   startChannel  )  { 
return  -  1  ; 
} 










public   abstract   boolean   fragmentAffectOnAlpha  (  )  ; 




public   final   void   registerUser  (  )  { 
userCount  ++  ; 
} 






public   final   void   unregisterUser  (  )  { 
userCount  --  ; 
if  (  userCount  ==  0  )  destroy  (  )  ; 
} 








public   boolean   isUpToDate  (  )  { 
if  (  channelMap  !=  null  )  if  (  channelMap  .  getStamp  (  )  !=  channelMapStamp  )  return   false  ; 
return   super  .  isUpToDate  (  )  ; 
} 




public   void   update  (  )  { 
if  (  channelMap  !=  null  )  channelMapStamp  =  channelMap  .  getStamp  (  )  ; 
super  .  update  (  )  ; 
} 





public   void   destroy  (  )  { 
} 
} 

