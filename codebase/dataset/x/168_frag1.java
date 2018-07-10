    private static boolean isJavaRebelAvailable() {

        try {

            JavaRebelIntegration.testAvailability();

            return true;

        } catch (NoClassDefFoundError e) {

            return false;

        }

    }
