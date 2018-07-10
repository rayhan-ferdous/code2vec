package   com  .  eteks  .  sweethome3d  .  j3d  ; 

import   java  .  awt  .  EventQueue  ; 
import   java  .  awt  .  geom  .  Area  ; 
import   java  .  awt  .  geom  .  GeneralPath  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  lang  .  reflect  .  Constructor  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  lang  .  reflect  .  Modifier  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  WeakHashMap  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   javax  .  media  .  j3d  .  Appearance  ; 
import   javax  .  media  .  j3d  .  BoundingBox  ; 
import   javax  .  media  .  j3d  .  Bounds  ; 
import   javax  .  media  .  j3d  .  BranchGroup  ; 
import   javax  .  media  .  j3d  .  Geometry  ; 
import   javax  .  media  .  j3d  .  GeometryArray  ; 
import   javax  .  media  .  j3d  .  GeometryStripArray  ; 
import   javax  .  media  .  j3d  .  Group  ; 
import   javax  .  media  .  j3d  .  IndexedGeometryArray  ; 
import   javax  .  media  .  j3d  .  IndexedGeometryStripArray  ; 
import   javax  .  media  .  j3d  .  IndexedQuadArray  ; 
import   javax  .  media  .  j3d  .  IndexedTriangleArray  ; 
import   javax  .  media  .  j3d  .  IndexedTriangleFanArray  ; 
import   javax  .  media  .  j3d  .  IndexedTriangleStripArray  ; 
import   javax  .  media  .  j3d  .  Light  ; 
import   javax  .  media  .  j3d  .  Node  ; 
import   javax  .  media  .  j3d  .  QuadArray  ; 
import   javax  .  media  .  j3d  .  Shape3D  ; 
import   javax  .  media  .  j3d  .  Transform3D  ; 
import   javax  .  media  .  j3d  .  TransformGroup  ; 
import   javax  .  media  .  j3d  .  TransparencyAttributes  ; 
import   javax  .  media  .  j3d  .  TriangleArray  ; 
import   javax  .  media  .  j3d  .  TriangleFanArray  ; 
import   javax  .  media  .  j3d  .  TriangleStripArray  ; 
import   javax  .  vecmath  .  Matrix3f  ; 
import   javax  .  vecmath  .  Point3d  ; 
import   javax  .  vecmath  .  Point3f  ; 
import   javax  .  vecmath  .  Vector3d  ; 
import   javax  .  vecmath  .  Vector3f  ; 
import   com  .  eteks  .  sweethome3d  .  model  .  Content  ; 
import   com  .  eteks  .  sweethome3d  .  tools  .  TemporaryURLContent  ; 
import   com  .  eteks  .  sweethome3d  .  tools  .  URLContent  ; 
import   com  .  microcrowd  .  loader  .  java3d  .  max3ds  .  Loader3DS  ; 
import   com  .  sun  .  j3d  .  loaders  .  IncorrectFormatException  ; 
import   com  .  sun  .  j3d  .  loaders  .  Loader  ; 
import   com  .  sun  .  j3d  .  loaders  .  ParsingErrorException  ; 
import   com  .  sun  .  j3d  .  loaders  .  Scene  ; 
import   com  .  sun  .  j3d  .  loaders  .  lw3d  .  Lw3dLoader  ; 









