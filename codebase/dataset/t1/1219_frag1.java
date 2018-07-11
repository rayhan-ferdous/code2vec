    private static Properties loadConfig() {

        InputStream in = DcmDir.class.getResourceAsStream("dcmdir.cfg");

        try {

            Properties retval = new Properties();

            retval.load(in);

            return retval;

        } catch (Exception e) {

            throw new RuntimeException("Could not read dcmdir.cfg", e);

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException ignore) {

                }

            }

        }

    }
