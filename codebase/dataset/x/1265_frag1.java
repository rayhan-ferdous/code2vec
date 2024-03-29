    public Properties getOutputProperties() {

        if (localOutputProperties == null) {

            if (executable == null) {

                return new Properties();

            } else {

                localOutputProperties = new Properties(executable.getDefaultOutputProperties());

            }

        }

        Properties newProps = new Properties();

        Enumeration keys = localOutputProperties.propertyNames();

        while (keys.hasMoreElements()) {

            String key = (String) keys.nextElement();

            newProps.setProperty(key, localOutputProperties.getProperty(key));

        }

        return newProps;

    }
