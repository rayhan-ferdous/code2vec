    public void loadEnvironmentProperties() {

        environmentProperties = new Properties();

        try {

            environmentProperties.load(ClassLoader.getSystemResourceAsStream(RESOURCES_ENVIRONMENT_PROPERTIES));

            originalProperties = (Properties) environmentProperties.clone();

        } catch (IOException e) {

            e.printStackTrace();

            log.error("Couldn't load environemnt properties", e);

        }

    }
