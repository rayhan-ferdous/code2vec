    public static void main(String args[]) throws XMPPException, IOException {

        Main c = new Main();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String msg;

        XMPPConnection.DEBUG_ENABLED = false;

        c.login("xxxxxxxx@gmail.com", "xxxxxxxx");

        System.out.println("Kivel akarsz chatelni? (ird be hogy bl):");

        String talkTo = br.readLine();

        if (talkTo.equals("bl")) {

            c.displayBuddyList();

            talkTo = br.readLine();

        }

        System.out.println();

        System.out.println("Irhatod az uzeneteket:");

        System.out.println();

        while (!(msg = br.readLine()).equals("exit")) {

            c.sendMessage(msg, talkTo);

        }

        c.disconnect();

        System.exit(0);

    }
