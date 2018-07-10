package   com  .  tms  .  webservices  .  applications  .  xtvd  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  Reader  ; 
import   java  .  sql  .  Connection  ; 
import   java  .  sql  .  PreparedStatement  ; 
import   java  .  sql  .  Statement  ; 
import   java  .  sql  .  SQLException  ; 
import   java  .  sql  .  Timestamp  ; 
import   java  .  util  .  Iterator  ; 
import   javax  .  xml  .  stream  .  XMLInputFactory  ; 
import   javax  .  xml  .  stream  .  XMLStreamException  ; 
import   javax  .  xml  .  stream  .  XMLStreamReader  ; 
import   com  .  tms  .  webservices  .  applications  .  DataDirectException  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  Date  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  DateTime  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  Duration  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  CrewMember  ; 
import   com  .  tms  .  webservices  .  applications  .  datatypes  .  MovieAdvisories  ; 













public   class   RDBMSParser   extends   AbstractParser  { 








public   static   final   String   STATION_STATEMENT  =  "insert into stations columns( id, call_sign, name, affiliate, fcc_channel_number ) values( ?, ?, ?, ?, ? )"  ; 








public   static   final   String   LINEUP_STATEMENT  =  "insert into lineups columns( id, name, location, type, device, postalCode ) values( ?, ?, ?, ?, ?, ? )"  ; 








public   static   final   String   MAP_STATEMENT  =  "insert into lineup_map columns( lineup, station, channel, channel_minor, from, to ) values( ?, ?, ?, ?, ?, ? )"  ; 








public   static   final   String   SCHEDULE_STATEMENT  =  "insert into schedules columns( program, station, time, duration, tv_rating, repeat, stereo, subtitled, hdtv, close_captioned, part_number, part_total ) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"  ; 








public   static   final   String   PROGRAM_STATEMENT  =  "insert into programs columns( id, series, title, subtitle, description, mpaa_rating, star_rating, runtime, year, show_type, color_code, syn_epi_num, original_air_date ) values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"  ; 








public   static   final   String   PROGRAM_MOVIE_ADVISORY_STATEMENT  =  "insert into program_movie_advisories columns( program, movie_advisory, ranking ) values( ?, ?, ? )"  ; 







public   static   final   String   CREW_STATEMENT  =  "insert into crew columns( program, role, given_name, surname ) values( ?, ?, ?, ? )"  ; 








public   static   final   String   GENRE_STATEMENT  =  "insert into program_genre columns( program, genre, relevance ) values( ?, ?, ? )"  ; 




Connection   connection  =  null  ; 





















protected   RDBMSParser  (  Reader   in  ,  Connection   connection  )  throws   DataDirectException  { 
super  (  )  ; 
try  { 
reader  =  XMLInputFactory  .  newInstance  (  )  .  createXMLStreamReader  (  in  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
} 
this  .  connection  =  connection  ; 
try  { 
connection  .  setAutoCommit  (  false  )  ; 
}  catch  (  SQLException   sex  )  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tError setting autoCommit off in RDBMSParser( Reader, Connection )."  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
log  .  write  (  DataDirectException  .  getStackTraceString  (  sex  )  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
} 
} 















public   void   parseXTVD  (  )  throws   DataDirectException  { 
try  { 
toStartTag  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "stations"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parseStations  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "lineups"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parseLineups  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "schedules"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parseSchedules  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "programs"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parsePrograms  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "productionCrew"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parseProductionCrew  (  )  ; 
while  (  !  reader  .  getLocalName  (  )  .  equals  (  "genres"  )  )  { 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
} 
parseGenres  (  )  ; 
while  (  reader  .  hasNext  (  )  )  reader  .  next  (  )  ; 
reader  .  close  (  )  ; 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tFinished parsing XTVD document"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  .  getMessage  (  )  ,  xsex  )  ; 
} 
} 













protected   void   parseStations  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing stations top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   statement  =  null  ; 
try  { 
statement  =  connection  .  prepareStatement  (  STATION_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
while  (  reader  .  getLocalName  (  )  .  equals  (  "station"  )  )  { 
Station   station  =  getStation  (  )  ; 
statement  .  setInt  (  1  ,  station  .  getId  (  )  )  ; 
statement  .  setString  (  2  ,  station  .  getCallSign  (  )  )  ; 
statement  .  setString  (  3  ,  station  .  getName  (  )  )  ; 
statement  .  setString  (  4  ,  station  .  getAffiliate  (  )  )  ; 
statement  .  setInt  (  5  ,  station  .  getFccChannelNumber  (  )  )  ; 
statement  .  addBatch  (  )  ; 
} 
statement  .  executeBatch  (  )  ; 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  statement  )  ; 
} 
} 










protected   void   parseLineups  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing lineups top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   lineupStatement  =  null  ; 
PreparedStatement   mapStatement  =  null  ; 
try  { 
lineupStatement  =  connection  .  prepareStatement  (  RDBMSParser  .  LINEUP_STATEMENT  )  ; 
mapStatement  =  connection  .  prepareStatement  (  RDBMSParser  .  MAP_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
while  (  reader  .  getLocalName  (  )  .  equals  (  "lineup"  )  )  { 
Lineup   lineup  =  getLineup  (  )  ; 
lineupStatement  .  setString  (  1  ,  lineup  .  getId  (  )  )  ; 
lineupStatement  .  setString  (  2  ,  lineup  .  getName  (  )  )  ; 
lineupStatement  .  setString  (  3  ,  lineup  .  getLocation  (  )  )  ; 
lineupStatement  .  setString  (  4  ,  lineup  .  getType  (  )  .  toString  (  )  )  ; 
lineupStatement  .  setString  (  5  ,  lineup  .  getDevice  (  )  )  ; 
lineupStatement  .  setString  (  6  ,  lineup  .  getPostalCode  (  )  )  ; 
lineupStatement  .  addBatch  (  )  ; 
for  (  Iterator   iterator  =  lineup  .  getMap  (  )  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
Map   map  =  (  Map  )  iterator  .  next  (  )  ; 
mapStatement  .  setString  (  1  ,  lineup  .  getId  (  )  )  ; 
mapStatement  .  setInt  (  2  ,  map  .  getStation  (  )  )  ; 
mapStatement  .  setString  (  3  ,  map  .  getChannel  (  )  )  ; 
mapStatement  .  setInt  (  4  ,  map  .  getChannelMinor  (  )  )  ; 
mapStatement  .  setDate  (  5  ,  new   java  .  sql  .  Date  (  map  .  getFrom  (  )  .  getDate  (  )  .  getTime  (  )  )  )  ; 
mapStatement  .  setDate  (  6  ,  new   java  .  sql  .  Date  (  map  .  getTo  (  )  .  getDate  (  )  .  getTime  (  )  )  )  ; 
mapStatement  .  addBatch  (  )  ; 
} 
} 
lineupStatement  .  executeBatch  (  )  ; 
mapStatement  .  executeBatch  (  )  ; 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  lineupStatement  )  ; 
closeStatement  (  mapStatement  )  ; 
} 
} 














