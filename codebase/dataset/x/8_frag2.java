    public static Object loadAndInstantiate(String className, Class source, Class type) {

        try {

            ClassLoader loader = getClassLoader(source);

            Class c = loader.loadClass(className);

            if (type.isAssignableFrom(c)) {

                return c.newInstance();

            }

        } catch (Exception e) {

            return null;

        }

        return null;

    }
