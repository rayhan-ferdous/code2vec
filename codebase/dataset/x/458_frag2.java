    public static synchronized PluginClassLoader getInstance() {

        if (instance == null) {

            instance = new PluginClassLoader();

        }

        return instance;

    }
