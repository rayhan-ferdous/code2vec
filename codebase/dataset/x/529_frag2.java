    public static EIBConnection getEIBConnection() {

        if (con == null) {

            con = new KNXnetIPConnection();

        }

        logger.debug("KNXnet/IP connection requested");

        return con;

    }
