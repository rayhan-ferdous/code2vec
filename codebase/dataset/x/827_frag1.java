    public static ConnexionServicesImpl getInstance() {

        if (cs == null) {

            cs = new ConnexionServicesImpl();

        }

        return cs;

    }
