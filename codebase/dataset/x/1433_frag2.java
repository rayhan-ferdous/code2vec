    public void close() {

        try {

            if (out != null) {

                out.close();

                out = null;

            }

        } catch (IOException ioe) {

            m_logCat.error("Cannot close archive: " + archiveFile, ioe);

        }

    }