public   class   ModelManager  { 




public   static   final   String   WINDOW_PANE_SHAPE_PREFIX  =  "sweethome3d_window_pane"  ; 




public   static   final   String   MIRROR_SHAPE_PREFIX  =  "sweethome3d_window_mirror"  ; 

private   static   final   TransparencyAttributes   WINDOW_PANE_TRANSPARENCY_ATTRIBUTES  =  new   TransparencyAttributes  (  TransparencyAttributes  .  NICEST  ,  0.5f  )  ; 

private   static   final   String   ADDITIONAL_LOADER_CLASSES  =  "com.eteks.sweethome3d.j3d.additionalLoaderClasses"  ; 

private   static   ModelManager   instance  ; 

private   Map  <  Content  ,  BranchGroup  >  loadedModelNodes  ; 

private   Map  <  Content  ,  List  <  ModelObserver  >  >  loadingModelObservers  ; 

private   ExecutorService   modelsLoader  ; 

private   Class  <  Loader  >  [  ]  additionalLoaderClasses  ; 

private   ModelManager  (  )  { 
this  .  loadedModelNodes  =  Collections  .  synchronizedMap  (  new   WeakHashMap  <  Content  ,  BranchGroup  >  (  )  )  ; 
this  .  loadingModelObservers  =  new   HashMap  <  Content  ,  List  <  ModelObserver  >  >  (  )  ; 
List  <  Class  <  Loader  >  >  loaderClasses  =  new   ArrayList  <  Class  <  Loader  >  >  (  )  ; 
String   loaderClassNames  =  System  .  getProperty  (  ADDITIONAL_LOADER_CLASSES  )  ; 
if  (  loaderClassNames  !=  null  )  { 
for  (  String   loaderClassName  :  loaderClassNames  .  split  (  "\\s|:"  )  )  { 
try  { 
loaderClasses  .  add  (  getLoaderClass  (  loaderClassName  )  )  ; 
}  catch  (  IllegalArgumentException   ex  )  { 
System  .  err  .  println  (  "Invalid loader class "  +  loaderClassName  +  ":\n"  +  ex  .  getMessage  (  )  )  ; 
} 
} 
} 
this  .  additionalLoaderClasses  =  loaderClasses  .  toArray  (  new   Class  [  loaderClasses  .  size  (  )  ]  )  ; 
} 




@  SuppressWarnings  (  "unchecked"  ) 
private   Class  <  Loader  >  getLoaderClass  (  String   loaderClassName  )  { 
try  { 
Class  <  Loader  >  loaderClass  =  (  Class  <  Loader  >  )  getClass  (  )  .  getClassLoader  (  )  .  loadClass  (  loaderClassName  )  ; 
if  (  !  Loader  .  class  .  isAssignableFrom  (  loaderClass  )  )  { 
throw   new   IllegalArgumentException  (  loaderClassName  +  " not a subclass of "  +  Loader  .  class  .  getName  (  )  )  ; 
}  else   if  (  Modifier  .  isAbstract  (  loaderClass  .  getModifiers  (  )  )  ||  !  Modifier  .  isPublic  (  loaderClass  .  getModifiers  (  )  )  )  { 
throw   new   IllegalArgumentException  (  loaderClassName  +  " not a public static class"  )  ; 
} 
Constructor  <  Loader  >  constructor  =  loaderClass  .  getConstructor  (  new   Class  [  0  ]  )  ; 
constructor  .  newInstance  (  new   Object  [  0  ]  )  ; 
return   loaderClass  ; 
}  catch  (  ClassNotFoundException   ex  )  { 
throw   new   IllegalArgumentException  (  ex  .  getMessage  (  )  ,  ex  )  ; 
}  catch  (  NoSuchMethodException   ex  )  { 
throw   new   IllegalArgumentException  (  ex  .  getMessage  (  )  ,  ex  )  ; 
}  catch  (  InvocationTargetException   ex  )  { 
throw   new   IllegalArgumentException  (  ex  .  getMessage  (  )  ,  ex  )  ; 
}  catch  (  IllegalAccessException   ex  )  { 
throw   new   IllegalArgumentException  (  loaderClassName  +  " constructor not accessible"  )  ; 
}  catch  (  InstantiationException   ex  )  { 
throw   new   IllegalArgumentException  (  loaderClassName  +  " not a public static class"  )  ; 
} 
} 




public   static   ModelManager   getInstance  (  )  { 
if  (  instance  ==  null  )  { 
instance  =  new   ModelManager  (  )  ; 
} 
return   instance  ; 
} 




public   void   clear  (  )  { 
if  (  this  .  modelsLoader  !=  null  )  { 
this  .  modelsLoader  .  shutdownNow  (  )  ; 
this  .  modelsLoader  =  null  ; 
} 
this  .  loadedModelNodes  .  clear  (  )  ; 
} 







public   Vector3f   getSize  (  Node   node  )  { 
BoundingBox   bounds  =  getBounds  (  node  )  ; 
Point3d   lower  =  new   Point3d  (  )  ; 
bounds  .  getLower  (  lower  )  ; 
Point3d   upper  =  new   Point3d  (  )  ; 
bounds  .  getUpper  (  upper  )  ; 
return   new   Vector3f  (  (  float  )  (  upper  .  x  -  lower  .  x  )  ,  (  float  )  (  upper  .  y  -  lower  .  y  )  ,  (  float  )  (  upper  .  z  -  lower  .  z  )  )  ; 
} 







public   BoundingBox   getBounds  (  Node   node  )  { 
BoundingBox   objectBounds  =  new   BoundingBox  (  new   Point3d  (  Double  .  POSITIVE_INFINITY  ,  Double  .  POSITIVE_INFINITY  ,  Double  .  POSITIVE_INFINITY  )  ,  new   Point3d  (  Double  .  NEGATIVE_INFINITY  ,  Double  .  NEGATIVE_INFINITY  ,  Double  .  NEGATIVE_INFINITY  )  )  ; 
computeBounds  (  node  ,  objectBounds  )  ; 
Point3d   lower  =  new   Point3d  (  )  ; 
objectBounds  .  getLower  (  lower  )  ; 
if  (  lower  .  x  ==  Double  .  POSITIVE_INFINITY  )  { 
throw   new   IllegalArgumentException  (  "Node has no bounds"  )  ; 
} 
return   objectBounds  ; 
} 

private   void   computeBounds  (  Node   node  ,  BoundingBox   bounds  )  { 
if  (  node   instanceof   Group  )  { 
Enumeration  <  ?  >  enumeration  =  (  (  Group  )  node  )  .  getAllChildren  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
computeBounds  (  (  Node  )  enumeration  .  nextElement  (  )  ,  bounds  )  ; 
} 
}  else   if  (  node   instanceof   Shape3D  )  { 
Bounds   shapeBounds  =  (  (  Shape3D  )  node  )  .  getBounds  (  )  ; 
bounds  .  combine  (  shapeBounds  )  ; 
} 
} 









