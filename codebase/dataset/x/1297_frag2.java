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
