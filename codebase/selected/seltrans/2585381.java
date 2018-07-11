package   ptplot  ; 

import   java  .  awt  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  lang  .  *  ; 
















































































public   class   PlotBox   extends   Panel  { 







public   boolean   action  (  Event   evt  ,  Object   arg  )  { 
if  (  evt  .  target  ==  _fillButton  )  { 
fillPlot  (  _graphics  )  ; 
return   true  ; 
}  else  { 
return   super  .  action  (  evt  ,  arg  )  ; 
} 
} 




public   void   addNotify  (  )  { 
super  .  addNotify  (  )  ; 
_measureFonts  (  )  ; 
} 







public   void   addLegend  (  int   dataset  ,  String   legend  )  { 
if  (  _debug  >  8  )  System  .  out  .  println  (  "PlotBox addLegend: "  +  dataset  +  " "  +  legend  )  ; 
_legendStrings  .  addElement  (  legend  )  ; 
_legendDatasets  .  addElement  (  new   Integer  (  dataset  )  )  ; 
} 








public   void   addXTick  (  String   label  ,  double   position  )  { 
if  (  _xticks  ==  null  )  { 
_xticks  =  new   Vector  (  )  ; 
_xticklabels  =  new   Vector  (  )  ; 
} 
_xticks  .  addElement  (  new   Double  (  position  )  )  ; 
_xticklabels  .  addElement  (  label  )  ; 
} 








public   void   addYTick  (  String   label  ,  double   position  )  { 
if  (  _yticks  ==  null  )  { 
_yticks  =  new   Vector  (  )  ; 
_yticklabels  =  new   Vector  (  )  ; 
} 
_yticks  .  addElement  (  new   Double  (  position  )  )  ; 
_yticklabels  .  addElement  (  label  )  ; 
} 





public   synchronized   void   drawPlot  (  Graphics   graphics  ,  boolean   clearfirst  )  { 
if  (  _debug  >  7  )  System  .  out  .  println  (  "PlotBox: drawPlot"  +  graphics  +  " "  +  clearfirst  )  ; 
if  (  graphics  ==  null  )  { 
System  .  out  .  println  (  "Attempt to draw axes without "  +  "a Graphics object."  )  ; 
return  ; 
} 
Rectangle   drawRect  =  bounds  (  )  ; 
graphics  .  setPaintMode  (  )  ; 
if  (  clearfirst  )  { 
graphics  .  clearRect  (  0  ,  0  ,  drawRect  .  width  ,  drawRect  .  height  )  ; 
} 
if  (  _debug  >  8  )  { 
System  .  out  .  println  (  "PlotBox: drawPlot drawRect ="  +  drawRect  .  width  +  " "  +  drawRect  .  height  +  " "  +  drawRect  .  x  +  " "  +  drawRect  .  y  )  ; 
graphics  .  drawRect  (  0  ,  0  ,  drawRect  .  width  ,  drawRect  .  height  )  ; 
} 
if  (  _errorMsg  !=  null  )  { 
int   fheight  =  _labelFontMetrics  .  getHeight  (  )  +  2  ; 
int   msgy  =  fheight  ; 
graphics  .  setColor  (  Color  .  black  )  ; 
for  (  int   i  =  0  ;  i  <  _errorMsg  .  length  ;  i  ++  )  { 
graphics  .  drawString  (  _errorMsg  [  i  ]  ,  10  ,  msgy  )  ; 
msgy  +=  fheight  ; 
} 
return  ; 
} 
if  (  !  _xRangeGiven  )  { 
if  (  _xBottom  >  _xTop  )  { 
_setXRange  (  0  ,  0  )  ; 
}  else  { 
_setXRange  (  _xBottom  ,  _xTop  )  ; 
} 
} 
if  (  !  _yRangeGiven  )  { 
if  (  _yBottom  >  _yTop  )  { 
_setYRange  (  0  ,  0  )  ; 
}  else  { 
_setYRange  (  _yBottom  ,  _yTop  )  ; 
} 
} 
int   titley  =  0  ; 
int   titlefontheight  =  _titleFontMetrics  .  getHeight  (  )  ; 
if  (  _title  !=  null  ||  _yExp  !=  0  )  { 
titley  =  titlefontheight  +  _topPadding  ; 
} 
graphics  .  setFont  (  _labelfont  )  ; 
int   labelheight  =  _labelFontMetrics  .  getHeight  (  )  ; 
int   halflabelheight  =  labelheight  /  2  ; 
int   ySPos  =  drawRect  .  height  -  5  ; 
if  (  _xExp  !=  0  &&  _xticks  ==  null  )  { 
int   xSPos  =  drawRect  .  width  -  _rightPadding  ; 
String   superscript  =  Integer  .  toString  (  _xExp  )  ; 
xSPos  -=  _superscriptFontMetrics  .  stringWidth  (  superscript  )  ; 
graphics  .  setFont  (  _superscriptfont  )  ; 
graphics  .  drawString  (  superscript  ,  xSPos  ,  ySPos  -  halflabelheight  )  ; 
xSPos  -=  _labelFontMetrics  .  stringWidth  (  "x10"  )  ; 
graphics  .  setFont  (  _labelfont  )  ; 
graphics  .  drawString  (  "x10"  ,  xSPos  ,  ySPos  )  ; 
_bottomPadding  =  (  3  *  labelheight  )  /  2  +  5  ; 
} 
if  (  _xlabel  !=  null  &&  _bottomPadding  <  labelheight  +  5  )  { 
_bottomPadding  =  titlefontheight  +  5  ; 
} 
_uly  =  titley  +  5  ; 
_lry  =  drawRect  .  height  -  labelheight  -  _bottomPadding  -  3  ; 
int   height  =  _lry  -  _uly  ; 
_yscale  =  height  /  (  _yMax  -  _yMin  )  ; 
_ytickscale  =  height  /  (  _ytickMax  -  _ytickMin  )  ; 
int   ny  =  2  +  height  /  (  labelheight  +  10  )  ; 
double   yStep  =  _roundUp  (  (  _ytickMax  -  _ytickMin  )  /  (  double  )  ny  )  ; 
double   yStart  =  yStep  *  Math  .  ceil  (  _ytickMin  /  yStep  )  ; 
int   widesty  =  0  ; 
String   ylabels  [  ]  =  new   String  [  ny  ]  ; 
int   ylabwidth  [  ]  =  new   int  [  ny  ]  ; 
int   ind  =  0  ; 
if  (  _yticks  ==  null  )  { 
int   numfracdigits  =  _numFracDigits  (  yStep  )  ; 
for  (  double   ypos  =  yStart  ;  ypos  <=  _ytickMax  ;  ypos  +=  yStep  )  { 
if  (  ind  >=  ny  )  break  ; 
String   yl  =  _formatNum  (  ypos  ,  numfracdigits  )  ; 
ylabels  [  ind  ]  =  yl  ; 
int   lw  =  _labelFontMetrics  .  stringWidth  (  yl  )  ; 
ylabwidth  [  ind  ++  ]  =  lw  ; 
if  (  lw  >  widesty  )  { 
widesty  =  lw  ; 
} 
} 
}  else  { 
Enumeration   nl  =  _yticklabels  .  elements  (  )  ; 
while  (  nl  .  hasMoreElements  (  )  )  { 
String   label  =  (  String  )  nl  .  nextElement  (  )  ; 
int   lw  =  _labelFontMetrics  .  stringWidth  (  label  )  ; 
if  (  lw  >  widesty  )  { 
widesty  =  lw  ; 
} 
} 
} 
if  (  _ylabel  !=  null  )  { 
_ulx  =  widesty  +  _labelFontMetrics  .  stringWidth  (  "W"  )  +  _leftPadding  ; 
}  else  { 
_ulx  =  widesty  +  _leftPadding  ; 
} 
int   legendwidth  =  _drawLegend  (  graphics  ,  drawRect  .  width  -  _rightPadding  ,  _uly  )  ; 
_lrx  =  drawRect  .  width  -  legendwidth  -  _rightPadding  ; 
int   width  =  _lrx  -  _ulx  ; 
_xscale  =  width  /  (  _xMax  -  _xMin  )  ; 
_xtickscale  =  width  /  (  _xtickMax  -  _xtickMin  )  ; 
if  (  _debug  >  8  )  { 
System  .  out  .  println  (  "PlotBox: drawPlot _ulx "  +  _ulx  +  " "  +  _uly  +  " "  +  _lrx  +  " "  +  _lry  +  " "  +  width  +  " "  +  height  )  ; 
} 
if  (  _background  ==  null  )  { 
throw   new   Error  (  "PlotBox.drawPlot(): _background == null\n"  +  "Be sure to call init() before calling paint()."  )  ; 
} 
graphics  .  setColor  (  _background  )  ; 
graphics  .  fillRect  (  _ulx  ,  _uly  ,  width  ,  height  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
graphics  .  drawRect  (  _ulx  ,  _uly  ,  width  ,  height  )  ; 
int   tickLength  =  5  ; 
int   xCoord1  =  _ulx  +  tickLength  ; 
int   xCoord2  =  _lrx  -  tickLength  ; 
if  (  _yticks  ==  null  )  { 
ind  =  0  ; 
for  (  double   ypos  =  yStart  ;  ypos  <=  _ytickMax  ;  ypos  +=  yStep  )  { 
if  (  ind  >=  ny  )  break  ; 
int   yCoord1  =  _lry  -  (  int  )  (  (  ypos  -  _ytickMin  )  *  _ytickscale  )  ; 
int   offset  =  0  ; 
if  (  ind  >  0  )  offset  =  halflabelheight  ; 
graphics  .  drawLine  (  _ulx  ,  yCoord1  ,  xCoord1  ,  yCoord1  )  ; 
graphics  .  drawLine  (  _lrx  ,  yCoord1  ,  xCoord2  ,  yCoord1  )  ; 
if  (  _grid  &&  yCoord1  !=  _uly  &&  yCoord1  !=  _lry  )  { 
graphics  .  setColor  (  Color  .  lightGray  )  ; 
graphics  .  drawLine  (  xCoord1  ,  yCoord1  ,  xCoord2  ,  yCoord1  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
} 
graphics  .  drawString  (  ylabels  [  ind  ]  ,  _ulx  -  ylabwidth  [  ind  ++  ]  -  4  ,  yCoord1  +  offset  )  ; 
} 
if  (  _yExp  !=  0  )  { 
graphics  .  drawString  (  "x10"  ,  2  ,  titley  )  ; 
graphics  .  setFont  (  _superscriptfont  )  ; 
graphics  .  drawString  (  Integer  .  toString  (  _yExp  )  ,  _labelFontMetrics  .  stringWidth  (  "x10"  )  +  2  ,  titley  -  halflabelheight  )  ; 
graphics  .  setFont  (  _labelfont  )  ; 
} 
}  else  { 
Enumeration   nt  =  _yticks  .  elements  (  )  ; 
Enumeration   nl  =  _yticklabels  .  elements  (  )  ; 
while  (  nl  .  hasMoreElements  (  )  )  { 
String   label  =  (  String  )  nl  .  nextElement  (  )  ; 
double   ypos  =  (  (  Double  )  (  nt  .  nextElement  (  )  )  )  .  doubleValue  (  )  ; 
if  (  ypos  >  _yMax  ||  ypos  <  _yMin  )  continue  ; 
int   yCoord1  =  _lry  -  (  int  )  (  (  ypos  -  _yMin  )  *  _yscale  )  ; 
int   offset  =  0  ; 
if  (  ypos  <  _lry  -  labelheight  )  offset  =  halflabelheight  ; 
graphics  .  drawLine  (  _ulx  ,  yCoord1  ,  xCoord1  ,  yCoord1  )  ; 
graphics  .  drawLine  (  _lrx  ,  yCoord1  ,  xCoord2  ,  yCoord1  )  ; 
if  (  _grid  &&  yCoord1  !=  _uly  &&  yCoord1  !=  _lry  )  { 
graphics  .  setColor  (  Color  .  lightGray  )  ; 
graphics  .  drawLine  (  xCoord1  ,  yCoord1  ,  xCoord2  ,  yCoord1  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
} 
graphics  .  drawString  (  label  ,  _ulx  -  _labelFontMetrics  .  stringWidth  (  label  )  -  3  ,  yCoord1  +  offset  )  ; 
} 
} 
int   yCoord1  =  _uly  +  tickLength  ; 
int   yCoord2  =  _lry  -  tickLength  ; 
if  (  _xticks  ==  null  )  { 
int   nx  =  10  ; 
double   xStep  =  0.0  ; 
int   numfracdigits  =  0  ; 
int   charwidth  =  _labelFontMetrics  .  stringWidth  (  "8"  )  ; 
int   count  =  0  ; 
while  (  count  ++  <=  10  )  { 
xStep  =  _roundUp  (  (  _xtickMax  -  _xtickMin  )  /  (  double  )  nx  )  ; 
numfracdigits  =  _numFracDigits  (  xStep  )  ; 
int   intdigits  =  _numIntDigits  (  _xtickMax  )  ; 
int   inttemp  =  _numIntDigits  (  _xtickMin  )  ; 
if  (  intdigits  <  inttemp  )  { 
intdigits  =  inttemp  ; 
} 
int   maxlabelwidth  =  charwidth  *  (  numfracdigits  +  2  +  intdigits  )  ; 
int   savenx  =  nx  ; 
nx  =  2  +  width  /  (  maxlabelwidth  +  10  )  ; 
if  (  nx  -  savenx  <=  1  ||  savenx  -  nx  <=  1  )  break  ; 
} 
xStep  =  _roundUp  (  (  _xtickMax  -  _xtickMin  )  /  (  double  )  nx  )  ; 
numfracdigits  =  _numFracDigits  (  xStep  )  ; 
double   xStart  =  xStep  *  Math  .  ceil  (  _xtickMin  /  xStep  )  ; 
for  (  double   xpos  =  xStart  ;  xpos  <=  _xtickMax  ;  xpos  +=  xStep  )  { 
String   xticklabel  =  _formatNum  (  xpos  ,  numfracdigits  )  ; 
xCoord1  =  _ulx  +  (  int  )  (  (  xpos  -  _xtickMin  )  *  _xtickscale  )  ; 
graphics  .  drawLine  (  xCoord1  ,  _uly  ,  xCoord1  ,  yCoord1  )  ; 
graphics  .  drawLine  (  xCoord1  ,  _lry  ,  xCoord1  ,  yCoord2  )  ; 
if  (  _grid  &&  xCoord1  !=  _ulx  &&  xCoord1  !=  _lrx  )  { 
graphics  .  setColor  (  Color  .  lightGray  )  ; 
graphics  .  drawLine  (  xCoord1  ,  yCoord1  ,  xCoord1  ,  yCoord2  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
} 
int   labxpos  =  xCoord1  -  _labelFontMetrics  .  stringWidth  (  xticklabel  )  /  2  ; 
graphics  .  drawString  (  xticklabel  ,  labxpos  ,  _lry  +  3  +  labelheight  )  ; 
} 
}  else  { 
Enumeration   nt  =  _xticks  .  elements  (  )  ; 
Enumeration   nl  =  _xticklabels  .  elements  (  )  ; 
while  (  nl  .  hasMoreElements  (  )  )  { 
String   label  =  (  String  )  nl  .  nextElement  (  )  ; 
double   xpos  =  (  (  Double  )  (  nt  .  nextElement  (  )  )  )  .  doubleValue  (  )  ; 
if  (  xpos  >  _xMax  ||  xpos  <  _xMin  )  continue  ; 
xCoord1  =  _ulx  +  (  int  )  (  (  xpos  -  _xMin  )  *  _xscale  )  ; 
graphics  .  drawLine  (  xCoord1  ,  _uly  ,  xCoord1  ,  yCoord1  )  ; 
graphics  .  drawLine  (  xCoord1  ,  _lry  ,  xCoord1  ,  yCoord2  )  ; 
if  (  _grid  &&  xCoord1  !=  _ulx  &&  xCoord1  !=  _lrx  )  { 
graphics  .  setColor  (  Color  .  lightGray  )  ; 
graphics  .  drawLine  (  xCoord1  ,  yCoord1  ,  xCoord1  ,  yCoord2  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
} 
int   labxpos  =  xCoord1  -  _labelFontMetrics  .  stringWidth  (  label  )  /  2  ; 
graphics  .  drawString  (  label  ,  labxpos  ,  _lry  +  3  +  labelheight  )  ; 
} 
} 
graphics  .  setColor  (  _foreground  )  ; 
if  (  _title  !=  null  )  { 
graphics  .  setFont  (  _titlefont  )  ; 
int   titlex  =  _ulx  +  (  width  -  _titleFontMetrics  .  stringWidth  (  _title  )  )  /  2  ; 
graphics  .  drawString  (  _title  ,  titlex  ,  titley  )  ; 
} 
graphics  .  setFont  (  _labelfont  )  ; 
if  (  _xlabel  !=  null  )  { 
int   labelx  =  _ulx  +  (  width  -  _labelFontMetrics  .  stringWidth  (  _xlabel  )  )  /  2  ; 
graphics  .  drawString  (  _xlabel  ,  labelx  ,  ySPos  )  ; 
} 
int   charcenter  =  2  +  _labelFontMetrics  .  stringWidth  (  "W"  )  /  2  ; 
int   charheight  =  labelheight  ; 
if  (  _ylabel  !=  null  )  { 
int   yl  =  _ylabel  .  length  (  )  ; 
int   starty  =  _uly  +  (  _lry  -  _uly  )  /  2  -  yl  *  charheight  /  2  +  charheight  ; 
for  (  int   i  =  0  ;  i  <  yl  ;  i  ++  )  { 
String   nchar  =  _ylabel  .  substring  (  i  ,  i  +  1  )  ; 
int   cwidth  =  _labelFontMetrics  .  stringWidth  (  nchar  )  ; 
graphics  .  drawString  (  nchar  ,  charcenter  -  cwidth  /  2  ,  starty  )  ; 
starty  +=  charheight  ; 
} 
} 
} 




public   synchronized   void   fillPlot  (  )  { 
if  (  _debug  >  7  )  System  .  out  .  println  (  "PlotBox: fillPlot()"  )  ; 
fillPlot  (  _graphics  )  ; 
} 




public   synchronized   void   fillPlot  (  Graphics   graphics  )  { 
setXRange  (  _xBottom  ,  _xTop  )  ; 
setYRange  (  _yBottom  ,  _yTop  )  ; 
paint  (  graphics  )  ; 
} 






public   Font   getFontByName  (  String   fullfontname  )  { 
String   fontname  =  new   String  (  "helvetica"  )  ; 
int   style  =  Font  .  PLAIN  ; 
int   size  =  12  ; 
StringTokenizer   stoken  =  new   StringTokenizer  (  fullfontname  ,  "-"  )  ; 
if  (  stoken  .  hasMoreTokens  (  )  )  { 
fontname  =  stoken  .  nextToken  (  )  ; 
} 
if  (  stoken  .  hasMoreTokens  (  )  )  { 
String   stylename  =  stoken  .  nextToken  (  )  ; 
if  (  stylename  .  equals  (  "PLAIN"  )  )  { 
style  =  Font  .  PLAIN  ; 
}  else   if  (  stylename  .  equals  (  "BOLD"  )  )  { 
style  =  Font  .  BOLD  ; 
}  else   if  (  stylename  .  equals  (  "ITALIC"  )  )  { 
style  =  Font  .  ITALIC  ; 
}  else  { 
try  { 
size  =  Integer  .  valueOf  (  stylename  )  .  intValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
} 
if  (  stoken  .  hasMoreTokens  (  )  )  { 
try  { 
size  =  Integer  .  valueOf  (  stoken  .  nextToken  (  )  )  .  intValue  (  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
if  (  _debug  >  7  )  System  .  out  .  println  (  "PlotBox: getFontByName: "  +  fontname  +  " "  +  style  +  " "  +  size  )  ; 
return   new   Font  (  fontname  ,  style  ,  size  )  ; 
} 




public   static   Color   getColorByName  (  String   name  )  { 
try  { 
Color   col  =  new   Color  (  Integer  .  parseInt  (  name  ,  16  )  )  ; 
return   col  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
String   names  [  ]  [  ]  =  {  {  "black"  ,  "00000"  }  ,  {  "white"  ,  "ffffff"  }  ,  {  "red"  ,  "ff0000"  }  ,  {  "green"  ,  "00ff00"  }  ,  {  "blue"  ,  "0000ff"  }  }  ; 
for  (  int   i  =  0  ;  i  <  names  .  length  ;  i  ++  )  { 
if  (  name  .  equals  (  names  [  i  ]  [  0  ]  )  )  { 
try  { 
Color   col  =  new   Color  (  Integer  .  parseInt  (  names  [  i  ]  [  1  ]  ,  16  )  )  ; 
return   col  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
} 
return   null  ; 
} 




public   String   getDataurl  (  )  { 
return   _dataurl  ; 
} 



public   URL   getDocumentBase  (  )  { 
return   _documentBase  ; 
} 




public   String   getLegend  (  int   dataset  )  { 
int   idx  =  _legendDatasets  .  indexOf  (  new   Integer  (  dataset  )  ,  0  )  ; 
if  (  idx  !=  -  1  )  { 
return  (  String  )  _legendStrings  .  elementAt  (  idx  )  ; 
}  else  { 
return   null  ; 
} 
} 



public   Dimension   getMinimumSize  (  )  { 
if  (  _debug  >  8  )  System  .  out  .  println  (  "PlotBox: getMinimumSize"  )  ; 
return   new   Dimension  (  _width  ,  _height  )  ; 
} 



public   Dimension   getPreferredSize  (  )  { 
if  (  _debug  >  8  )  System  .  out  .  println  (  "PlotBox: getPreferredSize"  )  ; 
return   new   Dimension  (  _width  ,  _height  )  ; 
} 




public   void   init  (  )  { 
if  (  _debug  >  8  )  System  .  out  .  println  (  "PlotBox: init"  )  ; 
_xticks  =  null  ; 
_xticklabels  =  null  ; 
_yticks  =  null  ; 
_yticklabels  =  null  ; 
_graphics  =  getGraphics  (  )  ; 
if  (  _graphics  ==  null  )  { 
System  .  out  .  println  (  "PlotBox::init(): Internal error: "  +  "_graphic was null, be sure to call show()\n"  +  "before calling init()"  )  ; 
return  ; 
} 
if  (  _foreground  !=  null  )  { 
setForeground  (  _foreground  )  ; 
}  else  { 
_foreground  =  Color  .  black  ; 
} 
if  (  _background  !=  null  )  { 
setBackground  (  _background  )  ; 
}  else  { 
_background  =  Color  .  white  ; 
} 
if  (  _debug  >  6  )  System  .  out  .  println  (  "PlotBox: color = "  +  _foreground  +  " "  +  _background  )  ; 
setLayout  (  new   FlowLayout  (  FlowLayout  .  RIGHT  )  )  ; 
_fillButton  =  new   Button  (  "fill"  )  ; 
add  (  _fillButton  )  ; 
validate  (  )  ; 
if  (  _dataurl  !=  null  )  { 
parseFile  (  _dataurl  ,  _documentBase  )  ; 
} 
} 





public   Dimension   minimumSize  (  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: minimumSize "  +  _width  +  " "  +  _height  )  ; 
return   getMinimumSize  (  )  ; 
} 






public   boolean   mouseDown  (  Event   evt  ,  int   x  ,  int   y  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: mouseDown "  +  x  +  " "  +  y  )  ; 
if  (  y  >  _lry  )  y  =  _lry  ; 
if  (  y  <  _uly  )  y  =  _uly  ; 
if  (  x  >  _lrx  )  x  =  _lrx  ; 
if  (  x  <  _ulx  )  x  =  _ulx  ; 
_zoomx  =  x  ; 
_zoomy  =  y  ; 
return   true  ; 
} 








public   synchronized   boolean   mouseDrag  (  Event   evt  ,  int   x  ,  int   y  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: mouseDrag "  +  x  +  " "  +  y  )  ; 
if  (  _graphics  ==  null  )  { 
System  .  out  .  println  (  "PlotBox.mouseDrag(): Internal error: "  +  "_graphic was null, be sure to call init()"  )  ; 
} 
if  (  y  >  _lry  )  y  =  _lry  ; 
if  (  y  <  _uly  )  y  =  _uly  ; 
if  (  x  >  _lrx  )  x  =  _lrx  ; 
if  (  x  <  _ulx  )  x  =  _ulx  ; 
if  (  (  _zoomx  !=  -  1  ||  _zoomy  !=  -  1  )  )  { 
if  (  _zoomin  ==  false  &&  _zoomout  ==  false  )  { 
if  (  y  <  _zoomy  )  { 
_zoomout  =  true  ; 
_graphics  .  drawRect  (  _zoomx  -  15  ,  _zoomy  -  15  ,  30  ,  30  )  ; 
}  else   if  (  y  >  _zoomy  )  { 
_zoomin  =  true  ; 
} 
} 
if  (  _zoomin  ==  true  )  { 
_graphics  .  setXORMode  (  _background  )  ; 
if  (  (  _zoomxn  !=  -  1  ||  _zoomyn  !=  -  1  )  &&  (  _drawn  ==  true  )  )  { 
int   minx  =  Math  .  min  (  _zoomx  ,  _zoomxn  )  ; 
int   maxx  =  Math  .  max  (  _zoomx  ,  _zoomxn  )  ; 
int   miny  =  Math  .  min  (  _zoomy  ,  _zoomyn  )  ; 
int   maxy  =  Math  .  max  (  _zoomy  ,  _zoomyn  )  ; 
_graphics  .  drawRect  (  minx  ,  miny  ,  maxx  -  minx  ,  maxy  -  miny  )  ; 
} 
if  (  y  >  _zoomy  )  { 
_zoomxn  =  x  ; 
_zoomyn  =  y  ; 
int   minx  =  Math  .  min  (  _zoomx  ,  _zoomxn  )  ; 
int   maxx  =  Math  .  max  (  _zoomx  ,  _zoomxn  )  ; 
int   miny  =  Math  .  min  (  _zoomy  ,  _zoomyn  )  ; 
int   maxy  =  Math  .  max  (  _zoomy  ,  _zoomyn  )  ; 
_graphics  .  drawRect  (  minx  ,  miny  ,  maxx  -  minx  ,  maxy  -  miny  )  ; 
_graphics  .  setPaintMode  (  )  ; 
_drawn  =  true  ; 
return   true  ; 
}  else   _drawn  =  false  ; 
}  else   if  (  _zoomout  ==  true  )  { 
_graphics  .  setXORMode  (  _background  )  ; 
if  (  (  _zoomxn  !=  -  1  ||  _zoomyn  !=  -  1  )  &&  (  _drawn  ==  true  )  )  { 
int   x_diff  =  Math  .  abs  (  _zoomx  -  _zoomxn  )  ; 
int   y_diff  =  Math  .  abs  (  _zoomy  -  _zoomyn  )  ; 
_graphics  .  drawRect  (  _zoomx  -  15  -  x_diff  ,  _zoomy  -  15  -  y_diff  ,  30  +  x_diff  *  2  ,  30  +  y_diff  *  2  )  ; 
} 
if  (  y  <  _zoomy  )  { 
_zoomxn  =  x  ; 
_zoomyn  =  y  ; 
int   x_diff  =  Math  .  abs  (  _zoomx  -  _zoomxn  )  ; 
int   y_diff  =  Math  .  abs  (  _zoomy  -  _zoomyn  )  ; 
_graphics  .  drawRect  (  _zoomx  -  15  -  x_diff  ,  _zoomy  -  15  -  y_diff  ,  30  +  x_diff  *  2  ,  30  +  y_diff  *  2  )  ; 
_graphics  .  setPaintMode  (  )  ; 
_drawn  =  true  ; 
return   true  ; 
}  else   _drawn  =  false  ; 
} 
} 
_graphics  .  setPaintMode  (  )  ; 
return   false  ; 
} 






public   synchronized   boolean   mouseUp  (  Event   evt  ,  int   x  ,  int   y  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: mouseUp"  )  ; 
boolean   handled  =  false  ; 
if  (  (  _zoomin  ==  true  )  &&  (  _drawn  ==  true  )  )  { 
if  (  _zoomxn  !=  -  1  ||  _zoomyn  !=  -  1  )  { 
int   minx  =  Math  .  min  (  _zoomx  ,  _zoomxn  )  ; 
int   maxx  =  Math  .  max  (  _zoomx  ,  _zoomxn  )  ; 
int   miny  =  Math  .  min  (  _zoomy  ,  _zoomyn  )  ; 
int   maxy  =  Math  .  max  (  _zoomy  ,  _zoomyn  )  ; 
_graphics  .  setXORMode  (  _background  )  ; 
_graphics  .  drawRect  (  minx  ,  miny  ,  maxx  -  minx  ,  maxy  -  miny  )  ; 
_graphics  .  setPaintMode  (  )  ; 
if  (  y  >  _lry  )  y  =  _lry  ; 
if  (  y  <  _uly  )  y  =  _uly  ; 
if  (  x  >  _lrx  )  x  =  _lrx  ; 
if  (  x  <  _ulx  )  x  =  _ulx  ; 
if  (  (  Math  .  abs  (  _zoomx  -  x  )  >  5  )  &&  (  Math  .  abs  (  _zoomy  -  y  )  >  5  )  )  { 
double   a  =  _xMin  +  (  _zoomx  -  _ulx  )  /  _xscale  ; 
double   b  =  _xMin  +  (  x  -  _ulx  )  /  _xscale  ; 
if  (  a  <  b  )  setXRange  (  a  ,  b  )  ;  else   setXRange  (  b  ,  a  )  ; 
a  =  _yMax  -  (  _zoomy  -  _uly  )  /  _yscale  ; 
b  =  _yMax  -  (  y  -  _uly  )  /  _yscale  ; 
if  (  a  <  b  )  setYRange  (  a  ,  b  )  ;  else   setYRange  (  b  ,  a  )  ; 
} 
drawPlot  (  _graphics  ,  true  )  ; 
handled  =  true  ; 
} 
}  else   if  (  (  _zoomout  ==  true  )  &&  (  _drawn  ==  true  )  )  { 
_graphics  .  setXORMode  (  _background  )  ; 
int   x_diff  =  Math  .  abs  (  _zoomx  -  _zoomxn  )  ; 
int   y_diff  =  Math  .  abs  (  _zoomy  -  _zoomyn  )  ; 
_graphics  .  drawRect  (  _zoomx  -  15  -  x_diff  ,  _zoomy  -  15  -  y_diff  ,  30  +  x_diff  *  2  ,  30  +  y_diff  *  2  )  ; 
_graphics  .  setPaintMode  (  )  ; 
double   a  =  (  double  )  (  Math  .  abs  (  _zoomx  -  x  )  )  /  30.0  ; 
double   b  =  (  double  )  (  Math  .  abs  (  _zoomy  -  y  )  )  /  30.0  ; 
double   newx1  =  _xMax  +  (  _xMax  -  _xMin  )  *  a  ; 
double   newx2  =  _xMin  -  (  _xMax  -  _xMin  )  *  a  ; 
if  (  newx1  >  _xTop  )  newx1  =  _xTop  ; 
if  (  newx2  <  _xBottom  )  newx2  =  _xBottom  ; 
double   newy1  =  _yMax  +  (  _yMax  -  _yMin  )  *  b  ; 
double   newy2  =  _yMin  -  (  _yMax  -  _yMin  )  *  b  ; 
if  (  newy1  >  _yTop  )  newy1  =  _yTop  ; 
if  (  newy2  <  _yBottom  )  newy2  =  _yBottom  ; 
setXRange  (  newx2  ,  newx1  )  ; 
setYRange  (  newy2  ,  newy1  )  ; 
drawPlot  (  _graphics  ,  true  )  ; 
handled  =  true  ; 
}  else   if  (  _drawn  ==  false  )  { 
drawPlot  (  _graphics  ,  true  )  ; 
handled  =  true  ; 
} 
_drawn  =  false  ; 
_zoomin  =  _zoomout  =  false  ; 
_zoomxn  =  _zoomyn  =  _zoomx  =  _zoomy  =  -  1  ; 
return   handled  ; 
} 





public   void   paint  (  Graphics   graphics  )  { 
if  (  _debug  >  7  )  System  .  out  .  println  (  "PlotBox: paint"  )  ; 
drawPlot  (  graphics  ,  true  )  ; 
} 




public   void   parseFile  (  String   dataurl  )  { 
parseFile  (  dataurl  ,  (  URL  )  null  )  ; 
} 






public   void   parseFile  (  String   dataurl  ,  URL   documentBase  )  { 
DataInputStream   in  ; 
if  (  _debug  >  2  )  System  .  out  .  println  (  "PlotBox: parseFile("  +  dataurl  +  " "  +  documentBase  +  ") _dataurl = "  +  _dataurl  +  " "  +  _documentBase  )  ; 
if  (  dataurl  ==  null  ||  dataurl  .  length  (  )  ==  0  )  { 
in  =  new   DataInputStream  (  System  .  in  )  ; 
}  else  { 
try  { 
URL   url  ; 
if  (  documentBase  ==  null  &&  _documentBase  !=  null  )  { 
documentBase  =  _documentBase  ; 
} 
if  (  documentBase  ==  null  )  { 
url  =  new   URL  (  _dataurl  )  ; 
}  else  { 
try  { 
url  =  new   URL  (  documentBase  ,  dataurl  )  ; 
}  catch  (  NullPointerException   e  )  { 
url  =  new   URL  (  _dataurl  )  ; 
} 
} 
in  =  new   DataInputStream  (  url  .  openStream  (  )  )  ; 
}  catch  (  MalformedURLException   e  )  { 
try  { 
in  =  new   DataInputStream  (  new   FileInputStream  (  dataurl  )  )  ; 
}  catch  (  FileNotFoundException   me  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "File not found: "  +  dataurl  ; 
_errorMsg  [  1  ]  =  me  .  getMessage  (  )  ; 
return  ; 
}  catch  (  SecurityException   me  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "Security Exception: "  +  dataurl  ; 
_errorMsg  [  1  ]  =  me  .  getMessage  (  )  ; 
return  ; 
} 
}  catch  (  IOException   ioe  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "Failure opening URL: "  +  dataurl  ; 
_errorMsg  [  1  ]  =  ioe  .  getMessage  (  )  ; 
return  ; 
} 
} 
_newFile  (  )  ; 
try  { 
if  (  _binary  )  { 
_parseBinaryStream  (  in  )  ; 
}  else  { 
String   line  =  in  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
_parseLine  (  line  )  ; 
line  =  in  .  readLine  (  )  ; 
} 
} 
}  catch  (  MalformedURLException   e  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "Malformed URL: "  +  dataurl  ; 
_errorMsg  [  1  ]  =  e  .  getMessage  (  )  ; 
return  ; 
}  catch  (  IOException   e  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "Failure reading data: "  +  dataurl  ; 
_errorMsg  [  1  ]  =  e  .  getMessage  (  )  ; 
}  catch  (  PlotDataException   e  )  { 
_errorMsg  =  new   String  [  2  ]  ; 
_errorMsg  [  0  ]  =  "Incorrectly formatted plot data in "  +  dataurl  ; 
_errorMsg  [  1  ]  =  e  .  getMessage  (  )  ; 
}  finally  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   me  )  { 
} 
} 
} 





public   Dimension   preferredSize  (  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: preferredSize "  +  _width  +  " "  +  _height  )  ; 
return   getPreferredSize  (  )  ; 
} 



public   void   reshape  (  int   x  ,  int   y  ,  int   width  ,  int   height  )  { 
if  (  _debug  >  9  )  System  .  out  .  println  (  "PlotBox: reshape: "  +  x  +  " "  +  y  +  " "  +  width  +  " "  +  height  )  ; 
_width  =  width  ; 
_height  =  height  ; 
super  .  reshape  (  x  ,  y  ,  _width  ,  _height  )  ; 
} 






public   void   resize  (  int   width  ,  int   height  )  { 
if  (  _debug  >  8  )  System  .  out  .  println  (  "PlotBox: resize"  +  width  +  " "  +  height  )  ; 
_width  =  width  ; 
_height  =  height  ; 
super  .  resize  (  width  ,  height  )  ; 
} 



public   void   setBackground  (  Color   background  )  { 
_background  =  background  ; 
super  .  setBackground  (  _background  )  ; 
} 






public   void   setDebug  (  int   debug  )  { 
_debug  =  debug  ; 
} 



public   void   setForeground  (  Color   foreground  )  { 
_foreground  =  foreground  ; 
super  .  setForeground  (  _foreground  )  ; 
} 




public   void   setBinary  (  boolean   binary  )  { 
_binary  =  binary  ; 
} 



public   void   setDataurl  (  String   dataurl  )  { 
_dataurl  =  dataurl  ; 
} 



public   void   setDocumentBase  (  URL   documentBase  )  { 
_documentBase  =  documentBase  ; 
} 



public   void   setGrid  (  boolean   grid  )  { 
_grid  =  grid  ; 
} 



public   void   setLabelFont  (  String   fullfontname  )  { 
_labelfont  =  getFontByName  (  fullfontname  )  ; 
} 





public   void   setTitle  (  String   title  )  { 
_title  =  title  ; 
} 



public   void   setTitleFont  (  String   fullfontname  )  { 
_titlefont  =  getFontByName  (  fullfontname  )  ; 
_titleFontMetrics  =  getFontMetrics  (  _titlefont  )  ; 
} 






public   void   setXLabel  (  String   label  )  { 
_xlabel  =  label  ; 
} 








public   void   setXRange  (  double   min  ,  double   max  )  { 
if  (  _debug  >  7  )  System  .  out  .  println  (  "PlotBox: setXRange"  )  ; 
_setXRange  (  min  ,  max  )  ; 
_xRangeGiven  =  true  ; 
} 






public   void   setYLabel  (  String   label  )  { 
_ylabel  =  label  ; 
} 








public   void   setYRange  (  double   min  ,  double   max  )  { 
_setYRange  (  min  ,  max  )  ; 
_yRangeGiven  =  true  ; 
} 












protected   void   _drawPoint  (  Graphics   graphics  ,  int   dataset  ,  long   xpos  ,  long   ypos  ,  boolean   clip  )  { 
boolean   pointinside  =  ypos  <=  _lry  &&  ypos  >=  _uly  &&  xpos  <=  _lrx  &&  xpos  >=  _ulx  ; 
if  (  !  pointinside  &&  clip  )  { 
return  ; 
} 
graphics  .  fillRect  (  (  int  )  xpos  -  6  ,  (  int  )  ypos  -  6  ,  6  ,  6  )  ; 
} 



protected   void   _newFile  (  )  { 
} 






protected   void   _parseBinaryStream  (  DataInputStream   in  )  throws   PlotDataException  ,  IOException  { 
throw   new   PlotDataException  (  "Binary data not supported in the"  +  "baseclass"  )  ; 
} 






protected   boolean   _parseLine  (  String   line  )  { 
if  (  _debug  >  20  )  System  .  out  .  println  (  "PlotBox: parseLine "  +  line  )  ; 
String   lcLine  =  new   String  (  line  .  toLowerCase  (  )  )  ; 
if  (  lcLine  .  startsWith  (  "#"  )  )  { 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "titletext:"  )  )  { 
setTitle  (  (  line  .  substring  (  10  )  )  .  trim  (  )  )  ; 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "xlabel:"  )  )  { 
setXLabel  (  (  line  .  substring  (  7  )  )  .  trim  (  )  )  ; 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "ylabel:"  )  )  { 
setYLabel  (  (  line  .  substring  (  7  )  )  .  trim  (  )  )  ; 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "xrange:"  )  )  { 
int   comma  =  line  .  indexOf  (  ","  ,  7  )  ; 
if  (  comma  >  0  )  { 
String   min  =  (  line  .  substring  (  7  ,  comma  )  )  .  trim  (  )  ; 
String   max  =  (  line  .  substring  (  comma  +  1  )  )  .  trim  (  )  ; 
try  { 
Double   dmin  =  new   Double  (  min  )  ; 
Double   dmax  =  new   Double  (  max  )  ; 
setXRange  (  dmin  .  doubleValue  (  )  ,  dmax  .  doubleValue  (  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "yrange:"  )  )  { 
int   comma  =  line  .  indexOf  (  ","  ,  7  )  ; 
if  (  comma  >  0  )  { 
String   min  =  (  line  .  substring  (  7  ,  comma  )  )  .  trim  (  )  ; 
String   max  =  (  line  .  substring  (  comma  +  1  )  )  .  trim  (  )  ; 
try  { 
Double   dmin  =  new   Double  (  min  )  ; 
Double   dmax  =  new   Double  (  max  )  ; 
setYRange  (  dmin  .  doubleValue  (  )  ,  dmax  .  doubleValue  (  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "xticks:"  )  )  { 
boolean   cont  =  true  ; 
_parsePairs  (  line  .  substring  (  7  )  ,  true  )  ; 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "yticks:"  )  )  { 
boolean   cont  =  true  ; 
_parsePairs  (  line  .  substring  (  7  )  ,  false  )  ; 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "grid:"  )  )  { 
if  (  lcLine  .  indexOf  (  "off"  ,  5  )  >=  0  )  { 
_grid  =  false  ; 
}  else  { 
_grid  =  true  ; 
} 
return   true  ; 
} 
if  (  lcLine  .  startsWith  (  "color:"  )  )  { 
if  (  lcLine  .  indexOf  (  "off"  ,  6  )  >=  0  )  { 
_usecolor  =  false  ; 
}  else  { 
_usecolor  =  true  ; 
} 
return   true  ; 
} 
return   false  ; 
} 



protected   void   _setButtonsVisibility  (  boolean   vis  )  { 
if  (  vis  )  { 
_fillButton  .  show  (  )  ; 
}  else  { 
_fillButton  .  hide  (  )  ; 
} 
} 

protected   int   _debug  =  0  ; 

protected   Graphics   _graphics  ; 

protected   double   _yMax  ,  _yMin  ,  _xMax  ,  _xMin  ; 

protected   boolean   _xRangeGiven  =  false  ; 

protected   boolean   _yRangeGiven  =  false  ; 

protected   double   _xBottom  =  Double  .  MAX_VALUE  ; 

protected   double   _xTop  =  -  Double  .  MAX_VALUE  ; 

protected   double   _yBottom  =  Double  .  MAX_VALUE  ; 

protected   double   _yTop  =  -  Double  .  MAX_VALUE  ; 

protected   boolean   _grid  =  true  ; 

protected   Color   _background  =  null  ; 

protected   Color   _foreground  =  null  ; 

protected   int   _topPadding  =  10  ; 

protected   int   _bottomPadding  =  5  ; 

protected   int   _rightPadding  =  10  ; 

protected   int   _leftPadding  =  10  ; 

protected   int   _ulx  ,  _uly  ,  _lrx  ,  _lry  ; 

protected   double   _yscale  ,  _xscale  ; 

protected   boolean   _usecolor  =  true  ; 

protected   static   Color  [  ]  _colors  =  {  new   Color  (  0xff0000  )  ,  new   Color  (  0x0000ff  )  ,  new   Color  (  0x14ff14  )  ,  new   Color  (  0x000000  )  ,  new   Color  (  0xffa500  )  ,  new   Color  (  0x53868b  )  ,  new   Color  (  0xff7f50  )  ,  new   Color  (  0x55bb2f  )  ,  new   Color  (  0x90422d  )  ,  new   Color  (  0xa0a0a0  )  ,  new   Color  (  0x00aaaa  )  }  ; 

protected   int   _width  =  400  ,  _height  =  400  ; 






private   int   _drawLegend  (  Graphics   graphics  ,  int   urx  ,  int   ury  )  { 
graphics  .  setFont  (  _labelfont  )  ; 
FontMetrics   _labelFontMetrics  =  graphics  .  getFontMetrics  (  )  ; 
int   spacing  =  _labelFontMetrics  .  getHeight  (  )  ; 
Enumeration   v  =  _legendStrings  .  elements  (  )  ; 
Enumeration   i  =  _legendDatasets  .  elements  (  )  ; 
int   ypos  =  ury  +  spacing  ; 
int   maxwidth  =  0  ; 
while  (  v  .  hasMoreElements  (  )  )  { 
String   legend  =  (  String  )  v  .  nextElement  (  )  ; 
int   dataset  =  (  (  Integer  )  i  .  nextElement  (  )  )  .  intValue  (  )  ; 
if  (  _usecolor  )  { 
int   color  =  dataset  %  _colors  .  length  ; 
graphics  .  setColor  (  _colors  [  color  ]  )  ; 
} 
_drawPoint  (  graphics  ,  dataset  ,  urx  -  3  ,  ypos  -  3  ,  false  )  ; 
graphics  .  setColor  (  _foreground  )  ; 
int   width  =  _labelFontMetrics  .  stringWidth  (  legend  )  ; 
if  (  width  >  maxwidth  )  maxwidth  =  width  ; 
graphics  .  drawString  (  legend  ,  urx  -  15  -  width  ,  ypos  )  ; 
ypos  +=  spacing  ; 
} 
return   22  +  maxwidth  ; 
} 

private   String   _formatNum  (  double   num  ,  int   numfracdigits  )  { 
double   fudge  =  0.5  ; 
if  (  num  <  0.0  )  fudge  =  -  0.5  ; 
String   numString  =  Double  .  toString  (  num  +  fudge  *  Math  .  pow  (  10.0  ,  -  numfracdigits  )  )  ; 
int   dpt  =  numString  .  lastIndexOf  (  "."  )  ; 
StringBuffer   result  =  new   StringBuffer  (  )  ; 
if  (  dpt  <  0  )  { 
if  (  numfracdigits  <=  0  )  { 
result  .  append  (  numString  )  ; 
return   result  .  toString  (  )  ; 
} 
result  .  append  (  "."  )  ; 
for  (  int   i  =  0  ;  i  <  numfracdigits  ;  i  ++  )  { 
result  .  append  (  "0"  )  ; 
} 
return   result  .  toString  (  )  ; 
}  else  { 
int   shortby  =  numfracdigits  -  (  numString  .  length  (  )  -  dpt  -  1  )  ; 
if  (  shortby  <=  0  )  { 
int   numtocopy  =  dpt  +  numfracdigits  +  1  ; 
if  (  numfracdigits  ==  0  )  { 
numtocopy  -=  1  ; 
} 
result  .  append  (  numString  .  substring  (  0  ,  numtocopy  )  )  ; 
return   result  .  toString  (  )  ; 
}  else  { 
result  .  append  (  numString  )  ; 
for  (  int   i  =  0  ;  i  <  shortby  ;  i  ++  )  { 
result  .  append  (  "0"  )  ; 
} 
return   result  .  toString  (  )  ; 
} 
} 
} 

private   void   _measureFonts  (  )  { 
if  (  _labelfont  ==  null  )  _labelfont  =  new   Font  (  "Helvetica"  ,  Font  .  PLAIN  ,  12  )  ; 
if  (  _superscriptfont  ==  null  )  _superscriptfont  =  new   Font  (  "Helvetica"  ,  Font  .  PLAIN  ,  9  )  ; 
if  (  _titlefont  ==  null  )  _titlefont  =  new   Font  (  "Helvetica"  ,  Font  .  BOLD  ,  14  )  ; 
_labelFontMetrics  =  getFontMetrics  (  _labelfont  )  ; 
_superscriptFontMetrics  =  getFontMetrics  (  _superscriptfont  )  ; 
_titleFontMetrics  =  getFontMetrics  (  _titlefont  )  ; 
} 

private   int   _numFracDigits  (  double   num  )  { 
int   numdigits  =  0  ; 
while  (  numdigits  <=  15  &&  num  !=  Math  .  floor  (  num  )  )  { 
num  *=  10.0  ; 
numdigits  +=  1  ; 
} 
return   numdigits  ; 
} 

private   int   _numIntDigits  (  double   num  )  { 
int   numdigits  =  0  ; 
while  (  numdigits  <=  15  &&  (  int  )  num  !=  0.0  )  { 
num  /=  10.0  ; 
numdigits  +=  1  ; 
} 
return   numdigits  ; 
} 

private   void   _parsePairs  (  String   line  ,  boolean   xtick  )  { 
int   start  =  0  ; 
boolean   cont  =  true  ; 
while  (  cont  )  { 
int   comma  =  line  .  indexOf  (  ","  ,  start  )  ; 
String   pair  ; 
if  (  comma  >  start  )  { 
pair  =  (  line  .  substring  (  start  ,  comma  )  )  .  trim  (  )  ; 
}  else  { 
pair  =  (  line  .  substring  (  start  )  )  .  trim  (  )  ; 
cont  =  false  ; 
} 
int   close  ; 
int   open  =  0  ; 
if  (  pair  .  startsWith  (  "\""  )  )  { 
close  =  pair  .  indexOf  (  "\""  ,  1  )  ; 
open  =  1  ; 
}  else  { 
close  =  pair  .  indexOf  (  " "  )  ; 
} 
if  (  close  >  0  )  { 
String   label  =  pair  .  substring  (  open  ,  close  )  ; 
String   index  =  (  pair  .  substring  (  close  +  1  )  )  .  trim  (  )  ; 
try  { 
double   idx  =  (  Double  .  valueOf  (  index  )  )  .  doubleValue  (  )  ; 
if  (  xtick  )  addXTick  (  label  ,  idx  )  ;  else   addYTick  (  label  ,  idx  )  ; 
}  catch  (  NumberFormatException   e  )  { 
} 
} 
start  =  comma  +  1  ; 
comma  =  line  .  indexOf  (  ","  ,  start  )  ; 
} 
} 

private   double   _roundUp  (  double   val  )  { 
int   exponent  ,  idx  ; 
exponent  =  (  int  )  Math  .  floor  (  Math  .  log  (  val  )  *  _log10scale  )  ; 
val  *=  Math  .  pow  (  10  ,  -  exponent  )  ; 
if  (  val  >  5.0  )  val  =  10.0  ;  else   if  (  val  >  2.0  )  val  =  5.0  ;  else   if  (  val  >  1.0  )  val  =  2.0  ; 
val  *=  Math  .  pow  (  10  ,  exponent  )  ; 
return   val  ; 
} 

private   void   _setXRange  (  double   min  ,  double   max  )  { 
if  (  min  >  max  )  { 
min  =  -  1.0  ; 
max  =  1.0  ; 
}  else   if  (  min  ==  max  )  { 
min  -=  1.0  ; 
max  +=  1.0  ; 
} 
double   largest  =  Math  .  max  (  Math  .  abs  (  min  )  ,  Math  .  abs  (  max  )  )  ; 
_xExp  =  (  int  )  Math  .  floor  (  Math  .  log  (  largest  )  *  _log10scale  )  ; 
if  (  _xExp  >  1  ||  _xExp  <  -  1  )  { 
double   xs  =  1.0  /  Math  .  pow  (  10.0  ,  (  double  )  _xExp  )  ; 
_xtickMin  =  min  *  xs  ; 
_xtickMax  =  max  *  xs  ; 
}  else  { 
_xtickMin  =  min  ; 
_xtickMax  =  max  ; 
_xExp  =  0  ; 
} 
_xMin  =  min  ; 
_xMax  =  max  ; 
} 

private   void   _setYRange  (  double   min  ,  double   max  )  { 
if  (  min  >  max  )  { 
min  =  -  1.0  ; 
max  =  1.0  ; 
}  else   if  (  min  ==  max  )  { 
min  -=  0.1  ; 
max  +=  0.1  ; 
} 
double   largest  =  Math  .  max  (  Math  .  abs  (  min  )  ,  Math  .  abs  (  max  )  )  ; 
_yExp  =  (  int  )  Math  .  floor  (  Math  .  log  (  largest  )  *  _log10scale  )  ; 
if  (  _yExp  >  1  ||  _yExp  <  -  1  )  { 
double   ys  =  1.0  /  Math  .  pow  (  10.0  ,  (  double  )  _yExp  )  ; 
_ytickMin  =  min  *  ys  ; 
_ytickMax  =  max  *  ys  ; 
}  else  { 
_ytickMin  =  min  ; 
_ytickMax  =  max  ; 
_yExp  =  0  ; 
} 
_yMin  =  min  ; 
_yMax  =  max  ; 
} 

private   String   _dataurl  =  null  ; 

private   URL   _documentBase  =  null  ; 

private   boolean   _binary  =  false  ; 

private   double   _ytickMax  ,  _ytickMin  ,  _xtickMax  ,  _xtickMin  ; 

private   int   _yExp  ,  _xExp  ; 

private   double   _ytickscale  ,  _xtickscale  ; 

private   Font   _labelfont  =  null  ,  _superscriptfont  =  null  ,  _titlefont  =  null  ; 

FontMetrics   _labelFontMetrics  =  null  ,  _superscriptFontMetrics  =  null  ,  _titleFontMetrics  =  null  ; 

private   static   final   double   _log10scale  =  1  /  Math  .  log  (  10  )  ; 

private   String   _errorMsg  [  ]  ; 

private   String   _xlabel  ,  _ylabel  ,  _title  ; 

private   Vector   _legendStrings  =  new   Vector  (  )  ; 

private   Vector   _legendDatasets  =  new   Vector  (  )  ; 

private   Vector   _xticks  ,  _xticklabels  ,  _yticks  ,  _yticklabels  ; 

private   Button   _fillButton  ; 

private   int   _zoomx  =  -  1  ; 

private   int   _zoomy  =  -  1  ; 

private   int   _zoomxn  =  -  1  ; 

private   int   _zoomyn  =  -  1  ; 

private   boolean   _zoomin  =  false  ; 

private   boolean   _zoomout  =  false  ; 

private   boolean   _drawn  =  false  ; 
} 