public   TransformGroup   getNormalizedTransformGroup  (  Node   node  ,  float  [  ]  [  ]  modelRotation  ,  float   width  )  { 
BoundingBox   modelBounds  =  ModelManager  .  getInstance  (  )  .  getBounds  (  node  )  ; 
Point3d   lower  =  new   Point3d  (  )  ; 
modelBounds  .  getLower  (  lower  )  ; 
Point3d   upper  =  new   Point3d  (  )  ; 
modelBounds  .  getUpper  (  upper  )  ; 
Transform3D   translation  =  new   Transform3D  (  )  ; 
translation  .  setTranslation  (  new   Vector3d  (  -  lower  .  x  -  (  upper  .  x  -  lower  .  x  )  /  2  ,  -  lower  .  y  -  (  upper  .  y  -  lower  .  y  )  /  2  ,  -  lower  .  z  -  (  upper  .  z  -  lower  .  z  )  /  2  )  )  ; 
Transform3D   scaleOneTransform  =  new   Transform3D  (  )  ; 
scaleOneTransform  .  setScale  (  new   Vector3d  (  width  /  (  upper  .  x  -  lower  .  x  )  ,  width  /  (  upper  .  y  -  lower  .  y  )  ,  width  /  (  upper  .  z  -  lower  .  z  )  )  )  ; 
scaleOneTransform  .  mul  (  translation  )  ; 
Transform3D   modelTransform  =  new   Transform3D  (  )  ; 
if  (  modelRotation  !=  null  )  { 
Matrix3f   modelRotationMatrix  =  new   Matrix3f  (  modelRotation  [  0  ]  [  0  ]  ,  modelRotation  [  0  ]  [  1  ]  ,  modelRotation  [  0  ]  [  2  ]  ,  modelRotation  [  1  ]  [  0  ]  ,  modelRotation  [  1  ]  [  1  ]  ,  modelRotation  [  1  ]  [  2  ]  ,  modelRotation  [  2  ]  [  0  ]  ,  modelRotation  [  2  ]  [  1  ]  ,  modelRotation  [  2  ]  [  2  ]  )  ; 
modelTransform  .  setRotation  (  modelRotationMatrix  )  ; 
} 
modelTransform  .  mul  (  scaleOneTransform  )  ; 
return   new   TransformGroup  (  modelTransform  )  ; 
} 








public   void   loadModel  (  Content   content  ,  ModelObserver   modelObserver  )  { 
loadModel  (  content  ,  false  ,  modelObserver  )  ; 
} 











public   void   loadModel  (  final   Content   content  ,  boolean   synchronous  ,  ModelObserver   modelObserver  )  { 
BranchGroup   modelRoot  =  this  .  loadedModelNodes  .  get  (  content  )  ; 
if  (  modelRoot  ==  null  )  { 
if  (  synchronous  )  { 
try  { 
modelRoot  =  loadModel  (  content  )  ; 
}  catch  (  IOException   ex  )  { 
modelObserver  .  modelError  (  ex  )  ; 
} 
this  .  loadedModelNodes  .  put  (  content  ,  (  BranchGroup  )  modelRoot  )  ; 
}  else  { 
if  (  this  .  modelsLoader  ==  null  )  { 
this  .  modelsLoader  =  Executors  .  newFixedThreadPool  (  Runtime  .  getRuntime  (  )  .  availableProcessors  (  )  )  ; 
} 
List  <  ModelObserver  >  observers  =  this  .  loadingModelObservers  .  get  (  content  )  ; 
if  (  observers  !=  null  )  { 
observers  .  add  (  modelObserver  )  ; 
}  else  { 
observers  =  new   ArrayList  <  ModelObserver  >  (  )  ; 
observers  .  add  (  modelObserver  )  ; 
this  .  loadingModelObservers  .  put  (  content  ,  observers  )  ; 
this  .  modelsLoader  .  execute  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
final   BranchGroup   loadedModel  =  loadModel  (  content  )  ; 
loadedModelNodes  .  put  (  content  ,  loadedModel  )  ; 
EventQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
for  (  final   ModelObserver   observer  :  loadingModelObservers  .  remove  (  content  )  )  { 
BranchGroup   modelNode  =  (  BranchGroup  )  loadedModel  .  cloneTree  (  true  )  ; 
observer  .  modelUpdated  (  modelNode  )  ; 
} 
} 
}  )  ; 
}  catch  (  final   IOException   ex  )  { 
synchronized  (  loadedModelNodes  )  { 
EventQueue  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
for  (  final   ModelObserver   observer  :  loadingModelObservers  .  remove  (  content  )  )  { 
observer  .  modelError  (  ex  )  ; 
} 
} 
}  )  ; 
} 
} 
} 
}  )  ; 
} 
return  ; 
} 
} 
modelObserver  .  modelUpdated  (  (  BranchGroup  )  modelRoot  .  cloneTree  (  true  )  )  ; 
} 






