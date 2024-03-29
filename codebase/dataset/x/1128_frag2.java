    public static String getBaseURIForLocalOntology(FileInputStream in, Model m) {

        String ret = null;

        ResIterator riter;

        initModelHash();

        m.read(in, "");

        riter = m.listSubjectsWithProperty(RDF.type, OWL.Ontology);

        if (riter.hasNext()) {

            ret = riter.nextResource().toString();

        }

        return ret;

    }
