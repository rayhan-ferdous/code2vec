    public void addTCP(final InetAddress addr, final int port) {

        Thread t;

        t = new Thread(new Runnable() {



            public void run() {

                serveTCP(addr, port);

            }

        });

        t.start();

    }



    public void addUDP(final InetAddress addr, final int port) {

        Thread t;

        t = new Thread(new Runnable() {



            public void run() {

                serveUDP(addr, port);

            }

        });

        t.start();

    }



    public static void main(String[] args) {

        if (args.length > 1) {

            System.out.println("usage: jnamed [conf]");

            System.exit(0);

        }

        jnamed s;

        try {

            String conf;

            if (args.length == 1) conf = args[0]; else conf = "jnamed.conf";

            s = new jnamed(conf);

        } catch (IOException e) {

            System.out.println(e);

        } catch (ZoneTransferException e) {

            System.out.println(e);

        }

    }

}
