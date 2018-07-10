    private static DB getInstance() {

        if (_instance == null) {

            synchronized (DB.class) {

                if (_instance == null) {

                    _instance = new DB();

                }

            }

        }

        return _instance;

    }
