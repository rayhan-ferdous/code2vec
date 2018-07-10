    public static void main(String[] args) {

        final SNGenerateNetwork controler = new SNGenerateNetwork();

        Runtime run = Runtime.getRuntime();

        run.addShutdownHook(new Thread() {



            @Override

            public void run() {

                controler.shutdown(true);

            }

        });

        controler.run(args);

        System.exit(0);

    }
