    public static void main(String[] args) {

        BasicConfigurator.configure();

        int port = DEFAULT_PORT;

        if (args.length < 1) {

            System.out.println("Usage: ExecutableServer <port>");

        } else {

            try {

                port = Integer.parseInt(args[0]);

            } catch (NumberFormatException e) {

            }

        }

        logger.info("ExecutableServer at port " + port);

        (new ExecutableServer(port)).run();

    }