protected   void   parseSchedules  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing schedules top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   statement  =  null  ; 
try  { 
statement  =  connection  .  prepareStatement  (  RDBMSParser  .  SCHEDULE_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
int   count  =  0  ; 
while  (  reader  .  getLocalName  (  )  .  equals  (  "schedule"  )  )  { 
Schedule   schedule  =  getSchedule  (  )  ; 
statement  .  setString  (  1  ,  schedule  .  getProgram  (  )  )  ; 
statement  .  setInt  (  2  ,  schedule  .  getStation  (  )  )  ; 
statement  .  setTimestamp  (  3  ,  new   Timestamp  (  schedule  .  getTime  (  )  .  getDate  (  )  .  getTime  (  )  )  )  ; 
statement  .  setString  (  4  ,  schedule  .  getDuration  (  )  .  toString  (  )  )  ; 
statement  .  setInt  (  5  ,  schedule  .  getTvRating  (  )  .  hashCode  (  )  )  ; 
if  (  schedule  .  getRepeat  (  )  )  { 
statement  .  setString  (  6  ,  "Y"  )  ; 
}  else  { 
statement  .  setString  (  6  ,  "N"  )  ; 
} 
if  (  schedule  .  getStereo  (  )  )  { 
statement  .  setString  (  7  ,  "Y"  )  ; 
}  else  { 
statement  .  setString  (  7  ,  "N"  )  ; 
} 
if  (  schedule  .  getSubtitled  (  )  )  { 
statement  .  setString  (  8  ,  "Y"  )  ; 
}  else  { 
statement  .  setString  (  8  ,  "N"  )  ; 
} 
if  (  schedule  .  getHdtv  (  )  )  { 
statement  .  setString  (  9  ,  "Y"  )  ; 
}  else  { 
statement  .  setString  (  9  ,  "N"  )  ; 
} 
if  (  schedule  .  getCloseCaptioned  (  )  )  { 
statement  .  setString  (  10  ,  "Y"  )  ; 
}  else  { 
statement  .  setString  (  10  ,  "N"  )  ; 
} 
if  (  schedule  .  getPart  (  )  !=  null  )  { 
statement  .  setInt  (  11  ,  schedule  .  getPart  (  )  .  getNumber  (  )  )  ; 
statement  .  setInt  (  12  ,  schedule  .  getPart  (  )  .  getTotal  (  )  )  ; 
}  else  { 
statement  .  setInt  (  11  ,  0  )  ; 
statement  .  setInt  (  12  ,  0  )  ; 
} 
statement  .  addBatch  (  )  ; 
++  count  ; 
if  (  count  ==  100  )  { 
count  =  0  ; 
statement  .  executeBatch  (  )  ; 
statement  .  clearBatch  (  )  ; 
} 
} 
if  (  count  >  0  )  { 
statement  .  executeBatch  (  )  ; 
} 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  statement  )  ; 
} 
} 










