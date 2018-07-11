        opts.addOption("h", "help", false, "print this message");

        opts.addOption("V", "version", false, "print the version information and exit");

        CommandLine cl = null;

        try {

            cl = new GnuParser().parse(opts, args);

        } catch (ParseException e) {

            exit("dcmhp: " + e.getMessage());

            throw new RuntimeException("unreachable");

        }

        if (cl.hasOption('V')) {

            Package p = DcmHPQR.class.getPackage();

            System.out.println("dcmhpqr v" + p.getImplementationVersion());

            System.exit(0);

        }

        if (cl.hasOption('h') || cl.getArgList().size() != 1) {
