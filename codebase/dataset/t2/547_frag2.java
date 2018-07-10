    public static synchronized MessageBrokerController getInstance() {

        if (instance == null) {

            instance = new MessageBrokerController();

        }

        return instance;

    }
