    public static Properties loadSoundProperties(Properties prop, String url) {

        InputStream in1;

        if (prop == null) {

            prop = new Properties();

        }

        try {

            in1 = getResourceStream(url);

            prop.load(in1);

            in1.close();

        } catch (Exception e) {

        }

        return prop;

    }
