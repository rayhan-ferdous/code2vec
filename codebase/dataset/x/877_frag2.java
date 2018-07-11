            try {

                out.close();

                out = null;

            } catch (IOException ioe) {

                m_logCat.error("Cannot close archive: " + archiveFile, ioe);

            }

        }

        Archive res = new Archive(newArchivePath);

        res.roots = roots;

        res.empty = empty;