public   BranchGroup   loadModel  (  Content   content  )  throws   IOException  { 
URLContent   urlContent  ; 
if  (  content   instanceof   URLContent  )  { 
urlContent  =  (  URLContent  )  content  ; 
}  else  { 
urlContent  =  TemporaryURLContent  .  copyToTemporaryURLContent  (  content  )  ; 
} 
Loader3DS   loader3DSWithNoStackTraces  =  new   Loader3DS  (  )  { 

@  Override 
public   Scene   load  (  URL   url  )  throws   FileNotFoundException  { 
PrintStream   defaultSystemErrorStream  =  System  .  err  ; 
try  { 
System  .  setErr  (  new   PrintStream  (  new   OutputStream  (  )  { 

@  Override 
public   void   write  (  int   b  )  throws   IOException  { 
} 
}  )  )  ; 
return   super  .  load  (  url  )  ; 
}  finally  { 
System  .  setErr  (  defaultSystemErrorStream  )  ; 
} 
} 
}  ; 
Loader  [  ]  defaultLoaders  =  new   Loader  [  ]  {  new   OBJLoader  (  )  ,  loader3DSWithNoStackTraces  ,  new   Lw3dLoader  (  )  }  ; 
Loader  [  ]  loaders  =  new   Loader  [  defaultLoaders  .  length  +  this  .  additionalLoaderClasses  .  length  ]  ; 
System  .  arraycopy  (  defaultLoaders  ,  0  ,  loaders  ,  0  ,  defaultLoaders  .  length  )  ; 
for  (  int   i  =  0  ;  i  <  this  .  additionalLoaderClasses  .  length  ;  i  ++  )  { 
try  { 
loaders  [  defaultLoaders  .  length  +  i  ]  =  this  .  additionalLoaderClasses  [  i  ]  .  newInstance  (  )  ; 
}  catch  (  InstantiationException   ex  )  { 
throw   new   InternalError  (  ex  .  getMessage  (  )  )  ; 
}  catch  (  IllegalAccessException   ex  )  { 
throw   new   InternalError  (  ex  .  getMessage  (  )  )  ; 
} 
} 
Exception   lastException  =  null  ; 
for  (  Loader   loader  :  loaders  )  { 
try  { 
loader  .  setFlags  (  loader  .  getFlags  (  )  &  ~  (  Loader  .  LOAD_LIGHT_NODES  |  Loader  .  LOAD_FOG_NODES  |  Loader  .  LOAD_BACKGROUND_NODES  |  Loader  .  LOAD_VIEW_GROUPS  )  )  ; 
Scene   scene  =  loader  .  load  (  urlContent  .  getURL  (  )  )  ; 
BranchGroup   modelNode  =  scene  .  getSceneGroup  (  )  ; 
if  (  modelNode  .  numChildren  (  )  ==  0  )  { 
throw   new   IllegalArgumentException  (  "Empty model"  )  ; 
} 
updateShapeNamesAndWindowPanesTransparency  (  scene  )  ; 
turnOffLightsAndAllowBoundsRead  (  modelNode  )  ; 
return   modelNode  ; 
}  catch  (  IllegalArgumentException   ex  )  { 
lastException  =  ex  ; 
}  catch  (  IncorrectFormatException   ex  )  { 
lastException  =  ex  ; 
}  catch  (  ParsingErrorException   ex  )  { 
lastException  =  ex  ; 
}  catch  (  IOException   ex  )  { 
lastException  =  ex  ; 
}  catch  (  RuntimeException   ex  )  { 
if  (  ex  .  getClass  (  )  .  getName  (  )  .  equals  (  "com.sun.j3d.utils.image.ImageException"  )  )  { 
lastException  =  ex  ; 
}  else  { 
throw   ex  ; 
} 
} 
} 
if  (  lastException   instanceof   IncorrectFormatException  )  { 
IOException   incorrectFormatException  =  new   IOException  (  "Incorrect format"  )  ; 
incorrectFormatException  .  initCause  (  lastException  )  ; 
throw   incorrectFormatException  ; 
}  else   if  (  lastException   instanceof   ParsingErrorException  )  { 
IOException   incorrectFormatException  =  new   IOException  (  "Parsing error"  )  ; 
incorrectFormatException  .  initCause  (  lastException  )  ; 
throw   incorrectFormatException  ; 
}  else  { 
throw  (  IOException  )  lastException  ; 
} 
} 




