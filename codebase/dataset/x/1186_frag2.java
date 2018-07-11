    private static Serializable readFromStream(File f) throws PersistenceException {

        FileInputStream ois = null;

        try {

            ois = new FileInputStream(f);

            XStream xs = new XStream();

            return (Serializable) xs.fromXML(ois);

        } catch (IOException e) {

            throw new PersistenceException("Could not read object from " + f.getAbsolutePath() + ".", e);

        } finally {

            if (ois != null) {

                try {

                    ois.close();

                } catch (IOException e) {

                }

            }

        }

    }
