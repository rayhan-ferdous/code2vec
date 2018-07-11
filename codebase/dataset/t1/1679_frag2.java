    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("dcmdir", args, "c:t:q:a:x:X:z:P:", LONG_OPTS);

        Properties cfg = loadConfig();

        int cmd = 0;

        File dirfile = null;

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 2:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), g.getOptarg());

                    break;

                case 3:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), "<yes>");

                    break;

                case 'c':

                case 't':

                case 'q':

                case 'a':

                case 'x':

                case 'X':

                case 'P':

                case 'z':

                    cmd = c;

                    dirfile = new File(g.getOptarg());

                    break;

                case 'p':

                    patientIDs.add(g.getOptarg());

                    break;

                case 's':

                    studyUIDs.add(g.getOptarg());

                    break;

                case 'e':

                    seriesUIDs.add(g.getOptarg());

                    break;

                case 'o':

                    sopInstUIDs.add(g.getOptarg());

                    break;

                case 'y':

                    putKey(cfg, g.getOptarg());

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }

        }

        if (cmd == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmDir dcmdir = new DcmDir(dirfile, cfg);

            switch(cmd) {

                case 0:

                    exit(messages.getString("missing"), true);

                    break;

                case 'c':

                    dcmdir.create(args, g.getOptind());

                    break;

                case 't':

                    dcmdir.list();

                    break;

                case 'q':

                    dcmdir.query();

                    break;

                case 'a':

                    dcmdir.append(args, g.getOptind());

                    break;

                case 'x':

                case 'X':

                    dcmdir.remove(args, g.getOptind(), cmd == 'X');

                    break;

                case 'z':

                    dcmdir.compact();

                    break;

                case 'P':

                    dcmdir.purge();

                    break;

                default:

                    throw new RuntimeException();

            }

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            exit(e.getMessage(), true);

        }

    }