private   void   updateShapeNamesAndWindowPanesTransparency  (  Scene   scene  )  { 
Map  <  String  ,  Object  >  namedObjects  =  scene  .  getNamedObjects  (  )  ; 
for  (  Map  .  Entry  <  String  ,  Object  >  entry  :  namedObjects  .  entrySet  (  )  )  { 
if  (  entry  .  getValue  (  )  instanceof   Shape3D  )  { 
Shape3D   shape  =  (  Shape3D  )  entry  .  getValue  (  )  ; 
shape  .  setUserData  (  entry  .  getKey  (  )  )  ; 
if  (  entry  .  getKey  (  )  .  startsWith  (  WINDOW_PANE_SHAPE_PREFIX  )  )  { 
Appearance   appearance  =  shape  .  getAppearance  (  )  ; 
if  (  appearance  ==  null  )  { 
appearance  =  new   Appearance  (  )  ; 
shape  .  setAppearance  (  appearance  )  ; 
} 
if  (  appearance  .  getTransparencyAttributes  (  )  ==  null  )  { 
appearance  .  setTransparencyAttributes  (  WINDOW_PANE_TRANSPARENCY_ATTRIBUTES  )  ; 
} 
} 
} 
} 
} 





private   void   turnOffLightsAndAllowBoundsRead  (  Node   node  )  { 
if  (  node   instanceof   Group  )  { 
Enumeration  <  ?  >  enumeration  =  (  (  Group  )  node  )  .  getAllChildren  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
turnOffLightsAndAllowBoundsRead  (  (  Node  )  enumeration  .  nextElement  (  )  )  ; 
} 
}  else   if  (  node   instanceof   Light  )  { 
(  (  Light  )  node  )  .  setEnable  (  false  )  ; 
}  else   if  (  node   instanceof   Shape3D  )  { 
node  .  setCapability  (  Shape3D  .  ALLOW_BOUNDS_READ  )  ; 
} 
} 





public   Area   getAreaOnFloor  (  Node   node  )  { 
Area   modelAreaOnFloor  =  new   Area  (  )  ; 
computeAreaOnFloor  (  node  ,  node  ,  modelAreaOnFloor  )  ; 
return   modelAreaOnFloor  ; 
} 




private   void   computeAreaOnFloor  (  Node   parent  ,  Node   node  ,  Area   nodeArea  )  { 
if  (  node   instanceof   Group  )  { 
Enumeration  <  ?  >  enumeration  =  (  (  Group  )  node  )  .  getAllChildren  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
computeAreaOnFloor  (  parent  ,  (  Node  )  enumeration  .  nextElement  (  )  ,  nodeArea  )  ; 
} 
}  else   if  (  node   instanceof   Shape3D  )  { 
Shape3D   shape  =  (  Shape3D  )  node  ; 
Transform3D   transformationToParent  =  getTransformationToParent  (  parent  ,  node  )  ; 
for  (  int   i  =  0  ,  n  =  shape  .  numGeometries  (  )  ;  i  <  n  ;  i  ++  )  { 
computeGeometryAreaOnFloor  (  shape  .  getGeometry  (  i  )  ,  transformationToParent  ,  nodeArea  )  ; 
} 
} 
} 





private   Transform3D   getTransformationToParent  (  Node   parent  ,  Node   child  )  { 
Transform3D   transform  =  new   Transform3D  (  )  ; 
if  (  child   instanceof   TransformGroup  )  { 
(  (  TransformGroup  )  child  )  .  getTransform  (  transform  )  ; 
} 
if  (  child  !=  parent  )  { 
Transform3D   parentTransform  =  getTransformationToParent  (  parent  ,  child  .  getParent  (  )  )  ; 
parentTransform  .  mul  (  transform  )  ; 
return   parentTransform  ; 
}  else  { 
return   transform  ; 
} 
} 




