    public static void main(String argv[]) {

        long uid = -1;

        int optind;

        for (optind = 0; optind < argv.length; optind++) {

            if (argv[optind].equals("-T")) {

                protocol = argv[++optind];

            } else if (argv[optind].equals("-H")) {

                host = argv[++optind];

            } else if (argv[optind].equals("-U")) {

                user = argv[++optind];

            } else if (argv[optind].equals("-P")) {

                password = argv[++optind];

            } else if (argv[optind].equals("-v")) {

                verbose = true;

            } else if (argv[optind].equals("-f")) {

                mbox = argv[++optind];

            } else if (argv[optind].equals("-L")) {

                url = argv[++optind];

            } else if (argv[optind].equals("--")) {

                optind++;

                break;

            } else if (argv[optind].startsWith("-")) {

                System.out.println("Usage: uidmsgshow [-L url] [-T protocol] [-H host] [-U user] [-P password] [-f mailbox] [uid] [-v]");

                System.exit(1);

            } else {

                break;

            }

        }

        try {

            if (optind < argv.length) uid = Long.parseLong(argv[optind]);

            Properties props = System.getProperties();

            Session session = Session.getInstance(props, null);

            Store store = null;

            if (url != null) {

                URLName urln = new URLName(url);

                store = session.getStore(urln);

                store.connect();

            } else {

                if (protocol != null) store = session.getStore(protocol); else store = session.getStore();

                if (host != null || user != null || password != null) store.connect(host, user, password); else store.connect();

            }

            Folder folder = store.getDefaultFolder();

            if (folder == null) {

                System.out.println("No default folder");

                System.exit(1);

            }

            folder = folder.getFolder(mbox);

            if (!folder.exists()) {

                System.out.println(mbox + "  does not exist");

                System.exit(1);

            }

            if (!(folder instanceof UIDFolder)) {

                System.out.println("This Provider or this folder does not support UIDs");

                System.exit(1);

            }

            UIDFolder ufolder = (UIDFolder) folder;

            folder.open(Folder.READ_WRITE);

            int totalMessages = folder.getMessageCount();

            if (totalMessages == 0) {

                System.out.println("Empty folder");

                folder.close(false);

                store.close();

                System.exit(1);

            }

            if (verbose) {

                int newMessages = folder.getNewMessageCount();

                System.out.println("Total messages = " + totalMessages);

                System.out.println("New messages = " + newMessages);

                System.out.println("-------------------------------");

            }

            if (uid == -1) {

                Message[] msgs = ufolder.getMessagesByUID(1, UIDFolder.LASTUID);

                FetchProfile fp = new FetchProfile();

                fp.add(FetchProfile.Item.ENVELOPE);

                fp.add(FetchProfile.Item.FLAGS);

                fp.add("X-Mailer");

                folder.fetch(msgs, fp);

                for (int i = 0; i < msgs.length; i++) {

                    System.out.println("--------------------------");

                    System.out.println("MESSAGE UID #" + ufolder.getUID(msgs[i]) + ":");

                    dumpEnvelope(msgs[i]);

                }

            } else {

                System.out.println("Getting message UID: " + uid);

                Message m = ufolder.getMessageByUID(uid);

                if (m != null) dumpPart(m); else System.out.println("This Message does not exist on this folder");

            }

            folder.close(false);

            store.close();

        } catch (Exception ex) {

            System.out.println("Oops, got exception! " + ex.getMessage());

            ex.printStackTrace();

        }

        System.exit(1);

    }
