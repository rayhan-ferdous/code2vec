    @SuppressWarnings("unchecked")

    public UnitTestBase(String targetClassName, String testProfileName) {

        String className = null;

        try {

            className = targetClassName;

            Class targetClass = Class.forName(targetClassName);

            className = testProfileName;

            Class testProfile = Class.forName(testProfileName);

            getInstances(targetClass, testProfile);

        } catch (ClassNotFoundException exception) {

            error("Class not found: " + className);

        }

    }
