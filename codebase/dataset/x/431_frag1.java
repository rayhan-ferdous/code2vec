    public static ClassFinder getInstance() {

        if (instance == null) {

            instance = new ClassFinder();

        }

        return instance;

    }
