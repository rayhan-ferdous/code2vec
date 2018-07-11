    public void exportCatalogAsFile() throws Exception {

        FileOutputStream fileWriter = null;

        try {

            File catalogFile = new File("serialization.rdf");

            fileWriter = new FileOutputStream(catalogFile);

            repository.getConnection().export(new RDFXMLPrettyWriter(fileWriter));

        } catch (Exception ex) {

            throw ex;

        } finally {

            if (fileWriter != null) fileWriter.close();

        }

    }
