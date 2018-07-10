    public static String getInternationalizedMessage(String key) {

        String value = null;

        try {

            File propertiesFile = new File(PREFERENCES_FILE_PATH);

            InputStream iStream = new FileInputStream(propertiesFile);

            PropertyResourceBundle props = new PropertyResourceBundle(iStream);

            value = (String) props.handleGetObject(key);

            iStream.close();

        } catch (IOException e) {

            e.printStackTrace();

            System.out.println("WARNING: Problems when dealing with internationalization file!");

        }

        return value;

    }
