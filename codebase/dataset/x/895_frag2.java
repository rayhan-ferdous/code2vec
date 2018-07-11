    Client() {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {



            @Override

            public void uncaughtException(Thread t, Throwable e) {

                e.printStackTrace(System.out);

            }

        });

        Client.instance = this;

        this.io = new IOFactory(15000);

        this.clientview = new ClientView();

        this.clientview.setVisible(true);

        this.game = null;

    }
