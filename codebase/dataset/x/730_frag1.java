                out.write("    }\n\n");

                out.write("    @Override\n");

                out.write("    public void setName(DmcObjectName n) throws DmcValueException {\n");

                out.write("        if (myName == null);\n");

                out.write("            myName = new  DmcTypeStringNameSV(MetaDMSAG.__name);\n");

                out.write("        myName.set(n);\n");

                out.write("    }\n\n");

                out.write("    @Override\n");

                out.write("    public DmcAttribute<?> getObjectNameAttribute(){\n");

                out.write("         return(myName);\n");

                out.write("    }\n\n");