protected   void   parsePrograms  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing programs top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   programStatement  =  null  ; 
PreparedStatement   advisoryStatement  =  null  ; 
try  { 
programStatement  =  connection  .  prepareStatement  (  RDBMSParser  .  PROGRAM_STATEMENT  )  ; 
advisoryStatement  =  connection  .  prepareStatement  (  RDBMSParser  .  PROGRAM_MOVIE_ADVISORY_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
int   count  =  0  ; 
while  (  reader  .  getLocalName  (  )  .  equals  (  "program"  )  )  { 
Program   program  =  getProgram  (  )  ; 
programStatement  .  setString  (  1  ,  program  .  getId  (  )  )  ; 
programStatement  .  setString  (  2  ,  program  .  getSeries  (  )  )  ; 
programStatement  .  setString  (  3  ,  program  .  getTitle  (  )  )  ; 
programStatement  .  setString  (  4  ,  program  .  getSubtitle  (  )  )  ; 
programStatement  .  setString  (  5  ,  program  .  getDescription  (  )  )  ; 
programStatement  .  setInt  (  6  ,  program  .  getMpaaRating  (  )  .  hashCode  (  )  )  ; 
programStatement  .  setInt  (  7  ,  program  .  getStarRating  (  )  .  hashCode  (  )  )  ; 
programStatement  .  setString  (  8  ,  program  .  getRunTime  (  )  .  toString  (  )  )  ; 
programStatement  .  setString  (  9  ,  program  .  getYear  (  )  )  ; 
programStatement  .  setString  (  10  ,  program  .  getShowType  (  )  )  ; 
programStatement  .  setString  (  11  ,  program  .  getColorCode  (  )  )  ; 
programStatement  .  setString  (  12  ,  program  .  getSyndicatedEpisodeNumber  (  )  )  ; 
programStatement  .  setDate  (  13  ,  new   java  .  sql  .  Date  (  program  .  getOriginalAirDate  (  )  .  getDate  (  )  .  getTime  (  )  )  )  ; 
programStatement  .  addBatch  (  )  ; 
int   ranking  =  0  ; 
if  (  !  program  .  getAdvisories  (  )  .  isEmpty  (  )  )  { 
for  (  Iterator   iterator  =  program  .  getAdvisories  (  )  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
MovieAdvisories   advisory  =  (  MovieAdvisories  )  iterator  .  next  (  )  ; 
++  ranking  ; 
advisoryStatement  .  setString  (  1  ,  program  .  getId  (  )  )  ; 
advisoryStatement  .  setInt  (  2  ,  advisory  .  hashCode  (  )  )  ; 
advisoryStatement  .  setInt  (  3  ,  ranking  )  ; 
advisoryStatement  .  addBatch  (  )  ; 
} 
} 
++  count  ; 
if  (  count  ==  50  )  { 
count  =  0  ; 
programStatement  .  executeBatch  (  )  ; 
programStatement  .  clearBatch  (  )  ; 
advisoryStatement  .  executeBatch  (  )  ; 
advisoryStatement  .  clearBatch  (  )  ; 
} 
} 
if  (  count  >  0  )  { 
programStatement  .  executeBatch  (  )  ; 
advisoryStatement  .  executeBatch  (  )  ; 
} 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  programStatement  )  ; 
closeStatement  (  advisoryStatement  )  ; 
} 
} 










