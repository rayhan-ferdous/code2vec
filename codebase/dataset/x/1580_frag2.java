    public static Boolean getBooleanAttribute(Element element, String name, Boolean defaultValue) {

        String valueString = element.getAttribute(name);

        if (valueString.equals("true")) {

            return true;

        } else if (valueString.equals("false")) {

            return false;

        } else {

            return defaultValue;

        }

    }
