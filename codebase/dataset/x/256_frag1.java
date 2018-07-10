    public static void main(String[] args) {

        threads = new HashSet<ConnectedThreadJava>();

        logger = Logger.getLogger("pckt.Test");

        try {

            fh = new FileHandler("Log.txt");

        } catch (Exception e) {

            logger.warning("Couldn't open file handler for logger: Main");

        }

        logger.addHandler(fh);

        if (!BTInicialization()) {

            System.exit(0);

        }

        ConnectingThreadJava connecting = new ConnectingThreadJava(strmNotf, threads, url);

        connecting.start();

    }