private   void   computeGeometryAreaOnFloor  (  Geometry   geometry  ,  Transform3D   transformationToParent  ,  Area   nodeArea  )  { 
if  (  geometry   instanceof   GeometryArray  )  { 
GeometryArray   geometryArray  =  (  GeometryArray  )  geometry  ; 
int   vertexCount  =  geometryArray  .  getVertexCount  (  )  ; 
float  [  ]  vertices  =  new   float  [  vertexCount  *  2  ]  ; 
Point3f   vertex  =  new   Point3f  (  )  ; 
if  (  (  geometryArray  .  getVertexFormat  (  )  &  GeometryArray  .  BY_REFERENCE  )  !=  0  )  { 
if  (  (  geometryArray  .  getVertexFormat  (  )  &  GeometryArray  .  INTERLEAVED  )  !=  0  )  { 
float  [  ]  vertexData  =  geometryArray  .  getInterleavedVertices  (  )  ; 
int   vertexSize  =  vertexData  .  length  /  vertexCount  ; 
for  (  int   index  =  0  ,  i  =  vertexSize  -  3  ;  index  <  vertices  .  length  ;  i  +=  vertexSize  )  { 
vertex  .  x  =  vertexData  [  i  ]  ; 
vertex  .  y  =  vertexData  [  i  +  1  ]  ; 
vertex  .  z  =  vertexData  [  i  +  2  ]  ; 
transformationToParent  .  transform  (  vertex  )  ; 
vertices  [  index  ++  ]  =  vertex  .  x  ; 
vertices  [  index  ++  ]  =  vertex  .  z  ; 
} 
}  else  { 
float  [  ]  vertexCoordinates  =  geometryArray  .  getCoordRefFloat  (  )  ; 
for  (  int   index  =  0  ,  i  =  0  ;  index  <  vertices  .  length  ;  i  +=  3  )  { 
vertex  .  x  =  vertexCoordinates  [  i  ]  ; 
vertex  .  y  =  vertexCoordinates  [  i  +  1  ]  ; 
vertex  .  z  =  vertexCoordinates  [  i  +  2  ]  ; 
transformationToParent  .  transform  (  vertex  )  ; 
vertices  [  index  ++  ]  =  vertex  .  x  ; 
vertices  [  index  ++  ]  =  vertex  .  z  ; 
} 
} 
}  else  { 
for  (  int   index  =  0  ,  i  =  0  ;  index  <  vertices  .  length  ;  i  ++  )  { 
geometryArray  .  getCoordinate  (  i  ,  vertex  )  ; 
transformationToParent  .  transform  (  vertex  )  ; 
vertices  [  index  ++  ]  =  vertex  .  x  ; 
vertices  [  index  ++  ]  =  vertex  .  z  ; 
} 
} 
GeneralPath   geometryPath  =  null  ; 
if  (  geometryArray   instanceof   IndexedGeometryArray  )  { 
if  (  geometryArray   instanceof   IndexedTriangleArray  )  { 
IndexedTriangleArray   triangleArray  =  (  IndexedTriangleArray  )  geometryArray  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
for  (  int   i  =  0  ,  triangleIndex  =  0  ,  n  =  triangleArray  .  getIndexCount  (  )  ;  i  <  n  ;  i  +=  3  )  { 
addIndexedTriangleToPath  (  triangleArray  ,  i  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
}  else   if  (  geometryArray   instanceof   IndexedQuadArray  )  { 
IndexedQuadArray   quadArray  =  (  IndexedQuadArray  )  geometryArray  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
for  (  int   i  =  0  ,  quadrilateralIndex  =  0  ,  n  =  quadArray  .  getIndexCount  (  )  ;  i  <  n  ;  i  +=  4  )  { 
addIndexedQuadrilateralToPath  (  quadArray  ,  i  ,  i  +  1  ,  i  +  2  ,  i  +  3  ,  vertices  ,  geometryPath  ,  quadrilateralIndex  ++  ,  nodeArea  )  ; 
} 
}  else   if  (  geometryArray   instanceof   IndexedGeometryStripArray  )  { 
IndexedGeometryStripArray   geometryStripArray  =  (  IndexedGeometryStripArray  )  geometryArray  ; 
int  [  ]  stripIndexCounts  =  new   int  [  geometryStripArray  .  getNumStrips  (  )  ]  ; 
geometryStripArray  .  getStripIndexCounts  (  stripIndexCounts  )  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
int   initialIndex  =  0  ; 
if  (  geometryStripArray   instanceof   IndexedTriangleStripArray  )  { 
for  (  int   strip  =  0  ,  triangleIndex  =  0  ;  strip  <  stripIndexCounts  .  length  ;  strip  ++  )  { 
for  (  int   i  =  initialIndex  ,  n  =  initialIndex  +  stripIndexCounts  [  strip  ]  -  2  ,  j  =  0  ;  i  <  n  ;  i  ++  ,  j  ++  )  { 
if  (  j  %  2  ==  0  )  { 
addIndexedTriangleToPath  (  geometryStripArray  ,  i  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
}  else  { 
addIndexedTriangleToPath  (  geometryStripArray  ,  i  ,  i  +  2  ,  i  +  1  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
} 
initialIndex  +=  stripIndexCounts  [  strip  ]  ; 
} 
}  else   if  (  geometryStripArray   instanceof   IndexedTriangleFanArray  )  { 
for  (  int   strip  =  0  ,  triangleIndex  =  0  ;  strip  <  stripIndexCounts  .  length  ;  strip  ++  )  { 
for  (  int   i  =  initialIndex  ,  n  =  initialIndex  +  stripIndexCounts  [  strip  ]  -  2  ;  i  <  n  ;  i  ++  )  { 
addIndexedTriangleToPath  (  geometryStripArray  ,  initialIndex  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
initialIndex  +=  stripIndexCounts  [  strip  ]  ; 
} 
} 
} 
}  else  { 
if  (  geometryArray   instanceof   TriangleArray  )  { 
TriangleArray   triangleArray  =  (  TriangleArray  )  geometryArray  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
for  (  int   i  =  0  ,  triangleIndex  =  0  ;  i  <  vertexCount  ;  i  +=  3  )  { 
addTriangleToPath  (  triangleArray  ,  i  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
}  else   if  (  geometryArray   instanceof   QuadArray  )  { 
QuadArray   quadArray  =  (  QuadArray  )  geometryArray  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
for  (  int   i  =  0  ,  quadrilateralIndex  =  0  ;  i  <  vertexCount  ;  i  +=  4  )  { 
addQuadrilateralToPath  (  quadArray  ,  i  ,  i  +  1  ,  i  +  2  ,  i  +  3  ,  vertices  ,  geometryPath  ,  quadrilateralIndex  ++  ,  nodeArea  )  ; 
} 
}  else   if  (  geometryArray   instanceof   GeometryStripArray  )  { 
GeometryStripArray   geometryStripArray  =  (  GeometryStripArray  )  geometryArray  ; 
int  [  ]  stripVertexCounts  =  new   int  [  geometryStripArray  .  getNumStrips  (  )  ]  ; 
geometryStripArray  .  getStripVertexCounts  (  stripVertexCounts  )  ; 
geometryPath  =  new   GeneralPath  (  GeneralPath  .  WIND_NON_ZERO  ,  1000  )  ; 
int   initialIndex  =  0  ; 
if  (  geometryStripArray   instanceof   TriangleStripArray  )  { 
for  (  int   strip  =  0  ,  triangleIndex  =  0  ;  strip  <  stripVertexCounts  .  length  ;  strip  ++  )  { 
for  (  int   i  =  initialIndex  ,  n  =  initialIndex  +  stripVertexCounts  [  strip  ]  -  2  ,  j  =  0  ;  i  <  n  ;  i  ++  ,  j  ++  )  { 
if  (  j  %  2  ==  0  )  { 
addTriangleToPath  (  geometryStripArray  ,  i  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
}  else  { 
addTriangleToPath  (  geometryStripArray  ,  i  ,  i  +  2  ,  i  +  1  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
} 
initialIndex  +=  stripVertexCounts  [  strip  ]  ; 
} 
}  else   if  (  geometryStripArray   instanceof   TriangleFanArray  )  { 
for  (  int   strip  =  0  ,  triangleIndex  =  0  ;  strip  <  stripVertexCounts  .  length  ;  strip  ++  )  { 
for  (  int   i  =  initialIndex  ,  n  =  initialIndex  +  stripVertexCounts  [  strip  ]  -  2  ;  i  <  n  ;  i  ++  )  { 
addTriangleToPath  (  geometryStripArray  ,  initialIndex  ,  i  +  1  ,  i  +  2  ,  vertices  ,  geometryPath  ,  triangleIndex  ++  ,  nodeArea  )  ; 
} 
initialIndex  +=  stripVertexCounts  [  strip  ]  ; 
} 
} 
} 
} 
if  (  geometryPath  !=  null  )  { 
nodeArea  .  add  (  new   Area  (  geometryPath  )  )  ; 
} 
} 
} 





private   void   addIndexedTriangleToPath  (  IndexedGeometryArray   geometryArray  ,  int   vertexIndex1  ,  int   vertexIndex2  ,  int   vertexIndex3  ,  float  [  ]  vertices  ,  GeneralPath   geometryPath  ,  int   triangleIndex  ,  Area   nodeArea  )  { 
addTriangleToPath  (  geometryArray  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex1  )  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex2  )  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex3  )  ,  vertices  ,  geometryPath  ,  triangleIndex  ,  nodeArea  )  ; 
} 





private   void   addIndexedQuadrilateralToPath  (  IndexedGeometryArray   geometryArray  ,  int   vertexIndex1  ,  int   vertexIndex2  ,  int   vertexIndex3  ,  int   vertexIndex4  ,  float  [  ]  vertices  ,  GeneralPath   geometryPath  ,  int   quadrilateralIndex  ,  Area   nodeArea  )  { 
addQuadrilateralToPath  (  geometryArray  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex1  )  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex2  )  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex3  )  ,  geometryArray  .  getCoordinateIndex  (  vertexIndex4  )  ,  vertices  ,  geometryPath  ,  quadrilateralIndex  ,  nodeArea  )  ; 
} 






private   void   addTriangleToPath  (  GeometryArray   geometryArray  ,  int   vertexIndex1  ,  int   vertexIndex2  ,  int   vertexIndex3  ,  float  [  ]  vertices  ,  GeneralPath   geometryPath  ,  int   triangleIndex  ,  Area   nodeArea  )  { 
float   xVertex1  =  vertices  [  2  *  vertexIndex1  ]  ; 
float   yVertex1  =  vertices  [  2  *  vertexIndex1  +  1  ]  ; 
float   xVertex2  =  vertices  [  2  *  vertexIndex2  ]  ; 
float   yVertex2  =  vertices  [  2  *  vertexIndex2  +  1  ]  ; 
float   xVertex3  =  vertices  [  2  *  vertexIndex3  ]  ; 
float   yVertex3  =  vertices  [  2  *  vertexIndex3  +  1  ]  ; 
if  (  (  xVertex2  -  xVertex1  )  *  (  yVertex3  -  yVertex2  )  -  (  yVertex2  -  yVertex1  )  *  (  xVertex3  -  xVertex2  )  >  0  )  { 
if  (  triangleIndex  >  0  &&  triangleIndex  %  1000  ==  0  )  { 
nodeArea  .  add  (  new   Area  (  geometryPath  )  )  ; 
geometryPath  .  reset  (  )  ; 
} 
geometryPath  .  moveTo  (  xVertex1  ,  yVertex1  )  ; 
geometryPath  .  lineTo  (  xVertex2  ,  yVertex2  )  ; 
geometryPath  .  lineTo  (  xVertex3  ,  yVertex3  )  ; 
geometryPath  .  closePath  (  )  ; 
} 
} 






private   void   addQuadrilateralToPath  (  GeometryArray   geometryArray  ,  int   vertexIndex1  ,  int   vertexIndex2  ,  int   vertexIndex3  ,  int   vertexIndex4  ,  float  [  ]  vertices  ,  GeneralPath   geometryPath  ,  int   quadrilateralIndex  ,  Area   nodeArea  )  { 
float   xVertex1  =  vertices  [  2  *  vertexIndex1  ]  ; 
float   yVertex1  =  vertices  [  2  *  vertexIndex1  +  1  ]  ; 
float   xVertex2  =  vertices  [  2  *  vertexIndex2  ]  ; 
float   yVertex2  =  vertices  [  2  *  vertexIndex2  +  1  ]  ; 
float   xVertex3  =  vertices  [  2  *  vertexIndex3  ]  ; 
float   yVertex3  =  vertices  [  2  *  vertexIndex3  +  1  ]  ; 
if  (  (  xVertex2  -  xVertex1  )  *  (  yVertex3  -  yVertex2  )  -  (  yVertex2  -  yVertex1  )  *  (  xVertex3  -  xVertex2  )  >  0  )  { 
if  (  quadrilateralIndex  >  0  &&  quadrilateralIndex  %  1000  ==  0  )  { 
nodeArea  .  add  (  new   Area  (  geometryPath  )  )  ; 
geometryPath  .  reset  (  )  ; 
} 
geometryPath  .  moveTo  (  xVertex1  ,  yVertex1  )  ; 
geometryPath  .  lineTo  (  xVertex2  ,  yVertex2  )  ; 
geometryPath  .  lineTo  (  xVertex3  ,  yVertex3  )  ; 
geometryPath  .  lineTo  (  vertices  [  2  *  vertexIndex4  ]  ,  vertices  [  2  *  vertexIndex4  +  1  ]  )  ; 
geometryPath  .  closePath  (  )  ; 
} 
} 




public   static   interface   ModelObserver  { 

public   void   modelUpdated  (  BranchGroup   modelRoot  )  ; 

public   void   modelError  (  Exception   ex  )  ; 
} 
} 

