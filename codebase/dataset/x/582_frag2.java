    public java.util.Vector testInitOfMGEDOE(java.util.Vector mgedOntClassNames) {

        java.util.Vector mgedOntClassEntries = new java.util.Vector();

        try {

            java.util.Iterator it = mgedOntClassNames.iterator();

            while (it.hasNext()) {

                String className = (String) it.next();

                MGEDOntologyClassEntry entry = new MGEDOntologyClassEntry(className, oh);

                mgedOntClassEntries.add(entry);

            }

        } catch (java.lang.Exception e) {

            e.printStackTrace();

        }

        return mgedOntClassEntries;

    }
