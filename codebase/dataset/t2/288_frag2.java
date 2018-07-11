    public void exportWorkspace() throws Exception {

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(new WebdavFile(new HttpsURL("https://ggxg.ath.cx/webdav/workspace.rdf")));

            repository.getConnection().export(new RDFXMLPrettyWriter(fos));

        } catch (Exception ex) {

            throw ex;

        } finally {

            if (fos != null) fos.close();

        }

    }
