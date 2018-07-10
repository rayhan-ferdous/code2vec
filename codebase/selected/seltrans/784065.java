package   ise  .  plugin  .  svn  .  library  ; 

import   java  .  io  .  *  ; 





public   class   FileUtilities  { 





public   static   int   BUFFER_SIZE  =  8192  ; 









public   static   void   copy  (  File   from  ,  File   to  )  throws   Exception  { 
copyFile  (  from  ,  to  )  ; 
} 









public   static   void   copy  (  InputStream   is  ,  File   to  )  throws   Exception  { 
copyToFile  (  is  ,  to  )  ; 
} 










public   static   void   copy  (  InputStream   is  ,  boolean   close  ,  File   to  )  throws   Exception  { 
copyToFile  (  is  ,  close  ,  to  )  ; 
} 









public   static   void   copy  (  InputStream   is  ,  OutputStream   os  )  throws   Exception  { 
copyToStream  (  is  ,  os  )  ; 
} 









public   static   void   copy  (  Reader   r  ,  Writer   w  )  throws   Exception  { 
copyToWriter  (  r  ,  w  )  ; 
} 









public   static   void   copyFile  (  File   from  ,  File   to  )  throws   Exception  { 
if  (  !  from  .  exists  (  )  )  return  ; 
FileInputStream   in  =  new   FileInputStream  (  from  )  ; 
FileOutputStream   out  =  new   FileOutputStream  (  to  )  ; 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
while  (  true  )  { 
bytes_read  =  in  .  read  (  buffer  )  ; 
if  (  bytes_read  ==  -  1  )  break  ; 
out  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
in  .  close  (  )  ; 
} 









public   static   void   copyToFile  (  InputStream   from  ,  File   to  )  throws   Exception  { 
copyToFile  (  from  ,  true  ,  to  )  ; 
} 










public   static   void   copyToFile  (  InputStream   from  ,  boolean   close  ,  File   to  )  throws   Exception  { 
FileOutputStream   out  =  new   FileOutputStream  (  to  )  ; 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
while  (  true  )  { 
bytes_read  =  from  .  read  (  buffer  )  ; 
if  (  bytes_read  ==  -  1  )  break  ; 
out  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
out  .  flush  (  )  ; 
out  .  close  (  )  ; 
if  (  close  )  from  .  close  (  )  ; 
} 









public   static   void   copyToStream  (  InputStream   from  ,  OutputStream   to  )  throws   Exception  { 
byte  [  ]  buffer  =  new   byte  [  BUFFER_SIZE  ]  ; 
int   bytes_read  ; 
while  (  true  )  { 
bytes_read  =  from  .  read  (  buffer  )  ; 
if  (  bytes_read  ==  -  1  )  break  ; 
to  .  write  (  buffer  ,  0  ,  bytes_read  )  ; 
} 
to  .  flush  (  )  ; 
from  .  close  (  )  ; 
} 









public   static   void   copyToWriter  (  Reader   from  ,  Writer   to  )  throws   Exception  { 
char  [  ]  buffer  =  new   char  [  BUFFER_SIZE  ]  ; 
int   chars_read  ; 
while  (  true  )  { 
chars_read  =  from  .  read  (  buffer  )  ; 
if  (  chars_read  ==  -  1  )  break  ; 
to  .  write  (  buffer  ,  0  ,  chars_read  )  ; 
} 
to  .  flush  (  )  ; 
from  .  close  (  )  ; 
} 
} 

