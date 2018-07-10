    public static Properties changeKeysToLowerCase(Properties p1) {

        Properties p2 = new Properties();

        for (Enumeration enum0 = p1.propertyNames(); enum0.hasMoreElements(); ) {

            String s = (String) enum0.nextElement();

            String v = p1.getProperty(s);

            p2.setProperty(s.toLowerCase(), v);

        }

        return p2;

    }