protected   void   parseProductionCrew  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing productionCrew top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   statement  =  null  ; 
try  { 
statement  =  connection  .  prepareStatement  (  RDBMSParser  .  CREW_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
int   count  =  0  ; 
while  (  reader  .  getLocalName  (  )  .  equals  (  "crew"  )  )  { 
Crew   crew  =  getCrew  (  )  ; 
if  (  !  crew  .  getMember  (  )  .  isEmpty  (  )  )  { 
for  (  Iterator   iterator  =  crew  .  getMember  (  )  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
CrewMember   member  =  (  CrewMember  )  iterator  .  next  (  )  ; 
statement  .  setString  (  1  ,  crew  .  getProgram  (  )  )  ; 
statement  .  setString  (  2  ,  member  .  getRole  (  )  )  ; 
statement  .  setString  (  3  ,  member  .  getGivenname  (  )  )  ; 
statement  .  setString  (  4  ,  member  .  getSurname  (  )  )  ; 
statement  .  addBatch  (  )  ; 
} 
} 
++  count  ; 
if  (  count  ==  50  )  { 
count  =  0  ; 
statement  .  executeBatch  (  )  ; 
statement  .  clearBatch  (  )  ; 
} 
} 
if  (  count  >  0  )  { 
statement  .  executeBatch  (  )  ; 
} 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  statement  )  ; 
} 
} 










protected   void   parseGenres  (  )  throws   DataDirectException  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tParsing genres top-level element"  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
PreparedStatement   statement  =  null  ; 
try  { 
statement  =  connection  .  prepareStatement  (  RDBMSParser  .  GENRE_STATEMENT  )  ; 
reader  .  next  (  )  ; 
toStartTag  (  )  ; 
int   count  =  0  ; 
while  (  reader  .  hasNext  (  )  &&  reader  .  getLocalName  (  )  .  equals  (  "programGenre"  )  )  { 
ProgramGenre   programGenre  =  getProgramGenre  (  )  ; 
for  (  Iterator   iterator  =  programGenre  .  getGenre  (  )  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
Genre   genre  =  (  Genre  )  iterator  .  next  (  )  ; 
statement  .  setString  (  1  ,  programGenre  .  getProgram  (  )  )  ; 
statement  .  setString  (  2  ,  genre  .  getClassValue  (  )  )  ; 
statement  .  setInt  (  3  ,  genre  .  getRelevance  (  )  )  ; 
statement  .  addBatch  (  )  ; 
} 
++  count  ; 
if  (  count  ==  100  )  { 
count  =  0  ; 
statement  .  executeBatch  (  )  ; 
statement  .  clearBatch  (  )  ; 
} 
} 
if  (  count  >  0  )  { 
statement  .  executeBatch  (  )  ; 
statement  .  clearBatch  (  )  ; 
} 
connection  .  commit  (  )  ; 
}  catch  (  XMLStreamException   xsex  )  { 
throw   new   DataDirectException  (  xsex  )  ; 
}  catch  (  SQLException   sex  )  { 
throw   new   DataDirectException  (  sex  )  ; 
}  finally  { 
closeStatement  (  statement  )  ; 
} 
} 









protected   void   closeStatement  (  Statement   statement  )  { 
try  { 
if  (  statement  !=  null  )  { 
statement  .  clearBatch  (  )  ; 
} 
}  catch  (  SQLException   sex  )  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tError clearing batch statements in RDBMSParser.closeStatement."  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
log  .  write  (  DataDirectException  .  getStackTraceString  (  sex  )  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
} 
try  { 
if  (  statement  !=  null  )  { 
statement  .  close  (  )  ; 
} 
}  catch  (  SQLException   sex  )  { 
try  { 
log  .  write  (  sdf  .  format  (  new   java  .  util  .  Date  (  )  )  )  ; 
log  .  write  (  "\tError closing statement in RDBMSParser.closeStatement."  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
log  .  write  (  DataDirectException  .  getStackTraceString  (  sex  )  )  ; 
log  .  write  (  Parser  .  END_OF_LINE  )  ; 
}  catch  (  IOException   ioex  )  { 
ioex  .  printStackTrace  (  )  ; 
} 
} 
} 
} 

