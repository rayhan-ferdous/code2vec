    public static synchronized PasswordService getInstance() {

        if (instance == null) {

            instance = new PasswordService();

        }

        return instance;

    }
