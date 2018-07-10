    public static PeerserverProperties getInstance() {

        if (instance == null) {

            instance = new PeerserverProperties();

        }

        return instance;

    }
