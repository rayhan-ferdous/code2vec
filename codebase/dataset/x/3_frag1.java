    private Properties extractCustomProperties(Plugin csPlugin) {

        Xpp3Dom[] rulesetDoms = null;

        Properties properties = new Properties();

        Object configuration = csPlugin.getConfiguration();

        if (configuration instanceof Xpp3Dom) {

            Xpp3Dom configDom = (Xpp3Dom) configuration;

            Xpp3Dom propertiesLocationDom = configDom.getChild("propertiesLocation");

            if (propertiesLocationDom != null) {

                propertiesLocationDom.getValue();

            }

            Xpp3Dom propertyExpansion = configDom.getChild("propertyExpansion");

            if (propertyExpansion != null) {

                String keyValuePair = propertyExpansion.getValue();

                try {

                    properties.load(new StringInputStream(keyValuePair));

                } catch (IOException e) {

                    console.logError("Failed to parse checkstyle propertyExpansion as properties.");

                }

            }

        }

        return properties;

    }
