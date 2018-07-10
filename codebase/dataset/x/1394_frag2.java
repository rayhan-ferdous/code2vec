    public static WorldRegistryImpl getInstance() {

        if (instance == null) {

            try {

                instance = new WorldRegistryImpl();

            } catch (RemoteException e) {

                throw new RuntimeException(e);

            }

        }

        return instance;

    }
