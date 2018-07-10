package   org  .  melati  .  poem  .  prepro  ; 

import   java  .  util  .  Vector  ; 
import   java  .  io  .  Writer  ; 
import   java  .  io  .  IOException  ; 









public   class   SearchabilityFieldDef   extends   FieldDef  { 












public   SearchabilityFieldDef  (  TableDef   table  ,  String   name  ,  int   displayOrder  ,  Vector   qualifiers  )  throws   IllegalityException  { 
super  (  table  ,  name  ,  "Searchability"  ,  "Integer"  ,  displayOrder  ,  qualifiers  )  ; 
table  .  addImport  (  "org.melati.poem.SearchabilityPoemType"  ,  "table"  )  ; 
table  .  addImport  (  "org.melati.poem.Searchability"  ,  "table"  )  ; 
table  .  addImport  (  "org.melati.poem.Searchability"  ,  "persistent"  )  ; 
} 






protected   void   generateColRawAccessors  (  Writer   w  )  throws   IOException  { 
super  .  generateColRawAccessors  (  w  )  ; 
w  .  write  (  "\n"  +  "          public Object getRaw(Persistent g)\n"  +  "              throws AccessPoemException {\n"  +  "            return (("  +  mainClass  +  ")g).get"  +  suffix  +  "Index();\n"  +  "          }\n"  +  "\n"  )  ; 
w  .  write  (  "          public void setRaw(Persistent g, Object raw)\n"  +  "              throws AccessPoemException {\n"  +  "            (("  +  mainClass  +  ")g).set"  +  suffix  +  "Index(("  +  rawType  +  ")raw);\n"  +  "          }\n"  )  ; 
} 






public   void   generateBaseMethods  (  Writer   w  )  throws   IOException  { 
super  .  generateBaseMethods  (  w  )  ; 
w  .  write  (  "\n /**\n"  +  "  * Retrieves the "  +  suffix  +  " index value \n"  +  "  * of this <code>Persistent</code>.\n"  +  (  (  description  !=  null  )  ?  "  * Field description: \n"  +  DSD  .  javadocFormat  (  2  ,  3  ,  description  )  +  "  * \n"  :  ""  )  +  "  * \n"  +  "  * @generator "  +  "org.melati.poem.prepro.SearchabiltyFieldDef"  +  "#generateBaseMethods \n"  +  "  * @throws AccessPoemException \n"  +  "  *         if the current <code>AccessToken</code> \n"  +  "  *         does not confer read access rights\n"  +  "  * @return the "  +  rawType  +  " "  +  name  +  "\n"  +  "  */\n"  )  ; 
w  .  write  (  "\n"  +  "  public Integer get"  +  suffix  +  "Index()\n"  +  "      throws AccessPoemException {\n"  +  "    readLock();\n"  +  "    return get"  +  suffix  +  "_unsafe();\n"  +  "  }\n"  +  "\n"  )  ; 
w  .  write  (  "\n /**\n"  +  "  * Sets the <code>"  +  suffix  +  "</code> index value, with checking, for this "  +  "<code>Persistent</code>.\n"  +  (  (  description  !=  null  )  ?  "  * Field description: \n"  +  DSD  .  javadocFormat  (  2  ,  3  ,  description  )  +  "  * \n"  :  ""  )  +  "  * \n"  +  "  * @generator "  +  "org.melati.poem.prepro.SearchabiltyFieldDef"  +  "#generateBaseMethods \n"  +  "  * @param raw  the value to set \n"  +  "  * @throws AccessPoemException \n"  +  "  *         if the current <code>AccessToken</code> \n"  +  "  *         does not confer write access rights\n"  +  "  */\n"  )  ; 
w  .  write  (  "  public void set"  +  suffix  +  "Index(Integer raw)\n"  +  "      throws AccessPoemException {\n"  +  "    "  +  tableAccessorMethod  +  "().get"  +  suffix  +  "Column()."  +  "getType().assertValidRaw(raw);\n"  +  "    writeLock();\n"  +  "    set"  +  suffix  +  "_unsafe(raw);\n"  +  "  }\n"  +  "\n"  )  ; 
w  .  write  (  "\n /**\n"  +  "  * Retrieves the "  +  suffix  +  " value \n"  +  "  * of this <code>Persistent</code>.\n"  +  (  (  description  !=  null  )  ?  "  * Field description: \n"  +  DSD  .  javadocFormat  (  2  ,  3  ,  description  )  +  "  * \n"  :  ""  )  +  "  *\n"  +  "  * @generator "  +  "org.melati.poem.prepro.SearchabiltyFieldDef"  +  "#generateBaseMethods \n"  +  "  * @throws AccessPoemException \n"  +  "  *         if the current <code>AccessToken</code> \n"  +  "  *         does not confer read access rights\n"  +  "  * @return the "  +  type  +  "\n"  +  "  */\n"  )  ; 
w  .  write  (  "  public "  +  type  +  " get"  +  suffix  +  "()\n"  +  "      throws AccessPoemException {\n"  +  "    Integer index = get"  +  suffix  +  "Index();\n"  +  "    return index == null ? null :\n"  +  "        Searchability.forIndex(index.intValue());\n"  +  "  }\n"  +  "\n"  )  ; 
w  .  write  (  "\n /**\n"  +  "  * Sets the <code>"  +  suffix  +  "</code> value, with checking, for the "  +  "<code>Persistent</code> argument.\n"  +  (  (  description  !=  null  )  ?  "  * Field description: \n"  +  DSD  .  javadocFormat  (  2  ,  3  ,  description  )  +  "  * \n"  :  ""  )  +  "  * \n"  +  "  * @generator "  +  "org.melati.poem.prepro.SearchabiltyFieldDef"  +  "#generateBaseMethods \n"  +  "  * @param cooked  the value to set \n"  +  "  * @throws AccessPoemException \n"  +  "  *         if the current <code>AccessToken</code> \n"  +  "  *         does not confer write access rights\n"  +  "  */\n"  )  ; 
w  .  write  (  "  public void set"  +  suffix  +  "("  +  type  +  " cooked)\n"  +  "      throws AccessPoemException {\n"  +  "    set"  +  suffix  +  "Index(cooked == null ? null : cooked.index);\n"  +  "  }\n"  )  ; 
} 








public   void   generateJavaDeclaration  (  Writer   w  )  throws   IOException  { 
w  .  write  (  "Integer "  +  name  )  ; 
} 


public   String   poemTypeJava  (  )  { 
return  "new SearchabilityPoemType()"  ; 
} 
} 

